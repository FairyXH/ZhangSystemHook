package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class AnimationThread extends com.android.server.ServiceThread {
    private static android.os.Handler sHandler;
    private static com.android.server.AnimationThread sInstance;

    private AnimationThread() {
        super("android.anim", -4, false);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new com.android.server.AnimationThread();
            sInstance.start();
            sInstance.getLooper().setTraceTag(32L);
            sHandler = makeSharedHandler(sInstance.getLooper());
        }
    }

    public static com.android.server.AnimationThread get() {
        com.android.server.AnimationThread animationThread;
        synchronized (com.android.server.AnimationThread.class) {
            ensureThreadLocked();
            animationThread = sInstance;
        }
        return animationThread;
    }

    public static android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (com.android.server.AnimationThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static void dispose() {
        synchronized (com.android.server.AnimationThread.class) {
            if (sInstance == null) {
                return;
            }
            getHandler().runWithScissors(new java.lang.Runnable() { // from class: com.android.server.AnimationThread$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.AnimationThread.sInstance.quit();
                }
            }, 0L);
            sInstance = null;
        }
    }
}
