package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class RampAnimator<T> {
    private static boolean mIsUpdateRealTimeBrightness = false;
    private boolean isPrimaryAnimator;
    private float mAnimatedValue;
    private boolean mAnimating;
    private float mAnimationDecreaseMaxTimeSecs;
    private float mAnimationIncreaseMaxTimeSecs;
    private final com.android.server.display.RampAnimator.Clock mClock;
    private float mCurrentValue;
    private int mDisplayId;
    private boolean mFirstTime;
    private long mLastFrameTimeNanos;
    private com.android.server.display.RampAnimator.Listener mListener;
    private final T mObject;
    private final android.util.FloatProperty<T> mProperty;
    private float mRate;
    private com.android.server.display.IRampAnimatorExt mRmpExt;
    private float mTargetHlgValue;
    private float mTargetLinearValue;

    interface Clock {
        long nanoTime();
    }

    public interface Listener {
        void onAnimationEnd(boolean z);

        void onAnimationStart(boolean z);
    }

    public RampAnimator(T object, android.util.FloatProperty<T> property, boolean primaryAnimator) {
        this(object, property, new com.android.server.display.RampAnimator.Clock() { // from class: com.android.server.display.RampAnimator$$ExternalSyntheticLambda0
            @Override // com.android.server.display.RampAnimator.Clock
            public final long nanoTime() {
                return java.lang.System.nanoTime();
            }
        });
        this.isPrimaryAnimator = primaryAnimator;
        android.util.Slog.d("RampAnimator", "construct animator, isPrimaryAnimator:" + this.isPrimaryAnimator);
    }

    public RampAnimator(T object, android.util.FloatProperty<T> property, com.android.server.display.RampAnimator.Clock clock) {
        this.mFirstTime = true;
        this.mRmpExt = (com.android.server.display.IRampAnimatorExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IRampAnimatorExt.class).base(this).create();
        this.mDisplayId = 0;
        this.isPrimaryAnimator = true;
        this.mObject = object;
        this.mProperty = property;
        this.mClock = clock;
    }

    void setAnimationTimeLimits(long animationRampIncreaseMaxTimeMillis, long animationRampDecreaseMaxTimeMillis) {
        this.mAnimationIncreaseMaxTimeSecs = animationRampIncreaseMaxTimeMillis > 0 ? animationRampIncreaseMaxTimeMillis / 1000.0f : 0.0f;
        this.mAnimationDecreaseMaxTimeSecs = animationRampDecreaseMaxTimeMillis > 0 ? animationRampDecreaseMaxTimeMillis / 1000.0f : 0.0f;
    }

    boolean setAnimationTarget(float targetLinear, float rate, boolean ignoreAnimationLimits) {
        float maxIncreaseTimeSecs = ignoreAnimationLimits ? 0.0f : this.mAnimationIncreaseMaxTimeSecs;
        float maxDecreaseTimeSecs = ignoreAnimationLimits ? 0.0f : this.mAnimationDecreaseMaxTimeSecs;
        return setAnimationTarget(targetLinear, rate, maxIncreaseTimeSecs, maxDecreaseTimeSecs);
    }

    private boolean setAnimationTarget(float targetLinear, float rate, float maxIncreaseTimeSecs, float maxDecreaseTimeSecs) {
        boolean isDolbyNoAnimation = this.mRmpExt.getEdrHdrBrightnessNoAnimation();
        if (this.isPrimaryAnimator) {
            mIsUpdateRealTimeBrightness = this.mRmpExt.updateRealTimeBrightness(this.mDisplayId, targetLinear != this.mTargetHlgValue, targetLinear);
            this.mFirstTime = isDolbyNoAnimation || mIsUpdateRealTimeBrightness || this.mFirstTime;
        } else {
            this.mFirstTime = this.mRmpExt.getEdrSdrBrightnessNoAnimation() || mIsUpdateRealTimeBrightness;
        }
        if (this.mFirstTime || rate <= 0.0f) {
            boolean changed = this.mFirstTime;
            if (!changed && targetLinear == this.mCurrentValue) {
                return false;
            }
            this.mFirstTime = false;
            this.mRate = 0.0f;
            this.mTargetHlgValue = targetLinear;
            this.mTargetLinearValue = targetLinear;
            this.mCurrentValue = targetLinear;
            this.mAnimatedValue = targetLinear;
            setPropertyValue(targetLinear);
            if (this.mAnimating) {
                this.mAnimating = false;
            }
            if (this.mListener != null) {
                this.mListener.onAnimationEnd(this.isPrimaryAnimator);
            }
            this.mRmpExt.animateRun(this.mCurrentValue, this.mAnimatedValue, this.mTargetHlgValue, this.mRate, true);
            return true;
        }
        if (targetLinear > this.mCurrentValue && maxIncreaseTimeSecs > 0.0f && (targetLinear - this.mCurrentValue) / rate > maxIncreaseTimeSecs) {
            rate = (targetLinear - this.mCurrentValue) / maxIncreaseTimeSecs;
        } else if (targetLinear < this.mCurrentValue && maxDecreaseTimeSecs > 0.0f && (this.mCurrentValue - targetLinear) / rate > maxDecreaseTimeSecs) {
            rate = (this.mCurrentValue - targetLinear) / maxDecreaseTimeSecs;
        }
        if (!this.mAnimating || rate > this.mRate || ((targetLinear <= this.mCurrentValue && this.mCurrentValue <= this.mTargetHlgValue) || (this.mTargetHlgValue <= this.mCurrentValue && this.mCurrentValue <= targetLinear))) {
            this.mRate = rate;
        }
        if (this.mAnimating && targetLinear != this.mTargetHlgValue) {
            this.mRmpExt.onAnimatingStart(this.mTargetHlgValue, targetLinear);
        }
        boolean changed2 = this.mTargetHlgValue != targetLinear;
        this.mTargetHlgValue = targetLinear;
        this.mTargetLinearValue = targetLinear;
        if (!this.mAnimating && targetLinear != this.mCurrentValue) {
            this.mRmpExt.onAnimationStart(this.mCurrentValue, targetLinear);
            if (this.mListener != null) {
                this.mListener.onAnimationStart(this.isPrimaryAnimator);
            }
            this.mAnimating = true;
            this.mAnimatedValue = this.mCurrentValue;
        }
        return changed2;
    }

    public void setListener(com.android.server.display.RampAnimator.Listener listener) {
        this.mListener = listener;
    }

    boolean isAnimating() {
        return this.mAnimating;
    }

    private void setPropertyValue(float val) {
        this.mRmpExt.setValue(this.mDisplayId, val);
        this.mProperty.setValue(this.mObject, val);
    }

    void performNextAnimationStep(long frameTimeNanos) {
        if (this.mRmpExt.isVsyncHalfOpt(frameTimeNanos) || this.mRmpExt.isDualRampOpt(this.mCurrentValue, this.mTargetHlgValue)) {
            return;
        }
        float timeDelta = (frameTimeNanos - this.mLastFrameTimeNanos) * 1.0E-9f;
        this.mLastFrameTimeNanos = frameTimeNanos;
        float scale = this.mRmpExt.getBrightnessNoAnimation() ? 0.0f : 1.0f;
        if (scale == 0.0f) {
            this.mAnimatedValue = this.mTargetHlgValue;
            this.mRmpExt.animateRun(this.mCurrentValue, this.mAnimatedValue, this.mTargetHlgValue, scale, true);
        } else {
            float amount = this.mRmpExt.getAmount(this.mDisplayId, this.mTargetHlgValue, this.mAnimatedValue, timeDelta, this.mRate, (this.mRate * timeDelta) / scale, this.isPrimaryAnimator);
            if (this.mTargetHlgValue > this.mCurrentValue) {
                this.mAnimatedValue = java.lang.Math.min(this.mAnimatedValue + amount, this.mTargetHlgValue);
            } else {
                this.mAnimatedValue = java.lang.Math.max(this.mAnimatedValue - amount, this.mTargetHlgValue);
            }
            this.mRmpExt.animateRun(this.mCurrentValue, this.mAnimatedValue, this.mTargetHlgValue, amount, false);
        }
        float amount2 = this.mCurrentValue;
        this.mCurrentValue = this.mAnimatedValue;
        boolean z = true;
        boolean needSetProperty = !com.android.internal.display.BrightnessSynchronizer.floatEquals(amount2, this.mCurrentValue);
        if (this.mRmpExt.isDualRampOptSupport()) {
            boolean animationEnd = com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mTargetHlgValue, this.mCurrentValue);
            if (java.lang.Math.round(amount2) == java.lang.Math.round(this.mCurrentValue) && !animationEnd) {
                z = false;
            }
            needSetProperty = z;
        }
        if (needSetProperty) {
            setPropertyValue(this.mCurrentValue);
        }
        this.mRmpExt.updateCurrentValue(this.mAnimatedValue, this.mCurrentValue);
        if (this.mAnimating && com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mTargetHlgValue, this.mCurrentValue)) {
            this.mAnimating = false;
            if (this.mListener != null) {
                this.mListener.onAnimationEnd(this.isPrimaryAnimator);
            }
            this.mRmpExt.onAnimationEnd(this.mCurrentValue);
        }
    }

    public void setDisplayId(int displayId, boolean isMainAnimator, boolean isPrimaryDisplay, com.android.server.display.RampAnimator<T> otherRamp) {
        this.mDisplayId = displayId;
        if (this.mRmpExt != null) {
            this.mRmpExt.setDisplayId(displayId, isMainAnimator, isPrimaryDisplay, otherRamp);
        }
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        if (this.mRmpExt != null) {
            this.mRmpExt.setLoggingEnabled(loggingEnabled);
        }
    }

    public boolean animateToByUser(float linearFirstTarget, float linearSecondTarget, float rate, boolean byUser) {
        if (this.mRmpExt != null) {
            this.mRmpExt.animateTo(this.mDisplayId, linearFirstTarget, linearSecondTarget, rate, byUser);
            return false;
        }
        return false;
    }

    public void setLastFrameTimeNanos(long time) {
        this.mLastFrameTimeNanos = time;
    }

    public void updateCurrentValue(float animateValue, float currentValue) {
        this.mAnimatedValue = animateValue;
        this.mCurrentValue = currentValue;
        this.mRmpExt.setValue(this.mDisplayId, this.mCurrentValue);
    }

    static class DualRampAnimator<T> {
        private boolean mAwaitingCallback;
        private final com.android.server.display.RampAnimator<T> mFirst;
        private com.android.server.display.RampAnimator.Listener mListener;
        private final com.android.server.display.RampAnimator<T> mSecond;
        private final java.lang.Runnable mAnimationCallback = new java.lang.Runnable() { // from class: com.android.server.display.RampAnimator.DualRampAnimator.1
            @Override // java.lang.Runnable
            public void run() {
                long frameTimeNanos = com.android.server.display.RampAnimator.DualRampAnimator.this.mChoreographer.getFrameTimeNanos();
                com.android.server.display.RampAnimator.DualRampAnimator.this.mFirst.performNextAnimationStep(frameTimeNanos);
                com.android.server.display.RampAnimator.DualRampAnimator.this.mSecond.performNextAnimationStep(frameTimeNanos);
                if (com.android.server.display.RampAnimator.DualRampAnimator.this.isAnimating()) {
                    com.android.server.display.RampAnimator.DualRampAnimator.this.postAnimationCallback();
                } else {
                    com.android.server.display.RampAnimator.Listener unused = com.android.server.display.RampAnimator.DualRampAnimator.this.mListener;
                    com.android.server.display.RampAnimator.DualRampAnimator.this.mAwaitingCallback = false;
                }
            }
        };
        private final android.view.Choreographer mChoreographer = android.view.Choreographer.getInstance();

        DualRampAnimator(T object, android.util.FloatProperty<T> firstProperty, android.util.FloatProperty<T> secondProperty) {
            this.mFirst = new com.android.server.display.RampAnimator<>((java.lang.Object) object, (android.util.FloatProperty) firstProperty, true);
            this.mSecond = new com.android.server.display.RampAnimator<>((java.lang.Object) object, (android.util.FloatProperty) secondProperty, false);
        }

        public void setAnimationTimeLimits(long animationRampIncreaseMaxTimeMillis, long animationRampDecreaseMaxTimeMillis) {
            this.mFirst.setAnimationTimeLimits(animationRampIncreaseMaxTimeMillis, animationRampDecreaseMaxTimeMillis);
            this.mSecond.setAnimationTimeLimits(animationRampIncreaseMaxTimeMillis, animationRampDecreaseMaxTimeMillis);
        }

        public boolean animateTo(float linearFirstTarget, float linearSecondTarget, float rate, boolean ignoreAnimationLimits) {
            boolean animationTargetChanged = this.mFirst.setAnimationTarget(linearFirstTarget, rate, ignoreAnimationLimits);
            boolean animationTargetChanged2 = animationTargetChanged | this.mSecond.setAnimationTarget(linearSecondTarget, rate, ignoreAnimationLimits);
            boolean shouldBeAnimating = isAnimating();
            if (shouldBeAnimating != this.mAwaitingCallback) {
                if (shouldBeAnimating) {
                    long lastFrameTimeNanos = java.lang.System.nanoTime();
                    this.mFirst.setLastFrameTimeNanos(lastFrameTimeNanos);
                    this.mSecond.setLastFrameTimeNanos(lastFrameTimeNanos);
                    this.mAwaitingCallback = true;
                    postAnimationCallback();
                } else if (this.mAwaitingCallback) {
                    this.mChoreographer.removeCallbacks(1, this.mAnimationCallback, null);
                    this.mAwaitingCallback = false;
                }
            }
            return animationTargetChanged2;
        }

        public void setListener(com.android.server.display.RampAnimator.Listener listener) {
            this.mListener = listener;
            this.mFirst.setListener(this.mListener);
            this.mSecond.setListener(this.mListener);
        }

        public boolean isAnimating() {
            return this.mFirst.isAnimating() || this.mSecond.isAnimating();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void postAnimationCallback() {
            this.mChoreographer.postCallback(1, this.mAnimationCallback, null);
        }

        public void setDisplayId(int displayId, boolean isPrimaryDisplay) {
            this.mFirst.setDisplayId(displayId, true, isPrimaryDisplay, this.mSecond);
            this.mSecond.setDisplayId(displayId, false, isPrimaryDisplay, this.mFirst);
        }

        public void setLoggingEnabled(boolean loggingEnabled) {
            this.mFirst.setLoggingEnabled(loggingEnabled);
            this.mSecond.setLoggingEnabled(loggingEnabled);
        }

        public boolean animateToByUser(float linearFirstTarget, float linearSecondTarget, float rate, boolean byUser) {
            boolean first = this.mFirst.animateToByUser(linearFirstTarget, linearSecondTarget, rate, byUser);
            this.mSecond.animateToByUser(linearFirstTarget, linearSecondTarget, rate, byUser);
            return first;
        }
    }
}
