package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class SystemClockTime {
    private static final java.lang.String TAG = "SystemClockTime";
    public static final int TIME_CONFIDENCE_HIGH = 100;
    public static final int TIME_CONFIDENCE_LOW = 0;
    private static final android.util.LocalLog sTimeDebugLog = new android.util.LocalLog(30, false);
    private static int sTimeConfidence = 0;
    private static final long sNativeData = init();

    @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE_USE})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface TimeConfidence {
    }

    private static native long init();

    private static native int setTime(long j, long j2);

    private SystemClockTime() {
    }

    public static void initializeIfRequired() {
        long systemBuildTime = java.lang.Long.max(android.os.SystemProperties.getLong("ro.build.date.utc", -1L) * 1000, java.lang.Long.max(android.os.Environment.getRootDirectory().lastModified(), android.os.Build.TIME));
        long currentTimeMillis = getCurrentTimeMillis();
        if (currentTimeMillis < systemBuildTime) {
            java.lang.String logMsg = "Current time only " + currentTimeMillis + ", advancing to build time " + systemBuildTime;
            android.util.Slog.i(TAG, logMsg);
            setTimeAndConfidence(systemBuildTime, 0, logMsg);
        }
    }

    public static void setTimeAndConfidence(long unixEpochMillis, int confidence, java.lang.String logMsg) {
        synchronized (com.android.server.SystemClockTime.class) {
            setTime(sNativeData, unixEpochMillis);
            sTimeConfidence = confidence;
            sTimeDebugLog.log(logMsg);
        }
    }

    public static void setConfidence(int confidence, java.lang.String logMsg) {
        synchronized (com.android.server.SystemClockTime.class) {
            sTimeConfidence = confidence;
            sTimeDebugLog.log(logMsg);
        }
    }

    private static long getCurrentTimeMillis() {
        return java.lang.System.currentTimeMillis();
    }

    public static int getTimeConfidence() {
        int i;
        synchronized (com.android.server.SystemClockTime.class) {
            i = sTimeConfidence;
        }
        return i;
    }

    public static void addDebugLogEntry(java.lang.String logMsg) {
        sTimeDebugLog.log(logMsg);
    }

    public static void dump(java.io.PrintWriter writer) {
        sTimeDebugLog.dump(writer);
    }
}
