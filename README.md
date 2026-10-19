# ZhangSystemHook

原生 libxposed API 102 Android 系统增强模块。保留原包名、仓库及配置键，已移除 YukiHookAPI、旧 XposedBridge API、KSP 入口和跨进程数据通道。

[Hook 与功能说明](docs/HOOKS_AND_FEATURES.md) · [迁移与验收记录](docs/API102_MIGRATION.md)

## 功能

- 无障碍防检测：系统服务注册、服务列表、Settings/Provider、应用查询、监听器；支持用户白名单、系统黑白名单和系统组件保护。
- 高性能模式：默认跳过节点与事件 Hook；关闭后按需安装扩展 Hook，重新开启后回调放行。高级广播/反射过滤默认关闭。
- 音频：普通应用通话模式回落、通信设备路由、扬声器、蓝牙兼容、媒体与通话/SCO 音量同步、听筒设备状态处理。
- 截屏：Android 14+ 官方回调拦截；增强模式在名单应用内过滤媒体查询，并处理含截图关键词 URI 的观察者注册。
- 设备策略：设备所有者配置的部分前置检查调整。
- ColorOS 桌面：最近任务应用锁数量、默认锁及有效性判断。
- 设置：iOS 风格分组界面、深色模式、模块总开关、框架状态、应用分类/搜索、桌面图标显隐、配置导入导出与功能测试。

## 环境与使用

- APK 最低 Android 8.1 / API 27。具体系统内部方法取决于 ROM，截图回调功能要求 Android 14+。
- 必须使用支持 **libxposed API 102** 的框架；仅支持传统 API 93 的旧版 LSPosed 无法加载此版本。
- 在框架中启用模块，确认推荐作用域：`android`、`com.android.providers.settings`；ColorOS 桌面功能还需 `com.android.launcher`。
- 无障碍核心拦截在系统侧运行。应用内扩展查询与增强截图功能，需要额外将目标应用加入框架作用域并重启目标应用。
- 设置页可申请名单应用的动态作用域；请求是否通过由框架决定。
- 安装更新后重启系统以加载新核心。之后普通配置通过远程偏好通知更新，无需旧数据通道。
- 勾选用户应用表示允许读取；系统应用默认勾选表示阻止读取，系统白名单开启后勾选表示允许读取。模块自身在系统侧保留测试用的阻止规则。
- 增强截图复用原名单作为目标集合，与上述无障碍名单语义是两个用途。

首次启动设置页会迁移模块自身旧偏好中的已知键。框架未连接时保存本地待同步配置，连接后同步；导入兼容旧版仅包含 `_block_apps` 的 XML，新版导出同时包含功能开关。

## 构建

构建链：AGP 9.3.0、Gradle 9.5.0、AGP 内置 Kotlin、compileSdk 37、targetSdk 35、JVM 17。版本从 `version.properties` 读取，构建不会自动修改该文件。

PowerShell，使用 JDK 17 或受当前 Gradle 支持的 JDK：

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleRelease --console=plain --no-daemon > build.log 2>&1
Get-Content build.log -Tail 80
```

默认产物：

- `app/build/outputs/apk/debug/app-debug.apk`：Debug 签名。
- `app/build/outputs/apk/release/app-release-unsigned.apk`：压缩后的未签名 Release，需自己的签名配置。

项目目录被同步软件监控时，可将输出和项目缓存放到其他目录：

```powershell
.\gradlew.bat :app:assembleDebug --console=plain --no-daemon --project-cache-dir D:/BuildCache/ZhangSystemHook -PbuildOutputRoot=D:/BuildOutput/ZhangSystemHook > build.log 2>&1
Get-Content build.log -Tail 80
```

## 结构

```text
app/src/main/
  java/io/github/fairyxh/ZhangSystemHook/
    core/          # 原生 API 102 拦截链、名单策略、反射缓存、音量映射
    hook/          # 各项功能
    data/          # 内存快照、远程偏好、本地迁移、XML 配置
    application/   # 原生 Application、启动检查、限流提示
    ui/            # 原生 iOS 风格界面和应用列表
    utils/tool/    # libxposed service 连接与动态作用域
  resources/META-INF/xposed/
    java_init.list # 无参 XposedModule 入口
    module.prop    # API 102 与动态作用域元数据
    scope.list     # 推荐作用域
app/src/test/      # 名单、音量、配置快照及拦截链回归测试
```

系统 Hook 的实际生效以安装签名、调用命中和设备行为为准。构建通过不等同于 ROM 适配通过；当前验收边界见迁移记录。

## 许可证

GNU General Public License v3.0，详见 [LICENSE](LICENSE)。
