package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class ImeInsetsSourceProvider extends com.android.server.wm.InsetsSourceProvider {
    private static final java.lang.String TAG = com.android.server.wm.ImeInsetsSourceProvider.class.getSimpleName();
    private boolean mFrozen;
    private boolean mGivenInsetsReady;
    private com.android.server.wm.InsetsControlTarget mImeRequester;
    private boolean mImeShowing;
    private final android.view.InsetsSource mLastSource;
    private boolean mServerVisible;
    private android.view.inputmethod.ImeTracker.Token mStatsToken;

    ImeInsetsSourceProvider(android.view.InsetsSource source, com.android.server.wm.InsetsStateController stateController, com.android.server.wm.DisplayContent displayContent) {
        super(source, stateController, displayContent);
        this.mLastSource = new android.view.InsetsSource(android.view.InsetsSource.ID_IME, android.view.WindowInsets.Type.ime());
        this.mGivenInsetsReady = false;
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    void onPostLayout() {
        super.onPostLayout();
        if (android.view.inputmethod.Flags.refactorInsetsController()) {
            com.android.server.wm.WindowState ws = this.mWindowContainer != null ? this.mWindowContainer.asWindowState() : null;
            boolean givenInsetsPending = ws != null && ws.mGivenInsetsPending;
            if (!this.mGivenInsetsReady && this.mServerVisible && !givenInsetsPending) {
                this.mGivenInsetsReady = true;
                this.mStateController.notifyControlChanged(this.mControlTarget);
            }
        }
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    protected boolean isLeashReadyForDispatching() {
        if (android.view.inputmethod.Flags.refactorInsetsController()) {
            com.android.server.wm.WindowState ws = this.mWindowContainer != null ? this.mWindowContainer.asWindowState() : null;
            boolean isDrawn = ws != null && ws.isDrawn();
            return super.isLeashReadyForDispatching() && this.mServerVisible && isDrawn && this.mGivenInsetsReady;
        }
        return super.isLeashReadyForDispatching();
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    android.view.InsetsSourceControl getControl(com.android.server.wm.InsetsControlTarget target) {
        com.android.server.wm.WindowState startingWin;
        android.view.InsetsSourceControl control = super.getControl(target);
        if (control != null && target != null && target.getWindow() != null) {
            com.android.server.wm.WindowState targetWin = target.getWindow();
            com.android.server.wm.Task task = targetWin.getTask();
            com.android.server.wm.StartingData startingData = null;
            if (task != null && (startingData = targetWin.mActivityRecord.mStartingData) == null && (startingWin = task.topStartingWindow()) != null) {
                startingData = startingWin.mStartingData;
            }
            control.setSkipAnimationOnce(startingData != null && startingData.hasImeSurface());
        }
        return control;
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    void setClientVisible(boolean clientVisible) {
        com.android.server.wm.InsetsControlTarget imeControlTarget;
        boolean wasClientVisible = isClientVisible();
        super.setClientVisible(clientVisible);
        if (!wasClientVisible && isClientVisible() && (imeControlTarget = getControlTarget()) != null && imeControlTarget.getWindow() != null && imeControlTarget.getWindow().mActivityRecord == null) {
            this.mDisplayContent.assignWindowLayers(false);
        }
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    void setServerVisible(boolean serverVisible) {
        if (this.mServerVisible != serverVisible) {
            this.mServerVisible = serverVisible;
            if (android.view.inputmethod.Flags.refactorInsetsController() && !serverVisible && !this.mFrozen) {
                this.mGivenInsetsReady = false;
                updateControlForTarget(this.mControlTarget, true);
            }
        }
        if (!this.mFrozen) {
            super.setServerVisible(serverVisible);
        }
    }

    void setFrozen(boolean frozen) {
        if (this.mFrozen == frozen) {
            return;
        }
        this.mFrozen = frozen;
        if (!frozen) {
            super.setServerVisible(this.mServerVisible);
        }
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    void updateSourceFrame(android.graphics.Rect frame) {
        super.updateSourceFrame(frame);
        onSourceChanged();
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    protected void updateVisibility() {
        boolean oldVisibility = this.mSource.isVisible();
        super.updateVisibility();
        if (android.view.inputmethod.Flags.refactorInsetsController() && this.mSource.isVisible() && !oldVisibility && this.mImeRequester != null) {
            reportImeDrawnForOrganizerIfNeeded(this.mImeRequester);
        }
        onSourceChanged();
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    void updateControlForTarget(com.android.server.wm.InsetsControlTarget target, boolean force) {
        if (target != null && target.getWindow() != null) {
            target = target.getWindow().getImeControlTarget();
        }
        super.updateControlForTarget(target, force);
        if (android.view.inputmethod.Flags.refactorInsetsController() && target != null) {
            invokeOnImeRequestedChangedListener(target.getWindow());
        }
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    protected boolean updateClientVisibility(com.android.server.wm.InsetsControlTarget caller) {
        com.android.server.wm.WindowState windowState;
        com.android.server.wm.InsetsControlTarget controlTarget = getControlTarget();
        if (caller != controlTarget) {
            if (android.view.inputmethod.Flags.refactorInsetsController() && isImeInputTarget(caller)) {
                boolean imeVisible = caller.isRequestedVisible(android.view.WindowInsets.Type.ime());
                if (controlTarget != null) {
                    controlTarget.setImeInputTargetRequestedVisibility(imeVisible);
                } else {
                    com.android.server.wm.InsetsControlTarget controlTarget2 = this.mDisplayContent.getImeHostOrFallback(caller.getWindow());
                    if (controlTarget2 != caller) {
                        controlTarget2.setImeInputTargetRequestedVisibility(imeVisible);
                    }
                }
                com.android.server.wm.WindowState windowState2 = caller.getWindow();
                invokeOnImeRequestedChangedListener(windowState2);
                return false;
            }
            return false;
        }
        boolean changed = super.updateClientVisibility(caller);
        if (!android.view.inputmethod.Flags.refactorInsetsController() && changed && caller.isRequestedVisible(this.mSource.getType())) {
            reportImeDrawnForOrganizerIfNeeded(caller);
        }
        boolean changed2 = changed | this.mDisplayContent.onImeInsetsClientVisibilityUpdate();
        if (android.view.inputmethod.Flags.refactorInsetsController() && changed2) {
            if (caller.getWindow() != null) {
                windowState = caller.getWindow();
            } else {
                windowState = this.mDisplayContent.getImeInputTarget() != null ? this.mDisplayContent.getImeInputTarget().getWindowState() : null;
            }
            invokeOnImeRequestedChangedListener(windowState);
        }
        return changed2;
    }

    void onInputTargetChanged(com.android.server.wm.InputTarget target) {
        if (android.view.inputmethod.Flags.refactorInsetsController() && target != null) {
            com.android.server.wm.WindowState targetWin = target.getWindowState();
            com.android.server.wm.InsetsControlTarget imeControlTarget = getControlTarget();
            if (target != imeControlTarget && targetWin != null && imeControlTarget != null) {
                imeControlTarget.setImeInputTargetRequestedVisibility((targetWin.getRequestedVisibleTypes() & android.view.WindowInsets.Type.ime()) != 0);
            }
        }
    }

    private void invokeOnImeRequestedChangedListener(final com.android.server.wm.WindowState windowState) {
        final com.android.server.wm.WindowManagerInternal.OnImeRequestedChangedListener imeListener = this.mDisplayContent.mWmService.mOnImeRequestedChangedListener;
        if (imeListener != null && windowState != null) {
            this.mDisplayContent.mWmService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ImeInsetsSourceProvider$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.wm.WindowManagerInternal.OnImeRequestedChangedListener onImeRequestedChangedListener = imeListener;
                    com.android.server.wm.WindowState windowState2 = windowState;
                    onImeRequestedChangedListener.onImeRequestedChanged(windowState2.mClient.asBinder(), windowState2.isRequestedVisible(android.view.WindowInsets.Type.ime()));
                }
            });
        }
    }

    private void reportImeDrawnForOrganizerIfNeeded(com.android.server.wm.InsetsControlTarget caller) {
        com.android.server.wm.WindowState callerWindow = caller.getWindow();
        if (callerWindow == null) {
            return;
        }
        com.android.server.wm.WindowToken imeToken = this.mWindowContainer.asWindowState() != null ? this.mWindowContainer.asWindowState().mToken : null;
        com.android.server.wm.AsyncRotationController rotationController = this.mDisplayContent.getAsyncRotationController();
        if (rotationController == null || !rotationController.isTargetToken(imeToken)) {
            if (imeToken != null && imeToken.isSelfAnimating(0, 64)) {
                return;
            }
            reportImeDrawnForOrganizer(caller);
        }
    }

    private void reportImeDrawnForOrganizer(com.android.server.wm.InsetsControlTarget caller) {
        com.android.server.wm.WindowState callerWindow = caller.getWindow();
        if (callerWindow != null && callerWindow.getTask() != null && callerWindow.getTask().isOrganized()) {
            this.mWindowContainer.mWmService.mAtmService.mTaskOrganizerController.reportImeDrawnOnTask(caller.getWindow().getTask());
        }
    }

    void reportImeDrawnForOrganizer() {
        com.android.server.wm.InsetsControlTarget imeControlTarget = getControlTarget();
        if (imeControlTarget != null) {
            reportImeDrawnForOrganizer(imeControlTarget);
        }
    }

    private void onSourceChanged() {
        if (this.mLastSource.equals(this.mSource)) {
            return;
        }
        this.mLastSource.set(this.mSource);
        this.mDisplayContent.mWmService.mH.obtainMessage(41, this.mDisplayContent).sendToTarget();
    }

    void scheduleShowImePostLayout(com.android.server.wm.InsetsControlTarget imeTarget, android.view.inputmethod.ImeTracker.Token statsToken) {
        if (this.mImeRequester == null) {
            android.os.Trace.asyncTraceBegin(32L, "WMS.showImePostLayout", 0);
        } else {
            logIsScheduledAndReadyToShowIme(false);
            android.view.inputmethod.ImeTracker.forLogging().onCancelled(this.mStatsToken, 18);
        }
        boolean targetChanged = isTargetChangedWithinActivity(imeTarget);
        this.mImeRequester = imeTarget;
        this.mStatsToken = statsToken;
        if (targetChanged) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, -786355099910065121L, 0, null, null);
            }
            checkAndStartShowImePostLayout();
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mImeRequester.getWindow() == null ? this.mImeRequester : this.mImeRequester.getWindow().getName());
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, 2634707843050913730L, 0, null, protoLogParam0);
        }
        this.mDisplayContent.mWmService.requestTraversal();
    }

    void checkAndStartShowImePostLayout() {
        if (!isScheduledAndReadyToShowIme()) {
            return;
        }
        if (android.view.inputmethod.Flags.refactorInsetsController()) {
            abortShowImePostLayout();
            if (this.mControl != null && this.mControl.getLeash() != null && this.mControlTarget.getWindow() != null && !this.mControlTarget.getWindow().mGivenInsetsPending) {
                int displayId = this.mDisplayContent.getDisplayId();
                this.mControlTarget.notifyInsetsControlChanged(displayId);
                return;
            }
            return;
        }
        android.view.inputmethod.ImeTracker.forLogging().onProgress(this.mStatsToken, 18);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, 8923821958256605927L, 0, null, null);
        }
        com.android.server.wm.InsetsControlTarget target = getControlTarget();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(target.getWindow() != null ? target.getWindow().getName() : "");
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, -3529253275087521638L, 0, null, protoLogParam0);
        }
        setImeShowing(true);
        target.showInsets(android.view.WindowInsets.Type.ime(), true, this.mStatsToken);
        android.os.Trace.asyncTraceEnd(32L, "WMS.showImePostLayout", 0);
        if (target != this.mImeRequester && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[3]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(this.mImeRequester.getWindow() != null ? this.mImeRequester.getWindow().getName() : "");
            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, 7927729210300708186L, 0, null, protoLogParam02);
        }
        resetShowImePostLayout();
    }

    void abortShowImePostLayout() {
        if (this.mImeRequester == null) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, -6529782994356455131L, 0, null, null);
        }
        android.os.Trace.asyncTraceEnd(32L, "WMS.showImePostLayout", 0);
        logIsScheduledAndReadyToShowIme(true);
        android.view.inputmethod.ImeTracker.forLogging().onFailed(this.mStatsToken, 43);
        resetShowImePostLayout();
    }

    private void resetShowImePostLayout() {
        this.mImeRequester = null;
        this.mStatsToken = null;
    }

    boolean isScheduledAndReadyToShowIme() {
        com.android.server.wm.InsetsControlTarget dcTarget;
        com.android.server.wm.InsetsControlTarget controlTarget;
        if (this.mImeRequester == null || !this.mServerVisible || this.mFrozen || this.mWindowContainer == null) {
            return false;
        }
        com.android.server.wm.WindowState windowState = this.mWindowContainer.asWindowState();
        if (windowState == null) {
            throw new java.lang.IllegalArgumentException("IME insets must be provided by a window.");
        }
        if (!windowState.isDrawn() || windowState.mGivenInsetsPending || (dcTarget = this.mDisplayContent.getImeTarget(0)) == null || (controlTarget = getControlTarget()) == null || controlTarget != this.mDisplayContent.getImeTarget(2) || this.mStateController.hasPendingControls(controlTarget) || getLeash(controlTarget) == null) {
            return false;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(dcTarget.getWindow().getName());
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mImeRequester.getWindow() == null ? this.mImeRequester : this.mImeRequester.getWindow().getName());
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, -6629998049460863403L, 0, null, protoLogParam0, protoLogParam1);
        }
        return (isImeLayeringTarget(this.mImeRequester, dcTarget) || isAboveImeLayeringTarget(this.mImeRequester, dcTarget) || isImeFallbackTarget(this.mImeRequester) || isImeInputTarget(this.mImeRequester) || sameAsImeControlTarget(this.mImeRequester)) && !isInTransitionAnimation(dcTarget);
    }

    private boolean isInTransitionAnimation(com.android.server.wm.InsetsControlTarget dcTarget) {
        if (dcTarget.getWindow() == null) {
            return false;
        }
        return dcTarget.getWindow().getWrapper().getExtImpl().isFlexibleTaskInTransitionAnimation(dcTarget.getWindow());
    }

    private void logIsScheduledAndReadyToShowIme(boolean aborted) {
        com.android.server.wm.WindowState windowState = this.mWindowContainer != null ? this.mWindowContainer.asWindowState() : null;
        com.android.server.wm.InsetsControlTarget dcTarget = this.mDisplayContent.getImeTarget(0);
        com.android.server.wm.InsetsControlTarget controlTarget = getControlTarget();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("showImePostLayout ").append(aborted ? "aborted" : "cancelled");
        sb.append(", isScheduledAndReadyToShowIme: ").append(isScheduledAndReadyToShowIme());
        sb.append(", mImeRequester: ").append(this.mImeRequester);
        sb.append(", serverVisible: ").append(this.mServerVisible);
        sb.append(", frozen: ").append(this.mFrozen);
        sb.append(", mWindowContainer is: ").append(this.mWindowContainer != null ? "non-null" : "null");
        sb.append(", windowState: ").append(windowState);
        if (windowState != null) {
            sb.append(", isDrawn: ").append(windowState.isDrawn());
            sb.append(", mGivenInsetsPending: ").append(windowState.mGivenInsetsPending);
        }
        sb.append(", dcTarget: ").append(dcTarget);
        sb.append(", controlTarget: ").append(controlTarget);
        if (this.mImeRequester != null && dcTarget != null && controlTarget != null) {
            sb.append("\n");
            sb.append("controlTarget == DisplayContent.controlTarget: ");
            sb.append(controlTarget == this.mDisplayContent.getImeTarget(2));
            sb.append(", hasPendingControls: ");
            sb.append(this.mStateController.hasPendingControls(controlTarget));
            boolean hasLeash = getLeash(controlTarget) != null;
            sb.append(", leash is: ").append(hasLeash ? "non-null" : "null");
            if (!hasLeash) {
                sb.append(", control is: ").append(this.mControl == null ? "null" : "non-null");
                sb.append(", mIsLeashReadyForDispatching: ").append(this.mIsLeashReadyForDispatching);
            }
            sb.append(", isImeLayeringTarget: ");
            sb.append(isImeLayeringTarget(this.mImeRequester, dcTarget));
            sb.append(", isAboveImeLayeringTarget: ");
            sb.append(isAboveImeLayeringTarget(this.mImeRequester, dcTarget));
            sb.append(", isImeFallbackTarget: ");
            sb.append(isImeFallbackTarget(this.mImeRequester));
            sb.append(", isImeInputTarget: ");
            sb.append(isImeInputTarget(this.mImeRequester));
            sb.append(", sameAsImeControlTarget: ");
            sb.append(sameAsImeControlTarget(this.mImeRequester));
        }
        android.util.Slog.d(TAG, sb.toString());
    }

    private static boolean isImeLayeringTarget(com.android.server.wm.InsetsControlTarget target, com.android.server.wm.InsetsControlTarget dcTarget) {
        return !isImeTargetWindowClosing(dcTarget.getWindow()) && target == dcTarget;
    }

    private static boolean isAboveImeLayeringTarget(com.android.server.wm.InsetsControlTarget target, com.android.server.wm.InsetsControlTarget dcTarget) {
        return target.getWindow() != null && dcTarget.getWindow().getParentWindow() == target && dcTarget.getWindow().mSubLayer > target.getWindow().mSubLayer;
    }

    private boolean isImeFallbackTarget(com.android.server.wm.InsetsControlTarget target) {
        return target == this.mDisplayContent.getImeFallback();
    }

    private boolean isImeInputTarget(com.android.server.wm.InsetsControlTarget target) {
        return target == this.mDisplayContent.getImeInputTarget();
    }

    private boolean sameAsImeControlTarget(com.android.server.wm.InsetsControlTarget target) {
        com.android.server.wm.InsetsControlTarget controlTarget = getControlTarget();
        return controlTarget == target && (target.getWindow() == null || !isImeTargetWindowClosing(target.getWindow()));
    }

    private static boolean isImeTargetWindowClosing(com.android.server.wm.WindowState win) {
        return win.mAnimatingExit || (win.mActivityRecord != null && ((win.mActivityRecord.isInTransition() && !win.mActivityRecord.isVisibleRequested()) || win.mActivityRecord.willCloseOrEnterPip()));
    }

    private boolean isTargetChangedWithinActivity(com.android.server.wm.InsetsControlTarget target) {
        return (target.getWindow() == null || this.mImeRequester == target || this.mImeRequester == null || this.mImeRequester.getWindow() == null || this.mImeRequester.getWindow().mActivityRecord != target.getWindow().mActivityRecord) ? false : true;
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        super.dump(pw, prefix);
        java.lang.String prefix2 = prefix + "  ";
        pw.print(prefix2);
        pw.print("mImeShowing=");
        pw.print(this.mImeShowing);
        if (this.mImeRequester != null) {
            pw.print(prefix2);
            pw.print("showImePostLayout pending for mImeRequester=");
            pw.print(this.mImeRequester);
            pw.println();
        } else {
            pw.print(prefix2);
            pw.print("showImePostLayout not scheduled, mImeRequester=null");
            pw.println();
        }
        pw.println();
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        long token = proto.start(fieldId);
        super.dumpDebug(proto, 1146756268033L, logLevel);
        com.android.server.wm.WindowState imeRequesterWindow = this.mImeRequester != null ? this.mImeRequester.getWindow() : null;
        if (imeRequesterWindow != null) {
            imeRequesterWindow.dumpDebug(proto, 1146756268034L, logLevel);
        }
        proto.end(token);
    }

    public void setImeShowing(boolean imeShowing) {
        if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE.isLogToLogcat()) {
            android.util.Slog.i(TAG, "setImeShowing mImeShowing:" + imeShowing);
        }
        this.mImeShowing = imeShowing;
    }

    public boolean isImeShowing() {
        return this.mImeShowing;
    }
}
