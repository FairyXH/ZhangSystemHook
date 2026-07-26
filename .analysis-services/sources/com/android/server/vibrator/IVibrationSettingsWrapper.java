package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public interface IVibrationSettingsWrapper {
    default com.android.server.vibrator.IVibrationSettingsExt getExtImpl() {
        return new com.android.server.vibrator.IVibrationSettingsExt() { // from class: com.android.server.vibrator.IVibrationSettingsWrapper.1
        };
    }

    default void registerSettingsObserverExt(android.net.Uri settingUri) {
    }

    default void updateTouchUsageVibrationIntensity(int intensity) {
    }

    default void updateNotificationUsageVibrationIntensity(int intensity) {
    }

    default void updateRingtoneUsageVibrationIntensity(int intensity) {
    }
}
