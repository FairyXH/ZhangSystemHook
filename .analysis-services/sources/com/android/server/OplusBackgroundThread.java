package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class OplusBackgroundThread extends android.os.HandlerThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 30000;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 10000;
    private static android.os.Handler sHandler;
    private static android.os.HandlerExecutor sHandlerExecutor;
    private static com.android.server.OplusBackgroundThread sInstance;

    private OplusBackgroundThread() {
        super("oplus.bg", 10);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new com.android.server.OplusBackgroundThread();
            sInstance.start();
            android.os.Looper looper = sInstance.getLooper();
            looper.setTraceTag(524288L);
            looper.setSlowLogThresholdMs(10000L, 30000L);
            sHandler = new android.os.Handler(sInstance.getLooper());
            sHandlerExecutor = new android.os.HandlerExecutor(sHandler);
        }
    }

    public static com.android.server.OplusBackgroundThread get() {
        com.android.server.OplusBackgroundThread oplusBackgroundThread;
        synchronized (com.android.server.OplusBackgroundThread.class) {
            ensureThreadLocked();
            oplusBackgroundThread = sInstance;
        }
        return oplusBackgroundThread;
    }

    public static android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (com.android.server.OplusBackgroundThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static java.util.concurrent.Executor getExecutor() {
        android.os.HandlerExecutor handlerExecutor;
        synchronized (com.android.server.OplusBackgroundThread.class) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
