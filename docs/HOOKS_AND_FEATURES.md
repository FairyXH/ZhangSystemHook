# ZhangSystemHook Hook 与功能说明

本文依据 2026-10-18 工作区源码整理，分析基线为 `8d4680e`。覆盖项目结构、入口、进程作用域、配置流、Hook 方法、参数操作和返回行为。

本文为静态代码说明：已核对加载入口与实现，未连接 Android 设备验证方法命中、ROM 签名或实际路由。文中的“加载”表示入口请求安装，不能等同于目标设备安装成功。

## 1. 项目结构与技术机制

项目为单个 Android 应用模块 `:app`，使用 Kotlin、Xposed 和 YukiHookAPI。配置为 `minSdk=27`、`compileSdk=35`、`targetSdk=35`、JVM 17；依赖配置为 YukiHookAPI 1.2.1、Kotlin 2.0.20、AGP 8.6.0。最低安装版本不代表所有功能兼容该版本。

以下源码目录均相对 `app/src/main/java/io/github/fairyxh/ZhangSystemHook/`：

| 路径 | 职责 |
| --- | --- |
| `hook/HookEntry.kt` | Xposed 入口、日志、系统及应用进程分流 |
| `hook/AccessibilityHooker.kt` | 无障碍调用者识别、名单策略、服务查询与设置读取拦截 |
| `hook/AudioCommunicationModeHooker.kt` | 音频模式、通信设备、音量与听筒状态干预 |
| `hook/Android14ScreenshotBlocker.kt` | 当前截图回调拦截实现，实际限制 Android 15+ |
| `hook/EnhancedScreenshotBlocker.kt` | 应用内 ContentResolver 观察者与媒体查询过滤 |
| `hook/DPMHooker.kt` | 设备所有者配置前置检查调整 |
| `hook/ColorOSHomeHooker.kt` | ColorOS 最近任务应用锁逻辑调整 |
| `hook/ScreenCaptureDetectionHooker.kt` | 旧版截图回调拦截及注册日志；当前未加载 |
| `hook/MethodMonitor.kt` | 批量方法监视工具；当前没有调用点 |
| `data/ConfigData.kt`、`data/ScreenshotConfig.kt` | 偏好读写、名单缓存、截图配置、名单导入导出函数 |
| `utils/tool/FrameworkTool.kt` | 模块应用与系统进程的数据通道 |
| `ui/activity/MainActivity.kt` | 状态展示、功能开关、无障碍检查、音频及截图测试 |
| `ui/activity/AppsConfigActivity.kt` | 应用名单、搜索、图标与列表展示 |
| `application/` | 模块应用初始化、启动完成检查、日志与 Toast |
| `utils/factory/FunctionFactory.kt` | 应用信息、桌面图标显隐、ColorOS 判断等工具 |

其他关键文件：

- [Manifest](../app/src/main/AndroidManifest.xml)：Xposed 元数据、共享偏好、Activity 与 `.Home` 桌面别名。
- [module_scope](../app/src/main/res/values/array.xml)：默认声明 `android`、`com.android.launcher`。
- [xposed_init](../app/src/main/assets/xposed_init)：指向生成入口 `io.github.fairyxh.ZhangSystemHook.hook.ZhangSystemHook`。
- [yukihookapi_init](../app/src/main/resources/META-INF/yukihookapi_init)：指向源码入口 `HookEntry`。
- [app/build.gradle.kts](../app/build.gradle.kts)：KSP 生成入口、ViewBinding、构建配置；Release 开启压缩与资源裁剪。
- [GitVersion.kt](../buildSrc/src/main/kotlin/GitVersion.kt)：按 Git 提交数与历史值计算版本，并在构建配置阶段写入 `version.properties`。

### 1.1 Hook 术语

| 写法 | 操作与效果 |
| --- | --- |
| `method { ... }.hook` | 按名称、参数、返回类型查找目标，再安装回调 |
| `Method.hook` | 反射枚举方法后逐个安装，常用于适配不同 ROM 重载 |
| `before` 中改写 `args[n]` | 原方法继续执行，但收到的是替换后的参数；通常保留原返回值 |
| `before` 中设置 `result`、`resultNull()`、`resultFalse()` | 提前提供结果，跳过原方法主体 |
| `after` 中设置 `result` | 原方法已经执行，再修改调用者看到的返回值；原副作用不会因此撤销 |
| `intercept()` | 本项目常在提前赋值后调用的拦截辅助操作；判断返回行为应同时看前面的赋值 |

系统进程 Hook 不会自动在第三方应用中安装相同的 Java Hook。应用通过 Binder 调用被挂钩的系统服务时，才可能从系统侧收到修改结果；应用自己的本地计算、缓存、其他服务路径不一定被覆盖。

## 2. 入口与加载顺序

源码：[HookEntry.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/hook/HookEntry.kt)。`@InjectYukiHookWithXposed` 生成 Xposed 入口，关闭资源 Hook；`onInit()`、`onHook()` 均将 Yuki 调试模式关闭。

```text
Xposed/LSPosed 注入
  -> 生成入口 -> HookEntry.onHook() -> encase
     -> loadSystem（android / system_server）
        -> ConfigData.init(PackageParam)
        -> 读取 ScreenshotConfig.targetPackages
        -> AccessibilityHooker -> 生命周期 onCreate 后安装
        -> DPMHooker           -> 生命周期 onCreate 后安装
        -> Android14ScreenshotBlocker -> 当场扫描，SDK >= 35
        -> AudioCommunicationModeHooker -> 当场扫描
     -> 遍历 targetPackages，排除模块自身
        -> loadApp(targetPackage) -> 配置开启时加载 EnhancedScreenshotBlocker
     -> loadApp(com.android.launcher)
        -> 初始化配置 -> 检测 ColorOS -> 生命周期 onCreate 后安装桌面 Hook
```

ColorOS 检测为任一类存在：`oppo.R`、`com.color.os.ColorBuild`、`oplus.R`，并非通过系统版本字符串判断。

### 2.1 实际加载状态

| 功能 | 当前入口 | 条件与范围 |
| --- | --- | --- |
| 无障碍核心 Hook | 已接入 | 系统进程请求安装，命中时再根据无障碍开关和名单判定 |
| 无障碍扩展 Hook | 不安装 | 源码 `high_proformance=true` 使整个扩展分支不可进入 |
| 设备策略 | 已接入 | 系统生命周期后安装，没有独立配置开关 |
| 截图回调 | 已接入 | 仅 SDK >= 35，开关默认 true，未按应用名单限制 |
| 音频 | 已接入 | 系统进程安装，开关默认 false，命中时读取 |
| 增强截图过滤 | 有条件加载代码，可达性存疑 | 目标包列表赋值位置及框架注入作用域均有约束，见第 5 节 |
| ColorOS 应用锁 | 已接入 | `com.android.launcher` 且命中 ColorOS 特征类 |
| 旧截图 Hook、MethodMonitor | 未接入 | 文件和部分配置存在，不代表运行 |

`ConfigData.ENABLE_MODULE` / `isEnableModule` 虽然存在，但入口和各 Hook 未读取它，不能作为全模块总开关。无障碍开关仅控制无障碍策略，不能关闭 DPM、桌面、截图或音频功能。

## 3. 无障碍防检测

源码：[AccessibilityHooker.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/hook/AccessibilityHooker.kt)。目的为改变调用者读取结果，不直接关闭已启用的无障碍服务。

### 3.1 调用者识别与策略

1. 生命周期 `onCreate` 安装 Hook，创建 `HookPolicy` 原子快照及缓存。
2. `pkgName()` 每隔至少 5 秒检查 `sys.boot_completed` 和用户解锁状态；未就绪时将调用者视为 `android`，不拦截。
3. 就绪后用 `Binder.getCallingUid()` 和 `PackageManager.getNameForUid()` 解析调用者，并按 UID 缓存；解析无结果时回退为 `android`。
4. `can_read()` 返回 true 表示放行、false 表示应用伪造结果。先判断功能开关、高性能限制、允许 addClient、开机状态及 `android` 特例，再查包名决策缓存。
5. 系统应用识别同时检查 `FLAG_SYSTEM`、`FLAG_UPDATED_SYSTEM_APP`；异常时视为系统应用。共享 UID 的名称不一定为单个包名，可能影响名单匹配，不能把解析结果等同于可靠的唯一包名。

以下规则以功能开启、系统已解锁、方法允许在高性能模式执行为前提：

| 调用者 | 系统白名单关闭 | 系统白名单开启 |
| --- | --- | --- |
| `android` | 始终放行 | 始终放行 |
| 模块应用自身 | 阻止，即使在名单中 | 阻止，即使在名单中 |
| 普通用户应用 | 在名单中放行，否则阻止 | 在名单中放行，否则阻止 |
| 系统应用 | 在名单中阻止，否则放行 | 在名单中放行，否则阻止 |
| `android.` 开头的系统应用 | 按系统应用黑名单处理 | `block_system=false` 时放行，否则按名单处理 |

名单存储键为 `_block_apps`，默认集合只含模块自身。这个集合同时承担用户应用白名单、系统应用黑名单/白名单和增强截图目标列表的职责，不能统一理解为“勾选即阻止”。

### 3.2 当前会尝试安装的四个核心 Hook

| 类 / 方法与匹配签名 | 时机 | 阻止时的操作 / 返回 |
| --- | --- | --- |
| `com.android.server.accessibility.AccessibilityManagerService.addClient(IAccessibilityManagerClient, int): long` | before | 返回 `0L`，不执行客户端注册；`allow_client_method=true` 时单独放行 |
| 同类 `getInstalledAccessibilityServiceList(int): ParceledListSlice` | before | 构造 `ParceledListSlice(emptyList<AccessibilityServiceInfo>())` 返回 |
| 同类 `getEnabledAccessibilityServiceList(int, int): List` | before | 返回空列表 |
| `android.content.ContentProvider$Transport.call(AttributionSource, String, String, String, Bundle): Bundle` | after | 修改已有 Bundle 的 `value` 及 `_generation_index`，不跳过原 Provider 调用 |

Provider 路径参数槽为 `args[1]=authority`、`args[2]=method`、`args[3]=key`。仅识别两个 key：

| key | 替换内容 |
| --- | --- |
| `enabled_accessibility_services` | `value=""`，`_generation_index=-1` |
| `accessibility_enabled` | `value="0"`，`_generation_index=-1` |

代码过滤条件是 `authority != settings && method != GET_secure` 时退出，因此只要 authority 或 method 任一匹配就会继续检查 key，并非要求二者同时匹配。原返回 Bundle 为 null 时不会创建新 Bundle。

该 Hook 仅在系统进程安装。Settings Provider 如运行在其他进程，其 `Transport.call` 不会因为本模块只注入 `android` 就自动被拦截，实际覆盖须看调用落点。

### 3.3 当前不挂载的扩展 Hook 清单

源码固定 `high_proformance=true`；外层条件为 `!high_performance_mode && !high_proformance`。因此关闭 UI 高性能开关也不会启用以下代码。它们仍处于系统进程 Hooker 内，即使修改源码启用，也不能据此宣称第三方应用本地调用已被覆盖。

下表描述源码拟实现的阻止行为，不代表当前运行效果；除单独标记 after 或改参外，主要通过 before 提前返回。

| 类 | Hook 方法 | 拟操作 / 返回 |
| --- | --- | --- |
| `AccessibilityManager` | `isEnabled`、`isTouchExplorationEnabled` | `false` |
| 同类 | `notifyTouchExplorationStateChanged`、`notifyAccessibilityStateChanged` | `null`，跳过通知 |
| 同类 | `getInstalledAccessibilityServiceList`、`getAccessibilityServiceList`、`getEnabledAccessibilityServiceList` | 空列表 |
| 同类 | `addAccessibilityStateChangeListener`、`addTouchExplorationStateChangeListener` | 单监听器重载返回 false；带 Handler 重载返回 null |
| `AccessibilityNodeInfo` | `isEnabled`、`isChecked`、`isFocused`、`performAction` | `false` |
| 同类 | `isVisibleToUser`、`isClickable` | `true` |
| 同类 | `getActionList` | 空列表 |
| 同类 | `getText`、`getPackageName`、`getStateDescription`、`getChild(int)`、`getParent` | `null` |
| 同类 | `setStateDescription(CharSequence)` | 写入弱引用缓存并返回 null，不执行原 setter；getter 并未返回缓存内容 |
| 同类 | `getClassName`、`getChildCount` | 分别返回 `"android.view.View"`、`0` |
| 同类 | `getBoundsInScreen(Rect)` 及返回 Rect 的候选方法 | 设置结果为零 Rect；对于 void 且通过参数输出的版本，未修改传入 Rect，不等同于正确清空边界 |
| `AccessibilityServiceTarget`、`InvisibleToggleAccessibilityServiceTarget` | `getAccessibilityServiceInfo` | `null` |
| `AccessibilityEvent` | `getContentChangeTypes`、`getEventType`、`getPackageName` | 前两者返回 0，包名返回 null |
| 同类 | `obtain` | 构造类型为 0、文本及描述清空的事件；回调内又调用 `obtain()`，若启用需核对重入问题 |
| `View` | `getContentDescription` | `null` |
| `AccessibilityInteractionClient` | `getConnection(int)` | `null` |
| `Settings.Secure` | `getInt(ContentResolver, String, int)` | after：key 包含 `accessibility` 时返回 0 |
| 同类 | `getString(ContentResolver, String)` | after：服务列表返回空字符串，启用状态返回 `"0"` |
| `ContentResolver` | `query` 五参、六参和 Bundle 重载 | after：符合各自 URI 判断时替换为零行、单列 `value` 的 MatrixCursor |
| `ApplicationPackageManager` | `queryIntentServices(Intent, int)` | after：查询 action 为 `android.accessibilityservice.AccessibilityService` 时返回空列表 |
| 同类 | `getPackageInfo(String, int)` | after：替换整个 PackageInfo，仅保留包名，清空服务、版本、权限、时间等；未限定只处理无障碍包 |
| `ActivityManager` | `getRunningServices(int)` | after：过滤 service 字符串含 `accessibility` 的条目 |
| `ServiceManager` | `getService(String)` | after：查询 `accessibility` 时返回 `onTransact=false` 的占位 Binder |
| `ContextWrapper` | `registerReceiver(BroadcastReceiver, IntentFilter)` | 非系统且被阻止的调用者注册敏感 action 时，将过滤器改为 `invalid.action`，保留原调用 |
| `IAccessibilityManager$Stub$Proxy` | 已启用/已安装服务列表、`addClient` | 空列表、空 ParceledListSlice、`0L` |
| `AccessibilityServiceInfo` | `loadDescription`、`getResolveInfo`、`toString` | 空字符串、null、随机拼装的描述字符串 |
| `Context` | `getSystemService(String)`、`getSystemService(Class)` | 请求无障碍服务时返回 null；具体 ROM 上的实现/重写仍须验证 |

还存在由第二个源码常量 `Dangerous_method=false` 关闭的内部子分支：

- `AccessibilityManager.addAccessibilityRequestPreparer`：提前返回 null；`addAccessibilityInteractionConnection`：提前返回 true。
- `BroadcastReceiver.onReceive`：指定设置变化广播且 key 为无障碍字段时跳过。
- `LocalBroadcastManager.registerReceiver`：敏感 action 时跳过注册。
- `Class.getDeclaredMethod` / `getMethod`：对指定无障碍类及方法名拟抛 `NoSuchMethodException`；`getDeclaredMethods` 在 after 过滤对应方法。回调中直接抛异常能否按预期传给调用者还取决于框架处理。

这些备用 Hook 中包含抽象类、易变内部签名和不完整的返回契约，不能仅通过修改常量就视为可用功能。

## 4. 音频：禁止普通应用切换通话模式

源码：[AudioCommunicationModeHooker.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/hook/AudioCommunicationModeHooker.kt)。统一开关 `block_third_party_communication_mode`，默认 false，回调中读取失败时通常按关闭处理。Hook 安装不等待应用生命周期。

方法扫描包含父类的非抽象方法，并按反射签名去重；不同 Hook 家族分别捕获安装异常。日志中的 `candidate not found` 表示当前 ROM 没有匹配签名。

### 4.1 模式与通信设备

| Hook 点 / 匹配 | 时机与操作 | 原方法和返回行为 |
| --- | --- | --- |
| `AudioService.setMode` 全部同名候选 | before：普通非系统、非特权调用请求 `MODE_IN_CALL` / `MODE_IN_COMMUNICATION` / `MODE_CALL_SCREENING` 时，将第一个 int 模式改为 `MODE_NORMAL` | 原方法继续执行，保留原返回 |
| `AudioService.setOriginalMode` | before：开关开启、请求通话模式且当前模式持有者 UID >= 10000 时，将第一个 int 改为普通模式 | 保留原调用；持有者无法识别时放行 |
| `AudioService.onUpdateAudioMode` / `setModeInt` | before：同上改参；after：检查实际模式并安排兜底 | 原调用后可能触发额外纠正 |
| `AudioSystem.setPhoneState` | before：开关开启、Binder UID >= 10000 且请求上述模式时返回 `-1` | 跳过原方法；并不额外判断系统应用标志或路由权限 |
| `AudioService.startBluetoothSco` / `startBluetoothScoVirtualCall` / `stopBluetoothSco` | before：记录决策，当前 acquisition 始终 false | 均放行，不会实际阻止 SCO |
| `AudioService.setCommunicationDevice*`，严格匹配 `(IBinder, int)` / `(IBinder, AudioDeviceInfo)` | before：满足规则时将第二参数替换为扬声器 ID / 对象 | 保留原调用及返回；其他参数形状不在本扫描范围 |
| `AudioService.setPreferredDeviceForStrategy`，最后参数为 `AudioDeviceAttributes` | before：开关开启且最后设备为内置听筒时，构造内置扬声器属性替换 | 保留原调用，无系统/特权豁免判断 |
| `AudioService.setSpeakerphoneOn`，前两参为 `(IBinder, boolean)` | before：开关开启，请求 false，调用者非系统、非特权且当前通信设备非蓝牙 SCO 时改为 true | 保留原调用 |

`setMode`、通信设备和扬声器路径中的系统/特权判定包括：UID < 10000；UID 下任一包具有系统或更新系统应用标志；持有 `MODIFY_PHONE_STATE` 或 `MODIFY_AUDIO_ROUTING` 调用权限。上下文或应用身份无法解析时偏向放行。

通信设备 ID 路径还放行：开关关闭、默认路由 ID=0、蓝牙 SCO、已经为扬声器、AudioManager/扬声器不可用、解析异常。对象路径放行 null、蓝牙 SCO 和已为扬声器。其他明确设备请求可能被改成扬声器，因此规则不只针对听筒，也不保证放行全部外接耳机类型。

模式提交和兜底路径通过 `mSetModeDeathHandlers` 中的 `getMode()`、`getUid()` / `mUid` 识别持有者，只按 UID >= 10000 判断，没有重新执行完整系统应用/权限豁免。Telephony 通话状态仅用于日志，不是该决策的直接条件。

兜底条件成立时，通过后台 `AudioModeCorrector` Handler 延迟 600 ms 反射调用 `setMode(MODE_NORMAL, binder[, "ZhangSystemHook"])`，用原子标志合并待执行任务。任务执行时没有再次完整复核开关、持有者和模式；成功纠正仍取决于能否匹配两参/三参签名。

### 4.2 音量操作

| Hook 点 | 条件与修改 | 返回行为 |
| --- | --- | --- |
| `AudioService.adjustStreamVolume*` | 开关开启且当前为通话模式，或请求/活跃流为 VOICE_CALL，将第 0 个流参数改为 MUSIC | 原调整继续，after 同步通话/SCO 音量 |
| `AudioService.adjustSuggestedStreamVolume*` | 同样条件；根据前两参是否为 `USE_DEFAULT_STREAM_TYPE` 判断流槽，无法命中特征时按第 1 参数处理 | 保留方向等其他参数；OEM 参数次序仍需验证 |
| `AudioService.getStreamVolume` / `getStreamMaxVolume` | 第 0 参数为 VOICE_CALL 或 SCO、UID >= 10000、开关开启且非内部同步时，将流改为 MUSIC | 返回原方法针对 MUSIC 的结果 |
| `AudioService.setStreamVolume*`，第 0 参数为 int | 同样条件下，不把流改成 MUSIC；改写第 1 个索引，使通话/SCO 索引跟随当前媒体音量比例 | 原方法设置原通话流，保留返回；不直接抬高媒体音量 |

系统 UID < 10000 的通话流 get/set/max 调用直接放行；内部同步以 `syncingVolumeAlias` 防止递归。上述流调整和同步路径没有统一套用系统应用、特权权限或真实电话状态豁免，因此不能概括为“所有音频干预都不影响系统电话”。

媒体索引 `[0, musicMax]` 映射为通话/SCO `[1, targetMax]`，采用四舍五入整数比例并限制输入范围。媒体音量为 0 时目标仍为 1，不能据此保证通信音频完全静音。同步同时更新 VOICE_CALL 和 SCO；缓存真实最大值，查询失败或状态未知时放行或跳过同步。显式设置路径缺少目标最大值时有 VOICE_CALL=9、SCO=15 的回退值。

### 4.3 听筒设备层

以下路径主要检查音频开关，没有通用的系统/特权或真实电话豁免：

| `AudioSystem` Hook 点 | 操作 / 返回 |
| --- | --- |
| `setDeviceConnectionState*` | before：按首参数 int 或 AudioDeviceAttributes 判断设备；源码识别为听筒且第二参数为 AVAILABLE=1 时，改为 UNAVAILABLE=0 |
| `getDeviceConnectionState` | before：第一个 int 为源码听筒常量 `0x1` 时，提前返回 0 |
| `getDevicesForAttributes` / `getOutputDevices` | after：仅当结果实际是 Int 且掩码含 `0x1` 时，清除此位并 OR 源码常量 `0x4`；列表/数组返回不变 |
| 安装后主动操作 | 延迟 5000 ms 检查开关，寻找首两参为 int 的旧连接状态方法，反射断开听筒一次 |

须区分 AudioDeviceInfo 公共设备类型与 AudioSystem 内部设备掩码。实现固定使用 `DEVICE_OUT_EARPIECE=0x1`、`DEVICE_OUT_SPEAKER=0x4`，并对属性对象调用 `getType()`；这些数值及属性含义是否符合目标 ROM，需要实测核对，不能仅凭常量名保证替换成扬声器。主动断开只适配当前代码构造的三参/四参形式，其他重载可能调用失败。

关闭开关后不再伪造新请求，但代码没有对已主动断开的听筒进行对应重连，也没有在开启开关时重新安排那次安装后任务。设备状态恢复需要设备端验证。

## 5. 截屏防检测

### 5.1 当前系统回调拦截

源码：[Android14ScreenshotBlocker.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/hook/Android14ScreenshotBlocker.kt)。虽然类名、键名和界面文案保留 Android 14，实际入口门槛是 `VANILLA_ICE_CREAM`，即 SDK 35 / Android 15。

| 项目 | 当前行为 |
| --- | --- |
| 目标类 | `com.android.server.wm.ActivityRecord` |
| 查找方法 | 本类及父类所有非抽象的 `onScreenCaptured` / `reportScreenCaptured`，不固定参数签名 |
| 时机 | before |
| 开关开启 | `resultNull()`，跳过截图回调分发 |
| 开关关闭 / 读取失败 | 保留原方法 |
| 开关 | `enable_android14_screenshot_blocker`，默认 true，每次命中读取 |
| 作用范围 | 当前注入系统进程中命中的 ActivityRecord，不按调用应用名单过滤 |

该实现没有修改截图生成、保存或 FLAG_SECURE，也不代表阻止录屏检测。它阻止应用获知该分发路径上的截图事件。

### 5.2 增强过滤及入口限制

源码：[EnhancedScreenshotBlocker.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/hook/EnhancedScreenshotBlocker.kt)。预期在目标应用进程加载，开关 `enable_enhanced_screenshot_blocker` 默认 false，在入口和 Hooker 初始化时检查；安装后的回调不再读取开关。

入口存在明显约束：`targetPackages` 每次 `onHook()` 先设为空，仅在 `loadSystem` 回调中赋值，之后才遍历注册 `loadApp(targetPackage)`。按系统/应用分支分别执行的流程，目标应用进程没有执行该赋值，列表仍为空；系统进程拿到列表也不等于为其他应用进程安装 Hook。因此当前不能宣称该功能已能正常加载，应以目标进程出现 `[Enhanced] installing` / `hooked` 日志为准。默认声明作用域也不包含这些第三方目标应用。

若确实进入 Hooker，其操作如下：

| Hook 点 | 时机 / 条件 | 操作 / 返回 |
| --- | --- | --- |
| `ContentResolver.registerContentObserver` 的所有 declared 重载 | before：首个 Uri 的 authority 为 `media`，且 URI 字符串包含截图关键词/路径 | 返回 null，跳过注册；其他 URI 放行 |
| `ContentResolver.query` 的所有 declared 重载 | after：authority 为 `media` 且返回非 null Cursor，未被本包装器包装 | 返回 `ScreenshotFilteringCursor` |

截图关键词为 `screenshot`、`截屏`、`截图`，路径为 `pictures/screenshots`、`dcim/screenshots`，比较前转为小写。泛化媒体 URI 如 `content://media/external/images/media` 本身不含截图词，观察者注册会放行；这里拦截的是注册调用，并没有逐条筛选 ContentObserver 通知。

Cursor 包装器扫描 `_display_name`、`title`、`relative_path`、`_data` 列，只要任一可用列含关键词就排除该行；缺少相关列时无法判断。包装器保存可见物理位置，仅重写 `getCount()` 与 `moveToPosition()`，其他移动、位置、批量访问接口未完整实现逻辑行映射，需要验证 `moveToNext()` 等遍历方式，不能仅以 count 变少判定过滤完整。命名匹配也可能误伤名称含截图词的普通图片。

### 5.3 备用截图实现

`ScreenCaptureDetectionHooker` 当前未由入口加载。它同样尝试 before 拦截 ActivityRecord 两个截图方法，使用废弃键 `block_screen_capture_detection`，默认 false；还对 `ActivityTaskManagerService.registerScreenCaptureObserver` 做 before 日志，不阻止注册。不要将该旧键与当前主界面的截图开关混淆。

## 6. 设备策略与 ColorOS 最近任务锁

### 6.1 DevicePolicyManagerService

源码：[DPMHooker.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/hook/DPMHooker.kt)。系统生命周期后执行，无独立开关、应用名单或调用者过滤。

| 方法签名 | 时机 | 操作 / 返回 |
| --- | --- | --- |
| `hasAccountsOnAnyUser(): boolean` | after | 改为 false；原检查已运行 |
| `enforceCanSetDeviceOwnerLocked(CallerIdentity, ComponentName, int, boolean)` | before | `resultNull()`，跳过原校验；after 仅打印提示 |
| `checkDeviceOwnerProvisioningPreConditionLocked(ComponentName, int, int, boolean, boolean): int` | after | 改为 0，按通过前置条件的方向干预 |

它改变部分设备所有者设置检查，没有自行发起设备所有者设置操作，也没有覆盖 DPM 全部权限或配置流程。after 修改返回不能保证消除原方法抛出的异常。

### 6.2 ColorOS 桌面

源码：[ColorOSHomeHooker.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/hook/ColorOSHomeHooker.kt)。仅 `com.android.launcher` 且判断为 ColorOS 时加载，生命周期后安装，无独立开关。

| 类 / 方法 | 时机 | 操作 / 返回 |
| --- | --- | --- |
| `com.oplus.quickstep.applock.AppLockModel.isExceededLockLimit(String, String): boolean` | before | 返回 false，跳过数量上限判断 |
| `com.oplus.quickstep.applock.AppLockInfo.isAllowDefaultLock(): boolean` | after | 返回 true |
| 同类 `isUseless(): boolean` | after | 返回 false |

功能针对桌面最近任务的锁定规则；没有实现其他应用的密码、隐私锁认证绕过。厂商桌面包名、类名和方法签名变化均会影响命中。

## 7. 配置持久化、刷新与操作说明

`ConfigData.init` 接受模块应用 Context 或 Hook 宿主 PackageParam。Context 可写偏好，PackageParam 只读；`_block_apps` 通过 `PrefsDataSetString` 缓存在内存。

| 存储键 / 默认值 | 作用 | 生效条件 |
| --- | --- | --- |
| `_enable_module=true` | 声明的总开关 | 当前无运行消费点 |
| `accessibility_hook_enable=false` | 无障碍策略开关 | HookPolicy 缓存刷新或系统进程重新加载 |
| `high_performance_mode=false` | 用户高性能配置 | 当前被源码固定高性能覆盖 |
| `allow_client_method=false` | 放行无障碍 addClient | 策略缓存刷新或重新加载 |
| `system_app_whitelist=false` | 系统应用名单语义 | 策略缓存刷新或重新加载 |
| `block_system=false` | 系统白名单模式中的 `android.` 系统组件处理 | 策略缓存刷新或重新加载 |
| `_block_apps={模块包名}` | 应用规则集合 | add/remove 写入后经数据通道通知系统刷新 |
| `enable_android14_screenshot_blocker=true` | 当前系统截图回调拦截 | SDK >= 35；每次回调读偏好，跨进程可见性需验证 |
| `enable_enhanced_screenshot_blocker=false` | 增强媒体过滤 | 须先解决加载可达性/作用域；安装后改变开关不会撤销已有 Hook |
| `block_third_party_communication_mode=false` | 音频干预 | 每次回调读取；已改变的设备状态不自动复原 |
| `show_notice=false` | `SystemNotifier.sendUserMsg` 的日志与 Toast | 每次读取；不控制直接调用 XposedBridge 的 HookLog |
| `MothodMonitor_SW=false` | 方法监视器配置 | 当前未接入执行 |
| `block_screen_capture_detection=false` | 旧截图实现 | 当前未加载 |

应用名单更新链路：

```text
AppsConfigActivity 点击条目
 -> ConfigData.blockApps.switch -> add/remove
 -> putStringSet 写入偏好
 -> FrameworkTool.refreshFrameworkPrefsData
 -> android 数据通道 call_refresh_host_prefs_data
 -> AccessibilityHooker 等待 500 ms
 -> ConfigData.refresh + refreshPolicyCallback
 -> 重读名单与策略，清空决策/系统应用缓存
```

主界面普通布尔 setter 只写偏好，没有发送上述刷新通知；无障碍策略已经缓存，不能保证切换后立即生效。修改名单会顺带刷新策略；重启相关系统进程/设备可重新加载。刷新流程没有清空 UID 名称缓存，也不会重建增强截图目标加载分支。

导入/导出函数当前没有 UI 调用点。导出仅写 `_block_apps`，不含功能布尔开关；导入读取 XML 的 string 元素后调用 `replaceAll()`，而 `replaceAll()` 没有 `putStringSet()`，只有内存替换及通知，不能保证导入结果持久化。函数在输入/输出流为 null 时也可能返回 true，不能据返回值证明成功。

### 7.1 用户操作与自检

1. 安装 APK，在 Xposed/LSPosed 中启用模块，确认系统 `android` 作用域；ColorOS 桌面功能需要相应桌面作用域。
2. 重新加载系统/桌面进程。主界面激活状态来自框架状态及与 `android` 数据通道的版本检查，不是所有 Hook 的逐项成功检测。
3. 开启无障碍防检测并配置名单，按上表核对勾选含义。主界面 `onResume` 执行六类检查：启用状态、已启用服务、服务列表、触摸探索、Settings 启用值与服务列表字符串。
4. “通话模式测试”在 MODE_IN_COMMUNICATION / MODE_NORMAL 间切换；界面文案按请求标志更新，应结合读取的实际 mode 和 AudioMode 日志判断是否被改写，不能仅凭“已进入”文案判定。
5. 截图测试在 Activity `onStart` 注册 ScreenCaptureCallback，在 `onStop` 取消；测试按钮只显示监听是否已注册。保持页面可见后实际截屏，观察回调与系统 BLOCK/ALLOW 日志。
6. 桌面图标开关改变 `.Home` Activity alias 状态；隐藏后可从框架模块设置入口进入。

主界面直接使用 `Activity.ScreenCaptureCallback`，注册处未设置 SDK 分支保护；即使 APK 最低版本为 27，也需要单独验证旧系统的类加载/页面兼容性。无障碍和桌面代码中的 `@RequiresApi(S)` 是标注，并非运行时版本判断。

## 8. 日志与验证边界

| 来源 / 关键词 | 用途 |
| --- | --- |
| `HookLog`，前缀 `[ZhangSystemHook]` | 直接写 XposedBridge，debug/info/warn/error 均使用该日志通道 |
| `AudioBlocker` / `AudioMode` | 候选签名、uid/pkg、ALLOW、FORCE_MODE_NORMAL、REWRITE_TO_SPEAKER、SYNC |
| `ScreenshotBlocker` 的 `[Android15]` | SDK 跳过、方法安装、BLOCK/ALLOW、配置读取错误 |
| `[Enhanced]` | 目标应用内安装、观察者放行/阻止、Cursor 过滤 |
| `SystemNotifier.sendUserMsg` | 受 show_notice 控制的 YLog 和主线程 Toast；不是所有日志的总开关 |
| `MainActivity` 的 `ScreenshotBlocker` / `ZhangSystemHookTest` | 模块应用测试回调与实际 mode 读取，使用 Android Log |

建议按独立路径验证，不以“扫描完成”替代方法安装和调用命中：

| 路径 | 最小验证步骤 | 应保留的证据 |
| --- | --- | --- |
| 无障碍 | 开关关闭建立基线；开启并刷新；用户应用加入/移出名单，分别查服务列表 | 同一调用者、相同查询、名单与刷新状态、原/新结果 |
| 系统应用策略 | 对同一系统应用切换黑名单/白名单语义 | 系统标志、名单状态、UID 解析结果 |
| 截图回调 | Android 15+ 保持测试页前台，关闭/开启开关分别实际截屏 | 安装签名、ALLOW/BLOCK、应用回调；截图文件仍生成 |
| 增强截图 | 先确认目标进程实际安装；再以含/不含关键列查询，并测试各种 Cursor 移动方式 | 目标进程、URI、projection、原始行、count 与实际遍历结果 |
| 音频模式 | 开关关闭/开启分别请求通信模式，再测试系统电话及蓝牙 | 请求参数、实际 mode、模式持有者 UID、路由与音量 |
| 音量 | 分别测试媒体音量 0/中间/最大值、应用设置通话音量、系统音量控制 | MUSIC/VOICE_CALL/SCO 的索引和最大值、是否误抬媒体音量 |
| 听筒 | 核对设备常量、启用后连接状态、禁用后恢复情况 | AudioSystem 参数签名、设备掩码、真实输出设备 |
| DPM / 桌面 | 对照目标 ROM 签名后执行对应操作 | 方法命中及前后行为；仅类存在不足以证明生效 |

本次只生成说明文档并核对源码，不修改 Hook 实现。上述加载疑点、兼容性和返回契约问题作为现状记录，尚未经设备运行验证。
