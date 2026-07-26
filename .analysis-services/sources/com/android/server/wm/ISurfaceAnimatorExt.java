package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ISurfaceAnimatorExt {
    default boolean isReuseLeash() {
        return false;
    }

    default boolean hookReset(com.android.server.wm.SurfaceAnimator animator, android.view.SurfaceControl.Transaction t) {
        return false;
    }

    default void customReset(boolean destroyLeash) {
    }

    default boolean hookResetForTask(com.android.server.wm.SurfaceAnimator surfaceAnimator, boolean startAnim) {
        return false;
    }

    default void setDeferAnimationFinish(com.android.server.wm.SurfaceAnimator.Animatable mAnimatable, boolean isFinish) {
    }

    default void setReuseLeash(com.android.server.wm.SurfaceAnimator surfaceAnimator) {
    }

    default boolean hookSetLeash(com.android.server.wm.SurfaceAnimator.Animatable mAnimatable, android.view.SurfaceControl mLeash) {
        return false;
    }

    default void resetIfNeeded(com.android.server.wm.SurfaceAnimator animator) {
    }

    public interface IStaticExt {
        default void adjustAnimationLeashLayerIfNeeded(android.view.SurfaceControl.Transaction t, com.android.server.wm.SurfaceAnimator.Animatable animatable, android.view.SurfaceControl leash) {
        }
    }

    default boolean useGesturePosition(com.android.server.wm.SurfaceAnimator animator, android.graphics.Point outPoint, boolean deleteData) {
        return false;
    }

    default void callOrmsSetSceneActionForRemoteAnimation(boolean isFinish, android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, int type) {
    }

    default boolean isDragZoomToSplitLeash(android.view.SurfaceControl leash) {
        return false;
    }

    default boolean isDragSplitToFullLeash(android.view.SurfaceControl leash) {
        return false;
    }

    default void boostLeashLayerIfNeed(com.android.server.wm.SurfaceAnimator.Animatable mAnimatable, int type, android.view.SurfaceControl.Transaction t, android.view.SurfaceControl mLeash) {
    }

    default void showTaskIfNeed(com.android.server.wm.SurfaceAnimator.Animatable mAnimatable, android.view.SurfaceControl.Transaction t) {
    }

    default boolean cancelAnimThreadUxIfNeed(com.android.server.wm.SurfaceAnimator.Animatable animatable, int type) {
        return true;
    }
}
