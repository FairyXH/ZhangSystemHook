package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class OplusPixelworksHelper {
    private final java.lang.String TAG = "OplusPixelworksHelper";
    private android.content.Context mContext;
    public static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final java.lang.String KEY_BRIGHTNESS_SMOOTH_SUPPORT = "ro.oplus.display.brightness.smooth";
    public static boolean sSupportBrightnessSmooth = android.os.SystemProperties.getBoolean(KEY_BRIGHTNESS_SMOOTH_SUPPORT, false);
    private static com.android.server.display.OplusPixelworksHelper sOplusPixelworksHelper = null;
    private static final java.lang.String KEY_MAX_BRIGHTNESS = "ro.oplus.display.brightness.max_brightness";
    private static final float sMaxBrightness = android.os.SystemProperties.getInt(KEY_MAX_BRIGHTNESS, 10238);
    private static final java.lang.String KEY_NORMAL_MAX_BRIGHTNESS = "ro.oplus.display.brightness.normal_max_brightness";
    private static final float sNormalMaxBrightness = android.os.SystemProperties.getInt(KEY_NORMAL_MAX_BRIGHTNESS, com.android.server.display.IOplusDisplayPowerControllerExt.MAX_BRIGHTNESS);
    private static final java.lang.String KEY_DEFAULT_BRIGHTNESS = "ro.oplus.display.brightness.default_brightness";
    private static final float sDefaultBrightness = android.os.SystemProperties.getInt(KEY_DEFAULT_BRIGHTNESS, 3758);
    private static final java.lang.String KEY_ENGINEERINGMODE_BRIGHTNESS_20P = "ro.display.brightness.mode.exp.per_20";
    private static final float sEngineeringMode20P = android.os.SystemProperties.getInt(KEY_ENGINEERINGMODE_BRIGHTNESS_20P, -1);

    public static com.android.server.display.OplusPixelworksHelper getInstance(android.content.Context context) {
        if (sOplusPixelworksHelper == null && sOplusPixelworksHelper == null) {
            sOplusPixelworksHelper = new com.android.server.display.OplusPixelworksHelper(context);
        }
        return sOplusPixelworksHelper;
    }

    public static float convertOverrideBrightness(float brightness, int uid) {
        return brightness;
    }

    private OplusPixelworksHelper(android.content.Context context) {
        this.mContext = context;
    }

    public static boolean isSupportBrightnessSmooth() {
        return sSupportBrightnessSmooth;
    }

    public static float getMaxBrightness() {
        return sMaxBrightness;
    }

    public static float getNormalMaxBrightness() {
        return sNormalMaxBrightness;
    }

    public static float getDefaultBrightness() {
        return sDefaultBrightness;
    }

    public static float getEngineeringModeBrightness20P() {
        return sEngineeringMode20P;
    }

    public static float convertGammaToLinearLevel(float gammaLevel, float maxLinearLevel) {
        return android.util.MathUtils.constrain(android.util.MathUtils.pow(gammaLevel / sNormalMaxBrightness, 2.2f), 0.0f, 1.0f) * maxLinearLevel;
    }
}
