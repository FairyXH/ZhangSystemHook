package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class PermissionThread extends com.android.server.ServiceThread {
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 200;
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 100;
    private static android.os.Handler sHandler;
    private static android.os.HandlerExecutor sHandlerExecutor;
    private static com.android.server.PermissionThread sInstance;
    private static final java.lang.Object sLock = new java.lang.Object();

    private PermissionThread() {
        super("android.perm", 0, true);
    }

    private static void ensureThreadLocked() {
        if (sInstance != null) {
            return;
        }
        sInstance = new com.android.server.PermissionThread();
        sInstance.start();
        android.os.Looper looper = sInstance.getLooper();
        looper.setTraceTag(524288L);
        looper.setSlowLogThresholdMs(SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);
        sHandler = new android.os.Handler(sInstance.getLooper());
        sHandlerExecutor = new android.os.HandlerExecutor(sHandler);
    }

    public static com.android.server.PermissionThread get() {
        com.android.server.PermissionThread permissionThread;
        synchronized (sLock) {
            ensureThreadLocked();
            permissionThread = sInstance;
        }
        return permissionThread;
    }

    public static android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (sLock) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static java.util.concurrent.Executor getExecutor() {
        android.os.HandlerExecutor handlerExecutor;
        synchronized (sLock) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
