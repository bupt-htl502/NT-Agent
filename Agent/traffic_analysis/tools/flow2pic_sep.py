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
        pic_size, pic_num, pcap = tool_parameters['pic_size'], tool_parameters['pic_num'], tool_parameters['pcap']

        # 创建 dpkt.pcap.Reader 对象来解析字节流
        pcap = self.download(pcap)
        pcap = dpkt.pcap.Reader(BytesIO(pcap))
        image_list = []
        for _, pkt in pcap:
            raw_packet_data = pkt
            pixel = np.frombuffer(raw_packet_data, dtype=np.uint8)
            padding_size = pic_size * pic_size - pixel.shape[0]
            if 0 < padding_size:
                padding = np.zeros(padding_size, dtype=np.uint8)
                pixel = np.concat([pixel, padding], axis=0)
            else:
                pixel = pixel[:pic_size * pic_size]
            pixel = pixel.reshape((pic_size, pic_size))
            image = Image.fromarray(pixel, mode='L')
            image_list.append(image)
            if len(image_list) >= pic_num * pic_num:
                # 完成提取pic_num方个数据包对应的字节序列
                break
        image_res = Image.new('L', (pic_num * pic_size, pic_num * pic_size))
        for idx, image in enumerate(image_list):
            row = idx // pic_num
            col = idx % pic_num
            x_offset = col * pic_size
            y_offset = row * pic_size
            image_res.paste(image, (x_offset, y_offset))

        blob = BytesIO()
        image_res.save(blob, format='png')
        yield self.create_blob_message(blob.getvalue(), meta={'mime_type': 'image/png'})
    
    def download(self, file):
        '''修复Dify插件file.blob BUG:https://github.com/langgenius/dify/issues/15460 
        '''
        url = urljoin(Config.FILE_BASE_URL, file.url)
        response = httpx.get(url)
        response.raise_for_status()
        return response.content
