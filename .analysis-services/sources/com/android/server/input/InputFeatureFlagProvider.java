package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
public final class InputFeatureFlagProvider {
    private static final boolean KEYBOARD_BACKLIGHT_CONTROL_ENABLED = ((java.lang.Boolean) android.sysprop.InputProperties.enable_keyboard_backlight_control().orElse(true)).booleanValue();
    private static final boolean KEYBOARD_BACKLIGHT_ANIMATION_ENABLED = ((java.lang.Boolean) android.sysprop.InputProperties.enable_keyboard_backlight_animation().orElse(false)).booleanValue();
    private static final boolean KEYBOARD_BACKLIGHT_CUSTOM_LEVELS_ENABLED = ((java.lang.Boolean) android.sysprop.InputProperties.enable_keyboard_backlight_custom_levels().orElse(true)).booleanValue();
    private static final boolean AMBIENT_KEYBOARD_BACKLIGHT_CONTROL_ENABLED = ((java.lang.Boolean) android.sysprop.InputProperties.enable_ambient_keyboard_backlight_control().orElse(true)).booleanValue();
    private static java.util.Optional<java.lang.Boolean> sKeyboardBacklightControlOverride = java.util.Optional.empty();
    private static java.util.Optional<java.lang.Boolean> sKeyboardBacklightAnimationOverride = java.util.Optional.empty();
    private static java.util.Optional<java.lang.Boolean> sKeyboardBacklightCustomLevelsOverride = java.util.Optional.empty();
    private static java.util.Optional<java.lang.Boolean> sAmbientKeyboardBacklightControlOverride = java.util.Optional.empty();

    public static boolean isKeyboardBacklightControlEnabled() {
        return sKeyboardBacklightControlOverride.orElse(java.lang.Boolean.valueOf(KEYBOARD_BACKLIGHT_CONTROL_ENABLED)).booleanValue();
    }

    public static boolean isKeyboardBacklightAnimationEnabled() {
        return sKeyboardBacklightAnimationOverride.orElse(java.lang.Boolean.valueOf(KEYBOARD_BACKLIGHT_ANIMATION_ENABLED)).booleanValue();
    }

    public static boolean isKeyboardBacklightCustomLevelsEnabled() {
        return sKeyboardBacklightCustomLevelsOverride.orElse(java.lang.Boolean.valueOf(KEYBOARD_BACKLIGHT_CUSTOM_LEVELS_ENABLED)).booleanValue();
    }

    public static boolean isAmbientKeyboardBacklightControlEnabled() {
        return sAmbientKeyboardBacklightControlOverride.orElse(java.lang.Boolean.valueOf(AMBIENT_KEYBOARD_BACKLIGHT_CONTROL_ENABLED)).booleanValue();
    }

    public static void setKeyboardBacklightControlEnabled(boolean enabled) {
        sKeyboardBacklightControlOverride = java.util.Optional.of(java.lang.Boolean.valueOf(enabled));
    }

    public static void setKeyboardBacklightAnimationEnabled(boolean enabled) {
        sKeyboardBacklightAnimationOverride = java.util.Optional.of(java.lang.Boolean.valueOf(enabled));
    }

    public static void setKeyboardBacklightCustomLevelsEnabled(boolean enabled) {
        sKeyboardBacklightCustomLevelsOverride = java.util.Optional.of(java.lang.Boolean.valueOf(enabled));
    }

    public static void setAmbientKeyboardBacklightControlEnabled(boolean enabled) {
        sAmbientKeyboardBacklightControlOverride = java.util.Optional.of(java.lang.Boolean.valueOf(enabled));
    }

    public static void clearOverrides() {
        sKeyboardBacklightControlOverride = java.util.Optional.empty();
        sKeyboardBacklightAnimationOverride = java.util.Optional.empty();
        sKeyboardBacklightCustomLevelsOverride = java.util.Optional.empty();
        sAmbientKeyboardBacklightControlOverride = java.util.Optional.empty();
    }
}
