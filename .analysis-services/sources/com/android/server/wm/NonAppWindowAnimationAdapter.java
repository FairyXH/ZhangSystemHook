package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class NonAppWindowAnimationAdapter implements com.android.server.wm.AnimationAdapter {
    private android.view.SurfaceControl mCapturedLeash;
    private com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback mCapturedLeashFinishCallback;
    private long mDurationHint;
    private int mLastAnimationType;
    private long mStatusBarTransitionDelay;
    private android.graphics.Point mSurfacePosition = new android.graphics.Point();
    private android.view.RemoteAnimationTarget mTarget;
    private final com.android.server.wm.WindowContainer mWindowContainer;

    @Override // com.android.server.wm.AnimationAdapter
    public boolean getShowWallpaper() {
        return false;
    }

    NonAppWindowAnimationAdapter(com.android.server.wm.WindowContainer w, long durationHint, long statusBarTransitionDelay) {
        this.mWindowContainer = w;
        this.mDurationHint = durationHint;
        this.mStatusBarTransitionDelay = statusBarTransitionDelay;
    }

    static android.view.RemoteAnimationTarget[] startNonAppWindowAnimations(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent, int transit, long durationHint, long statusBarTransitionDelay, java.util.ArrayList<com.android.server.wm.NonAppWindowAnimationAdapter> adaptersOut) {
        java.util.ArrayList<android.view.RemoteAnimationTarget> targets = new java.util.ArrayList<>();
        if (shouldStartNonAppWindowAnimationsForKeyguardExit(transit)) {
            startNonAppWindowAnimationsForKeyguardExit(service, durationHint, statusBarTransitionDelay, targets, adaptersOut);
        } else if (shouldAttachNavBarToApp(service, displayContent, transit)) {
            startNavigationBarWindowAnimation(displayContent, durationHint, statusBarTransitionDelay, targets, adaptersOut);
        }
        return (android.view.RemoteAnimationTarget[]) targets.toArray(new android.view.RemoteAnimationTarget[targets.size()]);
    }

    static boolean shouldStartNonAppWindowAnimationsForKeyguardExit(int transit) {
        return transit == 20 || transit == 21;
    }

    static boolean shouldAttachNavBarToApp(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent, int transit) {
        return (transit == 8 || transit == 10 || transit == 12) && displayContent.getDisplayPolicy().shouldAttachNavBarToAppDuringTransition() && service.getRecentsAnimationController() == null && displayContent.getAsyncRotationController() == null;
    }

    private static void startNonAppWindowAnimationsForKeyguardExit(final com.android.server.wm.WindowManagerService service, final long durationHint, final long statusBarTransitionDelay, final java.util.ArrayList<android.view.RemoteAnimationTarget> targets, final java.util.ArrayList<com.android.server.wm.NonAppWindowAnimationAdapter> adaptersOut) {
        com.android.server.policy.WindowManagerPolicy windowManagerPolicy = service.mPolicy;
        service.mRoot.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.NonAppWindowAnimationAdapter$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.NonAppWindowAnimationAdapter.lambda$startNonAppWindowAnimationsForKeyguardExit$0(service, durationHint, statusBarTransitionDelay, adaptersOut, targets, (com.android.server.wm.WindowState) obj);
            }
        }, true);
    }

    static /* synthetic */ void lambda$startNonAppWindowAnimationsForKeyguardExit$0(com.android.server.wm.WindowManagerService service, long durationHint, long statusBarTransitionDelay, java.util.ArrayList adaptersOut, java.util.ArrayList targets, com.android.server.wm.WindowState nonAppWindow) {
        if (nonAppWindow.mActivityRecord == null && nonAppWindow.canBeHiddenByKeyguard() && nonAppWindow.wouldBeVisibleIfPolicyIgnored() && !nonAppWindow.isVisible() && nonAppWindow != service.mRoot.getCurrentInputMethodWindow()) {
            com.android.server.wm.NonAppWindowAnimationAdapter nonAppAdapter = new com.android.server.wm.NonAppWindowAnimationAdapter(nonAppWindow, durationHint, statusBarTransitionDelay);
            nonAppAdapter.updateSurfacePosition(nonAppWindow);
            adaptersOut.add(nonAppAdapter);
            nonAppWindow.startAnimation(nonAppWindow.getPendingTransaction(), nonAppAdapter, false, 16);
            targets.add(nonAppAdapter.createRemoteAnimationTarget());
        }
    }

    private static void startNavigationBarWindowAnimation(com.android.server.wm.DisplayContent displayContent, long durationHint, long statusBarTransitionDelay, java.util.ArrayList<android.view.RemoteAnimationTarget> targets, java.util.ArrayList<com.android.server.wm.NonAppWindowAnimationAdapter> adaptersOut) {
        com.android.server.wm.WindowState navWindow = displayContent.getDisplayPolicy().getNavigationBar();
        com.android.server.wm.NonAppWindowAnimationAdapter nonAppAdapter = new com.android.server.wm.NonAppWindowAnimationAdapter(navWindow.mToken, durationHint, statusBarTransitionDelay);
        nonAppAdapter.updateSurfacePosition(navWindow);
        adaptersOut.add(nonAppAdapter);
        navWindow.mToken.startAnimation(navWindow.mToken.getPendingTransaction(), nonAppAdapter, false, 16);
        targets.add(nonAppAdapter.createRemoteAnimationTarget());
    }

    private void updateSurfacePosition(com.android.server.wm.WindowState win) {
        android.graphics.Point pos = win.getLastSurfacePosition();
        this.mSurfacePosition.set(pos.x, pos.y);
    }

    android.view.RemoteAnimationTarget createRemoteAnimationTarget() {
        this.mTarget = new android.view.RemoteAnimationTarget(-1, -1, getLeash(), false, new android.graphics.Rect(), (android.graphics.Rect) null, this.mWindowContainer.getPrefixOrderIndex(), this.mSurfacePosition, this.mWindowContainer.getBounds(), (android.graphics.Rect) null, this.mWindowContainer.getWindowConfiguration(), true, (android.view.SurfaceControl) null, (android.graphics.Rect) null, (android.app.ActivityManager.RunningTaskInfo) null, false, this.mWindowContainer.getWindowType());
        return this.mTarget;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void startAnimation(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, int type, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishCallback) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 3788905348567806832L, 0, null, null);
        }
        this.mCapturedLeash = animationLeash;
        this.mCapturedLeashFinishCallback = finishCallback;
        this.mLastAnimationType = type;
    }

    com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback getLeashFinishedCallback() {
        return this.mCapturedLeashFinishCallback;
    }

    int getLastAnimationType() {
        return this.mLastAnimationType;
    }

    com.android.server.wm.WindowContainer getWindowContainer() {
        return this.mWindowContainer;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public long getDurationHint() {
        return this.mDurationHint;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public long getStatusBarTransitionsStartTime() {
        return android.os.SystemClock.uptimeMillis() + this.mStatusBarTransitionDelay;
    }

    android.view.SurfaceControl getLeash() {
        return this.mCapturedLeash;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void onAnimationCancelled(android.view.SurfaceControl animationLeash) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 705955074330737483L, 0, null, null);
        }
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("windowContainer=");
        pw.println(this.mWindowContainer);
        if (this.mTarget != null) {
            pw.print(prefix);
            pw.println("Target:");
            this.mTarget.dump(pw, prefix + "  ");
        } else {
            pw.print(prefix);
            pw.println("Target: null");
        }
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void dumpDebug(android.util.proto.ProtoOutputStream proto) {
        long token = proto.start(1146756268034L);
        if (this.mTarget != null) {
            this.mTarget.dumpDebug(proto, 1146756268033L);
        }
        proto.end(token);
    }
}
