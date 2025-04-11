# NT-Agent 智能网络流量分析课程智能体
![智能网络流量分析课程智能体](/Sources/nt-agent.png)

## 项目结构
```bash
.
├── Agent           # 智能体相关组件  
├── Server          # 服务端
├── Sources         # 资源文件路径
├── Webapp          # 前端页面
└── README.md
```

## 项目模块
### 0. 前置组件
- docker version 24.0.2(可选)
- mysql version 8.0
- minio version
    - UI页面 http://10.101.170.78:9000
- dify version 1.1.3
    - UI页面 http://10.101.170.78
- Xinference version
    - UI页面 http://10.101.170.78:9997
### 1. Server服务端
- Java 21
- Spring Boot 3.4.4