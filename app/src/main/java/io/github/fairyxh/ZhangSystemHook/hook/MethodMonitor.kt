package io.github.fairyxh.ZhangSystemHook.hook

import io.github.fairyxh.ZhangSystemHook.core.HookRuntime
import io.github.fairyxh.ZhangSystemHook.data.ConfigData

object MethodMonitor {
    fun monitorClassMethods(targetClass: Class<*>, className: String) {
        if (!ConfigData.flag("MothodMonitor_SW")) return
        HookRuntime.methods(targetClass).filter { it.declaringClass != Any::class.java }.forEach { method ->
            HookRuntime.install(method, before = {
                HookRuntime.diagnostic("MethodMonitor") { "$className.${method.name}" }
            })
        }
    }
}
