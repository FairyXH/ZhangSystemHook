@file:Suppress("SetTextI18n")

package io.github.fairyxh.ZhangSystemHook.ui.activity

import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import io.github.fairyxh.ZhangSystemHook.BuildConfig
import io.github.fairyxh.ZhangSystemHook.R
import io.github.fairyxh.ZhangSystemHook.application.DefaultApplication.Companion.context
import io.github.fairyxh.ZhangSystemHook.application.SystemNotifier
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.databinding.ActivityMainBinding
import io.github.fairyxh.ZhangSystemHook.ui.activity.base.BaseActivity
import io.github.fairyxh.ZhangSystemHook.utils.factory.hideOrShowLauncherIcon
import io.github.fairyxh.ZhangSystemHook.utils.factory.isLauncherIconShowing
import io.github.fairyxh.ZhangSystemHook.utils.factory.locale
import io.github.fairyxh.ZhangSystemHook.utils.factory.navigate
import io.github.fairyxh.ZhangSystemHook.utils.factory.toast
import io.github.fairyxh.ZhangSystemHook.utils.tool.FrameworkTool
import java.lang.reflect.Constructor
import java.lang.reflect.Method


class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        private val systemVersion = "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT}) ${Build.DISPLAY}"
        var isModuleValid = false
    }

    override fun onCreate() {
        binding.mainTextVersion.text = locale.moduleVersion(BuildConfig.VERSION_NAME)
        binding.mainTextSystemVersion.text = locale.systemVersion(systemVersion)
        binding.mgrAppsConfigsButton.setOnClickListener {
            whenActivated {
                navigate<AppsConfigActivity>()
            }
        }

        // 隐藏桌面图标开关
        binding.hideIconInLauncherSwitch.isChecked = isLauncherIconShowing.not()
        binding.hideIconInLauncherSwitch.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed) hideOrShowLauncherIcon(isChecked)
        }

        //方法监视器开关
        val MothodMonitor_SW = PrefsData("MothodMonitor_SW", false)
        try {
            binding.MethodMonitorSwitch.isChecked = ConfigData.getBoolean(MothodMonitor_SW)
        } catch (e: Throwable) {
            binding.MethodMonitorSwitch.isChecked = false
        }
        binding.MethodMonitorSwitch.setOnCheckedChangeListener { button, isChecked ->
            try {
                ConfigData.putBoolean(MothodMonitor_SW, isChecked)
                if (isChecked) {
                    SystemNotifier.sendUserMsg("启用方法监视器", button.context)
                } else {
                    SystemNotifier.sendUserMsg("关闭方法监视器", button.context)
                }
            } catch (e: Throwable) {
                SystemNotifier.sendUserMsg("设置方法监视器开关失败", button.context)
            }
        }


        // 显示通知开关
        val SHOW_NOTICE = PrefsData("show_notice", false)
        try {
            binding.showNoticeSwitch.isChecked = ConfigData.getBoolean(SHOW_NOTICE)
        } catch (e: Throwable) {
            binding.showNoticeSwitch.isChecked = false
        }
        binding.showNoticeSwitch.setOnCheckedChangeListener { button, isChecked ->
            try {
                ConfigData.putBoolean(SHOW_NOTICE, isChecked)
                if (isChecked) {
                    SystemNotifier.sendUserMsg("将显示日志", button.context)
                } else {
                    SystemNotifier.sendUserMsg("不显示日志", button.context)
                }
            } catch (e: Throwable) {
                SystemNotifier.sendUserMsg("设置通知开关失败", button.context)
            }
        }

        // 系统应用白名单模式开关
        val SYSTEM_APP_WHITELIST = PrefsData("system_app_whitelist", false)
        try {
            binding.systemWhitelistSwitch2.isChecked = ConfigData.getBoolean(SYSTEM_APP_WHITELIST)
        } catch (e: Throwable) {
            binding.systemWhitelistSwitch2.isChecked = false
        }
        binding.systemWhitelistSwitch2.setOnCheckedChangeListener { button, isChecked ->
            try {
                ConfigData.putBoolean(SYSTEM_APP_WHITELIST, isChecked)
                if (isChecked) {
                    SystemNotifier.sendUserMsg("系统应用将使用白名单模式", button.context)
                } else {
                    SystemNotifier.sendUserMsg("关闭系统应用白名单模式", button.context)
                }
            } catch (e: Throwable) {
                SystemNotifier.sendUserMsg("设置系统应用白名单开关失败", button.context)
            }
        }

        // 阻止系统组件式开关
        val IS_HOOK_ANDROID_KERNEL = PrefsData("block_system", false)
        try {
            binding.hookAndroidKernel.isChecked = ConfigData.getBoolean(IS_HOOK_ANDROID_KERNEL)
        } catch (e: Throwable) {
            binding.hookAndroidKernel.isChecked = false
        }
        binding.hookAndroidKernel.setOnCheckedChangeListener { button, isChecked ->
            try {
                ConfigData.putBoolean(IS_HOOK_ANDROID_KERNEL, isChecked)
                if (isChecked) {
                    SystemNotifier.sendUserMsg("允许阻止系统组件获取无障碍", button.context)
                } else {
                    SystemNotifier.sendUserMsg("系统组件默认白名单模式", button.context)
                }
            } catch (e: Throwable) {
                SystemNotifier.sendUserMsg("设置系统组件开关失败", button.context)
            }
        }

        // 测试通知按钮
        binding.buttonNoticeTest.setOnClickListener {
            SystemNotifier.sendUserMsg("ZhangSystemHook", it.context)
        }
    }

    private fun refreshModuleStatus() {
        binding.mainLinStatus.setBackgroundResource(
            when {
                YukiHookAPI.Status.isXposedModuleActive && isModuleValid.not() -> R.drawable.bg_yellow_round
                YukiHookAPI.Status.isXposedModuleActive -> R.drawable.bg_green_round
                else -> R.drawable.bg_dark_round
            }
        )
        binding.mainImgStatus.setImageResource(
            when {
                YukiHookAPI.Status.isXposedModuleActive -> R.mipmap.ic_success
                else -> R.mipmap.ic_warn
            }
        )
        binding.mainTextStatus.text = when {
            YukiHookAPI.Status.isXposedModuleActive && isModuleValid.not() -> locale.moduleNotFullyActivated
            YukiHookAPI.Status.isXposedModuleActive -> locale.moduleIsActivated
            else -> locale.moduleNotActivated
        }
        binding.mainTextApiWay.isVisible = YukiHookAPI.Status.isXposedModuleActive
        binding.mainTextApiWay.text = "Activated by ${YukiHookAPI.Status.Executor.name} API ${YukiHookAPI.Status.Executor.apiLevel}"
    }

    private inline fun whenActivated(callback: () -> Unit) {
        if (YukiHookAPI.Status.isXposedModuleActive.not()) toast(locale.moduleNotActivated)
        else if (isModuleValid.not()) toast(locale.moduleNotFullyActivated)
        callback()
    }
    @SuppressLint("PrivateApi")
    private fun updateAllHookCheckResults(): String {
        val accessibilityManager = getSystemService(AccessibilityManager::class.java)
        val contentResolver = contentResolver
        val results = mutableListOf<String>()

        // 1. AccessibilityManager.isEnabled()
        results.add(try {
            val isEnabled = accessibilityManager.isEnabled
            "1. AccessibilityManager.isEnabled(): ${if (isEnabled) "true" else "未检出"}"
        } catch (e: Exception) {
            "1. AccessibilityManager.isEnabled(): 未检出"
        })

        // 2. AccessibilityManager.getEnabledAccessibilityServiceList(int feedbackType)
        results.add(try {
            val services = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            if (services.isNotEmpty()) "2. getEnabledAccessibilityServiceList(): ${services.map { it.id }.sorted().joinToString(", ")}"
            else "2. getEnabledAccessibilityServiceList(): 未检出"
        } catch (e: Exception) {
            "2. getEnabledAccessibilityServiceList(): 未检出"
        })

        // 3. AccessibilityManager.getAccessibilityServiceList()
        results.add(try {
            val services = accessibilityManager.getAccessibilityServiceList()
            if (services.isNotEmpty()) "3. getAccessibilityServiceList(): ${services.map { it.packageName }.sorted().joinToString(", ")}"
            else "3. getAccessibilityServiceList(): 未检出"
        } catch (e: Exception) {
            "3. getAccessibilityServiceList(): 未检出"
        })

        // 4. AccessibilityManager.isTouchExplorationEnabled()
        results.add(try {
            val method = AccessibilityManager::class.java.getMethod("isTouchExplorationEnabled")
            val isEnabled = method.invoke(accessibilityManager) as Boolean
            "4. isTouchExplorationEnabled(): ${if (isEnabled) "true" else "禁用"}"
        } catch (e: Exception) {
            "4. isTouchExplorationEnabled(): 未检出"
        })


        // 5. Settings.Secure.getInt(ACCESSIBILITY_ENABLED)
        results.add(try {
            val enabled = Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED) != 0
            "5. Settings.ACCESSIBILITY_ENABLED: ${if (enabled) "true" else "未检出"}"
        } catch (e: Exception) {
            "5. Settings.ACCESSIBILITY_ENABLED: 未检出"
        })

        // 6. Settings.Secure.getString(ENABLED_ACCESSIBILITY_SERVICES)
        results.add(try {
            val services = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (!services.isNullOrBlank()) "6. Settings.ENABLED_SERVICES: ${services.split(':').sorted().joinToString(", ")}"
            else "6. Settings.ENABLED_SERVICES: 未检出"
        } catch (e: Exception) {
            "6. Settings.ENABLED_SERVICES: 未检出"
        })

        results.add("更多检测检测状态略")
        return results.joinToString("\n\n")
    }

    override fun onResume() {
        super.onResume()
        refreshModuleStatus()
        FrameworkTool.checkingActivated(context = this) { isValid ->
            isModuleValid = isValid
            refreshModuleStatus()
        }
        // 所有结果显示在serviceCheckResult
        binding.serviceCheckResult.text = updateAllHookCheckResults()
    }
}