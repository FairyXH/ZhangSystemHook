package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IDropBoxManagerServiceExt {
    default void init(android.content.Context context) {
    }

    default void dumpLowStorageLog(long available, long nonreserved, int quotaPercent, int mBlockSize, int maximum) {
    }

    default void addDropBoxFile(long time, java.lang.String tag, int flags) {
    }

    default void addSystemLogFile(java.lang.String tag, byte[] data, int flags) {
    }

    default void handleEapData(java.lang.String tag, byte[] data, int flags) {
    }
}
