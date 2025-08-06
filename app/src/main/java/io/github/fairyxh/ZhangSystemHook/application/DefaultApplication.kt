package io.github.fairyxh.ZhangSystemHook.application

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.utils.factory.locale
import io.github.fairyxh.ZhangSystemHook.generated.locale.AppLocale

class DefaultApplication : ModuleApplication() {

    companion object {
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        locale = AppLocale.attach(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        ConfigData.init(this)
    }
}
