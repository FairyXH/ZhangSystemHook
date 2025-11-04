package io.github.fairyxh.ZhangSystemHook.hook
import android.util.Log
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.StringType
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import io.github.fairyxh.ZhangSystemHook.application.SystemNotifier
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.utils.factory.isNotColorOS


object NoLog {
    fun d(tag: String, msg: String) {}
    fun i(tag: String, msg: String) {}
    fun w(tag: String, msg: String) {}
    fun e(tag: String, msg: String, tr: Throwable? = null) {}
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
        loadSystem {
            ConfigData.init(this)
            loadHooker(AccessibilityHooker)
            loadHooker(DPMHooker)
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