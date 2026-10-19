package io.github.fairyxh.ZhangSystemHook.hook

import io.github.fairyxh.ZhangSystemHook.core.BaseHooker
import io.github.fairyxh.ZhangSystemHook.core.HookRuntime
import io.github.fairyxh.ZhangSystemHook.data.ConfigData

object DPMHooker : BaseHooker() {
    override fun onHook() {
        val type = "com.android.server.devicepolicy.DevicePolicyManagerService"
        HookRuntime.named(type, "hasAccountsOnAnyUser", after = {
            if (ConfigData.flag("dpm_enabled", true)) result = false
        }, filter = { it.returnType == Boolean::class.javaPrimitiveType })
        HookRuntime.named(type, "enforceCanSetDeviceOwnerLocked", before = {
            if (ConfigData.flag("dpm_enabled", true)) resultNull()
        })
        HookRuntime.named(type, "checkDeviceOwnerProvisioningPreConditionLocked", after = {
            if (ConfigData.flag("dpm_enabled", true)) result = 0
        }, filter = { it.returnType == Int::class.javaPrimitiveType })
    }
}
