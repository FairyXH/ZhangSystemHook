package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public interface IDeviceStorageMonitorServiceWrapper {
    default int msgCheckLow() {
        return 1;
    }

    default java.util.concurrent.atomic.AtomicInteger mSeq() {
        return null;
    }

    default int msgChecHigh() {
        return 2;
    }

    default long highCheckInterVal() {
        return 0L;
    }
}
