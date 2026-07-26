package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TaskOrganizerController extends android.window.ITaskOrganizerController.Stub {
    private static final java.lang.String TAG = "TaskOrganizerController";
    static com.android.server.wm.ITaskOrganizerControllerExt mExt;
    private final com.android.server.wm.WindowManagerGlobalLock mGlobalLock;
    private final com.android.server.wm.ActivityTaskManagerService mService;
    private final java.util.ArrayDeque<android.window.ITaskOrganizer> mTaskOrganizers = new java.util.ArrayDeque<>();
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.wm.TaskOrganizerController.TaskOrganizerState> mTaskOrganizerStates = new android.util.ArrayMap<>();
    private final java.util.HashSet<java.lang.Integer> mInterceptBackPressedOnRootTasks = new java.util.HashSet<>();

    class DeathRecipient implements android.os.IBinder.DeathRecipient {
        android.window.ITaskOrganizer mTaskOrganizer;

        DeathRecipient(android.window.ITaskOrganizer organizer) {
            this.mTaskOrganizer = organizer;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.TaskOrganizerController.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.TaskOrganizerController.TaskOrganizerState state = (com.android.server.wm.TaskOrganizerController.TaskOrganizerState) com.android.server.wm.TaskOrganizerController.this.mTaskOrganizerStates.get(this.mTaskOrganizer.asBinder());
                    if (state != null) {
                        state.dispose();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    private static class TaskOrganizerCallbacks {
        final android.window.ITaskOrganizer mTaskOrganizer;

        TaskOrganizerCallbacks(android.window.ITaskOrganizer taskOrg) {
            this.mTaskOrganizer = taskOrg;
        }

        android.os.IBinder getBinder() {
            return this.mTaskOrganizer.asBinder();
        }

        android.view.SurfaceControl prepareLeash(com.android.server.wm.Task task, java.lang.String reason) {
            return new android.view.SurfaceControl(task.getSurfaceControl(), reason);
        }

        void onTaskAppeared(com.android.server.wm.Task task) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                long protoLogParam0 = task.mTaskId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -6181189296332065162L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
            android.app.ActivityManager.RunningTaskInfo taskInfo = task.getTaskInfo();
            try {
                com.android.server.wm.utils.LogUtil.debuglogD(com.android.server.wm.TaskOrganizerController.TAG, "TaskOrganizerCallbacks.onTaskAppeared");
                this.mTaskOrganizer.onTaskAppeared(taskInfo, prepareLeash(task, "TaskOrganizerController.onTaskAppeared"));
                com.android.server.wm.TaskOrganizerController.mExt.onTaskAppeared(task, taskInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.wm.TaskOrganizerController.TAG, "Exception sending onTaskAppeared callback", e);
            }
        }

        void onTaskVanished(com.android.server.wm.Task task) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                long protoLogParam0 = task.mTaskId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 6535296991997214354L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
            android.app.ActivityManager.RunningTaskInfo taskInfo = task.getTaskInfo();
            try {
                this.mTaskOrganizer.onTaskVanished(taskInfo);
                com.android.server.wm.TaskOrganizerController.mExt.onTaskVanished(task, taskInfo);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.wm.TaskOrganizerController.TAG, "Exception sending onTaskVanished callback", e);
            }
        }

        void onTaskInfoChanged(com.android.server.wm.Task task, android.app.ActivityManager.RunningTaskInfo taskInfo) {
            if (!task.mTaskAppearedSent) {
                return;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                long protoLogParam0 = task.mTaskId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -6638141753476761854L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
            if (task.isOrganized()) {
                try {
                    android.util.Slog.i(com.android.server.wm.TaskOrganizerController.TAG, "onTaskInfoChanged taskid " + task.mTaskId + " config " + taskInfo.configuration.windowConfiguration);
                    if (taskInfo.token == null) {
                        taskInfo.token = task.mRemoteToken.toWindowContainerToken();
                        android.util.Slog.d(com.android.server.wm.TaskOrganizerController.TAG, "taskInfo.token == null, reload token -> " + taskInfo.token);
                    }
                    this.mTaskOrganizer.onTaskInfoChanged(taskInfo);
                    com.android.server.wm.TaskOrganizerController.mExt.onTaskInfoChanged(task, taskInfo);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.wm.TaskOrganizerController.TAG, "Exception sending onTaskInfoChanged callback", e);
                }
            }
        }

        void onBackPressedOnTaskRoot(com.android.server.wm.Task task) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                long protoLogParam0 = task.mTaskId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -8100069665346602959L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
            if (!task.mTaskAppearedSent || !task.isOrganized()) {
                return;
            }
            try {
                this.mTaskOrganizer.onBackPressedOnTaskRoot(task.getTaskInfo());
                com.android.server.wm.TaskOrganizerController.mExt.onBackPressedOnTaskRoot(task, task.getTaskInfo());
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.wm.TaskOrganizerController.TAG, "Exception sending onBackPressedOnTaskRoot callback", e);
            }
        }
    }

    static final class TaskOrganizerPendingEventsQueue {
        private final com.android.server.wm.TaskOrganizerController.TaskOrganizerState mOrganizerState;
        private android.app.ActivityManager.RunningTaskInfo mTmpTaskInfo;
        private final java.util.WeakHashMap<com.android.server.wm.Task, android.app.ActivityManager.RunningTaskInfo> mLastSentTaskInfos = new java.util.WeakHashMap<>();
        private boolean mNeedResetTmpTaskInfo = true;
        private final java.util.ArrayList<com.android.server.wm.TaskOrganizerController.PendingTaskEvent> mPendingTaskEvents = new java.util.ArrayList<>();

        TaskOrganizerPendingEventsQueue(com.android.server.wm.TaskOrganizerController.TaskOrganizerState taskOrganizerState) {
            this.mOrganizerState = taskOrganizerState;
        }

        public java.util.ArrayList<com.android.server.wm.TaskOrganizerController.PendingTaskEvent> getPendingEventList() {
            return this.mPendingTaskEvents;
        }

        int numPendingTaskEvents() {
            return this.mPendingTaskEvents.size();
        }

        void clearPendingTaskEvents() {
            com.android.server.wm.TaskOrganizerController.checkThreadSafety();
            this.mPendingTaskEvents.clear();
        }

        void addPendingTaskEvent(com.android.server.wm.TaskOrganizerController.PendingTaskEvent event) {
            this.mPendingTaskEvents.add(event);
        }

        void removePendingTaskEvent(com.android.server.wm.TaskOrganizerController.PendingTaskEvent event) {
            com.android.server.wm.TaskOrganizerController.checkThreadSafety();
            this.mPendingTaskEvents.remove(event);
        }

        boolean removePendingTaskEvents(com.android.server.wm.Task task) {
            boolean foundPendingAppearedEvents = false;
            for (int i = this.mPendingTaskEvents.size() - 1; i >= 0; i--) {
                com.android.server.wm.TaskOrganizerController.PendingTaskEvent entry = this.mPendingTaskEvents.get(i);
                if (task.mTaskId == entry.mTask.mTaskId) {
                    this.mPendingTaskEvents.remove(i);
                    com.android.server.wm.TaskOrganizerController.checkThreadSafety();
                    if (entry.mEventType == 0) {
                        foundPendingAppearedEvents = true;
                    }
                }
            }
            return foundPendingAppearedEvents;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.TaskOrganizerController.PendingTaskEvent getPendingTaskEvent(com.android.server.wm.Task task, int type) {
            for (int i = this.mPendingTaskEvents.size() - 1; i >= 0; i--) {
                com.android.server.wm.TaskOrganizerController.PendingTaskEvent entry = this.mPendingTaskEvents.get(i);
                if (task.mTaskId == entry.mTask.mTaskId && type == entry.mEventType) {
                    return entry;
                }
            }
            return null;
        }

        com.android.server.wm.TaskOrganizerController.PendingTaskEvent getPendingLifecycleTaskEvent(com.android.server.wm.Task task) {
            for (int i = this.mPendingTaskEvents.size() - 1; i >= 0; i--) {
                com.android.server.wm.TaskOrganizerController.PendingTaskEvent entry = this.mPendingTaskEvents.get(i);
                if (task.mTaskId == entry.mTask.mTaskId && entry.isLifecycleEvent()) {
                    return entry;
                }
            }
            return null;
        }

        void dispatchPendingEvents() {
            if (this.mPendingTaskEvents.isEmpty()) {
                return;
            }
            int n = this.mPendingTaskEvents.size();
            for (int i = 0; i < n; i++) {
                dispatchPendingEvent(this.mPendingTaskEvents.get(i));
            }
            this.mPendingTaskEvents.clear();
            com.android.server.wm.TaskOrganizerController.checkThreadSafety();
        }

        private void dispatchPendingEvent(com.android.server.wm.TaskOrganizerController.PendingTaskEvent event) {
            com.android.server.wm.Task task = event.mTask;
            switch (event.mEventType) {
                case 0:
                    if (task.taskAppearedReady()) {
                        this.mOrganizerState.mOrganizer.onTaskAppeared(task);
                    }
                    break;
                case 1:
                    this.mOrganizerState.mOrganizer.onTaskVanished(task);
                    com.android.server.wm.TaskOrganizerController.mExt.onTaskVanished(task, task.getTaskInfo());
                    this.mLastSentTaskInfos.remove(task);
                    break;
                case 2:
                    dispatchTaskInfoChanged(event.mTask, event.mForce);
                    break;
                case 3:
                    this.mOrganizerState.mOrganizer.onBackPressedOnTaskRoot(task);
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dispatchTaskInfoChanged(com.android.server.wm.Task task, boolean force) {
            android.app.ActivityManager.RunningTaskInfo lastInfo = this.mLastSentTaskInfos.get(task);
            if (this.mNeedResetTmpTaskInfo) {
                this.mTmpTaskInfo = new android.app.ActivityManager.RunningTaskInfo();
            }
            this.mTmpTaskInfo.configuration.unset();
            task.fillTaskInfo(this.mTmpTaskInfo);
            boolean changed = (this.mTmpTaskInfo.equalsForTaskOrganizer(lastInfo) && com.android.server.wm.WindowOrganizerController.configurationsAreEqualForOrganizer(this.mTmpTaskInfo.configuration, lastInfo.configuration)) ? false : true;
            if (!changed) {
                changed |= !com.android.server.wm.TaskOrganizerController.mExt.sameTaskInfoForSplitScreen(this.mTmpTaskInfo, lastInfo);
            }
            if (!com.android.server.wm.TaskOrganizerController.mExt.shouldDispatchTaskInfoChangedForEmbeddedTask(task, changed || com.android.server.wm.TaskOrganizerController.mExt.shouldDispatchTaskInfoChanged(this.mTmpTaskInfo, lastInfo)) && !force) {
                this.mNeedResetTmpTaskInfo = false;
                return;
            }
            android.app.ActivityManager.RunningTaskInfo newInfo = this.mTmpTaskInfo;
            this.mLastSentTaskInfos.put(task, this.mTmpTaskInfo);
            this.mNeedResetTmpTaskInfo = true;
            if (task.isOrganized()) {
                this.mOrganizerState.mOrganizer.onTaskInfoChanged(task, newInfo);
            }
        }
    }

    class TaskOrganizerState {
        private final com.android.server.wm.TaskOrganizerController.DeathRecipient mDeathRecipient;
        private final com.android.server.wm.TaskOrganizerController.TaskOrganizerCallbacks mOrganizer;
        private final int mUid;
        private final java.util.ArrayList<com.android.server.wm.Task> mOrganizedTasks = new java.util.ArrayList<>();
        private final com.android.server.wm.TaskOrganizerController.TaskOrganizerPendingEventsQueue mPendingEventsQueue = new com.android.server.wm.TaskOrganizerController.TaskOrganizerPendingEventsQueue(this);

        TaskOrganizerState(android.window.ITaskOrganizer organizer, int uid) {
            this.mOrganizer = new com.android.server.wm.TaskOrganizerController.TaskOrganizerCallbacks(organizer);
            this.mDeathRecipient = com.android.server.wm.TaskOrganizerController.this.new DeathRecipient(organizer);
            try {
                organizer.asBinder().linkToDeath(this.mDeathRecipient, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.wm.TaskOrganizerController.TAG, "TaskOrganizer failed to register death recipient");
            }
            this.mUid = uid;
        }

        com.android.server.wm.TaskOrganizerController.DeathRecipient getDeathRecipient() {
            return this.mDeathRecipient;
        }

        com.android.server.wm.TaskOrganizerController.TaskOrganizerPendingEventsQueue getPendingEventsQueue() {
            return this.mPendingEventsQueue;
        }

        android.view.SurfaceControl addTaskWithoutCallback(com.android.server.wm.Task t, java.lang.String reason) {
            t.mTaskAppearedSent = true;
            if (!this.mOrganizedTasks.contains(t)) {
                this.mOrganizedTasks.add(t);
            }
            return this.mOrganizer.prepareLeash(t, reason);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean addTask(com.android.server.wm.Task t) {
            if (t.mTaskAppearedSent) {
                return false;
            }
            if (!this.mOrganizedTasks.contains(t)) {
                this.mOrganizedTasks.add(t);
            }
            if (!t.taskAppearedReady()) {
                return false;
            }
            t.mTaskAppearedSent = true;
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean removeTask(com.android.server.wm.Task t, boolean removeFromSystem) {
            this.mOrganizedTasks.remove(t);
            com.android.server.wm.TaskOrganizerController.this.mInterceptBackPressedOnRootTasks.remove(java.lang.Integer.valueOf(t.mTaskId));
            boolean taskAppearedSent = t.mTaskAppearedSent;
            if (taskAppearedSent) {
                if (t.getSurfaceControl() != null) {
                    t.migrateToNewSurfaceControl(t.getSyncTransaction());
                }
                t.mTaskAppearedSent = false;
            }
            if (removeFromSystem) {
                com.android.server.wm.TaskOrganizerController.this.mService.removeTask(t.mTaskId);
            }
            return taskAppearedSent;
        }

        void dispose() {
            com.android.server.wm.TaskOrganizerController.this.mTaskOrganizers.remove(this.mOrganizer.mTaskOrganizer);
            while (!this.mOrganizedTasks.isEmpty()) {
                com.android.server.wm.Task t = this.mOrganizedTasks.get(0);
                if (t.mCreatedByOrganizer) {
                    t.removeImmediately();
                } else {
                    t.updateTaskOrganizerState();
                }
                if (this.mOrganizedTasks.contains(t) && removeTask(t, t.mRemoveWithTaskOrganizer)) {
                    com.android.server.wm.TaskOrganizerController.this.onTaskVanishedInternal(this, t);
                }
                if (com.android.server.wm.TaskOrganizerController.this.mService.getTransitionController().isShellTransitionsEnabled() && t.mTaskOrganizer != null && t.getSurfaceControl() != null) {
                    t.getSyncTransaction().show(t.getSurfaceControl());
                }
            }
            this.mPendingEventsQueue.clearPendingTaskEvents();
            com.android.server.wm.TaskOrganizerController.this.mTaskOrganizerStates.remove(this.mOrganizer.getBinder());
        }

        void unlinkDeath() {
            this.mOrganizer.getBinder().unlinkToDeath(this.mDeathRecipient, 0);
        }
    }

    static class PendingTaskEvent {
        static final int EVENT_APPEARED = 0;
        static final int EVENT_INFO_CHANGED = 2;
        static final int EVENT_ROOT_BACK_PRESSED = 3;
        static final int EVENT_VANISHED = 1;
        final int mEventType;
        boolean mForce;
        final com.android.server.wm.Task mTask;
        final android.window.ITaskOrganizer mTaskOrg;

        PendingTaskEvent(com.android.server.wm.Task task, int event) {
            this(task, task.mTaskOrganizer, event);
        }

        PendingTaskEvent(com.android.server.wm.Task task, android.window.ITaskOrganizer taskOrg, int eventType) {
            this.mTask = task;
            this.mTaskOrg = taskOrg;
            this.mEventType = eventType;
        }

        boolean isLifecycleEvent() {
            return this.mEventType == 0 || this.mEventType == 1 || this.mEventType == 2;
        }
    }

    TaskOrganizerController(com.android.server.wm.ActivityTaskManagerService atm) {
        this.mService = atm;
        this.mGlobalLock = atm.mGlobalLock;
        mExt = (com.android.server.wm.ITaskOrganizerControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITaskOrganizerControllerExt.class).base(this).create();
        mExt.setWmsLock(this.mGlobalLock);
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        try {
            return super.onTransact(code, data, reply, flags);
        } catch (java.lang.RuntimeException e) {
            throw com.android.server.wm.ActivityTaskManagerService.logAndRethrowRuntimeExceptionOnTransact(TAG, e);
        }
    }

    public android.content.pm.ParceledListSlice<android.window.TaskAppearedInfo> registerTaskOrganizer(final android.window.ITaskOrganizer organizer) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("registerTaskOrganizer()");
        final int uid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            final java.util.ArrayList<android.window.TaskAppearedInfo> taskInfos = new java.util.ArrayList<>();
            java.lang.Runnable withGlobalLock = new java.lang.Runnable() { // from class: com.android.server.wm.TaskOrganizerController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$registerTaskOrganizer$1(organizer, uid, taskInfos);
                }
            };
            if (this.mService.getTransitionController().isShellTransitionsEnabled()) {
                this.mService.getTransitionController().mRunningLock.runWhenIdle(1000L, withGlobalLock);
            } else {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        withGlobalLock.run();
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
            return new android.content.pm.ParceledListSlice<>(taskInfos);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerTaskOrganizer$1(android.window.ITaskOrganizer organizer, int uid, final java.util.ArrayList taskInfos) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(organizer.asBinder());
            long protoLogParam1 = uid;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -610138383571469481L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
        }
        if (!this.mTaskOrganizerStates.containsKey(organizer.asBinder())) {
            this.mTaskOrganizers.add(organizer);
            this.mTaskOrganizerStates.put(organizer.asBinder(), new com.android.server.wm.TaskOrganizerController.TaskOrganizerState(organizer, uid));
        }
        final com.android.server.wm.TaskOrganizerController.TaskOrganizerState state = this.mTaskOrganizerStates.get(organizer.asBinder());
        this.mService.mRootWindowContainer.forAllTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskOrganizerController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.TaskOrganizerController.lambda$registerTaskOrganizer$0(state, taskInfos, (com.android.server.wm.Task) obj);
            }
        });
    }

    static /* synthetic */ void lambda$registerTaskOrganizer$0(com.android.server.wm.TaskOrganizerController.TaskOrganizerState state, java.util.ArrayList taskInfos, com.android.server.wm.Task task) {
        boolean returnTask = !task.mCreatedByOrganizer;
        task.updateTaskOrganizerState(returnTask);
        if (task.isOrganized() && returnTask && task.getSurfaceControl() != null && task.getSurfaceControl().isValid()) {
            android.view.SurfaceControl taskLeash = state.addTaskWithoutCallback(task, "TaskOrganizerController.registerTaskOrganizer");
            taskInfos.add(new android.window.TaskAppearedInfo(task.getTaskInfo(), taskLeash));
        }
    }

    public void unregisterTaskOrganizer(final android.window.ITaskOrganizer organizer) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("unregisterTaskOrganizer()");
        final int uid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.Runnable withGlobalLock = new java.lang.Runnable() { // from class: com.android.server.wm.TaskOrganizerController$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$unregisterTaskOrganizer$2(organizer, uid);
                }
            };
            if (this.mService.getTransitionController().isShellTransitionsEnabled()) {
                this.mService.getTransitionController().mRunningLock.runWhenIdle(1000L, withGlobalLock);
            } else {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        withGlobalLock.run();
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterTaskOrganizer$2(android.window.ITaskOrganizer organizer, int uid) {
        com.android.server.wm.TaskOrganizerController.TaskOrganizerState state = this.mTaskOrganizerStates.get(organizer.asBinder());
        if (state == null) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(organizer.asBinder());
            long protoLogParam1 = uid;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 1705860547080436016L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
        }
        state.unlinkDeath();
        state.dispose();
    }

    android.window.ITaskOrganizer getTaskOrganizer() {
        return this.mTaskOrganizers.peekLast();
    }

    static class StartingWindowAnimationAdaptor implements com.android.server.wm.AnimationAdapter {
        StartingWindowAnimationAdaptor() {
        }

        @Override // com.android.server.wm.AnimationAdapter
        public boolean getShowWallpaper() {
            return false;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void startAnimation(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, int type, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishCallback) {
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void onAnimationCancelled(android.view.SurfaceControl animationLeash) {
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getDurationHint() {
            return 0L;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getStatusBarTransitionsStartTime() {
            return 0L;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dumpDebug(android.util.proto.ProtoOutputStream proto) {
        }
    }

    static android.view.SurfaceControl applyStartingWindowAnimation(com.android.server.wm.WindowState window) {
        android.view.SurfaceControl.Transaction t = window.getPendingTransaction();
        com.android.server.wm.TaskOrganizerController.StartingWindowAnimationAdaptor adaptor = new com.android.server.wm.TaskOrganizerController.StartingWindowAnimationAdaptor();
        window.startAnimation(t, adaptor, false, 128);
        android.view.SurfaceControl leash = window.getAnimationLeash();
        if (leash == null) {
            android.util.Slog.e(TAG, "Cannot start starting window animation, the window " + window + " was removed");
            return null;
        }
        if (leash == null || !leash.isValid()) {
            android.util.Slog.e(TAG, "Cannot start starting window animation, the window " + window + "was removed");
            return null;
        }
        t.setPosition(leash, window.mSurfacePosition.x, window.mSurfacePosition.y);
        if (window.getWrapper().getExtImpl().adjustPosForComapctWindow(window)) {
            t.setPosition(leash, 0.0f, window.mSurfacePosition.y);
        }
        window.getWCWrapper().getExtImpl().enablePendingApplyTransition(window, t);
        return leash;
    }

    boolean addStartingWindow(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord activity, int launchTheme, android.window.TaskSnapshot taskSnapshot) {
        android.window.ITaskOrganizer lastOrganizer;
        com.android.server.wm.Task rootTask = task.getRootTask();
        if (rootTask == null || activity.mStartingData == null || (lastOrganizer = getTaskOrganizer()) == null) {
            return false;
        }
        android.window.StartingWindowInfo info = task.getStartingWindowInfo(activity);
        if (launchTheme != 0) {
            info.splashScreenThemeResId = launchTheme;
        }
        info.taskSnapshot = taskSnapshot;
        this.mService.mTaskSupervisor.getWrapper().getExtImpl().markScheduleAddStartingWindow(activity);
        mExt.hookAddStartingWindow(activity, info);
        info.appToken = activity.token;
        try {
            mExt.hookSetBinderUxFlag(true);
            com.android.server.wm.utils.LogUtil.d(TAG, "TaskOrganizerController.addStartingWindow: call wmshell to addStartingWindow");
            lastOrganizer.addStartingWindow(info);
            mExt.hookSetBinderUxFlag(false);
            return true;
        } catch (android.os.RemoteException e) {
            mExt.hookSetBinderUxFlag(false);
            android.util.Slog.e(TAG, "Exception sending onTaskStart callback", e);
            return false;
        }
    }

    void removeStartingWindow(com.android.server.wm.Task task, android.window.ITaskOrganizer taskOrganizer, boolean prepareAnimation, boolean hasImeSurface) {
        com.android.server.wm.Task rootTask = task.getRootTask();
        if (rootTask == null) {
            return;
        }
        android.window.ITaskOrganizer lastOrganizer = taskOrganizer != null ? taskOrganizer : getTaskOrganizer();
        if (lastOrganizer == null) {
            return;
        }
        android.window.StartingWindowRemovalInfo removalInfo = new android.window.StartingWindowRemovalInfo();
        removalInfo.taskId = task.mTaskId;
        removalInfo.playRevealAnimation = prepareAnimation && task.getDisplayContent() != null && task.getDisplayInfo().state == 2;
        boolean playShiftUpAnimation = !task.inMultiWindowMode();
        com.android.server.wm.ActivityRecord topActivity = task.topActivityContainsStartingWindow();
        if (topActivity != null) {
            com.android.server.wm.DisplayContent dc = topActivity.getDisplayContent();
            if (hasImeSurface) {
                if (topActivity.isVisibleRequested() && dc.mInputMethodWindow != null && dc.isFixedRotationLaunchingApp(topActivity)) {
                    removalInfo.deferRemoveMode = 2;
                } else {
                    removalInfo.deferRemoveMode = 1;
                }
            }
            com.android.server.wm.WindowState mainWindow = topActivity.findMainWindow(false);
            if (mainWindow == null || mainWindow.mRemoved) {
                removalInfo.playRevealAnimation = false;
            } else if (removalInfo.playRevealAnimation && playShiftUpAnimation && mExt.playShiftUpAnimation(topActivity)) {
                removalInfo.roundedCornerRadius = topActivity.mLetterboxUiController.getRoundedCornersRadius(mainWindow);
                removalInfo.windowAnimationLeash = applyStartingWindowAnimation(mainWindow);
                removalInfo.mainFrame = new android.graphics.Rect(mainWindow.getFrame());
                removalInfo.mainFrame.offsetTo(mainWindow.mSurfacePosition.x, mainWindow.mSurfacePosition.y);
                if (mainWindow.getWrapper().getExtImpl().adjustPosForComapctWindow(mainWindow)) {
                    removalInfo.mainFrame.left = 0;
                }
            }
        }
        mExt.removeStartingWindow(removalInfo, task);
        try {
            com.android.server.wm.utils.LogUtil.d(TAG, "TaskOrganizerController.removeStartingWindow: call wmshell to removeStartingWindow");
            lastOrganizer.removeStartingWindow(removalInfo);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Exception sending onStartTaskFinished callback", e);
        }
    }

    int addWindowlessStartingSurface(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord activity, android.view.SurfaceControl root, android.window.TaskSnapshot taskSnapshot, android.content.res.Configuration configuration, android.window.IWindowlessStartingSurfaceCallback callback) {
        android.window.ITaskOrganizer lastOrganizer;
        com.android.server.wm.Task rootTask = task.getRootTask();
        if (rootTask == null || (lastOrganizer = this.mTaskOrganizers.peekLast()) == null) {
            return -1;
        }
        android.window.StartingWindowInfo info = task.getStartingWindowInfo(activity);
        info.taskInfo.configuration.setTo(configuration);
        info.taskInfo.taskDescription = activity.taskDescription;
        info.taskSnapshot = taskSnapshot;
        info.windowlessStartingSurfaceCallback = callback;
        info.rootSurface = root;
        try {
            lastOrganizer.addStartingWindow(info);
            return task.mTaskId;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Exception sending addWindowlessStartingSurface ", e);
            return -1;
        }
    }

    void removeWindowlessStartingSurface(int taskId, boolean immediately) {
        android.window.ITaskOrganizer lastOrganizer = this.mTaskOrganizers.peekLast();
        if (lastOrganizer == null || taskId == 0) {
            return;
        }
        android.window.StartingWindowRemovalInfo removalInfo = new android.window.StartingWindowRemovalInfo();
        removalInfo.taskId = taskId;
        removalInfo.windowlessSurface = true;
        removalInfo.removeImmediately = immediately;
        removalInfo.deferRemoveMode = 3;
        try {
            lastOrganizer.removeStartingWindow(removalInfo);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Exception sending removeWindowlessStartingSurface ", e);
        }
    }

    boolean copySplashScreenView(com.android.server.wm.Task task, android.window.ITaskOrganizer taskOrganizer) {
        com.android.server.wm.Task rootTask = task.getRootTask();
        if (rootTask == null) {
            return false;
        }
        android.window.ITaskOrganizer lastOrganizer = taskOrganizer != null ? taskOrganizer : getTaskOrganizer();
        if (lastOrganizer == null) {
            return false;
        }
        try {
            lastOrganizer.copySplashScreenView(task.mTaskId);
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Exception sending copyStartingWindowView callback", e);
            return false;
        }
    }

    boolean isSupportWindowlessStartingSurface() {
        android.window.ITaskOrganizer lastOrganizer = this.mTaskOrganizers.peekLast();
        return lastOrganizer != null;
    }

    public void onAppSplashScreenViewRemoved(com.android.server.wm.Task task, android.window.ITaskOrganizer organizer) {
        com.android.server.wm.Task rootTask = task.getRootTask();
        if (rootTask == null) {
            return;
        }
        android.window.ITaskOrganizer lastOrganizer = organizer != null ? organizer : getTaskOrganizer();
        if (lastOrganizer == null) {
            return;
        }
        try {
            rootTask.getWrapper().getExtImpl().setSplashScreenViewCopy(false);
            lastOrganizer.onAppSplashScreenViewRemoved(task.mTaskId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Exception sending onAppSplashScreenViewRemoved callback", e);
        }
    }

    void onTaskAppeared(android.window.ITaskOrganizer organizer, com.android.server.wm.Task task) {
        com.android.server.wm.TaskOrganizerController.TaskOrganizerState state = this.mTaskOrganizerStates.get(organizer.asBinder());
        if (state != null && state.addTask(task)) {
            com.android.server.wm.TaskOrganizerController.TaskOrganizerPendingEventsQueue pendingEvents = state.mPendingEventsQueue;
            com.android.server.wm.TaskOrganizerController.PendingTaskEvent pending = pendingEvents.getPendingTaskEvent(task, 0);
            if (pending == null) {
                pendingEvents.addPendingTaskEvent(new com.android.server.wm.TaskOrganizerController.PendingTaskEvent(task, 0));
            }
        }
    }

    void onTaskVanished(android.window.ITaskOrganizer organizer, com.android.server.wm.Task task) {
        com.android.server.wm.TaskOrganizerController.TaskOrganizerState state = this.mTaskOrganizerStates.get(organizer.asBinder());
        if (state != null && state.removeTask(task, task.mRemoveWithTaskOrganizer)) {
            onTaskVanishedInternal(state, task);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTaskVanishedInternal(com.android.server.wm.TaskOrganizerController.TaskOrganizerState organizerState, com.android.server.wm.Task task) {
        if (organizerState == null) {
            android.util.Slog.i(TAG, "cannot send onTaskVanished because organizer state is not present for this organizer");
            return;
        }
        com.android.server.wm.TaskOrganizerController.TaskOrganizerPendingEventsQueue pendingEventsQueue = organizerState.mPendingEventsQueue;
        boolean hadPendingAppearedEvents = pendingEventsQueue.removePendingTaskEvents(task);
        if (hadPendingAppearedEvents) {
            return;
        }
        pendingEventsQueue.addPendingTaskEvent(new com.android.server.wm.TaskOrganizerController.PendingTaskEvent(task, organizerState.mOrganizer.mTaskOrganizer, 1));
    }

    public void createRootTask(int displayId, int windowingMode, android.os.IBinder launchCookie, boolean removeWithTaskOrganizer) throws java.lang.Throwable {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock;
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("createRootTask()");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        } catch (java.lang.Throwable th) {
            th = th;
        }
        try {
            try {
                synchronized (windowManagerGlobalLock) {
                    try {
                        com.android.server.wm.DisplayContent display = this.mService.mRootWindowContainer.getDisplayContent(displayId);
                        if (display != null) {
                            createRootTask(display, windowingMode, launchCookie, removeWithTaskOrganizer);
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            android.os.Binder.restoreCallingIdentity(origId);
                        } else {
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[4]) {
                                long protoLogParam0 = displayId;
                                com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -2286607251115721394L, 1, null, java.lang.Long.valueOf(protoLogParam0));
                            }
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            android.os.Binder.restoreCallingIdentity(origId);
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        } catch (java.lang.Throwable th4) {
            th = th4;
            android.os.Binder.restoreCallingIdentity(origId);
            throw th;
        }
    }

    com.android.server.wm.Task createRootTask(com.android.server.wm.DisplayContent display, int windowingMode, android.os.IBinder launchCookie) {
        return createRootTask(display, windowingMode, launchCookie, false);
    }

    com.android.server.wm.Task createRootTask(com.android.server.wm.DisplayContent display, int windowingMode, android.os.IBinder launchCookie, boolean removeWithTaskOrganizer) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
            long protoLogParam0 = display.mDisplayId;
            long protoLogParam1 = windowingMode;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 8466395828406204368L, 5, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
        }
        com.android.server.wm.Task task = new com.android.server.wm.Task.Builder(this.mService).setWindowingMode(windowingMode).setIntent(new android.content.Intent()).setCreatedByOrganizer(true).setDeferTaskAppear(true).setLaunchCookie(launchCookie).setParent(display.getDefaultTaskDisplayArea()).setRemoveWithTaskOrganizer(removeWithTaskOrganizer).build();
        task.setDeferTaskAppear(false);
        return task;
    }

    public boolean deleteRootTask(android.window.WindowContainerToken token) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("deleteRootTask()");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowContainer wc = com.android.server.wm.WindowContainer.fromBinder(token.asBinder());
                    if (wc == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    com.android.server.wm.Task task = wc.asTask();
                    if (task == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    if (!task.mCreatedByOrganizer) {
                        throw new java.lang.IllegalArgumentException("Attempt to delete task not created by organizer task=" + task);
                    }
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                        long protoLogParam0 = task.getDisplayId();
                        long protoLogParam1 = task.getWindowingMode();
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 6867170298997192615L, 5, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
                    }
                    task.remove(true, "deleteRootTask");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return true;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    void dispatchPendingEvents() {
        if (this.mService.mWindowManager.mWindowPlacerLocked.isLayoutDeferred()) {
            return;
        }
        for (int taskOrgIdx = 0; taskOrgIdx < this.mTaskOrganizerStates.size(); taskOrgIdx++) {
            com.android.server.wm.TaskOrganizerController.TaskOrganizerState taskOrganizerState = this.mTaskOrganizerStates.valueAt(taskOrgIdx);
            taskOrganizerState.mPendingEventsQueue.dispatchPendingEvents();
        }
    }

    void reportImeDrawnOnTask(com.android.server.wm.Task task) {
        com.android.server.wm.TaskOrganizerController.TaskOrganizerState state = this.mTaskOrganizerStates.get(task.mTaskOrganizer.asBinder());
        if (state != null) {
            try {
                state.mOrganizer.mTaskOrganizer.onImeDrawnOnTask(task.mTaskId);
                mExt.reportImeDrawnOnTask(task.mTaskId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Exception sending onImeDrawnOnTask callback", e);
            }
        }
    }

    void onTaskInfoChanged(com.android.server.wm.Task task, boolean force) {
        if (!task.mTaskAppearedSent) {
            return;
        }
        com.android.server.wm.TaskOrganizerController.TaskOrganizerState taskOrganizerState = this.mTaskOrganizerStates.get(task.mTaskOrganizer.asBinder());
        if (taskOrganizerState == null) {
            android.util.Slog.i(TAG, "cannot send onTaskInfoChanged because task organizer state is not present for this organizer");
            return;
        }
        com.android.server.wm.TaskOrganizerController.TaskOrganizerPendingEventsQueue pendingEventsQueue = taskOrganizerState.mPendingEventsQueue;
        if (pendingEventsQueue == null) {
            android.util.Slog.i(TAG, "cannot send onTaskInfoChanged because pending events queue is not present for this organizer");
            return;
        }
        if (force && pendingEventsQueue.numPendingTaskEvents() == 0) {
            pendingEventsQueue.dispatchTaskInfoChanged(task, true);
            return;
        }
        com.android.server.wm.TaskOrganizerController.PendingTaskEvent pending = pendingEventsQueue.getPendingLifecycleTaskEvent(task);
        if (pending != null) {
            if (pending.mEventType != 2) {
                return;
            } else {
                pendingEventsQueue.removePendingTaskEvent(pending);
            }
        } else {
            pending = new com.android.server.wm.TaskOrganizerController.PendingTaskEvent(task, 2);
        }
        pending.mForce |= force;
        pendingEventsQueue.addPendingTaskEvent(pending);
    }

    public android.window.WindowContainerToken getImeTarget(int displayId) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("getImeTarget()");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = this.mService.mWindowManager.mRoot.getDisplayContent(displayId);
                    if (dc == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    com.android.server.wm.InsetsControlTarget imeLayeringTarget = dc.getImeTarget(0);
                    if (imeLayeringTarget != null && imeLayeringTarget.getWindow() != null) {
                        com.android.server.wm.Task task = imeLayeringTarget.getWindow().getTask();
                        if (task == null) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return null;
                        }
                        android.window.WindowContainerToken windowContainerToken = task.mRemoteToken.toWindowContainerToken();
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return windowContainerToken;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public java.util.List<android.app.ActivityManager.RunningTaskInfo> getChildTasks(android.window.WindowContainerToken parent, int[] activityTypes) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("getChildTasks()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (parent == null) {
                        throw new java.lang.IllegalArgumentException("Can't get children of null parent");
                    }
                    com.android.server.wm.WindowContainer container = com.android.server.wm.WindowContainer.fromBinder(parent.asBinder());
                    if (container == null) {
                        android.util.Slog.e(TAG, "Can't get children of " + parent + " because it is not valid.");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    com.android.server.wm.Task task = container.asTask();
                    if (task == null) {
                        android.util.Slog.e(TAG, container + " is not a task...");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    if (!task.mCreatedByOrganizer) {
                        android.util.Slog.w(TAG, "Can only get children of root tasks created via createRootTask");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    java.util.ArrayList<android.app.ActivityManager.RunningTaskInfo> out = new java.util.ArrayList<>();
                    for (int i = task.getChildCount() - 1; i >= 0; i--) {
                        com.android.server.wm.Task child = task.getChildAt(i).asTask();
                        if (child != null && (activityTypes == null || com.android.internal.util.ArrayUtils.contains(activityTypes, child.getActivityType()))) {
                            out.add(child.getTaskInfo());
                        }
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return out;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public java.util.List<android.app.ActivityManager.RunningTaskInfo> getRootTasks(int displayId, final int[] activityTypes) {
        final java.util.ArrayList<android.app.ActivityManager.RunningTaskInfo> out;
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("getRootTasks()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = this.mService.mRootWindowContainer.getDisplayContent(displayId);
                    if (dc == null) {
                        throw new java.lang.IllegalArgumentException("Display " + displayId + " doesn't exist");
                    }
                    out = new java.util.ArrayList<>();
                    dc.forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskOrganizerController$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            com.android.server.wm.TaskOrganizerController.lambda$getRootTasks$3(activityTypes, out, (com.android.server.wm.Task) obj);
                        }
                    });
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return out;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    static /* synthetic */ void lambda$getRootTasks$3(int[] activityTypes, java.util.ArrayList out, com.android.server.wm.Task task) {
        if (activityTypes != null && !com.android.internal.util.ArrayUtils.contains(activityTypes, task.getActivityType())) {
            return;
        }
        out.add(task.getTaskInfo());
    }

    public void setInterceptBackPressedOnTaskRoot(android.window.WindowContainerToken token, boolean interceptBackPressed) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("setInterceptBackPressedOnTaskRoot()");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -4296644831871159510L, 3, null, java.lang.Boolean.valueOf(interceptBackPressed));
                    }
                    com.android.server.wm.WindowContainer wc = com.android.server.wm.WindowContainer.fromBinder(token.asBinder());
                    if (wc == null) {
                        android.util.Slog.w(TAG, "Could not resolve window from token");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.Task task = wc.asTask();
                    if (task == null) {
                        android.util.Slog.w(TAG, "Could not resolve task from token");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    } else {
                        if (interceptBackPressed) {
                            this.mInterceptBackPressedOnRootTasks.add(java.lang.Integer.valueOf(task.mTaskId));
                        } else {
                            this.mInterceptBackPressedOnRootTasks.remove(java.lang.Integer.valueOf(task.mTaskId));
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void restartTaskTopActivityProcessIfVisible(android.window.WindowContainerToken token) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("restartTopActivityProcessIfVisible()");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowContainer wc = com.android.server.wm.WindowContainer.fromBinder(token.asBinder());
                    if (wc == null) {
                        android.util.Slog.w(TAG, "Could not resolve window from token");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.Task task = wc.asTask();
                    if (task == null) {
                        android.util.Slog.w(TAG, "Could not resolve task from token");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                        long protoLogParam0 = task.mTaskId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -558727273888268534L, 1, null, java.lang.Long.valueOf(protoLogParam0));
                    }
                    com.android.server.wm.ActivityRecord activity = task.getTopNonFinishingActivity();
                    if (activity != null) {
                        activity.restartProcessIfVisible();
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void updateCameraCompatControlState(android.window.WindowContainerToken token, int state) throws java.lang.Throwable {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("updateCameraCompatControlState()");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            try {
                try {
                    synchronized (windowManagerGlobalLock) {
                        try {
                            com.android.server.wm.WindowContainer wc = com.android.server.wm.WindowContainer.fromBinder(token.asBinder());
                            if (wc == null) {
                                android.util.Slog.w(TAG, "Could not resolve window from token");
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                android.os.Binder.restoreCallingIdentity(origId);
                                return;
                            }
                            com.android.server.wm.Task task = wc.asTask();
                            if (task == null) {
                                android.util.Slog.w(TAG, "Could not resolve task from token");
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                android.os.Binder.restoreCallingIdentity(origId);
                                return;
                            }
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                                java.lang.String protoLogParam0 = java.lang.String.valueOf(android.app.CameraCompatTaskInfo.cameraCompatControlStateToString(state));
                                long protoLogParam1 = task.mTaskId;
                                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -7064081458956324316L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
                            }
                            com.android.server.wm.ActivityRecord activity = task.getTopNonFinishingActivity();
                            if (activity != null) {
                                activity.updateCameraCompatStateFromUser(state);
                            }
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            android.os.Binder.restoreCallingIdentity(origId);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    android.os.Binder.restoreCallingIdentity(origId);
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
    }

    public boolean handleInterceptBackPressedOnTaskRoot(com.android.server.wm.Task task) {
        if (!shouldInterceptBackPressedOnRootTask(task)) {
            return false;
        }
        com.android.server.wm.TaskOrganizerController.TaskOrganizerPendingEventsQueue pendingEventsQueue = this.mTaskOrganizerStates.get(task.mTaskOrganizer.asBinder()).mPendingEventsQueue;
        if (pendingEventsQueue == null) {
            android.util.Slog.w(TAG, "cannot get handle BackPressedOnTaskRoot because organizerState is not present");
            return false;
        }
        com.android.server.wm.TaskOrganizerController.PendingTaskEvent pendingVanished = pendingEventsQueue.getPendingTaskEvent(task, 1);
        if (pendingVanished != null) {
            return false;
        }
        com.android.server.wm.TaskOrganizerController.PendingTaskEvent pending = pendingEventsQueue.getPendingTaskEvent(task, 3);
        if (pending == null) {
            pending = new com.android.server.wm.TaskOrganizerController.PendingTaskEvent(task, 3);
        } else {
            pendingEventsQueue.removePendingTaskEvent(pending);
        }
        pendingEventsQueue.addPendingTaskEvent(pending);
        this.mService.mWindowManager.mWindowPlacerLocked.requestTraversal();
        return true;
    }

    boolean shouldInterceptBackPressedOnRootTask(com.android.server.wm.Task task) {
        return task != null && task.isOrganized() && this.mInterceptBackPressedOnRootTasks.contains(java.lang.Integer.valueOf(task.mTaskId));
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        java.lang.String innerPrefix = prefix + "  ";
        pw.print(prefix);
        pw.println("TaskOrganizerController:");
        android.window.ITaskOrganizer lastOrganizer = this.mTaskOrganizers.peekLast();
        for (android.window.ITaskOrganizer organizer : this.mTaskOrganizers) {
            com.android.server.wm.TaskOrganizerController.TaskOrganizerState state = this.mTaskOrganizerStates.get(organizer.asBinder());
            java.util.ArrayList<com.android.server.wm.Task> tasks = state.mOrganizedTasks;
            pw.print(innerPrefix + "  ");
            pw.print(state.mOrganizer.mTaskOrganizer + " uid=" + state.mUid);
            if (lastOrganizer == organizer) {
                pw.print(" (active)");
            }
            pw.println(':');
            for (int k = 0; k < tasks.size(); k++) {
                com.android.server.wm.Task task = tasks.get(k);
                int mode = task.getWindowingMode();
                pw.println(innerPrefix + "    (" + android.app.WindowConfiguration.windowingModeToString(mode) + ") " + task);
            }
        }
        pw.println();
    }

    com.android.server.wm.TaskOrganizerController.TaskOrganizerState getTaskOrganizerState(android.os.IBinder taskOrganizer) {
        return this.mTaskOrganizerStates.get(taskOrganizer);
    }

    com.android.server.wm.TaskOrganizerController.TaskOrganizerPendingEventsQueue getTaskOrganizerPendingEvents(android.os.IBinder taskOrganizer) {
        return this.mTaskOrganizerStates.get(taskOrganizer).mPendingEventsQueue;
    }

    public static void checkThreadSafety() {
        mExt.checkThreadSafety();
    }
}
