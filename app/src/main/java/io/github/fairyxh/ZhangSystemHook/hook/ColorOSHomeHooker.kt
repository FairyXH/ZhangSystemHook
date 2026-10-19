package io.github.fairyxh.ZhangSystemHook.hook

import io.github.fairyxh.ZhangSystemHook.core.BaseHooker
import io.github.fairyxh.ZhangSystemHook.core.HookRuntime
import io.github.fairyxh.ZhangSystemHook.data.ConfigData

object ColorOSHomeHooker : BaseHooker() {
    override fun onHook() {
        if (listOf("oppo.R", "com.color.os.ColorBuild", "oplus.R").none { HookRuntime.findClass(it) != null }) return
        HookRuntime.named("com.oplus.quickstep.applock.AppLockModel", "isExceededLockLimit", before = {
            if (ConfigData.flag("launcher_enabled", true)) result = false
        }, filter = { it.returnType == Boolean::class.javaPrimitiveType })
        HookRuntime.named("com.oplus.quickstep.applock.AppLockInfo", "isAllowDefaultLock", after = {
            if (ConfigData.flag("launcher_enabled", true)) result = true
        }, filter = { it.returnType == Boolean::class.javaPrimitiveType })
        HookRuntime.named("com.oplus.quickstep.applock.AppLockInfo", "isUseless", after = {
            if (ConfigData.flag("launcher_enabled", true)) result = false
        }, filter = { it.returnType == Boolean::class.javaPrimitiveType })
    }
}
