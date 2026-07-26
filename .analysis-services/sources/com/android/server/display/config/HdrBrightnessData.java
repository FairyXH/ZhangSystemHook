package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class HdrBrightnessData {
    public final long mBrightnessDecreaseDebounceMillis;
    public final long mBrightnessIncreaseDebounceMillis;
    public final java.util.Map<java.lang.Float, java.lang.Float> mMaxBrightnessLimits;
    public final float mScreenBrightnessRampDecrease;
    public final float mScreenBrightnessRampIncrease;

    public HdrBrightnessData(java.util.Map<java.lang.Float, java.lang.Float> maxBrightnessLimits, long brightnessIncreaseDebounceMillis, float screenBrightnessRampIncrease, long brightnessDecreaseDebounceMillis, float screenBrightnessRampDecrease) {
        this.mMaxBrightnessLimits = maxBrightnessLimits;
        this.mBrightnessIncreaseDebounceMillis = brightnessIncreaseDebounceMillis;
        this.mScreenBrightnessRampIncrease = screenBrightnessRampIncrease;
        this.mBrightnessDecreaseDebounceMillis = brightnessDecreaseDebounceMillis;
        this.mScreenBrightnessRampDecrease = screenBrightnessRampDecrease;
    }

    public java.lang.String toString() {
        return "HdrBrightnessData {mMaxBrightnessLimits: " + this.mMaxBrightnessLimits + ", mBrightnessIncreaseDebounceMillis: " + this.mBrightnessIncreaseDebounceMillis + ", mScreenBrightnessRampIncrease: " + this.mScreenBrightnessRampIncrease + ", mBrightnessDecreaseDebounceMillis: " + this.mBrightnessDecreaseDebounceMillis + ", mScreenBrightnessRampDecrease: " + this.mScreenBrightnessRampDecrease + "} ";
    }

    public static com.android.server.display.config.HdrBrightnessData loadConfig(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.HdrBrightnessConfig hdrConfig = config.getHdrBrightnessConfig();
        if (hdrConfig == null) {
            return null;
        }
        java.util.List<com.android.server.display.config.NonNegativeFloatToFloatPoint> points = hdrConfig.getBrightnessMap().getPoint();
        java.util.Map<java.lang.Float, java.lang.Float> brightnessLimits = new java.util.HashMap<>();
        for (com.android.server.display.config.NonNegativeFloatToFloatPoint point : points) {
            brightnessLimits.put(java.lang.Float.valueOf(point.getFirst().floatValue()), java.lang.Float.valueOf(point.getSecond().floatValue()));
        }
        return new com.android.server.display.config.HdrBrightnessData(brightnessLimits, hdrConfig.getBrightnessIncreaseDebounceMillis().longValue(), hdrConfig.getScreenBrightnessRampIncrease().floatValue(), hdrConfig.getBrightnessDecreaseDebounceMillis().longValue(), hdrConfig.getScreenBrightnessRampDecrease().floatValue());
    }
}
