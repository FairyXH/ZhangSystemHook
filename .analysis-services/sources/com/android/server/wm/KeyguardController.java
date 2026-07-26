package com.android.server.wm;

import com.android.server.wm.ActivityTaskManagerService.SleepTokenAcquirer;

/* JADX INFO: loaded from: classes3.dex */
class KeyguardController {
    private static final int DEFER_WAKE_TRANSITION_TIMEOUT_MS = 5000;
    static final java.lang.String KEYGUARD_SLEEP_TOKEN_TAG = "keyguard";
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static com.android.server.wm.IKeyguardControllerExt.IStaticExt mKeyguardControllerStaticExt = (com.android.server.wm.IKeyguardControllerExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IKeyguardControllerExt.IStaticExt.class).create();
    private com.android.server.wm.RootWindowContainer mRootWindowContainer;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    private final com.android.server.wm.ActivityTaskManagerService.SleepTokenAcquirer mSleepTokenAcquirer;
    private final com.android.server.wm.ActivityTaskSupervisor mTaskSupervisor;
    private boolean mWaitingForWakeTransition;
    private com.android.server.wm.WindowManagerService mWindowManager;
    private final android.util.SparseArray<com.android.server.wm.KeyguardController.KeyguardDisplayState> mDisplayStates = new android.util.SparseArray<>();
    private com.android.server.wm.Transition.ReadyCondition mWaitAodHide = null;
    private final java.lang.Runnable mResetWaitTransition = new java.lang.Runnable() { // from class: com.android.server.wm.KeyguardController$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };
    private com.android.server.wm.KeyguardController.KeyguardControllerWrapper mKeyguardControllerWrapper = new com.android.server.wm.KeyguardController.KeyguardControllerWrapper();
    private com.android.server.wm.IKeyguardControllerExt mKeyguardControllerExt = (com.android.server.wm.IKeyguardControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IKeyguardControllerExt.class).base(this).create();

    KeyguardController(com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.ActivityTaskSupervisor taskSupervisor) {
        this.mService = service;
        this.mTaskSupervisor = taskSupervisor;
        com.android.server.wm.ActivityTaskManagerService activityTaskManagerService = this.mService;
        java.util.Objects.requireNonNull(activityTaskManagerService);
        this.mSleepTokenAcquirer = activityTaskManagerService.new SleepTokenAcquirer(KEYGUARD_SLEEP_TOKEN_TAG);
    }

    void setWindowManager(com.android.server.wm.WindowManagerService windowManager) {
        this.mWindowManager = windowManager;
        this.mRootWindowContainer = this.mService.mRootWindowContainer;
    }

    boolean isAodShowing(int displayId) {
        return getDisplayState(displayId).mAodShowing;
    }

    boolean isKeyguardOrAodShowing(int displayId) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = getDisplayState(displayId);
        return ((!state.mKeyguardShowing && !state.mAodShowing) || state.mKeyguardGoingAway || state.mOccluded) ? false : true;
    }

    boolean isKeyguardUnoccludedOrAodShowing(int displayId) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = getDisplayState(displayId);
        if (displayId == 0 && state.mAodShowing) {
            return !state.mKeyguardGoingAway;
        }
        return isKeyguardOrAodShowing(displayId);
    }

    boolean isKeyguardShowing(int displayId) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = getDisplayState(displayId);
        return (!state.mKeyguardShowing || state.mKeyguardGoingAway || state.mOccluded) ? false : true;
    }

    boolean isKeyguardLocked(int displayId) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = getDisplayState(displayId);
        return state.mKeyguardShowing && !state.mKeyguardGoingAway;
    }

    boolean isKeyguardOccluded(int displayId) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = getDisplayState(displayId);
        return state.mKeyguardShowing && !state.mKeyguardGoingAway && state.mOccluded;
    }

    boolean topActivityOccludesKeyguard(com.android.server.wm.ActivityRecord r) {
        return getDisplayState(r.getDisplayId()).mTopOccludesActivity == r;
    }

    boolean isKeyguardGoingAway(int displayId) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = getDisplayState(displayId);
        return state.mKeyguardGoingAway && state.mKeyguardShowing;
    }

    /* JADX WARN: Multi-variable type inference failed */
    void setKeyguardShown(int i, boolean z, boolean z2) {
        com.android.server.wm.DisplayContent defaultDisplay;
        com.android.server.wm.DisplayContent displayContent = this.mRootWindowContainer.getDisplayContent(i);
        if (displayContent == null) {
            android.util.Slog.w(TAG, "setKeyguardShown called on non-existent display " + i);
            return;
        }
        if (displayContent.isKeyguardAlwaysUnlocked()) {
            android.util.Slog.i(TAG, "setKeyguardShown ignoring always unlocked display " + i);
            return;
        }
        com.android.server.wm.KeyguardController.KeyguardDisplayState displayState = getDisplayState(i);
        boolean z3 = true;
        java.lang.Object[] objArr = z2 != displayState.mAodShowing;
        java.lang.Object[] objArr2 = displayState.mAodShowing && !z2;
        java.lang.Object[] objArr3 = displayState.mKeyguardGoingAway && z;
        if (z == displayState.mKeyguardShowing && (objArr3 == false || objArr2 != false)) {
            z3 = false;
        }
        boolean z4 = z3;
        if (objArr2 != false) {
            updateDeferTransitionForAod(false);
        }
        if (!z4 && objArr == false) {
            setWakeTransitionReady();
            return;
        }
        com.android.server.wm.EventLogTags.writeWmSetKeyguardShown(i, z ? 1 : 0, z2 ? 1 : 0, displayState.mKeyguardGoingAway ? 1 : 0, displayState.mOccluded ? 1 : 0, "setKeyguardShown");
        if (i == 0) {
            if ((((z2 ? 1 : 0) ^ (z ? 1 : 0)) != 0 || (z2 && objArr != false && z4)) && !displayState.mKeyguardGoingAway && android.view.Display.isOnState(displayContent.getDisplayInfo().state)) {
                this.mWindowManager.mTaskSnapshotController.snapshotForSleeping(0);
            }
        }
        displayState.mKeyguardShowing = z;
        displayState.mAodShowing = z2;
        if (z4) {
            displayState.mKeyguardGoingAway = false;
            if (z) {
                displayState.mDismissalRequested = false;
            }
            if (!this.mKeyguardControllerExt.shouldSkipTransition(displayContent, "setKeyguardShown") && (objArr3 != false || (com.android.window.flags.Flags.keyguardAppearTransition() && z && !android.view.Display.isOffState(displayContent.getDisplayInfo().state)))) {
                if (com.android.window.flags.Flags.keyguardAppearTransition()) {
                    defaultDisplay = displayContent;
                } else {
                    defaultDisplay = this.mRootWindowContainer.getDefaultDisplay();
                }
                defaultDisplay.requestTransitionAndLegacyPrepare(3, 2048);
                if (com.android.window.flags.Flags.keyguardAppearTransition()) {
                    displayContent.mWallpaperController.adjustWallpaperWindows();
                }
                defaultDisplay.executeAppTransition();
            }
        }
        updateKeyguardSleepToken();
        this.mRootWindowContainer.ensureActivitiesVisible();
        com.android.server.inputmethod.InputMethodManagerInternal.get().updateImeWindowStatus(false, i);
        setWakeTransitionReady();
        if (objArr != false) {
            this.mWindowManager.mWindowPlacerLocked.performSurfacePlacement();
        }
        this.mKeyguardControllerExt.setKeyguardShown(z4, z, i);
    }

    private void setWakeTransitionReady() {
        if (this.mWindowManager.mAtmService.getTransitionController().getCollectingTransitionType() == 11) {
            this.mWindowManager.mAtmService.getTransitionController().setReady(this.mRootWindowContainer.getDefaultDisplay());
        }
    }

    void keyguardGoingAway(int displayId, int flags) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = getDisplayState(displayId);
        if (!state.mKeyguardShowing || state.mKeyguardGoingAway) {
            return;
        }
        android.os.Trace.traceBegin(32L, "keyguardGoingAway");
        this.mService.deferWindowLayout();
        state.mKeyguardGoingAway = true;
        try {
            this.mKeyguardControllerExt.keyguardGoingAway(flags);
            this.mKeyguardControllerExt.enableOrientationListenerWhenKeyguradGoingAway(this.mRootWindowContainer.getDefaultDisplay().mDisplayContent, flags);
            com.android.server.wm.EventLogTags.writeWmSetKeyguardShown(displayId, state.mKeyguardShowing ? 1 : 0, state.mAodShowing ? 1 : 0, 1, state.mOccluded ? 1 : 0, "keyguardGoingAway");
            if (this.mKeyguardControllerExt.ifSkipTransition(displayId)) {
                updateKeyguardSleepToken();
                return;
            }
            int transitFlags = convertTransitFlags(flags);
            com.android.server.wm.DisplayContent dc = this.mRootWindowContainer.getDefaultDisplay();
            dc.prepareAppTransition(7, transitFlags);
            dc.mAtmService.getTransitionController().requestTransitionIfNeeded(4, transitFlags, null, dc);
            updateKeyguardSleepToken();
            this.mRootWindowContainer.resumeFocusedTasksTopActivities();
            this.mRootWindowContainer.ensureActivitiesVisible();
            this.mRootWindowContainer.addStartingWindowsForVisibleActivities();
            this.mWindowManager.executeAppTransition();
        } finally {
            this.mService.continueWindowLayout();
            android.os.Trace.traceEnd(32L);
        }
    }

    void dismissKeyguard(android.os.IBinder token, com.android.internal.policy.IKeyguardDismissCallback callback, java.lang.CharSequence message) {
        com.android.server.wm.ActivityRecord activityRecord = com.android.server.wm.ActivityRecord.forTokenLocked(token);
        if (activityRecord == null || !activityRecord.visibleIgnoringKeyguard) {
            failCallback(callback);
            return;
        }
        android.util.Slog.i(TAG, "Activity requesting to dismiss Keyguard: " + activityRecord);
        if (activityRecord.getTurnScreenOnFlag() && activityRecord.isTopRunningActivity()) {
            this.mTaskSupervisor.wakeUp("dismissKeyguard");
        }
        if (this.mKeyguardControllerExt.dismissKeyguard(this.mService.mContext, activityRecord, false)) {
            return;
        }
        this.mWindowManager.dismissKeyguard(callback, message);
    }

    private void failCallback(com.android.internal.policy.IKeyguardDismissCallback callback) {
        try {
            callback.onDismissError();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to call callback", e);
        }
    }

    private int convertTransitFlags(int keyguardGoingAwayFlags) {
        int result = 256;
        if ((keyguardGoingAwayFlags & 1) != 0) {
            result = 256 | 1;
        }
        if ((keyguardGoingAwayFlags & 2) != 0) {
            result |= 2;
        }
        if ((keyguardGoingAwayFlags & 4) != 0) {
            result |= 4;
        }
        if ((keyguardGoingAwayFlags & 8) != 0) {
            result |= 8;
        }
        if ((keyguardGoingAwayFlags & 16) != 0) {
            return result | 512;
        }
        return result;
    }

    boolean canShowActivityWhileKeyguardShowing(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = getDisplayState(r.getDisplayId());
        return r.containsDismissKeyguardWindow() && canDismissKeyguard() && !state.mAodShowing && (state.mDismissalRequested || (r.canShowWhenLocked() && state.mDismissingKeyguardActivity != r));
    }

    boolean canShowWhileOccluded(boolean dismissKeyguard, boolean showWhenLocked) {
        return showWhenLocked || (dismissKeyguard && !this.mWindowManager.isKeyguardSecure(this.mService.getCurrentUserId()));
    }

    boolean checkKeyguardVisibility(com.android.server.wm.ActivityRecord r) {
        if (r.mDisplayContent.canShowWithInsecureKeyguard() && canDismissKeyguard()) {
            return true;
        }
        if (this.mKeyguardControllerExt.checkKeyguardVisibility(r, this)) {
            android.util.Slog.d(TAG, "checkKeyguardVisibility: intercept ActivityRecord:" + r);
            return false;
        }
        if (isKeyguardOrAodShowing(r.mDisplayContent.getDisplayId())) {
            return canShowActivityWhileKeyguardShowing(r);
        }
        if (isKeyguardLocked(r.getDisplayId())) {
            return canShowWhileOccluded(r.containsDismissKeyguardWindow(), r.canShowWhenLocked());
        }
        return true;
    }

    void updateVisibility() {
        for (int displayNdx = this.mRootWindowContainer.getChildCount() - 1; displayNdx >= 0; displayNdx--) {
            com.android.server.wm.DisplayContent display = (com.android.server.wm.DisplayContent) this.mRootWindowContainer.getChildAt(displayNdx);
            if (!display.isRemoving() && !display.isRemoved()) {
                com.android.server.wm.KeyguardController.KeyguardDisplayState state = getDisplayState(display.mDisplayId);
                state.updateVisibility(this, display);
                if (state.mRequestDismissKeyguard) {
                    handleDismissKeyguard(display.getDisplayId());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOccludedChanged(int displayId, com.android.server.wm.ActivityRecord topActivity) {
        if (displayId != 0) {
            updateKeyguardSleepToken(displayId);
            return;
        }
        com.android.server.wm.TransitionController tc = this.mRootWindowContainer.mTransitionController;
        boolean occluded = getDisplayState(displayId).mOccluded;
        boolean performTransition = isKeyguardLocked(displayId);
        boolean executeTransition = performTransition && !tc.isCollecting();
        this.mWindowManager.mPolicy.onKeyguardOccludedChangedLw(occluded);
        this.mService.deferWindowLayout();
        try {
            if (isKeyguardLocked(displayId)) {
                int type = occluded ? 8 : 9;
                int flag = occluded ? 4096 : 8192;
                if (tc.isShellTransitionsEnabled()) {
                    com.android.server.wm.Task trigger = (!occluded || topActivity == null) ? null : topActivity.getRootTask();
                    com.android.server.wm.Transition transition = tc.requestTransitionIfNeeded(type, flag, trigger, this.mRootWindowContainer.getDefaultDisplay());
                    if (trigger != null) {
                        if (transition == null) {
                            transition = tc.getCollectingTransition();
                        }
                        transition.collect(trigger);
                    }
                } else {
                    this.mRootWindowContainer.getDefaultDisplay().prepareAppTransition(type, flag);
                }
            } else if (tc.inTransition()) {
                java.util.ArrayList<java.lang.Runnable> arrayList = tc.mStateValidators;
                com.android.server.policy.WindowManagerPolicy windowManagerPolicy = this.mWindowManager.mPolicy;
                java.util.Objects.requireNonNull(windowManagerPolicy);
                arrayList.add(new com.android.server.wm.KeyguardController$$ExternalSyntheticLambda0(windowManagerPolicy));
            } else {
                this.mWindowManager.mPolicy.applyKeyguardOcclusionChange();
            }
            updateKeyguardSleepToken(displayId);
            if (performTransition && executeTransition) {
                this.mWindowManager.executeAppTransition();
            }
            this.mKeyguardControllerExt.handleOccludedChangedEnd(executeTransition, this, displayId, this.mWindowManager, tc);
        } finally {
            this.mService.continueWindowLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKeyguardGoingAwayChanged(com.android.server.wm.DisplayContent dc) {
        this.mService.deferWindowLayout();
        try {
            dc.prepareAppTransition(7, 0);
            dc.mAtmService.getTransitionController().requestTransitionIfNeeded(1, 256, null, dc);
            updateKeyguardSleepToken();
            this.mWindowManager.executeAppTransition();
        } finally {
            this.mService.continueWindowLayout();
        }
    }

    private void handleDismissKeyguard(int displayId) {
        if (!this.mWindowManager.isKeyguardSecure(this.mService.getCurrentUserId())) {
            return;
        }
        this.mWindowManager.dismissKeyguard(null, null);
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = getDisplayState(displayId);
        state.mDismissalRequested = true;
        com.android.server.wm.DisplayContent dc = this.mRootWindowContainer.getDefaultDisplay();
        if (state.mKeyguardShowing && canDismissKeyguard() && dc.mAppTransition.containsTransitRequest(9)) {
            this.mWindowManager.executeAppTransition();
        }
    }

    com.android.server.wm.ActivityRecord getTopOccludingActivity(int displayId) {
        return getDisplayState(displayId).mTopOccludesActivity;
    }

    com.android.server.wm.ActivityRecord getDismissKeyguardActivity(int displayId) {
        return getDisplayState(displayId).mDismissingKeyguardActivity;
    }

    boolean canDismissKeyguard() {
        return this.mWindowManager.mPolicy.isKeyguardTrustedLw() || !this.mWindowManager.isKeyguardSecure(this.mService.getCurrentUserId());
    }

    private void updateKeyguardSleepToken() {
        for (int displayNdx = this.mRootWindowContainer.getChildCount() - 1; displayNdx >= 0; displayNdx--) {
            com.android.server.wm.DisplayContent display = (com.android.server.wm.DisplayContent) this.mRootWindowContainer.getChildAt(displayNdx);
            updateKeyguardSleepToken(display.mDisplayId);
        }
    }

    private void updateKeyguardSleepToken(int displayId) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = getDisplayState(displayId);
        if (isKeyguardUnoccludedOrAodShowing(displayId) && !this.mKeyguardControllerExt.skipAcquireSleepToken(displayId)) {
            state.mSleepTokenAcquirer.acquire(displayId);
        } else {
            state.mSleepTokenAcquirer.release(displayId);
        }
    }

    private com.android.server.wm.KeyguardController.KeyguardDisplayState getDisplayState(int displayId) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = this.mDisplayStates.get(displayId);
        if (state == null) {
            com.android.server.wm.KeyguardController.KeyguardDisplayState state2 = new com.android.server.wm.KeyguardController.KeyguardDisplayState(this.mService, displayId, this.mSleepTokenAcquirer);
            this.mDisplayStates.append(displayId, state2);
            return state2;
        }
        return state;
    }

    void onDisplayRemoved(int displayId) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState state = this.mDisplayStates.get(displayId);
        if (state != null) {
            state.onRemoved();
            this.mDisplayStates.remove(displayId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWindowManager.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                updateDeferTransitionForAod(false);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void updateDeferTransitionForAod(boolean waiting) {
        if (this.mService.getTransitionController().useFullReadyTracking()) {
            if (waiting == (this.mWaitAodHide != null)) {
                return;
            }
        } else if (waiting == this.mWaitingForWakeTransition) {
            return;
        }
        if (!this.mService.getTransitionController().isCollecting()) {
            return;
        }
        if (waiting && isAodShowing(0)) {
            this.mWaitingForWakeTransition = true;
            this.mWindowManager.mAtmService.getTransitionController().deferTransitionReady();
            this.mWaitAodHide = new com.android.server.wm.Transition.ReadyCondition("AOD hidden");
            this.mWindowManager.mAtmService.getTransitionController().waitFor(this.mWaitAodHide);
            this.mWindowManager.mH.postDelayed(this.mResetWaitTransition, 5000L);
            return;
        }
        if (!waiting) {
            this.mWaitingForWakeTransition = false;
            this.mWindowManager.mAtmService.getTransitionController().continueTransitionReady();
            this.mWindowManager.mH.removeCallbacks(this.mResetWaitTransition);
            com.android.server.wm.Transition.ReadyCondition waitAodHide = this.mWaitAodHide;
            this.mWaitAodHide = null;
            waitAodHide.meet();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class KeyguardDisplayState {
        private boolean mAodShowing;
        private boolean mDismissalRequested;
        private com.android.server.wm.ActivityRecord mDismissingKeyguardActivity;
        private final int mDisplayId;
        private boolean mKeyguardGoingAway;
        private boolean mKeyguardShowing;
        private boolean mOccluded;
        private boolean mRequestDismissKeyguard;
        private final com.android.server.wm.ActivityTaskManagerService mService;
        private final com.android.server.wm.ActivityTaskManagerService.SleepTokenAcquirer mSleepTokenAcquirer;
        private com.android.server.wm.ActivityRecord mTopOccludesActivity;
        private com.android.server.wm.ActivityRecord mTopTurnScreenOnActivity;

        KeyguardDisplayState(com.android.server.wm.ActivityTaskManagerService service, int displayId, com.android.server.wm.ActivityTaskManagerService.SleepTokenAcquirer acquirer) {
            this.mService = service;
            this.mDisplayId = displayId;
            this.mSleepTokenAcquirer = acquirer;
        }

        void onRemoved() {
            this.mTopOccludesActivity = null;
            this.mDismissingKeyguardActivity = null;
            this.mTopTurnScreenOnActivity = null;
            this.mSleepTokenAcquirer.release(this.mDisplayId);
        }

        void updateVisibility(com.android.server.wm.KeyguardController keyguardController, com.android.server.wm.DisplayContent displayContent) {
            boolean z = this.mOccluded;
            boolean z2 = this.mKeyguardGoingAway;
            com.android.server.wm.ActivityRecord activityRecord = this.mDismissingKeyguardActivity;
            this.mRequestDismissKeyguard = false;
            this.mOccluded = false;
            this.mTopOccludesActivity = null;
            this.mDismissingKeyguardActivity = null;
            this.mTopTurnScreenOnActivity = null;
            com.android.server.wm.Task rootTaskForControllingOccluding = getRootTaskForControllingOccluding(displayContent);
            com.android.server.wm.ActivityRecord topNonFinishingActivity = rootTaskForControllingOccluding != null ? rootTaskForControllingOccluding.getTopNonFinishingActivity() : null;
            if (topNonFinishingActivity != null) {
                boolean zContainsDismissKeyguardWindow = topNonFinishingActivity.containsDismissKeyguardWindow();
                boolean zCanShowWhenLocked = topNonFinishingActivity.canShowWhenLocked();
                if (this.mService.mTaskSupervisor.getKeyguardController().getWrapper().getExtImpl().checkKeyguardVisibility(topNonFinishingActivity, keyguardController)) {
                    android.util.Slog.d(com.android.server.wm.KeyguardController.TAG, "updateVisibility check showWhenLocked state:  checkKeyguardVisibility: intercept ActivityRecord:" + topNonFinishingActivity);
                    zContainsDismissKeyguardWindow = false;
                    zCanShowWhenLocked = false;
                }
                if (topNonFinishingActivity.getWrapper().getExtImpl().isDisableshowWhenLockByRecents()) {
                    android.util.Slog.d(com.android.server.wm.KeyguardController.TAG, "updateVisibility check showWhenLocked state:  checkKeyguardVisibility: intercept ActivityRecord:" + topNonFinishingActivity + ", isDisableshowWhenLockByRecents = true");
                    zCanShowWhenLocked = false;
                    zContainsDismissKeyguardWindow = false;
                    topNonFinishingActivity.getWrapper().getExtImpl().setDisableshowWhenLockByRecents(false);
                }
                if (zContainsDismissKeyguardWindow) {
                    this.mDismissingKeyguardActivity = topNonFinishingActivity;
                }
                if (topNonFinishingActivity.getTurnScreenOnFlag() && topNonFinishingActivity.currentLaunchCanTurnScreenOn()) {
                    this.mTopTurnScreenOnActivity = topNonFinishingActivity;
                }
                boolean zIsKeyguardSecure = keyguardController.mWindowManager.isKeyguardSecure(keyguardController.mService.getCurrentUserId());
                if (topNonFinishingActivity.mDismissKeyguardIfInsecure && this.mKeyguardShowing && !zIsKeyguardSecure) {
                    this.mKeyguardGoingAway = true;
                } else if (topNonFinishingActivity.canShowWhenLocked() && zCanShowWhenLocked) {
                    this.mTopOccludesActivity = topNonFinishingActivity;
                }
                topNonFinishingActivity.mDismissKeyguardIfInsecure = false;
                this.mOccluded = this.mTopOccludesActivity != null || (this.mDismissingKeyguardActivity != null && rootTaskForControllingOccluding.topRunningActivity() == this.mDismissingKeyguardActivity && keyguardController.canShowWhileOccluded(true, false));
                if (this.mDisplayId != 0) {
                    this.mOccluded |= displayContent.canShowWithInsecureKeyguard() && keyguardController.canDismissKeyguard();
                }
            }
            this.mRequestDismissKeyguard = (activityRecord == this.mDismissingKeyguardActivity || this.mOccluded || this.mKeyguardGoingAway || this.mDismissingKeyguardActivity == null) ? false : true;
            if (this.mOccluded && this.mKeyguardShowing && !displayContent.isSleeping() && !topNonFinishingActivity.fillsParent() && displayContent.mWallpaperController.getWallpaperTarget() == null) {
                displayContent.pendingLayoutChanges |= 4;
            }
            com.android.server.wm.KeyguardController.mKeyguardControllerStaticExt.setAppLayoutChanges(this.mOccluded, this.mKeyguardShowing, displayContent, topNonFinishingActivity, 4);
            if (this.mTopTurnScreenOnActivity != null && !this.mService.mWindowManager.mPowerManager.isInteractive() && (this.mRequestDismissKeyguard || this.mOccluded)) {
                keyguardController.mTaskSupervisor.wakeUp("handleTurnScreenOn");
                this.mTopTurnScreenOnActivity.setCurrentLaunchCanTurnScreenOn(false);
            }
            boolean z3 = false;
            if (z != this.mOccluded) {
                if (this.mDisplayId == 0) {
                    com.android.server.wm.EventLogTags.writeWmSetKeyguardShown(this.mDisplayId, this.mKeyguardShowing ? 1 : 0, this.mAodShowing ? 1 : 0, this.mKeyguardGoingAway ? 1 : 0, this.mOccluded ? 1 : 0, "updateVisibility");
                }
                keyguardController.handleOccludedChanged(this.mDisplayId, this.mTopOccludesActivity);
                z3 = true;
                android.util.Slog.d(com.android.server.wm.KeyguardController.TAG, "updateVisibility occlude from:" + z + " to " + this.mOccluded + "," + this.mDisplayId + "," + this.mTopOccludesActivity);
            } else if (!z2 && this.mKeyguardGoingAway) {
                keyguardController.handleKeyguardGoingAwayChanged(displayContent);
                z3 = true;
            }
            if (z3 && topNonFinishingActivity != null) {
                if (this.mOccluded || this.mKeyguardGoingAway) {
                    displayContent.mTransitionController.collect(topNonFinishingActivity);
                }
            }
        }

        private com.android.server.wm.Task getRootTaskForControllingOccluding(com.android.server.wm.DisplayContent display) {
            return display.getRootTask(new java.util.function.Predicate() { // from class: com.android.server.wm.KeyguardController$KeyguardDisplayState$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.KeyguardController.KeyguardDisplayState.lambda$getRootTaskForControllingOccluding$0((com.android.server.wm.Task) obj);
                }
            });
        }

        static /* synthetic */ boolean lambda$getRootTaskForControllingOccluding$0(com.android.server.wm.Task task) {
            return (task == null || !task.isFocusableAndVisible() || task.inPinnedWindowingMode() || task.getWrapper().getExtImpl().isSkipControllingOccluding(task)) ? false : true;
        }

        void dumpStatus(java.io.PrintWriter pw, java.lang.String prefix) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(prefix);
            sb.append(" KeyguardShowing=").append(this.mKeyguardShowing).append(" AodShowing=").append(this.mAodShowing).append(" KeyguardGoingAway=").append(this.mKeyguardGoingAway).append(" DismissalRequested=").append(this.mDismissalRequested).append("  Occluded=").append(this.mOccluded).append(" DismissingKeyguardActivity=").append(this.mDismissingKeyguardActivity).append(" TurnScreenOnActivity=").append(this.mTopTurnScreenOnActivity).append(" at display=").append(this.mDisplayId);
            sb.append(" mTopOccludesActivity=").append(this.mTopOccludesActivity);
            pw.println(sb.toString());
        }

        void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, this.mDisplayId);
            proto.write(1133871366146L, this.mKeyguardShowing);
            proto.write(1133871366147L, this.mAodShowing);
            proto.write(1133871366148L, this.mOccluded);
            proto.write(1133871366149L, this.mKeyguardGoingAway);
            proto.end(token);
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState default_state = getDisplayState(0);
        pw.println(prefix + "KeyguardController:");
        pw.println(prefix + "  mKeyguardShowing=" + default_state.mKeyguardShowing);
        pw.println(prefix + "  mAodShowing=" + default_state.mAodShowing);
        pw.println(prefix + "  mKeyguardGoingAway=" + default_state.mKeyguardGoingAway);
        dumpDisplayStates(pw, prefix);
        pw.println(prefix + "  mDismissalRequested=" + default_state.mDismissalRequested);
        pw.println();
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        com.android.server.wm.KeyguardController.KeyguardDisplayState default_state = getDisplayState(0);
        long token = proto.start(fieldId);
        proto.write(1133871366147L, default_state.mAodShowing);
        proto.write(1133871366145L, default_state.mKeyguardShowing);
        proto.write(1133871366149L, default_state.mKeyguardGoingAway);
        writeDisplayStatesToProto(proto, 2246267895812L);
        proto.end(token);
    }

    private void dumpDisplayStates(java.io.PrintWriter pw, java.lang.String prefix) {
        for (int i = 0; i < this.mDisplayStates.size(); i++) {
            this.mDisplayStates.valueAt(i).dumpStatus(pw, prefix);
        }
    }

    private void writeDisplayStatesToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        for (int i = 0; i < this.mDisplayStates.size(); i++) {
            this.mDisplayStates.valueAt(i).dumpDebug(proto, fieldId);
        }
    }

    public com.android.server.wm.IKeyguardControllerWrapper getWrapper() {
        return this.mKeyguardControllerWrapper;
    }

    private class KeyguardControllerWrapper implements com.android.server.wm.IKeyguardControllerWrapper {
        private KeyguardControllerWrapper() {
        }

        @Override // com.android.server.wm.IKeyguardControllerWrapper
        public com.android.server.wm.IKeyguardControllerExt getExtImpl() {
            return com.android.server.wm.KeyguardController.this.mKeyguardControllerExt;
        }
    }
}
