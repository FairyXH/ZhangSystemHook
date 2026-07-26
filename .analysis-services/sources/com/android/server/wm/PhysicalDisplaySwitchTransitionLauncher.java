package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class PhysicalDisplaySwitchTransitionLauncher {
    private final com.android.server.wm.ActivityTaskManagerService mAtmService;
    private final android.content.Context mContext;
    private com.android.server.wm.DeviceStateController.DeviceState mDeviceState;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private boolean mShouldRequestTransitionOnDisplaySwitch;
    private com.android.server.wm.Transition mTransition;
    private final com.android.server.wm.TransitionController mTransitionController;

    public PhysicalDisplaySwitchTransitionLauncher(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.TransitionController transitionController) {
        this(displayContent, displayContent.mWmService.mAtmService, displayContent.mWmService.mContext, transitionController);
    }

    public PhysicalDisplaySwitchTransitionLauncher(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.ActivityTaskManagerService service, android.content.Context context, com.android.server.wm.TransitionController transitionController) {
        this.mShouldRequestTransitionOnDisplaySwitch = false;
        this.mDeviceState = com.android.server.wm.DeviceStateController.DeviceState.UNKNOWN;
        this.mDisplayContent = displayContent;
        this.mAtmService = service;
        this.mContext = context;
        this.mTransitionController = transitionController;
    }

    void foldStateChanged(com.android.server.wm.DeviceStateController.DeviceState newDeviceState) {
        boolean isUnfolding = this.mDeviceState == com.android.server.wm.DeviceStateController.DeviceState.FOLDED && (newDeviceState == com.android.server.wm.DeviceStateController.DeviceState.HALF_FOLDED || newDeviceState == com.android.server.wm.DeviceStateController.DeviceState.OPEN);
        if (isUnfolding) {
            this.mShouldRequestTransitionOnDisplaySwitch = true;
        } else if (newDeviceState != com.android.server.wm.DeviceStateController.DeviceState.HALF_FOLDED && newDeviceState != com.android.server.wm.DeviceStateController.DeviceState.OPEN) {
            this.mShouldRequestTransitionOnDisplaySwitch = false;
        }
        this.mDeviceState = newDeviceState;
    }

    public void requestDisplaySwitchTransitionIfNeeded(int displayId, int oldDisplayWidth, int oldDisplayHeight, int newDisplayWidth, int newDisplayHeight) {
        if (this.mShouldRequestTransitionOnDisplaySwitch && this.mTransitionController.isShellTransitionsEnabled() && this.mDisplayContent.getLastHasContent()) {
            boolean shouldRequestUnfoldTransition = this.mContext.getResources().getBoolean(android.R.bool.config_supportShortPressPowerWhenDefaultDisplayOn) && android.animation.ValueAnimator.areAnimatorsEnabled();
            if (shouldRequestUnfoldTransition) {
                this.mTransition = null;
                if (this.mTransitionController.isCollecting()) {
                    this.mTransition = this.mTransitionController.getCollectingTransition();
                    this.mTransition.collect(this.mDisplayContent);
                    this.mTransition.setReady(this.mDisplayContent, false);
                    this.mTransition.addFlag(16384);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[0]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 5106303602270682056L, 0, null, null);
                    }
                } else {
                    android.window.TransitionRequestInfo.DisplayChange displayChange = new android.window.TransitionRequestInfo.DisplayChange(displayId);
                    android.graphics.Rect startAbsBounds = new android.graphics.Rect(0, 0, oldDisplayWidth, oldDisplayHeight);
                    displayChange.setStartAbsBounds(startAbsBounds);
                    android.graphics.Rect endAbsBounds = new android.graphics.Rect(0, 0, newDisplayWidth, newDisplayHeight);
                    displayChange.setEndAbsBounds(endAbsBounds);
                    displayChange.setPhysicalDisplayChanged(true);
                    this.mTransition = this.mTransitionController.requestStartDisplayTransition(6, 0, this.mDisplayContent, null, displayChange);
                    this.mTransition.collect(this.mDisplayContent);
                }
                if (this.mTransition != null) {
                    this.mAtmService.startPowerMode(2);
                }
                this.mShouldRequestTransitionOnDisplaySwitch = false;
            }
        }
    }

    public void onDisplayUpdated(int fromRotation, int toRotation, android.window.DisplayAreaInfo newDisplayAreaInfo) {
        if (this.mTransition == null) {
            return;
        }
        boolean started = this.mDisplayContent.mRemoteDisplayChangeController.performRemoteDisplayChange(fromRotation, toRotation, newDisplayAreaInfo, new com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback() { // from class: com.android.server.wm.PhysicalDisplaySwitchTransitionLauncher$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback
            public final void onContinueRemoteDisplayChange(android.window.WindowContainerTransaction windowContainerTransaction) {
                this.f$0.continueDisplayUpdate(windowContainerTransaction);
            }
        });
        if (!started) {
            markTransitionAsReady();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void continueDisplayUpdate(android.window.WindowContainerTransaction transaction) {
        if (this.mTransition == null) {
            return;
        }
        if (transaction != null) {
            this.mAtmService.mWindowOrganizerController.applyTransaction(transaction);
        }
        markTransitionAsReady();
    }

    private void markTransitionAsReady() {
        if (this.mTransition == null) {
            return;
        }
        this.mTransition.setAllReady();
        this.mTransition = null;
    }
}
