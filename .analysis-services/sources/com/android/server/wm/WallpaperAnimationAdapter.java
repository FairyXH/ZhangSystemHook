package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WallpaperAnimationAdapter implements com.android.server.wm.AnimationAdapter {
    private static final java.lang.String TAG = "WallpaperAnimationAdapter";
    private java.util.function.Consumer<com.android.server.wm.WallpaperAnimationAdapter> mAnimationCanceledRunnable;
    private android.view.SurfaceControl mCapturedLeash;
    private com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback mCapturedLeashFinishCallback;
    private long mDurationHint;
    private int mLastAnimationType;
    private long mStatusBarTransitionDelay;
    private android.view.RemoteAnimationTarget mTarget;
    private final com.android.server.wm.WallpaperWindowToken mWallpaperToken;

    WallpaperAnimationAdapter(com.android.server.wm.WallpaperWindowToken wallpaperToken, long durationHint, long statusBarTransitionDelay, java.util.function.Consumer<com.android.server.wm.WallpaperAnimationAdapter> animationCanceledRunnable) {
        this.mWallpaperToken = wallpaperToken;
        this.mDurationHint = durationHint;
        this.mStatusBarTransitionDelay = statusBarTransitionDelay;
        this.mAnimationCanceledRunnable = animationCanceledRunnable;
    }

    public static android.view.RemoteAnimationTarget[] startWallpaperAnimations(com.android.server.wm.DisplayContent displayContent, final long durationHint, final long statusBarTransitionDelay, final java.util.function.Consumer<com.android.server.wm.WallpaperAnimationAdapter> animationCanceledRunnable, final java.util.ArrayList<com.android.server.wm.WallpaperAnimationAdapter> adaptersOut) {
        if (!shouldStartWallpaperAnimation(displayContent)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(displayContent);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 1964980935866463086L, 0, null, protoLogParam0);
            }
            return new android.view.RemoteAnimationTarget[0];
        }
        final java.util.ArrayList<android.view.RemoteAnimationTarget> targets = new java.util.ArrayList<>();
        displayContent.forAllWallpaperWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WallpaperAnimationAdapter$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.WallpaperAnimationAdapter.lambda$startWallpaperAnimations$0(durationHint, statusBarTransitionDelay, animationCanceledRunnable, targets, adaptersOut, (com.android.server.wm.WallpaperWindowToken) obj);
            }
        });
        return (android.view.RemoteAnimationTarget[]) targets.toArray(new android.view.RemoteAnimationTarget[targets.size()]);
    }

    static /* synthetic */ void lambda$startWallpaperAnimations$0(long durationHint, long statusBarTransitionDelay, java.util.function.Consumer animationCanceledRunnable, java.util.ArrayList targets, java.util.ArrayList adaptersOut, com.android.server.wm.WallpaperWindowToken wallpaperWindow) {
        com.android.server.wm.WallpaperAnimationAdapter wallpaperAdapter = new com.android.server.wm.WallpaperAnimationAdapter(wallpaperWindow, durationHint, statusBarTransitionDelay, animationCanceledRunnable);
        wallpaperWindow.startAnimation(wallpaperWindow.getPendingTransaction(), wallpaperAdapter, false, 16);
        targets.add(wallpaperAdapter.createRemoteAnimationTarget());
        adaptersOut.add(wallpaperAdapter);
    }

    static boolean shouldStartWallpaperAnimation(com.android.server.wm.DisplayContent displayContent) {
        return displayContent.mWallpaperController.isWallpaperVisible();
    }

    android.view.RemoteAnimationTarget createRemoteAnimationTarget() {
        this.mTarget = new android.view.RemoteAnimationTarget(-1, -1, getLeash(), false, (android.graphics.Rect) null, (android.graphics.Rect) null, this.mWallpaperToken.getPrefixOrderIndex(), new android.graphics.Point(), (android.graphics.Rect) null, (android.graphics.Rect) null, this.mWallpaperToken.getWindowConfiguration(), true, (android.view.SurfaceControl) null, (android.graphics.Rect) null, (android.app.ActivityManager.RunningTaskInfo) null, false);
        return this.mTarget;
    }

    android.view.SurfaceControl getLeash() {
        return this.mCapturedLeash;
    }

    com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback getLeashFinishedCallback() {
        return this.mCapturedLeashFinishCallback;
    }

    int getLastAnimationType() {
        return this.mLastAnimationType;
    }

    com.android.server.wm.WallpaperWindowToken getToken() {
        return this.mWallpaperToken;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public boolean getShowWallpaper() {
        return false;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void startAnimation(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, int type, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishCallback) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 8131665298937888044L, 0, null, null);
        }
        t.setLayer(animationLeash, this.mWallpaperToken.getPrefixOrderIndex());
        this.mCapturedLeash = animationLeash;
        this.mCapturedLeashFinishCallback = finishCallback;
        this.mLastAnimationType = type;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void onAnimationCancelled(android.view.SurfaceControl animationLeash) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_REMOTE_ANIMATIONS_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 8030745595351281943L, 0, null, null);
        }
        this.mAnimationCanceledRunnable.accept(this);
    }

    @Override // com.android.server.wm.AnimationAdapter
    public long getDurationHint() {
        return this.mDurationHint;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public long getStatusBarTransitionsStartTime() {
        return android.os.SystemClock.uptimeMillis() + this.mStatusBarTransitionDelay;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("token=");
        pw.println(this.mWallpaperToken);
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
