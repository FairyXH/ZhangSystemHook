package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IProcessRecordWrapper {
    default com.android.server.am.IProcessRecordExt getExtImpl() {
        return new com.android.server.am.IProcessRecordExt() { // from class: com.android.server.am.IProcessRecordWrapper.1
        };
    }

    default int getRenderThreadTid() {
        return 0;
    }

    default android.util.IntArray getHwuiTaskThreads() {
        return null;
    }

    default long getLastActivityTime() {
        return 0L;
    }
}
