package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class NormalBrightnessModeController {
    private java.util.Map<com.android.server.display.DisplayDeviceConfig.BrightnessLimitMapType, java.util.Map<java.lang.Float, java.lang.Float>> mMaxBrightnessLimits = new java.util.HashMap();
    private float mAmbientLux = Float.MAX_VALUE;
    private boolean mAutoBrightnessEnabled = false;
    private float mMaxBrightness = 1.0f;

    NormalBrightnessModeController() {
    }

    boolean onAmbientLuxChange(float ambientLux) {
        this.mAmbientLux = ambientLux;
        return recalculateMaxBrightness();
    }

    boolean setAutoBrightnessState(int state) {
        boolean isEnabled = state == 1;
        if (isEnabled == this.mAutoBrightnessEnabled) {
            return false;
        }
        this.mAutoBrightnessEnabled = isEnabled;
        return recalculateMaxBrightness();
    }

    float getCurrentBrightnessMax() {
        return this.mMaxBrightness;
    }

    boolean resetNbmData(java.util.Map<com.android.server.display.DisplayDeviceConfig.BrightnessLimitMapType, java.util.Map<java.lang.Float, java.lang.Float>> maxBrightnessLimits) {
        this.mMaxBrightnessLimits = maxBrightnessLimits;
        return recalculateMaxBrightness();
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("NormalBrightnessModeController:");
        pw.println("  mAutoBrightnessEnabled=" + this.mAutoBrightnessEnabled);
        pw.println("  mAmbientLux=" + this.mAmbientLux);
        pw.println("  mMaxBrightness=" + this.mMaxBrightness);
        pw.println("  mMaxBrightnessLimits=" + this.mMaxBrightnessLimits);
    }

    private boolean recalculateMaxBrightness() {
        float foundAmbientBoundary = Float.MAX_VALUE;
        float foundMaxBrightness = 1.0f;
        java.util.Map<java.lang.Float, java.lang.Float> maxBrightnessPoints = null;
        if (this.mAutoBrightnessEnabled) {
            java.util.Map<java.lang.Float, java.lang.Float> maxBrightnessPoints2 = this.mMaxBrightnessLimits.get(com.android.server.display.DisplayDeviceConfig.BrightnessLimitMapType.ADAPTIVE);
            maxBrightnessPoints = maxBrightnessPoints2;
        }
        if (this.mAutoBrightnessEnabled && maxBrightnessPoints == null) {
            java.util.Map<java.lang.Float, java.lang.Float> maxBrightnessPoints3 = this.mMaxBrightnessLimits.get(com.android.server.display.DisplayDeviceConfig.BrightnessLimitMapType.DEFAULT);
            maxBrightnessPoints = maxBrightnessPoints3;
        }
        if (maxBrightnessPoints != null) {
            for (java.util.Map.Entry<java.lang.Float, java.lang.Float> brightnessPoint : maxBrightnessPoints.entrySet()) {
                float ambientBoundary = brightnessPoint.getKey().floatValue();
                if (ambientBoundary > this.mAmbientLux && ambientBoundary < foundAmbientBoundary) {
                    foundMaxBrightness = brightnessPoint.getValue().floatValue();
                    foundAmbientBoundary = ambientBoundary;
                }
            }
        }
        if (this.mMaxBrightness != foundMaxBrightness) {
            this.mMaxBrightness = foundMaxBrightness;
            return true;
        }
        return false;
    }
}
