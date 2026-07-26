package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
final class UsageStatsXmlV1 {
    private static final java.lang.String ACTIVE_ATTR = "active";
    private static final java.lang.String APP_LAUNCH_COUNT_ATTR = "appLaunchCount";
    private static final java.lang.String CATEGORY_TAG = "category";
    private static final java.lang.String CHOOSER_COUNT_TAG = "chosen_action";
    private static final java.lang.String CLASS_ATTR = "class";
    private static final java.lang.String CONFIGURATIONS_TAG = "configurations";
    private static final java.lang.String CONFIG_TAG = "config";
    private static final java.lang.String COUNT = "count";
    private static final java.lang.String COUNT_ATTR = "count";
    private static final java.lang.String END_TIME_ATTR = "endTime";
    private static final java.lang.String EVENT_LOG_TAG = "event-log";
    private static final java.lang.String EVENT_TAG = "event";
    private static final java.lang.String FLAGS_ATTR = "flags";
    private static final java.lang.String INSTANCE_ID_ATTR = "instanceId";
    private static final java.lang.String INTERACTIVE_TAG = "interactive";
    private static final java.lang.String KEYGUARD_HIDDEN_TAG = "keyguard-hidden";
    private static final java.lang.String KEYGUARD_SHOWN_TAG = "keyguard-shown";
    private static final java.lang.String LAST_EVENT_ATTR = "lastEvent";
    private static final java.lang.String LAST_TIME_ACTIVE_ATTR = "lastTimeActive";
    private static final java.lang.String LAST_TIME_SERVICE_USED_ATTR = "lastTimeServiceUsed";
    private static final java.lang.String LAST_TIME_VISIBLE_ATTR = "lastTimeVisible";
    private static final java.lang.String MAJOR_VERSION_ATTR = "majorVersion";
    private static final java.lang.String MINOR_VERSION_ATTR = "minorVersion";
    private static final java.lang.String NAME = "name";
    private static final java.lang.String NON_INTERACTIVE_TAG = "non-interactive";
    private static final java.lang.String NOTIFICATION_CHANNEL_ATTR = "notificationChannel";
    private static final java.lang.String PACKAGES_TAG = "packages";
    private static final java.lang.String PACKAGE_ATTR = "package";
    private static final java.lang.String PACKAGE_TAG = "package";
    private static final java.lang.String SHORTCUT_ID_ATTR = "shortcutId";
    private static final java.lang.String STANDBY_BUCKET_ATTR = "standbyBucket";
    private static final java.lang.String TAG = "UsageStatsXmlV1";
    private static final java.lang.String TIME_ATTR = "time";
    private static final java.lang.String TOTAL_TIME_ACTIVE_ATTR = "timeActive";
    private static final java.lang.String TOTAL_TIME_SERVICE_USED_ATTR = "timeServiceUsed";
    private static final java.lang.String TOTAL_TIME_VISIBLE_ATTR = "timeVisible";
    private static final java.lang.String TYPE_ATTR = "type";

    private static void loadUsageStats(org.xmlpull.v1.XmlPullParser parser, com.android.server.usage.IntervalStats statsOut) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String pkg = parser.getAttributeValue(null, "package");
        if (pkg == null) {
            throw new java.net.ProtocolException("no package attribute present");
        }
        android.app.usage.UsageStats stats = statsOut.getOrCreateUsageStats(pkg);
        stats.mLastTimeUsed = statsOut.beginTime + com.android.internal.util.XmlUtils.readLongAttribute(parser, LAST_TIME_ACTIVE_ATTR);
        try {
            stats.mLastTimeVisible = statsOut.beginTime + com.android.internal.util.XmlUtils.readLongAttribute(parser, LAST_TIME_VISIBLE_ATTR);
        } catch (java.io.IOException e) {
            android.util.Log.i(TAG, "Failed to parse mLastTimeVisible");
        }
        try {
            stats.mLastTimeForegroundServiceUsed = statsOut.beginTime + com.android.internal.util.XmlUtils.readLongAttribute(parser, LAST_TIME_SERVICE_USED_ATTR);
        } catch (java.io.IOException e2) {
            android.util.Log.i(TAG, "Failed to parse mLastTimeForegroundServiceUsed");
        }
        stats.mTotalTimeInForeground = com.android.internal.util.XmlUtils.readLongAttribute(parser, TOTAL_TIME_ACTIVE_ATTR);
        try {
            stats.mTotalTimeVisible = com.android.internal.util.XmlUtils.readLongAttribute(parser, TOTAL_TIME_VISIBLE_ATTR);
        } catch (java.io.IOException e3) {
            android.util.Log.i(TAG, "Failed to parse mTotalTimeVisible");
        }
        try {
            stats.mTotalTimeForegroundServiceUsed = com.android.internal.util.XmlUtils.readLongAttribute(parser, TOTAL_TIME_SERVICE_USED_ATTR);
        } catch (java.io.IOException e4) {
            android.util.Log.i(TAG, "Failed to parse mTotalTimeForegroundServiceUsed");
        }
        stats.mLastEvent = com.android.internal.util.XmlUtils.readIntAttribute(parser, LAST_EVENT_ATTR);
        stats.mAppLaunchCount = com.android.internal.util.XmlUtils.readIntAttribute(parser, APP_LAUNCH_COUNT_ATTR, 0);
        while (true) {
            int eventCode = parser.next();
            if (eventCode != 1) {
                java.lang.String tag = parser.getName();
                if (eventCode != 3 || !tag.equals("package")) {
                    if (eventCode == 2 && tag.equals(CHOOSER_COUNT_TAG)) {
                        java.lang.String action = com.android.internal.util.XmlUtils.readStringAttribute(parser, "name");
                        loadChooserCounts(parser, stats, action);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private static void loadCountAndTime(org.xmlpull.v1.XmlPullParser parser, com.android.server.usage.IntervalStats.EventTracker tracker) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        tracker.count = com.android.internal.util.XmlUtils.readIntAttribute(parser, com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, 0);
        tracker.duration = com.android.internal.util.XmlUtils.readLongAttribute(parser, TIME_ATTR, 0L);
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
    }

    private static void loadChooserCounts(org.xmlpull.v1.XmlPullParser parser, android.app.usage.UsageStats usageStats, java.lang.String action) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (action == null) {
            return;
        }
        if (usageStats.mChooserCounts == null) {
            usageStats.mChooserCounts = new android.util.ArrayMap();
        }
        if (!usageStats.mChooserCounts.containsKey(action)) {
            android.util.ArrayMap<java.lang.String, java.lang.Integer> counts = new android.util.ArrayMap<>();
            usageStats.mChooserCounts.put(action, counts);
        }
        while (true) {
            int eventCode = parser.next();
            if (eventCode != 1) {
                java.lang.String tag = parser.getName();
                if (eventCode != 3 || !tag.equals(CHOOSER_COUNT_TAG)) {
                    if (eventCode == 2 && tag.equals(CATEGORY_TAG)) {
                        java.lang.String category = com.android.internal.util.XmlUtils.readStringAttribute(parser, "name");
                        int count = com.android.internal.util.XmlUtils.readIntAttribute(parser, com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT);
                        ((android.util.ArrayMap) usageStats.mChooserCounts.get(action)).put(category, java.lang.Integer.valueOf(count));
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private static void loadConfigStats(org.xmlpull.v1.XmlPullParser parser, com.android.server.usage.IntervalStats statsOut) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        android.content.res.Configuration config = new android.content.res.Configuration();
        android.content.res.Configuration.readXmlAttrs(parser, config);
        android.app.usage.ConfigurationStats configStats = statsOut.getOrCreateConfigurationStats(config);
        configStats.mLastTimeActive = statsOut.beginTime + com.android.internal.util.XmlUtils.readLongAttribute(parser, LAST_TIME_ACTIVE_ATTR);
        configStats.mTotalTimeActive = com.android.internal.util.XmlUtils.readLongAttribute(parser, TOTAL_TIME_ACTIVE_ATTR);
        configStats.mActivationCount = com.android.internal.util.XmlUtils.readIntAttribute(parser, com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT);
        if (com.android.internal.util.XmlUtils.readBooleanAttribute(parser, "active")) {
            statsOut.activeConfiguration = configStats.mConfiguration;
        }
    }

    private static void loadEvent(org.xmlpull.v1.XmlPullParser parser, com.android.server.usage.IntervalStats statsOut) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String packageName = com.android.internal.util.XmlUtils.readStringAttribute(parser, "package");
        if (packageName == null) {
            throw new java.net.ProtocolException("no package attribute present");
        }
        java.lang.String className = com.android.internal.util.XmlUtils.readStringAttribute(parser, CLASS_ATTR);
        android.app.usage.UsageEvents.Event event = statsOut.buildEvent(packageName, className);
        event.mFlags = com.android.internal.util.XmlUtils.readIntAttribute(parser, FLAGS_ATTR, 0);
        event.mTimeStamp = statsOut.beginTime + com.android.internal.util.XmlUtils.readLongAttribute(parser, TIME_ATTR);
        event.mEventType = com.android.internal.util.XmlUtils.readIntAttribute(parser, "type");
        try {
            event.mInstanceId = com.android.internal.util.XmlUtils.readIntAttribute(parser, INSTANCE_ID_ATTR);
        } catch (java.io.IOException e) {
            android.util.Log.i(TAG, "Failed to parse mInstanceId");
        }
        switch (event.mEventType) {
            case 5:
                event.mConfiguration = new android.content.res.Configuration();
                android.content.res.Configuration.readXmlAttrs(parser, event.mConfiguration);
                break;
            case 8:
                java.lang.String id = com.android.internal.util.XmlUtils.readStringAttribute(parser, SHORTCUT_ID_ATTR);
                event.mShortcutId = id != null ? id.intern() : null;
                break;
            case 11:
                event.mBucketAndReason = com.android.internal.util.XmlUtils.readIntAttribute(parser, STANDBY_BUCKET_ATTR, 0);
                break;
            case 12:
                java.lang.String channelId = com.android.internal.util.XmlUtils.readStringAttribute(parser, NOTIFICATION_CHANNEL_ATTR);
                event.mNotificationChannelId = channelId != null ? channelId.intern() : null;
                break;
        }
        statsOut.addEvent(event);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void read(org.xmlpull.v1.XmlPullParser r7, com.android.server.usage.IntervalStats r8) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instruction units count: 264
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usage.UsageStatsXmlV1.read(org.xmlpull.v1.XmlPullParser, com.android.server.usage.IntervalStats):void");
    }

    private UsageStatsXmlV1() {
    }
}
