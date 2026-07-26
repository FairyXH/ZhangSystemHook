package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
class UserUsageStatsService {
    private static final java.lang.String TAG = "UsageStatsService";
    private static final int sDateFormatFlags = 131093;
    private final android.content.Context mContext;
    private final com.android.server.usage.UsageStatsDatabase mDatabase;
    private java.lang.String mLastBackgroundedPackage;
    private final com.android.server.usage.UserUsageStatsService.StatsUpdatedListener mListener;
    private final java.lang.String mLogPrefix;
    private final int mUserId;
    private static final boolean DEBUG = com.android.server.usage.UsageStatsService.DEBUG;
    private static final java.text.SimpleDateFormat sDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final long[] INTERVAL_LENGTH = {86400000, com.android.server.usage.UnixCalendar.WEEK_IN_MILLIS, com.android.server.usage.UnixCalendar.MONTH_IN_MILLIS, 31536000000L};
    private static final com.android.server.usage.UsageStatsDatabase.StatCombiner<android.app.usage.UsageStats> sUsageStatsCombiner = new com.android.server.usage.UsageStatsDatabase.StatCombiner<android.app.usage.UsageStats>() { // from class: com.android.server.usage.UserUsageStatsService.1
        @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
        public boolean combine(com.android.server.usage.IntervalStats stats, boolean mutable, java.util.List<android.app.usage.UsageStats> accResult) {
            if (!mutable) {
                accResult.addAll(stats.packageStats.values());
                return true;
            }
            int statCount = stats.packageStats.size();
            for (int i = 0; i < statCount; i++) {
                accResult.add(new android.app.usage.UsageStats(stats.packageStats.valueAt(i)));
            }
            return true;
        }
    };
    private static final com.android.server.usage.UsageStatsDatabase.StatCombiner<android.app.usage.ConfigurationStats> sConfigStatsCombiner = new com.android.server.usage.UsageStatsDatabase.StatCombiner<android.app.usage.ConfigurationStats>() { // from class: com.android.server.usage.UserUsageStatsService.2
        @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
        public boolean combine(com.android.server.usage.IntervalStats stats, boolean mutable, java.util.List<android.app.usage.ConfigurationStats> accResult) {
            if (!mutable) {
                accResult.addAll(stats.configurations.values());
                return true;
            }
            int configCount = stats.configurations.size();
            for (int i = 0; i < configCount; i++) {
                accResult.add(new android.app.usage.ConfigurationStats(stats.configurations.valueAt(i)));
            }
            return true;
        }
    };
    private static final com.android.server.usage.UsageStatsDatabase.StatCombiner<android.app.usage.EventStats> sEventStatsCombiner = new com.android.server.usage.UsageStatsDatabase.StatCombiner<android.app.usage.EventStats>() { // from class: com.android.server.usage.UserUsageStatsService.3
        @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
        public boolean combine(com.android.server.usage.IntervalStats stats, boolean mutable, java.util.List<android.app.usage.EventStats> accResult) {
            stats.addEventStatsTo(accResult);
            return true;
        }
    };
    private boolean mStatsChanged = false;
    private final android.util.SparseArrayMap<java.lang.String, com.android.server.usage.UserUsageStatsService.CachedEarlyEvents> mCachedEarlyEvents = new android.util.SparseArrayMap<>();
    private final com.android.server.usage.UnixCalendar mDailyExpiryDate = new com.android.server.usage.UnixCalendar(0);
    private final com.android.server.usage.IntervalStats[] mCurrentStats = new com.android.server.usage.IntervalStats[4];
    private long mRealTimeSnapshot = android.os.SystemClock.elapsedRealtime();
    private long mSystemTimeSnapshot = java.lang.System.currentTimeMillis();

    interface StatsUpdatedListener {
        void onNewUpdate(int i);

        void onStatsReloaded();

        void onStatsUpdated();
    }

    private static final class CachedEarlyEvents {
        public long eventTime;
        public java.util.List<android.app.usage.UsageEvents.Event> events;
        public long searchBeginTime;

        private CachedEarlyEvents() {
        }
    }

    UserUsageStatsService(android.content.Context context, int userId, java.io.File usageStatsDir, com.android.server.usage.UserUsageStatsService.StatsUpdatedListener listener) {
        this.mContext = context;
        this.mDatabase = new com.android.server.usage.UsageStatsDatabase(usageStatsDir);
        this.mListener = listener;
        this.mLogPrefix = "User[" + java.lang.Integer.toString(userId) + "] ";
        this.mUserId = userId;
    }

    void init(long currentTimeMillis, java.util.HashMap<java.lang.String, java.lang.Long> installedPackages, boolean deleteObsoleteData) {
        readPackageMappingsLocked(installedPackages, deleteObsoleteData);
        this.mDatabase.init(currentTimeMillis);
        if (this.mDatabase.wasUpgradePerformed()) {
            this.mDatabase.prunePackagesDataOnUpgrade(installedPackages);
        }
        int nullCount = 0;
        for (int i = 0; i < this.mCurrentStats.length; i++) {
            this.mCurrentStats[i] = this.mDatabase.getLatestUsageStats(i);
            if (this.mCurrentStats[i] == null) {
                nullCount++;
            }
        }
        if (nullCount > 0) {
            if (nullCount != this.mCurrentStats.length) {
                android.util.Slog.w(TAG, this.mLogPrefix + "Some stats have no latest available");
            }
            loadActiveStats(currentTimeMillis);
        } else {
            updateRolloverDeadline();
        }
        com.android.server.usage.IntervalStats currentDailyStats = this.mCurrentStats[0];
        if (currentDailyStats != null) {
            android.app.usage.UsageEvents.Event shutdownEvent = new android.app.usage.UsageEvents.Event(26, java.lang.Math.max(currentDailyStats.lastTimeSaved, currentDailyStats.endTime));
            shutdownEvent.mPackage = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME;
            currentDailyStats.addEvent(shutdownEvent);
            android.app.usage.UsageEvents.Event startupEvent = new android.app.usage.UsageEvents.Event(27, java.lang.System.currentTimeMillis());
            startupEvent.mPackage = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME;
            currentDailyStats.addEvent(startupEvent);
        }
        if (this.mDatabase.isNewUpdate()) {
            notifyNewUpdate();
        }
    }

    void userStopped() {
        persistActiveStats();
        this.mCachedEarlyEvents.clear();
    }

    int onPackageRemoved(java.lang.String packageName, long timeRemoved) {
        for (int i = this.mCachedEarlyEvents.numMaps() - 1; i >= 0; i--) {
            int eventType = this.mCachedEarlyEvents.keyAt(i);
            this.mCachedEarlyEvents.delete(eventType, packageName);
        }
        return this.mDatabase.onPackageRemoved(packageName, timeRemoved);
    }

    private void readPackageMappingsLocked(java.util.HashMap<java.lang.String, java.lang.Long> installedPackages, boolean deleteObsoleteData) {
        this.mDatabase.readMappingsLocked();
        if (this.mUserId != 0 && deleteObsoleteData) {
            updatePackageMappingsLocked(installedPackages);
        }
    }

    boolean updatePackageMappingsLocked(java.util.HashMap<java.lang.String, java.lang.Long> installedPackages) {
        if (com.android.internal.util.ArrayUtils.isEmpty(installedPackages)) {
            return true;
        }
        long timeNow = java.lang.System.currentTimeMillis();
        java.util.ArrayList<java.lang.String> removedPackages = new java.util.ArrayList<>();
        for (int i = this.mDatabase.mPackagesTokenData.packagesToTokensMap.size() - 1; i >= 0; i--) {
            java.lang.String packageName = this.mDatabase.mPackagesTokenData.packagesToTokensMap.keyAt(i);
            if (!installedPackages.containsKey(packageName)) {
                removedPackages.add(packageName);
            }
        }
        if (removedPackages.isEmpty()) {
            return true;
        }
        for (int i2 = removedPackages.size() - 1; i2 >= 0; i2--) {
            this.mDatabase.mPackagesTokenData.removePackage(removedPackages.get(i2), timeNow);
        }
        try {
            this.mDatabase.writeMappingsLocked();
            return true;
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Unable to write updated package mappings file on service initialization.");
            return false;
        }
    }

    boolean pruneUninstalledPackagesData() {
        return this.mDatabase.pruneUninstalledPackagesData();
    }

    private void onTimeChanged(long oldTime, long newTime) {
        this.mCachedEarlyEvents.clear();
        persistActiveStats();
        this.mDatabase.onTimeChanged(newTime - oldTime, newTime);
        loadActiveStats(newTime);
    }

    private long checkAndGetTimeLocked() {
        long actualSystemTime = java.lang.System.currentTimeMillis();
        if (!com.android.server.usage.UsageStatsService.ENABLE_TIME_CHANGE_CORRECTION) {
            return actualSystemTime;
        }
        long actualRealtime = android.os.SystemClock.elapsedRealtime();
        long expectedSystemTime = (actualRealtime - this.mRealTimeSnapshot) + this.mSystemTimeSnapshot;
        long diffSystemTime = actualSystemTime - expectedSystemTime;
        if (java.lang.Math.abs(diffSystemTime) > 2000) {
            android.util.Slog.i(TAG, this.mLogPrefix + "Time changed in by " + (diffSystemTime / 1000) + " seconds");
            onTimeChanged(expectedSystemTime, actualSystemTime);
            this.mRealTimeSnapshot = actualRealtime;
            this.mSystemTimeSnapshot = actualSystemTime;
        }
        return actualSystemTime;
    }

    private void convertToSystemTimeLocked(android.app.usage.UsageEvents.Event event) {
        event.mTimeStamp = java.lang.Math.max(0L, event.mTimeStamp - this.mRealTimeSnapshot) + this.mSystemTimeSnapshot;
    }

    void reportEvent(android.app.usage.UsageEvents.Event event) {
        if (DEBUG) {
            android.util.Slog.d(TAG, this.mLogPrefix + "Got usage event for " + event.mPackage + "[" + event.mTimeStamp + "]: " + eventToString(event.mEventType));
        }
        if (event.mEventType != 7 && event.mEventType != 31) {
            checkAndGetTimeLocked();
            convertToSystemTimeLocked(event);
        }
        if (event.mTimeStamp >= this.mDailyExpiryDate.getTimeInMillis()) {
            rolloverStats(event.mTimeStamp);
        }
        int i = 0;
        com.android.server.usage.IntervalStats currentDailyStats = this.mCurrentStats[0];
        android.content.res.Configuration newFullConfig = event.mConfiguration;
        if (event.mEventType == 5 && currentDailyStats.activeConfiguration != null) {
            event.mConfiguration = android.content.res.Configuration.generateDelta(currentDailyStats.activeConfiguration, newFullConfig);
        }
        if (event.mEventType != 6 && event.mEventType != 24 && event.mEventType != 25 && event.mEventType != 26 && event.mEventType != 31) {
            currentDailyStats.addEvent(event);
        }
        boolean incrementAppLaunch = false;
        if (event.mEventType == 1) {
            if (event.mPackage != null && !event.mPackage.equals(this.mLastBackgroundedPackage)) {
                incrementAppLaunch = true;
            }
        } else if (event.mEventType == 2 && event.mPackage != null) {
            this.mLastBackgroundedPackage = event.mPackage;
        }
        com.android.server.usage.IntervalStats[] intervalStatsArr = this.mCurrentStats;
        int length = intervalStatsArr.length;
        int i2 = 0;
        while (i2 < length) {
            com.android.server.usage.IntervalStats stats = intervalStatsArr[i2];
            switch (event.mEventType) {
                case 5:
                    stats.updateConfigurationStats(newFullConfig, event.mTimeStamp);
                    break;
                case 9:
                    stats.updateChooserCounts(event.mPackage, event.mContentType, event.mAction);
                    java.lang.String[] annotations = event.mContentAnnotations;
                    if (annotations != null) {
                        int length2 = annotations.length;
                        for (int i3 = i; i3 < length2; i3++) {
                            java.lang.String annotation = annotations[i3];
                            stats.updateChooserCounts(event.mPackage, annotation, event.mAction);
                        }
                    }
                    break;
                case 15:
                    stats.updateScreenInteractive(event.mTimeStamp);
                    break;
                case 16:
                    stats.updateScreenNonInteractive(event.mTimeStamp);
                    break;
                case 17:
                    stats.updateKeyguardShown(event.mTimeStamp);
                    break;
                case 18:
                    stats.updateKeyguardHidden(event.mTimeStamp);
                    break;
                default:
                    stats.update(event.mPackage, event.getClassName(), event.mTimeStamp, event.mEventType, event.mInstanceId);
                    if (incrementAppLaunch) {
                        stats.incrementAppLaunchCount(event.mPackage);
                    }
                    break;
            }
            i2++;
            i = 0;
        }
        notifyStatsChanged();
    }

    private static boolean validRange(long currentTime, long beginTime, long endTime) {
        return beginTime <= currentTime && beginTime < endTime;
    }

    private <T> java.util.List<T> queryStats(int intervalType, long beginTime, long endTime, com.android.server.usage.UsageStatsDatabase.StatCombiner<T> combiner, boolean skipEvents) {
        int intervalType2;
        if (intervalType != 4) {
            intervalType2 = intervalType;
        } else {
            int intervalType3 = this.mDatabase.findBestFitBucket(beginTime, endTime);
            if (intervalType3 >= 0) {
                intervalType2 = intervalType3;
            } else {
                intervalType2 = 0;
            }
        }
        if (intervalType2 < 0 || intervalType2 >= this.mCurrentStats.length) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this.mLogPrefix + "Bad intervalType used " + intervalType2);
            }
            return null;
        }
        com.android.server.usage.IntervalStats currentStats = this.mCurrentStats[intervalType2];
        if (DEBUG) {
            android.util.Slog.d(TAG, this.mLogPrefix + "SELECT * FROM " + intervalType2 + " WHERE beginTime >= " + beginTime + " AND endTime < " + endTime);
        }
        if (beginTime >= currentStats.endTime) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this.mLogPrefix + "Requesting stats after " + beginTime + " but latest is " + currentStats.endTime);
            }
            return null;
        }
        long truncatedEndTime = java.lang.Math.min(currentStats.beginTime, endTime);
        java.util.List<T> results = this.mDatabase.queryUsageStats(intervalType2, beginTime, truncatedEndTime, combiner, skipEvents);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Got " + (results != null ? results.size() : 0) + " results from disk");
            android.util.Slog.d(TAG, "Current stats beginTime=" + currentStats.beginTime + " endTime=" + currentStats.endTime);
        }
        int diskSize = results != null ? results.size() : 0;
        if (beginTime < currentStats.endTime && endTime > currentStats.beginTime) {
            if (DEBUG) {
                android.util.Slog.d(TAG, this.mLogPrefix + "Returning in-memory stats");
            }
            if (results == null) {
                results = new java.util.ArrayList();
            }
            this.mDatabase.filterStats(currentStats);
            combiner.combine(currentStats, true, results);
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, this.mLogPrefix + "Results: " + (results != null ? results.size() : 0));
        }
        android.util.Slog.d(TAG, this.mLogPrefix + "type=" + intervalType2 + " b=" + beginTime + " e=" + endTime + " cb=" + currentStats.beginTime + " ce=" + currentStats.endTime + " disk=" + diskSize + " all=" + (results != null ? results.size() : 0));
        return results;
    }

    java.util.List<android.app.usage.UsageStats> queryUsageStats(int bucketType, long beginTime, long endTime) {
        if (!validRange(checkAndGetTimeLocked(), beginTime, endTime)) {
            return null;
        }
        return queryStats(bucketType, beginTime, endTime, sUsageStatsCombiner, true);
    }

    java.util.List<android.app.usage.ConfigurationStats> queryConfigurationStats(int bucketType, long beginTime, long endTime) {
        if (!validRange(checkAndGetTimeLocked(), beginTime, endTime)) {
            return null;
        }
        return queryStats(bucketType, beginTime, endTime, sConfigStatsCombiner, true);
    }

    java.util.List<android.app.usage.EventStats> queryEventStats(int bucketType, long beginTime, long endTime) {
        if (!validRange(checkAndGetTimeLocked(), beginTime, endTime)) {
            return null;
        }
        return queryStats(bucketType, beginTime, endTime, sEventStatsCombiner, true);
    }

    android.app.usage.UsageEvents queryEvents(final long beginTime, final long endTime, final int flags, int[] eventTypeFilter, final android.util.ArraySet<java.lang.String> pkgNameFilter) {
        if (!validRange(checkAndGetTimeLocked(), beginTime, endTime)) {
            return null;
        }
        final boolean isQueryForAllEvents = com.android.internal.util.ArrayUtils.isEmpty(eventTypeFilter);
        final boolean isQueryForAllPackages = pkgNameFilter == null || pkgNameFilter.isEmpty();
        final boolean[] queryEventFilter = new boolean[33];
        if (!isQueryForAllEvents) {
            for (int eventType : eventTypeFilter) {
                if (eventType < 0 || eventType > 32) {
                    throw new java.lang.IllegalArgumentException("invalid event type: " + eventType);
                }
                queryEventFilter[eventType] = true;
            }
        }
        final android.util.ArraySet<java.lang.String> names = new android.util.ArraySet<>();
        java.util.List<android.app.usage.UsageEvents.Event> results = queryStats(0, beginTime, endTime, new com.android.server.usage.UsageStatsDatabase.StatCombiner<android.app.usage.UsageEvents.Event>() { // from class: com.android.server.usage.UserUsageStatsService.4
            @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
            public boolean combine(com.android.server.usage.IntervalStats stats, boolean mutable, java.util.List<android.app.usage.UsageEvents.Event> accumulatedResult) {
                int startIndex = stats.events.firstIndexOnOrAfter(beginTime);
                int size = stats.events.size();
                for (int i = startIndex; i < size; i++) {
                    android.app.usage.UsageEvents.Event event = stats.events.get(i);
                    if (event.mTimeStamp >= endTime) {
                        return false;
                    }
                    int eventType2 = event.mEventType;
                    if ((isQueryForAllEvents || queryEventFilter[eventType2]) && ((eventType2 != 8 || (flags & 2) != 2) && (eventType2 != 30 || (flags & 8) != 8))) {
                        if ((eventType2 == 10 || eventType2 == 12) && (flags & 4) == 4) {
                            event = event.getObfuscatedNotificationEvent();
                        }
                        if ((flags & 1) == 1) {
                            event = event.getObfuscatedIfInstantApp();
                        }
                        if (isQueryForAllPackages || pkgNameFilter.contains(event.mPackage)) {
                            if (event.mPackage != null) {
                                names.add(event.mPackage);
                            }
                            if (event.mClass != null) {
                                names.add(event.mClass);
                            }
                            if (event.mTaskRootPackage != null) {
                                names.add(event.mTaskRootPackage);
                            }
                            if (event.mTaskRootClass != null) {
                                names.add(event.mTaskRootClass);
                            }
                            accumulatedResult.add(event);
                        }
                    }
                }
                return true;
            }
        }, false);
        if (results == null || results.isEmpty()) {
            return null;
        }
        java.lang.String[] table = (java.lang.String[]) names.toArray(new java.lang.String[names.size()]);
        java.util.Arrays.sort(table);
        return new android.app.usage.UsageEvents(results, table, true);
    }

    android.app.usage.UsageEvents queryEarliestAppEvents(final long beginTime, final long endTime, final int eventType) {
        if (!validRange(checkAndGetTimeLocked(), beginTime, endTime)) {
            return null;
        }
        final android.util.ArraySet<java.lang.String> names = new android.util.ArraySet<>();
        final android.util.ArraySet<java.lang.String> eventSuccess = new android.util.ArraySet<>();
        java.util.List<android.app.usage.UsageEvents.Event> results = queryStats(0, beginTime, endTime, new com.android.server.usage.UsageStatsDatabase.StatCombiner() { // from class: com.android.server.usage.UserUsageStatsService$$ExternalSyntheticLambda1
            @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
            public final boolean combine(com.android.server.usage.IntervalStats intervalStats, boolean z, java.util.List list) {
                return com.android.server.usage.UserUsageStatsService.lambda$queryEarliestAppEvents$0(beginTime, endTime, eventSuccess, names, eventType, intervalStats, z, list);
            }
        }, false);
        if (results == null || results.isEmpty()) {
            return null;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Found " + results.size() + " early events for " + names.size() + " apps");
        }
        java.lang.String[] table = (java.lang.String[]) names.toArray(new java.lang.String[names.size()]);
        java.util.Arrays.sort(table);
        return new android.app.usage.UsageEvents(results, table, false);
    }

    static /* synthetic */ boolean lambda$queryEarliestAppEvents$0(long beginTime, long endTime, android.util.ArraySet eventSuccess, android.util.ArraySet names, int eventType, com.android.server.usage.IntervalStats stats, boolean mutable, java.util.List accumulatedResult) {
        int startIndex = stats.events.firstIndexOnOrAfter(beginTime);
        int size = stats.events.size();
        for (int i = startIndex; i < size; i++) {
            android.app.usage.UsageEvents.Event event = stats.events.get(i);
            if (event.getTimeStamp() >= endTime) {
                return false;
            }
            if (event.getPackageName() != null && !eventSuccess.contains(event.getPackageName())) {
                boolean firstEvent = names.add(event.getPackageName());
                if (event.getEventType() == eventType) {
                    accumulatedResult.add(event);
                    eventSuccess.add(event.getPackageName());
                } else if (firstEvent) {
                    accumulatedResult.add(event);
                }
            }
        }
        return true;
    }

    android.app.usage.UsageEvents queryEventsForPackage(final long beginTime, final long endTime, final java.lang.String packageName, final boolean includeTaskRoot) {
        if (!validRange(checkAndGetTimeLocked(), beginTime, endTime)) {
            return null;
        }
        final android.util.ArraySet<java.lang.String> names = new android.util.ArraySet<>();
        names.add(packageName);
        java.util.List<android.app.usage.UsageEvents.Event> results = queryStats(0, beginTime, endTime, new com.android.server.usage.UsageStatsDatabase.StatCombiner() { // from class: com.android.server.usage.UserUsageStatsService$$ExternalSyntheticLambda0
            @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
            public final boolean combine(com.android.server.usage.IntervalStats intervalStats, boolean z, java.util.List list) {
                return com.android.server.usage.UserUsageStatsService.lambda$queryEventsForPackage$1(beginTime, endTime, packageName, names, includeTaskRoot, intervalStats, z, list);
            }
        }, false);
        if (results == null || results.isEmpty()) {
            return null;
        }
        java.lang.String[] table = (java.lang.String[]) names.toArray(new java.lang.String[names.size()]);
        java.util.Arrays.sort(table);
        return new android.app.usage.UsageEvents(results, table, includeTaskRoot);
    }

    static /* synthetic */ boolean lambda$queryEventsForPackage$1(long beginTime, long endTime, java.lang.String packageName, android.util.ArraySet names, boolean includeTaskRoot, com.android.server.usage.IntervalStats stats, boolean mutable, java.util.List accumulatedResult) {
        int startIndex = stats.events.firstIndexOnOrAfter(beginTime);
        int size = stats.events.size();
        for (int i = startIndex; i < size; i++) {
            android.app.usage.UsageEvents.Event event = stats.events.get(i);
            if (event.mTimeStamp >= endTime) {
                return false;
            }
            if (packageName.equals(event.mPackage)) {
                if (event.mClass != null) {
                    names.add(event.mClass);
                }
                if (includeTaskRoot && event.mTaskRootPackage != null) {
                    names.add(event.mTaskRootPackage);
                }
                if (includeTaskRoot && event.mTaskRootClass != null) {
                    names.add(event.mTaskRootClass);
                }
                accumulatedResult.add(event);
            }
        }
        return true;
    }

    android.app.usage.UsageEvents queryEarliestEventsForPackage(long beginTime, final long endTime, final java.lang.String packageName, final int eventType) {
        long beginTime2;
        com.android.server.usage.UserUsageStatsService.CachedEarlyEvents cachedEvents;
        long currentTime = checkAndGetTimeLocked();
        if (!validRange(currentTime, beginTime, endTime)) {
            return null;
        }
        com.android.server.usage.UserUsageStatsService.CachedEarlyEvents cachedEvents2 = (com.android.server.usage.UserUsageStatsService.CachedEarlyEvents) this.mCachedEarlyEvents.get(eventType, packageName);
        if (cachedEvents2 == null) {
            com.android.server.usage.UserUsageStatsService.CachedEarlyEvents cachedEvents3 = new com.android.server.usage.UserUsageStatsService.CachedEarlyEvents();
            cachedEvents3.searchBeginTime = beginTime;
            this.mCachedEarlyEvents.add(eventType, packageName, cachedEvents3);
            beginTime2 = beginTime;
            cachedEvents = cachedEvents3;
        } else if (cachedEvents2.searchBeginTime <= beginTime && beginTime <= cachedEvents2.eventTime) {
            int numEvents = cachedEvents2.events == null ? 0 : cachedEvents2.events.size();
            if ((numEvents != 0 && cachedEvents2.events.get(numEvents - 1).getEventType() == eventType) || cachedEvents2.eventTime >= endTime) {
                if (cachedEvents2.eventTime > endTime || cachedEvents2.events == null) {
                    return null;
                }
                return new android.app.usage.UsageEvents(cachedEvents2.events, new java.lang.String[]{packageName}, false);
            }
            cachedEvents = cachedEvents2;
            beginTime2 = java.lang.Math.min(currentTime, cachedEvents2.eventTime);
        } else {
            cachedEvents2.searchBeginTime = beginTime;
            beginTime2 = beginTime;
            cachedEvents = cachedEvents2;
        }
        final long finalBeginTime = beginTime2;
        com.android.server.usage.UserUsageStatsService.CachedEarlyEvents cachedEvents4 = cachedEvents;
        java.util.List<android.app.usage.UsageEvents.Event> results = queryStats(0, beginTime2, endTime, new com.android.server.usage.UsageStatsDatabase.StatCombiner() { // from class: com.android.server.usage.UserUsageStatsService$$ExternalSyntheticLambda2
            @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
            public final boolean combine(com.android.server.usage.IntervalStats intervalStats, boolean z, java.util.List list) {
                return com.android.server.usage.UserUsageStatsService.lambda$queryEarliestEventsForPackage$2(finalBeginTime, endTime, packageName, eventType, intervalStats, z, list);
            }
        }, false);
        if (results == null || results.isEmpty()) {
            cachedEvents4.eventTime = java.lang.Math.min(currentTime, endTime);
            cachedEvents4.events = null;
            return null;
        }
        cachedEvents4.eventTime = results.get(results.size() - 1).getTimeStamp();
        cachedEvents4.events = results;
        return new android.app.usage.UsageEvents(results, new java.lang.String[]{packageName}, false);
    }

    static /* synthetic */ boolean lambda$queryEarliestEventsForPackage$2(long finalBeginTime, long endTime, java.lang.String packageName, int eventType, com.android.server.usage.IntervalStats stats, boolean mutable, java.util.List accumulatedResult) {
        int startIndex = stats.events.firstIndexOnOrAfter(finalBeginTime);
        int size = stats.events.size();
        for (int i = startIndex; i < size; i++) {
            android.app.usage.UsageEvents.Event event = stats.events.get(i);
            if (event.getTimeStamp() >= endTime) {
                return false;
            }
            if (packageName.equals(event.getPackageName())) {
                if (event.getEventType() == eventType) {
                    accumulatedResult.add(event);
                    return false;
                }
                if (accumulatedResult.size() == 0) {
                    accumulatedResult.add(event);
                }
            }
        }
        return true;
    }

    void persistActiveStats() {
        if (this.mStatsChanged) {
            android.util.Slog.i(TAG, this.mLogPrefix + "Flushing usage stats to disk");
            try {
                this.mDatabase.obfuscateCurrentStats(this.mCurrentStats);
                this.mDatabase.writeMappingsLocked();
                for (int i = 0; i < this.mCurrentStats.length; i++) {
                    this.mDatabase.putUsageStats(i, this.mCurrentStats[i]);
                }
                this.mStatsChanged = false;
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, this.mLogPrefix + "Failed to persist active stats", e);
            }
        }
    }

    private void rolloverStats(long currentTimeMillis) {
        int continueCount;
        android.util.ArraySet<java.lang.String> continuePkgs;
        android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> continueActivity;
        com.android.server.usage.IntervalStats[] intervalStatsArr;
        int i;
        com.android.server.usage.IntervalStats stat;
        long beginTime;
        int i2;
        com.android.server.usage.IntervalStats[] intervalStatsArr2;
        android.app.usage.UsageStats pkgStats;
        int i3;
        int pkgCount;
        com.android.server.usage.IntervalStats stat2;
        android.app.usage.UsageStats pkgStats2;
        long startTime = android.os.SystemClock.elapsedRealtime();
        android.util.Slog.i(TAG, this.mLogPrefix + "Rolling over usage stats");
        android.content.res.Configuration previousConfig = this.mCurrentStats[0].activeConfiguration;
        android.util.ArraySet<java.lang.String> continuePkgs2 = new android.util.ArraySet<>();
        android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> continueActivity2 = new android.util.ArrayMap<>();
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Integer>> continueForegroundService = new android.util.ArrayMap<>();
        com.android.server.usage.IntervalStats[] intervalStatsArr3 = this.mCurrentStats;
        int length = intervalStatsArr3.length;
        int i4 = 0;
        while (i4 < length) {
            com.android.server.usage.IntervalStats stat3 = intervalStatsArr3[i4];
            int pkgCount2 = stat3.packageStats.size();
            int i5 = 0;
            while (i5 < pkgCount2) {
                android.app.usage.UsageStats pkgStats3 = stat3.packageStats.valueAt(i5);
                if (pkgStats3.mActivities.size() > 0 || !pkgStats3.mForegroundServices.isEmpty()) {
                    if (pkgStats3.mActivities.size() <= 0) {
                        intervalStatsArr2 = intervalStatsArr3;
                        pkgStats = pkgStats3;
                        i3 = i5;
                        pkgCount = pkgCount2;
                        stat2 = stat3;
                    } else {
                        intervalStatsArr2 = intervalStatsArr3;
                        continueActivity2.put(pkgStats3.mPackageName, pkgStats3.mActivities);
                        pkgStats = pkgStats3;
                        i3 = i5;
                        pkgCount = pkgCount2;
                        stat2 = stat3;
                        stat3.update(pkgStats3.mPackageName, null, this.mDailyExpiryDate.getTimeInMillis() - 1, 3, 0);
                    }
                    android.app.usage.UsageStats pkgStats4 = pkgStats;
                    if (pkgStats4.mForegroundServices.isEmpty()) {
                        pkgStats2 = pkgStats4;
                    } else {
                        continueForegroundService.put(pkgStats4.mPackageName, pkgStats4.mForegroundServices);
                        pkgStats2 = pkgStats4;
                        stat2.update(pkgStats4.mPackageName, null, this.mDailyExpiryDate.getTimeInMillis() - 1, 22, 0);
                    }
                    continuePkgs2.add(pkgStats2.mPackageName);
                    notifyStatsChanged();
                } else {
                    intervalStatsArr2 = intervalStatsArr3;
                    i3 = i5;
                    pkgCount = pkgCount2;
                    stat2 = stat3;
                }
                i5 = i3 + 1;
                pkgCount2 = pkgCount;
                stat3 = stat2;
                intervalStatsArr3 = intervalStatsArr2;
            }
            com.android.server.usage.IntervalStats[] intervalStatsArr4 = intervalStatsArr3;
            com.android.server.usage.IntervalStats stat4 = stat3;
            stat4.updateConfigurationStats(null, this.mDailyExpiryDate.getTimeInMillis() - 1);
            stat4.commitTime(this.mDailyExpiryDate.getTimeInMillis() - 1);
            i4++;
            intervalStatsArr3 = intervalStatsArr4;
        }
        persistActiveStats();
        this.mDatabase.prune(currentTimeMillis);
        loadActiveStats(currentTimeMillis);
        int continueCount2 = continuePkgs2.size();
        int i6 = 0;
        while (i6 < continueCount2) {
            java.lang.String pkgName = continuePkgs2.valueAt(i6);
            long beginTime2 = this.mCurrentStats[0].beginTime;
            com.android.server.usage.IntervalStats[] intervalStatsArr5 = this.mCurrentStats;
            long beginTime3 = beginTime2;
            int i7 = 0;
            for (int length2 = intervalStatsArr5.length; i7 < length2; length2 = i2) {
                int i8 = length2;
                com.android.server.usage.IntervalStats stat5 = intervalStatsArr5[i7];
                if (!continueActivity2.containsKey(pkgName)) {
                    continueCount = continueCount2;
                    continuePkgs = continuePkgs2;
                    continueActivity = continueActivity2;
                    intervalStatsArr = intervalStatsArr5;
                    i = i7;
                    stat = stat5;
                    beginTime = beginTime3;
                    i2 = i8;
                } else {
                    continueCount = continueCount2;
                    android.util.SparseIntArray eventMap = continueActivity2.get(pkgName);
                    continuePkgs = continuePkgs2;
                    int size = eventMap.size();
                    continueActivity = continueActivity2;
                    int j = 0;
                    while (j < size) {
                        stat5.update(pkgName, null, beginTime3, eventMap.valueAt(j), eventMap.keyAt(j));
                        j++;
                        intervalStatsArr5 = intervalStatsArr5;
                        i7 = i7;
                        i8 = i8;
                        stat5 = stat5;
                    }
                    intervalStatsArr = intervalStatsArr5;
                    i = i7;
                    stat = stat5;
                    beginTime = beginTime3;
                    i2 = i8;
                }
                if (continueForegroundService.containsKey(pkgName)) {
                    android.util.ArrayMap<java.lang.String, java.lang.Integer> eventMap2 = continueForegroundService.get(pkgName);
                    int size2 = eventMap2.size();
                    for (int j2 = 0; j2 < size2; j2++) {
                        stat.update(pkgName, eventMap2.keyAt(j2), beginTime, eventMap2.valueAt(j2).intValue(), 0);
                    }
                }
                long beginTime4 = beginTime;
                stat.updateConfigurationStats(previousConfig, beginTime4);
                notifyStatsChanged();
                i7 = i + 1;
                beginTime3 = beginTime4;
                continueCount2 = continueCount;
                continuePkgs2 = continuePkgs;
                continueActivity2 = continueActivity;
                intervalStatsArr5 = intervalStatsArr;
            }
            i6++;
            continueCount2 = continueCount2;
            continuePkgs2 = continuePkgs2;
        }
        persistActiveStats();
        long totalTime = android.os.SystemClock.elapsedRealtime() - startTime;
        android.util.Slog.i(TAG, this.mLogPrefix + "Rolling over usage stats complete. Took " + totalTime + " milliseconds");
    }

    private void notifyStatsChanged() {
        if (!this.mStatsChanged) {
            this.mStatsChanged = true;
            this.mListener.onStatsUpdated();
        }
    }

    private void notifyNewUpdate() {
        this.mListener.onNewUpdate(this.mUserId);
    }

    private void loadActiveStats(long currentTimeMillis) {
        for (int intervalType = 0; intervalType < this.mCurrentStats.length; intervalType++) {
            com.android.server.usage.IntervalStats stats = this.mDatabase.getLatestUsageStats(intervalType);
            if (stats != null && currentTimeMillis < stats.beginTime + INTERVAL_LENGTH[intervalType]) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, this.mLogPrefix + "Loading existing stats @ " + sDateFormat.format(java.lang.Long.valueOf(stats.beginTime)) + "(" + stats.beginTime + ") for interval " + intervalType);
                }
                this.mCurrentStats[intervalType] = stats;
            } else {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Creating new stats @ " + sDateFormat.format(java.lang.Long.valueOf(currentTimeMillis)) + "(" + currentTimeMillis + ") for interval " + intervalType);
                }
                this.mCurrentStats[intervalType] = new com.android.server.usage.IntervalStats();
                this.mCurrentStats[intervalType].beginTime = currentTimeMillis;
                this.mCurrentStats[intervalType].endTime = 1 + currentTimeMillis;
            }
        }
        this.mStatsChanged = false;
        updateRolloverDeadline();
        this.mListener.onStatsReloaded();
    }

    private void updateRolloverDeadline() {
        this.mDailyExpiryDate.setTimeInMillis(this.mCurrentStats[0].beginTime);
        this.mDailyExpiryDate.addDays(1);
        android.util.Slog.i(TAG, this.mLogPrefix + "Rollover scheduled @ " + sDateFormat.format(java.lang.Long.valueOf(this.mDailyExpiryDate.getTimeInMillis())) + "(" + this.mDailyExpiryDate.getTimeInMillis() + ")");
    }

    void checkin(final com.android.internal.util.IndentingPrintWriter pw) {
        this.mDatabase.checkinDailyFiles(new com.android.server.usage.UsageStatsDatabase.CheckinAction() { // from class: com.android.server.usage.UserUsageStatsService.5
            @Override // com.android.server.usage.UsageStatsDatabase.CheckinAction
            public boolean checkin(com.android.server.usage.IntervalStats stats) {
                com.android.server.usage.UserUsageStatsService.this.printIntervalStats(pw, stats, false, false, null);
                return true;
            }
        });
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw, java.util.List<java.lang.String> pkgs) {
        dump(pw, pkgs, false);
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw, java.util.List<java.lang.String> pkgs, boolean compact) {
        printLast24HrEvents(pw, !compact, pkgs);
        for (int interval = 0; interval < this.mCurrentStats.length; interval++) {
            pw.print("In-memory ");
            pw.print(intervalToString(interval));
            pw.println(" stats");
            printIntervalStats(pw, this.mCurrentStats[interval], !compact, true, pkgs);
        }
        if (com.android.internal.util.CollectionUtils.isEmpty(pkgs)) {
            this.mDatabase.dump(pw, compact);
        }
    }

    void dumpDatabaseInfo(com.android.internal.util.IndentingPrintWriter ipw) {
        this.mDatabase.dump(ipw, false);
    }

    void dumpMappings(com.android.internal.util.IndentingPrintWriter ipw) {
        this.mDatabase.dumpMappings(ipw);
    }

    void deleteDataFor(java.lang.String pkg) {
        this.mDatabase.deleteDataFor(pkg);
    }

    void dumpFile(com.android.internal.util.IndentingPrintWriter ipw, java.lang.String[] args) {
        int interval;
        if (args == null || args.length == 0) {
            int numIntervals = this.mDatabase.mSortedStatFiles.length;
            for (int interval2 = 0; interval2 < numIntervals; interval2++) {
                ipw.println("interval=" + intervalToString(interval2));
                ipw.increaseIndent();
                dumpFileDetailsForInterval(ipw, interval2);
                ipw.decreaseIndent();
            }
            return;
        }
        try {
            int intervalValue = stringToInterval(args[0]);
            if (intervalValue == -1) {
                interval = java.lang.Integer.valueOf(args[0]).intValue();
            } else {
                interval = intervalValue;
            }
            if (interval < 0 || interval >= this.mDatabase.mSortedStatFiles.length) {
                ipw.println("the specified interval does not exist.");
                return;
            }
            if (args.length == 1) {
                dumpFileDetailsForInterval(ipw, interval);
                return;
            }
            try {
                long filename = java.lang.Long.valueOf(args[1]).longValue();
                com.android.server.usage.IntervalStats stats = this.mDatabase.readIntervalStatsForFile(interval, filename);
                if (stats == null) {
                    ipw.println("the specified filename does not exist.");
                } else {
                    dumpFileDetails(ipw, stats, java.lang.Long.valueOf(args[1]).longValue());
                }
            } catch (java.lang.NumberFormatException e) {
                ipw.println("invalid filename specified.");
            }
        } catch (java.lang.NumberFormatException e2) {
            ipw.println("invalid interval specified.");
        }
    }

    private void dumpFileDetailsForInterval(com.android.internal.util.IndentingPrintWriter ipw, int interval) {
        android.util.LongSparseArray<android.util.AtomicFile> files = this.mDatabase.mSortedStatFiles[interval];
        int numFiles = files.size();
        for (int i = 0; i < numFiles; i++) {
            long filename = files.keyAt(i);
            com.android.server.usage.IntervalStats stats = this.mDatabase.readIntervalStatsForFile(interval, filename);
            dumpFileDetails(ipw, stats, filename);
            ipw.println();
        }
    }

    private void dumpFileDetails(com.android.internal.util.IndentingPrintWriter ipw, com.android.server.usage.IntervalStats stats, long filename) {
        ipw.println("file=" + filename);
        ipw.increaseIndent();
        printIntervalStats(ipw, stats, false, false, null);
        ipw.decreaseIndent();
    }

    static java.lang.String formatDateTime(long dateTime, boolean pretty) {
        if (pretty) {
            return "\"" + sDateFormat.format(java.lang.Long.valueOf(dateTime)) + "\"";
        }
        return java.lang.Long.toString(dateTime);
    }

    private java.lang.String formatElapsedTime(long elapsedTime, boolean pretty) {
        if (pretty) {
            return "\"" + android.text.format.DateUtils.formatElapsedTime(elapsedTime / 1000) + "\"";
        }
        return java.lang.Long.toString(elapsedTime);
    }

    static void printEvent(com.android.internal.util.IndentingPrintWriter pw, android.app.usage.UsageEvents.Event event, boolean prettyDates) {
        pw.printPair("time", formatDateTime(event.mTimeStamp, prettyDates));
        pw.printPair("type", eventToString(event.mEventType));
        pw.printPair("package", event.mPackage);
        if (event.mClass != null) {
            pw.printPair("class", event.mClass);
        }
        if (event.mConfiguration != null) {
            pw.printPair("config", android.content.res.Configuration.resourceQualifierString(event.mConfiguration));
        }
        if (event.mShortcutId != null) {
            pw.printPair("shortcutId", event.mShortcutId);
        }
        if (event.mEventType == 11) {
            pw.printPair("standbyBucket", java.lang.Integer.valueOf(event.getAppStandbyBucket()));
            pw.printPair(com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY, android.app.usage.UsageStatsManager.reasonToString(event.getStandbyReason()));
        } else if (event.mEventType == 1 || event.mEventType == 2 || event.mEventType == 23) {
            pw.printPair("instanceId", java.lang.Integer.valueOf(event.getInstanceId()));
        }
        if (event.getTaskRootPackageName() != null) {
            pw.printPair("taskRootPackage", event.getTaskRootPackageName());
        }
        if (event.getTaskRootClassName() != null) {
            pw.printPair("taskRootClass", event.getTaskRootClassName());
        }
        if (event.mNotificationChannelId != null) {
            pw.printPair("channelId", event.mNotificationChannelId);
        }
        if (event.mEventType == 7 && event.mExtras != null) {
            pw.print(event.mExtras.toString());
        }
        pw.printHexPair("flags", event.mFlags);
        pw.println();
    }

    void printLast24HrEvents(com.android.internal.util.IndentingPrintWriter pw, boolean prettyDates, final java.util.List<java.lang.String> pkgs) {
        final long endTime = java.lang.System.currentTimeMillis();
        com.android.server.usage.UnixCalendar yesterday = new com.android.server.usage.UnixCalendar(endTime);
        yesterday.addDays(-1);
        final long beginTime = yesterday.getTimeInMillis();
        java.util.List<android.app.usage.UsageEvents.Event> events = queryStats(0, beginTime, endTime, new com.android.server.usage.UsageStatsDatabase.StatCombiner<android.app.usage.UsageEvents.Event>() { // from class: com.android.server.usage.UserUsageStatsService.6
            @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
            public boolean combine(com.android.server.usage.IntervalStats stats, boolean mutable, java.util.List<android.app.usage.UsageEvents.Event> accumulatedResult) {
                int startIndex = stats.events.firstIndexOnOrAfter(beginTime);
                int size = stats.events.size();
                for (int i = startIndex; i < size; i++) {
                    if (stats.events.get(i).mTimeStamp >= endTime) {
                        return false;
                    }
                    android.app.usage.UsageEvents.Event event = stats.events.get(i);
                    if (com.android.internal.util.CollectionUtils.isEmpty(pkgs) || pkgs.contains(event.mPackage)) {
                        accumulatedResult.add(event);
                    }
                }
                return true;
            }
        }, false);
        pw.print("Last 24 hour events (");
        if (prettyDates) {
            pw.printPair("timeRange", "\"" + android.text.format.DateUtils.formatDateRange(this.mContext, beginTime, endTime, sDateFormatFlags) + "\"");
        } else {
            pw.printPair("beginTime", java.lang.Long.valueOf(beginTime));
            pw.printPair("endTime", java.lang.Long.valueOf(endTime));
        }
        pw.println(")");
        if (events != null) {
            pw.increaseIndent();
            for (android.app.usage.UsageEvents.Event event : events) {
                printEvent(pw, event, prettyDates);
            }
            pw.decreaseIndent();
        }
    }

    void printEventAggregation(com.android.internal.util.IndentingPrintWriter pw, java.lang.String label, com.android.server.usage.IntervalStats.EventTracker tracker, boolean prettyDates) {
        if (tracker.count != 0 || tracker.duration != 0) {
            pw.print(label);
            pw.print(": ");
            pw.print(tracker.count);
            pw.print("x for ");
            pw.print(formatElapsedTime(tracker.duration, prettyDates));
            if (tracker.curStartTime != 0) {
                pw.print(" (now running, started at ");
                formatDateTime(tracker.curStartTime, prettyDates);
                pw.print(")");
            }
            pw.println();
        }
    }

    void printIntervalStats(com.android.internal.util.IndentingPrintWriter pw, com.android.server.usage.IntervalStats stats, boolean prettyDates, boolean skipEvents, java.util.List<java.lang.String> pkgs) {
        java.lang.String str;
        java.lang.String str2;
        java.lang.String str3;
        java.lang.String str4;
        if (prettyDates) {
            pw.printPair("timeRange", "\"" + android.text.format.DateUtils.formatDateRange(this.mContext, stats.beginTime, stats.endTime, sDateFormatFlags) + "\"");
        } else {
            pw.printPair("beginTime", java.lang.Long.valueOf(stats.beginTime));
            pw.printPair("endTime", java.lang.Long.valueOf(stats.endTime));
        }
        pw.println();
        pw.increaseIndent();
        pw.println("packages");
        pw.increaseIndent();
        android.util.ArrayMap<java.lang.String, android.app.usage.UsageStats> pkgStats = stats.packageStats;
        int pkgCount = pkgStats.size();
        int i = 0;
        while (true) {
            str = "errorCount";
            str2 = "package";
            if (i >= pkgCount) {
                break;
            }
            android.app.usage.UsageStats usageStats = pkgStats.valueAt(i);
            if (com.android.internal.util.CollectionUtils.isEmpty(pkgs) || pkgs.contains(usageStats.mPackageName)) {
                pw.printPair("package", usageStats.mPackageName);
                pw.printPair("totalTimeUsed", formatElapsedTime(usageStats.mTotalTimeInForeground, prettyDates));
                pw.printPair("lastTimeUsed", formatDateTime(usageStats.mLastTimeUsed, prettyDates));
                pw.printPair("totalTimeVisible", formatElapsedTime(usageStats.mTotalTimeVisible, prettyDates));
                pw.printPair("lastTimeVisible", formatDateTime(usageStats.mLastTimeVisible, prettyDates));
                pw.printPair("lastTimeComponentUsed", formatDateTime(usageStats.mLastTimeComponentUsed, prettyDates));
                pw.printPair("totalTimeFS", formatElapsedTime(usageStats.mTotalTimeForegroundServiceUsed, prettyDates));
                pw.printPair("lastTimeFS", formatDateTime(usageStats.mLastTimeForegroundServiceUsed, prettyDates));
                pw.printPair("appLaunchCount", java.lang.Integer.valueOf(usageStats.mAppLaunchCount));
                pw.printPair("errorCount", java.lang.Integer.valueOf(usageStats.mErrorCount));
                pw.println();
            }
            i++;
        }
        pw.decreaseIndent();
        pw.println();
        pw.println("ChooserCounts");
        pw.increaseIndent();
        for (android.app.usage.UsageStats usageStats2 : pkgStats.values()) {
            if (com.android.internal.util.CollectionUtils.isEmpty(pkgs) || pkgs.contains(usageStats2.mPackageName)) {
                pw.printPair(str2, usageStats2.mPackageName);
                pw.printPair(str, java.lang.Integer.valueOf(usageStats2.mErrorCount));
                if (usageStats2.mChooserCounts != null) {
                    int chooserCountSize = usageStats2.mChooserCounts.size();
                    int i2 = 0;
                    while (i2 < chooserCountSize) {
                        java.lang.String action = (java.lang.String) usageStats2.mChooserCounts.keyAt(i2);
                        android.util.ArrayMap<java.lang.String, java.lang.Integer> counts = (android.util.ArrayMap) usageStats2.mChooserCounts.valueAt(i2);
                        android.util.ArrayMap<java.lang.String, android.app.usage.UsageStats> pkgStats2 = pkgStats;
                        int annotationSize = counts.size();
                        int pkgCount2 = pkgCount;
                        int pkgCount3 = 0;
                        while (pkgCount3 < annotationSize) {
                            int annotationSize2 = annotationSize;
                            java.lang.String key = counts.keyAt(pkgCount3);
                            int count = counts.valueAt(pkgCount3).intValue();
                            if (count == 0) {
                                str3 = str;
                                str4 = str2;
                            } else {
                                str3 = str;
                                str4 = str2;
                                pw.printPair("ChooserCounts", action + ":" + key + " is " + java.lang.Integer.toString(count));
                                pw.println();
                            }
                            pkgCount3++;
                            annotationSize = annotationSize2;
                            str = str3;
                            str2 = str4;
                        }
                        i2++;
                        pkgStats = pkgStats2;
                        pkgCount = pkgCount2;
                    }
                }
                pw.println();
                pkgStats = pkgStats;
                pkgCount = pkgCount;
                str = str;
                str2 = str2;
            }
        }
        pw.decreaseIndent();
        if (com.android.internal.util.CollectionUtils.isEmpty(pkgs)) {
            pw.println("configurations");
            pw.increaseIndent();
            android.util.ArrayMap<android.content.res.Configuration, android.app.usage.ConfigurationStats> configStats = stats.configurations;
            int configCount = configStats.size();
            for (int i3 = 0; i3 < configCount; i3++) {
                android.app.usage.ConfigurationStats config = configStats.valueAt(i3);
                pw.printPair("config", android.content.res.Configuration.resourceQualifierString(config.mConfiguration));
                pw.printPair("totalTime", formatElapsedTime(config.mTotalTimeActive, prettyDates));
                pw.printPair("lastTime", formatDateTime(config.mLastTimeActive, prettyDates));
                pw.printPair(com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, java.lang.Integer.valueOf(config.mActivationCount));
                pw.println();
            }
            pw.decreaseIndent();
            pw.println("event aggregations");
            pw.increaseIndent();
            printEventAggregation(pw, "screen-interactive", stats.interactiveTracker, prettyDates);
            printEventAggregation(pw, "screen-non-interactive", stats.nonInteractiveTracker, prettyDates);
            printEventAggregation(pw, "keyguard-shown", stats.keyguardShownTracker, prettyDates);
            printEventAggregation(pw, "keyguard-hidden", stats.keyguardHiddenTracker, prettyDates);
            pw.decreaseIndent();
        }
        if (!skipEvents) {
            pw.println("events");
            pw.increaseIndent();
            android.app.usage.EventList events = stats.events;
            int eventCount = events != null ? events.size() : 0;
            for (int i4 = 0; i4 < eventCount; i4++) {
                android.app.usage.UsageEvents.Event event = events.get(i4);
                if (com.android.internal.util.CollectionUtils.isEmpty(pkgs) || pkgs.contains(event.mPackage)) {
                    printEvent(pw, event, prettyDates);
                }
            }
            pw.decreaseIndent();
        }
        pw.decreaseIndent();
    }

    public static java.lang.String intervalToString(int interval) {
        switch (interval) {
            case 0:
                return com.android.server.net.IOplusNetworkPolicyManagerServiceEx.TYPE_DAILY;
            case 1:
                return "weekly";
            case 2:
                return "monthly";
            case 3:
                return "yearly";
            default:
                return "?";
        }
    }

    private static int stringToInterval(java.lang.String interval) {
        byte b;
        java.lang.String lowerCase = interval.toLowerCase();
        switch (lowerCase.hashCode()) {
            case -791707519:
                b = !lowerCase.equals("weekly") ? (byte) -1 : (byte) 1;
                break;
            case -734561654:
                b = !lowerCase.equals("yearly") ? (byte) -1 : (byte) 3;
                break;
            case 95346201:
                b = !lowerCase.equals(com.android.server.net.IOplusNetworkPolicyManagerServiceEx.TYPE_DAILY) ? (byte) -1 : (byte) 0;
                break;
            case 1236635661:
                b = !lowerCase.equals("monthly") ? (byte) -1 : (byte) 2;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return -1;
        }
    }

    static java.lang.String eventToString(int eventType) {
        switch (eventType) {
            case 0:
                return "NONE";
            case 1:
                return "ACTIVITY_RESUMED";
            case 2:
                return "ACTIVITY_PAUSED";
            case 3:
                return "END_OF_DAY";
            case 4:
                return "CONTINUE_PREVIOUS_DAY";
            case 5:
                return "CONFIGURATION_CHANGE";
            case 6:
                return "SYSTEM_INTERACTION";
            case 7:
                return "USER_INTERACTION";
            case 8:
                return "SHORTCUT_INVOCATION";
            case 9:
                return "CHOOSER_ACTION";
            case 10:
                return "NOTIFICATION_SEEN";
            case 11:
                return "STANDBY_BUCKET_CHANGED";
            case 12:
                return "NOTIFICATION_INTERRUPTION";
            case 13:
                return "SLICE_PINNED_PRIV";
            case 14:
                return "SLICE_PINNED";
            case 15:
                return "SCREEN_INTERACTIVE";
            case 16:
                return "SCREEN_NON_INTERACTIVE";
            case 17:
                return "KEYGUARD_SHOWN";
            case 18:
                return "KEYGUARD_HIDDEN";
            case 19:
                return "FOREGROUND_SERVICE_START";
            case 20:
                return "FOREGROUND_SERVICE_STOP";
            case 21:
                return "CONTINUING_FOREGROUND_SERVICE";
            case 22:
                return "ROLLOVER_FOREGROUND_SERVICE";
            case 23:
                return "ACTIVITY_STOPPED";
            case 24:
            case 25:
            default:
                return "UNKNOWN_TYPE_" + eventType;
            case 26:
                return "DEVICE_SHUTDOWN";
            case 27:
                return "DEVICE_STARTUP";
            case 28:
                return "USER_UNLOCKED";
            case 29:
                return "USER_STOPPED";
            case 30:
                return "LOCUS_ID_SET";
            case 31:
                return "APP_COMPONENT_USED";
            case 32:
                return "CRASH_OR_NOT_RESPONCE";
        }
    }

    byte[] getBackupPayload(java.lang.String key) {
        checkAndGetTimeLocked();
        persistActiveStats();
        return this.mDatabase.getBackupPayload(key);
    }

    java.util.Set<java.lang.String> applyRestoredPayload(java.lang.String key, byte[] payload) {
        checkAndGetTimeLocked();
        return this.mDatabase.applyRestoredPayload(key, payload);
    }
}
