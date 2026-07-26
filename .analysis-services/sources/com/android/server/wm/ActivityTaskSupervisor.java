package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityTaskSupervisor implements com.android.server.wm.RecentTasks.Callbacks {
    private static final int ACTIVITY_RESTRICTION_APPOP = 2;
    private static final int ACTIVITY_RESTRICTION_NONE = 0;
    private static final int ACTIVITY_RESTRICTION_PERMISSION = 1;
    static final boolean DEFER_RESUME = true;
    private static final int IDLE_NOW_MSG = 201;
    private static final int IDLE_TIMEOUT_MSG = 200;
    private static final int KILL_TASK_PROCESSES_TIMEOUT_MS = 1000;
    private static final int KILL_TASK_PROCESSES_TIMEOUT_MSG = 206;
    private static final int LAUNCH_TASK_BEHIND_COMPLETE = 212;
    private static final int LAUNCH_TIMEOUT_MSG = 204;
    private static final int MAX_TASK_IDS_PER_USER = 100000;
    static final boolean ON_TOP = true;
    static final boolean PRESERVE_WINDOWS = true;
    private static final int PROCESS_STOPPING_AND_FINISHING_MSG = 205;
    static final boolean REMOVE_FROM_RECENTS = true;
    private static final int REPORT_MULTI_WINDOW_MODE_CHANGED_MSG = 214;
    private static final int REPORT_PIP_MODE_CHANGED_MSG = 215;
    private static final int RESTART_ACTIVITY_PROCESS_TIMEOUT_MSG = 213;
    private static final int RESUME_TOP_ACTIVITY_MSG = 202;
    private static final int SCHEDULE_FINISHING_STOPPING_ACTIVITY_MS = 200;
    private static final int SLEEP_TIMEOUT_MSG = 203;
    private static final int START_HOME_MSG = 216;
    private static final int TOP_RESUMED_STATE_LOSS_TIMEOUT = 500;
    private static final int TOP_RESUMED_STATE_LOSS_TIMEOUT_MSG = 217;
    private static final boolean VALIDATE_WAKE_LOCK_CALLER = false;
    private com.android.server.wm.ActivityMetricsLogger mActivityMetricsLogger;
    private android.app.AppOpsManager mAppOpsManager;
    boolean mAppVisibilitiesChangedSinceLastPause;
    com.android.server.wm.BackgroundActivityStartController mBalController;
    private int mDeferResumeCount;
    private boolean mDeferRootVisibilityUpdate;
    android.os.PowerManager.WakeLock mGoingToSleepWakeLock;
    private final com.android.server.wm.ActivityTaskSupervisor.ActivityTaskSupervisorHandler mHandler;
    private boolean mInitialized;
    private com.android.server.wm.KeyguardController mKeyguardController;
    private com.android.server.wm.LaunchParamsController mLaunchParamsController;
    com.android.server.wm.LaunchParamsPersister mLaunchParamsPersister;
    android.os.PowerManager.WakeLock mLaunchingActivityWakeLock;
    final android.os.Looper mLooper;
    com.android.server.wm.PersisterQueue mPersisterQueue;
    private android.graphics.Rect mPipModeChangedTargetRootTaskBounds;
    private android.os.PowerManager mPowerManager;
    com.android.server.wm.RecentTasks mRecentTasks;
    public com.android.server.wm.RootWindowContainer mRootWindowContainer;
    private com.android.server.wm.RunningTasks mRunningTasks;
    final com.android.server.wm.ActivityTaskManagerService mService;
    private android.content.ComponentName mSystemChooserActivity;
    private com.android.server.wm.ActivityRecord mTopResumedActivity;
    private boolean mTopResumedActivityWaitingForPrev;
    private android.companion.virtual.VirtualDeviceManager mVirtualDeviceManager;
    private int mVisibilityTransactionDepth;
    private com.android.server.wm.WindowManagerService mWindowManager;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static final java.lang.String TAG_IDLE = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_IDLE;
    private static final java.lang.String TAG_PAUSE = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_PAUSE;
    private static final java.lang.String TAG_RECENTS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_RECENTS;
    private static final java.lang.String TAG_ROOT_TASK = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_ROOT_TASK;
    private static final java.lang.String TAG_SWITCH = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_SWITCH;
    private static final boolean PANIC_DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    static final java.lang.String TAG_TASKS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_TASKS;
    public static boolean mPerfSendTapHint = false;
    public static boolean mIsPerfBoostAcquired = false;
    public static int mPerfHandle = -1;
    private static final int IDLE_TIMEOUT = android.os.Build.HW_TIMEOUT_MULTIPLIER * 10000;
    private static final int SLEEP_TIMEOUT = android.os.Build.HW_TIMEOUT_MULTIPLIER * 5000;
    private static final int LAUNCH_TIMEOUT = android.os.Build.HW_TIMEOUT_MULTIPLIER * 10000;
    private static final android.util.ArrayMap<java.lang.String, java.lang.String> ACTION_TO_RUNTIME_PERMISSION = new android.util.ArrayMap<>();
    final com.android.server.wm.ActivityTaskSupervisor.TaskInfoHelper mTaskInfoHelper = new com.android.server.wm.ActivityTaskSupervisor.TaskInfoHelper();
    final com.android.server.wm.ActivityTaskSupervisor.OpaqueActivityHelper mOpaqueActivityHelper = new com.android.server.wm.ActivityTaskSupervisor.OpaqueActivityHelper();
    private final java.util.ArrayList<com.android.server.wm.WindowProcessController> mActivityStateChangedProcs = new java.util.ArrayList<>();
    private final android.util.SparseIntArray mCurTaskIdForUser = new android.util.SparseIntArray(20);
    private final java.util.ArrayList<com.android.server.wm.ActivityTaskSupervisor.WaitInfo> mWaitingActivityLaunched = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.wm.ActivityRecord> mStoppingActivities = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.wm.ActivityRecord> mFinishingActivities = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.wm.ActivityRecord> mNoHistoryActivities = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.ActivityRecord> mMultiWindowModeChangedActivities = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.ActivityRecord> mPipModeChangedActivities = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.wm.ActivityRecord> mNoAnimActivities = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.am.UserState> mStartingUsers = new java.util.ArrayList<>();
    boolean mUserLeaving = false;
    private com.android.server.wm.ActivityTaskSupervisor.ActivityTaskSupervisorWrapper mATSWrapper = new com.android.server.wm.ActivityTaskSupervisor.ActivityTaskSupervisorWrapper();
    private com.android.server.wm.IActivityTaskSupervisorExt mActivityTaskSupervisorExt = (com.android.server.wm.IActivityTaskSupervisorExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityTaskSupervisorExt.class).base(this).create();
    private com.android.server.wm.IActivityTaskSupervisorSocExt mSocExt = (com.android.server.wm.IActivityTaskSupervisorSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityTaskSupervisorSocExt.class).base(this).create();

    static {
        ACTION_TO_RUNTIME_PERMISSION.put("android.media.action.IMAGE_CAPTURE", "android.permission.CAMERA");
        ACTION_TO_RUNTIME_PERMISSION.put("android.media.action.VIDEO_CAPTURE", "android.permission.CAMERA");
        ACTION_TO_RUNTIME_PERMISSION.put("android.intent.action.CALL", "android.permission.CALL_PHONE");
    }

    boolean canPlaceEntityOnDisplay(int displayId, int callingPid, int callingUid, android.content.pm.ActivityInfo activityInfo) {
        return canPlaceEntityOnDisplay(displayId, callingPid, callingUid, null, activityInfo);
    }

    boolean canPlaceEntityOnDisplay(int displayId, int callingPid, int callingUid, com.android.server.wm.Task task) {
        return canPlaceEntityOnDisplay(displayId, callingPid, callingUid, task, null);
    }

    private boolean canPlaceEntityOnDisplay(int displayId, int callingPid, int callingUid, com.android.server.wm.Task task, android.content.pm.ActivityInfo activityInfo) {
        if (displayId == 0) {
            return true;
        }
        if (!this.mService.mSupportsMultiDisplay || !isCallerAllowedToLaunchOnDisplay(callingPid, callingUid, displayId, activityInfo)) {
            return false;
        }
        com.android.server.wm.DisplayContent displayContent = this.mRootWindowContainer.getDisplayContentOrCreate(displayId);
        if (displayContent == null) {
            return true;
        }
        final java.util.ArrayList<android.content.pm.ActivityInfo> activities = new java.util.ArrayList<>();
        if (activityInfo != null) {
            activities.add(activityInfo);
        }
        if (task != null) {
            task.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    activities.add(((com.android.server.wm.ActivityRecord) obj).info);
                }
            });
        }
        return displayContent.mDwpcHelper.canContainActivities(activities, displayContent.getWindowingMode());
    }

    public ActivityTaskSupervisor(com.android.server.wm.ActivityTaskManagerService service, android.os.Looper looper) {
        this.mService = service;
        this.mLooper = looper;
        this.mHandler = new com.android.server.wm.ActivityTaskSupervisor.ActivityTaskSupervisorHandler(looper);
    }

    public void initialize() {
        if (this.mInitialized) {
            return;
        }
        this.mInitialized = true;
        setRunningTasks(new com.android.server.wm.RunningTasks());
        this.mActivityMetricsLogger = new com.android.server.wm.ActivityMetricsLogger(this, this.mHandler.getLooper());
        this.mKeyguardController = new com.android.server.wm.KeyguardController(this.mService, this);
        this.mPersisterQueue = new com.android.server.wm.PersisterQueue();
        this.mLaunchParamsPersister = new com.android.server.wm.LaunchParamsPersister(this.mPersisterQueue, this);
        this.mLaunchParamsController = new com.android.server.wm.LaunchParamsController(this.mService, this.mLaunchParamsPersister);
        this.mLaunchParamsController.registerDefaultModifiers(this);
        this.mBalController = new com.android.server.wm.BackgroundActivityStartController(this.mService, this);
    }

    void onSystemReady() {
        this.mLaunchParamsPersister.onSystemReady();
    }

    public void notifyServiceTracker(com.android.server.wm.ActivityRecord.State state, boolean early_notify, com.android.server.wm.ActivityRecord r, long createTime) {
    }

    void onUserUnlocked(int userId) {
        this.mPersisterQueue.startPersisting();
        this.mLaunchParamsPersister.onUnlockUser(userId);
        scheduleStartHome("userUnlocked");
    }

    public com.android.server.wm.ActivityMetricsLogger getActivityMetricsLogger() {
        return this.mActivityMetricsLogger;
    }

    public com.android.server.wm.KeyguardController getKeyguardController() {
        return this.mKeyguardController;
    }

    android.content.ComponentName getSystemChooserActivity() {
        if (this.mSystemChooserActivity == null) {
            this.mSystemChooserActivity = android.content.ComponentName.unflattenFromString(this.mService.mContext.getResources().getString(android.R.string.config_customCountryDetector));
        }
        return this.mSystemChooserActivity;
    }

    void setRecentTasks(com.android.server.wm.RecentTasks recentTasks) {
        if (this.mRecentTasks != null) {
            this.mRecentTasks.unregisterCallback(this);
        }
        this.mRecentTasks = recentTasks;
        this.mRecentTasks.registerCallback(this);
    }

    void setRunningTasks(com.android.server.wm.RunningTasks runningTasks) {
        this.mRunningTasks = runningTasks;
    }

    com.android.server.wm.RunningTasks getRunningTasks() {
        return this.mRunningTasks;
    }

    void initPowerManagement() {
        this.mPowerManager = (android.os.PowerManager) this.mService.mContext.getSystemService(android.os.PowerManager.class);
        this.mGoingToSleepWakeLock = this.mPowerManager.newWakeLock(1, "ActivityManager-Sleep");
        this.mLaunchingActivityWakeLock = this.mPowerManager.newWakeLock(1, "*launch*");
        this.mLaunchingActivityWakeLock.setReferenceCounted(false);
    }

    void setWindowManager(com.android.server.wm.WindowManagerService wm) {
        this.mWindowManager = wm;
        getKeyguardController().setWindowManager(wm);
    }

    void moveRecentsRootTaskToFront(java.lang.String reason) {
        com.android.server.wm.Task recentsRootTask = this.mRootWindowContainer.getDefaultTaskDisplayArea().getRootTask(0, 3);
        if (recentsRootTask != null) {
            recentsRootTask.moveToFront(reason);
        }
    }

    void setNextTaskIdForUser(int taskId, int userId) {
        int currentTaskId = this.mCurTaskIdForUser.get(userId, -1);
        if (taskId > currentTaskId) {
            this.mCurTaskIdForUser.put(userId, taskId);
        }
    }

    void finishNoHistoryActivitiesIfNeeded(com.android.server.wm.ActivityRecord next) {
        for (int i = this.mNoHistoryActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord noHistoryActivity = this.mNoHistoryActivities.get(i);
            if (!noHistoryActivity.finishing && noHistoryActivity != next && next.occludesParent() && noHistoryActivity.getDisplayId() == next.getDisplayId()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(noHistoryActivity);
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 7803197981786977817L, 0, null, protoLogParam0);
                }
                noHistoryActivity.finishIfPossible("resume-no-history", false);
                this.mNoHistoryActivities.remove(noHistoryActivity);
            }
        }
    }

    private static int nextTaskIdForUser(int taskId, int userId) {
        int nextTaskId = taskId + 1;
        if (nextTaskId == (userId + 1) * 100000) {
            return nextTaskId - 100000;
        }
        return nextTaskId;
    }

    int getNextTaskIdForUser() {
        return getNextTaskIdForUser(this.mRootWindowContainer.mCurrentUser);
    }

    int getNextTaskIdForUser(int userId) {
        int currentTaskId = this.mCurTaskIdForUser.get(userId, 100000 * userId);
        int candidateTaskId = nextTaskIdForUser(currentTaskId, userId);
        do {
            if (this.mRecentTasks.containsTaskId(candidateTaskId, userId) || this.mRootWindowContainer.anyTaskForId(candidateTaskId, 1) != null) {
                candidateTaskId = nextTaskIdForUser(candidateTaskId, userId);
            } else {
                this.mCurTaskIdForUser.put(userId, candidateTaskId);
                return candidateTaskId;
            }
        } while (candidateTaskId != currentTaskId);
        throw new java.lang.IllegalStateException("Cannot get an available task id. Reached limit of 100000 running tasks per user.");
    }

    void waitActivityVisibleOrLaunched(android.app.WaitResult w, com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityMetricsLogger.LaunchingState launchingState) {
        if (w.result != 2 && w.result != 0) {
            return;
        }
        com.android.server.wm.ActivityTaskSupervisor.WaitInfo waitInfo = new com.android.server.wm.ActivityTaskSupervisor.WaitInfo(w, r.mActivityComponent, launchingState);
        this.mWaitingActivityLaunched.add(waitInfo);
        do {
            try {
                this.mService.mGlobalLock.wait();
            } catch (java.lang.InterruptedException e) {
            }
        } while (this.mWaitingActivityLaunched.contains(waitInfo));
    }

    void cleanupActivity(com.android.server.wm.ActivityRecord r) {
        this.mFinishingActivities.remove(r);
        stopWaitingForActivityVisible(r);
    }

    void stopWaitingForActivityVisible(com.android.server.wm.ActivityRecord r) {
        reportActivityLaunched(false, r, -1L, 0);
    }

    void reportActivityLaunched(boolean timeout, com.android.server.wm.ActivityRecord r, long totalTime, int launchState) {
        boolean changed = false;
        for (int i = this.mWaitingActivityLaunched.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityTaskSupervisor.WaitInfo info = this.mWaitingActivityLaunched.get(i);
            if (info.matches(r)) {
                android.app.WaitResult w = info.mResult;
                w.timeout = timeout;
                w.who = r.mActivityComponent;
                w.totalTime = totalTime;
                w.launchState = launchState;
                this.mWaitingActivityLaunched.remove(i);
                changed = true;
            }
        }
        if (changed) {
            this.mService.mGlobalLock.notifyAll();
        }
    }

    void reportWaitingActivityLaunchedIfNeeded(com.android.server.wm.ActivityRecord r, int result) {
        if (this.mWaitingActivityLaunched.isEmpty()) {
            return;
        }
        if (result != 3 && result != 2) {
            return;
        }
        boolean changed = false;
        for (int i = this.mWaitingActivityLaunched.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityTaskSupervisor.WaitInfo info = this.mWaitingActivityLaunched.get(i);
            if (info.matches(r)) {
                android.app.WaitResult w = info.mResult;
                w.result = result;
                if (result == 3) {
                    w.who = r.mActivityComponent;
                    this.mWaitingActivityLaunched.remove(i);
                    changed = true;
                }
            }
        }
        if (changed) {
            this.mService.mGlobalLock.notifyAll();
        }
    }

    android.content.pm.ActivityInfo resolveActivity(android.content.Intent intent, android.content.pm.ResolveInfo rInfo, final int startFlags, final android.app.ProfilerInfo profilerInfo) {
        final android.content.pm.ActivityInfo aInfo = rInfo != null ? rInfo.activityInfo : null;
        if (aInfo != null) {
            intent.setComponent(new android.content.ComponentName(aInfo.applicationInfo.packageName, aInfo.name));
            boolean requestDebug = (startFlags & 14) != 0;
            boolean requestProfile = profilerInfo != null;
            if (requestDebug || requestProfile) {
                boolean debuggable = (android.os.Build.IS_DEBUGGABLE || (aInfo.applicationInfo.flags & 2) != 0) && !aInfo.processName.equals("system");
                if ((requestDebug && !debuggable) || (requestProfile && !debuggable && !aInfo.applicationInfo.isProfileableByShell())) {
                    android.util.Slog.w(TAG, "Ignore debugging for non-debuggable app: " + aInfo.packageName);
                } else {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            this.mService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda9
                                @Override // java.lang.Runnable
                                public final void run() {
                                    this.f$0.lambda$resolveActivity$1(aInfo, startFlags, profilerInfo);
                                }
                            });
                            try {
                                this.mService.mGlobalLock.wait();
                            } catch (java.lang.InterruptedException e) {
                            }
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            }
            java.lang.String intentLaunchToken = intent.getLaunchToken();
            if (aInfo.launchToken == null && intentLaunchToken != null) {
                aInfo.launchToken = intentLaunchToken;
            }
        }
        return aInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resolveActivity$1(android.content.pm.ActivityInfo aInfo, int startFlags, android.app.ProfilerInfo profilerInfo) {
        try {
            this.mService.mAmInternal.setDebugFlagsForStartingActivity(aInfo, startFlags, profilerInfo, this.mService.mGlobalLock);
        } finally {
        }
    }

    android.content.pm.ResolveInfo resolveIntent(android.content.Intent intent, java.lang.String resolvedType, int userId, int flags, int filterCallingUid, int callingPid) {
        int privateResolveFlags;
        try {
            android.os.Trace.traceBegin(32L, "resolveIntent");
            int modifiedFlags = flags | 65536 | 1024;
            int modifiedFlags2 = (intent.isWebIntent() || (intent.getFlags() & 2048) != 0) ? modifiedFlags | 8388608 : modifiedFlags;
            int privateResolveFlags2 = 0;
            if (intent.isWebIntent() && (intent.getFlags() & 1024) != 0) {
                privateResolveFlags2 = 0 | 1;
            }
            if ((intent.getFlags() & 512) == 0) {
                privateResolveFlags = privateResolveFlags2;
            } else {
                privateResolveFlags = privateResolveFlags2 | 2;
            }
            this.mActivityTaskSupervisorExt.setOplusCallingUid(intent);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return this.mService.getPackageManagerInternalLocked().resolveIntent(intent, resolvedType, modifiedFlags2, privateResolveFlags, userId, true, filterCallingUid, callingPid);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    public android.content.pm.ActivityInfo resolveActivity(android.content.Intent intent, java.lang.String resolvedType, int startFlags, android.app.ProfilerInfo profilerInfo, int userId, int filterCallingUid, int callingPid) {
        android.content.pm.ResolveInfo rInfo = resolveIntent(intent, resolvedType, userId, 0, filterCallingUid, callingPid);
        return resolveActivity(intent, rInfo, startFlags, profilerInfo);
    }

    /* JADX WARN: Removed duplicated region for block: B:192:0x04fe  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x051d A[Catch: all -> 0x055c, TRY_LEAVE, TryCatch #16 {all -> 0x055c, blocks: (B:199:0x0519, B:201:0x051d, B:205:0x0555, B:206:0x055b, B:193:0x0507, B:194:0x050c), top: B:228:0x0507 }] */
    /* JADX WARN: Removed duplicated region for block: B:204:0x0554  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x011b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0140 A[Catch: all -> 0x0123, TRY_ENTER, TRY_LEAVE, TryCatch #1 {all -> 0x0123, blocks: (B:40:0x011b, B:46:0x0131, B:48:0x0137, B:51:0x0140, B:57:0x014f, B:68:0x01b7, B:72:0x01d7, B:74:0x01dc, B:76:0x01e1, B:83:0x01f4, B:90:0x020c, B:95:0x029f, B:102:0x02bd), top: B:217:0x011b }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0147  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0157 A[Catch: all -> 0x055e, TRY_ENTER, TryCatch #19 {all -> 0x055e, blocks: (B:38:0x0116, B:44:0x0129, B:49:0x013a, B:55:0x0149, B:60:0x019d, B:65:0x01ac, B:69:0x01cb, B:79:0x01ea, B:88:0x0208, B:91:0x024e, B:93:0x0292, B:99:0x02b1, B:106:0x02cd, B:108:0x02f7, B:78:0x01e7, B:64:0x01a7, B:59:0x0157), top: B:242:0x0116 }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01a3  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01a7 A[Catch: all -> 0x055e, TryCatch #19 {all -> 0x055e, blocks: (B:38:0x0116, B:44:0x0129, B:49:0x013a, B:55:0x0149, B:60:0x019d, B:65:0x01ac, B:69:0x01cb, B:79:0x01ea, B:88:0x0208, B:91:0x024e, B:93:0x0292, B:99:0x02b1, B:106:0x02cd, B:108:0x02f7, B:78:0x01e7, B:64:0x01a7, B:59:0x0157), top: B:242:0x0116 }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01b7 A[Catch: all -> 0x0123, TRY_ENTER, TRY_LEAVE, TryCatch #1 {all -> 0x0123, blocks: (B:40:0x011b, B:46:0x0131, B:48:0x0137, B:51:0x0140, B:57:0x014f, B:68:0x01b7, B:72:0x01d7, B:74:0x01dc, B:76:0x01e1, B:83:0x01f4, B:90:0x020c, B:95:0x029f, B:102:0x02bd), top: B:217:0x011b }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x01e7 A[Catch: all -> 0x055e, TRY_ENTER, TRY_LEAVE, TryCatch #19 {all -> 0x055e, blocks: (B:38:0x0116, B:44:0x0129, B:49:0x013a, B:55:0x0149, B:60:0x019d, B:65:0x01ac, B:69:0x01cb, B:79:0x01ea, B:88:0x0208, B:91:0x024e, B:93:0x0292, B:99:0x02b1, B:106:0x02cd, B:108:0x02f7, B:78:0x01e7, B:64:0x01a7, B:59:0x0157), top: B:242:0x0116 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01f0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean realStartActivityLocked(com.android.server.wm.ActivityRecord r46, com.android.server.wm.WindowProcessController r47, boolean r48, boolean r49) throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 1390
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityTaskSupervisor.realStartActivityLocked(com.android.server.wm.ActivityRecord, com.android.server.wm.WindowProcessController, boolean, boolean):boolean");
    }

    void updateHomeProcessIfNeeded(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.ActivityRecord bottom;
        if (r.isActivityTypeHome() && (bottom = r.getTask().getBottomMostActivityInSamePackage()) != null) {
            updateHomeProcess(bottom.app);
        }
    }

    void updateHomeProcess(com.android.server.wm.WindowProcessController app) {
        if (app != null && this.mService.mHomeProcess != app) {
            scheduleStartHome("homeChanged");
            this.mService.mHomeProcess = app;
        }
    }

    private void scheduleStartHome(java.lang.String reason) {
        if (!this.mHandler.hasMessages(216)) {
            this.mHandler.obtainMessage(216, reason).sendToTarget();
        }
    }

    private void logIfTransactionTooLarge(android.content.Intent intent, android.os.Bundle icicle) {
        android.os.Bundle extras;
        int extrasSize = 0;
        if (intent != null && (extras = intent.getExtras()) != null) {
            extrasSize = extras.getSize();
        }
        int icicleSize = icicle == null ? 0 : icicle.getSize();
        if (extrasSize + icicleSize > 200000) {
            android.util.Slog.e(TAG, "Transaction too large, intent: " + intent + ", extras size: " + extrasSize + ", icicle size: " + icicleSize);
        }
    }

    void startSpecificActivity(com.android.server.wm.ActivityRecord r, boolean andResume, boolean checkConfig) {
        com.android.server.wm.WindowProcessController wpc = this.mService.getProcessController(r.processName, r.info.applicationInfo.uid);
        boolean knownToBeDead = false;
        this.mActivityTaskSupervisorExt.setLaunchTimeStart(r);
        if (wpc != null && wpc.hasThread()) {
            try {
                this.mSocExt.startSpecificActivityPerfHint(TAG, r, wpc.getPid());
                realStartActivityLocked(r, wpc, andResume, checkConfig);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Exception when starting activity " + r.intent.getComponent().flattenToShortString(), e);
                knownToBeDead = true;
                this.mService.mProcessNames.remove(wpc.mName, wpc.mUid);
                this.mService.mProcessMap.remove(wpc.getPid());
            }
        } else if (com.android.server.wm.ActivityTaskManagerService.isSdkSandboxActivityIntent(this.mService.mContext, r.intent)) {
            android.util.Slog.e(TAG, "Abort sandbox activity launching as no sandbox process to host it.");
            r.finishIfPossible("No sandbox process for the activity", false);
            r.launchFailed = true;
            r.detachFromProcess();
            return;
        }
        this.mActivityTaskSupervisorExt.hookStartSpecificActivity(this.mService.mContext);
        r.notifyUnknownVisibilityLaunchedForKeyguardTransition();
        this.mActivityTaskSupervisorExt.handleActivityStartBeforeStartProc(r, andResume, r.getDisplayId());
        boolean isTop = andResume && r.isTopRunningActivity();
        this.mActivityTaskSupervisorExt.modifyApplicaitonInfoForMirageCarMode(r);
        this.mActivityTaskSupervisorExt.notifySysActivityColdLaunch(com.android.server.wm.ActivityTaskSupervisor.class, r, r.mActivityComponent);
        this.mService.startProcessAsync(r, knownToBeDead, isTop, isTop ? com.android.server.am.HostingRecord.HOSTING_TYPE_TOP_ACTIVITY : com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
        this.mActivityTaskSupervisorExt.handleActivityStartAfterStartProc(r);
    }

    boolean checkStartAnyActivityPermission(android.content.Intent intent, android.content.pm.ActivityInfo aInfo, java.lang.String resultWho, int requestCode, int callingPid, int callingUid, java.lang.String callingPackage, java.lang.String callingFeatureId, boolean ignoreTargetSecurity, boolean launchingInTask, com.android.server.wm.WindowProcessController callerApp, com.android.server.wm.ActivityRecord resultRecord, com.android.server.wm.Task resultRootTask) {
        java.lang.String msg;
        boolean isCallerRecents = this.mService.getRecentTasks() != null && this.mService.getRecentTasks().isCallerRecents(callingUid);
        int startAnyPerm = com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.START_ANY_ACTIVITY", callingPid, callingUid);
        if (startAnyPerm == 0) {
            return true;
        }
        if (isCallerRecents && launchingInTask) {
            return true;
        }
        int componentRestriction = getComponentRestrictionForCallingPackage(aInfo, callingPackage, callingFeatureId, callingPid, callingUid, ignoreTargetSecurity);
        int actionRestriction = getActionRestrictionForCallingPackage(intent.getAction(), callingPackage, callingFeatureId, callingPid, callingUid);
        if (componentRestriction == 1 || actionRestriction == 1) {
            if (resultRecord != null) {
                resultRecord.sendResult(-1, resultWho, requestCode, 0, null, null, null);
            }
            if (actionRestriction == 1) {
                msg = "Permission Denial: starting " + intent.toString() + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") with revoked permission " + ACTION_TO_RUNTIME_PERMISSION.get(intent.getAction());
            } else if (aInfo.exported) {
                msg = "Permission Denial: starting " + intent.toString() + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") requires " + aInfo.permission;
            } else {
                msg = "Permission Denial: starting " + intent.toString() + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") not exported from uid " + aInfo.applicationInfo.uid;
            }
            android.util.Slog.w(TAG, msg);
            throw new java.lang.SecurityException(msg);
        }
        if (actionRestriction == 2) {
            java.lang.String message = "Appop Denial: starting " + intent.toString() + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") requires " + android.app.AppOpsManager.permissionToOp(ACTION_TO_RUNTIME_PERMISSION.get(intent.getAction()));
            android.util.Slog.w(TAG, message);
            return false;
        }
        if (componentRestriction == 2) {
            java.lang.String message2 = "Appop Denial: starting " + intent.toString() + " from " + callerApp + " (pid=" + callingPid + ", uid=" + callingUid + ") requires appop " + android.app.AppOpsManager.permissionToOp(aInfo.permission);
            android.util.Slog.w(TAG, message2);
            return false;
        }
        return true;
    }

    boolean isCallerAllowedToLaunchOnTaskDisplayArea(int callingPid, int callingUid, com.android.server.wm.TaskDisplayArea taskDisplayArea, android.content.pm.ActivityInfo aInfo) {
        return isCallerAllowedToLaunchOnDisplay(callingPid, callingUid, taskDisplayArea != null ? taskDisplayArea.getDisplayId() : 0, aInfo);
    }

    boolean isCallerAllowedToLaunchOnDisplay(int callingPid, int callingUid, int launchDisplayId, android.content.pm.ActivityInfo aInfo) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
            long protoLogParam0 = launchDisplayId;
            long protoLogParam1 = callingPid;
            long protoLogParam2 = callingUid;
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -8529426827020190143L, 21, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2));
        }
        if (callingPid == -1 && callingUid == -1) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 9147909968067116569L, 0, null, null);
            }
            return true;
        }
        com.android.server.wm.DisplayContent displayContent = this.mRootWindowContainer.getDisplayContentOrCreate(launchDisplayId);
        if (displayContent == null || displayContent.isRemoved()) {
            android.util.Slog.w(TAG, "Launch on display check: display not found");
            return false;
        }
        if ((displayContent.mDisplay.getFlags() & 8192) != 0) {
            android.util.Slog.w(TAG, "Launch on display check: activity launch is not allowed on rear display");
            return false;
        }
        int startAnyPerm = com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.INTERNAL_SYSTEM_WINDOW", callingPid, callingUid);
        if (startAnyPerm == 0) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 4781135167649953680L, 0, null, null);
            }
            return true;
        }
        boolean uidPresentOnDisplay = displayContent.isUidPresent(callingUid);
        android.view.Display display = displayContent.mDisplay;
        if (!display.isTrusted()) {
            if ((aInfo.flags & Integer.MIN_VALUE) == 0) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 7828411869729995271L, 0, null, null);
                }
                if (!this.mActivityTaskSupervisorExt.isPuttDisplay(launchDisplayId) && !this.mActivityTaskSupervisorExt.isMirageDisplay(launchDisplayId)) {
                    android.util.Slog.w(TAG, "Launch on display check:  allow putt or mirage task on virtual display");
                    return false;
                }
            }
            if (com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.ACTIVITY_EMBEDDING", callingPid, callingUid) == -1 && !uidPresentOnDisplay) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -2215878620906309682L, 0, null, null);
                }
                if (!this.mActivityTaskSupervisorExt.isPuttDisplay(launchDisplayId) && !this.mActivityTaskSupervisorExt.isMirageDisplay(launchDisplayId)) {
                    android.util.Slog.w(TAG, "Launch on display check:  allow putt or mirage task on virtual display");
                    return false;
                }
            }
        }
        if (!displayContent.isPrivate()) {
            int userId = android.os.UserHandle.getUserId(callingUid);
            int displayId = display.getDisplayId();
            boolean allowed = this.mWindowManager.mUmInternal.isUserVisible(userId, displayId);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(allowed ? "allow" : "disallow");
                long protoLogParam12 = userId;
                long protoLogParam22 = displayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 986565579776405555L, 20, null, protoLogParam02, java.lang.Long.valueOf(protoLogParam12), java.lang.Long.valueOf(protoLogParam22));
            }
            return allowed;
        }
        if (display.getOwnerUid() == callingUid) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -2201418325681949201L, 0, null, null);
            }
            return true;
        }
        if (!uidPresentOnDisplay) {
            android.util.Slog.w(TAG, "Launch on display check: denied");
            return false;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -4258279435559028377L, 0, null, null);
        }
        return true;
    }

    android.content.pm.UserInfo getUserInfo(int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return android.os.UserManager.get(this.mService.mContext).getUserInfo(userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    int getDeviceIdForDisplayId(int displayId) {
        if (displayId == 0 || displayId == -1) {
            return 0;
        }
        if (this.mVirtualDeviceManager == null) {
            if (this.mService.mHasCompanionDeviceSetupFeature) {
                this.mVirtualDeviceManager = (android.companion.virtual.VirtualDeviceManager) this.mService.mContext.getSystemService(android.companion.virtual.VirtualDeviceManager.class);
            }
            if (this.mVirtualDeviceManager == null) {
                return 0;
            }
        }
        return this.mVirtualDeviceManager.getDeviceIdForDisplayId(displayId);
    }

    private android.app.AppOpsManager getAppOpsManager() {
        if (this.mAppOpsManager == null) {
            this.mAppOpsManager = (android.app.AppOpsManager) this.mService.mContext.getSystemService(android.app.AppOpsManager.class);
        }
        return this.mAppOpsManager;
    }

    com.android.server.wm.BackgroundActivityStartController getBackgroundActivityLaunchController() {
        return this.mBalController;
    }

    private int getComponentRestrictionForCallingPackage(android.content.pm.ActivityInfo activityInfo, java.lang.String callingPackage, java.lang.String callingFeatureId, int callingPid, int callingUid, boolean ignoreTargetSecurity) {
        int opCode;
        if (ignoreTargetSecurity || com.android.server.wm.ActivityTaskManagerService.checkComponentPermission(activityInfo.permission, callingPid, callingUid, activityInfo.applicationInfo.uid, activityInfo.exported) != -1) {
            return (activityInfo.permission == null || (opCode = android.app.AppOpsManager.permissionToOpCode(activityInfo.permission)) == -1 || getAppOpsManager().noteOpNoThrow(opCode, callingUid, callingPackage, callingFeatureId, "") == 0 || ignoreTargetSecurity) ? 0 : 2;
        }
        return 1;
    }

    private int getActionRestrictionForCallingPackage(java.lang.String action, java.lang.String callingPackage, java.lang.String callingFeatureId, int callingPid, int callingUid) {
        java.lang.String permission;
        if (action == null || (permission = ACTION_TO_RUNTIME_PERMISSION.get(action)) == null) {
            return 0;
        }
        try {
            android.content.pm.PackageInfo packageInfo = this.mService.mContext.getPackageManager().getPackageInfoAsUser(callingPackage, 4096, android.os.UserHandle.getUserId(callingUid));
            if (!com.android.internal.util.ArrayUtils.contains(packageInfo.requestedPermissions, permission)) {
                return 0;
            }
            if (com.android.server.wm.ActivityTaskManagerService.checkPermission(permission, callingPid, callingUid) == -1) {
                return 1;
            }
            int opCode = android.app.AppOpsManager.permissionToOpCode(permission);
            if (opCode == -1 || getAppOpsManager().noteOpNoThrow(opCode, callingUid, callingPackage, callingFeatureId, "") == 0) {
                return 0;
            }
            if ("android.permission.CAMERA".equals(permission)) {
                android.hardware.SensorPrivacyManagerInternal spmi = (android.hardware.SensorPrivacyManagerInternal) com.android.server.LocalServices.getService(android.hardware.SensorPrivacyManagerInternal.class);
                android.os.UserHandle user = android.os.UserHandle.getUserHandleForUid(callingUid);
                boolean cameraPrivacyEnabled = spmi.isSensorPrivacyEnabled(user.getIdentifier(), 2);
                if (cameraPrivacyEnabled) {
                    android.app.AppOpsManagerInternal aomi = (android.app.AppOpsManagerInternal) com.android.server.LocalServices.getService(android.app.AppOpsManagerInternal.class);
                    int numCameraRestrictions = aomi.getOpRestrictionCount(26, user, callingPackage, (java.lang.String) null);
                    return numCameraRestrictions == 1 ? 0 : 2;
                }
                return 2;
            }
            return 2;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.i(TAG, "Cannot find package info for " + callingPackage);
            return 0;
        }
    }

    void setLaunchSource(int uid) {
        this.mLaunchingActivityWakeLock.setWorkSource(new android.os.WorkSource(uid));
    }

    void acquireLaunchWakelock() {
        if (android.os.Binder.getCallingUid() != 1000) {
            android.util.Slog.e(TAG, "acquireLaunchWakelock and binderUid is: " + android.os.Binder.getCallingUid() + " processUid is: 1000 current stack is: " + android.util.Log.getStackTraceString(new java.lang.Throwable()));
        }
        this.mLaunchingActivityWakeLock.acquire();
        if (!this.mHandler.hasMessages(204)) {
            this.mHandler.sendEmptyMessageDelayed(204, LAUNCH_TIMEOUT);
        }
    }

    private void checkFinishBootingLocked() {
        boolean booting = this.mService.isBooting();
        boolean enableScreen = false;
        this.mService.setBooting(false);
        if (!this.mService.isBooted()) {
            this.mService.setBooted(true);
            enableScreen = true;
        }
        if (booting || enableScreen) {
            this.mService.postFinishBooting(booting, enableScreen);
        }
    }

    void activityIdleInternal(com.android.server.wm.ActivityRecord r, boolean fromTimeout, boolean processPausingActivities, android.content.res.Configuration config) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ALL || this.mService.isBooting()) {
            android.util.Slog.v(TAG, "Activity idle: " + r + ",fromTimeout=" + fromTimeout + ",isBooting=" + this.mService.isBooting());
        }
        if (r != null) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
                android.util.Slog.d(TAG_IDLE, "activityIdleInternal: Callers=" + android.os.Debug.getCallers(4));
            }
            this.mHandler.removeMessages(200, r);
            r.finishLaunchTickingLocked();
            if (fromTimeout) {
                reportActivityLaunched(fromTimeout, r, -1L, -1);
            }
            if (config != null) {
                r.setLastReportedGlobalConfiguration(config);
            }
            r.idle = true;
            this.mActivityTaskSupervisorExt.handleActivityIdle(r);
            if ((this.mService.isBooting() && this.mRootWindowContainer.allResumedActivitiesIdle()) || fromTimeout) {
                checkFinishBootingLocked();
            }
            r.mRelaunchReason = 0;
        }
        if (this.mRootWindowContainer.allResumedActivitiesIdle()) {
            if (r != null) {
                this.mService.scheduleAppGcsLocked();
                this.mRecentTasks.onActivityIdle(r);
            }
            if (this.mLaunchingActivityWakeLock.isHeld()) {
                this.mHandler.removeMessages(204);
                this.mLaunchingActivityWakeLock.release();
            }
        }
        processStoppingAndFinishingActivities(r, processPausingActivities, "idle");
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
            com.android.server.utils.Slogf.i(TAG, "activityIdleInternal(): r=%s, mStartingUsers=%s", r, this.mStartingUsers);
        }
        if (!this.mStartingUsers.isEmpty()) {
            java.util.ArrayList<com.android.server.am.UserState> startingUsers = new java.util.ArrayList<>(this.mStartingUsers);
            this.mStartingUsers.clear();
            for (int i = 0; i < startingUsers.size(); i++) {
                com.android.server.am.UserState userState = startingUsers.get(i);
                com.android.server.utils.Slogf.i(TAG, "finishing switch of user %d", java.lang.Integer.valueOf(userState.mHandle.getIdentifier()));
                this.mService.mAmInternal.finishUserSwitch(userState);
            }
        }
        this.mService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$activityIdleInternal$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$activityIdleInternal$2() {
        this.mService.mAmInternal.trimApplications();
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x00eb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0078 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0095 A[Catch: all -> 0x0069, TRY_ENTER, TryCatch #6 {all -> 0x0069, blocks: (B:16:0x0060, B:23:0x0072, B:26:0x007a, B:29:0x0095, B:31:0x00ad, B:34:0x00b7), top: B:137:0x0060 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00be A[ADDED_TO_REGION, REMOVE] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0144  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void findTaskToMoveToFront(com.android.server.wm.Task r22, int r23, android.app.ActivityOptions r24, java.lang.String r25, boolean r26) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 557
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityTaskSupervisor.findTaskToMoveToFront(com.android.server.wm.Task, int, android.app.ActivityOptions, java.lang.String, boolean):void");
    }

    private void moveHomeRootTaskToFrontIfNeeded(int flags, com.android.server.wm.TaskDisplayArea taskDisplayArea, java.lang.String reason) {
        com.android.server.wm.Task focusedRootTask = taskDisplayArea.getFocusedRootTask();
        if ((taskDisplayArea.getWindowingMode() == 1 && (flags & 1) != 0) || (focusedRootTask != null && focusedRootTask.isActivityTypeRecents())) {
            taskDisplayArea.moveHomeRootTaskToFront(reason);
        }
    }

    boolean canUseActivityOptionsLaunchBounds(android.app.ActivityOptions options) {
        if (options == null || options.getLaunchBounds() == null) {
            return false;
        }
        return (this.mService.mSupportsPictureInPicture && options.getLaunchWindowingMode() == 2) || this.mService.mSupportsFreeformWindowManagement;
    }

    com.android.server.wm.LaunchParamsController getLaunchParamsController() {
        return this.mLaunchParamsController;
    }

    private void removePinnedRootTaskInSurfaceTransaction(com.android.server.wm.Task rootTask) {
        com.android.server.wm.Transition transition = rootTask.mTransitionController.requestTransitionIfNeeded(4, 0, rootTask, rootTask.mDisplayContent);
        if (transition == null) {
            rootTask.mTransitionController.collect(rootTask);
        } else {
            transition.collect(rootTask);
        }
        rootTask.cancelAnimation();
        rootTask.setForceHidden(1, true);
        rootTask.ensureActivitiesVisible(null);
        activityIdleInternal(null, false, true, null);
        com.android.server.wm.DisplayContent toDisplay = this.mRootWindowContainer.getDisplayContent(0);
        this.mService.deferWindowLayout();
        try {
            rootTask.setWindowingMode(0);
            if (rootTask.getWindowingMode() != 5) {
                rootTask.setBounds(null);
            }
            toDisplay.getDefaultTaskDisplayArea().positionTaskBehindHome(rootTask);
            rootTask.setForceHidden(1, false);
            this.mRootWindowContainer.ensureActivitiesVisible();
            this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        } finally {
            this.mService.continueWindowLayout();
        }
    }

    void removeRootTask(com.android.server.wm.Task rootTask) {
        if (rootTask.getWindowingMode() == 2) {
            removePinnedRootTaskInSurfaceTransaction(rootTask);
        } else {
            rootTask.forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$removeRootTask$3((com.android.server.wm.Task) obj);
                }
            }, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeRootTask$3(com.android.server.wm.Task task) {
        removeTask(task, true, true, "remove-root-task");
    }

    boolean removeTaskById(int taskId, boolean killProcess, boolean removeFromRecents, java.lang.String reason, int callingUid, int callingPid) {
        com.android.server.wm.Task task = this.mRootWindowContainer.anyTaskForId(taskId, 1);
        if (task != null) {
            removeTask(task, killProcess, removeFromRecents, reason, callingUid, callingPid, null);
            return true;
        }
        android.util.Slog.w(TAG, "Request to remove task ignored for non-existent task " + taskId);
        return false;
    }

    void removeTask(com.android.server.wm.Task task, boolean killProcess, boolean removeFromRecents, java.lang.String reason) {
        removeTask(task, killProcess, removeFromRecents, reason, 1000, -1, null);
    }

    void removeTask(com.android.server.wm.Task task, boolean killProcess, boolean removeFromRecents, java.lang.String reason, int callingUid, int callingPid, java.lang.String callerActivityClassName) {
        if (task.mInRemoveTask) {
            return;
        }
        com.android.server.wm.Transition transit = task.mTransitionController.requestCloseTransitionIfNeeded(task);
        if (transit != null) {
            transit.collectClose(task);
            if (!task.mTransitionController.useFullReadyTracking()) {
                transit.setReady(task, true);
            }
        } else if (task.mTransitionController.isCollecting()) {
            task.mTransitionController.getCollectingTransition().collectClose(task);
        }
        if (killProcess) {
            java.util.ArrayList<com.android.server.wm.ActivityRecord> activities = null;
            for (int i = this.mStoppingActivities.size() - 1; i >= 0; i--) {
                com.android.server.wm.ActivityRecord r = this.mStoppingActivities.get(i);
                if (!r.finishing && r.isState(com.android.server.wm.ActivityRecord.State.RESUMED) && r.getTask() == task) {
                    if (activities == null) {
                        activities = new java.util.ArrayList<>();
                    }
                    activities.add(r);
                    this.mStoppingActivities.remove(i);
                }
            }
            if (activities != null) {
                for (int i2 = activities.size() - 1; i2 >= 0; i2--) {
                    if (!activities.get(i2).isState(com.android.server.wm.ActivityRecord.State.DESTROYING, com.android.server.wm.ActivityRecord.State.DESTROYED)) {
                        activities.get(i2).stopIfPossible();
                    }
                }
            }
        }
        this.mActivityTaskSupervisorExt.hookBeforeRemoveTask(task, reason);
        task.mInRemoveTask = true;
        try {
            task.removeActivities(reason, false);
            this.mActivityTaskSupervisorExt.handleRemoveTask(task, killProcess, removeFromRecents, reason);
            if (task.getWrapper().getExtImpl().isCompactWindowingMode(task.getWindowingMode()) && task.getBaseIntent() != null && task.getBaseIntent().getComponent() != null) {
                task.getWrapper().getExtImpl().handleRemoveTask(task.getBaseIntent().getComponent().getPackageName(), task.mUserId);
            }
            cleanUpRemovedTask(task, killProcess, removeFromRecents);
            this.mService.getLockTaskController().clearLockedTask(task);
            this.mService.getTaskChangeNotificationController().notifyTaskStackChanged();
            if (task.isPersistable) {
                this.mService.notifyTaskPersisterLocked(null, true);
            }
            this.mBalController.checkActivityAllowedToClearTask(task, callingUid, callingPid, callerActivityClassName);
            if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE) {
                this.mService.getWrapper().getExtImpl().getRemoteTaskManager().handleRemoveTask(task, reason);
            }
        } finally {
            task.mInRemoveTask = false;
        }
    }

    static java.lang.CharSequence getApplicationLabel(android.content.pm.PackageManager pm, java.lang.String packageName) {
        try {
            android.content.pm.ApplicationInfo launchedFromPackageInfo = pm.getApplicationInfo(packageName, android.content.pm.PackageManager.ApplicationInfoFlags.of(0L));
            return pm.getApplicationLabel(launchedFromPackageInfo);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return packageName;
        }
    }

    private void cleanUpRemovedTask(com.android.server.wm.Task task, boolean killProcess, boolean removeFromRecents) {
        if (removeFromRecents) {
            this.mRecentTasks.remove(task);
        }
        android.content.Intent baseIntent = task.getBaseIntent();
        android.content.ComponentName component = baseIntent != null ? baseIntent.getComponent() : null;
        if (component == null) {
            android.util.Slog.w(TAG, "No component for base intent of task: " + task);
            return;
        }
        this.mActivityTaskSupervisorExt.handleRemoveTask(killProcess, task.mUserId, component.getPackageName());
        android.os.Message msg = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda2
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                ((android.app.ActivityManagerInternal) obj).cleanUpServices(((java.lang.Integer) obj2).intValue(), (android.content.ComponentName) obj3, (android.content.Intent) obj4);
            }
        }, this.mService.mAmInternal, java.lang.Integer.valueOf(task.mUserId), component, new android.content.Intent(baseIntent));
        this.mService.mH.sendMessage(msg);
        if (!killProcess) {
            return;
        }
        com.android.server.wm.ActivityRecord top = task.getTopMostActivity();
        if (top != null && top.finishing && !top.mAppStopped && top.lastVisibleTime > 0 && !task.mKillProcessesOnDestroyed && top.hasProcess()) {
            task.mKillProcessesOnDestroyed = true;
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(206, task), 1000L);
        } else {
            killTaskProcessesIfPossible(task);
        }
    }

    void removeTimeoutOfKillProcessesOnProcessDied(com.android.server.wm.ActivityRecord r, com.android.server.wm.Task task) {
        if (r.packageName.equals(task.getBasePackageName())) {
            task.mKillProcessesOnDestroyed = false;
            this.mHandler.removeMessages(206, task);
        }
    }

    void killTaskProcessesOnDestroyedIfNeeded(com.android.server.wm.Task task) {
        if (task == null || !task.mKillProcessesOnDestroyed) {
            return;
        }
        final int[] numDestroyingActivities = new int[1];
        task.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.ActivityTaskSupervisor.lambda$killTaskProcessesOnDestroyedIfNeeded$4(numDestroyingActivities, (com.android.server.wm.ActivityRecord) obj);
            }
        });
        if (numDestroyingActivities[0] > 1) {
            return;
        }
        this.mHandler.removeMessages(206, task);
        killTaskProcessesIfPossible(task);
    }

    static /* synthetic */ void lambda$killTaskProcessesOnDestroyedIfNeeded$4(int[] numDestroyingActivities, com.android.server.wm.ActivityRecord r) {
        if (r.finishing && r.lastVisibleTime > 0 && r.attachedToProcess()) {
            numDestroyingActivities[0] = numDestroyingActivities[0] + 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void killTaskProcessesIfPossible(com.android.server.wm.Task task) {
        int filterType;
        task.mKillProcessesOnDestroyed = false;
        java.lang.String pkg = task.getBasePackageName();
        java.util.ArrayList<java.lang.Object> procsToKill = null;
        android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.wm.WindowProcessController>> pmap = this.mService.mProcessNames.getMap();
        for (int i = 0; i < pmap.size(); i++) {
            android.util.SparseArray<com.android.server.wm.WindowProcessController> uids = pmap.valueAt(i);
            for (int j = 0; j < uids.size(); j++) {
                com.android.server.wm.WindowProcessController proc = uids.valueAt(j);
                if (proc.mUserId == task.mUserId && proc != this.mService.mHomeProcess && proc.containsPackage(pkg)) {
                    if (task.getWrapper() != null && task.getWrapper().getExtImpl() != null && task.getWrapper().getExtImpl().getPid() != proc.mPid) {
                        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_AMS) {
                            android.util.Slog.d(TAG, "task.pid: " + task.getWrapper().getExtImpl().getPid() + " proc.mPid: " + proc.mPid + " task: " + task);
                        }
                    } else {
                        if (!proc.shouldKillProcessForRemovedTask(task) || (filterType = this.mActivityTaskSupervisorExt.getRemoveTaskFilterType(proc)) == 1) {
                            return;
                        }
                        if (filterType == 2) {
                            continue;
                        } else if (filterType == 3) {
                            if (procsToKill == null) {
                                procsToKill = new java.util.ArrayList<>();
                            }
                            procsToKill.add(proc);
                        } else {
                            if (proc.hasForegroundServices()) {
                                return;
                            }
                            if (procsToKill == null) {
                                procsToKill = new java.util.ArrayList<>();
                            }
                            procsToKill.add(proc);
                        }
                    }
                }
            }
        }
        if (procsToKill == null) {
            return;
        }
        android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((android.app.ActivityManagerInternal) obj).killProcessesForRemovedTask((java.util.ArrayList) obj2);
            }
        }, this.mService.mAmInternal, procsToKill);
        this.mService.mH.sendMessage(m);
    }

    boolean restoreRecentTaskLocked(com.android.server.wm.Task task, android.app.ActivityOptions aOptions, boolean onTop) {
        com.android.server.wm.Task rootTask = this.mRootWindowContainer.getOrCreateRootTask(null, aOptions, task, onTop);
        com.android.server.wm.WindowContainer parent = task.getParent();
        if (parent == rootTask || task == rootTask) {
            return true;
        }
        if (parent != null) {
            if (!this.mActivityTaskSupervisorExt.checkIsValidParentForSplitScreen(task, rootTask)) {
                return true;
            }
            task.reparent(rootTask, Integer.MAX_VALUE, true, "restoreRecentTaskLocked");
            return true;
        }
        rootTask.addChild((com.android.server.wm.WindowContainer) task, onTop, true);
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
            android.util.Slog.v(TAG_RECENTS, "Added restored task=" + task + " to root task=" + rootTask);
        }
        return true;
    }

    @Override // com.android.server.wm.RecentTasks.Callbacks
    public void onRecentTaskAdded(com.android.server.wm.Task task) {
        task.touchActiveTime();
    }

    @Override // com.android.server.wm.RecentTasks.Callbacks
    public void onRecentTaskRemoved(com.android.server.wm.Task task, boolean wasTrimmed, boolean killProcess) {
        if (wasTrimmed) {
            removeTaskById(task.mTaskId, killProcess, false, "recent-task-trimmed", 1000, -1);
        }
        task.removedFromRecents();
        if (killProcess) {
            task.getWrapper().getExtImpl().removedFromRecents(task);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    com.android.server.wm.Task getReparentTargetRootTask(com.android.server.wm.Task task, com.android.server.wm.Task task2, boolean toTop) {
        com.android.server.wm.Task prevRootTask = task.getRootTask();
        int rootTaskId = task2.mTaskId;
        boolean inMultiWindowMode = task2.inMultiWindowMode();
        if (prevRootTask != null && prevRootTask.mTaskId == rootTaskId) {
            android.util.Slog.w(TAG, "Can not reparent to same root task, task=" + task + " already in rootTaskId=" + rootTaskId + " by " + android.os.Debug.getCallers(8));
            return prevRootTask;
        }
        if (inMultiWindowMode && !this.mService.mSupportsMultiWindow) {
            throw new java.lang.IllegalArgumentException("Device doesn't support multi-window, can not reparent task=" + task + " to root-task=" + task2);
        }
        if (task2.getDisplayId() != 0 && !this.mService.mSupportsMultiDisplay) {
            throw new java.lang.IllegalArgumentException("Device doesn't support multi-display, can not reparent task=" + task + " to rootTaskId=" + rootTaskId);
        }
        if (task2.getWindowingMode() == 5 && !this.mService.mSupportsFreeformWindowManagement) {
            throw new java.lang.IllegalArgumentException("Device doesn't support freeform, can not reparent task=" + task);
        }
        if (task2.inPinnedWindowingMode()) {
            throw new java.lang.IllegalArgumentException("No support to reparent to PIP, task=" + task);
        }
        if (inMultiWindowMode && !task.supportsMultiWindowInDisplayArea(task2.getDisplayArea())) {
            android.util.Slog.w(TAG, "Can not move unresizeable task=" + task + " to multi-window root task=" + task2 + " Moving to a fullscreen root task instead.");
            if (prevRootTask != null) {
                return prevRootTask;
            }
            com.android.server.wm.Task rootTask = task2.getDisplayArea().createRootTask(1, task2.getActivityType(), toTop);
            return rootTask;
        }
        return task2;
    }

    void goingToSleepLocked() {
        this.mActivityTaskSupervisorExt.recordTopActivityWhenScreenOff(this.mService);
        scheduleSleepTimeout();
        if (!this.mGoingToSleepWakeLock.isHeld()) {
            this.mGoingToSleepWakeLock.acquire();
            if (this.mLaunchingActivityWakeLock.isHeld()) {
                this.mLaunchingActivityWakeLock.release();
                this.mHandler.removeMessages(204);
            }
        }
        this.mRootWindowContainer.applySleepTokens(false);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                checkReadyForSleepLocked(true);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    boolean shutdownLocked(int timeout) {
        goingToSleepLocked();
        boolean timedout = false;
        long endTime = java.lang.System.currentTimeMillis() + ((long) timeout);
        while (true) {
            if (!this.mRootWindowContainer.putTasksToSleep(true, true)) {
                long timeRemaining = endTime - java.lang.System.currentTimeMillis();
                if (timeRemaining > 0) {
                    try {
                        this.mService.mGlobalLock.wait(timeRemaining);
                    } catch (java.lang.InterruptedException e) {
                    }
                } else {
                    android.util.Slog.w(TAG, "Activity manager shutdown timed out");
                    timedout = true;
                    break;
                }
            } else {
                break;
            }
        }
        checkReadyForSleepLocked(false);
        return timedout;
    }

    public com.android.server.wm.ActivityRecord getTopResumedActivity() {
        return this.mTopResumedActivity;
    }

    void comeOutOfSleepIfNeededLocked() {
        removeSleepTimeouts();
        if (this.mGoingToSleepWakeLock.isHeld()) {
            try {
                this.mGoingToSleepWakeLock.release();
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.e(TAG, "Fail to release wakelock." + e);
            }
        }
    }

    void checkReadyForSleepLocked(boolean allowDelay) {
        if (!this.mService.isSleepingOrShuttingDownLocked() || !this.mRootWindowContainer.putTasksToSleep(allowDelay, false)) {
            return;
        }
        this.mService.endPowerMode(3);
        this.mRootWindowContainer.rankTaskLayers();
        removeSleepTimeouts();
        if (this.mGoingToSleepWakeLock.isHeld()) {
            try {
                this.mGoingToSleepWakeLock.release();
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.e(TAG, "Fail to release wakelock." + e);
            }
        }
        if (this.mService.mShuttingDown) {
            this.mService.mGlobalLock.notifyAll();
        }
    }

    boolean reportResumedActivityLocked(com.android.server.wm.ActivityRecord r) {
        this.mStoppingActivities.remove(r);
        com.android.server.wm.Task rootTask = r.getRootTask();
        if (rootTask.getDisplayArea().allResumedActivitiesComplete()) {
            this.mRootWindowContainer.ensureActivitiesVisible();
            this.mRootWindowContainer.executeAppTransitionForAllDisplay();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLaunchTaskBehindCompleteLocked(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.Task task = r.getTask();
        com.android.server.wm.Task rootTask = task.getRootTask();
        this.mRecentTasks.add(task);
        this.mService.getTaskChangeNotificationController().notifyTaskStackChanged();
        rootTask.ensureActivitiesVisible(null);
        com.android.server.wm.ActivityRecord top = rootTask.getTopNonFinishingActivity();
        if (top != null) {
            top.getTask().touchActiveTime();
        }
    }

    void scheduleLaunchTaskBehindComplete(android.os.IBinder token) {
        this.mHandler.obtainMessage(212, token).sendToTarget();
    }

    /* JADX WARN: Multi-variable type inference failed */
    void processStoppingAndFinishingActivities(com.android.server.wm.ActivityRecord launchedActivity, boolean processPausingActivities, java.lang.String reason) {
        boolean displaySwapping = false;
        java.util.ArrayList<com.android.server.wm.ActivityRecord> readyToStopActivities = null;
        int i = 0;
        while (true) {
            if (i >= this.mStoppingActivities.size()) {
                break;
            }
            com.android.server.wm.ActivityRecord s = this.mStoppingActivities.get(i);
            if (s.getWrapper().getExtImpl().shouldMakeHomeActivityVisibleOnSecondary(s, getKeyguardController())) {
                android.util.Slog.d(TAG, "activity: " + s + "should be intercepted to stop state");
            } else {
                if ((s.isInTransition() || this.mActivityTaskSupervisorExt.skipStopLauncherWhenRemotePlaying(s)) && s.getTask() != null && !s.getTask().isForceHidden() && !s.getWrapper().getExtImpl().isForceHideByRemoveTask()) {
                    i = 1;
                }
                displaySwapping |= s.isDisplaySleepingAndSwapping();
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(s);
                    boolean protoLogParam1 = s.nowVisible;
                    java.lang.String protoLogParam3 = java.lang.String.valueOf(s.finishing);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 1496536241884839051L, 60, null, protoLogParam0, java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf((boolean) i), protoLogParam3);
                }
                if ((i == 0 && !displaySwapping) || this.mService.mShuttingDown || s.getRootTask().isForceHiddenForPinnedTask()) {
                    if (!processPausingActivities && s.isState(com.android.server.wm.ActivityRecord.State.PAUSING)) {
                        removeIdleTimeoutForActivity(launchedActivity);
                        scheduleIdleTimeout(launchedActivity);
                    } else {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                            java.lang.String protoLogParam02 = java.lang.String.valueOf(s);
                            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 5677125188685281770L, 0, null, protoLogParam02);
                        }
                        if (readyToStopActivities == null) {
                            readyToStopActivities = new java.util.ArrayList<>();
                        }
                        readyToStopActivities.add(s);
                        this.mStoppingActivities.remove(i);
                        i--;
                    }
                }
            }
            i++;
        }
        if (displaySwapping) {
            this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$processStoppingAndFinishingActivities$5();
                }
            }, 200L);
        }
        int numReadyStops = readyToStopActivities != null ? readyToStopActivities.size() : 0;
        for (int i2 = 0; i2 < numReadyStops; i2++) {
            com.android.server.wm.ActivityRecord r = readyToStopActivities.get(i2);
            if (r.isInHistory()) {
                if (r.finishing) {
                    r.destroyIfPossible(reason);
                } else {
                    r.stopIfPossible();
                }
            }
        }
        int numFinishingActivities = this.mFinishingActivities.size();
        if (numFinishingActivities == 0) {
            return;
        }
        java.util.ArrayList<com.android.server.wm.ActivityRecord> finishingActivities = new java.util.ArrayList<>(this.mFinishingActivities);
        this.mFinishingActivities.clear();
        for (int i3 = 0; i3 < numFinishingActivities; i3++) {
            com.android.server.wm.ActivityRecord r2 = finishingActivities.get(i3);
            if (r2.isInHistory()) {
                r2.destroyImmediately("finish-" + reason);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processStoppingAndFinishingActivities$5() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                scheduleProcessStoppingAndFinishingActivitiesIfNeeded();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void removeHistoryRecords(com.android.server.wm.WindowProcessController app) {
        removeHistoryRecords(this.mStoppingActivities, app, "mStoppingActivities");
        removeHistoryRecords(this.mFinishingActivities, app, "mFinishingActivities");
        removeHistoryRecords(this.mNoHistoryActivities, app, "mNoHistoryActivities");
    }

    private void removeHistoryRecords(java.util.ArrayList<com.android.server.wm.ActivityRecord> list, com.android.server.wm.WindowProcessController app, java.lang.String listName) {
        int i = list.size();
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
            android.util.Slog.v(com.android.server.wm.Task.TAG_CLEANUP, "Removing app " + this + " from list " + listName + " with " + i + " entries");
        }
        while (i > 0) {
            i--;
            com.android.server.wm.ActivityRecord r = list.get(i);
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
                android.util.Slog.v(com.android.server.wm.Task.TAG_CLEANUP, "Record #" + i + " " + r);
            }
            if (r.app == app) {
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
                    android.util.Slog.v(com.android.server.wm.Task.TAG_CLEANUP, "---> REMOVING this entry!");
                }
                list.remove(i);
                r.removeTimeouts();
            }
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println();
        pw.println("ActivityTaskSupervisor state:");
        this.mRootWindowContainer.dump(pw, prefix, true);
        getKeyguardController().dump(pw, prefix);
        this.mService.getLockTaskController().dump(pw, prefix);
        pw.print(prefix);
        pw.println("mCurTaskIdForUser=" + this.mCurTaskIdForUser);
        pw.println(prefix + "mUserRootTaskInFront=" + this.mRootWindowContainer.mUserRootTaskInFront);
        pw.println(prefix + "mVisibilityTransactionDepth=" + this.mVisibilityTransactionDepth);
        pw.print(prefix);
        pw.print("isHomeRecentsComponent=");
        pw.println(this.mRecentTasks.isRecentsComponentHomeActivity(this.mRootWindowContainer.mCurrentUser));
        if (!this.mWaitingActivityLaunched.isEmpty()) {
            pw.println(prefix + "mWaitingActivityLaunched=");
            for (int i = this.mWaitingActivityLaunched.size() - 1; i >= 0; i--) {
                this.mWaitingActivityLaunched.get(i).dump(pw, prefix + "  ");
            }
        }
        pw.println(prefix + "mNoHistoryActivities=" + this.mNoHistoryActivities);
        pw.println();
    }

    static boolean printThisActivity(java.io.PrintWriter pw, com.android.server.wm.ActivityRecord activity, java.lang.String dumpPackage, boolean needSep, java.lang.String prefix, java.lang.Runnable header) {
        return printThisActivity(pw, activity, dumpPackage, -1, needSep, prefix, header);
    }

    static boolean printThisActivity(java.io.PrintWriter pw, com.android.server.wm.ActivityRecord activity, java.lang.String dumpPackage, int displayIdFilter, boolean needSep, java.lang.String prefix, java.lang.Runnable header) {
        if (activity != null) {
            if (displayIdFilter == -1 || displayIdFilter == activity.getDisplayId()) {
                if (dumpPackage == null || dumpPackage.equals(activity.packageName)) {
                    if (needSep) {
                        pw.println();
                    }
                    if (header != null) {
                        header.run();
                    }
                    pw.print(prefix);
                    pw.println(activity);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    static boolean dumpHistoryList(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.util.List<com.android.server.wm.ActivityRecord> list, java.lang.String prefix, java.lang.String label, boolean complete, boolean brief, boolean client, java.lang.String dumpPackage, boolean needNL, java.lang.Runnable header, com.android.server.wm.Task lastTask) throws java.lang.Throwable {
        java.lang.Runnable header2 = header;
        com.android.server.wm.Task lastTask2 = lastTask;
        int i = list.size() - 1;
        boolean needNL2 = needNL;
        while (i >= 0) {
            com.android.server.wm.ActivityRecord r = list.get(i);
            int i2 = i;
            com.android.server.wm.ActivityRecord.dumpActivity(fd, pw, i, r, prefix, label, complete, brief, client, dumpPackage, needNL2, header2, lastTask2);
            lastTask2 = r.getTask();
            header2 = null;
            needNL2 = client && r.attachedToProcess();
            i = i2 - 1;
        }
        return false;
    }

    void scheduleIdleTimeout(com.android.server.wm.ActivityRecord next) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
            android.util.Slog.d(TAG_IDLE, "scheduleIdleTimeout: Callers=" + android.os.Debug.getCallers(4));
        }
        android.os.Message msg = this.mHandler.obtainMessage(200, next);
        this.mHandler.sendMessageDelayed(msg, IDLE_TIMEOUT);
    }

    final void scheduleIdle() {
        if (!this.mHandler.hasMessages(201)) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
                android.util.Slog.d(TAG_IDLE, "scheduleIdle: Callers=" + android.os.Debug.getCallers(4));
            }
            this.mHandler.sendEmptyMessage(201);
        }
    }

    com.android.server.wm.ActivityRecord updateTopResumedActivityIfNeeded(java.lang.String reason) {
        com.android.server.wm.ActivityRecord prevTopActivity = this.mTopResumedActivity;
        com.android.server.wm.Task topRootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask();
        if (topRootTask == null || topRootTask.getTopResumedActivity() == prevTopActivity || this.mActivityTaskSupervisorExt.skipUpdateResumedActivityIfNeeded(topRootTask, prevTopActivity, reason)) {
            if (topRootTask == null) {
                scheduleTopResumedActivityStateLossIfNeeded();
            }
            if (this.mService.isSleepingLocked()) {
                this.mService.updateTopApp(null);
            }
            return this.mTopResumedActivity;
        }
        scheduleTopResumedActivityStateLossIfNeeded();
        this.mTopResumedActivity = topRootTask.getTopResumedActivity();
        if (this.mTopResumedActivity != null && prevTopActivity != null) {
            if (this.mTopResumedActivity.app != null) {
                this.mTopResumedActivity.app.addToPendingTop();
            }
            this.mService.updateOomAdj();
        }
        if (this.mTopResumedActivity != null) {
            this.mService.setLastResumedActivityUncheckLocked(this.mTopResumedActivity, reason);
        }
        scheduleTopResumedActivityStateIfNeeded();
        if (this.mTopResumedActivity != null || this.mService.isSleepingLocked()) {
            this.mService.updateTopApp(this.mTopResumedActivity);
        }
        return this.mTopResumedActivity;
    }

    private void scheduleTopResumedActivityStateLossIfNeeded() {
        if (this.mTopResumedActivity != null && !this.mTopResumedActivityWaitingForPrev && this.mTopResumedActivity.scheduleTopResumedActivityChanged(false)) {
            scheduleTopResumedStateLossTimeout(this.mTopResumedActivity);
            this.mTopResumedActivityWaitingForPrev = true;
            this.mActivityTaskSupervisorExt.updateResumeLostActivity(this.mTopResumedActivity);
            this.mTopResumedActivity = null;
        }
    }

    private void scheduleTopResumedActivityStateIfNeeded() {
        if (this.mTopResumedActivity != null && !this.mTopResumedActivityWaitingForPrev) {
            com.android.server.wm.ActivityRecord topResume = this.mTopResumedActivity;
            this.mActivityTaskSupervisorExt.notifyAppSwitch(topResume, this.mService, this.mUserLeaving);
            topResume.scheduleTopResumedActivityChanged(true);
        }
    }

    private void scheduleTopResumedStateLossTimeout(com.android.server.wm.ActivityRecord r) {
        android.os.Message msg = this.mHandler.obtainMessage(217);
        msg.obj = r;
        r.topResumedStateLossTime = android.os.SystemClock.uptimeMillis();
        this.mHandler.sendMessageDelayed(msg, 500L);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(r);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 3604633008357193496L, 0, null, protoLogParam0);
        }
    }

    void handleTopResumedStateReleased(boolean timeout) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(timeout ? "(due to timeout)" : "(transition complete)");
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 3997062844427155487L, 0, null, protoLogParam0);
        }
        this.mHandler.removeMessages(217);
        if (!this.mTopResumedActivityWaitingForPrev) {
            return;
        }
        this.mTopResumedActivityWaitingForPrev = false;
        scheduleTopResumedActivityStateIfNeeded();
    }

    void removeIdleTimeoutForActivity(com.android.server.wm.ActivityRecord r) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
            android.util.Slog.d(TAG_IDLE, "removeTimeoutsForActivity: Callers=" + android.os.Debug.getCallers(4));
        }
        this.mHandler.removeMessages(200, r);
    }

    final void scheduleResumeTopActivities() {
        if (!this.mHandler.hasMessages(202)) {
            this.mHandler.sendEmptyMessage(202);
        }
    }

    void scheduleProcessStoppingAndFinishingActivitiesIfNeeded() {
        if (this.mStoppingActivities.isEmpty() && this.mFinishingActivities.isEmpty()) {
            return;
        }
        if (this.mRootWindowContainer.allResumedActivitiesIdle()) {
            scheduleIdle();
        } else if (!this.mHandler.hasMessages(205) && this.mRootWindowContainer.allResumedActivitiesVisible()) {
            this.mHandler.sendEmptyMessage(205);
        }
    }

    void removeSleepTimeouts() {
        this.mHandler.removeMessages(203);
    }

    final void scheduleSleepTimeout() {
        removeSleepTimeouts();
        this.mHandler.sendEmptyMessageDelayed(203, SLEEP_TIMEOUT);
    }

    boolean hasScheduledRestartTimeouts(com.android.server.wm.ActivityRecord r) {
        return this.mHandler.hasMessages(213, r);
    }

    void removeRestartTimeouts(com.android.server.wm.ActivityRecord r) {
        this.mHandler.removeMessages(213, r);
    }

    final void scheduleRestartTimeout(com.android.server.wm.ActivityRecord r) {
        removeRestartTimeouts(r);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(213, r), 2000L);
    }

    void handleNonResizableTaskIfNeeded(com.android.server.wm.Task task, int preferredWindowingMode, com.android.server.wm.TaskDisplayArea preferredTaskDisplayArea, com.android.server.wm.Task actualRootTask) {
        handleNonResizableTaskIfNeeded(task, preferredWindowingMode, preferredTaskDisplayArea, actualRootTask, false);
    }

    void handleNonResizableTaskIfNeeded(com.android.server.wm.Task task, int preferredWindowingMode, com.android.server.wm.TaskDisplayArea preferredTaskDisplayArea, com.android.server.wm.Task actualRootTask, boolean forceNonResizable) {
        boolean isSecondaryDisplayPreferred = (preferredTaskDisplayArea == null || preferredTaskDisplayArea.getDisplayId() == 0) ? false : true;
        if (!task.isActivityTypeStandardOrUndefined()) {
            return;
        }
        if (isSecondaryDisplayPreferred) {
            if (!task.canBeLaunchedOnDisplay(task.getDisplayId())) {
                throw new java.lang.IllegalStateException("Task resolved to incompatible display");
            }
            com.android.server.wm.DisplayContent preferredDisplay = preferredTaskDisplayArea.mDisplayContent;
            if (preferredDisplay != task.getDisplayContent()) {
                android.util.Slog.w(TAG, "Failed to put " + task + " on display " + preferredDisplay.mDisplayId);
                this.mService.getTaskChangeNotificationController().notifyActivityLaunchOnSecondaryDisplayFailed(task.getTaskInfo(), preferredDisplay.mDisplayId);
                return;
            } else {
                if (!forceNonResizable) {
                    handleForcedResizableTaskIfNeeded(task, 2);
                    return;
                }
                return;
            }
        }
        if (!forceNonResizable) {
            handleForcedResizableTaskIfNeeded(task, 1);
        }
    }

    private void handleForcedResizableTaskIfNeeded(com.android.server.wm.Task task, int reason) {
        com.android.server.wm.ActivityRecord topActivity = task.getTopNonFinishingActivity();
        if (topActivity == null || topActivity.noDisplay || !topActivity.canForceResizeNonResizable(task.getWindowingMode())) {
            return;
        }
        this.mService.getTaskChangeNotificationController().notifyActivityForcedResizable(task.mTaskId, reason, topActivity.info.applicationInfo.packageName);
    }

    void scheduleUpdateMultiWindowMode(com.android.server.wm.Task task) {
        task.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$scheduleUpdateMultiWindowMode$6((com.android.server.wm.ActivityRecord) obj);
            }
        });
        if (!this.mHandler.hasMessages(214)) {
            this.mHandler.sendEmptyMessage(214);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUpdateMultiWindowMode$6(com.android.server.wm.ActivityRecord r) {
        if (r.attachedToProcess()) {
            this.mMultiWindowModeChangedActivities.add(r);
        }
    }

    void scheduleUpdatePictureInPictureModeIfNeeded(com.android.server.wm.Task task, com.android.server.wm.Task prevRootTask) {
        com.android.server.wm.Task rootTask = task.getRootTask();
        if (prevRootTask != null) {
            if (prevRootTask != rootTask && !prevRootTask.inPinnedWindowingMode() && !rootTask.inPinnedWindowingMode()) {
                return;
            }
            scheduleUpdatePictureInPictureModeIfNeeded(task, rootTask.getRequestedOverrideBounds());
        }
    }

    void scheduleUpdatePictureInPictureModeIfNeeded(com.android.server.wm.Task task, android.graphics.Rect targetRootTaskBounds) {
        task.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityTaskSupervisor$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$scheduleUpdatePictureInPictureModeIfNeeded$7((com.android.server.wm.ActivityRecord) obj);
            }
        });
        this.mPipModeChangedTargetRootTaskBounds = targetRootTaskBounds;
        if (!this.mHandler.hasMessages(215)) {
            this.mHandler.sendEmptyMessage(215);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleUpdatePictureInPictureModeIfNeeded$7(com.android.server.wm.ActivityRecord r) {
        if (r.attachedToProcess()) {
            this.mPipModeChangedActivities.add(r);
            this.mMultiWindowModeChangedActivities.remove(r);
        }
    }

    void wakeUp(java.lang.String reason) {
        this.mPowerManager.wakeUp(android.os.SystemClock.uptimeMillis(), 2, "android.server.am:TURN_ON:" + reason);
    }

    void beginActivityVisibilityUpdate() {
        if (this.mVisibilityTransactionDepth == 0) {
            getKeyguardController().updateVisibility();
        }
        this.mVisibilityTransactionDepth++;
    }

    void endActivityVisibilityUpdate() {
        this.mVisibilityTransactionDepth--;
        if (this.mVisibilityTransactionDepth == 0) {
            computeProcessActivityStateBatch();
        }
    }

    boolean inActivityVisibilityUpdate() {
        return this.mVisibilityTransactionDepth > 0;
    }

    void setDeferRootVisibilityUpdate(boolean deferUpdate) {
        this.mDeferRootVisibilityUpdate = deferUpdate;
    }

    boolean isRootVisibilityUpdateDeferred() {
        return this.mDeferRootVisibilityUpdate;
    }

    void onProcessActivityStateChanged(com.android.server.wm.WindowProcessController wpc, boolean forceBatch) {
        if (forceBatch || inActivityVisibilityUpdate()) {
            if (!this.mActivityStateChangedProcs.contains(wpc)) {
                this.mActivityStateChangedProcs.add(wpc);
                return;
            }
            return;
        }
        wpc.computeProcessActivityState();
    }

    void computeProcessActivityStateBatch() {
        if (this.mActivityStateChangedProcs.isEmpty()) {
            return;
        }
        for (int i = this.mActivityStateChangedProcs.size() - 1; i >= 0; i--) {
            this.mActivityStateChangedProcs.get(i).computeProcessActivityState();
        }
        this.mActivityStateChangedProcs.clear();
    }

    void beginDeferResume() {
        this.mDeferResumeCount++;
    }

    void endDeferResume() {
        this.mDeferResumeCount--;
    }

    boolean readyToResume() {
        return this.mDeferResumeCount == 0;
    }

    private final class ActivityTaskSupervisorHandler extends android.os.Handler {
        ActivityTaskSupervisorHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityTaskSupervisor.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (handleMessageInner(msg)) {
                        return;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    switch (msg.what) {
                        case 213:
                            com.android.server.wm.ActivityRecord r = (com.android.server.wm.ActivityRecord) msg.obj;
                            java.lang.String processName = null;
                            int uid = 0;
                            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = com.android.server.wm.ActivityTaskSupervisor.this.mService.mGlobalLock;
                            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                            synchronized (windowManagerGlobalLock2) {
                                try {
                                    if (r.attachedToProcess() && r.isState(com.android.server.wm.ActivityRecord.State.RESTARTING_PROCESS)) {
                                        processName = r.app.mName;
                                        uid = r.app.mUid;
                                    }
                                } finally {
                                }
                                break;
                            }
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            if (processName != null) {
                                com.android.server.wm.ActivityTaskSupervisor.this.mService.mAmInternal.killProcess(processName, uid, "restartActivityProcessTimeout");
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                } finally {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            }
        }

        private void activityIdleFromMessage(com.android.server.wm.ActivityRecord idleActivity, boolean fromTimeout) {
            com.android.server.wm.ActivityTaskSupervisor.this.activityIdleInternal(idleActivity, fromTimeout, fromTimeout, null);
        }

        private boolean handleMessageInner(android.os.Message msg) {
            switch (msg.what) {
                case 200:
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
                        android.util.Slog.d(com.android.server.wm.ActivityTaskSupervisor.TAG_IDLE, "handleMessage: IDLE_TIMEOUT_MSG: r=" + msg.obj);
                    }
                    activityIdleFromMessage((com.android.server.wm.ActivityRecord) msg.obj, true);
                    return true;
                case 201:
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_IDLE) {
                        android.util.Slog.d(com.android.server.wm.ActivityTaskSupervisor.TAG_IDLE, "handleMessage: IDLE_NOW_MSG: r=" + msg.obj);
                    }
                    activityIdleFromMessage((com.android.server.wm.ActivityRecord) msg.obj, false);
                    return true;
                case 202:
                    com.android.server.wm.ActivityTaskSupervisor.this.mRootWindowContainer.resumeFocusedTasksTopActivities();
                    return true;
                case 203:
                    if (com.android.server.wm.ActivityTaskSupervisor.this.mService.isSleepingOrShuttingDownLocked()) {
                        android.util.Slog.w(com.android.server.wm.ActivityTaskSupervisor.TAG, "Sleep timeout!  Sleeping now.");
                        com.android.server.wm.ActivityTaskSupervisor.this.checkReadyForSleepLocked(false);
                    }
                    return true;
                case 204:
                    if (com.android.server.wm.ActivityTaskSupervisor.this.mLaunchingActivityWakeLock.isHeld()) {
                        android.util.Slog.w(com.android.server.wm.ActivityTaskSupervisor.TAG, "Launch timeout has expired, giving up wake lock!");
                        com.android.server.wm.ActivityTaskSupervisor.this.mActivityTaskSupervisorExt.appLaunchTimeout(com.android.server.wm.ActivityTaskSupervisor.this.mRootWindowContainer, com.android.server.wm.ActivityTaskSupervisor.this.mService.mContext);
                        com.android.server.wm.ActivityTaskSupervisor.this.mLaunchingActivityWakeLock.release();
                    }
                    return true;
                case 205:
                    com.android.server.wm.ActivityTaskSupervisor.this.processStoppingAndFinishingActivities(null, false, "transit");
                    return true;
                case 206:
                    com.android.server.wm.Task task = (com.android.server.wm.Task) msg.obj;
                    if (task.mKillProcessesOnDestroyed && task.hasActivity()) {
                        android.util.Slog.i(com.android.server.wm.ActivityTaskSupervisor.TAG, "Destroy timeout of remove-task, attempt to kill " + task);
                        com.android.server.wm.ActivityTaskSupervisor.this.killTaskProcessesIfPossible(task);
                    }
                    return true;
                case 207:
                case 208:
                case 209:
                case 210:
                case com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__ROLE_HOLDER_UPDATER_UPDATE_RETRY /* 211 */:
                case 213:
                default:
                    return false;
                case 212:
                    com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked((android.os.IBinder) msg.obj);
                    if (r != null) {
                        com.android.server.wm.ActivityTaskSupervisor.this.handleLaunchTaskBehindCompleteLocked(r);
                    }
                    return true;
                case 214:
                    for (int i = com.android.server.wm.ActivityTaskSupervisor.this.mMultiWindowModeChangedActivities.size() - 1; i >= 0; i--) {
                        ((com.android.server.wm.ActivityRecord) com.android.server.wm.ActivityTaskSupervisor.this.mMultiWindowModeChangedActivities.remove(i)).updateMultiWindowMode();
                    }
                    return true;
                case 215:
                    for (int i2 = com.android.server.wm.ActivityTaskSupervisor.this.mPipModeChangedActivities.size() - 1; i2 >= 0; i2--) {
                        ((com.android.server.wm.ActivityRecord) com.android.server.wm.ActivityTaskSupervisor.this.mPipModeChangedActivities.remove(i2)).updatePictureInPictureMode(com.android.server.wm.ActivityTaskSupervisor.this.mPipModeChangedTargetRootTaskBounds, false);
                    }
                    return true;
                case 216:
                    com.android.server.wm.ActivityTaskSupervisor.this.mHandler.removeMessages(216);
                    com.android.server.wm.ActivityTaskSupervisor.this.mRootWindowContainer.startHomeOnEmptyDisplays((java.lang.String) msg.obj);
                    return true;
                case 217:
                    com.android.server.wm.ActivityRecord r2 = (com.android.server.wm.ActivityRecord) msg.obj;
                    android.util.Slog.w(com.android.server.wm.ActivityTaskSupervisor.TAG, "Activity top resumed state loss timeout for " + r2);
                    if (r2.hasProcess()) {
                        com.android.server.wm.ActivityTaskSupervisor.this.mService.logAppTooSlow(r2.app, r2.topResumedStateLossTime, "top state loss for " + r2);
                    }
                    com.android.server.wm.ActivityTaskSupervisor.this.handleTopResumedStateReleased(true);
                    return true;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:113:0x020c A[Catch: all -> 0x0255, TRY_ENTER, TryCatch #12 {all -> 0x0255, blocks: (B:113:0x020c, B:114:0x0211, B:131:0x024f), top: B:229:0x0157 }] */
    /* JADX WARN: Removed duplicated region for block: B:212:0x02ef A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x01ae A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int startActivityFromRecents(int r28, int r29, int r30, com.android.server.wm.SafeActivityOptions r31) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 995
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityTaskSupervisor.startActivityFromRecents(int, int, int, com.android.server.wm.SafeActivityOptions):int");
    }

    static class OpaqueActivityHelper implements java.util.function.Predicate<com.android.server.wm.ActivityRecord> {
        private boolean mIgnoringKeyguard;
        private boolean mIncludeInvisibleAndFinishing;
        private com.android.server.wm.ActivityRecord mStarting;

        OpaqueActivityHelper() {
        }

        com.android.server.wm.ActivityRecord getOpaqueActivity(com.android.server.wm.WindowContainer<?> container, boolean ignoringKeyguard) {
            this.mIncludeInvisibleAndFinishing = true;
            this.mIgnoringKeyguard = ignoringKeyguard;
            return container.getActivity(this, true, null);
        }

        com.android.server.wm.ActivityRecord getVisibleOpaqueActivity(com.android.server.wm.WindowContainer<?> container, com.android.server.wm.ActivityRecord starting, boolean ignoringKeyguard) {
            this.mStarting = starting;
            this.mIncludeInvisibleAndFinishing = false;
            this.mIgnoringKeyguard = ignoringKeyguard;
            com.android.server.wm.ActivityRecord opaque = container.getActivity(this, true, null);
            this.mStarting = null;
            return opaque;
        }

        @Override // java.util.function.Predicate
        public boolean test(com.android.server.wm.ActivityRecord r) {
            if (!this.mIncludeInvisibleAndFinishing && r != this.mStarting) {
                if (!this.mIgnoringKeyguard || r.visibleIgnoringKeyguard) {
                    if (!this.mIgnoringKeyguard && !r.isVisible()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return r.occludesParent(this.mIncludeInvisibleAndFinishing);
        }
    }

    static class TaskInfoHelper implements java.util.function.Consumer<com.android.server.wm.ActivityRecord> {
        private android.app.TaskInfo mInfo;
        private com.android.server.wm.ActivityRecord mTopRunning;

        TaskInfoHelper() {
        }

        com.android.server.wm.ActivityRecord fillAndReturnTop(com.android.server.wm.Task task, android.app.TaskInfo info) {
            info.numActivities = 0;
            info.baseActivity = null;
            this.mInfo = info;
            task.forAllActivities(this);
            com.android.server.wm.ActivityRecord top = this.mTopRunning;
            this.mTopRunning = null;
            this.mInfo = null;
            return top;
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.wm.ActivityRecord r) {
            if (r.mLaunchCookie != null) {
                this.mInfo.addLaunchCookie(r.mLaunchCookie);
            }
            if (r.finishing) {
                return;
            }
            this.mInfo.numActivities++;
            this.mInfo.baseActivity = r.mActivityComponent;
            if (this.mTopRunning == null) {
                this.mTopRunning = r;
            }
        }
    }

    private static class WaitInfo {
        final com.android.server.wm.ActivityMetricsLogger.LaunchingState mLaunchingState;
        final android.app.WaitResult mResult;
        final android.content.ComponentName mTargetComponent;

        WaitInfo(android.app.WaitResult result, android.content.ComponentName component, com.android.server.wm.ActivityMetricsLogger.LaunchingState launchingState) {
            this.mResult = result;
            this.mTargetComponent = component;
            this.mLaunchingState = launchingState;
        }

        boolean matches(com.android.server.wm.ActivityRecord r) {
            if (!this.mLaunchingState.hasActiveTransitionInfo()) {
                return this.mTargetComponent.equals(r.mActivityComponent);
            }
            return this.mLaunchingState.contains(r);
        }

        void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "WaitInfo:");
            pw.println(prefix + "  mTargetComponent=" + this.mTargetComponent);
            pw.println(prefix + "  mResult=");
            this.mResult.dump(pw, prefix + "    ");
        }
    }

    public com.android.server.wm.IActivityTaskSupervisorWrapper getWrapper() {
        return this.mATSWrapper;
    }

    private class ActivityTaskSupervisorWrapper implements com.android.server.wm.IActivityTaskSupervisorWrapper {
        private ActivityTaskSupervisorWrapper() {
        }

        @Override // com.android.server.wm.IActivityTaskSupervisorWrapper
        public com.android.server.wm.IActivityTaskSupervisorExt getExtImpl() {
            return com.android.server.wm.ActivityTaskSupervisor.this.mActivityTaskSupervisorExt;
        }

        @Override // com.android.server.wm.IActivityTaskSupervisorWrapper
        public android.content.pm.ResolveInfo resolveIntent(android.content.Intent intent, java.lang.String resolvedType, int userId, int flags, int filterCallingUid, int callingPid) {
            return com.android.server.wm.ActivityTaskSupervisor.this.resolveIntent(intent, resolvedType, userId, flags, filterCallingUid, callingPid);
        }
    }
}
