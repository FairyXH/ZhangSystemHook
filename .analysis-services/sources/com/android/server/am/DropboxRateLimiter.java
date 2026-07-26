package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class DropboxRateLimiter {
    private static final java.lang.String FLAG_NAMESPACE = "dropbox";
    private static final int RATE_LIMIT_ALLOWED_ENTRIES_DEFAULT = 6;
    private static final long RATE_LIMIT_BUFFER_DURATION_DEFAULT = 600000;
    private static final long RATE_LIMIT_BUFFER_EXPIRY_FACTOR_DEFAULT = 3;
    private static final int STRICT_RATE_LIMIT_ALLOWED_ENTRIES_DEFAULT = 1;
    private static final long STRICT_RATE_LIMIT_BUFFER_DURATION_DEFAULT = 1200000;
    private static final java.lang.String TAG = "DropboxRateLimiter";
    private final com.android.server.am.DropboxRateLimiter.Clock mClock;
    private final android.util.ArrayMap<java.lang.String, com.android.server.am.DropboxRateLimiter.ErrorRecord> mErrorClusterRecords;
    private long mLastMapCleanUp;
    private int mRateLimitAllowedEntries;
    private long mRateLimitBufferDuration;
    private long mRateLimitBufferExpiryFactor;
    private long mStrictRateLimitBufferDuration;
    private int mStrictRatelimitAllowedEntries;

    public interface Clock {
        long uptimeMillis();
    }

    public DropboxRateLimiter() {
        this(new com.android.server.am.DropboxRateLimiter.DefaultClock());
    }

    public DropboxRateLimiter(com.android.server.am.DropboxRateLimiter.Clock clock) {
        this.mErrorClusterRecords = new android.util.ArrayMap<>();
        this.mLastMapCleanUp = 0L;
        this.mClock = clock;
        this.mRateLimitBufferDuration = 600000L;
        this.mRateLimitBufferExpiryFactor = 3L;
        this.mRateLimitAllowedEntries = 6;
        this.mStrictRatelimitAllowedEntries = 1;
        this.mStrictRateLimitBufferDuration = STRICT_RATE_LIMIT_BUFFER_DURATION_DEFAULT;
    }

    public void init() {
        this.mRateLimitBufferDuration = android.provider.DeviceConfig.getLong(FLAG_NAMESPACE, "DropboxRateLimiter__rate_limit_buffer_duration", 600000L);
        this.mRateLimitBufferExpiryFactor = android.provider.DeviceConfig.getLong(FLAG_NAMESPACE, "DropboxRateLimiter__rate_limit_buffer_expiry_factor", 3L);
        this.mRateLimitAllowedEntries = android.provider.DeviceConfig.getInt(FLAG_NAMESPACE, "DropboxRateLimiter__rate_limit_allowed_entries", 6);
        this.mStrictRatelimitAllowedEntries = android.provider.DeviceConfig.getInt(FLAG_NAMESPACE, "DropboxRateLimiter__strict_rate_limit_allowed_entries", 1);
        this.mStrictRateLimitBufferDuration = android.provider.DeviceConfig.getLong(FLAG_NAMESPACE, "DropboxRateLimiter__strict_rate_limit_buffer_duration", STRICT_RATE_LIMIT_BUFFER_DURATION_DEFAULT);
    }

    public com.android.server.am.DropboxRateLimiter.RateLimitResult shouldRateLimit(java.lang.String eventType, java.lang.String processName) {
        if (eventType != null && "wtf".equals(eventType)) {
            long now = this.mClock.uptimeMillis();
            synchronized (this.mErrorClusterRecords) {
                maybeRemoveExpiredRecords(now);
                com.android.server.am.DropboxRateLimiter.ErrorRecord errRecord = this.mErrorClusterRecords.get(errorKey(eventType, processName));
                if (errRecord == null) {
                    this.mErrorClusterRecords.put(errorKey(eventType, processName), new com.android.server.am.DropboxRateLimiter.ErrorRecord(now, 1));
                    return new com.android.server.am.DropboxRateLimiter.RateLimitResult(false, 0);
                }
                long timeSinceFirstError = now - errRecord.getStartTime();
                if (timeSinceFirstError > errRecord.getBufferDuration()) {
                    int errCount = recentlyDroppedCount(errRecord);
                    errRecord.setStartTime(now);
                    errRecord.setCount(1);
                    if (errCount > 0 && timeSinceFirstError < errRecord.getBufferDuration() * 2) {
                        errRecord.incrementSuccessiveRateLimitCycles();
                    } else {
                        errRecord.setSuccessiveRateLimitCycles(0);
                    }
                    return new com.android.server.am.DropboxRateLimiter.RateLimitResult(false, errCount);
                }
                errRecord.incrementCount();
                if (errRecord.getCount() > errRecord.getAllowedEntries()) {
                    return new com.android.server.am.DropboxRateLimiter.RateLimitResult(true, recentlyDroppedCount(errRecord));
                }
            }
        }
        return new com.android.server.am.DropboxRateLimiter.RateLimitResult(false, 0);
    }

    private int recentlyDroppedCount(com.android.server.am.DropboxRateLimiter.ErrorRecord errRecord) {
        if (errRecord == null || errRecord.getCount() < errRecord.getAllowedEntries()) {
            return 0;
        }
        return errRecord.getCount() - errRecord.getAllowedEntries();
    }

    private void maybeRemoveExpiredRecords(long currentTime) {
        if (currentTime - this.mLastMapCleanUp <= this.mRateLimitBufferExpiryFactor * this.mRateLimitBufferDuration) {
            return;
        }
        for (int i = this.mErrorClusterRecords.size() - 1; i >= 0; i--) {
            if (this.mErrorClusterRecords.valueAt(i).hasExpired(currentTime)) {
                com.android.modules.expresslog.Counter.logIncrement("stability_errors.value_dropbox_buffer_expired_count", this.mErrorClusterRecords.valueAt(i).getCount());
                this.mErrorClusterRecords.removeAt(i);
            }
        }
        this.mLastMapCleanUp = currentTime;
    }

    public void reset() {
        synchronized (this.mErrorClusterRecords) {
            this.mErrorClusterRecords.clear();
        }
        this.mLastMapCleanUp = 0L;
        android.util.Slog.i(TAG, "Rate limiter reset.");
    }

    java.lang.String errorKey(java.lang.String eventType, java.lang.String processName) {
        return eventType + processName;
    }

    public class RateLimitResult {
        final int mDroppedCountSinceRateLimitActivated;
        final boolean mShouldRateLimit;

        public RateLimitResult(boolean shouldRateLimit, int droppedCountSinceRateLimitActivated) {
            this.mShouldRateLimit = shouldRateLimit;
            this.mDroppedCountSinceRateLimitActivated = droppedCountSinceRateLimitActivated;
        }

        public boolean shouldRateLimit() {
            return this.mShouldRateLimit;
        }

        public int droppedCountSinceRateLimitActivated() {
            return this.mDroppedCountSinceRateLimitActivated;
        }

        public java.lang.String createHeader() {
            return "Dropped-Count: " + this.mDroppedCountSinceRateLimitActivated + "\n";
        }
    }

    private class ErrorRecord {
        int mCount;
        long mStartTime;
        int mSuccessiveRateLimitCycles = 0;

        ErrorRecord(long startTime, int count) {
            this.mStartTime = startTime;
            this.mCount = count;
        }

        public void setStartTime(long startTime) {
            this.mStartTime = startTime;
        }

        public void setCount(int count) {
            this.mCount = count;
        }

        public void incrementCount() {
            this.mCount++;
        }

        public void setSuccessiveRateLimitCycles(int successiveRateLimitCycles) {
            this.mSuccessiveRateLimitCycles = successiveRateLimitCycles;
        }

        public void incrementSuccessiveRateLimitCycles() {
            this.mSuccessiveRateLimitCycles++;
        }

        public long getStartTime() {
            return this.mStartTime;
        }

        public int getCount() {
            return this.mCount;
        }

        public int getSuccessiveRateLimitCycles() {
            return this.mSuccessiveRateLimitCycles;
        }

        public boolean isRepeated() {
            return this.mSuccessiveRateLimitCycles >= 2;
        }

        public int getAllowedEntries() {
            return isRepeated() ? com.android.server.am.DropboxRateLimiter.this.mStrictRatelimitAllowedEntries : com.android.server.am.DropboxRateLimiter.this.mRateLimitAllowedEntries;
        }

        public long getBufferDuration() {
            return isRepeated() ? com.android.server.am.DropboxRateLimiter.this.mStrictRateLimitBufferDuration : com.android.server.am.DropboxRateLimiter.this.mRateLimitBufferDuration;
        }

        public boolean hasExpired(long currentTime) {
            long bufferExpiry = com.android.server.am.DropboxRateLimiter.this.mRateLimitBufferExpiryFactor * getBufferDuration();
            return currentTime - this.mStartTime > bufferExpiry;
        }
    }

    private static class DefaultClock implements com.android.server.am.DropboxRateLimiter.Clock {
        private DefaultClock() {
        }

        @Override // com.android.server.am.DropboxRateLimiter.Clock
        public long uptimeMillis() {
            return android.os.SystemClock.uptimeMillis();
        }
    }
}
