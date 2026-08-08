package io.github.fairyxh.ZhangSystemHook.hook

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import io.github.fairyxh.ZhangSystemHook.data.ScreenshotConfig
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/** Suppresses Android 15+ ScreenCaptureCallback dispatch in system_server. */
object Android14ScreenshotBlocker : YukiBaseHooker() {
    private const val TAG = "ScreenshotBlocker"

    override fun onHook() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            HookLog.i(TAG, "[Android15] skipped: Android 15+ required, SDK=${Build.VERSION.SDK_INT}")
            return
        }
        HookLog.i(
            TAG,
            "[Android15] installing; enabled=${readEnabled()} SDK=${Build.VERSION.SDK_INT}"
        )
        hookScreenCaptureCallback()
    }

    private fun hookScreenCaptureCallback() {
        val activityRecord = runCatching {
            "com.android.server.wm.ActivityRecord".toClass()
        }.getOrElse {
            HookLog.e(TAG, "[Android15] ActivityRecord unavailable", it)
            return
        }
        val candidates = allMethods(activityRecord)
            .filter { it.name == "onScreenCaptured" || it.name == "reportScreenCaptured" }
            .distinctBy(Method::toGenericString)
        if (candidates.isEmpty()) {
            HookLog.w(TAG, "[Android15] ScreenCaptureCallback dispatch method not found")
            return
        }
        candidates.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        val enabled = readEnabled()
                        HookLog.i(
                            TAG,
                            "[Android15] ${if (enabled) "BLOCK" else "ALLOW"} " +
                                "${method.name}${method.parameterTypes.contentToString()} " +
                                "args=${args.contentToString()}"
                        )
                        if (enabled) resultNull()
                    }
                }
                HookLog.i(TAG, "[Android15] ScreenCaptureCallback hooked: ${method.name}")
            }.onFailure {
                HookLog.e(TAG, "[Android15] hook failed: ${method.toGenericString()}", it)
            }
        }
    }

    private fun readEnabled(): Boolean = runCatching {
        ScreenshotConfig.enableAndroid14Blocker
    }.onFailure {
        HookLog.e(TAG, "[Android15] failed to read current switch state; allowing callback", it)
    }.getOrDefault(false)

    private fun allMethods(clazz: Class<*>): List<Method> = buildList {
        var current: Class<*>? = clazz
        while (current != null) {
            addAll(current.declaredMethods.filter { !Modifier.isAbstract(it.modifiers) })
            current = current.superclass
        }
    }
}