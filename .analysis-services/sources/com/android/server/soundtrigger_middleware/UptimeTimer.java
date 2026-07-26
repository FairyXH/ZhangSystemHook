package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
class UptimeTimer {
    private final android.os.Handler mHandler;
    private final android.os.HandlerThread mHandlerThread;

    interface Task {
        void cancel();
    }

    UptimeTimer(java.lang.String threadName) {
        this.mHandlerThread = new android.os.HandlerThread(threadName);
        this.mHandlerThread.start();
        this.mHandler = new android.os.Handler(this.mHandlerThread.getLooper());
    }

    com.android.server.soundtrigger_middleware.UptimeTimer.Task createTask(java.lang.Runnable runnable, long uptimeMs) {
        java.lang.Object token = new java.lang.Object();
        com.android.server.soundtrigger_middleware.UptimeTimer.TaskImpl task = new com.android.server.soundtrigger_middleware.UptimeTimer.TaskImpl(this.mHandler, token);
        this.mHandler.postDelayed(runnable, token, uptimeMs);
        return task;
    }

    void quit() {
        this.mHandlerThread.quitSafely();
    }

    private static class TaskImpl implements com.android.server.soundtrigger_middleware.UptimeTimer.Task {
        private final android.os.Handler mHandler;
        private final java.lang.Object mToken;

        public TaskImpl(android.os.Handler handler, java.lang.Object token) {
            this.mHandler = handler;
            this.mToken = token;
        }

        @Override // com.android.server.soundtrigger_middleware.UptimeTimer.Task
        public void cancel() {
            this.mHandler.removeCallbacksAndMessages(this.mToken);
        }
    }
}
