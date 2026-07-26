package com.android.server.display.whitebalance;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayWhiteBalanceController implements com.android.server.display.whitebalance.AmbientSensor.AmbientBrightnessSensor.Callbacks, com.android.server.display.whitebalance.AmbientSensor.AmbientColorTemperatureSensor.Callbacks {
    private static final java.lang.String TAG = "DisplayWhiteBalanceController";
    private float mAmbientColorTemperature;
    private final com.android.server.display.utils.History mAmbientColorTemperatureHistory;
    private float mAmbientColorTemperatureOverride;
    private android.util.Spline.LinearSpline mAmbientToDisplayColorTemperatureSpline;
    com.android.server.display.utils.AmbientFilter mBrightnessFilter;
    private final com.android.server.display.whitebalance.AmbientSensor.AmbientBrightnessSensor mBrightnessSensor;
    private final com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal mColorDisplayServiceInternal;
    com.android.server.display.utils.AmbientFilter mColorTemperatureFilter;
    private final com.android.server.display.whitebalance.AmbientSensor.AmbientColorTemperatureSensor mColorTemperatureSensor;
    private com.android.server.display.whitebalance.DisplayWhiteBalanceController.Callbacks mDisplayPowerControllerCallbacks;
    private boolean mEnabled;
    private android.util.Spline.LinearSpline mHighLightAmbientBrightnessToBiasSpline;
    private android.util.Spline.LinearSpline mHighLightAmbientBrightnessToBiasSplineStrong;
    private final float mHighLightAmbientColorTemperature;
    private final float mHighLightAmbientColorTemperatureStrong;
    private float mLastAmbientColorTemperature;
    private float mLatestAmbientBrightness;
    private float mLatestAmbientColorTemperature;
    private float mLatestHighLightBias;
    private float mLatestLowLightBias;
    private final boolean mLightModeAllowed;
    private boolean mLoggingEnabled;
    private android.util.Spline.LinearSpline mLowLightAmbientBrightnessToBiasSpline;
    private android.util.Spline.LinearSpline mLowLightAmbientBrightnessToBiasSplineStrong;
    private final float mLowLightAmbientColorTemperature;
    private final float mLowLightAmbientColorTemperatureStrong;
    float mPendingAmbientColorTemperature;
    private android.util.Spline.LinearSpline mStrongAmbientToDisplayColorTemperatureSpline;
    private boolean mStrongModeEnabled;
    private final com.android.server.display.whitebalance.DisplayWhiteBalanceThrottler mThrottler;

    public interface Callbacks {
        void updateWhiteBalance();
    }

    public DisplayWhiteBalanceController(com.android.server.display.whitebalance.AmbientSensor.AmbientBrightnessSensor brightnessSensor, com.android.server.display.utils.AmbientFilter brightnessFilter, com.android.server.display.whitebalance.AmbientSensor.AmbientColorTemperatureSensor colorTemperatureSensor, com.android.server.display.utils.AmbientFilter colorTemperatureFilter, com.android.server.display.whitebalance.DisplayWhiteBalanceThrottler throttler, float[] lowLightAmbientBrightnesses, float[] lowLightAmbientBrightnessesStrong, float[] lowLightAmbientBiases, float[] lowLightAmbientBiasesStrong, float lowLightAmbientColorTemperature, float lowLightAmbientColorTemperatureStrong, float[] highLightAmbientBrightnesses, float[] highLightAmbientBrightnessesStrong, float[] highLightAmbientBiases, float[] highLightAmbientBiasesStrong, float highLightAmbientColorTemperature, float highLightAmbientColorTemperatureStrong, float[] ambientColorTemperatures, float[] displayColorTemperatures, float[] strongAmbientColorTemperatures, float[] strongDisplayColorTemperatures, boolean lightModeAllowed) {
        validateArguments(brightnessSensor, brightnessFilter, colorTemperatureSensor, colorTemperatureFilter, throttler);
        this.mBrightnessSensor = brightnessSensor;
        this.mBrightnessFilter = brightnessFilter;
        this.mColorTemperatureSensor = colorTemperatureSensor;
        this.mColorTemperatureFilter = colorTemperatureFilter;
        this.mThrottler = throttler;
        this.mLowLightAmbientColorTemperature = lowLightAmbientColorTemperature;
        this.mLowLightAmbientColorTemperatureStrong = lowLightAmbientColorTemperatureStrong;
        this.mHighLightAmbientColorTemperature = highLightAmbientColorTemperature;
        this.mHighLightAmbientColorTemperatureStrong = highLightAmbientColorTemperatureStrong;
        this.mAmbientColorTemperature = -1.0f;
        this.mPendingAmbientColorTemperature = -1.0f;
        this.mLastAmbientColorTemperature = -1.0f;
        this.mAmbientColorTemperatureHistory = new com.android.server.display.utils.History(50);
        this.mAmbientColorTemperatureOverride = -1.0f;
        this.mLightModeAllowed = lightModeAllowed;
        try {
        } catch (java.lang.Exception e) {
            e = e;
        }
        try {
            this.mLowLightAmbientBrightnessToBiasSpline = new android.util.Spline.LinearSpline(lowLightAmbientBrightnesses, lowLightAmbientBiases);
        } catch (java.lang.Exception e2) {
            e = e2;
            android.util.Slog.e(TAG, "failed to create low light ambient brightness to bias spline.", e);
            this.mLowLightAmbientBrightnessToBiasSpline = null;
        }
        if (this.mLowLightAmbientBrightnessToBiasSpline != null && (this.mLowLightAmbientBrightnessToBiasSpline.interpolate(0.0f) != 0.0f || this.mLowLightAmbientBrightnessToBiasSpline.interpolate(Float.POSITIVE_INFINITY) != 1.0f)) {
            android.util.Slog.d(TAG, "invalid low light ambient brightness to bias spline, bias must begin at 0.0 and end at 1.0.");
            this.mLowLightAmbientBrightnessToBiasSpline = null;
        }
        try {
        } catch (java.lang.Exception e3) {
            e = e3;
        }
        try {
            this.mLowLightAmbientBrightnessToBiasSplineStrong = new android.util.Spline.LinearSpline(lowLightAmbientBrightnessesStrong, lowLightAmbientBiasesStrong);
        } catch (java.lang.Exception e4) {
            e = e4;
            android.util.Slog.e(TAG, "failed to create strong low light ambient brightness to bias spline.", e);
            this.mLowLightAmbientBrightnessToBiasSplineStrong = null;
        }
        if (this.mLowLightAmbientBrightnessToBiasSplineStrong != null && (this.mLowLightAmbientBrightnessToBiasSplineStrong.interpolate(0.0f) != 0.0f || this.mLowLightAmbientBrightnessToBiasSplineStrong.interpolate(Float.POSITIVE_INFINITY) != 1.0f)) {
            android.util.Slog.d(TAG, "invalid strong low light ambient brightness to bias spline, bias must begin at 0.0 and end at 1.0.");
            this.mLowLightAmbientBrightnessToBiasSplineStrong = null;
        }
        try {
        } catch (java.lang.Exception e5) {
            e = e5;
        }
        try {
            this.mHighLightAmbientBrightnessToBiasSpline = new android.util.Spline.LinearSpline(highLightAmbientBrightnesses, highLightAmbientBiases);
        } catch (java.lang.Exception e6) {
            e = e6;
            android.util.Slog.e(TAG, "failed to create high light ambient brightness to bias spline.", e);
            this.mHighLightAmbientBrightnessToBiasSpline = null;
        }
        if (this.mHighLightAmbientBrightnessToBiasSpline != null && (this.mHighLightAmbientBrightnessToBiasSpline.interpolate(0.0f) != 0.0f || this.mHighLightAmbientBrightnessToBiasSpline.interpolate(Float.POSITIVE_INFINITY) != 1.0f)) {
            android.util.Slog.d(TAG, "invalid high light ambient brightness to bias spline, bias must begin at 0.0 and end at 1.0.");
            this.mHighLightAmbientBrightnessToBiasSpline = null;
        }
        try {
            try {
                this.mHighLightAmbientBrightnessToBiasSplineStrong = new android.util.Spline.LinearSpline(highLightAmbientBrightnessesStrong, highLightAmbientBiasesStrong);
            } catch (java.lang.Exception e7) {
                e = e7;
                android.util.Slog.e(TAG, "failed to create strong high light ambient brightness to bias spline.", e);
                this.mHighLightAmbientBrightnessToBiasSplineStrong = null;
            }
        } catch (java.lang.Exception e8) {
            e = e8;
        }
        if (this.mHighLightAmbientBrightnessToBiasSplineStrong != null && (this.mHighLightAmbientBrightnessToBiasSplineStrong.interpolate(0.0f) != 0.0f || this.mHighLightAmbientBrightnessToBiasSplineStrong.interpolate(Float.POSITIVE_INFINITY) != 1.0f)) {
            android.util.Slog.d(TAG, "invalid strong high light ambient brightness to bias spline, bias must begin at 0.0 and end at 1.0.");
            this.mHighLightAmbientBrightnessToBiasSplineStrong = null;
        }
        if (this.mLowLightAmbientBrightnessToBiasSpline != null && this.mHighLightAmbientBrightnessToBiasSpline != null && lowLightAmbientBrightnesses[lowLightAmbientBrightnesses.length - 1] > highLightAmbientBrightnesses[0]) {
            android.util.Slog.d(TAG, "invalid low light and high light ambient brightness to bias spline combination, defined domains must not intersect.");
            this.mLowLightAmbientBrightnessToBiasSpline = null;
            this.mHighLightAmbientBrightnessToBiasSpline = null;
        }
        if (this.mLowLightAmbientBrightnessToBiasSplineStrong != null && this.mHighLightAmbientBrightnessToBiasSplineStrong != null && lowLightAmbientBrightnessesStrong[lowLightAmbientBrightnessesStrong.length - 1] > highLightAmbientBrightnessesStrong[0]) {
            android.util.Slog.d(TAG, "invalid strong low light and high light ambient brightness to bias spline combination, defined domains must not intersect.");
            this.mLowLightAmbientBrightnessToBiasSplineStrong = null;
            this.mHighLightAmbientBrightnessToBiasSplineStrong = null;
        }
        try {
        } catch (java.lang.Exception e9) {
            e = e9;
        }
        try {
            this.mAmbientToDisplayColorTemperatureSpline = new android.util.Spline.LinearSpline(ambientColorTemperatures, displayColorTemperatures);
        } catch (java.lang.Exception e10) {
            e = e10;
            android.util.Slog.e(TAG, "failed to create ambient to display color temperature spline.", e);
            this.mAmbientToDisplayColorTemperatureSpline = null;
        }
        try {
        } catch (java.lang.Exception e11) {
            e = e11;
        }
        try {
            this.mStrongAmbientToDisplayColorTemperatureSpline = new android.util.Spline.LinearSpline(strongAmbientColorTemperatures, strongDisplayColorTemperatures);
        } catch (java.lang.Exception e12) {
            e = e12;
            android.util.Slog.e(TAG, "Failed to create strong ambient to display color temperature spline", e);
        }
        this.mColorDisplayServiceInternal = (com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal) com.android.server.LocalServices.getService(com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal.class);
    }

    public boolean setEnabled(boolean enabled) {
        if (enabled) {
            return enable();
        }
        return disable();
    }

    public void setStrongModeEnabled(boolean enabled) {
        this.mStrongModeEnabled = enabled;
        this.mColorDisplayServiceInternal.setDisplayWhiteBalanceAllowed(this.mLightModeAllowed || this.mStrongModeEnabled);
        if (this.mEnabled) {
            updateAmbientColorTemperature();
            updateDisplayColorTemperature();
        }
    }

    public boolean setCallbacks(com.android.server.display.whitebalance.DisplayWhiteBalanceController.Callbacks callbacks) {
        if (this.mDisplayPowerControllerCallbacks == callbacks) {
            return false;
        }
        this.mDisplayPowerControllerCallbacks = callbacks;
        return true;
    }

    public boolean setLoggingEnabled(boolean loggingEnabled) {
        if (this.mLoggingEnabled == loggingEnabled) {
            return false;
        }
        this.mLoggingEnabled = loggingEnabled;
        this.mBrightnessSensor.setLoggingEnabled(loggingEnabled);
        this.mBrightnessFilter.setLoggingEnabled(loggingEnabled);
        this.mColorTemperatureSensor.setLoggingEnabled(loggingEnabled);
        this.mColorTemperatureFilter.setLoggingEnabled(loggingEnabled);
        this.mThrottler.setLoggingEnabled(loggingEnabled);
        return true;
    }

    public boolean setAmbientColorTemperatureOverride(float ambientColorTemperatureOverride) {
        if (this.mAmbientColorTemperatureOverride == ambientColorTemperatureOverride) {
            return false;
        }
        this.mAmbientColorTemperatureOverride = ambientColorTemperatureOverride;
        return true;
    }

    public void dump(java.io.PrintWriter writer) {
        writer.println(TAG);
        writer.println("  mLoggingEnabled=" + this.mLoggingEnabled);
        writer.println("  mEnabled=" + this.mEnabled);
        writer.println("  mStrongModeEnabled=" + this.mStrongModeEnabled);
        writer.println("  mDisplayPowerControllerCallbacks=" + this.mDisplayPowerControllerCallbacks);
        this.mBrightnessSensor.dump(writer);
        this.mBrightnessFilter.dump(writer);
        this.mColorTemperatureSensor.dump(writer);
        this.mColorTemperatureFilter.dump(writer);
        this.mThrottler.dump(writer);
        writer.println("  mLowLightAmbientColorTemperature=" + this.mLowLightAmbientColorTemperature);
        writer.println("  mLowLightAmbientColorTemperatureStrong=" + this.mLowLightAmbientColorTemperatureStrong);
        writer.println("  mHighLightAmbientColorTemperature=" + this.mHighLightAmbientColorTemperature);
        writer.println("  mHighLightAmbientColorTemperatureStrong=" + this.mHighLightAmbientColorTemperatureStrong);
        writer.println("  mAmbientColorTemperature=" + this.mAmbientColorTemperature);
        writer.println("  mPendingAmbientColorTemperature=" + this.mPendingAmbientColorTemperature);
        writer.println("  mLastAmbientColorTemperature=" + this.mLastAmbientColorTemperature);
        writer.println("  mAmbientColorTemperatureHistory=" + this.mAmbientColorTemperatureHistory);
        writer.println("  mAmbientColorTemperatureOverride=" + this.mAmbientColorTemperatureOverride);
        writer.println("  mAmbientToDisplayColorTemperatureSpline=" + this.mAmbientToDisplayColorTemperatureSpline);
        writer.println("  mStrongAmbientToDisplayColorTemperatureSpline=" + this.mStrongAmbientToDisplayColorTemperatureSpline);
        writer.println("  mLowLightAmbientBrightnessToBiasSpline=" + this.mLowLightAmbientBrightnessToBiasSpline);
        writer.println("  mLowLightAmbientBrightnessToBiasSplineStrong=" + this.mLowLightAmbientBrightnessToBiasSplineStrong);
        writer.println("  mHighLightAmbientBrightnessToBiasSpline=" + this.mHighLightAmbientBrightnessToBiasSpline);
        writer.println("  mHighLightAmbientBrightnessToBiasSplineStrong=" + this.mHighLightAmbientBrightnessToBiasSplineStrong);
    }

    @Override // com.android.server.display.whitebalance.AmbientSensor.AmbientBrightnessSensor.Callbacks
    public void onAmbientBrightnessChanged(float value) {
        long time = java.lang.System.currentTimeMillis();
        this.mBrightnessFilter.addValue(time, value);
        updateAmbientColorTemperature();
    }

    @Override // com.android.server.display.whitebalance.AmbientSensor.AmbientColorTemperatureSensor.Callbacks
    public void onAmbientColorTemperatureChanged(float value) {
        long time = java.lang.System.currentTimeMillis();
        this.mColorTemperatureFilter.addValue(time, value);
        updateAmbientColorTemperature();
    }

    public void updateAmbientColorTemperature() {
        android.util.Spline.LinearSpline lowLightAmbientBrightnessToBiasSpline;
        android.util.Spline.LinearSpline highLightAmbientBrightnessToBiasSpline;
        long time = java.lang.System.currentTimeMillis();
        float lowLightAmbientColorTemperature = this.mStrongModeEnabled ? this.mLowLightAmbientColorTemperatureStrong : this.mLowLightAmbientColorTemperature;
        float highLightAmbientColorTemperature = this.mStrongModeEnabled ? this.mHighLightAmbientColorTemperatureStrong : this.mHighLightAmbientColorTemperature;
        if (this.mStrongModeEnabled) {
            lowLightAmbientBrightnessToBiasSpline = this.mLowLightAmbientBrightnessToBiasSplineStrong;
        } else {
            lowLightAmbientBrightnessToBiasSpline = this.mLowLightAmbientBrightnessToBiasSpline;
        }
        if (this.mStrongModeEnabled) {
            highLightAmbientBrightnessToBiasSpline = this.mHighLightAmbientBrightnessToBiasSplineStrong;
        } else {
            highLightAmbientBrightnessToBiasSpline = this.mHighLightAmbientBrightnessToBiasSpline;
        }
        float ambientColorTemperature = this.mColorTemperatureFilter.getEstimate(time);
        this.mLatestAmbientColorTemperature = ambientColorTemperature;
        if (this.mStrongModeEnabled) {
            if (this.mStrongAmbientToDisplayColorTemperatureSpline != null && ambientColorTemperature != -1.0f) {
                ambientColorTemperature = this.mStrongAmbientToDisplayColorTemperatureSpline.interpolate(ambientColorTemperature);
            }
        } else if (this.mAmbientToDisplayColorTemperatureSpline != null && ambientColorTemperature != -1.0f) {
            ambientColorTemperature = this.mAmbientToDisplayColorTemperatureSpline.interpolate(ambientColorTemperature);
        }
        float ambientBrightness = this.mBrightnessFilter.getEstimate(time);
        this.mLatestAmbientBrightness = ambientBrightness;
        if (ambientColorTemperature != -1.0f && ambientBrightness != -1.0f && lowLightAmbientBrightnessToBiasSpline != null) {
            float bias = lowLightAmbientBrightnessToBiasSpline.interpolate(ambientBrightness);
            ambientColorTemperature = (bias * ambientColorTemperature) + ((1.0f - bias) * lowLightAmbientColorTemperature);
            this.mLatestLowLightBias = bias;
        }
        if (ambientColorTemperature != -1.0f && ambientBrightness != -1.0f && highLightAmbientBrightnessToBiasSpline != null) {
            float bias2 = highLightAmbientBrightnessToBiasSpline.interpolate(ambientBrightness);
            ambientColorTemperature = ((1.0f - bias2) * ambientColorTemperature) + (bias2 * highLightAmbientColorTemperature);
            this.mLatestHighLightBias = bias2;
        }
        if (this.mAmbientColorTemperatureOverride != -1.0f) {
            if (this.mLoggingEnabled) {
                android.util.Slog.d(TAG, "override ambient color temperature: " + ambientColorTemperature + " => " + this.mAmbientColorTemperatureOverride);
            }
            ambientColorTemperature = this.mAmbientColorTemperatureOverride;
        }
        if (ambientColorTemperature == -1.0f || this.mThrottler.throttle(ambientColorTemperature)) {
            return;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "pending ambient color temperature: " + ambientColorTemperature);
        }
        this.mPendingAmbientColorTemperature = ambientColorTemperature;
        if (this.mDisplayPowerControllerCallbacks != null) {
            this.mDisplayPowerControllerCallbacks.updateWhiteBalance();
        }
    }

    public void updateDisplayColorTemperature() {
        float ambientColorTemperature = -1.0f;
        if (this.mAmbientColorTemperature == -1.0f && this.mPendingAmbientColorTemperature == -1.0f) {
            ambientColorTemperature = this.mLastAmbientColorTemperature;
        }
        if (this.mPendingAmbientColorTemperature != -1.0f && this.mPendingAmbientColorTemperature != this.mAmbientColorTemperature) {
            ambientColorTemperature = this.mPendingAmbientColorTemperature;
        }
        if (ambientColorTemperature == -1.0f) {
            return;
        }
        this.mAmbientColorTemperature = ambientColorTemperature;
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "ambient color temperature: " + this.mAmbientColorTemperature);
        }
        this.mPendingAmbientColorTemperature = -1.0f;
        this.mAmbientColorTemperatureHistory.add(this.mAmbientColorTemperature);
        android.util.Slog.d(TAG, "Display cct: " + this.mAmbientColorTemperature + " Latest ambient cct: " + this.mLatestAmbientColorTemperature + " Latest ambient lux: " + this.mLatestAmbientBrightness + " Latest low light bias: " + this.mLatestLowLightBias + " Latest high light bias: " + this.mLatestHighLightBias);
        this.mColorDisplayServiceInternal.setDisplayWhiteBalanceColorTemperature((int) this.mAmbientColorTemperature);
        this.mLastAmbientColorTemperature = this.mAmbientColorTemperature;
    }

    public float calculateAdjustedBrightnessNits(float requestedBrightnessNits) {
        float luminance = this.mColorDisplayServiceInternal.getDisplayWhiteBalanceLuminance();
        if (luminance == -1.0f) {
            return requestedBrightnessNits;
        }
        float effectiveBrightness = requestedBrightnessNits * luminance;
        return (requestedBrightnessNits - effectiveBrightness) + requestedBrightnessNits;
    }

    private void validateArguments(com.android.server.display.whitebalance.AmbientSensor.AmbientBrightnessSensor brightnessSensor, com.android.server.display.utils.AmbientFilter brightnessFilter, com.android.server.display.whitebalance.AmbientSensor.AmbientColorTemperatureSensor colorTemperatureSensor, com.android.server.display.utils.AmbientFilter colorTemperatureFilter, com.android.server.display.whitebalance.DisplayWhiteBalanceThrottler throttler) {
        java.util.Objects.requireNonNull(brightnessSensor, "brightnessSensor must not be null");
        java.util.Objects.requireNonNull(brightnessFilter, "brightnessFilter must not be null");
        java.util.Objects.requireNonNull(colorTemperatureSensor, "colorTemperatureSensor must not be null");
        java.util.Objects.requireNonNull(colorTemperatureFilter, "colorTemperatureFilter must not be null");
        java.util.Objects.requireNonNull(throttler, "throttler cannot be null");
    }

    private boolean enable() {
        if (this.mEnabled) {
            return false;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "enabling");
        }
        this.mEnabled = true;
        this.mBrightnessSensor.setEnabled(true);
        this.mColorTemperatureSensor.setEnabled(true);
        return true;
    }

    private boolean disable() {
        if (!this.mEnabled) {
            return false;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "disabling");
        }
        this.mEnabled = false;
        this.mBrightnessSensor.setEnabled(false);
        this.mBrightnessFilter.clear();
        this.mColorTemperatureSensor.setEnabled(false);
        this.mColorTemperatureFilter.clear();
        this.mThrottler.clear();
        this.mAmbientColorTemperature = -1.0f;
        this.mPendingAmbientColorTemperature = -1.0f;
        this.mColorDisplayServiceInternal.resetDisplayWhiteBalanceColorTemperature();
        return true;
    }
}
