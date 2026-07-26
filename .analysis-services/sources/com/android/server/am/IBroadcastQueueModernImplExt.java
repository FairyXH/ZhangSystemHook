package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IBroadcastQueueModernImplExt {
    public static final int BLOCK_UNTIL_OPT_IGNORE_ALL = 2;
    public static final int BLOCK_UNTIL_OPT_IGNORE_CURRENT = 1;
    public static final int BLOCK_UNTIL_OPT_NONE = 0;
    public static final int DELIVERY_GROUP_POLICY = 1;
    public static final int REPLACE_PENDING = 2;

    default void initArgs(com.android.server.am.ActivityManagerService service, android.os.Handler handler) {
    }

    default java.lang.String skipScheduleReceiverColdLocked(com.android.server.am.BroadcastQueue queue, com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo info) {
        return null;
    }

    default java.lang.String skipScheduleReceiverColdLocked(com.android.server.am.BroadcastQueue queue, com.android.server.am.BroadcastProcessQueue bpq, com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo info) {
        return null;
    }

    default void cleanupDisabledPackageReceiversLocked(java.lang.String packageName, java.util.Set<java.lang.String> filterByClasses, int userId) {
    }

    default java.lang.String skipScheduleReceiverWarmLocked(com.android.server.am.BroadcastQueue queue, com.android.server.am.BroadcastRecord r, java.lang.Object receiver) {
        return null;
    }

    default void handleEnqueuedBroadcastOption(com.android.server.am.BroadcastRecord r, int type, int receiverIndex) {
    }

    default boolean shouldSkipReceiver(com.android.server.am.BroadcastRecord r, java.lang.Object receiver) {
        return false;
    }

    default void broadcastStatistic(com.android.server.am.ProcessRecord app, com.android.server.am.BroadcastRecord r, java.lang.Object receiver, int procState) {
    }

    default boolean skipReceiverForOsense(com.android.server.am.BroadcastRecord r, java.lang.Object receiver, boolean processWarm) {
        return false;
    }

    default void beginAssertHealthLocked() {
    }

    default void assertHealthLocked(com.android.server.am.BroadcastProcessQueue leaf) {
    }

    default void endAssertHealthLocked(android.util.SparseArray<com.android.server.am.BroadcastProcessQueue> processQueues, android.os.Handler handler) {
    }

    default void dumpsys(java.io.PrintWriter pw, java.lang.String[] args) {
    }

    default void handleBroadcastTimeout(com.android.server.am.BroadcastRecord r, com.android.server.am.ProcessRecord app, int index) {
    }

    default boolean ignoreAnr(com.android.server.am.ProcessRecord app, com.android.server.am.BroadcastRecord r) {
        return false;
    }

    default void hookScheduleReceiverColdAfterStartProc(com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo info) {
    }

    default void hookCreateProcessQueue(com.android.server.am.BroadcastProcessQueue created) {
    }

    default boolean shouldIgnoreTempWhitelistChange(int uid, java.lang.String pkgName, boolean added, boolean isAppOnWhitelist) {
        return false;
    }

    default int getOptDeliverPolicy(com.android.server.am.BroadcastRecord r, int index, int curIndex) {
        return 0;
    }
}
