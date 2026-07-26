package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public interface IGnssListenerMultiplexerExt {
    default boolean addProxyBinder(android.os.IBinder bpBinder, android.os.IInterface iInterface, int uid, int pid) {
        return false;
    }

    default boolean removeProxyBinder(android.os.IBinder bpBinder, android.os.IInterface iInterface) {
        return false;
    }
}
