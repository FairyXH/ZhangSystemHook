package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class VibrationScaler {
    static final float ADAPTIVE_SCALE_NONE = 1.0f;
    private static final float SCALE_FACTOR_HIGH = 1.2f;
    private static final float SCALE_FACTOR_LOW = 0.8f;
    private static final float SCALE_FACTOR_NONE = 1.0f;
    private static final float SCALE_FACTOR_VERY_HIGH = 1.4f;
    private static final float SCALE_FACTOR_VERY_LOW = 0.6f;
    static final int SCALE_HIGH = 1;
    private static final com.android.server.vibrator.VibrationScaler.ScaleLevel SCALE_LEVEL_NONE = new com.android.server.vibrator.VibrationScaler.ScaleLevel(1.0f);
    static final int SCALE_LOW = -1;
    static final int SCALE_NONE = 0;
    static final int SCALE_VERY_HIGH = 2;
    static final int SCALE_VERY_LOW = -2;
    private static final java.lang.String TAG = "VibrationScaler";
    private static final int VIBRATE_VERY_HIGH = 2400;
    private final int mDefaultVibrationAmplitude;
    private final com.android.server.vibrator.VibrationSettings mSettingsController;
    private final android.util.SparseArray<java.lang.Float> mAdaptiveHapticsScales = new android.util.SparseArray<>();
    private final android.util.SparseArray<com.android.server.vibrator.VibrationScaler.ScaleLevel> mScaleLevels = new android.util.SparseArray<>();

    VibrationScaler(android.content.Context context, com.android.server.vibrator.VibrationSettings settingsController) {
        this.mSettingsController = settingsController;
        this.mDefaultVibrationAmplitude = context.getResources().getInteger(android.R.integer.config_defaultPictureInPictureGravity);
        this.mScaleLevels.put(-2, new com.android.server.vibrator.VibrationScaler.ScaleLevel(SCALE_FACTOR_VERY_LOW));
        this.mScaleLevels.put(-1, new com.android.server.vibrator.VibrationScaler.ScaleLevel(SCALE_FACTOR_LOW));
        this.mScaleLevels.put(0, SCALE_LEVEL_NONE);
        this.mScaleLevels.put(1, new com.android.server.vibrator.VibrationScaler.ScaleLevel(SCALE_FACTOR_HIGH));
        this.mScaleLevels.put(2, new com.android.server.vibrator.VibrationScaler.ScaleLevel(SCALE_FACTOR_VERY_HIGH));
    }

    public int getDefaultVibrationAmplitude() {
        return this.mDefaultVibrationAmplitude;
    }

    public int getScaleLevel(int usageHint) {
        this.mSettingsController.getDefaultIntensity(usageHint);
        int currentIntensity = this.mSettingsController.getCurrentIntensity(usageHint);
        if (currentIntensity == 0) {
            return 0;
        }
        if (currentIntensity < -2 || currentIntensity > 2400) {
            android.util.Slog.wtf(TAG, "Error in scaling calculations, ended up with invalid scale level " + currentIntensity + " for vibration with usage " + usageHint);
            return 0;
        }
        if (com.android.server.vibrator.VibrationThread.DEBUG) {
            android.util.Slog.d(TAG, "scaleLevel = " + currentIntensity);
        }
        return currentIntensity;
    }

    public float getAdaptiveHapticsScale(int usageHint) {
        if (android.os.vibrator.Flags.adaptiveHapticsEnabled()) {
            return this.mAdaptiveHapticsScales.get(usageHint, java.lang.Float.valueOf(1.0f)).floatValue();
        }
        return 1.0f;
    }

    public android.os.VibrationEffect scale(android.os.VibrationEffect effect, int usageHint) {
        if (!(effect instanceof android.os.VibrationEffect.Composed)) {
            android.util.Slog.wtf(TAG, "Error scaling unsupported vibration effect: " + effect);
            return effect;
        }
        int newEffectStrength = getEffectStrength(usageHint);
        com.android.server.vibrator.VibrationScaler.ScaleLevel scaleLevel = this.mScaleLevels.get(getScaleLevel(usageHint));
        float adaptiveScale = getAdaptiveHapticsScale(usageHint);
        if (scaleLevel == null) {
            android.util.Slog.e(TAG, "No configured scaling level found! (current=" + this.mSettingsController.getCurrentIntensity(usageHint) + ", default= " + this.mSettingsController.getDefaultIntensity(usageHint) + ")");
            scaleLevel = SCALE_LEVEL_NONE;
        }
        android.os.VibrationEffect.Composed composedEffect = (android.os.VibrationEffect.Composed) effect;
        java.util.ArrayList<android.os.vibrator.VibrationEffectSegment> segments = new java.util.ArrayList<>(composedEffect.getSegments());
        int segmentCount = segments.size();
        for (int i = 0; i < segmentCount; i++) {
            segments.set(i, segments.get(i).resolve(this.mDefaultVibrationAmplitude).applyEffectStrength(newEffectStrength).scale(scaleLevel.factor).scaleLinearly(adaptiveScale));
        }
        if (segments.equals(composedEffect.getSegments())) {
            return effect;
        }
        android.os.VibrationEffect.Composed scaled = new android.os.VibrationEffect.Composed(segments, composedEffect.getRepeatIndex());
        scaled.validate();
        return scaled;
    }

    public android.os.vibrator.PrebakedSegment scale(android.os.vibrator.PrebakedSegment prebaked, int usageHint) {
        return prebaked.applyEffectStrength(getEffectStrength(usageHint));
    }

    public void updateAdaptiveHapticsScale(int usageHint, float scale) {
        this.mAdaptiveHapticsScales.put(usageHint, java.lang.Float.valueOf(scale));
    }

    public void removeAdaptiveHapticsScale(int usageHint) {
        this.mAdaptiveHapticsScales.remove(usageHint);
    }

    public void clearAdaptiveHapticsScales() {
        this.mAdaptiveHapticsScales.clear();
    }

    void dump(android.util.IndentingPrintWriter pw) {
        pw.println("VibrationScaler:");
        pw.increaseIndent();
        pw.println("defaultVibrationAmplitude = " + this.mDefaultVibrationAmplitude);
        pw.println("ScaleLevels:");
        pw.increaseIndent();
        for (int i = 0; i < this.mScaleLevels.size(); i++) {
            int scaleLevelKey = this.mScaleLevels.keyAt(i);
            com.android.server.vibrator.VibrationScaler.ScaleLevel scaleLevel = this.mScaleLevels.valueAt(i);
            pw.println(scaleLevelToString(scaleLevelKey) + " = " + scaleLevel);
        }
        pw.decreaseIndent();
        pw.println("AdaptiveHapticsScales:");
        pw.increaseIndent();
        for (int i2 = 0; i2 < this.mAdaptiveHapticsScales.size(); i2++) {
            int usage = this.mAdaptiveHapticsScales.keyAt(i2);
            float scale = this.mAdaptiveHapticsScales.valueAt(i2).floatValue();
            pw.println(android.os.VibrationAttributes.usageToString(usage) + " = " + java.lang.String.format(java.util.Locale.ROOT, "%.2f", java.lang.Float.valueOf(scale)));
        }
        pw.decreaseIndent();
        pw.decreaseIndent();
    }

    void dump(android.util.proto.ProtoOutputStream proto) {
        proto.write(1120986464282L, this.mDefaultVibrationAmplitude);
    }

    public java.lang.String toString() {
        return "VibrationScaler{mScaleLevels=" + this.mScaleLevels + ", mDefaultVibrationAmplitude=" + this.mDefaultVibrationAmplitude + ", mAdaptiveHapticsScales=" + this.mAdaptiveHapticsScales + '}';
    }

    private int getEffectStrength(int usageHint) {
        int currentIntensity = this.mSettingsController.getCurrentIntensity(usageHint);
        if (currentIntensity == 0) {
            currentIntensity = this.mSettingsController.getDefaultIntensity(usageHint);
        }
        return intensityToEffectStrength(currentIntensity);
    }

    private static int intensityToEffectStrength(int intensity) {
        switch (intensity) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                android.util.Slog.w(TAG, "Got unexpected vibration intensity: " + intensity);
                break;
        }
        return 2;
    }

    static java.lang.String scaleLevelToString(int scaleLevel) {
        switch (scaleLevel) {
            case -2:
                return "VERY_LOW";
            case -1:
                return "LOW";
            case 0:
                return "NONE";
            case 1:
                return com.android.server.utils.PriorityDump.PRIORITY_ARG_HIGH;
            case 2:
                return "VERY_HIGH";
            default:
                return java.lang.String.valueOf(scaleLevel);
        }
    }

    private static final class ScaleLevel {
        public final float factor;

        ScaleLevel(float factor) {
            this.factor = factor;
        }

        public java.lang.String toString() {
            return "ScaleLevel{factor=" + this.factor + "}";
        }
    }
}
