package io.github.fairyxh.ZhangSystemHook.hook

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.AttributionSource
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.database.MatrixCursor
import android.graphics.Rect
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import android.os.SystemClock
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.CharSequenceClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.highcapable.yukihookapi.hook.type.java.StringArrayClass
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import de.robv.android.xposed.XposedHelpers
import io.github.fairyxh.ZhangSystemHook.BuildConfig
import io.github.fairyxh.ZhangSystemHook.application.BootStateChecker.isBootCompletedAndUnlocked
import io.github.fairyxh.ZhangSystemHook.application.DefaultApplication.Companion.context
import io.github.fairyxh.ZhangSystemHook.application.SystemNotifier
import io.github.fairyxh.ZhangSystemHook.bean.AppFiltersType
import io.github.fairyxh.ZhangSystemHook.bean.AppInfoBean
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.utils.factory.appNameOf
import io.github.fairyxh.ZhangSystemHook.utils.factory.listOfPackages
import io.github.fairyxh.ZhangSystemHook.utils.tool.FrameworkTool
import java.lang.reflect.Method
import java.util.Random
import java.util.WeakHashMap
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import java.util.stream.Collectors

object AccessibilityHooker : YukiBaseHooker() {

    @Volatile
    private var refreshPolicyCallback: (() -> Unit)? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onHook() {
        SystemNotifier.sendUserMsg(msg = "AccessibilityHooker 开始工作")
        registerLifecycle()
    }
    private val nodeDescCache = Collections.synchronizedMap(
        WeakHashMap<AccessibilityNodeInfo, CharSequence?>()
    )
    @RequiresApi(Build.VERSION_CODES.S)
    private fun hookAccessibility() {
        //是否注册危险的方法;true注册;false不注册;仅在源代码内有此开关
        val Dangerous_method = false
        //危险方法可能导致Zygisk崩溃/Lsposed安全模式

        //源码强制开关:高性能模式;true高性能模式;false启用所有方法
        val high_proformance = true
        //可开关一些可能影响性能的方法,设置为true后用户高性能开关无效

        SystemNotifier.sendUserMsg(msg = "AccessibilityHooker 执行Hook")
        val SYSTEM_APP_WHITELIST = PrefsData("system_app_whitelist", false)
        val IS_HOOK_ANDROID_KERNEL = PrefsData("block_system", false)
        val ACCESSIBILITY_HOOK_ENABLE = PrefsData("accessibility_hook_enable", false)
        val HIGH_PERFORMANCE_MODE = PrefsData("high_performance_mode", false)
        val ALLOW_ADDCLIENT_METHOD = PrefsData("allow_client_method", false)


        val packageManager = systemContext.packageManager
        data class HookPolicy(
            val accessibilityHookEnabled: Boolean,
            val highPerformanceMode: Boolean,
            val allowAddClient: Boolean,
            val systemAppWhitelist: Boolean,
            val hookAndroidKernel: Boolean
        )

        fun readPolicy() = HookPolicy(
            accessibilityHookEnabled = ConfigData.getBoolean(ACCESSIBILITY_HOOK_ENABLE),
            highPerformanceMode = ConfigData.getBoolean(HIGH_PERFORMANCE_MODE),
            allowAddClient = ConfigData.getBoolean(ALLOW_ADDCLIENT_METHOD),
            systemAppWhitelist = ConfigData.getBoolean(SYSTEM_APP_WHITELIST),
            hookAndroidKernel = ConfigData.getBoolean(IS_HOOK_ANDROID_KERNEL)
        )

        val policy = AtomicReference(readPolicy())
        val uidToNameCache = ConcurrentHashMap<Int, String>(32)
        val systemAppCache = ConcurrentHashMap<String, Boolean>(64)
        val decisionCache = ConcurrentHashMap<String, Boolean>(64)
        val bootState = AtomicReference(false)
        val bootStateCheckedAt = AtomicLong(0L)

        fun refreshPolicy() {
            policy.set(readPolicy())
            decisionCache.clear()
            systemAppCache.clear()
        }
        refreshPolicyCallback = ::refreshPolicy

        val pkgName = {
            val ctx = context
            val now = SystemClock.elapsedRealtime()
            if (now - bootStateCheckedAt.get() >= 5_000L) {
                if (bootStateCheckedAt.compareAndSet(bootStateCheckedAt.get(), now)) {
                    bootState.set(isBootCompletedAndUnlocked(ctx))
                }
            }
            if (!bootState.get()) {
                "android"
            } else {
                Binder.getCallingUid().let { uid ->
                    uidToNameCache.computeIfAbsent(uid) {
                        packageManager.getNameForUid(uid) ?: "android"
                    }
                }
            }
        }

        fun isSystemApp(packageName: String): Boolean {
            return systemAppCache.computeIfAbsent(packageName) {
                try {
                    val appInfo = packageManager.getApplicationInfo(it, 0)
                    (appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) ||
                            (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0)
                } catch (_: Exception) {
                    true
                }
            }
        }

        fun can_read(
            now_package_name: String,
            high_performance_allow_run: Boolean? = false,
            allow_addclient_method: Boolean? = false
        ): Boolean {
            //high_performance_allow_run: 这个方法允许在高性能模式下Hook --> true
            val currentPolicy = policy.get()
            //无障碍防检测功能关闭，不执行Hook
            if (!currentPolicy.accessibilityHookEnabled) {
                return true
            }
            //如果这个方法不允许在高性能模式下Hook
            high_performance_allow_run?.let {
                if (!it) {
                    if (high_proformance) {
                        return true //源码强制高性能模式开关
                    }
                    if (currentPolicy.highPerformanceMode) {
                        return true //处于高性能模式
                    }
                }
            }
            //允许防检测读取共存(Allow addClient)
            if (allow_addclient_method == true) {
                if (currentPolicy.allowAddClient) {
                    return true
                }
            }

            //未完全开机,不执行任何Hook
            if (!bootState.get()) {
                return true
            }
            //系统自身不Hook
            if (now_package_name == "android") {
                return true
            }
            decisionCache[now_package_name]?.let { return it }
            val is_system_use_whitelist = currentPolicy.systemAppWhitelist
            val can_hook_kernel = currentPolicy.hookAndroidKernel

            val is_in_config_list = ConfigData.blockApps.contains(now_package_name)
            val is_system_app = isSystemApp(now_package_name)
            val is_module_self = (now_package_name == BuildConfig.APPLICATION_ID)
            var canread_flag: Boolean
            if (is_module_self) {
                canread_flag = false
            } else {
                canread_flag = if (is_system_use_whitelist) {
                    if (now_package_name.startsWith("android.") && is_system_app && !can_hook_kernel) {
                        true
                    } else if (is_in_config_list) {
                        true
                    } else {
                        false
                    }
                } else {
                    if (is_system_app) {
                        !is_in_config_list
                    } else {
                        is_in_config_list
                    }
                }
            }
            decisionCache.putIfAbsent(now_package_name, canread_flag)
            return canread_flag
        }

        "com.android.server.accessibility.AccessibilityManagerService".toClass().apply {

            method {
                name = "addClient" //原HOOK方法
                paramCount = 2
                param {
                    it[0].name == "android.view.accessibility.IAccessibilityManagerClient"
                    it[1] == IntType
                }
                returnType = LongType
            }.hook {
                before {
                    if (can_read(pkgName(), true, allow_addclient_method = true)) return@before
                    SystemNotifier.sendUserMsg(
                        msg = String.format(
                            "阻止 %s 读取无障碍 --> addClient",
                            pkgName()
                        )
                    )
                    result = 0L
                    intercept()
                }
            }

            method {
                name = "getInstalledAccessibilityServiceList" //原HOOK方法
                param(IntType)
                returnType = "android.content.pm.ParceledListSlice".toClass()
            }.hook {
                before {
                    if (can_read(pkgName(), true)) return@before
                    val callerPkg = pkgName()
                    SystemNotifier.sendUserMsg(
                        msg = String.format(
                            "阻止 %s 读取已安装无障碍服务 --> AccessibilityManagerService.getInstalledAccessibilityServiceList",
                            callerPkg
                        )
                    )
                    val emptyList = listOf<AccessibilityServiceInfo>()
                    val resultInstance = "android.content.pm.ParceledListSlice".toClass()
                        .getConstructor(List::class.java)
                        .newInstance(emptyList)
                    result = resultInstance
                    intercept()
                }
            }

            method {
                name = "getEnabledAccessibilityServiceList" //原HOOK方法
                param(IntType, IntType)
                returnType = ListClass
            }.hook {
                before {
                    if (can_read(pkgName(), true)) return@before
                    SystemNotifier.sendUserMsg(
                        msg = String.format(
                            "阻止 %s 读取无障碍 --> getEnabledAccessibilityServiceList",
                            pkgName()
                        )
                    )
                    result = emptyList<Any>()
                    intercept()
                }
            }
        }

        "android.content.ContentProvider\$Transport".toClass().apply {
            method {
                name = "call" //原HOOK方法
                param(
                    AttributionSource::class.java,
                    StringClass,
                    StringClass,
                    StringClass,
                    BundleClass
                )
                returnType = BundleClass
            }.hook {
                runCatching {
                    val secureKeys = mapOf<String, Bundle.() -> Unit>(
                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES to {
                            putString(Settings.NameValueTable.VALUE, "")
                            putInt("_generation_index", -1)
                        },
                        Settings.Secure.ACCESSIBILITY_ENABLED to {
                            putString(Settings.NameValueTable.VALUE, "0")
                            putInt("_generation_index", -1)
                        }
                    )
                    after {
                        if (args(1).string() != Settings.AUTHORITY && args(2).string() != "GET_secure") return@after
                        val method = secureKeys[args(3).string()] ?: return@after
                        val callerPackage = pkgName()
                        if (can_read(callerPackage, true)) return@after
                        method.also {
                            SystemNotifier.sendUserMsg(
                                msg = String.format(
                                    "阻止 %s 读取无障碍 --> ContentProvider.call",
                                    callerPackage
                                )
                            )
                            result<Bundle>()?.apply(it)
                        }
                    }
                }
            }
        }

        val high_performance_mode = ConfigData.getBoolean(HIGH_PERFORMANCE_MODE)
        //高性能开关关闭时才挂载Hook
        if (!high_performance_mode && !high_proformance) {

            "android.view.accessibility.AccessibilityManager".toClass().apply {
                method {
                    name = "isEnabled" //开关HOOK
                    returnType = BooleanType
                }.hook {
                    before {
                        if (can_read(pkgName(), true)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> isEnabled",
                                pkgName()
                            )
                        )
                        result = false
                        intercept()
                    }
                }

                if (Dangerous_method) {
                    try {
                        method {
                            name = "addAccessibilityRequestPreparer"
                            param("android.view.accessibility.AccessibilityRequestPreparer".toClass())
                        }.hook {
                            before {
                                if (can_read(pkgName(), false)) return@before
                                SystemNotifier.sendUserMsg(
                                    msg = String.format(
                                        "阻止 %s 检测 addAccessibilityRequestPreparer",
                                        pkgName()
                                    )
                                )
                                result = null
                                intercept()
                            }
                        }
                    } catch (e: Exception) {
                    }
                    try {
                        method {
                            name = "addAccessibilityInteractionConnection"
                            param(
                                "android.view.accessibility.AccessibilityInteractionConnection".toClass(),
                                IntType
                            )
                        }.hook {
                            before {
                                if (can_read(pkgName(), false)) return@before
                                SystemNotifier.sendUserMsg(
                                    msg = String.format(
                                        "阻止 %s 建立无障碍交互连接",
                                        pkgName()
                                    )
                                )
                                result = true
                                intercept()
                            }
                        }
                    } catch (e: Exception) {
                    }
                }

                // 拦截触摸探索模式状态变化通知
                method {
                    name = "notifyTouchExplorationStateChanged"
                    paramCount = 0
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 接收触摸状态变化通知 --> notifyTouchExplorationStateChanged",
                                pkgName()
                            )
                        )
                        result = null
                        intercept()

                    }
                }
                method {
                    name = "notifyAccessibilityStateChanged"
                    paramCount = 0
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        val callerPkg = pkgName()
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 接收无障碍状态变化通知 --> notifyAccessibilityStateChanged",
                                callerPkg
                            )
                        )
                        result = null
                        intercept()
                    }
                }

                method {
                    name = "isTouchExplorationEnabled"
                    returnType = BooleanType
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> isTouchExplorationEnabled",
                                pkgName()
                            )
                        )
                        result = false
                        intercept()
                    }
                }

                method {
                    name = "getInstalledAccessibilityServiceList"
                    paramCount = 0
                    returnType = ListClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        val callerPkg = pkgName()
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取已安装无障碍服务 --> AccessibilityManager.getInstalledAccessibilityServiceList",
                                callerPkg
                            )
                        )
                        result = emptyList<Any>()
                        intercept()
                    }
                }
                method {
                    name = "getAccessibilityServiceList"
                    paramCount = 0
                    returnType = ListClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getAccessibilityServiceList",
                                pkgName()
                            )
                        )
                        result = emptyList<Any>()
                        intercept()
                    }
                }

                method {
                    name = "getEnabledAccessibilityServiceList"
                    param(IntType)
                    returnType = ListClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getEnabledAccessibilityServiceList(feedbackType)",
                                pkgName()
                            )
                        )
                        result = emptyList<Any>()
                        intercept()
                    }
                }

                method {
                    name = "addAccessibilityStateChangeListener"
                    param("android.view.accessibility.AccessibilityManager\$AccessibilityStateChangeListener".toClass())
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 注册无障碍状态监听 --> addAccessibilityStateChangeListener",
                                pkgName()
                            )
                        )
                        result = false
                        intercept()
                    }
                }

                method {
                    name = "addAccessibilityStateChangeListener"
                    param(
                        "android.view.accessibility.AccessibilityManager\$AccessibilityStateChangeListener".toClass(),
                        "android.os.Handler".toClass()
                    )
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 注册无障碍状态监听 --> addAccessibilityStateChangeListener",
                                pkgName()
                            )
                        )
                        result = null
                        intercept()
                    }
                }

                method {
                    name = "addTouchExplorationStateChangeListener"
                    param("android.view.accessibility.AccessibilityManager\$TouchExplorationStateChangeListener".toClass())
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 注册无障碍状态监听 --> addTouchExplorationStateChangeListener",
                                pkgName()
                            )
                        )
                        result = false
                        intercept()
                    }
                }

                method {
                    name = "addTouchExplorationStateChangeListener"
                    param(
                        "android.view.accessibility.AccessibilityManager\$TouchExplorationStateChangeListener".toClass(),
                        "android.os.Handler".toClass()
                    )
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 注册无障碍状态监听 --> addTouchExplorationStateChangeListener",
                                pkgName()
                            )
                        )
                        result = null
                        intercept()
                    }
                }
            }

            "android.view.accessibility.AccessibilityNodeInfo".toClass().apply {
                method {
                    name = "isEnabled" //开关HOOK
                    returnType = BooleanType
                }.hook {
                    before {
                        if (can_read(pkgName(), true)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> AccessibilityNodeInfo.isEnabled",
                                pkgName
                            )
                        )
                        result = false
                        intercept()
                    }
                }

                method {
                    name = "isChecked"
                    returnType = BooleanType
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> AccessibilityNodeInfo.isChecked",
                                pkgName
                            )
                        )
                        result = false
                        intercept()
                    }
                }

                method {
                    name = "isVisibleToUser"
                    returnType = BooleanType
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> AccessibilityNodeInfo.isVisibleToUser",
                                pkgName
                            )
                        )
                        result = true
                        intercept()
                    }
                }

                method {
                    name = "getActionList"
                    returnType = ListClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getActionList",
                                pkgName()
                            )
                        )
                        result = emptyList<Any>()
                        intercept()
                    }
                }

                method {
                    name = "isClickable"
                    returnType = BooleanType
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> AccessibilityNodeInfo.isClickable",
                                pkgName
                            )
                        )
                        result = true
                        intercept()
                    }
                }

                method {
                    name = "getText"
                    returnType = CharSequenceClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format("阻止 %s 读取无障碍 --> getText", pkgName)
                        )
                        result = null
                        intercept()
                    }
                }

                method {
                    name = "getPackageName"
                    returnType = CharSequenceClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format("阻止 %s 读取无障碍 --> getPackageName", pkgName)
                        )
                        result = null
                        intercept()
                    }
                }

                method {
                    name = "setStateDescription"
                    param(CharSequenceClass)
                    returnType = Void.TYPE
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        val node = instance as AccessibilityNodeInfo
                        val desc = args[0] as CharSequence?
                        nodeDescCache[node] = desc
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> setStateDescription",
                                pkgName
                            )
                        )
                        result = null
                        intercept()
                    }
                }

                method {
                    name = "getStateDescription"
                    paramCount = 0
                    returnType = CharSequenceClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        val node = instance as AccessibilityNodeInfo
                        val cachedDesc = nodeDescCache[node]
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getStateDescription",
                                pkgName
                            )
                        )
                        result = null
                        intercept()
                    }
                }

                method {
                    name = "getChild"
                    param(IntType)
                    returnType = "android.view.accessibility.AccessibilityNodeInfo".toClass()
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getChild",
                                pkgName
                            )
                        )
                        result = null
                        intercept()
                    }
                }

                method {
                    name = "getParent"
                    returnType = "android.view.accessibility.AccessibilityNodeInfo".toClass()
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getParent",
                                pkgName
                            )
                        )
                        result = null
                        intercept()
                    }
                }

                method {
                    name = "performAction"
                    param(IntType, BundleClass)
                    returnType = BooleanType
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> performAction",
                                pkgName
                            )
                        )
                        result = false
                        intercept()
                    }
                }

                method {
                    name = "getClassName"
                    returnType = CharSequenceClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getClassName",
                                pkgName
                            )
                        )
                        result = "android.view.View" // 返回通用类名
                        intercept()
                    }
                }

                method {
                    name = "isFocused"
                    returnType = BooleanType
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> isFocused",
                                pkgName
                            )
                        )
                        result = false
                        intercept()
                    }
                }

                method {
                    name = "getBoundsInScreen"
                    param("android.graphics.Rect".toClass())
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getBoundsInScreen",
                                pkgName
                            )
                        )
                        result = Rect(0, 0, 0, 0)
                        intercept()
                    }
                }

                method {
                    name = "getBoundsInScreen"
                    returnType = "android.graphics.Rect".toClass()
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getBoundsInScreen",
                                pkgName
                            )
                        )
                        result = Rect(0, 0, 0, 0)
                        intercept()
                    }
                }

                method {
                    name = "getChildCount"
                    returnType = IntType
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getChildCount",
                                pkgName
                            )
                        )
                        result = 0
                        intercept()
                    }
                }

            }

            "com.android.internal.accessibility.dialog.AccessibilityServiceTarget".toClass().apply {
                method {
                    name = "getAccessibilityServiceInfo"
                    paramCount = 0 // 无参数
                    returnType =
                        AccessibilityServiceInfo::class.java // 返回 AccessibilityServiceInfo 类型
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before // 允许读取则跳过
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> AccessibilityServiceTarget.getAccessibilityServiceInfo",
                                pkgName()
                            )
                        )
                        result = null // 返回空，阻止获取服务信息
                        intercept()
                    }
                }
            }

            "com.android.internal.accessibility.dialog.InvisibleToggleAccessibilityServiceTarget".toClass()
                .apply {
                    method {
                        name = "getAccessibilityServiceInfo"
                        paramCount = 0
                        returnType = AccessibilityServiceInfo::class.java
                    }.hook {
                        before {
                            if (can_read(pkgName(), false)) return@before
                            SystemNotifier.sendUserMsg(
                                msg = String.format(
                                    "阻止 %s 读取无障碍 --> InvisibleToggleAccessibilityServiceTarget.getAccessibilityServiceInfo",
                                    pkgName()
                                )
                            )
                            result = null
                            intercept()
                        }
                    }
                }

            "android.view.accessibility.AccessibilityEvent".toClass().apply {
                method {
                    name = "getContentChangeTypes"
                    returnType = IntType
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getContentChangeTypes",
                                pkgName
                            )
                        )
                        result = 0
                        intercept()
                    }
                }

                method {
                    name = "obtain"
                    returnType = "android.view.accessibility.AccessibilityEvent".toClass()
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format("阻止 %s 读取无障碍 --> obtain", pkgName)
                        )
                        val ev = AccessibilityEvent.obtain() // 创建空事件
                        ev.setEventType(0)
                        ev.text.clear()
                        ev.contentDescription = null
                        ev.packageName = null
                        result = ev
                        intercept()
                    }
                }

                method {
                    name = "getEventType"
                    returnType = IntType
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format("阻止 %s 读取无障碍 --> getEventType", pkgName)
                        )
                        result = 0
                        intercept()
                    }
                }
                method {
                    name = "getPackageName"
                    returnType = CharSequenceClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format("阻止 %s 读取无障碍 --> getPackageName", pkgName)
                        )
                        result = null
                        intercept()
                    }
                }
            }

            "android.view.View".toClass().apply {
                method {
                    name = "getContentDescription"
                    returnType = CharSequenceClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getContentDescription",
                                pkgName
                            )
                        )
                        result = null
                        intercept()
                    }
                }
            }

            "android.view.accessibility.AccessibilityInteractionClient".toClass().apply {
                method {
                    name = "getConnection"
                    param(IntType)
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg("阻止 ${pkgName()} 调用 getConnection")
                        result = null
                        intercept()
                    }
                }
            }

            "android.provider.Settings\$Secure".toClass().apply {
                method {
                    name = "getInt"
                    param("android.content.ContentResolver".toClass(), StringClass, IntType)
                }.hook {
                    after {
                        if (can_read(pkgName(), false)) return@after
                        val key = args[1] as? String ?: return@after
                        if (!can_read(pkgName(), false)) {
                            if (key.contains("accessibility")) {
                                result = 0
                                SystemNotifier.sendUserMsg("阻止 ${pkgName()} 调用 getInt")
                                intercept()
                            }
                        }
                    }
                }
                method {
                    name = "getString"
                    param("android.content.ContentResolver".toClass(), StringClass)
                }.hook {
                    after {
                        if (can_read(pkgName(), false)) return@after
                        try {
                            val key = args.getOrNull(1) as? String ?: return@after
                            if (can_read(pkgName(), false)) return@after

                            when (key) {
                                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES -> {
                                    result = ""
                                    SystemNotifier.sendUserMsg(
                                        msg = String.format(
                                            "阻止 %s 读取 %s -> return empty",
                                            pkgName(),
                                            key
                                        )
                                    )
                                }

                                Settings.Secure.ACCESSIBILITY_ENABLED -> {
                                    result = "0"
                                    SystemNotifier.sendUserMsg(
                                        msg = String.format(
                                            "阻止 %s 读取 %s -> return 0",
                                            pkgName(),
                                            key
                                        )
                                    )
                                }

                                else -> {
                                }
                            }
                        } catch (e: Throwable) {
                        }
                    }
                }
            }

            "android.content.ContentResolver".toClass().apply {
                method {
                    name = "query"
                    param(
                        Uri::class.java,
                        StringArrayClass,
                        StringClass,
                        StringArrayClass,
                        StringClass
                    )
                }.hook {
                    after {
                        if (can_read(pkgName(), false)) return@after
                        val uri = args.getOrNull(0) as? Uri ?: return@after
                        val uriString = uri.toString()
                        val authority = uri.authority
                        val path = uri.path

                        val isAccessibilityQuery = uriString.contains(
                            "enabled_accessibility_services",
                            ignoreCase = true
                        ) ||
                                uriString.contains("accessibility_enabled", ignoreCase = true) ||
                                uriString.contains("accessibility", ignoreCase = true) ||
                                (authority == "settings" && path?.contains("accessibility") == true) ||
                                (authority == "com.android.settings.provider.settings" && path?.contains(
                                    "secure"
                                ) == true)

                        if (isAccessibilityQuery) {
                            if (can_read(pkgName(), false)) return@after
                            SystemNotifier.sendUserMsg(
                                msg = String.format(
                                    "阻止 %s 读取无障碍 --> query",
                                    pkgName()
                                )
                            )
                            result = object : MatrixCursor(arrayOf("value")) {
                                override fun getCount(): Int = 0
                                override fun getString(column: Int): String? =
                                    if (column == 0) "0" else null
                            }
                        }
                    }
                }

                method {
                    name = "query"
                    param(
                        Uri::class.java,
                        StringArrayClass,
                        StringClass,
                        StringArrayClass,
                        StringClass,
                        "android.os.CancellationSignal".toClass()
                    )
                }.hook {
                    after {
                        if (can_read(pkgName(), false)) return@after
                        val uri = args.getOrNull(0) as? Uri ?: return@after
                        val isAccessibilityQuery = uri.toString()
                            .contains("enabled_accessibility_services", ignoreCase = true) ||
                                uri.toString().contains("accessibility_enabled", ignoreCase = true)
                        if (isAccessibilityQuery) {
                            if (can_read(pkgName(), false)) return@after
                            SystemNotifier.sendUserMsg(
                                msg = String.format(
                                    "阻止 %s 读取无障碍 --> query(CancellationSignal)",
                                    pkgName()
                                )
                            )
                            result = object : MatrixCursor(arrayOf("value")) {
                                override fun getCount(): Int = 0
                                override fun getString(column: Int): String? = "0"
                            }
                        }
                    }
                }

                method {
                    name = "query"
                    param(
                        Uri::class.java,
                        StringArrayClass,
                        Bundle::class.java,
                        "android.os.CancellationSignal".toClass()
                    )
                }.hook {
                    after {
                        if (can_read(pkgName(), false)) return@after
                        val uri = args.getOrNull(0) as? Uri ?: return@after
                        val isAccessibilityQuery = uri.authority == "settings" &&
                                uri.path?.contains("accessibility") == true
                        if (isAccessibilityQuery) {
                            if (can_read(pkgName(), false)) return@after
                            SystemNotifier.sendUserMsg(
                                msg = String.format(
                                    "阻止 %s 读取无障碍 --> query(Bundle)",
                                    pkgName()
                                )
                            )
                            result = object : MatrixCursor(arrayOf("value")) {
                                override fun getCount(): Int = 0
                                override fun getString(column: Int): String? = "0"
                            }
                        }
                    }
                }
            }

            "android.app.ApplicationPackageManager".toClass().apply { // 改为具体实现类
                method {
                    name = "queryIntentServices"
                    param("android.content.Intent".toClass(), IntType)
                    returnType = ListClass
                }.hook {
                    after {
                        if (can_read(pkgName(), false)) return@after
                        val intent = args[0] as? Intent ?: return@after
                        if (intent.action == "android.accessibilityservice.AccessibilityService") {
                            if (can_read(pkgName(), false)) return@after
                            SystemNotifier.sendUserMsg(
                                msg = String.format(
                                    "阻止 %s 查询无障碍服务列表 --> queryIntentServices",
                                    pkgName()
                                )
                            )
                            result = emptyList<Any>()
                        }
                    }
                }
                method {
                    name = "getPackageInfo"
                    param(StringClass, IntType)
                    returnType = "android.content.pm.PackageInfo".toClass()
                }.hook {
                    after {
                        if (can_read(pkgName(), false)) return@after
                        val packageName = args[0] as? String ?: return@after
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 查询无障碍服务列表 --> getPackageInfo",
                                pkgName()
                            )
                        )
                        result = sanitizedPackageInfo(packageName)
                    }
                }
            }

            "android.app.ActivityManager".toClass().apply {
                method {
                    name = "getRunningServices"
                    param(IntType)
                    returnType = ListClass
                }.hook {
                    after {
                        if (can_read(pkgName(), false)) return@after
                        val callerPkg = pkgName()
                        // 过滤所有包含"accessibility"的服务
                        val originalList = result as List<*>
                        val filteredList = originalList.filter { serviceInfo ->
                            val serviceName =
                                XposedHelpers.getObjectField(serviceInfo, "service").toString()
                            !serviceName.contains("accessibility", ignoreCase = true)
                        }
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 查询运行中服务（过滤无障碍服务）",
                                callerPkg
                            )
                        )
                        result = filteredList
                    }
                }
            }

            "android.os.ServiceManager".toClass().apply {
                method {
                    name = "getService"
                    param(StringClass)
                    returnType = IBinder::class.java
                }.hook {
                    after {
                        if (can_read(pkgName(), false)) return@after
                        val serviceName = args[0] as? String ?: return@after
                        if (serviceName == "accessibility") { // 无障碍服务的系统服务名
                            if (can_read(pkgName(), false)) return@after
                            SystemNotifier.sendUserMsg(
                                msg = String.format(
                                    "阻止 %s 获取无障碍系统服务代理 --> ServiceManager.getService",
                                    pkgName()
                                )
                            )
                            // 返回空Binder或代理Binder（避免直接调用）
                            result = object : Binder() {
                                override fun onTransact(
                                    code: Int,
                                    data: Parcel,
                                    reply: Parcel?,
                                    flags: Int
                                ): Boolean {
                                    return false // 拒绝所有跨进程调用
                                }
                            }
                        }
                    }
                }
            }

            "android.content.ContextWrapper".toClass().apply {
                method {
                    name = "registerReceiver"
                    param(
                        "android.content.BroadcastReceiver".toClass(),
                        "android.content.IntentFilter".toClass()
                    )
                    returnType = "android.content.Intent".toClass()
                }.hook {
                    before {
                        if (isSystemApp(pkgName())) return@before
                        val intentFilter = args[1] as? IntentFilter ?: return@before
                        // 正确引用 ACTION_SETTINGS_CHANGED（必须带 Intent 类名）
                        val sensitiveActions = listOf(
                            "android.accessibilityservice.AccessibilityService.ACTION_ACCESSIBILITY_SERVICE_CHANGED",
                            Settings.ACTION_ACCESSIBILITY_SETTINGS,
                            "android.settings.SETTINGS_CHANGED",
                            "android.settings.ACCESSIBILITY_ENABLED_CHANGED",
                            "android.intent.action.ACCESSIBILITY_SETTINGS_CHANGED"
                        )
                        val hasSensitiveAction = sensitiveActions.any { intentFilter.hasAction(it) }
                        if (hasSensitiveAction && !can_read(pkgName(), false)) {
                            SystemNotifier.sendUserMsg(
                                msg = String.format(
                                    "阻止 %s 注册无障碍相关广播",
                                    pkgName()
                                )
                            )
                            args[1] = IntentFilter("invalid.action") // 替换为无效广播
                        }
                    }
                }
            }

            "android.view.accessibility.IAccessibilityManager\$Stub\$Proxy".toClass().apply {
                method {
                    name = "getEnabledAccessibilityServiceList"
                    param(IntType, IntType)
                    returnType = ListClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getEnabledAccessibilityServiceList",
                                pkgName()
                            )
                        )
                        result = emptyList<Any>()
                        intercept()
                    }
                }

                method {
                    name = "addClient"
                    paramCount = 2
                    param {
                        it[0].name == "android.view.accessibility.IAccessibilityManagerClient"
                        it[1] == IntType
                    }
                    returnType = LongType
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> addClient",
                                pkgName()
                            )
                        )
                        result = 0L
                        intercept()
                    }
                }

                method {
                    name = "getInstalledAccessibilityServiceList"
                    param(IntType)
                    returnType =
                        "android.content.pm.ParceledListSlice".toClass() // 返回ParceledListSlice
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        val callerPkg = pkgName()
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取已安装无障碍服务 --> AccessibilityManagerService.getInstalledAccessibilityServiceList",
                                callerPkg
                            )
                        )
                        val emptyList = listOf<AccessibilityServiceInfo>()
                        val resultInstance = "android.content.pm.ParceledListSlice".toClass()
                            .getConstructor(List::class.java)
                            .newInstance(emptyList)
                        result = resultInstance
                        intercept()
                    }
                }
            }

            "android.accessibilityservice.AccessibilityServiceInfo".toClass().apply {
                method {
                    name = "loadDescription"
                    param("android.content.pm.PackageManager".toClass())
                    returnType = StringClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format("阻止 %s 读取无障碍 --> loadDescription", pkgName)
                        )
                        result = ""
                        intercept()
                    }
                }

                method {
                    name = "getResolveInfo"
                    returnType = "android.content.pm.ResolveInfo".toClass()
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        SystemNotifier.sendUserMsg(
                            msg = String.format(
                                "阻止 %s 读取无障碍 --> getResolveInfo",
                                pkgName
                            )
                        )
                        result = null
                        intercept()
                    }
                }

                method {
                    name = "toString"
                    returnType = StringClass
                }.hook {
                    before {
                        if (can_read(pkgName(), false)) return@before
                        val random = Random()
                        val eventTypes = listOf(
                            AccessibilityEvent.TYPE_VIEW_CLICKED,
                            AccessibilityEvent.TYPE_VIEW_FOCUSED,
                            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
                        ).shuffled(random).take(random.nextInt(3) + 1)
                            .fold(0) { acc, type -> acc or type }
                        val feedbackTypes = listOf(
                            AccessibilityServiceInfo.FEEDBACK_SPOKEN,
                            AccessibilityServiceInfo.FEEDBACK_HAPTIC,
                            AccessibilityServiceInfo.FEEDBACK_AUDIBLE,
                            AccessibilityServiceInfo.FEEDBACK_VISUAL
                        ).shuffled(random).take(random.nextInt(2) + 1)
                            .fold(0) { acc, type -> acc or type }
                        val notificationTimeout = random.nextInt(3000).toLong()
                        val flags =
                            if (random.nextBoolean()) 0 else AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
                        val capabilities = listOf(
                            AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT,
                            AccessibilityServiceInfo.CAPABILITY_CAN_PERFORM_GESTURES,
                            AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION
                        ).shuffled(random).take(random.nextInt(2) + 1)
                            .fold(0) { acc, cap -> acc or cap }
                        val id = random.nextInt(1000)
                        val isAccessibilityTool = random.nextBoolean()
                        val packageNames = if (random.nextBoolean()) null else arrayOf(
                            "com.example.${
                                random.nextInt(100)
                            }"
                        )
                        val randomStr = buildString {
                            append("eventTypes: $eventTypes, ")
                            append("packageNames: ${packageNames?.contentToString() ?: "null"}, ")
                            append("feedbackTypes: $feedbackTypes, ")
                            append("notificationTimeout: $notificationTimeout, ")
                            append("nonInteractiveUiTimeout: ${random.nextInt(1000).toLong()}, ")
                            append("interactiveUiTimeout: ${random.nextInt(1000).toLong()}, ")
                            append("flags: $flags, ")
                            append("id: $id, ")
                            append("resolveInfo: null, ")
                            append("settingsActivityName: null, ")
                            append("tileServiceName: null, ")
                            append("summary: null, ")
                            append("isAccessibilityTool: $isAccessibilityTool, ")
                            append("capabilities: $capabilities")
                        }
                        result = randomStr
                        SystemNotifier.sendUserMsg(
                            msg = String.format("阻止 %s 读取无障碍 --> toString", pkgName)
                        )
                        intercept()
                    }
                }

            }

            "android.content.Context".toClass().apply {
                method {
                    name = "getSystemService"
                    param(StringClass)
                    returnType = Any::class.java
                }.hook {
                    before {
                        runCatching {
                            try {
                                if (can_read(pkgName(), false)) return@before
                                val serviceName = args[0] as? String ?: return@before
                                // 拦截无障碍服务的获取
                                if (("accessibility" in serviceName.lowercase()) || serviceName == Context.ACCESSIBILITY_SERVICE) {
                                    SystemNotifier.sendUserMsg(
                                        msg = String.format(
                                            "阻止 %s 获取无障碍服务实例 --> getSystemService",
                                            pkgName()
                                        )
                                    )
                                    // 返回空实例或代理对象（避免空指针）
                                    result = null
                                    intercept()
                                }
                            } catch (e: Exception) {
                            }
                        }
                    }
                }

                method {
                    name = "getSystemService"
                    param(Class::class.java)
                    returnType = Any::class.java
                }.hook {
                    before {
                        runCatching {
                            try {
                                if (can_read(pkgName(), false)) return@before
                                val serviceClass = args[0] as? Class<*> ?: return@before
                                if ("accessibility" in serviceClass.name.lowercase()) {
                                    SystemNotifier.sendUserMsg(
                                        msg = String.format(
                                            "阻止 %s 获取无障碍服务实例 --> getSystemService(Class)",
                                            pkgName()
                                        )
                                    )
                                    result = null
                                    intercept()
                                }
                            } catch (e: Exception) {
                            }
                        }
                    }
                }
            }

            //可能引发崩溃的Hook
            if (Dangerous_method) {
                try {
                    "android.content.BroadcastReceiver".toClass().apply {
                        try {
                            method {
                                name = "onReceive"
                                param(
                                    "android.content.Context".toClass(),
                                    "android.content.Intent".toClass()
                                )
                            }.hook {
                                before {
                                    if (can_read(pkgName(), false)) return@before
                                    val intent = args[1] as? Intent ?: return@before
                                    val action = intent.action ?: return@before
                                    if (action == "android.settings.SETTINGS_CHANGED") {
                                        val changedKey = intent.getStringExtra("settings_changed")
                                        if (changedKey in listOf(
                                                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                                                Settings.Secure.ACCESSIBILITY_ENABLED
                                            )
                                        ) {
                                            SystemNotifier.sendUserMsg(
                                                msg = String.format(
                                                    "阻止 %s 处理无障碍设置变化广播 --> onReceive",
                                                    pkgName()
                                                )
                                            )
                                            result = null
                                            intercept()
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            YLog.info(msg = "方法注册失败:onReceive")
                        }
                    }
                } catch (e: Exception) {
                }

                try {
                    "androidx.localbroadcastmanager.content.LocalBroadcastManager".toClass().apply {
                        try {
                            method {
                                name = "registerReceiver"
                                param(
                                    "android.content.BroadcastReceiver".toClass(),
                                    "android.content.IntentFilter".toClass()
                                )
                            }.hook {
                                before {
                                    if (can_read(pkgName(), false)) return@before
                                    val intentFilter = args[1] as? IntentFilter ?: return@before
                                    val sensitiveActions = listOf(
                                        "android.accessibilityservice.AccessibilityService.ACTION_ACCESSIBILITY_SERVICE_CHANGED",
                                        Settings.ACTION_ACCESSIBILITY_SETTINGS,
                                        "custom.accessibility.state.changed",
                                        "android.settings.ACCESSIBILITY_ENABLED_CHANGED",
                                        "android.intent.action.ACCESSIBILITY_SETTINGS_CHANGED"
                                    )
                                    if (sensitiveActions.any { intentFilter.hasAction(it) }) {
                                        SystemNotifier.sendUserMsg(
                                            msg = String.format(
                                                "阻止 %s 注册本地无障碍广播 --> LocalBroadcastManager",
                                                pkgName()
                                            )
                                        )
                                        result = null
                                        intercept()

                                    }
                                }
                            }
                        } catch (e: Exception) {
                            YLog.info(msg = "方法注册失败:LocalBroadcastManager")
                        }
                    }
                } catch (e: Exception) {
                }


                try {
                    "java.lang.Class".toClass().apply {

                        try {
                            method {
                                name = "getDeclaredMethod"
                                param(StringClass, arrayOf<Class<*>>().javaClass)
                            }.hook {
                                before {
                                    if (can_read(pkgName(), false)) return@before
                                    val className = this.instance<Class<*>>().name
                                    val methodName = args[0] as? String ?: return@before
                                    if ((className.contains("AccessibilityManager") || className.contains(
                                            "AccessibilityService"
                                        )) &&
                                        (methodName.contains("isEnabled") || methodName.contains("getService") || methodName.contains(
                                            "getEnabled"
                                        ))
                                    ) {
                                        SystemNotifier.sendUserMsg("${pkgName()} 反射调用了无障碍方法 --> $className.$methodName")
                                        throw NoSuchMethodException("Method not found") // 只抛异常即可
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            YLog.info(msg = "方法注册失败:getDeclaredMethod")
                        }


                        try {
                            method {
                                name = "getMethod"
                                param(StringClass, arrayOf<Class<*>>().javaClass)
                            }.hook {
                                before {
                                    if (can_read(pkgName(), false)) return@before
                                    val className = this.instance<Class<*>>().name
                                    val methodName = args[0] as? String ?: return@before
                                    if ((className.contains("AccessibilityManager") || className.contains(
                                            "AccessibilityService"
                                        )) &&
                                        (methodName.contains("isEnabled") || methodName.contains("getService") || methodName.contains(
                                            "getEnabled"
                                        ))
                                    ) {
                                        SystemNotifier.sendUserMsg("${pkgName()} 反射调用了无障碍方法 --> $className.$methodName")
                                        throw NoSuchMethodException("Method not found") // 只抛异常即可
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            YLog.info(msg = "方法注册失败:getMethod")
                        }

                        try {
                            method {
                                name = "getDeclaredMethods"
                                paramCount = 0
                            }.hook {
                                after {
                                    if (can_read(pkgName(), false)) return@after
                                    val className = this.instance<Class<*>>().name
                                    if (!(className.contains("AccessibilityManager") || className.contains(
                                            "AccessibilityService"
                                        ))
                                    ) return@after
                                    val originalMethods = (result as? Array<*>) ?: return@after
                                    val filteredMethods = originalMethods.filter { method ->
                                        val methodName = (method as? Method)?.name ?: ""
                                        !(methodName.contains("isEnabled") || methodName.contains("getService") || methodName.contains(
                                            "getEnabled"
                                        ))
                                    }.toTypedArray()
                                    SystemNotifier.sendUserMsg("${pkgName()} 反射调用了无障碍方法 --> getDeclaredMethods")
                                    result = filteredMethods
                                    intercept()
                                }
                            }
                        } catch (e: Exception) {
                            YLog.info(msg = "方法注册失败:getDeclaredMethods")
                        }

                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun sanitizedPackageInfo(packageName: String): PackageInfo {
        return PackageInfo().apply {
            this.packageName = packageName
            versionName = "" // 清空版本信息
            versionCode = 0
            services = emptyArray() // 清空服务列表（避免泄露无障碍服务信息）
            applicationInfo = ApplicationInfo().apply {
                this.packageName = packageName
                // 清空可能包含敏感配置的字段
                metaData = null
                permission = ""
            }
            // 其他字段按需清空或设置默认值
            firstInstallTime = 0
            lastUpdateTime = 0
            permissions = emptyArray()
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
                SystemClock.sleep(500)
                ConfigData.refresh()
                refreshPolicyCallback?.invoke()
                if (prefs.isPreferencesAvailable.not()) YLog.warn("Cannot refreshing app errors config data, preferences is not available")
            }
            onPushAppListData { filters ->
                appContext?.let { context ->
                    var info = context.listOfPackages().stream().filter { it.packageName != "android" }
                    if (filters.name.isNotBlank()) {
                        info = info.filter {
                            it.packageName.contains(filters.name) || context.appNameOf(it.packageName).contains(filters.name)
                        }
                    }
                    fun PackageInfo.isSystemApp() = (applicationInfo?.flags ?: 0 and ApplicationInfo.FLAG_SYSTEM) != 0
                    when (filters.type) {
                        AppFiltersType.USER -> info.filter { it.isSystemApp().not() }
                        AppFiltersType.SYSTEM -> info.filter { it.isSystemApp() }
                        AppFiltersType.ALL -> info
                    }.sorted(
                        Comparator.comparing(PackageInfo::lastUpdateTime).reversed()
                    ).map {
                        AppInfoBean(name = context.appNameOf(it.packageName), packageName = it.packageName)
                    }.collect(Collectors.toList())
                } ?: listOf()
            }
        }
    }
}