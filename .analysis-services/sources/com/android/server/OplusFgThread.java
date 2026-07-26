package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class OplusFgThread extends com.android.server.ServiceThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 200;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 100;
    private static android.os.Handler sHandler;
    private static android.os.HandlerExecutor sHandlerExecutor;
    private static com.android.server.OplusFgThread sInstance;

    private OplusFgThread() {
        super("oplus.fg", 0, true);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new com.android.server.OplusFgThread();
            sInstance.start();
            android.os.Looper looper = sInstance.getLooper();
            looper.setTraceTag(524288L);
            looper.setSlowLogThresholdMs(SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);
            sHandler = makeSharedHandler(sInstance.getLooper());
            sHandlerExecutor = new android.os.HandlerExecutor(sHandler);
        }
    }

    public static com.android.server.OplusFgThread get() {
        com.android.server.OplusFgThread oplusFgThread;
        synchronized (com.android.server.OplusFgThread.class) {
            ensureThreadLocked();
            oplusFgThread = sInstance;
        }
        return oplusFgThread;
    }

    public static android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (com.android.server.OplusFgThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static java.util.concurrent.Executor getExecutor() {
        android.os.HandlerExecutor handlerExecutor;
        synchronized (com.android.server.OplusFgThread.class) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
