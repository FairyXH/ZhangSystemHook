package io.github.fairyxh.ZhangSystemHook.ui.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Switch
import io.github.fairyxh.ZhangSystemHook.BuildConfig
import io.github.fairyxh.ZhangSystemHook.application.SystemNotifier
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.ui.IosActivity
import io.github.fairyxh.ZhangSystemHook.utils.tool.FrameworkTool
import java.util.concurrent.Executors

class MainActivity : IosActivity() {
    private lateinit var status: TextView
    private lateinit var checks: TextView
    private lateinit var testResult: TextView
    private val switches = mutableMapOf<String, Switch>()
    private val worker = Executors.newSingleThreadExecutor()
    private var capture: AutoCloseable? = null
    private val frameworkChanged: () -> Unit = { runOnUiThread { refreshStatus(); refreshSwitches() } }
    private val configChanged: () -> Unit = { runOnUiThread { refreshSwitches() } }
    private val alias get() = ComponentName(this, "$packageName.Home")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        page("设置", "ZhangSystemHook  ·  API 102")
        group("模块状态") { card ->
            status = label("正在连接框架…", 20, true).apply { setPadding(dp(18), dp(18), dp(18), dp(6)) }
            card.addView(status)
            card.addView(label("版本 ${BuildConfig.VERSION_NAME}  ·  Android ${Build.VERSION.RELEASE}", 12, color = secondary).apply {
                setPadding(dp(18), 0, dp(18), dp(18))
            })
            setting(card, "_enable_module", "启用模块", "统一控制所有 Hook", true)
        }
        group("无障碍", "用户应用：勾选允许读取。系统应用默认：勾选阻止读取；开启系统白名单后，勾选允许读取。") { card ->
            setting(card, "accessibility_hook_enable", "无障碍防检测", "隐藏服务列表与系统启用状态")
            setting(card, "high_performance_mode", "高性能模式", "跳过节点与事件级 Hook；建议保持开启", true)
            setting(card, "allow_client_method", "允许 addClient", "允许应用注册无障碍客户端")
            setting(card, "system_app_whitelist", "系统应用白名单", "系统应用仅允许名单中的包")
            setting(card, "block_system", "阻止系统组件读取", "对白名单模式下 android. 系统组件应用规则")
            row(card, "应用名单", "搜索、分类与逐应用设置") { startActivity(Intent(this, AppsConfigActivity::class.java)) }
        }
        group("截屏与音频", "增强截屏需要将目标应用加入 LSPosed 作用域并重启应用。系统回调拦截适用于 Android 14 及以上。") { card ->
            setting(card, "enable_android14_screenshot_blocker", "截屏回调防检测", "阻止系统向应用分发截图事件", true)
            setting(card, "enable_enhanced_screenshot_blocker", "增强截屏防检测", "过滤名单应用的媒体查询与截图 URI 观察者")
            setting(card, "block_third_party_communication_mode", "禁止应用切换通话模式", "普通应用回落至媒体模式，保留系统与蓝牙适配")
            row(card, "申请目标应用作用域", "由框架显示授权界面") {
                FrameworkTool.requestScope(ConfigData.blockApps.data.filter { it != packageName }) { toast(it) }
            }
        }
        group("系统增强") { card ->
            setting(card, "dpm_enabled", "设备策略 Hook", "调整设备所有者配置前置检查", true)
            setting(card, "launcher_enabled", "ColorOS 最近任务锁", "解除锁定数量限制与默认锁判断", true)
        }
        group("显示与诊断") { card ->
            val hidden = packageManager.getComponentEnabledSetting(alias) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            toggle(card, "隐藏桌面图标", "仍可从 LSPosed 模块设置进入", hidden) {
                packageManager.setComponentEnabledSetting(alias, if (it) PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    else PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
            }
            setting(card, "show_notice", "显示 Hook 日志与提示", "诊断日志及 Toast 已限流")
            setting(card, "MothodMonitor_SW", "方法监视器", "记录无障碍调用；默认关闭")
            row(card, "导出配置", "包含功能开关和应用名单") {
                startActivityForResult(Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    type = "text/xml"; addCategory(Intent.CATEGORY_OPENABLE); putExtra(Intent.EXTRA_TITLE, "ZhangSystemHook.xml")
                }, 100)
            }
            row(card, "导入配置", "兼容旧版名单 XML") {
                startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "*/*"; addCategory(Intent.CATEGORY_OPENABLE)
                }, 101)
            }
        }
        group("功能测试") { card ->
            row(card, "重新检查无障碍") { runChecks() }
            row(card, "通知测试") { SystemNotifier.sendUserMsg("ZhangSystemHook · API 102", this) }
            row(card, "通话模式测试", "点按请求通信模式，再次点按恢复普通模式") {
                runCatching {
                    val audio = getSystemService(AudioManager::class.java)
                    val request = if (audio.mode == AudioManager.MODE_IN_COMMUNICATION) AudioManager.MODE_NORMAL else AudioManager.MODE_IN_COMMUNICATION
                    audio.mode = request
                    testResult.text = "请求 mode=$request，实际 mode=${audio.mode}"
                }.onFailure { testResult.text = "测试失败：${it.javaClass.simpleName}" }
            }
            row(card, "截屏回调测试", "保持此页前台，使用系统截屏") {
                testResult.text = if (capture != null) "监听已注册，等待实际截屏" else "当前系统不支持或监听注册失败"
            }
            testResult = label("等待测试", 12, color = secondary).apply { setPadding(dp(16), dp(12), dp(16), dp(16)) }
            card.addView(testResult)
        }
        group("无障碍检查") { card ->
            checks = label("检查中…", 13, color = secondary).apply { setPadding(dp(16), dp(16), dp(16), dp(16)); setTextIsSelectable(true) }
            card.addView(checks)
        }
        FrameworkTool.observe(frameworkChanged)
        ConfigData.observe(configChanged)
        refreshStatus()
    }
    private fun setting(parent: LinearLayout, key: String, title: String, detail: String, default: Boolean = false) {
        switches[key] = toggle(parent, title, detail, ConfigData.flag(key, default)) { ConfigData.setFlag(key, it) }
    }
    private fun refreshSwitches() {
        switches.forEach { (key, view) ->
            val default = key in setOf("_enable_module", "high_performance_mode", "enable_android14_screenshot_blocker", "dpm_enabled", "launcher_enabled")
            val checked = ConfigData.flag(key, default)
            if (view.isChecked != checked) view.isChecked = checked
        }
    }
    private fun refreshStatus() {
        val framework = FrameworkTool.service
        status.text = when {
            framework == null -> "框架未连接"
            !FrameworkTool.configReady -> "框架已连接 · 配置同步失败"
            else -> "${framework.frameworkName} · API ${framework.apiVersion}"
        }
        status.setTextColor(if (FrameworkTool.configReady) 0xFF34C759.toInt() else secondary)
    }
    private fun runChecks() {
        worker.execute {
            val manager = getSystemService(AccessibilityManager::class.java)
            val tests = listOf<Pair<String, () -> Any?>>(
                "isEnabled" to { manager.isEnabled },
                "getEnabledAccessibilityServiceList" to { manager.getEnabledAccessibilityServiceList(-1).map { it.id } },
                "getAccessibilityServiceList" to { @Suppress("DEPRECATION") manager.accessibilityServiceList.map { it.packageName } },
                "isTouchExplorationEnabled" to { manager.isTouchExplorationEnabled },
                "ACCESSIBILITY_ENABLED" to { Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0) },
                "ENABLED_ACCESSIBILITY_SERVICES" to { Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) }
            )
            val result = tests.joinToString("\n\n") { (name, read) ->
                "$name\n${runCatching { read()?.toString()?.ifEmpty { "未检出" } ?: "未检出" }.getOrElse { "读取失败：${it.javaClass.simpleName}" }}"
            }
            runOnUiThread { if (!isDestroyed) checks.text = result }
        }
    }
    override fun onResume() { super.onResume(); refreshStatus(); runChecks() }
    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= 34) runCatching {
            capture = CaptureWatcher(this) { testResult.text = "收到系统截图回调" }
        }.onFailure { testResult.text = "截图监听注册失败：${it.javaClass.simpleName}" }
    }
    override fun onStop() { capture?.close(); capture = null; super.onStop() }
    override fun onDestroy() {
        FrameworkTool.removeObserver(frameworkChanged); ConfigData.removeObserver(configChanged)
        worker.shutdownNow()
        super.onDestroy()
    }
    @Deprecated("Activity result API retained for native Activity")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        val uri = data?.data ?: return
        worker.execute {
            val success = if (requestCode == 100) ConfigData.exportConfig(this, uri) else ConfigData.importConfig(this, uri)
            toast(if (success) "配置${if (requestCode == 100) "导出" else "导入"}成功" else "配置操作失败")
        }
    }
    @android.annotation.TargetApi(34)
    private class CaptureWatcher(private val activity: Activity, callback: () -> Unit) : AutoCloseable {
        private val listener = Activity.ScreenCaptureCallback { callback() }
        init { activity.registerScreenCaptureCallback(activity.mainExecutor, listener) }
        override fun close() { activity.unregisterScreenCaptureCallback(listener) }
    }
}
