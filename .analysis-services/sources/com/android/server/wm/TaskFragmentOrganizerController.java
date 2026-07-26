package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class TaskFragmentOrganizerController extends android.window.ITaskFragmentOrganizerController.Stub {
    private static final java.lang.String TAG = "TaskFragmentOrganizerController";
    private static final long TEMPORARY_ACTIVITY_TOKEN_TIMEOUT_MS = 5000;
    private final com.android.server.wm.ActivityTaskManagerService mAtmService;
    private final com.android.server.wm.WindowManagerGlobalLock mGlobalLock;
    private final com.android.server.wm.WindowOrganizerController mWindowOrganizerController;
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState> mTaskFragmentOrganizerState = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<android.os.IBinder, java.util.List<com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent>> mPendingTaskFragmentEvents = new android.util.ArrayMap<>();
    private final android.util.ArraySet<com.android.server.wm.Task> mTmpTaskSet = new android.util.ArraySet<>();

    TaskFragmentOrganizerController(com.android.server.wm.ActivityTaskManagerService atm, com.android.server.wm.WindowOrganizerController windowOrganizerController) {
        this.mAtmService = (com.android.server.wm.ActivityTaskManagerService) java.util.Objects.requireNonNull(atm);
        this.mGlobalLock = atm.mGlobalLock;
        this.mWindowOrganizerController = (com.android.server.wm.WindowOrganizerController) java.util.Objects.requireNonNull(windowOrganizerController);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class TaskFragmentOrganizerState implements android.os.IBinder.DeathRecipient {
        private final android.app.IApplicationThread mAppThread;
        private final boolean mIsSystemOrganizer;
        private final android.window.ITaskFragmentOrganizer mOrganizer;
        private final int mOrganizerPid;
        private final int mOrganizerUid;
        private android.view.RemoteAnimationDefinition mRemoteAnimationDefinition;
        private final java.util.ArrayList<com.android.server.wm.TaskFragment> mOrganizedTaskFragments = new java.util.ArrayList<>();
        private final java.util.Map<com.android.server.wm.TaskFragment, android.window.TaskFragmentInfo> mLastSentTaskFragmentInfos = new java.util.WeakHashMap();
        private final java.util.Map<com.android.server.wm.TaskFragment, java.lang.Integer> mTaskFragmentTaskIds = new java.util.WeakHashMap();
        private final android.util.SparseArray<android.window.TaskFragmentParentInfo> mLastSentTaskFragmentParentInfos = new android.util.SparseArray<>();
        private final java.util.Map<android.os.IBinder, com.android.server.wm.ActivityRecord> mTemporaryActivityTokens = new java.util.WeakHashMap();
        private final android.util.ArrayMap<android.os.IBinder, java.lang.Integer> mDeferredTransitions = new android.util.ArrayMap<>();
        private final android.util.ArrayMap<android.os.IBinder, com.android.server.wm.Transition.ReadyCondition> mInFlightTransactions = new android.util.ArrayMap<>();

        TaskFragmentOrganizerState(android.window.ITaskFragmentOrganizer organizer, int pid, int uid, boolean isSystemOrganizer) {
            if (com.android.window.flags.Flags.bundleClientTransactionFlag()) {
                this.mAppThread = com.android.server.wm.TaskFragmentOrganizerController.this.getAppThread(pid, uid);
            } else {
                this.mAppThread = null;
            }
            this.mOrganizer = organizer;
            this.mOrganizerPid = pid;
            this.mOrganizerUid = uid;
            this.mIsSystemOrganizer = isSystemOrganizer;
            try {
                this.mOrganizer.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.wm.TaskFragmentOrganizerController.TAG, "TaskFragmentOrganizer failed to register death recipient");
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.TaskFragmentOrganizerController.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.TaskFragmentOrganizerController.this.removeOrganizer(this.mOrganizer, "client died");
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        boolean addTaskFragment(com.android.server.wm.TaskFragment taskFragment) {
            if (taskFragment.mTaskFragmentAppearedSent || this.mOrganizedTaskFragments.contains(taskFragment)) {
                return false;
            }
            this.mOrganizedTaskFragments.add(taskFragment);
            return true;
        }

        void removeTaskFragment(com.android.server.wm.TaskFragment taskFragment) {
            this.mOrganizedTaskFragments.remove(taskFragment);
        }

        void dispose(java.lang.String reason) {
            boolean wasVisible = false;
            for (int i = this.mOrganizedTaskFragments.size() - 1; i >= 0; i--) {
                com.android.server.wm.TaskFragment taskFragment = this.mOrganizedTaskFragments.get(i);
                if (taskFragment.isVisibleRequested()) {
                    wasVisible = true;
                }
                taskFragment.onTaskFragmentOrganizerRemoved();
            }
            com.android.server.wm.TransitionController transitionController = com.android.server.wm.TaskFragmentOrganizerController.this.mAtmService.getTransitionController();
            if (wasVisible && transitionController.isShellTransitionsEnabled() && !transitionController.isCollecting()) {
                com.android.server.wm.Task task = this.mOrganizedTaskFragments.get(0).getTask();
                boolean containsNonEmbeddedActivity = (task == null || task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragmentOrganizerController$TaskFragmentOrganizerState$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState.lambda$dispose$0((com.android.server.wm.ActivityRecord) obj);
                    }
                }) == null) ? false : true;
                transitionController.requestStartTransition(transitionController.createTransition(2), containsNonEmbeddedActivity ? null : task, null, null);
            }
            com.android.server.wm.TaskFragmentOrganizerController.this.mAtmService.deferWindowLayout();
            while (!this.mOrganizedTaskFragments.isEmpty()) {
                try {
                    this.mOrganizedTaskFragments.remove(0).removeImmediately();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.TaskFragmentOrganizerController.this.mAtmService.continueWindowLayout();
                    throw th;
                }
            }
            com.android.server.wm.TaskFragmentOrganizerController.this.mAtmService.continueWindowLayout();
            for (int i2 = this.mDeferredTransitions.size() - 1; i2 >= 0; i2--) {
                onTransactionFinished(this.mDeferredTransitions.keyAt(i2));
            }
            for (int i3 = this.mInFlightTransactions.size() - 1; i3 >= 0; i3--) {
                this.mInFlightTransactions.valueAt(i3).meetAlternate("disposed(" + reason + ")");
            }
            this.mOrganizer.asBinder().unlinkToDeath(this, 0);
        }

        static /* synthetic */ boolean lambda$dispose$0(com.android.server.wm.ActivityRecord a) {
            return !a.isEmbedded();
        }

        android.window.TaskFragmentTransaction.Change prepareTaskFragmentAppeared(com.android.server.wm.TaskFragment tf) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(tf.getName());
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -2808577027789344626L, 0, null, protoLogParam0);
            }
            android.window.TaskFragmentInfo info = tf.getTaskFragmentInfo();
            int taskId = tf.getTask().mTaskId;
            tf.mTaskFragmentAppearedSent = true;
            this.mLastSentTaskFragmentInfos.put(tf, info);
            this.mTaskFragmentTaskIds.put(tf, java.lang.Integer.valueOf(taskId));
            android.window.TaskFragmentTransaction.Change change = new android.window.TaskFragmentTransaction.Change(1).setTaskFragmentToken(tf.getFragmentToken()).setTaskFragmentInfo(info).setTaskId(taskId);
            if (this.mIsSystemOrganizer) {
                change.setTaskFragmentSurfaceControl(tf.getSurfaceControl());
            }
            return change;
        }

        android.window.TaskFragmentTransaction.Change prepareTaskFragmentVanished(com.android.server.wm.TaskFragment tf) {
            int taskId;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(tf.getName());
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -3582112419663037270L, 0, null, protoLogParam0);
            }
            tf.mTaskFragmentAppearedSent = false;
            this.mLastSentTaskFragmentInfos.remove(tf);
            if (this.mTaskFragmentTaskIds.containsKey(tf)) {
                taskId = this.mTaskFragmentTaskIds.remove(tf).intValue();
                if (!this.mTaskFragmentTaskIds.containsValue(java.lang.Integer.valueOf(taskId))) {
                    this.mLastSentTaskFragmentParentInfos.remove(taskId);
                }
            } else {
                taskId = -1;
            }
            return new android.window.TaskFragmentTransaction.Change(3).setTaskFragmentToken(tf.getFragmentToken()).setTaskFragmentInfo(tf.getTaskFragmentInfo()).setTaskId(taskId);
        }

        android.window.TaskFragmentTransaction.Change prepareTaskFragmentInfoChanged(com.android.server.wm.TaskFragment tf) {
            android.window.TaskFragmentInfo info = tf.getTaskFragmentInfo();
            android.window.TaskFragmentInfo lastInfo = this.mLastSentTaskFragmentInfos.get(tf);
            if (info.equalsForTaskFragmentOrganizer(lastInfo) && com.android.server.wm.WindowOrganizerController.configurationsAreEqualForOrganizer(info.getConfiguration(), lastInfo.getConfiguration())) {
                return null;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(tf.getName());
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 3294593748816836746L, 0, null, protoLogParam0);
            }
            this.mLastSentTaskFragmentInfos.put(tf, info);
            return new android.window.TaskFragmentTransaction.Change(2).setTaskFragmentToken(tf.getFragmentToken()).setTaskFragmentInfo(info).setTaskId(tf.getTask().mTaskId);
        }

        android.window.TaskFragmentTransaction.Change prepareTaskFragmentParentInfoChanged(com.android.server.wm.Task task) {
            android.content.res.Configuration lastParentConfig;
            int taskId = task.mTaskId;
            android.window.TaskFragmentParentInfo parentInfo = task.getTaskFragmentParentInfo();
            android.window.TaskFragmentParentInfo lastParentInfo = this.mLastSentTaskFragmentParentInfos.get(taskId);
            if (lastParentInfo == null) {
                lastParentConfig = null;
            } else {
                lastParentConfig = lastParentInfo.getConfiguration();
            }
            if (parentInfo.equalsForTaskFragmentOrganizer(lastParentInfo) && com.android.server.wm.WindowOrganizerController.configurationsAreEqualForOrganizer(parentInfo.getConfiguration(), lastParentConfig)) {
                return null;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(task.getName());
                long protoLogParam1 = taskId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 5007230330523630579L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
            }
            this.mLastSentTaskFragmentParentInfos.put(taskId, new android.window.TaskFragmentParentInfo(parentInfo));
            return new android.window.TaskFragmentTransaction.Change(4).setTaskId(taskId).setTaskFragmentParentInfo(parentInfo);
        }

        android.window.TaskFragmentTransaction.Change prepareTaskFragmentError(android.os.IBinder errorCallbackToken, com.android.server.wm.TaskFragment taskFragment, int opType, java.lang.Throwable exception) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(exception.toString());
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 6475066005515810081L, 0, null, protoLogParam0);
            }
            android.window.TaskFragmentInfo info = taskFragment != null ? taskFragment.getTaskFragmentInfo() : null;
            android.os.Bundle errorBundle = android.window.TaskFragmentOrganizer.putErrorInfoInBundle(exception, info, opType);
            return new android.window.TaskFragmentTransaction.Change(5).setErrorCallbackToken(errorCallbackToken).setErrorBundle(errorBundle);
        }

        android.window.TaskFragmentTransaction.Change prepareActivityReparentedToTask(com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord nextFillTaskActivity, android.os.IBinder lastParentTfToken) {
            final android.os.IBinder activityToken;
            if (activity.finishing) {
                android.util.Slog.d(com.android.server.wm.TaskFragmentOrganizerController.TAG, "Reparent activity=" + activity.token + " is finishing");
                return null;
            }
            com.android.server.wm.Task task = activity.getTask();
            if (task == null || task.effectiveUid != this.mOrganizerUid) {
                android.util.Slog.d(com.android.server.wm.TaskFragmentOrganizerController.TAG, "Reparent activity=" + activity.token + " is not in a task belong to the organizer app.");
                return null;
            }
            if (task.isAllowedToEmbedActivity(activity, this.mOrganizerUid) != 0) {
                android.util.Slog.d(com.android.server.wm.TaskFragmentOrganizerController.TAG, "Reparent activity=" + activity.token + " is not allowed to be embedded.");
                return null;
            }
            if (!task.isAllowedToEmbedActivityInTrustedMode(activity, this.mOrganizerUid) && !activity.isUntrustedEmbeddingStateSharingAllowed()) {
                android.util.Slog.d(com.android.server.wm.TaskFragmentOrganizerController.TAG, "Reparent activity=" + activity.token + " is not allowed to be shared with untrusted host.");
                return null;
            }
            if (activity.getPid() == this.mOrganizerPid && activity.getUid() == this.mOrganizerUid) {
                activityToken = activity.token;
            } else {
                activityToken = new android.os.Binder("TemporaryActivityToken");
                this.mTemporaryActivityTokens.put(activityToken, activity);
                java.lang.Runnable timeout = new java.lang.Runnable() { // from class: com.android.server.wm.TaskFragmentOrganizerController$TaskFragmentOrganizerState$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$prepareActivityReparentedToTask$1(activityToken);
                    }
                };
                com.android.server.wm.TaskFragmentOrganizerController.this.mAtmService.mWindowManager.mH.postDelayed(timeout, com.android.server.wm.TaskFragmentOrganizerController.TEMPORARY_ACTIVITY_TOKEN_TIMEOUT_MS);
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(activity.token);
                long protoLogParam1 = task.mTaskId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -7893265697482064583L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
            }
            android.window.TaskFragmentTransaction.Change change = new android.window.TaskFragmentTransaction.Change(6).setTaskId(task.mTaskId).setActivityIntent(com.android.server.wm.TaskFragmentOrganizerController.trimIntent(activity.intent)).setActivityToken(activityToken);
            if (lastParentTfToken != null) {
                change.setTaskFragmentToken(lastParentTfToken);
            }
            if (com.android.window.flags.Flags.fixPipRestoreToOverlay() && nextFillTaskActivity != null && nextFillTaskActivity.getPid() == this.mOrganizerPid && nextFillTaskActivity.getUid() == this.mOrganizerUid) {
                change.setOtherActivityToken(nextFillTaskActivity.token);
            }
            return change;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$prepareActivityReparentedToTask$1(android.os.IBinder activityToken) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.TaskFragmentOrganizerController.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mTemporaryActivityTokens.remove(activityToken);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        void dispatchTransaction(android.window.TaskFragmentTransaction transaction) {
            if (transaction.isEmpty()) {
                return;
            }
            try {
                if (com.android.window.flags.Flags.bundleClientTransactionFlag()) {
                    this.mAppThread.scheduleTaskFragmentTransaction(this.mOrganizer, transaction);
                } else {
                    this.mOrganizer.onTransactionReady(transaction);
                }
                if (!com.android.server.wm.TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().isCollecting()) {
                    return;
                }
                int transitionId = com.android.server.wm.TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().getCollectingTransitionId();
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                    long protoLogParam0 = transitionId;
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(transaction.getTransactionToken());
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 7048981249808281819L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
                }
                this.mDeferredTransitions.put(transaction.getTransactionToken(), java.lang.Integer.valueOf(transitionId));
                com.android.server.wm.Transition collect = com.android.server.wm.TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().getCollectingTransition();
                boolean readyInAdvance = collect != null && collect.getWrapper().getExtImpl().readyInAdvance(collect, com.android.server.wm.TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController());
                if (!readyInAdvance) {
                    com.android.server.wm.TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().deferTransitionReady();
                }
                com.android.server.wm.Transition.ReadyCondition transactionApplied = new com.android.server.wm.Transition.ReadyCondition("task-fragment transaction", transaction);
                com.android.server.wm.TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().waitFor(transactionApplied);
                this.mInFlightTransactions.put(transaction.getTransactionToken(), transactionApplied);
            } catch (android.os.RemoteException e) {
                android.util.Slog.d(com.android.server.wm.TaskFragmentOrganizerController.TAG, "Exception sending TaskFragmentTransaction", e);
            }
        }

        void onTransactionFinished(android.os.IBinder transactionToken) {
            if (!this.mDeferredTransitions.containsKey(transactionToken)) {
                return;
            }
            int transitionId = this.mDeferredTransitions.remove(transactionToken).intValue();
            if (!com.android.server.wm.TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().isCollecting() || com.android.server.wm.TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().getCollectingTransitionId() != transitionId) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[3]) {
                    long protoLogParam0 = transitionId;
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(transactionToken);
                    com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1315509853595025526L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
                    return;
                }
                return;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                long protoLogParam02 = transitionId;
                java.lang.String protoLogParam12 = java.lang.String.valueOf(transactionToken);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 7421521217481553621L, 1, null, java.lang.Long.valueOf(protoLogParam02), protoLogParam12);
            }
            com.android.server.wm.TaskFragmentOrganizerController.this.mWindowOrganizerController.getTransitionController().continueTransitionReady();
        }
    }

    com.android.server.wm.ActivityRecord getReparentActivityFromTemporaryToken(android.window.ITaskFragmentOrganizer organizer, android.os.IBinder activityToken) {
        com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state;
        if (organizer == null || activityToken == null || (state = this.mTaskFragmentOrganizerState.get(organizer.asBinder())) == null) {
            return null;
        }
        return (com.android.server.wm.ActivityRecord) state.mTemporaryActivityTokens.remove(activityToken);
    }

    public void registerOrganizer(android.window.ITaskFragmentOrganizer organizer, boolean isSystemOrganizer) {
        registerOrganizerInternal(organizer, com.android.window.flags.Flags.taskFragmentSystemOrganizerFlag() && isSystemOrganizer);
    }

    private void registerOrganizerInternal(android.window.ITaskFragmentOrganizer organizer, boolean isSystemOrganizer) {
        if (isSystemOrganizer) {
            com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("registerSystemOrganizer()");
        }
        int pid = android.os.Binder.getCallingPid();
        int uid = android.os.Binder.getCallingUid();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(organizer.asBinder());
                    long protoLogParam1 = uid;
                    long protoLogParam2 = pid;
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 3509684748201636981L, 20, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2));
                }
                if (isOrganizerRegistered(organizer)) {
                    throw new java.lang.IllegalStateException("Replacing existing organizer currently unsupported");
                }
                if (pid <= 0) {
                    throw new java.lang.IllegalStateException("Cannot register from invalid pid: " + pid);
                }
                this.mTaskFragmentOrganizerState.put(organizer.asBinder(), new com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState(organizer, pid, uid, isSystemOrganizer));
                this.mPendingTaskFragmentEvents.put(organizer.asBinder(), new java.util.ArrayList());
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void unregisterOrganizer(android.window.ITaskFragmentOrganizer organizer) throws java.lang.Throwable {
        int pid = android.os.Binder.getCallingPid();
        long uid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            try {
                try {
                    synchronized (windowManagerGlobalLock) {
                        try {
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                                java.lang.String protoLogParam0 = java.lang.String.valueOf(organizer.asBinder());
                                long protoLogParam2 = pid;
                                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -6777461169027010201L, 20, null, protoLogParam0, java.lang.Long.valueOf(uid), java.lang.Long.valueOf(protoLogParam2));
                            }
                            removeOrganizer(organizer, "unregistered");
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

    public void registerRemoteAnimations(android.window.ITaskFragmentOrganizer organizer, android.view.RemoteAnimationDefinition definition) {
        int pid = android.os.Binder.getCallingPid();
        int uid = android.os.Binder.getCallingUid();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(organizer.asBinder());
                    long protoLogParam1 = uid;
                    long protoLogParam2 = pid;
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 1327792561585467865L, 20, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2));
                }
                com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState organizerState = this.mTaskFragmentOrganizerState.get(organizer.asBinder());
                if (organizerState == null) {
                    throw new java.lang.IllegalStateException("The organizer hasn't been registered.");
                }
                if (organizerState.mRemoteAnimationDefinition != null) {
                    throw new java.lang.IllegalStateException("The organizer has already registered remote animations=" + organizerState.mRemoteAnimationDefinition);
                }
                definition.setCallingPidUid(pid, uid);
                organizerState.mRemoteAnimationDefinition = definition;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void unregisterRemoteAnimations(android.window.ITaskFragmentOrganizer organizer) {
        int pid = android.os.Binder.getCallingPid();
        long uid = android.os.Binder.getCallingUid();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(organizer.asBinder());
                    long protoLogParam2 = pid;
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -2524361347368208519L, 20, null, protoLogParam0, java.lang.Long.valueOf(uid), java.lang.Long.valueOf(protoLogParam2));
                }
                com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState organizerState = this.mTaskFragmentOrganizerState.get(organizer.asBinder());
                if (organizerState == null) {
                    android.util.Slog.e(TAG, "The organizer hasn't been registered.");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } else {
                    organizerState.mRemoteAnimationDefinition = null;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void onTransactionHandled(android.os.IBinder transactionToken, android.window.WindowContainerTransaction wct, int transitionType, boolean shouldApplyIndependently) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (isValidTransaction(wct)) {
                    applyTransaction(wct, transitionType, shouldApplyIndependently, null);
                }
                android.window.ITaskFragmentOrganizer organizer = wct.getTaskFragmentOrganizer();
                com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state = organizer != null ? this.mTaskFragmentOrganizerState.get(organizer.asBinder()) : null;
                if (state != null) {
                    state.onTransactionFinished(transactionToken);
                    com.android.server.wm.Transition.ReadyCondition condition = (com.android.server.wm.Transition.ReadyCondition) state.mInFlightTransactions.remove(transactionToken);
                    if (condition != null) {
                        condition.meet();
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void applyTransaction(android.window.WindowContainerTransaction wct, int transitionType, boolean shouldApplyIndependently, android.window.RemoteTransition remoteTransition) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!isValidTransaction(wct)) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } else {
                    this.mWindowOrganizerController.applyTaskFragmentTransactionLocked(wct, transitionType, shouldApplyIndependently, remoteTransition);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public android.view.RemoteAnimationDefinition getRemoteAnimationDefinition(android.window.ITaskFragmentOrganizer organizer) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState organizerState = this.mTaskFragmentOrganizerState.get(organizer.asBinder());
                if (organizerState == null) {
                    android.util.Slog.e(TAG, "TaskFragmentOrganizer has been unregistered or died when trying to play animation on its organized windows.");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                android.view.RemoteAnimationDefinition remoteAnimationDefinition = organizerState.mRemoteAnimationDefinition;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return remoteAnimationDefinition;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    int getTaskFragmentOrganizerUid(android.window.ITaskFragmentOrganizer organizer) {
        com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state = validateAndGetState(organizer);
        return state.mOrganizerUid;
    }

    void onTaskFragmentAppeared(android.window.ITaskFragmentOrganizer organizer, com.android.server.wm.TaskFragment taskFragment) {
        if (taskFragment.mTaskFragmentVanishedSent) {
            return;
        }
        if (taskFragment.getTask() == null) {
            android.util.Slog.w(TAG, "onTaskFragmentAppeared failed because it is not attached tf=" + taskFragment);
            return;
        }
        com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state = validateAndGetState(organizer);
        if (!state.addTaskFragment(taskFragment)) {
            return;
        }
        com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent pendingEvent = getPendingTaskFragmentEvent(taskFragment, 0);
        if (pendingEvent == null) {
            addPendingEvent(new com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder(0, organizer).setTaskFragment(taskFragment).build());
        }
    }

    void onTaskFragmentInfoChanged(android.window.ITaskFragmentOrganizer organizer, com.android.server.wm.TaskFragment taskFragment) {
        if (taskFragment.mTaskFragmentVanishedSent) {
            return;
        }
        validateAndGetState(organizer);
        if (!taskFragment.mTaskFragmentAppearedSent) {
            return;
        }
        com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent pendingEvent = getLastPendingLifecycleEvent(taskFragment);
        if (pendingEvent == null) {
            pendingEvent = new com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder(2, organizer).setTaskFragment(taskFragment).build();
        } else {
            removePendingEvent(pendingEvent);
            pendingEvent.mDeferTime = 0L;
        }
        addPendingEvent(pendingEvent);
    }

    void onTaskFragmentVanished(android.window.ITaskFragmentOrganizer organizer, com.android.server.wm.TaskFragment taskFragment) {
        if (taskFragment.mTaskFragmentVanishedSent) {
            return;
        }
        taskFragment.mTaskFragmentVanishedSent = true;
        com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state = validateAndGetState(organizer);
        java.util.List<com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent> pendingEvents = this.mPendingTaskFragmentEvents.get(organizer.asBinder());
        for (int i = pendingEvents.size() - 1; i >= 0; i--) {
            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent event = pendingEvents.get(i);
            if (taskFragment == event.mTaskFragment) {
                pendingEvents.remove(i);
            }
        }
        addPendingEvent(new com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder(1, organizer).setTaskFragment(taskFragment).build());
        state.removeTaskFragment(taskFragment);
        this.mAtmService.mWindowManager.mWindowPlacerLocked.requestTraversal();
    }

    void onTaskFragmentError(android.window.ITaskFragmentOrganizer organizer, android.os.IBinder errorCallbackToken, com.android.server.wm.TaskFragment taskFragment, int opType, java.lang.Throwable exception) {
        if (taskFragment != null && taskFragment.mTaskFragmentVanishedSent) {
            return;
        }
        validateAndGetState(organizer);
        android.util.Slog.w(TAG, "onTaskFragmentError ", exception);
        addPendingEvent(new com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder(4, organizer).setErrorCallbackToken(errorCallbackToken).setTaskFragment(taskFragment).setException(exception).setOpType(opType).build());
        this.mAtmService.mWindowManager.mWindowPlacerLocked.requestTraversal();
    }

    void onActivityReparentedToTask(final com.android.server.wm.ActivityRecord activity) {
        android.window.ITaskFragmentOrganizer organizer;
        com.android.server.wm.Task task = activity.getTask();
        if (activity.mLastTaskFragmentOrganizerBeforePip != null) {
            organizer = activity.mLastTaskFragmentOrganizerBeforePip;
        } else {
            final com.android.server.wm.TaskFragment[] organizedTf = new com.android.server.wm.TaskFragment[1];
            task.forAllLeafTaskFragments(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragmentOrganizerController$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.TaskFragmentOrganizerController.lambda$onActivityReparentedToTask$0(organizedTf, (com.android.server.wm.TaskFragment) obj);
                }
            });
            if (organizedTf[0] == null) {
                return;
            } else {
                organizer = organizedTf[0].getTaskFragmentOrganizer();
            }
        }
        if (!isOrganizerRegistered(organizer)) {
            android.util.Slog.w(TAG, "The last TaskFragmentOrganizer no longer exists");
            return;
        }
        final android.os.IBinder parentTfTokenBeforePip = activity.getLastEmbeddedParentTfTokenBeforePip();
        com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder builder = new com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder(5, organizer).setActivity(activity).setTaskFragmentToken(activity.getLastEmbeddedParentTfTokenBeforePip());
        com.android.server.wm.ActivityRecord candidateAssociatedActivity = task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskFragmentOrganizerController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.TaskFragmentOrganizerController.lambda$onActivityReparentedToTask$1(activity, parentTfTokenBeforePip, (com.android.server.wm.ActivityRecord) obj);
            }
        });
        if (candidateAssociatedActivity != null && (!candidateAssociatedActivity.isEmbedded() || candidateAssociatedActivity.getTaskFragment().fillsParent())) {
            builder.setOtherActivity(candidateAssociatedActivity);
        }
        addPendingEvent(builder.build());
    }

    static /* synthetic */ boolean lambda$onActivityReparentedToTask$0(com.android.server.wm.TaskFragment[] organizedTf, com.android.server.wm.TaskFragment tf) {
        if (!tf.isOrganizedTaskFragment()) {
            return false;
        }
        organizedTf[0] = tf;
        return true;
    }

    static /* synthetic */ boolean lambda$onActivityReparentedToTask$1(com.android.server.wm.ActivityRecord activity, android.os.IBinder parentTfTokenBeforePip, com.android.server.wm.ActivityRecord ar) {
        return (ar == activity || ar.finishing || ar.getTaskFragment().getFragmentToken() == parentTfTokenBeforePip) ? false : true;
    }

    void onTaskFragmentParentInfoChanged(android.window.ITaskFragmentOrganizer organizer, com.android.server.wm.Task task) {
        validateAndGetState(organizer);
        com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent pendingEvent = getLastPendingParentInfoChangedEvent(organizer, task);
        if (pendingEvent == null) {
            addPendingEvent(new com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder(3, organizer).setTask(task).build());
        }
        this.mAtmService.mWindowManager.mWindowPlacerLocked.requestTraversal();
    }

    boolean isSystemOrganizer(android.os.IBinder organizerToken) {
        com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state = this.mTaskFragmentOrganizerState.get(organizerToken);
        return state != null && state.mIsSystemOrganizer;
    }

    private com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent getLastPendingParentInfoChangedEvent(android.window.ITaskFragmentOrganizer organizer, com.android.server.wm.Task task) {
        java.util.List<com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent> events = this.mPendingTaskFragmentEvents.get(organizer.asBinder());
        for (int i = events.size() - 1; i >= 0; i--) {
            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent event = events.get(i);
            if (task == event.mTask && event.mEventType == 3) {
                return event;
            }
        }
        return null;
    }

    private void addPendingEvent(com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent event) {
        this.mPendingTaskFragmentEvents.get(event.mTaskFragmentOrg.asBinder()).add(event);
    }

    private void removePendingEvent(com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent event) {
        this.mPendingTaskFragmentEvents.get(event.mTaskFragmentOrg.asBinder()).remove(event);
    }

    private boolean isOrganizerRegistered(android.window.ITaskFragmentOrganizer organizer) {
        return this.mTaskFragmentOrganizerState.containsKey(organizer.asBinder());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeOrganizer(android.window.ITaskFragmentOrganizer organizer, java.lang.String reason) {
        com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state = this.mTaskFragmentOrganizerState.get(organizer.asBinder());
        if (state == null) {
            android.util.Slog.w(TAG, "The organizer has already been removed.");
            return;
        }
        this.mPendingTaskFragmentEvents.remove(organizer.asBinder());
        state.dispose(reason);
        this.mTaskFragmentOrganizerState.remove(organizer.asBinder());
    }

    private com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState validateAndGetState(android.window.ITaskFragmentOrganizer organizer) {
        com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state = this.mTaskFragmentOrganizerState.get(organizer.asBinder());
        if (state == null) {
            throw new java.lang.IllegalArgumentException("TaskFragmentOrganizer has not been registered. Organizer=" + organizer);
        }
        return state;
    }

    boolean isValidTransaction(android.window.WindowContainerTransaction t) {
        if (t.isEmpty()) {
            return false;
        }
        android.window.ITaskFragmentOrganizer organizer = t.getTaskFragmentOrganizer();
        if (t.getTaskFragmentOrganizer() == null || !isOrganizerRegistered(organizer)) {
            android.util.Slog.e(TAG, "Caller organizer=" + organizer + " is no longer registered");
            return false;
        }
        return true;
    }

    private static class PendingTaskFragmentEvent {
        static final int EVENT_ACTIVITY_REPARENTED_TO_TASK = 5;
        static final int EVENT_APPEARED = 0;
        static final int EVENT_ERROR = 4;
        static final int EVENT_INFO_CHANGED = 2;
        static final int EVENT_PARENT_INFO_CHANGED = 3;
        static final int EVENT_VANISHED = 1;
        private final com.android.server.wm.ActivityRecord mActivity;
        private long mDeferTime;
        private final android.os.IBinder mErrorCallbackToken;
        private final int mEventType;
        private final java.lang.Throwable mException;
        private int mOpType;
        private final com.android.server.wm.ActivityRecord mOtherActivity;
        private final com.android.server.wm.Task mTask;
        private final com.android.server.wm.TaskFragment mTaskFragment;
        private final android.window.ITaskFragmentOrganizer mTaskFragmentOrg;
        private final android.os.IBinder mTaskFragmentToken;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface EventType {
        }

        private PendingTaskFragmentEvent(int eventType, android.window.ITaskFragmentOrganizer taskFragmentOrg, com.android.server.wm.TaskFragment taskFragment, android.os.IBinder taskFragmentToken, android.os.IBinder errorCallbackToken, java.lang.Throwable exception, com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord otherActivity, com.android.server.wm.Task task, int opType) {
            this.mEventType = eventType;
            this.mTaskFragmentOrg = taskFragmentOrg;
            this.mTaskFragment = taskFragment;
            this.mTaskFragmentToken = taskFragmentToken;
            this.mErrorCallbackToken = errorCallbackToken;
            this.mException = exception;
            this.mActivity = activity;
            this.mOtherActivity = otherActivity;
            this.mTask = task;
            this.mOpType = opType;
        }

        boolean isLifecycleEvent() {
            switch (this.mEventType) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return true;
                default:
                    return false;
            }
        }

        private static class Builder {
            private com.android.server.wm.ActivityRecord mActivity;
            private android.os.IBinder mErrorCallbackToken;
            private final int mEventType;
            private java.lang.Throwable mException;
            private int mOpType;
            private com.android.server.wm.ActivityRecord mOtherActivity;
            private com.android.server.wm.Task mTask;
            private com.android.server.wm.TaskFragment mTaskFragment;
            private final android.window.ITaskFragmentOrganizer mTaskFragmentOrg;
            private android.os.IBinder mTaskFragmentToken;

            Builder(int eventType, android.window.ITaskFragmentOrganizer taskFragmentOrg) {
                this.mEventType = eventType;
                this.mTaskFragmentOrg = (android.window.ITaskFragmentOrganizer) java.util.Objects.requireNonNull(taskFragmentOrg);
            }

            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder setTaskFragment(com.android.server.wm.TaskFragment taskFragment) {
                this.mTaskFragment = taskFragment;
                return this;
            }

            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder setTaskFragmentToken(android.os.IBinder fragmentToken) {
                this.mTaskFragmentToken = fragmentToken;
                return this;
            }

            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder setErrorCallbackToken(android.os.IBinder errorCallbackToken) {
                this.mErrorCallbackToken = errorCallbackToken;
                return this;
            }

            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder setException(java.lang.Throwable exception) {
                this.mException = (java.lang.Throwable) java.util.Objects.requireNonNull(exception);
                return this;
            }

            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder setActivity(com.android.server.wm.ActivityRecord activity) {
                this.mActivity = (com.android.server.wm.ActivityRecord) java.util.Objects.requireNonNull(activity);
                return this;
            }

            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder setOtherActivity(com.android.server.wm.ActivityRecord otherActivity) {
                this.mOtherActivity = otherActivity;
                return this;
            }

            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder setTask(com.android.server.wm.Task task) {
                this.mTask = (com.android.server.wm.Task) java.util.Objects.requireNonNull(task);
                return this;
            }

            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder setOpType(int opType) {
                this.mOpType = opType;
                return this;
            }

            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent build() {
                return new com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent(this.mEventType, this.mTaskFragmentOrg, this.mTaskFragment, this.mTaskFragmentToken, this.mErrorCallbackToken, this.mException, this.mActivity, this.mOtherActivity, this.mTask, this.mOpType);
            }
        }
    }

    private com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent getLastPendingLifecycleEvent(com.android.server.wm.TaskFragment tf) {
        android.window.ITaskFragmentOrganizer organizer = tf.getTaskFragmentOrganizer();
        java.util.List<com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent> events = this.mPendingTaskFragmentEvents.get(organizer.asBinder());
        for (int i = events.size() - 1; i >= 0; i--) {
            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent event = events.get(i);
            if (tf == event.mTaskFragment && event.isLifecycleEvent()) {
                return event;
            }
        }
        return null;
    }

    private com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent getPendingTaskFragmentEvent(com.android.server.wm.TaskFragment taskFragment, int type) {
        android.window.ITaskFragmentOrganizer organizer = taskFragment.getTaskFragmentOrganizer();
        java.util.List<com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent> events = this.mPendingTaskFragmentEvents.get(organizer.asBinder());
        for (int i = events.size() - 1; i >= 0; i--) {
            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent event = events.get(i);
            if (taskFragment == event.mTaskFragment && type == event.mEventType) {
                return event;
            }
        }
        return null;
    }

    void dispatchPendingEvents() {
        if (this.mAtmService.mWindowManager.mWindowPlacerLocked.isLayoutDeferred() || this.mPendingTaskFragmentEvents.isEmpty()) {
            return;
        }
        int organizerNum = this.mPendingTaskFragmentEvents.size();
        for (int i = 0; i < organizerNum; i++) {
            com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state = this.mTaskFragmentOrganizerState.get(this.mPendingTaskFragmentEvents.keyAt(i));
            dispatchPendingEvents(state, this.mPendingTaskFragmentEvents.valueAt(i));
        }
    }

    private void dispatchPendingEvents(com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state, java.util.List<com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent> pendingEvents) {
        if (pendingEvents.isEmpty() || shouldDeferPendingEvents(state, pendingEvents)) {
            return;
        }
        this.mTmpTaskSet.clear();
        int numEvents = pendingEvents.size();
        android.window.TaskFragmentTransaction transaction = new android.window.TaskFragmentTransaction();
        for (int i = 0; i < numEvents; i++) {
            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent event = pendingEvents.get(i);
            if (event.mEventType == 0 || event.mEventType == 2) {
                com.android.server.wm.Task task = event.mTaskFragment.getTask();
                if (this.mTmpTaskSet.add(task)) {
                    transaction.addChange(prepareChange(new com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder(3, state.mOrganizer).setTask(task).build()));
                }
            }
            transaction.addChange(prepareChange(event));
        }
        this.mTmpTaskSet.clear();
        state.dispatchTransaction(transaction);
        pendingEvents.clear();
    }

    private boolean shouldDeferPendingEvents(com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state, java.util.List<com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent> pendingEvents) {
        com.android.server.wm.Task task;
        java.util.ArrayList<com.android.server.wm.Task> visibleTasks = new java.util.ArrayList<>();
        java.util.ArrayList<com.android.server.wm.Task> invisibleTasks = new java.util.ArrayList<>();
        int n = pendingEvents.size();
        for (int i = 0; i < n; i++) {
            com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent event = pendingEvents.get(i);
            if (event.mEventType != 3 && event.mEventType != 2 && event.mEventType != 0) {
                return false;
            }
            if (event.mEventType == 3) {
                task = event.mTask;
            } else {
                task = event.mTaskFragment.getTask();
            }
            if ((task.lastActiveTime > event.mDeferTime && isTaskVisible(task, visibleTasks, invisibleTasks)) || shouldSendEventWhenTaskInvisible(task, state, event)) {
                return false;
            }
            event.mDeferTime = task.lastActiveTime;
        }
        return true;
    }

    private static boolean isTaskVisible(com.android.server.wm.Task task, java.util.ArrayList<com.android.server.wm.Task> knownVisibleTasks, java.util.ArrayList<com.android.server.wm.Task> knownInvisibleTasks) {
        if (knownVisibleTasks.contains(task)) {
            return true;
        }
        if (knownInvisibleTasks.contains(task)) {
            return false;
        }
        if (task.shouldBeVisible(null)) {
            knownVisibleTasks.add(task);
            return true;
        }
        knownInvisibleTasks.add(task);
        return false;
    }

    private boolean shouldSendEventWhenTaskInvisible(com.android.server.wm.Task task, com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state, com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent event) {
        android.window.TaskFragmentParentInfo lastParentInfo = (android.window.TaskFragmentParentInfo) state.mLastSentTaskFragmentParentInfos.get(task.mTaskId);
        if (lastParentInfo == null || lastParentInfo.isVisible()) {
            return true;
        }
        if (event.mEventType != 2) {
            return false;
        }
        android.window.TaskFragmentInfo lastInfo = (android.window.TaskFragmentInfo) state.mLastSentTaskFragmentInfos.get(event.mTaskFragment);
        boolean isEmpty = event.mTaskFragment.getNonFinishingActivityCount() == 0;
        return lastInfo == null || lastInfo.isEmpty() != isEmpty;
    }

    void dispatchPendingInfoChangedEvent(com.android.server.wm.TaskFragment taskFragment) {
        com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent event = getPendingTaskFragmentEvent(taskFragment, 2);
        if (event == null) {
            return;
        }
        android.window.ITaskFragmentOrganizer organizer = taskFragment.getTaskFragmentOrganizer();
        com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state = validateAndGetState(organizer);
        android.window.TaskFragmentTransaction transaction = new android.window.TaskFragmentTransaction();
        transaction.addChange(prepareChange(new com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent.Builder(3, organizer).setTask(taskFragment.getTask()).build()));
        transaction.addChange(prepareChange(event));
        state.dispatchTransaction(transaction);
        this.mPendingTaskFragmentEvents.get(organizer.asBinder()).remove(event);
    }

    private android.window.TaskFragmentTransaction.Change prepareChange(com.android.server.wm.TaskFragmentOrganizerController.PendingTaskFragmentEvent event) {
        android.window.ITaskFragmentOrganizer taskFragmentOrg = event.mTaskFragmentOrg;
        com.android.server.wm.TaskFragment taskFragment = event.mTaskFragment;
        com.android.server.wm.TaskFragmentOrganizerController.TaskFragmentOrganizerState state = this.mTaskFragmentOrganizerState.get(taskFragmentOrg.asBinder());
        if (state == null) {
            return null;
        }
        switch (event.mEventType) {
            case 0:
                return state.prepareTaskFragmentAppeared(taskFragment);
            case 1:
                return state.prepareTaskFragmentVanished(taskFragment);
            case 2:
                return state.prepareTaskFragmentInfoChanged(taskFragment);
            case 3:
                return state.prepareTaskFragmentParentInfoChanged(event.mTask);
            case 4:
                return state.prepareTaskFragmentError(event.mErrorCallbackToken, taskFragment, event.mOpType, event.mException);
            case 5:
                return state.prepareActivityReparentedToTask(event.mActivity, event.mOtherActivity, event.mTaskFragmentToken);
            default:
                throw new java.lang.IllegalArgumentException("Unknown TaskFragmentEvent=" + event.mEventType);
        }
    }

    public boolean isActivityEmbedded(android.os.IBinder activityToken) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ActivityRecord activity = com.android.server.wm.ActivityRecord.forTokenLocked(activityToken);
                if (activity == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                com.android.server.wm.TaskFragment taskFragment = activity.getOrganizedTaskFragment();
                if (taskFragment == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                boolean isEmbed = activity.isEmbeddedInHostContainer();
                boolean zHookIsActivityEmbedded = taskFragment.mTaskFragmentExt.hookIsActivityEmbedded(isEmbed, taskFragment, activity);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return zHookIsActivityEmbedded;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    android.app.IApplicationThread getAppThread(int pid, int uid) {
        android.app.IApplicationThread appThread;
        com.android.server.wm.WindowProcessController wpc = this.mAtmService.mProcessMap.getProcess(pid);
        if (wpc != null && wpc.mUid == uid) {
            appThread = wpc.getThread();
        } else {
            appThread = null;
        }
        if (appThread == null) {
            throw new java.lang.IllegalArgumentException("Cannot find process for pid=" + pid + " uid=" + uid);
        }
        return appThread;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.content.Intent trimIntent(android.content.Intent intent) {
        return new android.content.Intent().setComponent(intent.getComponent()).setPackage(intent.getPackage()).setAction(intent.getAction());
    }
}
