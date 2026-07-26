package com.android.server.wm;

import com.android.server.wm.ActivityTaskManagerService.SleepTokenAcquirer;

/* JADX INFO: loaded from: classes3.dex */
public class RootWindowContainer extends com.android.server.wm.WindowContainer<com.android.server.wm.DisplayContent> implements android.hardware.display.DisplayManager.DisplayListener {
    private static final java.lang.String DISPLAY_OFF_SLEEP_TOKEN_TAG = "Display-off";
    static final int MATCH_ATTACHED_TASK_ONLY = 0;
    static final int MATCH_ATTACHED_TASK_OR_RECENT_TASKS = 1;
    static final int MATCH_ATTACHED_TASK_OR_RECENT_TASKS_AND_RESTORE = 2;
    private static final int MSG_SEND_SLEEP_TRANSITION = 3;
    private static final int PINNED_TASK_ABORT_TIMEOUT = 1000;
    private static final int SET_SCREEN_BRIGHTNESS_OVERRIDE = 1;
    private static final int SET_USER_ACTIVITY_TIMEOUT = 2;
    private static final long SLEEP_TRANSITION_WAIT_MILLIS = 1000;
    private final java.util.function.Consumer<com.android.server.wm.WindowState> mCloseSystemDialogsConsumer;
    private java.lang.String mCloseSystemDialogsReason;
    int mCurrentUser;
    private com.android.server.wm.DisplayContent mDefaultDisplay;
    private java.lang.String mDestroyAllActivitiesReason;
    private final java.lang.Runnable mDestroyAllActivitiesRunnable;
    private final com.android.server.wm.DeviceStateController mDeviceStateController;
    private final android.util.SparseArray<android.util.IntArray> mDisplayAccessUIDs;
    android.hardware.display.DisplayManager mDisplayManager;
    private android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    final com.android.server.wm.ActivityTaskManagerService.SleepTokenAcquirer mDisplayOffTokenAcquirer;
    private final com.android.server.wm.DisplayRotationCoordinator mDisplayRotationCoordinator;
    private final android.util.SparseArray<android.view.SurfaceControl.Transaction> mDisplayTransactions;
    private final com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> mFindOrientationChangingFunction;
    com.android.server.wm.RootWindowContainer.FinishDisabledPackageActivitiesHelper mFinishDisabledPackageActivitiesHelper;
    private final android.os.Handler mHandler;
    private java.lang.Object mLastWindowFreezeSource;
    private java.lang.Runnable mMaybeAbortPipEnterRunnable;
    private boolean mObscureApplicationContentOnSecondaryDisplays;
    boolean mOrientationChangeComplete;
    private final com.android.server.wm.RootWindowContainer.RankTaskLayersRunnable mRankTaskLayersRunnable;
    public com.android.server.wm.IRootWindowContainerExt mRootWindowContainerExt;
    private float mScreenBrightnessOverride;
    com.android.server.wm.ActivityTaskManagerService mService;
    final android.util.SparseArray<com.android.server.wm.RootWindowContainer.SleepToken> mSleepTokens;
    public com.android.server.wm.IRootWindowContainerSocExt mSocExt;
    private boolean mSustainedPerformanceModeCurrent;
    private boolean mSustainedPerformanceModeEnabled;
    private boolean mTaskLayersChanged;
    com.android.server.wm.ActivityTaskSupervisor mTaskSupervisor;
    private final com.android.server.wm.RootWindowContainer.FindTaskResult mTmpFindTaskResult;
    boolean mTmpOrientationChangeComplete;
    private int mTmpTaskLayerRank;
    final android.util.ArrayMap<java.lang.Integer, com.android.server.wm.ActivityRecord> mTopFocusedAppByProcess;
    private int mTopFocusedDisplayId;
    private boolean mUpdateRotation;
    private long mUserActivityTimeout;
    android.util.SparseIntArray mUserRootTaskInFront;
    boolean mWallpaperActionPending;
    com.android.server.wm.WindowManagerService mWindowManager;
    private static final java.lang.String TAG = "WindowManager";
    static final java.lang.String TAG_TASKS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_TASKS;
    static final java.lang.String TAG_STATES = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_STATES;
    private static final java.lang.String TAG_RECENTS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_RECENTS;
    public static boolean mPerfSendTapHint = false;
    public static boolean mIsPerfBoostAcquired = false;
    public static int mPerfHandle = -1;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AnyTaskForIdMatchTaskMode {
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ void commitPendingTransaction() {
        super.commitPendingTransaction();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ int compareTo(com.android.server.wm.WindowContainer windowContainer) {
        return super.compareTo(windowContainer);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl getAnimationLeash() {
        return super.getAnimationLeash();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl getAnimationLeashParent() {
        return super.getAnimationLeashParent();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl getFreezeSnapshotTarget() {
        return super.getFreezeSnapshotTarget();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ android.util.SparseArray getInsetsSourceProviders() {
        return super.getInsetsSourceProviders();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl getParentSurfaceControl() {
        return super.getParentSurfaceControl();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl.Transaction getPendingTransaction() {
        return super.getPendingTransaction();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl getSurfaceControl() {
        return super.getSurfaceControl();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ int getSurfaceHeight() {
        return super.getSurfaceHeight();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ int getSurfaceWidth() {
        return super.getSurfaceWidth();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl.Transaction getSyncTransaction() {
        return super.getSyncTransaction();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ com.android.server.wm.IWindowContainerWrapper getWCWrapper() {
        return super.getWCWrapper();
    }

    @Override // com.android.server.wm.WindowContainer
    public /* bridge */ /* synthetic */ boolean hasInsetsSourceProvider() {
        return super.hasInsetsSourceProvider();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ android.view.SurfaceControl.Builder makeAnimationLeash() {
        return super.makeAnimationLeash();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ void onAnimationLeashCreated(android.view.SurfaceControl.Transaction transaction, android.view.SurfaceControl surfaceControl) {
        super.onAnimationLeashCreated(transaction, surfaceControl);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public /* bridge */ /* synthetic */ void onAnimationLeashLost(android.view.SurfaceControl.Transaction transaction) {
        super.onAnimationLeashLost(transaction);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public /* bridge */ /* synthetic */ void onConfigurationChanged(android.content.res.Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public /* bridge */ /* synthetic */ void onRequestedOverrideConfigurationChanged(android.content.res.Configuration configuration) {
        super.onRequestedOverrideConfigurationChanged(configuration);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public /* bridge */ /* synthetic */ void onUnfrozen() {
        super.onUnfrozen();
    }

    /* JADX INFO: renamed from: com.android.server.wm.RootWindowContainer$1, reason: invalid class name */
    class AnonymousClass1 implements java.lang.Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.RootWindowContainer.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                        com.android.server.wm.RootWindowContainer.this.mTaskSupervisor.beginDeferResume();
                        com.android.server.wm.RootWindowContainer.this.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$1$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                this.f$0.lambda$run$0((com.android.server.wm.ActivityRecord) obj);
                            }
                        });
                    } finally {
                        com.android.server.wm.RootWindowContainer.this.mTaskSupervisor.endDeferResume();
                        com.android.server.wm.RootWindowContainer.this.resumeFocusedTasksTopActivities();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(com.android.server.wm.ActivityRecord r) {
            if (r.finishing || !r.isDestroyable()) {
                return;
            }
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
                android.util.Slog.v(com.android.server.wm.ActivityTaskManagerService.TAG_SWITCH, "Destroying " + r + " in state " + r.getState() + " resumed=" + r.getTask().getTopResumedActivity() + " pausing=" + r.getTask().getTopPausingActivity() + " for reason " + com.android.server.wm.RootWindowContainer.this.mDestroyAllActivitiesReason);
            }
            r.destroyImmediately(com.android.server.wm.RootWindowContainer.this.mDestroyAllActivitiesReason);
        }
    }

    static class FindTaskResult implements java.util.function.Predicate<com.android.server.wm.Task> {
        private android.content.ComponentName cls;
        private android.net.Uri documentData;
        private boolean isDocument;
        private int mActivityType;
        com.android.server.wm.ActivityRecord mCandidateRecord;
        com.android.server.wm.ActivityRecord mIdealRecord;
        private android.content.pm.ActivityInfo mInfo;
        private android.content.Intent mIntent;
        private java.lang.String mTaskAffinity;
        private int userId;
        private boolean mIncludeLaunchedFromBubble = true;
        private com.android.server.wm.IRootWindowContainerExt.IFindTaskResultExt mFindTaskResultExt = (com.android.server.wm.IRootWindowContainerExt.IFindTaskResultExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IRootWindowContainerExt.IFindTaskResultExt.class).create();

        FindTaskResult() {
        }

        void init(int activityType, java.lang.String taskAffinity, android.content.Intent intent, android.content.pm.ActivityInfo info, boolean includeLaunchedFromBubble) {
            this.mActivityType = activityType;
            this.mTaskAffinity = taskAffinity;
            this.mIntent = intent;
            this.mInfo = info;
            this.mIdealRecord = null;
            this.mCandidateRecord = null;
            this.mIncludeLaunchedFromBubble = includeLaunchedFromBubble;
        }

        void process(com.android.server.wm.WindowContainer parent) {
            this.cls = this.mIntent.getComponent();
            if (this.mInfo.targetActivity != null) {
                this.cls = new android.content.ComponentName(this.mInfo.packageName, this.mInfo.targetActivity);
            }
            this.userId = android.os.UserHandle.getUserId(this.mInfo.applicationInfo.uid);
            this.isDocument = (this.mIntent != null) & this.mIntent.isDocument();
            this.documentData = this.isDocument ? this.mIntent.getData() : null;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mInfo);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(parent);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -8961882615747561040L, 0, null, protoLogParam0, protoLogParam1);
            }
            parent.forAllLeafTasks(this);
        }

        @Override // java.util.function.Predicate
        public boolean test(com.android.server.wm.Task task) {
            boolean taskIsDocument;
            android.net.Uri taskDocumentData;
            if (!task.isLeafTask()) {
                return false;
            }
            if (!com.android.server.wm.ConfigurationContainer.isCompatibleActivityType(this.mActivityType, task.getActivityType())) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(task);
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 8899721161806265460L, 0, null, protoLogParam0);
                }
                return false;
            }
            if (task.voiceSession == null) {
                if (task.mUserId != this.userId) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                        java.lang.String protoLogParam02 = java.lang.String.valueOf(task);
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 4468520936943270392L, 0, null, protoLogParam02);
                    }
                    return false;
                }
                if (task.getWrapper().getExtImpl().isCreateForSingleSplit()) {
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
                        android.util.Slog.d(com.android.server.wm.RootWindowContainer.TAG, "Skipping " + task + ", create for single split");
                    }
                    return false;
                }
                com.android.server.wm.ActivityRecord r = task.getTopNonFinishingActivity(false, this.mIncludeLaunchedFromBubble);
                if (r != null && !r.finishing && r.mUserId == this.userId && r.launchMode != 3) {
                    if (!com.android.server.wm.ConfigurationContainer.isCompatibleActivityType(r.getActivityType(), this.mActivityType)) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                            java.lang.String protoLogParam03 = java.lang.String.valueOf(task);
                            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 9031436623838917667L, 0, null, protoLogParam03);
                        }
                        return false;
                    }
                    android.content.Intent taskIntent = task.intent;
                    android.content.Intent affinityIntent = task.affinityIntent;
                    if (taskIntent != null && taskIntent.isDocument()) {
                        taskIsDocument = true;
                        taskDocumentData = taskIntent.getData();
                    } else if (affinityIntent != null && affinityIntent.isDocument()) {
                        taskIsDocument = true;
                        taskDocumentData = affinityIntent.getData();
                    } else {
                        taskIsDocument = false;
                        taskDocumentData = null;
                    }
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                        java.lang.String protoLogParam04 = java.lang.String.valueOf(task.realActivity != null ? task.realActivity.flattenToShortString() : "");
                        java.lang.String protoLogParam1 = java.lang.String.valueOf(task.rootAffinity);
                        java.lang.String protoLogParam2 = java.lang.String.valueOf(this.mIntent.getComponent().flattenToShortString());
                        java.lang.String protoLogParam3 = java.lang.String.valueOf(this.mTaskAffinity);
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 6022828946761399284L, 0, null, protoLogParam04, protoLogParam1, protoLogParam2, protoLogParam3);
                    }
                    if (this.mFindTaskResultExt.shouldSkipReuseTask(task, this.mIntent)) {
                        return false;
                    }
                    if (task.realActivity != null && task.realActivity.compareTo(this.cls) == 0 && java.util.Objects.equals(this.documentData, taskDocumentData)) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -3413620974545388702L, 0, null, null);
                        }
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                            java.lang.String protoLogParam05 = java.lang.String.valueOf(this.mIntent);
                            java.lang.String protoLogParam12 = java.lang.String.valueOf(r.intent);
                            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -2649361982747625232L, 0, null, protoLogParam05, protoLogParam12);
                        }
                        this.mIdealRecord = r;
                        return true;
                    }
                    if (affinityIntent != null && affinityIntent.getComponent() != null && affinityIntent.getComponent().compareTo(this.cls) == 0 && java.util.Objects.equals(this.documentData, taskDocumentData)) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -3413620974545388702L, 0, null, null);
                        }
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                            java.lang.String protoLogParam06 = java.lang.String.valueOf(this.mIntent);
                            java.lang.String protoLogParam13 = java.lang.String.valueOf(r.intent);
                            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -2649361982747625232L, 0, null, protoLogParam06, protoLogParam13);
                        }
                        this.mIdealRecord = r;
                        return true;
                    }
                    if (!this.isDocument && !taskIsDocument && this.mIdealRecord == null && this.mCandidateRecord == null && task.rootAffinity != null) {
                        if (task.rootAffinity.equals(this.mTaskAffinity) && task.isSameRequiredDisplayCategory(this.mInfo)) {
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 7046266138098744790L, 0, null, null);
                            }
                            this.mCandidateRecord = r;
                        }
                    } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                        java.lang.String protoLogParam07 = java.lang.String.valueOf(task);
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 6481733556290926693L, 0, null, protoLogParam07);
                    }
                    return false;
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                    java.lang.String protoLogParam08 = java.lang.String.valueOf(task);
                    java.lang.String protoLogParam14 = java.lang.String.valueOf(r);
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, -4764624740388751268L, 0, null, protoLogParam08, protoLogParam14);
                }
                return false;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TASKS_enabled[0]) {
                java.lang.String protoLogParam09 = java.lang.String.valueOf(task);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TASKS, 6841550641928224256L, 0, null, protoLogParam09);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(com.android.server.wm.WindowState w) {
        if (w.mHasSurface) {
            try {
                w.mClient.closeSystemDialogs(this.mCloseSystemDialogsReason);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    RootWindowContainer(com.android.server.wm.WindowManagerService service) {
        super(service);
        this.mLastWindowFreezeSource = null;
        this.mScreenBrightnessOverride = Float.NaN;
        this.mUserActivityTimeout = -1L;
        this.mUpdateRotation = false;
        this.mObscureApplicationContentOnSecondaryDisplays = false;
        this.mSustainedPerformanceModeEnabled = false;
        this.mSustainedPerformanceModeCurrent = false;
        this.mOrientationChangeComplete = true;
        this.mWallpaperActionPending = false;
        this.mTopFocusedDisplayId = -1;
        this.mTopFocusedAppByProcess = new android.util.ArrayMap<>();
        this.mDisplayAccessUIDs = new android.util.SparseArray<>();
        this.mDisplayTransactions = new android.util.SparseArray<>();
        this.mUserRootTaskInFront = new android.util.SparseIntArray(2);
        this.mSocExt = (com.android.server.wm.IRootWindowContainerSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IRootWindowContainerSocExt.class).base(this).create();
        this.mRootWindowContainerExt = (com.android.server.wm.IRootWindowContainerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IRootWindowContainerExt.class).base(this).create();
        this.mSleepTokens = new android.util.SparseArray<>();
        this.mTaskLayersChanged = true;
        this.mRankTaskLayersRunnable = new com.android.server.wm.RootWindowContainer.RankTaskLayersRunnable();
        this.mDestroyAllActivitiesRunnable = new com.android.server.wm.RootWindowContainer.AnonymousClass1();
        this.mMaybeAbortPipEnterRunnable = null;
        this.mTmpFindTaskResult = new com.android.server.wm.RootWindowContainer.FindTaskResult();
        this.mCloseSystemDialogsConsumer = new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda45
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$0((com.android.server.wm.WindowState) obj);
            }
        };
        this.mFindOrientationChangingFunction = new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda46
            public final boolean apply(java.lang.Object obj) {
                return this.f$0.lambda$new$8((com.android.server.wm.WindowState) obj);
            }
        };
        this.mFinishDisabledPackageActivitiesHelper = new com.android.server.wm.RootWindowContainer.FinishDisabledPackageActivitiesHelper();
        this.mHandler = new com.android.server.wm.RootWindowContainer.MyHandler(service.mH.getLooper());
        this.mService = service.mAtmService;
        this.mTaskSupervisor = this.mService.mTaskSupervisor;
        this.mTaskSupervisor.mRootWindowContainer = this;
        com.android.server.wm.ActivityTaskManagerService activityTaskManagerService = this.mService;
        java.util.Objects.requireNonNull(activityTaskManagerService);
        this.mDisplayOffTokenAcquirer = activityTaskManagerService.new SleepTokenAcquirer(DISPLAY_OFF_SLEEP_TOKEN_TAG);
        this.mDeviceStateController = new com.android.server.wm.DeviceStateController(service.mContext, service.mGlobalLock);
        this.mDisplayRotationCoordinator = new com.android.server.wm.DisplayRotationCoordinator();
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
    boolean updateFocusedWindowLocked(int mode, boolean updateInputWindows) {
        this.mTopFocusedAppByProcess.clear();
        boolean changed = false;
        int topFocusedDisplayId = -1;
        int i = this.mChildren.size();
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            com.android.server.wm.DisplayContent dc = (com.android.server.wm.DisplayContent) this.mChildren.get(i);
            changed |= dc.updateFocusedWindowLocked(mode, updateInputWindows, topFocusedDisplayId);
            com.android.server.wm.WindowState newFocus = dc.mCurrentFocus;
            if (newFocus != null) {
                int pidOfNewFocus = newFocus.mSession.mPid;
                if (this.mTopFocusedAppByProcess.get(java.lang.Integer.valueOf(pidOfNewFocus)) == null) {
                    this.mTopFocusedAppByProcess.put(java.lang.Integer.valueOf(pidOfNewFocus), newFocus.mActivityRecord);
                }
                if (topFocusedDisplayId == -1) {
                    topFocusedDisplayId = dc.getDisplayId();
                }
            } else if (topFocusedDisplayId == -1 && dc.mFocusedApp != null) {
                topFocusedDisplayId = dc.getDisplayId();
            }
        }
        if (topFocusedDisplayId == -1) {
            topFocusedDisplayId = 0;
        }
        if (this.mTopFocusedDisplayId != topFocusedDisplayId) {
            this.mTopFocusedDisplayId = topFocusedDisplayId;
            this.mWmService.mInputManager.setFocusedDisplay(topFocusedDisplayId);
            this.mWmService.mPolicy.setTopFocusedDisplay(topFocusedDisplayId);
            this.mWmService.mAccessibilityController.setFocusedDisplay(topFocusedDisplayId);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[0]) {
                long protoLogParam0 = topFocusedDisplayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 3331249072840061049L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
        }
        return changed;
    }

    com.android.server.wm.DisplayContent getTopFocusedDisplayContent() {
        com.android.server.wm.DisplayContent dc = getDisplayContent(this.mTopFocusedDisplayId);
        return dc != null ? dc : getDisplayContent(0);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isOnTop() {
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    void onChildPositionChanged(com.android.server.wm.WindowContainer child) {
        this.mWmService.updateFocusedWindowLocked(0, !this.mWmService.mPerDisplayFocusEnabled);
        this.mTaskSupervisor.updateTopResumedActivityIfNeeded("onChildPositionChanged");
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isAttached() {
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
    void onSettingsRetrieved() {
        int numDisplays = this.mChildren.size();
        for (int displayNdx = 0; displayNdx < numDisplays; displayNdx++) {
            com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) this.mChildren.get(displayNdx);
            boolean changed = this.mWmService.mDisplayWindowSettings.updateSettingsForDisplay(displayContent);
            if (changed) {
                displayContent.reconfigureDisplayLocked();
                if (displayContent.isDefaultDisplay) {
                    android.content.res.Configuration newConfig = this.mWmService.computeNewConfiguration(displayContent.getDisplayId());
                    this.mWmService.mAtmService.updateConfigurationLocked(newConfig, null, false);
                }
            }
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
    boolean isLayoutNeeded() {
        int numDisplays = this.mChildren.size();
        for (int displayNdx = 0; displayNdx < numDisplays; displayNdx++) {
            com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) this.mChildren.get(displayNdx);
            if (displayContent.isLayoutNeeded()) {
                return true;
            }
        }
        return false;
    }

    void getWindowsByName(java.util.ArrayList<com.android.server.wm.WindowState> output, java.lang.String name) {
        int objectId = 0;
        try {
            objectId = java.lang.Integer.parseInt(name, 16);
            name = null;
        } catch (java.lang.RuntimeException e) {
        }
        getWindowsByName(output, name, objectId);
    }

    private void getWindowsByName(final java.util.ArrayList<com.android.server.wm.WindowState> output, final java.lang.String name, final int objectId) {
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda35
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.RootWindowContainer.lambda$getWindowsByName$1(name, output, objectId, (com.android.server.wm.WindowState) obj);
            }
        }, true);
    }

    static /* synthetic */ void lambda$getWindowsByName$1(java.lang.String name, java.util.ArrayList output, int objectId, com.android.server.wm.WindowState w) {
        if (name != null) {
            if (w.mAttrs.getTitle().toString().contains(name)) {
                output.add(w);
            }
        } else if (java.lang.System.identityHashCode(w) == objectId) {
            output.add(w);
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
    com.android.server.wm.WindowToken getWindowToken(android.os.IBinder binder) {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent dc = (com.android.server.wm.DisplayContent) this.mChildren.get(i);
            com.android.server.wm.WindowToken wtoken = dc.getWindowToken(binder);
            if (wtoken != null) {
                return wtoken;
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
    com.android.server.wm.DisplayContent getWindowTokenDisplay(com.android.server.wm.WindowToken token) {
        if (token == null) {
            return null;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent dc = (com.android.server.wm.DisplayContent) this.mChildren.get(i);
            com.android.server.wm.WindowToken current = dc.getWindowToken(token.token);
            if (current == token) {
                return dc;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.ConfigurationContainer
    public void dispatchConfigurationToChild(com.android.server.wm.DisplayContent child, android.content.res.Configuration config) {
        if (child.isDefaultDisplay) {
            child.performDisplayOverrideConfigUpdate(config);
            return;
        }
        this.mWindowContainerExt.dispatchConfigurationToChild(child, config);
        this.mWmService.mAtmService.getWrapper().getExtImpl().getRemoteTaskManager().onConfigurationChanged(config, child);
        child.onConfigurationChanged(config);
    }

    void refreshSecureSurfaceState() {
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda40
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.WindowState windowState = (com.android.server.wm.WindowState) obj;
                windowState.setSecureLocked(windowState.isSecureLocked());
            }
        }, true);
    }

    void updateHiddenWhileSuspendedState(final android.util.ArraySet<java.lang.String> packages, final boolean suspended) {
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda25
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.RootWindowContainer.lambda$updateHiddenWhileSuspendedState$3(packages, suspended, (com.android.server.wm.WindowState) obj);
            }
        }, false);
    }

    static /* synthetic */ void lambda$updateHiddenWhileSuspendedState$3(android.util.ArraySet packages, boolean suspended, com.android.server.wm.WindowState w) {
        if (packages.contains(w.getOwningPackage())) {
            w.setHiddenWhileSuspended(suspended);
        }
    }

    void updateAppOpsState() {
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.WindowState) obj).updateAppOpsState();
            }
        }, false);
    }

    static /* synthetic */ boolean lambda$canShowStrictModeViolation$5(int pid, com.android.server.wm.WindowState w) {
        return w.mSession.mPid == pid && w.isVisible();
    }

    boolean canShowStrictModeViolation(final int pid) {
        com.android.server.wm.WindowState win = getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda47
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.RootWindowContainer.lambda$canShowStrictModeViolation$5(pid, (com.android.server.wm.WindowState) obj);
            }
        });
        return win != null;
    }

    void closeSystemDialogs(java.lang.String reason) {
        this.mCloseSystemDialogsReason = reason;
        forAllWindows(this.mCloseSystemDialogsConsumer, false);
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
    boolean hasPendingLayoutChanges(com.android.server.wm.WindowAnimator animator) {
        boolean hasChanges = false;
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            int pendingChanges = ((com.android.server.wm.DisplayContent) this.mChildren.get(i)).pendingLayoutChanges;
            if ((pendingChanges & 4) != 0) {
                animator.mBulkUpdateParams |= 2;
            }
            if (pendingChanges != 0) {
                hasChanges = true;
            }
        }
        return hasChanges;
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
    boolean reclaimSomeSurfaceMemory(com.android.server.wm.WindowStateAnimator winAnimator, java.lang.String operation, boolean secure) throws java.lang.Throwable {
        com.android.server.wm.WindowSurfaceController surfaceController = winAnimator.mSurfaceController;
        boolean leakedSurface = false;
        boolean killedApps = false;
        com.android.server.wm.EventLogTags.writeWmNoSurfaceMemory(winAnimator.mWin.toString(), winAnimator.mSession.mPid, operation);
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            android.util.Slog.i(TAG, "Out of memory for surface!  Looking for leaks...");
            int numDisplays = this.mChildren.size();
            for (int displayNdx = 0; displayNdx < numDisplays; displayNdx++) {
                leakedSurface |= ((com.android.server.wm.DisplayContent) this.mChildren.get(displayNdx)).destroyLeakedSurfaces();
            }
            boolean z = false;
            if (!leakedSurface) {
                android.util.Slog.w(TAG, "No leaked surfaces; killing applications!");
                final android.util.SparseIntArray pidCandidates = new android.util.SparseIntArray();
                boolean killedApps2 = false;
                int displayNdx2 = 0;
                while (displayNdx2 < numDisplays) {
                    try {
                        ((com.android.server.wm.DisplayContent) this.mChildren.get(displayNdx2)).forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda39
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                this.f$0.lambda$reclaimSomeSurfaceMemory$6(pidCandidates, (com.android.server.wm.WindowState) obj);
                            }
                        }, z);
                        if (pidCandidates.size() > 0) {
                            int[] pids = new int[pidCandidates.size()];
                            for (int i = 0; i < pids.length; i++) {
                                pids[i] = pidCandidates.keyAt(i);
                            }
                            try {
                                try {
                                    if (this.mWmService.mActivityManager.killPids(pids, "Free memory", secure)) {
                                        killedApps2 = true;
                                    }
                                } catch (android.os.RemoteException e) {
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    android.os.Binder.restoreCallingIdentity(callingIdentity);
                                    throw th;
                                }
                            } catch (android.os.RemoteException e2) {
                            }
                        }
                        displayNdx2++;
                        z = false;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
                killedApps = killedApps2;
            }
            if (leakedSurface || killedApps) {
                try {
                    android.util.Slog.w(TAG, "Looks like we have reclaimed some memory, clearing surface for retry.");
                    if (surfaceController != null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_SURFACE_ALLOC_enabled[2]) {
                            java.lang.String protoLogParam0 = java.lang.String.valueOf(winAnimator.mWin);
                            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, 865845626039449679L, 0, null, protoLogParam0);
                        }
                        android.view.SurfaceControl.Transaction t = this.mWmService.mTransactionFactory.get();
                        winAnimator.destroySurface(t);
                        t.apply();
                        if (winAnimator.mWin.mActivityRecord != null) {
                            winAnimator.mWin.mActivityRecord.removeStartingWindow();
                        }
                    }
                    try {
                        winAnimator.mWin.mClient.dispatchGetNewSurface();
                    } catch (android.os.RemoteException e3) {
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    android.os.Binder.restoreCallingIdentity(callingIdentity);
                    throw th;
                }
            }
            android.os.Binder.restoreCallingIdentity(callingIdentity);
            return leakedSurface || killedApps;
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reclaimSomeSurfaceMemory$6(android.util.SparseIntArray pidCandidates, com.android.server.wm.WindowState w) {
        if (this.mWmService.mForceRemoves.contains(w)) {
            return;
        }
        com.android.server.wm.WindowStateAnimator wsa = w.mWinAnimator;
        if (wsa.mSurfaceController != null) {
            pidCandidates.append(wsa.mSession.mPid, wsa.mSession.mPid);
        }
    }

    void performSurfacePlacement() {
        if (android.os.Trace.isTagEnabled(32L)) {
            android.os.Trace.traceBegin(32L, "performSurfacePlacement:" + android.os.Debug.getCallers(3));
        } else {
            android.os.Trace.traceBegin(32L, "performSurfacePlacement");
        }
        try {
            performSurfacePlacementNoTrace();
        } finally {
            android.os.Trace.traceEnd(32L);
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
    void performSurfacePlacementNoTrace() {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WINDOW_TRACE) {
            android.util.Slog.v(TAG, "performSurfacePlacementInner: entry. Called by " + android.os.Debug.getCallers(3));
        }
        if (this.mWmService.mFocusMayChange) {
            this.mWmService.mFocusMayChange = false;
            this.mWmService.updateFocusedWindowLocked(3, false);
        }
        float brightnessOverride = Float.NaN;
        this.mScreenBrightnessOverride = Float.NaN;
        this.mRootWindowContainerExt.hookPerformSurfacePlacementNoTraceInit();
        this.mUserActivityTimeout = -1L;
        this.mObscureApplicationContentOnSecondaryDisplays = false;
        this.mSustainedPerformanceModeCurrent = false;
        this.mWmService.mTransactionSequence++;
        com.android.server.wm.DisplayContent defaultDisplay = this.mWmService.getDefaultDisplayContentLocked();
        com.android.server.wm.WindowSurfacePlacer surfacePlacer = this.mWmService.mWindowPlacerLocked;
        android.os.Trace.traceBegin(32L, "applySurfaceChanges");
        try {
            try {
                applySurfaceChangesTransaction();
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.wtf(TAG, "Unhandled exception in Window Manager", e);
            }
            if (com.android.window.flags.Flags.bundleClientTransactionFlag()) {
                handleResizingWindows();
                this.mWmService.mAtmService.getLifecycleManager().dispatchPendingTransactions();
            }
            this.mWmService.mAtmService.mTaskOrganizerController.dispatchPendingEvents();
            this.mWmService.mAtmService.mTaskFragmentOrganizerController.dispatchPendingEvents();
            this.mWmService.mSyncEngine.onSurfacePlacement();
            checkAppTransitionReady(surfacePlacer);
            com.android.server.wm.RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
            if (recentsAnimationController != null) {
                recentsAnimationController.checkAnimationReady(defaultDisplay.mWallpaperController);
            }
            this.mWmService.mAtmService.mBackNavigationController.checkAnimationReady(defaultDisplay.mWallpaperController);
            this.mRootWindowContainerExt.checkAnimationReady();
            for (int displayNdx = 0; displayNdx < this.mChildren.size(); displayNdx++) {
                com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) this.mChildren.get(displayNdx);
                if (displayContent.mWallpaperMayChange) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[1]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, -4150611780753674023L, 0, null, null);
                    }
                    displayContent.pendingLayoutChanges |= 4;
                    if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                        surfacePlacer.debugLayoutRepeats("WallpaperMayChange", displayContent.pendingLayoutChanges);
                    }
                }
            }
            if (this.mWmService.mFocusMayChange) {
                this.mWmService.mFocusMayChange = false;
                this.mWmService.updateFocusedWindowLocked(2, false);
            }
            if (isLayoutNeeded()) {
                defaultDisplay.pendingLayoutChanges |= 1;
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                    surfacePlacer.debugLayoutRepeats("mLayoutNeeded", defaultDisplay.pendingLayoutChanges);
                }
            }
            if (!com.android.window.flags.Flags.bundleClientTransactionFlag()) {
                handleResizingWindows();
            }
            clearFrameChangingWindows();
            if (this.mWmService.mDisplayFrozen && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                boolean protoLogParam0 = this.mOrientationChangeComplete;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 4177291132772627699L, 3, null, java.lang.Boolean.valueOf(protoLogParam0));
            }
            boolean protoLogParam02 = this.mOrientationChangeComplete;
            if (protoLogParam02) {
                if (this.mWmService.mWindowsFreezingScreen != 0) {
                    this.mWmService.mWindowsFreezingScreen = 0;
                    this.mWmService.mLastFinishedFreezeSource = this.mLastWindowFreezeSource;
                    this.mWmService.mH.removeMessages(11);
                }
                this.mWmService.stopFreezingDisplayLocked();
            }
            int i = this.mWmService.mDestroySurface.size();
            if (i > 0) {
                com.android.server.wm.IWindowManagerServiceExt mWmsExt = (com.android.server.wm.IWindowManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowManagerServiceExt.class).create();
                do {
                    i--;
                    com.android.server.wm.WindowState win = this.mWmService.mDestroySurface.get(i);
                    if (!this.mRootWindowContainerExt.shouldWindowSurfaceSaved(win, win.getDisplayContent())) {
                        mWmsExt.getDestroySavedSurface().remove(win);
                        win.mDestroying = false;
                        com.android.server.wm.DisplayContent displayContent2 = win.getDisplayContent();
                        if (displayContent2.mInputMethodWindow == win) {
                            displayContent2.setInputMethodWindowLocked(null);
                        }
                        if (displayContent2.mWallpaperController.isWallpaperTarget(win)) {
                            displayContent2.pendingLayoutChanges |= 4;
                        }
                        win.destroySurfaceUnchecked();
                    }
                } while (i > 0);
                this.mWmService.mDestroySurface.clear();
                this.mWmService.mDestroySurface.addAll(mWmsExt.getDestroySavedSurface());
            }
            for (int displayNdx2 = 0; displayNdx2 < this.mChildren.size(); displayNdx2++) {
                com.android.server.wm.DisplayContent displayContent3 = (com.android.server.wm.DisplayContent) this.mChildren.get(displayNdx2);
                if (displayContent3.pendingLayoutChanges != 0) {
                    displayContent3.setLayoutNeeded();
                }
            }
            if (!this.mWmService.mDisplayFrozen) {
                this.mRootWindowContainerExt.updatePendingScreenBrightnessOverrideMap();
                if (this.mScreenBrightnessOverride >= 0.0f && this.mScreenBrightnessOverride <= 1.0f) {
                    brightnessOverride = this.mScreenBrightnessOverride;
                }
                int brightnessFloatAsIntBits = java.lang.Float.floatToIntBits(brightnessOverride);
                this.mHandler.obtainMessage(1, brightnessFloatAsIntBits, 0).sendToTarget();
                this.mHandler.obtainMessage(2, java.lang.Long.valueOf(this.mUserActivityTimeout)).sendToTarget();
            }
            if (this.mSustainedPerformanceModeCurrent != this.mSustainedPerformanceModeEnabled) {
                this.mSustainedPerformanceModeEnabled = this.mSustainedPerformanceModeCurrent;
                this.mWmService.mPowerManagerInternal.setPowerMode(2, this.mSustainedPerformanceModeEnabled);
            }
            if (this.mUpdateRotation) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[0]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -5513616928833586179L, 0, null, null);
                }
                this.mUpdateRotation = updateRotationUnchecked();
            }
            if (!this.mWmService.mWaitingForDrawnCallbacks.isEmpty() || (this.mOrientationChangeComplete && !isLayoutNeeded() && !this.mUpdateRotation)) {
                this.mWmService.checkDrawnWindowsLocked();
            }
            forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda36
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.RootWindowContainer.lambda$performSurfacePlacementNoTrace$7((com.android.server.wm.DisplayContent) obj);
                }
            });
            this.mWmService.enableScreenIfNeededLocked();
            this.mWmService.scheduleAnimationLocked();
            this.mRootWindowContainerExt.checkCachedSurfaceBufferRelease(this);
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WINDOW_TRACE) {
                android.util.Slog.e(TAG, "performSurfacePlacementInner exit");
            }
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    static /* synthetic */ void lambda$performSurfacePlacementNoTrace$7(com.android.server.wm.DisplayContent dc) {
        dc.getInputMonitor().updateInputWindowsLw(true);
        dc.updateSystemGestureExclusion();
        dc.updateKeepClearAreas();
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
    private void checkAppTransitionReady(com.android.server.wm.WindowSurfacePlacer surfacePlacer) {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent curDisplay = (com.android.server.wm.DisplayContent) this.mChildren.get(i);
            if (curDisplay.mAppTransition.isReady()) {
                curDisplay.mAppTransitionController.handleAppTransitionReady();
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                    surfacePlacer.debugLayoutRepeats("after handleAppTransitionReady", curDisplay.pendingLayoutChanges);
                }
            }
            if (curDisplay.mAppTransition.isRunning() && !curDisplay.isAppTransitioning()) {
                curDisplay.handleAnimatingStoppedAndTransition();
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                    surfacePlacer.debugLayoutRepeats("after handleAnimStopAndXitionLock", curDisplay.pendingLayoutChanges);
                }
            }
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
    private void applySurfaceChangesTransaction() {
        com.android.server.wm.DisplayContent defaultDc = this.mDefaultDisplay;
        android.view.DisplayInfo defaultInfo = defaultDc.getDisplayInfo();
        int defaultDw = defaultInfo.logicalWidth;
        int defaultDh = defaultInfo.logicalHeight;
        android.view.SurfaceControl.Transaction t = defaultDc.getSyncTransaction();
        if (this.mWmService.mWatermark != null) {
            this.mWmService.mWatermark.positionSurface(defaultDw, defaultDh, t);
        }
        this.mRootWindowContainerExt.positionSurface(defaultDw, defaultDh);
        if (this.mWmService.mStrictModeFlash != null) {
            this.mWmService.mStrictModeFlash.positionSurface(defaultDw, defaultDh, t);
        }
        if (this.mWmService.mEmulatorDisplayOverlay != null) {
            this.mWmService.mEmulatorDisplayOverlay.positionSurface(defaultDw, defaultDh, defaultDc.getRotation(), t);
        }
        int count = this.mChildren.size();
        for (int j = 0; j < count; j++) {
            com.android.server.wm.DisplayContent dc = (com.android.server.wm.DisplayContent) this.mChildren.get(j);
            dc.applySurfaceChangesTransaction();
            this.mDisplayTransactions.append(dc.mDisplayId, dc.getSyncTransaction());
        }
        if (this.mWmService.mDisplayEnabled || this.mRootWindowContainerExt.isNotLargeFoldDevice()) {
            this.mWmService.mDisplayManagerInternal.performTraversal(t, this.mDisplayTransactions);
        }
        this.mDisplayTransactions.clear();
    }

    private void handleResizingWindows() {
        for (int i = this.mWmService.mResizingWindows.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState win = this.mWmService.mResizingWindows.get(i);
            if (!win.mAppFreezing && !win.getDisplayContent().mWaitingForConfig) {
                win.reportResized();
                this.mWmService.mResizingWindows.remove(i);
            }
        }
        if (this.mWmService.mResizingWindows.isEmpty()) {
            this.mRootWindowContainerExt.handleResizingWindows();
        }
    }

    private void clearFrameChangingWindows() {
        java.util.ArrayList<com.android.server.wm.WindowState> frameChangingWindows = this.mWmService.mFrameChangingWindows;
        for (int i = frameChangingWindows.size() - 1; i >= 0; i--) {
            frameChangingWindows.get(i).updateLastFrames();
        }
        frameChangingWindows.clear();
    }

    boolean handleNotObscuredLocked(com.android.server.wm.WindowState w, boolean obscured, boolean syswin) {
        boolean displayHasContent;
        boolean displayHasContent2;
        boolean onScreen = w.isOnScreen();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_KEEP_SCREEN_ON_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(w);
            boolean protoLogParam1 = w.mHasSurface;
            boolean protoLogParam3 = w.isDisplayed();
            long protoLogParam4 = w.mAttrs.userActivityTimeout;
            displayHasContent = false;
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, -7698723716637247994L, 508, null, protoLogParam0, java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(onScreen), java.lang.Boolean.valueOf(protoLogParam3), java.lang.Long.valueOf(protoLogParam4));
        } else {
            displayHasContent = false;
        }
        if (!onScreen) {
            return false;
        }
        if (!syswin && w.mAttrs.userActivityTimeout >= 0 && this.mUserActivityTimeout < 0) {
            this.mUserActivityTimeout = w.mAttrs.userActivityTimeout;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_KEEP_SCREEN_ON_enabled[0]) {
                long protoLogParam02 = this.mUserActivityTimeout;
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, 8621291657500572364L, 1, null, java.lang.Long.valueOf(protoLogParam02));
            }
        }
        if (w.isDrawn() || (w.mActivityRecord != null && w.mActivityRecord.firstWindowDrawn && w.mActivityRecord.isVisibleRequested())) {
            this.mRootWindowContainerExt.hookHandleNotObscuredLocked(w, obscured, syswin, this.mScreenBrightnessOverride);
            if (!syswin && w.mAttrs.screenBrightness >= 0.0f && java.lang.Float.isNaN(this.mScreenBrightnessOverride)) {
                this.mScreenBrightnessOverride = w.mAttrs.screenBrightness;
            }
            com.android.server.wm.DisplayContent displayContent = w.getDisplayContent();
            if (displayContent != null && displayContent.isDefaultDisplay) {
                if ((w.isDreamWindow() || this.mWmService.mPolicy.isKeyguardShowing()) && this.mRootWindowContainerExt.shouldObscureApplicationContentOnSecondaryDisplay()) {
                    this.mObscureApplicationContentOnSecondaryDisplays = true;
                }
                displayHasContent2 = true;
            } else if (displayContent != null && (!this.mObscureApplicationContentOnSecondaryDisplays || displayContent.isKeyguardAlwaysUnlocked() || (obscured && w.mAttrs.type == 2009))) {
                displayHasContent2 = true;
            } else {
                displayHasContent2 = displayHasContent;
            }
            if ((w.mAttrs.privateFlags & 65536) != 0) {
                this.mSustainedPerformanceModeCurrent = true;
            }
            return displayHasContent2;
        }
        return displayHasContent;
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
    boolean updateRotationUnchecked() {
        boolean changed = false;
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if (((com.android.server.wm.DisplayContent) this.mChildren.get(i)).getDisplayRotation().updateRotationAndSendNewConfigIfChanged()) {
                changed = true;
            }
        }
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$8(com.android.server.wm.WindowState w) {
        if (!w.getOrientationChanging() || w.isDrawn() || this.mRootWindowContainerExt.shouldSkipUnFreezeCheck(w)) {
            return false;
        }
        this.mTmpOrientationChangeComplete = false;
        return true;
    }

    void updateOrientationChangeIfNeeded() {
        if (this.mOrientationChangeComplete) {
            return;
        }
        this.mTmpOrientationChangeComplete = true;
        forAllWindows(this.mFindOrientationChangingFunction, true);
        this.mOrientationChangeComplete = this.mTmpOrientationChangeComplete;
    }

    boolean copyAnimToLayoutParams() {
        boolean doRequest = false;
        int bulkUpdateParams = this.mWmService.mAnimator.mBulkUpdateParams;
        if ((bulkUpdateParams & 1) != 0) {
            this.mUpdateRotation = true;
            doRequest = true;
        }
        if (this.mOrientationChangeComplete) {
            this.mLastWindowFreezeSource = this.mWmService.mAnimator.mLastWindowFreezeSource;
            if (this.mWmService.mWindowsFreezingScreen != 0) {
                doRequest = true;
            }
        }
        if ((bulkUpdateParams & 2) != 0) {
            this.mWallpaperActionPending = true;
        }
        return doRequest;
    }

    private final class MyHandler extends android.os.Handler {
        public MyHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.wm.RootWindowContainer.this.mWmService.mPowerManagerInternal.setScreenBrightnessOverrideFromWindowManager(java.lang.Float.intBitsToFloat(msg.arg1));
                    return;
                case 2:
                    com.android.server.wm.RootWindowContainer.this.mWmService.mPowerManagerInternal.setUserActivityTimeoutOverrideFromWindowManager(((java.lang.Long) msg.obj).longValue());
                    return;
                case 3:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.RootWindowContainer.this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            com.android.server.wm.RootWindowContainer.this.sendSleepTransition((com.android.server.wm.DisplayContent) msg.obj);
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
    void dumpDisplayContents(java.io.PrintWriter pw) {
        pw.println("WINDOW MANAGER DISPLAY CONTENTS (dumpsys window displays)");
        if (this.mWmService.mDisplayReady) {
            int count = this.mChildren.size();
            for (int i = 0; i < count; i++) {
                com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) this.mChildren.get(i);
                displayContent.dump(pw, "  ", true);
            }
            return;
        }
        pw.println("  NO DISPLAY");
    }

    void dumpTopFocusedDisplayId(java.io.PrintWriter pw) {
        pw.print("  mTopFocusedDisplayId=");
        pw.println(this.mTopFocusedDisplayId);
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
    void dumpLayoutNeededDisplayIds(java.io.PrintWriter pw) {
        if (!isLayoutNeeded()) {
            return;
        }
        pw.print("  mLayoutNeeded on displays=");
        int count = this.mChildren.size();
        for (int displayNdx = 0; displayNdx < count; displayNdx++) {
            com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) this.mChildren.get(displayNdx);
            if (displayContent.isLayoutNeeded()) {
                pw.print(displayContent.getDisplayId());
            }
        }
        pw.println();
    }

    void dumpWindowsNoHeader(final java.io.PrintWriter pw, final boolean dumpAll, final java.util.ArrayList<com.android.server.wm.WindowState> windows) {
        final int[] index = new int[1];
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda30
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.RootWindowContainer.lambda$dumpWindowsNoHeader$9(windows, pw, index, dumpAll, (com.android.server.wm.WindowState) obj);
            }
        }, true);
    }

    static /* synthetic */ void lambda$dumpWindowsNoHeader$9(java.util.ArrayList windows, java.io.PrintWriter pw, int[] index, boolean dumpAll, com.android.server.wm.WindowState w) {
        if (windows == null || windows.contains(w)) {
            pw.println("  Window #" + index[0] + " " + w + ":");
            w.dump(pw, "    ", dumpAll || windows != null);
            index[0] = index[0] + 1;
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
    void dumpTokens(java.io.PrintWriter pw, boolean dumpAll) {
        pw.println("  All tokens:");
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            ((com.android.server.wm.DisplayContent) this.mChildren.get(i)).dumpTokens(pw, dumpAll);
        }
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        if (logLevel == 2 && !isVisible()) {
            return;
        }
        long token = proto.start(fieldId);
        super.dumpDebug(proto, 1146756268033L, logLevel);
        this.mTaskSupervisor.getKeyguardController().dumpDebug(proto, 1146756268037L);
        proto.write(1133871366150L, this.mTaskSupervisor.mRecentTasks.isRecentsComponentHomeActivity(this.mCurrentUser));
        proto.end(token);
    }

    @Override // com.android.server.wm.ConfigurationContainer
    java.lang.String getName() {
        return "ROOT";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public void removeChild(com.android.server.wm.DisplayContent dc) {
        super.removeChild(dc);
        if (this.mTopFocusedDisplayId == dc.getDisplayId()) {
            this.mWmService.updateFocusedWindowLocked(0, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
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
    public void forAllDisplays(java.util.function.Consumer<com.android.server.wm.DisplayContent> callback) {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            callback.accept((com.android.server.wm.DisplayContent) this.mChildren.get(i));
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
    void forAllDisplayPolicies(java.util.function.Consumer<com.android.server.wm.DisplayPolicy> callback) {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            callback.accept(((com.android.server.wm.DisplayContent) this.mChildren.get(i)).getDisplayPolicy());
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
    com.android.server.wm.WindowState getCurrentInputMethodWindow() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) this.mChildren.get(i);
            if (displayContent.mInputMethodWindow != null) {
                return displayContent.mInputMethodWindow;
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
    void getDisplayContextsWithNonToastVisibleWindows(final int pid, java.util.List<android.content.Context> outContexts) {
        if (outContexts == null) {
            return;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent dc = (com.android.server.wm.DisplayContent) this.mChildren.get(i);
            if (dc.getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda24
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.RootWindowContainer.lambda$getDisplayContextsWithNonToastVisibleWindows$10(pid, (com.android.server.wm.WindowState) obj);
                }
            }) != null) {
                outContexts.add(dc.getDisplayUiContext());
            }
        }
    }

    static /* synthetic */ boolean lambda$getDisplayContextsWithNonToastVisibleWindows$10(int pid, com.android.server.wm.WindowState w) {
        return pid == w.mSession.mPid && w.isVisibleNow() && w.mAttrs.type != 2005;
    }

    android.content.Context getDisplayUiContext(int displayId) {
        if (getDisplayContent(displayId) != null) {
            return getDisplayContent(displayId).getDisplayUiContext();
        }
        return null;
    }

    void setWindowManager(com.android.server.wm.WindowManagerService wm) {
        this.mWindowManager = wm;
        this.mDisplayManager = (android.hardware.display.DisplayManager) this.mService.mContext.getSystemService(android.hardware.display.DisplayManager.class);
        this.mDisplayManager.registerDisplayListener(this, this.mService.mUiHandler);
        this.mDisplayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        android.view.Display[] displays = this.mDisplayManager.getDisplays();
        for (android.view.Display display : displays) {
            com.android.server.wm.DisplayContent displayContent = new com.android.server.wm.DisplayContent(display, this, this.mDeviceStateController);
            addChild(displayContent, Integer.MIN_VALUE);
            if (displayContent.mDisplayId == 0) {
                this.mDefaultDisplay = displayContent;
            }
        }
        com.android.server.wm.TaskDisplayArea defaultTaskDisplayArea = getDefaultTaskDisplayArea();
        defaultTaskDisplayArea.getOrCreateRootHomeTask(true);
        positionChildAt(Integer.MAX_VALUE, defaultTaskDisplayArea.mDisplayContent, false);
    }

    void onDisplayManagerReceivedDeviceState(int deviceState) {
        this.mDeviceStateController.onDeviceStateReceivedByDisplayManager(deviceState);
    }

    com.android.server.wm.DisplayContent getDefaultDisplay() {
        return this.mDefaultDisplay;
    }

    com.android.server.wm.DisplayRotationCoordinator getDisplayRotationCoordinator() {
        return this.mDisplayRotationCoordinator;
    }

    com.android.server.wm.TaskDisplayArea getDefaultTaskDisplayArea() {
        return this.mDefaultDisplay.getDefaultTaskDisplayArea();
    }

    com.android.server.wm.DisplayContent getDisplayContent(java.lang.String uniqueId) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent display = (com.android.server.wm.DisplayContent) getChildAt(i);
            boolean isValid = display.mDisplay.isValid();
            if (isValid && display.mDisplay.getUniqueId() != null && display.mDisplay.getUniqueId().equals(uniqueId)) {
                return display;
            }
        }
        return null;
    }

    com.android.server.wm.DisplayContent getDisplayContent(int displayId) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) getChildAt(i);
            if (displayContent.mDisplayId == displayId) {
                return displayContent;
            }
        }
        return null;
    }

    com.android.server.wm.DisplayContent getDisplayContentOrCreate(int displayId) {
        android.view.Display display;
        com.android.server.wm.DisplayContent displayContent = getDisplayContent(displayId);
        if (displayContent != null) {
            return displayContent;
        }
        if (this.mDisplayManager == null || (display = this.mDisplayManager.getDisplay(displayId)) == null) {
            return null;
        }
        com.android.server.wm.DisplayContent displayContent2 = new com.android.server.wm.DisplayContent(display, this, this.mDeviceStateController);
        if (displayContent2.getWrapper().getExtImpl().isActivityPreloadDisplay(displayContent2)) {
            displayContent2.mDontMoveToTop = true;
        }
        addChild(displayContent2, Integer.MIN_VALUE);
        return displayContent2;
    }

    com.android.server.wm.ActivityRecord getDefaultDisplayHomeActivityForUser(int userId) {
        return getDefaultTaskDisplayArea().getHomeActivityForUser(userId);
    }

    boolean startHomeOnAllDisplays(int userId, java.lang.String reason) {
        boolean homeStarted = false;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            int displayId = ((com.android.server.wm.DisplayContent) getChildAt(i)).mDisplayId;
            homeStarted |= startHomeOnDisplay(userId, reason, displayId);
        }
        return homeStarted;
    }

    void startHomeOnEmptyDisplays(final java.lang.String reason) {
        forAllTaskDisplayAreas(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda29
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$startHomeOnEmptyDisplays$11(reason, (com.android.server.wm.TaskDisplayArea) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startHomeOnEmptyDisplays$11(java.lang.String reason, com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        if (taskDisplayArea.topRunningActivity() == null) {
            int userId = this.mWmService.getUserAssignedToDisplay(taskDisplayArea.getDisplayId());
            startHomeOnTaskDisplayArea(userId, reason, taskDisplayArea, false, false);
        }
    }

    boolean startHomeOnDisplay(int userId, java.lang.String reason, int displayId) {
        return startHomeOnDisplay(userId, reason, displayId, false, false);
    }

    boolean startHomeOnDisplay(final int userId, final java.lang.String reason, int displayId, final boolean allowInstrumenting, final boolean fromHomeKey) {
        if (displayId == -1) {
            com.android.server.wm.Task rootTask = getTopDisplayFocusedRootTask();
            displayId = rootTask != null ? rootTask.getDisplayId() : 0;
        }
        com.android.server.wm.DisplayContent display = getDisplayContent(displayId);
        if (display == null) {
            android.util.Slog.d(TAG, "startHomeOnDisplay display null return");
            return false;
        }
        return ((java.lang.Boolean) display.reduceOnAllTaskDisplayAreas(new java.util.function.BiFunction() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda48
            @Override // java.util.function.BiFunction
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                return this.f$0.lambda$startHomeOnDisplay$12(userId, reason, allowInstrumenting, fromHomeKey, (com.android.server.wm.TaskDisplayArea) obj, (java.lang.Boolean) obj2);
            }
        }, false)).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$startHomeOnDisplay$12(int userId, java.lang.String reason, boolean allowInstrumenting, boolean fromHomeKey, com.android.server.wm.TaskDisplayArea taskDisplayArea, java.lang.Boolean result) {
        return java.lang.Boolean.valueOf(result.booleanValue() | startHomeOnTaskDisplayArea(userId, reason, taskDisplayArea, allowInstrumenting, fromHomeKey));
    }

    boolean startHomeOnTaskDisplayArea(int userId, java.lang.String reason, com.android.server.wm.TaskDisplayArea taskDisplayArea, boolean allowInstrumenting, boolean fromHomeKey) {
        if (taskDisplayArea == null) {
            com.android.server.wm.Task rootTask = getTopDisplayFocusedRootTask();
            taskDisplayArea = rootTask != null ? rootTask.getDisplayArea() : getDefaultTaskDisplayArea();
        }
        android.content.Intent homeIntent = null;
        android.content.pm.ActivityInfo aInfo = null;
        if (taskDisplayArea == getDefaultTaskDisplayArea() || this.mWmService.shouldPlacePrimaryHomeOnDisplay(taskDisplayArea.getDisplayId(), userId)) {
            homeIntent = this.mService.getHomeIntent();
            aInfo = resolveHomeActivity(userId, homeIntent);
        } else if (shouldPlaceSecondaryHomeOnDisplayArea(taskDisplayArea)) {
            android.util.Pair<android.content.pm.ActivityInfo, android.content.Intent> info = resolveSecondaryHomeActivity(userId, taskDisplayArea);
            aInfo = (android.content.pm.ActivityInfo) info.first;
            homeIntent = (android.content.Intent) info.second;
        }
        if (aInfo == null || homeIntent == null) {
            return false;
        }
        android.content.pm.ActivityInfo aInfo2 = this.mRootWindowContainerExt.switchDefaultLauncherForBootAware(this.mService.mContext, aInfo, userId, homeIntent);
        if (!canStartHomeOnDisplayArea(aInfo2, taskDisplayArea, allowInstrumenting)) {
            return false;
        }
        if (this.mService.mAmInternal.shouldDelayHomeLaunch(userId)) {
            android.util.Slog.d(TAG, "ThemeHomeDelay: Home launch was deferred with user " + userId);
            return false;
        }
        homeIntent.setComponent(new android.content.ComponentName(aInfo2.applicationInfo.packageName, aInfo2.name));
        homeIntent.setFlags(homeIntent.getFlags() | 268435456);
        if (fromHomeKey) {
            homeIntent.putExtra("android.intent.extra.FROM_HOME_KEY", true);
            if (this.mWindowManager.getRecentsAnimationController() != null) {
                this.mWindowManager.getRecentsAnimationController().cancelAnimationForHomeStart();
            }
        }
        homeIntent.putExtra("android.intent.extra.EXTRA_START_REASON", reason);
        this.mRootWindowContainerExt.putExtraIfNeededForDisplayingNewFeatures(reason, homeIntent, userId);
        java.lang.String myReason = reason + ":" + userId + ":" + android.os.UserHandle.getUserId(aInfo2.applicationInfo.uid) + ":" + taskDisplayArea.getDisplayId();
        this.mService.getActivityStartController().startHomeActivity(homeIntent, aInfo2, myReason, taskDisplayArea);
        return true;
    }

    android.content.pm.ActivityInfo resolveHomeActivity(int userId, android.content.Intent homeIntent) {
        android.content.ComponentName comp = homeIntent.getComponent();
        android.content.pm.ActivityInfo aInfo = null;
        try {
            if (comp != null) {
                aInfo = android.app.AppGlobals.getPackageManager().getActivityInfo(comp, 1024L, userId);
            } else {
                java.lang.String resolvedType = homeIntent.resolveTypeIfNeeded(this.mService.mContext.getContentResolver());
                android.content.pm.ResolveInfo info = this.mTaskSupervisor.resolveIntent(homeIntent, resolvedType, userId, 1024, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid());
                if (info != null) {
                    aInfo = info.activityInfo;
                }
            }
        } catch (android.os.RemoteException e) {
        }
        if (aInfo == null) {
            com.android.server.utils.Slogf.wtf(TAG, new java.lang.Exception(), "No home screen found for %s and user %d", homeIntent, java.lang.Integer.valueOf(userId));
            return null;
        }
        android.content.pm.ActivityInfo aInfo2 = new android.content.pm.ActivityInfo(aInfo);
        aInfo2.applicationInfo = this.mService.getAppInfoForUser(aInfo2.applicationInfo, userId);
        return aInfo2;
    }

    android.util.Pair<android.content.pm.ActivityInfo, android.content.Intent> resolveSecondaryHomeActivity(int userId, com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        android.content.ComponentName customHomeComponent;
        if (taskDisplayArea == getDefaultTaskDisplayArea()) {
            throw new java.lang.IllegalArgumentException("resolveSecondaryHomeActivity: Should not be default task container");
        }
        android.content.Intent homeIntent = this.mService.getHomeIntent();
        android.content.pm.ActivityInfo aInfo = resolveHomeActivity(userId, homeIntent);
        boolean lookForSecondaryHomeActivityInPrimaryHomePackage = aInfo != null;
        if (android.companion.virtual.flags.Flags.vdmCustomHome()) {
            if (taskDisplayArea.getDisplayContent() != null) {
                customHomeComponent = taskDisplayArea.getDisplayContent().getCustomHomeComponent();
            } else {
                customHomeComponent = null;
            }
            if (customHomeComponent != null) {
                homeIntent.setComponent(customHomeComponent);
                android.content.pm.ActivityInfo customHomeActivityInfo = resolveHomeActivity(userId, homeIntent);
                if (customHomeActivityInfo != null) {
                    aInfo = customHomeActivityInfo;
                    lookForSecondaryHomeActivityInPrimaryHomePackage = false;
                }
            }
        }
        if (lookForSecondaryHomeActivityInPrimaryHomePackage) {
            if (com.android.internal.app.ResolverActivity.class.getName().equals(aInfo.name)) {
                aInfo = null;
            } else {
                homeIntent = this.mService.getSecondaryHomeIntent(aInfo.applicationInfo.packageName);
                java.util.List<android.content.pm.ResolveInfo> resolutions = resolveActivities(userId, homeIntent);
                int size = resolutions.size();
                java.lang.String targetName = aInfo.name;
                aInfo = null;
                int i = 0;
                while (true) {
                    if (i >= size) {
                        break;
                    }
                    android.content.pm.ResolveInfo resolveInfo = resolutions.get(i);
                    if (!resolveInfo.activityInfo.name.equals(targetName)) {
                        i++;
                    } else {
                        aInfo = resolveInfo.activityInfo;
                        break;
                    }
                }
                if (aInfo == null && size > 0) {
                    aInfo = resolutions.get(0).activityInfo;
                }
            }
        }
        if (aInfo != null && !canStartHomeOnDisplayArea(aInfo, taskDisplayArea, false)) {
            aInfo = null;
        }
        if (aInfo == null) {
            homeIntent = this.mService.getSecondaryHomeIntent(null);
            aInfo = resolveHomeActivity(userId, homeIntent);
        }
        return android.util.Pair.create(aInfo, homeIntent);
    }

    java.util.List<android.content.pm.ResolveInfo> resolveActivities(int userId, android.content.Intent homeIntent) {
        try {
            java.lang.String resolvedType = homeIntent.resolveTypeIfNeeded(this.mService.mContext.getContentResolver());
            java.util.List<android.content.pm.ResolveInfo> resolutions = android.app.AppGlobals.getPackageManager().queryIntentActivities(homeIntent, resolvedType, 1024L, userId).getList();
            return resolutions;
        } catch (android.os.RemoteException e) {
            java.util.List<android.content.pm.ResolveInfo> resolutions2 = new java.util.ArrayList<>();
            return resolutions2;
        }
    }

    boolean resumeHomeActivity(com.android.server.wm.ActivityRecord prev, java.lang.String reason, com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        if (!this.mService.isBooting() && !this.mService.isBooted()) {
            return false;
        }
        if (taskDisplayArea == null) {
            taskDisplayArea = getDefaultTaskDisplayArea();
        }
        com.android.server.wm.ActivityRecord r = taskDisplayArea.getHomeActivity();
        java.lang.String myReason = reason + " resumeHomeActivity";
        if (r != null && !r.finishing) {
            r.moveFocusableActivityToTop(myReason);
            return resumeFocusedTasksTopActivities(r.getRootTask(), prev, null);
        }
        int userId = this.mWmService.getUserAssignedToDisplay(taskDisplayArea.getDisplayId());
        return startHomeOnTaskDisplayArea(userId, myReason, taskDisplayArea, false, false);
    }

    boolean shouldPlacePrimaryHomeOnDisplay(int displayId) {
        return displayId == 0 || (displayId != -1 && (displayId == this.mService.mVr2dDisplayId || this.mWmService.shouldPlacePrimaryHomeOnDisplay(displayId)));
    }

    boolean shouldPlaceSecondaryHomeOnDisplayArea(com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        boolean deviceProvisioned;
        com.android.server.wm.DisplayContent display;
        android.view.DisplayInfo info;
        if (getDefaultTaskDisplayArea() == taskDisplayArea) {
            throw new java.lang.IllegalArgumentException("shouldPlaceSecondaryHomeOnDisplay: Should not be on default task container");
        }
        if (taskDisplayArea == null) {
            return false;
        }
        boolean useSystemProvidedLauncher = this.mService.mContext.getResources().getBoolean(android.R.bool.config_supportsSystemDecorsOnSecondaryDisplays);
        com.android.server.wm.DisplayContent dc = taskDisplayArea.getDisplayContent();
        if (dc != null && useSystemProvidedLauncher && (info = dc.getDisplayInfo()) != null && info.type == 1) {
            return true;
        }
        if (!taskDisplayArea.canHostHomeTask()) {
            return false;
        }
        if (taskDisplayArea.getDisplayId() == 0 || this.mService.mSupportsMultiDisplay) {
            if (android.provider.Settings.Global.getInt(this.mService.mContext.getContentResolver(), "device_provisioned", 0) == 0) {
                deviceProvisioned = false;
            } else {
                deviceProvisioned = true;
            }
            if (!deviceProvisioned || !android.os.storage.StorageManager.isCeStorageUnlocked(this.mCurrentUser) || (display = taskDisplayArea.getDisplayContent()) == null || display.isRemoved() || !display.isHomeSupported()) {
                return false;
            }
            return true;
        }
        return false;
    }

    boolean canStartHomeOnDisplayArea(android.content.pm.ActivityInfo homeInfo, com.android.server.wm.TaskDisplayArea taskDisplayArea, boolean allowInstrumenting) {
        if (this.mService.mFactoryTest == 1 && this.mService.mTopAction == null) {
            return false;
        }
        com.android.server.wm.WindowProcessController app = this.mService.getProcessController(homeInfo.processName, homeInfo.applicationInfo.uid);
        if (!allowInstrumenting && app != null && app.isInstrumenting()) {
            return false;
        }
        if (taskDisplayArea != null && !taskDisplayArea.canHostHomeTask()) {
            return false;
        }
        int displayId = taskDisplayArea != null ? taskDisplayArea.getDisplayId() : -1;
        if (shouldPlacePrimaryHomeOnDisplay(displayId)) {
            return true;
        }
        if (!shouldPlaceSecondaryHomeOnDisplayArea(taskDisplayArea)) {
            return false;
        }
        boolean supportMultipleInstance = (homeInfo.launchMode == 2 || homeInfo.launchMode == 3) ? false : true;
        return supportMultipleInstance;
    }

    void ensureVisibilityAndConfig(com.android.server.wm.ActivityRecord starting, com.android.server.wm.DisplayContent displayContent, boolean deferResume) {
        ensureActivitiesVisible(null, false);
        android.content.res.Configuration config = displayContent.updateOrientation(starting, true);
        if (starting != null) {
            starting.reportDescendantOrientationChangeIfNeeded();
        }
        displayContent.updateDisplayOverrideConfigurationLocked(config, starting, deferResume);
    }

    java.util.List<com.android.server.wm.ActivityAssistInfo> getTopVisibleActivities() {
        final java.util.ArrayList<com.android.server.wm.ActivityAssistInfo> topVisibleActivities = new java.util.ArrayList<>();
        final java.util.ArrayList<com.android.server.wm.ActivityAssistInfo> activityAssistInfos = new java.util.ArrayList<>();
        final com.android.server.wm.Task topFocusedRootTask = getTopDisplayFocusedRootTask();
        forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.RootWindowContainer.lambda$getTopVisibleActivities$13(activityAssistInfos, topFocusedRootTask, topVisibleActivities, (com.android.server.wm.Task) obj);
            }
        });
        return topVisibleActivities;
    }

    static /* synthetic */ void lambda$getTopVisibleActivities$13(java.util.ArrayList activityAssistInfos, com.android.server.wm.Task topFocusedRootTask, java.util.ArrayList topVisibleActivities, com.android.server.wm.Task rootTask) {
        com.android.server.wm.ActivityRecord top;
        com.android.server.wm.ActivityRecord adjacentActivityRecord;
        if (rootTask.shouldBeVisible(null) && (top = rootTask.getTopNonFinishingActivity()) != null) {
            activityAssistInfos.clear();
            activityAssistInfos.add(new com.android.server.wm.ActivityAssistInfo(top));
            com.android.server.wm.Task adjacentTask = top.getTask().getAdjacentTask();
            if (adjacentTask != null && (adjacentActivityRecord = adjacentTask.getTopNonFinishingActivity()) != null) {
                activityAssistInfos.add(new com.android.server.wm.ActivityAssistInfo(adjacentActivityRecord));
            }
            if (rootTask == topFocusedRootTask) {
                topVisibleActivities.addAll(0, activityAssistInfos);
            } else {
                topVisibleActivities.addAll(activityAssistInfos);
            }
        }
    }

    public com.android.server.wm.Task getTopDisplayFocusedRootTask() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.Task focusedRootTask = ((com.android.server.wm.DisplayContent) getChildAt(i)).getFocusedRootTask();
            if (focusedRootTask != null) {
                return focusedRootTask;
            }
        }
        return null;
    }

    com.android.server.wm.ActivityRecord getTopResumedActivity() {
        com.android.server.wm.Task focusedRootTask = getTopDisplayFocusedRootTask();
        if (focusedRootTask == null) {
            return null;
        }
        com.android.server.wm.ActivityRecord resumedActivity = focusedRootTask.getTopResumedActivity();
        if (resumedActivity != null && resumedActivity.app != null) {
            return resumedActivity;
        }
        return (com.android.server.wm.ActivityRecord) getItemFromTaskDisplayAreas(new java.util.function.Function() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda26
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.wm.TaskDisplayArea) obj).getFocusedActivity();
            }
        });
    }

    boolean isTopDisplayFocusedRootTask(com.android.server.wm.Task task) {
        return task != null && task == getTopDisplayFocusedRootTask();
    }

    boolean attachApplication(com.android.server.wm.WindowProcessController app) throws android.os.RemoteException {
        java.util.ArrayList<com.android.server.wm.ActivityRecord> activities = this.mService.mStartingProcessActivities;
        android.os.RemoteException remoteException = null;
        boolean hasActivityStarted = false;
        for (int i = activities.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord r = activities.get(i);
            if (app.mUid != r.info.applicationInfo.uid || !app.mName.equals(r.processName)) {
                if (android.view.DynamicLoggerObserver.isLogToolRun()) {
                    android.util.Slog.d(TAG, "attachApplication: continue for app name or uid, r = " + r);
                }
            } else {
                activities.remove(i);
                com.android.server.wm.TaskFragment tf = r.getTaskFragment();
                if (tf == null || r.finishing || r.app != null || !r.shouldBeVisible(true) || !r.showToCurrentUser()) {
                    if (android.view.DynamicLoggerObserver.isLogToolRun()) {
                        android.util.Slog.d(TAG, "attachApplication: continue for activity states, r = " + r);
                    }
                } else {
                    try {
                        boolean canResume = r.isFocusable() && r == tf.topRunningActivity();
                        r.getWrapper().getExtImpl().setNotifyHotStart(false);
                        if (this.mTaskSupervisor.realStartActivityLocked(r, app, canResume, true)) {
                            hasActivityStarted = true;
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(TAG, "Exception in new process when starting " + r, e);
                        remoteException = e;
                    }
                }
            }
        }
        if (remoteException != null) {
            throw remoteException;
        }
        return hasActivityStarted;
    }

    void ensureActivitiesVisible() {
        ensureActivitiesVisible(null);
    }

    void ensureActivitiesVisible(com.android.server.wm.ActivityRecord starting) {
        ensureActivitiesVisible(starting, true);
    }

    void ensureActivitiesVisible(com.android.server.wm.ActivityRecord starting, boolean notifyClients) {
        if (this.mTaskSupervisor.inActivityVisibilityUpdate() || this.mTaskSupervisor.isRootVisibilityUpdateDeferred()) {
            return;
        }
        this.mTaskSupervisor.beginActivityVisibilityUpdate();
        try {
            for (int displayNdx = getChildCount() - 1; displayNdx >= 0; displayNdx--) {
                com.android.server.wm.DisplayContent display = (com.android.server.wm.DisplayContent) getChildAt(displayNdx);
                display.ensureActivitiesVisible(starting, notifyClients);
            }
        } finally {
            this.mTaskSupervisor.endActivityVisibilityUpdate();
        }
    }

    boolean switchUser(final int userId, com.android.server.am.UserState uss) {
        com.android.server.wm.Task topFocusedRootTask = getTopDisplayFocusedRootTask();
        int focusRootTaskId = topFocusedRootTask != null ? topFocusedRootTask.getRootTaskId() : -1;
        removeRootTasksInWindowingModes(2);
        this.mUserRootTaskInFront.put(this.mCurrentUser, focusRootTaskId);
        this.mCurrentUser = userId;
        this.mTaskSupervisor.mStartingUsers.add(uss);
        forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.Task) obj).switchUser(userId);
            }
        });
        if (topFocusedRootTask != null && isAlwaysVisibleUser(topFocusedRootTask.mUserId)) {
            android.util.Slog.i(TAG, "Persisting top task because it belongs to an always-visible user");
            this.mUserRootTaskInFront.put(this.mCurrentUser, focusRootTaskId);
        }
        int restoreRootTaskId = this.mUserRootTaskInFront.get(userId);
        com.android.server.wm.Task rootTask = getRootTask(restoreRootTaskId);
        if (rootTask == null) {
            rootTask = getDefaultTaskDisplayArea().getOrCreateRootHomeTask();
        }
        boolean homeInFront = rootTask.isActivityTypeHome();
        if (rootTask.isOnHomeDisplay()) {
            rootTask.moveToFront("switchUserOnHomeDisplay");
        } else {
            resumeHomeActivity(null, "switchUserOnOtherDisplay", getDefaultTaskDisplayArea());
        }
        return homeInFront;
    }

    private boolean isAlwaysVisibleUser(int userId) {
        com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        android.content.pm.UserProperties properties = umi.getUserProperties(userId);
        return properties != null && properties.getAlwaysVisible();
    }

    void removeUser(int userId) {
        this.mUserRootTaskInFront.delete(userId);
    }

    void updateUserRootTask(int userId, com.android.server.wm.Task rootTask) {
        if (userId != this.mCurrentUser) {
            if (rootTask == null) {
                rootTask = getDefaultTaskDisplayArea().getOrCreateRootHomeTask();
            }
            this.mUserRootTaskInFront.put(userId, rootTask.getRootTaskId());
        }
    }

    void moveRootTaskToTaskDisplayArea(int rootTaskId, com.android.server.wm.TaskDisplayArea taskDisplayArea, boolean onTop) {
        com.android.server.wm.Task rootTask = getRootTask(rootTaskId);
        if (rootTask == null) {
            throw new java.lang.IllegalArgumentException("moveRootTaskToTaskDisplayArea: Unknown rootTaskId=" + rootTaskId);
        }
        com.android.server.wm.TaskDisplayArea currentTaskDisplayArea = rootTask.getDisplayArea();
        if (currentTaskDisplayArea == null) {
            throw new java.lang.IllegalStateException("moveRootTaskToTaskDisplayArea: rootTask=" + rootTask + " is not attached to any task display area.");
        }
        if (taskDisplayArea == null) {
            throw new java.lang.IllegalArgumentException("moveRootTaskToTaskDisplayArea: Unknown taskDisplayArea=" + taskDisplayArea);
        }
        if (currentTaskDisplayArea == taskDisplayArea) {
            throw new java.lang.IllegalArgumentException("Trying to move rootTask=" + rootTask + " to its current taskDisplayArea=" + taskDisplayArea);
        }
        rootTask.reparent(taskDisplayArea, onTop);
        rootTask.resumeNextFocusAfterReparent();
    }

    void moveRootTaskToDisplay(int rootTaskId, int displayId, boolean onTop) {
        com.android.server.wm.DisplayContent displayContent = getDisplayContentOrCreate(displayId);
        if (displayContent == null) {
            throw new java.lang.IllegalArgumentException("moveRootTaskToDisplay: Unknown displayId=" + displayId);
        }
        moveRootTaskToTaskDisplayArea(rootTaskId, displayContent.getDefaultTaskDisplayArea(), onTop);
    }

    void moveActivityToPinnedRootTask(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord launchIntoPipHostActivity, java.lang.String reason) throws java.lang.Throwable {
        moveActivityToPinnedRootTask(r, launchIntoPipHostActivity, reason, null);
    }

    void moveActivityToPinnedRootTask(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord launchIntoPipHostActivity, java.lang.String reason, com.android.server.wm.Transition transition) throws java.lang.Throwable {
        moveActivityToPinnedRootTask(r, launchIntoPipHostActivity, reason, transition, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x0237  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0257  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x026b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:130:0x021b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:138:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x007a A[Catch: all -> 0x0063, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0063, blocks: (B:18:0x0058, B:24:0x007a, B:31:0x0096, B:33:0x009f, B:35:0x00a7, B:39:0x0104, B:47:0x0136, B:49:0x013d, B:51:0x0143, B:54:0x014b, B:60:0x0159, B:43:0x0123), top: B:127:0x0058 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b8 A[Catch: all -> 0x025f, TRY_ENTER, TRY_LEAVE, TryCatch #5 {all -> 0x025f, blocks: (B:15:0x0051, B:22:0x006a, B:25:0x0081, B:37:0x00b8, B:52:0x0145, B:57:0x0152, B:62:0x015d, B:56:0x014f, B:41:0x0110, B:45:0x0131), top: B:136:0x0051 }] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01b0 A[Catch: all -> 0x025b, TryCatch #3 {all -> 0x025b, blocks: (B:75:0x01a2, B:77:0x01b0, B:78:0x01bd, B:81:0x01ce, B:82:0x01d1, B:84:0x01d7, B:86:0x01e1, B:87:0x01f1, B:89:0x01fb, B:90:0x01fe, B:92:0x0203, B:94:0x0207, B:96:0x020d, B:70:0x0188, B:72:0x018e), top: B:132:0x0188 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01fb A[Catch: all -> 0x025b, TryCatch #3 {all -> 0x025b, blocks: (B:75:0x01a2, B:77:0x01b0, B:78:0x01bd, B:81:0x01ce, B:82:0x01d1, B:84:0x01d7, B:86:0x01e1, B:87:0x01f1, B:89:0x01fb, B:90:0x01fe, B:92:0x0203, B:94:0x0207, B:96:0x020d, B:70:0x0188, B:72:0x018e), top: B:132:0x0188 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void moveActivityToPinnedRootTask(com.android.server.wm.ActivityRecord r20, com.android.server.wm.ActivityRecord r21, java.lang.String r22, com.android.server.wm.Transition r23, android.graphics.Rect r24) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 645
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.RootWindowContainer.moveActivityToPinnedRootTask(com.android.server.wm.ActivityRecord, com.android.server.wm.ActivityRecord, java.lang.String, com.android.server.wm.Transition, android.graphics.Rect):void");
    }

    static /* synthetic */ void lambda$moveActivityToPinnedRootTask$15(com.android.server.wm.TaskFragment tf) {
        if (!tf.isOrganizedTaskFragment()) {
            return;
        }
        tf.resetAdjacentTaskFragment();
        tf.setCompanionTaskFragment(null);
        tf.setAnimationParams(android.window.TaskFragmentAnimationParams.DEFAULT);
        if (tf.getTopNonFinishingActivity() != null) {
            tf.setRelativeEmbeddedBounds(new android.graphics.Rect());
            tf.updateRequestedOverrideConfiguration(android.content.res.Configuration.EMPTY);
        }
    }

    private void scheduleTimeoutAbortPipEnter(com.android.server.wm.Task rootTask) {
        if (this.mMaybeAbortPipEnterRunnable != null) {
            this.mHandler.removeCallbacks(this.mMaybeAbortPipEnterRunnable);
            this.mMaybeAbortPipEnterRunnable.run();
        }
        java.lang.Throwable enterPipThrowable = new java.lang.Throwable();
        this.mMaybeAbortPipEnterRunnable = new com.android.server.wm.RootWindowContainer.AnonymousClass2(rootTask, enterPipThrowable);
        this.mHandler.postDelayed(this.mMaybeAbortPipEnterRunnable, 1000L);
        android.util.Slog.d(TAG, "a delayed check for potentially aborting PiP if in a wrong state is scheduled.");
    }

    /* JADX INFO: renamed from: com.android.server.wm.RootWindowContainer$2, reason: invalid class name */
    class AnonymousClass2 implements java.lang.Runnable {
        final /* synthetic */ java.lang.Throwable val$enterPipThrowable;
        final /* synthetic */ com.android.server.wm.Task val$rootTask;

        AnonymousClass2(com.android.server.wm.Task task, java.lang.Throwable th) {
            this.val$rootTask = task;
            this.val$enterPipThrowable = th;
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.RootWindowContainer.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.RootWindowContainer.this.mTransitionController.inTransition()) {
                        final java.lang.Runnable expectedMaybeAbortAtTimeout = com.android.server.wm.RootWindowContainer.this.mMaybeAbortPipEnterRunnable;
                        com.android.server.wm.RootWindowContainer.this.mTransitionController.mStateValidators.add(new java.lang.Runnable() { // from class: com.android.server.wm.RootWindowContainer$2$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$run$0(expectedMaybeAbortAtTimeout);
                            }
                        });
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.RootWindowContainer.this.mMaybeAbortPipEnterRunnable = null;
                    com.android.server.wm.RootWindowContainer.this.mService.deferWindowLayout();
                    com.android.server.wm.ActivityRecord top = this.val$rootTask.getTopMostActivity();
                    android.app.ActivityManager.RunningTaskInfo beforeTaskInfo = this.val$rootTask.getTaskInfo();
                    if (top != null && !top.inPinnedWindowingMode() && this.val$rootTask.abortPipEnter(top)) {
                        android.util.Slog.wtf(com.android.server.wm.RootWindowContainer.TAG, "Enter PiP was aborted via a scheduled timeouttask_state_before=" + beforeTaskInfo + "task_state_after=" + this.val$rootTask.getTaskInfo(), this.val$enterPipThrowable);
                    }
                    com.android.server.wm.RootWindowContainer.this.mService.continueWindowLayout();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(java.lang.Runnable expectedMaybeAbortAtTimeout) {
            if (expectedMaybeAbortAtTimeout != com.android.server.wm.RootWindowContainer.this.mMaybeAbortPipEnterRunnable) {
                return;
            }
            com.android.server.wm.RootWindowContainer.this.mMaybeAbortPipEnterRunnable = null;
            run();
        }
    }

    void notifyActivityPipModeChanged(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord r) {
        boolean inPip = r != null;
        if (inPip) {
            this.mService.getTaskChangeNotificationController().notifyActivityPinned(r);
        } else {
            this.mService.getTaskChangeNotificationController().notifyActivityUnpinned();
        }
        this.mWindowManager.mPolicy.setPipVisibilityLw(inPip);
        if (task.getSurfaceControl() != null) {
            this.mWmService.mTransactionFactory.get().setTrustedOverlay(task.getSurfaceControl(), inPip).apply();
        }
    }

    void executeAppTransitionForAllDisplay() {
        for (int displayNdx = getChildCount() - 1; displayNdx >= 0; displayNdx--) {
            com.android.server.wm.DisplayContent display = (com.android.server.wm.DisplayContent) getChildAt(displayNdx);
            display.mDisplayContent.executeAppTransition();
        }
    }

    com.android.server.wm.ActivityRecord findTask(com.android.server.wm.ActivityRecord r, com.android.server.wm.TaskDisplayArea preferredTaskDisplayArea, boolean includeLaunchedFromBubble) {
        return findTask(r.getActivityType(), r.taskAffinity, r.intent, r.info, preferredTaskDisplayArea, includeLaunchedFromBubble);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00ca A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00cb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    com.android.server.wm.ActivityRecord findTask(int r23, final java.lang.String r24, final android.content.Intent r25, final android.content.pm.ActivityInfo r26, final com.android.server.wm.TaskDisplayArea r27, boolean r28) {
        /*
            Method dump skipped, instruction units count: 238
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.RootWindowContainer.findTask(int, java.lang.String, android.content.Intent, android.content.pm.ActivityInfo, com.android.server.wm.TaskDisplayArea, boolean):com.android.server.wm.ActivityRecord");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.wm.ActivityRecord lambda$findTask$16(com.android.server.wm.TaskDisplayArea preferredTaskDisplayArea, java.lang.String taskAffinity, android.content.Intent intent, android.content.pm.ActivityInfo info, com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        if (taskDisplayArea == preferredTaskDisplayArea) {
            return null;
        }
        this.mTmpFindTaskResult.process(taskDisplayArea);
        if (this.mTmpFindTaskResult.mIdealRecord != null) {
            return this.mTmpFindTaskResult.mIdealRecord;
        }
        if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE && this.mRootWindowContainerExt.findTaskOnlyForLaunch(this.mService, this.mTmpFindTaskResult, taskAffinity, intent)) {
            return this.mTmpFindTaskResult.mCandidateRecord;
        }
        if (this.mTmpFindTaskResult.mCandidateRecord != null && this.mTmpFindTaskResult.mCandidateRecord.getRootTask() != null && (this.mTmpFindTaskResult.mCandidateRecord.getRootTask().getWrapper().getExtImpl().isPuttTask() || (preferredTaskDisplayArea != null && preferredTaskDisplayArea.mDisplayContent != null && preferredTaskDisplayArea.mDisplayContent.getWrapper().getNonStaticExtImpl().isPuttDisplay()))) {
            android.util.Slog.w(TAG, "putt: task exchange ,candidateR:" + this.mTmpFindTaskResult.mCandidateRecord + " for info:" + info);
            return this.mTmpFindTaskResult.mCandidateRecord;
        }
        if (this.mTmpFindTaskResult.mCandidateRecord == null || (!((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageDisplay(this.mTmpFindTaskResult.mCandidateRecord.getDisplayId()) && (preferredTaskDisplayArea == null || !((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageDisplay(preferredTaskDisplayArea.getDisplayId())))) {
            return null;
        }
        android.util.Slog.d(TAG, "Return candidate record for mirage mode");
        return this.mTmpFindTaskResult.mCandidateRecord;
    }

    com.android.server.wm.Task finishTopCrashedActivities(final com.android.server.wm.WindowProcessController app, final java.lang.String reason) {
        final com.android.server.wm.Task focusedRootTask = getTopDisplayFocusedRootTask();
        final com.android.server.wm.Task[] finishedTask = new com.android.server.wm.Task[1];
        forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.RootWindowContainer.lambda$finishTopCrashedActivities$17(finishedTask, focusedRootTask, app, reason, (com.android.server.wm.Task) obj);
            }
        }, true);
        return finishedTask[0];
    }

    static /* synthetic */ void lambda$finishTopCrashedActivities$17(com.android.server.wm.Task[] finishedTask, com.android.server.wm.Task focusedRootTask, com.android.server.wm.WindowProcessController app, java.lang.String reason, com.android.server.wm.Task leafTask) {
        boolean recordTopOrVisible = finishedTask[0] == null && (focusedRootTask == leafTask.getRootTask() || leafTask.isVisibleRequested());
        com.android.server.wm.Task t = leafTask.finishTopCrashedActivityLocked(app, reason);
        if (recordTopOrVisible) {
            finishedTask[0] = t;
        }
    }

    void ensureVisibilityOnVisibleActivityDiedOrCrashed(java.lang.String reason) {
        com.android.server.wm.Task topTask = getTopDisplayFocusedRootTask();
        if (topTask != null && topTask.topRunningActivity(true) == null) {
            topTask.adjustFocusToNextFocusableTask(reason);
        }
        if (!resumeFocusedTasksTopActivities()) {
            ensureActivitiesVisible();
        }
    }

    boolean resumeFocusedTasksTopActivities() {
        return resumeFocusedTasksTopActivities(null, null, null);
    }

    boolean resumeFocusedTasksTopActivities(com.android.server.wm.Task targetRootTask, com.android.server.wm.ActivityRecord target, android.app.ActivityOptions targetOptions) {
        return resumeFocusedTasksTopActivities(targetRootTask, target, targetOptions, false);
    }

    boolean resumeFocusedTasksTopActivities(final com.android.server.wm.Task targetRootTask, final com.android.server.wm.ActivityRecord target, final android.app.ActivityOptions targetOptions, boolean deferPause) {
        int displayNdx;
        boolean[] resumedOnDisplay;
        if (!this.mTaskSupervisor.readyToResume()) {
            return false;
        }
        boolean result = false;
        boolean[] zArr = null;
        if (targetRootTask != null && ((targetRootTask.isTopRootTaskInDisplayArea() || getTopDisplayFocusedRootTask() == targetRootTask) && getWCWrapper().getExtImpl().shouldResumeTaskTopActivity(targetRootTask, null))) {
            result = targetRootTask.resumeTopActivityUncheckedLocked(target, targetOptions, deferPause);
        }
        int i = 1;
        boolean result2 = result;
        int displayNdx2 = getChildCount() - 1;
        while (displayNdx2 >= 0) {
            com.android.server.wm.DisplayContent display = (com.android.server.wm.DisplayContent) getChildAt(displayNdx2);
            final boolean curResult = result2;
            final boolean[] resumedOnDisplay2 = new boolean[i];
            final com.android.server.wm.ActivityRecord topOfDisplay = display.topRunningActivity();
            if (this.mRootWindowContainerExt.resumeFocusedSkipped(display, targetRootTask, target)) {
                displayNdx = displayNdx2;
                resumedOnDisplay = zArr;
            } else {
                displayNdx = displayNdx2;
                display.forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda37
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$resumeFocusedTasksTopActivities$18(targetRootTask, resumedOnDisplay2, curResult, topOfDisplay, targetOptions, target, (com.android.server.wm.Task) obj);
                    }
                });
                boolean result3 = resumedOnDisplay2[0] | result2;
                if (resumedOnDisplay2[0]) {
                    resumedOnDisplay = null;
                } else {
                    com.android.server.wm.Task focusedRoot = display.getFocusedRootTask();
                    if (focusedRoot != null && !this.mRootWindowContainerExt.resumeSecondHomeIfNeed(display, focusedRoot, targetRootTask)) {
                        if (getWCWrapper().getExtImpl().shouldResumeTaskTopActivity(focusedRoot, null)) {
                            result2 = result3 | focusedRoot.resumeTopActivityUncheckedLocked(target, targetOptions);
                            resumedOnDisplay = null;
                        } else if (focusedRoot.getDisplayContent() == null) {
                            resumedOnDisplay = null;
                        } else {
                            focusedRoot.executeAppTransition(targetOptions);
                            resumedOnDisplay = null;
                        }
                    } else if (targetRootTask == null) {
                        resumedOnDisplay = null;
                        result2 = result3 | resumeHomeActivity(null, "no-focusable-task", display.getDefaultTaskDisplayArea());
                    } else {
                        resumedOnDisplay = null;
                    }
                }
                result2 = result3;
            }
            displayNdx2 = displayNdx - 1;
            zArr = resumedOnDisplay;
            i = 1;
        }
        return result2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resumeFocusedTasksTopActivities$18(com.android.server.wm.Task targetRootTask, boolean[] resumedOnDisplay, boolean curResult, com.android.server.wm.ActivityRecord topOfDisplay, android.app.ActivityOptions targetOptions, com.android.server.wm.ActivityRecord target, com.android.server.wm.Task rootTask) {
        com.android.server.wm.ActivityRecord topRunningActivity = rootTask.topRunningActivity();
        if (!rootTask.isFocusableAndVisible() || topRunningActivity == null) {
            return;
        }
        if (rootTask == targetRootTask) {
            resumedOnDisplay[0] = resumedOnDisplay[0] | curResult;
            return;
        }
        if (android.app.WindowConfiguration.sExtImpl.isWindowingZoomMode(rootTask.getWindowingMode()) || rootTask.getWrapper().getExtImpl().isFlexibleTaskAndHasCaption(rootTask)) {
            return;
        }
        if (topRunningActivity.isState(com.android.server.wm.ActivityRecord.State.RESUMED) && topRunningActivity == topOfDisplay) {
            rootTask.executeAppTransition(targetOptions);
        } else if (!topRunningActivity.shouldResumeActivity(target) || getWCWrapper().getExtImpl().shouldResumeTaskTopActivity(rootTask, topRunningActivity)) {
            resumedOnDisplay[0] = resumedOnDisplay[0] | topRunningActivity.makeActiveIfNeeded(target);
        }
    }

    void sendSleepTransition(final com.android.server.wm.DisplayContent display) {
        final com.android.server.wm.Transition transition = new com.android.server.wm.Transition(12, 0, display.mTransitionController, this.mWmService.mSyncEngine);
        com.android.server.wm.TransitionController.OnStartCollect sendSleepTransition = new com.android.server.wm.TransitionController.OnStartCollect() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda27
            @Override // com.android.server.wm.TransitionController.OnStartCollect
            public final void onCollectStarted(boolean z) {
                com.android.server.wm.RootWindowContainer.lambda$sendSleepTransition$19(transition, display, z);
            }
        };
        if (!display.mTransitionController.isCollecting()) {
            if (this.mWindowManager.mSyncEngine.hasActiveSync()) {
                android.util.Slog.w(TAG, "Ongoing sync outside of a transition.");
            }
            display.mTransitionController.moveToCollecting(transition);
            sendSleepTransition.onCollectStarted(false);
            return;
        }
        display.mTransitionController.startCollectOrQueue(transition, sendSleepTransition);
    }

    static /* synthetic */ void lambda$sendSleepTransition$19(com.android.server.wm.Transition transition, com.android.server.wm.DisplayContent display, boolean deferred) {
        if (!transition.isCollecting()) {
            android.util.Slog.w(TAG, "sendSleepTransition, shouldSleep: " + display.shouldSleep() + ", sleep transition isn't in collecting, skip abort or request");
        } else if (deferred && !display.shouldSleep()) {
            transition.abort();
        } else {
            display.mTransitionController.requestStartTransition(transition, null, null, null);
            transition.playNow();
        }
    }

    void applySleepTokens(boolean applyToRootTasks) {
        boolean scheduledSleepTransition = false;
        int displayNdx = getChildCount();
        while (true) {
            displayNdx--;
            if (displayNdx < 0) {
                break;
            }
            final com.android.server.wm.DisplayContent display = (com.android.server.wm.DisplayContent) getChildAt(displayNdx);
            final boolean displayShouldSleep = display.shouldSleep();
            if (displayShouldSleep != display.isSleeping()) {
                boolean wasSleeping = display.isSleeping();
                display.setIsSleeping(displayShouldSleep);
                if (display.mTransitionController.isShellTransitionsEnabled() && !scheduledSleepTransition && !this.mRootWindowContainerExt.skipSleepTransition(display) && displayShouldSleep && !display.mAllSleepTokens.isEmpty()) {
                    scheduledSleepTransition = true;
                    if (!this.mHandler.hasMessages(3)) {
                        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(3, display), 1000L);
                    }
                }
                if (applyToRootTasks) {
                    if (!displayShouldSleep && !this.mRootWindowContainerExt.isSecondDisplay(display) && display.mTransitionController.isShellTransitionsEnabled() && !display.mTransitionController.isCollecting()) {
                        int transit = 0;
                        com.android.server.wm.Task startTask = null;
                        int flags = 0;
                        if (display.isKeyguardOccluded()) {
                            startTask = display.getTaskOccludingKeyguard();
                            flags = 4096;
                            transit = 8;
                        }
                        if (wasSleeping) {
                            transit = 11;
                        }
                        display.mTransitionController.requestStartTransition(display.mTransitionController.createTransition(transit, flags), startTask, null, null);
                    }
                    display.forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda11
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$applySleepTokens$21(displayShouldSleep, display, (com.android.server.wm.Task) obj);
                        }
                    });
                }
            }
        }
        if (!scheduledSleepTransition) {
            this.mHandler.removeMessages(3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applySleepTokens$21(boolean displayShouldSleep, com.android.server.wm.DisplayContent display, com.android.server.wm.Task rootTask) {
        if (displayShouldSleep) {
            rootTask.goToSleepIfPossible(false);
            return;
        }
        rootTask.forAllLeafTasksAndLeafTaskFragments(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda51
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.TaskFragment) obj).awakeFromSleeping();
            }
        }, true);
        if (rootTask.isFocusedRootTaskOnDisplay() && !this.mTaskSupervisor.getKeyguardController().isKeyguardOrAodShowing(display.mDisplayId)) {
            rootTask.resumeTopActivityUncheckedLocked(null, null);
        }
        rootTask.ensureActivitiesVisible(null);
    }

    protected com.android.server.wm.Task getRootTask(int rooTaskId) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.Task rootTask = ((com.android.server.wm.DisplayContent) getChildAt(i)).getRootTask(rooTaskId);
            if (rootTask != null) {
                return rootTask;
            }
        }
        return null;
    }

    com.android.server.wm.Task getRootTask(int windowingMode, int activityType) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.Task rootTask = ((com.android.server.wm.DisplayContent) getChildAt(i)).getRootTask(windowingMode, activityType);
            if (rootTask != null) {
                return rootTask;
            }
        }
        return null;
    }

    private com.android.server.wm.Task getRootTask(int windowingMode, int activityType, int displayId) {
        com.android.server.wm.DisplayContent display = getDisplayContent(displayId);
        if (display == null) {
            return null;
        }
        return display.getRootTask(windowingMode, activityType);
    }

    private android.app.ActivityTaskManager.RootTaskInfo getRootTaskInfo(final com.android.server.wm.Task task) {
        final android.app.ActivityTaskManager.RootTaskInfo info = new android.app.ActivityTaskManager.RootTaskInfo();
        task.fillTaskInfo(info);
        com.android.server.wm.DisplayContent displayContent = task.getDisplayContent();
        if (displayContent == null) {
            info.position = -1;
        } else {
            final int[] taskIndex = new int[1];
            final boolean[] hasFound = new boolean[1];
            displayContent.forAllRootTasks(new java.util.function.Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda20
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.RootWindowContainer.lambda$getRootTaskInfo$22(task, hasFound, taskIndex, (com.android.server.wm.Task) obj);
                }
            }, false);
            info.position = hasFound[0] ? taskIndex[0] : -1;
        }
        info.visible = task.shouldBeVisible(null);
        task.getBounds(info.bounds);
        int numTasks = task.getDescendantTaskCount();
        info.childTaskIds = new int[numTasks];
        info.childTaskNames = new java.lang.String[numTasks];
        info.childTaskBounds = new android.graphics.Rect[numTasks];
        info.childTaskUserIds = new int[numTasks];
        final int[] currentIndex = {0};
        task.forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda21
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.RootWindowContainer.lambda$getRootTaskInfo$23(currentIndex, info, (com.android.server.wm.Task) obj);
            }
        }, false);
        com.android.server.wm.ActivityRecord top = task.topRunningActivity();
        info.topActivity = top != null ? top.intent.getComponent() : null;
        return info;
    }

    static /* synthetic */ boolean lambda$getRootTaskInfo$22(com.android.server.wm.Task task, boolean[] hasFound, int[] taskIndex, com.android.server.wm.Task rootTask) {
        if (task != rootTask) {
            taskIndex[0] = taskIndex[0] + 1;
            return false;
        }
        hasFound[0] = true;
        return true;
    }

    static /* synthetic */ void lambda$getRootTaskInfo$23(int[] currentIndex, android.app.ActivityTaskManager.RootTaskInfo info, com.android.server.wm.Task t) {
        java.lang.String strFlattenToString;
        int i = currentIndex[0];
        info.childTaskIds[i] = t.mTaskId;
        java.lang.String[] strArr = info.childTaskNames;
        if (t.origActivity != null) {
            strFlattenToString = t.origActivity.flattenToString();
        } else if (t.realActivity != null) {
            strFlattenToString = t.realActivity.flattenToString();
        } else {
            strFlattenToString = t.getTopNonFinishingActivity() != null ? t.getTopNonFinishingActivity().packageName : "unknown";
        }
        strArr[i] = strFlattenToString;
        info.childTaskBounds[i] = t.mAtmService.getTaskBounds(t.mTaskId);
        info.childTaskUserIds[i] = t.mUserId;
        currentIndex[0] = i + 1;
    }

    android.app.ActivityTaskManager.RootTaskInfo getRootTaskInfo(int taskId) {
        com.android.server.wm.Task task = getRootTask(taskId);
        if (task != null) {
            return getRootTaskInfo(task);
        }
        return null;
    }

    android.app.ActivityTaskManager.RootTaskInfo getRootTaskInfo(int windowingMode, int activityType) {
        com.android.server.wm.Task rootTask = getRootTask(windowingMode, activityType);
        if (rootTask != null) {
            return getRootTaskInfo(rootTask);
        }
        return null;
    }

    android.app.ActivityTaskManager.RootTaskInfo getRootTaskInfo(int windowingMode, int activityType, int displayId) {
        com.android.server.wm.Task rootTask = getRootTask(windowingMode, activityType, displayId);
        if (rootTask != null) {
            return getRootTaskInfo(rootTask);
        }
        return null;
    }

    java.util.ArrayList<android.app.ActivityTaskManager.RootTaskInfo> getAllRootTaskInfos(int displayId) {
        final java.util.ArrayList<android.app.ActivityTaskManager.RootTaskInfo> list = new java.util.ArrayList<>();
        if (displayId == -1) {
            forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$getAllRootTaskInfos$24(list, (com.android.server.wm.Task) obj);
                }
            });
            return list;
        }
        com.android.server.wm.DisplayContent display = getDisplayContent(displayId);
        if (display == null) {
            return list;
        }
        display.forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$getAllRootTaskInfos$25(list, (com.android.server.wm.Task) obj);
            }
        });
        return list;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getAllRootTaskInfos$24(java.util.ArrayList list, com.android.server.wm.Task rootTask) {
        list.add(getRootTaskInfo(rootTask));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getAllRootTaskInfos$25(java.util.ArrayList list, com.android.server.wm.Task rootTask) {
        list.add(getRootTaskInfo(rootTask));
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayAdded(int displayId) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            android.util.Slog.v(TAG, "Display added displayId=" + displayId);
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent display = getDisplayContentOrCreate(displayId);
                if (display == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                if (this.mService.isBooted() || this.mService.isBooting()) {
                    startSystemDecorations(display);
                }
                this.mWmService.mPossibleDisplayInfoMapper.removePossibleDisplayInfos(displayId);
                this.mRootWindowContainerExt.onDisplayAdded(display);
                ((com.android.server.wm.IMirageWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IMirageWindowManagerExt.class).create()).onDisplayAdded(displayId);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void startSystemDecorations(com.android.server.wm.DisplayContent displayContent) {
        startHomeOnDisplay(this.mCurrentUser, "displayAdded", displayContent.getDisplayId());
        displayContent.getDisplayPolicy().notifyDisplayReady();
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayRemoved(int displayId) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            android.util.Slog.v(TAG, "Display removed displayId=" + displayId);
        }
        if (displayId == 0) {
            throw new java.lang.IllegalArgumentException("Can't remove the primary display.");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = getDisplayContent(displayId);
                if (displayContent == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                this.mRootWindowContainerExt.onDisplayRemoved(displayContent);
                ((com.android.server.wm.IMirageWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IMirageWindowManagerExt.class).create()).onDisplayRemoved(displayId);
                displayContent.remove();
                this.mWmService.mPossibleDisplayInfoMapper.removePossibleDisplayInfos(displayId);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayChanged(final int displayId) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_ROOT_TASK) {
            android.util.Slog.v(TAG, "Display changed displayId=" + displayId);
        }
        if (this.mRootWindowContainerExt.loggingWhenFolding()) {
            android.util.Slog.d(TAG, "FSS_Display changed displayId=" + displayId);
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = getDisplayContent(displayId);
                if (displayContent != null) {
                    displayContent.requestDisplayUpdate(new java.lang.Runnable() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda41
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onDisplayChanged$26(displayId);
                        }
                    });
                } else {
                    lambda$onDisplayChanged$26(displayId);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: clearDisplayInfoCaches, reason: merged with bridge method [inline-methods] */
    public void lambda$onDisplayChanged$26(int displayId) {
        this.mWmService.mPossibleDisplayInfoMapper.removePossibleDisplayInfos(displayId);
        updateDisplayImePolicyCache();
    }

    void updateDisplayImePolicyCache() {
        final android.util.ArrayMap<java.lang.Integer, java.lang.Integer> displayImePolicyMap = new android.util.ArrayMap<>();
        forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) obj;
                displayImePolicyMap.put(java.lang.Integer.valueOf(displayContent.getDisplayId()), java.lang.Integer.valueOf(displayContent.getImePolicy()));
            }
        });
        this.mWmService.mDisplayImePolicyCache = java.util.Collections.unmodifiableMap(displayImePolicyMap);
    }

    void updateUIDsPresentOnDisplay() {
        this.mDisplayAccessUIDs.clear();
        for (int displayNdx = getChildCount() - 1; displayNdx >= 0; displayNdx--) {
            com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) getChildAt(displayNdx);
            if (displayContent.isPrivate()) {
                this.mDisplayAccessUIDs.append(displayContent.mDisplayId, displayContent.getPresentUIDs());
            }
        }
        this.mDisplayManagerInternal.setDisplayAccessUIDs(this.mDisplayAccessUIDs);
    }

    void prepareForShutdown() {
        for (int i = 0; i < getChildCount(); i++) {
            createSleepToken("shutdown", ((com.android.server.wm.DisplayContent) getChildAt(i)).mDisplayId);
        }
    }

    com.android.server.wm.RootWindowContainer.SleepToken createSleepToken(java.lang.String tag, int displayId) {
        return createSleepToken(tag, displayId, false);
    }

    com.android.server.wm.RootWindowContainer.SleepToken createSleepToken(java.lang.String tag, int displayId, boolean isSwappingDisplay) {
        com.android.server.wm.DisplayContent display = getDisplayContent(displayId);
        if (display == null) {
            throw new java.lang.IllegalArgumentException("Invalid display: " + displayId);
        }
        int tokenKey = makeSleepTokenKey(tag, displayId);
        com.android.server.wm.RootWindowContainer.SleepToken token = this.mSleepTokens.get(tokenKey);
        if (token == null) {
            com.android.server.wm.RootWindowContainer.SleepToken token2 = new com.android.server.wm.RootWindowContainer.SleepToken(tag, displayId, isSwappingDisplay);
            this.mSleepTokens.put(tokenKey, token2);
            display.mAllSleepTokens.add(token2);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(tag);
                long protoLogParam1 = displayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -4405347314716558580L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
            }
            if (isSwappingDisplay) {
                display.mWallpaperController.onDisplaySwitchStarted();
            }
            com.android.server.policy.DeviceStateProviderImpl.sExtImpl.notifyCreateSleepToken(token2.mTag, token2.mDisplayId, display.getDisplay());
            return token2;
        }
        throw new java.lang.RuntimeException("Create the same sleep token twice: " + token);
    }

    void removeSleepToken(com.android.server.wm.RootWindowContainer.SleepToken token) {
        if (!this.mSleepTokens.contains(token.mHashKey)) {
            android.util.Slog.d(TAG, "Remove non-exist sleep token: " + token + " from " + android.os.Debug.getCallers(6));
        }
        this.mSleepTokens.remove(token.mHashKey);
        com.android.server.wm.DisplayContent display = getDisplayContent(token.mDisplayId);
        if (display == null) {
            android.util.Slog.d(TAG, "Remove sleep token for non-existing display: " + token + " from " + android.os.Debug.getCallers(6));
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(token.mTag);
            long protoLogParam1 = token.mDisplayId;
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 1329131651776855609L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
        }
        display.mAllSleepTokens.remove(token);
        if (display.mAllSleepTokens.isEmpty()) {
            this.mService.updateSleepIfNeededLocked();
            if ((!this.mTaskSupervisor.getKeyguardController().isKeyguardOccluded(display.mDisplayId) && token.mTag.equals("keyguard")) || token.mTag.equals(DISPLAY_OFF_SLEEP_TOKEN_TAG)) {
                display.mSkipAppTransitionAnimation = true;
            }
        }
        com.android.server.policy.DeviceStateProviderImpl.sExtImpl.notifyRemoveSleepToken(token.mTag, token.mDisplayId, display.getDisplay());
    }

    void addStartingWindowsForVisibleActivities() {
        final java.util.ArrayList<com.android.server.wm.Task> addedTasks = new java.util.ArrayList<>();
        forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda23
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.RootWindowContainer.lambda$addStartingWindowsForVisibleActivities$28(addedTasks, (com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    static /* synthetic */ void lambda$addStartingWindowsForVisibleActivities$28(java.util.ArrayList addedTasks, com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.Task task = r.getTask();
        if (r.isVisibleRequested() && r.mStartingData != null) {
            addedTasks.add(task);
        }
        if (r.isVisibleRequested() && r.mStartingData == null && !addedTasks.contains(task)) {
            r.showStartingWindow(true);
            addedTasks.add(task);
        }
    }

    void invalidateTaskLayers() {
        if (!this.mTaskLayersChanged) {
            this.mTaskLayersChanged = true;
            this.mService.mH.post(this.mRankTaskLayersRunnable);
        }
    }

    void rankTaskLayers() {
        if (this.mTaskLayersChanged) {
            this.mTaskLayersChanged = false;
            this.mService.mH.removeCallbacks(this.mRankTaskLayersRunnable);
        }
        this.mTmpTaskLayerRank = 0;
        forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$rankTaskLayers$30((com.android.server.wm.Task) obj);
            }
        }, true);
        if (!this.mTaskSupervisor.inActivityVisibilityUpdate()) {
            this.mTaskSupervisor.computeProcessActivityStateBatch();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rankTaskLayers$30(com.android.server.wm.Task task) {
        int oldRank = task.mLayerRank;
        com.android.server.wm.ActivityRecord r = task.topRunningActivityLocked();
        if (r != null && r.isVisibleRequested()) {
            int i = this.mTmpTaskLayerRank + 1;
            this.mTmpTaskLayerRank = i;
            task.mLayerRank = i;
        } else {
            task.mLayerRank = -1;
        }
        if (task.mLayerRank != oldRank) {
            task.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda49
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$rankTaskLayers$29((com.android.server.wm.ActivityRecord) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rankTaskLayers$29(com.android.server.wm.ActivityRecord activity) {
        if (activity.hasProcess()) {
            this.mTaskSupervisor.onProcessActivityStateChanged(activity.app, true);
        }
    }

    void clearOtherAppTimeTrackers(final com.android.server.am.AppTimeTracker except) {
        forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.RootWindowContainer.lambda$clearOtherAppTimeTrackers$31(except, (com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    static /* synthetic */ void lambda$clearOtherAppTimeTrackers$31(com.android.server.am.AppTimeTracker except, com.android.server.wm.ActivityRecord r) {
        if (r.appTimeTracker != except) {
            r.appTimeTracker = null;
        }
    }

    void scheduleDestroyAllActivities(java.lang.String reason) {
        this.mDestroyAllActivitiesReason = reason;
        this.mService.mH.post(this.mDestroyAllActivitiesRunnable);
    }

    void removeAllMaybeAbortPipEnterRunnable() {
        if (this.mMaybeAbortPipEnterRunnable == null) {
            return;
        }
        this.mHandler.removeCallbacks(this.mMaybeAbortPipEnterRunnable);
        this.mMaybeAbortPipEnterRunnable = null;
    }

    boolean putTasksToSleep(final boolean allowDelay, final boolean shuttingDown) {
        final boolean[] result = {true};
        forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda38
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.RootWindowContainer.lambda$putTasksToSleep$32(shuttingDown, allowDelay, result, (com.android.server.wm.Task) obj);
            }
        });
        return result[0];
    }

    static /* synthetic */ void lambda$putTasksToSleep$32(boolean shuttingDown, boolean allowDelay, boolean[] result, com.android.server.wm.Task task) {
        if (!shuttingDown) {
            boolean shouldSleep = ((com.android.server.wm.IMirageWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IMirageWindowManagerExt.class).create()).onGoingToSleep(task.getDisplayId());
            if (!shouldSleep) {
                return;
            }
        }
        if (allowDelay) {
            result[0] = result[0] & task.goToSleepIfPossible(shuttingDown);
        } else {
            task.ensureActivitiesVisible(null);
        }
    }

    com.android.server.wm.ActivityRecord findActivity(android.content.Intent intent, android.content.pm.ActivityInfo info, boolean compareIntentFilters) {
        android.content.ComponentName cls = intent.getComponent();
        if (info.targetActivity != null) {
            cls = new android.content.ComponentName(info.packageName, info.targetActivity);
        }
        int userId = android.os.UserHandle.getUserId(info.applicationInfo.uid);
        com.android.internal.util.function.pooled.PooledPredicate p = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new com.android.internal.util.function.QuintPredicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda10
            public final boolean test(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                return com.android.server.wm.RootWindowContainer.matchesActivity((com.android.server.wm.ActivityRecord) obj, ((java.lang.Integer) obj2).intValue(), ((java.lang.Boolean) obj3).booleanValue(), (android.content.Intent) obj4, (android.content.ComponentName) obj5);
            }
        }, com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.ActivityRecord.class), java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(compareIntentFilters), intent, cls);
        com.android.server.wm.ActivityRecord r = getActivity(p);
        p.recycle();
        return r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean matchesActivity(com.android.server.wm.ActivityRecord r, int userId, boolean compareIntentFilters, android.content.Intent intent, android.content.ComponentName cls) {
        if (!r.canBeTopRunning() || r.mUserId != userId) {
            return false;
        }
        if (compareIntentFilters) {
            if (r.intent.filterEquals(intent)) {
                return true;
            }
        } else if (r.mActivityComponent.equals(cls)) {
            return true;
        }
        return false;
    }

    boolean hasAwakeDisplay() {
        for (int displayNdx = getChildCount() - 1; displayNdx >= 0; displayNdx--) {
            com.android.server.wm.DisplayContent display = (com.android.server.wm.DisplayContent) getChildAt(displayNdx);
            if (!display.shouldSleep()) {
                return true;
            }
        }
        return false;
    }

    com.android.server.wm.Task getOrCreateRootTask(com.android.server.wm.ActivityRecord r, android.app.ActivityOptions options, com.android.server.wm.Task candidateTask, boolean onTop) {
        return getOrCreateRootTask(r, options, candidateTask, null, onTop, null, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    com.android.server.wm.Task getOrCreateRootTask(com.android.server.wm.ActivityRecord r, android.app.ActivityOptions options, com.android.server.wm.Task candidateTask, com.android.server.wm.Task sourceTask, boolean onTop, com.android.server.wm.LaunchParamsController.LaunchParams launchParams, int launchFlags) {
        com.android.server.wm.TaskDisplayArea taskDisplayArea;
        int launchDisplayId;
        com.android.server.wm.DisplayContent displayContent;
        int activityType;
        com.android.server.wm.Task task;
        com.android.server.wm.TaskDisplayArea taskDisplayArea2;
        com.android.server.wm.TaskDisplayArea taskDisplayArea3;
        int candidateTaskId;
        com.android.server.wm.Task candidateRoot;
        if (options != null && (candidateRoot = com.android.server.wm.Task.fromWindowContainerToken(options.getLaunchRootTask())) != null && canLaunchOnDisplay(r, candidateRoot)) {
            return candidateRoot;
        }
        if (options != null && (candidateTaskId = options.getLaunchTaskId()) != -1) {
            options.setLaunchTaskId(-1);
            com.android.server.wm.Task task2 = anyTaskForId(candidateTaskId, 2, options, onTop);
            options.setLaunchTaskId(candidateTaskId);
            if (canLaunchOnDisplay(r, task2)) {
                return task2.getRootTask();
            }
        }
        if (launchParams != null && launchParams.mPreferredTaskDisplayArea != null) {
            com.android.server.wm.TaskDisplayArea taskDisplayArea4 = launchParams.mPreferredTaskDisplayArea;
            taskDisplayArea = taskDisplayArea4;
        } else if (options == null) {
            taskDisplayArea = null;
        } else {
            android.window.WindowContainerToken daToken = options.getLaunchTaskDisplayArea();
            com.android.server.wm.TaskDisplayArea taskDisplayArea5 = daToken != null ? (com.android.server.wm.TaskDisplayArea) com.android.server.wm.WindowContainer.fromBinder(daToken.asBinder()) : null;
            if (taskDisplayArea5 == null && (launchDisplayId = options.getLaunchDisplayId()) != -1 && (displayContent = getDisplayContent(launchDisplayId)) != null) {
                com.android.server.wm.TaskDisplayArea taskDisplayArea6 = displayContent.getDefaultTaskDisplayArea();
                taskDisplayArea = taskDisplayArea6;
            } else {
                taskDisplayArea = taskDisplayArea5;
            }
        }
        int activityType2 = resolveActivityType(r, options, candidateTask);
        if (taskDisplayArea != null) {
            if (canLaunchOnDisplay(r, taskDisplayArea.getDisplayId())) {
                return taskDisplayArea.getOrCreateRootTask(r, options, candidateTask, sourceTask, launchParams, launchFlags, activityType2, onTop);
            }
            activityType = activityType2;
            taskDisplayArea = null;
        } else {
            activityType = activityType2;
        }
        com.android.server.wm.Task rootTask = null;
        if (candidateTask != null) {
            rootTask = candidateTask.getRootTask();
        }
        if (rootTask == null && r != null) {
            com.android.server.wm.Task rootTask2 = r.getRootTask();
            task = rootTask2;
        } else {
            task = rootTask;
        }
        int windowingMode = launchParams != null ? launchParams.mWindowingMode : 0;
        if (task == 0) {
            taskDisplayArea2 = taskDisplayArea;
        } else {
            com.android.server.wm.TaskDisplayArea taskDisplayArea7 = task.getDisplayArea();
            if (taskDisplayArea7 != null && canLaunchOnDisplay(r, taskDisplayArea7.mDisplayContent.mDisplayId)) {
                if (windowingMode == 0) {
                    windowingMode = taskDisplayArea7.resolveWindowingMode(r, options, candidateTask);
                }
                if (task.isCompatible(windowingMode, activityType) || task.mCreatedByOrganizer) {
                    return task;
                }
                taskDisplayArea2 = taskDisplayArea7;
            } else {
                taskDisplayArea2 = null;
            }
        }
        if (taskDisplayArea2 != null) {
            taskDisplayArea3 = taskDisplayArea2;
        } else {
            com.android.server.wm.TaskDisplayArea taskDisplayArea8 = getDefaultTaskDisplayArea();
            taskDisplayArea3 = taskDisplayArea8;
        }
        return taskDisplayArea3.getOrCreateRootTask(r, options, candidateTask, sourceTask, launchParams, launchFlags, activityType, onTop);
    }

    private boolean canLaunchOnDisplay(com.android.server.wm.ActivityRecord r, com.android.server.wm.Task task) {
        if (task == null) {
            android.util.Slog.w(TAG, "canLaunchOnDisplay(), invalid task: " + task);
            return false;
        }
        if (!task.isAttached()) {
            android.util.Slog.w(TAG, "canLaunchOnDisplay(), Task is not attached: " + task);
            return false;
        }
        return canLaunchOnDisplay(r, task.getTaskDisplayArea().getDisplayId());
    }

    private boolean canLaunchOnDisplay(com.android.server.wm.ActivityRecord r, int displayId) {
        if (r == null || r.canBeLaunchedOnDisplay(displayId)) {
            return true;
        }
        android.util.Slog.w(TAG, "Not allow to launch " + r + " on display " + displayId);
        return false;
    }

    int resolveActivityType(com.android.server.wm.ActivityRecord r, android.app.ActivityOptions options, com.android.server.wm.Task task) {
        int activityType = r != null ? r.getActivityType() : 0;
        if (activityType == 0 && task != null) {
            activityType = task.getActivityType();
        }
        if (activityType != 0) {
            return activityType;
        }
        if (options != null) {
            activityType = options.getLaunchActivityType();
        }
        if (activityType != 0) {
            return activityType;
        }
        return 1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    com.android.server.wm.Task getNextFocusableRootTask(com.android.server.wm.Task task, boolean ignoreCurrent) {
        com.android.server.wm.Task nextFocusableRootTask;
        com.android.server.wm.TaskDisplayArea preferredDisplayArea = task.getDisplayArea();
        if (preferredDisplayArea == null) {
            preferredDisplayArea = getDisplayContent(task.mPrevDisplayId).getDefaultTaskDisplayArea();
        }
        com.android.server.wm.Task preferredFocusableRootTask = preferredDisplayArea.getNextFocusableRootTask(task, ignoreCurrent);
        if (preferredFocusableRootTask != null) {
            return preferredFocusableRootTask;
        }
        if (preferredDisplayArea.mDisplayContent.isHomeSupported()) {
            return null;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent display = (com.android.server.wm.DisplayContent) getChildAt(i);
            if (display != preferredDisplayArea.mDisplayContent && (nextFocusableRootTask = display.getDefaultTaskDisplayArea().getNextFocusableRootTask(task, ignoreCurrent)) != null) {
                return nextFocusableRootTask;
            }
        }
        return null;
    }

    void closeSystemDialogActivities(final java.lang.String reason) {
        forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda34
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$closeSystemDialogActivities$33(reason, (com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$closeSystemDialogActivities$33(java.lang.String reason, com.android.server.wm.ActivityRecord r) {
        if ((r.info.flags & 256) != 0 || shouldCloseAssistant(r, reason)) {
            r.finishIfPossible(reason, true);
        }
    }

    boolean hasVisibleWindowAboveButDoesNotOwnNotificationShade(final int uid) {
        final boolean[] visibleWindowFound = {false};
        return forAllWindows(new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda42
            public final boolean apply(java.lang.Object obj) {
                return com.android.server.wm.RootWindowContainer.lambda$hasVisibleWindowAboveButDoesNotOwnNotificationShade$34(uid, visibleWindowFound, (com.android.server.wm.WindowState) obj);
            }
        }, true);
    }

    static /* synthetic */ boolean lambda$hasVisibleWindowAboveButDoesNotOwnNotificationShade$34(int uid, boolean[] visibleWindowFound, com.android.server.wm.WindowState w) {
        if (w.mOwnerUid == uid && w.isVisible()) {
            visibleWindowFound[0] = true;
        }
        if (w.mAttrs.type == 2040) {
            return visibleWindowFound[0] && w.mOwnerUid != uid;
        }
        return false;
    }

    private boolean shouldCloseAssistant(com.android.server.wm.ActivityRecord r, java.lang.String reason) {
        if (r.isActivityTypeAssistant() && reason != com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_ASSIST) {
            return this.mWmService.mAssistantOnTopOfDream;
        }
        return false;
    }

    class FinishDisabledPackageActivitiesHelper implements java.util.function.Predicate<com.android.server.wm.ActivityRecord> {
        private final java.util.ArrayList<com.android.server.wm.ActivityRecord> mCollectedActivities = new java.util.ArrayList<>();
        private boolean mDoit;
        private boolean mEvenPersistent;
        private java.util.Set<java.lang.String> mFilterByClasses;
        private com.android.server.wm.Task mLastTask;
        private boolean mOnlyRemoveNoProcess;
        private java.lang.String mPackageName;
        private int mUserId;

        FinishDisabledPackageActivitiesHelper() {
        }

        private void reset(java.lang.String packageName, java.util.Set<java.lang.String> filterByClasses, boolean doit, boolean evenPersistent, int userId, boolean onlyRemoveNoProcess) {
            this.mPackageName = packageName;
            this.mFilterByClasses = filterByClasses;
            this.mDoit = doit;
            this.mEvenPersistent = evenPersistent;
            this.mUserId = userId;
            this.mOnlyRemoveNoProcess = onlyRemoveNoProcess;
            this.mLastTask = null;
        }

        boolean process(java.lang.String packageName, java.util.Set<java.lang.String> filterByClasses, boolean doit, boolean evenPersistent, int userId, boolean onlyRemoveNoProcess) {
            reset(packageName, filterByClasses, doit, evenPersistent, userId, onlyRemoveNoProcess);
            com.android.server.wm.RootWindowContainer.this.forAllActivities(this);
            boolean didSomething = false;
            int size = this.mCollectedActivities.size();
            for (int i = 0; i < size; i++) {
                com.android.server.wm.ActivityRecord r = this.mCollectedActivities.get(i);
                if (this.mOnlyRemoveNoProcess) {
                    if (!r.hasProcess()) {
                        didSomething = true;
                        android.util.Slog.i(com.android.server.wm.RootWindowContainer.TAG, "  Force removing " + r);
                        r.cleanUp(false, false);
                        r.removeFromHistory("force-stop");
                    }
                } else {
                    didSomething = true;
                    android.util.Slog.i(com.android.server.wm.RootWindowContainer.TAG, "  Force finishing " + r);
                    r.finishIfPossible("force-stop", true);
                }
            }
            this.mCollectedActivities.clear();
            return didSomething;
        }

        @Override // java.util.function.Predicate
        public boolean test(com.android.server.wm.ActivityRecord r) {
            boolean sameComponent = (r.packageName.equals(this.mPackageName) && (this.mFilterByClasses == null || this.mFilterByClasses.contains(r.mActivityComponent.getClassName()))) || (this.mPackageName == null && r.mUserId == this.mUserId);
            boolean noProcess = !r.hasProcess();
            if ((this.mUserId == -1 || r.mUserId == this.mUserId) && ((sameComponent || r.getTask() == this.mLastTask) && (noProcess || this.mEvenPersistent || !r.app.isPersistent()))) {
                if (!this.mDoit) {
                    return !r.finishing;
                }
                this.mCollectedActivities.add(r);
                this.mLastTask = r.getTask();
            }
            return false;
        }
    }

    boolean finishDisabledPackageActivities(java.lang.String packageName, java.util.Set<java.lang.String> filterByClasses, boolean doit, boolean evenPersistent, int userId, boolean onlyRemoveNoProcess) {
        return this.mFinishDisabledPackageActivitiesHelper.process(packageName, filterByClasses, doit, evenPersistent, userId, onlyRemoveNoProcess);
    }

    void updateActivityApplicationInfo(final android.content.pm.ApplicationInfo aInfo) {
        final java.lang.String packageName = aInfo.packageName;
        final int userId = android.os.UserHandle.getUserId(aInfo.uid);
        forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.RootWindowContainer.lambda$updateActivityApplicationInfo$35(userId, packageName, aInfo, (com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    static /* synthetic */ void lambda$updateActivityApplicationInfo$35(int userId, java.lang.String packageName, android.content.pm.ApplicationInfo aInfo, com.android.server.wm.ActivityRecord r) {
        if (r.mUserId == userId && packageName.equals(r.packageName)) {
            r.updateApplicationInfo(aInfo);
        }
    }

    void updateActivityApplicationInfo(final int userId, final android.util.ArrayMap<java.lang.String, android.content.pm.ApplicationInfo> applicationInfoByPackage) {
        forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda52
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.RootWindowContainer.lambda$updateActivityApplicationInfo$36(userId, applicationInfoByPackage, (com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    static /* synthetic */ void lambda$updateActivityApplicationInfo$36(int userId, android.util.ArrayMap applicationInfoByPackage, com.android.server.wm.ActivityRecord r) {
        android.content.pm.ApplicationInfo aInfo;
        if (r.mUserId == userId && (aInfo = (android.content.pm.ApplicationInfo) applicationInfoByPackage.get(r.packageName)) != null) {
            r.updateApplicationInfo(aInfo);
        }
    }

    void finishVoiceTask(android.service.voice.IVoiceInteractionSession session) {
        final android.os.IBinder binder = session.asBinder();
        forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda32
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.Task) obj).finishIfVoiceTask(binder);
            }
        }, true);
    }

    void removeRootTasksInWindowingModes(int... windowingModes) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            ((com.android.server.wm.DisplayContent) getChildAt(i)).removeRootTasksInWindowingModes(windowingModes);
        }
    }

    void removeRootTasksWithActivityTypes(int... activityTypes) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            ((com.android.server.wm.DisplayContent) getChildAt(i)).removeRootTasksWithActivityTypes(activityTypes);
        }
    }

    com.android.server.wm.ActivityRecord topRunningActivity() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord topActivity = ((com.android.server.wm.DisplayContent) getChildAt(i)).topRunningActivity();
            if (topActivity != null) {
                return topActivity;
            }
        }
        return null;
    }

    boolean allResumedActivitiesIdle() {
        com.android.server.wm.Task rootTask;
        for (int displayNdx = getChildCount() - 1; displayNdx >= 0; displayNdx--) {
            com.android.server.wm.DisplayContent display = (com.android.server.wm.DisplayContent) getChildAt(displayNdx);
            if (!display.isSleeping() && (rootTask = display.getFocusedRootTask()) != null && rootTask.hasActivity()) {
                com.android.server.wm.ActivityRecord resumedActivity = rootTask.getTopResumedActivity();
                if (resumedActivity == null || !resumedActivity.idle) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[0]) {
                        long protoLogParam0 = rootTask.getRootTaskId();
                        java.lang.String protoLogParam1 = java.lang.String.valueOf(resumedActivity);
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 1653728842643223887L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
                    }
                    if (this.mService.isBooting()) {
                        android.util.Slog.d(TAG, "allResumedActivitiesIdle not idle display=" + display + ",rootTask=" + rootTask + ",resumedActivity=" + resumedActivity);
                    }
                    return false;
                }
                if (this.mTransitionController.isTransientLaunch(resumedActivity) || this.mTransitionController.isTransientLaunch(resumedActivity)) {
                    return false;
                }
            }
        }
        this.mService.endPowerMode(1);
        return true;
    }

    boolean allResumedActivitiesVisible() {
        final boolean[] foundResumed = {false};
        boolean foundInvisibleResumedActivity = forAllRootTasks(new java.util.function.Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda28
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.RootWindowContainer.lambda$allResumedActivitiesVisible$38(foundResumed, (com.android.server.wm.Task) obj);
            }
        });
        if (foundInvisibleResumedActivity) {
            return false;
        }
        return foundResumed[0];
    }

    static /* synthetic */ boolean lambda$allResumedActivitiesVisible$38(boolean[] foundResumed, com.android.server.wm.Task rootTask) {
        com.android.server.wm.ActivityRecord r = rootTask.getTopResumedActivity();
        if (r != null) {
            if (!r.nowVisible) {
                return true;
            }
            foundResumed[0] = true;
        }
        return false;
    }

    boolean allPausedActivitiesComplete() {
        final boolean[] pausing = {true};
        boolean hasActivityNotCompleted = forAllLeafTasks(new java.util.function.Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda22
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$allPausedActivitiesComplete$39(pausing, (com.android.server.wm.Task) obj);
            }
        });
        if (hasActivityNotCompleted) {
            return false;
        }
        return pausing[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$allPausedActivitiesComplete$39(boolean[] pausing, com.android.server.wm.Task task) {
        com.android.server.wm.ActivityRecord r = task.getTopPausingActivity();
        if (r != null && !r.isState(com.android.server.wm.ActivityRecord.State.PAUSED, com.android.server.wm.ActivityRecord.State.STOPPED, com.android.server.wm.ActivityRecord.State.STOPPING, com.android.server.wm.ActivityRecord.State.FINISHING) && this.mRootWindowContainerExt.isWaitingPausingActivity(task)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(r);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(r.getState());
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 3785779399471740019L, 0, null, protoLogParam0, protoLogParam1);
            }
            if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES.isEnabled()) {
                pausing[0] = false;
            } else {
                return true;
            }
        }
        return false;
    }

    void lockAllProfileTasks(final int userId) {
        forAllLeafTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda44
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$lockAllProfileTasks$41(userId, (com.android.server.wm.Task) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$lockAllProfileTasks$41(final int userId, com.android.server.wm.Task task) {
        com.android.server.wm.ActivityRecord top = task.topRunningActivity();
        if ((top == null || top.finishing || !"android.app.action.CONFIRM_DEVICE_CREDENTIAL_WITH_USER".equals(top.intent.getAction()) || !top.packageName.equals(this.mService.getSysUiServiceComponentLocked().getPackageName())) && task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda31
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.RootWindowContainer.lambda$lockAllProfileTasks$40(userId, (com.android.server.wm.ActivityRecord) obj);
            }
        }) != null) {
            this.mService.getTaskChangeNotificationController().notifyTaskProfileLocked(task.getTaskInfo(), userId);
        }
    }

    static /* synthetic */ boolean lambda$lockAllProfileTasks$40(int userId, com.android.server.wm.ActivityRecord activity) {
        return !activity.finishing && activity.mUserId == userId;
    }

    com.android.server.wm.Task anyTaskForId(int id) {
        return anyTaskForId(id, 2);
    }

    com.android.server.wm.Task anyTaskForId(int id, int matchMode) {
        return anyTaskForId(id, matchMode, null, false);
    }

    com.android.server.wm.Task anyTaskForId(int id, int matchMode, android.app.ActivityOptions aOptions, boolean onTop) {
        com.android.server.wm.Task targetRootTask;
        int i = 2;
        if (matchMode != 2 && aOptions != null) {
            throw new java.lang.IllegalArgumentException("Should not specify activity options for non-restore lookup");
        }
        com.android.internal.util.function.pooled.PooledPredicate p = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new com.android.server.wm.AppTransition$$ExternalSyntheticLambda1(), com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.Task.class), java.lang.Integer.valueOf(id));
        com.android.server.wm.Task task = getTask(p);
        p.recycle();
        if (task != null) {
            if (aOptions != null && !this.mRootWindowContainerExt.skipResolveRootTaskIfNeed(task) && !this.mRootWindowContainerExt.isTaskOnPuttDisplay(task) && (targetRootTask = getOrCreateRootTask(null, aOptions, task, onTop)) != null && task.getRootTask() != targetRootTask && task.getParent() != targetRootTask) {
                if (onTop) {
                    i = 0;
                }
                int reparentMode = i;
                task.reparent(targetRootTask, onTop, reparentMode, true, true, "anyTaskForId");
            }
            return task;
        }
        if (matchMode == 0) {
            return null;
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
            android.util.Slog.v(TAG_RECENTS, "Looking for task id=" + id + " in recents");
        }
        com.android.server.wm.Task task2 = this.mTaskSupervisor.mRecentTasks.getTask(id);
        if (task2 == null) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                android.util.Slog.d(TAG_RECENTS, "\tDidn't find task id=" + id + " in recents");
            }
            return null;
        }
        if (matchMode == 1) {
            return task2;
        }
        if (!this.mTaskSupervisor.restoreRecentTaskLocked(task2, aOptions, onTop)) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
                android.util.Slog.w(TAG_RECENTS, "Couldn't restore task id=" + id + " found in recents");
            }
            return null;
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RECENTS) {
            android.util.Slog.w(TAG_RECENTS, "Restored task id=" + id + " from in recents");
        }
        return task2;
    }

    void getRunningTasks(int maxNum, java.util.List<android.app.ActivityManager.RunningTaskInfo> list, int flags, int callingUid, android.util.ArraySet<java.lang.Integer> profileIds, int displayId) {
        com.android.server.wm.WindowContainer root = this;
        if (displayId != -1 && (root = getDisplayContent(displayId)) == null) {
            return;
        }
        this.mTaskSupervisor.getRunningTasks().getTasks(maxNum, list, flags, this.mService.getRecentTasks(), root, callingUid, profileIds);
    }

    void startPowerModeLaunchIfNeeded(boolean forceSend, final com.android.server.wm.ActivityRecord targetActivity) {
        android.app.ActivityOptions opts;
        if (!forceSend && targetActivity != null && targetActivity.app != null) {
            final boolean[] noResumedActivities = {true};
            final boolean[] allFocusedProcessesDiffer = {true};
            forAllTaskDisplayAreas(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda13
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.RootWindowContainer.lambda$startPowerModeLaunchIfNeeded$42(noResumedActivities, allFocusedProcessesDiffer, targetActivity, (com.android.server.wm.TaskDisplayArea) obj);
                }
            });
            if (!noResumedActivities[0] && !allFocusedProcessesDiffer[0]) {
                return;
            }
        }
        int reason = 1;
        boolean isKeyguardLocked = targetActivity != null ? targetActivity.isKeyguardLocked() : this.mDefaultDisplay.isKeyguardLocked();
        if (isKeyguardLocked && targetActivity != null && !targetActivity.isLaunchSourceType(3) && ((opts = targetActivity.getOptions()) == null || opts.getSourceInfo() == null || opts.getSourceInfo().type != 3)) {
            reason = 1 | 4;
        }
        this.mService.startPowerMode(reason);
    }

    static /* synthetic */ void lambda$startPowerModeLaunchIfNeeded$42(boolean[] noResumedActivities, boolean[] allFocusedProcessesDiffer, com.android.server.wm.ActivityRecord targetActivity, com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        com.android.server.wm.ActivityRecord resumedActivity = taskDisplayArea.getFocusedActivity();
        com.android.server.wm.WindowProcessController resumedActivityProcess = resumedActivity == null ? null : resumedActivity.app;
        noResumedActivities[0] = noResumedActivities[0] & (resumedActivityProcess == null);
        if (resumedActivityProcess != null) {
            allFocusedProcessesDiffer[0] = allFocusedProcessesDiffer[0] & (true ^ resumedActivityProcess.equals(targetActivity.app));
        }
    }

    public int getTaskToShowPermissionDialogOn(final java.lang.String pkgName, final int uid) {
        final com.android.server.policy.PermissionPolicyInternal pPi = this.mService.getPermissionPolicyInternal();
        if (pPi == null) {
            return -1;
        }
        final int[] validTaskId = {-1};
        forAllLeafTaskFragments(new java.util.function.Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda33
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.RootWindowContainer.lambda$getTaskToShowPermissionDialogOn$44(pPi, uid, pkgName, validTaskId, (com.android.server.wm.TaskFragment) obj);
            }
        });
        return validTaskId[0];
    }

    static /* synthetic */ boolean lambda$getTaskToShowPermissionDialogOn$44(final com.android.server.policy.PermissionPolicyInternal pPi, int uid, java.lang.String pkgName, int[] validTaskId, com.android.server.wm.TaskFragment fragment) {
        com.android.server.wm.ActivityRecord record = fragment.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.RootWindowContainer.lambda$getTaskToShowPermissionDialogOn$43(pPi, (com.android.server.wm.ActivityRecord) obj);
            }
        });
        if (record == null || !record.isUid(uid) || !java.util.Objects.equals(pkgName, record.packageName) || !pPi.shouldShowNotificationDialogForTask(record.getTask().getTaskInfo(), pkgName, record.launchedFromPackage, record.intent, record.getName())) {
            return false;
        }
        validTaskId[0] = record.getTask().mTaskId;
        return true;
    }

    static /* synthetic */ boolean lambda$getTaskToShowPermissionDialogOn$43(com.android.server.policy.PermissionPolicyInternal pPi, com.android.server.wm.ActivityRecord r) {
        return r.canBeTopRunning() && r.isVisibleRequested() && !pPi.isIntentToPermissionDialog(r.intent);
    }

    java.util.ArrayList<com.android.server.wm.ActivityRecord> getDumpActivities(final java.lang.String name, final boolean dumpVisibleRootTasksOnly, boolean dumpFocusedRootTaskOnly, final int userId) {
        final int recentsComponentUid;
        if (dumpFocusedRootTaskOnly) {
            com.android.server.wm.Task topFocusedRootTask = getTopDisplayFocusedRootTask();
            if (topFocusedRootTask != null) {
                return topFocusedRootTask.getDumpActivitiesLocked(name, userId);
            }
            return new java.util.ArrayList<>();
        }
        com.android.server.wm.RecentTasks recentTasks = this.mWindowManager.mAtmService.getRecentTasks();
        if (recentTasks != null) {
            recentsComponentUid = recentTasks.getRecentsComponentUid();
        } else {
            recentsComponentUid = -1;
        }
        final java.util.ArrayList<com.android.server.wm.ActivityRecord> activities = new java.util.ArrayList<>();
        forAllLeafTasks(new java.util.function.Predicate() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda50
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.RootWindowContainer.lambda$getDumpActivities$45(recentsComponentUid, dumpVisibleRootTasksOnly, activities, name, userId, (com.android.server.wm.Task) obj);
            }
        });
        return activities;
    }

    static /* synthetic */ boolean lambda$getDumpActivities$45(int recentsComponentUid, boolean dumpVisibleRootTasksOnly, java.util.ArrayList activities, java.lang.String name, int userId, com.android.server.wm.Task task) {
        boolean isRecents = task.effectiveUid == recentsComponentUid;
        if (!dumpVisibleRootTasksOnly || task.shouldBeVisible(null) || isRecents) {
            activities.addAll(task.getDumpActivitiesLocked(name, userId));
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    public void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        super.dump(pw, prefix, dumpAll);
        pw.print(prefix);
        pw.println("topDisplayFocusedRootTask=" + getTopDisplayFocusedRootTask());
        for (int i = getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent display = (com.android.server.wm.DisplayContent) getChildAt(i);
            display.dump(pw, prefix, dumpAll);
        }
    }

    void dumpDisplayConfigs(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.println("Display override configurations:");
        int displayCount = getChildCount();
        for (int i = 0; i < displayCount; i++) {
            com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) getChildAt(i);
            pw.print(prefix);
            pw.print("  ");
            pw.print(displayContent.mDisplayId);
            pw.print(": ");
            pw.println(displayContent.getRequestedOverrideConfiguration());
        }
    }

    boolean dumpActivities(final java.io.FileDescriptor fd, final java.io.PrintWriter pw, final boolean dumpAll, final boolean dumpClient, final java.lang.String dumpPackage, int displayIdFilter) {
        char c = 0;
        final boolean[] printed = {false};
        final boolean[] needSep = {false};
        int displayNdx = getChildCount() - 1;
        while (displayNdx >= 0) {
            com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) getChildAt(displayNdx);
            if (printed[c]) {
                pw.println();
            }
            if (displayIdFilter == -1 || displayContent.mDisplayId == displayIdFilter) {
                pw.print("Display #");
                pw.print(displayContent.mDisplayId);
                pw.println(" (activities from top to bottom):");
                displayContent.forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wm.RootWindowContainer.lambda$dumpActivities$46(needSep, pw, fd, dumpAll, dumpClient, dumpPackage, printed, (com.android.server.wm.Task) obj);
                    }
                });
                displayContent.forAllTaskDisplayAreas(new java.util.function.Consumer() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wm.RootWindowContainer.lambda$dumpActivities$48(printed, pw, dumpPackage, needSep, (com.android.server.wm.TaskDisplayArea) obj);
                    }
                });
            }
            displayNdx--;
            c = 0;
        }
        printed[0] = printed[0] | com.android.server.wm.ActivityTaskSupervisor.dumpHistoryList(fd, pw, this.mTaskSupervisor.mFinishingActivities, "  ", "Fin", false, !dumpAll, false, dumpPackage, true, new java.lang.Runnable() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                pw.println("  Activities waiting to finish:");
            }
        }, null);
        printed[0] = printed[0] | com.android.server.wm.ActivityTaskSupervisor.dumpHistoryList(fd, pw, this.mTaskSupervisor.mStoppingActivities, "  ", "Stop", false, !dumpAll, false, dumpPackage, true, new java.lang.Runnable() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                pw.println("  Activities waiting to stop:");
            }
        }, null);
        return printed[0];
    }

    static /* synthetic */ void lambda$dumpActivities$46(boolean[] needSep, java.io.PrintWriter pw, java.io.FileDescriptor fd, boolean dumpAll, boolean dumpClient, java.lang.String dumpPackage, boolean[] printed, com.android.server.wm.Task rootTask) {
        if (needSep[0]) {
            pw.println();
        }
        needSep[0] = rootTask.dump(fd, pw, dumpAll, dumpClient, dumpPackage, false);
        printed[0] = printed[0] | needSep[0];
    }

    static /* synthetic */ void lambda$dumpActivities$48(boolean[] printed, final java.io.PrintWriter pw, java.lang.String dumpPackage, boolean[] needSep, com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        printed[0] = printed[0] | com.android.server.wm.ActivityTaskSupervisor.printThisActivity(pw, taskDisplayArea.getFocusedActivity(), dumpPackage, needSep[0], "    Resumed: ", new java.lang.Runnable() { // from class: com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                pw.println("  Resumed activities in task display areas (from top to bottom):");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int makeSleepTokenKey(java.lang.String tag, int displayId) {
        java.lang.String tokenKey = tag + displayId;
        return tokenKey.hashCode();
    }

    static final class SleepToken {
        private static final long DISPLAY_SWAP_TIMEOUT = 1000;
        private final long mAcquireTime = android.os.SystemClock.uptimeMillis();
        private final int mDisplayId;
        final int mHashKey;
        private final boolean mIsSwappingDisplay;
        private final java.lang.String mTag;

        SleepToken(java.lang.String tag, int displayId, boolean isSwappingDisplay) {
            this.mTag = tag;
            this.mDisplayId = displayId;
            this.mIsSwappingDisplay = isSwappingDisplay;
            this.mHashKey = com.android.server.wm.RootWindowContainer.makeSleepTokenKey(this.mTag, this.mDisplayId);
        }

        public boolean isDisplaySwapping() {
            long now = android.os.SystemClock.uptimeMillis();
            if (now - this.mAcquireTime > 1000) {
                return false;
            }
            return this.mIsSwappingDisplay;
        }

        public java.lang.String toString() {
            return "{\"" + this.mTag + "\", display " + this.mDisplayId + (this.mIsSwappingDisplay ? " is swapping " : "") + ", acquire at " + android.util.TimeUtils.formatUptime(this.mAcquireTime) + "}";
        }

        void writeTagToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
            proto.write(fieldId, this.mTag);
        }
    }

    private class RankTaskLayersRunnable implements java.lang.Runnable {
        private RankTaskLayersRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.RootWindowContainer.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.RootWindowContainer.this.mTaskLayersChanged) {
                        com.android.server.wm.RootWindowContainer.this.mTaskLayersChanged = false;
                        com.android.server.wm.RootWindowContainer.this.rankTaskLayers();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    private class AttachApplicationHelper implements java.util.function.Consumer<com.android.server.wm.Task>, java.util.function.Predicate<com.android.server.wm.ActivityRecord> {
        private com.android.server.wm.WindowProcessController mApp;
        private boolean mHasActivityStarted;
        private android.os.RemoteException mRemoteException;
        private com.android.server.wm.ActivityRecord mTop;

        private AttachApplicationHelper() {
        }

        void reset() {
            this.mHasActivityStarted = false;
            this.mRemoteException = null;
            this.mApp = null;
            this.mTop = null;
        }

        boolean process(com.android.server.wm.WindowProcessController app) throws android.os.RemoteException {
            this.mApp = app;
            for (int displayNdx = com.android.server.wm.RootWindowContainer.this.getChildCount() - 1; displayNdx >= 0; displayNdx--) {
                ((com.android.server.wm.DisplayContent) com.android.server.wm.RootWindowContainer.this.getChildAt(displayNdx)).forAllRootTasks((java.util.function.Consumer<com.android.server.wm.Task>) this);
                if (this.mRemoteException != null) {
                    throw this.mRemoteException;
                }
            }
            if (!this.mHasActivityStarted) {
                com.android.server.wm.RootWindowContainer.this.ensureActivitiesVisible();
            }
            return this.mHasActivityStarted;
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.wm.Task rootTask) {
            if (this.mRemoteException != null || rootTask.getVisibility(null) == 2) {
                return;
            }
            this.mTop = rootTask.topRunningActivity();
            rootTask.forAllActivities((java.util.function.Predicate<com.android.server.wm.ActivityRecord>) this);
        }

        @Override // java.util.function.Predicate
        public boolean test(com.android.server.wm.ActivityRecord r) {
            if (r.finishing || !r.showToCurrentUser() || !r.visibleIgnoringKeyguard || r.app != null || this.mApp.mUid != r.info.applicationInfo.uid || !this.mApp.mName.equals(r.processName)) {
                return false;
            }
            try {
                r.getWrapper().getExtImpl().setNotifyHotStart(false);
                if (com.android.server.wm.RootWindowContainer.this.mTaskSupervisor.realStartActivityLocked(r, this.mApp, this.mTop == r && r.getTask().canBeResumed(r), true)) {
                    this.mHasActivityStarted = true;
                }
                return false;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.wm.RootWindowContainer.TAG, "Exception in new application when starting activity " + this.mTop, e);
                this.mRemoteException = e;
                return true;
            }
        }
    }
}
