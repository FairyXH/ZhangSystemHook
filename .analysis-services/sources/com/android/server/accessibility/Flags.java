package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public final class Flags {
    private static com.android.server.accessibility.FeatureFlags FEATURE_FLAGS = new com.android.server.accessibility.FeatureFlagsImpl();
    public static final java.lang.String FLAG_ADD_WINDOW_TOKEN_WITHOUT_LOCK = "com.android.server.accessibility.add_window_token_without_lock";
    public static final java.lang.String FLAG_CLEANUP_A11Y_OVERLAYS = "com.android.server.accessibility.cleanup_a11y_overlays";
    public static final java.lang.String FLAG_CLEAR_DEFAULT_FROM_A11Y_SHORTCUT_TARGET_SERVICE_RESTORE = "com.android.server.accessibility.clear_default_from_a11y_shortcut_target_service_restore";
    public static final java.lang.String FLAG_COMPUTE_WINDOW_CHANGES_ON_A11Y_V2 = "com.android.server.accessibility.compute_window_changes_on_a11y_v2";
    public static final java.lang.String FLAG_DEPRECATE_PACKAGE_LIST_OBSERVER = "com.android.server.accessibility.deprecate_package_list_observer";
    public static final java.lang.String FLAG_DISABLE_CONTINUOUS_SHORTCUT_ON_FORCE_STOP = "com.android.server.accessibility.disable_continuous_shortcut_on_force_stop";
    public static final java.lang.String FLAG_DO_NOT_RESET_KEY_EVENT_STATE = "com.android.server.accessibility.do_not_reset_key_event_state";
    public static final java.lang.String FLAG_ENABLE_A11Y_CHECKER_LOGGING = "com.android.server.accessibility.enable_a11y_checker_logging";
    public static final java.lang.String FLAG_ENABLE_COLOR_CORRECTION_SATURATION = "com.android.server.accessibility.enable_color_correction_saturation";
    public static final java.lang.String FLAG_ENABLE_HARDWARE_SHORTCUT_DISABLES_WARNING = "com.android.server.accessibility.enable_hardware_shortcut_disables_warning";
    public static final java.lang.String FLAG_ENABLE_MAGNIFICATION_JOYSTICK = "com.android.server.accessibility.enable_magnification_joystick";
    public static final java.lang.String FLAG_ENABLE_MAGNIFICATION_MULTIPLE_FINGER_MULTIPLE_TAP_GESTURE = "com.android.server.accessibility.enable_magnification_multiple_finger_multiple_tap_gesture";
    public static final java.lang.String FLAG_ENABLE_MAGNIFICATION_ONE_FINGER_PANNING_GESTURE = "com.android.server.accessibility.enable_magnification_one_finger_panning_gesture";
    public static final java.lang.String FLAG_FIX_DRAG_POINTER_WHEN_ENDING_DRAG = "com.android.server.accessibility.fix_drag_pointer_when_ending_drag";
    public static final java.lang.String FLAG_FOCUS_CLICK_POINT_WINDOW_BOUNDS_FROM_A11Y_WINDOW_INFO = "com.android.server.accessibility.focus_click_point_window_bounds_from_a11y_window_info";
    public static final java.lang.String FLAG_FULLSCREEN_FLING_GESTURE = "com.android.server.accessibility.fullscreen_fling_gesture";
    public static final java.lang.String FLAG_HANDLE_MULTI_DEVICE_INPUT = "com.android.server.accessibility.handle_multi_device_input";
    public static final java.lang.String FLAG_MANAGER_AVOID_RECEIVER_TIMEOUT = "com.android.server.accessibility.manager_avoid_receiver_timeout";
    public static final java.lang.String FLAG_MANAGER_PACKAGE_MONITOR_LOGIC_FIX = "com.android.server.accessibility.manager_package_monitor_logic_fix";
    public static final java.lang.String FLAG_PINCH_ZOOM_ZERO_MIN_SPAN = "com.android.server.accessibility.pinch_zoom_zero_min_span";
    public static final java.lang.String FLAG_PROXY_USE_APPS_ON_VIRTUAL_DEVICE_LISTENER = "com.android.server.accessibility.proxy_use_apps_on_virtual_device_listener";
    public static final java.lang.String FLAG_REMOVE_ON_WINDOW_INFOS_CHANGED_HANDLER = "com.android.server.accessibility.remove_on_window_infos_changed_handler";
    public static final java.lang.String FLAG_RESETTABLE_DYNAMIC_PROPERTIES = "com.android.server.accessibility.resettable_dynamic_properties";
    public static final java.lang.String FLAG_RESET_HOVER_EVENT_TIMER_ON_ACTION_UP = "com.android.server.accessibility.reset_hover_event_timer_on_action_up";
    public static final java.lang.String FLAG_SCAN_PACKAGES_WITHOUT_LOCK = "com.android.server.accessibility.scan_packages_without_lock";
    public static final java.lang.String FLAG_SEND_A11Y_EVENTS_BASED_ON_STATE = "com.android.server.accessibility.send_a11y_events_based_on_state";
    public static final java.lang.String FLAG_SEND_HOVER_EVENTS_BASED_ON_EVENT_STREAM = "com.android.server.accessibility.send_hover_events_based_on_event_stream";
    public static final java.lang.String FLAG_SKIP_PACKAGE_CHANGE_BEFORE_USER_SWITCH = "com.android.server.accessibility.skip_package_change_before_user_switch";

    public static boolean addWindowTokenWithoutLock() {
        return FEATURE_FLAGS.addWindowTokenWithoutLock();
    }

    public static boolean cleanupA11yOverlays() {
        return FEATURE_FLAGS.cleanupA11yOverlays();
    }

    public static boolean clearDefaultFromA11yShortcutTargetServiceRestore() {
        return FEATURE_FLAGS.clearDefaultFromA11yShortcutTargetServiceRestore();
    }

    public static boolean computeWindowChangesOnA11yV2() {
        return FEATURE_FLAGS.computeWindowChangesOnA11yV2();
    }

    public static boolean deprecatePackageListObserver() {
        return FEATURE_FLAGS.deprecatePackageListObserver();
    }

    public static boolean disableContinuousShortcutOnForceStop() {
        return FEATURE_FLAGS.disableContinuousShortcutOnForceStop();
    }

    public static boolean doNotResetKeyEventState() {
        return FEATURE_FLAGS.doNotResetKeyEventState();
    }

    public static boolean enableA11yCheckerLogging() {
        return FEATURE_FLAGS.enableA11yCheckerLogging();
    }

    public static boolean enableColorCorrectionSaturation() {
        return FEATURE_FLAGS.enableColorCorrectionSaturation();
    }

    public static boolean enableHardwareShortcutDisablesWarning() {
        return FEATURE_FLAGS.enableHardwareShortcutDisablesWarning();
    }

    public static boolean enableMagnificationJoystick() {
        return FEATURE_FLAGS.enableMagnificationJoystick();
    }

    public static boolean enableMagnificationMultipleFingerMultipleTapGesture() {
        return FEATURE_FLAGS.enableMagnificationMultipleFingerMultipleTapGesture();
    }

    public static boolean enableMagnificationOneFingerPanningGesture() {
        return FEATURE_FLAGS.enableMagnificationOneFingerPanningGesture();
    }

    public static boolean fixDragPointerWhenEndingDrag() {
        return FEATURE_FLAGS.fixDragPointerWhenEndingDrag();
    }

    public static boolean focusClickPointWindowBoundsFromA11yWindowInfo() {
        return FEATURE_FLAGS.focusClickPointWindowBoundsFromA11yWindowInfo();
    }

    public static boolean fullscreenFlingGesture() {
        return FEATURE_FLAGS.fullscreenFlingGesture();
    }

    public static boolean handleMultiDeviceInput() {
        return FEATURE_FLAGS.handleMultiDeviceInput();
    }

    public static boolean managerAvoidReceiverTimeout() {
        return FEATURE_FLAGS.managerAvoidReceiverTimeout();
    }

    public static boolean managerPackageMonitorLogicFix() {
        return FEATURE_FLAGS.managerPackageMonitorLogicFix();
    }

    public static boolean pinchZoomZeroMinSpan() {
        return FEATURE_FLAGS.pinchZoomZeroMinSpan();
    }

    public static boolean proxyUseAppsOnVirtualDeviceListener() {
        return FEATURE_FLAGS.proxyUseAppsOnVirtualDeviceListener();
    }

    public static boolean removeOnWindowInfosChangedHandler() {
        return FEATURE_FLAGS.removeOnWindowInfosChangedHandler();
    }

    public static boolean resetHoverEventTimerOnActionUp() {
        return FEATURE_FLAGS.resetHoverEventTimerOnActionUp();
    }

    public static boolean resettableDynamicProperties() {
        return FEATURE_FLAGS.resettableDynamicProperties();
    }

    public static boolean scanPackagesWithoutLock() {
        return FEATURE_FLAGS.scanPackagesWithoutLock();
    }

    public static boolean sendA11yEventsBasedOnState() {
        return FEATURE_FLAGS.sendA11yEventsBasedOnState();
    }

    public static boolean sendHoverEventsBasedOnEventStream() {
        return FEATURE_FLAGS.sendHoverEventsBasedOnEventStream();
    }

    public static boolean skipPackageChangeBeforeUserSwitch() {
        return FEATURE_FLAGS.skipPackageChangeBeforeUserSwitch();
    }
}
