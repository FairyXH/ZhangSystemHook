package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public interface IVibrationSettingsExt {
    default void init(android.content.Context context) {
    }

    default void onSystemReady() {
    }

    default void updateSettings() {
    }

    default boolean shouldIgnoreVibration(int usageHint, int ringerMode) {
        return false;
    }

    default boolean shouldVibrateForRingerModeLocked(int intensity, int ringerMode) {
        return ringerMode != 0;
    }

    default boolean shouldIgnoreVibrationForPowerSaveMode(com.android.server.vibrator.Vibration.CallerInfo callerInfo) {
        return false;
    }
}
