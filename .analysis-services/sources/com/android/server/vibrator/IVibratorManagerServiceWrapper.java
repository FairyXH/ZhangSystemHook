package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public interface IVibratorManagerServiceWrapper {
    default com.android.server.vibrator.IVibratorManagerServiceExt getExtImpl() {
        return new com.android.server.vibrator.IVibratorManagerServiceExt() { // from class: com.android.server.vibrator.IVibratorManagerServiceWrapper.1
        };
    }

    default boolean isDebuggable() {
        return false;
    }

    default void setDebuggable(boolean enable) {
    }

    default com.android.server.vibrator.VibrationStepConductor getCurrentVibrationStepConductor() {
        return null;
    }

    default android.util.SparseArray<com.android.server.vibrator.VibratorController> getVibrators() {
        return null;
    }

    default com.android.server.vibrator.InputDeviceDelegate getInputDeviceDelegate() {
        return null;
    }

    default java.lang.Object getSyncLock() {
        return null;
    }

    default android.os.PowerManager.WakeLock getVibratorPartialWakeLock() {
        return null;
    }

    default android.os.Handler getHandler() {
        return null;
    }

    default void noteVibratorOnExtImpl(int uid, long duration) {
    }

    default void noteVibratorOffExtImpl(int uid) {
    }

    default com.android.server.vibrator.VibrationSettings getVibrationSettings() {
        return null;
    }
}
