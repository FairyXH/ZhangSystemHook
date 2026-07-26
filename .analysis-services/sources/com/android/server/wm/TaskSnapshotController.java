package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TaskSnapshotController extends com.android.server.wm.AbsAppSnapshotController<com.android.server.wm.Task, com.android.server.wm.TaskSnapshotCache> {
    static final java.lang.String SNAPSHOTS_DIRNAME = "snapshots";
    private static com.android.server.wm.ITaskSnapshotControllerExt.IStaticExt mTaskSnapConStaticExt = (com.android.server.wm.ITaskSnapshotControllerExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITaskSnapshotControllerExt.IStaticExt.class).create();
    private final android.os.Handler mHandler;
    private final com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider mPersistInfoProvider;
    private final com.android.server.wm.TaskSnapshotPersister mPersister;
    private final android.util.IntArray mSkipClosingAppSnapshotTasks;
    private com.android.server.wm.ITaskSnapshotControllerExt mTaskSnapConExt;
    private final android.util.ArraySet<com.android.server.wm.Task> mTmpTasks;
    private com.android.server.wm.TaskSnapshotController.TaskSnapshotControllerWrapper mWrapper;

    TaskSnapshotController(com.android.server.wm.WindowManagerService service, com.android.server.wm.SnapshotPersistQueue persistQueue) {
        super(service);
        this.mTaskSnapConExt = (com.android.server.wm.ITaskSnapshotControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITaskSnapshotControllerExt.class).base(this).create();
        this.mSkipClosingAppSnapshotTasks = new android.util.IntArray();
        this.mTmpTasks = new android.util.ArraySet<>();
        this.mHandler = new android.os.Handler();
        this.mWrapper = new com.android.server.wm.TaskSnapshotController.TaskSnapshotControllerWrapper();
        this.mPersistInfoProvider = createPersistInfoProvider(service, new com.android.server.wm.ActivitySnapshotController$$ExternalSyntheticLambda1());
        this.mPersister = new com.android.server.wm.TaskSnapshotPersister(persistQueue, this.mPersistInfoProvider);
        initialize(new com.android.server.wm.TaskSnapshotCache(new com.android.server.wm.AppSnapshotLoader(this.mPersistInfoProvider)));
        boolean snapshotEnabled = !service.mContext.getResources().getBoolean(android.R.bool.config_disableShutdownVibrationInZen);
        setSnapshotEnabled(snapshotEnabled);
    }

    static com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider createPersistInfoProvider(com.android.server.wm.WindowManagerService service, com.android.server.wm.BaseAppSnapshotPersister.DirectoryResolver resolver) {
        float lowResScaleFactor;
        boolean enableLowResSnapshots;
        float highResTaskSnapshotScale = service.mContext.getResources().getFloat(android.R.dimen.config_alertDialogSelectionScrollOffset);
        float lowResTaskSnapshotScale = service.mContext.getResources().getFloat(android.R.dimen.config_displayWhiteBalanceBrightnessFilterIntercept);
        boolean forceReduceSnapshot = mTaskSnapConStaticExt.reduceTaskSnapshotIfNeed();
        if (lowResTaskSnapshotScale < 0.0f || 1.0f <= lowResTaskSnapshotScale) {
            throw new java.lang.RuntimeException("Low-res scale must be between 0 and 1");
        }
        if (highResTaskSnapshotScale <= 0.0f || 1.0f < highResTaskSnapshotScale) {
            throw new java.lang.RuntimeException("High-res scale must be between 0 and 1");
        }
        if (highResTaskSnapshotScale <= lowResTaskSnapshotScale) {
            throw new java.lang.RuntimeException("High-res scale must be greater than low-res scale");
        }
        if (lowResTaskSnapshotScale > 0.0f) {
            float lowResScaleFactor2 = lowResTaskSnapshotScale / highResTaskSnapshotScale;
            lowResScaleFactor = lowResScaleFactor2;
            enableLowResSnapshots = true;
        } else if (forceReduceSnapshot) {
            lowResScaleFactor = 0.4f;
            enableLowResSnapshots = true;
        } else {
            lowResScaleFactor = 0.0f;
            enableLowResSnapshots = false;
        }
        boolean use16BitFormat = service.mContext.getResources().getBoolean(android.R.bool.config_supportsBubble);
        return new com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider(resolver, SNAPSHOTS_DIRNAME, enableLowResSnapshots, lowResScaleFactor, use16BitFormat, forceReduceSnapshot);
    }

    void handleClosingApps(android.util.ArraySet<com.android.server.wm.ActivityRecord> closingApps) {
        com.android.server.wm.Task task;
        if (shouldDisableSnapshots()) {
            return;
        }
        this.mTmpTasks.clear();
        for (int i = closingApps.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord activity = closingApps.valueAt(i);
            if (!activity.isActivityTypeHome() && (task = activity.getTask()) != null) {
                getClosingTasksInner(task, this.mTmpTasks);
            }
        }
        snapshotTasks(this.mTmpTasks);
        this.mTmpTasks.clear();
        this.mSkipClosingAppSnapshotTasks.clear();
    }

    void addSkipClosingAppSnapshotTasks(java.util.Set<com.android.server.wm.Task> tasks) {
        if (shouldDisableSnapshots()) {
            return;
        }
        for (com.android.server.wm.Task task : tasks) {
            this.mSkipClosingAppSnapshotTasks.add(task.mTaskId);
        }
    }

    void snapshotTasks(android.util.ArraySet<com.android.server.wm.Task> tasks) {
        for (int i = tasks.size() - 1; i >= 0; i--) {
            recordSnapshot(tasks.valueAt(i));
        }
    }

    void recordSnapshot(com.android.server.wm.Task task, com.android.server.wm.Transition.ChangeInfo changeInfo) {
        this.mCurrentChangeInfo = changeInfo;
        try {
            recordSnapshot(task);
        } finally {
            this.mCurrentChangeInfo = null;
        }
    }

    android.window.TaskSnapshot recordSnapshot(com.android.server.wm.Task task) {
        android.window.TaskSnapshot snapshot = recordSnapshotInner(task);
        if (snapshot != null && !task.isActivityTypeHome()) {
            this.mPersister.persistSnapshot(task.mTaskId, task.mUserId, snapshot);
            task.onSnapshotChanged(snapshot);
        }
        return snapshot;
    }

    android.window.TaskSnapshot getSnapshot(int taskId, int userId, boolean restoreFromDisk, boolean isLowResolution) {
        return ((com.android.server.wm.TaskSnapshotCache) this.mCache).getSnapshot(taskId, userId, restoreFromDisk, isLowResolution && this.mPersistInfoProvider.enableLowResSnapshots());
    }

    long getSnapshotCaptureTime(int taskId) {
        android.window.TaskSnapshot snapshot = ((com.android.server.wm.TaskSnapshotCache) this.mCache).getSnapshot(java.lang.Integer.valueOf(taskId));
        if (snapshot != null) {
            return snapshot.getCaptureTime();
        }
        return -1L;
    }

    public void clearSnapshotCache() {
        ((com.android.server.wm.TaskSnapshotCache) this.mCache).clearRunningCache();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public com.android.server.wm.ActivityRecord findAppTokenForSnapshot(com.android.server.wm.Task task) {
        return task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.TaskSnapshotController$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.ActivityRecord) obj).canCaptureSnapshot();
            }
        });
    }

    @Override // com.android.server.wm.AbsAppSnapshotController
    protected boolean use16BitFormat() {
        return this.mPersistInfoProvider.use16BitFormat();
    }

    private android.window.ScreenCapture.ScreenshotHardwareBuffer createImeSnapshot(com.android.server.wm.Task task, int pixelFormat) {
        if (task.getSurfaceControl() == null) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_SCREENSHOT) {
                android.util.Slog.w("WindowManager", "Failed to take screenshot. No surface control for " + task);
                return null;
            }
            return null;
        }
        com.android.server.wm.WindowState imeWindow = task.getDisplayContent().mInputMethodWindow;
        if (imeWindow == null || !imeWindow.isVisible()) {
            return null;
        }
        android.graphics.Rect bounds = imeWindow.getParentFrame();
        bounds.offsetTo(0, 0);
        android.window.ScreenCapture.LayerCaptureArgs captureArgs = new android.window.ScreenCapture.LayerCaptureArgs.Builder(imeWindow.getSurfaceControl()).setSourceCrop(bounds).setFrameScale(1.0f).setPixelFormat(pixelFormat).setCaptureSecureLayers(true).build();
        android.window.ScreenCapture.ScreenshotHardwareBuffer imeBuffer = android.window.ScreenCapture.captureLayers(captureArgs);
        return imeBuffer;
    }

    android.window.ScreenCapture.ScreenshotHardwareBuffer snapshotImeFromAttachedTask(com.android.server.wm.Task task) {
        if (checkIfReadyToSnapshot(task) == null) {
            return null;
        }
        int i = 1;
        if (this.mPersistInfoProvider.use16BitFormat() && this.mTaskSnapConExt.canUse16BitFormat(task, true)) {
            i = 4;
        }
        int pixelFormat = i;
        return createImeSnapshot(task, pixelFormat);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public com.android.server.wm.ActivityRecord getTopActivity(com.android.server.wm.Task source) {
        return source.getTopMostActivity();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public com.android.server.wm.ActivityRecord getTopFullscreenActivity(com.android.server.wm.Task source) {
        return source.getTopFullscreenActivity();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.AbsAppSnapshotController
    public android.app.ActivityManager.TaskDescription getTaskDescription(com.android.server.wm.Task source) {
        return source.getTaskDescription();
    }

    @Override // com.android.server.wm.AbsAppSnapshotController
    protected android.graphics.Rect getLetterboxInsets(com.android.server.wm.ActivityRecord topActivity) {
        return topActivity.getLetterboxInsets();
    }

    void getClosingTasksInner(com.android.server.wm.Task task, android.util.ArraySet<com.android.server.wm.Task> outClosingTasks) {
        if (isAnimatingByRecents(task)) {
            this.mSkipClosingAppSnapshotTasks.add(task.mTaskId);
        }
        if (!task.isVisible() && this.mSkipClosingAppSnapshotTasks.indexOf(task.mTaskId) < 0 && this.mTaskSnapConExt.shouldSnapShot(task, this)) {
            outClosingTasks.add(task);
        }
    }

    void removeAndDeleteSnapshot(int taskId, int userId) {
        ((com.android.server.wm.TaskSnapshotCache) this.mCache).onIdRemoved(java.lang.Integer.valueOf(taskId));
        this.mPersister.removeSnapshot(taskId, userId);
    }

    void removeSnapshotCache(int taskId) {
        ((com.android.server.wm.TaskSnapshotCache) this.mCache).removeRunningEntry(java.lang.Integer.valueOf(taskId));
    }

    void removeObsoleteTaskFiles(android.util.ArraySet<java.lang.Integer> persistentTaskIds, int[] runningUserIds) {
        this.mPersister.removeObsoleteFiles(persistentTaskIds, runningUserIds);
    }

    void screenTurningOff(final int displayId, final com.android.server.policy.WindowManagerPolicy.ScreenOffListener listener) {
        if (shouldDisableSnapshots() || this.mTaskSnapConExt.shouldDisableSnapshots()) {
            listener.onScreenOff();
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.TaskSnapshotController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$screenTurningOff$0(displayId, listener);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$screenTurningOff$0(int displayId, com.android.server.policy.WindowManagerPolicy.ScreenOffListener listener) {
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    snapshotForSleeping(displayId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            listener.onScreenOff();
        }
    }

    void snapshotForSleeping(int displayId) {
        com.android.server.wm.DisplayContent displayContent;
        if (shouldDisableSnapshots() || !this.mService.mDisplayEnabled || (displayContent = this.mService.mRoot.getDisplayContent(displayId)) == null) {
            return;
        }
        final boolean allowSnapshotHome = displayId == 0 && this.mService.mPolicy.isKeyguardSecure(this.mService.mCurrentUserId);
        this.mTmpTasks.clear();
        displayContent.forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.TaskSnapshotController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$snapshotForSleeping$1(allowSnapshotHome, (com.android.server.wm.Task) obj);
            }
        }, true);
        snapshotTasks(this.mTmpTasks);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$snapshotForSleeping$1(boolean allowSnapshotHome, com.android.server.wm.Task task) {
        if ((!allowSnapshotHome && task.isActivityTypeHome()) || !task.isVisible() || isAnimatingByRecents(task) || this.mTaskSnapConExt.skipSnapShotForSleeping(task, this)) {
            return;
        }
        this.mTmpTasks.add(task);
    }

    public float getLowResScaleFactor() {
        return this.mPersistInfoProvider.lowResScaleFactor();
    }

    public com.android.server.wm.ITaskSnapshotControllerWrapper getWrapper() {
        return this.mWrapper;
    }

    private class TaskSnapshotControllerWrapper implements com.android.server.wm.ITaskSnapshotControllerWrapper {
        private TaskSnapshotControllerWrapper() {
        }

        @Override // com.android.server.wm.ITaskSnapshotControllerWrapper
        public com.android.server.wm.ITaskSnapshotControllerExt getExtImpl() {
            return com.android.server.wm.TaskSnapshotController.this.mTaskSnapConExt;
        }

        @Override // com.android.server.wm.ITaskSnapshotControllerWrapper
        public com.android.server.wm.ITaskSnapshotControllerExt.IStaticExt getStaticExtImpl() {
            return com.android.server.wm.TaskSnapshotController.mTaskSnapConStaticExt;
        }
    }
}
