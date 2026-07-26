package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public interface IVibrationStepConductorWrapper {
    default com.android.server.vibrator.IVibrationStepConductorExt getExtImpl() {
        return new com.android.server.vibrator.IVibrationStepConductorExt() { // from class: com.android.server.vibrator.IVibrationStepConductorWrapper.1
        };
    }

    default void notifyVibrationAmplitudeUpdated() {
    }
}
