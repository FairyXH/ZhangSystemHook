package com.android.server.display.util;

/* JADX INFO: loaded from: classes2.dex */
public class PendingResult<R> {
    private static final int COUNT_DOWN_THREAD_AMOUNT = 1;
    private static final java.lang.String TAG = "PendingResult";
    private java.util.concurrent.CountDownLatch mLatch = new java.util.concurrent.CountDownLatch(1);
    private volatile R mResult;

    public PendingResult(R defResult) {
        this.mResult = defResult;
    }

    public R await(long timeout, java.util.concurrent.TimeUnit unit) {
        try {
            this.mLatch.await(timeout, unit);
        } catch (java.lang.InterruptedException ie) {
            android.util.Slog.i(TAG, ie.toString());
        }
        return this.mResult;
    }

    public R getResult() {
        return this.mResult;
    }

    public boolean isCounting() {
        return this.mLatch.getCount() == 1;
    }

    public void setResult(R result) {
        this.mResult = result;
        this.mLatch.countDown();
    }

    public void cancel() {
        this.mLatch.countDown();
    }
}
