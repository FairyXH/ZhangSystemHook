package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IWatchdogSocExt {
    default void getExceptionLog() {
    }

    default void WDTMatterJava(long lParam) {
    }

    default void switchFtrace(int config) {
    }

    default long getSfHangTime() {
        return 0L;
    }

    default int getSfRebootTime() {
        return 0;
    }

    default void setSfRebootTime() {
    }
}
