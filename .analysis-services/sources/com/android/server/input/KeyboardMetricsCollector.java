package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
public final class KeyboardMetricsCollector {
    public static final java.lang.String DEFAULT_LANGUAGE_TAG = "None";
    static final java.lang.String DEFAULT_LAYOUT_NAME = "Default";
    private static final java.lang.String TAG = "KeyboardMetricCollector";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    public enum KeyboardLogEvent {
        UNSPECIFIED(0, "INVALID_KEYBOARD_EVENT"),
        HOME(1, "HOME"),
        RECENT_APPS(2, "RECENT_APPS"),
        BACK(3, "BACK"),
        APP_SWITCH(4, "APP_SWITCH"),
        LAUNCH_ASSISTANT(5, "LAUNCH_ASSISTANT"),
        LAUNCH_VOICE_ASSISTANT(6, "LAUNCH_VOICE_ASSISTANT"),
        LAUNCH_SYSTEM_SETTINGS(7, "LAUNCH_SYSTEM_SETTINGS"),
        TOGGLE_NOTIFICATION_PANEL(8, "TOGGLE_NOTIFICATION_PANEL"),
        TOGGLE_TASKBAR(9, "TOGGLE_TASKBAR"),
        TAKE_SCREENSHOT(10, "TAKE_SCREENSHOT"),
        OPEN_SHORTCUT_HELPER(11, "OPEN_SHORTCUT_HELPER"),
        BRIGHTNESS_UP(12, "BRIGHTNESS_UP"),
        BRIGHTNESS_DOWN(13, "BRIGHTNESS_DOWN"),
        KEYBOARD_BACKLIGHT_UP(14, "KEYBOARD_BACKLIGHT_UP"),
        KEYBOARD_BACKLIGHT_DOWN(15, "KEYBOARD_BACKLIGHT_DOWN"),
        KEYBOARD_BACKLIGHT_TOGGLE(16, "KEYBOARD_BACKLIGHT_TOGGLE"),
        VOLUME_UP(17, "VOLUME_UP"),
        VOLUME_DOWN(18, "VOLUME_DOWN"),
        VOLUME_MUTE(19, "VOLUME_MUTE"),
        ALL_APPS(20, "ALL_APPS"),
        LAUNCH_SEARCH(21, "LAUNCH_SEARCH"),
        LANGUAGE_SWITCH(22, "LANGUAGE_SWITCH"),
        ACCESSIBILITY_ALL_APPS(23, "ACCESSIBILITY_ALL_APPS"),
        TOGGLE_CAPS_LOCK(24, "TOGGLE_CAPS_LOCK"),
        SYSTEM_MUTE(25, "SYSTEM_MUTE"),
        SPLIT_SCREEN_NAVIGATION(26, "SPLIT_SCREEN_NAVIGATION"),
        CHANGE_SPLITSCREEN_FOCUS(50, "CHANGE_SPLITSCREEN_FOCUS"),
        TRIGGER_BUG_REPORT(27, "TRIGGER_BUG_REPORT"),
        LOCK_SCREEN(28, "LOCK_SCREEN"),
        OPEN_NOTES(29, "OPEN_NOTES"),
        TOGGLE_POWER(30, "TOGGLE_POWER"),
        SYSTEM_NAVIGATION(31, "SYSTEM_NAVIGATION"),
        SLEEP(32, "SLEEP"),
        WAKEUP(33, "WAKEUP"),
        MEDIA_KEY(34, "MEDIA_KEY"),
        LAUNCH_DEFAULT_BROWSER(35, "LAUNCH_DEFAULT_BROWSER"),
        LAUNCH_DEFAULT_EMAIL(36, "LAUNCH_DEFAULT_EMAIL"),
        LAUNCH_DEFAULT_CONTACTS(37, "LAUNCH_DEFAULT_CONTACTS"),
        LAUNCH_DEFAULT_CALENDAR(38, "LAUNCH_DEFAULT_CALENDAR"),
        LAUNCH_DEFAULT_CALCULATOR(39, "LAUNCH_DEFAULT_CALCULATOR"),
        LAUNCH_DEFAULT_MUSIC(40, "LAUNCH_DEFAULT_MUSIC"),
        LAUNCH_DEFAULT_MAPS(41, "LAUNCH_DEFAULT_MAPS"),
        LAUNCH_DEFAULT_MESSAGING(42, "LAUNCH_DEFAULT_MESSAGING"),
        LAUNCH_DEFAULT_GALLERY(43, "LAUNCH_DEFAULT_GALLERY"),
        LAUNCH_DEFAULT_FILES(44, "LAUNCH_DEFAULT_FILES"),
        LAUNCH_DEFAULT_WEATHER(45, "LAUNCH_DEFAULT_WEATHER"),
        LAUNCH_DEFAULT_FITNESS(46, "LAUNCH_DEFAULT_FITNESS"),
        LAUNCH_APPLICATION_BY_PACKAGE_NAME(47, "LAUNCH_APPLICATION_BY_PACKAGE_NAME"),
        DESKTOP_MODE(48, "DESKTOP_MODE"),
        MULTI_WINDOW_NAVIGATION(49, "MULTIWINDOW_NAVIGATION");

        private static final android.util.SparseArray<com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent> VALUE_TO_ENUM_MAP = new android.util.SparseArray<>();
        private final java.lang.String mName;
        private final int mValue;

        static {
            for (com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent type : values()) {
                VALUE_TO_ENUM_MAP.put(type.mValue, type);
            }
        }

        KeyboardLogEvent(int enumValue, java.lang.String enumName) {
            this.mValue = enumValue;
            this.mName = enumName;
        }

        public int getIntValue() {
            return this.mValue;
        }

        public static com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent from(int value) {
            return VALUE_TO_ENUM_MAP.get(value);
        }

        public static com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent getVolumeEvent(int keycode) {
            switch (keycode) {
                case 24:
                    return VOLUME_UP;
                case 25:
                    return VOLUME_DOWN;
                case 164:
                    return VOLUME_MUTE;
                default:
                    return null;
            }
        }

        public static com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent getBrightnessEvent(int keycode) {
            switch (keycode) {
                case 220:
                    return BRIGHTNESS_DOWN;
                case 221:
                    return BRIGHTNESS_UP;
                default:
                    return null;
            }
        }

        public static com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent getLogEventFromIntent(android.content.Intent intent) {
            java.util.Set<java.lang.String> selectorCategories;
            android.content.Intent selectorIntent = intent.getSelector();
            if (selectorIntent != null && (selectorCategories = selectorIntent.getCategories()) != null && !selectorCategories.isEmpty()) {
                for (java.lang.String intentCategory : selectorCategories) {
                    com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent logEvent = getEventFromSelectorCategory(intentCategory);
                    if (logEvent != null) {
                        return logEvent;
                    }
                }
            }
            java.lang.String role = intent.getStringExtra(com.android.server.policy.ModifierShortcutManager.EXTRA_ROLE);
            if (!android.text.TextUtils.isEmpty(role)) {
                return getLogEventFromRole(role);
            }
            java.util.Set<java.lang.String> intentCategories = intent.getCategories();
            if (intentCategories == null || intentCategories.isEmpty() || !intentCategories.contains("android.intent.category.LAUNCHER") || intent.getComponent() == null) {
                return null;
            }
            return LAUNCH_APPLICATION_BY_PACKAGE_NAME;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0087  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private static com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent getEventFromSelectorCategory(java.lang.String r1) {
            /*
                Method dump skipped, instruction units count: 256
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.getEventFromSelectorCategory(java.lang.String):com.android.server.input.KeyboardMetricsCollector$KeyboardLogEvent");
        }

        private static com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent getLogEventFromRole(java.lang.String role) {
            if ("android.app.role.BROWSER".equals(role)) {
                return LAUNCH_DEFAULT_BROWSER;
            }
            if ("android.app.role.SMS".equals(role)) {
                return LAUNCH_DEFAULT_MESSAGING;
            }
            android.util.Log.w(com.android.server.input.KeyboardMetricsCollector.TAG, "Keyboard shortcut to launch " + role + " not supported for logging");
            return null;
        }
    }

    public static void logKeyboardSystemsEventReportedAtom(android.view.InputDevice inputDevice, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent keyboardSystemEvent, int modifierState, int... keyCodes) {
        if (inputDevice == null || inputDevice.isVirtual() || !inputDevice.isFullKeyboard()) {
            return;
        }
        if (keyboardSystemEvent == null) {
            android.util.Slog.w(TAG, "Invalid keyboard event logging, keycode = " + java.util.Arrays.toString(keyCodes) + ", modifier state = " + modifierState);
            return;
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.KEYBOARD_SYSTEMS_EVENT_REPORTED, inputDevice.getVendorId(), inputDevice.getProductId(), keyboardSystemEvent.getIntValue(), keyCodes, modifierState, inputDevice.getDeviceBus());
        if (DEBUG) {
            android.util.Slog.d(TAG, "Logging Keyboard system event: " + keyboardSystemEvent.mName);
        }
    }

    public static void logKeyboardConfiguredAtom(com.android.server.input.KeyboardMetricsCollector.KeyboardConfigurationEvent event) {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
        for (com.android.server.input.KeyboardMetricsCollector.LayoutConfiguration layoutConfiguration : event.getLayoutConfigurations()) {
            addKeyboardLayoutConfigurationToProto(proto, layoutConfiguration);
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.KEYBOARD_CONFIGURED, event.isFirstConfiguration(), event.getVendorId(), event.getProductId(), proto.getBytes(), event.getDeviceBus());
        if (DEBUG) {
            android.util.Slog.d(TAG, "Logging Keyboard configuration event: " + event);
        }
    }

    private static void addKeyboardLayoutConfigurationToProto(android.util.proto.ProtoOutputStream proto, com.android.server.input.KeyboardMetricsCollector.LayoutConfiguration layoutConfiguration) {
        long keyboardLayoutConfigToken = proto.start(2246267895809L);
        proto.write(1138166333442L, layoutConfiguration.keyboardLanguageTag);
        proto.write(1120986464257L, layoutConfiguration.keyboardLayoutType);
        proto.write(1138166333443L, layoutConfiguration.keyboardLayoutName);
        proto.write(1120986464260L, layoutConfiguration.layoutSelectionCriteria);
        proto.write(1138166333446L, layoutConfiguration.imeLanguageTag);
        proto.write(1120986464261L, layoutConfiguration.imeLayoutType);
        proto.end(keyboardLayoutConfigToken);
    }

    public static class KeyboardConfigurationEvent {
        private final android.view.InputDevice mInputDevice;
        private final boolean mIsFirstConfiguration;
        private final java.util.List<com.android.server.input.KeyboardMetricsCollector.LayoutConfiguration> mLayoutConfigurations;

        private KeyboardConfigurationEvent(android.view.InputDevice inputDevice, boolean isFirstConfiguration, java.util.List<com.android.server.input.KeyboardMetricsCollector.LayoutConfiguration> layoutConfigurations) {
            this.mInputDevice = inputDevice;
            this.mIsFirstConfiguration = isFirstConfiguration;
            this.mLayoutConfigurations = layoutConfigurations;
        }

        public int getVendorId() {
            return this.mInputDevice.getVendorId();
        }

        public int getProductId() {
            return this.mInputDevice.getProductId();
        }

        public int getDeviceBus() {
            return this.mInputDevice.getDeviceBus();
        }

        public boolean isFirstConfiguration() {
            return this.mIsFirstConfiguration;
        }

        public java.util.List<com.android.server.input.KeyboardMetricsCollector.LayoutConfiguration> getLayoutConfigurations() {
            return this.mLayoutConfigurations;
        }

        public java.lang.String toString() {
            return "InputDevice = {VendorId = " + java.lang.Integer.toHexString(getVendorId()) + ", ProductId = " + java.lang.Integer.toHexString(getProductId()) + ", Device Bus = " + java.lang.Integer.toHexString(getDeviceBus()) + "}, isFirstConfiguration = " + this.mIsFirstConfiguration + ", LayoutConfigurations = " + this.mLayoutConfigurations;
        }

        public static class Builder {
            private final android.view.InputDevice mInputDevice;
            private boolean mIsFirstConfiguration;
            private final java.util.List<android.view.inputmethod.InputMethodSubtype> mImeSubtypeList = new java.util.ArrayList();
            private final java.util.List<java.lang.String> mSelectedLayoutList = new java.util.ArrayList();
            private final java.util.List<java.lang.Integer> mLayoutSelectionCriteriaList = new java.util.ArrayList();

            public Builder(android.view.InputDevice inputDevice) {
                java.util.Objects.requireNonNull(inputDevice, "InputDevice provided should not be null");
                this.mInputDevice = inputDevice;
            }

            public com.android.server.input.KeyboardMetricsCollector.KeyboardConfigurationEvent.Builder setIsFirstTimeConfiguration(boolean isFirstTimeConfiguration) {
                this.mIsFirstConfiguration = isFirstTimeConfiguration;
                return this;
            }

            public com.android.server.input.KeyboardMetricsCollector.KeyboardConfigurationEvent.Builder addLayoutSelection(android.view.inputmethod.InputMethodSubtype imeSubtype, java.lang.String selectedLayout, int layoutSelectionCriteria) {
                java.util.Objects.requireNonNull(imeSubtype, "IME subtype provided should not be null");
                if (!com.android.server.input.KeyboardMetricsCollector.isValidSelectionCriteria(layoutSelectionCriteria)) {
                    throw new java.lang.IllegalStateException("Invalid layout selection criteria");
                }
                this.mImeSubtypeList.add(imeSubtype);
                this.mSelectedLayoutList.add(selectedLayout);
                this.mLayoutSelectionCriteriaList.add(java.lang.Integer.valueOf(layoutSelectionCriteria));
                return this;
            }

            public com.android.server.input.KeyboardMetricsCollector.KeyboardConfigurationEvent build() {
                java.lang.String str;
                int size = this.mImeSubtypeList.size();
                if (size == 0) {
                    throw new java.lang.IllegalStateException("Should have at least one configuration");
                }
                java.util.List<com.android.server.input.KeyboardMetricsCollector.LayoutConfiguration> configurationList = new java.util.ArrayList<>();
                int i = 0;
                while (i < size) {
                    int layoutSelectionCriteria = this.mLayoutSelectionCriteriaList.get(i).intValue();
                    android.view.inputmethod.InputMethodSubtype imeSubtype = this.mImeSubtypeList.get(i);
                    java.lang.String keyboardLanguageTag = this.mInputDevice.getKeyboardLanguageTag();
                    java.lang.String keyboardLanguageTag2 = android.text.TextUtils.isEmpty(keyboardLanguageTag) ? com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG : keyboardLanguageTag;
                    int keyboardLayoutType = android.hardware.input.KeyboardLayout.LayoutType.getLayoutTypeEnumValue(this.mInputDevice.getKeyboardLayoutType());
                    android.icu.util.ULocale pkLocale = imeSubtype.getPhysicalKeyboardHintLanguageTag();
                    java.lang.String imeLanguageTag = pkLocale != null ? pkLocale.toLanguageTag() : imeSubtype.getCanonicalizedLanguageTag();
                    java.lang.String imeLanguageTag2 = android.text.TextUtils.isEmpty(imeLanguageTag) ? com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG : imeLanguageTag;
                    int imeLayoutType = android.hardware.input.KeyboardLayout.LayoutType.getLayoutTypeEnumValue(imeSubtype.getPhysicalKeyboardHintLayoutType());
                    if (this.mSelectedLayoutList.get(i) == null) {
                        str = com.android.server.input.KeyboardMetricsCollector.DEFAULT_LAYOUT_NAME;
                    } else {
                        str = this.mSelectedLayoutList.get(i);
                    }
                    java.lang.String keyboardLayoutName = str;
                    configurationList.add(new com.android.server.input.KeyboardMetricsCollector.LayoutConfiguration(keyboardLayoutType, keyboardLanguageTag2, keyboardLayoutName, layoutSelectionCriteria, imeLayoutType, imeLanguageTag2));
                    i++;
                    size = size;
                }
                return new com.android.server.input.KeyboardMetricsCollector.KeyboardConfigurationEvent(this.mInputDevice, this.mIsFirstConfiguration, configurationList);
            }
        }
    }

    static class LayoutConfiguration {
        public final java.lang.String imeLanguageTag;
        public final int imeLayoutType;
        public final java.lang.String keyboardLanguageTag;
        public final java.lang.String keyboardLayoutName;
        public final int keyboardLayoutType;
        public final int layoutSelectionCriteria;

        private LayoutConfiguration(int keyboardLayoutType, java.lang.String keyboardLanguageTag, java.lang.String keyboardLayoutName, int layoutSelectionCriteria, int imeLayoutType, java.lang.String imeLanguageTag) {
            this.keyboardLayoutType = keyboardLayoutType;
            this.keyboardLanguageTag = keyboardLanguageTag;
            this.keyboardLayoutName = keyboardLayoutName;
            this.layoutSelectionCriteria = layoutSelectionCriteria;
            this.imeLayoutType = imeLayoutType;
            this.imeLanguageTag = imeLanguageTag;
        }

        public java.lang.String toString() {
            return "{keyboardLanguageTag = " + this.keyboardLanguageTag + " keyboardLayoutType = " + android.hardware.input.KeyboardLayout.LayoutType.getLayoutNameFromValue(this.keyboardLayoutType) + " keyboardLayoutName = " + this.keyboardLayoutName + " layoutSelectionCriteria = " + android.hardware.input.KeyboardLayoutSelectionResult.layoutSelectionCriteriaToString(this.layoutSelectionCriteria) + " imeLanguageTag = " + this.imeLanguageTag + " imeLayoutType = " + android.hardware.input.KeyboardLayout.LayoutType.getLayoutNameFromValue(this.imeLayoutType) + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidSelectionCriteria(int layoutSelectionCriteria) {
        return layoutSelectionCriteria == 1 || layoutSelectionCriteria == 2 || layoutSelectionCriteria == 3 || layoutSelectionCriteria == 4;
    }
}
