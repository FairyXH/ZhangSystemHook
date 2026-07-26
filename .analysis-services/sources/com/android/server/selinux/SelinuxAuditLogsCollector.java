package com.android.server.selinux;

/* JADX INFO: loaded from: classes3.dex */
class SelinuxAuditLogsCollector {
    private final com.android.server.selinux.QuotaLimiter mQuotaLimiter;
    private final com.android.server.selinux.RateLimiter mRateLimiter;
    private static final java.lang.String TAG = "SelinuxAuditLogs";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final java.lang.String SELINUX_PATTERN = "^.*\\bavc:\\s+(?<denial>.*)$";
    static final java.util.regex.Matcher SELINUX_MATCHER = java.util.regex.Pattern.compile(SELINUX_PATTERN).matcher("");
    java.time.Instant mLastWrite = java.time.Instant.MIN;
    java.util.concurrent.atomic.AtomicBoolean mStopRequested = new java.util.concurrent.atomic.AtomicBoolean(false);

    SelinuxAuditLogsCollector(com.android.server.selinux.RateLimiter rateLimiter, com.android.server.selinux.QuotaLimiter quotaLimiter) {
        this.mRateLimiter = rateLimiter;
        this.mQuotaLimiter = quotaLimiter;
    }

    public void setStopRequested(boolean stopRequested) {
        this.mStopRequested.set(stopRequested);
    }

    boolean collect(int tagCode) {
        java.util.Queue<android.util.EventLog.Event> logLines = new java.util.ArrayDeque<>();
        java.time.Instant latestTimestamp = collectLogLines(tagCode, logLines);
        boolean quotaExceeded = writeAuditLogs(logLines);
        if (quotaExceeded) {
            android.util.Slog.w(TAG, "Too many SELinux logs in the queue, I am giving up.");
            this.mLastWrite = latestTimestamp;
            logLines.clear();
        }
        return logLines.isEmpty();
    }

    private java.time.Instant collectLogLines(int tagCode, java.util.Queue<android.util.EventLog.Event> logLines) {
        java.util.List<android.util.EventLog.Event> events = new java.util.ArrayList<>();
        try {
            android.util.EventLog.readEvents(new int[]{tagCode}, events);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Error reading event logs", e);
        }
        java.time.Instant latestTimestamp = this.mLastWrite;
        for (android.util.EventLog.Event event : events) {
            java.time.Instant eventTime = java.time.Instant.ofEpochSecond(0L, event.getTimeNanos());
            if (eventTime.isAfter(latestTimestamp)) {
                latestTimestamp = eventTime;
            }
            if (eventTime.compareTo(this.mLastWrite) > 0) {
                java.lang.Object eventData = event.getData();
                if (eventData instanceof java.lang.String) {
                    logLines.add(event);
                }
            }
        }
        return latestTimestamp;
    }

    private boolean writeAuditLogs(java.util.Queue<android.util.EventLog.Event> logLines) {
        com.android.server.selinux.SelinuxAuditLogBuilder auditLogBuilder = new com.android.server.selinux.SelinuxAuditLogBuilder();
        int auditsWritten = 0;
        while (!this.mStopRequested.get() && !logLines.isEmpty()) {
            android.util.EventLog.Event event = logLines.poll();
            java.lang.String logLine = (java.lang.String) event.getData();
            java.time.Instant logTime = java.time.Instant.ofEpochSecond(0L, event.getTimeNanos());
            if (SELINUX_MATCHER.reset(logLine).matches()) {
                auditLogBuilder.reset(SELINUX_MATCHER.group("denial"));
                com.android.server.selinux.SelinuxAuditLogBuilder.SelinuxAuditLog auditLog = auditLogBuilder.build();
                if (auditLog == null) {
                    continue;
                } else {
                    if (!this.mQuotaLimiter.acquire()) {
                        if (DEBUG) {
                            com.android.server.utils.Slogf.d(TAG, "Running out of quota after %d logs.", java.lang.Integer.valueOf(auditsWritten));
                            return true;
                        }
                        return true;
                    }
                    this.mRateLimiter.acquire();
                    com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SELINUX_AUDIT_LOG, auditLog.mGranted, auditLog.mPermissions, auditLog.mSType, auditLog.mSCategories, auditLog.mTType, auditLog.mTCategories, auditLog.mTClass, auditLog.mPath, auditLog.mPermissive);
                    auditsWritten++;
                    if (logTime.isAfter(this.mLastWrite)) {
                        this.mLastWrite = logTime;
                    }
                }
            }
        }
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "Written %d logs", java.lang.Integer.valueOf(auditsWritten));
            return false;
        }
        return false;
    }
}
