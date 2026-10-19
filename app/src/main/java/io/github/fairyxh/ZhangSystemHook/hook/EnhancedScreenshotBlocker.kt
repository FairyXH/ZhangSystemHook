package io.github.fairyxh.ZhangSystemHook.hook

import android.database.Cursor
import android.net.Uri
import io.github.fairyxh.ZhangSystemHook.core.BaseHooker
import io.github.fairyxh.ZhangSystemHook.core.HookRuntime
import io.github.fairyxh.ZhangSystemHook.data.ScreenshotConfig

object EnhancedScreenshotBlocker : BaseHooker() {
    private fun enabled(): Boolean = ScreenshotConfig.enableEnhancedBlocker &&
        HookRuntime.packageName in ScreenshotConfig.targetPackages
    override fun onHook() {
        HookRuntime.named("android.content.ContentResolver", "registerContentObserver", before = {
            if (!enabled()) return@named
            val uri = args.firstOrNull { it is Uri } as? Uri ?: return@named
            if (uri.authority == "media" && ScreenshotMatcher.matches(listOf(uri.toString()))) resultNull()
        })
        HookRuntime.named("android.content.ContentResolver", "query", after = {
            if (!enabled()) return@named
            val uri = args.firstOrNull { it is Uri } as? Uri ?: return@named
            val cursor = result as? Cursor ?: return@named
            if (uri.authority == "media" && cursor !is ScreenshotFilteringCursor) result = ScreenshotFilteringCursor(cursor)
        })
    }
}

object ScreenshotMatcher {
    private val words = arrayOf("screenshot", "截屏", "截图", "pictures/screenshots", "dcim/screenshots")
    fun matches(values: Iterable<String>): Boolean = values.any { value ->
        words.any { word -> value.contains(word, ignoreCase = true) }
    }
}
