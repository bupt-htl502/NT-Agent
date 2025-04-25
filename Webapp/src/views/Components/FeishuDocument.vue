<template>
    <div class="feishu-container">
        <div id="feishu-page" class="feishu-page"></div>
    </div>
</template>

<style lang="scss" scoped>
.feishu-container {
    display: flex;
    width: 100%;
    height: 100%;
}

.feishu-page {
    flex: 1;
    with: 100%;
    margin-left: 5px;
    margin-right: 5px;
    margin-top: 5px;
    margin-bottom: 5px;
}
</style>

<script>
import { FeishuApi } from '@/apis/FeishuApi';

export default {
    data() { return {} },
    props: {
        url: { type: String, default: undefined },
    },
    mounted() {
        const script = document.createElement('script')
        script.src = 'https://lf1-cdn-tos.bytegoofy.com/goofy/locl/lark/external_js_sdk/h5-js-sdk-1.1.3.js'
        script.async = true
        script.onload = () => {
            // 执行一些操作，例如初始化该库或者调用一些方法
            this.authPage()
        }
        document.head.appendChild(script)
    },
    methods: {
        authPage() {
            FeishuApi.signature({ "url": this.url })
                .then((response) => {
                    let openId = response.openid
                    let signature = response.signature
                    let appId = response.appid
                    let timestamp = response.timestamp
                    let nonceStr = response.noncestr
                    let url = response.url
                    let jsApiList = response.jsApiList
                    let locale = response.locale
                    let that = this

                    window.webComponent.config({
                        openId, // 当前登录用户的open id，要确保与生成 signature 使用的 user_access_token 相对应，使用 app_access_token 时此项不填。注意：仅云文档组件可使用app_access_token
                        signature, // 签名
                        appId, // 应用 appId
                        timestamp, // 时间戳（毫秒）
                        nonceStr, // 随机字符串
                        url, // 第3步参与加密计算的url,同时也是最终需要访问的url
                        jsApiList, // 指定要使用的组件列表，请根据对应组件的开发文档填写。如云文档组件，填写['DocsComponent']
                        locale, // 指定组件的国际化语言：en-US-英文、zh-CN-中文、ja-JP-日文
                    })
                        .then(() => {
                            // 动态渲染，返回组件实例。
                            let myComponent = window.webComponent.render(
                                'DocsComponent',
                                {
                                    //组件参数
                                    src: url,
                                    minHeight: '500px',
                                    height: '100%', //该参数控制了页面的最大展开高度,不允许为Auto,也不能不写,会导致锚点定位,文本搜索失效
                                    width: '100%',
                                },
                                document.querySelector('#feishu-page'), // 将组件挂在到哪个元素上
                            )
                            // 通过setFeatureConfig方法修改组件的配置属性
                            myComponent.config.setFeatureConfig({
                                HEADER: { enable: false }, //是否显示头部菜单栏
                                LIKE: { enable: false }, //是否显示点赞
                                IMAGE: { maxWidth: 500 }, //图片最大宽度
                                //分享
                                SHARE: {
                                    enable: false, // 是否显示分享按钮
                                    visibleConfig: {
                                        // 区域的显隐配置
                                        invite: false, //邀请区域
                                        shareLink: false, //分享链接区域
                                        shareMethod: false, //分享方式区域
                                    },
                                },
                                //模态窗
                                MODAL: {
                                    innerMask: true, // 有模态窗时， 组件内是否有遮罩
                                    outerMask: {
                                        enable: true, // 有模态窗时， 组件外是否有遮罩
                                        zIndex: 1000, // 遮罩层的z-index
                                    },
                                    offset: {
                                        // 模态窗的偏移量
                                        x: 0,
                                        y: 0,
                                    },
                                },
                                //文档内容
                                CONTENT: {
                                    readonly: true, // 是否只读
                                    padding: [0, 0, 0, 0], // 文档内边距，例：[10, 0, 10, 0] 为上下增加 10 px 边距
                                    maxWidth: 0, //内部最大宽度
                                    titleVisible: true, //标题是否可见
                                    unscrollable: false, //云文档组件是都允许滚动
                                },
                                //评论
                                COMMENT: {
                                    partial: {
                                        //局部评论
                                        enable: false, //是否启用局部评论
                                        open: false, //是否展开局部评论侧栏
                                    },
                                    global: {
                                        //全局评论
                                        enable: false, //是否启用全局评论
                                    },
                                },
                                //侧边栏
                                SIDEBAR: {
                                    borderSide: [false, false, false, false], //侧边栏边框显隐，例：[true, true, false, false] 上、右侧有边框
                                },
                                //目录
                                DIRECTORY: {
                                    enable: true, //是否显示目录
                                    pin: true, //是否固定目录
                                },
                                //文档头部右侧更多菜单
                                MORE_MENU: {
                                    enable: true, //是否显示更多菜单
                                    items: {
                                        //菜单子项
                                        findAndReplaceEnable: true, //是否显示查找替换
                                        makeCopyEnable: false, //是否显示创建副本
                                        applyEditPermissionEnable: false, //是否显示申请编辑权限入口
                                        exportEnable: false, //是否显示导出
                                        documentInfoEnable: false, //是否显示文档详情
                                        editHistoryEnable: false, //是否显示编辑历史'
                                        commentHistoryEnable: false, //是否显示评论历史
                                        translateEnable: false, //是否显示翻译
                                        printEnable: false, //是否显示打印
                                        deleteEnable: false, //是否显示删除
                                    },
                                },
                                FULLSCREEN: { enable: false }, //是否显示全屏按钮
                                COLLABORATOR: { enable: false }, //是否显示协作者列表
                                FOOTER: { enable: false }, //是否显示页脚
                            })
                        })
                })
        },
    },
}
</script>