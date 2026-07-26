package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public interface DeviceStorageMonitorInternal {
    void checkMemory();

    long getMemoryLowThreshold();

    boolean isMemoryLow();
}
