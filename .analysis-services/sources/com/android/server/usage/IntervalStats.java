package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class IntervalStats {
    public static final int CURRENT_MAJOR_VERSION = 1;
    public static final int CURRENT_MINOR_VERSION = 1;
    public static final boolean IS_LIGHT_OS = android.os.SystemProperties.getBoolean("ro.oplus.lightos", false);
    public static final int MAX_EVENTS = 100000;
    public static final int MAX_EVENTS_LIGHT = 50000;
    private static final java.lang.String TAG = "IntervalStats";
    public android.content.res.Configuration activeConfiguration;
    public long beginTime;
    public long endTime;
    public long lastTimeSaved;
    public int majorVersion = 1;
    public int minorVersion = 1;
    public final com.android.server.usage.IntervalStats.EventTracker interactiveTracker = new com.android.server.usage.IntervalStats.EventTracker();
    public final com.android.server.usage.IntervalStats.EventTracker nonInteractiveTracker = new com.android.server.usage.IntervalStats.EventTracker();
    public final com.android.server.usage.IntervalStats.EventTracker keyguardShownTracker = new com.android.server.usage.IntervalStats.EventTracker();
    public final com.android.server.usage.IntervalStats.EventTracker keyguardHiddenTracker = new com.android.server.usage.IntervalStats.EventTracker();
    public final android.util.ArrayMap<java.lang.String, android.app.usage.UsageStats> packageStats = new android.util.ArrayMap<>();
    public final android.util.SparseArray<android.app.usage.UsageStats> packageStatsObfuscated = new android.util.SparseArray<>();
    public final android.util.ArrayMap<android.content.res.Configuration, android.app.usage.ConfigurationStats> configurations = new android.util.ArrayMap<>();
    public final android.app.usage.EventList events = new android.app.usage.EventList();
    public final android.util.ArraySet<java.lang.String> mStringCache = new android.util.ArraySet<>();

    public static final class EventTracker {
        public int count;
        public long curStartTime;
        public long duration;
        public long lastEventTime;

        public void commitTime(long timeStamp) {
            if (this.curStartTime != 0) {
                this.duration += timeStamp - this.curStartTime;
                this.curStartTime = 0L;
            }
        }

        public void update(long timeStamp) {
            if (this.curStartTime == 0) {
                this.count++;
            }
            commitTime(timeStamp);
            this.curStartTime = timeStamp;
            this.lastEventTime = timeStamp;
        }

        void addToEventStats(java.util.List<android.app.usage.EventStats> out, int event, long beginTime, long endTime) {
            if (this.count != 0 || this.duration != 0) {
                android.app.usage.EventStats ev = new android.app.usage.EventStats();
                ev.mEventType = event;
                ev.mCount = this.count;
                ev.mTotalTime = this.duration;
                ev.mLastEventTime = this.lastEventTime;
                ev.mBeginTimeStamp = beginTime;
                ev.mEndTimeStamp = endTime;
                out.add(ev);
            }
        }
    }

    android.app.usage.UsageStats getOrCreateUsageStats(java.lang.String packageName) {
        android.app.usage.UsageStats usageStats = this.packageStats.get(packageName);
        if (usageStats == null) {
            android.app.usage.UsageStats usageStats2 = new android.app.usage.UsageStats();
            usageStats2.mPackageName = getCachedStringRef(packageName);
            usageStats2.mBeginTimeStamp = this.beginTime;
            usageStats2.mEndTimeStamp = this.endTime;
            this.packageStats.put(usageStats2.mPackageName, usageStats2);
            return usageStats2;
        }
        return usageStats;
    }

    android.app.usage.ConfigurationStats getOrCreateConfigurationStats(android.content.res.Configuration config) {
        android.app.usage.ConfigurationStats configStats = this.configurations.get(config);
        if (configStats == null) {
            android.app.usage.ConfigurationStats configStats2 = new android.app.usage.ConfigurationStats();
            configStats2.mBeginTimeStamp = this.beginTime;
            configStats2.mEndTimeStamp = this.endTime;
            configStats2.mConfiguration = config;
            this.configurations.put(config, configStats2);
            return configStats2;
        }
        return configStats;
    }

    android.app.usage.UsageEvents.Event buildEvent(java.lang.String packageName, java.lang.String className) {
        android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event();
        event.mPackage = getCachedStringRef(packageName);
        if (className != null) {
            event.mClass = getCachedStringRef(className);
        }
        return event;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    android.app.usage.UsageEvents.Event buildEvent(android.util.proto.ProtoInputStream parser, java.util.List<java.lang.String> stringPool) throws java.io.IOException {
        android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event();
        while (true) {
            switch (parser.nextField()) {
                case -1:
                    switch (event.mEventType) {
                        case 5:
                            if (event.mConfiguration == null) {
                                event.mConfiguration = new android.content.res.Configuration();
                            }
                            return event;
                        case 8:
                            if (event.mShortcutId == null) {
                                event.mShortcutId = "";
                            }
                            return event;
                        case 12:
                            if (event.mNotificationChannelId == null) {
                                event.mNotificationChannelId = "";
                            }
                            return event;
                        case 30:
                            if (event.mLocusId == null) {
                                event.mLocusId = "";
                            }
                            return event;
                        default:
                            return event;
                    }
                case 1:
                    event.mPackage = getCachedStringRef(parser.readString(1138166333441L));
                    break;
                case 2:
                    event.mPackage = getCachedStringRef(stringPool.get(parser.readInt(1120986464258L) - 1));
                    break;
                case 3:
                    event.mClass = getCachedStringRef(parser.readString(1138166333443L));
                    break;
                case 4:
                    event.mClass = getCachedStringRef(stringPool.get(parser.readInt(1120986464260L) - 1));
                    break;
                case 5:
                    event.mTimeStamp = this.beginTime + parser.readLong(1112396529669L);
                    break;
                case 6:
                    event.mFlags = parser.readInt(1120986464262L);
                    break;
                case 7:
                    event.mEventType = parser.readInt(1120986464263L);
                    break;
                case 8:
                    event.mConfiguration = new android.content.res.Configuration();
                    event.mConfiguration.readFromProto(parser, 1146756268040L);
                    break;
                case 9:
                    event.mShortcutId = parser.readString(1138166333449L).intern();
                    break;
                case 11:
                    event.mBucketAndReason = parser.readInt(1120986464267L);
                    break;
                case 12:
                    event.mNotificationChannelId = parser.readString(1138166333452L);
                    break;
                case 13:
                    event.mNotificationChannelId = getCachedStringRef(stringPool.get(parser.readInt(1120986464269L) - 1));
                    break;
                case 14:
                    event.mInstanceId = parser.readInt(1120986464270L);
                    break;
                case 15:
                    event.mTaskRootPackage = getCachedStringRef(stringPool.get(parser.readInt(1120986464271L) - 1));
                    break;
                case 16:
                    event.mTaskRootClass = getCachedStringRef(stringPool.get(parser.readInt(1120986464272L) - 1));
                    break;
                case 17:
                    event.mLocusId = getCachedStringRef(stringPool.get(parser.readInt(1120986464273L) - 1));
                    break;
            }
        }
    }

    private boolean isStatefulEvent(int eventType) {
        switch (eventType) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 26:
                return true;
            default:
                return false;
        }
    }

    private boolean isUserVisibleEvent(int eventType) {
        return (eventType == 6 || eventType == 11) ? false : true;
    }

    public void update(java.lang.String packageName, java.lang.String className, long timeStamp, int eventType, int instanceId) {
        if (eventType == 26 || eventType == 25) {
            int size = this.packageStats.size();
            for (int i = 0; i < size; i++) {
                this.packageStats.valueAt(i).update(null, timeStamp, eventType, instanceId);
            }
        } else {
            android.app.usage.UsageStats usageStats = getOrCreateUsageStats(packageName);
            if (eventType == 32) {
                usageStats.mErrorCount++;
                return;
            }
            usageStats.update(className, timeStamp, eventType, instanceId);
        }
        if (timeStamp > this.endTime) {
            this.endTime = timeStamp;
        }
    }

    public void addEvent(android.app.usage.UsageEvents.Event event) {
        event.mPackage = getCachedStringRef(event.mPackage);
        if (event.mClass != null) {
            event.mClass = getCachedStringRef(event.mClass);
        }
        if (event.mTaskRootPackage != null) {
            event.mTaskRootPackage = getCachedStringRef(event.mTaskRootPackage);
        }
        if (event.mTaskRootClass != null) {
            event.mTaskRootClass = getCachedStringRef(event.mTaskRootClass);
        }
        if (event.mEventType == 12) {
            event.mNotificationChannelId = getCachedStringRef(event.mNotificationChannelId);
        }
        if (IS_LIGHT_OS) {
            if (this.events.size() < 50000) {
                this.events.insert(event);
            }
        } else if (this.events.size() < 100000) {
            this.events.insert(event);
        }
        if (event.mTimeStamp > this.endTime) {
            this.endTime = event.mTimeStamp;
        }
    }

    void updateChooserCounts(java.lang.String packageName, java.lang.String category, java.lang.String action) {
        android.util.ArrayMap<java.lang.String, java.lang.Integer> chooserCounts;
        android.app.usage.UsageStats usageStats = getOrCreateUsageStats(packageName);
        if (usageStats.mChooserCounts == null) {
            usageStats.mChooserCounts = new android.util.ArrayMap();
        }
        int idx = usageStats.mChooserCounts.indexOfKey(action);
        if (idx < 0) {
            chooserCounts = new android.util.ArrayMap<>();
            usageStats.mChooserCounts.put(action, chooserCounts);
        } else {
            android.util.ArrayMap<java.lang.String, java.lang.Integer> chooserCounts2 = usageStats.mChooserCounts;
            chooserCounts = (android.util.ArrayMap) chooserCounts2.valueAt(idx);
        }
        int currentCount = chooserCounts.getOrDefault(category, 0).intValue();
        chooserCounts.put(category, java.lang.Integer.valueOf(currentCount + 1));
    }

    void updateConfigurationStats(android.content.res.Configuration config, long timeStamp) {
        if (this.activeConfiguration != null) {
            android.app.usage.ConfigurationStats activeStats = this.configurations.get(this.activeConfiguration);
            activeStats.mTotalTimeActive += timeStamp - activeStats.mLastTimeActive;
            activeStats.mLastTimeActive = timeStamp - 1;
        }
        if (config != null) {
            android.app.usage.ConfigurationStats configStats = getOrCreateConfigurationStats(config);
            configStats.mLastTimeActive = timeStamp;
            configStats.mActivationCount++;
            this.activeConfiguration = configStats.mConfiguration;
        }
        if (timeStamp > this.endTime) {
            this.endTime = timeStamp;
        }
    }

    void incrementAppLaunchCount(java.lang.String packageName) {
        android.app.usage.UsageStats usageStats = getOrCreateUsageStats(packageName);
        usageStats.mAppLaunchCount++;
    }

    void commitTime(long timeStamp) {
        this.interactiveTracker.commitTime(timeStamp);
        this.nonInteractiveTracker.commitTime(timeStamp);
        this.keyguardShownTracker.commitTime(timeStamp);
        this.keyguardHiddenTracker.commitTime(timeStamp);
    }

    void updateScreenInteractive(long timeStamp) {
        this.interactiveTracker.update(timeStamp);
        this.nonInteractiveTracker.commitTime(timeStamp);
    }

    void updateScreenNonInteractive(long timeStamp) {
        this.nonInteractiveTracker.update(timeStamp);
        this.interactiveTracker.commitTime(timeStamp);
    }

    void updateKeyguardShown(long timeStamp) {
        this.keyguardShownTracker.update(timeStamp);
        this.keyguardHiddenTracker.commitTime(timeStamp);
    }

    void updateKeyguardHidden(long timeStamp) {
        this.keyguardHiddenTracker.update(timeStamp);
        this.keyguardShownTracker.commitTime(timeStamp);
    }

    void addEventStatsTo(java.util.List<android.app.usage.EventStats> out) {
        this.interactiveTracker.addToEventStats(out, 15, this.beginTime, this.endTime);
        this.nonInteractiveTracker.addToEventStats(out, 16, this.beginTime, this.endTime);
        this.keyguardShownTracker.addToEventStats(out, 17, this.beginTime, this.endTime);
        this.keyguardHiddenTracker.addToEventStats(out, 18, this.beginTime, this.endTime);
    }

    private java.lang.String getCachedStringRef(java.lang.String str) {
        int index = this.mStringCache.indexOf(str);
        if (index < 0) {
            this.mStringCache.add(str);
            return str;
        }
        return this.mStringCache.valueAt(index);
    }

    void upgradeIfNeeded() {
        if (this.majorVersion >= 1) {
            return;
        }
        this.majorVersion = 1;
    }

    private boolean deobfuscateUsageStats(com.android.server.usage.PackagesTokenData packagesTokenData) {
        int usageStatsSize;
        int usageStatsSize2;
        int chooserActionsSize;
        com.android.server.usage.PackagesTokenData packagesTokenData2 = packagesTokenData;
        boolean dataOmitted = false;
        android.util.ArraySet<java.lang.Integer> omittedTokens = new android.util.ArraySet<>();
        int usageStatsSize3 = this.packageStatsObfuscated.size();
        int statsIndex = 0;
        while (statsIndex < usageStatsSize3) {
            int packageToken = this.packageStatsObfuscated.keyAt(statsIndex);
            android.app.usage.UsageStats usageStats = this.packageStatsObfuscated.valueAt(statsIndex);
            usageStats.mPackageName = packagesTokenData2.getPackageString(packageToken);
            if (usageStats.mPackageName == null) {
                omittedTokens.add(java.lang.Integer.valueOf(packageToken));
                dataOmitted = true;
                usageStatsSize = usageStatsSize3;
            } else {
                int chooserActionsSize2 = usageStats.mChooserCountsObfuscated.size();
                int actionIndex = 0;
                while (actionIndex < chooserActionsSize2) {
                    android.util.ArrayMap<java.lang.String, java.lang.Integer> categoryCountsMap = new android.util.ArrayMap<>();
                    int actionToken = usageStats.mChooserCountsObfuscated.keyAt(actionIndex);
                    java.lang.String action = packagesTokenData2.getString(packageToken, actionToken);
                    if (action == null) {
                        usageStatsSize2 = usageStatsSize3;
                        chooserActionsSize = chooserActionsSize2;
                    } else {
                        android.util.SparseIntArray categoryCounts = (android.util.SparseIntArray) usageStats.mChooserCountsObfuscated.valueAt(actionIndex);
                        int categoriesSize = categoryCounts.size();
                        int categoryIndex = 0;
                        while (categoryIndex < categoriesSize) {
                            int usageStatsSize4 = usageStatsSize3;
                            int categoryToken = categoryCounts.keyAt(categoryIndex);
                            int chooserActionsSize3 = chooserActionsSize2;
                            java.lang.String category = packagesTokenData2.getString(packageToken, categoryToken);
                            if (category != null) {
                                categoryCountsMap.put(category, java.lang.Integer.valueOf(categoryCounts.valueAt(categoryIndex)));
                            }
                            categoryIndex++;
                            packagesTokenData2 = packagesTokenData;
                            usageStatsSize3 = usageStatsSize4;
                            chooserActionsSize2 = chooserActionsSize3;
                        }
                        usageStatsSize2 = usageStatsSize3;
                        chooserActionsSize = chooserActionsSize2;
                        usageStats.mChooserCounts.put(action, categoryCountsMap);
                    }
                    actionIndex++;
                    packagesTokenData2 = packagesTokenData;
                    usageStatsSize3 = usageStatsSize2;
                    chooserActionsSize2 = chooserActionsSize;
                }
                usageStatsSize = usageStatsSize3;
                this.packageStats.put(usageStats.mPackageName, usageStats);
            }
            statsIndex++;
            packagesTokenData2 = packagesTokenData;
            usageStatsSize3 = usageStatsSize;
        }
        if (dataOmitted) {
            android.util.Slog.d(TAG, "Unable to parse usage stats packages: " + java.util.Arrays.toString(omittedTokens.toArray()));
        }
        return dataOmitted;
    }

    private boolean deobfuscateEvents(com.android.server.usage.PackagesTokenData packagesTokenData) {
        boolean dataOmitted = false;
        android.util.ArraySet<java.lang.Integer> omittedTokens = new android.util.ArraySet<>();
        int i = this.events.size();
        while (true) {
            i--;
            if (i >= 0) {
                android.app.usage.UsageEvents.Event event = this.events.get(i);
                int packageToken = event.mPackageToken;
                event.mPackage = packagesTokenData.getPackageString(packageToken);
                if (event.mPackage == null) {
                    omittedTokens.add(java.lang.Integer.valueOf(packageToken));
                    this.events.remove(i);
                    dataOmitted = true;
                } else {
                    if (event.mClassToken != -1) {
                        event.mClass = packagesTokenData.getString(packageToken, event.mClassToken);
                    }
                    if (event.mTaskRootPackageToken != -1) {
                        event.mTaskRootPackage = packagesTokenData.getString(packageToken, event.mTaskRootPackageToken);
                    }
                    if (event.mTaskRootClassToken != -1) {
                        event.mTaskRootClass = packagesTokenData.getString(packageToken, event.mTaskRootClassToken);
                    }
                    switch (event.mEventType) {
                        case 5:
                            if (event.mConfiguration == null) {
                                event.mConfiguration = new android.content.res.Configuration();
                            }
                            break;
                        case 7:
                            if (event.mUserInteractionExtrasToken != null) {
                                java.lang.String category = packagesTokenData.getString(packageToken, event.mUserInteractionExtrasToken.mCategoryToken);
                                java.lang.String action = packagesTokenData.getString(packageToken, event.mUserInteractionExtrasToken.mActionToken);
                                if (android.text.TextUtils.isEmpty(category) || android.text.TextUtils.isEmpty(action)) {
                                    this.events.remove(i);
                                    dataOmitted = true;
                                } else {
                                    event.mExtras = new android.os.PersistableBundle();
                                    event.mExtras.putString("android.app.usage.extra.EVENT_CATEGORY", category);
                                    event.mExtras.putString("android.app.usage.extra.EVENT_ACTION", action);
                                    event.mUserInteractionExtrasToken = null;
                                }
                            }
                            break;
                        case 8:
                            event.mShortcutId = packagesTokenData.getString(packageToken, event.mShortcutIdToken);
                            if (event.mShortcutId == null) {
                                android.util.Slog.v(TAG, "Unable to parse shortcut " + event.mShortcutIdToken + " for package " + packageToken);
                                this.events.remove(i);
                                dataOmitted = true;
                            }
                            break;
                        case 12:
                            event.mNotificationChannelId = packagesTokenData.getString(packageToken, event.mNotificationChannelIdToken);
                            if (event.mNotificationChannelId == null) {
                                android.util.Slog.v(TAG, "Unable to parse notification channel " + event.mNotificationChannelIdToken + " for package " + packageToken);
                                this.events.remove(i);
                                dataOmitted = true;
                            }
                            break;
                        case 30:
                            event.mLocusId = packagesTokenData.getString(packageToken, event.mLocusIdToken);
                            if (event.mLocusId == null) {
                                android.util.Slog.v(TAG, "Unable to parse locus " + event.mLocusIdToken + " for package " + packageToken);
                                this.events.remove(i);
                                dataOmitted = true;
                            }
                            break;
                    }
                }
            } else {
                if (dataOmitted) {
                    android.util.Slog.d(TAG, "Unable to parse event packages: " + java.util.Arrays.toString(omittedTokens.toArray()));
                }
                return dataOmitted;
            }
        }
    }

    public boolean deobfuscateData(com.android.server.usage.PackagesTokenData packagesTokenData) {
        boolean statsOmitted = deobfuscateUsageStats(packagesTokenData);
        boolean eventsOmitted = deobfuscateEvents(packagesTokenData);
        return statsOmitted || eventsOmitted;
    }

    private void obfuscateUsageStatsData(com.android.server.usage.PackagesTokenData packagesTokenData) {
        int usageStatsSize;
        int usageStatsSize2;
        int usageStatsSize3 = this.packageStats.size();
        int statsIndex = 0;
        while (statsIndex < usageStatsSize3) {
            java.lang.String packageName = this.packageStats.keyAt(statsIndex);
            android.app.usage.UsageStats usageStats = this.packageStats.valueAt(statsIndex);
            if (usageStats == null) {
                usageStatsSize = usageStatsSize3;
            } else {
                int packageToken = packagesTokenData.getPackageTokenOrAdd(packageName, usageStats.mEndTimeStamp);
                if (packageToken == -1) {
                    usageStatsSize = usageStatsSize3;
                } else {
                    usageStats.mPackageToken = packageToken;
                    int chooserActionsSize = usageStats.mChooserCounts.size();
                    int actionIndex = 0;
                    while (actionIndex < chooserActionsSize) {
                        java.lang.String action = (java.lang.String) usageStats.mChooserCounts.keyAt(actionIndex);
                        android.util.ArrayMap<java.lang.String, java.lang.Integer> categoriesMap = (android.util.ArrayMap) usageStats.mChooserCounts.valueAt(actionIndex);
                        if (categoriesMap == null) {
                            usageStatsSize2 = usageStatsSize3;
                        } else {
                            android.util.SparseIntArray categoryCounts = new android.util.SparseIntArray();
                            int categoriesSize = categoriesMap.size();
                            int categoryIndex = 0;
                            while (categoryIndex < categoriesSize) {
                                java.lang.String category = categoriesMap.keyAt(categoryIndex);
                                int categoryToken = packagesTokenData.getTokenOrAdd(packageToken, packageName, category);
                                int usageStatsSize4 = usageStatsSize3;
                                int usageStatsSize5 = categoriesMap.valueAt(categoryIndex).intValue();
                                categoryCounts.put(categoryToken, usageStatsSize5);
                                categoryIndex++;
                                usageStatsSize3 = usageStatsSize4;
                            }
                            usageStatsSize2 = usageStatsSize3;
                            int actionToken = packagesTokenData.getTokenOrAdd(packageToken, packageName, action);
                            usageStats.mChooserCountsObfuscated.put(actionToken, categoryCounts);
                        }
                        actionIndex++;
                        usageStatsSize3 = usageStatsSize2;
                    }
                    usageStatsSize = usageStatsSize3;
                    this.packageStatsObfuscated.put(packageToken, usageStats);
                }
            }
            statsIndex++;
            usageStatsSize3 = usageStatsSize;
        }
    }

    private void obfuscateEventsData(com.android.server.usage.PackagesTokenData packagesTokenData) {
        for (int i = this.events.size() - 1; i >= 0; i--) {
            android.app.usage.UsageEvents.Event event = this.events.get(i);
            if (event != null) {
                int packageToken = packagesTokenData.getPackageTokenOrAdd(event.mPackage, event.mTimeStamp);
                if (packageToken == -1) {
                    this.events.remove(i);
                } else {
                    event.mPackageToken = packageToken;
                    if (!android.text.TextUtils.isEmpty(event.mClass)) {
                        event.mClassToken = packagesTokenData.getTokenOrAdd(packageToken, event.mPackage, event.mClass);
                    }
                    if (!android.text.TextUtils.isEmpty(event.mTaskRootPackage)) {
                        event.mTaskRootPackageToken = packagesTokenData.getTokenOrAdd(packageToken, event.mPackage, event.mTaskRootPackage);
                    }
                    if (!android.text.TextUtils.isEmpty(event.mTaskRootClass)) {
                        event.mTaskRootClassToken = packagesTokenData.getTokenOrAdd(packageToken, event.mPackage, event.mTaskRootClass);
                    }
                    switch (event.mEventType) {
                        case 7:
                            if (event.mExtras != null && event.mExtras.size() != 0) {
                                java.lang.String category = event.mExtras.getString("android.app.usage.extra.EVENT_CATEGORY");
                                java.lang.String action = event.mExtras.getString("android.app.usage.extra.EVENT_ACTION");
                                if (!android.text.TextUtils.isEmpty(category) && !android.text.TextUtils.isEmpty(action)) {
                                    event.mUserInteractionExtrasToken = new android.app.usage.UsageEvents.Event.UserInteractionEventExtrasToken();
                                    event.mUserInteractionExtrasToken.mCategoryToken = packagesTokenData.getTokenOrAdd(packageToken, event.mPackage, category);
                                    event.mUserInteractionExtrasToken.mActionToken = packagesTokenData.getTokenOrAdd(packageToken, event.mPackage, action);
                                }
                            }
                            break;
                        case 8:
                            if (!android.text.TextUtils.isEmpty(event.mShortcutId)) {
                                event.mShortcutIdToken = packagesTokenData.getTokenOrAdd(packageToken, event.mPackage, event.mShortcutId);
                            }
                            break;
                        case 12:
                            if (!android.text.TextUtils.isEmpty(event.mNotificationChannelId)) {
                                event.mNotificationChannelIdToken = packagesTokenData.getTokenOrAdd(packageToken, event.mPackage, event.mNotificationChannelId);
                            }
                            break;
                        case 30:
                            if (!android.text.TextUtils.isEmpty(event.mLocusId)) {
                                event.mLocusIdToken = packagesTokenData.getTokenOrAdd(packageToken, event.mPackage, event.mLocusId);
                            }
                            break;
                    }
                }
            }
        }
    }

    public void obfuscateData(com.android.server.usage.PackagesTokenData packagesTokenData) {
        obfuscateUsageStatsData(packagesTokenData);
        obfuscateEventsData(packagesTokenData);
    }
}
