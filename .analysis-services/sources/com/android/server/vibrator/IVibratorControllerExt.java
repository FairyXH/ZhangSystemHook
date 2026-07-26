package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public interface IVibratorControllerExt {
    default void setOnVibrationCompleteListener(com.android.server.vibrator.VibratorController.OnVibrationCompleteListener listener) {
    }

    default void richtapSetAmplitude(int amplitude) {
    }

    default void richtapPerformHe(int looper, int interval, int amplitude, int freq, int[] he, long vibrationId) {
    }

    default void richtapPerformEnvelope(int[] envInfo, boolean fastFlag, int amplitude, long vibrationId) {
    }

    default int richtapPerformEffect(int effectId, byte strength, long vibrationId) {
        return -1;
    }

    default void richtapStop() {
    }

    default void linearMotorVibratorOff() {
    }

    default void linearMotorVibratorOn(int waveformId, int amplitude, boolean isRTPMode, long vibrationId) {
    }

    default void linearMotorVibratorSetVmax(int strength) {
    }
}
