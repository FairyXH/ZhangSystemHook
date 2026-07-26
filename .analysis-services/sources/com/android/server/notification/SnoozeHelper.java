package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public final class SnoozeHelper {
    static final int CONCURRENT_SNOOZE_LIMIT = 500;
    static final java.lang.String EXTRA_KEY = "key";
    private static final java.lang.String EXTRA_USER_ID = "userId";
    private static final java.lang.String INDENT = "    ";
    static final int MAX_STRING_LENGTH = 1000;
    private static final java.lang.String REPOST_SCHEME = "repost";
    private static final int REQUEST_CODE_REPOST = 1;
    private static final java.lang.String XML_SNOOZED_NOTIFICATION = "notification";
    private static final java.lang.String XML_SNOOZED_NOTIFICATION_CONTEXT = "context";
    private static final java.lang.String XML_SNOOZED_NOTIFICATION_CONTEXT_ID = "id";
    private static final java.lang.String XML_SNOOZED_NOTIFICATION_KEY = "key";
    private static final java.lang.String XML_SNOOZED_NOTIFICATION_TIME = "time";
    public static final int XML_SNOOZED_NOTIFICATION_VERSION = 1;
    private static final java.lang.String XML_SNOOZED_NOTIFICATION_VERSION_LABEL = "version";
    protected static final java.lang.String XML_TAG_NAME = "snoozed-notifications";
    private android.app.AlarmManager mAm;
    private com.android.server.notification.SnoozeHelper.Callback mCallback;
    private final android.content.Context mContext;
    private final com.android.server.notification.ManagedServices.UserProfiles mUserProfiles;
    private static final java.lang.String TAG = "SnoozeHelper";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final java.lang.String REPOST_ACTION = com.android.server.notification.SnoozeHelper.class.getSimpleName() + ".EVALUATE";
    private android.util.ArrayMap<java.lang.String, com.android.server.notification.NotificationRecord> mSnoozedNotifications = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, java.lang.Long> mPersistedSnoozedNotifications = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, java.lang.String> mPersistedSnoozedNotificationsWithContext = new android.util.ArrayMap<>();
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.content.BroadcastReceiver mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.notification.SnoozeHelper.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (com.android.server.notification.SnoozeHelper.DEBUG) {
                android.util.Slog.d(com.android.server.notification.SnoozeHelper.TAG, "Reposting notification");
            }
            if (com.android.server.notification.SnoozeHelper.REPOST_ACTION.equals(intent.getAction())) {
                com.android.server.notification.SnoozeHelper.this.repost(intent.getStringExtra("key"), intent.getIntExtra("userId", 0), false);
            }
        }
    };

    protected interface Callback {
        void repost(int i, com.android.server.notification.NotificationRecord notificationRecord, boolean z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    interface Inserter<T> {
        void insert(T t) throws java.io.IOException;
    }

    public SnoozeHelper(android.content.Context context, com.android.server.notification.SnoozeHelper.Callback callback, com.android.server.notification.ManagedServices.UserProfiles userProfiles) {
        this.mContext = context;
        android.content.IntentFilter filter = new android.content.IntentFilter(REPOST_ACTION);
        filter.addDataScheme(REPOST_SCHEME);
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter, 2);
        this.mAm = (android.app.AlarmManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        this.mCallback = callback;
        this.mUserProfiles = userProfiles;
    }

    protected boolean canSnooze(int numberToSnooze) {
        synchronized (this.mLock) {
            if (this.mSnoozedNotifications.size() + numberToSnooze <= 500 && this.mPersistedSnoozedNotifications.size() + this.mPersistedSnoozedNotificationsWithContext.size() + numberToSnooze <= 500) {
                return true;
            }
            return false;
        }
    }

    protected java.lang.Long getSnoozeTimeForUnpostedNotification(int userId, java.lang.String pkg, java.lang.String key) {
        java.lang.Long time;
        synchronized (this.mLock) {
            time = this.mPersistedSnoozedNotifications.get(getTrimmedString(key));
        }
        if (time == null) {
            return 0L;
        }
        return time;
    }

    protected java.lang.String getSnoozeContextForUnpostedNotification(int userId, java.lang.String pkg, java.lang.String key) {
        java.lang.String str;
        synchronized (this.mLock) {
            str = this.mPersistedSnoozedNotificationsWithContext.get(getTrimmedString(key));
        }
        return str;
    }

    protected boolean isSnoozed(int userId, java.lang.String pkg, java.lang.String key) {
        boolean zContainsKey;
        synchronized (this.mLock) {
            zContainsKey = this.mSnoozedNotifications.containsKey(key);
        }
        return zContainsKey;
    }

    protected java.util.Collection<com.android.server.notification.NotificationRecord> getSnoozed(int userId, java.lang.String pkg) {
        java.util.ArrayList snoozed;
        synchronized (this.mLock) {
            snoozed = new java.util.ArrayList();
            for (com.android.server.notification.NotificationRecord r : this.mSnoozedNotifications.values()) {
                if (r.getUserId() == userId && r.getSbn().getPackageName().equals(pkg)) {
                    snoozed.add(r);
                }
            }
        }
        return snoozed;
    }

    java.util.ArrayList<com.android.server.notification.NotificationRecord> getNotifications(java.lang.String pkg, java.lang.String groupKey, java.lang.Integer userId) {
        java.util.ArrayList<com.android.server.notification.NotificationRecord> records = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mSnoozedNotifications.size(); i++) {
                com.android.server.notification.NotificationRecord r = this.mSnoozedNotifications.valueAt(i);
                if (r.getSbn().getPackageName().equals(pkg) && r.getUserId() == userId.intValue() && java.util.Objects.equals(r.getSbn().getGroup(), groupKey)) {
                    records.add(r);
                }
            }
        }
        return records;
    }

    protected com.android.server.notification.NotificationRecord getNotification(java.lang.String key) {
        com.android.server.notification.NotificationRecord notificationRecord;
        synchronized (this.mLock) {
            notificationRecord = this.mSnoozedNotifications.get(key);
        }
        return notificationRecord;
    }

    protected java.util.List<com.android.server.notification.NotificationRecord> getSnoozed() {
        java.util.List<com.android.server.notification.NotificationRecord> snoozed;
        synchronized (this.mLock) {
            snoozed = new java.util.ArrayList<>();
            snoozed.addAll(this.mSnoozedNotifications.values());
        }
        return snoozed;
    }

    protected void snooze(com.android.server.notification.NotificationRecord record, long duration) {
        java.lang.String key = record.getKey();
        snooze(record);
        scheduleRepost(key, duration);
        java.lang.Long activateAt = java.lang.Long.valueOf(java.lang.System.currentTimeMillis() + duration);
        synchronized (this.mLock) {
            this.mPersistedSnoozedNotifications.put(getTrimmedString(key), activateAt);
        }
    }

    protected void snooze(com.android.server.notification.NotificationRecord record, java.lang.String contextId) {
        if (contextId != null) {
            synchronized (this.mLock) {
                this.mPersistedSnoozedNotificationsWithContext.put(getTrimmedString(record.getKey()), getTrimmedString(contextId));
            }
        }
        snooze(record);
    }

    private void snooze(com.android.server.notification.NotificationRecord record) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Snoozing " + record.getKey());
        }
        synchronized (this.mLock) {
            this.mSnoozedNotifications.put(record.getKey(), record);
        }
    }

    private java.lang.String getTrimmedString(java.lang.String key) {
        if (key != null && key.length() > 1000) {
            return key.substring(0, 1000);
        }
        return key;
    }

    protected boolean cancel(int userId, java.lang.String pkg, java.lang.String tag, int id) {
        synchronized (this.mLock) {
            java.util.Set<java.util.Map.Entry<java.lang.String, com.android.server.notification.NotificationRecord>> records = this.mSnoozedNotifications.entrySet();
            for (java.util.Map.Entry<java.lang.String, com.android.server.notification.NotificationRecord> record : records) {
                android.service.notification.StatusBarNotification sbn = record.getValue().getSbn();
                if (sbn.getPackageName().equals(pkg) && sbn.getUserId() == userId && java.util.Objects.equals(sbn.getTag(), tag) && sbn.getId() == id) {
                    record.getValue().isCanceled = true;
                    return true;
                }
            }
            return false;
        }
    }

    protected void cancel(int userId, boolean includeCurrentProfiles) {
        synchronized (this.mLock) {
            if (this.mSnoozedNotifications.size() == 0) {
                return;
            }
            android.util.IntArray userIds = new android.util.IntArray();
            userIds.add(userId);
            if (includeCurrentProfiles) {
                userIds = this.mUserProfiles.getCurrentProfileIds();
            }
            for (com.android.server.notification.NotificationRecord r : this.mSnoozedNotifications.values()) {
                if (userIds.binarySearch(r.getUserId()) >= 0) {
                    r.isCanceled = true;
                }
            }
        }
    }

    protected boolean cancel(int userId, java.lang.String pkg) {
        synchronized (this.mLock) {
            int n = this.mSnoozedNotifications.size();
            for (int i = 0; i < n; i++) {
                com.android.server.notification.NotificationRecord r = this.mSnoozedNotifications.valueAt(i);
                if (r.getSbn().getPackageName().equals(pkg) && r.getUserId() == userId) {
                    r.isCanceled = true;
                }
            }
        }
        return true;
    }

    protected void update(int userId, com.android.server.notification.NotificationRecord record) {
        synchronized (this.mLock) {
            if (this.mSnoozedNotifications.containsKey(record.getKey())) {
                this.mSnoozedNotifications.put(record.getKey(), record);
            }
        }
    }

    protected void repostAll(android.util.IntArray userIds) {
        synchronized (this.mLock) {
            java.util.List<com.android.server.notification.NotificationRecord> snoozedNotifications = getSnoozed();
            for (com.android.server.notification.NotificationRecord r : snoozedNotifications) {
                if (userIds.binarySearch(r.getUserId()) >= 0) {
                    repost(r.getKey(), r.getUserId(), false);
                }
            }
        }
    }

    protected void repost(java.lang.String key, boolean muteOnReturn) {
        synchronized (this.mLock) {
            com.android.server.notification.NotificationRecord r = this.mSnoozedNotifications.get(key);
            if (r != null) {
                repost(key, r.getUserId(), muteOnReturn);
            }
        }
    }

    protected void repost(java.lang.String key, int userId, boolean muteOnReturn) {
        com.android.server.notification.NotificationRecord record;
        java.lang.String trimmedKey = getTrimmedString(key);
        synchronized (this.mLock) {
            this.mPersistedSnoozedNotifications.remove(trimmedKey);
            this.mPersistedSnoozedNotificationsWithContext.remove(trimmedKey);
            record = this.mSnoozedNotifications.remove(key);
        }
        if (record != null && !record.isCanceled) {
            android.app.PendingIntent pi = createPendingIntent(record.getKey());
            this.mAm.cancel(pi);
            com.android.internal.logging.MetricsLogger.action(record.getLogMaker().setCategory(com.android.internal.util.FrameworkStatsLog.SENSITIVE_NOTIFICATION_APP_PROTECTION_SESSION).setType(1));
            this.mCallback.repost(record.getUserId(), record, muteOnReturn);
        }
    }

    protected void repostGroupSummary(java.lang.String pkg, int userId, java.lang.String groupKey) {
        synchronized (this.mLock) {
            java.lang.String groupSummaryKey = null;
            int n = this.mSnoozedNotifications.size();
            int i = 0;
            while (true) {
                if (i >= n) {
                    break;
                }
                com.android.server.notification.NotificationRecord potentialGroupSummary = this.mSnoozedNotifications.valueAt(i);
                if (!potentialGroupSummary.getSbn().getPackageName().equals(pkg) || potentialGroupSummary.getUserId() != userId || !potentialGroupSummary.getSbn().isGroup() || !potentialGroupSummary.getNotification().isGroupSummary() || !groupKey.equals(potentialGroupSummary.getGroupKey())) {
                    i++;
                } else {
                    groupSummaryKey = potentialGroupSummary.getKey();
                    break;
                }
            }
            if (groupSummaryKey != null) {
                final com.android.server.notification.NotificationRecord record = this.mSnoozedNotifications.remove(groupSummaryKey);
                java.lang.String trimmedKey = getTrimmedString(groupSummaryKey);
                this.mPersistedSnoozedNotificationsWithContext.remove(trimmedKey);
                this.mPersistedSnoozedNotifications.remove(trimmedKey);
                if (record != null && !record.isCanceled) {
                    java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$repostGroupSummary$0(record);
                        }
                    };
                    runnable.run();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$repostGroupSummary$0(com.android.server.notification.NotificationRecord record) {
        com.android.internal.logging.MetricsLogger.action(record.getLogMaker().setCategory(com.android.internal.util.FrameworkStatsLog.SENSITIVE_NOTIFICATION_APP_PROTECTION_SESSION).setType(1));
        this.mCallback.repost(record.getUserId(), record, false);
    }

    protected void clearData(int userId, java.lang.String pkg) {
        synchronized (this.mLock) {
            int n = this.mSnoozedNotifications.size();
            for (int i = n - 1; i >= 0; i--) {
                final com.android.server.notification.NotificationRecord record = this.mSnoozedNotifications.valueAt(i);
                if (record.getUserId() == userId && record.getSbn().getPackageName().equals(pkg)) {
                    this.mSnoozedNotifications.removeAt(i);
                    java.lang.String trimmedKey = getTrimmedString(record.getKey());
                    this.mPersistedSnoozedNotificationsWithContext.remove(trimmedKey);
                    this.mPersistedSnoozedNotifications.remove(trimmedKey);
                    java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$clearData$1(record);
                        }
                    };
                    runnable.run();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearData$1(com.android.server.notification.NotificationRecord record) {
        android.app.PendingIntent pi = createPendingIntent(record.getKey());
        this.mAm.cancel(pi);
        com.android.internal.logging.MetricsLogger.action(record.getLogMaker().setCategory(com.android.internal.util.FrameworkStatsLog.SENSITIVE_NOTIFICATION_APP_PROTECTION_SESSION).setType(5));
    }

    protected void clearData(int userId) {
        synchronized (this.mLock) {
            int n = this.mSnoozedNotifications.size();
            for (int i = n - 1; i >= 0; i--) {
                final com.android.server.notification.NotificationRecord record = this.mSnoozedNotifications.valueAt(i);
                if (record.getUserId() == userId) {
                    this.mSnoozedNotifications.removeAt(i);
                    java.lang.String trimmedKey = getTrimmedString(record.getKey());
                    this.mPersistedSnoozedNotificationsWithContext.remove(trimmedKey);
                    this.mPersistedSnoozedNotifications.remove(trimmedKey);
                    java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$clearData$2(record);
                        }
                    };
                    runnable.run();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearData$2(com.android.server.notification.NotificationRecord record) {
        android.app.PendingIntent pi = createPendingIntent(record.getKey());
        this.mAm.cancel(pi);
        com.android.internal.logging.MetricsLogger.action(record.getLogMaker().setCategory(com.android.internal.util.FrameworkStatsLog.SENSITIVE_NOTIFICATION_APP_PROTECTION_SESSION).setType(5));
    }

    private android.app.PendingIntent createPendingIntent(java.lang.String key) {
        return android.app.PendingIntent.getBroadcast(this.mContext, 1, new android.content.Intent(REPOST_ACTION).setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME).setData(new android.net.Uri.Builder().scheme(REPOST_SCHEME).appendPath(key).build()).addFlags(268435456).putExtra("key", key), android.hardware.audio.common.V2_0.AudioFormat.DTS_HD);
    }

    public void scheduleRepostsForPersistedNotifications(long currentTime) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mPersistedSnoozedNotifications.size(); i++) {
                java.lang.String key = this.mPersistedSnoozedNotifications.keyAt(i);
                java.lang.Long time = this.mPersistedSnoozedNotifications.valueAt(i);
                if (time != null && time.longValue() > currentTime) {
                    scheduleRepostAtTime(key, time.longValue());
                }
            }
        }
    }

    private void scheduleRepost(java.lang.String key, long duration) {
        scheduleRepostAtTime(key, java.lang.System.currentTimeMillis() + duration);
    }

    private void scheduleRepostAtTime(final java.lang.String key, final long time) {
        java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleRepostAtTime$3(key, time);
            }
        };
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRepostAtTime$3(java.lang.String key, long time) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.app.PendingIntent pi = createPendingIntent(key);
            this.mAm.cancel(pi);
            if (DEBUG) {
                android.util.Slog.d(TAG, "Scheduling evaluate for " + new java.util.Date(time));
            }
            this.mAm.setExactAndAllowWhileIdle(0, time, pi);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void dump(java.io.PrintWriter pw, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        synchronized (this.mLock) {
            pw.println("\n  Snoozed notifications:");
            for (java.lang.String key : this.mSnoozedNotifications.keySet()) {
                pw.print(INDENT);
                pw.println("key: " + key);
            }
            pw.println("\n Pending snoozed notifications");
            for (java.lang.String key2 : this.mPersistedSnoozedNotifications.keySet()) {
                pw.print(INDENT);
                pw.println("key: " + key2 + " until: " + this.mPersistedSnoozedNotifications.get(key2));
            }
        }
    }

    protected void writeXml(final com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
        synchronized (this.mLock) {
            final long currentTime = java.lang.System.currentTimeMillis();
            out.startTag((java.lang.String) null, XML_TAG_NAME);
            writeXml(out, this.mPersistedSnoozedNotifications, XML_SNOOZED_NOTIFICATION, new com.android.server.notification.SnoozeHelper.Inserter() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda2
                @Override // com.android.server.notification.SnoozeHelper.Inserter
                public final void insert(java.lang.Object obj) throws java.io.IOException {
                    com.android.server.notification.SnoozeHelper.lambda$writeXml$4(currentTime, out, (java.lang.Long) obj);
                }
            });
            writeXml(out, this.mPersistedSnoozedNotificationsWithContext, XML_SNOOZED_NOTIFICATION_CONTEXT, new com.android.server.notification.SnoozeHelper.Inserter() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda3
                @Override // com.android.server.notification.SnoozeHelper.Inserter
                public final void insert(java.lang.Object obj) {
                    out.attribute((java.lang.String) null, com.android.server.notification.SnoozeHelper.XML_SNOOZED_NOTIFICATION_CONTEXT_ID, (java.lang.String) obj);
                }
            });
            out.endTag((java.lang.String) null, XML_TAG_NAME);
        }
    }

    static /* synthetic */ void lambda$writeXml$4(long currentTime, com.android.modules.utils.TypedXmlSerializer out, java.lang.Long value) throws java.io.IOException {
        if (value.longValue() < currentTime) {
            return;
        }
        out.attributeLong((java.lang.String) null, XML_SNOOZED_NOTIFICATION_TIME, value.longValue());
    }

    private <T> void writeXml(com.android.modules.utils.TypedXmlSerializer out, android.util.ArrayMap<java.lang.String, T> targets, java.lang.String tag, com.android.server.notification.SnoozeHelper.Inserter<T> attributeInserter) throws java.io.IOException {
        for (int j = 0; j < targets.size(); j++) {
            java.lang.String key = targets.keyAt(j);
            T value = targets.valueAt(j);
            out.startTag((java.lang.String) null, tag);
            attributeInserter.insert(value);
            out.attributeInt((java.lang.String) null, XML_SNOOZED_NOTIFICATION_VERSION_LABEL, 1);
            out.attribute((java.lang.String) null, "key", key);
            out.endTag((java.lang.String) null, tag);
        }
    }

    protected void readXml(com.android.modules.utils.TypedXmlPullParser parser, long currentTime) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        while (true) {
            int type = parser.next();
            if (type != 1) {
                java.lang.String tag = parser.getName();
                if (type != 3 || !XML_TAG_NAME.equals(tag)) {
                    if (type == 2 && (XML_SNOOZED_NOTIFICATION.equals(tag) || tag.equals(XML_SNOOZED_NOTIFICATION_CONTEXT))) {
                        if (parser.getAttributeInt((java.lang.String) null, XML_SNOOZED_NOTIFICATION_VERSION_LABEL, -1) == 1) {
                            try {
                                java.lang.String key = parser.getAttributeValue((java.lang.String) null, "key");
                                if (tag.equals(XML_SNOOZED_NOTIFICATION)) {
                                    java.lang.Long time = java.lang.Long.valueOf(parser.getAttributeLong((java.lang.String) null, XML_SNOOZED_NOTIFICATION_TIME, 0L));
                                    if (time.longValue() > currentTime) {
                                        synchronized (this.mLock) {
                                            this.mPersistedSnoozedNotifications.put(key, time);
                                        }
                                    }
                                }
                                if (tag.equals(XML_SNOOZED_NOTIFICATION_CONTEXT)) {
                                    java.lang.String creationId = parser.getAttributeValue((java.lang.String) null, XML_SNOOZED_NOTIFICATION_CONTEXT_ID);
                                    synchronized (this.mLock) {
                                        this.mPersistedSnoozedNotificationsWithContext.put(key, creationId);
                                    }
                                }
                            } catch (java.lang.Exception e) {
                                android.util.Slog.e(TAG, "Exception in reading snooze data from policy xml", e);
                            }
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    void setAlarmManager(android.app.AlarmManager am) {
        this.mAm = am;
    }
}
