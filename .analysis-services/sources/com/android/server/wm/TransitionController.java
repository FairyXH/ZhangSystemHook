package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TransitionController {
    private static final int CHANGE_TIMEOUT_MS = 2000;
    private static boolean DEBUG_PANIC = false;
    private static final int DEFAULT_TIMEOUT_MS = 5000;
    private static final int LEGACY_STATE_IDLE = 0;
    private static final int LEGACY_STATE_READY = 1;
    private static final int LEGACY_STATE_RUNNING = 2;
    private static final boolean SHELL_TRANSITIONS_ROTATION = android.os.SystemProperties.getBoolean("persist.wm.debug.shell_transit_rotate", false);
    static final int SYNC_METHOD;
    private static final java.lang.String TAG = "TransitionController";
    final com.android.server.wm.ActivityTaskManagerService mAtm;
    com.android.server.wm.Transition mFinishingTransition;
    final com.android.server.wm.TransitionController.RemotePlayer mRemotePlayer;
    com.android.server.wm.SnapshotController mSnapshotController;
    com.android.server.wm.BLASTSyncEngine mSyncEngine;
    com.android.server.wm.TransitionTracer mTransitionTracer;
    private final java.util.ArrayList<com.android.server.wm.TransitionController.TransitionPlayerRecord> mTransitionPlayers = new java.util.ArrayList<>();
    final com.android.server.wm.TransitionController.TransitionMetricsReporter mTransitionMetricsReporter = new com.android.server.wm.TransitionController.TransitionMetricsReporter();
    private boolean mFullReadyTracking = false;
    private final java.util.ArrayList<com.android.server.wm.WindowManagerInternal.AppTransitionListener> mLegacyListeners = new java.util.ArrayList<>();
    final java.util.ArrayList<java.lang.Runnable> mStateValidators = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.wm.ActivityRecord> mValidateCommitVis = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.wm.ActivityRecord> mValidateActivityCompat = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.wm.DisplayArea> mValidateDisplayVis = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.Transition> mPlayingTransitions = new java.util.ArrayList<>();
    int mTrackCount = 0;
    final java.util.ArrayList<com.android.server.wm.WindowState> mAnimatingExitWindows = new java.util.ArrayList<>();
    final com.android.server.wm.TransitionController.Lock mRunningLock = new com.android.server.wm.TransitionController.Lock();
    private final java.util.ArrayList<com.android.server.wm.TransitionController.QueuedTransition> mQueuedTransitions = new java.util.ArrayList<>();
    private com.android.server.wm.Transition mCollectingTransition = null;
    final java.util.ArrayList<com.android.server.wm.Transition> mWaitingTransitions = new java.util.ArrayList<>();
    final android.util.SparseArray<java.util.ArrayList<com.android.server.wm.Task>> mLatestOnTopTasksReported = new android.util.SparseArray<>();
    boolean mBuildingFinishLayers = false;
    boolean mNavigationBarAttachedToApp = false;
    private boolean mAnimatingState = false;
    final android.os.Handler mLoggerHandler = com.android.server.FgThread.getHandler();
    boolean mIsWaitingForDisplayEnabled = false;
    public com.android.server.wm.ITransitionControllerExt mExt = (com.android.server.wm.ITransitionControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITransitionControllerExt.class).base(this).create();
    private oplus.util.IOplusGcSupressionExt mOplusGcSupressionExt = (oplus.util.IOplusGcSupressionExt) system.ext.loader.core.ExtLoader.type(oplus.util.IOplusGcSupressionExt.class).create();
    private com.android.server.wm.ITransitionControllerWrapper mWrapper = new com.android.server.wm.TransitionController.TransitionControllerWrapper();

    interface OnStartCollect {
        void onCollectStarted(boolean z);
    }

    static {
        SYNC_METHOD = android.os.SystemProperties.getBoolean("persist.wm.debug.shell_transit_blast", false) ? 1 : 0;
        DEBUG_PANIC = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    }

    static class QueuedTransition {
        final com.android.server.wm.BLASTSyncEngine.SyncGroup mLegacySync;
        final com.android.server.wm.TransitionController.OnStartCollect mOnStartCollect;
        final com.android.server.wm.Transition mTransition;

        QueuedTransition(com.android.server.wm.Transition transition, com.android.server.wm.TransitionController.OnStartCollect onStartCollect) {
            this.mTransition = transition;
            this.mOnStartCollect = onStartCollect;
            this.mLegacySync = null;
        }

        QueuedTransition(com.android.server.wm.BLASTSyncEngine.SyncGroup legacySync, com.android.server.wm.TransitionController.OnStartCollect onStartCollect) {
            this.mTransition = null;
            this.mOnStartCollect = onStartCollect;
            this.mLegacySync = legacySync;
        }
    }

    TransitionController(com.android.server.wm.ActivityTaskManagerService atm) {
        this.mAtm = atm;
        this.mRemotePlayer = new com.android.server.wm.TransitionController.RemotePlayer(atm);
    }

    void setWindowManager(com.android.server.wm.WindowManagerService wms) {
        this.mSnapshotController = wms.mSnapshotController;
        this.mTransitionTracer = wms.mTransitionTracer;
        this.mIsWaitingForDisplayEnabled = !wms.mDisplayEnabled;
        registerLegacyListener(wms.mActivityManagerAppTransitionNotifier);
        setSyncEngine(wms.mSyncEngine);
        this.mFullReadyTracking = com.android.window.flags.Flags.transitReadyTracking();
    }

    void setSyncEngine(com.android.server.wm.BLASTSyncEngine syncEngine) {
        this.mSyncEngine = syncEngine;
        this.mSyncEngine.addOnIdleListener(new java.lang.Runnable() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.tryStartCollectFromQueue();
            }
        });
    }

    void flushRunningTransitions() {
        java.util.ArrayList<com.android.server.wm.TransitionController.TransitionPlayerRecord> temp = new java.util.ArrayList<>(this.mTransitionPlayers);
        this.mTransitionPlayers.clear();
        for (int i = this.mPlayingTransitions.size() - 1; i >= 0; i--) {
            this.mPlayingTransitions.get(i).cleanUpOnFailure();
        }
        this.mPlayingTransitions.clear();
        for (int i2 = this.mWaitingTransitions.size() - 1; i2 >= 0; i2--) {
            this.mWaitingTransitions.get(i2).abort();
        }
        this.mWaitingTransitions.clear();
        if (this.mCollectingTransition != null) {
            this.mCollectingTransition.abort();
        }
        this.mRemotePlayer.clear();
        this.mRunningLock.doNotifyLocked();
        this.mTransitionPlayers.addAll(temp);
    }

    com.android.server.wm.Transition createTransition(int type) {
        return createTransition(type, 0);
    }

    com.android.server.wm.Transition createTransition(int type, int flags) {
        if (this.mTransitionPlayers.isEmpty()) {
            throw new java.lang.IllegalStateException("Shell Transitions not enabled");
        }
        if (this.mCollectingTransition != null) {
            throw new java.lang.IllegalStateException("Trying to directly start transition collection while  collection is already ongoing. Use {@link #startCollectOrQueue} if possible.");
        }
        com.android.server.wm.Transition transit = new com.android.server.wm.Transition(type, flags, this, this.mSyncEngine);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(transit);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -233096875591058130L, 0, null, protoLogParam0);
        }
        this.mExt.hookCreateTransition(transit);
        moveToCollecting(transit);
        return transit;
    }

    void moveToCollecting(com.android.server.wm.Transition transition) {
        if (this.mCollectingTransition != null) {
            throw new java.lang.IllegalStateException("Simultaneous transition collection not supported.");
        }
        if (this.mTransitionPlayers.isEmpty()) {
            transition.abort();
            return;
        }
        this.mCollectingTransition = transition;
        long timeoutMs = transition.mType == 6 ? 2000L : 5000L;
        this.mCollectingTransition.startCollecting(timeoutMs);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mCollectingTransition);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 2154694726162725342L, 0, null, protoLogParam0);
        }
        dispatchLegacyAppTransitionPending();
    }

    void registerTransitionPlayer(android.window.ITransitionPlayer player, com.android.server.wm.WindowProcessController playerProc) {
        if (!this.mTransitionPlayers.isEmpty()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(player.asBinder());
                long protoLogParam1 = this.mTransitionPlayers.size();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -4546322749928357965L, 4, "Registering transition player %s over %d other players", protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
            }
            flushRunningTransitions();
        } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(player.asBinder());
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -4250307779892136611L, 0, "Registering transition player %s ", protoLogParam02);
        }
        this.mTransitionPlayers.add(new com.android.server.wm.TransitionController.TransitionPlayerRecord(player, playerProc));
        this.mExt.initFoldScreenBlackCoverStrategy();
    }

    void unregisterTransitionPlayer(android.window.ITransitionPlayer player) {
        int idx = this.mTransitionPlayers.size() - 1;
        while (idx >= 0 && this.mTransitionPlayers.get(idx).mPlayer.asBinder() != player.asBinder()) {
            idx--;
        }
        if (idx < 0) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[3]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(player.asBinder());
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 3242771541905259983L, 0, "Attempt to unregister transition player %s but it isn't registered", protoLogParam0);
                return;
            }
            return;
        }
        boolean needsFlush = idx == this.mTransitionPlayers.size() - 1;
        com.android.server.wm.TransitionController.TransitionPlayerRecord record = this.mTransitionPlayers.remove(idx);
        if (needsFlush) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(player.asBinder());
                long protoLogParam1 = idx;
                long protoLogParam2 = this.mTransitionPlayers.size();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 3691912781236221027L, 20, "Unregistering active transition player %s at index=%d leaving %d in stack", protoLogParam02, java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2));
            }
        } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
            java.lang.String protoLogParam03 = java.lang.String.valueOf(player.asBinder());
            long protoLogParam12 = idx;
            long protoLogParam22 = this.mTransitionPlayers.size();
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -2879980134100946679L, 20, "Unregistering transition player %s at index=%d leaving %d in stack", protoLogParam03, java.lang.Long.valueOf(protoLogParam12), java.lang.Long.valueOf(protoLogParam22));
        }
        record.unlinkToDeath();
        if (!needsFlush) {
            return;
        }
        flushRunningTransitions();
    }

    android.window.ITransitionPlayer getTransitionPlayer() {
        if (this.mTransitionPlayers.isEmpty()) {
            return null;
        }
        return ((com.android.server.wm.TransitionController.TransitionPlayerRecord) this.mTransitionPlayers.getLast()).mPlayer;
    }

    boolean isShellTransitionsEnabled() {
        return !this.mTransitionPlayers.isEmpty();
    }

    boolean useShellTransitionsRotation() {
        return isShellTransitionsEnabled() && SHELL_TRANSITIONS_ROTATION;
    }

    boolean useFullReadyTracking() {
        return this.mFullReadyTracking;
    }

    void setFullReadyTrackingForTest(boolean enabled) {
        this.mFullReadyTracking = enabled;
    }

    boolean isCollecting() {
        return this.mCollectingTransition != null;
    }

    com.android.server.wm.Transition getCollectingTransition() {
        return this.mCollectingTransition;
    }

    int getCollectingTransitionId() {
        if (this.mCollectingTransition == null) {
            throw new java.lang.IllegalStateException("There is no collecting transition");
        }
        return this.mCollectingTransition.getSyncId();
    }

    boolean isCollecting(com.android.server.wm.WindowContainer wc) {
        if (this.mCollectingTransition == null) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                android.util.Slog.e("TestFixed", "isCollecting mCollectingTransition = null");
            }
            return false;
        }
        if (this.mCollectingTransition.mParticipants.contains(wc)) {
            return true;
        }
        for (int i = 0; i < this.mWaitingTransitions.size(); i++) {
            if (this.mWaitingTransitions.get(i).mParticipants.contains(wc)) {
                return true;
            }
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            android.util.Slog.e("TestFixed", "isCollecting mParticipants not contains wc = " + wc);
        }
        return false;
    }

    boolean inCollectingTransition(com.android.server.wm.WindowContainer wc) {
        if (!isCollecting()) {
            return false;
        }
        if (this.mCollectingTransition.isInTransition(wc)) {
            return true;
        }
        for (int i = 0; i < this.mWaitingTransitions.size(); i++) {
            if (this.mWaitingTransitions.get(i).isInTransition(wc)) {
                return true;
            }
        }
        return false;
    }

    boolean isPlaying() {
        return !this.mPlayingTransitions.isEmpty();
    }

    boolean inPlayingTransition(com.android.server.wm.WindowContainer wc) {
        for (int i = this.mPlayingTransitions.size() - 1; i >= 0; i--) {
            if (this.mPlayingTransitions.get(i).isInTransition(wc)) {
                return true;
            }
        }
        return false;
    }

    boolean inFinishingTransition(com.android.server.wm.WindowContainer<?> wc) {
        return this.mFinishingTransition != null && this.mFinishingTransition.isInTransition(wc);
    }

    boolean inTransition() {
        return isCollecting() || isPlaying() || !this.mQueuedTransitions.isEmpty();
    }

    boolean inTransition(com.android.server.wm.WindowContainer wc) {
        return inCollectingTransition(wc) || inPlayingTransition(wc);
    }

    boolean inTransition(int syncId) {
        if (this.mCollectingTransition != null && this.mCollectingTransition.getSyncId() == syncId) {
            return true;
        }
        for (int i = this.mPlayingTransitions.size() - 1; i >= 0; i--) {
            if (this.mPlayingTransitions.get(i).getSyncId() == syncId) {
                return true;
            }
        }
        return false;
    }

    boolean isTransitionOnDisplay(com.android.server.wm.DisplayContent dc) {
        if (this.mCollectingTransition != null && this.mCollectingTransition.isOnDisplay(dc)) {
            return true;
        }
        for (int i = this.mWaitingTransitions.size() - 1; i >= 0; i--) {
            if (this.mWaitingTransitions.get(i).isOnDisplay(dc)) {
                return true;
            }
        }
        for (int i2 = this.mPlayingTransitions.size() - 1; i2 >= 0; i2--) {
            if (this.mPlayingTransitions.get(i2).isOnDisplay(dc)) {
                return true;
            }
        }
        return false;
    }

    boolean hasTransientLaunch(com.android.server.wm.DisplayContent dc) {
        if (this.mCollectingTransition != null && this.mCollectingTransition.hasTransientLaunch() && this.mCollectingTransition.isOnDisplay(dc)) {
            return true;
        }
        for (int i = this.mWaitingTransitions.size() - 1; i >= 0; i--) {
            com.android.server.wm.Transition transition = this.mWaitingTransitions.get(i);
            if (transition.hasTransientLaunch() && transition.isOnDisplay(dc)) {
                return true;
            }
        }
        for (int i2 = this.mPlayingTransitions.size() - 1; i2 >= 0; i2--) {
            com.android.server.wm.Transition transition2 = this.mPlayingTransitions.get(i2);
            if (transition2.hasTransientLaunch() && transition2.isOnDisplay(dc)) {
                return true;
            }
        }
        return false;
    }

    boolean isTransientHide(com.android.server.wm.Task task) {
        if (this.mCollectingTransition != null && this.mCollectingTransition.isInTransientHide(task)) {
            return true;
        }
        for (int i = this.mWaitingTransitions.size() - 1; i >= 0; i--) {
            if (this.mWaitingTransitions.get(i).isInTransientHide(task)) {
                return true;
            }
        }
        for (int i2 = this.mPlayingTransitions.size() - 1; i2 >= 0; i2--) {
            if (this.mPlayingTransitions.get(i2).isInTransientHide(task)) {
                return true;
            }
        }
        return false;
    }

    boolean isTransientVisible(com.android.server.wm.Task task) {
        if (this.mCollectingTransition != null && this.mCollectingTransition.isTransientVisible(task)) {
            return true;
        }
        for (int i = this.mWaitingTransitions.size() - 1; i >= 0; i--) {
            if (this.mWaitingTransitions.get(i).isTransientVisible(task)) {
                return true;
            }
        }
        for (int i2 = this.mPlayingTransitions.size() - 1; i2 >= 0; i2--) {
            if (this.mPlayingTransitions.get(i2).isTransientVisible(task)) {
                return true;
            }
        }
        return false;
    }

    boolean canApplyDim(com.android.server.wm.Task task) {
        if (task == null) {
            return true;
        }
        for (int i = this.mPlayingTransitions.size() - 1; i >= 0; i--) {
            if (!this.mPlayingTransitions.get(i).canApplyDim(task)) {
                return false;
            }
        }
        return true;
    }

    boolean shouldKeepFocus(com.android.server.wm.WindowContainer wc) {
        if (this.mCollectingTransition != null) {
            if (this.mPlayingTransitions.isEmpty()) {
                return this.mCollectingTransition.isInTransientHide(wc);
            }
            return false;
        }
        if (this.mPlayingTransitions.size() == 1) {
            return this.mPlayingTransitions.get(0).isInTransientHide(wc);
        }
        return false;
    }

    boolean isTransientCollect(com.android.server.wm.ActivityRecord ar) {
        return this.mCollectingTransition != null && this.mCollectingTransition.isTransientLaunch(ar);
    }

    boolean isTransientLaunch(com.android.server.wm.ActivityRecord ar) {
        if (isTransientCollect(ar)) {
            return true;
        }
        for (int i = this.mWaitingTransitions.size() - 1; i >= 0; i--) {
            if (this.mWaitingTransitions.get(i).isTransientLaunch(ar)) {
                return true;
            }
        }
        for (int i2 = this.mPlayingTransitions.size() - 1; i2 >= 0; i2--) {
            if (this.mPlayingTransitions.get(i2).isTransientLaunch(ar)) {
                return true;
            }
        }
        return false;
    }

    boolean canAssignLayers(com.android.server.wm.WindowContainer wc) {
        if (this.mBuildingFinishLayers) {
            return wc.asWindowState() == null;
        }
        if (wc.asWindowState() == null) {
            if (isPlaying() && !this.mExt.canAssignLayersWhenPlaying(wc)) {
                return false;
            }
            if (wc.asTask() != null && isCollecting() && !this.mExt.canAssignLayers(wc)) {
                return false;
            }
        }
        return true;
    }

    int getWindowingModeAtStart(com.android.server.wm.WindowContainer wc) {
        if (this.mCollectingTransition == null) {
            return wc.getWindowingMode();
        }
        com.android.server.wm.Transition.ChangeInfo ci = this.mCollectingTransition.mChanges.get(wc);
        if (ci == null) {
            return wc.getWindowingMode();
        }
        return ci.mWindowingMode;
    }

    int getCollectingTransitionType() {
        if (this.mCollectingTransition != null) {
            return this.mCollectingTransition.mType;
        }
        return 0;
    }

    boolean hasCollectingRotationChange(com.android.server.wm.WindowContainer<?> wc, int targetRotation) {
        com.android.server.wm.Transition.ChangeInfo changeInfo;
        com.android.server.wm.Transition transition = this.mCollectingTransition;
        return (transition == null || !transition.mParticipants.contains(wc) || (changeInfo = transition.mChanges.get(wc)) == null || changeInfo.mRotation == targetRotation) ? false : true;
    }

    private void setDisplaySyncMethod(android.window.TransitionRequestInfo.DisplayChange displayChange, com.android.server.wm.DisplayContent displayContent) {
        android.graphics.Rect startBounds = displayChange.getStartAbsBounds();
        android.graphics.Rect endBounds = displayChange.getEndAbsBounds();
        if (startBounds == null || endBounds == null) {
            return;
        }
        setDisplaySyncMethod(startBounds, endBounds, displayContent);
    }

    void setDisplaySyncMethod(android.graphics.Rect startBounds, android.graphics.Rect endBounds, com.android.server.wm.DisplayContent displayContent) {
        int startWidth = startBounds.width();
        int startHeight = startBounds.height();
        int endWidth = endBounds.width();
        int endHeight = endBounds.height();
        if ((endWidth > startWidth) == (endHeight > startHeight)) {
            if (endWidth != startWidth || endHeight != startHeight) {
                displayContent.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda8
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wm.TransitionController.lambda$setDisplaySyncMethod$0((com.android.server.wm.WindowState) obj);
                    }
                }, true);
            }
        }
    }

    static /* synthetic */ void lambda$setDisplaySyncMethod$0(com.android.server.wm.WindowState w) {
        if (w.mToken.mRoundedCornerOverlay && w.mHasSurface) {
            w.mSyncMethodOverride = 1;
        }
    }

    com.android.server.wm.Transition requestTransitionIfNeeded(int type, int flags, com.android.server.wm.WindowContainer trigger, com.android.server.wm.WindowContainer readyGroupRef) {
        if (this.mTransitionPlayers.isEmpty()) {
            return null;
        }
        if (isCollecting()) {
            this.mCollectingTransition.setReady(readyGroupRef, false);
            if ((flags & 14592) == 0) {
                return null;
            }
            this.mCollectingTransition.addFlag(flags & 14592);
            return null;
        }
        com.android.server.wm.Transition newTransition = requestStartTransition(createTransition(type, flags), trigger != null ? trigger.asTask() : null, null, null);
        return newTransition;
    }

    com.android.server.wm.Transition requestStartDisplayTransition(int type, int flags, com.android.server.wm.DisplayContent trigger, android.window.RemoteTransition remoteTransition, android.window.TransitionRequestInfo.DisplayChange displayChange) {
        com.android.server.wm.Transition newTransition = createTransition(type, flags);
        requestStartTransition(newTransition, null, remoteTransition, displayChange);
        if (displayChange != null) {
            setDisplaySyncMethod(displayChange, trigger);
            this.mExt.setWindowSyncMethod(displayChange, newTransition, trigger, this.mSyncEngine);
        }
        return newTransition;
    }

    com.android.server.wm.Transition requestStartTransition(final com.android.server.wm.Transition transition, com.android.server.wm.Task startTask, android.window.RemoteTransition remoteTransition, android.window.TransitionRequestInfo.DisplayChange displayChange) {
        android.app.ActivityManager.RunningTaskInfo pipTaskInfo;
        if (this.mIsWaitingForDisplayEnabled) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                long protoLogParam0 = transition.getSyncId();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -4235778637051052061L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
            transition.mIsPlayerEnabled = false;
            transition.mLogger.mRequestTimeNs = android.os.SystemClock.uptimeNanos();
            this.mAtm.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$requestStartTransition$1(transition);
                }
            });
            return transition;
        }
        if (this.mTransitionPlayers.isEmpty() || transition.isAborted()) {
            if (transition.isCollecting()) {
                transition.abort();
            }
            return transition;
        }
        try {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(transition);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 4005704720444963797L, 0, null, protoLogParam02);
            }
            this.mExt.hookSetBinderUxFlag(true);
            this.mLoggerHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$requestStartTransition$2();
                }
            });
            android.app.ActivityManager.RunningTaskInfo startTaskInfo = null;
            if (startTask != null) {
                startTaskInfo = startTask.getTaskInfo();
            }
            if (transition.getPipActivity() == null) {
                pipTaskInfo = null;
            } else {
                android.app.ActivityManager.RunningTaskInfo pipTaskInfo2 = transition.getPipActivity().getTask().getTaskInfo();
                transition.setPipActivity(null);
                pipTaskInfo = pipTaskInfo2;
            }
            android.window.TransitionRequestInfo request = new android.window.TransitionRequestInfo(transition.mType, startTaskInfo, pipTaskInfo, remoteTransition, displayChange, transition.getFlags(), transition.getSyncId());
            transition.mLogger.mRequestTimeNs = android.os.SystemClock.elapsedRealtimeNanos();
            transition.mLogger.mRequest = request;
            this.mExt.requestSysResource(transition, remoteTransition, displayChange);
            this.mExt.requestStartTransition(request, transition, startTask);
            this.mExt.notifySysWindowRotation(com.android.server.wm.TransitionController.class, null, displayChange);
            ((com.android.server.wm.TransitionController.TransitionPlayerRecord) this.mTransitionPlayers.getLast()).mPlayer.requestStartTransition(transition.getToken(), request);
            if (remoteTransition != null) {
                transition.setRemoteAnimationApp(remoteTransition.getAppThread());
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error requesting transition", e);
            transition.cleanUpOnFailure();
        }
        this.mExt.hookSetBinderUxFlag(false);
        return transition;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestStartTransition$1(com.android.server.wm.Transition transition) {
        this.mAtm.mWindowOrganizerController.startTransition(transition.getToken(), null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestStartTransition$2() {
        this.mExt.setAnimThreadUxIfNeed(true);
        this.mOplusGcSupressionExt.callGcSupression(2, 500);
    }

    com.android.server.wm.Transition requestCloseTransitionIfNeeded(com.android.server.wm.WindowContainer<?> wc) {
        if (this.mTransitionPlayers.isEmpty() || isCollecting() || !wc.isVisibleRequested() || this.mExt.skipRequestCloseTransitionIfNeeded(wc)) {
            return null;
        }
        return requestStartTransition(createTransition(2, 0), wc.asTask(), null, null);
    }

    void collect(com.android.server.wm.WindowContainer wc) {
        if (this.mCollectingTransition == null) {
            return;
        }
        this.mCollectingTransition.collect(wc);
    }

    void collectExistenceChange(com.android.server.wm.WindowContainer wc) {
        if (this.mCollectingTransition == null) {
            return;
        }
        this.mCollectingTransition.collectExistenceChange(wc);
    }

    void recordTaskOrder(com.android.server.wm.WindowContainer wc) {
        if (this.mCollectingTransition == null) {
            return;
        }
        this.mCollectingTransition.recordTaskOrder(wc);
    }

    boolean hasOrderChanges() {
        if (this.mCollectingTransition == null) {
            return false;
        }
        return this.mCollectingTransition.hasOrderChanges();
    }

    void collectForDisplayAreaChange(com.android.server.wm.DisplayArea<?> wc) {
        final com.android.server.wm.Transition transition = this.mCollectingTransition;
        if (transition == null || !transition.mParticipants.contains(wc)) {
            return;
        }
        transition.collectVisibleChange(wc);
        wc.forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.TransitionController.lambda$collectForDisplayAreaChange$3(transition, (com.android.server.wm.Task) obj);
            }
        }, true);
        com.android.server.wm.DisplayContent dc = wc.asDisplayContent();
        if (dc != null) {
            final boolean noAsyncRotation = dc.getAsyncRotationController() == null;
            wc.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$collectForDisplayAreaChange$4(noAsyncRotation, transition, (com.android.server.wm.WindowState) obj);
                }
            }, true);
        }
    }

    static /* synthetic */ void lambda$collectForDisplayAreaChange$3(com.android.server.wm.Transition transition, com.android.server.wm.Task task) {
        if (task.isVisible()) {
            transition.collect(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$collectForDisplayAreaChange$4(boolean noAsyncRotation, com.android.server.wm.Transition transition, com.android.server.wm.WindowState w) {
        if (w.mActivityRecord == null && w.isVisible() && !isCollecting(w.mToken)) {
            if (noAsyncRotation || !com.android.server.wm.AsyncRotationController.canBeAsync(w.mToken)) {
                transition.collect(w.mToken);
            }
        }
    }

    void collectVisibleChange(com.android.server.wm.WindowContainer wc) {
        if (isCollecting()) {
            this.mCollectingTransition.collectVisibleChange(wc);
        }
    }

    void collectReparentChange(com.android.server.wm.WindowContainer wc, com.android.server.wm.WindowContainer newParent) {
        if (isCollecting()) {
            this.mCollectingTransition.collectReparentChange(wc, newParent);
        }
    }

    void setStatusBarTransitionDelay(long delay) {
        if (this.mCollectingTransition == null) {
            return;
        }
        this.mCollectingTransition.mStatusBarTransitionDelay = delay;
    }

    void setOverrideAnimation(android.window.TransitionInfo.AnimationOptions options, android.os.IRemoteCallback startCallback, android.os.IRemoteCallback finishCallback) {
        if (this.mCollectingTransition == null) {
            return;
        }
        this.mCollectingTransition.setOverrideAnimation(options, startCallback, finishCallback);
        this.mExt.setOverrideAnimation(options, this.mCollectingTransition);
    }

    void setNoAnimation(com.android.server.wm.WindowContainer wc) {
        if (this.mCollectingTransition == null) {
            return;
        }
        this.mCollectingTransition.setNoAnimation(wc);
    }

    void setReady(com.android.server.wm.WindowContainer wc, boolean ready) {
        if (this.mCollectingTransition == null) {
            return;
        }
        this.mCollectingTransition.setReady(wc, ready);
    }

    void setReady(com.android.server.wm.WindowContainer wc) {
        setReady(wc, true);
    }

    void deferTransitionReady() {
        if (isShellTransitionsEnabled()) {
            if (this.mCollectingTransition == null) {
                throw new java.lang.IllegalStateException("No collecting transition to defer readiness for.");
            }
            this.mCollectingTransition.deferTransitionReady();
        }
    }

    void continueTransitionReady() {
        if (isShellTransitionsEnabled()) {
            if (this.mCollectingTransition == null) {
                throw new java.lang.IllegalStateException("No collecting transition to defer readiness for.");
            }
            this.mCollectingTransition.continueTransitionReady();
        }
    }

    void finishTransition(com.android.server.wm.Transition record) {
        this.mTransitionMetricsReporter.reportAnimationStart(record.getToken(), 0L);
        this.mAtm.endPowerMode(2);
        if (!this.mPlayingTransitions.contains(record)) {
            android.util.Slog.e(TAG, "Trying to finish a non-playing transition " + record);
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(record);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -6030030735787868329L, 0, null, protoLogParam0);
        }
        this.mPlayingTransitions.remove(record);
        if (!inTransition()) {
            this.mTrackCount = 0;
        }
        updateRunningRemoteAnimation(record, false);
        record.finishTransition();
        if (!this.mExt.delayTriggerExitAnimationDoneWhenFinish(record)) {
            for (int i = this.mAnimatingExitWindows.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowState w = this.mAnimatingExitWindows.get(i);
                if (w.mAnimatingExit && w.mHasSurface && !w.inTransition()) {
                    w.onExitAnimationDone();
                }
                if (!w.mAnimatingExit || !w.mHasSurface) {
                    this.mAnimatingExitWindows.remove(i);
                }
            }
        }
        this.mRunningLock.doNotifyLocked();
        if (!inTransition()) {
            validateStates();
            this.mAtm.mWindowManager.onAnimationFinished();
        }
        this.mExt.releaseSysResource(record);
        this.mExt.finishTransition(record);
        if (!inTransition() && !this.mAtm.mWindowManager.mH.hasMessages(61)) {
            android.util.Slog.d(TAG, "NFW_send RECOMPUTE_FOCUS in finishTransition");
            this.mAtm.mWindowManager.mH.sendEmptyMessage(61);
        }
    }

    void onCommittedInvisibles() {
        if (this.mCollectingTransition != null) {
            this.mCollectingTransition.mPriorVisibilityMightBeDirty = true;
        }
        for (int i = this.mWaitingTransitions.size() - 1; i >= 0; i--) {
            this.mWaitingTransitions.get(i).mPriorVisibilityMightBeDirty = true;
        }
    }

    private void validateStates() {
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, "validateStates, stateValidators:" + this.mStateValidators + ", validateCommitVis:" + this.mValidateCommitVis + ", call by:" + android.os.Debug.getCallers(5));
        }
        for (int i = 0; i < this.mStateValidators.size(); i++) {
            this.mStateValidators.get(i).run();
            if (inTransition()) {
                this.mStateValidators.subList(0, i + 1).clear();
                return;
            }
        }
        this.mStateValidators.clear();
        for (int i2 = 0; i2 < this.mValidateCommitVis.size(); i2++) {
            com.android.server.wm.ActivityRecord ar = this.mValidateCommitVis.get(i2);
            if (!ar.isVisibleRequested() && ar.isVisible()) {
                android.util.Slog.e(TAG, "Uncommitted visibility change: " + ar);
                ar.commitVisibility(ar.isVisibleRequested(), false, false);
            }
        }
        this.mValidateCommitVis.clear();
        for (int i3 = 0; i3 < this.mValidateActivityCompat.size(); i3++) {
            com.android.server.wm.ActivityRecord ar2 = this.mValidateActivityCompat.get(i3);
            if (ar2.getSurfaceControl() != null) {
                android.graphics.Point tmpPos = new android.graphics.Point();
                ar2.getRelativePosition(tmpPos);
                ar2.getSyncTransaction().setPosition(ar2.getSurfaceControl(), tmpPos.x, tmpPos.y);
            }
        }
        this.mValidateActivityCompat.clear();
        for (int i4 = 0; i4 < this.mValidateDisplayVis.size(); i4++) {
            com.android.server.wm.DisplayArea da = this.mValidateDisplayVis.get(i4);
            if (da.isAttached() && da.getSurfaceControl() != null && da.isVisibleRequested()) {
                android.util.Slog.e(TAG, "DisplayArea became visible outside of a transition: " + da);
                da.getSyncTransaction().show(da.getSurfaceControl());
            }
        }
        this.mValidateDisplayVis.clear();
        this.mExt.validateKeyguardOcclusion(this.mAtm.mRootWindowContainer.getDefaultDisplay());
    }

    void onVisibleWithoutCollectingTransition(final com.android.server.wm.WindowContainer<?> wc, java.lang.String caller) {
        boolean isPlaying = !this.mPlayingTransitions.isEmpty();
        android.util.Slog.e(TAG, "Set visible without transition " + wc + " playing=" + isPlaying + " caller=" + caller);
        if (!isPlaying) {
            com.android.server.wm.WindowContainer.enforceSurfaceVisible(wc);
        } else {
            this.mStateValidators.add(new java.lang.Runnable() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.wm.TransitionController.lambda$onVisibleWithoutCollectingTransition$5(wc);
                }
            });
        }
    }

    static /* synthetic */ void lambda$onVisibleWithoutCollectingTransition$5(com.android.server.wm.WindowContainer wc) {
        if (wc.isVisibleRequested()) {
            com.android.server.wm.WindowContainer.enforceSurfaceVisible(wc);
        }
    }

    void onTransitionPopulated(com.android.server.wm.Transition transition) {
        tryStartCollectFromQueue();
    }

    private boolean canStartCollectingNow(com.android.server.wm.Transition queued) {
        if (this.mCollectingTransition == null) {
            return true;
        }
        if (!this.mCollectingTransition.isPopulated() || !getCanBeIndependent(this.mCollectingTransition, queued)) {
            return false;
        }
        for (int i = 0; i < this.mWaitingTransitions.size(); i++) {
            if (!getCanBeIndependent(this.mWaitingTransitions.get(i), queued)) {
                return false;
            }
        }
        return true;
    }

    void tryStartCollectFromQueue() {
        if (this.mQueuedTransitions.isEmpty()) {
            return;
        }
        final com.android.server.wm.TransitionController.QueuedTransition queued = this.mQueuedTransitions.get(0);
        if (this.mCollectingTransition != null) {
            if (queued.mTransition == null || !canStartCollectingNow(queued.mTransition)) {
                return;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
                long protoLogParam0 = this.mCollectingTransition.getSyncId();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -1611886029896664304L, 1, "Moving #%d from collecting to waiting.", java.lang.Long.valueOf(protoLogParam0));
            }
            this.mWaitingTransitions.add(this.mCollectingTransition);
            this.mCollectingTransition = null;
        } else if (this.mSyncEngine.hasActiveSync()) {
            return;
        }
        this.mQueuedTransitions.remove(0);
        if (queued.mTransition != null) {
            moveToCollecting(queued.mTransition);
        } else {
            this.mSyncEngine.startSyncSet(queued.mLegacySync);
        }
        if (queued.mTransition != null && queued.mTransition.mType == 12) {
            queued.mOnStartCollect.onCollectStarted(true);
        } else {
            this.mAtm.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$tryStartCollectFromQueue$6(queued);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$tryStartCollectFromQueue$6(com.android.server.wm.TransitionController.QueuedTransition queued) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtm.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                queued.mOnStartCollect.onCollectStarted(true);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void moveToPlaying(com.android.server.wm.Transition transition) {
        if (transition == this.mCollectingTransition) {
            this.mCollectingTransition = null;
            if (!this.mWaitingTransitions.isEmpty()) {
                this.mCollectingTransition = this.mWaitingTransitions.remove(0);
            }
            if (this.mCollectingTransition == null) {
                this.mLatestOnTopTasksReported.clear();
            }
        } else if (!this.mWaitingTransitions.remove(transition)) {
            throw new java.lang.IllegalStateException("Trying to move non-collecting transition toplaying " + transition.getSyncId());
        }
        this.mPlayingTransitions.add(transition);
        updateRunningRemoteAnimation(transition, true);
    }

    boolean getCanBeIndependent(com.android.server.wm.Transition collecting, com.android.server.wm.Transition queued) {
        if (queued != null && queued.mParallelCollectType == 1 && collecting.mParallelCollectType == 1) {
            return true;
        }
        if (queued == null || queued.mParallelCollectType != 2) {
            return collecting.mParallelCollectType == 2;
        }
        if (collecting.mParallelCollectType == 2) {
            return false;
        }
        for (int i = 0; i < collecting.mParticipants.size(); i++) {
            com.android.server.wm.WindowContainer wc = collecting.mParticipants.valueAt(i);
            com.android.server.wm.ActivityRecord ar = wc.asActivityRecord();
            if (ar == null && wc.asWindowState() == null && wc.asWindowToken() == null) {
                return false;
            }
            if (ar != null && ar.isActivityTypeHomeOrRecents()) {
                return false;
            }
        }
        return true;
    }

    static boolean getIsIndependent(com.android.server.wm.Transition running, com.android.server.wm.Transition incoming) {
        com.android.server.wm.Transition recents;
        com.android.server.wm.Transition other;
        if (running.mParallelCollectType == 1 && incoming.mParallelCollectType == 1) {
            if (DEBUG_PANIC) {
                android.util.Slog.d(TAG, "make independent track for parallel, running:" + running + ", incoming:" + incoming);
            }
            return true;
        }
        if (running.mParallelCollectType == 2 && running.hasTransientLaunch()) {
            if (incoming.mParallelCollectType == 2) {
                return false;
            }
            recents = running;
            other = incoming;
        } else {
            if (incoming.mParallelCollectType != 2 || !incoming.hasTransientLaunch()) {
                return false;
            }
            recents = incoming;
            other = running;
        }
        for (int i = 0; i < other.mTargets.size(); i++) {
            com.android.server.wm.WindowContainer wc = other.mTargets.get(i).mContainer;
            com.android.server.wm.ActivityRecord ar = wc.asActivityRecord();
            if (ar == null && wc.asWindowState() == null && wc.asWindowToken() == null) {
                return false;
            }
            if (ar != null && recents.isTransientLaunch(ar)) {
                return false;
            }
        }
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, "make independent track, running:" + running + ", incoming:" + incoming);
        }
        return true;
    }

    void assignTrack(com.android.server.wm.Transition transition, android.window.TransitionInfo info) {
        int track = -1;
        boolean sync = false;
        boolean skipSyncAssignTrack = this.mExt.skipSyncAssignTrack(transition, info, this.mPlayingTransitions);
        int i = 0;
        while (true) {
            if (i >= this.mPlayingTransitions.size() || skipSyncAssignTrack) {
                break;
            }
            if (this.mPlayingTransitions.get(i) != transition) {
                if (!this.mExt.adjustTrackForRecentsFromRemote(this.mPlayingTransitions, transition, i, info)) {
                    if (!this.mExt.makeIndependentTrackIfNeed(this.mPlayingTransitions.get(i), transition) && !getIsIndependent(this.mPlayingTransitions.get(i), transition)) {
                        if (track >= 0) {
                            sync = true;
                            break;
                        }
                        track = this.mPlayingTransitions.get(i).mAnimationTrack;
                    }
                } else {
                    track = this.mPlayingTransitions.get(i).mAnimationTrack;
                    break;
                }
            }
            i++;
        }
        if (this.mExt.forceAsyncAssignTrackIfNeed(transition, info)) {
            sync = false;
        }
        if (sync) {
            track = 0;
        }
        if (track < 0 && (track = this.mTrackCount) > 0 && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
            long protoLogParam0 = transition.getSyncId();
            long protoLogParam1 = track;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -7097461682459496366L, 5, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
        }
        transition.mAnimationTrack = track;
        info.setTrack(track);
        this.mTrackCount = java.lang.Math.max(this.mTrackCount, track + 1);
        if (sync && this.mTrackCount > 1) {
            info.setFlags(info.getFlags() | 2097152);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                long protoLogParam02 = transition.getSyncId();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, -7364464699035275052L, 1, null, java.lang.Long.valueOf(protoLogParam02));
            }
        }
    }

    boolean isAnimating() {
        return this.mAnimatingState;
    }

    void updateAnimatingState() {
        boolean animatingState = !this.mPlayingTransitions.isEmpty() || (this.mCollectingTransition != null && this.mCollectingTransition.isStarted());
        if (animatingState && !this.mAnimatingState) {
            for (int i = this.mAtm.mRootWindowContainer.getChildCount() - 1; i >= 0; i--) {
                com.android.server.wm.DisplayContent dc = (com.android.server.wm.DisplayContent) this.mAtm.mRootWindowContainer.getChildAt(i);
                if (this.mCollectingTransition != null && this.mCollectingTransition.shouldUsePerfHint(dc)) {
                    dc.enableHighPerfTransition(true);
                } else {
                    int j = this.mPlayingTransitions.size() - 1;
                    while (true) {
                        if (j < 0) {
                            break;
                        }
                        if (this.mPlayingTransitions.get(j).shouldUsePerfHint(dc)) {
                            dc.enableHighPerfTransition(true);
                            break;
                        }
                        j--;
                    }
                }
            }
            this.mSnapshotController.setPause(true);
            this.mAnimatingState = true;
            com.android.server.wm.Transition.asyncTraceBegin("animating", 68942577);
            return;
        }
        if (!animatingState && this.mAnimatingState) {
            for (int i2 = this.mAtm.mRootWindowContainer.getChildCount() - 1; i2 >= 0; i2--) {
                ((com.android.server.wm.DisplayContent) this.mAtm.mRootWindowContainer.getChildAt(i2)).enableHighPerfTransition(false);
            }
            this.mAtm.mWindowManager.scheduleAnimationLocked();
            this.mSnapshotController.setPause(false);
            this.mAnimatingState = false;
            com.android.server.wm.Transition.asyncTraceEnd(68942577);
        }
    }

    private void updateRunningRemoteAnimation(com.android.server.wm.Transition transition, boolean isPlaying) {
        if (this.mTransitionPlayers.isEmpty()) {
            return;
        }
        com.android.server.wm.TransitionController.TransitionPlayerRecord record = (com.android.server.wm.TransitionController.TransitionPlayerRecord) this.mTransitionPlayers.getLast();
        if (record.mPlayerProc == null) {
            return;
        }
        if (isPlaying) {
            record.mPlayerProc.setRunningRemoteAnimation(true);
        } else if (this.mPlayingTransitions.isEmpty()) {
            record.mPlayerProc.setRunningRemoteAnimation(false);
            this.mRemotePlayer.clear();
        }
    }

    void onAbort(com.android.server.wm.Transition transition) {
        if (transition != this.mCollectingTransition) {
            int waitingIdx = this.mWaitingTransitions.indexOf(transition);
            if (waitingIdx < 0) {
                throw new java.lang.IllegalStateException("Too late for abort.");
            }
            this.mWaitingTransitions.remove(waitingIdx);
            return;
        }
        this.mCollectingTransition = null;
        if (!this.mWaitingTransitions.isEmpty()) {
            this.mCollectingTransition = this.mWaitingTransitions.remove(0);
        }
        if (this.mCollectingTransition == null) {
            this.mLatestOnTopTasksReported.clear();
        }
    }

    void setTransientLaunch(com.android.server.wm.ActivityRecord activity, com.android.server.wm.Task restoreBelowTask) {
        if (this.mCollectingTransition == null) {
            return;
        }
        this.mCollectingTransition.setTransientLaunch(activity, restoreBelowTask);
        if (activity.isActivityTypeHomeOrRecents()) {
            this.mCollectingTransition.addFlag(128);
            activity.getTask().setCanAffectSystemUiFlags(false);
            this.mExt.setTransientLaunchIfNeed(activity, this);
        }
    }

    void setCanPipOnFinish(boolean canPipOnFinish) {
        if (this.mCollectingTransition == null) {
            return;
        }
        this.mCollectingTransition.setCanPipOnFinish(canPipOnFinish);
    }

    void legacyDetachNavigationBarFromApp(android.os.IBinder token) {
        com.android.server.wm.Transition transition = com.android.server.wm.Transition.fromBinder(token);
        if (transition == null || !this.mPlayingTransitions.contains(transition)) {
            android.util.Slog.e(TAG, "Transition isn't playing: " + token);
        } else {
            transition.legacyRestoreNavigationBarFromApp();
        }
    }

    void registerLegacyListener(com.android.server.wm.WindowManagerInternal.AppTransitionListener listener) {
        this.mLegacyListeners.add(listener);
    }

    void unregisterLegacyListener(com.android.server.wm.WindowManagerInternal.AppTransitionListener listener) {
        this.mLegacyListeners.remove(listener);
    }

    void dispatchLegacyAppTransitionPending() {
        for (int i = 0; i < this.mLegacyListeners.size(); i++) {
            this.mLegacyListeners.get(i).onAppTransitionPendingLocked();
        }
    }

    void dispatchLegacyAppTransitionStarting(android.window.TransitionInfo info, long statusBarTransitionDelay) {
        for (int i = 0; i < this.mLegacyListeners.size(); i++) {
            this.mLegacyListeners.get(i).onAppTransitionStartingLocked(android.os.SystemClock.uptimeMillis() + statusBarTransitionDelay, 120L);
        }
    }

    void dispatchLegacyAppTransitionFinished(com.android.server.wm.ActivityRecord ar) {
        for (int i = 0; i < this.mLegacyListeners.size(); i++) {
            this.mLegacyListeners.get(i).onAppTransitionFinishedLocked(ar.token);
        }
    }

    void dispatchLegacyAppTransitionCancelled() {
        for (int i = 0; i < this.mLegacyListeners.size(); i++) {
            this.mLegacyListeners.get(i).onAppTransitionCancelledLocked(false);
        }
    }

    void dumpDebugLegacy(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        int state = 0;
        if (!this.mPlayingTransitions.isEmpty()) {
            state = 2;
        } else if ((this.mCollectingTransition != null && this.mCollectingTransition.getLegacyIsReady()) || this.mSyncEngine.hasPendingSyncSets()) {
            state = 1;
        }
        proto.write(1159641169921L, state);
        proto.end(token);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        java.lang.String subPrefix = "  " + prefix;
        pw.println(prefix + "TransitionController:");
        pw.println(subPrefix + "  mPlayingTransitions:" + this.mPlayingTransitions.size());
        for (int i = 0; i < this.mPlayingTransitions.size(); i++) {
            this.mPlayingTransitions.get(i).dump(pw, subPrefix, dumpAll);
        }
        pw.println(subPrefix + "mWaitingTransitions:" + this.mWaitingTransitions.size());
        for (int i2 = 0; i2 < this.mWaitingTransitions.size(); i2++) {
            this.mWaitingTransitions.get(i2).dump(pw, subPrefix, dumpAll);
        }
        if (this.mCollectingTransition != null) {
            pw.println(subPrefix + "mCollectingTransition:");
            this.mCollectingTransition.dump(pw, subPrefix + "    ", dumpAll);
        }
        this.mSyncEngine.dump(pw, prefix, dumpAll);
    }

    private void queueTransition(com.android.server.wm.Transition transit, com.android.server.wm.TransitionController.OnStartCollect onStartCollect) {
        this.mQueuedTransitions.add(new com.android.server.wm.TransitionController.QueuedTransition(transit, onStartCollect));
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(transit);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -5509640937151643757L, 0, "Queueing transition: %s", protoLogParam0);
        }
    }

    boolean startCollectOrQueue(com.android.server.wm.Transition transit, com.android.server.wm.TransitionController.OnStartCollect onStartCollect) {
        if (!this.mQueuedTransitions.isEmpty()) {
            queueTransition(transit, onStartCollect);
            return false;
        }
        this.mLoggerHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startCollectOrQueue$7();
            }
        });
        if (this.mSyncEngine.hasActiveSync()) {
            if (isCollecting()) {
                if (canStartCollectingNow(transit)) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
                        long protoLogParam0 = this.mCollectingTransition.getSyncId();
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -1611886029896664304L, 1, "Moving #%d from collecting to waiting.", java.lang.Long.valueOf(protoLogParam0));
                    }
                    this.mWaitingTransitions.add(this.mCollectingTransition);
                    this.mCollectingTransition = null;
                    moveToCollecting(transit);
                    onStartCollect.onCollectStarted(false);
                    return true;
                }
            } else {
                android.util.Slog.w(TAG, "Ongoing Sync outside of transition.");
            }
            queueTransition(transit, onStartCollect);
            return false;
        }
        moveToCollecting(transit);
        onStartCollect.onCollectStarted(false);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startCollectOrQueue$7() {
        this.mExt.setAnimThreadUxIfNeed(true);
    }

    com.android.server.wm.Transition createAndStartCollecting(int type) {
        if (this.mTransitionPlayers.isEmpty() || !this.mQueuedTransitions.isEmpty()) {
            return null;
        }
        if (this.mSyncEngine.hasActiveSync()) {
            if (isCollecting()) {
                if (canStartCollectingNow(null)) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
                        long protoLogParam0 = this.mCollectingTransition.getSyncId();
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -1611886029896664304L, 1, "Moving #%d from collecting to waiting.", java.lang.Long.valueOf(protoLogParam0));
                    }
                    this.mWaitingTransitions.add(this.mCollectingTransition);
                    this.mCollectingTransition = null;
                    com.android.server.wm.Transition transit = new com.android.server.wm.Transition(type, 0, this, this.mSyncEngine);
                    moveToCollecting(transit);
                    return transit;
                }
            } else {
                android.util.Slog.w(TAG, "Ongoing Sync outside of transition.");
            }
            return null;
        }
        com.android.server.wm.Transition transit2 = new com.android.server.wm.Transition(type, 0, this, this.mSyncEngine);
        moveToCollecting(transit2);
        return transit2;
    }

    void startLegacySyncOrQueue(com.android.server.wm.BLASTSyncEngine.SyncGroup syncGroup, final java.util.function.Consumer<java.lang.Boolean> applySync) {
        if (!this.mQueuedTransitions.isEmpty() || this.mSyncEngine.hasActiveSync()) {
            this.mQueuedTransitions.add(new com.android.server.wm.TransitionController.QueuedTransition(syncGroup, new com.android.server.wm.TransitionController.OnStartCollect() { // from class: com.android.server.wm.TransitionController$$ExternalSyntheticLambda2
                @Override // com.android.server.wm.TransitionController.OnStartCollect
                public final void onCollectStarted(boolean z) {
                    applySync.accept(true);
                }
            }));
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(syncGroup.mSyncId);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -2741593375634604522L, 0, "Queueing legacy sync-set: %s", protoLogParam0);
                return;
            }
            return;
        }
        this.mSyncEngine.startSyncSet(syncGroup);
        applySync.accept(false);
    }

    void waitFor(com.android.server.wm.Transition.ReadyCondition condition) {
        if (this.mCollectingTransition == null) {
            android.util.Slog.e(TAG, "No collecting transition available to wait for " + condition);
            condition.mTracker = com.android.server.wm.Transition.ReadyTracker.NULL_TRACKER;
        } else {
            this.mCollectingTransition.mReadyTracker.add(condition);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class TransitionPlayerRecord {
        android.os.IBinder.DeathRecipient mDeath = null;
        final android.window.ITransitionPlayer mPlayer;
        private com.android.server.wm.WindowProcessController mPlayerProc;

        TransitionPlayerRecord(android.window.ITransitionPlayer player, com.android.server.wm.WindowProcessController playerProc) {
            this.mPlayer = player;
            this.mPlayerProc = playerProc;
            try {
                linkToDeath();
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException("Unable to set transition player");
            }
        }

        private void linkToDeath() throws android.os.RemoteException {
            if (this.mPlayer.asBinder() == null) {
                return;
            }
            this.mDeath = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.wm.TransitionController$TransitionPlayerRecord$$ExternalSyntheticLambda0
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.lambda$linkToDeath$0();
                }
            };
            this.mPlayer.asBinder().linkToDeath(this.mDeath, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$linkToDeath$0() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.TransitionController.this.mAtm.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.TransitionController.this.unregisterTransitionPlayer(this.mPlayer);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        void unlinkToDeath() {
            if (this.mPlayer.asBinder() == null || this.mDeath == null) {
                return;
            }
            this.mPlayer.asBinder().unlinkToDeath(this.mDeath, 0);
            this.mDeath = null;
        }
    }

    static class RemotePlayer {
        private static final long REPORT_RUNNING_GRACE_PERIOD_MS = 200;
        private final com.android.server.wm.ActivityTaskManagerService mAtm;
        private final android.util.ArrayMap<android.os.IBinder, com.android.server.wm.TransitionController.RemotePlayer.DelegateProcess> mDelegateProcesses = new android.util.ArrayMap<>();

        private class DelegateProcess implements java.lang.Runnable {
            boolean mNeedReport;
            final com.android.server.wm.WindowProcessController mProc;

            DelegateProcess(com.android.server.wm.WindowProcessController proc) {
                this.mProc = proc;
            }

            @Override // java.lang.Runnable
            public void run() {
                synchronized (com.android.server.wm.TransitionController.RemotePlayer.this.mAtm.mGlobalLockWithoutBoost) {
                    com.android.server.wm.TransitionController.RemotePlayer.this.update(this.mProc, false, false);
                }
            }
        }

        RemotePlayer(com.android.server.wm.ActivityTaskManagerService atm) {
            this.mAtm = atm;
        }

        void update(com.android.server.wm.WindowProcessController delegate, boolean running, boolean predict) {
            if (!running) {
                synchronized (this.mDelegateProcesses) {
                    boolean removed = false;
                    int i = this.mDelegateProcesses.size() - 1;
                    while (true) {
                        if (i < 0) {
                            break;
                        }
                        if (this.mDelegateProcesses.valueAt(i).mProc != delegate) {
                            i--;
                        } else {
                            this.mDelegateProcesses.removeAt(i);
                            removed = true;
                            break;
                        }
                    }
                    if (removed) {
                        delegate.setRunningRemoteAnimation(false);
                        return;
                    }
                    return;
                }
            }
            if (delegate.isRunningRemoteTransition() || !delegate.hasThread()) {
                return;
            }
            delegate.setRunningRemoteAnimation(true);
            com.android.server.wm.TransitionController.RemotePlayer.DelegateProcess delegateProc = new com.android.server.wm.TransitionController.RemotePlayer.DelegateProcess(delegate);
            if (predict) {
                delegateProc.mNeedReport = true;
                this.mAtm.mH.postDelayed(delegateProc, REPORT_RUNNING_GRACE_PERIOD_MS);
            }
            synchronized (this.mDelegateProcesses) {
                this.mDelegateProcesses.put(delegate.getThread().asBinder(), delegateProc);
            }
        }

        void clear() {
            synchronized (this.mDelegateProcesses) {
                for (int i = this.mDelegateProcesses.size() - 1; i >= 0; i--) {
                    this.mDelegateProcesses.valueAt(i).mProc.setRunningRemoteAnimation(false);
                }
                this.mDelegateProcesses.clear();
            }
        }

        boolean reportRunning(android.app.IApplicationThread appThread) {
            com.android.server.wm.TransitionController.RemotePlayer.DelegateProcess delegate;
            synchronized (this.mDelegateProcesses) {
                delegate = this.mDelegateProcesses.get(appThread.asBinder());
                if (delegate != null && delegate.mNeedReport) {
                    delegate.mNeedReport = false;
                    this.mAtm.mH.removeCallbacks(delegate);
                }
            }
            return delegate != null;
        }
    }

    static class Logger implements java.lang.Runnable {
        long mAbortTimeNs;
        long mCollectTimeNs;
        long mCreateTimeNs;
        long mCreateWallTimeMs;
        long mFinishTimeNs;
        android.window.TransitionInfo mInfo;
        long mReadyTimeNs;
        android.window.TransitionRequestInfo mRequest;
        long mRequestTimeNs;
        long mSendTimeNs;
        long mStartTimeNs;
        android.window.WindowContainerTransaction mStartWCT;
        int mSyncId;

        Logger() {
        }

        private java.lang.String buildOnSendLog() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("Sent Transition (#").append(this.mSyncId).append(") createdAt=").append(android.util.TimeUtils.logTimeOfDay(this.mCreateWallTimeMs));
            if (this.mRequest != null) {
                sb.append(" via request=").append(this.mRequest);
            }
            return sb.toString();
        }

        void logOnSendAsync(android.os.Handler handler) {
            handler.post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                logOnSend();
            } catch (java.lang.Exception e) {
                android.util.Slog.w(com.android.server.wm.TransitionController.TAG, "Failed to log transition", e);
            }
        }

        void logOnSend() {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(buildOnSendLog());
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -5051723169912572741L, 0, "%s", protoLogParam0);
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this.mStartWCT);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 4281568181321808508L, 0, "    startWCT=%s", protoLogParam02);
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
                java.lang.String protoLogParam03 = java.lang.String.valueOf(this.mInfo.toString("    "));
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 5141999957143860655L, 0, "    info=%s", protoLogParam03);
            }
        }

        private static java.lang.String toMsString(long nanos) {
            return (java.lang.Math.round(nanos / 1000.0d) / 1000.0d) + "ms";
        }

        private java.lang.String buildOnFinishLog() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("Finish Transition (#").append(this.mSyncId).append("): created at ").append(android.util.TimeUtils.logTimeOfDay(this.mCreateWallTimeMs));
            sb.append(" collect-started=").append(toMsString(this.mCollectTimeNs - this.mCreateTimeNs));
            if (this.mRequestTimeNs != 0) {
                sb.append(" request-sent=").append(toMsString(this.mRequestTimeNs - this.mCreateTimeNs));
            }
            sb.append(" started=").append(toMsString(this.mStartTimeNs - this.mCreateTimeNs));
            sb.append(" ready=").append(toMsString(this.mReadyTimeNs - this.mCreateTimeNs));
            sb.append(" sent=").append(toMsString(this.mSendTimeNs - this.mCreateTimeNs));
            sb.append(" finished=").append(toMsString(this.mFinishTimeNs - this.mCreateTimeNs));
            return sb.toString();
        }

        void logOnFinish() {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(buildOnFinishLog());
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, -5051723169912572741L, 0, "%s", protoLogParam0);
            }
        }
    }

    static class TransitionMetricsReporter extends android.window.ITransitionMetricsReporter.Stub {
        private final android.util.ArrayMap<android.os.IBinder, java.util.function.LongConsumer> mMetricConsumers = new android.util.ArrayMap<>();

        TransitionMetricsReporter() {
        }

        void associate(android.os.IBinder transitionToken, java.util.function.LongConsumer consumer) {
            synchronized (this.mMetricConsumers) {
                this.mMetricConsumers.put(transitionToken, consumer);
            }
        }

        public void reportAnimationStart(android.os.IBinder transitionToken, long startTime) {
            synchronized (this.mMetricConsumers) {
                if (this.mMetricConsumers.isEmpty()) {
                    return;
                }
                java.util.function.LongConsumer c = this.mMetricConsumers.remove(transitionToken);
                if (c != null) {
                    c.accept(startTime);
                }
            }
        }
    }

    class Lock {
        private int mTransitionWaiters = 0;

        Lock() {
        }

        void runWhenIdle(long timeout, java.lang.Runnable r) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.TransitionController.this.mAtm.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (!com.android.server.wm.TransitionController.this.inTransition()) {
                        r.run();
                        return;
                    }
                    this.mTransitionWaiters++;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    long startTime = android.os.SystemClock.uptimeMillis();
                    long endTime = startTime + timeout;
                    while (true) {
                        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = com.android.server.wm.TransitionController.this.mAtm.mGlobalLock;
                        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock2) {
                            try {
                                if (!com.android.server.wm.TransitionController.this.inTransition() || android.os.SystemClock.uptimeMillis() > endTime) {
                                    break;
                                }
                            } finally {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            }
                        }
                        synchronized (this) {
                            try {
                                try {
                                    wait(timeout);
                                } catch (java.lang.InterruptedException e) {
                                    return;
                                }
                            } finally {
                            }
                        }
                    }
                    this.mTransitionWaiters--;
                    r.run();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } finally {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            }
        }

        void doNotifyLocked() {
            synchronized (this) {
                if (this.mTransitionWaiters > 0) {
                    notifyAll();
                }
            }
        }
    }

    public com.android.server.wm.ITransitionControllerWrapper getWrapper() {
        return this.mWrapper;
    }

    private class TransitionControllerWrapper implements com.android.server.wm.ITransitionControllerWrapper {
        private TransitionControllerWrapper() {
        }

        @Override // com.android.server.wm.ITransitionControllerWrapper
        public java.util.ArrayList<com.android.server.wm.Transition> getPlayingTransitions() {
            return com.android.server.wm.TransitionController.this.mPlayingTransitions;
        }
    }
}
