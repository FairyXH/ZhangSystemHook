package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityTaskManagerService extends android.app.IActivityTaskManager.Stub {
    static final long ACTIVITY_BG_START_GRACE_PERIOD_MS = 10000;
    static final boolean ANIMATE = true;
    static final int APP_SWITCH_ALLOW = 2;
    static final int APP_SWITCH_DISALLOW = 0;
    static final int APP_SWITCH_FG_ONLY = 1;
    static final int DEMOTE_TOP_REASON_ANIMATING_RECENTS = 2;
    static final int DEMOTE_TOP_REASON_DURING_UNLOCKING = 1;
    private static final long DOZE_ANIMATING_STATE_RETAIN_TIME_MS = 2000;
    public static final java.lang.String DUMP_ACTIVITIES_CMD = "activities";
    public static final java.lang.String DUMP_ACTIVITIES_SHORT_CMD = "a";
    public static final java.lang.String DUMP_CONTAINERS_CMD = "containers";
    public static final java.lang.String DUMP_LASTANR_CMD = "lastanr";
    public static final java.lang.String DUMP_LASTANR_TRACES_CMD = "lastanr-traces";
    public static final java.lang.String DUMP_RECENTS_CMD = "recents";
    public static final java.lang.String DUMP_RECENTS_SHORT_CMD = "r";
    public static final java.lang.String DUMP_STARTER_CMD = "starter";
    public static final java.lang.String DUMP_TOP_RESUMED_ACTIVITY = "top-resumed";
    public static final java.lang.String DUMP_VISIBLE_ACTIVITIES = "visible";
    static final long INSTRUMENTATION_KEY_DISPATCHING_TIMEOUT_MILLIS = 60000;
    private static final java.lang.String KEY_MATERIAL_COLOR = "material_color_value";
    static final int LAYOUT_REASON_CONFIG_CHANGED = 1;
    static final int LAYOUT_REASON_VISIBILITY_CHANGED = 2;
    private static final int PENDING_ASSIST_EXTRAS_LONG_TIMEOUT = 2000;
    private static final int PENDING_ASSIST_EXTRAS_TIMEOUT = 500;
    private static final int PENDING_AUTOFILL_ASSIST_STRUCTURE_TIMEOUT = 2000;
    static final int POWER_MODE_REASON_ALL = 3;
    static final int POWER_MODE_REASON_CHANGE_DISPLAY = 2;
    static final int POWER_MODE_REASON_START_ACTIVITY = 1;
    static final int POWER_MODE_REASON_UNKNOWN_VISIBILITY = 4;
    private static final long POWER_MODE_UNKNOWN_VISIBILITY_TIMEOUT_MS = 1000;
    public static final int RELAUNCH_REASON_FREE_RESIZE = 2;
    public static final int RELAUNCH_REASON_NONE = 0;
    public static final int RELAUNCH_REASON_WINDOWING_MODE_RESIZE = 1;
    private static final long RESUME_FG_APP_SWITCH_MS = 500;
    private volatile android.content.ComponentName mActiveDreamComponent;
    android.content.ComponentName mActiveVoiceInteractionServiceComponent;
    com.android.server.wm.ActivityClientController mActivityClientController;
    private com.android.server.wm.ActivityStartController mActivityStartController;
    android.app.ActivityManagerInternal mAmInternal;
    private android.app.AppOpsManager mAppOpsManager;
    com.android.server.wm.AppWarnings mAppWarnings;
    private com.android.server.wm.BackgroundActivityStartCallback mBackgroundActivityStartCallback;
    com.android.server.wm.CompatModePackages mCompatModePackages;
    private int mConfigurationSeq;
    android.content.Context mContext;
    com.android.server.am.AppTimeTracker mCurAppTimeTracker;
    volatile int mDemoteTopAppReasons;
    boolean mDevEnableNonResizableMultiWindow;
    boolean mForceResizableActivities;
    private int mGlobalAssetsSeq;
    com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal mGrammaticalManagerInternal;
    com.android.server.wm.ActivityTaskManagerService.H mH;
    boolean mHasCompanionDeviceSetupFeature;
    boolean mHasHeavyWeightFeature;
    boolean mHasLeanbackFeature;
    volatile com.android.server.wm.WindowProcessController mHeavyWeightProcess;
    volatile com.android.server.wm.WindowProcessController mHomeProcess;
    com.android.server.firewall.IntentFirewall mIntentFirewall;
    com.android.server.wm.KeyguardController mKeyguardController;
    java.lang.String mLastANRState;
    com.android.server.wm.ActivityRecord mLastResumedActivity;
    private volatile long mLastStopAppSwitchesTime;
    private int mLayoutReasons;
    private com.android.server.wm.LockTaskController mLockTaskController;
    float mMinPercentageMultiWindowSupportHeight;
    float mMinPercentageMultiWindowSupportWidth;
    com.android.server.wm.PackageConfigPersister mPackageConfigPersister;
    com.android.server.am.PendingIntentController mPendingIntentController;
    private com.android.server.policy.PermissionPolicyInternal mPermissionPolicyInternal;
    private android.content.pm.PackageManagerInternal mPmInternal;
    private android.os.PowerManagerInternal mPowerManagerInternal;
    private int mPowerModeReasons;
    volatile com.android.server.wm.WindowProcessController mPreviousProcess;
    private long mPreviousProcessVisibleTime;
    private com.android.server.wm.RecentTasks mRecentTasks;
    int mRespectsActivityMinWidthHeightMultiWindow;
    private volatile boolean mRetainPowerModeAndTopProcessState;
    com.android.server.wm.RootWindowContainer mRootWindowContainer;
    android.service.voice.IVoiceInteractionSession mRunningVoice;
    private com.android.server.wm.ActivityTaskManagerService.SettingObserver mSettingsObserver;
    volatile boolean mShuttingDown;
    private volatile boolean mSleeping;
    private com.android.server.statusbar.StatusBarManagerInternal mStatusBarManagerInternal;
    boolean mSupportsExpandedPictureInPicture;
    boolean mSupportsFreeformWindowManagement;
    boolean mSupportsMultiDisplay;
    boolean mSupportsMultiWindow;
    int mSupportsNonResizableMultiWindow;
    boolean mSupportsPictureInPicture;
    boolean mSupportsSplitScreenMultiWindow;
    boolean mSuppressResizeConfigChanges;
    private android.content.ComponentName mSysUiServiceComponent;
    private com.android.server.wm.TaskChangeNotificationController mTaskChangeNotificationController;
    public com.android.server.wm.ActivityTaskSupervisor mTaskSupervisor;
    private int mThumbnailHeight;
    private int mThumbnailWidth;
    volatile com.android.server.wm.WindowProcessController mTopApp;
    android.content.ComponentName mTopComponent;
    java.lang.String mTopData;
    private com.android.server.wm.ActivityRecord mTracedResumedActivity;
    com.android.server.uri.UriGrantsManagerInternal mUgmInternal;
    com.android.server.wm.ActivityTaskManagerService.UiHandler mUiHandler;
    private android.app.usage.UsageStatsManagerInternal mUsageStatsInternal;
    private com.android.server.pm.UserManagerService mUserManager;
    volatile com.android.server.wm.WindowProcessController mVisibleDozeUiProcess;
    android.os.PowerManager.WakeLock mVoiceWakeLock;
    com.android.server.wm.VrController mVrController;
    private com.android.server.wallpaper.WallpaperManagerInternal mWallpaperManagerInternal;
    com.android.server.wm.WindowManagerService mWindowManager;
    private static final java.lang.String TAG = "ActivityTaskManager";
    static final java.lang.String TAG_ROOT_TASK = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_ROOT_TASK;
    static final java.lang.String TAG_SWITCH = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_SWITCH;
    static boolean LTW_DISABLE = android.os.SystemProperties.getBoolean("persist.sys.ltw.disable", false);
    static com.android.server.wm.IActivityTaskManagerServiceExt mActivityTaskManagerExt = (com.android.server.wm.IActivityTaskManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityTaskManagerServiceExt.class).create();
    private static java.lang.Boolean sIsPip2ExperimentEnabled = null;
    final java.util.ArrayList<com.android.server.wm.ActivityRecord> mStartingProcessActivities = new java.util.ArrayList<>();
    final com.android.server.wm.WindowManagerGlobalLock mGlobalLock = new com.android.server.wm.WindowManagerGlobalLock();
    final java.lang.Object mGlobalLockWithoutBoost = this.mGlobalLock;
    final com.android.server.wm.MirrorActiveUids mActiveUids = new com.android.server.wm.MirrorActiveUids();
    final com.android.internal.app.ProcessMap<com.android.server.wm.WindowProcessController> mProcessNames = new com.android.internal.app.ProcessMap<>();
    final com.android.server.wm.WindowProcessControllerMap mProcessMap = new com.android.server.wm.WindowProcessControllerMap();
    private boolean mKeyguardShown = false;
    private int mViSessionId = 1000;
    private final java.util.ArrayList<com.android.server.wm.ActivityTaskManagerService.PendingAssistExtras> mPendingAssistExtras = new java.util.ArrayList<>();
    private final java.util.Map<java.lang.Integer, java.util.Set<java.lang.Integer>> mCompanionAppUidsMap = new android.util.ArrayMap();
    private android.util.SparseArray<com.android.server.wm.ActivityInterceptorCallback> mActivityInterceptorCallbacks = new android.util.SparseArray<>();
    final com.android.server.wm.ActivityTaskManagerService.UpdateConfigurationResult mTmpUpdateConfigurationResult = new com.android.server.wm.ActivityTaskManagerService.UpdateConfigurationResult();
    private java.lang.String[] mSupportedSystemLocales = null;
    private android.content.res.Configuration mTempConfig = new android.content.res.Configuration();
    private volatile int mAppSwitchesState = 2;
    private final java.util.List<android.app.AnrController> mAnrController = new java.util.ArrayList();
    public android.app.IActivityController mController = null;
    boolean mControllerIsAMonkey = false;
    java.lang.String mTopAction = "android.intent.action.MAIN";
    java.lang.String mProfileApp = null;
    com.android.server.wm.WindowProcessController mProfileProc = null;
    android.app.ProfilerInfo mProfilerInfo = null;
    private final android.os.UpdateLock mUpdateLock = new android.os.UpdateLock("immersive");
    final android.util.SparseArray<android.util.ArrayMap<java.lang.String, java.lang.Integer>> mAllowAppSwitchUids = new android.util.SparseArray<>();
    final java.util.List<com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver> mScreenObservers = java.util.Collections.synchronizedList(new java.util.ArrayList());
    int mVr2dDisplayId = -1;
    volatile int mTopProcessState = 2;
    private boolean mShowDialogs = true;
    private int[] mAccessibilityServiceUids = new int[0];
    private int mDeviceOwnerUid = -1;
    private com.android.server.wm.IFlexibleWindowManagerExt mFlexibleWindowManagerExt = (com.android.server.wm.IFlexibleWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IFlexibleWindowManagerExt.class).base(this).create();
    public com.android.server.wm.IActivityTaskManagerServiceSocExt mSocExt = (com.android.server.wm.IActivityTaskManagerServiceSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityTaskManagerServiceSocExt.class).base(this).create();
    private java.util.Set<java.lang.Integer> mProfileOwnerUids = new android.util.ArraySet();
    private volatile long mMaterialColor = -1;
    private final java.lang.Runnable mUpdateOomAdjRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService.1
        @Override // java.lang.Runnable
        public void run() {
            com.android.server.wm.ActivityTaskManagerService.this.mAmInternal.updateOomAdj(1);
        }
    };
    private final com.android.server.wm.ActivityTaskManagerService.ActivityTaskManagerServiceWrapper mAtmsWrapper = new com.android.server.wm.ActivityTaskManagerService.ActivityTaskManagerServiceWrapper();
    final int mFactoryTest = android.os.FactoryTest.getMode();
    final android.app.ActivityThread mSystemThread = android.app.ActivityThread.currentActivityThread();
    private final android.content.Context mUiContext = this.mSystemThread.getSystemUiContext();
    private final com.android.server.wm.ClientLifecycleManager mLifecycleManager = new com.android.server.wm.ClientLifecycleManager();
    final com.android.server.wm.VisibleActivityProcessTracker mVisibleActivityProcessTracker = new com.android.server.wm.VisibleActivityProcessTracker(this);
    final com.android.server.wm.ActivityTaskManagerInternal mInternal = new com.android.server.wm.ActivityTaskManagerService.LocalService();
    final int GL_ES_VERSION = android.os.SystemProperties.getInt("ro.opengles.version", 0);
    com.android.server.wm.WindowOrganizerController mWindowOrganizerController = new com.android.server.wm.WindowOrganizerController(this);
    com.android.server.wm.TaskOrganizerController mTaskOrganizerController = this.mWindowOrganizerController.mTaskOrganizerController;
    com.android.server.wm.TaskFragmentOrganizerController mTaskFragmentOrganizerController = this.mWindowOrganizerController.mTaskFragmentOrganizerController;
    final com.android.server.wm.BackNavigationController mBackNavigationController = new com.android.server.wm.BackNavigationController();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface AppSwitchState {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface DemoteTopReason {
    }

    @java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface HotPath {
        public static final int LRU_UPDATE = 2;
        public static final int NONE = 0;
        public static final int OOM_ADJUSTMENT = 1;
        public static final int PROCESS_CHANGE = 3;
        public static final int START_SERVICE = 4;

        int caller() default 0;
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface LayoutReason {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface PowerModeReason {
    }

    static final class UpdateConfigurationResult {
        boolean activityRelaunched;
        int changes;
        boolean mIsUpdating;

        UpdateConfigurationResult() {
        }
    }

    private final class SettingObserver extends android.database.ContentObserver {
        private final android.net.Uri mFontScaleUri;
        private final android.net.Uri mFontWeightAdjustmentUri;
        private final android.net.Uri mHideErrorDialogsUri;
        private final android.net.Uri mMaterialColorUri;

        SettingObserver() {
            super(com.android.server.wm.ActivityTaskManagerService.this.mH);
            this.mFontScaleUri = android.provider.Settings.System.getUriFor("font_scale");
            this.mHideErrorDialogsUri = android.provider.Settings.Global.getUriFor("hide_error_dialogs");
            this.mFontWeightAdjustmentUri = android.provider.Settings.Secure.getUriFor("font_weight_adjustment");
            this.mMaterialColorUri = android.provider.Settings.System.getUriFor(com.android.server.wm.ActivityTaskManagerService.KEY_MATERIAL_COLOR);
            android.content.ContentResolver resolver = com.android.server.wm.ActivityTaskManagerService.this.mContext.getContentResolver();
            resolver.registerContentObserver(this.mFontScaleUri, false, this, -1);
            resolver.registerContentObserver(this.mHideErrorDialogsUri, false, this, -1);
            resolver.registerContentObserver(this.mFontWeightAdjustmentUri, false, this, -1);
            resolver.registerContentObserver(this.mMaterialColorUri, false, this, -1);
        }

        public void onChange(boolean selfChange, java.util.Collection<android.net.Uri> uris, int flags, int userId) {
            for (android.net.Uri uri : uris) {
                if (this.mFontScaleUri.equals(uri)) {
                    com.android.server.wm.ActivityTaskManagerService.this.updateFontScaleIfNeeded(userId);
                } else if (this.mHideErrorDialogsUri.equals(uri)) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            com.android.server.wm.ActivityTaskManagerService.this.updateShouldShowDialogsLocked(com.android.server.wm.ActivityTaskManagerService.this.getGlobalConfiguration());
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } else if (this.mFontWeightAdjustmentUri.equals(uri)) {
                    com.android.server.wm.ActivityTaskManagerService.this.updateFontWeightAdjustmentIfNeeded(userId);
                } else if (this.mMaterialColorUri.equals(uri)) {
                    com.android.server.wm.ActivityTaskManagerService.this.updateMaterialColorValues(com.android.server.wm.ActivityTaskManagerService.this.getCurrentUserId());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMaterialColorValues(int userId) {
        try {
            this.mMaterialColor = android.provider.Settings.System.getLongForUser(this.mContext.getContentResolver(), KEY_MATERIAL_COLOR, userId);
        } catch (java.lang.Exception e) {
            this.mMaterialColor = -1L;
        }
    }

    public ActivityTaskManagerService(android.content.Context context) {
        this.mContext = context;
        mActivityTaskManagerExt.hookInitOplusATMSEnhance(this);
        this.mFlexibleWindowManagerExt.init(this);
    }

    public void onSystemReady() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                android.content.pm.PackageManager pm = this.mContext.getPackageManager();
                this.mHasHeavyWeightFeature = pm.hasSystemFeature("android.software.cant_save_state");
                this.mHasLeanbackFeature = pm.hasSystemFeature("android.software.leanback");
                this.mHasCompanionDeviceSetupFeature = pm.hasSystemFeature("android.software.companion_device_setup");
                this.mVrController.onSystemReady();
                this.mRecentTasks.onSystemReadyLocked();
                this.mTaskSupervisor.onSystemReady();
                this.mActivityClientController.onSystemReady();
                this.mAppWarnings.onSystemReady();
                mActivityTaskManagerExt.onSystemReady();
                com.android.server.wm.ActivitySecurityModelFeatureFlags.initialize(this.mContext.getMainExecutor());
                this.mGrammaticalManagerInternal = (com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal) com.android.server.LocalServices.getService(com.android.server.grammaticalinflection.GrammaticalInflectionManagerInternal.class);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        mActivityTaskManagerExt.systemReady();
    }

    public void onInitPowerManagement() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mTaskSupervisor.initPowerManagement();
                android.os.PowerManager pm = (android.os.PowerManager) this.mContext.getSystemService("power");
                this.mPowerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
                this.mVoiceWakeLock = pm.newWakeLock(1, "*voice*");
                this.mVoiceWakeLock.setReferenceCounted(false);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void installSystemProviders() {
        this.mSettingsObserver = new com.android.server.wm.ActivityTaskManagerService.SettingObserver();
    }

    /* JADX WARN: Removed duplicated region for block: B:67:0x0163 A[Catch: all -> 0x01a6, TryCatch #2 {all -> 0x01a6, blocks: (B:65:0x0156, B:67:0x0163, B:68:0x0179, B:69:0x0193, B:76:0x01a1), top: B:85:0x00fc }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void retrieveSettings(android.content.ContentResolver r27) {
        /*
            Method dump skipped, instruction units count: 424
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityTaskManagerService.retrieveSettings(android.content.ContentResolver):void");
    }

    public com.android.server.wm.WindowManagerGlobalLock getGlobalLock() {
        return this.mGlobalLock;
    }

    public com.android.server.wm.ActivityTaskManagerInternal getAtmInternal() {
        return this.mInternal;
    }

    public void initialize(com.android.server.firewall.IntentFirewall intentFirewall, com.android.server.am.PendingIntentController intentController, android.os.Looper looper) {
        this.mH = new com.android.server.wm.ActivityTaskManagerService.H(looper);
        this.mUiHandler = new com.android.server.wm.ActivityTaskManagerService.UiHandler();
        this.mIntentFirewall = intentFirewall;
        java.io.File systemDir = com.android.server.SystemServiceManager.ensureSystemDir();
        this.mAppWarnings = createAppWarnings(this.mUiContext, this.mH, this.mUiHandler, systemDir);
        this.mCompatModePackages = new com.android.server.wm.CompatModePackages(this, systemDir, this.mH);
        this.mPendingIntentController = intentController;
        this.mTaskSupervisor = createTaskSupervisor();
        this.mActivityClientController = new com.android.server.wm.ActivityClientController(this);
        this.mTaskChangeNotificationController = new com.android.server.wm.TaskChangeNotificationController(this.mTaskSupervisor, this.mH);
        this.mLockTaskController = new com.android.server.wm.LockTaskController(this.mContext, this.mTaskSupervisor, this.mH, this.mTaskChangeNotificationController);
        this.mActivityStartController = new com.android.server.wm.ActivityStartController(this);
        setRecentTasks(new com.android.server.wm.RecentTasks(this, this.mTaskSupervisor));
        this.mVrController = new com.android.server.wm.VrController(this.mGlobalLock);
        this.mKeyguardController = this.mTaskSupervisor.getKeyguardController();
        this.mPackageConfigPersister = new com.android.server.wm.PackageConfigPersister(this.mTaskSupervisor.mPersisterQueue, this);
    }

    public void onActivityManagerInternalAdded() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAmInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
                this.mUgmInternal = (com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    int increaseConfigurationSeqLocked() {
        int i = this.mConfigurationSeq + 1;
        this.mConfigurationSeq = i;
        this.mConfigurationSeq = java.lang.Math.max(i, 1);
        return this.mConfigurationSeq;
    }

    protected com.android.server.wm.ActivityTaskSupervisor createTaskSupervisor() {
        com.android.server.wm.ActivityTaskSupervisor supervisor = new com.android.server.wm.ActivityTaskSupervisor(this, this.mH.getLooper());
        supervisor.initialize();
        return supervisor;
    }

    protected com.android.server.wm.AppWarnings createAppWarnings(android.content.Context uiContext, android.os.Handler handler, android.os.Handler uiHandler, java.io.File systemDir) {
        return new com.android.server.wm.AppWarnings(this, uiContext, handler, uiHandler, systemDir);
    }

    public void setWindowManager(com.android.server.wm.WindowManagerService wm) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mWindowManager = wm;
                this.mRootWindowContainer = wm.mRoot;
                this.mWindowOrganizerController.mTransitionController.setWindowManager(wm);
                this.mLifecycleManager.setWindowManager(wm);
                this.mTempConfig.setToDefaults();
                this.mTempConfig.setLocales(android.os.LocaleList.getDefault());
                this.mTempConfig.seq = 1;
                this.mConfigurationSeq = 1;
                this.mRootWindowContainer.onConfigurationChanged(this.mTempConfig);
                this.mLockTaskController.setWindowManager(wm);
                this.mTaskSupervisor.setWindowManager(wm);
                this.mRootWindowContainer.setWindowManager(wm);
                this.mBackNavigationController.setWindowManager(wm);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void setUsageStatsManager(android.app.usage.UsageStatsManagerInternal usageStatsManager) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mUsageStatsInternal = usageStatsManager;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    android.content.Context getUiContext() {
        return this.mUiContext;
    }

    com.android.server.pm.UserManagerService getUserManager() {
        if (this.mUserManager == null) {
            android.os.IBinder b = android.os.ServiceManager.getService("user");
            this.mUserManager = android.os.IUserManager.Stub.asInterface(b);
        }
        return this.mUserManager;
    }

    android.app.AppOpsManager getAppOpsManager() {
        if (this.mAppOpsManager == null) {
            this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        }
        return this.mAppOpsManager;
    }

    boolean hasUserRestriction(java.lang.String restriction, int userId) {
        return getUserManager().hasUserRestriction(restriction, userId);
    }

    boolean hasSystemAlertWindowPermission(int callingUid, int callingPid, java.lang.String callingPackage) {
        int mode = getAppOpsManager().noteOpNoThrow(24, callingUid, callingPackage, (java.lang.String) null, "");
        return mode == 3 ? checkPermission("android.permission.SYSTEM_ALERT_WINDOW", callingPid, callingUid) == 0 : mode == 0;
    }

    protected void setRecentTasks(com.android.server.wm.RecentTasks recentTasks) {
        this.mRecentTasks = recentTasks;
        this.mTaskSupervisor.setRecentTasks(recentTasks);
    }

    com.android.server.wm.RecentTasks getRecentTasks() {
        return this.mRecentTasks;
    }

    com.android.server.wm.ClientLifecycleManager getLifecycleManager() {
        return this.mLifecycleManager;
    }

    com.android.server.wm.ActivityStartController getActivityStartController() {
        return this.mActivityStartController;
    }

    com.android.server.wm.TaskChangeNotificationController getTaskChangeNotificationController() {
        return this.mTaskChangeNotificationController;
    }

    com.android.server.wm.LockTaskController getLockTaskController() {
        return this.mLockTaskController;
    }

    com.android.server.wm.TransitionController getTransitionController() {
        return this.mWindowOrganizerController.getTransitionController();
    }

    private android.content.res.Configuration getGlobalConfigurationForCallingPid() {
        int pid = android.os.Binder.getCallingPid();
        if (pid == com.android.server.wm.WindowManagerService.MY_PID || pid < 0) {
            return getGlobalConfiguration();
        }
        com.android.server.wm.WindowProcessController app = this.mProcessMap.getProcess(pid);
        return app != null ? app.getConfiguration() : getGlobalConfiguration();
    }

    public android.content.pm.ConfigurationInfo getDeviceConfigurationInfo() {
        android.content.pm.ConfigurationInfo config = new android.content.pm.ConfigurationInfo();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                android.content.res.Configuration globalConfig = getGlobalConfigurationForCallingPid();
                config.reqTouchScreen = globalConfig.touchscreen;
                config.reqKeyboardType = globalConfig.keyboard;
                config.reqNavigation = globalConfig.navigation;
                if (globalConfig.navigation == 2 || globalConfig.navigation == 3) {
                    config.reqInputFeatures |= 2;
                }
                if (globalConfig.keyboard != 0 && globalConfig.keyboard != 1) {
                    config.reqInputFeatures |= 1;
                }
                config.reqGlEsVersion = this.GL_ES_VERSION;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return config;
    }

    public com.android.server.wm.BackgroundActivityStartCallback getBackgroundActivityStartCallback() {
        return this.mBackgroundActivityStartCallback;
    }

    android.util.SparseArray<com.android.server.wm.ActivityInterceptorCallback> getActivityInterceptorCallbacks() {
        return this.mActivityInterceptorCallbacks;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void start() {
        com.android.server.LocalServices.addService(com.android.server.wm.ActivityTaskManagerInternal.class, this.mInternal);
        mActivityTaskManagerExt.onOplusStart();
        mActivityTaskManagerExt.init(this.mUiContext);
        mActivityTaskManagerExt.publish();
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private final com.android.server.wm.ActivityTaskManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
            com.android.server.wm.ActivityTaskManagerService oplusAtms = com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.getActivityTaskManagerService(context);
            if (oplusAtms != null) {
                this.mService = oplusAtms;
            } else {
                this.mService = new com.android.server.wm.ActivityTaskManagerService(context);
            }
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            publishBinderService("activity_task", this.mService);
            this.mService.start();
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocked(com.android.server.SystemService.TargetUser user) {
            com.android.server.wm.WindowManagerGlobalLock globalLock = this.mService.getGlobalLock();
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (globalLock) {
                try {
                    this.mService.mTaskSupervisor.onUserUnlocked(user.getUserIdentifier());
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE) {
                this.mService.getWrapper().getExtImpl().getRemoteTaskManager().setRootWindowContainer(this.mService.mRootWindowContainer);
            }
        }

        @Override // com.android.server.SystemService
        public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
            this.mService.updateMaterialColorValues(to.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStopped(com.android.server.SystemService.TargetUser user) {
            com.android.server.wm.WindowManagerGlobalLock globalLock = this.mService.getGlobalLock();
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (globalLock) {
                try {
                    this.mService.mTaskSupervisor.mLaunchParamsPersister.onCleanupUser(user.getUserIdentifier());
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        public com.android.server.wm.ActivityTaskManagerService getService() {
            return this.mService;
        }
    }

    public final int startActivity(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle bOptions) {
        return startActivityAsUser(caller, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, profilerInfo, bOptions, android.os.UserHandle.getCallingUserId());
    }

    public final int startActivities(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent[] intents, java.lang.String[] resolvedTypes, android.os.IBinder resultTo, android.os.Bundle bOptions, int userId) {
        assertPackageMatchesCallingUid(callingPackage);
        enforceNotIsolatedCaller("startActivities");
        return getActivityStartController().startActivities(caller, -1, 0, -1, callingPackage, callingFeatureId, intents, resolvedTypes, resultTo, com.android.server.wm.SafeActivityOptions.fromBundle(bOptions), handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "startActivities"), "startActivities", null, android.app.BackgroundStartPrivileges.NONE);
    }

    public int startActivityAsUser(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle bOptions, int userId) {
        return startActivityAsUser(caller, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, profilerInfo, bOptions, userId, true);
    }

    static boolean isSdkSandboxActivityIntent(android.content.Context context, android.content.Intent intent) {
        return intent != null && (!com.android.sdksandbox.flags.Flags.sandboxActivitySdkBasedContext() ? !intent.isSandboxActivity(context) : !android.app.sdksandbox.sandboxactivity.SdkSandboxActivityAuthority.isSdkSandboxActivityIntent(context, intent));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int startActivityAsUser(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle bOptions, int userId, boolean validateIncomingUser) {
        com.android.server.wm.SafeActivityOptions opts = com.android.server.wm.SafeActivityOptions.fromBundle(bOptions);
        assertPackageMatchesCallingUid(callingPackage);
        enforceNotIsolatedCaller("startActivityAsUser");
        mActivityTaskManagerExt.notifyStartActivity(intent, callingPackage, this, opts);
        if (isSdkSandboxActivityIntent(this.mContext, intent)) {
            ((com.android.server.sdksandbox.SdkSandboxManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.sdksandbox.SdkSandboxManagerLocal.class)).enforceAllowedToHostSandboxedActivity(intent, android.os.Binder.getCallingUid(), callingPackage);
        }
        if (android.os.Process.isSdkSandboxUid(android.os.Binder.getCallingUid())) {
            com.android.server.sdksandbox.SdkSandboxManagerLocal sdkSandboxManagerLocal = (com.android.server.sdksandbox.SdkSandboxManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.sdksandbox.SdkSandboxManagerLocal.class);
            if (sdkSandboxManagerLocal == null) {
                throw new java.lang.IllegalStateException("SdkSandboxManagerLocal not found when starting an activity from an SDK sandbox uid.");
            }
            sdkSandboxManagerLocal.enforceAllowedToStartActivity(intent);
        }
        return getActivityStartController().obtainStarter(intent, "startActivityAsUser").setCaller(caller).setCallingPackage(callingPackage).setCallingFeatureId(callingFeatureId).setResolvedType(resolvedType).setResultTo(resultTo).setResultWho(resultWho).setRequestCode(requestCode).setStartFlags(startFlags).setProfilerInfo(profilerInfo).setActivityOptions(opts).setUserId(getActivityStartController().checkTargetUser(userId, validateIncomingUser, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), "startActivityAsUser")).execute();
    }

    public int startActivityIntentSender(android.app.IApplicationThread caller, android.content.IIntentSender target, android.os.IBinder allowlistToken, android.content.Intent fillInIntent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int flagsMask, int flagsValues, android.os.Bundle bOptions) {
        enforceNotIsolatedCaller("startActivityIntentSender");
        if (fillInIntent != null) {
            if (fillInIntent.hasFileDescriptors()) {
                throw new java.lang.IllegalArgumentException("File descriptors passed in Intent");
            }
            fillInIntent.removeExtendedFlags(1);
        }
        if (!(target instanceof com.android.server.am.PendingIntentRecord)) {
            throw new java.lang.IllegalArgumentException("Bad PendingIntent object");
        }
        com.android.server.am.PendingIntentRecord pir = (com.android.server.am.PendingIntentRecord) target;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task topFocusedRootTask = getTopDisplayFocusedRootTask();
                if (topFocusedRootTask != null && topFocusedRootTask.getTopResumedActivity() != null && topFocusedRootTask.getTopResumedActivity().info.applicationInfo.uid == android.os.Binder.getCallingUid()) {
                    this.mAppSwitchesState = 2;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return pir.sendInner(caller, 0, fillInIntent, resolvedType, allowlistToken, null, null, resultTo, resultWho, requestCode, flagsMask, flagsValues, bOptions);
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x00ac, code lost:
    
        r8 = r8 + r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00ad, code lost:
    
        if (r8 >= r7) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00af, code lost:
    
        r10 = r0.get(r8).activityInfo;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00ba, code lost:
    
        r10 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00bc, code lost:
    
        if (r15 == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00be, code lost:
    
        android.util.Slog.v(com.android.server.wm.ActivityTaskManagerService.TAG, "Next matching activity: found current " + r0.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + r0.info.name);
        r12 = new java.lang.StringBuilder().append("Next matching activity: next is ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00f3, code lost:
    
        if (r10 != null) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00f5, code lost:
    
        r14 = "null";
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00f9, code lost:
    
        r14 = r10.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + r10.name;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0114, code lost:
    
        android.util.Slog.v(com.android.server.wm.ActivityTaskManagerService.TAG, r12.append(r14).toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0121, code lost:
    
        r16 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0124, code lost:
    
        r16 = r10;
     */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0139 A[Catch: all -> 0x0223, TRY_ENTER, TryCatch #4 {all -> 0x0223, blocks: (B:25:0x0044, B:29:0x0065, B:31:0x006d, B:33:0x0079, B:35:0x0083, B:39:0x008c, B:41:0x009e, B:43:0x00ac, B:45:0x00af, B:49:0x00be, B:53:0x0114, B:52:0x00f9, B:66:0x0139, B:68:0x013e, B:69:0x0145, B:72:0x014a, B:74:0x0177, B:75:0x017a, B:93:0x0205, B:94:0x0208, B:103:0x0218, B:104:0x021c, B:106:0x021e), top: B:119:0x001f }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x014a A[Catch: all -> 0x0223, TRY_ENTER, TryCatch #4 {all -> 0x0223, blocks: (B:25:0x0044, B:29:0x0065, B:31:0x006d, B:33:0x0079, B:35:0x0083, B:39:0x008c, B:41:0x009e, B:43:0x00ac, B:45:0x00af, B:49:0x00be, B:53:0x0114, B:52:0x00f9, B:66:0x0139, B:68:0x013e, B:69:0x0145, B:72:0x014a, B:74:0x0177, B:75:0x017a, B:93:0x0205, B:94:0x0208, B:103:0x0218, B:104:0x021c, B:106:0x021e), top: B:119:0x001f }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean startNextMatchingActivity(android.os.IBinder r21, android.content.Intent r22, android.os.Bundle r23) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 549
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityTaskManagerService.startNextMatchingActivity(android.os.IBinder, android.content.Intent, android.os.Bundle):boolean");
    }

    boolean isDreaming() {
        return this.mActiveDreamComponent != null;
    }

    boolean canLaunchDreamActivity(java.lang.String packageName) {
        if (this.mActiveDreamComponent == null || packageName == null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_DREAM_enabled[4]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mActiveDreamComponent);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(packageName);
                com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DREAM, -3811526397232923712L, 0, "Cannot launch dream activity due to invalid state. dream component: %s packageName: %s", protoLogParam0, protoLogParam1);
            }
            return false;
        }
        if (packageName.equals(this.mActiveDreamComponent.getPackageName())) {
            return true;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_DREAM_enabled[4]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(packageName);
            java.lang.String protoLogParam12 = java.lang.String.valueOf(java.lang.String.valueOf(this.mActiveDreamComponent));
            com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DREAM, -6981899770129924827L, 0, "Dream packageName does not match active dream. Package %s does not match %s", protoLogParam02, protoLogParam12);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.IAppTask startDreamActivityInternal(android.content.Intent intent, int callingUid, int callingPid) {
        android.app.IAppTask appTask;
        android.content.pm.ActivityInfo a = new android.content.pm.ActivityInfo();
        a.theme = android.R.style.Theme.DeviceDefault.Settings.DialogWhenLarge.NoActionBar;
        a.exported = true;
        a.name = android.service.dreams.DreamActivity.class.getName();
        a.enabled = true;
        a.persistableMode = 1;
        a.screenOrientation = -1;
        a.colorMode = 0;
        a.flags |= 8388640;
        a.configChanges = -1;
        if (com.android.internal.hidden_from_bootclasspath.android.service.controls.flags.Flags.homePanelDream()) {
            a.launchMode = 0;
            a.documentLaunchMode = 2;
        } else {
            a.resizeMode = 0;
            a.launchMode = 3;
        }
        android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic();
        options.setLaunchActivityType(5);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowProcessController process = this.mProcessMap.getProcess(callingPid);
                a.packageName = process.mInfo.packageName;
                a.applicationInfo = process.mInfo;
                a.processName = process.mName;
                a.uiOptions = process.mInfo.uiOptions;
                a.taskAffinity = "android:" + a.packageName + "/dream";
                com.android.server.wm.ActivityRecord[] outActivity = new com.android.server.wm.ActivityRecord[1];
                getActivityStartController().obtainStarter(intent, "dream").setCallingUid(callingUid).setCallingPid(callingPid).setCallingPackage(intent.getPackage()).setActivityInfo(a).setActivityOptions(createSafeActivityOptionsWithBalAllowed(options)).setOutActivity(outActivity).setRealCallingUid(android.os.Binder.getCallingUid()).setBackgroundStartPrivileges(android.app.BackgroundStartPrivileges.ALLOW_BAL).execute();
                com.android.server.wm.ActivityRecord started = outActivity[0];
                appTask = started == null ? null : new com.android.server.wm.AppTaskImpl(this, started.getTask().mTaskId, callingUid);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return appTask;
    }

    public final android.app.WaitResult startActivityAndWait(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle bOptions, int userId) {
        assertPackageMatchesCallingUid(callingPackage);
        android.app.WaitResult res = new android.app.WaitResult();
        enforceNotIsolatedCaller("startActivityAndWait");
        getActivityStartController().obtainStarter(intent, "startActivityAndWait").setCaller(caller).setCallingPackage(callingPackage).setCallingFeatureId(callingFeatureId).setResolvedType(resolvedType).setResultTo(resultTo).setResultWho(resultWho).setRequestCode(requestCode).setStartFlags(startFlags).setActivityOptions(bOptions).setUserId(handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "startActivityAndWait")).setProfilerInfo(profilerInfo).setWaitResult(res).execute();
        return res;
    }

    public final int startActivityWithConfig(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.content.res.Configuration config, android.os.Bundle bOptions, int userId) {
        assertPackageMatchesCallingUid(callingPackage);
        enforceNotIsolatedCaller("startActivityWithConfig");
        return getActivityStartController().obtainStarter(intent, "startActivityWithConfig").setCaller(caller).setCallingPackage(callingPackage).setCallingFeatureId(callingFeatureId).setResolvedType(resolvedType).setResultTo(resultTo).setResultWho(resultWho).setRequestCode(requestCode).setStartFlags(startFlags).setGlobalConfiguration(config).setActivityOptions(bOptions).setUserId(handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, "startActivityWithConfig")).execute();
    }

    public final int startActivityAsCaller(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle bOptions, boolean ignoreTargetSecurity, int userId) throws java.lang.Throwable {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (resultTo == null) {
                    throw new java.lang.SecurityException("Must be called from an activity");
                }
                try {
                    com.android.server.wm.ActivityRecord sourceRecord = com.android.server.wm.ActivityRecord.isInAnyTask(resultTo);
                    if (sourceRecord == null) {
                        throw new java.lang.SecurityException("Called with bad activity token: " + resultTo);
                    }
                    if (sourceRecord.app == null) {
                        throw new java.lang.SecurityException("Called without a process attached to activity");
                    }
                    if (checkCallingPermission("android.permission.START_ACTIVITY_AS_CALLER") != 0) {
                        if (!sourceRecord.info.packageName.equals(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME)) {
                            throw new java.lang.SecurityException("Must be called from an activity that is declared in the android package");
                        }
                        if (android.os.UserHandle.getAppId(sourceRecord.app.mUid) != 1000 && sourceRecord.app.mUid != sourceRecord.launchedFromUid) {
                            throw new java.lang.SecurityException("Calling activity in uid " + sourceRecord.app.mUid + " must be system uid or original calling uid " + sourceRecord.launchedFromUid);
                        }
                    }
                    if (ignoreTargetSecurity) {
                        if (intent.getComponent() == null) {
                            throw new java.lang.SecurityException("Component must be specified with ignoreTargetSecurity");
                        }
                        if (intent.getSelector() != null) {
                            throw new java.lang.SecurityException("Selector not allowed with ignoreTargetSecurity");
                        }
                    }
                    int targetUid = sourceRecord.launchedFromUid;
                    java.lang.String targetPackage = sourceRecord.launchedFromPackage;
                    java.lang.String targetFeatureId = sourceRecord.launchedFromFeatureId;
                    boolean isResolver = sourceRecord.isResolverOrChildActivity();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    int userId2 = userId;
                    if (userId2 == -10000) {
                        userId2 = android.os.UserHandle.getUserId(sourceRecord.app.mUid);
                    }
                    try {
                        try {
                            try {
                                try {
                                    try {
                                        try {
                                            try {
                                                return getActivityStartController().obtainStarter(intent, "startActivityAsCaller").setCallingUid(targetUid).setCallingPackage(targetPackage).setCallingFeatureId(targetFeatureId).setResolvedType(resolvedType).setResultTo(resultTo).setResultWho(resultWho).setRequestCode(requestCode).setStartFlags(startFlags).setActivityOptions(createSafeActivityOptionsWithBalAllowed(bOptions)).setUserId(userId2).setIgnoreTargetSecurity(ignoreTargetSecurity).setFilterCallingUid(isResolver ? 0 : targetUid).setBackgroundStartPrivileges(android.app.BackgroundStartPrivileges.ALLOW_BAL).execute();
                                            } catch (java.lang.SecurityException e) {
                                                throw e;
                                            }
                                        } catch (java.lang.SecurityException e2) {
                                            e = e2;
                                            throw e;
                                        }
                                    } catch (java.lang.SecurityException e3) {
                                        e = e3;
                                        throw e;
                                    }
                                } catch (java.lang.SecurityException e4) {
                                    e = e4;
                                    throw e;
                                }
                            } catch (java.lang.SecurityException e5) {
                                e = e5;
                                throw e;
                            }
                        } catch (java.lang.SecurityException e6) {
                            e = e6;
                            throw e;
                        }
                    } catch (java.lang.SecurityException e7) {
                        e = e7;
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    int handleIncomingUser(int callingPid, int callingUid, int userId, java.lang.String name) {
        return this.mAmInternal.handleIncomingUser(callingPid, callingUid, userId, false, 0, name, (java.lang.String) null);
    }

    public int startVoiceActivity(java.lang.String callingPackage, java.lang.String callingFeatureId, int callingPid, int callingUid, android.content.Intent intent, java.lang.String resolvedType, android.service.voice.IVoiceInteractionSession session, com.android.internal.app.IVoiceInteractor interactor, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle bOptions, int userId) {
        assertPackageMatchesCallingUid(callingPackage);
        this.mAmInternal.enforceCallingPermission("android.permission.BIND_VOICE_INTERACTION", "startVoiceActivity()");
        if (session == null || interactor == null) {
            throw new java.lang.NullPointerException("null session or interactor");
        }
        return getActivityStartController().obtainStarter(intent, "startVoiceActivity").setCallingUid(callingUid).setCallingPackage(callingPackage).setCallingFeatureId(callingFeatureId).setResolvedType(resolvedType).setVoiceSession(session).setVoiceInteractor(interactor).setStartFlags(startFlags).setProfilerInfo(profilerInfo).setActivityOptions(createSafeActivityOptionsWithBalAllowed(bOptions)).setUserId(handleIncomingUser(callingPid, callingUid, userId, "startVoiceActivity")).setBackgroundStartPrivileges(android.app.BackgroundStartPrivileges.ALLOW_BAL).execute();
    }

    public java.lang.String getVoiceInteractorPackageName(android.os.IBinder callingVoiceInteractor) {
        return ((android.service.voice.VoiceInteractionManagerInternal) com.android.server.LocalServices.getService(android.service.voice.VoiceInteractionManagerInternal.class)).getVoiceInteractorPackageName(callingVoiceInteractor);
    }

    public int startAssistantActivity(java.lang.String callingPackage, java.lang.String callingFeatureId, int callingPid, int callingUid, android.content.Intent intent, java.lang.String resolvedType, android.os.Bundle bOptions, int userId) {
        assertPackageMatchesCallingUid(callingPackage);
        this.mAmInternal.enforceCallingPermission("android.permission.BIND_VOICE_INTERACTION", "startAssistantActivity()");
        int userId2 = handleIncomingUser(callingPid, callingUid, userId, "startAssistantActivity");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            return getActivityStartController().obtainStarter(intent, "startAssistantActivity").setCallingUid(callingUid).setCallingPackage(callingPackage).setCallingFeatureId(callingFeatureId).setResolvedType(resolvedType).setActivityOptions(createSafeActivityOptionsWithBalAllowed(bOptions)).setUserId(userId2).setBackgroundStartPrivileges(android.app.BackgroundStartPrivileges.ALLOW_BAL).execute();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void startRecentsActivity(android.content.Intent intent, long eventTime, android.view.IRecentsAnimationRunner recentsAnimationRunner) throws java.lang.Throwable {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock;
        android.content.ComponentName recentsComponent;
        java.lang.String recentsFeatureId;
        int recentsUid;
        com.android.server.wm.WindowProcessController caller;
        enforceTaskPermission("startRecentsActivity()");
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            try {
                try {
                    synchronized (windowManagerGlobalLock2) {
                        try {
                            recentsComponent = this.mRecentTasks.getRecentsComponent();
                            recentsFeatureId = this.mRecentTasks.getRecentsComponentFeatureId();
                            recentsUid = this.mRecentTasks.getRecentsComponentUid();
                            caller = getProcessController(callingPid, callingUid);
                            windowManagerGlobalLock = windowManagerGlobalLock2;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            windowManagerGlobalLock = windowManagerGlobalLock2;
                        }
                        try {
                            com.android.server.wm.RecentsAnimation anim = new com.android.server.wm.RecentsAnimation(this, this.mTaskSupervisor, getActivityStartController(), this.mWindowManager, intent, recentsComponent, recentsFeatureId, recentsUid, caller);
                            if (recentsAnimationRunner == null) {
                                anim.preloadRecentsActivity();
                            } else {
                                anim.startRecentsActivity(recentsAnimationRunner, eventTime);
                            }
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            android.os.Binder.restoreCallingIdentity(origId);
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    android.os.Binder.restoreCallingIdentity(origId);
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        } catch (java.lang.Throwable th5) {
            th = th5;
        }
    }

    public final int startActivityFromRecents(int taskId, android.os.Bundle bOptions) {
        this.mAmInternal.enforceCallingPermission("android.permission.START_TASKS_FROM_RECENTS", "startActivityFromRecents()");
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.wm.SafeActivityOptions safeOptions = com.android.server.wm.SafeActivityOptions.fromBundle(bOptions);
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            return this.mTaskSupervisor.startActivityFromRecents(callingPid, callingUid, taskId, safeOptions);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public int startActivityFromGameSession(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String callingFeatureId, int callingPid, int callingUid, android.content.Intent intent, int taskId, int userId) {
        if (checkCallingPermission("android.permission.MANAGE_GAME_ACTIVITY") != 0) {
            java.lang.String msg = "Permission Denial: startActivityFromGameSession() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.MANAGE_GAME_ACTIVITY";
            android.util.Slog.w(TAG, msg);
            throw new java.lang.SecurityException(msg);
        }
        assertPackageMatchesCallingUid(callingPackage);
        android.app.ActivityOptions activityOptions = android.app.ActivityOptions.makeBasic();
        activityOptions.setLaunchTaskId(taskId);
        int userId2 = handleIncomingUser(callingPid, callingUid, userId, "startActivityFromGameSession");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            return getActivityStartController().obtainStarter(intent, "startActivityFromGameSession").setCaller(caller).setCallingUid(callingUid).setCallingPid(callingPid).setCallingPackage(intent.getPackage()).setCallingFeatureId(callingFeatureId).setUserId(userId2).setActivityOptions(activityOptions.toBundle()).setRealCallingUid(android.os.Binder.getCallingUid()).execute();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public android.window.BackNavigationInfo startBackNavigation(android.os.RemoteCallback navigationObserver, android.window.BackAnimationAdapter adapter) {
        this.mAmInternal.enforceCallingPermission("android.permission.START_TASKS_FROM_RECENTS", "startBackNavigation()");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            return this.mBackNavigationController.startBackNavigation(navigationObserver, adapter);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public final boolean isActivityStartAllowedOnDisplay(int displayId, android.content.Intent intent, java.lang.String resolvedType, int userId) {
        boolean zCanPlaceEntityOnDisplay;
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.ActivityInfo aInfo = resolveActivityInfoForIntent(intent, resolvedType, userId, callingUid, callingPid);
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    zCanPlaceEntityOnDisplay = this.mTaskSupervisor.canPlaceEntityOnDisplay(displayId, callingPid, callingUid, aInfo);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return zCanPlaceEntityOnDisplay;
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    android.content.pm.ActivityInfo resolveActivityInfoForIntent(android.content.Intent intent, java.lang.String resolvedType, int userId, int callingUid, int callingPid) {
        android.content.pm.ActivityInfo aInfo = this.mTaskSupervisor.resolveActivity(intent, resolvedType, 0, null, userId, com.android.server.wm.ActivityStarter.computeResolveFilterUid(callingUid, callingUid, -10000), callingPid);
        return this.mAmInternal.getActivityInfoForUser(aInfo, userId);
    }

    public android.app.IActivityClientController getActivityClientController() {
        return this.mActivityClientController;
    }

    void applyUpdateLockStateLocked(final com.android.server.wm.ActivityRecord r) {
        final boolean nextState = r != null && r.immersive;
        this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$applyUpdateLockStateLocked$0(nextState, r);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyUpdateLockStateLocked$0(boolean nextState, com.android.server.wm.ActivityRecord r) {
        if (this.mUpdateLock.isHeld() != nextState) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IMMERSIVE_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(nextState);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(r);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IMMERSIVE, 6075150529915862250L, 0, null, protoLogParam0, protoLogParam1);
            }
            if (nextState) {
                this.mUpdateLock.acquire();
            } else {
                this.mUpdateLock.release();
            }
        }
    }

    public boolean isTopActivityImmersive() {
        enforceNotIsolatedCaller("isTopActivityImmersive");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task topFocusedRootTask = getTopDisplayFocusedRootTask();
                boolean z = false;
                if (topFocusedRootTask == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                com.android.server.wm.ActivityRecord r = topFocusedRootTask.topRunningActivity();
                if (r != null && r.immersive) {
                    z = true;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return z;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public int getFrontActivityScreenCompatMode() {
        enforceNotIsolatedCaller("getFrontActivityScreenCompatMode");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task rootTask = getTopDisplayFocusedRootTask();
                com.android.server.wm.ActivityRecord r = rootTask != null ? rootTask.topRunningActivity() : null;
                if (r == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return -3;
                }
                int iComputeCompatModeLocked = this.mCompatModePackages.computeCompatModeLocked(r.info.applicationInfo);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return iComputeCompatModeLocked;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setFrontActivityScreenCompatMode(int mode) {
        this.mAmInternal.enforceCallingPermission("android.permission.SET_SCREEN_COMPATIBILITY", "setFrontActivityScreenCompatMode");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task rootTask = getTopDisplayFocusedRootTask();
                com.android.server.wm.ActivityRecord r = rootTask != null ? rootTask.topRunningActivity() : null;
                if (r == null) {
                    android.util.Slog.w(TAG, "setFrontActivityScreenCompatMode failed: no top activity");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } else {
                    android.content.pm.ApplicationInfo ai = r.info.applicationInfo;
                    this.mCompatModePackages.setPackageScreenCompatModeLocked(ai, mode);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public android.app.ActivityTaskManager.RootTaskInfo getFocusedRootTaskInfo() throws android.os.RemoteException {
        enforceTaskPermission("getFocusedRootTaskInfo()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.Task focusedRootTask = getTopDisplayFocusedRootTask();
                    if (focusedRootTask != null) {
                        android.app.ActivityTaskManager.RootTaskInfo rootTaskInfo = this.mRootWindowContainer.getRootTaskInfo(focusedRootTask.mTaskId);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return rootTaskInfo;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    android.os.Binder.restoreCallingIdentity(ident);
                    return null;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void setFocusedRootTask(int taskId) {
        enforceTaskPermission("setFocusedRootTask()");
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[0]) {
            long protoLogParam0 = taskId;
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, -4356952232698761083L, 1, null, java.lang.Long.valueOf(protoLogParam0));
        }
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.Task task = this.mRootWindowContainer.getRootTask(taskId);
                    if (task == null) {
                        android.util.Slog.w(TAG, "setFocusedRootTask: No task with id=" + taskId);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.ActivityRecord r = task.topRunningActivity();
                    if (r != null && r.moveFocusableActivityToTop("setFocusedRootTask")) {
                        this.mRootWindowContainer.resumeFocusedTasksTopActivities();
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public void setFocusedTask(int taskId) {
        enforceTaskPermission("setFocusedTask()");
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    setFocusedTask(taskId, null);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public void focusTopTask(int displayId) {
        enforceTaskPermission("focusTopTask()");
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = this.mRootWindowContainer.getDisplayContent(displayId);
                    if (dc == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.Task task = dc.getTask(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda4
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.wm.ActivityTaskManagerService.lambda$focusTopTask$1((com.android.server.wm.Task) obj);
                        }
                    }, true);
                    if (task == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    } else {
                        setFocusedTask(task.mTaskId, null);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    static /* synthetic */ boolean lambda$focusTopTask$1(com.android.server.wm.Task t) {
        return t.isLeafTask() && t.isTopActivityFocusable() && !t.getWrapper().getExtImpl().isInMiniMode(t);
    }

    void setFocusedTask(int taskId, com.android.server.wm.ActivityRecord touchedActivity) {
        com.android.server.wm.ActivityRecord r;
        com.android.server.wm.Transition transition;
        com.android.server.wm.TaskFragment parent;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[0]) {
            long protoLogParam0 = taskId;
            java.lang.String protoLogParam1 = java.lang.String.valueOf(touchedActivity);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, 301842347780487555L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
        }
        com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId, 0);
        if (task == null || (r = task.topRunningActivityLocked()) == null || getWrapper().getExtImpl().shouldSkipSetFocusedTaskForFlexibleWindow(task)) {
            return;
        }
        if ((touchedActivity == null || r == touchedActivity) && r.isState(com.android.server.wm.ActivityRecord.State.RESUMED) && r == this.mRootWindowContainer.getTopResumedActivity()) {
            setLastResumedActivityUncheckLocked(r, "setFocusedTask-alreadyTop");
            return;
        }
        if (getTransitionController().isCollecting() || !getTransitionController().isShellTransitionsEnabled() || getWrapper().getExtImpl().withNoneTransition(null, task, null, 3, "setFocusedTask")) {
            transition = null;
        } else {
            transition = getTransitionController().createTransition(3);
        }
        if (transition != null) {
            transition.setReady(task, true);
        }
        boolean movedToTop = r.moveFocusableActivityToTop("setFocusedTask");
        if (movedToTop) {
            if (transition != null) {
                getTransitionController().requestStartTransition(transition, null, null, null);
            }
            this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        } else if (touchedActivity != null && touchedActivity.isFocusable() && (parent = touchedActivity.getTaskFragment()) != null && parent.isEmbedded()) {
            com.android.server.wm.DisplayContent displayContent = touchedActivity.getDisplayContent();
            displayContent.setFocusedApp(touchedActivity);
            this.mWindowManager.updateFocusedWindowLocked(0, true);
        }
        if (transition != null && !movedToTop) {
            transition.abort();
        }
    }

    public boolean removeTask(int taskId) {
        this.mAmInternal.enforceCallingPermission("android.permission.REMOVE_TASKS", "removeTask()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId, 1);
                    if (task == null) {
                        android.util.Slog.w(TAG, "removeTask: No task remove with id=" + taskId);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    if (task.isLeafTask()) {
                        this.mTaskSupervisor.removeTask(task, true, true, "remove-task");
                    } else {
                        this.mTaskSupervisor.removeRootTask(task);
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return true;
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void removeAllVisibleRecentTasks() {
        this.mAmInternal.enforceCallingPermission("android.permission.REMOVE_TASKS", "removeAllVisibleRecentTasks()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    getRecentTasks().removeAllVisibleTasks(this.mAmInternal.getCurrentUserId());
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public android.graphics.Rect getTaskBounds(int taskId) {
        enforceTaskPermission("getTaskBounds()");
        long ident = android.os.Binder.clearCallingIdentity();
        android.graphics.Rect rect = new android.graphics.Rect();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId, 1);
                    if (task == null) {
                        android.util.Slog.w(TAG, "getTaskBounds: taskId=" + taskId + " not found");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return rect;
                    }
                    if (task.getParent() != null) {
                        rect.set(task.getBounds());
                    } else if (task.mLastNonFullscreenBounds != null) {
                        rect.set(task.mLastNonFullscreenBounds);
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return rect;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void setLocusId(android.content.LocusId locusId, android.os.IBinder appToken) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(appToken);
                if (r != null) {
                    r.setLocusId(locusId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    com.android.server.uri.NeededUriGrants collectGrants(android.content.Intent intent, com.android.server.wm.ActivityRecord target) {
        if (target != null) {
            return this.mUgmInternal.checkGrantUriPermissionFromIntent(intent, android.os.Binder.getCallingUid(), target.packageName, target.mUserId);
        }
        return null;
    }

    public void unhandledBack() {
        this.mAmInternal.enforceCallingPermission("android.permission.FORCE_BACK", "unhandledBack()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long origId = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.wm.Task topFocusedRootTask = getTopDisplayFocusedRootTask();
                    if (topFocusedRootTask != null) {
                        topFocusedRootTask.unhandledBackLocked();
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void moveTaskToFront(android.app.IApplicationThread appThread, java.lang.String callingPackage, int taskId, int flags, android.os.Bundle bOptions) {
        this.mAmInternal.enforceCallingPermission("android.permission.REORDER_TASKS", "moveTaskToFront()");
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
            long protoLogParam0 = taskId;
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 7095858131234795548L, 1, null, java.lang.Long.valueOf(protoLogParam0));
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                moveTaskToFrontLocked(appThread, callingPackage, taskId, flags, com.android.server.wm.SafeActivityOptions.fromBundle(bOptions));
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void moveTaskToFrontLocked(android.app.IApplicationThread appThread, java.lang.String callingPackage, int taskId, int flags, com.android.server.wm.SafeActivityOptions options) throws java.lang.Throwable {
        com.android.server.wm.WindowProcessController callerApp;
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        assertPackageMatchesCallingUid(callingPackage);
        android.util.Slog.i(TAG, "moveTaskToFrontLocked callingPid:" + android.os.Binder.getCallingPid() + ", callingUid:" + android.os.Binder.getCallingUid());
        long origId = android.os.Binder.clearCallingIdentity();
        if (appThread != null) {
            com.android.server.wm.WindowProcessController callerApp2 = getProcessController(appThread);
            callerApp = callerApp2;
        } else {
            callerApp = null;
        }
        com.android.server.wm.BackgroundActivityStartController balController = this.mTaskSupervisor.getBackgroundActivityLaunchController();
        com.android.server.wm.BackgroundActivityStartController.BalVerdict balVerdict = balController.checkBackgroundActivityStart(callingUid, callingPid, callingPackage, -1, -1, callerApp, null, android.app.BackgroundStartPrivileges.NONE, null, null, null);
        if (balVerdict.blocks() && !isBackgroundActivityStartsEnabled()) {
            android.util.Slog.w(TAG, "moveTaskToFront blocked: " + balVerdict);
            return;
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ACTIVITY_STARTS) {
            android.util.Slog.d(TAG, "moveTaskToFront allowed: " + balVerdict);
        }
        try {
            com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId);
            try {
                if (task == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                        long protoLogParam0 = taskId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -4458288191054594222L, 1, null, java.lang.Long.valueOf(protoLogParam0));
                    }
                    com.android.server.wm.SafeActivityOptions.abort(options);
                    android.os.Binder.restoreCallingIdentity(origId);
                    return;
                }
                if (getLockTaskController().isLockTaskModeViolation(task)) {
                    android.util.Slog.e(TAG, "moveTaskToFront: Attempt to violate Lock Task Mode");
                    com.android.server.wm.SafeActivityOptions.abort(options);
                    android.os.Binder.restoreCallingIdentity(origId);
                    return;
                }
                android.app.ActivityOptions realOptions = options != null ? options.getOptions(this.mTaskSupervisor) : null;
                if (appThread != null) {
                    try {
                        this.mTaskSupervisor.getWrapper().getExtImpl().updateFlexibleWindowTask(task, realOptions, callingPid);
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                }
                if (mActivityTaskManagerExt.shouldAbortMoveTaskToFront(task, realOptions)) {
                    android.util.Slog.d(TAG, "moveTaskToFront: abort move encryption task to front, taskId=" + taskId);
                    android.os.Binder.restoreCallingIdentity(origId);
                    return;
                } else {
                    this.mTaskSupervisor.findTaskToMoveToFront(task, flags, realOptions, "moveTaskToFront", false);
                    android.os.Binder.restoreCallingIdentity(origId);
                    return;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        } catch (java.lang.Throwable th3) {
            th = th3;
        }
        android.os.Binder.restoreCallingIdentity(origId);
        throw th;
    }

    private boolean isSameApp(int callingUid, java.lang.String packageName) {
        if (callingUid != 0 && callingUid != 1000) {
            return this.mPmInternal.isSameApp(packageName, callingUid, android.os.UserHandle.getUserId(callingUid));
        }
        return true;
    }

    void assertPackageMatchesCallingUid(java.lang.String packageName) {
        int callingUid = android.os.Binder.getCallingUid();
        if (isSameApp(callingUid, packageName)) {
            return;
        }
        java.lang.String msg = "Permission Denial: package=" + packageName + " does not belong to uid=" + callingUid;
        android.util.Slog.w(TAG, msg);
        throw new java.lang.SecurityException(msg);
    }

    int getBalAppSwitchesState() {
        return this.mAppSwitchesState;
    }

    public void registerAnrController(android.app.AnrController controller) {
        synchronized (this.mAnrController) {
            this.mAnrController.add(controller);
        }
    }

    public void unregisterAnrController(android.app.AnrController controller) {
        synchronized (this.mAnrController) {
            this.mAnrController.remove(controller);
        }
    }

    public android.app.AnrController getAnrController(android.content.pm.ApplicationInfo info) {
        java.util.ArrayList<android.app.AnrController> controllers;
        if (info == null || info.packageName == null) {
            return null;
        }
        synchronized (this.mAnrController) {
            controllers = new java.util.ArrayList<>(this.mAnrController);
        }
        java.lang.String packageName = info.packageName;
        int uid = info.uid;
        long maxDelayMs = 0;
        android.app.AnrController controllerWithMaxDelay = null;
        for (android.app.AnrController controller : controllers) {
            long delayMs = controller.getAnrDelayMillis(packageName, uid);
            if (delayMs > 0 && delayMs > maxDelayMs) {
                controllerWithMaxDelay = controller;
                maxDelayMs = delayMs;
            }
        }
        return controllerWithMaxDelay;
    }

    public void setActivityController(android.app.IActivityController controller, boolean imAMonkey) {
        this.mAmInternal.enforceCallingPermission("android.permission.SET_ACTIVITY_WATCHER", "setActivityController()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mController = controller;
                this.mControllerIsAMonkey = imAMonkey;
                com.android.server.Watchdog.getInstance().setActivityController(controller);
                android.util.Slog.w(TAG, "controller : " + controller + ", callingPid : " + android.os.Binder.getCallingPid());
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public boolean isControllerAMonkey() {
        boolean z;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                z = this.mController != null && this.mControllerIsAMonkey;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return z;
    }

    public java.util.List<android.app.ActivityManager.RunningTaskInfo> getTasks(int maxNum) {
        return getTasks(maxNum, false, false, -1);
    }

    public java.util.List<android.app.ActivityManager.RunningTaskInfo> getTasks(int maxNum, boolean filterOnlyVisibleRecents, boolean keepIntentExtra) {
        return getTasks(maxNum, filterOnlyVisibleRecents, keepIntentExtra, -1);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:34:0x0094
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public java.util.List<android.app.ActivityManager.RunningTaskInfo> getTasks(int r18, boolean r19, boolean r20, int r21) {
        /*
            r17 = this;
            r1 = r17
            int r9 = android.os.Binder.getCallingUid()
            int r10 = android.os.Binder.getCallingPid()
            r0 = r19
            r2 = 0
            if (r20 == 0) goto L12
            r3 = 8
            goto L13
        L12:
            r3 = r2
        L13:
            r0 = r0 | r3
            boolean r11 = r1.isCrossUserAllowed(r10, r9)
            if (r11 == 0) goto L1c
            r3 = 4
            goto L1d
        L1c:
            r3 = r2
        L1d:
            r3 = r3 | r0
            com.android.server.pm.UserManagerService r0 = r17.getUserManager()
            int r4 = android.os.UserHandle.getUserId(r9)
            r5 = 1
            int[] r12 = r0.getProfileIds(r4, r5)
            android.util.ArraySet r0 = new android.util.ArraySet
            r0.<init>()
            r13 = r0
            r0 = 0
        L32:
            int r4 = r12.length
            if (r0 >= r4) goto L41
            r4 = r12[r0]
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r13.add(r4)
            int r0 = r0 + 1
            goto L32
        L41:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r14 = r0
            com.android.server.wm.WindowManagerGlobalLock r15 = r1.mGlobalLock
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection()
            monitor-enter(r15)
            boolean r0 = com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ALL     // Catch: java.lang.Throwable -> L94
            if (r0 == 0) goto L70
            java.lang.String r0 = "ActivityTaskManager"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6c
            r4.<init>()     // Catch: java.lang.Throwable -> L6c
            java.lang.String r5 = "getTasks: max="
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Throwable -> L6c
            r8 = r18
            java.lang.StringBuilder r4 = r4.append(r8)     // Catch: java.lang.Throwable -> L94
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L94
            android.util.Slog.v(r0, r4)     // Catch: java.lang.Throwable -> L94
            goto L72
        L6c:
            r0 = move-exception
            r8 = r18
            goto L95
        L70:
            r8 = r18
        L72:
            java.lang.String r0 = "getTasks"
            boolean r0 = r1.isGetTasksAllowed(r0, r10, r9)     // Catch: java.lang.Throwable -> L94
            if (r0 == 0) goto L7b
            r2 = 2
        L7b:
            r16 = r3 | r2
            com.android.server.wm.RootWindowContainer r2 = r1.mRootWindowContainer     // Catch: java.lang.Throwable -> L90
            r3 = r18
            r4 = r14
            r5 = r16
            r6 = r9
            r7 = r13
            r8 = r21
            r2.getRunningTasks(r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L90
            monitor-exit(r15)     // Catch: java.lang.Throwable -> L90
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            return r14
        L90:
            r0 = move-exception
            r3 = r16
            goto L95
        L94:
            r0 = move-exception
        L95:
            monitor-exit(r15)     // Catch: java.lang.Throwable -> L94
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityTaskManagerService.getTasks(int, boolean, boolean, int):java.util.List");
    }

    public void moveTaskToRootTask(int taskId, int rootTaskId, boolean toTop) {
        enforceTaskPermission("moveTaskToRootTask()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId);
                    if (task == null) {
                        android.util.Slog.w(TAG, "moveTaskToRootTask: No task for id=" + taskId);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                        long protoLogParam0 = taskId;
                        long protoLogParam1 = rootTaskId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -1136891560663761442L, 53, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1), java.lang.Boolean.valueOf(toTop));
                    }
                    com.android.server.wm.Task rootTask = this.mRootWindowContainer.getRootTask(rootTaskId);
                    if (rootTask == null) {
                        throw new java.lang.IllegalStateException("moveTaskToRootTask: No rootTask for rootTaskId=" + rootTaskId);
                    }
                    if (!rootTask.isActivityTypeStandardOrUndefined()) {
                        throw new java.lang.IllegalArgumentException("moveTaskToRootTask: Attempt to move task " + taskId + " to rootTask " + rootTaskId);
                    }
                    task.reparent(rootTask, toTop, 1, true, false, "moveTaskToRootTask");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void removeRootTasksInWindowingModes(int[] windowingModes) {
        enforceTaskPermission("removeRootTasksInWindowingModes()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    this.mRootWindowContainer.removeRootTasksInWindowingModes(windowingModes);
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void removeRootTasksWithActivityTypes(int[] activityTypes) {
        enforceTaskPermission("removeRootTasksWithActivityTypes()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    this.mRootWindowContainer.removeRootTasksWithActivityTypes(activityTypes);
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public android.content.pm.ParceledListSlice<android.app.ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags, int userId) {
        android.content.pm.ParceledListSlice<android.app.ActivityManager.RecentTaskInfo> recentTasks;
        int callingUid = android.os.Binder.getCallingUid();
        int userId2 = handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, "getRecentTasks");
        boolean allowed = isGetTasksAllowed("getRecentTasks", android.os.Binder.getCallingPid(), callingUid);
        if (!this.mAmInternal.isUserRunning(userId2, 4)) {
            android.util.Slog.i(TAG, "User " + userId2 + " is locked. Cannot load recents");
            return android.content.pm.ParceledListSlice.emptyList();
        }
        this.mRecentTasks.loadRecentTasksIfNeeded(userId2);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                recentTasks = this.mRecentTasks.getRecentTasks(maxNum, flags, allowed, userId2, callingUid);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return recentTasks;
    }

    public java.util.List<android.app.ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos() {
        java.util.ArrayList<android.app.ActivityTaskManager.RootTaskInfo> allRootTaskInfos;
        enforceTaskPermission("getAllRootTaskInfos()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    allRootTaskInfos = this.mRootWindowContainer.getAllRootTaskInfos(-1);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return allRootTaskInfos;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public android.app.ActivityTaskManager.RootTaskInfo getRootTaskInfo(int windowingMode, int activityType) {
        android.app.ActivityTaskManager.RootTaskInfo rootTaskInfo;
        enforceTaskPermission("getRootTaskInfo()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    rootTaskInfo = this.mRootWindowContainer.getRootTaskInfo(windowingMode, activityType);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return rootTaskInfo;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public java.util.List<android.app.ActivityTaskManager.RootTaskInfo> getAllRootTaskInfosOnDisplay(int displayId) {
        java.util.ArrayList<android.app.ActivityTaskManager.RootTaskInfo> allRootTaskInfos;
        enforceTaskPermission("getAllRootTaskInfosOnDisplay()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    allRootTaskInfos = this.mRootWindowContainer.getAllRootTaskInfos(displayId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return allRootTaskInfos;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public android.app.ActivityTaskManager.RootTaskInfo getRootTaskInfoOnDisplay(int windowingMode, int activityType, int displayId) {
        android.app.ActivityTaskManager.RootTaskInfo rootTaskInfo;
        enforceTaskPermission("getRootTaskInfoOnDisplay()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    rootTaskInfo = this.mRootWindowContainer.getRootTaskInfo(windowingMode, activityType, displayId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return rootTaskInfo;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void cancelRecentsAnimation(boolean restoreHomeRootTaskPosition) {
        int i;
        enforceTaskPermission("cancelRecentsAnimation()");
        long callingUid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService windowManagerService = this.mWindowManager;
                    if (restoreHomeRootTaskPosition) {
                        i = 2;
                    } else {
                        i = 0;
                    }
                    windowManagerService.cancelRecentsAnimation(i, "cancelRecentsAnimation/uid=" + callingUid);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void startSystemLockTaskMode(int taskId) {
        enforceTaskPermission("startSystemLockTaskMode");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId, 0);
                    if (task != null) {
                        task.getRootTask().moveToFront("startSystemLockTaskMode");
                        startLockTaskMode(task, true);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void stopSystemLockTaskMode() throws android.os.RemoteException {
        enforceTaskPermission("stopSystemLockTaskMode");
        stopLockTaskModeInternal(null, true);
    }

    void startLockTaskMode(com.android.server.wm.Task task, boolean isSystemCaller) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[3]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(task);
            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, 6954122272402912822L, 0, null, protoLogParam0);
        }
        if (task == null || task.mLockTaskAuth == 0) {
            return;
        }
        com.android.server.wm.Task rootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask();
        if (rootTask == null || task != rootTask.getTopMostTask()) {
            throw new java.lang.IllegalArgumentException("Invalid task, not in foreground");
        }
        int callingUid = android.os.Binder.getCallingUid();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            getLockTaskController().startLockTaskMode(task, isSystemCaller, callingUid);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    void stopLockTaskModeInternal(android.os.IBinder token, boolean isSystemCaller) {
        int callingUid = android.os.Binder.getCallingUid();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                com.android.server.wm.Task task = null;
                if (token != null) {
                    try {
                        com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                        if (r != null) {
                            task = r.getTask();
                        } else {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                getLockTaskController().stopLockTaskMode(task, isSystemCaller, callingUid);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                android.telecom.TelecomManager tm = (android.telecom.TelecomManager) this.mContext.getSystemService("telecom");
                if (tm != null) {
                    tm.showInCallScreen(false);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void updateLockTaskPackages(int userId, java.lang.String[] packages) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 0 && callingUid != 1000) {
            this.mAmInternal.enforceCallingPermission("android.permission.UPDATE_LOCK_TASK_PACKAGES", "updateLockTaskPackages()");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[3]) {
                        long protoLogParam0 = userId;
                        java.lang.String protoLogParam1 = java.lang.String.valueOf(java.util.Arrays.toString(packages));
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, -829638795650515884L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
                    }
                    getLockTaskController().updateLockTaskPackages(userId, packages);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public boolean isInLockTaskMode() {
        return getLockTaskModeState() != 0;
    }

    public int getLockTaskModeState() {
        return getLockTaskController().getLockTaskModeState();
    }

    public java.util.List<android.os.IBinder> getAppTasks(java.lang.String callingPackage) {
        assertPackageMatchesCallingUid(callingPackage);
        return getAppTasks(callingPackage, android.os.Binder.getCallingUid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.os.IBinder> getAppTasks(java.lang.String pkgName, int uid) {
        java.util.ArrayList<android.os.IBinder> appTasksList;
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    appTasksList = this.mRecentTasks.getAppTasksList(uid, pkgName);
                } finally {
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return appTasksList;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void finishVoiceTask(android.service.voice.IVoiceInteractionSession session) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long origId = android.os.Binder.clearCallingIdentity();
                try {
                    this.mRootWindowContainer.finishVoiceTask(session);
                } finally {
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void reportAssistContextExtras(android.os.IBinder assistToken, android.os.Bundle extras, android.app.assist.AssistStructure structure, android.app.assist.AssistContent content, android.net.Uri referrer) {
        com.android.server.wm.ActivityTaskManagerService.PendingAssistExtras pae = (com.android.server.wm.ActivityTaskManagerService.PendingAssistExtras) assistToken;
        synchronized (pae) {
            pae.result = extras;
            pae.structure = structure;
            pae.content = content;
            if (referrer != null) {
                pae.extras.putParcelable("android.intent.extra.REFERRER", referrer);
            }
            if (pae.activity.isAttached()) {
                if (structure != null) {
                    com.android.server.wm.Task task = pae.activity.getTask();
                    if (task == null) {
                        return;
                    }
                    structure.setTaskId(task.mTaskId);
                    structure.setActivityComponent(pae.activity.mActivityComponent);
                    structure.setHomeActivity(pae.isHome);
                }
                pae.haveResult = true;
                pae.notifyAll();
                if (pae.intent == null && pae.receiver == null) {
                    return;
                }
                android.os.Bundle sendBundle = null;
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        buildAssistBundleLocked(pae, extras);
                        boolean exists = this.mPendingAssistExtras.remove(pae);
                        this.mUiHandler.removeCallbacks(pae);
                        if (!exists) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        android.app.IAssistDataReceiver sendReceiver = pae.receiver;
                        if (sendReceiver != null && pae.activity.getTask() != null) {
                            sendBundle = new android.os.Bundle();
                            com.android.server.wm.Task task2 = pae.activity.getTask();
                            if (task2 == null) {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            sendBundle.putInt(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_TASK_ID, task2.mTaskId);
                            sendBundle.putBinder(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_ACTIVITY_ID, pae.activity.assistToken);
                            sendBundle.putBundle("data", pae.extras);
                            sendBundle.putParcelable(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_STRUCTURE, pae.structure);
                            sendBundle.putParcelable(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT, pae.content);
                            sendBundle.putBundle(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_RECEIVER_EXTRAS, pae.receiverExtras);
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        if (sendReceiver != null) {
                            try {
                                sendReceiver.onHandleAssistData(sendBundle);
                                return;
                            } catch (android.os.RemoteException e) {
                                return;
                            }
                        }
                        long ident = android.os.Binder.clearCallingIdentity();
                        try {
                            pae.intent.replaceExtras(pae.extras);
                            pae.intent.setFlags(872415232);
                            this.mInternal.closeSystemDialogs(com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_ASSIST);
                            try {
                                this.mContext.startActivityAsUser(pae.intent, new android.os.UserHandle(pae.userHandle));
                            } catch (android.content.ActivityNotFoundException e2) {
                                android.util.Slog.w(TAG, "No activity to handle assist action.", e2);
                            }
                        } finally {
                            android.os.Binder.restoreCallingIdentity(ident);
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int addAppTask(android.os.IBinder activityToken, android.content.Intent intent, android.app.ActivityManager.TaskDescription description, android.graphics.Bitmap thumbnail) throws android.os.RemoteException {
        int callingUid = android.os.Binder.getCallingUid();
        long callingIdent = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(activityToken);
                    if (r == null) {
                        throw new java.lang.IllegalArgumentException("Activity does not exist; token=" + activityToken);
                    }
                    android.content.ComponentName comp = intent.getComponent();
                    if (comp == null) {
                        throw new java.lang.IllegalArgumentException("Intent " + intent + " must specify explicit component");
                    }
                    if (thumbnail.getWidth() != this.mThumbnailWidth || thumbnail.getHeight() != this.mThumbnailHeight) {
                        throw new java.lang.IllegalArgumentException("Bad thumbnail size: got " + thumbnail.getWidth() + "x" + thumbnail.getHeight() + ", require " + this.mThumbnailWidth + "x" + this.mThumbnailHeight);
                    }
                    if (intent.getSelector() != null) {
                        intent.setSelector(null);
                    }
                    if (intent.getSourceBounds() != null) {
                        intent.setSourceBounds(null);
                    }
                    if ((intent.getFlags() & 524288) != 0 && (intent.getFlags() & 8192) == 0) {
                        intent.addFlags(8192);
                    }
                    android.content.pm.ActivityInfo ainfo = android.app.AppGlobals.getPackageManager().getActivityInfo(comp, 1024L, android.os.UserHandle.getUserId(callingUid));
                    if (ainfo != null && ainfo.applicationInfo.uid == callingUid) {
                        com.android.server.wm.Task rootTask = r.getRootTask();
                        com.android.server.wm.Task task = new com.android.server.wm.Task.Builder(this).setWindowingMode(rootTask.getWindowingMode()).setActivityType(rootTask.getActivityType()).setActivityInfo(ainfo).setIntent(intent).setTaskId(rootTask.getDisplayArea().getNextRootTaskId()).build();
                        if (!this.mRecentTasks.addToBottom(task)) {
                            rootTask.removeChild(task, "addAppTask");
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return -1;
                        }
                        task.getTaskDescription().copyFrom(description);
                        int i = task.mTaskId;
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return i;
                    }
                    android.util.Slog.e(TAG, "Can't add task for another application: target uid=" + (ainfo == null ? -1 : ainfo.applicationInfo.uid) + ", calling uid=" + callingUid);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return -1;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdent);
        }
    }

    public android.graphics.Point getAppTaskThumbnailSize() {
        android.graphics.Point point;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                point = new android.graphics.Point(this.mThumbnailWidth, this.mThumbnailHeight);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return point;
    }

    public void setTaskResizeable(int taskId, int resizeableMode) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId, 1);
                if (task == null) {
                    android.util.Slog.w(TAG, "setTaskResizeable: taskId=" + taskId + " not found");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } else {
                    task.setResizeMode(resizeableMode);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void resizeTask(int taskId, final android.graphics.Rect bounds, final int resizeMode) {
        enforceTaskPermission("resizeTask()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            try {
                try {
                    synchronized (windowManagerGlobalLock) {
                        try {
                            final com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId, 0);
                            if (task == null) {
                                android.util.Slog.w(TAG, "resizeTask: taskId=" + taskId + " not found");
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                android.os.Binder.restoreCallingIdentity(ident);
                                return;
                            }
                            if (!task.getWindowConfiguration().canResizeTask() && !task.getWrapper().getExtImpl().isFlexibleTaskMaximizing() && !task.getWrapper().getExtImpl().isFlexibleTaskChanging()) {
                                android.util.Slog.w(TAG, "resizeTask not allowed on task=" + task);
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                android.os.Binder.restoreCallingIdentity(ident);
                                return;
                            }
                            final boolean preserveWindow = (resizeMode & 1) != 0;
                            if (!getTransitionController().isShellTransitionsEnabled()) {
                                task.resize(bounds, resizeMode, preserveWindow);
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                android.os.Binder.restoreCallingIdentity(ident);
                            } else {
                                final com.android.server.wm.Transition transition = new com.android.server.wm.Transition(6, 0, getTransitionController(), this.mWindowManager.mSyncEngine);
                                getTransitionController().startCollectOrQueue(transition, new com.android.server.wm.TransitionController.OnStartCollect() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda5
                                    @Override // com.android.server.wm.TransitionController.OnStartCollect
                                    public final void onCollectStarted(boolean z) {
                                        this.f$0.lambda$resizeTask$2(task, transition, bounds, resizeMode, preserveWindow, z);
                                    }
                                });
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                android.os.Binder.restoreCallingIdentity(ident);
                            }
                        } catch (java.lang.Throwable th) {
                            th = th;
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    android.os.Binder.restoreCallingIdentity(ident);
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resizeTask$2(com.android.server.wm.Task task, com.android.server.wm.Transition transition, android.graphics.Rect bounds, int resizeMode, boolean preserveWindow, boolean deferred) {
        if (deferred && !task.getWindowConfiguration().canResizeTask() && !task.getWrapper().getExtImpl().isFlexibleTaskMaximizing() && !task.getWrapper().getExtImpl().isFlexibleTaskChanging()) {
            android.util.Slog.w(TAG, "resizeTask not allowed on task=" + task);
            transition.abort();
        } else {
            getTransitionController().requestStartTransition(transition, task, null, null);
            getTransitionController().collect(task);
            task.resize(bounds, resizeMode, preserveWindow);
            transition.setReady(task, true);
        }
    }

    public void releaseSomeActivities(android.app.IApplicationThread appInt) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long origId = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.wm.WindowProcessController app = getProcessController(appInt);
                    app.releaseSomeActivities("low-mem");
                } finally {
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void setLockScreenShown(final boolean keyguardShowing, final boolean aodShowing) {
        if (checkCallingPermission("android.permission.DEVICE_POWER") != 0) {
            throw new java.lang.SecurityException("Requires permission android.permission.DEVICE_POWER");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long ident = android.os.Binder.clearCallingIdentity();
                if (this.mKeyguardShown != keyguardShowing) {
                    this.mKeyguardShown = keyguardShowing;
                    android.os.Message msg = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda0
                        @Override // java.util.function.BiConsumer
                        public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                            ((android.app.ActivityManagerInternal) obj).reportCurKeyguardUsageEvent(((java.lang.Boolean) obj2).booleanValue());
                        }
                    }, this.mAmInternal, java.lang.Boolean.valueOf(keyguardShowing));
                    this.mH.sendMessage(msg);
                    if (!keyguardShowing) {
                        mActivityTaskManagerExt.setScreenOffPlay(false);
                    }
                }
                if ((this.mDemoteTopAppReasons & 1) != 0) {
                    this.mDemoteTopAppReasons &= -2;
                    if (this.mTopApp != null) {
                        this.mTopApp.scheduleUpdateOomAdj();
                    }
                }
                try {
                    android.os.Trace.traceBegin(32L, "setLockScreenShown");
                    this.mRootWindowContainer.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$setLockScreenShown$3(keyguardShowing, aodShowing, (com.android.server.wm.DisplayContent) obj);
                        }
                    });
                    maybeHideLockedProfileActivityLocked();
                } finally {
                    android.os.Trace.traceEnd(32L);
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setLockScreenShown$4(keyguardShowing);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLockScreenShown$3(boolean keyguardShowing, boolean aodShowing, com.android.server.wm.DisplayContent displayContent) {
        this.mKeyguardController.setKeyguardShown(displayContent.getDisplayId(), keyguardShowing, aodShowing);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLockScreenShown$4(boolean keyguardShowing) {
        for (int i = this.mScreenObservers.size() - 1; i >= 0; i--) {
            this.mScreenObservers.get(i).onKeyguardStateChanged(keyguardShowing);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeHideLockedProfileActivityLocked() {
        android.content.pm.UserInfo userInfo;
        if (this.mKeyguardController.isKeyguardLocked(0) && this.mLastResumedActivity != null && (userInfo = getUserManager().getUserInfo(this.mLastResumedActivity.mUserId)) != null && userInfo.isManagedProfile() && this.mAmInternal.shouldConfirmCredentials(this.mLastResumedActivity.mUserId)) {
            this.mInternal.startHomeActivity(this.mAmInternal.getCurrentUserId(), "maybeHideLockedProfileActivityLocked");
        }
    }

    public void onScreenAwakeChanged(final boolean isAwake) {
        com.android.server.wm.WindowProcessController proc;
        this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onScreenAwakeChanged$5(isAwake);
            }
        });
        if (isAwake) {
            return;
        }
        synchronized (this.mGlobalLockWithoutBoost) {
            this.mDemoteTopAppReasons &= -2;
            com.android.server.wm.WindowState notificationShade = this.mRootWindowContainer.getDefaultDisplay().getDisplayPolicy().getNotificationShade();
            proc = notificationShade != null ? notificationShade.getProcess() : null;
        }
        setProcessAnimatingWhileDozing(proc);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onScreenAwakeChanged$5(boolean isAwake) {
        for (int i = this.mScreenObservers.size() - 1; i >= 0; i--) {
            this.mScreenObservers.get(i).onAwakeStateChanged(isAwake);
        }
    }

    void setProcessAnimatingWhileDozing(com.android.server.wm.WindowProcessController proc) {
        if (proc == null) {
            return;
        }
        proc.setRunningAnimationUnsafe();
        this.mH.sendMessage(this.mH.obtainMessage(5, proc));
        this.mH.removeMessages(6, proc);
        this.mH.sendMessageDelayed(this.mH.obtainMessage(6, proc), DOZE_ANIMATING_STATE_RETAIN_TIME_MS);
        android.os.Trace.instant(32L, "requestWakefulnessAnimating");
    }

    public android.graphics.Bitmap getTaskDescriptionIcon(final java.lang.String filePath, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        int userId2 = handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, "getTaskDescriptionIcon");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord matchingActivity = this.mRootWindowContainer.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda11
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.wm.ActivityTaskManagerService.lambda$getTaskDescriptionIcon$6(filePath, (com.android.server.wm.ActivityRecord) obj);
                    }
                });
                if (matchingActivity == null || matchingActivity.getUid() != callingUid) {
                    try {
                        enforceActivityTaskPermission("getTaskDescriptionIcon");
                    } catch (java.lang.SecurityException e) {
                        android.util.Slog.w(TAG, "getTaskDescriptionIcon(): request (callingUid=" + callingUid + ", filePath=" + filePath + ", user=" + userId2 + ") doesn't match any activity");
                        throw e;
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        java.io.File passedIconFile = new java.io.File(filePath);
        java.io.File legitIconFile = new java.io.File(com.android.server.wm.TaskPersister.getUserImagesDir(userId2), passedIconFile.getName());
        if (!legitIconFile.getPath().equals(filePath) || !filePath.contains("_activity_icon_")) {
            throw new java.lang.IllegalArgumentException("Bad file path: " + filePath + " passed for userId " + userId2);
        }
        return this.mRecentTasks.getTaskDescriptionIcon(filePath);
    }

    static /* synthetic */ boolean lambda$getTaskDescriptionIcon$6(java.lang.String filePath, com.android.server.wm.ActivityRecord r) {
        if (r.taskDescription == null || r.taskDescription.getIconFilename() == null) {
            return false;
        }
        return r.taskDescription.getIconFilename().equals(filePath);
    }

    public void moveRootTaskToDisplay(int taskId, int displayId) {
        this.mAmInternal.enforceCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "moveRootTaskToDisplay()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                        long protoLogParam0 = taskId;
                        long protoLogParam1 = displayId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 893763316922465955L, 5, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
                    }
                    this.mRootWindowContainer.moveRootTaskToDisplay(taskId, displayId, true);
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void registerTaskStackListener(android.app.ITaskStackListener listener) {
        enforceTaskPermission("registerTaskStackListener()");
        this.mTaskChangeNotificationController.registerTaskStackListener(listener);
    }

    public void unregisterTaskStackListener(android.app.ITaskStackListener listener) {
        enforceTaskPermission("unregisterTaskStackListener()");
        this.mTaskChangeNotificationController.unregisterTaskStackListener(listener);
    }

    public boolean requestAssistContextExtras(int requestType, android.app.IAssistDataReceiver receiver, android.os.Bundle receiverExtras, android.os.IBinder activityToken, boolean checkActivityIsTop, boolean newSessionId) {
        return enqueueAssistContext(requestType, null, null, receiver, receiverExtras, activityToken, checkActivityIsTop, newSessionId, android.os.UserHandle.getCallingUserId(), null, DOZE_ANIMATING_STATE_RETAIN_TIME_MS, 0) != null;
    }

    public boolean requestAssistDataForTask(android.app.IAssistDataReceiver receiver, int taskId, java.lang.String callingPackageName, java.lang.String callingAttributionTag) {
        this.mAmInternal.enforceCallingPermission("android.permission.GET_TOP_ACTIVITY_INFO", "requestAssistDataForTask()");
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens tokens = this.mInternal.getAttachedNonFinishingActivityForTask(taskId, null);
            if (tokens == null) {
                android.util.Log.e(TAG, "Could not find activity for task " + taskId);
                return false;
            }
            com.android.server.wm.AssistDataReceiverProxy proxy = new com.android.server.wm.AssistDataReceiverProxy(receiver, callingPackageName);
            java.lang.Object lock = new java.lang.Object();
            com.android.server.am.AssistDataRequester requester = new com.android.server.am.AssistDataRequester(this.mContext, this.mWindowManager, getAppOpsManager(), proxy, lock, 49, -1);
            java.util.List<android.os.IBinder> topActivityToken = new java.util.ArrayList<>();
            topActivityToken.add(tokens.getActivityToken());
            requester.requestAssistData(topActivityToken, true, false, false, true, false, true, android.os.Binder.getCallingUid(), callingPackageName, callingAttributionTag);
            return true;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    public boolean requestAutofillData(android.app.IAssistDataReceiver receiver, android.os.Bundle receiverExtras, android.os.IBinder activityToken, int flags) {
        return enqueueAssistContext(2, null, null, receiver, receiverExtras, activityToken, true, true, android.os.UserHandle.getCallingUserId(), null, DOZE_ANIMATING_STATE_RETAIN_TIME_MS, flags) != null;
    }

    public android.os.Bundle getAssistContextExtras(int requestType) throws android.os.RemoteException {
        com.android.server.wm.ActivityTaskManagerService.PendingAssistExtras pae = enqueueAssistContext(requestType, null, null, null, null, null, true, true, android.os.UserHandle.getCallingUserId(), null, 500L, 0);
        if (pae == null) {
            return null;
        }
        synchronized (pae) {
            while (!pae.haveResult) {
                try {
                    pae.wait();
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                buildAssistBundleLocked(pae, pae.result);
                this.mPendingAssistExtras.remove(pae);
                this.mUiHandler.removeCallbacks(pae);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return pae.extras;
    }

    private static int checkCallingPermission(java.lang.String permission) {
        return checkPermission(permission, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
    }

    boolean checkCanCloseSystemDialogs(int pid, int uid, java.lang.String packageName) {
        com.android.server.wm.WindowProcessController process;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                process = this.mProcessMap.getProcess(pid);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        if (packageName == null && process != null) {
            packageName = process.mInfo.packageName;
        }
        java.lang.String caller = "(pid=" + pid + ", uid=" + uid + ")";
        if (packageName != null) {
            caller = packageName + " " + caller;
        }
        if (canCloseSystemDialogs(pid, uid)) {
            return true;
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(174664365L, uid)) {
            if (packageName != null) {
                mActivityTaskManagerExt.handleCompatibilityException(1, packageName);
            }
            throw new java.lang.SecurityException("Permission Denial: android.intent.action.CLOSE_SYSTEM_DIALOGS broadcast from " + caller + " requires android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS.");
        }
        if (android.app.compat.CompatChanges.isChangeEnabled(174664120L, uid)) {
            if (packageName != null) {
                mActivityTaskManagerExt.handleCompatibilityException(1, packageName);
            }
            android.util.Slog.e(TAG, "Permission Denial: android.intent.action.CLOSE_SYSTEM_DIALOGS broadcast from " + caller + " requires android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS, dropping broadcast.");
            return false;
        }
        android.util.Slog.w(TAG, "android.intent.action.CLOSE_SYSTEM_DIALOGS broadcast from " + caller + " will require android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS in future builds.");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canCloseSystemDialogs(int pid, int uid) {
        if (checkPermission("android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS", pid, uid) == 0) {
            return true;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                android.util.ArraySet<com.android.server.wm.WindowProcessController> processes = this.mProcessMap.getProcesses(uid);
                if (processes != null) {
                    int n = processes.size();
                    for (int i = 0; i < n; i++) {
                        com.android.server.wm.WindowProcessController process = processes.valueAt(i);
                        int sourceUid = process.getInstrumentationSourceUid();
                        if (process.isInstrumenting() && sourceUid != -1 && checkPermission("android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS", -1, sourceUid) == 0) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return true;
                        }
                        if (process.canCloseSystemDialogsByToken()) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return true;
                        }
                    }
                }
                if (!android.app.compat.CompatChanges.isChangeEnabled(174664365L, uid)) {
                    if (this.mRootWindowContainer.hasVisibleWindowAboveButDoesNotOwnNotificationShade(uid)) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return true;
                    }
                    if (com.android.internal.util.ArrayUtils.contains(this.mAccessibilityServiceUids, uid)) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return true;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return false;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void enforceActivityTaskPermission(java.lang.String func) {
        enforceTaskPermission(func);
    }

    static void enforceTaskPermission(java.lang.String func) {
        if (checkCallingPermission("android.permission.MANAGE_ACTIVITY_TASKS") == 0) {
            return;
        }
        if (checkCallingPermission("android.permission.MANAGE_ACTIVITY_STACKS") == 0) {
            android.util.Slog.w(TAG, "MANAGE_ACTIVITY_STACKS is deprecated, please use alternative permission: MANAGE_ACTIVITY_TASKS");
        } else {
            if (mActivityTaskManagerExt.checkOplusWindowPermission()) {
                return;
            }
            java.lang.String msg = "Permission Denial: " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.MANAGE_ACTIVITY_TASKS";
            android.util.Slog.w(TAG, msg);
            throw new java.lang.SecurityException(msg);
        }
    }

    static int checkPermission(java.lang.String permission, int pid, int uid) {
        if (permission == null) {
            return -1;
        }
        return checkComponentPermission(permission, pid, uid, -1, true);
    }

    public static int checkComponentPermission(java.lang.String permission, int pid, int uid, int owningUid, boolean exported) {
        return com.android.server.am.ActivityManagerService.checkComponentPermission(permission, pid, uid, owningUid, exported);
    }

    boolean isCallerRecents(int callingUid) {
        return this.mRecentTasks.isCallerRecents(callingUid);
    }

    boolean isGetTasksAllowed(java.lang.String caller, int callingPid, int callingUid) {
        if (isCallerRecents(callingUid)) {
            return true;
        }
        boolean allowed = checkPermission("android.permission.REAL_GET_TASKS", callingPid, callingUid) == 0;
        if (!allowed) {
            if (checkPermission("android.permission.GET_TASKS", callingPid, callingUid) == 0) {
                try {
                    if (android.app.AppGlobals.getPackageManager().isUidPrivileged(callingUid)) {
                        allowed = true;
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[3]) {
                            java.lang.String protoLogParam0 = java.lang.String.valueOf(caller);
                            long protoLogParam1 = callingUid;
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 8392804603924461448L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
                        }
                    }
                } catch (android.os.RemoteException e) {
                }
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[3]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(caller);
                long protoLogParam12 = callingUid;
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 4303745325174700522L, 4, null, protoLogParam02, java.lang.Long.valueOf(protoLogParam12));
            }
        }
        return allowed;
    }

    boolean isCrossUserAllowed(int pid, int uid) {
        return checkPermission("android.permission.INTERACT_ACROSS_USERS", pid, uid) == 0 || checkPermission("android.permission.INTERACT_ACROSS_USERS_FULL", pid, uid) == 0;
    }

    private com.android.server.wm.ActivityTaskManagerService.PendingAssistExtras enqueueAssistContext(int requestType, android.content.Intent intent, java.lang.String hint, android.app.IAssistDataReceiver receiver, android.os.Bundle receiverExtras, android.os.IBinder activityToken, boolean checkActivityIsTop, boolean newSessionId, int userHandle, android.os.Bundle args, long timeout, int flags) throws android.os.RemoteException {
        boolean z;
        com.android.server.wm.ActivityRecord activity;
        com.android.server.wm.ActivityRecord activity2;
        com.android.server.wm.ActivityRecord caller;
        this.mAmInternal.enforceCallingPermission("android.permission.GET_TOP_ACTIVITY_INFO", "enqueueAssistContext()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    com.android.server.wm.Task rootTask = getTopDisplayFocusedRootTask();
                    if (rootTask == null) {
                        activity = null;
                    } else {
                        try {
                            activity = rootTask.getTopNonFinishingActivity();
                        } catch (java.lang.Throwable th) {
                            e = th;
                        }
                    }
                    if (activity == null) {
                        android.util.Slog.w(TAG, "getAssistContextExtras failed: no top activity");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    if (!activity.attachedToProcess()) {
                        android.util.Slog.w(TAG, "getAssistContextExtras failed: no process for " + activity);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    z = checkActivityIsTop;
                    try {
                        try {
                            if (mActivityTaskManagerExt.isFromViewExtract(z, receiverExtras)) {
                                if (activityToken != null && activity != (caller = com.android.server.wm.ActivityRecord.forTokenLocked(activityToken))) {
                                    android.util.Slog.w(TAG, "enqueueAssistContext failed: caller " + caller + " is not current top " + activity);
                                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                    return null;
                                }
                                activity2 = activity;
                            } else {
                                com.android.server.wm.ActivityRecord activity3 = com.android.server.wm.ActivityRecord.forTokenLocked(activityToken);
                                if (activity3 == null) {
                                    android.util.Slog.w(TAG, "enqueueAssistContext failed: activity for token=" + activityToken + " couldn't be found");
                                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                    return null;
                                }
                                if (activity3.attachedToProcess()) {
                                    activity2 = activity3;
                                } else {
                                    android.util.Slog.w(TAG, "enqueueAssistContext failed: no process for " + activity3);
                                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                    return null;
                                }
                            }
                            android.os.Bundle extras = new android.os.Bundle();
                            if (args != null) {
                                extras.putAll(args);
                            }
                            extras.putString("android.intent.extra.ASSIST_PACKAGE", activity2.packageName);
                            extras.putInt("android.intent.extra.ASSIST_UID", activity2.app.mUid);
                            com.android.server.wm.ActivityRecord activity4 = activity2;
                            com.android.server.wm.ActivityTaskManagerService.PendingAssistExtras pae = new com.android.server.wm.ActivityTaskManagerService.PendingAssistExtras(activity2, extras, intent, hint, receiver, receiverExtras, userHandle);
                            pae.isHome = activity4.isActivityTypeHome();
                            if (newSessionId) {
                                this.mViSessionId++;
                            }
                            try {
                                activity4.app.getThread().requestAssistContextExtras(activity4.token, pae, requestType, this.mViSessionId, flags);
                                this.mPendingAssistExtras.add(pae);
                            } catch (android.os.RemoteException e) {
                            }
                            try {
                                this.mUiHandler.postDelayed(pae, timeout);
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return pae;
                            } catch (android.os.RemoteException e2) {
                                android.util.Slog.w(TAG, "getAssistContextExtras failed: crash calling " + activity4);
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return null;
                            }
                        } catch (java.lang.Throwable th2) {
                            e = th2;
                        }
                    } catch (java.lang.Throwable th3) {
                        e = th3;
                    }
                } catch (java.lang.Throwable th4) {
                    e = th4;
                }
            } catch (java.lang.Throwable th5) {
                e = th5;
                z = checkActivityIsTop;
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            throw e;
        }
    }

    private void buildAssistBundleLocked(com.android.server.wm.ActivityTaskManagerService.PendingAssistExtras pae, android.os.Bundle result) {
        if (result != null) {
            pae.extras.putBundle("android.intent.extra.ASSIST_CONTEXT", result);
        }
        if (pae.hint != null) {
            pae.extras.putBoolean(pae.hint, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pendingAssistExtrasTimedOut(com.android.server.wm.ActivityTaskManagerService.PendingAssistExtras pae) {
        android.app.IAssistDataReceiver receiver;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mPendingAssistExtras.remove(pae);
                receiver = pae.receiver;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        if (receiver != null) {
            android.os.Bundle sendBundle = new android.os.Bundle();
            sendBundle.putBundle(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_RECEIVER_EXTRAS, pae.receiverExtras);
            try {
                pae.receiver.onHandleAssistData(sendBundle);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public class PendingAssistExtras extends android.os.Binder implements java.lang.Runnable {
        public final com.android.server.wm.ActivityRecord activity;
        public final android.os.Bundle extras;
        public final java.lang.String hint;
        public final android.content.Intent intent;
        public boolean isHome;
        public final android.app.IAssistDataReceiver receiver;
        public android.os.Bundle receiverExtras;
        public final int userHandle;
        public boolean haveResult = false;
        public android.os.Bundle result = null;
        public android.app.assist.AssistStructure structure = null;
        public android.app.assist.AssistContent content = null;

        public PendingAssistExtras(com.android.server.wm.ActivityRecord _activity, android.os.Bundle _extras, android.content.Intent _intent, java.lang.String _hint, android.app.IAssistDataReceiver _receiver, android.os.Bundle _receiverExtras, int _userHandle) {
            this.activity = _activity;
            this.extras = _extras;
            this.intent = _intent;
            this.hint = _hint;
            this.receiver = _receiver;
            this.receiverExtras = _receiverExtras;
            this.userHandle = _userHandle;
        }

        @Override // java.lang.Runnable
        public void run() {
            android.util.Slog.w(com.android.server.wm.ActivityTaskManagerService.TAG, "getAssistContextExtras failed: timeout retrieving from " + this.activity);
            synchronized (this) {
                this.haveResult = true;
                notifyAll();
            }
            com.android.server.wm.ActivityTaskManagerService.this.pendingAssistExtrasTimedOut(this);
        }
    }

    public boolean isAssistDataAllowed() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task focusedRootTask = getTopDisplayFocusedRootTask();
                if (focusedRootTask != null && !focusedRootTask.isActivityTypeAssistant()) {
                    com.android.server.wm.ActivityRecord activity = focusedRootTask.getTopNonFinishingActivity();
                    if (activity == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    int userId = activity.mUserId;
                    com.android.server.wm.DisplayContent displayContent = activity.getDisplayContent();
                    if (displayContent == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    long callingIdentity = android.os.Binder.clearCallingIdentity();
                    try {
                        boolean hasRestrictedWindow = displayContent.forAllWindows(new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda22
                            public final boolean apply(java.lang.Object obj) {
                                return this.f$0.lambda$isAssistDataAllowed$7((com.android.server.wm.WindowState) obj);
                            }
                        }, true);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return android.app.admin.DevicePolicyCache.getInstance().isScreenCaptureAllowed(userId) && !hasRestrictedWindow;
                    } finally {
                        android.os.Binder.restoreCallingIdentity(callingIdentity);
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return false;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$isAssistDataAllowed$7(com.android.server.wm.WindowState windowState) {
        return windowState.isOnScreen() && (android.os.UserManager.isUserTypePrivateProfile(getUserManager().getProfileType(windowState.mShowUserId)) || hasUserRestriction("no_assist_content", windowState.mShowUserId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocalVoiceInteractionStartedLocked(android.os.IBinder activity, android.service.voice.IVoiceInteractionSession voiceSession, com.android.internal.app.IVoiceInteractor voiceInteractor) {
        com.android.server.wm.ActivityRecord activityToCallback = com.android.server.wm.ActivityRecord.forTokenLocked(activity);
        if (activityToCallback == null) {
            return;
        }
        activityToCallback.setVoiceSessionLocked(voiceSession);
        try {
            activityToCallback.app.getThread().scheduleLocalVoiceInteractionStarted(activity, voiceInteractor);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                startRunningVoiceLocked(voiceSession, activityToCallback.info.applicationInfo.uid);
                android.os.Binder.restoreCallingIdentity(token);
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        } catch (android.os.RemoteException e) {
            activityToCallback.clearVoiceSessionLocked();
        }
    }

    private void startRunningVoiceLocked(android.service.voice.IVoiceInteractionSession session, int targetUid) {
        android.util.Slog.d(TAG, "<<<  startRunningVoiceLocked()");
        this.mVoiceWakeLock.setWorkSource(new android.os.WorkSource(targetUid));
        if (this.mRunningVoice == null || this.mRunningVoice.asBinder() != session.asBinder()) {
            boolean wasRunningVoice = this.mRunningVoice != null;
            this.mRunningVoice = session;
            if (!wasRunningVoice) {
                this.mVoiceWakeLock.acquire();
                updateSleepIfNeededLocked();
            }
        }
    }

    void finishRunningVoiceLocked() {
        if (this.mRunningVoice != null) {
            this.mRunningVoice = null;
            this.mVoiceWakeLock.release();
            updateSleepIfNeededLocked();
        }
    }

    public void setVoiceKeepAwake(android.service.voice.IVoiceInteractionSession session, boolean keepAwake) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mRunningVoice != null && this.mRunningVoice.asBinder() == session.asBinder()) {
                    if (keepAwake) {
                        this.mVoiceWakeLock.acquire();
                    } else {
                        this.mVoiceWakeLock.release();
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void keyguardGoingAway(final int flags) {
        this.mAmInternal.enforceCallingPermission("android.permission.CONTROL_KEYGUARD", "unlock keyguard");
        enforceNotIsolatedCaller("keyguardGoingAway");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if ((flags & 16) != 0) {
                        this.mActivityClientController.invalidateHomeTaskSnapshot(null);
                    } else if (this.mKeyguardShown) {
                        this.mDemoteTopAppReasons |= 1;
                    }
                    this.mRootWindowContainer.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda16
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$keyguardGoingAway$8(flags, (com.android.server.wm.DisplayContent) obj);
                        }
                    });
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            com.android.server.wallpaper.WallpaperManagerInternal wallpaperManagerInternal = getWallpaperManagerInternal();
            if (wallpaperManagerInternal != null) {
                wallpaperManagerInternal.onKeyguardGoingAway();
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$keyguardGoingAway$8(int flags, com.android.server.wm.DisplayContent displayContent) {
        this.mKeyguardController.keyguardGoingAway(displayContent.getDisplayId(), flags);
    }

    public void suppressResizeConfigChanges(boolean suppress) throws android.os.RemoteException {
        this.mAmInternal.enforceCallingPermission("android.permission.MANAGE_ACTIVITY_TASKS", "suppressResizeConfigChanges()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mSuppressResizeConfigChanges = suppress;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void onSplashScreenViewCopyFinished(int taskId, android.window.SplashScreenView.SplashScreenViewParcelable parcelable) throws android.os.RemoteException {
        this.mAmInternal.enforceCallingPermission("android.permission.MANAGE_ACTIVITY_TASKS", "copySplashScreenViewFinish()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId, 0);
                if (task != null) {
                    task.getWrapper().getExtImpl().setSplashScreenViewCopy(true);
                    com.android.server.wm.ActivityRecord r = task.getTopWaitSplashScreenActivity();
                    if (r != null) {
                        r.onCopySplashScreenFinish(parcelable);
                    }
                } else {
                    android.util.Slog.d(TAG, "onSplashScreenViewCopyFinished taskId=" + taskId + ",task null ");
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    boolean prepareAutoEnterPictureAndPictureMode(com.android.server.wm.ActivityRecord r) {
        if (r.inPinnedWindowingMode()) {
            return true;
        }
        if (r.canAutoEnterPip() && getTransitionController().getCollectingTransition() != null) {
            getTransitionController().getCollectingTransition().setPipActivity(r);
            return true;
        }
        return false;
    }

    boolean enterPictureInPictureMode(com.android.server.wm.ActivityRecord r, android.app.PictureInPictureParams params, boolean fromClient) {
        return enterPictureInPictureMode(r, params, fromClient, false);
    }

    boolean enterPictureInPictureMode(final com.android.server.wm.ActivityRecord r, final android.app.PictureInPictureParams params, boolean fromClient, final boolean isAutoEnter) {
        com.android.server.wm.Transition transition;
        if (r.inPinnedWindowingMode()) {
            return true;
        }
        if (!r.checkEnterPictureInPictureState("enterPictureInPictureMode", false)) {
            return false;
        }
        if (fromClient && r.isState(com.android.server.wm.ActivityRecord.State.PAUSING) && params.isAutoEnterEnabled()) {
            android.util.Slog.w(TAG, "Skip client enterPictureInPictureMode request while pausing, auto-enter-pip is enabled");
            return false;
        }
        if (isPip2ExperimentEnabled()) {
            final com.android.server.wm.Transition enterPipTransition = new com.android.server.wm.Transition(10, 0, getTransitionController(), this.mWindowManager.mSyncEngine);
            enterPipTransition.setPipActivity(r);
            r.mAutoEnteringPip = isAutoEnter;
            getTransitionController().startCollectOrQueue(enterPipTransition, new com.android.server.wm.TransitionController.OnStartCollect() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda7
                @Override // com.android.server.wm.TransitionController.OnStartCollect
                public final void onCollectStarted(boolean z) {
                    this.f$0.lambda$enterPictureInPictureMode$9(enterPipTransition, r, z);
                }
            });
            return true;
        }
        boolean originallyFromClient = fromClient && (!r.isState(com.android.server.wm.ActivityRecord.State.PAUSING) || params.isAutoEnterEnabled());
        if (getTransitionController().isShellTransitionsEnabled() && originallyFromClient) {
            transition = new com.android.server.wm.Transition(10, 0, getTransitionController(), this.mWindowManager.mSyncEngine);
        } else {
            transition = null;
        }
        final com.android.server.wm.Transition transition2 = transition;
        if (r.getTaskFragment() != null && r.getTaskFragment().isEmbeddedWithBoundsOverride() && transition2 != null) {
            transition2.addFlag(512);
        }
        if (mActivityTaskManagerExt.interceptEnterPictureInPictureMode(r, params)) {
            return false;
        }
        if (!LTW_DISABLE && mActivityTaskManagerExt.getRemoteTaskManager().anyTaskExist(r.getTask().mTaskId)) {
            return false;
        }
        final java.lang.Runnable enterPipRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$enterPictureInPictureMode$10(r, transition2, isAutoEnter, params);
            }
        };
        if (r.isKeyguardLocked()) {
            this.mActivityClientController.dismissKeyguard(r.token, new com.android.server.wm.ActivityTaskManagerService.AnonymousClass2(transition2, enterPipRunnable), null);
        } else if (transition2 != null) {
            getTransitionController().startCollectOrQueue(transition2, new com.android.server.wm.TransitionController.OnStartCollect() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda9
                @Override // com.android.server.wm.TransitionController.OnStartCollect
                public final void onCollectStarted(boolean z) {
                    enterPipRunnable.run();
                }
            });
        } else {
            enterPipRunnable.run();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enterPictureInPictureMode$9(com.android.server.wm.Transition enterPipTransition, com.android.server.wm.ActivityRecord r, boolean deferred) {
        getTransitionController().requestStartTransition(enterPipTransition, r.getTask(), null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enterPictureInPictureMode$10(com.android.server.wm.ActivityRecord r, com.android.server.wm.Transition transition, boolean isAutoEnter, android.app.PictureInPictureParams params) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.utils.LogUtil.d(TAG, "The app: " + r.packageName + " Enter picture-in-picture mode, caller: " + android.os.Debug.getCallers(3));
                if (r.getParent() == null) {
                    android.util.Slog.e(TAG, "Skip enterPictureInPictureMode, destroyed " + r);
                    if (transition != null) {
                        transition.abort();
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                com.android.server.wm.EventLogTags.writeWmEnterPip(r.mUserId, java.lang.System.identityHashCode(r), r.shortComponentName, java.lang.Boolean.toString(isAutoEnter));
                r.setPictureInPictureParams(params);
                r.mAutoEnteringPip = isAutoEnter;
                this.mRootWindowContainer.moveActivityToPinnedRootTask(r, null, "enterPictureInPictureMode", transition);
                if (r.isState(com.android.server.wm.ActivityRecord.State.PAUSING) && r.mPauseSchedulePendingForPip) {
                    r.getTask().schedulePauseActivity(r, false, false, true, "auto-pip");
                }
                r.mAutoEnteringPip = false;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: renamed from: com.android.server.wm.ActivityTaskManagerService$2, reason: invalid class name */
    class AnonymousClass2 extends com.android.internal.policy.KeyguardDismissCallback {
        final /* synthetic */ java.lang.Runnable val$enterPipRunnable;
        final /* synthetic */ com.android.server.wm.Transition val$transition;

        AnonymousClass2(com.android.server.wm.Transition transition, java.lang.Runnable runnable) {
            this.val$transition = transition;
            this.val$enterPipRunnable = runnable;
        }

        public void onDismissSucceeded() {
            if (this.val$transition == null) {
                com.android.server.wm.ActivityTaskManagerService.this.mH.post(this.val$enterPipRunnable);
                return;
            }
            com.android.server.wm.TransitionController transitionController = com.android.server.wm.ActivityTaskManagerService.this.getTransitionController();
            com.android.server.wm.Transition transition = this.val$transition;
            final java.lang.Runnable runnable = this.val$enterPipRunnable;
            transitionController.startCollectOrQueue(transition, new com.android.server.wm.TransitionController.OnStartCollect() { // from class: com.android.server.wm.ActivityTaskManagerService$2$$ExternalSyntheticLambda0
                @Override // com.android.server.wm.TransitionController.OnStartCollect
                public final void onCollectStarted(boolean z) {
                    this.f$0.lambda$onDismissSucceeded$0(runnable, z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDismissSucceeded$0(java.lang.Runnable enterPipRunnable, boolean deferred) {
            if (deferred) {
                enterPipRunnable.run();
            } else {
                com.android.server.wm.ActivityTaskManagerService.this.mH.post(enterPipRunnable);
            }
        }
    }

    public android.window.IWindowOrganizerController getWindowOrganizerController() {
        return this.mWindowOrganizerController;
    }

    public void enforceSystemHasVrFeature() {
        if (!this.mContext.getPackageManager().hasSystemFeature("android.hardware.vr.high_performance")) {
            throw new java.lang.UnsupportedOperationException("VR mode not supported on this device!");
        }
    }

    public boolean supportsLocalVoiceInteraction() {
        return ((android.service.voice.VoiceInteractionManagerInternal) com.android.server.LocalServices.getService(android.service.voice.VoiceInteractionManagerInternal.class)).supportsLocalVoiceInteraction();
    }

    public boolean updateConfiguration(android.content.res.Configuration values) {
        this.mAmInternal.enforceCallingPermission("android.permission.CHANGE_CONFIGURATION", "updateConfiguration()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mWindowManager == null) {
                    android.util.Slog.w(TAG, "Skip updateConfiguration because mWindowManager isn't set");
                    this.mTempConfig.setTo(getGlobalConfiguration());
                    this.mTempConfig.updateFrom(values);
                    this.mTempConfig.seq = increaseConfigurationSeqLocked();
                    this.mSystemThread.applyConfigurationToResources(this.mTempConfig);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                if (values == null) {
                    values = this.mWindowManager.computeNewConfiguration(0);
                }
                this.mH.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda24(), this.mAmInternal, 0));
                long origId = android.os.Binder.clearCallingIdentity();
                if (values != null) {
                    try {
                        android.provider.Settings.System.clearConfiguration(values);
                    } finally {
                        android.os.Binder.restoreCallingIdentity(origId);
                    }
                }
                updateConfigurationLocked(values, null, false, false, -10000, false);
                boolean z = this.mTmpUpdateConfigurationResult.changes != 0;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return z;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void cancelTaskWindowTransition(int taskId) {
        enforceTaskPermission("cancelTaskWindowTransition()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId, 0);
                    if (task == null) {
                        android.util.Slog.w(TAG, "cancelTaskWindowTransition: taskId=" + taskId + " not found");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    } else {
                        task.cancelTaskWindowTransition();
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX WARN: Finally extract failed */
    public android.window.TaskSnapshot getTaskSnapshot(int taskId, boolean isLowResolution) {
        this.mAmInternal.enforceCallingPermission("android.permission.READ_FRAME_BUFFER", "getTaskSnapshot()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId, 1);
                    if (task == null) {
                        android.util.Slog.w(TAG, "getTaskSnapshot: taskId=" + taskId + " not found");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(ident);
                        return null;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    android.window.TaskSnapshot snapshot = this.mWindowManager.mTaskSnapshotController.getSnapshot(taskId, task.mUserId, true, isLowResolution);
                    android.os.Binder.restoreCallingIdentity(ident);
                    return snapshot;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } catch (java.lang.Throwable th2) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th2;
        }
    }

    public android.window.TaskSnapshot takeTaskSnapshot(int taskId, boolean updateCache) {
        this.mAmInternal.enforceCallingPermission("android.permission.READ_FRAME_BUFFER", "takeTaskSnapshot()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId, 1);
                    if (task != null && task.isVisible()) {
                        if (!updateCache) {
                            android.window.TaskSnapshot taskSnapshotSnapshot = this.mWindowManager.mTaskSnapshotController.snapshot(task);
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return taskSnapshotSnapshot;
                        }
                        if (mActivityTaskManagerExt.shouldDisableSnapshotsWithOrientation(task)) {
                            android.util.Slog.d(TAG, "shouldDisableSnapshotsWithOrientation, No need snapshot.");
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return null;
                        }
                        android.window.TaskSnapshot recordSnapshot = this.mWindowManager.mTaskSnapshotController.recordSnapshot(task);
                        getTransitionController().mExt.recordEmbeddedTaskSnapshots(task);
                        if (recordSnapshot != null) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return recordSnapshot;
                        }
                        android.util.Slog.w(TAG, "recordSnapshot null, use getSnapshot");
                        android.window.TaskSnapshot snapshot = this.mWindowManager.mTaskSnapshotController.getSnapshot(taskId, task.mUserId, false, false);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return snapshot;
                    }
                    android.util.Slog.w(TAG, "takeTaskSnapshot: taskId=" + taskId + " not found or not visible");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public int getLastResumedActivityUserId() {
        this.mAmInternal.enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "getLastResumedActivityUserId()");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mLastResumedActivity == null) {
                    int currentUserId = getCurrentUserId();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return currentUserId;
                }
                int i = this.mLastResumedActivity.mUserId;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return i;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void updateLockTaskFeatures(int userId, int flags) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 0 && callingUid != 1000) {
            this.mAmInternal.enforceCallingPermission("android.permission.UPDATE_LOCK_TASK_PACKAGES", "updateLockTaskFeatures()");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[3]) {
                    long protoLogParam0 = userId;
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(java.lang.Integer.toHexString(flags));
                    com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, -559595900417262876L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
                }
                getLockTaskController().updateLockTaskFeatures(userId, flags);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void registerRemoteAnimationForNextActivityStart(java.lang.String packageName, android.view.RemoteAnimationAdapter adapter, android.os.IBinder launchCookie) {
        this.mAmInternal.enforceCallingPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", "registerRemoteAnimationForNextActivityStart");
        adapter.setCallingPidUid(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long origId = android.os.Binder.clearCallingIdentity();
                try {
                    getActivityStartController().registerRemoteAnimationForNextActivityStart(packageName, adapter, launchCookie);
                } finally {
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void registerRemoteAnimationsForDisplay(int displayId, android.view.RemoteAnimationDefinition definition) {
        this.mAmInternal.enforceCallingPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", "registerRemoteAnimations");
        definition.setCallingPidUid(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent display = this.mRootWindowContainer.getDisplayContent(displayId);
                if (display == null) {
                    android.util.Slog.e(TAG, "Couldn't find display with id: " + displayId);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                long origId = android.os.Binder.clearCallingIdentity();
                try {
                    display.registerRemoteAnimations(definition);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } finally {
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void alwaysShowUnsupportedCompileSdkWarning(android.content.ComponentName activity) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long origId = android.os.Binder.clearCallingIdentity();
                try {
                    this.mAppWarnings.alwaysShowUnsupportedCompileSdkWarning(activity);
                } finally {
                    android.os.Binder.restoreCallingIdentity(origId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void setVrThread(int tid) {
        enforceSystemHasVrFeature();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                int pid = android.os.Binder.getCallingPid();
                com.android.server.wm.WindowProcessController wpc = this.mProcessMap.getProcess(pid);
                this.mVrController.setVrThreadLocked(tid, pid, wpc);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void setPersistentVrThread(int tid) {
        if (checkCallingPermission("android.permission.RESTRICTED_VR_ACCESS") != 0) {
            java.lang.String msg = "Permission Denial: setPersistentVrThread() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires android.permission.RESTRICTED_VR_ACCESS";
            android.util.Slog.w(TAG, msg);
            throw new java.lang.SecurityException(msg);
        }
        enforceSystemHasVrFeature();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                int pid = android.os.Binder.getCallingPid();
                com.android.server.wm.WindowProcessController proc = this.mProcessMap.getProcess(pid);
                this.mVrController.setPersistentVrThreadLocked(tid, pid, proc);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void stopAppSwitches() {
        this.mAmInternal.enforceCallingPermission("android.permission.STOP_APP_SWITCHES", "stopAppSwitches");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAppSwitchesState = 0;
                this.mLastStopAppSwitchesTime = android.os.SystemClock.uptimeMillis();
                this.mH.removeMessages(4);
                this.mH.sendEmptyMessageDelayed(4, 500L);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void resumeAppSwitches() {
        this.mAmInternal.enforceCallingPermission("android.permission.STOP_APP_SWITCHES", "resumeAppSwitches");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAppSwitchesState = 2;
                this.mH.removeMessages(4);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    long getLastStopAppSwitchesTime() {
        return this.mLastStopAppSwitchesTime;
    }

    public void setLastStopAppSwitchesTime() {
        this.mLastStopAppSwitchesTime = android.os.SystemClock.uptimeMillis();
    }

    boolean shouldDisableNonVrUiLocked() {
        return this.mVrController.shouldDisableNonVrUiLocked();
    }

    void applyUpdateVrModeLocked(final com.android.server.wm.ActivityRecord r) {
        if (r.requestedVrComponent != null && r.getDisplayId() != 0) {
            android.util.Slog.i(TAG, "Moving " + r.shortComponentName + " from display " + r.getDisplayId() + " to main display for VR");
            this.mRootWindowContainer.moveRootTaskToDisplay(r.getRootTaskId(), 0, true);
        }
        this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$applyUpdateVrModeLocked$12(r);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyUpdateVrModeLocked$12(com.android.server.wm.ActivityRecord r) {
        if (!this.mVrController.onVrModeChanged(r)) {
            return;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                boolean disableNonVrUi = this.mVrController.shouldDisableNonVrUiLocked();
                this.mWindowManager.disableNonVrUi(disableNonVrUi);
                if (disableNonVrUi) {
                    this.mRootWindowContainer.removeRootTasksInWindowingModes(2);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public int getPackageScreenCompatMode(java.lang.String packageName) {
        int packageScreenCompatModeLocked;
        enforceNotIsolatedCaller("getPackageScreenCompatMode");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                packageScreenCompatModeLocked = this.mCompatModePackages.getPackageScreenCompatModeLocked(packageName);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return packageScreenCompatModeLocked;
    }

    public void setPackageScreenCompatMode(java.lang.String packageName, int mode) {
        this.mAmInternal.enforceCallingPermission("android.permission.SET_SCREEN_COMPATIBILITY", "setPackageScreenCompatMode");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mCompatModePackages.setPackageScreenCompatModeLocked(packageName, mode);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public boolean getPackageAskScreenCompat(java.lang.String packageName) {
        boolean packageAskCompatModeLocked;
        enforceNotIsolatedCaller("getPackageAskScreenCompat");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                packageAskCompatModeLocked = this.mCompatModePackages.getPackageAskCompatModeLocked(packageName);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return packageAskCompatModeLocked;
    }

    public void setPackageAskScreenCompat(java.lang.String packageName, boolean ask) {
        this.mAmInternal.enforceCallingPermission("android.permission.SET_SCREEN_COMPATIBILITY", "setPackageAskScreenCompat");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mCompatModePackages.setPackageAskCompatModeLocked(packageName, ask);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public static java.lang.String relaunchReasonToString(int relaunchReason) {
        switch (relaunchReason) {
            case 1:
                return "window_resize";
            case 2:
                return "free_resize";
            default:
                return null;
        }
    }

    com.android.server.wm.Task getTopDisplayFocusedRootTask() {
        return this.mRootWindowContainer.getTopDisplayFocusedRootTask();
    }

    void notifyTaskPersisterLocked(com.android.server.wm.Task task, boolean flush) {
        this.mRecentTasks.notifyTaskPersisterLocked(task, flush);
    }

    public void clearLaunchParamsForPackages(java.util.List<java.lang.String> packageNames) {
        enforceTaskPermission("clearLaunchParamsForPackages");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            for (int i = 0; i < packageNames.size(); i++) {
                try {
                    this.mTaskSupervisor.mLaunchParamsPersister.removeRecordForPackage(packageNames.get(i));
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void onPictureInPictureUiStateChanged(android.app.PictureInPictureUiState pipState) {
        com.android.server.wm.Task task;
        enforceTaskPermission("onPictureInPictureUiStateChanged");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mRootWindowContainer.getDefaultTaskDisplayArea().hasPinnedTask()) {
                    task = this.mRootWindowContainer.getDefaultTaskDisplayArea().getRootPinnedTask();
                } else {
                    task = this.mRootWindowContainer.getDefaultTaskDisplayArea().getRootTask(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda13
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return ((com.android.server.wm.Task) obj).isActivityTypeStandard();
                        }
                    });
                }
                if (task != null && task.getTopMostActivity() != null && !task.getTopMostActivity().isState(com.android.server.wm.ActivityRecord.State.FINISHING, com.android.server.wm.ActivityRecord.State.DESTROYING, com.android.server.wm.ActivityRecord.State.DESTROYED)) {
                    this.mWindowManager.mAtmService.mActivityClientController.onPictureInPictureUiStateChanged(task.getTopMostActivity(), pipState);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void detachNavigationBarFromApp(android.os.IBinder transition) {
        this.mAmInternal.enforceCallingPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", "detachNavigationBarFromApp");
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    getTransitionController().legacyDetachNavigationBarFromApp(transition);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    void dumpLastANRLocked(java.io.PrintWriter pw) {
        pw.println("ACTIVITY MANAGER LAST ANR (dumpsys activity lastanr)");
        if (this.mLastANRState == null) {
            pw.println("  <no ANR has occurred since boot>");
        } else {
            pw.println(this.mLastANRState);
        }
    }

    void dumpLastANRTracesLocked(java.io.PrintWriter pw) {
        pw.println("ACTIVITY MANAGER LAST ANR TRACES (dumpsys activity lastanr-traces)");
        java.io.File[] files = new java.io.File(com.android.server.am.StackTracesDumpHelper.ANR_TRACE_DIR).listFiles();
        if (com.android.internal.util.ArrayUtils.isEmpty(files)) {
            pw.println("  <no ANR has occurred since boot>");
            return;
        }
        java.io.File latest = null;
        for (java.io.File f : files) {
            if (latest == null || latest.lastModified() < f.lastModified()) {
                latest = f;
            }
        }
        pw.print("File: ");
        pw.print(latest.getName());
        pw.println();
        try {
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(latest));
            while (true) {
                try {
                    java.lang.String line = in.readLine();
                    if (line != null) {
                        pw.println(line);
                    } else {
                        in.close();
                        return;
                    }
                } finally {
                }
            }
        } catch (java.io.IOException e) {
            pw.print("Unable to read: ");
            pw.print(e);
            pw.println();
        }
    }

    void dumpTopResumedActivityLocked(java.io.PrintWriter pw) {
        pw.println("ACTIVITY MANAGER TOP-RESUMED (dumpsys activity top-resumed)");
        com.android.server.wm.ActivityRecord topRecord = this.mRootWindowContainer.getTopResumedActivity();
        if (topRecord != null) {
            topRecord.dump(pw, "", true);
        }
    }

    void dumpVisibleActivitiesLocked(java.io.PrintWriter pw, int displayIdFilter) {
        pw.println("ACTIVITY MANAGER VISIBLE ACTIVITIES (dumpsys activity visible)");
        java.util.ArrayList<com.android.server.wm.ActivityRecord> activities = this.mRootWindowContainer.getDumpActivities("all", true, false, -1);
        boolean needSeparator = false;
        boolean printedAnything = false;
        for (int i = activities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord activity = activities.get(i);
            if (activity.isVisible() && (displayIdFilter == -1 || activity.getDisplayId() == displayIdFilter)) {
                if (needSeparator) {
                    pw.println();
                }
                printedAnything = true;
                activity.dump(pw, "", true);
                needSeparator = true;
            }
        }
        if (!printedAnything) {
            pw.println("(nothing)");
        }
    }

    void dumpActivitiesLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, boolean dumpClient, java.lang.String dumpPackage, int displayIdFilter) {
        dumpActivitiesLocked(fd, pw, args, opti, dumpAll, dumpClient, dumpPackage, displayIdFilter, "ACTIVITY MANAGER ACTIVITIES (dumpsys activity activities)");
    }

    void dumpActivitiesLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, boolean dumpClient, java.lang.String dumpPackage, int displayIdFilter, java.lang.String header) {
        boolean needSep;
        pw.println(header);
        boolean printedAnything = this.mRootWindowContainer.dumpActivities(fd, pw, dumpAll, dumpClient, dumpPackage, displayIdFilter);
        boolean printed = com.android.server.wm.ActivityTaskSupervisor.printThisActivity(pw, this.mRootWindowContainer.getTopResumedActivity(), dumpPackage, displayIdFilter, printedAnything, "  ResumedActivity: ", null);
        if (!printed) {
            needSep = printedAnything;
        } else {
            printedAnything = true;
            needSep = false;
        }
        if (dumpPackage == null) {
            if (needSep) {
                pw.println();
            }
            printedAnything = true;
            this.mTaskSupervisor.dump(pw, "  ");
            this.mTaskOrganizerController.dump(pw, "  ");
            this.mVisibleActivityProcessTracker.dump(pw, "  ");
            this.mActiveUids.dump(pw, "  ");
            pw.println("  SleepTokens=" + this.mRootWindowContainer.mSleepTokens);
            if (this.mDemoteTopAppReasons != 0) {
                pw.println("  mDemoteTopAppReasons=" + this.mDemoteTopAppReasons);
            }
            if (!this.mStartingProcessActivities.isEmpty()) {
                pw.println("  mStartingProcessActivities=" + this.mStartingProcessActivities);
            }
        }
        if (!printedAnything) {
            pw.println("  (nothing)");
        }
    }

    void dumpActivityContainersLocked(java.io.PrintWriter pw) {
        pw.println("ACTIVITY MANAGER CONTAINERS (dumpsys activity containers)");
        this.mRootWindowContainer.dumpChildrenNames(pw, "");
        pw.println(" ");
    }

    void dumpActivityStarterLocked(java.io.PrintWriter pw, java.lang.String dumpPackage) {
        pw.println("ACTIVITY MANAGER STARTER (dumpsys activity starter)");
        getActivityStartController().dump(pw, "", dumpPackage);
    }

    void dumpInstalledPackagesConfig(java.io.PrintWriter pw) {
        this.mPackageConfigPersister.dump(pw, getCurrentUserId());
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x00bb, code lost:
    
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        r22 = r4;
        r23 = r6;
        dumpActivity("  ", r26, r27, r6.get(r4), r18, r31, r36);
        r1 = r19;
        r2 = r20;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected boolean dumpActivity(java.io.FileDescriptor r26, java.io.PrintWriter r27, java.lang.String r28, java.lang.String[] r29, int r30, boolean r31, boolean r32, boolean r33, int r34, int r35, long r36) {
        /*
            Method dump skipped, instruction units count: 290
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityTaskManagerService.dumpActivity(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String, java.lang.String[], int, boolean, boolean, boolean, int, int, long):boolean");
    }

    private void dumpActivity(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter pw, com.android.server.wm.ActivityRecord r, java.lang.String[] args, boolean dumpAll, long timeout) {
        java.lang.String innerPrefix = prefix + "  ";
        android.app.IApplicationThread appThread = null;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                pw.print(prefix);
                pw.print("ACTIVITY ");
                pw.print(r.shortComponentName);
                pw.print(" ");
                pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(r)));
                pw.print(" pid=");
                if (r.hasProcess()) {
                    pw.print(r.app.getPid());
                    appThread = r.app.getThread();
                } else {
                    pw.print("(not running)");
                }
                pw.print(" userId=");
                pw.print(r.mUserId);
                pw.print(" uid=");
                pw.print(r.getUid());
                printDisplayInfoAndNewLine(pw, r);
                if (dumpAll) {
                    r.dump(pw, innerPrefix, true);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        if (appThread != null) {
            if (mActivityTaskManagerExt.isFrozenByHans(r.packageName, r.getUid())) {
                pw.println("\n** this package: " + r.packageName + " has been frozen **");
                return;
            }
            pw.flush();
            try {
                com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
                try {
                    appThread.dumpActivity(tp.getWriteFd(), r.token, innerPrefix, args);
                    tp.go(fd, timeout);
                    tp.close();
                } finally {
                }
            } catch (android.os.RemoteException e) {
                pw.println(innerPrefix + "Got a RemoteException while dumping the activity");
            } catch (java.io.IOException e2) {
                pw.println(innerPrefix + "Failure while dumping the activity: " + e2);
            }
        }
    }

    private void printDisplayInfoAndNewLine(java.io.PrintWriter pw, com.android.server.wm.ActivityRecord r) {
        pw.print(" displayId=");
        com.android.server.wm.DisplayContent displayContent = r.getDisplayContent();
        if (displayContent == null) {
            pw.println("N/A");
            return;
        }
        android.view.Display display = displayContent.getDisplay();
        pw.print(display.getDisplayId());
        pw.print("(type=");
        pw.print(android.view.Display.typeToString(display.getType()));
        pw.println(")");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeSleepStateToProto(android.util.proto.ProtoOutputStream proto, int wakeFullness, boolean testPssMode) {
        long sleepToken = proto.start(1146756268059L);
        proto.write(1159641169921L, android.os.PowerManagerInternal.wakefulnessToProtoEnum(wakeFullness));
        int tokenSize = this.mRootWindowContainer.mSleepTokens.size();
        for (int i = 0; i < tokenSize; i++) {
            com.android.server.wm.RootWindowContainer.SleepToken st = this.mRootWindowContainer.mSleepTokens.valueAt(i);
            proto.write(2237677961218L, st.toString());
        }
        proto.write(1133871366147L, this.mSleeping);
        proto.write(1133871366148L, this.mShuttingDown);
        proto.write(1133871366149L, testPssMode);
        proto.end(sleepToken);
    }

    int getCurrentUserId() {
        return this.mAmInternal.getCurrentUserId();
    }

    static void enforceNotIsolatedCaller(java.lang.String caller) {
        if (android.os.UserHandle.isIsolated(android.os.Binder.getCallingUid())) {
            throw new java.lang.SecurityException("Isolated process not allowed to call " + caller);
        }
    }

    public android.content.res.Configuration getConfiguration() {
        android.content.res.Configuration ci;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ci = new android.content.res.Configuration(getGlobalConfigurationForCallingPid());
                mActivityTaskManagerExt.updateConfigForPocketStudio(ci);
                ci.userSetLocale = false;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return ci;
    }

    android.content.res.Configuration getGlobalConfiguration() {
        return this.mRootWindowContainer != null ? this.mRootWindowContainer.getConfiguration() : new android.content.res.Configuration();
    }

    boolean updateConfigurationLocked(android.content.res.Configuration values, com.android.server.wm.ActivityRecord starting, boolean initLocale) {
        return updateConfigurationLocked(values, starting, initLocale, false);
    }

    boolean updateConfigurationLocked(android.content.res.Configuration values, com.android.server.wm.ActivityRecord starting, boolean initLocale, boolean deferResume) {
        return updateConfigurationLocked(values, starting, initLocale, false, -10000, deferResume);
    }

    public void updatePersistentConfiguration(android.content.res.Configuration values, int userId) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    values.windowConfiguration.setToDefaults();
                    updateConfigurationLocked(values, null, false, true, userId, false);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    boolean updateConfigurationLocked(android.content.res.Configuration values, com.android.server.wm.ActivityRecord starting, boolean initLocale, boolean persistent, int userId, boolean deferResume) {
        int changes = 0;
        boolean kept = true;
        deferWindowLayout();
        if (values != null) {
            try {
                changes = updateGlobalConfigurationLocked(values, initLocale, persistent, userId);
                this.mTmpUpdateConfigurationResult.changes = changes;
                this.mTmpUpdateConfigurationResult.mIsUpdating = true;
            } catch (java.lang.Throwable th) {
                this.mTmpUpdateConfigurationResult.mIsUpdating = false;
                continueWindowLayout();
                throw th;
            }
        }
        if (!deferResume) {
            kept = ensureConfigAndVisibilityAfterUpdate(starting, changes);
        }
        this.mTmpUpdateConfigurationResult.mIsUpdating = false;
        continueWindowLayout();
        this.mTmpUpdateConfigurationResult.activityRelaunched = kept ? false : true;
        return kept;
    }

    int updateGlobalConfigurationLocked(android.content.res.Configuration values, boolean initLocale, boolean persistent, int userId) {
        this.mTempConfig.setTo(getGlobalConfiguration());
        mActivityTaskManagerExt.clearCompactWindowModeWhenUpdateConfiguration(values, this.mTempConfig);
        int changes = this.mTempConfig.updateFrom(values);
        char c = 1;
        boolean pendingNight = (changes & 512) != 0;
        int pendingNightMode = this.mTempConfig.uiMode;
        mActivityTaskManagerExt.handleUiModeChanged(changes);
        mActivityTaskManagerExt.hookAtmsConfigurationChang(changes, this.mRootWindowContainer, this.mWindowManager, values);
        mActivityTaskManagerExt.clearCacheWhenOnConfigurationChange(this.mTempConfig, changes);
        if (changes == 0) {
            return 0;
        }
        if (pendingNight && this.mTempConfig.uiMode != pendingNightMode) {
            this.mTempConfig.uiMode = pendingNightMode;
        }
        android.os.Trace.traceBegin(32L, "updateGlobalConfiguration");
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(values);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, 2008996027621913637L, 0, null, protoLogParam0);
        }
        com.android.server.am.EventLogTags.writeConfigurationChanged(changes);
        com.android.internal.util.FrameworkStatsLog.write(66, values.colorMode, values.densityDpi, values.fontScale, values.hardKeyboardHidden, values.keyboard, values.keyboardHidden, values.mcc, values.mnc, values.navigation, values.navigationHidden, values.orientation, values.screenHeightDp, values.screenLayout, values.screenWidthDp, values.smallestScreenWidthDp, values.touchscreen, values.uiMode);
        if (android.os.Process.myUid() == 1000) {
            if (values.mcc != 0) {
                android.os.SystemProperties.set("debug.tracing.mcc", java.lang.Integer.toString(values.mcc));
            }
            if (values.mnc != 0) {
                android.os.SystemProperties.set("debug.tracing.mnc", java.lang.Integer.toString(values.mnc));
            }
        }
        if (!initLocale && !values.getLocales().isEmpty() && values.userSetLocale) {
            android.os.LocaleList locales = values.getLocales();
            int bestLocaleIndex = 0;
            if (locales.size() > 1) {
                if (this.mSupportedSystemLocales == null) {
                    this.mSupportedSystemLocales = android.content.res.Resources.getSystem().getAssets().getLocales();
                }
                bestLocaleIndex = java.lang.Math.max(0, locales.getFirstMatchIndex(this.mSupportedSystemLocales));
            }
            android.os.SystemProperties.set("persist.sys.locale", locales.get(bestLocaleIndex).toLanguageTag());
            android.os.LocaleList.setDefault(locales, bestLocaleIndex);
        }
        this.mTempConfig.seq = increaseConfigurationSeqLocked();
        android.util.Slog.i(TAG, "Config changes=" + java.lang.Integer.toHexString(changes) + " " + this.mTempConfig + " callers:" + android.os.Debug.getCallers(10));
        this.mUsageStatsInternal.reportConfigurationChange(this.mTempConfig, this.mAmInternal.getCurrentUserId());
        updateShouldShowDialogsLocked(this.mTempConfig);
        com.android.internal.policy.AttributeCache ac = com.android.internal.policy.AttributeCache.instance();
        if (ac != null) {
            ac.updateConfiguration(this.mTempConfig);
        }
        this.mSystemThread.applyConfigurationToResources(this.mTempConfig);
        if (persistent && android.provider.Settings.System.hasInterestingConfigurationChanges(changes)) {
            android.os.Message msg = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda14
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                    ((com.android.server.wm.ActivityTaskManagerService) obj).sendPutConfigurationForUserMsg(((java.lang.Integer) obj2).intValue(), (android.content.res.Configuration) obj3);
                }
            }, this, java.lang.Integer.valueOf(userId), new android.content.res.Configuration(this.mTempConfig));
            this.mH.sendMessage(msg);
        }
        java.lang.StringBuilder logStringBuilder = mActivityTaskManagerExt.beginLogProcessConfigurationWhenFolding(this.mTempConfig);
        android.util.SparseArray<com.android.server.wm.WindowProcessController> pidMap = this.mProcessMap.getPidMap();
        int i = pidMap.size() - 1;
        while (i >= 0) {
            int pid = pidMap.keyAt(i);
            com.android.server.wm.WindowProcessController app = pidMap.get(pid);
            if (app != null) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[c]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(app.mName);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mTempConfig);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -6404059840638143757L, 0, null, protoLogParam02, protoLogParam1);
                }
                mActivityTaskManagerExt.logProcessConfigurationWhenFolding(logStringBuilder, app.mName);
                app.onConfigurationChanged(this.mTempConfig);
            }
            i--;
            c = 1;
        }
        mActivityTaskManagerExt.endLogProcessConfigurationWhenFolding(TAG, logStringBuilder);
        mActivityTaskManagerExt.handleExtraConfigurationChanges(changes, this.mTempConfig, this.mContext, this.mUiHandler, getCurrentUserId());
        mActivityTaskManagerExt.onConfigurationChanged(this.mTempConfig);
        android.os.Message msg2 = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda15
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((android.app.ActivityManagerInternal) obj).broadcastGlobalConfigurationChanged(((java.lang.Integer) obj2).intValue(), ((java.lang.Boolean) obj3).booleanValue());
            }
        }, this.mAmInternal, java.lang.Integer.valueOf(changes), java.lang.Boolean.valueOf(initLocale));
        this.mH.sendMessage(msg2);
        mActivityTaskManagerExt.updataeAccidentPreventionState(this.mContext, false, this.mTempConfig.windowConfiguration.getRotation(), this.mRootWindowContainer.getConfiguration().windowConfiguration.getRotation());
        android.os.Trace.traceBegin(32L, "RootConfigChange");
        this.mRootWindowContainer.onConfigurationChanged(this.mTempConfig);
        android.os.Trace.traceEnd(32L);
        mActivityTaskManagerExt.updateGlobalConfigurationEnd(this.mTempConfig, userId, this.mContext, changes);
        android.os.Trace.traceEnd(32L);
        return changes;
    }

    private int increaseAssetConfigurationSeq() {
        int i = this.mGlobalAssetsSeq + 1;
        this.mGlobalAssetsSeq = i;
        this.mGlobalAssetsSeq = java.lang.Math.max(i, 1);
        return this.mGlobalAssetsSeq;
    }

    public void updateActivityApplicationInfo(int userId, android.util.ArrayMap<java.lang.String, android.content.pm.ApplicationInfo> applicationInfoByPackage) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mRootWindowContainer != null) {
                    this.mRootWindowContainer.updateActivityApplicationInfo(userId, applicationInfoByPackage);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void updateAssetConfiguration(java.util.List<com.android.server.wm.WindowProcessController> processes, boolean updateFrameworkRes) throws java.lang.Throwable {
        updateAssetConfigurationForSwitchUser(processes, updateFrameworkRes, false);
    }

    public void updateAssetConfigurationForSwitchUser(java.util.List<com.android.server.wm.WindowProcessController> processes, boolean updateFrameworkRes, boolean fromSwitchUser) throws java.lang.Throwable {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    int assetSeq = increaseAssetConfigurationSeq();
                    android.util.Slog.w(TAG, "updateAssetConfigurationForSwitchUser assetSeq = " + assetSeq + "; updateFrameworkRes = " + updateFrameworkRes + "; fromSwitchUser = " + fromSwitchUser + "; mMaterialColor = " + this.mMaterialColor + ";\n callers:" + android.os.Debug.getCallers(50));
                    android.content.res.Configuration configuration = getGlobalConfiguration();
                    oplus.content.res.OplusExtraConfiguration extraConfig = configuration.getOplusExtraConfiguration();
                    long globalColor = extraConfig.mMaterialColor;
                    long materialColor = extraConfig.mMaterialColor;
                    if (this.mMaterialColor != -1) {
                        materialColor = this.mMaterialColor;
                    }
                    if ((!fromSwitchUser || materialColor != globalColor) && updateFrameworkRes) {
                        android.content.res.Configuration newConfig = new android.content.res.Configuration();
                        if (!fromSwitchUser) {
                            newConfig.assetsSeq = assetSeq;
                        }
                        if (materialColor != globalColor) {
                            newConfig.getOplusExtraConfiguration().mMaterialColor = materialColor;
                        }
                        updateConfiguration(newConfig);
                    }
                    for (int i = processes.size() - 1; i >= 0; i--) {
                        com.android.server.wm.WindowProcessController wpc = processes.get(i);
                        if (fromSwitchUser && wpc.mInfo != null && wpc.mInfo.packageName != null && "com.android.launcher".equals(wpc.mInfo.packageName)) {
                            android.util.Slog.i(TAG, "launcher no relaunch in switchUser");
                        } else {
                            android.util.Slog.w(TAG, "updateAssetConfigurationForSwitchUser assetSeq = " + assetSeq + "; wpc = " + wpc);
                            wpc.updateAssetConfiguration(assetSeq);
                        }
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    th = th;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    void startPowerMode(int reason) {
        int prevReasons = this.mPowerModeReasons;
        this.mPowerModeReasons |= reason;
        if ((reason & 4) != 0) {
            if (this.mRetainPowerModeAndTopProcessState) {
                this.mH.removeMessages(3);
            }
            this.mRetainPowerModeAndTopProcessState = true;
            this.mH.sendEmptyMessageDelayed(3, 1000L);
            android.util.Slog.d(TAG, "Temporarily retain top process state for launching app");
        }
        if (this.mPowerManagerInternal == null) {
            return;
        }
        if ((reason & 1) != 0 && (prevReasons & 1) == 0) {
            android.os.Trace.instant(32L, "StartModeLaunch");
            this.mPowerManagerInternal.setPowerMode(5, true);
        } else if (reason == 2 && (prevReasons & 2) == 0) {
            android.os.Trace.instant(32L, "StartModeDisplayChange");
            this.mPowerManagerInternal.setPowerMode(17, true);
        }
    }

    void endPowerMode(int reason) {
        if (this.mPowerModeReasons == 0) {
            return;
        }
        int prevReasons = this.mPowerModeReasons;
        this.mPowerModeReasons &= ~reason;
        if ((this.mPowerModeReasons & 4) != 0) {
            boolean allResolved = true;
            for (int i = this.mRootWindowContainer.getChildCount() - 1; i >= 0; i--) {
                allResolved &= ((com.android.server.wm.DisplayContent) this.mRootWindowContainer.getChildAt(i)).mUnknownAppVisibilityController.allResolved();
            }
            if (allResolved) {
                this.mPowerModeReasons &= -5;
                this.mRetainPowerModeAndTopProcessState = false;
                this.mH.removeMessages(3);
            }
        }
        if (this.mPowerManagerInternal == null) {
            return;
        }
        if ((prevReasons & 5) != 0 && (this.mPowerModeReasons & 5) == 0) {
            android.os.Trace.instant(32L, "EndModeLaunch");
            this.mPowerManagerInternal.setPowerMode(5, false);
        }
        if ((prevReasons & 2) != 0 && (this.mPowerModeReasons & 2) == 0) {
            android.os.Trace.instant(32L, "EndModeDisplayChange");
            this.mPowerManagerInternal.setPowerMode(17, false);
        }
    }

    void deferWindowLayout() {
        if (!this.mWindowManager.mWindowPlacerLocked.isLayoutDeferred()) {
            this.mLayoutReasons = 0;
        }
        if (mActivityTaskManagerExt.logDiffer()) {
            mActivityTaskManagerExt.beforeDeferLayout(android.os.Debug.getCallers(15));
        }
        this.mWindowManager.mWindowPlacerLocked.deferLayout();
    }

    void continueWindowLayout() {
        this.mWindowManager.mWindowPlacerLocked.continueLayout(this.mLayoutReasons != 0);
        if (mActivityTaskManagerExt.logDiffer()) {
            mActivityTaskManagerExt.afterContinueLayout(android.os.Debug.getCallers(15));
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ALL && !this.mWindowManager.mWindowPlacerLocked.isLayoutDeferred()) {
            android.util.Slog.i(TAG, "continueWindowLayout reason=" + this.mLayoutReasons);
        }
        this.mLifecycleManager.onLayoutContinued();
    }

    void addWindowLayoutReasons(int reasons) {
        this.mLayoutReasons |= reasons;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEventDispatchingLocked(boolean booted) {
        this.mWindowManager.setEventDispatching(booted && !this.mShuttingDown);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendPutConfigurationForUserMsg(int userId, android.content.res.Configuration config) {
        android.content.ContentResolver resolver = this.mContext.getContentResolver();
        if (mActivityTaskManagerExt.hookAtmssendPutConfigurationForUserMsg(resolver, userId, config)) {
            return;
        }
        android.provider.Settings.System.putConfigurationForUser(resolver, config, userId);
    }

    boolean isActivityStartsLoggingEnabled() {
        return this.mAmInternal.isActivityStartsLoggingEnabled();
    }

    boolean isBackgroundActivityStartsEnabled() {
        return this.mAmInternal.isBackgroundActivityStartsEnabled();
    }

    static long getInputDispatchingTimeoutMillisLocked(com.android.server.wm.ActivityRecord r) {
        if (r == null || !r.hasProcess()) {
            return android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        }
        return getInputDispatchingTimeoutMillisLocked(r.app);
    }

    private static long getInputDispatchingTimeoutMillisLocked(com.android.server.wm.WindowProcessController r) {
        if (r == null) {
            return android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        }
        return r.getInputDispatchingTimeoutMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShouldShowDialogsLocked(android.content.res.Configuration config) {
        boolean z = false;
        boolean inputMethodExists = (config.keyboard == 1 && config.touchscreen == 1 && config.navigation == 1) ? false : true;
        boolean hideDialogsSet = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "hide_error_dialogs", 0) != 0;
        if (inputMethodExists && android.app.ActivityTaskManager.currentUiModeSupportsErrorDialogs(config) && !hideDialogsSet) {
            z = true;
        }
        this.mShowDialogs = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFontScaleIfNeeded(int userId) {
        if (userId != getCurrentUserId()) {
            return;
        }
        float scaleFactor = android.provider.Settings.System.getFloatForUser(this.mContext.getContentResolver(), "font_scale", 1.0f, userId);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (getGlobalConfiguration().fontScale == scaleFactor) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                android.content.res.Configuration configuration = this.mWindowManager.computeNewConfiguration(0);
                configuration.fontScale = scaleFactor;
                updatePersistentConfiguration(configuration, userId);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFontWeightAdjustmentIfNeeded(int userId) {
        if (userId != getCurrentUserId()) {
            return;
        }
        int fontWeightAdjustment = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "font_weight_adjustment", Integer.MAX_VALUE, userId);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (getGlobalConfiguration().fontWeightAdjustment == fontWeightAdjustment) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                android.content.res.Configuration configuration = this.mWindowManager.computeNewConfiguration(0);
                configuration.fontWeightAdjustment = fontWeightAdjustment;
                updatePersistentConfiguration(configuration, userId);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    boolean isSleepingOrShuttingDownLocked() {
        return isSleepingLocked() || this.mShuttingDown;
    }

    boolean isSleepingLocked() {
        return this.mSleeping;
    }

    void setLastResumedActivityUncheckLocked(com.android.server.wm.ActivityRecord r, java.lang.String reason) {
        android.service.voice.IVoiceInteractionSession session;
        com.android.server.wm.Task task = r.getTask();
        if (task.isActivityTypeStandard()) {
            if (this.mCurAppTimeTracker != r.appTimeTracker) {
                if (this.mCurAppTimeTracker != null) {
                    this.mCurAppTimeTracker.stop();
                    this.mH.obtainMessage(1, this.mCurAppTimeTracker).sendToTarget();
                    this.mRootWindowContainer.clearOtherAppTimeTrackers(r.appTimeTracker);
                    this.mCurAppTimeTracker = null;
                }
                if (r.appTimeTracker != null) {
                    this.mCurAppTimeTracker = r.appTimeTracker;
                    startTimeTrackingFocusedActivityLocked();
                }
            } else {
                startTimeTrackingFocusedActivityLocked();
            }
        } else {
            r.appTimeTracker = null;
        }
        if (task.voiceInteractor != null) {
            startRunningVoiceLocked(task.voiceSession, r.info.applicationInfo.uid);
        } else {
            finishRunningVoiceLocked();
            if (this.mLastResumedActivity != null) {
                com.android.server.wm.Task lastResumedActivityTask = this.mLastResumedActivity.getTask();
                if (lastResumedActivityTask != null && lastResumedActivityTask.voiceSession != null) {
                    session = lastResumedActivityTask.voiceSession;
                } else {
                    session = this.mLastResumedActivity.voiceSession;
                }
                if (session != null) {
                    finishVoiceTask(session);
                }
            }
        }
        if (this.mLastResumedActivity != null && r.mUserId != this.mLastResumedActivity.mUserId) {
            this.mAmInternal.sendForegroundProfileChanged(r.mUserId);
        }
        com.android.server.wm.Task task2 = this.mLastResumedActivity != null ? this.mLastResumedActivity.getTask() : null;
        com.android.server.wm.Task prevTask = task2;
        mActivityTaskManagerExt.setLastResumedActivity(r);
        updateResumedAppTrace(r);
        mActivityTaskManagerExt.pidChanged(task2, task, this.mLastResumedActivity, r, reason);
        this.mLastResumedActivity = r;
        boolean focusedAppChanged = false;
        if (!getTransitionController().isTransientCollect(r)) {
            focusedAppChanged = r.mDisplayContent.setFocusedApp(r);
            android.util.Slog.d(TAG, "NFW_setLastResumedActivityUncheckLocked:" + focusedAppChanged + " r:" + r + " currentFocus:" + r.mDisplayContent.mCurrentFocus);
            if (focusedAppChanged || r.mDisplayContent.mCurrentFocus == null) {
                this.mWindowManager.updateFocusedWindowLocked(0, true);
            }
            mActivityTaskManagerExt.sendApplicationFocusGain(this.mUiHandler, this.mContext, r.packageName);
        }
        if (task != prevTask) {
            this.mTaskSupervisor.mRecentTasks.add(task);
            mActivityTaskManagerExt.taskFocusChanged(prevTask, task, this.mLastResumedActivity, reason);
            mActivityTaskManagerExt.notifySysActivityHotLaunch(com.android.server.wm.ActivityTaskManagerService.class, r, task);
        }
        if (focusedAppChanged) {
            applyUpdateLockStateLocked(r);
        }
        if (this.mVrController.mVrService != null) {
            applyUpdateVrModeLocked(r);
        }
        com.android.server.wm.EventLogTags.writeWmSetResumedActivity(r.mUserId, r.shortComponentName, reason);
    }

    final class SleepTokenAcquirer {
        private final android.util.SparseArray<com.android.server.wm.RootWindowContainer.SleepToken> mSleepTokens = new android.util.SparseArray<>();
        private final java.lang.String mTag;

        SleepTokenAcquirer(java.lang.String tag) {
            this.mTag = tag;
        }

        void acquire(int displayId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (!this.mSleepTokens.contains(displayId)) {
                        this.mSleepTokens.append(displayId, com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.createSleepToken(this.mTag, displayId));
                        com.android.server.wm.ActivityTaskManagerService.this.updateSleepIfNeededLocked();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        void release(int displayId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.RootWindowContainer.SleepToken token = this.mSleepTokens.get(displayId);
                    if (token != null) {
                        com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.removeSleepToken(token);
                        this.mSleepTokens.remove(displayId);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    void updateSleepIfNeededLocked() {
        boolean shouldSleep = !this.mRootWindowContainer.hasAwakeDisplay();
        boolean wasSleeping = this.mSleeping;
        boolean updateOomAdj = false;
        if (!shouldSleep) {
            if (wasSleeping) {
                this.mSleeping = false;
                com.android.internal.util.FrameworkStatsLog.write(14, 2);
                startTimeTrackingFocusedActivityLocked();
                if (this.mTopApp != null) {
                    this.mTopApp.addToPendingTop();
                }
                this.mTopProcessState = 2;
                android.util.Slog.d(TAG, "Top Process State changed to PROCESS_STATE_TOP");
                this.mTaskSupervisor.comeOutOfSleepIfNeededLocked();
            }
            this.mRootWindowContainer.applySleepTokens(true);
            mActivityTaskManagerExt.applySleepTokens(wasSleeping);
            if (wasSleeping) {
                updateOomAdj = true;
            }
        } else if (!this.mSleeping && shouldSleep) {
            this.mSleeping = true;
            com.android.internal.util.FrameworkStatsLog.write(14, 1);
            if (this.mCurAppTimeTracker != null) {
                this.mCurAppTimeTracker.stop();
            }
            this.mTopProcessState = 12;
            android.util.Slog.d(TAG, "Top Process State changed to PROCESS_STATE_TOP_SLEEPING");
            this.mTaskSupervisor.goingToSleepLocked();
            updateResumedAppTrace(null);
            updateOomAdj = true;
        }
        mActivityTaskManagerExt.updateSleepTokens(wasSleeping, shouldSleep);
        if (updateOomAdj) {
            mActivityTaskManagerExt.updateOomAdjForSleep(this.mUpdateOomAdjRunnable);
        }
    }

    void updateOomAdj() {
        this.mH.removeCallbacks(this.mUpdateOomAdjRunnable);
        this.mH.post(this.mUpdateOomAdjRunnable);
    }

    void updateCpuStats() {
        com.android.server.wm.ActivityTaskManagerService.H h = this.mH;
        final android.app.ActivityManagerInternal activityManagerInternal = this.mAmInternal;
        java.util.Objects.requireNonNull(activityManagerInternal);
        h.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                activityManagerInternal.updateCpuStats();
            }
        });
    }

    void updateBatteryStats(com.android.server.wm.ActivityRecord component, boolean resumed) {
        if (component.app == null) {
            android.util.Slog.e(TAG, "updateBatteryStats failed as app is null, record = " + component);
        } else {
            android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda10
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                    ((android.app.ActivityManagerInternal) obj).updateBatteryStats((android.content.ComponentName) obj2, ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), ((java.lang.Boolean) obj5).booleanValue());
                }
            }, this.mAmInternal, component.mActivityComponent, java.lang.Integer.valueOf(component.app.mUid), java.lang.Integer.valueOf(component.mUserId), java.lang.Boolean.valueOf(resumed));
            this.mH.sendMessage(m);
        }
    }

    void updateTopApp(com.android.server.wm.ActivityRecord topResumedActivity) {
        com.android.server.wm.ActivityRecord top = topResumedActivity != null ? topResumedActivity : this.mRootWindowContainer.getTopResumedActivity();
        this.mTopApp = top != null ? top.app : null;
        if (this.mTopApp == this.mPreviousProcess) {
            this.mPreviousProcess = null;
        }
    }

    void updatePreviousProcess(com.android.server.wm.ActivityRecord stoppedActivity) {
        if (stoppedActivity.app != null && this.mTopApp != null && stoppedActivity.app != this.mTopApp && stoppedActivity.lastVisibleTime > this.mPreviousProcessVisibleTime && stoppedActivity.app != this.mHomeProcess) {
            this.mPreviousProcess = stoppedActivity.app;
            this.mPreviousProcessVisibleTime = stoppedActivity.lastVisibleTime;
            mActivityTaskManagerExt.setProcRaiseAdjList(this.mPreviousProcess.mOwner);
        }
    }

    void updateActivityUsageStats(com.android.server.wm.ActivityRecord activity, int event) {
        android.content.ComponentName taskRoot = null;
        int taskId = -1;
        com.android.server.wm.Task task = activity.getTask();
        if (task != null) {
            com.android.server.wm.ActivityRecord rootActivity = task.getRootActivity();
            if (rootActivity != null) {
                taskRoot = rootActivity.mActivityComponent;
            }
            taskId = task.mTaskId;
        }
        android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.HeptConsumer() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda12
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7) {
                ((android.app.ActivityManagerInternal) obj).updateActivityUsageStats((android.content.ComponentName) obj2, ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), (android.os.IBinder) obj5, (android.content.ComponentName) obj6, (android.app.assist.ActivityId) obj7);
            }
        }, this.mAmInternal, activity.mActivityComponent, java.lang.Integer.valueOf(activity.mUserId), java.lang.Integer.valueOf(event), activity.token, taskRoot, new android.app.assist.ActivityId(taskId, activity.shareableActivityToken));
        this.mH.sendMessage(m);
    }

    void startProcessAsync(com.android.server.wm.ActivityRecord activity, boolean knownToBeDead, boolean isTop, java.lang.String hostingType) {
        if (!this.mStartingProcessActivities.contains(activity)) {
            this.mStartingProcessActivities.add(activity);
            if (this.mStartingProcessActivities.size() > 1) {
                this.mStartingProcessActivities.sort(null);
            }
        } else if (this.mProcessNames.get(activity.processName, activity.info.applicationInfo.uid) != null) {
            return;
        }
        try {
            if (android.os.Trace.isTagEnabled(32L)) {
                android.os.Trace.traceBegin(32L, "dispatchingStartProcess:" + activity.processName);
            }
            android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.HeptConsumer() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda21
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7) {
                    ((android.app.ActivityManagerInternal) obj).startProcess((java.lang.String) obj2, (android.content.pm.ApplicationInfo) obj3, ((java.lang.Boolean) obj4).booleanValue(), ((java.lang.Boolean) obj5).booleanValue(), (java.lang.String) obj6, (android.content.ComponentName) obj7);
                }
            }, this.mAmInternal, activity.processName, activity.info.applicationInfo, java.lang.Boolean.valueOf(knownToBeDead), java.lang.Boolean.valueOf(isTop), hostingType, activity.intent.getComponent());
            if (isTop) {
                mActivityTaskManagerExt.setUxForStartProcessAsync();
                com.android.server.OplusAppStartPerfThread.getHandler().sendMessageAtFrontOfQueue(m);
            } else {
                this.mH.sendMessageAtFrontOfQueue(m);
            }
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    void setBooting(boolean booting) {
        this.mAmInternal.setBooting(booting);
    }

    boolean isBooting() {
        return this.mAmInternal.isBooting();
    }

    void setBooted(boolean booted) {
        this.mAmInternal.setBooted(booted);
    }

    boolean isBooted() {
        return this.mAmInternal.isBooted();
    }

    void postFinishBooting(final boolean finishBooting, final boolean enableScreen) {
        this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$postFinishBooting$14(finishBooting, enableScreen);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postFinishBooting$14(boolean finishBooting, boolean enableScreen) {
        if (finishBooting) {
            this.mAmInternal.finishBooting();
        }
        if (enableScreen) {
            this.mInternal.enableScreenAfterBoot(isBooted());
        }
    }

    void setHeavyWeightProcess(com.android.server.wm.ActivityRecord root) {
        this.mHeavyWeightProcess = root.app;
        android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda18
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                ((com.android.server.wm.ActivityTaskManagerService) obj).postHeavyWeightProcessNotification((com.android.server.wm.WindowProcessController) obj2, (android.content.Intent) obj3, ((java.lang.Integer) obj4).intValue());
            }
        }, this, root.app, root.intent, java.lang.Integer.valueOf(root.mUserId));
        this.mH.sendMessage(m);
    }

    void clearHeavyWeightProcessIfEquals(com.android.server.wm.WindowProcessController proc) {
        if (this.mHeavyWeightProcess == null || this.mHeavyWeightProcess != proc) {
            return;
        }
        this.mHeavyWeightProcess = null;
        android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda17
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.wm.ActivityTaskManagerService) obj).cancelHeavyWeightProcessNotification(((java.lang.Integer) obj2).intValue());
            }
        }, this, java.lang.Integer.valueOf(proc.mUserId));
        this.mH.sendMessage(m);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelHeavyWeightProcessNotification(int userId) {
        android.app.INotificationManager inm = android.app.NotificationManager.getService();
        if (inm == null) {
            return;
        }
        try {
            inm.cancelNotificationWithTag(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, (java.lang.String) null, 11, userId);
        } catch (android.os.RemoteException e) {
        } catch (java.lang.RuntimeException e2) {
            android.util.Slog.w(TAG, "Error canceling notification for service", e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v2, types: [android.os.RemoteException] */
    /* JADX WARN: Type inference failed for: r0v3 */
    public void postHeavyWeightProcessNotification(com.android.server.wm.WindowProcessController proc, android.content.Intent intent, int userId) {
        android.app.INotificationManager inm;
        java.lang.String e = TAG;
        if (proc == null || (inm = android.app.NotificationManager.getService()) == null) {
            return;
        }
        try {
            android.content.Context context = this.mContext.createPackageContext(proc.mInfo.packageName, 0);
            java.lang.String text = this.mContext.getString(android.R.string.harmful_app_warning_uninstall, context.getApplicationInfo().loadLabel(context.getPackageManager()));
            android.app.Notification notification = new android.app.Notification.Builder(context, com.android.internal.notification.SystemNotificationChannels.HEAVY_WEIGHT_APP).setSmallIcon(android.R.drawable.seekbar_thumb_pressed_to_unpressed_animation).setWhen(0L).setOngoing(true).setTicker(text).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color)).setContentTitle(text).setContentText(this.mContext.getText(android.R.string.hearing_aids_feature_name)).setContentIntent(android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF, null, new android.os.UserHandle(userId))).build();
            try {
                inm.enqueueNotificationWithTag(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, (java.lang.String) null, 11, notification, userId);
            } catch (android.os.RemoteException e2) {
                e = e2;
            } catch (java.lang.RuntimeException e3) {
                android.util.Slog.w(TAG, "Error showing notification for heavy-weight app", e3);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e4) {
            android.util.Slog.w(e, "Unable to create context for heavy notification", e4);
        }
    }

    android.content.IIntentSender getIntentSenderLocked(int type, java.lang.String packageName, java.lang.String featureId, int callingUid, int userId, android.os.IBinder token, java.lang.String resultWho, int requestCode, android.content.Intent[] intents, java.lang.String[] resolvedTypes, int flags, android.os.Bundle bOptions) throws java.lang.Throwable {
        com.android.server.wm.ActivityRecord activity;
        if (type == 3) {
            com.android.server.wm.ActivityRecord activity2 = com.android.server.wm.ActivityRecord.isInRootTaskLocked(token);
            if (activity2 == null) {
                android.util.Slog.w(TAG, "Failed createPendingResult: activity " + token + " not in any root task");
                return null;
            }
            if (!activity2.finishing) {
                activity = activity2;
            } else {
                android.util.Slog.w(TAG, "Failed createPendingResult: activity " + activity2 + " is finishing");
                return null;
            }
        } else {
            activity = null;
        }
        com.android.server.wm.ActivityRecord activity3 = activity;
        com.android.server.am.PendingIntentRecord rec = this.mPendingIntentController.getIntentSender(type, packageName, featureId, callingUid, userId, token, resultWho, requestCode, intents, resolvedTypes, flags, bOptions);
        boolean noCreate = (flags & 536870912) != 0;
        if (noCreate) {
            return rec;
        }
        if (type == 3) {
            if (activity3.pendingResults == null) {
                activity3.pendingResults = new java.util.HashSet<>();
            }
            activity3.pendingResults.add(rec.ref);
        }
        return rec;
    }

    private void startTimeTrackingFocusedActivityLocked() {
        com.android.server.wm.ActivityRecord resumedActivity = this.mRootWindowContainer.getTopResumedActivity();
        if (!this.mSleeping && this.mCurAppTimeTracker != null && resumedActivity != null) {
            this.mCurAppTimeTracker.start(resumedActivity.packageName);
        }
    }

    private void updateResumedAppTrace(com.android.server.wm.ActivityRecord resumed) {
        if (android.os.Trace.isTagEnabled(32L)) {
            if (this.mTracedResumedActivity != null) {
                android.os.Trace.asyncTraceForTrackEnd(32L, "Focused app", java.lang.System.identityHashCode(this.mTracedResumedActivity));
            }
            if (resumed != null) {
                android.os.Trace.asyncTraceForTrackBegin(32L, "Focused app", resumed.mActivityComponent.flattenToShortString(), java.lang.System.identityHashCode(resumed));
            }
        }
        this.mTracedResumedActivity = resumed;
    }

    boolean ensureConfigAndVisibilityAfterUpdate(com.android.server.wm.ActivityRecord starting, int changes) {
        com.android.server.wm.Task mainRootTask;
        if ((starting == null && this.mTaskSupervisor.isRootVisibilityUpdateDeferred()) || (mainRootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask()) == null) {
            return true;
        }
        if (changes != 0 && starting == null) {
            starting = mainRootTask.topRunningActivity();
        }
        if (starting == null) {
            return true;
        }
        boolean kept = starting.ensureActivityConfiguration();
        this.mRootWindowContainer.ensureActivitiesVisible(starting);
        mActivityTaskManagerExt.updateConfigForLauncherLocked(starting, changes);
        return kept;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleAppGcsLocked$15() {
        this.mAmInternal.scheduleAppGcs();
    }

    void scheduleAppGcsLocked() {
        this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleAppGcsLocked$15();
            }
        });
    }

    android.content.res.CompatibilityInfo compatibilityInfoForPackageLocked(android.content.pm.ApplicationInfo ai) {
        return this.mCompatModePackages.compatibilityInfoForPackageLocked(ai);
    }

    android.content.pm.IPackageManager getPackageManager() {
        return android.app.AppGlobals.getPackageManager();
    }

    android.content.pm.PackageManagerInternal getPackageManagerInternalLocked() {
        if (this.mPmInternal == null) {
            this.mPmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        }
        return this.mPmInternal;
    }

    android.content.ComponentName getSysUiServiceComponentLocked() {
        if (this.mSysUiServiceComponent == null) {
            android.content.pm.PackageManagerInternal pm = getPackageManagerInternalLocked();
            this.mSysUiServiceComponent = pm.getSystemUiServiceComponent();
        }
        return this.mSysUiServiceComponent;
    }

    com.android.server.policy.PermissionPolicyInternal getPermissionPolicyInternal() {
        if (this.mPermissionPolicyInternal == null) {
            this.mPermissionPolicyInternal = (com.android.server.policy.PermissionPolicyInternal) com.android.server.LocalServices.getService(com.android.server.policy.PermissionPolicyInternal.class);
        }
        return this.mPermissionPolicyInternal;
    }

    com.android.server.statusbar.StatusBarManagerInternal getStatusBarManagerInternal() {
        if (this.mStatusBarManagerInternal == null) {
            this.mStatusBarManagerInternal = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
        }
        return this.mStatusBarManagerInternal;
    }

    com.android.server.wallpaper.WallpaperManagerInternal getWallpaperManagerInternal() {
        if (this.mWallpaperManagerInternal == null) {
            this.mWallpaperManagerInternal = (com.android.server.wallpaper.WallpaperManagerInternal) com.android.server.LocalServices.getService(com.android.server.wallpaper.WallpaperManagerInternal.class);
        }
        return this.mWallpaperManagerInternal;
    }

    com.android.server.wm.AppWarnings getAppWarningsLocked() {
        return this.mAppWarnings;
    }

    android.content.Intent getHomeIntent() {
        android.content.Intent intent = new android.content.Intent(this.mTopAction, this.mTopData != null ? android.net.Uri.parse(this.mTopData) : null);
        intent.setComponent(this.mTopComponent);
        intent.addFlags(256);
        if (this.mFactoryTest != 1) {
            intent.addCategory("android.intent.category.HOME");
        }
        return intent;
    }

    android.content.Intent getSecondaryHomeIntent(java.lang.String preferredPackage) {
        android.content.Intent intent = new android.content.Intent(this.mTopAction, this.mTopData != null ? android.net.Uri.parse(this.mTopData) : null);
        boolean useSystemProvidedLauncher = this.mContext.getResources().getBoolean(android.R.bool.config_supportsSystemDecorsOnSecondaryDisplays);
        if (preferredPackage == null || useSystemProvidedLauncher) {
            java.lang.String secondaryHomePackage = this.mContext.getResources().getString(android.R.string.config_sensorUseStartedActivity_hwToggle);
            intent.setPackage(secondaryHomePackage);
        } else {
            intent.setPackage(preferredPackage);
        }
        intent.addFlags(256);
        if (this.mFactoryTest != 1) {
            intent.addCategory("android.intent.category.SECONDARY_HOME");
        }
        return intent;
    }

    android.content.pm.ApplicationInfo getAppInfoForUser(android.content.pm.ApplicationInfo info, int userId) {
        if (info == null) {
            return null;
        }
        android.content.pm.ApplicationInfo newInfo = new android.content.pm.ApplicationInfo(info);
        newInfo.initForUser(userId);
        return newInfo;
    }

    com.android.server.wm.WindowProcessController getProcessController(java.lang.String processName, int uid) {
        if (uid == 1000) {
            android.util.SparseArray<com.android.server.wm.WindowProcessController> procs = (android.util.SparseArray) this.mProcessNames.getMap().get(processName);
            if (procs == null) {
                return null;
            }
            int procCount = procs.size();
            for (int i = 0; i < procCount; i++) {
                int procUid = procs.keyAt(i);
                if (!android.os.UserHandle.isApp(procUid) && android.os.UserHandle.isSameUser(procUid, uid)) {
                    return procs.valueAt(i);
                }
            }
        }
        return (com.android.server.wm.WindowProcessController) this.mProcessNames.get(processName, uid);
    }

    com.android.server.wm.WindowProcessController getProcessController(android.app.IApplicationThread thread) {
        if (thread == null) {
            return null;
        }
        android.os.IBinder threadBinder = thread.asBinder();
        android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.wm.WindowProcessController>> pmap = this.mProcessNames.getMap();
        for (int i = pmap.size() - 1; i >= 0; i--) {
            android.util.SparseArray<com.android.server.wm.WindowProcessController> procs = pmap.valueAt(i);
            for (int j = procs.size() - 1; j >= 0; j--) {
                com.android.server.wm.WindowProcessController proc = procs.valueAt(j);
                if (proc.hasThread() && proc.getThread().asBinder() == threadBinder) {
                    return proc;
                }
            }
        }
        return null;
    }

    com.android.server.wm.WindowProcessController getProcessController(int pid, int uid) {
        com.android.server.wm.WindowProcessController proc = this.mProcessMap.getProcess(pid);
        if (proc == null || !android.os.UserHandle.isApp(uid) || proc.mUid != uid) {
            return null;
        }
        return proc;
    }

    java.lang.String getPackageNameIfUnique(int uid, int pid) {
        com.android.server.wm.WindowProcessController proc = this.mProcessMap.getProcess(pid);
        if (proc == null || proc.mUid != uid) {
            android.util.Slog.w(TAG, "callingPackage for (uid=" + uid + ", pid=" + pid + ") has no WPC");
            return null;
        }
        java.util.List<java.lang.String> realCallingPackages = proc.getPackageList();
        if (realCallingPackages.size() != 1) {
            android.util.Slog.w(TAG, "callingPackage for (uid=" + uid + ", pid=" + pid + ") is ambiguous: " + realCallingPackages);
            return null;
        }
        return realCallingPackages.get(0);
    }

    boolean hasActiveVisibleWindow(int uid) {
        if (this.mVisibleActivityProcessTracker.hasVisibleActivity(uid)) {
            return true;
        }
        return this.mActiveUids.hasNonAppVisibleWindow(uid);
    }

    boolean isDeviceOwner(int uid) {
        return uid >= 0 && this.mDeviceOwnerUid == uid;
    }

    void setDeviceOwnerUid(int uid) {
        this.mDeviceOwnerUid = uid;
    }

    boolean isAffiliatedProfileOwner(int uid) {
        return uid >= 0 && this.mProfileOwnerUids.contains(java.lang.Integer.valueOf(uid)) && android.app.admin.DeviceStateCache.getInstance().hasAffiliationWithDevice(android.os.UserHandle.getUserId(uid));
    }

    void setProfileOwnerUids(java.util.Set<java.lang.Integer> uids) {
        this.mProfileOwnerUids = uids;
    }

    void saveANRState(com.android.server.wm.ActivityRecord activity, java.lang.String reason) {
        java.io.StringWriter sw = new java.io.StringWriter();
        final java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(sw, false, 1024);
        pw.println("  ANR time: " + java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));
        if (reason != null) {
            pw.println("  Reason: " + reason);
        }
        pw.println();
        if (activity != null) {
            com.android.server.wm.Task rootTask = activity.getRootTask();
            if (rootTask != null) {
                rootTask.forAllTaskFragments(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda20
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wm.TaskFragment) obj).dumpInner("  ", pw, true, null);
                    }
                });
                pw.println();
            }
            this.mActivityStartController.dump(pw, "  ", activity.packageName);
            if (this.mActivityStartController.getLastStartActivity() != activity) {
                activity.dump(pw, "  ", true);
            }
        }
        com.android.server.wm.ActivityTaskSupervisor.printThisActivity(pw, this.mRootWindowContainer.getTopResumedActivity(), null, -1, true, "  ResumedActivity: ", null);
        this.mLockTaskController.dump(pw, "  ");
        this.mKeyguardController.dump(pw, "  ");
        pw.println("-------------------------------------------------------------------------------");
        pw.close();
        this.mLastANRState = sw.toString();
    }

    void logAppTooSlow(com.android.server.wm.WindowProcessController app, long startTime, java.lang.String msg) {
    }

    boolean isAssociatedCompanionApp(int userId, int uid) {
        java.util.Set<java.lang.Integer> allUids = this.mCompanionAppUidsMap.get(java.lang.Integer.valueOf(userId));
        if (allUids == null) {
            return false;
        }
        return allUids.contains(java.lang.Integer.valueOf(uid));
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        try {
            return super.onTransact(code, data, reply, flags);
        } catch (java.lang.RuntimeException e) {
            throw logAndRethrowRuntimeExceptionOnTransact(TAG, e);
        }
    }

    static java.lang.RuntimeException logAndRethrowRuntimeExceptionOnTransact(java.lang.String name, java.lang.RuntimeException e) {
        if (!(e instanceof java.lang.SecurityException)) {
            android.util.Slog.w(TAG, name + " onTransact aborts UID:" + android.os.Binder.getCallingUid() + " PID:" + android.os.Binder.getCallingPid(), e);
            throw e;
        }
        throw e;
    }

    public void setRunningRemoteTransitionDelegate(android.app.IApplicationThread delegate) {
        com.android.server.wm.TransitionController controller = getTransitionController();
        if (delegate != null && controller.mRemotePlayer.reportRunning(delegate)) {
            return;
        }
        this.mAmInternal.enforceCallingPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", "setRunningRemoteTransition");
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowProcessController callingProc = getProcessController(callingPid, callingUid);
                if (callingProc == null || !callingProc.isRunningRemoteTransition()) {
                    java.lang.String msg = "Can't call setRunningRemoteTransition from a process (pid=" + callingPid + " uid=" + callingUid + ") which isn't itself running a remote transition.";
                    android.util.Slog.e(TAG, msg);
                    throw new java.lang.SecurityException(msg);
                }
                com.android.server.wm.WindowProcessController wpc = getProcessController(delegate);
                if (wpc == null) {
                    android.util.Slog.w(TAG, "setRunningRemoteTransition: no process for " + delegate);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } else {
                    controller.mRemotePlayer.update(wpc, true, false);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void registerScreenCaptureObserver(android.os.IBinder activityToken, android.app.IScreenCaptureObserver observer) {
        this.mAmInternal.enforceCallingPermission("android.permission.DETECT_SCREEN_CAPTURE", "registerScreenCaptureObserver");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord activityRecord = com.android.server.wm.ActivityRecord.forTokenLocked(activityToken);
                if (activityRecord != null) {
                    activityRecord.registerCaptureObserver(observer);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void unregisterScreenCaptureObserver(android.os.IBinder activityToken, android.app.IScreenCaptureObserver observer) {
        this.mAmInternal.enforceCallingPermission("android.permission.DETECT_SCREEN_CAPTURE", "unregisterScreenCaptureObserver");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord activityRecord = com.android.server.wm.ActivityRecord.forTokenLocked(activityToken);
                if (activityRecord != null) {
                    activityRecord.unregisterCaptureObserver(observer);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void registerCompatScaleProvider(int id, com.android.server.wm.CompatScaleProvider provider) {
        this.mCompatModePackages.registerCompatScaleProvider(id, provider);
    }

    void unregisterCompatScaleProvider(int id) {
        this.mCompatModePackages.unregisterCompatScaleProvider(id);
    }

    boolean instrumentationSourceHasPermission(int pid, java.lang.String permission) {
        com.android.server.wm.WindowProcessController process;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                process = this.mProcessMap.getProcess(pid);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        if (process == null || !process.isInstrumenting()) {
            return false;
        }
        int sourceUid = process.getInstrumentationSourceUid();
        return checkPermission(permission, -1, sourceUid) == 0;
    }

    private com.android.server.wm.SafeActivityOptions createSafeActivityOptionsWithBalAllowed(android.app.ActivityOptions options) {
        if (options == null) {
            options = android.app.ActivityOptions.makeBasic().setPendingIntentBackgroundActivityStartMode(1);
        } else if (options.getPendingIntentBackgroundActivityStartMode() == 0) {
            options.setPendingIntentBackgroundActivityStartMode(1);
        }
        return new com.android.server.wm.SafeActivityOptions(options);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.wm.SafeActivityOptions createSafeActivityOptionsWithBalAllowed(android.os.Bundle bOptions) {
        return createSafeActivityOptionsWithBalAllowed(android.app.ActivityOptions.fromBundle(bOptions));
    }

    final class H extends android.os.Handler {
        static final int ADD_WAKEFULNESS_ANIMATING_REASON = 5;
        static final int END_POWER_MODE_UNKNOWN_VISIBILITY_MSG = 3;
        static final int FIRST_ACTIVITY_TASK_MSG = 100;
        static final int FIRST_SUPERVISOR_TASK_MSG = 200;
        static final int REMOVE_WAKEFULNESS_ANIMATING_REASON = 6;
        static final int REPORT_TIME_TRACKER_MSG = 1;
        static final int RESUME_FG_APP_SWITCH_MSG = 4;

        H(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.am.AppTimeTracker tracker = (com.android.server.am.AppTimeTracker) msg.obj;
                    tracker.deliverResult(com.android.server.wm.ActivityTaskManagerService.this.mContext);
                    return;
                case 2:
                default:
                    return;
                case 3:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            com.android.server.wm.ActivityTaskManagerService.this.mRetainPowerModeAndTopProcessState = false;
                            com.android.server.wm.ActivityTaskManagerService.this.endPowerMode(4);
                            if (com.android.server.wm.ActivityTaskManagerService.this.mTopApp != null && com.android.server.wm.ActivityTaskManagerService.this.mTopProcessState == 12) {
                                com.android.server.wm.ActivityTaskManagerService.this.mTopApp.updateProcessInfo(false, false, true, false);
                            }
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                case 4:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock2) {
                        try {
                            if (com.android.server.wm.ActivityTaskManagerService.this.mAppSwitchesState == 0) {
                                com.android.server.wm.ActivityTaskManagerService.this.mAppSwitchesState = 1;
                            }
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                case 5:
                    com.android.server.wm.WindowProcessController proc = (com.android.server.wm.WindowProcessController) msg.obj;
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock3 = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock3) {
                        try {
                            proc.addAnimatingReason(2);
                        } finally {
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                case 6:
                    com.android.server.wm.WindowProcessController proc2 = (com.android.server.wm.WindowProcessController) msg.obj;
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock4 = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock4) {
                        try {
                            proc2.removeAnimatingReason(2);
                        } finally {
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    android.os.Trace.instant(32L, "finishWakefulnessAnimating");
                    return;
            }
        }
    }

    final class UiHandler extends android.os.Handler {
        static final int DISMISS_DIALOG_UI_MSG = 1;

        public UiHandler() {
            super(com.android.server.UiThread.get().getLooper(), null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    android.app.Dialog d = (android.app.Dialog) msg.obj;
                    d.dismiss();
                    break;
            }
        }
    }

    final class LocalService extends com.android.server.wm.ActivityTaskManagerInternal {
        LocalService() {
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public android.content.ComponentName getHomeActivityForUser(int userId) {
            android.content.ComponentName componentName;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord homeActivity = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.getDefaultDisplayHomeActivityForUser(userId);
                    componentName = homeActivity == null ? null : homeActivity.mActivityComponent;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return componentName;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onLocalVoiceInteractionStarted(android.os.IBinder activity, android.service.voice.IVoiceInteractionSession voiceSession, com.android.internal.app.IVoiceInteractor voiceInteractor) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.onLocalVoiceInteractionStartedLocked(activity, voiceSession, voiceInteractor);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public java.util.List<com.android.server.wm.ActivityAssistInfo> getTopVisibleActivities() {
            java.util.List<com.android.server.wm.ActivityAssistInfo> topVisibleActivities;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    topVisibleActivities = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.getTopVisibleActivities();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return topVisibleActivities;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean hasResumedActivity(int uid) {
            return com.android.server.wm.ActivityTaskManagerService.this.mVisibleActivityProcessTracker.hasResumedActivity(uid);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void setBackgroundActivityStartCallback(com.android.server.wm.BackgroundActivityStartCallback backgroundActivityStartCallback) {
            com.android.server.wm.ActivityTaskManagerService.this.mBackgroundActivityStartCallback = backgroundActivityStartCallback;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void setAccessibilityServiceUids(android.util.IntArray uids) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mAccessibilityServiceUids = uids.toArray();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public int startActivitiesAsPackage(java.lang.String packageName, java.lang.String featureId, int userId, android.content.Intent[] intents, android.os.Bundle bOptions) throws java.lang.Throwable {
            int packageUid;
            java.util.Objects.requireNonNull(intents, "intents");
            java.lang.String[] resolvedTypes = new java.lang.String[intents.length];
            long ident = android.os.Binder.clearCallingIdentity();
            for (int i = 0; i < intents.length; i++) {
                try {
                    resolvedTypes[i] = intents[i].resolveTypeIfNeeded(com.android.server.wm.ActivityTaskManagerService.this.mContext.getContentResolver());
                } catch (android.os.RemoteException e) {
                } catch (java.lang.Throwable th) {
                    th = th;
                }
            }
            try {
                packageUid = android.app.AppGlobals.getPackageManager().getPackageUid(packageName, 268435456L, userId);
                android.os.Binder.restoreCallingIdentity(ident);
            } catch (android.os.RemoteException e2) {
                android.os.Binder.restoreCallingIdentity(ident);
                packageUid = 0;
            } catch (java.lang.Throwable th2) {
                th = th2;
                android.os.Binder.restoreCallingIdentity(ident);
                throw th;
            }
            if (com.android.server.wm.ActivityTaskManagerService.this.mAtmsWrapper.getExtImpl().startPairTaskIfNeed(intents, bOptions, userId)) {
                return 0;
            }
            com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.notifyStartActivityAsPackage(com.android.server.wm.SafeActivityOptions.fromBundle(bOptions), com.android.server.wm.ActivityTaskManagerService.this);
            return com.android.server.wm.ActivityTaskManagerService.this.getActivityStartController().startActivitiesInPackage(packageUid, packageName, featureId, intents, resolvedTypes, null, com.android.server.wm.SafeActivityOptions.fromBundle(bOptions), userId, false, null, android.app.BackgroundStartPrivileges.NONE);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public int startActivitiesInPackage(int uid, int realCallingPid, int realCallingUid, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent[] intents, java.lang.String[] resolvedTypes, android.os.IBinder resultTo, com.android.server.wm.SafeActivityOptions options, int userId, boolean validateIncomingUser, com.android.server.am.PendingIntentRecord originatingPendingIntent, android.app.BackgroundStartPrivileges forcedBalByPiSender) {
            com.android.server.wm.ActivityTaskManagerService.this.assertPackageMatchesCallingUid(callingPackage);
            return com.android.server.wm.ActivityTaskManagerService.this.getActivityStartController().startActivitiesInPackage(uid, realCallingPid, realCallingUid, callingPackage, callingFeatureId, intents, resolvedTypes, resultTo, options, userId, validateIncomingUser, originatingPendingIntent, forcedBalByPiSender);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public int startActivityInPackage(int uid, int realCallingPid, int realCallingUid, java.lang.String callingPackage, java.lang.String callingFeatureId, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, com.android.server.wm.SafeActivityOptions options, int userId, com.android.server.wm.Task inTask, java.lang.String reason, boolean validateIncomingUser, com.android.server.am.PendingIntentRecord originatingPendingIntent, android.app.BackgroundStartPrivileges forcedBalByPiSender) {
            com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.notifyStartActivityInPackage(options, realCallingUid, com.android.server.wm.ActivityTaskManagerService.this);
            com.android.server.wm.ActivityTaskManagerService.this.assertPackageMatchesCallingUid(callingPackage);
            return com.android.server.wm.ActivityTaskManagerService.this.getActivityStartController().startActivityInPackage(uid, realCallingPid, realCallingUid, callingPackage, callingFeatureId, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, options, userId, inTask, reason, validateIncomingUser, originatingPendingIntent, forcedBalByPiSender);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public int startActivityAsUser(android.app.IApplicationThread caller, java.lang.String callerPackage, java.lang.String callerFeatureId, android.content.Intent intent, android.os.IBinder resultTo, int startFlags, android.os.Bundle options, int userId) {
            return com.android.server.wm.ActivityTaskManagerService.this.startActivityAsUser(caller, callerPackage, callerFeatureId, intent, intent.resolveTypeIfNeeded(com.android.server.wm.ActivityTaskManagerService.this.mContext.getContentResolver()), resultTo, null, 0, startFlags, null, options, userId, false);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public int startActivityWithScreenshot(android.content.Intent intent, java.lang.String callingPackage, int callingUid, int callingPid, android.os.IBinder resultTo, android.os.Bundle options, int userId) {
            return com.android.server.wm.ActivityTaskManagerService.this.getActivityStartController().obtainStarter(intent, "startActivityWithScreenshot").setCallingUid(callingUid).setCallingPid(callingPid).setCallingPackage(callingPackage).setResultTo(resultTo).setActivityOptions(com.android.server.wm.ActivityTaskManagerService.this.createSafeActivityOptionsWithBalAllowed(options)).setRealCallingUid(android.os.Binder.getCallingUid()).setUserId(com.android.server.wm.ActivityTaskManagerService.this.getActivityStartController().checkTargetUser(userId, false, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), "startActivityWithScreenshot")).setBackgroundStartPrivileges(android.app.BackgroundStartPrivileges.ALLOW_BAL).setFreezeScreen(true).execute();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void setVr2dDisplayId(int vr2dDisplayId) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                long protoLogParam0 = vr2dDisplayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -1123414663662718691L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mVr2dDisplayId = vr2dDisplayId;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public int getDisplayId(android.os.IBinder token) {
            int displayId;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
                    if (r == null) {
                        throw new java.lang.IllegalArgumentException("getDisplayId: No activity record matching token=" + token);
                    }
                    displayId = r.getDisplayId();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return displayId;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void registerScreenObserver(com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver observer) {
            com.android.server.wm.ActivityTaskManagerService.this.mScreenObservers.add(observer);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void unregisterScreenObserver(com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver observer) {
            com.android.server.wm.ActivityTaskManagerService.this.mScreenObservers.remove(observer);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean isCallerRecents(int callingUid) {
            return com.android.server.wm.ActivityTaskManagerService.this.isCallerRecents(callingUid);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean isRecentsComponentHomeActivity(int userId) {
            return com.android.server.wm.ActivityTaskManagerService.this.getRecentTasks().isRecentsComponentHomeActivity(userId);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean checkCanCloseSystemDialogs(int pid, int uid, java.lang.String packageName) {
            return com.android.server.wm.ActivityTaskManagerService.this.checkCanCloseSystemDialogs(pid, uid, packageName);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean canCloseSystemDialogs(int pid, int uid) {
            return com.android.server.wm.ActivityTaskManagerService.this.canCloseSystemDialogs(pid, uid);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void notifyActiveVoiceInteractionServiceChanged(android.content.ComponentName component) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mActiveVoiceInteractionServiceComponent = component;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void notifyActiveDreamChanged(android.content.ComponentName dreamComponent) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mActiveDreamComponent = dreamComponent;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public android.app.IAppTask startDreamActivity(android.content.Intent intent, int callingUid, int callingPid) {
            return com.android.server.wm.ActivityTaskManagerService.this.startDreamActivityInternal(intent, callingUid, callingPid);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void setAllowAppSwitches(java.lang.String type, int uid, int userId) {
            if (!com.android.server.wm.ActivityTaskManagerService.this.mAmInternal.isUserRunning(userId, 1)) {
                return;
            }
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    android.util.ArrayMap<java.lang.String, java.lang.Integer> types = com.android.server.wm.ActivityTaskManagerService.this.mAllowAppSwitchUids.get(userId);
                    if (types == null) {
                        if (uid < 0) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        } else {
                            types = new android.util.ArrayMap<>();
                            com.android.server.wm.ActivityTaskManagerService.this.mAllowAppSwitchUids.put(userId, types);
                        }
                    }
                    if (uid < 0) {
                        types.remove(type);
                    } else {
                        types.put(type, java.lang.Integer.valueOf(uid));
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onUserStopped(int userId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.getRecentTasks().unloadUserDataFromMemoryLocked(userId);
                    com.android.server.wm.ActivityTaskManagerService.this.mAllowAppSwitchUids.remove(userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean isGetTasksAllowed(java.lang.String caller, int callingPid, int callingUid) {
            return com.android.server.wm.ActivityTaskManagerService.this.isGetTasksAllowed(caller, callingPid, callingUid);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onProcessAdded(com.android.server.wm.WindowProcessController proc) {
            synchronized (com.android.server.wm.ActivityTaskManagerService.this.mGlobalLockWithoutBoost) {
                com.android.server.wm.ActivityTaskManagerService.this.mProcessNames.put(proc.mName, proc.mUid, proc);
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onProcessRemoved(java.lang.String name, int uid) {
            synchronized (com.android.server.wm.ActivityTaskManagerService.this.mGlobalLockWithoutBoost) {
                com.android.server.wm.WindowProcessController proc = (com.android.server.wm.WindowProcessController) com.android.server.wm.ActivityTaskManagerService.this.mProcessNames.remove(name, uid);
                if (proc != null && !com.android.server.wm.ActivityTaskManagerService.this.mStartingProcessActivities.isEmpty()) {
                    for (int i = com.android.server.wm.ActivityTaskManagerService.this.mStartingProcessActivities.size() - 1; i >= 0; i--) {
                        try {
                            com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityTaskManagerService.this.mStartingProcessActivities.get(i);
                            if (r != null && uid == r.info.applicationInfo.uid && name.equals(r.processName) && (r.app != null || !r.isState(com.android.server.wm.ActivityRecord.State.INITIALIZING))) {
                                android.util.Slog.w(com.android.server.wm.ActivityTaskManagerService.TAG, proc + " is removed with pending start " + r);
                                com.android.server.wm.ActivityTaskManagerService.this.mStartingProcessActivities.remove(i);
                                if (r.isVisibleRequested()) {
                                    r.finishIfPossible("starting-proc-removed", false);
                                }
                            }
                        } catch (java.lang.IndexOutOfBoundsException e) {
                            android.util.Slog.e(com.android.server.wm.ActivityTaskManagerService.TAG, " Exception thrown  :" + e);
                        }
                    }
                }
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onCleanUpApplicationRecord(com.android.server.wm.WindowProcessController proc) {
            synchronized (com.android.server.wm.ActivityTaskManagerService.this.mGlobalLockWithoutBoost) {
                if (proc == com.android.server.wm.ActivityTaskManagerService.this.mHomeProcess) {
                    com.android.server.wm.ActivityTaskManagerService.this.mHomeProcess = null;
                }
                if (proc == com.android.server.wm.ActivityTaskManagerService.this.mPreviousProcess) {
                    com.android.server.wm.ActivityTaskManagerService.this.mPreviousProcess = null;
                }
                proc.getWrapper().getExtImpl().updateWaitActivityToAttach(false);
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public int getTopProcessState() {
            if (com.android.server.wm.ActivityTaskManagerService.this.mRetainPowerModeAndTopProcessState) {
                return 2;
            }
            return com.android.server.wm.ActivityTaskManagerService.this.mTopProcessState;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean useTopSchedGroupForTopProcess() {
            return com.android.server.wm.ActivityTaskManagerService.this.mDemoteTopAppReasons == 0;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void clearHeavyWeightProcessIfEquals(com.android.server.wm.WindowProcessController proc) {
            synchronized (com.android.server.wm.ActivityTaskManagerService.this.mGlobalLockWithoutBoost) {
                com.android.server.wm.ActivityTaskManagerService.this.clearHeavyWeightProcessIfEquals(proc);
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void finishHeavyWeightApp() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.ActivityTaskManagerService.this.mHeavyWeightProcess != null) {
                        com.android.server.wm.ActivityTaskManagerService.this.mHeavyWeightProcess.finishActivities();
                    }
                    com.android.server.wm.ActivityTaskManagerService.this.clearHeavyWeightProcessIfEquals(com.android.server.wm.ActivityTaskManagerService.this.mHeavyWeightProcess);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean isSleeping() {
            return com.android.server.wm.ActivityTaskManagerService.this.mSleeping;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean isShuttingDown() {
            return com.android.server.wm.ActivityTaskManagerService.this.mShuttingDown;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean shuttingDown(boolean booted, int timeout) {
            boolean zShutdownLocked;
            com.android.server.wm.ActivityTaskManagerService.this.mShuttingDown = true;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.prepareForShutdown();
                    com.android.server.wm.ActivityTaskManagerService.this.updateEventDispatchingLocked(booted);
                    com.android.server.wm.ActivityTaskManagerService.this.notifyTaskPersisterLocked(null, true);
                    zShutdownLocked = com.android.server.wm.ActivityTaskManagerService.this.mTaskSupervisor.shutdownLocked(timeout);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return zShutdownLocked;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void enableScreenAfterBoot(boolean booted) {
            com.android.server.am.EventLogTags.writeBootProgressEnableScreen(android.os.SystemClock.uptimeMillis());
            ((com.android.server.pm.IPackageManagerServiceUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceUtilsExt.class).create()).addBootEvent("AMS:ENABLE_SCREEN");
            com.android.server.wm.ActivityTaskManagerService.this.mWindowManager.enableScreenAfterBoot();
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.setBootstage();
                    com.android.server.wm.ActivityTaskManagerService.this.updateEventDispatchingLocked(booted);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean showStrictModeViolationDialog() {
            boolean z;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    z = (!com.android.server.wm.ActivityTaskManagerService.this.mShowDialogs || com.android.server.wm.ActivityTaskManagerService.this.mSleeping || com.android.server.wm.ActivityTaskManagerService.this.mShuttingDown) ? false : true;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return z;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void showSystemReadyErrorDialogsIfNeeded() {
            if (android.os.Trace.isTagEnabled(32L)) {
                android.os.Trace.traceBegin(32L, "showSystemReadyErrorDialogs");
            }
            boolean isBuildConsistent = android.os.Build.isBuildConsistent();
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                        if (android.app.AppGlobals.getPackageManager().hasSystemUidErrors()) {
                            android.util.Slog.e(com.android.server.wm.ActivityTaskManagerService.TAG, "UIDs on the system are inconsistent, you need to wipe your data partition or your device will be unstable.");
                            com.android.server.wm.ActivityTaskManagerService.this.mUiHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService$LocalService$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$showSystemReadyErrorDialogsIfNeeded$0();
                                }
                            });
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (android.os.RemoteException e) {
                }
                if (!isBuildConsistent) {
                    android.util.Slog.e(com.android.server.wm.ActivityTaskManagerService.TAG, "Build fingerprint is not consistent, warning user");
                    com.android.server.wm.ActivityTaskManagerService.this.mUiHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService$LocalService$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$showSystemReadyErrorDialogsIfNeeded$1();
                        }
                    });
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            android.os.Trace.traceEnd(32L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showSystemReadyErrorDialogsIfNeeded$0() {
            if (com.android.server.wm.ActivityTaskManagerService.this.mShowDialogs) {
                android.app.AlertDialog d = new com.android.server.am.BaseErrorDialog(com.android.server.wm.ActivityTaskManagerService.this.mUiContext);
                d.getWindow().setType(2010);
                d.setCancelable(false);
                d.setTitle(com.android.server.wm.ActivityTaskManagerService.this.mUiContext.getText(android.R.string.alert_windows_notification_channel_group_name));
                d.setMessage(com.android.server.wm.ActivityTaskManagerService.this.mUiContext.getText(android.R.string.status_bar_alarm_clock));
                d.setButton(-1, com.android.server.wm.ActivityTaskManagerService.this.mUiContext.getText(android.R.string.ok), com.android.server.wm.ActivityTaskManagerService.this.mUiHandler.obtainMessage(1, d));
                d.show();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showSystemReadyErrorDialogsIfNeeded$1() {
            if (com.android.server.wm.ActivityTaskManagerService.this.mShowDialogs) {
                android.app.AlertDialog d = new com.android.server.am.BaseErrorDialog(com.android.server.wm.ActivityTaskManagerService.this.mUiContext);
                d.getWindow().setType(2010);
                d.setCancelable(false);
                d.setTitle(com.android.server.wm.ActivityTaskManagerService.this.mUiContext.getText(android.R.string.alert_windows_notification_channel_group_name));
                d.setMessage(com.android.server.wm.ActivityTaskManagerService.this.mUiContext.getText(android.R.string.status_bar_airplane));
                d.setButton(-1, com.android.server.wm.ActivityTaskManagerService.this.mUiContext.getText(android.R.string.ok), com.android.server.wm.ActivityTaskManagerService.this.mUiHandler.obtainMessage(1, d));
                d.show();
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onProcessMapped(int pid, com.android.server.wm.WindowProcessController proc) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mProcessMap.put(pid, proc);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onProcessUnMapped(int pid) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.onProcessUnMapped(com.android.server.wm.ActivityTaskManagerService.this.mProcessMap.getProcess(pid));
                    com.android.server.wm.ActivityTaskManagerService.this.mProcessMap.remove(pid);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onPackageDataCleared(java.lang.String name, int userId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.clearSnapshotCacheForPackage(name);
                    com.android.server.wm.ActivityTaskManagerService.this.mCompatModePackages.handlePackageDataClearedLocked(name);
                    com.android.server.wm.ActivityTaskManagerService.this.mAppWarnings.onPackageDataCleared(name, userId);
                    com.android.server.wm.ActivityTaskManagerService.this.mPackageConfigPersister.onPackageDataCleared(name, userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onPackageUninstalled(java.lang.String name, int userId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.clearSnapshotCacheForPackage(name);
                    com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.onPackageUninstalled(name);
                    com.android.server.wm.ActivityTaskManagerService.this.mAppWarnings.onPackageUninstalled(name, userId);
                    com.android.server.wm.ActivityTaskManagerService.this.mCompatModePackages.handlePackageUninstalledLocked(name);
                    com.android.server.wm.ActivityTaskManagerService.this.mPackageConfigPersister.onPackageUninstall(name, userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onPackageAdded(java.lang.String name, boolean replacing) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mCompatModePackages.handlePackageAddedLocked(name, replacing);
                    com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.onPackageAdded(name);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onPackageReplaced(android.content.pm.ApplicationInfo aInfo) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.clearSnapshotCacheForPackage(aInfo.packageName);
                    com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.updateActivityApplicationInfo(aInfo);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public android.content.res.CompatibilityInfo compatibilityInfoForPackage(android.content.pm.ApplicationInfo ai) {
            android.content.res.CompatibilityInfo compatibilityInfoCompatibilityInfoForPackageLocked;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    compatibilityInfoCompatibilityInfoForPackageLocked = com.android.server.wm.ActivityTaskManagerService.this.compatibilityInfoForPackageLocked(ai);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return compatibilityInfoCompatibilityInfoForPackageLocked;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void sendActivityResult(int callingUid, android.os.IBinder activityToken, java.lang.String resultWho, int requestCode, int resultCode, android.content.Intent data) throws java.lang.Throwable {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                        com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(activityToken);
                        if (r == null || r.getRootTask() == null) {
                            return;
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        com.android.server.uri.NeededUriGrants dataGrants = com.android.server.wm.ActivityTaskManagerService.this.collectGrants(data, r);
                        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
                        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock2) {
                            try {
                                r.sendResult(callingUid, resultWho, requestCode, resultCode, data, new android.os.Binder(), dataGrants);
                            } finally {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            }
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    } catch (java.lang.Throwable th) {
                        th = th;
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    while (true) {
                        th = th2;
                    }
                }
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void clearPendingResultForActivity(android.os.IBinder activityToken, java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord> pir) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(activityToken);
                    if (r != null && r.pendingResults != null) {
                        r.pendingResults.remove(pir);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public android.content.ComponentName getActivityName(android.os.IBinder activityToken) {
            android.content.ComponentName component;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(activityToken);
                    component = r != null ? r.intent.getComponent() : null;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return component;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens getAttachedNonFinishingActivityForTask(int taskId, android.os.IBinder token) throws java.lang.Throwable {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                        com.android.server.wm.Task task = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.anyTaskForId(taskId, 0);
                        if (task == null) {
                            android.util.Slog.w(com.android.server.wm.ActivityTaskManagerService.TAG, "getApplicationThreadForTopActivity failed: Requested task not found");
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return null;
                        }
                        final java.util.List<com.android.server.wm.ActivityRecord> list = new java.util.ArrayList<>();
                        task.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityTaskManagerService$LocalService$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                com.android.server.wm.ActivityTaskManagerService.LocalService.lambda$getAttachedNonFinishingActivityForTask$2(list, (com.android.server.wm.ActivityRecord) obj);
                            }
                        });
                        if (list.size() <= 0) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return null;
                        }
                        if (token == null && list.get(0).attachedToProcess()) {
                            com.android.server.wm.ActivityRecord topRecord = list.get(0);
                            com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens activityTokens = new com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens(topRecord.token, topRecord.assistToken, topRecord.app.getThread(), topRecord.shareableActivityToken, topRecord.getUid());
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return activityTokens;
                        }
                        for (int i = 0; i < list.size(); i++) {
                            com.android.server.wm.ActivityRecord record = list.get(i);
                            if (record.shareableActivityToken == token && record.attachedToProcess()) {
                                com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens activityTokens2 = new com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens(record.token, record.assistToken, record.app.getThread(), record.shareableActivityToken, record.getUid());
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return activityTokens2;
                            }
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        static /* synthetic */ void lambda$getAttachedNonFinishingActivityForTask$2(java.util.List list, com.android.server.wm.ActivityRecord r) {
            if (!r.finishing) {
                list.add(r);
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public android.content.IIntentSender getIntentSender(int type, java.lang.String packageName, java.lang.String featureId, int callingUid, int userId, android.os.IBinder token, java.lang.String resultWho, int requestCode, android.content.Intent[] intents, java.lang.String[] resolvedTypes, int flags, android.os.Bundle bOptions) {
            android.content.IIntentSender intentSenderLocked;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    intentSenderLocked = com.android.server.wm.ActivityTaskManagerService.this.getIntentSenderLocked(type, packageName, featureId, callingUid, userId, token, resultWho, requestCode, intents, resolvedTypes, flags, bOptions);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return intentSenderLocked;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public com.android.server.wm.ActivityServiceConnectionsHolder getServiceConnectionsHolder(android.os.IBinder token) {
            com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forToken(token);
            if (r == null || !r.inHistory) {
                return null;
            }
            return r.getOrCreateServiceConnectionsHolder();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public android.content.Intent getHomeIntent() {
            android.content.Intent homeIntent;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    homeIntent = com.android.server.wm.ActivityTaskManagerService.this.getHomeIntent();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return homeIntent;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean startHomeActivity(int userId, java.lang.String reason) {
            boolean zStartHomeOnDisplay;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    zStartHomeOnDisplay = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.startHomeOnDisplay(userId, reason, 0);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return zStartHomeOnDisplay;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean startHomeOnDisplay(int userId, java.lang.String reason, int displayId, boolean allowInstrumenting, boolean fromHomeKey) {
            boolean zStartHomeOnDisplay;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    zStartHomeOnDisplay = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.startHomeOnDisplay(userId, reason, displayId, allowInstrumenting, fromHomeKey);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return zStartHomeOnDisplay;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean startHomeOnAllDisplays(int userId, java.lang.String reason) {
            boolean zStartHomeOnAllDisplays;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    zStartHomeOnAllDisplays = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.startHomeOnAllDisplays(userId, reason);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return zStartHomeOnAllDisplays;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void updateTopComponentForFactoryTest() {
            final java.lang.CharSequence errorMsg;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.ActivityTaskManagerService.this.mFactoryTest != 1) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    android.content.pm.ResolveInfo ri = com.android.server.wm.ActivityTaskManagerService.this.mContext.getPackageManager().resolveActivity(new android.content.Intent("android.intent.action.FACTORY_TEST"), 1024);
                    if (ri != null) {
                        android.content.pm.ActivityInfo ai = ri.activityInfo;
                        android.content.pm.ApplicationInfo app = ai.applicationInfo;
                        if ((1 & app.flags) != 0) {
                            com.android.server.wm.ActivityTaskManagerService.this.mTopAction = "android.intent.action.FACTORY_TEST";
                            com.android.server.wm.ActivityTaskManagerService.this.mTopData = null;
                            com.android.server.wm.ActivityTaskManagerService.this.mTopComponent = new android.content.ComponentName(app.packageName, ai.name);
                            errorMsg = null;
                        } else {
                            errorMsg = com.android.server.wm.ActivityTaskManagerService.this.mContext.getResources().getText(android.R.string.failed_to_copy_to_clipboard);
                        }
                    } else {
                        errorMsg = com.android.server.wm.ActivityTaskManagerService.this.mContext.getResources().getText(android.R.string.factorytest_reboot);
                    }
                    if (errorMsg == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.ActivityTaskManagerService.this.mTopAction = null;
                    com.android.server.wm.ActivityTaskManagerService.this.mTopData = null;
                    com.android.server.wm.ActivityTaskManagerService.this.mTopComponent = null;
                    com.android.server.wm.ActivityTaskManagerService.this.mUiHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskManagerService$LocalService$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$updateTopComponentForFactoryTest$3(errorMsg);
                        }
                    });
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateTopComponentForFactoryTest$3(java.lang.CharSequence errorMsg) {
            android.app.Dialog d = new com.android.server.wm.FactoryErrorDialog(com.android.server.wm.ActivityTaskManagerService.this.mUiContext, errorMsg);
            d.show();
            com.android.server.wm.ActivityTaskManagerService.this.mAmInternal.ensureBootCompleted();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void handleAppDied(com.android.server.wm.WindowProcessController wpc, boolean restarting, java.lang.Runnable finishInstrumentationCallback) {
            synchronized (com.android.server.wm.ActivityTaskManagerService.this.mGlobalLockWithoutBoost) {
                com.android.server.wm.ActivityTaskManagerService.this.mTaskSupervisor.beginDeferResume();
                try {
                    boolean hasVisibleActivities = wpc.handleAppDied();
                    com.android.server.wm.ActivityTaskManagerService.this.mTaskSupervisor.endDeferResume();
                    com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.hookRecordAppDiedCount(wpc.mUid, wpc.mInfo.packageName, wpc.mName);
                    if (com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.interceptHandleAppDied(wpc, restarting, hasVisibleActivities)) {
                        return;
                    }
                    if (!restarting && hasVisibleActivities) {
                        com.android.server.wm.ActivityTaskManagerService.this.deferWindowLayout();
                        try {
                            com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.ensureVisibilityOnVisibleActivityDiedOrCrashed("handleAppDied");
                            com.android.server.wm.ActivityTaskManagerService.this.continueWindowLayout();
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.ActivityTaskManagerService.this.continueWindowLayout();
                            throw th;
                        }
                    }
                    if (wpc.isInstrumenting()) {
                        finishInstrumentationCallback.run();
                    }
                } catch (java.lang.Throwable th2) {
                    com.android.server.wm.ActivityTaskManagerService.this.mTaskSupervisor.endDeferResume();
                    throw th2;
                }
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void closeSystemDialogs(java.lang.String reason) {
            com.android.server.wm.ActivityTaskManagerService.enforceNotIsolatedCaller("closeSystemDialogs");
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            if (!checkCanCloseSystemDialogs(pid, uid, null)) {
                return;
            }
            long origId = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    if (uid >= 10000) {
                        try {
                            com.android.server.wm.WindowProcessController proc = com.android.server.wm.ActivityTaskManagerService.this.mProcessMap.getProcess(pid);
                            if (!proc.isPerceptible()) {
                                android.util.Slog.w(com.android.server.wm.ActivityTaskManagerService.TAG, "Ignoring closeSystemDialogs " + reason + " from background process " + proc);
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.wm.ActivityTaskManagerService.this.mWindowManager.closeSystemDialogs(reason);
                    com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.closeSystemDialogActivities(reason);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    com.android.server.wm.ActivityTaskManagerService.this.mAmInternal.broadcastCloseSystemDialogs(reason);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(origId);
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void cleanupDisabledPackageComponents(java.lang.String packageName, java.util.Set<java.lang.String> disabledClasses, int userId, boolean booted) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.finishDisabledPackageActivities(packageName, disabledClasses, true, false, userId, false) && booted) {
                        com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.resumeFocusedTasksTopActivities();
                        com.android.server.wm.ActivityTaskManagerService.this.mTaskSupervisor.scheduleIdle();
                    }
                    com.android.server.wm.ActivityTaskManagerService.this.getRecentTasks().cleanupDisabledPackageTasksLocked(packageName, disabledClasses, userId);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean onForceStopPackage(java.lang.String packageName, boolean doit, boolean evenPersistent, int userId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    if (com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.interceptOnForceStopPackage(packageName, userId)) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    boolean zFinishDisabledPackageActivities = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.finishDisabledPackageActivities(packageName, null, doit, evenPersistent, userId, true);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return zFinishDisabledPackageActivities;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void resumeTopActivities(boolean scheduleIdle) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.resumeFocusedTasksTopActivities();
                    if (scheduleIdle) {
                        com.android.server.wm.ActivityTaskManagerService.this.mTaskSupervisor.scheduleIdle();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void preBindApplication(com.android.server.wm.WindowProcessController wpc) {
            synchronized (com.android.server.wm.ActivityTaskManagerService.this.mGlobalLockWithoutBoost) {
                com.android.server.wm.ActivityTaskManagerService.this.mTaskSupervisor.getActivityMetricsLogger().notifyBindApplication(wpc.mInfo);
                wpc.onConfigurationChanged(com.android.server.wm.ActivityTaskManagerService.this.getGlobalConfiguration());
                com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.onPreBindApplication(wpc);
                int i = com.android.server.wm.ActivityTaskManagerService.this.mStartingProcessActivities.size() - 1;
                while (true) {
                    if (i >= 0) {
                        com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityTaskManagerService.this.mStartingProcessActivities.get(i);
                        if (r == null || wpc.mUid != r.info.applicationInfo.uid || !wpc.mName.equals(r.processName)) {
                            i--;
                        } else {
                            wpc.registerActivityConfigurationListener(r);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void preBindApplication(com.android.server.wm.WindowProcessController wpc, android.content.res.Configuration outOverrideConfig, android.os.Bundle bundle) {
            synchronized (com.android.server.wm.ActivityTaskManagerService.this.mGlobalLockWithoutBoost) {
                com.android.server.wm.ActivityTaskManagerService.this.mTaskSupervisor.getActivityMetricsLogger().notifyBindApplication(wpc.mInfo);
                wpc.onConfigurationChanged(com.android.server.wm.ActivityTaskManagerService.this.getGlobalConfiguration());
                com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.onPreBindApplication(wpc);
                com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.preBindApplication(wpc, outOverrideConfig, bundle);
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean attachApplication(com.android.server.wm.WindowProcessController wpc) throws android.os.RemoteException {
            boolean zAttachApplication;
            synchronized (com.android.server.wm.ActivityTaskManagerService.this.mGlobalLockWithoutBoost) {
                if (android.os.Trace.isTagEnabled(32L)) {
                    android.os.Trace.traceBegin(32L, "attachApplication:" + wpc.mName);
                }
                try {
                    if (com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.isLogToolRun()) {
                        android.util.Slog.d(com.android.server.wm.ActivityTaskManagerService.TAG, "attachApplication:" + wpc.mName);
                    }
                    zAttachApplication = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.attachApplication(wpc);
                } finally {
                    android.os.Trace.traceEnd(32L);
                }
            }
            return zAttachApplication;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void notifyLockedProfile(int userId) {
            try {
                if (!android.app.AppGlobals.getPackageManager().isUidPrivileged(android.os.Binder.getCallingUid())) {
                    throw new java.lang.SecurityException("Only privileged app can call notifyLockedProfile");
                }
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        long ident = android.os.Binder.clearCallingIdentity();
                        try {
                            if (com.android.server.wm.ActivityTaskManagerService.this.mAmInternal.shouldConfirmCredentials(userId)) {
                                com.android.server.wm.ActivityTaskManagerService.this.maybeHideLockedProfileActivityLocked();
                                com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.lockAllProfileTasks(userId);
                            }
                        } finally {
                            android.os.Binder.restoreCallingIdentity(ident);
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (android.os.RemoteException ex) {
                throw new java.lang.SecurityException("Fail to check is caller a privileged app", ex);
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void startConfirmDeviceCredentialIntent(android.content.Intent intent, android.os.Bundle options) {
            com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("startConfirmDeviceCredentialIntent");
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    long ident = android.os.Binder.clearCallingIdentity();
                    try {
                        intent.addFlags(276824064);
                        android.app.ActivityOptions activityOptions = options != null ? new android.app.ActivityOptions(options) : android.app.ActivityOptions.makeBasic();
                        com.android.server.wm.ActivityTaskManagerService.this.mContext.startActivityAsUser(intent, activityOptions.toBundle(), android.os.UserHandle.CURRENT);
                    } finally {
                        android.os.Binder.restoreCallingIdentity(ident);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void writeActivitiesToProto(android.util.proto.ProtoOutputStream proto) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.dumpDebug(proto, 1146756268034L, 0);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void dump(java.lang.String cmd, java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, boolean dumpClient, java.lang.String dumpPackage, int displayIdFilter) throws java.lang.Throwable {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
                if (!com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_CMD.equals(cmd)) {
                    try {
                        if (!com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD.equals(cmd)) {
                            if (com.android.server.wm.ActivityTaskManagerService.DUMP_LASTANR_CMD.equals(cmd)) {
                                com.android.server.wm.ActivityTaskManagerService.this.dumpLastANRLocked(pw);
                            } else if (com.android.server.wm.ActivityTaskManagerService.DUMP_LASTANR_TRACES_CMD.equals(cmd)) {
                                com.android.server.wm.ActivityTaskManagerService.this.dumpLastANRTracesLocked(pw);
                            } else if (com.android.server.wm.ActivityTaskManagerService.DUMP_STARTER_CMD.equals(cmd)) {
                                com.android.server.wm.ActivityTaskManagerService.this.dumpActivityStarterLocked(pw, dumpPackage);
                            } else if (com.android.server.wm.ActivityTaskManagerService.DUMP_CONTAINERS_CMD.equals(cmd)) {
                                com.android.server.wm.ActivityTaskManagerService.this.dumpActivityContainersLocked(pw);
                            } else if (com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_CMD.equals(cmd) || com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD.equals(cmd)) {
                                try {
                                    if (com.android.server.wm.ActivityTaskManagerService.this.getRecentTasks() != null) {
                                        com.android.server.wm.ActivityTaskManagerService.this.getRecentTasks().dump(pw, dumpAll, dumpPackage);
                                    }
                                } catch (java.lang.Throwable th3) {
                                    th = th3;
                                }
                            } else if (com.android.server.wm.ActivityTaskManagerService.DUMP_TOP_RESUMED_ACTIVITY.equals(cmd)) {
                                com.android.server.wm.ActivityTaskManagerService.this.dumpTopResumedActivityLocked(pw);
                            } else if (com.android.server.wm.ActivityTaskManagerService.DUMP_VISIBLE_ACTIVITIES.equals(cmd)) {
                                com.android.server.wm.ActivityTaskManagerService.this.dumpVisibleActivitiesLocked(pw, displayIdFilter);
                            }
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
                com.android.server.wm.ActivityTaskManagerService.this.dumpActivitiesLocked(fd, pw, args, opti, dumpAll, dumpClient, dumpPackage, displayIdFilter);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:121:0x019b A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0055 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0063 A[Catch: all -> 0x037c, TryCatch #3 {all -> 0x037c, blocks: (B:16:0x0032, B:18:0x004f, B:21:0x0057, B:24:0x0063, B:25:0x0068, B:27:0x0084, B:30:0x008c, B:32:0x0096, B:33:0x00ae, B:36:0x00b6, B:39:0x00c2, B:40:0x00c7, B:42:0x00e3, B:44:0x010a, B:47:0x0114, B:48:0x012c, B:50:0x013a, B:51:0x014b, B:53:0x0151, B:55:0x0169, B:59:0x0172, B:60:0x0178, B:65:0x01d7), top: B:123:0x0032 }] */
        /* JADX WARN: Removed duplicated region for block: B:27:0x0084 A[Catch: all -> 0x037c, TryCatch #3 {all -> 0x037c, blocks: (B:16:0x0032, B:18:0x004f, B:21:0x0057, B:24:0x0063, B:25:0x0068, B:27:0x0084, B:30:0x008c, B:32:0x0096, B:33:0x00ae, B:36:0x00b6, B:39:0x00c2, B:40:0x00c7, B:42:0x00e3, B:44:0x010a, B:47:0x0114, B:48:0x012c, B:50:0x013a, B:51:0x014b, B:53:0x0151, B:55:0x0169, B:59:0x0172, B:60:0x0178, B:65:0x01d7), top: B:123:0x0032 }] */
        /* JADX WARN: Removed duplicated region for block: B:35:0x00b4 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00c2 A[Catch: all -> 0x037c, TryCatch #3 {all -> 0x037c, blocks: (B:16:0x0032, B:18:0x004f, B:21:0x0057, B:24:0x0063, B:25:0x0068, B:27:0x0084, B:30:0x008c, B:32:0x0096, B:33:0x00ae, B:36:0x00b6, B:39:0x00c2, B:40:0x00c7, B:42:0x00e3, B:44:0x010a, B:47:0x0114, B:48:0x012c, B:50:0x013a, B:51:0x014b, B:53:0x0151, B:55:0x0169, B:59:0x0172, B:60:0x0178, B:65:0x01d7), top: B:123:0x0032 }] */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00e3 A[Catch: all -> 0x037c, TryCatch #3 {all -> 0x037c, blocks: (B:16:0x0032, B:18:0x004f, B:21:0x0057, B:24:0x0063, B:25:0x0068, B:27:0x0084, B:30:0x008c, B:32:0x0096, B:33:0x00ae, B:36:0x00b6, B:39:0x00c2, B:40:0x00c7, B:42:0x00e3, B:44:0x010a, B:47:0x0114, B:48:0x012c, B:50:0x013a, B:51:0x014b, B:53:0x0151, B:55:0x0169, B:59:0x0172, B:60:0x0178, B:65:0x01d7), top: B:123:0x0032 }] */
        /* JADX WARN: Removed duplicated region for block: B:44:0x010a A[Catch: all -> 0x037c, TryCatch #3 {all -> 0x037c, blocks: (B:16:0x0032, B:18:0x004f, B:21:0x0057, B:24:0x0063, B:25:0x0068, B:27:0x0084, B:30:0x008c, B:32:0x0096, B:33:0x00ae, B:36:0x00b6, B:39:0x00c2, B:40:0x00c7, B:42:0x00e3, B:44:0x010a, B:47:0x0114, B:48:0x012c, B:50:0x013a, B:51:0x014b, B:53:0x0151, B:55:0x0169, B:59:0x0172, B:60:0x0178, B:65:0x01d7), top: B:123:0x0032 }] */
        /* JADX WARN: Removed duplicated region for block: B:71:0x026d  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x0275 A[Catch: all -> 0x0378, TryCatch #1 {all -> 0x0378, blocks: (B:68:0x0242, B:72:0x026f, B:74:0x0275, B:75:0x027f, B:78:0x028b, B:80:0x0295, B:81:0x02a0, B:84:0x02a8), top: B:119:0x0242 }] */
        /* JADX WARN: Removed duplicated region for block: B:77:0x0289  */
        /* JADX WARN: Removed duplicated region for block: B:97:0x0309  */
        /* JADX WARN: Removed duplicated region for block: B:99:0x030d A[Catch: all -> 0x038e, TryCatch #4 {all -> 0x038e, blocks: (B:94:0x02fe, B:90:0x02bf, B:92:0x02c5, B:93:0x02cb, B:112:0x0389, B:95:0x0301, B:99:0x030d, B:101:0x0313, B:102:0x033b, B:103:0x0373), top: B:125:0x02bf }] */
        @Override // com.android.server.wm.ActivityTaskManagerInternal
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean dumpForProcesses(java.io.FileDescriptor r14, java.io.PrintWriter r15, boolean r16, java.lang.String r17, int r18, boolean r19, boolean r20, int r21) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 912
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityTaskManagerService.LocalService.dumpForProcesses(java.io.FileDescriptor, java.io.PrintWriter, boolean, java.lang.String, int, boolean, boolean, int):boolean");
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void writeProcessesToProto(android.util.proto.ProtoOutputStream proto, java.lang.String dumpPackage, int wakeFullness, boolean testPssMode) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                if (dumpPackage == null) {
                    try {
                        com.android.server.wm.ActivityTaskManagerService.this.getGlobalConfiguration().dumpDebug(proto, 1146756268051L);
                        com.android.server.wm.Task topFocusedRootTask = com.android.server.wm.ActivityTaskManagerService.this.getTopDisplayFocusedRootTask();
                        if (topFocusedRootTask != null) {
                            proto.write(1133871366165L, topFocusedRootTask.mConfigWillChange);
                        }
                        com.android.server.wm.ActivityTaskManagerService.this.writeSleepStateToProto(proto, wakeFullness, testPssMode);
                        if (com.android.server.wm.ActivityTaskManagerService.this.mRunningVoice != null) {
                            long vrToken = proto.start(1146756268060L);
                            proto.write(1138166333441L, com.android.server.wm.ActivityTaskManagerService.this.mRunningVoice.toString());
                            com.android.server.wm.ActivityTaskManagerService.this.mVoiceWakeLock.dumpDebug(proto, 1146756268034L);
                            proto.end(vrToken);
                        }
                        com.android.server.wm.ActivityTaskManagerService.this.mVrController.dumpDebug(proto, 1146756268061L);
                        if (com.android.server.wm.ActivityTaskManagerService.this.mController != null) {
                            long token = proto.start(1146756268069L);
                            proto.write(1138166333441L, com.android.server.wm.ActivityTaskManagerService.this.mController.toString());
                            proto.write(1133871366146L, com.android.server.wm.ActivityTaskManagerService.this.mControllerIsAMonkey);
                            proto.end(token);
                        }
                        com.android.server.wm.ActivityTaskManagerService.this.mTaskSupervisor.mGoingToSleepWakeLock.dumpDebug(proto, 1146756268079L);
                        com.android.server.wm.ActivityTaskManagerService.this.mTaskSupervisor.mLaunchingActivityWakeLock.dumpDebug(proto, 1146756268080L);
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                if (com.android.server.wm.ActivityTaskManagerService.this.mHomeProcess != null && (dumpPackage == null || com.android.server.wm.ActivityTaskManagerService.this.mHomeProcess.containsPackage(dumpPackage))) {
                    com.android.server.wm.ActivityTaskManagerService.this.mHomeProcess.dumpDebug(proto, 1146756268047L);
                }
                if (com.android.server.wm.ActivityTaskManagerService.this.mPreviousProcess != null && (dumpPackage == null || com.android.server.wm.ActivityTaskManagerService.this.mPreviousProcess.containsPackage(dumpPackage))) {
                    com.android.server.wm.ActivityTaskManagerService.this.mPreviousProcess.dumpDebug(proto, 1146756268048L);
                    proto.write(1112396529681L, com.android.server.wm.ActivityTaskManagerService.this.mPreviousProcessVisibleTime);
                }
                if (com.android.server.wm.ActivityTaskManagerService.this.mHeavyWeightProcess != null && (dumpPackage == null || com.android.server.wm.ActivityTaskManagerService.this.mHeavyWeightProcess.containsPackage(dumpPackage))) {
                    com.android.server.wm.ActivityTaskManagerService.this.mHeavyWeightProcess.dumpDebug(proto, 1146756268050L);
                }
                for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : com.android.server.wm.ActivityTaskManagerService.this.mCompatModePackages.getPackages().entrySet()) {
                    java.lang.String pkg = entry.getKey();
                    int mode = entry.getValue().intValue();
                    if (dumpPackage == null || dumpPackage.equals(pkg)) {
                        long compatToken = proto.start(2246267895830L);
                        proto.write(1138166333441L, pkg);
                        proto.write(1120986464258L, mode);
                        proto.end(compatToken);
                    }
                }
                if (com.android.server.wm.ActivityTaskManagerService.this.mCurAppTimeTracker != null) {
                    com.android.server.wm.ActivityTaskManagerService.this.mCurAppTimeTracker.dumpDebug(proto, 1146756268063L, true);
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean dumpActivity(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String name, java.lang.String[] args, int opti, boolean dumpAll, boolean dumpVisibleRootTasksOnly, boolean dumpFocusedRootTaskOnly, int displayIdFilter, int userId) {
            return com.android.server.wm.ActivityTaskManagerService.this.dumpActivity(fd, pw, name, args, opti, dumpAll, dumpVisibleRootTasksOnly, dumpFocusedRootTaskOnly, displayIdFilter, userId, 5000L);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void dumpForOom(java.io.PrintWriter pw) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    pw.println("  mHomeProcess: " + com.android.server.wm.ActivityTaskManagerService.this.mHomeProcess);
                    pw.println("  mPreviousProcess: " + com.android.server.wm.ActivityTaskManagerService.this.mPreviousProcess);
                    if (com.android.server.wm.ActivityTaskManagerService.this.mHeavyWeightProcess != null) {
                        pw.println("  mHeavyWeightProcess: " + com.android.server.wm.ActivityTaskManagerService.this.mHeavyWeightProcess);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean canGcNow() {
            boolean z;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    z = isSleeping() || com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.allResumedActivitiesIdle();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return z;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public com.android.server.wm.WindowProcessController getTopApp() {
            return com.android.server.wm.ActivityTaskManagerService.this.mTopApp;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void scheduleDestroyAllActivities(java.lang.String reason) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.scheduleDestroyAllActivities(reason);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void removeUser(int userId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.removeUser(userId);
                    com.android.server.wm.ActivityTaskManagerService.this.mPackageConfigPersister.removeUser(userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean switchUser(int userId, com.android.server.am.UserState userState) {
            boolean zSwitchUser;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    zSwitchUser = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.switchUser(userId, userState);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return zSwitchUser;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onHandleAppCrash(com.android.server.wm.WindowProcessController wpc) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    wpc.handleAppCrash();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public int finishTopCrashedActivities(com.android.server.wm.WindowProcessController crashedApp, java.lang.String reason) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.deferWindowLayout();
                    try {
                        com.android.server.wm.Task finishedTask = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.finishTopCrashedActivities(crashedApp, reason);
                        if (finishedTask != null) {
                            com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.ensureVisibilityOnVisibleActivityDiedOrCrashed(reason);
                            int i = finishedTask.mTaskId;
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return i;
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return -1;
                    } finally {
                        com.android.server.wm.ActivityTaskManagerService.this.continueWindowLayout();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onUidActive(int uid, int procState) {
            com.android.server.wm.ActivityTaskManagerService.this.mActiveUids.onUidActive(uid, procState);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onUidInactive(int uid) {
            com.android.server.wm.ActivityTaskManagerService.this.mActiveUids.onUidInactive(uid);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onUidProcStateChanged(int uid, int procState) {
            com.android.server.wm.ActivityTaskManagerService.this.mActiveUids.onUidProcStateChanged(uid, procState);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean handleAppCrashInActivityController(java.lang.String processName, int pid, java.lang.String shortMsg, java.lang.String longMsg, long timeMillis, java.lang.String stackTrace, java.lang.Runnable killCrashingAppCallback) {
            boolean procRes;
            java.lang.Runnable targetRunnable = null;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.ActivityTaskManagerService.this.mController == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    try {
                        if (com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.ismOplusActivityControlerSchedulerexist()) {
                            procRes = com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.scheduleAppCrash(processName, pid, shortMsg, longMsg, timeMillis, stackTrace);
                        } else {
                            procRes = com.android.server.wm.ActivityTaskManagerService.this.mController.appCrashed(processName, pid, shortMsg, longMsg, timeMillis, stackTrace);
                        }
                        if (!procRes) {
                            targetRunnable = killCrashingAppCallback;
                        }
                    } catch (android.os.RemoteException e) {
                        com.android.server.wm.ActivityTaskManagerService.this.mController = null;
                        com.android.server.Watchdog.getInstance().setActivityController(null);
                        if (com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.ismOplusActivityControlerSchedulerexist()) {
                            com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.exitRunningScheduler();
                        }
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    if (targetRunnable == null) {
                        return false;
                    }
                    targetRunnable.run();
                    return true;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void removeRecentTasksByPackageName(java.lang.String packageName, int userId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mRecentTasks.removeTasksByPackageName(packageName, userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void cleanupRecentTasksForUser(int userId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mRecentTasks.cleanupLocked(userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void loadRecentTasksForUser(int userId) {
            com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.tryRemoveAllUserRecentTasksLocked();
            com.android.server.wm.ActivityTaskManagerService.this.mRecentTasks.loadRecentTasksIfNeeded(userId);
            com.android.server.wm.ActivityTaskManagerService.this.mPackageConfigPersister.loadUserPackages(userId);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void onPackagesSuspendedChanged(java.lang.String[] packages, boolean suspended, int userId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mRecentTasks.onPackagesSuspendedChanged(packages, suspended, userId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void flushRecentTasks() {
            com.android.server.wm.ActivityTaskManagerService.this.mRecentTasks.flush();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void clearLockedTasks(java.lang.String reason) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.getLockTaskController().clearLockedTasks(reason);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void updateUserConfiguration() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    android.content.res.Configuration configuration = new android.content.res.Configuration(com.android.server.wm.ActivityTaskManagerService.this.getGlobalConfiguration());
                    int currentUserId = com.android.server.wm.ActivityTaskManagerService.this.mAmInternal.getCurrentUserId();
                    android.provider.Settings.System.adjustConfigurationForUser(com.android.server.wm.ActivityTaskManagerService.this.mContext.getContentResolver(), configuration, currentUserId, android.provider.Settings.System.canWrite(com.android.server.wm.ActivityTaskManagerService.this.mContext));
                    com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.adjustConfigurationForUser(com.android.server.wm.ActivityTaskManagerService.this.mContext.getContentResolver(), configuration, currentUserId);
                    com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.updateExtraConfigurationForUser(com.android.server.wm.ActivityTaskManagerService.this.mContext, configuration, currentUserId);
                    com.android.server.wm.ActivityTaskManagerService.this.updateConfigurationLocked(configuration, null, false, false, currentUserId, false);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean canShowErrorDialogs() {
            boolean z;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    z = false;
                    if (com.android.server.wm.ActivityTaskManagerService.this.mShowDialogs && !com.android.server.wm.ActivityTaskManagerService.this.mSleeping && !com.android.server.wm.ActivityTaskManagerService.this.mShuttingDown && !com.android.server.wm.ActivityTaskManagerService.this.mKeyguardController.isKeyguardOrAodShowing(0) && !com.android.server.wm.ActivityTaskManagerService.this.hasUserRestriction("no_system_error_dialogs", com.android.server.wm.ActivityTaskManagerService.this.mAmInternal.getCurrentUserId()) && (!android.os.UserManager.isDeviceInDemoMode(com.android.server.wm.ActivityTaskManagerService.this.mContext) || !com.android.server.wm.ActivityTaskManagerService.this.mAmInternal.getCurrentUser().isDemo())) {
                        z = true;
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return z;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void setProfileApp(java.lang.String profileApp) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mProfileApp = profileApp;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void setProfileProc(com.android.server.wm.WindowProcessController wpc) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mProfileProc = wpc;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void setProfilerInfo(android.app.ProfilerInfo profilerInfo) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mProfilerInfo = profilerInfo;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public com.android.server.wm.ActivityMetricsLaunchObserverRegistry getLaunchObserverRegistry() {
            com.android.server.wm.ActivityMetricsLaunchObserverRegistry launchObserverRegistry;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    launchObserverRegistry = com.android.server.wm.ActivityTaskManagerService.this.mTaskSupervisor.getActivityMetricsLogger().getLaunchObserverRegistry();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return launchObserverRegistry;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public android.os.IBinder getUriPermissionOwnerForActivity(android.os.IBinder activityToken) {
            android.os.Binder externalToken;
            com.android.server.wm.ActivityTaskManagerService.enforceNotIsolatedCaller("getUriPermissionOwnerForActivity");
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.isInRootTaskLocked(activityToken);
                    externalToken = r == null ? null : r.getUriPermissionsLocked().getExternalToken();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return externalToken;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public android.window.TaskSnapshot getTaskSnapshotBlocking(int taskId, boolean isLowResolution) {
            return com.android.server.wm.ActivityTaskManagerService.this.getTaskSnapshot(taskId, isLowResolution);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean isUidForeground(int uid) {
            return com.android.server.wm.ActivityTaskManagerService.this.hasActiveVisibleWindow(uid);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void setDeviceOwnerUid(int uid) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.setDeviceOwnerUid(uid);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void setProfileOwnerUids(java.util.Set<java.lang.Integer> uids) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.setProfileOwnerUids(uids);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void setCompanionAppUids(int userId, java.util.Set<java.lang.Integer> companionAppUids) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.ActivityTaskManagerService.this.mCompanionAppUidsMap.put(java.lang.Integer.valueOf(userId), companionAppUids);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean isBaseOfLockedTask(java.lang.String packageName) {
            boolean zIsBaseOfLockedTask;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    zIsBaseOfLockedTask = com.android.server.wm.ActivityTaskManagerService.this.getLockTaskController().isBaseOfLockedTask(packageName);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return zIsBaseOfLockedTask;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater createPackageConfigurationUpdater() {
            return new com.android.server.wm.PackageConfigurationUpdaterImpl(android.os.Binder.getCallingPid(), com.android.server.wm.ActivityTaskManagerService.this);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater createPackageConfigurationUpdater(java.lang.String packageName, int userId) {
            return new com.android.server.wm.PackageConfigurationUpdaterImpl(packageName, userId, com.android.server.wm.ActivityTaskManagerService.this);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public com.android.server.wm.ActivityTaskManagerInternal.PackageConfig getApplicationConfig(java.lang.String packageName, int userId) {
            return com.android.server.wm.ActivityTaskManagerService.this.mPackageConfigPersister.findPackageConfiguration(packageName, userId);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean hasSystemAlertWindowPermission(int callingUid, int callingPid, java.lang.String callingPackage) {
            return com.android.server.wm.ActivityTaskManagerService.this.hasSystemAlertWindowPermission(callingUid, callingPid, callingPackage);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void registerActivityStartInterceptor(int id, com.android.server.wm.ActivityInterceptorCallback callback) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.ActivityTaskManagerService.this.mActivityInterceptorCallbacks.contains(id)) {
                        throw new java.lang.IllegalArgumentException("Duplicate id provided: " + id);
                    }
                    if (callback == null) {
                        throw new java.lang.IllegalArgumentException("The passed ActivityInterceptorCallback can not be null");
                    }
                    if (!com.android.server.wm.ActivityInterceptorCallback.isValidOrderId(id)) {
                        throw new java.lang.IllegalArgumentException("Provided id " + id + " is not in range of valid ids for system services [0,5] nor in range of valid ids for mainline module services [1000,1001]");
                    }
                    com.android.server.wm.ActivityTaskManagerService.this.mActivityInterceptorCallbacks.put(id, callback);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void unregisterActivityStartInterceptor(int id) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (!com.android.server.wm.ActivityTaskManagerService.this.mActivityInterceptorCallbacks.contains(id)) {
                        throw new java.lang.IllegalArgumentException("ActivityInterceptorCallback with id (" + id + ") is not registered");
                    }
                    com.android.server.wm.ActivityTaskManagerService.this.mActivityInterceptorCallbacks.remove(id);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public android.app.ActivityManager.RecentTaskInfo getMostRecentTaskFromBackground() {
            java.util.List<android.app.ActivityManager.RunningTaskInfo> runningTaskInfoList = com.android.server.wm.ActivityTaskManagerService.this.getTasks(1);
            if (runningTaskInfoList.size() > 0) {
                android.app.ActivityManager.RunningTaskInfo runningTaskInfo = runningTaskInfoList.get(0);
                java.util.List<android.app.ActivityManager.RecentTaskInfo> recentTaskInfoList = com.android.server.wm.ActivityTaskManagerService.this.getRecentTasks(2, 2, com.android.server.wm.ActivityTaskManagerService.this.mContext.getUserId()).getList();
                for (android.app.ActivityManager.RecentTaskInfo info : recentTaskInfoList) {
                    if (info.id != runningTaskInfo.id) {
                        return info;
                    }
                }
                return null;
            }
            android.util.Slog.i(com.android.server.wm.ActivityTaskManagerService.TAG, "No running task found!");
            return null;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public java.util.List<android.app.ActivityManager.AppTask> getAppTasks(java.lang.String pkgName, int uid) {
            java.util.ArrayList<android.app.ActivityManager.AppTask> tasks = new java.util.ArrayList<>();
            java.util.List<android.os.IBinder> appTasks = com.android.server.wm.ActivityTaskManagerService.this.getAppTasks(pkgName, uid);
            int numAppTasks = appTasks.size();
            for (int i = 0; i < numAppTasks; i++) {
                tasks.add(new android.app.ActivityManager.AppTask(android.app.IAppTask.Stub.asInterface(appTasks.get(i))));
            }
            return tasks;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public int getTaskToShowPermissionDialogOn(java.lang.String pkgName, int uid) {
            int taskToShowPermissionDialogOn;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    taskToShowPermissionDialogOn = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.getTaskToShowPermissionDialogOn(pkgName, uid);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return taskToShowPermissionDialogOn;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void restartTaskActivityProcessIfVisible(int taskId, final java.lang.String packageName) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.Task task = com.android.server.wm.ActivityTaskManagerService.this.mRootWindowContainer.anyTaskForId(taskId, 0);
                    if (task == null) {
                        android.util.Slog.w(com.android.server.wm.ActivityTaskManagerService.TAG, "Failed to restart Activity. No task found for id: " + taskId);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.ActivityRecord activity = task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityTaskManagerService$LocalService$$ExternalSyntheticLambda4
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.wm.ActivityTaskManagerService.LocalService.lambda$restartTaskActivityProcessIfVisible$4(packageName, (com.android.server.wm.ActivityRecord) obj);
                        }
                    });
                    if (activity == null) {
                        android.util.Slog.w(com.android.server.wm.ActivityTaskManagerService.TAG, "Failed to restart Activity. No Activity found for package name: " + packageName + " in task: " + taskId);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    } else {
                        activity.restartProcessIfVisible();
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        static /* synthetic */ boolean lambda$restartTaskActivityProcessIfVisible$4(java.lang.String packageName, com.android.server.wm.ActivityRecord activityRecord) {
            return packageName.equals(activityRecord.packageName) && !activityRecord.finishing;
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void registerTaskStackListener(android.app.ITaskStackListener listener) {
            com.android.server.wm.ActivityTaskManagerService.this.registerTaskStackListener(listener);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void unregisterTaskStackListener(android.app.ITaskStackListener listener) {
            com.android.server.wm.ActivityTaskManagerService.this.unregisterTaskStackListener(listener);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void registerCompatScaleProvider(int id, com.android.server.wm.CompatScaleProvider provider) {
            com.android.server.wm.ActivityTaskManagerService.this.registerCompatScaleProvider(id, provider);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public void unregisterCompatScaleProvider(int id) {
            com.android.server.wm.ActivityTaskManagerService.this.unregisterCompatScaleProvider(id);
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal
        public boolean isAssistDataAllowed() {
            return com.android.server.wm.ActivityTaskManagerService.this.isAssistDataAllowed();
        }
    }

    static boolean isPip2ExperimentEnabled() {
        if (sIsPip2ExperimentEnabled == null) {
            android.content.pm.FeatureInfo arcFeature = com.android.server.SystemConfig.getInstance().getAvailableFeatures().get("org.chromium.arc");
            android.content.pm.FeatureInfo tvFeature = com.android.server.SystemConfig.getInstance().getAvailableFeatures().get("android.software.leanback");
            boolean z = true;
            boolean isArc = arcFeature != null && arcFeature.version >= 0;
            boolean isTv = tvFeature != null && tvFeature.version >= 0;
            if (!android.os.SystemProperties.getBoolean("persist.wm_shell.pip2", false) && (!com.android.wm.shell.Flags.enablePip2Implementation() || isArc || isTv)) {
                z = false;
            }
            sIsPip2ExperimentEnabled = java.lang.Boolean.valueOf(z);
        }
        return sIsPip2ExperimentEnabled.booleanValue();
    }

    public com.android.server.wm.IActivityTaskManagerServiceWrapper getWrapper() {
        return this.mAtmsWrapper;
    }

    private class ActivityTaskManagerServiceWrapper implements com.android.server.wm.IActivityTaskManagerServiceWrapper {
        private ActivityTaskManagerServiceWrapper() {
        }

        @Override // com.android.server.wm.IActivityTaskManagerServiceWrapper
        public boolean canShowDialogs() {
            return com.android.server.wm.ActivityTaskManagerService.this.mShowDialogs;
        }

        @Override // com.android.server.wm.IActivityTaskManagerServiceWrapper
        public com.android.server.wm.WindowProcessController getHomeProcess() {
            return com.android.server.wm.ActivityTaskManagerService.this.mHomeProcess;
        }

        @Override // com.android.server.wm.IActivityTaskManagerServiceWrapper
        public com.android.server.wm.IActivityTaskManagerServiceExt getExtImpl() {
            return com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt;
        }

        @Override // com.android.server.wm.IActivityTaskManagerServiceWrapper
        public com.android.server.wm.IFlexibleWindowManagerExt getFlexibleExtImpl() {
            return com.android.server.wm.ActivityTaskManagerService.this.mFlexibleWindowManagerExt;
        }

        @Override // com.android.server.wm.IActivityTaskManagerServiceWrapper
        public boolean isIOPreloadPkg(java.lang.String pkgName, int userId) {
            return com.android.server.wm.ActivityTaskManagerService.mActivityTaskManagerExt.isIOPreloadPkg(pkgName, userId);
        }
    }
}
