package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class OplusAppStartPerfThread extends com.android.server.ServiceThread {
    private static final int START_TOP_PROCESS_EVENT_ID = 1015;
    private static android.os.Handler sHandler;
    private static android.os.HandlerExecutor sHandlerExecutor;
    private static com.android.server.OplusAppStartPerfThread sInstance;
    private static int sPid = android.os.Process.myPid();
    private static int sTid;

    private OplusAppStartPerfThread() {
        super("oplus.appStartPerf", -4, true);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new com.android.server.OplusAppStartPerfThread();
            sInstance.start();
            sHandler = new android.os.Handler(sInstance.getLooper());
            sHandlerExecutor = new android.os.HandlerExecutor(sHandler);
            sTid = sInstance.getThreadId();
            com.oplus.osense.IOplusUserAwareManagerExt userAwareManagerExt = (com.oplus.osense.IOplusUserAwareManagerExt) system.ext.loader.core.ExtLoader.type(com.oplus.osense.IOplusUserAwareManagerExt.class).create();
            userAwareManagerExt.reportKeyThread(sInstance.getName(), sTid, sPid, 1015, (android.os.Bundle) null);
        }
    }

    public static com.android.server.OplusAppStartPerfThread get() {
        com.android.server.OplusAppStartPerfThread oplusAppStartPerfThread;
        synchronized (com.android.server.OplusAppStartPerfThread.class) {
            ensureThreadLocked();
            oplusAppStartPerfThread = sInstance;
        }
        return oplusAppStartPerfThread;
    }

    public static android.os.Handler getHandler() {
        android.os.Handler handler;
        synchronized (com.android.server.OplusAppStartPerfThread.class) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static java.util.concurrent.Executor getExecutor() {
        android.os.HandlerExecutor handlerExecutor;
        synchronized (com.android.server.OplusAppStartPerfThread.class) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }

    public static int getTid() {
        int i;
        synchronized (com.android.server.OplusAppStartPerfThread.class) {
            ensureThreadLocked();
            i = sTid;
        }
        return i;
    }

    public static void boost() {
        android.common.OplusFeatureCache.get(com.oplus.uifirst.IOplusUIFirstManager.DEFAULT).setUxThreadValueByFile(sPid, getTid(), 130);
    }

    public static void reset() {
        android.common.OplusFeatureCache.get(com.oplus.uifirst.IOplusUIFirstManager.DEFAULT).setUxThreadValueByFile(sPid, getTid(), 0);
    }
}
