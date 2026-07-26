package com.android.server.utils.quota;

/* JADX INFO: loaded from: classes3.dex */
public class CountQuotaTracker extends com.android.server.utils.quota.QuotaTracker {
    private static final boolean DEBUG = false;
    private static final int MSG_CLEAN_UP_EVENTS = 1;
    private final android.util.ArrayMap<com.android.server.utils.quota.Category, java.lang.Long> mCategoryCountWindowSizesMs;
    private java.util.function.Function<java.lang.Void, com.android.server.utils.quota.CountQuotaTracker.ExecutionStats> mCreateExecutionStats;
    private java.util.function.Function<java.lang.Void, android.util.LongArrayQueue> mCreateLongArrayQueue;
    private final com.android.server.utils.quota.CountQuotaTracker.DeleteEventTimesFunctor mDeleteOldEventTimesFunctor;
    private final com.android.server.utils.quota.CountQuotaTracker.EarliestEventTimeFunctor mEarliestEventTimeFunctor;
    private final android.app.AlarmManager.OnAlarmListener mEventCleanupAlarmListener;
    private final com.android.server.utils.quota.UptcMap<android.util.LongArrayQueue> mEventTimes;
    private final com.android.server.utils.quota.UptcMap<com.android.server.utils.quota.CountQuotaTracker.ExecutionStats> mExecutionStatsCache;
    private final android.os.Handler mHandler;
    private boolean mHasCleanUpEvents;
    private final android.util.ArrayMap<com.android.server.utils.quota.Category, java.lang.Integer> mMaxCategoryCounts;
    private long mMaxPeriodMs;
    private long mNextCleanupTimeElapsed;
    private static final java.lang.String TAG = com.android.server.utils.quota.CountQuotaTracker.class.getSimpleName();
    private static final java.lang.String ALARM_TAG_CLEANUP = com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER + TAG + ".cleanup*";

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ boolean isWithinQuota(int i, java.lang.String str, java.lang.String str2) {
        return super.isWithinQuota(i, str, str2);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void registerQuotaChangeListener(com.android.server.utils.quota.QuotaChangeListener quotaChangeListener) {
        super.registerQuotaChangeListener(quotaChangeListener);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void setEnabled(boolean z) {
        super.setEnabled(z);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void setQuotaFree(int i, java.lang.String str, boolean z) {
        super.setQuotaFree(i, str, z);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void setQuotaFree(boolean z) {
        super.setQuotaFree(z);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public /* bridge */ /* synthetic */ void unregisterQuotaChangeListener(com.android.server.utils.quota.QuotaChangeListener quotaChangeListener) {
        super.unregisterQuotaChangeListener(quotaChangeListener);
    }

    static class ExecutionStats {
        public int countInWindow;
        public int countLimit;
        public long expirationTimeElapsed;
        public long inQuotaTimeElapsed;
        public long windowSizeMs;

        ExecutionStats() {
        }

        public java.lang.String toString() {
            return "expirationTime=" + this.expirationTimeElapsed + ", windowSizeMs=" + this.windowSizeMs + ", countLimit=" + this.countLimit + ", countInWindow=" + this.countInWindow + ", inQuotaTime=" + this.inQuotaTimeElapsed;
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.utils.quota.CountQuotaTracker.ExecutionStats)) {
                return false;
            }
            com.android.server.utils.quota.CountQuotaTracker.ExecutionStats other = (com.android.server.utils.quota.CountQuotaTracker.ExecutionStats) obj;
            return this.expirationTimeElapsed == other.expirationTimeElapsed && this.windowSizeMs == other.windowSizeMs && this.countLimit == other.countLimit && this.countInWindow == other.countInWindow && this.inQuotaTimeElapsed == other.inQuotaTimeElapsed;
        }

        public int hashCode() {
            int result = (0 * 31) + java.lang.Long.hashCode(this.expirationTimeElapsed);
            return (((((((result * 31) + java.lang.Long.hashCode(this.windowSizeMs)) * 31) + this.countLimit) * 31) + this.countInWindow) * 31) + java.lang.Long.hashCode(this.inQuotaTimeElapsed);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    public CountQuotaTracker(android.content.Context context, com.android.server.utils.quota.Categorizer categorizer) {
        this(context, categorizer, new com.android.server.utils.quota.QuotaTracker.Injector());
    }

    /* JADX WARN: Multi-variable type inference failed */
    CountQuotaTracker(android.content.Context context, com.android.server.utils.quota.Categorizer categorizer, com.android.server.utils.quota.QuotaTracker.Injector injector) {
        super(context, categorizer, injector);
        this.mEventTimes = new com.android.server.utils.quota.UptcMap<>();
        this.mExecutionStatsCache = new com.android.server.utils.quota.UptcMap<>();
        this.mNextCleanupTimeElapsed = 0L;
        this.mEventCleanupAlarmListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda6
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.lambda$new$0();
            }
        };
        this.mCategoryCountWindowSizesMs = new android.util.ArrayMap<>();
        this.mMaxCategoryCounts = new android.util.ArrayMap<>();
        this.mMaxPeriodMs = 0L;
        this.mHasCleanUpEvents = true;
        this.mEarliestEventTimeFunctor = new com.android.server.utils.quota.CountQuotaTracker.EarliestEventTimeFunctor();
        this.mDeleteOldEventTimesFunctor = new com.android.server.utils.quota.CountQuotaTracker.DeleteEventTimesFunctor();
        this.mCreateLongArrayQueue = new java.util.function.Function() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.utils.quota.CountQuotaTracker.lambda$new$4((java.lang.Void) obj);
            }
        };
        this.mCreateExecutionStats = new java.util.function.Function() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.utils.quota.CountQuotaTracker.lambda$new$5((java.lang.Void) obj);
            }
        };
        this.mHandler = new com.android.server.utils.quota.CountQuotaTracker.CqtHandler(context.getMainLooper());
    }

    public boolean noteEvent(int userId, java.lang.String packageName, java.lang.String tag) {
        synchronized (this.mLock) {
            if (isEnabledLocked() && !isQuotaFreeLocked(userId, packageName)) {
                long nowElapsed = this.mInjector.getElapsedRealtime();
                android.util.LongArrayQueue times = this.mEventTimes.getOrCreate(userId, packageName, tag, this.mCreateLongArrayQueue);
                times.addLast(nowElapsed);
                com.android.server.utils.quota.CountQuotaTracker.ExecutionStats stats = getExecutionStatsLocked(userId, packageName, tag);
                stats.countInWindow++;
                stats.expirationTimeElapsed = java.lang.Math.min(stats.expirationTimeElapsed, stats.windowSizeMs + nowElapsed);
                if (stats.countInWindow == stats.countLimit) {
                    long windowEdgeElapsed = nowElapsed - stats.windowSizeMs;
                    while (times.size() > 0 && times.peekFirst() < windowEdgeElapsed) {
                        times.removeFirst();
                    }
                    stats.inQuotaTimeElapsed = times.peekFirst() + stats.windowSizeMs;
                    postQuotaStatusChanged(userId, packageName, tag);
                } else if (stats.countLimit > 9 && stats.countInWindow == (stats.countLimit * 4) / 5) {
                    android.util.Slog.w(TAG, com.android.server.utils.quota.Uptc.string(userId, packageName, tag) + " has reached 80% of it's count limit of " + stats.countLimit);
                }
                maybeScheduleCleanupAlarmLocked();
                return isWithinQuotaLocked(stats);
            }
            return true;
        }
    }

    public void setCountLimit(com.android.server.utils.quota.Category category, int limit, long timeWindowMs) {
        if (limit < 0 || timeWindowMs < 0) {
            throw new java.lang.IllegalArgumentException("Limit and window size must be nonnegative.");
        }
        synchronized (this.mLock) {
            java.lang.Integer oldLimit = this.mMaxCategoryCounts.put(category, java.lang.Integer.valueOf(limit));
            long newWindowSizeMs = java.lang.Math.max(20000L, java.lang.Math.min(timeWindowMs, com.android.server.usage.UnixCalendar.MONTH_IN_MILLIS));
            java.lang.Long oldWindowSizeMs = this.mCategoryCountWindowSizesMs.put(category, java.lang.Long.valueOf(newWindowSizeMs));
            if (oldLimit == null || oldWindowSizeMs == null || oldLimit.intValue() != limit || oldWindowSizeMs.longValue() != newWindowSizeMs) {
                this.mDeleteOldEventTimesFunctor.updateMaxPeriod();
                this.mMaxPeriodMs = this.mDeleteOldEventTimesFunctor.mMaxPeriodMs;
                invalidateAllExecutionStatsLocked();
                scheduleQuotaCheck();
            }
        }
    }

    public int getLimit(com.android.server.utils.quota.Category category) {
        int iIntValue;
        synchronized (this.mLock) {
            java.lang.Integer limit = this.mMaxCategoryCounts.get(category);
            if (limit == null) {
                throw new java.lang.IllegalArgumentException("Limit for " + category + " not defined");
            }
            iIntValue = limit.intValue();
        }
        return iIntValue;
    }

    public long getWindowSizeMs(com.android.server.utils.quota.Category category) {
        long jLongValue;
        synchronized (this.mLock) {
            java.lang.Long limitMs = this.mCategoryCountWindowSizesMs.get(category);
            if (limitMs == null) {
                throw new java.lang.IllegalArgumentException("Limit for " + category + " not defined");
            }
            jLongValue = limitMs.longValue();
        }
        return jLongValue;
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    void dropEverythingLocked() {
        this.mExecutionStatsCache.clear();
        this.mEventTimes.clear();
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    android.os.Handler getHandler() {
        return this.mHandler;
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    long getInQuotaTimeElapsedLocked(int userId, java.lang.String packageName, java.lang.String tag) {
        return getExecutionStatsLocked(userId, packageName, tag).inQuotaTimeElapsed;
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    void handleRemovedAppLocked(int userId, java.lang.String packageName) {
        if (packageName == null) {
            android.util.Slog.wtf(TAG, "Told app removed but given null package name.");
        } else {
            this.mEventTimes.delete(userId, packageName);
            this.mExecutionStatsCache.delete(userId, packageName);
        }
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    void handleRemovedUserLocked(int userId) {
        this.mEventTimes.delete(userId);
        this.mExecutionStatsCache.delete(userId);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    boolean isWithinQuotaLocked(int userId, java.lang.String packageName, java.lang.String tag) {
        if (isEnabledLocked() && !isQuotaFreeLocked(userId, packageName)) {
            return isWithinQuotaLocked(getExecutionStatsLocked(userId, packageName, tag));
        }
        return true;
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    void maybeUpdateAllQuotaStatusLocked() {
        final com.android.server.utils.quota.UptcMap<java.lang.Boolean> doneMap = new com.android.server.utils.quota.UptcMap<>();
        this.mEventTimes.forEach(new com.android.server.utils.quota.UptcMap.UptcDataConsumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda0
            @Override // com.android.server.utils.quota.UptcMap.UptcDataConsumer
            public final void accept(int i, java.lang.String str, java.lang.String str2, java.lang.Object obj) {
                this.f$0.lambda$maybeUpdateAllQuotaStatusLocked$1(doneMap, i, str, str2, (android.util.LongArrayQueue) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeUpdateAllQuotaStatusLocked$1(com.android.server.utils.quota.UptcMap doneMap, int userId, java.lang.String packageName, java.lang.String tag, android.util.LongArrayQueue events) {
        if (!doneMap.contains(userId, packageName, tag)) {
            maybeUpdateStatusForUptcLocked(userId, packageName, tag);
            doneMap.add(userId, packageName, tag, java.lang.Boolean.TRUE);
        }
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    void maybeUpdateQuotaStatus(int userId, java.lang.String packageName, java.lang.String tag) {
        synchronized (this.mLock) {
            maybeUpdateStatusForUptcLocked(userId, packageName, tag);
        }
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    void onQuotaFreeChangedLocked(boolean isFree) {
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    void onQuotaFreeChangedLocked(int userId, java.lang.String packageName, boolean isFree) {
        maybeUpdateStatusForPkgLocked(userId, packageName);
    }

    private boolean isWithinQuotaLocked(com.android.server.utils.quota.CountQuotaTracker.ExecutionStats stats) {
        return isUnderCountQuotaLocked(stats);
    }

    private boolean isUnderCountQuotaLocked(com.android.server.utils.quota.CountQuotaTracker.ExecutionStats stats) {
        return stats.countInWindow < stats.countLimit;
    }

    com.android.server.utils.quota.CountQuotaTracker.ExecutionStats getExecutionStatsLocked(int userId, java.lang.String packageName, java.lang.String tag) {
        return getExecutionStatsLocked(userId, packageName, tag, true);
    }

    private com.android.server.utils.quota.CountQuotaTracker.ExecutionStats getExecutionStatsLocked(int userId, java.lang.String packageName, java.lang.String tag, boolean refreshStatsIfOld) {
        com.android.server.utils.quota.CountQuotaTracker.ExecutionStats stats = this.mExecutionStatsCache.getOrCreate(userId, packageName, tag, this.mCreateExecutionStats);
        if (refreshStatsIfOld) {
            com.android.server.utils.quota.Category category = this.mCategorizer.getCategory(userId, packageName, tag);
            long countWindowSizeMs = this.mCategoryCountWindowSizesMs.getOrDefault(category, Long.MAX_VALUE).longValue();
            int countLimit = this.mMaxCategoryCounts.getOrDefault(category, Integer.MAX_VALUE).intValue();
            if (stats.expirationTimeElapsed <= this.mInjector.getElapsedRealtime() || stats.windowSizeMs != countWindowSizeMs || stats.countLimit != countLimit) {
                stats.windowSizeMs = countWindowSizeMs;
                stats.countLimit = countLimit;
                updateExecutionStatsLocked(userId, packageName, tag, stats);
            }
        }
        return stats;
    }

    void updateExecutionStatsLocked(int userId, java.lang.String packageName, java.lang.String tag, com.android.server.utils.quota.CountQuotaTracker.ExecutionStats stats) {
        android.util.LongArrayQueue events;
        long emptyTimeMs;
        stats.countInWindow = 0;
        if (stats.countLimit == 0) {
            stats.inQuotaTimeElapsed = Long.MAX_VALUE;
        } else {
            stats.inQuotaTimeElapsed = 0L;
        }
        long nowElapsed = this.mInjector.getElapsedRealtime();
        stats.expirationTimeElapsed = this.mMaxPeriodMs + nowElapsed;
        android.util.LongArrayQueue events2 = this.mEventTimes.get(userId, packageName, tag);
        if (events2 == null) {
            return;
        }
        long emptyTimeMs2 = Long.MAX_VALUE - nowElapsed;
        long eventStartWindowElapsed = nowElapsed - stats.windowSizeMs;
        int i = events2.size() - 1;
        while (i >= 0) {
            long eventTimeElapsed = events2.get(i);
            if (eventTimeElapsed < eventStartWindowElapsed) {
                break;
            }
            stats.countInWindow++;
            long emptyTimeMs3 = java.lang.Math.min(emptyTimeMs2, eventTimeElapsed - eventStartWindowElapsed);
            if (stats.countInWindow < stats.countLimit) {
                events = events2;
                emptyTimeMs = emptyTimeMs3;
            } else {
                events = events2;
                emptyTimeMs = emptyTimeMs3;
                stats.inQuotaTimeElapsed = java.lang.Math.max(stats.inQuotaTimeElapsed, stats.windowSizeMs + eventTimeElapsed);
            }
            i--;
            events2 = events;
            emptyTimeMs2 = emptyTimeMs;
        }
        stats.expirationTimeElapsed = nowElapsed + emptyTimeMs2;
    }

    private void invalidateAllExecutionStatsLocked() {
        final long nowElapsed = this.mInjector.getElapsedRealtime();
        this.mExecutionStatsCache.forEach(new java.util.function.Consumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.utils.quota.CountQuotaTracker.lambda$invalidateAllExecutionStatsLocked$2(nowElapsed, (com.android.server.utils.quota.CountQuotaTracker.ExecutionStats) obj);
            }
        });
    }

    static /* synthetic */ void lambda$invalidateAllExecutionStatsLocked$2(long nowElapsed, com.android.server.utils.quota.CountQuotaTracker.ExecutionStats appStats) {
        if (appStats != null) {
            appStats.expirationTimeElapsed = nowElapsed;
        }
    }

    private void invalidateAllExecutionStatsLocked(int userId, java.lang.String packageName) {
        android.util.ArrayMap<java.lang.String, com.android.server.utils.quota.CountQuotaTracker.ExecutionStats> appStats = this.mExecutionStatsCache.get(userId, packageName);
        if (appStats != null) {
            long nowElapsed = this.mInjector.getElapsedRealtime();
            int numStats = appStats.size();
            for (int i = 0; i < numStats; i++) {
                com.android.server.utils.quota.CountQuotaTracker.ExecutionStats stats = appStats.valueAt(i);
                if (stats != null) {
                    stats.expirationTimeElapsed = nowElapsed;
                }
            }
        }
    }

    private void invalidateExecutionStatsLocked(int userId, java.lang.String packageName, java.lang.String tag) {
        com.android.server.utils.quota.CountQuotaTracker.ExecutionStats stats = this.mExecutionStatsCache.get(userId, packageName, tag);
        if (stats != null) {
            stats.expirationTimeElapsed = this.mInjector.getElapsedRealtime();
        }
    }

    private static final class EarliestEventTimeFunctor implements java.util.function.Consumer<android.util.LongArrayQueue> {
        long earliestTimeElapsed;

        private EarliestEventTimeFunctor() {
            this.earliestTimeElapsed = Long.MAX_VALUE;
        }

        @Override // java.util.function.Consumer
        public void accept(android.util.LongArrayQueue events) {
            if (events != null && events.size() > 0) {
                this.earliestTimeElapsed = java.lang.Math.min(this.earliestTimeElapsed, events.get(0));
            }
        }

        void reset() {
            this.earliestTimeElapsed = Long.MAX_VALUE;
        }
    }

    void maybeScheduleCleanupAlarmLocked() {
        if (this.mNextCleanupTimeElapsed > this.mInjector.getElapsedRealtime() || !this.mHasCleanUpEvents) {
            return;
        }
        this.mHasCleanUpEvents = false;
        this.mEarliestEventTimeFunctor.reset();
        this.mEventTimes.forEach(this.mEarliestEventTimeFunctor);
        long earliestEndElapsed = this.mEarliestEventTimeFunctor.earliestTimeElapsed;
        if (earliestEndElapsed == Long.MAX_VALUE) {
            return;
        }
        long nextCleanupElapsed = this.mMaxPeriodMs + earliestEndElapsed;
        if (nextCleanupElapsed - this.mNextCleanupTimeElapsed <= 600000) {
            nextCleanupElapsed += 600000;
        }
        this.mNextCleanupTimeElapsed = nextCleanupElapsed;
        scheduleAlarm(3, nextCleanupElapsed, ALARM_TAG_CLEANUP, this.mEventCleanupAlarmListener);
    }

    private boolean maybeUpdateStatusForPkgLocked(final int userId, final java.lang.String packageName) {
        final com.android.server.utils.quota.UptcMap<java.lang.Boolean> done = new com.android.server.utils.quota.UptcMap<>();
        if (!this.mEventTimes.contains(userId, packageName)) {
            return false;
        }
        android.util.ArrayMap<java.lang.String, android.util.LongArrayQueue> events = this.mEventTimes.get(userId, packageName);
        if (events == null) {
            android.util.Slog.wtf(TAG, "Events map was null even though mEventTimes said it contained " + com.android.server.utils.quota.Uptc.string(userId, packageName, null));
            return false;
        }
        final boolean[] changed = {false};
        events.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$maybeUpdateStatusForPkgLocked$3(done, userId, packageName, changed, (java.lang.String) obj, (android.util.LongArrayQueue) obj2);
            }
        });
        return changed[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeUpdateStatusForPkgLocked$3(com.android.server.utils.quota.UptcMap done, int userId, java.lang.String packageName, boolean[] changed, java.lang.String tag, android.util.LongArrayQueue eventList) {
        if (!done.contains(userId, packageName, tag)) {
            changed[0] = changed[0] | maybeUpdateStatusForUptcLocked(userId, packageName, tag);
            done.add(userId, packageName, tag, java.lang.Boolean.TRUE);
        }
    }

    private boolean maybeUpdateStatusForUptcLocked(int userId, java.lang.String packageName, java.lang.String tag) {
        boolean newInQuota;
        boolean oldInQuota = isWithinQuotaLocked(getExecutionStatsLocked(userId, packageName, tag, false));
        if (!isEnabledLocked() || isQuotaFreeLocked(userId, packageName)) {
            newInQuota = true;
        } else {
            newInQuota = isWithinQuotaLocked(getExecutionStatsLocked(userId, packageName, tag, true));
        }
        if (!newInQuota) {
            maybeScheduleStartAlarmLocked(userId, packageName, tag);
        } else {
            cancelScheduledStartAlarmLocked(userId, packageName, tag);
        }
        if (oldInQuota == newInQuota) {
            return false;
        }
        postQuotaStatusChanged(userId, packageName, tag);
        return true;
    }

    private final class DeleteEventTimesFunctor implements java.util.function.Consumer<android.util.LongArrayQueue> {
        private long mMaxPeriodMs;

        private DeleteEventTimesFunctor() {
        }

        @Override // java.util.function.Consumer
        public void accept(android.util.LongArrayQueue times) {
            if (times != null) {
                while (times.size() > 0 && times.peekFirst() <= com.android.server.utils.quota.CountQuotaTracker.this.mInjector.getElapsedRealtime() - this.mMaxPeriodMs) {
                    times.removeFirst();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateMaxPeriod() {
            long maxPeriodMs = 0;
            for (int i = com.android.server.utils.quota.CountQuotaTracker.this.mCategoryCountWindowSizesMs.size() - 1; i >= 0; i--) {
                maxPeriodMs = java.lang.Long.max(maxPeriodMs, ((java.lang.Long) com.android.server.utils.quota.CountQuotaTracker.this.mCategoryCountWindowSizesMs.valueAt(i)).longValue());
            }
            this.mMaxPeriodMs = maxPeriodMs;
        }
    }

    void deleteObsoleteEventsLocked() {
        this.mEventTimes.forEach(this.mDeleteOldEventTimesFunctor);
    }

    private class CqtHandler extends android.os.Handler {
        CqtHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            synchronized (com.android.server.utils.quota.CountQuotaTracker.this.mLock) {
                switch (msg.what) {
                    case 1:
                        com.android.server.utils.quota.CountQuotaTracker.this.deleteObsoleteEventsLocked();
                        com.android.server.utils.quota.CountQuotaTracker.this.mHasCleanUpEvents = true;
                        com.android.server.utils.quota.CountQuotaTracker.this.maybeScheduleCleanupAlarmLocked();
                        break;
                }
            }
        }
    }

    static /* synthetic */ android.util.LongArrayQueue lambda$new$4(java.lang.Void aVoid) {
        return new android.util.LongArrayQueue();
    }

    static /* synthetic */ com.android.server.utils.quota.CountQuotaTracker.ExecutionStats lambda$new$5(java.lang.Void aVoid) {
        return new com.android.server.utils.quota.CountQuotaTracker.ExecutionStats();
    }

    android.util.LongArrayQueue getEvents(int userId, java.lang.String packageName, java.lang.String tag) {
        return this.mEventTimes.get(userId, packageName, tag);
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public void dump(final android.util.IndentingPrintWriter pw) {
        pw.print(TAG);
        pw.println(":");
        pw.increaseIndent();
        synchronized (this.mLock) {
            super.dump(pw);
            pw.println();
            pw.println("Instantaneous events:");
            pw.increaseIndent();
            this.mEventTimes.forEach(new com.android.server.utils.quota.UptcMap.UptcDataConsumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda4
                @Override // com.android.server.utils.quota.UptcMap.UptcDataConsumer
                public final void accept(int i, java.lang.String str, java.lang.String str2, java.lang.Object obj) {
                    com.android.server.utils.quota.CountQuotaTracker.lambda$dump$6(pw, i, str, str2, (android.util.LongArrayQueue) obj);
                }
            });
            pw.decreaseIndent();
            pw.println();
            pw.println("Cached execution stats:");
            pw.increaseIndent();
            this.mExecutionStatsCache.forEach(new com.android.server.utils.quota.UptcMap.UptcDataConsumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda5
                @Override // com.android.server.utils.quota.UptcMap.UptcDataConsumer
                public final void accept(int i, java.lang.String str, java.lang.String str2, java.lang.Object obj) {
                    com.android.server.utils.quota.CountQuotaTracker.lambda$dump$7(pw, i, str, str2, (com.android.server.utils.quota.CountQuotaTracker.ExecutionStats) obj);
                }
            });
            pw.decreaseIndent();
            pw.println();
            pw.println("Limits:");
            pw.increaseIndent();
            int numCategories = this.mCategoryCountWindowSizesMs.size();
            for (int i = 0; i < numCategories; i++) {
                com.android.server.utils.quota.Category category = this.mCategoryCountWindowSizesMs.keyAt(i);
                pw.print(category);
                pw.print(": ");
                pw.print(this.mMaxCategoryCounts.get(category));
                pw.print(" events in ");
                pw.println(android.util.TimeUtils.formatDuration(this.mCategoryCountWindowSizesMs.get(category).longValue()));
            }
            pw.decreaseIndent();
        }
        pw.decreaseIndent();
    }

    static /* synthetic */ void lambda$dump$6(android.util.IndentingPrintWriter pw, int userId, java.lang.String pkgName, java.lang.String tag, android.util.LongArrayQueue events) {
        if (events.size() > 0) {
            pw.print(com.android.server.utils.quota.Uptc.string(userId, pkgName, tag));
            pw.println(":");
            pw.increaseIndent();
            pw.print(events.get(0));
            for (int i = 1; i < events.size(); i++) {
                pw.print(", ");
                pw.print(events.get(i));
            }
            pw.decreaseIndent();
            pw.println();
        }
    }

    static /* synthetic */ void lambda$dump$7(android.util.IndentingPrintWriter pw, int userId, java.lang.String pkgName, java.lang.String tag, com.android.server.utils.quota.CountQuotaTracker.ExecutionStats stats) {
        if (stats != null) {
            pw.print(com.android.server.utils.quota.Uptc.string(userId, pkgName, tag));
            pw.println(":");
            pw.increaseIndent();
            pw.println(stats);
            pw.decreaseIndent();
        }
    }

    @Override // com.android.server.utils.quota.QuotaTracker
    public void dump(final android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        synchronized (this.mLock) {
            super.dump(proto, 1146756268033L);
            for (int i = 0; i < this.mCategoryCountWindowSizesMs.size(); i++) {
                com.android.server.utils.quota.Category category = this.mCategoryCountWindowSizesMs.keyAt(i);
                long clToken = proto.start(2246267895810L);
                category.dumpDebug(proto, 1146756268033L);
                proto.write(1120986464258L, this.mMaxCategoryCounts.get(category).intValue());
                proto.write(1112396529667L, this.mCategoryCountWindowSizesMs.get(category).longValue());
                proto.end(clToken);
            }
            this.mExecutionStatsCache.forEach(new com.android.server.utils.quota.UptcMap.UptcDataConsumer() { // from class: com.android.server.utils.quota.CountQuotaTracker$$ExternalSyntheticLambda1
                @Override // com.android.server.utils.quota.UptcMap.UptcDataConsumer
                public final void accept(int i2, java.lang.String str, java.lang.String str2, java.lang.Object obj) {
                    this.f$0.lambda$dump$8(proto, i2, str, str2, (com.android.server.utils.quota.CountQuotaTracker.ExecutionStats) obj);
                }
            });
            proto.end(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$8(android.util.proto.ProtoOutputStream proto, int userId, java.lang.String pkgName, java.lang.String tag, com.android.server.utils.quota.CountQuotaTracker.ExecutionStats stats) {
        boolean isQuotaFree = isIndividualQuotaFreeLocked(userId, pkgName);
        long j = 2246267895811L;
        long usToken = proto.start(2246267895811L);
        new com.android.server.utils.quota.Uptc(userId, pkgName, tag).dumpDebug(proto, 1146756268033L);
        proto.write(1133871366146L, isQuotaFree);
        android.util.LongArrayQueue events = this.mEventTimes.get(userId, pkgName, tag);
        if (events != null) {
            int j2 = events.size() - 1;
            while (j2 >= 0) {
                long eToken = proto.start(j);
                proto.write(1112396529665L, events.get(j2));
                proto.end(eToken);
                j2--;
                j = 2246267895811L;
            }
        }
        long statsToken = proto.start(2246267895812L);
        proto.write(1112396529665L, stats.expirationTimeElapsed);
        proto.write(1112396529666L, stats.windowSizeMs);
        proto.write(1120986464259L, stats.countLimit);
        proto.write(1120986464260L, stats.countInWindow);
        proto.write(1112396529669L, stats.inQuotaTimeElapsed);
        proto.end(statsToken);
        proto.end(usToken);
    }
}
