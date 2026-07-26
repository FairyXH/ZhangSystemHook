package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BrightnessMappingStrategy {
    public static final float INVALID_LUX = -1.0f;
    public static final float INVALID_NITS = -1.0f;
    private static final float LUX_GRAD_SMOOTHING = 0.25f;
    private static final float MAX_GRAD = 1.0f;
    private static final float MIN_PERMISSABLE_INCREASE = 0.004f;
    private static final float SHORT_TERM_MODEL_THRESHOLD_RATIO = 0.6f;
    protected boolean mLoggingEnabled;
    private static final java.lang.String TAG = "BrightnessMappingStrategy";
    private static final com.android.server.display.utils.Plog PLOG = com.android.server.display.utils.Plog.createSystemPlog(TAG);
    public static com.android.server.display.IBrightnessMappingStrategyExt mBrightnessMappingStrategyExt = (com.android.server.display.IBrightnessMappingStrategyExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IBrightnessMappingStrategyExt.class).create();

    public abstract void addUserDataPoint(float f, float f2);

    public abstract void clearUserDataPoints();

    public abstract float convertToAdjustedNits(float f);

    public abstract float convertToNits(float f);

    public abstract void dump(java.io.PrintWriter printWriter, float f);

    public abstract float getAutoBrightnessAdjustment();

    public abstract float getBrightness(float f, java.lang.String str, int i);

    public abstract android.hardware.display.BrightnessConfiguration getBrightnessConfiguration();

    public abstract float getBrightnessFromNits(float f);

    public abstract android.hardware.display.BrightnessConfiguration getDefaultConfig();

    abstract int getMode();

    public abstract long getShortTermModelTimeout();

    abstract float getUserBrightness();

    abstract float getUserLux();

    public abstract boolean hasUserDataPoints();

    public abstract boolean isDefaultConfig();

    public abstract void recalculateSplines(boolean z, float[] fArr);

    public abstract boolean setAutoBrightnessAdjustment(float f);

    public abstract boolean setBrightnessConfiguration(android.hardware.display.BrightnessConfiguration brightnessConfiguration);

    static com.android.server.display.BrightnessMappingStrategy create(android.content.Context context, com.android.server.display.DisplayDeviceConfig displayDeviceConfig, int mode, com.android.server.display.whitebalance.DisplayWhiteBalanceController displayWhiteBalanceController) {
        float[] brightnessLevelsNits;
        float[] brightnessLevels;
        float[] luxLevels;
        int preset = android.provider.Settings.System.getIntForUser(context.getContentResolver(), "screen_brightness_for_als", 2, -2);
        switch (mode) {
            case 0:
                float[] brightnessLevelsNits2 = displayDeviceConfig.getAutoBrightnessBrighteningLevelsNits();
                float[] luxLevels2 = displayDeviceConfig.getAutoBrightnessBrighteningLevelsLux(mode, preset);
                float[] brightnessLevels2 = displayDeviceConfig.getAutoBrightnessBrighteningLevels(mode, preset);
                brightnessLevelsNits = brightnessLevelsNits2;
                brightnessLevels = brightnessLevels2;
                luxLevels = luxLevels2;
                break;
            case 1:
                float[] brightnessLevelsNits3 = getFloatArray(context.getResources().obtainTypedArray(android.R.array.config_autoBrightnessDisplayValuesNits));
                float[] luxLevels3 = getLuxLevels(context.getResources().getIntArray(android.R.array.config_autoBrightnessLevels));
                brightnessLevelsNits = brightnessLevelsNits3;
                brightnessLevels = null;
                luxLevels = luxLevels3;
                break;
            case 2:
                float[] luxLevels4 = displayDeviceConfig.getAutoBrightnessBrighteningLevelsLux(mode, preset);
                float[] brightnessLevels3 = displayDeviceConfig.getAutoBrightnessBrighteningLevels(mode, preset);
                brightnessLevelsNits = null;
                brightnessLevels = brightnessLevels3;
                luxLevels = luxLevels4;
                break;
            default:
                brightnessLevelsNits = null;
                brightnessLevels = null;
                luxLevels = null;
                break;
        }
        float autoBrightnessAdjustmentMaxGamma = context.getResources().getFraction(android.R.fraction.config_autoBrightnessAdjustmentMaxGamma, 1, 1);
        long shortTermModelTimeout = context.getResources().getInteger(android.R.integer.config_audio_ring_vol_default);
        com.android.server.display.BrightnessMappingStrategy strategy = mBrightnessMappingStrategyExt.getBrightnessMappingStrategy();
        if (strategy != null) {
            return strategy;
        }
        float[] nitsRange = displayDeviceConfig.getNits();
        float[] brightnessRange = displayDeviceConfig.getBrightness();
        if (isValidMapping(nitsRange, brightnessRange) && isValidMapping(luxLevels, brightnessLevelsNits)) {
            android.hardware.display.BrightnessConfiguration.Builder builder = new android.hardware.display.BrightnessConfiguration.Builder(luxLevels, brightnessLevelsNits);
            builder.setShortTermModelTimeoutMillis(shortTermModelTimeout);
            builder.setShortTermModelLowerLuxMultiplier(SHORT_TERM_MODEL_THRESHOLD_RATIO);
            builder.setShortTermModelUpperLuxMultiplier(SHORT_TERM_MODEL_THRESHOLD_RATIO);
            return new com.android.server.display.BrightnessMappingStrategy.PhysicalMappingStrategy(builder.build(), nitsRange, brightnessRange, autoBrightnessAdjustmentMaxGamma, mode, displayWhiteBalanceController);
        }
        long shortTermModelTimeout2 = shortTermModelTimeout;
        if (isValidMapping(luxLevels, brightnessLevels)) {
            return new com.android.server.display.BrightnessMappingStrategy.SimpleMappingStrategy(luxLevels, brightnessLevels, autoBrightnessAdjustmentMaxGamma, shortTermModelTimeout2, mode);
        }
        return null;
    }

    private static float[] getLuxLevels(int[] lux) {
        float[] levels = new float[lux.length + 1];
        for (int i = 0; i < lux.length; i++) {
            levels[i + 1] = lux[i];
        }
        return levels;
    }

    protected static float normalizeAbsoluteBrightness(int brightness) {
        return com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(brightness);
    }

    public static float[] getFloatArray(android.content.res.TypedArray array) {
        int N = array.length();
        float[] vals = new float[N];
        for (int i = 0; i < N; i++) {
            vals[i] = array.getFloat(i, -1.0f);
        }
        array.recycle();
        return vals;
    }

    private static boolean isValidMapping(float[] x, float[] y) {
        if (x == null || y == null || x.length == 0 || y.length == 0 || x.length != y.length) {
            return false;
        }
        int N = x.length;
        float prevX = x[0];
        float prevY = y[0];
        if (prevX < 0.0f || prevY < 0.0f || java.lang.Float.isNaN(prevX) || java.lang.Float.isNaN(prevY)) {
            return false;
        }
        for (int i = 1; i < N; i++) {
            if (prevX >= x[i] || prevY > y[i] || java.lang.Float.isNaN(x[i]) || java.lang.Float.isNaN(y[i])) {
                return false;
            }
            prevX = x[i];
            prevY = y[i];
        }
        return true;
    }

    private static boolean isValidMapping(float[] x, int[] y) {
        if (x == null || y == null || x.length == 0 || y.length == 0 || x.length != y.length) {
            return false;
        }
        int N = x.length;
        float prevX = x[0];
        int prevY = y[0];
        if (prevX < 0.0f || prevY < 0 || java.lang.Float.isNaN(prevX)) {
            return false;
        }
        for (int i = 1; i < N; i++) {
            if (prevX >= x[i] || prevY > y[i] || java.lang.Float.isNaN(x[i])) {
                return false;
            }
            prevX = x[i];
            prevY = y[i];
        }
        return true;
    }

    public boolean setLoggingEnabled(boolean loggingEnabled) {
        if (this.mLoggingEnabled == loggingEnabled) {
            return false;
        }
        this.mLoggingEnabled = loggingEnabled;
        return true;
    }

    public float getBrightness(float lux) {
        return getBrightness(lux, null, -1);
    }

    public boolean shouldResetShortTermModel(float ambientLux, float shortTermModelAnchor) {
        android.hardware.display.BrightnessConfiguration config = getBrightnessConfiguration();
        float minThresholdRatio = SHORT_TERM_MODEL_THRESHOLD_RATIO;
        float maxThresholdRatio = SHORT_TERM_MODEL_THRESHOLD_RATIO;
        if (config != null) {
            if (!java.lang.Float.isNaN(config.getShortTermModelLowerLuxMultiplier())) {
                minThresholdRatio = config.getShortTermModelLowerLuxMultiplier();
            }
            if (!java.lang.Float.isNaN(config.getShortTermModelUpperLuxMultiplier())) {
                maxThresholdRatio = config.getShortTermModelUpperLuxMultiplier();
            }
        }
        float minAmbientLux = shortTermModelAnchor - (shortTermModelAnchor * minThresholdRatio);
        float maxAmbientLux = (shortTermModelAnchor * maxThresholdRatio) + shortTermModelAnchor;
        if (minAmbientLux < ambientLux && ambientLux <= maxAmbientLux) {
            if (this.mLoggingEnabled) {
                android.util.Slog.d(TAG, "ShortTermModel: re-validate user data, ambient lux is " + minAmbientLux + " < " + ambientLux + " < " + maxAmbientLux);
                return false;
            }
            return false;
        }
        android.util.Slog.d(TAG, "ShortTermModel: reset data, ambient lux is " + ambientLux + "(" + minAmbientLux + ", " + maxAmbientLux + ")");
        return true;
    }

    private android.util.Pair<float[], float[]> insertControlPoint(float[] luxLevels, float[] brightnessLevels, float lux, float brightness) {
        float[] newLuxLevels;
        float[] newBrightnessLevels;
        int idx = findInsertionPoint(luxLevels, lux);
        if (idx == luxLevels.length) {
            newLuxLevels = java.util.Arrays.copyOf(luxLevels, luxLevels.length + 1);
            newBrightnessLevels = java.util.Arrays.copyOf(brightnessLevels, brightnessLevels.length + 1);
            newLuxLevels[idx] = lux;
            newBrightnessLevels[idx] = brightness;
        } else if (luxLevels[idx] == lux) {
            newLuxLevels = java.util.Arrays.copyOf(luxLevels, luxLevels.length);
            newBrightnessLevels = java.util.Arrays.copyOf(brightnessLevels, brightnessLevels.length);
            newBrightnessLevels[idx] = brightness;
        } else {
            newLuxLevels = java.util.Arrays.copyOf(luxLevels, luxLevels.length + 1);
            java.lang.System.arraycopy(newLuxLevels, idx, newLuxLevels, idx + 1, luxLevels.length - idx);
            newLuxLevels[idx] = lux;
            newBrightnessLevels = java.util.Arrays.copyOf(brightnessLevels, brightnessLevels.length + 1);
            java.lang.System.arraycopy(newBrightnessLevels, idx, newBrightnessLevels, idx + 1, brightnessLevels.length - idx);
            newBrightnessLevels[idx] = brightness;
        }
        smoothCurve(newLuxLevels, newBrightnessLevels, idx);
        return android.util.Pair.create(newLuxLevels, newBrightnessLevels);
    }

    private int findInsertionPoint(float[] arr, float val) {
        for (int i = 0; i < arr.length; i++) {
            if (val <= arr[i]) {
                return i;
            }
        }
        int i2 = arr.length;
        return i2;
    }

    private void smoothCurve(float[] lux, float[] brightness, int idx) {
        if (this.mLoggingEnabled) {
            PLOG.logCurve("unsmoothed curve", lux, brightness);
        }
        float prevLux = lux[idx];
        float prevBrightness = brightness[idx];
        for (int i = idx + 1; i < lux.length; i++) {
            float currLux = lux[i];
            float currBrightness = brightness[i];
            float maxBrightness = android.util.MathUtils.max(permissibleRatio(currLux, prevLux) * prevBrightness, MIN_PERMISSABLE_INCREASE + prevBrightness);
            float newBrightness = android.util.MathUtils.constrain(currBrightness, prevBrightness, maxBrightness);
            if (newBrightness == currBrightness) {
                break;
            }
            prevLux = currLux;
            prevBrightness = newBrightness;
            brightness[i] = newBrightness;
        }
        float prevLux2 = lux[idx];
        float prevBrightness2 = brightness[idx];
        for (int i2 = idx - 1; i2 >= 0; i2--) {
            float currLux2 = lux[i2];
            float currBrightness2 = brightness[i2];
            float minBrightness = permissibleRatio(currLux2, prevLux2) * prevBrightness2;
            float newBrightness2 = android.util.MathUtils.constrain(currBrightness2, minBrightness, prevBrightness2);
            if (newBrightness2 == currBrightness2) {
                break;
            }
            prevLux2 = currLux2;
            prevBrightness2 = newBrightness2;
            brightness[i2] = newBrightness2;
        }
        if (this.mLoggingEnabled) {
            PLOG.logCurve("smoothed curve", lux, brightness);
        }
    }

    private float permissibleRatio(float currLux, float prevLux) {
        return android.util.MathUtils.pow((currLux + LUX_GRAD_SMOOTHING) / (LUX_GRAD_SMOOTHING + prevLux), 1.0f);
    }

    protected float inferAutoBrightnessAdjustment(float maxGamma, float desiredBrightness, float currentBrightness) {
        float adjustment;
        float gamma = Float.NaN;
        if (currentBrightness <= 0.1f || currentBrightness >= 0.9f) {
            adjustment = desiredBrightness - currentBrightness;
        } else if (desiredBrightness == 0.0f) {
            adjustment = -1.0f;
        } else if (desiredBrightness == 1.0f) {
            adjustment = 1.0f;
        } else {
            gamma = android.util.MathUtils.log(desiredBrightness) / android.util.MathUtils.log(currentBrightness);
            adjustment = (-android.util.MathUtils.log(gamma)) / android.util.MathUtils.log(maxGamma);
        }
        float adjustment2 = android.util.MathUtils.constrain(adjustment, -1.0f, 1.0f);
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "inferAutoBrightnessAdjustment: " + maxGamma + "^" + (-adjustment2) + "=" + android.util.MathUtils.pow(maxGamma, -adjustment2) + " == " + gamma);
            android.util.Slog.d(TAG, "inferAutoBrightnessAdjustment: " + currentBrightness + "^" + gamma + "=" + android.util.MathUtils.pow(currentBrightness, gamma) + " == " + desiredBrightness);
        }
        return adjustment2;
    }

    protected android.util.Pair<float[], float[]> getAdjustedCurve(float[] lux, float[] brightness, float userLux, float userBrightness, float adjustment, float maxGamma) {
        float[] newLux = lux;
        float[] newBrightness = java.util.Arrays.copyOf(brightness, brightness.length);
        if (this.mLoggingEnabled) {
            PLOG.logCurve("unadjusted curve", newLux, newBrightness);
        }
        float adjustment2 = android.util.MathUtils.constrain(adjustment, -1.0f, 1.0f);
        float gamma = android.util.MathUtils.pow(maxGamma, -adjustment2);
        if (this.mLoggingEnabled) {
            android.util.Slog.d(TAG, "getAdjustedCurve: " + maxGamma + "^" + (-adjustment2) + "=" + android.util.MathUtils.pow(maxGamma, -adjustment2) + " == " + gamma);
        }
        if (gamma != 1.0f) {
            for (int i = 0; i < newBrightness.length; i++) {
                newBrightness[i] = android.util.MathUtils.pow(newBrightness[i], gamma);
            }
        }
        if (this.mLoggingEnabled) {
            PLOG.logCurve("gamma adjusted curve", newLux, newBrightness);
        }
        if (userLux != -1.0f) {
            android.util.Pair<float[], float[]> curve = insertControlPoint(newLux, newBrightness, userLux, userBrightness);
            newLux = (float[]) curve.first;
            newBrightness = (float[]) curve.second;
            if (this.mLoggingEnabled) {
                PLOG.logCurve("gamma and user adjusted curve", newLux, newBrightness);
                android.util.Pair<float[], float[]> curve2 = insertControlPoint(lux, brightness, userLux, userBrightness);
                PLOG.logCurve("user adjusted curve", (float[]) curve2.first, (float[]) curve2.second);
            }
        }
        return android.util.Pair.create(newLux, newBrightness);
    }

    private static class SimpleMappingStrategy extends com.android.server.display.BrightnessMappingStrategy {
        private float mAutoBrightnessAdjustment;
        private final float[] mBrightness;
        private final float[] mLux;
        private float mMaxGamma;
        private final int mMode;
        private long mShortTermModelTimeout;
        private android.util.Spline mSpline;
        private float mUserBrightness;
        private float mUserLux;

        private SimpleMappingStrategy(float[] lux, float[] brightness, float maxGamma, long timeout, int mode) {
            com.android.internal.util.Preconditions.checkArgument((lux.length == 0 || brightness.length == 0) ? false : true, "Lux and brightness arrays must not be empty!");
            com.android.internal.util.Preconditions.checkArgument(lux.length == brightness.length, "Lux and brightness arrays must be the same length!");
            com.android.internal.util.Preconditions.checkArrayElementsInRange(lux, 0.0f, Float.MAX_VALUE, "lux");
            com.android.internal.util.Preconditions.checkArrayElementsInRange(brightness, 0.0f, 2.1474836E9f, "brightness");
            int N = brightness.length;
            this.mLux = new float[N];
            this.mBrightness = new float[N];
            for (int i = 0; i < N; i++) {
                this.mLux[i] = lux[i];
                this.mBrightness[i] = brightness[i];
            }
            this.mMaxGamma = maxGamma;
            this.mAutoBrightnessAdjustment = 0.0f;
            this.mUserLux = -1.0f;
            this.mUserBrightness = Float.NaN;
            if (this.mLoggingEnabled) {
                com.android.server.display.BrightnessMappingStrategy.PLOG.start("simple mapping strategy");
            }
            computeSpline();
            this.mShortTermModelTimeout = timeout;
            this.mMode = mode;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public long getShortTermModelTimeout() {
            return this.mShortTermModelTimeout;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean setBrightnessConfiguration(android.hardware.display.BrightnessConfiguration config) {
            return false;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public android.hardware.display.BrightnessConfiguration getBrightnessConfiguration() {
            return null;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float getBrightness(float lux, java.lang.String packageName, int category) {
            return this.mSpline.interpolate(lux);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float getAutoBrightnessAdjustment() {
            return this.mAutoBrightnessAdjustment;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean setAutoBrightnessAdjustment(float adjustment) {
            float adjustment2 = android.util.MathUtils.constrain(adjustment, -1.0f, 1.0f);
            if (adjustment2 == this.mAutoBrightnessAdjustment) {
                return false;
            }
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.BrightnessMappingStrategy.TAG, "setAutoBrightnessAdjustment: " + this.mAutoBrightnessAdjustment + " => " + adjustment2);
                com.android.server.display.BrightnessMappingStrategy.PLOG.start("auto-brightness adjustment");
            }
            this.mAutoBrightnessAdjustment = adjustment2;
            computeSpline();
            return true;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float convertToNits(float brightness) {
            return -1.0f;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float convertToAdjustedNits(float brightness) {
            return -1.0f;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float getBrightnessFromNits(float nits) {
            return Float.NaN;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void addUserDataPoint(float lux, float brightness) {
            float unadjustedBrightness = getUnadjustedBrightness(lux);
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.BrightnessMappingStrategy.TAG, "addUserDataPoint: (" + lux + "," + brightness + ")");
                com.android.server.display.BrightnessMappingStrategy.PLOG.start("add user data point").logPoint("user data point", lux, brightness).logPoint("current brightness", lux, unadjustedBrightness);
            }
            float adjustment = inferAutoBrightnessAdjustment(this.mMaxGamma, brightness, unadjustedBrightness);
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.BrightnessMappingStrategy.TAG, "addUserDataPoint: " + this.mAutoBrightnessAdjustment + " => " + adjustment);
            }
            this.mAutoBrightnessAdjustment = adjustment;
            this.mUserLux = lux;
            this.mUserBrightness = brightness;
            computeSpline();
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void clearUserDataPoints() {
            if (this.mUserLux != -1.0f) {
                if (this.mLoggingEnabled) {
                    android.util.Slog.d(com.android.server.display.BrightnessMappingStrategy.TAG, "clearUserDataPoints: " + this.mAutoBrightnessAdjustment + " => 0");
                    com.android.server.display.BrightnessMappingStrategy.PLOG.start("clear user data points").logPoint("user data point", this.mUserLux, this.mUserBrightness);
                }
                this.mAutoBrightnessAdjustment = 0.0f;
                this.mUserLux = -1.0f;
                this.mUserBrightness = Float.NaN;
                computeSpline();
            }
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean hasUserDataPoints() {
            return this.mUserLux != -1.0f;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean isDefaultConfig() {
            return true;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public android.hardware.display.BrightnessConfiguration getDefaultConfig() {
            return null;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void recalculateSplines(boolean applyAdjustment, float[] adjustment) {
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void dump(java.io.PrintWriter pw, float hbmTransition) {
            pw.println("SimpleMappingStrategy");
            pw.println("  mSpline=" + this.mSpline);
            pw.println("  mMaxGamma=" + this.mMaxGamma);
            pw.println("  mAutoBrightnessAdjustment=" + this.mAutoBrightnessAdjustment);
            pw.println("  mUserLux=" + this.mUserLux);
            pw.println("  mUserBrightness=" + this.mUserBrightness);
            pw.println("  mShortTermModelTimeout=" + this.mShortTermModelTimeout);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        int getMode() {
            return this.mMode;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        float getUserLux() {
            return this.mUserLux;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        float getUserBrightness() {
            return this.mUserBrightness;
        }

        private void computeSpline() {
            android.util.Pair<float[], float[]> curve = getAdjustedCurve(this.mLux, this.mBrightness, this.mUserLux, this.mUserBrightness, this.mAutoBrightnessAdjustment, this.mMaxGamma);
            this.mSpline = android.util.Spline.createSpline((float[]) curve.first, (float[]) curve.second);
        }

        private float getUnadjustedBrightness(float lux) {
            android.util.Spline spline = android.util.Spline.createSpline(this.mLux, this.mBrightness);
            return spline.interpolate(lux);
        }
    }

    static class PhysicalMappingStrategy extends com.android.server.display.BrightnessMappingStrategy {
        private static final java.text.SimpleDateFormat FORMAT = new java.text.SimpleDateFormat("MM-dd HH:mm:ss.SSS");
        private static final int NO_OF_PREVIOUS_CONFIGS_TO_LOG = 5;
        private android.util.Spline mAdjustedNitsToBrightnessSpline;
        private float mAutoBrightnessAdjustment;
        private final float[] mBrightness;
        private boolean mBrightnessRangeAdjustmentApplied;
        private android.util.Spline mBrightnessSpline;
        private android.util.Spline mBrightnessToAdjustedNitsSpline;
        private android.util.Spline mBrightnessToNitsSpline;
        private android.hardware.display.BrightnessConfiguration mConfig;
        private final android.hardware.display.BrightnessConfiguration mDefaultConfig;
        private final com.android.server.display.whitebalance.DisplayWhiteBalanceController mDisplayWhiteBalanceController;
        private final float mMaxGamma;
        private final int mMode;
        private final float[] mNits;
        private android.util.Spline mNitsToBrightnessSpline;
        private float mUserBrightness;
        private float mUserLux;
        private java.util.List<android.util.Spline> mPreviousBrightnessSplines = new java.util.ArrayList();
        private android.util.LongArray mBrightnessSplineChangeTimes = new android.util.LongArray();

        public PhysicalMappingStrategy(android.hardware.display.BrightnessConfiguration config, float[] nits, float[] brightness, float maxGamma, int mode, com.android.server.display.whitebalance.DisplayWhiteBalanceController displayWhiteBalanceController) {
            com.android.internal.util.Preconditions.checkArgument((nits.length == 0 || brightness.length == 0) ? false : true, "Nits and brightness arrays must not be empty!");
            com.android.internal.util.Preconditions.checkArgument(nits.length == brightness.length, "Nits and brightness arrays must be the same length!");
            java.util.Objects.requireNonNull(config);
            com.android.internal.util.Preconditions.checkArrayElementsInRange(nits, 0.0f, Float.MAX_VALUE, "nits");
            com.android.internal.util.Preconditions.checkArrayElementsInRange(brightness, 0.0f, 1.0f, "brightness");
            this.mMode = mode;
            this.mMaxGamma = maxGamma;
            this.mAutoBrightnessAdjustment = 0.0f;
            this.mUserLux = -1.0f;
            this.mUserBrightness = Float.NaN;
            this.mDisplayWhiteBalanceController = displayWhiteBalanceController;
            this.mNits = nits;
            this.mBrightness = brightness;
            computeNitsBrightnessSplines(this.mNits);
            this.mAdjustedNitsToBrightnessSpline = this.mNitsToBrightnessSpline;
            this.mBrightnessToAdjustedNitsSpline = this.mBrightnessToNitsSpline;
            this.mDefaultConfig = config;
            if (this.mLoggingEnabled) {
                com.android.server.display.BrightnessMappingStrategy.PLOG.start("physical mapping strategy");
            }
            this.mConfig = config;
            computeSpline();
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public long getShortTermModelTimeout() {
            if (this.mConfig.getShortTermModelTimeoutMillis() >= 0) {
                return this.mConfig.getShortTermModelTimeoutMillis();
            }
            return this.mDefaultConfig.getShortTermModelTimeoutMillis();
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean setBrightnessConfiguration(android.hardware.display.BrightnessConfiguration config) {
            if (config == null) {
                config = this.mDefaultConfig;
            }
            if (config.equals(this.mConfig)) {
                return false;
            }
            if (this.mLoggingEnabled) {
                com.android.server.display.BrightnessMappingStrategy.PLOG.start("brightness configuration");
            }
            this.mConfig = config;
            computeSpline();
            return true;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public android.hardware.display.BrightnessConfiguration getBrightnessConfiguration() {
            return this.mConfig;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float getBrightness(float lux, java.lang.String packageName, int category) {
            float nits = this.mBrightnessSpline.interpolate(lux);
            if (this.mDisplayWhiteBalanceController != null) {
                nits = this.mDisplayWhiteBalanceController.calculateAdjustedBrightnessNits(nits);
            }
            float brightness = this.mAdjustedNitsToBrightnessSpline.interpolate(nits);
            if (this.mUserLux == -1.0f) {
                return correctBrightness(brightness, packageName, category);
            }
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.BrightnessMappingStrategy.TAG, "user point set, correction not applied");
                return brightness;
            }
            return brightness;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float getAutoBrightnessAdjustment() {
            return this.mAutoBrightnessAdjustment;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean setAutoBrightnessAdjustment(float adjustment) {
            float adjustment2 = android.util.MathUtils.constrain(adjustment, -1.0f, 1.0f);
            if (adjustment2 == this.mAutoBrightnessAdjustment) {
                return false;
            }
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.BrightnessMappingStrategy.TAG, "setAutoBrightnessAdjustment: " + this.mAutoBrightnessAdjustment + " => " + adjustment2);
                com.android.server.display.BrightnessMappingStrategy.PLOG.start("auto-brightness adjustment");
            }
            this.mAutoBrightnessAdjustment = adjustment2;
            computeSpline();
            return true;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float convertToNits(float brightness) {
            return this.mBrightnessToNitsSpline.interpolate(brightness);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float convertToAdjustedNits(float brightness) {
            return this.mBrightnessToAdjustedNitsSpline.interpolate(brightness);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public float getBrightnessFromNits(float nits) {
            return this.mNitsToBrightnessSpline.interpolate(nits);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void addUserDataPoint(float lux, float brightness) {
            float unadjustedBrightness = getUnadjustedBrightness(lux);
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.BrightnessMappingStrategy.TAG, "addUserDataPoint: (" + lux + "," + brightness + ")");
                com.android.server.display.BrightnessMappingStrategy.PLOG.start("add user data point").logPoint("user data point", lux, brightness).logPoint("current brightness", lux, unadjustedBrightness);
            }
            float adjustment = inferAutoBrightnessAdjustment(this.mMaxGamma, brightness, unadjustedBrightness);
            if (this.mLoggingEnabled) {
                android.util.Slog.d(com.android.server.display.BrightnessMappingStrategy.TAG, "addUserDataPoint: " + this.mAutoBrightnessAdjustment + " => " + adjustment);
            }
            this.mAutoBrightnessAdjustment = adjustment;
            this.mUserLux = lux;
            this.mUserBrightness = brightness;
            computeSpline();
            if (this.mPreviousBrightnessSplines.size() == 5) {
                this.mPreviousBrightnessSplines.remove(0);
                this.mBrightnessSplineChangeTimes.remove(0);
            }
            this.mPreviousBrightnessSplines.add(this.mBrightnessSpline);
            this.mBrightnessSplineChangeTimes.add(java.lang.System.currentTimeMillis());
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void clearUserDataPoints() {
            if (this.mUserLux != -1.0f) {
                if (this.mLoggingEnabled) {
                    android.util.Slog.d(com.android.server.display.BrightnessMappingStrategy.TAG, "clearUserDataPoints: " + this.mAutoBrightnessAdjustment + " => 0");
                    com.android.server.display.BrightnessMappingStrategy.PLOG.start("clear user data points").logPoint("user data point", this.mUserLux, this.mUserBrightness);
                }
                this.mAutoBrightnessAdjustment = 0.0f;
                this.mUserLux = -1.0f;
                this.mUserBrightness = Float.NaN;
                computeSpline();
            }
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean hasUserDataPoints() {
            return this.mUserLux != -1.0f;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public boolean isDefaultConfig() {
            return this.mDefaultConfig.equals(this.mConfig);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public android.hardware.display.BrightnessConfiguration getDefaultConfig() {
            return this.mDefaultConfig;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void recalculateSplines(boolean applyAdjustment, float[] adjustedNits) {
            this.mBrightnessRangeAdjustmentApplied = applyAdjustment;
            if (applyAdjustment) {
                this.mAdjustedNitsToBrightnessSpline = android.util.Spline.createSpline(adjustedNits, this.mBrightness);
                this.mBrightnessToAdjustedNitsSpline = android.util.Spline.createSpline(this.mBrightness, adjustedNits);
            } else {
                this.mAdjustedNitsToBrightnessSpline = this.mNitsToBrightnessSpline;
                this.mBrightnessToAdjustedNitsSpline = this.mBrightnessToNitsSpline;
            }
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        public void dump(java.io.PrintWriter pw, float hbmTransition) {
            pw.println("PhysicalMappingStrategy");
            pw.println("  mConfig=" + this.mConfig);
            pw.println("  mBrightnessSpline=" + this.mBrightnessSpline);
            pw.println("  mNitsToBrightnessSpline=" + this.mNitsToBrightnessSpline);
            pw.println("  mBrightnessToNitsSpline=" + this.mBrightnessToNitsSpline);
            pw.println("  mAdjustedNitsToBrightnessSpline=" + this.mAdjustedNitsToBrightnessSpline);
            pw.println("  mAdjustedBrightnessToNitsSpline=" + this.mBrightnessToAdjustedNitsSpline);
            pw.println("  mMaxGamma=" + this.mMaxGamma);
            pw.println("  mAutoBrightnessAdjustment=" + this.mAutoBrightnessAdjustment);
            pw.println("  mUserLux=" + this.mUserLux);
            pw.println("  mUserBrightness=" + this.mUserBrightness);
            pw.println("  mDefaultConfig=" + this.mDefaultConfig);
            pw.println("  mBrightnessRangeAdjustmentApplied=" + this.mBrightnessRangeAdjustmentApplied);
            pw.println("  shortTermModelTimeout=" + getShortTermModelTimeout());
            pw.println("  Previous short-term models (oldest to newest): ");
            for (int i = 0; i < this.mPreviousBrightnessSplines.size(); i++) {
                pw.println("  Computed at " + FORMAT.format(new java.util.Date(this.mBrightnessSplineChangeTimes.get(i))) + ": ");
                dumpConfigDiff(pw, hbmTransition, this.mPreviousBrightnessSplines.get(i), true);
            }
            pw.println("  Difference between current config and default: ");
            dumpConfigDiff(pw, hbmTransition, this.mBrightnessSpline, false);
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        int getMode() {
            return this.mMode;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        float getUserLux() {
            return this.mUserLux;
        }

        @Override // com.android.server.display.BrightnessMappingStrategy
        float getUserBrightness() {
            return this.mUserBrightness;
        }

        private void dumpConfigDiff(java.io.PrintWriter pw, float hbmTransition, android.util.Spline brightnessSpline, boolean shortTermModelOnly) {
            int i;
            float[] luxes;
            java.io.PrintWriter printWriter;
            boolean needsHeaders;
            com.android.server.display.BrightnessMappingStrategy.PhysicalMappingStrategy physicalMappingStrategy = this;
            android.util.Pair<float[], float[]> currentCurve = physicalMappingStrategy.mConfig.getCurve();
            android.util.Spline currSpline = android.util.Spline.createSpline((float[]) currentCurve.first, (float[]) currentCurve.second);
            android.util.Pair<float[], float[]> defaultCurve = physicalMappingStrategy.mDefaultConfig.getCurve();
            android.util.Spline defaultSpline = android.util.Spline.createSpline((float[]) defaultCurve.first, (float[]) defaultCurve.second);
            float[] luxes2 = (float[]) currentCurve.first;
            if (physicalMappingStrategy.mUserLux >= 0.0f) {
                luxes2 = java.util.Arrays.copyOf((float[]) currentCurve.first, ((float[]) currentCurve.first).length + 1);
                luxes2[luxes2.length - 1] = physicalMappingStrategy.mUserLux;
                java.util.Arrays.sort(luxes2);
            }
            java.lang.StringBuilder sbLong = null;
            java.lang.StringBuilder sbShort = null;
            java.lang.StringBuilder sbBrightness = null;
            java.lang.StringBuilder sbPercent = null;
            java.lang.StringBuilder sbPercentHbm = null;
            java.lang.StringBuilder sbPercent2 = null;
            java.lang.StringBuilder sbPercentHbm2 = null;
            boolean needsHeaders2 = true;
            java.lang.String separator = "";
            int i2 = 0;
            while (true) {
                android.util.Pair<float[], float[]> defaultCurve2 = defaultCurve;
                if (i2 < luxes2.length) {
                    float lux = luxes2[i2];
                    if (needsHeaders2) {
                        java.lang.StringBuilder sbLux = new java.lang.StringBuilder("            lux: ");
                        java.lang.StringBuilder sbNits = new java.lang.StringBuilder("        default: ");
                        java.lang.StringBuilder sbLong2 = new java.lang.StringBuilder("      long-term: ");
                        java.lang.StringBuilder sbShort2 = new java.lang.StringBuilder("        current: ");
                        java.lang.StringBuilder sbBrightness2 = new java.lang.StringBuilder("    current(bl): ");
                        java.lang.StringBuilder sbPercent3 = new java.lang.StringBuilder("     current(%): ");
                        java.lang.StringBuilder sbPercentHbm3 = new java.lang.StringBuilder("  current(hbm%): ");
                        needsHeaders2 = false;
                        sbPercent2 = sbPercent3;
                        sbPercentHbm2 = sbPercentHbm3;
                        sbPercent = sbShort2;
                        sbPercentHbm = sbBrightness2;
                        sbShort = sbNits;
                        sbBrightness = sbLong2;
                        sbLong = sbLux;
                    }
                    boolean needsHeaders3 = needsHeaders2;
                    float defaultNits = defaultSpline.interpolate(lux);
                    android.util.Spline defaultSpline2 = defaultSpline;
                    float longTermNits = currSpline.interpolate(lux);
                    android.util.Spline currSpline2 = currSpline;
                    float shortTermNits = brightnessSpline.interpolate(lux);
                    float brightness = physicalMappingStrategy.mAdjustedNitsToBrightnessSpline.interpolate(shortTermNits);
                    int i3 = i2;
                    float[] luxes3 = luxes2;
                    java.lang.String luxPrefix = lux == physicalMappingStrategy.mUserLux ? "^" : "";
                    java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append(luxPrefix);
                    java.lang.String luxPrefix2 = physicalMappingStrategy.toStrFloatForDump(lux);
                    java.lang.String strLux = sbAppend.append(luxPrefix2).toString();
                    java.lang.String strNits = physicalMappingStrategy.toStrFloatForDump(defaultNits);
                    java.lang.String strLong = physicalMappingStrategy.toStrFloatForDump(longTermNits);
                    java.lang.String strShort = physicalMappingStrategy.toStrFloatForDump(shortTermNits);
                    java.lang.String strBrightness = physicalMappingStrategy.toStrFloatForDump(brightness);
                    java.lang.String strPercent = java.lang.String.valueOf(java.lang.Math.round(com.android.internal.display.BrightnessUtils.convertLinearToGamma(brightness / hbmTransition) * 100.0f));
                    java.lang.String strPercentHbm = java.lang.String.valueOf(java.lang.Math.round(com.android.internal.display.BrightnessUtils.convertLinearToGamma(brightness) * 100.0f));
                    int maxLen = java.lang.Math.max(strLux.length(), java.lang.Math.max(strNits.length(), java.lang.Math.max(strBrightness.length(), java.lang.Math.max(strPercent.length(), java.lang.Math.max(strPercentHbm.length(), java.lang.Math.max(strLong.length(), strShort.length()))))));
                    java.lang.String format = separator + "%" + maxLen + "s";
                    sbLong.append(android.text.TextUtils.formatSimple(format, new java.lang.Object[]{strLux}));
                    sbShort.append(android.text.TextUtils.formatSimple(format, new java.lang.Object[]{strNits}));
                    sbBrightness.append(android.text.TextUtils.formatSimple(format, new java.lang.Object[]{strLong}));
                    sbPercent.append(android.text.TextUtils.formatSimple(format, new java.lang.Object[]{strShort}));
                    sbPercentHbm.append(android.text.TextUtils.formatSimple(format, new java.lang.Object[]{strBrightness}));
                    sbPercent2.append(android.text.TextUtils.formatSimple(format, new java.lang.Object[]{strPercent}));
                    sbPercentHbm2 = sbPercentHbm2;
                    sbPercentHbm2.append(android.text.TextUtils.formatSimple(format, new java.lang.Object[]{strPercentHbm}));
                    if (sbLong.length() <= 80) {
                        luxes = luxes3;
                        i = i3;
                        if (i != luxes.length - 1) {
                            printWriter = pw;
                            separator = ", ";
                            needsHeaders = needsHeaders3;
                        }
                        i2 = i + 1;
                        needsHeaders2 = needsHeaders;
                        luxes2 = luxes;
                        defaultCurve = defaultCurve2;
                        defaultSpline = defaultSpline2;
                        currSpline = currSpline2;
                        physicalMappingStrategy = this;
                    } else {
                        i = i3;
                        luxes = luxes3;
                    }
                    printWriter = pw;
                    printWriter.println(sbLong);
                    if (!shortTermModelOnly) {
                        printWriter.println(sbShort);
                        printWriter.println(sbBrightness);
                    }
                    printWriter.println(sbPercent);
                    printWriter.println(sbPercentHbm);
                    printWriter.println(sbPercent2);
                    if (hbmTransition < 1.0f) {
                        printWriter.println(sbPercentHbm2);
                    }
                    printWriter.println("");
                    needsHeaders = true;
                    separator = "";
                    i2 = i + 1;
                    needsHeaders2 = needsHeaders;
                    luxes2 = luxes;
                    defaultCurve = defaultCurve2;
                    defaultSpline = defaultSpline2;
                    currSpline = currSpline2;
                    physicalMappingStrategy = this;
                } else {
                    return;
                }
            }
        }

        private java.lang.String toStrFloatForDump(float value) {
            if (value == 0.0f) {
                return "0";
            }
            if (value < 0.1f) {
                return java.lang.String.format(java.util.Locale.US, "%.3f", java.lang.Float.valueOf(value));
            }
            if (value < 1.0f) {
                return java.lang.String.format(java.util.Locale.US, "%.2f", java.lang.Float.valueOf(value));
            }
            if (value < 10.0f) {
                return java.lang.String.format(java.util.Locale.US, "%.1f", java.lang.Float.valueOf(value));
            }
            return android.text.TextUtils.formatSimple("%d", new java.lang.Object[]{java.lang.Integer.valueOf(java.lang.Math.round(value))});
        }

        private void computeNitsBrightnessSplines(float[] nits) {
            this.mNitsToBrightnessSpline = android.util.Spline.createSpline(nits, this.mBrightness);
            this.mBrightnessToNitsSpline = android.util.Spline.createSpline(this.mBrightness, nits);
        }

        private void computeSpline() {
            android.util.Pair<float[], float[]> defaultCurve = this.mConfig.getCurve();
            float[] defaultLux = (float[]) defaultCurve.first;
            float[] defaultNits = (float[]) defaultCurve.second;
            float[] defaultBrightness = new float[defaultNits.length];
            for (int i = 0; i < defaultBrightness.length; i++) {
                defaultBrightness[i] = this.mAdjustedNitsToBrightnessSpline.interpolate(defaultNits[i]);
            }
            android.util.Pair<float[], float[]> curve = getAdjustedCurve(defaultLux, defaultBrightness, this.mUserLux, this.mUserBrightness, this.mAutoBrightnessAdjustment, this.mMaxGamma);
            float[] lux = (float[]) curve.first;
            float[] brightness = (float[]) curve.second;
            float[] nits = new float[brightness.length];
            for (int i2 = 0; i2 < nits.length; i2++) {
                nits[i2] = this.mBrightnessToAdjustedNitsSpline.interpolate(brightness[i2]);
            }
            this.mBrightnessSpline = android.util.Spline.createSpline(lux, nits);
        }

        private float getUnadjustedBrightness(float lux) {
            android.util.Pair<float[], float[]> curve = this.mConfig.getCurve();
            android.util.Spline spline = android.util.Spline.createSpline((float[]) curve.first, (float[]) curve.second);
            return this.mAdjustedNitsToBrightnessSpline.interpolate(spline.interpolate(lux));
        }

        private float correctBrightness(float brightness, java.lang.String packageName, int category) {
            android.hardware.display.BrightnessCorrection correction;
            android.hardware.display.BrightnessCorrection correction2;
            if (packageName != null && (correction2 = this.mConfig.getCorrectionByPackageName(packageName)) != null) {
                return correction2.apply(brightness);
            }
            if (category != -1 && (correction = this.mConfig.getCorrectionByCategory(category)) != null) {
                return correction.apply(brightness);
            }
            return brightness;
        }
    }
}
