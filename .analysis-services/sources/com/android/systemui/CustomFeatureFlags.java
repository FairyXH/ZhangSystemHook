package com.android.systemui;

/* JADX INFO: loaded from: classes3.dex */
public class CustomFeatureFlags implements com.android.systemui.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.systemui.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.systemui.Flags.FLAG_ACTIVITY_TRANSITION_USE_LARGEST_WINDOW, com.android.systemui.Flags.FLAG_AMBIENT_TOUCH_MONITOR_LISTEN_TO_DISPLAY_CHANGES, com.android.systemui.Flags.FLAG_APP_CLIPS_BACKLINKS, com.android.systemui.Flags.FLAG_BIND_KEYGUARD_MEDIA_VISIBILITY, com.android.systemui.Flags.FLAG_BP_TALKBACK, com.android.systemui.Flags.FLAG_BRIGHTNESS_SLIDER_FOCUS_STATE, com.android.systemui.Flags.FLAG_CENTRALIZED_STATUS_BAR_HEIGHT_FIX, com.android.systemui.Flags.FLAG_CLIPBOARD_NONINTERACTIVE_ON_LOCKSCREEN, com.android.systemui.Flags.FLAG_CLOCK_REACTIVE_VARIANTS, com.android.systemui.Flags.FLAG_COMMUNAL_BOUNCER_DO_NOT_MODIFY_PLUGIN_OPEN, com.android.systemui.Flags.FLAG_COMMUNAL_HUB, com.android.systemui.Flags.FLAG_COMPOSE_BOUNCER, com.android.systemui.Flags.FLAG_COMPOSE_LOCKSCREEN, com.android.systemui.Flags.FLAG_CONFINE_NOTIFICATION_TOUCH_TO_VIEW_WIDTH, com.android.systemui.Flags.FLAG_CONSTRAINT_BP, com.android.systemui.Flags.FLAG_CONTEXTUAL_TIPS_ASSISTANT_DISMISS_FIX, com.android.systemui.Flags.FLAG_COROUTINE_TRACING, com.android.systemui.Flags.FLAG_DEDICATED_NOTIF_INFLATION_THREAD, com.android.systemui.Flags.FLAG_DELAY_SHOW_MAGNIFICATION_BUTTON, com.android.systemui.Flags.FLAG_DELAYED_WAKELOCK_RELEASE_ON_BACKGROUND_THREAD, com.android.systemui.Flags.FLAG_DEVICE_ENTRY_UDFPS_REFACTOR, com.android.systemui.Flags.FLAG_DISABLE_CONTEXTUAL_TIPS_FREQUENCY_CHECK, com.android.systemui.Flags.FLAG_DISABLE_CONTEXTUAL_TIPS_IOS_SWITCHER_CHECK, com.android.systemui.Flags.FLAG_DOZEUI_SCHEDULING_ALARMS_BACKGROUND_EXECUTION, com.android.systemui.Flags.FLAG_DREAM_INPUT_SESSION_PILFER_ONCE, com.android.systemui.Flags.FLAG_DREAM_OVERLAY_BOUNCER_SWIPE_DIRECTION_FILTERING, com.android.systemui.Flags.FLAG_DUAL_SHADE, com.android.systemui.Flags.FLAG_EDGE_BACK_GESTURE_HANDLER_THREAD, com.android.systemui.Flags.FLAG_EDGEBACK_GESTURE_HANDLER_GET_RUNNING_TASKS_BACKGROUND, com.android.systemui.Flags.FLAG_ENABLE_BACKGROUND_KEYGUARD_ONDRAWN_CALLBACK, com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIP_FOR_MUTE_VOLUME, com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIP_FOR_POWER_OFF, com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIP_FOR_TAKE_SCREENSHOT, com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIPS, com.android.systemui.Flags.FLAG_ENABLE_EFFICIENT_DISPLAY_REPOSITORY, com.android.systemui.Flags.FLAG_ENABLE_LAYOUT_TRACING, com.android.systemui.Flags.FLAG_ENABLE_VIEW_CAPTURE_TRACING, com.android.systemui.Flags.FLAG_ENABLE_WIDGET_PICKER_SIZE_FILTER, com.android.systemui.Flags.FLAG_ENFORCE_BRIGHTNESS_BASE_USER_RESTRICTION, com.android.systemui.Flags.FLAG_EXAMPLE_FLAG, com.android.systemui.Flags.FLAG_FAST_UNLOCK_TRANSITION, com.android.systemui.Flags.FLAG_FIX_IMAGE_WALLPAPER_CRASH_SURFACE_ALREADY_RELEASED, com.android.systemui.Flags.FLAG_FIX_SCREENSHOT_ACTION_DISMISS_SYSTEM_WINDOWS, com.android.systemui.Flags.FLAG_FLOATING_MENU_ANIMATED_TUCK, com.android.systemui.Flags.FLAG_FLOATING_MENU_DRAG_TO_EDIT, com.android.systemui.Flags.FLAG_FLOATING_MENU_DRAG_TO_HIDE, com.android.systemui.Flags.FLAG_FLOATING_MENU_IME_DISPLACEMENT_ANIMATION, com.android.systemui.Flags.FLAG_FLOATING_MENU_NARROW_TARGET_CONTENT_OBSERVER, com.android.systemui.Flags.FLAG_FLOATING_MENU_OVERLAPS_NAV_BARS_FLAG, com.android.systemui.Flags.FLAG_FLOATING_MENU_RADII_ANIMATION, com.android.systemui.Flags.FLAG_GET_CONNECTED_DEVICE_NAME_UNSYNCHRONIZED, com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_ALLOW_KEYGUARD_WHEN_DREAMING, com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_FULLSCREEN_SWIPE, com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_GESTURE_HANDLE, com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_SHORTCUT_BUTTON, com.android.systemui.Flags.FLAG_HAPTIC_BRIGHTNESS_SLIDER, com.android.systemui.Flags.FLAG_HAPTIC_VOLUME_SLIDER, com.android.systemui.Flags.FLAG_HEARING_AIDS_QS_TILE_DIALOG, com.android.systemui.Flags.FLAG_HEARING_DEVICES_DIALOG_RELATED_TOOLS, com.android.systemui.Flags.FLAG_KEYBOARD_DOCKING_INDICATOR, com.android.systemui.Flags.FLAG_KEYBOARD_SHORTCUT_HELPER_REWRITE, com.android.systemui.Flags.FLAG_KEYGUARD_BOTTOM_AREA_REFACTOR, com.android.systemui.Flags.FLAG_KEYGUARD_WM_STATE_REFACTOR, com.android.systemui.Flags.FLAG_LIGHT_REVEAL_MIGRATION, com.android.systemui.Flags.FLAG_MEDIA_CONTROLS_LOCKSCREEN_SHADE_BUG_FIX, com.android.systemui.Flags.FLAG_MEDIA_CONTROLS_REFACTOR, com.android.systemui.Flags.FLAG_MEDIA_CONTROLS_USER_INITIATED_DELETEINTENT, com.android.systemui.Flags.FLAG_MIGRATE_CLOCKS_TO_BLUEPRINT, com.android.systemui.Flags.FLAG_NEW_AOD_TRANSITION, com.android.systemui.Flags.FLAG_NEW_TOUCHPAD_GESTURES_TUTORIAL, com.android.systemui.Flags.FLAG_NEW_VOLUME_PANEL, com.android.systemui.Flags.FLAG_NOTIFICATION_ASYNC_GROUP_HEADER_INFLATION, com.android.systemui.Flags.FLAG_NOTIFICATION_ASYNC_HYBRID_VIEW_INFLATION, com.android.systemui.Flags.FLAG_NOTIFICATION_AVALANCHE_SUPPRESSION, com.android.systemui.Flags.FLAG_NOTIFICATION_AVALANCHE_THROTTLE_HUN, com.android.systemui.Flags.FLAG_NOTIFICATION_BACKGROUND_TINT_OPTIMIZATION, com.android.systemui.Flags.FLAG_NOTIFICATION_COLOR_UPDATE_LOGGER, com.android.systemui.Flags.FLAG_NOTIFICATION_CONTENT_ALPHA_OPTIMIZATION, com.android.systemui.Flags.FLAG_NOTIFICATION_FOOTER_BACKGROUND_TINT_OPTIMIZATION, com.android.systemui.Flags.FLAG_NOTIFICATION_MEDIA_MANAGER_BACKGROUND_EXECUTION, com.android.systemui.Flags.FLAG_NOTIFICATION_MINIMALISM_PROTOTYPE, com.android.systemui.Flags.FLAG_NOTIFICATION_OVER_EXPANSION_CLIPPING_FIX, com.android.systemui.Flags.FLAG_NOTIFICATION_PULSING_FIX, com.android.systemui.Flags.FLAG_NOTIFICATION_ROW_CONTENT_BINDER_REFACTOR, com.android.systemui.Flags.FLAG_NOTIFICATION_ROW_USER_CONTEXT, com.android.systemui.Flags.FLAG_NOTIFICATION_VIEW_FLIPPER_PAUSING_V2, com.android.systemui.Flags.FLAG_NOTIFICATIONS_BACKGROUND_ICONS, com.android.systemui.Flags.FLAG_NOTIFICATIONS_FOOTER_VIEW_REFACTOR, com.android.systemui.Flags.FLAG_NOTIFICATIONS_HEADS_UP_REFACTOR, com.android.systemui.Flags.FLAG_NOTIFICATIONS_HIDE_ON_DISPLAY_SWITCH, com.android.systemui.Flags.FLAG_NOTIFICATIONS_ICON_CONTAINER_REFACTOR, com.android.systemui.Flags.FLAG_NOTIFICATIONS_IMPROVED_HUN_ANIMATION, com.android.systemui.Flags.FLAG_NOTIFICATIONS_LIVE_DATA_STORE_REFACTOR, com.android.systemui.Flags.FLAG_NOTIFY_POWER_MANAGER_USER_ACTIVITY_BACKGROUND, com.android.systemui.Flags.FLAG_PIN_INPUT_FIELD_STYLED_FOCUS_STATE, com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_ANIMATE_BOUNCER, com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_ANIMATE_DIALOGS, com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_ANIMATE_SHADE, com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_SYSUI, com.android.systemui.Flags.FLAG_PRIORITY_PEOPLE_SECTION, com.android.systemui.Flags.FLAG_PRIVACY_DOT_UNFOLD_WRONG_CORNER_FIX, com.android.systemui.Flags.FLAG_PSS_APP_SELECTOR_ABRUPT_EXIT_FIX, com.android.systemui.Flags.FLAG_PSS_APP_SELECTOR_RECENTS_SPLIT_SCREEN, com.android.systemui.Flags.FLAG_PSS_TASK_SWITCHER, com.android.systemui.Flags.FLAG_QS_CUSTOM_TILE_CLICK_GUARANTEED_BUG_FIX, com.android.systemui.Flags.FLAG_QS_NEW_PIPELINE, com.android.systemui.Flags.FLAG_QS_NEW_TILES, com.android.systemui.Flags.FLAG_QS_NEW_TILES_FUTURE, com.android.systemui.Flags.FLAG_QS_TILE_FOCUS_STATE, com.android.systemui.Flags.FLAG_QS_UI_REFACTOR, com.android.systemui.Flags.FLAG_QUICK_SETTINGS_VISUAL_HAPTICS_LONGPRESS, com.android.systemui.Flags.FLAG_RECORD_ISSUE_QS_TILE, com.android.systemui.Flags.FLAG_REFACTOR_GET_CURRENT_USER, com.android.systemui.Flags.FLAG_REGISTER_BATTERY_CONTROLLER_RECEIVERS_IN_CORESTARTABLE, com.android.systemui.Flags.FLAG_REGISTER_NEW_WALLET_CARD_IN_BACKGROUND, com.android.systemui.Flags.FLAG_REGISTER_WALLPAPER_NOTIFIER_BACKGROUND, com.android.systemui.Flags.FLAG_REGISTER_ZEN_MODE_CONTENT_OBSERVER_BACKGROUND, com.android.systemui.Flags.FLAG_REMOVE_DREAM_OVERLAY_HIDE_ON_TOUCH, com.android.systemui.Flags.FLAG_REST_TO_UNLOCK, com.android.systemui.Flags.FLAG_RESTART_DREAM_ON_UNOCCLUDE, com.android.systemui.Flags.FLAG_REVAMPED_BOUNCER_MESSAGES, com.android.systemui.Flags.FLAG_RUN_FINGERPRINT_DETECT_ON_DISMISSIBLE_KEYGUARD, com.android.systemui.Flags.FLAG_SAVE_AND_RESTORE_MAGNIFICATION_SETTINGS_BUTTONS, com.android.systemui.Flags.FLAG_SCENE_CONTAINER, com.android.systemui.Flags.FLAG_SCREENSHARE_NOTIFICATION_HIDING_BUG_FIX, com.android.systemui.Flags.FLAG_SCREENSHOT_ACTION_DISMISS_SYSTEM_WINDOWS, com.android.systemui.Flags.FLAG_SCREENSHOT_PRIVATE_PROFILE_ACCESSIBILITY_ANNOUNCEMENT_FIX, com.android.systemui.Flags.FLAG_SCREENSHOT_PRIVATE_PROFILE_BEHAVIOR_FIX, com.android.systemui.Flags.FLAG_SCREENSHOT_SCROLL_CROP_VIEW_CRASH_FIX, com.android.systemui.Flags.FLAG_SCREENSHOT_SHELF_UI2, com.android.systemui.Flags.FLAG_SHADE_COLLAPSE_ACTIVITY_LAUNCH_FIX, com.android.systemui.Flags.FLAG_SHADERLIB_LOADING_EFFECT_REFACTOR, com.android.systemui.Flags.FLAG_SLICE_BROADCAST_RELAY_IN_BACKGROUND, com.android.systemui.Flags.FLAG_SLICE_MANAGER_BINDER_CALL_BACKGROUND, com.android.systemui.Flags.FLAG_SMARTSPACE_LOCKSCREEN_VIEWMODEL, com.android.systemui.Flags.FLAG_SMARTSPACE_RELOCATE_TO_BOTTOM, com.android.systemui.Flags.FLAG_SMARTSPACE_REMOTEVIEWS_RENDERING, com.android.systemui.Flags.FLAG_STATUS_BAR_MONOCHROME_ICONS_FIX, com.android.systemui.Flags.FLAG_STATUS_BAR_SCREEN_SHARING_CHIPS, com.android.systemui.Flags.FLAG_STATUS_BAR_STATIC_INOUT_INDICATORS, com.android.systemui.Flags.FLAG_SWITCH_USER_ON_BG, com.android.systemui.Flags.FLAG_SYSUI_TEAMFOOD, com.android.systemui.Flags.FLAG_THEME_OVERLAY_CONTROLLER_WAKEFULNESS_DEPRECATION, com.android.systemui.Flags.FLAG_THREE_BUTTON_CORNER_SWIPE, com.android.systemui.Flags.FLAG_TRUNCATED_STATUS_BAR_ICONS_FIX, com.android.systemui.Flags.FLAG_UDFPS_VIEW_PERFORMANCE, com.android.systemui.Flags.FLAG_UNFOLD_ANIMATION_BACKGROUND_PROGRESS, com.android.systemui.Flags.FLAG_UPDATE_USER_SWITCHER_BACKGROUND, com.android.systemui.Flags.FLAG_VALIDATE_KEYBOARD_SHORTCUT_HELPER_ICON_URI, com.android.systemui.Flags.FLAG_VISUAL_INTERRUPTIONS_REFACTOR, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.systemui.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean activityTransitionUseLargestWindow() {
        return getValue(com.android.systemui.Flags.FLAG_ACTIVITY_TRANSITION_USE_LARGEST_WINDOW, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda106
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).activityTransitionUseLargestWindow();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean ambientTouchMonitorListenToDisplayChanges() {
        return getValue(com.android.systemui.Flags.FLAG_AMBIENT_TOUCH_MONITOR_LISTEN_TO_DISPLAY_CHANGES, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda91
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).ambientTouchMonitorListenToDisplayChanges();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean appClipsBacklinks() {
        return getValue(com.android.systemui.Flags.FLAG_APP_CLIPS_BACKLINKS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda98
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).appClipsBacklinks();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean bindKeyguardMediaVisibility() {
        return getValue(com.android.systemui.Flags.FLAG_BIND_KEYGUARD_MEDIA_VISIBILITY, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda49
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).bindKeyguardMediaVisibility();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean bpTalkback() {
        return getValue(com.android.systemui.Flags.FLAG_BP_TALKBACK, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda40
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).bpTalkback();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean brightnessSliderFocusState() {
        return getValue(com.android.systemui.Flags.FLAG_BRIGHTNESS_SLIDER_FOCUS_STATE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda82
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).brightnessSliderFocusState();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean centralizedStatusBarHeightFix() {
        return getValue(com.android.systemui.Flags.FLAG_CENTRALIZED_STATUS_BAR_HEIGHT_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda38
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).centralizedStatusBarHeightFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean clipboardNoninteractiveOnLockscreen() {
        return getValue(com.android.systemui.Flags.FLAG_CLIPBOARD_NONINTERACTIVE_ON_LOCKSCREEN, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda149
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).clipboardNoninteractiveOnLockscreen();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean clockReactiveVariants() {
        return getValue(com.android.systemui.Flags.FLAG_CLOCK_REACTIVE_VARIANTS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).clockReactiveVariants();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean communalBouncerDoNotModifyPluginOpen() {
        return getValue(com.android.systemui.Flags.FLAG_COMMUNAL_BOUNCER_DO_NOT_MODIFY_PLUGIN_OPEN, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda84
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).communalBouncerDoNotModifyPluginOpen();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean communalHub() {
        return getValue(com.android.systemui.Flags.FLAG_COMMUNAL_HUB, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda119
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).communalHub();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean composeBouncer() {
        return getValue(com.android.systemui.Flags.FLAG_COMPOSE_BOUNCER, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda133
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).composeBouncer();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean composeLockscreen() {
        return getValue(com.android.systemui.Flags.FLAG_COMPOSE_LOCKSCREEN, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda104
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).composeLockscreen();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean confineNotificationTouchToViewWidth() {
        return getValue(com.android.systemui.Flags.FLAG_CONFINE_NOTIFICATION_TOUCH_TO_VIEW_WIDTH, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda51
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).confineNotificationTouchToViewWidth();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean constraintBp() {
        return getValue(com.android.systemui.Flags.FLAG_CONSTRAINT_BP, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda50
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).constraintBp();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean contextualTipsAssistantDismissFix() {
        return getValue(com.android.systemui.Flags.FLAG_CONTEXTUAL_TIPS_ASSISTANT_DISMISS_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda123
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).contextualTipsAssistantDismissFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean coroutineTracing() {
        return getValue(com.android.systemui.Flags.FLAG_COROUTINE_TRACING, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda150
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).coroutineTracing();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean createWindowlessWindowMagnifier() {
        return getValue(com.android.systemui.Flags.FLAG_CREATE_WINDOWLESS_WINDOW_MAGNIFIER, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda24
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).createWindowlessWindowMagnifier();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean dedicatedNotifInflationThread() {
        return getValue(com.android.systemui.Flags.FLAG_DEDICATED_NOTIF_INFLATION_THREAD, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda73
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).dedicatedNotifInflationThread();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean delayShowMagnificationButton() {
        return getValue(com.android.systemui.Flags.FLAG_DELAY_SHOW_MAGNIFICATION_BUTTON, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda37
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).delayShowMagnificationButton();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean delayedWakelockReleaseOnBackgroundThread() {
        return getValue(com.android.systemui.Flags.FLAG_DELAYED_WAKELOCK_RELEASE_ON_BACKGROUND_THREAD, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda29
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).delayedWakelockReleaseOnBackgroundThread();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean deviceEntryUdfpsRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_DEVICE_ENTRY_UDFPS_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).deviceEntryUdfpsRefactor();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean disableContextualTipsFrequencyCheck() {
        return getValue(com.android.systemui.Flags.FLAG_DISABLE_CONTEXTUAL_TIPS_FREQUENCY_CHECK, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda72
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).disableContextualTipsFrequencyCheck();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean disableContextualTipsIosSwitcherCheck() {
        return getValue(com.android.systemui.Flags.FLAG_DISABLE_CONTEXTUAL_TIPS_IOS_SWITCHER_CHECK, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda52
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).disableContextualTipsIosSwitcherCheck();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean dozeuiSchedulingAlarmsBackgroundExecution() {
        return getValue(com.android.systemui.Flags.FLAG_DOZEUI_SCHEDULING_ALARMS_BACKGROUND_EXECUTION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda34
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).dozeuiSchedulingAlarmsBackgroundExecution();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean dreamInputSessionPilferOnce() {
        return getValue(com.android.systemui.Flags.FLAG_DREAM_INPUT_SESSION_PILFER_ONCE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda113
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).dreamInputSessionPilferOnce();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean dreamOverlayBouncerSwipeDirectionFiltering() {
        return getValue(com.android.systemui.Flags.FLAG_DREAM_OVERLAY_BOUNCER_SWIPE_DIRECTION_FILTERING, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).dreamOverlayBouncerSwipeDirectionFiltering();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean dualShade() {
        return getValue(com.android.systemui.Flags.FLAG_DUAL_SHADE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda127
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).dualShade();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean edgeBackGestureHandlerThread() {
        return getValue(com.android.systemui.Flags.FLAG_EDGE_BACK_GESTURE_HANDLER_THREAD, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda117
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).edgeBackGestureHandlerThread();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean edgebackGestureHandlerGetRunningTasksBackground() {
        return getValue(com.android.systemui.Flags.FLAG_EDGEBACK_GESTURE_HANDLER_GET_RUNNING_TASKS_BACKGROUND, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda22
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).edgebackGestureHandlerGetRunningTasksBackground();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableBackgroundKeyguardOndrawnCallback() {
        return getValue(com.android.systemui.Flags.FLAG_ENABLE_BACKGROUND_KEYGUARD_ONDRAWN_CALLBACK, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda28
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).enableBackgroundKeyguardOndrawnCallback();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableContextualTipForMuteVolume() {
        return getValue(com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIP_FOR_MUTE_VOLUME, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda132
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).enableContextualTipForMuteVolume();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableContextualTipForPowerOff() {
        return getValue(com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIP_FOR_POWER_OFF, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda141
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).enableContextualTipForPowerOff();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableContextualTipForTakeScreenshot() {
        return getValue(com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIP_FOR_TAKE_SCREENSHOT, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda41
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).enableContextualTipForTakeScreenshot();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableContextualTips() {
        return getValue(com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIPS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda31
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).enableContextualTips();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableEfficientDisplayRepository() {
        return getValue(com.android.systemui.Flags.FLAG_ENABLE_EFFICIENT_DISPLAY_REPOSITORY, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda145
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).enableEfficientDisplayRepository();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableLayoutTracing() {
        return getValue(com.android.systemui.Flags.FLAG_ENABLE_LAYOUT_TRACING, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda25
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).enableLayoutTracing();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableViewCaptureTracing() {
        return getValue(com.android.systemui.Flags.FLAG_ENABLE_VIEW_CAPTURE_TRACING, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).enableViewCaptureTracing();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enableWidgetPickerSizeFilter() {
        return getValue(com.android.systemui.Flags.FLAG_ENABLE_WIDGET_PICKER_SIZE_FILTER, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda120
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).enableWidgetPickerSizeFilter();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean enforceBrightnessBaseUserRestriction() {
        return getValue(com.android.systemui.Flags.FLAG_ENFORCE_BRIGHTNESS_BASE_USER_RESTRICTION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).enforceBrightnessBaseUserRestriction();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean exampleFlag() {
        return getValue(com.android.systemui.Flags.FLAG_EXAMPLE_FLAG, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda128
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).exampleFlag();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean fastUnlockTransition() {
        return getValue(com.android.systemui.Flags.FLAG_FAST_UNLOCK_TRANSITION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda126
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).fastUnlockTransition();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean fixImageWallpaperCrashSurfaceAlreadyReleased() {
        return getValue(com.android.systemui.Flags.FLAG_FIX_IMAGE_WALLPAPER_CRASH_SURFACE_ALREADY_RELEASED, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda107
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).fixImageWallpaperCrashSurfaceAlreadyReleased();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean fixScreenshotActionDismissSystemWindows() {
        return getValue(com.android.systemui.Flags.FLAG_FIX_SCREENSHOT_ACTION_DISMISS_SYSTEM_WINDOWS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda81
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).fixScreenshotActionDismissSystemWindows();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuAnimatedTuck() {
        return getValue(com.android.systemui.Flags.FLAG_FLOATING_MENU_ANIMATED_TUCK, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda89
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).floatingMenuAnimatedTuck();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuDragToEdit() {
        return getValue(com.android.systemui.Flags.FLAG_FLOATING_MENU_DRAG_TO_EDIT, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda33
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).floatingMenuDragToEdit();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuDragToHide() {
        return getValue(com.android.systemui.Flags.FLAG_FLOATING_MENU_DRAG_TO_HIDE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda66
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).floatingMenuDragToHide();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuImeDisplacementAnimation() {
        return getValue(com.android.systemui.Flags.FLAG_FLOATING_MENU_IME_DISPLACEMENT_ANIMATION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda75
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).floatingMenuImeDisplacementAnimation();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuNarrowTargetContentObserver() {
        return getValue(com.android.systemui.Flags.FLAG_FLOATING_MENU_NARROW_TARGET_CONTENT_OBSERVER, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda131
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).floatingMenuNarrowTargetContentObserver();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuOverlapsNavBarsFlag() {
        return getValue(com.android.systemui.Flags.FLAG_FLOATING_MENU_OVERLAPS_NAV_BARS_FLAG, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).floatingMenuOverlapsNavBarsFlag();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean floatingMenuRadiiAnimation() {
        return getValue(com.android.systemui.Flags.FLAG_FLOATING_MENU_RADII_ANIMATION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda32
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).floatingMenuRadiiAnimation();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean getConnectedDeviceNameUnsynchronized() {
        return getValue(com.android.systemui.Flags.FLAG_GET_CONNECTED_DEVICE_NAME_UNSYNCHRONIZED, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda94
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).getConnectedDeviceNameUnsynchronized();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean glanceableHubAllowKeyguardWhenDreaming() {
        return getValue(com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_ALLOW_KEYGUARD_WHEN_DREAMING, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda114
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).glanceableHubAllowKeyguardWhenDreaming();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean glanceableHubFullscreenSwipe() {
        return getValue(com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_FULLSCREEN_SWIPE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda121
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).glanceableHubFullscreenSwipe();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean glanceableHubGestureHandle() {
        return getValue(com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_GESTURE_HANDLE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).glanceableHubGestureHandle();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean glanceableHubShortcutButton() {
        return getValue(com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_SHORTCUT_BUTTON, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda70
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).glanceableHubShortcutButton();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean hapticBrightnessSlider() {
        return getValue(com.android.systemui.Flags.FLAG_HAPTIC_BRIGHTNESS_SLIDER, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda47
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).hapticBrightnessSlider();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean hapticVolumeSlider() {
        return getValue(com.android.systemui.Flags.FLAG_HAPTIC_VOLUME_SLIDER, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda122
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).hapticVolumeSlider();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean hearingAidsQsTileDialog() {
        return getValue(com.android.systemui.Flags.FLAG_HEARING_AIDS_QS_TILE_DIALOG, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda44
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).hearingAidsQsTileDialog();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean hearingDevicesDialogRelatedTools() {
        return getValue(com.android.systemui.Flags.FLAG_HEARING_DEVICES_DIALOG_RELATED_TOOLS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda60
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).hearingDevicesDialogRelatedTools();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean keyboardDockingIndicator() {
        return getValue(com.android.systemui.Flags.FLAG_KEYBOARD_DOCKING_INDICATOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda139
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).keyboardDockingIndicator();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean keyboardShortcutHelperRewrite() {
        return getValue(com.android.systemui.Flags.FLAG_KEYBOARD_SHORTCUT_HELPER_REWRITE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda148
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).keyboardShortcutHelperRewrite();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean keyguardBottomAreaRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_KEYGUARD_BOTTOM_AREA_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda97
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).keyguardBottomAreaRefactor();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean keyguardWmStateRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_KEYGUARD_WM_STATE_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda36
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).keyguardWmStateRefactor();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean lightRevealMigration() {
        return getValue(com.android.systemui.Flags.FLAG_LIGHT_REVEAL_MIGRATION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda56
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).lightRevealMigration();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean mediaControlsLockscreenShadeBugFix() {
        return getValue(com.android.systemui.Flags.FLAG_MEDIA_CONTROLS_LOCKSCREEN_SHADE_BUG_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda27
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).mediaControlsLockscreenShadeBugFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean mediaControlsRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_MEDIA_CONTROLS_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda112
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).mediaControlsRefactor();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean mediaControlsUserInitiatedDeleteintent() {
        return getValue(com.android.systemui.Flags.FLAG_MEDIA_CONTROLS_USER_INITIATED_DELETEINTENT, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda100
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).mediaControlsUserInitiatedDeleteintent();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean migrateClocksToBlueprint() {
        return getValue(com.android.systemui.Flags.FLAG_MIGRATE_CLOCKS_TO_BLUEPRINT, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda88
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).migrateClocksToBlueprint();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean newAodTransition() {
        return getValue(com.android.systemui.Flags.FLAG_NEW_AOD_TRANSITION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).newAodTransition();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean newTouchpadGesturesTutorial() {
        return getValue(com.android.systemui.Flags.FLAG_NEW_TOUCHPAD_GESTURES_TUTORIAL, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda136
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).newTouchpadGesturesTutorial();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean newVolumePanel() {
        return getValue(com.android.systemui.Flags.FLAG_NEW_VOLUME_PANEL, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda135
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).newVolumePanel();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationAsyncGroupHeaderInflation() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_ASYNC_GROUP_HEADER_INFLATION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda30
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationAsyncGroupHeaderInflation();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationAsyncHybridViewInflation() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_ASYNC_HYBRID_VIEW_INFLATION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda118
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationAsyncHybridViewInflation();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationAvalancheSuppression() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_AVALANCHE_SUPPRESSION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda42
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationAvalancheSuppression();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationAvalancheThrottleHun() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_AVALANCHE_THROTTLE_HUN, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda53
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationAvalancheThrottleHun();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationBackgroundTintOptimization() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_BACKGROUND_TINT_OPTIMIZATION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda63
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationBackgroundTintOptimization();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationColorUpdateLogger() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_COLOR_UPDATE_LOGGER, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda144
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationColorUpdateLogger();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationContentAlphaOptimization() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_CONTENT_ALPHA_OPTIMIZATION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda78
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationContentAlphaOptimization();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationFooterBackgroundTintOptimization() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_FOOTER_BACKGROUND_TINT_OPTIMIZATION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationFooterBackgroundTintOptimization();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationMediaManagerBackgroundExecution() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_MEDIA_MANAGER_BACKGROUND_EXECUTION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda16
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationMediaManagerBackgroundExecution();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationMinimalismPrototype() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_MINIMALISM_PROTOTYPE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda77
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationMinimalismPrototype();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationOverExpansionClippingFix() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_OVER_EXPANSION_CLIPPING_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda95
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationOverExpansionClippingFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationPulsingFix() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_PULSING_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda90
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationPulsingFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationRowContentBinderRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_ROW_CONTENT_BINDER_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda111
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationRowContentBinderRefactor();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationRowUserContext() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_ROW_USER_CONTEXT, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda142
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationRowUserContext();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationViewFlipperPausingV2() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATION_VIEW_FLIPPER_PAUSING_V2, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda115
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationViewFlipperPausingV2();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsBackgroundIcons() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATIONS_BACKGROUND_ICONS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda43
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationsBackgroundIcons();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsFooterViewRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATIONS_FOOTER_VIEW_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda108
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationsFooterViewRefactor();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsHeadsUpRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATIONS_HEADS_UP_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda39
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationsHeadsUpRefactor();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsHideOnDisplaySwitch() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATIONS_HIDE_ON_DISPLAY_SWITCH, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda137
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationsHideOnDisplaySwitch();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsIconContainerRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATIONS_ICON_CONTAINER_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda138
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationsIconContainerRefactor();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsImprovedHunAnimation() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATIONS_IMPROVED_HUN_ANIMATION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda83
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationsImprovedHunAnimation();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notificationsLiveDataStoreRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFICATIONS_LIVE_DATA_STORE_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda92
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notificationsLiveDataStoreRefactor();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean notifyPowerManagerUserActivityBackground() {
        return getValue(com.android.systemui.Flags.FLAG_NOTIFY_POWER_MANAGER_USER_ACTIVITY_BACKGROUND, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda21
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).notifyPowerManagerUserActivityBackground();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean pinInputFieldStyledFocusState() {
        return getValue(com.android.systemui.Flags.FLAG_PIN_INPUT_FIELD_STYLED_FOCUS_STATE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda93
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).pinInputFieldStyledFocusState();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean predictiveBackAnimateBouncer() {
        return getValue(com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_ANIMATE_BOUNCER, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda68
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).predictiveBackAnimateBouncer();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean predictiveBackAnimateDialogs() {
        return getValue(com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_ANIMATE_DIALOGS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda26
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).predictiveBackAnimateDialogs();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean predictiveBackAnimateShade() {
        return getValue(com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_ANIMATE_SHADE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda129
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).predictiveBackAnimateShade();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean predictiveBackSysui() {
        return getValue(com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_SYSUI, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda62
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).predictiveBackSysui();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean priorityPeopleSection() {
        return getValue(com.android.systemui.Flags.FLAG_PRIORITY_PEOPLE_SECTION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda86
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).priorityPeopleSection();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean privacyDotUnfoldWrongCornerFix() {
        return getValue(com.android.systemui.Flags.FLAG_PRIVACY_DOT_UNFOLD_WRONG_CORNER_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda140
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).privacyDotUnfoldWrongCornerFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean pssAppSelectorAbruptExitFix() {
        return getValue(com.android.systemui.Flags.FLAG_PSS_APP_SELECTOR_ABRUPT_EXIT_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda54
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).pssAppSelectorAbruptExitFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean pssAppSelectorRecentsSplitScreen() {
        return getValue(com.android.systemui.Flags.FLAG_PSS_APP_SELECTOR_RECENTS_SPLIT_SCREEN, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).pssAppSelectorRecentsSplitScreen();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean pssTaskSwitcher() {
        return getValue(com.android.systemui.Flags.FLAG_PSS_TASK_SWITCHER, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda146
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).pssTaskSwitcher();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsCustomTileClickGuaranteedBugFix() {
        return getValue(com.android.systemui.Flags.FLAG_QS_CUSTOM_TILE_CLICK_GUARANTEED_BUG_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda87
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).qsCustomTileClickGuaranteedBugFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsNewPipeline() {
        return getValue(com.android.systemui.Flags.FLAG_QS_NEW_PIPELINE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda48
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).qsNewPipeline();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsNewTiles() {
        return getValue(com.android.systemui.Flags.FLAG_QS_NEW_TILES, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda101
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).qsNewTiles();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsNewTilesFuture() {
        return getValue(com.android.systemui.Flags.FLAG_QS_NEW_TILES_FUTURE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda125
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).qsNewTilesFuture();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsTileFocusState() {
        return getValue(com.android.systemui.Flags.FLAG_QS_TILE_FOCUS_STATE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).qsTileFocusState();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean qsUiRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_QS_UI_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda19
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).qsUiRefactor();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean quickSettingsVisualHapticsLongpress() {
        return getValue(com.android.systemui.Flags.FLAG_QUICK_SETTINGS_VISUAL_HAPTICS_LONGPRESS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda124
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).quickSettingsVisualHapticsLongpress();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean recordIssueQsTile() {
        return getValue(com.android.systemui.Flags.FLAG_RECORD_ISSUE_QS_TILE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda99
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).recordIssueQsTile();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean refactorGetCurrentUser() {
        return getValue(com.android.systemui.Flags.FLAG_REFACTOR_GET_CURRENT_USER, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda20
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).refactorGetCurrentUser();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean registerBatteryControllerReceiversInCorestartable() {
        return getValue(com.android.systemui.Flags.FLAG_REGISTER_BATTERY_CONTROLLER_RECEIVERS_IN_CORESTARTABLE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda45
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).registerBatteryControllerReceiversInCorestartable();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean registerNewWalletCardInBackground() {
        return getValue(com.android.systemui.Flags.FLAG_REGISTER_NEW_WALLET_CARD_IN_BACKGROUND, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda64
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).registerNewWalletCardInBackground();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean registerWallpaperNotifierBackground() {
        return getValue(com.android.systemui.Flags.FLAG_REGISTER_WALLPAPER_NOTIFIER_BACKGROUND, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).registerWallpaperNotifierBackground();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean registerZenModeContentObserverBackground() {
        return getValue(com.android.systemui.Flags.FLAG_REGISTER_ZEN_MODE_CONTENT_OBSERVER_BACKGROUND, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).registerZenModeContentObserverBackground();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean removeDreamOverlayHideOnTouch() {
        return getValue(com.android.systemui.Flags.FLAG_REMOVE_DREAM_OVERLAY_HIDE_ON_TOUCH, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda143
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).removeDreamOverlayHideOnTouch();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean restToUnlock() {
        return getValue(com.android.systemui.Flags.FLAG_REST_TO_UNLOCK, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).restToUnlock();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean restartDreamOnUnocclude() {
        return getValue(com.android.systemui.Flags.FLAG_RESTART_DREAM_ON_UNOCCLUDE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda71
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).restartDreamOnUnocclude();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean revampedBouncerMessages() {
        return getValue(com.android.systemui.Flags.FLAG_REVAMPED_BOUNCER_MESSAGES, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda110
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).revampedBouncerMessages();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean runFingerprintDetectOnDismissibleKeyguard() {
        return getValue(com.android.systemui.Flags.FLAG_RUN_FINGERPRINT_DETECT_ON_DISMISSIBLE_KEYGUARD, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda67
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).runFingerprintDetectOnDismissibleKeyguard();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean saveAndRestoreMagnificationSettingsButtons() {
        return getValue(com.android.systemui.Flags.FLAG_SAVE_AND_RESTORE_MAGNIFICATION_SETTINGS_BUTTONS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda55
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).saveAndRestoreMagnificationSettingsButtons();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean sceneContainer() {
        return getValue(com.android.systemui.Flags.FLAG_SCENE_CONTAINER, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda109
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).sceneContainer();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshareNotificationHidingBugFix() {
        return getValue(com.android.systemui.Flags.FLAG_SCREENSHARE_NOTIFICATION_HIDING_BUG_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).screenshareNotificationHidingBugFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshotActionDismissSystemWindows() {
        return getValue(com.android.systemui.Flags.FLAG_SCREENSHOT_ACTION_DISMISS_SYSTEM_WINDOWS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).screenshotActionDismissSystemWindows();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshotPrivateProfileAccessibilityAnnouncementFix() {
        return getValue(com.android.systemui.Flags.FLAG_SCREENSHOT_PRIVATE_PROFILE_ACCESSIBILITY_ANNOUNCEMENT_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda65
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).screenshotPrivateProfileAccessibilityAnnouncementFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshotPrivateProfileBehaviorFix() {
        return getValue(com.android.systemui.Flags.FLAG_SCREENSHOT_PRIVATE_PROFILE_BEHAVIOR_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda76
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).screenshotPrivateProfileBehaviorFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshotScrollCropViewCrashFix() {
        return getValue(com.android.systemui.Flags.FLAG_SCREENSHOT_SCROLL_CROP_VIEW_CRASH_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).screenshotScrollCropViewCrashFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean screenshotShelfUi2() {
        return getValue(com.android.systemui.Flags.FLAG_SCREENSHOT_SHELF_UI2, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda61
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).screenshotShelfUi2();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean shadeCollapseActivityLaunchFix() {
        return getValue(com.android.systemui.Flags.FLAG_SHADE_COLLAPSE_ACTIVITY_LAUNCH_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda57
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).shadeCollapseActivityLaunchFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean shaderlibLoadingEffectRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_SHADERLIB_LOADING_EFFECT_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda102
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).shaderlibLoadingEffectRefactor();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean sliceBroadcastRelayInBackground() {
        return getValue(com.android.systemui.Flags.FLAG_SLICE_BROADCAST_RELAY_IN_BACKGROUND, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda147
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).sliceBroadcastRelayInBackground();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean sliceManagerBinderCallBackground() {
        return getValue(com.android.systemui.Flags.FLAG_SLICE_MANAGER_BINDER_CALL_BACKGROUND, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda35
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).sliceManagerBinderCallBackground();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean smartspaceLockscreenViewmodel() {
        return getValue(com.android.systemui.Flags.FLAG_SMARTSPACE_LOCKSCREEN_VIEWMODEL, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda134
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).smartspaceLockscreenViewmodel();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean smartspaceRelocateToBottom() {
        return getValue(com.android.systemui.Flags.FLAG_SMARTSPACE_RELOCATE_TO_BOTTOM, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda74
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).smartspaceRelocateToBottom();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean smartspaceRemoteviewsRendering() {
        return getValue(com.android.systemui.Flags.FLAG_SMARTSPACE_REMOTEVIEWS_RENDERING, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda69
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).smartspaceRemoteviewsRendering();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean statusBarMonochromeIconsFix() {
        return getValue(com.android.systemui.Flags.FLAG_STATUS_BAR_MONOCHROME_ICONS_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda79
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).statusBarMonochromeIconsFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean statusBarScreenSharingChips() {
        return getValue(com.android.systemui.Flags.FLAG_STATUS_BAR_SCREEN_SHARING_CHIPS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda85
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).statusBarScreenSharingChips();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean statusBarStaticInoutIndicators() {
        return getValue(com.android.systemui.Flags.FLAG_STATUS_BAR_STATIC_INOUT_INDICATORS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda105
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).statusBarStaticInoutIndicators();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean switchUserOnBg() {
        return getValue(com.android.systemui.Flags.FLAG_SWITCH_USER_ON_BG, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda46
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).switchUserOnBg();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean sysuiTeamfood() {
        return getValue(com.android.systemui.Flags.FLAG_SYSUI_TEAMFOOD, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda103
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).sysuiTeamfood();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean themeOverlayControllerWakefulnessDeprecation() {
        return getValue(com.android.systemui.Flags.FLAG_THEME_OVERLAY_CONTROLLER_WAKEFULNESS_DEPRECATION, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda80
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).themeOverlayControllerWakefulnessDeprecation();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean threeButtonCornerSwipe() {
        return getValue(com.android.systemui.Flags.FLAG_THREE_BUTTON_CORNER_SWIPE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).threeButtonCornerSwipe();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean truncatedStatusBarIconsFix() {
        return getValue(com.android.systemui.Flags.FLAG_TRUNCATED_STATUS_BAR_ICONS_FIX, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda96
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).truncatedStatusBarIconsFix();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean udfpsViewPerformance() {
        return getValue(com.android.systemui.Flags.FLAG_UDFPS_VIEW_PERFORMANCE, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda58
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).udfpsViewPerformance();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean unfoldAnimationBackgroundProgress() {
        return getValue(com.android.systemui.Flags.FLAG_UNFOLD_ANIMATION_BACKGROUND_PROGRESS, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda59
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).unfoldAnimationBackgroundProgress();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean updateUserSwitcherBackground() {
        return getValue(com.android.systemui.Flags.FLAG_UPDATE_USER_SWITCHER_BACKGROUND, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda116
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).updateUserSwitcherBackground();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean validateKeyboardShortcutHelperIconUri() {
        return getValue(com.android.systemui.Flags.FLAG_VALIDATE_KEYBOARD_SHORTCUT_HELPER_ICON_URI, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).validateKeyboardShortcutHelperIconUri();
            }
        });
    }

    @Override // com.android.systemui.FeatureFlags
    public boolean visualInterruptionsRefactor() {
        return getValue(com.android.systemui.Flags.FLAG_VISUAL_INTERRUPTIONS_REFACTOR, new java.util.function.Predicate() { // from class: com.android.systemui.CustomFeatureFlags$$ExternalSyntheticLambda130
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.systemui.FeatureFlags) obj).visualInterruptionsRefactor();
            }
        });
    }

    public boolean isFlagReadOnlyOptimized(java.lang.String flagName) {
        if (this.mReadOnlyFlagsSet.contains(flagName) && isOptimizationEnabled()) {
            return true;
        }
        return false;
    }

    private boolean isOptimizationEnabled() {
        return false;
    }

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.systemui.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.systemui.Flags.FLAG_ACTIVITY_TRANSITION_USE_LARGEST_WINDOW, com.android.systemui.Flags.FLAG_AMBIENT_TOUCH_MONITOR_LISTEN_TO_DISPLAY_CHANGES, com.android.systemui.Flags.FLAG_APP_CLIPS_BACKLINKS, com.android.systemui.Flags.FLAG_BIND_KEYGUARD_MEDIA_VISIBILITY, com.android.systemui.Flags.FLAG_BP_TALKBACK, com.android.systemui.Flags.FLAG_BRIGHTNESS_SLIDER_FOCUS_STATE, com.android.systemui.Flags.FLAG_CENTRALIZED_STATUS_BAR_HEIGHT_FIX, com.android.systemui.Flags.FLAG_CLIPBOARD_NONINTERACTIVE_ON_LOCKSCREEN, com.android.systemui.Flags.FLAG_CLOCK_REACTIVE_VARIANTS, com.android.systemui.Flags.FLAG_COMMUNAL_BOUNCER_DO_NOT_MODIFY_PLUGIN_OPEN, com.android.systemui.Flags.FLAG_COMMUNAL_HUB, com.android.systemui.Flags.FLAG_COMPOSE_BOUNCER, com.android.systemui.Flags.FLAG_COMPOSE_LOCKSCREEN, com.android.systemui.Flags.FLAG_CONFINE_NOTIFICATION_TOUCH_TO_VIEW_WIDTH, com.android.systemui.Flags.FLAG_CONSTRAINT_BP, com.android.systemui.Flags.FLAG_CONTEXTUAL_TIPS_ASSISTANT_DISMISS_FIX, com.android.systemui.Flags.FLAG_COROUTINE_TRACING, com.android.systemui.Flags.FLAG_CREATE_WINDOWLESS_WINDOW_MAGNIFIER, com.android.systemui.Flags.FLAG_DEDICATED_NOTIF_INFLATION_THREAD, com.android.systemui.Flags.FLAG_DELAY_SHOW_MAGNIFICATION_BUTTON, com.android.systemui.Flags.FLAG_DELAYED_WAKELOCK_RELEASE_ON_BACKGROUND_THREAD, com.android.systemui.Flags.FLAG_DEVICE_ENTRY_UDFPS_REFACTOR, com.android.systemui.Flags.FLAG_DISABLE_CONTEXTUAL_TIPS_FREQUENCY_CHECK, com.android.systemui.Flags.FLAG_DISABLE_CONTEXTUAL_TIPS_IOS_SWITCHER_CHECK, com.android.systemui.Flags.FLAG_DOZEUI_SCHEDULING_ALARMS_BACKGROUND_EXECUTION, com.android.systemui.Flags.FLAG_DREAM_INPUT_SESSION_PILFER_ONCE, com.android.systemui.Flags.FLAG_DREAM_OVERLAY_BOUNCER_SWIPE_DIRECTION_FILTERING, com.android.systemui.Flags.FLAG_DUAL_SHADE, com.android.systemui.Flags.FLAG_EDGE_BACK_GESTURE_HANDLER_THREAD, com.android.systemui.Flags.FLAG_EDGEBACK_GESTURE_HANDLER_GET_RUNNING_TASKS_BACKGROUND, com.android.systemui.Flags.FLAG_ENABLE_BACKGROUND_KEYGUARD_ONDRAWN_CALLBACK, com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIP_FOR_MUTE_VOLUME, com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIP_FOR_POWER_OFF, com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIP_FOR_TAKE_SCREENSHOT, com.android.systemui.Flags.FLAG_ENABLE_CONTEXTUAL_TIPS, com.android.systemui.Flags.FLAG_ENABLE_EFFICIENT_DISPLAY_REPOSITORY, com.android.systemui.Flags.FLAG_ENABLE_LAYOUT_TRACING, com.android.systemui.Flags.FLAG_ENABLE_VIEW_CAPTURE_TRACING, com.android.systemui.Flags.FLAG_ENABLE_WIDGET_PICKER_SIZE_FILTER, com.android.systemui.Flags.FLAG_ENFORCE_BRIGHTNESS_BASE_USER_RESTRICTION, com.android.systemui.Flags.FLAG_EXAMPLE_FLAG, com.android.systemui.Flags.FLAG_FAST_UNLOCK_TRANSITION, com.android.systemui.Flags.FLAG_FIX_IMAGE_WALLPAPER_CRASH_SURFACE_ALREADY_RELEASED, com.android.systemui.Flags.FLAG_FIX_SCREENSHOT_ACTION_DISMISS_SYSTEM_WINDOWS, com.android.systemui.Flags.FLAG_FLOATING_MENU_ANIMATED_TUCK, com.android.systemui.Flags.FLAG_FLOATING_MENU_DRAG_TO_EDIT, com.android.systemui.Flags.FLAG_FLOATING_MENU_DRAG_TO_HIDE, com.android.systemui.Flags.FLAG_FLOATING_MENU_IME_DISPLACEMENT_ANIMATION, com.android.systemui.Flags.FLAG_FLOATING_MENU_NARROW_TARGET_CONTENT_OBSERVER, com.android.systemui.Flags.FLAG_FLOATING_MENU_OVERLAPS_NAV_BARS_FLAG, com.android.systemui.Flags.FLAG_FLOATING_MENU_RADII_ANIMATION, com.android.systemui.Flags.FLAG_GET_CONNECTED_DEVICE_NAME_UNSYNCHRONIZED, com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_ALLOW_KEYGUARD_WHEN_DREAMING, com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_FULLSCREEN_SWIPE, com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_GESTURE_HANDLE, com.android.systemui.Flags.FLAG_GLANCEABLE_HUB_SHORTCUT_BUTTON, com.android.systemui.Flags.FLAG_HAPTIC_BRIGHTNESS_SLIDER, com.android.systemui.Flags.FLAG_HAPTIC_VOLUME_SLIDER, com.android.systemui.Flags.FLAG_HEARING_AIDS_QS_TILE_DIALOG, com.android.systemui.Flags.FLAG_HEARING_DEVICES_DIALOG_RELATED_TOOLS, com.android.systemui.Flags.FLAG_KEYBOARD_DOCKING_INDICATOR, com.android.systemui.Flags.FLAG_KEYBOARD_SHORTCUT_HELPER_REWRITE, com.android.systemui.Flags.FLAG_KEYGUARD_BOTTOM_AREA_REFACTOR, com.android.systemui.Flags.FLAG_KEYGUARD_WM_STATE_REFACTOR, com.android.systemui.Flags.FLAG_LIGHT_REVEAL_MIGRATION, com.android.systemui.Flags.FLAG_MEDIA_CONTROLS_LOCKSCREEN_SHADE_BUG_FIX, com.android.systemui.Flags.FLAG_MEDIA_CONTROLS_REFACTOR, com.android.systemui.Flags.FLAG_MEDIA_CONTROLS_USER_INITIATED_DELETEINTENT, com.android.systemui.Flags.FLAG_MIGRATE_CLOCKS_TO_BLUEPRINT, com.android.systemui.Flags.FLAG_NEW_AOD_TRANSITION, com.android.systemui.Flags.FLAG_NEW_TOUCHPAD_GESTURES_TUTORIAL, com.android.systemui.Flags.FLAG_NEW_VOLUME_PANEL, com.android.systemui.Flags.FLAG_NOTIFICATION_ASYNC_GROUP_HEADER_INFLATION, com.android.systemui.Flags.FLAG_NOTIFICATION_ASYNC_HYBRID_VIEW_INFLATION, com.android.systemui.Flags.FLAG_NOTIFICATION_AVALANCHE_SUPPRESSION, com.android.systemui.Flags.FLAG_NOTIFICATION_AVALANCHE_THROTTLE_HUN, com.android.systemui.Flags.FLAG_NOTIFICATION_BACKGROUND_TINT_OPTIMIZATION, com.android.systemui.Flags.FLAG_NOTIFICATION_COLOR_UPDATE_LOGGER, com.android.systemui.Flags.FLAG_NOTIFICATION_CONTENT_ALPHA_OPTIMIZATION, com.android.systemui.Flags.FLAG_NOTIFICATION_FOOTER_BACKGROUND_TINT_OPTIMIZATION, com.android.systemui.Flags.FLAG_NOTIFICATION_MEDIA_MANAGER_BACKGROUND_EXECUTION, com.android.systemui.Flags.FLAG_NOTIFICATION_MINIMALISM_PROTOTYPE, com.android.systemui.Flags.FLAG_NOTIFICATION_OVER_EXPANSION_CLIPPING_FIX, com.android.systemui.Flags.FLAG_NOTIFICATION_PULSING_FIX, com.android.systemui.Flags.FLAG_NOTIFICATION_ROW_CONTENT_BINDER_REFACTOR, com.android.systemui.Flags.FLAG_NOTIFICATION_ROW_USER_CONTEXT, com.android.systemui.Flags.FLAG_NOTIFICATION_VIEW_FLIPPER_PAUSING_V2, com.android.systemui.Flags.FLAG_NOTIFICATIONS_BACKGROUND_ICONS, com.android.systemui.Flags.FLAG_NOTIFICATIONS_FOOTER_VIEW_REFACTOR, com.android.systemui.Flags.FLAG_NOTIFICATIONS_HEADS_UP_REFACTOR, com.android.systemui.Flags.FLAG_NOTIFICATIONS_HIDE_ON_DISPLAY_SWITCH, com.android.systemui.Flags.FLAG_NOTIFICATIONS_ICON_CONTAINER_REFACTOR, com.android.systemui.Flags.FLAG_NOTIFICATIONS_IMPROVED_HUN_ANIMATION, com.android.systemui.Flags.FLAG_NOTIFICATIONS_LIVE_DATA_STORE_REFACTOR, com.android.systemui.Flags.FLAG_NOTIFY_POWER_MANAGER_USER_ACTIVITY_BACKGROUND, com.android.systemui.Flags.FLAG_PIN_INPUT_FIELD_STYLED_FOCUS_STATE, com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_ANIMATE_BOUNCER, com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_ANIMATE_DIALOGS, com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_ANIMATE_SHADE, com.android.systemui.Flags.FLAG_PREDICTIVE_BACK_SYSUI, com.android.systemui.Flags.FLAG_PRIORITY_PEOPLE_SECTION, com.android.systemui.Flags.FLAG_PRIVACY_DOT_UNFOLD_WRONG_CORNER_FIX, com.android.systemui.Flags.FLAG_PSS_APP_SELECTOR_ABRUPT_EXIT_FIX, com.android.systemui.Flags.FLAG_PSS_APP_SELECTOR_RECENTS_SPLIT_SCREEN, com.android.systemui.Flags.FLAG_PSS_TASK_SWITCHER, com.android.systemui.Flags.FLAG_QS_CUSTOM_TILE_CLICK_GUARANTEED_BUG_FIX, com.android.systemui.Flags.FLAG_QS_NEW_PIPELINE, com.android.systemui.Flags.FLAG_QS_NEW_TILES, com.android.systemui.Flags.FLAG_QS_NEW_TILES_FUTURE, com.android.systemui.Flags.FLAG_QS_TILE_FOCUS_STATE, com.android.systemui.Flags.FLAG_QS_UI_REFACTOR, com.android.systemui.Flags.FLAG_QUICK_SETTINGS_VISUAL_HAPTICS_LONGPRESS, com.android.systemui.Flags.FLAG_RECORD_ISSUE_QS_TILE, com.android.systemui.Flags.FLAG_REFACTOR_GET_CURRENT_USER, com.android.systemui.Flags.FLAG_REGISTER_BATTERY_CONTROLLER_RECEIVERS_IN_CORESTARTABLE, com.android.systemui.Flags.FLAG_REGISTER_NEW_WALLET_CARD_IN_BACKGROUND, com.android.systemui.Flags.FLAG_REGISTER_WALLPAPER_NOTIFIER_BACKGROUND, com.android.systemui.Flags.FLAG_REGISTER_ZEN_MODE_CONTENT_OBSERVER_BACKGROUND, com.android.systemui.Flags.FLAG_REMOVE_DREAM_OVERLAY_HIDE_ON_TOUCH, com.android.systemui.Flags.FLAG_REST_TO_UNLOCK, com.android.systemui.Flags.FLAG_RESTART_DREAM_ON_UNOCCLUDE, com.android.systemui.Flags.FLAG_REVAMPED_BOUNCER_MESSAGES, com.android.systemui.Flags.FLAG_RUN_FINGERPRINT_DETECT_ON_DISMISSIBLE_KEYGUARD, com.android.systemui.Flags.FLAG_SAVE_AND_RESTORE_MAGNIFICATION_SETTINGS_BUTTONS, com.android.systemui.Flags.FLAG_SCENE_CONTAINER, com.android.systemui.Flags.FLAG_SCREENSHARE_NOTIFICATION_HIDING_BUG_FIX, com.android.systemui.Flags.FLAG_SCREENSHOT_ACTION_DISMISS_SYSTEM_WINDOWS, com.android.systemui.Flags.FLAG_SCREENSHOT_PRIVATE_PROFILE_ACCESSIBILITY_ANNOUNCEMENT_FIX, com.android.systemui.Flags.FLAG_SCREENSHOT_PRIVATE_PROFILE_BEHAVIOR_FIX, com.android.systemui.Flags.FLAG_SCREENSHOT_SCROLL_CROP_VIEW_CRASH_FIX, com.android.systemui.Flags.FLAG_SCREENSHOT_SHELF_UI2, com.android.systemui.Flags.FLAG_SHADE_COLLAPSE_ACTIVITY_LAUNCH_FIX, com.android.systemui.Flags.FLAG_SHADERLIB_LOADING_EFFECT_REFACTOR, com.android.systemui.Flags.FLAG_SLICE_BROADCAST_RELAY_IN_BACKGROUND, com.android.systemui.Flags.FLAG_SLICE_MANAGER_BINDER_CALL_BACKGROUND, com.android.systemui.Flags.FLAG_SMARTSPACE_LOCKSCREEN_VIEWMODEL, com.android.systemui.Flags.FLAG_SMARTSPACE_RELOCATE_TO_BOTTOM, com.android.systemui.Flags.FLAG_SMARTSPACE_REMOTEVIEWS_RENDERING, com.android.systemui.Flags.FLAG_STATUS_BAR_MONOCHROME_ICONS_FIX, com.android.systemui.Flags.FLAG_STATUS_BAR_SCREEN_SHARING_CHIPS, com.android.systemui.Flags.FLAG_STATUS_BAR_STATIC_INOUT_INDICATORS, com.android.systemui.Flags.FLAG_SWITCH_USER_ON_BG, com.android.systemui.Flags.FLAG_SYSUI_TEAMFOOD, com.android.systemui.Flags.FLAG_THEME_OVERLAY_CONTROLLER_WAKEFULNESS_DEPRECATION, com.android.systemui.Flags.FLAG_THREE_BUTTON_CORNER_SWIPE, com.android.systemui.Flags.FLAG_TRUNCATED_STATUS_BAR_ICONS_FIX, com.android.systemui.Flags.FLAG_UDFPS_VIEW_PERFORMANCE, com.android.systemui.Flags.FLAG_UNFOLD_ANIMATION_BACKGROUND_PROGRESS, com.android.systemui.Flags.FLAG_UPDATE_USER_SWITCHER_BACKGROUND, com.android.systemui.Flags.FLAG_VALIDATE_KEYBOARD_SHORTCUT_HELPER_ICON_URI, com.android.systemui.Flags.FLAG_VISUAL_INTERRUPTIONS_REFACTOR);
    }
}
