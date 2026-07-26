package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IScreenRotationAnimationExt {
    default float computStartLuma(float startLuma) {
        return startLuma;
    }

    default void setRotationLayer(android.view.SurfaceControl sc, com.android.server.wm.DisplayContent displayContent) {
    }

    default void setCustomAnim(boolean customAnim) {
    }

    default boolean hookLoadAnimation(int delta, int originWidth, int originHeight, int finalWidth, int finalHeight) {
        return false;
    }

    default void changeRotateAnimation(android.view.animation.Animation rotateExitAnimation, android.view.animation.Animation rotateEnterAnimation, android.view.animation.Animation rotateAlphaAnimation, android.content.Context context) {
    }

    default boolean enterAnimationinitialize(android.view.animation.Animation rotateEnterAnimation, int width, int height, int parentWidth, int parentHeight) {
        return false;
    }

    default boolean hookComputStartLumaForDismiss(int curRotation, int originalRotation, com.android.server.wm.DisplayContent mDisplayContent) {
        return false;
    }

    default void notifyScreenshotAnimationStart() {
    }

    default boolean startScreenRotateBackColorAnimation(float[] backColorRGBFloats, android.view.animation.Animation animation, android.view.SurfaceControl leash, com.android.server.wm.DisplayContent displayContent) {
        return false;
    }

    default float getWindowCornerRadius() {
        return 0.0f;
    }

    default boolean hookAdjustScreenshotInitialRotation(com.android.server.wm.LocalAnimationAdapter localAnimationAdapter, com.android.server.wm.SurfaceAnimator animator, int width, int height, boolean forceDefaultOrientation, com.android.server.wm.DisplayContent displayContent, android.view.SurfaceControl leash, int currentRotation) {
        return false;
    }

    default void setFrozenByUserSwitching(boolean isUserSwitching) {
    }

    default void adjustBlurBackgroundLayer() {
    }

    default boolean getDeviceFolding() {
        return false;
    }

    default void onScreenRotationAnimationEnd() {
    }

    default void updateAnimationForFolding(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl mBackColorSurface, android.view.SurfaceControl mScreenshotLayer, com.android.server.wm.DisplayContent displayContent, boolean isNeedShowAndApply) {
    }

    default android.window.ScreenCapture.ScreenshotHardwareBuffer getScreenshotHardwareBuffer() {
        return null;
    }

    default float getLuma(boolean startLuma) {
        return Float.MIN_VALUE;
    }
}
