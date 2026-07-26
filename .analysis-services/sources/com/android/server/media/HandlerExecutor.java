package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public class HandlerExecutor implements java.util.concurrent.Executor {
    private final android.os.Handler mHandler;

    public HandlerExecutor(android.os.Handler handler) {
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler);
    }

    @Override // java.util.concurrent.Executor
    public void execute(java.lang.Runnable command) {
        if (!this.mHandler.post(command)) {
            throw new java.util.concurrent.RejectedExecutionException(this.mHandler + " is shutting down");
        }
    }
}
