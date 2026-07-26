package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class CustomFeatureFlags implements com.android.server.accessibility.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.accessibility.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.accessibility.Flags.FLAG_ADD_WINDOW_TOKEN_WITHOUT_LOCK, com.android.server.accessibility.Flags.FLAG_CLEANUP_A11Y_OVERLAYS, com.android.server.accessibility.Flags.FLAG_CLEAR_DEFAULT_FROM_A11Y_SHORTCUT_TARGET_SERVICE_RESTORE, com.android.server.accessibility.Flags.FLAG_COMPUTE_WINDOW_CHANGES_ON_A11Y_V2, com.android.server.accessibility.Flags.FLAG_DEPRECATE_PACKAGE_LIST_OBSERVER, com.android.server.accessibility.Flags.FLAG_DISABLE_CONTINUOUS_SHORTCUT_ON_FORCE_STOP, com.android.server.accessibility.Flags.FLAG_DO_NOT_RESET_KEY_EVENT_STATE, com.android.server.accessibility.Flags.FLAG_ENABLE_A11Y_CHECKER_LOGGING, com.android.server.accessibility.Flags.FLAG_ENABLE_COLOR_CORRECTION_SATURATION, com.android.server.accessibility.Flags.FLAG_ENABLE_HARDWARE_SHORTCUT_DISABLES_WARNING, com.android.server.accessibility.Flags.FLAG_ENABLE_MAGNIFICATION_JOYSTICK, com.android.server.accessibility.Flags.FLAG_ENABLE_MAGNIFICATION_MULTIPLE_FINGER_MULTIPLE_TAP_GESTURE, com.android.server.accessibility.Flags.FLAG_ENABLE_MAGNIFICATION_ONE_FINGER_PANNING_GESTURE, com.android.server.accessibility.Flags.FLAG_FIX_DRAG_POINTER_WHEN_ENDING_DRAG, com.android.server.accessibility.Flags.FLAG_FOCUS_CLICK_POINT_WINDOW_BOUNDS_FROM_A11Y_WINDOW_INFO, com.android.server.accessibility.Flags.FLAG_FULLSCREEN_FLING_GESTURE, com.android.server.accessibility.Flags.FLAG_HANDLE_MULTI_DEVICE_INPUT, com.android.server.accessibility.Flags.FLAG_MANAGER_AVOID_RECEIVER_TIMEOUT, com.android.server.accessibility.Flags.FLAG_MANAGER_PACKAGE_MONITOR_LOGIC_FIX, com.android.server.accessibility.Flags.FLAG_PINCH_ZOOM_ZERO_MIN_SPAN, com.android.server.accessibility.Flags.FLAG_PROXY_USE_APPS_ON_VIRTUAL_DEVICE_LISTENER, com.android.server.accessibility.Flags.FLAG_REMOVE_ON_WINDOW_INFOS_CHANGED_HANDLER, com.android.server.accessibility.Flags.FLAG_RESET_HOVER_EVENT_TIMER_ON_ACTION_UP, com.android.server.accessibility.Flags.FLAG_RESETTABLE_DYNAMIC_PROPERTIES, com.android.server.accessibility.Flags.FLAG_SCAN_PACKAGES_WITHOUT_LOCK, com.android.server.accessibility.Flags.FLAG_SEND_A11Y_EVENTS_BASED_ON_STATE, com.android.server.accessibility.Flags.FLAG_SEND_HOVER_EVENTS_BASED_ON_EVENT_STREAM, com.android.server.accessibility.Flags.FLAG_SKIP_PACKAGE_CHANGE_BEFORE_USER_SWITCH, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.accessibility.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean addWindowTokenWithoutLock() {
        return getValue(com.android.server.accessibility.Flags.FLAG_ADD_WINDOW_TOKEN_WITHOUT_LOCK, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).addWindowTokenWithoutLock();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean cleanupA11yOverlays() {
        return getValue(com.android.server.accessibility.Flags.FLAG_CLEANUP_A11Y_OVERLAYS, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).cleanupA11yOverlays();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean clearDefaultFromA11yShortcutTargetServiceRestore() {
        return getValue(com.android.server.accessibility.Flags.FLAG_CLEAR_DEFAULT_FROM_A11Y_SHORTCUT_TARGET_SERVICE_RESTORE, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).clearDefaultFromA11yShortcutTargetServiceRestore();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean computeWindowChangesOnA11yV2() {
        return getValue(com.android.server.accessibility.Flags.FLAG_COMPUTE_WINDOW_CHANGES_ON_A11Y_V2, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).computeWindowChangesOnA11yV2();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean deprecatePackageListObserver() {
        return getValue(com.android.server.accessibility.Flags.FLAG_DEPRECATE_PACKAGE_LIST_OBSERVER, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda21
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).deprecatePackageListObserver();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean disableContinuousShortcutOnForceStop() {
        return getValue(com.android.server.accessibility.Flags.FLAG_DISABLE_CONTINUOUS_SHORTCUT_ON_FORCE_STOP, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).disableContinuousShortcutOnForceStop();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean doNotResetKeyEventState() {
        return getValue(com.android.server.accessibility.Flags.FLAG_DO_NOT_RESET_KEY_EVENT_STATE, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda22
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).doNotResetKeyEventState();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableA11yCheckerLogging() {
        return getValue(com.android.server.accessibility.Flags.FLAG_ENABLE_A11Y_CHECKER_LOGGING, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda26
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).enableA11yCheckerLogging();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableColorCorrectionSaturation() {
        return getValue(com.android.server.accessibility.Flags.FLAG_ENABLE_COLOR_CORRECTION_SATURATION, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).enableColorCorrectionSaturation();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableHardwareShortcutDisablesWarning() {
        return getValue(com.android.server.accessibility.Flags.FLAG_ENABLE_HARDWARE_SHORTCUT_DISABLES_WARNING, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda25
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).enableHardwareShortcutDisablesWarning();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableMagnificationJoystick() {
        return getValue(com.android.server.accessibility.Flags.FLAG_ENABLE_MAGNIFICATION_JOYSTICK, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).enableMagnificationJoystick();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableMagnificationMultipleFingerMultipleTapGesture() {
        return getValue(com.android.server.accessibility.Flags.FLAG_ENABLE_MAGNIFICATION_MULTIPLE_FINGER_MULTIPLE_TAP_GESTURE, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).enableMagnificationMultipleFingerMultipleTapGesture();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableMagnificationOneFingerPanningGesture() {
        return getValue(com.android.server.accessibility.Flags.FLAG_ENABLE_MAGNIFICATION_ONE_FINGER_PANNING_GESTURE, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).enableMagnificationOneFingerPanningGesture();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean fixDragPointerWhenEndingDrag() {
        return getValue(com.android.server.accessibility.Flags.FLAG_FIX_DRAG_POINTER_WHEN_ENDING_DRAG, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda19
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).fixDragPointerWhenEndingDrag();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean focusClickPointWindowBoundsFromA11yWindowInfo() {
        return getValue(com.android.server.accessibility.Flags.FLAG_FOCUS_CLICK_POINT_WINDOW_BOUNDS_FROM_A11Y_WINDOW_INFO, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda16
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).focusClickPointWindowBoundsFromA11yWindowInfo();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean fullscreenFlingGesture() {
        return getValue(com.android.server.accessibility.Flags.FLAG_FULLSCREEN_FLING_GESTURE, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).fullscreenFlingGesture();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean handleMultiDeviceInput() {
        return getValue(com.android.server.accessibility.Flags.FLAG_HANDLE_MULTI_DEVICE_INPUT, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda27
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).handleMultiDeviceInput();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean managerAvoidReceiverTimeout() {
        return getValue(com.android.server.accessibility.Flags.FLAG_MANAGER_AVOID_RECEIVER_TIMEOUT, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).managerAvoidReceiverTimeout();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean managerPackageMonitorLogicFix() {
        return getValue(com.android.server.accessibility.Flags.FLAG_MANAGER_PACKAGE_MONITOR_LOGIC_FIX, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda20
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).managerPackageMonitorLogicFix();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean pinchZoomZeroMinSpan() {
        return getValue(com.android.server.accessibility.Flags.FLAG_PINCH_ZOOM_ZERO_MIN_SPAN, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).pinchZoomZeroMinSpan();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean proxyUseAppsOnVirtualDeviceListener() {
        return getValue(com.android.server.accessibility.Flags.FLAG_PROXY_USE_APPS_ON_VIRTUAL_DEVICE_LISTENER, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda24
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).proxyUseAppsOnVirtualDeviceListener();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean removeOnWindowInfosChangedHandler() {
        return getValue(com.android.server.accessibility.Flags.FLAG_REMOVE_ON_WINDOW_INFOS_CHANGED_HANDLER, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).removeOnWindowInfosChangedHandler();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean resetHoverEventTimerOnActionUp() {
        return getValue(com.android.server.accessibility.Flags.FLAG_RESET_HOVER_EVENT_TIMER_ON_ACTION_UP, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).resetHoverEventTimerOnActionUp();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean resettableDynamicProperties() {
        return getValue(com.android.server.accessibility.Flags.FLAG_RESETTABLE_DYNAMIC_PROPERTIES, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).resettableDynamicProperties();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean scanPackagesWithoutLock() {
        return getValue(com.android.server.accessibility.Flags.FLAG_SCAN_PACKAGES_WITHOUT_LOCK, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).scanPackagesWithoutLock();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean sendA11yEventsBasedOnState() {
        return getValue(com.android.server.accessibility.Flags.FLAG_SEND_A11Y_EVENTS_BASED_ON_STATE, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).sendA11yEventsBasedOnState();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean sendHoverEventsBasedOnEventStream() {
        return getValue(com.android.server.accessibility.Flags.FLAG_SEND_HOVER_EVENTS_BASED_ON_EVENT_STREAM, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).sendHoverEventsBasedOnEventStream();
            }
        });
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean skipPackageChangeBeforeUserSwitch() {
        return getValue(com.android.server.accessibility.Flags.FLAG_SKIP_PACKAGE_CHANGE_BEFORE_USER_SWITCH, new java.util.function.Predicate() { // from class: com.android.server.accessibility.CustomFeatureFlags$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.accessibility.FeatureFlags) obj).skipPackageChangeBeforeUserSwitch();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.accessibility.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.accessibility.Flags.FLAG_ADD_WINDOW_TOKEN_WITHOUT_LOCK, com.android.server.accessibility.Flags.FLAG_CLEANUP_A11Y_OVERLAYS, com.android.server.accessibility.Flags.FLAG_CLEAR_DEFAULT_FROM_A11Y_SHORTCUT_TARGET_SERVICE_RESTORE, com.android.server.accessibility.Flags.FLAG_COMPUTE_WINDOW_CHANGES_ON_A11Y_V2, com.android.server.accessibility.Flags.FLAG_DEPRECATE_PACKAGE_LIST_OBSERVER, com.android.server.accessibility.Flags.FLAG_DISABLE_CONTINUOUS_SHORTCUT_ON_FORCE_STOP, com.android.server.accessibility.Flags.FLAG_DO_NOT_RESET_KEY_EVENT_STATE, com.android.server.accessibility.Flags.FLAG_ENABLE_A11Y_CHECKER_LOGGING, com.android.server.accessibility.Flags.FLAG_ENABLE_COLOR_CORRECTION_SATURATION, com.android.server.accessibility.Flags.FLAG_ENABLE_HARDWARE_SHORTCUT_DISABLES_WARNING, com.android.server.accessibility.Flags.FLAG_ENABLE_MAGNIFICATION_JOYSTICK, com.android.server.accessibility.Flags.FLAG_ENABLE_MAGNIFICATION_MULTIPLE_FINGER_MULTIPLE_TAP_GESTURE, com.android.server.accessibility.Flags.FLAG_ENABLE_MAGNIFICATION_ONE_FINGER_PANNING_GESTURE, com.android.server.accessibility.Flags.FLAG_FIX_DRAG_POINTER_WHEN_ENDING_DRAG, com.android.server.accessibility.Flags.FLAG_FOCUS_CLICK_POINT_WINDOW_BOUNDS_FROM_A11Y_WINDOW_INFO, com.android.server.accessibility.Flags.FLAG_FULLSCREEN_FLING_GESTURE, com.android.server.accessibility.Flags.FLAG_HANDLE_MULTI_DEVICE_INPUT, com.android.server.accessibility.Flags.FLAG_MANAGER_AVOID_RECEIVER_TIMEOUT, com.android.server.accessibility.Flags.FLAG_MANAGER_PACKAGE_MONITOR_LOGIC_FIX, com.android.server.accessibility.Flags.FLAG_PINCH_ZOOM_ZERO_MIN_SPAN, com.android.server.accessibility.Flags.FLAG_PROXY_USE_APPS_ON_VIRTUAL_DEVICE_LISTENER, com.android.server.accessibility.Flags.FLAG_REMOVE_ON_WINDOW_INFOS_CHANGED_HANDLER, com.android.server.accessibility.Flags.FLAG_RESET_HOVER_EVENT_TIMER_ON_ACTION_UP, com.android.server.accessibility.Flags.FLAG_RESETTABLE_DYNAMIC_PROPERTIES, com.android.server.accessibility.Flags.FLAG_SCAN_PACKAGES_WITHOUT_LOCK, com.android.server.accessibility.Flags.FLAG_SEND_A11Y_EVENTS_BASED_ON_STATE, com.android.server.accessibility.Flags.FLAG_SEND_HOVER_EVENTS_BASED_ON_EVENT_STREAM, com.android.server.accessibility.Flags.FLAG_SKIP_PACKAGE_CHANGE_BEFORE_USER_SWITCH);
    }
}
