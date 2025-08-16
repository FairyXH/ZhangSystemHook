@file:Suppress("SetTextI18n")

package io.github.fairyxh.ZhangSystemHook.ui.activity

import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.hook.xposed.prefs.data.PrefsData
import io.github.fairyxh.ZhangSystemHook.BuildConfig
import io.github.fairyxh.ZhangSystemHook.R
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

        //隐藏桌面图标开关
        binding.hideIconInLauncherSwitch.isChecked = isLauncherIconShowing.not()
        binding.hideIconInLauncherSwitch.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed) hideOrShowLauncherIcon(isChecked)
        }

        //显示通知开关
        val SHOW_NOTICE = PrefsData("show_notice", false)
        try {
            binding.showNoticeSwitch.isChecked = ConfigData.getBoolean(SHOW_NOTICE)
        } catch (e: Throwable){
            binding.showNoticeSwitch.isChecked =false
        }
        binding.showNoticeSwitch.setOnCheckedChangeListener {button, isChecked ->

            try {
                ConfigData.putBoolean(SHOW_NOTICE, isChecked)
                if (isChecked){
                SystemNotifier.sendUserMsg("将显示日志",button.context)} else{
                    SystemNotifier.sendUserMsg("不显示日志",button.context)
                }
            } catch (e: Throwable){
                SystemNotifier.sendUserMsg("设置通知开关失败",button.context)
            }
        }

        //显示通知开关
        val SYSTEM_APP_WHITELIST = PrefsData("system_app_whitelist", false)
        try {
            binding.systemWhitelistSwitch2.isChecked = ConfigData.getBoolean(SYSTEM_APP_WHITELIST)
        } catch (e: Throwable){
            binding.systemWhitelistSwitch2.isChecked =false
        }
        binding.systemWhitelistSwitch2.setOnCheckedChangeListener {button, isChecked ->

            try {
                ConfigData.putBoolean(SYSTEM_APP_WHITELIST, isChecked)
                if (isChecked){
                SystemNotifier.sendUserMsg("系统应用将使用白名单模式",button.context)}else{
                    SystemNotifier.sendUserMsg("关闭系统应用白名单模式",button.context)
                }
            } catch (e: Throwable){
                SystemNotifier.sendUserMsg("设置系统应用白名单开关失败",button.context)
            }
        }

        //测试通知按钮
        binding.buttonNoticeTest.setOnClickListener {
            SystemNotifier.sendUserMsg("ZhangSystemHook",it.context)
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
        if (YukiHookAPI.Status.isXposedModuleActive.not())  toast(locale.moduleNotActivated)
        else if(isModuleValid.not()) toast(locale.moduleNotFullyActivated)
        else callback()
    }

    override fun onResume() {
        super.onResume()
        /** 刷新模块状态 */
        refreshModuleStatus()
        /** 检查模块激活状态 */
        FrameworkTool.checkingActivated(context = this) { isValid ->
            isModuleValid = isValid
            refreshModuleStatus()
        }

        binding.serviceCheckResult.text = getSystemService(AccessibilityManager::class.java).let { am ->
            listOf("isEnabled: " + am.isEnabled) + am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK).map { it.id }.sorted()
        }.joinToString("\r\n")

        binding.settingsSecureCheckResult.text = (
            listOf("isEnabled: " + try { (Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED) != 0) } catch (e : Exception) { e.message })
            + try { Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES).split(':').sorted() } catch (e : Exception) { listOf(e.message ?: "ERROR") }
        ).joinToString("\r\n")
    }
}

