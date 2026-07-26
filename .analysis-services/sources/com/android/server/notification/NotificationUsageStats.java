package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationUsageStats {
    private static final boolean DEBUG = false;
    private static final java.lang.String DEVICE_GLOBAL_STATS = "__global";
    private static final long EMIT_PERIOD = 14400000;
    private static final com.android.server.notification.NotificationUsageStats.AggregatedStats[] EMPTY_AGGREGATED_STATS = new com.android.server.notification.NotificationUsageStats.AggregatedStats[0];
    private static final boolean ENABLE_AGGREGATED_IN_MEMORY_STATS = true;
    public static final int FOUR_HOURS = 14400000;
    private static final int MSG_EMIT = 1;
    private static final java.lang.String TAG = "NotificationUsageStats";
    public static final int TEN_SECONDS = 10000;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final java.util.Map<java.lang.String, com.android.server.notification.NotificationUsageStats.AggregatedStats> mStats = new java.util.HashMap();
    private final java.util.ArrayDeque<com.android.server.notification.NotificationUsageStats.AggregatedStats[]> mStatsArrays = new java.util.ArrayDeque<>();
    private android.util.ArraySet<java.lang.String> mStatExpiredkeys = new android.util.ArraySet<>();
    private long mLastEmitTime = android.os.SystemClock.elapsedRealtime();

    public NotificationUsageStats(android.content.Context context) {
        this.mContext = context;
        this.mHandler = new android.os.Handler(this.mContext.getMainLooper()) { // from class: com.android.server.notification.NotificationUsageStats.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        com.android.server.notification.NotificationUsageStats.this.emit();
                        break;
                    default:
                        android.util.Log.wtf(com.android.server.notification.NotificationUsageStats.TAG, "Unknown message type: " + msg.what);
                        break;
                }
            }
        };
        this.mHandler.sendEmptyMessageDelayed(1, 14400000L);
    }

    public synchronized float getAppEnqueueRate(java.lang.String packageName) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats stats;
        stats = getOrCreateAggregatedStatsLocked(packageName);
        return stats.getEnqueueRate(android.os.SystemClock.elapsedRealtime());
    }

    public synchronized boolean isAlertRateLimited(java.lang.String packageName) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats stats;
        stats = getOrCreateAggregatedStatsLocked(packageName);
        return stats.isAlertRateLimited();
    }

    public synchronized void registerEnqueuedByApp(java.lang.String packageName) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(packageName);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            stats.numEnqueuedByApp++;
        }
        releaseAggregatedStatsLocked(aggregatedStatsArray);
    }

    public synchronized void registerEnqueuedByAppAndAccepted(java.lang.String packageName) {
        long now = android.os.SystemClock.elapsedRealtime();
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(packageName);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            stats.updateInterarrivalEstimate(now);
        }
        releaseAggregatedStatsLocked(aggregatedStatsArray);
    }

    public synchronized void registerPostedByApp(com.android.server.notification.NotificationRecord notification) {
        notification.stats.posttimeElapsedMs = android.os.SystemClock.elapsedRealtime();
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(notification);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            int i = 1;
            stats.numPostedByApp++;
            stats.countApiUse(notification);
            int i2 = stats.numUndecoratedRemoteViews;
            if (!notification.hasUndecoratedRemoteView()) {
                i = 0;
            }
            stats.numUndecoratedRemoteViews = i2 + i;
        }
        releaseAggregatedStatsLocked(aggregatedStatsArray);
    }

    public synchronized void registerUpdatedByApp(com.android.server.notification.NotificationRecord notification, com.android.server.notification.NotificationRecord old) {
        notification.stats.updateFrom(old.stats);
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(notification);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            stats.numUpdatedByApp++;
            stats.countApiUse(notification);
        }
        releaseAggregatedStatsLocked(aggregatedStatsArray);
    }

    public synchronized void registerRemovedByApp(com.android.server.notification.NotificationRecord notification) {
        notification.stats.onRemoved();
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(notification);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            stats.numRemovedByApp++;
        }
        releaseAggregatedStatsLocked(aggregatedStatsArray);
    }

    public synchronized void registerDismissedByUser(com.android.server.notification.NotificationRecord notification) {
        com.android.internal.logging.MetricsLogger.histogram(this.mContext, "note_dismiss_longevity", ((int) (java.lang.System.currentTimeMillis() - notification.getRankingTimeMs())) / 60000);
        notification.stats.onDismiss();
    }

    public synchronized void registerClickedByUser(com.android.server.notification.NotificationRecord notification) {
        com.android.internal.logging.MetricsLogger.histogram(this.mContext, "note_click_longevity", ((int) (java.lang.System.currentTimeMillis() - notification.getRankingTimeMs())) / 60000);
        notification.stats.onClick();
    }

    public synchronized void registerPeopleAffinity(com.android.server.notification.NotificationRecord notification, boolean valid, boolean starred, boolean cached) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(notification);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            if (valid) {
                stats.numWithValidPeople++;
            }
            if (starred) {
                stats.numWithStaredPeople++;
            }
            if (cached) {
                stats.numPeopleCacheHit++;
            } else {
                stats.numPeopleCacheMiss++;
            }
        }
        releaseAggregatedStatsLocked(aggregatedStatsArray);
    }

    public synchronized void registerBlocked(com.android.server.notification.NotificationRecord notification) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(notification);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            stats.numBlocked++;
        }
        releaseAggregatedStatsLocked(aggregatedStatsArray);
    }

    public synchronized void registerSuspendedByAdmin(com.android.server.notification.NotificationRecord notification) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(notification);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            stats.numSuspendedByAdmin++;
        }
        releaseAggregatedStatsLocked(aggregatedStatsArray);
    }

    public synchronized void registerOverRateQuota(java.lang.String packageName) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(packageName);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            stats.numRateViolations++;
        }
    }

    public synchronized void registerOverCountQuota(java.lang.String packageName) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(packageName);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            stats.numQuotaViolations++;
        }
    }

    public synchronized void registerImageRemoved(java.lang.String packageName) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(packageName);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            stats.numImagesRemoved++;
        }
    }

    public synchronized void registerTooOldBlocked(com.android.server.notification.NotificationRecord notification) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] aggregatedStatsArray = getAggregatedStatsLocked(notification);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats stats : aggregatedStatsArray) {
            stats.numTooOld++;
        }
        releaseAggregatedStatsLocked(aggregatedStatsArray);
    }

    private com.android.server.notification.NotificationUsageStats.AggregatedStats[] getAggregatedStatsLocked(com.android.server.notification.NotificationRecord record) {
        return getAggregatedStatsLocked(record.getSbn().getPackageName());
    }

    private com.android.server.notification.NotificationUsageStats.AggregatedStats[] getAggregatedStatsLocked(java.lang.String packageName) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats[] array = this.mStatsArrays.poll();
        if (array == null) {
            array = new com.android.server.notification.NotificationUsageStats.AggregatedStats[2];
        }
        array[0] = getOrCreateAggregatedStatsLocked(DEVICE_GLOBAL_STATS);
        array[1] = getOrCreateAggregatedStatsLocked(packageName);
        return array;
    }

    private void releaseAggregatedStatsLocked(com.android.server.notification.NotificationUsageStats.AggregatedStats[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
        this.mStatsArrays.offer(array);
    }

    private com.android.server.notification.NotificationUsageStats.AggregatedStats getOrCreateAggregatedStatsLocked(java.lang.String key) {
        com.android.server.notification.NotificationUsageStats.AggregatedStats result = this.mStats.get(key);
        if (result == null) {
            result = new com.android.server.notification.NotificationUsageStats.AggregatedStats(this.mContext, key);
            this.mStats.put(key, result);
        }
        result.mLastAccessTime = android.os.SystemClock.elapsedRealtime();
        return result;
    }

    public synchronized org.json.JSONObject dumpJson(com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        org.json.JSONObject dump;
        dump = new org.json.JSONObject();
        try {
            org.json.JSONArray aggregatedStats = new org.json.JSONArray();
            for (com.android.server.notification.NotificationUsageStats.AggregatedStats as : this.mStats.values()) {
                if (filter == null || filter.matches(as.key)) {
                    aggregatedStats.put(as.dumpJson());
                }
            }
            dump.put("current", aggregatedStats);
        } catch (org.json.JSONException e) {
        }
        return dump;
    }

    public com.android.server.notification.PulledStats remoteViewStats(long startMs, boolean aggregate) {
        com.android.server.notification.PulledStats stats = new com.android.server.notification.PulledStats(startMs);
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats as : this.mStats.values()) {
            if (as.numUndecoratedRemoteViews > 0) {
                stats.addUndecoratedPackage(as.key, as.mCreated);
            }
        }
        return stats;
    }

    public synchronized void dump(java.io.PrintWriter pw, java.lang.String indent, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        for (com.android.server.notification.NotificationUsageStats.AggregatedStats as : this.mStats.values()) {
            if (filter == null || filter.matches(as.key)) {
                as.dump(pw, indent);
            }
        }
        pw.println(indent + "mStatsArrays.size(): " + this.mStatsArrays.size());
        pw.println(indent + "mStats.size(): " + this.mStats.size());
    }

    public synchronized void emit() {
        com.android.server.notification.NotificationUsageStats.AggregatedStats stats = getOrCreateAggregatedStatsLocked(DEVICE_GLOBAL_STATS);
        stats.emit();
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessageDelayed(1, 14400000L);
        for (java.lang.String key : this.mStats.keySet()) {
            if (this.mStats.get(key).mLastAccessTime < this.mLastEmitTime) {
                this.mStatExpiredkeys.add(key);
            }
        }
        java.util.Iterator<java.lang.String> it = this.mStatExpiredkeys.iterator();
        while (it.hasNext()) {
            this.mStats.remove(it.next());
        }
        this.mStatExpiredkeys.clear();
        this.mLastEmitTime = android.os.SystemClock.elapsedRealtime();
    }

    private static class AggregatedStats {
        public com.android.server.notification.NotificationUsageStats.ImportanceHistogram finalImportance;
        public final java.lang.String key;
        private final android.content.Context mContext;
        public long mLastAccessTime;
        private com.android.server.notification.NotificationUsageStats.AggregatedStats mPrevious;
        public com.android.server.notification.NotificationUsageStats.ImportanceHistogram noisyImportance;
        public int numAlertViolations;
        public int numAutoCancel;
        public int numBlocked;
        public int numEnqueuedByApp;
        public int numForegroundService;
        public int numImagesRemoved;
        public int numInterrupt;
        public int numOngoing;
        public int numPeopleCacheHit;
        public int numPeopleCacheMiss;
        public int numPostedByApp;
        public int numPrivate;
        public int numQuotaViolations;
        public int numRateViolations;
        public int numRemovedByApp;
        public int numSecret;
        public int numSuspendedByAdmin;
        public int numTooOld;
        public int numUndecoratedRemoteViews;
        public int numUpdatedByApp;
        public int numUserInitiatedJob;
        public int numWithActions;
        public int numWithBigPicture;
        public int numWithBigText;
        public int numWithInbox;
        public int numWithInfoText;
        public int numWithLargeIcon;
        public int numWithMediaSession;
        public int numWithStaredPeople;
        public int numWithSubText;
        public int numWithText;
        public int numWithTitle;
        public int numWithValidPeople;
        public com.android.server.notification.NotificationUsageStats.ImportanceHistogram quietImportance;
        private final long mCreated = android.os.SystemClock.elapsedRealtime();
        public com.android.server.notification.RateEstimator enqueueRate = new com.android.server.notification.RateEstimator();
        public com.android.server.notification.AlertRateLimiter alertRate = new com.android.server.notification.AlertRateLimiter();

        public AggregatedStats(android.content.Context context, java.lang.String key) {
            this.key = key;
            this.mContext = context;
            this.noisyImportance = new com.android.server.notification.NotificationUsageStats.ImportanceHistogram(context, "note_imp_noisy_");
            this.quietImportance = new com.android.server.notification.NotificationUsageStats.ImportanceHistogram(context, "note_imp_quiet_");
            this.finalImportance = new com.android.server.notification.NotificationUsageStats.ImportanceHistogram(context, "note_importance_");
        }

        public com.android.server.notification.NotificationUsageStats.AggregatedStats getPrevious() {
            if (this.mPrevious == null) {
                this.mPrevious = new com.android.server.notification.NotificationUsageStats.AggregatedStats(this.mContext, this.key);
            }
            return this.mPrevious;
        }

        public void countApiUse(com.android.server.notification.NotificationRecord record) {
            android.app.Notification n = record.getNotification();
            if (n.actions != null) {
                this.numWithActions++;
            }
            if ((n.flags & 64) != 0) {
                this.numForegroundService++;
            }
            if ((n.flags & 32768) != 0) {
                this.numUserInitiatedJob++;
            }
            if ((n.flags & 2) != 0) {
                this.numOngoing++;
            }
            if ((n.flags & 16) != 0) {
                this.numAutoCancel++;
            }
            if ((n.defaults & 1) != 0 || (n.defaults & 2) != 0 || n.sound != null || n.vibrate != null) {
                this.numInterrupt++;
            }
            switch (n.visibility) {
                case -1:
                    this.numSecret++;
                    break;
                case 0:
                    this.numPrivate++;
                    break;
            }
            if (record.stats.isNoisy) {
                this.noisyImportance.increment(record.stats.requestedImportance);
            } else {
                this.quietImportance.increment(record.stats.requestedImportance);
            }
            this.finalImportance.increment(record.getImportance());
            java.util.Set<java.lang.String> names = n.extras.keySet();
            if (names.contains("android.bigText")) {
                this.numWithBigText++;
            }
            if (names.contains("android.picture")) {
                this.numWithBigPicture++;
            }
            if (names.contains("android.largeIcon")) {
                this.numWithLargeIcon++;
            }
            if (names.contains("android.textLines")) {
                this.numWithInbox++;
            }
            if (names.contains("android.mediaSession")) {
                this.numWithMediaSession++;
            }
            if (names.contains("android.title") && !android.text.TextUtils.isEmpty(n.extras.getCharSequence("android.title"))) {
                this.numWithTitle++;
            }
            if (names.contains("android.text") && !android.text.TextUtils.isEmpty(n.extras.getCharSequence("android.text"))) {
                this.numWithText++;
            }
            if (names.contains("android.subText") && !android.text.TextUtils.isEmpty(n.extras.getCharSequence("android.subText"))) {
                this.numWithSubText++;
            }
            if (names.contains("android.infoText") && !android.text.TextUtils.isEmpty(n.extras.getCharSequence("android.infoText"))) {
                this.numWithInfoText++;
            }
        }

        public void emit() {
            com.android.server.notification.NotificationUsageStats.AggregatedStats previous = getPrevious();
            maybeCount("note_enqueued", this.numEnqueuedByApp - previous.numEnqueuedByApp);
            maybeCount("note_post", this.numPostedByApp - previous.numPostedByApp);
            maybeCount("note_update", this.numUpdatedByApp - previous.numUpdatedByApp);
            maybeCount("note_remove", this.numRemovedByApp - previous.numRemovedByApp);
            maybeCount("note_with_people", this.numWithValidPeople - previous.numWithValidPeople);
            maybeCount("note_with_stars", this.numWithStaredPeople - previous.numWithStaredPeople);
            maybeCount("people_cache_hit", this.numPeopleCacheHit - previous.numPeopleCacheHit);
            maybeCount("people_cache_miss", this.numPeopleCacheMiss - previous.numPeopleCacheMiss);
            maybeCount("note_blocked", this.numBlocked - previous.numBlocked);
            maybeCount("note_suspended", this.numSuspendedByAdmin - previous.numSuspendedByAdmin);
            maybeCount("note_with_actions", this.numWithActions - previous.numWithActions);
            maybeCount("note_private", this.numPrivate - previous.numPrivate);
            maybeCount("note_secret", this.numSecret - previous.numSecret);
            maybeCount("note_interupt", this.numInterrupt - previous.numInterrupt);
            maybeCount("note_big_text", this.numWithBigText - previous.numWithBigText);
            maybeCount("note_big_pic", this.numWithBigPicture - previous.numWithBigPicture);
            maybeCount("note_fg", this.numForegroundService - previous.numForegroundService);
            maybeCount("note_uij", this.numUserInitiatedJob - previous.numUserInitiatedJob);
            maybeCount("note_ongoing", this.numOngoing - previous.numOngoing);
            maybeCount("note_auto", this.numAutoCancel - previous.numAutoCancel);
            maybeCount("note_large_icon", this.numWithLargeIcon - previous.numWithLargeIcon);
            maybeCount("note_inbox", this.numWithInbox - previous.numWithInbox);
            maybeCount("note_media", this.numWithMediaSession - previous.numWithMediaSession);
            maybeCount("note_title", this.numWithTitle - previous.numWithTitle);
            maybeCount("note_text", this.numWithText - previous.numWithText);
            maybeCount("note_sub_text", this.numWithSubText - previous.numWithSubText);
            maybeCount("note_info_text", this.numWithInfoText - previous.numWithInfoText);
            maybeCount("note_over_rate", this.numRateViolations - previous.numRateViolations);
            maybeCount("note_over_alert_rate", this.numAlertViolations - previous.numAlertViolations);
            maybeCount("note_over_quota", this.numQuotaViolations - previous.numQuotaViolations);
            maybeCount("note_images_removed", this.numImagesRemoved - previous.numImagesRemoved);
            maybeCount("not_too_old", this.numTooOld - previous.numTooOld);
            this.noisyImportance.maybeCount(previous.noisyImportance);
            this.quietImportance.maybeCount(previous.quietImportance);
            this.finalImportance.maybeCount(previous.finalImportance);
            previous.numEnqueuedByApp = this.numEnqueuedByApp;
            previous.numPostedByApp = this.numPostedByApp;
            previous.numUpdatedByApp = this.numUpdatedByApp;
            previous.numRemovedByApp = this.numRemovedByApp;
            previous.numPeopleCacheHit = this.numPeopleCacheHit;
            previous.numPeopleCacheMiss = this.numPeopleCacheMiss;
            previous.numWithStaredPeople = this.numWithStaredPeople;
            previous.numWithValidPeople = this.numWithValidPeople;
            previous.numBlocked = this.numBlocked;
            previous.numSuspendedByAdmin = this.numSuspendedByAdmin;
            previous.numWithActions = this.numWithActions;
            previous.numPrivate = this.numPrivate;
            previous.numSecret = this.numSecret;
            previous.numInterrupt = this.numInterrupt;
            previous.numWithBigText = this.numWithBigText;
            previous.numWithBigPicture = this.numWithBigPicture;
            previous.numForegroundService = this.numForegroundService;
            previous.numUserInitiatedJob = this.numUserInitiatedJob;
            previous.numOngoing = this.numOngoing;
            previous.numAutoCancel = this.numAutoCancel;
            previous.numWithLargeIcon = this.numWithLargeIcon;
            previous.numWithInbox = this.numWithInbox;
            previous.numWithMediaSession = this.numWithMediaSession;
            previous.numWithTitle = this.numWithTitle;
            previous.numWithText = this.numWithText;
            previous.numWithSubText = this.numWithSubText;
            previous.numWithInfoText = this.numWithInfoText;
            previous.numRateViolations = this.numRateViolations;
            previous.numAlertViolations = this.numAlertViolations;
            previous.numQuotaViolations = this.numQuotaViolations;
            previous.numImagesRemoved = this.numImagesRemoved;
            previous.numTooOld = this.numTooOld;
            this.noisyImportance.update(previous.noisyImportance);
            this.quietImportance.update(previous.quietImportance);
            this.finalImportance.update(previous.finalImportance);
        }

        void maybeCount(java.lang.String name, int value) {
            if (value > 0) {
                com.android.internal.logging.MetricsLogger.count(this.mContext, name, value);
            }
        }

        public void dump(java.io.PrintWriter pw, java.lang.String indent) {
            pw.println(toStringWithIndent(indent));
        }

        public java.lang.String toString() {
            return toStringWithIndent("");
        }

        public float getEnqueueRate() {
            return getEnqueueRate(android.os.SystemClock.elapsedRealtime());
        }

        public float getEnqueueRate(long now) {
            return this.enqueueRate.getRate(now);
        }

        public void updateInterarrivalEstimate(long now) {
            this.enqueueRate.update(now);
        }

        public boolean isAlertRateLimited() {
            boolean limited = this.alertRate.shouldRateLimitAlert(android.os.SystemClock.elapsedRealtime());
            if (limited) {
                this.numAlertViolations++;
            }
            return limited;
        }

        private java.lang.String toStringWithIndent(java.lang.String indent) {
            java.lang.StringBuilder output = new java.lang.StringBuilder();
            output.append(indent).append("AggregatedStats{\n");
            java.lang.String indentPlusTwo = indent + "  ";
            output.append(indentPlusTwo);
            output.append("key='").append(this.key).append("',\n");
            output.append(indentPlusTwo);
            output.append("numEnqueuedByApp=").append(this.numEnqueuedByApp).append(",\n");
            output.append(indentPlusTwo);
            output.append("numPostedByApp=").append(this.numPostedByApp).append(",\n");
            output.append(indentPlusTwo);
            output.append("numUpdatedByApp=").append(this.numUpdatedByApp).append(",\n");
            output.append(indentPlusTwo);
            output.append("numRemovedByApp=").append(this.numRemovedByApp).append(",\n");
            output.append(indentPlusTwo);
            output.append("numPeopleCacheHit=").append(this.numPeopleCacheHit).append(",\n");
            output.append(indentPlusTwo);
            output.append("numWithStaredPeople=").append(this.numWithStaredPeople).append(",\n");
            output.append(indentPlusTwo);
            output.append("numWithValidPeople=").append(this.numWithValidPeople).append(",\n");
            output.append(indentPlusTwo);
            output.append("numPeopleCacheMiss=").append(this.numPeopleCacheMiss).append(",\n");
            output.append(indentPlusTwo);
            output.append("numBlocked=").append(this.numBlocked).append(",\n");
            output.append(indentPlusTwo);
            output.append("numSuspendedByAdmin=").append(this.numSuspendedByAdmin).append(",\n");
            output.append(indentPlusTwo);
            output.append("numWithActions=").append(this.numWithActions).append(",\n");
            output.append(indentPlusTwo);
            output.append("numPrivate=").append(this.numPrivate).append(",\n");
            output.append(indentPlusTwo);
            output.append("numSecret=").append(this.numSecret).append(",\n");
            output.append(indentPlusTwo);
            output.append("numInterrupt=").append(this.numInterrupt).append(",\n");
            output.append(indentPlusTwo);
            output.append("numWithBigText=").append(this.numWithBigText).append(",\n");
            output.append(indentPlusTwo);
            output.append("numWithBigPicture=").append(this.numWithBigPicture).append("\n");
            output.append(indentPlusTwo);
            output.append("numForegroundService=").append(this.numForegroundService).append("\n");
            output.append(indentPlusTwo);
            output.append("numUserInitiatedJob=").append(this.numUserInitiatedJob).append("\n");
            output.append(indentPlusTwo);
            output.append("numOngoing=").append(this.numOngoing).append("\n");
            output.append(indentPlusTwo);
            output.append("numAutoCancel=").append(this.numAutoCancel).append("\n");
            output.append(indentPlusTwo);
            output.append("numWithLargeIcon=").append(this.numWithLargeIcon).append("\n");
            output.append(indentPlusTwo);
            output.append("numWithInbox=").append(this.numWithInbox).append("\n");
            output.append(indentPlusTwo);
            output.append("numWithMediaSession=").append(this.numWithMediaSession).append("\n");
            output.append(indentPlusTwo);
            output.append("numWithTitle=").append(this.numWithTitle).append("\n");
            output.append(indentPlusTwo);
            output.append("numWithText=").append(this.numWithText).append("\n");
            output.append(indentPlusTwo);
            output.append("numWithSubText=").append(this.numWithSubText).append("\n");
            output.append(indentPlusTwo);
            output.append("numWithInfoText=").append(this.numWithInfoText).append("\n");
            output.append(indentPlusTwo);
            output.append("numRateViolations=").append(this.numRateViolations).append("\n");
            output.append(indentPlusTwo);
            output.append("numAlertViolations=").append(this.numAlertViolations).append("\n");
            output.append(indentPlusTwo);
            output.append("numQuotaViolations=").append(this.numQuotaViolations).append("\n");
            output.append(indentPlusTwo);
            output.append("numImagesRemoved=").append(this.numImagesRemoved).append("\n");
            output.append(indentPlusTwo);
            output.append("numTooOld=").append(this.numTooOld).append("\n");
            output.append(indentPlusTwo).append(this.noisyImportance.toString()).append("\n");
            output.append(indentPlusTwo).append(this.quietImportance.toString()).append("\n");
            output.append(indentPlusTwo).append(this.finalImportance.toString()).append("\n");
            output.append(indentPlusTwo);
            output.append("numUndecorateRVs=").append(this.numUndecoratedRemoteViews).append("\n");
            output.append(indent).append("}");
            return output.toString();
        }

        public org.json.JSONObject dumpJson() throws org.json.JSONException {
            com.android.server.notification.NotificationUsageStats.AggregatedStats previous = getPrevious();
            org.json.JSONObject dump = new org.json.JSONObject();
            dump.put("key", this.key);
            dump.put("duration", android.os.SystemClock.elapsedRealtime() - this.mCreated);
            maybePut(dump, "numEnqueuedByApp", this.numEnqueuedByApp);
            maybePut(dump, "numPostedByApp", this.numPostedByApp);
            maybePut(dump, "numUpdatedByApp", this.numUpdatedByApp);
            maybePut(dump, "numRemovedByApp", this.numRemovedByApp);
            maybePut(dump, "numPeopleCacheHit", this.numPeopleCacheHit);
            maybePut(dump, "numPeopleCacheMiss", this.numPeopleCacheMiss);
            maybePut(dump, "numWithStaredPeople", this.numWithStaredPeople);
            maybePut(dump, "numWithValidPeople", this.numWithValidPeople);
            maybePut(dump, "numBlocked", this.numBlocked);
            maybePut(dump, "numSuspendedByAdmin", this.numSuspendedByAdmin);
            maybePut(dump, "numWithActions", this.numWithActions);
            maybePut(dump, "numPrivate", this.numPrivate);
            maybePut(dump, "numSecret", this.numSecret);
            maybePut(dump, "numInterrupt", this.numInterrupt);
            maybePut(dump, "numWithBigText", this.numWithBigText);
            maybePut(dump, "numWithBigPicture", this.numWithBigPicture);
            maybePut(dump, "numForegroundService", this.numForegroundService);
            maybePut(dump, "numUserInitiatedJob", this.numUserInitiatedJob);
            maybePut(dump, "numOngoing", this.numOngoing);
            maybePut(dump, "numAutoCancel", this.numAutoCancel);
            maybePut(dump, "numWithLargeIcon", this.numWithLargeIcon);
            maybePut(dump, "numWithInbox", this.numWithInbox);
            maybePut(dump, "numWithMediaSession", this.numWithMediaSession);
            maybePut(dump, "numWithTitle", this.numWithTitle);
            maybePut(dump, "numWithText", this.numWithText);
            maybePut(dump, "numWithSubText", this.numWithSubText);
            maybePut(dump, "numWithInfoText", this.numWithInfoText);
            maybePut(dump, "numRateViolations", this.numRateViolations);
            maybePut(dump, "numQuotaLViolations", this.numQuotaViolations);
            maybePut(dump, "notificationEnqueueRate", getEnqueueRate());
            maybePut(dump, "numAlertViolations", this.numAlertViolations);
            maybePut(dump, "numImagesRemoved", this.numImagesRemoved);
            maybePut(dump, "numTooOld", this.numTooOld);
            this.noisyImportance.maybePut(dump, previous.noisyImportance);
            this.quietImportance.maybePut(dump, previous.quietImportance);
            this.finalImportance.maybePut(dump, previous.finalImportance);
            return dump;
        }

        private void maybePut(org.json.JSONObject dump, java.lang.String name, int value) throws org.json.JSONException {
            if (value > 0) {
                dump.put(name, value);
            }
        }

        private void maybePut(org.json.JSONObject dump, java.lang.String name, float value) throws org.json.JSONException {
            if (value > 0.0d) {
                dump.put(name, value);
            }
        }
    }

    private static class ImportanceHistogram {
        private static final java.lang.String[] IMPORTANCE_NAMES = {"none", "min", "low", "default", "high", "max"};
        private static final int NUM_IMPORTANCES = 6;
        private final android.content.Context mContext;
        private int[] mCount = new int[6];
        private final java.lang.String[] mCounterNames = new java.lang.String[6];
        private final java.lang.String mPrefix;

        ImportanceHistogram(android.content.Context context, java.lang.String prefix) {
            this.mContext = context;
            this.mPrefix = prefix;
            for (int i = 0; i < 6; i++) {
                this.mCounterNames[i] = this.mPrefix + IMPORTANCE_NAMES[i];
            }
        }

        void increment(int imp) {
            int imp2 = java.lang.Math.max(0, java.lang.Math.min(imp, this.mCount.length - 1));
            int[] iArr = this.mCount;
            iArr[imp2] = iArr[imp2] + 1;
        }

        void maybeCount(com.android.server.notification.NotificationUsageStats.ImportanceHistogram prev) {
            for (int i = 0; i < 6; i++) {
                int value = this.mCount[i] - prev.mCount[i];
                if (value > 0) {
                    com.android.internal.logging.MetricsLogger.count(this.mContext, this.mCounterNames[i], value);
                }
            }
        }

        void update(com.android.server.notification.NotificationUsageStats.ImportanceHistogram that) {
            for (int i = 0; i < 6; i++) {
                this.mCount[i] = that.mCount[i];
            }
        }

        public void maybePut(org.json.JSONObject dump, com.android.server.notification.NotificationUsageStats.ImportanceHistogram prev) throws org.json.JSONException {
            dump.put(this.mPrefix, new org.json.JSONArray(this.mCount));
        }

        public java.lang.String toString() {
            java.lang.StringBuilder output = new java.lang.StringBuilder();
            output.append(this.mPrefix).append(": [");
            for (int i = 0; i < 6; i++) {
                output.append(this.mCount[i]);
                if (i < 5) {
                    output.append(", ");
                }
            }
            output.append("]");
            return output.toString();
        }
    }

    public static class SingleNotificationStats {
        public boolean isNoisy;
        public int naturalImportance;
        public int requestedImportance;
        private boolean isVisible = false;
        private boolean isExpanded = false;
        public long posttimeElapsedMs = -1;
        public long posttimeToFirstClickMs = -1;
        public long posttimeToDismissMs = -1;
        public long airtimeCount = 0;
        public long posttimeToFirstAirtimeMs = -1;
        public long currentAirtimeStartElapsedMs = -1;
        public long airtimeMs = 0;
        public long posttimeToFirstVisibleExpansionMs = -1;
        public long currentAirtimeExpandedStartElapsedMs = -1;
        public long airtimeExpandedMs = 0;
        public long userExpansionCount = 0;

        public long getCurrentPosttimeMs() {
            if (this.posttimeElapsedMs < 0) {
                return 0L;
            }
            return android.os.SystemClock.elapsedRealtime() - this.posttimeElapsedMs;
        }

        public long getCurrentAirtimeMs() {
            long result = this.airtimeMs;
            if (this.currentAirtimeStartElapsedMs >= 0) {
                return result + (android.os.SystemClock.elapsedRealtime() - this.currentAirtimeStartElapsedMs);
            }
            return result;
        }

        public long getCurrentAirtimeExpandedMs() {
            long result = this.airtimeExpandedMs;
            if (this.currentAirtimeExpandedStartElapsedMs >= 0) {
                return result + (android.os.SystemClock.elapsedRealtime() - this.currentAirtimeExpandedStartElapsedMs);
            }
            return result;
        }

        public void onClick() {
            if (this.posttimeToFirstClickMs < 0) {
                this.posttimeToFirstClickMs = android.os.SystemClock.elapsedRealtime() - this.posttimeElapsedMs;
            }
        }

        public void onDismiss() {
            if (this.posttimeToDismissMs < 0) {
                this.posttimeToDismissMs = android.os.SystemClock.elapsedRealtime() - this.posttimeElapsedMs;
            }
            finish();
        }

        public void onCancel() {
            finish();
        }

        public void onRemoved() {
            finish();
        }

        public void onVisibilityChanged(boolean visible) {
            long elapsedNowMs = android.os.SystemClock.elapsedRealtime();
            boolean wasVisible = this.isVisible;
            this.isVisible = visible;
            if (visible) {
                if (this.currentAirtimeStartElapsedMs < 0) {
                    this.airtimeCount++;
                    this.currentAirtimeStartElapsedMs = elapsedNowMs;
                }
                if (this.posttimeToFirstAirtimeMs < 0) {
                    this.posttimeToFirstAirtimeMs = elapsedNowMs - this.posttimeElapsedMs;
                }
            } else if (this.currentAirtimeStartElapsedMs >= 0) {
                this.airtimeMs += elapsedNowMs - this.currentAirtimeStartElapsedMs;
                this.currentAirtimeStartElapsedMs = -1L;
            }
            if (wasVisible != this.isVisible) {
                updateVisiblyExpandedStats();
            }
        }

        public void onExpansionChanged(boolean userAction, boolean expanded) {
            this.isExpanded = expanded;
            if (this.isExpanded && userAction) {
                this.userExpansionCount++;
            }
            updateVisiblyExpandedStats();
        }

        public boolean hasBeenVisiblyExpanded() {
            return this.posttimeToFirstVisibleExpansionMs >= 0;
        }

        private void updateVisiblyExpandedStats() {
            long elapsedNowMs = android.os.SystemClock.elapsedRealtime();
            if (!this.isExpanded || !this.isVisible) {
                if (this.currentAirtimeExpandedStartElapsedMs >= 0) {
                    this.airtimeExpandedMs += elapsedNowMs - this.currentAirtimeExpandedStartElapsedMs;
                    this.currentAirtimeExpandedStartElapsedMs = -1L;
                    return;
                }
                return;
            }
            if (this.currentAirtimeExpandedStartElapsedMs < 0) {
                this.currentAirtimeExpandedStartElapsedMs = elapsedNowMs;
            }
            if (this.posttimeToFirstVisibleExpansionMs < 0) {
                this.posttimeToFirstVisibleExpansionMs = elapsedNowMs - this.posttimeElapsedMs;
            }
        }

        public void finish() {
            onVisibilityChanged(false);
        }

        public java.lang.String toString() {
            java.lang.StringBuilder output = new java.lang.StringBuilder();
            output.append("SingleNotificationStats{");
            output.append("posttimeElapsedMs=").append(this.posttimeElapsedMs).append(", ");
            output.append("posttimeToFirstClickMs=").append(this.posttimeToFirstClickMs).append(", ");
            output.append("posttimeToDismissMs=").append(this.posttimeToDismissMs).append(", ");
            output.append("airtimeCount=").append(this.airtimeCount).append(", ");
            output.append("airtimeMs=").append(this.airtimeMs).append(", ");
            output.append("currentAirtimeStartElapsedMs=").append(this.currentAirtimeStartElapsedMs).append(", ");
            output.append("airtimeExpandedMs=").append(this.airtimeExpandedMs).append(", ");
            output.append("posttimeToFirstVisibleExpansionMs=").append(this.posttimeToFirstVisibleExpansionMs).append(", ");
            output.append("currentAirtimeExpandedStartElapsedMs=").append(this.currentAirtimeExpandedStartElapsedMs).append(", ");
            output.append("requestedImportance=").append(this.requestedImportance).append(", ");
            output.append("naturalImportance=").append(this.naturalImportance).append(", ");
            output.append("isNoisy=").append(this.isNoisy);
            output.append('}');
            return output.toString();
        }

        public void updateFrom(com.android.server.notification.NotificationUsageStats.SingleNotificationStats old) {
            this.posttimeElapsedMs = old.posttimeElapsedMs;
            this.posttimeToFirstClickMs = old.posttimeToFirstClickMs;
            this.airtimeCount = old.airtimeCount;
            this.posttimeToFirstAirtimeMs = old.posttimeToFirstAirtimeMs;
            this.currentAirtimeStartElapsedMs = old.currentAirtimeStartElapsedMs;
            this.airtimeMs = old.airtimeMs;
            this.posttimeToFirstVisibleExpansionMs = old.posttimeToFirstVisibleExpansionMs;
            this.currentAirtimeExpandedStartElapsedMs = old.currentAirtimeExpandedStartElapsedMs;
            this.airtimeExpandedMs = old.airtimeExpandedMs;
            this.userExpansionCount = old.userExpansionCount;
        }
    }

    public static class Aggregate {
        double avg;
        long numSamples;
        double sum2;
        double var;

        public void addSample(long sample) {
            this.numSamples++;
            double n = this.numSamples;
            double delta = sample - this.avg;
            this.avg += (1.0d / n) * delta;
            this.sum2 += ((n - 1.0d) / n) * delta * delta;
            double divisor = this.numSamples != 1 ? n - 1.0d : 1.0d;
            this.var = this.sum2 / divisor;
        }

        public java.lang.String toString() {
            return "Aggregate{numSamples=" + this.numSamples + ", avg=" + this.avg + ", var=" + this.var + '}';
        }
    }
}
