package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class OplusIoThread extends com.android.server.ServiceThread {
    private static android.os.Handler sHandler;
    private static android.os.HandlerExecutor sHandlerExecutor;
    private static com.android.server.OplusIoThread sInstance;

    private OplusIoThread() {
        super("oplus.io", 0, true);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new com.android.server.OplusIoThread();
            sInstance.start();
            sInstance.getLooper().setTraceTag(524288L);
            sHandler = new android.os.Handler(sInstance.getLooper());
            sHandlerExecutor = new android.os.HandlerExecutor(sHandler);
        }
    }

    public static com.android.server.OplusIoThread get() {
        com.android.server.OplusIoThread oplusIoThread;
        synchronized (com.android.server.OplusIoThread.class) {
            ensureThreadLocked();
            oplusIoThread = sInstance;
        }
        return oplusIoThread;
    }

    public static android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (com.android.server.OplusIoThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static java.util.concurrent.Executor getExecutor() {
        android.os.HandlerExecutor handlerExecutor;
        synchronized (com.android.server.OplusIoThread.class) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
