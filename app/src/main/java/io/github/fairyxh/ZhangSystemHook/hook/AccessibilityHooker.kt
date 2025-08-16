package io.github.fairyxh.ZhangSystemHook.hook

import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.AttributionSource
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import io.github.fairyxh.ZhangSystemHook.BuildConfig
import io.github.fairyxh.ZhangSystemHook.application.SystemNotifier
import io.github.fairyxh.ZhangSystemHook.bean.AppFiltersType
import io.github.fairyxh.ZhangSystemHook.bean.AppInfoBean
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.utils.factory.appNameOf
import io.github.fairyxh.ZhangSystemHook.utils.factory.listOfPackages
import io.github.fairyxh.ZhangSystemHook.utils.tool.FrameworkTool
import java.util.WeakHashMap
import java.util.stream.Collectors

object AccessibilityHooker : YukiBaseHooker() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onHook() {
        SystemNotifier.sendUserMsg(msg = "AccessibilityHooker 开始工作")
        registerLifecycle()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun hookAccessibility() {
        SystemNotifier.sendUserMsg(msg = "AccessibilityHooker 执行Hook")
        val SYSTEM_APP_WHITELIST = PrefsData("system_app_whitelist", false)
        val packageManager = systemContext.packageManager
        val uidToNameCache = WeakHashMap<Int, String>(1024)
        val pkgName = {
            Binder.getCallingUid().let { uid ->
                uidToNameCache.computeIfAbsent(uid) { _ ->
                    packageManager.getNameForUid(uid)
                }
            }
        }
        fun isSystemApp(packageName: String): Boolean {
            return try {
                val appInfo = systemContext.packageManager.getApplicationInfo(packageName, 0)
                (appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) ||
                        (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0)
            } catch (e: Exception) {
                true
            }
        }

        fun can_block(now_package_name: String): Boolean {
            var is_system_use_whitelist = ConfigData.getBoolean(SYSTEM_APP_WHITELIST)
            var is_in_white_list = ConfigData.blockApps.contains(now_package_name)
            var is_system_app = isSystemApp(now_package_name)
            var is_module_self = (now_package_name == BuildConfig.APPLICATION_ID)
            var block_flag = false //true允许读取；false阻止读取。
            //系统应用是否使用白名单模式
            if (is_module_self) {
                block_flag = false //模块自身直接阻止
            } else {
                if (is_system_use_whitelist) { //开启系统应用白名单模式的情况
                    if (now_package_name.startsWith("android.") && is_system_app) {
                        block_flag = true //以android.开头的系统关键组件不阻止
                    } else if ((is_in_white_list)) {
                        block_flag = true //在白名单内允许读取
                    } else {
                        block_flag = false //其余情况阻止读取
                    }
                } else { //关闭系统应用白名单的情况
                    if (is_system_app) {
                        block_flag = true //系统应用全允许读取
                    } else {
                        if (is_in_white_list) {
                            block_flag = true //第三方应用在白名单允许读取
                        } else {
                            block_flag = false //其余阻止读取
                        }
                    }
                }
            }
            return block_flag
        }
        "com.android.server.accessibility.AccessibilityManagerService".toClass().apply {
            method {
                name = "addClient"
                paramCount = 2
                param {
                    it[0].name == "android.view.accessibility.IAccessibilityManagerClient"
                    it[1] == IntType /* userId */
                }
                returnType = LongType
            }.hook {
                before {

                    if (can_block(pkgName())) return@before
                    SystemNotifier.sendUserMsg(msg = String.format("阻止 %s 读取无障碍 --> addClient",pkgName()))
                    result = 0L
                }
            }
            method {
                name = "getEnabledAccessibilityServiceList"
                param(IntType/* feedbackType */, IntType/* userId */)
                returnType = ListClass
            }.hook {
                before {
                    if (can_block(pkgName())) return@before
                    SystemNotifier.sendUserMsg(msg = String.format("阻止 %s 读取无障碍 --> getEnabledAccessibilityServiceList",pkgName()))
                    result = listOf<AccessibilityServiceInfo>()
                }
            }
        }
        "android.content.ContentProvider\$Transport".toClass().apply {
            method {
                name = "call"
                param(AttributionSource::class.java, StringClass/* 1.authority */, StringClass/* 2.method */, StringClass/* 3.arg */, BundleClass/* 4.extras */)
                returnType = BundleClass
            }.hook {
                val secureKeys = mapOf<String, Bundle.() -> Unit>(
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES to {
                        putString(Settings.NameValueTable.VALUE, "")
                        putInt("_generation_index" /* Settings.CALL_METHOD_GENERATION_INDEX_KEY */, -1)
                    },
                    Settings.Secure.ACCESSIBILITY_ENABLED to {
                        putString(Settings.NameValueTable.VALUE, "0")
                        putInt("_generation_index" /* Settings.CALL_METHOD_GENERATION_INDEX_KEY */, -1)
                    }
                )
                after {
                    if(args(1).string() != Settings.AUTHORITY && args(2).string() != "GET_secure" /* Settings.CALL_METHOD_GET_SECURE */) return@after
                    secureKeys[args(3).string()]?.also { method ->
                        if (can_block(pkgName())) return@after
                        SystemNotifier.sendUserMsg(msg = String.format("阻止 %s 读取无障碍 --> call",pkgName()))
                        result<Bundle>()?.apply(method)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun registerLifecycle() {
        onAppLifecycle {
            onCreate {
                hookAccessibility()
            }
        }
        FrameworkTool.Host.with(instance = this) {
            onRefreshFrameworkPrefsData {
                /** 必要的延迟防止 Sp 存储不刷新 */
                SystemClock.sleep(500)
                /** 刷新存储类 */
                ConfigData.refresh()
                if (prefs.isPreferencesAvailable.not()) YLog.warn("Cannot refreshing app errors config data, preferences is not available")
            }
            onPushAppListData { filters ->
                appContext?.let { context ->
                    var info = context.listOfPackages().stream().filter { it.packageName.let { e -> e != "android" } }
                    if (filters.name.isNotBlank()) {
                        info = info.filter {
                            it.packageName.contains(filters.name) || context.appNameOf(it.packageName).contains(filters.name)
                        }
                    }
                    fun PackageInfo.isSystemApp() = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                    when (filters.type) {
                        AppFiltersType.USER -> info.filter { it.isSystemApp().not() }
                        AppFiltersType.SYSTEM -> info.filter { it.isSystemApp() }
                        AppFiltersType.ALL -> info
                    }.sorted (
                        Comparator.comparing(PackageInfo::lastUpdateTime).reversed()
                    ).map {
                        AppInfoBean(name = context.appNameOf(it.packageName), packageName = it.packageName)
                    }.collect(Collectors.toList())
                } ?: listOf()
            }
        }
    }
}