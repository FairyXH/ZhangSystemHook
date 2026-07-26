package com.android.server.utils.quota;

/* JADX INFO: loaded from: classes3.dex */
abstract class QuotaTracker {
    private static final boolean DEBUG = false;
    static final long MAX_WINDOW_SIZE_MS = 2592000000L;
    static final long MIN_WINDOW_SIZE_MS = 20000;
    private final android.app.AlarmManager mAlarmManager;
    final com.android.server.utils.quota.Categorizer mCategorizer;
    protected final android.content.Context mContext;
    private final com.android.server.utils.quota.QuotaTracker.InQuotaAlarmQueue mInQuotaAlarmQueue;
    protected final com.android.server.utils.quota.QuotaTracker.Injector mInjector;
    private boolean mIsQuotaFree;
    private static final java.lang.String TAG = com.android.server.utils.quota.QuotaTracker.class.getSimpleName();
    private static final java.lang.String ALARM_TAG_QUOTA_CHECK = com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER + TAG + ".quota_check*";
    final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArraySet<com.android.server.utils.quota.QuotaChangeListener> mQuotaChangeListeners = new android.util.ArraySet<>();
    private final android.util.SparseArrayMap<java.lang.String, java.lang.Boolean> mFreeQuota = new android.util.SparseArrayMap<>();
    private boolean mIsEnabled = true;
    private final android.content.BroadcastReceiver mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.utils.quota.QuotaTracker.1
        private java.lang.String getPackageName(android.content.Intent intent) {
            android.net.Uri uri = intent.getData();
            if (uri != null) {
                return uri.getSchemeSpecificPart();
            }
            return null;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            byte b;
            if (intent == null || intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                return;
            }
            java.lang.String action = intent.getAction();
            if (action == null) {
                android.util.Slog.e(com.android.server.utils.quota.QuotaTracker.TAG, "Received intent with null action");
                return;
            }
            switch (action.hashCode()) {
                case -2061058799:
                    b = !action.equals("android.intent.action.USER_REMOVED") ? (byte) -1 : (byte) 1;
                    break;
                case 1580442797:
                    b = !action.equals("android.intent.action.PACKAGE_FULLY_REMOVED") ? (byte) -1 : (byte) 0;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    int uid = intent.getIntExtra("android.intent.extra.UID", -1);
                    synchronized (com.android.server.utils.quota.QuotaTracker.this.mLock) {
                        com.android.server.utils.quota.QuotaTracker.this.onAppRemovedLocked(android.os.UserHandle.getUserId(uid), getPackageName(intent));
                        break;
                    }
                    return;
                case 1:
                    int userId = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    synchronized (com.android.server.utils.quota.QuotaTracker.this.mLock) {
                        com.android.server.utils.quota.QuotaTracker.this.onUserRemovedLocked(userId);
                        break;
                    }
                    return;
                default:
                    return;
            }
        }
    };

    abstract void dropEverythingLocked();

    abstract android.os.Handler getHandler();

    abstract long getInQuotaTimeElapsedLocked(int i, java.lang.String str, java.lang.String str2);

    abstract void handleRemovedAppLocked(int i, java.lang.String str);

    abstract void handleRemovedUserLocked(int i);

    abstract boolean isWithinQuotaLocked(int i, java.lang.String str, java.lang.String str2);

    abstract void maybeUpdateAllQuotaStatusLocked();

    abstract void maybeUpdateQuotaStatus(int i, java.lang.String str, java.lang.String str2);

    abstract void onQuotaFreeChangedLocked(int i, java.lang.String str, boolean z);

    abstract void onQuotaFreeChangedLocked(boolean z);

    static class Injector {
        Injector() {
        }

        long getElapsedRealtime() {
            return android.os.SystemClock.elapsedRealtime();
        }

        boolean isAlarmManagerReady() {
            return ((com.android.server.SystemServiceManager) com.android.server.LocalServices.getService(com.android.server.SystemServiceManager.class)).isBootCompleted();
        }
    }

    QuotaTracker(android.content.Context context, com.android.server.utils.quota.Categorizer categorizer, com.android.server.utils.quota.QuotaTracker.Injector injector) {
        this.mCategorizer = categorizer;
        this.mContext = context;
        this.mInjector = injector;
        this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        this.mInQuotaAlarmQueue = new com.android.server.utils.quota.QuotaTracker.InQuotaAlarmQueue(this.mContext, com.android.server.FgThread.getHandler().getLooper());
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        filter.addDataScheme("package");
        context.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, filter, null, com.android.internal.os.BackgroundThread.getHandler());
        android.content.IntentFilter userFilter = new android.content.IntentFilter("android.intent.action.USER_REMOVED");
        context.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, userFilter, null, com.android.internal.os.BackgroundThread.getHandler());
    }

    public void clear() {
        synchronized (this.mLock) {
            this.mInQuotaAlarmQueue.removeAllAlarms();
            this.mFreeQuota.clear();
            dropEverythingLocked();
        }
    }

    public boolean isWithinQuota(int userId, java.lang.String packageName, java.lang.String tag) {
        boolean zIsWithinQuotaLocked;
        synchronized (this.mLock) {
            zIsWithinQuotaLocked = isWithinQuotaLocked(userId, packageName, tag);
        }
        return zIsWithinQuotaLocked;
    }

    public void setQuotaFree(int userId, java.lang.String packageName, boolean isFree) {
        synchronized (this.mLock) {
            boolean wasFree = ((java.lang.Boolean) this.mFreeQuota.getOrDefault(userId, packageName, java.lang.Boolean.FALSE)).booleanValue();
            if (wasFree != isFree) {
                this.mFreeQuota.add(userId, packageName, java.lang.Boolean.valueOf(isFree));
                onQuotaFreeChangedLocked(userId, packageName, isFree);
            }
        }
    }

    public void setQuotaFree(boolean isFree) {
        synchronized (this.mLock) {
            if (this.mIsQuotaFree == isFree) {
                return;
            }
            this.mIsQuotaFree = isFree;
            if (this.mIsEnabled) {
                onQuotaFreeChangedLocked(this.mIsQuotaFree);
                scheduleQuotaCheck();
            }
        }
    }

    public void registerQuotaChangeListener(com.android.server.utils.quota.QuotaChangeListener listener) {
        synchronized (this.mLock) {
            if (this.mQuotaChangeListeners.add(listener) && this.mQuotaChangeListeners.size() == 1) {
                scheduleQuotaCheck();
            }
        }
    }

    public void unregisterQuotaChangeListener(com.android.server.utils.quota.QuotaChangeListener listener) {
        synchronized (this.mLock) {
            this.mQuotaChangeListeners.remove(listener);
        }
    }

    public void setEnabled(boolean enable) {
        synchronized (this.mLock) {
            if (this.mIsEnabled == enable) {
                return;
            }
            this.mIsEnabled = enable;
            if (!this.mIsEnabled) {
                clear();
            }
        }
    }

    boolean isEnabledLocked() {
        return this.mIsEnabled;
    }

    boolean isQuotaFreeLocked() {
        return this.mIsQuotaFree;
    }

    boolean isQuotaFreeLocked(int userId, java.lang.String packageName) {
        return this.mIsQuotaFree || ((java.lang.Boolean) this.mFreeQuota.getOrDefault(userId, packageName, java.lang.Boolean.FALSE)).booleanValue();
    }

    boolean isIndividualQuotaFreeLocked(int userId, java.lang.String packageName) {
        return ((java.lang.Boolean) this.mFreeQuota.getOrDefault(userId, packageName, java.lang.Boolean.FALSE)).booleanValue();
    }

    void scheduleAlarm(final int type, final long triggerAtMillis, final java.lang.String tag, final android.app.AlarmManager.OnAlarmListener listener) {
        com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.utils.quota.QuotaTracker$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleAlarm$0(type, triggerAtMillis, tag, listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleAlarm$0(int type, long triggerAtMillis, java.lang.String tag, android.app.AlarmManager.OnAlarmListener listener) {
        if (this.mInjector.isAlarmManagerReady()) {
            this.mAlarmManager.set(type, triggerAtMillis, tag, listener, getHandler());
        } else {
            android.util.Slog.w(TAG, "Alarm not scheduled because boot isn't completed");
        }
    }

    void cancelAlarm(final android.app.AlarmManager.OnAlarmListener listener) {
        com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.utils.quota.QuotaTracker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cancelAlarm$1(listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelAlarm$1(android.app.AlarmManager.OnAlarmListener listener) {
        if (this.mInjector.isAlarmManagerReady()) {
            this.mAlarmManager.cancel(listener);
        } else {
            android.util.Slog.w(TAG, "Alarm not cancelled because boot isn't completed");
        }
    }

    void scheduleQuotaCheck() {
        com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.utils.quota.QuotaTracker$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleQuotaCheck$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleQuotaCheck$2() {
        synchronized (this.mLock) {
            if (this.mQuotaChangeListeners.size() > 0) {
                maybeUpdateAllQuotaStatusLocked();
            }
        }
    }

    void onAppRemovedLocked(int userId, java.lang.String packageName) {
        if (packageName == null) {
            android.util.Slog.wtf(TAG, "Told app removed but given null package name.");
            return;
        }
        this.mInQuotaAlarmQueue.removeAlarms(userId, packageName);
        this.mFreeQuota.delete(userId, packageName);
        handleRemovedAppLocked(userId, packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserRemovedLocked(int userId) {
        this.mInQuotaAlarmQueue.removeAlarmsForUserId(userId);
        this.mFreeQuota.delete(userId);
        handleRemovedUserLocked(userId);
    }

    void postQuotaStatusChanged(final int userId, final java.lang.String packageName, final java.lang.String tag) {
        com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.utils.quota.QuotaTracker$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$postQuotaStatusChanged$3(userId, packageName, tag);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postQuotaStatusChanged$3(int userId, java.lang.String packageName, java.lang.String tag) {
        com.android.server.utils.quota.QuotaChangeListener[] listeners;
        synchronized (this.mLock) {
            listeners = (com.android.server.utils.quota.QuotaChangeListener[]) this.mQuotaChangeListeners.toArray(new com.android.server.utils.quota.QuotaChangeListener[this.mQuotaChangeListeners.size()]);
        }
        for (com.android.server.utils.quota.QuotaChangeListener listener : listeners) {
            listener.onQuotaStateChanged(userId, packageName, tag);
        }
    }

    void maybeScheduleStartAlarmLocked(int userId, java.lang.String packageName, java.lang.String tag) {
        if (this.mQuotaChangeListeners.size() == 0) {
            return;
        }
        com.android.server.utils.quota.Uptc.string(userId, packageName, tag);
        if (isWithinQuota(userId, packageName, tag)) {
            this.mInQuotaAlarmQueue.removeAlarmForKey(new com.android.server.utils.quota.Uptc(userId, packageName, tag));
            maybeUpdateQuotaStatus(userId, packageName, tag);
        } else {
            this.mInQuotaAlarmQueue.addAlarm(new com.android.server.utils.quota.Uptc(userId, packageName, tag), getInQuotaTimeElapsedLocked(userId, packageName, tag));
        }
    }

    void cancelScheduledStartAlarmLocked(int userId, java.lang.String packageName, java.lang.String tag) {
        this.mInQuotaAlarmQueue.removeAlarmForKey(new com.android.server.utils.quota.Uptc(userId, packageName, tag));
    }

    /* JADX INFO: Access modifiers changed from: private */
    class InQuotaAlarmQueue extends com.android.server.utils.AlarmQueue<com.android.server.utils.quota.Uptc> {
        private InQuotaAlarmQueue(android.content.Context context, android.os.Looper looper) {
            super(context, looper, com.android.server.utils.quota.QuotaTracker.ALARM_TAG_QUOTA_CHECK, "In quota", false, 0L);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.utils.AlarmQueue
        public boolean isForUser(com.android.server.utils.quota.Uptc uptc, int userId) {
            return userId == uptc.userId;
        }

        static /* synthetic */ boolean lambda$removeAlarms$0(int userId, java.lang.String packageName, com.android.server.utils.quota.Uptc uptc) {
            return userId == uptc.userId && packageName.equals(uptc.packageName);
        }

        void removeAlarms(final int userId, final java.lang.String packageName) {
            removeAlarmsIf(new java.util.function.Predicate() { // from class: com.android.server.utils.quota.QuotaTracker$InQuotaAlarmQueue$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.utils.quota.QuotaTracker.InQuotaAlarmQueue.lambda$removeAlarms$0(userId, packageName, (com.android.server.utils.quota.Uptc) obj);
                }
            });
        }

        @Override // com.android.server.utils.AlarmQueue
        protected void processExpiredAlarms(android.util.ArraySet<com.android.server.utils.quota.Uptc> expired) {
            for (int i = 0; i < expired.size(); i++) {
                final com.android.server.utils.quota.Uptc uptc = expired.valueAt(i);
                com.android.server.utils.quota.QuotaTracker.this.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.utils.quota.QuotaTracker$InQuotaAlarmQueue$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$processExpiredAlarms$1(uptc);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$processExpiredAlarms$1(com.android.server.utils.quota.Uptc uptc) {
            com.android.server.utils.quota.QuotaTracker.this.maybeUpdateQuotaStatus(uptc.userId, uptc.packageName, uptc.tag);
        }
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        pw.println("QuotaTracker:");
        pw.increaseIndent();
        synchronized (this.mLock) {
            pw.println("Is enabled: " + this.mIsEnabled);
            pw.println("Is global quota free: " + this.mIsQuotaFree);
            pw.println("Current elapsed time: " + this.mInjector.getElapsedRealtime());
            pw.println();
            pw.println();
            this.mInQuotaAlarmQueue.dump(pw);
            pw.println();
            pw.println("Per-app free quota:");
            pw.increaseIndent();
            for (int u = 0; u < this.mFreeQuota.numMaps(); u++) {
                int userId = this.mFreeQuota.keyAt(u);
                for (int p = 0; p < this.mFreeQuota.numElementsForKey(userId); p++) {
                    java.lang.String pkgName = (java.lang.String) this.mFreeQuota.keyAt(u, p);
                    pw.print(com.android.server.utils.quota.Uptc.string(userId, pkgName, null));
                    pw.print(": ");
                    pw.println(this.mFreeQuota.get(userId, pkgName));
                }
            }
            pw.decreaseIndent();
        }
        pw.decreaseIndent();
    }

    public void dump(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        synchronized (this.mLock) {
            proto.write(1133871366145L, this.mIsEnabled);
            proto.write(1133871366146L, this.mIsQuotaFree);
            proto.write(1112396529667L, this.mInjector.getElapsedRealtime());
        }
        proto.end(token);
    }
}
