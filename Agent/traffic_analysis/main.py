import os
from dify_plugin import Plugin, DifyPluginEnv
from dify_plugin.config.config import InstallMethod
from config import Config

plugin = Plugin(DifyPluginEnv(MAX_REQUEST_TIMEOUT=120))

if __name__ == '__main__':
    Config.INSTALL_METHOD=plugin.config.INSTALL_METHOD
    Config.REMOTE_INSTALL_HOST=plugin.config.REMOTE_INSTALL_HOST
    Config.REMOTE_INSTALL_PORT=plugin.config.REMOTE_INSTALL_PORT
    if Config.INSTALL_METHOD==InstallMethod.Remote:
        Config.FILE_BASE_URL=f"http://{Config.REMOTE_INSTALL_HOST}"
    plugin.run()
