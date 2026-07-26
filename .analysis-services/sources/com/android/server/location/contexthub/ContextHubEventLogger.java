package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
public class ContextHubEventLogger {
    public static final int NUM_EVENTS_TO_STORE = 20;
    private static final java.lang.String TAG = "ContextHubEventLogger";
    private static com.android.server.location.contexthub.ContextHubEventLogger sInstance = null;
    private final com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<com.android.server.location.contexthub.ContextHubEventLogger.NanoappLoadEvent> mNanoappLoadEventQueue = new com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<>(20);
    private final com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<com.android.server.location.contexthub.ContextHubEventLogger.NanoappUnloadEvent> mNanoappUnloadEventQueue = new com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<>(20);
    private final com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<com.android.server.location.contexthub.ContextHubEventLogger.NanoappMessageEvent> mMessageFromNanoappQueue = new com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<>(20);
    private final com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<com.android.server.location.contexthub.ContextHubEventLogger.NanoappMessageEvent> mMessageToNanoappQueue = new com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<>(20);
    private final com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<com.android.server.location.contexthub.ContextHubEventLogger.ContextHubRestartEvent> mContextHubRestartEventQueue = new com.android.server.location.contexthub.ConcurrentLinkedEvictingDeque<>(20);

    public static class ContextHubEventBase {
        public final int contextHubId;
        public final long timeStampInMs;

        public ContextHubEventBase(long mTimeStampInMs, int mContextHubId) {
            this.timeStampInMs = mTimeStampInMs;
            this.contextHubId = mContextHubId;
        }
    }

    public static class NanoappEventBase extends com.android.server.location.contexthub.ContextHubEventLogger.ContextHubEventBase {
        public final long nanoappId;
        public final boolean success;

        public NanoappEventBase(long mTimeStampInMs, int mContextHubId, long mNanoappId, boolean mSuccess) {
            super(mTimeStampInMs, mContextHubId);
            this.nanoappId = mNanoappId;
            this.success = mSuccess;
        }
    }

    public static class NanoappLoadEvent extends com.android.server.location.contexthub.ContextHubEventLogger.NanoappEventBase {
        public final long nanoappSize;
        public final int nanoappVersion;

        public NanoappLoadEvent(long mTimeStampInMs, int mContextHubId, long mNanoappId, int mNanoappVersion, long mNanoappSize, boolean mSuccess) {
            super(mTimeStampInMs, mContextHubId, mNanoappId, mSuccess);
            this.nanoappVersion = mNanoappVersion;
            this.nanoappSize = mNanoappSize;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(com.android.server.location.contexthub.ContextHubServiceUtil.formatDateFromTimestamp(this.timeStampInMs));
            sb.append(": NanoappLoadEvent[hubId = ");
            sb.append(this.contextHubId);
            sb.append(", appId = 0x");
            sb.append(java.lang.Long.toHexString(this.nanoappId));
            sb.append(", appVersion = ");
            sb.append(this.nanoappVersion);
            sb.append(", appSize = ");
            sb.append(this.nanoappSize);
            sb.append(" bytes, success = ");
            sb.append(this.success ? "true" : "false");
            sb.append(']');
            return sb.toString();
        }
    }

    public static class NanoappUnloadEvent extends com.android.server.location.contexthub.ContextHubEventLogger.NanoappEventBase {
        public NanoappUnloadEvent(long mTimeStampInMs, int mContextHubId, long mNanoappId, boolean mSuccess) {
            super(mTimeStampInMs, mContextHubId, mNanoappId, mSuccess);
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(com.android.server.location.contexthub.ContextHubServiceUtil.formatDateFromTimestamp(this.timeStampInMs));
            sb.append(": NanoappUnloadEvent[hubId = ");
            sb.append(this.contextHubId);
            sb.append(", appId = 0x");
            sb.append(java.lang.Long.toHexString(this.nanoappId));
            sb.append(", success = ");
            sb.append(this.success ? "true" : "false");
            sb.append(']');
            return sb.toString();
        }
    }

    public static class NanoappMessageEvent extends com.android.server.location.contexthub.ContextHubEventLogger.NanoappEventBase {
        public final android.hardware.location.NanoAppMessage message;

        public NanoappMessageEvent(long mTimeStampInMs, int mContextHubId, android.hardware.location.NanoAppMessage mMessage, boolean mSuccess) {
            super(mTimeStampInMs, mContextHubId, 0L, mSuccess);
            this.message = mMessage;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(com.android.server.location.contexthub.ContextHubServiceUtil.formatDateFromTimestamp(this.timeStampInMs));
            sb.append(": NanoappMessageEvent[hubId = ");
            sb.append(this.contextHubId);
            sb.append(", ");
            sb.append(this.message.toString());
            sb.append(", success = ");
            sb.append(this.success ? "true" : "false");
            sb.append(']');
            return sb.toString();
        }
    }

    public static class ContextHubRestartEvent extends com.android.server.location.contexthub.ContextHubEventLogger.ContextHubEventBase {
        public ContextHubRestartEvent(long mTimeStampInMs, int mContextHubId) {
            super(mTimeStampInMs, mContextHubId);
        }

        public java.lang.String toString() {
            return com.android.server.location.contexthub.ContextHubServiceUtil.formatDateFromTimestamp(this.timeStampInMs) + ": ContextHubRestartEvent[hubId = " + this.contextHubId + ']';
        }
    }

    private ContextHubEventLogger() {
    }

    public static synchronized com.android.server.location.contexthub.ContextHubEventLogger getInstance() {
        if (sInstance == null) {
            sInstance = new com.android.server.location.contexthub.ContextHubEventLogger();
        }
        return sInstance;
    }

    public synchronized void clear() {
        java.util.Collection<?>[] collectionArr = {this.mNanoappLoadEventQueue, this.mNanoappUnloadEventQueue, this.mMessageFromNanoappQueue, this.mMessageToNanoappQueue, this.mContextHubRestartEventQueue};
        for (int i = 0; i < 5; i++) {
            java.util.Collection<?> deque = collectionArr[i];
            deque.clear();
        }
    }

    public synchronized void logNanoappLoad(int contextHubId, long nanoappId, int nanoappVersion, long nanoappSize, boolean success) {
        long timeStampInMs = java.lang.System.currentTimeMillis();
        com.android.server.location.contexthub.ContextHubEventLogger.NanoappLoadEvent event = new com.android.server.location.contexthub.ContextHubEventLogger.NanoappLoadEvent(timeStampInMs, contextHubId, nanoappId, nanoappVersion, nanoappSize, success);
        boolean status = this.mNanoappLoadEventQueue.add(event);
        if (!status) {
            android.util.Log.e(TAG, "Unable to add nanoapp load event to queue: " + event);
        }
    }

    public synchronized void logNanoappUnload(int contextHubId, long nanoappId, boolean success) {
        long timeStampInMs = java.lang.System.currentTimeMillis();
        com.android.server.location.contexthub.ContextHubEventLogger.NanoappUnloadEvent event = new com.android.server.location.contexthub.ContextHubEventLogger.NanoappUnloadEvent(timeStampInMs, contextHubId, nanoappId, success);
        boolean status = this.mNanoappUnloadEventQueue.add(event);
        if (!status) {
            android.util.Log.e(TAG, "Unable to add nanoapp unload event to queue: " + event);
        }
    }

    public synchronized void logMessageFromNanoapp(int contextHubId, android.hardware.location.NanoAppMessage message, boolean success) {
        if (message == null) {
            return;
        }
        long timeStampInMs = java.lang.System.currentTimeMillis();
        com.android.server.location.contexthub.ContextHubEventLogger.NanoappMessageEvent event = new com.android.server.location.contexthub.ContextHubEventLogger.NanoappMessageEvent(timeStampInMs, contextHubId, message, success);
        boolean status = this.mMessageFromNanoappQueue.add(event);
        if (!status) {
            android.util.Log.e(TAG, "Unable to add message from nanoapp event to queue: " + event);
        }
    }

    public synchronized void logMessageToNanoapp(int contextHubId, android.hardware.location.NanoAppMessage message, boolean success) {
        if (message == null) {
            return;
        }
        long timeStampInMs = java.lang.System.currentTimeMillis();
        com.android.server.location.contexthub.ContextHubEventLogger.NanoappMessageEvent event = new com.android.server.location.contexthub.ContextHubEventLogger.NanoappMessageEvent(timeStampInMs, contextHubId, message, success);
        boolean status = this.mMessageToNanoappQueue.add(event);
        if (!status) {
            android.util.Log.e(TAG, "Unable to add message to nanoapp event to queue: " + event);
        }
    }

    public synchronized void logContextHubRestart(int contextHubId) {
        long timeStampInMs = java.lang.System.currentTimeMillis();
        com.android.server.location.contexthub.ContextHubEventLogger.ContextHubRestartEvent event = new com.android.server.location.contexthub.ContextHubEventLogger.ContextHubRestartEvent(timeStampInMs, contextHubId);
        boolean status = this.mContextHubRestartEventQueue.add(event);
        if (!status) {
            android.util.Log.e(TAG, "Unable to add Context Hub restart event to queue: " + event);
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("Nanoapp Loads:");
        sb.append(java.lang.System.lineSeparator());
        for (com.android.server.location.contexthub.ContextHubEventLogger.NanoappLoadEvent event : this.mNanoappLoadEventQueue) {
            sb.append(event);
            sb.append(java.lang.System.lineSeparator());
        }
        sb.append(java.lang.System.lineSeparator());
        sb.append("Nanoapp Unloads:");
        sb.append(java.lang.System.lineSeparator());
        for (com.android.server.location.contexthub.ContextHubEventLogger.NanoappUnloadEvent event2 : this.mNanoappUnloadEventQueue) {
            sb.append(event2);
            sb.append(java.lang.System.lineSeparator());
        }
        sb.append(java.lang.System.lineSeparator());
        sb.append("Messages from Nanoapps:");
        sb.append(java.lang.System.lineSeparator());
        for (com.android.server.location.contexthub.ContextHubEventLogger.NanoappMessageEvent event3 : this.mMessageFromNanoappQueue) {
            sb.append(event3);
            sb.append(java.lang.System.lineSeparator());
        }
        sb.append(java.lang.System.lineSeparator());
        sb.append("Messages to Nanoapps:");
        sb.append(java.lang.System.lineSeparator());
        for (com.android.server.location.contexthub.ContextHubEventLogger.NanoappMessageEvent event4 : this.mMessageToNanoappQueue) {
            sb.append(event4);
            sb.append(java.lang.System.lineSeparator());
        }
        sb.append(java.lang.System.lineSeparator());
        sb.append("Context Hub Restarts:");
        sb.append(java.lang.System.lineSeparator());
        for (com.android.server.location.contexthub.ContextHubEventLogger.ContextHubRestartEvent event5 : this.mContextHubRestartEventQueue) {
            sb.append(event5);
            sb.append(java.lang.System.lineSeparator());
        }
        return sb.toString();
    }
}
