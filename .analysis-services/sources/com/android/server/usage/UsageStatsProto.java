package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
final class UsageStatsProto {
    private static java.lang.String TAG = "UsageStatsProto";

    private UsageStatsProto() {
    }

    private static java.util.List<java.lang.String> readStringPool(android.util.proto.ProtoInputStream proto) throws java.io.IOException {
        java.util.List<java.lang.String> stringPool;
        long token = proto.start(1146756268034L);
        if (proto.nextField(1120986464257L)) {
            stringPool = new java.util.ArrayList<>(proto.readInt(1120986464257L));
        } else {
            stringPool = new java.util.ArrayList<>();
        }
        while (proto.nextField() != -1) {
            switch (proto.getFieldNumber()) {
                case 2:
                    stringPool.add(proto.readString(2237677961218L));
                    break;
            }
        }
        proto.end(token);
        return stringPool;
    }

    private static void loadUsageStats(android.util.proto.ProtoInputStream proto, long fieldId, com.android.server.usage.IntervalStats statsOut, java.util.List<java.lang.String> stringPool) throws java.io.IOException {
        android.app.usage.UsageStats stats;
        long token = proto.start(fieldId);
        if (proto.nextField(1120986464258L)) {
            stats = statsOut.getOrCreateUsageStats(stringPool.get(proto.readInt(1120986464258L) - 1));
        } else if (proto.nextField(1138166333441L)) {
            stats = statsOut.getOrCreateUsageStats(proto.readString(1138166333441L));
        } else {
            stats = new android.app.usage.UsageStats();
        }
        while (proto.nextField() != -1) {
            switch (proto.getFieldNumber()) {
                case 1:
                    android.app.usage.UsageStats tempPackage = statsOut.getOrCreateUsageStats(proto.readString(1138166333441L));
                    tempPackage.mLastTimeUsed = stats.mLastTimeUsed;
                    tempPackage.mTotalTimeInForeground = stats.mTotalTimeInForeground;
                    tempPackage.mLastEvent = stats.mLastEvent;
                    tempPackage.mAppLaunchCount = stats.mAppLaunchCount;
                    stats = tempPackage;
                    break;
                case 2:
                    android.app.usage.UsageStats tempPackageIndex = statsOut.getOrCreateUsageStats(stringPool.get(proto.readInt(1120986464258L) - 1));
                    tempPackageIndex.mLastTimeUsed = stats.mLastTimeUsed;
                    tempPackageIndex.mTotalTimeInForeground = stats.mTotalTimeInForeground;
                    tempPackageIndex.mLastEvent = stats.mLastEvent;
                    tempPackageIndex.mAppLaunchCount = stats.mAppLaunchCount;
                    stats = tempPackageIndex;
                    break;
                case 3:
                    stats.mLastTimeUsed = statsOut.beginTime + proto.readLong(1112396529667L);
                    break;
                case 4:
                    stats.mTotalTimeInForeground = proto.readLong(1112396529668L);
                    break;
                case 5:
                    stats.mLastEvent = proto.readInt(1120986464261L);
                    break;
                case 6:
                    stats.mAppLaunchCount = proto.readInt(1120986464262L);
                    break;
                case 7:
                    try {
                        long chooserToken = proto.start(2246267895815L);
                        loadChooserCounts(proto, stats);
                        proto.end(chooserToken);
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, "Unable to read chooser counts for " + stats.mPackageName, e);
                    }
                    break;
                case 8:
                    stats.mLastTimeForegroundServiceUsed = statsOut.beginTime + proto.readLong(1112396529672L);
                    break;
                case 9:
                    stats.mTotalTimeForegroundServiceUsed = proto.readLong(1112396529673L);
                    break;
                case 10:
                    stats.mLastTimeVisible = statsOut.beginTime + proto.readLong(1112396529674L);
                    break;
                case 11:
                    stats.mTotalTimeVisible = proto.readLong(1112396529675L);
                    break;
                case 12:
                    stats.mLastTimeComponentUsed = statsOut.beginTime + proto.readLong(1112396529676L);
                    break;
            }
        }
        proto.end(token);
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
        android.util.ArrayMap<java.lang.String, java.lang.Integer> counts;
        if (usageStats.mChooserCounts == null) {
            usageStats.mChooserCounts = new android.util.ArrayMap();
        }
        java.lang.String action = null;
        if (proto.nextField(1138166333441L)) {
            action = proto.readString(1138166333441L);
            counts = (android.util.ArrayMap) usageStats.mChooserCounts.get(action);
            if (counts == null) {
                counts = new android.util.ArrayMap<>();
                usageStats.mChooserCounts.put(action, counts);
            }
        } else {
            counts = new android.util.ArrayMap<>();
        }
        while (true) {
            switch (proto.nextField()) {
                case 1:
                    action = proto.readString(1138166333441L);
                    usageStats.mChooserCounts.put(action, counts);
                    continue;
                case 3:
                    long token = proto.start(2246267895811L);
                    loadCountsForAction(proto, counts);
                    proto.end(token);
                    break;
            }
        }
        if (action == null) {
            usageStats.mChooserCounts.put("", counts);
        }
    }

    private static void loadCountsForAction(android.util.proto.ProtoInputStream proto, android.util.ArrayMap<java.lang.String, java.lang.Integer> counts) throws java.io.IOException {
        java.lang.String category = null;
        int count = 0;
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    if (category == null) {
                        counts.put("", java.lang.Integer.valueOf(count));
                        return;
                    } else {
                        counts.put(category, java.lang.Integer.valueOf(count));
                        return;
                    }
                case 1:
                    category = proto.readString(1138166333441L);
                    break;
                case 3:
                    count = proto.readInt(1120986464259L);
                    break;
            }
        }
    }

    private static void loadConfigStats(android.util.proto.ProtoInputStream proto, long fieldId, com.android.server.usage.IntervalStats statsOut) throws java.io.IOException {
        android.app.usage.ConfigurationStats configStats;
        long token = proto.start(fieldId);
        boolean configActive = false;
        android.content.res.Configuration config = new android.content.res.Configuration();
        if (proto.nextField(1146756268033L)) {
            config.readFromProto(proto, 1146756268033L);
            configStats = statsOut.getOrCreateConfigurationStats(config);
        } else {
            configStats = new android.app.usage.ConfigurationStats();
        }
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    if (configActive) {
                        statsOut.activeConfiguration = configStats.mConfiguration;
                    }
                    proto.end(token);
                    return;
                case 1:
                    config.readFromProto(proto, 1146756268033L);
                    android.app.usage.ConfigurationStats temp = statsOut.getOrCreateConfigurationStats(config);
                    temp.mLastTimeActive = configStats.mLastTimeActive;
                    temp.mTotalTimeActive = configStats.mTotalTimeActive;
                    temp.mActivationCount = configStats.mActivationCount;
                    configStats = temp;
                    break;
                case 2:
                    configStats.mLastTimeActive = statsOut.beginTime + proto.readLong(1112396529666L);
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

    private static void loadEvent(android.util.proto.ProtoInputStream proto, long fieldId, com.android.server.usage.IntervalStats statsOut, java.util.List<java.lang.String> stringPool) throws java.io.IOException {
        long token = proto.start(fieldId);
        android.app.usage.UsageEvents.Event event = statsOut.buildEvent(proto, stringPool);
        proto.end(token);
        if (event.mPackage == null) {
            throw new java.net.ProtocolException("no package field present");
        }
        statsOut.events.insert(event);
    }

    private static void writeStringPool(android.util.proto.ProtoOutputStream proto, com.android.server.usage.IntervalStats stats) throws java.lang.IllegalArgumentException {
        long token = proto.start(1146756268034L);
        int size = stats.mStringCache.size();
        proto.write(1120986464257L, size);
        for (int i = 0; i < size; i++) {
            proto.write(2237677961218L, stats.mStringCache.valueAt(i));
        }
        proto.end(token);
    }

    private static void writeUsageStats(android.util.proto.ProtoOutputStream proto, long fieldId, com.android.server.usage.IntervalStats stats, android.app.usage.UsageStats usageStats) throws java.lang.IllegalArgumentException {
        long token = proto.start(fieldId);
        int packageIndex = stats.mStringCache.indexOf(usageStats.mPackageName);
        if (packageIndex >= 0) {
            proto.write(1120986464258L, packageIndex + 1);
        } else {
            proto.write(1138166333441L, usageStats.mPackageName);
        }
        com.android.server.usage.UsageStatsProtoV2.writeOffsetTimestamp(proto, 1112396529667L, usageStats.mLastTimeUsed, stats.beginTime);
        proto.write(1112396529668L, usageStats.mTotalTimeInForeground);
        proto.write(1120986464261L, usageStats.mLastEvent);
        com.android.server.usage.UsageStatsProtoV2.writeOffsetTimestamp(proto, 1112396529672L, usageStats.mLastTimeForegroundServiceUsed, stats.beginTime);
        proto.write(1112396529673L, usageStats.mTotalTimeForegroundServiceUsed);
        com.android.server.usage.UsageStatsProtoV2.writeOffsetTimestamp(proto, 1112396529674L, usageStats.mLastTimeVisible, stats.beginTime);
        proto.write(1112396529675L, usageStats.mTotalTimeVisible);
        com.android.server.usage.UsageStatsProtoV2.writeOffsetTimestamp(proto, 1112396529676L, usageStats.mLastTimeComponentUsed, stats.beginTime);
        proto.write(1120986464262L, usageStats.mAppLaunchCount);
        try {
            writeChooserCounts(proto, usageStats);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Unable to write chooser counts for " + usageStats.mPackageName, e);
        }
        proto.end(token);
    }

    private static void writeCountAndTime(android.util.proto.ProtoOutputStream proto, long fieldId, int count, long time) throws java.lang.IllegalArgumentException {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, count);
        proto.write(1112396529666L, time);
        proto.end(token);
    }

    private static void writeChooserCounts(android.util.proto.ProtoOutputStream proto, android.app.usage.UsageStats usageStats) throws java.lang.IllegalArgumentException {
        if (usageStats == null || usageStats.mChooserCounts == null || usageStats.mChooserCounts.keySet().isEmpty()) {
            return;
        }
        int chooserCountSize = usageStats.mChooserCounts.size();
        for (int i = 0; i < chooserCountSize; i++) {
            java.lang.String action = (java.lang.String) usageStats.mChooserCounts.keyAt(i);
            android.util.ArrayMap<java.lang.String, java.lang.Integer> counts = (android.util.ArrayMap) usageStats.mChooserCounts.valueAt(i);
            if (action != null && counts != null && !counts.isEmpty()) {
                long token = proto.start(2246267895815L);
                proto.write(1138166333441L, action);
                writeCountsForAction(proto, counts);
                proto.end(token);
            }
        }
    }

    private static void writeCountsForAction(android.util.proto.ProtoOutputStream proto, android.util.ArrayMap<java.lang.String, java.lang.Integer> counts) throws java.lang.IllegalArgumentException {
        int countsSize = counts.size();
        for (int i = 0; i < countsSize; i++) {
            java.lang.String key = counts.keyAt(i);
            int count = counts.valueAt(i).intValue();
            if (count > 0) {
                long token = proto.start(2246267895811L);
                proto.write(1138166333441L, key);
                proto.write(1120986464259L, count);
                proto.end(token);
            }
        }
    }

    private static void writeConfigStats(android.util.proto.ProtoOutputStream proto, long fieldId, com.android.server.usage.IntervalStats stats, android.app.usage.ConfigurationStats configStats, boolean isActive) throws java.lang.IllegalArgumentException {
        long token = proto.start(fieldId);
        configStats.mConfiguration.dumpDebug(proto, 1146756268033L);
        com.android.server.usage.UsageStatsProtoV2.writeOffsetTimestamp(proto, 1112396529666L, configStats.mLastTimeActive, stats.beginTime);
        proto.write(1112396529667L, configStats.mTotalTimeActive);
        proto.write(1120986464260L, configStats.mActivationCount);
        proto.write(1133871366149L, isActive);
        proto.end(token);
    }

    private static void writeEvent(android.util.proto.ProtoOutputStream proto, long fieldId, com.android.server.usage.IntervalStats stats, android.app.usage.UsageEvents.Event event) throws java.lang.IllegalArgumentException {
        int locusIdIndex;
        int taskRootClassIndex;
        int taskRootPackageIndex;
        long token = proto.start(fieldId);
        int packageIndex = stats.mStringCache.indexOf(event.mPackage);
        if (packageIndex >= 0) {
            proto.write(1120986464258L, packageIndex + 1);
        } else {
            proto.write(1138166333441L, event.mPackage);
        }
        if (event.mClass != null) {
            int classIndex = stats.mStringCache.indexOf(event.mClass);
            if (classIndex >= 0) {
                proto.write(1120986464260L, classIndex + 1);
            } else {
                proto.write(1138166333443L, event.mClass);
            }
        }
        com.android.server.usage.UsageStatsProtoV2.writeOffsetTimestamp(proto, 1112396529669L, event.mTimeStamp, stats.beginTime);
        proto.write(1120986464262L, event.mFlags);
        proto.write(1120986464263L, event.mEventType);
        proto.write(1120986464270L, event.mInstanceId);
        if (event.mTaskRootPackage != null && (taskRootPackageIndex = stats.mStringCache.indexOf(event.mTaskRootPackage)) >= 0) {
            proto.write(1120986464271L, taskRootPackageIndex + 1);
        }
        if (event.mTaskRootClass != null && (taskRootClassIndex = stats.mStringCache.indexOf(event.mTaskRootClass)) >= 0) {
            proto.write(1120986464272L, taskRootClassIndex + 1);
        }
        switch (event.mEventType) {
            case 5:
                if (event.mConfiguration != null) {
                    event.mConfiguration.dumpDebug(proto, 1146756268040L);
                }
                break;
            case 8:
                if (event.mShortcutId != null) {
                    proto.write(1138166333449L, event.mShortcutId);
                }
                break;
            case 11:
                if (event.mBucketAndReason != 0) {
                    proto.write(1120986464267L, event.mBucketAndReason);
                }
                break;
            case 12:
                if (event.mNotificationChannelId != null) {
                    int channelIndex = stats.mStringCache.indexOf(event.mNotificationChannelId);
                    if (channelIndex >= 0) {
                        proto.write(1120986464269L, channelIndex + 1);
                    } else {
                        proto.write(1138166333452L, event.mNotificationChannelId);
                    }
                }
                break;
            case 30:
                if (event.mLocusId != null && (locusIdIndex = stats.mStringCache.indexOf(event.mLocusId)) >= 0) {
                    proto.write(1120986464273L, locusIdIndex + 1);
                }
                break;
        }
        proto.end(token);
    }

    public static void read(java.io.InputStream in, com.android.server.usage.IntervalStats statsOut) throws java.io.IOException {
        android.util.proto.ProtoInputStream proto = new android.util.proto.ProtoInputStream(in);
        java.util.List<java.lang.String> stringPool = null;
        statsOut.packageStats.clear();
        statsOut.configurations.clear();
        statsOut.activeConfiguration = null;
        statsOut.events.clear();
        while (true) {
            switch (proto.nextField()) {
                case -1:
                    statsOut.upgradeIfNeeded();
                    return;
                case 1:
                    statsOut.endTime = statsOut.beginTime + proto.readLong(1112396529665L);
                    break;
                case 2:
                    try {
                        stringPool = readStringPool(proto);
                        statsOut.mStringCache.addAll(stringPool);
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, "Unable to read string pool from proto.", e);
                    }
                    break;
                case 3:
                    statsOut.majorVersion = proto.readInt(1120986464259L);
                    break;
                case 4:
                    statsOut.minorVersion = proto.readInt(1120986464260L);
                    break;
                case 10:
                    loadCountAndTime(proto, 1146756268042L, statsOut.interactiveTracker);
                    break;
                case 11:
                    loadCountAndTime(proto, 1146756268043L, statsOut.nonInteractiveTracker);
                    break;
                case 12:
                    loadCountAndTime(proto, 1146756268044L, statsOut.keyguardShownTracker);
                    break;
                case 13:
                    loadCountAndTime(proto, 1146756268045L, statsOut.keyguardHiddenTracker);
                    break;
                case 20:
                    try {
                        loadUsageStats(proto, 2246267895828L, statsOut, stringPool);
                    } catch (java.io.IOException e2) {
                        android.util.Slog.e(TAG, "Unable to read some usage stats from proto.", e2);
                    }
                    break;
                case 21:
                    try {
                        loadConfigStats(proto, 2246267895829L, statsOut);
                    } catch (java.io.IOException e3) {
                        android.util.Slog.e(TAG, "Unable to read some configuration stats from proto.", e3);
                    }
                    break;
                case 22:
                    try {
                        loadEvent(proto, 2246267895830L, statsOut, stringPool);
                    } catch (java.io.IOException e4) {
                        android.util.Slog.e(TAG, "Unable to read some events from proto.", e4);
                    }
                    break;
            }
        }
    }

    public static void write(java.io.OutputStream out, com.android.server.usage.IntervalStats stats) throws java.io.IOException, java.lang.IllegalArgumentException {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(out);
        proto.write(1112396529665L, com.android.server.usage.UsageStatsProtoV2.getOffsetTimestamp(stats.endTime, stats.beginTime));
        proto.write(1120986464259L, stats.majorVersion);
        proto.write(1120986464260L, stats.minorVersion);
        try {
            writeStringPool(proto, stats);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Unable to write string pool to proto.", e);
        }
        try {
            writeCountAndTime(proto, 1146756268042L, stats.interactiveTracker.count, stats.interactiveTracker.duration);
            writeCountAndTime(proto, 1146756268043L, stats.nonInteractiveTracker.count, stats.nonInteractiveTracker.duration);
            writeCountAndTime(proto, 1146756268044L, stats.keyguardShownTracker.count, stats.keyguardShownTracker.duration);
            writeCountAndTime(proto, 1146756268045L, stats.keyguardHiddenTracker.count, stats.keyguardHiddenTracker.duration);
        } catch (java.lang.IllegalArgumentException e2) {
            android.util.Slog.e(TAG, "Unable to write some interval stats trackers to proto.", e2);
        }
        int statsCount = stats.packageStats.size();
        for (int i = 0; i < statsCount; i++) {
            try {
                writeUsageStats(proto, 2246267895828L, stats, stats.packageStats.valueAt(i));
            } catch (java.lang.IllegalArgumentException e3) {
                android.util.Slog.e(TAG, "Unable to write some usage stats to proto.", e3);
            }
        }
        int configCount = stats.configurations.size();
        for (int i2 = 0; i2 < configCount; i2++) {
            boolean active = stats.activeConfiguration.equals(stats.configurations.keyAt(i2));
            try {
                writeConfigStats(proto, 2246267895829L, stats, stats.configurations.valueAt(i2), active);
            } catch (java.lang.IllegalArgumentException e4) {
                android.util.Slog.e(TAG, "Unable to write some configuration stats to proto.", e4);
            }
        }
        int eventCount = stats.events.size();
        for (int i3 = 0; i3 < eventCount; i3++) {
            try {
                writeEvent(proto, 2246267895830L, stats, stats.events.get(i3));
            } catch (java.lang.IllegalArgumentException e5) {
                android.util.Slog.e(TAG, "Unable to write some events to proto.", e5);
            }
        }
        proto.flush();
    }
}
