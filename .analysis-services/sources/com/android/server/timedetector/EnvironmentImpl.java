package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
final class EnvironmentImpl implements com.android.server.timedetector.TimeDetectorStrategyImpl.Environment {
    private static final java.lang.String LOG_TAG = "time_detector";
    private final com.android.server.AlarmManagerInternal mAlarmManagerInternal;
    private final android.os.Handler mHandler;
    private final android.os.PowerManager.WakeLock mWakeLock;

    EnvironmentImpl(android.content.Context context, android.os.Handler handler) {
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler);
        android.os.PowerManager powerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        this.mWakeLock = (android.os.PowerManager.WakeLock) java.util.Objects.requireNonNull(powerManager.newWakeLock(1, LOG_TAG));
        this.mAlarmManagerInternal = (com.android.server.AlarmManagerInternal) java.util.Objects.requireNonNull((com.android.server.AlarmManagerInternal) com.android.server.LocalServices.getService(com.android.server.AlarmManagerInternal.class));
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void acquireWakeLock() {
        if (this.mWakeLock.isHeld()) {
            android.util.Slog.wtf(LOG_TAG, "WakeLock " + this.mWakeLock + " already held");
        }
        this.mWakeLock.acquire();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public long elapsedRealtimeMillis() {
        return android.os.SystemClock.elapsedRealtime();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public long systemClockMillis() {
        return java.lang.System.currentTimeMillis();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public int systemClockConfidence() {
        return com.android.server.SystemClockTime.getTimeConfidence();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void setSystemClock(long newTimeMillis, int confidence, java.lang.String logMsg) {
        checkWakeLockHeld();
        this.mAlarmManagerInternal.setTime(newTimeMillis, confidence, logMsg);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void setSystemClockConfidence(int confidence, java.lang.String logMsg) {
        checkWakeLockHeld();
        com.android.server.SystemClockTime.setConfidence(confidence, logMsg);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void releaseWakeLock() {
        checkWakeLockHeld();
        this.mWakeLock.release();
    }

    private void checkWakeLockHeld() {
        if (!this.mWakeLock.isHeld()) {
            android.util.Slog.wtf(LOG_TAG, "WakeLock " + this.mWakeLock + " not held");
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void addDebugLogEntry(java.lang.String logMsg) {
        com.android.server.SystemClockTime.addDebugLogEntry(logMsg);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void dumpDebugLog(android.util.IndentingPrintWriter pw) {
        long elapsedRealtimeMillis = elapsedRealtimeMillis();
        pw.printf("elapsedRealtimeMillis()=%s (%s)\n", new java.lang.Object[]{java.time.Duration.ofMillis(elapsedRealtimeMillis), java.lang.Long.valueOf(elapsedRealtimeMillis)});
        long systemClockMillis = systemClockMillis();
        pw.printf("systemClockMillis()=%s (%s)\n", new java.lang.Object[]{java.time.Instant.ofEpochMilli(systemClockMillis), java.lang.Long.valueOf(systemClockMillis)});
        pw.println("systemClockConfidence()=" + systemClockConfidence());
        pw.println("SystemClockTime debug log:");
        pw.increaseIndent();
        com.android.server.SystemClockTime.dump(pw);
        pw.decreaseIndent();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void runAsync(java.lang.Runnable runnable) {
        this.mHandler.post(runnable);
    }
}
