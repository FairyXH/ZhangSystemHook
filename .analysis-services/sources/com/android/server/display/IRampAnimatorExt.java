package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IRampAnimatorExt<T> {
    default float getAmount(int displayId, float target, float animatedValue, float timeDelta, float rate, float amount, boolean isPrimaryAnimator) {
        return 0.0f;
    }

    default void setBrightnessNoAnimation(boolean noAnimation) {
    }

    default boolean getBrightnessNoAnimation() {
        return false;
    }

    default boolean updateRealTimeBrightness(int displayId, boolean changed, float target) {
        return false;
    }

    default void animateRun(float currentValue, float animatedValue, float targetValue, float amount, boolean immediately) {
    }

    default boolean isVsyncHalfOpt(long frameTimeNanos) {
        return false;
    }

    default void setDisplayId(int displayId, boolean isMainAnimator, boolean isPrimaryDisplay, com.android.server.display.RampAnimator<T> otherRamp) {
    }

    default boolean getEdrHdrBrightnessNoAnimation() {
        return false;
    }

    default void setValue(int displayId, float currentValue) {
    }

    default boolean animateTo(int displayId, float linearFirstTarget, float linearSecondTarget, float rate, boolean byUser) {
        return false;
    }

    default boolean isDualRampOptSupport() {
        return false;
    }

    default boolean isDualRampOpt(float currentValue, float targetValue) {
        return false;
    }

    default void updateCurrentValue(float animateValue, float currentValue) {
    }

    default void onAnimatingStart(float oldTarget, float newTarget) {
    }

    default void onAnimationStart(float current, float target) {
    }

    default void onAnimationEnd(float current) {
    }

    default void setLoggingEnabled(boolean loggingEnabled) {
    }

    default boolean getEdrSdrBrightnessNoAnimation() {
        return false;
    }
}
