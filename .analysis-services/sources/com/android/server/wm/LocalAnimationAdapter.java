package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class LocalAnimationAdapter implements com.android.server.wm.AnimationAdapter {
    private final com.android.server.wm.SurfaceAnimationRunner mAnimator;
    private final com.android.server.wm.LocalAnimationAdapter.AnimationSpec mSpec;

    LocalAnimationAdapter(com.android.server.wm.LocalAnimationAdapter.AnimationSpec spec, com.android.server.wm.SurfaceAnimationRunner animator) {
        this.mSpec = spec;
        this.mAnimator = animator;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public boolean getShowWallpaper() {
        return this.mSpec.getShowWallpaper();
    }

    @Override // com.android.server.wm.AnimationAdapter
    public boolean getShowBackground() {
        return this.mSpec.getShowBackground();
    }

    @Override // com.android.server.wm.AnimationAdapter
    public int getBackgroundColor() {
        return this.mSpec.getBackgroundColor();
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void startAnimation(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, final int type, final com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishCallback) {
        this.mAnimator.startAnimation(this.mSpec, animationLeash, t, new java.lang.Runnable() { // from class: com.android.server.wm.LocalAnimationAdapter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startAnimation$0(finishCallback, type);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$0(com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishCallback, int type) {
        finishCallback.onAnimationFinished(type, this);
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void onAnimationCancelled(android.view.SurfaceControl animationLeash) {
        this.mAnimator.onAnimationCancelled(animationLeash);
    }

    @Override // com.android.server.wm.AnimationAdapter
    public long getDurationHint() {
        return this.mSpec.getDuration();
    }

    @Override // com.android.server.wm.AnimationAdapter
    public long getStatusBarTransitionsStartTime() {
        return this.mSpec.calculateStatusBarTransitionStartTime();
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        this.mSpec.dump(pw, prefix);
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void dumpDebug(android.util.proto.ProtoOutputStream proto) {
        long token = proto.start(1146756268033L);
        this.mSpec.dumpDebug(proto, 1146756268033L);
        proto.end(token);
    }

    interface AnimationSpec {
        void apply(android.view.SurfaceControl.Transaction transaction, android.view.SurfaceControl surfaceControl, long j);

        void dump(java.io.PrintWriter printWriter, java.lang.String str);

        void dumpDebugInner(android.util.proto.ProtoOutputStream protoOutputStream);

        long getDuration();

        default boolean getShowWallpaper() {
            return false;
        }

        default boolean getShowBackground() {
            return false;
        }

        default int getBackgroundColor() {
            return 0;
        }

        default long calculateStatusBarTransitionStartTime() {
            return android.os.SystemClock.uptimeMillis();
        }

        default boolean canSkipFirstFrame() {
            return false;
        }

        default boolean needsEarlyWakeup() {
            return false;
        }

        default float getFraction(float currentPlayTime) {
            float duration = getDuration();
            if (duration > 0.0f) {
                return currentPlayTime / duration;
            }
            return 1.0f;
        }

        default void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            dumpDebugInner(proto);
            proto.end(token);
        }

        default com.android.server.wm.WindowAnimationSpec asWindowAnimationSpec() {
            return null;
        }
    }
}
