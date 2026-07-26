package com.android.server.backup.restore;

/* JADX INFO: loaded from: classes.dex */
public abstract class RestoreEngine {
    public static final int SUCCESS = 0;
    private static final java.lang.String TAG = "RestoreEngine";
    public static final int TARGET_FAILURE = -2;
    public static final int TRANSPORT_FAILURE = -3;
    private final java.util.concurrent.atomic.AtomicBoolean mRunning = new java.util.concurrent.atomic.AtomicBoolean(false);
    private final java.util.concurrent.atomic.AtomicInteger mResult = new java.util.concurrent.atomic.AtomicInteger(0);

    public boolean isRunning() {
        return this.mRunning.get();
    }

    public void setRunning(boolean stillRunning) {
        synchronized (this.mRunning) {
            this.mRunning.set(stillRunning);
            this.mRunning.notifyAll();
        }
    }

    public int waitForResult() {
        synchronized (this.mRunning) {
            while (isRunning()) {
                try {
                    this.mRunning.wait();
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
        return getResult();
    }

    public int getResult() {
        return this.mResult.get();
    }

    public void setResult(int result) {
        this.mResult.set(result);
    }
}
