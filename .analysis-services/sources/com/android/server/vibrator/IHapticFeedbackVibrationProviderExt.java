package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public interface IHapticFeedbackVibrationProviderExt {
    default void init() {
    }

    default android.os.VibrationEffect getOverrideVibrationForHapticFeedback(int effectId) {
        return null;
    }
}
