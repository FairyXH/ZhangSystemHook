package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public abstract class RankingReconsideration implements java.lang.Runnable {
    private static final int CANCELLED = 3;
    private static final int DONE = 2;
    private static final long IMMEDIATE = 0;
    private static final int RUNNING = 1;
    private static final int START = 0;
    private long mDelay;
    protected java.lang.String mKey;
    private int mState;

    public abstract void applyChangesLocked(com.android.server.notification.NotificationRecord notificationRecord);

    public abstract void work();

    public RankingReconsideration(java.lang.String key) {
        this(key, 0L);
    }

    public RankingReconsideration(java.lang.String key, long delay) {
        this.mDelay = delay;
        this.mKey = key;
        this.mState = 0;
    }

    public java.lang.String getKey() {
        return this.mKey;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.mState == 0) {
            this.mState = 1;
            work();
            this.mState = 2;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    public long getDelay(java.util.concurrent.TimeUnit unit) {
        return unit.convert(this.mDelay, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        if (this.mState == 0) {
            this.mState = 3;
            return true;
        }
        return false;
    }

    public boolean isCancelled() {
        return this.mState == 3;
    }

    public boolean isDone() {
        return this.mState == 2;
    }
}
