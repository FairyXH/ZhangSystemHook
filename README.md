# ZhangSystemHook

一个基于 Kotlin、YukiHookAPI 和 Xposed/LSPosed 的 Android 系统增强模块。

本模块主要用于调整系统和应用对无障碍服务状态的读取结果，并提供设备策略、ColorOS 启动器应用锁等附加 Hook。模块只在 Xposed/LSPosed 注入后生效，普通方式直接启动 APK 不会启用系统 Hook。

> 当前项目属于面向特定 Android/ColorOS 版本的实验性系统模块。系统内部类、方法签名和厂商实现可能随 ROM 版本变化，使用前请准备好禁用模块或进入 LSPosED 安全模式的恢复方式。

## 功能

### 已由当前入口加载的功能

- **无障碍防检测**
  - 在 `android` 系统进程中 Hook 无障碍相关系统服务和查询路径。
  - 可对用户应用、系统应用分别使用白名单/黑名单策略。
  - 支持阻止或伪造部分 `AccessibilityManager`、Settings、Provider、PackageManager 查询结果。
  - 支持限制部分应用获取无障碍系统服务、读取已安装/已启用的无障碍服务列表。
  - 默认跳过部分系统关键组件；可选是否阻止以 `android.` 开头的系统组件。
  - 内置高性能路径：默认跳过高频 `AccessibilityNodeInfo`、`AccessibilityEvent` 等节点级 Hook，降低输入法和系统 UI 卡顿风险。

- **禁止App切换通话模式**
  - 在 `android` 系统进程中 Hook `AudioService` / `AudioSystem` 的音频模式与路由链路。
  - 开关开启后所有应用一视同仁：禁止进入通话/通信模式，禁止使用听筒，通信音频强制输出到内置扬声器并按媒体音量播放，音量键仍可正常调节。
  - 多入口、多方案自动适配：覆盖 `setMode`、`setOriginalMode`、`onUpdateAudioMode`/`setModeInt`、`AudioSystem.setPhoneState` 等模式入口，以及 `setCommunicationDevice`、`setPreferredDeviceForStrategy`、`setSpeakerphoneOn` 等路由入口；各方案独立安装，当前 ROM 缺少对应方法时自动跳过并记录日志。
  - 系统层禁用听筒设备：拦截听筒连接请求、向系统报告听筒不可用并主动断开听筒，使路由策略回落到扬声器。
  - 兜底强制回退：检测到通话/通信模式已实际生效时，自动调用 `setMode(MODE_NORMAL)` 切回普通模式。

- **截屏防检测**
  - Android 14+ 使用系统官方 `ScreenCaptureCallback` API 拦截截图检测回调（`Android14ScreenshotBlocker`）。
  - 可选增强模式（`EnhancedScreenshotBlocker`）过滤 MediaStore 中的截图记录与 ContentObserver 通知，不影响普通图片读取。

- **设备策略 Hook**
  - 在 `DevicePolicyManagerService` 中调整账号检查、设备所有者设置前置条件和部分权限校验。
  - 该功能可能影响设备管理、企业策略和系统设置稳定性。

- **ColorOS 启动器 Hook**
  - 仅在检测到 ColorOS 时加载到 `com.android.launcher`。
  - 调整 Oplus/ColorOS 应用锁相关方法，包括最近任务锁数量限制和默认锁判定。

- **模块自检与配置界面**
  - 显示 Xposed/LSPosed 激活状态、模块状态和系统版本。
  - 可检查常见无障碍读取路径是否仍能检测到服务。
  - 支持隐藏桌面启动图标；隐藏后可从 LSPosed 模块设置进入应用。
  - 支持应用搜索、按用户应用/系统应用筛选，以及单独切换应用策略。

### 代码中存在的实验功能

仓库中还包含 `MethodMonitor` 方法监视器实现和相关界面开关。它会尝试为目标类的全部方法安装 Hook，并在调用时输出通知，开销和稳定性风险都很高；当前默认入口没有加载该 Hook，不建议在生产系统启用或自行接入前进行充分测试。

## 配置说明

主界面中的配置项包括：

| 配置 | 说明 |
| --- | --- |
| 无障碍防检测 | 总开关，关闭后不执行无障碍防检测逻辑 |
| 无障碍高性能模式 | 减少部分低收益、高频 Hook；修改后建议重启相关进程或设备 |
| 允许 `addClient` | 允许部分应用注册无障碍客户端，可能降低隐藏效果 |
| 系统应用白名单模式 | 让系统应用也按白名单逻辑处理，而不是默认的系统应用黑名单逻辑 |
| 阻止系统组件访问无障碍 | 允许进一步限制系统组件；可能降低系统稳定性 |
| 显示 Hook 通知 | 将 Hook 过程输出为日志/Toast；高频 Hook 场景不建议开启 |
| 应用列表 | 配置被允许读取无障碍信息的包名集合 |
| 禁止App切换通话模式 | 开启后禁止所有应用进入通话/通信模式、使用听筒，通信音频强制输出到扬声器并按媒体音量播放 |

应用规则的默认行为：

- 用户应用默认按白名单处理，只有加入列表后才允许读取。
- 系统应用默认按黑名单处理，加入列表后会被阻止读取。
- 开启系统应用白名单模式后，系统应用也需要加入列表才能读取。
- `android` 本身默认不执行无障碍防检测 Hook。

## Hook 作用域

模块声明的 Xposed 作用域为：

- `android`
- `com.android.launcher`

因此无障碍和设备策略逻辑主要运行在系统进程，ColorOS 启动器逻辑运行在启动器进程。模块不会自动注入所有第三方应用。

## 环境要求

- Android 8.1 或更高版本（项目 `minSdk = 27`）。
- 已安装并正常工作的 Xposed/LSPosed 框架。
- 推荐 Android 12 或更高版本；无障碍相关部分使用了 Android 12（API 31）及以上可用的系统 API/内部实现。
- 目标设备需要与当前 Hook 的系统类和方法签名兼容。

## 安装与使用

1. 构建并安装 APK。
2. 在 LSPosed 中启用模块。
3. 确认作用域包含 `android`；使用 ColorOS 功能时同时包含系统启动器。
4. 强制停止或重启相关进程，确保 Hook 重新加载。
5. 打开模块设置，先保持高性能模式，按需加入允许读取无障碍信息的应用。
6. 使用“Accessibility Check”检查常见检测路径结果。

隐藏桌面图标后，应用不会再出现在桌面启动器中；如需再次打开设置，请从 LSPosed 的模块设置入口进入，并关闭 LSPosed 的“强制应用显示启动器图标”选项。

## 构建

项目使用 Gradle Wrapper 和 Kotlin DSL。Windows PowerShell 下：

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
\.\gradlew.bat :app:assembleDebug --no-daemon
```

生成的 Debug APK 位于：

```text
app\build\outputs\apk\debug\app-debug.apk
```

项目参数：

- `compileSdk = 35`
- `targetSdk = 35`
- `minSdk = 27`
- Java/Kotlin JVM target = 17
- 包名：`io.github.fairyxh.ZhangSystemHook`

## 性能注意事项

- 不建议在输入法、系统 UI 等高频路径中启用大量节点级 Hook。
- 不建议长期打开 Hook Toast 通知；每次命中都会增加日志、主线程 Toast 和调度压力。
- 不建议启用方法监视器实验功能，它会为目标类批量挂钩方法。
- 如果出现输入法卡顿、桌面重启或系统设置闪退，优先关闭无障碍防检测，禁用模块作用域后重启设备。
- 系统升级、切换 ROM 或修改启动器版本后，应重新验证方法签名和作用域。

## 项目结构

```text
app/src/main/java/io/github/fairyxh/ZhangSystemHook/
├── hook/
│   ├── AccessibilityHooker.kt       # 无障碍相关 Hook
│   ├── AudioCommunicationModeHooker.kt # 禁止通话模式/听筒，强制扬声器与媒体音量
│   ├── Android14ScreenshotBlocker.kt  # Android 14+ 截屏检测回调拦截
│   ├── EnhancedScreenshotBlocker.kt   # 增强截屏防检测（MediaStore 过滤）
│   ├── ScreenCaptureDetectionHooker.kt # 截屏检测相关 Hook
│   ├── DPMHooker.kt                  # DevicePolicyManagerService Hook
│   ├── ColorOSHomeHooker.kt          # ColorOS 启动器 Hook
│   ├── HookEntry.kt                  # 模块入口与 Hook 加载
│   └── MethodMonitor.kt              # 实验性方法监视器
├── ui/activity/                  # 主界面和应用配置界面
├── data/                         # 配置读取、刷新和导入导出
└── application/                  # Application、通知和启动状态工具
```

## 许可证

本项目使用 GNU General Public License v3.0，详见 [LICENSE](LICENSE)。
