package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public interface INotifierWrapper {
    default void setPendingWakeUpBroadcast(boolean value) {
    }

    default java.lang.Object getLock() {
        return new java.lang.Object();
    }

    default void updatePendingBroadcastLocked() {
    }

    default void finishPendingBroadcastLocked() {
    }
}
