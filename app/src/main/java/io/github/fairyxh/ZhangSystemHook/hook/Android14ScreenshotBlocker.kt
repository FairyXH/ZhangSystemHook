package io.github.fairyxh.ZhangSystemHook.hook

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import io.github.fairyxh.ZhangSystemHook.data.ScreenshotConfig
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/** Suppresses Android 14 ScreenCaptureCallback dispatch in system_server. */
object Android14ScreenshotBlocker : YukiBaseHooker() {
    private const val TAG = "ScreenshotBlocker"

    override fun onHook() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            HookLog.i(TAG, "[Android14] skipped: SDK=${Build.VERSION.SDK_INT}")
            return
        }
        if (!ScreenshotConfig.enableAndroid14Blocker) {
            HookLog.i(TAG, "[Android14] disabled")
            return
        }
        hookScreenCaptureCallback()
    }

    private fun hookScreenCaptureCallback() {
        val activityRecord = runCatching {
            "com.android.server.wm.ActivityRecord".toClass()
        }.getOrElse {
            HookLog.e(TAG, "[Android14] ActivityRecord unavailable", it)
            return
        }
        val candidates = allMethods(activityRecord)
            .filter { it.name == "onScreenCaptured" || it.name == "reportScreenCaptured" }
            .distinctBy(Method::toGenericString)
        if (candidates.isEmpty()) {
            HookLog.w(TAG, "[Android14] ScreenCaptureCallback dispatch method not found")
            return
        }
        candidates.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        resultNull()
                        HookLog.i(TAG, "[Android14] ScreenCaptureCallback blocked")
                    }
                }
                HookLog.i(TAG, "[Android14] ScreenCaptureCallback hooked: ${method.name}")
            }.onFailure {
                HookLog.e(TAG, "[Android14] hook failed: ${method.toGenericString()}", it)
            }
        }
    }

    private fun allMethods(clazz: Class<*>): List<Method> = buildList {
        var current: Class<*>? = clazz
        while (current != null) {
            addAll(current.declaredMethods.filter { !Modifier.isAbstract(it.modifiers) })
            current = current.superclass
        }
    }
}