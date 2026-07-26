package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ResetTargetTaskHelper implements java.util.function.Consumer<com.android.server.wm.Task>, java.util.function.Predicate<com.android.server.wm.ActivityRecord> {
    private int mActivityReparentPosition;
    private boolean mCanMoveOptions;
    private boolean mForceReset;
    private boolean mIsTargetTask;
    private com.android.server.wm.ActivityRecord mRoot;
    private com.android.server.wm.Task mTargetRootTask;
    private com.android.server.wm.Task mTargetTask;
    private boolean mTargetTaskFound;
    private com.android.server.wm.Task mTask;
    private android.app.ActivityOptions mTopOptions;
    private java.util.ArrayList<com.android.server.wm.ActivityRecord> mResultActivities = new java.util.ArrayList<>();
    private java.util.ArrayList<com.android.server.wm.ActivityRecord> mAllActivities = new java.util.ArrayList<>();
    private java.util.ArrayList<com.android.server.wm.ActivityRecord> mPendingReparentActivities = new java.util.ArrayList<>();

    ResetTargetTaskHelper() {
    }

    private void reset(com.android.server.wm.Task task) {
        this.mTask = task;
        this.mRoot = null;
        this.mCanMoveOptions = true;
        this.mResultActivities.clear();
        this.mAllActivities.clear();
    }

    android.app.ActivityOptions process(com.android.server.wm.Task targetTask, boolean forceReset) {
        this.mTopOptions = null;
        this.mForceReset = forceReset;
        this.mTargetTask = targetTask;
        this.mTargetTaskFound = false;
        this.mTargetRootTask = targetTask.getRootTask();
        this.mActivityReparentPosition = -1;
        targetTask.mWmService.mRoot.forAllLeafTasks(this, true);
        processPendingReparentActivities();
        reset(null);
        return this.mTopOptions;
    }

    @Override // java.util.function.Consumer
    public void accept(com.android.server.wm.Task task) {
        reset(task);
        this.mRoot = task.getRootActivity(true);
        if (this.mRoot == null) {
            return;
        }
        this.mIsTargetTask = task == this.mTargetTask;
        if (this.mIsTargetTask) {
            this.mTargetTaskFound = true;
        }
        task.forAllActivities((java.util.function.Predicate<com.android.server.wm.ActivityRecord>) this);
    }

    @Override // java.util.function.Predicate
    public boolean test(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.ActivityRecord p;
        if (r == this.mRoot) {
            return true;
        }
        this.mAllActivities.add(r);
        int flags = r.info.flags;
        boolean finishOnTaskLaunch = (flags & 2) != 0;
        boolean allowTaskReparenting = (flags & 64) != 0;
        boolean clearWhenTaskReset = (r.intent.getFlags() & 524288) != 0;
        if (this.mIsTargetTask) {
            if (!finishOnTaskLaunch && !clearWhenTaskReset) {
                if (r.resultTo != null) {
                    this.mResultActivities.add(r);
                    return false;
                }
                if (allowTaskReparenting && r.taskAffinity != null && !r.taskAffinity.equals(this.mTask.affinity)) {
                    this.mPendingReparentActivities.add(r);
                    return false;
                }
            }
            if (this.mForceReset || finishOnTaskLaunch || clearWhenTaskReset) {
                if (clearWhenTaskReset) {
                    finishActivities(this.mAllActivities, "clearWhenTaskReset");
                } else {
                    this.mResultActivities.add(r);
                    finishActivities(this.mResultActivities, "reset-task");
                }
                this.mResultActivities.clear();
                return false;
            }
            this.mResultActivities.clear();
            return false;
        }
        if (r.resultTo != null) {
            this.mResultActivities.add(r);
            return false;
        }
        if (this.mTargetTaskFound && allowTaskReparenting && this.mTargetTask.affinity != null && this.mTargetTask.affinity.equals(r.taskAffinity)) {
            this.mResultActivities.add(r);
            if (this.mForceReset || finishOnTaskLaunch) {
                finishActivities(this.mResultActivities, "move-affinity");
                return false;
            }
            if (this.mActivityReparentPosition == -1) {
                this.mActivityReparentPosition = this.mTargetTask.getChildCount();
            }
            processResultActivities(r, this.mTargetTask, this.mActivityReparentPosition, false, false);
            if (r.info.launchMode == 1 && (p = this.mTargetTask.getActivityBelow(r)) != null && p.intent.getComponent().equals(r.intent.getComponent())) {
                p.finishIfPossible("replace", false);
            }
        }
        return false;
    }

    private void finishActivities(java.util.ArrayList<com.android.server.wm.ActivityRecord> activities, java.lang.String reason) {
        boolean noOptions = this.mCanMoveOptions;
        while (!activities.isEmpty()) {
            com.android.server.wm.ActivityRecord p = activities.remove(0);
            if (!p.finishing) {
                noOptions = takeOption(p, noOptions);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[3]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(p);
                    com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -4617490621756721600L, 0, null, protoLogParam0);
                }
                p.finishIfPossible(reason, false);
            }
        }
    }

    private void processResultActivities(com.android.server.wm.ActivityRecord target, com.android.server.wm.Task targetTask, int position, boolean ignoreFinishing, boolean takeOptions) {
        boolean noOptions = this.mCanMoveOptions;
        while (!this.mResultActivities.isEmpty()) {
            com.android.server.wm.ActivityRecord p = this.mResultActivities.remove(0);
            if (!ignoreFinishing || !p.finishing) {
                if (takeOptions) {
                    noOptions = takeOption(p, noOptions);
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[2]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(p);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mTask);
                    java.lang.String protoLogParam2 = java.lang.String.valueOf(targetTask);
                    java.lang.String protoLogParam3 = java.lang.String.valueOf(android.os.Debug.getCallers(4));
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 3361857745281957526L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2, protoLogParam3);
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(p);
                    java.lang.String protoLogParam12 = java.lang.String.valueOf(target);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 3958829063955690349L, 0, null, protoLogParam02, protoLogParam12);
                }
                p.reparent(targetTask, position, "resetTargetTaskIfNeeded");
            }
        }
    }

    private void processPendingReparentActivities() {
        com.android.server.wm.Task task;
        if (this.mPendingReparentActivities.isEmpty()) {
            return;
        }
        com.android.server.wm.ActivityTaskManagerService atmService = this.mTargetRootTask.mAtmService;
        com.android.server.wm.TaskDisplayArea taskDisplayArea = this.mTargetRootTask.getDisplayArea();
        int windowingMode = this.mTargetRootTask.getWindowingMode();
        int activityType = this.mTargetRootTask.getActivityType();
        while (!this.mPendingReparentActivities.isEmpty()) {
            com.android.server.wm.ActivityRecord r = this.mPendingReparentActivities.remove(0);
            boolean alwaysCreateTask = com.android.server.wm.DisplayContent.alwaysCreateRootTask(windowingMode, activityType);
            if (!alwaysCreateTask) {
                task = this.mTargetRootTask.getBottomMostTask();
            } else {
                task = taskDisplayArea.getBottomMostTask();
            }
            com.android.server.wm.Task targetTask = null;
            if (task != null && r.taskAffinity.equals(task.affinity)) {
                targetTask = task;
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(r);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(targetTask);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 1730793580703791926L, 0, null, protoLogParam0, protoLogParam1);
                }
            }
            if (targetTask == null) {
                if (alwaysCreateTask) {
                    targetTask = taskDisplayArea.getOrCreateRootTask(windowingMode, activityType, false);
                } else {
                    targetTask = this.mTargetRootTask.reuseOrCreateTask(r.info, null, false);
                }
                targetTask.affinityIntent = r.intent;
            }
            r.reparent(targetTask, 0, "resetTargetTaskIfNeeded");
            atmService.mTaskSupervisor.mRecentTasks.add(targetTask);
        }
    }

    private boolean takeOption(com.android.server.wm.ActivityRecord p, boolean noOptions) {
        this.mCanMoveOptions = false;
        if (noOptions && this.mTopOptions == null) {
            this.mTopOptions = p.getOptions();
            if (this.mTopOptions != null) {
                p.clearOptionsAnimation();
                return false;
            }
            return noOptions;
        }
        return noOptions;
    }
}
