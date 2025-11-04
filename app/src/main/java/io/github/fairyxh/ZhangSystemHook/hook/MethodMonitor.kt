package io.github.fairyxh.ZhangSystemHook.hook
import android.content.pm.ApplicationInfo
import android.os.Binder
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import io.github.fairyxh.ZhangSystemHook.application.SystemNotifier
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.hook.AccessibilityHooker.systemContext
import java.lang.reflect.Method
import java.util.WeakHashMap

object MethodMonitor {
    fun isSystemApp(packageName: String): Boolean {
        return try {
            val appInfo = systemContext.packageManager.getApplicationInfo(packageName, 0)
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) ||
                    (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0)
        } catch (e: Exception) {
            true
        }
    }
    val MothodMonitor_SW = PrefsData("MothodMonitor_SW", false)
    var Allow_MothodMonitor = ConfigData.getBoolean(MothodMonitor_SW)

    val packageManager = systemContext.packageManager
    val uidToNameCache = WeakHashMap<Int, String>(1024)
    val pkgName = {
        Binder.getCallingUid().let { uid ->
            uidToNameCache.computeIfAbsent(uid) { _ ->
                packageManager.getNameForUid(uid)
            }
        }
    }
    private val SYSTEM_PROCESSES = setOf(
        "android", // 系统进程
        "com.android.systemui", // 系统UI进程
        "com.android.server", // 系统服务进程
        "zygote", // Zygote进程
        "system" // Zygote进程
    )

    fun monitorClassMethods(targetClass: Class<*>, className: String) {
        if (!Allow_MothodMonitor) {
            return
        }

        val currentPkg = pkgName()
        if (SYSTEM_PROCESSES.any { currentPkg.startsWith(it) }) {
            return
        }

        if(isSystemApp(currentPkg)){
            return
        }

        try {
            val simpleClassName = className.substringAfterLast('.')
            targetClass.declaredMethods.forEach { method: Method ->
                if (method.declaringClass.name != "java.lang.Object") {
                    hookSingleMethod(className, method, simpleClassName)
                }
            }
        } catch (e: Exception) {
            SystemNotifier.sendUserMsg("监视类 $className 失败: ${e.message}")
        }
    }
    private fun hookSingleMethod(
        className: String,
        method: Method,
        simpleClassName: String
    ) {
        try {
            XposedHelpers.findAndHookMethod(
                className,
                null,
                method.name,
                *method.parameterTypes,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val logMsg = "($pkgName)调用类: $simpleClassName -> 方法: ${method.name}"
                        SystemNotifier.sendUserMsg(logMsg)
                    }
                }
            )
        } catch (e: Exception) {
            SystemNotifier.sendUserMsg(
                "挂钩方法 ${simpleClassName}.${method.name} 失败: ${e.message}"
            )
        }
    }
}
