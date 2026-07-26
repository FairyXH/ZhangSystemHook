package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class ShortcutService extends android.content.pm.IShortcutService.Stub {
    private static final java.lang.String ATTR_VALUE = "value";
    private static final long CALLBACK_DELAY = 100;
    static final boolean DEBUG_LOAD = false;
    static final boolean DEBUG_PROCSTATE = false;
    static final boolean DEBUG_REBOOT = true;
    static final int DEFAULT_ICON_PERSIST_QUALITY = 100;
    static final int DEFAULT_MAX_ICON_DIMENSION_DP = 96;
    static final int DEFAULT_MAX_ICON_DIMENSION_LOWRAM_DP = 48;
    static final int DEFAULT_MAX_SHORTCUTS_PER_ACTIVITY = 15;
    static final int DEFAULT_MAX_SHORTCUTS_PER_APP = 100;
    static final int DEFAULT_MAX_UPDATES_PER_INTERVAL = 10;
    static final long DEFAULT_RESET_INTERVAL_SEC = 86400;
    static final int DEFAULT_SAVE_DELAY_MS = 3000;
    static final java.lang.String DIRECTORY_BITMAPS = "bitmaps";
    static final java.lang.String DIRECTORY_DUMP = "shortcut_dump";
    static final java.lang.String DIRECTORY_PER_USER = "shortcut_service";
    private static final java.lang.String DUMMY_MAIN_ACTIVITY = "android.__dummy__";
    static final java.lang.String FILENAME_BASE_STATE = "shortcut_service.xml";
    static final java.lang.String FILENAME_USER_PACKAGES = "shortcuts.xml";
    static final java.lang.String FILENAME_USER_PACKAGES_RESERVE_COPY = "shortcuts.xml.reservecopy";
    private static final java.lang.String KEY_ICON_SIZE = "iconSize";
    private static final java.lang.String KEY_LOW_RAM = "lowRam";
    private static final java.lang.String KEY_SHORTCUT = "shortcut";
    private static final java.lang.String LAUNCHER_INTENT_CATEGORY = "android.intent.category.LAUNCHER";
    static final int OPERATION_ADD = 1;
    static final int OPERATION_SET = 0;
    static final int OPERATION_UPDATE = 2;
    private static final int PACKAGE_MATCH_FLAGS = 795136;
    private static final int PROCESS_STATE_FOREGROUND_THRESHOLD = 5;
    private static final int SYSTEM_APP_MASK = 129;
    static final java.lang.String TAG = "ShortcutService";
    private static final java.lang.String TAG_LAST_RESET_TIME = "last_reset_time";
    private static final java.lang.String TAG_ROOT = "root";
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private final java.util.concurrent.atomic.AtomicBoolean mBootCompleted;
    private android.content.ComponentName mChooserActivity;
    final android.content.Context mContext;
    private java.util.List<java.lang.Integer> mDirtyUserIds;
    private final android.os.Handler mHandler;
    private final android.content.pm.IPackageManager mIPackageManager;
    private android.graphics.Bitmap.CompressFormat mIconPersistFormat;
    private int mIconPersistQuality;
    private final boolean mIsAppSearchEnabled;
    private int mLastLockedUser;
    private java.lang.Exception mLastWtfStacktrace;
    private final java.util.ArrayList<android.content.pm.ShortcutServiceInternal.ShortcutChangeListener> mListeners;
    private int mMaxIconDimension;
    private int mMaxShortcuts;
    private int mMaxShortcutsPerApp;
    int mMaxUpdatesPerInterval;
    private final com.android.internal.logging.MetricsLogger mMetricsLogger;
    private final java.lang.Object mNonPersistentUsersLock;
    private final android.app.role.OnRoleHoldersChangedListener mOnRoleHoldersChangedListener;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    final android.content.BroadcastReceiver mPackageMonitor;
    private final java.util.concurrent.atomic.AtomicLong mRawLastResetTime;
    final android.content.BroadcastReceiver mReceiver;
    private long mResetInterval;
    private final android.app.role.RoleManager mRoleManager;
    int mSaveDelayMillis;
    private final java.lang.Runnable mSaveDirtyInfoRunner;
    private final java.lang.Object mServiceLock;
    private final java.util.ArrayList<android.content.pm.LauncherApps.ShortcutChangeCallback> mShortcutChangeCallbacks;
    private final com.android.server.pm.ShortcutDumpFiles mShortcutDumpFiles;
    private final android.util.SparseArray<com.android.server.pm.ShortcutNonPersistentUser> mShortcutNonPersistentUsers;
    private final com.android.server.pm.ShortcutRequestPinProcessor mShortcutRequestPinProcessor;
    final com.android.server.pm.IShortcutServiceExt mShortcutServiceExt;
    private com.android.server.pm.IShortcutServiceWrapper mShortcutWrapper;
    private final java.util.concurrent.atomic.AtomicBoolean mShutdown;
    private final android.content.BroadcastReceiver mShutdownReceiver;
    private final com.android.internal.util.StatLogger mStatLogger;
    final android.util.SparseLongArray mUidLastForegroundElapsedTime;
    private final android.app.IUidObserver mUidObserver;
    final android.util.SparseIntArray mUidState;
    final android.util.SparseBooleanArray mUnlockedUsers;
    private final android.app.IUriGrantsManager mUriGrantsManager;
    private final com.android.server.uri.UriGrantsManagerInternal mUriGrantsManagerInternal;
    private final android.os.IBinder mUriPermissionOwner;
    private final android.app.usage.UsageStatsManagerInternal mUsageStatsManagerInternal;
    final com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private final android.util.SparseArray<com.android.server.pm.ShortcutUser> mUsers;
    private int mWtfCount;
    private final java.lang.Object mWtfLock;
    static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    static final java.lang.String DEFAULT_ICON_PERSIST_FORMAT = android.graphics.Bitmap.CompressFormat.PNG.name();
    private static final java.util.List<android.content.pm.ResolveInfo> EMPTY_RESOLVE_INFO = new java.util.ArrayList(0);
    private static final java.util.function.Predicate<android.content.pm.ResolveInfo> ACTIVITY_NOT_EXPORTED = new java.util.function.Predicate<android.content.pm.ResolveInfo>() { // from class: com.android.server.pm.ShortcutService.1
        @Override // java.util.function.Predicate
        public boolean test(android.content.pm.ResolveInfo ri) {
            return !ri.activityInfo.exported;
        }
    };
    private static final java.util.function.Predicate<android.content.pm.ResolveInfo> ACTIVITY_NOT_INSTALLED = new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda25
        @Override // java.util.function.Predicate
        public final boolean test(java.lang.Object obj) {
            return com.android.server.pm.ShortcutService.lambda$static$0((android.content.pm.ResolveInfo) obj);
        }
    };
    private static final java.util.function.Predicate<android.content.pm.PackageInfo> PACKAGE_NOT_INSTALLED = new java.util.function.Predicate<android.content.pm.PackageInfo>() { // from class: com.android.server.pm.ShortcutService.2
        @Override // java.util.function.Predicate
        public boolean test(android.content.pm.PackageInfo pi) {
            return !com.android.server.pm.ShortcutService.isInstalled(pi);
        }
    };
    private static com.android.server.pm.IShortcutServiceExt mStaticShortcutServiceExt = (com.android.server.pm.IShortcutServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IShortcutServiceExt.class).base((java.lang.Object) null).create();

    interface ConfigConstants {
        public static final java.lang.String KEY_ICON_FORMAT = "icon_format";
        public static final java.lang.String KEY_ICON_QUALITY = "icon_quality";
        public static final java.lang.String KEY_MAX_ICON_DIMENSION_DP = "max_icon_dimension_dp";
        public static final java.lang.String KEY_MAX_ICON_DIMENSION_DP_LOWRAM = "max_icon_dimension_dp_lowram";
        public static final java.lang.String KEY_MAX_SHORTCUTS = "max_shortcuts";
        public static final java.lang.String KEY_MAX_SHORTCUTS_PER_APP = "max_shortcuts_per_app";
        public static final java.lang.String KEY_MAX_UPDATES_PER_INTERVAL = "max_updates_per_interval";
        public static final java.lang.String KEY_RESET_INTERVAL_SEC = "reset_interval_sec";
        public static final java.lang.String KEY_SAVE_DELAY_MILLIS = "save_delay_ms";
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface ShortcutOperation {
    }

    interface Stats {
        public static final int ASYNC_PRELOAD_USER_DELAY = 15;
        public static final int CHECK_LAUNCHER_ACTIVITY = 12;
        public static final int CHECK_PACKAGE_CHANGES = 8;
        public static final int CLEANUP_DANGLING_BITMAPS = 5;
        public static final int COUNT = 17;
        public static final int GET_ACTIVITY_WITH_METADATA = 6;
        public static final int GET_APPLICATION_INFO = 3;
        public static final int GET_APPLICATION_RESOURCES = 9;
        public static final int GET_DEFAULT_HOME = 0;
        public static final int GET_DEFAULT_LAUNCHER = 16;
        public static final int GET_INSTALLED_PACKAGES = 7;
        public static final int GET_LAUNCHER_ACTIVITY = 11;
        public static final int GET_PACKAGE_INFO = 1;
        public static final int GET_PACKAGE_INFO_WITH_SIG = 2;
        public static final int IS_ACTIVITY_ENABLED = 13;
        public static final int LAUNCHER_PERMISSION_CHECK = 4;
        public static final int PACKAGE_UPDATE_CHECK = 14;
        public static final int RESOURCE_NAME_LOOKUP = 10;
    }

    static /* synthetic */ boolean lambda$static$0(android.content.pm.ResolveInfo ri) {
        return !isInstalled(ri.activityInfo);
    }

    static class InvalidFileFormatException extends java.lang.Exception {
        public InvalidFileFormatException(java.lang.String message, java.lang.Throwable cause) {
            super(message, cause);
        }
    }

    public ShortcutService(android.content.Context context) {
        this(context, getBgLooper(), false);
    }

    private static android.os.Looper getBgLooper() {
        android.os.HandlerThread handlerThread = new android.os.HandlerThread(KEY_SHORTCUT, 10);
        handlerThread.start();
        return handlerThread.getLooper();
    }

    /* JADX WARN: Multi-variable type inference failed */
    ShortcutService(android.content.Context context, android.os.Looper looper, boolean z) {
        this.mServiceLock = new java.lang.Object();
        this.mNonPersistentUsersLock = new java.lang.Object();
        this.mWtfLock = new java.lang.Object();
        this.mListeners = new java.util.ArrayList<>(1);
        this.mShortcutChangeCallbacks = new java.util.ArrayList<>(1);
        this.mRawLastResetTime = new java.util.concurrent.atomic.AtomicLong(0L);
        this.mUsers = new android.util.SparseArray<>();
        this.mShortcutNonPersistentUsers = new android.util.SparseArray<>();
        this.mUidState = new android.util.SparseIntArray();
        this.mUidLastForegroundElapsedTime = new android.util.SparseLongArray();
        this.mDirtyUserIds = new java.util.ArrayList();
        this.mBootCompleted = new java.util.concurrent.atomic.AtomicBoolean();
        this.mShutdown = new java.util.concurrent.atomic.AtomicBoolean();
        this.mUnlockedUsers = new android.util.SparseBooleanArray();
        this.mStatLogger = new com.android.internal.util.StatLogger(new java.lang.String[]{"getHomeActivities()", "Launcher permission check", "getPackageInfo()", "getPackageInfo(SIG)", "getApplicationInfo", "cleanupDanglingBitmaps", "getActivity+metadata", "getInstalledPackages", "checkPackageChanges", "getApplicationResources", "resourceNameLookup", "getLauncherActivity", "checkLauncherActivity", "isActivityEnabled", "packageUpdateCheck", "asyncPreloadUserDelay", "getDefaultLauncher()"});
        this.mWtfCount = 0;
        this.mMetricsLogger = new com.android.internal.logging.MetricsLogger();
        this.mOnRoleHoldersChangedListener = new com.android.server.pm.ShortcutService.AnonymousClass3();
        this.mUidObserver = new com.android.server.pm.ShortcutService.AnonymousClass4();
        this.mSaveDirtyInfoRunner = new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.saveDirtyInfo();
            }
        };
        this.mLastLockedUser = -1;
        this.mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.pm.ShortcutService.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (!com.android.server.pm.ShortcutService.this.mBootCompleted.get()) {
                    return;
                }
                try {
                    if ("android.intent.action.LOCALE_CHANGED".equals(intent.getAction())) {
                        com.android.server.pm.ShortcutService.this.handleLocaleChanged();
                    }
                } catch (java.lang.Exception e) {
                    com.android.server.pm.ShortcutService.this.wtf("Exception in mReceiver.onReceive", e);
                }
            }
        };
        this.mPackageMonitor = new android.content.BroadcastReceiver() { // from class: com.android.server.pm.ShortcutService.6
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:29:0x00c4  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r13, android.content.Intent r14) {
                /*
                    Method dump skipped, instruction units count: 558
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShortcutService.AnonymousClass6.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        this.mShutdownReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.pm.ShortcutService.7
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                boolean z2 = com.android.server.pm.ShortcutService.DEBUG;
                android.util.Slog.d(com.android.server.pm.ShortcutService.TAG, "Shutdown broadcast received.");
                synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                    if (com.android.server.pm.ShortcutService.this.mHandler.hasCallbacks(com.android.server.pm.ShortcutService.this.mSaveDirtyInfoRunner)) {
                        com.android.server.pm.ShortcutService.this.mHandler.removeCallbacks(com.android.server.pm.ShortcutService.this.mSaveDirtyInfoRunner);
                        com.android.server.pm.ShortcutService.this.forEachLoadedUserLocked(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$7$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                ((com.android.server.pm.ShortcutUser) obj).cancelAllInFlightTasks();
                            }
                        });
                        com.android.server.pm.ShortcutService.this.saveDirtyInfo();
                    }
                    com.android.server.pm.ShortcutService.this.mShutdown.set(true);
                }
            }
        };
        this.mShortcutWrapper = new com.android.server.pm.ShortcutService.ShortcutServiceWrapper();
        this.mShortcutServiceExt = (com.android.server.pm.IShortcutServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IShortcutServiceExt.class).base(this).create();
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        com.android.server.LocalServices.addService(android.content.pm.ShortcutServiceInternal.class, new com.android.server.pm.ShortcutService.LocalService());
        android.os.HandlerThread handlerThread = new android.os.HandlerThread("shortcutservice");
        handlerThread.start();
        this.mHandler = new android.os.Handler(handlerThread.getLooper());
        this.mIPackageManager = android.app.AppGlobals.getPackageManager();
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) java.util.Objects.requireNonNull((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class));
        this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) java.util.Objects.requireNonNull((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class));
        this.mUsageStatsManagerInternal = (android.app.usage.UsageStatsManagerInternal) java.util.Objects.requireNonNull((android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class));
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) java.util.Objects.requireNonNull((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class));
        this.mUriGrantsManager = (android.app.IUriGrantsManager) java.util.Objects.requireNonNull(android.app.UriGrantsManager.getService());
        this.mUriGrantsManagerInternal = (com.android.server.uri.UriGrantsManagerInternal) java.util.Objects.requireNonNull((com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class));
        this.mUriPermissionOwner = this.mUriGrantsManagerInternal.newUriPermissionOwner(TAG);
        this.mRoleManager = (android.app.role.RoleManager) java.util.Objects.requireNonNull((android.app.role.RoleManager) this.mContext.getSystemService(android.app.role.RoleManager.class));
        this.mShortcutRequestPinProcessor = new com.android.server.pm.ShortcutRequestPinProcessor(this, this.mServiceLock);
        this.mShortcutDumpFiles = new com.android.server.pm.ShortcutDumpFiles(this);
        this.mIsAppSearchEnabled = android.provider.DeviceConfig.getBoolean("systemui", "shortcut_appsearch_integration", false) && !injectIsLowRamDevice();
        if (z) {
            return;
        }
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=IGNORE_WM_COMP");
        intentFilter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        intentFilter.addDataScheme("package");
        intentFilter.setPriority(1000);
        this.mContext.registerReceiverAsUser(this.mPackageMonitor, android.os.UserHandle.ALL, intentFilter, null, this.mHandler);
        android.content.IntentFilter intentFilter2 = new android.content.IntentFilter();
        intentFilter2.addAction("android.intent.action.LOCALE_CHANGED");
        intentFilter2.setPriority(1000);
        this.mContext.registerReceiverAsUser(this.mReceiver, android.os.UserHandle.ALL, intentFilter2, null, this.mHandler);
        android.content.IntentFilter intentFilter3 = new android.content.IntentFilter();
        intentFilter3.addAction("android.intent.action.ACTION_SHUTDOWN");
        intentFilter3.setPriority(1000);
        this.mContext.registerReceiverAsUser(this.mShutdownReceiver, android.os.UserHandle.SYSTEM, intentFilter3, null, this.mHandler);
        injectRegisterUidObserver(this.mUidObserver, 3);
        injectRegisterRoleHoldersListener(this.mOnRoleHoldersChangedListener);
    }

    boolean isAppSearchEnabled() {
        return this.mIsAppSearchEnabled;
    }

    long getStatStartTime() {
        return this.mStatLogger.getTime();
    }

    void logDurationStat(int statId, long start) {
        this.mStatLogger.logDurationStat(statId, start);
    }

    public java.lang.String injectGetLocaleTagsForUser(int userId) {
        return android.os.LocaleList.getDefault().toLanguageTags();
    }

    /* JADX INFO: renamed from: com.android.server.pm.ShortcutService$3, reason: invalid class name */
    class AnonymousClass3 implements android.app.role.OnRoleHoldersChangedListener {
        AnonymousClass3() {
        }

        public void onRoleHoldersChanged(java.lang.String roleName, final android.os.UserHandle user) {
            if ("android.app.role.HOME".equals(roleName)) {
                com.android.server.pm.ShortcutService.this.mHandler.postAtFrontOfQueue(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutService$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onRoleHoldersChanged$0(user);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRoleHoldersChanged$0(android.os.UserHandle user) {
            com.android.server.pm.ShortcutService.this.handleOnDefaultLauncherChanged(user.getIdentifier());
        }
    }

    void handleOnDefaultLauncherChanged(int userId) {
        if (DEBUG) {
            android.util.Slog.v(TAG, "Default launcher changed for user: " + userId);
        }
        this.mUriGrantsManagerInternal.revokeUriPermissionFromOwner(this.mUriPermissionOwner, null, -1, 0);
        synchronized (this.mServiceLock) {
            if (isUserLoadedLocked(userId)) {
                getUserShortcutsLocked(userId).setCachedLauncher(null);
            }
        }
    }

    /* JADX INFO: renamed from: com.android.server.pm.ShortcutService$4, reason: invalid class name */
    class AnonymousClass4 extends android.app.UidObserver {
        AnonymousClass4() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUidStateChanged$0(int uid, int procState) {
            com.android.server.pm.ShortcutService.this.handleOnUidStateChanged(uid, procState);
        }

        public void onUidStateChanged(final int uid, final int procState, long procStateSeq, int capability) {
            com.android.server.pm.ShortcutService.this.injectPostToHandler(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutService$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUidStateChanged$0(uid, procState);
                }
            });
        }

        public void onUidGone(final int uid, boolean disabled) {
            com.android.server.pm.ShortcutService.this.injectPostToHandler(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutService$4$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUidGone$1(uid);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUidGone$1(int uid) {
            com.android.server.pm.ShortcutService.this.handleOnUidStateChanged(uid, 20);
        }
    }

    void handleOnUidStateChanged(int uid, int procState) {
        android.os.Trace.traceBegin(524288L, "shortcutHandleOnUidStateChanged");
        synchronized (this.mServiceLock) {
            this.mUidState.put(uid, procState);
            if (isProcessStateForeground(procState)) {
                this.mUidLastForegroundElapsedTime.put(uid, injectElapsedRealtime());
            }
        }
        android.os.Trace.traceEnd(524288L);
    }

    private boolean isProcessStateForeground(int processState) {
        return processState <= 5;
    }

    boolean isUidForegroundLocked(int uid) {
        if (uid == 1000 || isProcessStateForeground(this.mUidState.get(uid, 20))) {
            return true;
        }
        return isProcessStateForeground(this.mActivityManagerInternal.getUidProcessState(uid));
    }

    long getUidLastForegroundElapsedTimeLocked(int uid) {
        return this.mUidLastForegroundElapsedTime.get(uid);
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        final com.android.server.pm.ShortcutService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
            if (com.android.server.pm.ShortcutService.DEBUG) {
                android.os.Binder.LOG_RUNTIME_EXCEPTION = true;
            }
            this.mService = new com.android.server.pm.ShortcutService(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            publishBinderService(com.android.server.pm.ShortcutService.KEY_SHORTCUT, this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            this.mService.onBootPhase(phase);
        }

        @Override // com.android.server.SystemService
        public void onUserStopping(com.android.server.SystemService.TargetUser user) {
            this.mService.handleStopUser(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
            this.mService.handleUnlockUser(user.getUserIdentifier());
        }
    }

    void onBootPhase(int phase) {
        android.util.Slog.d(TAG, "onBootPhase: " + phase);
        switch (phase) {
            case com.android.server.SystemService.PHASE_LOCK_SETTINGS_READY /* 480 */:
                initialize();
                break;
            case 1000:
                this.mBootCompleted.set(true);
                break;
        }
    }

    void handleUnlockUser(final int userId) {
        android.util.Slog.d(TAG, "handleUnlockUser: user=" + userId);
        synchronized (this.mUnlockedUsers) {
            this.mUnlockedUsers.put(userId, true);
        }
        final long start = getStatStartTime();
        injectRunOnNewThread(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleUnlockUser$1(start, userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleUnlockUser$1(long start, int userId) {
        android.os.Trace.traceBegin(524288L, "shortcutHandleUnlockUser");
        synchronized (this.mServiceLock) {
            logDurationStat(15, start);
            boolean needSave = this.mShortcutWrapper.getExtImpl().beforeGetUserShortcutsOnUnlockUser(userId);
            getUserShortcutsLocked(userId);
            this.mShortcutWrapper.getExtImpl().afterGetUserShortcutsOnUnlockUser(needSave, userId);
        }
        android.os.Trace.traceEnd(524288L);
    }

    void handleStopUser(int userId) {
        android.util.Slog.d(TAG, "handleStopUser: user=" + userId);
        android.os.Trace.traceBegin(524288L, "shortcutHandleStopUser");
        synchronized (this.mServiceLock) {
            unloadUserLocked(userId);
            synchronized (this.mUnlockedUsers) {
                this.mUnlockedUsers.put(userId, false);
            }
        }
        android.os.Trace.traceEnd(524288L);
    }

    private void unloadUserLocked(int userId) {
        android.util.Slog.d(TAG, "unloadUserLocked: user=" + userId);
        getUserShortcutsLocked(userId).cancelAllInFlightTasks();
        saveDirtyInfo();
        this.mUsers.delete(userId);
    }

    final com.android.server.pm.ResilientAtomicFile getBaseStateFile() {
        java.io.File mainFile = new java.io.File(injectSystemDataPath(), FILENAME_BASE_STATE);
        java.io.File temporaryBackup = new java.io.File(injectSystemDataPath(), "shortcut_service.xml.backup");
        java.io.File reserveCopy = new java.io.File(injectSystemDataPath(), "shortcut_service.xml.reservecopy");
        return new com.android.server.pm.ResilientAtomicFile(mainFile, temporaryBackup, reserveCopy, 505, "base shortcut", null);
    }

    private void initialize() {
        synchronized (this.mServiceLock) {
            loadConfigurationLocked();
            loadBaseStateLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadConfigurationLocked() {
        updateConfigurationLocked(injectShortcutManagerConstants());
    }

    boolean updateConfigurationLocked(java.lang.String config) {
        int i;
        boolean result = true;
        android.util.KeyValueListParser parser = new android.util.KeyValueListParser(',');
        try {
            parser.setString(config);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Bad shortcut manager settings", e);
            result = false;
        }
        this.mSaveDelayMillis = java.lang.Math.max(0, (int) parser.getLong(com.android.server.pm.ShortcutService.ConfigConstants.KEY_SAVE_DELAY_MILLIS, 3000L));
        this.mResetInterval = java.lang.Math.max(1L, parser.getLong(com.android.server.pm.ShortcutService.ConfigConstants.KEY_RESET_INTERVAL_SEC, DEFAULT_RESET_INTERVAL_SEC) * 1000);
        this.mMaxUpdatesPerInterval = java.lang.Math.max(0, (int) parser.getLong(com.android.server.pm.ShortcutService.ConfigConstants.KEY_MAX_UPDATES_PER_INTERVAL, 10L));
        this.mMaxShortcuts = java.lang.Math.max(0, (int) parser.getLong(com.android.server.pm.ShortcutService.ConfigConstants.KEY_MAX_SHORTCUTS, 15L));
        this.mMaxShortcutsPerApp = java.lang.Math.max(0, (int) parser.getLong(com.android.server.pm.ShortcutService.ConfigConstants.KEY_MAX_SHORTCUTS_PER_APP, CALLBACK_DELAY));
        if (injectIsLowRamDevice()) {
            i = (int) parser.getLong(com.android.server.pm.ShortcutService.ConfigConstants.KEY_MAX_ICON_DIMENSION_DP_LOWRAM, 48L);
        } else {
            i = (int) parser.getLong(com.android.server.pm.ShortcutService.ConfigConstants.KEY_MAX_ICON_DIMENSION_DP, 96L);
        }
        int iconDimensionDp = java.lang.Math.max(1, i);
        this.mMaxIconDimension = injectDipToPixel(iconDimensionDp);
        this.mIconPersistFormat = android.graphics.Bitmap.CompressFormat.valueOf(parser.getString(com.android.server.pm.ShortcutService.ConfigConstants.KEY_ICON_FORMAT, DEFAULT_ICON_PERSIST_FORMAT));
        this.mIconPersistQuality = (int) parser.getLong(com.android.server.pm.ShortcutService.ConfigConstants.KEY_ICON_QUALITY, CALLBACK_DELAY);
        return result;
    }

    java.lang.String injectShortcutManagerConstants() {
        return android.provider.Settings.Global.getString(this.mContext.getContentResolver(), "shortcut_manager_constants");
    }

    int injectDipToPixel(int dip) {
        return (int) android.util.TypedValue.applyDimension(1, dip, this.mContext.getResources().getDisplayMetrics());
    }

    static java.lang.String parseStringAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attribute) {
        return parser.getAttributeValue((java.lang.String) null, attribute);
    }

    static boolean parseBooleanAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attribute) {
        return parseLongAttribute(parser, attribute) == 1;
    }

    static boolean parseBooleanAttribute(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser, java.lang.String str, boolean z) {
        return parseLongAttribute(typedXmlPullParser, str, z ? 1L : 0L) == 1;
    }

    static int parseIntAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attribute) {
        return (int) parseLongAttribute(parser, attribute);
    }

    static int parseIntAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attribute, int def) {
        return (int) parseLongAttribute(parser, attribute, def);
    }

    static long parseLongAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attribute) {
        return parseLongAttribute(parser, attribute, 0L);
    }

    static long parseLongAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attribute, long def) {
        java.lang.String value = parseStringAttribute(parser, attribute);
        if (android.text.TextUtils.isEmpty(value)) {
            return def;
        }
        try {
            return java.lang.Long.parseLong(value);
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e(TAG, "Error parsing long " + value);
            return def;
        }
    }

    static android.content.ComponentName parseComponentNameAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attribute) {
        java.lang.String value = parseStringAttribute(parser, attribute);
        if (android.text.TextUtils.isEmpty(value)) {
            return null;
        }
        return android.content.ComponentName.unflattenFromString(value);
    }

    static android.content.Intent parseIntentAttributeNoDefault(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attribute) {
        java.lang.String value = parseStringAttribute(parser, attribute);
        if (android.text.TextUtils.isEmpty(value)) {
            return null;
        }
        try {
            android.content.Intent parsed = android.content.Intent.parseUri(value, 0);
            return parsed;
        } catch (java.net.URISyntaxException e) {
            android.util.Slog.e(TAG, "Error parsing intent", e);
            return null;
        }
    }

    static android.content.Intent parseIntentAttribute(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String attribute) {
        android.content.Intent parsed = parseIntentAttributeNoDefault(parser, attribute);
        if (parsed == null) {
            return new android.content.Intent("android.intent.action.VIEW");
        }
        return parsed;
    }

    static void writeTagValue(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, java.lang.String value) throws java.io.IOException {
        if (android.text.TextUtils.isEmpty(value)) {
            return;
        }
        out.startTag((java.lang.String) null, tag);
        out.attribute((java.lang.String) null, ATTR_VALUE, value);
        out.endTag((java.lang.String) null, tag);
    }

    static void writeTagValue(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, long value) throws java.io.IOException {
        writeTagValue(out, tag, java.lang.Long.toString(value));
    }

    static void writeTagValue(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, android.content.ComponentName name) throws java.io.IOException {
        if (name == null) {
            return;
        }
        writeTagValue(out, tag, name.flattenToString());
    }

    static void writeTagExtra(com.android.modules.utils.TypedXmlSerializer out, java.lang.String tag, android.os.PersistableBundle bundle) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (bundle == null) {
            return;
        }
        out.startTag((java.lang.String) null, tag);
        bundle.saveToXml(out);
        out.endTag((java.lang.String) null, tag);
    }

    static void writeAttr(com.android.modules.utils.TypedXmlSerializer out, java.lang.String name, java.lang.CharSequence value) throws java.io.IOException {
        if (android.text.TextUtils.isEmpty(value)) {
            return;
        }
        out.attribute((java.lang.String) null, name, value.toString());
    }

    static void writeAttr(com.android.modules.utils.TypedXmlSerializer out, java.lang.String name, long value) throws java.io.IOException {
        writeAttr(out, name, java.lang.String.valueOf(value));
    }

    static void writeAttr(com.android.modules.utils.TypedXmlSerializer out, java.lang.String name, boolean value) throws java.io.IOException {
        if (value) {
            writeAttr(out, name, "1");
        } else {
            writeAttr(out, name, "0");
        }
    }

    static void writeAttr(com.android.modules.utils.TypedXmlSerializer out, java.lang.String name, android.content.ComponentName comp) throws java.io.IOException {
        if (comp == null) {
            return;
        }
        writeAttr(out, name, comp.flattenToString());
    }

    static void writeAttr(com.android.modules.utils.TypedXmlSerializer out, java.lang.String name, android.content.Intent intent) throws java.io.IOException {
        if (intent == null) {
            return;
        }
        writeAttr(out, name, intent.toUri(0));
    }

    void saveBaseState() {
        java.io.FileOutputStream outs;
        com.android.server.pm.ResilientAtomicFile file = getBaseStateFile();
        try {
            android.util.Slog.d(TAG, "Saving to " + file.getBaseFile());
            try {
                synchronized (this.mServiceLock) {
                    outs = file.startWrite();
                }
                com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(outs);
                out.startDocument((java.lang.String) null, true);
                out.startTag((java.lang.String) null, TAG_ROOT);
                writeTagValue(out, TAG_LAST_RESET_TIME, this.mRawLastResetTime.get());
                out.endTag((java.lang.String) null, TAG_ROOT);
                out.endDocument();
                file.finishWrite(outs);
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Failed to write to file " + file.getBaseFile(), e);
                file.failWrite(null);
            }
            if (file != null) {
                file.close();
            }
        } catch (java.lang.Throwable th) {
            if (file != null) {
                try {
                    file.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void loadBaseStateLocked() {
        byte b;
        this.mRawLastResetTime.set(0L);
        com.android.server.pm.ResilientAtomicFile file = getBaseStateFile();
        try {
            android.util.Slog.d(TAG, "Loading from " + file.getBaseFile());
            try {
                try {
                    java.io.FileInputStream in = file.openRead();
                    if (in == null) {
                        throw new java.io.FileNotFoundException(file.getBaseFile().getAbsolutePath());
                    }
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                    while (true) {
                        int type = parser.next();
                        if (type != 1) {
                            if (type == 2) {
                                int depth = parser.getDepth();
                                java.lang.String tag = parser.getName();
                                if (depth == 1) {
                                    if (!TAG_ROOT.equals(tag)) {
                                        android.util.Slog.e(TAG, "Invalid root tag: " + tag);
                                        if (file != null) {
                                            file.close();
                                            return;
                                        }
                                        return;
                                    }
                                } else {
                                    switch (tag.hashCode()) {
                                        case -68726522:
                                            if (tag.equals(TAG_LAST_RESET_TIME)) {
                                                b = 0;
                                                break;
                                            }
                                        default:
                                            b = -1;
                                            break;
                                    }
                                    switch (b) {
                                        case 0:
                                            this.mRawLastResetTime.set(parseLongAttribute(parser, ATTR_VALUE));
                                            break;
                                        default:
                                            android.util.Slog.e(TAG, "Invalid tag: " + tag);
                                            break;
                                    }
                                }
                            }
                        }
                    }
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                    file.failRead(null, e);
                    loadBaseStateLocked();
                    if (file != null) {
                        file.close();
                        return;
                    }
                    return;
                }
            } catch (java.io.FileNotFoundException e2) {
            }
            if (file != null) {
                file.close();
            }
            getLastResetTimeLocked();
        } catch (java.lang.Throwable th) {
            if (file != null) {
                try {
                    file.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    final com.android.server.pm.ResilientAtomicFile getUserFile(int userId) {
        java.io.File mainFile = new java.io.File(injectUserDataPath(userId), FILENAME_USER_PACKAGES);
        java.io.File temporaryBackup = new java.io.File(injectUserDataPath(userId), "shortcuts.xml.backup");
        java.io.File reserveCopy = new java.io.File(injectUserDataPath(userId), FILENAME_USER_PACKAGES_RESERVE_COPY);
        return new com.android.server.pm.ResilientAtomicFile(mainFile, temporaryBackup, reserveCopy, 505, "user shortcut", null);
    }

    private void saveUser(int userId) {
        java.io.FileOutputStream os;
        com.android.server.pm.ResilientAtomicFile file = getUserFile(userId);
        try {
            try {
                android.util.Slog.d(TAG, "Saving to " + file);
                synchronized (this.mServiceLock) {
                    os = file.startWrite();
                    saveUserInternalLocked(userId, os, false);
                }
                file.finishWrite(os);
                cleanupDanglingBitmapDirectoriesLocked(userId);
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.e(TAG, "Failed to write to file " + file, e);
                file.failWrite(null);
            }
            if (file != null) {
                file.close();
            }
            getUserShortcutsLocked(userId).logSharingShortcutStats(this.mMetricsLogger);
        } catch (java.lang.Throwable th) {
            if (file != null) {
                try {
                    file.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void saveUserInternalLocked(int userId, java.io.OutputStream os, boolean forBackup) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.modules.utils.TypedXmlSerializer out;
        if (forBackup) {
            out = android.util.Xml.newFastSerializer();
            out.setOutput(os, java.nio.charset.StandardCharsets.UTF_8.name());
        } else {
            out = android.util.Xml.resolveSerializer(os);
        }
        out.startDocument((java.lang.String) null, true);
        getUserShortcutsLocked(userId).saveToXml(out, forBackup);
        out.endDocument();
        os.flush();
    }

    static java.io.IOException throwForInvalidTag(int depth, java.lang.String tag) throws java.io.IOException {
        throw new java.io.IOException(java.lang.String.format("Invalid tag '%s' found at depth %d", tag, java.lang.Integer.valueOf(depth)));
    }

    static void warnForInvalidTag(int depth, java.lang.String tag) throws java.io.IOException {
        android.util.Slog.w(TAG, java.lang.String.format("Invalid tag '%s' found at depth %d", tag, java.lang.Integer.valueOf(depth)));
    }

    private com.android.server.pm.ShortcutUser loadUserLocked(int userId) throws java.lang.Exception {
        com.android.server.pm.ResilientAtomicFile file = getUserFile(userId);
        try {
            try {
                android.util.Slog.d(TAG, "Loading from " + file);
                java.io.FileInputStream in = file.openRead();
                if (in == null) {
                    android.util.Slog.d(TAG, "Not found " + file);
                    if (file != null) {
                        file.close();
                        return null;
                    }
                    return null;
                }
                com.android.server.pm.ShortcutUser shortcutUserLoadUserInternal = loadUserInternal(userId, in, false);
                if (file != null) {
                    file.close();
                }
                return shortcutUserLoadUserInternal;
            } catch (java.lang.Exception e) {
                file.failRead(null, e);
                com.android.server.pm.ShortcutUser shortcutUserLoadUserLocked = loadUserLocked(userId);
                if (file != null) {
                    file.close();
                }
                return shortcutUserLoadUserLocked;
            }
        } catch (java.lang.Throwable e2) {
            if (file != null) {
                try {
                    file.close();
                } catch (java.lang.Throwable th) {
                    e2.addSuppressed(th);
                }
            }
            throw e2;
        }
    }

    private com.android.server.pm.ShortcutUser loadUserInternal(int userId, java.io.InputStream is, boolean fromBackup) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, com.android.server.pm.ShortcutService.InvalidFileFormatException {
        com.android.modules.utils.TypedXmlPullParser parser;
        com.android.server.pm.ShortcutUser ret = null;
        if (fromBackup) {
            parser = android.util.Xml.newFastPullParser();
            parser.setInput(is, java.nio.charset.StandardCharsets.UTF_8.name());
        } else {
            parser = android.util.Xml.resolvePullParser(is);
        }
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type == 2) {
                    int depth = parser.getDepth();
                    java.lang.String tag = parser.getName();
                    android.util.Slog.d(TAG, java.lang.String.format("depth=%d type=%d name=%s", java.lang.Integer.valueOf(depth), java.lang.Integer.valueOf(type), tag));
                    if (depth == 1 && "user".equals(tag)) {
                        ret = com.android.server.pm.ShortcutUser.loadFromXml(this, parser, userId, fromBackup);
                    } else {
                        throwForInvalidTag(depth, tag);
                    }
                }
            } else {
                return ret;
            }
        }
    }

    private void scheduleSaveBaseState() {
        scheduleSaveInner(-10000);
    }

    void scheduleSaveUser(int userId) {
        scheduleSaveInner(userId);
    }

    private void scheduleSaveInner(int userId) {
        android.util.Slog.d(TAG, "Scheduling to save for " + userId);
        synchronized (this.mServiceLock) {
            if (!this.mDirtyUserIds.contains(java.lang.Integer.valueOf(userId))) {
                this.mDirtyUserIds.add(java.lang.Integer.valueOf(userId));
            }
        }
        this.mHandler.removeCallbacks(this.mSaveDirtyInfoRunner);
        this.mHandler.postDelayed(this.mSaveDirtyInfoRunner, this.mSaveDelayMillis);
    }

    void saveDirtyInfo() {
        java.util.List<java.lang.Integer> tmp;
        android.util.Slog.d(TAG, "saveDirtyInfo");
        if (this.mShutdown.get()) {
            return;
        }
        try {
            try {
                android.os.Trace.traceBegin(524288L, "shortcutSaveDirtyInfo");
                java.util.List<java.lang.Integer> dirtyUserIds = new java.util.ArrayList<>();
                synchronized (this.mServiceLock) {
                    tmp = this.mDirtyUserIds;
                    this.mDirtyUserIds = dirtyUserIds;
                }
                for (int i = tmp.size() - 1; i >= 0; i--) {
                    int userId = tmp.get(i).intValue();
                    if (userId == -10000) {
                        saveBaseState();
                    } else {
                        saveUser(userId);
                    }
                }
            } catch (java.lang.Exception e) {
                wtf("Exception in saveDirtyInfo", e);
            }
        } finally {
            android.os.Trace.traceEnd(524288L);
        }
    }

    long getLastResetTimeLocked() {
        updateTimesLocked();
        return this.mRawLastResetTime.get();
    }

    long getNextResetTimeLocked() {
        updateTimesLocked();
        return this.mRawLastResetTime.get() + this.mResetInterval;
    }

    static boolean isClockValid(long time) {
        return time >= 1420070400;
    }

    private void updateTimesLocked() {
        long now = injectCurrentTimeMillis();
        long prevLastResetTime = this.mRawLastResetTime.get();
        long newLastResetTime = prevLastResetTime;
        if (newLastResetTime == 0) {
            newLastResetTime = now;
        } else if (now < newLastResetTime) {
            if (isClockValid(now)) {
                android.util.Slog.w(TAG, "Clock rewound");
                newLastResetTime = now;
            }
        } else if (this.mResetInterval + newLastResetTime <= now) {
            long offset = newLastResetTime % this.mResetInterval;
            newLastResetTime = ((now / this.mResetInterval) * this.mResetInterval) + offset;
        }
        this.mRawLastResetTime.set(newLastResetTime);
        if (prevLastResetTime != newLastResetTime) {
            scheduleSaveBaseState();
        }
    }

    protected boolean isUserUnlockedL(int userId) {
        synchronized (this.mUnlockedUsers) {
            if (this.mUnlockedUsers.get(userId)) {
                return true;
            }
            return this.mUserManagerInternal.isUserUnlockingOrUnlocked(userId);
        }
    }

    void throwIfUserLockedL(int userId) {
        if (!isUserUnlockedL(userId)) {
            throw new java.lang.IllegalStateException("User " + userId + " is locked or not running");
        }
    }

    private boolean isUserLoadedLocked(int userId) {
        return this.mUsers.get(userId) != null;
    }

    com.android.server.pm.ShortcutUser getUserShortcutsLocked(int userId) throws java.lang.Exception {
        if (!isUserUnlockedL(userId)) {
            if (userId != this.mLastLockedUser) {
                wtf("User still locked");
                this.mLastLockedUser = userId;
            }
        } else {
            this.mLastLockedUser = -1;
        }
        com.android.server.pm.ShortcutUser userPackages = this.mUsers.get(userId);
        if (userPackages == null) {
            userPackages = loadUserLocked(userId);
            if (userPackages == null) {
                userPackages = new com.android.server.pm.ShortcutUser(this, userId);
            }
            this.mUsers.put(userId, userPackages);
            checkPackageChanges(userId);
        }
        return userPackages;
    }

    com.android.server.pm.ShortcutNonPersistentUser getNonPersistentUserLocked(int userId) {
        com.android.server.pm.ShortcutNonPersistentUser ret = this.mShortcutNonPersistentUsers.get(userId);
        if (ret == null) {
            com.android.server.pm.ShortcutNonPersistentUser ret2 = new com.android.server.pm.ShortcutNonPersistentUser(userId);
            this.mShortcutNonPersistentUsers.put(userId, ret2);
            return ret2;
        }
        return ret;
    }

    void forEachLoadedUserLocked(java.util.function.Consumer<com.android.server.pm.ShortcutUser> c) {
        for (int i = this.mUsers.size() - 1; i >= 0; i--) {
            c.accept(this.mUsers.valueAt(i));
        }
    }

    com.android.server.pm.ShortcutPackage getPackageShortcutsLocked(java.lang.String packageName, int userId) {
        return getUserShortcutsLocked(userId).getPackageShortcuts(packageName);
    }

    com.android.server.pm.ShortcutPackage getPackageShortcutsForPublisherLocked(java.lang.String packageName, int userId) {
        com.android.server.pm.ShortcutPackage ret = getUserShortcutsLocked(userId).getPackageShortcuts(packageName);
        ret.getUser().onCalledByPublisher(packageName);
        return ret;
    }

    com.android.server.pm.ShortcutLauncher getLauncherShortcutsLocked(java.lang.String packageName, int ownerUserId, int launcherUserId) {
        return getUserShortcutsLocked(ownerUserId).getLauncherShortcuts(packageName, launcherUserId);
    }

    public void cleanupBitmapsForPackage(int userId, java.lang.String packageName) {
        java.io.File packagePath = new java.io.File(getUserBitmapFilePath(userId), packageName);
        if (!packagePath.isDirectory()) {
            return;
        }
        if (!android.os.FileUtils.deleteContents(packagePath) || !packagePath.delete()) {
            android.util.Slog.w(TAG, "Unable to remove directory " + packagePath);
        }
    }

    private void cleanupDanglingBitmapDirectoriesLocked(int userId) throws java.lang.Exception {
        if (DEBUG) {
            android.util.Slog.d(TAG, "cleanupDanglingBitmaps: userId=" + userId);
        }
        long start = getStatStartTime();
        com.android.server.pm.ShortcutUser user = getUserShortcutsLocked(userId);
        java.io.File bitmapDir = getUserBitmapFilePath(userId);
        java.io.File[] children = bitmapDir.listFiles();
        if (children == null) {
            return;
        }
        for (java.io.File child : children) {
            if (child.isDirectory()) {
                java.lang.String packageName = child.getName();
                if (DEBUG) {
                    android.util.Slog.d(TAG, "cleanupDanglingBitmaps: Found directory=" + packageName);
                }
                if (!user.hasPackage(packageName)) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Removing dangling bitmap directory: " + packageName);
                    }
                    cleanupBitmapsForPackage(userId, packageName);
                } else {
                    user.getPackageShortcuts(packageName).cleanupDanglingBitmapFiles(child);
                }
            }
        }
        logDurationStat(5, start);
    }

    static class FileOutputStreamWithPath extends java.io.FileOutputStream {
        private final java.io.File mFile;

        public FileOutputStreamWithPath(java.io.File file) throws java.io.FileNotFoundException {
            super(file);
            this.mFile = file;
        }

        public java.io.File getFile() {
            return this.mFile;
        }
    }

    com.android.server.pm.ShortcutService.FileOutputStreamWithPath openIconFileForWrite(int userId, android.content.pm.ShortcutInfo shortcut) throws java.io.IOException {
        java.io.File file;
        java.io.File packagePath = new java.io.File(getUserBitmapFilePath(userId), shortcut.getPackage());
        if (!packagePath.isDirectory()) {
            packagePath.mkdirs();
            if (!packagePath.isDirectory()) {
                throw new java.io.IOException("Unable to create directory " + packagePath);
            }
            android.os.SELinux.restorecon(packagePath);
        }
        java.lang.String baseName = java.lang.String.valueOf(injectCurrentTimeMillis());
        int suffix = 0;
        while (true) {
            java.lang.String filename = (suffix == 0 ? baseName : baseName + "_" + suffix) + ".png";
            file = new java.io.File(packagePath, filename);
            if (!file.exists()) {
                break;
            }
            suffix++;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Saving icon to " + file.getAbsolutePath());
        }
        return new com.android.server.pm.ShortcutService.FileOutputStreamWithPath(file);
    }

    void saveIconAndFixUpShortcutLocked(com.android.server.pm.ShortcutPackage p, android.content.pm.ShortcutInfo shortcut) {
        if (shortcut.hasIconFile() || shortcut.hasIconResource() || shortcut.hasIconUri()) {
            return;
        }
        long token = injectClearCallingIdentity();
        try {
            p.removeIcon(shortcut);
            android.graphics.drawable.Icon icon = shortcut.getIcon();
            if (icon == null) {
                return;
            }
            int maxIconDimension = this.mMaxIconDimension;
            try {
                switch (icon.getType()) {
                    case 1:
                        icon.getBitmap();
                        break;
                    case 2:
                        injectValidateIconResPackage(shortcut, icon);
                        shortcut.setIconResourceId(icon.getResId());
                        shortcut.addFlags(4);
                        return;
                    case 3:
                    default:
                        throw android.content.pm.ShortcutInfo.getInvalidIconException();
                    case 4:
                        shortcut.setIconUri(icon.getUriString());
                        shortcut.addFlags(32768);
                        return;
                    case 5:
                        icon.getBitmap();
                        maxIconDimension = (int) (maxIconDimension * ((android.graphics.drawable.AdaptiveIconDrawable.getExtraInsetFraction() * 2.0f) + 1.0f));
                        break;
                    case 6:
                        shortcut.setIconUri(icon.getUriString());
                        shortcut.addFlags(33280);
                        return;
                }
                p.saveBitmap(shortcut, maxIconDimension, this.mIconPersistFormat, this.mIconPersistQuality);
            } finally {
                shortcut.clearIcon();
            }
        } finally {
            injectRestoreCallingIdentity(token);
        }
    }

    void injectValidateIconResPackage(android.content.pm.ShortcutInfo shortcut, android.graphics.drawable.Icon icon) {
        if (!shortcut.getPackage().equals(icon.getResPackage())) {
            throw new java.lang.IllegalArgumentException("Icon resource must reside in shortcut owner package");
        }
    }

    static android.graphics.Bitmap shrinkBitmap(android.graphics.Bitmap in, int maxSize) {
        int ow = in.getWidth();
        int oh = in.getHeight();
        if (ow <= maxSize && oh <= maxSize) {
            if (DEBUG) {
                android.util.Slog.d(TAG, java.lang.String.format("Icon size %dx%d, no need to shrink", java.lang.Integer.valueOf(ow), java.lang.Integer.valueOf(oh)));
            }
            return in;
        }
        int longerDimension = java.lang.Math.max(ow, oh);
        int nw = (ow * maxSize) / longerDimension;
        int nh = (oh * maxSize) / longerDimension;
        if (DEBUG) {
            android.util.Slog.d(TAG, java.lang.String.format("Icon size %dx%d, shrinking to %dx%d", java.lang.Integer.valueOf(ow), java.lang.Integer.valueOf(oh), java.lang.Integer.valueOf(nw), java.lang.Integer.valueOf(nh)));
        }
        android.graphics.Bitmap scaledBitmap = android.graphics.Bitmap.createBitmap(nw, nh, android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Canvas c = new android.graphics.Canvas(scaledBitmap);
        android.graphics.RectF dst = new android.graphics.RectF(0.0f, 0.0f, nw, nh);
        c.drawBitmap(in, (android.graphics.Rect) null, dst, (android.graphics.Paint) null);
        return scaledBitmap;
    }

    void fixUpShortcutResourceNamesAndValues(android.content.pm.ShortcutInfo si) {
        android.content.res.Resources publisherRes = injectGetResourcesForApplicationAsUser(si.getPackage(), si.getUserId());
        if (publisherRes != null) {
            long start = getStatStartTime();
            try {
                si.lookupAndFillInResourceNames(publisherRes);
                logDurationStat(10, start);
                si.resolveResourceStrings(publisherRes);
            } catch (java.lang.Throwable th) {
                logDurationStat(10, start);
                throw th;
            }
        }
    }

    private boolean isCallerSystem() {
        int callingUid = injectBinderCallingUid();
        return android.os.UserHandle.isSameApp(callingUid, 1000);
    }

    private boolean isCallerShell() {
        int callingUid = injectBinderCallingUid();
        return callingUid == 2000 || callingUid == 0;
    }

    android.content.ComponentName injectChooserActivity() {
        if (this.mChooserActivity == null) {
            this.mChooserActivity = android.content.ComponentName.unflattenFromString(this.mContext.getResources().getString(android.R.string.config_customCountryDetector));
        }
        return this.mChooserActivity;
    }

    private boolean isCallerChooserActivity() {
        int callingUid = injectBinderCallingUid();
        android.content.ComponentName systemChooser = injectChooserActivity();
        if (systemChooser == null) {
            return false;
        }
        int uid = injectGetPackageUid(systemChooser.getPackageName(), 0);
        return android.os.UserHandle.getAppId(uid) == android.os.UserHandle.getAppId(callingUid);
    }

    private void enforceSystemOrShell() {
        if (!isCallerSystem() && !isCallerShell()) {
            throw new java.lang.SecurityException("Caller must be system or shell");
        }
    }

    private void enforceShell() {
        if (!isCallerShell()) {
            throw new java.lang.SecurityException("Caller must be shell");
        }
    }

    private void enforceSystem() {
        if (!isCallerSystem()) {
            throw new java.lang.SecurityException("Caller must be system");
        }
    }

    private void enforceResetThrottlingPermission() {
        if (isCallerSystem()) {
            return;
        }
        enforceCallingOrSelfPermission("android.permission.RESET_SHORTCUT_MANAGER_THROTTLING", null);
    }

    private void enforceCallingOrSelfPermission(java.lang.String permission, java.lang.String message) {
        if (isCallerSystem()) {
            return;
        }
        injectEnforceCallingPermission(permission, message);
    }

    void injectEnforceCallingPermission(java.lang.String permission, java.lang.String message) {
        this.mContext.enforceCallingPermission(permission, message);
    }

    private void verifyCallerUserId(int userId) {
        if (isCallerSystem()) {
            return;
        }
        int callingUid = injectBinderCallingUid();
        if (android.os.UserHandle.getUserId(callingUid) != userId) {
            throw new java.lang.SecurityException("Invalid user-ID");
        }
    }

    private void verifyCaller(java.lang.String packageName, int userId) {
        com.android.internal.util.Preconditions.checkStringNotEmpty(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        if (isCallerSystem()) {
            return;
        }
        int callingUid = injectBinderCallingUid();
        if (android.os.UserHandle.getUserId(callingUid) != userId) {
            throw new java.lang.SecurityException("Invalid user-ID");
        }
        if (injectGetPackageUid(packageName, userId) != callingUid) {
            throw new java.lang.SecurityException("Calling package name mismatch");
        }
        com.android.internal.util.Preconditions.checkState(!isEphemeralApp(packageName, userId), "Ephemeral apps can't use ShortcutManager");
    }

    private void verifyShortcutInfoPackage(java.lang.String callerPackage, android.content.pm.ShortcutInfo si) {
        if (si == null) {
            return;
        }
        if (!java.util.Objects.equals(callerPackage, si.getPackage())) {
            android.util.EventLog.writeEvent(1397638484, "109824443", -1, "");
            throw new java.lang.SecurityException("Shortcut package name mismatch");
        }
        int callingUid = injectBinderCallingUid();
        if (android.os.UserHandle.getUserId(callingUid) != si.getUserId()) {
            throw new java.lang.SecurityException("User-ID in shortcut doesn't match the caller");
        }
    }

    private void verifyShortcutInfoPackages(java.lang.String callerPackage, java.util.List<android.content.pm.ShortcutInfo> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            verifyShortcutInfoPackage(callerPackage, list.get(i));
        }
    }

    void injectPostToHandler(java.lang.Runnable r) {
        this.mHandler.post(r);
    }

    void injectRunOnNewThread(java.lang.Runnable r) {
        new java.lang.Thread(r).start();
    }

    void injectPostToHandlerDebounced(java.lang.Object token, java.lang.Runnable r) {
        java.util.Objects.requireNonNull(token);
        java.util.Objects.requireNonNull(r);
        synchronized (this.mServiceLock) {
            this.mHandler.removeCallbacksAndMessages(token);
            this.mHandler.postDelayed(r, token, CALLBACK_DELAY);
        }
    }

    void enforceMaxActivityShortcuts(int numShortcuts) {
        if (numShortcuts > this.mMaxShortcuts) {
            throw new java.lang.IllegalArgumentException("Max number of dynamic shortcuts exceeded");
        }
    }

    int getMaxActivityShortcuts() {
        return this.mMaxShortcuts;
    }

    int getMaxAppShortcuts() {
        return this.mMaxShortcutsPerApp;
    }

    void packageShortcutsChanged(com.android.server.pm.ShortcutPackage sp, java.util.List<android.content.pm.ShortcutInfo> changedShortcuts, java.util.List<android.content.pm.ShortcutInfo> removedShortcuts) {
        java.util.Objects.requireNonNull(sp);
        java.lang.String packageName = sp.getPackageName();
        int userId = sp.getPackageUserId();
        if (DEBUG) {
            android.util.Slog.d(TAG, java.lang.String.format("Shortcut changes: package=%s, user=%d", packageName, java.lang.Integer.valueOf(userId)));
        }
        injectPostToHandlerDebounced(sp, notifyListenerRunnable(packageName, userId));
        notifyShortcutChangeCallbacks(packageName, userId, changedShortcuts, removedShortcuts);
        sp.scheduleSave();
        this.mShortcutServiceExt.hookInPackageShortcutsChanged(userId, sp);
    }

    private void notifyListeners(java.lang.String packageName, int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, java.lang.String.format("Shortcut changes: package=%s, user=%d", packageName, java.lang.Integer.valueOf(userId)));
        }
        injectPostToHandler(notifyListenerRunnable(packageName, userId));
    }

    private java.lang.Runnable notifyListenerRunnable(final java.lang.String packageName, final int userId) {
        return new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyListenerRunnable$2(userId, packageName);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyListenerRunnable$2(int userId, java.lang.String packageName) {
        try {
            synchronized (this.mServiceLock) {
                if (isUserUnlockedL(userId)) {
                    java.util.ArrayList<android.content.pm.ShortcutServiceInternal.ShortcutChangeListener> copy = new java.util.ArrayList<>(this.mListeners);
                    for (int i = copy.size() - 1; i >= 0; i--) {
                        copy.get(i).onShortcutChanged(packageName, userId);
                    }
                }
            }
        } catch (java.lang.Exception e) {
        }
    }

    private void notifyShortcutChangeCallbacks(final java.lang.String packageName, final int userId, java.util.List<android.content.pm.ShortcutInfo> changedShortcuts, java.util.List<android.content.pm.ShortcutInfo> removedShortcuts) {
        final java.util.List<android.content.pm.ShortcutInfo> changedList = removeNonKeyFields(changedShortcuts);
        final java.util.List<android.content.pm.ShortcutInfo> removedList = removeNonKeyFields(removedShortcuts);
        final android.os.UserHandle user = android.os.UserHandle.of(userId);
        injectPostToHandler(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyShortcutChangeCallbacks$3(userId, changedList, packageName, user, removedList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyShortcutChangeCallbacks$3(int userId, java.util.List changedList, java.lang.String packageName, android.os.UserHandle user, java.util.List removedList) {
        try {
            synchronized (this.mServiceLock) {
                if (isUserUnlockedL(userId)) {
                    java.util.ArrayList<android.content.pm.LauncherApps.ShortcutChangeCallback> copy = new java.util.ArrayList<>(this.mShortcutChangeCallbacks);
                    for (int i = copy.size() - 1; i >= 0; i--) {
                        if (!com.android.internal.util.CollectionUtils.isEmpty(changedList)) {
                            copy.get(i).onShortcutsAddedOrUpdated(packageName, changedList, user);
                        }
                        if (!com.android.internal.util.CollectionUtils.isEmpty(removedList)) {
                            copy.get(i).onShortcutsRemoved(packageName, removedList, user);
                        }
                    }
                }
            }
        } catch (java.lang.Exception e) {
        }
    }

    private java.util.List<android.content.pm.ShortcutInfo> removeNonKeyFields(java.util.List<android.content.pm.ShortcutInfo> shortcutInfos) {
        if (com.android.internal.util.CollectionUtils.isEmpty(shortcutInfos)) {
            return shortcutInfos;
        }
        int size = shortcutInfos.size();
        java.util.List<android.content.pm.ShortcutInfo> keyFieldOnlyShortcuts = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            android.content.pm.ShortcutInfo si = shortcutInfos.get(i);
            if (si.hasKeyFieldsOnly()) {
                keyFieldOnlyShortcuts.add(si);
            } else {
                keyFieldOnlyShortcuts.add(si.clone(4));
            }
        }
        return keyFieldOnlyShortcuts;
    }

    private void fixUpIncomingShortcutInfo(android.content.pm.ShortcutInfo shortcut, boolean forUpdate, boolean forPinRequest) {
        if (shortcut.isReturnedByServer()) {
            android.util.Log.w(TAG, "Re-publishing ShortcutInfo returned by server is not supported. Some information such as icon may lost from shortcut.");
        }
        java.util.Objects.requireNonNull(shortcut, "Null shortcut detected");
        if (shortcut.getActivity() != null) {
            com.android.internal.util.Preconditions.checkState(shortcut.getPackage().equals(shortcut.getActivity().getPackageName()), "Cannot publish shortcut: activity " + shortcut.getActivity() + " does not belong to package " + shortcut.getPackage());
            com.android.internal.util.Preconditions.checkState(injectIsMainActivity(shortcut.getActivity(), shortcut.getUserId()), "Cannot publish shortcut: activity " + shortcut.getActivity() + " is not main activity");
        }
        if (!forUpdate) {
            shortcut.enforceMandatoryFields(forPinRequest);
            if (!forPinRequest) {
                com.android.internal.util.Preconditions.checkState(shortcut.getActivity() != null, "Cannot publish shortcut: target activity is not set");
            }
        }
        if (shortcut.getIcon() != null) {
            android.content.pm.ShortcutInfo.validateIcon(shortcut.getIcon());
            validateIconURI(shortcut);
        }
        shortcut.replaceFlags(shortcut.getFlags() & 8192);
    }

    private void validateIconURI(android.content.pm.ShortcutInfo si) {
        int callingUid = injectBinderCallingUid();
        android.graphics.drawable.Icon icon = si.getIcon();
        if (icon == null) {
            return;
        }
        int iconType = icon.getType();
        if (iconType != 4 && iconType != 6) {
            return;
        }
        android.net.Uri uri = icon.getUri();
        this.mUriGrantsManagerInternal.checkGrantUriPermission(callingUid, si.getPackage(), android.content.ContentProvider.getUriWithoutUserId(uri), 1, android.content.ContentProvider.getUserIdFromUri(uri, android.os.UserHandle.getUserId(callingUid)));
    }

    private void fixUpIncomingShortcutInfo(android.content.pm.ShortcutInfo shortcut, boolean forUpdate) {
        fixUpIncomingShortcutInfo(shortcut, forUpdate, false);
    }

    public void validateShortcutForPinRequest(android.content.pm.ShortcutInfo shortcut) {
        fixUpIncomingShortcutInfo(shortcut, false, true);
    }

    private void fillInDefaultActivity(java.util.List<android.content.pm.ShortcutInfo> shortcuts) {
        android.content.ComponentName defaultActivity = null;
        for (int i = shortcuts.size() - 1; i >= 0; i--) {
            android.content.pm.ShortcutInfo si = shortcuts.get(i);
            if (si.getActivity() == null) {
                if (defaultActivity == null) {
                    defaultActivity = injectGetDefaultMainActivity(si.getPackage(), si.getUserId());
                    com.android.internal.util.Preconditions.checkState(defaultActivity != null, "Launcher activity not found for package " + si.getPackage());
                }
                si.setActivity(defaultActivity);
            }
        }
    }

    private void assignImplicitRanks(java.util.List<android.content.pm.ShortcutInfo> shortcuts) {
        for (int i = shortcuts.size() - 1; i >= 0; i--) {
            shortcuts.get(i).setImplicitRank(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.content.pm.ShortcutInfo> setReturnedByServer(java.util.List<android.content.pm.ShortcutInfo> shortcuts) {
        for (int i = shortcuts.size() - 1; i >= 0; i--) {
            shortcuts.get(i).setReturnedByServer();
        }
        return shortcuts;
    }

    public boolean setDynamicShortcuts(java.lang.String packageName, android.content.pm.ParceledListSlice shortcutInfoList, int userId) {
        verifyCaller(packageName, userId);
        boolean unlimited = injectHasUnlimitedShortcutsApiCallsPermission(injectBinderCallingPid(), injectBinderCallingUid());
        java.util.List<android.content.pm.ShortcutInfo> newShortcuts = shortcutInfoList.getList();
        verifyShortcutInfoPackages(packageName, newShortcuts);
        int size = newShortcuts.size();
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            com.android.server.pm.ShortcutPackage ps = getPackageShortcutsForPublisherLocked(packageName, userId);
            ps.ensureImmutableShortcutsNotIncluded(newShortcuts, true);
            ps.ensureNoBitmapIconIfShortcutIsLongLived(newShortcuts);
            fillInDefaultActivity(newShortcuts);
            ps.enforceShortcutCountsBeforeOperation(newShortcuts, 0);
            if (!ps.tryApiCall(unlimited)) {
                return false;
            }
            ps.clearAllImplicitRanks();
            assignImplicitRanks(newShortcuts);
            for (int i = 0; i < size; i++) {
                fixUpIncomingShortcutInfo(newShortcuts.get(i), false);
            }
            java.util.ArrayList<android.content.pm.ShortcutInfo> cachedOrPinned = new java.util.ArrayList<>();
            ps.findAll(cachedOrPinned, new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda14
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.pm.ShortcutService.lambda$setDynamicShortcuts$4((android.content.pm.ShortcutInfo) obj);
                }
            }, 4);
            java.util.List<android.content.pm.ShortcutInfo> removedShortcuts = ps.deleteAllDynamicShortcuts();
            for (int i2 = 0; i2 < size; i2++) {
                android.content.pm.ShortcutInfo newShortcut = newShortcuts.get(i2);
                ps.addOrReplaceDynamicShortcut(newShortcut);
            }
            ps.adjustRanks();
            java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = prepareChangedShortcuts(cachedOrPinned, newShortcuts, removedShortcuts, ps);
            packageShortcutsChanged(ps, changedShortcuts, removedShortcuts);
            verifyStates();
            return true;
        }
    }

    static /* synthetic */ boolean lambda$setDynamicShortcuts$4(android.content.pm.ShortcutInfo si) {
        return si.isVisibleToPublisher() && si.isDynamic() && (si.isCached() || si.isPinned());
    }

    public boolean updateShortcuts(java.lang.String packageName, android.content.pm.ParceledListSlice shortcutInfoList, int userId) {
        verifyCaller(packageName, userId);
        boolean unlimited = injectHasUnlimitedShortcutsApiCallsPermission(injectBinderCallingPid(), injectBinderCallingUid());
        java.util.List<android.content.pm.ShortcutInfo> newShortcuts = shortcutInfoList.getList();
        verifyShortcutInfoPackages(packageName, newShortcuts);
        int size = newShortcuts.size();
        final java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = new java.util.ArrayList<>(1);
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            final com.android.server.pm.ShortcutPackage ps = getPackageShortcutsForPublisherLocked(packageName, userId);
            ps.ensureImmutableShortcutsNotIncluded(newShortcuts, true);
            ps.ensureNoBitmapIconIfShortcutIsLongLived(newShortcuts);
            ps.ensureAllShortcutsVisibleToLauncher(newShortcuts);
            ps.enforceShortcutCountsBeforeOperation(newShortcuts, 2);
            if (!ps.tryApiCall(unlimited)) {
                return false;
            }
            ps.clearAllImplicitRanks();
            assignImplicitRanks(newShortcuts);
            for (int i = 0; i < size; i++) {
                final android.content.pm.ShortcutInfo source = newShortcuts.get(i);
                fixUpIncomingShortcutInfo(source, true);
                ps.mutateShortcut(source.getId(), null, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda17
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$updateShortcuts$5(source, ps, changedShortcuts, (android.content.pm.ShortcutInfo) obj);
                    }
                });
            }
            ps.adjustRanks();
            packageShortcutsChanged(ps, changedShortcuts.isEmpty() ? null : changedShortcuts, null);
            verifyStates();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateShortcuts$5(android.content.pm.ShortcutInfo source, com.android.server.pm.ShortcutPackage ps, java.util.List changedShortcuts, android.content.pm.ShortcutInfo target) {
        if (target == null || !target.isVisibleToPublisher()) {
            return;
        }
        if (target.isEnabled() != source.isEnabled()) {
            android.util.Slog.w(TAG, "ShortcutInfo.enabled cannot be changed with updateShortcuts()");
        }
        if (target.isLongLived() != source.isLongLived()) {
            android.util.Slog.w(TAG, "ShortcutInfo.longLived cannot be changed with updateShortcuts()");
        }
        if (source.hasRank()) {
            target.setRankChanged();
            target.setImplicitRank(source.getImplicitRank());
        }
        boolean replacingIcon = source.getIcon() != null;
        if (replacingIcon) {
            ps.removeIcon(target);
        }
        target.copyNonNullFieldsFrom(source);
        target.setTimestamp(injectCurrentTimeMillis());
        if (replacingIcon) {
            saveIconAndFixUpShortcutLocked(ps, target);
        }
        if (replacingIcon || source.hasStringResources()) {
            fixUpShortcutResourceNamesAndValues(target);
        }
        changedShortcuts.add(target);
    }

    public boolean addDynamicShortcuts(java.lang.String packageName, android.content.pm.ParceledListSlice shortcutInfoList, int userId) {
        verifyCaller(packageName, userId);
        boolean unlimited = injectHasUnlimitedShortcutsApiCallsPermission(injectBinderCallingPid(), injectBinderCallingUid());
        java.util.List<android.content.pm.ShortcutInfo> newShortcuts = shortcutInfoList.getList();
        verifyShortcutInfoPackages(packageName, newShortcuts);
        int size = newShortcuts.size();
        java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = null;
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            com.android.server.pm.ShortcutPackage ps = getPackageShortcutsForPublisherLocked(packageName, userId);
            ps.ensureImmutableShortcutsNotIncluded(newShortcuts, true);
            ps.ensureNoBitmapIconIfShortcutIsLongLived(newShortcuts);
            fillInDefaultActivity(newShortcuts);
            ps.enforceShortcutCountsBeforeOperation(newShortcuts, 1);
            ps.clearAllImplicitRanks();
            assignImplicitRanks(newShortcuts);
            if (!ps.tryApiCall(unlimited)) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                android.content.pm.ShortcutInfo newShortcut = newShortcuts.get(i);
                fixUpIncomingShortcutInfo(newShortcut, false);
                newShortcut.setRankChanged();
                ps.addOrReplaceDynamicShortcut(newShortcut);
                if (changedShortcuts == null) {
                    changedShortcuts = new java.util.ArrayList<>(1);
                }
                changedShortcuts.add(newShortcut);
            }
            ps.adjustRanks();
            packageShortcutsChanged(ps, changedShortcuts, null);
            verifyStates();
            return true;
        }
    }

    public void pushDynamicShortcut(java.lang.String packageName, android.content.pm.ShortcutInfo shortcut, int userId) {
        verifyCaller(packageName, userId);
        verifyShortcutInfoPackage(packageName, shortcut);
        java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = new java.util.ArrayList<>();
        java.util.List<android.content.pm.ShortcutInfo> removedShortcuts = null;
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            com.android.server.pm.ShortcutPackage ps = getPackageShortcutsForPublisherLocked(packageName, userId);
            ps.ensureNotImmutable(shortcut.getId(), true);
            fillInDefaultActivity(java.util.Arrays.asList(shortcut));
            if (!shortcut.hasRank()) {
                shortcut.setRank(0);
            }
            ps.clearAllImplicitRanks();
            shortcut.setImplicitRank(0);
            fixUpIncomingShortcutInfo(shortcut, false);
            shortcut.setRankChanged();
            boolean deleted = ps.pushDynamicShortcut(shortcut, changedShortcuts);
            if (deleted) {
                if (changedShortcuts.isEmpty()) {
                    return;
                }
                removedShortcuts = java.util.Collections.singletonList(changedShortcuts.get(0));
                changedShortcuts.clear();
            }
            changedShortcuts.add(shortcut);
            ps.adjustRanks();
            packageShortcutsChanged(ps, changedShortcuts, removedShortcuts);
            ps.reportShortcutUsed(this.mUsageStatsManagerInternal, shortcut.getId());
            verifyStates();
        }
    }

    public void requestPinShortcut(java.lang.String packageName, android.content.pm.ShortcutInfo shortcut, android.content.IntentSender resultIntent, int userId, com.android.internal.infra.AndroidFuture<java.lang.String> ret) {
        java.util.Objects.requireNonNull(shortcut);
        com.android.internal.util.Preconditions.checkArgument(shortcut.isEnabled(), "Shortcut must be enabled");
        com.android.internal.util.Preconditions.checkArgument(true ^ shortcut.isExcludedFromSurfaces(1), "Shortcut excluded from launcher cannot be pinned");
        ret.complete(java.lang.String.valueOf(requestPinItem(packageName, userId, shortcut, null, null, resultIntent)));
    }

    public void createShortcutResultIntent(java.lang.String packageName, android.content.pm.ShortcutInfo shortcut, int userId, com.android.internal.infra.AndroidFuture<android.content.Intent> ret) throws android.os.RemoteException {
        android.content.Intent intent;
        java.util.Objects.requireNonNull(shortcut);
        com.android.internal.util.Preconditions.checkArgument(shortcut.isEnabled(), "Shortcut must be enabled");
        verifyCaller(packageName, userId);
        verifyShortcutInfoPackage(packageName, shortcut);
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            intent = this.mShortcutRequestPinProcessor.createShortcutResultIntent(shortcut, userId);
        }
        verifyStates();
        ret.complete(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean requestPinItem(java.lang.String callingPackage, int userId, android.content.pm.ShortcutInfo shortcut, android.appwidget.AppWidgetProviderInfo appWidget, android.os.Bundle extras, android.content.IntentSender resultIntent) {
        return requestPinItem(callingPackage, userId, shortcut, appWidget, extras, resultIntent, injectBinderCallingPid(), injectBinderCallingUid());
    }

    private boolean requestPinItem(java.lang.String callingPackage, int userId, android.content.pm.ShortcutInfo shortcut, android.appwidget.AppWidgetProviderInfo appWidget, android.os.Bundle extras, android.content.IntentSender resultIntent, int callingPid, int callingUid) {
        boolean ret;
        if (!this.mShortcutServiceExt.adjustVerifyCallerInRequestPinItem(userId, callingPackage, shortcut)) {
            verifyCaller(callingPackage, userId);
        }
        if (shortcut == null || !injectHasAccessShortcutsPermission(callingPid, callingUid)) {
            verifyShortcutInfoPackage(callingPackage, shortcut);
        }
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            com.android.internal.util.Preconditions.checkState(isUidForegroundLocked(callingUid), "Calling application must have a foreground activity or a foreground service");
            if (shortcut != null) {
                java.lang.String shortcutPackage = shortcut.getPackage();
                com.android.server.pm.ShortcutPackage ps = getPackageShortcutsForPublisherLocked(shortcutPackage, userId);
                java.lang.String id = shortcut.getId();
                if (ps.isShortcutExistsAndInvisibleToPublisher(id)) {
                    ps.updateInvisibleShortcutForPinRequestWith(shortcut);
                    packageShortcutsChanged(ps, java.util.Collections.singletonList(shortcut), null);
                }
            }
            ret = this.mShortcutRequestPinProcessor.requestPinItemLocked(shortcut, appWidget, extras, userId, resultIntent);
        }
        verifyStates();
        return ret;
    }

    public void disableShortcuts(java.lang.String packageName, java.util.List shortcutIds, java.lang.CharSequence disabledMessage, int disabledMessageResId, int userId) {
        com.android.server.pm.ShortcutPackage ps;
        int i;
        verifyCaller(packageName, userId);
        java.util.Objects.requireNonNull(shortcutIds, "shortcutIds must be provided");
        java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = null;
        java.util.List<android.content.pm.ShortcutInfo> removedShortcuts = null;
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            ps = getPackageShortcutsForPublisherLocked(packageName, userId);
            ps.ensureImmutableShortcutsNotIncludedWithIds(shortcutIds, true);
            java.lang.String disabledMessageString = disabledMessage == null ? null : disabledMessage.toString();
            int i2 = shortcutIds.size() - 1;
            while (i2 >= 0) {
                java.lang.String id = (java.lang.String) com.android.internal.util.Preconditions.checkStringNotEmpty((java.lang.String) shortcutIds.get(i2));
                if (!ps.isShortcutExistsAndVisibleToPublisher(id)) {
                    i = i2;
                } else {
                    i = i2;
                    android.content.pm.ShortcutInfo deleted = ps.disableWithId(id, disabledMessageString, disabledMessageResId, false, true, 1);
                    if (deleted == null) {
                        if (changedShortcuts == null) {
                            changedShortcuts = new java.util.ArrayList<>(1);
                        }
                        changedShortcuts.add(ps.findShortcutById(id));
                    } else {
                        if (removedShortcuts == null) {
                            removedShortcuts = new java.util.ArrayList<>(1);
                        }
                        removedShortcuts.add(deleted);
                    }
                }
                i2 = i - 1;
            }
            ps.adjustRanks();
        }
        packageShortcutsChanged(ps, changedShortcuts, removedShortcuts);
        verifyStates();
    }

    public void enableShortcuts(java.lang.String packageName, java.util.List shortcutIds, int userId) {
        com.android.server.pm.ShortcutPackage ps;
        verifyCaller(packageName, userId);
        java.util.Objects.requireNonNull(shortcutIds, "shortcutIds must be provided");
        java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = null;
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            ps = getPackageShortcutsForPublisherLocked(packageName, userId);
            ps.ensureImmutableShortcutsNotIncludedWithIds(shortcutIds, true);
            for (int i = shortcutIds.size() - 1; i >= 0; i--) {
                java.lang.String id = (java.lang.String) com.android.internal.util.Preconditions.checkStringNotEmpty((java.lang.String) shortcutIds.get(i));
                if (ps.isShortcutExistsAndVisibleToPublisher(id)) {
                    ps.enableWithId(id);
                    if (changedShortcuts == null) {
                        changedShortcuts = new java.util.ArrayList<>(1);
                    }
                    changedShortcuts.add(ps.findShortcutById(id));
                }
            }
        }
        packageShortcutsChanged(ps, changedShortcuts, null);
        verifyStates();
    }

    public void removeDynamicShortcuts(java.lang.String packageName, java.util.List<java.lang.String> shortcutIds, int userId) {
        com.android.server.pm.ShortcutPackage ps;
        verifyCaller(packageName, userId);
        java.util.Objects.requireNonNull(shortcutIds, "shortcutIds must be provided");
        java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = null;
        java.util.List<android.content.pm.ShortcutInfo> removedShortcuts = null;
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            ps = getPackageShortcutsForPublisherLocked(packageName, userId);
            ps.ensureImmutableShortcutsNotIncludedWithIds(shortcutIds, true);
            for (int i = shortcutIds.size() - 1; i >= 0; i--) {
                java.lang.String id = (java.lang.String) com.android.internal.util.Preconditions.checkStringNotEmpty(shortcutIds.get(i));
                if (ps.isShortcutExistsAndVisibleToPublisher(id)) {
                    android.content.pm.ShortcutInfo removed = ps.deleteDynamicWithId(id, true, false);
                    if (removed == null) {
                        if (changedShortcuts == null) {
                            changedShortcuts = new java.util.ArrayList<>(1);
                        }
                        changedShortcuts.add(ps.findShortcutById(id));
                    } else {
                        if (removedShortcuts == null) {
                            removedShortcuts = new java.util.ArrayList<>(1);
                        }
                        removedShortcuts.add(removed);
                    }
                }
            }
            ps.adjustRanks();
        }
        packageShortcutsChanged(ps, changedShortcuts, removedShortcuts);
        verifyStates();
    }

    public void removeAllDynamicShortcuts(java.lang.String packageName, int userId) {
        com.android.server.pm.ShortcutPackage ps;
        java.util.List<android.content.pm.ShortcutInfo> removedShortcuts;
        java.util.List<android.content.pm.ShortcutInfo> changedShortcuts;
        verifyCaller(packageName, userId);
        java.util.List<android.content.pm.ShortcutInfo> changedShortcuts2 = new java.util.ArrayList<>();
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            ps = getPackageShortcutsForPublisherLocked(packageName, userId);
            ps.findAll(changedShortcuts2, new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda20
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.pm.ShortcutService.lambda$removeAllDynamicShortcuts$6((android.content.pm.ShortcutInfo) obj);
                }
            }, 4);
            removedShortcuts = ps.deleteAllDynamicShortcuts();
            changedShortcuts = prepareChangedShortcuts(changedShortcuts2, (java.util.List<android.content.pm.ShortcutInfo>) null, removedShortcuts, ps);
        }
        packageShortcutsChanged(ps, changedShortcuts, removedShortcuts);
        verifyStates();
    }

    static /* synthetic */ boolean lambda$removeAllDynamicShortcuts$6(android.content.pm.ShortcutInfo si) {
        return si.isVisibleToPublisher() && si.isDynamic() && (si.isCached() || si.isPinned());
    }

    public void removeLongLivedShortcuts(java.lang.String packageName, java.util.List shortcutIds, int userId) {
        com.android.server.pm.ShortcutPackage ps;
        verifyCaller(packageName, userId);
        java.util.Objects.requireNonNull(shortcutIds, "shortcutIds must be provided");
        java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = null;
        java.util.List<android.content.pm.ShortcutInfo> removedShortcuts = null;
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            ps = getPackageShortcutsForPublisherLocked(packageName, userId);
            ps.ensureImmutableShortcutsNotIncludedWithIds(shortcutIds, true);
            for (int i = shortcutIds.size() - 1; i >= 0; i--) {
                java.lang.String id = (java.lang.String) com.android.internal.util.Preconditions.checkStringNotEmpty((java.lang.String) shortcutIds.get(i));
                if (ps.isShortcutExistsAndVisibleToPublisher(id)) {
                    android.content.pm.ShortcutInfo removed = ps.deleteLongLivedWithId(id, true);
                    if (removed != null) {
                        if (removedShortcuts == null) {
                            removedShortcuts = new java.util.ArrayList<>(1);
                        }
                        removedShortcuts.add(removed);
                    } else {
                        if (changedShortcuts == null) {
                            changedShortcuts = new java.util.ArrayList<>(1);
                        }
                        changedShortcuts.add(ps.findShortcutById(id));
                    }
                }
            }
            ps.adjustRanks();
        }
        packageShortcutsChanged(ps, changedShortcuts, removedShortcuts);
        verifyStates();
    }

    public android.content.pm.ParceledListSlice<android.content.pm.ShortcutInfo> getShortcuts(java.lang.String packageName, int matchFlags, int userId) {
        android.content.pm.ParceledListSlice<android.content.pm.ShortcutInfo> shortcutsWithQueryLocked;
        verifyCaller(packageName, userId);
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            int i = 1;
            boolean matchDynamic = (matchFlags & 2) != 0;
            boolean matchPinned = (matchFlags & 4) != 0;
            boolean matchManifest = (matchFlags & 1) != 0;
            boolean matchCached = (matchFlags & 8) != 0;
            if (!matchDynamic) {
                i = 0;
            }
            final int shortcutFlags = i | (matchPinned ? 2 : 0) | (matchManifest ? 32 : 0) | (matchCached ? 1610629120 : 0);
            shortcutsWithQueryLocked = getShortcutsWithQueryLocked(packageName, userId, 9, new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda9
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.pm.ShortcutService.lambda$getShortcuts$7(shortcutFlags, (android.content.pm.ShortcutInfo) obj);
                }
            });
        }
        return shortcutsWithQueryLocked;
    }

    static /* synthetic */ boolean lambda$getShortcuts$7(int shortcutFlags, android.content.pm.ShortcutInfo si) {
        return si.isVisibleToPublisher() && (si.getFlags() & shortcutFlags) != 0;
    }

    public android.content.pm.ParceledListSlice getShareTargets(java.lang.String packageName, final android.content.IntentFilter filter, int userId) {
        android.content.pm.ParceledListSlice parceledListSlice;
        com.android.internal.util.Preconditions.checkStringNotEmpty(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        java.util.Objects.requireNonNull(filter, "intentFilter");
        if (!isCallerChooserActivity()) {
            verifyCaller(packageName, userId);
        }
        enforceCallingOrSelfPermission("android.permission.MANAGE_APP_PREDICTIONS", "getShareTargets");
        android.content.ComponentName chooser = injectChooserActivity();
        final java.lang.String pkg = chooser != null ? chooser.getPackageName() : this.mContext.getPackageName();
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            final java.util.List<android.content.pm.ShortcutManager.ShareShortcutInfo> shortcutInfoList = new java.util.ArrayList<>();
            com.android.server.pm.ShortcutUser user = getUserShortcutsLocked(userId);
            user.forAllPackages(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda22
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    shortcutInfoList.addAll(((com.android.server.pm.ShortcutPackage) obj).getMatchingShareTargets(filter, pkg));
                }
            });
            parceledListSlice = new android.content.pm.ParceledListSlice(shortcutInfoList);
        }
        return parceledListSlice;
    }

    public boolean hasShareTargets(java.lang.String packageName, java.lang.String packageToCheck, int userId) {
        boolean zHasShareTargets;
        verifyCaller(packageName, userId);
        enforceCallingOrSelfPermission("android.permission.MANAGE_APP_PREDICTIONS", "hasShareTargets");
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            zHasShareTargets = getPackageShortcutsLocked(packageToCheck, userId).hasShareTargets();
        }
        return zHasShareTargets;
    }

    public boolean isSharingShortcut(int callingUserId, java.lang.String callingPackage, java.lang.String packageName, java.lang.String shortcutId, int userId, android.content.IntentFilter filter) {
        verifyCaller(callingPackage, callingUserId);
        enforceCallingOrSelfPermission("android.permission.MANAGE_APP_PREDICTIONS", "isSharingShortcut");
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            throwIfUserLockedL(callingUserId);
            java.util.List<android.content.pm.ShortcutManager.ShareShortcutInfo> matchedTargets = getPackageShortcutsLocked(packageName, userId).getMatchingShareTargets(filter);
            int matchedSize = matchedTargets.size();
            for (int i = 0; i < matchedSize; i++) {
                if (matchedTargets.get(i).getShortcutInfo().getId().equals(shortcutId)) {
                    return true;
                }
            }
            return false;
        }
    }

    private android.content.pm.ParceledListSlice<android.content.pm.ShortcutInfo> getShortcutsWithQueryLocked(java.lang.String packageName, int userId, int cloneFlags, java.util.function.Predicate<android.content.pm.ShortcutInfo> filter) {
        java.util.ArrayList<android.content.pm.ShortcutInfo> ret = new java.util.ArrayList<>();
        com.android.server.pm.ShortcutPackage ps = getPackageShortcutsForPublisherLocked(packageName, userId);
        ps.findAll(ret, filter, cloneFlags);
        return new android.content.pm.ParceledListSlice<>(setReturnedByServer(ret));
    }

    public int getMaxShortcutCountPerActivity(java.lang.String packageName, int userId) throws android.os.RemoteException {
        verifyCaller(packageName, userId);
        return this.mMaxShortcuts;
    }

    public int getRemainingCallCount(java.lang.String packageName, int userId) {
        int apiCallCount;
        verifyCaller(packageName, userId);
        boolean unlimited = injectHasUnlimitedShortcutsApiCallsPermission(injectBinderCallingPid(), injectBinderCallingUid());
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            com.android.server.pm.ShortcutPackage ps = getPackageShortcutsForPublisherLocked(packageName, userId);
            apiCallCount = this.mMaxUpdatesPerInterval - ps.getApiCallCount(unlimited);
        }
        return apiCallCount;
    }

    public long getRateLimitResetTime(java.lang.String packageName, int userId) {
        long nextResetTimeLocked;
        verifyCaller(packageName, userId);
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            nextResetTimeLocked = getNextResetTimeLocked();
        }
        return nextResetTimeLocked;
    }

    public int getIconMaxDimensions(java.lang.String packageName, int userId) {
        int i;
        verifyCaller(packageName, userId);
        synchronized (this.mServiceLock) {
            i = this.mMaxIconDimension;
        }
        return i;
    }

    public void reportShortcutUsed(java.lang.String packageName, java.lang.String shortcutId, int userId) {
        verifyCaller(packageName, userId);
        java.util.Objects.requireNonNull(shortcutId);
        if (DEBUG) {
            android.util.Slog.d(TAG, java.lang.String.format("reportShortcutUsed: Shortcut %s package %s used on user %d", shortcutId, packageName, java.lang.Integer.valueOf(userId)));
        }
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            com.android.server.pm.ShortcutPackage ps = getPackageShortcutsForPublisherLocked(packageName, userId);
            if (ps.findShortcutById(shortcutId) == null) {
                android.util.Log.w(TAG, java.lang.String.format("reportShortcutUsed: package %s doesn't have shortcut %s", packageName, shortcutId));
            } else {
                ps.reportShortcutUsed(this.mUsageStatsManagerInternal, shortcutId);
            }
        }
    }

    public boolean isRequestPinItemSupported(int callingUserId, int requestType) {
        verifyCallerUserId(callingUserId);
        long token = injectClearCallingIdentity();
        try {
            return this.mShortcutRequestPinProcessor.isRequestPinItemSupported(callingUserId, requestType);
        } finally {
            injectRestoreCallingIdentity(token);
        }
    }

    public void resetThrottling() {
        enforceSystemOrShell();
        resetThrottlingInner(getCallingUserId());
    }

    void resetThrottlingInner(int userId) {
        synchronized (this.mServiceLock) {
            if (!isUserUnlockedL(userId)) {
                android.util.Log.w(TAG, "User " + userId + " is locked or not running");
                return;
            }
            getUserShortcutsLocked(userId).resetThrottling();
            scheduleSaveUser(userId);
            android.util.Slog.i(TAG, "ShortcutManager: throttling counter reset for user " + userId);
        }
    }

    void resetAllThrottlingInner() {
        this.mRawLastResetTime.set(injectCurrentTimeMillis());
        scheduleSaveBaseState();
        android.util.Slog.i(TAG, "ShortcutManager: throttling counter reset for all users");
    }

    public void onApplicationActive(java.lang.String packageName, int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onApplicationActive: package=" + packageName + "  userid=" + userId);
        }
        enforceResetThrottlingPermission();
        synchronized (this.mServiceLock) {
            if (isUserUnlockedL(userId)) {
                getPackageShortcutsLocked(packageName, userId).resetRateLimitingForCommandLineNoSaving();
                saveUser(userId);
            }
        }
    }

    boolean hasShortcutHostPermission(java.lang.String callingPackage, int userId, int callingPid, int callingUid) {
        if (canSeeAnyPinnedShortcut(callingPackage, userId, callingPid, callingUid)) {
            return true;
        }
        long start = getStatStartTime();
        try {
            return hasShortcutHostPermissionInner(callingPackage, userId);
        } finally {
            logDurationStat(4, start);
        }
    }

    boolean canSeeAnyPinnedShortcut(java.lang.String callingPackage, int userId, int callingPid, int callingUid) {
        boolean zHasHostPackage;
        if (injectHasAccessShortcutsPermission(callingPid, callingUid)) {
            return true;
        }
        synchronized (this.mNonPersistentUsersLock) {
            zHasHostPackage = getNonPersistentUserLocked(userId).hasHostPackage(callingPackage);
        }
        return zHasHostPackage;
    }

    boolean injectHasAccessShortcutsPermission(int callingPid, int callingUid) {
        return this.mContext.checkPermission("android.permission.ACCESS_SHORTCUTS", callingPid, callingUid) == 0;
    }

    boolean injectHasUnlimitedShortcutsApiCallsPermission(int callingPid, int callingUid) {
        return this.mContext.checkPermission("android.permission.UNLIMITED_SHORTCUTS_API_CALLS", callingPid, callingUid) == 0;
    }

    boolean hasShortcutHostPermissionInner(java.lang.String packageName, int userId) {
        synchronized (this.mServiceLock) {
            throwIfUserLockedL(userId);
            java.lang.String defaultLauncher = getDefaultLauncher(userId);
            if (defaultLauncher == null) {
                return false;
            }
            if (DEBUG) {
                android.util.Slog.v(TAG, "Detected launcher: " + defaultLauncher + " user: " + userId);
            }
            return defaultLauncher.equals(packageName);
        }
    }

    boolean areShortcutsSupportedOnHomeScreen(int userId) {
        boolean isSupported = true;
        if (!com.android.internal.hidden_from_bootclasspath.android.os.Flags.allowPrivateProfile() || !android.multiuser.Flags.disablePrivateSpaceItemsOnHome() || !android.multiuser.Flags.enablePrivateSpaceFeatures()) {
            return true;
        }
        long start = getStatStartTime();
        long token = injectClearCallingIdentity();
        try {
            synchronized (this.mServiceLock) {
                android.content.pm.UserProperties userProperties = this.mUserManagerInternal.getUserProperties(userId);
                if (userProperties == null || userProperties.areItemsRestrictedOnHomeScreen()) {
                    isSupported = false;
                }
            }
            return isSupported;
        } finally {
            injectRestoreCallingIdentity(token);
            logDurationStat(16, start);
        }
    }

    java.lang.String getDefaultLauncher(int userId) {
        long start = getStatStartTime();
        long token = injectClearCallingIdentity();
        try {
            synchronized (this.mServiceLock) {
                throwIfUserLockedL(userId);
                com.android.server.pm.ShortcutUser user = getUserShortcutsLocked(userId);
                java.lang.String cachedLauncher = user.getCachedLauncher();
                if (cachedLauncher != null) {
                    return cachedLauncher;
                }
                long startGetHomeRoleHoldersAsUser = getStatStartTime();
                java.lang.String defaultLauncher = injectGetHomeRoleHolderAsUser(getParentOrSelfUserId(userId));
                logDurationStat(0, startGetHomeRoleHoldersAsUser);
                if (defaultLauncher != null) {
                    if (DEBUG) {
                        android.util.Slog.v(TAG, "Default launcher from RoleManager: " + defaultLauncher + " user: " + userId);
                    }
                    user.setCachedLauncher(defaultLauncher);
                } else {
                    android.util.Slog.e(TAG, "Default launcher not found. user: " + userId);
                }
                return defaultLauncher;
            }
        } finally {
            injectRestoreCallingIdentity(token);
            logDurationStat(16, start);
        }
    }

    public void setShortcutHostPackage(java.lang.String type, java.lang.String packageName, int userId) {
        synchronized (this.mNonPersistentUsersLock) {
            getNonPersistentUserLocked(userId).setShortcutHostPackage(type, packageName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanUpPackageForAllLoadedUsers(final java.lang.String packageName, final int packageUserId, final boolean appStillExists) {
        synchronized (this.mServiceLock) {
            forEachLoadedUserLocked(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) throws java.lang.Exception {
                    this.f$0.lambda$cleanUpPackageForAllLoadedUsers$9(packageName, packageUserId, appStillExists, (com.android.server.pm.ShortcutUser) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanUpPackageForAllLoadedUsers$9(java.lang.String packageName, int packageUserId, boolean appStillExists, com.android.server.pm.ShortcutUser user) throws java.lang.Exception {
        cleanUpPackageLocked(packageName, user.getUserId(), packageUserId, appStillExists);
    }

    void cleanUpPackageLocked(final java.lang.String packageName, int owningUserId, final int packageUserId, boolean appStillExists) throws java.lang.Exception {
        final com.android.server.pm.ShortcutPackage sp;
        boolean wasUserLoaded = isUserLoadedLocked(owningUserId);
        com.android.server.pm.ShortcutUser user = getUserShortcutsLocked(owningUserId);
        boolean doNotify = false;
        if (packageUserId != owningUserId) {
            sp = null;
        } else {
            sp = user.removePackage(packageName);
        }
        if (sp != null) {
            doNotify = true;
        }
        user.removeLauncher(packageUserId, packageName);
        user.forAllLaunchers(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.pm.ShortcutLauncher) obj).cleanUpPackage(packageName, packageUserId);
            }
        });
        user.forAllPackages(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.pm.ShortcutPackage) obj).refreshPinnedFlags();
            }
        });
        if (doNotify) {
            notifyListeners(packageName, owningUserId);
            sp.refreshPinnedFlags();
            packageShortcutsChanged(sp, null, null);
        }
        if (appStillExists && packageUserId == owningUserId) {
            user.rescanPackageIfNeeded(packageName, true);
        }
        if (!appStillExists && packageUserId == owningUserId && sp != null) {
            injectPostToHandler(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    sp.removeShortcutPackageItem();
                }
            });
        }
        if (!wasUserLoaded) {
            unloadUserLocked(owningUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class LocalService extends android.content.pm.ShortcutServiceInternal {
        private LocalService() {
        }

        public java.util.List<android.content.pm.ShortcutInfo> getShortcuts(final int launcherUserId, final java.lang.String callingPackage, final long changedSince, java.lang.String packageName, java.util.List<java.lang.String> shortcutIds, final java.util.List<android.content.LocusId> locusIds, final android.content.ComponentName componentName, final int queryFlags, final int userId, final int callingPid, final int callingUid) throws java.lang.Throwable {
            java.util.List<java.lang.String> shortcutIds2;
            final java.util.ArrayList<android.content.pm.ShortcutInfo> ret;
            com.android.server.pm.ShortcutService.LocalService localService;
            android.util.Slog.d(com.android.server.pm.ShortcutService.TAG, "Getting shortcuts for launcher= " + callingPackage + "user=" + userId + " pkg=" + packageName);
            java.util.ArrayList<android.content.pm.ShortcutInfo> ret2 = new java.util.ArrayList<>();
            int flags = 27;
            if ((queryFlags & 4) != 0) {
                flags = 4;
            } else if ((queryFlags & 2048) != 0) {
                flags = 27 & (-17);
            }
            final int flags2 = com.android.server.pm.ShortcutService.this.mShortcutWrapper.getExtImpl().adjustFlagsIfNeed(callingPackage, flags, queryFlags, callingUid, userId);
            if (packageName != null) {
                shortcutIds2 = shortcutIds;
            } else {
                shortcutIds2 = null;
            }
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                try {
                    com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                    com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                    com.android.server.pm.ShortcutService.this.getLauncherShortcutsLocked(callingPackage, userId, launcherUserId).attemptToRestoreIfNeededAndSave();
                    if (packageName != null) {
                        ret = ret2;
                        try {
                            getShortcutsInnerLocked(launcherUserId, callingPackage, packageName, shortcutIds2, locusIds, changedSince, componentName, queryFlags, userId, ret, flags2, callingPid, callingUid);
                            localService = this;
                            return com.android.server.pm.ShortcutService.this.setReturnedByServer(ret);
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
                    } else {
                        ret = ret2;
                        final java.util.List<java.lang.String> shortcutIdsF = shortcutIds2;
                        localService = this;
                        try {
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                            while (true) {
                                throw th;
                            }
                        }
                        try {
                            com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).forAllPackages(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$LocalService$$ExternalSyntheticLambda8
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) throws java.lang.Exception {
                                    this.f$0.lambda$getShortcuts$0(launcherUserId, callingPackage, shortcutIdsF, locusIds, changedSince, componentName, queryFlags, userId, ret, flags2, callingPid, callingUid, (com.android.server.pm.ShortcutPackage) obj);
                                }
                            });
                            return com.android.server.pm.ShortcutService.this.setReturnedByServer(ret);
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                            while (true) {
                                throw th;
                            }
                        }
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getShortcuts$0(int launcherUserId, java.lang.String callingPackage, java.util.List shortcutIdsF, java.util.List locusIdsF, long changedSince, android.content.ComponentName componentName, int queryFlags, int userId, java.util.ArrayList ret, int cloneFlag, int callingPid, int callingUid, com.android.server.pm.ShortcutPackage p) throws java.lang.Exception {
            getShortcutsInnerLocked(launcherUserId, callingPackage, p.getPackageName(), shortcutIdsF, locusIdsF, changedSince, componentName, queryFlags, userId, ret, cloneFlag, callingPid, callingUid);
        }

        private void getShortcutsInnerLocked(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.util.List<java.lang.String> shortcutIds, java.util.List<android.content.LocusId> locusIds, long changedSince, android.content.ComponentName componentName, int queryFlags, int userId, java.util.ArrayList<android.content.pm.ShortcutInfo> ret, int cloneFlag, int callingPid, int callingUid) throws java.lang.Exception {
            android.util.ArraySet<java.lang.String> ids = shortcutIds == null ? null : new android.util.ArraySet<>(shortcutIds);
            com.android.server.pm.ShortcutUser user = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId);
            com.android.server.pm.ShortcutPackage p = user.getPackageShortcutsIfExists(packageName);
            if (p == null) {
                android.util.Log.d(com.android.server.pm.ShortcutService.TAG, "getShortcutsInnerLocked() returned empty results because " + packageName + " isn't loaded");
                return;
            }
            boolean canAccessAllShortcuts = com.android.server.pm.ShortcutService.this.canSeeAnyPinnedShortcut(callingPackage, launcherUserId, callingPid, callingUid);
            boolean getPinnedByAnyLauncher = canAccessAllShortcuts && (queryFlags & 1024) != 0;
            java.util.function.Predicate<android.content.pm.ShortcutInfo> filter = getFilterFromQuery(ids, locusIds, changedSince, componentName, queryFlags | (getPinnedByAnyLauncher ? 2 : 0), getPinnedByAnyLauncher);
            p.findAll(ret, filter, cloneFlag, callingPackage, launcherUserId, getPinnedByAnyLauncher);
        }

        private java.util.function.Predicate<android.content.pm.ShortcutInfo> getFilterFromQuery(final android.util.ArraySet<java.lang.String> ids, java.util.List<android.content.LocusId> locusIds, final long changedSince, final android.content.ComponentName componentName, int queryFlags, final boolean getPinnedByAnyLauncher) {
            final android.util.ArraySet<android.content.LocusId> locIds = locusIds == null ? null : new android.util.ArraySet<>(locusIds);
            final boolean matchDynamic = (queryFlags & 1) != 0;
            final boolean matchPinned = (queryFlags & 2) != 0;
            final boolean matchManifest = (queryFlags & 8) != 0;
            final boolean matchCached = (queryFlags & 16) != 0;
            return new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutService$LocalService$$ExternalSyntheticLambda9
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.pm.ShortcutService.LocalService.lambda$getFilterFromQuery$1(changedSince, ids, locIds, componentName, matchDynamic, matchPinned, getPinnedByAnyLauncher, matchManifest, matchCached, (android.content.pm.ShortcutInfo) obj);
                }
            };
        }

        static /* synthetic */ boolean lambda$getFilterFromQuery$1(long changedSince, android.util.ArraySet ids, android.util.ArraySet locIds, android.content.ComponentName componentName, boolean matchDynamic, boolean matchPinned, boolean getPinnedByAnyLauncher, boolean matchManifest, boolean matchCached, android.content.pm.ShortcutInfo si) {
            if (si.getLastChangedTimestamp() < changedSince) {
                return false;
            }
            if (ids != null && !ids.contains(si.getId())) {
                return false;
            }
            if (locIds != null && !locIds.contains(si.getLocusId())) {
                return false;
            }
            if (componentName != null && si.getActivity() != null && !si.getActivity().equals(componentName)) {
                return false;
            }
            if (matchDynamic && si.isDynamic()) {
                return true;
            }
            if ((matchPinned || getPinnedByAnyLauncher) && si.isPinned()) {
                return true;
            }
            if (matchManifest && si.isDeclaredInManifest()) {
                return true;
            }
            return matchCached && si.isCached();
        }

        public void getShortcutsAsync(int launcherUserId, java.lang.String callingPackage, long changedSince, java.lang.String packageName, java.util.List<java.lang.String> shortcutIds, java.util.List<android.content.LocusId> locusIds, android.content.ComponentName componentName, int queryFlags, int userId, int callingPid, int callingUid, final com.android.internal.infra.AndroidFuture<java.util.List<android.content.pm.ShortcutInfo>> cb) throws java.lang.Throwable {
            final java.util.List<android.content.pm.ShortcutInfo> ret = getShortcuts(launcherUserId, callingPackage, changedSince, packageName, shortcutIds, locusIds, componentName, queryFlags, userId, callingPid, callingUid);
            if (shortcutIds == null || packageName == null || ret.size() >= shortcutIds.size()) {
                cb.complete(ret);
                return;
            }
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                try {
                    try {
                        com.android.server.pm.ShortcutPackage p = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).getPackageShortcutsIfExists(packageName);
                        if (p == null) {
                            cb.complete(ret);
                            return;
                        }
                        final android.util.ArraySet<java.lang.String> ids = new android.util.ArraySet<>(shortcutIds);
                        java.util.List list = (java.util.List) ret.stream().map(new com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda34()).collect(java.util.stream.Collectors.toList());
                        java.util.Objects.requireNonNull(ids);
                        list.forEach(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$LocalService$$ExternalSyntheticLambda5
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                ids.remove((java.lang.String) obj);
                            }
                        });
                        int flags = 27;
                        if ((queryFlags & 4) != 0) {
                            flags = 4;
                        } else if ((queryFlags & 2048) != 0) {
                            flags = 27 & (-17);
                        }
                        final int cloneFlag = flags;
                        p.getShortcutByIdsAsync(ids, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$LocalService$$ExternalSyntheticLambda6
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                com.android.server.pm.ShortcutService.LocalService.lambda$getShortcutsAsync$3(cloneFlag, ret, cb, (java.util.List) obj);
                            }
                        });
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }

        static /* synthetic */ void lambda$getShortcutsAsync$3(final int cloneFlag, java.util.List ret, com.android.internal.infra.AndroidFuture cb, java.util.List shortcuts) {
            if (shortcuts != null) {
                java.util.stream.Stream map = shortcuts.stream().map(new java.util.function.Function() { // from class: com.android.server.pm.ShortcutService$LocalService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return ((android.content.pm.ShortcutInfo) obj).clone(cloneFlag);
                    }
                });
                java.util.Objects.requireNonNull(ret);
                map.forEach(new com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda44(ret));
            }
            cb.complete(ret);
        }

        public boolean isPinnedByCaller(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.lang.String shortcutId, int userId) {
            boolean z;
            com.android.internal.util.Preconditions.checkStringNotEmpty(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            com.android.internal.util.Preconditions.checkStringNotEmpty(shortcutId, "shortcutId");
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                com.android.server.pm.ShortcutService.this.getLauncherShortcutsLocked(callingPackage, userId, launcherUserId).attemptToRestoreIfNeededAndSave();
                android.content.pm.ShortcutInfo si = getShortcutInfoLocked(launcherUserId, callingPackage, packageName, shortcutId, userId, false);
                z = si != null && si.isPinned();
            }
            return z;
        }

        private android.content.pm.ShortcutInfo getShortcutInfoLocked(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, final java.lang.String shortcutId, int userId, boolean getPinnedByAnyLauncher) {
            com.android.internal.util.Preconditions.checkStringNotEmpty(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            com.android.internal.util.Preconditions.checkStringNotEmpty(shortcutId, "shortcutId");
            com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
            com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
            com.android.server.pm.ShortcutPackage p = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).getPackageShortcutsIfExists(packageName);
            if (p == null) {
                return null;
            }
            java.util.ArrayList<android.content.pm.ShortcutInfo> list = new java.util.ArrayList<>(1);
            p.findAll(list, new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutService$LocalService$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return shortcutId.equals(((android.content.pm.ShortcutInfo) obj).getId());
                }
            }, 0, callingPackage, launcherUserId, getPinnedByAnyLauncher);
            if (list.size() == 0) {
                return null;
            }
            return list.get(0);
        }

        private void getShortcutInfoAsync(int launcherUserId, java.lang.String packageName, java.lang.String shortcutId, int userId, final java.util.function.Consumer<android.content.pm.ShortcutInfo> cb) {
            com.android.server.pm.ShortcutPackage p;
            com.android.internal.util.Preconditions.checkStringNotEmpty(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            com.android.internal.util.Preconditions.checkStringNotEmpty(shortcutId, "shortcutId");
            com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
            com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                p = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).getPackageShortcutsIfExists(packageName);
            }
            if (p == null) {
                cb.accept(null);
            } else {
                p.getShortcutByIdsAsync(java.util.Collections.singleton(shortcutId), new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$LocalService$$ExternalSyntheticLambda7
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        java.util.List list = (java.util.List) obj;
                        cb.accept((list == null || list.isEmpty()) ? null : (android.content.pm.ShortcutInfo) list.get(0));
                    }
                });
            }
        }

        public void pinShortcuts(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.util.List<java.lang.String> shortcutIds, int userId) throws java.lang.Throwable {
            com.android.server.pm.ShortcutPackage sp;
            com.android.internal.util.Preconditions.checkStringNotEmpty(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            java.util.Objects.requireNonNull(shortcutIds, "shortcutIds");
            java.util.List<android.content.pm.ShortcutInfo> removedShortcuts = null;
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                try {
                    try {
                        com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                        com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                        com.android.server.pm.ShortcutLauncher launcher = com.android.server.pm.ShortcutService.this.getLauncherShortcutsLocked(callingPackage, userId, launcherUserId);
                        launcher.attemptToRestoreIfNeededAndSave();
                        com.android.server.pm.ShortcutPackage sp2 = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).getPackageShortcutsIfExists(packageName);
                        if (sp2 == null) {
                            sp = sp2;
                        } else {
                            java.util.List<android.content.pm.ShortcutInfo> removedShortcuts2 = new java.util.ArrayList<>();
                            try {
                                sp = sp2;
                                sp2.findAll(removedShortcuts2, new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutService$LocalService$$ExternalSyntheticLambda0
                                    @Override // java.util.function.Predicate
                                    public final boolean test(java.lang.Object obj) {
                                        return com.android.server.pm.ShortcutService.LocalService.lambda$pinShortcuts$6((android.content.pm.ShortcutInfo) obj);
                                    }
                                }, 4, callingPackage, launcherUserId, false);
                                removedShortcuts = removedShortcuts2;
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                        android.util.ArraySet<java.lang.String> oldPinnedIds = launcher.getPinnedShortcutIds(packageName, userId);
                        launcher.pinShortcuts(userId, packageName, shortcutIds, false);
                        if (oldPinnedIds != null && removedShortcuts != null) {
                            for (int i = 0; i < removedShortcuts.size(); i++) {
                                oldPinnedIds.remove(removedShortcuts.get(i).getId());
                            }
                        }
                        java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = com.android.server.pm.ShortcutService.this.prepareChangedShortcuts(oldPinnedIds, (android.util.ArraySet<java.lang.String>) new android.util.ArraySet(shortcutIds), removedShortcuts, sp);
                        if (sp != null) {
                            com.android.server.pm.ShortcutService.this.packageShortcutsChanged(sp, changedShortcuts, removedShortcuts);
                        }
                        com.android.server.pm.ShortcutService.this.verifyStates();
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            }
        }

        static /* synthetic */ boolean lambda$pinShortcuts$6(android.content.pm.ShortcutInfo si) {
            return (!si.isVisibleToPublisher() || !si.isPinned() || si.isCached() || si.isDynamic() || si.isDeclaredInManifest()) ? false : true;
        }

        public void cacheShortcuts(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.util.List<java.lang.String> shortcutIds, int userId, int cacheFlags) throws java.lang.Throwable {
            updateCachedShortcutsInternal(launcherUserId, callingPackage, packageName, shortcutIds, userId, cacheFlags, true);
        }

        public void uncacheShortcuts(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.util.List<java.lang.String> shortcutIds, int userId, int cacheFlags) throws java.lang.Throwable {
            updateCachedShortcutsInternal(launcherUserId, callingPackage, packageName, shortcutIds, userId, cacheFlags, false);
        }

        public java.util.List<android.content.pm.ShortcutManager.ShareShortcutInfo> getShareTargets(java.lang.String callingPackage, android.content.IntentFilter intentFilter, int userId) {
            return com.android.server.pm.ShortcutService.this.getShareTargets(callingPackage, intentFilter, userId).getList();
        }

        public boolean isSharingShortcut(int callingUserId, java.lang.String callingPackage, java.lang.String packageName, java.lang.String shortcutId, int userId, android.content.IntentFilter filter) {
            com.android.internal.util.Preconditions.checkStringNotEmpty(callingPackage, "callingPackage");
            com.android.internal.util.Preconditions.checkStringNotEmpty(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            com.android.internal.util.Preconditions.checkStringNotEmpty(shortcutId, "shortcutId");
            return com.android.server.pm.ShortcutService.this.isSharingShortcut(callingUserId, callingPackage, packageName, shortcutId, userId, filter);
        }

        private void updateCachedShortcutsInternal(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.util.List<java.lang.String> shortcutIds, int userId, int cacheFlags, boolean doCache) throws java.lang.Throwable {
            int idSize;
            com.android.internal.util.Preconditions.checkStringNotEmpty(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            java.util.Objects.requireNonNull(shortcutIds, "shortcutIds");
            com.android.internal.util.Preconditions.checkState((1610629120 & cacheFlags) != 0, "invalid cacheFlags");
            java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = null;
            java.util.List<android.content.pm.ShortcutInfo> removedShortcuts = null;
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                try {
                    try {
                        com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                        com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                        int idSize2 = shortcutIds.size();
                        com.android.server.pm.ShortcutPackage sp = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).getPackageShortcutsIfExists(packageName);
                        if (idSize2 == 0 || sp == null) {
                            return;
                        }
                        int i = 0;
                        while (i < idSize2) {
                            java.lang.String id = (java.lang.String) com.android.internal.util.Preconditions.checkStringNotEmpty(shortcutIds.get(i));
                            android.content.pm.ShortcutInfo si = sp.findShortcutById(id);
                            if (si == null) {
                                idSize = idSize2;
                            } else if (doCache == si.hasFlags(cacheFlags)) {
                                idSize = idSize2;
                            } else if (doCache) {
                                if (si.isLongLived()) {
                                    si.addFlags(cacheFlags);
                                    if (changedShortcuts != null) {
                                        idSize = idSize2;
                                    } else {
                                        idSize = idSize2;
                                        changedShortcuts = new java.util.ArrayList<>(1);
                                    }
                                    changedShortcuts.add(si);
                                } else {
                                    idSize = idSize2;
                                    android.util.Log.w(com.android.server.pm.ShortcutService.TAG, "Only long lived shortcuts can get cached. Ignoring id " + si.getId());
                                }
                            } else {
                                idSize = idSize2;
                                android.content.pm.ShortcutInfo removed = null;
                                si.clearFlags(cacheFlags);
                                if (!si.isDynamic() && !si.isCached()) {
                                    removed = sp.deleteLongLivedWithId(id, true);
                                }
                                if (removed == null) {
                                    if (changedShortcuts == null) {
                                        changedShortcuts = new java.util.ArrayList<>(1);
                                    }
                                    changedShortcuts.add(si);
                                } else {
                                    if (removedShortcuts == null) {
                                        removedShortcuts = new java.util.ArrayList<>(1);
                                    }
                                    removedShortcuts.add(removed);
                                }
                            }
                            i++;
                            idSize2 = idSize;
                        }
                        com.android.server.pm.ShortcutService.this.packageShortcutsChanged(sp, changedShortcuts, removedShortcuts);
                        com.android.server.pm.ShortcutService.this.verifyStates();
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }

        public android.content.Intent[] createShortcutIntents(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.lang.String shortcutId, int userId, int callingPid, int callingUid) throws java.lang.Throwable {
            com.android.internal.util.Preconditions.checkStringNotEmpty(packageName, "packageName can't be empty");
            com.android.internal.util.Preconditions.checkStringNotEmpty(shortcutId, "shortcutId can't be empty");
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                try {
                    try {
                        com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                        com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                        com.android.server.pm.ShortcutService.this.getLauncherShortcutsLocked(callingPackage, userId, launcherUserId).attemptToRestoreIfNeededAndSave();
                        boolean getPinnedByAnyLauncher = com.android.server.pm.ShortcutService.this.canSeeAnyPinnedShortcut(callingPackage, launcherUserId, callingPid, callingUid);
                        android.content.pm.ShortcutInfo si = getShortcutInfoLocked(launcherUserId, callingPackage, packageName, shortcutId, userId, getPinnedByAnyLauncher);
                        if (si != null && si.isEnabled() && (si.isAlive() || getPinnedByAnyLauncher)) {
                            return si.getIntents();
                        }
                        android.util.Log.e(com.android.server.pm.ShortcutService.TAG, "Shortcut " + shortcutId + " does not exist or disabled");
                        return null;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }

        public void createShortcutIntentsAsync(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.lang.String shortcutId, int userId, int callingPid, int callingUid, final com.android.internal.infra.AndroidFuture<android.content.Intent[]> cb) {
            com.android.internal.util.Preconditions.checkStringNotEmpty(packageName, "packageName can't be empty");
            com.android.internal.util.Preconditions.checkStringNotEmpty(shortcutId, "shortcutId can't be empty");
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                com.android.server.pm.ShortcutService.this.getLauncherShortcutsLocked(callingPackage, userId, launcherUserId).attemptToRestoreIfNeededAndSave();
                boolean getPinnedByAnyLauncher = com.android.server.pm.ShortcutService.this.canSeeAnyPinnedShortcut(callingPackage, launcherUserId, callingPid, callingUid);
                android.content.pm.ShortcutInfo si = getShortcutInfoLocked(launcherUserId, callingPackage, packageName, shortcutId, userId, getPinnedByAnyLauncher);
                if (si != null) {
                    if (si.isEnabled() && (si.isAlive() || getPinnedByAnyLauncher)) {
                        cb.complete(si.getIntents());
                        return;
                    }
                    android.util.Log.e(com.android.server.pm.ShortcutService.TAG, "Shortcut " + shortcutId + " does not exist or disabled");
                    cb.complete((java.lang.Object) null);
                    return;
                }
                getShortcutInfoAsync(launcherUserId, packageName, shortcutId, userId, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$LocalService$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        android.content.pm.ShortcutInfo shortcutInfo = (android.content.pm.ShortcutInfo) obj;
                        cb.complete(shortcutInfo == null ? null : shortcutInfo.getIntents());
                    }
                });
            }
        }

        public void addListener(android.content.pm.ShortcutServiceInternal.ShortcutChangeListener listener) {
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                com.android.server.pm.ShortcutService.this.mListeners.add((android.content.pm.ShortcutServiceInternal.ShortcutChangeListener) java.util.Objects.requireNonNull(listener));
            }
        }

        public void addShortcutChangeCallback(android.content.pm.LauncherApps.ShortcutChangeCallback callback) {
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                com.android.server.pm.ShortcutService.this.mShortcutChangeCallbacks.add((android.content.pm.LauncherApps.ShortcutChangeCallback) java.util.Objects.requireNonNull(callback));
            }
        }

        public int getShortcutIconResId(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.lang.String shortcutId, int userId) {
            java.util.Objects.requireNonNull(callingPackage, "callingPackage");
            java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            java.util.Objects.requireNonNull(shortcutId, "shortcutId");
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                com.android.server.pm.ShortcutService.this.getLauncherShortcutsLocked(callingPackage, userId, launcherUserId).attemptToRestoreIfNeededAndSave();
                com.android.server.pm.ShortcutPackage p = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).getPackageShortcutsIfExists(packageName);
                int iconResourceId = 0;
                if (p == null) {
                    return 0;
                }
                android.content.pm.ShortcutInfo shortcutInfo = p.findShortcutById(shortcutId);
                if (shortcutInfo != null && shortcutInfo.hasIconResource()) {
                    iconResourceId = shortcutInfo.getIconResourceId();
                }
                return iconResourceId;
            }
        }

        public java.lang.String getShortcutStartingThemeResName(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.lang.String shortcutId, int userId) {
            java.util.Objects.requireNonNull(callingPackage, "callingPackage");
            java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            java.util.Objects.requireNonNull(shortcutId, "shortcutId");
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                com.android.server.pm.ShortcutService.this.getLauncherShortcutsLocked(callingPackage, userId, launcherUserId).attemptToRestoreIfNeededAndSave();
                com.android.server.pm.ShortcutPackage p = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).getPackageShortcutsIfExists(packageName);
                if (p == null) {
                    return null;
                }
                android.content.pm.ShortcutInfo shortcutInfo = p.findShortcutById(shortcutId);
                return shortcutInfo != null ? shortcutInfo.getStartingThemeResName() : null;
            }
        }

        public android.os.ParcelFileDescriptor getShortcutIconFd(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.lang.String shortcutId, int userId) {
            java.util.Objects.requireNonNull(callingPackage, "callingPackage");
            java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            java.util.Objects.requireNonNull(shortcutId, "shortcutId");
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                com.android.server.pm.ShortcutService.this.getLauncherShortcutsLocked(callingPackage, userId, launcherUserId).attemptToRestoreIfNeededAndSave();
                com.android.server.pm.ShortcutPackage p = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).getPackageShortcutsIfExists(packageName);
                if (p == null) {
                    return null;
                }
                android.content.pm.ShortcutInfo shortcutInfo = p.findShortcutById(shortcutId);
                if (shortcutInfo == null) {
                    return null;
                }
                return getShortcutIconParcelFileDescriptor(p, shortcutInfo);
            }
        }

        public void getShortcutIconFdAsync(int launcherUserId, java.lang.String callingPackage, java.lang.String packageName, java.lang.String shortcutId, int userId, final com.android.internal.infra.AndroidFuture<android.os.ParcelFileDescriptor> cb) {
            java.util.Objects.requireNonNull(callingPackage, "callingPackage");
            java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            java.util.Objects.requireNonNull(shortcutId, "shortcutId");
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                com.android.server.pm.ShortcutService.this.getLauncherShortcutsLocked(callingPackage, userId, launcherUserId).attemptToRestoreIfNeededAndSave();
                final com.android.server.pm.ShortcutPackage p = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).getPackageShortcutsIfExists(packageName);
                if (p == null) {
                    cb.complete((java.lang.Object) null);
                    return;
                }
                android.content.pm.ShortcutInfo shortcutInfo = p.findShortcutById(shortcutId);
                if (shortcutInfo != null) {
                    cb.complete(getShortcutIconParcelFileDescriptor(p, shortcutInfo));
                } else {
                    getShortcutInfoAsync(launcherUserId, packageName, shortcutId, userId, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$LocalService$$ExternalSyntheticLambda10
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$getShortcutIconFdAsync$8(cb, p, (android.content.pm.ShortcutInfo) obj);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getShortcutIconFdAsync$8(com.android.internal.infra.AndroidFuture cb, com.android.server.pm.ShortcutPackage p, android.content.pm.ShortcutInfo si) {
            cb.complete(getShortcutIconParcelFileDescriptor(p, si));
        }

        private android.os.ParcelFileDescriptor getShortcutIconParcelFileDescriptor(com.android.server.pm.ShortcutPackage p, android.content.pm.ShortcutInfo shortcutInfo) {
            if (p == null || shortcutInfo == null || !shortcutInfo.hasIconFile()) {
                return null;
            }
            java.lang.String path = p.getBitmapPathMayWait(shortcutInfo);
            if (path == null) {
                android.util.Slog.w(com.android.server.pm.ShortcutService.TAG, "null bitmap detected in getShortcutIconFd()");
                return null;
            }
            try {
                return android.os.ParcelFileDescriptor.open(new java.io.File(path), 268435456);
            } catch (java.io.FileNotFoundException e) {
                android.util.Slog.e(com.android.server.pm.ShortcutService.TAG, "Icon file not found: " + path);
                return null;
            }
        }

        public java.lang.String getShortcutIconUri(int launcherUserId, java.lang.String launcherPackage, java.lang.String packageName, java.lang.String shortcutId, int userId) {
            java.util.Objects.requireNonNull(launcherPackage, "launcherPackage");
            java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            java.util.Objects.requireNonNull(shortcutId, "shortcutId");
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                com.android.server.pm.ShortcutService.this.getLauncherShortcutsLocked(launcherPackage, userId, launcherUserId).attemptToRestoreIfNeededAndSave();
                com.android.server.pm.ShortcutPackage p = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).getPackageShortcutsIfExists(packageName);
                if (p == null) {
                    return null;
                }
                android.content.pm.ShortcutInfo shortcutInfo = p.findShortcutById(shortcutId);
                if (shortcutInfo == null) {
                    return null;
                }
                return getShortcutIconUriInternal(launcherUserId, launcherPackage, packageName, shortcutInfo, userId);
            }
        }

        public void getShortcutIconUriAsync(final int launcherUserId, final java.lang.String launcherPackage, final java.lang.String packageName, java.lang.String shortcutId, final int userId, final com.android.internal.infra.AndroidFuture<java.lang.String> cb) {
            java.util.Objects.requireNonNull(launcherPackage, "launcherPackage");
            java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
            java.util.Objects.requireNonNull(shortcutId, "shortcutId");
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(userId);
                com.android.server.pm.ShortcutService.this.throwIfUserLockedL(launcherUserId);
                com.android.server.pm.ShortcutService.this.getLauncherShortcutsLocked(launcherPackage, userId, launcherUserId).attemptToRestoreIfNeededAndSave();
                com.android.server.pm.ShortcutPackage p = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(userId).getPackageShortcutsIfExists(packageName);
                if (p == null) {
                    cb.complete((java.lang.Object) null);
                    return;
                }
                android.content.pm.ShortcutInfo shortcutInfo = p.findShortcutById(shortcutId);
                if (shortcutInfo != null) {
                    cb.complete(getShortcutIconUriInternal(launcherUserId, launcherPackage, packageName, shortcutInfo, userId));
                } else {
                    getShortcutInfoAsync(launcherUserId, packageName, shortcutId, userId, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$LocalService$$ExternalSyntheticLambda3
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$getShortcutIconUriAsync$9(cb, launcherUserId, launcherPackage, packageName, userId, (android.content.pm.ShortcutInfo) obj);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getShortcutIconUriAsync$9(com.android.internal.infra.AndroidFuture cb, int launcherUserId, java.lang.String launcherPackage, java.lang.String packageName, int userId, android.content.pm.ShortcutInfo si) {
            cb.complete(si == null ? null : getShortcutIconUriInternal(launcherUserId, launcherPackage, packageName, si, userId));
        }

        private java.lang.String getShortcutIconUriInternal(int launcherUserId, java.lang.String launcherPackage, java.lang.String packageName, android.content.pm.ShortcutInfo shortcutInfo, int userId) throws java.lang.Throwable {
            if (shortcutInfo == null) {
                android.util.Slog.w(com.android.server.pm.ShortcutService.TAG, "Can not get shortcutInfo in getShortcutIconUriInternal");
                return null;
            }
            if (!shortcutInfo.hasIconUri()) {
                return null;
            }
            java.lang.String uri = shortcutInfo.getIconUri();
            if (uri == null) {
                android.util.Slog.w(com.android.server.pm.ShortcutService.TAG, "null uri detected in getShortcutIconUri()");
                return null;
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    try {
                        int packageUid = com.android.server.pm.ShortcutService.this.mPackageManagerInternal.getPackageUid(packageName, 268435456L, userId);
                        com.android.server.pm.ShortcutService.this.mUriGrantsManager.grantUriPermissionFromOwner(com.android.server.pm.ShortcutService.this.mUriPermissionOwner, packageUid, launcherPackage, android.net.Uri.parse(uri), 1, userId, launcherUserId);
                        android.os.Binder.restoreCallingIdentity(token);
                    } catch (java.lang.Exception e) {
                        e = e;
                        try {
                            android.util.Slog.e(com.android.server.pm.ShortcutService.TAG, "Failed to grant uri access to " + launcherPackage + " for " + uri, e);
                            uri = null;
                            android.os.Binder.restoreCallingIdentity(token);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            android.os.Binder.restoreCallingIdentity(token);
                            throw th;
                        }
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    android.os.Binder.restoreCallingIdentity(token);
                    throw th;
                }
            } catch (java.lang.Exception e2) {
                e = e2;
            } catch (java.lang.Throwable th3) {
                th = th3;
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
            return uri;
        }

        public boolean hasShortcutHostPermission(int launcherUserId, java.lang.String callingPackage, int callingPid, int callingUid) {
            return com.android.server.pm.ShortcutService.this.hasShortcutHostPermission(callingPackage, launcherUserId, callingPid, callingUid);
        }

        public boolean areShortcutsSupportedOnHomeScreen(int userId) {
            return com.android.server.pm.ShortcutService.this.areShortcutsSupportedOnHomeScreen(userId);
        }

        public void setShortcutHostPackage(java.lang.String type, java.lang.String packageName, int userId) {
            com.android.server.pm.ShortcutService.this.setShortcutHostPackage(type, packageName, userId);
        }

        public boolean requestPinAppWidget(java.lang.String callingPackage, android.appwidget.AppWidgetProviderInfo appWidget, android.os.Bundle extras, android.content.IntentSender resultIntent, int userId) {
            java.util.Objects.requireNonNull(appWidget);
            return com.android.server.pm.ShortcutService.this.requestPinItem(callingPackage, userId, null, appWidget, extras, resultIntent);
        }

        public boolean isRequestPinItemSupported(int callingUserId, int requestType) {
            return com.android.server.pm.ShortcutService.this.isRequestPinItemSupported(callingUserId, requestType);
        }

        public boolean isForegroundDefaultLauncher(java.lang.String callingPackage, int callingUid) {
            java.util.Objects.requireNonNull(callingPackage);
            int userId = android.os.UserHandle.getUserId(callingUid);
            java.lang.String defaultLauncher = com.android.server.pm.ShortcutService.this.getDefaultLauncher(userId);
            if (defaultLauncher == null || !callingPackage.equals(defaultLauncher)) {
                return false;
            }
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                if (!com.android.server.pm.ShortcutService.this.isUidForegroundLocked(callingUid)) {
                    return false;
                }
                return true;
            }
        }
    }

    void handleLocaleChanged() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "handleLocaleChanged");
        }
        scheduleSaveBaseState();
        synchronized (this.mServiceLock) {
            long token = injectClearCallingIdentity();
            try {
                forEachLoadedUserLocked(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda16
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.pm.ShortcutUser) obj).detectLocaleChange();
                    }
                });
            } finally {
                injectRestoreCallingIdentity(token);
            }
        }
    }

    void checkPackageChanges(int ownerUserId) {
        android.util.Slog.d(TAG, "checkPackageChanges() ownerUserId=" + ownerUserId);
        if (injectIsSafeModeEnabled()) {
            android.util.Slog.i(TAG, "Safe mode, skipping checkPackageChanges()");
            return;
        }
        long start = getStatStartTime();
        try {
            final java.util.ArrayList<android.content.pm.UserPackage> gonePackages = new java.util.ArrayList<>();
            synchronized (this.mServiceLock) {
                com.android.server.pm.ShortcutUser user = getUserShortcutsLocked(ownerUserId);
                user.forAllPackageItems(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda18
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$checkPackageChanges$14(gonePackages, (com.android.server.pm.ShortcutPackageItem) obj);
                    }
                });
                if (gonePackages.size() > 0) {
                    for (int i = gonePackages.size() - 1; i >= 0; i--) {
                        android.content.pm.UserPackage up = gonePackages.get(i);
                        cleanUpPackageLocked(up.packageName, ownerUserId, up.userId, false);
                    }
                }
                rescanUpdatedPackagesLocked(ownerUserId, user.getLastAppScanTime());
            }
            logDurationStat(8, start);
            verifyStates();
        } catch (java.lang.Throwable th) {
            logDurationStat(8, start);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPackageChanges$14(java.util.ArrayList gonePackages, com.android.server.pm.ShortcutPackageItem spi) {
        if (!spi.getPackageInfo().isShadow() && !isPackageInstalled(spi.getPackageName(), spi.getPackageUserId())) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Uninstalled: " + spi.getPackageName() + " user " + spi.getPackageUserId());
            }
            gonePackages.add(android.content.pm.UserPackage.of(spi.getPackageUserId(), spi.getPackageName()));
        }
    }

    private void rescanUpdatedPackagesLocked(final int userId, long lastScanTime) throws java.lang.Exception {
        android.util.Slog.d(TAG, "rescan updated package user=" + userId + " last scanned=" + lastScanTime);
        final com.android.server.pm.ShortcutUser user = getUserShortcutsLocked(userId);
        long now = injectCurrentTimeMillis();
        boolean afterOta = !injectBuildFingerprint().equals(user.getLastAppScanOsFingerprint());
        forUpdatedPackages(userId, lastScanTime, afterOta, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda23
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$rescanUpdatedPackagesLocked$15(user, userId, (android.content.pm.ApplicationInfo) obj);
            }
        });
        user.setLastAppScanTime(now);
        user.setLastAppScanOsFingerprint(injectBuildFingerprint());
        scheduleSaveUser(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rescanUpdatedPackagesLocked$15(com.android.server.pm.ShortcutUser user, int userId, android.content.pm.ApplicationInfo ai) {
        user.attemptToRestoreIfNeededAndSave(this, ai.packageName, userId);
        user.rescanPackageIfNeeded(ai.packageName, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageAdded(java.lang.String packageName, int userId) {
        android.util.Slog.d(TAG, java.lang.String.format("handlePackageAdded: %s user=%d", packageName, java.lang.Integer.valueOf(userId)));
        synchronized (this.mServiceLock) {
            com.android.server.pm.ShortcutUser user = getUserShortcutsLocked(userId);
            user.attemptToRestoreIfNeededAndSave(this, packageName, userId);
            user.rescanPackageIfNeeded(packageName, true);
        }
        verifyStates();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageUpdateFinished(java.lang.String packageName, int userId) {
        android.util.Slog.d(TAG, java.lang.String.format("handlePackageUpdateFinished: %s user=%d", packageName, java.lang.Integer.valueOf(userId)));
        synchronized (this.mServiceLock) {
            com.android.server.pm.ShortcutUser user = getUserShortcutsLocked(userId);
            user.attemptToRestoreIfNeededAndSave(this, packageName, userId);
            if (isPackageInstalled(packageName, userId)) {
                user.rescanPackageIfNeeded(packageName, true);
            }
        }
        verifyStates();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageRemoved(java.lang.String packageName, int packageUserId) {
        android.util.Slog.d(TAG, java.lang.String.format("handlePackageRemoved: %s user=%d", packageName, java.lang.Integer.valueOf(packageUserId)));
        cleanUpPackageForAllLoadedUsers(packageName, packageUserId, false);
        verifyStates();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageDataCleared(java.lang.String packageName, int packageUserId) {
        android.util.Slog.d(TAG, java.lang.String.format("handlePackageDataCleared: %s user=%d", packageName, java.lang.Integer.valueOf(packageUserId)));
        cleanUpPackageForAllLoadedUsers(packageName, packageUserId, true);
        verifyStates();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageChanged(java.lang.String packageName, int packageUserId) {
        if (!isPackageInstalled(packageName, packageUserId)) {
            handlePackageRemoved(packageName, packageUserId);
            return;
        }
        android.util.Slog.d(TAG, java.lang.String.format("handlePackageChanged: %s user=%d", packageName, java.lang.Integer.valueOf(packageUserId)));
        synchronized (this.mServiceLock) {
            com.android.server.pm.ShortcutUser user = getUserShortcutsLocked(packageUserId);
            user.rescanPackageIfNeeded(packageName, true);
        }
        verifyStates();
    }

    final android.content.pm.PackageInfo getPackageInfoWithSignatures(java.lang.String packageName, int userId) {
        return getPackageInfo(packageName, userId, true);
    }

    final android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, int userId) {
        return getPackageInfo(packageName, userId, false);
    }

    int injectGetPackageUid(java.lang.String packageName, int userId) {
        long token = injectClearCallingIdentity();
        try {
            try {
                return this.mIPackageManager.getPackageUid(packageName, 795136L, userId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(TAG, "RemoteException", e);
                injectRestoreCallingIdentity(token);
                return -1;
            }
        } finally {
            injectRestoreCallingIdentity(token);
        }
    }

    final android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, int userId, boolean getSignatures) {
        return isInstalledOrNull(injectPackageInfoWithUninstalled(packageName, userId, getSignatures));
    }

    android.content.pm.PackageInfo injectPackageInfoWithUninstalled(java.lang.String packageName, int userId, boolean getSignatures) {
        long start = getStatStartTime();
        long token = injectClearCallingIdentity();
        try {
            try {
                android.content.pm.PackageInfo packageInfo = this.mIPackageManager.getPackageInfo(packageName, (getSignatures ? 134217728 : 0) | PACKAGE_MATCH_FLAGS, userId);
                injectRestoreCallingIdentity(token);
                logDurationStat(getSignatures ? 2 : 1, start);
                return packageInfo;
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(TAG, "RemoteException", e);
                injectRestoreCallingIdentity(token);
                logDurationStat(getSignatures ? 2 : 1, start);
                return null;
            }
        } catch (java.lang.Throwable th) {
            injectRestoreCallingIdentity(token);
            logDurationStat(getSignatures ? 2 : 1, start);
            throw th;
        }
    }

    final android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, int userId) {
        return isInstalledOrNull(injectApplicationInfoWithUninstalled(packageName, userId));
    }

    android.content.pm.ApplicationInfo injectApplicationInfoWithUninstalled(java.lang.String packageName, int userId) {
        long start = getStatStartTime();
        long token = injectClearCallingIdentity();
        try {
            try {
                return this.mIPackageManager.getApplicationInfo(packageName, 795136L, userId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(TAG, "RemoteException", e);
                injectRestoreCallingIdentity(token);
                logDurationStat(3, start);
                return null;
            }
        } finally {
            injectRestoreCallingIdentity(token);
            logDurationStat(3, start);
        }
    }

    final android.content.pm.ActivityInfo getActivityInfoWithMetadata(android.content.ComponentName activity, int userId) {
        return isInstalledOrNull(injectGetActivityInfoWithMetadataWithUninstalled(activity, userId));
    }

    android.content.pm.ActivityInfo injectGetActivityInfoWithMetadataWithUninstalled(android.content.ComponentName activity, int userId) {
        long start = getStatStartTime();
        long token = injectClearCallingIdentity();
        try {
            try {
                return this.mIPackageManager.getActivityInfo(activity, 795264L, userId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(TAG, "RemoteException", e);
                injectRestoreCallingIdentity(token);
                logDurationStat(6, start);
                return null;
            }
        } finally {
            injectRestoreCallingIdentity(token);
            logDurationStat(6, start);
        }
    }

    final java.util.List<android.content.pm.PackageInfo> getInstalledPackages(int userId) {
        long start = getStatStartTime();
        long token = injectClearCallingIdentity();
        try {
            try {
                java.util.List<android.content.pm.PackageInfo> all = injectGetPackagesWithUninstalled(userId);
                all.removeIf(PACKAGE_NOT_INSTALLED);
                return all;
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(TAG, "RemoteException", e);
                injectRestoreCallingIdentity(token);
                logDurationStat(7, start);
                return null;
            }
        } finally {
            injectRestoreCallingIdentity(token);
            logDurationStat(7, start);
        }
    }

    java.util.List<android.content.pm.PackageInfo> injectGetPackagesWithUninstalled(int userId) throws android.os.RemoteException {
        android.content.pm.ParceledListSlice<android.content.pm.PackageInfo> parceledList = this.mIPackageManager.getInstalledPackages(795136L, userId);
        if (parceledList == null) {
            return java.util.Collections.emptyList();
        }
        return parceledList.getList();
    }

    private void forUpdatedPackages(int userId, long lastScanTime, boolean afterOta, java.util.function.Consumer<android.content.pm.ApplicationInfo> callback) {
        android.util.Slog.d(TAG, "forUpdatedPackages for user " + userId + ", lastScanTime=" + lastScanTime + " afterOta=" + afterOta);
        java.util.List<android.content.pm.PackageInfo> list = getInstalledPackages(userId);
        for (int i = list.size() - 1; i >= 0; i--) {
            android.content.pm.PackageInfo pi = list.get(i);
            if (afterOta || pi.lastUpdateTime >= lastScanTime) {
                android.util.Slog.d(TAG, "Found updated package " + pi.packageName + " updateTime=" + pi.lastUpdateTime);
                callback.accept(pi.applicationInfo);
            }
        }
    }

    private boolean isApplicationFlagSet(java.lang.String packageName, int userId, int flags) {
        android.content.pm.ApplicationInfo ai = injectApplicationInfoWithUninstalled(packageName, userId);
        return ai != null && (ai.flags & flags) == flags;
    }

    private boolean isEnabled(android.content.pm.ActivityInfo ai, int userId) {
        if (ai == null) {
            return false;
        }
        long token = injectClearCallingIdentity();
        try {
            try {
                int enabledFlag = this.mIPackageManager.getComponentEnabledSetting(ai.getComponentName(), userId);
                injectRestoreCallingIdentity(token);
                return (enabledFlag == 0 && ai.enabled) || enabledFlag == 1;
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(TAG, "RemoteException", e);
                injectRestoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            injectRestoreCallingIdentity(token);
            throw th;
        }
    }

    private static boolean isSystem(android.content.pm.ActivityInfo ai) {
        return ai != null && isSystem(ai.applicationInfo);
    }

    private static boolean isSystem(android.content.pm.ApplicationInfo ai) {
        return (ai == null || (ai.flags & 129) == 0) ? false : true;
    }

    private static boolean isInstalled(android.content.pm.ApplicationInfo ai) {
        return (ai == null || !mStaticShortcutServiceExt.adjustPackageEnabledForIsInstalled(ai.enabled, ai, ai.mApplicationInfoExt) || (ai.flags & 8388608) == 0) ? false : true;
    }

    private static boolean isEphemeralApp(android.content.pm.ApplicationInfo ai) {
        return ai != null && ai.isInstantApp();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isInstalled(android.content.pm.PackageInfo pi) {
        return pi != null && isInstalled(pi.applicationInfo);
    }

    private static boolean isInstalled(android.content.pm.ActivityInfo ai) {
        return ai != null && isInstalled(ai.applicationInfo);
    }

    private static android.content.pm.ApplicationInfo isInstalledOrNull(android.content.pm.ApplicationInfo ai) {
        if (isInstalled(ai)) {
            return ai;
        }
        return null;
    }

    private static android.content.pm.PackageInfo isInstalledOrNull(android.content.pm.PackageInfo pi) {
        if (isInstalled(pi)) {
            return pi;
        }
        return null;
    }

    private static android.content.pm.ActivityInfo isInstalledOrNull(android.content.pm.ActivityInfo ai) {
        if (isInstalled(ai)) {
            return ai;
        }
        return null;
    }

    boolean isPackageInstalled(java.lang.String packageName, int userId) {
        return getApplicationInfo(packageName, userId) != null;
    }

    boolean isEphemeralApp(java.lang.String packageName, int userId) {
        return isEphemeralApp(getApplicationInfo(packageName, userId));
    }

    android.content.res.XmlResourceParser injectXmlMetaData(android.content.pm.ActivityInfo activityInfo, java.lang.String key) {
        return activityInfo.loadXmlMetaData(this.mContext.getPackageManager(), key);
    }

    android.content.res.Resources injectGetResourcesForApplicationAsUser(java.lang.String packageName, int userId) {
        long start = getStatStartTime();
        long token = injectClearCallingIdentity();
        try {
            try {
                return this.mContext.createContextAsUser(android.os.UserHandle.of(userId), 0).getPackageManager().getResourcesForApplication(packageName);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.e(TAG, "Resources of package " + packageName + " for user " + userId + " not found");
                injectRestoreCallingIdentity(token);
                logDurationStat(9, start);
                return null;
            }
        } finally {
            injectRestoreCallingIdentity(token);
            logDurationStat(9, start);
        }
    }

    private android.content.Intent getMainActivityIntent() {
        android.content.Intent intent = new android.content.Intent("android.intent.action.MAIN");
        intent.addCategory(LAUNCHER_INTENT_CATEGORY);
        return intent;
    }

    java.util.List<android.content.pm.ResolveInfo> queryActivities(android.content.Intent baseIntent, java.lang.String packageName, android.content.ComponentName activity, int userId) {
        baseIntent.setPackage((java.lang.String) java.util.Objects.requireNonNull(packageName));
        if (activity != null) {
            baseIntent.setComponent(activity);
        }
        return queryActivities(baseIntent, userId, true);
    }

    java.util.List<android.content.pm.ResolveInfo> queryActivities(android.content.Intent intent, final int userId, boolean exportedOnly) {
        long token = injectClearCallingIdentity();
        try {
            java.util.List<android.content.pm.ResolveInfo> resolved = this.mContext.getPackageManager().queryIntentActivitiesAsUser(intent, PACKAGE_MATCH_FLAGS, userId);
            if (resolved == null || resolved.size() == 0) {
                return EMPTY_RESOLVE_INFO;
            }
            resolved.removeIf(ACTIVITY_NOT_INSTALLED);
            resolved.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda29
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$queryActivities$16(userId, (android.content.pm.ResolveInfo) obj);
                }
            });
            if (exportedOnly) {
                resolved.removeIf(ACTIVITY_NOT_EXPORTED);
            }
            return resolved;
        } finally {
            injectRestoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$queryActivities$16(int userId, android.content.pm.ResolveInfo ri) {
        android.content.pm.ActivityInfo ai = ri.activityInfo;
        return (isSystem(ai) || isEnabled(ai, userId)) ? false : true;
    }

    android.content.ComponentName injectGetDefaultMainActivity(java.lang.String packageName, int userId) {
        long start = getStatStartTime();
        try {
            java.util.List<android.content.pm.ResolveInfo> resolved = queryActivities(getMainActivityIntent(), packageName, null, userId);
            return resolved.size() != 0 ? resolved.get(0).activityInfo.getComponentName() : null;
        } finally {
            logDurationStat(11, start);
        }
    }

    boolean injectIsMainActivity(android.content.ComponentName activity, int userId) {
        long start = getStatStartTime();
        try {
            if (activity == null) {
                wtf("null activity detected");
                return false;
            }
            if (DUMMY_MAIN_ACTIVITY.equals(activity.getClassName())) {
                return true;
            }
            java.util.List<android.content.pm.ResolveInfo> resolved = queryActivities(getMainActivityIntent(), activity.getPackageName(), activity, userId);
            return resolved.size() > 0;
        } finally {
            logDurationStat(12, start);
        }
    }

    android.content.ComponentName getDummyMainActivity(java.lang.String packageName) {
        return new android.content.ComponentName(packageName, DUMMY_MAIN_ACTIVITY);
    }

    boolean isDummyMainActivity(android.content.ComponentName name) {
        return name != null && DUMMY_MAIN_ACTIVITY.equals(name.getClassName());
    }

    java.util.List<android.content.pm.ResolveInfo> injectGetMainActivities(java.lang.String packageName, int userId) {
        long start = getStatStartTime();
        try {
            return queryActivities(getMainActivityIntent(), packageName, null, userId);
        } finally {
            logDurationStat(12, start);
        }
    }

    boolean injectIsActivityEnabledAndExported(android.content.ComponentName activity, int userId) {
        long start = getStatStartTime();
        try {
            return queryActivities(new android.content.Intent(), activity.getPackageName(), activity, userId).size() > 0;
        } finally {
            logDurationStat(13, start);
        }
    }

    android.content.ComponentName injectGetPinConfirmationActivity(java.lang.String launcherPackageName, int launcherUserId, int requestType) {
        java.lang.String action;
        java.util.Objects.requireNonNull(launcherPackageName);
        if (requestType == 1) {
            action = "android.content.pm.action.CONFIRM_PIN_SHORTCUT";
        } else {
            action = "android.content.pm.action.CONFIRM_PIN_APPWIDGET";
        }
        android.content.Intent confirmIntent = new android.content.Intent(action).setPackage(launcherPackageName);
        java.util.List<android.content.pm.ResolveInfo> candidates = queryActivities(confirmIntent, launcherUserId, false);
        java.util.Iterator<android.content.pm.ResolveInfo> it = candidates.iterator();
        if (it.hasNext()) {
            android.content.pm.ResolveInfo ri = it.next();
            return ri.activityInfo.getComponentName();
        }
        return null;
    }

    boolean injectIsSafeModeEnabled() {
        long token = injectClearCallingIdentity();
        try {
            boolean zIsSafeModeEnabled = android.view.IWindowManager.Stub.asInterface(android.os.ServiceManager.getService("window")).isSafeModeEnabled();
            injectRestoreCallingIdentity(token);
            return zIsSafeModeEnabled;
        } catch (android.os.RemoteException e) {
            injectRestoreCallingIdentity(token);
            return false;
        } catch (java.lang.Throwable th) {
            injectRestoreCallingIdentity(token);
            throw th;
        }
    }

    int getParentOrSelfUserId(int userId) {
        return this.mUserManagerInternal.getProfileParentId(userId);
    }

    void injectSendIntentSender(android.content.IntentSender intentSender, android.content.Intent extras) {
        if (intentSender == null) {
            return;
        }
        try {
            android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic().setPendingIntentBackgroundActivityStartMode(2);
            intentSender.sendIntent(this.mContext, 0, extras, null, null, null, options.toBundle());
        } catch (android.content.IntentSender.SendIntentException e) {
            android.util.Slog.w(TAG, "sendIntent failed().", e);
        }
    }

    boolean shouldBackupApp(java.lang.String packageName, int userId) {
        return isApplicationFlagSet(packageName, userId, 32768);
    }

    static boolean shouldBackupApp(android.content.pm.PackageInfo pi) {
        return (pi.applicationInfo.flags & 32768) != 0;
    }

    public byte[] getBackupPayload(int userId) {
        enforceSystem();
        if (DEBUG) {
            android.util.Slog.d(TAG, "Backing up user " + userId);
        }
        synchronized (this.mServiceLock) {
            if (!isUserUnlockedL(userId)) {
                wtf("Can't backup: user " + userId + " is locked or not running");
                return null;
            }
            com.android.server.pm.ShortcutUser user = getUserShortcutsLocked(userId);
            if (user == null) {
                wtf("Can't backup: user not found: id=" + userId);
                return null;
            }
            user.forAllPackageItems(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda26
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.pm.ShortcutPackageItem) obj).refreshPackageSignatureAndSave();
                }
            });
            user.forAllPackages(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda27
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.pm.ShortcutPackage) obj).rescanPackageIfNeeded(false, true);
                }
            });
            user.forAllLaunchers(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda28
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.pm.ShortcutLauncher) obj).ensurePackageInfo();
                }
            });
            scheduleSaveUser(userId);
            saveDirtyInfo();
            java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream(32768);
            try {
                saveUserInternalLocked(userId, os, true);
                byte[] payload = os.toByteArray();
                this.mShortcutDumpFiles.save("backup-1-payload.txt", payload);
                return payload;
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.w(TAG, "Backup failed.", e);
                return null;
            }
        }
    }

    public void applyRestore(byte[] payload, int userId) {
        enforceSystem();
        android.util.Slog.d(TAG, "Restoring user " + userId);
        synchronized (this.mServiceLock) {
            if (!isUserUnlockedL(userId)) {
                wtf("Can't restore: user " + userId + " is locked or not running");
                return;
            }
            this.mShortcutDumpFiles.save("restore-0-start.txt", new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$applyRestore$20((java.io.PrintWriter) obj);
                }
            });
            this.mShortcutDumpFiles.save("restore-1-payload.xml", payload);
            java.io.ByteArrayInputStream is = new java.io.ByteArrayInputStream(payload);
            try {
                com.android.server.pm.ShortcutUser restored = loadUserInternal(userId, is, true);
                this.mShortcutDumpFiles.save("restore-2.txt", new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.dumpInner((java.io.PrintWriter) obj);
                    }
                });
                getUserShortcutsLocked(userId).mergeRestoredFile(restored);
                this.mShortcutDumpFiles.save("restore-3.txt", new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.dumpInner((java.io.PrintWriter) obj);
                    }
                });
                rescanUpdatedPackagesLocked(userId, 0L);
                this.mShortcutDumpFiles.save("restore-4.txt", new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.dumpInner((java.io.PrintWriter) obj);
                    }
                });
                this.mShortcutDumpFiles.save("restore-5-finish.txt", new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$applyRestore$21((java.io.PrintWriter) obj);
                    }
                });
                saveUser(userId);
            } catch (com.android.server.pm.ShortcutService.InvalidFileFormatException | java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.w(TAG, "Restoration failed.", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyRestore$20(java.io.PrintWriter pw) {
        pw.print("Start time: ");
        dumpCurrentTime(pw);
        pw.println();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyRestore$21(java.io.PrintWriter pw) {
        pw.print("Finish time: ");
        dumpCurrentTime(pw);
        pw.println();
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(this.mContext, TAG, pw)) {
            dumpNoCheck(fd, pw, args);
        }
    }

    void dumpNoCheck(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        com.android.server.pm.ShortcutService.DumpFilter filter = parseDumpArgs(args);
        if (filter.shouldDumpCheckIn()) {
            dumpCheckin(pw, filter.shouldCheckInClear());
            return;
        }
        if (filter.shouldDumpMain()) {
            dumpInner(pw, filter);
            pw.println();
        }
        if (filter.shouldDumpUid()) {
            dumpUid(pw);
            pw.println();
        }
        if (filter.shouldDumpFiles()) {
            dumpDumpFiles(pw);
            pw.println();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:66:0x0103, code lost:
    
        r2 = r6.length;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0104, code lost:
    
        if (r1 >= r2) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0106, code lost:
    
        r0.addPackage(r6[r1]);
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x010f, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static com.android.server.pm.ShortcutService.DumpFilter parseDumpArgs(java.lang.String[] r6) {
        /*
            Method dump skipped, instruction units count: 272
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShortcutService.parseDumpArgs(java.lang.String[]):com.android.server.pm.ShortcutService$DumpFilter");
    }

    static class DumpFilter {
        private boolean mDumpCheckIn = false;
        private boolean mCheckInClear = false;
        private boolean mDumpMain = true;
        private boolean mDumpUid = false;
        private boolean mDumpFiles = false;
        private boolean mDumpDetails = true;
        private final java.util.List<java.util.regex.Pattern> mPackagePatterns = new java.util.ArrayList();
        private final java.util.List<java.lang.Integer> mUsers = new java.util.ArrayList();

        DumpFilter() {
        }

        void addPackageRegex(java.lang.String regex) {
            this.mPackagePatterns.add(java.util.regex.Pattern.compile(regex));
        }

        public void addPackage(java.lang.String packageName) {
            addPackageRegex(java.util.regex.Pattern.quote(packageName));
        }

        void addUser(int userId) {
            this.mUsers.add(java.lang.Integer.valueOf(userId));
        }

        boolean isPackageMatch(java.lang.String packageName) {
            if (this.mPackagePatterns.size() == 0) {
                return true;
            }
            for (int i = 0; i < this.mPackagePatterns.size(); i++) {
                if (this.mPackagePatterns.get(i).matcher(packageName).find()) {
                    return true;
                }
            }
            return false;
        }

        boolean isUserMatch(int userId) {
            if (this.mUsers.size() == 0) {
                return true;
            }
            for (int i = 0; i < this.mUsers.size(); i++) {
                if (this.mUsers.get(i).intValue() == userId) {
                    return true;
                }
            }
            return false;
        }

        public boolean shouldDumpCheckIn() {
            return this.mDumpCheckIn;
        }

        public void setDumpCheckIn(boolean dumpCheckIn) {
            this.mDumpCheckIn = dumpCheckIn;
        }

        public boolean shouldCheckInClear() {
            return this.mCheckInClear;
        }

        public void setCheckInClear(boolean checkInClear) {
            this.mCheckInClear = checkInClear;
        }

        public boolean shouldDumpMain() {
            return this.mDumpMain;
        }

        public void setDumpMain(boolean dumpMain) {
            this.mDumpMain = dumpMain;
        }

        public boolean shouldDumpUid() {
            return this.mDumpUid;
        }

        public void setDumpUid(boolean dumpUid) {
            this.mDumpUid = dumpUid;
        }

        public boolean shouldDumpFiles() {
            return this.mDumpFiles;
        }

        public void setDumpFiles(boolean dumpFiles) {
            this.mDumpFiles = dumpFiles;
        }

        public boolean shouldDumpDetails() {
            return this.mDumpDetails;
        }

        public void setDumpDetails(boolean dumpDetails) {
            this.mDumpDetails = dumpDetails;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInner(java.io.PrintWriter pw) {
        dumpInner(pw, new com.android.server.pm.ShortcutService.DumpFilter());
    }

    private void dumpInner(java.io.PrintWriter pw, com.android.server.pm.ShortcutService.DumpFilter filter) {
        synchronized (this.mServiceLock) {
            if (filter.shouldDumpDetails()) {
                long now = injectCurrentTimeMillis();
                pw.print("Now: [");
                pw.print(now);
                pw.print("] ");
                pw.print(formatTime(now));
                pw.print("  Raw last reset: [");
                pw.print(this.mRawLastResetTime.get());
                pw.print("] ");
                pw.print(formatTime(this.mRawLastResetTime.get()));
                long last = getLastResetTimeLocked();
                pw.print("  Last reset: [");
                pw.print(last);
                pw.print("] ");
                pw.print(formatTime(last));
                long next = getNextResetTimeLocked();
                pw.print("  Next reset: [");
                pw.print(next);
                pw.print("] ");
                pw.print(formatTime(next));
                pw.println();
                pw.println();
                pw.print("  Config:");
                pw.print("    Max icon dim: ");
                pw.println(this.mMaxIconDimension);
                pw.print("    Icon format: ");
                pw.println(this.mIconPersistFormat);
                pw.print("    Icon quality: ");
                pw.println(this.mIconPersistQuality);
                pw.print("    saveDelayMillis: ");
                pw.println(this.mSaveDelayMillis);
                pw.print("    resetInterval: ");
                pw.println(this.mResetInterval);
                pw.print("    maxUpdatesPerInterval: ");
                pw.println(this.mMaxUpdatesPerInterval);
                pw.print("    maxShortcutsPerActivity: ");
                pw.println(this.mMaxShortcuts);
                pw.println();
                this.mStatLogger.dump(pw, "  ");
                synchronized (this.mWtfLock) {
                    pw.println();
                    pw.print("  #Failures: ");
                    pw.println(this.mWtfCount);
                    if (this.mLastWtfStacktrace != null) {
                        pw.print("  Last failure stack trace: ");
                        pw.println(android.util.Log.getStackTraceString(this.mLastWtfStacktrace));
                    }
                }
                pw.println();
            }
            for (int i = 0; i < this.mUsers.size(); i++) {
                com.android.server.pm.ShortcutUser user = this.mUsers.valueAt(i);
                if (filter.isUserMatch(user.getUserId())) {
                    user.dump(pw, "  ", filter);
                    pw.println();
                }
            }
            for (int i2 = 0; i2 < this.mShortcutNonPersistentUsers.size(); i2++) {
                com.android.server.pm.ShortcutNonPersistentUser user2 = this.mShortcutNonPersistentUsers.valueAt(i2);
                if (filter.isUserMatch(user2.getUserId())) {
                    user2.dump(pw, "  ", filter);
                    pw.println();
                }
            }
        }
    }

    private void dumpUid(java.io.PrintWriter pw) {
        synchronized (this.mServiceLock) {
            pw.println("** SHORTCUT MANAGER UID STATES (dumpsys shortcut -n -u)");
            for (int i = 0; i < this.mUidState.size(); i++) {
                int uid = this.mUidState.keyAt(i);
                int state = this.mUidState.valueAt(i);
                pw.print("    UID=");
                pw.print(uid);
                pw.print(" state=");
                pw.print(state);
                if (isProcessStateForeground(state)) {
                    pw.print("  [FG]");
                }
                pw.print("  last FG=");
                pw.print(this.mUidLastForegroundElapsedTime.get(uid));
                pw.println();
            }
        }
    }

    static java.lang.String formatTime(long time) {
        return android.text.format.TimeMigrationUtils.formatMillisWithFixedFormat(time);
    }

    private void dumpCurrentTime(java.io.PrintWriter pw) {
        pw.print(formatTime(injectCurrentTimeMillis()));
    }

    private void dumpCheckin(java.io.PrintWriter pw, boolean clear) {
        synchronized (this.mServiceLock) {
            try {
                org.json.JSONArray users = new org.json.JSONArray();
                for (int i = 0; i < this.mUsers.size(); i++) {
                    users.put(this.mUsers.valueAt(i).dumpCheckin(clear));
                }
                org.json.JSONObject result = new org.json.JSONObject();
                result.put(KEY_SHORTCUT, users);
                result.put(KEY_LOW_RAM, injectIsLowRamDevice());
                result.put(KEY_ICON_SIZE, this.mMaxIconDimension);
                pw.println(result.toString(1));
            } catch (org.json.JSONException e) {
                android.util.Slog.e(TAG, "Unable to write in json", e);
            }
        }
    }

    private void dumpDumpFiles(java.io.PrintWriter pw) {
        synchronized (this.mServiceLock) {
            pw.println("** SHORTCUT MANAGER FILES (dumpsys shortcut -n -f)");
            this.mShortcutDumpFiles.dumpAll(pw);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws java.lang.Throwable {
        enforceShell();
        long token = injectClearCallingIdentity();
        try {
            int status = new com.android.server.pm.ShortcutService.MyShellCommand().exec(this, in, out, err, args, callback, resultReceiver);
            try {
                resultReceiver.send(status, null);
                injectRestoreCallingIdentity(token);
            } catch (java.lang.Throwable th) {
                th = th;
                injectRestoreCallingIdentity(token);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    static class CommandException extends java.lang.Exception {
        public CommandException(java.lang.String message) {
            super(message);
        }
    }

    private class MyShellCommand extends android.os.ShellCommand {
        private int mShortcutMatchFlags;
        private int mUserId;

        private MyShellCommand() {
            this.mUserId = 0;
            this.mShortcutMatchFlags = 15;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0024  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void parseOptionsLocked(boolean r5) throws com.android.server.pm.ShortcutService.CommandException {
            /*
                r4 = this;
            L1:
                java.lang.String r0 = r4.getNextOption()
                r1 = r0
                if (r0 == 0) goto L86
                int r0 = r1.hashCode()
                switch(r0) {
                    case -1626182425: goto L1a;
                    case 1333469547: goto L10;
                    default: goto Lf;
                }
            Lf:
                goto L24
            L10:
                java.lang.String r0 = "--user"
                boolean r0 = r1.equals(r0)
                if (r0 == 0) goto Lf
                r0 = 0
                goto L25
            L1a:
                java.lang.String r0 = "--flags"
                boolean r0 = r1.equals(r0)
                if (r0 == 0) goto Lf
                r0 = 1
                goto L25
            L24:
                r0 = -1
            L25:
                switch(r0) {
                    case 0: goto L41;
                    case 1: goto L79;
                    default: goto L28;
                }
            L28:
                com.android.server.pm.ShortcutService$CommandException r0 = new com.android.server.pm.ShortcutService$CommandException
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "Unknown option: "
                java.lang.StringBuilder r2 = r2.append(r3)
                java.lang.StringBuilder r2 = r2.append(r1)
                java.lang.String r2 = r2.toString()
                r0.<init>(r2)
                throw r0
            L41:
                if (r5 == 0) goto L79
                java.lang.String r0 = r4.getNextArgRequired()
                int r0 = android.os.UserHandle.parseUserArg(r0)
                r4.mUserId = r0
                com.android.server.pm.ShortcutService r0 = com.android.server.pm.ShortcutService.this
                int r2 = r4.mUserId
                boolean r0 = r0.isUserUnlockedL(r2)
                if (r0 == 0) goto L58
                goto L84
            L58:
                com.android.server.pm.ShortcutService$CommandException r0 = new com.android.server.pm.ShortcutService$CommandException
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "User "
                java.lang.StringBuilder r2 = r2.append(r3)
                int r3 = r4.mUserId
                java.lang.StringBuilder r2 = r2.append(r3)
                java.lang.String r3 = " is not running or locked"
                java.lang.StringBuilder r2 = r2.append(r3)
                java.lang.String r2 = r2.toString()
                r0.<init>(r2)
                throw r0
            L79:
                java.lang.String r0 = r4.getNextArgRequired()
                int r0 = java.lang.Integer.parseInt(r0)
                r4.mShortcutMatchFlags = r0
            L84:
                goto L1
            L86:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShortcutService.MyShellCommand.parseOptionsLocked(boolean):void");
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:8:0x0014  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int onCommand(java.lang.String r6) {
            /*
                Method dump skipped, instruction units count: 282
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShortcutService.MyShellCommand.onCommand(java.lang.String):int");
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.println("Usage: cmd shortcut COMMAND [options ...]");
            pw.println();
            pw.println("cmd shortcut reset-throttling [--user USER_ID]");
            pw.println("    Reset throttling for all packages and users");
            pw.println();
            pw.println("cmd shortcut reset-all-throttling");
            pw.println("    Reset the throttling state for all users");
            pw.println();
            pw.println("cmd shortcut override-config CONFIG");
            pw.println("    Override the configuration for testing (will last until reboot)");
            pw.println();
            pw.println("cmd shortcut reset-config");
            pw.println("    Reset the configuration set with \"update-config\"");
            pw.println();
            pw.println("[Deprecated] cmd shortcut get-default-launcher [--user USER_ID]");
            pw.println("    Show the default launcher");
            pw.println("    Note: This command is deprecated. Callers should query the default launcher from RoleManager instead.");
            pw.println();
            pw.println("cmd shortcut unload-user [--user USER_ID]");
            pw.println("    Unload a user from the memory");
            pw.println("    (This should not affect any observable behavior)");
            pw.println();
            pw.println("cmd shortcut clear-shortcuts [--user USER_ID] PACKAGE");
            pw.println("    Remove all shortcuts from a package, including pinned shortcuts");
            pw.println();
            pw.println("cmd shortcut get-shortcuts [--user USER_ID] [--flags FLAGS] PACKAGE");
            pw.println("    Show the shortcuts for a package that match the given flags");
            pw.println();
            pw.println("cmd shortcut has-shortcut-access [--user USER_ID] PACKAGE");
            pw.println("    Prints \"true\" if the package can access shortcuts, \"false\" otherwise");
            pw.println();
        }

        private void handleResetThrottling() throws com.android.server.pm.ShortcutService.CommandException {
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                parseOptionsLocked(true);
                android.util.Slog.i("ShellCommand", "cmd: handleResetThrottling: user=" + this.mUserId);
                com.android.server.pm.ShortcutService.this.resetThrottlingInner(this.mUserId);
            }
        }

        private void handleResetAllThrottling() {
            android.util.Slog.i("ShellCommand", "cmd: handleResetAllThrottling");
            com.android.server.pm.ShortcutService.this.resetAllThrottlingInner();
        }

        private void handleOverrideConfig() throws com.android.server.pm.ShortcutService.CommandException {
            java.lang.String config = getNextArgRequired();
            android.util.Slog.i("ShellCommand", "cmd: handleOverrideConfig: " + config);
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                if (!com.android.server.pm.ShortcutService.this.updateConfigurationLocked(config)) {
                    throw new com.android.server.pm.ShortcutService.CommandException("override-config failed.  See logcat for details.");
                }
            }
        }

        private void handleResetConfig() {
            android.util.Slog.i("ShellCommand", "cmd: handleResetConfig");
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                com.android.server.pm.ShortcutService.this.loadConfigurationLocked();
            }
        }

        private void handleGetDefaultLauncher() throws com.android.server.pm.ShortcutService.CommandException {
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                parseOptionsLocked(true);
                java.lang.String defaultLauncher = com.android.server.pm.ShortcutService.this.getDefaultLauncher(this.mUserId);
                if (defaultLauncher == null) {
                    throw new com.android.server.pm.ShortcutService.CommandException("Failed to get the default launcher for user " + this.mUserId);
                }
                java.util.List<android.content.pm.ResolveInfo> allHomeCandidates = new java.util.ArrayList<>();
                com.android.server.pm.ShortcutService.this.mPackageManagerInternal.getHomeActivitiesAsUser(allHomeCandidates, com.android.server.pm.ShortcutService.this.getParentOrSelfUserId(this.mUserId));
                java.util.Iterator<android.content.pm.ResolveInfo> it = allHomeCandidates.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    android.content.pm.ResolveInfo ri = it.next();
                    android.content.pm.ComponentInfo ci = ri.getComponentInfo();
                    if (ci.packageName.equals(defaultLauncher)) {
                        getOutPrintWriter().println("Launcher: " + ci.getComponentName());
                        break;
                    }
                }
            }
        }

        private void handleUnloadUser() throws com.android.server.pm.ShortcutService.CommandException {
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                parseOptionsLocked(true);
                android.util.Slog.i("ShellCommand", "cmd: handleUnloadUser: user=" + this.mUserId);
                com.android.server.pm.ShortcutService.this.handleStopUser(this.mUserId);
            }
        }

        private void handleClearShortcuts() throws com.android.server.pm.ShortcutService.CommandException {
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                parseOptionsLocked(true);
                java.lang.String packageName = getNextArgRequired();
                android.util.Slog.i("ShellCommand", "cmd: handleClearShortcuts: user" + this.mUserId + ", " + packageName);
                com.android.server.pm.ShortcutService.this.cleanUpPackageForAllLoadedUsers(packageName, this.mUserId, true);
            }
        }

        private void handleGetShortcuts() throws com.android.server.pm.ShortcutService.CommandException {
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                parseOptionsLocked(true);
                java.lang.String packageName = getNextArgRequired();
                android.util.Slog.i("ShellCommand", "cmd: handleGetShortcuts: user=" + this.mUserId + ", flags=" + this.mShortcutMatchFlags + ", package=" + packageName);
                com.android.server.pm.ShortcutUser user = com.android.server.pm.ShortcutService.this.getUserShortcutsLocked(this.mUserId);
                com.android.server.pm.ShortcutPackage p = user.getPackageShortcutsIfExists(packageName);
                if (p == null) {
                    return;
                }
                p.dumpShortcuts(getOutPrintWriter(), this.mShortcutMatchFlags);
            }
        }

        private void handleVerifyStates() throws com.android.server.pm.ShortcutService.CommandException {
            try {
                com.android.server.pm.ShortcutService.this.verifyStatesForce();
            } catch (java.lang.Throwable th) {
                throw new com.android.server.pm.ShortcutService.CommandException(th.getMessage() + "\n" + android.util.Log.getStackTraceString(th));
            }
        }

        private void handleHasShortcutAccess() throws com.android.server.pm.ShortcutService.CommandException {
            synchronized (com.android.server.pm.ShortcutService.this.mServiceLock) {
                parseOptionsLocked(true);
                java.lang.String packageName = getNextArgRequired();
                boolean shortcutAccess = com.android.server.pm.ShortcutService.this.hasShortcutHostPermissionInner(packageName, this.mUserId);
                getOutPrintWriter().println(java.lang.Boolean.toString(shortcutAccess));
            }
        }
    }

    long injectCurrentTimeMillis() {
        return java.lang.System.currentTimeMillis();
    }

    long injectElapsedRealtime() {
        return android.os.SystemClock.elapsedRealtime();
    }

    long injectUptimeMillis() {
        return android.os.SystemClock.uptimeMillis();
    }

    int injectBinderCallingUid() {
        return getCallingUid();
    }

    int injectBinderCallingPid() {
        return getCallingPid();
    }

    private int getCallingUserId() {
        return android.os.UserHandle.getUserId(injectBinderCallingUid());
    }

    long injectClearCallingIdentity() {
        return android.os.Binder.clearCallingIdentity();
    }

    void injectRestoreCallingIdentity(long token) {
        android.os.Binder.restoreCallingIdentity(token);
    }

    java.lang.String injectBuildFingerprint() {
        return android.os.Build.FINGERPRINT;
    }

    final void wtf(java.lang.String message) {
        wtf(message, null);
    }

    void wtf(java.lang.String message, java.lang.Throwable e) {
        if (e == null) {
            e = new java.lang.RuntimeException("Stacktrace");
        }
        synchronized (this.mWtfLock) {
            this.mWtfCount++;
            this.mLastWtfStacktrace = new java.lang.Exception("Last failure was logged here:");
        }
        android.util.Slog.wtf(TAG, message, e);
    }

    java.io.File injectSystemDataPath() {
        return android.os.Environment.getDataSystemDirectory();
    }

    java.io.File injectUserDataPath(int userId) {
        return new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), DIRECTORY_PER_USER);
    }

    public java.io.File getDumpPath() {
        return new java.io.File(injectUserDataPath(0), DIRECTORY_DUMP);
    }

    boolean injectIsLowRamDevice() {
        return android.app.ActivityManager.isLowRamDeviceStatic();
    }

    void injectRegisterUidObserver(android.app.IUidObserver observer, int which) {
        try {
            android.app.ActivityManager.getService().registerUidObserver(observer, which, -1, (java.lang.String) null);
        } catch (android.os.RemoteException e) {
        }
    }

    void injectRegisterRoleHoldersListener(android.app.role.OnRoleHoldersChangedListener listener) {
        this.mRoleManager.addOnRoleHoldersChangedListenerAsUser(this.mContext.getMainExecutor(), listener, android.os.UserHandle.ALL);
    }

    java.lang.String injectGetHomeRoleHolderAsUser(int userId) {
        java.util.List<java.lang.String> roleHolders = this.mRoleManager.getRoleHoldersAsUser("android.app.role.HOME", android.os.UserHandle.of(userId));
        if (roleHolders.isEmpty()) {
            return null;
        }
        return roleHolders.get(0);
    }

    java.io.File getUserBitmapFilePath(int userId) {
        return new java.io.File(injectUserDataPath(userId), DIRECTORY_BITMAPS);
    }

    android.util.SparseArray<com.android.server.pm.ShortcutUser> getShortcutsForTest() {
        return this.mUsers;
    }

    int getMaxShortcutsForTest() {
        return this.mMaxShortcuts;
    }

    int getMaxUpdatesPerIntervalForTest() {
        return this.mMaxUpdatesPerInterval;
    }

    long getResetIntervalForTest() {
        return this.mResetInterval;
    }

    int getMaxIconDimensionForTest() {
        return this.mMaxIconDimension;
    }

    android.graphics.Bitmap.CompressFormat getIconPersistFormatForTest() {
        return this.mIconPersistFormat;
    }

    int getIconPersistQualityForTest() {
        return this.mIconPersistQuality;
    }

    com.android.server.pm.ShortcutPackage getPackageShortcutForTest(java.lang.String packageName, int userId) {
        synchronized (this.mServiceLock) {
            com.android.server.pm.ShortcutUser user = this.mUsers.get(userId);
            if (user == null) {
                return null;
            }
            return user.getAllPackagesForTest().get(packageName);
        }
    }

    android.content.pm.ShortcutInfo getPackageShortcutForTest(java.lang.String packageName, java.lang.String shortcutId, int userId) {
        synchronized (this.mServiceLock) {
            com.android.server.pm.ShortcutPackage pkg = getPackageShortcutForTest(packageName, userId);
            if (pkg == null) {
                return null;
            }
            return pkg.findShortcutById(shortcutId);
        }
    }

    void updatePackageShortcutForTest(java.lang.String packageName, java.lang.String shortcutId, int userId, java.util.function.Consumer<android.content.pm.ShortcutInfo> cb) {
        synchronized (this.mServiceLock) {
            com.android.server.pm.ShortcutPackage pkg = getPackageShortcutForTest(packageName, userId);
            if (pkg == null) {
                return;
            }
            cb.accept(pkg.findShortcutById(shortcutId));
        }
    }

    com.android.server.pm.ShortcutLauncher getLauncherShortcutForTest(java.lang.String packageName, int userId) {
        synchronized (this.mServiceLock) {
            com.android.server.pm.ShortcutUser user = this.mUsers.get(userId);
            if (user == null) {
                return null;
            }
            return user.getAllLaunchersForTest().get(android.content.pm.UserPackage.of(userId, packageName));
        }
    }

    com.android.server.pm.ShortcutRequestPinProcessor getShortcutRequestPinProcessorForTest() {
        return this.mShortcutRequestPinProcessor;
    }

    boolean injectShouldPerformVerification() {
        return false;
    }

    final void verifyStates() {
        if (injectShouldPerformVerification()) {
            verifyStatesInner();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void verifyStatesForce() {
        verifyStatesInner();
    }

    private void verifyStatesInner() {
        synchronized (this.mServiceLock) {
            forEachLoadedUserLocked(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda15
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.pm.ShortcutUser) obj).forAllPackageItems(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda24
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj2) {
                            ((com.android.server.pm.ShortcutPackageItem) obj2).verifyStates();
                        }
                    });
                }
            });
        }
    }

    void waitForBitmapSavesForTest() {
        synchronized (this.mServiceLock) {
            forEachLoadedUserLocked(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda19
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.pm.ShortcutUser) obj).forAllPackageItems(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda6
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj2) {
                            ((com.android.server.pm.ShortcutPackageItem) obj2).waitForBitmapSaves();
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.content.pm.ShortcutInfo> prepareChangedShortcuts(android.util.ArraySet<java.lang.String> changedIds, android.util.ArraySet<java.lang.String> newIds, java.util.List<android.content.pm.ShortcutInfo> deletedList, com.android.server.pm.ShortcutPackage ps) {
        if (ps == null) {
            return null;
        }
        if (com.android.internal.util.CollectionUtils.isEmpty(changedIds) && com.android.internal.util.CollectionUtils.isEmpty(newIds)) {
            return null;
        }
        final android.util.ArraySet<java.lang.String> resultIds = new android.util.ArraySet<>();
        if (!com.android.internal.util.CollectionUtils.isEmpty(changedIds)) {
            resultIds.addAll((android.util.ArraySet<? extends java.lang.String>) changedIds);
        }
        if (!com.android.internal.util.CollectionUtils.isEmpty(newIds)) {
            resultIds.addAll((android.util.ArraySet<? extends java.lang.String>) newIds);
        }
        if (!com.android.internal.util.CollectionUtils.isEmpty(deletedList)) {
            deletedList.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda11
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return resultIds.contains(((android.content.pm.ShortcutInfo) obj).getId());
                }
            });
        }
        java.util.List<android.content.pm.ShortcutInfo> result = new java.util.ArrayList<>();
        ps.findAll(result, new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutService$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return resultIds.contains(((android.content.pm.ShortcutInfo) obj).getId());
            }
        }, 4);
        return result;
    }

    private java.util.List<android.content.pm.ShortcutInfo> prepareChangedShortcuts(java.util.List<android.content.pm.ShortcutInfo> changedList, java.util.List<android.content.pm.ShortcutInfo> newList, java.util.List<android.content.pm.ShortcutInfo> deletedList, com.android.server.pm.ShortcutPackage ps) {
        android.util.ArraySet<java.lang.String> changedIds = new android.util.ArraySet<>();
        addShortcutIdsToSet(changedIds, changedList);
        android.util.ArraySet<java.lang.String> newIds = new android.util.ArraySet<>();
        addShortcutIdsToSet(newIds, newList);
        return prepareChangedShortcuts(changedIds, newIds, deletedList, ps);
    }

    private void addShortcutIdsToSet(android.util.ArraySet<java.lang.String> ids, java.util.List<android.content.pm.ShortcutInfo> shortcuts) {
        if (com.android.internal.util.CollectionUtils.isEmpty(shortcuts)) {
            return;
        }
        int size = shortcuts.size();
        for (int i = 0; i < size; i++) {
            ids.add(shortcuts.get(i).getId());
        }
    }

    public com.android.server.pm.IShortcutServiceWrapper getWrapper() {
        return this.mShortcutWrapper;
    }

    private class ShortcutServiceWrapper implements com.android.server.pm.IShortcutServiceWrapper {
        private ShortcutServiceWrapper() {
        }

        @Override // com.android.server.pm.IShortcutServiceWrapper
        public com.android.server.pm.IShortcutServiceExt getExtImpl() {
            return com.android.server.pm.ShortcutService.this.mShortcutServiceExt;
        }
    }
}
