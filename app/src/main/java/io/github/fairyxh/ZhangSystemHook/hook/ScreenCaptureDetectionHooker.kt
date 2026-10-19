package io.github.fairyxh.ZhangSystemHook.hook

import io.github.fairyxh.ZhangSystemHook.core.BaseHooker

/** Retained source entry for callers migrating from the old screenshot hook. */
object ScreenCaptureDetectionHooker : BaseHooker() {
    override fun onHook() = Android14ScreenshotBlocker.onHook()
}
