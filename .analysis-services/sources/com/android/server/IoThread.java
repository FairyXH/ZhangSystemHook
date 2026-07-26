package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class IoThread extends com.android.server.ServiceThread {
    private static android.os.Handler sHandler;
    private static android.os.HandlerExecutor sHandlerExecutor;
    private static com.android.server.IoThread sInstance;

    private IoThread() {
        super("android.io", 0, true);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new com.android.server.IoThread();
            sInstance.start();
            sInstance.getLooper().setTraceTag(524288L);
            sHandler = makeSharedHandler(sInstance.getLooper());
            sHandlerExecutor = new android.os.HandlerExecutor(sHandler);
        }
    }

    public static com.android.server.IoThread get() {
        com.android.server.IoThread ioThread;
        synchronized (com.android.server.IoThread.class) {
            ensureThreadLocked();
            ioThread = sInstance;
        }
        return ioThread;
    }

    public static android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (com.android.server.IoThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static java.util.concurrent.Executor getExecutor() {
        android.os.HandlerExecutor handlerExecutor;
        synchronized (com.android.server.IoThread.class) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
