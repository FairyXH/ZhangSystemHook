package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
public class UsageStatsService extends com.android.server.SystemService implements com.android.server.usage.UserUsageStatsService.StatsUpdatedListener {
    private static final java.io.File COMMON_USAGE_STATS_DIR;
    static final boolean COMPRESS_TIME = false;
    static final boolean DEBUG_RESPONSE_STATS;
    private static final boolean ENABLE_KERNEL_UPDATES = true;
    private static final long FLUSH_INTERVAL = 1200000;
    private static final java.lang.String GLOBAL_COMPONENT_USAGE_FILE_NAME = "globalcomponentusage";
    private static final java.io.File KERNEL_COUNTER_FILE;
    private static final java.io.File LEGACY_COMMON_USAGE_STATS_DIR;
    private static final java.io.File LEGACY_USER_USAGE_STATS_DIR;
    private static final int MAX_TEXT_LENGTH = 127;
    static final int MSG_FLUSH_TO_DISK = 1;
    static final int MSG_HANDLE_LAUNCH_TIME_ON_USER_UNLOCK = 8;
    static final int MSG_NOTIFY_ESTIMATED_LAUNCH_TIMES_CHANGED = 9;
    static final int MSG_NOTIFY_USAGE_EVENT_LISTENER = 12;
    static final int MSG_ON_START = 7;
    static final int MSG_PACKAGE_REMOVED = 6;
    static final int MSG_REMOVE_USER = 2;
    static final int MSG_REPORT_EVENT = 0;
    static final int MSG_REPORT_EVENT_TO_ALL_USERID = 4;
    static final int MSG_UID_REMOVED = 10;
    static final int MSG_UID_STATE_CHANGED = 3;
    static final int MSG_UNLOCKED_USER = 5;
    static final int MSG_USER_STARTED = 11;
    private static final long ONE_DAY = 86400000;
    private static final long ONE_WEEK = 604800000;
    static final java.lang.String TAG = "UsageStatsService";
    private static final long TEN_SECONDS = 10000;
    static final long TIME_CHANGE_THRESHOLD_MILLIS = 2000;
    private static final char TOKEN_DELIMITER = '/';
    private static final long TWENTY_MINUTES = 1200000;
    private static final long UNKNOWN_LAUNCH_TIME_DELAY_MS = 31536000000L;
    android.app.AppOpsManager mAppOps;
    com.android.server.usage.AppStandbyInternal mAppStandby;
    com.android.server.usage.AppTimeLimitController mAppTimeLimit;
    android.app.admin.DevicePolicyManagerInternal mDpmInternal;
    private final java.util.concurrent.CopyOnWriteArraySet<android.app.usage.UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener> mEstimatedLaunchTimeChangedListeners;
    private final com.android.server.usage.IUsageStatsServiceExt mExt;
    private android.os.Handler mHandler;
    private final com.android.server.usage.UsageStatsService.Injector mInjector;
    private android.os.Handler mIoHandler;
    private final android.os.Handler.Callback mIoHandlerCallback;
    private final java.util.Map<java.lang.String, java.lang.Long> mLastTimeComponentUsedGlobal;
    private final android.util.SparseArray<com.android.server.usage.UsageStatsService.LaunchTimeAlarmQueue> mLaunchTimeAlarmQueues;
    private final java.lang.Object mLock;
    android.content.pm.PackageManager mPackageManager;
    android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final com.android.internal.content.PackageMonitor mPackageMonitor;
    private final android.util.SparseSetArray<java.lang.String> mPendingLaunchTimeChangePackages;
    private long mRealTimeSnapshot;
    private final android.util.SparseArray<java.util.LinkedList<android.app.usage.UsageEvents.Event>> mReportedEvents;
    private com.android.server.usage.BroadcastResponseStatsTracker mResponseStatsTracker;
    android.content.pm.ShortcutServiceInternal mShortcutServiceInternal;
    private com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener mStandbyChangeListener;
    private long mSystemTimeSnapshot;
    private final android.app.IUidObserver mUidObserver;
    private final android.util.SparseIntArray mUidToKernelCounter;
    private final android.util.ArraySet<android.app.usage.UsageStatsManagerInternal.UsageEventListener> mUsageEventListeners;
    final android.util.SparseArray<android.util.ArraySet<java.lang.String>> mUsageReporters;
    int mUsageSource;
    android.os.UserManager mUserManager;
    private final android.util.SparseArray<com.android.server.usage.UserUsageStatsService> mUserState;
    private final java.util.concurrent.CopyOnWriteArraySet<java.lang.Integer> mUserUnlockedStates;
    final android.util.SparseArray<com.android.server.usage.UsageStatsService.ActivityData> mVisibleActivities;
    public static final boolean ENABLE_TIME_CHANGE_CORRECTION = android.os.SystemProperties.getBoolean("persist.debug.time_correction", true);
    private static final boolean USE_DEDICATED_HANDLER_THREAD = android.os.SystemProperties.getBoolean("persist.debug.use_dedicated_handler_thread", android.app.usage.Flags.useDedicatedHandlerThread());
    static boolean DEBUG_USAGE = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    static final boolean DEBUG = DEBUG_USAGE;

    static {
        boolean z = true;
        if (!DEBUG && !android.util.Log.isLoggable(TAG, 3)) {
            z = false;
        }
        DEBUG_RESPONSE_STATS = z;
        KERNEL_COUNTER_FILE = new java.io.File("/proc/uid_procstat/set");
        COMMON_USAGE_STATS_DIR = new java.io.File(android.os.Environment.getDataSystemDirectory(), "usagestats");
        LEGACY_USER_USAGE_STATS_DIR = COMMON_USAGE_STATS_DIR;
        LEGACY_COMMON_USAGE_STATS_DIR = new java.io.File(android.os.Environment.getDataSystemDeDirectory(), "usagestats");
    }

    private static class ActivityData {
        public int lastEvent;
        private final java.lang.String mTaskRootClass;
        private final java.lang.String mTaskRootPackage;
        private final java.lang.String mUsageSourcePackage;

        private ActivityData(java.lang.String taskRootPackage, java.lang.String taskRootClass, java.lang.String sourcePackage) {
            this.lastEvent = 0;
            this.mTaskRootPackage = taskRootPackage;
            this.mTaskRootClass = taskRootClass;
            this.mUsageSourcePackage = sourcePackage;
        }
    }

    static class Injector {
        Injector() {
        }

        com.android.server.usage.AppStandbyInternal getAppStandbyController(android.content.Context context) {
            return com.android.server.usage.AppStandbyInternal.newAppStandbyController(com.android.server.usage.UsageStatsService.class.getClassLoader(), context);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(android.os.Message msg) {
        switch (msg.what) {
            case 3:
                int userId = msg.arg1;
                int procState = msg.arg2;
                int newCounter = procState <= 2 ? 0 : 1;
                synchronized (this.mUidToKernelCounter) {
                    int oldCounter = this.mUidToKernelCounter.get(userId, 0);
                    if (newCounter == oldCounter) {
                        break;
                    } else {
                        this.mUidToKernelCounter.put(userId, newCounter);
                        try {
                            android.os.FileUtils.stringToFile(KERNEL_COUNTER_FILE, userId + " " + newCounter);
                        } catch (java.io.IOException e) {
                            android.util.Slog.w(TAG, "Failed to update counter set: " + e);
                        }
                        break;
                    }
                }
                return true;
            case 8:
                int userId2 = msg.arg1;
                android.os.Trace.traceBegin(524288L, "usageStatsHandleEstimatedLaunchTimesOnUser(" + userId2 + ")");
                handleEstimatedLaunchTimesOnUserUnlock(userId2);
                android.os.Trace.traceEnd(524288L);
                return true;
            case 12:
                int userId3 = msg.arg1;
                android.app.usage.UsageEvents.Event event = (android.app.usage.UsageEvents.Event) msg.obj;
                synchronized (this.mUsageEventListeners) {
                    int size = this.mUsageEventListeners.size();
                    for (int i = 0; i < size; i++) {
                        this.mUsageEventListeners.valueAt(i).onUsageEvent(userId3, event);
                    }
                    break;
                }
                return true;
            default:
                return false;
        }
    }

    public UsageStatsService(android.content.Context context) {
        this(context, new com.android.server.usage.UsageStatsService.Injector());
    }

    UsageStatsService(android.content.Context context, com.android.server.usage.UsageStatsService.Injector injector) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mUserState = new android.util.SparseArray<>();
        this.mUserUnlockedStates = new java.util.concurrent.CopyOnWriteArraySet<>();
        this.mUidToKernelCounter = new android.util.SparseIntArray();
        this.mLastTimeComponentUsedGlobal = new android.util.ArrayMap();
        this.mPackageMonitor = new com.android.server.usage.UsageStatsService.MyPackageMonitor();
        this.mReportedEvents = new android.util.SparseArray<>();
        this.mUsageReporters = new android.util.SparseArray<>();
        this.mVisibleActivities = new android.util.SparseArray<>();
        this.mLaunchTimeAlarmQueues = new android.util.SparseArray<>();
        this.mUsageEventListeners = new android.util.ArraySet<>();
        this.mEstimatedLaunchTimeChangedListeners = new java.util.concurrent.CopyOnWriteArraySet<>();
        this.mPendingLaunchTimeChangePackages = new android.util.SparseSetArray<>();
        this.mExt = (com.android.server.usage.IUsageStatsServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.usage.IUsageStatsServiceExt.class).base(this).create();
        this.mStandbyChangeListener = new com.android.server.usage.AppStandbyInternal.AppIdleStateChangeListener() { // from class: com.android.server.usage.UsageStatsService.1
            public void onAppIdleStateChanged(java.lang.String packageName, int userId, boolean idle, int bucket, int reason) {
                android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(11, android.os.SystemClock.elapsedRealtime());
                event.mBucketAndReason = (bucket << 16) | (65535 & reason);
                event.mPackage = packageName;
                com.android.server.usage.UsageStatsService.this.reportEventOrAddToQueue(userId, event);
            }
        };
        this.mIoHandlerCallback = new android.os.Handler.Callback() { // from class: com.android.server.usage.UsageStatsService$$ExternalSyntheticLambda0
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(android.os.Message message) {
                return this.f$0.lambda$new$0(message);
            }
        };
        this.mUidObserver = new android.app.UidObserver() { // from class: com.android.server.usage.UsageStatsService.3
            public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) {
                com.android.server.usage.UsageStatsService.this.mIoHandler.obtainMessage(3, uid, procState).sendToTarget();
            }

            public void onUidGone(int uid, boolean disabled) {
                onUidStateChanged(uid, 20, 0L, 0);
            }
        };
        this.mInjector = injector;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.SystemService
    public void onStart() {
        this.mAppOps = (android.app.AppOpsManager) getContext().getSystemService("appops");
        this.mUserManager = (android.os.UserManager) getContext().getSystemService("user");
        this.mPackageManager = getContext().getPackageManager();
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mHandler = getUsageEventProcessingHandler();
        this.mIoHandler = new android.os.Handler(com.android.server.IoThread.get().getLooper(), this.mIoHandlerCallback);
        this.mAppStandby = this.mInjector.getAppStandbyController(getContext());
        this.mResponseStatsTracker = new com.android.server.usage.BroadcastResponseStatsTracker(this.mAppStandby, getContext());
        this.mAppTimeLimit = new com.android.server.usage.AppTimeLimitController(getContext(), new com.android.server.usage.AppTimeLimitController.TimeLimitCallbackListener() { // from class: com.android.server.usage.UsageStatsService.2
            @Override // com.android.server.usage.AppTimeLimitController.TimeLimitCallbackListener
            public void onLimitReached(int observerId, int userId, long timeLimit, long timeElapsed, android.app.PendingIntent callbackIntent) {
                if (callbackIntent == null) {
                    return;
                }
                android.content.Intent intent = new android.content.Intent();
                intent.putExtra("android.app.usage.extra.OBSERVER_ID", observerId);
                intent.putExtra("android.app.usage.extra.TIME_LIMIT", timeLimit);
                intent.putExtra("android.app.usage.extra.TIME_USED", timeElapsed);
                try {
                    callbackIntent.send(com.android.server.usage.UsageStatsService.this.getContext(), 0, intent);
                } catch (android.app.PendingIntent.CanceledException e) {
                    android.util.Slog.w(com.android.server.usage.UsageStatsService.TAG, "Couldn't deliver callback: " + callbackIntent);
                }
            }

            @Override // com.android.server.usage.AppTimeLimitController.TimeLimitCallbackListener
            public void onSessionEnd(int observerId, int userId, long timeElapsed, android.app.PendingIntent callbackIntent) {
                if (callbackIntent == null) {
                    return;
                }
                android.content.Intent intent = new android.content.Intent();
                intent.putExtra("android.app.usage.extra.OBSERVER_ID", observerId);
                intent.putExtra("android.app.usage.extra.TIME_USED", timeElapsed);
                try {
                    callbackIntent.send(com.android.server.usage.UsageStatsService.this.getContext(), 0, intent);
                } catch (android.app.PendingIntent.CanceledException e) {
                    android.util.Slog.w(com.android.server.usage.UsageStatsService.TAG, "Couldn't deliver callback: " + callbackIntent);
                }
            }
        }, this.mHandler.getLooper());
        this.mAppStandby.addListener(this.mStandbyChangeListener);
        java.lang.Object[] objArr = 0;
        java.lang.Object[] objArr2 = 0;
        this.mPackageMonitor.register(getContext(), USE_DEDICATED_HANDLER_THREAD ? this.mHandler.getLooper() : null, android.os.UserHandle.ALL, true);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.intent.action.USER_STARTED");
        getContext().registerReceiverAsUser(new com.android.server.usage.UsageStatsService.UserActionsReceiver(), android.os.UserHandle.ALL, intentFilter, null, USE_DEDICATED_HANDLER_THREAD ? this.mHandler : null);
        getContext().registerReceiverAsUser(new com.android.server.usage.UsageStatsService.UidRemovedReceiver(), android.os.UserHandle.ALL, new android.content.IntentFilter("android.intent.action.UID_REMOVED"), null, USE_DEDICATED_HANDLER_THREAD ? this.mHandler : null);
        this.mRealTimeSnapshot = android.os.SystemClock.elapsedRealtime();
        this.mSystemTimeSnapshot = java.lang.System.currentTimeMillis();
        publishLocalService(android.app.usage.UsageStatsManagerInternal.class, new com.android.server.usage.UsageStatsService.LocalService());
        publishLocalService(com.android.server.usage.AppStandbyInternal.class, this.mAppStandby);
        publishBinderServices();
        this.mHandler.obtainMessage(7).sendToTarget();
    }

    void publishBinderServices() {
        publishBinderService("usagestats", new com.android.server.usage.UsageStatsService.BinderService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        this.mAppStandby.onBootPhase(phase);
        if (phase == 500) {
            getDpmInternal();
            getShortcutServiceInternal();
            this.mResponseStatsTracker.onSystemServicesReady(getContext());
            if (KERNEL_COUNTER_FILE.exists()) {
                try {
                    android.app.ActivityManager.getService().registerUidObserver(this.mUidObserver, 3, -1, (java.lang.String) null);
                } catch (android.os.RemoteException e) {
                    throw new java.lang.RuntimeException(e);
                }
            } else {
                android.util.Slog.w(TAG, "Missing procfs interface: " + KERNEL_COUNTER_FILE);
            }
            readUsageSourceSetting();
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        this.mUserState.put(user.getUserIdentifier(), null);
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        this.mHandler.obtainMessage(5, user.getUserIdentifier(), 0).sendToTarget();
    }

    @Override // com.android.server.SystemService
    public void onUserStopping(com.android.server.SystemService.TargetUser user) {
        int userId = user.getUserIdentifier();
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                persistPendingEventsLocked(userId);
                return;
            }
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(29, android.os.SystemClock.elapsedRealtime());
            event.mPackage = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME;
            reportEvent(event, userId);
            com.android.server.usage.UserUsageStatsService userService = this.mUserState.get(userId);
            if (userService != null) {
                userService.userStopped();
            }
            this.mUserUnlockedStates.remove(java.lang.Integer.valueOf(userId));
            this.mUserState.put(userId, null);
            synchronized (this.mLaunchTimeAlarmQueues) {
                com.android.server.usage.UsageStatsService.LaunchTimeAlarmQueue alarmQueue = this.mLaunchTimeAlarmQueues.get(userId);
                if (alarmQueue != null) {
                    alarmQueue.removeAllAlarms();
                    this.mLaunchTimeAlarmQueues.remove(userId);
                }
            }
        }
    }

    private android.os.Handler getUsageEventProcessingHandler() {
        if (USE_DEDICATED_HANDLER_THREAD) {
            return new com.android.server.usage.UsageStatsService.H(com.android.server.usage.UsageStatsHandlerThread.get().getLooper());
        }
        return new com.android.server.usage.UsageStatsService.H(this.mExt.getBackgroundHandlerThread().getLooper());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserUnlocked(int userId) {
        java.util.HashMap<java.lang.String, java.lang.Long> installedPackages = getInstalledPackages(userId);
        com.android.server.usage.UsageStatsIdleService.scheduleUpdateMappingsJob(getContext(), userId);
        boolean deleteObsoleteData = shouldDeleteObsoleteData(android.os.UserHandle.of(userId));
        synchronized (this.mLock) {
            this.mUserUnlockedStates.add(java.lang.Integer.valueOf(userId));
            android.app.usage.UsageEvents.Event unlockEvent = new android.app.usage.UsageEvents.Event(28, android.os.SystemClock.elapsedRealtime());
            unlockEvent.mPackage = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME;
            migrateStatsToSystemCeIfNeededLocked(userId);
            java.util.LinkedList<android.app.usage.UsageEvents.Event> pendingEvents = new java.util.LinkedList<>();
            android.os.Trace.traceBegin(524288L, "loadPendingEvents");
            loadPendingEventsLocked(userId, pendingEvents);
            android.os.Trace.traceEnd(524288L);
            synchronized (this.mReportedEvents) {
                java.util.LinkedList<android.app.usage.UsageEvents.Event> eventsInMem = this.mReportedEvents.get(userId);
                if (eventsInMem != null) {
                    pendingEvents.addAll(eventsInMem);
                    this.mReportedEvents.remove(userId);
                }
            }
            boolean needToFlush = !pendingEvents.isEmpty();
            initializeUserUsageStatsServiceLocked(userId, java.lang.System.currentTimeMillis(), installedPackages, deleteObsoleteData);
            com.android.server.usage.UserUsageStatsService userService = getUserUsageStatsServiceLocked(userId);
            if (userService == null) {
                android.util.Slog.i(TAG, "Attempted to unlock stopped or removed user " + userId);
                return;
            }
            while (pendingEvents.peek() != null) {
                reportEvent(pendingEvents.poll(), userId);
            }
            reportEvent(unlockEvent, userId);
            deleteRecursively(new java.io.File(android.os.Environment.getDataSystemDeDirectory(userId), "usagestats"));
            if (needToFlush) {
                userService.persistActiveStats();
            }
            this.mIoHandler.obtainMessage(8, userId, 0).sendToTarget();
        }
    }

    private java.util.HashMap<java.lang.String, java.lang.Long> getInstalledPackages(int userId) {
        if (this.mPackageManager == null) {
            return null;
        }
        java.util.List<android.content.pm.PackageInfo> installedPackages = this.mPackageManager.getInstalledPackagesAsUser(8192, userId);
        java.util.HashMap<java.lang.String, java.lang.Long> packagesMap = new java.util.HashMap<>();
        for (int i = installedPackages.size() - 1; i >= 0; i--) {
            android.content.pm.PackageInfo packageInfo = installedPackages.get(i);
            packagesMap.put(packageInfo.packageName, java.lang.Long.valueOf(packageInfo.firstInstallTime));
        }
        return packagesMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.admin.DevicePolicyManagerInternal getDpmInternal() {
        if (this.mDpmInternal == null) {
            this.mDpmInternal = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        }
        return this.mDpmInternal;
    }

    private android.content.pm.ShortcutServiceInternal getShortcutServiceInternal() {
        if (this.mShortcutServiceInternal == null) {
            this.mShortcutServiceInternal = (android.content.pm.ShortcutServiceInternal) com.android.server.LocalServices.getService(android.content.pm.ShortcutServiceInternal.class);
        }
        return this.mShortcutServiceInternal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readUsageSourceSetting() {
        synchronized (this.mLock) {
            this.mUsageSource = android.provider.Settings.Global.getInt(getContext().getContentResolver(), "app_time_limit_usage_source", 1);
        }
    }

    private class LaunchTimeAlarmQueue extends com.android.server.utils.AlarmQueue<java.lang.String> {
        private final int mUserId;

        LaunchTimeAlarmQueue(int userId, android.content.Context context, android.os.Looper looper) {
            super(context, looper, "*usage.launchTime*", "Estimated launch times", true, 30000L);
            this.mUserId = userId;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.utils.AlarmQueue
        public boolean isForUser(java.lang.String key, int userId) {
            return this.mUserId == userId;
        }

        @Override // com.android.server.utils.AlarmQueue
        protected void processExpiredAlarms(android.util.ArraySet<java.lang.String> expired) {
            if (com.android.server.usage.UsageStatsService.DEBUG) {
                android.util.Slog.d(com.android.server.usage.UsageStatsService.TAG, "Processing " + expired.size() + " expired alarms: " + expired.toString());
            }
            if (expired.size() > 0) {
                synchronized (com.android.server.usage.UsageStatsService.this.mPendingLaunchTimeChangePackages) {
                    com.android.server.usage.UsageStatsService.this.mPendingLaunchTimeChangePackages.addAll(this.mUserId, expired);
                }
                com.android.server.usage.UsageStatsService.this.mHandler.sendEmptyMessage(9);
            }
        }
    }

    private class UserActionsReceiver extends android.content.BroadcastReceiver {
        private UserActionsReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            int userId = intent.getIntExtra("android.intent.extra.user_handle", -1);
            java.lang.String action = intent.getAction();
            if ("android.intent.action.USER_REMOVED".equals(action)) {
                if (userId >= 0) {
                    com.android.server.usage.UsageStatsService.this.mHandler.obtainMessage(2, userId, 0).sendToTarget();
                }
            } else if ("android.intent.action.USER_STARTED".equals(action) && userId >= 0) {
                if (!android.app.usage.Flags.disableIdleCheck() || userId > 0) {
                    com.android.server.usage.UsageStatsService.this.mHandler.obtainMessage(11, userId, 0).sendToTarget();
                }
            }
        }
    }

    private class UidRemovedReceiver extends android.content.BroadcastReceiver {
        private UidRemovedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            int uid = intent.getIntExtra("android.intent.extra.UID", -1);
            if (uid == -1) {
                return;
            }
            com.android.server.usage.UsageStatsService.this.mHandler.obtainMessage(10, uid, 0).sendToTarget();
        }
    }

    @Override // com.android.server.usage.UserUsageStatsService.StatsUpdatedListener
    public void onStatsUpdated() {
        this.mHandler.sendEmptyMessageDelayed(1, 1200000L);
    }

    @Override // com.android.server.usage.UserUsageStatsService.StatsUpdatedListener
    public void onStatsReloaded() {
        this.mAppStandby.postOneTimeCheckIdleStates();
    }

    @Override // com.android.server.usage.UserUsageStatsService.StatsUpdatedListener
    public void onNewUpdate(int userId) {
        this.mAppStandby.initializeDefaultsForSystemApps(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sameApp(int callingUid, int userId, java.lang.String packageName) {
        return this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId) == callingUid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInstantApp(java.lang.String packageName, int userId) {
        return this.mPackageManagerInternal.isPackageEphemeral(userId, packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldObfuscateInstantAppsForCaller(int callingUid, int userId) {
        return !this.mPackageManagerInternal.canAccessInstantApps(callingUid, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldHideShortcutInvocationEvents(int userId, java.lang.String callingPackage, int callingPid, int callingUid) {
        android.content.pm.ShortcutServiceInternal shortcutServiceInternal = getShortcutServiceInternal();
        if (shortcutServiceInternal != null) {
            return true ^ shortcutServiceInternal.hasShortcutHostPermission(userId, callingPackage, callingPid, callingUid);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldHideLocusIdEvents(int callingPid, int callingUid) {
        return (callingUid == 1000 || getContext().checkPermission("android.permission.ACCESS_LOCUS_ID_USAGE_STATS", callingPid, callingUid) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldObfuscateNotificationEvents(int callingPid, int callingUid) {
        return (callingUid == 1000 || getContext().checkPermission("android.permission.MANAGE_NOTIFICATIONS", callingPid, callingUid) == 0) ? false : true;
    }

    private static void deleteRecursively(java.io.File path) {
        java.io.File[] files;
        if (path.isDirectory() && (files = path.listFiles()) != null) {
            for (java.io.File subFile : files) {
                deleteRecursively(subFile);
            }
        }
        if (path.exists() && !path.delete()) {
            android.util.Slog.e(TAG, "Failed to delete " + path);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.usage.UserUsageStatsService getUserUsageStatsServiceLocked(int userId) {
        com.android.server.usage.UserUsageStatsService service = this.mUserState.get(userId);
        if (service == null) {
            android.util.Slog.wtf(TAG, "Failed to fetch usage stats service for user " + userId + ". The user might not have been initialized yet.");
        }
        return service;
    }

    private void initializeUserUsageStatsServiceLocked(int userId, long currentTimeMillis, java.util.HashMap<java.lang.String, java.lang.Long> installedPackages, boolean deleteObsoleteData) throws java.lang.Exception {
        java.io.File usageStatsDir = new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), "usagestats");
        com.android.server.usage.UserUsageStatsService service = new com.android.server.usage.UserUsageStatsService(getContext(), userId, usageStatsDir, this);
        try {
            service.init(currentTimeMillis, installedPackages, deleteObsoleteData);
            this.mUserState.put(userId, service);
        } catch (java.lang.Exception e) {
            if (this.mUserManager.isUserUnlocked(userId)) {
                android.util.Slog.w(TAG, "Failed to initialized unlocked user " + userId);
                throw e;
            }
            android.util.Slog.w(TAG, "Attempted to initialize service for stopped or removed user " + userId);
        }
    }

    private void migrateStatsToSystemCeIfNeededLocked(int userId) {
        java.io.File usageStatsDir = new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), "usagestats");
        if (!usageStatsDir.mkdirs() && !usageStatsDir.exists()) {
            throw new java.lang.IllegalStateException("Usage stats directory does not exist: " + usageStatsDir.getAbsolutePath());
        }
        java.io.File migrated = new java.io.File(usageStatsDir, "migrated");
        if (migrated.exists()) {
            try {
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(migrated));
                try {
                    int previousVersion = java.lang.Integer.parseInt(reader.readLine());
                    if (previousVersion >= 4) {
                        deleteLegacyUserDir(userId);
                        reader.close();
                        return;
                    }
                    reader.close();
                } catch (java.lang.Throwable th) {
                    try {
                        reader.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (java.io.IOException | java.lang.NumberFormatException e) {
                android.util.Slog.e(TAG, "Failed to read migration status file, possibly corrupted.");
                deleteRecursively(usageStatsDir);
                if (usageStatsDir.exists()) {
                    android.util.Slog.e(TAG, "Unable to delete usage stats CE directory.");
                    throw new java.lang.RuntimeException(e);
                }
                if (!usageStatsDir.mkdirs() && !usageStatsDir.exists()) {
                    throw new java.lang.IllegalStateException("Usage stats directory does not exist: " + usageStatsDir.getAbsolutePath());
                }
            }
        }
        android.util.Slog.i(TAG, "Starting migration to system CE for user " + userId);
        java.io.File legacyUserDir = new java.io.File(LEGACY_USER_USAGE_STATS_DIR, java.lang.Integer.toString(userId));
        if (legacyUserDir.exists()) {
            copyRecursively(usageStatsDir, legacyUserDir);
        }
        try {
            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(migrated));
            try {
                writer.write(java.lang.Integer.toString(4));
                writer.write("\n");
                writer.flush();
                writer.close();
                android.util.Slog.i(TAG, "Finished migration to system CE for user " + userId);
                deleteLegacyUserDir(userId);
            } finally {
            }
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Failed to write migrated status file");
            throw new java.lang.RuntimeException(e2);
        }
    }

    private static void copyRecursively(java.io.File parent, java.io.File f) {
        java.io.File[] files = f.listFiles();
        if (files == null) {
            try {
                java.nio.file.Files.copy(f.toPath(), new java.io.File(parent, f.getName()).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                return;
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Failed to move usage stats file : " + f.toString());
                throw new java.lang.RuntimeException(e);
            }
        }
        for (int i = files.length - 1; i >= 0; i--) {
            java.io.File newParent = parent;
            if (files[i].isDirectory()) {
                newParent = new java.io.File(parent, files[i].getName());
                boolean mkdirSuccess = newParent.mkdirs();
                if (!mkdirSuccess && !newParent.exists()) {
                    throw new java.lang.IllegalStateException("Failed to create usage stats directory during migration: " + newParent.getAbsolutePath());
                }
            }
            copyRecursively(newParent, files[i]);
        }
    }

    private void deleteLegacyUserDir(int userId) {
        java.io.File legacyUserDir = new java.io.File(LEGACY_USER_USAGE_STATS_DIR, java.lang.Integer.toString(userId));
        if (legacyUserDir.exists()) {
            deleteRecursively(legacyUserDir);
            if (legacyUserDir.exists()) {
                android.util.Slog.w(TAG, "Error occurred while attempting to delete legacy usage stats dir for user " + userId);
            }
        }
    }

    void shutdown() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(0);
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(26, android.os.SystemClock.elapsedRealtime());
            event.mPackage = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME;
            reportEventToAllUserId(event);
            flushToDiskLocked();
            persistGlobalComponentUsageLocked();
        }
        this.mAppStandby.flushToDisk();
    }

    void prepareForPossibleShutdown() {
        android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(26, android.os.SystemClock.elapsedRealtime());
        event.mPackage = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME;
        this.mHandler.obtainMessage(4, event).sendToTarget();
        this.mHandler.sendEmptyMessage(1);
    }

    private void loadPendingEventsLocked(int userId, java.util.LinkedList<android.app.usage.UsageEvents.Event> pendingEvents) {
        java.io.File usageStatsDeDir = new java.io.File(android.os.Environment.getDataSystemDeDirectory(userId), "usagestats");
        java.io.File[] pendingEventsFiles = usageStatsDeDir.listFiles();
        if (pendingEventsFiles == null || pendingEventsFiles.length == 0) {
            return;
        }
        java.util.Arrays.sort(pendingEventsFiles);
        int numFiles = pendingEventsFiles.length;
        for (int i = 0; i < numFiles; i++) {
            android.util.AtomicFile af = new android.util.AtomicFile(pendingEventsFiles[i]);
            java.util.LinkedList<android.app.usage.UsageEvents.Event> tmpEvents = new java.util.LinkedList<>();
            try {
                java.io.FileInputStream in = af.openRead();
                try {
                    com.android.server.usage.UsageStatsProtoV2.readPendingEvents(in, tmpEvents);
                    if (in != null) {
                        in.close();
                    }
                    pendingEvents.addAll(tmpEvents);
                } catch (java.lang.Throwable th) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Could not read " + pendingEventsFiles[i] + " for user " + userId);
            }
        }
    }

    private void persistPendingEventsLocked(int userId) {
        java.util.LinkedList<android.app.usage.UsageEvents.Event> pendingEvents = this.mReportedEvents.get(userId);
        if (pendingEvents == null || pendingEvents.isEmpty()) {
            return;
        }
        java.io.File deDir = android.os.Environment.getDataSystemDeDirectory(userId);
        java.io.File usageStatsDeDir = new java.io.File(deDir, "usagestats");
        if (!usageStatsDeDir.mkdir() && !usageStatsDeDir.exists()) {
            if (deDir.exists()) {
                android.util.Slog.e(TAG, "Failed to create " + usageStatsDeDir);
                return;
            } else {
                android.util.Slog.w(TAG, "User " + userId + " was already removed! Discarding pending events");
                pendingEvents.clear();
                return;
            }
        }
        java.io.File pendingEventsFile = new java.io.File(usageStatsDeDir, "pendingevents_" + java.lang.System.currentTimeMillis());
        android.util.AtomicFile af = new android.util.AtomicFile(pendingEventsFile);
        java.io.FileOutputStream fos = null;
        try {
            try {
                java.io.FileOutputStream fos2 = af.startWrite();
                com.android.server.usage.UsageStatsProtoV2.writePendingEvents(fos2, pendingEvents);
                af.finishWrite(fos2);
                fos = null;
                pendingEvents.clear();
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Failed to write " + pendingEventsFile.getAbsolutePath() + " for user " + userId);
            }
        } finally {
            af.failWrite(fos);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadGlobalComponentUsageLocked() {
        android.util.AtomicFile af = new android.util.AtomicFile(new java.io.File(COMMON_USAGE_STATS_DIR, GLOBAL_COMPONENT_USAGE_FILE_NAME));
        if (!af.exists()) {
            af = new android.util.AtomicFile(new java.io.File(LEGACY_COMMON_USAGE_STATS_DIR, GLOBAL_COMPONENT_USAGE_FILE_NAME));
            if (!af.exists()) {
                return;
            } else {
                android.util.Slog.i(TAG, "Reading globalcomponentusage file from old location");
            }
        }
        java.util.Map<java.lang.String, java.lang.Long> tmpUsage = new android.util.ArrayMap<>();
        try {
            java.io.FileInputStream in = af.openRead();
            try {
                com.android.server.usage.UsageStatsProtoV2.readGlobalComponentUsage(in, tmpUsage);
                if (in != null) {
                    in.close();
                }
                java.util.Map.Entry<java.lang.String, java.lang.Long>[] entries = (java.util.Map.Entry[]) tmpUsage.entrySet().toArray();
                int size = entries.length;
                for (int i = 0; i < size; i++) {
                    this.mLastTimeComponentUsedGlobal.putIfAbsent(entries[i].getKey(), entries[i].getValue());
                }
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Could not read " + af.getBaseFile());
        }
    }

    private void persistGlobalComponentUsageLocked() {
        if (this.mLastTimeComponentUsedGlobal.isEmpty()) {
            return;
        }
        if (!COMMON_USAGE_STATS_DIR.mkdirs() && !COMMON_USAGE_STATS_DIR.exists()) {
            throw new java.lang.IllegalStateException("Common usage stats directory does not exist: " + COMMON_USAGE_STATS_DIR.getAbsolutePath());
        }
        java.io.File lastTimePackageFile = new java.io.File(COMMON_USAGE_STATS_DIR, GLOBAL_COMPONENT_USAGE_FILE_NAME);
        android.util.AtomicFile af = new android.util.AtomicFile(lastTimePackageFile);
        java.io.FileOutputStream fos = null;
        try {
            try {
                fos = af.startWrite();
                com.android.server.usage.UsageStatsProtoV2.writeGlobalComponentUsage(fos, this.mLastTimeComponentUsedGlobal);
                af.finishWrite(fos);
                fos = null;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Failed to write " + lastTimePackageFile.getAbsolutePath());
            }
        } finally {
            af.failWrite(fos);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportEventOrAddToQueue(int userId, android.app.usage.UsageEvents.Event event) {
        if (this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
            this.mHandler.obtainMessage(0, userId, 0, event).sendToTarget();
            return;
        }
        if (android.os.Trace.isTagEnabled(524288L)) {
            java.lang.String traceTag = "usageStatsQueueEvent(" + userId + ") #" + com.android.server.usage.UserUsageStatsService.eventToString(event.mEventType);
            android.os.Trace.traceBegin(524288L, traceTag);
        }
        synchronized (this.mReportedEvents) {
            java.util.LinkedList<android.app.usage.UsageEvents.Event> events = this.mReportedEvents.get(userId);
            if (events == null) {
                events = new java.util.LinkedList<>();
                this.mReportedEvents.put(userId, events);
            }
            events.add(event);
            if (events.size() == 1) {
                this.mHandler.sendEmptyMessageDelayed(1, 1200000L);
            }
        }
        android.os.Trace.traceEnd(524288L);
    }

    private void convertToSystemTimeLocked(android.app.usage.UsageEvents.Event event) {
        long actualSystemTime = java.lang.System.currentTimeMillis();
        if (ENABLE_TIME_CHANGE_CORRECTION) {
            long actualRealtime = android.os.SystemClock.elapsedRealtime();
            long expectedSystemTime = (actualRealtime - this.mRealTimeSnapshot) + this.mSystemTimeSnapshot;
            long diffSystemTime = actualSystemTime - expectedSystemTime;
            if (java.lang.Math.abs(diffSystemTime) > TIME_CHANGE_THRESHOLD_MILLIS) {
                android.util.Slog.i(TAG, "Time changed in by " + (diffSystemTime / 1000) + " seconds");
                this.mRealTimeSnapshot = actualRealtime;
                this.mSystemTimeSnapshot = actualSystemTime;
            }
        }
        event.mTimeStamp = java.lang.Math.max(0L, event.mTimeStamp - this.mRealTimeSnapshot) + this.mSystemTimeSnapshot;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:24:0x009c A[Catch: all -> 0x0265, TryCatch #3 {, blocks: (B:13:0x0027, B:15:0x0033, B:16:0x0081, B:18:0x0083, B:19:0x0087, B:93:0x024c, B:95:0x0252, B:97:0x0254, B:98:0x0257, B:21:0x008c, B:22:0x0090, B:24:0x009c, B:25:0x00e0, B:27:0x00e2, B:29:0x00e6, B:30:0x00eb, B:31:0x00ed, B:35:0x00fb, B:48:0x0135, B:49:0x0136, B:51:0x013a, B:52:0x0146, B:55:0x0152, B:59:0x015e, B:60:0x015f, B:61:0x0168, B:62:0x016e, B:63:0x0180, B:65:0x018c, B:66:0x0190, B:70:0x019e, B:72:0x01b5, B:74:0x01bb, B:69:0x0197, B:71:0x01b0, B:75:0x01c9, B:78:0x01d9, B:79:0x01dd, B:83:0x01eb, B:85:0x020e, B:87:0x0216, B:89:0x021a, B:90:0x0236, B:92:0x0245, B:82:0x01e4, B:36:0x00fc, B:38:0x0103, B:39:0x0109, B:43:0x012e, B:42:0x0116, B:44:0x0131, B:32:0x00ee, B:33:0x00f8), top: B:110:0x0027, inners: #0, #2, #4, #5, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00e2 A[Catch: all -> 0x0265, TryCatch #3 {, blocks: (B:13:0x0027, B:15:0x0033, B:16:0x0081, B:18:0x0083, B:19:0x0087, B:93:0x024c, B:95:0x0252, B:97:0x0254, B:98:0x0257, B:21:0x008c, B:22:0x0090, B:24:0x009c, B:25:0x00e0, B:27:0x00e2, B:29:0x00e6, B:30:0x00eb, B:31:0x00ed, B:35:0x00fb, B:48:0x0135, B:49:0x0136, B:51:0x013a, B:52:0x0146, B:55:0x0152, B:59:0x015e, B:60:0x015f, B:61:0x0168, B:62:0x016e, B:63:0x0180, B:65:0x018c, B:66:0x0190, B:70:0x019e, B:72:0x01b5, B:74:0x01bb, B:69:0x0197, B:71:0x01b0, B:75:0x01c9, B:78:0x01d9, B:79:0x01dd, B:83:0x01eb, B:85:0x020e, B:87:0x0216, B:89:0x021a, B:90:0x0236, B:92:0x0245, B:82:0x01e4, B:36:0x00fc, B:38:0x0103, B:39:0x0109, B:43:0x012e, B:42:0x0116, B:44:0x0131, B:32:0x00ee, B:33:0x00f8), top: B:110:0x0027, inners: #0, #2, #4, #5, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0252 A[Catch: all -> 0x0265, DONT_GENERATE, TryCatch #3 {, blocks: (B:13:0x0027, B:15:0x0033, B:16:0x0081, B:18:0x0083, B:19:0x0087, B:93:0x024c, B:95:0x0252, B:97:0x0254, B:98:0x0257, B:21:0x008c, B:22:0x0090, B:24:0x009c, B:25:0x00e0, B:27:0x00e2, B:29:0x00e6, B:30:0x00eb, B:31:0x00ed, B:35:0x00fb, B:48:0x0135, B:49:0x0136, B:51:0x013a, B:52:0x0146, B:55:0x0152, B:59:0x015e, B:60:0x015f, B:61:0x0168, B:62:0x016e, B:63:0x0180, B:65:0x018c, B:66:0x0190, B:70:0x019e, B:72:0x01b5, B:74:0x01bb, B:69:0x0197, B:71:0x01b0, B:75:0x01c9, B:78:0x01d9, B:79:0x01dd, B:83:0x01eb, B:85:0x020e, B:87:0x0216, B:89:0x021a, B:90:0x0236, B:92:0x0245, B:82:0x01e4, B:36:0x00fc, B:38:0x0103, B:39:0x0109, B:43:0x012e, B:42:0x0116, B:44:0x0131, B:32:0x00ee, B:33:0x00f8), top: B:110:0x0027, inners: #0, #2, #4, #5, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0254 A[Catch: all -> 0x0265, TryCatch #3 {, blocks: (B:13:0x0027, B:15:0x0033, B:16:0x0081, B:18:0x0083, B:19:0x0087, B:93:0x024c, B:95:0x0252, B:97:0x0254, B:98:0x0257, B:21:0x008c, B:22:0x0090, B:24:0x009c, B:25:0x00e0, B:27:0x00e2, B:29:0x00e6, B:30:0x00eb, B:31:0x00ed, B:35:0x00fb, B:48:0x0135, B:49:0x0136, B:51:0x013a, B:52:0x0146, B:55:0x0152, B:59:0x015e, B:60:0x015f, B:61:0x0168, B:62:0x016e, B:63:0x0180, B:65:0x018c, B:66:0x0190, B:70:0x019e, B:72:0x01b5, B:74:0x01bb, B:69:0x0197, B:71:0x01b0, B:75:0x01c9, B:78:0x01d9, B:79:0x01dd, B:83:0x01eb, B:85:0x020e, B:87:0x0216, B:89:0x021a, B:90:0x0236, B:92:0x0245, B:82:0x01e4, B:36:0x00fc, B:38:0x0103, B:39:0x0109, B:43:0x012e, B:42:0x0116, B:44:0x0131, B:32:0x00ee, B:33:0x00f8), top: B:110:0x0027, inners: #0, #2, #4, #5, #6 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void reportEvent(android.app.usage.UsageEvents.Event r14, int r15) {
        /*
            Method dump skipped, instruction units count: 672
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usage.UsageStatsService.reportEvent(android.app.usage.UsageEvents$Event, int):void");
    }

    private void logAppUsageEventReportedAtomLocked(int eventType, int uid, java.lang.String packageName) {
        com.android.internal.util.FrameworkStatsLog.write(269, uid, packageName, "", getAppUsageEventOccurredAtomEventType(eventType));
    }

    private int getAppUsageEventOccurredAtomEventType(int eventType) {
        switch (eventType) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 11:
                return 11;
            case 19:
                return 19;
            case 20:
                return 20;
            default:
                android.util.Slog.w(TAG, "Unsupported usage event logging: " + eventType);
                return -1;
        }
    }

    private java.lang.String getUsageSourcePackage(android.app.usage.UsageEvents.Event event) {
        switch (this.mUsageSource) {
            case 2:
                return event.mPackage;
            default:
                return event.mTaskRootPackage;
        }
    }

    void reportEventToAllUserId(android.app.usage.UsageEvents.Event event) {
        synchronized (this.mLock) {
            int userCount = this.mUserState.size();
            for (int i = 0; i < userCount; i++) {
                android.app.usage.UsageEvents.Event copy = new android.app.usage.UsageEvents.Event(event);
                reportEventOrAddToQueue(this.mUserState.keyAt(i), copy);
            }
        }
    }

    void flushToDisk() {
        synchronized (this.mLock) {
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(25, android.os.SystemClock.elapsedRealtime());
            event.mPackage = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME;
            reportEventToAllUserId(event);
            flushToDiskLocked();
        }
        this.mAppStandby.flushToDisk();
    }

    void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            android.util.Slog.i(TAG, "Removing user " + userId + " and all data.");
            this.mUserState.remove(userId);
            this.mAppTimeLimit.onUserRemoved(userId);
        }
        synchronized (this.mLaunchTimeAlarmQueues) {
            com.android.server.usage.UsageStatsService.LaunchTimeAlarmQueue alarmQueue = this.mLaunchTimeAlarmQueues.get(userId);
            if (alarmQueue != null) {
                alarmQueue.removeAllAlarms();
                this.mLaunchTimeAlarmQueues.remove(userId);
            }
        }
        synchronized (this.mPendingLaunchTimeChangePackages) {
            this.mPendingLaunchTimeChangePackages.remove(userId);
        }
        this.mAppStandby.onUserRemoved(userId);
        this.mResponseStatsTracker.onUserRemoved(userId);
        com.android.server.usage.UsageStatsIdleService.cancelPruneJob(getContext(), userId);
        com.android.server.usage.UsageStatsIdleService.cancelUpdateMappingsJob(getContext(), userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageRemoved(int userId, java.lang.String packageName) {
        synchronized (this.mPendingLaunchTimeChangePackages) {
            android.util.ArraySet<java.lang.String> pkgNames = this.mPendingLaunchTimeChangePackages.get(userId);
            if (pkgNames != null) {
                pkgNames.remove(packageName);
            }
        }
        synchronized (this.mLaunchTimeAlarmQueues) {
            com.android.server.usage.UsageStatsService.LaunchTimeAlarmQueue alarmQueue = this.mLaunchTimeAlarmQueues.get(userId);
            if (alarmQueue != null) {
                alarmQueue.removeAlarmForKey(packageName);
            }
        }
        synchronized (this.mLock) {
            long timeRemoved = java.lang.System.currentTimeMillis();
            if (this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                com.android.server.usage.UserUsageStatsService userService = this.mUserState.get(userId);
                if (userService == null) {
                    return;
                }
                int tokenRemoved = userService.onPackageRemoved(packageName, timeRemoved);
                if (tokenRemoved != -1) {
                    com.android.server.usage.UsageStatsIdleService.schedulePruneJob(getContext(), userId);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean pruneUninstalledPackagesData(int userId) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                return false;
            }
            com.android.server.usage.UserUsageStatsService userService = this.mUserState.get(userId);
            if (userService == null) {
                return false;
            }
            return userService.pruneUninstalledPackagesData();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updatePackageMappingsData(int userId) {
        if (!shouldDeleteObsoleteData(android.os.UserHandle.of(userId))) {
            return true;
        }
        java.util.HashMap<java.lang.String, java.lang.Long> installedPkgs = getInstalledPackages(userId);
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                return false;
            }
            com.android.server.usage.UserUsageStatsService userService = this.mUserState.get(userId);
            if (userService == null) {
                return false;
            }
            return userService.updatePackageMappingsLocked(installedPkgs);
        }
    }

    java.util.List<android.app.usage.UsageStats> queryUsageStats(int userId, int bucketType, long beginTime, long endTime, boolean obfuscateInstantApps) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                android.util.Slog.w(TAG, "Failed to query usage stats for locked user " + userId);
                return null;
            }
            com.android.server.usage.UserUsageStatsService service = getUserUsageStatsServiceLocked(userId);
            if (service == null) {
                return null;
            }
            java.util.List<android.app.usage.UsageStats> list = service.queryUsageStats(bucketType, beginTime, endTime);
            if (list == null) {
                return null;
            }
            if (obfuscateInstantApps) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    android.app.usage.UsageStats stats = list.get(i);
                    if (isInstantApp(stats.mPackageName, userId)) {
                        list.set(i, stats.getObfuscatedForInstantApp());
                    }
                }
            }
            return list;
        }
    }

    java.util.List<android.app.usage.ConfigurationStats> queryConfigurationStats(int userId, int bucketType, long beginTime, long endTime) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                android.util.Slog.w(TAG, "Failed to query configuration stats for locked user " + userId);
                return null;
            }
            com.android.server.usage.UserUsageStatsService service = getUserUsageStatsServiceLocked(userId);
            if (service == null) {
                return null;
            }
            return service.queryConfigurationStats(bucketType, beginTime, endTime);
        }
    }

    java.util.List<android.app.usage.EventStats> queryEventStats(int userId, int bucketType, long beginTime, long endTime) {
        synchronized (this.mLock) {
            if (android.os.Build.IS_AGING_VERSION) {
                android.util.Slog.e(TAG, "skip queryEventStats for aging test");
                return null;
            }
            if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                android.util.Slog.w(TAG, "Failed to query event stats for locked user " + userId);
                return null;
            }
            com.android.server.usage.UserUsageStatsService service = getUserUsageStatsServiceLocked(userId);
            if (service == null) {
                return null;
            }
            return service.queryEventStats(bucketType, beginTime, endTime);
        }
    }

    android.app.usage.UsageEvents queryEvents(int userId, long beginTime, long endTime, int flags) {
        return queryEventsWithQueryFilters(userId, beginTime, endTime, flags, libcore.util.EmptyArray.INT, null);
    }

    android.app.usage.UsageEvents queryEventsWithQueryFilters(int userId, long beginTime, long endTime, int flags, int[] eventTypeFilter, android.util.ArraySet<java.lang.String> pkgNameFilter) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                        android.util.Slog.w(TAG, "Failed to query events for locked user " + userId);
                        return null;
                    }
                    com.android.server.usage.UserUsageStatsService service = getUserUsageStatsServiceLocked(userId);
                    if (service == null) {
                        return null;
                    }
                    return service.queryEvents(beginTime, endTime, flags, eventTypeFilter, pkgNameFilter);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    android.app.usage.UsageEvents queryEventsForPackage(int userId, long beginTime, long endTime, java.lang.String packageName, boolean includeTaskRoot) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                        android.util.Slog.w(TAG, "Failed to query package events for locked user " + userId);
                        return null;
                    }
                    com.android.server.usage.UserUsageStatsService service = getUserUsageStatsServiceLocked(userId);
                    if (service == null) {
                        return null;
                    }
                    return service.queryEventsForPackage(beginTime, endTime, packageName, includeTaskRoot);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    private android.app.usage.UsageEvents queryEarliestAppEvents(int userId, long beginTime, long endTime, int eventType) {
        synchronized (this.mLock) {
            if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                android.util.Slog.w(TAG, "Failed to query earliest events for locked user " + userId);
                return null;
            }
            com.android.server.usage.UserUsageStatsService service = getUserUsageStatsServiceLocked(userId);
            if (service == null) {
                return null;
            }
            return service.queryEarliestAppEvents(beginTime, endTime, eventType);
        }
    }

    private android.app.usage.UsageEvents queryEarliestEventsForPackage(int userId, long beginTime, long endTime, java.lang.String packageName, int eventType) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                        android.util.Slog.w(TAG, "Failed to query earliest package events for locked user " + userId);
                        return null;
                    }
                    com.android.server.usage.UserUsageStatsService service = getUserUsageStatsServiceLocked(userId);
                    if (service == null) {
                        return null;
                    }
                    return service.queryEarliestEventsForPackage(beginTime, endTime, packageName, eventType);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    long getEstimatedPackageLaunchTime(int userId, java.lang.String packageName) throws java.lang.Throwable {
        long estimatedLaunchTime = this.mAppStandby.getEstimatedLaunchTime(packageName, userId);
        long now = java.lang.System.currentTimeMillis();
        if (estimatedLaunchTime < now || estimatedLaunchTime == Long.MAX_VALUE) {
            long estimatedLaunchTime2 = calculateEstimatedPackageLaunchTime(userId, packageName);
            this.mAppStandby.setEstimatedLaunchTime(packageName, userId, estimatedLaunchTime2);
            getOrCreateLaunchTimeAlarmQueue(userId).addAlarm(packageName, android.os.SystemClock.elapsedRealtime() + (estimatedLaunchTime2 - now));
            return estimatedLaunchTime2;
        }
        return estimatedLaunchTime;
    }

    private com.android.server.usage.UsageStatsService.LaunchTimeAlarmQueue getOrCreateLaunchTimeAlarmQueue(int userId) {
        com.android.server.usage.UsageStatsService.LaunchTimeAlarmQueue alarmQueue;
        synchronized (this.mLaunchTimeAlarmQueues) {
            alarmQueue = this.mLaunchTimeAlarmQueues.get(userId);
            if (alarmQueue == null) {
                alarmQueue = new com.android.server.usage.UsageStatsService.LaunchTimeAlarmQueue(userId, getContext(), this.mHandler.getLooper());
                this.mLaunchTimeAlarmQueues.put(userId, alarmQueue);
            }
        }
        return alarmQueue;
    }

    private long calculateEstimatedPackageLaunchTime(int userId, java.lang.String packageName) throws java.lang.Throwable {
        long endTime = java.lang.System.currentTimeMillis();
        long beginTime = endTime - 604800000;
        long unknownTime = endTime + 31536000000L;
        android.app.usage.UsageEvents events = queryEarliestEventsForPackage(userId, beginTime, endTime, packageName, 1);
        if (events == null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "No events for " + userId + ":" + packageName);
            }
            return unknownTime;
        }
        android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event();
        if (events.getNextEvent(event)) {
            boolean hasMoreThan24HoursOfHistory = endTime - event.getTimeStamp() > 86400000;
            if (DEBUG) {
                android.util.Slog.d(TAG, userId + ":" + packageName + " history > 24 hours=" + hasMoreThan24HoursOfHistory);
            }
            do {
                if (event.getEventType() == 1) {
                    long timestamp = event.getTimeStamp();
                    long nextLaunch = calculateNextLaunchTime(hasMoreThan24HoursOfHistory, timestamp);
                    if (nextLaunch > endTime) {
                        return nextLaunch;
                    }
                }
            } while (events.getNextEvent(event));
            return unknownTime;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, userId + ":" + packageName + " has no events");
        }
        return unknownTime;
    }

    private static long calculateNextLaunchTime(boolean hasMoreThan24HoursOfHistory, long eventTimestamp) {
        if (hasMoreThan24HoursOfHistory) {
            return 604800000 + eventTimestamp;
        }
        return 86400000 + eventTimestamp;
    }

    private void handleEstimatedLaunchTimesOnUserUnlock(int userId) {
        long beginTime;
        boolean hasHistory;
        long nowElapsed = android.os.SystemClock.elapsedRealtime();
        long now = java.lang.System.currentTimeMillis();
        long beginTime2 = now - 604800000;
        android.app.usage.UsageEvents events = queryEarliestAppEvents(userId, beginTime2, now, 1);
        if (events == null) {
            return;
        }
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> hasMoreThan24HoursOfHistory = new android.util.ArrayMap<>();
        android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event();
        boolean changedTimes = false;
        com.android.server.usage.UsageStatsService.LaunchTimeAlarmQueue alarmQueue = getOrCreateLaunchTimeAlarmQueue(userId);
        boolean unprocessedEvent = events.getNextEvent(event);
        while (unprocessedEvent) {
            java.lang.String packageName = event.getPackageName();
            if (!hasMoreThan24HoursOfHistory.containsKey(packageName)) {
                boolean hasHistory2 = now - event.getTimeStamp() > 86400000;
                if (DEBUG) {
                    hasHistory = hasHistory2;
                    android.util.Slog.d(TAG, userId + ":" + packageName + " history > 24 hours=" + hasHistory);
                } else {
                    hasHistory = hasHistory2;
                }
                hasMoreThan24HoursOfHistory.put(packageName, java.lang.Boolean.valueOf(hasHistory));
            }
            if (event.getEventType() != 1) {
                beginTime = beginTime2;
            } else {
                long estimatedLaunchTime = this.mAppStandby.getEstimatedLaunchTime(packageName, userId);
                if (estimatedLaunchTime < now || estimatedLaunchTime == Long.MAX_VALUE) {
                    beginTime = beginTime2;
                    long estimatedLaunchTime2 = calculateNextLaunchTime(hasMoreThan24HoursOfHistory.get(packageName).booleanValue(), event.getTimeStamp());
                    this.mAppStandby.setEstimatedLaunchTime(packageName, userId, estimatedLaunchTime2);
                    estimatedLaunchTime = estimatedLaunchTime2;
                } else {
                    beginTime = beginTime2;
                }
                if (estimatedLaunchTime < now + 604800000) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "User " + userId + " unlock resulting in estimated launch time change for " + packageName);
                    }
                    changedTimes |= stageChangedEstimatedLaunchTime(userId, packageName);
                }
                alarmQueue.addAlarm(packageName, (estimatedLaunchTime - now) + nowElapsed);
            }
            unprocessedEvent = events.getNextEvent(event);
            beginTime2 = beginTime;
        }
        if (changedTimes) {
            this.mHandler.sendEmptyMessage(9);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEstimatedLaunchTime(int userId, java.lang.String packageName, long estimatedLaunchTime) {
        long now = java.lang.System.currentTimeMillis();
        if (estimatedLaunchTime <= now) {
            if (DEBUG) {
                android.util.Slog.w(TAG, "Ignoring new estimate for " + userId + ":" + packageName + " because it's old");
                return;
            }
            return;
        }
        long oldEstimatedLaunchTime = this.mAppStandby.getEstimatedLaunchTime(packageName, userId);
        if (estimatedLaunchTime != oldEstimatedLaunchTime) {
            this.mAppStandby.setEstimatedLaunchTime(packageName, userId, estimatedLaunchTime);
            if (stageChangedEstimatedLaunchTime(userId, packageName)) {
                this.mHandler.sendEmptyMessage(9);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEstimatedLaunchTimes(int userId, java.util.List<android.app.usage.AppLaunchEstimateInfo> launchEstimates) {
        boolean changedTimes = false;
        long now = java.lang.System.currentTimeMillis();
        for (int i = launchEstimates.size() - 1; i >= 0; i--) {
            android.app.usage.AppLaunchEstimateInfo estimate = launchEstimates.get(i);
            if (estimate.estimatedLaunchTime <= now) {
                if (DEBUG) {
                    android.util.Slog.w(TAG, "Ignoring new estimate for " + userId + ":" + estimate.packageName + " because it's old");
                }
            } else {
                long oldEstimatedLaunchTime = this.mAppStandby.getEstimatedLaunchTime(estimate.packageName, userId);
                if (estimate.estimatedLaunchTime != oldEstimatedLaunchTime) {
                    this.mAppStandby.setEstimatedLaunchTime(estimate.packageName, userId, estimate.estimatedLaunchTime);
                    changedTimes |= stageChangedEstimatedLaunchTime(userId, estimate.packageName);
                }
            }
        }
        if (changedTimes) {
            this.mHandler.sendEmptyMessage(9);
        }
    }

    private boolean stageChangedEstimatedLaunchTime(int userId, java.lang.String packageName) {
        boolean zAdd;
        synchronized (this.mPendingLaunchTimeChangePackages) {
            zAdd = this.mPendingLaunchTimeChangePackages.add(userId, packageName);
        }
        return zAdd;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerListener(android.app.usage.UsageStatsManagerInternal.UsageEventListener listener) {
        synchronized (this.mUsageEventListeners) {
            this.mUsageEventListeners.add(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterListener(android.app.usage.UsageStatsManagerInternal.UsageEventListener listener) {
        synchronized (this.mUsageEventListeners) {
            this.mUsageEventListeners.remove(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerLaunchTimeChangedListener(android.app.usage.UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener listener) {
        this.mEstimatedLaunchTimeChangedListeners.add(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterLaunchTimeChangedListener(android.app.usage.UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener listener) {
        this.mEstimatedLaunchTimeChangedListeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldDeleteObsoleteData(android.os.UserHandle userHandle) {
        android.app.admin.DevicePolicyManagerInternal dpmInternal = getDpmInternal();
        return dpmInternal == null || dpmInternal.getProfileOwnerOrDeviceOwnerSupervisionComponent(userHandle) == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String buildFullToken(java.lang.String packageName, java.lang.String token) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(packageName.length() + token.length() + 1);
        sb.append(packageName);
        sb.append(TOKEN_DELIMITER);
        sb.append(token);
        return sb.toString();
    }

    private void flushToDiskLocked() {
        int userCount = this.mUserState.size();
        for (int i = 0; i < userCount; i++) {
            int userId = this.mUserState.keyAt(i);
            if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                persistPendingEventsLocked(userId);
            } else {
                com.android.server.usage.UserUsageStatsService service = this.mUserState.get(userId);
                if (service != null) {
                    service.persistActiveStats();
                }
            }
        }
        this.mHandler.removeMessages(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getTrimmedString(java.lang.String input) {
        if (input != null && input.length() > 127) {
            return input.substring(0, 127);
        }
        return input;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:228:? -> B:189:0x03ff). Please report as a decompilation issue!!! */
    void dump(java.lang.String[] args, java.io.PrintWriter pw) throws java.lang.Throwable {
        boolean compact;
        boolean checkin;
        boolean checkin2;
        com.android.internal.util.IndentingPrintWriter idpw = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
        java.util.ArrayList<java.lang.String> pkgs = new java.util.ArrayList<>();
        java.lang.String[] strArr = null;
        if (args == null) {
            compact = false;
            checkin = false;
        } else {
            int i = 0;
            boolean compact2 = false;
            checkin = false;
            while (i < args.length) {
                java.lang.String arg = args[i];
                if ("--checkin".equals(arg)) {
                    checkin = true;
                } else if ("-c".equals(arg)) {
                    compact2 = true;
                } else {
                    if ("flush".equals(arg)) {
                        synchronized (this.mLock) {
                            flushToDiskLocked();
                        }
                        this.mAppStandby.flushToDisk();
                        pw.println("Flushed stats to disk");
                        return;
                    }
                    if ("is-app-standby-enabled".equals(arg)) {
                        pw.println(this.mAppStandby.isAppIdleEnabled());
                        return;
                    }
                    if ("apptimelimit".equals(arg)) {
                        synchronized (this.mLock) {
                            if (i + 1 >= args.length) {
                                this.mAppTimeLimit.dump(strArr, pw);
                            } else {
                                java.lang.String[] remainingArgs = (java.lang.String[]) java.util.Arrays.copyOfRange(args, i + 1, args.length);
                                this.mAppTimeLimit.dump(remainingArgs, pw);
                            }
                        }
                        return;
                    }
                    if ("file".equals(arg)) {
                        com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
                        synchronized (this.mLock) {
                            if (i + 1 >= args.length) {
                                int numUsers = this.mUserState.size();
                                for (int user = 0; user < numUsers; user++) {
                                    int userId = this.mUserState.keyAt(user);
                                    if (this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                                        ipw.println("user=" + userId);
                                        ipw.increaseIndent();
                                        this.mUserState.valueAt(user).dumpFile(ipw, null);
                                        ipw.decreaseIndent();
                                    }
                                }
                            } else {
                                int user2 = parseUserIdFromArgs(args, i, ipw);
                                if (user2 != -10000) {
                                    java.lang.String[] remainingArgs2 = (java.lang.String[]) java.util.Arrays.copyOfRange(args, i + 2, args.length);
                                    this.mUserState.get(user2).dumpFile(ipw, remainingArgs2);
                                }
                            }
                        }
                        return;
                    }
                    if ("database-info".equals(arg)) {
                        com.android.internal.util.IndentingPrintWriter ipw2 = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
                        synchronized (this.mLock) {
                            if (i + 1 >= args.length) {
                                int numUsers2 = this.mUserState.size();
                                for (int user3 = 0; user3 < numUsers2; user3++) {
                                    int userId2 = this.mUserState.keyAt(user3);
                                    if (this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId2))) {
                                        ipw2.println("user=" + userId2);
                                        ipw2.increaseIndent();
                                        this.mUserState.valueAt(user3).dumpDatabaseInfo(ipw2);
                                        ipw2.decreaseIndent();
                                    }
                                }
                            } else {
                                int user4 = parseUserIdFromArgs(args, i, ipw2);
                                if (user4 != -10000) {
                                    this.mUserState.get(user4).dumpDatabaseInfo(ipw2);
                                }
                            }
                        }
                        return;
                    }
                    if ("appstandby".equals(arg)) {
                        pw.println("UsageStatsService Thread name = " + this.mHandler.getLooper().getThread().getName());
                        pw.println();
                        this.mAppStandby.dumpState(args, pw);
                        return;
                    }
                    if ("stats-directory".equals(arg)) {
                        com.android.internal.util.IndentingPrintWriter ipw3 = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
                        synchronized (this.mLock) {
                            int userId3 = parseUserIdFromArgs(args, i, ipw3);
                            if (userId3 != -10000) {
                                ipw3.println(new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId3), "usagestats").getAbsolutePath());
                            }
                        }
                        return;
                    }
                    if ("mappings".equals(arg)) {
                        com.android.internal.util.IndentingPrintWriter ipw4 = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
                        synchronized (this.mLock) {
                            int userId4 = parseUserIdFromArgs(args, i, ipw4);
                            if (userId4 != -10000) {
                                this.mUserState.get(userId4).dumpMappings(ipw4);
                            }
                        }
                        return;
                    }
                    if ("broadcast-response-stats".equals(arg)) {
                        synchronized (this.mLock) {
                            this.mResponseStatsTracker.dump(idpw);
                        }
                        return;
                    }
                    if ("app-component-usage".equals(arg)) {
                        com.android.internal.util.IndentingPrintWriter ipw5 = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
                        synchronized (this.mLock) {
                            try {
                                if (!this.mLastTimeComponentUsedGlobal.isEmpty()) {
                                    ipw5.println("App Component Usages:");
                                    ipw5.increaseIndent();
                                    for (java.lang.String pkg : this.mLastTimeComponentUsedGlobal.keySet()) {
                                        boolean compact3 = compact2;
                                        try {
                                            ipw5.println("package=" + pkg + " lastUsed=" + com.android.server.usage.UserUsageStatsService.formatDateTime(this.mLastTimeComponentUsedGlobal.get(pkg).longValue(), true));
                                            compact2 = compact3;
                                        } catch (java.lang.Throwable th) {
                                            th = th;
                                            throw th;
                                        }
                                    }
                                    ipw5.decreaseIndent();
                                }
                                return;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        }
                    } else {
                        boolean compact4 = compact2;
                        if (arg != null && !arg.startsWith("-")) {
                            pkgs.add(arg);
                        }
                        compact2 = compact4;
                    }
                }
                i++;
                strArr = null;
            }
            compact = compact2;
        }
        pw.println("Flags:");
        pw.println("    android.app.usage.user_interaction_type_api: " + android.app.usage.Flags.userInteractionTypeApi());
        pw.println("    android.app.usage.use_parceled_list: " + android.app.usage.Flags.useParceledList());
        pw.println("    android.app.usage.filter_based_event_query_api: " + android.app.usage.Flags.filterBasedEventQueryApi());
        pw.println("    android.app.usage.disable_idle_check: " + android.app.usage.Flags.disableIdleCheck());
        synchronized (this.mLock) {
            try {
                int userCount = this.mUserState.size();
                int[] userIds = new int[userCount];
                int i2 = 0;
                while (i2 < userCount) {
                    int userId5 = this.mUserState.keyAt(i2);
                    userIds[i2] = userId5;
                    idpw.printPair("user", java.lang.Integer.valueOf(userId5));
                    idpw.println();
                    idpw.increaseIndent();
                    if (this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId5))) {
                        if (checkin) {
                            try {
                                this.mUserState.valueAt(i2).checkin(idpw);
                                checkin2 = checkin;
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                                throw th;
                            }
                        } else {
                            this.mUserState.valueAt(i2).dump(idpw, pkgs, compact);
                            idpw.println();
                            checkin2 = checkin;
                        }
                    } else {
                        synchronized (this.mReportedEvents) {
                            try {
                                java.util.LinkedList<android.app.usage.UsageEvents.Event> pendingEvents = this.mReportedEvents.get(userId5);
                                if (pendingEvents == null || pendingEvents.isEmpty()) {
                                    checkin2 = checkin;
                                } else {
                                    int eventCount = pendingEvents.size();
                                    checkin2 = checkin;
                                    try {
                                        idpw.println("Pending events: count=" + eventCount);
                                        idpw.increaseIndent();
                                        int idx = 0;
                                        while (idx < eventCount) {
                                            com.android.server.usage.UserUsageStatsService.printEvent(idpw, pendingEvents.get(idx), true);
                                            idx++;
                                            pendingEvents = pendingEvents;
                                        }
                                        idpw.decreaseIndent();
                                        idpw.println();
                                    } catch (java.lang.Throwable th4) {
                                        th = th4;
                                        throw th;
                                    }
                                }
                            } catch (java.lang.Throwable th5) {
                                th = th5;
                                throw th;
                            }
                        }
                    }
                    try {
                        idpw.decreaseIndent();
                        i2++;
                        checkin = checkin2;
                    } catch (java.lang.Throwable th6) {
                        th = th6;
                        throw th;
                    }
                }
                idpw.println();
                idpw.printPair("Usage Source", android.app.usage.UsageStatsManager.usageSourceToString(this.mUsageSource));
                idpw.println();
                this.mAppTimeLimit.dump(null, pw);
                idpw.println();
                this.mResponseStatsTracker.dump(idpw);
                this.mAppStandby.dumpUsers(idpw, userIds, pkgs);
                if (com.android.internal.util.CollectionUtils.isEmpty(pkgs)) {
                    pw.println();
                    this.mAppStandby.dumpState(args, pw);
                }
            } catch (java.lang.Throwable th7) {
                th = th7;
            }
        }
    }

    private int parseUserIdFromArgs(java.lang.String[] args, int index, com.android.internal.util.IndentingPrintWriter ipw) {
        try {
            int userId = java.lang.Integer.parseInt(args[index + 1]);
            if (this.mUserState.indexOfKey(userId) < 0) {
                ipw.println("the specified user does not exist.");
                return -10000;
            }
            if (!this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                ipw.println("the specified user is currently in a locked state.");
                return -10000;
            }
            return userId;
        } catch (java.lang.ArrayIndexOutOfBoundsException | java.lang.NumberFormatException e) {
            ipw.println("invalid user specified.");
            return -10000;
        }
    }

    class H extends android.os.Handler {
        public H(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            int numUsers;
            int userId;
            switch (msg.what) {
                case 0:
                    com.android.server.usage.UsageStatsService.this.reportEvent((android.app.usage.UsageEvents.Event) msg.obj, msg.arg1);
                    return;
                case 1:
                    com.android.server.usage.UsageStatsService.this.flushToDisk();
                    return;
                case 2:
                    com.android.server.usage.UsageStatsService.this.onUserRemoved(msg.arg1);
                    return;
                case 3:
                case 8:
                default:
                    super.handleMessage(msg);
                    return;
                case 4:
                    com.android.server.usage.UsageStatsService.this.reportEventToAllUserId((android.app.usage.UsageEvents.Event) msg.obj);
                    return;
                case 5:
                    int userId2 = msg.arg1;
                    try {
                        try {
                            android.os.Trace.traceBegin(524288L, "usageStatsHandleUserUnlocked(" + userId2 + ")");
                            com.android.server.usage.UsageStatsService.this.onUserUnlocked(userId2);
                            break;
                        } catch (java.lang.Exception e) {
                            if (com.android.server.usage.UsageStatsService.this.mUserManager.isUserUnlocked(userId2)) {
                                throw e;
                            }
                            android.util.Slog.w(com.android.server.usage.UsageStatsService.TAG, "Attempted to unlock stopped or removed user " + msg.arg1);
                        }
                        return;
                    } finally {
                        android.os.Trace.traceEnd(524288L);
                    }
                case 6:
                    com.android.server.usage.UsageStatsService.this.onPackageRemoved(msg.arg1, (java.lang.String) msg.obj);
                    return;
                case 7:
                    synchronized (com.android.server.usage.UsageStatsService.this.mLock) {
                        com.android.server.usage.UsageStatsService.this.loadGlobalComponentUsageLocked();
                        break;
                    }
                    return;
                case 9:
                    removeMessages(9);
                    android.util.ArraySet<java.lang.String> pkgNames = new android.util.ArraySet<>();
                    synchronized (com.android.server.usage.UsageStatsService.this.mPendingLaunchTimeChangePackages) {
                        numUsers = com.android.server.usage.UsageStatsService.this.mPendingLaunchTimeChangePackages.size();
                        break;
                    }
                    for (int u = numUsers - 1; u >= 0; u--) {
                        pkgNames.clear();
                        synchronized (com.android.server.usage.UsageStatsService.this.mPendingLaunchTimeChangePackages) {
                            userId = com.android.server.usage.UsageStatsService.this.mPendingLaunchTimeChangePackages.keyAt(u);
                            pkgNames.addAll(com.android.server.usage.UsageStatsService.this.mPendingLaunchTimeChangePackages.get(userId));
                            com.android.server.usage.UsageStatsService.this.mPendingLaunchTimeChangePackages.remove(userId);
                            break;
                        }
                        if (com.android.server.usage.UsageStatsService.DEBUG) {
                            android.util.Slog.d(com.android.server.usage.UsageStatsService.TAG, "Notifying listeners for " + userId + "-->" + pkgNames);
                        }
                        for (int p = pkgNames.size() - 1; p >= 0; p--) {
                            java.lang.String pkgName = pkgNames.valueAt(p);
                            long nextEstimatedLaunchTime = com.android.server.usage.UsageStatsService.this.getEstimatedPackageLaunchTime(userId, pkgName);
                            for (android.app.usage.UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener listener : com.android.server.usage.UsageStatsService.this.mEstimatedLaunchTimeChangedListeners) {
                                listener.onEstimatedLaunchTimeChanged(userId, pkgName, nextEstimatedLaunchTime);
                            }
                        }
                    }
                    return;
                case 10:
                    com.android.server.usage.UsageStatsService.this.mResponseStatsTracker.onUidRemoved(msg.arg1);
                    return;
                case 11:
                    com.android.server.usage.UsageStatsService.this.mAppStandby.postCheckIdleStates(msg.arg1);
                    return;
            }
        }
    }

    void clearLastUsedTimestamps(java.lang.String packageName, int userId) {
        this.mAppStandby.clearLastUsedTimestampsForTest(packageName, userId);
    }

    void deletePackageData(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            this.mUserState.get(userId).deleteDataFor(packageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class BinderService extends android.app.usage.IUsageStatsManager.Stub {
        private BinderService() {
        }

        private boolean hasQueryPermission(java.lang.String callingPackage) {
            int callingUid = android.os.Binder.getCallingUid();
            if (callingUid == 1000) {
                return true;
            }
            int mode = com.android.server.usage.UsageStatsService.this.mAppOps.noteOp(43, callingUid, callingPackage);
            return mode == 3 ? com.android.server.usage.UsageStatsService.this.getContext().checkCallingPermission("android.permission.PACKAGE_USAGE_STATS") == 0 : mode == 0;
        }

        private boolean canReportUsageStats() {
            return isCallingUidSystem() || com.android.server.usage.UsageStatsService.this.getContext().checkCallingPermission("android.permission.REPORT_USAGE_STATS") == 0;
        }

        private boolean hasObserverPermission() {
            int callingUid = android.os.Binder.getCallingUid();
            android.app.admin.DevicePolicyManagerInternal dpmInternal = com.android.server.usage.UsageStatsService.this.getDpmInternal();
            return callingUid == 1000 || (dpmInternal != null && (dpmInternal.isActiveProfileOwner(callingUid) || dpmInternal.isActiveDeviceOwner(callingUid))) || com.android.server.usage.UsageStatsService.this.getContext().checkCallingPermission("android.permission.OBSERVE_APP_USAGE") == 0;
        }

        private boolean hasPermissions(java.lang.String... permissions) {
            int callingUid = android.os.Binder.getCallingUid();
            if (callingUid == 1000) {
                return true;
            }
            boolean hasPermissions = true;
            android.content.Context context = com.android.server.usage.UsageStatsService.this.getContext();
            for (java.lang.String str : permissions) {
                hasPermissions = hasPermissions && context.checkCallingPermission(str) == 0;
            }
            return hasPermissions;
        }

        private void checkCallerIsSystemOrSameApp(java.lang.String pkg) {
            if (isCallingUidSystem()) {
                return;
            }
            checkCallerIsSameApp(pkg);
        }

        private void checkCallerIsSameApp(java.lang.String pkg) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingUserId = android.os.UserHandle.getUserId(callingUid);
            if (com.android.server.usage.UsageStatsService.this.mPackageManagerInternal.getPackageUid(pkg, 0L, callingUserId) != callingUid) {
                throw new java.lang.SecurityException("Calling uid " + callingUid + " cannot query eventsfor package " + pkg);
            }
        }

        private boolean isCallingUidSystem() {
            int uid = android.os.UserHandle.getAppId(android.os.Binder.getCallingUid());
            return uid == 1000;
        }

        private android.app.usage.UsageEvents queryEventsHelper(int userId, long beginTime, long endTime, java.lang.String callingPackage, int[] eventTypeFilter, android.util.ArraySet<java.lang.String> pkgNameFilter) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            boolean obfuscateInstantApps = com.android.server.usage.UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, android.os.UserHandle.getCallingUserId());
            long token = android.os.Binder.clearCallingIdentity();
            try {
                boolean hideShortcutInvocationEvents = com.android.server.usage.UsageStatsService.this.shouldHideShortcutInvocationEvents(userId, callingPackage, callingPid, callingUid);
                boolean hideLocusIdEvents = com.android.server.usage.UsageStatsService.this.shouldHideLocusIdEvents(callingPid, callingUid);
                boolean obfuscateNotificationEvents = com.android.server.usage.UsageStatsService.this.shouldObfuscateNotificationEvents(callingPid, callingUid);
                int flags = obfuscateInstantApps ? 0 | 1 : 0;
                if (hideShortcutInvocationEvents) {
                    flags |= 2;
                }
                if (hideLocusIdEvents) {
                    flags |= 8;
                }
                if (obfuscateNotificationEvents) {
                    flags |= 4;
                }
                return com.android.server.usage.UsageStatsService.this.queryEventsWithQueryFilters(userId, beginTime, endTime, flags, eventTypeFilter, pkgNameFilter);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        private void reportUserInteractionInnerHelper(java.lang.String packageName, int userId, android.os.PersistableBundle extras) {
            if (android.app.usage.Flags.reportUsageStatsPermission()) {
                if (!canReportUsageStats()) {
                    throw new java.lang.SecurityException("Only the system or holders of the REPORT_USAGE_STATS permission are allowed to call reportUserInteraction");
                }
                if (userId != android.os.UserHandle.getCallingUserId()) {
                    com.android.server.usage.UsageStatsService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "Caller doesn't have INTERACT_ACROSS_USERS_FULL permission");
                }
            } else if (!isCallingUidSystem()) {
                throw new java.lang.SecurityException("Only system is allowed to call reportUserInteraction");
            }
            if (com.android.server.usage.UsageStatsService.this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId) < 0) {
                throw new java.lang.IllegalArgumentException("Package " + packageName + " does not exist!");
            }
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(7, android.os.SystemClock.elapsedRealtime());
            event.mPackage = packageName;
            event.mExtras = extras;
            com.android.server.usage.UsageStatsService.this.reportEventOrAddToQueue(userId, event);
        }

        public android.content.pm.ParceledListSlice<android.app.usage.UsageStats> queryUsageStats(int bucketType, long beginTime, long endTime, java.lang.String callingPackage, int userId) {
            if (!hasQueryPermission(callingPackage)) {
                return null;
            }
            int callingUid = android.os.Binder.getCallingUid();
            int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, true, "queryUsageStats", callingPackage);
            boolean obfuscateInstantApps = com.android.server.usage.UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, android.os.UserHandle.getCallingUserId());
            long token = android.os.Binder.clearCallingIdentity();
            try {
                java.util.List<android.app.usage.UsageStats> results = com.android.server.usage.UsageStatsService.this.queryUsageStats(userId2, bucketType, beginTime, endTime, obfuscateInstantApps);
                if (results != null) {
                    return new android.content.pm.ParceledListSlice<>(results);
                }
                return null;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.content.pm.ParceledListSlice<android.app.usage.ConfigurationStats> queryConfigurationStats(int bucketType, long beginTime, long endTime, java.lang.String callingPackage) throws android.os.RemoteException {
            if (!hasQueryPermission(callingPackage)) {
                return null;
            }
            int userId = android.os.UserHandle.getCallingUserId();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                java.util.List<android.app.usage.ConfigurationStats> results = com.android.server.usage.UsageStatsService.this.queryConfigurationStats(userId, bucketType, beginTime, endTime);
                if (results != null) {
                    return new android.content.pm.ParceledListSlice<>(results);
                }
                return null;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.content.pm.ParceledListSlice<android.app.usage.EventStats> queryEventStats(int bucketType, long beginTime, long endTime, java.lang.String callingPackage) throws android.os.RemoteException {
            if (!hasQueryPermission(callingPackage)) {
                return null;
            }
            int userId = android.os.UserHandle.getCallingUserId();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                java.util.List<android.app.usage.EventStats> results = com.android.server.usage.UsageStatsService.this.queryEventStats(userId, bucketType, beginTime, endTime);
                if (results != null) {
                    return new android.content.pm.ParceledListSlice<>(results);
                }
                return null;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.app.usage.UsageEvents queryEvents(long beginTime, long endTime, java.lang.String callingPackage) {
            if (!hasQueryPermission(callingPackage)) {
                return null;
            }
            if (android.os.Build.IS_AGING_VERSION) {
                android.util.Slog.e(com.android.server.usage.UsageStatsService.TAG, "skip queryEvent for aging test");
                return null;
            }
            return queryEventsHelper(android.os.UserHandle.getCallingUserId(), beginTime, endTime, callingPackage, libcore.util.EmptyArray.INT, null);
        }

        public android.app.usage.UsageEvents queryEventsWithFilter(android.app.usage.UsageEventsQuery query, java.lang.String callingPackage) {
            java.util.Objects.requireNonNull(query);
            java.util.Objects.requireNonNull(callingPackage);
            if (!hasQueryPermission(callingPackage)) {
                return null;
            }
            int callingUserId = android.os.UserHandle.getCallingUserId();
            int userId = query.getUserId();
            if (userId == -10000) {
                userId = callingUserId;
            }
            if (userId != callingUserId) {
                com.android.server.usage.UsageStatsService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "No permission to query usage stats for user " + userId);
            }
            return queryEventsHelper(userId, query.getBeginTimeMillis(), query.getEndTimeMillis(), callingPackage, query.getEventTypes(), new android.util.ArraySet<>(query.getPackageNames()));
        }

        public android.app.usage.UsageEvents queryEventsForPackage(long beginTime, long endTime, java.lang.String callingPackage) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingUserId = android.os.UserHandle.getUserId(callingUid);
            checkCallerIsSameApp(callingPackage);
            boolean includeTaskRoot = hasQueryPermission(callingPackage);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.usage.UsageStatsService.this.queryEventsForPackage(callingUserId, beginTime, endTime, callingPackage, includeTaskRoot);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.app.usage.UsageEvents queryEventsForUser(long beginTime, long endTime, int userId, java.lang.String callingPackage) {
            if (!hasQueryPermission(callingPackage)) {
                return null;
            }
            int callingUserId = android.os.UserHandle.getCallingUserId();
            if (userId != callingUserId) {
                com.android.server.usage.UsageStatsService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "No permission to query usage stats for this user");
            }
            return queryEventsHelper(userId, beginTime, endTime, callingPackage, libcore.util.EmptyArray.INT, null);
        }

        public android.app.usage.UsageEvents queryEventsForPackageForUser(long beginTime, long endTime, int userId, java.lang.String pkg, java.lang.String callingPackage) {
            if (!hasQueryPermission(callingPackage)) {
                return null;
            }
            if (userId != android.os.UserHandle.getCallingUserId()) {
                com.android.server.usage.UsageStatsService.this.getContext().enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "No permission to query usage stats for this user");
            }
            checkCallerIsSystemOrSameApp(pkg);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.usage.UsageStatsService.this.queryEventsForPackage(userId, beginTime, endTime, pkg, true);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean isAppStandbyEnabled() {
            return com.android.server.usage.UsageStatsService.this.mAppStandby.isAppIdleEnabled();
        }

        public boolean isAppInactive(java.lang.String packageName, int userId, java.lang.String callingPackage) {
            int callingUid = android.os.Binder.getCallingUid();
            try {
                int userId2 = android.app.ActivityManager.getService().handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, false, "isAppInactive", (java.lang.String) null);
                if (packageName.equals(callingPackage)) {
                    int actualCallingUid = com.android.server.usage.UsageStatsService.this.mPackageManagerInternal.getPackageUid(callingPackage, 0L, userId2);
                    if (actualCallingUid != callingUid) {
                        return false;
                    }
                } else if (!hasQueryPermission(callingPackage)) {
                    return false;
                }
                boolean obfuscateInstantApps = com.android.server.usage.UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, userId2);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    return com.android.server.usage.UsageStatsService.this.mAppStandby.isAppIdleFiltered(packageName, userId2, android.os.SystemClock.elapsedRealtime(), obfuscateInstantApps);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            } catch (android.os.RemoteException re) {
                throw re.rethrowFromSystemServer();
            }
        }

        public void setAppInactive(java.lang.String packageName, boolean idle, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            try {
                int userId2 = android.app.ActivityManager.getService().handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, true, "setAppInactive", (java.lang.String) null);
                com.android.server.usage.UsageStatsService.this.getContext().enforceCallingPermission("android.permission.CHANGE_APP_IDLE_STATE", "No permission to change app idle state");
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    int appId = com.android.server.usage.UsageStatsService.this.mAppStandby.getAppId(packageName);
                    if (appId < 0) {
                        return;
                    }
                    com.android.server.usage.UsageStatsService.this.mAppStandby.setAppIdleAsync(packageName, idle, userId2);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            } catch (android.os.RemoteException re) {
                throw re.rethrowFromSystemServer();
            }
        }

        public int getAppStandbyBucket(java.lang.String packageName, java.lang.String callingPackage, int userId) throws java.lang.Throwable {
            int callingUid = android.os.Binder.getCallingUid();
            try {
                int userId2 = android.app.ActivityManager.getService().handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, false, "getAppStandbyBucket", (java.lang.String) null);
                int packageUid = com.android.server.usage.UsageStatsService.this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId2);
                boolean sameApp = packageUid == callingUid;
                if (!sameApp && !hasQueryPermission(callingPackage)) {
                    throw new java.lang.SecurityException("Don't have permission to query app standby bucket");
                }
                boolean isInstantApp = com.android.server.usage.UsageStatsService.this.isInstantApp(packageName, userId2);
                boolean cannotAccessInstantApps = com.android.server.usage.UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, userId2);
                if (packageUid < 0 || (!sameApp && isInstantApp && cannotAccessInstantApps)) {
                    throw new java.lang.IllegalArgumentException("Cannot get standby bucket for non existent package (" + packageName + ")");
                }
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    try {
                        int appStandbyBucket = com.android.server.usage.UsageStatsService.this.mAppStandby.getAppStandbyBucket(packageName, userId2, android.os.SystemClock.elapsedRealtime(), false);
                        android.os.Binder.restoreCallingIdentity(token);
                        return appStandbyBucket;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(token);
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (android.os.RemoteException re) {
                throw re.rethrowFromSystemServer();
            }
        }

        public void setAppStandbyBucket(java.lang.String packageName, int bucket, int userId) {
            super.setAppStandbyBucket_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.usage.UsageStatsService.this.mAppStandby.setAppStandbyBucket(packageName, bucket, userId, callingUid, callingPid);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.content.pm.ParceledListSlice<android.app.usage.AppStandbyInfo> getAppStandbyBuckets(java.lang.String callingPackageName, int userId) {
            final int callingUid = android.os.Binder.getCallingUid();
            try {
                final int userId2 = android.app.ActivityManager.getService().handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, false, "getAppStandbyBucket", (java.lang.String) null);
                if (!hasQueryPermission(callingPackageName)) {
                    throw new java.lang.SecurityException("Don't have permission to query app standby bucket");
                }
                final boolean cannotAccessInstantApps = com.android.server.usage.UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, userId2);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    java.util.List<android.app.usage.AppStandbyInfo> standbyBucketList = com.android.server.usage.UsageStatsService.this.mAppStandby.getAppStandbyBuckets(userId2);
                    if (standbyBucketList == null) {
                        return android.content.pm.ParceledListSlice.emptyList();
                    }
                    standbyBucketList.removeIf(new java.util.function.Predicate() { // from class: com.android.server.usage.UsageStatsService$BinderService$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return this.f$0.lambda$getAppStandbyBuckets$0(callingUid, userId2, cannotAccessInstantApps, (android.app.usage.AppStandbyInfo) obj);
                        }
                    });
                    return new android.content.pm.ParceledListSlice<>(standbyBucketList);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            } catch (android.os.RemoteException re) {
                throw re.rethrowFromSystemServer();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$getAppStandbyBuckets$0(int callingUid, int targetUserId, boolean cannotAccessInstantApps, android.app.usage.AppStandbyInfo i) {
            return !com.android.server.usage.UsageStatsService.this.sameApp(callingUid, targetUserId, i.mPackageName) && com.android.server.usage.UsageStatsService.this.isInstantApp(i.mPackageName, targetUserId) && cannotAccessInstantApps;
        }

        public void setAppStandbyBuckets(android.content.pm.ParceledListSlice appBuckets, int userId) {
            super.setAppStandbyBuckets_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.usage.UsageStatsService.this.mAppStandby.setAppStandbyBuckets(appBuckets.getList(), userId, callingUid, callingPid);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int getAppMinStandbyBucket(java.lang.String packageName, java.lang.String callingPackage, int userId) {
            int callingUid = android.os.Binder.getCallingUid();
            try {
                int userId2 = android.app.ActivityManager.getService().handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, false, "getAppStandbyBucket", (java.lang.String) null);
                int packageUid = com.android.server.usage.UsageStatsService.this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId2);
                if (packageUid != callingUid && !hasQueryPermission(callingPackage)) {
                    throw new java.lang.SecurityException("Don't have permission to query min app standby bucket");
                }
                boolean isInstantApp = com.android.server.usage.UsageStatsService.this.isInstantApp(packageName, userId2);
                boolean cannotAccessInstantApps = com.android.server.usage.UsageStatsService.this.shouldObfuscateInstantAppsForCaller(callingUid, userId2);
                if (packageUid < 0 || (isInstantApp && cannotAccessInstantApps)) {
                    throw new java.lang.IllegalArgumentException("Cannot get min standby bucket for non existent package (" + packageName + ")");
                }
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    return com.android.server.usage.UsageStatsService.this.mAppStandby.getAppMinStandbyBucket(packageName, android.os.UserHandle.getAppId(packageUid), userId2, false);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            } catch (android.os.RemoteException re) {
                throw re.rethrowFromSystemServer();
            }
        }

        public void setEstimatedLaunchTime(java.lang.String packageName, long estimatedLaunchTime, int userId) {
            super.setEstimatedLaunchTime_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.usage.UsageStatsService.this.setEstimatedLaunchTime(userId, packageName, estimatedLaunchTime);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setEstimatedLaunchTimes(android.content.pm.ParceledListSlice estimatedLaunchTimes, int userId) {
            super.setEstimatedLaunchTimes_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.usage.UsageStatsService.this.setEstimatedLaunchTimes(userId, estimatedLaunchTimes.getList());
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void onCarrierPrivilegedAppsChanged() {
            if (com.android.server.usage.UsageStatsService.DEBUG) {
                android.util.Slog.i(com.android.server.usage.UsageStatsService.TAG, "Carrier privileged apps changed");
            }
            com.android.server.usage.UsageStatsService.this.getContext().enforceCallingOrSelfPermission("android.permission.BIND_CARRIER_SERVICES", "onCarrierPrivilegedAppsChanged can only be called by privileged apps.");
            com.android.server.usage.UsageStatsService.this.mAppStandby.clearCarrierPrivilegedApps();
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) throws java.lang.Throwable {
            if (com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(com.android.server.usage.UsageStatsService.this.getContext(), com.android.server.usage.UsageStatsService.TAG, pw)) {
                com.android.server.usage.UsageStatsService.this.dump(args, pw);
            }
        }

        public void reportChooserSelection(java.lang.String packageName, int userId, java.lang.String contentType, java.lang.String[] annotations, java.lang.String action) {
            if (packageName == null) {
                throw new java.lang.IllegalArgumentException("Package selection must not be null.");
            }
            if (contentType == null || contentType.isBlank() || action == null || action.isBlank()) {
                return;
            }
            if (android.app.usage.Flags.reportUsageStatsPermission() && !canReportUsageStats()) {
                throw new java.lang.SecurityException("Only the system or holders of the REPORT_USAGE_STATS permission are allowed to call reportChooserSelection");
            }
            if (com.android.server.usage.UsageStatsService.this.mPackageManagerInternal.getPackageUid(packageName, 0L, userId) < 0) {
                android.util.Slog.w(com.android.server.usage.UsageStatsService.TAG, "Event report user selecting an invalid package");
                return;
            }
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(9, android.os.SystemClock.elapsedRealtime());
            event.mPackage = packageName;
            event.mAction = action;
            event.mContentType = contentType;
            event.mContentAnnotations = annotations;
            com.android.server.usage.UsageStatsService.this.reportEventOrAddToQueue(userId, event);
        }

        public void reportUserInteraction(java.lang.String packageName, int userId) {
            reportUserInteractionInnerHelper(packageName, userId, null);
        }

        public void reportUserInteractionWithBundle(java.lang.String packageName, int userId, android.os.PersistableBundle extras) {
            java.util.Objects.requireNonNull(packageName);
            if (extras == null || extras.size() == 0) {
                throw new java.lang.IllegalArgumentException("Emtry extras!");
            }
            java.lang.String category = extras.getString("android.app.usage.extra.EVENT_CATEGORY");
            if (android.text.TextUtils.isEmpty(category)) {
                throw new java.lang.IllegalArgumentException("Empty android.app.usage.extra.EVENT_CATEGORY");
            }
            java.lang.String action = extras.getString("android.app.usage.extra.EVENT_ACTION");
            if (android.text.TextUtils.isEmpty(action)) {
                throw new java.lang.IllegalArgumentException("Empty android.app.usage.extra.EVENT_ACTION");
            }
            android.os.PersistableBundle extrasCopy = new android.os.PersistableBundle();
            extrasCopy.putString("android.app.usage.extra.EVENT_CATEGORY", com.android.server.usage.UsageStatsService.this.getTrimmedString(category));
            extrasCopy.putString("android.app.usage.extra.EVENT_ACTION", com.android.server.usage.UsageStatsService.this.getTrimmedString(action));
            reportUserInteractionInnerHelper(packageName, userId, extrasCopy);
        }

        public void registerAppUsageObserver(int observerId, java.lang.String[] packages, long timeLimitMs, android.app.PendingIntent callbackIntent, java.lang.String callingPackage) {
            if (!hasObserverPermission()) {
                throw new java.lang.SecurityException("Caller doesn't have OBSERVE_APP_USAGE permission");
            }
            if (packages == null || packages.length == 0) {
                throw new java.lang.IllegalArgumentException("Must specify at least one package");
            }
            if (callbackIntent == null) {
                throw new java.lang.NullPointerException("callbackIntent can't be null");
            }
            int callingUid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserId(callingUid);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.usage.UsageStatsService.this.registerAppUsageObserver(callingUid, observerId, packages, timeLimitMs, callbackIntent, userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void unregisterAppUsageObserver(int observerId, java.lang.String callingPackage) {
            if (!hasObserverPermission()) {
                throw new java.lang.SecurityException("Caller doesn't have OBSERVE_APP_USAGE permission");
            }
            int callingUid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserId(callingUid);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.usage.UsageStatsService.this.unregisterAppUsageObserver(callingUid, observerId, userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void registerUsageSessionObserver(int sessionObserverId, java.lang.String[] observed, long timeLimitMs, long sessionThresholdTimeMs, android.app.PendingIntent limitReachedCallbackIntent, android.app.PendingIntent sessionEndCallbackIntent, java.lang.String callingPackage) {
            if (!hasObserverPermission()) {
                throw new java.lang.SecurityException("Caller doesn't have OBSERVE_APP_USAGE permission");
            }
            if (observed == null || observed.length == 0) {
                throw new java.lang.IllegalArgumentException("Must specify at least one observed entity");
            }
            if (limitReachedCallbackIntent == null) {
                throw new java.lang.NullPointerException("limitReachedCallbackIntent can't be null");
            }
            int callingUid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserId(callingUid);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.usage.UsageStatsService.this.registerUsageSessionObserver(callingUid, sessionObserverId, observed, timeLimitMs, sessionThresholdTimeMs, limitReachedCallbackIntent, sessionEndCallbackIntent, userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void unregisterUsageSessionObserver(int sessionObserverId, java.lang.String callingPackage) {
            if (!hasObserverPermission()) {
                throw new java.lang.SecurityException("Caller doesn't have OBSERVE_APP_USAGE permission");
            }
            int callingUid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserId(callingUid);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.usage.UsageStatsService.this.unregisterUsageSessionObserver(callingUid, sessionObserverId, userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void registerAppUsageLimitObserver(int observerId, java.lang.String[] packages, long timeLimitMs, long timeUsedMs, android.app.PendingIntent callbackIntent, java.lang.String callingPackage) {
            int callingUid = android.os.Binder.getCallingUid();
            android.app.admin.DevicePolicyManagerInternal dpmInternal = com.android.server.usage.UsageStatsService.this.getDpmInternal();
            if (!hasPermissions("android.permission.SUSPEND_APPS", "android.permission.OBSERVE_APP_USAGE") && (dpmInternal == null || !dpmInternal.isActiveSupervisionApp(callingUid))) {
                throw new java.lang.SecurityException("Caller must be the active supervision app or it must have both SUSPEND_APPS and OBSERVE_APP_USAGE permissions");
            }
            if (packages == null || packages.length == 0) {
                throw new java.lang.IllegalArgumentException("Must specify at least one package");
            }
            if (callbackIntent == null && timeUsedMs < timeLimitMs) {
                throw new java.lang.NullPointerException("callbackIntent can't be null");
            }
            int userId = android.os.UserHandle.getUserId(callingUid);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.usage.UsageStatsService.this.registerAppUsageLimitObserver(callingUid, observerId, packages, timeLimitMs, timeUsedMs, callbackIntent, userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void unregisterAppUsageLimitObserver(int observerId, java.lang.String callingPackage) {
            int callingUid = android.os.Binder.getCallingUid();
            android.app.admin.DevicePolicyManagerInternal dpmInternal = com.android.server.usage.UsageStatsService.this.getDpmInternal();
            if (!hasPermissions("android.permission.SUSPEND_APPS", "android.permission.OBSERVE_APP_USAGE") && (dpmInternal == null || !dpmInternal.isActiveSupervisionApp(callingUid))) {
                throw new java.lang.SecurityException("Caller must be the active supervision app or it must have both SUSPEND_APPS and OBSERVE_APP_USAGE permissions");
            }
            int userId = android.os.UserHandle.getUserId(callingUid);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.usage.UsageStatsService.this.unregisterAppUsageLimitObserver(callingUid, observerId, userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void reportUsageStart(android.os.IBinder activity, java.lang.String token, java.lang.String callingPackage) {
            reportPastUsageStart(activity, token, 0L, callingPackage);
        }

        public void reportPastUsageStart(android.os.IBinder activity, java.lang.String token, long timeAgoMs, java.lang.String callingPackage) {
            android.util.ArraySet<java.lang.String> tokens;
            int callingUid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserId(callingUid);
            long binderToken = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.usage.UsageStatsService.this.mUsageReporters) {
                    tokens = com.android.server.usage.UsageStatsService.this.mUsageReporters.get(activity.hashCode());
                    if (tokens == null) {
                        tokens = new android.util.ArraySet<>();
                        com.android.server.usage.UsageStatsService.this.mUsageReporters.put(activity.hashCode(), tokens);
                    }
                }
                synchronized (tokens) {
                    if (!tokens.add(token)) {
                        throw new java.lang.IllegalArgumentException(token + " for " + callingPackage + " is already reported as started for this activity");
                    }
                }
                com.android.server.usage.UsageStatsService.this.mAppTimeLimit.noteUsageStart(com.android.server.usage.UsageStatsService.this.buildFullToken(callingPackage, token), userId, timeAgoMs);
            } finally {
                android.os.Binder.restoreCallingIdentity(binderToken);
            }
        }

        public void reportUsageStop(android.os.IBinder activity, java.lang.String token, java.lang.String callingPackage) {
            android.util.ArraySet<java.lang.String> tokens;
            int callingUid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserId(callingUid);
            long binderToken = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.usage.UsageStatsService.this.mUsageReporters) {
                    tokens = com.android.server.usage.UsageStatsService.this.mUsageReporters.get(activity.hashCode());
                    if (tokens == null) {
                        throw new java.lang.IllegalArgumentException("Unknown reporter trying to stop token " + token + " for " + callingPackage);
                    }
                }
                synchronized (tokens) {
                    if (!tokens.remove(token)) {
                        throw new java.lang.IllegalArgumentException(token + " for " + callingPackage + " is already reported as stopped for this activity");
                    }
                }
                com.android.server.usage.UsageStatsService.this.mAppTimeLimit.noteUsageStop(com.android.server.usage.UsageStatsService.this.buildFullToken(callingPackage, token), userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(binderToken);
            }
        }

        public int getUsageSource() {
            int i;
            if (!hasObserverPermission()) {
                throw new java.lang.SecurityException("Caller doesn't have OBSERVE_APP_USAGE permission");
            }
            synchronized (com.android.server.usage.UsageStatsService.this.mLock) {
                i = com.android.server.usage.UsageStatsService.this.mUsageSource;
            }
            return i;
        }

        public void forceUsageSourceSettingRead() {
            com.android.server.usage.UsageStatsService.this.readUsageSourceSetting();
        }

        public long getLastTimeAnyComponentUsed(java.lang.String packageName, java.lang.String callingPackage) {
            long jLongValue;
            if (!hasPermissions("android.permission.INTERACT_ACROSS_USERS")) {
                throw new java.lang.SecurityException("Caller doesn't have INTERACT_ACROSS_USERS permission");
            }
            if (!hasQueryPermission(callingPackage)) {
                throw new java.lang.SecurityException("Don't have permission to query usage stats");
            }
            synchronized (com.android.server.usage.UsageStatsService.this.mLock) {
                jLongValue = (((java.lang.Long) com.android.server.usage.UsageStatsService.this.mLastTimeComponentUsedGlobal.getOrDefault(packageName, 0L)).longValue() / java.util.concurrent.TimeUnit.DAYS.toMillis(1L)) * java.util.concurrent.TimeUnit.DAYS.toMillis(1L);
            }
            return jLongValue;
        }

        public android.app.usage.BroadcastResponseStatsList queryBroadcastResponseStats(java.lang.String packageName, long id, java.lang.String callingPackage, int userId) {
            java.util.Objects.requireNonNull(callingPackage);
            if (id < 0) {
                throw new java.lang.IllegalArgumentException("id needs to be >=0");
            }
            com.android.server.usage.UsageStatsService.this.getContext().enforceCallingOrSelfPermission("android.permission.ACCESS_BROADCAST_RESPONSE_STATS", "queryBroadcastResponseStats");
            int callingUid = android.os.Binder.getCallingUid();
            return new android.app.usage.BroadcastResponseStatsList(com.android.server.usage.UsageStatsService.this.mResponseStatsTracker.queryBroadcastResponseStats(callingUid, packageName, id, android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, false, "queryBroadcastResponseStats", callingPackage)));
        }

        public void clearBroadcastResponseStats(java.lang.String packageName, long id, java.lang.String callingPackage, int userId) {
            java.util.Objects.requireNonNull(callingPackage);
            if (id < 0) {
                throw new java.lang.IllegalArgumentException("id needs to be >=0");
            }
            com.android.server.usage.UsageStatsService.this.getContext().enforceCallingOrSelfPermission("android.permission.ACCESS_BROADCAST_RESPONSE_STATS", "clearBroadcastResponseStats");
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.usage.UsageStatsService.this.mResponseStatsTracker.clearBroadcastResponseStats(callingUid, packageName, id, android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, false, "clearBroadcastResponseStats", callingPackage));
        }

        public void clearBroadcastEvents(java.lang.String callingPackage, int userId) {
            java.util.Objects.requireNonNull(callingPackage);
            com.android.server.usage.UsageStatsService.this.getContext().enforceCallingOrSelfPermission("android.permission.ACCESS_BROADCAST_RESPONSE_STATS", "clearBroadcastEvents");
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.usage.UsageStatsService.this.mResponseStatsTracker.clearBroadcastEvents(callingUid, android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, false, "clearBroadcastResponseStats", callingPackage));
        }

        public boolean isPackageExemptedFromBroadcastResponseStats(java.lang.String callingPackage, int userId) {
            java.util.Objects.requireNonNull(callingPackage);
            com.android.server.usage.UsageStatsService.this.getContext().enforceCallingOrSelfPermission("android.permission.DUMP", "isPackageExemptedFromBroadcastResponseStats");
            return com.android.server.usage.UsageStatsService.this.mResponseStatsTracker.isPackageExemptedFromBroadcastResponseStats(callingPackage, android.os.UserHandle.of(userId));
        }

        public java.lang.String getAppStandbyConstant(java.lang.String key) {
            java.util.Objects.requireNonNull(key);
            if (!hasPermissions("android.permission.READ_DEVICE_CONFIG")) {
                throw new java.lang.SecurityException("Caller doesn't have READ_DEVICE_CONFIG permission");
            }
            return com.android.server.usage.UsageStatsService.this.mAppStandby.getAppStandbyConstant(key);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
            return new com.android.server.usage.UsageStatsShellCommand(com.android.server.usage.UsageStatsService.this).exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
        }
    }

    void registerAppUsageObserver(int callingUid, int observerId, java.lang.String[] packages, long timeLimitMs, android.app.PendingIntent callbackIntent, int userId) throws java.lang.Throwable {
        this.mAppTimeLimit.addAppUsageObserver(callingUid, observerId, packages, timeLimitMs, callbackIntent, userId);
    }

    void unregisterAppUsageObserver(int callingUid, int observerId, int userId) {
        this.mAppTimeLimit.removeAppUsageObserver(callingUid, observerId, userId);
    }

    void registerUsageSessionObserver(int callingUid, int observerId, java.lang.String[] observed, long timeLimitMs, long sessionThresholdTime, android.app.PendingIntent limitReachedCallbackIntent, android.app.PendingIntent sessionEndCallbackIntent, int userId) throws java.lang.Throwable {
        this.mAppTimeLimit.addUsageSessionObserver(callingUid, observerId, observed, timeLimitMs, sessionThresholdTime, limitReachedCallbackIntent, sessionEndCallbackIntent, userId);
    }

    void unregisterUsageSessionObserver(int callingUid, int sessionObserverId, int userId) {
        this.mAppTimeLimit.removeUsageSessionObserver(callingUid, sessionObserverId, userId);
    }

    void registerAppUsageLimitObserver(int callingUid, int observerId, java.lang.String[] packages, long timeLimitMs, long timeUsedMs, android.app.PendingIntent callbackIntent, int userId) throws java.lang.Throwable {
        this.mAppTimeLimit.addAppUsageLimitObserver(callingUid, observerId, packages, timeLimitMs, timeUsedMs, callbackIntent, userId);
    }

    void unregisterAppUsageLimitObserver(int callingUid, int observerId, int userId) {
        this.mAppTimeLimit.removeAppUsageLimitObserver(callingUid, observerId, userId);
    }

    private final class LocalService extends android.app.usage.UsageStatsManagerInternal {
        private LocalService() {
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportEvent(android.content.ComponentName component, int userId, int eventType, int instanceId, android.content.ComponentName taskRoot) {
            if (component == null) {
                android.util.Slog.w(com.android.server.usage.UsageStatsService.TAG, "Event reported without a component name");
                return;
            }
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(eventType, android.os.SystemClock.elapsedRealtime());
            event.mPackage = component.getPackageName();
            event.mClass = component.getClassName();
            event.mInstanceId = instanceId;
            if (taskRoot == null) {
                event.mTaskRootPackage = null;
                event.mTaskRootClass = null;
            } else {
                event.mTaskRootPackage = taskRoot.getPackageName();
                event.mTaskRootClass = taskRoot.getClassName();
            }
            com.android.server.usage.UsageStatsService.this.reportEventOrAddToQueue(userId, event);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportEvent(java.lang.String packageName, int userId, int eventType) {
            if (packageName == null) {
                android.util.Slog.w(com.android.server.usage.UsageStatsService.TAG, "Event reported without a package name, eventType:" + eventType);
                return;
            }
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(eventType, android.os.SystemClock.elapsedRealtime());
            event.mPackage = packageName;
            com.android.server.usage.UsageStatsService.this.reportEventOrAddToQueue(userId, event);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportConfigurationChange(android.content.res.Configuration config, int userId) {
            if (config == null) {
                android.util.Slog.w(com.android.server.usage.UsageStatsService.TAG, "Configuration event reported with a null config");
                return;
            }
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(5, android.os.SystemClock.elapsedRealtime());
            event.mPackage = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME;
            event.mConfiguration = new android.content.res.Configuration(config);
            com.android.server.usage.UsageStatsService.this.reportEventOrAddToQueue(userId, event);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportInterruptiveNotification(java.lang.String packageName, java.lang.String channelId, int userId) {
            if (packageName == null || channelId == null) {
                android.util.Slog.w(com.android.server.usage.UsageStatsService.TAG, "Event reported without a package name or a channel ID");
                return;
            }
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(12, android.os.SystemClock.elapsedRealtime());
            event.mPackage = packageName.intern();
            event.mNotificationChannelId = channelId.intern();
            com.android.server.usage.UsageStatsService.this.reportEventOrAddToQueue(userId, event);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportShortcutUsage(java.lang.String packageName, java.lang.String shortcutId, int userId) {
            if (packageName == null || shortcutId == null) {
                android.util.Slog.w(com.android.server.usage.UsageStatsService.TAG, "Event reported without a package name or a shortcut ID");
                return;
            }
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(8, android.os.SystemClock.elapsedRealtime());
            event.mPackage = packageName.intern();
            event.mShortcutId = shortcutId.intern();
            com.android.server.usage.UsageStatsService.this.reportEventOrAddToQueue(userId, event);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportLocusUpdate(android.content.ComponentName activity, int userId, android.content.LocusId locusId, android.os.IBinder appToken) {
            if (locusId == null) {
                return;
            }
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(30, android.os.SystemClock.elapsedRealtime());
            event.mLocusId = locusId.getId();
            event.mPackage = activity.getPackageName();
            event.mClass = activity.getClassName();
            event.mInstanceId = appToken.hashCode();
            com.android.server.usage.UsageStatsService.this.reportEventOrAddToQueue(userId, event);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportContentProviderUsage(java.lang.String name, java.lang.String packageName, int userId) {
            com.android.server.usage.UsageStatsService.this.mAppStandby.postReportContentProviderUsage(name, packageName, userId);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportUserInteractionEvent(java.lang.String pkgName, int userId, android.os.PersistableBundle extras) {
            if (extras != null && extras.size() != 0) {
                java.lang.String category = extras.getString("android.app.usage.extra.EVENT_CATEGORY");
                java.lang.String action = extras.getString("android.app.usage.extra.EVENT_ACTION");
                extras.putString("android.app.usage.extra.EVENT_CATEGORY", com.android.server.usage.UsageStatsService.this.getTrimmedString(category));
                extras.putString("android.app.usage.extra.EVENT_ACTION", com.android.server.usage.UsageStatsService.this.getTrimmedString(action));
            }
            android.app.usage.UsageEvents.Event event = new android.app.usage.UsageEvents.Event(7, android.os.SystemClock.elapsedRealtime());
            event.mPackage = pkgName;
            event.mExtras = extras;
            com.android.server.usage.UsageStatsService.this.reportEventOrAddToQueue(userId, event);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public boolean isAppIdle(java.lang.String packageName, int uidForAppId, int userId) {
            return com.android.server.usage.UsageStatsService.this.mAppStandby.isAppIdleFiltered(packageName, uidForAppId, userId, android.os.SystemClock.elapsedRealtime());
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public int getAppStandbyBucket(java.lang.String packageName, int userId, long nowElapsed) {
            return com.android.server.usage.UsageStatsService.this.mAppStandby.getAppStandbyBucket(packageName, userId, nowElapsed, false);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public int[] getIdleUidsForUser(int userId) {
            return com.android.server.usage.UsageStatsService.this.mAppStandby.getIdleUidsForUser(userId);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void prepareShutdown() {
            com.android.server.usage.UsageStatsService.this.shutdown();
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void prepareForPossibleShutdown() {
            com.android.server.usage.UsageStatsService.this.prepareForPossibleShutdown();
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public byte[] getBackupPayload(int userId, java.lang.String key) {
            if (!com.android.server.usage.UsageStatsService.this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                android.util.Slog.w(com.android.server.usage.UsageStatsService.TAG, "Failed to get backup payload for locked user " + userId);
                return null;
            }
            synchronized (com.android.server.usage.UsageStatsService.this.mLock) {
                com.android.server.usage.UserUsageStatsService userStats = com.android.server.usage.UsageStatsService.this.getUserUsageStatsServiceLocked(userId);
                if (userStats == null) {
                    return null;
                }
                android.util.Slog.i(com.android.server.usage.UsageStatsService.TAG, "Returning backup payload for u=" + userId);
                return userStats.getBackupPayload(key);
            }
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void applyRestoredPayload(int userId, java.lang.String key, byte[] payload) {
            synchronized (com.android.server.usage.UsageStatsService.this.mLock) {
                if (!com.android.server.usage.UsageStatsService.this.mUserUnlockedStates.contains(java.lang.Integer.valueOf(userId))) {
                    android.util.Slog.w(com.android.server.usage.UsageStatsService.TAG, "Failed to apply restored payload for locked user " + userId);
                    return;
                }
                com.android.server.usage.UserUsageStatsService userStats = com.android.server.usage.UsageStatsService.this.getUserUsageStatsServiceLocked(userId);
                if (userStats == null) {
                    return;
                }
                java.util.Set<java.lang.String> restoredApps = userStats.applyRestoredPayload(key, payload);
                com.android.server.usage.UsageStatsService.this.mAppStandby.restoreAppsToRare(restoredApps, userId);
            }
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public java.util.List<android.app.usage.UsageStats> queryUsageStatsForUser(int userId, int intervalType, long beginTime, long endTime, boolean obfuscateInstantApps) {
            return com.android.server.usage.UsageStatsService.this.queryUsageStats(userId, intervalType, beginTime, endTime, obfuscateInstantApps);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public android.app.usage.UsageEvents queryEventsForUser(int userId, long beginTime, long endTime, int flags) {
            return com.android.server.usage.UsageStatsService.this.queryEvents(userId, beginTime, endTime, flags);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void setLastJobRunTime(java.lang.String packageName, int userId, long elapsedRealtime) {
            com.android.server.usage.UsageStatsService.this.mAppStandby.setLastJobRunTime(packageName, userId, elapsedRealtime);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public long getEstimatedPackageLaunchTime(java.lang.String packageName, int userId) {
            return com.android.server.usage.UsageStatsService.this.getEstimatedPackageLaunchTime(userId, packageName);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public long getTimeSinceLastJobRun(java.lang.String packageName, int userId) {
            return com.android.server.usage.UsageStatsService.this.mAppStandby.getTimeSinceLastJobRun(packageName, userId);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportAppJobState(java.lang.String packageName, int userId, int numDeferredJobs, long timeSinceLastJobRun) {
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void onActiveAdminAdded(java.lang.String packageName, int userId) {
            com.android.server.usage.UsageStatsService.this.mAppStandby.addActiveDeviceAdmin(packageName, userId);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void setActiveAdminApps(java.util.Set<java.lang.String> packageNames, int userId) {
            com.android.server.usage.UsageStatsService.this.mAppStandby.setActiveAdminApps(packageNames, userId);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void setAdminProtectedPackages(java.util.Set<java.lang.String> packageNames, int userId) {
            com.android.server.usage.UsageStatsService.this.mAppStandby.setAdminProtectedPackages(packageNames, userId);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void onAdminDataAvailable() {
            com.android.server.usage.UsageStatsService.this.mAppStandby.onAdminDataAvailable();
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportSyncScheduled(java.lang.String packageName, int userId, boolean exempted) {
            com.android.server.usage.UsageStatsService.this.mAppStandby.postReportSyncScheduled(packageName, userId, exempted);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportExemptedSyncStart(java.lang.String packageName, int userId) {
            com.android.server.usage.UsageStatsService.this.mAppStandby.postReportExemptedSyncStart(packageName, userId);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public android.app.usage.UsageStatsManagerInternal.AppUsageLimitData getAppUsageLimit(java.lang.String packageName, android.os.UserHandle user) {
            return com.android.server.usage.UsageStatsService.this.mAppTimeLimit.getAppUsageLimit(packageName, user);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public boolean pruneUninstalledPackagesData(int userId) {
            return com.android.server.usage.UsageStatsService.this.pruneUninstalledPackagesData(userId);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public boolean updatePackageMappingsData(int userId) {
            return com.android.server.usage.UsageStatsService.this.updatePackageMappingsData(userId);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void registerListener(android.app.usage.UsageStatsManagerInternal.UsageEventListener listener) {
            com.android.server.usage.UsageStatsService.this.registerListener(listener);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void unregisterListener(android.app.usage.UsageStatsManagerInternal.UsageEventListener listener) {
            com.android.server.usage.UsageStatsService.this.unregisterListener(listener);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void registerLaunchTimeChangedListener(android.app.usage.UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener listener) {
            com.android.server.usage.UsageStatsService.this.registerLaunchTimeChangedListener(listener);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void unregisterLaunchTimeChangedListener(android.app.usage.UsageStatsManagerInternal.EstimatedLaunchTimeChangedListener listener) {
            com.android.server.usage.UsageStatsService.this.unregisterLaunchTimeChangedListener(listener);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportBroadcastDispatched(int sourceUid, java.lang.String targetPackage, android.os.UserHandle targetUser, long idForResponseEvent, long timestampMs, int targetUidProcState) {
            com.android.server.usage.UsageStatsService.this.mResponseStatsTracker.reportBroadcastDispatchEvent(sourceUid, targetPackage, targetUser, idForResponseEvent, timestampMs, targetUidProcState);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportNotificationPosted(java.lang.String packageName, android.os.UserHandle user, long timestampMs) {
            com.android.server.usage.UsageStatsService.this.mResponseStatsTracker.reportNotificationPosted(packageName, user, timestampMs);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportNotificationUpdated(java.lang.String packageName, android.os.UserHandle user, long timestampMs) {
            com.android.server.usage.UsageStatsService.this.mResponseStatsTracker.reportNotificationUpdated(packageName, user, timestampMs);
        }

        @Override // android.app.usage.UsageStatsManagerInternal
        public void reportNotificationRemoved(java.lang.String packageName, android.os.UserHandle user, long timestampMs) {
            com.android.server.usage.UsageStatsService.this.mResponseStatsTracker.reportNotificationCancelled(packageName, user, timestampMs);
        }
    }

    private class MyPackageMonitor extends com.android.internal.content.PackageMonitor {
        private MyPackageMonitor() {
        }

        public void onPackageRemoved(java.lang.String packageName, int uid) {
            int changingUserId = getChangingUserId();
            if (com.android.server.usage.UsageStatsService.this.shouldDeleteObsoleteData(android.os.UserHandle.of(changingUserId))) {
                com.android.server.usage.UsageStatsService.this.mHandler.obtainMessage(6, changingUserId, 0, packageName).sendToTarget();
            }
            com.android.server.usage.UsageStatsService.this.mResponseStatsTracker.onPackageRemoved(packageName, android.os.UserHandle.getUserId(uid));
            super.onPackageRemoved(packageName, uid);
        }
    }
}
