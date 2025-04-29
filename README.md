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
ps: 各组件的访问用户名&密码，参见Server服务端application.properties
- docker version 24.0.2(可选)
- mysql version 8.0
- minio version
    - UI页面 http://10.101.170.78:9000
- Rabbit MQ
    - UI页面 http://10.101.170.78:15672
- dify version 1.1.3
    - UI页面 http://10.101.170.78
- Xinference version
    - UI页面 http://10.101.170.78:9997
### 1. Server服务端
#### 1.1 环境配置
- Java 21
- Spring Boot 3.4.4

#### 1.2 项目结构
```bash
.
├── pom.xml
└── src.main
    ├── java
    │   └── com.coldwindx.server
    │       ├── aop                         # 各种Aop切面
    │       ├── config                      # Spring配置Bean
    │       ├── controller                  # 接口定义
    │       ├── entity                      # 各类实体
    │       │   ├── enums                       # 自定义枚举                 
    │       │   ├── except                      # 自定义异常
    │       │   ├── form                        # 自定义实体类
    │       │   └── query                       # 自定义查询条件
    │       ├── manager                     # 第三方平台 & 子引擎 & 复杂任务拆分
    │       ├── mapper                      # 数据访问接口定义
    │       ├── service                     # 核心逻辑层
    │       ├── listener                    # 消息队列消费者
    │       └── utils                       # 通用工具
    └── resources
        └── mapper                          # 数据访问接口实现
        └── application.properties          # 服务器配置
```
### 2. Vue前端
#### 2.1 启动
```bash 
cd .\Webapp & npm run dev --host 0.0.0.0 --port 5173
```
### 3. Agent工具集
#### 3.1 环境配置
- 见requirements.txt。
- 要求python >= 3.12。
- 请提前安装Dify开发工具脚手架。（服务端请与管理员确认是否已安装工具）
#### 3.1 项目结构
```bash
.
├── network_protocol_analysis                   # 1. 网络协议解析工具集
│   ├── provider
│   └── tools
├── file_processing                             # 2. 流量文件处理工具集
├── traffic_analysis                            # 3. 流量解析工具集
│   └── tools
│       ├── flow2pic.py                         ## 3.1 流量转图片工具
│       ├── flow2pic.yaml
└── traffic_classification                      # 4. 流量分类工具集
```

#### 3.2 启动
以启动流量解析工具集为例。
```bash 
conda activate dify & cd ./Agent/traffic_analysis & python -m main
```