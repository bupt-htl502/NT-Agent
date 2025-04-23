import dpkt
import hashlib
from dify_plugin import Tool
from dify_plugin.entities.tool import ToolInvokeMessage
from typing import Any, List, Tuple
from io import BytesIO
import httpx
from urllib.parse import urljoin
from config import Config
from collections.abc import Generator


class FileCleaning(Tool):
    def _invoke(self, tool_parameters: dict[str, Any]) -> Generator[ToolInvokeMessage]:
        pcap_file = self.download(tool_parameters['pcap_file'])
        # 用于存储有效数据包及其时间戳
        valid_packets: List[Tuple[float, bytes]] = []

        # 解析pcap文件
        pcap = dpkt.pcap.Reader(BytesIO(pcap_file))
        # 存储每个数据包的MD5值，以去重
        unique_packets = set()

        for timestamp, packet in pcap:
            if self.is_valid_packet(packet):  # 检查数据包有效性
                packet_md5 = self.calculate_md5(packet)  # 计算MD5值
                if packet_md5 not in unique_packets:  # 检查是否重复
                    unique_packets.add(packet_md5)  # 添加到唯一集合
                    valid_packets.append((timestamp, packet))  # 保存有效数据包及其时间戳

        # 按时间戳排序有效数据包
        valid_packets.sort(key=lambda x: x[0])

        # 创建新的pcap文件并写入有效数据包
        output_pcap = BytesIO()
        pcap_writer = dpkt.pcap.Writer(output_pcap)
        for timestamp, packet in valid_packets:
            pcap_writer.writepkt(packet, ts=timestamp)  # 写入数据包

        output_pcap.seek(0)  # 移动到ByteIO的开始位置

        # 返回处理后的pcap文件
        yield ToolInvokeMessage(file=output_pcap.getvalue(), filename="cleaned.pcap")

    def calculate_md5(self, packet: bytes) -> str:
        """计算数据包的MD5值"""
        return hashlib.md5(packet).hexdigest()

    def is_valid_packet(self, packet: bytes) -> bool:
        """判断数据包是否有效"""
        eth = dpkt.ethernet.Ethernet(packet)
        # 检查以太网帧的有效性和IP层
        if isinstance(eth.data, dpkt.ip.IP):
            # 检查IP数据包是否完整
            return eth.data.len > 0 and eth.data.total <= len(packet)
        return False

    def download(self, file):
        """下载指定pcap文件"""
        url = urljoin(Config.FILE_BASE_URL, file.url)
        response = httpx.get(url)
        response.raise_for_status()
        return response.content
