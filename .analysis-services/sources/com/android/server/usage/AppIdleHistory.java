package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class AppIdleHistory {
    static final java.lang.String APP_IDLE_FILENAME = "app_idle_stats.xml";
    private static final java.lang.String ATTR_BUCKET = "bucket";
    private static final java.lang.String ATTR_BUCKETING_REASON = "bucketReason";
    private static final java.lang.String ATTR_BUCKET_ACTIVE_TIMEOUT_TIME = "activeTimeoutTime";
    private static final java.lang.String ATTR_BUCKET_WORKING_SET_TIMEOUT_TIME = "workingSetTimeoutTime";
    private static final java.lang.String ATTR_CURRENT_BUCKET = "appLimitBucket";
    private static final java.lang.String ATTR_ELAPSED_IDLE = "elapsedIdleTime";
    private static final java.lang.String ATTR_EXPIRY_TIME = "expiry";
    private static final java.lang.String ATTR_LAST_PREDICTED_TIME = "lastPredictedTime";
    private static final java.lang.String ATTR_LAST_RESTRICTION_ATTEMPT_ELAPSED = "lastRestrictionAttemptElapsedTime";
    private static final java.lang.String ATTR_LAST_RESTRICTION_ATTEMPT_REASON = "lastRestrictionAttemptReason";
    private static final java.lang.String ATTR_LAST_RUN_JOB_TIME = "lastJobRunTime";
    private static final java.lang.String ATTR_LAST_USED_BY_USER_ELAPSED = "lastUsedByUserElapsedTime";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_NEXT_ESTIMATED_APP_LAUNCH_TIME = "nextEstimatedAppLaunchTime";
    private static final java.lang.String ATTR_SCREEN_IDLE = "screenIdleTime";
    private static final java.lang.String ATTR_VERSION = "version";
    private static final boolean DEBUG = false;
    static final int IDLE_BUCKET_CUTOFF = 40;
    private static final long ONE_MINUTE = 60000;
    static final int STANDBY_BUCKET_UNKNOWN = -1;
    private static final java.lang.String TAG = "AppIdleHistory";
    private static final java.lang.String TAG_BUCKET_EXPIRY_TIMES = "expiryTimes";
    private static final java.lang.String TAG_ITEM = "item";
    private static final java.lang.String TAG_PACKAGE = "package";
    private static final java.lang.String TAG_PACKAGES = "packages";
    private static final int XML_VERSION_ADD_BUCKET_EXPIRY_TIMES = 1;
    private static final int XML_VERSION_CURRENT = 1;
    private static final int XML_VERSION_INITIAL = 0;
    private long mElapsedDuration;
    private long mElapsedSnapshot;
    private android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory>> mIdleHistory = new android.util.SparseArray<>();
    private boolean mScreenOn;
    private long mScreenOnDuration;
    private long mScreenOnSnapshot;
    private final java.io.File mStorageDir;

    static class AppUsageHistory {
        android.util.SparseLongArray bucketExpiryTimesMs;
        int bucketingReason;
        int currentBucket;
        int lastInformedBucket;
        long lastJobRunTime;
        int lastPredictedBucket = -1;
        long lastPredictedTime;
        long lastRestrictAttemptElapsedTime;
        int lastRestrictReason;
        long lastUsedByUserElapsedTime;
        long lastUsedElapsedTime;
        long lastUsedScreenTime;
        long nextEstimatedLaunchTime;

        AppUsageHistory() {
        }
    }

    AppIdleHistory(java.io.File storageDir, long elapsedRealtime) {
        this.mElapsedSnapshot = elapsedRealtime;
        this.mScreenOnSnapshot = elapsedRealtime;
        this.mStorageDir = storageDir;
        readScreenOnTime();
    }

    public void updateDisplay(boolean screenOn, long elapsedRealtime) {
        if (screenOn == this.mScreenOn) {
            return;
        }
        this.mScreenOn = screenOn;
        if (this.mScreenOn) {
            this.mScreenOnSnapshot = elapsedRealtime;
            return;
        }
        this.mScreenOnDuration += elapsedRealtime - this.mScreenOnSnapshot;
        this.mElapsedDuration += elapsedRealtime - this.mElapsedSnapshot;
        this.mElapsedSnapshot = elapsedRealtime;
    }

    public long getScreenOnTime(long elapsedRealtime) {
        long screenOnTime = this.mScreenOnDuration;
        if (this.mScreenOn) {
            return screenOnTime + (elapsedRealtime - this.mScreenOnSnapshot);
        }
        return screenOnTime;
    }

    java.io.File getScreenOnTimeFile() {
        return new java.io.File(this.mStorageDir, "screen_on_time");
    }

    private void readScreenOnTime() {
        java.io.File screenOnTimeFile = getScreenOnTimeFile();
        if (screenOnTimeFile.exists()) {
            try {
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(screenOnTimeFile));
                this.mScreenOnDuration = java.lang.Long.parseLong(reader.readLine());
                this.mElapsedDuration = java.lang.Long.parseLong(reader.readLine());
                reader.close();
                return;
            } catch (java.io.IOException | java.lang.NumberFormatException e) {
                return;
            }
        }
        writeScreenOnTime();
    }

    private void writeScreenOnTime() {
        android.util.AtomicFile screenOnTimeFile = new android.util.AtomicFile(getScreenOnTimeFile());
        java.io.FileOutputStream fos = null;
        try {
            fos = screenOnTimeFile.startWrite();
            fos.write((java.lang.Long.toString(this.mScreenOnDuration) + "\n" + java.lang.Long.toString(this.mElapsedDuration) + "\n").getBytes());
            screenOnTimeFile.finishWrite(fos);
        } catch (java.io.IOException e) {
            screenOnTimeFile.failWrite(fos);
        }
    }

    public void writeAppIdleDurations() {
        long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
        this.mElapsedDuration += elapsedRealtime - this.mElapsedSnapshot;
        this.mElapsedSnapshot = elapsedRealtime;
        writeScreenOnTime();
    }

    com.android.server.usage.AppIdleHistory.AppUsageHistory reportUsage(com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory, java.lang.String packageName, int userId, int newBucket, int usageReason, long nowElapsedRealtimeMs, long expiryElapsedRealtimeMs) {
        int newBucket2 = newBucket;
        int bucketingReason = usageReason | 768;
        boolean isUserUsage = com.android.server.usage.AppStandbyController.isUserUsage(bucketingReason);
        if (appUsageHistory.currentBucket == 45 && !isUserUsage && (appUsageHistory.bucketingReason & 65280) != 512) {
            newBucket2 = 45;
            bucketingReason = appUsageHistory.bucketingReason;
        } else if (expiryElapsedRealtimeMs > nowElapsedRealtimeMs) {
            long expiryTimeMs = getElapsedTime(expiryElapsedRealtimeMs);
            if (appUsageHistory.bucketExpiryTimesMs == null) {
                appUsageHistory.bucketExpiryTimesMs = new android.util.SparseLongArray();
            }
            long currentExpiryTimeMs = appUsageHistory.bucketExpiryTimesMs.get(newBucket2);
            appUsageHistory.bucketExpiryTimesMs.put(newBucket2, java.lang.Math.max(expiryTimeMs, currentExpiryTimeMs));
            removeElapsedExpiryTimes(appUsageHistory, getElapsedTime(nowElapsedRealtimeMs));
        }
        if (nowElapsedRealtimeMs != 0) {
            appUsageHistory.lastUsedElapsedTime = this.mElapsedDuration + (nowElapsedRealtimeMs - this.mElapsedSnapshot);
            if (isUserUsage) {
                appUsageHistory.lastUsedByUserElapsedTime = appUsageHistory.lastUsedElapsedTime;
            }
            appUsageHistory.lastUsedScreenTime = getScreenOnTime(nowElapsedRealtimeMs);
        }
        if (appUsageHistory.currentBucket >= newBucket2) {
            if (appUsageHistory.currentBucket > newBucket2) {
                appUsageHistory.currentBucket = newBucket2;
                logAppStandbyBucketChanged(packageName, userId, newBucket2, bucketingReason);
            }
            appUsageHistory.bucketingReason = bucketingReason;
        }
        return appUsageHistory;
    }

    private void removeElapsedExpiryTimes(com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory, long elapsedTimeMs) {
        if (appUsageHistory.bucketExpiryTimesMs == null) {
            return;
        }
        for (int i = appUsageHistory.bucketExpiryTimesMs.size() - 1; i >= 0; i--) {
            if (appUsageHistory.bucketExpiryTimesMs.valueAt(i) < elapsedTimeMs) {
                appUsageHistory.bucketExpiryTimesMs.removeAt(i);
            }
        }
    }

    public com.android.server.usage.AppIdleHistory.AppUsageHistory reportUsage(java.lang.String packageName, int userId, int newBucket, int usageReason, long nowElapsedRealtimeMs, long expiryElapsedRealtimeMs) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory history = getPackageHistory(userHistory, packageName, nowElapsedRealtimeMs, true);
        return reportUsage(history, packageName, userId, newBucket, usageReason, nowElapsedRealtimeMs, expiryElapsedRealtimeMs);
    }

    private android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> getUserHistory(int userId) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = this.mIdleHistory.get(userId);
        if (userHistory == null) {
            android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory2 = new android.util.ArrayMap<>();
            this.mIdleHistory.put(userId, userHistory2);
            readAppIdleTimes(userId, userHistory2);
            return userHistory2;
        }
        return userHistory;
    }

    private com.android.server.usage.AppIdleHistory.AppUsageHistory getPackageHistory(android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory, java.lang.String packageName, long elapsedRealtime, boolean create) {
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = userHistory.get(packageName);
        if (appUsageHistory == null && create) {
            com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory2 = new com.android.server.usage.AppIdleHistory.AppUsageHistory();
            appUsageHistory2.lastUsedByUserElapsedTime = -2147483648L;
            appUsageHistory2.lastUsedElapsedTime = -2147483648L;
            appUsageHistory2.lastUsedScreenTime = -2147483648L;
            appUsageHistory2.lastPredictedTime = -2147483648L;
            appUsageHistory2.currentBucket = 50;
            appUsageHistory2.bucketingReason = 256;
            appUsageHistory2.lastInformedBucket = -1;
            appUsageHistory2.lastJobRunTime = Long.MIN_VALUE;
            userHistory.put(packageName, appUsageHistory2);
            return appUsageHistory2;
        }
        return appUsageHistory;
    }

    public void onUserRemoved(int userId) {
        this.mIdleHistory.remove(userId);
    }

    public boolean isIdle(java.lang.String packageName, int userId, long elapsedRealtime) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtime, true);
        return appUsageHistory.currentBucket >= 40;
    }

    public com.android.server.usage.AppIdleHistory.AppUsageHistory getAppUsageHistory(java.lang.String packageName, int userId, long elapsedRealtime) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtime, true);
        return appUsageHistory;
    }

    public void setAppStandbyBucket(java.lang.String packageName, int userId, long elapsedRealtime, int bucket, int reason) {
        setAppStandbyBucket(packageName, userId, elapsedRealtime, bucket, reason, false);
    }

    public void setAppStandbyBucket(java.lang.String packageName, int userId, long elapsedRealtime, int bucket, int reason, boolean resetExpiryTimes) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtime, true);
        boolean changed = appUsageHistory.currentBucket != bucket;
        appUsageHistory.currentBucket = bucket;
        appUsageHistory.bucketingReason = reason;
        long elapsed = getElapsedTime(elapsedRealtime);
        if ((65280 & reason) == 1280) {
            appUsageHistory.lastPredictedTime = elapsed;
            appUsageHistory.lastPredictedBucket = bucket;
        }
        if (resetExpiryTimes && appUsageHistory.bucketExpiryTimesMs != null) {
            appUsageHistory.bucketExpiryTimesMs.clear();
        }
        if (changed) {
            logAppStandbyBucketChanged(packageName, userId, bucket, reason);
        }
    }

    public void updateLastPrediction(com.android.server.usage.AppIdleHistory.AppUsageHistory app, long elapsedTimeAdjusted, int bucket) {
        app.lastPredictedTime = elapsedTimeAdjusted;
        app.lastPredictedBucket = bucket;
    }

    public void setEstimatedLaunchTime(java.lang.String packageName, int userId, long nowElapsed, long launchTime) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, nowElapsed, true);
        appUsageHistory.nextEstimatedLaunchTime = launchTime;
    }

    public void setLastJobRunTime(java.lang.String packageName, int userId, long elapsedRealtime) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtime, true);
        appUsageHistory.lastJobRunTime = getElapsedTime(elapsedRealtime);
    }

    void noteRestrictionAttempt(java.lang.String packageName, int userId, long elapsedRealtime, int reason) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtime, true);
        appUsageHistory.lastRestrictAttemptElapsedTime = getElapsedTime(elapsedRealtime);
        appUsageHistory.lastRestrictReason = reason;
    }

    public long getEstimatedLaunchTime(java.lang.String packageName, int userId, long nowElapsed) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, nowElapsed, false);
        if (appUsageHistory == null || appUsageHistory.nextEstimatedLaunchTime < java.lang.System.currentTimeMillis()) {
            return Long.MAX_VALUE;
        }
        return appUsageHistory.nextEstimatedLaunchTime;
    }

    public long getTimeSinceLastJobRun(java.lang.String packageName, int userId, long elapsedRealtime) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtime, false);
        if (appUsageHistory == null || appUsageHistory.lastJobRunTime == Long.MIN_VALUE) {
            return Long.MAX_VALUE;
        }
        return getElapsedTime(elapsedRealtime) - appUsageHistory.lastJobRunTime;
    }

    public long getTimeSinceLastUsedByUser(java.lang.String packageName, int userId, long elapsedRealtime) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtime, false);
        if (appUsageHistory == null || appUsageHistory.lastUsedByUserElapsedTime == Long.MIN_VALUE || appUsageHistory.lastUsedByUserElapsedTime <= 0) {
            return Long.MAX_VALUE;
        }
        return getElapsedTime(elapsedRealtime) - appUsageHistory.lastUsedByUserElapsedTime;
    }

    public int getAppStandbyBucket(java.lang.String packageName, int userId, long elapsedRealtime) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtime, false);
        if (appUsageHistory == null) {
            return 50;
        }
        return appUsageHistory.currentBucket;
    }

    public java.util.ArrayList<android.app.usage.AppStandbyInfo> getAppStandbyBuckets(int userId, boolean appIdleEnabled) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        int size = userHistory.size();
        java.util.ArrayList<android.app.usage.AppStandbyInfo> buckets = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            buckets.add(new android.app.usage.AppStandbyInfo(userHistory.keyAt(i), appIdleEnabled ? userHistory.valueAt(i).currentBucket : 10));
        }
        return buckets;
    }

    public int getAppStandbyReason(java.lang.String packageName, int userId, long elapsedRealtime) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtime, false);
        if (appUsageHistory != null) {
            return appUsageHistory.bucketingReason;
        }
        return 0;
    }

    public long getElapsedTime(long elapsedRealtime) {
        return (elapsedRealtime - this.mElapsedSnapshot) + this.mElapsedDuration;
    }

    public int setIdle(java.lang.String packageName, int userId, boolean idle, long elapsedRealtime) {
        int newBucket;
        int reason;
        if (idle) {
            newBucket = 40;
            reason = 1024;
            com.android.server.usage.AppIdleHistory.AppUsageHistory appHistory = getAppUsageHistory(packageName, userId, elapsedRealtime);
            if (appHistory.bucketExpiryTimesMs != null) {
                for (int i = appHistory.bucketExpiryTimesMs.size() - 1; i >= 0; i--) {
                    if (appHistory.bucketExpiryTimesMs.keyAt(i) < 40) {
                        appHistory.bucketExpiryTimesMs.removeAt(i);
                    }
                }
            }
        } else {
            newBucket = 10;
            reason = com.android.server.usb.descriptors.UsbTerminalTypes.TERMINAL_OUT_HEADMOUNTED;
        }
        setAppStandbyBucket(packageName, userId, elapsedRealtime, newBucket, reason, false);
        return newBucket;
    }

    public void clearUsage(java.lang.String packageName, int userId) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        userHistory.remove(packageName);
    }

    boolean shouldInformListeners(java.lang.String packageName, int userId, long elapsedRealtime, int bucket) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtime, true);
        if (appUsageHistory.lastInformedBucket != bucket) {
            appUsageHistory.lastInformedBucket = bucket;
            return true;
        }
        return false;
    }

    int getThresholdIndex(java.lang.String packageName, int userId, long elapsedRealtime, long[] screenTimeThresholds, long[] elapsedTimeThresholds) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtime, false);
        if (appUsageHistory == null || appUsageHistory.lastUsedElapsedTime < 0 || appUsageHistory.lastUsedScreenTime < 0) {
            return -1;
        }
        long screenOnDelta = getScreenOnTime(elapsedRealtime) - appUsageHistory.lastUsedScreenTime;
        long elapsedDelta = getElapsedTime(elapsedRealtime) - appUsageHistory.lastUsedElapsedTime;
        for (int i = screenTimeThresholds.length - 1; i >= 0; i--) {
            if (screenOnDelta >= screenTimeThresholds[i] && elapsedDelta >= elapsedTimeThresholds[i]) {
                return i;
            }
        }
        return 0;
    }

    private void logAppStandbyBucketChanged(java.lang.String packageName, int userId, int bucket, int reason) {
        com.android.internal.util.FrameworkStatsLog.write(258, packageName, userId, bucket, reason & 65280, reason & 255);
    }

    long getBucketExpiryTimeMs(java.lang.String packageName, int userId, int bucket, long elapsedRealtimeMs) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, elapsedRealtimeMs, false);
        if (appUsageHistory == null || appUsageHistory.bucketExpiryTimesMs == null) {
            return 0L;
        }
        return appUsageHistory.bucketExpiryTimesMs.get(bucket, 0L);
    }

    java.io.File getUserFile(int userId) {
        return new java.io.File(new java.io.File(new java.io.File(this.mStorageDir, com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_USERS), java.lang.Integer.toString(userId)), APP_IDLE_FILENAME);
    }

    void clearLastUsedTimestamps(java.lang.String packageName, int userId) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory = getUserHistory(userId);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = getPackageHistory(userHistory, packageName, android.os.SystemClock.elapsedRealtime(), false);
        if (appUsageHistory != null) {
            appUsageHistory.lastUsedByUserElapsedTime = -2147483648L;
            appUsageHistory.lastUsedElapsedTime = -2147483648L;
            appUsageHistory.lastUsedScreenTime = -2147483648L;
        }
    }

    public boolean userFileExists(int userId) {
        return getUserFile(userId).exists();
    }

    private void readAppIdleTimes(int userId, android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory) {
        android.util.AtomicFile appIdleFile;
        org.xmlpull.v1.XmlPullParser parser;
        int type;
        int i;
        int type2;
        android.util.AtomicFile appIdleFile2;
        org.xmlpull.v1.XmlPullParser parser2;
        int type3;
        long bucketWorkingSetTimeoutTime;
        java.io.FileInputStream fis = null;
        try {
            try {
                try {
                    appIdleFile = new android.util.AtomicFile(getUserFile(userId));
                    fis = appIdleFile.openRead();
                    parser = android.util.Xml.newPullParser();
                    parser.setInput(fis, java.nio.charset.StandardCharsets.UTF_8.name());
                    do {
                        type = parser.next();
                        i = 1;
                        type2 = 2;
                        if (type == 2) {
                            break;
                        }
                    } while (type != 1);
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                    android.util.Slog.e(TAG, "Unable to read app idle file for user " + userId, e);
                }
            } catch (java.io.FileNotFoundException e2) {
                android.util.Slog.d(TAG, "App idle file for user " + userId + " does not exist");
            }
            if (type != 2) {
                android.util.Slog.e(TAG, "Unable to read app idle file for user " + userId);
                return;
            }
            if (parser.getName().equals(TAG_PACKAGES)) {
                int version = getIntValue(parser, ATTR_VERSION, 0);
                while (true) {
                    int type4 = parser.next();
                    if (type4 == i) {
                        break;
                    }
                    if (type4 == type2) {
                        java.lang.String name = parser.getName();
                        if (name.equals("package")) {
                            java.lang.String packageName = parser.getAttributeValue(null, "name");
                            com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = new com.android.server.usage.AppIdleHistory.AppUsageHistory();
                            appUsageHistory.lastUsedElapsedTime = java.lang.Long.parseLong(parser.getAttributeValue(null, ATTR_ELAPSED_IDLE));
                            appUsageHistory.lastUsedByUserElapsedTime = getLongValue(parser, ATTR_LAST_USED_BY_USER_ELAPSED, appUsageHistory.lastUsedElapsedTime);
                            appUsageHistory.lastUsedScreenTime = java.lang.Long.parseLong(parser.getAttributeValue(null, ATTR_SCREEN_IDLE));
                            appUsageHistory.lastPredictedTime = getLongValue(parser, ATTR_LAST_PREDICTED_TIME, 0L);
                            java.lang.String currentBucketString = parser.getAttributeValue(null, ATTR_CURRENT_BUCKET);
                            appUsageHistory.currentBucket = currentBucketString == null ? 10 : java.lang.Integer.parseInt(currentBucketString);
                            java.lang.String bucketingReason = parser.getAttributeValue(null, ATTR_BUCKETING_REASON);
                            appUsageHistory.lastJobRunTime = getLongValue(parser, ATTR_LAST_RUN_JOB_TIME, Long.MIN_VALUE);
                            appUsageHistory.bucketingReason = 256;
                            if (bucketingReason != null) {
                                try {
                                    appUsageHistory.bucketingReason = java.lang.Integer.parseInt(bucketingReason, 16);
                                } catch (java.lang.NumberFormatException nfe) {
                                    android.util.Slog.wtf(TAG, "Unable to read bucketing reason", nfe);
                                }
                            }
                            appUsageHistory.lastRestrictAttemptElapsedTime = getLongValue(parser, ATTR_LAST_RESTRICTION_ATTEMPT_ELAPSED, 0L);
                            java.lang.String lastRestrictReason = parser.getAttributeValue(null, ATTR_LAST_RESTRICTION_ATTEMPT_REASON);
                            if (lastRestrictReason != null) {
                                try {
                                    appUsageHistory.lastRestrictReason = java.lang.Integer.parseInt(lastRestrictReason, 16);
                                } catch (java.lang.NumberFormatException nfe2) {
                                    android.util.Slog.wtf(TAG, "Unable to read last restrict reason", nfe2);
                                }
                            }
                            type3 = type4;
                            appUsageHistory.nextEstimatedLaunchTime = getLongValue(parser, ATTR_NEXT_ESTIMATED_APP_LAUNCH_TIME, 0L);
                            if (com.android.server.usage.Flags.avoidIdleCheck()) {
                                appUsageHistory.lastInformedBucket = appUsageHistory.currentBucket;
                            } else {
                                appUsageHistory.lastInformedBucket = -1;
                            }
                            userHistory.put(packageName, appUsageHistory);
                            if (version >= 1) {
                                int outerDepth = parser.getDepth();
                                while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                                    if (TAG_BUCKET_EXPIRY_TIMES.equals(parser.getName())) {
                                        readBucketExpiryTimes(parser, appUsageHistory);
                                    }
                                }
                                appIdleFile2 = appIdleFile;
                                parser2 = parser;
                            } else {
                                long bucketActiveTimeoutTime = getLongValue(parser, ATTR_BUCKET_ACTIVE_TIMEOUT_TIME, 0L);
                                long bucketWorkingSetTimeoutTime2 = getLongValue(parser, ATTR_BUCKET_WORKING_SET_TIMEOUT_TIME, 0L);
                                appIdleFile2 = appIdleFile;
                                parser2 = parser;
                                if (bucketActiveTimeoutTime == 0) {
                                    bucketWorkingSetTimeoutTime = bucketWorkingSetTimeoutTime2;
                                    if (bucketWorkingSetTimeoutTime != 0) {
                                    }
                                } else {
                                    bucketWorkingSetTimeoutTime = bucketWorkingSetTimeoutTime2;
                                }
                                insertBucketExpiryTime(appUsageHistory, 10, bucketActiveTimeoutTime);
                                insertBucketExpiryTime(appUsageHistory, 20, bucketWorkingSetTimeoutTime);
                            }
                        } else {
                            appIdleFile2 = appIdleFile;
                            parser2 = parser;
                            type3 = type4;
                        }
                    } else {
                        appIdleFile2 = appIdleFile;
                        parser2 = parser;
                        type3 = type4;
                    }
                    appIdleFile = appIdleFile2;
                    parser = parser2;
                    i = 1;
                    type2 = 2;
                }
            }
        } finally {
            libcore.io.IoUtils.closeQuietly((java.lang.AutoCloseable) null);
        }
    }

    private void readBucketExpiryTimes(org.xmlpull.v1.XmlPullParser parser, com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int depth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
            if ("item".equals(parser.getName())) {
                int bucket = getIntValue(parser, ATTR_BUCKET, -1);
                if (bucket == -1) {
                    android.util.Slog.e(TAG, "Error reading the buckets expiry times");
                } else {
                    long expiryTimeMs = getLongValue(parser, ATTR_EXPIRY_TIME, 0L);
                    insertBucketExpiryTime(appUsageHistory, bucket, expiryTimeMs);
                }
            }
        }
    }

    private void insertBucketExpiryTime(com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory, int bucket, long expiryTimeMs) {
        if (expiryTimeMs == 0) {
            return;
        }
        if (appUsageHistory.bucketExpiryTimesMs == null) {
            appUsageHistory.bucketExpiryTimesMs = new android.util.SparseLongArray();
        }
        appUsageHistory.bucketExpiryTimesMs.put(bucket, expiryTimeMs);
    }

    private long getLongValue(org.xmlpull.v1.XmlPullParser parser, java.lang.String attrName, long defValue) {
        java.lang.String value = parser.getAttributeValue(null, attrName);
        return value == null ? defValue : java.lang.Long.parseLong(value);
    }

    private int getIntValue(org.xmlpull.v1.XmlPullParser parser, java.lang.String attrName, int defValue) {
        java.lang.String value = parser.getAttributeValue(null, attrName);
        return value == null ? defValue : java.lang.Integer.parseInt(value);
    }

    public void writeAppIdleTimes(long elapsedRealtimeMs) {
        int size = this.mIdleHistory.size();
        for (int i = 0; i < size; i++) {
            writeAppIdleTimes(this.mIdleHistory.keyAt(i), elapsedRealtimeMs);
        }
    }

    public void writeAppIdleTimes(int userId, long elapsedRealtimeMs) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory;
        java.lang.String packageName;
        com.android.server.usage.AppIdleHistory.AppUsageHistory history;
        int size;
        java.io.FileOutputStream fos = null;
        android.util.AtomicFile appIdleFile = new android.util.AtomicFile(getUserFile(userId));
        try {
            fos = appIdleFile.startWrite();
            java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(fos);
            com.android.internal.util.FastXmlSerializer xml = new com.android.internal.util.FastXmlSerializer();
            xml.setOutput(bos, java.nio.charset.StandardCharsets.UTF_8.name());
            xml.startDocument((java.lang.String) null, true);
            xml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            xml.startTag((java.lang.String) null, TAG_PACKAGES);
            xml.attribute((java.lang.String) null, ATTR_VERSION, java.lang.String.valueOf(1));
            long elapsedTimeMs = getElapsedTime(elapsedRealtimeMs);
            android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory2 = getUserHistory(userId);
            int N = userHistory2.size();
            int i = 0;
            while (true) {
                java.io.BufferedOutputStream bos2 = bos;
                int N2 = N;
                if (i < N2) {
                    N = N2;
                    java.lang.String packageName2 = userHistory2.keyAt(i);
                    if (packageName2 == null) {
                        android.util.Slog.w(TAG, "Skipping App Idle write for unexpected null package");
                        userHistory = userHistory2;
                    } else {
                        com.android.server.usage.AppIdleHistory.AppUsageHistory history2 = userHistory2.valueAt(i);
                        userHistory = userHistory2;
                        xml.startTag((java.lang.String) null, "package");
                        xml.attribute((java.lang.String) null, "name", packageName2);
                        xml.attribute((java.lang.String) null, ATTR_ELAPSED_IDLE, java.lang.Long.toString(history2.lastUsedElapsedTime));
                        xml.attribute((java.lang.String) null, ATTR_LAST_USED_BY_USER_ELAPSED, java.lang.Long.toString(history2.lastUsedByUserElapsedTime));
                        xml.attribute((java.lang.String) null, ATTR_SCREEN_IDLE, java.lang.Long.toString(history2.lastUsedScreenTime));
                        xml.attribute((java.lang.String) null, ATTR_LAST_PREDICTED_TIME, java.lang.Long.toString(history2.lastPredictedTime));
                        xml.attribute((java.lang.String) null, ATTR_CURRENT_BUCKET, java.lang.Integer.toString(history2.currentBucket));
                        xml.attribute((java.lang.String) null, ATTR_BUCKETING_REASON, java.lang.Integer.toHexString(history2.bucketingReason));
                        if (history2.lastJobRunTime != Long.MIN_VALUE) {
                            xml.attribute((java.lang.String) null, ATTR_LAST_RUN_JOB_TIME, java.lang.Long.toString(history2.lastJobRunTime));
                        }
                        if (history2.lastRestrictAttemptElapsedTime > 0) {
                            xml.attribute((java.lang.String) null, ATTR_LAST_RESTRICTION_ATTEMPT_ELAPSED, java.lang.Long.toString(history2.lastRestrictAttemptElapsedTime));
                        }
                        xml.attribute((java.lang.String) null, ATTR_LAST_RESTRICTION_ATTEMPT_REASON, java.lang.Integer.toHexString(history2.lastRestrictReason));
                        if (history2.nextEstimatedLaunchTime > 0) {
                            xml.attribute((java.lang.String) null, ATTR_NEXT_ESTIMATED_APP_LAUNCH_TIME, java.lang.Long.toString(history2.nextEstimatedLaunchTime));
                        }
                        if (history2.bucketExpiryTimesMs != null) {
                            xml.startTag((java.lang.String) null, TAG_BUCKET_EXPIRY_TIMES);
                            int size2 = history2.bucketExpiryTimesMs.size();
                            int j = 0;
                            while (j < size2) {
                                long expiryTimeMs = history2.bucketExpiryTimesMs.valueAt(j);
                                if (expiryTimeMs < elapsedTimeMs) {
                                    packageName = packageName2;
                                    history = history2;
                                    size = size2;
                                } else {
                                    int bucket = history2.bucketExpiryTimesMs.keyAt(j);
                                    packageName = packageName2;
                                    xml.startTag((java.lang.String) null, "item");
                                    history = history2;
                                    size = size2;
                                    xml.attribute((java.lang.String) null, ATTR_BUCKET, java.lang.String.valueOf(bucket));
                                    xml.attribute((java.lang.String) null, ATTR_EXPIRY_TIME, java.lang.String.valueOf(expiryTimeMs));
                                    xml.endTag((java.lang.String) null, "item");
                                }
                                j++;
                                packageName2 = packageName;
                                history2 = history;
                                size2 = size;
                            }
                            xml.endTag((java.lang.String) null, TAG_BUCKET_EXPIRY_TIMES);
                        }
                        xml.endTag((java.lang.String) null, "package");
                    }
                    i++;
                    bos = bos2;
                    userHistory2 = userHistory;
                } else {
                    xml.endTag((java.lang.String) null, TAG_PACKAGES);
                    xml.endDocument();
                    appIdleFile.finishWrite(fos);
                    return;
                }
            }
        } catch (java.lang.Exception e) {
            appIdleFile.failWrite(fos);
            android.util.Slog.e(TAG, "Error writing app idle file for user " + userId, e);
        }
    }

    public void dumpUsers(android.util.IndentingPrintWriter idpw, int[] userIds, java.util.List<java.lang.String> pkgs) {
        for (int i : userIds) {
            idpw.println();
            dumpUser(idpw, i, pkgs);
        }
    }

    private void dumpUser(android.util.IndentingPrintWriter idpw, int userId, java.util.List<java.lang.String> pkgs) {
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory;
        int P;
        int p;
        int i;
        int i2 = userId;
        idpw.print("User ");
        idpw.print(userId);
        idpw.println(" App Standby States:");
        idpw.increaseIndent();
        android.util.ArrayMap<java.lang.String, com.android.server.usage.AppIdleHistory.AppUsageHistory> userHistory2 = this.mIdleHistory.get(i2);
        long now = java.lang.System.currentTimeMillis();
        long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
        long totalElapsedTime = getElapsedTime(elapsedRealtime);
        getScreenOnTime(elapsedRealtime);
        if (userHistory2 == null) {
            return;
        }
        int P2 = userHistory2.size();
        int p2 = 0;
        while (p2 < P2) {
            java.lang.String packageName = userHistory2.keyAt(p2);
            com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory = userHistory2.valueAt(p2);
            if (!com.android.internal.util.CollectionUtils.isEmpty(pkgs) && !pkgs.contains(packageName)) {
                P = P2;
                p = p2;
                i = i2;
                userHistory = userHistory2;
            } else {
                idpw.print("package=" + packageName);
                idpw.print(" u=" + i2);
                idpw.print(" bucket=" + appUsageHistory.currentBucket + " reason=" + android.app.usage.UsageStatsManager.reasonToString(appUsageHistory.bucketingReason));
                idpw.print(" used=");
                userHistory = userHistory2;
                P = P2;
                p = p2;
                printLastActionElapsedTime(idpw, totalElapsedTime, appUsageHistory.lastUsedElapsedTime);
                idpw.print(" usedByUser=");
                printLastActionElapsedTime(idpw, totalElapsedTime, appUsageHistory.lastUsedByUserElapsedTime);
                idpw.print(" usedScr=");
                printLastActionElapsedTime(idpw, totalElapsedTime, appUsageHistory.lastUsedScreenTime);
                idpw.print(" lastPred=");
                printLastActionElapsedTime(idpw, totalElapsedTime, appUsageHistory.lastPredictedTime);
                dumpBucketExpiryTimes(idpw, appUsageHistory, totalElapsedTime);
                idpw.print(" lastJob=");
                android.util.TimeUtils.formatDuration(totalElapsedTime - appUsageHistory.lastJobRunTime, idpw);
                idpw.print(" lastInformedBucket=" + appUsageHistory.lastInformedBucket);
                if (appUsageHistory.lastRestrictAttemptElapsedTime > 0) {
                    idpw.print(" lastRestrictAttempt=");
                    android.util.TimeUtils.formatDuration(totalElapsedTime - appUsageHistory.lastRestrictAttemptElapsedTime, idpw);
                    idpw.print(" lastRestrictReason=" + android.app.usage.UsageStatsManager.reasonToString(appUsageHistory.lastRestrictReason));
                }
                if (appUsageHistory.nextEstimatedLaunchTime > 0) {
                    idpw.print(" nextEstimatedLaunchTime=");
                    android.util.TimeUtils.formatDuration(appUsageHistory.nextEstimatedLaunchTime - now, idpw);
                }
                i = userId;
                idpw.print(" idle=" + (isIdle(packageName, i, elapsedRealtime) ? "y" : "n"));
                idpw.println();
            }
            p2 = p + 1;
            i2 = i;
            userHistory2 = userHistory;
            P2 = P;
        }
        idpw.println();
        idpw.print("totalElapsedTime=");
        android.util.TimeUtils.formatDuration(getElapsedTime(elapsedRealtime), idpw);
        idpw.println();
        idpw.print("totalScreenOnTime=");
        android.util.TimeUtils.formatDuration(getScreenOnTime(elapsedRealtime), idpw);
        idpw.println();
        idpw.decreaseIndent();
    }

    private void printLastActionElapsedTime(android.util.IndentingPrintWriter idpw, long totalElapsedTimeMS, long lastActionTimeMs) {
        if (lastActionTimeMs < 0) {
            idpw.print("<uninitialized>");
        } else {
            android.util.TimeUtils.formatDuration(totalElapsedTimeMS - lastActionTimeMs, idpw);
        }
    }

    private void dumpBucketExpiryTimes(android.util.IndentingPrintWriter idpw, com.android.server.usage.AppIdleHistory.AppUsageHistory appUsageHistory, long totalElapsedTimeMs) {
        idpw.print(" expiryTimes=");
        if (appUsageHistory.bucketExpiryTimesMs == null || appUsageHistory.bucketExpiryTimesMs.size() == 0) {
            idpw.print("<none>");
            return;
        }
        idpw.print("(");
        int size = appUsageHistory.bucketExpiryTimesMs.size();
        for (int i = 0; i < size; i++) {
            int bucket = appUsageHistory.bucketExpiryTimesMs.keyAt(i);
            long expiryTimeMs = appUsageHistory.bucketExpiryTimesMs.valueAt(i);
            if (i != 0) {
                idpw.print(",");
            }
            idpw.print(bucket + ":");
            android.util.TimeUtils.formatDuration(totalElapsedTimeMs - expiryTimeMs, idpw);
        }
        idpw.print(")");
    }
}
