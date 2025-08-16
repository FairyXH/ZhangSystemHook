package io.github.fairyxh.ZhangSystemHook.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import io.github.fairyxh.ZhangSystemHook.application.SystemNotifier
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.utils.factory.isNotColorOS

@InjectYukiHookWithXposed(entryClassName = "ZhangSystemHook", isUsingResourcesHook = false)
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        debugLog {
            tag = "ZhangSystemHook"
            elements(TAG, PRIORITY)
        }
    }

    override fun onHook() = encase {
        SystemNotifier.sendUserMsg(msg = "ZhangSystemHook 开始运行")
        loadSystem {
            ConfigData.init(this)
            loadHooker(AccessibilityHooker)
            loadHooker(DPMHooker)
        }
        loadApp("com.android.launcher") {
            ConfigData.init(instance = this)
            if (isNotColorOS) return@encase SystemNotifier.sendUserMsg(msg ="非ColorOS系统,不加载ColorOS的功能")
            if (ConfigData.isEnableModule)
                loadHooker(ColorOSHomeHooker)
        }

    }

}