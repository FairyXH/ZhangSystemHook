package com.android.server.display.utils;

/* JADX INFO: loaded from: classes2.dex */
public class AmbientFilterFactory {
    public static com.android.server.display.utils.AmbientFilter createAmbientFilter(java.lang.String tag, int horizon, float intercept) {
        if (!java.lang.Float.isNaN(intercept)) {
            return new com.android.server.display.utils.AmbientFilter.WeightedMovingAverageAmbientFilter(tag, horizon, intercept);
        }
        throw new java.lang.IllegalArgumentException("missing configurations: expected config_displayWhiteBalanceBrightnessFilterIntercept");
    }

    public static com.android.server.display.utils.AmbientFilter createBrightnessFilter(java.lang.String tag, android.content.res.Resources resources) {
        int horizon = resources.getInteger(android.R.integer.config_defaultVibrationAmplitude);
        float intercept = getFloat(resources, android.R.dimen.chooser_preview_image_border);
        return createAmbientFilter(tag, horizon, intercept);
    }

    public static com.android.server.display.utils.AmbientFilter createColorTemperatureFilter(java.lang.String tag, android.content.res.Resources resources) {
        int horizon = resources.getInteger(android.R.integer.config_demo_pointing_aligned_duration_millis);
        float intercept = getFloat(resources, android.R.dimen.chooser_preview_image_font_size);
        return createAmbientFilter(tag, horizon, intercept);
    }

    private AmbientFilterFactory() {
    }

    private static float getFloat(android.content.res.Resources resources, int id) {
        android.util.TypedValue value = new android.util.TypedValue();
        resources.getValue(id, value, true);
        if (value.type != 4) {
            return Float.NaN;
        }
        return value.getFloat();
    }
}
