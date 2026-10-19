# Hook 与功能说明：API 102

本文对应原生 API 102 重构后的代码。重构前基线说明保留在 Git 历史 `6e2783e:docs/HOOKS_AND_FEATURES.md`，其中 Yuki 入口、配置刷新限制和未挂载状态不再代表当前实现。

## 入口、作用域与返回约定

[HookEntry.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/hook/HookEntry.kt) 继承无参 `XposedModule`，通过 `META-INF/xposed/java_init.list` 声明；API/service/interface 均为 102.0.0。没有传统入口或旧 Hook API 依赖。

- `onModuleLoaded`：绑定框架远程偏好，记录 API 对象。
- `onSystemServerStarting`：设置系统 ClassLoader，分别安装无障碍、DPM、截图、音频 Hook。
- `onPackageReady`：对被框架注入的主包初始化应用上下文捕获、无障碍应用侧路径、增强截图；桌面另安装 ColorOS Hook。
- 推荐作用域：系统框架（`system`）、`com.android.providers.settings`、`com.android.launcher`。API 102 的 `android` 指普通框架应用，不能替代 `system`。应用内功能需目标应用作用域。
- 系统和应用入口独立执行，不再通过系统分支变量向应用分支传递截图目标列表。
- 每个 Hooker 安装失败单独记录，不阻止其他功能继续安装。
- 不开启自动热重载；更新 APK 后重新启动目标进程，系统 Hook 更新需要重启系统。

[HookRuntime.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/core/HookRuntime.kt) 将回调直接安装到 API 102 的 `hook(method).intercept(chain)`：

| 操作 | 原方法 / 返回行为 |
| --- | --- |
| before 改写 `args` | 调用 `chain.proceed(newArgs)`，保留原返回 |
| before 设置 `result`（含 null） | 跳过原方法；after 仍可处理已提供结果 |
| after 设置 `result` | 原方法已执行，仅替换返回值 |
| 普通回调异常 | before 失败以原参数放行；after 失败保留原返回，不重复原操作 |
| 原方法异常 | 直接传播，不重试原方法 |
| 显式 failure | 仅高级反射过滤使用异常传播模式，向调用者提供 NoSuchMethodException |
| 总开关关闭 | 直接 `chain.proceed()`，不执行功能回调 |

方法表按 Class 缓存。每个方法可注册不同功能的回调；相同回调类重复安装会被去重，避免配置刷新重复挂钩，也避免无障碍与增强截图争用 ContentResolver.query。

## 无障碍

[AccessibilityHooker.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/hook/AccessibilityHooker.kt) 与 [AccessPolicy.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/core/AccessPolicy.kt)。

系统及 Settings Provider 路径根据 Binder UID 解析调用者。系统尚未完成启动/用户未解锁、身份不明或系统基础 UID 时放行；启动状态检查间隔 5 秒，UID 分类缓存 60 秒，上限约 512 条。共享 UID 的用户应用要求其所有包均在允许集合内。

应用内路径使用已捕获的 Application Context 和进程包名。模块 APK 不额外注入自身；它通过系统服务路径保留无障碍测试。

| 调用者 | 默认名单模式 | 系统白名单模式 |
| --- | --- | --- |
| 普通用户应用 | 勾选允许，未选阻止 | 勾选允许，未选阻止 |
| 系统/更新系统应用 | 勾选阻止，未选允许 | 勾选允许，未选阻止 |
| `android.` 系统组件 | 按系统黑名单 | block_system=false 时保护放行，否则按名单 |
| 模块自身 | 系统侧阻止 | 系统侧阻止 |

总开关或无障碍开关关闭时放行；allow_client_method 对 addClient 单独放行。

| Hook 点 | 时机 | 阻止时操作 |
| --- | --- | --- |
| AccessibilityManagerService.addClient（long 返回候选） | before | 返回 0L |
| 同类 getInstalledAccessibilityServiceList / getEnabledAccessibilityServiceList | before | 根据返回类型提供空 List 或空 ParceledListSlice |
| ContentProvider.Transport.call | after | GET_secure 及对应 key 命中时改 Bundle：服务字符串为空、启用字符串为 0，generation index=-1 |
| 应用内 AccessibilityManager.isEnabled / isTouchExplorationEnabled | before | false |
| 同类三个服务列表方法 | before | 空服务列表 |
| Settings.Secure.getInt / getIntForUser | after | ACCESSIBILITY_ENABLED 返回 0 |
| Settings.Secure.getString / getStringForUser | after | 服务列表为空字符串，启用状态为字符串 0 |
| IAccessibilityManager.Stub.Proxy 服务列表 / addClient | before | 同系统服务返回规则 |

以下为关闭高性能模式后的扩展路径，均要求目标应用已在作用域：

| Hook 点 | 操作 |
| --- | --- |
| ContentResolver.query | after：settings 的无障碍 URI 替换为空 value Cursor，并关闭原 Cursor |
| ApplicationPackageManager.queryIntentServices | after：无障碍服务 action 返回空列表 |
| ApplicationPackageManager.getPackageInfo | after：清空 PackageInfo 的服务、版本、权限等，保留包名 |
| ActivityManager.getRunningServices | after：过滤字符串含 accessibility 的条目 |
| ServiceManager.getService | after：accessibility 返回占位 Binder |
| ContextWrapper.registerReceiver | before：敏感 action 替换成 invalid.action |
| ContextImpl.getSystemService | before：accessibility 返回 null |
| AccessibilityServiceInfo.loadDescription / getResolveInfo / toString | 空描述、null、无服务信息的描述 |
| AccessibilityManager 状态监听/通知方法 | false 或 null，按返回类型跳过 |
| AccessibilityNodeInfo 状态、文本、父子节点、动作 | 保留原有伪造方向：状态 false；可见/可点击 true；文本与节点 null；动作空列表；子节点数 0 |
| 同类 getClassName / getBoundsInScreen / setStateDescription | 通用 View 类名、清空输出 Rect 或返回零 Rect、跳过描述写入 |
| AccessibilityEvent.obtain / 内容类型 / 事件类型 / 包名 | obtain 后清理真实返回事件，避免递归 obtain；类型 0、包名 null |
| View.getContentDescription / AccessibilityInteractionClient.getConnection | null |
| 内部 AccessibilityServiceTarget 两类 getAccessibilityServiceInfo | null |

节点/事件 Hook 在首次关闭高性能模式时安装，重新开启后快速放行；需要彻底移除已安装节点 Hook 时重启目标应用。

原源码关闭的高级广播/反射分支保留为 `extended_filter_enabled`，默认 false，需关闭高性能模式：

- addAccessibilityRequestPreparer / addAccessibilityInteractionConnection：跳过调用并按返回类型提供结果。
- BroadcastReceiver.onReceive / LocalBroadcastManager.registerReceiver：匹配的无障碍设置广播跳过。
- Class.getMethod / getDeclaredMethod：目标无障碍类和敏感方法名命中后抛 NoSuchMethodException。
- Class.getDeclaredMethods：过滤对应方法。抽象 onReceive 本身无法安装，具体厂商实现仍需实机验证。

## 音频

[AudioCommunicationModeHooker.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/hook/AudioCommunicationModeHooker.kt) 保留模式、设备、音量、听筒处理链，统一使用内存开关 `block_third_party_communication_mode`，默认 false。

| Hook 点 | 修改 / 返回 |
| --- | --- |
| AudioService.setMode | 普通非系统非特权调用请求 IN_CALL / IN_COMMUNICATION / CALL_SCREENING 时，首个 int 改 MODE_NORMAL，保留原调用 |
| setOriginalMode / onUpdateAudioMode / setModeInt | 当前通话模式持有者 UID >= 10000 时改模式；提交后复查并延迟 600ms 纠正 |
| AudioSystem.setPhoneState | Binder UID >= 10000 的上述模式请求返回 -1 |
| startBluetoothSco / startBluetoothScoVirtualCall / stopBluetoothSco | 保留放行与诊断入口，不阻止蓝牙 SCO |
| setCommunicationDevice* | 支持前两参为 IBinder + 设备 ID/对象的候选（可带后续 OEM 参数），按原规则替换为扬声器 |
| setPreferredDeviceForStrategy | 最后设备参数是听筒时改扬声器属性 |
| setSpeakerphoneOn | 普通非特权、非当前 SCO 场景将 false 改 true |
| adjustStreamVolume* / adjustSuggestedStreamVolume* | 通话模式或 VOICE_CALL 流选择时改为 MUSIC，after 同步通话/SCO 索引 |
| getStreamVolume / getStreamMaxVolume | 应用读取 VOICE_CALL/SCO 时映射到 MUSIC；系统内部调用放行 |
| setStreamVolume* | 应用设置通话/SCO 索引时，以当前媒体比例改原通话流索引，不抬高媒体音量 |
| AudioSystem.setDeviceConnectionState* | 识别听筒连接请求后将 AVAILABLE 改为 UNAVAILABLE |
| getDeviceConnectionState | 听筒返回不可用 0 |
| getDevicesForAttributes / getOutputDevices | 仅对 Int 掩码清除听筒位并加入扬声器位 |
| 安装后任务 | 延迟 5 秒尝试通过旧连接状态签名断开听筒一次 |

系统/特权放行并非适用于全部路径：模式持有者、HAL、音量选择和听筒部分仍沿用各自判定；真实电话、蓝牙和 OEM 路由需要设备回归。默认路由 ID=0、明确 SCO 请求、扬声器已选、设备或身份无法解析时，通信设备入口通常放行。

性能与正确性调整：关闭功能时 before 提前返回；43 处音频 info/debug 日志改为惰性表达式且限流；方法、字段及反射调用候选缓存；音量比例计算独立测试；扬声器内部掩码修正为 0x2；延迟纠正执行前再次核对开关、持有者和实际模式。

媒体 0 映射至原通话最低索引 1；不因此保证通信音频完全静音。关闭功能不会自动重连已经主动断开的听筒，该状态恢复仍需实测。

## 截图

| 功能 | Hook 与行为 |
| --- | --- |
| 官方回调 | Android 14+，ActivityRecord.onScreenCaptured / reportScreenCaptured 的 void 候选，before 返回 null 阻止分发；默认 true |
| 增强模式 | 每次调用检查开关和目标名单；默认 false；目标应用必须在作用域 |
| 观察者注册 | ContentResolver.registerContentObserver：media authority 且 URI 含截图关键词时跳过；泛媒体 URI 仍放行 |
| 媒体查询 | ContentResolver.query 的 media Cursor 包装为 ScreenshotFilteringCursor |
| 关键词 | screenshot、截屏、截图、pictures/screenshots、dcim/screenshots，忽略大小写 |
| 行识别列 | _display_name、title、relative_path、_data；无相关列时保留全部行 |
| Cursor 协议 | AbstractCursor 持有逻辑位置，onMove 映射物理行；支持 next/previous/first/last、计数、类型与数据读取、关闭和数据集更新 |

增强模式不修改媒体数据库，只修改目标应用看到的结果；关键词匹配可能包含名称带截图词的普通图片。回调拦截不阻止截图生成、保存，不解除 FLAG_SECURE，也不代表覆盖全部录屏检测。

`ScreenCaptureDetectionHooker` 保留为当前官方截图 Hook 的源码别名，不重复加载。

## DPM 与 ColorOS

| 类 / 方法 | 行为 |
| --- | --- |
| DevicePolicyManagerService.hasAccountsOnAnyUser | after 返回 false |
| enforceCanSetDeviceOwnerLocked | before 返回 null，跳过前置校验 |
| checkDeviceOwnerProvisioningPreConditionLocked | after 返回 0 |
| AppLockModel.isExceededLockLimit | before 返回 false |
| AppLockInfo.isAllowDefaultLock / isUseless | after 返回 true / false |

DPM 与桌面分别新增独立开关，默认 true 保留旧行为。桌面须为 com.android.launcher，且检测到 oppo.R / com.color.os.ColorBuild / oplus.R。DPM 不自行设置设备所有者，桌面 Hook 只针对最近任务锁规则。

## 配置、界面与诊断

[ConfigData.kt](../app/src/main/java/io/github/fairyxh/ZhangSystemHook/data/ConfigData.kt) 保留全部原键及默认方向，新增有效模块总开关、DPM/桌面独立开关和高级过滤开关。

- app：本地偏好镜像 + libxposed service 可写远程偏好；离线编辑记录 pending keys，连接后同步；已存在远程配置优先于无改动的旧镜像。
- target：API 102 只读远程偏好 + 强引用变更监听器 + 内存快照，热路径不轮询偏好、不重建名单集合。
- UI 写入先更新镜像和内存，后台串行提交远程配置，提交时不占用 UI setter 锁；同键并发修改只确认已提交版本。
- 首次迁移仅枚举模块应用自身 shared_prefs 中的已知键；不读取其他应用偏好。
- XML 导入实际持久化名单和支持的布尔键，导出使用 XML serializer 转义；支持旧版名单文件。
- 隐藏桌面图标只操作 .Home alias；激活状态基于框架服务 API 与配置连接，不冒充逐 Hook 实机成功。
- 六条无障碍检查、通知、模式与截图测试均保留。截图监听仅在 API >=34 创建，离开前台时释放。
- 应用列表后台读取、150ms 搜索防抖、用户/系统分类、64 项图标缓存与按可见行加载。
- MethodMonitor 默认关闭，启用后对已挂载的无障碍方法记录限流日志；源码工具仍可针对指定类补充安装。
- 默认不输出高频诊断字符串。show_notice 控制诊断与限流 Toast；安装/错误日志独立保留，方法监视器有独立开关。

## 验证边界

本地覆盖名单、共享 UID、音量边界、配置变更/重绑定/热读、拦截改参/提前返回/异常传播和同方法多功能注册。完整构建与设备验证结果见 [迁移记录](API102_MIGRATION.md)。

已在隔离 Android 16 模拟器验证页面启动、深浅色排版、开关触控、应用搜索和本地名单持久化；没有已连接的 LSPosed 测试设备。ROM 内部签名、真实电话/蓝牙/听筒状态、截图通知覆盖及其他 Android 版本运行情况，不能仅凭编译、单元测试或普通模拟器宣称通过。
