package io.github.fairyxh.ZhangSystemHook.hook

import android.os.Build
import io.github.fairyxh.ZhangSystemHook.core.BaseHooker
import io.github.fairyxh.ZhangSystemHook.core.HookRuntime
import io.github.fairyxh.ZhangSystemHook.data.ScreenshotConfig

object Android14ScreenshotBlocker : BaseHooker() {
    override fun onHook() {
        if (Build.VERSION.SDK_INT < 34) return
        HookRuntime.named("com.android.server.wm.ActivityRecord", "onScreenCaptured", "reportScreenCaptured", before = {
            if (ScreenshotConfig.enableAndroid14Blocker) {
                resultNull()
                HookRuntime.diagnostic("ScreenshotBlocker") { "BLOCK ${method.name}" }
            }
        }, filter = { it.returnType == Void.TYPE })
    }
}
