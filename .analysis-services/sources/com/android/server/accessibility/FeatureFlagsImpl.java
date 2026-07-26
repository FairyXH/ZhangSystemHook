package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public final class FeatureFlagsImpl implements com.android.server.accessibility.FeatureFlags {
    @Override // com.android.server.accessibility.FeatureFlags
    public boolean addWindowTokenWithoutLock() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean cleanupA11yOverlays() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean clearDefaultFromA11yShortcutTargetServiceRestore() {
        return true;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean computeWindowChangesOnA11yV2() {
        return true;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean deprecatePackageListObserver() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean disableContinuousShortcutOnForceStop() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean doNotResetKeyEventState() {
        return true;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableA11yCheckerLogging() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableColorCorrectionSaturation() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableHardwareShortcutDisablesWarning() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableMagnificationJoystick() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableMagnificationMultipleFingerMultipleTapGesture() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean enableMagnificationOneFingerPanningGesture() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean fixDragPointerWhenEndingDrag() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean focusClickPointWindowBoundsFromA11yWindowInfo() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean fullscreenFlingGesture() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean handleMultiDeviceInput() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean managerAvoidReceiverTimeout() {
        return true;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean managerPackageMonitorLogicFix() {
        return true;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean pinchZoomZeroMinSpan() {
        return true;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean proxyUseAppsOnVirtualDeviceListener() {
        return true;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean removeOnWindowInfosChangedHandler() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean resetHoverEventTimerOnActionUp() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean resettableDynamicProperties() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean scanPackagesWithoutLock() {
        return true;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean sendA11yEventsBasedOnState() {
        return false;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean sendHoverEventsBasedOnEventStream() {
        return true;
    }

    @Override // com.android.server.accessibility.FeatureFlags
    public boolean skipPackageChangeBeforeUserSwitch() {
        return false;
    }
}
