package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ActivitySnapshotController extends com.android.server.wm.AbsAppSnapshotController<com.android.server.wm.ActivityRecord, com.android.server.wm.ActivitySnapshotCache> {
    private static final boolean DEBUG = false;
    private static final int MAX_PERSIST_SNAPSHOT_COUNT = 20;
    static final java.lang.String SNAPSHOTS_DIRNAME = "activity_snapshots";
    private static final java.lang.String TAG = "WindowManager";
    private static com.android.server.wm.IActivitySnapshotControllerExt.IStaticExt mActivitySnapConStaticExt = (com.android.server.wm.IActivitySnapshotControllerExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivitySnapshotControllerExt.IStaticExt.class).create();
    private com.android.server.wm.IActivitySnapshotControllerExt mActivitySnapConExt;
    private final android.util.ArraySet<com.android.server.wm.ActivityRecord> mOnBackPressedActivities;
    final android.util.ArraySet<com.android.server.wm.ActivityRecord> mPendingDeleteActivity;
    final android.util.ArraySet<com.android.server.wm.ActivityRecord> mPendingLoadActivity;
    final android.util.ArraySet<com.android.server.wm.ActivityRecord> mPendingRemoveActivity;
    private final com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider mPersistInfoProvider;
    private final com.android.server.wm.TaskSnapshotPersister mPersister;
    private final java.util.ArrayList<com.android.server.wm.ActivitySnapshotController.UserSavedFile> mSavedFilesInOrder;
    private final com.android.server.wm.AppSnapshotLoader mSnapshotLoader;
    private final com.android.server.wm.SnapshotPersistQueue mSnapshotPersistQueue;
    private final java.util.ArrayList<com.android.server.wm.ActivityRecord> mTmpBelowActivities;
    private final java.util.ArrayList<com.android.server.wm.WindowContainer> mTmpTransitionParticipants;
    private final android.util.SparseArray<android.util.SparseArray<com.android.server.wm.ActivitySnapshotController.UserSavedFile>> mUserSavedFiles;

    ActivitySnapshotController(com.android.server.wm.WindowManagerService service, com.android.server.wm.SnapshotPersistQueue persistQueue) {
        super(service);
        this.mPendingRemoveActivity = new android.util.ArraySet<>();
        this.mPendingDeleteActivity = new android.util.ArraySet<>();
        this.mPendingLoadActivity = new android.util.ArraySet<>();
        this.mOnBackPressedActivities = new android.util.ArraySet<>();
        this.mTmpBelowActivities = new java.util.ArrayList<>();
        this.mTmpTransitionParticipants = new java.util.ArrayList<>();
        this.mUserSavedFiles = new android.util.SparseArray<>();
        this.mSavedFilesInOrder = new java.util.ArrayList<>();
        this.mActivitySnapConExt = (com.android.server.wm.IActivitySnapshotControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivitySnapshotControllerExt.class).base(this).create();
        this.mSnapshotPersistQueue = persistQueue;
        this.mPersistInfoProvider = createPersistInfoProvider(service, new com.android.server.wm.ActivitySnapshotController$$ExternalSyntheticLambda1());
        this.mPersister = new com.android.server.wm.TaskSnapshotPersister(persistQueue, this.mPersistInfoProvider);
        this.mSnapshotLoader = new com.android.server.wm.AppSnapshotLoader(this.mPersistInfoProvider);
        initialize(new com.android.server.wm.ActivitySnapshotCache());
        boolean snapshotEnabled = (service.mContext.getResources().getBoolean(android.R.bool.config_disableShutdownVibrationInZen) || !isSnapshotEnabled() || android.app.ActivityManager.isLowRamDeviceStatic()) ? false : true;
        setSnapshotEnabled(snapshotEnabled);
    }

    @Override // com.android.server.wm.AbsAppSnapshotController
    protected float initSnapshotScale() {
        float config = this.mService.mContext.getResources().getFloat(android.R.dimen.config_letterboxDefaultMinAspectRatioForUnresizableApps);
        return java.lang.Math.max(java.lang.Math.min(config, 1.0f), 0.1f);
    }

    static boolean isSnapshotEnabled() {
        return android.os.SystemProperties.getInt("persist.wm.debug.activity_screenshot", 0) != 0 || com.android.window.flags.Flags.activitySnapshotByDefault();
    }

    static com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider createPersistInfoProvider(com.android.server.wm.WindowManagerService service, com.android.server.wm.BaseAppSnapshotPersister.DirectoryResolver resolver) {
        boolean reduceTaskSnapshot = mActivitySnapConStaticExt.reduceTaskSnapshotIfNeed();
        boolean use16BitFormat = service.mContext.getResources().getBoolean(android.R.bool.config_supportsBubble) || reduceTaskSnapshot;
        return new com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider(resolver, SNAPSHOTS_DIRNAME, false, 0.0f, use16BitFormat, reduceTaskSnapshot);
    }

    android.window.TaskSnapshot getSnapshot(com.android.server.wm.ActivityRecord[] activities) {
        com.android.server.wm.ActivitySnapshotController.UserSavedFile tmpUsf;
        if (activities.length == 0 || (tmpUsf = findSavedFile(activities[0])) == null || tmpUsf.mActivityIds.size() != activities.length) {
            return null;
        }
        int fileId = 0;
        for (int i = activities.length - 1; i >= 0; i--) {
            fileId ^= getSystemHashCode(activities[i]);
        }
        int i2 = tmpUsf.mFileId;
        if (i2 == fileId) {
            return ((com.android.server.wm.ActivitySnapshotCache) this.mCache).getSnapshot(java.lang.Integer.valueOf(tmpUsf.mActivityIds.get(0)));
        }
        return null;
    }

    private void cleanUpUserFiles(int userId) {
        synchronized (this.mSnapshotPersistQueue.getLock()) {
            this.mSnapshotPersistQueue.sendToQueueLocked(new com.android.server.wm.SnapshotPersistQueue.WriteQueueItem(this.mPersistInfoProvider, userId) { // from class: com.android.server.wm.ActivitySnapshotController.1
                @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
                void write() {
                    java.io.File[] contents;
                    android.os.Trace.traceBegin(32L, "cleanUpUserFiles");
                    java.io.File file = this.mPersistInfoProvider.getDirectory(this.mUserId);
                    if (file.exists() && (contents = file.listFiles()) != null) {
                        for (int i = contents.length - 1; i >= 0; i--) {
                            contents[i].delete();
                        }
                    }
                    android.os.Trace.traceEnd(32L);
                }
            });
        }
    }

    void addOnBackPressedActivity(com.android.server.wm.ActivityRecord ar) {
        if (shouldDisableSnapshots()) {
            return;
        }
        this.mOnBackPressedActivities.add(ar);
    }

    void clearOnBackPressedActivities() {
        if (shouldDisableSnapshots()) {
            return;
        }
        this.mOnBackPressedActivities.clear();
    }

    void beginSnapshotProcess() {
        if (shouldDisableSnapshots()) {
            return;
        }
        resetTmpFields();
    }

    void endSnapshotProcess() {
        if (shouldDisableSnapshots()) {
            return;
        }
        for (int i = this.mOnBackPressedActivities.size() - 1; i >= 0; i--) {
            handleActivityTransition(this.mOnBackPressedActivities.valueAt(i));
        }
        this.mOnBackPressedActivities.clear();
        this.mTmpTransitionParticipants.clear();
        postProcess();
    }

    void resetTmpFields() {
        this.mPendingRemoveActivity.clear();
        this.mPendingDeleteActivity.clear();
        this.mPendingLoadActivity.clear();
    }

    private void postProcess() {
        loadActivitySnapshot();
        for (int i = this.mPendingRemoveActivity.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord ar = this.mPendingRemoveActivity.valueAt(i);
            removeCachedFiles(ar);
        }
        for (int i2 = this.mPendingDeleteActivity.size() - 1; i2 >= 0; i2--) {
            com.android.server.wm.ActivityRecord ar2 = this.mPendingDeleteActivity.valueAt(i2);
            removeIfUserSavedFileExist(ar2);
        }
        resetTmpFields();
    }

    class LoadActivitySnapshotItem extends com.android.server.wm.SnapshotPersistQueue.WriteQueueItem {
        private final com.android.server.wm.ActivityRecord[] mActivities;
        private final int mCode;

        LoadActivitySnapshotItem(com.android.server.wm.ActivityRecord[] activities, int code, int userId, com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider persistInfoProvider) {
            super(persistInfoProvider, userId);
            this.mActivities = activities;
            this.mCode = code;
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        void write() {
            try {
                android.os.Trace.traceBegin(32L, "load_activity_snapshot");
                android.window.TaskSnapshot snapshot = com.android.server.wm.ActivitySnapshotController.this.mSnapshotLoader.loadTask(this.mCode, this.mUserId, false);
                if (snapshot == null) {
                    return;
                }
                synchronized (com.android.server.wm.ActivitySnapshotController.this.mService.getWindowManagerLock()) {
                    if (com.android.server.wm.ActivitySnapshotController.this.hasRecord(this.mActivities[0])) {
                        for (com.android.server.wm.ActivityRecord ar : this.mActivities) {
                            ((com.android.server.wm.ActivitySnapshotCache) com.android.server.wm.ActivitySnapshotController.this.mCache).putSnapshot(ar, snapshot);
                        }
                    }
                }
            } finally {
                android.os.Trace.traceEnd(32L);
            }
        }

        public boolean equals(java.lang.Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.wm.ActivitySnapshotController.LoadActivitySnapshotItem other = (com.android.server.wm.ActivitySnapshotController.LoadActivitySnapshotItem) o;
            return this.mCode == other.mCode && this.mUserId == other.mUserId && this.mPersistInfoProvider == other.mPersistInfoProvider;
        }

        public java.lang.String toString() {
            return "LoadActivitySnapshotItem{code=" + this.mCode + ", UserId=" + this.mUserId + "}";
        }
    }

    void loadActivitySnapshot() {
        if (this.mPendingLoadActivity.isEmpty()) {
            return;
        }
        android.util.ArraySet<com.android.server.wm.ActivitySnapshotController.UserSavedFile> loadingFiles = new android.util.ArraySet<>();
        for (int i = this.mPendingLoadActivity.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord ar = this.mPendingLoadActivity.valueAt(i);
            com.android.server.wm.ActivitySnapshotController.UserSavedFile usf = findSavedFile(ar);
            if (usf != null) {
                loadingFiles.add(usf);
            }
        }
        int i2 = loadingFiles.size();
        for (int i3 = i2 - 1; i3 >= 0; i3--) {
            com.android.server.wm.ActivitySnapshotController.UserSavedFile usf2 = loadingFiles.valueAt(i3);
            com.android.server.wm.ActivityRecord[] activities = usf2.filterExistActivities(this.mPendingLoadActivity);
            if (activities != null && getSnapshot(activities) == null) {
                loadSnapshotInner(activities, usf2);
            }
        }
    }

    void loadSnapshotInner(com.android.server.wm.ActivityRecord[] activities, com.android.server.wm.ActivitySnapshotController.UserSavedFile usf) {
        synchronized (this.mSnapshotPersistQueue.getLock()) {
            this.mSnapshotPersistQueue.insertQueueAtFirstLocked(new com.android.server.wm.ActivitySnapshotController.LoadActivitySnapshotItem(activities, usf.mFileId, usf.mUserId, this.mPersistInfoProvider));
        }
    }

    void recordSnapshot(java.util.ArrayList<com.android.server.wm.ActivityRecord> activity) {
        if (shouldDisableSnapshots() || activity.isEmpty()) {
            return;
        }
        int size = activity.size();
        int[] mixedCode = new int[size];
        if (size == 1) {
            com.android.server.wm.ActivityRecord singleActivity = activity.get(0);
            android.window.TaskSnapshot snapshot = recordSnapshotInner(singleActivity);
            if (snapshot != null) {
                mixedCode[0] = getSystemHashCode(singleActivity);
                addUserSavedFile(singleActivity.mUserId, snapshot, mixedCode);
                return;
            }
            return;
        }
        com.android.server.wm.Task mainTask = activity.get(0).getTask();
        android.window.TaskSnapshot snapshot2 = this.mService.mTaskSnapshotController.snapshot(mainTask, this.mHighResSnapshotScale);
        if (snapshot2 == null) {
            return;
        }
        for (int i = 0; i < activity.size(); i++) {
            com.android.server.wm.ActivityRecord next = activity.get(i);
            ((com.android.server.wm.ActivitySnapshotCache) this.mCache).putSnapshot(next, snapshot2);
            mixedCode[i] = getSystemHashCode(next);
        }
        int i2 = mainTask.mUserId;
        addUserSavedFile(i2, snapshot2, mixedCode);
    }

    void notifyAppVisibilityChanged(com.android.server.wm.ActivityRecord ar, boolean visible) {
        if (shouldDisableSnapshots()) {
            return;
        }
        com.android.server.wm.Task task = ar.getTask();
        if (task != null && !visible) {
            resetTmpFields();
            addBelowActivityIfExist(ar, this.mPendingRemoveActivity, false, "remove-snapshot");
            postProcess();
        }
    }

    static int getSystemHashCode(com.android.server.wm.ActivityRecord activity) {
        return java.lang.System.identityHashCode(activity);
    }

    void handleTransitionFinish(java.util.ArrayList<com.android.server.wm.WindowContainer> windows) {
        this.mTmpTransitionParticipants.clear();
        this.mTmpTransitionParticipants.addAll(windows);
        for (int i = this.mTmpTransitionParticipants.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer next = this.mTmpTransitionParticipants.get(i);
            if (next.asTask() != null) {
                handleTaskTransition(next.asTask());
            } else if (next.asTaskFragment() != null) {
                com.android.server.wm.TaskFragment tf = next.asTaskFragment();
                com.android.server.wm.ActivityRecord ar = tf.getTopMostActivity();
                if (ar != null) {
                    handleActivityTransition(ar);
                }
            } else if (next.asActivityRecord() != null) {
                handleActivityTransition(next.asActivityRecord());
            }
        }
    }

    private void handleActivityTransition(com.android.server.wm.ActivityRecord ar) {
        if (shouldDisableSnapshots()) {
            return;
        }
        if (ar.isVisibleRequested()) {
            this.mPendingDeleteActivity.add(ar);
            addBelowActivityIfExist(ar, this.mPendingLoadActivity, false, "load-snapshot");
        } else {
            addBelowActivityIfExist(ar, this.mPendingRemoveActivity, true, "remove-snapshot");
        }
    }

    private void handleTaskTransition(com.android.server.wm.Task task) {
        com.android.server.wm.ActivityRecord topActivity;
        if (shouldDisableSnapshots() || (topActivity = task.getTopMostActivity()) == null) {
            return;
        }
        if (task.isVisibleRequested()) {
            addBelowActivityIfExist(topActivity, this.mPendingLoadActivity, true, "load-snapshot");
            adjustSavedFileOrder(task);
        } else {
            addBelowActivityIfExist(topActivity, this.mPendingRemoveActivity, true, "remove-snapshot");
        }
    }

    private void addBelowActivityIfExist(com.android.server.wm.ActivityRecord currentActivity, android.util.ArraySet<com.android.server.wm.ActivityRecord> set, boolean inTransition, java.lang.String debugMessage) {
        getActivityBelow(currentActivity, inTransition, this.mTmpBelowActivities);
        for (int i = this.mTmpBelowActivities.size() - 1; i >= 0; i--) {
            set.add(this.mTmpBelowActivities.get(i));
        }
        this.mTmpBelowActivities.clear();
    }

    private void getActivityBelow(com.android.server.wm.ActivityRecord currentActivity, boolean inTransition, java.util.ArrayList<com.android.server.wm.ActivityRecord> result) {
        com.android.server.wm.ActivityRecord initPrev;
        int currentIndex;
        com.android.server.wm.Task currentTask = currentActivity.getTask();
        if (currentTask == null || (initPrev = currentTask.getActivityBelow(currentActivity)) == null) {
            return;
        }
        com.android.server.wm.TaskFragment currTF = currentActivity.getTaskFragment();
        com.android.server.wm.TaskFragment prevTF = initPrev.getTaskFragment();
        com.android.server.wm.TaskFragment prevAdjacentTF = prevTF != null ? prevTF.getAdjacentTaskFragment() : null;
        if ((currTF == prevTF && currTF != null) || prevAdjacentTF == null) {
            if (!inTransition || isInParticipant(initPrev, this.mTmpTransitionParticipants)) {
                result.add(initPrev);
                return;
            }
            return;
        }
        if (prevAdjacentTF == currTF) {
            getActivityBelow(initPrev, inTransition, result);
            return;
        }
        com.android.server.wm.Task prevAdjacentTask = prevAdjacentTF.getTask();
        if (prevAdjacentTask == currentTask) {
            if (currTF != null) {
                currentIndex = currentTask.mChildren.indexOf(currTF);
            } else {
                currentIndex = currentTask.mChildren.indexOf(currentActivity);
            }
            int prevAdjacentIndex = prevAdjacentTask.mChildren.indexOf(prevAdjacentTF);
            if (prevAdjacentIndex > currentIndex) {
                return;
            }
        }
        if (!inTransition || isInParticipant(initPrev, this.mTmpTransitionParticipants)) {
            result.add(initPrev);
        }
        com.android.server.wm.ActivityRecord prevAdjacentActivity = prevAdjacentTF.getTopMostActivity();
        if (prevAdjacentActivity != null) {
            if (!inTransition || isInParticipant(prevAdjacentActivity, this.mTmpTransitionParticipants)) {
                result.add(prevAdjacentActivity);
            }
        }
    }

    static boolean isInParticipant(com.android.server.wm.ActivityRecord ar, java.util.ArrayList<com.android.server.wm.WindowContainer> transitionParticipants) {
        for (int i = transitionParticipants.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer wc = transitionParticipants.get(i);
            if (ar == wc || ar.isDescendantOf(wc)) {
                return true;
            }
        }
        return false;
    }

    private void adjustSavedFileOrder(com.android.server.wm.Task nextTopTask) {
        nextTopTask.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivitySnapshotController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$adjustSavedFileOrder$0((com.android.server.wm.ActivityRecord) obj);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$adjustSavedFileOrder$0(com.android.server.wm.ActivityRecord ar) {
        com.android.server.wm.ActivitySnapshotController.UserSavedFile usf = findSavedFile(ar);
        if (usf != null) {
            this.mSavedFilesInOrder.remove(usf);
            this.mSavedFilesInOrder.add(usf);
        }
    }

    @Override // com.android.server.wm.AbsAppSnapshotController
    void onAppRemoved(com.android.server.wm.ActivityRecord activity) {
        if (shouldDisableSnapshots()) {
            return;
        }
        removeIfUserSavedFileExist(activity);
    }

    @Override // com.android.server.wm.AbsAppSnapshotController
    void onAppDied(com.android.server.wm.ActivityRecord activity) {
        if (shouldDisableSnapshots()) {
            return;
        }
        removeIfUserSavedFileExist(activity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public com.android.server.wm.ActivityRecord getTopActivity(com.android.server.wm.ActivityRecord activity) {
        return activity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public com.android.server.wm.ActivityRecord getTopFullscreenActivity(com.android.server.wm.ActivityRecord activity) {
        com.android.server.wm.WindowState win = activity.findMainWindow();
        if (win == null || !win.mAttrs.isFullscreen()) {
            return null;
        }
        return activity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public android.app.ActivityManager.TaskDescription getTaskDescription(com.android.server.wm.ActivityRecord object) {
        return object.taskDescription;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public com.android.server.wm.ActivityRecord findAppTokenForSnapshot(com.android.server.wm.ActivityRecord activity) {
        if (activity != null && activity.canCaptureSnapshot()) {
            return activity;
        }
        return null;
    }

    @Override // com.android.server.wm.AbsAppSnapshotController
    protected boolean use16BitFormat() {
        return this.mPersistInfoProvider.use16BitFormat();
    }

    @Override // com.android.server.wm.AbsAppSnapshotController
    protected android.graphics.Rect getLetterboxInsets(com.android.server.wm.ActivityRecord topActivity) {
        return com.android.server.wm.Letterbox.EMPTY_RECT;
    }

    private android.util.SparseArray<com.android.server.wm.ActivitySnapshotController.UserSavedFile> getUserFiles(int userId) {
        if (this.mUserSavedFiles.get(userId) == null) {
            this.mUserSavedFiles.put(userId, new android.util.SparseArray<>());
            cleanUpUserFiles(userId);
        }
        return this.mUserSavedFiles.get(userId);
    }

    com.android.server.wm.ActivitySnapshotController.UserSavedFile findSavedFile(com.android.server.wm.ActivityRecord ar) {
        int code = getSystemHashCode(ar);
        return findSavedFile(ar.mUserId, code);
    }

    com.android.server.wm.ActivitySnapshotController.UserSavedFile findSavedFile(int userId, int code) {
        android.util.SparseArray<com.android.server.wm.ActivitySnapshotController.UserSavedFile> usfs = getUserFiles(userId);
        return usfs.get(code);
    }

    private void removeCachedFiles(com.android.server.wm.ActivityRecord ar) {
        com.android.server.wm.ActivitySnapshotController.UserSavedFile usf = findSavedFile(ar);
        if (usf != null) {
            for (int i = usf.mActivityIds.size() - 1; i >= 0; i--) {
                int activityId = usf.mActivityIds.get(i);
                ((com.android.server.wm.ActivitySnapshotCache) this.mCache).onIdRemoved(java.lang.Integer.valueOf(activityId));
            }
        }
    }

    private void removeIfUserSavedFileExist(com.android.server.wm.ActivityRecord ar) {
        com.android.server.wm.ActivitySnapshotController.UserSavedFile usf = findSavedFile(ar);
        if (usf != null) {
            android.util.SparseArray<com.android.server.wm.ActivitySnapshotController.UserSavedFile> usfs = getUserFiles(ar.mUserId);
            for (int i = usf.mActivityIds.size() - 1; i >= 0; i--) {
                int activityId = usf.mActivityIds.get(i);
                usf.remove(activityId);
                ((com.android.server.wm.ActivitySnapshotCache) this.mCache).onIdRemoved(java.lang.Integer.valueOf(activityId));
                usfs.remove(activityId);
            }
            this.mSavedFilesInOrder.remove(usf);
            this.mPersister.removeSnapshot(usf.mFileId, ar.mUserId);
        }
    }

    boolean hasRecord(com.android.server.wm.ActivityRecord ar) {
        return findSavedFile(ar) != null;
    }

    void addUserSavedFile(int userId, android.window.TaskSnapshot snapshot, int[] code) {
        com.android.server.wm.ActivitySnapshotController.UserSavedFile savedFile = findSavedFile(userId, code[0]);
        if (savedFile != null) {
            android.util.Slog.w(TAG, "Duplicate request for recording activity snapshot " + savedFile);
            return;
        }
        int fileId = 0;
        for (int i = code.length - 1; i >= 0; i--) {
            fileId ^= code[i];
        }
        com.android.server.wm.ActivitySnapshotController.UserSavedFile usf = new com.android.server.wm.ActivitySnapshotController.UserSavedFile(fileId, userId);
        android.util.SparseArray<com.android.server.wm.ActivitySnapshotController.UserSavedFile> usfs = getUserFiles(userId);
        for (int i2 = code.length - 1; i2 >= 0; i2--) {
            usfs.put(code[i2], usf);
        }
        usf.mActivityIds.addAll(code);
        this.mSavedFilesInOrder.add(usf);
        this.mPersister.persistSnapshot(fileId, userId, snapshot);
        if (this.mSavedFilesInOrder.size() > 40) {
            purgeSavedFile();
        }
    }

    private void purgeSavedFile() {
        int savedFileCount = this.mSavedFilesInOrder.size();
        int removeCount = savedFileCount - 20;
        if (removeCount < 1) {
            return;
        }
        java.util.ArrayList<com.android.server.wm.ActivitySnapshotController.UserSavedFile> removeTargets = new java.util.ArrayList<>();
        for (int i = removeCount - 1; i >= 0; i--) {
            com.android.server.wm.ActivitySnapshotController.UserSavedFile usf = this.mSavedFilesInOrder.remove(i);
            android.util.SparseArray<com.android.server.wm.ActivitySnapshotController.UserSavedFile> files = this.mUserSavedFiles.get(usf.mUserId);
            for (int j = usf.mActivityIds.size() - 1; j >= 0; j--) {
                ((com.android.server.wm.ActivitySnapshotCache) this.mCache).removeRunningEntry(java.lang.Integer.valueOf(usf.mActivityIds.get(j)));
                files.remove(usf.mActivityIds.get(j));
            }
            removeTargets.add(usf);
        }
        int i2 = removeTargets.size();
        for (int i3 = i2 - 1; i3 >= 0; i3--) {
            com.android.server.wm.ActivitySnapshotController.UserSavedFile usf2 = removeTargets.get(i3);
            this.mPersister.removeSnapshot(usf2.mFileId, usf2.mUserId);
        }
    }

    @Override // com.android.server.wm.AbsAppSnapshotController
    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        super.dump(pw, prefix);
        java.lang.String doublePrefix = prefix + "  ";
        java.lang.String triplePrefix = doublePrefix + "  ";
        for (int i = this.mUserSavedFiles.size() - 1; i >= 0; i--) {
            android.util.SparseArray<com.android.server.wm.ActivitySnapshotController.UserSavedFile> usfs = this.mUserSavedFiles.valueAt(i);
            pw.println(doublePrefix + "UserSavedFile userId=" + this.mUserSavedFiles.keyAt(i));
            android.util.ArraySet<com.android.server.wm.ActivitySnapshotController.UserSavedFile> sets = new android.util.ArraySet<>();
            for (int j = usfs.size() - 1; j >= 0; j--) {
                sets.add(usfs.valueAt(j));
            }
            int j2 = sets.size();
            for (int j3 = j2 - 1; j3 >= 0; j3--) {
                pw.println(triplePrefix + "SavedFile=" + sets.valueAt(j3));
            }
        }
    }

    static class UserSavedFile {
        final android.util.IntArray mActivityIds = new android.util.IntArray();
        final int mFileId;
        final int mUserId;

        UserSavedFile(int fileId, int userId) {
            this.mFileId = fileId;
            this.mUserId = userId;
        }

        boolean contains(int code) {
            return this.mActivityIds.contains(code);
        }

        void remove(int code) {
            int index = this.mActivityIds.indexOf(code);
            if (index >= 0) {
                this.mActivityIds.remove(index);
            }
        }

        com.android.server.wm.ActivityRecord[] filterExistActivities(android.util.ArraySet<com.android.server.wm.ActivityRecord> pendingLoadActivity) {
            java.util.ArrayList<com.android.server.wm.ActivityRecord> matchedActivities = null;
            for (int i = pendingLoadActivity.size() - 1; i >= 0; i--) {
                com.android.server.wm.ActivityRecord ar = pendingLoadActivity.valueAt(i);
                if (contains(com.android.server.wm.ActivitySnapshotController.getSystemHashCode(ar))) {
                    if (matchedActivities == null) {
                        matchedActivities = new java.util.ArrayList<>();
                    }
                    matchedActivities.add(ar);
                }
            }
            if (matchedActivities == null || matchedActivities.size() != this.mActivityIds.size()) {
                return null;
            }
            return (com.android.server.wm.ActivityRecord[]) matchedActivities.toArray(new com.android.server.wm.ActivityRecord[0]);
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append("UserSavedFile{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(" fileId=");
            sb.append(java.lang.Integer.toHexString(this.mFileId));
            sb.append(", activityIds=[");
            for (int i = this.mActivityIds.size() - 1; i >= 0; i--) {
                sb.append(java.lang.Integer.toHexString(this.mActivityIds.get(i)));
                if (i > 0) {
                    sb.append(',');
                }
            }
            sb.append("]");
            sb.append("}");
            return sb.toString();
        }
    }
}
