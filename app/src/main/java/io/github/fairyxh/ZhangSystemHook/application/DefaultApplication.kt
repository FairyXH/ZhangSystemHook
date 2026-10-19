package io.github.fairyxh.ZhangSystemHook.application

import android.app.Application
import io.github.fairyxh.ZhangSystemHook.data.ConfigData
import io.github.fairyxh.ZhangSystemHook.utils.tool.FrameworkTool

class DefaultApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ConfigData.init(this)
        FrameworkTool.connect()
    }
}
