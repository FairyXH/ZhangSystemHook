package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class DeferredDisplayUpdater implements com.android.server.wm.DisplayUpdater {
    static final com.android.server.wm.utils.DisplayInfoOverrides.DisplayInfoFieldsUpdater DEFERRABLE_FIELDS = new com.android.server.wm.utils.DisplayInfoOverrides.DisplayInfoFieldsUpdater() { // from class: com.android.server.wm.DeferredDisplayUpdater$$ExternalSyntheticLambda0
        @Override // com.android.server.wm.utils.DisplayInfoOverrides.DisplayInfoFieldsUpdater
        public final void setFields(android.view.DisplayInfo displayInfo, android.view.DisplayInfo displayInfo2) {
            com.android.server.wm.DeferredDisplayUpdater.lambda$static$0(displayInfo, displayInfo2);
        }
    };
    static final int DIFF_EVERYTHING = -1;
    static final int DIFF_NONE = 0;
    static final int DIFF_NOT_WM_DEFERRABLE = 2;
    static final int DIFF_WM_DEFERRABLE = 1;
    private static final java.lang.String TAG = "DeferredDisplayUpdater";
    private static final java.lang.String TRACE_TAG_WAIT_FOR_TRANSITION = "Screen unblock: wait for transition";
    private static final int WAIT_FOR_TRANSITION_TIMEOUT = 1000;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private android.view.DisplayInfo mLastDisplayInfo;
    private android.view.DisplayInfo mLastWmDisplayInfo;
    private android.os.Message mScreenUnblocker;
    private boolean mShouldWaitForTransitionWhenScreenOn;
    private final android.view.DisplayInfo mNonOverrideDisplayInfo = new android.view.DisplayInfo();
    private final android.view.DisplayInfo mOutputDisplayInfo = new android.view.DisplayInfo();
    private final java.lang.Runnable mScreenUnblockTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.DeferredDisplayUpdater$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$1();
        }
    };
    private com.android.server.wm.IDeferredDisplayUpdaterExt mExt = (com.android.server.wm.IDeferredDisplayUpdaterExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDeferredDisplayUpdaterExt.class).base(this).create();
    private com.android.server.wm.IDeferredDisplayUpdaterWrapper mWrapper = new com.android.server.wm.DeferredDisplayUpdater.DeferredDisplayUpdaterWrapper();

    static /* synthetic */ void lambda$static$0(android.view.DisplayInfo out, android.view.DisplayInfo override) {
        out.uniqueId = override.uniqueId;
        out.address = override.address;
        com.android.server.wm.utils.DisplayInfoOverrides.WM_OVERRIDE_FIELDS.setFields(out, override);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        android.util.Slog.e(TAG, "Timeout waiting for the display switch transition to start");
        continueScreenUnblocking();
    }

    public DeferredDisplayUpdater(com.android.server.wm.DisplayContent displayContent) {
        this.mDisplayContent = displayContent;
        this.mNonOverrideDisplayInfo.copyFrom(this.mDisplayContent.getDisplayInfo());
    }

    @Override // com.android.server.wm.DisplayUpdater
    public void updateDisplayInfo(final java.lang.Runnable finishCallback) {
        final android.view.DisplayInfo displayInfo = getCurrentDisplayInfo();
        int displayInfoDiff = calculateDisplayInfoDiff(this.mLastDisplayInfo, displayInfo);
        boolean physicalDisplayUpdated = isPhysicalDisplayUpdated(this.mLastDisplayInfo, displayInfo);
        this.mLastDisplayInfo = displayInfo;
        if (displayInfoDiff == -1 || !this.mDisplayContent.getLastHasContent() || this.mExt.applyDisplayInfoChangeImmediately(displayInfo, displayInfoDiff, physicalDisplayUpdated) || !this.mDisplayContent.mTransitionController.isShellTransitionsEnabled()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -8058211784911995417L, 0, null, null);
            }
            this.mLastWmDisplayInfo = displayInfo;
            applyLatestDisplayInfo();
            finishCallback.run();
            return;
        }
        if ((displayInfoDiff & 2) > 0) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1944392458089872195L, 0, null, null);
            }
            applyLatestDisplayInfo();
        }
        if ((displayInfoDiff & 1) > 0) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 8391643185322408089L, 0, null, null);
            }
            requestDisplayChangeTransition(physicalDisplayUpdated, new java.lang.Runnable() { // from class: com.android.server.wm.DeferredDisplayUpdater$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateDisplayInfo$2(displayInfo, finishCallback);
                }
            });
            return;
        }
        finishCallback.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDisplayInfo$2(android.view.DisplayInfo displayInfo, java.lang.Runnable finishCallback) {
        this.mLastWmDisplayInfo = displayInfo;
        applyLatestDisplayInfo();
        finishCallback.run();
    }

    private void requestDisplayChangeTransition(final boolean physicalDisplayUpdated, final java.lang.Runnable onStartCollect) {
        final com.android.server.wm.Transition transition = new com.android.server.wm.Transition(6, 0, this.mDisplayContent.mTransitionController, this.mDisplayContent.mTransitionController.mSyncEngine);
        this.mDisplayContent.mAtmService.startPowerMode(2);
        this.mExt.requestDisplayChangeTransition(transition, physicalDisplayUpdated);
        this.mDisplayContent.mTransitionController.startCollectOrQueue(transition, new com.android.server.wm.TransitionController.OnStartCollect() { // from class: com.android.server.wm.DeferredDisplayUpdater$$ExternalSyntheticLambda1
            @Override // com.android.server.wm.TransitionController.OnStartCollect
            public final void onCollectStarted(boolean z) {
                this.f$0.lambda$requestDisplayChangeTransition$3(physicalDisplayUpdated, onStartCollect, transition, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestDisplayChangeTransition$3(boolean physicalDisplayUpdated, java.lang.Runnable onStartCollect, com.android.server.wm.Transition transition, boolean deferred) {
        com.android.server.wm.WindowState notificationShade;
        android.graphics.Rect startBounds = new android.graphics.Rect(0, 0, this.mDisplayContent.mInitialDisplayWidth, this.mDisplayContent.mInitialDisplayHeight);
        int fromRotation = this.mDisplayContent.getRotation();
        if (com.android.window.flags.Flags.blastSyncNotificationShadeOnDisplaySwitch() && physicalDisplayUpdated && (notificationShade = this.mDisplayContent.getDisplayPolicy().getNotificationShade()) != null && notificationShade.isVisible() && this.mDisplayContent.mAtmService.mKeyguardController.isKeyguardOrAodShowing(this.mDisplayContent.mDisplayId)) {
            android.util.Slog.i(TAG, notificationShade + " uses blast for display switch");
            notificationShade.mSyncMethodOverride = 1;
        }
        this.mDisplayContent.mAtmService.deferWindowLayout();
        try {
            onStartCollect.run();
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -915675022936690176L, 0, null, null);
            }
            if (physicalDisplayUpdated) {
                onDisplayUpdated(transition, fromRotation, startBounds);
            } else {
                android.window.TransitionRequestInfo.DisplayChange displayChange = getCurrentDisplayChange(fromRotation, startBounds);
                this.mDisplayContent.mTransitionController.requestStartTransition(transition, null, null, displayChange);
            }
        } finally {
            this.mDisplayContent.mAtmService.continueWindowLayout();
        }
    }

    private void applyLatestDisplayInfo() {
        com.android.server.wm.utils.DisplayInfoOverrides.copyDisplayInfoFields(this.mOutputDisplayInfo, this.mLastDisplayInfo, this.mLastWmDisplayInfo, DEFERRABLE_FIELDS);
        this.mDisplayContent.onDisplayInfoUpdated(this.mOutputDisplayInfo);
    }

    private android.view.DisplayInfo getCurrentDisplayInfo() {
        this.mDisplayContent.mWmService.mDisplayManagerInternal.getNonOverrideDisplayInfo(this.mDisplayContent.mDisplayId, this.mNonOverrideDisplayInfo);
        return new android.view.DisplayInfo(this.mNonOverrideDisplayInfo);
    }

    private android.window.TransitionRequestInfo.DisplayChange getCurrentDisplayChange(int fromRotation, android.graphics.Rect startBounds) {
        android.graphics.Rect endBounds = new android.graphics.Rect(0, 0, this.mDisplayContent.mInitialDisplayWidth, this.mDisplayContent.mInitialDisplayHeight);
        int toRotation = this.mDisplayContent.getRotation();
        android.window.TransitionRequestInfo.DisplayChange displayChange = new android.window.TransitionRequestInfo.DisplayChange(this.mDisplayContent.getDisplayId());
        displayChange.setStartAbsBounds(startBounds);
        displayChange.setEndAbsBounds(endBounds);
        displayChange.setStartRotation(fromRotation);
        displayChange.setEndRotation(toRotation);
        return displayChange;
    }

    private void onDisplayUpdated(final com.android.server.wm.Transition transition, int fromRotation, android.graphics.Rect startBounds) {
        int toRotation = this.mDisplayContent.getRotation();
        this.mExt.onDisplayUpdated(transition);
        android.window.TransitionRequestInfo.DisplayChange displayChange = getCurrentDisplayChange(fromRotation, startBounds);
        displayChange.setPhysicalDisplayChanged(true);
        transition.addTransactionCompletedListener(new java.lang.Runnable() { // from class: com.android.server.wm.DeferredDisplayUpdater$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.continueScreenUnblocking();
            }
        });
        this.mDisplayContent.mTransitionController.requestStartTransition(transition, null, null, displayChange);
        android.window.DisplayAreaInfo newDisplayAreaInfo = this.mDisplayContent.getDisplayAreaInfo();
        boolean startedRemoteChange = this.mDisplayContent.mRemoteDisplayChangeController.performRemoteDisplayChange(fromRotation, toRotation, newDisplayAreaInfo, new com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback() { // from class: com.android.server.wm.DeferredDisplayUpdater$$ExternalSyntheticLambda5
            @Override // com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback
            public final void onContinueRemoteDisplayChange(android.window.WindowContainerTransaction windowContainerTransaction) {
                this.f$0.lambda$onDisplayUpdated$4(transition, windowContainerTransaction);
            }
        });
        if (!startedRemoteChange) {
            lambda$onDisplayUpdated$4(null, transition);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: finishDisplayUpdate, reason: merged with bridge method [inline-methods] */
    public void lambda$onDisplayUpdated$4(android.window.WindowContainerTransaction wct, com.android.server.wm.Transition transition) {
        if (wct != null) {
            this.mDisplayContent.mAtmService.mWindowOrganizerController.applyTransaction(wct);
        }
        transition.setAllReady();
    }

    private boolean isPhysicalDisplayUpdated(android.view.DisplayInfo first, android.view.DisplayInfo second) {
        if (first == null || second == null) {
            return true;
        }
        return true ^ java.util.Objects.equals(first.uniqueId, second.uniqueId);
    }

    @Override // com.android.server.wm.DisplayUpdater
    public void onDisplayContentDisplayPropertiesPostChanged(int previousRotation, int newRotation, android.window.DisplayAreaInfo newDisplayAreaInfo) {
        if (this.mScreenUnblocker != null && !this.mDisplayContent.mTransitionController.inTransition()) {
            this.mScreenUnblocker.sendToTarget();
            this.mScreenUnblocker = null;
        }
    }

    @Override // com.android.server.wm.DisplayUpdater
    public void onDisplaySwitching(boolean switching) {
        if (this.mExt.skipWaitForTransition(switching)) {
            return;
        }
        this.mShouldWaitForTransitionWhenScreenOn = switching;
    }

    @Override // com.android.server.wm.DisplayUpdater
    public boolean waitForTransition(android.os.Message screenUnblocker) {
        if (!com.android.window.flags.Flags.waitForTransitionOnDisplaySwitch() || !this.mShouldWaitForTransitionWhenScreenOn) {
            return false;
        }
        this.mScreenUnblocker = screenUnblocker;
        if (android.os.Trace.isTagEnabled(32L)) {
            android.os.Trace.beginAsyncSection(TRACE_TAG_WAIT_FOR_TRANSITION, screenUnblocker.hashCode());
        }
        this.mDisplayContent.mWmService.mH.removeCallbacks(this.mScreenUnblockTimeoutRunnable);
        this.mDisplayContent.mWmService.mH.postDelayed(this.mScreenUnblockTimeoutRunnable, 1000L);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void continueScreenUnblocking() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mDisplayContent.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mShouldWaitForTransitionWhenScreenOn = false;
                this.mDisplayContent.mWmService.mH.removeCallbacks(this.mScreenUnblockTimeoutRunnable);
                if (this.mScreenUnblocker == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                this.mScreenUnblocker.sendToTarget();
                if (android.os.Trace.isTagEnabled(32L)) {
                    android.os.Trace.endAsyncSection(TRACE_TAG_WAIT_FOR_TRANSITION, this.mScreenUnblocker.hashCode());
                }
                this.mScreenUnblocker = null;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    static int calculateDisplayInfoDiff(android.view.DisplayInfo first, android.view.DisplayInfo second) {
        int diff = 0;
        if (java.util.Objects.equals(first, second)) {
            return 0;
        }
        if (first == null || second == null) {
            return -1;
        }
        if (first.layerStack != second.layerStack || first.flags != second.flags || first.type != second.type || first.displayId != second.displayId || first.displayGroupId != second.displayGroupId || !java.util.Objects.equals(first.deviceProductInfo, second.deviceProductInfo) || first.modeId != second.modeId || first.renderFrameRate != second.renderFrameRate || first.defaultModeId != second.defaultModeId || first.userPreferredModeId != second.userPreferredModeId || !java.util.Arrays.equals(first.supportedModes, second.supportedModes) || !java.util.Arrays.equals(first.appsSupportedModes, second.appsSupportedModes) || first.colorMode != second.colorMode || !java.util.Arrays.equals(first.supportedColorModes, second.supportedColorModes) || !java.util.Objects.equals(first.hdrCapabilities, second.hdrCapabilities) || !java.util.Arrays.equals(first.userDisabledHdrTypes, second.userDisabledHdrTypes) || first.minimalPostProcessingSupported != second.minimalPostProcessingSupported || first.appVsyncOffsetNanos != second.appVsyncOffsetNanos || first.presentationDeadlineNanos != second.presentationDeadlineNanos || first.state != second.state || first.committedState != second.committedState || first.ownerUid != second.ownerUid || !java.util.Objects.equals(first.ownerPackageName, second.ownerPackageName) || first.removeMode != second.removeMode || first.getRefreshRate() != second.getRefreshRate() || first.brightnessMinimum != second.brightnessMinimum || first.brightnessMaximum != second.brightnessMaximum || first.brightnessDefault != second.brightnessDefault || first.installOrientation != second.installOrientation || !java.util.Objects.equals(first.layoutLimitedRefreshRate, second.layoutLimitedRefreshRate) || !com.android.internal.display.BrightnessSynchronizer.floatEquals(first.hdrSdrRatio, second.hdrSdrRatio) || !first.thermalRefreshRateThrottling.contentEquals(second.thermalRefreshRateThrottling) || !java.util.Objects.equals(first.thermalBrightnessThrottlingDataId, second.thermalBrightnessThrottlingDataId)) {
            diff = 0 | 2;
        }
        if (first.appWidth != second.appWidth || first.appHeight != second.appHeight || first.smallestNominalAppWidth != second.smallestNominalAppWidth || first.smallestNominalAppHeight != second.smallestNominalAppHeight || first.largestNominalAppWidth != second.largestNominalAppWidth || first.largestNominalAppHeight != second.largestNominalAppHeight || first.logicalWidth != second.logicalWidth || first.logicalHeight != second.logicalHeight || first.physicalXDpi != second.physicalXDpi || first.physicalYDpi != second.physicalYDpi || first.rotation != second.rotation || !java.util.Objects.equals(first.displayCutout, second.displayCutout) || first.logicalDensityDpi != second.logicalDensityDpi || !java.util.Objects.equals(first.roundedCorners, second.roundedCorners) || !java.util.Objects.equals(first.displayShape, second.displayShape) || !java.util.Objects.equals(first.uniqueId, second.uniqueId) || !java.util.Objects.equals(first.address, second.address)) {
            return diff | 1;
        }
        return diff;
    }

    public com.android.server.wm.IDeferredDisplayUpdaterWrapper getWrapper() {
        return this.mWrapper;
    }

    private class DeferredDisplayUpdaterWrapper implements com.android.server.wm.IDeferredDisplayUpdaterWrapper {
        private DeferredDisplayUpdaterWrapper() {
        }

        @Override // com.android.server.wm.IDeferredDisplayUpdaterWrapper
        public com.android.server.wm.IDeferredDisplayUpdaterExt getExtImpl() {
            return com.android.server.wm.DeferredDisplayUpdater.this.mExt;
        }

        @Override // com.android.server.wm.IDeferredDisplayUpdaterWrapper
        public com.android.server.wm.DisplayContent getDisplayContent() {
            return com.android.server.wm.DeferredDisplayUpdater.this.mDisplayContent;
        }

        @Override // com.android.server.wm.IDeferredDisplayUpdaterWrapper
        public android.view.DisplayInfo getLastDisplayInfo() {
            return com.android.server.wm.DeferredDisplayUpdater.this.mLastDisplayInfo;
        }
    }
}
