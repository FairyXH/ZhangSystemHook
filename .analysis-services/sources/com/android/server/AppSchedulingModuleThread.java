package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class AppSchedulingModuleThread extends android.os.HandlerThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 30000;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 10000;
    private static android.os.Handler sHandler;
    private static java.util.concurrent.Executor sHandlerExecutor;
    private static com.android.server.AppSchedulingModuleThread sInstance;

    private AppSchedulingModuleThread() {
        super("appscheduling.default", 0);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new com.android.server.AppSchedulingModuleThread();
            sInstance.start();
            android.os.Looper looper = sInstance.getLooper();
            looper.setTraceTag(524288L);
            looper.setSlowLogThresholdMs(10000L, 30000L);
            sHandler = new android.os.Handler(sInstance.getLooper());
            sHandlerExecutor = new android.os.HandlerExecutor(sHandler);
        }
    }

    public static com.android.server.AppSchedulingModuleThread get() {
        com.android.server.AppSchedulingModuleThread appSchedulingModuleThread;
        synchronized (com.android.server.AppSchedulingModuleThread.class) {
            ensureThreadLocked();
            appSchedulingModuleThread = sInstance;
        }
        return appSchedulingModuleThread;
    }

    public static android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (com.android.server.AppSchedulingModuleThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static java.util.concurrent.Executor getExecutor() {
        java.util.concurrent.Executor executor;
        synchronized (com.android.server.AppSchedulingModuleThread.class) {
            ensureThreadLocked();
            executor = sHandlerExecutor;
        }
        return executor;
    }
}
