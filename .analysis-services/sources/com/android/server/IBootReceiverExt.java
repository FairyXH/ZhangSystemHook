package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IBootReceiverExt {
    default void notifyOTAUpdateResult(android.content.Context context) {
    }

    default void init(android.content.Context context) {
    }

    default void recordAbnormalRestart(android.os.DropBoxManager db) {
    }

    default void addFile(android.os.DropBoxManager db, java.util.HashMap<java.lang.String, java.lang.Long> timestamps, java.lang.String headers, android.content.Context ctx) {
    }

    default void incrementCriticalDataAndRecordRebootBlocked() {
    }

    default void initPowerkeyMonitor() {
    }

    default void syncCacheToEmmc() {
    }

    default void hookAddTombstoneToDropBox(java.lang.String string) {
    }
}
