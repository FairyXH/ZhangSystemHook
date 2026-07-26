package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IBroadcastProcessQueueExt {
    public static final int REASON_OPLUS_LIST = 101;
    public static final java.lang.String REASON_OPLUS_LIST_NAME = "OPLUS_LIST";

    default java.util.ArrayList<java.lang.String> beginAssertHealthLocked() {
        return null;
    }

    default void assertHealthLocked(com.android.server.am.BroadcastRecord record, long waitingTime, java.util.ArrayList<java.lang.String> brs) {
    }

    default void endAssertHealthLocked(android.content.Context context) {
    }

    default long getCustomizedRunnableAt(long originalRunnableAt) {
        return Long.MAX_VALUE;
    }

    default void setCustomizedRunnableAtDelayMillis(long runnableAtDelayMillis) {
    }

    default void updateRunnableAtEnd(int oldRunnableAt, int newRunnableAt) {
    }

    default void dispatchReceiverFinish(com.android.server.am.BroadcastRecord broadcastRecord, int index) {
    }

    public interface IStaticExt {
        default java.lang.String reasonToStringExtend(int reason) {
            return null;
        }
    }
}
