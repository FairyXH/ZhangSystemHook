package io.github.fairyxh.ZhangSystemHook.utils.tool

import android.util.Log
import io.github.fairyxh.ZhangSystemHook.core.HookRuntime
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.libxposed.service.XposedService
import io.github.libxposed.service.XposedServiceHelper
import java.util.concurrent.CopyOnWriteArraySet

object FrameworkTool {
    @Volatile var service: XposedService? = null
        private set
    @Volatile var configReady = false
        private set
    private val listeners = CopyOnWriteArraySet<() -> Unit>()
    fun observe(callback: () -> Unit) { listeners.add(callback) }
    fun removeObserver(callback: () -> Unit) { listeners.remove(callback) }
    fun connect() {
        XposedServiceHelper.registerListener(object : XposedServiceHelper.OnServiceListener {
            override fun onServiceBind(service: XposedService) {
                FrameworkTool.service = service
                configReady = runCatching {
                    check(service.apiVersion >= 102)
                    ConfigData.connect(service.getRemotePreferences(ConfigData.GROUP))
                    true
                }.getOrElse { HookRuntime.log(Log.ERROR, "Framework", "Configuration service unavailable", it); false }
                listeners.forEach { it() }
            }
            override fun onServiceDied(service: XposedService) {
                FrameworkTool.service = null
                configReady = false
                ConfigData.disconnect()
                listeners.forEach { it() }
            }
        })
    }
    fun requestScope(packages: List<String>, callback: (String) -> Unit) {
        val framework = service ?: return callback("框架未连接，请先在 LSPosed 中启用模块")
        runCatching {
            // Modern API names system_server "system"; "android" is the ordinary framework package.
            val requested = (listOf("system", "com.android.providers.settings", "com.android.launcher") + packages).distinct()
            framework.requestScope(requested, object : XposedService.OnScopeEventListener {
                override fun onScopeRequestApproved(approved: List<String>) = callback("作用域已更新，请重启目标进程")
                override fun onScopeRequestFailed(message: String) = callback(message)
            })
        }.onFailure { callback(it.message ?: "作用域请求失败") }
    }
}
