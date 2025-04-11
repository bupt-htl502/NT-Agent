from collections.abc import Generator
from typing import Any
from urllib.parse import urljoin

from dify_plugin import Tool
from dify_plugin.entities.tool import ToolInvokeMessage
import httpx
from config import Config

class TrafficAnalysisTool(Tool):
    def _invoke(self, tool_parameters: dict[str, Any]) -> Generator[ToolInvokeMessage]:
        print(tool_parameters)
        pcap = tool_parameters["pcap"]
        pcap_content = self.download(pcap)

        yield self.create_json_message({"result": "Hello, world!"})
    
    def download(self, file):
        '''修复Dify插件file.blob BUG:https://github.com/langgenius/dify/issues/15460 
        '''
        url = urljoin(Config.FILE_BASE_URL, file.url)
        response = httpx.get(url)
        response.raise_for_status()
        return response.content
