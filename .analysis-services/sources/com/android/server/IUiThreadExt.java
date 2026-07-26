package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IUiThreadExt {
    default boolean setThreadSchedPolicy(int tid, java.lang.String tidName, int group) {
        return false;
    }
}
