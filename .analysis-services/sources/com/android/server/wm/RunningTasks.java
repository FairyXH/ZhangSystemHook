package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class RunningTasks implements java.util.function.Consumer<com.android.server.wm.Task> {
    static final int FLAG_ALLOWED = 2;
    static final int FLAG_CROSS_USERS = 4;
    static final int FLAG_FILTER_ONLY_VISIBLE_RECENTS = 1;
    static final int FLAG_KEEP_INTENT_EXTRA = 8;
    private boolean mAllowed;
    private int mCallingUid;
    private boolean mCrossUser;
    private boolean mFilterOnlyVisibleRecents;
    private boolean mKeepIntentExtra;
    private android.util.ArraySet<java.lang.Integer> mProfileIds;
    private com.android.server.wm.RecentTasks mRecentTasks;
    private int mUserId;
    private final java.util.ArrayList<com.android.server.wm.Task> mTmpSortedTasks = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.Task> mTmpVisibleTasks = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.Task> mTmpInvisibleTasks = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.Task> mTmpFocusedTasks = new java.util.ArrayList<>();
    private com.android.server.wm.IRunningTasksExt mRunningTasksExt = (com.android.server.wm.IRunningTasksExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IRunningTasksExt.class).base(this).create();

    RunningTasks() {
    }

    void getTasks(int maxNum, java.util.List<android.app.ActivityManager.RunningTaskInfo> list, int flags, com.android.server.wm.RecentTasks recentTasks, com.android.server.wm.WindowContainer<?> root, int callingUid, android.util.ArraySet<java.lang.Integer> profileIds) {
        if (maxNum <= 0) {
            return;
        }
        this.mCallingUid = callingUid;
        this.mUserId = android.os.UserHandle.getUserId(callingUid);
        this.mCrossUser = (flags & 4) == 4;
        this.mProfileIds = profileIds;
        this.mAllowed = (flags & 2) == 2;
        this.mFilterOnlyVisibleRecents = (flags & 1) == 1;
        this.mRecentTasks = recentTasks;
        this.mKeepIntentExtra = (flags & 8) == 8;
        if (root instanceof com.android.server.wm.RootWindowContainer) {
            ((com.android.server.wm.RootWindowContainer) root).forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.RunningTasks$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$getTasks$0((com.android.server.wm.DisplayContent) obj);
                }
            });
        } else {
            com.android.server.wm.DisplayContent dc = root.getDisplayContent();
            com.android.server.wm.Task focusedTask = null;
            if (dc != null && dc.mFocusedApp != null) {
                focusedTask = dc.mFocusedApp.getTask();
            }
            boolean rootContainsFocusedTask = focusedTask != null && focusedTask.isDescendantOf(root);
            if (rootContainsFocusedTask) {
                this.mTmpFocusedTasks.add(focusedTask);
            }
            processTaskInWindowContainer(root);
        }
        int visibleTaskCount = this.mTmpVisibleTasks.size();
        for (int i = 0; i < this.mTmpFocusedTasks.size(); i++) {
            com.android.server.wm.Task focusedTask2 = this.mTmpFocusedTasks.get(i);
            boolean containsFocusedTask = this.mTmpVisibleTasks.remove(focusedTask2);
            if (containsFocusedTask) {
                this.mTmpSortedTasks.add(focusedTask2);
            }
        }
        if (!this.mTmpVisibleTasks.isEmpty()) {
            this.mTmpSortedTasks.addAll(this.mTmpVisibleTasks);
        }
        if (!this.mTmpInvisibleTasks.isEmpty()) {
            this.mTmpSortedTasks.addAll(this.mTmpInvisibleTasks);
        }
        boolean callerIsLauncher = false;
        try {
            callerIsLauncher = callingUid == root.mWmService.mContext.getPackageManager().getPackageUidAsUser("com.android.launcher", this.mUserId);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        int size = java.lang.Math.min(maxNum, this.mTmpSortedTasks.size());
        long now = android.os.SystemClock.elapsedRealtime();
        int i2 = 0;
        while (i2 < size) {
            com.android.server.wm.Task task = this.mTmpSortedTasks.get(i2);
            if (callerIsLauncher) {
                task = this.mRunningTasksExt.replaceByMultiSearchIfNeed(task, this.mTmpSortedTasks);
            }
            long visibleActiveTime = i2 < visibleTaskCount ? (((long) size) + now) - ((long) i2) : -1L;
            list.add(createRunningTaskInfo(task, visibleActiveTime));
            i2++;
        }
        this.mTmpFocusedTasks.clear();
        this.mTmpVisibleTasks.clear();
        this.mTmpInvisibleTasks.clear();
        this.mTmpSortedTasks.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTasks$0(com.android.server.wm.DisplayContent dc) {
        com.android.server.wm.Task focusedTask = dc.mFocusedApp != null ? dc.mFocusedApp.getTask() : null;
        if (focusedTask != null) {
            this.mTmpFocusedTasks.add(focusedTask);
        }
        processTaskInWindowContainer(dc);
    }

    private void processTaskInWindowContainer(com.android.server.wm.WindowContainer wc) {
        wc.forAllLeafTasks(this, true);
    }

    @Override // java.util.function.Consumer
    public void accept(com.android.server.wm.Task task) {
        if (task.getTopNonFinishingActivity() == null) {
            return;
        }
        if (task.effectiveUid != this.mCallingUid && ((task.mUserId != this.mUserId && !this.mCrossUser && !this.mProfileIds.contains(java.lang.Integer.valueOf(task.mUserId))) || !this.mAllowed)) {
            return;
        }
        if (this.mFilterOnlyVisibleRecents && task.getActivityType() != 2 && task.getActivityType() != 3 && !this.mRecentTasks.isVisibleRecentTask(task)) {
            return;
        }
        if (task.isVisibleRequested()) {
            this.mTmpVisibleTasks.add(task);
        } else {
            this.mTmpInvisibleTasks.add(task);
        }
    }

    private android.app.ActivityManager.RunningTaskInfo createRunningTaskInfo(com.android.server.wm.Task task, long visibleActiveTime) {
        android.app.ActivityManager.RunningTaskInfo rti = new android.app.ActivityManager.RunningTaskInfo();
        task.fillTaskInfo(rti, !this.mKeepIntentExtra);
        if (visibleActiveTime > 0) {
            rti.lastActiveTime = visibleActiveTime;
        }
        rti.id = rti.taskId;
        if (!this.mAllowed) {
            com.android.server.wm.Task.trimIneffectiveInfo(task, rti);
        }
        return rti;
    }
}
