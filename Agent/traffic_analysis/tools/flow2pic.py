from collections.abc import Generator
from typing import Any
from urllib.parse import urljoin
import numpy as np
from PIL import Image

from dify_plugin import Tool
from dify_plugin.entities.tool import ToolInvokeMessage
import httpx
import dpkt
from config import Config
from io import BytesIO

class Flow2PicTool(Tool):
    def _invoke(self, tool_parameters: dict[str, Any]) -> Generator[ToolInvokeMessage]:
        # 解析参数
        width, high, pcap = tool_parameters['width'], tool_parameters['high'], tool_parameters['pcap']

        # 创建 dpkt.pcap.Reader 对象来解析字节流
        pcap = self.download(pcap)
        pcap = dpkt.pcap.Reader(BytesIO(pcap))

        # 提取字节序列
        buf = bytes(0)
        for _, pkt in pcap:
            if width * high <= len(buf):
                break
            length = min(width * high - len(buf), len(pkt))
            buf += pkt[:length]

        # 转换为灰度图像
        pixel = np.frombuffer(buf, dtype=np.uint8)
        padding_size = width * high - pixel.shape[0]
        if 0 < padding_size:
            padding = np.zeros(padding_size, dtype=np.uint8)
            pixel = np.concat([pixel, padding], axis=0)
        pixel = pixel.reshape((width, high))
        image = Image.fromarray(pixel, mode='L')

        # 返回图像
        blob = BytesIO()
        image.save(blob, format='png')
        yield self.create_blob_message(blob.getvalue(), meta={'mime_type': 'image/png'})
    
    def download(self, file):
        '''修复Dify插件file.blob BUG:https://github.com/langgenius/dify/issues/15460 
        '''
        url = urljoin(Config.FILE_BASE_URL, file.url)
        response = httpx.get(url)
        response.raise_for_status()
        return response.content
