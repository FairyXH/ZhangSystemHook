package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IBroadcastRecordWrapper {
    default com.android.server.am.IBroadcastRecordExt getExtImpl() {
        return new com.android.server.am.IBroadcastRecordExt() { // from class: com.android.server.am.IBroadcastRecordWrapper.1
        };
    }

    default void setDeliveryState(int index, long runnableAt, int runnableAtReason) {
    }
}
