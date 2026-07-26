package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IBroadcastQueueExt {
    public static final int BROADCAST_NEXT_MSG = 202;
    public static final int mAllowDebugTime = 1000;

    default int getBroadcastNextMsgValue() {
        return 202;
    }

    default java.lang.String getBroadcastQueueName() {
        return null;
    }

    default int getOrderedBroadcastsSize() {
        return 0;
    }

    default void requestProcessNextBroadcastLocked(boolean fromMsg, boolean skipOomAdj) {
    }

    default void removeNextMessages(com.android.server.am.BroadcastRecord record) {
    }

    default void setMessageDelayFlagForBroadcastRecord(com.android.server.am.BroadcastRecord record, boolean flagValue) {
    }

    default boolean getMessageDelayFlagOfBroadcastRecord(com.android.server.am.BroadcastRecord record) {
        return false;
    }

    default void handleNextBroadcastMsg(com.android.server.am.BroadcastQueue queue, com.android.server.am.ActivityManagerService ams, android.os.Message msg, java.lang.String logTag) {
    }

    default android.os.Looper createBroadcastLooper(android.os.Handler handler) {
        return null;
    }

    default boolean optimizationBroadcast(com.android.server.am.ActivityManagerService ams, com.android.server.am.BroadcastRecord r, com.android.server.am.ProcessRecord app, android.os.Handler handler, java.lang.String TAG, java.lang.String TAG_BROADCAST) {
        return false;
    }

    default boolean broadcastIntentMissing(android.os.Handler handler) {
        return false;
    }

    default boolean removeNextBroadcastMessage(android.os.Handler handler, com.android.server.am.BroadcastRecord r) {
        return false;
    }

    default void handleBroadcastDeliverException(com.android.server.am.ActivityManagerService ams, com.android.server.am.ProcessRecord app, java.lang.String TAG) {
    }

    default void setLastTimeForDispatchMsg(long time) {
    }

    default long getLastTimeForDispatchMsg() {
        return 0L;
    }

    default boolean isAllowedBySystem(com.android.server.am.BroadcastRecord r, java.lang.Object o) {
        return true;
    }

    default android.util.Printer getLogPrinterForMsgDump() {
        return new android.util.LogPrinter(3, "IBroadcastQueueExt");
    }

    default void hookEnqueueParallelBroadcast(java.util.ArrayList<com.android.server.am.BroadcastRecord> broadcastsQueue, com.android.server.am.BroadcastRecord r, java.lang.String logTag) {
    }

    default void hookSkipCurrentReceiver(com.android.server.am.BroadcastQueue queue, com.android.server.am.ProcessRecord app) {
    }

    default boolean hookSkipDeliverReceiver(com.android.server.am.BroadcastQueue broadcastQueue, com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo info, com.android.server.am.BroadcastFilter filter, boolean skip, boolean registered) {
        return skip;
    }

    default boolean hookSkipDeliverReceiverAtTail(com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo info, boolean skip) {
        return skip;
    }

    default boolean hookSkipDeliverReceiverAtTail(android.content.Context context, com.android.server.am.BroadcastQueue queue, com.android.server.am.ProcessRecord app, java.lang.String targetProcess, com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo info, java.lang.String logTag, boolean skip) {
        return skip;
    }

    default void adjustParallelBroadcastReceiversQueue(com.android.server.am.BroadcastRecord r) {
    }

    default void adjustOrderedBroadcastReceiversQueue(com.android.server.am.BroadcastRecord r, int numReceivers) {
    }

    default boolean shouldPreventStartProcessForBroadcast(android.content.Context context, com.android.server.am.BroadcastQueue queue, com.android.server.am.ProcessRecord app, java.lang.String targetProcess, com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo info, java.lang.String logTag) {
        return false;
    }

    default void monitorAppStartupInfo(com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo info) {
    }

    default boolean isProxyBySystem(boolean isParallel, com.android.server.am.BroadcastRecord r, java.lang.Object target) {
        return false;
    }

    default void hookAfterPerformReceive(com.android.server.am.BroadcastRecord r, com.android.server.am.BroadcastFilter filter, com.android.server.am.ProcessRecord callerApp) {
    }

    default void hookAfterScheduleCurReceiver(com.android.server.am.BroadcastRecord r, com.android.server.am.ProcessRecord calledApp) {
    }

    default void handleScheduleCurReceiver(com.android.server.am.BroadcastRecord br, boolean procStart) {
    }

    default void killPhantomProcessWhenUidChanged(com.android.server.am.ActivityManagerService ams, com.android.server.am.ProcessRecord app) {
    }

    default void deliverBrComplete(boolean registered, boolean order) {
    }
}
