package io.github.fairyxh.ZhangSystemHook.core

import io.github.fairyxh.ZhangSystemHook.hook.ScreenshotMatcher
import org.junit.Assert.*
import org.junit.Test

class ScreenshotMatcherTest {
    @Test fun recognizesCaseInsensitiveNamesAndChinese() {
        assertTrue(ScreenshotMatcher.matches(listOf("Screenshot_2026.png")))
        assertTrue(ScreenshotMatcher.matches(listOf("截图_001.png")))
        assertTrue(ScreenshotMatcher.matches(listOf("截屏.png")))
    }
    @Test fun recognizesScreenshotFoldersWithUnrelatedFilenames() {
        assertTrue(ScreenshotMatcher.matches(listOf("image_001.png", "Pictures/Screenshots/")))
        assertTrue(ScreenshotMatcher.matches(listOf("DCIM/Screenshots/")))
    }
    @Test fun leavesOrdinaryPhotosAndGenericMediaUrisVisible() {
        assertFalse(ScreenshotMatcher.matches(listOf("IMG_001.jpg", "DCIM/Camera/")))
        assertFalse(ScreenshotMatcher.matches(listOf("content://media/external/images/media")))
        assertFalse(ScreenshotMatcher.matches(emptyList()))
    }
}
