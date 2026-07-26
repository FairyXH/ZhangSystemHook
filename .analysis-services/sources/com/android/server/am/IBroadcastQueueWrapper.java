package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IBroadcastQueueWrapper {
    default com.android.server.am.IBroadcastQueueExt getExtImpl() {
        return new com.android.server.am.IBroadcastQueueExt() { // from class: com.android.server.am.IBroadcastQueueWrapper.1
        };
    }

    default com.android.server.am.IBroadcastQueueModernImplExt getModernExtImpl() {
        return new com.android.server.am.IBroadcastQueueModernImplExt() { // from class: com.android.server.am.IBroadcastQueueWrapper.2
        };
    }

    default void processNextBroadcastLocked(boolean fromMsg, boolean skipOomAdj) {
    }

    default void processNextBroadcast(boolean fromMsg) {
    }

    default java.lang.String getQueueName() {
        return "";
    }

    default android.util.SparseArray<com.android.server.am.BroadcastProcessQueue> getProcessQueues() {
        return null;
    }

    default void enqueueBroadcastLocked(java.util.ArrayList<com.android.server.am.BroadcastRecord> pendingBroadcasts, boolean wouldBeSkipped, boolean addFirst) {
    }
}
