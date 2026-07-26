package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class AmbientBrightnessStatsTracker {
    static final int MAX_DAYS_TO_TRACK = 7;
    private final com.android.server.display.AmbientBrightnessStatsTracker.AmbientBrightnessStats mAmbientBrightnessStats;
    private float mCurrentAmbientBrightness;
    private int mCurrentUserId;
    private final com.android.server.display.AmbientBrightnessStatsTracker.Injector mInjector;
    private final com.android.server.display.AmbientBrightnessStatsTracker.Timer mTimer;
    private final android.os.UserManager mUserManager;
    private static final java.lang.String TAG = "AmbientBrightnessStatsTracker";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);
    static final float[] BUCKET_BOUNDARIES_FOR_NEW_STATS = {0.0f, 0.1f, 0.3f, 1.0f, 3.0f, 10.0f, 30.0f, 100.0f, 300.0f, 1000.0f, 3000.0f, 10000.0f};

    interface Clock {
        long elapsedTimeMillis();
    }

    public AmbientBrightnessStatsTracker(android.os.UserManager userManager, com.android.server.display.AmbientBrightnessStatsTracker.Injector injector) {
        this.mUserManager = userManager;
        if (injector != null) {
            this.mInjector = injector;
        } else {
            this.mInjector = new com.android.server.display.AmbientBrightnessStatsTracker.Injector();
        }
        this.mAmbientBrightnessStats = new com.android.server.display.AmbientBrightnessStatsTracker.AmbientBrightnessStats();
        this.mTimer = new com.android.server.display.AmbientBrightnessStatsTracker.Timer(new com.android.server.display.AmbientBrightnessStatsTracker.Clock() { // from class: com.android.server.display.AmbientBrightnessStatsTracker$$ExternalSyntheticLambda0
            @Override // com.android.server.display.AmbientBrightnessStatsTracker.Clock
            public final long elapsedTimeMillis() {
                return this.f$0.lambda$new$0();
            }
        });
        this.mCurrentAmbientBrightness = -1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ long lambda$new$0() {
        return this.mInjector.elapsedRealtimeMillis();
    }

    public synchronized void start() {
        this.mTimer.reset();
        this.mTimer.start();
    }

    public synchronized void stop() {
        if (this.mTimer.isRunning()) {
            this.mAmbientBrightnessStats.log(this.mCurrentUserId, this.mInjector.getLocalDate(), this.mCurrentAmbientBrightness, this.mTimer.totalDurationSec());
        }
        this.mTimer.reset();
        this.mCurrentAmbientBrightness = -1.0f;
    }

    public synchronized void add(int userId, float newAmbientBrightness) {
        if (this.mTimer.isRunning()) {
            if (userId == this.mCurrentUserId) {
                this.mAmbientBrightnessStats.log(this.mCurrentUserId, this.mInjector.getLocalDate(), this.mCurrentAmbientBrightness, this.mTimer.totalDurationSec());
            } else {
                if (DEBUG) {
                    android.util.Slog.v(TAG, "User switched since last sensor event.");
                }
                this.mCurrentUserId = userId;
            }
            this.mTimer.reset();
            this.mTimer.start();
            this.mCurrentAmbientBrightness = newAmbientBrightness;
        } else if (DEBUG) {
            android.util.Slog.e(TAG, "Timer not running while trying to add brightness stats.");
        }
    }

    public synchronized void writeStats(java.io.OutputStream stream) throws java.io.IOException {
        this.mAmbientBrightnessStats.writeToXML(stream);
    }

    public synchronized void readStats(java.io.InputStream stream) throws java.io.IOException {
        this.mAmbientBrightnessStats.readFromXML(stream);
    }

    public synchronized java.util.ArrayList<android.hardware.display.AmbientBrightnessDayStats> getUserStats(int userId) {
        return this.mAmbientBrightnessStats.getUserStats(userId);
    }

    public synchronized void dump(java.io.PrintWriter pw) {
        pw.println("AmbientBrightnessStats:");
        pw.print(this.mAmbientBrightnessStats);
    }

    class AmbientBrightnessStats {
        private static final java.lang.String ATTR_BUCKET_BOUNDARIES = "bucket-boundaries";
        private static final java.lang.String ATTR_BUCKET_STATS = "bucket-stats";
        private static final java.lang.String ATTR_LOCAL_DATE = "local-date";
        private static final java.lang.String ATTR_USER = "user";
        private static final java.lang.String TAG_AMBIENT_BRIGHTNESS_DAY_STATS = "ambient-brightness-day-stats";
        private static final java.lang.String TAG_AMBIENT_BRIGHTNESS_STATS = "ambient-brightness-stats";
        private java.util.Map<java.lang.Integer, java.util.Deque<android.hardware.display.AmbientBrightnessDayStats>> mStats = new java.util.HashMap();

        public AmbientBrightnessStats() {
        }

        public void log(int userId, java.time.LocalDate localDate, float ambientBrightness, float durationSec) {
            java.util.Deque<android.hardware.display.AmbientBrightnessDayStats> userStats = getOrCreateUserStats(this.mStats, userId);
            android.hardware.display.AmbientBrightnessDayStats dayStats = getOrCreateDayStats(userStats, localDate);
            dayStats.log(ambientBrightness, durationSec);
        }

        public java.util.ArrayList<android.hardware.display.AmbientBrightnessDayStats> getUserStats(int userId) {
            if (this.mStats.containsKey(java.lang.Integer.valueOf(userId))) {
                return new java.util.ArrayList<>(this.mStats.get(java.lang.Integer.valueOf(userId)));
            }
            return null;
        }

        public void writeToXML(java.io.OutputStream stream) throws java.io.IOException {
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(stream);
            out.startDocument((java.lang.String) null, true);
            out.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            java.time.LocalDate cutOffDate = com.android.server.display.AmbientBrightnessStatsTracker.this.mInjector.getLocalDate().minusDays(7L);
            out.startTag((java.lang.String) null, TAG_AMBIENT_BRIGHTNESS_STATS);
            for (java.util.Map.Entry<java.lang.Integer, java.util.Deque<android.hardware.display.AmbientBrightnessDayStats>> entry : this.mStats.entrySet()) {
                for (android.hardware.display.AmbientBrightnessDayStats userDayStats : entry.getValue()) {
                    int userSerialNumber = com.android.server.display.AmbientBrightnessStatsTracker.this.mInjector.getUserSerialNumber(com.android.server.display.AmbientBrightnessStatsTracker.this.mUserManager, entry.getKey().intValue());
                    if (userSerialNumber != -1 && userDayStats.getLocalDate().isAfter(cutOffDate)) {
                        out.startTag((java.lang.String) null, TAG_AMBIENT_BRIGHTNESS_DAY_STATS);
                        out.attributeInt((java.lang.String) null, ATTR_USER, userSerialNumber);
                        out.attribute((java.lang.String) null, ATTR_LOCAL_DATE, userDayStats.getLocalDate().toString());
                        java.lang.StringBuilder bucketBoundariesValues = new java.lang.StringBuilder();
                        java.lang.StringBuilder timeSpentValues = new java.lang.StringBuilder();
                        for (int i = 0; i < userDayStats.getBucketBoundaries().length; i++) {
                            if (i > 0) {
                                bucketBoundariesValues.append(",");
                                timeSpentValues.append(",");
                            }
                            bucketBoundariesValues.append(userDayStats.getBucketBoundaries()[i]);
                            timeSpentValues.append(userDayStats.getStats()[i]);
                        }
                        out.attribute((java.lang.String) null, ATTR_BUCKET_BOUNDARIES, bucketBoundariesValues.toString());
                        out.attribute((java.lang.String) null, ATTR_BUCKET_STATS, timeSpentValues.toString());
                        out.endTag((java.lang.String) null, TAG_AMBIENT_BRIGHTNESS_DAY_STATS);
                    }
                }
            }
            out.endTag((java.lang.String) null, TAG_AMBIENT_BRIGHTNESS_STATS);
            out.endDocument();
            stream.flush();
        }

        /* JADX WARN: Code restructure failed: missing block: B:44:0x00e8, code lost:
        
            r17.mStats = r2;
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x00eb, code lost:
        
            return;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void readFromXML(java.io.InputStream r18) throws java.io.IOException {
            /*
                Method dump skipped, instruction units count: 270
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.AmbientBrightnessStatsTracker.AmbientBrightnessStats.readFromXML(java.io.InputStream):void");
        }

        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            for (java.util.Map.Entry<java.lang.Integer, java.util.Deque<android.hardware.display.AmbientBrightnessDayStats>> entry : this.mStats.entrySet()) {
                for (android.hardware.display.AmbientBrightnessDayStats dayStats : entry.getValue()) {
                    builder.append("  ");
                    builder.append(entry.getKey()).append(" ");
                    builder.append(dayStats).append("\n");
                }
            }
            return builder.toString();
        }

        private java.util.Deque<android.hardware.display.AmbientBrightnessDayStats> getOrCreateUserStats(java.util.Map<java.lang.Integer, java.util.Deque<android.hardware.display.AmbientBrightnessDayStats>> stats, int userId) {
            if (!stats.containsKey(java.lang.Integer.valueOf(userId))) {
                stats.put(java.lang.Integer.valueOf(userId), new java.util.ArrayDeque());
            }
            return stats.get(java.lang.Integer.valueOf(userId));
        }

        private android.hardware.display.AmbientBrightnessDayStats getOrCreateDayStats(java.util.Deque<android.hardware.display.AmbientBrightnessDayStats> userStats, java.time.LocalDate localDate) {
            android.hardware.display.AmbientBrightnessDayStats lastBrightnessStats = userStats.peekLast();
            if (lastBrightnessStats != null && lastBrightnessStats.getLocalDate().equals(localDate)) {
                return lastBrightnessStats;
            }
            if (lastBrightnessStats != null) {
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.AMBIENT_BRIGHTNESS_STATS_REPORTED, lastBrightnessStats.getStats(), lastBrightnessStats.getBucketBoundaries());
            }
            android.hardware.display.AmbientBrightnessDayStats dayStats = new android.hardware.display.AmbientBrightnessDayStats(localDate, com.android.server.display.AmbientBrightnessStatsTracker.BUCKET_BOUNDARIES_FOR_NEW_STATS);
            if (userStats.size() == 7) {
                userStats.poll();
            }
            userStats.offer(dayStats);
            return dayStats;
        }
    }

    static class Timer {
        private final com.android.server.display.AmbientBrightnessStatsTracker.Clock clock;
        private long startTimeMillis;
        private boolean started;

        public Timer(com.android.server.display.AmbientBrightnessStatsTracker.Clock clock) {
            this.clock = clock;
        }

        public void reset() {
            this.started = false;
        }

        public void start() {
            if (!this.started) {
                this.startTimeMillis = this.clock.elapsedTimeMillis();
                this.started = true;
            }
        }

        public boolean isRunning() {
            return this.started;
        }

        public float totalDurationSec() {
            if (this.started) {
                return (float) ((this.clock.elapsedTimeMillis() - this.startTimeMillis) / 1000.0d);
            }
            return 0.0f;
        }
    }

    static class Injector {
        Injector() {
        }

        public long elapsedRealtimeMillis() {
            return android.os.SystemClock.elapsedRealtime();
        }

        public int getUserSerialNumber(android.os.UserManager userManager, int userId) {
            return userManager.getUserSerialNumber(userId);
        }

        public int getUserId(android.os.UserManager userManager, int userSerialNumber) {
            return userManager.getUserHandle(userSerialNumber);
        }

        public java.time.LocalDate getLocalDate() {
            return java.time.LocalDate.now();
        }
    }
}
