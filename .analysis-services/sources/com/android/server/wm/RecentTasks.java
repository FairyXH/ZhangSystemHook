package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class RecentTasks {
    private static final int DEFAULT_INITIAL_CAPACITY = 5;
    private static final int MAX_HIDDEN_TASK_SIZE = 10;
    private long mActiveTasksSessionDurationMs;
    private final java.util.ArrayList<com.android.server.wm.RecentTasks.Callbacks> mCallbacks;
    private boolean mCheckTrimmableTasksOnIdle;
    private java.lang.String mFeatureId;
    private boolean mFreezeTaskListReordering;
    private long mFreezeTaskListTimeoutMs;
    private int mGlobalMaxNumTasks;
    private boolean mHasVisibleRecentTasks;
    private final java.util.ArrayList<com.android.server.wm.Task> mHiddenTasks;
    private final android.view.WindowManagerPolicyConstants.PointerEventListener mListener;
    private int mMaxNumVisibleTasks;
    private int mMinNumVisibleTasks;
    private final android.util.SparseArray<android.util.SparseBooleanArray> mPersistedTaskIds;
    com.android.server.wm.IRecentTasksExt mRecentTasksExt;
    private final com.android.server.wm.RecentTasks.RecentTasksWrapper mRecentTasksWrapper;
    private android.content.ComponentName mRecentsComponent;
    private int mRecentsUid;
    private final java.lang.Runnable mResetFreezeTaskListOnTimeoutRunnable;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    public com.android.server.wm.IRecentTasksSocExt mSocExt;
    private final com.android.server.wm.ActivityTaskSupervisor mSupervisor;
    private com.android.server.wm.TaskChangeNotificationController mTaskNotificationController;
    private final com.android.server.wm.TaskPersister mTaskPersister;
    private final java.util.ArrayList<com.android.server.wm.Task> mTasks;
    private final java.util.HashMap<android.content.ComponentName, android.content.pm.ActivityInfo> mTmpAvailActCache;
    private final java.util.HashMap<java.lang.String, android.content.pm.ApplicationInfo> mTmpAvailAppCache;
    private final android.util.SparseBooleanArray mTmpQuietProfileUserIds;
    private final java.util.ArrayList<com.android.server.wm.Task> mTmpRecents;
    private final android.graphics.Rect mTmpRect;
    private final android.util.SparseArray<java.util.concurrent.atomic.AtomicBoolean> mUsersWithRecentsLoaded;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static final java.lang.String TAG_RECENTS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_RECENTS;
    private static final java.lang.String TAG_TASKS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_TASKS + "_Recents";
    private static final boolean PANIC_DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final long FREEZE_TASK_LIST_TIMEOUT_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(5);
    private static final java.util.Comparator<com.android.server.wm.Task> TASK_ID_COMPARATOR = new java.util.Comparator() { // from class: com.android.server.wm.RecentTasks$$ExternalSyntheticLambda0
        @Override // java.util.Comparator
        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
            return com.android.server.wm.RecentTasks.lambda$static$0((com.android.server.wm.Task) obj, (com.android.server.wm.Task) obj2);
        }
    };
    private static final android.content.pm.ActivityInfo NO_ACTIVITY_INFO_TOKEN = new android.content.pm.ActivityInfo();
    private static final android.content.pm.ApplicationInfo NO_APPLICATION_INFO_TOKEN = new android.content.pm.ApplicationInfo();

    interface Callbacks {
        void onRecentTaskAdded(com.android.server.wm.Task task);

        void onRecentTaskRemoved(com.android.server.wm.Task task, boolean z, boolean z2);
    }

    static /* synthetic */ int lambda$static$0(com.android.server.wm.Task lhs, com.android.server.wm.Task rhs) {
        return rhs.mTaskId - lhs.mTaskId;
    }

    /* JADX INFO: renamed from: com.android.server.wm.RecentTasks$1, reason: invalid class name */
    class AnonymousClass1 implements android.view.WindowManagerPolicyConstants.PointerEventListener {
        AnonymousClass1() {
        }

        public void onPointerEvent(android.view.MotionEvent ev) {
            if (com.android.server.wm.RecentTasks.this.mService.mH.hasCallbacks(com.android.server.wm.RecentTasks.this.mResetFreezeTaskListOnTimeoutRunnable) && com.android.server.wm.RecentTasks.this.mRecentTasksExt.skipResetFreezeTaskListReordering(ev)) {
                android.util.Slog.d(com.android.server.wm.RecentTasks.TAG, "Ignore resetFreezeTaskListReordering while touching from touchpad!");
                return;
            }
            if (!com.android.server.wm.RecentTasks.this.mFreezeTaskListReordering || ev.getAction() != 0 || ev.getClassification() == 4) {
                return;
            }
            final int displayId = ev.getDisplayId();
            final int x = (int) ev.getX();
            final int y = (int) ev.getY();
            com.android.server.wm.RecentTasks.this.mService.mH.post(com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new java.util.function.Consumer() { // from class: com.android.server.wm.RecentTasks$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onPointerEvent$0(displayId, x, y, obj);
                }
            }, (java.lang.Object) null).recycleOnUse());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPointerEvent$0(int displayId, int x, int y, java.lang.Object nonArg) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.RecentTasks.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.RootWindowContainer rac = com.android.server.wm.RecentTasks.this.mService.mRootWindowContainer;
                    if (rac.getDisplayContent(displayId) == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.DisplayContent dc = rac.getDisplayContent(displayId).mDisplayContent;
                    com.android.server.wm.WindowState win = dc.getTouchableWinAtPointLocked(x, y);
                    if (win == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    android.view.InsetsState insetsState = dc.getInsetsStateController().getRawInsetsState();
                    com.android.server.wm.RecentTasks.this.mTmpRect.set(win.getFrame());
                    com.android.server.wm.RecentTasks.this.mTmpRect.inset(insetsState.calculateInsets(win.getFrame(), android.view.WindowInsets.Type.mandatorySystemGestures(), false));
                    if (!com.android.server.wm.RecentTasks.this.mTmpRect.contains(x, y)) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (dc.getWrapper().getExtImpl().pointWithinAppWindow(x, y)) {
                        com.android.server.wm.Task stack = com.android.server.wm.RecentTasks.this.mService.getTopDisplayFocusedRootTask();
                        com.android.server.wm.Task topTask = stack != null ? stack.getTopMostTask() : null;
                        if (com.android.server.wm.RecentTasks.this.mFreezeTaskListReordering) {
                            android.util.Slog.i(com.android.server.wm.RecentTasks.TAG, "onPointerEvent in app window x=" + x + " y=" + y + " topTask:" + topTask);
                        }
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[2]) {
                            java.lang.String protoLogParam0 = java.lang.String.valueOf(win);
                            long protoLogParam1 = x;
                            long protoLogParam2 = y;
                            java.lang.String protoLogParam3 = java.lang.String.valueOf(com.android.server.wm.RecentTasks.this.mTmpRect);
                            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -1640401313436844534L, 20, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2), protoLogParam3);
                        }
                        com.android.server.wm.RecentTasks.this.resetFreezeTaskListReordering(topTask);
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }
    }

    RecentTasks(com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.TaskPersister taskPersister) {
        this.mRecentsUid = -1;
        this.mRecentsComponent = null;
        this.mUsersWithRecentsLoaded = new android.util.SparseArray<>(5);
        this.mPersistedTaskIds = new android.util.SparseArray<>(5);
        this.mTasks = new java.util.ArrayList<>();
        this.mCallbacks = new java.util.ArrayList<>();
        this.mHiddenTasks = new java.util.ArrayList<>();
        this.mFreezeTaskListTimeoutMs = FREEZE_TASK_LIST_TIMEOUT_MS;
        this.mTmpRecents = new java.util.ArrayList<>();
        this.mTmpAvailActCache = new java.util.HashMap<>();
        this.mTmpAvailAppCache = new java.util.HashMap<>();
        this.mTmpQuietProfileUserIds = new android.util.SparseBooleanArray();
        this.mTmpRect = new android.graphics.Rect();
        this.mRecentTasksExt = (com.android.server.wm.IRecentTasksExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IRecentTasksExt.class).base(this).create();
        this.mSocExt = (com.android.server.wm.IRecentTasksSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IRecentTasksSocExt.class).base(this).create();
        this.mListener = new com.android.server.wm.RecentTasks.AnonymousClass1();
        this.mResetFreezeTaskListOnTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.RecentTasks$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.resetFreezeTaskListReorderingOnTimeout();
            }
        };
        this.mRecentTasksWrapper = new com.android.server.wm.RecentTasks.RecentTasksWrapper();
        this.mService = service;
        this.mSupervisor = this.mService.mTaskSupervisor;
        this.mTaskPersister = taskPersister;
        this.mGlobalMaxNumTasks = android.app.ActivityTaskManager.getMaxRecentTasksStatic();
        this.mHasVisibleRecentTasks = true;
        this.mTaskNotificationController = service.getTaskChangeNotificationController();
    }

    RecentTasks(com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.ActivityTaskSupervisor taskSupervisor) {
        this.mRecentsUid = -1;
        this.mRecentsComponent = null;
        this.mUsersWithRecentsLoaded = new android.util.SparseArray<>(5);
        this.mPersistedTaskIds = new android.util.SparseArray<>(5);
        this.mTasks = new java.util.ArrayList<>();
        this.mCallbacks = new java.util.ArrayList<>();
        this.mHiddenTasks = new java.util.ArrayList<>();
        this.mFreezeTaskListTimeoutMs = FREEZE_TASK_LIST_TIMEOUT_MS;
        this.mTmpRecents = new java.util.ArrayList<>();
        this.mTmpAvailActCache = new java.util.HashMap<>();
        this.mTmpAvailAppCache = new java.util.HashMap<>();
        this.mTmpQuietProfileUserIds = new android.util.SparseBooleanArray();
        this.mTmpRect = new android.graphics.Rect();
        this.mRecentTasksExt = (com.android.server.wm.IRecentTasksExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IRecentTasksExt.class).base(this).create();
        this.mSocExt = (com.android.server.wm.IRecentTasksSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IRecentTasksSocExt.class).base(this).create();
        this.mListener = new com.android.server.wm.RecentTasks.AnonymousClass1();
        this.mResetFreezeTaskListOnTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.RecentTasks$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.resetFreezeTaskListReorderingOnTimeout();
            }
        };
        this.mRecentTasksWrapper = new com.android.server.wm.RecentTasks.RecentTasksWrapper();
        java.io.File systemDir = android.os.Environment.getDataSystemDirectory();
        android.content.res.Resources res = service.mContext.getResources();
        this.mService = service;
        this.mSupervisor = this.mService.mTaskSupervisor;
        this.mTaskPersister = new com.android.server.wm.TaskPersister(systemDir, taskSupervisor, service, this, taskSupervisor.mPersisterQueue);
        this.mGlobalMaxNumTasks = android.app.ActivityTaskManager.getMaxRecentTasksStatic();
        this.mTaskNotificationController = service.getTaskChangeNotificationController();
        this.mHasVisibleRecentTasks = res.getBoolean(android.R.bool.config_fillSecondaryBuiltInDisplayCutout);
        loadParametersFromResources(res);
    }

    void setParameters(int minNumVisibleTasks, int maxNumVisibleTasks, long activeSessionDurationMs) {
        this.mMinNumVisibleTasks = minNumVisibleTasks;
        this.mMaxNumVisibleTasks = maxNumVisibleTasks;
        this.mActiveTasksSessionDurationMs = activeSessionDurationMs;
    }

    void setGlobalMaxNumTasks(int globalMaxNumTasks) {
        this.mGlobalMaxNumTasks = globalMaxNumTasks;
    }

    void setFreezeTaskListTimeout(long timeoutMs) {
        this.mFreezeTaskListTimeoutMs = timeoutMs;
    }

    android.view.WindowManagerPolicyConstants.PointerEventListener getInputListener() {
        return this.mListener;
    }

    void setFreezeTaskListReordering() {
        if (!this.mFreezeTaskListReordering) {
            this.mTaskNotificationController.notifyTaskListFrozen(true);
            this.mFreezeTaskListReordering = true;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[2]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -8803811426486764449L, 0, null, null);
        }
        this.mService.mH.removeCallbacks(this.mResetFreezeTaskListOnTimeoutRunnable);
        this.mService.mH.postDelayed(this.mResetFreezeTaskListOnTimeoutRunnable, this.mFreezeTaskListTimeoutMs);
    }

    void resetFreezeTaskListReordering(com.android.server.wm.Task topTask) {
        if (!this.mFreezeTaskListReordering) {
            return;
        }
        this.mFreezeTaskListReordering = false;
        this.mService.mH.removeCallbacks(this.mResetFreezeTaskListOnTimeoutRunnable);
        if (topTask != null) {
            this.mTasks.remove(topTask);
            this.mTasks.add(0, topTask);
        }
        trimInactiveRecentTasks();
        this.mTaskNotificationController.notifyTaskStackChanged();
        this.mTaskNotificationController.notifyTaskListFrozen(false);
    }

    void resetFreezeTaskListReorderingOnTimeout() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task focusedStack = this.mService.getTopDisplayFocusedRootTask();
                com.android.server.wm.Task reorderToEndTask = null;
                com.android.server.wm.Task topTask = focusedStack != null ? focusedStack.getTopMostTask() : null;
                if (topTask != null && topTask.hasChild()) {
                    reorderToEndTask = topTask;
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[2]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 4040735335719974079L, 0, null, null);
                }
                resetFreezeTaskListReordering(reorderToEndTask);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    boolean isFreezeTaskListReorderingSet() {
        return this.mFreezeTaskListReordering;
    }

    void loadParametersFromResources(android.content.res.Resources res) {
        long millis;
        if (android.app.ActivityManager.isLowRamDeviceStatic()) {
            this.mMinNumVisibleTasks = res.getInteger(android.R.integer.config_maxDesktopWindowingActiveTasks);
            this.mMaxNumVisibleTasks = res.getInteger(android.R.integer.config_lidNavigationAccessibility);
        } else {
            this.mMinNumVisibleTasks = res.getInteger(android.R.integer.config_lowPowerStandbyNonInteractiveTimeout);
            this.mMaxNumVisibleTasks = res.getInteger(android.R.integer.config_lidKeyboardAccessibility);
        }
        int sessionDurationHrs = res.getInteger(android.R.integer.button_pressed_animation_delay);
        if (sessionDurationHrs > 0) {
            millis = java.util.concurrent.TimeUnit.HOURS.toMillis(sessionDurationHrs);
        } else {
            millis = -1;
        }
        this.mActiveTasksSessionDurationMs = millis;
    }

    void loadRecentsComponent(android.content.res.Resources res) {
        android.content.ComponentName cn;
        java.lang.String rawRecentsComponent = res.getString(android.R.string.config_satellite_nidd_apn_name);
        if (!android.text.TextUtils.isEmpty(rawRecentsComponent) && (cn = android.content.ComponentName.unflattenFromString(rawRecentsComponent)) != null) {
            try {
                android.content.pm.ApplicationInfo appInfo = android.app.AppGlobals.getPackageManager().getApplicationInfo(cn.getPackageName(), 8704L, this.mService.mContext.getUserId());
                if (appInfo != null) {
                    this.mRecentsUid = appInfo.uid;
                    this.mRecentsComponent = cn;
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Could not load application info for recents component: " + cn);
            }
        }
    }

    boolean isCallerRecents(int callingUid) {
        return android.os.UserHandle.isSameApp(callingUid, this.mRecentsUid);
    }

    boolean isRecentsComponent(android.content.ComponentName cn, int uid) {
        return cn.equals(this.mRecentsComponent) && android.os.UserHandle.isSameApp(uid, this.mRecentsUid);
    }

    boolean isRecentsComponentHomeActivity(int userId) {
        android.content.ComponentName defaultHomeActivity = this.mService.getPackageManagerInternalLocked().getDefaultHomeActivity(userId);
        return (defaultHomeActivity == null || this.mRecentsComponent == null || !defaultHomeActivity.getPackageName().equals(this.mRecentsComponent.getPackageName())) ? false : true;
    }

    android.content.ComponentName getRecentsComponent() {
        return this.mRecentsComponent;
    }

    java.lang.String getRecentsComponentFeatureId() {
        return this.mFeatureId;
    }

    int getRecentsComponentUid() {
        return this.mRecentsUid;
    }

    void registerCallback(com.android.server.wm.RecentTasks.Callbacks callback) {
        this.mCallbacks.add(callback);
    }

    void unregisterCallback(com.android.server.wm.RecentTasks.Callbacks callback) {
        this.mCallbacks.remove(callback);
    }

    private void notifyTaskAdded(com.android.server.wm.Task task) {
        android.util.Slog.d(TAG_RECENTS, "notifyTaskAdded, task: " + task + " trace:" + android.os.Debug.getCallers(5));
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onRecentTaskAdded(task);
        }
        this.mTaskNotificationController.notifyTaskListUpdated();
    }

    private void notifyTaskRemoved(com.android.server.wm.Task task, boolean wasTrimmed, boolean killProcess) {
        android.util.Slog.d(TAG_RECENTS, "notifyTaskRemoved, task: " + task + " trace:" + android.os.Debug.getCallers(5));
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onRecentTaskRemoved(task, wasTrimmed, killProcess);
        }
        this.mTaskNotificationController.notifyTaskListUpdated();
    }

    void loadRecentTasksIfNeeded(int userId) {
        java.util.concurrent.atomic.AtomicBoolean userLoaded;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                userLoaded = this.mUsersWithRecentsLoaded.get(userId);
                if (userLoaded == null) {
                    android.util.SparseArray<java.util.concurrent.atomic.AtomicBoolean> sparseArray = this.mUsersWithRecentsLoaded;
                    java.util.concurrent.atomic.AtomicBoolean atomicBoolean = new java.util.concurrent.atomic.AtomicBoolean();
                    userLoaded = atomicBoolean;
                    sparseArray.append(userId, atomicBoolean);
                }
            } finally {
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        synchronized (userLoaded) {
            if (userLoaded.get()) {
                return;
            }
            android.util.SparseBooleanArray persistedTaskIds = this.mTaskPersister.readPersistedTaskIdsFromFileForUser(userId);
            com.android.server.wm.TaskPersister.RecentTaskFiles taskFiles = com.android.server.wm.TaskPersister.loadTasksForUser(userId);
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock2) {
                try {
                    restoreRecentTasksLocked(userId, persistedTaskIds, taskFiles);
                } finally {
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            userLoaded.set(true);
        }
    }

    private void restoreRecentTasksLocked(int userId, android.util.SparseBooleanArray persistedTaskIds, com.android.server.wm.TaskPersister.RecentTaskFiles taskFiles) {
        this.mTaskPersister.setPersistedTaskIds(userId, persistedTaskIds);
        this.mPersistedTaskIds.put(userId, persistedTaskIds.clone());
        android.util.IntArray existedTaskIds = new android.util.IntArray();
        for (int i = this.mTasks.size() - 1; i >= 0; i--) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            if (task.mUserId == userId && shouldPersistTaskLocked(task)) {
                existedTaskIds.add(task.mTaskId);
            }
        }
        android.util.Slog.i(TAG, "Restoring recents for user " + userId);
        java.util.ArrayList<com.android.server.wm.Task> tasks = this.mTaskPersister.restoreTasksForUserLocked(userId, taskFiles, existedTaskIds);
        long currentElapsedTime = android.os.SystemClock.elapsedRealtime();
        for (int i2 = 0; i2 < tasks.size(); i2++) {
            tasks.get(i2).lastActiveTime = currentElapsedTime - ((long) i2);
        }
        this.mTasks.addAll(tasks);
        cleanupLocked(userId);
        if (existedTaskIds.size() > 0) {
            syncPersistentTaskIdsLocked();
        }
    }

    private boolean isRecentTasksLoaded(int userId) {
        java.util.concurrent.atomic.AtomicBoolean userLoaded = this.mUsersWithRecentsLoaded.get(userId);
        return userLoaded != null && userLoaded.get();
    }

    boolean containsTaskId(int taskId, int userId) {
        android.util.SparseBooleanArray taskIds = this.mPersistedTaskIds.get(userId);
        return taskIds != null && taskIds.get(taskId);
    }

    android.util.SparseBooleanArray getTaskIdsForLoadedUser(int loadedUserId) {
        android.util.SparseBooleanArray taskIds = this.mPersistedTaskIds.get(loadedUserId);
        if (taskIds == null) {
            android.util.Slog.wtf(TAG, "Loaded user without loaded tasks, userId=" + loadedUserId);
            return new android.util.SparseBooleanArray();
        }
        return taskIds;
    }

    void notifyTaskPersisterLocked(com.android.server.wm.Task task, boolean flush) {
        com.android.server.wm.Task rootTask = task != null ? task.getRootTask() : null;
        if ((rootTask != null && rootTask.isActivityTypeHomeOrRecents()) || this.mRecentTasksExt.skipPersistMultiSearchTask(task)) {
            return;
        }
        if (task != null && task.getWrapper().getExtImpl().isCreateForSingleSplit()) {
            return;
        }
        syncPersistentTaskIdsLocked();
        this.mTaskPersister.wakeup(task, flush);
    }

    private void syncPersistentTaskIdsLocked() {
        for (int i = this.mPersistedTaskIds.size() - 1; i >= 0; i--) {
            int userId = this.mPersistedTaskIds.keyAt(i);
            if (isRecentTasksLoaded(userId)) {
                this.mPersistedTaskIds.valueAt(i).clear();
            }
        }
        for (int i2 = this.mTasks.size() - 1; i2 >= 0; i2--) {
            com.android.server.wm.Task task = this.mTasks.get(i2);
            if (shouldPersistTaskLocked(task)) {
                if (this.mPersistedTaskIds.get(task.mUserId) == null) {
                    android.util.Slog.wtf(TAG, "No task ids found for userId " + task.mUserId + ". task=" + task + " mPersistedTaskIds=" + this.mPersistedTaskIds);
                    this.mPersistedTaskIds.put(task.mUserId, new android.util.SparseBooleanArray());
                }
                this.mPersistedTaskIds.get(task.mUserId).put(task.mTaskId, true);
            }
        }
    }

    private static boolean shouldPersistTaskLocked(com.android.server.wm.Task task) {
        com.android.server.wm.Task rootTask = task.getRootTask();
        return task.isPersistable && (rootTask == null || !rootTask.isActivityTypeHomeOrRecents());
    }

    void onSystemReadyLocked() {
        loadRecentsComponent(this.mService.mContext.getResources());
        this.mTasks.clear();
    }

    android.graphics.Bitmap getTaskDescriptionIcon(java.lang.String path) {
        return this.mTaskPersister.getTaskDescriptionIcon(path);
    }

    void saveImage(android.graphics.Bitmap image, java.lang.String path) {
        this.mTaskPersister.saveImage(image, path);
    }

    void flush() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                syncPersistentTaskIdsLocked();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        this.mTaskPersister.flush();
    }

    int[] usersWithRecentsLoadedLocked() {
        int[] usersWithRecentsLoaded = new int[this.mUsersWithRecentsLoaded.size()];
        int len = 0;
        for (int i = 0; i < usersWithRecentsLoaded.length; i++) {
            int userId = this.mUsersWithRecentsLoaded.keyAt(i);
            if (this.mUsersWithRecentsLoaded.valueAt(i).get()) {
                usersWithRecentsLoaded[len] = userId;
                len++;
            }
        }
        int i2 = usersWithRecentsLoaded.length;
        if (len < i2) {
            return java.util.Arrays.copyOf(usersWithRecentsLoaded, len);
        }
        return usersWithRecentsLoaded;
    }

    void unloadUserDataFromMemoryLocked(int userId) {
        if (isRecentTasksLoaded(userId)) {
            android.util.Slog.i(TAG, "Unloading recents for user " + userId + " from memory.");
            this.mUsersWithRecentsLoaded.delete(userId);
            removeTasksForUserLocked(userId);
        }
        this.mPersistedTaskIds.delete(userId);
        this.mTaskPersister.unloadUserDataFromMemory(userId);
    }

    private void removeTasksForUserLocked(int userId) {
        if (userId <= 0) {
            android.util.Slog.i(TAG, "Can't remove recent task on user " + userId);
            return;
        }
        for (int i = this.mTasks.size() - 1; i >= 0; i--) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            if (task.mUserId == userId) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[2]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(task);
                    long protoLogParam1 = userId;
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 3308140128142966415L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
                }
                remove(task);
            }
        }
    }

    void onPackagesSuspendedChanged(java.lang.String[] packages, boolean suspended, int userId) {
        java.util.Set<java.lang.String> packageNames = com.google.android.collect.Sets.newHashSet(packages);
        for (int i = this.mTasks.size() - 1; i >= 0; i--) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            if (task.realActivity != null && packageNames.contains(task.realActivity.getPackageName()) && task.mUserId == userId && task.realActivitySuspended != suspended) {
                task.realActivitySuspended = suspended;
                if (suspended) {
                    this.mSupervisor.removeTask(task, false, true, "suspended-package");
                }
                notifyTaskPersisterLocked(task, false);
            }
        }
    }

    void onLockTaskModeStateChanged(int lockTaskModeState, int userId) {
        if (lockTaskModeState == 1) {
            for (int i = this.mTasks.size() - 1; i >= 0; i--) {
                com.android.server.wm.Task task = this.mTasks.get(i);
                if (task.mUserId == userId) {
                    this.mService.getLockTaskController();
                    if (!com.android.server.wm.LockTaskController.isTaskAuthAllowlisted(task.mLockTaskAuth)) {
                        remove(task);
                    }
                }
            }
        }
    }

    void removeTasksByPackageName(java.lang.String packageName, int userId) {
        for (int i = this.mTasks.size() - 1; i >= 0; i--) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            if (task.mUserId == userId && task.getBasePackageName().equals(packageName)) {
                this.mSupervisor.removeTask(task, true, true, "remove-package-task");
            }
        }
    }

    void removeAllVisibleTasks(int userId) {
        java.util.Set<java.lang.Integer> profileIds = getProfileIds(userId);
        for (int i = this.mTasks.size() - 1; i >= 0; i--) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            if (profileIds.contains(java.lang.Integer.valueOf(task.mUserId)) && isVisibleRecentTask(task)) {
                this.mTasks.remove(i);
                notifyTaskRemoved(task, true, true);
            }
        }
    }

    void cleanupDisabledPackageTasksLocked(java.lang.String packageName, java.util.Set<java.lang.String> filterByClasses, int userId) {
        for (int i = this.mTasks.size() - 1; i >= 0; i--) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            if (userId == -1 || task.mUserId == userId) {
                android.content.ComponentName cn = task.intent != null ? task.intent.getComponent() : null;
                boolean sameComponent = cn != null && cn.getPackageName().equals(packageName) && (filterByClasses == null || filterByClasses.contains(cn.getClassName()));
                if (sameComponent) {
                    this.mSupervisor.removeTask(task, false, true, "disabled-package");
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00e3  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00fe  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void cleanupLocked(int r14) {
        /*
            Method dump skipped, instruction units count: 409
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.RecentTasks.cleanupLocked(int):void");
    }

    private boolean canAddTaskWithoutTrim(com.android.server.wm.Task task) {
        return findRemoveIndexForAddTask(task) == -1;
    }

    java.util.ArrayList<android.os.IBinder> getAppTasksList(int callingUid, java.lang.String callingPackage) {
        java.util.ArrayList<android.os.IBinder> list = new java.util.ArrayList<>();
        int size = this.mTasks.size();
        for (int i = 0; i < size; i++) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            if (task.effectiveUid == callingUid && callingPackage.equals(task.getBasePackageName())) {
                com.android.server.wm.AppTaskImpl taskImpl = new com.android.server.wm.AppTaskImpl(this.mService, task.mTaskId, callingUid);
                list.add(taskImpl.asBinder());
            }
        }
        return list;
    }

    java.util.Set<java.lang.Integer> getProfileIds(int userId) {
        java.util.Set<java.lang.Integer> userIds = new android.util.ArraySet<>();
        int[] profileIds = this.mService.getUserManager().getProfileIds(userId, false);
        for (int i : profileIds) {
            userIds.add(java.lang.Integer.valueOf(i));
        }
        return userIds;
    }

    android.content.pm.UserInfo getUserInfo(int userId) {
        return this.mService.getUserManager().getUserInfo(userId);
    }

    int[] getCurrentProfileIds() {
        return this.mService.mAmInternal.getCurrentProfileIds();
    }

    android.content.pm.ParceledListSlice<android.app.ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags, boolean getTasksAllowed, int userId, int callingUid) {
        return new android.content.pm.ParceledListSlice<>(getRecentTasksImpl(maxNum, flags, getTasksAllowed, userId, callingUid));
    }

    private java.util.ArrayList<android.app.ActivityManager.RecentTaskInfo> getRecentTasksImpl(int maxNum, int flags, boolean getTasksAllowed, int userId, int callingUid) {
        boolean withExcluded = (flags & 1) != 0;
        java.util.Set<java.lang.Integer> includedUsers = getProfileIds(userId);
        includedUsers.add(java.lang.Integer.valueOf(userId));
        this.mRecentTasksExt.setFirstTaskAndFlags(this.mTasks.get(0), flags);
        java.util.ArrayList<android.app.ActivityManager.RecentTaskInfo> res = new java.util.ArrayList<>();
        int size = this.mTasks.size();
        int numVisibleTasks = 0;
        this.mRecentTasksExt.clearFilterBuffer();
        for (int i = 0; i < size; i++) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            if (isVisibleRecentTask(task)) {
                numVisibleTasks++;
                if (!isInVisibleRange(task, i, numVisibleTasks, withExcluded)) {
                    android.util.Slog.d(TAG_RECENTS, "Skipping, invisible task by policy range: " + task + ",position:" + i);
                } else if (res.size() >= maxNum) {
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        android.util.Slog.d(TAG_RECENTS, "Skipping, task num reach the requested size: " + task);
                    }
                } else if (!includedUsers.contains(java.lang.Integer.valueOf(task.mUserId))) {
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        android.util.Slog.d(TAG_RECENTS, "Skipping, not user: " + task);
                    }
                } else if (task.realActivitySuspended) {
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        android.util.Slog.d(TAG_RECENTS, "Skipping, activity suspended: " + task);
                    }
                } else if (!getTasksAllowed && !task.isActivityTypeHome() && task.effectiveUid != callingUid) {
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        android.util.Slog.d(TAG_RECENTS, "Skipping, not allowed: " + task);
                    }
                } else if (task.autoRemoveRecents && task.getTopNonFinishingActivity() == null) {
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        android.util.Slog.d(TAG_RECENTS, "Skipping, auto-remove without activity: " + task);
                    }
                } else if ((flags & 2) != 0 && !task.isAvailable) {
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        android.util.Slog.d(TAG_RECENTS, "Skipping, unavail real act: " + task);
                    }
                } else if (!task.mUserSetupComplete && !this.mRecentTasksExt.reCheckUserSetupComplete(task)) {
                    android.util.Slog.d(TAG_RECENTS, "Skipping, user setup not complete: " + task);
                } else if (this.mRecentTasksExt.skipMultiSearchTask(task)) {
                    android.util.Slog.d(TAG, "Skipping, skipMultiSearchTask: " + task);
                } else if (this.mRecentTasksExt.skipShowRecentTask(task, callingUid)) {
                    android.util.Slog.d(TAG, "Skipping, skipShowRecentTask: " + task);
                } else if (this.mRecentTasksExt.getRecentTasksImpl(task.getBaseIntent())) {
                    android.util.Slog.d(TAG, "Skipping, shouldHideTask: " + task);
                } else if (((com.android.server.wm.IMirageWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IMirageWindowManagerExt.class).create()).shouldHideTaskInRecents(task)) {
                    android.util.Slog.d(TAG, "Skipping, shouldHideTaskInRecents: " + task);
                } else if (!this.mRecentTasksExt.skipPreloadingTaskInRecents(task)) {
                    res.add(createRecentTaskInfo(task, true, getTasksAllowed));
                } else {
                    android.util.Slog.d(TAG, "getRecentTasksImpl skip in ActivityPreloading!");
                }
            } else {
                android.util.Slog.d(TAG_RECENTS, "Skipping, invisible task: " + task);
            }
        }
        android.util.Slog.d(TAG_RECENTS, "pre-Filtered:" + this.mRecentTasksExt.getFilteredBuffer() + "\nafter skip size " + res.size());
        return res;
    }

    void getPersistableTaskIds(android.util.ArraySet<java.lang.Integer> persistentTaskIds) {
        int size = this.mTasks.size();
        for (int i = 0; i < size; i++) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            com.android.server.wm.Task rootTask = task.getRootTask();
            if ((task.isPersistable || task.inRecents) && (rootTask == null || !rootTask.isActivityTypeHomeOrRecents())) {
                persistentTaskIds.add(java.lang.Integer.valueOf(task.mTaskId));
            }
        }
    }

    java.util.ArrayList<com.android.server.wm.Task> getRawTasks() {
        return this.mTasks;
    }

    android.util.SparseBooleanArray getRecentTaskIds() {
        android.util.SparseBooleanArray res = new android.util.SparseBooleanArray();
        int size = this.mTasks.size();
        int numVisibleTasks = 0;
        for (int i = 0; i < size; i++) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            if (isVisibleRecentTask(task)) {
                numVisibleTasks++;
                if (isInVisibleRange(task, i, numVisibleTasks, false)) {
                    res.put(task.mTaskId, true);
                }
            }
        }
        return res;
    }

    com.android.server.wm.Task getTask(int id) {
        int recentsCount = this.mTasks.size();
        for (int i = 0; i < recentsCount; i++) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            if (task.mTaskId == id) {
                return task;
            }
        }
        return null;
    }

    void add(com.android.server.wm.Task task) {
        int taskIndex;
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
            android.util.Slog.d(TAG, "add: task=" + task);
        }
        boolean isAffiliated = (task.mAffiliatedTaskId == task.mTaskId && task.mNextAffiliateTaskId == -1 && task.mPrevAffiliateTaskId == -1) ? false : true;
        int recentsCount = this.mTasks.size();
        if (task.voiceSession != null) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                android.util.Slog.d(TAG_RECENTS, "addRecent: not adding voice interaction " + task);
                return;
            }
            return;
        }
        if (!isAffiliated && recentsCount > 0 && this.mTasks.get(0) == task) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                android.util.Slog.d(TAG_RECENTS, "addRecent: already at top: " + task);
                return;
            }
            return;
        }
        if (isAffiliated && recentsCount > 0 && task.inRecents && task.mAffiliatedTaskId == this.mTasks.get(0).mAffiliatedTaskId) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                android.util.Slog.d(TAG_RECENTS, "addRecent: affiliated " + this.mTasks.get(0) + " at top when adding " + task);
                return;
            }
            return;
        }
        boolean needAffiliationFix = false;
        if (this.mRecentTasksExt.skipAddPreloadingFakeTask(task)) {
            android.util.Slog.d(TAG_RECENTS, "addRecent: not adding preloading fake task: " + task);
            return;
        }
        if (task.inRecents) {
            int taskIndex2 = this.mTasks.indexOf(task);
            if (taskIndex2 >= 0) {
                if (!isAffiliated) {
                    if (!this.mFreezeTaskListReordering && !this.mRecentTasksExt.skipMoveTask(task)) {
                        this.mTasks.remove(taskIndex2);
                        this.mTasks.add(0, task);
                        this.mRecentTasksExt.addPsContainerToTop(this.mTasks, task, 0);
                        if (taskIndex2 != 0) {
                            this.mTaskNotificationController.notifyTaskListUpdated();
                        }
                        if (PANIC_DEBUG) {
                            android.util.Slog.d(TAG_TASKS, "addRecent: moving " + task + " to index 0 from " + taskIndex2);
                        }
                    }
                    notifyTaskPersisterLocked(task, false);
                    return;
                }
            } else {
                android.util.Slog.wtf(TAG, "Task with inRecent not in recents: " + task);
                needAffiliationFix = true;
            }
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
            android.util.Slog.d(TAG_RECENTS, "addRecent: trimming tasks for " + task);
        }
        int removedIndex = removeForAddTask(task);
        task.inRecents = true;
        if (!isAffiliated || needAffiliationFix) {
            int indexToAdd = (!this.mFreezeTaskListReordering || removedIndex == -1) ? 0 : removedIndex;
            this.mTasks.add(this.mRecentTasksExt.adjustPreloadingTaskIndex(indexToAdd, removedIndex, task, recentsCount), task);
            notifyTaskAdded(task);
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                android.util.Slog.d(TAG_RECENTS, "addRecent: adding " + task);
            }
        } else if (isAffiliated) {
            com.android.server.wm.Task other = task.mNextAffiliate;
            if (other == null) {
                other = task.mPrevAffiliate;
            }
            if (other != null) {
                int otherIndex = this.mTasks.indexOf(other);
                if (otherIndex >= 0) {
                    if (other == task.mNextAffiliate) {
                        taskIndex = otherIndex + 1;
                    } else {
                        taskIndex = otherIndex;
                    }
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        android.util.Slog.d(TAG_RECENTS, "addRecent: new affiliated task added at " + taskIndex + ": " + task);
                    }
                    this.mTasks.add(taskIndex, task);
                    notifyTaskAdded(task);
                    if (moveAffiliatedTasksToFront(task, taskIndex)) {
                        return;
                    } else {
                        needAffiliationFix = true;
                    }
                } else {
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                        android.util.Slog.d(TAG_RECENTS, "addRecent: couldn't find other affiliation " + other);
                    }
                    needAffiliationFix = true;
                }
            } else {
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                    android.util.Slog.d(TAG_RECENTS, "addRecent: adding affiliated task without next/prev:" + task);
                }
                needAffiliationFix = true;
            }
        }
        if (needAffiliationFix) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                android.util.Slog.d(TAG_RECENTS, "addRecent: regrouping affiliations");
            }
            cleanupLocked(task.mUserId);
        }
        this.mCheckTrimmableTasksOnIdle = true;
        notifyTaskPersisterLocked(task, false);
    }

    private int findIndexToAdd(com.android.server.wm.Task task) {
        com.android.server.wm.Task otherTask;
        int indexToAdd = 0;
        for (int i = 0; i < this.mTasks.size() && task != (otherTask = this.mTasks.get(i)) && otherTask.isAttached(); i++) {
            if (!otherTask.inPinnedWindowingMode() && otherTask.topRunningActivity() != null) {
                if (task.compareTo((com.android.server.wm.WindowContainer) otherTask) > 0) {
                    break;
                }
                indexToAdd = i + 1;
            }
        }
        return indexToAdd;
    }

    boolean addToBottom(com.android.server.wm.Task task) {
        if (!canAddTaskWithoutTrim(task)) {
            return false;
        }
        add(task);
        return true;
    }

    void remove(com.android.server.wm.Task task) {
        this.mTasks.remove(task);
        notifyTaskRemoved(task, false, false);
        this.mSocExt.removeTaskUxPerf(task);
        if (task != null && task.getWrapper().getExtImpl().isContainerTask()) {
            this.mRecentTasksExt.removeContainerTask(task);
        }
    }

    void onActivityIdle(com.android.server.wm.ActivityRecord r) {
        if (!this.mHiddenTasks.isEmpty() && r.isActivityTypeHome() && r.isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
            removeUnreachableHiddenTasks(r.getWindowingMode());
        }
        if (this.mCheckTrimmableTasksOnIdle) {
            this.mCheckTrimmableTasksOnIdle = false;
            trimInactiveRecentTasks();
        }
    }

    private void trimInactiveRecentTasks() {
        if (this.mFreezeTaskListReordering) {
            return;
        }
        int recentsCount = this.mTasks.size();
        while (recentsCount > this.mGlobalMaxNumTasks) {
            com.android.server.wm.Task task = this.mTasks.remove(recentsCount - 1);
            notifyTaskRemoved(task, true, false);
            recentsCount--;
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                android.util.Slog.d(TAG, "Trimming over max-recents task=" + task + " max=" + this.mGlobalMaxNumTasks);
            }
        }
        int[] profileUserIds = getCurrentProfileIds();
        this.mTmpQuietProfileUserIds.clear();
        for (int userId : profileUserIds) {
            android.content.pm.UserInfo userInfo = getUserInfo(userId);
            if (userInfo != null && userInfo.isManagedProfile() && userInfo.isQuietModeEnabled()) {
                this.mTmpQuietProfileUserIds.put(userId, true);
            }
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                android.util.Slog.d(TAG, "User: " + userInfo + " quiet=" + this.mTmpQuietProfileUserIds.get(userId));
            }
        }
        int numVisibleTasks = 0;
        int i = 0;
        while (i < this.mTasks.size()) {
            com.android.server.wm.Task task2 = this.mTasks.get(i);
            if (isActiveRecentTask(task2, this.mTmpQuietProfileUserIds)) {
                if (!this.mHasVisibleRecentTasks) {
                    i++;
                } else if (!isVisibleRecentTask(task2)) {
                    i++;
                } else {
                    numVisibleTasks++;
                    if (isInVisibleRange(task2, i, numVisibleTasks, false) || !isTrimmable(task2)) {
                        i++;
                    } else if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                        android.util.Slog.d(TAG, "Trimming out-of-range visible task=" + task2);
                    }
                }
            } else if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                android.util.Slog.d(TAG, "Trimming inactive task=" + task2);
            }
            if (this.mRecentTasksExt.skipRemoveRecentTask(this.mService.mContext, task2.realActivity.getClassName())) {
                android.util.Slog.d(TAG, "skip remove task : " + task2.realActivity.getClassName());
                i++;
            } else {
                this.mTasks.remove(task2);
                notifyTaskRemoved(task2, true, false);
                notifyTaskPersisterLocked(task2, false);
            }
        }
    }

    private boolean isActiveRecentTask(com.android.server.wm.Task task, android.util.SparseBooleanArray quietProfileUserIds) {
        com.android.server.wm.Task affiliatedTask;
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
            android.util.Slog.d(TAG, "isActiveRecentTask: task=" + task + " globalMax=" + this.mGlobalMaxNumTasks);
        }
        if (quietProfileUserIds.get(task.mUserId)) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                android.util.Slog.d(TAG, "\tisQuietProfileTask=true");
            }
            return false;
        }
        if (task.mAffiliatedTaskId != -1 && task.mAffiliatedTaskId != task.mTaskId && (affiliatedTask = getTask(task.mAffiliatedTaskId)) != null && !isActiveRecentTask(affiliatedTask, quietProfileUserIds)) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                android.util.Slog.d(TAG, "\taffiliatedWithTask=" + affiliatedTask + " is not active");
            }
            return false;
        }
        return true;
    }

    boolean isVisibleRecentTask(com.android.server.wm.Task task) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
            android.util.Slog.d(TAG, "isVisibleRecentTask: task=" + task + " minVis=" + this.mMinNumVisibleTasks + " maxVis=" + this.mMaxNumVisibleTasks + " sessionDuration=" + this.mActiveTasksSessionDurationMs + " inactiveDuration=" + task.getInactiveDuration() + " activityType=" + task.getActivityType() + " windowingMode=" + task.getWindowingMode() + " isAlwaysOnTopWhenVisible=" + task.isAlwaysOnTopWhenVisible() + " intentFlags=" + task.getBaseIntent().getFlags());
        }
        switch (task.getActivityType()) {
            case 2:
            case 3:
            case 5:
                return false;
            case 4:
                if ((task.getBaseIntent().getFlags() & 8388608) == 8388608) {
                    return false;
                }
                break;
        }
        switch (task.getWindowingMode()) {
            case 2:
                return false;
            case 6:
                if (task.isAlwaysOnTopWhenVisible()) {
                    return false;
                }
                break;
        }
        if (task == this.mService.getLockTaskController().getRootTask()) {
            return false;
        }
        return task.getDisplayContent() == null || task.getDisplayContent().canShowTasksInHostDeviceRecents();
    }

    private boolean isInVisibleRange(com.android.server.wm.Task task, int taskIndex, int numVisibleTasks, boolean skipExcludedCheck) {
        if (!skipExcludedCheck) {
            boolean isExcludeFromRecents = (task.getBaseIntent().getFlags() & 8388608) == 8388608;
            if (this.mRecentTasksExt.isExcludeFromRecentsForFlexible(this.mService, task, isExcludeFromRecents)) {
                if (PANIC_DEBUG) {
                    android.util.Slog.d(TAG, "\texcludeFromRecents=true, taskIndex = " + taskIndex + ", isOnHomeDisplay: " + task.isOnHomeDisplay() + ",mLauncherIndexZero:" + this.mRecentTasksExt.isLauncherIndexZero() + ",mLauncherInSpecificScene:" + this.mRecentTasksExt.isLauncherInSpecificScene());
                }
                if (task.isOnHomeDisplay()) {
                    return taskIndex == 0 || this.mRecentTasksExt.isSpecificSceneInRecentList(taskIndex);
                }
                return false;
            }
        }
        if ((this.mMinNumVisibleTasks < 0 || numVisibleTasks > this.mMinNumVisibleTasks) && task.mChildPipActivity == null) {
            return this.mMaxNumVisibleTasks >= 0 ? numVisibleTasks <= this.mMaxNumVisibleTasks : this.mActiveTasksSessionDurationMs > 0 && task.getInactiveDuration() <= this.mActiveTasksSessionDurationMs;
        }
        return true;
    }

    protected boolean isTrimmable(com.android.server.wm.Task task) {
        com.android.server.wm.Task rootHomeTask;
        if (task.isAttached()) {
            return task.isOnHomeDisplay() && !this.mRecentTasksExt.isTrimmable(task) && (rootHomeTask = task.getDisplayArea().getRootHomeTask()) != null && task.compareTo((com.android.server.wm.WindowContainer) rootHomeTask) < 0;
        }
        return true;
    }

    private void removeUnreachableHiddenTasks(int windowingMode) {
        int size = this.mHiddenTasks.size();
        if (size <= 10) {
            return;
        }
        for (int i = size - 1; i >= 10; i--) {
            com.android.server.wm.Task hiddenTask = this.mHiddenTasks.get(i);
            if (!hiddenTask.hasChild() || hiddenTask.inRecents) {
                this.mHiddenTasks.remove(i);
            } else if (hiddenTask.getWindowingMode() == windowingMode && hiddenTask.getTopVisibleActivity() == null && (com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE || !this.mService.getWrapper().getExtImpl().getRemoteTaskManager().anyTaskExist(hiddenTask.mTaskId))) {
                this.mHiddenTasks.remove(i);
                this.mSupervisor.removeTask(hiddenTask, false, false, "remove-hidden-task");
            }
        }
    }

    private int removeForAddTask(com.android.server.wm.Task task) {
        this.mHiddenTasks.remove(task);
        int removeIndex = findRemoveIndexForAddTask(task);
        if (removeIndex == -1) {
            return removeIndex;
        }
        com.android.server.wm.Task removedTask = this.mTasks.remove(removeIndex);
        if (removedTask != task) {
            if (removedTask.hasChild() && !removedTask.isActivityTypeHome()) {
                android.util.Slog.i(TAG, "Add " + removedTask + " to hidden list because adding " + task);
                this.mHiddenTasks.add(0, removedTask);
            }
            notifyTaskRemoved(removedTask, false, false);
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS_TRIM_TASKS) {
                android.util.Slog.d(TAG, "Trimming task=" + removedTask + " for addition of task=" + task);
            }
        }
        this.mRecentTasksExt.removeForAddTask(task, removedTask);
        notifyTaskPersisterLocked(removedTask, false);
        return removeIndex;
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0112  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int findRemoveIndexForAddTask(com.android.server.wm.Task r21) {
        /*
            Method dump skipped, instruction units count: 413
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.RecentTasks.findRemoveIndexForAddTask(com.android.server.wm.Task):int");
    }

    private int processNextAffiliateChainLocked(int start) {
        com.android.server.wm.Task startTask = this.mTasks.get(start);
        int affiliateId = startTask.mAffiliatedTaskId;
        if (startTask.mTaskId == affiliateId && startTask.mPrevAffiliate == null && startTask.mNextAffiliate == null) {
            startTask.inRecents = true;
            return start + 1;
        }
        this.mTmpRecents.clear();
        for (int i = this.mTasks.size() - 1; i >= start; i--) {
            com.android.server.wm.Task task = this.mTasks.get(i);
            if (task.mAffiliatedTaskId == affiliateId) {
                this.mTasks.remove(i);
                this.mTmpRecents.add(task);
            }
        }
        java.util.Collections.sort(this.mTmpRecents, TASK_ID_COMPARATOR);
        com.android.server.wm.Task first = this.mTmpRecents.get(0);
        first.inRecents = true;
        if (first.mNextAffiliate != null) {
            android.util.Slog.w(TAG, "Link error 1 first.next=" + first.mNextAffiliate);
            first.setNextAffiliate(null);
            notifyTaskPersisterLocked(first, false);
        }
        int tmpSize = this.mTmpRecents.size();
        for (int i2 = 0; i2 < tmpSize - 1; i2++) {
            com.android.server.wm.Task next = this.mTmpRecents.get(i2);
            com.android.server.wm.Task prev = this.mTmpRecents.get(i2 + 1);
            if (next.mPrevAffiliate != prev) {
                android.util.Slog.w(TAG, "Link error 2 next=" + next + " prev=" + next.mPrevAffiliate + " setting prev=" + prev);
                next.setPrevAffiliate(prev);
                notifyTaskPersisterLocked(next, false);
            }
            if (prev.mNextAffiliate != next) {
                android.util.Slog.w(TAG, "Link error 3 prev=" + prev + " next=" + prev.mNextAffiliate + " setting next=" + next);
                prev.setNextAffiliate(next);
                notifyTaskPersisterLocked(prev, false);
            }
            prev.inRecents = true;
        }
        com.android.server.wm.Task last = this.mTmpRecents.get(tmpSize - 1);
        if (last.mPrevAffiliate != null) {
            android.util.Slog.w(TAG, "Link error 4 last.prev=" + last.mPrevAffiliate);
            last.setPrevAffiliate(null);
            notifyTaskPersisterLocked(last, false);
        }
        this.mTasks.addAll(start, this.mTmpRecents);
        this.mTmpRecents.clear();
        return start + tmpSize;
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x008d, code lost:
    
        android.util.Slog.wtf(com.android.server.wm.RecentTasks.TAG, "Bad chain @" + r7 + ": first task has next affiliate: " + r10);
        r6 = false;
     */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00c8 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean moveAffiliatedTasksToFront(com.android.server.wm.Task r18, int r19) {
        /*
            Method dump skipped, instruction units count: 668
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.RecentTasks.moveAffiliatedTasksToFront(com.android.server.wm.Task, int):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x01f7  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0136  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void dump(java.io.PrintWriter r17, boolean r18, java.lang.String r19) {
        /*
            Method dump skipped, instruction units count: 546
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.RecentTasks.dump(java.io.PrintWriter, boolean, java.lang.String):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    android.app.ActivityManager.RecentTaskInfo createRecentTaskInfo(com.android.server.wm.Task task, boolean stripExtras, boolean getTasksAllowed) {
        com.android.server.wm.TaskDisplayArea tda;
        android.app.ActivityManager.RecentTaskInfo rti = new android.app.ActivityManager.RecentTaskInfo();
        if (task.isAttached()) {
            tda = task.getDisplayArea();
        } else {
            tda = this.mService.mRootWindowContainer.getDefaultTaskDisplayArea();
        }
        task.fillTaskInfo(rti, stripExtras, tda);
        rti.id = rti.isRunning ? rti.taskId : -1;
        rti.persistentId = rti.taskId;
        rti.lastSnapshotData.set(task.mLastTaskSnapshotData);
        if (!getTasksAllowed) {
            com.android.server.wm.Task.trimIneffectiveInfo(task, rti);
        }
        if (task.mCreatedByOrganizer) {
            for (int i = task.getChildCount() - 1; i >= 0; i--) {
                com.android.server.wm.Task childTask = task.getChildAt(i).asTask();
                if (childTask != null && childTask.isOrganized()) {
                    android.app.ActivityManager.RecentTaskInfo cti = new android.app.ActivityManager.RecentTaskInfo();
                    childTask.fillTaskInfo(cti, true, tda);
                    rti.childrenTaskInfos.add(cti);
                }
            }
        }
        return rti;
    }

    private boolean hasCompatibleActivityTypeAndWindowingMode(com.android.server.wm.Task t1, com.android.server.wm.Task t2) {
        int activityType = t1.getActivityType();
        int windowingMode = t1.getWindowingMode();
        boolean isUndefinedType = activityType == 0;
        boolean isUndefinedMode = windowingMode == 0;
        int otherActivityType = t2.getActivityType();
        int otherWindowingMode = t2.getWindowingMode();
        boolean isOtherUndefinedType = otherActivityType == 0;
        boolean isOtherUndefinedMode = otherWindowingMode == 0;
        boolean isCompatibleType = activityType == otherActivityType || isUndefinedType || isOtherUndefinedType;
        boolean isCompatibleMode = windowingMode == otherWindowingMode || isUndefinedMode || isOtherUndefinedMode || this.mRecentTasksExt.isLaunchedFromMultiSearch(t1, t2) || this.mRecentTasksExt.hasCompatibleActivityTypeAndWindowingMode(windowingMode, otherWindowingMode);
        return isCompatibleType && isCompatibleMode;
    }

    public com.android.server.wm.IRecentTasksWrapper getWrapper() {
        return this.mRecentTasksWrapper;
    }

    private class RecentTasksWrapper implements com.android.server.wm.IRecentTasksWrapper {
        private RecentTasksWrapper() {
        }

        @Override // com.android.server.wm.IRecentTasksWrapper
        public java.util.ArrayList<com.android.server.wm.Task> getHiddenTasks() {
            return com.android.server.wm.RecentTasks.this.mHiddenTasks;
        }
    }
}
