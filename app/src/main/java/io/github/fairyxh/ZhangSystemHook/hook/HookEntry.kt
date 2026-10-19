package io.github.fairyxh.ZhangSystemHook.hook

import android.util.Log
import io.github.fairyxh.ZhangSystemHook.BuildConfig
import io.github.fairyxh.ZhangSystemHook.core.BaseHooker
import io.github.fairyxh.ZhangSystemHook.core.HookRuntime
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.*

object HookLog {
    fun d(tag: String, msg: () -> String) = HookRuntime.diagnostic(tag, message = msg)
    fun i(tag: String, msg: () -> String) = HookRuntime.diagnostic(tag, message = msg)
    fun w(tag: String, msg: String) = HookRuntime.log(Log.WARN, tag, msg)
    fun e(tag: String, msg: String, tr: Throwable? = null) = HookRuntime.log(Log.ERROR, tag, msg, tr)
}

class HookEntry : XposedModule() {
    private var systemInstalled = false
    private val packages = mutableSetOf<String>()
    override fun onModuleLoaded(param: ModuleLoadedParam) {
        HookRuntime.api = this
        runCatching { ConfigData.bind(getRemotePreferences(ConfigData.GROUP)) }
            .onFailure { HookRuntime.log(Log.ERROR, "Module", "Remote preferences unavailable; defaults in use", it) }
    }
    override fun onSystemServerStarting(param: SystemServerStartingParam) {
        if (systemInstalled) return
        systemInstalled = true
        HookRuntime.classLoader = param.classLoader
        HookRuntime.packageName = "android"
        install(AccessibilityHooker, DPMHooker, Android14ScreenshotBlocker, AudioCommunicationModeHooker)
    }
    override fun onPackageReady(param: PackageReadyParam) {
        if (!param.isFirstPackage || !packages.add(param.packageName)) return
        HookRuntime.classLoader = param.classLoader
        HookRuntime.packageName = param.packageName
        if (param.packageName == BuildConfig.APPLICATION_ID) return
        HookRuntime.named("android.app.Application", "attach", after = {
            HookRuntime.context = arg(0) as? android.content.Context
        })
        install(AccessibilityHooker)
        if (param.packageName == "com.android.launcher") install(ColorOSHomeHooker)
        // Install once in every scoped app; target membership and switches are checked per call.
        install(EnhancedScreenshotBlocker)
    }
    private fun install(vararg hookers: BaseHooker) = hookers.forEach {
        runCatching { it.onHook() }.onFailure { error ->
            HookRuntime.log(Log.ERROR, "Module", "Failed: ${it.javaClass.simpleName}", error)
        }
    }
}
