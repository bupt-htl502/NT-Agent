import dpkt
from dify_plugin import Tool
from dify_plugin.entities.tool import ToolInvokeMessage
import httpx
from io import BytesIO
from collections.abc import Generator
from urllib.parse import urljoin
from typing import Any
from config import Config
import methods.tlsMap as tlsMap
import pyshark

class StaticFea(Tool):
    def _invoke(self, tool_parameters: dict[str, Any]) -> Generator[ToolInvokeMessage]:
        # 目前是根据前端输入的具体字段返回指定的特征值，是否返回全部内容有待商榷
        pcap, fea_name = tool_parameters['pcap'], tool_parameters['fea_name']
        pcap = self.download(pcap)
        pcap = pyshark.FileCapture(pcap)
        app_res = {}
        pkt_num = 0
        for pkt in pcap:
            layer_res = None
            if pkt_num > 10:
                # 仅对握手包提字段，不考虑后续数据包，直接跳出
                break
            elif len(pkt.layers) > 3:
                # 仅对存在4层的数据包进行分析
                layer_res = self.layer_analyse(pkt)
            if layer_res:
                app_res.update(layer_res)
        yield self.create_text_message(text=app_res[fea_name])

    def download(self, file):
        '''修复Dify插件file.blob BUG:https://github.com/langgenius/dify/issues/15460 
        '''
        url = urljoin(Config.FILE_BASE_URL, file.url)
        response = httpx.get(url)
        response.raise_for_status()
        return response.content    
    
    def layer_analyse(pkt):
        """
        该方法对pyshark解析的数据包进行解析，提取相应字段
        :param pkt: pyshark解析的单个数据包
        :return: 相应字段的字典res
        """
        res = {}
        for layer in pkt.layers[3:]:  # 对三层之后的可能的应用层协议部分进行解析
            # 仅对TLS协议进行分析，若课程需要可以进行拓展
            if layer.layer_name == 'tls':
                # 由于一个数据包内，可能包含多个record，，需要对record类型和数目进行统计
                record_num = 0
                handshake_count = 0  # 握手record计数
                if layer.get_field('record'):
                    record_num = len(layer.get_field('record').fields)
                opaque_num = 0  # TLS1.3加密握手record数目
                if layer.get_field('record_opaque_type'):
                    opaque_num = len(layer.get_field('record_opaque_type').fields)
                unencrypted_record_num = record_num - opaque_num  # 正常情况下pyshark解析的数据包是先明文record再密文record
                for record_idx in range(record_num):
                    if record_idx < unencrypted_record_num:
                        if record_idx < len(layer.get_field('record_content_type').fields):
                            record_content_type_c = layer.get_field('record_content_type').fields[record_idx]
                        else:
                            print('unknown structure')
                            continue
                    # 课程实验暂不处理密文，根据以后需要进行增加
                    else:
                        record_content_type_c = layer.get_field('record_opaque_type').fields[
                            record_idx - unencrypted_record_num]
                    if not record_content_type_c:
                        continue
                    record_content_type = record_content_type_c.show
                    if record_content_type == '22':
                        # 是握手协议
                        if "Encrypted Handshake Message" in layer.get_field('handshake').fields[handshake_count].showname:
                            # 加密信息在pyshark中需要单独处理，handshake之外的handshake前缀的key对应的value是不会包含加密包
                            handshake_count += 1
                            continue
                        if handshake_count < len(layer.get_field('handshake_type').fields):
                            handshake_type = layer.get_field('handshake_type').fields[handshake_count].show
                        else:
                            print('unknown structure')
                            continue
                        if handshake_type == '1':
                            # Client Hello报文
                            res['client_ciphers'] = tlsMap.cipher2str(layer.get_field('handshake_ciphersuite'))
                            if layer.get_field('handshake_sig_hash_alg'):
                                sigHashAlgs = layer.get_field('handshake_sig_hash_alg')
                                res['client_signature_algorithms'] = tlsMap.sigHashAlg2str(sigHashAlgs)
        return res