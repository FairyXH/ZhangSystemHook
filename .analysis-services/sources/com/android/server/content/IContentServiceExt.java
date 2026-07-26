package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
public interface IContentServiceExt {
    default int checkUserHandle(java.lang.String authority, int userHandle) {
        return userHandle;
    }

    default boolean addProxyBinder(android.os.IBinder bpBinder, int uid, int pid) {
        return true;
    }

    default boolean removeProxyBinder(android.os.IBinder bpBinder, int uid) {
        return true;
    }

    default boolean interceptRegisterContentObserver(android.net.Uri uri, int uid, int pid) {
        return false;
    }
}
