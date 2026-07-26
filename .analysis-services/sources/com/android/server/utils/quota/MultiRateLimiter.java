package com.android.server.utils.quota;

/* JADX INFO: loaded from: classes3.dex */
public class MultiRateLimiter {
    private static final com.android.server.utils.quota.CountQuotaTracker[] EMPTY_TRACKER_ARRAY = new com.android.server.utils.quota.CountQuotaTracker[0];
    private static final java.lang.String TAG = "MultiRateLimiter";
    private final java.lang.Object mLock;
    private final com.android.server.utils.quota.CountQuotaTracker[] mQuotaTrackers;

    private MultiRateLimiter(java.util.List<com.android.server.utils.quota.CountQuotaTracker> quotaTrackers) {
        this.mLock = new java.lang.Object();
        this.mQuotaTrackers = (com.android.server.utils.quota.CountQuotaTracker[]) quotaTrackers.toArray(EMPTY_TRACKER_ARRAY);
    }

    public void noteEvent(int userId, java.lang.String packageName, java.lang.String tag) {
        synchronized (this.mLock) {
            noteEventLocked(userId, packageName, tag);
        }
    }

    public boolean isWithinQuota(int userId, java.lang.String packageName, java.lang.String tag) {
        boolean zIsWithinQuotaLocked;
        synchronized (this.mLock) {
            zIsWithinQuotaLocked = isWithinQuotaLocked(userId, packageName, tag);
        }
        return zIsWithinQuotaLocked;
    }

    public void clear(int userId, java.lang.String packageName) {
        synchronized (this.mLock) {
            clearLocked(userId, packageName);
        }
    }

    private void noteEventLocked(int userId, java.lang.String packageName, java.lang.String tag) {
        for (com.android.server.utils.quota.CountQuotaTracker quotaTracker : this.mQuotaTrackers) {
            quotaTracker.noteEvent(userId, packageName, tag);
        }
    }

    private boolean isWithinQuotaLocked(int userId, java.lang.String packageName, java.lang.String tag) {
        for (com.android.server.utils.quota.CountQuotaTracker quotaTracker : this.mQuotaTrackers) {
            if (!quotaTracker.isWithinQuota(userId, packageName, tag)) {
                return false;
            }
        }
        return true;
    }

    private void clearLocked(int userId, java.lang.String packageName) {
        for (com.android.server.utils.quota.CountQuotaTracker quotaTracker : this.mQuotaTrackers) {
            quotaTracker.onAppRemovedLocked(userId, packageName);
        }
    }

    public static class Builder {
        private final com.android.server.utils.quota.Categorizer mCategorizer;
        private final com.android.server.utils.quota.Category mCategory;
        private final android.content.Context mContext;
        private final com.android.server.utils.quota.QuotaTracker.Injector mInjector;
        private final java.util.List<com.android.server.utils.quota.CountQuotaTracker> mQuotaTrackers;

        Builder(android.content.Context context, com.android.server.utils.quota.QuotaTracker.Injector injector) {
            this.mQuotaTrackers = new java.util.ArrayList();
            this.mContext = context;
            this.mInjector = injector;
            this.mCategorizer = com.android.server.utils.quota.Categorizer.SINGLE_CATEGORIZER;
            this.mCategory = com.android.server.utils.quota.Category.SINGLE_CATEGORY;
        }

        public Builder(android.content.Context context) {
            this(context, null);
        }

        public com.android.server.utils.quota.MultiRateLimiter.Builder addRateLimit(int limit, java.time.Duration windowSize) {
            com.android.server.utils.quota.CountQuotaTracker countQuotaTracker;
            if (this.mInjector != null) {
                countQuotaTracker = new com.android.server.utils.quota.CountQuotaTracker(this.mContext, this.mCategorizer, this.mInjector);
            } else {
                countQuotaTracker = new com.android.server.utils.quota.CountQuotaTracker(this.mContext, this.mCategorizer);
            }
            countQuotaTracker.setCountLimit(this.mCategory, limit, windowSize.toMillis());
            this.mQuotaTrackers.add(countQuotaTracker);
            return this;
        }

        public com.android.server.utils.quota.MultiRateLimiter.Builder addRateLimit(com.android.server.utils.quota.MultiRateLimiter.RateLimit rateLimit) {
            return addRateLimit(rateLimit.mLimit, rateLimit.mWindowSize);
        }

        public com.android.server.utils.quota.MultiRateLimiter.Builder addRateLimits(com.android.server.utils.quota.MultiRateLimiter.RateLimit[] rateLimits) {
            for (com.android.server.utils.quota.MultiRateLimiter.RateLimit rateLimit : rateLimits) {
                addRateLimit(rateLimit);
            }
            return this;
        }

        public com.android.server.utils.quota.MultiRateLimiter build() {
            return new com.android.server.utils.quota.MultiRateLimiter(this.mQuotaTrackers);
        }
    }

    public static class RateLimit {
        public final int mLimit;
        public final java.time.Duration mWindowSize;

        private RateLimit(int limit, java.time.Duration windowSize) {
            this.mLimit = limit;
            this.mWindowSize = windowSize;
        }

        public static com.android.server.utils.quota.MultiRateLimiter.RateLimit create(int limit, java.time.Duration windowSize) {
            return new com.android.server.utils.quota.MultiRateLimiter.RateLimit(limit, windowSize);
        }
    }
}
