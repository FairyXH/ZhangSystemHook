package io.github.fairyxh.ZhangSystemHook.hook

import android.database.Cursor
import android.database.CursorWrapper
import android.net.Uri
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import io.github.fairyxh.ZhangSystemHook.data.ScreenshotConfig


/** Hides screenshot rows from MediaStore observers and query results in target apps. */
object EnhancedScreenshotBlocker : YukiBaseHooker() {
    private const val TAG = "ScreenshotBlocker"
    private val screenshotNames = listOf("screenshot", "截屏", "截图")
    private val screenshotPaths = listOf("pictures/screenshots", "dcim/screenshots")

    override fun onHook() {
        if (!ScreenshotConfig.enableEnhancedBlocker) {
            HookLog.i(TAG, "[Enhanced] disabled")
            return
        }
        HookLog.i(TAG, "[Enhanced] installing ContentResolver hooks")
        hookContentObservers()
        hookQueries()
    }

    private fun hookContentObservers() {
        runCatching {
            val clazz = "android.content.ContentResolver".toClass()
            val candidates = clazz.declaredMethods.filter { it.name == "registerContentObserver" }
            if (candidates.isEmpty()) {
                HookLog.w(TAG, "[Enhanced] candidate not found: ContentResolver.registerContentObserver")
                return
            }
            candidates.forEach { method ->
                method.isAccessible = true
                method.hook {
                    before {
                        val uri = args.firstOrNull { it is Uri } as? Uri
                        if (uri?.isScreenshotUri() == true) {
                            resultNull()
                            HookLog.i(TAG, "[Enhanced] BLOCK observer uri=$uri args=${args.contentToString()}")
                        } else {
                            HookLog.i(TAG, "[Enhanced] ALLOW observer uri=$uri")
                        }
                    }
                }
                HookLog.i(TAG, "[Enhanced] hooked registerContentObserver${method.parameterTypes.contentToString()}")
            }
        }.onFailure { HookLog.e(TAG, "[Enhanced] failed to hook registerContentObserver", it) }
    }

    private fun hookQueries() {
        runCatching {
            val clazz = "android.content.ContentResolver".toClass()
            val candidates = clazz.declaredMethods.filter { it.name == "query" }
            if (candidates.isEmpty()) {
                HookLog.w(TAG, "[Enhanced] candidate not found: ContentResolver.query")
                return
            }
            candidates.forEach { method ->
                method.isAccessible = true
                method.hook {
                    after {
                        val uri = args.firstOrNull { it is Uri } as? Uri
                        val cursor = result as? Cursor
                        if (uri?.isMediaStoreUri() == true && cursor != null && cursor !is ScreenshotFilteringCursor) {
                            result = ScreenshotFilteringCursor(cursor)
                            HookLog.i(TAG, "[Enhanced] FILTER query uri=$uri count=${(result as Cursor).count}")
                        } else {
                            HookLog.i(TAG, "[Enhanced] ALLOW query uri=$uri cursor=${cursor != null}")
                        }
                    }
                }
                HookLog.i(TAG, "[Enhanced] hooked query${method.parameterTypes.contentToString()}")
            }
        }.onFailure { HookLog.e(TAG, "[Enhanced] failed to hook query", it) }
    }

    private fun Uri.isMediaStoreUri(): Boolean = authority == "media"

    private fun Uri.isScreenshotUri(): Boolean {
        if (!isMediaStoreUri()) return false
        val value = toString().lowercase()
        return screenshotNames.any(value::contains) || screenshotPaths.any(value::contains)
    }

    private class ScreenshotFilteringCursor(cursor: Cursor) : CursorWrapper(cursor) {
        private val visiblePositions = buildList {
            val originalPosition = cursor.position
            try {
                if (cursor.moveToPosition(-1)) Unit
                while (cursor.moveToNext()) {
                    if (!cursor.isScreenshotRow()) add(cursor.position)
                }
            } finally {
                cursor.moveToPosition(originalPosition)
            }
        }

        init {
            val filtered = cursor.count - visiblePositions.size
            if (filtered > 0) HookLog.i(TAG, "[Enhanced] Filtered screenshot item count=$filtered")
        }

        override fun getCount(): Int = visiblePositions.size

        override fun moveToPosition(position: Int): Boolean = when {
            position < 0 -> super.moveToPosition(-1)
            position >= visiblePositions.size -> super.moveToPosition(wrappedCursor.count)
            else -> super.moveToPosition(visiblePositions[position])
        }

        private fun Cursor.isScreenshotRow(): Boolean {
            val values = sequenceOf("_display_name", "title", "relative_path", "_data")
                .map { getColumnIndex(it) }
                .filter { it >= 0 && !isNull(it) }
                .map { getString(it).orEmpty().lowercase() }
                .toList()
            return values.any { value ->
                screenshotNames.any(value::contains) || screenshotPaths.any(value::contains)
            }
        }
    }
}