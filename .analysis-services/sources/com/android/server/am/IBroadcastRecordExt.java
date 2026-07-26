package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IBroadcastRecordExt {
    default int getCtrlType() {
        return -1;
    }

    default void setCtrlType(int mCtrlType) {
    }

    default boolean getIgnoreBrOpt() {
        return false;
    }

    default void setIgnoreBrOpt(boolean value) {
    }

    default android.content.Intent setAndGetBackupIntent(android.content.Intent intent) {
        return null;
    }

    default void init(int deliveryLength) {
    }

    default void setDeliveryState(int index, long runnableAt, int runnableAtReason) {
    }

    default void dumpDeliveryState(java.io.PrintWriter pw, int index) {
    }

    default void setSkipReason(com.android.server.am.BroadcastRecord record, int index, int newDeliveryState, java.lang.String reason) {
    }

    default boolean ignoreBlockUntil(android.content.Intent intent) {
        return false;
    }

    default int[] calculateBlockedUntilBeyondCount(java.util.List<java.lang.Object> receivers, boolean ordered, android.content.Intent brIntent) {
        return com.android.server.am.BroadcastRecord.calculateBlockedUntilBeyondCount(receivers, ordered);
    }
}
