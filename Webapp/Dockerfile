# 基于轻量的官方 Nginx 镜像
FROM nginx:stable-perl

# 复制本地dist目录下的静态文件到nginx默认目录
COPY dist/ /usr/share/nginx/html

# 复制自定义nginx配置（可选，按需提供）
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露80端口（nginx默认）
EXPOSE 80

# 启动nginx（官方镜像默认入口，无需额外CMD）
