package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
final class EnvironmentImpl implements com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment {
    private static final java.lang.String TIMEZONE_PROPERTY = "persist.sys.timezone";
    private final android.os.Handler mHandler;

    EnvironmentImpl(android.os.Handler handler) {
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public java.lang.String getDeviceTimeZone() {
        return android.os.SystemProperties.get(TIMEZONE_PROPERTY);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public int getDeviceTimeZoneConfidence() {
        return com.android.server.SystemTimeZone.getTimeZoneConfidence();
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public void setDeviceTimeZoneAndConfidence(java.lang.String zoneId, int confidence, java.lang.String logInfo) {
        com.android.server.AlarmManagerInternal alarmManagerInternal = (com.android.server.AlarmManagerInternal) com.android.server.LocalServices.getService(com.android.server.AlarmManagerInternal.class);
        alarmManagerInternal.setTimeZone(zoneId, confidence, logInfo);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public long elapsedRealtimeMillis() {
        return android.os.SystemClock.elapsedRealtime();
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public void addDebugLogEntry(java.lang.String logMsg) {
        com.android.server.SystemTimeZone.addDebugLogEntry(logMsg);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public void dumpDebugLog(java.io.PrintWriter printWriter) {
        com.android.server.SystemTimeZone.dump(printWriter);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public void runAsync(java.lang.Runnable runnable) {
        this.mHandler.post(runnable);
    }
}
