package io.github.fairyxh.ZhangSystemHook.data

object ScreenshotConfig {
    val ENABLE_ANDROID14_BLOCKER = PreferenceKey("enable_android14_screenshot_blocker", true)
    val ENABLE_ENHANCED_BLOCKER = PreferenceKey("enable_enhanced_screenshot_blocker", false)
    var enableAndroid14Blocker: Boolean
        get() = ConfigData.getBoolean(ENABLE_ANDROID14_BLOCKER)
        set(value) = ConfigData.putBoolean(ENABLE_ANDROID14_BLOCKER, value)
    var enableEnhancedBlocker: Boolean
        get() = ConfigData.getBoolean(ENABLE_ENHANCED_BLOCKER)
        set(value) = ConfigData.putBoolean(ENABLE_ENHANCED_BLOCKER, value)
    val targetPackages: Set<String> get() = ConfigData.blockApps.data
}
