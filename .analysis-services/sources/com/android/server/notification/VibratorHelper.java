package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public final class VibratorHelper {
    private static final long[] DEFAULT_VIBRATE_PATTERN = {0, 200, 200, 0};
    private static final java.lang.String TAG = "NotificationVibratorHelper";
    private static final int VIBRATE_PATTERN_MAXLEN = 17;
    private final long[] mDefaultPattern;
    private final float[] mDefaultPwlePattern;
    private final int mDefaultVibrationAmplitude;
    private final long[] mFallbackPattern;
    private final float[] mFallbackPwlePattern;
    private final android.os.Vibrator mVibrator;

    public VibratorHelper(android.content.Context context) {
        this.mVibrator = (android.os.Vibrator) context.getSystemService(android.os.Vibrator.class);
        this.mDefaultPattern = getLongArray(context.getResources(), android.R.array.config_defaultCloudSearchServices, 17, DEFAULT_VIBRATE_PATTERN);
        this.mFallbackPattern = getLongArray(context.getResources(), android.R.array.config_nightDisplayColorTemperatureCoefficients, 17, DEFAULT_VIBRATE_PATTERN);
        this.mDefaultPwlePattern = getFloatArray(context.getResources(), android.R.array.config_defaultFirstUserRestrictions);
        this.mFallbackPwlePattern = getFloatArray(context.getResources(), android.R.array.config_nightDisplayColorTemperatureCoefficientsNative);
        this.mDefaultVibrationAmplitude = context.getResources().getInteger(android.R.integer.config_defaultPictureInPictureGravity);
    }

    public static android.os.VibrationEffect createWaveformVibration(long[] pattern, boolean insistent) {
        if (pattern != null) {
            try {
                return android.os.VibrationEffect.createWaveform(pattern, insistent ? 0 : -1);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.e(TAG, "Error creating vibration waveform with pattern: " + java.util.Arrays.toString(pattern));
                return null;
            }
        }
        return null;
    }

    public static android.os.VibrationEffect createPwleWaveformVibration(float[] values, boolean insistent) {
        if (values == null) {
            return null;
        }
        try {
            int length = values.length;
            if (length != 0 && length % 3 == 0) {
                android.os.VibrationEffect.WaveformBuilder waveformBuilder = android.os.VibrationEffect.startWaveform();
                for (int i = 0; i < length; i += 3) {
                    waveformBuilder.addTransition(java.time.Duration.ofMillis((int) values[i + 2]), android.os.VibrationEffect.VibrationParameter.targetAmplitude(values[i]), android.os.VibrationEffect.VibrationParameter.targetFrequency(values[i + 1]));
                }
                android.os.VibrationEffect effect = waveformBuilder.build();
                if (insistent) {
                    return android.os.VibrationEffect.startComposition().repeatEffectIndefinitely(effect).compose();
                }
                return effect;
            }
            return null;
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Error creating vibration PWLE waveform with pattern: " + java.util.Arrays.toString(values));
            return null;
        }
    }

    public android.os.VibrationEffect scale(android.os.VibrationEffect effect, float scale) {
        return effect.resolve(this.mDefaultVibrationAmplitude).scale(scale);
    }

    public void vibrate(android.os.VibrationEffect effect, android.media.AudioAttributes attrs, java.lang.String reason) {
        this.mVibrator.vibrate(1000, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, effect, reason, new android.os.VibrationAttributes.Builder(attrs).build());
    }

    public void cancelVibration() {
        this.mVibrator.cancel(-15);
    }

    public android.os.VibrationEffect createFallbackVibration(boolean insistent) {
        android.os.VibrationEffect effect;
        if (this.mVibrator.hasFrequencyControl() && (effect = createPwleWaveformVibration(this.mFallbackPwlePattern, insistent)) != null) {
            return effect;
        }
        return createWaveformVibration(this.mFallbackPattern, insistent);
    }

    public android.os.VibrationEffect createDefaultVibration(boolean insistent) {
        android.os.VibrationEffect effect;
        if (this.mVibrator.hasFrequencyControl() && (effect = createPwleWaveformVibration(this.mDefaultPwlePattern, insistent)) != null) {
            return effect;
        }
        return createWaveformVibration(this.mDefaultPattern, insistent);
    }

    public boolean areEffectComponentsSupported(android.os.VibrationEffect effect) {
        return this.mVibrator.areVibrationFeaturesSupported(effect);
    }

    private static float[] getFloatArray(android.content.res.Resources resources, int resId) {
        android.content.res.TypedArray array = resources.obtainTypedArray(resId);
        try {
            float[] values = new float[array.length()];
            for (int i = 0; i < values.length; i++) {
                values[i] = array.getFloat(i, Float.NaN);
                if (java.lang.Float.isNaN(values[i])) {
                    array.recycle();
                    return null;
                }
            }
            return values;
        } finally {
            array.recycle();
        }
    }

    private static long[] getLongArray(android.content.res.Resources resources, int resId, int maxLength, long[] def) {
        int[] ar = resources.getIntArray(resId);
        if (ar == null) {
            return def;
        }
        int len = ar.length > maxLength ? maxLength : ar.length;
        long[] out = new long[len];
        for (int i = 0; i < len; i++) {
            out[i] = ar[i];
        }
        return out;
    }
}
