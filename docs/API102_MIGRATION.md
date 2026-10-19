# API 102 重构记录

## 范围与检查点

在原仓库、原包名内迁移；保留用户已有的 `version.properties` 改动，不加入任务提交。

| 单元 | 保留能力与验收方向 |
| --- | --- |
| 核心 | 原生 libxposed API 102 入口、系统/应用分流、Hook 隔离、现代元数据；移除 Yuki 与旧 Xposed 依赖 |
| 配置 | 原键名、应用名单、黑白名单、实时刷新、旧偏好迁移、XML 导入导出、框架状态 |
| 无障碍 | 服务注册/列表、Settings/Provider、应用内扩展查询与节点路径、系统组件保护、高性能开关 |
| 音频 | 模式提交/兜底、路由、蓝牙放行、媒体/通话/SCO 音量同步、听筒处理 |
| 截图 | 官方回调、增强媒体过滤、目标应用作用域、完整 Cursor 遍历 |
| 系统扩展 | DPM 与 ColorOS 最近任务锁；保留方法监视能力，默认关闭 |
| 界面 | iOS 风格分组设置、大标题、浅灰底色、圆角列表、绿色开关、深色模式；保留全部配置和测试 |
| 验证 | 单元测试、Debug/Release 编译、APK 入口/依赖检查、设备验证清单 |

## 参考

- 用户模板：https://github.com/banchengkemeng/LSPosedModuleTemplate （当前模板采用传统 API，结构供参考）
- API 102 源码：https://github.com/JingMatrix/libxposed-api
- 远程偏好与框架服务：https://github.com/JingMatrix/libxposed-service
- 现代模块入口示例：https://github.com/JingMatrix/libxposed-example

## 验证环境

开始时 ADB 无设备连接。构建与本地测试不等同于 ROM 上 Hook 命中或路由验证，交付时分别记录。
