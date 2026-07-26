package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class UsageStatsHandlerThread extends com.android.server.ServiceThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 30000;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 10000;
    private static com.android.server.usage.UsageStatsHandlerThread sInstance;
    private static final java.lang.Object sLock = new java.lang.Object();

    private UsageStatsHandlerThread() {
        super("android.usagestats", 0, true);
    }

    private static void ensureThreadLocked() {
        if (sInstance != null) {
            return;
        }
        sInstance = new com.android.server.usage.UsageStatsHandlerThread();
        sInstance.start();
        android.os.Looper looper = sInstance.getLooper();
        looper.setTraceTag(524288L);
        looper.setSlowLogThresholdMs(10000L, 30000L);
    }

    public static com.android.server.usage.UsageStatsHandlerThread get() {
        com.android.server.usage.UsageStatsHandlerThread usageStatsHandlerThread;
        synchronized (sLock) {
            ensureThreadLocked();
            usageStatsHandlerThread = sInstance;
        }
        return usageStatsHandlerThread;
    }
}
