package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class FgThread extends com.android.server.ServiceThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 200;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 100;
    private static android.os.Handler sHandler;
    private static android.os.HandlerExecutor sHandlerExecutor;
    private static com.android.server.FgThread sInstance;

    private FgThread() {
        super("android.fg", 0, true);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new com.android.server.FgThread();
            sInstance.start();
            android.os.Looper looper = sInstance.getLooper();
            looper.setTraceTag(524288L);
            looper.setSlowLogThresholdMs(SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);
            sHandler = makeSharedHandler(sInstance.getLooper());
            sHandlerExecutor = new android.os.HandlerExecutor(sHandler);
        }
    }

    public static com.android.server.FgThread get() {
        com.android.server.FgThread fgThread;
        synchronized (com.android.server.FgThread.class) {
            ensureThreadLocked();
            fgThread = sInstance;
        }
        return fgThread;
    }

    public static android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (com.android.server.FgThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static java.util.concurrent.Executor getExecutor() {
        android.os.HandlerExecutor handlerExecutor;
        synchronized (com.android.server.FgThread.class) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
