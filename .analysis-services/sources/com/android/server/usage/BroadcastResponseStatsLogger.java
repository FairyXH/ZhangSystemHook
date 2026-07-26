package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class BroadcastResponseStatsLogger {
    private static final int MAX_LOG_SIZE;
    private final java.lang.Object mLock = new java.lang.Object();
    private final com.android.server.usage.BroadcastResponseStatsLogger.LogBuffer mBroadcastEventsBuffer = new com.android.server.usage.BroadcastResponseStatsLogger.LogBuffer(new java.util.function.Supplier() { // from class: com.android.server.usage.BroadcastResponseStatsLogger$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final java.lang.Object get() {
            return com.android.server.usage.BroadcastResponseStatsLogger.m9807$r8$lambda$Ar2wcV2w4QjqlPjHFaij6oV5bQ();
        }
    }, new java.util.function.IntFunction() { // from class: com.android.server.usage.BroadcastResponseStatsLogger$$ExternalSyntheticLambda1
        @Override // java.util.function.IntFunction
        public final java.lang.Object apply(int i) {
            return com.android.server.usage.BroadcastResponseStatsLogger.lambda$new$0(i);
        }
    }, MAX_LOG_SIZE);
    private final com.android.server.usage.BroadcastResponseStatsLogger.LogBuffer mNotificationEventsBuffer = new com.android.server.usage.BroadcastResponseStatsLogger.LogBuffer(new java.util.function.Supplier() { // from class: com.android.server.usage.BroadcastResponseStatsLogger$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final java.lang.Object get() {
            return com.android.server.usage.BroadcastResponseStatsLogger.m9808$r8$lambda$lW8VT65e47HoACtwf8YZwpc8Xs();
        }
    }, new java.util.function.IntFunction() { // from class: com.android.server.usage.BroadcastResponseStatsLogger$$ExternalSyntheticLambda3
        @Override // java.util.function.IntFunction
        public final java.lang.Object apply(int i) {
            return com.android.server.usage.BroadcastResponseStatsLogger.lambda$new$1(i);
        }
    }, MAX_LOG_SIZE);

    private interface Data {
        void reset();
    }

    /* JADX INFO: renamed from: $r8$lambda$A-r2wcV2w4QjqlPjHFaij6oV5bQ, reason: not valid java name */
    public static /* synthetic */ com.android.server.usage.BroadcastResponseStatsLogger.BroadcastEvent m9807$r8$lambda$Ar2wcV2w4QjqlPjHFaij6oV5bQ() {
        return new com.android.server.usage.BroadcastResponseStatsLogger.BroadcastEvent();
    }

    /* JADX INFO: renamed from: $r8$lambda$lW8VT65-e47HoACtwf8YZwpc8Xs, reason: not valid java name */
    public static /* synthetic */ com.android.server.usage.BroadcastResponseStatsLogger.NotificationEvent m9808$r8$lambda$lW8VT65e47HoACtwf8YZwpc8Xs() {
        return new com.android.server.usage.BroadcastResponseStatsLogger.NotificationEvent();
    }

    static {
        MAX_LOG_SIZE = android.app.ActivityManager.isLowRamDeviceStatic() ? 20 : 50;
    }

    static /* synthetic */ java.lang.Object lambda$new$0(int x$0) {
        return new com.android.server.usage.BroadcastResponseStatsLogger.BroadcastEvent[x$0];
    }

    static /* synthetic */ java.lang.Object lambda$new$1(int x$0) {
        return new com.android.server.usage.BroadcastResponseStatsLogger.NotificationEvent[x$0];
    }

    void logBroadcastDispatchEvent(int sourceUid, java.lang.String targetPackage, android.os.UserHandle targetUser, long idForResponseEvent, long timeStampMs, int targetUidProcessState) {
        synchronized (this.mLock) {
            if (com.android.server.usage.UsageStatsService.DEBUG_RESPONSE_STATS) {
                android.util.Slog.d("ResponseStatsTracker", getBroadcastDispatchEventLog(sourceUid, targetPackage, targetUser.getIdentifier(), idForResponseEvent, timeStampMs, targetUidProcessState));
            }
            this.mBroadcastEventsBuffer.logBroadcastDispatchEvent(sourceUid, targetPackage, targetUser, idForResponseEvent, timeStampMs, targetUidProcessState);
        }
    }

    void logNotificationEvent(int event, java.lang.String packageName, android.os.UserHandle user, long timestampMs) {
        synchronized (this.mLock) {
            if (com.android.server.usage.UsageStatsService.DEBUG_RESPONSE_STATS) {
                android.util.Slog.d("ResponseStatsTracker", getNotificationEventLog(event, packageName, user.getIdentifier(), timestampMs));
            }
            this.mNotificationEventsBuffer.logNotificationEvent(event, packageName, user, timestampMs);
        }
    }

    void dumpLogs(com.android.internal.util.IndentingPrintWriter ipw) {
        synchronized (this.mLock) {
            ipw.println("Broadcast events (most recent first):");
            ipw.increaseIndent();
            this.mBroadcastEventsBuffer.reverseDump(ipw);
            ipw.decreaseIndent();
            ipw.println();
            ipw.println("Notification events (most recent first):");
            ipw.increaseIndent();
            this.mNotificationEventsBuffer.reverseDump(ipw);
            ipw.decreaseIndent();
        }
    }

    private static final class LogBuffer<T extends com.android.server.usage.BroadcastResponseStatsLogger.Data> extends com.android.internal.util.RingBuffer<T> {
        LogBuffer(java.util.function.Supplier<T> newItem, java.util.function.IntFunction<T[]> newBacking, int capacity) {
            super(newItem, newBacking, capacity);
        }

        void logBroadcastDispatchEvent(int sourceUid, java.lang.String targetPackage, android.os.UserHandle targetUser, long idForResponseEvent, long timeStampMs, int targetUidProcessState) {
            com.android.server.usage.BroadcastResponseStatsLogger.Data data = (com.android.server.usage.BroadcastResponseStatsLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            com.android.server.usage.BroadcastResponseStatsLogger.BroadcastEvent event = (com.android.server.usage.BroadcastResponseStatsLogger.BroadcastEvent) data;
            event.sourceUid = sourceUid;
            event.targetUserId = targetUser.getIdentifier();
            event.targetUidProcessState = targetUidProcessState;
            event.targetPackage = targetPackage;
            event.idForResponseEvent = idForResponseEvent;
            event.timestampMs = timeStampMs;
        }

        void logNotificationEvent(int type, java.lang.String packageName, android.os.UserHandle user, long timestampMs) {
            com.android.server.usage.BroadcastResponseStatsLogger.Data data = (com.android.server.usage.BroadcastResponseStatsLogger.Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            com.android.server.usage.BroadcastResponseStatsLogger.NotificationEvent event = (com.android.server.usage.BroadcastResponseStatsLogger.NotificationEvent) data;
            event.type = type;
            event.packageName = packageName;
            event.userId = user.getIdentifier();
            event.timestampMs = timestampMs;
        }

        public void reverseDump(com.android.internal.util.IndentingPrintWriter pw) {
            com.android.server.usage.BroadcastResponseStatsLogger.Data[] allData = (com.android.server.usage.BroadcastResponseStatsLogger.Data[]) toArray();
            for (int i = allData.length - 1; i >= 0; i--) {
                if (allData[i] != null) {
                    pw.println(getContent(allData[i]));
                }
            }
        }

        public java.lang.String getContent(com.android.server.usage.BroadcastResponseStatsLogger.Data data) {
            return data.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getBroadcastDispatchEventLog(int sourceUid, java.lang.String targetPackage, int targetUserId, long idForResponseEvent, long timestampMs, int targetUidProcState) {
        return android.text.TextUtils.formatSimple("broadcast:%s; srcUid=%d, tgtPkg=%s, tgtUsr=%d, id=%d, state=%s", new java.lang.Object[]{android.util.TimeUtils.formatDuration(timestampMs), java.lang.Integer.valueOf(sourceUid), targetPackage, java.lang.Integer.valueOf(targetUserId), java.lang.Long.valueOf(idForResponseEvent), android.app.ActivityManager.procStateToString(targetUidProcState)});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getNotificationEventLog(int event, java.lang.String packageName, int userId, long timestampMs) {
        return android.text.TextUtils.formatSimple("notification:%s; event=<%s>, pkg=%s, usr=%d", new java.lang.Object[]{android.util.TimeUtils.formatDuration(timestampMs), notificationEventToString(event), packageName, java.lang.Integer.valueOf(userId)});
    }

    private static java.lang.String notificationEventToString(int event) {
        switch (event) {
            case 0:
                return "posted";
            case 1:
                return "updated";
            case 2:
                return "cancelled";
            default:
                return java.lang.String.valueOf(event);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class BroadcastEvent implements com.android.server.usage.BroadcastResponseStatsLogger.Data {
        public long idForResponseEvent;
        public int sourceUid;
        public java.lang.String targetPackage;
        public int targetUidProcessState;
        public int targetUserId;
        public long timestampMs;

        private BroadcastEvent() {
        }

        @Override // com.android.server.usage.BroadcastResponseStatsLogger.Data
        public void reset() {
            this.targetPackage = null;
        }

        public java.lang.String toString() {
            return com.android.server.usage.BroadcastResponseStatsLogger.getBroadcastDispatchEventLog(this.sourceUid, this.targetPackage, this.targetUserId, this.idForResponseEvent, this.timestampMs, this.targetUidProcessState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class NotificationEvent implements com.android.server.usage.BroadcastResponseStatsLogger.Data {
        public java.lang.String packageName;
        public long timestampMs;
        public int type;
        public int userId;

        private NotificationEvent() {
        }

        @Override // com.android.server.usage.BroadcastResponseStatsLogger.Data
        public void reset() {
            this.packageName = null;
        }

        public java.lang.String toString() {
            return com.android.server.usage.BroadcastResponseStatsLogger.getNotificationEventLog(this.type, this.packageName, this.userId, this.timestampMs);
        }
    }
}
