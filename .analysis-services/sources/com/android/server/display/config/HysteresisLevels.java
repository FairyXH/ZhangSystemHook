package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class HysteresisLevels {
    private final float[] mBrighteningThresholdLevels;
    private final float[] mBrighteningThresholdsPercentages;
    private final float[] mDarkeningThresholdLevels;
    private final float[] mDarkeningThresholdsPercentages;
    private final float mMinBrightening;
    private final float mMinDarkening;
    private static final float[] DEFAULT_AMBIENT_BRIGHTENING_THRESHOLDS = {100.0f};
    private static final float[] DEFAULT_AMBIENT_DARKENING_THRESHOLDS = {200.0f};
    private static final float[] DEFAULT_AMBIENT_THRESHOLD_LEVELS = {0.0f};
    private static final float[] DEFAULT_SCREEN_THRESHOLD_LEVELS = {0.0f};
    private static final float[] DEFAULT_SCREEN_BRIGHTENING_THRESHOLDS = {100.0f};
    private static final float[] DEFAULT_SCREEN_DARKENING_THRESHOLDS = {200.0f};
    private static final java.lang.String TAG = "HysteresisLevels";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);

    public HysteresisLevels(float[] brighteningThresholdsPercentages, float[] darkeningThresholdsPercentages, float[] brighteningThresholdLevels, float[] darkeningThresholdLevels, float minDarkeningThreshold, float minBrighteningThreshold) {
        if (brighteningThresholdsPercentages.length != brighteningThresholdLevels.length || darkeningThresholdsPercentages.length != darkeningThresholdLevels.length) {
            throw new java.lang.IllegalArgumentException("Mismatch between hysteresis array lengths.");
        }
        this.mBrighteningThresholdsPercentages = setArrayFormat(brighteningThresholdsPercentages, 100.0f);
        this.mDarkeningThresholdsPercentages = setArrayFormat(darkeningThresholdsPercentages, 100.0f);
        this.mBrighteningThresholdLevels = setArrayFormat(brighteningThresholdLevels, 1.0f);
        this.mDarkeningThresholdLevels = setArrayFormat(darkeningThresholdLevels, 1.0f);
        this.mMinDarkening = minDarkeningThreshold;
        this.mMinBrightening = minBrighteningThreshold;
    }

    public float getBrighteningThreshold(float value) {
        float brightConstant = getReferenceLevel(value, this.mBrighteningThresholdLevels, this.mBrighteningThresholdsPercentages);
        float brightThreshold = (1.0f + brightConstant) * value;
        if (DEBUG) {
            android.util.Slog.d(TAG, "bright hysteresis constant=" + brightConstant + ", threshold=" + brightThreshold + ", value=" + value);
        }
        return java.lang.Math.max(brightThreshold, this.mMinBrightening + value);
    }

    public float getDarkeningThreshold(float value) {
        float darkConstant = getReferenceLevel(value, this.mDarkeningThresholdLevels, this.mDarkeningThresholdsPercentages);
        float darkThreshold = (1.0f - darkConstant) * value;
        if (DEBUG) {
            android.util.Slog.d(TAG, "dark hysteresis constant=: " + darkConstant + ", threshold=" + darkThreshold + ", value=" + value);
        }
        return java.lang.Math.max(java.lang.Math.min(darkThreshold, value - this.mMinDarkening), 0.0f);
    }

    public float[] getBrighteningThresholdsPercentages() {
        return this.mBrighteningThresholdsPercentages;
    }

    public float[] getDarkeningThresholdsPercentages() {
        return this.mDarkeningThresholdsPercentages;
    }

    public float[] getBrighteningThresholdLevels() {
        return this.mBrighteningThresholdLevels;
    }

    public float[] getDarkeningThresholdLevels() {
        return this.mDarkeningThresholdLevels;
    }

    public float getMinDarkening() {
        return this.mMinDarkening;
    }

    public float getMinBrightening() {
        return this.mMinBrightening;
    }

    private float getReferenceLevel(float value, float[] thresholdLevels, float[] thresholdPercentages) {
        if (thresholdLevels == null || thresholdLevels.length == 0 || value < thresholdLevels[0]) {
            return 0.0f;
        }
        int index = 0;
        while (index < thresholdLevels.length - 1 && value >= thresholdLevels[index + 1]) {
            index++;
        }
        return thresholdPercentages[index];
    }

    private float[] setArrayFormat(float[] configArray, float divideFactor) {
        float[] levelArray = new float[configArray.length];
        for (int index = 0; levelArray.length > index; index++) {
            levelArray[index] = configArray[index] / divideFactor;
        }
        return levelArray;
    }

    public java.lang.String toString() {
        return "HysteresisLevels {\n    mBrighteningThresholdLevels=" + java.util.Arrays.toString(this.mBrighteningThresholdLevels) + ",\n    mBrighteningThresholdsPercentages=" + java.util.Arrays.toString(this.mBrighteningThresholdsPercentages) + ",\n    mMinBrightening=" + this.mMinBrightening + ",\n    mDarkeningThresholdLevels=" + java.util.Arrays.toString(this.mDarkeningThresholdLevels) + ",\n    mDarkeningThresholdsPercentages=" + java.util.Arrays.toString(this.mDarkeningThresholdsPercentages) + ",\n    mMinDarkening=" + this.mMinDarkening + "\n}";
    }

    public static com.android.server.display.config.HysteresisLevels loadAmbientBrightnessConfig(com.android.server.display.config.DisplayConfiguration config, android.content.res.Resources resources) {
        return createHysteresisLevels(config == null ? null : config.getAmbientBrightnessChangeThresholds(), android.R.array.config_ambientThresholdLevels, android.R.array.config_ambientBrighteningThresholds, android.R.array.config_ambientDarkeningThresholds, DEFAULT_AMBIENT_THRESHOLD_LEVELS, DEFAULT_AMBIENT_BRIGHTENING_THRESHOLDS, DEFAULT_AMBIENT_DARKENING_THRESHOLDS, resources, false);
    }

    public static com.android.server.display.config.HysteresisLevels loadDisplayBrightnessConfig(com.android.server.display.config.DisplayConfiguration config, android.content.res.Resources resources) {
        return createHysteresisLevels(config == null ? null : config.getDisplayBrightnessChangeThresholds(), android.R.array.config_roundedCornerTopRadiusArray, android.R.array.config_roundedCornerBottomRadiusArray, android.R.array.config_roundedCornerTopRadiusAdjustmentArray, DEFAULT_SCREEN_THRESHOLD_LEVELS, DEFAULT_SCREEN_BRIGHTENING_THRESHOLDS, DEFAULT_SCREEN_DARKENING_THRESHOLDS, resources, true);
    }

    public static com.android.server.display.config.HysteresisLevels loadAmbientBrightnessIdleConfig(com.android.server.display.config.DisplayConfiguration config, android.content.res.Resources resources) {
        return createHysteresisLevels(config == null ? null : config.getAmbientBrightnessChangeThresholdsIdle(), android.R.array.config_ambientThresholdLevels, android.R.array.config_ambientBrighteningThresholds, android.R.array.config_ambientDarkeningThresholds, DEFAULT_AMBIENT_THRESHOLD_LEVELS, DEFAULT_AMBIENT_BRIGHTENING_THRESHOLDS, DEFAULT_AMBIENT_DARKENING_THRESHOLDS, resources, false);
    }

    public static com.android.server.display.config.HysteresisLevels loadDisplayBrightnessIdleConfig(com.android.server.display.config.DisplayConfiguration config, android.content.res.Resources resources) {
        return createHysteresisLevels(config == null ? null : config.getDisplayBrightnessChangeThresholdsIdle(), android.R.array.config_roundedCornerTopRadiusArray, android.R.array.config_roundedCornerBottomRadiusArray, android.R.array.config_roundedCornerTopRadiusAdjustmentArray, DEFAULT_SCREEN_THRESHOLD_LEVELS, DEFAULT_SCREEN_BRIGHTENING_THRESHOLDS, DEFAULT_SCREEN_DARKENING_THRESHOLDS, resources, true);
    }

    private static com.android.server.display.config.HysteresisLevels createHysteresisLevels(com.android.server.display.config.Thresholds thresholds, int configLevels, int configBrighteningThresholds, int configDarkeningThresholds, float[] defaultLevels, float[] defaultBrighteningThresholds, float[] defaultDarkeningThresholds, android.content.res.Resources resources, boolean potentialOldBrightnessScale) {
        float brighteningMinThreshold;
        com.android.server.display.config.BrightnessThresholds brighteningThresholds = thresholds == null ? null : thresholds.getBrighteningThresholds();
        com.android.server.display.config.BrightnessThresholds darkeningThresholds = thresholds != null ? thresholds.getDarkeningThresholds() : null;
        android.util.Pair<float[], float[]> brighteningPair = getBrightnessLevelAndPercentage(brighteningThresholds, configLevels, configBrighteningThresholds, defaultLevels, defaultBrighteningThresholds, potentialOldBrightnessScale, resources);
        android.util.Pair<float[], float[]> darkeningPair = getBrightnessLevelAndPercentage(darkeningThresholds, configLevels, configDarkeningThresholds, defaultLevels, defaultDarkeningThresholds, potentialOldBrightnessScale, resources);
        float fFloatValue = 0.0f;
        if (brighteningThresholds == null || brighteningThresholds.getMinimum() == null) {
            brighteningMinThreshold = 0.0f;
        } else {
            brighteningMinThreshold = brighteningThresholds.getMinimum().floatValue();
        }
        if (darkeningThresholds != null && darkeningThresholds.getMinimum() != null) {
            fFloatValue = darkeningThresholds.getMinimum().floatValue();
        }
        float darkeningMinThreshold = fFloatValue;
        return new com.android.server.display.config.HysteresisLevels((float[]) brighteningPair.second, (float[]) darkeningPair.second, (float[]) brighteningPair.first, (float[]) darkeningPair.first, darkeningMinThreshold, brighteningMinThreshold);
    }

    private static android.util.Pair<float[], float[]> getBrightnessLevelAndPercentage(com.android.server.display.config.BrightnessThresholds thresholds, int configFallbackThreshold, int configFallbackPermille, float[] defaultLevels, float[] defaultPercentage, boolean potentialOldBrightnessScale, android.content.res.Resources resources) {
        int configThresholdsSize;
        if (thresholds != null && thresholds.getBrightnessThresholdPoints() != null && !thresholds.getBrightnessThresholdPoints().getBrightnessThresholdPoint().isEmpty()) {
            java.util.List<com.android.server.display.config.ThresholdPoint> points = thresholds.getBrightnessThresholdPoints().getBrightnessThresholdPoint();
            int size = points.size();
            float[] thresholdLevels = new float[size];
            float[] thresholdPercentages = new float[size];
            int i = 0;
            for (com.android.server.display.config.ThresholdPoint point : points) {
                thresholdLevels[i] = point.getThreshold().floatValue();
                thresholdPercentages[i] = point.getPercentage().floatValue();
                i++;
            }
            return new android.util.Pair<>(thresholdLevels, thresholdPercentages);
        }
        if (resources != null) {
            int[] configThresholdArray = resources.getIntArray(configFallbackThreshold);
            if (configThresholdArray != null && configThresholdArray.length != 0) {
                configThresholdsSize = configThresholdArray.length + 1;
            } else {
                configThresholdsSize = 1;
            }
            int[] configPermille = resources.getIntArray(configFallbackPermille);
            boolean emptyArray = configPermille == null || configPermille.length == 0;
            if (emptyArray && configThresholdsSize == 1) {
                return new android.util.Pair<>(defaultLevels, defaultPercentage);
            }
            if (emptyArray || configPermille.length != configThresholdsSize) {
                throw new java.lang.IllegalArgumentException("Brightness threshold arrays do not align in length");
            }
            float[] configThresholdWithZeroPrefixed = new float[configThresholdsSize];
            for (int i2 = 1; i2 < configThresholdsSize; i2++) {
                configThresholdWithZeroPrefixed[i2] = configThresholdArray[i2 - 1];
            }
            if (potentialOldBrightnessScale) {
                configThresholdWithZeroPrefixed = constraintInRangeIfNeeded(configThresholdWithZeroPrefixed);
            }
            float[] configPercentage = new float[configThresholdsSize];
            for (int i3 = 0; i3 < configPermille.length; i3++) {
                configPercentage[i3] = configPermille[i3] / 10.0f;
            }
            return new android.util.Pair<>(configThresholdWithZeroPrefixed, configPercentage);
        }
        return new android.util.Pair<>(defaultLevels, defaultPercentage);
    }

    private static float[] constraintInRangeIfNeeded(float[] thresholdLevels) {
        if (isAllInRange(thresholdLevels, 0.0f, 1.0f)) {
            return thresholdLevels;
        }
        android.util.Slog.w(TAG, "Detected screen thresholdLevels on a deprecated brightness scale");
        float[] thresholdLevelsScaled = new float[thresholdLevels.length];
        for (int index = 0; thresholdLevels.length > index; index++) {
            thresholdLevelsScaled[index] = thresholdLevels[index] / 255.0f;
        }
        return thresholdLevelsScaled;
    }

    private static boolean isAllInRange(float[] configArray, float minValueInclusive, float maxValueInclusive) {
        for (float v : configArray) {
            if (v < minValueInclusive || v > maxValueInclusive) {
                return false;
            }
        }
        return true;
    }
}
