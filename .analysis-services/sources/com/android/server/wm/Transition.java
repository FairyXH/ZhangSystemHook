package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class Transition implements com.android.server.wm.BLASTSyncEngine.TransactionReadyListener {
    private static final int CAPTIRE_LAYERS_FOR_SCREEN_ROTATION = -222;
    private static final java.lang.String DEFAULT_PACKAGE = "android";
    static final int PARALLEL_TYPE_FLEXIBLE = 100;
    static final int PARALLEL_TYPE_MUTUAL = 1;
    static final int PARALLEL_TYPE_NONE = 0;
    static final int PARALLEL_TYPE_RECENTS = 2;
    private static final int STATE_ABORT = 3;
    private static final int STATE_COLLECTING = 0;
    private static final int STATE_FINISHED = 4;
    private static final int STATE_PENDING = -1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_STARTED = 1;
    private static final java.lang.String TAG = "Transition";
    private static final java.lang.String TRACE_NAME_PLAY_TRANSITION = "playing";
    private final com.android.server.wm.TransitionController mController;
    private int mFlags;
    private android.window.TransitionInfo.AnimationOptions mOverrideOptions;
    private com.android.server.wm.ActivityRecord mPipActivity;
    long mStatusBarTransitionDelay;
    private final com.android.server.wm.BLASTSyncEngine mSyncEngine;
    java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> mTargets;
    private java.util.ArrayList<com.android.server.wm.Task> mTransientHideTasks;
    final int mType;
    private static final boolean PANIC_DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static com.android.server.wm.ITransitionExt.IStaticExt mTransitionStaticExt = (com.android.server.wm.ITransitionExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITransitionExt.IStaticExt.class).create();
    private int mSyncId = -1;
    private android.view.SurfaceControl.Transaction mStartTransaction = null;
    private android.view.SurfaceControl.Transaction mFinishTransaction = null;
    private android.view.SurfaceControl.Transaction mCleanupTransaction = null;
    private android.view.SurfaceControl.Transaction mInputSinkTransaction = null;
    final android.util.ArrayMap<com.android.server.wm.WindowContainer, com.android.server.wm.Transition.ChangeInfo> mChanges = new android.util.ArrayMap<>();
    final android.util.ArraySet<com.android.server.wm.WindowContainer> mParticipants = new android.util.ArraySet<>();
    private final java.util.ArrayList<com.android.server.wm.DisplayContent> mTargetDisplays = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.Task> mOnTopTasksStart = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.Task> mOnTopTasksAtReady = new java.util.ArrayList<>();
    private final android.util.ArraySet<com.android.server.wm.WindowToken> mVisibleAtTransitionEndTokens = new android.util.ArraySet<>();
    private android.util.ArrayMap<com.android.server.wm.ActivityRecord, com.android.server.wm.Task> mTransientLaunches = null;
    java.util.ArrayList<java.lang.Runnable> mTransactionCompletedListeners = null;
    private java.util.ArrayList<java.lang.Runnable> mTransitionEndedListeners = null;
    private android.os.IRemoteCallback mClientAnimationStartCallback = null;
    private android.os.IRemoteCallback mClientAnimationFinishCallback = null;
    private int mState = -1;
    private final com.android.server.wm.Transition.ReadyTrackerOld mReadyTrackerOld = new com.android.server.wm.Transition.ReadyTrackerOld();
    final com.android.server.wm.Transition.ReadyTracker mReadyTracker = new com.android.server.wm.Transition.ReadyTracker(this);
    private int mRecentsDisplayId = -1;
    private boolean mCanPipOnFinish = true;
    private boolean mIsSeamlessRotation = false;
    private com.android.server.wm.Transition.IContainerFreezer mContainerFreezer = null;
    boolean mPriorVisibilityMightBeDirty = false;
    final com.android.server.wm.TransitionController.Logger mLogger = new com.android.server.wm.TransitionController.Logger();
    private boolean mForcePlaying = false;
    boolean mIsPlayerEnabled = true;
    private com.android.server.wm.ITransitionExt mTransitionExt = (com.android.server.wm.ITransitionExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITransitionExt.class).base(this).create();
    private oplus.util.IOplusGcSupressionExt mOplusGcSupressionExt = (oplus.util.IOplusGcSupressionExt) system.ext.loader.core.ExtLoader.type(oplus.util.IOplusGcSupressionExt.class).create();
    private com.android.server.wm.ITransitionSocExt mTransitionSocExt = (com.android.server.wm.ITransitionSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITransitionSocExt.class).base(this).create();
    int mParallelCollectType = 0;
    int mAnimationTrack = 0;
    java.util.ArrayList<com.android.server.wm.ActivityRecord> mConfigAtEndActivities = null;
    private com.android.server.wm.Transition.TransitionWrapper mTransitionWrapper = new com.android.server.wm.Transition.TransitionWrapper();
    private final com.android.server.wm.Transition.Token mToken = new com.android.server.wm.Transition.Token(this);

    interface IContainerFreezer {
        void cleanUp(android.view.SurfaceControl.Transaction transaction);

        boolean freeze(com.android.server.wm.WindowContainer windowContainer, android.graphics.Rect rect);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface ParallelType {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface TransitionState {
    }

    /* JADX WARN: Multi-variable type inference failed */
    Transition(int i, int i2, com.android.server.wm.TransitionController transitionController, com.android.server.wm.BLASTSyncEngine bLASTSyncEngine) {
        this.mType = i;
        this.mFlags = i2;
        this.mController = transitionController;
        this.mSyncEngine = bLASTSyncEngine;
        this.mLogger.mCreateWallTimeMs = java.lang.System.currentTimeMillis();
        this.mLogger.mCreateTimeNs = android.os.SystemClock.elapsedRealtimeNanos();
        this.mTransitionSocExt.hookInitPerf();
    }

    static com.android.server.wm.Transition fromBinder(android.os.IBinder token) {
        if (token == null) {
            return null;
        }
        try {
            return ((com.android.server.wm.Transition.Token) token).mTransition.get();
        } catch (java.lang.ClassCastException e) {
            android.util.Slog.w(TAG, "Invalid transition token: " + token, e);
            return null;
        }
    }

    android.os.IBinder getToken() {
        return this.mToken;
    }

    void addFlag(int flag) {
        this.mTransitionExt.addFlag(flag);
        this.mFlags |= flag;
    }

    void calcParallelCollectType(android.window.WindowContainerTransaction wct) {
        android.os.Bundle b;
        for (int i = 0; i < wct.getHierarchyOps().size(); i++) {
            android.window.WindowContainerTransaction.HierarchyOp hop = (android.window.WindowContainerTransaction.HierarchyOp) wct.getHierarchyOps().get(i);
            if (hop.getType() == 7 && (b = hop.getLaunchOptions()) != null && !b.isEmpty()) {
                boolean transientLaunch = b.getBoolean("android.activity.transientLaunch");
                if (transientLaunch) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -2700498872917476567L, 0, null, null);
                    }
                    this.mParallelCollectType = 2;
                }
            }
        }
    }

    void setTransientLaunch(com.android.server.wm.ActivityRecord activity, final com.android.server.wm.Task restoreBelow) {
        com.android.server.wm.WindowContainer<?> parent;
        if (this.mTransientLaunches == null) {
            this.mTransientLaunches = new android.util.ArrayMap<>();
            this.mTransientHideTasks = new java.util.ArrayList<>();
        }
        this.mTransientLaunches.put(activity, restoreBelow);
        setTransientLaunchToChanges(activity);
        final com.android.server.wm.Task transientRootTask = activity.getRootTask();
        if (restoreBelow != null) {
            parent = restoreBelow.getParent();
        } else {
            parent = transientRootTask != null ? transientRootTask.getParent() : null;
        }
        if (parent != null) {
            parent.forAllTasks(new java.util.function.Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$setTransientLaunch$0(transientRootTask, restoreBelow, (com.android.server.wm.Task) obj);
                }
            });
            for (int i = this.mChanges.size() - 1; i >= 0; i--) {
                updateTransientFlags(this.mChanges.valueAt(i));
            }
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            long protoLogParam0 = this.mSyncId;
            java.lang.String protoLogParam1 = java.lang.String.valueOf(activity);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -8676279589273455859L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setTransientLaunch$0(com.android.server.wm.Task transientRootTask, com.android.server.wm.Task restoreBelow, com.android.server.wm.Task t) {
        if (t == transientRootTask) {
            return false;
        }
        if ((t.isVisibleRequested() && !t.isAlwaysOnTop()) || this.mTransitionExt.addTaskToTransientHideTasks(t, restoreBelow)) {
            if (t.isRootTask()) {
                this.mTransientHideTasks.add(t);
            }
            if (t.isLeafTask()) {
                collect(t);
            }
        }
        if (restoreBelow != null) {
            if (t != restoreBelow) {
                return false;
            }
            return true;
        }
        if (!t.isRootTask() || !t.fillsParent()) {
            return false;
        }
        return true;
    }

    boolean isInTransientHide(com.android.server.wm.WindowContainer wc) {
        if (this.mTransientHideTasks == null) {
            return false;
        }
        for (int i = this.mTransientHideTasks.size() - 1; i >= 0; i--) {
            com.android.server.wm.Task task = this.mTransientHideTasks.get(i);
            if (wc == task || wc.isDescendantOf(task)) {
                return true;
            }
        }
        return false;
    }

    boolean isTransientVisible(com.android.server.wm.Task task) {
        com.android.server.wm.WindowContainer<?> rootParent;
        if (this.mTransientLaunches == null) {
            return false;
        }
        int occludedCount = 0;
        int numTransient = this.mTransientLaunches.size();
        for (int i = numTransient - 1; i >= 0; i--) {
            com.android.server.wm.Task transientRoot = this.mTransientLaunches.keyAt(i).getRootTask();
            if (transientRoot != null && (rootParent = transientRoot.getParent()) != null && rootParent.getTopChild() != transientRoot && !notOccludedCountIfNeed(rootParent, transientRoot)) {
                com.android.server.wm.ActivityRecord topOpaque = this.mController.mAtm.mTaskSupervisor.mOpaqueActivityHelper.getOpaqueActivity(rootParent, true);
                if (transientRoot.compareTo((com.android.server.wm.WindowContainer) topOpaque.getRootTask()) < 0) {
                    occludedCount++;
                }
            }
        }
        if (occludedCount == numTransient) {
            for (int i2 = this.mTransientLaunches.size() - 1; i2 >= 0; i2--) {
                if (this.mTransientLaunches.keyAt(i2).isDescendantOf(task)) {
                    return true;
                }
            }
            return false;
        }
        return isInTransientHide(task);
    }

    private boolean notOccludedCountIfNeed(com.android.server.wm.WindowContainer<?> rootParent, com.android.server.wm.Task transientRoot) {
        return this.mTransitionExt.notOccludedCountIfNeed(rootParent, transientRoot);
    }

    boolean canApplyDim(com.android.server.wm.Task task) {
        if (this.mTransientLaunches == null) {
            return true;
        }
        com.android.server.wm.Dimmer dimmer = task.getDimmer();
        if (dimmer == null) {
            return false;
        }
        if (dimmer.getHost().asTask() != null || this.mTransitionExt.canApplyDimWithStartingSurface(task)) {
            return true;
        }
        for (int i = this.mTransientLaunches.size() - 1; i >= 0; i--) {
            com.android.server.wm.Task transientTask = this.mTransientLaunches.keyAt(i).getTask();
            if (transientTask != null && transientTask.canAffectSystemUiFlags()) {
                return false;
            }
        }
        return true;
    }

    boolean hasTransientLaunch() {
        return (this.mTransientLaunches == null || this.mTransientLaunches.isEmpty()) ? false : true;
    }

    boolean isTransientLaunch(com.android.server.wm.ActivityRecord activity) {
        return this.mTransientLaunches != null && this.mTransientLaunches.containsKey(activity);
    }

    com.android.server.wm.Task getTransientLaunchRestoreTarget(com.android.server.wm.WindowContainer container) {
        if (this.mTransientLaunches == null) {
            return null;
        }
        for (int i = 0; i < this.mTransientLaunches.size(); i++) {
            if (this.mTransientLaunches.keyAt(i).isDescendantOf(container)) {
                return this.mTransientLaunches.valueAt(i);
            }
        }
        return null;
    }

    boolean isOnDisplay(com.android.server.wm.DisplayContent dc) {
        return this.mTargetDisplays.contains(dc);
    }

    void setConfigAtEnd(com.android.server.wm.WindowContainer<?> wc) {
        wc.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$setConfigAtEnd$1((com.android.server.wm.ActivityRecord) obj);
            }
        });
        snapshotStartState(wc);
        this.mChanges.get(wc).mFlags |= 64;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setConfigAtEnd$1(com.android.server.wm.ActivityRecord ar) {
        if (!ar.isVisible() || !ar.isVisibleRequested()) {
            return;
        }
        if (this.mConfigAtEndActivities == null) {
            this.mConfigAtEndActivities = new java.util.ArrayList<>();
        }
        if (this.mConfigAtEndActivities.contains(ar)) {
            return;
        }
        this.mConfigAtEndActivities.add(ar);
        ar.pauseConfigurationDispatch();
        snapshotStartState(ar);
        this.mChanges.get(ar).mFlags |= 64;
    }

    void setSeamlessRotation(com.android.server.wm.WindowContainer wc) {
        com.android.server.wm.Transition.ChangeInfo info = this.mChanges.get(wc);
        if (info == null) {
            return;
        }
        info.mFlags |= 1;
        onSeamlessRotating(wc.getDisplayContent());
    }

    void onSeamlessRotating(com.android.server.wm.DisplayContent dc) {
        if (this.mSyncEngine.getSyncSet(this.mSyncId).mSyncMethod == 1) {
            return;
        }
        if (this.mContainerFreezer == null) {
            this.mContainerFreezer = new com.android.server.wm.Transition.ScreenshotFreezer();
        }
        com.android.server.wm.WindowState top = dc.getDisplayPolicy().getTopFullscreenOpaqueWindow();
        if (top != null) {
            this.mIsSeamlessRotation = true;
            top.mSyncMethodOverride = 1;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(top.getName());
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 2734227875286695843L, 0, null, protoLogParam0);
            }
        }
    }

    void setPipActivity(com.android.server.wm.ActivityRecord pipActivity) {
        this.mPipActivity = pipActivity;
    }

    com.android.server.wm.ActivityRecord getPipActivity() {
        return this.mPipActivity;
    }

    private void setTransientLaunchToChanges(com.android.server.wm.WindowContainer wc) {
        for (com.android.server.wm.WindowContainer curr = wc; curr != null && this.mChanges.containsKey(curr); curr = curr.getParent()) {
            if (curr.asTask() == null && curr.asActivityRecord() == null) {
                return;
            }
            com.android.server.wm.Transition.ChangeInfo info = this.mChanges.get(curr);
            info.mFlags |= 2;
        }
    }

    void setContainerFreezer(com.android.server.wm.Transition.IContainerFreezer freezer) {
        this.mContainerFreezer = freezer;
    }

    int getState() {
        return this.mState;
    }

    int getSyncId() {
        return this.mSyncId;
    }

    int getFlags() {
        return this.mFlags;
    }

    android.view.SurfaceControl.Transaction getStartTransaction() {
        return this.mStartTransaction;
    }

    android.view.SurfaceControl.Transaction getFinishTransaction() {
        return this.mFinishTransaction;
    }

    boolean isPending() {
        return this.mState == -1;
    }

    boolean isCollecting() {
        return this.mState == 0 || this.mState == 1;
    }

    boolean isAborted() {
        return this.mState == 3;
    }

    boolean isStarted() {
        return this.mState == 1;
    }

    boolean isPlaying() {
        return this.mState == 2;
    }

    boolean isFinished() {
        return this.mState == 4;
    }

    void startCollecting(long timeoutMs) {
        if (this.mState != -1) {
            throw new java.lang.IllegalStateException("Attempting to re-use a transition");
        }
        this.mState = 0;
        this.mSyncId = this.mSyncEngine.startSyncSet(this, timeoutMs, "Transition-" + android.view.WindowManager.transitTypeToString(this.mType), this.mParallelCollectType != 0);
        this.mSyncEngine.setSyncMethod(this.mSyncId, com.android.server.wm.TransitionController.SYNC_METHOD);
        this.mLogger.mSyncId = this.mSyncId;
        this.mLogger.mCollectTimeNs = android.os.SystemClock.elapsedRealtimeNanos();
    }

    void start() {
        if (this.mState < 0) {
            throw new java.lang.IllegalStateException("Can't start Transition which isn't collecting.");
        }
        if (this.mState >= 1) {
            android.util.Slog.w(TAG, "Transition already started id=" + this.mSyncId + " state=" + this.mState);
            return;
        }
        this.mState = 1;
        this.mTransitionSocExt.hookPerfHint(this.mType);
        this.mTransitionExt.startTransition(this.mType);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            long protoLogParam0 = this.mSyncId;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 2808217645990556209L, 1, null, java.lang.Long.valueOf(protoLogParam0));
        }
        applyReady();
        this.mLogger.mStartTimeNs = android.os.SystemClock.elapsedRealtimeNanos();
        this.mController.updateAnimatingState();
    }

    void collect(com.android.server.wm.WindowContainer wc) {
        if (this.mState < 0) {
            throw new java.lang.IllegalStateException("Transition hasn't started collecting.");
        }
        if (!isCollecting()) {
            return;
        }
        if (PANIC_DEBUG) {
            android.util.Slog.d(TAG, "Collecting in transition " + this.mSyncId + ", wc:" + wc + ", caller:" + android.os.Debug.getCallers(5));
        }
        snapshotStartState(getAnimatableParent(wc));
        if (this.mParticipants.contains(wc)) {
            return;
        }
        if (!isInTransientHide(wc)) {
            this.mSyncEngine.addToSyncSet(this.mSyncId, wc);
        }
        if (wc.asWindowToken() != null && wc.asWindowToken().mRoundedCornerOverlay) {
            return;
        }
        com.android.server.wm.Transition.ChangeInfo info = this.mChanges.get(wc);
        if (info == null) {
            info = new com.android.server.wm.Transition.ChangeInfo(wc);
            updateTransientFlags(info);
            this.mChanges.put(wc, info);
        }
        this.mParticipants.add(wc);
        recordDisplay(wc.getDisplayContent());
        if (info.mShowWallpaper) {
            wc.mDisplayContent.mWallpaperController.collectTopWallpapers(this);
        }
    }

    private void snapshotStartState(com.android.server.wm.WindowContainer<?> wc) {
        for (com.android.server.wm.WindowContainer<?> curr = wc; curr != null && !this.mChanges.containsKey(curr); curr = getAnimatableParent(curr)) {
            com.android.server.wm.Transition.ChangeInfo info = new com.android.server.wm.Transition.ChangeInfo(curr);
            updateTransientFlags(info);
            this.mChanges.put(curr, info);
            if (isReadyGroup(curr)) {
                this.mReadyTrackerOld.addGroup(curr);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                    long protoLogParam0 = this.mSyncId;
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(curr);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 65881049096729394L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
                }
            }
        }
    }

    private void updateTransientFlags(com.android.server.wm.Transition.ChangeInfo info) {
        com.android.server.wm.WindowContainer<?> wc = info.mContainer;
        if (!(wc.asTaskFragment() == null && wc.asActivityRecord() == null) && isInTransientHide(wc)) {
            info.mFlags |= 4;
        }
    }

    private void recordDisplay(com.android.server.wm.DisplayContent dc) {
        if (dc == null || this.mTargetDisplays.contains(dc)) {
            return;
        }
        this.mTargetDisplays.add(dc);
        addOnTopTasks(dc, this.mOnTopTasksStart);
        if (this.mController.isAnimating()) {
            dc.enableHighPerfTransition(true);
        }
    }

    void recordTaskOrder(com.android.server.wm.WindowContainer from) {
        recordDisplay(from.getDisplayContent());
    }

    private static void addOnTopTasks(com.android.server.wm.Task task, java.util.ArrayList<com.android.server.wm.Task> out) {
        for (int i = task.getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.Task child = task.getChildAt(i).asTask();
            if (child == null) {
                return;
            }
            if (!child.getWindowConfiguration().isAlwaysOnTop()) {
                out.add(child);
                addOnTopTasks(child, out);
                return;
            }
        }
    }

    private static void addOnTopTasks(com.android.server.wm.DisplayContent dc, java.util.ArrayList<com.android.server.wm.Task> out) {
        com.android.server.wm.Task topNotAlwaysOnTop = dc.getRootTask(new java.util.function.Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Transition.lambda$addOnTopTasks$2((com.android.server.wm.Task) obj);
            }
        });
        if (topNotAlwaysOnTop == null) {
            return;
        }
        out.add(topNotAlwaysOnTop);
        addOnTopTasks(topNotAlwaysOnTop, out);
    }

    static /* synthetic */ boolean lambda$addOnTopTasks$2(com.android.server.wm.Task t) {
        return (t.getWindowConfiguration().isAlwaysOnTop() || t.getWrapper().getExtImpl().isFlexibleAlwaysOnTop()) ? false : true;
    }

    void collectExistenceChange(com.android.server.wm.WindowContainer wc) {
        if (this.mState >= 2) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            long protoLogParam0 = this.mSyncId;
            java.lang.String protoLogParam1 = java.lang.String.valueOf(wc);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1101215730201607371L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
        }
        collect(wc);
        if (this.mChanges.get(wc) == null) {
            return;
        }
        this.mChanges.get(wc).mExistenceChanged = true;
    }

    void collectVisibleChange(com.android.server.wm.WindowContainer wc) {
        if (this.mSyncEngine.getSyncSet(this.mSyncId).mSyncMethod == 1 || wc.mDisplayContent == null || !isInTransition(wc)) {
            return;
        }
        if (!wc.mDisplayContent.getDisplayPolicy().isScreenOnFully() || wc.mDisplayContent.getDisplayInfo().state == 1) {
            this.mFlags |= 1024;
            return;
        }
        if (wc.asActivityRecord() != null) {
            com.android.server.wm.ActivityRecord activityRecord = wc.asActivityRecord();
            if (activityRecord.mStartingData != null && activityRecord.mStartingData.mAssociatedTask != null) {
                return;
            }
        }
        if (this.mContainerFreezer == null) {
            this.mContainerFreezer = new com.android.server.wm.Transition.ScreenshotFreezer();
        }
        com.android.server.wm.Transition.ChangeInfo change = this.mChanges.get(wc);
        if (change == null || !change.mVisible || !wc.isVisibleRequested()) {
            return;
        }
        android.os.Trace.traceBegin(32L, "WMS.doStartFreezingDisplay 1 ");
        this.mContainerFreezer.freeze(wc, change.mAbsoluteBounds);
        android.os.Trace.traceEnd(32L);
        com.oplus.android.internal.util.OplusFrameworkStatsLog.write(100191, java.lang.System.currentTimeMillis(), 1, 0, (java.lang.String) null);
    }

    void collectReparentChange(com.android.server.wm.WindowContainer wc, com.android.server.wm.WindowContainer newParent) {
        com.android.server.wm.WindowContainer prevParent;
        if (!this.mChanges.containsKey(wc)) {
            return;
        }
        com.android.server.wm.Transition.ChangeInfo change = this.mChanges.get(wc);
        if (change.mStartParent == null || change.mStartParent.isAttached()) {
            prevParent = change.mStartParent;
        } else {
            prevParent = change.mCommonAncestor;
        }
        if (prevParent == null || !prevParent.isAttached()) {
            android.util.Slog.w(TAG, "Trying to collect reparenting of a window after the previous parent has been detached: " + wc);
            return;
        }
        if (prevParent == newParent) {
            android.util.Slog.w(TAG, "Trying to collect reparenting of a window that has not been reparented: " + wc);
            return;
        }
        if (!newParent.isAttached()) {
            android.util.Slog.w(TAG, "Trying to collect reparenting of a window that is not attached after reparenting: " + wc);
            return;
        }
        com.android.server.wm.WindowContainer ancestor = newParent;
        while (prevParent != ancestor && !prevParent.isDescendantOf(ancestor)) {
            ancestor = ancestor.getParent();
        }
        change.mCommonAncestor = ancestor;
    }

    void collectClose(com.android.server.wm.WindowContainer<?> wc) {
        if (wc.isVisibleRequested()) {
            collectExistenceChange(wc);
        } else {
            collect(wc);
        }
    }

    boolean isInTransition(com.android.server.wm.WindowContainer wc) {
        for (com.android.server.wm.WindowContainer p = wc; p != null; p = p.getParent()) {
            if (this.mParticipants.contains(p)) {
                return true;
            }
        }
        return false;
    }

    void setKnownConfigChanges(com.android.server.wm.WindowContainer<?> wc, int changes) {
        com.android.server.wm.Transition.ChangeInfo changeInfo = this.mChanges.get(wc);
        if (changeInfo != null) {
            changeInfo.mKnownConfigChanges = changes;
        }
    }

    private void sendRemoteCallback(android.os.IRemoteCallback callback) {
        if (callback == null) {
            return;
        }
        this.mController.mAtm.mH.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((android.os.IRemoteCallback) obj).sendResult((android.os.Bundle) null);
            }
        }, callback));
    }

    void setOverrideAnimation(android.window.TransitionInfo.AnimationOptions options, android.os.IRemoteCallback startCallback, android.os.IRemoteCallback finishCallback) {
        if (isCollecting() && !this.mTransitionExt.shouldIgnoreOverrideAnimation(this.mOverrideOptions, options)) {
            this.mOverrideOptions = options;
            sendRemoteCallback(this.mClientAnimationStartCallback);
            this.mClientAnimationStartCallback = startCallback;
            this.mClientAnimationFinishCallback = finishCallback;
        }
    }

    void setReady(com.android.server.wm.WindowContainer wc, boolean ready) {
        if (!isCollecting() || this.mSyncId < 0) {
            return;
        }
        this.mReadyTrackerOld.setReadyFrom(wc, ready);
        applyReady();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyReady() {
        boolean ready;
        if (this.mState < 1) {
            return;
        }
        if (this.mController.useFullReadyTracking()) {
            ready = this.mReadyTracker.isReady();
        } else {
            ready = this.mReadyTrackerOld.allReady();
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            boolean protoLogParam0 = ready;
            long protoLogParam1 = this.mSyncId;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -3942072270654590479L, 7, null, java.lang.Boolean.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
        }
        boolean changed = this.mSyncEngine.setReady(this.mSyncId, ready);
        if (changed && ready) {
            this.mLogger.mReadyTimeNs = android.os.SystemClock.elapsedRealtimeNanos();
            this.mOnTopTasksAtReady.clear();
            for (int i = 0; i < this.mTargetDisplays.size(); i++) {
                addOnTopTasks(this.mTargetDisplays.get(i), this.mOnTopTasksAtReady);
            }
            this.mController.onTransitionPopulated(this);
        }
    }

    void setAllReady() {
        if (!isCollecting() || this.mSyncId < 0) {
            return;
        }
        this.mReadyTrackerOld.setAllReady();
        applyReady();
    }

    boolean allReady() {
        return this.mReadyTrackerOld.allReady();
    }

    boolean isPopulated() {
        return this.mState >= 1 && this.mReadyTrackerOld.allReady();
    }

    private void resetSurfaceTransform(android.view.SurfaceControl.Transaction t, com.android.server.wm.WindowContainer target, android.view.SurfaceControl targetLeash) {
        android.graphics.Point tmpPos = new android.graphics.Point();
        target.getRelativePosition(tmpPos);
        t.setPosition(targetLeash, tmpPos.x, tmpPos.y);
        if (target.asTaskFragment() == null) {
            t.setCrop(targetLeash, null);
        } else {
            android.graphics.Rect clipRect = target.getResolvedOverrideBounds();
            t.setWindowCrop(targetLeash, clipRect.width(), clipRect.height());
        }
        t.setMatrix(targetLeash, 1.0f, 0.0f, 0.0f, 1.0f);
        if (target.isOrganized() && target.matchParentBounds()) {
            t.setWindowCrop(targetLeash, -1, -1);
        }
    }

    private void buildFinishTransaction(android.view.SurfaceControl.Transaction t, android.window.TransitionInfo info) {
        android.util.ArraySet<com.android.server.wm.DisplayContent> displays = new android.util.ArraySet<>();
        for (int i = this.mTargets.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer<?> target = this.mTargets.get(i).mContainer;
            if (target.getParent() == null) {
                this.mTransitionExt.fixTargetInBuildFinishTransaction(t, info, target);
            } else {
                android.view.SurfaceControl targetLeash = getLeashSurface(target, null);
                android.view.SurfaceControl origParent = getOrigParentSurface(target);
                t.reparent(targetLeash, origParent);
                t.setLayer(targetLeash, target.getLastLayer());
                t.setCornerRadius(targetLeash, 0.0f);
                t.setShadowRadius(targetLeash, 0.0f);
                t.setAlpha(targetLeash, 1.0f);
                displays.add(target.getDisplayContent());
                if ((this.mTargets.get(i).mFlags & 64) == 0) {
                    resetSurfaceTransform(t, target, targetLeash);
                }
                this.mTransitionExt.buildFinishTransaction(t, info, target, targetLeash);
            }
        }
        if (this.mContainerFreezer != null) {
            android.os.Trace.traceBegin(32L, "WMS.doStopFreezingDisplayLocked 1 ");
            this.mContainerFreezer.cleanUp(t);
            android.os.Trace.traceEnd(32L);
        }
        for (int i2 = displays.size() - 1; i2 >= 0; i2--) {
            if (displays.valueAt(i2) != null) {
                this.mTransitionExt.setAssignLayerOnBuildFinish(true);
                assignLayers(displays.valueAt(i2), t);
                this.mTransitionExt.setAssignLayerOnBuildFinish(false);
            }
        }
        for (int i3 = 0; i3 < info.getRootCount(); i3++) {
            t.reparent(info.getRoot(i3).getLeash(), null);
        }
    }

    static void assignLayers(com.android.server.wm.WindowContainer<?> wc, android.view.SurfaceControl.Transaction t) {
        wc.mTransitionController.mBuildingFinishLayers = true;
        try {
            wc.assignChildLayers(t);
        } finally {
            wc.mTransitionController.mBuildingFinishLayers = false;
        }
    }

    private static void buildCleanupTransaction(android.view.SurfaceControl.Transaction t, android.window.TransitionInfo info) {
        int i = info.getChanges().size();
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            android.window.TransitionInfo.Change c = (android.window.TransitionInfo.Change) info.getChanges().get(i);
            if (c.getSnapshot() != null) {
                t.reparent(c.getSnapshot(), null);
            }
            if (c.hasFlags(32) && c.getStartRotation() != c.getEndRotation() && c.getContainer() != null) {
                t.unsetFixedTransformHint(com.android.server.wm.WindowContainer.fromBinder(c.getContainer().asBinder()).asDisplayContent().mSurfaceControl);
            }
        }
        int i2 = info.getRootCount();
        for (int i3 = i2 - 1; i3 >= 0; i3--) {
            android.view.SurfaceControl leash = info.getRoot(i3).getLeash();
            if (leash != null) {
                t.reparent(leash, null);
            }
        }
    }

    void setCanPipOnFinish(boolean canPipOnFinish) {
        this.mCanPipOnFinish = canPipOnFinish;
    }

    private boolean didCommitTransientLaunch() {
        if (this.mTransientLaunches == null) {
            return false;
        }
        for (int j = 0; j < this.mTransientLaunches.size(); j++) {
            if (this.mTransientLaunches.keyAt(j).isVisibleRequested()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkEnterPipOnFinish(com.android.server.wm.ActivityRecord ar) {
        if (!this.mCanPipOnFinish || !ar.isVisible() || ar.getTask() == null || !ar.isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
            return false;
        }
        com.android.server.wm.ActivityRecord resuming = getVisibleTransientLaunch(ar.getTaskDisplayArea());
        if (ar.pictureInPictureArgs != null && ar.pictureInPictureArgs.isAutoEnterEnabled()) {
            if (!ar.getTask().isVisibleRequested() || didCommitTransientLaunch()) {
                ar.supportsEnterPipOnTaskSwitch = true;
            }
            if (!ar.checkEnterPictureInPictureState("enterPictureInPictureMode", true)) {
                return false;
            }
            int prevMode = ar.getTask().getWindowingMode();
            boolean inPip = this.mController.mAtm.enterPictureInPictureMode(ar, ar.pictureInPictureArgs, false, true);
            int currentMode = ar.getTask().getWindowingMode();
            if (prevMode == 1 && currentMode == 2 && this.mTransientLaunches != null && ar.mDisplayContent.hasTopFixedRotationLaunchingApp()) {
                ar.mDisplayContent.mPinnedTaskController.setEnterPipTransaction(null);
            }
            return inPip;
        }
        if ((!ar.getTask().isVisibleRequested() || didCommitTransientLaunch()) && ar.supportsPictureInPicture()) {
            ar.supportsEnterPipOnTaskSwitch = true;
        }
        try {
            this.mController.mAtm.mTaskSupervisor.mUserLeaving = true;
            ar.getTaskFragment().startPausing(false, resuming, "finishTransition");
            return false;
        } finally {
            this.mController.mAtm.mTaskSupervisor.mUserLeaving = false;
        }
    }

    void finishTransition() {
        boolean z;
        com.android.server.wm.Transition.ChangeInfo changeInfo;
        java.lang.String str;
        com.android.server.wm.ActivityRecord top;
        this.mController.mAtm.mTaskSupervisor.getWrapper().getExtImpl().markTransitionFinish(this.mSyncId);
        boolean z2 = PANIC_DEBUG;
        java.lang.String str2 = TAG;
        if (z2) {
            android.util.Slog.d(TAG, "finish transition#" + getSyncId());
        }
        if (android.os.Trace.isTagEnabled(32L) && this.mIsPlayerEnabled) {
            asyncTraceEnd(java.lang.System.identityHashCode(this));
        }
        if (com.android.server.wm.WindowState.DEBUG_PANIC) {
            this.mLogger.mFinishTimeNs = android.os.SystemClock.elapsedRealtimeNanos();
            android.os.Handler handler = this.mController.mLoggerHandler;
            final com.android.server.wm.TransitionController.Logger logger = this.mLogger;
            java.util.Objects.requireNonNull(logger);
            handler.post(new java.lang.Runnable() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    logger.logOnFinish();
                }
            });
            this.mController.mTransitionTracer.logFinishedTransition(this);
        }
        if (this.mStartTransaction != null) {
            this.mStartTransaction.close();
        }
        if (this.mFinishTransaction != null) {
            this.mFinishTransaction.close();
        }
        this.mFinishTransaction = null;
        this.mStartTransaction = null;
        android.view.SurfaceControl.Transaction recentCleanupTransaction = this.mTransitionExt.deferCleanupTransactionApply(this);
        if (this.mCleanupTransaction != null) {
            if (recentCleanupTransaction != null) {
                if (recentCleanupTransaction != this.mCleanupTransaction) {
                    recentCleanupTransaction.merge(this.mCleanupTransaction);
                }
            } else {
                this.mCleanupTransaction.apply();
                this.mCleanupTransaction = null;
            }
        }
        if (this.mState >= 2) {
            this.mController.mFinishingTransition = this;
            this.mController.mExt.setFinishingRecentTransition(this);
            if (this.mTransientHideTasks != null && !this.mTransientHideTasks.isEmpty()) {
                this.mController.mAtm.mRootWindowContainer.ensureActivitiesVisible();
                for (int i = 0; i < this.mTransientHideTasks.size(); i++) {
                    final com.android.server.wm.Task rootTask = this.mTransientHideTasks.get(i);
                    rootTask.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda3
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$finishTransition$4(rootTask, (com.android.server.wm.ActivityRecord) obj);
                        }
                    });
                }
            }
            int i2 = 0;
            boolean hasVisibleTransientLaunch = false;
            boolean enterAutoPip = false;
            boolean committedSomeInvisible = false;
            int i3 = 0;
            while (i3 < this.mParticipants.size()) {
                com.android.server.wm.WindowContainer<?> participant = this.mParticipants.valueAt(i3);
                com.android.server.wm.ActivityRecord ar = participant.asActivityRecord();
                if (ar != null) {
                    com.android.server.wm.Task task = ar.getTask();
                    if (task == null) {
                        str = str2;
                    } else {
                        boolean visibleAtTransitionEnd = this.mVisibleAtTransitionEndTokens.contains(ar);
                        android.util.Slog.d(str2, "finishTransition ar " + ar + ", isVisibleRequested=" + ar.isVisibleRequested() + ", state=" + ar.getState() + ", visibleAtTransitionEndTokens=" + this.mVisibleAtTransitionEndTokens + ", this=" + this);
                        if ((isTransientLaunch(ar) && !ar.isVisibleRequested() && this.mController.inCollectingTransition(ar)) || this.mTransitionExt.forceVisibleAtTransitionEnd(this, ar) || 0 != 0) {
                            if (0 != 0) {
                                android.util.Slog.d(str2, "finishTransition mergedToRecents make " + ar + " visibleAtTransitionEnd");
                            }
                            visibleAtTransitionEnd = true;
                        }
                        if (this.mTransitionExt.deferCommitVisible(this, ar)) {
                            visibleAtTransitionEnd = true;
                        }
                        if (visibleAtTransitionEnd && this.mTransitionExt.forceCommitVisibility(ar)) {
                            this.mController.mValidateCommitVis.add(ar);
                        }
                        if (this.mTransitionExt.deferCommitVisible(this, this.mController, ar)) {
                            visibleAtTransitionEnd = true;
                            this.mController.mValidateCommitVis.add(ar);
                        }
                        boolean isScreenOff = ar.mDisplayContent == null || ar.mDisplayContent.getDisplayInfo().state == 1;
                        if ((!visibleAtTransitionEnd || isScreenOff) && !ar.isVisibleRequested()) {
                            boolean commitVisibility = !checkEnterPipOnFinish(ar);
                            if (commitVisibility) {
                                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                                    java.lang.String protoLogParam0 = java.lang.String.valueOf(ar);
                                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -4688704756793656554L, 0, null, protoLogParam0);
                                }
                                com.android.server.wm.SnapshotController snapController = this.mController.mSnapshotController;
                                if (this.mTransientLaunches == null || task.isVisibleRequested() || task.isActivityTypeHome()) {
                                    str = str2;
                                } else {
                                    str = str2;
                                    long startTimeNs = this.mLogger.mSendTimeNs;
                                    long lastSnapshotTimeNs = snapController.mTaskSnapshotController.getSnapshotCaptureTime(task.mTaskId);
                                    if (lastSnapshotTimeNs < startTimeNs) {
                                        if (this.mTransitionExt.checkIfNeedRecordSnapshot(task.mTaskId)) {
                                            snapController.mTaskSnapshotController.recordSnapshot(task);
                                            this.mController.mExt.recordEmbeddedTaskSnapshots(task);
                                        }
                                    } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                                        long startTimeNs2 = task.mTaskId;
                                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1817207111271920503L, 1, null, java.lang.Long.valueOf(startTimeNs2));
                                    }
                                }
                                if (!mTransitionStaticExt.isTaskBarNoAnim(ar)) {
                                    ar.commitVisibility(false, false, true);
                                }
                                committedSomeInvisible = true;
                                this.mTransitionExt.hideDeferredWallpapersIfNeeded(participant, ar, this.mController);
                            } else {
                                str = str2;
                                enterAutoPip = true;
                            }
                        } else {
                            str = str2;
                        }
                        com.android.server.wm.Transition.ChangeInfo changeInfo2 = this.mChanges.get(ar);
                        if (changeInfo2 != null && changeInfo2.mVisible != visibleAtTransitionEnd) {
                            ar.mEnteringAnimation = visibleAtTransitionEnd;
                            if (ar.mEnteringAnimation) {
                                ar.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda4
                                    @Override // java.util.function.Consumer
                                    public final void accept(java.lang.Object obj) {
                                        com.android.server.wm.Transition.lambda$finishTransition$5((com.android.server.wm.WindowState) obj);
                                    }
                                }, true);
                            }
                        } else if (this.mTransientLaunches != null && this.mTransientLaunches.containsKey(ar) && ar.isVisible()) {
                            ar.mEnteringAnimation = true;
                            if (!task.isFocused() && ar.isTopRunningActivity()) {
                                this.mController.mAtm.setLastResumedActivityUncheckLocked(ar, "transitionFinished");
                            }
                            hasVisibleTransientLaunch = true;
                        }
                    }
                } else {
                    str = str2;
                    if (participant.asDisplayContent() != null) {
                        i2 = 1;
                    } else {
                        final com.android.server.wm.Task tr = participant.asTask();
                        if (tr != null && tr.isVisibleRequested() && tr.inPinnedWindowingMode() && (top = tr.getTopNonFinishingActivity()) != null && !top.inPinnedWindowingMode()) {
                            this.mController.mStateValidators.add(new java.lang.Runnable() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda5
                                @Override // java.lang.Runnable
                                public final void run() {
                                    com.android.server.wm.Transition.lambda$finishTransition$6(tr);
                                }
                            });
                        }
                    }
                }
                i3++;
                str2 = str;
            }
            for (int i4 = this.mParticipants.size() - 1; i4 >= 0; i4--) {
                com.android.server.wm.WallpaperWindowToken wt = this.mParticipants.valueAt(i4).asWallpaperToken();
                if (wt != null) {
                    com.android.server.wm.WindowState target = wt.mDisplayContent.mWallpaperController.getWallpaperTarget();
                    boolean isTargetInvisible = target == null || !target.mToken.isVisible();
                    boolean isWallpaperVisibleAtEnd = wt.isVisibleRequested() || this.mVisibleAtTransitionEndTokens.contains(wt);
                    if (isWallpaperVisibleAtEnd) {
                        wt.mDisplayContent.mWallpaperController.mWallpaperControllerExt.screenshotWallpaper();
                    }
                    if (isTargetInvisible || !isWallpaperVisibleAtEnd) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                            java.lang.String protoLogParam02 = java.lang.String.valueOf(wt);
                            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -2960171012238790176L, 0, null, protoLogParam02);
                        }
                        wt.commitVisibility(false);
                    }
                    if (isTargetInvisible) {
                        com.android.server.wm.DisplayContent displayContent = wt.mDisplayContent;
                        displayContent.pendingLayoutChanges = 4 | displayContent.pendingLayoutChanges;
                    }
                }
            }
            if (committedSomeInvisible) {
                this.mController.onCommittedInvisibles();
            }
            if (hasVisibleTransientLaunch) {
                if (enterAutoPip) {
                    this.mController.mAtm.getTaskChangeNotificationController().notifyTaskStackChanged();
                }
                this.mController.mAtm.stopAppSwitches();
                this.mController.mAtm.mRootWindowContainer.rankTaskLayers();
            }
            commitConfigAtEndActivities();
            for (int i5 = 0; i5 < this.mParticipants.size(); i5++) {
                com.android.server.wm.ActivityRecord ar2 = this.mParticipants.valueAt(i5).asActivityRecord();
                if (ar2 != null) {
                    if ((ar2.isVisibleRequested() || !ar2.isState(com.android.server.wm.ActivityRecord.State.INITIALIZING)) && !ar2.isAnimating(2, 256) && !this.mTransitionExt.hookDispatchLegacyAppTransitionFinished(ar2, this.mController)) {
                        this.mController.dispatchLegacyAppTransitionFinished(ar2);
                    }
                    if (ar2.currentLaunchCanTurnScreenOn() && ar2.getDisplayContent() != null && ar2.getDisplayContent().topRunningActivity() != ar2) {
                        ar2.setCurrentLaunchCanTurnScreenOn(false);
                    }
                }
            }
            int i6 = 0;
            for (int i7 = 0; i7 < this.mParticipants.size(); i7++) {
                com.android.server.wm.ActivityRecord ar3 = this.mParticipants.valueAt(i7).asActivityRecord();
                if (ar3 != null && ar3.isVisible() && ar3.getParent() != null) {
                    i6 = 1;
                    ar3.mActivityRecordInputSink.applyChangesToSurfaceIfChanged(ar3.getPendingTransaction());
                }
            }
            if (i6 != 0) {
                this.mController.mAtm.mWindowManager.scheduleAnimationLocked();
            }
            for (int i8 = 0; i8 < this.mParticipants.size(); i8++) {
                this.mParticipants.valueAt(i8).getWCWrapper().getExtImpl().updateSurfaceVisibility(this.mParticipants.valueAt(i8));
            }
            this.mController.mAtm.mTaskSupervisor.scheduleProcessStoppingAndFinishingActivitiesIfNeeded();
            sendRemoteCallback(this.mClientAnimationFinishCallback);
            legacyRestoreNavigationBarFromApp();
            if (this.mRecentsDisplayId != -1) {
                com.android.server.wm.DisplayContent dc = this.mController.mAtm.mRootWindowContainer.getDisplayContent(this.mRecentsDisplayId);
                dc.getInputMonitor().setActiveRecents(null, null);
                dc.getInputMonitor().updateInputWindowsLw(false);
            }
            if (this.mTransientLaunches != null) {
                for (int i9 = this.mTransientLaunches.size() - 1; i9 >= 0; i9--) {
                    com.android.server.wm.Task task2 = this.mTransientLaunches.keyAt(i9).getTask();
                    if (task2 != null) {
                        task2.setCanAffectSystemUiFlags(true);
                    }
                }
            }
            boolean isMergeToRecentsInLightOs = this.mTransitionExt.isMergeToRecentsInLightOs(this);
            if (this.mTransitionExt.isLightOsEnable() && !isMergeToRecentsInLightOs) {
                this.mTransitionExt.finishTransitionForInterrupt(this, this.mParticipants);
            }
            for (int i10 = 0; i10 < this.mTargetDisplays.size(); i10++) {
                com.android.server.wm.DisplayContent dc2 = this.mTargetDisplays.get(i10);
                com.android.server.wm.AsyncRotationController asyncRotationController = dc2.getAsyncRotationController();
                if (asyncRotationController != null && containsChangeFor(dc2, this.mTargets)) {
                    asyncRotationController.onTransitionFinished();
                }
                if (!this.mTransitionExt.hasAnimatingFixedRotationTransition(dc2)) {
                    dc2.onTransitionFinished();
                }
                if (i2 != 0 && dc2.mDisplayRotationCompatPolicy != null && (changeInfo = this.mChanges.get(dc2)) != null && changeInfo.mRotation != dc2.getWindowConfiguration().getRotation()) {
                    dc2.mDisplayRotationCompatPolicy.onScreenRotationAnimationFinished();
                }
                if (this.mTransientLaunches != null) {
                    com.android.server.wm.TaskDisplayArea transientTDA = null;
                    int t = 0;
                    while (true) {
                        if (t >= this.mTransientLaunches.size()) {
                            break;
                        }
                        if (this.mTransientLaunches.keyAt(t).getDisplayContent() != dc2) {
                            t++;
                        } else {
                            if (hasVisibleTransientLaunch) {
                                updateImeForVisibleTransientLaunch(dc2);
                            }
                            transientTDA = this.mTransientLaunches.keyAt(t).getTaskDisplayArea();
                        }
                    }
                    if (!hasVisibleTransientLaunch && this.mRecentsDisplayId == dc2.mDisplayId) {
                        com.android.server.inputmethod.InputMethodManagerInternal.get().updateImeWindowStatus(false, dc2.getDisplayId());
                    }
                    if (!hasVisibleTransientLaunch && transientTDA != null) {
                        transientTDA.pauseBackTasks(null);
                    }
                }
                dc2.removeImeSurfaceImmediately();
                if (!isMergeToRecentsInLightOs) {
                    dc2.handleCompleteDeferredRemoval();
                }
            }
            validateKeyguardOcclusion();
            this.mState = 4;
            this.mTransitionSocExt.hookPerfLockRelease();
            if (i2 == 0 || this.mController.useShellTransitionsRotation()) {
                z = false;
            } else {
                z = false;
                this.mController.mAtm.mWindowManager.updateRotation(false, false);
            }
            if (!this.mTransitionExt.isLightOsEnable()) {
                this.mTransitionExt.finishTransitionForInterrupt(this, this.mParticipants);
            }
            cleanUpInternal((recentCleanupTransaction == null || recentCleanupTransaction == this.mCleanupTransaction) ? z : true);
            this.mController.mAtm.mBackNavigationController.onTransitionFinish(this.mTargets, this);
            if (!getWrapper().getExtImpl().isFinishByRecents()) {
                this.mTransitionExt.finishTransition(this, this.mParticipants);
            }
            this.mController.mFinishingTransition = null;
            this.mController.mSnapshotController.onTransitionFinish(this, this.mType, this.mTargets);
            this.mController.updateAnimatingState();
            invokeTransitionEndedListeners();
            if ((getFlags() & 128) != 0) {
                this.mController.mExt.setFinishingRecentTransition(null);
            }
            if (!isMergeToRecentsInLightOs) {
                this.mTransitionExt.shouldPerformSurfacePlacement(this);
            }
            if (!isMergeToRecentsInLightOs) {
                this.mController.mLoggerHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$finishTransition$7();
                    }
                });
            }
            this.mTransitionExt.resetOccludeParent(this.mParticipants, this);
            return;
        }
        throw new java.lang.IllegalStateException("Can't finish a non-playing transition " + this.mSyncId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishTransition$4(com.android.server.wm.Task rootTask, com.android.server.wm.ActivityRecord r) {
        if (this.mParticipants.contains(r.getTask())) {
            if (rootTask.isVisibleRequested()) {
                if (!r.isVisibleRequested()) {
                    this.mController.mValidateCommitVis.add(r);
                    return;
                } else {
                    this.mParticipants.add(r);
                    return;
                }
            }
            this.mParticipants.add(r);
        }
    }

    static /* synthetic */ void lambda$finishTransition$5(com.android.server.wm.WindowState w) {
        com.android.server.wm.ActivityRecord activity = w.mActivityRecord;
        if (w.mWinAnimator.mDrawState == 3 && activity != null && activity.canShowWindows()) {
            android.util.Slog.w(TAG, "Transition is finish,but window not show " + w);
            w.performShowLocked();
        }
    }

    static /* synthetic */ void lambda$finishTransition$6(com.android.server.wm.Task tr) {
        if (!tr.isAttached() || !tr.isVisibleRequested() || !tr.inPinnedWindowingMode()) {
            return;
        }
        com.android.server.wm.ActivityRecord currTop = tr.getTopNonFinishingActivity();
        if (currTop.inPinnedWindowingMode()) {
            return;
        }
        android.util.Slog.e(TAG, "Enter-PIP was started but not completed, this is a Shell/SysUI bug. This state breaks gesture-nav, so attempting clean-up.");
        tr.abortPipEnter(currTop);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishTransition$7() {
        this.mTransitionExt.setAnimThreadUxIfNeed(false);
    }

    private void invokeTransitionEndedListeners() {
        if (this.mTransitionEndedListeners == null) {
            return;
        }
        for (int i = 0; i < this.mTransitionEndedListeners.size(); i++) {
            this.mTransitionEndedListeners.get(i).run();
        }
        this.mTransitionEndedListeners = null;
    }

    private void commitConfigAtEndActivities() {
        if (this.mConfigAtEndActivities == null || this.mConfigAtEndActivities.isEmpty()) {
            return;
        }
        final android.view.SurfaceControl.Transaction t = this.mController.mAtm.mWindowManager.mTransactionFactory.get();
        for (int i = 0; i < this.mTargets.size(); i++) {
            com.android.server.wm.WindowContainer target = this.mTargets.get(i).mContainer;
            if (target.getParent() != null && (this.mTargets.get(i).mFlags & 64) != 0) {
                android.view.SurfaceControl targetLeash = getLeashSurface(target, null);
                resetSurfaceTransform(t, target, targetLeash);
            }
        }
        com.android.server.wm.BLASTSyncEngine.SyncGroup sg = this.mSyncEngine.prepareSyncSet(new com.android.server.wm.BLASTSyncEngine.TransactionReadyListener() { // from class: com.android.server.wm.Transition.1
            @Override // com.android.server.wm.BLASTSyncEngine.TransactionReadyListener
            public void onTransactionReady(int mSyncId, android.view.SurfaceControl.Transaction transaction) {
                t.merge(transaction);
                t.apply();
            }

            @Override // com.android.server.wm.BLASTSyncEngine.TransactionReadyListener
            public void onTransactionCommitTimeout() {
                t.apply();
            }
        }, "ConfigAtTransitEnd");
        int syncId = sg.mSyncId;
        this.mSyncEngine.startSyncSet(sg, 5000L, true);
        this.mSyncEngine.setSyncMethod(syncId, 1);
        for (int i2 = 0; i2 < this.mConfigAtEndActivities.size(); i2++) {
            com.android.server.wm.ActivityRecord ar = this.mConfigAtEndActivities.get(i2);
            this.mSyncEngine.addToSyncSet(syncId, ar);
            ar.resumeConfigurationDispatch();
        }
        this.mSyncEngine.setReady(syncId);
    }

    private com.android.server.wm.ActivityRecord getVisibleTransientLaunch(com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        if (this.mTransientLaunches == null) {
            return null;
        }
        for (int i = this.mTransientLaunches.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord candidateActivity = this.mTransientLaunches.keyAt(i);
            if (candidateActivity.getTaskDisplayArea() == taskDisplayArea && candidateActivity.isVisibleRequested()) {
                return candidateActivity;
            }
        }
        return null;
    }

    private void updateImeForVisibleTransientLaunch(com.android.server.wm.DisplayContent dc) {
        com.android.server.wm.InsetsSourceProvider sourceProvider;
        com.android.server.wm.WindowState imeTarget = dc.computeImeTarget(true);
        com.android.server.wm.WindowState imeWindow = dc.mInputMethodWindow;
        if (imeWindow == null || imeTarget == null || !this.mController.hasCollectingRotationChange(dc, dc.getRotation()) || (sourceProvider = imeWindow.getControllableInsetProvider()) == null || sourceProvider.mControl == null || !sourceProvider.isClientVisible() || imeTarget == sourceProvider.getControlTarget()) {
            return;
        }
        android.view.SurfaceControl imeInsetsLeash = sourceProvider.mControl.getLeash();
        com.android.server.wm.InsetsControlTarget controlTarget = sourceProvider.getControlTarget();
        if (imeInsetsLeash != null && controlTarget != null && controlTarget.getWindow() != null && !controlTarget.getWindow().mToken.isVisible()) {
            dc.getSyncTransaction().reparent(imeInsetsLeash, null);
        }
    }

    void abort() {
        if (this.mState == 3) {
            return;
        }
        android.util.Slog.e(TAG, "Aborting Transition mSyncId=" + this.mSyncId + ", caller=" + android.os.Debug.getCallers(3));
        if (this.mState == -1) {
            this.mState = 3;
            return;
        }
        if (this.mState != 0 && this.mState != 1) {
            throw new java.lang.IllegalStateException("Too late to abort. state=" + this.mState);
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            long protoLogParam0 = this.mSyncId;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1230784960534033968L, 1, null, java.lang.Long.valueOf(protoLogParam0));
        }
        this.mTransitionExt.hookOnAbort();
        this.mState = 3;
        this.mLogger.mAbortTimeNs = android.os.SystemClock.elapsedRealtimeNanos();
        this.mController.mTransitionTracer.logAbortedTransition(this);
        this.mSyncEngine.abort(this.mSyncId);
        this.mController.dispatchLegacyAppTransitionCancelled();
        invokeTransitionEndedListeners();
    }

    void playNow() {
        if (this.mState != 0 && this.mState != 1) {
            if (this.mType == 12) {
                android.util.Slog.i(TAG, "playNow() mType == TRANSIT_SLEEP,mState=" + this.mState);
                return;
            }
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            long protoLogParam0 = this.mSyncId;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -892865733969888022L, 1, null, java.lang.Long.valueOf(protoLogParam0));
        }
        this.mForcePlaying = true;
        for (int i = this.mReadyTracker.mConditions.size() - 1; i >= 0; i--) {
            this.mReadyTracker.mConditions.get(i).meetAlternate("play-now");
        }
        com.android.server.wm.Transition.ReadyCondition forcePlay = new com.android.server.wm.Transition.ReadyCondition("force-play-now");
        this.mReadyTracker.add(forcePlay);
        forcePlay.meet();
        setAllReady();
        if (this.mState == 0) {
            start();
        }
        this.mSyncEngine.onSurfacePlacement();
    }

    boolean isForcePlaying() {
        return this.mForcePlaying;
    }

    void setRemoteAnimationApp(android.app.IApplicationThread app) {
        com.android.server.wm.WindowProcessController wpc = this.mController.mAtm.getProcessController(app);
        if (wpc != null) {
            this.mController.mRemotePlayer.update(wpc, true, true);
        }
    }

    void setNoAnimation(com.android.server.wm.WindowContainer wc) {
        com.android.server.wm.Transition.ChangeInfo change = this.mChanges.get(wc);
        if (change == null) {
            throw new java.lang.IllegalStateException("Can't set no-animation property of non-participant");
        }
        change.mFlags |= 8;
    }

    static boolean containsChangeFor(com.android.server.wm.WindowContainer wc, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).mContainer == wc) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.wm.BLASTSyncEngine.TransactionReadyListener
    public void onTransactionReady(int syncId, android.view.SurfaceControl.Transaction transaction) {
        com.android.server.wm.DisplayContent defaultDisplay;
        if (syncId != this.mSyncId) {
            android.util.Slog.e(TAG, "Unexpected Sync ID " + syncId + ". Expected " + this.mSyncId);
            return;
        }
        if (this.mController.useFullReadyTracking()) {
            for (int i = 0; i < this.mReadyTracker.mMet.size(); i++) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                    long protoLogParam0 = this.mSyncId;
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mReadyTracker.mMet.get(i));
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1354622424895965634L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
                }
            }
        }
        commitVisibleActivities(transaction);
        commitVisibleWallpapers(transaction);
        if (this.mTransactionCompletedListeners != null) {
            for (int i2 = 0; i2 < this.mTransactionCompletedListeners.size(); i2++) {
                final java.lang.Runnable listener = this.mTransactionCompletedListeners.get(i2);
                transaction.addTransactionCompletedListener(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new java.util.function.Consumer() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda15
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        listener.run();
                    }
                });
            }
        }
        if (!this.mTargetDisplays.isEmpty()) {
            defaultDisplay = this.mTargetDisplays.get(0);
        } else {
            defaultDisplay = this.mController.mAtm.mRootWindowContainer.getDefaultDisplay();
        }
        com.android.server.wm.DisplayContent primaryDisplay = defaultDisplay;
        this.mTransitionExt.resetStartFromLauncherBeforeAnimGo(false, this, this.mState);
        if (this.mState == 3) {
            this.mController.onAbort(this);
            if (this.mConfigAtEndActivities != null) {
                for (int i3 = 0; i3 < this.mConfigAtEndActivities.size(); i3++) {
                    this.mConfigAtEndActivities.get(i3).resumeConfigurationDispatch();
                }
                this.mConfigAtEndActivities = null;
            }
            primaryDisplay.getPendingTransaction().merge(transaction);
            if (getWrapper().getExtImpl().hasSyncHide()) {
                primaryDisplay.getWCWrapper().getExtImpl().enablePendingApplyTransition(primaryDisplay, primaryDisplay.getPendingTransaction());
            }
            this.mSyncId = -1;
            this.mOverrideOptions = null;
            cleanUpInternal();
            return;
        }
        if (this.mState != 1) {
            android.util.Slog.e(TAG, "Playing a Transition which hasn't started! #" + this.mSyncId + " This will likely cause an exception in Shell");
        }
        primaryDisplay.getWCWrapper().getExtImpl().applyPendingTransitionIfNeed();
        this.mController.mAtm.mTaskSupervisor.getWrapper().getExtImpl().markTransitionReady(this.mSyncId);
        this.mState = 2;
        this.mStartTransaction = transaction;
        this.mFinishTransaction = this.mController.mAtm.mWindowManager.mTransactionFactory.get();
        if (primaryDisplay.isKeyguardLocked()) {
            this.mFlags |= 64;
        }
        collectOrderChanges(this.mController.mWaitingTransitions.isEmpty());
        if (this.mPriorVisibilityMightBeDirty) {
            updatePriorVisibility();
        }
        this.mFlags = this.mTransitionExt.updateFlag(this.mFlags, this.mController.mAtm.mRootWindowContainer.getDefaultDisplay());
        this.mTargets = calculateTargets(this.mParticipants, this.mChanges, this.mFlags);
        this.mController.mAtm.mBackNavigationController.onTransactionReady(this, this.mTargets, transaction);
        int overrideType = this.mTransitionExt.fixTransitType(this.mType, this.mController);
        mTransitionStaticExt.setTransitionToken(this.mToken);
        android.window.TransitionInfo info = calculateTransitionInfo(overrideType, this.mFlags, this.mTargets, transaction);
        info.setDebugId(this.mSyncId);
        this.mController.assignTrack(this, info);
        this.mController.moveToPlaying(this);
        this.mTargetDisplays.clear();
        for (int i4 = 0; i4 < info.getRootCount(); i4++) {
            this.mTargetDisplays.add(this.mController.mAtm.mRootWindowContainer.getDisplayContent(info.getRoot(i4).getDisplayId()));
        }
        for (int i5 = 0; i5 < this.mTargets.size(); i5++) {
            com.android.server.wm.DisplayArea da = this.mTargets.get(i5).mContainer.asDisplayArea();
            if (da != null) {
                if (da.isVisibleRequested()) {
                    this.mController.mValidateDisplayVis.remove(da);
                } else {
                    this.mController.mValidateDisplayVis.add(da);
                }
            }
        }
        this.mTransitionExt.onTransactionReady(this, this.mTargets, info, transaction);
        overrideAnimationOptionsToInfoIfNecessary(info);
        for (int i6 = 0; i6 < this.mTargetDisplays.size(); i6++) {
            handleLegacyRecentsStartBehavior(this.mTargetDisplays.get(i6), info);
            if (this.mRecentsDisplayId != -1) {
                break;
            }
        }
        sendRemoteCallback(this.mClientAnimationStartCallback);
        for (int i7 = this.mParticipants.size() - 1; i7 >= 0; i7--) {
            com.android.server.wm.ActivityRecord ar = this.mParticipants.valueAt(i7).asActivityRecord();
            if (ar != null && ar.isVisibleRequested() && ar.getSurfaceControl() != null && ar.getSurfaceControl().isValid()) {
                transaction.show(ar.getSurfaceControl());
                ar.getWrapper().getExtImpl().hookTransactionReadyShowSurfaces(transaction, true);
                ar.mLastSurfaceShowing = true;
                for (com.android.server.wm.WindowContainer p = ar.getParent(); p != null && !containsChangeFor(p, this.mTargets); p = p.getParent()) {
                    if (p.getSurfaceControl() != null && this.mTransitionExt.showParentIfNeeded(p)) {
                        transaction.show(p.getSurfaceControl());
                    }
                }
            }
        }
        if (this.mTransientLaunches == null) {
            for (int i8 = this.mParticipants.size() - 1; i8 >= 0; i8--) {
                com.android.server.wm.WindowContainer wc = this.mParticipants.valueAt(i8);
                if (wc.asWindowToken() != null && wc.isVisibleRequested()) {
                    this.mVisibleAtTransitionEndTokens.add(wc.asWindowToken());
                }
            }
        }
        for (int i9 = 0; i9 < this.mTargetDisplays.size(); i9++) {
            com.android.server.wm.DisplayContent dc = this.mTargetDisplays.get(i9);
            dc.getWrapper().getExtImpl().forceStartAsyncRotationIfNeed(this);
            com.android.server.wm.AsyncRotationController controller = dc.getAsyncRotationController();
            if (controller != null && containsChangeFor(dc, this.mTargets)) {
                controller.setupStartTransaction(transaction);
            }
        }
        buildFinishTransaction(this.mFinishTransaction, info);
        this.mCleanupTransaction = this.mController.mAtm.mWindowManager.mTransactionFactory.get();
        buildCleanupTransaction(this.mCleanupTransaction, info);
        if (this.mController.getTransitionPlayer() != null && this.mIsPlayerEnabled) {
            this.mController.dispatchLegacyAppTransitionStarting(info, this.mStatusBarTransitionDelay);
            try {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(info);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -5350671621840749173L, 0, null, protoLogParam02);
                }
                this.mLogger.mSendTimeNs = android.os.SystemClock.elapsedRealtimeNanos();
                this.mLogger.mInfo = info;
                this.mTransitionExt.hookSetBinderUxFlag(-1, 1);
                this.mController.getTransitionPlayer().onTransitionReady(this.mToken, info, transaction, this.mFinishTransaction);
                this.mTransitionExt.hookSetBinderUxFlag(-1, 0);
                if (android.os.Trace.isTagEnabled(32L)) {
                    asyncTraceBegin(TRACE_NAME_PLAY_TRANSITION, java.lang.System.identityHashCode(this));
                }
            } catch (android.os.RemoteException e) {
                postCleanupOnFailure();
            }
            for (int i10 = 0; i10 < this.mTargetDisplays.size(); i10++) {
                com.android.server.wm.DisplayContent dc2 = this.mTargetDisplays.get(i10);
                com.android.server.wm.AccessibilityController accessibilityController = dc2.mWmService.mAccessibilityController;
                if (accessibilityController.hasCallbacks()) {
                    accessibilityController.onWMTransition(dc2.getDisplayId(), this.mType, this.mFlags);
                }
            }
        } else {
            if (!this.mIsPlayerEnabled) {
                this.mLogger.mSendTimeNs = android.os.SystemClock.uptimeNanos();
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                    long protoLogParam03 = this.mSyncId;
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1830385055586991567L, 1, null, java.lang.Long.valueOf(protoLogParam03));
                }
            }
            postCleanupOnFailure();
        }
        this.mOverrideOptions = null;
        reportStartReasonsToLogger();
        if (this.mTransientLaunches == null && !this.mTransitionExt.skipRecordSnapshotWhenReady()) {
            this.mController.mSnapshotController.onTransactionReady(this.mType, this.mTargets);
        }
        info.releaseAnimSurfaces();
        if (this.mLogger.mInfo != null) {
            this.mLogger.logOnSendAsync(this.mController.mLoggerHandler);
            this.mController.mTransitionTracer.logSentTransition(this, this.mTargets);
        }
        this.mOplusGcSupressionExt.callGcDesupression(2);
    }

    private void overrideAnimationOptionsToInfoIfNecessary(android.window.TransitionInfo info) {
        if (this.mOverrideOptions == null) {
            return;
        }
        if (!com.android.window.flags.Flags.moveAnimationOptionsToChange()) {
            info.setAnimationOptions(this.mOverrideOptions);
        } else {
            java.util.List<android.window.TransitionInfo.Change> changes = info.getChanges();
            for (int i = changes.size() - 1; i >= 0; i--) {
                if (this.mTargets.get(i).mContainer.asActivityRecord() != null) {
                    changes.get(i).setAnimationOptions(this.mOverrideOptions);
                    changes.get(i).setBackgroundColor(this.mOverrideOptions.getBackgroundColor());
                }
            }
        }
        updateActivityTargetForCrossProfileAnimation(info);
    }

    private void updateActivityTargetForCrossProfileAnimation(android.window.TransitionInfo info) {
        int i;
        if (this.mOverrideOptions.getType() != 12) {
            return;
        }
        for (int i2 = 0; i2 < this.mTargets.size(); i2++) {
            com.android.server.wm.ActivityRecord activity = this.mTargets.get(i2).mContainer.asActivityRecord();
            android.window.TransitionInfo.Change change = (android.window.TransitionInfo.Change) info.getChanges().get(i2);
            if (activity != null && change.getMode() == 1) {
                int flags = change.getFlags();
                if (activity.mUserId == activity.mWmService.mCurrentUserId) {
                    i = 4096;
                } else {
                    i = 8192;
                }
                change.setFlags(flags | i);
                return;
            }
        }
    }

    @Override // com.android.server.wm.BLASTSyncEngine.TransactionReadyListener
    public void onTransactionCommitTimeout() {
        if (this.mCleanupTransaction == null) {
            return;
        }
        for (int i = this.mTargetDisplays.size() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent dc = this.mTargetDisplays.get(i);
            com.android.server.wm.AsyncRotationController asyncRotationController = dc.getAsyncRotationController();
            if (asyncRotationController != null && containsChangeFor(dc, this.mTargets)) {
                asyncRotationController.onTransactionCommitTimeout(this.mCleanupTransaction);
            }
        }
    }

    void addTransactionCompletedListener(java.lang.Runnable listener) {
        if (this.mTransactionCompletedListeners == null) {
            this.mTransactionCompletedListeners = new java.util.ArrayList<>();
        }
        this.mTransactionCompletedListeners.add(listener);
    }

    void addTransitionEndedListener(java.lang.Runnable listener) {
        if (this.mState != 0 && this.mState != 1) {
            throw new java.lang.IllegalStateException("Can't register listeners if the transition isn't collecting. state=" + this.mState);
        }
        if (this.mTransitionEndedListeners == null) {
            this.mTransitionEndedListeners = new java.util.ArrayList<>();
        }
        this.mTransitionEndedListeners.add(listener);
    }

    boolean hasOrderChanges() {
        java.util.ArrayList<com.android.server.wm.Task> onTopTasks = new java.util.ArrayList<>();
        for (com.android.server.wm.DisplayContent dc : this.mTargetDisplays) {
            addOnTopTasks(dc, onTopTasks);
        }
        for (com.android.server.wm.Task task : onTopTasks) {
            if (!this.mOnTopTasksStart.contains(task)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.wm.BLASTSyncEngine.TransactionReadyListener
    public void onPreReady(int i) {
        if (this.mType != 1 && this.mType != 3) {
            android.util.Slog.w(TAG, "transition is not open type, skip onPreReady");
        } else {
            if (i != this.mSyncId) {
                android.util.Slog.e(TAG, "Unexpected Sync ID " + i + ". Expected " + this.mSyncId);
                return;
            }
            if (PANIC_DEBUG) {
                android.util.Slog.d(TAG, "onPreReady " + this.mSyncId + " " + android.os.Debug.getCallers(20));
            }
            start();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00c7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void collectOrderChanges(boolean r12) {
        /*
            Method dump skipped, instruction units count: 305
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.Transition.collectOrderChanges(boolean):void");
    }

    private void postCleanupOnFailure() {
        this.mController.mAtm.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$postCleanupOnFailure$9();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postCleanupOnFailure$9() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mController.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                cleanUpOnFailure();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void cleanUpOnFailure() {
        if (this.mState < 2) {
            return;
        }
        if (this.mStartTransaction != null) {
            this.mStartTransaction.apply();
        }
        if (this.mFinishTransaction != null) {
            this.mFinishTransaction.apply();
        }
        this.mController.finishTransition(this);
    }

    public android.window.TransitionInfo onPreTransactionReady(com.android.server.wm.ActivityRecord startActivity, android.view.SurfaceControl.Transaction tmpStartT, android.view.SurfaceControl.Transaction tmpFinishT) {
        int overrideType = this.mTransitionExt.fixTransitType(this.mType, this.mController);
        boolean isOpeningType = this.mType == 1 || this.mType == 3;
        if (!this.mParticipants.contains(startActivity) || !isOpeningType) {
            return null;
        }
        java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targets = calculateTargets(this.mParticipants, this.mChanges, this.mFlags);
        for (int i = targets.size() - 1; i >= 0; i--) {
            com.android.server.wm.Transition.ChangeInfo info = targets.get(i);
            com.android.server.wm.WindowContainer target = info.mContainer;
            if (target.isActivityTypeHomeOrRecents()) {
                targets.remove(info);
                android.util.Slog.d(TAG, "onPreTransactionReady remove target:" + target);
            }
            if (target.asTask() == null) {
                android.util.Slog.d(TAG, "onPreTransactionReady target " + target + " don't is task, cancel preReady");
                return null;
            }
        }
        android.window.TransitionInfo info2 = calculateTransitionInfo(overrideType, this.mFlags, targets, tmpStartT);
        buildPrereadyFinishTransaction(tmpFinishT, targets);
        info2.setDebugId(this.mSyncId);
        return info2;
    }

    private void buildPrereadyFinishTransaction(android.view.SurfaceControl.Transaction t, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targets) {
        if (t == null || targets.isEmpty()) {
            return;
        }
        for (int i = targets.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer<?> target = targets.get(i).mContainer;
            android.view.SurfaceControl targetLeash = getLeashSurface(target, null);
            android.view.SurfaceControl origParent = getOrigParentSurface(target);
            t.reparent(targetLeash, origParent);
            t.setLayer(targetLeash, target.getLastLayer());
            t.setCornerRadius(targetLeash, 0.0f);
            t.setShadowRadius(targetLeash, 0.0f);
            t.setAlpha(targetLeash, 1.0f);
        }
    }

    private void cleanUpInternal() {
        cleanUpInternal(false);
    }

    private void cleanUpInternal(boolean deferApply) {
        for (int i = 0; i < this.mChanges.size(); i++) {
            com.android.server.wm.Transition.ChangeInfo ci = this.mChanges.valueAt(i);
            if (ci.mSnapshot != null) {
                ci.mSnapshot.release();
            }
        }
        if (this.mCleanupTransaction != null) {
            if (PANIC_DEBUG) {
                android.util.Slog.d(TAG, "cleanUpInternal deferApply=" + deferApply + ", cleanupTransaction=" + this.mCleanupTransaction + " " + this);
            }
            if (!deferApply) {
                this.mCleanupTransaction.apply();
            }
            this.mCleanupTransaction = null;
        }
    }

    private void commitVisibleActivities(android.view.SurfaceControl.Transaction transaction) {
        for (int i = this.mParticipants.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord ar = this.mParticipants.valueAt(i).asActivityRecord();
            this.mTransitionExt.commitVisibleActivitiesIfNeed(this.mParticipants.valueAt(i));
            if (ar != null && ar.getTask() != null) {
                if (ar.isVisibleRequested()) {
                    ar.commitVisibility(true, false, true);
                    ar.commitFinishDrawing(transaction);
                }
                ar.getTask().setDeferTaskAppear(false);
            }
        }
    }

    private void commitVisibleWallpapers(android.view.SurfaceControl.Transaction t) {
        boolean showWallpaper = shouldWallpaperBeVisible();
        for (int i = this.mParticipants.size() - 1; i >= 0; i--) {
            com.android.server.wm.WallpaperWindowToken wallpaper = this.mParticipants.valueAt(i).asWallpaperToken();
            if (wallpaper != null) {
                if (!wallpaper.isVisible() && wallpaper.isVisibleRequested()) {
                    wallpaper.commitVisibility(showWallpaper);
                }
                this.mTransitionExt.hookCommitVisibleWallpapers(this, wallpaper, showWallpaper, t);
                if (showWallpaper && com.android.window.flags.Flags.ensureWallpaperInTransitions() && wallpaper.isVisibleRequested() && getLeashSurface(wallpaper, t) != wallpaper.getSurfaceControl()) {
                    t.show(wallpaper.getSurfaceControl());
                }
            }
        }
    }

    private boolean shouldWallpaperBeVisible() {
        for (int i = this.mParticipants.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer participant = this.mParticipants.valueAt(i);
            if (participant.showWallpaper()) {
                return true;
            }
        }
        return false;
    }

    public void handleLegacyRecentsStartBehavior(com.android.server.wm.DisplayContent dc, android.window.TransitionInfo info) {
        com.android.server.wm.WindowState navWindow;
        com.android.server.wm.Task task;
        if ((this.mFlags & 128) == 0 && !this.mTransitionExt.isMergedToRecents()) {
            return;
        }
        com.android.server.wm.InputConsumerImpl recentsAnimationInputConsumer = dc.getInputMonitor().getInputConsumer("recents_animation_input_consumer");
        com.android.server.wm.ActivityRecord recentsActivity = null;
        if (recentsAnimationInputConsumer != null) {
            com.android.server.wm.Task topNonRecentsTask = null;
            for (int i = 0; i < info.getChanges().size(); i++) {
                android.app.ActivityManager.RunningTaskInfo taskInfo = ((android.window.TransitionInfo.Change) info.getChanges().get(i)).getTaskInfo();
                if (taskInfo != null && (task = com.android.server.wm.Task.fromWindowContainerToken(taskInfo.token)) != null) {
                    int activityType = taskInfo.topActivityType;
                    boolean isRecents = activityType == 2 || activityType == 3;
                    if (isRecents && recentsActivity == null) {
                        recentsActivity = task.getTopVisibleActivity();
                    } else if (!isRecents && topNonRecentsTask == null) {
                        topNonRecentsTask = task;
                    }
                }
            }
            recentsActivity = this.mTransitionExt.updateRecentsActivityForInterruptIfNeed(recentsActivity);
            if (recentsActivity != null && topNonRecentsTask != null) {
                recentsAnimationInputConsumer.mWindowHandle.touchableRegion.set(topNonRecentsTask.getBounds());
                dc.getInputMonitor().setActiveRecents(recentsActivity, topNonRecentsTask);
            }
        }
        if (recentsActivity == null) {
            return;
        }
        this.mRecentsDisplayId = dc.mDisplayId;
        if (!dc.getDisplayPolicy().shouldAttachNavBarToAppDuringTransition() || dc.getAsyncRotationController() != null) {
            return;
        }
        com.android.server.wm.WindowContainer topWC = null;
        for (int i2 = 0; i2 < info.getChanges().size(); i2++) {
            android.window.TransitionInfo.Change c = (android.window.TransitionInfo.Change) info.getChanges().get(i2);
            if (c.getTaskInfo() != null && c.getTaskInfo().displayId == this.mRecentsDisplayId && c.getTaskInfo().getActivityType() == 1 && (c.getMode() == 2 || c.getMode() == 4)) {
                topWC = com.android.server.wm.WindowContainer.fromBinder(c.getContainer().asBinder());
                break;
            }
        }
        if (topWC == null || topWC.inMultiWindowMode() || (navWindow = dc.getDisplayPolicy().getNavigationBar()) == null || navWindow.mToken == null) {
            return;
        }
        this.mController.mNavigationBarAttachedToApp = true;
        navWindow.mToken.cancelAnimation();
        android.view.SurfaceControl.Transaction t = navWindow.mToken.getPendingTransaction();
        android.view.SurfaceControl navSurfaceControl = navWindow.mToken.getSurfaceControl();
        t.reparent(navSurfaceControl, topWC.getSurfaceControl());
        t.show(navSurfaceControl);
        com.android.server.wm.WindowContainer imeContainer = dc.getImeContainer();
        if (imeContainer.isVisible()) {
            t.setRelativeLayer(navSurfaceControl, imeContainer.getSurfaceControl(), 1);
        } else {
            t.setLayer(navSurfaceControl, Integer.MAX_VALUE);
        }
        com.android.server.statusbar.StatusBarManagerInternal bar = dc.getDisplayPolicy().getStatusBarManagerInternal();
        if (bar != null) {
            bar.setNavigationBarLumaSamplingEnabled(this.mRecentsDisplayId, false);
        }
    }

    void legacyRestoreNavigationBarFromApp() {
        if (!this.mController.mNavigationBarAttachedToApp) {
            return;
        }
        this.mController.mNavigationBarAttachedToApp = false;
        int recentsDisplayId = this.mRecentsDisplayId;
        if (recentsDisplayId == -1) {
            android.util.Slog.i(TAG, "Restore parent surface of navigation bar by another transition");
            recentsDisplayId = 0;
        }
        com.android.server.wm.DisplayContent dc = this.mController.mAtm.mRootWindowContainer.getDisplayContent(recentsDisplayId);
        com.android.server.statusbar.StatusBarManagerInternal bar = dc.getDisplayPolicy().getStatusBarManagerInternal();
        if (bar != null) {
            bar.setNavigationBarLumaSamplingEnabled(recentsDisplayId, true);
        }
        com.android.server.wm.WindowState navWindow = dc.getDisplayPolicy().getNavigationBar();
        if (navWindow == null) {
            return;
        }
        navWindow.setSurfaceTranslationY(0);
        com.android.server.wm.WindowToken navToken = navWindow.mToken;
        if (navToken == null) {
            return;
        }
        android.view.SurfaceControl.Transaction t = dc.getPendingTransaction();
        com.android.server.wm.WindowContainer parent = navToken.getParent();
        t.setLayer(navToken.getSurfaceControl(), navToken.getLastLayer());
        boolean animate = false;
        int i = 0;
        while (true) {
            if (i < this.mTargets.size()) {
                com.android.server.wm.Task task = this.mTargets.get(i).mContainer.asTask();
                if (task == null || !task.isActivityTypeHomeOrRecents()) {
                    i++;
                } else {
                    animate = task.isVisibleRequested();
                    break;
                }
            } else {
                break;
            }
        }
        if (animate) {
            com.android.server.wm.NavBarFadeAnimationController controller = new com.android.server.wm.NavBarFadeAnimationController(dc);
            controller.fadeWindowToken(true);
        } else {
            t.reparent(navToken.getSurfaceControl(), parent.getSurfaceControl());
        }
        dc.mWmService.scheduleAnimationLocked();
    }

    private void reportStartReasonsToLogger() {
        android.util.ArrayMap<com.android.server.wm.WindowContainer, java.lang.Integer> reasons = new android.util.ArrayMap<>();
        for (int i = this.mParticipants.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord r = this.mParticipants.valueAt(i).asActivityRecord();
            if (r != null && r.isVisibleRequested()) {
                int transitionReason = 2;
                if ((r.mStartingData instanceof com.android.server.wm.SplashScreenStartingData) && !r.mLastAllReadyAtSync) {
                    transitionReason = 1;
                } else if (r.isActivityTypeHomeOrRecents() && isTransientLaunch(r)) {
                    transitionReason = 5;
                }
                reasons.put(r, java.lang.Integer.valueOf(transitionReason));
            }
        }
        this.mController.mAtm.mTaskSupervisor.getActivityMetricsLogger().notifyTransitionStarting(reasons);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        java.lang.String subPrefix = "  " + prefix;
        pw.println(subPrefix + toString());
        for (int i = 0; i < this.mParticipants.size(); i++) {
            pw.println(subPrefix + this.mParticipants.valueAt(i));
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(64);
        sb.append("TransitionRecord{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(" id=" + this.mSyncId);
        sb.append(" track=" + this.mAnimationTrack);
        sb.append(" type=" + android.view.WindowManager.transitTypeToString(this.mType));
        sb.append(" flags=0x" + java.lang.Integer.toHexString(this.mFlags));
        sb.append(" state=" + this.mState);
        sb.append('}');
        return sb.toString();
    }

    private static com.android.server.wm.WindowContainer<?> getAnimatableParent(com.android.server.wm.WindowContainer<?> wc) {
        com.android.server.wm.WindowContainer<?> parent = wc.getParent();
        while (parent != null && !parent.canCreateRemoteAnimationTarget() && !parent.isOrganized()) {
            parent = parent.getParent();
        }
        return parent;
    }

    private static boolean reportIfNotTop(com.android.server.wm.WindowContainer wc) {
        return wc.isOrganized();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isWallpaper(com.android.server.wm.WindowContainer wc) {
        return wc.asWallpaperToken() != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isInputMethod(com.android.server.wm.WindowContainer wc) {
        return wc.getWindowType() == 2011;
    }

    private static boolean occludesKeyguard(com.android.server.wm.WindowContainer wc) {
        com.android.server.wm.ActivityRecord top;
        com.android.server.wm.ActivityRecord ar = wc.asActivityRecord();
        if (ar != null) {
            return ar.canShowWhenLocked();
        }
        com.android.server.wm.Task t = wc.asTask();
        return (t == null || (top = t.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.ActivityRecord) obj).isClientVisible();
            }
        })) == null || !top.canShowWhenLocked()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isTranslucent(com.android.server.wm.WindowContainer wc) {
        com.android.server.wm.TaskFragment taskFragment = wc.asTaskFragment();
        if (taskFragment == null) {
            return true ^ wc.fillsParent();
        }
        if (taskFragment.isTranslucentForTransition()) {
            return true;
        }
        com.android.server.wm.TaskFragment adjacentTaskFragment = taskFragment.getAdjacentTaskFragment();
        if (adjacentTaskFragment == null) {
            return true ^ wc.fillsParent();
        }
        return adjacentTaskFragment.isTranslucentForTransition();
    }

    private void updatePriorVisibility() {
        for (int i = 0; i < this.mChanges.size(); i++) {
            com.android.server.wm.Transition.ChangeInfo chg = this.mChanges.valueAt(i);
            if ((chg.mContainer.asActivityRecord() != null || chg.mContainer.asTask() != null) && chg.mVisible) {
                chg.mVisible = chg.mContainer.isVisible();
            }
        }
    }

    private static boolean canPromote(com.android.server.wm.Transition.ChangeInfo targetChange, com.android.server.wm.Transition.Targets targets, android.util.ArrayMap<com.android.server.wm.WindowContainer, com.android.server.wm.Transition.ChangeInfo> changes) {
        com.android.server.wm.WindowContainer<?> target = targetChange.mContainer;
        com.android.server.wm.WindowContainer<?> parent = target.getParent();
        com.android.server.wm.Transition.ChangeInfo parentChange = changes.get(parent);
        if (!parent.canCreateRemoteAnimationTarget() || parentChange == null || !parentChange.hasChanged()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf("parent can't be target " + parent);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -758501334967569539L, 0, null, protoLogParam0);
            }
            return false;
        }
        if (isWallpaper(target)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -2714847784842612086L, 0, null, null);
            }
            return false;
        }
        if (targetChange.mStartParent != null && target.getParent() != targetChange.mStartParent && mTransitionStaticExt.dontPromoteWhenReparent(targetChange, changes)) {
            return false;
        }
        int mode = targetChange.getTransitMode(target);
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer<?> sibling = parent.getChildAt(i);
            if (target != sibling) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(sibling);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 1855461834864671586L, 0, null, protoLogParam02);
                }
                com.android.server.wm.Transition.ChangeInfo siblingChange = changes.get(sibling);
                if (siblingChange == null || !targets.wasParticipated(siblingChange)) {
                    if (sibling.isVisibleRequested()) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -6292043690918793069L, 0, null, null);
                        }
                        return false;
                    }
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                        java.lang.String protoLogParam03 = java.lang.String.valueOf(sibling);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 7897657428993391672L, 0, null, protoLogParam03);
                    }
                } else {
                    int siblingMode = siblingChange.getTransitMode(sibling);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                        java.lang.String protoLogParam04 = java.lang.String.valueOf(android.window.TransitionInfo.modeToString(siblingMode));
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 3873493605120555608L, 0, null, protoLogParam04);
                    }
                    if (reduceMode(mode) != reduceMode(siblingMode)) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                            java.lang.String protoLogParam05 = java.lang.String.valueOf(android.window.TransitionInfo.modeToString(mode));
                            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 7665553560859456426L, 0, null, protoLogParam05);
                        }
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static int reduceMode(int mode) {
        switch (mode) {
            case 3:
                return 1;
            case 4:
                return 2;
            default:
                return mode;
        }
    }

    private static void tryPromote(com.android.server.wm.Transition.Targets targets, android.util.ArrayMap<com.android.server.wm.WindowContainer, com.android.server.wm.Transition.ChangeInfo> changes) {
        com.android.server.wm.WindowContainer<?> lastNonPromotableParent = null;
        int i = targets.mArray.size() - 1;
        while (i >= 0) {
            com.android.server.wm.Transition.ChangeInfo targetChange = targets.mArray.valueAt(i);
            com.android.server.wm.WindowContainer<?> target = targetChange.mContainer;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(target);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -8916099332247176657L, 0, null, protoLogParam0);
            }
            com.android.server.wm.WindowContainer<?> parent = target.getParent();
            if (parent == lastNonPromotableParent) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -6818387694968032301L, 0, null, null);
                }
            } else if (!canPromote(targetChange, targets, changes) || !mTransitionStaticExt.canPromote(targetChange, changes)) {
                lastNonPromotableParent = parent;
            } else {
                if (reportIfNotTop(target)) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                        java.lang.String protoLogParam02 = java.lang.String.valueOf(target);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -7326702978448933012L, 0, null, protoLogParam02);
                    }
                } else {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                        java.lang.String protoLogParam03 = java.lang.String.valueOf(target);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 943961036184959431L, 0, null, protoLogParam03);
                    }
                    targets.remove(i);
                }
                com.android.server.wm.Transition.ChangeInfo parentChange = changes.get(parent);
                if (targets.mArray.indexOfValue(parentChange) < 0) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                        java.lang.String protoLogParam04 = java.lang.String.valueOf(parent);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 841543868388687804L, 0, null, protoLogParam04);
                    }
                    i++;
                    targets.add(parentChange);
                }
                if ((targetChange.mFlags & 8) != 0) {
                    parentChange.mFlags |= 8;
                } else {
                    parentChange.mFlags |= 16;
                }
                if ((targetChange.mFlags & 64) != 0) {
                    parentChange.mFlags |= 64;
                }
            }
            i--;
        }
    }

    static java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> calculateTargets(android.util.ArraySet<com.android.server.wm.WindowContainer> participants, android.util.ArrayMap<com.android.server.wm.WindowContainer, com.android.server.wm.Transition.ChangeInfo> changes, int flags) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(participants);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 743586316159041023L, 0, null, protoLogParam0);
        }
        com.android.server.wm.Transition.Targets targets = new com.android.server.wm.Transition.Targets();
        for (int i = participants.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer<?> wc = participants.valueAt(i);
            if (!wc.isAttached()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(wc);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -7247430213293162757L, 0, null, protoLogParam02);
                }
            } else if (wc.asWindowState() == null) {
                com.android.server.wm.Transition.ChangeInfo changeInfo = changes.get(wc);
                if (changeInfo == null || (!changeInfo.hasChanged() && ((!com.android.window.flags.Flags.ensureWallpaperInTransitions() || wc.asWallpaperToken() == null) && !mTransitionStaticExt.isTaskBarAnim(wc)))) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                        java.lang.String protoLogParam03 = java.lang.String.valueOf(wc);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -5811837191094192313L, 0, null, protoLogParam03);
                    }
                } else {
                    targets.add(changeInfo);
                }
            }
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam04 = java.lang.String.valueOf(targets.mArray);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -1153926883525904120L, 0, null, protoLogParam04);
        }
        tryPromote(targets, changes);
        populateParentChanges(targets, changes);
        java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targetList = targets.getListSortedByZ();
        mTransitionStaticExt.updateAnimTargetIfNeed(participants, targetList, changes);
        mTransitionStaticExt.filterAnimTargetIfNeed(targetList, changes, flags);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam05 = java.lang.String.valueOf(targetList);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -9191328656870721224L, 0, null, protoLogParam05);
        }
        return targetList;
    }

    private static void populateParentChanges(com.android.server.wm.Transition.Targets targets, android.util.ArrayMap<com.android.server.wm.WindowContainer, com.android.server.wm.Transition.ChangeInfo> changes) {
        java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> intermediates = new java.util.ArrayList<>();
        java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targetList = new java.util.ArrayList<>(targets.mArray.size());
        for (int i = targets.mArray.size() - 1; i >= 0; i--) {
            targetList.add(targets.mArray.valueAt(i));
        }
        int i2 = targetList.size();
        for (int i3 = i2 - 1; i3 >= 0; i3--) {
            com.android.server.wm.Transition.ChangeInfo targetChange = targetList.get(i3);
            com.android.server.wm.WindowContainer wc = targetChange.mContainer;
            boolean skipIntermediateReports = isWallpaper(wc);
            intermediates.clear();
            boolean foundParentInTargets = false;
            com.android.server.wm.WindowContainer p = getAnimatableParent(wc);
            while (true) {
                if (p == null) {
                    break;
                }
                com.android.server.wm.Transition.ChangeInfo parentChange = changes.get(p);
                if (parentChange == null) {
                    break;
                }
                if (parentChange.hasChanged() && p.mRemoteToken != null) {
                    if (parentChange.mEndParent != null && !skipIntermediateReports) {
                        targetChange.mEndParent = p;
                        break;
                    }
                    if (targetList.contains(parentChange)) {
                        if (skipIntermediateReports) {
                            targetChange.mEndParent = p;
                        } else {
                            intermediates.add(parentChange);
                        }
                        if ((targetChange.mFlags & 64) != 0 && targetChange.mContainer.asActivityRecord() != null && targetChange.mContainer.getParent() == p) {
                            parentChange.mFlags |= 64;
                        }
                        foundParentInTargets = true;
                    } else if (reportIfNotTop(p) && !skipIntermediateReports) {
                        intermediates.add(parentChange);
                    }
                }
                p = getAnimatableParent(p);
            }
            if (foundParentInTargets && !intermediates.isEmpty()) {
                targetChange.mEndParent = intermediates.get(0).mContainer;
                for (int j = 0; j < intermediates.size() - 1; j++) {
                    com.android.server.wm.Transition.ChangeInfo intermediate = intermediates.get(j);
                    intermediate.mEndParent = intermediates.get(j + 1).mContainer;
                    targets.add(intermediate);
                }
            }
        }
    }

    private static android.view.SurfaceControl getLeashSurface(com.android.server.wm.WindowContainer wc, android.view.SurfaceControl.Transaction t) {
        com.android.server.wm.WindowToken asToken;
        com.android.server.wm.DisplayContent asDC = wc.asDisplayContent();
        if (asDC != null) {
            return asDC.getWindowingLayer();
        }
        if (!wc.mTransitionController.useShellTransitionsRotation() && (asToken = wc.asWindowToken()) != null) {
            android.view.SurfaceControl leash = t != null ? asToken.getOrCreateFixedRotationLeash(t) : asToken.getFixedRotationLeash();
            if (leash != null) {
                return leash;
            }
        }
        return wc.getSurfaceControl();
    }

    private static android.view.SurfaceControl getOrigParentSurface(com.android.server.wm.WindowContainer wc) {
        if (wc.asDisplayContent() != null) {
            return wc.getSurfaceControl();
        }
        return mTransitionStaticExt.getReplaceParentSurface(wc, wc.getParent().getSurfaceControl());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isReadyGroup(com.android.server.wm.WindowContainer wc) {
        return wc instanceof com.android.server.wm.DisplayContent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getDisplayId(com.android.server.wm.WindowContainer wc) {
        if (wc.getDisplayContent() != null) {
            return wc.getDisplayContent().getDisplayId();
        }
        return -1;
    }

    static void calculateTransitionRoots(android.window.TransitionInfo outInfo, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> sortedTargets, android.view.SurfaceControl.Transaction startT) {
        com.android.server.wm.DisplayContent dc;
        for (int i = 0; i < sortedTargets.size(); i++) {
            com.android.server.wm.WindowContainer<?> wc = sortedTargets.get(i).mContainer;
            if (!isWallpaper(wc) && (dc = wc.getDisplayContent()) != null) {
                int endDisplayId = dc.getDisplayId();
                if (outInfo.findRootIndex(endDisplayId) < 0) {
                    com.android.server.wm.WindowContainer<?> ancestor = findCommonAncestor(sortedTargets, wc);
                    boolean hasReparent = !wc.isDescendantOf(ancestor);
                    com.android.server.wm.WindowContainer<?> parent = wc;
                    if (hasReparent) {
                        android.util.Slog.e(TAG, "Did not find common ancestor! Ancestor= " + ancestor + " target= " + wc);
                    } else {
                        while (parent.getParent() != ancestor) {
                            parent = parent.getParent();
                        }
                    }
                    android.view.SurfaceControl rootLeash = mTransitionStaticExt.getPreReadyRootLeashIfNeed(outInfo, parent);
                    if (rootLeash == null) {
                        rootLeash = parent.makeAnimationLeash().setName("Transition Root: " + parent.getName()).setCallsite("Transition.calculateTransitionRoots").build();
                    }
                    rootLeash.setUnreleasedWarningCallSite("Transition.calculateTransitionRoots");
                    assignLayers(dc, startT);
                    startT.setLayer(rootLeash, parent.getLastLayer());
                    mTransitionStaticExt.setWindowCropForTransitionIfNeed(startT, rootLeash, parent);
                    outInfo.addRootLeash(endDisplayId, rootLeash, ancestor.getBounds().left, ancestor.getBounds().top);
                }
            }
        }
    }

    static android.window.TransitionInfo calculateTransitionInfo(int type, int flags, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> sortedTargets, android.view.SurfaceControl.Transaction startT) {
        int endRotation;
        com.android.server.wm.TaskFragment organizedTf;
        com.android.server.wm.Task parentTask;
        int backgroundColor;
        java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> arrayList = sortedTargets;
        android.window.TransitionInfo out = new android.window.TransitionInfo(type, flags);
        calculateTransitionRoots(out, arrayList, startT);
        if (out.getRootCount() == 0) {
            return out;
        }
        android.window.TransitionInfo.AnimationOptions animOptionsForActivityTransition = calculateAnimationOptionsForActivityTransition(type, arrayList);
        if (!com.android.window.flags.Flags.moveAnimationOptionsToChange() && animOptionsForActivityTransition != null) {
            out.setAnimationOptions(animOptionsForActivityTransition);
        }
        android.util.ArraySet<com.android.server.wm.WindowContainer> occludedAtEndContainers = new android.util.ArraySet<>();
        int count = sortedTargets.size();
        int i = 0;
        while (i < count) {
            com.android.server.wm.Transition.ChangeInfo info = arrayList.get(i);
            com.android.server.wm.WindowContainer target = info.mContainer;
            android.window.TransitionInfo.Change change = new android.window.TransitionInfo.Change(target.mRemoteToken != null ? target.mRemoteToken.toWindowContainerToken() : null, getLeashSurface(target, startT));
            if (info.mEndParent != null) {
                change.setParent(info.mEndParent.mRemoteToken.toWindowContainerToken());
            }
            if (info.mStartParent != null && info.mStartParent.mRemoteToken != null && target.getParent() != info.mStartParent) {
                change.setLastParent(info.mStartParent.mRemoteToken.toWindowContainerToken());
            }
            mTransitionStaticExt.adjustChangeAndResetTaskBarAnimStatus(info.mContainer, change);
            change.setMode(info.getTransitMode(target));
            info.mReadyMode = change.getMode();
            change.setStartAbsBounds(info.mAbsoluteBounds);
            change.setFlags(info.getChangeFlags(target));
            info.mReadyFlags = change.getFlags();
            change.setDisplayId(info.mDisplayId, getDisplayId(target));
            if (change.getMode() == 3 || change.getMode() == 1) {
                int occIndex = occludedAtEndContainers.size() - 1;
                while (true) {
                    if (occIndex < 0) {
                        break;
                    }
                    if (!target.isDescendantOf(occludedAtEndContainers.valueAt(occIndex))) {
                        occIndex--;
                    } else {
                        change.setFlags(change.getFlags() | 32768);
                        break;
                    }
                }
            }
            if (!change.hasFlags(4) && (change.getMode() == 1 || change.getMode() == 3 || change.getMode() == 6)) {
                occludedAtEndContainers.add(target.getParent());
            }
            com.android.server.wm.Task task = target.asTask();
            com.android.server.wm.TaskFragment taskFragment = target.asTaskFragment();
            boolean isEmbeddedTaskFragment = taskFragment != null && taskFragment.isEmbedded();
            com.android.server.wm.ActivityRecord activityRecord = target.asActivityRecord();
            if (task != null) {
                android.app.ActivityManager.RunningTaskInfo tinfo = new android.app.ActivityManager.RunningTaskInfo();
                task.fillTaskInfo(tinfo);
                change.setTaskInfo(tinfo);
                change.setRotationAnimation(getTaskRotationAnimation(mTransitionStaticExt.adjustChangeRotationAnimation(task)));
                com.android.server.wm.ActivityRecord topRunningActivity = task.topRunningActivity();
                if (topRunningActivity != null) {
                    if (topRunningActivity.info.supportsPictureInPicture()) {
                        change.setAllowEnterPip(topRunningActivity.checkEnterPictureInPictureAppOpsState());
                    }
                    setEndFixedRotationIfNeeded(change, task, topRunningActivity);
                }
            } else if ((info.mFlags & 1) != 0) {
                change.setRotationAnimation(3);
            }
            com.android.server.wm.WindowContainer<?> parent = target.getParent();
            android.graphics.Rect bounds = target.getBounds();
            android.graphics.Rect parentBounds = parent.getBounds();
            change.setEndRelOffset(bounds.left - parentBounds.left, bounds.top - parentBounds.top);
            int endRotation2 = target.getWindowConfiguration().getRotation();
            if (activityRecord != null) {
                change.setEndAbsBounds(parentBounds);
                if (activityRecord.getRelativeDisplayRotation() != 0 && !activityRecord.mTransitionController.useShellTransitionsRotation()) {
                    int endRotation3 = parent.getWindowConfiguration().getRotation();
                    endRotation = endRotation3;
                } else {
                    endRotation = endRotation2;
                }
            } else if (isWallpaper(target) && com.android.window.flags.Flags.ensureWallpaperInTransitions() && target.getRelativeDisplayRotation() != 0 && !target.mTransitionController.useShellTransitionsRotation()) {
                change.setEndAbsBounds(parent.getBounds());
                int endRotation4 = parent.getWindowConfiguration().getRotation();
                endRotation = endRotation4;
            } else {
                change.setEndAbsBounds(bounds);
                endRotation = endRotation2;
            }
            if (activityRecord != null || isEmbeddedTaskFragment) {
                if (activityRecord != null) {
                    organizedTf = activityRecord.getOrganizedTaskFragment();
                } else {
                    organizedTf = taskFragment.getOrganizedTaskFragment();
                }
                if (organizedTf != null && organizedTf.getAnimationParams().getAnimationBackgroundColor() != 0) {
                    backgroundColor = organizedTf.getAnimationParams().getAnimationBackgroundColor();
                } else {
                    if (activityRecord != null) {
                        parentTask = activityRecord.getTask();
                    } else {
                        parentTask = taskFragment.getTask();
                    }
                    backgroundColor = parentTask.getTaskDescription().getBackgroundColor();
                }
                change.setBackgroundColor(com.android.internal.graphics.ColorUtils.setAlphaComponent(backgroundColor, 255));
            }
            android.window.TransitionInfo.AnimationOptions animOptions = null;
            if (com.android.window.flags.Flags.moveAnimationOptionsToChange()) {
                if (activityRecord != null && animOptionsForActivityTransition != null) {
                    animOptions = animOptionsForActivityTransition;
                } else if (com.android.window.flags.Flags.activityEmbeddingOverlayPresentationFlag() && isEmbeddedTaskFragment) {
                    android.window.TaskFragmentAnimationParams params = taskFragment.getAnimationParams();
                    if (params.hasOverrideAnimation()) {
                        animOptions = android.window.TransitionInfo.AnimationOptions.makeCustomAnimOptions(taskFragment.getTask().getBasePackageName(), params.getOpenAnimationResId(), params.getChangeAnimationResId(), params.getCloseAnimationResId(), 0, false);
                    }
                }
                if (animOptions != null) {
                    change.setAnimationOptions(animOptions);
                }
            }
            if (activityRecord != null) {
                change.setActivityComponent(activityRecord.mActivityComponent);
            }
            change.setRotation(info.mRotation, endRotation);
            if (info.mSnapshot != null) {
                change.setSnapshot(info.mSnapshot, info.mSnapshotLuma);
            }
            if (!mTransitionStaticExt.skipCurrentOrAdjustChange(out, type, change, info, sortedTargets, target)) {
                out.addChange(change);
            }
            i++;
            arrayList = sortedTargets;
        }
        return out;
    }

    private static android.window.TransitionInfo.AnimationOptions calculateAnimationOptionsForActivityTransition(int type, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> sortedTargets) {
        android.window.TransitionInfo.AnimationOptions animOptions = null;
        com.android.server.wm.WindowContainer<?> topApp = null;
        int i = 0;
        while (true) {
            if (i < sortedTargets.size()) {
                if (isWallpaper(sortedTargets.get(i).mContainer)) {
                    i++;
                } else {
                    topApp = sortedTargets.get(i).mContainer;
                    break;
                }
            } else {
                break;
            }
        }
        if (topApp.asActivityRecord() != null) {
            com.android.server.wm.ActivityRecord topActivity = topApp.asActivityRecord();
            android.window.TransitionInfo.AnimationOptions animOptions2 = addCustomActivityTransition(topActivity, true, null);
            animOptions = addCustomActivityTransition(topActivity, false, animOptions2);
        }
        android.view.WindowManager.LayoutParams animLp = getLayoutParamsForAnimationsStyle(type, sortedTargets);
        if (animLp != null && animLp.type != 3 && animLp.windowAnimations != 0) {
            if (animOptions != null) {
                animOptions.addOptionsFromLayoutParameters(animLp);
                return animOptions;
            }
            android.window.TransitionInfo.AnimationOptions animOptions3 = android.window.TransitionInfo.AnimationOptions.makeAnimOptionsFromLayoutParameters(animLp);
            return animOptions3;
        }
        return animOptions;
    }

    private static android.window.TransitionInfo.AnimationOptions addCustomActivityTransition(com.android.server.wm.ActivityRecord activity, boolean open, android.window.TransitionInfo.AnimationOptions animOptions) {
        com.android.server.wm.ActivityRecord.CustomAppTransition customAnim = activity.getCustomAnimation(open);
        if (customAnim != null) {
            if (animOptions == null) {
                animOptions = android.window.TransitionInfo.AnimationOptions.makeCommonAnimOptions(activity.packageName);
            }
            animOptions.addCustomActivityTransition(open, customAnim.mEnterAnim, customAnim.mExitAnim, customAnim.mBackgroundColor);
        }
        return animOptions;
    }

    private static void setEndFixedRotationIfNeeded(android.window.TransitionInfo.Change change, com.android.server.wm.Task task, com.android.server.wm.ActivityRecord taskTopRunning) {
        com.android.server.wm.WindowContainer<?> orientationSource;
        int nextRotation;
        if (!taskTopRunning.isVisibleRequested()) {
            return;
        }
        if (task.inMultiWindowMode() && taskTopRunning.inMultiWindowMode()) {
            return;
        }
        int taskRotation = task.getWindowConfiguration().getDisplayRotation();
        int activityRotation = taskTopRunning.getWindowConfiguration().getDisplayRotation();
        if (taskRotation != activityRotation) {
            change.setEndFixedRotation(activityRotation);
        } else if (task.inPinnedWindowingMode() && !taskTopRunning.mDisplayContent.inTransition() && (orientationSource = taskTopRunning.mDisplayContent.getLastOrientationSource()) != null && taskRotation != (nextRotation = orientationSource.getWindowConfiguration().getDisplayRotation())) {
            change.setEndFixedRotation(nextRotation);
        }
    }

    private static com.android.server.wm.WindowContainer<?> findCommonAncestor(java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targets, com.android.server.wm.WindowContainer<?> topApp) {
        int transitionMode;
        int displayId = getDisplayId(topApp);
        com.android.server.wm.WindowContainer<?> ancestor = topApp.getParent();
        for (int i = targets.size() - 1; i >= 0; i--) {
            com.android.server.wm.Transition.ChangeInfo change = targets.get(i);
            com.android.server.wm.WindowContainer wc = change.mContainer;
            if (!isWallpaper(wc) && getDisplayId(wc) == displayId) {
                if (change.mStartParent != null && wc.getParent() != null && change.mStartParent.isAttached() && wc.getParent() != change.mStartParent && i == targets.size() - 1 && ((transitionMode = change.getTransitMode(wc)) == 2 || transitionMode == 4)) {
                    ancestor = change.mStartParent;
                } else {
                    while (!wc.isDescendantOf(ancestor)) {
                        ancestor = ancestor.getParent();
                    }
                    com.android.server.wm.WindowContainer<?> windowContainer = change.mCommonAncestor;
                    if (windowContainer != null && windowContainer.isAttached()) {
                        while (windowContainer != ancestor && !windowContainer.isDescendantOf(ancestor)) {
                            ancestor = ancestor.getParent();
                        }
                    }
                }
            }
        }
        return ancestor;
    }

    private static android.view.WindowManager.LayoutParams getLayoutParamsForAnimationsStyle(int type, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> sortedTargets) {
        android.util.ArraySet<java.lang.Integer> activityTypes = new android.util.ArraySet<>();
        int targetCount = sortedTargets.size();
        for (int i = 0; i < targetCount; i++) {
            com.android.server.wm.WindowContainer target = sortedTargets.get(i).mContainer;
            if (target.asActivityRecord() != null) {
                activityTypes.add(java.lang.Integer.valueOf(target.getActivityType()));
            } else if (target.asWindowToken() == null && target.asWindowState() == null) {
                return null;
            }
        }
        if (activityTypes.isEmpty()) {
            return null;
        }
        com.android.server.wm.ActivityRecord animLpActivity = findAnimLayoutParamsActivityRecord(sortedTargets, type, activityTypes);
        com.android.server.wm.WindowState mainWindow = animLpActivity != null ? animLpActivity.findMainWindow() : null;
        if (mainWindow != null) {
            return mainWindow.mAttrs;
        }
        return null;
    }

    private static com.android.server.wm.ActivityRecord findAnimLayoutParamsActivityRecord(java.util.List<com.android.server.wm.Transition.ChangeInfo> sortedTargets, final int transit, final android.util.ArraySet<java.lang.Integer> activityTypes) {
        com.android.server.wm.ActivityRecord result = lookForTopWindowWithFilter(sortedTargets, new java.util.function.Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Transition.lambda$findAnimLayoutParamsActivityRecord$10(transit, activityTypes, (com.android.server.wm.ActivityRecord) obj);
            }
        });
        if (result != null) {
            return result;
        }
        com.android.server.wm.ActivityRecord result2 = lookForTopWindowWithFilter(sortedTargets, new java.util.function.Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Transition.lambda$findAnimLayoutParamsActivityRecord$11((com.android.server.wm.ActivityRecord) obj);
            }
        });
        if (result2 != null) {
            return result2;
        }
        return lookForTopWindowWithFilter(sortedTargets, new java.util.function.Predicate() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Transition.lambda$findAnimLayoutParamsActivityRecord$12((com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$findAnimLayoutParamsActivityRecord$10(int transit, android.util.ArraySet activityTypes, com.android.server.wm.ActivityRecord w) {
        return w.getRemoteAnimationDefinition() != null && w.getRemoteAnimationDefinition().hasTransition(transit, activityTypes);
    }

    static /* synthetic */ boolean lambda$findAnimLayoutParamsActivityRecord$11(com.android.server.wm.ActivityRecord w) {
        return w.fillsParent() && w.findMainWindow() != null;
    }

    static /* synthetic */ boolean lambda$findAnimLayoutParamsActivityRecord$12(com.android.server.wm.ActivityRecord w) {
        return w.findMainWindow() != null;
    }

    private static com.android.server.wm.ActivityRecord lookForTopWindowWithFilter(java.util.List<com.android.server.wm.Transition.ChangeInfo> sortedTargets, java.util.function.Predicate<com.android.server.wm.ActivityRecord> filter) {
        com.android.server.wm.ActivityRecord activityRecord;
        int count = sortedTargets.size();
        for (int i = 0; i < count; i++) {
            com.android.server.wm.WindowContainer target = sortedTargets.get(i).mContainer;
            if (target.asTaskFragment() != null) {
                activityRecord = target.asTaskFragment().getTopNonFinishingActivity();
            } else {
                activityRecord = target.asActivityRecord();
            }
            if (activityRecord != null && filter.test(activityRecord)) {
                return activityRecord;
            }
        }
        return null;
    }

    private static int getTaskRotationAnimation(com.android.server.wm.Task task) {
        com.android.server.wm.WindowState mainWin;
        com.android.server.wm.ActivityRecord top = task.getTopVisibleActivity();
        if (top == null || (mainWin = top.findMainWindow(false)) == null) {
            return -1;
        }
        int anim = mainWin.getRotationAnimationHint();
        if (anim >= 0) {
            return anim;
        }
        int anim2 = mainWin.getAttrs().rotationAnimation;
        if (anim2 != 3) {
            return anim2;
        }
        if (mainWin != task.mDisplayContent.getDisplayPolicy().getTopFullscreenOpaqueWindow() || !top.matchParentBounds()) {
            return -1;
        }
        return mainWin.getAttrs().rotationAnimation;
    }

    private void validateKeyguardOcclusion() {
        if ((this.mFlags & 14592) != 0) {
            java.util.ArrayList<java.lang.Runnable> arrayList = this.mController.mStateValidators;
            com.android.server.policy.WindowManagerPolicy windowManagerPolicy = this.mController.mAtm.mWindowManager.mPolicy;
            java.util.Objects.requireNonNull(windowManagerPolicy);
            arrayList.add(new com.android.server.wm.KeyguardController$$ExternalSyntheticLambda0(windowManagerPolicy));
        }
    }

    boolean shouldUsePerfHint(com.android.server.wm.DisplayContent dc) {
        if (this.mOverrideOptions != null && this.mOverrideOptions.getType() == 5 && this.mType == 4 && this.mParticipants.size() == 1) {
            return false;
        }
        return this.mTargetDisplays.contains(dc);
    }

    boolean shouldApplyOnDisplayThread() {
        com.android.server.wm.Transition.ChangeInfo changeInfo;
        for (int i = this.mParticipants.size() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent dc = this.mParticipants.valueAt(i).asDisplayContent();
            if (dc != null && (changeInfo = this.mChanges.get(dc)) != null && changeInfo.mRotation != dc.getRotation()) {
                return android.os.Looper.myLooper() != this.mController.mAtm.mWindowManager.mH.getLooper();
            }
        }
        return false;
    }

    void applyDisplayChangeIfNeeded(final android.util.ArraySet<com.android.server.wm.WindowContainer<?>> activitiesMayChange) {
        for (int i = this.mParticipants.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer<?> wc = this.mParticipants.valueAt(i);
            com.android.server.wm.DisplayContent dc = wc.asDisplayContent();
            if (dc != null && this.mChanges.get(dc).hasChanged() && !this.mTransitionExt.ignoreConfigChangedIfFixRotation(this, dc, this.mType)) {
                boolean changed = dc.sendNewConfiguration();
                if (!this.mReadyTrackerOld.mUsed) {
                    setReady(dc, true);
                    this.mTransitionExt.setAllReadyIfNeeded(this, dc);
                }
                if (changed && this.mController.mAtm.mTaskSupervisor.isRootVisibilityUpdateDeferred()) {
                    dc.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Transition$$ExternalSyntheticLambda9
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            com.android.server.wm.Transition.lambda$applyDisplayChangeIfNeeded$13(activitiesMayChange, (com.android.server.wm.ActivityRecord) obj);
                        }
                    });
                }
            }
        }
    }

    static /* synthetic */ void lambda$applyDisplayChangeIfNeeded$13(android.util.ArraySet activitiesMayChange, com.android.server.wm.ActivityRecord r) {
        if (r.isVisibleRequested()) {
            activitiesMayChange.add(r);
        }
    }

    boolean getLegacyIsReady() {
        return isCollecting() && this.mSyncId >= 0;
    }

    static void asyncTraceBegin(java.lang.String name, int cookie) {
        android.os.Trace.asyncTraceForTrackBegin(32L, TAG, name, cookie);
    }

    static void asyncTraceEnd(int cookie) {
        android.os.Trace.asyncTraceForTrackEnd(32L, TAG, cookie);
    }

    static class ChangeInfo {
        private static final int FLAG_ABOVE_TRANSIENT_LAUNCH = 4;
        private static final int FLAG_CHANGE_CONFIG_AT_END = 64;
        private static final int FLAG_CHANGE_MOVED_TO_TOP = 32;
        private static final int FLAG_CHANGE_NO_ANIMATION = 8;
        private static final int FLAG_CHANGE_YES_ANIMATION = 16;
        private static final int FLAG_NONE = 0;
        private static final int FLAG_SEAMLESS_ROTATION = 1;
        private static final int FLAG_TRANSIENT_LAUNCH = 2;
        final android.graphics.Rect mAbsoluteBounds;
        com.android.server.wm.WindowContainer mCommonAncestor;
        final com.android.server.wm.WindowContainer mContainer;
        int mDisplayId;
        com.android.server.wm.WindowContainer mEndParent;
        boolean mExistenceChanged;
        int mFlags;
        int mKnownConfigChanges;
        int mReadyFlags;
        int mReadyMode;
        int mRotation;
        boolean mShowWallpaper;
        android.view.SurfaceControl mSnapshot;
        float mSnapshotLuma;
        com.android.server.wm.WindowContainer mStartParent;
        boolean mVisible;
        int mWindowingMode;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @interface Flag {
        }

        ChangeInfo(com.android.server.wm.WindowContainer origState) {
            this.mExistenceChanged = false;
            this.mAbsoluteBounds = new android.graphics.Rect();
            this.mRotation = -1;
            this.mDisplayId = -1;
            this.mFlags = 0;
            this.mContainer = origState;
            this.mVisible = origState.isVisibleRequested();
            this.mWindowingMode = origState.getWindowingMode();
            this.mAbsoluteBounds.set(origState.getBounds());
            this.mShowWallpaper = origState.showWallpaper();
            this.mRotation = origState.getWindowConfiguration().getRotation();
            this.mStartParent = origState.getParent();
            this.mDisplayId = com.android.server.wm.Transition.getDisplayId(origState);
        }

        ChangeInfo(com.android.server.wm.WindowContainer container, boolean visible, boolean existChange) {
            this(container);
            this.mVisible = visible;
            this.mExistenceChanged = existChange;
            this.mShowWallpaper = false;
        }

        public java.lang.String toString() {
            return this.mContainer.toString();
        }

        boolean hasChanged() {
            if ((this.mFlags & 2) != 0 || (this.mFlags & 4) != 0) {
                return true;
            }
            boolean currVisible = this.mContainer.isVisibleRequested();
            if (currVisible == this.mVisible && !this.mVisible) {
                return false;
            }
            if (currVisible == this.mVisible && this.mKnownConfigChanges == 0) {
                return ((this.mWindowingMode == 0 || this.mContainer.getWindowingMode() == this.mWindowingMode) && this.mContainer.getBounds().equals(this.mAbsoluteBounds) && !com.android.server.wm.Transition.mTransitionStaticExt.hasChanged(this.mContainer, this.mAbsoluteBounds) && this.mRotation == this.mContainer.getWindowConfiguration().getRotation() && this.mDisplayId == com.android.server.wm.Transition.getDisplayId(this.mContainer) && (this.mFlags & 32) == 0) ? false : true;
            }
            return true;
        }

        int getTransitMode(com.android.server.wm.WindowContainer wc) {
            if ((this.mFlags & 4) != 0) {
                return this.mExistenceChanged ? 2 : 4;
            }
            boolean nowVisible = wc.isVisibleRequested();
            if (nowVisible == this.mVisible) {
                return 6;
            }
            return this.mExistenceChanged ? nowVisible ? 1 : 2 : nowVisible ? 3 : 4;
        }

        int getChangeFlags(com.android.server.wm.WindowContainer wc) {
            int flags = 0;
            if (this.mShowWallpaper || wc.showWallpaper()) {
                flags = 0 | 1;
            }
            if (com.android.server.wm.Transition.isTranslucent(wc)) {
                flags |= 4;
            }
            if (wc.mWmService.mAtmService.mBackNavigationController.isMonitorTransitionTarget(wc)) {
                flags |= 131072;
            }
            com.android.server.wm.Task task = wc.asTask();
            if (task != null) {
                com.android.server.wm.ActivityRecord topActivity = task.getTopNonFinishingActivity();
                if (topActivity != null) {
                    if (topActivity.mStartingData != null && topActivity.mStartingData.hasImeSurface()) {
                        flags |= 2048;
                    }
                    if (topActivity.mLaunchTaskBehind) {
                        android.util.Slog.e(com.android.server.wm.Transition.TAG, "Unexpected launch-task-behind operation in shell transition");
                        flags |= 524288;
                    }
                    if ((topActivity.mTransitionChangeFlags & 294912) == 294912) {
                        flags |= 294912;
                    }
                }
                if (task.voiceSession != null) {
                    flags |= 16;
                }
            }
            com.android.server.wm.Task parentTask = null;
            com.android.server.wm.ActivityRecord record = wc.asActivityRecord();
            if (record != null) {
                parentTask = record.getTask();
                if (record.mVoiceInteraction) {
                    flags |= 16;
                }
                flags |= record.mTransitionChangeFlags;
                if (record.isConfigurationDispatchPaused()) {
                    flags |= 4194304;
                }
            }
            com.android.server.wm.TaskFragment taskFragment = wc.asTaskFragment();
            if (taskFragment != null && task == null) {
                parentTask = taskFragment.getTask();
            }
            if (parentTask != null) {
                if (parentTask.forAllLeafTaskFragments(new com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda6())) {
                    flags |= 512;
                }
                com.android.server.wm.ActivityRecord starting = parentTask.topActivityContainsStartingWindow();
                if (starting != null) {
                    if (starting == record || (starting.mStartingData != null && starting.mStartingData.mAssociatedTask != null)) {
                        flags |= 16384;
                    } else if (record != null && parentTask.mChildren.indexOf(record) < parentTask.mChildren.indexOf(starting)) {
                        flags |= 16384;
                    }
                }
                if (isWindowFillingTask(wc, parentTask)) {
                    flags |= 1024;
                }
            } else {
                com.android.server.wm.DisplayContent dc = wc.asDisplayContent();
                if (dc != null) {
                    flags |= 32;
                    if (dc.hasAlertWindowSurfaces() && !com.android.server.wm.Transition.mTransitionStaticExt.forceSeamlesslyRotated(dc, "System Alert windows")) {
                        flags |= 128;
                    }
                } else if (com.android.server.wm.Transition.isWallpaper(wc)) {
                    flags |= 2;
                } else if (com.android.server.wm.Transition.isInputMethod(wc)) {
                    flags |= 256;
                } else {
                    int type = wc.getWindowType();
                    if (type >= 2000 && type <= 2999) {
                        flags |= 65536;
                    }
                }
            }
            if ((this.mFlags & 8) != 0 && (this.mFlags & 16) == 0) {
                flags |= 262144;
            }
            if ((this.mFlags & 32) != 0) {
                flags |= 1048576;
            }
            if ((this.mFlags & 64) != 0) {
                return flags | 4194304;
            }
            return flags;
        }

        private boolean isWindowFillingTask(com.android.server.wm.WindowContainer wc, com.android.server.wm.Task parentTask) {
            android.graphics.Rect taskBounds = parentTask.getBounds();
            int taskWidth = taskBounds.width();
            int taskHeight = taskBounds.height();
            android.graphics.Rect startBounds = this.mAbsoluteBounds;
            android.graphics.Rect endBounds = wc.getBounds();
            boolean isInvisibleOrFillingTaskBeforeTransition = !this.mVisible || (taskWidth == startBounds.width() && taskHeight == startBounds.height());
            boolean isInVisibleOrFillingTaskAfterTransition = !wc.isVisibleRequested() || (taskWidth == endBounds.width() && taskHeight == endBounds.height());
            return isInvisibleOrFillingTaskBeforeTransition && isInVisibleOrFillingTaskAfterTransition;
        }
    }

    void deferTransitionReady() {
        this.mReadyTrackerOld.mDeferReadyDepth++;
        if (isFinished() || this.mSyncId < 0) {
            return;
        }
        this.mSyncEngine.setReady(this.mSyncId, false);
    }

    void continueTransitionReady() {
        com.android.server.wm.Transition.ReadyTrackerOld readyTrackerOld = this.mReadyTrackerOld;
        readyTrackerOld.mDeferReadyDepth--;
        if (isFinished() || this.mSyncId < 0 || isPlaying() || this.mSyncEngine.getSyncSet(this.mSyncId) == null) {
            android.util.Slog.e(TAG, " continueTransitionReady  " + this + ",callback=" + android.os.Debug.getCallers(5));
        } else {
            applyReady();
        }
    }

    public com.android.server.wm.ITransitionWrapper getWrapper() {
        return this.mTransitionWrapper;
    }

    private class TransitionWrapper implements com.android.server.wm.ITransitionWrapper {
        private TransitionWrapper() {
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public com.android.server.wm.ITransitionExt getExtImpl() {
            return com.android.server.wm.Transition.this.mTransitionExt;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public boolean isWcTranslucent(com.android.server.wm.WindowContainer wc) {
            return com.android.server.wm.Transition.isTranslucent(wc);
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public android.util.ArrayMap<com.android.server.wm.ActivityRecord, com.android.server.wm.Task> getTransientLaunches() {
            return com.android.server.wm.Transition.this.mTransientLaunches;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public void initTransientLaunches(android.util.ArrayMap<com.android.server.wm.ActivityRecord, com.android.server.wm.Task> transientLaunches) {
            com.android.server.wm.Transition.this.mTransientLaunches = transientLaunches;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public java.util.ArrayList<com.android.server.wm.Task> getTransientHideTasks() {
            return com.android.server.wm.Transition.this.mTransientHideTasks;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public android.util.ArraySet<com.android.server.wm.WindowToken> getVisibleAtTransitionEndTokens() {
            return com.android.server.wm.Transition.this.mVisibleAtTransitionEndTokens;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public void initTransientHideTasks(java.util.ArrayList<com.android.server.wm.Task> transientHideTasks) {
            com.android.server.wm.Transition.this.mTransientHideTasks = transientHideTasks;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public java.util.ArrayList<com.android.server.wm.DisplayContent> getTargetDisplays() {
            return com.android.server.wm.Transition.this.mTargetDisplays;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public int getRecentsDisplayId() {
            return com.android.server.wm.Transition.this.mRecentsDisplayId;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public boolean getIsSeamlessRotation() {
            return com.android.server.wm.Transition.this.mIsSeamlessRotation;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public com.android.server.wm.TransitionController getTransitionController() {
            return com.android.server.wm.Transition.this.mController;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public android.view.SurfaceControl.Transaction getCleanupTransaction() {
            return com.android.server.wm.Transition.this.mCleanupTransaction;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public android.view.SurfaceControl.Transaction getInputSinkTransaction() {
            return com.android.server.wm.Transition.this.mInputSinkTransaction;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public void setInputSinkTransaction(android.view.SurfaceControl.Transaction t) {
            com.android.server.wm.Transition.this.mInputSinkTransaction = t;
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public void closeStartTransition() {
            if (com.android.server.wm.Transition.this.mStartTransaction != null) {
                com.android.server.wm.Transition.this.mStartTransaction.close();
                com.android.server.wm.Transition.this.mStartTransaction = null;
            }
        }

        @Override // com.android.server.wm.ITransitionWrapper
        public void closeFinishTransition() {
            if (com.android.server.wm.Transition.this.mFinishTransaction != null) {
                com.android.server.wm.Transition.this.mFinishTransaction.close();
                com.android.server.wm.Transition.this.mFinishTransaction = null;
            }
        }
    }

    @Override // com.android.server.wm.BLASTSyncEngine.TransactionReadyListener
    public void onReadyTimeout() {
        if (!this.mController.useFullReadyTracking()) {
            android.util.Slog.e(TAG, "#" + this.mSyncId + " readiness timeout, used=" + this.mReadyTrackerOld.mUsed + " deferReadyDepth=" + this.mReadyTrackerOld.mDeferReadyDepth + " group=" + this.mReadyTrackerOld.mReadyGroups);
        } else {
            android.util.Slog.e(TAG, "#" + this.mSyncId + " met conditions: " + this.mReadyTracker.mMet);
            android.util.Slog.e(TAG, "#" + this.mSyncId + " unmet conditions: " + this.mReadyTracker.mConditions);
        }
    }

    static class ReadyCondition {
        java.lang.String mAlternate;
        final java.lang.Object mDebugTarget;
        boolean mMet;
        final java.lang.String mName;
        com.android.server.wm.Transition.ReadyTracker mTracker;

        ReadyCondition(java.lang.String name) {
            this.mMet = false;
            this.mAlternate = null;
            this.mName = name;
            this.mDebugTarget = null;
        }

        ReadyCondition(java.lang.String name, java.lang.Object debugTarget) {
            this.mMet = false;
            this.mAlternate = null;
            this.mName = name;
            this.mDebugTarget = debugTarget;
        }

        protected java.lang.String getDebugRep() {
            if (this.mDebugTarget != null) {
                return this.mName + ":" + this.mDebugTarget;
            }
            return this.mName;
        }

        public java.lang.String toString() {
            return "{" + getDebugRep() + (this.mAlternate != null ? " (" + this.mAlternate + ")" : "") + "}";
        }

        void startTracking() {
        }

        void meetAlternate(java.lang.String reason) {
            if (this.mMet) {
                return;
            }
            this.mAlternate = reason;
            meet();
        }

        void meet() {
            if (this.mMet) {
                return;
            }
            if (this.mTracker == null) {
                throw new java.lang.IllegalStateException("Can't meet a condition before it is waited on");
            }
            this.mTracker.meet(this);
        }
    }

    static class ReadyTracker {
        static final com.android.server.wm.Transition.ReadyTracker NULL_TRACKER = new com.android.server.wm.Transition.ReadyTracker(null);
        final java.util.ArrayList<com.android.server.wm.Transition.ReadyCondition> mConditions = new java.util.ArrayList<>();
        final java.util.ArrayList<com.android.server.wm.Transition.ReadyCondition> mMet = new java.util.ArrayList<>();
        private final com.android.server.wm.Transition mTransition;

        ReadyTracker(com.android.server.wm.Transition transition) {
            this.mTransition = transition;
        }

        void add(com.android.server.wm.Transition.ReadyCondition condition) {
            if (this.mTransition == null || !this.mTransition.mController.useFullReadyTracking()) {
                condition.mTracker = NULL_TRACKER;
                return;
            }
            this.mConditions.add(condition);
            condition.mTracker = this;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(condition);
                long protoLogParam1 = this.mTransition.mSyncId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -2971560715211489406L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
            }
            condition.startTracking();
        }

        void meet(com.android.server.wm.Transition.ReadyCondition condition) {
            if (this.mTransition == null || !this.mTransition.mController.useFullReadyTracking()) {
                return;
            }
            if (this.mTransition.mState >= 2) {
                android.util.Slog.w(com.android.server.wm.Transition.TAG, "#%d: Condition met too late, already in state=" + this.mTransition.mState + ": " + condition);
                return;
            }
            if (!this.mConditions.remove(condition)) {
                if (this.mMet.contains(condition)) {
                    throw new java.lang.IllegalStateException("Can't meet the same condition more than once: " + condition + " #" + this.mTransition.mSyncId);
                }
                throw new java.lang.IllegalArgumentException("Can't meet a condition that isn't being waited on: " + condition + " in #" + this.mTransition.mSyncId);
            }
            condition.mMet = true;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(condition);
                long protoLogParam1 = this.mTransition.mSyncId;
                long protoLogParam2 = this.mConditions.size();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 7631061720069910622L, 20, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2));
            }
            this.mMet.add(condition);
            this.mTransition.applyReady();
        }

        boolean isReady() {
            return this.mConditions.isEmpty() && !this.mMet.isEmpty();
        }
    }

    private static class ReadyTrackerOld {
        private int mDeferReadyDepth;
        private final android.util.ArrayMap<com.android.server.wm.WindowContainer, java.lang.Boolean> mReadyGroups;
        private boolean mReadyOverride;
        private boolean mUsed;

        private ReadyTrackerOld() {
            this.mReadyGroups = new android.util.ArrayMap<>();
            this.mUsed = false;
            this.mReadyOverride = false;
            this.mDeferReadyDepth = 0;
        }

        void addGroup(com.android.server.wm.WindowContainer wc) {
            if (this.mReadyGroups.containsKey(wc)) {
                return;
            }
            this.mReadyGroups.put(wc, false);
        }

        void setReadyFrom(com.android.server.wm.WindowContainer wc, boolean ready) {
            this.mUsed = true;
            for (com.android.server.wm.WindowContainer current = wc; current != null; current = current.getParent()) {
                if (com.android.server.wm.Transition.isReadyGroup(current)) {
                    this.mReadyGroups.put(current, java.lang.Boolean.valueOf(ready));
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                        java.lang.String protoLogParam1 = java.lang.String.valueOf(current);
                        java.lang.String protoLogParam2 = java.lang.String.valueOf(wc);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -4770394322045550928L, 3, null, java.lang.Boolean.valueOf(ready), protoLogParam1, protoLogParam2);
                        return;
                    }
                    return;
                }
            }
        }

        void setAllReady() {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 6039132370452820927L, 0, null, null);
            }
            this.mUsed = true;
            this.mReadyOverride = true;
        }

        boolean allReady() {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                boolean protoLogParam0 = this.mUsed;
                boolean protoLogParam1 = this.mReadyOverride;
                long protoLogParam2 = this.mDeferReadyDepth;
                java.lang.String protoLogParam3 = java.lang.String.valueOf(groupsToString());
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -3263748870548668913L, 31, null, java.lang.Boolean.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2), protoLogParam3);
            }
            boolean protoLogParam02 = this.mUsed;
            if (!protoLogParam02 || this.mDeferReadyDepth > 0) {
                return false;
            }
            if (this.mReadyOverride) {
                return true;
            }
            for (int i = this.mReadyGroups.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowContainer wc = this.mReadyGroups.keyAt(i);
                if (wc.isAttached() && wc.isVisibleRequested() && !this.mReadyGroups.valueAt(i).booleanValue()) {
                    return false;
                }
            }
            return true;
        }

        private java.lang.String groupsToString() {
            java.lang.StringBuilder b = new java.lang.StringBuilder();
            for (int i = 0; i < this.mReadyGroups.size(); i++) {
                if (i != 0) {
                    b.append(',');
                }
                b.append(this.mReadyGroups.keyAt(i)).append(':').append(this.mReadyGroups.valueAt(i));
            }
            return b.toString();
        }
    }

    private static class Targets {
        final android.util.SparseArray<com.android.server.wm.Transition.ChangeInfo> mArray;
        private int mDepthFactor;
        private java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> mRemovedTargets;

        private Targets() {
            this.mArray = new android.util.SparseArray<>();
        }

        void add(com.android.server.wm.Transition.ChangeInfo target) {
            if (this.mDepthFactor == 0) {
                this.mDepthFactor = target.mContainer.mWmService.mRoot.getTreeWeight() + 1;
            }
            int score = target.mContainer.getPrefixOrderIndex();
            com.android.server.wm.WindowContainer<?> wc = target.mContainer;
            while (wc != null) {
                com.android.server.wm.WindowContainer<?> parent = wc.getParent();
                if (parent != null) {
                    score += this.mDepthFactor;
                }
                wc = parent;
            }
            this.mArray.put(score, target);
        }

        void remove(int index) {
            com.android.server.wm.Transition.ChangeInfo removingTarget = this.mArray.valueAt(index);
            this.mArray.removeAt(index);
            if (this.mRemovedTargets == null) {
                this.mRemovedTargets = new java.util.ArrayList<>();
            }
            this.mRemovedTargets.add(removingTarget);
        }

        boolean wasParticipated(com.android.server.wm.Transition.ChangeInfo wc) {
            return this.mArray.indexOfValue(wc) >= 0 || (this.mRemovedTargets != null && this.mRemovedTargets.contains(wc));
        }

        java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> getListSortedByZ() {
            android.util.SparseArray<com.android.server.wm.Transition.ChangeInfo> arrayByZ = new android.util.SparseArray<>(this.mArray.size());
            for (int i = this.mArray.size() - 1; i >= 0; i--) {
                int zOrder = this.mArray.keyAt(i) % this.mDepthFactor;
                arrayByZ.put(zOrder, this.mArray.valueAt(i));
            }
            java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> sortedTargets = new java.util.ArrayList<>(arrayByZ.size());
            for (int i2 = arrayByZ.size() - 1; i2 >= 0; i2--) {
                sortedTargets.add(arrayByZ.valueAt(i2));
            }
            return sortedTargets;
        }
    }

    private class ScreenshotFreezer implements com.android.server.wm.Transition.IContainerFreezer {
        private final android.util.ArraySet<com.android.server.wm.WindowContainer> mFrozen;

        private ScreenshotFreezer() {
            this.mFrozen = new android.util.ArraySet<>();
        }

        @Override // com.android.server.wm.Transition.IContainerFreezer
        public boolean freeze(com.android.server.wm.WindowContainer wc, android.graphics.Rect bounds) {
            if (!wc.isVisibleRequested()) {
                return false;
            }
            for (com.android.server.wm.WindowContainer p = wc; p != null; p = p.getParent()) {
                if (this.mFrozen.contains(p)) {
                    return false;
                }
            }
            if (com.android.server.wm.Transition.this.mIsSeamlessRotation) {
                com.android.server.wm.WindowState top = wc.getDisplayContent() == null ? null : wc.getDisplayContent().getDisplayPolicy().getTopFullscreenOpaqueWindow();
                if (top != null && (top == wc || top.isDescendantOf(wc))) {
                    this.mFrozen.add(wc);
                    return true;
                }
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(wc.toString());
                java.lang.String protoLogParam1 = java.lang.String.valueOf(bounds.toString());
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 2699903406935781477L, 0, null, protoLogParam0, protoLogParam1);
            }
            android.graphics.Rect cropBounds = new android.graphics.Rect(bounds);
            cropBounds.offsetTo(0, 0);
            boolean isDisplayRotation = wc.asDisplayContent() != null && wc.asDisplayContent().isRotationChanging();
            android.window.ScreenCapture.LayerCaptureArgs captureArgs = new android.window.ScreenCapture.LayerCaptureArgs.Builder(wc.getSurfaceControl()).setSourceCrop(cropBounds).setUid(-222L).setCaptureSecureLayers(true).setAllowProtected(true).setHintForSeamlessTransition(isDisplayRotation).build();
            android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer = android.window.ScreenCapture.captureLayers(captureArgs);
            android.hardware.HardwareBuffer buffer = screenshotBuffer == null ? null : screenshotBuffer.getHardwareBuffer();
            if (buffer == null || buffer.getWidth() <= 1 || buffer.getHeight() <= 1) {
                android.util.Slog.w(com.android.server.wm.Transition.TAG, "Failed to capture screenshot for " + wc);
                return false;
            }
            java.lang.String name = isDisplayRotation ? "RotationLayer" : "transition snapshot: " + wc;
            android.view.SurfaceControl snapshotSurface = wc.makeAnimationLeash().setName(name).setOpaque(wc.fillsParent()).setParent(wc.getSurfaceControl()).setSecure(screenshotBuffer.containsSecureLayers()).setCallsite("Transition.ScreenshotSync").setBLASTLayer().build();
            this.mFrozen.add(wc);
            com.android.server.wm.Transition.ChangeInfo changeInfo = (com.android.server.wm.Transition.ChangeInfo) java.util.Objects.requireNonNull(com.android.server.wm.Transition.this.mChanges.get(wc));
            changeInfo.mSnapshot = snapshotSurface;
            if (changeInfo.mRotation != wc.mDisplayContent.getRotation()) {
                changeInfo.mSnapshotLuma = com.android.internal.policy.TransitionAnimation.getBorderLuma(buffer, screenshotBuffer.getColorSpace(), wc.mSurfaceControl);
            }
            android.view.SurfaceControl.Transaction t = wc.mWmService.mTransactionFactory.get();
            com.android.internal.policy.TransitionAnimation.configureScreenshotLayer(t, snapshotSurface, screenshotBuffer);
            t.show(snapshotSurface);
            t.setLayer(snapshotSurface, Integer.MAX_VALUE);
            t.apply();
            t.close();
            buffer.close();
            wc.getSyncTransaction().reparent(snapshotSurface, null);
            com.android.server.wm.Transition.this.mTransitionExt.hideStartingSurfaceImmediatelyInRotateScene(wc.asDisplayContent(), isDisplayRotation);
            return true;
        }

        @Override // com.android.server.wm.Transition.IContainerFreezer
        public void cleanUp(android.view.SurfaceControl.Transaction t) {
            for (int i = 0; i < this.mFrozen.size(); i++) {
                android.view.SurfaceControl snap = ((com.android.server.wm.Transition.ChangeInfo) java.util.Objects.requireNonNull(com.android.server.wm.Transition.this.mChanges.get(this.mFrozen.valueAt(i)))).mSnapshot;
                if (snap != null) {
                    t.reparent(snap, null);
                }
            }
        }
    }

    private static class Token extends android.os.Binder {
        final java.lang.ref.WeakReference<com.android.server.wm.Transition> mTransition;

        Token(com.android.server.wm.Transition transition) {
            this.mTransition = new java.lang.ref.WeakReference<>(transition);
        }

        public java.lang.String toString() {
            return "Token{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + this.mTransition.get() + "}";
        }
    }
}
