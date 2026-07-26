package com.android.server.contentcapture;

/* JADX INFO: loaded from: classes.dex */
public final class ContentCaptureMetricsLogger {
    private ContentCaptureMetricsLogger() {
    }

    public static void writeServiceEvent(int eventType, java.lang.String serviceName) {
        com.android.internal.util.FrameworkStatsLog.write(207, eventType, serviceName, (java.lang.String) null, 0, 0);
    }

    public static void writeServiceEvent(int eventType, android.content.ComponentName service) {
        writeServiceEvent(eventType, android.content.ComponentName.flattenToShortString(service));
    }

    public static void writeSetWhitelistEvent(android.content.ComponentName service, java.util.List<java.lang.String> packages, java.util.List<android.content.ComponentName> activities) {
        java.lang.String serviceName = android.content.ComponentName.flattenToShortString(service);
        int packageCount = packages != null ? packages.size() : 0;
        int activityCount = activities != null ? activities.size() : 0;
        com.android.internal.util.FrameworkStatsLog.write(207, 3, serviceName, (java.lang.String) null, packageCount, activityCount);
    }

    public static void writeSessionEvent(int sessionId, int event, int flags, android.content.ComponentName service, boolean isChildSession) {
        com.android.internal.util.FrameworkStatsLog.write(208, sessionId, event, flags, android.content.ComponentName.flattenToShortString(service), (java.lang.String) null, isChildSession);
    }

    public static void writeSessionFlush(int sessionId, android.content.ComponentName service, android.service.contentcapture.FlushMetrics fm, android.content.ContentCaptureOptions options, int flushReason) {
        com.android.internal.util.FrameworkStatsLog.write(209, sessionId, android.content.ComponentName.flattenToShortString(service), (java.lang.String) null, fm.sessionStarted, fm.sessionFinished, fm.viewAppearedCount, fm.viewDisappearedCount, fm.viewTextChangedCount, options.maxBufferSize, options.idleFlushingFrequencyMs, options.textChangeFlushingFrequencyMs, flushReason);
    }
}
