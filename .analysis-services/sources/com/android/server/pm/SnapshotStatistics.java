package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class SnapshotStatistics {
    private static final int REBUILD_LATENCY_BUCKET_LESS_THAN_100_MILLIS = 100;
    private static final int REBUILD_LATENCY_BUCKET_LESS_THAN_10_MILLIS = 10;
    private static final int REBUILD_LATENCY_BUCKET_LESS_THAN_1_MILLIS = 1;
    private static final int REBUILD_LATENCY_BUCKET_LESS_THAN_20_MILLIS = 20;
    private static final int REBUILD_LATENCY_BUCKET_LESS_THAN_2_MILLIS = 2;
    private static final int REBUILD_LATENCY_BUCKET_LESS_THAN_50_MILLIS = 50;
    private static final int REBUILD_LATENCY_BUCKET_LESS_THAN_5_MILLIS = 5;
    private static final int REUSE_COUNT_BUCKET_LESS_THAN_1 = 1;
    private static final int REUSE_COUNT_BUCKET_LESS_THAN_10 = 10;
    private static final int REUSE_COUNT_BUCKET_LESS_THAN_100 = 100;
    private static final int REUSE_COUNT_BUCKET_LESS_THAN_1000 = 1000;
    private static final int REUSE_COUNT_BUCKET_LESS_THAN_10000 = 10000;
    public static final int SNAPSHOT_BIG_BUILD_TIME_US = 10000;
    public static final int SNAPSHOT_BUILD_REPORT_LIMIT = 10;
    private static final long SNAPSHOT_LOG_INTERVAL_US = java.util.concurrent.TimeUnit.DAYS.toMicros(1);
    public static final int SNAPSHOT_REPORTABLE_BUILD_TIME_US = 30000;
    public static final int SNAPSHOT_SHORT_LIFETIME = 5;
    public static final int SNAPSHOT_TICK_INTERVAL_MS = 60000;
    private static final int US_IN_MS = 1000;
    private android.os.Handler mHandler;
    private long mLastLogTimeUs;
    private final com.android.server.pm.SnapshotStatistics.Stats[] mLong;
    private int mPackageCount;
    private final com.android.server.pm.SnapshotStatistics.Stats[] mShort;
    private final java.lang.Object mLock = new java.lang.Object();
    private int mEventsReported = 0;
    private final com.android.server.pm.SnapshotStatistics.BinMap mTimeBins = new com.android.server.pm.SnapshotStatistics.BinMap(new int[]{1, 2, 5, 10, 20, 50, 100});
    private final com.android.server.pm.SnapshotStatistics.BinMap mUseBins = new com.android.server.pm.SnapshotStatistics.BinMap(new int[]{1, 10, 100, 1000, 10000});

    private int usToMs(int us) {
        return us / 1000;
    }

    private static class BinMap {
        private final int mCount;
        private final int mMaxBin;
        private final int[] mUserKey;

        BinMap(int[] userKey) {
            this.mUserKey = java.util.Arrays.copyOf(userKey, userKey.length);
            this.mCount = this.mUserKey.length + 1;
            this.mMaxBin = this.mUserKey[this.mUserKey.length - 1] + 1;
        }

        public int getBin(int x) {
            if (x >= 0 && x < this.mMaxBin) {
                for (int i = 0; i < this.mUserKey.length; i++) {
                    if (x <= this.mUserKey[i]) {
                        return i;
                    }
                }
                return 0;
            }
            if (x < this.mMaxBin) {
                return 0;
            }
            return this.mUserKey.length;
        }

        public int count() {
            return this.mCount;
        }

        public int[] userKeys() {
            return this.mUserKey;
        }
    }

    public class Stats {
        public int mBigBuilds;
        public int mMaxBuildTimeUs;
        public int mMaxUsedCount;
        public int mShortLived;
        public long mStartTimeUs;
        public long mStopTimeUs;
        public final int[] mTimes;
        public int mTotalBuilds;
        public long mTotalTimeUs;
        public int mTotalUsed;
        public final int[] mUsed;

        /* JADX INFO: Access modifiers changed from: private */
        public void rebuild(int duration, int used, int buildBin, int useBin, boolean big, boolean quick) {
            this.mTotalBuilds++;
            int[] iArr = this.mTimes;
            iArr[buildBin] = iArr[buildBin] + 1;
            if (used >= 0) {
                this.mTotalUsed += used;
                int[] iArr2 = this.mUsed;
                iArr2[useBin] = iArr2[useBin] + 1;
            }
            this.mTotalTimeUs += (long) duration;
            if (big) {
                this.mBigBuilds++;
            }
            if (quick) {
                this.mShortLived++;
            }
            if (this.mMaxBuildTimeUs < duration) {
                this.mMaxBuildTimeUs = duration;
            }
            if (this.mMaxUsedCount < used) {
                this.mMaxUsedCount = used;
            }
        }

        private Stats(long now) {
            this.mStartTimeUs = 0L;
            this.mStopTimeUs = 0L;
            this.mTotalBuilds = 0;
            this.mTotalUsed = 0;
            this.mBigBuilds = 0;
            this.mShortLived = 0;
            this.mTotalTimeUs = 0L;
            this.mMaxBuildTimeUs = 0;
            this.mMaxUsedCount = 0;
            this.mStartTimeUs = now;
            this.mTimes = new int[com.android.server.pm.SnapshotStatistics.this.mTimeBins.count()];
            this.mUsed = new int[com.android.server.pm.SnapshotStatistics.this.mUseBins.count()];
        }

        private Stats(com.android.server.pm.SnapshotStatistics.Stats orig) {
            this.mStartTimeUs = 0L;
            this.mStopTimeUs = 0L;
            this.mTotalBuilds = 0;
            this.mTotalUsed = 0;
            this.mBigBuilds = 0;
            this.mShortLived = 0;
            this.mTotalTimeUs = 0L;
            this.mMaxBuildTimeUs = 0;
            this.mMaxUsedCount = 0;
            this.mStartTimeUs = orig.mStartTimeUs;
            this.mStopTimeUs = orig.mStopTimeUs;
            this.mTimes = java.util.Arrays.copyOf(orig.mTimes, orig.mTimes.length);
            this.mUsed = java.util.Arrays.copyOf(orig.mUsed, orig.mUsed.length);
            this.mTotalBuilds = orig.mTotalBuilds;
            this.mTotalUsed = orig.mTotalUsed;
            this.mBigBuilds = orig.mBigBuilds;
            this.mShortLived = orig.mShortLived;
            this.mTotalTimeUs = orig.mTotalTimeUs;
            this.mMaxBuildTimeUs = orig.mMaxBuildTimeUs;
            this.mMaxUsedCount = orig.mMaxUsedCount;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void complete(long stop) {
            this.mStopTimeUs = stop;
        }

        private java.lang.String durationToString(long us) {
            int s = (int) (us / 1000000);
            int m = s / 60;
            int s2 = s % 60;
            int h = m / 60;
            int m2 = m % 60;
            int d = h / 24;
            int h2 = h % 24;
            if (d != 0) {
                return android.text.TextUtils.formatSimple("%2d:%02d:%02d:%02d", new java.lang.Object[]{java.lang.Integer.valueOf(d), java.lang.Integer.valueOf(h2), java.lang.Integer.valueOf(m2), java.lang.Integer.valueOf(s2)});
            }
            return h2 != 0 ? android.text.TextUtils.formatSimple("%2s %02d:%02d:%02d", new java.lang.Object[]{"", java.lang.Integer.valueOf(h2), java.lang.Integer.valueOf(m2), java.lang.Integer.valueOf(s2)}) : android.text.TextUtils.formatSimple("%2s %2s %2d:%02d", new java.lang.Object[]{"", "", java.lang.Integer.valueOf(m2), java.lang.Integer.valueOf(s2)});
        }

        private void dumpPrefix(java.io.PrintWriter pw, java.lang.String indent, long now, boolean header, java.lang.String title) {
            pw.print(indent + " ");
            if (header) {
                pw.format(java.util.Locale.US, "%-23s", title);
                return;
            }
            pw.format(java.util.Locale.US, "%11s", durationToString(now - this.mStartTimeUs));
            if (this.mStopTimeUs != 0) {
                pw.format(java.util.Locale.US, " %11s", durationToString(now - this.mStopTimeUs));
            } else {
                pw.format(java.util.Locale.US, " %11s", "now");
            }
        }

        private void dumpStats(java.io.PrintWriter pw, java.lang.String indent, long now, boolean header) {
            dumpPrefix(pw, indent, now, header, "Summary stats");
            if (header) {
                pw.format(java.util.Locale.US, "  %10s  %10s  %10s  %10s  %10s  %10s", "TotBlds", "TotUsed", "BigBlds", "ShortLvd", "TotTime", "MaxTime");
            } else {
                pw.format(java.util.Locale.US, "  %10d  %10d  %10d  %10d  %10d  %10d", java.lang.Integer.valueOf(this.mTotalBuilds), java.lang.Integer.valueOf(this.mTotalUsed), java.lang.Integer.valueOf(this.mBigBuilds), java.lang.Integer.valueOf(this.mShortLived), java.lang.Long.valueOf(this.mTotalTimeUs / 1000), java.lang.Integer.valueOf(this.mMaxBuildTimeUs / 1000));
            }
            pw.println();
        }

        private void dumpTimes(java.io.PrintWriter pw, java.lang.String indent, long now, boolean header) {
            dumpPrefix(pw, indent, now, header, "Build times");
            if (header) {
                int[] keys = com.android.server.pm.SnapshotStatistics.this.mTimeBins.userKeys();
                for (int i : keys) {
                    pw.format(java.util.Locale.US, "  %10s", android.text.TextUtils.formatSimple("<= %dms", new java.lang.Object[]{java.lang.Integer.valueOf(i)}));
                }
                pw.format(java.util.Locale.US, "  %10s", android.text.TextUtils.formatSimple("> %dms", new java.lang.Object[]{java.lang.Integer.valueOf(keys[keys.length - 1])}));
            } else {
                for (int i2 = 0; i2 < this.mTimes.length; i2++) {
                    pw.format(java.util.Locale.US, "  %10d", java.lang.Integer.valueOf(this.mTimes[i2]));
                }
            }
            pw.println();
        }

        private void dumpUsage(java.io.PrintWriter pw, java.lang.String indent, long now, boolean header) {
            dumpPrefix(pw, indent, now, header, "Use counters");
            if (header) {
                int[] keys = com.android.server.pm.SnapshotStatistics.this.mUseBins.userKeys();
                for (int i : keys) {
                    pw.format(java.util.Locale.US, "  %10s", android.text.TextUtils.formatSimple("<= %d", new java.lang.Object[]{java.lang.Integer.valueOf(i)}));
                }
                pw.format(java.util.Locale.US, "  %10s", android.text.TextUtils.formatSimple("> %d", new java.lang.Object[]{java.lang.Integer.valueOf(keys[keys.length - 1])}));
            } else {
                for (int i2 = 0; i2 < this.mUsed.length; i2++) {
                    pw.format(java.util.Locale.US, "  %10d", java.lang.Integer.valueOf(this.mUsed[i2]));
                }
            }
            pw.println();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw, java.lang.String indent, long now, boolean header, java.lang.String what) {
            if (what.equals("stats")) {
                dumpStats(pw, indent, now, header);
            } else if (what.equals("times")) {
                dumpTimes(pw, indent, now, header);
            } else {
                if (what.equals("usage")) {
                    dumpUsage(pw, indent, now, header);
                    return;
                }
                throw new java.lang.IllegalArgumentException("unrecognized choice: " + what);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void logSnapshotStatistics(int packageCount) {
            long avgLatencyUs = this.mTotalBuilds == 0 ? 0L : this.mTotalTimeUs / ((long) this.mTotalBuilds);
            int avgUsedCount = this.mTotalBuilds == 0 ? 0 : this.mTotalUsed / this.mTotalBuilds;
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.PACKAGE_MANAGER_SNAPSHOT_REPORTED, this.mTimes, this.mUsed, this.mMaxBuildTimeUs, this.mMaxUsedCount, avgLatencyUs, avgUsedCount, packageCount);
        }
    }

    public SnapshotStatistics() {
        this.mHandler = null;
        long now = android.os.SystemClock.currentTimeMicro();
        this.mLong = new com.android.server.pm.SnapshotStatistics.Stats[2];
        this.mLong[0] = new com.android.server.pm.SnapshotStatistics.Stats(now);
        this.mShort = new com.android.server.pm.SnapshotStatistics.Stats[10];
        this.mShort[0] = new com.android.server.pm.SnapshotStatistics.Stats(now);
        this.mLastLogTimeUs = now;
        this.mHandler = new android.os.Handler(android.os.Looper.getMainLooper()) { // from class: com.android.server.pm.SnapshotStatistics.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                com.android.server.pm.SnapshotStatistics.this.handleMessage(msg);
            }
        };
        scheduleTick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMessage(android.os.Message msg) {
        tick();
        scheduleTick();
    }

    private void scheduleTick() {
        this.mHandler.sendEmptyMessageDelayed(0, 60000L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [long] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v2 */
    public final void rebuild(long now, long done, int hits, int packageCount) throws java.lang.Throwable {
        ?? r2 = done - now;
        int duration = (int) r2;
        boolean reportEvent = false;
        java.lang.Object obj = this.mLock;
        synchronized (obj) {
            try {
                try {
                    this.mPackageCount = packageCount;
                    int timeBin = this.mTimeBins.getBin(duration / 1000);
                    int useBin = this.mUseBins.getBin(hits);
                    boolean big = duration >= 10000;
                    boolean quick = hits <= 5;
                    this.mShort[0].rebuild(duration, hits, timeBin, useBin, big, quick);
                    this.mLong[0].rebuild(duration, hits, timeBin, useBin, big, quick);
                    if (duration >= 30000) {
                        int i = this.mEventsReported;
                        this.mEventsReported = i + 1;
                        if (i < 10) {
                            reportEvent = true;
                        }
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    r2 = obj;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
        if (reportEvent) {
            com.android.server.EventLogTags.writePmSnapshotRebuild(duration / 1000, hits);
        }
    }

    private void shift(com.android.server.pm.SnapshotStatistics.Stats[] s, long now) {
        s[0].complete(now);
        for (int i = s.length - 1; i > 0; i--) {
            s[i] = s[i - 1];
        }
        s[0] = new com.android.server.pm.SnapshotStatistics.Stats(now);
    }

    private void tick() {
        synchronized (this.mLock) {
            long now = android.os.SystemClock.currentTimeMicro();
            if (now - this.mLastLogTimeUs > SNAPSHOT_LOG_INTERVAL_US) {
                shift(this.mLong, now);
                this.mLastLogTimeUs = now;
                this.mLong[this.mLong.length - 1].logSnapshotStatistics(this.mPackageCount);
            }
            shift(this.mShort, now);
            this.mEventsReported = 0;
        }
    }

    private void dump(java.io.PrintWriter pw, java.lang.String indent, long now, com.android.server.pm.SnapshotStatistics.Stats[] l, com.android.server.pm.SnapshotStatistics.Stats[] s, java.lang.String what) {
        l[0].dump(pw, indent, now, true, what);
        for (int i = 0; i < s.length; i++) {
            if (s[i] != null) {
                s[i].dump(pw, indent, now, false, what);
            }
        }
        for (int i2 = 0; i2 < l.length; i2++) {
            if (l[i2] != null) {
                l[i2].dump(pw, indent, now, false, what);
            }
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String indent, long now, int unrecorded, boolean brief) throws java.lang.Throwable {
        com.android.server.pm.SnapshotStatistics.Stats[] l;
        com.android.server.pm.SnapshotStatistics.Stats[] s;
        synchronized (this.mLock) {
            try {
                l = (com.android.server.pm.SnapshotStatistics.Stats[]) java.util.Arrays.copyOf(this.mLong, this.mLong.length);
                l[0] = new com.android.server.pm.SnapshotStatistics.Stats(l[0]);
                s = (com.android.server.pm.SnapshotStatistics.Stats[]) java.util.Arrays.copyOf(this.mShort, this.mShort.length);
                s[0] = new com.android.server.pm.SnapshotStatistics.Stats(s[0]);
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        pw.format(java.util.Locale.US, "%s Unrecorded-hits: %d", indent, java.lang.Integer.valueOf(unrecorded));
        pw.println();
        dump(pw, indent, now, l, s, "stats");
        if (brief) {
            return;
        }
        pw.println();
        dump(pw, indent, now, l, s, "times");
        pw.println();
        dump(pw, indent, now, l, s, "usage");
    }
}
