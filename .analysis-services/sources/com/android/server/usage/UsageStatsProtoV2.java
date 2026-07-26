package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
final class UsageStatsProtoV2 {
    private static final long ONE_HOUR_MS = java.util.concurrent.TimeUnit.HOURS.toMillis(1);
    private static final java.lang.String TAG = "UsageStatsProtoV2";

    private UsageStatsProtoV2() {
    }

    private static android.app.usage.UsageStats parseUsageStats(android.util.proto.ProtoInputStream proto, long beginTime) throws java.io.IOException {
        android.app.usage.UsageStats stats = new android.app.usage.UsageStats();
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return stats;
                case 1:
                    stats.mPackageToken = proto.readInt(1120986464257L) - 1;
                    break;
                case 3:
                    stats.mLastTimeUsed = proto.readLong(1112396529667L) + beginTime;
                    break;
                case 4:
                    stats.mTotalTimeInForeground = proto.readLong(1112396529668L);
                    break;
                case 6:
                    stats.mAppLaunchCount = proto.readInt(1120986464262L);
                    break;
                case 7:
                    try {
                        long token = proto.start(2246267895815L);
                        loadChooserCounts(proto, stats);
                        proto.end(token);
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, "Unable to read chooser counts for " + stats.mPackageToken);
                    }
                    break;
                case 8:
                    stats.mLastTimeForegroundServiceUsed = proto.readLong(1112396529672L) + beginTime;
                    break;
                case 9:
                    stats.mTotalTimeForegroundServiceUsed = proto.readLong(1112396529673L);
                    break;
                case 10:
                    stats.mLastTimeVisible = proto.readLong(1112396529674L) + beginTime;
                    break;
                case 11:
                    stats.mTotalTimeVisible = proto.readLong(1112396529675L);
                    break;
                case 12:
                    stats.mLastTimeComponentUsed = proto.readLong(1112396529676L) + beginTime;
                    break;
                case 13:
                    try {
                        stats.mErrorCount = proto.readInt(1120986464269L);
                    } catch (java.lang.IllegalArgumentException e2) {
                        stats.mErrorCount = 0;
                        android.util.Slog.e(TAG, "IllegalArgumentException Unable to read mErrorCount" + e2);
                    }
                    break;
            }
        }
    }

    private static void loadCountAndTime(android.util.proto.ProtoInputStream proto, long fieldId, com.android.server.usage.IntervalStats.EventTracker tracker) {
        long token;
        try {
            token = proto.start(fieldId);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Unable to read event tracker " + fieldId, e);
            return;
        }
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    proto.end(token);
                    return;
                case 0:
                default:
                    continue;
                case 1:
                    tracker.count = proto.readInt(1120986464257L);
                    continue;
                case 2:
                    tracker.duration = proto.readLong(1112396529666L);
                    continue;
            }
            android.util.Slog.e(TAG, "Unable to read event tracker " + fieldId, e);
            return;
        }
    }

    private static void loadChooserCounts(android.util.proto.ProtoInputStream proto, android.app.usage.UsageStats usageStats) throws java.io.IOException {
        android.util.SparseIntArray counts;
        if (proto.nextField(1120986464257L)) {
            int actionToken = proto.readInt(1120986464257L) - 1;
            counts = (android.util.SparseIntArray) usageStats.mChooserCountsObfuscated.get(actionToken);
            if (counts == null) {
                counts = new android.util.SparseIntArray();
                usageStats.mChooserCountsObfuscated.put(actionToken, counts);
            }
        } else {
            counts = new android.util.SparseIntArray();
        }
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return;
                case 1:
                    usageStats.mChooserCountsObfuscated.put(proto.readInt(1120986464257L) - 1, counts);
                    break;
                case 2:
                    long token = proto.start(2246267895810L);
                    loadCountsForAction(proto, counts);
                    proto.end(token);
                    break;
            }
        }
    }

    private static void loadCountsForAction(android.util.proto.ProtoInputStream proto, android.util.SparseIntArray counts) throws java.io.IOException {
        int categoryToken = -1;
        int count = 0;
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    if (categoryToken != -1) {
                        counts.put(categoryToken, count);
                        return;
                    }
                    return;
                case 1:
                    int categoryToken2 = proto.readInt(1120986464257L) - 1;
                    categoryToken = categoryToken2;
                    break;
                case 2:
                    count = proto.readInt(1120986464258L);
                    break;
            }
        }
    }

    private static void loadConfigStats(android.util.proto.ProtoInputStream proto, com.android.server.usage.IntervalStats stats) throws java.io.IOException {
        boolean configActive = false;
        android.content.res.Configuration config = new android.content.res.Configuration();
        android.app.usage.ConfigurationStats configStats = new android.app.usage.ConfigurationStats();
        if (proto.nextField(1146756268033L)) {
            config.readFromProto(proto, 1146756268033L);
            configStats = stats.getOrCreateConfigurationStats(config);
        }
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    if (configActive) {
                        stats.activeConfiguration = configStats.mConfiguration;
                        return;
                    }
                    return;
                case 1:
                    config.readFromProto(proto, 1146756268033L);
                    android.app.usage.ConfigurationStats temp = stats.getOrCreateConfigurationStats(config);
                    temp.mLastTimeActive = configStats.mLastTimeActive;
                    temp.mTotalTimeActive = configStats.mTotalTimeActive;
                    temp.mActivationCount = configStats.mActivationCount;
                    configStats = temp;
                    break;
                case 2:
                    configStats.mLastTimeActive = stats.beginTime + proto.readLong(1112396529666L);
                    break;
                case 3:
                    configStats.mTotalTimeActive = proto.readLong(1112396529667L);
                    break;
                case 4:
                    configStats.mActivationCount = proto.readInt(1120986464260L);
                    break;
                case 5:
                    configActive = proto.readBoolean(1133871366149L);
                    break;
            }
        }
    }

    private static android.app.usage.UsageEvents.Event parseEvent(android.util.proto.ProtoInputStream proto, long beginTime) throws java.io.IOException {
        android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event();
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    if (event.mPackageToken == -1) {
                        return null;
                    }
                    return event;
                case 1:
                    event.mPackageToken = proto.readInt(1120986464257L) - 1;
                    break;
                case 2:
                    event.mClassToken = proto.readInt(1120986464258L) - 1;
                    break;
                case 3:
                    event.mTimeStamp = proto.readLong(1112396529667L) + beginTime;
                    break;
                case 4:
                    event.mFlags = proto.readInt(1120986464260L);
                    break;
                case 5:
                    event.mEventType = proto.readInt(1120986464261L);
                    break;
                case 6:
                    event.mConfiguration = new android.content.res.Configuration();
                    event.mConfiguration.readFromProto(proto, 1146756268038L);
                    break;
                case 7:
                    event.mShortcutIdToken = proto.readInt(1120986464263L) - 1;
                    break;
                case 8:
                    event.mBucketAndReason = proto.readInt(1120986464264L);
                    break;
                case 9:
                    event.mNotificationChannelIdToken = proto.readInt(1120986464265L) - 1;
                    break;
                case 10:
                    event.mInstanceId = proto.readInt(1120986464266L);
                    break;
                case 11:
                    event.mTaskRootPackageToken = proto.readInt(1120986464267L) - 1;
                    break;
                case 12:
                    event.mTaskRootClassToken = proto.readInt(1120986464268L) - 1;
                    break;
                case 13:
                    event.mLocusIdToken = proto.readInt(1120986464269L) - 1;
                    break;
                case 14:
                    try {
                        long interactionExtrasToken = proto.start(1146756268046L);
                        event.mUserInteractionExtrasToken = parseUserInteractionEventExtras(proto);
                        proto.end(interactionExtrasToken);
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, "Unable to read some user interaction extras from proto.", e);
                    }
                    break;
            }
        }
    }

    static void writeOffsetTimestamp(android.util.proto.ProtoOutputStream proto, long fieldId, long timestamp, long beginTime) {
        long rolloverGracePeriod = beginTime - ONE_HOUR_MS;
        if (timestamp > rolloverGracePeriod) {
            proto.write(fieldId, getOffsetTimestamp(timestamp, beginTime));
        }
    }

    static long getOffsetTimestamp(long timestamp, long offset) {
        long offsetTimestamp = timestamp - offset;
        return offsetTimestamp == 0 ? 1 + offsetTimestamp : offsetTimestamp;
    }

    private static void writeUsageStats(android.util.proto.ProtoOutputStream proto, long beginTime, android.app.usage.UsageStats stats) throws java.lang.IllegalArgumentException {
        proto.write(1120986464257L, stats.mPackageToken + 1);
        writeOffsetTimestamp(proto, 1112396529667L, stats.mLastTimeUsed, beginTime);
        proto.write(1112396529668L, stats.mTotalTimeInForeground);
        writeOffsetTimestamp(proto, 1112396529672L, stats.mLastTimeForegroundServiceUsed, beginTime);
        proto.write(1112396529673L, stats.mTotalTimeForegroundServiceUsed);
        writeOffsetTimestamp(proto, 1112396529674L, stats.mLastTimeVisible, beginTime);
        proto.write(1112396529675L, stats.mTotalTimeVisible);
        writeOffsetTimestamp(proto, 1112396529676L, stats.mLastTimeComponentUsed, beginTime);
        proto.write(1120986464262L, stats.mAppLaunchCount);
        proto.write(1120986464269L, stats.mErrorCount);
        try {
            writeChooserCounts(proto, stats);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Unable to write chooser counts for " + stats.mPackageName, e);
        }
    }

    private static void writeCountAndTime(android.util.proto.ProtoOutputStream proto, long fieldId, int count, long time) throws java.lang.IllegalArgumentException {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, count);
        proto.write(1112396529666L, time);
        proto.end(token);
    }

    private static void writeChooserCounts(android.util.proto.ProtoOutputStream proto, android.app.usage.UsageStats stats) throws java.lang.IllegalArgumentException {
        if (stats == null || stats.mChooserCountsObfuscated.size() == 0) {
            return;
        }
        int chooserCountSize = stats.mChooserCountsObfuscated.size();
        for (int i = 0; i < chooserCountSize; i++) {
            int action = stats.mChooserCountsObfuscated.keyAt(i);
            android.util.SparseIntArray counts = (android.util.SparseIntArray) stats.mChooserCountsObfuscated.valueAt(i);
            if (counts != null && counts.size() != 0) {
                long token = proto.start(2246267895815L);
                proto.write(1120986464257L, action + 1);
                writeCountsForAction(proto, counts);
                proto.end(token);
            }
        }
    }

    private static void writeCountsForAction(android.util.proto.ProtoOutputStream proto, android.util.SparseIntArray counts) throws java.lang.IllegalArgumentException {
        int countsSize = counts.size();
        for (int i = 0; i < countsSize; i++) {
            int category = counts.keyAt(i);
            int count = counts.valueAt(i);
            if (count > 0) {
                long token = proto.start(2246267895810L);
                proto.write(1120986464257L, category + 1);
                proto.write(1120986464258L, count);
                proto.end(token);
            }
        }
    }

    private static void writeConfigStats(android.util.proto.ProtoOutputStream proto, long statsBeginTime, android.app.usage.ConfigurationStats configStats, boolean isActive) throws java.lang.IllegalArgumentException {
        configStats.mConfiguration.dumpDebug(proto, 1146756268033L);
        writeOffsetTimestamp(proto, 1112396529666L, configStats.mLastTimeActive, statsBeginTime);
        proto.write(1112396529667L, configStats.mTotalTimeActive);
        proto.write(1120986464260L, configStats.mActivationCount);
        proto.write(1133871366149L, isActive);
    }

    private static void writeEvent(android.util.proto.ProtoOutputStream proto, long statsBeginTime, android.app.usage.UsageEvents.Event event) throws java.io.IOException, java.lang.IllegalArgumentException {
        proto.write(1120986464257L, event.mPackageToken + 1);
        if (event.mClassToken != -1) {
            proto.write(1120986464258L, event.mClassToken + 1);
        }
        writeOffsetTimestamp(proto, 1112396529667L, event.mTimeStamp, statsBeginTime);
        proto.write(1120986464260L, event.mFlags);
        proto.write(1120986464261L, event.mEventType);
        proto.write(1120986464266L, event.mInstanceId);
        if (event.mTaskRootPackageToken != -1) {
            proto.write(1120986464267L, event.mTaskRootPackageToken + 1);
        }
        if (event.mTaskRootClassToken != -1) {
            proto.write(1120986464268L, event.mTaskRootClassToken + 1);
        }
        switch (event.mEventType) {
            case 5:
                if (event.mConfiguration != null) {
                    event.mConfiguration.dumpDebug(proto, 1146756268038L);
                }
                break;
            case 7:
                if (event.mUserInteractionExtrasToken != null) {
                    writeUserInteractionEventExtras(proto, 1146756268046L, event.mUserInteractionExtrasToken);
                }
                break;
            case 8:
                if (event.mShortcutIdToken != -1) {
                    proto.write(1120986464263L, event.mShortcutIdToken + 1);
                }
                break;
            case 11:
                if (event.mBucketAndReason != 0) {
                    proto.write(1120986464264L, event.mBucketAndReason);
                }
                break;
            case 12:
                if (event.mNotificationChannelIdToken != -1) {
                    proto.write(1120986464265L, event.mNotificationChannelIdToken + 1);
                }
                break;
            case 30:
                if (event.mLocusIdToken != -1) {
                    proto.write(1120986464269L, event.mLocusIdToken + 1);
                }
                break;
        }
    }

    public static void read(java.io.InputStream in, com.android.server.usage.IntervalStats stats, boolean skipEvents) throws java.io.IOException {
        android.util.proto.ProtoInputStream proto = new android.util.proto.ProtoInputStream(in);
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    int usageStatsSize = stats.packageStatsObfuscated.size();
                    for (int i = 0; i < usageStatsSize; i++) {
                        android.app.usage.UsageStats usageStats = stats.packageStatsObfuscated.valueAt(i);
                        usageStats.mBeginTimeStamp = stats.beginTime;
                        usageStats.mEndTimeStamp = stats.endTime;
                    }
                    return;
                case 1:
                    stats.endTime = stats.beginTime + proto.readLong(1112396529665L);
                    break;
                case 2:
                    stats.majorVersion = proto.readInt(1120986464258L);
                    break;
                case 3:
                    stats.minorVersion = proto.readInt(1120986464259L);
                    break;
                case 10:
                    loadCountAndTime(proto, 1146756268042L, stats.interactiveTracker);
                    break;
                case 11:
                    loadCountAndTime(proto, 1146756268043L, stats.nonInteractiveTracker);
                    break;
                case 12:
                    loadCountAndTime(proto, 1146756268044L, stats.keyguardShownTracker);
                    break;
                case 13:
                    loadCountAndTime(proto, 1146756268045L, stats.keyguardHiddenTracker);
                    break;
                case 20:
                    try {
                        long packagesToken = proto.start(2246267895828L);
                        android.app.usage.UsageStats usageStats2 = parseUsageStats(proto, stats.beginTime);
                        proto.end(packagesToken);
                        if (usageStats2.mPackageToken != -1) {
                            stats.packageStatsObfuscated.put(usageStats2.mPackageToken, usageStats2);
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, "Unable to read some usage stats from proto.", e);
                    }
                    break;
                case 21:
                    try {
                        long configsToken = proto.start(2246267895829L);
                        loadConfigStats(proto, stats);
                        proto.end(configsToken);
                    } catch (java.io.IOException e2) {
                        android.util.Slog.e(TAG, "Unable to read some configuration stats from proto.", e2);
                    }
                    break;
                case 22:
                    if (!skipEvents) {
                        try {
                            long eventsToken = proto.start(2246267895830L);
                            android.app.usage.UsageEvents.Event event = parseEvent(proto, stats.beginTime);
                            proto.end(eventsToken);
                            if (event != null) {
                                stats.events.insert(event);
                            }
                        } catch (java.io.IOException e3) {
                            android.util.Slog.e(TAG, "Unable to read some events from proto.", e3);
                        }
                    }
                    break;
            }
        }
    }

    public static void write(java.io.OutputStream out, com.android.server.usage.IntervalStats stats) throws java.io.IOException, java.lang.IllegalArgumentException {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(out);
        proto.write(1112396529665L, getOffsetTimestamp(stats.endTime, stats.beginTime));
        proto.write(1120986464258L, stats.majorVersion);
        proto.write(1120986464259L, stats.minorVersion);
        try {
            writeCountAndTime(proto, 1146756268042L, stats.interactiveTracker.count, stats.interactiveTracker.duration);
            writeCountAndTime(proto, 1146756268043L, stats.nonInteractiveTracker.count, stats.nonInteractiveTracker.duration);
            writeCountAndTime(proto, 1146756268044L, stats.keyguardShownTracker.count, stats.keyguardShownTracker.duration);
            writeCountAndTime(proto, 1146756268045L, stats.keyguardHiddenTracker.count, stats.keyguardHiddenTracker.duration);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Unable to write some interval stats trackers to proto.", e);
        }
        int statsCount = stats.packageStatsObfuscated.size();
        for (int i = 0; i < statsCount; i++) {
            try {
                long token = proto.start(2246267895828L);
                writeUsageStats(proto, stats.beginTime, stats.packageStatsObfuscated.valueAt(i));
                proto.end(token);
            } catch (java.lang.IllegalArgumentException e2) {
                android.util.Slog.e(TAG, "Unable to write some usage stats to proto.", e2);
            }
        }
        int configCount = stats.configurations.size();
        for (int i2 = 0; i2 < configCount; i2++) {
            boolean active = stats.activeConfiguration.equals(stats.configurations.keyAt(i2));
            try {
                long token2 = proto.start(2246267895829L);
                writeConfigStats(proto, stats.beginTime, stats.configurations.valueAt(i2), active);
                proto.end(token2);
            } catch (java.lang.IllegalArgumentException e3) {
                android.util.Slog.e(TAG, "Unable to write some configuration stats to proto.", e3);
            }
        }
        int eventCount = stats.events.size();
        for (int i3 = 0; i3 < eventCount; i3++) {
            try {
                long token3 = proto.start(2246267895830L);
                writeEvent(proto, stats.beginTime, stats.events.get(i3));
                proto.end(token3);
            } catch (java.lang.IllegalArgumentException e4) {
                android.util.Slog.e(TAG, "Unable to write some events to proto.", e4);
            }
        }
        proto.flush();
    }

    private static void loadPackagesMap(android.util.proto.ProtoInputStream proto, android.util.SparseArray<java.util.ArrayList<java.lang.String>> tokensToPackagesMap) throws java.io.IOException {
        int key = -1;
        java.util.ArrayList<java.lang.String> strings = new java.util.ArrayList<>();
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    if (key != -1) {
                        tokensToPackagesMap.put(key, strings);
                        return;
                    }
                    return;
                case 1:
                    int key2 = proto.readInt(1120986464257L) - 1;
                    key = key2;
                    break;
                case 2:
                    strings.add(proto.readString(2237677961218L));
                    break;
            }
        }
    }

    static void readObfuscatedData(java.io.InputStream in, com.android.server.usage.PackagesTokenData packagesTokenData) throws java.io.IOException {
        android.util.proto.ProtoInputStream proto = new android.util.proto.ProtoInputStream(in);
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return;
                case 1:
                    packagesTokenData.counter = proto.readInt(1120986464257L);
                    break;
                case 2:
                    long token = proto.start(2246267895810L);
                    loadPackagesMap(proto, packagesTokenData.tokensToPackagesMap);
                    proto.end(token);
                    break;
            }
        }
    }

    static void writeObfuscatedData(java.io.OutputStream out, com.android.server.usage.PackagesTokenData packagesTokenData) throws java.io.IOException, java.lang.IllegalArgumentException {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(out);
        proto.write(1120986464257L, packagesTokenData.counter);
        int mapSize = packagesTokenData.tokensToPackagesMap.size();
        for (int i = 0; i < mapSize; i++) {
            long token = proto.start(2246267895810L);
            int packageToken = packagesTokenData.tokensToPackagesMap.keyAt(i);
            proto.write(1120986464257L, packageToken + 1);
            java.util.ArrayList<java.lang.String> strings = packagesTokenData.tokensToPackagesMap.valueAt(i);
            int listSize = strings.size();
            for (int j = 0; j < listSize; j++) {
                proto.write(2237677961218L, strings.get(j));
            }
            proto.end(token);
        }
        proto.flush();
    }

    private static android.app.usage.UsageEvents.Event parsePendingEvent(android.util.proto.ProtoInputStream proto) throws java.io.IOException {
        android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event();
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    switch (event.mEventType) {
                        case 5:
                            if (event.mConfiguration == null) {
                                event.mConfiguration = new android.content.res.Configuration();
                            }
                            break;
                        case 8:
                            if (event.mShortcutId == null) {
                                event.mShortcutId = "";
                            }
                            break;
                        case 12:
                            if (event.mNotificationChannelId == null) {
                                event.mNotificationChannelId = "";
                            }
                            break;
                    }
                    if (event.mPackage == null) {
                        return null;
                    }
                    return event;
                case 1:
                    event.mPackage = proto.readString(1138166333441L);
                    break;
                case 2:
                    event.mClass = proto.readString(1138166333442L);
                    break;
                case 3:
                    event.mTimeStamp = proto.readLong(1112396529667L);
                    break;
                case 4:
                    event.mFlags = proto.readInt(1120986464260L);
                    break;
                case 5:
                    event.mEventType = proto.readInt(1120986464261L);
                    break;
                case 6:
                    event.mConfiguration = new android.content.res.Configuration();
                    event.mConfiguration.readFromProto(proto, 1146756268038L);
                    break;
                case 7:
                    event.mShortcutId = proto.readString(1138166333447L);
                    break;
                case 8:
                    event.mBucketAndReason = proto.readInt(1120986464264L);
                    break;
                case 9:
                    event.mNotificationChannelId = proto.readString(1138166333449L);
                    break;
                case 10:
                    event.mInstanceId = proto.readInt(1120986464266L);
                    break;
                case 11:
                    event.mTaskRootPackage = proto.readString(1138166333451L);
                    break;
                case 12:
                    event.mTaskRootClass = proto.readString(1138166333452L);
                    break;
                case 14:
                    event.mExtras = parsePendingEventExtras(proto, 1151051235342L);
                    break;
            }
        }
    }

    static void readPendingEvents(java.io.InputStream in, java.util.LinkedList<android.app.usage.UsageEvents.Event> events) throws java.io.IOException {
        android.util.proto.ProtoInputStream proto = new android.util.proto.ProtoInputStream(in);
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return;
                case 23:
                    try {
                        long token = proto.start(2246267895831L);
                        android.app.usage.UsageEvents.Event event = parsePendingEvent(proto);
                        proto.end(token);
                        if (event != null) {
                            events.add(event);
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, "Unable to parse some pending events from proto.", e);
                    }
                    break;
            }
        }
    }

    private static void writePendingEvent(android.util.proto.ProtoOutputStream proto, android.app.usage.UsageEvents.Event event) throws java.io.IOException, java.lang.IllegalArgumentException {
        proto.write(1138166333441L, event.mPackage);
        if (event.mClass != null) {
            proto.write(1138166333442L, event.mClass);
        }
        proto.write(1112396529667L, event.mTimeStamp);
        proto.write(1120986464260L, event.mFlags);
        proto.write(1120986464261L, event.mEventType);
        proto.write(1120986464266L, event.mInstanceId);
        if (event.mTaskRootPackage != null) {
            proto.write(1138166333451L, event.mTaskRootPackage);
        }
        if (event.mTaskRootClass != null) {
            proto.write(1138166333452L, event.mTaskRootClass);
        }
        switch (event.mEventType) {
            case 5:
                if (event.mConfiguration != null) {
                    event.mConfiguration.dumpDebug(proto, 1146756268038L);
                }
                break;
            case 7:
                if (event.mExtras != null && event.mExtras.size() != 0) {
                    writePendingEventExtras(proto, 1151051235342L, event.mExtras);
                    break;
                }
                break;
            case 8:
                if (event.mShortcutId != null) {
                    proto.write(1138166333447L, event.mShortcutId);
                }
                break;
            case 11:
                if (event.mBucketAndReason != 0) {
                    proto.write(1120986464264L, event.mBucketAndReason);
                }
                break;
            case 12:
                if (event.mNotificationChannelId != null) {
                    proto.write(1138166333449L, event.mNotificationChannelId);
                }
                break;
        }
    }

    static void writePendingEvents(java.io.OutputStream out, java.util.LinkedList<android.app.usage.UsageEvents.Event> events) throws java.io.IOException, java.lang.IllegalArgumentException {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(out);
        int eventCount = events.size();
        for (int i = 0; i < eventCount; i++) {
            try {
                long token = proto.start(2246267895831L);
                writePendingEvent(proto, events.get(i));
                proto.end(token);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.e(TAG, "Unable to write some pending events to proto.", e);
            }
        }
        proto.flush();
    }

    private static android.util.Pair<java.lang.String, java.lang.Long> parseGlobalComponentUsage(android.util.proto.ProtoInputStream proto) throws java.io.IOException {
        java.lang.String packageName = "";
        long time = 0;
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return new android.util.Pair<>(packageName, java.lang.Long.valueOf(time));
                case 1:
                    packageName = proto.readString(1138166333441L);
                    break;
                case 2:
                    time = proto.readLong(1112396529666L);
                    break;
            }
        }
    }

    static void readGlobalComponentUsage(java.io.InputStream in, java.util.Map<java.lang.String, java.lang.Long> lastTimeComponentUsedGlobal) throws java.io.IOException {
        android.util.proto.ProtoInputStream proto = new android.util.proto.ProtoInputStream(in);
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return;
                case 24:
                    try {
                        long token = proto.start(2246267895832L);
                        android.util.Pair<java.lang.String, java.lang.Long> usage = parseGlobalComponentUsage(proto);
                        proto.end(token);
                        if (!((java.lang.String) usage.first).isEmpty() && ((java.lang.Long) usage.second).longValue() > 0) {
                            lastTimeComponentUsedGlobal.put((java.lang.String) usage.first, (java.lang.Long) usage.second);
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, "Unable to parse some package usage from proto.", e);
                    }
                    break;
            }
        }
    }

    static void writeGlobalComponentUsage(java.io.OutputStream out, java.util.Map<java.lang.String, java.lang.Long> lastTimeComponentUsedGlobal) {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(out);
        java.util.Map.Entry<java.lang.String, java.lang.Long>[] entries = (java.util.Map.Entry[]) lastTimeComponentUsedGlobal.entrySet().toArray();
        int size = entries.length;
        for (int i = 0; i < size; i++) {
            if (entries[i].getValue().longValue() > 0) {
                long token = proto.start(2246267895832L);
                proto.write(1138166333441L, entries[i].getKey());
                proto.write(1112396529666L, entries[i].getValue().longValue());
                proto.end(token);
            }
        }
    }

    private static android.app.usage.UsageEvents.Event.UserInteractionEventExtrasToken parseUserInteractionEventExtras(android.util.proto.ProtoInputStream proto) throws java.io.IOException {
        android.app.usage.UsageEvents.Event.UserInteractionEventExtrasToken interactionExtrasToken = new android.app.usage.UsageEvents.Event.UserInteractionEventExtrasToken();
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    return interactionExtrasToken;
                case 1:
                    interactionExtrasToken.mCategoryToken = proto.readInt(1120986464257L) - 1;
                    break;
                case 2:
                    interactionExtrasToken.mActionToken = proto.readInt(1120986464258L) - 1;
                    break;
            }
        }
    }

    static void writeUserInteractionEventExtras(android.util.proto.ProtoOutputStream proto, long fieldId, android.app.usage.UsageEvents.Event.UserInteractionEventExtrasToken interactionExtras) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, interactionExtras.mCategoryToken + 1);
        proto.write(1120986464258L, interactionExtras.mActionToken + 1);
        proto.end(token);
    }

    private static android.os.PersistableBundle parsePendingEventExtras(android.util.proto.ProtoInputStream proto, long fieldId) throws java.io.IOException {
        return android.os.PersistableBundle.readFromStream(new java.io.ByteArrayInputStream(proto.readBytes(fieldId)));
    }

    static void writePendingEventExtras(android.util.proto.ProtoOutputStream proto, long fieldId, android.os.PersistableBundle eventExtras) throws java.io.IOException {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        eventExtras.writeToStream(baos);
        proto.write(fieldId, baos.toByteArray());
    }
}
