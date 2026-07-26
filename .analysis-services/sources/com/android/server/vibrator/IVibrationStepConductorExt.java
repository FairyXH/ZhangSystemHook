package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public interface IVibrationStepConductorExt {
    public static final float DEFAULT_AMPLITUDE_RATIO = 1.0f;

    default float getVibrationAmplitudeRatio() {
        return 1.0f;
    }

    default void setVibrationAmplitudeRatio(float ratio) {
    }

    default void updateVibrationAmplitude(com.android.server.vibrator.Step nextStep) {
    }

    default com.android.server.vibrator.AbstractVibratorStep nextVibrateStep(android.os.vibrator.VibrationEffectSegment segment, long startTime, com.android.server.vibrator.VibratorController controller, android.os.VibrationEffect.Composed effect, int segmentIndex, long previousStepVibratorOffTimeout) {
        return null;
    }
}
