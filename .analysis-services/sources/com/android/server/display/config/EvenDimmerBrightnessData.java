package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class EvenDimmerBrightnessData {
    private static final java.lang.String TAG = "EvenDimmerBrightnessData";
    public final float[] mBacklight;
    public final android.util.Spline mBacklightToBrightness;
    public final android.util.Spline mBacklightToNits;
    public final float[] mBrightness;
    public final android.util.Spline mBrightnessToBacklight;
    public final android.util.Spline mMinLuxToNits;
    public final float[] mNits;
    public final android.util.Spline mNitsToBacklight;
    public final float mTransitionPoint;

    public EvenDimmerBrightnessData(float transitionPoint, float[] nits, float[] backlight, float[] brightness, android.util.Spline backlightToNits, android.util.Spline nitsToBacklight, android.util.Spline brightnessToBacklight, android.util.Spline backlightToBrightness, android.util.Spline minLuxToNits) {
        this.mTransitionPoint = transitionPoint;
        this.mNits = nits;
        this.mBacklight = backlight;
        this.mBrightness = brightness;
        this.mBacklightToNits = backlightToNits;
        this.mNitsToBacklight = nitsToBacklight;
        this.mBrightnessToBacklight = brightnessToBacklight;
        this.mBacklightToBrightness = backlightToBrightness;
        this.mMinLuxToNits = minLuxToNits;
    }

    public java.lang.String toString() {
        return "EvenDimmerBrightnessData {mTransitionPoint: " + this.mTransitionPoint + ", mNits: " + java.util.Arrays.toString(this.mNits) + ", mBacklight: " + java.util.Arrays.toString(this.mBacklight) + ", mBrightness: " + java.util.Arrays.toString(this.mBrightness) + ", mBacklightToNits: " + this.mBacklightToNits + ", mNitsToBacklight: " + this.mNitsToBacklight + ", mBrightnessToBacklight: " + this.mBrightnessToBacklight + ", mBacklightToBrightness: " + this.mBacklightToBrightness + ", mMinLuxToNits: " + this.mMinLuxToNits + "} ";
    }

    public static com.android.server.display.config.EvenDimmerBrightnessData loadConfig(com.android.server.display.config.DisplayConfiguration config) {
        com.android.server.display.config.ComprehensiveBrightnessMap map;
        com.android.server.display.config.EvenDimmerMode lbm;
        java.util.List<com.android.server.display.config.Point> points;
        com.android.server.display.config.EvenDimmerMode lbm2 = config.getEvenDimmer();
        if (lbm2 == null) {
            return null;
        }
        boolean lbmIsEnabled = lbm2.getEnabled();
        if (!lbmIsEnabled || (map = lbm2.getBrightnessMapping()) == null) {
            return null;
        }
        java.lang.String interpolation = map.getInterpolation();
        java.util.List<com.android.server.display.config.BrightnessPoint> brightnessPoints = map.getBrightnessPoint();
        if (brightnessPoints.isEmpty()) {
            return null;
        }
        float[] nits = new float[brightnessPoints.size()];
        float[] backlight = new float[brightnessPoints.size()];
        float[] brightness = new float[brightnessPoints.size()];
        for (int i = 0; i < brightnessPoints.size(); i++) {
            com.android.server.display.config.BrightnessPoint val = brightnessPoints.get(i);
            nits[i] = val.getNits().floatValue();
            backlight[i] = val.getBacklight().floatValue();
            brightness[i] = val.getBrightness().floatValue();
        }
        float transitionPoint = lbm2.getTransitionPoint().floatValue();
        com.android.server.display.config.NitsMap minimumNitsMap = lbm2.getLuxToMinimumNitsMap();
        if (minimumNitsMap == null) {
            android.util.Slog.e(TAG, "Invalid min lux to nits mapping");
            return null;
        }
        java.util.List<com.android.server.display.config.Point> points2 = minimumNitsMap.getPoint();
        int size = points2.size();
        float[] minLux = new float[size];
        float[] minNits = new float[size];
        int i2 = 0;
        for (com.android.server.display.config.Point point : points2) {
            minLux[i2] = point.getValue().floatValue();
            minNits[i2] = point.getNits().floatValue();
            if (i2 > 0) {
                lbm = lbm2;
                if (minLux[i2] < minLux[i2 - 1]) {
                    points = points2;
                    android.util.Slog.e(TAG, "minLuxToNitsSpline must be non-decreasing, ignoring rest  of configuration. Value: " + minLux[i2] + " < " + minLux[i2 - 1]);
                } else {
                    points = points2;
                }
                if (minNits[i2] < minNits[i2 - 1]) {
                    android.util.Slog.e(TAG, "minLuxToNitsSpline must be non-decreasing, ignoring rest  of configuration. Nits: " + minNits[i2] + " < " + minNits[i2 - 1]);
                }
            } else {
                lbm = lbm2;
                points = points2;
            }
            i2++;
            lbm2 = lbm;
            points2 = points;
        }
        if ("linear".equals(interpolation)) {
            return new com.android.server.display.config.EvenDimmerBrightnessData(transitionPoint, nits, backlight, brightness, new android.util.Spline.LinearSpline(backlight, nits), new android.util.Spline.LinearSpline(nits, backlight), new android.util.Spline.LinearSpline(brightness, backlight), new android.util.Spline.LinearSpline(backlight, brightness), new android.util.Spline.LinearSpline(minLux, minNits));
        }
        return new com.android.server.display.config.EvenDimmerBrightnessData(transitionPoint, nits, backlight, brightness, android.util.Spline.createSpline(backlight, nits), android.util.Spline.createSpline(nits, backlight), android.util.Spline.createSpline(brightness, backlight), android.util.Spline.createSpline(backlight, brightness), android.util.Spline.createSpline(minLux, minNits));
    }
}
