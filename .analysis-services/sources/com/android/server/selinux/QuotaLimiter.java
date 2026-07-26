package com.android.server.selinux;

/* JADX INFO: loaded from: classes3.dex */
public class QuotaLimiter {
    private final com.android.internal.os.Clock mClock;
    private long mCurrentWindow;
    private int mMaxPermits;
    private int mPermitsGranted;
    private final java.time.Duration mWindowSize;

    QuotaLimiter(com.android.internal.os.Clock clock, java.time.Duration windowSize, int maxPermits) {
        this.mClock = clock;
        this.mWindowSize = windowSize;
        this.mMaxPermits = maxPermits;
    }

    public QuotaLimiter(java.time.Duration windowSize, int maxPermits) {
        this(com.android.internal.os.Clock.SYSTEM_CLOCK, windowSize, maxPermits);
    }

    public QuotaLimiter(int maxPermitsPerDay) {
        this(com.android.internal.os.Clock.SYSTEM_CLOCK, java.time.Duration.ofDays(1L), maxPermitsPerDay);
    }

    boolean acquire() {
        long nowWindow = java.time.Duration.between(java.time.Instant.EPOCH, java.time.Instant.ofEpochMilli(this.mClock.currentTimeMillis())).dividedBy(this.mWindowSize);
        if (nowWindow > this.mCurrentWindow) {
            this.mCurrentWindow = nowWindow;
            this.mPermitsGranted = 0;
        }
        if (this.mPermitsGranted >= this.mMaxPermits) {
            return false;
        }
        this.mPermitsGranted++;
        return true;
    }

    public void setMaxPermits(int maxPermits) {
        this.mMaxPermits = maxPermits;
    }
}
