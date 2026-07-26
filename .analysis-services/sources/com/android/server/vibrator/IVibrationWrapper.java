package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public interface IVibrationWrapper {
    default void setVibrationPid(int pid) {
    }

    default int getVibrationPid() {
        return -1;
    }
}
