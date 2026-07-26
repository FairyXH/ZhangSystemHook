package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public final class SurfaceAnimationThread extends com.android.server.ServiceThread {
    private static android.os.Handler sHandler;
    private static com.android.server.wm.SurfaceAnimationThread sInstance;

    private SurfaceAnimationThread() {
        super("android.anim.lf", -4, false);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new com.android.server.wm.SurfaceAnimationThread();
            sInstance.start();
            sInstance.getLooper().setTraceTag(32L);
            sHandler = makeSharedHandler(sInstance.getLooper());
        }
    }

    public static com.android.server.wm.SurfaceAnimationThread get() {
        com.android.server.wm.SurfaceAnimationThread surfaceAnimationThread;
        synchronized (com.android.server.wm.SurfaceAnimationThread.class) {
            ensureThreadLocked();
            surfaceAnimationThread = sInstance;
        }
        return surfaceAnimationThread;
    }

    public static android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (com.android.server.wm.SurfaceAnimationThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static void dispose() {
        synchronized (com.android.server.wm.SurfaceAnimationThread.class) {
            if (sInstance == null) {
                return;
            }
            getHandler().runWithScissors(new java.lang.Runnable() { // from class: com.android.server.wm.SurfaceAnimationThread$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.wm.SurfaceAnimationThread.sInstance.quit();
                }
            }, 0L);
            sInstance = null;
        }
    }
}
