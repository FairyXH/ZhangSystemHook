package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class AppStandbyController implements com.android.server.usage.AppStandbyInternal, android.app.usage.UsageStatsManagerInternal.UsageEventListener {
    static final boolean COMPRESS_TIME = false;
    static final boolean DEBUG = false;
    private static final long DEFAULT_PREDICTION_TIMEOUT = 43200000;
    private static final int HEADLESS_APP_CHECK_FLAGS = 1835520;
    static final int MSG_CHECK_IDLE_STATES = 5;
    static final int MSG_CHECK_PACKAGE_IDLE_STATE = 11;
    static final int MSG_FORCE_IDLE_STATE = 4;
    static final int MSG_INFORM_LISTENERS = 3;
    static final int MSG_ONE_TIME_CHECK_IDLE_STATES = 10;
    static final int MSG_PAROLE_STATE_CHANGED = 9;
    static final int MSG_REPORT_CONTENT_PROVIDER_USAGE = 8;
    static final int MSG_REPORT_EXEMPTED_SYNC_START = 13;
    static final int MSG_REPORT_SYNC_SCHEDULED = 12;
    static final int MSG_TRIGGER_LISTENER_QUOTA_BUMP = 7;
    private static final long NETWORK_SCORER_CACHE_DURATION_MILLIS = 5000;
    private static final long NOTIFICATION_SEEN_HOLD_DURATION_FOR_PRE_T_APPS = 43200000;
    private static final int NOTIFICATION_SEEN_PROMOTED_BUCKET_FOR_PRE_T_APPS = 20;
    private static final long ONE_DAY = 86400000;
    private static final long ONE_HOUR = 3600000;
    private static final long ONE_MINUTE = 60000;
    private static final int SYSTEM_PACKAGE_FLAGS = 542908416;
    private static final java.lang.String TAG = "AppStandbyController";
    private static final long WAIT_FOR_ADMIN_DATA_TIMEOUT_MS = 10000;
    private final android.util.SparseArray<java.util.Set<java.lang.String>> mActiveAdminApps;
    private final java.util.concurrent.CountDownLatch mAdminDataAvailableLatch;
    private final android.util.SparseArray<java.util.Set<java.lang.String>> mAdminProtectedPackages;
    private volatile boolean mAppIdleEnabled;
    private com.android.server.usage.AppIdleHistory mAppIdleHistory;
    private final java.lang.Object mAppIdleLock;
    private android.app.AppOpsManager mAppOpsManager;
    private com.android.server.usage.IAppStandbyControllerExt mAppStandByExt;
    private com.android.server.usage.AppStandbyController.AppStandbyControllerWrapper mAppStandbyControllerWrapper;
    long[] mAppStandbyElapsedThresholds;
    private final java.util.Map<java.lang.String, java.lang.String> mAppStandbyProperties;
    long[] mAppStandbyScreenThresholds;
    private android.appwidget.AppWidgetManager mAppWidgetManager;
    private final android.util.SparseSetArray<java.lang.String> mAppsToRestoreToRare;
    volatile java.lang.String mBroadcastResponseExemptedPermissions;
    volatile java.util.List<java.lang.String> mBroadcastResponseExemptedPermissionsList;
    volatile java.lang.String mBroadcastResponseExemptedRoles;
    volatile java.util.List<java.lang.String> mBroadcastResponseExemptedRolesList;
    volatile int mBroadcastResponseFgThresholdState;
    volatile long mBroadcastResponseWindowDurationMillis;
    volatile long mBroadcastSessionsDurationMs;
    volatile long mBroadcastSessionsWithResponseDurationMs;
    private java.lang.String mCachedDeviceProvisioningPackage;
    private volatile java.lang.String mCachedNetworkScorer;
    private volatile long mCachedNetworkScorerAtMillis;
    private java.util.List<java.lang.String> mCarrierPrivilegedApps;
    private final java.lang.Object mCarrierPrivilegedLock;
    long mCheckIdleIntervalMillis;
    private final android.content.Context mContext;
    private final android.hardware.display.DisplayManager.DisplayListener mDisplayListener;
    long mExemptedSyncScheduledDozeTimeoutMillis;
    long mExemptedSyncScheduledNonDozeTimeoutMillis;
    long mExemptedSyncStartTimeoutMillis;
    private final com.android.server.usage.AppStandbyController.AppStandbyHandler mHandler;
    private boolean mHaveCarrierPrivilegedApps;
    private final android.util.ArraySet<java.lang.String> mHeadlessSystemApps;
    long mInitialForegroundServiceStartTimeoutMillis;
    com.android.server.usage.AppStandbyController.Injector mInjector;
    private volatile boolean mIsCharging;
    boolean mLinkCrossProfileApps;
    volatile boolean mNoteResponseEventForAllBroadcastSessions;
    int mNotificationSeenPromotedBucket;
    long mNotificationSeenTimeoutMillis;
    private final java.util.ArrayList<com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener> mPackageAccessListeners;
    private android.content.pm.PackageManager mPackageManager;
    private final android.util.SparseLongArray mPendingIdleStateChecks;
    private boolean mPendingInitializeDefaults;
    private volatile boolean mPendingOneTimeCheckIdleStates;
    long mPredictionTimeoutMillis;
    boolean mRetainNotificationSeenImpactForPreTApps;
    long mSlicePinnedTimeoutMillis;
    long mStrongUsageTimeoutMillis;
    long mSyncAdapterTimeoutMillis;
    private final android.util.SparseIntArray mSystemExemptionAppOpMode;
    long mSystemInteractionTimeoutMillis;
    private final java.util.ArrayList<java.lang.Integer> mSystemPackagesAppIds;
    private boolean mSystemServicesReady;
    long mSystemUpdateUsageTimeoutMillis;
    private boolean mTriggerQuotaBumpOnNotificationSeen;
    long mUnexemptedSyncScheduledTimeoutMillis;
    static final long[] DEFAULT_SCREEN_TIME_THRESHOLDS = {0, 0, 3600000, com.android.server.usage.AppStandbyController.ConstantsObserver.DEFAULT_SYSTEM_UPDATE_TIMEOUT, 21600000};
    static final long[] MINIMUM_SCREEN_TIME_THRESHOLDS = {0, 0, 0, 1800000, 3600000};
    static final long[] DEFAULT_ELAPSED_TIME_THRESHOLDS = {0, 43200000, 86400000, 172800000, 691200000};
    static final long[] MINIMUM_ELAPSED_TIME_THRESHOLDS = {0, 3600000, 3600000, com.android.server.usage.AppStandbyController.ConstantsObserver.DEFAULT_SYSTEM_UPDATE_TIMEOUT, 14400000};
    private static final int[] THRESHOLD_BUCKETS = {10, 20, 30, 40, 45};

    static class Lock {
        Lock() {
        }
    }

    private static class Pool<T> {
        private final T[] mArray;
        private int mSize = 0;

        Pool(T[] array) {
            this.mArray = array;
        }

        synchronized T obtain() {
            T t;
            if (this.mSize > 0) {
                T[] tArr = this.mArray;
                int i = this.mSize - 1;
                this.mSize = i;
                t = tArr[i];
            } else {
                t = null;
            }
            return t;
        }

        synchronized void recycle(T instance) {
            if (this.mSize < this.mArray.length) {
                T[] tArr = this.mArray;
                int i = this.mSize;
                this.mSize = i + 1;
                tArr[i] = instance;
            }
        }
    }

    private static class StandbyUpdateRecord {
        private static final com.android.server.usage.AppStandbyController.Pool<com.android.server.usage.AppStandbyController.StandbyUpdateRecord> sPool = new com.android.server.usage.AppStandbyController.Pool<>(new com.android.server.usage.AppStandbyController.StandbyUpdateRecord[10]);
        int bucket;
        boolean isUserInteraction;
        java.lang.String packageName;
        int reason;
        int userId;

        private StandbyUpdateRecord() {
        }

        public static com.android.server.usage.AppStandbyController.StandbyUpdateRecord obtain(java.lang.String pkgName, int userId, int bucket, int reason, boolean isInteraction) {
            com.android.server.usage.AppStandbyController.StandbyUpdateRecord r = sPool.obtain();
            if (r == null) {
                r = new com.android.server.usage.AppStandbyController.StandbyUpdateRecord();
            }
            r.packageName = pkgName;
            r.userId = userId;
            r.bucket = bucket;
            r.reason = reason;
            r.isUserInteraction = isInteraction;
            return r;
        }

        public void recycle() {
            sPool.recycle(this);
        }
    }

    private static class ContentProviderUsageRecord {
        private static final com.android.server.usage.AppStandbyController.Pool<com.android.server.usage.AppStandbyController.ContentProviderUsageRecord> sPool = new com.android.server.usage.AppStandbyController.Pool<>(new com.android.server.usage.AppStandbyController.ContentProviderUsageRecord[10]);
        public java.lang.String name;
        public java.lang.String packageName;
        public int userId;

        private ContentProviderUsageRecord() {
        }

        public static com.android.server.usage.AppStandbyController.ContentProviderUsageRecord obtain(java.lang.String name, java.lang.String packageName, int userId) {
            com.android.server.usage.AppStandbyController.ContentProviderUsageRecord r = sPool.obtain();
            if (r == null) {
                r = new com.android.server.usage.AppStandbyController.ContentProviderUsageRecord();
            }
            r.name = name;
            r.packageName = packageName;
            r.userId = userId;
            return r;
        }

        public void recycle() {
            sPool.recycle(this);
        }
    }

    public AppStandbyController(android.content.Context context) {
        this(new com.android.server.usage.AppStandbyController.Injector(context, com.android.server.AppSchedulingModuleThread.get().getLooper()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    AppStandbyController(com.android.server.usage.AppStandbyController.Injector injector) {
        this.mAppIdleLock = new com.android.server.usage.AppStandbyController.Lock();
        this.mPackageAccessListeners = new java.util.ArrayList<>();
        this.mCarrierPrivilegedLock = new com.android.server.usage.AppStandbyController.Lock();
        this.mActiveAdminApps = new android.util.SparseArray<>();
        this.mAdminProtectedPackages = new android.util.SparseArray<>();
        this.mHeadlessSystemApps = new android.util.ArraySet<>();
        this.mAdminDataAvailableLatch = new java.util.concurrent.CountDownLatch(1);
        this.mPendingIdleStateChecks = new android.util.SparseLongArray();
        this.mSystemExemptionAppOpMode = new android.util.SparseIntArray();
        java.lang.Object[] objArr = 0;
        this.mCachedNetworkScorer = null;
        this.mCachedNetworkScorerAtMillis = 0L;
        this.mCachedDeviceProvisioningPackage = null;
        this.mCheckIdleIntervalMillis = java.lang.Math.min(DEFAULT_ELAPSED_TIME_THRESHOLDS[1] / 4, 14400000L);
        this.mAppStandbyScreenThresholds = DEFAULT_SCREEN_TIME_THRESHOLDS;
        this.mAppStandbyElapsedThresholds = DEFAULT_ELAPSED_TIME_THRESHOLDS;
        this.mStrongUsageTimeoutMillis = 3600000L;
        this.mNotificationSeenTimeoutMillis = 43200000L;
        this.mSlicePinnedTimeoutMillis = 43200000L;
        this.mNotificationSeenPromotedBucket = 20;
        this.mTriggerQuotaBumpOnNotificationSeen = false;
        this.mRetainNotificationSeenImpactForPreTApps = false;
        this.mSystemUpdateUsageTimeoutMillis = com.android.server.usage.AppStandbyController.ConstantsObserver.DEFAULT_SYSTEM_UPDATE_TIMEOUT;
        this.mPredictionTimeoutMillis = 43200000L;
        this.mSyncAdapterTimeoutMillis = 600000L;
        this.mExemptedSyncScheduledNonDozeTimeoutMillis = 600000L;
        this.mExemptedSyncScheduledDozeTimeoutMillis = 14400000L;
        this.mExemptedSyncStartTimeoutMillis = 600000L;
        this.mUnexemptedSyncScheduledTimeoutMillis = 600000L;
        this.mSystemInteractionTimeoutMillis = 600000L;
        this.mInitialForegroundServiceStartTimeoutMillis = 1800000L;
        this.mLinkCrossProfileApps = true;
        this.mBroadcastResponseWindowDurationMillis = 120000L;
        this.mBroadcastResponseFgThresholdState = 2;
        this.mBroadcastSessionsDurationMs = 120000L;
        this.mBroadcastSessionsWithResponseDurationMs = 120000L;
        this.mNoteResponseEventForAllBroadcastSessions = true;
        this.mBroadcastResponseExemptedRoles = "";
        this.mBroadcastResponseExemptedRolesList = java.util.Collections.EMPTY_LIST;
        this.mBroadcastResponseExemptedPermissions = "";
        this.mBroadcastResponseExemptedPermissionsList = java.util.Collections.EMPTY_LIST;
        this.mAppStandbyProperties = new android.util.ArrayMap();
        this.mAppsToRestoreToRare = new android.util.SparseSetArray<>();
        this.mSystemPackagesAppIds = new java.util.ArrayList<>();
        this.mSystemServicesReady = false;
        this.mDisplayListener = new android.hardware.display.DisplayManager.DisplayListener() { // from class: com.android.server.usage.AppStandbyController.2
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int displayId) {
                if (displayId == 0) {
                    boolean displayOn = com.android.server.usage.AppStandbyController.this.isDisplayOn();
                    synchronized (com.android.server.usage.AppStandbyController.this.mAppIdleLock) {
                        com.android.server.usage.AppStandbyController.this.mAppIdleHistory.updateDisplay(displayOn, com.android.server.usage.AppStandbyController.this.mInjector.elapsedRealtime());
                    }
                }
            }
        };
        this.mAppStandbyControllerWrapper = new com.android.server.usage.AppStandbyController.AppStandbyControllerWrapper();
        this.mAppStandByExt = (com.android.server.usage.IAppStandbyControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.usage.IAppStandbyControllerExt.class).base(this).create();
        this.mInjector = injector;
        this.mContext = this.mInjector.getContext();
        this.mHandler = new com.android.server.usage.AppStandbyController.AppStandbyHandler(this.mInjector.getLooper());
        this.mPackageManager = this.mContext.getPackageManager();
        com.android.server.usage.AppStandbyController.DeviceStateReceiver deviceStateReceiver = new com.android.server.usage.AppStandbyController.DeviceStateReceiver();
        android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.os.action.CHARGING");
        intentFilter.addAction("android.os.action.DISCHARGING");
        intentFilter.addAction("android.os.action.POWER_SAVE_WHITELIST_CHANGED");
        this.mContext.registerReceiver(deviceStateReceiver, intentFilter);
        synchronized (this.mAppIdleLock) {
            this.mAppIdleHistory = new com.android.server.usage.AppIdleHistory(this.mInjector.getDataSystemDirectory(), this.mInjector.elapsedRealtime());
        }
        android.content.IntentFilter intentFilter2 = new android.content.IntentFilter();
        intentFilter2.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter2.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter2.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter2.addDataScheme("package");
        this.mContext.registerReceiverAsUser(new com.android.server.usage.AppStandbyController.PackageReceiver(), android.os.UserHandle.ALL, intentFilter2, null, this.mHandler);
        this.mAppStandByExt.initConstructor(this.mContext, this, this.mAppIdleHistory, this.mHandler);
    }

    void setAppIdleEnabled(boolean enabled) {
        android.app.usage.UsageStatsManagerInternal usmi = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
        if (enabled) {
            usmi.registerListener(this);
        } else {
            usmi.unregisterListener(this);
        }
        synchronized (this.mAppIdleLock) {
            if (this.mAppIdleEnabled != enabled) {
                boolean oldParoleState = isInParole();
                this.mAppIdleEnabled = enabled;
                if (isInParole() != oldParoleState) {
                    postParoleStateChanged();
                }
            }
        }
    }

    public boolean isAppIdleEnabled() {
        return this.mAppIdleEnabled;
    }

    public void onBootPhase(int phase) throws java.lang.Throwable {
        boolean userFileExists;
        this.mInjector.onBootPhase(phase);
        if (phase == 500) {
            android.util.Slog.d(TAG, "Setting app idle enabled state");
            if (this.mAppIdleEnabled) {
                ((android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class)).registerListener(this);
            }
            com.android.server.usage.AppStandbyController.ConstantsObserver settingsObserver = new com.android.server.usage.AppStandbyController.ConstantsObserver(this.mHandler);
            settingsObserver.start();
            this.mAppWidgetManager = (android.appwidget.AppWidgetManager) this.mContext.getSystemService(android.appwidget.AppWidgetManager.class);
            this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
            com.android.internal.app.IAppOpsService iAppOpsService = this.mInjector.getAppOpsService();
            try {
                iAppOpsService.startWatchingMode(128, (java.lang.String) null, new com.android.internal.app.IAppOpsCallback.Stub() { // from class: com.android.server.usage.AppStandbyController.1
                    public void opChanged(int op, int uid, java.lang.String packageName, java.lang.String persistentDeviceId) {
                        int userId = android.os.UserHandle.getUserId(uid);
                        synchronized (com.android.server.usage.AppStandbyController.this.mSystemExemptionAppOpMode) {
                            com.android.server.usage.AppStandbyController.this.mSystemExemptionAppOpMode.delete(uid);
                        }
                        com.android.server.usage.AppStandbyController.this.mHandler.obtainMessage(11, userId, uid, packageName).sendToTarget();
                    }
                });
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(TAG, "Failed start watching for app op", e);
            }
            this.mInjector.registerDisplayListener(this.mDisplayListener, this.mHandler);
            synchronized (this.mAppIdleLock) {
                this.mAppIdleHistory.updateDisplay(isDisplayOn(), this.mInjector.elapsedRealtime());
            }
            this.mSystemServicesReady = true;
            synchronized (this.mAppIdleLock) {
                userFileExists = this.mAppIdleHistory.userFileExists(0);
            }
            if (this.mPendingInitializeDefaults || !userFileExists) {
                initializeDefaultsForSystemApps(0);
            }
            if (!com.android.server.usage.Flags.avoidIdleCheck() && this.mPendingOneTimeCheckIdleStates) {
                postOneTimeCheckIdleStates();
            }
            java.util.List<android.content.pm.ApplicationInfo> systemApps = this.mPackageManager.getInstalledApplications(SYSTEM_PACKAGE_FLAGS);
            int size = systemApps.size();
            for (int i = 0; i < size; i++) {
                android.content.pm.ApplicationInfo appInfo = systemApps.get(i);
                this.mSystemPackagesAppIds.add(java.lang.Integer.valueOf(android.os.UserHandle.getAppId(appInfo.uid)));
            }
            return;
        }
        if (phase == 1000) {
            setChargingState(this.mInjector.isCharging());
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.usage.AppStandbyController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.updatePowerWhitelistCache();
                }
            });
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.usage.AppStandbyController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.loadHeadlessSystemAppCache();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:34:? -> B:26:0x0087). Please report as a decompilation issue!!! */
    public void reportContentProviderUsage(java.lang.String authority, java.lang.String providerPkgName, int userId) throws java.lang.Throwable {
        int i;
        java.lang.Object obj;
        if (this.mAppIdleEnabled) {
            java.lang.String[] packages = android.content.ContentResolver.getSyncAdapterPackagesForAuthorityAsUser(authority, userId);
            android.content.pm.PackageManagerInternal pmi = this.mInjector.getPackageManagerInternal();
            long elapsedRealtime = this.mInjector.elapsedRealtime();
            int length = packages.length;
            int i2 = 0;
            while (i2 < length) {
                java.lang.String packageName = packages[i2];
                if (this.mAppStandByExt.matchGoogleRestrictRule(packageName)) {
                    return;
                }
                if (packageName.equals(providerPkgName)) {
                    i = i2;
                } else {
                    int appId = android.os.UserHandle.getAppId(pmi.getPackageUid(packageName, 0L, userId));
                    if (this.mSystemPackagesAppIds.contains(java.lang.Integer.valueOf(appId))) {
                        java.util.List<android.os.UserHandle> linkedProfiles = getCrossProfileTargets(packageName, userId);
                        java.lang.Object obj2 = this.mAppIdleLock;
                        synchronized (obj2) {
                            try {
                                this.mAppStandByExt.uploadAABPredictInfoWhenReportEvent(null, packageName, 10, 8, userId);
                                obj = obj2;
                                i = i2;
                            } catch (java.lang.Throwable th) {
                                th = th;
                                obj = obj2;
                                throw th;
                            }
                            try {
                                reportNoninteractiveUsageCrossUserLocked(packageName, userId, 10, 8, elapsedRealtime, this.mSyncAdapterTimeoutMillis, linkedProfiles);
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        }
                    } else {
                        i = i2;
                    }
                }
                i2 = i + 1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportExemptedSyncScheduled(java.lang.String packageName, int userId) {
        int bucketToPromote;
        int usageReason;
        long durationMillis;
        if (!this.mAppIdleEnabled || this.mAppStandByExt.matchGoogleRestrictRule(packageName)) {
            return;
        }
        if (!this.mInjector.isDeviceIdleMode()) {
            bucketToPromote = 10;
            usageReason = 11;
            durationMillis = this.mExemptedSyncScheduledNonDozeTimeoutMillis;
        } else {
            bucketToPromote = 20;
            usageReason = 12;
            durationMillis = this.mExemptedSyncScheduledDozeTimeoutMillis;
        }
        long elapsedRealtime = this.mInjector.elapsedRealtime();
        java.util.List<android.os.UserHandle> linkedProfiles = getCrossProfileTargets(packageName, userId);
        synchronized (this.mAppIdleLock) {
            reportNoninteractiveUsageCrossUserLocked(packageName, userId, bucketToPromote, usageReason, elapsedRealtime, durationMillis, linkedProfiles);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportUnexemptedSyncScheduled(java.lang.String packageName, int userId) {
        if (!this.mAppIdleEnabled || this.mAppStandByExt.matchGoogleRestrictRule(packageName)) {
            return;
        }
        long elapsedRealtime = this.mInjector.elapsedRealtime();
        synchronized (this.mAppIdleLock) {
            int currentBucket = this.mAppIdleHistory.getAppStandbyBucket(packageName, userId, elapsedRealtime);
            if (currentBucket == 50) {
                java.util.List<android.os.UserHandle> linkedProfiles = getCrossProfileTargets(packageName, userId);
                this.mAppStandByExt.uploadAABPredictInfoWhenReportEvent(null, packageName, 20, 13, userId);
                reportNoninteractiveUsageCrossUserLocked(packageName, userId, 20, 14, elapsedRealtime, this.mUnexemptedSyncScheduledTimeoutMillis, linkedProfiles);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportExemptedSyncStart(java.lang.String packageName, int userId) throws java.lang.Throwable {
        if (!this.mAppIdleEnabled || this.mAppStandByExt.matchGoogleRestrictRule(packageName)) {
            return;
        }
        long elapsedRealtime = this.mInjector.elapsedRealtime();
        java.util.List<android.os.UserHandle> linkedProfiles = getCrossProfileTargets(packageName, userId);
        synchronized (this.mAppIdleLock) {
            try {
                try {
                    this.mAppStandByExt.uploadAABPredictInfoWhenReportEvent(null, packageName, 10, 13, userId);
                    reportNoninteractiveUsageCrossUserLocked(packageName, userId, 10, 13, elapsedRealtime, this.mExemptedSyncStartTimeoutMillis, linkedProfiles);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    private void reportNoninteractiveUsageCrossUserLocked(java.lang.String packageName, int userId, int bucket, int subReason, long elapsedRealtime, long nextCheckDelay, java.util.List<android.os.UserHandle> otherProfiles) {
        reportNoninteractiveUsageLocked(packageName, userId, bucket, subReason, elapsedRealtime, nextCheckDelay);
        int size = otherProfiles.size();
        for (int profileIndex = 0; profileIndex < size; profileIndex++) {
            int otherUserId = otherProfiles.get(profileIndex).getIdentifier();
            reportNoninteractiveUsageLocked(packageName, otherUserId, bucket, subReason, elapsedRealtime, nextCheckDelay);
        }
    }

    private void reportNoninteractiveUsageLocked(java.lang.String packageName, int userId, int bucket, int subReason, long elapsedRealtime, long nextCheckDelay) {
        com.android.server.usage.AppIdleHistory.AppUsageHistory appUsage = this.mAppIdleHistory.reportUsage(packageName, userId, bucket, subReason, 0L, elapsedRealtime + nextCheckDelay);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(11, userId, -1, packageName), nextCheckDelay);
        maybeInformListeners(packageName, userId, elapsedRealtime, appUsage.currentBucket, appUsage.bucketingReason, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void triggerListenerQuotaBump(java.lang.String packageName, int userId) {
        if (this.mAppIdleEnabled) {
            synchronized (this.mPackageAccessListeners) {
                for (com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener listener : this.mPackageAccessListeners) {
                    listener.triggerTemporaryQuotaBump(packageName, userId);
                }
            }
        }
    }

    void setChargingState(boolean isCharging) {
        if (this.mIsCharging != isCharging) {
            this.mIsCharging = isCharging;
            postParoleStateChanged();
        }
    }

    public boolean isInParole() {
        return !this.mAppIdleEnabled || this.mIsCharging;
    }

    private void postParoleStateChanged() {
        this.mHandler.removeMessages(9);
        this.mHandler.sendEmptyMessage(9);
    }

    public void postCheckIdleStates(int userId) {
        if (userId == -1) {
            postOneTimeCheckIdleStates();
            return;
        }
        synchronized (this.mPendingIdleStateChecks) {
            this.mPendingIdleStateChecks.put(userId, this.mInjector.elapsedRealtime());
        }
        this.mHandler.obtainMessage(5).sendToTarget();
    }

    public void postOneTimeCheckIdleStates() {
        if (this.mInjector.getBootPhase() < 500) {
            this.mPendingOneTimeCheckIdleStates = true;
        } else {
            this.mHandler.sendEmptyMessage(10);
            this.mPendingOneTimeCheckIdleStates = false;
        }
    }

    boolean checkIdleStates(int checkUserId) throws java.lang.Throwable {
        if (!this.mAppIdleEnabled) {
            return false;
        }
        try {
            int[] runningUserIds = this.mInjector.getRunningUserIds();
            if (checkUserId != -1) {
                if (!com.android.internal.util.ArrayUtils.contains(runningUserIds, checkUserId)) {
                    return false;
                }
            }
            long elapsedRealtime = this.mInjector.elapsedRealtime();
            for (int userId : runningUserIds) {
                if (checkUserId == -1 || checkUserId == userId) {
                    java.util.List<android.content.pm.PackageInfo> packages = this.mPackageManager.getInstalledPackagesAsUser(512, userId);
                    int packageCount = packages.size();
                    for (int p = 0; p < packageCount; p++) {
                        android.content.pm.PackageInfo pi = packages.get(p);
                        java.lang.String packageName = pi.packageName;
                        checkAndUpdateStandbyState(packageName, userId, pi.applicationInfo.uid, elapsedRealtime);
                    }
                }
            }
            return true;
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:67:0x010e  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0121 A[Catch: all -> 0x0163, TRY_LEAVE, TryCatch #0 {all -> 0x0163, blocks: (B:48:0x00c5, B:52:0x00d1, B:56:0x00d9, B:57:0x00dc, B:59:0x00e7, B:61:0x00f2, B:63:0x0100, B:73:0x0121), top: B:91:0x00c5 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void checkAndUpdateStandbyState(java.lang.String r28, int r29, int r30, long r31) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 368
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usage.AppStandbyController.checkAndUpdateStandbyState(java.lang.String, int, int, long):void");
    }

    private boolean predictionTimedOut(com.android.server.usage.AppIdleHistory.AppUsageHistory app, long elapsedRealtime) {
        return app.lastPredictedTime > 0 && this.mAppIdleHistory.getElapsedTime(elapsedRealtime) - app.lastPredictedTime > this.mPredictionTimeoutMillis;
    }

    private void maybeInformListeners(java.lang.String packageName, int userId, long elapsedRealtime, int bucket, int reason, boolean userStartedInteracting) {
        synchronized (this.mAppIdleLock) {
            if (this.mAppIdleHistory.shouldInformListeners(packageName, userId, elapsedRealtime, bucket)) {
                com.android.server.usage.AppStandbyController.StandbyUpdateRecord r = com.android.server.usage.AppStandbyController.StandbyUpdateRecord.obtain(packageName, userId, bucket, reason, userStartedInteracting);
                this.mHandler.sendMessage(this.mHandler.obtainMessage(3, r));
            }
        }
    }

    private int getBucketForLocked(java.lang.String packageName, int userId, long elapsedRealtime) {
        int bucketIndex = this.mAppIdleHistory.getThresholdIndex(packageName, userId, elapsedRealtime, this.mAppStandbyScreenThresholds, this.mAppStandbyElapsedThresholds);
        if (bucketIndex >= 0) {
            return THRESHOLD_BUCKETS[bucketIndex];
        }
        return 50;
    }

    private void notifyBatteryStats(java.lang.String packageName, int userId, boolean idle) {
        try {
            int uid = this.mPackageManager.getPackageUidAsUser(packageName, 8192, userId);
            if (idle) {
                this.mInjector.noteEvent(15, packageName, uid);
            } else {
                this.mInjector.noteEvent(16, packageName, uid);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException | android.os.RemoteException e) {
        }
    }

    @Override // android.app.usage.UsageStatsManagerInternal.UsageEventListener
    public void onUsageEvent(int userId, android.app.usage.UsageEvents.Event event) throws java.lang.Throwable {
        if (!this.mAppIdleEnabled) {
            return;
        }
        int eventType = event.getEventType();
        if (eventType == 1 || eventType == 2 || eventType == 6 || eventType == 7 || eventType == 10 || eventType == 14 || eventType == 13 || eventType == 19) {
            java.lang.String pkg = event.getPackageName();
            java.util.List<android.os.UserHandle> linkedProfiles = getCrossProfileTargets(pkg, userId);
            synchronized (this.mAppIdleLock) {
                try {
                    try {
                        long elapsedRealtime = this.mInjector.elapsedRealtime();
                        if (this.mAppStandByExt.interceptReportEvent(event, elapsedRealtime, userId)) {
                            return;
                        }
                        reportEventLocked(pkg, eventType, elapsedRealtime, userId);
                        int size = linkedProfiles.size();
                        for (int profileIndex = 0; profileIndex < size; profileIndex++) {
                            int linkedUserId = linkedProfiles.get(profileIndex).getIdentifier();
                            reportEventLocked(pkg, eventType, elapsedRealtime, linkedUserId);
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }
    }

    private void reportEventLocked(java.lang.String pkg, int eventType, long elapsedRealtime, int userId) {
        int reason;
        int prevBucketReason;
        int prevBucket;
        int i;
        boolean previouslyIdle;
        com.android.server.usage.AppIdleHistory.AppUsageHistory appHistory;
        long nextCheckDelay;
        int i2;
        com.android.server.usage.AppIdleHistory.AppUsageHistory appHistory2;
        boolean previouslyIdle2;
        java.lang.String str;
        int notificationSeenPromotedBucket;
        long notificationSeenTimeoutMillis;
        boolean previouslyIdle3 = this.mAppIdleHistory.isIdle(pkg, userId, elapsedRealtime);
        com.android.server.usage.AppIdleHistory.AppUsageHistory appHistory3 = this.mAppIdleHistory.getAppUsageHistory(pkg, userId, elapsedRealtime);
        int prevBucket2 = appHistory3.currentBucket;
        int prevBucketReason2 = appHistory3.bucketingReason;
        int subReason = usageEventToSubReason(eventType);
        int reason2 = subReason | 768;
        if (eventType == 10) {
            if (this.mRetainNotificationSeenImpactForPreTApps && getTargetSdkVersion(pkg) < 33) {
                notificationSeenPromotedBucket = 20;
                notificationSeenTimeoutMillis = 43200000;
            } else {
                if (this.mTriggerQuotaBumpOnNotificationSeen) {
                    this.mHandler.obtainMessage(7, userId, -1, pkg).sendToTarget();
                }
                int notificationSeenPromotedBucket2 = this.mNotificationSeenPromotedBucket;
                notificationSeenPromotedBucket = notificationSeenPromotedBucket2;
                notificationSeenTimeoutMillis = this.mNotificationSeenTimeoutMillis;
            }
            reason = reason2;
            this.mAppStandByExt.uploadAABPredictInfoWhenReportEvent(appHistory3, pkg, this.mNotificationSeenPromotedBucket, subReason, userId);
            prevBucketReason = prevBucketReason2;
            prevBucket = prevBucket2;
            i = userId;
            this.mAppIdleHistory.reportUsage(appHistory3, pkg, userId, notificationSeenPromotedBucket, subReason, 0L, elapsedRealtime + notificationSeenTimeoutMillis);
            long nextCheckDelay2 = notificationSeenTimeoutMillis;
            nextCheckDelay = nextCheckDelay2;
            previouslyIdle = previouslyIdle3;
            appHistory = appHistory3;
        } else {
            reason = reason2;
            prevBucketReason = prevBucketReason2;
            prevBucket = prevBucket2;
            i = userId;
            if (eventType == 14) {
                this.mAppStandByExt.uploadAABPredictInfoWhenReportEvent(appHistory3, pkg, 20, subReason, userId);
                prevBucket = prevBucket;
                i = i;
                this.mAppIdleHistory.reportUsage(appHistory3, pkg, userId, 20, subReason, 0L, elapsedRealtime + this.mSlicePinnedTimeoutMillis);
                nextCheckDelay = this.mSlicePinnedTimeoutMillis;
                previouslyIdle = previouslyIdle3;
                appHistory = appHistory3;
            } else if (eventType == 6) {
                this.mAppStandByExt.uploadAABPredictInfoWhenReportEvent(appHistory3, pkg, 10, subReason, userId);
                prevBucket = prevBucket;
                i = i;
                this.mAppIdleHistory.reportUsage(appHistory3, pkg, userId, 10, subReason, 0L, elapsedRealtime + this.mSystemInteractionTimeoutMillis);
                nextCheckDelay = this.mSystemInteractionTimeoutMillis;
                previouslyIdle = previouslyIdle3;
                appHistory = appHistory3;
            } else if (eventType == 19) {
                if (prevBucket != 50) {
                    return;
                }
                this.mAppStandByExt.uploadAABPredictInfoWhenReportEvent(appHistory3, pkg, 10, subReason, userId);
                prevBucket = prevBucket;
                appHistory = appHistory3;
                previouslyIdle = previouslyIdle3;
                i = i;
                this.mAppIdleHistory.reportUsage(appHistory3, pkg, userId, 10, subReason, 0L, elapsedRealtime + this.mInitialForegroundServiceStartTimeoutMillis);
                nextCheckDelay = this.mInitialForegroundServiceStartTimeoutMillis;
            } else {
                previouslyIdle = previouslyIdle3;
                appHistory = appHistory3;
                this.mAppIdleHistory.reportUsage(appHistory, pkg, userId, 10, subReason, elapsedRealtime, elapsedRealtime + this.mStrongUsageTimeoutMillis);
                nextCheckDelay = this.mStrongUsageTimeoutMillis;
            }
        }
        if (appHistory.currentBucket == prevBucket) {
            i2 = i;
            appHistory2 = appHistory;
            previouslyIdle2 = previouslyIdle;
            str = pkg;
        } else {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(11, i, -1, pkg), nextCheckDelay);
            boolean userStartedInteracting = appHistory.currentBucket == 10 && (prevBucketReason & 65280) != 768;
            i2 = i;
            appHistory2 = appHistory;
            previouslyIdle2 = previouslyIdle;
            str = pkg;
            maybeInformListeners(pkg, userId, elapsedRealtime, appHistory.currentBucket, reason, userStartedInteracting);
        }
        boolean stillIdle = appHistory2.currentBucket >= 40;
        if (previouslyIdle2 != stillIdle) {
            notifyBatteryStats(str, i2, stillIdle);
        }
    }

    private int getTargetSdkVersion(java.lang.String packageName) {
        return this.mInjector.getPackageManagerInternal().getPackageTargetSdkVersion(packageName);
    }

    private int getMinBucketWithValidExpiryTime(com.android.server.usage.AppIdleHistory.AppUsageHistory usageHistory, int targetBucket, long elapsedTimeMs) {
        if (usageHistory.bucketExpiryTimesMs == null) {
            return -1;
        }
        int size = usageHistory.bucketExpiryTimesMs.size();
        for (int i = 0; i < size; i++) {
            int bucket = usageHistory.bucketExpiryTimesMs.keyAt(i);
            if (targetBucket <= bucket) {
                break;
            }
            long expiryTimeMs = usageHistory.bucketExpiryTimesMs.valueAt(i);
            if (expiryTimeMs > elapsedTimeMs) {
                return bucket;
            }
        }
        return -1;
    }

    private java.util.List<android.os.UserHandle> getCrossProfileTargets(java.lang.String pkg, int userId) {
        synchronized (this.mAppIdleLock) {
            if (this.mLinkCrossProfileApps) {
                return this.mInjector.getValidCrossProfileTargets(pkg, userId);
            }
            return java.util.Collections.emptyList();
        }
    }

    private int usageEventToSubReason(int eventType) {
        switch (eventType) {
            case 1:
                return 4;
            case 2:
                return 5;
            case 6:
                return 1;
            case 7:
                return 3;
            case 10:
                return 2;
            case 13:
                return 10;
            case 14:
                return 9;
            case 19:
                return 15;
            default:
                return 0;
        }
    }

    void forceIdleState(java.lang.String packageName, int userId, boolean idle) throws java.lang.Throwable {
        int appId;
        int standbyBucket;
        if (this.mAppIdleEnabled && (appId = getAppId(packageName)) >= 0) {
            int minBucket = getAppMinBucket(packageName, appId, userId);
            if (idle && minBucket < 40) {
                android.util.Slog.e(TAG, "Tried to force an app to be idle when its min bucket is " + android.app.usage.UsageStatsManager.standbyBucketToString(minBucket));
                return;
            }
            long elapsedRealtime = this.mInjector.elapsedRealtime();
            boolean previouslyIdle = isAppIdleFiltered(packageName, appId, userId, elapsedRealtime);
            synchronized (this.mAppIdleLock) {
                try {
                    standbyBucket = this.mAppIdleHistory.setIdle(packageName, userId, idle, elapsedRealtime);
                } catch (java.lang.Throwable th) {
                    th = th;
                    while (true) {
                        try {
                            throw th;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    }
                }
            }
            boolean stillIdle = isAppIdleFiltered(packageName, appId, userId, elapsedRealtime);
            maybeInformListeners(packageName, userId, elapsedRealtime, standbyBucket, 1024, false);
            if (previouslyIdle != stillIdle) {
                notifyBatteryStats(packageName, userId, stillIdle);
            }
        }
    }

    public void setLastJobRunTime(java.lang.String packageName, int userId, long elapsedRealtime) {
        synchronized (this.mAppIdleLock) {
            this.mAppIdleHistory.setLastJobRunTime(packageName, userId, elapsedRealtime);
        }
    }

    public long getTimeSinceLastJobRun(java.lang.String packageName, int userId) {
        long timeSinceLastJobRun;
        long elapsedRealtime = this.mInjector.elapsedRealtime();
        synchronized (this.mAppIdleLock) {
            timeSinceLastJobRun = this.mAppIdleHistory.getTimeSinceLastJobRun(packageName, userId, elapsedRealtime);
        }
        return timeSinceLastJobRun;
    }

    public void setEstimatedLaunchTime(java.lang.String packageName, int userId, long launchTime) {
        long nowElapsed = this.mInjector.elapsedRealtime();
        synchronized (this.mAppIdleLock) {
            this.mAppIdleHistory.setEstimatedLaunchTime(packageName, userId, nowElapsed, launchTime);
        }
    }

    public long getEstimatedLaunchTime(java.lang.String packageName, int userId) {
        long estimatedLaunchTime;
        long elapsedRealtime = this.mInjector.elapsedRealtime();
        synchronized (this.mAppIdleLock) {
            estimatedLaunchTime = this.mAppIdleHistory.getEstimatedLaunchTime(packageName, userId, elapsedRealtime);
        }
        return estimatedLaunchTime;
    }

    public long getTimeSinceLastUsedByUser(java.lang.String packageName, int userId) {
        long timeSinceLastUsedByUser;
        long elapsedRealtime = this.mInjector.elapsedRealtime();
        synchronized (this.mAppIdleLock) {
            timeSinceLastUsedByUser = this.mAppIdleHistory.getTimeSinceLastUsedByUser(packageName, userId, elapsedRealtime);
        }
        return timeSinceLastUsedByUser;
    }

    public void onUserRemoved(int userId) {
        synchronized (this.mAppIdleLock) {
            this.mAppIdleHistory.onUserRemoved(userId);
            synchronized (this.mActiveAdminApps) {
                this.mActiveAdminApps.remove(userId);
            }
            synchronized (this.mAdminProtectedPackages) {
                this.mAdminProtectedPackages.remove(userId);
            }
        }
    }

    private boolean isAppIdleUnfiltered(java.lang.String packageName, int userId, long elapsedRealtime) {
        boolean zIsIdle;
        synchronized (this.mAppIdleLock) {
            zIsIdle = this.mAppIdleHistory.isIdle(packageName, userId, elapsedRealtime);
        }
        return zIsIdle;
    }

    public void addListener(com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener listener) {
        synchronized (this.mPackageAccessListeners) {
            if (!this.mPackageAccessListeners.contains(listener)) {
                this.mPackageAccessListeners.add(listener);
            }
        }
    }

    public void removeListener(com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener listener) {
        synchronized (this.mPackageAccessListeners) {
            this.mPackageAccessListeners.remove(listener);
        }
    }

    public int getAppId(java.lang.String packageName) {
        try {
            android.content.pm.ApplicationInfo ai = this.mPackageManager.getApplicationInfo(packageName, 4194816);
            return ai.uid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    public boolean isAppIdleFiltered(java.lang.String packageName, int userId, long elapsedRealtime, boolean shouldObfuscateInstantApps) {
        if (shouldObfuscateInstantApps && this.mInjector.isPackageEphemeral(userId, packageName)) {
            return false;
        }
        return isAppIdleFiltered(packageName, getAppId(packageName), userId, elapsedRealtime);
    }

    private int getAppMinBucket(java.lang.String packageName, int userId) {
        try {
            int uid = this.mPackageManager.getPackageUidAsUser(packageName, userId);
            return getAppMinBucket(packageName, android.os.UserHandle.getAppId(uid), userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return 50;
        }
    }

    private int getAppMinBucket(java.lang.String packageName, int appId, int userId) {
        if (packageName == null) {
            return 50;
        }
        if (!this.mAppIdleEnabled || appId < 10000 || packageName.equals(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME) || this.mAppStandByExt.isCustomizeDozeModeDisabled()) {
            return 5;
        }
        if (this.mSystemServicesReady) {
            if (this.mAppStandByExt.matchGoogleRestrictRule(packageName)) {
                return 50;
            }
            if (this.mInjector.isNonIdleWhitelisted(packageName) || isActiveDeviceAdmin(packageName, userId) || isAdminProtectedPackages(packageName, userId) || isActiveNetworkScorer(packageName)) {
                return 5;
            }
            int uid = android.os.UserHandle.getUid(userId, appId);
            synchronized (this.mSystemExemptionAppOpMode) {
                if (this.mSystemExemptionAppOpMode.indexOfKey(uid) >= 0) {
                    if (this.mSystemExemptionAppOpMode.get(uid) == 0) {
                        return 5;
                    }
                } else {
                    int mode = this.mAppOpsManager.checkOpNoThrow(128, uid, packageName);
                    this.mSystemExemptionAppOpMode.put(uid, mode);
                    if (mode == 0) {
                        return 5;
                    }
                }
                if (this.mAppWidgetManager != null && this.mInjector.isBoundWidgetPackage(this.mAppWidgetManager, packageName, userId)) {
                    return 10;
                }
                if (isDeviceProvisioningPackage(packageName)) {
                    return 5;
                }
                if (this.mInjector.isWellbeingPackage(packageName) || this.mInjector.shouldGetExactAlarmBucketElevation(packageName, android.os.UserHandle.getUid(userId, appId))) {
                    return 20;
                }
            }
        }
        if (isCarrierApp(packageName)) {
            return 5;
        }
        if (this.mSystemServicesReady && this.mAppStandByExt.isSystemApp(packageName, userId)) {
            return 5;
        }
        if (isHeadlessSystemApp(packageName)) {
            return 10;
        }
        if (this.mPackageManager.checkPermission("android.permission.ACCESS_BACKGROUND_LOCATION", packageName) != 0) {
            return 50;
        }
        return 30;
    }

    private boolean isHeadlessSystemApp(java.lang.String packageName) {
        boolean zContains;
        synchronized (this.mHeadlessSystemApps) {
            zContains = this.mHeadlessSystemApps.contains(packageName);
        }
        return zContains;
    }

    public boolean isAppIdleFiltered(java.lang.String packageName, int appId, int userId, long elapsedRealtime) {
        return this.mAppIdleEnabled && !this.mIsCharging && isAppIdleUnfiltered(packageName, userId, elapsedRealtime) && getAppMinBucket(packageName, appId, userId) >= 40;
    }

    static boolean isUserUsage(int reason) {
        if ((65280 & reason) != 768) {
            return false;
        }
        int subReason = reason & 255;
        return subReason == 3 || subReason == 4;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0082  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int[] getIdleUidsForUser(int r19) {
        /*
            r18 = this;
            r6 = r18
            boolean r0 = r6.mAppIdleEnabled
            if (r0 != 0) goto L9
            int[] r0 = libcore.util.EmptyArray.INT
            return r0
        L9:
            java.lang.String r0 = "getIdleUidsForUser"
            r7 = 64
            android.os.Trace.traceBegin(r7, r0)
            com.android.server.usage.AppStandbyController$Injector r0 = r6.mInjector
            long r9 = r0.elapsedRealtime()
            com.android.server.usage.AppStandbyController$Injector r0 = r6.mInjector
            android.content.pm.PackageManagerInternal r11 = r0.getPackageManagerInternal()
            r0 = 0
            int r2 = android.os.Process.myUid()
            r12 = r19
            java.util.List r13 = r11.getInstalledApplications(r0, r12, r2)
            if (r13 != 0) goto L2d
            int[] r0 = libcore.util.EmptyArray.INT
            return r0
        L2d:
            android.util.SparseBooleanArray r0 = new android.util.SparseBooleanArray
            r0.<init>()
            r14 = r0
            r0 = 0
            int r1 = r13.size()
            r15 = 1
            int r1 = r1 - r15
            r16 = r0
            r4 = r1
        L3d:
            if (r4 < 0) goto L8b
            java.lang.Object r0 = r13.get(r4)
            r5 = r0
            android.content.pm.ApplicationInfo r5 = (android.content.pm.ApplicationInfo) r5
            int r0 = r5.uid
            int r3 = r14.indexOfKey(r0)
            if (r3 >= 0) goto L50
            r0 = r15
            goto L54
        L50:
            boolean r0 = r14.valueAt(r3)
        L54:
            r17 = r0
            if (r17 == 0) goto L70
            java.lang.String r1 = r5.packageName
            int r0 = r5.uid
            int r2 = android.os.UserHandle.getAppId(r0)
            r0 = r18
            r7 = r3
            r3 = r19
            r8 = r4
            r15 = r5
            r4 = r9
            boolean r0 = r0.isAppIdleFiltered(r1, r2, r3, r4)
            if (r0 == 0) goto L73
            r0 = 1
            goto L74
        L70:
            r7 = r3
            r8 = r4
            r15 = r5
        L73:
            r0 = 0
        L74:
            if (r17 == 0) goto L7a
            if (r0 != 0) goto L7a
            int r16 = r16 + 1
        L7a:
            if (r7 >= 0) goto L82
            int r1 = r15.uid
            r14.put(r1, r0)
            goto L85
        L82:
            r14.setValueAt(r7, r0)
        L85:
            int r4 = r8 + (-1)
            r7 = 64
            r15 = 1
            goto L3d
        L8b:
            r8 = r4
            int r0 = r14.size()
            int r0 = r0 - r16
            int[] r1 = new int[r0]
            int r2 = r14.size()
            r3 = 1
            int r2 = r2 - r3
        L9a:
            if (r2 < 0) goto Lad
            boolean r3 = r14.valueAt(r2)
            if (r3 == 0) goto Laa
            int r0 = r0 + (-1)
            int r3 = r14.keyAt(r2)
            r1[r0] = r3
        Laa:
            int r2 = r2 + (-1)
            goto L9a
        Lad:
            r2 = 64
            android.os.Trace.traceEnd(r2)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usage.AppStandbyController.getIdleUidsForUser(int):int[]");
    }

    public void setAppIdleAsync(java.lang.String str, boolean z, int i) {
        if (str == null || !this.mAppIdleEnabled) {
            return;
        }
        this.mHandler.obtainMessage(4, i, z ? 1 : 0, str).sendToTarget();
    }

    public int getAppStandbyBucket(java.lang.String packageName, int userId, long elapsedRealtime, boolean shouldObfuscateInstantApps) {
        int appStandbyBucket;
        if (!this.mAppIdleEnabled) {
            return 5;
        }
        if (shouldObfuscateInstantApps && this.mInjector.isPackageEphemeral(userId, packageName)) {
            return 10;
        }
        synchronized (this.mAppIdleLock) {
            appStandbyBucket = this.mAppIdleHistory.getAppStandbyBucket(packageName, userId, elapsedRealtime);
        }
        return appStandbyBucket;
    }

    public int getAppStandbyBucketReason(java.lang.String packageName, int userId, long elapsedRealtime) {
        int appStandbyReason;
        synchronized (this.mAppIdleLock) {
            appStandbyReason = this.mAppIdleHistory.getAppStandbyReason(packageName, userId, elapsedRealtime);
        }
        return appStandbyReason;
    }

    public java.util.List<android.app.usage.AppStandbyInfo> getAppStandbyBuckets(int userId) {
        java.util.ArrayList<android.app.usage.AppStandbyInfo> appStandbyBuckets;
        synchronized (this.mAppIdleLock) {
            appStandbyBuckets = this.mAppIdleHistory.getAppStandbyBuckets(userId, this.mAppIdleEnabled);
        }
        return appStandbyBuckets;
    }

    public int getAppMinStandbyBucket(java.lang.String packageName, int appId, int userId, boolean shouldObfuscateInstantApps) {
        int appMinBucket;
        if (shouldObfuscateInstantApps && this.mInjector.isPackageEphemeral(userId, packageName)) {
            return 50;
        }
        synchronized (this.mAppIdleLock) {
            appMinBucket = getAppMinBucket(packageName, appId, userId);
        }
        return appMinBucket;
    }

    public void restrictApp(java.lang.String packageName, int userId, int restrictReason) throws java.lang.Throwable {
        restrictApp(packageName, userId, 1536, restrictReason);
    }

    public void restrictApp(java.lang.String packageName, int userId, int mainReason, int restrictReason) throws java.lang.Throwable {
        if (mainReason != 1536 && mainReason != 1024) {
            android.util.Slog.e(TAG, "Tried to restrict app " + packageName + " for an unsupported reason");
        } else {
            if (!this.mInjector.isPackageInstalled(packageName, 0, userId)) {
                android.util.Slog.e(TAG, "Tried to restrict uninstalled app: " + packageName);
                return;
            }
            int reason = (65280 & mainReason) | (restrictReason & 255);
            long nowElapsed = this.mInjector.elapsedRealtime();
            setAppStandbyBucket(packageName, userId, 45, reason, nowElapsed, false);
        }
    }

    public void restoreAppsToRare(java.util.Set<java.lang.String> restoredApps, final int userId) throws java.lang.Throwable {
        long nowElapsed = this.mInjector.elapsedRealtime();
        for (java.lang.String packageName : restoredApps) {
            if (!this.mInjector.isPackageInstalled(packageName, 0, userId)) {
                android.util.Slog.i(TAG, "Tried to restore bucket for uninstalled app: " + packageName);
                this.mAppsToRestoreToRare.add(userId, packageName);
            } else {
                restoreAppToRare(packageName, userId, nowElapsed, 258);
            }
        }
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.usage.AppStandbyController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$restoreAppsToRare$0(userId);
            }
        }, 28800000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreAppsToRare$0(int userId) {
        this.mAppsToRestoreToRare.remove(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreAppToRare(java.lang.String pkgName, int userId, long nowElapsed, int reason) throws java.lang.Throwable {
        int standbyBucket = getAppStandbyBucket(pkgName, userId, nowElapsed, false);
        if (standbyBucket == 50) {
            setAppStandbyBucket(pkgName, userId, 40, reason, nowElapsed, false);
        }
    }

    public void setAppStandbyBucket(java.lang.String packageName, int bucket, int userId, int callingUid, int callingPid) throws java.lang.Throwable {
        setAppStandbyBuckets(java.util.Collections.singletonList(new android.app.usage.AppStandbyInfo(packageName, bucket)), userId, callingUid, callingPid);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0034  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setAppStandbyBuckets(java.util.List<android.app.usage.AppStandbyInfo> r22, int r23, int r24, int r25) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 225
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usage.AppStandbyController.setAppStandbyBuckets(java.util.List, int, int, int):void");
    }

    void setAppStandbyBucket(java.lang.String packageName, int userId, int newBucket, int reason) throws java.lang.Throwable {
        setAppStandbyBucket(packageName, userId, newBucket, reason, this.mInjector.elapsedRealtime(), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x026e A[Catch: all -> 0x0281, TryCatch #0 {all -> 0x0281, blocks: (B:145:0x0242, B:149:0x024d, B:155:0x026e, B:156:0x0271), top: B:169:0x0242 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setAppStandbyBucket(java.lang.String r28, int r29, int r30, int r31, long r32, boolean r34) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 656
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usage.AppStandbyController.setAppStandbyBucket(java.lang.String, int, int, int, long, boolean):void");
    }

    public boolean isActiveDeviceAdmin(java.lang.String packageName, int userId) {
        boolean z;
        synchronized (this.mActiveAdminApps) {
            java.util.Set<java.lang.String> adminPkgs = this.mActiveAdminApps.get(userId);
            z = adminPkgs != null && adminPkgs.contains(packageName);
        }
        return z;
    }

    private boolean isAdminProtectedPackages(java.lang.String packageName, int userId) {
        synchronized (this.mAdminProtectedPackages) {
            boolean z = true;
            if (this.mAdminProtectedPackages.contains(-1) && this.mAdminProtectedPackages.get(-1).contains(packageName)) {
                return true;
            }
            if (!this.mAdminProtectedPackages.contains(userId) || !this.mAdminProtectedPackages.get(userId).contains(packageName)) {
                z = false;
            }
            return z;
        }
    }

    public void addActiveDeviceAdmin(java.lang.String adminPkg, int userId) {
        synchronized (this.mActiveAdminApps) {
            java.util.Set<java.lang.String> adminPkgs = this.mActiveAdminApps.get(userId);
            if (adminPkgs == null) {
                adminPkgs = new android.util.ArraySet();
                this.mActiveAdminApps.put(userId, adminPkgs);
            }
            adminPkgs.add(adminPkg);
        }
    }

    public void setActiveAdminApps(java.util.Set<java.lang.String> adminPkgs, int userId) {
        synchronized (this.mActiveAdminApps) {
            if (adminPkgs == null) {
                this.mActiveAdminApps.remove(userId);
            } else {
                this.mActiveAdminApps.put(userId, adminPkgs);
            }
        }
    }

    public void setAdminProtectedPackages(java.util.Set<java.lang.String> packageNames, int userId) {
        synchronized (this.mAdminProtectedPackages) {
            if (packageNames == null) {
                this.mAdminProtectedPackages.remove(userId);
            } else if (packageNames.isEmpty()) {
                this.mAdminProtectedPackages.remove(userId);
            } else {
                this.mAdminProtectedPackages.put(userId, packageNames);
            }
        }
        if (android.app.admin.flags.Flags.disallowUserControlBgUsageFix() && !com.android.server.usage.Flags.avoidIdleCheck()) {
            postCheckIdleStates(userId);
        }
    }

    public void onAdminDataAvailable() {
        this.mAdminDataAvailableLatch.countDown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void waitForAdminData() {
        if (this.mContext.getPackageManager().hasSystemFeature("android.software.device_admin")) {
            com.android.internal.util.ConcurrentUtils.waitForCountDownNoInterrupt(this.mAdminDataAvailableLatch, 10000L, "Wait for admin data");
        }
    }

    java.util.Set<java.lang.String> getActiveAdminAppsForTest(int userId) {
        java.util.Set<java.lang.String> set;
        synchronized (this.mActiveAdminApps) {
            set = this.mActiveAdminApps.get(userId);
        }
        return set;
    }

    java.util.Set<java.lang.String> getAdminProtectedPackagesForTest(int userId) {
        java.util.Set<java.lang.String> set;
        synchronized (this.mAdminProtectedPackages) {
            set = this.mAdminProtectedPackages.get(userId);
        }
        return set;
    }

    private boolean isDeviceProvisioningPackage(java.lang.String packageName) {
        if (this.mCachedDeviceProvisioningPackage == null) {
            this.mCachedDeviceProvisioningPackage = this.mContext.getResources().getString(android.R.string.config_dozeDoubleTapSensorType);
        }
        return this.mCachedDeviceProvisioningPackage.equals(packageName);
    }

    private boolean isCarrierApp(java.lang.String packageName) {
        synchronized (this.mCarrierPrivilegedLock) {
            if (!this.mHaveCarrierPrivilegedApps) {
                fetchCarrierPrivilegedAppsCPL();
            }
            if (this.mCarrierPrivilegedApps == null) {
                return false;
            }
            return this.mCarrierPrivilegedApps.contains(packageName);
        }
    }

    public void clearCarrierPrivilegedApps() {
        synchronized (this.mCarrierPrivilegedLock) {
            this.mHaveCarrierPrivilegedApps = false;
            this.mCarrierPrivilegedApps = null;
        }
    }

    private void fetchCarrierPrivilegedAppsCPL() {
        android.telephony.TelephonyManager telephonyManager = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
        this.mCarrierPrivilegedApps = telephonyManager.getCarrierPrivilegedPackagesForAllActiveSubscriptions();
        this.mHaveCarrierPrivilegedApps = true;
    }

    private boolean isActiveNetworkScorer(java.lang.String packageName) {
        long now = android.os.SystemClock.elapsedRealtime();
        if (this.mCachedNetworkScorer == null || this.mCachedNetworkScorerAtMillis < now - NETWORK_SCORER_CACHE_DURATION_MILLIS) {
            this.mCachedNetworkScorer = this.mInjector.getActiveNetworkScorer();
            this.mCachedNetworkScorerAtMillis = now;
        }
        return packageName.equals(this.mCachedNetworkScorer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void informListeners(java.lang.String packageName, int userId, int bucket, int reason, boolean userInteraction) {
        boolean idle = bucket >= 40;
        synchronized (this.mPackageAccessListeners) {
            for (com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener listener : this.mPackageAccessListeners) {
                listener.onAppIdleStateChanged(packageName, userId, idle, bucket, reason);
                if (userInteraction) {
                    listener.onUserInteractionStarted(packageName, userId);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void informParoleStateChanged() {
        boolean paroled = isInParole();
        synchronized (this.mPackageAccessListeners) {
            for (com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener listener : this.mPackageAccessListeners) {
                listener.onParoleStateChanged(paroled);
            }
        }
    }

    public long getBroadcastResponseWindowDurationMs() {
        return this.mBroadcastResponseWindowDurationMillis;
    }

    public int getBroadcastResponseFgThresholdState() {
        return this.mBroadcastResponseFgThresholdState;
    }

    public long getBroadcastSessionsDurationMs() {
        return this.mBroadcastSessionsDurationMs;
    }

    public long getBroadcastSessionsWithResponseDurationMs() {
        return this.mBroadcastSessionsWithResponseDurationMs;
    }

    public boolean shouldNoteResponseEventForAllBroadcastSessions() {
        return this.mNoteResponseEventForAllBroadcastSessions;
    }

    public java.util.List<java.lang.String> getBroadcastResponseExemptedRoles() {
        return this.mBroadcastResponseExemptedRolesList;
    }

    public java.util.List<java.lang.String> getBroadcastResponseExemptedPermissions() {
        return this.mBroadcastResponseExemptedPermissionsList;
    }

    public java.lang.String getAppStandbyConstant(java.lang.String key) {
        return this.mAppStandbyProperties.get(key);
    }

    public void clearLastUsedTimestampsForTest(java.lang.String packageName, int userId) {
        synchronized (this.mAppIdleLock) {
            this.mAppIdleHistory.clearLastUsedTimestamps(packageName, userId);
        }
    }

    static /* synthetic */ void lambda$flushHandler$1() {
    }

    boolean flushHandler(long timeoutMillis) {
        return this.mHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.usage.AppStandbyController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.usage.AppStandbyController.lambda$flushHandler$1();
            }
        }, timeoutMillis);
    }

    public void flushToDisk() {
        synchronized (this.mAppIdleLock) {
            this.mAppIdleHistory.writeAppIdleTimes(this.mInjector.elapsedRealtime());
            this.mAppIdleHistory.writeAppIdleDurations();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDisplayOn() {
        return this.mInjector.isDefaultDisplayOn();
    }

    void clearAppIdleForPackage(java.lang.String packageName, int userId) {
        synchronized (this.mAppIdleLock) {
            this.mAppIdleHistory.clearUsage(packageName, userId);
        }
    }

    void maybeUnrestrictBuggyApp(java.lang.String packageName, int userId) throws java.lang.Throwable {
        maybeUnrestrictApp(packageName, userId, 1536, 4, 256, 1);
    }

    public void maybeUnrestrictApp(java.lang.String packageName, int userId, int prevMainReasonRestrict, int prevSubReasonRestrict, int mainReasonUnrestrict, int subReasonUnrestrict) throws java.lang.Throwable {
        int newBucket;
        int newReason;
        synchronized (this.mAppIdleLock) {
            try {
                try {
                    long elapsedRealtime = this.mInjector.elapsedRealtime();
                    com.android.server.usage.AppIdleHistory.AppUsageHistory app = this.mAppIdleHistory.getAppUsageHistory(packageName, userId, elapsedRealtime);
                    if (app.currentBucket == 45 && (app.bucketingReason & 65280) == prevMainReasonRestrict) {
                        if ((app.bucketingReason & 255) == prevSubReasonRestrict) {
                            newBucket = 40;
                            newReason = mainReasonUnrestrict | subReasonUnrestrict;
                        } else {
                            newBucket = 45;
                            newReason = app.bucketingReason & (~prevSubReasonRestrict);
                        }
                        this.mAppIdleHistory.setAppStandbyBucket(packageName, userId, elapsedRealtime, newBucket, newReason);
                        maybeInformListeners(packageName, userId, elapsedRealtime, newBucket, newReason, false);
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePowerWhitelistCache() {
        if (this.mInjector.getBootPhase() < 500) {
            return;
        }
        this.mInjector.updatePowerWhitelistCache();
        postCheckIdleStates(-1);
    }

    private class PackageReceiver extends android.content.BroadcastReceiver {
        private PackageReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) throws java.lang.Throwable {
            java.lang.String action = intent.getAction();
            java.lang.String pkgName = intent.getData().getSchemeSpecificPart();
            int userId = getSendingUserId();
            if ("android.intent.action.PACKAGE_ADDED".equals(action) || "android.intent.action.PACKAGE_CHANGED".equals(action)) {
                java.lang.String[] cmpList = intent.getStringArrayExtra("android.intent.extra.changed_component_name_list");
                if (cmpList == null || (cmpList.length == 1 && pkgName.equals(cmpList[0]))) {
                    com.android.server.usage.AppStandbyController.this.clearCarrierPrivilegedApps();
                    com.android.server.usage.AppStandbyController.this.evaluateSystemAppException(pkgName, userId);
                }
                if ("android.intent.action.PACKAGE_CHANGED".equals(action)) {
                    com.android.server.usage.AppStandbyController.this.mHandler.obtainMessage(11, userId, -1, pkgName).sendToTarget();
                }
            }
            if ("android.intent.action.PACKAGE_REMOVED".equals(action) || "android.intent.action.PACKAGE_ADDED".equals(action)) {
                if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                    com.android.server.usage.AppStandbyController.this.maybeUnrestrictBuggyApp(pkgName, userId);
                } else if (!"android.intent.action.PACKAGE_ADDED".equals(action)) {
                    com.android.server.usage.AppStandbyController.this.clearAppIdleForPackage(pkgName, userId);
                } else if (com.android.server.usage.AppStandbyController.this.mAppsToRestoreToRare.contains(userId, pkgName)) {
                    com.android.server.usage.AppStandbyController.this.restoreAppToRare(pkgName, userId, com.android.server.usage.AppStandbyController.this.mInjector.elapsedRealtime(), 258);
                    com.android.server.usage.AppStandbyController.this.mAppsToRestoreToRare.remove(userId, pkgName);
                }
            }
            synchronized (com.android.server.usage.AppStandbyController.this.mSystemExemptionAppOpMode) {
                if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                    int uid = intent.getIntExtra("android.intent.extra.UID", -1);
                    com.android.server.usage.AppStandbyController.this.mSystemExemptionAppOpMode.delete(uid);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateSystemAppException(java.lang.String packageName, int userId) {
        if (!this.mSystemServicesReady) {
            return;
        }
        try {
            android.content.pm.PackageInfo pi = this.mPackageManager.getPackageInfoAsUser(packageName, HEADLESS_APP_CHECK_FLAGS, userId);
            maybeUpdateHeadlessSystemAppCache(pi);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            synchronized (this.mHeadlessSystemApps) {
                this.mHeadlessSystemApps.remove(packageName);
            }
        }
    }

    private boolean maybeUpdateHeadlessSystemAppCache(android.content.pm.PackageInfo pkgInfo) {
        if (pkgInfo == null || pkgInfo.applicationInfo == null || (!pkgInfo.applicationInfo.isSystemApp() && !pkgInfo.applicationInfo.isUpdatedSystemApp())) {
            return false;
        }
        android.content.Intent frontDoorActivityIntent = new android.content.Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER").setPackage(pkgInfo.packageName);
        java.util.List<android.content.pm.ResolveInfo> res = this.mPackageManager.queryIntentActivitiesAsUser(frontDoorActivityIntent, HEADLESS_APP_CHECK_FLAGS, 0);
        return updateHeadlessSystemAppCache(pkgInfo.packageName, com.android.internal.util.ArrayUtils.isEmpty(res));
    }

    private boolean updateHeadlessSystemAppCache(java.lang.String packageName, boolean add) {
        synchronized (this.mHeadlessSystemApps) {
            if (add) {
                return this.mHeadlessSystemApps.add(packageName);
            }
            return this.mHeadlessSystemApps.remove(packageName);
        }
    }

    public void initializeDefaultsForSystemApps(int userId) throws java.lang.Throwable {
        java.lang.Object obj;
        if (!this.mSystemServicesReady) {
            this.mPendingInitializeDefaults = true;
            return;
        }
        android.util.Slog.d(TAG, "Initializing defaults for system apps on user " + userId + ", appIdleEnabled=" + this.mAppIdleEnabled);
        long elapsedRealtime = this.mInjector.elapsedRealtime();
        java.util.List<android.content.pm.PackageInfo> packages = this.mPackageManager.getInstalledPackagesAsUser(512, userId);
        int packageCount = packages.size();
        java.lang.Object obj2 = this.mAppIdleLock;
        synchronized (obj2) {
            int i = 0;
            while (i < packageCount) {
                try {
                    android.content.pm.PackageInfo pi = packages.get(i);
                    java.lang.String packageName = pi.packageName;
                    if (pi.applicationInfo != null && pi.applicationInfo.isSystemApp()) {
                        obj = obj2;
                        try {
                            this.mAppIdleHistory.reportUsage(packageName, userId, 10, 6, 0L, elapsedRealtime + this.mSystemUpdateUsageTimeoutMillis);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } else {
                        obj = obj2;
                    }
                    i++;
                    obj2 = obj;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    obj = obj2;
                }
            }
            java.lang.Object obj3 = obj2;
            this.mAppIdleHistory.writeAppIdleTimes(userId, elapsedRealtime);
        }
    }

    private java.util.Set<java.lang.String> getSystemPackagesWithLauncherActivities() {
        android.content.Intent intent = new android.content.Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER");
        java.util.List<android.content.pm.ResolveInfo> activities = this.mPackageManager.queryIntentActivitiesAsUser(intent, HEADLESS_APP_CHECK_FLAGS, 0);
        android.util.ArraySet<java.lang.String> ret = new android.util.ArraySet<>();
        for (android.content.pm.ResolveInfo ri : activities) {
            ret.add(ri.activityInfo.packageName);
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadHeadlessSystemAppCache() {
        long start = android.os.SystemClock.uptimeMillis();
        java.util.List<android.content.pm.PackageInfo> packages = this.mPackageManager.getInstalledPackagesAsUser(HEADLESS_APP_CHECK_FLAGS, 0);
        java.util.Set<java.lang.String> systemLauncherActivities = getSystemPackagesWithLauncherActivities();
        int packageCount = packages.size();
        for (int i = 0; i < packageCount; i++) {
            android.content.pm.PackageInfo pkgInfo = packages.get(i);
            if (pkgInfo != null) {
                java.lang.String pkg = pkgInfo.packageName;
                boolean isHeadLess = !systemLauncherActivities.contains(pkg);
                if (updateHeadlessSystemAppCache(pkg, isHeadLess) && !com.android.server.usage.Flags.avoidIdleCheck()) {
                    this.mHandler.obtainMessage(11, 0, -1, pkg).sendToTarget();
                }
            }
        }
        long end = android.os.SystemClock.uptimeMillis();
        android.util.Slog.d(TAG, "Loaded headless system app cache in " + (end - start) + " ms: appIdleEnabled=" + this.mAppIdleEnabled);
    }

    public void postReportContentProviderUsage(java.lang.String name, java.lang.String packageName, int userId) {
        com.android.server.usage.AppStandbyController.ContentProviderUsageRecord record = com.android.server.usage.AppStandbyController.ContentProviderUsageRecord.obtain(name, packageName, userId);
        this.mHandler.obtainMessage(8, record).sendToTarget();
    }

    public void postReportSyncScheduled(java.lang.String str, int i, boolean z) {
        this.mHandler.obtainMessage(12, i, z ? 1 : 0, str).sendToTarget();
    }

    public void postReportExemptedSyncStart(java.lang.String packageName, int userId) {
        this.mHandler.obtainMessage(13, userId, 0, packageName).sendToTarget();
    }

    com.android.server.usage.AppIdleHistory getAppIdleHistoryForTest() {
        com.android.server.usage.AppIdleHistory appIdleHistory;
        synchronized (this.mAppIdleLock) {
            appIdleHistory = this.mAppIdleHistory;
        }
        return appIdleHistory;
    }

    public void dumpUsers(android.util.IndentingPrintWriter idpw, int[] userIds, java.util.List<java.lang.String> pkgs) {
        synchronized (this.mAppIdleLock) {
            this.mAppIdleHistory.dumpUsers(idpw, userIds, pkgs);
        }
    }

    public void dumpState(java.lang.String[] args, java.io.PrintWriter pw) {
        pw.println("Flags: ");
        pw.println("    com.android.server.usage.avoid_idle_check: " + com.android.server.usage.Flags.avoidIdleCheck());
        pw.println();
        synchronized (this.mCarrierPrivilegedLock) {
            pw.println("Carrier privileged apps (have=" + this.mHaveCarrierPrivilegedApps + "): " + this.mCarrierPrivilegedApps);
        }
        pw.println();
        pw.println("Settings:");
        pw.print("  mCheckIdleIntervalMillis=");
        android.util.TimeUtils.formatDuration(this.mCheckIdleIntervalMillis, pw);
        pw.println();
        pw.print("  mStrongUsageTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mStrongUsageTimeoutMillis, pw);
        pw.println();
        pw.print("  mNotificationSeenTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mNotificationSeenTimeoutMillis, pw);
        pw.println();
        pw.print("  mNotificationSeenPromotedBucket=");
        pw.print(android.app.usage.UsageStatsManager.standbyBucketToString(this.mNotificationSeenPromotedBucket));
        pw.println();
        pw.print("  mTriggerQuotaBumpOnNotificationSeen=");
        pw.print(this.mTriggerQuotaBumpOnNotificationSeen);
        pw.println();
        pw.print("  mRetainNotificationSeenImpactForPreTApps=");
        pw.print(this.mRetainNotificationSeenImpactForPreTApps);
        pw.println();
        pw.print("  mSlicePinnedTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mSlicePinnedTimeoutMillis, pw);
        pw.println();
        pw.print("  mSyncAdapterTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mSyncAdapterTimeoutMillis, pw);
        pw.println();
        pw.print("  mSystemInteractionTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mSystemInteractionTimeoutMillis, pw);
        pw.println();
        pw.print("  mInitialForegroundServiceStartTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mInitialForegroundServiceStartTimeoutMillis, pw);
        pw.println();
        pw.print("  mPredictionTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mPredictionTimeoutMillis, pw);
        pw.println();
        pw.print("  mExemptedSyncScheduledNonDozeTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mExemptedSyncScheduledNonDozeTimeoutMillis, pw);
        pw.println();
        pw.print("  mExemptedSyncScheduledDozeTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mExemptedSyncScheduledDozeTimeoutMillis, pw);
        pw.println();
        pw.print("  mExemptedSyncStartTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mExemptedSyncStartTimeoutMillis, pw);
        pw.println();
        pw.print("  mUnexemptedSyncScheduledTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mUnexemptedSyncScheduledTimeoutMillis, pw);
        pw.println();
        pw.print("  mSystemUpdateUsageTimeoutMillis=");
        android.util.TimeUtils.formatDuration(this.mSystemUpdateUsageTimeoutMillis, pw);
        pw.println();
        pw.print("  mBroadcastResponseWindowDurationMillis=");
        android.util.TimeUtils.formatDuration(this.mBroadcastResponseWindowDurationMillis, pw);
        pw.println();
        pw.print("  mBroadcastResponseFgThresholdState=");
        pw.print(android.app.ActivityManager.procStateToString(this.mBroadcastResponseFgThresholdState));
        pw.println();
        pw.print("  mBroadcastSessionsDurationMs=");
        android.util.TimeUtils.formatDuration(this.mBroadcastSessionsDurationMs, pw);
        pw.println();
        pw.print("  mBroadcastSessionsWithResponseDurationMs=");
        android.util.TimeUtils.formatDuration(this.mBroadcastSessionsWithResponseDurationMs, pw);
        pw.println();
        pw.print("  mNoteResponseEventForAllBroadcastSessions=");
        pw.print(this.mNoteResponseEventForAllBroadcastSessions);
        pw.println();
        pw.print("  mBroadcastResponseExemptedRoles=");
        pw.print(this.mBroadcastResponseExemptedRoles);
        pw.println();
        pw.print("  mBroadcastResponseExemptedPermissions=");
        pw.print(this.mBroadcastResponseExemptedPermissions);
        pw.println();
        pw.println();
        pw.print("mAppIdleEnabled=");
        pw.print(this.mAppIdleEnabled);
        pw.print(" mIsCharging=");
        pw.print(this.mIsCharging);
        pw.println();
        pw.print("mScreenThresholds=");
        pw.println(java.util.Arrays.toString(this.mAppStandbyScreenThresholds));
        pw.print("mElapsedThresholds=");
        pw.println(java.util.Arrays.toString(this.mAppStandbyElapsedThresholds));
        pw.println();
        pw.println("mHeadlessSystemApps=[");
        synchronized (this.mHeadlessSystemApps) {
            for (int i = this.mHeadlessSystemApps.size() - 1; i >= 0; i--) {
                pw.print("  ");
                pw.print(this.mHeadlessSystemApps.valueAt(i));
                if (i != 0) {
                    pw.println(",");
                }
            }
        }
        pw.println("]");
        pw.println();
        pw.println("mSystemPackagesAppIds=[");
        synchronized (this.mSystemPackagesAppIds) {
            for (int i2 = this.mSystemPackagesAppIds.size() - 1; i2 >= 0; i2--) {
                pw.print("  ");
                pw.print(this.mSystemPackagesAppIds.get(i2));
                if (i2 != 0) {
                    pw.println(",");
                }
            }
        }
        pw.println("]");
        pw.println();
        pw.println("mActiveAdminApps=[");
        synchronized (this.mActiveAdminApps) {
            int size = this.mActiveAdminApps.size();
            for (int i3 = 0; i3 < size; i3++) {
                int userId = this.mActiveAdminApps.keyAt(i3);
                pw.print(" ");
                pw.print(userId);
                pw.print(": ");
                pw.print(this.mActiveAdminApps.valueAt(i3));
                if (i3 != size - 1) {
                    pw.print(",");
                }
                pw.println();
            }
        }
        pw.println("]");
        pw.println();
        pw.println("mAdminProtectedPackages=[");
        synchronized (this.mAdminProtectedPackages) {
            int size2 = this.mAdminProtectedPackages.size();
            for (int i4 = 0; i4 < size2; i4++) {
                int userId2 = this.mAdminProtectedPackages.keyAt(i4);
                pw.print(" ");
                pw.print(userId2);
                pw.print(": ");
                pw.print(this.mAdminProtectedPackages.valueAt(i4));
                if (i4 != size2 - 1) {
                    pw.print(",");
                }
                pw.println();
            }
        }
        pw.println("]");
        pw.println();
        this.mInjector.dump(pw);
    }

    static class Injector {
        private com.android.server.AlarmManagerInternal mAlarmManagerInternal;
        private android.os.BatteryManager mBatteryManager;
        private com.android.internal.app.IBatteryStats mBatteryStats;
        int mBootPhase;
        private final android.content.Context mContext;
        private android.content.pm.CrossProfileAppsInternal mCrossProfileAppsInternal;
        private android.os.IDeviceIdleController mDeviceIdleController;
        private android.hardware.display.DisplayManager mDisplayManager;
        private final android.os.Looper mLooper;
        private android.content.pm.PackageManagerInternal mPackageManagerInternal;
        private android.os.PowerManager mPowerManager;
        long mAutoRestrictedBucketDelayMs = 3600000;
        private final android.util.ArraySet<java.lang.String> mPowerWhitelistedApps = new android.util.ArraySet<>();
        private java.lang.String mWellbeingApp = null;

        Injector(android.content.Context context, android.os.Looper looper) {
            this.mContext = context;
            this.mLooper = looper;
        }

        android.content.Context getContext() {
            return this.mContext;
        }

        android.os.Looper getLooper() {
            return this.mLooper;
        }

        void onBootPhase(int phase) {
            if (phase == 500) {
                this.mDeviceIdleController = android.os.IDeviceIdleController.Stub.asInterface(android.os.ServiceManager.getService("deviceidle"));
                this.mBatteryStats = com.android.internal.app.IBatteryStats.Stub.asInterface(android.os.ServiceManager.getService("batterystats"));
                this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
                this.mDisplayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService("display");
                this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
                this.mBatteryManager = (android.os.BatteryManager) this.mContext.getSystemService(android.os.BatteryManager.class);
                this.mCrossProfileAppsInternal = (android.content.pm.CrossProfileAppsInternal) com.android.server.LocalServices.getService(android.content.pm.CrossProfileAppsInternal.class);
                this.mAlarmManagerInternal = (com.android.server.AlarmManagerInternal) com.android.server.LocalServices.getService(com.android.server.AlarmManagerInternal.class);
                android.app.ActivityManager activityManager = (android.app.ActivityManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
                if (activityManager.isLowRamDevice() || android.app.ActivityManager.isSmallBatteryDevice()) {
                    this.mAutoRestrictedBucketDelayMs = 43200000L;
                }
            } else if (phase == 1000) {
                android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
                this.mWellbeingApp = packageManager.getWellbeingPackageName();
            }
            this.mBootPhase = phase;
        }

        int getBootPhase() {
            return this.mBootPhase;
        }

        long elapsedRealtime() {
            return android.os.SystemClock.elapsedRealtime();
        }

        long currentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }

        boolean isAppIdleEnabled() {
            boolean buildFlag = this.mContext.getResources().getBoolean(android.R.bool.config_enableActivityRecognitionHardwareOverlay);
            boolean runtimeFlag = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "app_standby_enabled", 1) == 1 && android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "adaptive_battery_management_enabled", 1) == 1;
            return buildFlag && runtimeFlag;
        }

        boolean isCharging() {
            return this.mBatteryManager.isCharging();
        }

        boolean isNonIdleWhitelisted(java.lang.String packageName) {
            boolean zContains;
            if (this.mBootPhase < 500) {
                return false;
            }
            synchronized (this.mPowerWhitelistedApps) {
                zContains = this.mPowerWhitelistedApps.contains(packageName);
            }
            return zContains;
        }

        com.android.internal.app.IAppOpsService getAppOpsService() {
            return com.android.internal.app.IAppOpsService.Stub.asInterface(android.os.ServiceManager.getService("appops"));
        }

        boolean isWellbeingPackage(java.lang.String packageName) {
            return packageName.equals(this.mWellbeingApp);
        }

        boolean shouldGetExactAlarmBucketElevation(java.lang.String packageName, int uid) {
            return this.mAlarmManagerInternal.shouldGetBucketElevation(packageName, uid);
        }

        void updatePowerWhitelistCache() {
            try {
                java.lang.String[] whitelistedPkgs = this.mDeviceIdleController.getFullPowerWhitelistExceptIdle();
                synchronized (this.mPowerWhitelistedApps) {
                    this.mPowerWhitelistedApps.clear();
                    for (java.lang.String str : whitelistedPkgs) {
                        this.mPowerWhitelistedApps.add(str);
                    }
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(com.android.server.usage.AppStandbyController.TAG, "Failed to get power whitelist", e);
            }
        }

        java.io.File getDataSystemDirectory() {
            return android.os.Environment.getDataSystemDirectory();
        }

        long getAutoRestrictedBucketDelayMs() {
            return this.mAutoRestrictedBucketDelayMs;
        }

        void noteEvent(int event, java.lang.String packageName, int uid) throws android.os.RemoteException {
            if (this.mBatteryStats != null) {
                this.mBatteryStats.noteEvent(event, packageName, uid);
            }
        }

        android.content.pm.PackageManagerInternal getPackageManagerInternal() {
            return this.mPackageManagerInternal;
        }

        boolean isPackageEphemeral(int userId, java.lang.String packageName) {
            return this.mPackageManagerInternal.isPackageEphemeral(userId, packageName);
        }

        boolean isPackageInstalled(java.lang.String packageName, int flags, int userId) {
            return this.mPackageManagerInternal.getPackageUid(packageName, (long) flags, userId) >= 0;
        }

        int[] getRunningUserIds() throws android.os.RemoteException {
            return android.app.ActivityManager.getService().getRunningUserIds();
        }

        boolean isDefaultDisplayOn() {
            return this.mDisplayManager.getDisplay(0).getState() == 2;
        }

        void registerDisplayListener(android.hardware.display.DisplayManager.DisplayListener listener, android.os.Handler handler) {
            this.mDisplayManager.registerDisplayListener(listener, handler);
        }

        java.lang.String getActiveNetworkScorer() {
            android.net.NetworkScoreManager nsm = (android.net.NetworkScoreManager) this.mContext.getSystemService("network_score");
            return nsm.getActiveScorerPackage();
        }

        public boolean isBoundWidgetPackage(android.appwidget.AppWidgetManager appWidgetManager, java.lang.String packageName, int userId) {
            return appWidgetManager.isBoundWidgetPackage(packageName, userId);
        }

        android.provider.DeviceConfig.Properties getDeviceConfigProperties(java.lang.String... keys) {
            return android.provider.DeviceConfig.getProperties("app_standby", keys);
        }

        public boolean isDeviceIdleMode() {
            return this.mPowerManager.isDeviceIdleMode();
        }

        public java.util.List<android.os.UserHandle> getValidCrossProfileTargets(java.lang.String pkg, int userId) {
            int uid = this.mPackageManagerInternal.getPackageUid(pkg, 0L, userId);
            com.android.server.pm.pkg.AndroidPackage aPkg = this.mPackageManagerInternal.getPackage(uid);
            if (uid < 0 || aPkg == null || !aPkg.isCrossProfile() || !this.mCrossProfileAppsInternal.verifyUidHasInteractAcrossProfilePermission(pkg, uid)) {
                if (uid >= 0 && aPkg == null) {
                    android.util.Slog.wtf(com.android.server.usage.AppStandbyController.TAG, "Null package retrieved for UID " + uid);
                }
                return java.util.Collections.emptyList();
            }
            return this.mCrossProfileAppsInternal.getTargetUserProfiles(pkg, userId);
        }

        void registerDeviceConfigPropertiesChangedListener(android.provider.DeviceConfig.OnPropertiesChangedListener listener) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("app_standby", com.android.server.AppSchedulingModuleThread.getExecutor(), listener);
        }

        void dump(java.io.PrintWriter pw) {
            pw.println("mPowerWhitelistedApps=[");
            synchronized (this.mPowerWhitelistedApps) {
                for (int i = this.mPowerWhitelistedApps.size() - 1; i >= 0; i--) {
                    pw.print("  ");
                    pw.print(this.mPowerWhitelistedApps.valueAt(i));
                    pw.println(",");
                }
            }
            pw.println("]");
            pw.println();
        }
    }

    class AppStandbyHandler extends android.os.Handler {
        AppStandbyHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            switch (msg.what) {
                case 3:
                    com.android.server.usage.AppStandbyController.StandbyUpdateRecord r = (com.android.server.usage.AppStandbyController.StandbyUpdateRecord) msg.obj;
                    com.android.server.usage.AppStandbyController.this.informListeners(r.packageName, r.userId, r.bucket, r.reason, r.isUserInteraction);
                    r.recycle();
                    return;
                case 4:
                    com.android.server.usage.AppStandbyController.this.forceIdleState((java.lang.String) msg.obj, msg.arg1, msg.arg2 == 1);
                    return;
                case 5:
                    removeMessages(5);
                    long earliestCheck = Long.MAX_VALUE;
                    long nowElapsed = com.android.server.usage.AppStandbyController.this.mInjector.elapsedRealtime();
                    synchronized (com.android.server.usage.AppStandbyController.this.mPendingIdleStateChecks) {
                        for (int i = com.android.server.usage.AppStandbyController.this.mPendingIdleStateChecks.size() - 1; i >= 0; i--) {
                            long expirationTime = com.android.server.usage.AppStandbyController.this.mPendingIdleStateChecks.valueAt(i);
                            if (expirationTime <= nowElapsed) {
                                int userId = com.android.server.usage.AppStandbyController.this.mPendingIdleStateChecks.keyAt(i);
                                if (com.android.server.usage.AppStandbyController.this.checkIdleStates(userId) && com.android.server.usage.AppStandbyController.this.mAppIdleEnabled) {
                                    expirationTime = nowElapsed + com.android.server.usage.AppStandbyController.this.mCheckIdleIntervalMillis;
                                    com.android.server.usage.AppStandbyController.this.mPendingIdleStateChecks.put(userId, expirationTime);
                                    earliestCheck = java.lang.Math.min(earliestCheck, expirationTime);
                                } else {
                                    com.android.server.usage.AppStandbyController.this.mPendingIdleStateChecks.removeAt(i);
                                }
                            } else {
                                earliestCheck = java.lang.Math.min(earliestCheck, expirationTime);
                            }
                        }
                        break;
                    }
                    if (earliestCheck != Long.MAX_VALUE) {
                        com.android.server.usage.AppStandbyController.this.mHandler.sendMessageDelayed(com.android.server.usage.AppStandbyController.this.mHandler.obtainMessage(5), earliestCheck - nowElapsed);
                        return;
                    }
                    return;
                case 6:
                default:
                    super.handleMessage(msg);
                    return;
                case 7:
                    com.android.server.usage.AppStandbyController.this.triggerListenerQuotaBump((java.lang.String) msg.obj, msg.arg1);
                    return;
                case 8:
                    com.android.server.usage.AppStandbyController.ContentProviderUsageRecord record = (com.android.server.usage.AppStandbyController.ContentProviderUsageRecord) msg.obj;
                    com.android.server.usage.AppStandbyController.this.reportContentProviderUsage(record.name, record.packageName, record.userId);
                    record.recycle();
                    return;
                case 9:
                    com.android.server.usage.AppStandbyController.this.informParoleStateChanged();
                    return;
                case 10:
                    com.android.server.usage.AppStandbyController.this.mHandler.removeMessages(10);
                    com.android.server.usage.AppStandbyController.this.waitForAdminData();
                    com.android.server.usage.AppStandbyController.this.checkIdleStates(-1);
                    return;
                case 11:
                    com.android.server.usage.AppStandbyController.this.checkAndUpdateStandbyState((java.lang.String) msg.obj, msg.arg1, msg.arg2, com.android.server.usage.AppStandbyController.this.mInjector.elapsedRealtime());
                    return;
                case 12:
                    boolean exempted = msg.arg2 > 0;
                    if (exempted) {
                        com.android.server.usage.AppStandbyController.this.reportExemptedSyncScheduled((java.lang.String) msg.obj, msg.arg1);
                        return;
                    } else {
                        com.android.server.usage.AppStandbyController.this.reportUnexemptedSyncScheduled((java.lang.String) msg.obj, msg.arg1);
                        return;
                    }
                case 13:
                    com.android.server.usage.AppStandbyController.this.reportExemptedSyncStart((java.lang.String) msg.obj, msg.arg1);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DeviceStateReceiver extends android.content.BroadcastReceiver {
        private DeviceStateReceiver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:14:0x002c  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r5, android.content.Intent r6) {
            /*
                r4 = this;
                java.lang.String r0 = r6.getAction()
                int r1 = r0.hashCode()
                r2 = 0
                r3 = 1
                switch(r1) {
                    case -65633567: goto L22;
                    case -54942926: goto L18;
                    case 948344062: goto Le;
                    default: goto Ld;
                }
            Ld:
                goto L2c
            Le:
                java.lang.String r1 = "android.os.action.CHARGING"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = r2
                goto L2d
            L18:
                java.lang.String r1 = "android.os.action.DISCHARGING"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = r3
                goto L2d
            L22:
                java.lang.String r1 = "android.os.action.POWER_SAVE_WHITELIST_CHANGED"
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto Ld
                r0 = 2
                goto L2d
            L2c:
                r0 = -1
            L2d:
                switch(r0) {
                    case 0: goto L50;
                    case 1: goto L4a;
                    case 2: goto L31;
                    default: goto L30;
                }
            L30:
                goto L56
            L31:
                com.android.server.usage.AppStandbyController r0 = com.android.server.usage.AppStandbyController.this
                boolean r0 = com.android.server.usage.AppStandbyController.m9783$$Nest$fgetmSystemServicesReady(r0)
                if (r0 == 0) goto L56
                com.android.server.usage.AppStandbyController r0 = com.android.server.usage.AppStandbyController.this
                com.android.server.usage.AppStandbyController$AppStandbyHandler r0 = com.android.server.usage.AppStandbyController.m9780$$Nest$fgetmHandler(r0)
                com.android.server.usage.AppStandbyController r1 = com.android.server.usage.AppStandbyController.this
                com.android.server.usage.AppStandbyController$DeviceStateReceiver$$ExternalSyntheticLambda0 r2 = new com.android.server.usage.AppStandbyController$DeviceStateReceiver$$ExternalSyntheticLambda0
                r2.<init>()
                r0.post(r2)
                goto L56
            L4a:
                com.android.server.usage.AppStandbyController r0 = com.android.server.usage.AppStandbyController.this
                r0.setChargingState(r2)
                goto L56
            L50:
                com.android.server.usage.AppStandbyController r0 = com.android.server.usage.AppStandbyController.this
                r0.setChargingState(r3)
            L56:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.usage.AppStandbyController.DeviceStateReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    private class ConstantsObserver extends android.database.ContentObserver implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        public static final long DEFAULT_AUTO_RESTRICTED_BUCKET_DELAY_MS = 3600000;
        private static final java.lang.String DEFAULT_BROADCAST_RESPONSE_EXEMPTED_PERMISSIONS = "";
        private static final java.lang.String DEFAULT_BROADCAST_RESPONSE_EXEMPTED_ROLES = "";
        public static final int DEFAULT_BROADCAST_RESPONSE_FG_THRESHOLD_STATE = 2;
        public static final long DEFAULT_BROADCAST_RESPONSE_WINDOW_DURATION_MS = 120000;
        public static final long DEFAULT_BROADCAST_SESSIONS_DURATION_MS = 120000;
        public static final long DEFAULT_BROADCAST_SESSIONS_WITH_RESPONSE_DURATION_MS = 120000;
        public static final long DEFAULT_CHECK_IDLE_INTERVAL_MS = 14400000;
        public static final boolean DEFAULT_CROSS_PROFILE_APPS_SHARE_STANDBY_BUCKETS = true;
        public static final long DEFAULT_EXEMPTED_SYNC_SCHEDULED_DOZE_TIMEOUT = 14400000;
        public static final long DEFAULT_EXEMPTED_SYNC_SCHEDULED_NON_DOZE_TIMEOUT = 600000;
        public static final long DEFAULT_EXEMPTED_SYNC_START_TIMEOUT = 600000;
        public static final long DEFAULT_INITIAL_FOREGROUND_SERVICE_START_TIMEOUT = 1800000;
        public static final boolean DEFAULT_NOTE_RESPONSE_EVENT_FOR_ALL_BROADCAST_SESSIONS = true;
        public static final int DEFAULT_NOTIFICATION_SEEN_PROMOTED_BUCKET = 20;
        public static final long DEFAULT_NOTIFICATION_TIMEOUT = 43200000;
        public static final boolean DEFAULT_RETAIN_NOTIFICATION_SEEN_IMPACT_FOR_PRE_T_APPS = false;
        public static final long DEFAULT_SLICE_PINNED_TIMEOUT = 43200000;
        public static final long DEFAULT_STRONG_USAGE_TIMEOUT = 3600000;
        public static final long DEFAULT_SYNC_ADAPTER_TIMEOUT = 600000;
        public static final long DEFAULT_SYSTEM_INTERACTION_TIMEOUT = 600000;
        public static final long DEFAULT_SYSTEM_UPDATE_TIMEOUT = 7200000;
        public static final boolean DEFAULT_TRIGGER_QUOTA_BUMP_ON_NOTIFICATION_SEEN = false;
        public static final long DEFAULT_UNEXEMPTED_SYNC_SCHEDULED_TIMEOUT = 600000;
        private static final java.lang.String KEY_AUTO_RESTRICTED_BUCKET_DELAY_MS = "auto_restricted_bucket_delay_ms";
        private static final java.lang.String KEY_BROADCAST_RESPONSE_EXEMPTED_PERMISSIONS = "brodacast_response_exempted_permissions";
        private static final java.lang.String KEY_BROADCAST_RESPONSE_EXEMPTED_ROLES = "brodacast_response_exempted_roles";
        private static final java.lang.String KEY_BROADCAST_RESPONSE_FG_THRESHOLD_STATE = "broadcast_response_fg_threshold_state";
        private static final java.lang.String KEY_BROADCAST_RESPONSE_WINDOW_DURATION_MS = "broadcast_response_window_timeout_ms";
        private static final java.lang.String KEY_BROADCAST_SESSIONS_DURATION_MS = "broadcast_sessions_duration_ms";
        private static final java.lang.String KEY_BROADCAST_SESSIONS_WITH_RESPONSE_DURATION_MS = "broadcast_sessions_with_response_duration_ms";
        private static final java.lang.String KEY_CROSS_PROFILE_APPS_SHARE_STANDBY_BUCKETS = "cross_profile_apps_share_standby_buckets";
        private static final java.lang.String KEY_EXEMPTED_SYNC_SCHEDULED_DOZE_HOLD_DURATION = "exempted_sync_scheduled_d_duration";
        private static final java.lang.String KEY_EXEMPTED_SYNC_SCHEDULED_NON_DOZE_HOLD_DURATION = "exempted_sync_scheduled_nd_duration";
        private static final java.lang.String KEY_EXEMPTED_SYNC_START_HOLD_DURATION = "exempted_sync_start_duration";
        private static final java.lang.String KEY_INITIAL_FOREGROUND_SERVICE_START_HOLD_DURATION = "initial_foreground_service_start_duration";
        private static final java.lang.String KEY_NOTE_RESPONSE_EVENT_FOR_ALL_BROADCAST_SESSIONS = "note_response_event_for_all_broadcast_sessions";
        private static final java.lang.String KEY_NOTIFICATION_SEEN_HOLD_DURATION = "notification_seen_duration";
        private static final java.lang.String KEY_NOTIFICATION_SEEN_PROMOTED_BUCKET = "notification_seen_promoted_bucket";
        private static final java.lang.String KEY_PREDICTION_TIMEOUT = "prediction_timeout";
        private static final java.lang.String KEY_PREFIX_ELAPSED_TIME_THRESHOLD = "elapsed_threshold_";
        private static final java.lang.String KEY_PREFIX_SCREEN_TIME_THRESHOLD = "screen_threshold_";
        private static final java.lang.String KEY_RETAIN_NOTIFICATION_SEEN_IMPACT_FOR_PRE_T_APPS = "retain_notification_seen_impact_for_pre_t_apps";
        private static final java.lang.String KEY_SLICE_PINNED_HOLD_DURATION = "slice_pinned_duration";
        private static final java.lang.String KEY_STRONG_USAGE_HOLD_DURATION = "strong_usage_duration";
        private static final java.lang.String KEY_SYNC_ADAPTER_HOLD_DURATION = "sync_adapter_duration";
        private static final java.lang.String KEY_SYSTEM_INTERACTION_HOLD_DURATION = "system_interaction_duration";
        private static final java.lang.String KEY_SYSTEM_UPDATE_HOLD_DURATION = "system_update_usage_duration";
        private static final java.lang.String KEY_TRIGGER_QUOTA_BUMP_ON_NOTIFICATION_SEEN = "trigger_quota_bump_on_notification_seen";
        private static final java.lang.String KEY_UNEXEMPTED_SYNC_SCHEDULED_HOLD_DURATION = "unexempted_sync_scheduled_duration";
        private final java.lang.String[] KEYS_ELAPSED_TIME_THRESHOLDS;
        private final java.lang.String[] KEYS_SCREEN_TIME_THRESHOLDS;
        private final android.text.TextUtils.SimpleStringSplitter mStringPipeSplitter;

        ConstantsObserver(android.os.Handler handler) {
            super(handler);
            this.KEYS_SCREEN_TIME_THRESHOLDS = new java.lang.String[]{"screen_threshold_active", "screen_threshold_working_set", "screen_threshold_frequent", "screen_threshold_rare", "screen_threshold_restricted"};
            this.KEYS_ELAPSED_TIME_THRESHOLDS = new java.lang.String[]{"elapsed_threshold_active", "elapsed_threshold_working_set", "elapsed_threshold_frequent", "elapsed_threshold_rare", "elapsed_threshold_restricted"};
            this.mStringPipeSplitter = new android.text.TextUtils.SimpleStringSplitter('|');
        }

        public void start() {
            android.content.ContentResolver cr = com.android.server.usage.AppStandbyController.this.mContext.getContentResolver();
            cr.registerContentObserver(android.provider.Settings.Global.getUriFor("app_standby_enabled"), false, this);
            cr.registerContentObserver(android.provider.Settings.Global.getUriFor("adaptive_battery_management_enabled"), false, this);
            com.android.server.usage.AppStandbyController.this.mInjector.registerDeviceConfigPropertiesChangedListener(this);
            processProperties(com.android.server.usage.AppStandbyController.this.mInjector.getDeviceConfigProperties(new java.lang.String[0]));
            updateSettings();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            updateSettings();
            com.android.server.usage.AppStandbyController.this.postOneTimeCheckIdleStates();
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            processProperties(properties);
            com.android.server.usage.AppStandbyController.this.postOneTimeCheckIdleStates();
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:12:0x002b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void processProperties(android.provider.DeviceConfig.Properties r16) {
            /*
                Method dump skipped, instruction units count: 890
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.usage.AppStandbyController.ConstantsObserver.processProperties(android.provider.DeviceConfig$Properties):void");
        }

        private java.util.List<java.lang.String> splitPipeSeparatedString(java.lang.String string) {
            java.util.List<java.lang.String> values = new java.util.ArrayList<>();
            this.mStringPipeSplitter.setString(string);
            while (this.mStringPipeSplitter.hasNext()) {
                values.add(this.mStringPipeSplitter.next());
            }
            return values;
        }

        private void updateTimeThresholds() {
            android.provider.DeviceConfig.Properties screenThresholdProperties = com.android.server.usage.AppStandbyController.this.mInjector.getDeviceConfigProperties(this.KEYS_SCREEN_TIME_THRESHOLDS);
            android.provider.DeviceConfig.Properties elapsedThresholdProperties = com.android.server.usage.AppStandbyController.this.mInjector.getDeviceConfigProperties(this.KEYS_ELAPSED_TIME_THRESHOLDS);
            com.android.server.usage.AppStandbyController.this.mAppStandbyScreenThresholds = generateThresholdArray(screenThresholdProperties, this.KEYS_SCREEN_TIME_THRESHOLDS, com.android.server.usage.AppStandbyController.DEFAULT_SCREEN_TIME_THRESHOLDS, com.android.server.usage.AppStandbyController.MINIMUM_SCREEN_TIME_THRESHOLDS);
            com.android.server.usage.AppStandbyController.this.mAppStandbyElapsedThresholds = generateThresholdArray(elapsedThresholdProperties, this.KEYS_ELAPSED_TIME_THRESHOLDS, com.android.server.usage.AppStandbyController.DEFAULT_ELAPSED_TIME_THRESHOLDS, com.android.server.usage.AppStandbyController.MINIMUM_ELAPSED_TIME_THRESHOLDS);
            com.android.server.usage.AppStandbyController.this.mCheckIdleIntervalMillis = java.lang.Math.min(com.android.server.usage.AppStandbyController.this.mAppStandbyElapsedThresholds[1] / 4, 14400000L);
        }

        void updateSettings() {
            com.android.server.usage.AppStandbyController.this.setAppIdleEnabled(com.android.server.usage.AppStandbyController.this.mInjector.isAppIdleEnabled());
        }

        long[] generateThresholdArray(android.provider.DeviceConfig.Properties properties, java.lang.String[] keys, long[] defaults, long[] minValues) {
            if (properties.getKeyset().isEmpty()) {
                return defaults;
            }
            if (keys.length != com.android.server.usage.AppStandbyController.THRESHOLD_BUCKETS.length) {
                throw new java.lang.IllegalStateException("# keys (" + keys.length + ") != # buckets (" + com.android.server.usage.AppStandbyController.THRESHOLD_BUCKETS.length + ")");
            }
            if (defaults.length != com.android.server.usage.AppStandbyController.THRESHOLD_BUCKETS.length) {
                throw new java.lang.IllegalStateException("# defaults (" + defaults.length + ") != # buckets (" + com.android.server.usage.AppStandbyController.THRESHOLD_BUCKETS.length + ")");
            }
            if (minValues.length != com.android.server.usage.AppStandbyController.THRESHOLD_BUCKETS.length) {
                android.util.Slog.wtf(com.android.server.usage.AppStandbyController.TAG, "minValues array is the wrong size");
                minValues = new long[com.android.server.usage.AppStandbyController.THRESHOLD_BUCKETS.length];
            }
            long[] array = new long[com.android.server.usage.AppStandbyController.THRESHOLD_BUCKETS.length];
            for (int i = 0; i < com.android.server.usage.AppStandbyController.THRESHOLD_BUCKETS.length; i++) {
                array[i] = java.lang.Math.max(minValues[i], properties.getLong(keys[i], defaults[i]));
            }
            return array;
        }
    }

    public com.android.server.usage.IAppStandbyControllerWrapper getWrapper() {
        return this.mAppStandbyControllerWrapper;
    }

    private class AppStandbyControllerWrapper implements com.android.server.usage.IAppStandbyControllerWrapper {
        private AppStandbyControllerWrapper() {
        }

        @Override // com.android.server.usage.IAppStandbyControllerWrapper
        public void setAppStandbyBucket(java.lang.String packageName, int userId, int newBucket, int reason, long elapsedRealtime, boolean resetTimeout) throws java.lang.Throwable {
            com.android.server.usage.AppStandbyController.this.setAppStandbyBucket(packageName, userId, newBucket, reason, elapsedRealtime, resetTimeout);
        }
    }
}
