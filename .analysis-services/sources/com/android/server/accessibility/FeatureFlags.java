package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public interface FeatureFlags {
    boolean addWindowTokenWithoutLock();

    boolean cleanupA11yOverlays();

    boolean clearDefaultFromA11yShortcutTargetServiceRestore();

    boolean computeWindowChangesOnA11yV2();

    boolean deprecatePackageListObserver();

    boolean disableContinuousShortcutOnForceStop();

    boolean doNotResetKeyEventState();

    boolean enableA11yCheckerLogging();

    boolean enableColorCorrectionSaturation();

    boolean enableHardwareShortcutDisablesWarning();

    boolean enableMagnificationJoystick();

    boolean enableMagnificationMultipleFingerMultipleTapGesture();

    boolean enableMagnificationOneFingerPanningGesture();

    boolean fixDragPointerWhenEndingDrag();

    boolean focusClickPointWindowBoundsFromA11yWindowInfo();

    boolean fullscreenFlingGesture();

    boolean handleMultiDeviceInput();

    boolean managerAvoidReceiverTimeout();

    boolean managerPackageMonitorLogicFix();

    boolean pinchZoomZeroMinSpan();

    boolean proxyUseAppsOnVirtualDeviceListener();

    boolean removeOnWindowInfosChangedHandler();

    boolean resetHoverEventTimerOnActionUp();

    boolean resettableDynamicProperties();

    boolean scanPackagesWithoutLock();

    boolean sendA11yEventsBasedOnState();

    boolean sendHoverEventsBasedOnEventStream();

    boolean skipPackageChangeBeforeUserSwitch();
}
