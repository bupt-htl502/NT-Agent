import dpkt
from dify_plugin import Tool
from dify_plugin.entities.tool import ToolInvokeMessage
from typing import Any, Generator, Dict
from io import BytesIO
import httpx
from urllib.parse import urljoin
from collections import defaultdict
from config import Config

class FileSplitting(Tool):
    def _invoke(self, tool_parameters: dict[str, Any]) -> Generator[ToolInvokeMessage]:
        pcap_file = self.download(tool_parameters['pcap_file'])

        # 存储每个网络流的 pcap 数据
        flows: Dict[str, list] = defaultdict(list)

        # 解析pcap文件
        pcap = dpkt.pcap.Reader(BytesIO(pcap_file))

        for timestamp, packet in pcap:
            eth = dpkt.ethernet.Ethernet(packet)

            # 只处理 IPv4 包和 UDP/TCP 数据包
            if isinstance(eth.data, dpkt.ip.IP):
                ip = eth.data
                source_ip = ip.src
                dest_ip = ip.dst

                # 判断传输层协议
                if isinstance(ip.data, dpkt.tcp.TCP):
                    protocol = "TCP"
                    source_port = ip.data.sport
                    dest_port = ip.data.dport
                elif isinstance(ip.data, dpkt.udp.UDP):
                    protocol = "UDP"
                    source_port = ip.data.sport
                    dest_port = ip.data.dport
                else:
                    continue  # 跳过不支持的协议

                # 创建五元组的唯一标识
                flow_key = f"{source_ip}:{source_port}-{dest_ip}:{dest_port}_{protocol}"
                flows[flow_key].append((timestamp, packet))  # 存储包和时间戳

        # 创建输出文件
        for flow_key, packets in flows.items():
            output_pcap = BytesIO()
            pcap_writer = dpkt.pcap.Writer(output_pcap)
            for timestamp, packet in packets:
                pcap_writer.writepkt(packet, ts=timestamp)  # 写入数据包

            output_pcap.seek(0)  # 移动到ByteIO的开始位置
            
            # 返回每个网络流的 pcap 文件
            flow_filename = f"{flow_key.replace(':', '_').replace('-', '_').replace('/', '_')}.pcap"
            yield ToolInvokeMessage(file=output_pcap.getvalue(), filename=flow_filename)

    def download(self, file):
        """下载指定pcap文件"""
        url = urljoin(Config.FILE_BASE_URL, file.url)
        response = httpx.get(url)
        response.raise_for_status()
        return response.content