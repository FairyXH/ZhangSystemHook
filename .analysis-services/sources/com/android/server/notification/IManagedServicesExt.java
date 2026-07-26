package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface IManagedServicesExt {
    default boolean isInterceptRebindServices(boolean forceRebind, int userToRebind) {
        return false;
    }
}
