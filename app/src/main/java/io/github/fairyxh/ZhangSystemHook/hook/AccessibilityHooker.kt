package io.github.fairyxh.ZhangSystemHook.hook

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.database.MatrixCursor
import android.graphics.Rect
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import io.github.fairyxh.ZhangSystemHook.BuildConfig
import io.github.fairyxh.ZhangSystemHook.application.BootStateChecker
import io.github.fairyxh.ZhangSystemHook.core.AccessPolicy
import io.github.fairyxh.ZhangSystemHook.core.BaseHooker
import io.github.fairyxh.ZhangSystemHook.core.HookCall
import io.github.fairyxh.ZhangSystemHook.core.HookRuntime
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import java.util.concurrent.ConcurrentHashMap

object AccessibilityHooker : BaseHooker() {
    private data class Caller(val packages: List<String>, val system: Boolean, val time: Long)
    private val callers = ConcurrentHashMap<Int, Caller>()
    @Volatile private var bootReady = false
    @Volatile private var bootChecked = -5000L
    val systemContext: Context get() = HookRuntime.systemContext() ?: error("System context not ready")

    private fun blocked(client: Boolean = false, extended: Boolean = false): Boolean {
        if (!ConfigData.flag("accessibility_hook_enable")) return false
        if (extended && ConfigData.flag("high_performance_mode", true)) return false
        if (client && ConfigData.flag("allow_client_method")) return false
        val context = HookRuntime.systemContext() ?: return false
        val now = SystemClock.elapsedRealtime()
        if (now - bootChecked >= 5000) {
            bootChecked = now
            bootReady = BootStateChecker.isBootCompletedAndUnlocked(context)
        }
        if (!bootReady) return false
        val names: List<String>
        val system: Boolean
        if (HookRuntime.packageName == "android" || HookRuntime.packageName == "com.android.providers.settings") {
            val uid = Binder.getCallingUid()
            if (uid < 10000) return false
            val caller = callers[uid]?.takeIf { now - it.time < 60000 } ?: runCatching {
                val packages = context.packageManager.getPackagesForUid(uid)?.toList().orEmpty()
                val flags = ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
                val isSystem = packages.any { context.packageManager.getApplicationInfo(it, 0).flags and flags != 0 }
                Caller(packages, isSystem, now).also {
                    if (callers.size > 512) callers.clear()
                    callers[uid] = it
                }
            }.getOrNull() ?: return false
            names = caller.packages
            system = caller.system
        } else {
            names = listOf(HookRuntime.packageName)
            system = context.applicationInfo.flags and (ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
        }
        return !AccessPolicy.allows(names, BuildConfig.APPLICATION_ID, ConfigData.blockApps.data, system,
            ConfigData.flag("system_app_whitelist"), ConfigData.flag("block_system"))
    }

    override fun onHook() {
        if (HookRuntime.packageName == "android") {
            val service = "com.android.server.accessibility.AccessibilityManagerService"
            HookRuntime.named(service, "addClient", before = {
                if (blocked(client = true)) result = 0L
            }, filter = { it.returnType == Long::class.javaPrimitiveType })
            HookRuntime.named(service, "getInstalledAccessibilityServiceList", before = {
                if (blocked()) result = emptyServices(method.returnType)
            })
            HookRuntime.named(service, "getEnabledAccessibilityServiceList", before = {
                if (blocked()) result = emptyServices(method.returnType)
            })
        }
        hookSettingsTransport()
        if (HookRuntime.packageName != "android") {
            hookAppQueries()
            // Heavy node hooks are installed only when requested. Once installed, the switch is checked per call.
            val installExtended = {
                if (!ConfigData.flag("high_performance_mode", true)) hookNodes()
            }
            installExtended()
            ConfigData.observe(installExtended)
            val installOptional = { if (ConfigData.flag("extended_filter_enabled")) hookOptionalFilters() }
            installOptional()
            ConfigData.observe(installOptional)
        }
    }

    private fun emptyServices(type: Class<*>): Any =
        if (List::class.java.isAssignableFrom(type)) emptyList<Any>()
        else type.getConstructor(List::class.java).newInstance(emptyList<Any>())

    private fun hookSettingsTransport() {
        HookRuntime.named("android.content.ContentProvider\$Transport", "call", after = {
            if (!blocked()) return@named
            val strings = args.filterIsInstance<String>()
            if ("GET_secure" !in strings) return@named
            val key = strings.firstOrNull { it == Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES ||
                it == Settings.Secure.ACCESSIBILITY_ENABLED } ?: return@named
            (result as? Bundle)?.apply {
                putString("value", if (key == Settings.Secure.ACCESSIBILITY_ENABLED) "0" else "")
                putInt("_generation_index", -1)
            }
        })
    }

    private fun hookAppQueries() {
        val manager = "android.view.accessibility.AccessibilityManager"
        HookRuntime.named(manager, "isEnabled", "isTouchExplorationEnabled", before = {
            if (blocked()) result = false
        }, filter = { it.returnType == Boolean::class.javaPrimitiveType })
        HookRuntime.named(manager, "getInstalledAccessibilityServiceList", "getAccessibilityServiceList",
            "getEnabledAccessibilityServiceList", before = { if (blocked()) result = emptyServices(method.returnType) })
        HookRuntime.named("android.provider.Settings\$Secure", "getInt", "getIntForUser", after = {
            if (blocked() && args.filterIsInstance<String>().any {
                it == Settings.Secure.ACCESSIBILITY_ENABLED ||
                    (!ConfigData.flag("high_performance_mode", true) && it.contains("accessibility"))
            }) result = 0
        }, filter = { it.returnType == Int::class.javaPrimitiveType })
        HookRuntime.named("android.provider.Settings\$Secure", "getString", "getStringForUser", after = {
            if (!blocked()) return@named
            when {
                args.any { it == Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES } -> result = ""
                args.any { it == Settings.Secure.ACCESSIBILITY_ENABLED } -> result = "0"
            }
        })
        HookRuntime.named("android.view.accessibility.IAccessibilityManager\$Stub\$Proxy",
            "getEnabledAccessibilityServiceList", "getInstalledAccessibilityServiceList", before = {
                if (blocked()) result = emptyServices(method.returnType)
            })
        HookRuntime.named("android.view.accessibility.IAccessibilityManager\$Stub\$Proxy", "addClient", before = {
            if (blocked(client = true)) result = 0L
        }, filter = { it.returnType == Long::class.javaPrimitiveType })
        HookRuntime.named("android.content.ContentResolver", "query", after = {
            if (!blocked(extended = true)) return@named
            val uri = args.firstOrNull { it is Uri } as? Uri ?: return@named
            val keys = args.flatMap { value -> when (value) {
                is String -> listOf(value)
                is Array<*> -> value.filterIsInstance<String>()
                else -> emptyList()
            } }
            if (uri.authority in setOf("settings", "com.android.settings.provider.settings") &&
                (uri.toString().contains("accessibility", true) || keys.any { it.contains("accessibility", true) })) {
                (result as? android.database.Cursor)?.close()
                result = MatrixCursor(arrayOf("value"))
            }
        })
        HookRuntime.named("android.app.ApplicationPackageManager", "queryIntentServices", after = {
            if (blocked(extended = true) && args.filterIsInstance<Intent>().any {
                it.action == "android.accessibilityservice.AccessibilityService" }) result = emptyList<Any>()
        })
        HookRuntime.named("android.app.ApplicationPackageManager", "getPackageInfo", after = {
            if (blocked(extended = true) && result is PackageInfo) {
                val originalPackage = (result as PackageInfo).packageName
                result = PackageInfo().apply {
                    packageName = originalPackage
                    versionName = ""; services = emptyArray(); permissions = emptyArray()
                    applicationInfo = ApplicationInfo().apply { packageName = originalPackage }
                }
            }
        })
        HookRuntime.named("android.app.ActivityManager", "getRunningServices", after = {
            if (blocked(extended = true)) result = (result as? List<*>)?.filter {
                val service = (it as? android.app.ActivityManager.RunningServiceInfo)?.service?.flattenToString()
                    ?: it.toString()
                !service.contains("accessibility", true)
            } ?: emptyList<Any>()
        })
        HookRuntime.named("android.os.ServiceManager", "getService", after = {
            if (arg(0) == "accessibility" && blocked(extended = true)) result = Binder()
        })
        HookRuntime.named("android.content.ContextWrapper", "registerReceiver", before = {
            if (blocked(extended = true)) {
                val index = args.indexOfFirst { it is IntentFilter }
                val filter = args.getOrNull(index) as? IntentFilter ?: return@named
                if ((0 until filter.countActions()).any { filter.getAction(it).contains("ACCESSIBILITY", true) ||
                    filter.getAction(it) == "android.settings.SETTINGS_CHANGED" }) args[index] = IntentFilter("invalid.action")
            }
        })
        HookRuntime.named("android.content.ContextImpl", "getSystemService", before = {
            if (arg(0) == Context.ACCESSIBILITY_SERVICE && blocked(extended = true)) resultNull()
        })
        HookRuntime.named("android.accessibilityservice.AccessibilityServiceInfo", "loadDescription", before = {
            if (blocked(extended = true)) result = ""
        })
        HookRuntime.named("android.accessibilityservice.AccessibilityServiceInfo", "getResolveInfo", before = {
            if (blocked(extended = true)) resultNull()
        })
        HookRuntime.named("android.accessibilityservice.AccessibilityServiceInfo", "toString", before = {
            if (blocked(extended = true)) result = "AccessibilityServiceInfo{eventTypes=0, feedbackTypes=0, capabilities=0}"
        })
        HookRuntime.named(manager, "addAccessibilityStateChangeListener", "addTouchExplorationStateChangeListener",
            "notifyTouchExplorationStateChanged", "notifyAccessibilityStateChanged", before = {
                if (blocked(extended = true)) result = if (method.returnType == Boolean::class.javaPrimitiveType) false else null
            })
    }

    private fun constant(type: String, names: Array<String>, value: Any?) {
        HookRuntime.named(type, *names, before = { if (blocked(extended = true)) result = value })
    }

    private fun hookOptionalFilters() {
        fun enabled() = ConfigData.flag("extended_filter_enabled") && blocked(extended = true)
        HookRuntime.named("android.view.accessibility.AccessibilityManager", "addAccessibilityRequestPreparer",
            "addAccessibilityInteractionConnection", before = {
                if (enabled()) result = when (method.returnType) {
                    Boolean::class.javaPrimitiveType -> true
                    Int::class.javaPrimitiveType -> 0
                    else -> null
                }
            })
        HookRuntime.named("android.content.BroadcastReceiver", "onReceive", before = {
            if (enabled() && args.filterIsInstance<Intent>().any {
                it.action == "android.settings.SETTINGS_CHANGED" &&
                    it.getStringExtra("settings_changed") in setOf(Settings.Secure.ACCESSIBILITY_ENABLED, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            }) resultNull()
        })
        HookRuntime.named("androidx.localbroadcastmanager.content.LocalBroadcastManager", "registerReceiver", before = {
            if (enabled() && args.filterIsInstance<IntentFilter>().any { filter ->
                (0 until filter.countActions()).any { filter.getAction(it).contains("accessibility", true) }
            }) resultNull()
        })
        fun relevant(type: Any?) = (type as? Class<*>)?.name?.let {
            it.contains("AccessibilityManager") || it.contains("AccessibilityService")
        } == true
        fun sensitive(name: String) = name.contains("isEnabled") || name.contains("getService") || name.contains("getEnabled")
        HookRuntime.named("java.lang.Class", "getDeclaredMethod", "getMethod", before = {
            if (enabled() && relevant(instance) && sensitive(arg(0) as? String ?: "")) {
                failure = NoSuchMethodException("Method not found")
            }
        }, propagateFailure = true)
        HookRuntime.named("java.lang.Class", "getDeclaredMethods", after = {
            if (enabled() && relevant(instance)) {
                @Suppress("UNCHECKED_CAST")
                result = (result as? Array<java.lang.reflect.Method>)?.filterNot { sensitive(it.name) }?.toTypedArray()
            }
        })
    }

    private fun hookNodes() {
        val node = "android.view.accessibility.AccessibilityNodeInfo"
        constant(node, arrayOf("isEnabled", "isChecked", "isFocused", "performAction"), false)
        constant(node, arrayOf("isVisibleToUser", "isClickable"), true)
        constant(node, arrayOf("getActionList"), emptyList<Any>())
        constant(node, arrayOf("getText", "getPackageName", "getStateDescription", "getChild", "getParent"), null)
        constant(node, arrayOf("getClassName"), "android.view.View")
        constant(node, arrayOf("getChildCount"), 0)
        constant(node, arrayOf("setStateDescription"), null)
        HookRuntime.named(node, "getBoundsInScreen", before = {
            if (blocked(extended = true)) {
                val rectangle = args.firstOrNull { it is Rect } as? Rect
                rectangle?.setEmpty()
                result = if (method.returnType == Rect::class.java) Rect() else null
            }
        })
        val event = "android.view.accessibility.AccessibilityEvent"
        constant(event, arrayOf("getContentChangeTypes", "getEventType"), 0)
        constant(event, arrayOf("getPackageName"), null)
        // Sanitize the real obtained event after creation to avoid recursive obtain() hooks.
        HookRuntime.named(event, "obtain", after = {
            if (blocked(extended = true)) (result as? android.view.accessibility.AccessibilityEvent)?.apply {
                eventType = 0; text.clear(); contentDescription = null; packageName = null
            }
        })
        constant("android.view.View", arrayOf("getContentDescription"), null)
        constant("android.view.accessibility.AccessibilityInteractionClient", arrayOf("getConnection"), null)
        constant("com.android.internal.accessibility.dialog.AccessibilityServiceTarget", arrayOf("getAccessibilityServiceInfo"), null)
        constant("com.android.internal.accessibility.dialog.InvisibleToggleAccessibilityServiceTarget", arrayOf("getAccessibilityServiceInfo"), null)
        if (ConfigData.flag("MothodMonitor_SW")) {
            HookRuntime.findClass("android.view.accessibility.AccessibilityManager")?.let {
                MethodMonitor.monitorClassMethods(it, it.name)
            }
        }
    }
}
