package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class PowerStatsScheduler {
    private final long mAggregatedPowerStatsSpanDuration;
    private final com.android.server.power.stats.PowerStatsScheduler.AlarmScheduler mAlarmScheduler;
    private final com.android.internal.os.Clock mClock;
    private final java.util.function.Supplier<java.lang.Long> mEarliestAvailableBatteryHistoryTimeMs;
    private boolean mEnablePeriodicPowerStatsCollection;
    private final android.os.Handler mHandler;
    private long mLastSavedSpanEndMonotonicTime;
    private final com.android.internal.os.MonotonicClock mMonotonicClock;
    private final long mPowerStatsAggregationPeriod;
    private final com.android.server.power.stats.PowerStatsAggregator mPowerStatsAggregator;
    private final java.lang.Runnable mPowerStatsCollector;
    private final com.android.server.power.stats.PowerStatsStore mPowerStatsStore;
    private static final long MINUTE_IN_MILLIS = java.util.concurrent.TimeUnit.MINUTES.toMillis(1);
    private static final long HOUR_IN_MILLIS = java.util.concurrent.TimeUnit.HOURS.toMillis(1);

    public interface AlarmScheduler {
        void scheduleAlarm(long j, java.lang.String str, android.app.AlarmManager.OnAlarmListener onAlarmListener, android.os.Handler handler);
    }

    public PowerStatsScheduler(java.lang.Runnable powerStatsCollector, com.android.server.power.stats.PowerStatsAggregator powerStatsAggregator, long aggregatedPowerStatsSpanDuration, long powerStatsAggregationPeriod, com.android.server.power.stats.PowerStatsStore powerStatsStore, com.android.server.power.stats.PowerStatsScheduler.AlarmScheduler alarmScheduler, com.android.internal.os.Clock clock, com.android.internal.os.MonotonicClock monotonicClock, java.util.function.Supplier<java.lang.Long> earliestAvailableBatteryHistoryTimeMs, android.os.Handler handler) {
        this.mPowerStatsAggregator = powerStatsAggregator;
        this.mAggregatedPowerStatsSpanDuration = aggregatedPowerStatsSpanDuration;
        this.mPowerStatsAggregationPeriod = powerStatsAggregationPeriod;
        this.mPowerStatsStore = powerStatsStore;
        this.mAlarmScheduler = alarmScheduler;
        this.mClock = clock;
        this.mMonotonicClock = monotonicClock;
        this.mHandler = handler;
        this.mPowerStatsCollector = powerStatsCollector;
        this.mEarliestAvailableBatteryHistoryTimeMs = earliestAvailableBatteryHistoryTimeMs;
    }

    public void start(boolean enablePeriodicPowerStatsCollection) {
        this.mEnablePeriodicPowerStatsCollection = enablePeriodicPowerStatsCollection;
        if (this.mEnablePeriodicPowerStatsCollection) {
            schedulePowerStatsAggregation();
            scheduleNextPowerStatsAggregation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleNextPowerStatsAggregation() {
        this.mAlarmScheduler.scheduleAlarm(this.mClock.elapsedRealtime() + this.mPowerStatsAggregationPeriod, "PowerStats", new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.power.stats.PowerStatsScheduler$$ExternalSyntheticLambda5
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.lambda$scheduleNextPowerStatsAggregation$0();
            }
        }, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleNextPowerStatsAggregation$0() {
        schedulePowerStatsAggregation();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.stats.PowerStatsScheduler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.scheduleNextPowerStatsAggregation();
            }
        });
    }

    public void schedulePowerStatsAggregation() {
        this.mPowerStatsCollector.run();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.stats.PowerStatsScheduler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.aggregateAndStorePowerStats();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void aggregateAndStorePowerStats() {
        long startTime;
        long currentTimeMillis = this.mClock.currentTimeMillis();
        long currentMonotonicTime = this.mMonotonicClock.monotonicTime();
        long startTime2 = getLastSavedSpanEndMonotonicTime();
        if (startTime2 >= 0) {
            startTime = startTime2;
        } else {
            startTime = this.mEarliestAvailableBatteryHistoryTimeMs.get().longValue();
        }
        long endTimeMs = alignToWallClock(startTime + this.mAggregatedPowerStatsSpanDuration, this.mAggregatedPowerStatsSpanDuration, currentMonotonicTime, currentTimeMillis);
        while (endTimeMs <= currentMonotonicTime) {
            this.mPowerStatsAggregator.aggregatePowerStats(startTime, endTimeMs, new java.util.function.Consumer() { // from class: com.android.server.power.stats.PowerStatsScheduler$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$aggregateAndStorePowerStats$1((com.android.server.power.stats.AggregatedPowerStats) obj);
                }
            });
            startTime = endTimeMs;
            endTimeMs += this.mAggregatedPowerStatsSpanDuration;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$aggregateAndStorePowerStats$1(com.android.server.power.stats.AggregatedPowerStats stats) {
        storeAggregatedPowerStats(stats);
        this.mLastSavedSpanEndMonotonicTime = stats.getStartTime() + stats.getDuration();
    }

    public void aggregateAndDumpPowerStats(java.io.PrintWriter pw) {
        if (this.mHandler.getLooper().isCurrentThread()) {
            throw new java.lang.IllegalStateException("Should not be executed on the bg handler thread.");
        }
        schedulePowerStatsAggregation();
        awaitCompletion();
        final android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.stats.PowerStatsScheduler$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$aggregateAndDumpPowerStats$3(ipw);
            }
        });
        awaitCompletion();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$aggregateAndDumpPowerStats$3(final android.util.IndentingPrintWriter ipw) {
        this.mPowerStatsStore.dump(ipw);
        long powerStoreEndMonotonicTime = getLastSavedSpanEndMonotonicTime();
        this.mPowerStatsAggregator.aggregatePowerStats(powerStoreEndMonotonicTime, -1L, new java.util.function.Consumer() { // from class: com.android.server.power.stats.PowerStatsScheduler$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.power.stats.PowerStatsScheduler.lambda$aggregateAndDumpPowerStats$2(ipw, (com.android.server.power.stats.AggregatedPowerStats) obj);
            }
        });
    }

    static /* synthetic */ void lambda$aggregateAndDumpPowerStats$2(android.util.IndentingPrintWriter ipw, com.android.server.power.stats.AggregatedPowerStats stats) {
        com.android.server.power.stats.PowerStatsSpan span = com.android.server.power.stats.PowerStatsStore.createPowerStatsSpan(stats);
        if (span != null) {
            span.dump(ipw);
        }
    }

    public static long alignToWallClock(long targetMonotonicTime, long interval, long currentMonotonicTime, long currentTimeMillis) {
        long targetWallClockTime = currentTimeMillis + (targetMonotonicTime - currentMonotonicTime);
        if (interval >= MINUTE_IN_MILLIS && java.util.concurrent.TimeUnit.HOURS.toMillis(1L) % interval == 0) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTimeInMillis((MINUTE_IN_MILLIS + targetWallClockTime) - 1);
            cal.set(13, 0);
            cal.set(14, 0);
            int intervalInMinutes = (int) (interval / MINUTE_IN_MILLIS);
            cal.set(12, (((cal.get(12) + intervalInMinutes) - 1) / intervalInMinutes) * intervalInMinutes);
            long adjustment = cal.getTimeInMillis() - targetWallClockTime;
            return targetMonotonicTime + adjustment;
        }
        if (interval >= HOUR_IN_MILLIS && java.util.concurrent.TimeUnit.DAYS.toMillis(1L) % interval == 0) {
            java.util.Calendar cal2 = java.util.Calendar.getInstance();
            cal2.setTimeInMillis((HOUR_IN_MILLIS + targetWallClockTime) - 1);
            cal2.set(12, 0);
            cal2.set(13, 0);
            cal2.set(14, 0);
            int intervalInHours = (int) (interval / HOUR_IN_MILLIS);
            cal2.set(11, (((cal2.get(11) + intervalInHours) - 1) / intervalInHours) * intervalInHours);
            long adjustment2 = cal2.getTimeInMillis() - targetWallClockTime;
            return targetMonotonicTime + adjustment2;
        }
        return targetMonotonicTime;
    }

    private long getLastSavedSpanEndMonotonicTime() {
        if (this.mLastSavedSpanEndMonotonicTime != 0) {
            return this.mLastSavedSpanEndMonotonicTime;
        }
        this.mLastSavedSpanEndMonotonicTime = -1L;
        for (com.android.server.power.stats.PowerStatsSpan.Metadata metadata : this.mPowerStatsStore.getTableOfContents()) {
            if (metadata.getSections().contains(com.android.server.power.stats.AggregatedPowerStatsSection.TYPE)) {
                for (com.android.server.power.stats.PowerStatsSpan.TimeFrame timeFrame : metadata.getTimeFrames()) {
                    long endMonotonicTime = timeFrame.startMonotonicTime + timeFrame.duration;
                    if (endMonotonicTime > this.mLastSavedSpanEndMonotonicTime) {
                        this.mLastSavedSpanEndMonotonicTime = endMonotonicTime;
                    }
                }
            }
        }
        return this.mLastSavedSpanEndMonotonicTime;
    }

    private void storeAggregatedPowerStats(com.android.server.power.stats.AggregatedPowerStats stats) {
        this.mPowerStatsStore.storeAggregatedPowerStats(stats);
    }

    private void awaitCompletion() {
        android.os.ConditionVariable done = new android.os.ConditionVariable();
        android.os.Handler handler = this.mHandler;
        java.util.Objects.requireNonNull(done);
        handler.post(new com.android.server.power.stats.BatteryStatsImpl$$ExternalSyntheticLambda10(done));
        done.block();
    }
}
