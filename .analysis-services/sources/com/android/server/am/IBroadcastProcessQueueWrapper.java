package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IBroadcastProcessQueueWrapper {
    default com.android.server.am.IBroadcastProcessQueueExt getExtImpl() {
        return new com.android.server.am.IBroadcastProcessQueueExt() { // from class: com.android.server.am.IBroadcastProcessQueueWrapper.1
        };
    }

    default void enqueueBroadcast(com.android.server.am.BroadcastRecord record, int recordIndex, boolean addFirst, com.android.server.am.BroadcastProcessQueue.BroadcastConsumer deferredStatesApplyConsumer) {
    }

    default long getRunnableAtWithoutRefresh() {
        return 0L;
    }

    default int getRunnableAtReasonWithoutRefresh() {
        return 0;
    }
}
