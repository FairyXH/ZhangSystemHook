package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface AnimationAdapter {
    public static final long STATUS_BAR_TRANSITION_DURATION = 120;

    void dump(java.io.PrintWriter printWriter, java.lang.String str);

    void dumpDebug(android.util.proto.ProtoOutputStream protoOutputStream);

    long getDurationHint();

    boolean getShowWallpaper();

    long getStatusBarTransitionsStartTime();

    void onAnimationCancelled(android.view.SurfaceControl surfaceControl);

    void startAnimation(android.view.SurfaceControl surfaceControl, android.view.SurfaceControl.Transaction transaction, int i, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback onAnimationFinishedCallback);

    default boolean getShowBackground() {
        return false;
    }

    default int getBackgroundColor() {
        return 0;
    }

    default void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        dumpDebug(proto);
        proto.end(token);
    }

    default boolean shouldDeferAnimationFinish(java.lang.Runnable endDeferFinishCallback) {
        return false;
    }
}
