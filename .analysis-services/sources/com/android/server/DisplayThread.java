package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class DisplayThread extends com.android.server.ServiceThread {
    private static android.os.Handler sHandler;
    private static com.android.server.DisplayThread sInstance;

    private DisplayThread() {
        super("android.display", -3, false);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new com.android.server.DisplayThread();
            sInstance.start();
            sInstance.getLooper().setTraceTag(524288L);
            sHandler = makeSharedHandler(sInstance.getLooper());
        }
    }

    public static com.android.server.DisplayThread get() {
        com.android.server.DisplayThread displayThread;
        synchronized (com.android.server.DisplayThread.class) {
            ensureThreadLocked();
            displayThread = sInstance;
        }
        return displayThread;
    }

    public static android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (com.android.server.DisplayThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static void dispose() {
        synchronized (com.android.server.DisplayThread.class) {
            if (sInstance == null) {
                return;
            }
            getHandler().runWithScissors(new java.lang.Runnable() { // from class: com.android.server.DisplayThread$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.DisplayThread.sInstance.quit();
                }
            }, 0L);
            sInstance = null;
        }
    }
}
