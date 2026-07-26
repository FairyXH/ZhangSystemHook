package io.github.fairyxh.ZhangSystemHook.hook

import android.content.Context
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/** 仅在 system_server 中阻止截图检测回调，不影响截图生成、保存或 MediaStore。 */
object ScreenCaptureDetectionHooker : YukiBaseHooker() {
    override fun onHook() {
        val activityRecord = "com.android.server.wm.ActivityRecord".toClass()
        hookNamedMethods(activityRecord, "onScreenCaptured")
        hookNamedMethods(activityRecord, "reportScreenCaptured")

        // 只记录 observer 注册，不阻止注册，否则模块 App 无法作为测试客户端。
        val activityTaskManager = runCatching {
            "com.android.server.wm.ActivityTaskManagerService".toClass()
        }.getOrNull()
        if (activityTaskManager != null) {
            hookObserverRegistration(activityTaskManager)
        } else {
            HookLog.w("ScreenCapture", "ActivityTaskManagerService class not found")
        }
    }

    private fun hookNamedMethods(clazz: Class<*>, methodName: String) {
        val candidates = allMethods(clazz).filter { it.name == methodName }.distinctBy { it.toGenericString() }
        if (candidates.isEmpty()) {
            HookLog.w("ScreenCapture", "candidate not found: ${clazz.name}.$methodName")
            return
        }
        candidates.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        val enabled = runCatching {
                            ConfigData.getBoolean(ConfigData.BLOCK_SCREEN_CAPTURE_DETECTION)
                        }.getOrDefault(false)
                        val action = if (enabled) "BLOCK" else "ALLOW"
                        HookLog.i(
                            "ScreenCapture",
                            "$action $methodName${method.parameterTypes.contentToString()} " +
                                "record=${describeRecord(instance)} args=${args.contentToString()}"
                        )
                        if (enabled) resultNull()
                    }
                }
                HookLog.i("ScreenCapture", "installed $methodName${method.parameterTypes.contentToString()}")
            }.onFailure {
                HookLog.e("ScreenCapture", "failed to hook $methodName ${method.toGenericString()}", it)
            }
        }
    }

    private fun hookObserverRegistration(clazz: Class<*>) {
        val candidates = allMethods(clazz)
            .filter { it.name == "registerScreenCaptureObserver" }
            .distinctBy { it.toGenericString() }
        if (candidates.isEmpty()) {
            HookLog.w("ScreenCapture", "candidate not found: ${clazz.name}.registerScreenCaptureObserver")
            return
        }
        candidates.forEach { method ->
            runCatching {
                method.isAccessible = true
                method.hook {
                    before {
                        HookLog.i(
                            "ScreenCapture",
                            "observer register ${method.parameterTypes.contentToString()} " +
                                "args=${args.contentToString()}"
                        )
                    }
                }
                HookLog.i("ScreenCapture", "installed registerScreenCaptureObserver${method.parameterTypes.contentToString()}")
            }.onFailure {
                HookLog.e("ScreenCapture", "failed to hook observer registration", it)
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

    private fun describeRecord(record: Any?): String = runCatching {
        record?.toString() ?: "<null>"
    }.getOrDefault("<unavailable>")
}
