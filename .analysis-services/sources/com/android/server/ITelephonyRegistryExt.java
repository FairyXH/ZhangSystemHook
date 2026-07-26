package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface ITelephonyRegistryExt {
    default boolean addProxyBinder(android.os.IBinder bpBinder, int uid, int pid) {
        return false;
    }

    default boolean removeProxyBinder(android.os.IBinder bpBinder, int uid) {
        return false;
    }
}
