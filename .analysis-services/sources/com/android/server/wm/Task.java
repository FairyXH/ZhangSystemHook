package com.android.server.wm;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes3.dex */
public class Task extends com.android.server.wm.TaskFragment {
    private static final java.lang.String ATTR_AFFINITY = "affinity";
    private static final java.lang.String ATTR_AUTOREMOVERECENTS = "auto_remove_recents";
    private static final java.lang.String ATTR_CALLING_FEATURE_ID = "calling_feature_id";
    private static final java.lang.String ATTR_CALLING_PACKAGE = "calling_package";
    private static final java.lang.String ATTR_CALLING_UID = "calling_uid";
    private static final java.lang.String ATTR_CONTAINER_TASK_ID = "containerTaskId";
    private static final java.lang.String ATTR_EFFECTIVE_UID = "effective_uid";
    private static final java.lang.String ATTR_EMBEDDED_CHILDREN = "embeddedChildren";
    private static final java.lang.String ATTR_EMBEDDED_CONTAINER = "embedded_container";
    private static final java.lang.String ATTR_IS_CONTAINER_TASK = "isContainerTask";
    private static final java.lang.String ATTR_IS_SHOW_RECENT = "isShowRecent";
    private static final java.lang.String ATTR_LASTDESCRIPTION = "last_description";
    private static final java.lang.String ATTR_LASTTIMEMOVED = "last_time_moved";
    private static final java.lang.String ATTR_LAST_SNAPSHOT_BUFFER_SIZE = "last_snapshot_buffer_size";
    private static final java.lang.String ATTR_LAST_SNAPSHOT_CONTENT_INSETS = "last_snapshot_content_insets";
    private static final java.lang.String ATTR_LAST_SNAPSHOT_TASK_SIZE = "last_snapshot_task_size";
    private static final java.lang.String ATTR_MIN_HEIGHT = "min_height";
    private static final java.lang.String ATTR_MIN_WIDTH = "min_width";
    private static final java.lang.String ATTR_NEVERRELINQUISH = "never_relinquish_identity";
    private static final java.lang.String ATTR_NEXT_AFFILIATION = "next_affiliation";
    private static final java.lang.String ATTR_NON_FULLSCREEN_BOUNDS = "non_fullscreen_bounds";
    private static final java.lang.String ATTR_OPLUS_FLAGS = "oplusFlags";
    private static final java.lang.String ATTR_ORIGACTIVITY = "orig_activity";
    private static final java.lang.String ATTR_PERSIST_TASK_VERSION = "persist_task_version";
    private static final java.lang.String ATTR_PREV_AFFILIATION = "prev_affiliation";
    private static final java.lang.String ATTR_REALACTIVITY = "real_activity";
    private static final java.lang.String ATTR_REALACTIVITY_SUSPENDED = "real_activity_suspended";
    private static final java.lang.String ATTR_RESIZE_MODE = "resize_mode";
    private static final java.lang.String ATTR_ROOTHASRESET = "root_has_reset";
    private static final java.lang.String ATTR_ROOT_AFFINITY = "root_affinity";
    private static final java.lang.String ATTR_SUPPORTS_PICTURE_IN_PICTURE = "supports_picture_in_picture";
    private static final java.lang.String ATTR_TASKID = "task_id";

    @java.lang.Deprecated
    private static final java.lang.String ATTR_TASKTYPE = "task_type";
    private static final java.lang.String ATTR_TASK_AFFILIATION = "task_affiliation";
    private static final java.lang.String ATTR_USERID = "user_id";
    private static final java.lang.String ATTR_USER_SETUP_COMPLETE = "user_setup_complete";
    private static final java.lang.String ATTR_WINDOW_LAYOUT_AFFINITY = "window_layout_affinity";
    private static final int DEFAULT_MIN_TASK_SIZE_DP = 220;
    static final int LAYER_RANK_INVISIBLE = -1;
    static final int PERSIST_TASK_VERSION = 1;
    static final int REPARENT_KEEP_ROOT_TASK_AT_FRONT = 1;
    static final int REPARENT_LEAVE_ROOT_TASK_IN_PLACE = 2;
    static final int REPARENT_MOVE_ROOT_TASK_TO_FRONT = 0;
    private static final java.lang.String TAG_ACTIVITY = "activity";
    private static final java.lang.String TAG_AFFINITYINTENT = "affinity_intent";
    private static final java.lang.String TAG_INTENT = "intent";
    private static final long TRANSLUCENT_CONVERSION_TIMEOUT = 2000;
    private static final int TRANSLUCENT_TIMEOUT_MSG = 101;
    private static java.lang.Exception sTmpException;
    java.lang.String affinity;
    android.content.Intent affinityIntent;
    boolean autoRemoveRecents;
    int effectiveUid;
    boolean inRecents;
    android.content.Intent intent;
    boolean isAvailable;
    boolean isPersistable;
    long lastActiveTime;
    java.lang.CharSequence lastDescription;
    int mAffiliatedTaskId;
    boolean mAlignActivityLocaleWithTask;
    private final com.android.server.wm.AnimatingActivityRegistry mAnimatingActivityRegistry;
    java.lang.String mCallingFeatureId;
    java.lang.String mCallingPackage;
    int mCallingUid;
    private boolean mCanAffectSystemUiFlags;
    com.android.server.wm.ActivityRecord mChildPipActivity;
    boolean mConfigWillChange;
    int mCurrentUser;
    com.android.server.wm.Task.DecorSurfaceContainer mDecorSurfaceContainer;
    private boolean mDeferTaskAppear;
    private boolean mDragResizing;
    private final com.android.server.wm.Task.FindRootHelper mFindRootHelper;
    private boolean mForceShowForAllUsers;
    private final android.os.Handler mHandler;
    private boolean mHasBeenVisible;
    boolean mInRemoveTask;
    boolean mInResumeTopActivity;
    boolean mIsEffectivelySystemApp;
    boolean mKillProcessesOnDestroyed;
    android.graphics.Rect mLastNonFullscreenBounds;
    android.view.SurfaceControl mLastRecentsAnimationOverlay;
    android.window.PictureInPictureSurfaceTransaction mLastRecentsAnimationTransaction;
    int mLastReportedRequestedOrientation;
    boolean mLastSurfaceShowing;
    final android.app.ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData mLastTaskSnapshotData;
    long mLastTimeMoved;
    android.os.IBinder mLaunchCookie;
    int mLayerRank;
    int mLockTaskAuth;
    int mLockTaskUid;
    android.window.WindowContainerToken mMultiWindowRestoreParent;
    int mMultiWindowRestoreWindowingMode;
    private boolean mNeverRelinquishIdentity;
    com.android.server.wm.Task mNextAffiliate;
    int mNextAffiliateTaskId;
    private com.android.server.wm.ActivityRecord mPendingConvertFromTranslucentActivity;
    com.android.server.wm.Task mPrevAffiliate;
    int mPrevAffiliateTaskId;
    int mPrevDisplayId;
    boolean mRemoveWithTaskOrganizer;
    private boolean mRemoving;
    boolean mReparentLeafTaskIfRelaunch;
    java.lang.String mRequiredDisplayCategory;
    int mResizeMode;
    private boolean mReuseTask;
    private com.android.server.wm.WindowProcessController mRootProcess;
    com.android.server.wm.StartingData mSharedStartingData;
    boolean mSupportsPictureInPicture;
    boolean mTaskAppearedSent;
    private android.app.ActivityManager.TaskDescription mTaskDescription;
    java.lang.String mTaskFragmentHostProcessName;
    int mTaskFragmentHostUid;
    final int mTaskId;
    android.window.ITaskOrganizer mTaskOrganizer;
    private com.android.server.wm.Task.TaskWrapper mTaskWrapper;
    private final android.graphics.Rect mTmpRect;
    com.android.server.wm.ActivityRecord mTranslucentActivityWaiting;
    java.util.ArrayList<com.android.server.wm.ActivityRecord> mUndrawnActivitiesBelowTopTranslucent;
    int mUserId;
    boolean mUserSetupComplete;
    java.lang.String mWindowLayoutAffinity;
    int maxRecents;
    android.content.ComponentName origActivity;
    android.content.ComponentName realActivity;
    boolean realActivitySuspended;
    java.lang.String rootAffinity;
    boolean rootWasReset;
    java.lang.String stringName;
    com.android.internal.app.IVoiceInteractor voiceInteractor;
    android.service.voice.IVoiceInteractionSession voiceSession;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static final java.lang.String TAG_RECENTS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_RECENTS;
    static final java.lang.String TAG_TASKS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_TASKS;
    static final java.lang.String TAG_CLEANUP = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_CLEANUP;
    private static final java.lang.String TAG_SWITCH = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_SWITCH;
    private static final java.lang.String TAG_TRANSITION = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_TRANSITION;
    private static final java.lang.String TAG_USER_LEAVING = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_USER_LEAVING;
    static final java.lang.String TAG_VISIBILITY = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_VISIBILITY;
    private static final android.graphics.Rect sTmpBounds = new android.graphics.Rect();
    private static final com.android.server.wm.ResetTargetTaskHelper sResetTargetTaskHelper = new com.android.server.wm.ResetTargetTaskHelper();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface ReparentMoveRootTaskMode {
    }

    private class ActivityTaskHandler extends android.os.Handler {
        ActivityTaskHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 101:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.Task.this.mAtmService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            com.android.server.wm.Task.this.notifyActivityDrawnLocked(null);
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                default:
                    return;
            }
        }
    }

    private class FindRootHelper implements java.util.function.Predicate<com.android.server.wm.ActivityRecord> {
        private boolean mIgnoreRelinquishIdentity;
        private com.android.server.wm.ActivityRecord mRoot;
        private boolean mSetToBottomIfNone;

        private FindRootHelper() {
        }

        com.android.server.wm.ActivityRecord findRoot(boolean ignoreRelinquishIdentity, boolean setToBottomIfNone) {
            this.mIgnoreRelinquishIdentity = ignoreRelinquishIdentity;
            this.mSetToBottomIfNone = setToBottomIfNone;
            com.android.server.wm.Task.this.forAllActivities((java.util.function.Predicate<com.android.server.wm.ActivityRecord>) this, false);
            com.android.server.wm.ActivityRecord root = this.mRoot;
            this.mRoot = null;
            return root;
        }

        @Override // java.util.function.Predicate
        public boolean test(com.android.server.wm.ActivityRecord r) {
            if (this.mRoot == null && this.mSetToBottomIfNone) {
                this.mRoot = r;
            }
            if (r.finishing) {
                return false;
            }
            if (this.mRoot == null || this.mRoot.finishing) {
                this.mRoot = r;
            }
            int uid = this.mRoot == r ? com.android.server.wm.Task.this.effectiveUid : r.info.applicationInfo.uid;
            if (!this.mIgnoreRelinquishIdentity && (this.mRoot.info.flags & 4096) != 0) {
                if (this.mRoot.info.applicationInfo.uid != 1000 && !this.mRoot.info.applicationInfo.isSystemApp() && this.mRoot.info.applicationInfo.uid != uid) {
                    return true;
                }
                this.mRoot = r;
                return false;
            }
            return true;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Task(com.android.server.wm.ActivityTaskManagerService activityTaskManagerService, int i, android.content.Intent intent, android.content.Intent intent2, java.lang.String str, java.lang.String str2, android.content.ComponentName componentName, android.content.ComponentName componentName2, boolean z, boolean z2, int i2, int i3, java.lang.String str3, long j, boolean z3, android.app.ActivityManager.TaskDescription taskDescription, android.app.ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData persistedTaskSnapshotData, int i4, int i5, int i6, int i7, java.lang.String str4, java.lang.String str5, int i8, boolean z4, boolean z5, boolean z6, int i9, int i10, android.content.pm.ActivityInfo activityInfo, android.service.voice.IVoiceInteractionSession iVoiceInteractionSession, com.android.internal.app.IVoiceInteractor iVoiceInteractor, boolean z7, android.os.IBinder iBinder, boolean z8, boolean z9) {
        android.app.ActivityManager.TaskDescription taskDescription2;
        android.app.ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData persistedTaskSnapshotData2;
        super(activityTaskManagerService, null, z7, false);
        this.mTranslucentActivityWaiting = null;
        this.mUndrawnActivitiesBelowTopTranslucent = new java.util.ArrayList<>();
        this.mPendingConvertFromTranslucentActivity = null;
        this.mInResumeTopActivity = false;
        this.mLockTaskAuth = 1;
        this.mLockTaskUid = -1;
        this.isPersistable = false;
        this.mNeverRelinquishIdentity = true;
        this.mReuseTask = false;
        this.mPrevAffiliateTaskId = -1;
        this.mNextAffiliateTaskId = -1;
        this.mLastNonFullscreenBounds = null;
        this.mLayerRank = -1;
        this.mPrevDisplayId = -1;
        this.mMultiWindowRestoreWindowingMode = -1;
        this.mLastReportedRequestedOrientation = -1;
        this.mTmpRect = new android.graphics.Rect();
        this.mCanAffectSystemUiFlags = true;
        this.mAnimatingActivityRegistry = new com.android.server.wm.AnimatingActivityRegistry();
        this.mFindRootHelper = new com.android.server.wm.Task.FindRootHelper();
        this.mAlignActivityLocaleWithTask = false;
        this.mTaskWrapper = new com.android.server.wm.Task.TaskWrapper();
        this.mTaskId = i;
        this.mUserId = i2;
        this.mResizeMode = i8;
        this.mSupportsPictureInPicture = z4;
        if (taskDescription != null) {
            taskDescription2 = taskDescription;
        } else {
            taskDescription2 = new android.app.ActivityManager.TaskDescription();
        }
        this.mTaskDescription = taskDescription2;
        if (persistedTaskSnapshotData != null) {
            persistedTaskSnapshotData2 = persistedTaskSnapshotData;
        } else {
            persistedTaskSnapshotData2 = new android.app.ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData();
        }
        this.mLastTaskSnapshotData = persistedTaskSnapshotData2;
        this.affinityIntent = intent2;
        this.affinity = str;
        this.rootAffinity = str2;
        this.voiceSession = iVoiceInteractionSession;
        this.voiceInteractor = iVoiceInteractor;
        this.realActivity = componentName;
        this.realActivitySuspended = z5;
        this.origActivity = componentName2;
        this.rootWasReset = z;
        this.isAvailable = true;
        this.autoRemoveRecents = z2;
        this.mUserSetupComplete = z6;
        this.effectiveUid = i3;
        touchActiveTime();
        this.lastDescription = str3;
        this.mLastTimeMoved = j;
        this.mNeverRelinquishIdentity = z3;
        this.mAffiliatedTaskId = i4;
        this.mPrevAffiliateTaskId = i5;
        this.mNextAffiliateTaskId = i6;
        this.mCallingUid = i7;
        this.mCallingPackage = str4;
        this.mCallingFeatureId = str5;
        this.mResizeMode = i8;
        if (activityInfo != null) {
            setIntent(intent, activityInfo);
            setMinDimensions(activityInfo);
        } else {
            this.intent = intent;
            this.mMinWidth = i9;
            this.mMinHeight = i10;
        }
        this.mAtmService.getTaskChangeNotificationController().notifyTaskCreated(i, this.realActivity);
        this.mHandler = new com.android.server.wm.Task.ActivityTaskHandler(this.mTaskSupervisor.mLooper);
        this.mCurrentUser = this.mAtmService.mAmInternal.getCurrentUserId();
        this.mLaunchCookie = iBinder;
        this.mDeferTaskAppear = z8;
        this.mRemoveWithTaskOrganizer = z9;
        com.android.server.wm.EventLogTags.writeWmTaskCreated(this.mTaskId);
        this.mTaskWrapper.getExtImpl().handleTaskCreated(this);
    }

    static com.android.server.wm.Task fromWindowContainerToken(android.window.WindowContainerToken token) {
        com.android.server.wm.WindowContainer wc;
        if (token == null || (wc = fromBinder(token.asBinder())) == null) {
            return null;
        }
        return wc.asTask();
    }

    com.android.server.wm.Task reuseAsLeafTask(android.service.voice.IVoiceInteractionSession _voiceSession, com.android.internal.app.IVoiceInteractor _voiceInteractor, android.content.Intent intent, android.content.pm.ActivityInfo info, com.android.server.wm.ActivityRecord activity) {
        this.voiceSession = _voiceSession;
        this.voiceInteractor = _voiceInteractor;
        setIntent(activity, intent, info);
        setMinDimensions(info);
        this.mAtmService.getTaskChangeNotificationController().notifyTaskCreated(this.mTaskId, this.realActivity);
        return this;
    }

    private void cleanUpResourcesForDestroy(com.android.server.wm.WindowContainer<?> oldParent) {
        if (hasChild()) {
            return;
        }
        saveLaunchingStateIfNeeded(oldParent.getDisplayContent());
        boolean isVoiceSession = this.voiceSession != null;
        if (isVoiceSession) {
            try {
                this.voiceSession.taskFinished(this.intent, this.mTaskId);
            } catch (android.os.RemoteException e) {
            }
        }
        if (shouldAutoRemoveFromRecents(oldParent.asTaskFragment()) || isVoiceSession) {
            this.mTaskSupervisor.mRecentTasks.remove(this);
        }
        removeIfPossible("cleanUpResourcesForDestroy");
    }

    @Override // com.android.server.wm.WindowContainer
    void removeIfPossible() {
        removeIfPossible("removeTaskIfPossible");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeIfPossible(java.lang.String reason) {
        boolean isRootTask = isRootTask();
        if (!isRootTask || !this.mAtmService.getLockTaskController().mLockTaskControllerExt.isLockDeviceMode()) {
            this.mAtmService.getLockTaskController().clearLockedTask(this);
        }
        if (shouldDeferRemoval()) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ROOT_TASK) {
                android.util.Slog.i(TAG, "removeTask:" + reason + " deferring removing taskId=" + this.mTaskId);
                return;
            }
            return;
        }
        boolean isLeafTask = isLeafTask();
        removeImmediately(reason);
        if (isLeafTask) {
            this.mAtmService.getTaskChangeNotificationController().notifyTaskRemoved(this.mTaskId);
            com.android.server.wm.TaskDisplayArea taskDisplayArea = getDisplayArea();
            if (taskDisplayArea != null) {
                taskDisplayArea.onLeafTaskRemoved(this.mTaskId);
            }
        }
        this.mTaskWrapper.getExtImpl().onTaskRemoved(this);
    }

    void setResizeMode(int resizeMode) {
        if (this.mResizeMode == resizeMode) {
            return;
        }
        this.mResizeMode = resizeMode;
        this.mRootWindowContainer.ensureActivitiesVisible();
        this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        updateTaskDescription();
    }

    boolean resize(android.graphics.Rect bounds, int resizeMode, boolean preserveWindow) {
        com.android.server.wm.ActivityRecord r;
        this.mAtmService.deferWindowLayout();
        boolean forced = (resizeMode & 2) != 0;
        try {
            if (getParent() == null) {
                setBounds(bounds);
                if (!inFreeformWindowingMode()) {
                    this.mTaskSupervisor.restoreRecentTaskLocked(this, null, false);
                }
                return true;
            }
            if (!canResizeToBounds(bounds)) {
                throw new java.lang.IllegalArgumentException("resizeTask: Can not resize task=" + this + " to bounds=" + bounds + " resizeMode=" + this.mResizeMode);
            }
            android.os.Trace.traceBegin(32L, "resizeTask_" + this.mTaskId);
            boolean kept = true;
            if (setBounds(bounds, forced) != 0 && (r = topRunningActivityLocked()) != null) {
                kept = r.ensureActivityConfiguration();
                this.mRootWindowContainer.ensureActivitiesVisible(r);
                if (!kept) {
                    this.mRootWindowContainer.resumeFocusedTasksTopActivities();
                }
            }
            saveLaunchingStateIfNeeded();
            android.os.Trace.traceEnd(32L);
            return kept;
        } finally {
            this.mAtmService.continueWindowLayout();
        }
    }

    boolean reparent(com.android.server.wm.Task preferredRootTask, boolean toTop, int moveRootTaskMode, boolean animate, boolean deferResume, java.lang.String reason) {
        return reparent(preferredRootTask, toTop ? Integer.MAX_VALUE : 0, moveRootTaskMode, animate, deferResume, true, reason);
    }

    boolean reparent(com.android.server.wm.Task preferredRootTask, int position, int moveRootTaskMode, boolean animate, boolean deferResume, boolean schedulePictureInPictureModeChange, java.lang.String reason) {
        com.android.server.wm.ActivityTaskSupervisor supervisor = this.mTaskSupervisor;
        com.android.server.wm.RootWindowContainer root = this.mRootWindowContainer;
        com.android.server.wm.WindowManagerService windowManagerService = this.mAtmService.mWindowManager;
        com.android.server.wm.Task sourceRootTask = getRootTask();
        com.android.server.wm.Task toRootTask = supervisor.getReparentTargetRootTask(this, preferredRootTask, position == Integer.MAX_VALUE);
        if (toRootTask == sourceRootTask || !canBeLaunchedOnDisplay(toRootTask.getDisplayId())) {
            return false;
        }
        com.android.server.wm.ActivityRecord topActivity = getTopNonFinishingActivity();
        this.mAtmService.deferWindowLayout();
        try {
            this.mTaskWrapper.getExtImpl().reparentTask(this, toRootTask);
            com.android.server.wm.ActivityRecord r = topRunningActivityLocked();
            boolean wasFocused = r != null && root.isTopDisplayFocusedRootTask(sourceRootTask) && topRunningActivityLocked() == r;
            boolean wasFront = r != null && sourceRootTask.isTopRootTaskInDisplayArea() && sourceRootTask.topRunningActivity() == r;
            boolean moveRootTaskToFront = moveRootTaskMode == 0 || (moveRootTaskMode == 1 && (wasFocused || wasFront));
            reparent(toRootTask, position, moveRootTaskToFront, reason);
            if (schedulePictureInPictureModeChange) {
                supervisor.scheduleUpdatePictureInPictureModeIfNeeded(this, sourceRootTask);
            }
            if (r != null && moveRootTaskToFront) {
                toRootTask.moveToFront(reason);
                if (r.isState(com.android.server.wm.ActivityRecord.State.RESUMED) && r == this.mRootWindowContainer.getTopResumedActivity()) {
                    this.mAtmService.setLastResumedActivityUncheckLocked(r, reason);
                }
            }
            if (!animate) {
                this.mTaskSupervisor.mNoAnimActivities.add(topActivity);
            }
            if (!deferResume) {
                root.ensureActivitiesVisible();
                root.resumeFocusedTasksTopActivities();
            }
            supervisor.handleNonResizableTaskIfNeeded(this, preferredRootTask.getWindowingMode(), this.mRootWindowContainer.getDefaultTaskDisplayArea(), toRootTask);
            return preferredRootTask == toRootTask;
        } finally {
            this.mAtmService.continueWindowLayout();
        }
    }

    void touchActiveTime() {
        this.lastActiveTime = android.os.SystemClock.elapsedRealtime();
    }

    long getInactiveDuration() {
        return android.os.SystemClock.elapsedRealtime() - this.lastActiveTime;
    }

    void setIntent(com.android.server.wm.ActivityRecord r) {
        setIntent(r, null, null);
    }

    void setIntent(com.android.server.wm.ActivityRecord r, android.content.Intent intent, android.content.pm.ActivityInfo info) {
        if (r == null) {
            return;
        }
        boolean updateIdentity = false;
        if (this.intent == null) {
            updateIdentity = true;
        } else if (!this.mNeverRelinquishIdentity) {
            android.content.pm.ActivityInfo activityInfo = info != null ? info : r.info;
            updateIdentity = this.effectiveUid == 1000 || this.mIsEffectivelySystemApp || this.effectiveUid == activityInfo.applicationInfo.uid;
        }
        if (updateIdentity) {
            this.mCallingUid = r.launchedFromUid;
            this.mCallingPackage = r.launchedFromPackage;
            this.mCallingFeatureId = r.launchedFromFeatureId;
            setIntent(intent != null ? intent : r.intent, info != null ? info : r.info);
        }
        this.mTaskWrapper.getExtImpl().onSetTaskIntent(this, info != null ? info : r.info, updateIdentity);
        setLockTaskAuth(r);
    }

    private void setIntent(android.content.Intent _intent, android.content.pm.ActivityInfo info) {
        if (isLeafTask()) {
            this.mNeverRelinquishIdentity = (info.flags & 4096) == 0;
            this.affinity = info.taskAffinity;
            if (this.intent == null) {
                this.rootAffinity = this.affinity;
                this.mRequiredDisplayCategory = info.requiredDisplayCategory;
            }
            this.effectiveUid = info.applicationInfo.uid;
            this.mIsEffectivelySystemApp = info.applicationInfo.isSystemApp();
            if (info.targetActivity == null) {
                if (_intent != null && (_intent.getSelector() != null || _intent.getSourceBounds() != null)) {
                    _intent = new android.content.Intent(_intent);
                    _intent.setSelector(null);
                    _intent.setSourceBounds(null);
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(_intent);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -8609432747982701423L, 0, null, protoLogParam0, protoLogParam1);
                }
                this.intent = _intent;
                this.realActivity = _intent != null ? _intent.getComponent() : null;
                this.origActivity = null;
            } else {
                android.content.ComponentName targetComponent = new android.content.ComponentName(info.packageName, info.targetActivity);
                if (_intent != null) {
                    android.content.Intent targetIntent = new android.content.Intent(_intent);
                    targetIntent.setSelector(null);
                    targetIntent.setSourceBounds(null);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[1]) {
                        java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                        java.lang.String protoLogParam12 = java.lang.String.valueOf(targetIntent);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -9155008290180285590L, 0, null, protoLogParam02, protoLogParam12);
                    }
                    this.intent = targetIntent;
                    this.realActivity = targetComponent;
                    this.origActivity = _intent.getComponent();
                } else {
                    this.intent = null;
                    this.realActivity = targetComponent;
                    this.origActivity = new android.content.ComponentName(info.packageName, info.name);
                }
            }
            this.mTaskFragmentExt.addTask(getParent(), this, _intent, info);
            this.mWindowLayoutAffinity = info.windowLayout == null ? null : info.windowLayout.windowLayoutAffinity;
            int intentFlags = this.intent == null ? 0 : this.intent.getFlags();
            if ((2097152 & intentFlags) != 0) {
                this.rootWasReset = true;
            }
            this.mUserId = android.os.UserHandle.getUserId(info.applicationInfo.uid);
            this.mUserSetupComplete = android.provider.Settings.Secure.getIntForUser(this.mAtmService.mContext.getContentResolver(), ATTR_USER_SETUP_COMPLETE, 0, this.mUserId) != 0;
            if ((info.flags & 8192) != 0) {
                this.autoRemoveRecents = true;
            } else if ((532480 & intentFlags) != 524288 || info.documentLaunchMode != 0) {
                this.autoRemoveRecents = false;
            } else {
                this.autoRemoveRecents = true;
            }
            if (this.mResizeMode != info.resizeMode) {
                this.mResizeMode = info.resizeMode;
                updateTaskDescription();
            }
            this.mSupportsPictureInPicture = info.supportsPictureInPicture();
            this.stringName = null;
            if (this.inRecents) {
                this.mTaskSupervisor.mRecentTasks.remove(this);
                this.mTaskSupervisor.mRecentTasks.add(this);
            }
        }
    }

    void setMinDimensions(android.content.pm.ActivityInfo info) {
        if (info != null && info.windowLayout != null) {
            this.mMinWidth = info.windowLayout.minWidth;
            this.mMinHeight = info.windowLayout.minHeight;
        } else {
            this.mMinWidth = -1;
            this.mMinHeight = -1;
        }
    }

    boolean isSameIntentFilter(com.android.server.wm.ActivityRecord r) {
        android.content.Intent intent = new android.content.Intent(r.intent);
        if (java.util.Objects.equals(this.realActivity, r.mActivityComponent) && this.intent != null) {
            intent.setComponent(this.intent.getComponent());
            if (intent.getSelector() == null) {
                intent.setPackage(this.intent.getPackage());
            }
        }
        return intent.filterEquals(this.intent);
    }

    boolean returnsToHomeRootTask() {
        if (inMultiWindowMode() || !hasChild() || getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
            return false;
        }
        if (this.intent != null && !android.app.WindowConfiguration.sExtImpl.isWindowingZoomMode(getWindowingMode())) {
            if ((this.intent.getFlags() & 268451840) != 268451840) {
                return false;
            }
            com.android.server.wm.Task task = getDisplayArea() != null ? getDisplayArea().getRootHomeTask() : null;
            return task == null || !this.mAtmService.getLockTaskController().isLockTaskModeViolation(task);
        }
        com.android.server.wm.Task bottomTask = getBottomMostTask();
        return bottomTask != this && bottomTask.returnsToHomeRootTask();
    }

    void setPrevAffiliate(com.android.server.wm.Task prevAffiliate) {
        this.mPrevAffiliate = prevAffiliate;
        this.mPrevAffiliateTaskId = prevAffiliate == null ? -1 : prevAffiliate.mTaskId;
    }

    void setNextAffiliate(com.android.server.wm.Task nextAffiliate) {
        this.mNextAffiliate = nextAffiliate;
        this.mNextAffiliateTaskId = nextAffiliate == null ? -1 : nextAffiliate.mTaskId;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    void onParentChanged(com.android.server.wm.ConfigurationContainer rawNewParent, com.android.server.wm.ConfigurationContainer rawOldParent) {
        com.android.server.wm.WindowContainer<?> newParent = (com.android.server.wm.WindowContainer) rawNewParent;
        com.android.server.wm.WindowContainer<?> oldParent = (com.android.server.wm.WindowContainer) rawOldParent;
        com.android.server.wm.DisplayContent display = newParent != null ? newParent.getDisplayContent() : null;
        com.android.server.wm.DisplayContent oldDisplay = oldParent != null ? oldParent.getDisplayContent() : null;
        this.mPrevDisplayId = oldDisplay != null ? oldDisplay.mDisplayId : -1;
        if (oldParent != null && newParent == null) {
            cleanUpResourcesForDestroy(oldParent);
        }
        if (display != null) {
            getConfiguration().windowConfiguration.setRotation(display.getWindowConfiguration().getRotation());
        }
        super.onParentChanged(newParent, oldParent);
        this.mTaskWrapper.getExtImpl().onTaskParentChanged(oldDisplay, display, oldParent, newParent, this);
        this.mTaskWrapper.getExtImpl().onTaskParentChanged(oldParent, newParent, this);
        updateTaskOrganizerState();
        if (getParent() == null && this.mDisplayContent != null) {
            this.mDisplayContent = null;
            this.mWmService.mWindowPlacerLocked.requestTraversal();
        }
        if (oldParent != null) {
            final com.android.server.wm.Task oldParentTask = oldParent.asTask();
            if (oldParentTask != null) {
                java.util.Objects.requireNonNull(oldParentTask);
                forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda40
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.cleanUpActivityReferences((com.android.server.wm.ActivityRecord) obj);
                    }
                });
            }
            if (newParent == null || !newParent.inPinnedWindowingMode()) {
                if (oldParent.inPinnedWindowingMode()) {
                    this.mRootWindowContainer.notifyActivityPipModeChanged(this, null);
                } else if (inPinnedWindowingMode()) {
                    android.util.Slog.e(TAG, "Pinned task is removed t=" + this);
                    this.mRootWindowContainer.notifyActivityPipModeChanged(this, null);
                }
            }
        }
        if (newParent != null) {
            if (!this.mCreatedByOrganizer && !canBeOrganized()) {
                getSyncTransaction().show(this.mSurfaceControl);
            }
            if (this.voiceSession != null) {
                try {
                    this.voiceSession.taskStarted(this.intent, this.mTaskId);
                } catch (android.os.RemoteException e) {
                }
            }
        }
        if (oldParent == null && newParent != null) {
            updateOverrideConfigurationFromLaunchBounds();
        }
        this.mRootWindowContainer.updateUIDsPresentOnDisplay();
        forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda41
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.ActivityRecord) obj).updateAnimatingActivityRegistry();
            }
        });
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.wm.TaskFragment
    com.android.server.wm.ActivityRecord getTopResumedActivity() {
        com.android.server.wm.ActivityRecord resumedActivity;
        if (!isLeafTask()) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                com.android.server.wm.Task task = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask();
                if (task != null && (resumedActivity = task.getTopResumedActivity()) != null) {
                    return resumedActivity;
                }
            }
        }
        com.android.server.wm.ActivityRecord taskResumedActivity = getResumedActivity();
        com.android.server.wm.ActivityRecord topResumedActivity = null;
        for (int i2 = this.mChildren.size() - 1; i2 >= 0; i2--) {
            com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(i2);
            if (child.asTaskFragment() != null) {
                topResumedActivity = child.asTaskFragment().getTopResumedActivity();
            } else if (taskResumedActivity != null && child.asActivityRecord() == taskResumedActivity) {
                topResumedActivity = taskResumedActivity;
            }
            if (topResumedActivity != null) {
                return topResumedActivity;
            }
        }
        return null;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.wm.TaskFragment
    com.android.server.wm.ActivityRecord getTopPausingActivity() {
        if (!isLeafTask()) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                com.android.server.wm.ActivityRecord pausingActivity = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask().getTopPausingActivity();
                if (pausingActivity != null) {
                    return pausingActivity;
                }
            }
        }
        com.android.server.wm.ActivityRecord taskPausingActivity = getPausingActivity();
        com.android.server.wm.ActivityRecord topPausingActivity = null;
        for (int i2 = this.mChildren.size() - 1; i2 >= 0; i2--) {
            com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(i2);
            if (child.asTaskFragment() != null) {
                topPausingActivity = child.asTaskFragment().getTopPausingActivity();
            } else if (taskPausingActivity != null && child.asActivityRecord() == taskPausingActivity) {
                topPausingActivity = taskPausingActivity;
            }
            if (topPausingActivity != null) {
                return topPausingActivity;
            }
        }
        return null;
    }

    boolean pauseActivityIfNeeded(final com.android.server.wm.ActivityRecord resuming, final java.lang.String reason) {
        if (!isLeafTask()) {
            return false;
        }
        final int[] someActivityPaused = {0};
        if (!isLeafTaskFragment()) {
            com.android.server.wm.ActivityRecord top = topRunningActivity();
            com.android.server.wm.ActivityRecord resumedActivity = getResumedActivity();
            if (resumedActivity != null && ((top == null || top.getTaskFragment() != this || !canBeResumed(resuming)) && startPausing(false, resuming, reason))) {
                someActivityPaused[0] = someActivityPaused[0] + 1;
            }
        }
        forAllLeafTaskFragments(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.Task.lambda$pauseActivityIfNeeded$0(resuming, reason, someActivityPaused, (com.android.server.wm.TaskFragment) obj);
            }
        }, true);
        return someActivityPaused[0] > 0;
    }

    static /* synthetic */ void lambda$pauseActivityIfNeeded$0(com.android.server.wm.ActivityRecord resuming, java.lang.String reason, int[] someActivityPaused, com.android.server.wm.TaskFragment taskFrag) {
        com.android.server.wm.ActivityRecord resumedActivity = taskFrag.getResumedActivity();
        if (resumedActivity != null && !taskFrag.canBeResumed(resuming) && taskFrag.startPausing(false, resuming, reason)) {
            someActivityPaused[0] = someActivityPaused[0] + 1;
        }
    }

    void updateTaskMovement(boolean z, boolean z2, int i) {
        com.android.server.wm.EventLogTags.writeWmTaskMoved(this.mTaskId, getRootTaskId(), getDisplayId(), z ? 1 : 0, i);
        com.android.server.wm.TaskDisplayArea displayArea = getDisplayArea();
        if (displayArea != null && isLeafTask()) {
            displayArea.onLeafTaskMoved(this, z, z2);
        }
        if (this.isPersistable) {
            this.mLastTimeMoved = java.lang.System.currentTimeMillis();
        }
        if (z && this.inRecents) {
            this.mTaskSupervisor.mRecentTasks.add(this);
        }
    }

    private void closeRecentsChain() {
        if (this.mPrevAffiliate != null) {
            this.mPrevAffiliate.setNextAffiliate(this.mNextAffiliate);
        }
        if (this.mNextAffiliate != null) {
            this.mNextAffiliate.setPrevAffiliate(this.mPrevAffiliate);
        }
        setPrevAffiliate(null);
        setNextAffiliate(null);
    }

    void removedFromRecents() {
        closeRecentsChain();
        if (this.inRecents) {
            this.inRecents = false;
            this.mAtmService.notifyTaskPersisterLocked(this, false);
        }
        clearRootProcess();
        this.mAtmService.mWindowManager.mTaskSnapshotController.removeAndDeleteSnapshot(this.mTaskId, this.mUserId);
    }

    void setTaskToAffiliateWith(com.android.server.wm.Task taskToAffiliateWith) {
        closeRecentsChain();
        this.mAffiliatedTaskId = taskToAffiliateWith.mAffiliatedTaskId;
        while (true) {
            if (taskToAffiliateWith.mNextAffiliate == null) {
                break;
            }
            com.android.server.wm.Task nextRecents = taskToAffiliateWith.mNextAffiliate;
            if (nextRecents.mAffiliatedTaskId != this.mAffiliatedTaskId) {
                android.util.Slog.e(TAG, "setTaskToAffiliateWith: nextRecents=" + nextRecents + " affilTaskId=" + nextRecents.mAffiliatedTaskId + " should be " + this.mAffiliatedTaskId);
                if (nextRecents.mPrevAffiliate == taskToAffiliateWith) {
                    nextRecents.setPrevAffiliate(null);
                }
                taskToAffiliateWith.setNextAffiliate(null);
            } else {
                taskToAffiliateWith = nextRecents;
            }
        }
        taskToAffiliateWith.setNextAffiliate(this);
        setPrevAffiliate(taskToAffiliateWith);
        setNextAffiliate(null);
    }

    android.content.Intent getBaseIntent() {
        if (this.intent != null) {
            return this.intent;
        }
        if (this.affinityIntent != null) {
            return this.affinityIntent;
        }
        com.android.server.wm.Task topTask = getTopMostTask();
        if (topTask == this || topTask == null) {
            return null;
        }
        return topTask.getBaseIntent();
    }

    java.lang.String getBasePackageName() {
        android.content.ComponentName componentName;
        android.content.Intent intent = getBaseIntent();
        return (intent == null || (componentName = intent.getComponent()) == null) ? "" : componentName.getPackageName();
    }

    com.android.server.wm.ActivityRecord getRootActivity() {
        return getRootActivity(true, false);
    }

    com.android.server.wm.ActivityRecord getRootActivity(boolean setToBottomIfNone) {
        return getRootActivity(false, setToBottomIfNone);
    }

    com.android.server.wm.ActivityRecord getRootActivity(boolean ignoreRelinquishIdentity, boolean setToBottomIfNone) {
        return this.mFindRootHelper.findRoot(ignoreRelinquishIdentity, setToBottomIfNone);
    }

    com.android.server.wm.ActivityRecord topRunningActivityLocked() {
        if (getParent() == null) {
            return null;
        }
        return getActivity(new com.android.server.wm.ActivityStarter$$ExternalSyntheticLambda0());
    }

    boolean isUidPresent(int uid) {
        com.android.internal.util.function.pooled.PooledPredicate p = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new com.android.server.wm.DisplayContent$$ExternalSyntheticLambda11(), com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.ActivityRecord.class), java.lang.Integer.valueOf(uid));
        boolean isUidPresent = getActivity(p) != null;
        p.recycle();
        return isUidPresent;
    }

    static /* synthetic */ boolean lambda$topStartingWindow$1(com.android.server.wm.WindowState w) {
        return w.mAttrs.type == 3;
    }

    com.android.server.wm.WindowState topStartingWindow() {
        return getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda28
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Task.lambda$topStartingWindow$1((com.android.server.wm.WindowState) obj);
            }
        });
    }

    com.android.server.wm.ActivityRecord topActivityContainsStartingWindow() {
        com.android.server.wm.WindowState startingWindow = topStartingWindow();
        if (startingWindow != null) {
            return startingWindow.mActivityRecord;
        }
        return null;
    }

    final boolean moveActivityToFront(com.android.server.wm.ActivityRecord newTop) {
        boolean moved;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(newTop);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(android.os.Debug.getCallers(4));
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 6424220442758232673L, 0, null, protoLogParam0, protoLogParam1);
        }
        com.android.server.wm.TaskFragment taskFragment = newTop.getTaskFragment();
        if (taskFragment != this) {
            if (taskFragment.isEmbedded() && taskFragment.getNonFinishingActivityCount() == 1) {
                taskFragment.mClearedForReorderActivityToFront = true;
            }
            if (taskFragment.mTaskFragmentExt.isCreateForMagicWindow()) {
                taskFragment.positionChildAt(Integer.MAX_VALUE, newTop, false);
            } else {
                newTop.reparent(this, Integer.MAX_VALUE);
            }
            moved = true;
            if (taskFragment.isEmbedded()) {
                this.mAtmService.mWindowOrganizerController.mTaskFragmentOrganizerController.onActivityReparentedToTask(newTop);
            }
        } else {
            moved = moveChildToFront(newTop);
        }
        updateEffectiveIntent();
        return moved;
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void addChild(com.android.server.wm.WindowContainer child, int index) {
        super.addChild(child, getAdjustedChildPosition(child, index));
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -1028890010429408946L, 0, null, protoLogParam0);
        }
        if (this.mTaskOrganizer != null && this.mCreatedByOrganizer && child.asTask() != null) {
            getDisplayArea().addRootTaskReferenceIfNeeded((com.android.server.wm.Task) child);
        }
        this.mTaskWrapper.getExtImpl().addChild(child);
        this.mRootWindowContainer.updateUIDsPresentOnDisplay();
        com.android.server.wm.TaskFragment childTaskFrag = child.asTaskFragment();
        if (childTaskFrag != null && childTaskFrag.asTask() == null) {
            if (childTaskFrag.mTaskFragmentOrganizerProcessName != null && this.mTaskFragmentHostProcessName == null) {
                this.mTaskFragmentHostUid = childTaskFrag.mTaskFragmentOrganizerUid;
                this.mTaskFragmentHostProcessName = childTaskFrag.mTaskFragmentOrganizerProcessName;
            }
            childTaskFrag.setMinDimensions(this.mMinWidth, this.mMinHeight);
            com.android.server.wm.ActivityRecord top = getTopMostActivity();
            if (top != null) {
                top.associateStartingWindowWithTaskIfNeeded();
            }
        }
    }

    void onDescendantActivityAdded(boolean hadActivity, int activityType, com.android.server.wm.ActivityRecord r) {
        warnForNonLeafTask("onDescendantActivityAdded");
        if (!hadActivity) {
            int activityOverrideType = r.getRequestedOverrideConfiguration().windowConfiguration.getActivityType();
            if (activityOverrideType == 0) {
                activityOverrideType = activityType != 0 ? activityType : 1;
                r.getRequestedOverrideConfiguration().windowConfiguration.setActivityType(activityOverrideType);
            }
            setActivityType(activityOverrideType);
            this.isPersistable = r.isPersistable();
            this.mCallingUid = r.launchedFromUid;
            this.mCallingPackage = r.launchedFromPackage;
            this.mCallingFeatureId = r.launchedFromFeatureId;
            this.maxRecents = java.lang.Math.min(java.lang.Math.max(r.info.maxRecents, 1), android.app.ActivityTaskManager.getMaxAppRecentsLimitStatic());
        } else {
            r.setActivityType(activityType);
        }
        updateEffectiveIntent();
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void removeChild(com.android.server.wm.WindowContainer child) {
        removeChild(child, "removeChild");
        this.mTaskFragmentExt.removeChild(child);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeChild(com.android.server.wm.WindowContainer r, java.lang.String reason) {
        if (this.mCreatedByOrganizer && r.asTask() != null) {
            getDisplayArea().removeRootTaskReferenceIfNeeded((com.android.server.wm.Task) r);
        }
        if (!this.mChildren.contains(r)) {
            android.util.Slog.e(TAG, "removeChild: r=" + r + " not found in t=" + this);
            return;
        }
        this.mTaskWrapper.getExtImpl().removeChild(r);
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_MOVEMENT) {
            android.util.Slog.d("WindowManager", "removeChild: child=" + r + " reason=" + reason);
        }
        super.removeChild(r, false);
        if (inPinnedWindowingMode()) {
            this.mAtmService.getTaskChangeNotificationController().notifyTaskStackChanged();
        }
        if (this.mDecorSurfaceContainer != null && r == this.mDecorSurfaceContainer.mOwnerTaskFragment) {
            removeDecorSurface();
        }
        if (hasChild()) {
            updateEffectiveIntent();
            if (onlyHasTaskOverlayActivities(true)) {
                this.mTaskSupervisor.removeTask(this, false, false, reason);
                return;
            }
            return;
        }
        if (!this.mReuseTask && shouldRemoveSelfOnLastChildRemoval() && !this.mTaskWrapper.mTaskExt.isTaskInreParent()) {
            removeIfPossible(reason + ", last child = " + r + " in " + this);
        }
    }

    boolean onlyHasTaskOverlayActivities(boolean includeFinishing) {
        int count = 0;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord r = getChildAt(i).asActivityRecord();
            if (r == null) {
                return false;
            }
            if (includeFinishing || !r.finishing) {
                if (!r.isTaskOverlay()) {
                    return false;
                }
                count++;
            }
        }
        return count > 0;
    }

    private boolean shouldAutoRemoveFromRecents(com.android.server.wm.TaskFragment oldParentFragment) {
        return this.autoRemoveRecents || !(hasChild() || getHasBeenVisible()) || ((oldParentFragment != null && oldParentFragment.isEmbedded()) || (!(this.mDisplayContent == null || this.mDisplayContent.canShowTasksInHostDeviceRecents()) || this.mTaskWrapper.getExtImpl().isEmptyTask()));
    }

    private void clearPinnedTaskIfNeed() {
        if (this.mChildPipActivity != null && this.mChildPipActivity.getTask() != null) {
            this.mTaskSupervisor.removeRootTask(this.mChildPipActivity.getTask());
        }
    }

    void removeActivities(final java.lang.String reason, final boolean excludingTaskOverlay) {
        clearPinnedTaskIfNeed();
        if (getRootTask() == null) {
            forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda18
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$removeActivities$2(excludingTaskOverlay, reason, (com.android.server.wm.ActivityRecord) obj);
                }
            });
            return;
        }
        final java.util.ArrayList<com.android.server.wm.ActivityRecord> finishingActivities = new java.util.ArrayList<>();
        forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.Task.lambda$removeActivities$3(excludingTaskOverlay, finishingActivities, (com.android.server.wm.ActivityRecord) obj);
            }
        });
        for (int i = finishingActivities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord r = finishingActivities.get(i);
            if (r.isState(com.android.server.wm.ActivityRecord.State.RESUMED) || (r.isVisible() && this.mDisplayContent != null && !this.mDisplayContent.mAppTransition.containsTransitRequest(2))) {
                r.finishIfPossible(reason, false);
            } else {
                r.destroyIfPossible(reason);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeActivities$2(boolean excludingTaskOverlay, java.lang.String reason, com.android.server.wm.ActivityRecord r) {
        if (r.finishing) {
            return;
        }
        if (excludingTaskOverlay && r.isTaskOverlay()) {
            return;
        }
        r.takeFromHistory();
        removeChild(r, reason);
    }

    static /* synthetic */ void lambda$removeActivities$3(boolean excludingTaskOverlay, java.util.ArrayList finishingActivities, com.android.server.wm.ActivityRecord r) {
        if (r.finishing) {
            return;
        }
        if (excludingTaskOverlay && r.isTaskOverlay()) {
            return;
        }
        finishingActivities.add(r);
    }

    void performClearTaskForReuse(boolean excludingTaskOverlay) {
        this.mReuseTask = true;
        this.mTaskSupervisor.beginDeferResume();
        try {
            removeActivities("clear-task-all", excludingTaskOverlay);
        } finally {
            this.mTaskSupervisor.endDeferResume();
            this.mReuseTask = false;
        }
    }

    com.android.server.wm.ActivityRecord performClearTop(com.android.server.wm.ActivityRecord newR, int launchFlags, int[] finishCount) {
        this.mReuseTask = true;
        this.mTaskSupervisor.beginDeferResume();
        try {
            com.android.server.wm.ActivityRecord result = clearTopActivities(newR, launchFlags, finishCount);
            return result;
        } finally {
            this.mTaskSupervisor.endDeferResume();
            this.mReuseTask = false;
        }
    }

    private com.android.server.wm.ActivityRecord clearTopActivities(com.android.server.wm.ActivityRecord newR, int launchFlags, final int[] finishCount) {
        com.android.server.wm.ActivityRecord r = findActivityInHistory(newR.mActivityComponent, newR.mUserId);
        if (r == null) {
            return null;
        }
        if (this.mTaskWrapper.getExtImpl().canClearActivityRecord(r)) {
            if (r.isVisible()) {
                return null;
            }
            return r;
        }
        moveTaskFragmentsToBottomIfNeeded(r, finishCount);
        com.android.internal.util.function.pooled.PooledPredicate f = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new java.util.function.BiPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda10
            @Override // java.util.function.BiPredicate
            public final boolean test(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.wm.Task.finishActivityAbove((com.android.server.wm.ActivityRecord) obj, (com.android.server.wm.ActivityRecord) obj2, finishCount);
            }
        }, com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.ActivityRecord.class), r);
        forAllActivities((java.util.function.Predicate<com.android.server.wm.ActivityRecord>) f);
        f.recycle();
        if (r.launchMode == 0 && (536870912 & launchFlags) == 0 && !com.android.server.wm.ActivityStarter.isDocumentLaunchesIntoExisting(launchFlags) && !r.finishing) {
            r.finishIfPossible("clear-task-top", false);
        }
        this.mTaskWrapper.getExtImpl().applyNewOrientationWhenReuseIfNeed(r, newR);
        return r;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    void moveTaskFragmentsToBottomIfNeeded(com.android.server.wm.ActivityRecord r, int[] finishCount) {
        int activityIndex = this.mChildren.indexOf(r);
        if (activityIndex < 0) {
            return;
        }
        java.util.List<com.android.server.wm.TaskFragment> taskFragmentsToMove = null;
        for (int i = this.mChildren.size() - 1; i > activityIndex; i--) {
            com.android.server.wm.TaskFragment taskFragment = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTaskFragment();
            if (taskFragment != null && taskFragment.isMoveToBottomIfClearWhenLaunch()) {
                if (taskFragmentsToMove == null) {
                    taskFragmentsToMove = new java.util.ArrayList<>();
                }
                taskFragmentsToMove.add(taskFragment);
            }
        }
        if (taskFragmentsToMove == null) {
            return;
        }
        int size = taskFragmentsToMove.size();
        for (int i2 = 0; i2 < size; i2++) {
            com.android.server.wm.TaskFragment taskFragment2 = taskFragmentsToMove.get(i2);
            this.mTransitionController.collect(taskFragment2);
            positionChildAt(Integer.MIN_VALUE, taskFragment2, false);
        }
        int i3 = finishCount[0];
        finishCount[0] = i3 + size;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean finishActivityAbove(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord boundaryActivity, int[] finishCount) {
        if (r == boundaryActivity) {
            return true;
        }
        if (!r.finishing && !r.isTaskOverlay()) {
            android.app.ActivityOptions opts = r.getOptions();
            if (opts != null) {
                r.clearOptionsAnimation();
                boundaryActivity.updateOptionsLocked(opts);
            }
            if (r.getWrapper().getExtImpl().performClearTaskLocked(r, boundaryActivity)) {
                finishCount[0] = finishCount[0] + 1;
                r.finishIfPossible("clear-task-stack", false);
            }
        }
        return false;
    }

    java.lang.String lockTaskAuthToString() {
        switch (this.mLockTaskAuth) {
            case 0:
                return "LOCK_TASK_AUTH_DONT_LOCK";
            case 1:
                return "LOCK_TASK_AUTH_PINNABLE";
            case 2:
                return "LOCK_TASK_AUTH_LAUNCHABLE";
            case 3:
                return "LOCK_TASK_AUTH_ALLOWLISTED";
            case 4:
                return "LOCK_TASK_AUTH_LAUNCHABLE_PRIV";
            default:
                return "unknown=" + this.mLockTaskAuth;
        }
    }

    void setLockTaskAuth() {
        setLockTaskAuth(getRootActivity());
    }

    private void setLockTaskAuth(com.android.server.wm.ActivityRecord r) {
        this.mLockTaskAuth = this.mAtmService.getLockTaskController().getLockTaskAuth(r, this);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_LOCKTASK_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(lockTaskAuthToString());
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_LOCKTASK, 38991867929900764L, 0, null, protoLogParam0, protoLogParam1);
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean supportsSplitScreenWindowingMode() {
        return supportsSplitScreenWindowingModeInDisplayArea(getDisplayArea());
    }

    boolean supportsSplitScreenWindowingModeInDisplayArea(com.android.server.wm.TaskDisplayArea tda) {
        com.android.server.wm.Task topTask = getTopMostTask();
        return super.supportsSplitScreenWindowingMode() && (topTask == null || topTask.supportsSplitScreenWindowingModeInner(tda));
    }

    private boolean supportsSplitScreenWindowingModeInner(com.android.server.wm.TaskDisplayArea tda) {
        return super.supportsSplitScreenWindowingMode() && this.mAtmService.mSupportsSplitScreenMultiWindow && this.mTaskWrapper.getExtImpl().supportsSplitScreenByVendorPolicy(this, supportsMultiWindowInDisplayArea(tda));
    }

    boolean supportsFreeform() {
        return supportsFreeformInDisplayArea(getDisplayArea());
    }

    boolean supportsFreeformInDisplayArea(com.android.server.wm.TaskDisplayArea tda) {
        return this.mAtmService.mSupportsFreeformWindowManagement && supportsMultiWindowInDisplayArea(tda);
    }

    boolean canBeLaunchedOnDisplay(int displayId) {
        return this.mTaskSupervisor.canPlaceEntityOnDisplay(displayId, -1, -1, this);
    }

    private boolean canResizeToBounds(android.graphics.Rect bounds) {
        if (bounds == null || !inFreeformWindowingMode()) {
            return true;
        }
        boolean landscape = bounds.width() > bounds.height();
        android.graphics.Rect configBounds = getRequestedOverrideBounds();
        if (this.mResizeMode != 7) {
            return !(this.mResizeMode == 6 && landscape) && (this.mResizeMode != 5 || landscape);
        }
        if (configBounds.isEmpty()) {
            return true;
        }
        return landscape == (configBounds.width() > configBounds.height());
    }

    boolean isClearingToReuseTask() {
        return this.mReuseTask;
    }

    com.android.server.wm.ActivityRecord findActivityInHistory(android.content.ComponentName component, int userId) {
        com.android.internal.util.function.pooled.PooledPredicate p = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new com.android.internal.util.function.TriPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda35
            public final boolean test(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                return com.android.server.wm.Task.matchesActivityInHistory((com.android.server.wm.ActivityRecord) obj, (android.content.ComponentName) obj2, ((java.lang.Integer) obj3).intValue());
            }
        }, com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.ActivityRecord.class), component, java.lang.Integer.valueOf(userId));
        com.android.server.wm.ActivityRecord r = getActivity(p);
        p.recycle();
        return r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean matchesActivityInHistory(com.android.server.wm.ActivityRecord r, android.content.ComponentName activityComponent, int userId) {
        return !r.finishing && r.mActivityComponent.equals(activityComponent) && r.mUserId == userId;
    }

    void updateTaskDescription() {
        com.android.server.wm.Task t;
        com.android.server.wm.ActivityRecord root = getRootActivity(true);
        if (root == null) {
            return;
        }
        android.app.ActivityManager.TaskDescription taskDescription = new android.app.ActivityManager.TaskDescription();
        com.android.internal.util.function.pooled.PooledPredicate f = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new com.android.internal.util.function.TriPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda17
            public final boolean test(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                return com.android.server.wm.Task.setTaskDescriptionFromActivityAboveRoot((com.android.server.wm.ActivityRecord) obj, (com.android.server.wm.ActivityRecord) obj2, (android.app.ActivityManager.TaskDescription) obj3);
            }
        }, com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.ActivityRecord.class), root, taskDescription);
        forAllActivities((java.util.function.Predicate<com.android.server.wm.ActivityRecord>) f);
        f.recycle();
        taskDescription.setResizeMode(this.mResizeMode);
        taskDescription.setMinWidth(this.mMinWidth);
        taskDescription.setMinHeight(this.mMinHeight);
        setTaskDescription(taskDescription);
        this.mAtmService.getTaskChangeNotificationController().notifyTaskDescriptionChanged(getTaskInfo());
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent != null && (t = parent.asTask()) != null) {
            t.updateTaskDescription();
        }
        dispatchTaskInfoChangedIfNeeded(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean setTaskDescriptionFromActivityAboveRoot(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord root, android.app.ActivityManager.TaskDescription td) {
        if (!r.isTaskOverlay() && r.taskDescription != null) {
            android.app.ActivityManager.TaskDescription atd = r.taskDescription;
            if (td.getLabel() == null) {
                td.setLabel(atd.getLabel());
            }
            if (td.getRawIcon() == null) {
                td.setIcon(atd.getRawIcon());
            }
            if (td.getIconFilename() == null) {
                td.setIconFilename(atd.getIconFilename());
            }
            if (td.getPrimaryColor() == 0) {
                td.setPrimaryColor(atd.getPrimaryColor());
            }
            if (td.getBackgroundColor() == 0) {
                td.setBackgroundColor(atd.getBackgroundColor());
            }
            if (td.getStatusBarColor() == 0) {
                td.setStatusBarColor(atd.getStatusBarColor());
                td.setEnsureStatusBarContrastWhenTransparent(atd.getEnsureStatusBarContrastWhenTransparent());
            }
            if (td.getSystemBarsAppearance() == 0) {
                td.setSystemBarsAppearance(atd.getSystemBarsAppearance());
            }
            if (td.getTopOpaqueSystemBarsAppearance() == 0 && r.fillsParent()) {
                td.setTopOpaqueSystemBarsAppearance(atd.getSystemBarsAppearance());
            }
            if (td.getNavigationBarColor() == 0) {
                td.setNavigationBarColor(atd.getNavigationBarColor());
                td.setEnsureNavigationBarContrastWhenTransparent(atd.getEnsureNavigationBarContrastWhenTransparent());
            }
            if (td.getBackgroundColorFloating() == 0) {
                td.setBackgroundColorFloating(atd.getBackgroundColorFloating());
            }
        }
        return r == root;
    }

    void updateEffectiveIntent() {
        com.android.server.wm.ActivityRecord root = getRootActivity(true);
        if (root != null) {
            setIntent(root);
            updateTaskDescription();
        }
    }

    void setLastNonFullscreenBounds(android.graphics.Rect bounds) {
        if (this.mLastNonFullscreenBounds == null) {
            this.mLastNonFullscreenBounds = new android.graphics.Rect(bounds);
        } else {
            this.mLastNonFullscreenBounds.set(bounds);
        }
    }

    private void onConfigurationChangedInner(android.content.res.Configuration newParentConfig) {
        com.android.server.wm.ActivityRecord r;
        boolean prevPersistTaskBounds = getWindowConfiguration().persistTaskBounds();
        boolean nextPersistTaskBounds = getRequestedOverrideConfiguration().windowConfiguration.persistTaskBounds();
        if (getRequestedOverrideWindowingMode() == 0) {
            nextPersistTaskBounds = newParentConfig.windowConfiguration.persistTaskBounds();
        }
        boolean nextPersistTaskBounds2 = nextPersistTaskBounds & (getRequestedOverrideConfiguration().windowConfiguration.getBounds() == null || getRequestedOverrideConfiguration().windowConfiguration.getBounds().isEmpty());
        if (!prevPersistTaskBounds && nextPersistTaskBounds2 && this.mLastNonFullscreenBounds != null && !this.mLastNonFullscreenBounds.isEmpty()) {
            getRequestedOverrideConfiguration().windowConfiguration.setBounds(this.mLastNonFullscreenBounds);
        }
        int prevWinMode = getWindowingMode();
        this.mTmpPrevBounds.set(getBounds());
        boolean wasInMultiWindowMode = inMultiWindowMode();
        boolean wasInPictureInPicture = inPinnedWindowingMode();
        if (!this.mTaskWrapper.getExtImpl().onConfigurationChangedOfTask(newParentConfig, this.mTmpPrevBounds, this)) {
            super.onConfigurationChanged(newParentConfig);
        }
        updateSurfaceSize(getSyncTransaction());
        boolean pipChanging = wasInPictureInPicture != inPinnedWindowingMode();
        if (pipChanging) {
            this.mTaskSupervisor.scheduleUpdatePictureInPictureModeIfNeeded(this, getRootTask());
        } else if (wasInMultiWindowMode != inMultiWindowMode()) {
            this.mTaskSupervisor.scheduleUpdateMultiWindowMode(this);
        }
        int newWinMode = getWindowingMode();
        if (newWinMode != prevWinMode) {
            com.android.server.wm.EventLogTags.writeWmTaskWindowingModeChanged(this.mTaskId, getRootTaskId(), newWinMode, prevWinMode);
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_MOVEMENT) {
                android.util.Slog.d(TAG, java.lang.String.format("newWinMode:%s prevWinMode:%s %s", java.lang.Integer.valueOf(newWinMode), java.lang.Integer.valueOf(prevWinMode), this));
            }
        }
        if (shouldStartChangeTransition(prevWinMode, this.mTmpPrevBounds)) {
            initializeChangeTransition(this.mTmpPrevBounds);
        }
        if (prevWinMode != newWinMode) {
            this.mTaskWrapper.getExtImpl().onTaskWindowingModeChanged(this, prevWinMode, newWinMode);
        }
        if (getWindowConfiguration().persistTaskBounds()) {
            android.graphics.Rect currentBounds = getRequestedOverrideBounds();
            if (!currentBounds.isEmpty()) {
                setLastNonFullscreenBounds(currentBounds);
            }
        }
        if (pipChanging && wasInPictureInPicture && !this.mTransitionController.isShellTransitionsEnabled() && (r = topRunningActivity()) != null && this.mDisplayContent.isFixedRotationLaunchingApp(r)) {
            resetSurfaceControlTransforms();
        }
        saveLaunchingStateIfNeeded();
        boolean taskOrgChanged = updateTaskOrganizerState();
        if (taskOrgChanged) {
            updateSurfacePosition(getSyncTransaction());
            if (!isOrganized()) {
                updateSurfaceSize(getSyncTransaction());
            }
        }
        if (!taskOrgChanged) {
            dispatchTaskInfoChangedIfNeeded(false);
        }
        this.mTaskWrapper.mTaskExt.handleConfigChanged(newParentConfig, this.mTmpRect, this);
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(android.content.res.Configuration newParentConfig) {
        if (this.mDisplayContent != null && this.mDisplayContent.mPinnedTaskController.isFreezingTaskConfig(this)) {
            return;
        }
        if (!isRootTask()) {
            onConfigurationChangedInner(newParentConfig);
            return;
        }
        int prevWindowingMode = getWindowingMode();
        boolean prevIsAlwaysOnTop = isAlwaysOnTop();
        int prevRotation = getWindowConfiguration().getRotation();
        android.graphics.Rect newBounds = this.mTmpRect;
        this.mTaskWrapper.getExtImpl().adjustTaskConfiguration(this, newParentConfig);
        getBounds(newBounds);
        onConfigurationChangedInner(newParentConfig);
        com.android.server.wm.TaskDisplayArea taskDisplayArea = getDisplayArea();
        if (taskDisplayArea == null) {
            return;
        }
        if (prevWindowingMode != getWindowingMode()) {
            taskDisplayArea.onRootTaskWindowingModeChanged(this);
        }
        if (!isOrganized() && !getRequestedOverrideBounds().isEmpty() && this.mDisplayContent != null) {
            int newRotation = getWindowConfiguration().getRotation();
            boolean rotationChanged = prevRotation != newRotation;
            if (rotationChanged) {
                this.mDisplayContent.rotateBounds(prevRotation, newRotation, newBounds);
                setBounds(newBounds);
            }
        }
        if (prevIsAlwaysOnTop != isAlwaysOnTop() && !this.mTaskWrapper.getExtImpl().isPendingToBottomTask(this.mTaskId) && !getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
            taskDisplayArea.positionChildAt(Integer.MAX_VALUE, this, false);
        }
    }

    void resolveLeafTaskOnlyOverrideConfigs(android.content.res.Configuration newParentConfig, android.graphics.Rect previousBounds) {
        if (!isLeafTask()) {
            return;
        }
        int windowingMode = getResolvedOverrideConfiguration().windowConfiguration.getWindowingMode();
        if (windowingMode == 0) {
            windowingMode = newParentConfig.windowConfiguration.getWindowingMode();
        }
        getConfiguration().windowConfiguration.setWindowingMode(windowingMode);
        android.graphics.Rect outOverrideBounds = getResolvedOverrideConfiguration().windowConfiguration.getBounds();
        if (windowingMode == 1) {
            if ((!isOrganized() || (this.mTaskWrapper.getExtImpl().isZoomMode(this.mTaskFragmentExt.getPrevWinMode()) && !this.mCreatedByOrganizer)) && !getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
                outOverrideBounds.setEmpty();
                return;
            }
            return;
        }
        adjustForMinimalTaskDimensions(outOverrideBounds, previousBounds, newParentConfig);
        if (windowingMode == 5) {
            computeFreeformBounds(outOverrideBounds, newParentConfig);
        }
    }

    void adjustForMinimalTaskDimensions(android.graphics.Rect bounds, android.graphics.Rect previousBounds, android.content.res.Configuration parentConfig) {
        int minWidth = this.mMinWidth;
        int minHeight = this.mMinHeight;
        if (!inPinnedWindowingMode()) {
            int defaultMinSizeDp = this.mDisplayContent == null ? 220 : this.mDisplayContent.mMinSizeOfResizeableTaskDp;
            float density = parentConfig.densityDpi / 160.0f;
            int defaultMinSize = (int) (defaultMinSizeDp * density);
            if (minWidth == -1) {
                minWidth = defaultMinSize;
            }
            if (minHeight == -1) {
                minHeight = defaultMinSize;
            }
        }
        if (bounds.isEmpty()) {
            android.graphics.Rect parentBounds = parentConfig.windowConfiguration.getBounds();
            if (parentBounds.width() >= minWidth && parentBounds.height() >= minHeight) {
                return;
            } else {
                bounds.set(parentBounds);
            }
        }
        boolean adjustWidth = minWidth > bounds.width();
        boolean adjustHeight = minHeight > bounds.height();
        if (!adjustWidth && !adjustHeight) {
            return;
        }
        if (adjustWidth) {
            if (!previousBounds.isEmpty() && bounds.right == previousBounds.right) {
                bounds.left = bounds.right - minWidth;
            } else {
                bounds.right = bounds.left + minWidth;
            }
        }
        if (adjustHeight) {
            if (!previousBounds.isEmpty() && bounds.bottom == previousBounds.bottom) {
                bounds.top = bounds.bottom - minHeight;
            } else {
                bounds.bottom = bounds.top + minHeight;
            }
        }
    }

    private void computeFreeformBounds(android.graphics.Rect outBounds, android.content.res.Configuration newParentConfig) {
        float density = newParentConfig.densityDpi / 160.0f;
        android.graphics.Rect parentBounds = new android.graphics.Rect(newParentConfig.windowConfiguration.getBounds());
        com.android.server.wm.DisplayContent display = getDisplayContent();
        if (display != null) {
            android.graphics.Rect stableBounds = new android.graphics.Rect();
            display.getStableRect(stableBounds);
            parentBounds.intersect(stableBounds);
        }
        fitWithinBounds(outBounds, parentBounds, (int) (48.0f * density), (int) (32.0f * density));
        int offsetTop = parentBounds.top - outBounds.top;
        if (offsetTop > 0) {
            outBounds.offset(0, offsetTop);
        }
    }

    private static void fitWithinBounds(android.graphics.Rect bounds, android.graphics.Rect rootTaskBounds, int overlapPxX, int overlapPxY) {
        if (rootTaskBounds == null || rootTaskBounds.isEmpty() || rootTaskBounds.contains(bounds)) {
            return;
        }
        int horizontalDiff = 0;
        int overlapLR = java.lang.Math.min(overlapPxX, bounds.width());
        if (bounds.right < rootTaskBounds.left + overlapLR) {
            horizontalDiff = overlapLR - (bounds.right - rootTaskBounds.left);
        } else if (bounds.left > rootTaskBounds.right - overlapLR) {
            horizontalDiff = -(overlapLR - (rootTaskBounds.right - bounds.left));
        }
        int verticalDiff = 0;
        int overlapTB = java.lang.Math.min(overlapPxY, bounds.width());
        if (bounds.bottom < rootTaskBounds.top + overlapTB) {
            verticalDiff = overlapTB - (bounds.bottom - rootTaskBounds.top);
        } else if (bounds.top > rootTaskBounds.bottom - overlapTB) {
            verticalDiff = -(overlapTB - (rootTaskBounds.bottom - bounds.top));
        }
        bounds.offset(horizontalDiff, verticalDiff);
    }

    private boolean shouldStartChangeTransition(int prevWinMode, android.graphics.Rect prevBounds) {
        if ((!isLeafTask() && !this.mCreatedByOrganizer) || !canStartChangeTransition()) {
            return false;
        }
        int newWinMode = getWindowingMode();
        if (!this.mTransitionController.inTransition(this)) {
            return (prevWinMode == 5) != (newWinMode == 5);
        }
        android.graphics.Rect newBounds = getConfiguration().windowConfiguration.getBounds();
        return (prevWinMode == newWinMode && prevBounds.width() == newBounds.width() && prevBounds.height() == newBounds.height()) ? false : true;
    }

    @Override // com.android.server.wm.WindowContainer
    void migrateToNewSurfaceControl(android.view.SurfaceControl.Transaction t) {
        super.migrateToNewSurfaceControl(t);
        getWrapper().getExtImpl().migrateToNewSurfaceControl(t, getSurfaceControl());
        this.mLastSurfaceSize.x = 0;
        this.mLastSurfaceSize.y = 0;
        updateSurfaceSize(t);
    }

    void updateSurfaceSize(android.view.SurfaceControl.Transaction transaction) {
        if (this.mSurfaceControl == null || !this.mSurfaceControl.isValid() || isOrganized() || this.mTaskWrapper.getExtImpl().isCompactWindowingMode(getWindowingMode()) || this.mTaskWrapper.getExtImpl().isPuttTask()) {
            return;
        }
        if (this.mTaskWrapper.getExtImpl().isRootTaskBeforeBootComplete()) {
            android.util.Slog.d(TAG, "skip bootReg root task updateSurfaceSize before boot");
            return;
        }
        int width = 0;
        int height = 0;
        if (isRootTask()) {
            android.graphics.Rect taskBounds = getBounds();
            width = taskBounds.width();
            height = taskBounds.height();
        }
        if (width == this.mLastSurfaceSize.x && height == this.mLastSurfaceSize.y) {
            return;
        }
        transaction.setWindowCrop(this.mSurfaceControl, width, height);
        this.mLastSurfaceSize.set(width, height);
    }

    android.graphics.Point getLastSurfaceSize() {
        return this.mLastSurfaceSize;
    }

    boolean isInChangeTransition() {
        return this.mSurfaceFreezer.hasLeash() || com.android.server.wm.AppTransition.isChangeTransitOld(this.mTransit);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public android.view.SurfaceControl getFreezeSnapshotTarget() {
        if (!this.mDisplayContent.mAppTransition.containsTransitRequest(6)) {
            return null;
        }
        android.util.ArraySet<java.lang.Integer> activityTypes = new android.util.ArraySet<>();
        activityTypes.add(java.lang.Integer.valueOf(getActivityType()));
        android.view.RemoteAnimationAdapter adapter = this.mDisplayContent.mAppTransitionController.getRemoteAnimationOverride(this, 27, activityTypes);
        if (adapter == null || adapter.getChangeNeedsSnapshot()) {
            return getSurfaceControl();
        }
        return null;
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void writeIdentifierToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, java.lang.System.identityHashCode(this));
        proto.write(1120986464258L, this.mUserId);
        proto.write(1138166333443L, (this.intent == null || this.intent.getComponent() == null) ? "Task" : this.intent.getComponent().flattenToShortString());
        proto.end(token);
    }

    private void saveLaunchingStateIfNeeded() {
        saveLaunchingStateIfNeeded(getDisplayContent());
    }

    private void saveLaunchingStateIfNeeded(com.android.server.wm.DisplayContent display) {
        if (!isLeafTask() || !getHasBeenVisible()) {
            return;
        }
        int windowingMode = getWindowingMode();
        if ((windowingMode != 1 && windowingMode != 5) || getTaskDisplayArea() == null || getTaskDisplayArea().getWindowingMode() != 5) {
            return;
        }
        this.mTaskSupervisor.mLaunchParamsPersister.saveTask(this, display);
    }

    android.graphics.Rect updateOverrideConfigurationFromLaunchBounds() {
        com.android.server.wm.Task rootTask = getRootTask();
        android.graphics.Rect bounds = (rootTask == this || !rootTask.isOrganized()) ? getLaunchBounds() : null;
        setBounds(bounds);
        if (bounds != null && !bounds.isEmpty()) {
            bounds.set(getRequestedOverrideBounds());
        }
        return bounds;
    }

    android.graphics.Rect getLaunchBounds() {
        com.android.server.wm.Task rootTask = getRootTask();
        if (rootTask == null) {
            return null;
        }
        int windowingMode = getWindowingMode();
        if (!isActivityTypeStandardOrUndefined() || windowingMode == 1) {
            if (isResizeable() || getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
                return rootTask.getRequestedOverrideBounds();
            }
            return null;
        }
        if (!getWindowConfiguration().persistTaskBounds()) {
            return rootTask.getRequestedOverrideBounds();
        }
        return this.mLastNonFullscreenBounds;
    }

    void setRootProcess(com.android.server.wm.WindowProcessController proc) {
        clearRootProcess();
        if (this.intent != null && (this.intent.getFlags() & 8388608) == 0) {
            this.mRootProcess = proc;
            this.mRootProcess.addRecentTask(this);
            if (proc != null) {
                getWrapper().getExtImpl().setPid(proc.mPid);
            }
        }
    }

    void clearRootProcess() {
        if (this.mRootProcess != null) {
            this.mRootProcess.removeRecentTask(this);
            this.mRootProcess = null;
        }
    }

    int getRootTaskId() {
        return getRootTask().mTaskId;
    }

    com.android.server.wm.Task getOrganizedTask() {
        com.android.server.wm.Task parentTask;
        if (isOrganized()) {
            return this;
        }
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent == null || (parentTask = parent.asTask()) == null) {
            return null;
        }
        return parentTask.getOrganizedTask();
    }

    com.android.server.wm.Task getCreatedByOrganizerTask() {
        com.android.server.wm.Task parentTask;
        if (this.mCreatedByOrganizer) {
            return this;
        }
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent == null || (parentTask = parent.asTask()) == null) {
            return null;
        }
        return parentTask.getCreatedByOrganizerTask();
    }

    com.android.server.wm.Task getAdjacentTask() {
        com.android.server.wm.TaskFragment adjacentTaskFragment = getAdjacentTaskFragment();
        if (adjacentTaskFragment != null && adjacentTaskFragment.asTask() != null) {
            return adjacentTaskFragment.asTask();
        }
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent == null || parent.asTask() == null) {
            return null;
        }
        return parent.asTask().getAdjacentTask();
    }

    boolean isRootTask() {
        return getRootTask() == this;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    boolean isLeafTask() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if (((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask() != null) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public com.android.server.wm.Task getTopLeafTask() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.Task child = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask();
            if (child != null) {
                return child.getTopLeafTask();
            }
        }
        return this;
    }

    int getDescendantTaskCount() {
        final int[] currentCount = {0};
        forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.Task.lambda$getDescendantTaskCount$5(currentCount, (com.android.server.wm.Task) obj);
            }
        }, false);
        return currentCount[0];
    }

    static /* synthetic */ void lambda$getDescendantTaskCount$5(int[] currentCount, com.android.server.wm.Task t) {
        currentCount[0] = currentCount[0] + 1;
    }

    com.android.server.wm.Task adjustFocusToNextFocusableTask(java.lang.String reason) {
        return adjustFocusToNextFocusableTask(reason, false, true);
    }

    private com.android.server.wm.Task getNextFocusableTask(final boolean allowFocusSelf) {
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent == null) {
            return null;
        }
        com.android.server.wm.Task focusableTask = parent.getTask(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda37
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getNextFocusableTask$6(allowFocusSelf, obj);
            }
        });
        if (focusableTask == null && parent.asTask() != null) {
            return parent.asTask().getNextFocusableTask(allowFocusSelf);
        }
        return focusableTask;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getNextFocusableTask$6(boolean allowFocusSelf, java.lang.Object task) {
        return (allowFocusSelf || task != this) && ((com.android.server.wm.Task) task).isFocusableAndVisible() && this.mTaskWrapper.getExtImpl().isMiniRootTask((com.android.server.wm.Task) task) && !this.mTaskWrapper.getExtImpl().skipFlexibleTask((com.android.server.wm.Task) task, allowFocusSelf);
    }

    com.android.server.wm.Task adjustFocusToNextFocusableTask(java.lang.String reason, boolean allowFocusSelf, boolean moveDisplayToTop) {
        com.android.server.wm.Task focusableTask = getNextFocusableTask(allowFocusSelf);
        if (focusableTask == null) {
            focusableTask = this.mRootWindowContainer.getNextFocusableRootTask(this, !allowFocusSelf);
        }
        if (focusableTask == null) {
            com.android.server.wm.TaskDisplayArea taskDisplayArea = getDisplayArea();
            if (taskDisplayArea != null) {
                taskDisplayArea.clearPreferredTopFocusableRootTask();
                return null;
            }
            return null;
        }
        com.android.server.wm.Task rootTask = focusableTask.getRootTask();
        if (!this.mTaskWrapper.getExtImpl().adjustMoveDisplayToTopForMirage(rootTask.getDisplayId(), moveDisplayToTop)) {
            com.android.server.wm.WindowContainer parent = focusableTask.getParent();
            com.android.server.wm.WindowContainer next = focusableTask;
            do {
                parent.positionChildAt(Integer.MAX_VALUE, next, false);
                next = parent;
                parent = next.getParent();
                if (next.asTask() == null) {
                    break;
                }
            } while (parent != null);
            return rootTask;
        }
        java.lang.String myReason = reason + " adjustFocusToNextFocusableTask";
        com.android.server.wm.ActivityRecord top = focusableTask.topRunningActivity();
        if (focusableTask.isActivityTypeHome() && (top == null || !top.isVisibleRequested())) {
            focusableTask.getDisplayArea().moveHomeActivityToTop(myReason);
            return rootTask;
        }
        focusableTask.moveToFront(myReason);
        if (rootTask.getTopResumedActivity() != null) {
            this.mTaskSupervisor.updateTopResumedActivityIfNeeded(reason);
        }
        return rootTask;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private int computeMinUserPosition(int minPosition, int size) {
        while (minPosition < size) {
            com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(minPosition);
            boolean canShow = child.showToCurrentUser();
            if (canShow) {
                break;
            }
            minPosition++;
        }
        return minPosition;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private int computeMaxUserPosition(int maxPosition) {
        while (maxPosition > 0) {
            com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(maxPosition);
            boolean canShow = child.showToCurrentUser();
            if (!canShow) {
                break;
            }
            maxPosition--;
        }
        return maxPosition;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private int getAdjustedChildPosition(com.android.server.wm.WindowContainer wc, int suggestedPosition) {
        boolean canShowChild = wc.showToCurrentUser();
        int size = this.mChildren.size();
        int minPosition = canShowChild ? computeMinUserPosition(0, size) : 0;
        int maxPosition = minPosition;
        if (size > 0) {
            int iComputeMaxUserPosition = size - 1;
            if (!canShowChild) {
                iComputeMaxUserPosition = computeMaxUserPosition(iComputeMaxUserPosition);
            }
            maxPosition = iComputeMaxUserPosition;
        }
        if (!wc.isAlwaysOnTop()) {
            while (maxPosition > minPosition && ((com.android.server.wm.WindowContainer) this.mChildren.get(maxPosition)).isAlwaysOnTop() && (!isAlwaysOnTop() || !this.mTaskWrapper.getExtImpl().isZoomMode(getWindowingMode()))) {
                maxPosition--;
            }
        }
        if (suggestedPosition == Integer.MIN_VALUE && minPosition == 0) {
            return Integer.MIN_VALUE;
        }
        if (suggestedPosition == Integer.MAX_VALUE && maxPosition >= size - 1) {
            return Integer.MAX_VALUE;
        }
        if (!hasChild(wc)) {
            maxPosition++;
        }
        return java.lang.Math.min(java.lang.Math.max(suggestedPosition, minPosition), maxPosition);
    }

    @Override // com.android.server.wm.WindowContainer
    void positionChildAt(int position, com.android.server.wm.WindowContainer child, boolean includingParents) {
        boolean toTop = position >= this.mChildren.size() - 1;
        if (this.mTaskWrapper.mTaskExt.handleActivityReorder(this, child, position, toTop)) {
            return;
        }
        int position2 = getAdjustedChildPosition(child, position);
        super.positionChildAt(position2, child, includingParents);
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_MOVEMENT) {
            android.util.Slog.d("WindowManager", "positionChildAt: child=" + child + " position=" + position2 + " parent=" + this);
        }
        com.android.server.wm.Task task = child.asTask();
        if (task != null) {
            task.updateTaskMovement(toTop, position2 == Integer.MIN_VALUE, position2);
        }
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void removeImmediately() {
        removeImmediately("removeTask");
    }

    @Override // com.android.server.wm.TaskFragment
    void removeImmediately(java.lang.String reason) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ROOT_TASK) {
            android.util.Slog.i(TAG, "removeTask:" + reason + " removing taskId=" + this.mTaskId);
        }
        if (this.mRemoving) {
            return;
        }
        this.mRemoving = true;
        this.mTaskWrapper.mTaskExt.forceRemoveSplashScreenViewCopyIfNeed(this);
        com.android.server.wm.EventLogTags.writeWmTaskRemoved(this.mTaskId, getRootTaskId(), getDisplayId(), reason);
        clearPinnedTaskIfNeed();
        if (this.mChildPipActivity != null) {
            this.mChildPipActivity.clearLastParentBeforePip();
        }
        setTaskOrganizer(null);
        if (this.mDecorSurfaceContainer != null) {
            this.mDecorSurfaceContainer.release();
        }
        super.removeImmediately();
        this.mRemoving = false;
    }

    void reparent(com.android.server.wm.Task rootTask, int position, boolean moveParents, java.lang.String reason) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ROOT_TASK) {
            android.util.Slog.i(TAG, "reParentTask: removing taskId=" + this.mTaskId + " from rootTask=" + getRootTask());
        }
        com.android.server.wm.EventLogTags.writeWmTaskRemoved(this.mTaskId, getRootTaskId(), getDisplayId(), "reParentTask:" + reason);
        reparent(rootTask, position);
        rootTask.positionChildAt(position, this, moveParents);
    }

    public int setBounds(android.graphics.Rect bounds, boolean forceResize) {
        int boundsChanged = setBounds(bounds);
        if (forceResize && (boundsChanged & 2) != 2) {
            onResize();
            return boundsChanged | 2;
        }
        return boundsChanged;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public int setBounds(android.graphics.Rect bounds) {
        if ((!this.mTaskWrapper.getExtImpl().isPuttTask() || !isRootTask()) && isRootTask()) {
            return setBounds(getRequestedOverrideBounds(), bounds);
        }
        int boundsChange = super.setBounds(bounds);
        updateSurfacePositionNonOrganized();
        return boundsChange;
    }

    int setBoundsUnchecked(android.graphics.Rect bounds) {
        int boundsChange = super.setBounds(bounds);
        updateSurfaceBounds();
        return boundsChange;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean isCompatible(int windowingMode, int activityType) {
        if (activityType == 0) {
            activityType = 1;
        }
        return super.isCompatible(windowingMode, activityType);
    }

    @Override // com.android.server.wm.WindowContainer
    public boolean onDescendantOrientationChanged(com.android.server.wm.WindowContainer requestingContainer) {
        getWrapper().getExtImpl().onDescendantOrientationChanged(requestingContainer);
        if (super.onDescendantOrientationChanged(requestingContainer)) {
            return true;
        }
        if (getParent() != null) {
            onConfigurationChanged(getParent().getConfiguration());
            return true;
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean handlesOrientationChangeFromDescendant(int orientation) {
        if (!super.handlesOrientationChangeFromDescendant(orientation)) {
            return false;
        }
        if (isLeafTask()) {
            return canSpecifyOrientation() && getDisplayArea().canSpecifyOrientation(orientation);
        }
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    void reparent(com.android.server.wm.WindowContainer newParent, int position) {
        if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE && !this.mTaskWrapper.getExtImpl().getAllowReparent()) {
            return;
        }
        super.reparent(newParent, position);
    }

    @Override // com.android.server.wm.WindowContainer
    void onDisplayChanged(com.android.server.wm.DisplayContent dc) {
        super.onDisplayChanged(dc);
        if (isLeafTask()) {
            int displayId = dc != null ? dc.getDisplayId() : -1;
            this.mWmService.mAtmService.getTaskChangeNotificationController().notifyTaskDisplayChanged(this.mTaskId, displayId);
        }
        if (isRootTask()) {
            updateSurfaceBounds();
        }
        sendTaskFragmentParentInfoChangedIfNeeded();
    }

    boolean isResizeable() {
        return isResizeable(true);
    }

    boolean isResizeable(boolean checkPictureInPictureSupport) {
        boolean forceResizable = this.mAtmService.mForceResizableActivities && getActivityType() == 1;
        return forceResizable || android.content.pm.ActivityInfo.isResizeableMode(this.mResizeMode) || this.mTaskWrapper.getExtImpl().getLaunchedFromMultiSearch() || (this.mSupportsPictureInPicture && checkPictureInPictureSupport);
    }

    boolean preserveOrientationOnResize() {
        return this.mResizeMode == 6 || this.mResizeMode == 5 || this.mResizeMode == 7;
    }

    boolean cropWindowsToRootTaskBounds() {
        if (isActivityTypeHomeOrRecents()) {
            com.android.server.wm.Task rootTask = getRootTask();
            com.android.server.wm.Task topNonOrgTask = rootTask.mCreatedByOrganizer ? rootTask.getTopMostTask() : rootTask;
            if (this == topNonOrgTask || isDescendantOf(topNonOrgTask)) {
                return false;
            }
        }
        if (getWrapper().getExtImpl().cropWindowsToRootTaskBounds(this)) {
            return isResizeable();
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    void getAnimationFrames(android.graphics.Rect outFrame, android.graphics.Rect outInsets, android.graphics.Rect outStableInsets, android.graphics.Rect outSurfaceInsets) {
        if (getAdjacentTask() != null) {
            super.getAnimationFrames(outFrame, outInsets, outStableInsets, outSurfaceInsets);
            return;
        }
        com.android.server.wm.WindowState windowState = getTopVisibleAppMainWindow();
        if (windowState != null) {
            windowState.getAnimationFrames(outFrame, outInsets, outStableInsets, outSurfaceInsets);
        } else {
            super.getAnimationFrames(outFrame, outInsets, outStableInsets, outSurfaceInsets);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getMaxVisibleBounds(com.android.server.wm.ActivityRecord token, android.graphics.Rect out, boolean[] foundTop) {
        com.android.server.wm.WindowState win;
        if (token.mIsExiting || !token.isClientVisible() || !token.isVisibleRequested() || (win = token.findMainWindow()) == null) {
            return;
        }
        if (!foundTop[0]) {
            foundTop[0] = true;
            out.setEmpty();
        }
        android.graphics.Rect visibleFrame = sTmpBounds;
        android.view.WindowManager.LayoutParams attrs = win.mAttrs;
        visibleFrame.set(win.getFrame());
        visibleFrame.inset(win.getInsetsStateWithVisibilityOverride().calculateVisibleInsets(visibleFrame, attrs.type, win.getActivityType(), attrs.softInputMode, attrs.flags));
        out.union(visibleFrame);
    }

    @Override // com.android.server.wm.TaskFragment
    void getDimBounds(final android.graphics.Rect out) {
        if (isRootTask()) {
            getBounds(out);
            return;
        }
        com.android.server.wm.Task rootTask = getRootTask();
        if (inFreeformWindowingMode()) {
            final boolean[] foundTop = {false};
            forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda31
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.Task.getMaxVisibleBounds((com.android.server.wm.ActivityRecord) obj, out, foundTop);
                }
            });
            if (foundTop[0]) {
                return;
            }
        }
        if (!matchParentBounds()) {
            rootTask.getBounds(this.mTmpRect);
            this.mTmpRect.intersect(getBounds());
            out.set(this.mTmpRect);
            return;
        }
        out.set(getBounds());
    }

    void setDragResizing(boolean dragResizing) {
        if (this.mDragResizing != dragResizing) {
            if (dragResizing && !getRootTask().getWindowConfiguration().canResizeTask()) {
                android.util.Slog.e(TAG, "Drag resize isn't allowed for root task id=" + getRootTaskId());
            } else {
                this.mDragResizing = dragResizing;
                resetDragResizingChangeReported();
            }
        }
    }

    boolean isDragResizing() {
        return this.mDragResizing;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    void cancelTaskWindowTransition() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).cancelAnimation();
        }
    }

    boolean showForAllUsers() {
        com.android.server.wm.ActivityRecord r;
        return (this.mChildren.isEmpty() || (r = getTopNonFinishingActivity()) == null || !r.mShowForAllUsers) ? false : true;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean showToCurrentUser() {
        return this.mForceShowForAllUsers || showForAllUsers() || this.mWmService.isUserVisible(getTopMostTask().mUserId);
    }

    void setForceShowForAllUsers(boolean forceShowForAllUsers) {
        this.mForceShowForAllUsers = forceShowForAllUsers;
    }

    com.android.server.wm.ActivityRecord getOccludingActivityAbove(final com.android.server.wm.ActivityRecord activity) {
        com.android.server.wm.ActivityRecord top = getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Task.lambda$getOccludingActivityAbove$8(activity, (com.android.server.wm.ActivityRecord) obj);
            }
        });
        if (top != activity) {
            return top;
        }
        return null;
    }

    static /* synthetic */ boolean lambda$getOccludingActivityAbove$8(com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord r) {
        if (r == activity) {
            return true;
        }
        if (!r.occludesParent()) {
            return false;
        }
        com.android.server.wm.TaskFragment parent = r.getTaskFragment();
        if (parent == activity.getTaskFragment()) {
            return true;
        }
        if (parent != null && parent.asTask() != null) {
            return true;
        }
        com.android.server.wm.TaskFragment grandParent = parent.getParent().asTaskFragment();
        while (grandParent != null && parent.getBounds().equals(grandParent.getBounds())) {
            if (grandParent.asTask() != null) {
                return true;
            }
            parent = grandParent;
            grandParent = parent.getParent().asTaskFragment();
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl.Builder makeAnimationLeash() {
        return super.makeAnimationLeash().setMetadata(3, this.mTaskId);
    }

    boolean shouldAnimate() {
        if (isOrganized()) {
            return false;
        }
        com.android.server.wm.RecentsAnimationController controller = this.mWmService.getRecentsAnimationController();
        return (controller != null && controller.isAnimatingTask(this) && controller.shouldDeferCancelUntilNextTransition()) ? false : true;
    }

    @Override // com.android.server.wm.WindowContainer
    void setInitialSurfaceControlProperties(android.view.SurfaceControl.Builder b) {
        b.setEffectLayer().setMetadata(3, this.mTaskId);
        super.setInitialSurfaceControlProperties(b);
    }

    boolean isAnimatingByRecents() {
        return isAnimating(4, 8) || this.mTransitionController.isTransientHide(this);
    }

    com.android.server.wm.WindowState getTopVisibleAppMainWindow() {
        com.android.server.wm.ActivityRecord activity = getTopVisibleActivity();
        if (activity != null) {
            return activity.findMainWindow();
        }
        return null;
    }

    com.android.server.wm.ActivityRecord topRunningNonDelayedActivityLocked(com.android.server.wm.ActivityRecord notTop) {
        com.android.internal.util.function.pooled.PooledPredicate p = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new java.util.function.BiPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda20
            @Override // java.util.function.BiPredicate
            public final boolean test(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.wm.Task.isTopRunningNonDelayed((com.android.server.wm.ActivityRecord) obj, (com.android.server.wm.ActivityRecord) obj2);
            }
        }, com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.ActivityRecord.class), notTop);
        com.android.server.wm.ActivityRecord r = getActivity(p);
        p.recycle();
        return r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isTopRunningNonDelayed(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord notTop) {
        return (r.delayedResume || r == notTop || !r.canBeTopRunning()) ? false : true;
    }

    com.android.server.wm.ActivityRecord topRunningActivity(android.os.IBinder token, int taskId) {
        com.android.internal.util.function.pooled.PooledPredicate p = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new com.android.internal.util.function.TriPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda9
            public final boolean test(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                return com.android.server.wm.Task.isTopRunning((com.android.server.wm.ActivityRecord) obj, ((java.lang.Integer) obj2).intValue(), (android.os.IBinder) obj3);
            }
        }, com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.ActivityRecord.class), java.lang.Integer.valueOf(taskId), token);
        com.android.server.wm.ActivityRecord r = getActivity(p);
        p.recycle();
        return r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isTopRunning(com.android.server.wm.ActivityRecord r, int taskId, android.os.IBinder notTop) {
        return (r.getTask().mTaskId == taskId || r.token == notTop || !r.canBeTopRunning()) ? false : true;
    }

    com.android.server.wm.ActivityRecord getTopFullscreenActivity() {
        return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Task.lambda$getTopFullscreenActivity$9((com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$getTopFullscreenActivity$9(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.WindowState win = r.findMainWindow();
        return win != null && win.mAttrs.isFullscreen();
    }

    static /* synthetic */ boolean lambda$getTopVisibleActivity$10(com.android.server.wm.ActivityRecord r) {
        return !r.mIsExiting && r.isClientVisible() && r.isVisibleRequested();
    }

    com.android.server.wm.ActivityRecord getTopVisibleActivity() {
        return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda26
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Task.lambda$getTopVisibleActivity$10((com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$getTopRealVisibleActivity$11(com.android.server.wm.ActivityRecord r) {
        return !r.mIsExiting && r.isClientVisible() && r.isVisible();
    }

    com.android.server.wm.ActivityRecord getTopRealVisibleActivity() {
        return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda33
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Task.lambda$getTopRealVisibleActivity$11((com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    com.android.server.wm.ActivityRecord getTopWaitSplashScreenActivity() {
        return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Task.lambda$getTopWaitSplashScreenActivity$12((com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$getTopWaitSplashScreenActivity$12(com.android.server.wm.ActivityRecord r) {
        return r.mHandleExitSplashScreen && r.mTransferringSplashScreenState == 1;
    }

    void setTaskDescription(android.app.ActivityManager.TaskDescription taskDescription) {
        this.mTaskDescription = taskDescription;
    }

    void onSnapshotChanged(android.window.TaskSnapshot snapshot) {
        this.mLastTaskSnapshotData.set(snapshot);
        this.mAtmService.getTaskChangeNotificationController().notifyTaskSnapshotChanged(this.mTaskId, snapshot);
    }

    void onSnapshotInvalidated() {
        this.mAtmService.getTaskChangeNotificationController().notifyTaskSnapshotInvalidated(this.mTaskId);
    }

    android.app.ActivityManager.TaskDescription getTaskDescription() {
        return this.mTaskDescription;
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.wm.WindowContainer
    void forAllLeafTasks(java.util.function.Consumer<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        int count = this.mChildren.size();
        boolean isLeafTask = true;
        if (traverseTopToBottom) {
            for (int i = count - 1; i >= 0; i--) {
                com.android.server.wm.Task child = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask();
                if (child != null) {
                    isLeafTask = false;
                    child.forAllLeafTasks(callback, traverseTopToBottom);
                }
            }
        } else {
            for (int i2 = 0; i2 < count; i2++) {
                com.android.server.wm.Task child2 = ((com.android.server.wm.WindowContainer) this.mChildren.get(i2)).asTask();
                if (child2 != null) {
                    isLeafTask = false;
                    child2.forAllLeafTasks(callback, traverseTopToBottom);
                }
            }
        }
        if (isLeafTask) {
            callback.accept(this);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void forAllTasks(java.util.function.Consumer<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        super.forAllTasks(callback, traverseTopToBottom);
        callback.accept(this);
    }

    @Override // com.android.server.wm.WindowContainer
    void forAllRootTasks(java.util.function.Consumer<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        if (isRootTask()) {
            callback.accept(this);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    boolean forAllTasks(java.util.function.Predicate<com.android.server.wm.Task> callback) {
        if (super.forAllTasks(callback)) {
            return true;
        }
        return callback.test(this);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.wm.WindowContainer
    boolean forAllLeafTasks(java.util.function.Predicate<com.android.server.wm.Task> callback) {
        boolean isLeafTask = true;
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.Task child = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask();
            if (child != null) {
                isLeafTask = false;
                if (child.forAllLeafTasks(callback)) {
                    return true;
                }
            }
        }
        if (isLeafTask) {
            return callback.test(this);
        }
        return false;
    }

    void forAllLeafTasksAndLeafTaskFragments(final java.util.function.Consumer<com.android.server.wm.TaskFragment> callback, final boolean traverseTopToBottom) {
        forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda27
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.Task.lambda$forAllLeafTasksAndLeafTaskFragments$13(callback, traverseTopToBottom, (com.android.server.wm.Task) obj);
            }
        }, traverseTopToBottom);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    static /* synthetic */ void lambda$forAllLeafTasksAndLeafTaskFragments$13(java.util.function.Consumer callback, boolean traverseTopToBottom, com.android.server.wm.Task task) {
        if (task.isLeafTaskFragment()) {
            callback.accept(task);
            return;
        }
        boolean consumed = false;
        if (traverseTopToBottom) {
            for (int i = task.mChildren.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) task.mChildren.get(i);
                if (child.asTaskFragment() != null) {
                    child.forAllLeafTaskFragments(callback, traverseTopToBottom);
                } else if (child.asActivityRecord() != null && !consumed) {
                    callback.accept(task);
                    consumed = true;
                }
            }
            return;
        }
        for (int i2 = 0; i2 < task.mChildren.size(); i2++) {
            com.android.server.wm.WindowContainer child2 = (com.android.server.wm.WindowContainer) task.mChildren.get(i2);
            if (child2.asTaskFragment() != null) {
                child2.forAllLeafTaskFragments(callback, traverseTopToBottom);
            } else if (child2.asActivityRecord() != null && !consumed) {
                callback.accept(task);
                consumed = true;
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer
    boolean forAllRootTasks(java.util.function.Predicate<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        if (isRootTask()) {
            return callback.test(this);
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.Task getTask(java.util.function.Predicate<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        com.android.server.wm.Task t = super.getTask(callback, traverseTopToBottom);
        if (t != null) {
            return t;
        }
        if (callback.test(this)) {
            return this;
        }
        return null;
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.Task getRootTask(java.util.function.Predicate<com.android.server.wm.Task> callback, boolean traverseTopToBottom) {
        if (isRootTask() && callback.test(this)) {
            return this;
        }
        return null;
    }

    void setCanAffectSystemUiFlags(boolean canAffectSystemUiFlags) {
        this.mCanAffectSystemUiFlags = canAffectSystemUiFlags;
    }

    boolean canAffectSystemUiFlags() {
        return this.mCanAffectSystemUiFlags;
    }

    void dontAnimateDimExit() {
        com.android.server.wm.Dimmer dimmer = getDimmer();
        if (dimmer != null) {
            dimmer.dontAnimateExit();
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    java.lang.String getName() {
        return "Task=" + this.mTaskId;
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    com.android.server.wm.Dimmer getDimmer() {
        if (inMultiWindowMode() || this.mTaskWrapper.getExtImpl().shouldUseSelfDimmer()) {
            return this.mDimmer;
        }
        if (!isRootTask() || ((com.android.server.wm.Dimmer.DIMMER_REFACTOR && isTranslucentAndVisible()) || (!com.android.window.flags.Flags.getDimmerOnClosing() ? isTranslucent(null) : isTranslucentForTransition()))) {
            return super.getDimmer();
        }
        return this.mDimmer;
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void prepareSurfaces() {
        this.mDimmer.resetDimStates();
        super.prepareSurfaces();
        this.mTaskWrapper.getExtImpl().hideBackgroundSurface(this);
        this.mTaskWrapper.getExtImpl().removeCompactMask(this, false);
        this.mTaskWrapper.getExtImpl().prepareSurfaces(getPendingTransaction(), this.mDisplayContent, this);
        android.graphics.Rect dimBounds = this.mDimmer.getDimBounds();
        if (dimBounds != null) {
            getDimBounds(dimBounds);
            if (inFreeformWindowingMode()) {
                getBounds(this.mTmpRect);
                dimBounds.offsetTo(dimBounds.left - this.mTmpRect.left, dimBounds.top - this.mTmpRect.top);
            } else {
                dimBounds.offsetTo(0, 0);
            }
        }
        android.view.SurfaceControl.Transaction t = getSyncTransaction();
        this.mAtmService.getWrapper().getFlexibleExtImpl().prepareSurfaces(this);
        if (dimBounds != null && this.mDimmer.updateDims(t)) {
            scheduleAnimation();
        }
        if (this.mTransitionController.isCollecting() && (this.mCreatedByOrganizer || this.mTaskWrapper.mTaskExt.skipPreapreSurface(this.mTransitionController))) {
            return;
        }
        boolean visible = isVisible();
        boolean z = true;
        boolean show = visible || isAnimating(7);
        boolean inTransition = inTransition();
        if (this.mWindowContainerExt.dependShellTransition(show)) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM && inTransition) {
                android.util.Slog.d(TAG, "prepareSurfaces: inTransition=true, task=" + this);
            }
            if (!show && !inTransition) {
                z = false;
            }
            show = z;
        }
        if (this.mTaskWrapper.getExtImpl().hasNoSurfaceShowing(this, show, this.mLastSurfaceShowing)) {
            this.mLastSurfaceShowing = false;
        }
        if (this.mSurfaceControl != null && show != this.mLastSurfaceShowing) {
            if (getWrapper().getExtImpl().shouldHideEmbeddedSurfaceBeforeReparent()) {
                android.util.Slog.d(TAG, "prepareSurfaces skip task show " + this + ", show:" + show + ", mLastSurfaceShowing:" + this.mLastSurfaceShowing);
                t.setVisibility(this.mSurfaceControl, false);
                this.mTaskWrapper.getExtImpl().removeStartingSurfaceWhenVisibleChange(this);
                return;
            } else {
                t.setVisibility(this.mSurfaceControl, show);
                if (!show) {
                    this.mTaskWrapper.getExtImpl().removeStartingSurfaceWhenVisibleChange(this);
                }
                this.mTaskWrapper.getExtImpl().updateAlphaInPinnedMode(this, t, this.mSurfaceControl);
            }
        }
        if (this.mOverlayHost != null) {
            this.mOverlayHost.setVisibility(t, visible);
        }
        this.mLastSurfaceShowing = show;
    }

    @Override // com.android.server.wm.WindowContainer
    protected void applyAnimationUnchecked(android.view.WindowManager.LayoutParams lp, boolean enter, int transit, boolean isVoiceInteraction, final java.util.ArrayList<com.android.server.wm.WindowContainer> sources) {
        com.android.server.wm.RecentsAnimationController control = this.mWmService.getRecentsAnimationController();
        if (control != null) {
            if (enter && !isActivityTypeHomeOrRecents()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RECENTS_ANIMATIONS_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(control);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(asTask());
                    java.lang.String protoLogParam2 = java.lang.String.valueOf(com.android.server.wm.AppTransition.appTransitionOldToString(transit));
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RECENTS_ANIMATIONS, -3401780415681318335L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2);
                }
                final int size = sources != null ? sources.size() : 0;
                control.addTaskToTargets(this, new com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda2
                    @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
                    public final void onAnimationFinished(int i, com.android.server.wm.AnimationAdapter animationAdapter) {
                        com.android.server.wm.Task.lambda$applyAnimationUnchecked$14(size, sources, i, animationAdapter);
                    }
                });
                return;
            }
            return;
        }
        if (this.mTaskWrapper.getExtImpl().isDragZoomMode()) {
            android.util.Slog.d(TAG, "skipping app transition animation task:" + this);
            return;
        }
        if (!enter && this.mTaskWrapper.getExtImpl().isNoAnimationTask(this.mTaskId)) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM) {
                android.util.Slog.d(TAG, "applyAnimationUnchecked ==> for exiting task we marked onAnimation, don't apply animation any more : " + this);
            }
            this.mTaskWrapper.getExtImpl().onApplyNoAnimationOfTask(this);
        } else {
            this.mTaskWrapper.getExtImpl().saveFixedRotatedTaskWhenKeyGuardGoingAway(this, transit, enter);
            super.applyAnimationUnchecked(lp, enter, transit, isVoiceInteraction, sources);
        }
    }

    static /* synthetic */ void lambda$applyAnimationUnchecked$14(int size, java.util.ArrayList sources, int type, com.android.server.wm.AnimationAdapter anim) {
        for (int i = 0; i < size; i++) {
            ((com.android.server.wm.WindowContainer) sources.get(i)).onAnimationFinished(type, anim);
        }
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        super.dump(pw, prefix, dumpAll);
        this.mAnimatingActivityRegistry.dump(pw, "AnimatingApps:", prefix);
    }

    void fillTaskInfo(android.app.TaskInfo info) {
        fillTaskInfo(info, true);
    }

    void fillTaskInfo(android.app.TaskInfo info, boolean stripExtras) {
        fillTaskInfo(info, stripExtras, getDisplayArea());
    }

    void fillTaskInfo(android.app.TaskInfo info, boolean stripExtras, com.android.server.wm.TaskDisplayArea tda) {
        android.content.Intent intentCloneFilter;
        boolean z;
        boolean z2;
        int i;
        int i2;
        int i3;
        boolean z3;
        boolean isTopActivityResumed;
        boolean isTopActivityVisible;
        boolean z4;
        boolean z5;
        boolean z6;
        int cameraCompatControlState;
        int i4;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        info.launchCookies.clear();
        info.addLaunchCookie(this.mLaunchCookie);
        com.android.server.wm.ActivityRecord top = this.mTaskSupervisor.mTaskInfoHelper.fillAndReturnTop(this, info);
        info.userId = isLeafTask() ? this.mUserId : this.mCurrentUser;
        info.taskId = this.mTaskId;
        info.displayId = getDisplayId();
        info.displayAreaFeatureId = tda != null ? tda.mFeatureId : -1;
        android.content.Intent baseIntent = getBaseIntent();
        int freeformCameraCompatMode = 0;
        int baseIntentFlags = baseIntent == null ? 0 : baseIntent.getFlags();
        if (baseIntent == null) {
            intentCloneFilter = new android.content.Intent();
        } else {
            intentCloneFilter = stripExtras ? baseIntent.cloneFilter() : new android.content.Intent(baseIntent);
        }
        info.baseIntent = intentCloneFilter;
        info.baseIntent.setFlags(baseIntentFlags);
        boolean z14 = true;
        if (top == null) {
            z = false;
        } else {
            z = true;
        }
        info.isRunning = z;
        info.topActivity = top != null ? top.mActivityComponent : null;
        info.origActivity = this.origActivity;
        info.realActivity = this.realActivity;
        info.lastActiveTime = this.lastActiveTime;
        info.taskDescription = new android.app.ActivityManager.TaskDescription(getTaskDescription());
        info.supportsSplitScreenMultiWindow = supportsSplitScreenWindowingModeInDisplayArea(tda);
        if (!this.mHasBeenVisible || !getWrapper().getExtImpl().isFlexibleEmbedded()) {
            z2 = false;
        } else {
            z2 = true;
        }
        info.isInFlexibleEmbedded = z2;
        info.uid = -1;
        info.pid = -1;
        if (top != null) {
            info.uid = top.getUid();
            info.pid = top.getPid();
        }
        info.supportsMultiWindow = supportsMultiWindowInDisplayArea(tda);
        info.configuration.setTo(getConfiguration());
        info.configuration.windowConfiguration.setActivityType(getActivityType());
        info.configuration.windowConfiguration.setWindowingMode(getWindowingMode());
        info.token = this.mRemoteToken.toWindowContainerToken();
        com.android.server.wm.Task topTask = top != null ? top.getTask() : this;
        info.resizeMode = topTask.mResizeMode;
        info.topActivityType = topTask.getActivityType();
        info.displayCutoutInsets = topTask.getDisplayCutoutInsets();
        info.isResizeable = isResizeable();
        info.minWidth = this.mMinWidth;
        info.minHeight = this.mMinHeight;
        if (this.mDisplayContent != null) {
            i = this.mDisplayContent.mMinSizeOfResizeableTaskDp;
        } else {
            i = 220;
        }
        info.defaultMinSize = i;
        info.positionInParent = getRelativePosition();
        info.topActivityInfo = top != null ? top.info : null;
        info.pictureInPictureParams = getPictureInPictureParams(top);
        if (info.pictureInPictureParams == null || !info.pictureInPictureParams.isLaunchIntoPip() || top.getLastParentBeforePip() == null) {
            i2 = -1;
        } else {
            i2 = top.getLastParentBeforePip().mTaskId;
        }
        info.launchIntoPipHostTaskId = i2;
        if (top == null || top.getLastParentBeforePip() == null) {
            i3 = -1;
        } else {
            i3 = top.getLastParentBeforePip().mTaskId;
        }
        info.lastParentTaskIdBeforePip = i3;
        if (top == null || !top.shouldDockBigOverlays) {
            z3 = false;
        } else {
            z3 = true;
        }
        info.shouldDockBigOverlays = z3;
        info.mTopActivityLocusId = top != null ? top.getLocusId() : null;
        if (top == null || top.getOrganizedTask() != this || !top.isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
            isTopActivityResumed = false;
        } else {
            isTopActivityResumed = true;
        }
        if (top == null || top.getOrganizedTask() != this || !top.isVisible()) {
            isTopActivityVisible = false;
        } else {
            isTopActivityVisible = true;
        }
        android.app.AppCompatTaskInfo appCompatTaskInfo = info.appCompatTaskInfo;
        if (!isTopActivityVisible || !top.inSizeCompatMode()) {
            z4 = false;
        } else {
            z4 = true;
        }
        appCompatTaskInfo.topActivityInSizeCompat = z4;
        if (appCompatTaskInfo.topActivityInSizeCompat && this.mWmService.mLetterboxConfiguration.isTranslucentLetterboxingEnabled()) {
            appCompatTaskInfo.topActivityInSizeCompat = top.fillsParent();
        }
        if (!isTopActivityResumed || !top.isEligibleForLetterboxEducation()) {
            z5 = false;
        } else {
            z5 = true;
        }
        appCompatTaskInfo.topActivityEligibleForLetterboxEducation = z5;
        if (top == null || !top.mLetterboxUiController.isLetterboxEducationEnabled()) {
            z6 = false;
        } else {
            z6 = true;
        }
        appCompatTaskInfo.isLetterboxEducationEnabled = z6;
        android.app.CameraCompatTaskInfo cameraCompatTaskInfo = appCompatTaskInfo.cameraCompatTaskInfo;
        if (isTopActivityResumed) {
            cameraCompatControlState = top.getCameraCompatControlState();
        } else {
            cameraCompatControlState = 0;
        }
        cameraCompatTaskInfo.cameraCompatControlState = cameraCompatControlState;
        com.android.server.wm.Task parentTask = getParent() != null ? getParent().asTask() : null;
        if (parentTask != null && parentTask.mCreatedByOrganizer) {
            i4 = parentTask.mTaskId;
        } else {
            i4 = -1;
        }
        info.parentTaskId = i4;
        info.isFocused = isFocused();
        info.isVisible = hasVisibleChildren();
        info.isVisibleRequested = isVisibleRequested();
        info.isSleeping = shouldSleepActivities();
        if (top == null || top.fillsParent()) {
            z7 = false;
        } else {
            z7 = true;
        }
        info.isTopActivityTransparent = z7;
        appCompatTaskInfo.topActivityLetterboxVerticalPosition = -1;
        appCompatTaskInfo.topActivityLetterboxHorizontalPosition = -1;
        appCompatTaskInfo.topActivityLetterboxWidth = -1;
        appCompatTaskInfo.topActivityLetterboxHeight = -1;
        if (top == null || !top.mLetterboxUiController.shouldApplyUserFullscreenOverride()) {
            z8 = false;
        } else {
            z8 = true;
        }
        appCompatTaskInfo.isUserFullscreenOverrideEnabled = z8;
        if (top == null || !top.mLetterboxUiController.isSystemOverrideToFullscreenEnabled()) {
            z9 = false;
        } else {
            z9 = true;
        }
        appCompatTaskInfo.isSystemFullscreenOverrideEnabled = z9;
        if (top == null || !top.mLetterboxUiController.isFromDoubleTap()) {
            z10 = false;
        } else {
            z10 = true;
        }
        appCompatTaskInfo.isFromLetterboxDoubleTap = z10;
        if (top != null) {
            appCompatTaskInfo.topActivityLetterboxWidth = top.getBounds().width();
            appCompatTaskInfo.topActivityLetterboxHeight = top.getBounds().height();
        }
        if (top == null || !top.mLetterboxUiController.isLetterboxDoubleTapEducationEnabled()) {
            z11 = false;
        } else {
            z11 = true;
        }
        appCompatTaskInfo.isLetterboxDoubleTapEnabled = z11;
        if (appCompatTaskInfo.isLetterboxDoubleTapEnabled) {
            if (appCompatTaskInfo.isTopActivityPillarboxed()) {
                if (top.mLetterboxUiController.allowHorizontalReachabilityForThinLetterbox()) {
                    appCompatTaskInfo.topActivityLetterboxHorizontalPosition = top.mLetterboxUiController.getLetterboxPositionForHorizontalReachability();
                } else {
                    appCompatTaskInfo.isLetterboxDoubleTapEnabled = false;
                }
            } else if (top.mLetterboxUiController.allowVerticalReachabilityForThinLetterbox()) {
                appCompatTaskInfo.topActivityLetterboxVerticalPosition = top.mLetterboxUiController.getLetterboxPositionForVerticalReachability();
            } else {
                appCompatTaskInfo.isLetterboxDoubleTapEnabled = false;
            }
        }
        if (top == null || appCompatTaskInfo.topActivityInSizeCompat || !top.mLetterboxUiController.shouldEnableUserAspectRatioSettings() || info.isTopActivityTransparent) {
            z12 = false;
        } else {
            z12 = true;
        }
        appCompatTaskInfo.topActivityEligibleForUserAspectRatioButton = z12;
        if (top == null || !top.getWrapper().getExtImpl().inOplusCompatMode()) {
            z13 = false;
        } else {
            z13 = true;
        }
        appCompatTaskInfo.topActivityInOplusCompatMode = z13;
        if (top != null) {
            boolean isSystemApp = top.info.applicationInfo.isSystemApp();
            if (isSystemApp) {
                info.supportsSplitScreenMultiWindow &= top.supportsSplitScreenWindowingMode();
            }
        }
        info.topActivityLetterboxInsets = top != null ? top.getLetterboxInsets() : new android.graphics.Rect();
        this.mTaskWrapper.getExtImpl().addExtraTaskInfo(this, info);
        if (top == null || !top.areBoundsLetterboxed()) {
            z14 = false;
        }
        appCompatTaskInfo.topActivityBoundsLetterboxed = z14;
        android.app.CameraCompatTaskInfo cameraCompatTaskInfo2 = appCompatTaskInfo.cameraCompatTaskInfo;
        if (top != null) {
            freeformCameraCompatMode = top.mLetterboxUiController.getFreeformCameraCompatMode();
        }
        cameraCompatTaskInfo2.freeformCameraCompatMode = freeformCameraCompatMode;
    }

    static /* synthetic */ boolean lambda$trimIneffectiveInfo$15(com.android.server.wm.ActivityRecord r) {
        return !r.finishing;
    }

    static void trimIneffectiveInfo(com.android.server.wm.Task task, android.app.TaskInfo info) {
        com.android.server.wm.ActivityRecord baseActivity = task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Task.lambda$trimIneffectiveInfo$15((com.android.server.wm.ActivityRecord) obj);
            }
        }, false);
        int baseActivityUid = baseActivity != null ? baseActivity.getUid() : task.effectiveUid;
        if (info.topActivityInfo != null && task.effectiveUid != info.topActivityInfo.applicationInfo.uid) {
            info.topActivityInfo = new android.content.pm.ActivityInfo(info.topActivityInfo);
            info.topActivityInfo.applicationInfo = new android.content.pm.ApplicationInfo(info.topActivityInfo.applicationInfo);
            info.topActivity = new android.content.ComponentName("", "");
            info.topActivityInfo.packageName = "";
            info.topActivityInfo.taskAffinity = "";
            info.topActivityInfo.processName = "";
            info.topActivityInfo.name = "";
            info.topActivityInfo.parentActivityName = "";
            info.topActivityInfo.targetActivity = "";
            info.topActivityInfo.splitName = "";
            info.topActivityInfo.applicationInfo.className = "";
            info.topActivityInfo.applicationInfo.credentialProtectedDataDir = "";
            info.topActivityInfo.applicationInfo.dataDir = "";
            info.topActivityInfo.applicationInfo.deviceProtectedDataDir = "";
            info.topActivityInfo.applicationInfo.manageSpaceActivityName = "";
            info.topActivityInfo.applicationInfo.nativeLibraryDir = "";
            info.topActivityInfo.applicationInfo.nativeLibraryRootDir = "";
            info.topActivityInfo.applicationInfo.processName = "";
            info.topActivityInfo.applicationInfo.publicSourceDir = "";
            info.topActivityInfo.applicationInfo.scanPublicSourceDir = "";
            info.topActivityInfo.applicationInfo.scanSourceDir = "";
            info.topActivityInfo.applicationInfo.sourceDir = "";
            info.topActivityInfo.applicationInfo.taskAffinity = "";
            info.topActivityInfo.applicationInfo.name = "";
            info.topActivityInfo.applicationInfo.packageName = "";
        }
        if (task.effectiveUid != baseActivityUid) {
            info.baseActivity = new android.content.ComponentName("", "");
        }
    }

    android.app.PictureInPictureParams getPictureInPictureParams() {
        com.android.server.wm.Task topTask = getTopMostTask();
        if (topTask == null) {
            return null;
        }
        return getPictureInPictureParams(topTask.getTopMostActivity());
    }

    private static android.app.PictureInPictureParams getPictureInPictureParams(com.android.server.wm.ActivityRecord top) {
        if (top == null || top.pictureInPictureArgs.empty()) {
            return null;
        }
        return new android.app.PictureInPictureParams(top.pictureInPictureArgs);
    }

    android.graphics.Rect getDisplayCutoutInsets() {
        int displayCutoutMode;
        if (this.mDisplayContent == null || getDisplayInfo().displayCutout == null) {
            return null;
        }
        com.android.server.wm.WindowState w = getTopVisibleAppMainWindow();
        if (w == null) {
            displayCutoutMode = 0;
        } else {
            displayCutoutMode = w.getAttrs().layoutInDisplayCutoutMode;
        }
        if (displayCutoutMode == 3 || displayCutoutMode == 1) {
            return null;
        }
        return getDisplayInfo().displayCutout.getSafeInsets();
    }

    android.app.ActivityManager.RunningTaskInfo getTaskInfo() {
        android.app.ActivityManager.RunningTaskInfo info = new android.app.ActivityManager.RunningTaskInfo();
        fillTaskInfo(info);
        return info;
    }

    android.window.StartingWindowInfo getStartingWindowInfo(com.android.server.wm.ActivityRecord activity) {
        int i;
        com.android.server.wm.ActivityRecord topFullscreenActivity;
        com.android.server.wm.WindowState mainWindow;
        com.android.server.wm.WindowState topMainWin;
        android.window.StartingWindowInfo info = new android.window.StartingWindowInfo();
        info.taskInfo = getTaskInfo();
        info.targetActivityInfo = (info.taskInfo.topActivityInfo == null || activity.info == info.taskInfo.topActivityInfo) ? null : activity.info;
        info.isKeyguardOccluded = this.mAtmService.mKeyguardController.isKeyguardOccluded(info.taskInfo.displayId);
        if (activity.mStartingData != null) {
            i = activity.mStartingData.mTypeParams;
        } else {
            i = 272;
        }
        info.startingWindowTypeParameter = i;
        if ((info.startingWindowTypeParameter & 16) != 0 && (topMainWin = getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Task.lambda$getStartingWindowInfo$16((com.android.server.wm.WindowState) obj);
            }
        })) != null) {
            info.mainWindowLayoutParams = topMainWin.getAttrs();
            info.requestedVisibleTypes = topMainWin.getRequestedVisibleTypes();
        }
        android.graphics.Rect rotatedBounds = activity.getFixedRotationTransformDisplayBounds();
        info.taskBounds.set(rotatedBounds != null ? rotatedBounds : info.taskInfo.configuration.windowConfiguration.getBounds());
        info.taskInfo.configuration.setTo(activity.getConfiguration());
        if (!com.android.window.flags.Flags.drawSnapshotAspectRatioMatch() && (topFullscreenActivity = getTopFullscreenActivity()) != null && (mainWindow = topFullscreenActivity.findMainWindow(false)) != null) {
            info.topOpaqueWindowInsetsState = mainWindow.getInsetsStateWithVisibilityOverride();
            info.topOpaqueWindowLayoutParams = mainWindow.getAttrs();
        }
        return info;
    }

    static /* synthetic */ boolean lambda$getStartingWindowInfo$16(com.android.server.wm.WindowState w) {
        return w.mAttrs.type == 1;
    }

    android.window.TaskFragmentParentInfo getTaskFragmentParentInfo() {
        return new android.window.TaskFragmentParentInfo(getConfiguration(), getDisplayId(), shouldBeVisible(null), hasNonFinishingDirectActivity(), getDecorSurface());
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    protected boolean onChildVisibleRequestedChanged(com.android.server.wm.WindowContainer child) {
        if (!super.onChildVisibleRequestedChanged(child)) {
            return false;
        }
        sendTaskFragmentParentInfoChangedIfNeeded();
        return true;
    }

    void sendTaskFragmentParentInfoChangedIfNeeded() {
        com.android.server.wm.TaskFragment childOrganizedTf;
        if (isLeafTask() && (childOrganizedTf = getTaskFragment(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.TaskFragment) obj).isOrganizedTaskFragment();
            }
        })) != null) {
            childOrganizedTf.sendTaskFragmentParentInfoChanged();
        }
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.wm.WindowContainer
    void assignChildLayers(android.view.SurfaceControl.Transaction t) {
        com.android.server.wm.TaskFragment adjacentTf;
        int layer = 0;
        boolean decorSurfacePlaced = false;
        for (int j = 0; j < this.mChildren.size(); j++) {
            com.android.server.wm.WindowContainer wc = (com.android.server.wm.WindowContainer) this.mChildren.get(j);
            wc.assignChildLayers(t);
            if (!wc.needsZBoost()) {
                if (this.mDecorSurfaceContainer != null && !this.mDecorSurfaceContainer.mIsBoosted && !decorSurfacePlaced && shouldPlaceDecorSurfaceBelowContainer(wc)) {
                    this.mDecorSurfaceContainer.assignLayer(t, layer);
                    decorSurfacePlaced = true;
                    layer++;
                }
                int layer2 = layer + 1;
                wc.assignLayer(t, layer);
                com.android.server.wm.TaskFragment taskFragment = wc.asTaskFragment();
                if (taskFragment != null && taskFragment.isEmbedded() && (adjacentTf = taskFragment.getAdjacentTaskFragment()) != null && adjacentTf.shouldBoostDimmer()) {
                    adjacentTf.assignLayer(t, layer2);
                    layer2++;
                }
                if (this.mDecorSurfaceContainer == null || this.mDecorSurfaceContainer.mIsBoosted || decorSurfacePlaced || wc != this.mDecorSurfaceContainer.mOwnerTaskFragment) {
                    layer = layer2;
                } else {
                    this.mDecorSurfaceContainer.assignLayer(t, layer2);
                    decorSurfacePlaced = true;
                    layer = layer2 + 1;
                }
            }
        }
        if (this.mDecorSurfaceContainer != null && this.mDecorSurfaceContainer.mIsBoosted) {
            this.mDecorSurfaceContainer.assignLayer(t, layer);
            layer++;
        }
        for (int j2 = 0; j2 < this.mChildren.size(); j2++) {
            com.android.server.wm.WindowContainer wc2 = (com.android.server.wm.WindowContainer) this.mChildren.get(j2);
            if (wc2.needsZBoost()) {
                wc2.assignLayer(t, layer);
                layer++;
            }
        }
        if (this.mOverlayHost != null) {
            int i = layer + 1;
            this.mOverlayHost.setLayer(t, layer);
        }
    }

    boolean shouldPlaceDecorSurfaceBelowContainer(com.android.server.wm.WindowContainer wc) {
        boolean isOwnActivity = wc.asActivityRecord() != null && wc.asActivityRecord().isUid(this.effectiveUid);
        boolean isTrustedTaskFragment = wc.asTaskFragment() != null && wc.asTaskFragment().isEmbedded() && wc.asTaskFragment().isAllowedToBeEmbeddedInTrustedMode();
        return (isOwnActivity || isTrustedTaskFragment) ? false : true;
    }

    void requestDecorSurfaceBoosted(com.android.server.wm.TaskFragment ownerTaskFragment, boolean isBoosted, android.view.SurfaceControl.Transaction clientTransaction) {
        if (this.mDecorSurfaceContainer == null || this.mDecorSurfaceContainer.mOwnerTaskFragment != ownerTaskFragment) {
            return;
        }
        this.mDecorSurfaceContainer.requestBoosted(isBoosted, clientTransaction);
    }

    void commitDecorSurfaceBoostedState() {
        if (this.mDecorSurfaceContainer == null) {
            return;
        }
        this.mDecorSurfaceContainer.commitBoostedState();
        assignChildLayers();
    }

    boolean isDecorSurfaceBoosted() {
        return this.mDecorSurfaceContainer != null && this.mDecorSurfaceContainer.mIsBoosted;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTaskId(int taskId) {
        return this.mTaskId == taskId;
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.Task asTask() {
        return this;
    }

    com.android.server.wm.ActivityRecord isInTask(com.android.server.wm.ActivityRecord r) {
        if (r == null || !r.isDescendantOf(this)) {
            return null;
        }
        return r;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("userId=");
        pw.print(this.mUserId);
        pw.print(" effectiveUid=");
        android.os.UserHandle.formatUid(pw, this.effectiveUid);
        pw.print(" mCallingUid=");
        android.os.UserHandle.formatUid(pw, this.mCallingUid);
        pw.print(" mUserSetupComplete=");
        pw.print(this.mUserSetupComplete);
        pw.print(" mCallingPackage=");
        pw.print(this.mCallingPackage);
        pw.print(" mCallingFeatureId=");
        pw.println(this.mCallingFeatureId);
        if (this.affinity != null || this.rootAffinity != null) {
            pw.print(prefix);
            pw.print("affinity=");
            pw.print(this.affinity);
            if (this.affinity == null || !this.affinity.equals(this.rootAffinity)) {
                pw.print(" root=");
                pw.println(this.rootAffinity);
            } else {
                pw.println();
            }
        }
        if (this.mWindowLayoutAffinity != null) {
            pw.print(prefix);
            pw.print("windowLayoutAffinity=");
            pw.println(this.mWindowLayoutAffinity);
        }
        if (this.voiceSession != null || this.voiceInteractor != null) {
            pw.print(prefix);
            pw.print("VOICE: session=0x");
            pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.voiceSession)));
            pw.print(" interactor=0x");
            pw.println(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.voiceInteractor)));
        }
        if (this.intent != null) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
            sb.append(prefix);
            sb.append("intent={");
            this.intent.toShortString(sb, false, true, false, false);
            sb.append('}');
            pw.println(sb.toString());
        }
        if (this.affinityIntent != null) {
            java.lang.StringBuilder sb2 = new java.lang.StringBuilder(128);
            sb2.append(prefix);
            sb2.append("affinityIntent={");
            this.affinityIntent.toShortString(sb2, false, true, false, false);
            sb2.append('}');
            pw.println(sb2.toString());
        }
        if (this.origActivity != null) {
            pw.print(prefix);
            pw.print("origActivity=");
            pw.println(this.origActivity.flattenToShortString());
        }
        if (this.realActivity != null) {
            pw.print(prefix);
            pw.print("mActivityComponent=");
            pw.println(this.realActivity.flattenToShortString());
        }
        if (this.autoRemoveRecents || this.isPersistable || !isActivityTypeStandard()) {
            pw.print(prefix);
            pw.print("autoRemoveRecents=");
            pw.print(this.autoRemoveRecents);
            pw.print(" isPersistable=");
            pw.print(this.isPersistable);
            pw.print(" activityType=");
            pw.println(getActivityType());
        }
        if (this.rootWasReset || this.mNeverRelinquishIdentity || this.mReuseTask || this.mLockTaskAuth != 1) {
            pw.print(prefix);
            pw.print("rootWasReset=");
            pw.print(this.rootWasReset);
            pw.print(" mNeverRelinquishIdentity=");
            pw.print(this.mNeverRelinquishIdentity);
            pw.print(" mReuseTask=");
            pw.print(this.mReuseTask);
            pw.print(" mLockTaskAuth=");
            pw.println(lockTaskAuthToString());
        }
        if (this.mAffiliatedTaskId != this.mTaskId || this.mPrevAffiliateTaskId != -1 || this.mPrevAffiliate != null || this.mNextAffiliateTaskId != -1 || this.mNextAffiliate != null) {
            pw.print(prefix);
            pw.print("affiliation=");
            pw.print(this.mAffiliatedTaskId);
            pw.print(" prevAffiliation=");
            pw.print(this.mPrevAffiliateTaskId);
            pw.print(" (");
            if (this.mPrevAffiliate == null) {
                pw.print("null");
            } else {
                pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.mPrevAffiliate)));
            }
            pw.print(") nextAffiliation=");
            pw.print(this.mNextAffiliateTaskId);
            pw.print(" (");
            if (this.mNextAffiliate == null) {
                pw.print("null");
            } else {
                pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.mNextAffiliate)));
            }
            pw.println(")");
        }
        pw.print(prefix);
        pw.print("Activities=");
        pw.println(this.mChildren);
        if (!this.inRecents || !this.isAvailable) {
            pw.print(prefix);
            pw.print(" inRecents=");
            pw.print(this.inRecents);
            pw.print(" isAvailable=");
            pw.println(this.isAvailable);
        }
        if (this.lastDescription != null) {
            pw.print(prefix);
            pw.print("lastDescription=");
            pw.println(this.lastDescription);
        }
        if (this.mRootProcess != null) {
            pw.print(prefix);
            pw.print("mRootProcess=");
            pw.println(this.mRootProcess);
        }
        if (this.mSharedStartingData != null) {
            pw.println(prefix + "mSharedStartingData=" + this.mSharedStartingData);
        }
        if (this.mKillProcessesOnDestroyed) {
            pw.println(prefix + "mKillProcessesOnDestroyed=true");
        }
        pw.print(prefix);
        pw.print("taskId=" + this.mTaskId);
        pw.println(" rootTaskId=" + getRootTaskId());
        pw.print(prefix);
        pw.println("hasChildPipActivity=" + (this.mChildPipActivity != null));
        pw.print(prefix);
        pw.print("mHasBeenVisible=");
        pw.println(getHasBeenVisible());
        pw.print(prefix);
        pw.print("mResizeMode=");
        pw.print(android.content.pm.ActivityInfo.resizeModeToString(this.mResizeMode));
        pw.print(" mSupportsPictureInPicture=");
        pw.print(this.mSupportsPictureInPicture);
        pw.print(" isResizeable=");
        pw.println(isResizeable());
        pw.print(prefix);
        pw.print("lastActiveTime=");
        pw.print(this.lastActiveTime);
        pw.println(" (inactive for " + (getInactiveDuration() / 1000) + "s)");
        this.mTaskWrapper.getExtImpl().dump(pw, prefix);
    }

    @Override // com.android.server.wm.TaskFragment
    java.lang.String toFullString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(192);
        sb.append(this);
        sb.setLength(sb.length() - 1);
        sb.append(" U=");
        sb.append(this.mUserId);
        com.android.server.wm.Task rootTask = getRootTask();
        if (rootTask != this) {
            sb.append(" rootTaskId=");
            sb.append(rootTask.mTaskId);
        }
        sb.append(" visible=");
        sb.append(shouldBeVisible(null));
        sb.append(" visibleRequested=");
        sb.append(isVisibleRequested());
        sb.append(" mode=");
        sb.append(android.app.WindowConfiguration.windowingModeToString(getWindowingMode()));
        sb.append(" translucent=");
        sb.append(isTranslucent(null));
        sb.append(" sz=");
        sb.append(getChildCount());
        sb.append('}');
        return sb.toString();
    }

    @Override // com.android.server.wm.TaskFragment
    public java.lang.String toString() {
        if (this.stringName != null) {
            return this.stringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("Task{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(" #");
        sb.append(this.mTaskId);
        sb.append(" type=" + android.app.WindowConfiguration.activityTypeToString(getActivityType()));
        if (this.affinity != null) {
            sb.append(" A=");
            sb.append(this.affinity);
        } else if (this.intent != null && this.intent.getComponent() != null) {
            sb.append(" I=");
            sb.append(this.intent.getComponent().flattenToShortString());
        } else if (this.affinityIntent != null && this.affinityIntent.getComponent() != null) {
            sb.append(" aI=");
            sb.append(this.affinityIntent.getComponent().flattenToShortString());
        }
        sb.append('}');
        java.lang.String string = sb.toString();
        this.stringName = string;
        return string;
    }

    void saveToXml(com.android.modules.utils.TypedXmlSerializer out) throws java.lang.Exception {
        java.lang.String info;
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
            android.util.Slog.i(TAG_RECENTS, "Saving task=" + this);
        }
        out.attributeInt((java.lang.String) null, ATTR_TASKID, this.mTaskId);
        if (this.realActivity != null) {
            out.attribute((java.lang.String) null, ATTR_REALACTIVITY, this.realActivity.flattenToShortString());
        }
        out.attributeBoolean((java.lang.String) null, ATTR_REALACTIVITY_SUSPENDED, this.realActivitySuspended);
        if (this.origActivity != null) {
            out.attribute((java.lang.String) null, ATTR_ORIGACTIVITY, this.origActivity.flattenToShortString());
        }
        if (this.affinity != null) {
            out.attribute((java.lang.String) null, ATTR_AFFINITY, this.affinity);
            if (!this.affinity.equals(this.rootAffinity)) {
                out.attribute((java.lang.String) null, ATTR_ROOT_AFFINITY, this.rootAffinity != null ? this.rootAffinity : "@");
            }
        } else if (this.rootAffinity != null) {
            out.attribute((java.lang.String) null, ATTR_ROOT_AFFINITY, this.rootAffinity != null ? this.rootAffinity : "@");
        }
        if (this.mWindowLayoutAffinity != null) {
            out.attribute((java.lang.String) null, ATTR_WINDOW_LAYOUT_AFFINITY, this.mWindowLayoutAffinity);
        }
        out.attributeBoolean((java.lang.String) null, ATTR_ROOTHASRESET, this.rootWasReset);
        out.attributeBoolean((java.lang.String) null, ATTR_AUTOREMOVERECENTS, this.autoRemoveRecents);
        out.attributeInt((java.lang.String) null, ATTR_USERID, this.mUserId);
        out.attributeBoolean((java.lang.String) null, ATTR_USER_SETUP_COMPLETE, this.mUserSetupComplete);
        out.attributeInt((java.lang.String) null, ATTR_EFFECTIVE_UID, this.effectiveUid);
        out.attributeLong((java.lang.String) null, ATTR_LASTTIMEMOVED, this.mLastTimeMoved);
        out.attributeBoolean((java.lang.String) null, ATTR_NEVERRELINQUISH, this.mNeverRelinquishIdentity);
        if (this.lastDescription != null) {
            out.attribute((java.lang.String) null, ATTR_LASTDESCRIPTION, this.lastDescription.toString());
        }
        if (getTaskDescription() != null) {
            getTaskDescription().saveToXml(out);
        }
        out.attributeInt((java.lang.String) null, ATTR_TASK_AFFILIATION, this.mAffiliatedTaskId);
        out.attributeInt((java.lang.String) null, ATTR_PREV_AFFILIATION, this.mPrevAffiliateTaskId);
        out.attributeInt((java.lang.String) null, ATTR_NEXT_AFFILIATION, this.mNextAffiliateTaskId);
        out.attributeInt((java.lang.String) null, ATTR_CALLING_UID, this.mCallingUid);
        out.attribute((java.lang.String) null, ATTR_CALLING_PACKAGE, this.mCallingPackage == null ? "" : this.mCallingPackage);
        out.attribute((java.lang.String) null, ATTR_CALLING_FEATURE_ID, this.mCallingFeatureId == null ? "" : this.mCallingFeatureId);
        out.attributeInt((java.lang.String) null, ATTR_RESIZE_MODE, this.mResizeMode);
        out.attributeBoolean((java.lang.String) null, ATTR_SUPPORTS_PICTURE_IN_PICTURE, this.mSupportsPictureInPicture);
        if (this.mLastNonFullscreenBounds != null) {
            out.attribute((java.lang.String) null, ATTR_NON_FULLSCREEN_BOUNDS, this.mLastNonFullscreenBounds.flattenToString());
        }
        out.attributeInt((java.lang.String) null, ATTR_MIN_WIDTH, this.mMinWidth);
        out.attributeInt((java.lang.String) null, ATTR_MIN_HEIGHT, this.mMinHeight);
        out.attributeInt((java.lang.String) null, ATTR_PERSIST_TASK_VERSION, 1);
        if (this.mLastTaskSnapshotData.taskSize != null) {
            out.attribute((java.lang.String) null, ATTR_LAST_SNAPSHOT_TASK_SIZE, this.mLastTaskSnapshotData.taskSize.flattenToString());
        }
        if (this.mLastTaskSnapshotData.contentInsets != null) {
            out.attribute((java.lang.String) null, ATTR_LAST_SNAPSHOT_CONTENT_INSETS, this.mLastTaskSnapshotData.contentInsets.flattenToString());
        }
        if (this.mLastTaskSnapshotData.bufferSize != null) {
            out.attribute((java.lang.String) null, ATTR_LAST_SNAPSHOT_BUFFER_SIZE, this.mLastTaskSnapshotData.bufferSize.flattenToString());
        }
        if (this.affinityIntent != null) {
            out.startTag((java.lang.String) null, TAG_AFFINITYINTENT);
            this.affinityIntent.saveToXml(out);
            out.endTag((java.lang.String) null, TAG_AFFINITYINTENT);
        }
        if (this.intent != null) {
            out.startTag((java.lang.String) null, TAG_INTENT);
            this.intent.saveToXml(out);
            out.endTag((java.lang.String) null, TAG_INTENT);
        }
        out.startTag((java.lang.String) null, ATTR_EMBEDDED_CONTAINER);
        out.attributeBoolean((java.lang.String) null, ATTR_IS_CONTAINER_TASK, this.mTaskWrapper.getExtImpl().isContainerTask());
        out.attributeBoolean((java.lang.String) null, ATTR_IS_SHOW_RECENT, this.mTaskWrapper.getExtImpl().isShowRecent());
        out.attributeInt((java.lang.String) null, ATTR_CONTAINER_TASK_ID, this.mTaskWrapper.getExtImpl().getEmbeddedContainerTaskId());
        if (this.mTaskWrapper.getExtImpl().getEmbeddedChildren() != null && this.mTaskWrapper.getExtImpl().getEmbeddedChildren().size() > 0 && (info = this.mTaskWrapper.getExtImpl().getCanvasTaskChildrenInfoForXML()) != null && !info.equals("")) {
            out.attribute((java.lang.String) null, ATTR_EMBEDDED_CHILDREN, info);
        }
        if (this.intent != null && (this.intent.getIntentExt().getOplusFlags() & 16384) != 0) {
            out.attributeInt((java.lang.String) null, ATTR_OPLUS_FLAGS, 16384);
        }
        out.endTag((java.lang.String) null, ATTR_EMBEDDED_CONTAINER);
        sTmpException = null;
        com.android.internal.util.function.pooled.PooledPredicate f = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new com.android.internal.util.function.TriPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda30
            public final boolean test(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                return com.android.server.wm.Task.saveActivityToXml((com.android.server.wm.ActivityRecord) obj, (com.android.server.wm.ActivityRecord) obj2, (com.android.modules.utils.TypedXmlSerializer) obj3);
            }
        }, com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.ActivityRecord.class), getBottomMostActivity(), out);
        forAllActivities((java.util.function.Predicate<com.android.server.wm.ActivityRecord>) f);
        f.recycle();
        if (sTmpException != null) {
            throw sTmpException;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean saveActivityToXml(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord first, com.android.modules.utils.TypedXmlSerializer out) {
        if (r.info.persistableMode == 0 || !r.isPersistable() || (((r.intent.getFlags() & 524288) | 8192) == 524288 && r != first)) {
            return true;
        }
        try {
            out.startTag((java.lang.String) null, "activity");
            r.saveToXml(out);
            out.endTag((java.lang.String) null, "activity");
            return false;
        } catch (java.lang.Exception e) {
            sTmpException = e;
            return true;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0221  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0618  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static com.android.server.wm.Task restoreFromXml(com.android.modules.utils.TypedXmlPullParser r58, com.android.server.wm.ActivityTaskSupervisor r59) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instruction units count: 2144
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.Task.restoreFromXml(com.android.modules.utils.TypedXmlPullParser, com.android.server.wm.ActivityTaskSupervisor):com.android.server.wm.Task");
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    boolean isOrganized() {
        return this.mTaskOrganizer != null;
    }

    private boolean canBeOrganized() {
        if (isRootTask() || this.mCreatedByOrganizer) {
            return true;
        }
        com.android.server.wm.Task parentTask = getParent().asTask();
        return parentTask != null && parentTask.mCreatedByOrganizer;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean showSurfaceOnCreation() {
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    protected void reparentSurfaceControl(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl newParent) {
        if (isOrganized() && isAlwaysOnTop() && !getWrapper().getExtImpl().isFlexibleTaskAndHasCaption(this)) {
            return;
        }
        super.reparentSurfaceControl(t, newParent);
    }

    void setHasBeenVisible(boolean hasBeenVisible) {
        this.mHasBeenVisible = hasBeenVisible;
        if (!hasBeenVisible) {
            return;
        }
        if (!this.mDeferTaskAppear) {
            sendTaskAppeared();
        }
        for (com.android.server.wm.WindowContainer<?> parent = getParent(); parent != null; parent = parent.getParent()) {
            com.android.server.wm.Task parentTask = parent.asTask();
            if (parentTask != null) {
                parentTask.setHasBeenVisible(true);
            } else {
                return;
            }
        }
    }

    boolean getHasBeenVisible() {
        return this.mHasBeenVisible;
    }

    void setDeferTaskAppear(boolean deferTaskAppear) {
        boolean wasDeferred = this.mDeferTaskAppear;
        this.mDeferTaskAppear = deferTaskAppear;
        if (wasDeferred && !deferTaskAppear) {
            sendTaskAppeared();
        }
    }

    boolean taskAppearedReady() {
        if (this.mTaskOrganizer == null || this.mDeferTaskAppear) {
            return false;
        }
        if (this.mCreatedByOrganizer) {
            return true;
        }
        return this.mSurfaceControl != null && getHasBeenVisible();
    }

    private void sendTaskAppeared() {
        if (this.mTaskOrganizer != null) {
            this.mAtmService.mTaskOrganizerController.onTaskAppeared(this.mTaskOrganizer, this);
        }
    }

    private void sendTaskVanished(android.window.ITaskOrganizer organizer) {
        if (organizer != null) {
            this.mAtmService.mTaskOrganizerController.onTaskVanished(organizer, this);
        }
    }

    boolean setTaskOrganizer(android.window.ITaskOrganizer organizer) {
        return setTaskOrganizer(organizer, false);
    }

    boolean setTaskOrganizer(android.window.ITaskOrganizer organizer, boolean skipTaskAppeared) {
        if (this.mTaskOrganizer == organizer) {
            return false;
        }
        android.window.ITaskOrganizer prevOrganizer = this.mTaskOrganizer;
        this.mTaskOrganizer = organizer;
        sendTaskVanished(prevOrganizer);
        if (this.mTaskOrganizer != null) {
            if (!skipTaskAppeared) {
                sendTaskAppeared();
                return true;
            }
            return true;
        }
        com.android.server.wm.TaskDisplayArea taskDisplayArea = getDisplayArea();
        if (taskDisplayArea != null) {
            taskDisplayArea.removeLaunchRootTask(this);
        }
        setForceHidden(2, false);
        if (this.mCreatedByOrganizer) {
            removeImmediately("setTaskOrganizer");
            return true;
        }
        return true;
    }

    boolean updateTaskOrganizerState() {
        return updateTaskOrganizerState(false);
    }

    boolean updateTaskOrganizerState(boolean skipTaskAppeared) {
        if (getSurfaceControl() == null) {
            return false;
        }
        if (!canBeOrganized()) {
            return setTaskOrganizer(null);
        }
        com.android.server.wm.TaskOrganizerController controller = this.mWmService.mAtmService.mTaskOrganizerController;
        android.window.ITaskOrganizer organizer = controller.getTaskOrganizer();
        if (!this.mCreatedByOrganizer || this.mTaskOrganizer == null || organizer == null || this.mTaskOrganizer == organizer) {
            return setTaskOrganizer(organizer, skipTaskAppeared);
        }
        return false;
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void setSurfaceControl(android.view.SurfaceControl sc) {
        super.setSurfaceControl(sc);
        sendTaskAppeared();
    }

    boolean isFocused() {
        if (this.mDisplayContent == null || this.mDisplayContent.mFocusedApp == null) {
            return false;
        }
        com.android.server.wm.Task focusedTask = this.mDisplayContent.mFocusedApp.getTask();
        return focusedTask == this || (focusedTask != null && focusedTask.getParent() == this);
    }

    private boolean hasVisibleChildren() {
        return (!isAttached() || isForceHidden() || getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda36
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.ActivityRecord) obj).isVisible();
            }
        }) == null) ? false : true;
    }

    void onAppFocusChanged(boolean hasFocus) {
        if (hasFocus && (!this.mTaskWrapper.getExtImpl().isInPendingAnimation(this) || !this.mTaskWrapper.getExtImpl().isDragZoomMode())) {
            if (this.mTaskWrapper.getExtImpl().shouldSkipLaunchIntoCompactWindowingMode()) {
                this.mTaskWrapper.getExtImpl().skipNextLaunchIntoCompactWindowingMode(false);
            } else {
                android.util.Slog.d(TAG, "onWindowFocusChanged " + this + " would transfer to compact");
                this.mTaskWrapper.getExtImpl().launchIntoCompactwindowingMode(this, false);
            }
        }
        dispatchTaskInfoChangedIfNeeded(false);
        com.android.server.wm.Task parentTask = getParent().asTask();
        if (parentTask != null) {
            parentTask.dispatchTaskInfoChangedIfNeeded(false);
        }
        this.mAtmService.getTaskChangeNotificationController().notifyTaskFocusChanged(this.mTaskId, hasFocus);
    }

    void onPictureInPictureParamsChanged() {
        if (inPinnedWindowingMode()) {
            dispatchTaskInfoChangedIfNeeded(true);
        }
    }

    void onShouldDockBigOverlaysChanged() {
        dispatchTaskInfoChangedIfNeeded(true);
    }

    void onSizeCompatActivityChanged() {
        dispatchTaskInfoChangedIfNeeded(true);
    }

    void setMainWindowSizeChangeTransaction(android.view.SurfaceControl.Transaction t) {
        setMainWindowSizeChangeTransaction(t, this);
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.WindowState) obj).requestRedrawForSync();
            }
        }, true);
    }

    private void setMainWindowSizeChangeTransaction(final android.view.SurfaceControl.Transaction t, com.android.server.wm.Task origin) {
        com.android.server.wm.ActivityRecord topActivity = getTopNonFinishingActivity();
        com.android.server.wm.Task leaf = topActivity != null ? topActivity.getTask() : null;
        if (leaf == null) {
            return;
        }
        if (leaf != this) {
            leaf.setMainWindowSizeChangeTransaction(t, origin);
            return;
        }
        final com.android.server.wm.WindowState w = getTopVisibleAppMainWindow();
        if (w != null) {
            w.mIsSurfacePositionPaused = true;
            w.applyWithNextDraw(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda42
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.Task.lambda$setMainWindowSizeChangeTransaction$17(w, t, (android.view.SurfaceControl.Transaction) obj);
                }
            });
        } else {
            t.apply();
        }
    }

    static /* synthetic */ void lambda$setMainWindowSizeChangeTransaction$17(com.android.server.wm.WindowState w, android.view.SurfaceControl.Transaction t, android.view.SurfaceControl.Transaction d) {
        w.mIsSurfacePositionPaused = false;
        w.updateSurfacePosition(d);
        d.merge(t);
    }

    @Override // com.android.server.wm.TaskFragment
    boolean setForceHidden(int flags, boolean set) {
        boolean wasHidden = isForceHidden();
        boolean wasVisible = isVisible();
        if (!super.setForceHidden(flags, set)) {
            return false;
        }
        boolean nowHidden = isForceHidden();
        if (wasHidden == nowHidden) {
            return true;
        }
        if (wasVisible && nowHidden) {
            moveToBack("setForceHidden", null);
            return true;
        }
        if (isAlwaysOnTop() || (getWrapper().getExtImpl().isAlwaysOnTop() && !nowHidden)) {
            moveToFront("setForceHidden");
            return true;
        }
        return true;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean isAlwaysOnTop() {
        if (getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0]) && (!getWrapper().getExtImpl().isFlexibleTaskAndHasCaption(this) || !getWrapper().getExtImpl().isPuttTask())) {
            return !isForceHidden() && getWindowConfiguration().isFlexibleAlwaysOnTop();
        }
        if (isForceHidden()) {
            return false;
        }
        return super.isAlwaysOnTop() || getWrapper().getExtImpl().isForceAlwaysOnTop(this);
    }

    public boolean isAlwaysOnTopWhenVisible() {
        return super.isAlwaysOnTop();
    }

    boolean isForceHiddenForPinnedTask() {
        return (this.mForceHiddenFlags & 1) != 0;
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268037L;
    }

    void restoreWindowingMode() {
        if (this.mMultiWindowRestoreWindowingMode == -1) {
            return;
        }
        if (!getParent().mRemoteToken.toWindowContainerToken().equals(this.mMultiWindowRestoreParent)) {
            com.android.server.wm.Task parent = fromWindowContainerToken(this.mMultiWindowRestoreParent);
            reparent(parent, Integer.MAX_VALUE);
        }
        setWindowingMode(this.mMultiWindowRestoreWindowingMode);
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void setWindowingMode(int windowingMode) {
        if (!isRootTask()) {
            this.mMultiWindowRestoreWindowingMode = -1;
            super.setWindowingMode(windowingMode);
        } else {
            setWindowingMode(windowingMode, false);
        }
    }

    void setWindowingMode(int preferredWindowingMode, boolean creating) {
        com.android.server.wm.TaskDisplayArea taskDisplayArea = getDisplayArea();
        if (taskDisplayArea == null) {
            android.util.Slog.d(TAG, "taskDisplayArea is null, bail early");
            return;
        }
        int currentMode = getWindowingMode();
        com.android.server.wm.Task topTask = getTopMostTask();
        int windowingMode = preferredWindowingMode;
        if (!creating && !taskDisplayArea.isValidWindowingMode(windowingMode, null, topTask)) {
            windowingMode = 0;
        }
        if (currentMode != windowingMode) {
            this.mMultiWindowRestoreWindowingMode = -1;
            com.android.server.wm.ActivityRecord topActivity = getTopNonFinishingActivity();
            int likelyResolvedMode = windowingMode;
            if (windowingMode == 0) {
                com.android.server.wm.ConfigurationContainer parent = getParent();
                likelyResolvedMode = parent != null ? parent.getWindowingMode() : 1;
            }
            if (currentMode == 2) {
                setCanAffectSystemUiFlags(true);
                this.mRootWindowContainer.notifyActivityPipModeChanged(this, null);
            }
            if (likelyResolvedMode == 2 && taskDisplayArea.getRootPinnedTask() != null) {
                taskDisplayArea.getRootPinnedTask().dismissPip();
            }
            if (likelyResolvedMode != 1 && topActivity != null && !topActivity.noDisplay && preferredWindowingMode != 6 && !this.mTaskWrapper.getExtImpl().isZoomMode(preferredWindowingMode) && !this.mTaskWrapper.getExtImpl().isCompactWindowingMode(preferredWindowingMode) && topActivity.canForceResizeNonResizable(likelyResolvedMode)) {
                java.lang.String packageName = topActivity.info.applicationInfo.packageName;
                this.mAtmService.getTaskChangeNotificationController().notifyActivityForcedResizable(topTask.mTaskId, 1, packageName);
            }
            this.mAtmService.deferWindowLayout();
            if (topActivity != null) {
                try {
                    this.mTaskSupervisor.mNoAnimActivities.add(topActivity);
                } finally {
                    this.mAtmService.continueWindowLayout();
                }
            }
            boolean isPip2ExperimentEnabled = com.android.server.wm.ActivityTaskManagerService.isPip2ExperimentEnabled();
            if (!isPip2ExperimentEnabled) {
                super.setWindowingMode(windowingMode);
                this.mTaskWrapper.getExtImpl().onWindowingModeChanged(this, currentMode);
            }
            if (currentMode == 2 && topActivity != null) {
                if (topActivity.getLastParentBeforePip() != null && !isForceHidden() && topActivity.getLastParentBeforePip().isAttached()) {
                    this.mTransitionController.collect(topActivity);
                    com.android.server.wm.Task lastParentBeforePip = topActivity.getLastParentBeforePip();
                    if (this.mTaskFragmentExt.isCompactMode(lastParentBeforePip.getWindowingMode())) {
                        lastParentBeforePip.moveToFront("movePinActivityToOriginTask-beforeReprent");
                    }
                    getWrapper().getExtImpl().pipToFullScreen(lastParentBeforePip, topActivity);
                    topActivity.reparent(lastParentBeforePip, lastParentBeforePip.getChildCount(), "movePinnedActivityToOriginalTask");
                    com.android.server.wm.DisplayContent dc = topActivity.getDisplayContent();
                    if (dc != null && dc.isFixedRotationLaunchingApp(topActivity)) {
                        topActivity.getOrCreateFixedRotationLeash(topActivity.getSyncTransaction());
                    }
                    lastParentBeforePip.moveToFront("movePinnedActivityToOriginalTask");
                    if (com.android.window.flags.Flags.removePrepareSurfaceInPlacement() && lastParentBeforePip.mSyncState == 0) {
                        lastParentBeforePip.prepareSurfaces();
                        if (topActivity.mTransitionController.inFinishingTransition(topActivity)) {
                            com.android.server.wm.Transition.assignLayers(taskDisplayArea, taskDisplayArea.getPendingTransaction());
                            android.view.SurfaceControl leash = topActivity.getFixedRotationLeash();
                            if (leash != null) {
                                taskDisplayArea.getPendingTransaction().setLayer(leash, topActivity.getLastLayer());
                            }
                        }
                    }
                }
                if (isPip2ExperimentEnabled) {
                    super.setWindowingMode(windowingMode);
                }
                if (topActivity.shouldBeVisible()) {
                    this.mAtmService.resumeAppSwitches();
                    if (!isPip2ExperimentEnabled) {
                        com.android.server.wm.ActivityRecord ar = this.mAtmService.mLastResumedActivity;
                        if (ar != null && ar.getTask() != null) {
                            this.mAtmService.takeTaskSnapshot(ar.getTask().mTaskId, true);
                        }
                    }
                }
            } else if (isPip2ExperimentEnabled) {
                super.setWindowingMode(windowingMode);
            }
            this.mTaskWrapper.getExtImpl().notifyZoomModeChanged(getWindowingMode(), currentMode);
            if (creating) {
                return;
            }
            if (topActivity != null && currentMode == 1 && windowingMode == 2 && !this.mTransitionController.isShellTransitionsEnabled()) {
                this.mDisplayContent.mPinnedTaskController.deferOrientationChangeForEnteringPipFromFullScreenIfNeeded();
            }
            this.mAtmService.continueWindowLayout();
            if (!this.mTaskSupervisor.isRootVisibilityUpdateDeferred()) {
                this.mRootWindowContainer.ensureActivitiesVisible();
                this.mRootWindowContainer.resumeFocusedTasksTopActivities();
                return;
            }
            return;
        }
        getRequestedOverrideConfiguration().windowConfiguration.setWindowingMode(windowingMode);
    }

    boolean abortPipEnter(com.android.server.wm.ActivityRecord top) {
        if (!inPinnedWindowingMode() || top.inPinnedWindowingMode() || !canMoveTaskToBack(this)) {
            return false;
        }
        com.android.server.wm.Transition transition = new com.android.server.wm.Transition(4, 0, this.mTransitionController, this.mWmService.mSyncEngine);
        this.mTransitionController.moveToCollecting(transition);
        this.mTransitionController.requestStartTransition(transition, this, null, null);
        if (top.getLastParentBeforePip() != null) {
            com.android.server.wm.Task lastParentBeforePip = top.getLastParentBeforePip();
            if (lastParentBeforePip.isAttached()) {
                top.reparent(lastParentBeforePip, lastParentBeforePip.getChildCount(), "movePinnedActivityToOriginalTask");
            }
        }
        if (isAttached()) {
            setWindowingMode(0);
            moveTaskToBackInner(this, null);
        }
        if (top.isAttached()) {
            top.setWindowingMode(0);
            top.mWaitForEnteringPinnedMode = false;
            return true;
        }
        return true;
    }

    void resumeNextFocusAfterReparent() {
        adjustFocusToNextFocusableTask("reparent", true, true);
        this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        this.mRootWindowContainer.ensureActivitiesVisible();
    }

    final boolean isOnHomeDisplay() {
        return getDisplayId() == 0;
    }

    void moveToFront(java.lang.String reason) {
        moveToFront(reason, null);
    }

    void moveToFront(java.lang.String reason, com.android.server.wm.Task task) {
        if (!isAttached()) {
            return;
        }
        this.mTransitionController.recordTaskOrder(this);
        com.android.server.wm.TaskDisplayArea taskDisplayArea = getDisplayArea();
        if (!isActivityTypeHome() && returnsToHomeRootTask()) {
            taskDisplayArea.moveHomeRootTaskToFront(reason + " returnToHome");
        }
        if (getWrapper().getExtImpl().isAlwaysOnTop()) {
            setAlwaysOnTopOnly(true);
        }
        com.android.server.wm.Task lastFocusedTask = isRootTask() ? taskDisplayArea.getFocusedRootTask() : null;
        if (task == null) {
            task = this;
        }
        this.mAtmService.getWrapper().getFlexibleExtImpl().moveTaskToFront(task, null, reason);
        getWrapper().getExtImpl().forceHideByRemoveTask(false);
        task.getParent().positionChildAt(Integer.MAX_VALUE, task, true);
        taskDisplayArea.updateLastFocusedRootTask(lastFocusedTask, reason);
    }

    void moveToBack(java.lang.String reason, com.android.server.wm.Task task) {
        if (!isAttached()) {
            return;
        }
        if (getWrapper().getExtImpl().isAlwaysOnTop()) {
            setAlwaysOnTopOnly(false);
        }
        com.android.server.wm.TaskDisplayArea displayArea = getDisplayArea();
        if (!this.mCreatedByOrganizer || this.mTaskWrapper.getExtImpl().isZoomMode(getWindowingMode())) {
            com.android.server.wm.WindowContainer parent = getParent();
            com.android.server.wm.Task parentTask = parent != null ? parent.asTask() : null;
            if (parentTask != null) {
                parentTask.moveToBack(reason, this);
            } else {
                com.android.server.wm.Task lastFocusedTask = displayArea.getFocusedRootTask();
                displayArea.positionChildAt(Integer.MIN_VALUE, this, false);
                displayArea.updateLastFocusedRootTask(lastFocusedTask, reason);
            }
            if (task != null && task != this) {
                if (this.mTaskWrapper.getExtImpl().isZoomMode(getWindowingMode())) {
                    if (this.mTaskWrapper.getExtImpl().isParentChanged(task.getParent() != null ? task.getParent().asTask() : null, this)) {
                        return;
                    }
                }
                positionChildAtBottom(task);
                return;
            }
            return;
        }
        if (task == null || task == this) {
            return;
        }
        displayArea.positionTaskBehindHome(task);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void switchUser(int userId) {
        if (this.mCurrentUser == userId) {
            return;
        }
        this.mCurrentUser = userId;
        super.switchUser(userId);
        if (!isRootTask() && showToCurrentUser()) {
            getParent().positionChildAt(Integer.MAX_VALUE, this, false);
        }
    }

    void checkReadyForSleep() {
        if (shouldSleepActivities() && goToSleepIfPossible(false)) {
            this.mTaskSupervisor.checkReadyForSleepLocked(true);
        }
    }

    boolean goToSleepIfPossible(final boolean shuttingDown) {
        final int[] sleepInProgress = {0};
        forAllLeafTasksAndLeafTaskFragments(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.Task.lambda$goToSleepIfPossible$18(shuttingDown, sleepInProgress, (com.android.server.wm.TaskFragment) obj);
            }
        }, true);
        return sleepInProgress[0] == 0;
    }

    static /* synthetic */ void lambda$goToSleepIfPossible$18(boolean shuttingDown, int[] sleepInProgress, com.android.server.wm.TaskFragment taskFragment) {
        if (!taskFragment.sleepIfPossible(shuttingDown)) {
            sleepInProgress[0] = sleepInProgress[0] + 1;
        }
    }

    boolean isTopRootTaskInDisplayArea() {
        com.android.server.wm.TaskDisplayArea taskDisplayArea = getDisplayArea();
        return taskDisplayArea != null && taskDisplayArea.isTopRootTask(this);
    }

    boolean isFocusedRootTaskOnDisplay() {
        return this.mDisplayContent != null && this == this.mDisplayContent.getFocusedRootTask();
    }

    void ensureActivitiesVisible(com.android.server.wm.ActivityRecord starting) {
        ensureActivitiesVisible(starting, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void ensureActivitiesVisible(final com.android.server.wm.ActivityRecord starting, final boolean notifyClients) {
        this.mTaskSupervisor.beginActivityVisibilityUpdate();
        try {
            forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wm.Task) obj).updateActivityVisibilities(starting, notifyClients);
                }
            }, true);
            if (this.mTranslucentActivityWaiting != null && this.mUndrawnActivitiesBelowTopTranslucent.isEmpty()) {
                notifyActivityDrawnLocked(null);
            }
        } finally {
            this.mTaskSupervisor.endActivityVisibilityUpdate();
        }
    }

    void abortTranslucentActivityWaiting(com.android.server.wm.ActivityRecord r) {
        if (r != this.mTranslucentActivityWaiting && r != this.mPendingConvertFromTranslucentActivity) {
            return;
        }
        if (this.mTranslucentActivityWaiting != null) {
            if (!this.mTranslucentActivityWaiting.finishing) {
                this.mTranslucentActivityWaiting.setOccludesParent(true);
            }
            this.mTranslucentActivityWaiting = null;
        }
        if (this.mPendingConvertFromTranslucentActivity != null) {
            if (!this.mPendingConvertFromTranslucentActivity.finishing) {
                this.mPendingConvertFromTranslucentActivity.setOccludesParent(true);
            }
            this.mPendingConvertFromTranslucentActivity = null;
        }
        this.mUndrawnActivitiesBelowTopTranslucent.clear();
        this.mHandler.removeMessages(101);
    }

    void checkTranslucentActivityWaiting(com.android.server.wm.ActivityRecord top) {
        if (this.mTranslucentActivityWaiting != top) {
            this.mUndrawnActivitiesBelowTopTranslucent.clear();
            if (this.mTranslucentActivityWaiting != null) {
                notifyActivityDrawnLocked(null);
                this.mTranslucentActivityWaiting = null;
            }
            this.mHandler.removeMessages(101);
        }
    }

    void convertActivityToTranslucent(com.android.server.wm.ActivityRecord r) {
        this.mTranslucentActivityWaiting = r;
        this.mPendingConvertFromTranslucentActivity = r;
        this.mUndrawnActivitiesBelowTopTranslucent.clear();
        this.mHandler.sendEmptyMessageDelayed(101, TRANSLUCENT_CONVERSION_TIMEOUT);
    }

    void convertActivityFromTranslucent(com.android.server.wm.ActivityRecord r) {
        if (r != this.mPendingConvertFromTranslucentActivity) {
            android.util.Slog.e(TAG, "convertFromTranslucent expects " + this.mPendingConvertFromTranslucentActivity + " but is " + r);
        }
        this.mPendingConvertFromTranslucentActivity = null;
    }

    void notifyActivityDrawnLocked(com.android.server.wm.ActivityRecord r) {
        if (r == null || (this.mUndrawnActivitiesBelowTopTranslucent.remove(r) && this.mUndrawnActivitiesBelowTopTranslucent.isEmpty())) {
            com.android.server.wm.ActivityRecord waitingActivity = this.mTranslucentActivityWaiting;
            this.mTranslucentActivityWaiting = null;
            this.mUndrawnActivitiesBelowTopTranslucent.clear();
            this.mHandler.removeMessages(101);
            if (waitingActivity != null) {
                waitingActivity.setMainWindowOpaque(false);
                if (waitingActivity.attachedToProcess()) {
                    try {
                        waitingActivity.app.getThread().scheduleTranslucentConversionComplete(waitingActivity.token, r != null);
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
        }
    }

    boolean resumeTopActivityUncheckedLocked(com.android.server.wm.ActivityRecord prev, android.app.ActivityOptions options, boolean deferPause) {
        if (this.mInResumeTopActivity) {
            return false;
        }
        if (this.mTransitionController.mExt.isTransientHideInRecentsFromRemote(this)) {
            android.util.Slog.d(TAG, "skip resumeTopActivityUncheckedLocked in " + this + " because isTransientHideInRecentsFromRemote");
            return false;
        }
        if (this.mTaskWrapper.getExtImpl().ignoreResumePuttTask(getRootTask())) {
            android.util.Slog.v(TAG, "putt ignore resume top: " + prev);
            return false;
        }
        boolean someActivityResumed = false;
        try {
            this.mInResumeTopActivity = true;
            if (!isLeafTask()) {
                int idx = this.mChildren.size() - 1;
                while (idx >= 0) {
                    int idx2 = idx - 1;
                    com.android.server.wm.Task child = (com.android.server.wm.Task) getChildAt(idx);
                    if (child.isTopActivityFocusable()) {
                        if (child.getVisibility(null) != 0) {
                            if (child.topRunningActivity() != null) {
                                break;
                            }
                        } else {
                            someActivityResumed |= child.resumeTopActivityUncheckedLocked(prev, options, deferPause);
                            if (idx2 >= this.mChildren.size()) {
                                idx = this.mChildren.size() - 1;
                            } else {
                                idx = idx2;
                            }
                        }
                    }
                    idx = idx2;
                }
            } else if (isFocusableAndVisible()) {
                someActivityResumed = resumeTopActivityInnerLocked(prev, options, deferPause);
            }
            com.android.server.wm.ActivityRecord next = topRunningActivity(true);
            if (next == null || !next.canTurnScreenOn()) {
                checkReadyForSleep();
            }
            return someActivityResumed;
        } finally {
            this.mInResumeTopActivity = false;
        }
    }

    boolean resumeTopActivityUncheckedLocked(com.android.server.wm.ActivityRecord prev, android.app.ActivityOptions options) {
        return resumeTopActivityUncheckedLocked(prev, options, false);
    }

    private boolean resumeTopActivityInnerLocked(final com.android.server.wm.ActivityRecord prev, final android.app.ActivityOptions options, final boolean deferPause) {
        if (!this.mAtmService.isBooting() && !this.mAtmService.isBooted()) {
            return false;
        }
        com.android.server.wm.ActivityRecord topActivity = topRunningActivity(true);
        if (topActivity == null) {
            return resumeNextFocusableActivityWhenRootTaskIsEmpty(prev, options);
        }
        final boolean[] resumed = new boolean[1];
        if (getWrapper().getExtImpl().resumeTopActivityInnerInCompactWindow(resumed, prev, options, deferPause)) {
            return resumed[0];
        }
        final com.android.server.wm.TaskFragment topFragment = topActivity.getTaskFragment();
        resumed[0] = topFragment.resumeTopActivity(prev, options, deferPause);
        forAllLeafTaskFragments(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda43
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.Task.lambda$resumeTopActivityInnerLocked$20(topFragment, resumed, prev, options, deferPause, (com.android.server.wm.TaskFragment) obj);
            }
        }, true);
        return resumed[0];
    }

    static /* synthetic */ void lambda$resumeTopActivityInnerLocked$20(com.android.server.wm.TaskFragment topFragment, boolean[] resumed, com.android.server.wm.ActivityRecord prev, android.app.ActivityOptions options, boolean deferPause, com.android.server.wm.TaskFragment f) {
        if (topFragment == f || !f.canBeResumed(null)) {
            return;
        }
        resumed[0] = resumed[0] | f.resumeTopActivity(prev, options, deferPause);
    }

    private boolean resumeNextFocusableActivityWhenRootTaskIsEmpty(com.android.server.wm.ActivityRecord prev, android.app.ActivityOptions options) {
        com.android.server.wm.Task nextFocusedTask;
        if (!isActivityTypeHome() && (nextFocusedTask = adjustFocusToNextFocusableTask("noMoreActivities")) != null) {
            return this.mRootWindowContainer.resumeFocusedTasksTopActivities(nextFocusedTask, prev, null);
        }
        android.app.ActivityOptions.abort(options);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf("noMoreActivities");
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 4037728373502324767L, 0, null, protoLogParam0);
        }
        return this.mRootWindowContainer.resumeHomeActivity(prev, "noMoreActivities", getDisplayArea());
    }

    void startActivityLocked(com.android.server.wm.ActivityRecord r, com.android.server.wm.Task topTask, boolean newTask, boolean isTaskSwitch, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord sourceRecord) {
        boolean doShow;
        com.android.server.wm.Task rTask = r.getTask();
        boolean allowMoveToFront = options == null || !options.getAvoidMoveToFront();
        boolean isOrhasTask = rTask == this || hasChild(rTask);
        if (!r.mLaunchTaskBehind && allowMoveToFront && (!isOrhasTask || newTask)) {
            positionChildAtTop(rTask);
        }
        if (!newTask && isOrhasTask && !r.shouldBeVisible()) {
            android.app.ActivityOptions.abort(options);
            return;
        }
        com.android.server.wm.Task activityTask = r.getTask();
        if (null == activityTask && this.mChildren.indexOf(null) != getChildCount() - 1) {
            this.mTaskSupervisor.mUserLeaving = false;
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_USER_LEAVING) {
                android.util.Slog.v(TAG_USER_LEAVING, "startActivity() behind front, mUserLeaving=false");
            }
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(r);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(activityTask);
            java.lang.String protoLogParam2 = java.lang.String.valueOf(new java.lang.RuntimeException("here").fillInStackTrace());
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -2261257617975724313L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2);
        }
        if (isActivityTypeHomeOrRecents() && getActivityBelow(r) == null) {
            android.app.ActivityOptions.abort(options);
            return;
        }
        if (!allowMoveToFront) {
            android.app.ActivityOptions.abort(options);
            return;
        }
        com.android.server.wm.DisplayContent dc = this.mDisplayContent;
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
            android.util.Slog.v(TAG_TRANSITION, "Prepare open transition: starting " + r);
        }
        if ((r.intent.getFlags() & 65536) != 0) {
            dc.prepareAppTransition(0);
            this.mTaskSupervisor.mNoAnimActivities.add(r);
            this.mTransitionController.setNoAnimation(r);
        } else {
            dc.prepareAppTransition(1);
            this.mTaskSupervisor.mNoAnimActivities.remove(r);
            if (!r.getWrapper().getExtImpl().shouldSkipAppTransitionWhenStarting()) {
                dc.prepareAppTransition(1);
                this.mTaskSupervisor.mNoAnimActivities.remove(r);
                getWrapper().getExtImpl().excuteAppTransitionForCompactWindowIfNeed(r, this);
            }
        }
        if (newTask && !r.mLaunchTaskBehind && !com.android.server.wm.ActivityTaskManagerService.isPip2ExperimentEnabled()) {
            com.android.server.wm.ActivityRecord pipCandidate = findEnterPipOnTaskSwitchCandidate(topTask);
            enableEnterPipOnTaskSwitch(pipCandidate, null, r, options);
        }
        boolean doShow2 = true;
        if (newTask) {
            if ((r.intent.getFlags() & 2097152) != 0) {
                resetTaskIfNeeded(r, r);
                doShow2 = topRunningNonDelayedActivityLocked(null) == r;
            }
        } else if (options != null && options.getAnimationType() == 5) {
            doShow2 = false;
        }
        if (options != null && options.getDisableStartingWindow()) {
            doShow = false;
        } else {
            doShow = doShow2;
        }
        getWrapper().getExtImpl().onPreShowStartingWindow(r, doShow);
        if (r.mLaunchTaskBehind) {
            r.setVisibility(true);
            ensureActivitiesVisible(null);
            if (!r.isVisibleRequested()) {
                r.notifyUnknownVisibilityLaunchedForKeyguardTransition();
            }
            this.mDisplayContent.executeAppTransition();
            return;
        }
        if (doShow) {
            if (options != null && options.getExtraNoAnimation()) {
                android.util.Slog.w(TAG, "not need startingWindow when set KEY_ACTIVITY_NO_ANIM");
                return;
            }
            com.android.server.wm.Task baseTask = r.getTask();
            com.android.server.wm.ActivityRecord prev = baseTask.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda25
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.Task.lambda$startActivityLocked$21((com.android.server.wm.ActivityRecord) obj);
                }
            });
            this.mWmService.mStartingSurfaceController.showStartingWindow(r, prev, newTask, isTaskSwitch, sourceRecord);
        }
    }

    static /* synthetic */ boolean lambda$startActivityLocked$21(com.android.server.wm.ActivityRecord a) {
        return a.mStartingData != null && a.showToCurrentUser();
    }

    static com.android.server.wm.ActivityRecord findEnterPipOnTaskSwitchCandidate(com.android.server.wm.Task topTask) {
        if (topTask == null) {
            return null;
        }
        final com.android.server.wm.ActivityRecord[] candidate = new com.android.server.wm.ActivityRecord[1];
        topTask.forAllLeafTaskFragments(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Task.lambda$findEnterPipOnTaskSwitchCandidate$22(candidate, (com.android.server.wm.TaskFragment) obj);
            }
        });
        return candidate[0];
    }

    static /* synthetic */ boolean lambda$findEnterPipOnTaskSwitchCandidate$22(com.android.server.wm.ActivityRecord[] candidate, com.android.server.wm.TaskFragment tf) {
        com.android.server.wm.ActivityRecord topActivity = tf.getTopNonFinishingActivity();
        if (topActivity == null || !topActivity.isState(com.android.server.wm.ActivityRecord.State.RESUMED, com.android.server.wm.ActivityRecord.State.PAUSING) || !topActivity.supportsPictureInPicture()) {
            return false;
        }
        candidate[0] = topActivity;
        return true;
    }

    static void enableEnterPipOnTaskSwitch(com.android.server.wm.ActivityRecord pipCandidate, com.android.server.wm.Task toFrontTask, com.android.server.wm.ActivityRecord toFrontActivity, android.app.ActivityOptions opts) {
        com.android.server.wm.Task targetRootTask;
        if (pipCandidate == null) {
            return;
        }
        if ((opts != null && opts.disallowEnterPictureInPictureWhileLaunching()) || pipCandidate.inPinnedWindowingMode()) {
            return;
        }
        if (toFrontTask != null) {
            targetRootTask = toFrontTask.getRootTask();
        } else {
            targetRootTask = toFrontActivity != null ? toFrontActivity.getRootTask() : null;
        }
        if (targetRootTask == null) {
            android.util.Slog.e(TAG, "No root task for enter pip, both to front task and activity are null?");
            return;
        }
        boolean z = false;
        boolean isTransient = (opts != null && opts.getTransientLaunch()) || targetRootTask.mTransitionController.isTransientHide(targetRootTask);
        if (!targetRootTask.isActivityTypeAssistant() && !isTransient) {
            z = true;
        }
        pipCandidate.supportsEnterPipOnTaskSwitch = z;
    }

    com.android.server.wm.ActivityRecord resetTaskIfNeeded(com.android.server.wm.ActivityRecord taskTop, com.android.server.wm.ActivityRecord newActivity) {
        com.android.server.wm.ActivityRecord newTop;
        boolean forceReset = (newActivity.info.flags & 4) != 0;
        com.android.server.wm.Task task = taskTop.getTask();
        this.mReuseTask = true;
        try {
            android.app.ActivityOptions topOptions = sResetTargetTaskHelper.process(task, forceReset);
            this.mReuseTask = false;
            if ((this.mChildren.contains(task) || this == task) && (newTop = task.getTopNonFinishingActivity()) != null) {
                taskTop = newTop;
            }
            if (topOptions != null) {
                taskTop.updateOptionsLocked(topOptions);
            }
            return taskTop;
        } catch (java.lang.Throwable th) {
            this.mReuseTask = false;
            throw th;
        }
    }

    final com.android.server.wm.Task finishTopCrashedActivityLocked(com.android.server.wm.WindowProcessController app, java.lang.String reason) {
        com.android.server.wm.ActivityRecord r = topRunningActivity();
        if (r == null || r.app != app) {
            return null;
        }
        if (r.isActivityTypeHome() && this.mAtmService.mHomeProcess == app) {
            android.util.Slog.w(TAG, "  Not force finishing home activity " + r.intent.getComponent().flattenToShortString());
            return null;
        }
        android.util.Slog.w(TAG, "  Force finishing activity " + r.intent.getComponent().flattenToShortString());
        com.android.server.wm.Task finishedTask = r.getTask();
        this.mDisplayContent.requestTransitionAndLegacyPrepare(2, 16);
        r.finishIfPossible(reason, false);
        com.android.server.wm.ActivityRecord activityBelow = getActivityBelow(r);
        if (activityBelow != null && activityBelow.isState(com.android.server.wm.ActivityRecord.State.STARTED, com.android.server.wm.ActivityRecord.State.RESUMED, com.android.server.wm.ActivityRecord.State.PAUSING, com.android.server.wm.ActivityRecord.State.PAUSED) && (!activityBelow.isActivityTypeHome() || this.mAtmService.mHomeProcess != activityBelow.app)) {
            android.util.Slog.w(TAG, "  Force finishing activity " + activityBelow.intent.getComponent().flattenToShortString());
            activityBelow.finishIfPossible(reason, false);
        }
        this.mTaskWrapper.getExtImpl().onTaskTopActivityCrashed(this);
        return finishedTask;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishIfVoiceTask(android.os.IBinder binder) {
        if (this.voiceSession != null && this.voiceSession.asBinder() == binder) {
            forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$finishIfVoiceTask$23((com.android.server.wm.ActivityRecord) obj);
                }
            });
            return;
        }
        com.android.internal.util.function.pooled.PooledPredicate f = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new java.util.function.BiPredicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda6
            @Override // java.util.function.BiPredicate
            public final boolean test(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.wm.Task.finishIfVoiceActivity((com.android.server.wm.ActivityRecord) obj, (android.os.IBinder) obj2);
            }
        }, com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.ActivityRecord.class), binder);
        forAllActivities((java.util.function.Predicate<com.android.server.wm.ActivityRecord>) f);
        f.recycle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishIfVoiceTask$23(com.android.server.wm.ActivityRecord r) {
        if (r.finishing) {
            return;
        }
        r.finishIfPossible("finish-voice", false);
        this.mAtmService.updateOomAdj();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean finishIfVoiceActivity(com.android.server.wm.ActivityRecord r, android.os.IBinder binder) {
        if (r.voiceSession == null || r.voiceSession.asBinder() != binder) {
            return false;
        }
        r.clearVoiceSessionLocked();
        try {
            r.app.getThread().scheduleLocalVoiceInteractionStarted(r.token, (com.android.internal.app.IVoiceInteractor) null);
        } catch (android.os.RemoteException e) {
        }
        r.mAtmService.finishRunningVoiceLocked();
        return true;
    }

    private boolean inFrontOfStandardRootTask() {
        com.android.server.wm.TaskDisplayArea taskDisplayArea = getDisplayArea();
        if (taskDisplayArea == null) {
            return false;
        }
        final boolean[] hasFound = new boolean[1];
        com.android.server.wm.Task rootTaskBehind = taskDisplayArea.getRootTask(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda22
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$inFrontOfStandardRootTask$24(hasFound, (com.android.server.wm.Task) obj);
            }
        });
        if (rootTaskBehind == null || !rootTaskBehind.isActivityTypeStandard()) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$inFrontOfStandardRootTask$24(boolean[] hasFound, com.android.server.wm.Task task) {
        if (hasFound[0]) {
            return true;
        }
        if (task == this) {
            hasFound[0] = true;
        }
        return false;
    }

    boolean shouldUpRecreateTaskLocked(com.android.server.wm.ActivityRecord srec, java.lang.String destAffinity) {
        java.lang.String affinity = com.android.server.wm.ActivityRecord.computeTaskAffinity(destAffinity, srec.getUid());
        if (srec == null || srec.getTask().affinity == null || !srec.getTask().affinity.equals(affinity)) {
            return true;
        }
        com.android.server.wm.Task task = srec.getTask();
        if (srec.isRootOfTask() && task.getBaseIntent() != null && task.getBaseIntent().isDocument()) {
            if (!inFrontOfStandardRootTask()) {
                return true;
            }
            com.android.server.wm.Task prevTask = getTaskBelow(task);
            if (prevTask == null) {
                android.util.Slog.w(TAG, "shouldUpRecreateTask: task not in history for " + srec);
                return false;
            }
            if (!task.affinity.equals(prevTask.affinity)) {
                return true;
            }
        }
        return false;
    }

    boolean navigateUpTo(com.android.server.wm.ActivityRecord srec, android.content.Intent destIntent, java.lang.String resolvedType, com.android.server.uri.NeededUriGrants destGrants, int resultCode, android.content.Intent resultData, com.android.server.uri.NeededUriGrants resultGrants) {
        boolean foundParentInTask;
        com.android.server.wm.ActivityRecord parent;
        com.android.server.wm.ActivityRecord next;
        com.android.server.wm.ActivityRecord candidate;
        if (!srec.attachedToProcess()) {
            return false;
        }
        com.android.server.wm.Task task = srec.getTask();
        if (!srec.isDescendantOf(this)) {
            return false;
        }
        com.android.server.wm.ActivityRecord parent2 = task.getActivityBelow(srec);
        final android.content.ComponentName dest = destIntent.getComponent();
        if (task.getBottomMostActivity() != srec && dest != null && (candidate = task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Task.lambda$navigateUpTo$25(dest, (com.android.server.wm.ActivityRecord) obj);
            }
        }, srec, false, true)) != null) {
            foundParentInTask = true;
            parent = candidate;
        } else {
            foundParentInTask = false;
            parent = parent2;
        }
        android.app.IActivityController controller = this.mAtmService.mController;
        if (controller != null && (next = topRunningActivity(srec.token, -1)) != null) {
            boolean resumeOK = true;
            try {
                resumeOK = controller.activityResuming(next.packageName);
            } catch (android.os.RemoteException e) {
                this.mAtmService.mController = null;
                com.android.server.Watchdog.getInstance().setActivityController(null);
            }
            if (!resumeOK) {
                return false;
            }
        }
        long origId = android.os.Binder.clearCallingIdentity();
        final int[] resultCodeHolder = {resultCode};
        final android.content.Intent[] resultDataHolder = {resultData};
        final com.android.server.uri.NeededUriGrants[] resultGrantsHolder = {resultGrants};
        final com.android.server.wm.ActivityRecord finalParent = parent;
        task.forAllActivities(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda24
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.Task.lambda$navigateUpTo$26(finalParent, resultCodeHolder, resultDataHolder, resultGrantsHolder, (com.android.server.wm.ActivityRecord) obj);
            }
        }, srec, true, true);
        int resultCode2 = resultCodeHolder[0];
        android.content.Intent resultData2 = resultDataHolder[0];
        if (parent != null && foundParentInTask) {
            int callingUid = srec.info.applicationInfo.uid;
            int res = this.mAtmService.getActivityStartController().obtainStarter(destIntent, "navigateUpTo").setResolvedType(resolvedType).setUserId(srec.mUserId).setCaller(srec.app.getThread()).setResultTo(parent.token).setIntentGrants(destGrants).setCallingPid(-1).setCallingUid(callingUid).setCallingPackage(srec.packageName).setCallingFeatureId(parent.launchedFromFeatureId).setRealCallingPid(-1).setRealCallingUid(callingUid).setComponentSpecified(true).execute();
            foundParentInTask = android.app.ActivityManager.isStartResultSuccessful(res);
            if (res == 0) {
                parent.finishIfPossible(resultCode2, resultData2, resultGrants, "navigate-top", true);
            }
        }
        android.os.Binder.restoreCallingIdentity(origId);
        return foundParentInTask;
    }

    static /* synthetic */ boolean lambda$navigateUpTo$25(android.content.ComponentName dest, com.android.server.wm.ActivityRecord ar) {
        return ar.info.packageName.equals(dest.getPackageName()) && ar.info.name.equals(dest.getClassName());
    }

    static /* synthetic */ boolean lambda$navigateUpTo$26(com.android.server.wm.ActivityRecord finalParent, int[] resultCodeHolder, android.content.Intent[] resultDataHolder, com.android.server.uri.NeededUriGrants[] resultGrantsHolder, com.android.server.wm.ActivityRecord ar) {
        if (ar == finalParent) {
            return true;
        }
        ar.finishIfPossible(resultCodeHolder[0], resultDataHolder[0], resultGrantsHolder[0], "navigate-up", true);
        resultCodeHolder[0] = 0;
        resultDataHolder[0] = null;
        return false;
    }

    void removeLaunchTickMessages() {
        forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda34
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.ActivityRecord) obj).removeLaunchTickRunnable();
            }
        });
    }

    private void updateTransitLocked(int transit, android.app.ActivityOptions options) {
        if (options != null) {
            com.android.server.wm.ActivityRecord r = topRunningActivity();
            if (r != null && (!r.isState(com.android.server.wm.ActivityRecord.State.RESUMED) || this.mTaskWrapper.getExtImpl().shouldUpdateTransitLocked(r, transit, options))) {
                r.updateOptionsLocked(options);
            } else {
                android.app.ActivityOptions.abort(options);
            }
        }
        this.mDisplayContent.prepareAppTransition(transit);
    }

    final void moveTaskToFront(com.android.server.wm.Task tr, boolean noAnimation, android.app.ActivityOptions options, com.android.server.am.AppTimeTracker timeTracker, java.lang.String reason) {
        moveTaskToFront(tr, noAnimation, options, timeTracker, false, reason);
    }

    final void moveTaskToFront(com.android.server.wm.Task tr, boolean noAnimation, android.app.ActivityOptions options, final com.android.server.am.AppTimeTracker timeTracker, boolean deferResume, java.lang.String reason) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            android.util.Slog.v(TAG_SWITCH, "moveTaskToFront: " + tr);
        }
        this.mTaskWrapper.getExtImpl().moveTaskToFront(options, tr);
        this.mTaskWrapper.getExtImpl().launchIntoCompactwindowingMode(this, true);
        com.android.server.wm.ActivityRecord pipCandidate = findEnterPipOnTaskSwitchCandidate(getDisplayArea().getTopRootTask());
        if (pipCandidate == null && getDisplayArea().getTopRootTask() != null && getDisplayArea().getTopRootTask().getWrapper().getExtImpl().isTaskEmbedded()) {
            pipCandidate = getWrapper().getExtImpl().findEnterPipOnTaskSwitchCandidateForPs(getDisplayArea().getTopRootTask());
        }
        if (tr != this && !tr.isDescendantOf(this)) {
            if (noAnimation) {
                android.app.ActivityOptions.abort(options);
                return;
            } else {
                updateTransitLocked(3, options);
                return;
            }
        }
        if (timeTracker != null) {
            tr.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda21
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wm.ActivityRecord) obj).appTimeTracker = timeTracker;
                }
            });
        }
        try {
            this.mDisplayContent.deferUpdateImeTarget();
            com.android.server.wm.ActivityRecord top = tr.getTopNonFinishingActivity();
            if (top != null && top.showToCurrentUser()) {
                if (!getWrapper().getExtImpl().isAvoidMoveTaskToFront(options)) {
                    top.moveFocusableActivityToTop(reason);
                }
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
                    android.util.Slog.v(TAG_TRANSITION, "Prepare to front transition: task=" + tr);
                }
                if (noAnimation || (tr != null && tr.mTaskWrapper.getExtImpl().isTaskEmbedded())) {
                    this.mDisplayContent.prepareAppTransition(0);
                    this.mTaskSupervisor.mNoAnimActivities.add(top);
                    this.mTransitionController.collect(top);
                    this.mTransitionController.setNoAnimation(top);
                    android.app.ActivityOptions.abort(options);
                } else if (this.mTaskWrapper.getExtImpl().shouldDoPuttTransition(this.mTaskId)) {
                    updateTransitLocked(100, options);
                } else {
                    updateTransitLocked(3, options);
                }
                if (pipCandidate == null || ((!this.mTaskWrapper.getExtImpl().isZoomMode(pipCandidate.getWindowingMode()) && !getWrapper().getExtImpl().isFlexibleTaskAndHasCaption(pipCandidate.getTask())) || !pipCandidate.isState(com.android.server.wm.ActivityRecord.State.RESUMED))) {
                    enableEnterPipOnTaskSwitch(pipCandidate, tr, null, options);
                }
                if (!deferResume) {
                    this.mRootWindowContainer.resumeFocusedTasksTopActivities();
                }
                return;
            }
            positionChildAtTop(tr);
            if (top != null) {
                this.mTaskSupervisor.mRecentTasks.add(top.getTask());
            }
            android.app.ActivityOptions.abort(options);
        } finally {
            this.mDisplayContent.continueUpdateImeTarget();
        }
    }

    private boolean canMoveTaskToBack(com.android.server.wm.Task task) {
        if (!this.mAtmService.getLockTaskController().canMoveTaskToBack(task)) {
            return false;
        }
        if (this.mAtmService.mController != null && isTopRootTaskInDisplayArea()) {
            com.android.server.wm.ActivityRecord next = topRunningActivity(null, task.mTaskId);
            if (next == null) {
                next = topRunningActivity(null, -1);
            }
            if (next != null) {
                boolean moveOK = true;
                try {
                    moveOK = this.mAtmService.mController.activityResuming(next.packageName);
                } catch (android.os.RemoteException e) {
                    this.mAtmService.mController = null;
                    com.android.server.Watchdog.getInstance().setActivityController(null);
                }
                return moveOK;
            }
            return true;
        }
        return true;
    }

    boolean moveTaskToBack(final com.android.server.wm.Task tr) {
        android.util.Slog.i(TAG, "moveTaskToBack: " + tr);
        if (!canMoveTaskToBack(tr)) {
            return false;
        }
        this.mTaskWrapper.getExtImpl().startFreezingDisplay(tr, this.mAtmService);
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
            android.util.Slog.v(TAG_TRANSITION, "Prepare to back transition: task=" + tr.mTaskId);
        }
        if (this.mTransitionController.isShellTransitionsEnabled() && !this.mAtmService.getWrapper().getExtImpl().withNoneTransition(null, tr, null, 4, "moveTaskToBack")) {
            com.android.server.wm.Transition collecting = this.mTransitionController.getCollectingTransition();
            if (collecting != null && collecting.mType == 1) {
                collecting.collect(tr);
                moveTaskToBackInner(tr, collecting);
                return true;
            }
            final com.android.server.wm.Transition transition = new com.android.server.wm.Transition(4, 0, this.mTransitionController, this.mWmService.mSyncEngine);
            this.mTransitionController.startCollectOrQueue(transition, new com.android.server.wm.TransitionController.OnStartCollect() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda32
                @Override // com.android.server.wm.TransitionController.OnStartCollect
                public final void onCollectStarted(boolean z) {
                    this.f$0.lambda$moveTaskToBack$28(tr, transition, z);
                }
            });
        } else {
            if (!inPinnedWindowingMode()) {
                this.mDisplayContent.prepareAppTransition(4);
            }
            if (!this.mTaskWrapper.getExtImpl().isSkipMoveTaskToBack()) {
                moveTaskToBackInner(tr, null);
            } else {
                android.util.Slog.d(TAG, " will skip move task to back once.");
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$moveTaskToBack$28(com.android.server.wm.Task tr, com.android.server.wm.Transition transition, boolean deferred) {
        if (!isAttached() || (deferred && !canMoveTaskToBack(tr))) {
            android.util.Slog.e(TAG, "Failed to move task to back after saying we could: " + tr.mTaskId);
            transition.abort();
        } else {
            this.mTransitionController.requestStartTransition(transition, tr, null, null);
            this.mTransitionController.collect(tr);
            moveTaskToBackInner(tr, transition);
        }
    }

    private void moveTaskToBackInner(com.android.server.wm.Task task, com.android.server.wm.Transition transition) {
        com.android.server.wm.Transition.ReadyCondition movedToBack = new com.android.server.wm.Transition.ReadyCondition("moved-to-back", task);
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            this.mAtmService.deferWindowLayout();
        }
        if (transition != null) {
            try {
                this.mTaskWrapper.getExtImpl().skipToEndFlexibleCustomAnimBeforeTransitionIfNeed(transition, "moveTaskToBack");
                transition.mReadyTracker.add(movedToBack);
            } finally {
                if (this.mTransitionController.isShellTransitionsEnabled()) {
                    this.mAtmService.continueWindowLayout();
                }
                if (transition != null) {
                    movedToBack.meet();
                }
            }
        }
        moveToBack("moveTaskToBackInner", task);
        this.mDisplayContent.getWrapper().getExtImpl().setAnimationThreadUx(true, false, 1);
        if (task.getWrapper().getExtImpl().isAlwaysOnTop()) {
            task.setAlwaysOnTopOnly(false);
        }
        this.mTaskWrapper.getExtImpl().moveTaskToBack(task, getDisplayContent().getFocusedRootTask());
        if (inPinnedWindowingMode()) {
            this.mTaskSupervisor.removeRootTask(this);
            if (transition != null) {
                return;
            } else {
                return;
            }
        }
        this.mRootWindowContainer.ensureVisibilityAndConfig(null, this.mDisplayContent, false);
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            this.mAtmService.continueWindowLayout();
        }
        if (transition != null) {
            movedToBack.meet();
        }
        com.android.server.wm.ActivityRecord topActivity = getDisplayArea().topRunningActivity();
        com.android.server.wm.Task topRootTask = topActivity != null ? topActivity.getRootTask() : null;
        if (topRootTask != null && topRootTask != this && topActivity.isState(com.android.server.wm.ActivityRecord.State.RESUMED) && !this.mTaskWrapper.mTaskExt.resumeFocusedTasksTopActivities(topRootTask, task)) {
            this.mDisplayContent.executeAppTransition();
            this.mDisplayContent.setFocusedApp(topActivity);
        } else {
            this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        }
    }

    boolean willActivityBeVisible(android.os.IBinder token) {
        com.android.server.wm.ActivityRecord r = com.android.server.wm.ActivityRecord.forTokenLocked(token);
        if (r == null || !r.shouldBeVisible()) {
            return false;
        }
        if (r.finishing) {
            android.util.Slog.e(TAG, "willActivityBeVisible: Returning false, would have returned true for r=" + r);
        }
        return !r.finishing;
    }

    void unhandledBackLocked() {
        com.android.server.wm.ActivityRecord topActivity = getTopMostActivity();
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            android.util.Slog.d(TAG_SWITCH, "Performing unhandledBack(): top activity: " + topActivity);
        }
        if (topActivity != null) {
            topActivity.finishIfPossible("unhandled-back", true);
        }
    }

    boolean dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, boolean dumpAll, boolean dumpClient, java.lang.String dumpPackage, boolean needSep) {
        return dump("  ", fd, pw, dumpAll, dumpClient, dumpPackage, needSep, null);
    }

    @Override // com.android.server.wm.TaskFragment
    void dumpInner(java.lang.String prefix, java.io.PrintWriter pw, boolean dumpAll, java.lang.String dumpPackage) {
        super.dumpInner(prefix, pw, dumpAll, dumpPackage);
        if (this.mCreatedByOrganizer) {
            pw.println(prefix + "  mCreatedByOrganizer=true");
        }
        if (this.mLastNonFullscreenBounds != null) {
            pw.print(prefix);
            pw.print("  mLastNonFullscreenBounds=");
            pw.println(this.mLastNonFullscreenBounds);
        }
        if (isLeafTask()) {
            pw.println(prefix + "  isSleeping=" + shouldSleepActivities());
            com.android.server.wm.ActivityTaskSupervisor.printThisActivity(pw, getTopPausingActivity(), dumpPackage, false, prefix + "  topPausingActivity=", null);
            com.android.server.wm.ActivityTaskSupervisor.printThisActivity(pw, getTopResumedActivity(), dumpPackage, false, prefix + "  topResumedActivity=", null);
            if (this.mMinWidth != -1 || this.mMinHeight != -1) {
                pw.print(prefix);
                pw.print("  mMinWidth=");
                pw.print(this.mMinWidth);
                pw.print(" mMinHeight=");
                pw.println(this.mMinHeight);
            }
        }
    }

    java.util.ArrayList<com.android.server.wm.ActivityRecord> getDumpActivitiesLocked(java.lang.String name, int userId) {
        final java.util.ArrayList<com.android.server.wm.ActivityRecord> activities = new java.util.ArrayList<>();
        if ("all".equals(name)) {
            java.util.Objects.requireNonNull(activities);
            forAllActivities(new com.android.server.wm.Task$$ExternalSyntheticLambda38(activities));
        } else if ("top".equals(name)) {
            com.android.server.wm.ActivityRecord topActivity = getTopMostActivity();
            if (topActivity != null) {
                activities.add(topActivity);
            }
        } else {
            final com.android.server.am.ActivityManagerService.ItemMatcher matcher = new com.android.server.am.ActivityManagerService.ItemMatcher();
            matcher.build(name);
            forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda39
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.Task.lambda$getDumpActivitiesLocked$29(matcher, activities, (com.android.server.wm.ActivityRecord) obj);
                }
            });
        }
        if (userId != -1) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (activities.get(i).mUserId != userId) {
                    activities.remove(i);
                }
            }
        }
        return activities;
    }

    static /* synthetic */ void lambda$getDumpActivitiesLocked$29(com.android.server.am.ActivityManagerService.ItemMatcher matcher, java.util.ArrayList activities, com.android.server.wm.ActivityRecord r) {
        if (matcher.match(r, r.intent.getComponent())) {
            activities.add(r);
        }
    }

    com.android.server.wm.Task reuseOrCreateTask(android.content.pm.ActivityInfo info, android.content.Intent intent, boolean toTop) {
        return reuseOrCreateTask(info, intent, null, null, toTop, null, null, null);
    }

    com.android.server.wm.Task reuseOrCreateTask(android.content.pm.ActivityInfo info, android.content.Intent intent, android.service.voice.IVoiceInteractionSession voiceSession, com.android.internal.app.IVoiceInteractor voiceInteractor, boolean toTop, com.android.server.wm.ActivityRecord activity, com.android.server.wm.ActivityRecord source, android.app.ActivityOptions options) {
        int taskId;
        com.android.server.wm.Task task;
        if (canReuseAsLeafTask()) {
            task = reuseAsLeafTask(voiceSession, voiceInteractor, intent, info, activity);
        } else {
            if (activity != null) {
                taskId = this.mTaskSupervisor.getNextTaskIdForUser(activity.mUserId);
            } else {
                taskId = this.mTaskSupervisor.getNextTaskIdForUser();
            }
            getActivityType();
            task = new com.android.server.wm.Task.Builder(this.mAtmService).setTaskId(taskId).setActivityInfo(info).setActivityOptions(options).setIntent(intent).setVoiceSession(voiceSession).setVoiceInteractor(voiceInteractor).setOnTop(toTop).setParent(this).build();
        }
        if (activity.getWrapper().getExtImpl().getLaunchedFromMultiSearch()) {
            task.getWrapper().getExtImpl().setLaunchedFromMultiSearch(true);
        }
        this.mTaskWrapper.getExtImpl().reuseOrCreateTask(info, intent, activity, source, options);
        int displayId = getDisplayId();
        if (displayId == -1) {
            displayId = 0;
        }
        boolean isLockscreenShown = this.mAtmService.mTaskSupervisor.getKeyguardController().isKeyguardOrAodShowing(displayId);
        if (!this.mTaskSupervisor.getLaunchParamsController().layoutTask(task, info.windowLayout, activity, source, options) && !getRequestedOverrideBounds().isEmpty() && task.isResizeable() && !isLockscreenShown) {
            task.setBounds(getRequestedOverrideBounds());
        }
        return task;
    }

    private boolean canReuseAsLeafTask() {
        if (this.mCreatedByOrganizer || !isLeafTask()) {
            return false;
        }
        int windowingMode = getWindowingMode();
        int activityType = getActivityType();
        return com.android.server.wm.DisplayContent.alwaysCreateRootTask(windowingMode, activityType);
    }

    void addChild(com.android.server.wm.WindowContainer child, boolean toTop, boolean showForAllUsers) {
        com.android.server.wm.Task task = child.asTask();
        if (task != null) {
            try {
                task.setForceShowForAllUsers(showForAllUsers);
            } catch (java.lang.Throwable th) {
                if (task != null) {
                    task.setForceShowForAllUsers(false);
                }
                throw th;
            }
        }
        addChild(child, toTop ? Integer.MAX_VALUE : 0, toTop);
        if (task != null) {
            task.setForceShowForAllUsers(false);
        }
    }

    public void setAlwaysOnTopOnly(boolean alwaysOnTop) {
        if (getWrapper().getExtImpl().isNeedSetAlwaysOnTopOnly(alwaysOnTop)) {
            android.util.Slog.d(TAG, "setAlwaysOnTopOnly alwaysOnTop " + alwaysOnTop + " this " + this);
            super.setAlwaysOnTop(alwaysOnTop);
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        if (super.isAlwaysOnTop() == alwaysOnTop) {
            return;
        }
        super.setAlwaysOnTop(alwaysOnTop);
        if (!isForceHidden() && !this.mTaskWrapper.getExtImpl().isPendingToBottomTask(this.mTaskId)) {
            getDisplayArea().positionChildAt(Integer.MAX_VALUE, this, false);
        }
    }

    void dismissPip() {
        if (!isActivityTypeStandardOrUndefined()) {
            throw new java.lang.IllegalArgumentException("You can't move tasks from non-standard root tasks.");
        }
        if (getWindowingMode() != 2) {
            throw new java.lang.IllegalArgumentException("Can't exit pinned mode if it's not pinned already.");
        }
        com.android.server.wm.Task task = getBottomMostTask();
        setWindowingMode(0);
        if (isAttached()) {
            getDisplayArea().positionChildAt(Integer.MAX_VALUE, this, false);
        }
        this.mTaskSupervisor.scheduleUpdatePictureInPictureModeIfNeeded(task, this);
    }

    private int setBounds(android.graphics.Rect existing, android.graphics.Rect bounds) {
        if (equivalentBounds(existing, bounds)) {
            return 0;
        }
        return setBoundsUnchecked((inMultiWindowMode() || this.mTaskWrapper.getExtImpl().isZoomMode(getWindowingMode()) || getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) ? bounds : null);
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public void getBounds(android.graphics.Rect bounds) {
        bounds.set(getBounds());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addChild(com.android.server.wm.WindowContainer child, int position, boolean moveParents) {
        addChild(child, (java.util.Comparator<com.android.server.wm.WindowContainer>) null);
        positionChildAt(position, child, moveParents);
    }

    void positionChildAtTop(com.android.server.wm.Task child) {
        if (child == null) {
            return;
        }
        if (child == this) {
            moveToFront("positionChildAtTop");
        } else {
            positionChildAt(Integer.MAX_VALUE, child, true);
        }
    }

    void positionChildAtBottom(com.android.server.wm.Task child) {
        com.android.server.wm.Task nextFocusableRootTask = getDisplayArea().getNextFocusableRootTask(child.getRootTask(), true);
        positionChildAtBottom(child, nextFocusableRootTask == null);
    }

    void positionChildAtBottom(com.android.server.wm.Task child, boolean includingParents) {
        if (child == null) {
            return;
        }
        positionChildAt(Integer.MIN_VALUE, child, includingParents);
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer
    void onChildPositionChanged(com.android.server.wm.WindowContainer child) {
        dispatchTaskInfoChangedIfNeeded(false);
        if (!this.mChildren.contains(child)) {
            return;
        }
        if (child.asTask() != null) {
            this.mRootWindowContainer.invalidateTaskLayers();
        }
        if (child.asActivityRecord() != null) {
            sendTaskFragmentParentInfoChangedIfNeeded();
        }
    }

    void reparent(com.android.server.wm.TaskDisplayArea newParent, boolean onTop) {
        if (newParent == null) {
            throw new java.lang.IllegalArgumentException("Task can't reparent to null " + this);
        }
        if (getParent() == newParent) {
            throw new java.lang.IllegalArgumentException("Task=" + this + " already child of " + newParent);
        }
        if (canBeLaunchedOnDisplay(newParent.getDisplayId())) {
            reparent(newParent, onTop ? Integer.MAX_VALUE : Integer.MIN_VALUE);
            if (isLeafTask()) {
                newParent.onLeafTaskMoved(this, onTop, !onTop);
                return;
            }
            return;
        }
        android.util.Slog.w(TAG, "Task=" + this + " can't reparent to " + newParent);
    }

    void setLastRecentsAnimationTransaction(android.window.PictureInPictureSurfaceTransaction transaction, android.view.SurfaceControl overlay) {
        this.mLastRecentsAnimationTransaction = new android.window.PictureInPictureSurfaceTransaction(transaction);
        this.mLastRecentsAnimationOverlay = overlay;
    }

    void clearLastRecentsAnimationTransaction(boolean forceRemoveOverlay) {
        if (forceRemoveOverlay && this.mLastRecentsAnimationOverlay != null) {
            getPendingTransaction().remove(this.mLastRecentsAnimationOverlay);
        }
        this.mLastRecentsAnimationTransaction = null;
        this.mLastRecentsAnimationOverlay = null;
        resetSurfaceControlTransforms();
    }

    void resetSurfaceControlTransforms() {
        getSyncTransaction().setMatrix(this.mSurfaceControl, android.graphics.Matrix.IDENTITY_MATRIX, new float[9]).setWindowCrop(this.mSurfaceControl, null).setShadowRadius(this.mSurfaceControl, 0.0f).setCornerRadius(this.mSurfaceControl, 0.0f);
    }

    void maybeApplyLastRecentsAnimationTransaction() {
        if (this.mLastRecentsAnimationTransaction != null) {
            android.view.SurfaceControl.Transaction tx = getPendingTransaction();
            if (this.mLastRecentsAnimationOverlay != null) {
                tx.reparent(this.mLastRecentsAnimationOverlay, this.mSurfaceControl);
            }
            android.window.PictureInPictureSurfaceTransaction.apply(this.mLastRecentsAnimationTransaction, this.mSurfaceControl, tx);
            tx.show(this.mSurfaceControl);
            this.mLastRecentsAnimationTransaction = null;
            this.mLastRecentsAnimationOverlay = null;
        }
    }

    private void updateSurfaceBounds() {
        updateSurfaceSize(getSyncTransaction());
        updateSurfacePositionNonOrganized();
        scheduleAnimation();
    }

    private android.graphics.Point getRelativePosition() {
        android.graphics.Point position = new android.graphics.Point();
        getRelativePosition(position);
        return position;
    }

    boolean shouldIgnoreInput() {
        if (this.mAtmService.mHasLeanbackFeature && inPinnedWindowingMode() && !isFocusedRootTaskOnDisplay()) {
            return true;
        }
        return false;
    }

    private void warnForNonLeafTask(java.lang.String func) {
        if (!isLeafTask()) {
            android.util.Slog.w(TAG, func + " on non-leaf task " + this);
        }
    }

    public android.view.DisplayInfo getDisplayInfo() {
        return this.mDisplayContent.getDisplayInfo();
    }

    com.android.server.wm.AnimatingActivityRegistry getAnimatingActivityRegistry() {
        return this.mAnimatingActivityRegistry;
    }

    @Override // com.android.server.wm.TaskFragment
    void executeAppTransition(android.app.ActivityOptions options) {
        this.mDisplayContent.executeAppTransition();
        android.app.ActivityOptions.abort(options);
    }

    @Override // com.android.server.wm.TaskFragment
    boolean shouldSleepActivities() {
        boolean isKeyguardGoingAway;
        com.android.server.wm.DisplayContent display = this.mDisplayContent;
        if (this.mDisplayContent != null) {
            isKeyguardGoingAway = this.mDisplayContent.isKeyguardGoingAway();
        } else {
            isKeyguardGoingAway = this.mRootWindowContainer.getDefaultDisplay().isKeyguardGoingAway();
        }
        if (isKeyguardGoingAway && isFocusedRootTaskOnDisplay() && display != null && display.isDefaultDisplay) {
            return false;
        }
        return display != null ? display.isSleeping() : this.mAtmService.isSleepingLocked();
    }

    private android.graphics.Rect getRawBounds() {
        return super.getBounds();
    }

    void dispatchTaskInfoChangedIfNeeded(boolean force) {
        if (isOrganized()) {
            this.mAtmService.mTaskOrganizerController.onTaskInfoChanged(this, force);
        }
    }

    void setReparentLeafTaskIfRelaunch(boolean reparentLeafTaskIfRelaunch) {
        if (isOrganized()) {
            this.mReparentLeafTaskIfRelaunch = reparentLeafTaskIfRelaunch;
        }
    }

    boolean isSameRequiredDisplayCategory(android.content.pm.ActivityInfo info) {
        return (this.mRequiredDisplayCategory != null && this.mRequiredDisplayCategory.equals(info.requiredDisplayCategory)) || (this.mRequiredDisplayCategory == null && info.requiredDisplayCategory == null);
    }

    @Override // com.android.server.wm.TaskFragment, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        if (logLevel == 2 && !isVisible()) {
            return;
        }
        long token = proto.start(fieldId);
        proto.write(1120986464258L, this.mTaskId);
        proto.write(1120986464272L, getRootTaskId());
        if (getTopResumedActivity() != null) {
            getTopResumedActivity().writeIdentifierToProto(proto, 1146756268044L);
        }
        if (this.realActivity != null) {
            proto.write(1138166333453L, this.realActivity.flattenToShortString());
        }
        if (this.origActivity != null) {
            proto.write(1138166333454L, this.origActivity.flattenToShortString());
        }
        proto.write(1120986464274L, this.mResizeMode);
        proto.write(1133871366148L, matchParentBounds());
        getRawBounds().dumpDebug(proto, 1146756268037L);
        if (this.mLastNonFullscreenBounds != null) {
            this.mLastNonFullscreenBounds.dumpDebug(proto, 1146756268054L);
        }
        if (this.mSurfaceControl != null) {
            proto.write(1120986464264L, this.mSurfaceControl.getWidth());
            proto.write(1120986464265L, this.mSurfaceControl.getHeight());
        }
        proto.write(1133871366172L, this.mCreatedByOrganizer);
        proto.write(1138166333469L, this.affinity);
        proto.write(1133871366174L, this.mChildPipActivity != null);
        super.dumpDebug(proto, 1146756268063L, logLevel);
        proto.end(token);
    }

    static class Builder {
        private android.content.pm.ActivityInfo mActivityInfo;
        private android.app.ActivityOptions mActivityOptions;
        private int mActivityType;
        private java.lang.String mAffinity;
        private android.content.Intent mAffinityIntent;
        private final com.android.server.wm.ActivityTaskManagerService mAtmService;
        private boolean mAutoRemoveRecents;
        private java.lang.String mCallingFeatureId;
        private java.lang.String mCallingPackage;
        private int mCallingUid;
        private boolean mCreatedByOrganizer;
        private boolean mDeferTaskAppear;
        private int mEffectiveUid;
        private boolean mHasBeenVisible;
        private android.content.Intent mIntent;
        private java.lang.String mLastDescription;
        private android.app.ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData mLastSnapshotData;
        private android.app.ActivityManager.TaskDescription mLastTaskDescription;
        private long mLastTimeMoved;
        private android.os.IBinder mLaunchCookie;
        private int mLaunchFlags;
        private boolean mNeverRelinquishIdentity;
        private boolean mOnTop;
        private android.content.ComponentName mOrigActivity;
        private com.android.server.wm.WindowContainer mParent;
        private android.content.ComponentName mRealActivity;
        private boolean mRealActivitySuspended;
        private boolean mRemoveWithTaskOrganizer;
        private int mResizeMode;
        private java.lang.String mRootAffinity;
        private boolean mRootWasReset;
        private com.android.server.wm.Task mSourceTask;
        private boolean mSupportsPictureInPicture;
        private int mTaskAffiliation;
        private int mTaskId;
        private int mUserId;
        private boolean mUserSetupComplete;
        private com.android.internal.app.IVoiceInteractor mVoiceInteractor;
        private android.service.voice.IVoiceInteractionSession mVoiceSession;
        private int mPrevAffiliateTaskId = -1;
        private int mNextAffiliateTaskId = -1;
        private int mMinWidth = -1;
        private int mMinHeight = -1;
        private int mWindowingMode = 0;

        Builder(com.android.server.wm.ActivityTaskManagerService atm) {
            this.mAtmService = atm;
        }

        com.android.server.wm.Task.Builder setParent(com.android.server.wm.WindowContainer parent) {
            this.mParent = parent;
            return this;
        }

        com.android.server.wm.Task.Builder setSourceTask(com.android.server.wm.Task sourceTask) {
            this.mSourceTask = sourceTask;
            return this;
        }

        com.android.server.wm.Task.Builder setLaunchFlags(int launchFlags) {
            this.mLaunchFlags = launchFlags;
            return this;
        }

        com.android.server.wm.Task.Builder setTaskId(int taskId) {
            this.mTaskId = taskId;
            return this;
        }

        com.android.server.wm.Task.Builder setIntent(android.content.Intent intent) {
            this.mIntent = intent;
            return this;
        }

        com.android.server.wm.Task.Builder setRealActivity(android.content.ComponentName realActivity) {
            this.mRealActivity = realActivity;
            return this;
        }

        com.android.server.wm.Task.Builder setEffectiveUid(int effectiveUid) {
            this.mEffectiveUid = effectiveUid;
            return this;
        }

        com.android.server.wm.Task.Builder setMinWidth(int minWidth) {
            this.mMinWidth = minWidth;
            return this;
        }

        com.android.server.wm.Task.Builder setMinHeight(int minHeight) {
            this.mMinHeight = minHeight;
            return this;
        }

        com.android.server.wm.Task.Builder setActivityInfo(android.content.pm.ActivityInfo info) {
            this.mActivityInfo = info;
            return this;
        }

        com.android.server.wm.Task.Builder setActivityOptions(android.app.ActivityOptions opts) {
            this.mActivityOptions = opts;
            return this;
        }

        com.android.server.wm.Task.Builder setVoiceSession(android.service.voice.IVoiceInteractionSession voiceSession) {
            this.mVoiceSession = voiceSession;
            return this;
        }

        com.android.server.wm.Task.Builder setActivityType(int activityType) {
            this.mActivityType = activityType;
            return this;
        }

        int getActivityType() {
            return this.mActivityType;
        }

        com.android.server.wm.Task.Builder setWindowingMode(int windowingMode) {
            this.mWindowingMode = windowingMode;
            return this;
        }

        int getWindowingMode() {
            return this.mWindowingMode;
        }

        com.android.server.wm.Task.Builder setCreatedByOrganizer(boolean createdByOrganizer) {
            this.mCreatedByOrganizer = createdByOrganizer;
            return this;
        }

        boolean getCreatedByOrganizer() {
            return this.mCreatedByOrganizer;
        }

        com.android.server.wm.Task.Builder setDeferTaskAppear(boolean defer) {
            this.mDeferTaskAppear = defer;
            return this;
        }

        com.android.server.wm.Task.Builder setLaunchCookie(android.os.IBinder launchCookie) {
            this.mLaunchCookie = launchCookie;
            return this;
        }

        com.android.server.wm.Task.Builder setOnTop(boolean onTop) {
            this.mOnTop = onTop;
            return this;
        }

        com.android.server.wm.Task.Builder setHasBeenVisible(boolean hasBeenVisible) {
            this.mHasBeenVisible = hasBeenVisible;
            return this;
        }

        com.android.server.wm.Task.Builder setRemoveWithTaskOrganizer(boolean removeWithTaskOrganizer) {
            this.mRemoveWithTaskOrganizer = removeWithTaskOrganizer;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setUserId(int userId) {
            this.mUserId = userId;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setLastTimeMoved(long lastTimeMoved) {
            this.mLastTimeMoved = lastTimeMoved;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setNeverRelinquishIdentity(boolean neverRelinquishIdentity) {
            this.mNeverRelinquishIdentity = neverRelinquishIdentity;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setCallingUid(int callingUid) {
            this.mCallingUid = callingUid;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setCallingPackage(java.lang.String callingPackage) {
            this.mCallingPackage = callingPackage;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setResizeMode(int resizeMode) {
            this.mResizeMode = resizeMode;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setSupportsPictureInPicture(boolean supportsPictureInPicture) {
            this.mSupportsPictureInPicture = supportsPictureInPicture;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setUserSetupComplete(boolean userSetupComplete) {
            this.mUserSetupComplete = userSetupComplete;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setTaskAffiliation(int taskAffiliation) {
            this.mTaskAffiliation = taskAffiliation;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setPrevAffiliateTaskId(int prevAffiliateTaskId) {
            this.mPrevAffiliateTaskId = prevAffiliateTaskId;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setNextAffiliateTaskId(int nextAffiliateTaskId) {
            this.mNextAffiliateTaskId = nextAffiliateTaskId;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setCallingFeatureId(java.lang.String callingFeatureId) {
            this.mCallingFeatureId = callingFeatureId;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setRealActivitySuspended(boolean realActivitySuspended) {
            this.mRealActivitySuspended = realActivitySuspended;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setLastDescription(java.lang.String lastDescription) {
            this.mLastDescription = lastDescription;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setLastTaskDescription(android.app.ActivityManager.TaskDescription lastTaskDescription) {
            this.mLastTaskDescription = lastTaskDescription;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setLastSnapshotData(android.app.ActivityManager.RecentTaskInfo.PersistedTaskSnapshotData lastSnapshotData) {
            this.mLastSnapshotData = lastSnapshotData;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setOrigActivity(android.content.ComponentName origActivity) {
            this.mOrigActivity = origActivity;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setRootWasReset(boolean rootWasReset) {
            this.mRootWasReset = rootWasReset;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setAutoRemoveRecents(boolean autoRemoveRecents) {
            this.mAutoRemoveRecents = autoRemoveRecents;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setAffinityIntent(android.content.Intent affinityIntent) {
            this.mAffinityIntent = affinityIntent;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setAffinity(java.lang.String affinity) {
            this.mAffinity = affinity;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setRootAffinity(java.lang.String rootAffinity) {
            this.mRootAffinity = rootAffinity;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.Task.Builder setVoiceInteractor(com.android.internal.app.IVoiceInteractor voiceInteractor) {
            this.mVoiceInteractor = voiceInteractor;
            return this;
        }

        private void validateRootTask(com.android.server.wm.TaskDisplayArea tda) {
            com.android.server.wm.Task rootTask;
            if (this.mActivityType == 0 && !this.mCreatedByOrganizer) {
                this.mActivityType = 1;
            }
            if (!com.android.server.wm.DisplayContent.alwaysCreateRootTask(tda.getWindowingMode(), this.mActivityType) && this.mActivityType != 0 && (rootTask = tda.getRootTask(0, this.mActivityType)) != null) {
                throw new java.lang.IllegalArgumentException("Root task=" + rootTask + " of activityType=" + this.mActivityType + " already on display=" + tda + ". Can't have multiple.");
            }
            if (!com.android.server.wm.TaskDisplayArea.isWindowingModeSupported(this.mWindowingMode, this.mAtmService.mSupportsMultiWindow, this.mAtmService.mSupportsFreeformWindowManagement, this.mAtmService.mSupportsPictureInPicture)) {
                throw new java.lang.IllegalArgumentException("Can't create root task for unsupported windowingMode=" + this.mWindowingMode);
            }
            if (this.mWindowingMode == 2 && this.mActivityType != 1) {
                throw new java.lang.IllegalArgumentException("Root task with pinned windowing mode cannot with non-standard activity type.");
            }
            if (this.mWindowingMode == 2 && tda.getRootPinnedTask() != null) {
                tda.getRootPinnedTask().dismissPip();
            }
            if (this.mIntent != null) {
                this.mLaunchFlags |= this.mIntent.getFlags();
            }
            com.android.server.wm.Task launchRootTask = this.mCreatedByOrganizer ? null : tda.getLaunchRootTask(this.mWindowingMode, this.mActivityType, this.mActivityOptions, this.mSourceTask, this.mLaunchFlags);
            if (launchRootTask != null) {
                this.mWindowingMode = 0;
                this.mParent = launchRootTask;
            }
            this.mTaskId = tda.getNextRootTaskId();
        }

        com.android.server.wm.Task build() {
            if (this.mParent != null && (this.mParent instanceof com.android.server.wm.TaskDisplayArea)) {
                validateRootTask((com.android.server.wm.TaskDisplayArea) this.mParent);
            }
            if (this.mActivityInfo == null) {
                this.mActivityInfo = new android.content.pm.ActivityInfo();
                this.mActivityInfo.applicationInfo = new android.content.pm.ApplicationInfo();
            }
            this.mUserId = android.os.UserHandle.getUserId(this.mActivityInfo.applicationInfo.uid);
            this.mTaskAffiliation = this.mTaskId;
            this.mLastTimeMoved = java.lang.System.currentTimeMillis();
            this.mNeverRelinquishIdentity = true;
            this.mCallingUid = this.mActivityInfo.applicationInfo.uid;
            this.mCallingPackage = this.mActivityInfo.packageName;
            this.mResizeMode = this.mActivityInfo.resizeMode;
            this.mSupportsPictureInPicture = this.mActivityInfo.supportsPictureInPicture();
            if (!this.mRemoveWithTaskOrganizer && this.mActivityOptions != null) {
                this.mRemoveWithTaskOrganizer = this.mActivityOptions.getRemoveWithTaskOranizer();
            }
            com.android.server.wm.Task task = buildInner();
            task.mHasBeenVisible = this.mHasBeenVisible;
            if (this.mActivityType != 0) {
                task.setActivityType(this.mActivityType);
            }
            if (this.mParent != null) {
                if (this.mParent instanceof com.android.server.wm.Task) {
                    com.android.server.wm.Task parentTask = (com.android.server.wm.Task) this.mParent;
                    parentTask.addChild(task, this.mOnTop ? Integer.MAX_VALUE : Integer.MIN_VALUE, (this.mActivityInfo.flags & 1024) != 0);
                } else {
                    this.mParent.addChild(task, this.mOnTop ? Integer.MAX_VALUE : Integer.MIN_VALUE);
                }
            }
            if (this.mWindowingMode != 0) {
                task.setWindowingMode(this.mWindowingMode, true);
            }
            return task;
        }

        com.android.server.wm.Task buildInner() {
            return new com.android.server.wm.Task(this.mAtmService, this.mTaskId, this.mIntent, this.mAffinityIntent, this.mAffinity, this.mRootAffinity, this.mRealActivity, this.mOrigActivity, this.mRootWasReset, this.mAutoRemoveRecents, this.mUserId, this.mEffectiveUid, this.mLastDescription, this.mLastTimeMoved, this.mNeverRelinquishIdentity, this.mLastTaskDescription, this.mLastSnapshotData, this.mTaskAffiliation, this.mPrevAffiliateTaskId, this.mNextAffiliateTaskId, this.mCallingUid, this.mCallingPackage, this.mCallingFeatureId, this.mResizeMode, this.mSupportsPictureInPicture, this.mRealActivitySuspended, this.mUserSetupComplete, this.mMinWidth, this.mMinHeight, this.mActivityInfo, this.mVoiceSession, this.mVoiceInteractor, this.mCreatedByOrganizer, this.mLaunchCookie, this.mDeferTaskAppear, this.mRemoveWithTaskOrganizer);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void updateOverlayInsetsState(com.android.server.wm.WindowState originalChange) {
        super.updateOverlayInsetsState(originalChange);
        if (originalChange == getTopVisibleAppMainWindow() && this.mOverlayHost != null) {
            android.view.InsetsState s = originalChange.getInsetsState(true);
            getBounds(this.mTmpRect);
            this.mOverlayHost.dispatchInsetsChanged(s, this.mTmpRect);
        }
    }

    com.android.server.wm.ActivityRecord getBottomMostActivityInSamePackage() {
        if (this.realActivity == null) {
            return null;
        }
        return getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.Task$$ExternalSyntheticLambda29
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getBottomMostActivityInSamePackage$30((com.android.server.wm.ActivityRecord) obj);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getBottomMostActivityInSamePackage$30(com.android.server.wm.ActivityRecord ar) {
        return ar.packageName.equals(this.realActivity.getPackageName());
    }

    void moveOrCreateDecorSurfaceFor(com.android.server.wm.TaskFragment taskFragment, boolean visible) {
        if (this.mDecorSurfaceContainer != null) {
            this.mDecorSurfaceContainer.mOwnerTaskFragment = taskFragment;
            return;
        }
        this.mDecorSurfaceContainer = new com.android.server.wm.Task.DecorSurfaceContainer(taskFragment, visible);
        assignChildLayers();
        sendTaskFragmentParentInfoChangedIfNeeded();
    }

    void removeDecorSurface() {
        if (this.mDecorSurfaceContainer == null) {
            return;
        }
        this.mDecorSurfaceContainer.release();
        this.mDecorSurfaceContainer = null;
        sendTaskFragmentParentInfoChangedIfNeeded();
    }

    android.view.SurfaceControl getDecorSurface() {
        if (this.mDecorSurfaceContainer != null) {
            return this.mDecorSurfaceContainer.mDecorSurface;
        }
        return null;
    }

    void setDecorSurfaceVisible(android.view.SurfaceControl.Transaction t) {
        if (this.mDecorSurfaceContainer == null) {
            return;
        }
        t.show(this.mDecorSurfaceContainer.mDecorSurface);
    }

    class DecorSurfaceContainer {
        final android.view.SurfaceControl mContainerSurface;
        final android.view.SurfaceControl mDecorSurface;
        private boolean mIsBoosted;
        private boolean mIsBoostedRequested;
        com.android.server.wm.TaskFragment mOwnerTaskFragment;
        private final java.util.List<android.view.SurfaceControl.Transaction> mPendingClientTransactions;

        private DecorSurfaceContainer(com.android.server.wm.TaskFragment initialOwner, boolean visible) {
            this.mPendingClientTransactions = new java.util.ArrayList();
            this.mOwnerTaskFragment = initialOwner;
            this.mContainerSurface = com.android.server.wm.Task.this.makeSurface().setContainerLayer().setParent(com.android.server.wm.Task.this.mSurfaceControl).setName(com.android.server.wm.Task.this.mSurfaceControl + " - decor surface container").setContainerLayer().setHidden(false).setCallsite("Task.DecorSurfaceContainer").build();
            this.mDecorSurface = com.android.server.wm.Task.this.makeSurface().setParent(this.mContainerSurface).setName(com.android.server.wm.Task.this.mSurfaceControl + " - decor surface").setHidden(!visible).setCallsite("Task.DecorSurfaceContainer").build();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void requestBoosted(boolean isBoosted, android.view.SurfaceControl.Transaction clientTransaction) {
            this.mIsBoostedRequested = isBoosted;
            if (clientTransaction != null) {
                this.mPendingClientTransactions.add(clientTransaction);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void commitBoostedState() {
            this.mIsBoosted = this.mIsBoostedRequested;
            applyPendingClientTransactions(com.android.server.wm.Task.this.getSyncTransaction());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void assignLayer(android.view.SurfaceControl.Transaction t, int layer) {
            t.setLayer(this.mContainerSurface, layer);
            t.setVisibility(this.mContainerSurface, this.mOwnerTaskFragment.isVisible() || this.mIsBoosted);
        }

        private void applyPendingClientTransactions(android.view.SurfaceControl.Transaction t) {
            for (int i = 0; i < this.mPendingClientTransactions.size(); i++) {
                t.merge(this.mPendingClientTransactions.get(i));
            }
            this.mPendingClientTransactions.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void release() {
            com.android.server.wm.Task.this.getSyncTransaction().remove(this.mDecorSurface).remove(this.mContainerSurface);
        }
    }

    @Override // com.android.server.wm.TaskFragment
    boolean forceCreateRemoteAnimationTarget(com.android.server.wm.ActivityRecord r) {
        boolean needCreateRemoteAnimation = this.mTaskWrapper.mTaskExt.forceCreateRemoteAnimationTarget(r);
        if (needCreateRemoteAnimation) {
            this.mTaskWrapper.mTaskExt.addStartingBackColorLayerIfNeed(r);
        }
        return needCreateRemoteAnimation;
    }

    @Override // com.android.server.wm.TaskFragment
    void setAppTransitionReadyInAdvance(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.ActivityRecord activity) {
        this.mTaskWrapper.mTaskExt.setAppTransitionReadyInAdvance(this.mDisplayContent, activity);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    boolean allResumedActivitiesComplete() {
        if (!isLeafTask()) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                com.android.server.wm.Task task = ((com.android.server.wm.WindowContainer) this.mChildren.get(i)).asTask();
                if (task != null && !task.allResumedActivitiesComplete()) {
                    return false;
                }
            }
        } else {
            com.android.server.wm.ActivityRecord taskResumedActivity = getResumedActivity();
            com.android.server.wm.ActivityRecord topResumedActivity = null;
            for (int i2 = this.mChildren.size() - 1; i2 >= 0; i2--) {
                com.android.server.wm.WindowContainer child = (com.android.server.wm.WindowContainer) this.mChildren.get(i2);
                if (child.asTaskFragment() != null) {
                    topResumedActivity = child.asTaskFragment().getTopResumedActivity();
                } else if (taskResumedActivity != null && child.asActivityRecord() == taskResumedActivity) {
                    topResumedActivity = taskResumedActivity;
                }
                if (topResumedActivity != null && !topResumedActivity.isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void callSuperOnConfigurationChanged(android.content.res.Configuration config) {
        super.onConfigurationChanged(config);
    }

    public com.android.server.wm.ITaskWrapper getWrapper() {
        return this.mTaskWrapper;
    }

    private class TaskWrapper implements com.android.server.wm.ITaskWrapper {
        private com.android.server.wm.ITaskExt mTaskExt;

        private TaskWrapper() {
            this.mTaskExt = (com.android.server.wm.ITaskExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITaskExt.class).base(com.android.server.wm.Task.this).create();
        }

        @Override // com.android.server.wm.ITaskWrapper
        public com.android.server.wm.ITaskExt getExtImpl() {
            return this.mTaskExt;
        }

        @Override // com.android.server.wm.ITaskWrapper
        public com.android.server.wm.WindowProcessController getWindowProcessController() {
            return com.android.server.wm.Task.this.mRootProcess;
        }

        @Override // com.android.server.wm.ITaskWrapper
        public void removeHiddenFlags(int flags) {
            com.android.server.wm.Task.this.mForceHiddenFlags &= ~flags;
        }

        @Override // com.android.server.wm.ITaskWrapper
        public void callSuperOnConfigurationChanged(android.content.res.Configuration config) {
            com.android.server.wm.Task.this.callSuperOnConfigurationChanged(config);
        }

        @Override // com.android.server.wm.ITaskWrapper
        public com.android.server.wm.ActivityRecord topRunningActivityLocked() {
            return com.android.server.wm.Task.this.topRunningActivityLocked();
        }
    }
}
