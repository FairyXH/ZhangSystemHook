package com.android.server.display.brightness.clamper;

/* JADX INFO: loaded from: classes2.dex */
public class HdrClamper {
    private float mAmbientLux;
    private boolean mAutoBrightnessEnabled;
    private final com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener mClamperChangeListener;
    private final java.lang.Runnable mDebouncer;
    private float mDesiredMaxBrightness;
    private float mDesiredTransitionRate;
    private final android.os.Handler mHandler;
    private com.android.server.display.config.HdrBrightnessData mHdrBrightnessData;
    private final com.android.server.display.brightness.clamper.HdrClamper.HdrLayerInfoListener mHdrListener;
    private boolean mHdrVisible;
    private float mMaxBrightness;
    private android.os.IBinder mRegisteredDisplayToken;
    private float mTransitionRate;
    private boolean mUseSlowTransition;

    @java.lang.FunctionalInterface
    interface HdrListener {
        void onHdrVisible(boolean z);
    }

    public HdrClamper(com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener clamperChangeListener, android.os.Handler handler) {
        this(clamperChangeListener, handler, new com.android.server.display.brightness.clamper.HdrClamper.Injector());
    }

    public HdrClamper(com.android.server.display.brightness.clamper.BrightnessClamperController.ClamperChangeListener clamperChangeListener, android.os.Handler handler, com.android.server.display.brightness.clamper.HdrClamper.Injector injector) {
        this.mHdrBrightnessData = null;
        this.mRegisteredDisplayToken = null;
        this.mAmbientLux = Float.MAX_VALUE;
        this.mHdrVisible = false;
        this.mMaxBrightness = 1.0f;
        this.mDesiredMaxBrightness = 1.0f;
        this.mTransitionRate = -1.0f;
        this.mDesiredTransitionRate = -1.0f;
        this.mAutoBrightnessEnabled = false;
        this.mUseSlowTransition = false;
        this.mClamperChangeListener = clamperChangeListener;
        this.mHandler = handler;
        this.mDebouncer = new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.HdrClamper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
        this.mHdrListener = injector.getHdrListener(new com.android.server.display.brightness.clamper.HdrClamper.HdrListener() { // from class: com.android.server.display.brightness.clamper.HdrClamper$$ExternalSyntheticLambda1
            @Override // com.android.server.display.brightness.clamper.HdrClamper.HdrListener
            public final void onHdrVisible(boolean z) {
                this.f$0.lambda$new$1(z);
            }
        }, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mTransitionRate = this.mDesiredTransitionRate;
        this.mMaxBrightness = this.mDesiredMaxBrightness;
        this.mUseSlowTransition = true;
        this.mClamperChangeListener.onChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(boolean visible) {
        this.mHdrVisible = visible;
        recalculateBrightnessCap(this.mHdrBrightnessData, this.mAmbientLux, this.mHdrVisible);
    }

    public float clamp(float brightness) {
        return java.lang.Math.min(brightness, this.mMaxBrightness);
    }

    public float getMaxBrightness() {
        return this.mMaxBrightness;
    }

    public float getTransitionRate() {
        float expectedTransitionRate = this.mUseSlowTransition ? this.mTransitionRate : -1.0f;
        this.mUseSlowTransition = false;
        return expectedTransitionRate;
    }

    public void onAmbientLuxChange(float ambientLux) {
        this.mAmbientLux = ambientLux;
        recalculateBrightnessCap(this.mHdrBrightnessData, ambientLux, this.mHdrVisible);
    }

    public void resetHdrConfig(com.android.server.display.config.HdrBrightnessData data, int width, int height, float minimumHdrPercentOfScreen, android.os.IBinder displayToken) {
        this.mHdrBrightnessData = data;
        this.mHdrListener.mHdrMinPixels = width * height * minimumHdrPercentOfScreen;
        if (displayToken != this.mRegisteredDisplayToken) {
            if (this.mRegisteredDisplayToken != null) {
                this.mHdrListener.unregister(this.mRegisteredDisplayToken);
                this.mHdrVisible = false;
                this.mRegisteredDisplayToken = null;
            }
            if (displayToken != null && this.mHdrListener.mHdrMinPixels >= 0.0f) {
                this.mHdrListener.register(displayToken);
                this.mRegisteredDisplayToken = displayToken;
            }
        }
        recalculateBrightnessCap(data, this.mAmbientLux, this.mHdrVisible);
    }

    public void setAutoBrightnessState(int state) {
        boolean isEnabled = state == 1;
        if (isEnabled != this.mAutoBrightnessEnabled) {
            this.mAutoBrightnessEnabled = isEnabled;
            recalculateBrightnessCap(this.mHdrBrightnessData, this.mAmbientLux, this.mHdrVisible);
        }
    }

    public void stop() {
        if (this.mRegisteredDisplayToken != null) {
            this.mHdrListener.unregister(this.mRegisteredDisplayToken);
        }
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("HdrClamper:");
        pw.println("  mMaxBrightness=" + this.mMaxBrightness);
        pw.println("  mDesiredMaxBrightness=" + this.mDesiredMaxBrightness);
        pw.println("  mTransitionRate=" + this.mTransitionRate);
        pw.println("  mDesiredTransitionRate=" + this.mDesiredTransitionRate);
        pw.println("  mHdrVisible=" + this.mHdrVisible);
        pw.println("  mHdrListener.mHdrMinPixels=" + this.mHdrListener.mHdrMinPixels);
        pw.println("  mHdrBrightnessData=" + (this.mHdrBrightnessData == null ? "null" : this.mHdrBrightnessData.toString()));
        pw.println("  mHdrListener registered=" + (this.mRegisteredDisplayToken != null));
        pw.println("  mAmbientLux=" + this.mAmbientLux);
        pw.println("  mAutoBrightnessEnabled=" + this.mAutoBrightnessEnabled);
    }

    private void reset() {
        if (this.mMaxBrightness == 1.0f && this.mDesiredMaxBrightness == 1.0f && this.mTransitionRate == -1.0f && this.mDesiredTransitionRate == -1.0f) {
            return;
        }
        this.mHandler.removeCallbacks(this.mDebouncer);
        this.mMaxBrightness = 1.0f;
        this.mDesiredMaxBrightness = 1.0f;
        this.mDesiredTransitionRate = -1.0f;
        this.mTransitionRate = -1.0f;
        this.mUseSlowTransition = false;
        this.mClamperChangeListener.onChanged();
    }

    private void recalculateBrightnessCap(com.android.server.display.config.HdrBrightnessData data, float ambientLux, boolean hdrVisible) {
        long debounceTime;
        if (data == null || !hdrVisible || !this.mAutoBrightnessEnabled) {
            reset();
            return;
        }
        float expectedMaxBrightness = findBrightnessLimit(data, ambientLux);
        if (this.mMaxBrightness == expectedMaxBrightness) {
            this.mDesiredMaxBrightness = this.mMaxBrightness;
            this.mDesiredTransitionRate = -1.0f;
            this.mTransitionRate = -1.0f;
            this.mHandler.removeCallbacks(this.mDebouncer);
            return;
        }
        if (this.mDesiredMaxBrightness != expectedMaxBrightness) {
            this.mDesiredMaxBrightness = expectedMaxBrightness;
            if (this.mDesiredMaxBrightness > this.mMaxBrightness) {
                debounceTime = this.mHdrBrightnessData.mBrightnessIncreaseDebounceMillis;
                this.mDesiredTransitionRate = this.mHdrBrightnessData.mScreenBrightnessRampIncrease;
            } else {
                debounceTime = this.mHdrBrightnessData.mBrightnessDecreaseDebounceMillis;
                this.mDesiredTransitionRate = this.mHdrBrightnessData.mScreenBrightnessRampDecrease;
            }
            this.mHandler.removeCallbacks(this.mDebouncer);
            this.mHandler.postDelayed(this.mDebouncer, debounceTime);
        }
    }

    private float findBrightnessLimit(com.android.server.display.config.HdrBrightnessData data, float ambientLux) {
        float foundAmbientBoundary = Float.MAX_VALUE;
        float foundMaxBrightness = 1.0f;
        for (java.util.Map.Entry<java.lang.Float, java.lang.Float> brightnessPoint : data.mMaxBrightnessLimits.entrySet()) {
            float ambientBoundary = brightnessPoint.getKey().floatValue();
            if (ambientBoundary > ambientLux && ambientBoundary < foundAmbientBoundary) {
                foundMaxBrightness = brightnessPoint.getValue().floatValue();
                foundAmbientBoundary = ambientBoundary;
            }
        }
        return foundMaxBrightness;
    }

    static class HdrLayerInfoListener extends android.view.SurfaceControlHdrLayerInfoListener {
        private final android.os.Handler mHandler;
        private final com.android.server.display.brightness.clamper.HdrClamper.HdrListener mHdrListener;
        private float mHdrMinPixels = Float.MAX_VALUE;

        HdrLayerInfoListener(com.android.server.display.brightness.clamper.HdrClamper.HdrListener hdrListener, android.os.Handler handler) {
            this.mHdrListener = hdrListener;
            this.mHandler = handler;
        }

        public void onHdrInfoChanged(android.os.IBinder displayToken, final int numberOfHdrLayers, final int maxW, final int maxH, int flags, float maxDesiredHdrSdrRatio) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.brightness.clamper.HdrClamper$HdrLayerInfoListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onHdrInfoChanged$0(numberOfHdrLayers, maxW, maxH);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHdrInfoChanged$0(int numberOfHdrLayers, int maxW, int maxH) {
            this.mHdrListener.onHdrVisible(numberOfHdrLayers > 0 && ((float) (maxW * maxH)) >= this.mHdrMinPixels);
        }
    }

    static class Injector {
        Injector() {
        }

        com.android.server.display.brightness.clamper.HdrClamper.HdrLayerInfoListener getHdrListener(com.android.server.display.brightness.clamper.HdrClamper.HdrListener hdrListener, android.os.Handler handler) {
            return new com.android.server.display.brightness.clamper.HdrClamper.HdrLayerInfoListener(hdrListener, handler);
        }
    }
}
