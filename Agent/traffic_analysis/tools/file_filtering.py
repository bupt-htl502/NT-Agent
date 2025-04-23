import dpkt
from dify_plugin import Tool
from dify_plugin.entities.tool import ToolInvokeMessage
from typing import Any, Generator
from io import BytesIO
import httpx
from urllib.parse import urljoin
from config import Config

class FileFiltering(Tool):
    def _invoke(self, tool_parameters: dict[str, Any]) -> Generator[ToolInvokeMessage]:
        pcap_file = self.download(tool_parameters['pcap_file'])
        protocol_to_keep = tool_parameters['protocol'].upper()  # 获取指定的协议，转为大写
        
        # 用于存储有效数据包及其时间戳
        filtered_packets = []

        # 解析pcap文件
        pcap = dpkt.pcap.Reader(BytesIO(pcap_file))

        for timestamp, packet in pcap:
            eth = dpkt.ethernet.Ethernet(packet)
            if self.is_protocol_kept(eth, protocol_to_keep):
                filtered_packets.append((timestamp, packet))  # 保留指定协议的数据包

        # 创建新的pcap文件并写入筛选后的数据包
        output_pcap = BytesIO()
        pcap_writer = dpkt.pcap.Writer(output_pcap)
        for timestamp, packet in filtered_packets:
            pcap_writer.writepkt(packet, ts=timestamp)  # 写入数据包

        output_pcap.seek(0)  # 移动到ByteIO的开始位置

        # 返回处理后的pcap文件
        yield ToolInvokeMessage(file=output_pcap.getvalue(), filename="filtered.pcap")

    def is_protocol_kept(self, eth, protocol: str) -> bool:
        """判断数据包是否需要保留，仅支持 IPv4、IPv6、TCP 和 UDP"""
        if protocol == 'IPv4' and isinstance(eth.data, dpkt.ip.IP):
            return True
        elif protocol == 'IPv6' and isinstance(eth.data, dpkt.ipv6.IPV6):
            return True
        elif protocol == 'TCP' and isinstance(eth.data, dpkt.ip.IP) and isinstance(eth.data.data, dpkt.tcp.TCP):
            return True
        elif protocol == 'UDP' and isinstance(eth.data, dpkt.ip.IP) and isinstance(eth.data.data, dpkt.udp.UDP):
            return True
        return False  # 如果不匹配，返回 False

    def download(self, file):
        """下载指定pcap文件"""
        url = urljoin(Config.FILE_BASE_URL, file.url)
        response = httpx.get(url)
        response.raise_for_status()
        return response.content