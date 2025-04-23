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

### 2. Vue前端

### 3. Agent工具集
```bash
.
├── network_protocol_analysis                   # 1. 网络协议解析工具集
│   ├── provider
│   │   ├── network_protocol_analysis.py
│   │   └── network_protocol_analysis.yaml
│   └── tools
│       ├── network_protocol_analysis.py
│       └── network_protocol_analysis.yaml
├── file_processing                             # 2. 流量文件处理工具集
│   ├── provider
│   │   ├── file_processing.py
│   │   └── file_processing.yaml
│   └── tools
│       ├── file_cleaning.py
│       │── file_cleaning.yaml
│       ├── file_filtering.py
│       │── file_filtering.yaml
│       ├── file_splitting.py
│       └── file_splitting.yaml
├── traffic_analysis                            # 3. 流量解析工具集
│   ├── provider
│   │   ├── traffic_analysis.py
│   │   └── traffic_analysis.yaml
│   └── tools
│       ├── flow2pic.py                         ## 3.1 流量转图片工具
│       ├── flow2pic.yaml
│       ├── traffic_analysis.py
│       └── traffic_analysis.yaml
└── traffic_classification                      # 4. 流量分类工具集
    ├── provider
    │   ├── traffic_classification.py
    │   └── traffic_classification.yaml
    └── tools
        ├── traffic_classification.py
        └── traffic_classification.yaml
```