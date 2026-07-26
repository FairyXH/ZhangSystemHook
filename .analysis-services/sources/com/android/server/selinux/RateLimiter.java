package com.android.server.selinux;

/* JADX INFO: loaded from: classes3.dex */
public final class RateLimiter {
    private final com.android.internal.os.Clock mClock;
    private java.time.Instant mNextPermit;
    private final java.time.Duration mWindow;

    RateLimiter(com.android.internal.os.Clock clock, java.time.Duration window) {
        this.mNextPermit = java.time.Instant.EPOCH;
        this.mClock = clock;
        this.mWindow = window;
    }

    public RateLimiter(java.time.Duration window) {
        this(com.android.internal.os.Clock.SYSTEM_CLOCK, window);
    }

    public void acquire() {
        java.time.Instant now = java.time.Instant.ofEpochMilli(this.mClock.currentTimeMillis());
        if (this.mNextPermit.isAfter(now)) {
            android.os.SystemClock.sleep(java.time.temporal.ChronoUnit.MILLIS.between(now, this.mNextPermit));
            this.mNextPermit = this.mNextPermit.plus((java.time.temporal.TemporalAmount) this.mWindow);
        } else {
            this.mNextPermit = now.plus((java.time.temporal.TemporalAmount) this.mWindow);
        }
    }

    public boolean tryAcquire() {
        java.time.Instant now = java.time.Instant.ofEpochMilli(this.mClock.currentTimeMillis());
        if (this.mNextPermit.isAfter(now)) {
            return false;
        }
        this.mNextPermit = now.plus((java.time.temporal.TemporalAmount) this.mWindow);
        return true;
    }
}
