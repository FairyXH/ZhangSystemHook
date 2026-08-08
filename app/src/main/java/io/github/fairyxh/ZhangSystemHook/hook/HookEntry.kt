package io.github.fairyxh.ZhangSystemHook.hook
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.StringType
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import io.github.fairyxh.ZhangSystemHook.application.SystemNotifier
import io.github.fairyxh.ZhangSystemHook.BuildConfig
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.data.ScreenshotConfig
import io.github.fairyxh.ZhangSystemHook.utils.factory.isNotColorOS
import de.robv.android.xposed.XposedBridge


object HookLog {
    private const val PREFIX = "[ZhangSystemHook]"

    fun d(tag: String, msg: String) = XposedBridge.log("$PREFIX/$tag: $msg")
    fun i(tag: String, msg: String) = XposedBridge.log("$PREFIX/$tag: $msg")
    fun w(tag: String, msg: String) = XposedBridge.log("$PREFIX/$tag: WARN $msg")
    fun e(tag: String, msg: String, tr: Throwable? = null) {
        XposedBridge.log("$PREFIX/$tag: ERROR $msg")
        tr?.let { XposedBridge.log(it) }
    }
}

@InjectYukiHookWithXposed(entryClassName = "ZhangSystemHook", isUsingResourcesHook = false)
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        debugLog {
            tag = "ZhangSystemHook"
            elements(TAG, PRIORITY)
        }
        YukiHookAPI.Configs.apply {
            isDebug = false
        }
    }

    override fun onHook() = encase {
        YukiHookAPI.Configs.isDebug = false

        SystemNotifier.sendUserMsg(msg = "ZhangSystemHook 开始运行")
        var targetPackages = emptySet<String>()
        loadSystem {
            ConfigData.init(this)
            targetPackages = ScreenshotConfig.targetPackages.toSet()
            loadHooker(AccessibilityHooker)
            loadHooker(DPMHooker)
            loadHooker(Android14ScreenshotBlocker)
            loadHooker(AudioCommunicationModeHooker)
        }
        targetPackages
            .filter { it != BuildConfig.APPLICATION_ID }
            .forEach { targetPackage ->
                loadApp(targetPackage) {
                    ConfigData.init(instance = this)
                    if (ScreenshotConfig.enableEnhancedBlocker) {
                        loadHooker(EnhancedScreenshotBlocker)
                    }
                }
            }
        loadApp("com.android.launcher") {
            ConfigData.init(instance = this)
            if (isNotColorOS) {
                SystemNotifier.sendUserMsg(msg ="非ColorOS系统,不加载ColorOS的功能")
            } else {
                loadHooker(ColorOSHomeHooker)
            }
        }
    }

}