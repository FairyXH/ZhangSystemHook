package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowAnimationSpecExt {
    default void setClipSide(int clipSide) {
    }

    default int getmClipSide() {
        return 0;
    }

    default void setUseExtendAnimation(boolean useExtendAnimation) {
    }

    default boolean useExtendAnimation() {
        return true;
    }

    default void clipTmpRect(int mClipSide, android.graphics.Rect mTmpRect, float aFloat, android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
    }

    default void adjustCropRect(android.view.animation.Animation anim, android.graphics.Rect tmpRect, android.view.animation.Transformation transformation, android.view.SurfaceControl.Transaction t) {
    }
}
