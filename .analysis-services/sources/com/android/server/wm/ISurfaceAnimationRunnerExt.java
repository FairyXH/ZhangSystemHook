package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ISurfaceAnimationRunnerExt {
    default void tryClearAnimPointsWhenCancelled(com.android.server.wm.LocalAnimationAdapter.AnimationSpec animationSpec, int leashHash) {
    }

    default void trySaveAnimationLeashHashAndReinitializeAnimParams(com.android.server.wm.LocalAnimationAdapter.AnimationSpec animationSpec, int leashHash) {
    }

    default void computeAnimHashForstartAnimationLocked(com.android.server.wm.LocalAnimationAdapter.AnimationSpec animationSpec) {
    }

    default void recordCurrentAnimationPoints(long currentTime) {
    }

    default boolean hookonAnimationEndRemove(boolean blockOriginal, android.view.SurfaceControl leash) {
        return false;
    }

    default void onWindowAnimationEnded(int leashHash) {
    }

    default void onAnimationStart(com.android.server.wm.LocalAnimationAdapter.AnimationSpec animationSpec, long duration, android.view.Choreographer choreographer) {
    }

    default void onAnimationEnd(com.android.server.wm.LocalAnimationAdapter.AnimationSpec animationSpec, android.view.Choreographer choreographer) {
    }

    default void callGcSupression(int type, int time) {
    }

    default void callGcDesupression(int type) {
    }
}
