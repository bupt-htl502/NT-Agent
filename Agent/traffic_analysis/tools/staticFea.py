import dpkt
from dify_plugin import Tool
from dify_plugin.entities.tool import ToolInvokeMessage
import httpx
from io import BytesIO
from collections.abc import Generator
from urllib.parse import urljoin
from typing import Any
from config import Config

class StaticFeaTool(Tool):
    def _invoke(self, tool_parameters: dict[str, Any]) -> Generator[ToolInvokeMessage]:
        # 目前是根据前端输入的具体特征名返回指定的特征值，是否返回全部内容有待商榷
        pcap, fea_name = tool_parameters['pcap'], tool_parameters['fea_name']
        packet_lengths = []
        packet_timestamps = []
        static_res = {}
        # 创建 dpkt.pcap.Reader 对象来解析字节流
        pcap = self.download(pcap)
        pcap = dpkt.pcap.Reader(BytesIO(pcap))
        ack_num = 0
        psh_num = 0
        for ts, pkt in pcap:
            pkt_sz = len(pkt)
            packet_lengths.append(pkt_sz)
            packet_timestamps.append(ts)
            eth = dpkt.ethernet.Ethernet(pkt)
            ip = eth.data
            tcp = ip.data
            flags = tcp.flags
            ack_num += 1 if (flags // 16) % 2 == 1 else 0
            psh_num += 1 if (flags // 8) % 2 == 1 else 0
        static_res['duration'] = packet_timestamps[-1] - packet_timestamps[0]
        static_res['pkt_num'] = len(packet_lengths)
        static_res['bytes'] = sum(packet_lengths)
        static_res['max_pkt_sz'] = max(packet_lengths)
        static_res['min_pkt_sz'] = min(packet_lengths)
        static_res['avg_pkt_sz'] = static_res['bytes'] / static_res['pkt_num']
        intervals = [(packet_timestamps[i + 1] - packet_timestamps[i]) for i in range(len(packet_timestamps) - 1)]
        static_res['max_time_interval'] = max(intervals)
        static_res['min_time_interval'] = min(intervals)
        static_res['avg_time_interval'] = static_res['duration'] / static_res['pkt_num']
        static_res['pkt_rate'] = static_res['pkt_num'] / static_res['duration']
        static_res['byte_rate'] = static_res['bytes'] / static_res['duration']
        static_res['ack_num'] = ack_num
        static_res['psh_num'] = psh_num
        
        yield self.create_text_message(text=str(static_res[fea_name]))

    def download(self, file):
        '''修复Dify插件file.blob BUG:https://github.com/langgenius/dify/issues/15460 
        '''
        url = urljoin(Config.FILE_BASE_URL, file.url)
        response = httpx.get(url)
        response.raise_for_status()
        return response.content    