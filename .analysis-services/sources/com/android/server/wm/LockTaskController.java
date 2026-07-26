package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class LockTaskController {
    static final int LOCK_TASK_AUTH_ALLOWLISTED = 3;
    static final int LOCK_TASK_AUTH_DONT_LOCK = 0;
    static final int LOCK_TASK_AUTH_LAUNCHABLE = 2;
    static final int LOCK_TASK_AUTH_LAUNCHABLE_PRIV = 4;
    static final int LOCK_TASK_AUTH_PINNABLE = 1;
    private static final java.lang.String LOCK_TASK_TAG = "Lock-to-App";
    static final int STATUS_BAR_MASK_LOCKED = 128319488;
    static final int STATUS_BAR_MASK_PINNED = 111083520;
    private final android.content.Context mContext;
    android.app.admin.IDevicePolicyManager mDevicePolicyManager;
    private final android.os.Handler mHandler;
    com.android.internal.widget.LockPatternUtils mLockPatternUtils;
    com.android.internal.statusbar.IStatusBarService mStatusBarService;
    private final com.android.server.wm.ActivityTaskSupervisor mSupervisor;
    private final com.android.server.wm.TaskChangeNotificationController mTaskChangeNotificationController;
    android.telecom.TelecomManager mTelecomManager;
    com.android.server.wm.WindowManagerService mWindowManager;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static final java.lang.String TAG_LOCKTASK = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_LOCKTASK;
    private static final android.util.SparseArray<android.util.Pair<java.lang.Integer, java.lang.Integer>> STATUS_BAR_FLAG_MAP_LOCKED = new android.util.SparseArray<>();
    private final android.os.IBinder mToken = new com.android.server.wm.LockTaskController.LockTaskToken();
    public com.android.server.wm.ILockTaskControllerExt mLockTaskControllerExt = (com.android.server.wm.ILockTaskControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ILockTaskControllerExt.class).base(this).create();
    private final java.util.ArrayList<com.android.server.wm.Task> mLockTaskModeTasks = new java.util.ArrayList<>();
    private final android.util.SparseArray<java.lang.String[]> mLockTaskPackages = new android.util.SparseArray<>();
    private final android.util.SparseIntArray mLockTaskFeatures = new android.util.SparseIntArray();
    private volatile int mLockTaskModeState = 0;
    private int mPendingDisableFromDismiss = -10000;

    static {
        STATUS_BAR_FLAG_MAP_LOCKED.append(1, new android.util.Pair<>(8388608, 2));
        STATUS_BAR_FLAG_MAP_LOCKED.append(2, new android.util.Pair<>(393216, 4));
        STATUS_BAR_FLAG_MAP_LOCKED.append(4, new android.util.Pair<>(2097152, 0));
        STATUS_BAR_FLAG_MAP_LOCKED.append(8, new android.util.Pair<>(16777216, 0));
        STATUS_BAR_FLAG_MAP_LOCKED.append(16, new android.util.Pair<>(0, 8));
    }

    LockTaskController(android.content.Context context, com.android.server.wm.ActivityTaskSupervisor supervisor, android.os.Handler handler, com.android.server.wm.TaskChangeNotificationController taskChangeNotificationController) {
        this.mContext = context;
        this.mSupervisor = supervisor;
        this.mHandler = handler;
        this.mTaskChangeNotificationController = taskChangeNotificationController;
        this.mLockTaskControllerExt.init(this.mContext, this.mSupervisor, this, this.mLockTaskModeTasks, this.mLockTaskPackages);
    }

    void setWindowManager(com.android.server.wm.WindowManagerService windowManager) {
        this.mWindowManager = windowManager;
    }

    int getLockTaskModeState() {
        return this.mLockTaskModeState;
    }

    boolean isTaskLocked(com.android.server.wm.Task task) {
        return this.mLockTaskModeTasks.contains(task);
    }

    private boolean isRootTask(com.android.server.wm.Task task) {
        return this.mLockTaskModeTasks.indexOf(task) == 0;
    }

    boolean activityBlockedFromFinish(final com.android.server.wm.ActivityRecord activity) {
        com.android.server.wm.Task task = activity.getTask();
        if (task.mLockTaskAuth == 4 || !isRootTask(task)) {
            return false;
        }
        com.android.server.wm.ActivityRecord taskTop = task.getTopNonFinishingActivity();
        com.android.server.wm.ActivityRecord taskRoot = task.getRootActivity();
        if (activity != taskRoot || activity != taskTop) {
            com.android.server.wm.TaskFragment taskFragment = activity.getTaskFragment();
            final com.android.server.wm.TaskFragment adjacentTaskFragment = taskFragment.getAdjacentTaskFragment();
            if (taskFragment.asTask() != null || !taskFragment.isDelayLastActivityRemoval() || adjacentTaskFragment == null) {
                return false;
            }
            boolean hasOtherActivityInTaskFragment = taskFragment.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.LockTaskController$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.LockTaskController.lambda$activityBlockedFromFinish$0(activity, (com.android.server.wm.ActivityRecord) obj);
                }
            }) != null;
            if (hasOtherActivityInTaskFragment) {
                return false;
            }
            boolean hasOtherActivityInTask = task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.LockTaskController$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.LockTaskController.lambda$activityBlockedFromFinish$1(activity, adjacentTaskFragment, (com.android.server.wm.ActivityRecord) obj);
                }
            }) != null;
            if (hasOtherActivityInTask) {
                return false;
            }
        }
        android.util.Slog.i(TAG, "Not finishing task in lock task mode");
        showLockTaskToast();
        return true;
    }

    static /* synthetic */ boolean lambda$activityBlockedFromFinish$0(com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord a) {
        return (a.finishing || a == activity) ? false : true;
    }

    static /* synthetic */ boolean lambda$activityBlockedFromFinish$1(com.android.server.wm.ActivityRecord activity, com.android.server.wm.TaskFragment adjacentTaskFragment, com.android.server.wm.ActivityRecord a) {
        return (a.finishing || a == activity || a.getTaskFragment() == adjacentTaskFragment) ? false : true;
    }

    boolean canMoveTaskToBack(com.android.server.wm.Task task) {
        if (isRootTask(task)) {
            showLockTaskToast();
            return false;
        }
        return true;
    }

    static boolean isTaskAuthAllowlisted(int lockTaskAuth) {
        switch (lockTaskAuth) {
            case 2:
            case 3:
            case 4:
                return true;
            default:
                return false;
        }
    }

    boolean isLockTaskModeViolation(com.android.server.wm.Task task) {
        return isLockTaskModeViolation(task, false);
    }

    boolean isLockTaskModeViolation(com.android.server.wm.Task task, boolean isNewClearTask) {
        if ((!isTaskLocked(task) || isNewClearTask) && isLockTaskModeViolationInternal(task, task.mUserId, task.intent, task.mLockTaskAuth)) {
            showLockTaskToast();
            return true;
        }
        return false;
    }

    boolean isNewTaskLockTaskModeViolation(com.android.server.wm.ActivityRecord activity) {
        if (activity.getTask() != null) {
            return isLockTaskModeViolation(activity.getTask());
        }
        int auth = getLockTaskAuth(activity, null);
        if (isLockTaskModeViolationInternal(activity, activity.mUserId, activity.intent, auth)) {
            showLockTaskToast();
            return true;
        }
        return false;
    }

    com.android.server.wm.Task getRootTask() {
        if (this.mLockTaskModeTasks.isEmpty()) {
            return null;
        }
        return this.mLockTaskModeTasks.get(0);
    }

    private boolean isLockTaskModeViolationInternal(com.android.server.wm.WindowContainer wc, int userId, android.content.Intent intent, int taskAuth) {
        if (this.mLockTaskControllerExt.isLockDeviceMode()) {
            return this.mLockTaskControllerExt.isLockTaskModeViolationInternal(wc, taskAuth);
        }
        if (wc.isActivityTypeRecents() && isRecentsAllowed(userId)) {
            return false;
        }
        return ((isKeyguardAllowed(userId) && isEmergencyCallIntent(intent)) || wc.isActivityTypeDream() || isWirelessEmergencyAlert(intent) || isTaskAuthAllowlisted(taskAuth) || this.mLockTaskModeTasks.isEmpty()) ? false : true;
    }

    private boolean isRecentsAllowed(int userId) {
        return (getLockTaskFeaturesForUser(userId) & 8) != 0;
    }

    private boolean isKeyguardAllowed(int userId) {
        return (getLockTaskFeaturesForUser(userId) & 32) != 0;
    }

    private boolean isBlockingInTaskEnabled(int userId) {
        return (getLockTaskFeaturesForUser(userId) & 64) != 0;
    }

    boolean isActivityAllowed(int userId, java.lang.String packageName, int lockTaskLaunchMode) {
        if (this.mLockTaskModeState != 1 || !isBlockingInTaskEnabled(userId)) {
            return true;
        }
        switch (lockTaskLaunchMode) {
            case 1:
                return false;
            case 2:
                return true;
            default:
                return isPackageAllowlisted(userId, packageName);
        }
    }

    private boolean isWirelessEmergencyAlert(android.content.Intent intent) {
        android.content.ComponentName cellBroadcastAlertDialogComponentName;
        if (intent == null || (cellBroadcastAlertDialogComponentName = com.android.internal.telephony.CellBroadcastUtils.getDefaultCellBroadcastAlertDialogComponent(this.mContext)) == null || !cellBroadcastAlertDialogComponentName.equals(intent.getComponent())) {
            return false;
        }
        return true;
    }

    private boolean isEmergencyCallIntent(android.content.Intent intent) {
        if (intent == null) {
            return false;
        }
        if (android.telecom.TelecomManager.EMERGENCY_DIALER_COMPONENT.equals(intent.getComponent()) || "android.intent.action.CALL_EMERGENCY".equals(intent.getAction())) {
            return true;
        }
        android.telecom.TelecomManager tm = getTelecomManager();
        java.lang.String dialerPackage = tm != null ? tm.getSystemDialerPackage() : null;
        return dialerPackage != null && dialerPackage.equals(intent.getComponent().getPackageName());
    }

    void stopLockTaskMode(com.android.server.wm.Task task, boolean stopAppPinning, int callingUid) {
        if (this.mLockTaskModeState == 0 || this.mLockTaskControllerExt.isLockDeviceMode()) {
            return;
        }
        if (stopAppPinning) {
            if (this.mLockTaskModeState == 2) {
                clearLockedTasks("stopAppPinning");
                return;
            } else {
                android.util.Slog.e(TAG_LOCKTASK, "Attempted to stop app pinning while fully locked");
                showLockTaskToast();
                return;
            }
        }
        if (task == null) {
            throw new java.lang.IllegalArgumentException("can't stop LockTask for null task");
        }
        if (callingUid != task.mLockTaskUid && (task.mLockTaskUid != 0 || callingUid != task.effectiveUid)) {
            throw new java.lang.SecurityException("Invalid uid, expected " + task.mLockTaskUid + " callingUid=" + callingUid + " effectiveUid=" + task.effectiveUid);
        }
        clearLockedTask(task);
    }

    void clearLockedTasks(java.lang.String reason) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(reason);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, 8891808212671675155L, 0, null, protoLogParam0);
        }
        if (!this.mLockTaskModeTasks.isEmpty()) {
            clearLockedTask(this.mLockTaskModeTasks.get(0));
        }
    }

    void clearLockedTask(com.android.server.wm.Task task) {
        if (task == null || this.mLockTaskModeTasks.isEmpty()) {
            return;
        }
        if (task == this.mLockTaskModeTasks.get(0)) {
            for (int taskNdx = this.mLockTaskModeTasks.size() - 1; taskNdx > 0; taskNdx--) {
                clearLockedTask(this.mLockTaskModeTasks.get(taskNdx));
            }
        }
        removeLockedTask(task);
        if (this.mLockTaskModeTasks.isEmpty()) {
            return;
        }
        task.performClearTaskForReuse(false);
        this.mSupervisor.mRootWindowContainer.resumeFocusedTasksTopActivities();
    }

    private void removeLockedTask(final com.android.server.wm.Task task) {
        if (!this.mLockTaskModeTasks.remove(task)) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(task);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, 8970634498594714645L, 0, null, protoLogParam0);
        }
        if (this.mLockTaskModeTasks.isEmpty()) {
            if (this.mLockTaskControllerExt.isLockDeviceMode()) {
                this.mLockTaskControllerExt.stopLockDeviceModeBySystem();
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[0]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(task);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, 8735562128135241598L, 0, null, protoLogParam02, protoLogParam1);
            }
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.LockTaskController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$removeLockedTask$2(task);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeLockedTask$2(com.android.server.wm.Task task) {
        performStopLockTask(task.mUserId);
    }

    private void performStopLockTask(int userId) {
        com.android.internal.statusbar.IStatusBarService statusBarService;
        int oldLockTaskModeState = this.mLockTaskModeState;
        this.mLockTaskModeState = 0;
        this.mTaskChangeNotificationController.notifyLockTaskModeChanged(this.mLockTaskModeState);
        try {
            setStatusBarState(this.mLockTaskModeState, userId);
            setKeyguardState(this.mLockTaskModeState, userId);
            if (oldLockTaskModeState == 2) {
                lockKeyguardIfNeeded(userId);
            }
            if (getDevicePolicyManager() != null) {
                getDevicePolicyManager().notifyLockTaskModeChanged(false, (java.lang.String) null, userId);
            }
            if (oldLockTaskModeState == 2 && (statusBarService = getStatusBarService()) != null) {
                statusBarService.showPinningEnterExitToast(false);
            }
            this.mWindowManager.onLockTaskStateChanged(this.mLockTaskModeState);
        } catch (android.os.RemoteException ex) {
            throw new java.lang.RuntimeException(ex);
        }
    }

    void showLockTaskToast() {
        if (this.mLockTaskModeState == 2) {
            try {
                com.android.internal.statusbar.IStatusBarService statusBarService = getStatusBarService();
                if (statusBarService != null) {
                    statusBarService.showPinningEscapeToast();
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to send pinning escape toast", e);
            }
        }
    }

    void startLockTaskMode(com.android.server.wm.Task task, boolean isSystemCaller, int callingUid) throws java.lang.Throwable {
        if (task.mLockTaskAuth == 0) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[3]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, 737192738184050156L, 0, null, null);
                return;
            }
            return;
        }
        if (!isSystemCaller) {
            task.mLockTaskUid = callingUid;
            if (task.mLockTaskAuth != 1) {
                if (this.mLockTaskModeState == 2) {
                    android.util.Slog.i(TAG, "Stop app pinning before entering full lock task mode");
                    stopLockTaskMode(null, true, callingUid);
                }
            } else {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[3]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, -7119521978513736788L, 0, null, null);
                }
                com.android.server.statusbar.StatusBarManagerInternal statusBarManager = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
                if (statusBarManager != null) {
                    statusBarManager.showScreenPinningRequest(task.mTaskId);
                    return;
                }
                return;
            }
        }
        this.mSupervisor.mRootWindowContainer.removeRootTasksInWindowingModes(2);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[3]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(isSystemCaller ? "Locking pinned" : "Locking fully");
            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, -1557441750657584614L, 0, null, protoLogParam0);
        }
        setLockTaskMode(task, isSystemCaller ? 2 : 1, "startLockTask", true);
    }

    private void setLockTaskMode(final com.android.server.wm.Task task, final int lockTaskModeState, java.lang.String reason, boolean andResume) throws java.lang.Throwable {
        if (task.mLockTaskAuth == 0) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[3]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, -4314079913933391851L, 0, null, null);
                return;
            }
            return;
        }
        if (this.mLockTaskControllerExt.setLockTaskMode(this.mLockTaskModeTasks, task)) {
            android.util.Slog.w(TAG_LOCKTASK, "setLockTaskMode: ignore add lock task to LockTaskModeTasks");
            return;
        }
        if (isLockTaskModeViolation(task)) {
            android.util.Slog.e(TAG_LOCKTASK, "setLockTaskMode: Attempt to start an unauthorized lock task.");
            return;
        }
        final android.content.Intent taskIntent = task.intent;
        if (this.mLockTaskModeTasks.isEmpty() && taskIntent != null) {
            this.mSupervisor.mRecentTasks.onLockTaskModeStateChanged(lockTaskModeState, task.mUserId);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.LockTaskController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setLockTaskMode$3(taskIntent, task, lockTaskModeState);
                }
            });
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[3]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(task);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(android.os.Debug.getCallers(4));
            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, 3321878763832425380L, 0, null, protoLogParam0, protoLogParam1);
        }
        if (!this.mLockTaskModeTasks.contains(task)) {
            this.mLockTaskModeTasks.add(task);
        }
        if (task.mLockTaskUid == -1) {
            task.mLockTaskUid = task.effectiveUid;
        }
        if (andResume) {
            this.mSupervisor.findTaskToMoveToFront(task, 0, null, reason, lockTaskModeState != 0);
            this.mSupervisor.mRootWindowContainer.resumeFocusedTasksTopActivities();
            com.android.server.wm.Task rootTask = task.getRootTask();
            if (rootTask != null) {
                rootTask.mDisplayContent.executeAppTransition();
                return;
            }
            return;
        }
        if (lockTaskModeState != 0) {
            this.mSupervisor.handleNonResizableTaskIfNeeded(task, 0, this.mSupervisor.mRootWindowContainer.getDefaultTaskDisplayArea(), task.getRootTask(), true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setLockTaskMode$3(android.content.Intent taskIntent, com.android.server.wm.Task task, int lockTaskModeState) {
        performStartLockTask(taskIntent.getComponent().getPackageName(), task.mUserId, lockTaskModeState);
    }

    private void performStartLockTask(java.lang.String packageName, int userId, int lockTaskModeState) {
        if (lockTaskModeState == 2) {
            try {
                com.android.internal.statusbar.IStatusBarService statusBarService = getStatusBarService();
                if (statusBarService != null) {
                    statusBarService.showPinningEnterExitToast(true);
                }
            } catch (android.os.RemoteException ex) {
                throw new java.lang.RuntimeException(ex);
            }
        }
        this.mWindowManager.onLockTaskStateChanged(lockTaskModeState);
        this.mLockTaskModeState = lockTaskModeState;
        this.mTaskChangeNotificationController.notifyLockTaskModeChanged(this.mLockTaskModeState);
        setStatusBarState(lockTaskModeState, userId);
        setKeyguardState(lockTaskModeState, userId);
        if (getDevicePolicyManager() != null) {
            getDevicePolicyManager().notifyLockTaskModeChanged(true, packageName, userId);
        }
    }

    void updateLockTaskPackages(int userId, java.lang.String[] packages) throws java.lang.Throwable {
        if (this.mLockTaskControllerExt.isLockDeviceMode()) {
            return;
        }
        this.mLockTaskPackages.put(userId, packages);
        boolean taskChanged = false;
        for (int taskNdx = this.mLockTaskModeTasks.size() - 1; taskNdx >= 0; taskNdx--) {
            com.android.server.wm.Task lockedTask = this.mLockTaskModeTasks.get(taskNdx);
            boolean wasAllowlisted = lockedTask.mLockTaskAuth == 2 || lockedTask.mLockTaskAuth == 3;
            lockedTask.setLockTaskAuth();
            boolean isAllowlisted = lockedTask.mLockTaskAuth == 2 || lockedTask.mLockTaskAuth == 3;
            if (this.mLockTaskModeState == 1 && lockedTask.mUserId == userId && wasAllowlisted && !isAllowlisted) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(lockedTask);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(lockedTask.lockTaskAuthToString());
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, -4819015209006579825L, 0, null, protoLogParam0, protoLogParam1);
                }
                removeLockedTask(lockedTask);
                lockedTask.performClearTaskForReuse(false);
                taskChanged = true;
            }
        }
        this.mSupervisor.mRootWindowContainer.forAllTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.LockTaskController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.Task) obj).setLockTaskAuth();
            }
        });
        com.android.server.wm.ActivityRecord r = this.mSupervisor.mRootWindowContainer.topRunningActivity();
        com.android.server.wm.Task task = r != null ? r.getTask() : null;
        if (this.mLockTaskModeTasks.isEmpty() && task != null && task.mLockTaskAuth == 2) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[0]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(task);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, 2119751067469297845L, 0, null, protoLogParam02);
            }
            setLockTaskMode(task, 1, "package updated", false);
            taskChanged = true;
        }
        if (taskChanged) {
            this.mSupervisor.mRootWindowContainer.resumeFocusedTasksTopActivities();
        }
    }

    int getLockTaskAuth(com.android.server.wm.ActivityRecord rootActivity, com.android.server.wm.Task task) {
        java.lang.String pkg;
        if (rootActivity == null && task == null) {
            return 0;
        }
        int i = 1;
        if (rootActivity == null) {
            return 1;
        }
        if (task == null || task.realActivity == null) {
            pkg = rootActivity.packageName;
        } else {
            pkg = task.realActivity.getPackageName();
        }
        int userId = task != null ? task.mUserId : rootActivity.mUserId;
        switch (rootActivity.lockTaskLaunchMode) {
            case 0:
                if (isPackageAllowlisted(userId, pkg)) {
                    i = 3;
                }
                int lockTaskAuth = i;
                return lockTaskAuth;
            case 1:
                return 0;
            case 2:
                return 4;
            case 3:
                if (isPackageAllowlisted(userId, pkg)) {
                    i = 2;
                }
                int lockTaskAuth2 = i;
                return lockTaskAuth2;
            default:
                return 0;
        }
    }

    boolean isPackageAllowlisted(int userId, java.lang.String pkg) {
        java.lang.String[] allowlist;
        if (pkg == null || (allowlist = this.mLockTaskPackages.get(userId)) == null) {
            return false;
        }
        for (java.lang.String allowlistedPkg : allowlist) {
            if (pkg.equals(allowlistedPkg)) {
                return true;
            }
        }
        return false;
    }

    void updateLockTaskFeatures(final int userId, int flags) {
        int oldFlags = getLockTaskFeaturesForUser(userId);
        if (flags == oldFlags) {
            return;
        }
        this.mLockTaskFeatures.put(userId, flags);
        if (!this.mLockTaskModeTasks.isEmpty() && userId == this.mLockTaskModeTasks.get(0).mUserId) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.LockTaskController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateLockTaskFeatures$4(userId);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateLockTaskFeatures$4(int userId) {
        if (this.mLockTaskModeState == 1) {
            setStatusBarState(this.mLockTaskModeState, userId);
            setKeyguardState(this.mLockTaskModeState, userId);
        }
    }

    private void setStatusBarState(int lockTaskModeState, int userId) {
        com.android.internal.statusbar.IStatusBarService statusBar = getStatusBarService();
        if (statusBar == null) {
            android.util.Slog.e(TAG, "Can't find StatusBarService");
            return;
        }
        int flags1 = 0;
        int flags2 = 0;
        if (lockTaskModeState == 2) {
            flags1 = STATUS_BAR_MASK_PINNED;
        } else if (lockTaskModeState == 1) {
            int lockTaskFeatures = getLockTaskFeaturesForUser(userId);
            android.util.Pair<java.lang.Integer, java.lang.Integer> statusBarFlags = getStatusBarDisableFlags(lockTaskFeatures);
            flags1 = ((java.lang.Integer) statusBarFlags.first).intValue();
            flags2 = ((java.lang.Integer) statusBarFlags.second).intValue();
        }
        try {
            statusBar.disable(flags1, this.mToken, this.mContext.getPackageName());
            statusBar.disable2(flags2, this.mToken, this.mContext.getPackageName());
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to set status bar flags", e);
        }
    }

    private void setKeyguardState(int lockTaskModeState, int userId) {
        this.mPendingDisableFromDismiss = -10000;
        if (lockTaskModeState == 0) {
            this.mWindowManager.reenableKeyguard(this.mToken, userId);
            return;
        }
        if (lockTaskModeState == 1) {
            if (isKeyguardAllowed(userId)) {
                this.mWindowManager.reenableKeyguard(this.mToken, userId);
                return;
            } else if (this.mWindowManager.isKeyguardLocked() && !this.mWindowManager.isKeyguardSecure(userId)) {
                this.mPendingDisableFromDismiss = userId;
                this.mWindowManager.dismissKeyguard(new com.android.server.wm.LockTaskController.AnonymousClass1(userId), null);
                return;
            } else {
                this.mWindowManager.disableKeyguard(this.mToken, LOCK_TASK_TAG, userId);
                return;
            }
        }
        this.mWindowManager.disableKeyguard(this.mToken, LOCK_TASK_TAG, userId);
    }

    /* JADX INFO: renamed from: com.android.server.wm.LockTaskController$1, reason: invalid class name */
    class AnonymousClass1 extends com.android.internal.policy.IKeyguardDismissCallback.Stub {
        final /* synthetic */ int val$userId;

        AnonymousClass1(int i) {
            this.val$userId = i;
        }

        public void onDismissError() throws android.os.RemoteException {
            android.util.Slog.i(com.android.server.wm.LockTaskController.TAG, "setKeyguardState: failed to dismiss keyguard");
        }

        public void onDismissSucceeded() throws android.os.RemoteException {
            android.os.Handler handler = com.android.server.wm.LockTaskController.this.mHandler;
            final int i = this.val$userId;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.wm.LockTaskController$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onDismissSucceeded$0(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDismissSucceeded$0(int userId) {
            if (com.android.server.wm.LockTaskController.this.mPendingDisableFromDismiss == userId) {
                com.android.server.wm.LockTaskController.this.mWindowManager.disableKeyguard(com.android.server.wm.LockTaskController.this.mToken, com.android.server.wm.LockTaskController.LOCK_TASK_TAG, userId);
                com.android.server.wm.LockTaskController.this.mPendingDisableFromDismiss = -10000;
            }
        }

        public void onDismissCancelled() throws android.os.RemoteException {
            android.util.Slog.i(com.android.server.wm.LockTaskController.TAG, "setKeyguardState: dismiss cancelled");
        }
    }

    private void lockKeyguardIfNeeded(int userId) {
        if (shouldLockKeyguard(userId)) {
            this.mWindowManager.lockNow(null);
            this.mWindowManager.dismissKeyguard(null, null);
            getLockPatternUtils().requireCredentialEntry(-1);
        }
    }

    private boolean shouldLockKeyguard(int userId) {
        try {
            return android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "lock_to_app_exit_locked", -2) != 0;
        } catch (android.provider.Settings.SettingNotFoundException e) {
            android.util.EventLog.writeEvent(1397638484, "127605586", -1, "");
            return getLockPatternUtils().isSecure(userId);
        }
    }

    android.util.Pair<java.lang.Integer, java.lang.Integer> getStatusBarDisableFlags(int lockTaskFlags) {
        int flags1 = 134152192;
        int flags2 = 31;
        for (int i = STATUS_BAR_FLAG_MAP_LOCKED.size() - 1; i >= 0; i--) {
            android.util.Pair<java.lang.Integer, java.lang.Integer> statusBarFlags = STATUS_BAR_FLAG_MAP_LOCKED.valueAt(i);
            if ((STATUS_BAR_FLAG_MAP_LOCKED.keyAt(i) & lockTaskFlags) != 0) {
                flags1 &= ~((java.lang.Integer) statusBarFlags.first).intValue();
                flags2 &= ~((java.lang.Integer) statusBarFlags.second).intValue();
            }
        }
        return new android.util.Pair<>(java.lang.Integer.valueOf(flags1 & STATUS_BAR_MASK_LOCKED), java.lang.Integer.valueOf(flags2));
    }

    boolean isBaseOfLockedTask(java.lang.String packageName) {
        for (int i = 0; i < this.mLockTaskModeTasks.size(); i++) {
            if (packageName.equals(this.mLockTaskModeTasks.get(i).getBasePackageName())) {
                return true;
            }
        }
        return false;
    }

    private int getLockTaskFeaturesForUser(int userId) {
        return this.mLockTaskFeatures.get(userId, 0);
    }

    private com.android.internal.statusbar.IStatusBarService getStatusBarService() {
        if (this.mStatusBarService == null) {
            this.mStatusBarService = com.android.internal.statusbar.IStatusBarService.Stub.asInterface(android.os.ServiceManager.checkService("statusbar"));
            if (this.mStatusBarService == null) {
                android.util.Slog.w("StatusBarManager", "warning: no STATUS_BAR_SERVICE");
            }
        }
        return this.mStatusBarService;
    }

    private android.app.admin.IDevicePolicyManager getDevicePolicyManager() {
        if (this.mDevicePolicyManager == null) {
            this.mDevicePolicyManager = android.app.admin.IDevicePolicyManager.Stub.asInterface(android.os.ServiceManager.checkService("device_policy"));
            if (this.mDevicePolicyManager == null) {
                android.util.Slog.w(TAG, "warning: no DEVICE_POLICY_SERVICE");
            }
        }
        return this.mDevicePolicyManager;
    }

    private com.android.internal.widget.LockPatternUtils getLockPatternUtils() {
        if (this.mLockPatternUtils == null) {
            return new com.android.internal.widget.LockPatternUtils(this.mContext);
        }
        return this.mLockPatternUtils;
    }

    private android.telecom.TelecomManager getTelecomManager() {
        if (this.mTelecomManager == null) {
            return (android.telecom.TelecomManager) this.mContext.getSystemService(android.telecom.TelecomManager.class);
        }
        return this.mTelecomManager;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "LockTaskController:");
        java.lang.String prefix2 = prefix + "  ";
        pw.println(prefix2 + "mLockTaskModeState=" + lockTaskModeToString());
        pw.println(prefix2 + "mLockTaskModeTasks=");
        for (int i = 0; i < this.mLockTaskModeTasks.size(); i++) {
            pw.println(prefix2 + "  #" + i + " " + this.mLockTaskModeTasks.get(i));
        }
        pw.println(prefix2 + "mLockTaskPackages (userId:packages)=");
        for (int i2 = 0; i2 < this.mLockTaskPackages.size(); i2++) {
            pw.println(prefix2 + "  u" + this.mLockTaskPackages.keyAt(i2) + ":" + java.util.Arrays.toString(this.mLockTaskPackages.valueAt(i2)));
        }
        pw.println();
        this.mLockTaskControllerExt.dump(pw, prefix2);
    }

    private java.lang.String lockTaskModeToString() {
        switch (this.mLockTaskModeState) {
            case 0:
                return "NONE";
            case 1:
                return "LOCKED";
            case 2:
                return "PINNED";
            default:
                return "unknown=" + this.mLockTaskModeState;
        }
    }

    static class LockTaskToken extends android.os.Binder {
        private LockTaskToken() {
        }
    }
}
