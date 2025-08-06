package io.github.fairyxh.ZhangSystemHook.hook

 import android.annotation.SuppressLint
 import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
 import com.highcapable.yukihookapi.hook.log.YLog
 import com.highcapable.yukihookapi.hook.log.YLog.Configs.isEnable
 import com.highcapable.yukihookapi.hook.log.YLog.Configs.isRecord
 import com.highcapable.yukihookapi.hook.type.java.BooleanType
 import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
 import io.github.fairyxh.ZhangSystemHook.application.SystemNotifier
 import io.github.fairyxh.ZhangSystemHook.data.ConfigData

@InjectYukiHookWithXposed(entryClassName = "ZhangSystemHook", isUsingResourcesHook = false)
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        debugLog {
            tag = "ZhangSystemHook"
            elements(TAG, PRIORITY)
        }
    }

    override fun onHook() = encase {
        loadSystem {
            ConfigData.init(this)
            SystemNotifier.sendUserMsg(msg = "ZhangSystemHook 开始运行")
            loadHooker(AccessibilityHooker)
            loadHooker(DPMHooker)
        }

    }

}