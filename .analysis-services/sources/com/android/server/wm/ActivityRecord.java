package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public final class ActivityRecord extends com.android.server.wm.WindowToken implements com.android.server.wm.WindowManagerService.AppFreezeListener {
    static final java.lang.String ACTIVITY_ICON_SUFFIX = "_activity_icon_";
    private static final float ASPECT_RATIO_ROUNDING_TOLERANCE = 0.005f;
    private static final java.lang.String ATTR_COMPONENTSPECIFIED = "component_specified";
    private static final java.lang.String ATTR_ID = "id";
    private static final java.lang.String ATTR_LAUNCHEDFROMFEATURE = "launched_from_feature";
    private static final java.lang.String ATTR_LAUNCHEDFROMPACKAGE = "launched_from_package";
    private static final java.lang.String ATTR_LAUNCHEDFROMUID = "launched_from_uid";
    private static final java.lang.String ATTR_RESOLVEDTYPE = "resolved_type";
    private static final java.lang.String ATTR_USERID = "user_id";
    private static final int DESTROY_TIMEOUT = 10000;
    static final int FINISH_RESULT_CANCELLED = 0;
    static final int FINISH_RESULT_REMOVED = 2;
    static final int FINISH_RESULT_REQUESTED = 1;
    static final int INVALID_PID = -1;
    static final int LAUNCH_SOURCE_TYPE_APPLICATION = 4;
    static final int LAUNCH_SOURCE_TYPE_HOME = 2;
    static final int LAUNCH_SOURCE_TYPE_SYSTEM = 1;
    static final int LAUNCH_SOURCE_TYPE_SYSTEMUI = 3;
    private static final int LAUNCH_TICK = 500;
    private static final int MAX_STOPPING_TO_FORCE = 3;
    private static final int PAUSE_TIMEOUT = 500;
    private static final int SPLASH_SCREEN_BEHAVIOR_DEFAULT = 0;
    private static final int SPLASH_SCREEN_BEHAVIOR_ICON_PREFERRED = 1;
    static final int STARTING_WINDOW_TYPE_NONE = 0;
    static final int STARTING_WINDOW_TYPE_SNAPSHOT = 1;
    static final int STARTING_WINDOW_TYPE_SPLASH_SCREEN = 2;
    private static final int STOP_TIMEOUT = 11000;
    private static final java.lang.String TAG_INITIAL_CALLER_INFO = "initial_caller_info";
    private static final java.lang.String TAG_INTENT = "intent";
    private static final java.lang.String TAG_PERSISTABLEBUNDLE = "persistable_bundle";
    static final int TRANSFER_SPLASH_SCREEN_ATTACH_TO_CLIENT = 2;
    static final int TRANSFER_SPLASH_SCREEN_COPYING = 1;
    static final int TRANSFER_SPLASH_SCREEN_FINISH = 3;
    static final int TRANSFER_SPLASH_SCREEN_IDLE = 0;
    private static final int TRANSFER_SPLASH_SCREEN_TIMEOUT = 2000;
    private static android.content.pm.ConstrainDisplayApisConfig sConstrainDisplayApisConfig;
    boolean allDrawn;
    public com.android.server.wm.WindowProcessController app;
    com.android.server.am.AppTimeTracker appTimeTracker;
    final android.os.Binder assistToken;
    private final boolean componentSpecified;
    long createTime;
    boolean delayedResume;
    boolean finishing;
    boolean firstWindowDrawn;
    boolean hasBeenLaunched;
    boolean idle;
    boolean immersive;
    volatile boolean inHistory;
    public final android.content.pm.ActivityInfo info;
    final android.os.IBinder initialCallerInfoAccessToken;
    public final android.content.Intent intent;
    private boolean keysPaused;
    long lastLaunchTime;
    long lastVisibleTime;
    int launchCount;
    boolean launchFailed;
    int launchMode;
    long launchTickTime;
    long launchTimeStartOppo;
    final java.lang.String launchedFromFeatureId;
    final java.lang.String launchedFromPackage;
    final int launchedFromPid;
    final int launchedFromUid;
    int lockTaskLaunchMode;
    final android.content.ComponentName mActivityComponent;
    private com.android.server.wm.IActivityRecordExt mActivityRecordExt;
    final com.android.server.wm.ActivityRecordInputSink mActivityRecordInputSink;
    boolean mActivityRecordInputSinkEnabled;
    public com.android.server.wm.IActivityRecordSocExt mActivityRecordSocExt;
    boolean mAllowCrossUidActivitySwitchFromBelow;
    private final boolean mAllowUntrustedEmbeddingStateSharing;
    int mAllowedTouchUid;
    com.android.server.wm.AnimatingActivityRegistry mAnimatingActivityRegistry;
    private final boolean mAppActivityEmbeddingSplitsEnabled;
    boolean mAppStopped;
    final com.android.server.wm.ActivityTaskManagerService mAtmService;
    boolean mAutoEnteringPip;
    final com.android.server.wm.ActivityCallerState mCallerState;
    private boolean mCameraCompatControlClickedByUser;
    private final boolean mCameraCompatControlEnabled;
    private int mCameraCompatControlState;
    private android.os.RemoteCallbackList<android.app.IScreenCaptureObserver> mCaptureCallbacks;
    boolean mClientVisibilityDeferred;
    private final com.android.server.display.color.ColorDisplayService.ColorTransformController mColorTransformController;
    private android.app.ICompatCameraControlCallback mCompatCameraControlCallback;
    private com.android.server.wm.ActivityRecord.CompatDisplayInsets mCompatDisplayInsets;
    private int mConfigurationSeq;
    private boolean mCurrentLaunchCanTurnScreenOn;
    private com.android.server.wm.ActivityRecord.CustomAppTransition mCustomCloseTransition;
    private com.android.server.wm.ActivityRecord.CustomAppTransition mCustomOpenTransition;
    boolean mDeferAnimationFinish;
    private boolean mDeferHidingClient;
    private final java.lang.Runnable mDestroyTimeoutRunnable;
    boolean mDismissKeyguardIfInsecure;
    boolean mEnableRecentsScreenshot;
    boolean mEnteringAnimation;
    private boolean mForceSendResultForMediaProjection;
    private boolean mFreezingScreen;
    boolean mHandleExitSplashScreen;
    int mHandoverLaunchDisplayId;
    com.android.server.wm.TaskDisplayArea mHandoverTaskDisplayArea;
    private java.lang.Boolean mHasDeskResources;
    final boolean mHasSceneTransition;
    private boolean mHaveState;
    private android.os.Bundle mIcicle;
    boolean mImeInsetsFrozenUntilStartInput;
    private boolean mInSizeCompatModeForBounds;
    private boolean mInheritShownWhenLocked;
    private android.view.InputApplicationHandle mInputApplicationHandle;
    long mInputDispatchingTimeoutMillis;
    private boolean mIsAspectRatioApplied;
    private boolean mIsEligibleForFixedOrientationLetterbox;
    boolean mIsExiting;
    private boolean mIsInputDroppedForAnimation;
    boolean mIsSplashScreenWindow;
    private final boolean mIsUserAlwaysVisible;
    private boolean mLastAllDrawn;
    boolean mLastAllReadyAtSync;
    private com.android.server.wm.ActivityRecord.AppSaturationInfo mLastAppSaturationInfo;
    private boolean mLastContainsDismissKeyguardWindow;
    private boolean mLastContainsShowWhenLockedWindow;
    private boolean mLastContainsTurnScreenOnWindow;
    private boolean mLastDeferHidingClient;
    private int mLastDropInputMode;
    private android.os.IBinder mLastEmbeddedParentTfTokenBeforePip;
    boolean mLastImeShown;
    android.content.Intent mLastNewIntent;
    private com.android.server.wm.Task mLastParentBeforePip;
    private final android.window.ActivityWindowInfo mLastReportedActivityWindowInfo;
    private final android.util.MergedConfiguration mLastReportedConfiguration;
    private int mLastReportedDisplayId;
    boolean mLastReportedMultiWindowMode;
    boolean mLastReportedPictureInPictureMode;
    boolean mLastSurfaceShowing;
    android.window.ITaskFragmentOrganizer mLastTaskFragmentOrganizerBeforePip;
    private long mLastTransactionSequence;
    android.os.IBinder mLaunchCookie;
    private com.android.server.wm.ActivityRecord mLaunchIntoPipHostActivity;
    android.window.WindowContainerToken mLaunchRootTask;
    int mLaunchSourceType;
    private final java.lang.Runnable mLaunchTickRunnable;
    private boolean mLaunchedFromBubble;
    private android.graphics.Rect mLetterboxBoundsForAspectRatio;
    private android.graphics.Rect mLetterboxBoundsForFixedOrientationAndAspectRatio;
    final com.android.server.wm.LetterboxUiController mLetterboxUiController;
    private android.content.LocusId mLocusId;
    private boolean mNeedsLetterboxedAnimation;
    private int mNumDrawnWindows;
    private int mNumInterestingWindows;
    private boolean mOccludesParent;
    final boolean mOptInOnBackInvoked;
    private final boolean mOptOutEdgeToEdge;
    boolean mOverrideTaskTransition;
    int mPauseConfigurationDispatchCount;
    boolean mPauseSchedulePendingForPip;
    private final java.lang.Runnable mPauseTimeoutRunnable;
    private android.app.ActivityOptions mPendingOptions;
    int mPendingRelaunchCount;
    android.view.RemoteAnimationAdapter mPendingRemoteAnimation;
    private android.window.RemoteTransition mPendingRemoteTransition;
    private android.os.PersistableBundle mPersistentState;
    int mRelaunchReason;
    long mRelaunchStartTime;
    private android.view.RemoteAnimationDefinition mRemoteAnimationDefinition;
    private boolean mRemovingFromDisplay;
    private boolean mReportedDrawn;
    private final com.android.server.wm.WindowState.UpdateReportedVisibilityResults mReportedVisibilityResults;
    boolean mRequestForceTransition;
    android.os.IBinder mRequestedLaunchingTaskFragmentToken;
    final com.android.server.wm.TaskFragment.ConfigOverrideHint mResolveConfigHint;
    final com.android.server.wm.RootWindowContainer mRootWindowContainer;
    int mRotationAnimationHint;
    com.android.server.wm.ActivityServiceConnectionsHolder mServiceConnectionsHolder;
    boolean mShareIdentity;
    final boolean mShowForAllUsers;
    private boolean mShowWhenLocked;
    private android.graphics.Rect mSizeCompatBounds;
    private float mSizeCompatScale;
    private android.window.SizeConfigurationBuckets mSizeConfigurations;
    boolean mSplashScreenStyleSolidColor;
    com.android.server.wm.StartingData mStartingData;
    com.android.server.wm.StartingSurfaceController.StartingSurface mStartingSurface;
    com.android.server.wm.WindowState mStartingWindow;
    private com.android.server.wm.ActivityRecord.State mState;
    private final java.lang.Runnable mStopTimeoutRunnable;
    final boolean mStyleFillsParent;
    int mTargetSdk;
    private boolean mTaskOverlay;
    final com.android.server.wm.ActivityTaskSupervisor mTaskSupervisor;
    private android.os.ITheiaManagerExt mTheiaManagerExt;
    private final android.window.ActivityWindowInfo mTmpActivityWindowInfo;
    private final android.graphics.Rect mTmpBounds;
    private final android.content.res.Configuration mTmpConfig;
    private final java.lang.Runnable mTransferSplashScreenTimeoutRunnable;
    int mTransferringSplashScreenState;
    int mTransitionChangeFlags;
    final com.android.server.wm.TransparentPolicy mTransparentPolicy;
    private boolean mTurnScreenOn;
    final int mUserId;
    private boolean mVisible;
    volatile boolean mVisibleForServiceConnection;
    private boolean mVisibleSetFromTransferredStartingWindow;
    boolean mVoiceInteraction;
    boolean mWaitForEnteringPinnedMode;
    private boolean mWillCloseOrEnterPip;
    private com.android.server.wm.IActivityRecordWrapper mWrapper;
    public com.android.server.zenmode.IZenModeManagerExt mZenModeManagerExt;
    java.util.ArrayList<com.android.internal.content.ReferrerIntent> newIntents;
    boolean noDisplay;
    boolean nowVisible;
    public final java.lang.String packageName;
    long pauseTime;
    java.util.HashSet<java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord>> pendingResults;
    boolean pendingVoiceInteractionStart;
    android.app.PictureInPictureParams pictureInPictureArgs;
    final java.lang.String processName;
    boolean reportedVisible;
    final int requestCode;
    android.content.ComponentName requestedVrComponent;
    final java.lang.String resolvedType;
    com.android.server.wm.ActivityRecord resultTo;
    final java.lang.String resultWho;
    java.util.ArrayList<android.app.ResultInfo> results;
    android.app.ActivityOptions returningOptions;
    final boolean rootVoiceInteraction;
    final android.os.Binder shareableActivityToken;
    final java.lang.String shortComponentName;
    boolean shouldDockBigOverlays;
    boolean startingMoved;
    final boolean stateNotNeeded;
    boolean supportsEnterPipOnTaskSwitch;
    private com.android.server.wm.Task task;
    final java.lang.String taskAffinity;
    android.app.ActivityManager.TaskDescription taskDescription;
    private final int theme;
    long topResumedStateLossTime;
    com.android.server.uri.UriPermissionOwner uriPermissions;
    boolean visibleIgnoringKeyguard;
    android.service.voice.IVoiceInteractionSession voiceSession;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static final java.lang.String TAG_ADD_REMOVE = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_ADD_REMOVE;
    private static final java.lang.String TAG_APP = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_APP;
    private static final java.lang.String TAG_CONFIGURATION = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_CONFIGURATION;
    private static final java.lang.String TAG_CONTAINERS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_CONTAINERS;
    private static final java.lang.String TAG_FOCUS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_FOCUS;
    private static final java.lang.String TAG_PAUSE = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_PAUSE;
    private static final java.lang.String TAG_RESULTS = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_RESULTS;
    private static final java.lang.String TAG_SAVED_STATE = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_SAVED_STATE;
    private static final java.lang.String TAG_STATES = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_STATES;
    private static final java.lang.String TAG_SWITCH = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_SWITCH;
    private static final java.lang.String TAG_TRANSITION = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_TRANSITION;
    private static final java.lang.String TAG_USER_LEAVING = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_USER_LEAVING;
    private static final java.lang.String TAG_VISIBILITY = TAG + com.android.server.wm.ActivityTaskManagerDebugConfig.POSTFIX_VISIBILITY;
    private static boolean DEBUG_PANIC = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);

    @interface FinishRequest {
    }

    @interface LaunchSourceType {
    }

    @interface SplashScreenBehavior {
    }

    enum State {
        INITIALIZING,
        STARTED,
        RESUMED,
        PAUSING,
        PAUSED,
        STOPPING,
        STOPPED,
        FINISHING,
        DESTROYING,
        DESTROYED,
        RESTARTING_PROCESS
    }

    @interface TransferSplashScreenState {
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

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public /* bridge */ /* synthetic */ void onRequestedOverrideConfigurationChanged(android.content.res.Configuration configuration) {
        super.onRequestedOverrideConfigurationChanged(configuration);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public /* bridge */ /* synthetic */ void onUnfrozen() {
        super.onUnfrozen();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(final float[] matrix, final float[] translation) {
        this.mWmService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0(matrix, translation);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(float[] matrix, float[] translation) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mLastAppSaturationInfo == null) {
                    this.mLastAppSaturationInfo = new com.android.server.wm.ActivityRecord.AppSaturationInfo();
                }
                this.mLastAppSaturationInfo.setSaturation(matrix, translation);
                updateColorTransform();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    @dalvik.annotation.optimization.NeverCompile
    void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        java.lang.String str;
        long now = android.os.SystemClock.uptimeMillis();
        pw.print(prefix);
        pw.print("packageName=");
        pw.print(this.packageName);
        pw.print(" processName=");
        pw.println(this.processName);
        pw.print(prefix);
        pw.print("launchedFromUid=");
        pw.print(this.launchedFromUid);
        pw.print(" launchedFromPackage=");
        pw.print(this.launchedFromPackage);
        pw.print(" launchedFromFeature=");
        pw.print(this.launchedFromFeatureId);
        pw.print(" userId=");
        pw.println(this.mUserId);
        pw.print(prefix);
        pw.print("app=");
        pw.println(this.app);
        pw.print(prefix);
        pw.println(this.intent.toInsecureString());
        pw.print(prefix);
        pw.print("rootOfTask=");
        pw.print(isRootOfTask());
        pw.print(" task=");
        pw.println(this.task);
        pw.print(prefix);
        pw.print("taskAffinity=");
        pw.println(this.taskAffinity);
        pw.print(prefix);
        pw.print("mActivityComponent=");
        pw.println(this.mActivityComponent.flattenToShortString());
        android.content.pm.ApplicationInfo appInfo = this.info.applicationInfo;
        pw.print(prefix);
        pw.print("baseDir=");
        pw.println(appInfo.sourceDir);
        if (!java.util.Objects.equals(appInfo.sourceDir, appInfo.publicSourceDir)) {
            pw.print(prefix);
            pw.print("resDir=");
            pw.println(appInfo.publicSourceDir);
        }
        pw.print(prefix);
        pw.print("dataDir=");
        pw.println(appInfo.dataDir);
        if (appInfo.splitSourceDirs != null) {
            pw.print(prefix);
            pw.print("splitDir=");
            pw.println(java.util.Arrays.toString(appInfo.splitSourceDirs));
        }
        pw.print(prefix);
        pw.print("stateNotNeeded=");
        pw.print(this.stateNotNeeded);
        pw.print(" componentSpecified=");
        pw.print(this.componentSpecified);
        pw.print(" mActivityType=");
        pw.println(android.app.WindowConfiguration.activityTypeToString(getActivityType()));
        if (this.rootVoiceInteraction) {
            pw.print(prefix);
            pw.print("rootVoiceInteraction=");
            pw.println(this.rootVoiceInteraction);
        }
        pw.print(prefix);
        pw.print("compat=");
        pw.print(this.mAtmService.compatibilityInfoForPackageLocked(this.info.applicationInfo));
        pw.print(" theme=0x");
        pw.println(java.lang.Integer.toHexString(this.theme));
        pw.println(prefix + "mLastReportedConfigurations:");
        this.mLastReportedConfiguration.dump(pw, prefix + "  ");
        if (com.android.window.flags.Flags.activityWindowInfoFlag()) {
            pw.print(prefix);
            pw.print("mLastReportedActivityWindowInfo=");
            pw.println(this.mLastReportedActivityWindowInfo);
        }
        pw.print(prefix);
        pw.print("CurrentConfiguration=");
        pw.println(getConfiguration());
        if (!getRequestedOverrideConfiguration().equals(android.content.res.Configuration.EMPTY)) {
            pw.println(prefix + "RequestedOverrideConfiguration=" + getRequestedOverrideConfiguration());
        }
        if (!getResolvedOverrideConfiguration().equals(getRequestedOverrideConfiguration())) {
            pw.println(prefix + "ResolvedOverrideConfiguration=" + getResolvedOverrideConfiguration());
        }
        if (!matchParentBounds()) {
            pw.println(prefix + "bounds=" + getBounds());
        }
        if (this.resultTo != null || this.resultWho != null) {
            pw.print(prefix);
            pw.print("resultTo=");
            pw.print(this.resultTo);
            pw.print(" resultWho=");
            pw.print(this.resultWho);
            pw.print(" resultCode=");
            pw.println(this.requestCode);
        }
        if (this.taskDescription != null) {
            java.lang.String iconFilename = this.taskDescription.getIconFilename();
            if (iconFilename != null || this.taskDescription.getLabel() != null || this.taskDescription.getPrimaryColor() != 0) {
                pw.print(prefix);
                pw.print("taskDescription:");
                pw.print(" label=\"");
                pw.print(this.taskDescription.getLabel());
                pw.print("\"");
                pw.print(" icon=");
                if (this.taskDescription.getInMemoryIcon() != null) {
                    str = this.taskDescription.getInMemoryIcon().getByteCount() + " bytes";
                } else {
                    str = "null";
                }
                pw.print(str);
                pw.print(" iconResource=");
                pw.print(this.taskDescription.getIconResourcePackage());
                pw.print(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
                pw.print(this.taskDescription.getIconResource());
                pw.print(" iconFilename=");
                pw.print(this.taskDescription.getIconFilename());
                pw.print(" primaryColor=");
                pw.println(java.lang.Integer.toHexString(this.taskDescription.getPrimaryColor()));
                pw.print(prefix);
                pw.print("  backgroundColor=");
                pw.print(java.lang.Integer.toHexString(this.taskDescription.getBackgroundColor()));
                pw.print(" statusBarColor=");
                pw.print(java.lang.Integer.toHexString(this.taskDescription.getStatusBarColor()));
                pw.print(" navigationBarColor=");
                pw.println(java.lang.Integer.toHexString(this.taskDescription.getNavigationBarColor()));
                pw.print(prefix);
                pw.print(" backgroundColorFloating=");
                pw.println(java.lang.Integer.toHexString(this.taskDescription.getBackgroundColorFloating()));
            }
        }
        if (this.results != null) {
            pw.print(prefix);
            pw.print("results=");
            pw.println(this.results);
        }
        if (this.pendingResults != null && this.pendingResults.size() > 0) {
            pw.print(prefix);
            pw.println("Pending Results:");
            java.util.Iterator<java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord>> it = this.pendingResults.iterator();
            while (it.hasNext()) {
                java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord> wpir = it.next();
                com.android.server.am.PendingIntentRecord pir = wpir != null ? wpir.get() : null;
                pw.print(prefix);
                pw.print("  - ");
                if (pir == null) {
                    pw.println("null");
                } else {
                    pw.println(pir);
                    pir.dump(pw, prefix + "    ");
                }
            }
        }
        if (this.newIntents != null && this.newIntents.size() > 0) {
            pw.print(prefix);
            pw.println("Pending New Intents:");
            for (int i = 0; i < this.newIntents.size(); i++) {
                android.content.Intent intent = this.newIntents.get(i);
                pw.print(prefix);
                pw.print("  - ");
                if (intent == null) {
                    pw.println("null");
                } else {
                    pw.println(intent.toShortString(false, true, false, false));
                }
            }
        }
        if (this.mPendingOptions != null) {
            pw.print(prefix);
            pw.print("pendingOptions=");
            pw.println(this.mPendingOptions);
        }
        if (this.mPendingRemoteAnimation != null) {
            pw.print(prefix);
            pw.print("pendingRemoteAnimationCallingPid=");
            pw.println(this.mPendingRemoteAnimation.getCallingPid());
        }
        if (this.mPendingRemoteTransition != null) {
            pw.print(prefix + " pendingRemoteTransition=" + this.mPendingRemoteTransition.getRemoteTransition());
        }
        if (this.appTimeTracker != null) {
            this.appTimeTracker.dumpWithHeader(pw, prefix, false);
        }
        if (this.uriPermissions != null) {
            this.uriPermissions.dump(pw, prefix);
        }
        pw.print(prefix);
        pw.print("launchFailed=");
        pw.print(this.launchFailed);
        pw.print(" launchCount=");
        pw.print(this.launchCount);
        pw.print(" lastLaunchTime=");
        if (this.lastLaunchTime == 0) {
            pw.print("0");
        } else {
            android.util.TimeUtils.formatDuration(this.lastLaunchTime, now, pw);
        }
        pw.println();
        if (this.mLaunchCookie != null) {
            pw.print(prefix);
            pw.print("launchCookie=");
            pw.println(this.mLaunchCookie);
        }
        if (this.mLaunchRootTask != null) {
            pw.print(prefix);
            pw.print("mLaunchRootTask=");
            pw.println(this.mLaunchRootTask);
        }
        pw.print(prefix);
        pw.print("mHaveState=");
        pw.print(this.mHaveState);
        pw.print(" mIcicle=");
        pw.println(this.mIcicle);
        pw.print(prefix);
        pw.print("state=");
        pw.print(this.mState);
        pw.print(" delayedResume=");
        pw.print(this.delayedResume);
        pw.print(" finishing=");
        pw.println(this.finishing);
        pw.print(prefix);
        pw.print("keysPaused=");
        pw.print(this.keysPaused);
        pw.print(" inHistory=");
        pw.print(this.inHistory);
        pw.print(" idle=");
        pw.println(this.idle);
        pw.print(prefix);
        pw.print("occludesParent=");
        pw.print(occludesParent());
        pw.print(" noDisplay=");
        pw.print(this.noDisplay);
        pw.print(" immersive=");
        pw.print(this.immersive);
        pw.print(" launchMode=");
        pw.println(this.launchMode);
        pw.print(prefix);
        pw.print("mActivityType=");
        pw.println(android.app.WindowConfiguration.activityTypeToString(getActivityType()));
        pw.print(prefix);
        pw.print("mImeInsetsFrozenUntilStartInput=");
        pw.println(this.mImeInsetsFrozenUntilStartInput);
        if (this.requestedVrComponent != null) {
            pw.print(prefix);
            pw.print("requestedVrComponent=");
            pw.println(this.requestedVrComponent);
        }
        super.dump(pw, prefix, dumpAll);
        if (this.mVoiceInteraction) {
            pw.println(prefix + "mVoiceInteraction=true");
        }
        pw.print(prefix);
        pw.print("providesOrientation=");
        pw.println(providesOrientation());
        pw.print(prefix);
        pw.print("mOccludesParent=");
        pw.println(this.mOccludesParent);
        pw.print(prefix);
        pw.print("overrideOrientation=");
        pw.println(android.content.pm.ActivityInfo.screenOrientationToString(getOverrideOrientation()));
        pw.print(prefix);
        pw.print("requestedOrientation=");
        pw.println(android.content.pm.ActivityInfo.screenOrientationToString(super.getOverrideOrientation()));
        pw.println(prefix + "mVisibleRequested=" + this.mVisibleRequested + " mVisible=" + this.mVisible + " mClientVisible=" + isClientVisible() + (this.mDeferHidingClient ? " mDeferHidingClient=" + this.mDeferHidingClient : "") + " reportedDrawn=" + this.mReportedDrawn + " reportedVisible=" + this.reportedVisible);
        if (this.paused) {
            pw.print(prefix);
            pw.print("paused=");
            pw.println(this.paused);
        }
        if (this.mAppStopped) {
            pw.print(prefix);
            pw.print("mAppStopped=");
            pw.println(this.mAppStopped);
        }
        if (this.mNumInterestingWindows != 0 || this.mNumDrawnWindows != 0 || this.allDrawn || this.mLastAllDrawn) {
            pw.print(prefix);
            pw.print("mNumInterestingWindows=");
            pw.print(this.mNumInterestingWindows);
            pw.print(" mNumDrawnWindows=");
            pw.print(this.mNumDrawnWindows);
            pw.print(" allDrawn=");
            pw.print(this.allDrawn);
            pw.print(" lastAllDrawn=");
            pw.print(this.mLastAllDrawn);
            pw.println(")");
        }
        if (this.mStartingData != null || this.firstWindowDrawn || this.mIsExiting) {
            pw.print(prefix);
            pw.print("startingData=");
            pw.print(this.mStartingData);
            pw.print(" firstWindowDrawn=");
            pw.print(this.firstWindowDrawn);
            pw.print(" mIsExiting=");
            pw.println(this.mIsExiting);
        }
        if (this.mStartingWindow != null || this.mStartingData != null || this.mStartingSurface != null || this.startingMoved || this.mVisibleSetFromTransferredStartingWindow) {
            pw.print(prefix);
            pw.print("startingWindow=");
            pw.print(this.mStartingWindow);
            pw.print(" startingSurface=");
            pw.print(this.mStartingSurface);
            pw.print(" startingDisplayed=");
            pw.print(isStartingWindowDisplayed());
            pw.print(" startingMoved=");
            pw.print(this.startingMoved);
            pw.println(" mVisibleSetFromTransferredStartingWindow=" + this.mVisibleSetFromTransferredStartingWindow);
        }
        if (this.mPendingRelaunchCount != 0) {
            pw.print(prefix);
            pw.print("mPendingRelaunchCount=");
            pw.println(this.mPendingRelaunchCount);
        }
        if (this.mSizeCompatScale != 1.0f || this.mSizeCompatBounds != null) {
            pw.println(prefix + "mSizeCompatScale=" + this.mSizeCompatScale + " mSizeCompatBounds=" + this.mSizeCompatBounds);
        }
        if (this.mRemovingFromDisplay) {
            pw.println(prefix + "mRemovingFromDisplay=" + this.mRemovingFromDisplay);
        }
        if (this.lastVisibleTime != 0 || this.nowVisible) {
            pw.print(prefix);
            pw.print("nowVisible=");
            pw.print(this.nowVisible);
            pw.print(" lastVisibleTime=");
            if (this.lastVisibleTime == 0) {
                pw.print("0");
            } else {
                android.util.TimeUtils.formatDuration(this.lastVisibleTime, now, pw);
            }
            pw.println();
        }
        if (this.mDeferHidingClient) {
            pw.println(prefix + "mDeferHidingClient=" + this.mDeferHidingClient);
        }
        if (this.mServiceConnectionsHolder != null) {
            pw.print(prefix);
            pw.print("connections=");
            pw.println(this.mServiceConnectionsHolder);
        }
        if (this.info != null) {
            pw.println(prefix + "resizeMode=" + android.content.pm.ActivityInfo.resizeModeToString(this.info.resizeMode));
            pw.println(prefix + "mLastReportedMultiWindowMode=" + this.mLastReportedMultiWindowMode + " mLastReportedPictureInPictureMode=" + this.mLastReportedPictureInPictureMode);
            if (this.info.supportsPictureInPicture()) {
                pw.println(prefix + "supportsPictureInPicture=" + this.info.supportsPictureInPicture());
                pw.println(prefix + "supportsEnterPipOnTaskSwitch: " + this.supportsEnterPipOnTaskSwitch);
                pw.println(prefix + "mPauseSchedulePendingForPip=" + this.mPauseSchedulePendingForPip);
            }
            if (getMaxAspectRatio() != 0.0f) {
                pw.println(prefix + "maxAspectRatio=" + getMaxAspectRatio());
            }
            float minAspectRatio = getMinAspectRatio();
            if (minAspectRatio != 0.0f) {
                pw.println(prefix + "minAspectRatio=" + minAspectRatio);
            }
            if (minAspectRatio != this.info.getManifestMinAspectRatio()) {
                pw.println(prefix + "manifestMinAspectRatio=" + this.info.getManifestMinAspectRatio());
            }
            pw.println(prefix + "supportsSizeChanges=" + android.content.pm.ActivityInfo.sizeChangesSupportModeToString(supportsSizeChanges()));
            if (this.info.configChanges != 0) {
                pw.println(prefix + "configChanges=0x" + java.lang.Integer.toHexString(this.info.configChanges));
            }
            pw.println(prefix + "neverSandboxDisplayApis=" + this.info.neverSandboxDisplayApis(sConstrainDisplayApisConfig));
            pw.println(prefix + "alwaysSandboxDisplayApis=" + this.info.alwaysSandboxDisplayApis(sConstrainDisplayApisConfig));
        }
        if (this.mLastParentBeforePip != null) {
            pw.println(prefix + "lastParentTaskIdBeforePip=" + this.mLastParentBeforePip.mTaskId);
        }
        if (this.mLaunchIntoPipHostActivity != null) {
            pw.println(prefix + "launchIntoPipHostActivity=" + this.mLaunchIntoPipHostActivity);
        }
        if (this.mWaitForEnteringPinnedMode) {
            pw.print(prefix);
            pw.println("mWaitForEnteringPinnedMode=true");
        }
        this.mLetterboxUiController.dump(pw, prefix);
        pw.println(prefix + "mRootLockActivity=" + this.mActivityRecordExt.getRootLockActivity());
        pw.println(prefix + "mShowWhenLocked=" + this.mShowWhenLocked);
        pw.println(prefix + "mLaunchedFromMultiSearch=" + this.mActivityRecordExt.getLaunchedFromMultiSearch());
        java.lang.Object flexibleActivityInfo = this.mActivityRecordExt.getFlexibleActivityInfo();
        if (flexibleActivityInfo != null) {
            pw.println(prefix + "flexibleActivityInfo=" + flexibleActivityInfo);
        }
        pw.println(prefix + "mCameraCompatControlState=" + android.app.CameraCompatTaskInfo.cameraCompatControlStateToString(this.mCameraCompatControlState));
        pw.println(prefix + "mCameraCompatControlEnabled=" + this.mCameraCompatControlEnabled);
    }

    static boolean dumpActivity(java.io.FileDescriptor fd, java.io.PrintWriter pw, int index, com.android.server.wm.ActivityRecord r, java.lang.String prefix, java.lang.String label, boolean complete, boolean brief, boolean client, java.lang.String dumpPackage, boolean needNL, java.lang.Runnable header, com.android.server.wm.Task lastTask) throws java.lang.Throwable {
        if (dumpPackage != null && !dumpPackage.equals(r.packageName)) {
            return false;
        }
        boolean full = !brief && (complete || !r.isInHistory());
        if (needNL) {
            pw.println("");
        }
        if (header != null) {
            header.run();
        }
        java.lang.String innerPrefix = prefix + "  ";
        java.lang.String[] args = new java.lang.String[0];
        if (lastTask != r.getTask()) {
            com.android.server.wm.Task lastTask2 = r.getTask();
            pw.print(prefix);
            pw.print(full ? "* " : "  ");
            pw.println(lastTask2);
            if (full) {
                lastTask2.dump(pw, prefix + "  ");
            } else if (complete && lastTask2.intent != null) {
                pw.print(prefix);
                pw.print("  ");
                pw.println(lastTask2.intent.toInsecureString());
            }
        }
        pw.print(prefix);
        pw.print(full ? "* " : "    ");
        pw.print(label);
        pw.print(" #");
        pw.print(index);
        pw.print(": ");
        pw.println(r);
        if (full) {
            r.dump(pw, innerPrefix, true);
        } else if (complete) {
            pw.print(innerPrefix);
            pw.println(r.intent.toInsecureString());
            if (r.app != null) {
                pw.print(innerPrefix);
                pw.println(r.app);
            }
        }
        if (client && r.attachedToProcess()) {
            if (r.getWrapper().getExtImpl().isFrozenByHans(r.packageName, r.getUid())) {
                pw.println("\n** this package: " + r.packageName + " has been frozen **");
                return true;
            }
            pw.flush();
            try {
                com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
                try {
                    try {
                        r.app.getThread().dumpActivity(tp.getWriteFd(), r.token, innerPrefix, args);
                        try {
                            tp.go(fd, 2000L);
                            tp.kill();
                        } catch (java.lang.Throwable th) {
                            th = th;
                            tp.kill();
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (android.os.RemoteException e) {
                    pw.println(innerPrefix + "Got a RemoteException while dumping the activity");
                } catch (java.io.IOException e2) {
                    e = e2;
                    pw.println(innerPrefix + "Failure while dumping the activity: " + e);
                }
            } catch (android.os.RemoteException e3) {
            } catch (java.io.IOException e4) {
                e = e4;
            }
        }
        return true;
    }

    void setSavedState(android.os.Bundle savedState) {
        this.mIcicle = savedState;
        this.mHaveState = this.mIcicle != null;
    }

    android.os.Bundle getSavedState() {
        return this.mIcicle;
    }

    boolean hasSavedState() {
        return this.mHaveState;
    }

    android.os.PersistableBundle getPersistentSavedState() {
        return this.mPersistentState;
    }

    void updateApplicationInfo(android.content.pm.ApplicationInfo aInfo) {
        this.info.applicationInfo = aInfo;
    }

    void setSizeConfigurations(android.window.SizeConfigurationBuckets sizeConfigurations) {
        this.mSizeConfigurations = sizeConfigurations;
    }

    private void scheduleActivityMovedToDisplay(int displayId, android.content.res.Configuration config, android.window.ActivityWindowInfo activityWindowInfo) {
        if (!attachedToProcess()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SWITCH_enabled[3]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                long protoLogParam1 = displayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SWITCH, -6509265758887333864L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
                return;
            }
            return;
        }
        try {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SWITCH_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                long protoLogParam12 = displayId;
                java.lang.String protoLogParam2 = java.lang.String.valueOf(config);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SWITCH, -4183059578873561863L, 4, null, protoLogParam02, java.lang.Long.valueOf(protoLogParam12), protoLogParam2);
            }
            this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), android.app.servertransaction.MoveToDisplayItem.obtain(this.token, displayId, config, activityWindowInfo));
        } catch (android.os.RemoteException e) {
        }
    }

    private void scheduleConfigurationChanged(android.content.res.Configuration config, android.window.ActivityWindowInfo activityWindowInfo) {
        if (!attachedToProcess()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[3]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, 7435279034964784633L, 0, null, protoLogParam0);
                return;
            }
            return;
        }
        try {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(config);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -7418876140361338495L, 0, null, protoLogParam02, protoLogParam1);
            }
            if (this.mActivityRecordExt.loggingWhenFolding()) {
                android.util.Slog.d(TAG, "FSS_Sending new config to " + this + ", config: " + config + ", activityWindowInfo: " + activityWindowInfo);
            }
            this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), android.app.servertransaction.ActivityConfigurationChangeItem.obtain(this.token, config, activityWindowInfo));
        } catch (android.os.RemoteException e) {
        }
    }

    boolean scheduleTopResumedActivityChanged(boolean onTop) {
        if (!attachedToProcess()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[3]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -4284934398288119962L, 0, null, protoLogParam0);
            }
            return false;
        }
        if (onTop) {
            this.app.addToPendingTop();
        }
        try {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 7244227111034368231L, 12, null, protoLogParam02, java.lang.Boolean.valueOf(onTop));
            }
            this.mActivityRecordExt.topResumedActivityChanged(this, onTop, this.app);
            this.mActivityRecordExt.hookSetBinderUxFlag(true, this);
            this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), android.app.servertransaction.TopResumedActivityChangeItem.obtain(this.token, onTop));
            this.mActivityRecordExt.hookSetBinderUxFlag(false, this);
            if (onTop) {
                this.mAtmService.mSocExt.onActivityStateChanged(this, onTop);
            }
            return true;
        } catch (android.os.RemoteException e) {
            this.mActivityRecordExt.hookSetBinderUxFlag(false, this);
            android.util.Slog.w(TAG, "Failed to send top-resumed=" + onTop + " to " + this, e);
            return false;
        }
    }

    void updateMultiWindowMode() {
        boolean inMultiWindowMode;
        if (this.task != null && this.task.getRootTask() != null && attachedToProcess() && (inMultiWindowMode = inMultiWindowMode()) != this.mLastReportedMultiWindowMode) {
            if (!inMultiWindowMode && this.mLastReportedPictureInPictureMode) {
                updatePictureInPictureMode(null, false);
            } else {
                this.mLastReportedMultiWindowMode = inMultiWindowMode;
                ensureActivityConfiguration();
            }
        }
    }

    void updatePictureInPictureMode(android.graphics.Rect targetRootTaskBounds, boolean forceUpdate) {
        if (this.task == null || this.task.getRootTask() == null || !attachedToProcess()) {
            return;
        }
        boolean inPictureInPictureMode = inPinnedWindowingMode() && targetRootTaskBounds != null;
        if (inPictureInPictureMode != this.mLastReportedPictureInPictureMode || forceUpdate) {
            this.mLastReportedPictureInPictureMode = inPictureInPictureMode;
            this.mLastReportedMultiWindowMode = inPictureInPictureMode;
            ensureActivityConfiguration(true);
            if (inPictureInPictureMode && findMainWindow() == null && this.task.topRunningActivity() == this) {
                android.util.EventLog.writeEvent(1397638484, "265293293", -1, "");
                removeImmediately();
            }
        }
    }

    com.android.server.wm.Task getTask() {
        return this.task;
    }

    com.android.server.wm.TaskFragment getTaskFragment() {
        com.android.server.wm.WindowContainer parent = getParent();
        if (parent != null) {
            return parent.asTaskFragment();
        }
        return null;
    }

    private boolean shouldStartChangeTransition(com.android.server.wm.TaskFragment newParent, com.android.server.wm.TaskFragment oldParent) {
        if (newParent == null || oldParent == null || !canStartChangeTransition()) {
            return false;
        }
        boolean isInPip2 = com.android.server.wm.ActivityTaskManagerService.isPip2ExperimentEnabled() && inPinnedWindowingMode();
        if (!newParent.isOrganizedTaskFragment() && !isInPip2) {
            return false;
        }
        return !newParent.getBounds().equals(oldParent.getBounds());
    }

    @Override // com.android.server.wm.WindowContainer
    boolean canStartChangeTransition() {
        com.android.server.wm.Task task = getTask();
        return (task == null || task.isDragResizing() || !super.canStartChangeTransition()) ? false : true;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    void onParentChanged(com.android.server.wm.ConfigurationContainer rawNewParent, com.android.server.wm.ConfigurationContainer rawOldParent) {
        com.android.server.wm.TaskFragment oldParent = (com.android.server.wm.TaskFragment) rawOldParent;
        com.android.server.wm.TaskFragment newParent = (com.android.server.wm.TaskFragment) rawNewParent;
        com.android.server.wm.Task oldTask = oldParent != null ? oldParent.getTask() : null;
        com.android.server.wm.Task newTask = newParent != null ? newParent.getTask() : null;
        this.task = newTask;
        if (shouldStartChangeTransition(newParent, oldParent)) {
            if (this.mTransitionController.isShellTransitionsEnabled()) {
                initializeChangeTransition(getBounds());
            } else {
                newParent.initializeChangeTransition(getBounds(), getSurfaceControl());
            }
        }
        if (this.mActivityRecordExt.getRootLockActivity() && newTask != null && newTask.getWrapper().getExtImpl() != null) {
            newTask.getWrapper().getExtImpl().setRootLockDeviceTask(true);
        }
        if (this.mActivityRecordExt.getLaunchedFromMultiSearch() && newTask != null) {
            newTask.getWrapper().getExtImpl().setLaunchedFromMultiSearch(true);
        }
        this.mActivityRecordExt.onActivityRecordParentChanged(oldParent, newParent, this);
        super.onParentChanged(newParent, oldParent);
        if (isPersistable()) {
            if (oldTask != null) {
                this.mAtmService.notifyTaskPersisterLocked(oldTask, false);
            }
            if (newTask != null) {
                this.mAtmService.notifyTaskPersisterLocked(newTask, false);
            }
        }
        if (oldParent == null && newParent != null) {
            this.mVoiceInteraction = newTask.voiceSession != null;
            newTask.updateOverrideConfigurationFromLaunchBounds();
            this.mLastReportedMultiWindowMode = inMultiWindowMode();
            this.mLastReportedPictureInPictureMode = inPinnedWindowingMode();
        }
        if (this.task == null && getDisplayContent() != null) {
            getDisplayContent().mClosingApps.remove(this);
        }
        com.android.server.wm.Task rootTask = getRootTask();
        updateAnimatingActivityRegistry();
        if (this.task == this.mLastParentBeforePip && this.task != null) {
            this.mAtmService.mWindowOrganizerController.mTaskFragmentOrganizerController.onActivityReparentedToTask(this);
            clearLastParentBeforePip();
        }
        if (this.task == this.mActivityRecordExt.getLastParentBeforeSplitScreen() && this.task != null) {
            this.mActivityRecordExt.clearLastParentBeforeSplitScreen();
        }
        if (oldTask != null) {
            oldTask.getWrapper().getExtImpl().notifyChildActivityRecordRemoved(this);
        }
        if (newTask != null) {
            newTask.getWrapper().getExtImpl().notifyChildActivityRecordAdded(this);
        }
        this.mActivityRecordExt.onActivityRecordParentChangedAfter(oldParent, newParent, this);
        updateColorTransform();
        if (oldParent != null) {
            oldParent.cleanUpActivityReferences(this);
            this.mRequestedLaunchingTaskFragmentToken = null;
        }
        if (newParent != null) {
            if (isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
                newParent.setResumedActivity(this, "onParentChanged");
            }
            this.mTransparentPolicy.start();
        }
        if (rootTask != null && rootTask.topRunningActivity() == this && this.firstWindowDrawn) {
            rootTask.setHasBeenVisible(true);
        }
        updateUntrustedEmbeddingInputProtection();
    }

    @Override // com.android.server.wm.WindowContainer
    void setSurfaceControl(android.view.SurfaceControl sc) {
        super.setSurfaceControl(sc);
        if (sc != null) {
            this.mLastDropInputMode = 0;
            updateUntrustedEmbeddingInputProtection();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDropInputForAnimation(boolean isInputDroppedForAnimation) {
        if (this.mIsInputDroppedForAnimation == isInputDroppedForAnimation) {
            return;
        }
        this.mIsInputDroppedForAnimation = isInputDroppedForAnimation;
        updateUntrustedEmbeddingInputProtection();
    }

    private void updateUntrustedEmbeddingInputProtection() {
        if (getSurfaceControl() == null) {
            return;
        }
        if (this.mIsInputDroppedForAnimation) {
            setDropInputMode(1);
        } else if (isEmbeddedInUntrustedMode()) {
            setDropInputMode(2);
        } else {
            setDropInputMode(0);
        }
    }

    void setDropInputMode(int mode) {
        if (this.mLastDropInputMode != mode) {
            this.mLastDropInputMode = mode;
            this.mWmService.mTransactionFactory.get().setDropInputMode(getSurfaceControl(), mode).apply();
        }
    }

    private boolean isEmbeddedInUntrustedMode() {
        com.android.server.wm.TaskFragment organizedTaskFragment = getOrganizedTaskFragment();
        if (organizedTaskFragment == null) {
            return false;
        }
        return !organizedTaskFragment.isAllowedToEmbedActivityInTrustedMode(this);
    }

    void updateAnimatingActivityRegistry() {
        com.android.server.wm.AnimatingActivityRegistry registry;
        com.android.server.wm.Task rootTask = getRootTask();
        if (rootTask != null) {
            registry = rootTask.getAnimatingActivityRegistry();
        } else {
            registry = null;
        }
        if (this.mAnimatingActivityRegistry != null && this.mAnimatingActivityRegistry != registry) {
            this.mAnimatingActivityRegistry.notifyFinished(this);
        }
        this.mAnimatingActivityRegistry = registry;
    }

    boolean canAutoEnterPip() {
        boolean activityCanPip = checkEnterPictureInPictureState("startActivityUnchecked", false);
        return activityCanPip && this.pictureInPictureArgs != null && this.pictureInPictureArgs.isAutoEnterEnabled();
    }

    void setLastParentBeforePip(com.android.server.wm.ActivityRecord launchIntoPipHostActivity) {
        com.android.server.wm.Task task;
        com.android.server.wm.TaskFragment organizedTf;
        android.window.ITaskFragmentOrganizer taskFragmentOrganizer;
        if (launchIntoPipHostActivity == null) {
            task = getTask();
        } else {
            task = launchIntoPipHostActivity.getTask();
        }
        this.mLastParentBeforePip = task;
        this.mLastParentBeforePip.mChildPipActivity = this;
        this.mLaunchIntoPipHostActivity = launchIntoPipHostActivity;
        if (launchIntoPipHostActivity == null) {
            organizedTf = getOrganizedTaskFragment();
        } else {
            organizedTf = launchIntoPipHostActivity.getOrganizedTaskFragment();
        }
        if (organizedTf != null) {
            taskFragmentOrganizer = organizedTf.getTaskFragmentOrganizer();
        } else {
            taskFragmentOrganizer = null;
        }
        this.mLastTaskFragmentOrganizerBeforePip = taskFragmentOrganizer;
        if (organizedTf != null && launchIntoPipHostActivity == null) {
            this.mLastEmbeddedParentTfTokenBeforePip = organizedTf.getFragmentToken();
        }
    }

    void clearLastParentBeforePip() {
        if (this.mLastParentBeforePip != null) {
            this.mLastParentBeforePip.mChildPipActivity = null;
            this.mLastParentBeforePip = null;
        }
        this.mLaunchIntoPipHostActivity = null;
        this.mLastTaskFragmentOrganizerBeforePip = null;
        this.mLastEmbeddedParentTfTokenBeforePip = null;
    }

    com.android.server.wm.Task getLastParentBeforePip() {
        return this.mLastParentBeforePip;
    }

    android.os.IBinder getLastEmbeddedParentTfTokenBeforePip() {
        return this.mLastEmbeddedParentTfTokenBeforePip;
    }

    com.android.server.wm.ActivityRecord getLaunchIntoPipHostActivity() {
        return this.mLaunchIntoPipHostActivity;
    }

    private void updateColorTransform() {
        if (this.mSurfaceControl != null && this.mLastAppSaturationInfo != null) {
            getPendingTransaction().setColorTransform(this.mSurfaceControl, this.mLastAppSaturationInfo.mMatrix, this.mLastAppSaturationInfo.mTranslation);
            this.mWmService.scheduleAnimationLocked();
        }
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    void onDisplayChanged(com.android.server.wm.DisplayContent dc) {
        com.android.server.wm.DisplayContent prevDc = this.mDisplayContent;
        super.onDisplayChanged(dc);
        if (prevDc == this.mDisplayContent) {
            return;
        }
        this.mDisplayContent.onRunningActivityChanged();
        if (prevDc == null) {
            return;
        }
        prevDc.onRunningActivityChanged();
        this.mTransitionController.collect(this);
        if (prevDc.mOpeningApps.remove(this)) {
            if (DEBUG_PANIC) {
                android.util.Slog.d(TAG, "onDisplayChanged, adding " + this + " to mOpeningApps, callers=" + android.os.Debug.getCallers(5));
            }
            this.mDisplayContent.mOpeningApps.add(this);
            this.mDisplayContent.transferAppTransitionFrom(prevDc);
            this.mDisplayContent.executeAppTransition();
        }
        prevDc.mClosingApps.remove(this);
        prevDc.getDisplayPolicy().removeRelaunchingApp(this);
        if (prevDc.mFocusedApp == this) {
            prevDc.setFocusedApp(null);
            if (dc.getTopMostActivity() == this) {
                dc.setFocusedApp(this);
            }
        }
        this.mLetterboxUiController.onMovedToDisplay(this.mDisplayContent.getDisplayId());
        this.mActivityRecordExt.onDisplayChanged();
    }

    void layoutLetterboxIfNeeded(com.android.server.wm.WindowState winHint) {
        this.mLetterboxUiController.layoutLetterboxIfNeeded(winHint);
    }

    boolean hasWallpaperBackgroundForLetterbox() {
        return this.mLetterboxUiController.hasWallpaperBackgroundForLetterbox();
    }

    void updateLetterboxSurfaceIfNeeded(com.android.server.wm.WindowState winHint, android.view.SurfaceControl.Transaction t) {
        this.mLetterboxUiController.updateLetterboxSurfaceIfNeeded(winHint, t, getPendingTransaction());
    }

    void updateLetterboxSurfaceIfNeeded(com.android.server.wm.WindowState winHint) {
        this.mLetterboxUiController.updateLetterboxSurfaceIfNeeded(winHint);
    }

    android.graphics.Rect getLetterboxInsets() {
        return this.mLetterboxUiController.getLetterboxInsets();
    }

    void getLetterboxInnerBounds(android.graphics.Rect outBounds) {
        this.mLetterboxUiController.getLetterboxInnerBounds(outBounds);
    }

    void updateCameraCompatState(boolean showControl, boolean transformationApplied, android.app.ICompatCameraControlCallback callback) {
        int newCameraCompatControlState;
        if (!isCameraCompatControlEnabled()) {
            return;
        }
        if (this.mCameraCompatControlClickedByUser && (showControl || this.mCameraCompatControlState == 3)) {
            return;
        }
        this.mCompatCameraControlCallback = callback;
        if (!showControl) {
            newCameraCompatControlState = 0;
        } else if (transformationApplied) {
            newCameraCompatControlState = 2;
        } else {
            newCameraCompatControlState = 1;
        }
        boolean changed = setCameraCompatControlState(newCameraCompatControlState);
        if (!changed) {
            return;
        }
        this.mTaskSupervisor.getActivityMetricsLogger().logCameraCompatControlAppearedEventReported(newCameraCompatControlState, this.info.applicationInfo.uid);
        if (newCameraCompatControlState == 0) {
            this.mCameraCompatControlClickedByUser = false;
            this.mCompatCameraControlCallback = null;
        }
        getTask().dispatchTaskInfoChangedIfNeeded(true);
        getDisplayContent().setLayoutNeeded();
        this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
    }

    void updateCameraCompatStateFromUser(int state) {
        if (isCameraCompatControlEnabled()) {
            if (state == 0) {
                android.util.Slog.w(TAG, "Unexpected hidden state in updateCameraCompatState");
                return;
            }
            boolean changed = setCameraCompatControlState(state);
            this.mCameraCompatControlClickedByUser = true;
            if (!changed) {
                return;
            }
            this.mTaskSupervisor.getActivityMetricsLogger().logCameraCompatControlClickedEventReported(state, this.info.applicationInfo.uid);
            if (state == 3) {
                this.mCompatCameraControlCallback = null;
                return;
            }
            if (this.mCompatCameraControlCallback == null) {
                android.util.Slog.w(TAG, "Callback for a camera compat control is null");
                return;
            }
            try {
                if (state == 2) {
                    this.mCompatCameraControlCallback.applyCameraCompatTreatment();
                } else {
                    this.mCompatCameraControlCallback.revertCameraCompatTreatment();
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Unable to apply or revert camera compat treatment", e);
            }
        }
    }

    private boolean setCameraCompatControlState(int state) {
        if (!isCameraCompatControlEnabled() || this.mCameraCompatControlState == state) {
            return false;
        }
        this.mCameraCompatControlState = state;
        return true;
    }

    int getCameraCompatControlState() {
        return this.mCameraCompatControlState;
    }

    boolean isCameraCompatControlEnabled() {
        return this.mCameraCompatControlEnabled;
    }

    boolean isFullyTransparentBarAllowed(android.graphics.Rect rect) {
        return this.mLetterboxUiController.isFullyTransparentBarAllowed(rect);
    }

    private static class Token extends android.os.Binder {
        java.lang.ref.WeakReference<com.android.server.wm.ActivityRecord> mActivityRef;

        private Token() {
        }

        public java.lang.String toString() {
            return "Token{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + this.mActivityRef.get() + "}";
        }
    }

    static com.android.server.wm.ActivityRecord forToken(android.os.IBinder token) {
        if (token == null) {
            return null;
        }
        try {
            com.android.server.wm.ActivityRecord.Token activityToken = (com.android.server.wm.ActivityRecord.Token) token;
            return activityToken.mActivityRef.get();
        } catch (java.lang.ClassCastException e) {
            android.util.Slog.w(TAG, "Bad activity token: " + token, e);
            return null;
        }
    }

    static com.android.server.wm.ActivityRecord forTokenLocked(android.os.IBinder token) {
        com.android.server.wm.ActivityRecord r = forToken(token);
        if (r == null || r.getRootTask() == null) {
            return null;
        }
        return r;
    }

    static boolean isResolverActivity(java.lang.String className) {
        return com.android.internal.app.ResolverActivity.class.getName().equals(className);
    }

    boolean isResolverOrDelegateActivity() {
        return isResolverActivity(this.mActivityComponent.getClassName()) || java.util.Objects.equals(this.mActivityComponent, this.mAtmService.mTaskSupervisor.getSystemChooserActivity());
    }

    boolean isResolverOrChildActivity() {
        if (!com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(this.packageName)) {
            return false;
        }
        try {
            return com.android.internal.app.ResolverActivity.class.isAssignableFrom(java.lang.Object.class.getClassLoader().loadClass(this.mActivityComponent.getClassName()));
        } catch (java.lang.ClassNotFoundException e) {
            return false;
        }
    }

    boolean hasCaller(android.os.IBinder callerToken) {
        return this.mCallerState.hasCaller(callerToken);
    }

    int getCallerUid(android.os.IBinder callerToken) {
        return this.mCallerState.getUid(callerToken);
    }

    java.lang.String getCallerPackage(android.os.IBinder callerToken) {
        return this.mCallerState.getPackage(callerToken);
    }

    boolean isCallerShareIdentityEnabled(android.os.IBinder callerToken) {
        return this.mCallerState.isShareIdentityEnabled(callerToken);
    }

    void computeInitialCallerInfo() {
        computeCallerInfo(this.initialCallerInfoAccessToken, this.intent, this.launchedFromUid, this.launchedFromPackage, this.mShareIdentity);
    }

    void computeCallerInfo(android.os.IBinder callerToken, android.content.Intent intent, int callerUid, java.lang.String callerPackageName, boolean isCallerShareIdentityEnabled) {
        this.mCallerState.computeCallerInfo(callerToken, intent, callerUid, callerPackageName, isCallerShareIdentityEnabled);
    }

    boolean checkContentUriPermission(android.os.IBinder callerToken, com.android.server.uri.GrantUri grantUri, int modeFlags) {
        return this.mCallerState.checkContentUriPermission(callerToken, grantUri, modeFlags);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ActivityRecord(com.android.server.wm.ActivityTaskManagerService activityTaskManagerService, com.android.server.wm.WindowProcessController windowProcessController, int i, int i2, java.lang.String str, java.lang.String str2, android.content.Intent intent, java.lang.String str3, android.content.pm.ActivityInfo activityInfo, android.content.res.Configuration configuration, com.android.server.wm.ActivityRecord activityRecord, java.lang.String str4, int i3, boolean z, boolean z2, com.android.server.wm.ActivityTaskSupervisor activityTaskSupervisor, android.app.ActivityOptions activityOptions, com.android.server.wm.ActivityRecord activityRecord2, android.os.PersistableBundle persistableBundle, android.app.ActivityManager.TaskDescription taskDescription, long j) {
        int i4;
        java.lang.String str5;
        super(activityTaskManagerService.mWindowManager, new com.android.server.wm.ActivityRecord.Token(), 2, true, null, false);
        this.mHandoverLaunchDisplayId = -1;
        this.mActivityRecordSocExt = (com.android.server.wm.IActivityRecordSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityRecordSocExt.class).base(this).create();
        this.createTime = java.lang.System.currentTimeMillis();
        this.mLastReportedActivityWindowInfo = new android.window.ActivityWindowInfo();
        this.mHaveState = true;
        this.pictureInPictureArgs = new android.app.PictureInPictureParams.Builder().build();
        this.mSplashScreenStyleSolidColor = false;
        this.mPauseSchedulePendingForPip = false;
        this.mAutoEnteringPip = false;
        this.mTaskOverlay = false;
        this.mRelaunchReason = 0;
        this.mForceSendResultForMediaProjection = false;
        this.mRemovingFromDisplay = false;
        this.mReportedVisibilityResults = new com.android.server.wm.WindowState.UpdateReportedVisibilityResults();
        this.mCurrentLaunchCanTurnScreenOn = true;
        this.mInputDispatchingTimeoutMillis = android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        this.mLastTransactionSequence = Long.MIN_VALUE;
        this.mLastAllReadyAtSync = false;
        this.mSizeCompatScale = 1.0f;
        this.mInSizeCompatModeForBounds = false;
        this.mIsAspectRatioApplied = false;
        this.mCameraCompatControlState = 0;
        this.mEnableRecentsScreenshot = true;
        this.mLastDropInputMode = 0;
        this.mTransferringSplashScreenState = 0;
        this.mRotationAnimationHint = -1;
        this.mColorTransformController = new com.android.server.display.color.ColorDisplayService.ColorTransformController() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda28
            @Override // com.android.server.display.color.ColorDisplayService.ColorTransformController
            public final void applyAppSaturation(float[] fArr, float[] fArr2) {
                this.f$0.lambda$new$1(fArr, fArr2);
            }
        };
        this.mTmpConfig = new android.content.res.Configuration();
        this.mTmpBounds = new android.graphics.Rect();
        this.mTmpActivityWindowInfo = new android.window.ActivityWindowInfo();
        this.assistToken = new android.os.Binder();
        this.shareableActivityToken = new android.os.Binder();
        this.initialCallerInfoAccessToken = new android.os.Binder();
        this.mZenModeManagerExt = (com.android.server.zenmode.IZenModeManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.zenmode.IZenModeManagerExt.class).create();
        this.mActivityRecordInputSinkEnabled = true;
        this.mTheiaManagerExt = (android.os.ITheiaManagerExt) system.ext.loader.core.ExtLoader.type(android.os.ITheiaManagerExt.class).create();
        this.mPauseConfigurationDispatchCount = 0;
        this.mPauseTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRecord.1
            @Override // java.lang.Runnable
            public void run() {
                android.util.Slog.w(com.android.server.wm.ActivityRecord.TAG, "Activity pause timeout for " + com.android.server.wm.ActivityRecord.this);
                long ts = android.os.SystemClock.uptimeMillis();
                com.android.server.wm.ActivityRecord.this.mTheiaManagerExt.sendEvent(2L, ts, com.android.server.wm.ActivityRecord.this.getPid(), com.android.server.wm.ActivityRecord.this.getUid(), 4099L, com.android.server.wm.ActivityRecord.this.packageName);
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityRecord.this.mAtmService.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        if (!com.android.server.wm.ActivityRecord.this.hasProcess()) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        com.android.server.wm.ActivityRecord.this.mAtmService.logAppTooSlow(com.android.server.wm.ActivityRecord.this.app, com.android.server.wm.ActivityRecord.this.pauseTime, "pausing " + com.android.server.wm.ActivityRecord.this);
                        com.android.server.wm.ActivityRecord.this.activityPaused(true);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            }
        };
        this.mLaunchTickRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRecord.2
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityRecord.this.mAtmService.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        if (com.android.server.wm.ActivityRecord.this.continueLaunchTicking()) {
                            com.android.server.wm.ActivityRecord.this.mAtmService.logAppTooSlow(com.android.server.wm.ActivityRecord.this.app, com.android.server.wm.ActivityRecord.this.launchTickTime, "launching " + com.android.server.wm.ActivityRecord.this);
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
        };
        this.mDestroyTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRecord.3
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityRecord.this.mAtmService.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        android.util.Slog.w(com.android.server.wm.ActivityRecord.TAG, "Activity destroy timeout for " + com.android.server.wm.ActivityRecord.this);
                        com.android.server.wm.ActivityRecord.this.destroyed("destroyTimeout");
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
        };
        this.mStopTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRecord.4
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityRecord.this.mAtmService.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        android.util.Slog.w(com.android.server.wm.ActivityRecord.TAG, "Activity stop timeout for " + com.android.server.wm.ActivityRecord.this);
                        if (com.android.server.wm.ActivityRecord.this.isInHistory()) {
                            com.android.server.wm.ActivityRecord.this.activityStopped(null, null, null);
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
        };
        this.mTransferSplashScreenTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRecord.5
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ActivityRecord.this.mAtmService.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        android.util.Slog.w(com.android.server.wm.ActivityRecord.TAG, "Activity transferring splash screen timeout for " + com.android.server.wm.ActivityRecord.this + " state " + com.android.server.wm.ActivityRecord.this.mTransferringSplashScreenState);
                        if (com.android.server.wm.ActivityRecord.this.isTransferringSplashScreen()) {
                            com.android.server.wm.ActivityRecord.this.mTransferringSplashScreenState = 3;
                            com.android.server.wm.ActivityRecord.this.removeStartingWindow();
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
        };
        this.mWrapper = new com.android.server.wm.ActivityRecord.ActivityRecordWrapper();
        this.mActivityRecordExt = (com.android.server.wm.IActivityRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityRecordExt.class).base(this).create();
        this.mAtmService = activityTaskManagerService;
        ((com.android.server.wm.ActivityRecord.Token) this.token).mActivityRef = new java.lang.ref.WeakReference<>(this);
        this.info = activityInfo;
        this.mUserId = android.os.UserHandle.getUserId(this.info.applicationInfo.uid);
        this.packageName = this.info.applicationInfo.packageName;
        this.intent = intent;
        if (this.info.targetActivity == null || (this.info.targetActivity.equals(this.intent.getComponent().getClassName()) && (this.info.launchMode == 0 || this.info.launchMode == 1))) {
            this.mActivityComponent = this.intent.getComponent();
        } else {
            this.mActivityComponent = new android.content.ComponentName(this.info.packageName, this.info.targetActivity);
        }
        this.mTransparentPolicy = new com.android.server.wm.TransparentPolicy(this, this.mWmService.mLetterboxConfiguration);
        this.mLetterboxUiController = new com.android.server.wm.LetterboxUiController(this.mWmService, this);
        this.mCameraCompatControlEnabled = this.mWmService.mContext.getResources().getBoolean(android.R.bool.config_gnssLocationOverlayUnstableFallback);
        this.mResolveConfigHint = new com.android.server.wm.TaskFragment.ConfigOverrideHint();
        if (this.mWmService.mFlags.mInsetsDecoupledConfiguration) {
            this.mResolveConfigHint.mUseOverrideInsetsForConfig = (this.info.isChangeEnabled(151861875L) || this.info.isChangeEnabled(327313645L)) ? false : true;
        } else {
            this.mResolveConfigHint.mUseOverrideInsetsForConfig = this.info.isChangeEnabled(327313645L);
        }
        this.mTargetSdk = this.info.applicationInfo.targetSdkVersion;
        android.content.pm.UserProperties userProperties = ((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getUserProperties(this.mUserId);
        this.mIsUserAlwaysVisible = userProperties != null && userProperties.getAlwaysVisible();
        this.mShowForAllUsers = (this.info.flags & 1024) != 0 || this.mIsUserAlwaysVisible;
        setOrientation(this.mActivityRecordExt.onActivityRecordOrientationInit(this, this.info.screenOrientation));
        this.mRotationAnimationHint = this.info.rotationAnimation;
        this.mShowWhenLocked = (activityInfo.flags & 8388608) != 0;
        this.mInheritShownWhenLocked = (activityInfo.privateFlags & 1) != 0;
        this.mTurnScreenOn = (activityInfo.flags & 16777216) != 0;
        int themeResource = this.info.getThemeResource();
        if (themeResource != 0) {
            i4 = themeResource;
        } else {
            i4 = activityInfo.applicationInfo.targetSdkVersion < 11 ? android.R.style.Theme : android.R.style.Theme.Holo;
        }
        com.android.internal.policy.AttributeCache.Entry entry = com.android.internal.policy.AttributeCache.instance().get(this.packageName, i4, com.android.internal.R.styleable.Window, this.mUserId);
        if (entry != null) {
            this.mOccludesParent = !android.content.pm.ActivityInfo.isTranslucentOrFloating(entry.array) || entry.array.getBoolean(14, false);
            if (!this.mOccludesParent) {
                this.mOccludesParent = this.mActivityRecordExt.makeActivityLanchFromLauncherOccludesParentIfNeed(this, str);
            }
            this.mStyleFillsParent = this.mOccludesParent;
            this.noDisplay = entry.array.getBoolean(10, false);
            this.mActivityRecordExt.updateCompactFullScreenWindow(this, entry, i4);
            this.mOptOutEdgeToEdge = entry.array.getBoolean(63, false);
        } else {
            this.mOccludesParent = true;
            this.mStyleFillsParent = true;
            this.noDisplay = false;
            this.mOptOutEdgeToEdge = false;
        }
        if (activityOptions != null) {
            this.mLaunchTaskBehind = activityOptions.getLaunchTaskBehind();
            int rotationAnimationHint = activityOptions.getRotationAnimationHint();
            if (rotationAnimationHint >= 0) {
                this.mRotationAnimationHint = rotationAnimationHint;
            }
            if (activityOptions.getLaunchIntoPipParams() != null) {
                this.pictureInPictureArgs = activityOptions.getLaunchIntoPipParams();
                if (activityRecord2 != null) {
                    adjustPictureInPictureParamsIfNeeded(activityRecord2.getBounds());
                }
            }
            this.mOverrideTaskTransition = activityOptions.getOverrideTaskTransition();
            this.mDismissKeyguardIfInsecure = activityOptions.getDismissKeyguardIfInsecure();
            this.mShareIdentity = activityOptions.isShareIdentityEnabled();
        }
        ((com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal) com.android.server.LocalServices.getService(com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal.class)).attachColorTransformController(this.packageName, this.mUserId, new java.lang.ref.WeakReference<>(this.mColorTransformController));
        this.mRootWindowContainer = activityTaskManagerService.mRootWindowContainer;
        this.launchedFromPid = i;
        this.launchedFromUid = i2;
        this.launchedFromPackage = str;
        this.launchedFromFeatureId = str2;
        this.mLaunchSourceType = determineLaunchSourceType(i2, windowProcessController);
        this.shortComponentName = intent.getComponent().flattenToShortString();
        this.resolvedType = str3;
        this.componentSpecified = z;
        this.rootVoiceInteraction = z2;
        this.mLastReportedConfiguration = new android.util.MergedConfiguration(configuration);
        this.resultTo = activityRecord;
        this.resultWho = str4;
        this.requestCode = i3;
        setState(com.android.server.wm.ActivityRecord.State.INITIALIZING, "ActivityRecord ctor");
        callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.INITIALIZING, true);
        this.launchFailed = false;
        this.delayedResume = false;
        this.finishing = false;
        this.keysPaused = false;
        this.inHistory = false;
        this.nowVisible = false;
        super.setClientVisible(true);
        this.idle = false;
        this.hasBeenLaunched = false;
        this.mActivityRecordSocExt.initSoc();
        this.mTaskSupervisor = activityTaskSupervisor;
        this.info.taskAffinity = computeTaskAffinity(this.info.taskAffinity, this.info.applicationInfo.uid);
        this.taskAffinity = this.info.taskAffinity;
        java.lang.String string = java.lang.Integer.toString(this.info.applicationInfo.uid);
        if (this.info.windowLayout != null && this.info.windowLayout.windowLayoutAffinity != null && !this.info.windowLayout.windowLayoutAffinity.startsWith(string)) {
            str5 = string;
            this.info.windowLayout.windowLayoutAffinity = string + ":" + this.info.windowLayout.windowLayoutAffinity;
        } else {
            str5 = string;
        }
        if (sConstrainDisplayApisConfig == null) {
            sConstrainDisplayApisConfig = new android.content.pm.ConstrainDisplayApisConfig();
        }
        this.stateNotNeeded = (activityInfo.flags & 16) != 0;
        this.theme = activityInfo.getThemeResource();
        if ((activityInfo.flags & 1) == 0 || windowProcessController == null || (activityInfo.applicationInfo.uid != 1000 && activityInfo.applicationInfo.uid != windowProcessController.mInfo.uid)) {
            this.processName = activityInfo.processName;
        } else {
            this.processName = windowProcessController.mName;
        }
        if ((activityInfo.flags & 32) != 0) {
            this.intent.addFlags(8388608);
        }
        this.launchMode = activityInfo.launchMode;
        setActivityType(z, i2, intent, activityOptions, activityRecord2);
        this.immersive = (activityInfo.flags & 2048) != 0;
        this.requestedVrComponent = activityInfo.requestedVrComponent == null ? null : android.content.ComponentName.unflattenFromString(activityInfo.requestedVrComponent);
        this.lockTaskLaunchMode = getLockTaskLaunchMode(activityInfo, activityOptions);
        if (activityOptions != null) {
            setOptions(activityOptions);
            this.mHasSceneTransition = (activityOptions.getAnimationType() != 5 || activityOptions.getSceneTransitionInfo() == null || activityOptions.getSceneTransitionInfo().getResultReceiver() == null) ? false : true;
            android.app.PendingIntent usageTimeReport = activityOptions.getUsageTimeReport();
            if (usageTimeReport != null) {
                this.appTimeTracker = new com.android.server.am.AppTimeTracker(usageTimeReport);
            }
            android.window.WindowContainerToken launchTaskDisplayArea = activityOptions.getLaunchTaskDisplayArea();
            this.mHandoverTaskDisplayArea = launchTaskDisplayArea != null ? (com.android.server.wm.TaskDisplayArea) com.android.server.wm.WindowContainer.fromBinder(launchTaskDisplayArea.asBinder()) : null;
            this.mHandoverLaunchDisplayId = activityOptions.getLaunchDisplayId();
            this.mLaunchCookie = activityOptions.getLaunchCookie();
            this.mLaunchRootTask = activityOptions.getLaunchRootTask();
            this.mActivityRecordExt.setLaunchedFromMultiSearch(activityOptions.getLaunchedFromMultiSearch());
            this.mActivityRecordExt.parseLaunchOptions(activityOptions);
        } else {
            this.mHasSceneTransition = false;
        }
        this.mPersistentState = persistableBundle;
        this.taskDescription = taskDescription;
        this.shouldDockBigOverlays = this.mWmService.mContext.getResources().getBoolean(android.R.bool.config_displayWhiteBalanceEnabledDefault);
        if (j > 0) {
            this.createTime = j;
        }
        this.mAtmService.mPackageConfigPersister.updateConfigIfNeeded(this, this.mUserId, this.packageName);
        this.mActivityRecordInputSink = new com.android.server.wm.ActivityRecordInputSink(this, activityRecord2);
        if (activityInfo.lockTaskLaunchMode == 3 && this.packageName.equals(this.mActivityRecordExt.getRootLockPkgName())) {
            this.mActivityRecordExt.setRootLockActivity(true);
        }
        this.mActivityRecordExt.onActivityRecordCreated(this);
        this.mAppActivityEmbeddingSplitsEnabled = isAppActivityEmbeddingSplitsEnabled();
        this.mAllowUntrustedEmbeddingStateSharing = getAllowUntrustedEmbeddingStateSharingProperty();
        this.mOptInOnBackInvoked = android.window.WindowOnBackInvokedDispatcher.isOnBackInvokedCallbackEnabled(this.info, this.info.applicationInfo, new java.util.function.Supplier() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda29
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$new$2();
            }
        });
        this.mCallerState = new com.android.server.wm.ActivityCallerState(this.mAtmService);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.Context lambda$new$2() {
        android.content.Context appContext = null;
        try {
            appContext = this.mAtmService.mContext.createPackageContextAsUser(this.info.packageName, 4, android.os.UserHandle.of(this.mUserId));
            appContext.setTheme(this.theme);
            return appContext;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return appContext;
        }
    }

    private boolean isAppActivityEmbeddingSplitsEnabled() {
        if (!android.view.WindowManager.hasWindowExtensionsEnabled()) {
            return false;
        }
        if (android.view.WindowManager.ACTIVITY_EMBEDDING_GUARD_WITH_ANDROID_15 && !android.app.compat.CompatChanges.isChangeEnabled(306666082L, this.info.packageName, android.os.UserHandle.getUserHandleForUid(getUid()))) {
            return false;
        }
        try {
            return this.mAtmService.mContext.getPackageManager().getProperty("android.window.PROPERTY_ACTIVITY_EMBEDDING_SPLITS_ENABLED", this.packageName).getBoolean();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    static java.lang.String computeTaskAffinity(java.lang.String affinity, int uid) {
        java.lang.String uidStr = java.lang.Integer.toString(uid);
        if (affinity != null && !affinity.startsWith(uidStr)) {
            return uidStr + ":" + affinity;
        }
        return affinity;
    }

    static int getLockTaskLaunchMode(android.content.pm.ActivityInfo aInfo, android.app.ActivityOptions options) {
        int lockTaskLaunchMode = aInfo.lockTaskLaunchMode;
        if (!aInfo.applicationInfo.isPrivilegedApp() && (lockTaskLaunchMode == 2 || lockTaskLaunchMode == 1)) {
            lockTaskLaunchMode = 0;
        }
        if (options != null) {
            boolean useLockTask = options.getLockTaskMode();
            if (useLockTask && lockTaskLaunchMode == 0) {
                return 3;
            }
            return lockTaskLaunchMode;
        }
        return lockTaskLaunchMode;
    }

    android.view.InputApplicationHandle getInputApplicationHandle(boolean update) {
        if (this.mInputApplicationHandle == null) {
            this.mInputApplicationHandle = new android.view.InputApplicationHandle(this.token, toString(), this.mInputDispatchingTimeoutMillis);
        } else if (update) {
            java.lang.String name = toString();
            if (this.mInputDispatchingTimeoutMillis != this.mInputApplicationHandle.dispatchingTimeoutMillis || !name.equals(this.mInputApplicationHandle.name)) {
                this.mInputApplicationHandle = new android.view.InputApplicationHandle(this.token, name, this.mInputDispatchingTimeoutMillis);
            }
        }
        return this.mInputApplicationHandle;
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.ActivityRecord asActivityRecord() {
        return this;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean hasActivity() {
        return true;
    }

    void setProcess(com.android.server.wm.WindowProcessController proc) {
        this.app = proc;
        proc.getWrapper().getExtImpl().updateWaitActivityToAttach(false);
        com.android.server.wm.ActivityRecord root = this.task != null ? this.task.getRootActivity() : null;
        if (root == this) {
            this.task.setRootProcess(proc);
        }
        proc.addActivityIfNeeded(this);
        this.mInputDispatchingTimeoutMillis = com.android.server.wm.ActivityTaskManagerService.getInputDispatchingTimeoutMillisLocked(this);
        com.android.server.wm.TaskFragment tf = getTaskFragment();
        if (tf != null) {
            tf.sendTaskFragmentInfoChanged();
        }
    }

    boolean hasProcess() {
        return this.app != null;
    }

    boolean attachedToProcess() {
        return hasProcess() && this.app.hasThread();
    }

    private int evaluateStartingWindowTheme(com.android.server.wm.ActivityRecord prev, java.lang.String pkg, int originalTheme, int replaceTheme) {
        if (!validateStartingWindowTheme(prev, pkg, originalTheme)) {
            return 0;
        }
        if (replaceTheme == 0 || !validateStartingWindowTheme(prev, pkg, replaceTheme)) {
            return originalTheme;
        }
        return replaceTheme;
    }

    private boolean launchedFromSystemSurface() {
        return this.mLaunchSourceType == 1 || this.mLaunchSourceType == 2 || this.mLaunchSourceType == 3;
    }

    boolean isLaunchSourceType(int type) {
        return this.mLaunchSourceType == type;
    }

    void updateLaunchSourceType(int launchFromUid, com.android.server.wm.WindowProcessController caller) {
        this.mLaunchSourceType = determineLaunchSourceType(launchFromUid, caller);
    }

    private int determineLaunchSourceType(int launchFromUid, com.android.server.wm.WindowProcessController caller) {
        if (launchFromUid == 1000 || launchFromUid == 0) {
            return 1;
        }
        if (caller != null) {
            if (caller.isHomeProcess()) {
                return 2;
            }
            if (this.mAtmService.getSysUiServiceComponentLocked().getPackageName().equals(caller.mInfo.packageName)) {
                return 3;
            }
            return 4;
        }
        return 4;
    }

    private boolean validateStartingWindowTheme(com.android.server.wm.ActivityRecord prev, java.lang.String pkg, int theme) {
        com.android.internal.policy.AttributeCache.Entry ent;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[1]) {
            long protoLogParam0 = theme;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 338586566486930495L, 1, null, java.lang.Long.valueOf(protoLogParam0));
        }
        if (theme != 0 && (ent = com.android.internal.policy.AttributeCache.instance().get(pkg, theme, com.android.internal.R.styleable.Window, this.mWmService.mCurrentUserId)) != null) {
            boolean windowIsTranslucent = ent.array.getBoolean(5, false);
            boolean windowIsFloating = ent.array.getBoolean(4, false);
            boolean windowShowWallpaper = ent.array.getBoolean(14, false);
            boolean windowDisableStarting = ent.array.getBoolean(12, false);
            getWrapper().getExtImpl().setStaringWindowStyle(windowIsTranslucent, windowIsFloating, windowShowWallpaper, windowDisableStarting, this.mLaunchSourceType);
            android.util.Slog.d(TAG, "validateStartingWindowTheme:  Pkg=" + pkg + ", Translucent=" + windowIsTranslucent + ", Floating=" + windowIsFloating + ", ShowWallpaper=" + windowShowWallpaper + ", Disable=" + windowDisableStarting + ", LaunchSource=" + this.mLaunchSourceType);
            if ((windowIsTranslucent && (!this.mActivityRecordExt.hasSplashWindowFlag() || !this.mActivityRecordExt.hasPreloadBitmap(this))) || (windowIsFloating && !this.mActivityRecordExt.hasSplashWindowFlag())) {
                this.mActivityRecordSocExt.setTranslucentWindowLaunch(true);
                return false;
            }
            if (!windowShowWallpaper || getDisplayContent().mWallpaperController.getWallpaperTarget() == null || this.mActivityRecordExt.hasSplashWindowFlag()) {
                boolean z = false;
                if (!windowDisableStarting || ((launchedFromSystemSurface() && !this.mActivityRecordExt.notIgnoreWindowDisableStarting(this)) || this.mActivityRecordExt.hasSplashWindowFlag())) {
                    return true;
                }
                if (prev != null && prev.getActivityType() == 1 && prev.mTransferringSplashScreenState == 0) {
                    if (prev.mStartingData != null) {
                        return true;
                    }
                    if (prev.mStartingWindow != null && prev.mStartingSurface != null) {
                        return true;
                    }
                }
                return z;
            }
            return false;
        }
        return false;
    }

    boolean addStartingWindow(java.lang.String pkg, int resolvedTheme, com.android.server.wm.ActivityRecord from, boolean newTask, boolean taskSwitch, boolean processRunning, boolean allowTaskSnapshot, boolean activityCreated, boolean isSimple, boolean activityAllDrawn) {
        java.lang.String str;
        int type;
        getWrapper().getExtImpl().addStartingWindow();
        if (!okToDisplay()) {
            android.util.Slog.w(TAG, "addStartingWindow fail due to not okToDisplay");
            getWrapper().getExtImpl().notifyAddStartingWindowFail(2048);
            return false;
        }
        if (hasStartingWindow()) {
            getWrapper().getExtImpl().notifyAddStartingWindowFail(com.android.server.wm.IActivityRecordExt.REASON_STARTING_WINDOW_ALREADY_SHOWN);
            if (!this.mActivityRecordExt.hasStartingDataAndMainWindowIsDrawn(this)) {
                this.mActivityRecordExt.attachExStartingSurface(this, 2, null, true, null, false);
            }
            return false;
        }
        com.android.server.wm.WindowState mainWin = findMainWindow(false);
        java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("addStartingWindow for ").append(this).append(", mainWin=").append(mainWin);
        if (mainWin != null) {
            str = ", mVisible=" + this.mVisible + ", mVisibleRequested=" + this.mVisibleRequested + ", drawn=" + mainWin.isDrawn() + ", mDestroying=" + mainWin.mDestroying;
        } else {
            str = "";
        }
        android.util.Slog.d(TAG, sbAppend.append(str).append(", state=").append(getState()).append(", isKeyguardLocked=").append(this.mDisplayContent.isKeyguardLocked()).append(", isKeyguardGoingAway=").append(this.mDisplayContent.isKeyguardGoingAway()).toString());
        if (mainWin != null && mainWin.isDrawn() && (this.mVisible || this.mVisibleRequested || this.mActivityRecordExt.isWindowSurfaceSaved(mainWin) || this.mActivityRecordExt.skipAddStartingWindow())) {
            android.util.Slog.w(TAG, "App already has a visible window, why would you want a starting window?");
            getWrapper().getExtImpl().makeSurePrevStartingWindowType(this, newTask, taskSwitch, processRunning, allowTaskSnapshot, activityCreated, activityAllDrawn);
            getWrapper().getExtImpl().notifyAddStartingWindowFail(4096);
            return false;
        }
        if (this.task != null) {
            if (getWrapper().getExtImpl().shouldInterceptAddStartingWindowForFlexible(this.task, this)) {
                android.util.Slog.w(TAG, "intercept addStartingWindow for flexible minimize");
                getWrapper().getExtImpl().notifyAddStartingWindowFail(com.android.server.wm.IActivityRecordExt.REASON_FLEXIBLE_MINIMIZE);
                return false;
            }
            if (!this.mActivityRecordExt.allowUseSnapshot(this, newTask, taskSwitch, processRunning, activityCreated)) {
                android.util.Slog.v(TAG, "allowUseSnapshot return");
                return false;
            }
            android.window.TaskSnapshot snapshot = this.mWmService.mTaskSnapshotController.getSnapshot(this.task.mTaskId, this.task.mUserId, false, false);
            if (snapshot != null && snapshot.getWindowingMode() == 100) {
                android.util.Slog.v(TAG, "last windowmode is zoom: " + this.task);
                getWrapper().getExtImpl().notifyAddStartingWindowFail(com.android.server.wm.IActivityRecordExt.REASON_ZOOM_MODE);
                return false;
            }
            int type2 = this.mActivityRecordExt.getStartingWindowType(newTask, taskSwitch, processRunning, allowTaskSnapshot, activityCreated, activityAllDrawn, snapshot);
            if (this.mActivityRecordExt.canCreateTaskSnapShotSurface(type2, this)) {
                type = type2;
            } else {
                type = 2;
            }
            if (type == 2) {
                this.mIsSplashScreenWindow = true;
            }
            boolean useLegacy = type == 2 && this.mWmService.mStartingSurfaceController.isExceptionApp(this.packageName, this.mTargetSdk, new java.util.function.Supplier() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda24
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$addStartingWindow$3();
                }
            });
            int typeParameter = com.android.server.wm.StartingSurfaceController.makeStartingWindowTypeParameter(newTask, taskSwitch, processRunning, allowTaskSnapshot, activityCreated, isSimple, useLegacy, activityAllDrawn, type, isIconStylePreferred(resolvedTheme), this.packageName, this.mUserId);
            if (type == 1) {
                if (isActivityTypeHome()) {
                    this.mWmService.mTaskSnapshotController.removeSnapshotCache(this.task.mTaskId);
                    if ((2 & this.mDisplayContent.mAppTransition.getTransitFlags()) == 0) {
                        getWrapper().getExtImpl().notifyAddStartingWindowFail(com.android.server.wm.IActivityRecordExt.REASON_KEYGUARD_GOING_AWAY_NO_ANIMATION);
                        return false;
                    }
                }
                if (snapshot != null && this.mActivityRecordExt.attachExStartingSurface(this, 1, snapshot, false, null, false)) {
                    return false;
                }
                return createSnapshot(snapshot, typeParameter);
            }
            int type3 = type;
            if (this.mTaskOverlay && this.mActivityRecordExt.addStartingSurfaceForTaskWithPermissionsActivity(this, snapshot)) {
                return false;
            }
            if (resolvedTheme == 0 && this.theme != 0) {
                android.util.Slog.d(TAG, "skip addStartingWindow due to resolvedTheme: " + resolvedTheme + ", theme: " + this.theme);
                getWrapper().getExtImpl().notifyAddStartingWindowFail(com.android.server.wm.IActivityRecordExt.REASON_RESOLVED_THEME_FAIL);
                this.mActivityRecordExt.attachExStartingSurface(this, 2, snapshot, true, null, false);
                return false;
            }
            if (from != null && transferStartingWindow(from)) {
                android.util.Slog.d(TAG, "skip transferStartingWindow  from: " + from);
                return true;
            }
            if (type3 != 2) {
                getWrapper().getExtImpl().notifyAddStartingWindowFail(com.android.server.wm.IActivityRecordExt.REASON_TYPE_NO_STARTING_WINDOW);
                this.mActivityRecordExt.attachExStartingSurface(this, snapshot == null ? 2 : 1, snapshot, true, null, false);
                return false;
            }
            this.mActivityRecordExt.attachExStartingSurface(this, 2, snapshot, true, null, false);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 7269690012594027154L, 0, null, null);
            }
            this.mStartingData = new com.android.server.wm.SplashScreenStartingData(this.mWmService, resolvedTheme, typeParameter);
            scheduleAddStartingWindow();
            return true;
        }
        android.util.Slog.w(TAG, "addStartingWindow fail due to task is null");
        getWrapper().getExtImpl().notifyAddStartingWindowFail(com.android.server.wm.IActivityRecordExt.REASON_TASK_DESTROYED);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.pm.ApplicationInfo lambda$addStartingWindow$3() {
        android.content.pm.ActivityInfo activityInfo = this.intent.resolveActivityInfo(this.mAtmService.mContext.getPackageManager(), 128);
        if (activityInfo != null) {
            return activityInfo.applicationInfo;
        }
        return null;
    }

    private boolean createSnapshot(android.window.TaskSnapshot snapshot, int typeParams) {
        if (snapshot == null) {
            getWrapper().getExtImpl().notifyAddStartingWindowFail(com.android.server.wm.IActivityRecordExt.REASON_NO_SNAPSHOT);
            return false;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[1]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -3432060893368468911L, 0, null, null);
        }
        this.mStartingData = new com.android.server.wm.SnapshotStartingData(this.mWmService, snapshot, typeParams);
        this.mActivityRecordExt.setSnapshotStarting(true);
        if ((!this.mStyleFillsParent && this.task.getChildCount() > 1 && !this.mActivityRecordExt.disableAssociateStartingDataWithTask(snapshot, this)) || this.task.forAllLeafTaskFragments(new com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda6()) || this.mActivityRecordExt.shouldAssociateStartingDataWithTask()) {
            associateStartingDataWithTask();
        }
        scheduleAddStartingWindow();
        return true;
    }

    private void scheduleAddStartingWindow() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mStartingData);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 5659016061937922595L, 0, null, protoLogParam0, protoLogParam1);
        }
        this.mStartingSurface = this.mStartingData.createStartingSurface(this);
        if (this.mStartingSurface != null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                java.lang.String protoLogParam12 = java.lang.String.valueOf(this.mStartingWindow);
                java.lang.String protoLogParam2 = java.lang.String.valueOf(this.mStartingSurface);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 7506106334102501360L, 0, null, protoLogParam02, protoLogParam12, protoLogParam2);
            }
            this.mActivityRecordExt.updateStartingRecords(this, true);
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[1]) {
            java.lang.String protoLogParam03 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 1048048288756547220L, 0, null, protoLogParam03);
        }
        getWrapper().getExtImpl().notifyAddStartingWindowFail(com.android.server.wm.IActivityRecordExt.SUB_REASON_CREATE_SURFACE_NULL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getStartingWindowType(boolean newTask, boolean taskSwitch, boolean processRunning, boolean allowTaskSnapshot, boolean activityCreated, boolean activityAllDrawn, android.window.TaskSnapshot snapshot) {
        com.android.server.wm.ActivityRecord topAttached;
        android.util.Slog.d(TAG, "getStartingWindowType: , newTask=" + newTask + ", taskSwitch=" + taskSwitch + ", processRunning=" + processRunning + ", allowTaskSnapshot=" + allowTaskSnapshot + ", activityCreated=" + activityCreated + ", activityAllDrawn=" + activityAllDrawn + ", snapshot exist: " + (snapshot != null));
        if (!newTask && taskSwitch && processRunning && !activityCreated && this.task.intent != null && this.mActivityComponent.equals(this.task.intent.getComponent()) && (topAttached = this.task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.ActivityRecord) obj).attachedToProcess();
            }
        })) != null) {
            int rotation = this.mActivityRecordExt.getFixRotationForSnapshot(this, this.mDisplayContent.getDisplayRotation().rotationForOrientation(getOverrideOrientation(), this.mDisplayContent.getRotation()), snapshot);
            if (snapshot != null) {
                android.util.Slog.d(TAG, "getStartingWindowType and snapshot rotation is: " + snapshot.getRotation() + " rotation is: " + rotation);
            }
            if (!topAttached.isSnapshotCompatible(snapshot) || ((snapshot == null || rotation != snapshot.getRotation()) && !this.mActivityRecordExt.useSnapshotIngoreRotationNotMatch())) {
                return this.mActivityRecordExt.allowAddStartingWindow(topAttached, snapshot, rotation) ? 2 : 0;
            }
            return 1;
        }
        boolean isActivityHome = isActivityTypeHome();
        if ((newTask || !processRunning || (taskSwitch && !activityCreated)) && !isActivityHome) {
            return 2;
        }
        if (taskSwitch) {
            if (this.mActivityRecordExt.isFlexibleZoomWindow(getWindowingMode()) && !isActivityHome && this.mActivityRecordExt.isZoomSplashExceptionList(this.packageName)) {
                return 2;
            }
            if (allowTaskSnapshot) {
                if (isSnapshotCompatible(snapshot) || this.mActivityRecordExt.canSupportSnapshot(this, snapshot, activityCreated)) {
                    return 1;
                }
                if (!isActivityHome) {
                    return 2;
                }
            }
            return (activityAllDrawn || isActivityHome || this.mActivityRecordExt.clearStartingWindowWhenSnapshotDiffOrientation(this)) ? 0 : 2;
        }
        return 0;
    }

    boolean isSnapshotCompatible(android.window.TaskSnapshot snapshot) {
        return snapshot != null && isSnapshotComponentCompatible(snapshot) && isSnapshotOrientationCompatible(snapshot);
    }

    boolean isSnapshotComponentCompatible(android.window.TaskSnapshot snapshot) {
        return snapshot.getTopActivityComponent().equals(this.mActivityComponent);
    }

    boolean isSnapshotOrientationCompatible(android.window.TaskSnapshot snapshot) {
        int targetRotation;
        int rotation = this.mDisplayContent.rotationForActivityInDifferentOrientation(this);
        int currentRotation = this.task.getWindowConfiguration().getRotation();
        if (rotation != -1) {
            targetRotation = rotation;
        } else {
            targetRotation = currentRotation;
        }
        if (snapshot.getRotation() != targetRotation && !this.mActivityRecordExt.ignoreSnapShotRotation(snapshot, this, this.task)) {
            android.util.Slog.w(TAG, "isSnapshotCompatible, wrong snapshot rotation:" + snapshot.getRotation() + ",targetRotation:" + targetRotation + ",rotation:" + rotation + ",currentRotation:" + currentRotation);
            return false;
        }
        android.graphics.Rect taskBounds = this.task.getBounds();
        int w = taskBounds.width();
        int h = taskBounds.height();
        android.graphics.Point taskSize = snapshot.getTaskSize();
        if (java.lang.Math.abs(currentRotation - targetRotation) % 2 == 1) {
            w = h;
            h = w;
        }
        android.util.Slog.d(TAG, "isSnapshotCompatible: w=" + w + ", h=" + h + ", taskSize=" + taskSize);
        return java.lang.Math.abs((((float) taskSize.x) / ((float) java.lang.Math.max(taskSize.y, 1))) - (((float) w) / ((float) java.lang.Math.max(h, 1)))) <= 0.01f;
    }

    void setCustomizeSplashScreenExitAnimation(boolean enable) {
        if (this.mHandleExitSplashScreen == enable) {
            return;
        }
        this.mHandleExitSplashScreen = enable;
    }

    private void scheduleTransferSplashScreenTimeout() {
        this.mAtmService.mH.postDelayed(this.mTransferSplashScreenTimeoutRunnable, 2000L);
    }

    private void removeTransferSplashScreenTimeout() {
        this.mAtmService.mH.removeCallbacks(this.mTransferSplashScreenTimeoutRunnable);
    }

    private boolean transferSplashScreenIfNeeded() {
        if (this.finishing || !this.mHandleExitSplashScreen || this.mStartingSurface == null || this.mStartingWindow == null || this.mTransferringSplashScreenState == 3) {
            return false;
        }
        if (this.mStartingData != null && this.mStartingData.mResizedFromTransfer) {
            return false;
        }
        if (isTransferringSplashScreen()) {
            return true;
        }
        if (this.mStartingData != null && this.mStartingData.mWaitForSyncTransactionCommit) {
            this.mStartingData.mRemoveAfterTransaction = 2;
            return true;
        }
        requestCopySplashScreen();
        return isTransferringSplashScreen();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTransferringSplashScreen() {
        return this.mTransferringSplashScreenState == 2 || this.mTransferringSplashScreenState == 1;
    }

    private void requestCopySplashScreen() {
        this.mTransferringSplashScreenState = 1;
        if (this.mStartingSurface == null || !this.mAtmService.mTaskOrganizerController.copySplashScreenView(getTask(), this.mStartingSurface.mTaskOrganizer)) {
            this.mTransferringSplashScreenState = 3;
            removeStartingWindow();
        }
        scheduleTransferSplashScreenTimeout();
    }

    void onCopySplashScreenFinish(android.window.SplashScreenView.SplashScreenViewParcelable parcelable) {
        android.view.SurfaceControl windowAnimationLeash;
        removeTransferSplashScreenTimeout();
        if (parcelable == null || this.mTransferringSplashScreenState != 1 || this.mStartingWindow == null || this.mStartingWindow.mRemoved || this.finishing) {
            windowAnimationLeash = null;
        } else {
            windowAnimationLeash = com.android.server.wm.TaskOrganizerController.applyStartingWindowAnimation(this.mStartingWindow);
        }
        if (windowAnimationLeash != null) {
            try {
                this.mStartingWindow.getWrapper().getExtImpl().setCopySplashScreenFinish(true);
                this.mTransferringSplashScreenState = 2;
                this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), android.app.servertransaction.TransferSplashScreenViewStateItem.obtain(this.token, parcelable, windowAnimationLeash));
                scheduleTransferSplashScreenTimeout();
                return;
            } catch (java.lang.Exception e) {
                android.util.Slog.w(TAG, "onCopySplashScreenComplete fail: " + this);
                this.mStartingWindow.cancelAnimation();
                parcelable.clearIfNeeded();
                this.mTransferringSplashScreenState = 3;
                this.mStartingWindow.getWrapper().getExtImpl().setCopySplashScreenFinish(false);
                return;
            }
        }
        if (parcelable != null) {
            parcelable.clearIfNeeded();
        }
        this.mTransferringSplashScreenState = 3;
        removeStartingWindow();
    }

    private void onSplashScreenAttachComplete() {
        removeTransferSplashScreenTimeout();
        if (this.mStartingWindow != null) {
            this.mStartingWindow.cancelAnimation();
            this.mStartingWindow.hide(false, false);
        }
        this.mTransferringSplashScreenState = 3;
        removeStartingWindowAnimation(false);
    }

    void cleanUpSplashScreen() {
        if (!this.mHandleExitSplashScreen || this.startingMoved) {
            return;
        }
        if (this.mTransferringSplashScreenState == 3 || this.mTransferringSplashScreenState == 0) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -1298801500610545721L, 0, null, protoLogParam0);
            }
            this.mAtmService.mTaskOrganizerController.onAppSplashScreenViewRemoved(getTask(), this.mStartingSurface != null ? this.mStartingSurface.mTaskOrganizer : null);
        }
    }

    boolean isStartingWindowDisplayed() {
        com.android.server.wm.StartingData data;
        if (this.mStartingData != null) {
            data = this.mStartingData;
        } else {
            data = this.task != null ? this.task.mSharedStartingData : null;
        }
        if (data != null) {
            android.util.Slog.d(TAG, "isStartingWindowDisplayed: data.mIsDisplayed = " + data.mIsDisplayed + ", mStartingWindow.isDrawn = " + (this.mStartingWindow != null ? java.lang.Boolean.valueOf(this.mStartingWindow.isDrawn()) : null));
        }
        return data != null && data.mIsDisplayed && this.mStartingWindow != null && this.mStartingWindow.isDrawn();
    }

    void attachStartingWindow(com.android.server.wm.WindowState startingWindow) {
        startingWindow.mStartingData = this.mStartingData;
        this.mStartingWindow = startingWindow;
        android.util.Slog.d(TAG, "attachStartingWindow: mStartingData=" + startingWindow.mStartingData + ",mStartingWindow=" + this.mStartingWindow);
        if (this.mStartingData != null) {
            if (this.mStartingData.mAssociatedTask != null) {
                attachStartingSurfaceToAssociatedTask();
            } else if (isEmbedded()) {
                associateStartingWindowWithTaskIfNeeded();
            }
            if (this.mTransitionController.isCollecting()) {
                this.mStartingData.mTransitionId = this.mTransitionController.getCollectingTransitionId();
            }
        }
    }

    private void attachStartingSurfaceToAssociatedTask() {
        if (this.mStartingData == null || this.mStartingData.mAssociatedTask == null || this.mStartingWindow.mSurfaceControl == null) {
            return;
        }
        if (this.mSyncState == 0 && isEmbedded()) {
            this.mTransitionController.collect(this);
        }
        overrideConfigurationPropagation(this.mStartingWindow, this.mStartingData.mAssociatedTask);
        getSyncTransaction().reparent(this.mStartingWindow.mSurfaceControl, this.mStartingData.mAssociatedTask.mSurfaceControl);
    }

    private void associateStartingDataWithTask() {
        com.android.server.wm.TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null && taskFragment.isEmbedded()) {
            com.android.server.wm.TaskFragment organized = taskFragment.getOrganizedTaskFragment();
            com.android.server.wm.TaskFragment adjacent = organized != null ? organized.getAdjacentTaskFragment() : null;
            com.android.server.wm.ActivityRecord adjacentActivity = adjacent != null ? adjacent.topRunningActivity() : null;
            if (adjacentActivity != null && adjacentActivity.isVisible() && adjacentActivity.firstWindowDrawn) {
                return;
            }
        }
        this.mStartingData.mAssociatedTask = this.task;
        this.task.mSharedStartingData = this.mStartingData;
    }

    void associateStartingWindowWithTaskIfNeeded() {
        if (this.mStartingWindow == null || this.mStartingData == null || this.mStartingData.mAssociatedTask != null) {
            return;
        }
        associateStartingDataWithTask();
        attachStartingSurfaceToAssociatedTask();
    }

    void removeStartingWindow() {
        boolean prevEligibleForLetterboxEducation = isEligibleForLetterboxEducation();
        if (transferSplashScreenIfNeeded()) {
            android.util.Slog.d(TAG, "cancel removeStartingWindow due to transferSplashScreenIfNeeded");
            return;
        }
        removeStartingWindowAnimation(true);
        com.android.server.wm.Task task = getTask();
        if (prevEligibleForLetterboxEducation != isEligibleForLetterboxEducation() && task != null) {
            task.dispatchTaskInfoChangedIfNeeded(true);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void waitForSyncTransactionCommit(android.util.ArraySet<com.android.server.wm.WindowContainer> wcAwaitingCommit) {
        super.waitForSyncTransactionCommit(wcAwaitingCommit);
        if (this.mStartingData != null) {
            this.mStartingData.mWaitForSyncTransactionCommit = true;
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void onSyncTransactionCommitted(android.view.SurfaceControl.Transaction t) {
        super.onSyncTransactionCommitted(t);
        if (this.mStartingData == null) {
            this.mActivityRecordExt.resetWaitForSyncTransactionCommitIfNeeded(this);
            return;
        }
        com.android.server.wm.StartingData lastData = this.mStartingData;
        lastData.mWaitForSyncTransactionCommit = false;
        if (lastData.mRemoveAfterTransaction == 1) {
            removeStartingWindowAnimation(lastData.mPrepareRemoveAnimation);
        } else if (lastData.mRemoveAfterTransaction == 2) {
            removeStartingWindow();
        }
    }

    void removeStartingWindowAnimation(boolean prepareAnimation) {
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, "removeStartingWindowAnimation for activity=" + this + " prepareAnimation=" + prepareAnimation + ",Callers=" + android.os.Debug.getCallers(5));
        }
        this.mTransferringSplashScreenState = 0;
        if (this.mStartingData != null && this.task != null) {
            this.task.mSharedStartingData = null;
        }
        if (this.mStartingWindow == null) {
            android.util.Slog.d(TAG, "Clearing startingData for activity=" + this + " mStartingData=" + this.mStartingData + " mStartingSurface=" + this.mStartingSurface);
            if (this.mStartingData != null && !this.mActivityRecordExt.shouldSkipRemoveStartingWindow(findMainWindow(false), this)) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -1948849214526113495L, 0, null, protoLogParam0);
                }
                this.mStartingData = null;
                if (this.mStartingSurface != null) {
                    this.mStartingSurface.remove(false, false);
                }
                this.mStartingSurface = null;
                return;
            }
            return;
        }
        com.android.server.wm.WindowState startingWindow = this.mStartingWindow;
        if (this.mStartingData == null) {
            android.util.Slog.d(TAG, "Tried to remove starting window but startingWindow was null: " + this);
            return;
        }
        if (this.mStartingData.mWaitForSyncTransactionCommit || ((this.mTransitionController.isCollecting(this) && !this.mActivityRecordExt.shouldRemoveStartingWindowImmediately(this)) || this.mActivityRecordExt.shouldSkipRemoveStartingWindow(findMainWindow(false), this))) {
            android.util.Slog.d(TAG, "cannot removestartingWindow syncTransactionCommit:" + this.mStartingData.mWaitForSyncTransactionCommit + " ,isCollecting:" + this.mTransitionController.isCollecting(this));
            this.mStartingData.mRemoveAfterTransaction = 1;
            this.mStartingData.mPrepareRemoveAnimation = prepareAnimation;
            return;
        }
        boolean animate = prepareAnimation && this.mStartingData.needRevealAnimation() && this.mStartingWindow.isVisibleByPolicy() && this.mStartingWindow.hasDrawn();
        boolean hasImeSurface = this.mStartingData.hasImeSurface();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[1]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mStartingWindow);
            boolean protoLogParam2 = animate;
            java.lang.String protoLogParam3 = java.lang.String.valueOf(android.os.Debug.getCallers(5));
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 5545923784327902026L, 48, null, protoLogParam02, protoLogParam1, java.lang.Boolean.valueOf(protoLogParam2), protoLogParam3);
        }
        com.android.server.wm.StartingSurfaceController.StartingSurface surface = this.mStartingSurface;
        this.mStartingData = null;
        this.mStartingSurface = null;
        this.mStartingWindow = null;
        this.mTransitionChangeFlags &= -9;
        if (surface == null || (findMainWindow(false) != null && (startingWindow.getSurfaceControl() == null || !startingWindow.getSurfaceControl().isValid()))) {
            android.util.Slog.d(TAG, "startingWindow was set but startingSurface==null, couldn't remove, surface=" + surface);
            return;
        }
        android.util.Slog.d(TAG, "removeStartingWindowAnimation starting " + java.lang.String.format("%s startingWindow=%s ,startingView=%s", this, this.mStartingWindow, surface) + ",Callers=" + android.os.Debug.getCallers(5));
        if (this.mActivityRecordExt.interceptRemoveStartingWindow(this, this.mWmService.mAnimationHandler, surface, this.mActivityRecordExt.isSnapshotStarting())) {
            return;
        }
        surface.remove(animate, hasImeSurface);
        getWrapper().getExtImpl().removeStartingWindow();
    }

    void reparent(com.android.server.wm.TaskFragment newTaskFrag, int position, java.lang.String reason) {
        if (getParent() == null) {
            android.util.Slog.w(TAG, "reparent: Attempted to reparent non-existing app token: " + this.token);
            return;
        }
        com.android.server.wm.TaskFragment prevTaskFrag = getTaskFragment();
        if (prevTaskFrag == newTaskFrag) {
            throw new java.lang.IllegalArgumentException(reason + ": task fragment =" + newTaskFrag + " is already the parent of r=" + this);
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            long protoLogParam1 = this.task.mTaskId;
            long protoLogParam2 = position;
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 5521236266092347335L, 20, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2));
        }
        this.mActivityRecordExt.handleActivityReparent(this, this.task);
        reparent(newTaskFrag, position);
    }

    static boolean isHomeIntent(android.content.Intent intent) {
        return "android.intent.action.MAIN".equals(intent.getAction()) && (intent.hasCategory("android.intent.category.HOME") || intent.hasCategory("android.intent.category.SECONDARY_HOME")) && intent.getCategories().size() == 1 && intent.getData() == null && intent.getType() == null;
    }

    static boolean isMainIntent(android.content.Intent intent) {
        return "android.intent.action.MAIN".equals(intent.getAction()) && intent.hasCategory("android.intent.category.LAUNCHER") && intent.getCategories().size() == 1 && intent.getData() == null && intent.getType() == null;
    }

    boolean canLaunchHomeActivity(int uid, com.android.server.wm.ActivityRecord sourceRecord) {
        if (uid == 1000 || uid == 0) {
            return true;
        }
        com.android.server.wm.RecentTasks recentTasks = this.mTaskSupervisor.mService.getRecentTasks();
        if (recentTasks == null || !recentTasks.isCallerRecents(uid)) {
            return sourceRecord != null && sourceRecord.isResolverOrDelegateActivity();
        }
        return true;
    }

    private boolean canLaunchAssistActivity(java.lang.String packageName) {
        android.content.ComponentName assistComponent = this.mAtmService.mActiveVoiceInteractionServiceComponent;
        if (assistComponent != null) {
            return assistComponent.getPackageName().equals(packageName);
        }
        return false;
    }

    private void setActivityType(boolean componentSpecified, int launchedFromUid, android.content.Intent intent, android.app.ActivityOptions options, com.android.server.wm.ActivityRecord sourceRecord) {
        int activityType = 0;
        if ((!componentSpecified || canLaunchHomeActivity(launchedFromUid, sourceRecord)) && isHomeIntent(intent) && !isResolverOrDelegateActivity()) {
            activityType = 2;
            if (this.info.resizeMode == 4 || this.info.resizeMode == 1) {
                this.info.resizeMode = 0;
            }
        } else if (this.mAtmService.getRecentTasks().isRecentsComponent(this.mActivityComponent, this.info.applicationInfo.uid)) {
            activityType = 3;
        } else if (options != null && options.getLaunchActivityType() == 4 && canLaunchAssistActivity(this.launchedFromPackage)) {
            activityType = 4;
        } else if (options != null && options.getLaunchActivityType() == 5 && this.mAtmService.canLaunchDreamActivity(this.launchedFromPackage) && android.service.dreams.DreamActivity.class.getName() == this.info.name) {
            activityType = 5;
        }
        setActivityType(this.mActivityRecordExt.toMultiSearchActivityTypeIfNeed(this.info, this.mAtmService.getPackageManager(), activityType));
    }

    void setTaskToAffiliateWith(com.android.server.wm.Task taskToAffiliateWith) {
        if (this.launchMode != 3 && this.launchMode != 2) {
            this.task.setTaskToAffiliateWith(taskToAffiliateWith);
        }
    }

    com.android.server.wm.Task getRootTask() {
        if (this.task != null) {
            return this.task.getRootTask();
        }
        return null;
    }

    int getRootTaskId() {
        if (this.task != null) {
            return this.task.getRootTaskId();
        }
        return -1;
    }

    com.android.server.wm.Task getOrganizedTask() {
        if (this.task != null) {
            return this.task.getOrganizedTask();
        }
        return null;
    }

    com.android.server.wm.TaskFragment getOrganizedTaskFragment() {
        com.android.server.wm.TaskFragment parent = getTaskFragment();
        if (parent != null) {
            return parent.getOrganizedTaskFragment();
        }
        return null;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isEmbedded() {
        com.android.server.wm.TaskFragment parent = getTaskFragment();
        return parent != null && parent.isEmbedded();
    }

    boolean isUntrustedEmbeddingStateSharingAllowed() {
        if (!com.android.window.flags.Flags.untrustedEmbeddingStateSharing()) {
            return false;
        }
        return this.mAllowUntrustedEmbeddingStateSharing;
    }

    private boolean getAllowUntrustedEmbeddingStateSharingProperty() {
        if (!com.android.window.flags.Flags.untrustedEmbeddingStateSharing()) {
            return false;
        }
        try {
            return this.mAtmService.mContext.getPackageManager().getProperty("android.window.PROPERTY_ALLOW_UNTRUSTED_ACTIVITY_EMBEDDING_STATE_SHARING", this.mActivityComponent).getBoolean();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    boolean isEmbeddedInHostContainer() {
        com.android.server.wm.TaskFragment taskFragment = getOrganizedTaskFragment();
        return taskFragment != null && taskFragment.isEmbeddedWithBoundsOverride();
    }

    android.window.ActivityWindowInfo getActivityWindowInfo() {
        if (!com.android.window.flags.Flags.activityWindowInfoFlag() || !isAttached()) {
            return this.mTmpActivityWindowInfo;
        }
        if (isFixedRotationTransforming()) {
            android.graphics.Rect bounds = getBounds();
            this.mTmpActivityWindowInfo.set(false, bounds, bounds);
        } else {
            this.mTmpActivityWindowInfo.set(isEmbeddedInHostContainer(), getTask().getBounds(), getTaskFragment().getBounds());
        }
        return this.mTmpActivityWindowInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public com.android.server.wm.TaskDisplayArea getDisplayArea() {
        return (com.android.server.wm.TaskDisplayArea) super.getDisplayArea();
    }

    @Override // com.android.server.wm.WindowContainer
    boolean providesOrientation() {
        return this.mStyleFillsParent || this.mOccludesParent;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean fillsParent() {
        return occludesParent(true);
    }

    boolean occludesParent() {
        return occludesParent(false);
    }

    boolean occludesParent(boolean includingFinishing) {
        if (includingFinishing || !this.finishing) {
            return this.mActivityRecordExt.adjustOccludesParent(this.mOccludesParent || showWallpaper());
        }
        return false;
    }

    boolean setOccludesParent(boolean occludesParent) {
        boolean changed = occludesParent != this.mOccludesParent;
        this.mOccludesParent = occludesParent;
        setMainWindowOpaque(occludesParent);
        if (changed && this.task != null) {
            if (!occludesParent) {
                getRootTask().convertActivityToTranslucent(this);
            } else {
                getRootTask().convertActivityFromTranslucent(this);
            }
        }
        if (changed || !occludesParent) {
            this.mRootWindowContainer.ensureActivitiesVisible();
        }
        return changed;
    }

    void setMainWindowOpaque(boolean isOpaque) {
        com.android.server.wm.WindowState win = findMainWindow();
        if (win == null) {
            return;
        }
        win.mWinAnimator.setOpaqueLocked(isOpaque & (!android.graphics.PixelFormat.formatHasAlpha(win.getAttrs().format)));
    }

    void takeFromHistory() {
        if (this.inHistory) {
            this.inHistory = false;
            if (this.task != null && !this.finishing) {
                this.task = null;
            }
            abortAndClearOptionsAnimation();
        }
    }

    boolean isInHistory() {
        return this.inHistory;
    }

    boolean isInRootTaskLocked() {
        com.android.server.wm.Task rootTask = getRootTask();
        return (rootTask == null || rootTask.isInTask(this) == null) ? false : true;
    }

    boolean isPersistable() {
        return (this.info.persistableMode == 0 || this.info.persistableMode == 2) && (this.intent == null || (this.intent.getFlags() & 8388608) == 0);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isFocusable() {
        return super.isFocusable() && (canReceiveKeys() || isAlwaysFocusable());
    }

    boolean canReceiveKeys() {
        return getWindowConfiguration().canReceiveKeys() && !this.mWaitForEnteringPinnedMode;
    }

    boolean isResizeable() {
        return isResizeable(true);
    }

    boolean isResizeable(boolean checkPictureInPictureSupport) {
        return this.mAtmService.mForceResizableActivities || android.content.pm.ActivityInfo.isResizeableMode(this.info.resizeMode) || (this.info.supportsPictureInPicture() && checkPictureInPictureSupport) || this.mActivityRecordExt.isResizeableForMultiSearch(this.task) || isEmbedded();
    }

    boolean canForceResizeNonResizable(int windowingMode) {
        boolean supportsMultiWindow;
        if (windowingMode == 2 && this.info.supportsPictureInPicture()) {
            return false;
        }
        if (this.task != null) {
            supportsMultiWindow = this.task.supportsMultiWindow() || supportsMultiWindow();
        } else {
            supportsMultiWindow = supportsMultiWindow();
        }
        return ((android.app.WindowConfiguration.inMultiWindowMode(windowingMode) && supportsMultiWindow && !this.mAtmService.mForceResizableActivities) || this.info.resizeMode == 2 || this.info.resizeMode == 1) ? false : true;
    }

    boolean supportsPictureInPicture() {
        return this.mAtmService.mSupportsPictureInPicture && isActivityTypeStandardOrUndefined() && this.info.supportsPictureInPicture();
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean supportsSplitScreenWindowingMode() {
        return supportsSplitScreenWindowingModeInDisplayArea(getDisplayArea());
    }

    boolean supportsSplitScreenWindowingModeInDisplayArea(com.android.server.wm.TaskDisplayArea tda) {
        return super.supportsSplitScreenWindowingMode() && this.mAtmService.mSupportsSplitScreenMultiWindow && this.mActivityRecordExt.supportsSplitScreenByVendorPolicy(this, supportsMultiWindowInDisplayArea(tda));
    }

    boolean supportsFreeform() {
        return supportsFreeformInDisplayArea(getDisplayArea());
    }

    boolean supportsFreeformInDisplayArea(com.android.server.wm.TaskDisplayArea tda) {
        return this.mAtmService.mSupportsFreeformWindowManagement && supportsMultiWindowInDisplayArea(tda);
    }

    boolean supportsMultiWindow() {
        return supportsMultiWindowInDisplayArea(getDisplayArea());
    }

    boolean supportsMultiWindowInDisplayArea(com.android.server.wm.TaskDisplayArea tda) {
        if (isActivityTypeHome() || !this.mAtmService.mSupportsMultiWindow || tda == null) {
            return false;
        }
        if (!isResizeable() && !tda.supportsNonResizableMultiWindow()) {
            return false;
        }
        android.content.pm.ActivityInfo.WindowLayout windowLayout = this.info.windowLayout;
        return windowLayout == null || tda.supportsActivityMinWidthHeightMultiWindow(windowLayout.minWidth, windowLayout.minHeight, this.info);
    }

    boolean canBeLaunchedOnDisplay(int displayId) {
        return this.mAtmService.mTaskSupervisor.canPlaceEntityOnDisplay(displayId, this.launchedFromPid, this.launchedFromUid, this.info);
    }

    boolean checkEnterPictureInPictureState(java.lang.String caller, boolean beforeStopping) {
        if (!supportsPictureInPicture() || !checkEnterPictureInPictureAppOpsState() || this.mAtmService.shouldDisableNonVrUiLocked()) {
            return false;
        }
        if (this.mDisplayContent != null && !this.mDisplayContent.mDwpcHelper.isEnteringPipAllowed(getUid())) {
            android.util.Slog.w(TAG, "Display " + this.mDisplayContent.getDisplayId() + " doesn't support enter picture-in-picture mode. caller = " + caller);
            return false;
        }
        boolean isCurrentAppLocked = this.mAtmService.getLockTaskModeState() != 0;
        com.android.server.wm.TaskDisplayArea taskDisplayArea = getDisplayArea();
        boolean hasRootPinnedTask = taskDisplayArea != null && taskDisplayArea.hasPinnedTask();
        boolean isNotLockedOrOnKeyguard = (isKeyguardLocked() || isCurrentAppLocked) ? false : true;
        if (beforeStopping && hasRootPinnedTask) {
            return false;
        }
        switch (this.mState.ordinal()) {
            case 2:
                if (isCurrentAppLocked) {
                    return false;
                }
                return this.supportsEnterPipOnTaskSwitch || !beforeStopping;
            case 3:
            case 4:
                return isNotLockedOrOnKeyguard && !hasRootPinnedTask && this.supportsEnterPipOnTaskSwitch;
            case 5:
                return this.supportsEnterPipOnTaskSwitch && isNotLockedOrOnKeyguard && !hasRootPinnedTask;
            default:
                return false;
        }
    }

    void setWillCloseOrEnterPip(boolean willCloseOrEnterPip) {
        this.mWillCloseOrEnterPip = willCloseOrEnterPip;
    }

    boolean willCloseOrEnterPip() {
        return this.mWillCloseOrEnterPip;
    }

    boolean checkEnterPictureInPictureAppOpsState() {
        return this.mZenModeManagerExt.canEnterPictureInPicture(this.packageName, this.info.applicationInfo.uid) && this.mAtmService.getAppOpsManager().checkOpNoThrow(67, this.info.applicationInfo.uid, this.packageName) == 0;
    }

    private boolean isAlwaysFocusable() {
        return (this.info.flags & 262144) != 0;
    }

    boolean windowsAreFocusable() {
        return windowsAreFocusable(false);
    }

    boolean windowsAreFocusable(boolean fromUserTouch) {
        if (!fromUserTouch && this.mTargetSdk < 29) {
            int pid = getPid();
            com.android.server.wm.ActivityRecord topFocusedAppOfMyProcess = this.mWmService.mRoot.mTopFocusedAppByProcess.get(java.lang.Integer.valueOf(pid));
            if (topFocusedAppOfMyProcess != null && topFocusedAppOfMyProcess != this) {
                return false;
            }
        }
        return (canReceiveKeys() || isAlwaysFocusable()) && isAttached();
    }

    boolean moveFocusableActivityToTop(java.lang.String reason) {
        int topFocusedDisplayId;
        if (!isFocusable()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, -9024836052864189016L, 0, null, protoLogParam0);
            }
            return false;
        }
        com.android.server.wm.Task rootTask = getRootTask();
        if (rootTask == null) {
            android.util.Slog.w(TAG, "moveFocusableActivityToTop: invalid root task: activity=" + this + " task=" + this.task);
            return false;
        }
        com.android.server.wm.ActivityRecord currentFocusedApp = this.mDisplayContent.mFocusedApp;
        if (this.mRootWindowContainer.getTopFocusedDisplayContent() != null) {
            topFocusedDisplayId = this.mRootWindowContainer.getTopFocusedDisplayContent().getDisplayId();
        } else {
            topFocusedDisplayId = -1;
        }
        if (currentFocusedApp != null && currentFocusedApp.task == this.task && topFocusedDisplayId == this.mDisplayContent.getDisplayId()) {
            com.android.server.wm.Task topFocusableTask = this.mDisplayContent.getTask(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda31
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.ActivityRecord.lambda$moveFocusableActivityToTop$4((com.android.server.wm.Task) obj);
                }
            }, true);
            if (this.task == topFocusableTask) {
                if (currentFocusedApp == this) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[0]) {
                        java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, 134255351804410010L, 0, null, protoLogParam02);
                    }
                } else {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[0]) {
                        java.lang.String protoLogParam03 = java.lang.String.valueOf(this);
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, -1058622321669556178L, 0, null, protoLogParam03);
                    }
                    this.mDisplayContent.setFocusedApp(this);
                    this.mAtmService.mWindowManager.updateFocusedWindowLocked(0, true);
                }
                return !isState(com.android.server.wm.ActivityRecord.State.RESUMED);
            }
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[0]) {
            java.lang.String protoLogParam04 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, 731006689098152100L, 0, null, protoLogParam04);
        }
        rootTask.moveToFront(reason, this.task);
        if (this.mState == com.android.server.wm.ActivityRecord.State.RESUMED && this.mRootWindowContainer.getTopResumedActivity() == this) {
            this.mAtmService.setLastResumedActivityUncheckLocked(this, reason);
        }
        return true;
    }

    static /* synthetic */ boolean lambda$moveFocusableActivityToTop$4(com.android.server.wm.Task t) {
        return t.isLeafTask() && t.isFocusable() && !t.inPinnedWindowingMode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishIfSubActivity(com.android.server.wm.ActivityRecord parent, java.lang.String otherResultWho, int otherRequestCode) {
        if (this.resultTo != parent || this.requestCode != otherRequestCode || !java.util.Objects.equals(this.resultWho, otherResultWho)) {
            return;
        }
        finishIfPossible("request-sub", false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean finishIfSameAffinity(com.android.server.wm.ActivityRecord r) {
        if (!java.util.Objects.equals(r.taskAffinity, this.taskAffinity)) {
            return true;
        }
        r.finishIfPossible("request-affinity", true);
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [android.os.Bundle, java.util.ArrayList<android.app.ResultInfo>, java.util.ArrayList<com.android.internal.content.ReferrerIntent>, java.util.HashSet<java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord>>] */
    /* JADX WARN: Type inference failed for: r0v26 */
    /* JADX WARN: Type inference failed for: r0v27 */
    /* JADX WARN: Type inference failed for: r0v28 */
    private void finishActivityResults(final int i, final android.content.Intent intent, final com.android.server.uri.NeededUriGrants neededUriGrants) {
        ?? r0;
        com.android.server.wm.ActivityRecord activityRecord;
        if (this.resultTo != null) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RESULTS) {
                android.util.Slog.v(TAG_RESULTS, "Adding result to " + this.resultTo + " who=" + this.resultWho + " req=" + this.requestCode + " res=" + i + " data=" + intent);
            }
            if (this.resultTo.mUserId != this.mUserId && intent != null) {
                intent.prepareToLeaveUser(this.mUserId);
            }
            if (this.info.applicationInfo.uid > 0) {
                this.mAtmService.mUgmInternal.grantUriPermissionUncheckedFromIntent(neededUriGrants, this.resultTo.getUriPermissionsLocked());
            }
            final android.os.Binder binder = new android.os.Binder();
            if (android.security.Flags.contentUriPermissionApis()) {
                try {
                    this.resultTo.computeCallerInfo(binder, intent, getUid(), this.mAtmService.getPackageManager().getNameForUid(getUid()), false);
                } catch (android.os.RemoteException e) {
                    throw new java.lang.RuntimeException(e);
                }
            }
            if (!this.mForceSendResultForMediaProjection && !this.resultTo.isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
                activityRecord = null;
                this.resultTo.addResultLocked(this, this.resultWho, this.requestCode, i, intent, binder);
                this.resultTo = activityRecord;
                r0 = activityRecord;
            } else {
                com.android.server.wm.ActivityRecord activityRecord2 = null;
                final com.android.server.wm.ActivityRecord activityRecord3 = this.resultTo;
                this.mAtmService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$finishActivityResults$5(activityRecord3, i, intent, binder, neededUriGrants);
                    }
                });
                activityRecord = activityRecord2;
                this.resultTo = activityRecord;
                r0 = activityRecord;
            }
        } else {
            java.lang.Object obj = null;
            r0 = obj;
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RESULTS) {
                android.util.Slog.v(TAG_RESULTS, "No result destination from " + this);
                r0 = obj;
            }
        }
        this.results = r0;
        this.pendingResults = r0;
        this.newIntents = r0;
        setSavedState(r0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishActivityResults$5(com.android.server.wm.ActivityRecord resultToActivity, int resultCode, android.content.Intent resultData, android.os.IBinder callerToken, com.android.server.uri.NeededUriGrants resultGrants) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                resultToActivity.sendResult(getUid(), this.resultWho, this.requestCode, resultCode, resultData, callerToken, resultGrants, this.mForceSendResultForMediaProjection);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    int finishIfPossible(java.lang.String reason, boolean oomAdj) {
        return finishIfPossible(0, null, null, reason, oomAdj);
    }

    int finishIfPossible(int resultCode, android.content.Intent resultData, com.android.server.uri.NeededUriGrants resultGrants, java.lang.String reason, boolean oomAdj) {
        com.android.server.wm.ActivityRecord nextRunning;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            long protoLogParam1 = resultCode;
            java.lang.String protoLogParam2 = java.lang.String.valueOf(resultData);
            java.lang.String protoLogParam3 = java.lang.String.valueOf(reason);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 3707721620395081349L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), protoLogParam2, protoLogParam3);
        }
        if (this.finishing) {
            android.util.Slog.w(TAG, "Duplicate finish request for r=" + this);
            return 0;
        }
        if (!isInRootTaskLocked()) {
            android.util.Slog.w(TAG, "Finish request when not in root task for r=" + this);
            return 0;
        }
        com.android.server.wm.Task rootTask = getRootTask();
        boolean mayAdjustTop = (isState(com.android.server.wm.ActivityRecord.State.RESUMED) || rootTask.getTopResumedActivity() == null) && rootTask.isFocusedRootTaskOnDisplay() && !this.task.isClearingToReuseTask();
        boolean shouldAdjustGlobalFocus = mayAdjustTop && this.mRootWindowContainer.isTopDisplayFocusedRootTask(rootTask);
        this.mAtmService.deferWindowLayout();
        try {
            this.mTaskSupervisor.mNoHistoryActivities.remove(this);
            makeFinishingLocked();
            if (!isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
                this.mActivityRecordExt.onActivityFinish(this, reason);
            }
            com.android.server.wm.Task task = getTask();
            com.android.server.wm.EventLogTags.writeWmFinishActivity(this.mUserId, java.lang.System.identityHashCode(this), task.mTaskId, this.shortComponentName, reason);
            this.mActivityRecordExt.collectAppRequestFinishAr(this, reason);
            this.mActivityRecordExt.activityPreloadAbort(this, "finishActivity");
            com.android.server.wm.ActivityRecord next = task.getActivityAbove(this);
            if (next != null && (this.intent.getFlags() & 524288) != 0) {
                next.intent.addFlags(524288);
            }
            pauseKeyDispatchingLocked();
            if (mayAdjustTop && task.topRunningActivity(true) == null) {
                this.mActivityRecordExt.notifySysActivityHotLaunch(com.android.server.wm.ActivityRecord.class, this.mActivityComponent);
                task.adjustFocusToNextFocusableTask("finish-top", false, shouldAdjustGlobalFocus);
            }
            finishActivityResults(resultCode, resultData, resultGrants);
            boolean endTask = task.getTopNonFinishingActivity() == null && !task.isClearingToReuseTask();
            com.android.server.wm.WindowContainer<?> trigger = endTask ? task : this;
            boolean withNoneTransition = this.mActivityRecordExt.shouldSkipTransition(reason);
            com.android.server.wm.Transition newTransition = withNoneTransition ? null : this.mTransitionController.requestCloseTransitionIfNeeded(trigger);
            this.mActivityRecordExt.finishIfPossible(this, reason, endTask, newTransition);
            if (newTransition != null) {
                newTransition.collectClose(trigger);
            } else if (this.mTransitionController.isCollecting()) {
                this.mTransitionController.getCollectingTransition().collectClose(trigger);
            }
            if (!isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
                if (!isState(com.android.server.wm.ActivityRecord.State.PAUSING)) {
                    if (this.mVisibleRequested && !this.mActivityRecordExt.shouldBlockPrepareActivityHideTransitionAnimation(this, this.mVisibleRequested)) {
                        if (this.mTransitionController.isShellTransitionsEnabled()) {
                            setVisibility(false);
                            if (newTransition != null) {
                                newTransition.setReady(this.mDisplayContent, true);
                            }
                        } else {
                            prepareActivityHideTransitionAnimation();
                        }
                    }
                    boolean removedActivity = completeFinishing("finishIfPossible") == null;
                    if (oomAdj && isState(com.android.server.wm.ActivityRecord.State.STOPPING)) {
                        this.mAtmService.updateOomAdj();
                    }
                    if (task.onlyHasTaskOverlayActivities(false)) {
                        task.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda18
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                ((com.android.server.wm.ActivityRecord) obj).prepareActivityHideTransitionAnimationIfOvarlay();
                            }
                        });
                    }
                    this.mActivityRecordExt.finishActivity(this, reason, false);
                    return removedActivity ? 2 : 1;
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 5813636479397543744L, 0, null, protoLogParam02);
                }
                if (!this.allDrawn && getDisplayContent() != null) {
                    android.util.Slog.d(TAG, "finishIfPossible mOpeningApps remove r=" + this);
                    getDisplayContent().mOpeningApps.remove(this);
                }
            } else {
                if (endTask) {
                    this.mActivityRecordExt.startCompactMask(getTask());
                    this.mAtmService.getTaskChangeNotificationController().notifyTaskRemovalStarted(task.getTaskInfo());
                }
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY || com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
                    android.util.Slog.v(TAG_TRANSITION, "Prepare close transition: finishing " + this);
                }
                if (!this.mActivityRecordExt.skipPrepareAppTransitionForMirageIfNeed(task, this.mDisplayContent.getDisplayId(), reason)) {
                    this.mDisplayContent.prepareAppTransition(2);
                }
                this.mDisplayContent.getWrapper().getExtImpl().setAnimationThreadUx(true, false, 1);
                setVisibility(false);
                if (this.mLastImeShown && this.mTransitionController.isShellTransitionsEnabled() && (nextRunning = task.topRunningActivity()) != null) {
                    nextRunning.mLastImeShown = true;
                }
                if (getTaskFragment().getPausingActivity() == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                        java.lang.String protoLogParam03 = java.lang.String.valueOf(this);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -3691592300155948194L, 0, null, protoLogParam03);
                    }
                    if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_USER_LEAVING) {
                        android.util.Slog.v(TAG_USER_LEAVING, "finish() => pause with userLeaving=false");
                    }
                    getTaskFragment().startPausing(false, false, null, "finish");
                    this.mAtmService.mSocExt.onBeforeActivitySwitch(getTask(), this);
                }
                this.mActivityRecordExt.onActivityFinish(this, reason);
                if (endTask) {
                    this.mAtmService.getLockTaskController().clearLockedTask(task);
                    if (mayAdjustTop && this.mActivityRecordExt.assignLayersIfNeed(this)) {
                        this.mNeedsZBoost = true;
                        this.mDisplayContent.assignWindowLayers(false);
                    }
                }
            }
            this.mActivityRecordExt.finishActivity(this, reason, true);
            this.mActivityRecordExt.clearAccessControlPassPackages(task, this.packageName, this.mUserId, reason, this);
            return 1;
        } finally {
            this.mAtmService.continueWindowLayout();
        }
    }

    void setForceSendResultForMediaProjection() {
        this.mForceSendResultForMediaProjection = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareActivityHideTransitionAnimationIfOvarlay() {
        if (this.mTaskOverlay) {
            prepareActivityHideTransitionAnimation();
        }
    }

    private void prepareActivityHideTransitionAnimation() {
        com.android.server.wm.DisplayContent dc = this.mDisplayContent;
        dc.prepareAppTransition(2);
        setVisibility(false);
        dc.executeAppTransition();
    }

    com.android.server.wm.ActivityRecord completeFinishing(java.lang.String reason) {
        return completeFinishing(true, reason);
    }

    com.android.server.wm.ActivityRecord completeFinishing(boolean updateVisibility, java.lang.String reason) {
        if (!this.finishing || isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
            throw new java.lang.IllegalArgumentException("Activity must be finishing and not resumed to complete, r=" + this + ", finishing=" + this.finishing + ", state=" + this.mState);
        }
        if (isState(com.android.server.wm.ActivityRecord.State.PAUSING)) {
            return this;
        }
        boolean isCurrentVisible = this.mVisibleRequested || isState(com.android.server.wm.ActivityRecord.State.PAUSED, com.android.server.wm.ActivityRecord.State.STARTED);
        if (updateVisibility && isCurrentVisible && !this.task.isClearingToReuseTask()) {
            boolean ensureVisibility = false;
            if (occludesParent(true)) {
                ensureVisibility = true;
                if (this.mTaskSupervisor.mStoppingActivities.contains(this) && "reset-task".equals(this.mActivityRecordExt.getFinishReason())) {
                    ensureVisibility = false;
                }
            } else if (isKeyguardLocked() && this.mTaskSupervisor.getKeyguardController().topActivityOccludesKeyguard(this)) {
                ensureVisibility = true;
                if (getDisplayArea() != null && this.mActivityRecordExt.hasOtherTopActivityOccludesKeyguard(getDisplayArea().topRunningActivity(true))) {
                    ensureVisibility = false;
                }
            }
            if (ensureVisibility) {
                this.mDisplayContent.ensureActivitiesVisible(null, true);
            }
        }
        boolean activityRemoved = false;
        com.android.server.wm.ActivityRecord next = getDisplayArea() == null ? null : getDisplayArea().topRunningActivity(true);
        boolean delayRemoval = false;
        com.android.server.wm.TaskFragment taskFragment = getTaskFragment();
        if (next != null && taskFragment != null && taskFragment.isEmbedded()) {
            com.android.server.wm.TaskFragment organized = taskFragment.getOrganizedTaskFragment();
            com.android.server.wm.TaskFragment adjacent = organized != null ? organized.getAdjacentTaskFragment() : null;
            if (adjacent != null && next.isDescendantOf(adjacent) && organized.topRunningActivity() == null) {
                delayRemoval = organized.isDelayLastActivityRemoval();
            }
        }
        if (!delayRemoval && this.mActivityRecordExt.shouldDelayRemovalInCompleteFinishing(next)) {
            delayRemoval = true;
        }
        boolean isNextNotYetVisible = (next == null || (next.nowVisible && next.isVisibleRequested())) ? false : true;
        if (isNextNotYetVisible && this.mDisplayContent.isSleeping() && next == next.getTaskFragment().mLastPausedActivity) {
            next.getTaskFragment().clearLastPausedActivity();
        }
        if (isCurrentVisible) {
            if (isNextNotYetVisible || delayRemoval || (next != null && isInTransition() && this.mActivityRecordExt.isZoomMode(next.getWindowingMode()) && !this.mActivityRecordExt.isZoomMode(getWindowingMode()) && isCurrentVisible && this.mOccludesParent)) {
                addToStopping(false, false, "completeFinishing");
                callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.STOPPING, true);
                setState(com.android.server.wm.ActivityRecord.State.STOPPING, "completeFinishing");
            } else if (!addToFinishingAndWaitForIdle()) {
                activityRemoved = destroyIfPossible(reason);
            }
        } else {
            addToFinishingAndWaitForIdle();
            activityRemoved = destroyIfPossible(reason);
        }
        if (activityRemoved) {
            return null;
        }
        return this;
    }

    boolean destroyIfPossible(java.lang.String reason) {
        callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.FINISHING, true);
        setState(com.android.server.wm.ActivityRecord.State.FINISHING, "destroyIfPossible");
        this.mTaskSupervisor.mStoppingActivities.remove(this);
        this.mActivityRecordExt.forceHideByRemoveTask(false);
        com.android.server.wm.Task rootTask = getRootTask();
        com.android.server.wm.TaskDisplayArea taskDisplayArea = getDisplayArea();
        if (taskDisplayArea == null) {
            android.util.Slog.e(TAG_STATES, "getDisplayArea is null");
            return false;
        }
        com.android.server.wm.ActivityRecord next = taskDisplayArea.topRunningActivity();
        boolean isLastRootTaskOverEmptyHome = next == null && rootTask.isFocusedRootTaskOnDisplay() && taskDisplayArea.getOrCreateRootHomeTask() != null;
        if (isLastRootTaskOverEmptyHome) {
            addToFinishingAndWaitForIdle();
            return false;
        }
        makeFinishingLocked();
        boolean activityRemoved = destroyImmediately("finish-imm:" + reason);
        if (next == null) {
            this.mRootWindowContainer.ensureVisibilityAndConfig(null, this.mDisplayContent, true);
            if (this.mDisplayContent.topRunningActivity() == null) {
                this.mTransitionController.setReady(this.mDisplayContent);
            }
        }
        if (activityRemoved) {
            this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTAINERS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(activityRemoved);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTAINERS, -2989211291975863399L, 0, null, protoLogParam0, protoLogParam1);
        }
        return activityRemoved;
    }

    boolean addToFinishingAndWaitForIdle() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 3169053633576517098L, 0, null, protoLogParam0);
        }
        callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.FINISHING, true);
        setState(com.android.server.wm.ActivityRecord.State.FINISHING, "addToFinishingAndWaitForIdle");
        if (!this.mTaskSupervisor.mFinishingActivities.contains(this)) {
            this.mTaskSupervisor.mFinishingActivities.add(this);
        }
        resumeKeyDispatchingLocked();
        return this.mRootWindowContainer.resumeFocusedTasksTopActivities();
    }

    boolean destroyImmediately(java.lang.String reason) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH || com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
            android.util.Slog.v(TAG_SWITCH, "Removing activity from " + reason + ": token=" + this + ", app=" + (hasProcess() ? this.app.mName : "(null)"));
        }
        if (isState(com.android.server.wm.ActivityRecord.State.DESTROYING, com.android.server.wm.ActivityRecord.State.DESTROYED)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(reason);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 9050478058743283018L, 0, null, protoLogParam0, protoLogParam1);
            }
            return false;
        }
        com.android.server.wm.EventLogTags.writeWmDestroyActivity(this.mUserId, java.lang.System.identityHashCode(this), this.task.mTaskId, this.shortComponentName, reason);
        boolean removedFromHistory = false;
        cleanUp(false, false);
        setVisibleRequested(false);
        if (hasProcess()) {
            this.app.removeActivity(this, true);
            if (!this.app.hasActivities()) {
                this.mAtmService.clearHeavyWeightProcessIfEquals(this.app);
            }
            boolean skipDestroy = false;
            try {
                if (isState(com.android.server.wm.ActivityRecord.State.FINISHING)) {
                    this.mAtmService.mSocExt.onActivityStateChanged(this, false);
                }
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
                    android.util.Slog.i(TAG_SWITCH, "Destroying: " + this);
                }
                this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), android.app.servertransaction.DestroyActivityItem.obtain(this.token, this.finishing));
            } catch (java.lang.Exception e) {
                if (this.finishing) {
                    removeFromHistory(reason + " exceptionInScheduleDestroy");
                    removedFromHistory = true;
                    skipDestroy = true;
                }
            }
            this.nowVisible = false;
            if (this.finishing && !skipDestroy) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 5672598223877126839L, 0, null, protoLogParam02);
                }
                callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.DESTROYING, true);
                setState(com.android.server.wm.ActivityRecord.State.DESTROYING, "destroyActivityLocked. finishing and not skipping destroy");
                this.mAtmService.mH.postDelayed(this.mDestroyTimeoutRunnable, 10000L);
                return removedFromHistory;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                java.lang.String protoLogParam03 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -1834399855266808961L, 0, null, protoLogParam03);
            }
            callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.DESTROYED, true);
            setState(com.android.server.wm.ActivityRecord.State.DESTROYED, "destroyActivityLocked. not finishing or skipping destroy");
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_APP) {
                android.util.Slog.v(TAG_APP, "Clearing app during destroy for activity " + this);
            }
            detachFromProcess();
            return removedFromHistory;
        }
        if (this.finishing) {
            removeFromHistory(reason + " hadNoApp");
            return true;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            java.lang.String protoLogParam04 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 3282063745558462269L, 0, null, protoLogParam04);
        }
        callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.DESTROYED, true);
        setState(com.android.server.wm.ActivityRecord.State.DESTROYED, "destroyActivityLocked. not finishing and had no app");
        return false;
    }

    void removeFromHistory(java.lang.String reason) {
        finishActivityResults(0, null, null);
        makeFinishingLocked();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(reason);
            java.lang.String protoLogParam2 = java.lang.String.valueOf(android.os.Debug.getCallers(5));
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 8836546031252812807L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2);
        }
        takeFromHistory();
        removeTimeouts();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 8348126473928520781L, 0, null, protoLogParam02);
        }
        callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.DESTROYED, true);
        setState(com.android.server.wm.ActivityRecord.State.DESTROYED, "removeFromHistory");
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_APP) {
            android.util.Slog.v(TAG_APP, "Clearing app during remove for activity " + this);
        }
        detachFromProcess();
        resumeKeyDispatchingLocked();
        this.mDisplayContent.removeAppToken(this.token);
        cleanUpActivityServices();
        removeUriPermissionsLocked();
    }

    void detachFromProcess() {
        if (this.app != null) {
            this.app.removeActivity(this, false);
        }
        this.app = null;
        this.mInputDispatchingTimeoutMillis = android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
    }

    void makeFinishingLocked() {
        com.android.server.wm.ActivityRecord nextCookieTarget;
        if (this.finishing) {
            return;
        }
        this.finishing = true;
        if (this.mVisible) {
            this.mActivityRecordExt.removeUnVisibleWindow(getUid(), this.packageName);
        }
        if (this.mLaunchCookie != null && this.mState != com.android.server.wm.ActivityRecord.State.RESUMED && this.task != null && !this.task.mInRemoveTask && !this.task.isClearingToReuseTask() && (nextCookieTarget = this.task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda34
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$makeFinishingLocked$7((com.android.server.wm.ActivityRecord) obj);
            }
        }, this, false, false)) != null) {
            nextCookieTarget.mLaunchCookie = this.mLaunchCookie;
            this.mLaunchCookie = null;
        }
        com.android.server.wm.TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null) {
            com.android.server.wm.Task task = taskFragment.getTask();
            if (task != null && task.isClearingToReuseTask() && taskFragment.getTopNonFinishingActivity() == null) {
                taskFragment.mClearedTaskForReuse = true;
            }
            taskFragment.sendTaskFragmentInfoChanged();
        }
        if (this.mAppStopped) {
            abortAndClearOptionsAnimation();
        }
        if (this.mDisplayContent != null) {
            this.mDisplayContent.mUnknownAppVisibilityController.appRemovedOrHidden(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$makeFinishingLocked$7(com.android.server.wm.ActivityRecord r) {
        return r.mLaunchCookie == null && !r.finishing && r.isUid(getUid());
    }

    void destroyed(java.lang.String reason) {
        removeDestroyTimeout();
        if (this.mActivityRecordExt.ignoreTimeOutForNonFinishing(this, reason)) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTAINERS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTAINERS, -8001673213497887656L, 0, null, protoLogParam0);
        }
        if (!isState(com.android.server.wm.ActivityRecord.State.DESTROYING, com.android.server.wm.ActivityRecord.State.DESTROYED, com.android.server.wm.ActivityRecord.State.RESTARTING_PROCESS) && !this.mActivityRecordExt.ignoreTimeOut(this, reason)) {
            throw new java.lang.IllegalStateException("Reported destroyed for activity that is not destroying: r=" + this);
        }
        this.mTaskSupervisor.killTaskProcessesOnDestroyedIfNeeded(this.task);
        if (isInRootTaskLocked()) {
            cleanUp(true, false);
            removeFromHistory(reason);
        }
        this.mRootWindowContainer.resumeFocusedTasksTopActivities();
        this.mActivityRecordExt.onActivityDestroyed(this);
    }

    void cleanUp(boolean cleanServices, boolean setState) {
        if (getTaskFragment() != null) {
            getTaskFragment().cleanUpActivityReferences(this);
        }
        clearLastParentBeforePip();
        this.mActivityRecordExt.clearLastParentBeforeSplitScreen();
        com.android.server.wm.Task rootTask = getRootTask();
        if (rootTask != null) {
            rootTask.abortTranslucentActivityWaiting(this);
        }
        cleanUpSplashScreen();
        if (setState) {
            callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.DESTROYED, true);
            setState(com.android.server.wm.ActivityRecord.State.DESTROYED, "cleanUp");
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_APP) {
                android.util.Slog.v(TAG_APP, "Clearing app during cleanUp for activity " + this);
            }
            detachFromProcess();
        }
        this.mTaskSupervisor.cleanupActivity(this);
        if (this.finishing && this.pendingResults != null) {
            for (java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord> apr : this.pendingResults) {
                com.android.server.am.PendingIntentRecord rec = apr.get();
                if (rec != null) {
                    this.mAtmService.mPendingIntentController.cancelIntentSender(rec, false, 16);
                }
            }
            this.pendingResults = null;
        }
        if (cleanServices) {
            cleanUpActivityServices();
        }
        removeTimeouts();
        clearRelaunching();
    }

    boolean isRelaunching() {
        return this.mPendingRelaunchCount > 0;
    }

    void startRelaunching() {
        if (this.mPendingRelaunchCount == 0) {
            this.mRelaunchStartTime = android.os.SystemClock.elapsedRealtime();
            if (this.mVisibleRequested) {
                this.mDisplayContent.getDisplayPolicy().addRelaunchingApp(this);
            }
        }
        clearAllDrawn();
        this.mPendingRelaunchCount++;
        this.mVisibleSetFromTransferredStartingWindow = false;
    }

    void finishRelaunching() {
        this.mLetterboxUiController.setRelaunchingAfterRequestedOrientationChanged(false);
        this.mTaskSupervisor.getActivityMetricsLogger().notifyActivityRelaunched(this);
        if (this.mPendingRelaunchCount > 0) {
            this.mPendingRelaunchCount--;
            if (this.mPendingRelaunchCount == 0 && !isClientVisible()) {
                finishOrAbortReplacingWindow();
            }
        } else {
            checkKeyguardFlagsChanged();
        }
        com.android.server.wm.Task rootTask = getRootTask();
        if (rootTask != null && rootTask.shouldSleepOrShutDownActivities()) {
            rootTask.ensureActivitiesVisible(null);
        }
    }

    void clearRelaunching() {
        if (this.mPendingRelaunchCount == 0) {
            return;
        }
        this.mPendingRelaunchCount = 0;
        finishOrAbortReplacingWindow();
    }

    void finishOrAbortReplacingWindow() {
        this.mRelaunchStartTime = 0L;
        this.mDisplayContent.getDisplayPolicy().removeRelaunchingApp(this);
    }

    com.android.server.wm.ActivityServiceConnectionsHolder getOrCreateServiceConnectionsHolder() {
        com.android.server.wm.ActivityServiceConnectionsHolder activityServiceConnectionsHolder;
        synchronized (this) {
            if (this.mServiceConnectionsHolder == null) {
                this.mServiceConnectionsHolder = new com.android.server.wm.ActivityServiceConnectionsHolder(this);
            }
            activityServiceConnectionsHolder = this.mServiceConnectionsHolder;
        }
        return activityServiceConnectionsHolder;
    }

    private void cleanUpActivityServices() {
        synchronized (this) {
            if (this.mServiceConnectionsHolder == null) {
                return;
            }
            this.mServiceConnectionsHolder.disconnectActivityFromServices();
            this.mServiceConnectionsHolder = null;
        }
    }

    private void updateVisibleForServiceConnection() {
        this.mVisibleForServiceConnection = this.mVisibleRequested || this.mState == com.android.server.wm.ActivityRecord.State.RESUMED || this.mState == com.android.server.wm.ActivityRecord.State.PAUSING;
    }

    void handleAppDied() {
        boolean remove;
        com.android.server.wm.ActivityRecord top;
        if (android.os.Process.isSdkSandboxUid(getUid())) {
            remove = true;
        } else if ((this.mRelaunchReason == 1 || this.mRelaunchReason == 2) && this.launchCount < 3 && !this.finishing) {
            remove = false;
        } else {
            boolean remove2 = this.mHaveState;
            if ((!remove2 && !this.mActivityRecordExt.isFontPageKilled(getTask(), this) && !this.stateNotNeeded && !isState(com.android.server.wm.ActivityRecord.State.RESTARTING_PROCESS)) || this.finishing) {
                remove = true;
            } else {
                boolean remove3 = this.mVisibleRequested;
                if (!remove3 && this.launchCount > 2 && this.lastLaunchTime > android.os.SystemClock.uptimeMillis() - 60000) {
                    remove = true;
                } else {
                    remove = false;
                }
            }
        }
        if (remove) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                boolean protoLogParam1 = this.mHaveState;
                java.lang.String protoLogParam2 = java.lang.String.valueOf(this.stateNotNeeded);
                boolean protoLogParam3 = this.finishing;
                java.lang.String protoLogParam4 = java.lang.String.valueOf(this.mState);
                java.lang.String protoLogParam5 = java.lang.String.valueOf(android.os.Debug.getCallers(5));
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 587363723665813898L, 204, null, protoLogParam0, java.lang.Boolean.valueOf(protoLogParam1), protoLogParam2, java.lang.Boolean.valueOf(protoLogParam3), protoLogParam4, protoLogParam5);
            }
            if (!this.finishing || (this.app != null && this.app.isRemoved())) {
                android.util.Slog.w(TAG, "Force removing " + this + ": app died, no saved state");
                com.android.server.wm.EventLogTags.writeWmFinishActivity(this.mUserId, java.lang.System.identityHashCode(this), this.task != null ? this.task.mTaskId : -1, this.shortComponentName, "proc died without state saved");
            }
        } else if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_APP) {
            android.util.Slog.v(TAG_APP, "Keeping entry during removeHistory for activity " + this);
        }
        if (this.task != null && this.task.mKillProcessesOnDestroyed) {
            this.mTaskSupervisor.removeTimeoutOfKillProcessesOnProcessDied(this, this.task);
        }
        com.android.server.wm.WindowContainer<?> windowContainer = (remove && this.task != null && this.task.getChildCount() == 1) ? this.task : this;
        com.android.server.wm.Transition newTransit = this.mTransitionController.requestCloseTransitionIfNeeded(windowContainer);
        this.mActivityRecordExt.handleAppDied(this, newTransit);
        if (newTransit != null) {
            newTransit.collectClose(windowContainer);
        } else if (this.mTransitionController.isCollecting()) {
            this.mTransitionController.getCollectingTransition().collectClose(windowContainer);
        }
        this.mActivityRecordExt.activityPreloadAbort(this, "appDied");
        this.mTaskSupervisor.killTaskProcessesOnDestroyedIfNeeded(this.task);
        cleanUp(true, true);
        if (remove) {
            if (this.mStartingData != null && this.mVisible && this.task != null && (top = this.task.topRunningActivity()) != null && !top.mVisible && top.shouldBeVisible()) {
                top.transferStartingWindow(this);
            }
            removeFromHistory("appDied");
        }
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    void removeImmediately() {
        if (this.mState != com.android.server.wm.ActivityRecord.State.DESTROYED) {
            android.util.Slog.w(TAG, "Force remove immediately " + this + " state=" + this.mState);
            destroyImmediately("removeImmediately");
            destroyed("removeImmediately");
        } else {
            onRemovedFromDisplay();
        }
        this.mActivityRecordInputSink.releaseSurfaceControl();
        this.mActivityRecordExt.removeImmediately();
        super.removeImmediately();
    }

    @Override // com.android.server.wm.WindowContainer
    void removeIfPossible() {
        this.mIsExiting = false;
        removeAllWindowsIfPossible();
        removeImmediately();
    }

    @Override // com.android.server.wm.WindowContainer
    boolean handleCompleteDeferredRemoval() {
        if (this.mIsExiting) {
            removeIfPossible();
        }
        return super.handleCompleteDeferredRemoval();
    }

    void onRemovedFromDisplay() {
        boolean selfAnimating;
        if (this.mRemovingFromDisplay) {
            return;
        }
        this.mRemovingFromDisplay = true;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -1842512343787359105L, 0, null, protoLogParam0);
        }
        getDisplayContent().mOpeningApps.remove(this);
        getDisplayContent().mUnknownAppVisibilityController.appRemovedOrHidden(this);
        this.mWmService.mSnapshotController.onAppRemoved(this);
        this.mAtmService.mStartingProcessActivities.remove(this);
        this.mTaskSupervisor.getActivityMetricsLogger().notifyActivityRemoved(this);
        this.mTaskSupervisor.mStoppingActivities.remove(this);
        this.mLetterboxUiController.destroy();
        if (getParent() != null && (getParent() instanceof com.android.server.wm.TaskFragment)) {
            selfAnimating = getParent().isAnimating(5) || getParent().inTransition();
        } else {
            selfAnimating = isAnimating(5) || inTransition();
        }
        boolean delayed = isAnimating(7) && (selfAnimating || this.task == null || !this.task.isVisible());
        if (getDisplayContent().mClosingApps.contains(this)) {
            delayed = true;
            if (this.app == null && this.mActivityRecordExt.isMirageWindowDisplayId(getDisplayContent().getDisplayId())) {
                delayed = false;
            }
        } else if (!delayed && getDisplayContent().mAppTransition.isTransitionSet()) {
            getDisplayContent().mClosingApps.add(this);
            delayed = true;
        } else if (this.mTransitionController.inTransition()) {
            delayed = true;
        }
        if (!delayed) {
            commitVisibility(false, true);
        } else {
            setVisibleRequested(false);
        }
        this.mTransitionController.collect(this);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
            boolean protoLogParam1 = delayed;
            java.lang.String protoLogParam2 = java.lang.String.valueOf(getAnimation());
            boolean protoLogParam3 = isAnimating(3, 1);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 5548174277852675449L, 204, null, protoLogParam02, java.lang.Boolean.valueOf(protoLogParam1), protoLogParam2, java.lang.Boolean.valueOf(protoLogParam3));
        }
        android.util.Slog.d(TAG, java.lang.String.format("removeAppToken: %s delayed=%b parent=%s animating=%b Callers=%s", this, java.lang.Boolean.valueOf(delayed), getParent(), java.lang.Boolean.valueOf(this.mTransitionController.inTransition() || inTransition()), android.os.Debug.getCallers(4)));
        if (this.mStartingData != null) {
            removeStartingWindow();
        }
        if (isAnimating(3, 1) || inTransition()) {
            getDisplayContent().mNoAnimationNotifyOnTransitionFinished.add(this.token);
        }
        if (delayed && !isEmpty()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
                java.lang.String protoLogParam03 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 3478214322581157355L, 0, null, protoLogParam03);
            }
            this.mIsExiting = true;
        } else {
            cancelAnimation();
            removeIfPossible();
        }
        stopFreezingScreen(true, true);
        com.android.server.wm.DisplayContent dc = getDisplayContent();
        if (dc.mFocusedApp == this) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[1]) {
                java.lang.String protoLogParam04 = java.lang.String.valueOf(this);
                long protoLogParam12 = dc.getDisplayId();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -7226216420432530281L, 4, null, protoLogParam04, java.lang.Long.valueOf(protoLogParam12));
            }
            dc.setFocusedApp(null);
            this.mWmService.updateFocusedWindowLocked(0, true);
        }
        if (!delayed) {
            updateReportedVisibilityLocked();
        }
        this.mDisplayContent.mPinnedTaskController.onActivityHidden(this.mActivityComponent);
        this.mDisplayContent.onRunningActivityChanged();
        this.mRemovingFromDisplay = false;
    }

    @Override // com.android.server.wm.WindowToken
    protected boolean isFirstChildWindowGreaterThanSecond(com.android.server.wm.WindowState newWindow, com.android.server.wm.WindowState existingWindow) {
        int type1 = newWindow.mAttrs.type;
        int type2 = existingWindow.mAttrs.type;
        if (type1 == 1 && type2 != 1) {
            return false;
        }
        if (type1 == 1 || type2 != 1) {
            return (type1 == 3 && type2 != 3) || type1 == 3 || type2 != 3;
        }
        return true;
    }

    boolean hasStartingWindow() {
        if (this.mStartingData != null) {
            return true;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if (((com.android.server.wm.WindowState) getChildAt(i)).mAttrs.type == 3) {
                return true;
            }
        }
        return this.mActivityRecordExt.isShowStartingSurfaceLocked(this) && this.mActivityRecordExt.getAppQuickStartingSufaceType() != 2;
    }

    boolean isLastWindow(com.android.server.wm.WindowState win) {
        return this.mChildren.size() == 1 && this.mChildren.get(0) == win;
    }

    @Override // com.android.server.wm.WindowToken
    void addWindow(com.android.server.wm.WindowState w) {
        super.addWindow(w);
        checkKeyguardFlagsChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void removeChild(com.android.server.wm.WindowState child) {
        if (!this.mChildren.contains(child)) {
            return;
        }
        super.removeChild(child);
        checkKeyguardFlagsChanged();
        updateLetterboxSurfaceIfNeeded(child);
    }

    void setAppLayoutChanges(int changes, java.lang.String reason) {
        if (!this.mChildren.isEmpty()) {
            com.android.server.wm.DisplayContent dc = getDisplayContent();
            dc.pendingLayoutChanges |= changes;
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                this.mWmService.mWindowPlacerLocked.debugLayoutRepeats(reason, dc.pendingLayoutChanges);
            }
        }
    }

    private boolean transferStartingWindow(com.android.server.wm.ActivityRecord fromActivity) {
        com.android.server.wm.WindowState tStartingWindow = fromActivity.mStartingWindow;
        if (tStartingWindow != null && fromActivity.mStartingSurface != null) {
            if (tStartingWindow.getParent() == null) {
                return false;
            }
            if (fromActivity.getRequestedConfigurationOrientation() != getRequestedConfigurationOrientation() && getRequestedConfigurationOrientation() != 0 && !"com.tencent.mm".equals(fromActivity.packageName) && !"com.smile.gifmaker".equals(fromActivity.packageName)) {
                return false;
            }
            if (this.mStartingSurface != null || this.mStartingData != null) {
                android.util.Slog.v("WindowManager", "transferStartingWindow, fromToken already add a starting window.");
                removeStartingWindow();
            }
            if (fromActivity.mVisible) {
                this.mDisplayContent.mSkipAppTransitionAnimation = true;
            }
            android.util.Slog.d(TAG, "Moving existing StartingWindow " + tStartingWindow + " from " + fromActivity + " to " + this);
            long origId = android.os.Binder.clearCallingIdentity();
            try {
                if (fromActivity.hasFixedRotationTransform()) {
                    this.mDisplayContent.handleTopActivityLaunchingInDifferentOrientation(this, false);
                }
                this.mStartingData = fromActivity.mStartingData;
                this.mStartingSurface = fromActivity.mStartingSurface;
                this.mStartingWindow = tStartingWindow;
                this.reportedVisible = fromActivity.reportedVisible;
                this.mActivityRecordExt.resetWaitForSyncTransactionCommitIfNeeded(this, fromActivity, this.mStartingData);
                this.mIsSplashScreenWindow = fromActivity.mIsSplashScreenWindow;
                fromActivity.mStartingData = null;
                fromActivity.mStartingSurface = null;
                fromActivity.mStartingWindow = null;
                fromActivity.startingMoved = true;
                tStartingWindow.mToken = this;
                tStartingWindow.mActivityRecord = this;
                android.util.Slog.d(TAG, "Removing StartingWindow " + tStartingWindow + " from " + fromActivity);
                this.mTransitionController.collect(tStartingWindow);
                tStartingWindow.reparent(this, Integer.MAX_VALUE);
                tStartingWindow.clearFrozenInsetsState();
                if (fromActivity.allDrawn) {
                    this.allDrawn = true;
                }
                if (fromActivity.firstWindowDrawn) {
                    this.firstWindowDrawn = true;
                }
                if (fromActivity.isVisible()) {
                    setVisible(true);
                    setVisibleRequested(true);
                    this.mVisibleSetFromTransferredStartingWindow = true;
                }
                setClientVisible(fromActivity.isClientVisible());
                if (fromActivity.isAnimating() && !this.mActivityRecordExt.shouldBlockTransferAnimation(fromActivity, this.mAnimatingActivityRegistry)) {
                    if (fromActivity.mDeferAnimationFinish) {
                        android.util.Slog.e(TAG, "transferAnimation from=" + fromActivity + " which anim is deferfinished, should not transfer to " + this);
                    }
                    transferAnimation(fromActivity);
                    this.mTransitionChangeFlags |= 8;
                    if (this.mAnimatingActivityRegistry != null) {
                        this.mAnimatingActivityRegistry.notifyStarting(this);
                    }
                } else if (this.mTransitionController.getTransitionPlayer() != null) {
                    this.mTransitionChangeFlags |= 8;
                }
                fromActivity.postWindowRemoveStartingWindowCleanup(tStartingWindow);
                fromActivity.mVisibleSetFromTransferredStartingWindow = false;
                this.mActivityRecordExt.transferStartingWindow(fromActivity, this);
                this.mWmService.updateFocusedWindowLocked(3, true);
                getDisplayContent().setLayoutNeeded();
                this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
                return true;
            } finally {
                android.os.Binder.restoreCallingIdentity(origId);
            }
        }
        if (fromActivity.mStartingData == null || !this.mActivityRecordExt.isTransferAllowed(fromActivity, this)) {
            return false;
        }
        android.util.Slog.d(TAG, "Moving pending StartingWindow from " + fromActivity + " to " + this);
        this.mStartingData = fromActivity.mStartingData;
        fromActivity.mStartingData = null;
        fromActivity.startingMoved = true;
        this.mActivityRecordExt.transferPreloadedInfoIfNeed(fromActivity, this);
        scheduleAddStartingWindow();
        return true;
    }

    void transferStartingWindowFromHiddenAboveTokenIfNeeded() {
        com.android.server.wm.WindowState mainWin = findMainWindow(false);
        if (mainWin != null && mainWin.mWinAnimator.getShown()) {
            return;
        }
        this.task.forAllActivities(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$transferStartingWindowFromHiddenAboveTokenIfNeeded$8((com.android.server.wm.ActivityRecord) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$transferStartingWindowFromHiddenAboveTokenIfNeeded$8(com.android.server.wm.ActivityRecord fromActivity) {
        if (fromActivity == this) {
            return true;
        }
        com.android.server.wm.StartingData tmpStartingData = fromActivity.mStartingData;
        if (tmpStartingData != null && tmpStartingData.mAssociatedTask == null && this.mTransitionController.isCollecting(fromActivity) && (tmpStartingData instanceof com.android.server.wm.SnapshotStartingData)) {
            android.graphics.Rect fromBounds = fromActivity.getBounds();
            android.graphics.Rect myBounds = getBounds();
            if (!fromBounds.equals(myBounds)) {
                if (this.mTransitionController.inPlayingTransition(fromActivity)) {
                    this.mTransitionController.setNoAnimation(this);
                    this.mTransitionController.setNoAnimation(fromActivity);
                }
                fromActivity.removeStartingWindow();
                return true;
            }
        }
        return !fromActivity.isVisibleRequested() && transferStartingWindow(fromActivity);
    }

    boolean isKeyguardLocked() {
        return this.mDisplayContent != null ? this.mDisplayContent.isKeyguardLocked() : this.mRootWindowContainer.getDefaultDisplay().isKeyguardLocked();
    }

    void checkKeyguardFlagsChanged() {
        boolean containsDismissKeyguard = containsDismissKeyguardWindow();
        boolean containsShowWhenLocked = containsShowWhenLockedWindow();
        if (containsDismissKeyguard != this.mLastContainsDismissKeyguardWindow || containsShowWhenLocked != this.mLastContainsShowWhenLockedWindow) {
            this.mDisplayContent.notifyKeyguardFlagsChanged();
        }
        this.mLastContainsDismissKeyguardWindow = containsDismissKeyguard;
        this.mLastContainsShowWhenLockedWindow = containsShowWhenLocked;
        this.mLastContainsTurnScreenOnWindow = containsTurnScreenOnWindow();
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
    boolean containsDismissKeyguardWindow() {
        if (isRelaunching()) {
            return this.mLastContainsDismissKeyguardWindow;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if ((((com.android.server.wm.WindowState) this.mChildren.get(i)).mAttrs.flags & 4194304) != 0) {
                return true;
            }
        }
        return false;
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
    boolean containsShowWhenLockedWindow() {
        if (isRelaunching()) {
            return this.mLastContainsShowWhenLockedWindow;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if ((((com.android.server.wm.WindowState) this.mChildren.get(i)).mAttrs.flags & 524288) != 0) {
                return true;
            }
        }
        return false;
    }

    void setShowWhenLocked(boolean showWhenLocked) {
        android.util.Slog.d(TAG, "setShowWhenLocked  from:" + this.mShowWhenLocked + " to " + showWhenLocked + "," + this);
        this.mShowWhenLocked = showWhenLocked;
        this.mAtmService.mRootWindowContainer.ensureActivitiesVisible();
    }

    void setInheritShowWhenLocked(boolean inheritShowWhenLocked) {
        this.mInheritShownWhenLocked = inheritShowWhenLocked;
        this.mAtmService.mRootWindowContainer.ensureActivitiesVisible();
    }

    private static boolean canShowWhenLocked(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.ActivityRecord activity;
        if (r == null || r.getTaskFragment() == null) {
            return false;
        }
        if (canShowWhenLockedInner(r)) {
            return true;
        }
        return r.mInheritShownWhenLocked && (activity = r.getTaskFragment().getActivityBelow(r)) != null && canShowWhenLockedInner(activity);
    }

    private static boolean canShowWhenLockedInner(com.android.server.wm.ActivityRecord r) {
        return !r.inPinnedWindowingMode() && (r.mShowWhenLocked || r.containsShowWhenLockedWindow() || r.mIsUserAlwaysVisible);
    }

    boolean canShowWhenLocked() {
        com.android.server.wm.TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null && taskFragment.getAdjacentTaskFragment() != null && taskFragment.isEmbedded()) {
            com.android.server.wm.TaskFragment adjacentTaskFragment = taskFragment.getAdjacentTaskFragment();
            com.android.server.wm.ActivityRecord r = adjacentTaskFragment.getTopNonFinishingActivity();
            return canShowWhenLocked(this) && canShowWhenLocked(r);
        }
        return canShowWhenLocked(this);
    }

    boolean canShowWindows() {
        return this.mTransitionController.isShellTransitionsEnabled() ? this.mSyncState != 1 : this.allDrawn;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean forAllActivities(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, boolean traverseTopToBottom) {
        return callback.test(this);
    }

    @Override // com.android.server.wm.WindowContainer
    void forAllActivities(java.util.function.Consumer<com.android.server.wm.ActivityRecord> callback, boolean traverseTopToBottom) {
        callback.accept(this);
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.ActivityRecord getActivity(java.util.function.Predicate<com.android.server.wm.ActivityRecord> callback, boolean traverseTopToBottom, com.android.server.wm.ActivityRecord boundary) {
        if (callback.test(this)) {
            return this;
        }
        return null;
    }

    void logStartActivity(int tag, com.android.server.wm.Task task) {
        android.net.Uri data = this.intent.getData();
        java.lang.String strData = data != null ? data.toSafeString() : null;
        android.util.EventLog.writeEvent(tag, java.lang.Integer.valueOf(this.mUserId), java.lang.Integer.valueOf(java.lang.System.identityHashCode(this)), java.lang.Integer.valueOf(task.mTaskId), this.shortComponentName, this.intent.getAction(), this.intent.getType(), strData, java.lang.Integer.valueOf(this.intent.getFlags()));
    }

    com.android.server.uri.UriPermissionOwner getUriPermissionsLocked() {
        if (this.uriPermissions == null) {
            this.uriPermissions = new com.android.server.uri.UriPermissionOwner(this.mAtmService.mUgmInternal, this);
        }
        return this.uriPermissions;
    }

    void addResultLocked(com.android.server.wm.ActivityRecord from, java.lang.String resultWho, int requestCode, int resultCode, android.content.Intent resultData, android.os.IBinder callerToken) {
        com.android.server.wm.ActivityResult r = new com.android.server.wm.ActivityResult(from, resultWho, requestCode, resultCode, resultData, callerToken);
        if (this.results == null) {
            this.results = new java.util.ArrayList<>();
        }
        this.results.add(r);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void removeResultsLocked(com.android.server.wm.ActivityRecord r4, java.lang.String r5, int r6) {
        /*
            r3 = this;
            java.util.ArrayList<android.app.ResultInfo> r0 = r3.results
            if (r0 == 0) goto L38
            java.util.ArrayList<android.app.ResultInfo> r0 = r3.results
            int r0 = r0.size()
            int r0 = r0 + (-1)
        Lc:
            if (r0 < 0) goto L38
            java.util.ArrayList<android.app.ResultInfo> r1 = r3.results
            java.lang.Object r1 = r1.get(r0)
            com.android.server.wm.ActivityResult r1 = (com.android.server.wm.ActivityResult) r1
            com.android.server.wm.ActivityRecord r2 = r1.mFrom
            if (r2 == r4) goto L1b
            goto L35
        L1b:
            java.lang.String r2 = r1.mResultWho
            if (r2 != 0) goto L22
            if (r5 == 0) goto L2b
            goto L35
        L22:
            java.lang.String r2 = r1.mResultWho
            boolean r2 = r2.equals(r5)
            if (r2 != 0) goto L2b
            goto L35
        L2b:
            int r2 = r1.mRequestCode
            if (r2 == r6) goto L30
            goto L35
        L30:
            java.util.ArrayList<android.app.ResultInfo> r2 = r3.results
            r2.remove(r0)
        L35:
            int r0 = r0 + (-1)
            goto Lc
        L38:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityRecord.removeResultsLocked(com.android.server.wm.ActivityRecord, java.lang.String, int):void");
    }

    void sendResult(int callingUid, java.lang.String resultWho, int requestCode, int resultCode, android.content.Intent data, android.os.IBinder callerToken, com.android.server.uri.NeededUriGrants dataGrants) {
        sendResult(callingUid, resultWho, requestCode, resultCode, data, callerToken, dataGrants, false);
    }

    void sendResult(int callingUid, java.lang.String resultWho, int requestCode, int resultCode, android.content.Intent data, android.os.IBinder callerToken, com.android.server.uri.NeededUriGrants dataGrants, boolean forceSendForMediaProjection) {
        java.lang.String str;
        java.util.ArrayList<android.app.ResultInfo> list;
        if (android.security.Flags.contentUriPermissionApis() && !this.mCallerState.hasCaller(callerToken)) {
            try {
                computeCallerInfo(callerToken, data, callingUid, this.mAtmService.getPackageManager().getNameForUid(callingUid), false);
            } catch (android.os.RemoteException e) {
                throw new java.lang.RuntimeException(e);
            }
        }
        if (callingUid > 0) {
            this.mAtmService.mUgmInternal.grantUriPermissionUncheckedFromIntent(dataGrants, getUriPermissionsLocked());
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_RESULTS) {
            android.util.Slog.v(TAG, "Send activity result to " + this + " : who=" + resultWho + " req=" + requestCode + " res=" + resultCode + " data=" + data + " forceSendForMediaProjection=" + forceSendForMediaProjection);
        }
        if (!isState(com.android.server.wm.ActivityRecord.State.RESUMED) || !attachedToProcess()) {
            str = "Exception thrown sending result to ";
        } else {
            try {
                list = new java.util.ArrayList<>();
                str = "Exception thrown sending result to ";
            } catch (java.lang.Exception e2) {
                e = e2;
                str = "Exception thrown sending result to ";
            }
            try {
                list.add(new android.app.ResultInfo(resultWho, requestCode, resultCode, data, callerToken));
                this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), android.app.servertransaction.ActivityResultItem.obtain(this.token, list));
                return;
            } catch (java.lang.Exception e3) {
                e = e3;
                android.util.Slog.w(TAG, str + this, e);
                if (!forceSendForMediaProjection) {
                }
                addResultLocked(null, resultWho, requestCode, resultCode, data, callerToken);
            }
        }
        if (!forceSendForMediaProjection && attachedToProcess() && isState(com.android.server.wm.ActivityRecord.State.STARTED, com.android.server.wm.ActivityRecord.State.PAUSING, com.android.server.wm.ActivityRecord.State.PAUSED, com.android.server.wm.ActivityRecord.State.STOPPING, com.android.server.wm.ActivityRecord.State.STOPPED)) {
            android.app.servertransaction.ClientTransactionItem clientTransactionItemObtain = android.app.servertransaction.ActivityResultItem.obtain(this.token, java.util.List.of(new android.app.ResultInfo(resultWho, requestCode, resultCode, data, callerToken)));
            android.app.servertransaction.ActivityLifecycleItem lifecycleItem = getLifecycleItemForCurrentStateForResult();
            try {
                if (lifecycleItem == null) {
                    android.util.Slog.w(TAG, "Unable to get the lifecycle item for state " + this.mState + " so couldn't immediately send result");
                    this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), clientTransactionItemObtain);
                } else {
                    this.mAtmService.getLifecycleManager().scheduleTransactionAndLifecycleItems(this.app.getThread(), clientTransactionItemObtain, lifecycleItem);
                }
                return;
            } catch (android.os.RemoteException e4) {
                android.util.Slog.w(TAG, str + this, e4);
                return;
            }
        }
        addResultLocked(null, resultWho, requestCode, resultCode, data, callerToken);
    }

    private android.app.servertransaction.ActivityLifecycleItem getLifecycleItemForCurrentStateForResult() {
        switch (this.mState.ordinal()) {
            case 1:
                return android.app.servertransaction.StartActivityItem.obtain(this.token, (android.app.ActivityOptions.SceneTransitionInfo) null);
            case 2:
            default:
                return null;
            case 3:
            case 4:
                return android.app.servertransaction.PauseActivityItem.obtain(this.token);
            case 5:
            case 6:
                return android.app.servertransaction.StopActivityItem.obtain(this.token);
        }
    }

    private void addNewIntentLocked(com.android.internal.content.ReferrerIntent intent) {
        if (this.newIntents == null) {
            this.newIntents = new java.util.ArrayList<>();
        }
        this.newIntents.add(intent);
    }

    final boolean isSleeping() {
        com.android.server.wm.Task rootTask = getRootTask();
        return rootTask != null ? rootTask.shouldSleepActivities() : this.mAtmService.isSleepingLocked();
    }

    final void deliverNewIntentLocked(int callingUid, android.content.Intent intent, com.android.server.uri.NeededUriGrants intentGrants, java.lang.String referrer, boolean isShareIdentityEnabled, int userId, int recipientAppId) {
        android.os.IBinder callerToken = new android.os.Binder();
        if (android.security.Flags.contentUriPermissionApis()) {
            computeCallerInfo(callerToken, intent, callingUid, referrer, isShareIdentityEnabled);
        }
        this.mAtmService.mUgmInternal.grantUriPermissionUncheckedFromIntent(intentGrants, getUriPermissionsLocked());
        if (isShareIdentityEnabled && android.security.Flags.contentUriPermissionApis()) {
            android.content.pm.PackageManagerInternal pmInternal = this.mAtmService.getPackageManagerInternalLocked();
            pmInternal.grantImplicitAccess(userId, intent, recipientAppId, callingUid, true);
        }
        com.android.internal.content.ReferrerIntent rintent = new com.android.internal.content.ReferrerIntent(intent, getFilteredReferrer(referrer), callerToken);
        boolean unsent = true;
        boolean isTopActivityWhileSleeping = isTopRunningActivity() && isSleeping();
        if ((this.mState == com.android.server.wm.ActivityRecord.State.RESUMED || this.mState == com.android.server.wm.ActivityRecord.State.PAUSED || isTopActivityWhileSleeping) && attachedToProcess()) {
            try {
                java.util.ArrayList<com.android.internal.content.ReferrerIntent> ar = new java.util.ArrayList<>(1);
                ar.add(rintent);
                this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), android.app.servertransaction.NewIntentItem.obtain(this.token, ar, this.mState == com.android.server.wm.ActivityRecord.State.RESUMED));
                unsent = false;
                this.mActivityRecordExt.setLastIntentReceived(intent);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Exception thrown sending new intent to " + this, e);
            } catch (java.lang.NullPointerException e2) {
                android.util.Slog.w(TAG, "Exception thrown sending new intent to " + this, e2);
            }
        }
        if (unsent) {
            addNewIntentLocked(rintent);
        }
    }

    void updateOptionsLocked(android.app.ActivityOptions options) {
        if (options != null) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
                android.util.Slog.i(TAG, "Update options for " + this);
            }
            if (this.mPendingOptions != null) {
                this.mPendingOptions.abort();
            }
            setOptions(options);
        }
    }

    boolean getLaunchedFromBubble() {
        return this.mLaunchedFromBubble;
    }

    private void setOptions(android.app.ActivityOptions options) {
        this.mLaunchedFromBubble = options.getLaunchedFromBubble();
        this.mPendingOptions = options;
        this.mActivityRecordExt.setLaunchDisplayId(options.getLaunchDisplayId());
        if (options.getAnimationType() == 13) {
            this.mPendingRemoteAnimation = options.getRemoteAnimationAdapter();
        }
        this.mPendingRemoteTransition = options.getRemoteTransition();
    }

    void applyOptionsAnimation() {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
            android.util.Slog.i(TAG, "Applying options for " + this);
        }
        android.view.RemoteAnimationAdapter tempPendingRemoteAnimation = this.mPendingRemoteAnimation;
        if (tempPendingRemoteAnimation != null) {
            this.mDisplayContent.mAppTransition.overridePendingAppTransitionRemote(tempPendingRemoteAnimation);
            this.mTransitionController.setStatusBarTransitionDelay(tempPendingRemoteAnimation.getStatusBarTransitionDelay());
        } else {
            if (this.mPendingOptions == null) {
                return;
            }
            if (this.mPendingOptions.getAnimationType() == 5) {
                this.mTransitionController.setOverrideAnimation(android.window.TransitionInfo.AnimationOptions.makeSceneTransitionAnimOptions(), null, null);
                return;
            }
            applyOptionsAnimation(this.mPendingOptions, this.intent);
        }
        clearOptionsAnimationForSiblings();
    }

    private void applyOptionsAnimation(android.app.ActivityOptions pendingOptions, android.content.Intent intent) {
        boolean scaleUp;
        int animationType = pendingOptions.getAnimationType();
        com.android.server.wm.DisplayContent displayContent = getDisplayContent();
        android.window.TransitionInfo.AnimationOptions options = null;
        android.os.IRemoteCallback startCallback = null;
        android.os.IRemoteCallback finishCallback = null;
        switch (animationType) {
            case -1:
            case 0:
                break;
            case 1:
                displayContent.mAppTransition.overridePendingAppTransition(pendingOptions.getPackageName(), pendingOptions.getCustomEnterResId(), pendingOptions.getCustomExitResId(), pendingOptions.getCustomBackgroundColor(), pendingOptions.getAnimationStartedListener(), pendingOptions.getAnimationFinishedListener(), pendingOptions.getOverrideTaskTransition());
                options = android.window.TransitionInfo.AnimationOptions.makeCustomAnimOptions(pendingOptions.getPackageName(), pendingOptions.getCustomEnterResId(), pendingOptions.getCustomExitResId(), pendingOptions.getCustomBackgroundColor(), pendingOptions.getOverrideTaskTransition());
                startCallback = pendingOptions.getAnimationStartedListener();
                finishCallback = pendingOptions.getAnimationFinishedListener();
                break;
            case 2:
                displayContent.mAppTransition.overridePendingAppTransitionScaleUp(pendingOptions.getStartX(), pendingOptions.getStartY(), pendingOptions.getWidth(), pendingOptions.getHeight());
                options = android.window.TransitionInfo.AnimationOptions.makeScaleUpAnimOptions(pendingOptions.getStartX(), pendingOptions.getStartY(), pendingOptions.getWidth(), pendingOptions.getHeight());
                if (intent.getSourceBounds() == null) {
                    intent.setSourceBounds(new android.graphics.Rect(pendingOptions.getStartX(), pendingOptions.getStartY(), pendingOptions.getStartX() + pendingOptions.getWidth(), pendingOptions.getStartY() + pendingOptions.getHeight()));
                }
                break;
            case 3:
            case 4:
                scaleUp = animationType == 3;
                android.hardware.HardwareBuffer buffer = pendingOptions.getThumbnail();
                displayContent.mAppTransition.overridePendingAppTransitionThumb(buffer, pendingOptions.getStartX(), pendingOptions.getStartY(), pendingOptions.getAnimationStartedListener(), scaleUp);
                options = android.window.TransitionInfo.AnimationOptions.makeThumbnailAnimOptions(buffer, pendingOptions.getStartX(), pendingOptions.getStartY(), scaleUp);
                startCallback = pendingOptions.getAnimationStartedListener();
                if (intent.getSourceBounds() == null && buffer != null) {
                    intent.setSourceBounds(new android.graphics.Rect(pendingOptions.getStartX(), pendingOptions.getStartY(), pendingOptions.getStartX() + buffer.getWidth(), pendingOptions.getStartY() + buffer.getHeight()));
                }
                break;
            case 5:
            case 6:
            case 7:
            case 10:
            default:
                android.util.Slog.e("WindowManager", "applyOptionsLocked: Unknown animationType=" + animationType);
                break;
            case 8:
            case 9:
                android.view.AppTransitionAnimationSpec[] specs = pendingOptions.getAnimSpecs();
                android.view.IAppTransitionAnimationSpecsFuture specsFuture = pendingOptions.getSpecsFuture();
                if (specsFuture != null) {
                    com.android.server.wm.AppTransition appTransition = displayContent.mAppTransition;
                    android.os.IRemoteCallback animationStartedListener = pendingOptions.getAnimationStartedListener();
                    scaleUp = animationType == 8;
                    appTransition.overridePendingAppTransitionMultiThumbFuture(specsFuture, animationStartedListener, scaleUp);
                } else if (animationType == 9 && specs != null) {
                    displayContent.mAppTransition.overridePendingAppTransitionMultiThumb(specs, pendingOptions.getAnimationStartedListener(), pendingOptions.getAnimationFinishedListener(), false);
                } else {
                    displayContent.mAppTransition.overridePendingAppTransitionAspectScaledThumb(pendingOptions.getThumbnail(), pendingOptions.getStartX(), pendingOptions.getStartY(), pendingOptions.getWidth(), pendingOptions.getHeight(), pendingOptions.getAnimationStartedListener(), animationType == 8);
                    if (intent.getSourceBounds() == null) {
                        intent.setSourceBounds(new android.graphics.Rect(pendingOptions.getStartX(), pendingOptions.getStartY(), pendingOptions.getStartX() + pendingOptions.getWidth(), pendingOptions.getStartY() + pendingOptions.getHeight()));
                    }
                }
                break;
            case 11:
                displayContent.mAppTransition.overridePendingAppTransitionClipReveal(pendingOptions.getStartX(), pendingOptions.getStartY(), pendingOptions.getWidth(), pendingOptions.getHeight());
                options = android.window.TransitionInfo.AnimationOptions.makeClipRevealAnimOptions(pendingOptions.getStartX(), pendingOptions.getStartY(), pendingOptions.getWidth(), pendingOptions.getHeight());
                if (intent.getSourceBounds() == null) {
                    intent.setSourceBounds(new android.graphics.Rect(pendingOptions.getStartX(), pendingOptions.getStartY(), pendingOptions.getStartX() + pendingOptions.getWidth(), pendingOptions.getStartY() + pendingOptions.getHeight()));
                }
                break;
            case 12:
                displayContent.mAppTransition.overridePendingAppTransitionStartCrossProfileApps();
                options = android.window.TransitionInfo.AnimationOptions.makeCrossProfileAnimOptions();
                break;
        }
        if (options != null) {
            this.mTransitionController.setOverrideAnimation(options, startCallback, finishCallback);
        }
    }

    void clearAllDrawn() {
        this.allDrawn = false;
        this.mLastAllDrawn = false;
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
    private boolean allDrawnStatesConsidered() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState child = (com.android.server.wm.WindowState) this.mChildren.get(i);
            if (child.mightAffectAllDrawn() && !child.getDrawnStateEvaluated()) {
                return false;
            }
        }
        return true;
    }

    void updateAllDrawn() {
        int numInteresting;
        if (!this.allDrawn && (numInteresting = this.mNumInterestingWindows) > 0 && allDrawnStatesConsidered() && this.mNumDrawnWindows >= numInteresting && !isRelaunching()) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG, "allDrawn: " + this + " interesting=" + numInteresting + " drawn=" + this.mNumDrawnWindows);
            }
            this.allDrawn = true;
            if (this.mDisplayContent != null) {
                this.mDisplayContent.mUnknownAppVisibilityController.appRemovedOrHidden(this);
            }
            this.mActivityRecordExt.updateAllDrawnActivity(this);
            if (this.mDisplayContent != null) {
                this.mDisplayContent.setLayoutNeeded();
            }
            this.mWmService.mH.obtainMessage(32, this).sendToTarget();
        }
    }

    void abortAndClearOptionsAnimation() {
        if (this.mPendingOptions != null) {
            this.mPendingOptions.abort();
        }
        clearOptionsAnimation();
    }

    void clearOptionsAnimation() {
        this.mPendingOptions = null;
        this.mPendingRemoteAnimation = null;
        this.mPendingRemoteTransition = null;
    }

    void clearOptionsAnimationForSiblings() {
        if (this.task == null) {
            clearOptionsAnimation();
        } else {
            this.task.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda21
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wm.ActivityRecord) obj).clearOptionsAnimation();
                }
            });
        }
    }

    android.app.ActivityOptions getOptions() {
        return this.mPendingOptions;
    }

    android.app.ActivityOptions.SceneTransitionInfo takeSceneTransitionInfo() {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_TRANSITION) {
            android.util.Slog.i(TAG, "Taking SceneTransitionInfo for " + this + " callers=" + android.os.Debug.getCallers(6));
        }
        if (this.mPendingOptions == null) {
            return null;
        }
        android.app.ActivityOptions opts = this.mPendingOptions;
        this.mPendingOptions = null;
        return opts.getSceneTransitionInfo();
    }

    android.window.RemoteTransition takeRemoteTransition() {
        android.window.RemoteTransition out = this.mPendingRemoteTransition;
        this.mPendingRemoteTransition = null;
        return out;
    }

    boolean allowMoveToFront() {
        return this.mPendingOptions == null || !this.mPendingOptions.getAvoidMoveToFront();
    }

    void removeUriPermissionsLocked() {
        if (this.uriPermissions != null) {
            this.uriPermissions.removeUriPermissions();
            this.uriPermissions = null;
        }
    }

    void pauseKeyDispatchingLocked() {
        if (!this.keysPaused) {
            this.keysPaused = true;
            if (getDisplayContent() != null) {
                getDisplayContent().getInputMonitor().pauseDispatchingLw(this);
            }
        }
    }

    void resumeKeyDispatchingLocked() {
        if (this.keysPaused) {
            this.keysPaused = false;
            if (getDisplayContent() != null) {
                getDisplayContent().getInputMonitor().resumeDispatchingLw(this);
            }
        }
    }

    private void updateTaskDescription(java.lang.CharSequence description) {
        this.task.lastDescription = description;
    }

    void setDeferHidingClient(boolean deferHidingClient) {
        if (this.mActivityRecordExt.shouldAvoidDeferHidingClient(this) || this.mDeferHidingClient == deferHidingClient) {
            return;
        }
        this.mDeferHidingClient = deferHidingClient;
        if (!this.mDeferHidingClient && !this.mVisibleRequested) {
            setVisibility(false);
        }
    }

    boolean getDeferHidingClient() {
        return this.mDeferHidingClient;
    }

    boolean canAffectSystemUiFlags() {
        return (this.task == null || !this.task.canAffectSystemUiFlags() || !isVisible() || this.mWaitForEnteringPinnedMode || inPinnedWindowingMode()) ? false : true;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isVisible() {
        return this.mVisible;
    }

    void setVisible(boolean visible) {
        if (visible != this.mVisible) {
            this.mVisible = visible;
            if (this.app != null) {
                this.mTaskSupervisor.onProcessActivityStateChanged(this.app, false);
            }
            scheduleAnimation();
            if (this.mVisible) {
                this.mActivityRecordExt.addVisibleWindow(getUid(), this.packageName, this.info.applicationInfo);
            } else {
                this.mActivityRecordExt.removeUnVisibleWindow(getUid(), this.packageName);
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer
    boolean setVisibleRequested(boolean visible) {
        boolean z = false;
        if (!super.setVisibleRequested(visible)) {
            return false;
        }
        setInsetsFrozen(!visible);
        updateVisibleForServiceConnection();
        if (this.app != null) {
            this.mTaskSupervisor.onProcessActivityStateChanged(this.app, false);
        }
        logAppCompatState();
        if (!visible) {
            com.android.server.wm.InputTarget imeInputTarget = this.mDisplayContent.getImeInputTarget();
            if (imeInputTarget != null && imeInputTarget.getWindowState() != null && imeInputTarget.getWindowState().mActivityRecord == this && this.mDisplayContent.mInputMethodWindow != null && this.mDisplayContent.mInputMethodWindow.isVisible()) {
                z = true;
            }
            this.mLastImeShown = z;
            finishOrAbortReplacingWindow();
        }
        this.mActivityRecordExt.setVisibleRequested(visible, this.mTransitionController);
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    protected boolean onChildVisibleRequestedChanged(com.android.server.wm.WindowContainer child) {
        return false;
    }

    void setVisibility(boolean visible) {
        if (getParent() == null) {
            android.util.Slog.w("WindowManager", "Attempted to set visibility of non-existing app token: " + this.token);
            return;
        }
        if (visible) {
            this.mActivityRecordExt.forceHideByRemoveTask(false);
        }
        if (visible == this.mVisibleRequested && visible == this.mVisible && visible == isClientVisible() && this.mTransitionController.isShellTransitionsEnabled()) {
            return;
        }
        if (visible) {
            this.mDeferHidingClient = false;
        }
        setVisibility(visible, this.mDeferHidingClient);
        this.mAtmService.addWindowLayoutReasons(2);
        this.mTaskSupervisor.getActivityMetricsLogger().notifyVisibilityChanged(this);
        this.mTaskSupervisor.mAppVisibilitiesChangedSinceLastPause = true;
    }

    private void setVisibility(boolean visible, boolean deferHidingClient) {
        com.android.server.wm.AppTransition appTransition = getDisplayContent().mAppTransition;
        if (!visible && !this.mVisibleRequested) {
            if (!deferHidingClient && this.mLastDeferHidingClient) {
                this.mLastDeferHidingClient = deferHidingClient;
                setClientVisible(false);
                return;
            }
            return;
        }
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, "setAppVisibility(" + this.token + ", visible=" + visible + "): " + appTransition + " visible=" + isVisible() + " mVisibleRequested=" + this.mVisibleRequested + " Callers=" + android.os.Debug.getCallers(6));
        } else {
            android.util.Slog.d(TAG, "setAppVisibility(" + this.token + ", visible=" + visible + "): " + appTransition + " visible=" + isVisible() + " mVisibleRequested=" + this.mVisibleRequested);
        }
        boolean isCollecting = false;
        boolean inFinishingTransition = false;
        boolean inPlayingTransition = false;
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            isCollecting = this.mTransitionController.isCollecting();
            if (!isCollecting) {
                inFinishingTransition = this.mTransitionController.inFinishingTransition(this);
                inPlayingTransition = this.mTransitionController.inPlayingTransition(this);
                if (!inFinishingTransition && !this.mActivityRecordExt.hideEmbeddedSurfaceBeforeReparent()) {
                    if (DEBUG_PANIC) {
                        android.util.Slog.e(TAG, "setVisibility=" + visible + " while transition is not collecting or finishing " + this + " caller=" + android.os.Debug.getCallers(8));
                    }
                    if (visible) {
                        if (!this.mDisplayContent.isSleeping() || canShowWhenLocked()) {
                            this.mTransitionController.onVisibleWithoutCollectingTransition(this, android.os.Debug.getCallers(1, 1));
                        }
                    } else if (!this.mDisplayContent.isSleeping()) {
                        android.util.Slog.w(TAG, "Set invisible without transition " + this);
                    }
                }
            } else if (!this.mActivityRecordExt.skipCollectWhenSetVisibleRequest(visible)) {
                this.mTransitionController.collect(this);
            }
        }
        onChildVisibilityRequested(visible);
        com.android.server.wm.DisplayContent displayContent = getDisplayContent();
        displayContent.mOpeningApps.remove(this);
        displayContent.mClosingApps.remove(this);
        setVisibleRequested(visible);
        this.mLastDeferHidingClient = deferHidingClient;
        if (!visible) {
            if (this.startingMoved && !this.firstWindowDrawn && hasChild()) {
                setClientVisible(false);
            }
        } else {
            if (!appTransition.isTransitionSet() && appTransition.isReady()) {
                if (DEBUG_PANIC) {
                    android.util.Slog.d(TAG, "setAppVisibility, adding " + this + " to mOpeningApps, isTransitionSet()=" + appTransition.isTransitionSet() + ", isReady()=" + appTransition.isReady());
                }
                displayContent.mOpeningApps.add(this);
            }
            this.startingMoved = false;
            if (!isVisible() || this.mAppStopped) {
                clearAllDrawn();
                if (!isVisible() && (!isClientVisible() || this.mActivityRecordExt.resetDrawStateWhenSetAppVisible())) {
                    forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda37
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$setVisibility$9((com.android.server.wm.WindowState) obj);
                        }
                    }, true);
                }
            }
            setClientVisible(true);
            requestUpdateWallpaperIfNeeded();
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1728033820691545386L, 0, null, protoLogParam0);
            }
            this.mAppStopped = false;
            transferStartingWindowFromHiddenAboveTokenIfNeeded();
        }
        if (isCollecting) {
            if (!visible) {
                if (this.mTransitionController.inPlayingTransition(this)) {
                    this.mTransitionChangeFlags |= 32768;
                } else if (this.mTransitionController.inFinishingTransition(this) && !this.mActivityRecordExt.skipAddNoAnimationFlag(this)) {
                    this.mTransitionChangeFlags |= 294912;
                }
            } else {
                this.mTransitionChangeFlags &= -32769;
            }
            android.util.Slog.d(TAG, java.lang.String.format("setVisibility(%s, visible=%b) defer commitVisibility for transition collecting", this.token, java.lang.Boolean.valueOf(visible)));
            return;
        }
        if (inPlayingTransition || inFinishingTransition) {
            android.util.Slog.d(TAG, "setVisibility defer change visibility for:" + this + " visible:" + visible);
            this.mTransitionController.mStateValidators.add(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setVisibility$10();
                }
            });
        }
        if (inFinishingTransition || this.mActivityRecordExt.deferCommitVisibilityIfNeed("setVisibleRequested")) {
            this.mTransitionController.mValidateCommitVis.add(this);
            android.util.Slog.d(TAG, java.lang.String.format("setVisibility(%s, visible=%b) defer commitVisibility for inFinishingTransition", this.token, java.lang.Boolean.valueOf(visible)));
        } else {
            if (deferCommitVisibilityChange(visible)) {
                return;
            }
            commitVisibility(visible, true);
            updateReportedVisibilityLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setVisibility$9(com.android.server.wm.WindowState w) {
        if (w.mWinAnimator.mDrawState == 4 && this.mActivityRecordExt.resetDrawStateIfNeed(w, "setVisibility") && !this.mActivityRecordExt.isWindowSurfaceSaved(w)) {
            w.mWinAnimator.resetDrawState();
            w.forceReportingResized();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setVisibility$10() {
        android.util.Slog.d(TAG, "setVisibility after transition for:" + this);
        if (!isVisibleRequested() || this.mSurfaceControl == null || this.mDisplayContent == null) {
            return;
        }
        commitVisibility(true, false);
        getSyncTransaction().show(this.mSurfaceControl);
        this.mLastSurfaceShowing = true;
        for (com.android.server.wm.WindowContainer<?> p = getParent(); p != null && p != this.mDisplayContent; p = p.getParent()) {
            if (p.mSurfaceControl != null) {
                p.getSyncTransaction().show(p.mSurfaceControl);
                com.android.server.wm.Task task = p.asTask();
                if (task != null) {
                    task.mLastSurfaceShowing = true;
                }
            }
        }
        scheduleAnimation();
    }

    private boolean deferCommitVisibilityChange(boolean visible) {
        com.android.server.wm.WindowState win;
        com.android.server.wm.ActivityRecord focusedActivity;
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            return false;
        }
        if (!this.mDisplayContent.mAppTransition.isTransitionSet() && (isActivityTypeHome() || !isAnimating(2, 8))) {
            return false;
        }
        if (this.mWaitForEnteringPinnedMode && this.mVisible == visible) {
            return false;
        }
        boolean ignoreScreenOn = canTurnScreenOn() || this.mTaskSupervisor.getKeyguardController().isKeyguardGoingAway(this.mDisplayContent.mDisplayId);
        if (!okToAnimate(true, ignoreScreenOn) || this.mActivityRecordExt.shouldSkipAppTransition(this)) {
            return false;
        }
        if (visible) {
            if (DEBUG_PANIC) {
                android.util.Slog.d(TAG, "setAppVisibility, adding " + this + " to mOpeningApps, visible=" + visible);
            }
            this.mDisplayContent.mOpeningApps.add(this);
            this.mEnteringAnimation = true;
        } else if (this.mVisible) {
            this.mDisplayContent.mClosingApps.add(this);
            this.mEnteringAnimation = false;
        }
        if ((this.mDisplayContent.mAppTransition.getTransitFlags() & 32) != 0 && (win = this.mDisplayContent.findFocusedWindow()) != null && (focusedActivity = win.mActivityRecord) != null) {
            if (DEBUG_PANIC) {
                android.util.Slog.d(TAG, "setAppVisibility, TransitFlags=TRANSIT_FLAG_OPEN_BEHIND，  adding " + focusedActivity + " to mOpeningApps");
            }
            this.mDisplayContent.mOpeningApps.add(focusedActivity);
        }
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean applyAnimation(android.view.WindowManager.LayoutParams lp, int transit, boolean enter, boolean isVoiceInteraction, java.util.ArrayList<com.android.server.wm.WindowContainer> sources) {
        if ((this.mTransitionChangeFlags & 8) != 0) {
            return false;
        }
        this.mRequestForceTransition = false;
        return super.applyAnimation(lp, transit, enter, isVoiceInteraction, sources);
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
    void commitVisibility(boolean visible, boolean performLayout, boolean fromTransition) {
        this.mActivityRecordExt.resetUseTransferredAnimIfRequired(this.mVisibleSetFromTransferredStartingWindow, visible);
        this.mVisibleSetFromTransferredStartingWindow = false;
        if (visible == isVisible()) {
            return;
        }
        if (!visible) {
            this.mActivityRecordExt.clearAccessControlPassPackages(this.task, this.packageName, this.mUserId, "notVisible", this);
        }
        int windowsCount = this.mChildren.size();
        boolean runningAnimation = com.android.server.wm.WindowManagerService.sEnableShellTransitions ? visible : isAnimating(2, 1);
        for (int i = 0; i < windowsCount; i++) {
            ((com.android.server.wm.WindowState) this.mChildren.get(i)).onAppVisibilityChanged(visible, runningAnimation);
        }
        setVisible(visible);
        setVisibleRequested(visible);
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, java.lang.String.format("commitVisibility: %s: visible=%b visibleRequested=%b, isInTransition=%b, runningAnimation=%b, caller=%s", this, java.lang.Boolean.valueOf(isVisible()), java.lang.Boolean.valueOf(this.mVisibleRequested), java.lang.Boolean.valueOf(isInTransition()), java.lang.Boolean.valueOf(runningAnimation), android.os.Debug.getCallers(6)));
        } else {
            android.util.Slog.d(TAG, java.lang.String.format("commitVisibility: %s: visible=%b visibleRequested=%b, isInTransition=%b, runningAnimation=%b ", this, java.lang.Boolean.valueOf(isVisible()), java.lang.Boolean.valueOf(this.mVisibleRequested), java.lang.Boolean.valueOf(isInTransition()), java.lang.Boolean.valueOf(runningAnimation)));
        }
        this.mActivityRecordExt.notifyActivityRecordVisible(this, visible);
        if (!visible) {
            stopFreezingScreen(true, true);
        } else {
            if (this.mStartingWindow != null && !this.mStartingWindow.isDrawn() && ((this.firstWindowDrawn || this.allDrawn) && this.mActivityRecordExt.shouldClearStartingPolicyVisibility(this))) {
                this.mStartingWindow.clearPolicyVisibilityFlag(1);
                this.mStartingWindow.mLegacyPolicyVisibilityAfterAnim = false;
            }
            final com.android.server.wm.WindowManagerService windowManagerService = this.mWmService;
            java.util.Objects.requireNonNull(windowManagerService);
            forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    windowManagerService.makeWindowFreezingScreenIfNeededLocked((com.android.server.wm.WindowState) obj);
                }
            }, true);
        }
        for (com.android.server.wm.Task task = getOrganizedTask(); task != null; task = task.getParent().asTask()) {
            task.dispatchTaskInfoChangedIfNeeded(false);
        }
        com.android.server.wm.DisplayContent displayContent = getDisplayContent();
        displayContent.getInputMonitor().setUpdateInputWindowsNeededLw();
        if (performLayout) {
            this.mWmService.updateFocusedWindowLocked(3, false);
            this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
        }
        displayContent.getInputMonitor().updateInputWindowsLw(false);
        this.mTransitionChangeFlags = 0;
        postApplyAnimation(visible, fromTransition);
    }

    void commitVisibility(boolean visible, boolean performLayout) {
        commitVisibility(visible, performLayout, false);
    }

    void setNeedsLetterboxedAnimation(boolean needsLetterboxedAnimation) {
        this.mNeedsLetterboxedAnimation = needsLetterboxedAnimation;
    }

    boolean isNeedsLetterboxedAnimation() {
        return this.mNeedsLetterboxedAnimation;
    }

    boolean isInLetterboxAnimation() {
        return this.mNeedsLetterboxedAnimation && isAnimating();
    }

    private void postApplyAnimation(boolean visible, boolean fromTransition) {
        boolean usingShellTransitions = this.mTransitionController.isShellTransitionsEnabled();
        boolean delayed = !usingShellTransitions && isAnimating(6, 25);
        if (!delayed && !usingShellTransitions) {
            onAnimationFinished(1, null);
            if (visible) {
                this.mEnteringAnimation = true;
                this.mWmService.mActivityManagerAppTransitionNotifier.onAppTransitionFinishedLocked(this.token);
            }
        }
        if (visible || (this.mState != com.android.server.wm.ActivityRecord.State.RESUMED && (usingShellTransitions || !isAnimating(2, 9)))) {
            setClientVisible(visible);
        }
        com.android.server.wm.DisplayContent displayContent = getDisplayContent();
        if (!visible) {
            this.mImeInsetsFrozenUntilStartInput = true;
        }
        if (!displayContent.mClosingApps.contains(this) && !displayContent.mOpeningApps.contains(this) && !fromTransition) {
            this.mWmService.mSnapshotController.notifyAppVisibilityChanged(this, visible);
        }
        if (!usingShellTransitions && !isVisible() && !delayed && !displayContent.mAppTransition.isTransitionSet()) {
            forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda39
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$postApplyAnimation$11((com.android.server.wm.WindowState) obj);
                }
            }, true);
            scheduleAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$postApplyAnimation$11(com.android.server.wm.WindowState win) {
        win.mWinAnimator.hide(getPendingTransaction(), "immediately hidden");
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
    void commitFinishDrawing(android.view.SurfaceControl.Transaction t) {
        boolean committed = false;
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            committed |= ((com.android.server.wm.WindowState) this.mChildren.get(i)).commitFinishDrawing(t);
        }
        if (committed) {
            requestUpdateWallpaperIfNeeded();
        }
    }

    boolean shouldApplyAnimation(boolean visible) {
        if (this.mActivityRecordExt.shouldApplyAnimation(this, visible)) {
            return isVisible() != visible || this.mRequestForceTransition || (!isVisible() && this.mIsExiting);
        }
        return false;
    }

    void setRecentsScreenshotEnabled(boolean enabled) {
        this.mEnableRecentsScreenshot = enabled;
    }

    boolean shouldUseAppThemeSnapshot() {
        return !this.mEnableRecentsScreenshot || forAllWindows(new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda30
            public final boolean apply(java.lang.Object obj) {
                return this.f$0.lambda$shouldUseAppThemeSnapshot$12((com.android.server.wm.WindowState) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$shouldUseAppThemeSnapshot$12(com.android.server.wm.WindowState w) {
        return this.mActivityRecordExt.shouldUseAppThemeSnapshot(w, w.isSecureLocked());
    }

    void setCurrentLaunchCanTurnScreenOn(boolean currentLaunchCanTurnScreenOn) {
        this.mCurrentLaunchCanTurnScreenOn = currentLaunchCanTurnScreenOn;
    }

    boolean currentLaunchCanTurnScreenOn() {
        return this.mCurrentLaunchCanTurnScreenOn;
    }

    void setState(com.android.server.wm.ActivityRecord.State state, java.lang.String reason) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(getState());
            java.lang.String protoLogParam2 = java.lang.String.valueOf(state);
            java.lang.String protoLogParam3 = java.lang.String.valueOf(reason);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -6873410057142191118L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2, protoLogParam3);
        }
        if (state == this.mState && !this.mActivityRecordExt.updateActvityState(this)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(state);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 4437231720834282527L, 0, null, protoLogParam02);
            }
            this.mActivityRecordExt.updateActivityStateChanged(this, getTaskFragment(), state, reason);
            return;
        }
        com.android.server.wm.ActivityRecord.State preState = this.mState;
        this.mState = state;
        this.mActivityRecordExt.updateActvityResumeTimeStamp(this);
        callServiceTrackeronActivityStatechange(state, false);
        if (getTaskFragment() != null) {
            getTaskFragment().onActivityStateChanged(this, state, reason);
        }
        this.mActivityRecordExt.onActivityStateChanged(preState, state);
        if (state == com.android.server.wm.ActivityRecord.State.STOPPING && !isSleeping() && getParent() == null) {
            android.util.Slog.w("WindowManager", "Attempted to notify stopping on non-existing app token: " + this.token);
            return;
        }
        updateVisibleForServiceConnection();
        if (this.app != null) {
            this.mTaskSupervisor.onProcessActivityStateChanged(this.app, false);
        }
        this.mActivityRecordExt.hookLifecyclePause(reason, this.shortComponentName, state.toString());
        switch (state.ordinal()) {
            case 2:
                this.mAtmService.updateBatteryStats(this, true);
                this.mAtmService.updateActivityUsageStats(this, 1);
            case 1:
                if (this.app != null) {
                    this.app.updateProcessInfo(false, true, true, true);
                }
                this.mAtmService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda27
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.notifyActivityStartedToContentCaptureService();
                    }
                });
                break;
            case 4:
                this.mAtmService.updateBatteryStats(this, false);
                this.mAtmService.updateActivityUsageStats(this, 2);
                this.mActivityRecordExt.notifyActivityPaused(this.task, this);
                break;
            case 5:
                if (preState == com.android.server.wm.ActivityRecord.State.RESUMED) {
                    this.mAtmService.updateActivityUsageStats(this, 2);
                }
                break;
            case 6:
                this.mAtmService.updateActivityUsageStats(this, 23);
                if (this.mDisplayContent != null) {
                    this.mDisplayContent.mUnknownAppVisibilityController.appRemovedOrHidden(this);
                }
                break;
            case 9:
                if (this.app != null && (this.mVisible || this.mVisibleRequested)) {
                    this.mAtmService.updateBatteryStats(this, false);
                }
                this.mAtmService.updateActivityUsageStats(this, 24);
            case 8:
                if (this.app != null && !this.app.hasActivities()) {
                    this.app.updateProcessInfo(true, false, true, false);
                }
                break;
        }
        this.mActivityRecordExt.setStateForVisible(preState, state, getUid(), this.packageName, this.info.applicationInfo, java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
    }

    void callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State state, boolean early_notify) {
        this.mAtmService.mTaskSupervisor.notifyServiceTracker(state, early_notify, this, this.createTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyActivityStartedToContentCaptureService() {
        com.android.server.contentcapture.ContentCaptureManagerInternal contentCaptureService = (com.android.server.contentcapture.ContentCaptureManagerInternal) com.android.server.LocalServices.getService(com.android.server.contentcapture.ContentCaptureManagerInternal.class);
        if (contentCaptureService != null) {
            contentCaptureService.notifyActivityEvent(this.mUserId, this.mActivityComponent, 10000, new android.app.assist.ActivityId(getTask() != null ? getTask().mTaskId : -1, this.shareableActivityToken));
            contentCaptureService.sendActivityStartAssistData(this.mUserId, this.shareableActivityToken, this.intent);
        }
    }

    com.android.server.wm.ActivityRecord.State getState() {
        return this.mState;
    }

    boolean isState(com.android.server.wm.ActivityRecord.State state) {
        return state == this.mState;
    }

    boolean isState(com.android.server.wm.ActivityRecord.State state1, com.android.server.wm.ActivityRecord.State state2) {
        return state1 == this.mState || state2 == this.mState;
    }

    boolean isState(com.android.server.wm.ActivityRecord.State state1, com.android.server.wm.ActivityRecord.State state2, com.android.server.wm.ActivityRecord.State state3) {
        return state1 == this.mState || state2 == this.mState || state3 == this.mState;
    }

    boolean isState(com.android.server.wm.ActivityRecord.State state1, com.android.server.wm.ActivityRecord.State state2, com.android.server.wm.ActivityRecord.State state3, com.android.server.wm.ActivityRecord.State state4) {
        return state1 == this.mState || state2 == this.mState || state3 == this.mState || state4 == this.mState;
    }

    boolean isState(com.android.server.wm.ActivityRecord.State state1, com.android.server.wm.ActivityRecord.State state2, com.android.server.wm.ActivityRecord.State state3, com.android.server.wm.ActivityRecord.State state4, com.android.server.wm.ActivityRecord.State state5) {
        return state1 == this.mState || state2 == this.mState || state3 == this.mState || state4 == this.mState || state5 == this.mState;
    }

    boolean isState(com.android.server.wm.ActivityRecord.State state1, com.android.server.wm.ActivityRecord.State state2, com.android.server.wm.ActivityRecord.State state3, com.android.server.wm.ActivityRecord.State state4, com.android.server.wm.ActivityRecord.State state5, com.android.server.wm.ActivityRecord.State state6) {
        return state1 == this.mState || state2 == this.mState || state3 == this.mState || state4 == this.mState || state5 == this.mState || state6 == this.mState;
    }

    void destroySurfaces() {
        destroySurfaces(false);
    }

    private void destroySurfaces(boolean cleanupOnResume) {
        boolean destroyedSomething = false;
        java.util.ArrayList<com.android.server.wm.WindowState> children = new java.util.ArrayList<>(this.mChildren);
        for (int i = children.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState win = children.get(i);
            if (!this.mActivityRecordExt.handleDestroySurfaces(this.packageName, win.mAttrs.type)) {
                destroyedSomething |= win.destroySurface(cleanupOnResume, this.mAppStopped);
            }
        }
        if (destroyedSomething) {
            com.android.server.wm.DisplayContent dc = getDisplayContent();
            dc.assignWindowLayers(true);
            updateLetterboxSurfaceIfNeeded(null);
        }
    }

    void notifyAppResumed() {
        if (getParent() == null) {
            android.util.Slog.w("WindowManager", "Attempted to notify resumed of non-existing app token: " + this.token);
            return;
        }
        boolean wasStopped = this.mAppStopped;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 926038819327785799L, 3, null, java.lang.Boolean.valueOf(wasStopped), protoLogParam1);
        }
        this.mAppStopped = false;
        if (this.mAtmService.getActivityStartController().isInExecution()) {
            setCurrentLaunchCanTurnScreenOn(true);
        }
        if (!wasStopped) {
            destroySurfaces(true);
        }
    }

    void notifyUnknownVisibilityLaunchedForKeyguardTransition() {
        if (this.noDisplay || !isKeyguardLocked()) {
            return;
        }
        this.mDisplayContent.mUnknownAppVisibilityController.notifyLaunched(this);
    }

    private boolean shouldBeVisible(boolean behindOccludedContainer, boolean ignoringKeyguard) {
        if (this.mActivityRecordExt.shouldBeVisible(behindOccludedContainer, getDisplayId())) {
            return true;
        }
        updateVisibilityIgnoringKeyguard(behindOccludedContainer);
        if (ignoringKeyguard) {
            return this.visibleIgnoringKeyguard;
        }
        return shouldBeVisibleUnchecked();
    }

    boolean shouldBeVisibleUnchecked() {
        com.android.server.wm.Task rootTask = getRootTask();
        if (isActivityTypeHome() && ((com.android.server.wm.IMirageWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IMirageWindowManagerExt.class).create()).shouldForceLauncherVisible()) {
            return true;
        }
        if (this.mActivityRecordExt.shouldMakeHomeActivityVisibleOnSecondary(this, this.mTaskSupervisor.getKeyguardController())) {
            android.util.Slog.d(TAG, "activity: " + this + " shouldBeVisibleUnchecked");
            return true;
        }
        if (rootTask == null || !this.visibleIgnoringKeyguard) {
            return false;
        }
        if ((inPinnedWindowingMode() && rootTask.isForceHidden()) || hasOverlayOverUntrustedModeEmbedded()) {
            return false;
        }
        if (this.mActivityRecordExt.shouldBeVisible(!this.visibleIgnoringKeyguard, getDisplayId())) {
            return true;
        }
        if (this.mActivityRecordExt.isForceHidden()) {
            return false;
        }
        if (this.mDisplayContent.isSleeping()) {
            return canTurnScreenOn();
        }
        if (this.mActivityRecordExt.skipCheckKeyguardVisibility()) {
            return true;
        }
        return this.mTaskSupervisor.getKeyguardController().checkKeyguardVisibility(this);
    }

    boolean hasOverlayOverUntrustedModeEmbedded() {
        if (!isEmbeddedInUntrustedMode() || getTask() == null) {
            return false;
        }
        com.android.server.wm.ActivityRecord differentUidOverlayActivity = getTask().getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda35
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$hasOverlayOverUntrustedModeEmbedded$13((com.android.server.wm.ActivityRecord) obj);
            }
        }, this, false, false);
        return differentUidOverlayActivity != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$hasOverlayOverUntrustedModeEmbedded$13(com.android.server.wm.ActivityRecord a) {
        return (a.finishing || a.getUid() == getUid()) ? false : true;
    }

    void updateVisibilityIgnoringKeyguard(boolean behindOccludedContainer) {
        this.visibleIgnoringKeyguard = (!behindOccludedContainer || this.mLaunchTaskBehind) && showToCurrentUser();
    }

    boolean shouldBeVisible() {
        return shouldBeVisible(false);
    }

    boolean shouldBeVisible(boolean ignoringKeyguard) {
        com.android.server.wm.Task task = getTask();
        if (task == null) {
            return false;
        }
        boolean behindOccludedContainer = (task.shouldBeVisible(null) && task.getOccludingActivityAbove(this) == null) ? false : true;
        return shouldBeVisible(behindOccludedContainer, ignoringKeyguard);
    }

    void makeVisibleIfNeeded(com.android.server.wm.ActivityRecord starting, boolean reportToClient) {
        getRootTask();
        if ((this.mState == com.android.server.wm.ActivityRecord.State.RESUMED && this.mVisibleRequested) || this == starting) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.d(TAG_VISIBILITY, "Not making visible, r=" + this + " state=" + this.mState + " starting=" + starting);
                return;
            }
            return;
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v(TAG_VISIBILITY, "Making visible and scheduling visibility: " + this);
        }
        com.android.server.wm.Task rootTask = getRootTask();
        try {
            if (rootTask.mTranslucentActivityWaiting != null) {
                updateOptionsLocked(this.returningOptions);
                rootTask.mUndrawnActivitiesBelowTopTranslucent.add(this);
            }
            setVisibility(true);
            this.app.postPendingUiCleanMsg(true);
            if (reportToClient) {
                this.mClientVisibilityDeferred = false;
                makeActiveIfNeeded(starting);
            } else {
                this.mClientVisibilityDeferred = true;
            }
            this.mTaskSupervisor.mStoppingActivities.remove(this);
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Exception thrown making visible: " + this.intent.getComponent(), e);
        }
        handleAlreadyVisible();
    }

    void makeInvisible() {
        boolean deferHidingClient;
        if (!this.mVisibleRequested) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG_VISIBILITY, "Already invisible: " + this);
                return;
            }
            return;
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v(TAG_VISIBILITY, "Making invisible: " + this + ", state=" + getState());
        }
        try {
            boolean canEnterPictureInPicture = checkEnterPictureInPictureState("makeInvisible", true);
            if (canEnterPictureInPicture && !isState(com.android.server.wm.ActivityRecord.State.STARTED, com.android.server.wm.ActivityRecord.State.STOPPING, com.android.server.wm.ActivityRecord.State.STOPPED, com.android.server.wm.ActivityRecord.State.PAUSED)) {
                deferHidingClient = true;
            } else {
                deferHidingClient = false;
            }
            setDeferHidingClient(deferHidingClient);
            setVisibility(false);
            switch (getState()) {
                case INITIALIZING:
                case STARTED:
                case PAUSING:
                case PAUSED:
                    break;
                case RESUMED:
                    if (deferHidingClient && this.mActivityRecordExt.isMirageWindowDisplayId(getDisplayContent().getDisplayId())) {
                        getTaskFragment().startPausing(false, null, "makeInvisible");
                        return;
                    }
                    break;
                case STOPPING:
                case STOPPED:
                    this.supportsEnterPipOnTaskSwitch = false;
                    return;
                default:
                    return;
            }
            addToStopping(true, canEnterPictureInPicture, "makeInvisible");
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Exception thrown making hidden: " + this.intent.getComponent(), e);
        }
    }

    boolean makeActiveIfNeeded(com.android.server.wm.ActivityRecord activeActivity) {
        if (shouldResumeActivity(activeActivity)) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG_VISIBILITY, "Resume visible activity, " + this);
            }
            return getRootTask().resumeTopActivityUncheckedLocked(activeActivity, null);
        }
        if (shouldPauseActivity(activeActivity)) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG_VISIBILITY, "Pause visible activity, " + this);
            }
            callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.PAUSING, true);
            setState(com.android.server.wm.ActivityRecord.State.PAUSING, "makeActiveIfNeeded");
            this.mActivityRecordExt.makeActiveIfNeeded(getUid(), this.packageName, this.info.applicationInfo);
            this.mActivityRecordExt.hookSetBinderUxFlag(true, this);
            com.android.server.wm.EventLogTags.writeWmPauseActivity(this.mUserId, java.lang.System.identityHashCode(this), this.shortComponentName, "userLeaving=false", "make-active");
            try {
                android.os.Trace.traceBegin(32L, "cmz.mtk.makeActiveIfNeeded.activityPaused");
                this.mAtmService.mSocExt.onActivityStateChanged(this, false);
                android.os.Trace.traceEnd(32L);
                this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), android.app.servertransaction.PauseActivityItem.obtain(this.token, this.finishing, false, false, this.mAutoEnteringPip));
            } catch (java.lang.Exception e) {
                android.util.Slog.w(TAG, "Exception thrown sending pause: " + this.intent.getComponent(), e);
            }
            this.mActivityRecordExt.hookSetBinderUxFlag(false, this);
        } else if (shouldStartActivity()) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG_VISIBILITY, "Start visible activity, " + this);
            }
            com.android.server.wm.ActivityRecord.State changedState = getWrapper().getExtImpl().changeStartActiveStateIfNeed(com.android.server.wm.ActivityRecord.State.STARTED);
            callServiceTrackeronActivityStatechange(changedState, true);
            setState(changedState, "makeActiveIfNeeded");
            this.mActivityRecordExt.makeActiveIfNeeded(getUid(), this.packageName, this.info.applicationInfo);
            this.mActivityRecordSocExt.acquireActivityBoost(this.packageName, this.app, this.info, this.mAtmService, this.processName);
            try {
                this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), android.app.servertransaction.StartActivityItem.obtain(this.token, takeSceneTransitionInfo()));
            } catch (java.lang.Exception e2) {
                android.util.Slog.w(TAG, "Exception thrown sending start: " + this.intent.getComponent(), e2);
                this.mActivityRecordSocExt.releaseActivityBoost();
            }
            this.mTaskSupervisor.mStoppingActivities.remove(this);
        }
        return false;
    }

    boolean shouldPauseActivity(com.android.server.wm.ActivityRecord activeActivity) {
        return shouldMakeActive(activeActivity) && !isFocusable() && !isState(com.android.server.wm.ActivityRecord.State.PAUSING, com.android.server.wm.ActivityRecord.State.PAUSED) && this.results == null;
    }

    boolean shouldResumeActivity(com.android.server.wm.ActivityRecord activeActivity) {
        return shouldBeResumed(activeActivity) && !isState(com.android.server.wm.ActivityRecord.State.RESUMED);
    }

    private boolean shouldBeResumed(com.android.server.wm.ActivityRecord activeActivity) {
        return shouldMakeActive(activeActivity) && isFocusable() && getTaskFragment().getVisibility(activeActivity) == 0 && canResumeByCompat();
    }

    private boolean shouldStartActivity() {
        return this.mVisibleRequested && (isState(com.android.server.wm.ActivityRecord.State.STOPPED) || isState(com.android.server.wm.ActivityRecord.State.STOPPING));
    }

    boolean shouldMakeActive(com.android.server.wm.ActivityRecord activeActivity) {
        if (!isState(com.android.server.wm.ActivityRecord.State.STARTED, com.android.server.wm.ActivityRecord.State.RESUMED, com.android.server.wm.ActivityRecord.State.PAUSED, com.android.server.wm.ActivityRecord.State.STOPPED, com.android.server.wm.ActivityRecord.State.STOPPING) || this.mTransitionController.mExt.isTransientHideInRecentsFromRemote(this.task) || getRootTask().mTranslucentActivityWaiting != null || this == activeActivity || !this.mTaskSupervisor.readyToResume() || this.mLaunchTaskBehind) {
            return false;
        }
        if (this.task.hasChild(this)) {
            return !getWrapper().getExtImpl().notMakeActiveInCompactMode() && getTaskFragment().topRunningActivity() == this;
        }
        throw new java.lang.IllegalStateException("Activity not found in its task");
    }

    void handleAlreadyVisible() {
        try {
            if (this.returningOptions != null && this.returningOptions.getAnimationType() == 5 && this.returningOptions.getSceneTransitionInfo() != null) {
                this.app.getThread().scheduleOnNewSceneTransitionInfo(this.token, this.returningOptions.getSceneTransitionInfo());
            }
        } catch (android.os.RemoteException e) {
        }
    }

    static void activityResumedLocked(android.os.IBinder token, boolean handleSplashScreenExit) {
        com.android.server.wm.ActivityRecord r = forTokenLocked(token);
        if (r == null) {
            return;
        }
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, "activityResumedLocked, token=" + token + ", handleSplashScreenExit=" + handleSplashScreenExit + ", r =" + r + ", callers=" + android.os.Debug.getCallers(5));
        }
        r.setCustomizeSplashScreenExitAnimation(handleSplashScreenExit);
        r.setSavedState(null);
        r.mDisplayContent.handleActivitySizeCompatModeIfNeeded(r);
        r.mDisplayContent.mUnknownAppVisibilityController.notifyAppResumedFinished(r);
    }

    static void activityRefreshedLocked(android.os.IBinder token) {
        com.android.server.wm.ActivityRecord r = forTokenLocked(token);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(r);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -69666241054231397L, 0, null, protoLogParam0);
        }
        if (r != null && r.mDisplayContent.mActivityRefresher != null) {
            r.mDisplayContent.mActivityRefresher.onActivityRefreshed(r);
        }
    }

    static void splashScreenAttachedLocked(android.os.IBinder token) {
        com.android.server.wm.ActivityRecord r = forTokenLocked(token);
        if (r == null) {
            android.util.Slog.w(TAG, "splashScreenTransferredLocked cannot find activity");
        } else {
            r.onSplashScreenAttachComplete();
        }
    }

    void completeResumeLocked() {
        this.idle = false;
        this.results = null;
        if (this.newIntents != null && this.newIntents.size() > 0) {
            this.mLastNewIntent = this.newIntents.get(this.newIntents.size() - 1);
            this.mActivityRecordExt.setLastIntentReceived(this.mLastNewIntent);
        }
        this.newIntents = null;
        this.mTaskSupervisor.updateHomeProcessIfNeeded(this);
        if (this.nowVisible) {
            this.mTaskSupervisor.stopWaitingForActivityVisible(this);
        }
        this.mTaskSupervisor.scheduleIdleTimeout(this);
        this.mTaskSupervisor.reportResumedActivityLocked(this);
        resumeKeyDispatchingLocked();
        com.android.server.wm.Task rootTask = getRootTask();
        this.mTaskSupervisor.mNoAnimActivities.clear();
        this.returningOptions = null;
        if (canTurnScreenOn()) {
            if ("com.google.android.dialer".equals(this.packageName)) {
                this.mTaskSupervisor.wakeUp("turnScreenOnFlag:googledialer");
            } else {
                this.mTaskSupervisor.wakeUp("turnScreenOnFlag");
            }
        } else {
            rootTask.checkReadyForSleep();
        }
        getTask().getWrapper().getExtImpl().sendBroadcastResumedActivity(this.mAtmService.mH, this.mAtmService.mContext, this);
        this.mAtmService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$completeResumeLocked$14();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$completeResumeLocked$14() {
        this.mActivityRecordExt.updateAllTopApps();
    }

    void activityPaused(boolean timeout) {
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, "activityPaused, token=" + this.token + ", timeout=" + timeout + ", callers=" + android.os.Debug.getCallers(5));
        }
        com.android.server.wm.TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null) {
            removePauseTimeout();
            com.android.server.wm.ActivityRecord pausingActivity = taskFragment.getPausingActivity();
            if (pausingActivity == this) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(timeout ? "(due to timeout)" : " (pause complete)");
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 6879640870754727133L, 0, null, protoLogParam0, protoLogParam1);
                }
                this.mAtmService.deferWindowLayout();
                try {
                    taskFragment.completePause(true, null);
                    this.mAtmService.continueWindowLayout();
                    this.mDisplayContent.handleActivitySizeCompatModeIfNeeded(this);
                    return;
                } catch (java.lang.Throwable th) {
                    this.mAtmService.continueWindowLayout();
                    throw th;
                }
            }
            com.android.server.wm.EventLogTags.writeWmFailedToPause(this.mUserId, java.lang.System.identityHashCode(this), this.shortComponentName, pausingActivity != null ? pausingActivity.shortComponentName : "(none)");
            if (isState(com.android.server.wm.ActivityRecord.State.PAUSING)) {
                callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.PAUSED, true);
                setState(com.android.server.wm.ActivityRecord.State.PAUSED, "activityPausedLocked");
                if (this.finishing) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                        java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 2737811012914917932L, 0, null, protoLogParam02);
                    }
                    completeFinishing("activityPausedLocked");
                }
            }
        }
        this.mDisplayContent.handleActivitySizeCompatModeIfNeeded(this);
        this.mRootWindowContainer.ensureActivitiesVisible();
    }

    void schedulePauseTimeout() {
        this.pauseTime = android.os.SystemClock.uptimeMillis();
        this.mAtmService.mH.postDelayed(this.mPauseTimeoutRunnable, 500L);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -2566496855129705006L, 0, null, null);
        }
    }

    private void removePauseTimeout() {
        this.mAtmService.mH.removeCallbacks(this.mPauseTimeoutRunnable);
    }

    private void removeDestroyTimeout() {
        this.mAtmService.mH.removeCallbacks(this.mDestroyTimeoutRunnable);
    }

    private void removeStopTimeout() {
        this.mAtmService.mH.removeCallbacks(this.mStopTimeoutRunnable);
    }

    void removeTimeouts() {
        this.mTaskSupervisor.removeIdleTimeoutForActivity(this);
        removePauseTimeout();
        removeStopTimeout();
        removeDestroyTimeout();
        finishLaunchTickingLocked();
    }

    void stopIfPossible() {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            android.util.Slog.d(TAG_SWITCH, "Stopping: " + this);
        }
        this.mActivityRecordSocExt.setLaunching(false);
        this.mActivityRecordExt.forceHideByRemoveTask(false);
        if (this.finishing) {
            android.util.Slog.e(TAG, "Request to stop a finishing activity: " + this);
            destroyIfPossible("stopIfPossible-finishing");
            return;
        }
        if (isNoHistory()) {
            if (!this.task.shouldSleepActivities()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 7498807658620137882L, 0, null, protoLogParam0);
                }
                if (finishIfPossible("stop-no-history", false) != 0) {
                    resumeKeyDispatchingLocked();
                    return;
                }
            } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[0]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 3207149655622038378L, 0, null, protoLogParam02);
            }
        }
        if (!attachedToProcess()) {
            return;
        }
        resumeKeyDispatchingLocked();
        try {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                java.lang.String protoLogParam03 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -2530718588485487045L, 0, null, protoLogParam03);
            }
            callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.STOPPING, true);
            this.mActivityRecordExt.setSavingWindowSurface(findMainWindow(false), this.mDisplayContent);
            if (isState(com.android.server.wm.ActivityRecord.State.RESUMED, com.android.server.wm.ActivityRecord.State.PAUSED)) {
                this.mAtmService.mSocExt.onActivityStateChanged(this, false);
            }
            setState(com.android.server.wm.ActivityRecord.State.STOPPING, "stopIfPossible");
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG_VISIBILITY, "Stopping:" + this);
            }
            com.android.server.wm.EventLogTags.writeWmStopActivity(this.mUserId, java.lang.System.identityHashCode(this), this.shortComponentName);
            this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), android.app.servertransaction.StopActivityItem.obtain(this.token));
            this.mAtmService.mH.postDelayed(this.mStopTimeoutRunnable, 11000L);
        } catch (java.lang.Exception e) {
            android.util.Slog.w(TAG, "Exception thrown during pause", e);
            this.mAppStopped = true;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                java.lang.String protoLogParam04 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -8424334454318351870L, 0, null, protoLogParam04);
            }
            callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.STOPPED, true);
            setState(com.android.server.wm.ActivityRecord.State.STOPPED, "stopIfPossible");
        }
    }

    void activityStopped(android.os.Bundle newIcicle, android.os.PersistableBundle newPersistentState, java.lang.CharSequence description) {
        removeStopTimeout();
        boolean isStopping = this.mState == com.android.server.wm.ActivityRecord.State.STOPPING;
        if (!isStopping && this.mState != com.android.server.wm.ActivityRecord.State.RESTARTING_PROCESS) {
            android.util.Slog.i(TAG, "Activity reported stop, but no longer stopping: " + this + " " + this.mState);
            if (this.mState == com.android.server.wm.ActivityRecord.State.RESUMED) {
                if (this.firstWindowDrawn || this.allDrawn) {
                    if (this.mStartingData != null) {
                        removeStartingWindow();
                    }
                    this.mActivityRecordExt.hideStartingSurfaceIfNeeded(this);
                    return;
                }
                return;
            }
            return;
        }
        if (newPersistentState != null) {
            this.mPersistentState = newPersistentState;
            this.mAtmService.notifyTaskPersisterLocked(this.task, false);
        }
        if (newIcicle != null) {
            setSavedState(newIcicle);
            this.launchCount = 0;
            updateTaskDescription(description);
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mIcicle);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, -4913512058893421188L, 0, null, protoLogParam0, protoLogParam1);
        }
        if (isStopping) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 7613353074402340933L, 0, null, protoLogParam02);
            }
            setState(com.android.server.wm.ActivityRecord.State.STOPPED, "activityStopped");
        }
        this.mAppStopped = true;
        this.firstWindowDrawn = false;
        if (this.task.mLastRecentsAnimationTransaction != null) {
            this.task.clearLastRecentsAnimationTransaction(true);
        }
        this.mDisplayContent.mPinnedTaskController.onActivityHidden(this.mActivityComponent);
        if (isClientVisible()) {
            setClientVisible(false);
        }
        if (!this.mActivityRecordExt.shouldWindowSurfaceSaved(findMainWindow(false), this.mDisplayContent)) {
            destroySurfaces();
        }
        if (!this.mActivityRecordExt.skipRemoveOnActivityStopped()) {
            removeStartingWindow();
        }
        if (this.finishing) {
            abortAndClearOptionsAnimation();
        } else {
            this.mAtmService.updatePreviousProcess(this);
        }
        this.mTaskSupervisor.checkReadyForSleepLocked(true);
        this.mActivityRecordExt.onActivityStopped(this);
    }

    void addToStopping(boolean scheduleIdle, boolean idleDelayed, java.lang.String reason) {
        if (!this.mTaskSupervisor.mStoppingActivities.contains(this)) {
            com.android.server.wm.EventLogTags.writeWmAddToStopping(this.mUserId, java.lang.System.identityHashCode(this), this.shortComponentName, reason);
            this.mTaskSupervisor.mStoppingActivities.add(this);
            this.mActivityRecordExt.pauseFlexibleResumedActivityIfNeeded(scheduleIdle, idleDelayed, this);
        }
        com.android.server.wm.Task rootTask = getRootTask();
        boolean forceIdle = this.mTaskSupervisor.mStoppingActivities.size() > 3 || (isRootOfTask() && rootTask.getChildCount() <= 1);
        if (scheduleIdle || forceIdle) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                boolean protoLogParam0 = forceIdle;
                boolean protoLogParam1 = !idleDelayed;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 3981777934616509782L, 15, null, java.lang.Boolean.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1));
            }
            if (!idleDelayed) {
                this.mTaskSupervisor.scheduleIdle();
                return;
            } else {
                this.mTaskSupervisor.scheduleIdleTimeout(this);
                return;
            }
        }
        rootTask.checkReadyForSleep();
    }

    void startLaunchTickingLocked() {
        if (!android.os.Build.IS_USER && this.launchTickTime == 0) {
            this.launchTickTime = android.os.SystemClock.uptimeMillis();
            continueLaunchTicking();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean continueLaunchTicking() {
        com.android.server.wm.Task rootTask;
        if (this.launchTickTime == 0 || (rootTask = getRootTask()) == null) {
            return false;
        }
        rootTask.removeLaunchTickMessages();
        this.mAtmService.mH.postDelayed(this.mLaunchTickRunnable, 500L);
        return true;
    }

    void removeLaunchTickRunnable() {
        this.mAtmService.mH.removeCallbacks(this.mLaunchTickRunnable);
    }

    void finishLaunchTickingLocked() {
        this.launchTickTime = 0L;
        com.android.server.wm.Task rootTask = getRootTask();
        if (rootTask == null) {
            return;
        }
        rootTask.removeLaunchTickMessages();
    }

    boolean mayFreezeScreenLocked() {
        return mayFreezeScreenLocked(this.app);
    }

    private boolean mayFreezeScreenLocked(com.android.server.wm.WindowProcessController app) {
        return (!hasProcess() || app.isCrashing() || app.isNotResponding()) ? false : true;
    }

    void startFreezingScreenLocked(com.android.server.wm.WindowProcessController app, int configChanges) {
        if (mayFreezeScreenLocked(app)) {
            if (getParent() == null) {
                android.util.Slog.w("WindowManager", "Attempted to freeze screen with non-existing app token: " + this.token);
                return;
            }
            int freezableConfigChanges = (-536870913) & configChanges;
            if (freezableConfigChanges == 0 && okToDisplay()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this.token);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 1083992181663415298L, 0, null, protoLogParam0);
                    return;
                }
                return;
            }
            startFreezingScreen();
        }
    }

    void startFreezingScreen() {
        startFreezingScreen(-1);
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
    void startFreezingScreen(int overrideOriginalDisplayRotation) {
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.token);
            boolean protoLogParam1 = isVisible();
            boolean protoLogParam2 = this.mFreezingScreen;
            boolean protoLogParam3 = this.mVisibleRequested;
            java.lang.String protoLogParam4 = java.lang.String.valueOf(new java.lang.RuntimeException().fillInStackTrace());
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 3713860954819212080L, android.hardware.audio.common.V2_0.AudioChannelMask.IN_6, null, protoLogParam0, java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3), protoLogParam4);
        }
        if (!this.mVisibleRequested) {
            return;
        }
        boolean forceRotation = overrideOriginalDisplayRotation != -1;
        if (!this.mFreezingScreen) {
            this.mFreezingScreen = true;
            this.mWmService.registerAppFreezeListener(this);
            this.mWmService.mAppsFreezingScreen++;
            if (this.mWmService.mAppsFreezingScreen == 1) {
                if (forceRotation) {
                    this.mDisplayContent.getDisplayRotation().cancelSeamlessRotation();
                }
                this.mWmService.startFreezingDisplay(0, 0, this.mDisplayContent, overrideOriginalDisplayRotation);
                this.mWmService.mH.removeMessages(17);
                this.mWmService.mH.sendEmptyMessageDelayed(17, 2000L);
            }
        }
        if (forceRotation) {
            return;
        }
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            com.android.server.wm.WindowState w = (com.android.server.wm.WindowState) this.mChildren.get(i);
            w.onStartFreezingScreen();
        }
    }

    boolean isFreezingScreen() {
        return this.mFreezingScreen;
    }

    @Override // com.android.server.wm.WindowManagerService.AppFreezeListener
    public void onAppFreezeTimeout() {
        android.util.Slog.w("WindowManager", "Force clearing freeze: " + this);
        stopFreezingScreen(true, true);
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
    void stopFreezingScreen(boolean unfreezeSurfaceNow, boolean force) {
        if (!this.mFreezingScreen) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 7696002120820208745L, 12, null, protoLogParam0, java.lang.Boolean.valueOf(force));
        }
        int count = this.mChildren.size();
        boolean unfrozeWindows = false;
        for (int i = 0; i < count; i++) {
            com.android.server.wm.WindowState w = (com.android.server.wm.WindowState) this.mChildren.get(i);
            unfrozeWindows |= w.onStopFreezingScreen();
        }
        if (force || unfrozeWindows) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -8387262166329116492L, 0, null, protoLogParam02);
            }
            this.mFreezingScreen = false;
            this.mWmService.unregisterAppFreezeListener(this);
            this.mWmService.mAppsFreezingScreen--;
            this.mWmService.mLastFinishedFreezeSource = this;
        }
        if (unfreezeSurfaceNow) {
            if (unfrozeWindows) {
                this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
            }
            this.mWmService.stopFreezingDisplayLocked();
        }
    }

    void onFirstWindowDrawn(com.android.server.wm.WindowState win) {
        this.firstWindowDrawn = true;
        this.mSplashScreenStyleSolidColor = true;
        this.mAtmService.mBackNavigationController.removePredictiveSurfaceIfNeeded(this);
        this.mActivityRecordExt.notifyFirstWindowDrawn(this);
        if (this.mStartingWindow != null) {
            android.util.Slog.d(TAG, "Finish StartingWindow: " + win.mToken + "first real window is shown");
            win.cancelAnimation();
        }
        com.android.server.wm.Task associatedTask = this.task.mSharedStartingData != null ? this.task : null;
        if (associatedTask == null) {
            if (this.task != null && this.task.getWrapper().getExtImpl().getSharedStaringWindow()) {
                android.util.Slog.d(TAG, "onFirstWindowDrawn activity:" + this + " mSharedStartingWindow is true");
                this.task.getWrapper().getExtImpl().removeSharedStartingWindowIfNeeded(this.task);
            } else {
                removeStartingWindow();
            }
        } else if (associatedTask.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$onFirstWindowDrawn$15((com.android.server.wm.ActivityRecord) obj);
            }
        }) == null) {
            com.android.server.wm.ActivityRecord r = associatedTask.topActivityContainsStartingWindow();
            if (r != null) {
                r.removeStartingWindow();
            } else {
                android.util.Slog.d(TAG, "Don't removeStartingWindow due to ActivityRecord is null!");
            }
        } else {
            android.util.Slog.d(TAG, "Don't removeStartingWindow due to visible windows!");
        }
        updateReportedVisibilityLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onFirstWindowDrawn$15(com.android.server.wm.ActivityRecord r) {
        if (!r.isVisibleRequested() || this == r) {
            return false;
        }
        com.android.server.wm.WindowState mainWin = r.findMainWindow(false);
        return mainWin == null || mainWin.mWinAnimator.mDrawState < 4;
    }

    private void setTaskHasBeenVisible() {
        boolean wasTaskVisible = this.task.getHasBeenVisible();
        if (wasTaskVisible) {
            return;
        }
        if (inTransition() && this.mActivityRecordExt.shouldDeferTaskAppear(this.task)) {
            this.task.setDeferTaskAppear(true);
        }
        this.task.setHasBeenVisible(true);
    }

    void onStartingWindowDrawn() {
        if (this.task != null) {
            this.mSplashScreenStyleSolidColor = true;
            setTaskHasBeenVisible();
        }
        if (this.mStartingData == null || this.mStartingData.mIsDisplayed) {
            return;
        }
        this.mStartingData.mIsDisplayed = true;
        boolean isZoomMode = this.mActivityRecordExt.isFlexibleZoomWindow(getWindowingMode());
        android.util.Slog.d(TAG, "onStartingWindowDrawn: " + java.lang.String.format("app=%s, mStartingData=%s, finishing=%b,mLaunchedFromBubble=%b, mVisibleRequested=%b, mDisplayContent=%s, isZoomMode=%b", this.app, this.mStartingData, java.lang.Boolean.valueOf(this.finishing), java.lang.Boolean.valueOf(this.mLaunchedFromBubble), java.lang.Boolean.valueOf(this.mVisibleRequested), this.mDisplayContent, java.lang.Boolean.valueOf(isZoomMode)));
        if (this.app == null && !this.finishing && !this.mLaunchedFromBubble && this.mVisibleRequested && !this.mDisplayContent.mAppTransition.isReady() && !this.mDisplayContent.mAppTransition.isRunning() && !isZoomMode && this.mDisplayContent.isNextTransitionForward()) {
            this.mStartingData.mIsTransitionForward = true;
            if (this != this.mDisplayContent.getLastOrientationSource()) {
                this.mDisplayContent.updateOrientation();
            }
            this.mDisplayContent.executeAppTransition();
        }
        this.mActivityRecordExt.notifyStartingWindowDrawn(this);
    }

    private void onWindowsDrawn() {
        this.mActivityRecordSocExt.hookOnWindowsDrawn();
        com.android.server.wm.ActivityMetricsLogger.TransitionInfoSnapshot info = this.mTaskSupervisor.getActivityMetricsLogger().notifyWindowsDrawn(this);
        boolean validInfo = info != null;
        int windowsDrawnDelayMs = validInfo ? info.windowsDrawnDelayMs : -1;
        int launchState = validInfo ? info.getLaunchState() : 0;
        if (validInfo || (getDisplayArea() != null && this == getDisplayArea().topRunningActivity())) {
            this.mTaskSupervisor.reportActivityLaunched(false, this, windowsDrawnDelayMs, launchState);
        }
        finishLaunchTickingLocked();
        logLaunchTime();
        if (this.task != null) {
            setTaskHasBeenVisible();
        }
        this.mLaunchRootTask = null;
        if (this.task != null && this.task.getWrapper().getExtImpl().getLaunchedFromMultiSearch()) {
            this.task.dispatchTaskInfoChangedIfNeeded(false);
        }
        this.mActivityRecordExt.onWindowsDrawn(this);
    }

    void onWindowsVisible() {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v("WindowManager", "Reporting visible in " + this.token);
        }
        this.mTaskSupervisor.stopWaitingForActivityVisible(this);
        this.mActivityRecordExt.onWindowsVisible(this);
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            android.util.Log.v(TAG_SWITCH, "windowsVisibleLocked(): " + this);
        }
        if (!this.nowVisible) {
            this.nowVisible = true;
            this.mActivityRecordSocExt.setLaunching(false);
            this.lastVisibleTime = android.os.SystemClock.uptimeMillis();
            this.mAtmService.scheduleAppGcsLocked();
            this.mTaskSupervisor.scheduleProcessStoppingAndFinishingActivitiesIfNeeded();
            if (this.mImeInsetsFrozenUntilStartInput && getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda7
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return android.view.WindowManager.LayoutParams.mayUseInputMethod(((com.android.server.wm.WindowState) obj).mAttrs.flags);
                }
            }) == null) {
                this.mImeInsetsFrozenUntilStartInput = false;
            }
        }
    }

    void onWindowsGone() {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v("WindowManager", "Reporting gone in " + this.token);
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
            android.util.Log.v(TAG_SWITCH, "windowsGone(): " + this);
        }
        this.nowVisible = false;
        this.mActivityRecordSocExt.setLaunching(false);
    }

    @Override // com.android.server.wm.WindowContainer
    void checkAppWindowsReadyToShow() {
        if (this.allDrawn == this.mLastAllDrawn) {
            return;
        }
        this.mLastAllDrawn = this.allDrawn;
        if (!this.allDrawn) {
            return;
        }
        if (this.mFreezingScreen) {
            showAllWindowsLocked();
            stopFreezingScreen(false, true);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                long protoLogParam1 = this.mNumInterestingWindows;
                long protoLogParam2 = this.mNumDrawnWindows;
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 3235691043029201724L, 20, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2));
            }
            setAppLayoutChanges(4, "checkAppWindowsReadyToShow: freezingScreen");
            return;
        }
        setAppLayoutChanges(8, "checkAppWindowsReadyToShow");
        if (!getDisplayContent().mOpeningApps.contains(this) && canShowWindows()) {
            showAllWindowsLocked();
        }
    }

    void showAllWindowsLocked() {
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.ActivityRecord.lambda$showAllWindowsLocked$17((com.android.server.wm.WindowState) obj);
            }
        }, false);
        this.mActivityRecordExt.onShowAllWindowsOfActivity(getTask());
    }

    static /* synthetic */ void lambda$showAllWindowsLocked$17(com.android.server.wm.WindowState windowState) {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v(TAG, "performing show on: " + windowState);
        }
        windowState.performShowLocked();
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
    void updateReportedVisibilityLocked() {
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v(TAG, "Update reported visibility: " + this);
        }
        int count = this.mChildren.size();
        this.mReportedVisibilityResults.reset();
        for (int i = 0; i < count; i++) {
            com.android.server.wm.WindowState win = (com.android.server.wm.WindowState) this.mChildren.get(i);
            win.updateReportedVisibility(this.mReportedVisibilityResults);
        }
        int numInteresting = this.mReportedVisibilityResults.numInteresting;
        int numVisible = this.mReportedVisibilityResults.numVisible;
        int numDrawn = this.mReportedVisibilityResults.numDrawn;
        boolean nowGone = this.mReportedVisibilityResults.nowGone;
        boolean nowVisible = false;
        boolean nowDrawn = numInteresting > 0 && numDrawn >= numInteresting;
        if (numInteresting > 0 && numVisible >= numInteresting && isVisible()) {
            nowVisible = true;
        }
        if (!nowGone) {
            if (!nowDrawn) {
                nowDrawn = this.mReportedDrawn;
            }
            if (!nowVisible) {
                nowVisible = this.reportedVisible;
            }
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v(TAG, "VIS " + this + ": interesting=" + numInteresting + " visible=" + numVisible);
        }
        if (nowDrawn != this.mReportedDrawn) {
            if (nowDrawn) {
                onWindowsDrawn();
            }
            this.mReportedDrawn = nowDrawn;
        }
        if (nowVisible != this.reportedVisible) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG, "Visibility changed in " + this + ": vis=" + nowVisible);
            }
            this.reportedVisible = nowVisible;
            if (nowVisible) {
                onWindowsVisible();
            } else {
                onWindowsGone();
            }
        }
    }

    boolean isReportedDrawn() {
        return this.mReportedDrawn;
    }

    @Override // com.android.server.wm.WindowToken
    void setClientVisible(boolean clientVisible) {
        if (this.mActivityRecordExt.shouldAvoidDeferHidingClient(this) || clientVisible || !this.mDeferHidingClient) {
            super.setClientVisible(clientVisible);
        }
    }

    boolean updateDrawnWindowStates(com.android.server.wm.WindowState w) {
        w.setDrawnStateEvaluated(true);
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_STARTING_WINDOW_VERBOSE && w == this.mStartingWindow) {
            android.util.Slog.d(TAG, "updateWindows: starting " + w + " isOnScreen=" + w.isOnScreen() + " allDrawn=" + this.allDrawn + " freezingScreen=" + this.mFreezingScreen);
        }
        if (this.allDrawn && !this.mFreezingScreen) {
            return false;
        }
        if (this.mLastTransactionSequence != this.mWmService.mTransactionSequence) {
            this.mLastTransactionSequence = this.mWmService.mTransactionSequence;
            this.mNumDrawnWindows = 0;
            this.mNumInterestingWindows = findMainWindow(false) != null ? 1 : 0;
        }
        com.android.server.wm.WindowStateAnimator winAnimator = w.mWinAnimator;
        if (this.allDrawn || !w.mightAffectAllDrawn()) {
            return false;
        }
        if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY || com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
            boolean isAnimationSet = isAnimating(3, 1);
            android.util.Slog.v(TAG, "Eval win " + w + ": isDrawn=" + w.isDrawn() + ", isAnimationSet=" + isAnimationSet);
            if (!w.isDrawn()) {
                android.util.Slog.v(TAG, "Not displayed: s=" + winAnimator.mSurfaceController + " pv=" + w.isVisibleByPolicy() + " mDrawState=" + winAnimator.drawStateToString() + " ph=" + w.isParentWindowHidden() + " th=" + this.mVisibleRequested + " a=" + isAnimationSet);
            }
        }
        if (w == this.mStartingWindow || !w.isInteresting() || w.mAttrs.type == 3) {
            return false;
        }
        if (findMainWindow(false) != w) {
            this.mNumInterestingWindows++;
        }
        if (w.isDrawn()) {
            this.mNumDrawnWindows++;
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY || com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                android.util.Slog.v(TAG, "tokenMayBeDrawn: " + this + " w=" + w + " numInteresting=" + this.mNumInterestingWindows + " freezingScreen=" + this.mFreezingScreen + " mAppFreezing=" + w.mAppFreezing);
            }
            return true;
        }
        return false;
    }

    public boolean inputDispatchingTimedOut(com.android.internal.os.TimeoutRecord timeoutRecord, int windowPid) {
        com.android.server.wm.ActivityRecord anrActivity;
        com.android.server.wm.WindowProcessController anrApp;
        boolean blameActivityProcess;
        try {
            android.os.Trace.traceBegin(64L, "ActivityRecord#inputDispatchingTimedOut()");
            timeoutRecord.mLatencyTracker.waitingOnGlobalLockStarted();
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtmService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    timeoutRecord.mLatencyTracker.waitingOnGlobalLockEnded();
                    anrActivity = getWaitingHistoryRecordLocked();
                    anrApp = this.app;
                    blameActivityProcess = hasProcess() && (this.app.getPid() == windowPid || windowPid == -1);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            if (blameActivityProcess) {
                return this.mAtmService.mAmInternal.inputDispatchingTimedOut(anrApp.mOwner, anrActivity.shortComponentName, anrActivity.info.applicationInfo, this.shortComponentName, this.app, false, timeoutRecord);
            }
            long timeoutMillis = this.mAtmService.mAmInternal.inputDispatchingTimedOut(windowPid, false, timeoutRecord);
            return timeoutMillis <= 0;
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    private com.android.server.wm.ActivityRecord getWaitingHistoryRecordLocked() {
        com.android.server.wm.Task rootTask;
        if (!this.mAppStopped || (rootTask = this.mRootWindowContainer.getTopDisplayFocusedRootTask()) == null) {
            return this;
        }
        com.android.server.wm.ActivityRecord r = rootTask.getTopResumedActivity();
        if (r == null) {
            r = rootTask.getTopPausingActivity();
        }
        if (r != null) {
            return r;
        }
        return this;
    }

    boolean canBeTopRunning() {
        return !this.finishing && showToCurrentUser();
    }

    public boolean isInterestingToUserLocked() {
        return this.mVisibleRequested || this.nowVisible || this.mState == com.android.server.wm.ActivityRecord.State.PAUSING || this.mState == com.android.server.wm.ActivityRecord.State.RESUMED;
    }

    static int getTaskForActivityLocked(android.os.IBinder token, boolean onlyRoot) {
        com.android.server.wm.ActivityRecord r = forTokenLocked(token);
        if (r == null || r.getParent() == null) {
            return -1;
        }
        com.android.server.wm.Task task = r.task;
        if (onlyRoot && r.compareTo((com.android.server.wm.WindowContainer) task.getRootActivity(false, true)) > 0 && !r.getWrapper().getExtImpl().isCompactRoot(r)) {
            return -1;
        }
        return task.mTaskId;
    }

    static com.android.server.wm.ActivityRecord isInRootTaskLocked(android.os.IBinder token) {
        com.android.server.wm.ActivityRecord r = forTokenLocked(token);
        if (r != null) {
            return r.getRootTask().isInTask(r);
        }
        return null;
    }

    static com.android.server.wm.Task getRootTask(android.os.IBinder token) {
        com.android.server.wm.ActivityRecord r = isInRootTaskLocked(token);
        if (r != null) {
            return r.getRootTask();
        }
        return null;
    }

    static com.android.server.wm.ActivityRecord isInAnyTask(android.os.IBinder token) {
        com.android.server.wm.ActivityRecord r = forTokenLocked(token);
        if (r == null || !r.isAttached()) {
            return null;
        }
        return r;
    }

    int getDisplayId() {
        if (this.task == null || this.task.mDisplayContent == null) {
            return -1;
        }
        return this.task.mDisplayContent.mDisplayId;
    }

    final boolean isDestroyable() {
        return (this.finishing || !hasProcess() || isState(com.android.server.wm.ActivityRecord.State.RESUMED) || getRootTask() == null || this == getTaskFragment().getPausingActivity() || !this.mHaveState || !this.mAppStopped || this.mVisibleRequested) ? false : true;
    }

    private static java.lang.String createImageFilename(long createTime, int taskId) {
        return java.lang.String.valueOf(taskId) + ACTIVITY_ICON_SUFFIX + createTime + ".png";
    }

    void setTaskDescription(android.app.ActivityManager.TaskDescription _taskDescription) {
        android.graphics.Bitmap icon;
        if (_taskDescription.getIconFilename() == null && (icon = _taskDescription.getIcon()) != null) {
            java.lang.String iconFilename = createImageFilename(this.createTime, this.task.mTaskId);
            java.io.File iconFile = new java.io.File(com.android.server.wm.TaskPersister.getUserImagesDir(this.task.mUserId), iconFilename);
            java.lang.String iconFilePath = iconFile.getAbsolutePath();
            this.mAtmService.getRecentTasks().saveImage(icon, iconFilePath);
            _taskDescription.setIconFilename(iconFilePath);
        }
        this.taskDescription = _taskDescription;
        getTask().updateTaskDescription();
    }

    void setLocusId(android.content.LocusId locusId) {
        if (java.util.Objects.equals(locusId, this.mLocusId)) {
            return;
        }
        this.mLocusId = locusId;
        com.android.server.wm.Task task = getTask();
        if (task != null) {
            getTask().dispatchTaskInfoChangedIfNeeded(false);
        }
    }

    android.content.LocusId getLocusId() {
        return this.mLocusId;
    }

    public void reportScreenCaptured() {
        if (this.mCaptureCallbacks != null) {
            int n = this.mCaptureCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                android.app.IScreenCaptureObserver obs = this.mCaptureCallbacks.getBroadcastItem(i);
                try {
                    obs.onScreenCaptured();
                } catch (android.os.RemoteException e) {
                }
            }
            this.mCaptureCallbacks.finishBroadcast();
        }
    }

    public void registerCaptureObserver(android.app.IScreenCaptureObserver observer) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mCaptureCallbacks == null) {
                    this.mCaptureCallbacks = new android.os.RemoteCallbackList<>();
                }
                this.mCaptureCallbacks.register(observer);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void unregisterCaptureObserver(android.app.IScreenCaptureObserver observer) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mCaptureCallbacks != null) {
                    this.mCaptureCallbacks.unregister(observer);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    boolean isRegisteredForScreenCaptureCallback() {
        return this.mCaptureCallbacks != null && this.mCaptureCallbacks.getRegisteredCallbackCount() > 0;
    }

    void setVoiceSessionLocked(android.service.voice.IVoiceInteractionSession session) {
        this.voiceSession = session;
        this.pendingVoiceInteractionStart = false;
    }

    void clearVoiceSessionLocked() {
        this.voiceSession = null;
        this.pendingVoiceInteractionStart = false;
    }

    void showStartingWindow(boolean taskSwitch) {
        com.android.server.wm.ActivityRecord prev = this.task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$showStartingWindow$18((com.android.server.wm.ActivityRecord) obj);
            }
        });
        showStartingWindow(prev, false, taskSwitch, false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$showStartingWindow$18(com.android.server.wm.ActivityRecord a) {
        return (a == this || a.mStartingData == null || !a.showToCurrentUser()) ? false : true;
    }

    private com.android.server.wm.ActivityRecord searchCandidateLaunchingActivity() {
        com.android.server.wm.WindowProcessController candidateProcess;
        com.android.server.wm.ActivityRecord below = this.task.getActivityBelow(this);
        if (below == null) {
            below = this.task.getParent().getActivityBelow(this);
        }
        if (below == null || below.isActivityTypeHome()) {
            return null;
        }
        com.android.server.wm.WindowProcessController myProcess = this.app != null ? this.app : (com.android.server.wm.WindowProcessController) this.mAtmService.mProcessNames.get(this.processName, this.info.applicationInfo.uid);
        if (below.app != null) {
            candidateProcess = below.app;
        } else {
            candidateProcess = (com.android.server.wm.WindowProcessController) this.mAtmService.mProcessNames.get(below.processName, below.info.applicationInfo.uid);
        }
        if (candidateProcess != myProcess && !this.mActivityComponent.getPackageName().equals(below.mActivityComponent.getPackageName())) {
            return null;
        }
        return below;
    }

    private boolean isIconStylePreferred(int theme) {
        com.android.internal.policy.AttributeCache.Entry ent;
        return theme != 0 && (ent = com.android.internal.policy.AttributeCache.instance().get(this.packageName, theme, com.android.internal.R.styleable.Window, this.mWmService.mCurrentUserId)) != null && ent.array.hasValue(61) && ent.array.getInt(61, 0) == 1;
    }

    private boolean shouldUseSolidColorSplashScreen(com.android.server.wm.ActivityRecord sourceRecord, boolean startActivity, android.app.ActivityOptions options, int resolvedTheme) {
        boolean z;
        if (sourceRecord == null && !startActivity && this.task != null) {
            com.android.server.wm.ActivityRecord above = this.task.getActivityAbove(this);
            if (above != null) {
                return true;
            }
        }
        int optionsStyle = options != null ? options.getSplashScreenStyle() : -1;
        if (optionsStyle == 0) {
            return true;
        }
        if (optionsStyle == 1 || isIconStylePreferred(resolvedTheme)) {
            return false;
        }
        if (this.mActivityRecordExt.isInRestoring(this)) {
            android.util.Slog.d(TAG, "shouldUseSolidColorSplashScreen: use icon style for restore case");
            return false;
        }
        if (this.mLaunchSourceType == 2 || this.launchedFromUid == 2000) {
            return false;
        }
        if (this.mLaunchSourceType == 3) {
            return true;
        }
        if (sourceRecord == null) {
            sourceRecord = searchCandidateLaunchingActivity();
        }
        if (sourceRecord != null) {
            return sourceRecord.mSplashScreenStyleSolidColor;
        }
        if (this.mActivityRecordExt.isFlexibleZoomWindow(getWindowingMode()) && this.mActivityRecordExt.isZoomSplashExceptionList(this.packageName)) {
            z = true;
        } else {
            z = false;
        }
        boolean startActivity2 = startActivity | z;
        if (this.mLaunchSourceType != 1 || !startActivity2) {
            return true;
        }
        return false;
    }

    private int getSplashscreenTheme(android.app.ActivityOptions options) {
        java.lang.String splashScreenThemeResName;
        if (options == null) {
            splashScreenThemeResName = null;
        } else {
            splashScreenThemeResName = options.getSplashScreenThemeResName();
        }
        if (splashScreenThemeResName == null || splashScreenThemeResName.isEmpty()) {
            try {
                splashScreenThemeResName = this.mAtmService.getPackageManager().getSplashScreenTheme(this.packageName, this.mUserId);
            } catch (android.os.RemoteException e) {
            }
        }
        int splashScreenThemeResId = 0;
        if (splashScreenThemeResName != null && !splashScreenThemeResName.isEmpty()) {
            try {
                android.content.Context packageContext = this.mAtmService.mContext.createPackageContext(this.packageName, 0);
                splashScreenThemeResId = packageContext.getResources().getIdentifier(splashScreenThemeResName, null, null);
            } catch (android.content.pm.PackageManager.NameNotFoundException | android.content.res.Resources.NotFoundException e2) {
            }
        }
        if (splashScreenThemeResId == 0) {
            return this.mWrapper.getExtImpl().getSplashscreenTheme(options);
        }
        return splashScreenThemeResId;
    }

    void showStartingWindow(com.android.server.wm.ActivityRecord prev, boolean newTask, boolean taskSwitch, boolean startActivity, com.android.server.wm.ActivityRecord sourceRecord) {
        showStartingWindow(prev, newTask, taskSwitch, isProcessRunning(), startActivity, sourceRecord, null);
    }

    void showStartingWindow(com.android.server.wm.ActivityRecord prev, boolean newTask, boolean taskSwitch, boolean processRunning, boolean startActivity, com.android.server.wm.ActivityRecord sourceRecord, android.app.ActivityOptions candidateOptions) {
        android.app.ActivityOptions activityOptions;
        int splashscreenTheme;
        if (this.mActivityRecordExt.ignoreShowStartingWindow(prev, newTask, taskSwitch, processRunning, startActivity, sourceRecord, candidateOptions, this.mState)) {
            return;
        }
        if (this.mTaskOverlay && !this.mActivityRecordExt.taskOverlayStartingWindow(newTask, processRunning, sourceRecord, this) && !this.mActivityRecordExt.addStartingSurfaceIngoreTaskOverlay(this)) {
            android.util.Slog.d(TAG, "skip showStartingWindow due to mTaskOverlay");
            getWrapper().getExtImpl().notifyAddStartingWindowFail(1024);
            return;
        }
        if (candidateOptions == null) {
            activityOptions = this.mPendingOptions;
        } else {
            activityOptions = candidateOptions;
        }
        android.app.ActivityOptions startOptions = activityOptions;
        if (startOptions == null || startOptions.getAnimationType() != 5) {
            if (this.mActivityRecordExt.isBackgroundPuttTask(this)) {
                android.util.Slog.d(TAG, "skip showStartingWindow at One Putt scene");
                getWrapper().getExtImpl().notifyAddStartingWindowFail(1536);
                return;
            }
            this.mActivityRecordExt.reviseWindowFlagsForStarting(this, sourceRecord, newTask, taskSwitch, isProcessRunning(), false, this.mState);
            if (startActivity) {
                splashscreenTheme = getSplashscreenTheme(startOptions);
            } else {
                splashscreenTheme = 0;
            }
            int splashScreenTheme = splashscreenTheme;
            int resolvedTheme = this.mActivityRecordExt.updateOrSaveResolvedThemeIfNeeded(this.task, this, newTask, taskSwitch, sourceRecord, startOptions, processRunning, startActivity, this.theme, splashScreenTheme, evaluateStartingWindowTheme(prev, this.packageName, this.theme, splashScreenTheme));
            this.mSplashScreenStyleSolidColor = shouldUseSolidColorSplashScreen(sourceRecord, startActivity, startOptions, resolvedTheme);
            boolean activityCreated = this.mState.ordinal() >= com.android.server.wm.ActivityRecord.State.STARTED.ordinal() && this.mState.ordinal() <= com.android.server.wm.ActivityRecord.State.STOPPED.ordinal();
            boolean newSingleActivity = (newTask || activityCreated || this.task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda26
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$showStartingWindow$19((com.android.server.wm.ActivityRecord) obj);
                }
            }) != null) ? false : true;
            boolean scheduled = addStartingWindow(this.packageName, resolvedTheme, prev, newTask || newSingleActivity, taskSwitch, processRunning, allowTaskSnapshot(), activityCreated, this.mSplashScreenStyleSolidColor, this.allDrawn);
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_STARTING_WINDOW_VERBOSE && scheduled) {
                android.util.Slog.d(TAG, "Scheduled starting window for " + this);
                return;
            }
            return;
        }
        android.util.Slog.d(TAG, "skip showStartingWindow when using shared element transition");
        getWrapper().getExtImpl().notifyAddStartingWindowFail(1280);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$showStartingWindow$19(com.android.server.wm.ActivityRecord r) {
        return (r.finishing || r == this) ? false : true;
    }

    void cancelInitializing() {
        if (this.mStartingData != null) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.w(TAG_VISIBILITY, "Found orphaned starting window " + this);
            }
            removeStartingWindowAnimation(false);
        }
        if (!this.mDisplayContent.mUnknownAppVisibilityController.allResolved()) {
            this.mDisplayContent.mUnknownAppVisibilityController.appRemovedOrHidden(this);
        }
    }

    void postWindowRemoveStartingWindowCleanup(com.android.server.wm.WindowState win) {
        if (this.mStartingWindow == win) {
            this.mActivityRecordExt.postWindowRemoveStartingWindow(this.mStartingSurface, getTask(), this);
            this.mStartingWindow = null;
            this.mStartingData = null;
            this.mStartingSurface = null;
        }
        if (this.mChildren.size() == 0 && this.mVisibleSetFromTransferredStartingWindow) {
            setVisible(false);
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
    void requestUpdateWallpaperIfNeeded() {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState w = (com.android.server.wm.WindowState) this.mChildren.get(i);
            w.requestUpdateWallpaperIfNeeded();
        }
    }

    com.android.server.wm.WindowState findMainWindow() {
        return findMainWindow(true);
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
    public com.android.server.wm.WindowState findMainWindow(boolean includeStartingApp) {
        com.android.server.wm.WindowState candidate = null;
        for (int j = this.mChildren.size() - 1; j >= 0; j--) {
            com.android.server.wm.WindowState win = (com.android.server.wm.WindowState) this.mChildren.get(j);
            int type = win.mAttrs.type;
            if (type == 1 || (includeStartingApp && type == 3)) {
                if (win.mAnimatingExit) {
                    candidate = win;
                } else {
                    return win;
                }
            }
        }
        return candidate;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean needsZBoost() {
        return this.mNeedsZBoost || super.needsZBoost();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl getAnimationLeashParent() {
        if (inPinnedWindowingMode()) {
            return getRootTask().getSurfaceControl();
        }
        return super.getAnimationLeashParent();
    }

    boolean shouldAnimate() {
        return this.task == null || this.task.shouldAnimate();
    }

    public int isAppInfoGame() {
        if (this.info.applicationInfo == null) {
            return 0;
        }
        int isGame = (this.info.applicationInfo.category == 0 || (this.info.applicationInfo.flags & 33554432) == 33554432) ? 1 : 0;
        return isGame;
    }

    private android.view.SurfaceControl createAnimationBoundsLayer(android.view.SurfaceControl.Transaction t) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[2]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 5991628884266137609L, 0, null, null);
        }
        android.view.SurfaceControl.Builder builder = makeAnimationLeash().setParent(getAnimationLeashParent()).setName(getSurfaceControl() + " - animation-bounds").setCallsite("ActivityRecord.createAnimationBoundsLayer");
        if (this.mNeedsLetterboxedAnimation) {
            builder.setEffectLayer();
        }
        android.view.SurfaceControl boundsLayer = builder.build();
        t.show(boundsLayer);
        return boundsLayer;
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public boolean shouldDeferAnimationFinish(java.lang.Runnable endDeferFinishCallback) {
        return this.mAnimatingActivityRegistry != null && this.mAnimatingActivityRegistry.notifyAboutToFinish(this, endDeferFinishCallback);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isWaitingForTransitionStart() {
        com.android.server.wm.DisplayContent dc = getDisplayContent();
        return dc != null && dc.mAppTransition.isTransitionSet() && (dc.mOpeningApps.contains(this) || dc.mClosingApps.contains(this) || dc.mChangingContainers.contains(this));
    }

    boolean isTransitionForward() {
        return (this.mStartingData != null && this.mStartingData.mIsTransitionForward) || this.mDisplayContent.isNextTransitionForward();
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    void resetSurfacePositionForAnimationLeash(android.view.SurfaceControl.Transaction t) {
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public void onLeashAnimationStarting(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
        if (this.mAnimatingActivityRegistry != null) {
            this.mAnimatingActivityRegistry.notifyStarting(this);
        }
        if (this.mNeedsLetterboxedAnimation) {
            updateLetterboxSurfaceIfNeeded(findMainWindow(), t);
            this.mNeedsAnimationBoundsLayer = true;
        }
        if (this.mNeedsAnimationBoundsLayer) {
            this.mTmpRect.setEmpty();
            if (getDisplayContent().mAppTransitionController.isTransitWithinTask(getTransit(), this.task)) {
                this.task.getBounds(this.mTmpRect);
            } else {
                com.android.server.wm.Task rootTask = getRootTask();
                if (rootTask == null) {
                    return;
                } else {
                    rootTask.getBounds(this.mTmpRect);
                }
            }
            if (this.mAnimationBoundsLayer != null) {
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM) {
                    android.util.Slog.d(TAG, "onLeashAnimationStarting ==> if already has an mAnimationBoundsLayer before creating a new one, we should remove the previous one this =:" + this);
                }
                t.remove(this.mAnimationBoundsLayer);
                this.mAnimationBoundsLayer = null;
            }
            this.mAnimationBoundsLayer = createAnimationBoundsLayer(t);
            t.setLayer(this.mAnimationBoundsLayer, getLastLayer());
            if (this.mNeedsLetterboxedAnimation) {
                int cornerRadius = this.mLetterboxUiController.getRoundedCornersRadius(findMainWindow());
                android.graphics.Rect letterboxInnerBounds = new android.graphics.Rect();
                getLetterboxInnerBounds(letterboxInnerBounds);
                t.setCornerRadius(this.mAnimationBoundsLayer, cornerRadius).setCrop(this.mAnimationBoundsLayer, letterboxInnerBounds);
            }
            this.mActivityRecordExt.setAnimationLayer(getLastLayer());
            t.reparent(leash, this.mAnimationBoundsLayer);
        }
        this.mActivityRecordExt.onLeashAnimationStarting(t, leash);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean showSurfaceOnCreation() {
        return false;
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    void prepareSurfaces() {
        boolean z = true;
        boolean isDecorSurfaceBoosted = getTask() != null && getTask().isDecorSurfaceBoosted();
        boolean show = (isVisible() && !isDecorSurfaceBoosted) || isAnimating(2, 265);
        boolean inTransition = inTransition();
        if (this.mWindowContainerExt.dependShellTransition(show)) {
            if (!show && !inTransition) {
                z = false;
            }
            show = z;
        }
        if (this.mSurfaceControl != null) {
            if (show && !this.mLastSurfaceShowing) {
                this.mActivityRecordExt.printSyncState(this);
                getSyncTransaction().show(this.mSurfaceControl);
            } else if (!show && this.mLastSurfaceShowing) {
                this.mActivityRecordExt.printSyncState(this);
                getSyncTransaction().hide(this.mSurfaceControl);
            }
            this.mActivityRecordExt.hookPrepareSurfaces(show);
            if (show && this.mSyncState == 0 && !this.mActivityRecordExt.isSettingTaskFragment(getTaskFragment())) {
                this.mActivityRecordInputSink.applyChangesToSurfaceIfChanged(getPendingTransaction());
            }
        }
        if (this.mThumbnail != null) {
            this.mThumbnail.setShowing(getPendingTransaction(), show);
        }
        this.mLastSurfaceShowing = show;
        super.prepareSurfaces();
    }

    boolean isSurfaceShowing() {
        return this.mLastSurfaceShowing;
    }

    void attachThumbnailAnimation() {
        if (!isAnimating(2, 1)) {
            return;
        }
        android.hardware.HardwareBuffer thumbnailHeader = getDisplayContent().mAppTransition.getAppTransitionThumbnailHeader(this.task);
        if (thumbnailHeader == null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.task);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -1836789237982086339L, 0, null, protoLogParam0);
                return;
            }
            return;
        }
        clearThumbnail();
        android.view.SurfaceControl.Transaction transaction = getAnimatingContainer().getPendingTransaction();
        this.mThumbnail = new com.android.server.wm.WindowContainerThumbnail(transaction, getAnimatingContainer(), thumbnailHeader);
        this.mThumbnail.startAnimation(transaction, loadThumbnailAnimation(thumbnailHeader));
    }

    void attachCrossProfileAppsThumbnailAnimation() {
        android.graphics.drawable.Drawable thumbnailDrawable;
        if (!isAnimating(2, 1)) {
            return;
        }
        clearThumbnail();
        com.android.server.wm.WindowState win = findMainWindow();
        if (win == null) {
            return;
        }
        android.graphics.Rect frame = win.getRelativeFrame();
        final android.content.Context context = this.mAtmService.getUiContext();
        if (this.task.mUserId == this.mWmService.mCurrentUserId) {
            thumbnailDrawable = context.getDrawable(android.R.drawable.fastscroll_track_pressed_holo_dark);
        } else {
            android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) context.getSystemService(android.app.admin.DevicePolicyManager.class);
            thumbnailDrawable = dpm.getResources().getDrawable("WORK_PROFILE_ICON", "OUTLINE", "PROFILE_SWITCH_ANIMATION", new java.util.function.Supplier() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda14
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return context.getDrawable(android.R.drawable.ic_btn_round_more_normal);
                }
            });
        }
        android.hardware.HardwareBuffer thumbnail = getDisplayContent().mAppTransition.createCrossProfileAppsThumbnail(thumbnailDrawable, frame);
        if (thumbnail == null) {
            return;
        }
        android.view.SurfaceControl.Transaction transaction = getPendingTransaction();
        this.mThumbnail = new com.android.server.wm.WindowContainerThumbnail(transaction, getTask(), thumbnail);
        android.view.animation.Animation animation = getDisplayContent().mAppTransition.createCrossProfileAppsThumbnailAnimationLocked(frame);
        this.mThumbnail.startAnimation(transaction, animation, new android.graphics.Point(frame.left, frame.top));
    }

    private android.view.animation.Animation loadThumbnailAnimation(android.hardware.HardwareBuffer thumbnailHeader) {
        android.graphics.Rect appRect;
        android.graphics.Rect insets;
        android.view.DisplayInfo displayInfo = this.mDisplayContent.getDisplayInfo();
        com.android.server.wm.WindowState win = findMainWindow();
        if (win != null) {
            insets = win.getInsetsStateWithVisibilityOverride().calculateInsets(win.getFrame(), android.view.WindowInsets.Type.systemBars(), false).toRect();
            appRect = new android.graphics.Rect(win.getFrame());
            appRect.inset(insets);
        } else {
            appRect = new android.graphics.Rect(0, 0, displayInfo.appWidth, displayInfo.appHeight);
            insets = null;
        }
        android.content.res.Configuration displayConfig = this.mDisplayContent.getConfiguration();
        return getDisplayContent().mAppTransition.createThumbnailAspectScaleAnimationLocked(appRect, insets, thumbnailHeader, this.task, displayConfig.orientation);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public void onAnimationLeashLost(android.view.SurfaceControl.Transaction t) {
        super.onAnimationLeashLost(t);
        if (this.mAnimationBoundsLayer != null) {
            t.remove(this.mAnimationBoundsLayer);
            this.mAnimationBoundsLayer = null;
        }
        this.mNeedsAnimationBoundsLayer = false;
        if (this.mNeedsLetterboxedAnimation) {
            this.mNeedsLetterboxedAnimation = false;
            updateLetterboxSurfaceIfNeeded(findMainWindow(), t);
        }
        if (this.mAnimatingActivityRegistry != null) {
            this.mAnimatingActivityRegistry.notifyFinished(this);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    protected void onAnimationFinished(int type, com.android.server.wm.AnimationAdapter anim) {
        com.android.server.wm.WindowState transferredStarting;
        super.onAnimationFinished(type, anim);
        if (type != 128) {
            this.mActivityRecordExt.onCompactWindowAnimationFinished(this);
            android.os.Trace.traceBegin(32L, "AR#onAnimationFinished");
            this.mTransit = -1;
            this.mTransitFlags = 0;
            setAppLayoutChanges(12, "ActivityRecord");
            clearThumbnail();
            setClientVisible(isVisible() || this.mVisibleRequested);
            getDisplayContent().computeImeTargetIfNeeded(this);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                boolean protoLogParam1 = this.reportedVisible;
                boolean protoLogParam2 = okToDisplay();
                boolean protoLogParam3 = okToAnimate();
                boolean protoLogParam4 = isStartingWindowDisplayed();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -8809523216004991008L, 1020, null, protoLogParam0, java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3), java.lang.Boolean.valueOf(protoLogParam4));
            }
            if (this.mThumbnail != null) {
                this.mThumbnail.destroy();
                this.mThumbnail = null;
            }
            this.mActivityRecordExt.onAnimationFinished(this);
            java.util.ArrayList<com.android.server.wm.WindowState> children = new java.util.ArrayList<>(this.mChildren);
            children.forEach(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda19
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wm.WindowState) obj).onExitAnimationDone();
                }
            });
            if (this.task != null && this.startingMoved && (transferredStarting = this.task.getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda20
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.ActivityRecord.lambda$onAnimationFinished$21((com.android.server.wm.WindowState) obj);
                }
            })) != null && transferredStarting.mAnimatingExit && !transferredStarting.isSelfAnimating(0, 16)) {
                transferredStarting.onExitAnimationDone();
            }
            getDisplayContent().mAppTransition.notifyAppTransitionFinishedLocked(this.token);
            scheduleAnimation();
            this.mTaskSupervisor.scheduleProcessStoppingAndFinishingActivitiesIfNeeded();
            android.os.Trace.traceEnd(32L);
        }
    }

    static /* synthetic */ boolean lambda$onAnimationFinished$21(com.android.server.wm.WindowState w) {
        return w.mAttrs.type == 3;
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
    void clearAnimatingFlags() {
        boolean wallpaperMightChange = false;
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState win = (com.android.server.wm.WindowState) this.mChildren.get(i);
            wallpaperMightChange |= win.clearAnimatingFlags();
        }
        if (wallpaperMightChange) {
            requestUpdateWallpaperIfNeeded();
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void cancelAnimation() {
        super.cancelAnimation();
        clearThumbnail();
    }

    private void clearThumbnail() {
        if (this.mThumbnail == null) {
            return;
        }
        this.mThumbnail.destroy();
        this.mThumbnail = null;
    }

    public int getTransit() {
        return this.mTransit;
    }

    void registerRemoteAnimations(android.view.RemoteAnimationDefinition definition) {
        this.mRemoteAnimationDefinition = definition;
        if (definition != null) {
            definition.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda15
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.unregisterRemoteAnimations();
                }
            });
        }
    }

    void unregisterRemoteAnimations() {
        this.mRemoteAnimationDefinition = null;
    }

    @Override // com.android.server.wm.WindowContainer
    android.view.RemoteAnimationDefinition getRemoteAnimationDefinition() {
        return this.mActivityRecordExt.getRemoteAnimationDefinition(this.mRemoteAnimationDefinition);
    }

    @Override // com.android.server.wm.WindowToken
    void applyFixedRotationTransform(android.view.DisplayInfo info, com.android.server.wm.DisplayFrames displayFrames, android.content.res.Configuration config) {
        super.applyFixedRotationTransform(info, displayFrames, config);
        ensureActivityConfiguration();
    }

    @Override // com.android.server.wm.WindowContainer
    int getRequestedConfigurationOrientation(boolean forDisplay) {
        return getRequestedConfigurationOrientation(forDisplay, getOverrideOrientation());
    }

    @Override // com.android.server.wm.WindowContainer
    int getRequestedConfigurationOrientation(boolean forDisplay, int requestedOrientation) {
        com.android.server.wm.ActivityRecord belowCandidate;
        if (this.mTransparentPolicy.hasInheritedOrientation()) {
            com.android.server.wm.RootDisplayArea root = getRootDisplayArea();
            if (forDisplay && root != null && root.isOrientationDifferentFromDisplay()) {
                return reverseConfigurationOrientation(this.mTransparentPolicy.getInheritedOrientation());
            }
            return this.mTransparentPolicy.getInheritedOrientation();
        }
        if (this.task != null && requestedOrientation == 3 && (belowCandidate = this.task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda36
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.ActivityRecord) obj).canDefineOrientationForActivitiesAbove();
            }
        }, this, false, true)) != null) {
            return belowCandidate.getRequestedConfigurationOrientation(forDisplay);
        }
        return super.getRequestedConfigurationOrientation(forDisplay, requestedOrientation);
    }

    public static int reverseConfigurationOrientation(int orientation) {
        switch (orientation) {
            case 1:
                return 2;
            case 2:
                return 1;
            default:
                return orientation;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canDefineOrientationForActivitiesAbove() {
        int overrideOrientation;
        return (this.finishing || (overrideOrientation = getOverrideOrientation()) == -2 || overrideOrientation == 3) ? false : true;
    }

    @Override // com.android.server.wm.WindowToken
    void onCancelFixedRotationTransform(int originalDisplayRotation) {
        if (this != this.mDisplayContent.getLastOrientationSource()) {
            return;
        }
        int requestedOrientation = getRequestedConfigurationOrientation();
        if (requestedOrientation != 0 && requestedOrientation != this.mDisplayContent.getConfiguration().orientation) {
            return;
        }
        this.mDisplayContent.mPinnedTaskController.onCancelFixedRotationTransform();
        if (!this.mActivityRecordExt.shouldExitFixedRotation(this.mDisplayContent, this)) {
            startFreezingScreen(originalDisplayRotation);
        }
        ensureActivityConfiguration();
        if (this.mTransitionController.isCollecting(this)) {
            this.task.resetSurfaceControlTransforms();
        }
    }

    void setRequestedOrientation(int requestedOrientation) {
        if (this.mActivityRecordExt.blockActivityRecordRequestOrientation(this, requestedOrientation)) {
            return;
        }
        this.mActivityRecordExt.setOrientation(this, requestedOrientation, getOverrideOrientation());
        if (this.mLetterboxUiController.shouldIgnoreRequestedOrientation(requestedOrientation)) {
            return;
        }
        int originalRelaunchingCount = this.mPendingRelaunchCount;
        if (getRequestedConfigurationOrientation(false, requestedOrientation) != getRequestedConfigurationOrientation(false)) {
            clearSizeCompatModeAttributes();
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(android.content.pm.ActivityInfo.screenOrientationToString(requestedOrientation));
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -9178011226407552682L, 0, null, protoLogParam0, protoLogParam1);
        }
        setOrientation(requestedOrientation, this);
        if (!getMergedOverrideConfiguration().equals(this.mLastReportedConfiguration.getMergedConfiguration())) {
            ensureActivityConfiguration(false);
            if (this.mPendingRelaunchCount > originalRelaunchingCount) {
                this.mLetterboxUiController.setRelaunchingAfterRequestedOrientationChanged(true);
            }
            if (this.mTransitionController.inPlayingTransition(this)) {
                this.mTransitionController.mValidateActivityCompat.add(this);
            }
        }
        this.mAtmService.getTaskChangeNotificationController().notifyActivityRequestedOrientationChanged(this.task.mTaskId, requestedOrientation);
        this.mDisplayContent.getDisplayRotation().onSetRequestedOrientation();
    }

    void reportDescendantOrientationChangeIfNeeded() {
        if (onDescendantOrientationChanged(this)) {
            this.task.dispatchTaskInfoChangedIfNeeded(true);
        }
    }

    boolean shouldIgnoreOrientationRequests() {
        return this.mAppActivityEmbeddingSplitsEnabled && android.content.pm.ActivityInfo.isFixedOrientationPortrait(getOverrideOrientation()) && !this.task.inMultiWindowMode() && getTask().getConfiguration().smallestScreenWidthDp >= 600;
    }

    @Override // com.android.server.wm.WindowContainer
    int getOrientation(int candidate) {
        if (getWrapper().getExtImpl().shouldIgnoreOrientationRequests(this) || this.finishing || shouldIgnoreOrientationRequests()) {
            return -2;
        }
        int fixedRotation = this.mActivityRecordExt.getFixedRotationForSplashScreen(this);
        if (fixedRotation > -1) {
            return fixedRotation;
        }
        if (candidate == 3) {
            return getOverrideOrientation();
        }
        if (!isVisibleRequested() || getWrapper().getExtImpl().shouldInterceptReturnOrientation(candidate)) {
            return -2;
        }
        return getOverrideOrientation();
    }

    @Override // com.android.server.wm.WindowContainer
    protected int getOverrideOrientation() {
        return this.mLetterboxUiController.overrideOrientationIfNeeded(super.getOverrideOrientation());
    }

    int getRequestedOrientation() {
        return super.getOverrideOrientation();
    }

    void setLastReportedGlobalConfiguration(android.content.res.Configuration config) {
        this.mActivityRecordExt.forceRelaunchWhenActivityIdle(config);
        this.mLastReportedConfiguration.setGlobalConfiguration(config);
    }

    void setLastReportedConfiguration(android.content.res.Configuration global, android.content.res.Configuration override) {
        this.mLastReportedConfiguration.setConfiguration(global, override);
    }

    void setLastReportedActivityWindowInfo(android.window.ActivityWindowInfo activityWindowInfo) {
        if (com.android.window.flags.Flags.activityWindowInfoFlag()) {
            this.mLastReportedActivityWindowInfo.set(activityWindowInfo);
        }
    }

    com.android.server.wm.ActivityRecord.CompatDisplayInsets getCompatDisplayInsets() {
        if (this.mTransparentPolicy.isRunning()) {
            return this.mTransparentPolicy.getInheritedCompatDisplayInsets();
        }
        return this.mCompatDisplayInsets;
    }

    boolean hasCompatDisplayInsetsWithoutInheritance() {
        return this.mCompatDisplayInsets != null;
    }

    boolean inSizeCompatMode() {
        com.android.server.wm.WindowContainer parent;
        if (this.mInSizeCompatModeForBounds) {
            return true;
        }
        if (getCompatDisplayInsets() == null || !shouldCreateCompatDisplayInsets() || isFixedRotationTransforming()) {
            return false;
        }
        android.graphics.Rect appBounds = getConfiguration().windowConfiguration.getAppBounds();
        if (appBounds == null || (parent = getParent()) == null) {
            return false;
        }
        if (this.mSizeCompatBounds != null && this.mActivityRecordExt.isCompactWindowingMode(getWindowingMode())) {
            return false;
        }
        android.content.res.Configuration parentConfig = parent.getConfiguration();
        return parentConfig.densityDpi != getConfiguration().densityDpi;
    }

    boolean shouldCreateCompatDisplayInsets() {
        if (this.mLetterboxUiController.hasFullscreenOverride()) {
            return false;
        }
        switch (supportsSizeChanges()) {
            case 1:
                break;
            case 2:
            case 3:
                break;
            default:
                if (getRootTask() == null || !getRootTask().getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
                    com.android.server.wm.TaskDisplayArea tda = getTaskDisplayArea();
                    if (inMultiWindowMode() || (tda != null && tda.inFreeformWindowingMode())) {
                        com.android.server.wm.ActivityRecord root = this.task != null ? this.task.getRootActivity() : null;
                        if (root != null && root != this && !root.shouldCreateCompatDisplayInsets()) {
                        }
                    }
                    if (!isResizeable()) {
                        if (this.info.isFixedOrientation() || hasFixedAspectRatio()) {
                            if ((!this.mActivityRecordExt.isZoomMode(getWindowingMode()) || this.mActivityRecordExt.getMaxBoundsForZoomWindow()) && !this.mActivityRecordExt.isCompactWindowingMode(getWindowingMode()) && !this.mActivityRecordExt.inOplusCompatMode() && this.mActivityRecordExt.shouldCreateCompatDisplayInsetsForSquare(this) && !this.mActivityRecordExt.shouldCreateCompatDisplayInsetsForMirageWindow(getDisplayId()) && isActivityTypeStandardOrUndefined() && !this.mActivityRecordExt.isSupprotBracketMode(this) && !this.mActivityRecordExt.isActivityPreloadDisplay(getDisplayId(), this.mActivityRecordExt.getLastReportedDisplay())) {
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }

    private int supportsSizeChanges() {
        if (this.mLetterboxUiController.shouldOverrideForceNonResizeApp()) {
            return 1;
        }
        if (this.info.supportsSizeChanges) {
            return 2;
        }
        if (this.mLetterboxUiController.shouldOverrideForceResizeApp()) {
            return 3;
        }
        return 0;
    }

    @Override // com.android.server.wm.WindowToken
    boolean hasSizeCompatBounds() {
        return this.mSizeCompatBounds != null;
    }

    private void updateCompatDisplayInsets() {
        android.graphics.Rect letterboxedContainerBounds;
        if (getCompatDisplayInsets() != null || !shouldCreateCompatDisplayInsets()) {
            return;
        }
        android.content.res.Configuration overrideConfig = getRequestedOverrideConfiguration();
        android.content.res.Configuration fullConfig = getConfiguration();
        overrideConfig.colorMode = fullConfig.colorMode;
        overrideConfig.densityDpi = fullConfig.densityDpi;
        overrideConfig.smallestScreenWidthDp = fullConfig.smallestScreenWidthDp;
        if (android.content.pm.ActivityInfo.isFixedOrientation(getOverrideOrientation())) {
            overrideConfig.windowConfiguration.setRotation(fullConfig.windowConfiguration.getRotation());
        }
        if (this.mLetterboxBoundsForFixedOrientationAndAspectRatio != null) {
            letterboxedContainerBounds = this.mLetterboxBoundsForFixedOrientationAndAspectRatio;
        } else {
            letterboxedContainerBounds = this.mLetterboxBoundsForAspectRatio;
        }
        this.mCompatDisplayInsets = new com.android.server.wm.ActivityRecord.CompatDisplayInsets(this.mDisplayContent, this, letterboxedContainerBounds, this.mResolveConfigHint.mUseOverrideInsetsForConfig);
    }

    private void clearSizeCompatModeAttributes() {
        this.mInSizeCompatModeForBounds = false;
        float lastSizeCompatScale = this.mSizeCompatScale;
        this.mSizeCompatScale = 1.0f;
        if (this.mSizeCompatScale != lastSizeCompatScale) {
            forAllWindows((java.util.function.Consumer<com.android.server.wm.WindowState>) new com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda8(), false);
        }
        this.mSizeCompatBounds = null;
        this.mCompatDisplayInsets = null;
        this.mTransparentPolicy.clearInheritedCompatDisplayInsets();
    }

    void clearSizeCompatMode() {
        clearSizeCompatModeAttributes();
        int activityType = getActivityType();
        android.content.res.Configuration overrideConfig = getRequestedOverrideConfiguration();
        overrideConfig.unset();
        overrideConfig.windowConfiguration.setActivityType(activityType);
        onRequestedOverrideConfigurationChanged(overrideConfig);
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean matchParentBounds() {
        com.android.server.wm.WindowContainer parent;
        android.graphics.Rect overrideBounds = getResolvedOverrideBounds();
        return overrideBounds.isEmpty() || (parent = getParent()) == null || parent.getBounds().equals(overrideBounds);
    }

    @Override // com.android.server.wm.WindowToken
    float getCompatScale() {
        if (this.mActivityRecordExt.hasSizeCompatBoundsInOplusCompatMode()) {
            return 1.0f;
        }
        return hasSizeCompatBounds() ? this.mSizeCompatScale : super.getCompatScale();
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.ConfigurationContainer
    void resolveOverrideConfiguration(android.content.res.Configuration newParentConfiguration) {
        android.content.res.Configuration newParentConfiguration2 = newParentConfiguration;
        android.content.res.Configuration requestedOverrideConfig = getRequestedOverrideConfiguration();
        if (requestedOverrideConfig.assetsSeq != 0 && newParentConfiguration2.assetsSeq > requestedOverrideConfig.assetsSeq) {
            requestedOverrideConfig.assetsSeq = 0;
        }
        super.resolveOverrideConfiguration(newParentConfiguration);
        android.content.res.Configuration resolvedConfig = getResolvedOverrideConfiguration();
        applyLocaleOverrideIfNeeded(resolvedConfig);
        if (isFixedRotationTransforming()) {
            this.mTmpConfig.setTo(newParentConfiguration2);
            this.mTmpConfig.updateFrom(resolvedConfig);
            newParentConfiguration2 = this.mTmpConfig;
        }
        this.mIsAspectRatioApplied = false;
        this.mIsEligibleForFixedOrientationLetterbox = false;
        this.mLetterboxBoundsForFixedOrientationAndAspectRatio = null;
        this.mLetterboxBoundsForAspectRatio = null;
        this.mResolveConfigHint.resolveTmpOverrides(this.mDisplayContent, newParentConfiguration2, isFixedRotationTransforming(), this);
        int parentWindowingMode = newParentConfiguration2.windowConfiguration.getWindowingMode();
        boolean isInCameraCompatFreeform = parentWindowingMode == 5 && this.mLetterboxUiController.getFreeformCameraCompatMode() != 0;
        boolean isFixedOrientationLetterboxAllowed = !getLaunchedFromBubble() && (parentWindowingMode == 6 || parentWindowingMode == 1 || isInCameraCompatFreeform || (!this.mWaitForEnteringPinnedMode && parentWindowingMode == 2 && resolvedConfig.windowConfiguration.getWindowingMode() == 1));
        if (isFixedOrientationLetterboxAllowed && !((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageCarMode(getDisplayId()) && !this.mActivityRecordExt.inOplusCompatMode()) {
            resolveFixedOrientationConfiguration(newParentConfiguration2);
        }
        if (this.mCompatDisplayInsets != null && this.mActivityRecordExt.shouldClearSizeCompatMode(newParentConfiguration2)) {
            clearSizeCompatModeIfNeeded();
        }
        if (com.android.window.flags.Flags.immersiveAppRepositioning() && !isLetterboxedForFixedOrientationAndAspectRatio() && ((!((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageCarMode(getDisplayId()) || this.mActivityRecordExt.inOplusCompatMode()) && !this.mLetterboxUiController.hasFullscreenOverride())) {
            resolveAspectRatioRestriction(newParentConfiguration2);
        }
        com.android.server.wm.ActivityRecord.CompatDisplayInsets compatDisplayInsets = getCompatDisplayInsets();
        if (compatDisplayInsets != null) {
            resolveSizeCompatModeConfiguration(newParentConfiguration2, compatDisplayInsets);
        } else if (inMultiWindowMode() && !isFixedOrientationLetterboxAllowed) {
            resolvedConfig.orientation = 0;
            if (!matchParentBounds()) {
                computeConfigByResolveHint(resolvedConfig, newParentConfiguration2);
            }
        }
        if (!com.android.window.flags.Flags.immersiveAppRepositioning() && !isLetterboxedForFixedOrientationAndAspectRatio() && ((!((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageCarMode(getDisplayId()) || this.mActivityRecordExt.inOplusCompatMode()) && !this.mInSizeCompatModeForBounds && !this.mLetterboxUiController.hasFullscreenOverride())) {
            resolveAspectRatioRestriction(newParentConfiguration2);
        }
        this.mActivityRecordExt.applyOplusCompatAspectRatioIfNeed(resolvedConfig, newParentConfiguration2);
        if (isFixedOrientationLetterboxAllowed || compatDisplayInsets != null || (!inMultiWindowMode() && !this.mActivityRecordExt.isCompactWindowingMode(getWindowingMode()))) {
            updateResolvedBoundsPosition(newParentConfiguration2);
        }
        boolean isIgnoreOrientationRequest = this.mDisplayContent != null && this.mDisplayContent.getIgnoreOrientationRequest();
        if (compatDisplayInsets == null && (this.mLetterboxBoundsForFixedOrientationAndAspectRatio != null || (isIgnoreOrientationRequest && this.mIsAspectRatioApplied))) {
            resolvedConfig.smallestScreenWidthDp = java.lang.Math.min(resolvedConfig.screenWidthDp, resolvedConfig.screenHeightDp);
        }
        int i = this.mConfigurationSeq + 1;
        this.mConfigurationSeq = i;
        this.mConfigurationSeq = java.lang.Math.max(i, 1);
        getResolvedOverrideConfiguration().seq = this.mConfigurationSeq;
        this.mActivityRecordExt.setMaxBoundsForZoomWindow(true);
        if (providesMaxBounds()) {
            this.mActivityRecordExt.setMaxBoundsForZoomWindow(false);
            this.mTmpBounds.set(resolvedConfig.windowConfiguration.getBounds());
            if (this.mTmpBounds.isEmpty()) {
                this.mTmpBounds.set(newParentConfiguration2.windowConfiguration.getBounds());
            }
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(getUid());
                java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mTmpBounds);
                java.lang.String protoLogParam2 = java.lang.String.valueOf(this.info.neverSandboxDisplayApis(sConstrainDisplayApisConfig));
                java.lang.String protoLogParam3 = java.lang.String.valueOf(this.info.alwaysSandboxDisplayApis(sConstrainDisplayApisConfig));
                java.lang.String protoLogParam4 = java.lang.String.valueOf(!matchParentBounds());
                java.lang.String protoLogParam5 = java.lang.String.valueOf(compatDisplayInsets != null);
                java.lang.String protoLogParam6 = java.lang.String.valueOf(shouldCreateCompatDisplayInsets());
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -1963190756391505590L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2, protoLogParam3, protoLogParam4, protoLogParam5, protoLogParam6);
            }
            resolvedConfig.windowConfiguration.setMaxBounds(this.mTmpBounds);
        } else {
            this.mActivityRecordExt.setMaxBoundsForZoomWindow(false);
        }
        applySizeOverrideIfNeeded(this.mDisplayContent, this.info.applicationInfo, newParentConfiguration2, resolvedConfig, this.mOptOutEdgeToEdge, hasFixedRotationTransform(), getCompatDisplayInsets() != null, this.mActivityRecordExt);
        com.android.server.wm.TaskFragment parentTf = getTaskFragment();
        if (parentTf != null && parentTf.mTaskFragmentExt.shouldUseParentScreenWidthDp(parentTf, this)) {
            resolvedConfig.screenWidthDp = newParentConfiguration2.screenWidthDp;
        }
        this.mResolveConfigHint.resetTmpOverrides();
        logAppCompatState();
        this.mActivityRecordExt.adjustAppCutoutInCompactWindow(this, newParentConfiguration2.windowConfiguration.getAppBounds(), resolvedConfig);
        this.mActivityRecordExt.resolveAppOrientationIfNeed(this, resolvedConfig, getOverrideOrientation(), newParentConfiguration2);
        this.mActivityRecordExt.addDeviceFoldingFlagIfNeed(this, resolvedConfig);
    }

    android.graphics.Rect getParentAppBoundsOverride() {
        return android.graphics.Rect.copyOrNull(this.mResolveConfigHint.mParentAppBoundsOverride);
    }

    private void computeConfigByResolveHint(android.content.res.Configuration resolvedConfig, android.content.res.Configuration parentConfig) {
        this.task.computeConfigResourceOverrides(resolvedConfig, parentConfig, this.mResolveConfigHint, this);
        this.mResolveConfigHint.mTmpCompatInsets = null;
        this.mResolveConfigHint.mTmpOverrideDisplayInfo = null;
    }

    boolean areBoundsLetterboxed() {
        return getAppCompatState(true) != 2;
    }

    private void logAppCompatState() {
        this.mTaskSupervisor.getActivityMetricsLogger().logAppCompatState(this);
    }

    int getAppCompatState() {
        return getAppCompatState(false);
    }

    private int getAppCompatState(boolean ignoreVisibility) {
        if (!ignoreVisibility && !this.mVisibleRequested) {
            return 1;
        }
        if (this.mTransparentPolicy.isRunning()) {
            return this.mTransparentPolicy.getInheritedAppCompatState();
        }
        if (this.mInSizeCompatModeForBounds) {
            return 3;
        }
        if (isLetterboxedForFixedOrientationAndAspectRatio()) {
            return 4;
        }
        if (isLetterboxedForAspectRatioOnly()) {
            return 5;
        }
        return 2;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0118  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0163  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0190  */
    /* JADX WARN: Removed duplicated region for block: B:56:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void updateResolvedBoundsPosition(android.content.res.Configuration r29) {
        /*
            Method dump skipped, instruction units count: 427
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityRecord.updateResolvedBoundsPosition(android.content.res.Configuration):void");
    }

    boolean isImmersiveMode(android.graphics.Rect parentBounds) {
        if (!com.android.window.flags.Flags.immersiveAppRepositioning()) {
            return false;
        }
        if (!this.mResolveConfigHint.mUseOverrideInsetsForConfig && this.mWmService.mFlags.mInsetsDecoupledConfiguration) {
            return false;
        }
        android.graphics.Insets navBarInsets = this.mDisplayContent.getInsetsStateController().getRawInsetsState().calculateInsets(parentBounds, android.view.WindowInsets.Type.navigationBars(), false);
        return android.graphics.Insets.NONE.equals(navBarInsets);
    }

    android.graphics.Rect getScreenResolvedBounds() {
        android.content.res.Configuration resolvedConfig = getResolvedOverrideConfiguration();
        android.graphics.Rect resolvedBounds = resolvedConfig.windowConfiguration.getBounds();
        return this.mSizeCompatBounds != null ? this.mSizeCompatBounds : resolvedBounds;
    }

    void recomputeConfiguration() {
        if (!this.mTransparentPolicy.applyOnOpaqueActivityBelow(new java.util.function.Consumer() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.ActivityRecord) obj).recomputeConfiguration();
            }
        })) {
            onRequestedOverrideConfigurationChanged(getRequestedOverrideConfiguration());
        }
    }

    boolean isInTransition() {
        return inTransitionSelfOrParent();
    }

    boolean isDisplaySleepingAndSwapping() {
        for (int i = this.mDisplayContent.mAllSleepTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.RootWindowContainer.SleepToken sleepToken = this.mDisplayContent.mAllSleepTokens.get(i);
            if (sleepToken.isDisplaySwapping()) {
                return true;
            }
        }
        return false;
    }

    boolean isLetterboxedForFixedOrientationAndAspectRatio() {
        return this.mLetterboxBoundsForFixedOrientationAndAspectRatio != null;
    }

    boolean isLetterboxedForAspectRatioOnly() {
        return this.mLetterboxBoundsForAspectRatio != null;
    }

    boolean isAspectRatioApplied() {
        return this.mIsAspectRatioApplied;
    }

    boolean isEligibleForLetterboxEducation() {
        return this.mWmService.mLetterboxConfiguration.getIsEducationEnabled() && this.mIsEligibleForFixedOrientationLetterbox && getWindowingMode() == 1 && getRequestedConfigurationOrientation() == 1 && this.mStartingWindow == null;
    }

    private boolean orientationRespectedWithInsets(android.graphics.Rect parentBounds, android.graphics.Rect outStableBounds, android.graphics.Rect outNonDecorBounds) {
        int requestedOrientation;
        android.view.DisplayInfo di;
        outStableBounds.setEmpty();
        if (this.mDisplayContent == null || !this.mResolveConfigHint.mUseOverrideInsetsForConfig || (requestedOrientation = getRequestedConfigurationOrientation()) == 0) {
            return true;
        }
        int orientation = parentBounds.height() >= parentBounds.width() ? 1 : 2;
        if (isFixedRotationTransforming()) {
            di = getFixedRotationTransformDisplayInfo();
        } else {
            di = this.mDisplayContent.getDisplayInfo();
        }
        com.android.server.wm.Task task = getTask();
        task.calculateInsetFrames(outNonDecorBounds, outStableBounds, parentBounds, di, this.mResolveConfigHint.mUseOverrideInsetsForConfig);
        int orientationWithInsets = outStableBounds.height() >= outStableBounds.width() ? 1 : 2;
        return orientation == orientationWithInsets || orientationWithInsets == requestedOrientation;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean handlesOrientationChangeFromDescendant(int orientation) {
        if (shouldIgnoreOrientationRequests()) {
            return false;
        }
        return super.handlesOrientationChangeFromDescendant(orientation);
    }

    private void resolveFixedOrientationConfiguration(android.content.res.Configuration newParentConfig) {
        boolean isResizeable;
        android.graphics.Rect containingBounds;
        android.graphics.Rect containingBoundsWithInsets;
        float desiredAspectRatio;
        android.graphics.Rect parentBounds = newParentConfig.windowConfiguration.getBounds();
        android.graphics.Rect stableBounds = new android.graphics.Rect();
        android.graphics.Rect outNonDecorBounds = this.mTmpBounds;
        boolean orientationRespectedWithInsets = orientationRespectedWithInsets(parentBounds, stableBounds, outNonDecorBounds);
        if (handlesOrientationChangeFromDescendant(getOverrideOrientation()) && this.mActivityRecordExt.ignoreOrientationRespectedWithInsets(this, orientationRespectedWithInsets)) {
            return;
        }
        com.android.server.wm.TaskFragment organizedTf = getOrganizedTaskFragment();
        if (organizedTf != null && !organizedTf.fillsParent()) {
            return;
        }
        boolean z = false;
        if (this.task != null) {
            isResizeable = this.task.isResizeable() || isResizeable();
        } else {
            isResizeable = isResizeable();
        }
        if ((android.app.WindowConfiguration.inMultiWindowMode(newParentConfig.windowConfiguration.getWindowingMode()) && isResizeable) || this.mActivityRecordExt.isFlexibleWindowTask()) {
            return;
        }
        android.graphics.Rect resolvedBounds = getResolvedOverrideConfiguration().windowConfiguration.getBounds();
        int stableBoundsOrientation = stableBounds.width() > stableBounds.height() ? 2 : 1;
        int parentOrientation = this.mResolveConfigHint.mUseOverrideInsetsForConfig ? stableBoundsOrientation : newParentConfig.orientation;
        int forcedOrientation = getRequestedConfigurationOrientation();
        if (forcedOrientation != 0 && forcedOrientation != parentOrientation) {
            z = true;
        }
        this.mIsEligibleForFixedOrientationLetterbox = z;
        if (!this.mIsEligibleForFixedOrientationLetterbox && (forcedOrientation == 0 || orientationRespectedWithInsets)) {
            return;
        }
        com.android.server.wm.ActivityRecord.CompatDisplayInsets compatDisplayInsets = getCompatDisplayInsets();
        if (compatDisplayInsets != null && !compatDisplayInsets.mIsInFixedOrientationOrAspectRatioLetterbox) {
            return;
        }
        android.graphics.Rect parentAppBounds = this.mResolveConfigHint.mUseOverrideInsetsForConfig ? outNonDecorBounds : newParentConfig.windowConfiguration.getAppBounds();
        android.graphics.Rect parentBoundsWithInsets = orientationRespectedWithInsets ? parentAppBounds : stableBounds;
        android.graphics.Rect containingBounds2 = new android.graphics.Rect();
        android.graphics.Rect containingBoundsWithInsets2 = new android.graphics.Rect();
        if (forcedOrientation == 2) {
            int bottom = java.lang.Math.min((parentBoundsWithInsets.top + parentBoundsWithInsets.width()) - 1, parentBoundsWithInsets.bottom);
            containingBounds = containingBounds2;
            containingBounds.set(parentBounds.left, parentBoundsWithInsets.top, parentBounds.right, bottom);
            containingBoundsWithInsets = containingBoundsWithInsets2;
            containingBoundsWithInsets.set(parentBoundsWithInsets.left, parentBoundsWithInsets.top, parentBoundsWithInsets.right, bottom);
        } else {
            containingBounds = containingBounds2;
            containingBoundsWithInsets = containingBoundsWithInsets2;
            int right = java.lang.Math.min(parentBoundsWithInsets.left + parentBoundsWithInsets.height(), parentBoundsWithInsets.right);
            containingBounds.set(parentBoundsWithInsets.left, parentBounds.top, right, parentBounds.bottom);
            containingBoundsWithInsets.set(parentBoundsWithInsets.left, parentBoundsWithInsets.top, right, parentBoundsWithInsets.bottom);
        }
        android.graphics.Rect prevResolvedBounds = new android.graphics.Rect(resolvedBounds);
        resolvedBounds.set(containingBounds);
        float letterboxAspectRatioOverride = this.mLetterboxUiController.getFixedOrientationLetterboxAspectRatio(newParentConfig);
        if (isDefaultMultiWindowLetterboxAspectRatioDesired(newParentConfig)) {
            desiredAspectRatio = 1.01f;
        } else if (letterboxAspectRatioOverride > 1.0f) {
            desiredAspectRatio = letterboxAspectRatioOverride;
        } else {
            desiredAspectRatio = computeAspectRatio(parentBounds);
        }
        this.mIsAspectRatioApplied = applyAspectRatio(resolvedBounds, containingBoundsWithInsets, containingBounds, desiredAspectRatio);
        if (compatDisplayInsets != null) {
            compatDisplayInsets.getBoundsByRotation(this.mTmpBounds, newParentConfig.windowConfiguration.getRotation());
            if (resolvedBounds.width() != this.mTmpBounds.width() || resolvedBounds.height() != this.mTmpBounds.height()) {
                resolvedBounds.set(prevResolvedBounds);
                return;
            }
        }
        if (resolvedBounds.equals(parentBounds)) {
            resolvedBounds.set(prevResolvedBounds);
            return;
        }
        this.mResolveConfigHint.mTmpCompatInsets = compatDisplayInsets;
        computeConfigByResolveHint(getResolvedOverrideConfiguration(), newParentConfig);
        this.mLetterboxBoundsForFixedOrientationAndAspectRatio = new android.graphics.Rect(resolvedBounds);
    }

    private boolean isDefaultMultiWindowLetterboxAspectRatioDesired(android.content.res.Configuration parentConfig) {
        if (this.mDisplayContent == null) {
            return false;
        }
        int windowingMode = parentConfig.windowConfiguration.getWindowingMode();
        return android.app.WindowConfiguration.inMultiWindowMode(windowingMode) && !this.mDisplayContent.getIgnoreOrientationRequest();
    }

    private void resolveAspectRatioRestriction(android.content.res.Configuration newParentConfiguration) {
        android.content.res.Configuration resolvedConfig = getResolvedOverrideConfiguration();
        android.graphics.Rect parentAppBounds = this.mResolveConfigHint.mParentAppBoundsOverride;
        android.graphics.Rect parentBounds = newParentConfiguration.windowConfiguration.getBounds();
        android.graphics.Rect resolvedBounds = resolvedConfig.windowConfiguration.getBounds();
        this.mTmpBounds.setEmpty();
        this.mIsAspectRatioApplied = applyAspectRatio(this.mTmpBounds, parentAppBounds, parentBounds);
        this.mActivityRecordExt.adjustBracketMode(newParentConfiguration, parentAppBounds, parentBounds, this.mTmpBounds, this);
        this.mActivityRecordExt.resolveFlexibleActivityConfig(newParentConfiguration, this.mTmpBounds);
        if (!this.mTmpBounds.isEmpty()) {
            resolvedBounds.set(this.mTmpBounds);
        }
        if (!this.mActivityRecordExt.isFlexibleSuitable() && !resolvedBounds.isEmpty() && !resolvedBounds.equals(parentBounds)) {
            this.mResolveConfigHint.mTmpOverrideDisplayInfo = getFixedRotationTransformDisplayInfo();
            computeConfigByResolveHint(resolvedConfig, newParentConfiguration);
            this.mLetterboxBoundsForAspectRatio = new android.graphics.Rect(resolvedBounds);
        }
        this.mActivityRecordExt.resolveAppOrientationIfNeed(this, resolvedConfig, getOverrideOrientation(), newParentConfiguration);
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x0203  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0209  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0222  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0226  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0159  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x017d  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01ac  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01d8  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01ea  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01f3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void resolveSizeCompatModeConfiguration(android.content.res.Configuration r27, com.android.server.wm.ActivityRecord.CompatDisplayInsets r28) {
        /*
            Method dump skipped, instruction units count: 743
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityRecord.resolveSizeCompatModeConfiguration(android.content.res.Configuration, com.android.server.wm.ActivityRecord$CompatDisplayInsets):void");
    }

    void updateSizeCompatScale(final android.graphics.Rect resolvedAppBounds, final android.graphics.Rect containerAppBounds) {
        this.mSizeCompatScale = ((java.lang.Float) this.mTransparentPolicy.findOpaqueNotFinishingActivityBelow().map(new java.util.function.Function() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda32
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Float.valueOf(((com.android.server.wm.ActivityRecord) obj).mSizeCompatScale);
            }
        }).orElseGet(new java.util.function.Supplier() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda33
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.wm.ActivityRecord.lambda$updateSizeCompatScale$24(resolvedAppBounds, containerAppBounds);
            }
        })).floatValue();
    }

    static /* synthetic */ java.lang.Float lambda$updateSizeCompatScale$24(android.graphics.Rect resolvedAppBounds, android.graphics.Rect containerAppBounds) {
        int contentW = resolvedAppBounds.width();
        int contentH = resolvedAppBounds.height();
        int viewportW = containerAppBounds.width();
        int viewportH = containerAppBounds.height();
        return java.lang.Float.valueOf((contentW > viewportW || contentH > viewportH) ? java.lang.Math.min(viewportW / contentW, viewportH / contentH) : 1.0f);
    }

    private boolean isInSizeCompatModeForBounds(android.graphics.Rect appBounds, android.graphics.Rect containerBounds) {
        if (this.mTransparentPolicy.isRunning()) {
            return false;
        }
        int appWidth = appBounds.width();
        int appHeight = appBounds.height();
        int containerAppWidth = containerBounds.width();
        int containerAppHeight = containerBounds.height();
        if (containerAppWidth == appWidth && containerAppHeight == appHeight) {
            return false;
        }
        if ((containerAppWidth > appWidth && containerAppHeight > appHeight) || containerAppWidth < appWidth || containerAppHeight < appHeight) {
            return true;
        }
        float maxAspectRatio = getMaxAspectRatio();
        if (maxAspectRatio > 0.0f) {
            float aspectRatio = (java.lang.Math.max(appWidth, appHeight) + 0.5f) / java.lang.Math.min(appWidth, appHeight);
            if (aspectRatio >= maxAspectRatio) {
                return false;
            }
        }
        float minAspectRatio = getMinAspectRatio();
        if (minAspectRatio > 0.0f) {
            float containerAspectRatio = (java.lang.Math.max(containerAppWidth, containerAppHeight) + 0.5f) / java.lang.Math.min(containerAppWidth, containerAppHeight);
            if (containerAspectRatio <= minAspectRatio) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getCenterOffset(int viewportDim, int contentDim) {
        return (int) (((viewportDim - contentDim) + 1) * 0.5f);
    }

    private static void offsetBounds(android.content.res.Configuration inOutConfig, int offsetX, int offsetY) {
        if (inOutConfig == null || inOutConfig.windowConfiguration == null) {
            return;
        }
        if (inOutConfig.windowConfiguration.getBounds() != null) {
            inOutConfig.windowConfiguration.getBounds().offset(offsetX, offsetY);
        }
        if (inOutConfig.windowConfiguration.getAppBounds() != null) {
            inOutConfig.windowConfiguration.getAppBounds().offset(offsetX, offsetY);
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public android.graphics.Rect getBounds() {
        final android.graphics.Rect superBounds = super.getBounds();
        return (android.graphics.Rect) this.mTransparentPolicy.findOpaqueNotFinishingActivityBelow().map(new java.util.function.Function() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda22
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.wm.ActivityRecord) obj).getBounds();
            }
        }).orElseGet(new java.util.function.Supplier() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda23
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$getBounds$25(superBounds);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.graphics.Rect lambda$getBounds$25(android.graphics.Rect superBounds) {
        if (this.mSizeCompatBounds != null) {
            if (this.mActivityRecordExt.isZoomMode(getWindowingMode())) {
                return super.getBounds();
            }
            if (this.mActivityRecordExt.isCompactWindowingMode(getWindowingMode())) {
                return super.getBounds();
            }
            return this.mSizeCompatBounds;
        }
        if (this.mActivityRecordExt.hasSizeCompatBoundsInOplusCompatMode()) {
            return this.mActivityRecordExt.getSizeCompatBoundsInOplusCompatMode();
        }
        return superBounds;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean providesMaxBounds() {
        if (getUid() == 1000) {
            return false;
        }
        if ((this.mDisplayContent == null || this.mDisplayContent.sandboxDisplayApis()) && !this.info.neverSandboxDisplayApis(sConstrainDisplayApisConfig)) {
            return this.info.alwaysSandboxDisplayApis(sConstrainDisplayApisConfig) || getCompatDisplayInsets() != null || shouldCreateCompatDisplayInsets() || this.mActivityRecordExt.getLaunchedFromMultiSearch() || (this.task != null && this.task.getWrapper().getExtImpl().getLaunchedFromMultiSearch()) || this.mActivityRecordExt.inOplusCompatMode() || this.mActivityRecordExt.inOplusActivityCompatMode();
        }
        return false;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    protected boolean setOverrideGender(android.content.res.Configuration requestsTmpConfig, int gender) {
        return com.android.server.wm.WindowProcessController.applyConfigGenderOverride(requestsTmpConfig, gender, this.mAtmService.mGrammaticalManagerInternal, getUid());
    }

    @Override // com.android.server.wm.WindowContainer
    android.graphics.Rect getAnimationBounds(int appRootTaskClipMode) {
        com.android.server.wm.TaskFragment taskFragment = getTaskFragment();
        return taskFragment != null ? taskFragment.getBounds() : getBounds();
    }

    @Override // com.android.server.wm.WindowContainer
    void getAnimationPosition(android.graphics.Point outPosition) {
        outPosition.set(0, 0);
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(android.content.res.Configuration newParentConfig) {
        int rotation;
        int projectedWindowingMode;
        this.mActivityRecordExt.onPreActivityRecordConfigurationChanged(newParentConfig);
        if (this.mActivityRecordExt.inOplusCompatMode()) {
            clearSizeCompatModeIfNeeded();
        }
        if (this.mTransitionController.isShellTransitionsEnabled() && isVisible() && isVisibleRequested()) {
            if (getRequestedOverrideWindowingMode() == 0) {
                projectedWindowingMode = newParentConfig.windowConfiguration.getWindowingMode();
            } else {
                projectedWindowingMode = getRequestedOverrideWindowingMode();
            }
            if (getWindowingMode() != projectedWindowingMode && (!this.mWaitForEnteringPinnedMode || !this.mTransitionController.inFinishingTransition(this))) {
                this.mTransitionController.collect(this);
            }
        }
        if (getCompatDisplayInsets() != null) {
            android.content.res.Configuration overrideConfig = getRequestedOverrideConfiguration();
            boolean wasFixedOrient = overrideConfig.windowConfiguration.getRotation() != -1;
            int requestedOrient = getRequestedConfigurationOrientation();
            if (requestedOrient != 0 && requestedOrient != getConfiguration().orientation && requestedOrient == getParent().getConfiguration().orientation && overrideConfig.windowConfiguration.getRotation() != getParent().getWindowConfiguration().getRotation()) {
                overrideConfig.windowConfiguration.setRotation(getParent().getWindowConfiguration().getRotation());
                onRequestedOverrideConfigurationChanged(overrideConfig);
                return;
            } else if (wasFixedOrient && requestedOrient == 0 && overrideConfig.windowConfiguration.getRotation() != -1) {
                overrideConfig.windowConfiguration.setRotation(-1);
                onRequestedOverrideConfigurationChanged(overrideConfig);
                return;
            }
        }
        boolean wasInPictureInPicture = inPinnedWindowingMode();
        com.android.server.wm.DisplayContent display = this.mDisplayContent;
        int activityType = getActivityType();
        if (wasInPictureInPicture && attachedToProcess() && display != null) {
            try {
                this.app.pauseConfigurationDispatch();
                super.onConfigurationChanged(newParentConfig);
                if (this.mVisibleRequested && !inMultiWindowMode() && (rotation = this.mActivityRecordExt.hookRotationForPIPIfNeeded(display.rotationForActivityInDifferentOrientation(this), display, this)) != -1) {
                    this.app.resumeConfigurationDispatch();
                    display.setFixedRotationLaunchingApp(this, rotation);
                }
            } finally {
                if (this.app.resumeConfigurationDispatch()) {
                    this.app.dispatchConfiguration(this.app.getConfiguration());
                }
            }
        } else {
            super.onConfigurationChanged(newParentConfig);
        }
        if (activityType != 0 && activityType != getActivityType()) {
            java.lang.String errorMessage = "Can't change activity type once set: " + this + " activityType=" + android.app.WindowConfiguration.activityTypeToString(getActivityType()) + ", was " + android.app.WindowConfiguration.activityTypeToString(activityType);
            if (android.os.Build.IS_DEBUGGABLE) {
                throw new java.lang.IllegalStateException(errorMessage);
            }
            android.util.Slog.w(TAG, errorMessage);
        }
        if (!wasInPictureInPicture && inPinnedWindowingMode() && this.task != null) {
            this.mWaitForEnteringPinnedMode = false;
            this.mTaskSupervisor.scheduleUpdatePictureInPictureModeIfNeeded(this.task, this.task.getBounds());
        }
        if (this.mWaitForEnteringPinnedMode && !wasInPictureInPicture && !inPinnedWindowingMode() && this.task != null && !this.task.inPinnedWindowingMode()) {
            this.mWaitForEnteringPinnedMode = false;
            android.util.Slog.d(TAG, "force reset mWaitForEnteringPinnedMode=" + this.mWaitForEnteringPinnedMode + ", this=" + this + ",task=" + this.task);
        }
        if (display == null) {
            return;
        }
        if (this.mVisibleRequested) {
            display.handleActivitySizeCompatModeIfNeeded(this);
            return;
        }
        if (getCompatDisplayInsets() != null && !this.visibleIgnoringKeyguard) {
            if (this.app == null || !this.app.hasVisibleActivities()) {
                int displayChanges = display.getCurrentOverrideConfigurationChanges();
                boolean hasNonOrienSizeChanged = hasResizeChange(displayChanges) && (displayChanges & 536872064) != 536872064;
                if (hasNonOrienSizeChanged || (displayChanges & 4096) != 0) {
                    restartProcessIfVisible();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.ConfigurationContainer
    public void dispatchConfigurationToChild(com.android.server.wm.WindowState child, android.content.res.Configuration config) {
        if (isConfigurationDispatchPaused()) {
            return;
        }
        super.dispatchConfigurationToChild(child, config);
    }

    void pauseConfigurationDispatch() {
        this.mPauseConfigurationDispatchCount++;
        if (this.mPauseConfigurationDispatchCount != 1 || !com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
            return;
        }
        java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 2612201759169917322L, 0, "Pausing configuration dispatch for  %s", protoLogParam0);
    }

    boolean resumeConfigurationDispatch() {
        this.mPauseConfigurationDispatchCount--;
        if (this.mPauseConfigurationDispatchCount > 0) {
            return false;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_MIN_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS_MIN, 5153784493059555057L, 0, "Resuming configuration dispatch for %s", protoLogParam0);
        }
        if (this.mPauseConfigurationDispatchCount < 0) {
            android.util.Slog.wtf(TAG, "Trying to resume non-paused configuration dispatch");
            this.mPauseConfigurationDispatchCount = 0;
            return false;
        }
        if (this.mLastReportedDisplayId == getDisplayId() && getConfiguration().equals(this.mLastReportedConfiguration.getMergedConfiguration())) {
            return false;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            dispatchConfigurationToChild((com.android.server.wm.WindowState) getChildAt(i), getConfiguration());
        }
        updateReportedConfigurationAndSend();
        return true;
    }

    boolean isConfigurationDispatchPaused() {
        return this.mPauseConfigurationDispatchCount > 0;
    }

    private boolean applyAspectRatio(android.graphics.Rect outBounds, android.graphics.Rect containingAppBounds, android.graphics.Rect containingBounds) {
        return applyAspectRatio(outBounds, containingAppBounds, containingBounds, 0.0f);
    }

    private boolean applyAspectRatio(android.graphics.Rect outBounds, android.graphics.Rect containingAppBounds, android.graphics.Rect containingBounds, float desiredAspectRatio) {
        int activityHeight;
        boolean adjustWidth;
        if (this.mActivityRecordExt.dontApplyAspectRatio(this)) {
            return false;
        }
        float maxAspectRatio = this.mActivityRecordExt.getFixedAspectRatioForActivity(this, true);
        if (maxAspectRatio <= 0.0f) {
            maxAspectRatio = this.mActivityRecordExt.getMaxAspectRatio(this.info, containingAppBounds);
        }
        com.android.server.wm.Task rootTask = getRootTask();
        float minAspectRatio = this.mActivityRecordExt.getFixedAspectRatioForActivity(this, false);
        if (minAspectRatio <= 0.0f) {
            minAspectRatio = getMinAspectRatio();
        }
        com.android.server.wm.TaskFragment organizedTf = getOrganizedTaskFragment();
        float aspectRatioToApply = desiredAspectRatio;
        if (this.task == null || rootTask == null) {
            return false;
        }
        if (maxAspectRatio >= 1.0f || minAspectRatio >= 1.0f || aspectRatioToApply >= 1.0f) {
            if ((!this.task.getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0]) || this.mActivityRecordExt.inOplusActivityCompatMode()) && !isInVrUiMode(getConfiguration())) {
                if (organizedTf != null && !organizedTf.fillsParent()) {
                    return false;
                }
                int containingAppWidth = containingAppBounds.width();
                int containingAppHeight = containingAppBounds.height();
                float containingRatio = computeAspectRatio(containingAppBounds);
                boolean inOplusCompatMode = this.mActivityRecordExt.inOplusCompatMode() || this.mActivityRecordExt.inOplusActivityCompatMode();
                if (inOplusCompatMode) {
                    containingAppWidth = containingBounds.width();
                    containingAppHeight = containingBounds.height();
                    containingRatio = computeAspectRatio(containingBounds);
                }
                if (aspectRatioToApply < 1.0f) {
                    aspectRatioToApply = containingRatio;
                }
                if (inOplusCompatMode) {
                    aspectRatioToApply = 0.0f;
                }
                if (maxAspectRatio >= 1.0f && aspectRatioToApply > maxAspectRatio) {
                    aspectRatioToApply = maxAspectRatio;
                } else if (minAspectRatio >= 1.0f && aspectRatioToApply < minAspectRatio) {
                    aspectRatioToApply = minAspectRatio;
                }
                int activityWidth = containingAppWidth;
                int activityHeight2 = containingAppHeight;
                if (containingRatio - aspectRatioToApply <= ASPECT_RATIO_ROUNDING_TOLERANCE || inOplusCompatMode) {
                    activityHeight = activityHeight2;
                    if (aspectRatioToApply - containingRatio > ASPECT_RATIO_ROUNDING_TOLERANCE || inOplusCompatMode) {
                        switch (getRequestedConfigurationOrientation()) {
                            case 1:
                                adjustWidth = true;
                                break;
                            case 2:
                                adjustWidth = false;
                                break;
                            default:
                                if (containingAppWidth < containingAppHeight) {
                                    adjustWidth = true;
                                } else {
                                    adjustWidth = false;
                                }
                                break;
                        }
                        if (this.mActivityRecordExt.adjustActivityWidth(this, adjustWidth)) {
                            activityWidth = (int) ((activityHeight / aspectRatioToApply) + 0.5f);
                        } else {
                            activityHeight = (int) ((activityWidth / aspectRatioToApply) + 0.5f);
                        }
                    }
                } else if (containingAppWidth < containingAppHeight) {
                    activityHeight = (int) ((activityWidth * aspectRatioToApply) + 0.5f);
                } else {
                    activityHeight = activityHeight2;
                    activityWidth = (int) ((activityHeight * aspectRatioToApply) + 0.5f);
                }
                if (containingAppWidth <= activityWidth && containingAppHeight <= activityHeight) {
                    return false;
                }
                if (inOplusCompatMode) {
                    outBounds.set(containingBounds.left, containingBounds.top, containingBounds.left + activityWidth, containingBounds.top + activityHeight);
                    return true;
                }
                int right = containingAppBounds.left + activityWidth;
                int left = containingAppBounds.left;
                if (right >= containingAppBounds.right) {
                    right = containingBounds.right;
                    left = containingBounds.left;
                }
                int bottom = containingAppBounds.top + activityHeight;
                int top = containingAppBounds.top;
                if (bottom >= containingAppBounds.bottom) {
                    bottom = containingBounds.bottom;
                    top = containingBounds.top;
                }
                outBounds.set(left, top, right, bottom);
                return true;
            }
            return false;
        }
        return false;
    }

    float getMinAspectRatio() {
        if (this.mTransparentPolicy.isRunning()) {
            return this.mTransparentPolicy.getInheritedMinAspectRatio();
        }
        if (this.info.applicationInfo == null) {
            return this.info.getMinAspectRatio();
        }
        if (this.mLetterboxUiController.shouldApplyUserMinAspectRatioOverride()) {
            return this.mLetterboxUiController.getUserMinAspectRatio();
        }
        if (!this.mLetterboxUiController.shouldOverrideMinAspectRatio() && !this.mLetterboxUiController.shouldOverrideMinAspectRatioForCamera()) {
            return this.info.getMinAspectRatio();
        }
        if (this.info.isChangeEnabled(203647190L) && !android.content.pm.ActivityInfo.isFixedOrientationPortrait(getOverrideOrientation())) {
            return this.info.getMinAspectRatio();
        }
        if (this.info.isChangeEnabled(218959984L) && isParentFullscreenPortrait()) {
            return this.info.getMinAspectRatio();
        }
        if (this.info.isChangeEnabled(208648326L)) {
            return java.lang.Math.max(this.mLetterboxUiController.getSplitScreenAspectRatio(), this.info.getMinAspectRatio());
        }
        if (this.info.isChangeEnabled(180326787L)) {
            return java.lang.Math.max(1.7777778f, this.info.getMinAspectRatio());
        }
        if (this.info.isChangeEnabled(180326845L)) {
            return java.lang.Math.max(1.5f, this.info.getMinAspectRatio());
        }
        return this.info.getMinAspectRatio();
    }

    private boolean isParentFullscreenPortrait() {
        com.android.server.wm.WindowContainer parent = getParent();
        return parent != null && parent.getConfiguration().orientation == 1 && parent.getWindowConfiguration().getWindowingMode() == 1;
    }

    float getMaxAspectRatio() {
        if (this.mTransparentPolicy.isRunning()) {
            return this.mTransparentPolicy.getInheritedMaxAspectRatio();
        }
        return this.info.getMaxAspectRatio();
    }

    private boolean hasFixedAspectRatio() {
        return (getMaxAspectRatio() == 0.0f && getMinAspectRatio() == 0.0f) ? false : true;
    }

    static float computeAspectRatio(android.graphics.Rect rect) {
        int width = rect.width();
        int height = rect.height();
        if (width == 0 || height == 0) {
            return 0.0f;
        }
        return java.lang.Math.max(width, height) / java.lang.Math.min(width, height);
    }

    boolean shouldUpdateConfigForDisplayChanged() {
        return this.mLastReportedDisplayId != getDisplayId();
    }

    boolean ensureActivityConfiguration() {
        return ensureActivityConfiguration(false);
    }

    boolean ensureActivityConfiguration(boolean ignoreVisibility) {
        com.android.server.wm.Task rootTask = getRootTask();
        if (rootTask == null) {
            return true;
        }
        if (rootTask.mConfigWillChange) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -8630021188868292872L, 0, null, protoLogParam0);
            }
            return true;
        }
        if (this.finishing) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -3976984054291875926L, 0, null, protoLogParam02);
            }
            return true;
        }
        if (isState(com.android.server.wm.ActivityRecord.State.DESTROYED)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                java.lang.String protoLogParam03 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -1036762753077003128L, 0, null, protoLogParam03);
            }
            return true;
        }
        if (!ignoreVisibility && (this.mState == com.android.server.wm.ActivityRecord.State.STOPPING || this.mState == com.android.server.wm.ActivityRecord.State.STOPPED || !shouldBeVisible())) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                java.lang.String protoLogParam04 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -6543078196636665108L, 0, null, protoLogParam04);
            }
            return true;
        }
        if (isConfigurationDispatchPaused()) {
            return true;
        }
        return updateReportedConfigurationAndSend();
    }

    boolean isCameraActive() {
        return (this.mDisplayContent == null || this.mDisplayContent.getDisplayRotationCompatPolicy() == null || !this.mDisplayContent.getDisplayRotationCompatPolicy().isCameraActive(this, true)) ? false : true;
    }

    boolean updateReportedConfigurationAndSend() {
        int changes;
        if (isConfigurationDispatchPaused()) {
            android.util.Slog.wtf(TAG, "trying to update reported(client) config while dispatch is paused");
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -3588725633248053181L, 0, null, protoLogParam0);
        }
        int newDisplayId = getDisplayId();
        boolean displayChanged = this.mLastReportedDisplayId != newDisplayId;
        int mTmpLastReportedDisplayId = this.mLastReportedDisplayId;
        if (displayChanged) {
            this.mActivityRecordExt.activityPreloadHandleDisplayChanged(getProcessGlobalConfiguration(), this.mLastReportedDisplayId);
            this.mActivityRecordExt.setLastReportedDisplay(getDisplayContent());
            this.mLastReportedDisplayId = newDisplayId;
        }
        if (this.mVisibleRequested) {
            updateCompatDisplayInsets();
        }
        this.mTmpConfig.setTo(this.mLastReportedConfiguration.getMergedConfiguration());
        android.window.ActivityWindowInfo newActivityWindowInfo = getActivityWindowInfo();
        boolean isActivityWindowInfoChanged = com.android.window.flags.Flags.activityWindowInfoFlag() && !this.mLastReportedActivityWindowInfo.equals(newActivityWindowInfo);
        if (displayChanged || isActivityWindowInfoChanged || !getConfiguration().equals(this.mTmpConfig)) {
            int changes2 = getConfigurationChanges(this.mTmpConfig);
            if (!displayChanged) {
                changes = changes2;
            } else {
                changes = this.mActivityRecordExt.needChangeDiff(changes2, mTmpLastReportedDisplayId, newDisplayId);
            }
            android.content.res.Configuration newMergedOverrideConfig = getMergedOverrideConfiguration();
            this.mActivityRecordExt.reviseMergedOverrideConfiguration(newMergedOverrideConfig, newDisplayId);
            boolean forceNewConfig = this.mActivityRecordExt.shouldForceRelaunch(changes, this.mLastReportedConfiguration, newMergedOverrideConfig, this.mTmpConfig, displayChanged);
            setLastReportedConfiguration(getProcessGlobalConfiguration(), newMergedOverrideConfig);
            int changes3 = changes;
            this.mActivityRecordExt.adjustLastReportedConfiguration(getProcessGlobalConfiguration(), newMergedOverrideConfig, changes3, this, this.mLastReportedConfiguration);
            setLastReportedActivityWindowInfo(newActivityWindowInfo);
            if (this.mState == com.android.server.wm.ActivityRecord.State.INITIALIZING) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -8624278141553396410L, 0, null, protoLogParam02);
                }
                return true;
            }
            if (changes3 == 0) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                    java.lang.String protoLogParam03 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, 2485365009287691179L, 0, null, protoLogParam03);
                }
                if (displayChanged) {
                    scheduleActivityMovedToDisplay(newDisplayId, newMergedOverrideConfig, newActivityWindowInfo);
                } else {
                    scheduleConfigurationChanged(newMergedOverrideConfig, newActivityWindowInfo);
                }
                notifyActivityRefresherAboutConfigurationChange(this.mLastReportedConfiguration.getMergedConfiguration(), this.mTmpConfig);
                return true;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                java.lang.String protoLogParam04 = java.lang.String.valueOf(this);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(android.content.res.Configuration.configurationDiffToString(changes3));
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -8909639363543223474L, 0, null, protoLogParam04, protoLogParam1);
            }
            if (this.mActivityRecordExt.loggingWhenFolding()) {
                android.util.Slog.d(TAG, "FSS_Configuration changes for " + this + ", allChanges=" + android.content.res.Configuration.configurationDiffToString(changes3));
            }
            if (!attachedToProcess()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                    java.lang.String protoLogParam05 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, -8048404379899908050L, 0, null, protoLogParam05);
                }
                return true;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                java.lang.String protoLogParam06 = java.lang.String.valueOf(this.info.name);
                java.lang.String protoLogParam12 = java.lang.String.valueOf(java.lang.Integer.toHexString(changes3));
                java.lang.String protoLogParam2 = java.lang.String.valueOf(java.lang.Integer.toHexString(this.info.getRealConfigChanged()));
                java.lang.String protoLogParam3 = java.lang.String.valueOf(this.mLastReportedConfiguration);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, 4979286847769557939L, 0, null, protoLogParam06, protoLogParam12, protoLogParam2, protoLogParam3);
            }
            android.util.Slog.d(TAG, "Checking to restart " + this + ": changed=0x = " + java.lang.Integer.toHexString(changes3) + "; handles=0x" + java.lang.Integer.toHexString(this.info.getRealConfigChanged()) + "; \n mLastReportedConfiguration = " + this.mLastReportedConfiguration + "; \n mFullConfig = " + getConfiguration());
            boolean ltwIgnore = true;
            if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE) {
                ltwIgnore = !this.mAtmService.getWrapper().getExtImpl().getRemoteTaskManager().shouldIgnoreRelaunch(displayChanged, mTmpLastReportedDisplayId, newDisplayId, changes3);
            }
            if ((shouldRelaunchLocked(changes3, this.mTmpConfig) && ltwIgnore) || forceNewConfig) {
                if (this.mVisible && this.mAtmService.mTmpUpdateConfigurationResult.mIsUpdating && !this.mTransitionController.isShellTransitionsEnabled()) {
                    startFreezingScreenLocked(this.app, this.mAtmService.mTmpUpdateConfigurationResult.changes);
                }
                boolean displayMayChange = (this.mTmpConfig.windowConfiguration.getDisplayRotation() == getWindowConfiguration().getDisplayRotation() && this.mTmpConfig.windowConfiguration.getMaxBounds().equals(getWindowConfiguration().getMaxBounds())) ? false : true;
                boolean isAppResizeOnly = !displayMayChange && (changes3 & (-3457)) == 0;
                boolean preserveWindow = isAppResizeOnly && !this.mFreezingScreen;
                boolean preserveWindow2 = this.mActivityRecordExt.adjustPreserveWindowWhenRelaunch(preserveWindow, changes3, newMergedOverrideConfig);
                boolean hasResizeChange = hasResizeChange((~this.info.getRealConfigChanged()) & changes3);
                if (!hasResizeChange) {
                    this.mRelaunchReason = 0;
                } else {
                    boolean isDragResizing = this.task.isDragResizing();
                    this.mRelaunchReason = isDragResizing ? 2 : 1;
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
                    java.lang.String protoLogParam07 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, 6779426581354721909L, 0, null, protoLogParam07);
                }
                if (!this.mVisibleRequested && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STATES_enabled[1]) {
                    java.lang.String protoLogParam08 = java.lang.String.valueOf(this);
                    java.lang.String protoLogParam13 = java.lang.String.valueOf(android.os.Debug.getCallers(4));
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STATES, 8969401915706456725L, 0, null, protoLogParam08, protoLogParam13);
                }
                relaunchActivityLocked(preserveWindow2, changes3);
                return false;
            }
            if (displayChanged) {
                scheduleActivityMovedToDisplay(newDisplayId, newMergedOverrideConfig, newActivityWindowInfo);
            } else {
                scheduleConfigurationChanged(newMergedOverrideConfig, newActivityWindowInfo);
            }
            notifyActivityRefresherAboutConfigurationChange(this.mLastReportedConfiguration.getMergedConfiguration(), this.mTmpConfig);
            return true;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONFIGURATION_enabled[1]) {
            java.lang.String protoLogParam09 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONFIGURATION, 4672360193194734037L, 0, null, protoLogParam09);
        }
        return true;
    }

    private void notifyActivityRefresherAboutConfigurationChange(android.content.res.Configuration newConfig, android.content.res.Configuration lastReportedConfig) {
        if (this.mDisplayContent.mActivityRefresher == null || !shouldBeResumed(null)) {
            return;
        }
        this.mDisplayContent.mActivityRefresher.onActivityConfigurationChanging(this, newConfig, lastReportedConfig);
    }

    private android.content.res.Configuration getProcessGlobalConfiguration() {
        return this.app != null ? this.app.getConfiguration() : this.mAtmService.getGlobalConfiguration();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldRelaunchLocked(int changes, android.content.res.Configuration changesConfig) {
        int configChanged = this.info.getRealConfigChanged();
        boolean onlyVrUiModeChanged = onlyVrUiModeChanged(changes, changesConfig);
        if (this.info.applicationInfo.targetSdkVersion < 26 && this.requestedVrComponent != null && onlyVrUiModeChanged) {
            configChanged |= 512;
        }
        if (this.mWmService.mSkipActivityRelaunchWhenDocking && onlyDeskInUiModeChanged(changesConfig) && !hasDeskResources()) {
            configChanged |= 512;
        }
        return this.mActivityRecordExt.hookShouldRelaunchLocked(changes, configChanged, changesConfig);
    }

    private boolean onlyVrUiModeChanged(int changes, android.content.res.Configuration lastReportedConfig) {
        android.content.res.Configuration currentConfig = getConfiguration();
        return changes == 512 && isInVrUiMode(currentConfig) != isInVrUiMode(lastReportedConfig);
    }

    private boolean onlyDeskInUiModeChanged(android.content.res.Configuration lastReportedConfig) {
        android.content.res.Configuration currentConfig = getConfiguration();
        boolean deskModeChanged = isInDeskUiMode(currentConfig) != isInDeskUiMode(lastReportedConfig);
        boolean uiModeOtherFieldsChanged = (currentConfig.uiMode & (-16)) != (lastReportedConfig.uiMode & (-16));
        return deskModeChanged && !uiModeOtherFieldsChanged;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0035, code lost:
    
        r6.mHasDeskResources = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean hasDeskResources() {
        /*
            r6 = this;
            java.lang.Boolean r0 = r6.mHasDeskResources
            if (r0 == 0) goto Lb
            java.lang.Boolean r0 = r6.mHasDeskResources
            boolean r0 = r0.booleanValue()
            return r0
        Lb:
            r0 = 0
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)
            r6.mHasDeskResources = r1
            com.android.server.wm.ActivityTaskManagerService r1 = r6.mAtmService     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            android.content.Context r1 = r1.mContext     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            java.lang.String r2 = r6.packageName     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            int r3 = r6.mUserId     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            android.os.UserHandle r3 = android.os.UserHandle.of(r3)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            android.content.Context r1 = r1.createPackageContextAsUser(r2, r0, r3)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            android.content.res.Resources r1 = r1.getResources()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            android.content.res.Configuration[] r2 = r1.getSizeAndUiModeConfigurations()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            int r3 = r2.length     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
        L2b:
            if (r0 >= r3) goto L40
            r4 = r2[r0]     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            boolean r5 = isInDeskUiMode(r4)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            if (r5 == 0) goto L3d
            r0 = 1
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            r6.mHasDeskResources = r0     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L41
            goto L40
        L3d:
            int r0 = r0 + 1
            goto L2b
        L40:
            goto L5a
        L41:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Exception thrown during checking for desk resources "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r6)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "ActivityTaskManager"
            android.util.Slog.w(r2, r1, r0)
        L5a:
            java.lang.Boolean r0 = r6.mHasDeskResources
            boolean r0 = r0.booleanValue()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityRecord.hasDeskResources():boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getConfigurationChanges(android.content.res.Configuration lastReportedConfig) {
        int changes = android.window.SizeConfigurationBuckets.filterDiff(lastReportedConfig.diff(getConfiguration()), lastReportedConfig, getConfiguration(), this.mSizeConfigurations);
        if ((536870912 & changes) != 0) {
            changes &= -536870913;
        }
        return this.mActivityRecordExt.calculateNewChanges(changes, lastReportedConfig, this.mSizeConfigurations);
    }

    private static boolean hasResizeChange(int change) {
        return (change & 3456) != 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0120 A[Catch: RemoteException -> 0x0148, TryCatch #0 {RemoteException -> 0x0148, blocks: (B:47:0x011a, B:49:0x0120, B:51:0x0135, B:50:0x012f), top: B:66:0x011a }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x012f A[Catch: RemoteException -> 0x0148, TryCatch #0 {RemoteException -> 0x0148, blocks: (B:47:0x011a, B:49:0x0120, B:51:0x0135, B:50:0x012f), top: B:66:0x011a }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01a5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void relaunchActivityLocked(boolean r23, int r24) {
        /*
            Method dump skipped, instruction units count: 445
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ActivityRecord.relaunchActivityLocked(boolean, int):void");
    }

    void restartProcessIfVisible() {
        if (this.finishing) {
            return;
        }
        android.util.Slog.i(TAG, "Request to restart process of " + this);
        clearSizeCompatMode();
        if (!attachedToProcess()) {
            return;
        }
        callServiceTrackeronActivityStatechange(com.android.server.wm.ActivityRecord.State.RESTARTING_PROCESS, true);
        setState(com.android.server.wm.ActivityRecord.State.RESTARTING_PROCESS, "restartActivityProcess");
        if (!this.mVisibleRequested || this.mHaveState) {
            if (DEBUG_PANIC) {
                android.util.Slog.d(TAG, "AMS kill message post, caller: " + android.os.Debug.getCallers(10));
            }
            this.mAtmService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$restartProcessIfVisible$26();
                }
            });
        } else if (this.mTransitionController.isShellTransitionsEnabled()) {
            final com.android.server.wm.Transition transition = new com.android.server.wm.Transition(5, 0, this.mTransitionController, this.mWmService.mSyncEngine);
            this.mTransitionController.startCollectOrQueue(transition, new com.android.server.wm.TransitionController.OnStartCollect() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda12
                @Override // com.android.server.wm.TransitionController.OnStartCollect
                public final void onCollectStarted(boolean z) {
                    this.f$0.lambda$restartProcessIfVisible$27(transition, z);
                }
            });
        } else {
            startFreezingScreen();
            scheduleStopForRestartProcess();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restartProcessIfVisible$26() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mAtmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (hasProcess() && this.app.getReportedProcState() > 6) {
                    com.android.server.wm.WindowProcessController wpc = this.app;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    this.mAtmService.mAmInternal.killProcess(wpc.mName, wpc.mUid, "resetConfig");
                    if (DEBUG_PANIC) {
                        android.util.Slog.d(TAG, "AMS killInterface called!");
                        return;
                    }
                    return;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restartProcessIfVisible$27(com.android.server.wm.Transition transition, boolean deferred) {
        if (this.mState != com.android.server.wm.ActivityRecord.State.RESTARTING_PROCESS || !attachedToProcess()) {
            transition.abort();
            return;
        }
        setVisibleRequested(false);
        transition.collect(this);
        this.mTransitionController.requestStartTransition(transition, this.task, null, null);
        scheduleStopForRestartProcess();
    }

    private void scheduleStopForRestartProcess() {
        try {
            this.mAtmService.getLifecycleManager().scheduleTransactionItem(this.app.getThread(), android.app.servertransaction.StopActivityItem.obtain(this.token));
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Exception thrown during restart " + this, e);
        }
        this.mTaskSupervisor.scheduleRestartTimeout(this);
    }

    boolean isProcessRunning() {
        com.android.server.wm.WindowProcessController proc = this.app;
        if (proc == null) {
            proc = (com.android.server.wm.WindowProcessController) this.mAtmService.mProcessNames.get(this.processName, this.info.applicationInfo.uid);
        }
        return proc != null && proc.hasThread();
    }

    private boolean allowTaskSnapshot() {
        if (this.mActivityRecordExt.isFlexibleZoomWindow(getWindowingMode())) {
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_SWITCH) {
                android.util.Slog.v(TAG_SWITCH, "Don't allow Snapshot for starting window in zoom mode, this=" + this);
            }
            return false;
        }
        if (this.newIntents == null) {
            return true;
        }
        for (int i = this.newIntents.size() - 1; i >= 0; i--) {
            android.content.Intent intent = this.newIntents.get(i);
            if (intent != null && !isMainIntent(intent)) {
                boolean sameIntent = this.mLastNewIntent != null ? this.mLastNewIntent.filterEquals(intent) : this.intent.filterEquals(intent);
                if (!sameIntent || intent.getExtras() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean isNoHistory() {
        return ((this.intent.getFlags() & 1073741824) == 0 && (this.info.flags & 128) == 0) ? false : true;
    }

    void saveToXml(com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.wm.ActivityCallerState.CallerInfo initialCallerInfo;
        out.attributeLong((java.lang.String) null, ATTR_ID, this.createTime);
        out.attributeInt((java.lang.String) null, ATTR_LAUNCHEDFROMUID, this.launchedFromUid);
        if (this.launchedFromPackage != null) {
            out.attribute((java.lang.String) null, ATTR_LAUNCHEDFROMPACKAGE, this.launchedFromPackage);
        }
        if (this.launchedFromFeatureId != null) {
            out.attribute((java.lang.String) null, ATTR_LAUNCHEDFROMFEATURE, this.launchedFromFeatureId);
        }
        if (this.resolvedType != null) {
            out.attribute((java.lang.String) null, ATTR_RESOLVEDTYPE, this.resolvedType);
        }
        out.attributeBoolean((java.lang.String) null, ATTR_COMPONENTSPECIFIED, this.componentSpecified);
        out.attributeInt((java.lang.String) null, ATTR_USERID, this.mUserId);
        if (this.taskDescription != null) {
            this.taskDescription.saveToXml(out);
        }
        out.startTag((java.lang.String) null, TAG_INTENT);
        this.intent.saveToXml(out);
        out.endTag((java.lang.String) null, TAG_INTENT);
        if (isPersistable() && this.mPersistentState != null) {
            out.startTag((java.lang.String) null, TAG_PERSISTABLEBUNDLE);
            this.mPersistentState.saveToXml(out);
            out.endTag((java.lang.String) null, TAG_PERSISTABLEBUNDLE);
        }
        if (android.security.Flags.contentUriPermissionApis() && (initialCallerInfo = this.mCallerState.getCallerInfoOrNull(this.initialCallerInfoAccessToken)) != null) {
            out.startTag((java.lang.String) null, TAG_INITIAL_CALLER_INFO);
            initialCallerInfo.saveToXml(out);
            out.endTag((java.lang.String) null, TAG_INITIAL_CALLER_INFO);
        }
    }

    static com.android.server.wm.ActivityRecord restoreFromXml(com.android.modules.utils.TypedXmlPullParser in, com.android.server.wm.ActivityTaskSupervisor taskSupervisor) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth;
        android.content.Intent intent = null;
        android.os.PersistableBundle persistentState = null;
        int launchedFromUid = in.getAttributeInt((java.lang.String) null, ATTR_LAUNCHEDFROMUID, 0);
        java.lang.String launchedFromPackage = in.getAttributeValue((java.lang.String) null, ATTR_LAUNCHEDFROMPACKAGE);
        java.lang.String launchedFromFeature = in.getAttributeValue((java.lang.String) null, ATTR_LAUNCHEDFROMFEATURE);
        java.lang.String resolvedType = in.getAttributeValue((java.lang.String) null, ATTR_RESOLVEDTYPE);
        boolean componentSpecified = in.getAttributeBoolean((java.lang.String) null, ATTR_COMPONENTSPECIFIED, false);
        int userId = in.getAttributeInt((java.lang.String) null, ATTR_USERID, 0);
        long createTime = in.getAttributeLong((java.lang.String) null, ATTR_ID, -1L);
        int outerDepth2 = in.getDepth();
        android.app.ActivityManager.TaskDescription taskDescription = new android.app.ActivityManager.TaskDescription();
        taskDescription.restoreFromXml(in);
        com.android.server.wm.ActivityCallerState.CallerInfo initialCallerInfo = null;
        while (true) {
            int event = in.next();
            if (event != 1 && (event != 3 || in.getDepth() >= outerDepth2)) {
                if (event == 2) {
                    java.lang.String name = in.getName();
                    if (TAG_INTENT.equals(name)) {
                        intent = android.content.Intent.restoreFromXml(in);
                        outerDepth = outerDepth2;
                    } else if (TAG_PERSISTABLEBUNDLE.equals(name)) {
                        persistentState = android.os.PersistableBundle.restoreFromXml(in);
                        outerDepth = outerDepth2;
                    } else if (android.security.Flags.contentUriPermissionApis() && TAG_INITIAL_CALLER_INFO.equals(name)) {
                        outerDepth = outerDepth2;
                        initialCallerInfo = com.android.server.wm.ActivityCallerState.CallerInfo.restoreFromXml(in);
                    } else {
                        outerDepth = outerDepth2;
                        android.util.Slog.w(TAG, "restoreActivity: unexpected name=" + name);
                        com.android.internal.util.XmlUtils.skipCurrentTag(in);
                    }
                    outerDepth2 = outerDepth;
                }
            }
        }
        if (intent == null) {
            throw new org.xmlpull.v1.XmlPullParserException("restoreActivity error intent=" + intent);
        }
        com.android.server.wm.ActivityTaskManagerService service = taskSupervisor.mService;
        com.android.server.wm.ActivityCallerState.CallerInfo initialCallerInfo2 = initialCallerInfo;
        android.content.pm.ActivityInfo aInfo = taskSupervisor.resolveActivity(intent, resolvedType, 0, null, userId, android.os.Binder.getCallingUid(), 0);
        if (aInfo == null) {
            throw new org.xmlpull.v1.XmlPullParserException("restoreActivity resolver error. Intent=" + intent + " resolvedType=" + resolvedType);
        }
        com.android.server.wm.ActivityRecord r = new com.android.server.wm.ActivityRecord.Builder(service).setLaunchedFromUid(launchedFromUid).setLaunchedFromPackage(launchedFromPackage).setLaunchedFromFeature(launchedFromFeature).setIntent(intent).setResolvedType(resolvedType).setActivityInfo(aInfo).setComponentSpecified(componentSpecified).setPersistentState(persistentState).setTaskDescription(taskDescription).setCreateTime(createTime).build();
        if (android.security.Flags.contentUriPermissionApis() && initialCallerInfo2 != null) {
            r.mCallerState.add(r.initialCallerInfoAccessToken, initialCallerInfo2);
        }
        return r;
    }

    private static boolean isInVrUiMode(android.content.res.Configuration config) {
        return (config.uiMode & 15) == 7;
    }

    private static boolean isInDeskUiMode(android.content.res.Configuration config) {
        return (config.uiMode & 15) == 2;
    }

    java.lang.String getProcessName() {
        return this.info.applicationInfo.processName;
    }

    int getUid() {
        return this.info.applicationInfo.uid;
    }

    boolean isUid(int uid) {
        return this.info.applicationInfo.uid == uid;
    }

    int getPid() {
        if (this.app != null) {
            return this.app.getPid();
        }
        return 0;
    }

    int getLaunchedFromPid() {
        return this.launchedFromPid;
    }

    int getLaunchedFromUid() {
        return this.launchedFromUid;
    }

    java.lang.String getFilteredReferrer(java.lang.String referrerPackage) {
        if (referrerPackage != null) {
            if (!referrerPackage.equals(this.packageName) && this.mWmService.mPmInternal.filterAppAccess(referrerPackage, this.info.applicationInfo.uid, this.mUserId)) {
                return null;
            }
            return referrerPackage;
        }
        return null;
    }

    boolean canTurnScreenOn() {
        return getTurnScreenOnFlag() && this.mCurrentLaunchCanTurnScreenOn && this.mTaskSupervisor.getKeyguardController().checkKeyguardVisibility(this);
    }

    void setTurnScreenOn(boolean turnScreenOn) {
        this.mActivityRecordExt.resolveScreenOnFlag(this, turnScreenOn);
        this.mTurnScreenOn = turnScreenOn;
    }

    void setAllowCrossUidActivitySwitchFromBelow(boolean allowed) {
        this.mAllowCrossUidActivitySwitchFromBelow = allowed;
    }

    boolean getTurnScreenOnFlag() {
        return this.mTurnScreenOn || containsTurnScreenOnWindow();
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
    private boolean containsTurnScreenOnWindow() {
        if (isRelaunching()) {
            return this.mLastContainsTurnScreenOnWindow;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if ((((com.android.server.wm.WindowState) this.mChildren.get(i)).mAttrs.flags & 2097152) != 0) {
                return true;
            }
        }
        return false;
    }

    boolean canResumeByCompat() {
        return this.app == null || this.app.updateTopResumingActivityInProcessIfNeeded(this);
    }

    boolean isTopRunningActivity() {
        return this.mRootWindowContainer.topRunningActivity() == this;
    }

    boolean isFocusedActivityOnDisplay() {
        return this.mDisplayContent.forAllTaskDisplayAreas(new java.util.function.Predicate() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$isFocusedActivityOnDisplay$28((com.android.server.wm.TaskDisplayArea) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$isFocusedActivityOnDisplay$28(com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        return taskDisplayArea.getFocusedActivity() == this;
    }

    boolean isRootOfTask() {
        if (this.task == null) {
            return false;
        }
        com.android.server.wm.ActivityRecord rootActivity = this.task.getRootActivity(true);
        return this == rootActivity;
    }

    void setTaskOverlay(boolean taskOverlay) {
        this.mTaskOverlay = taskOverlay;
        setAlwaysOnTop(this.mTaskOverlay);
    }

    boolean isTaskOverlay() {
        return this.mTaskOverlay;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public boolean isAlwaysOnTop() {
        return this.mTaskOverlay || super.isAlwaysOnTop();
    }

    @Override // com.android.server.wm.WindowContainer
    boolean showToCurrentUser() {
        return this.mShowForAllUsers || this.mWmService.isUserVisible(this.mUserId);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean canCustomizeAppTransition() {
        return true;
    }

    @Override // com.android.server.wm.WindowToken
    public java.lang.String toString() {
        if (this.stringName != null) {
            return this.stringName + " t" + (this.task == null ? -1 : this.task.mTaskId) + (this.finishing ? " f}" : "") + (this.mIsExiting ? " isExiting" : "") + "}";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("ActivityRecord{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(" u");
        sb.append(this.mUserId);
        sb.append(' ');
        sb.append(this.intent.getComponent().flattenToShortString());
        this.stringName = sb.toString();
        return this.stringName;
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, int logLevel) {
        writeNameToProto(proto, 1138166333441L);
        super.dumpDebug(proto, 1146756268034L, logLevel);
        proto.write(1133871366147L, this.mLastSurfaceShowing);
        proto.write(1133871366148L, isWaitingForTransitionStart());
        proto.write(1133871366149L, isAnimating(7, 17));
        if (this.mThumbnail != null) {
            this.mThumbnail.dumpDebug(proto, 1146756268038L);
        }
        proto.write(1133871366151L, fillsParent());
        proto.write(1133871366152L, this.mAppStopped);
        proto.write(1133871366174L, !occludesParent());
        proto.write(1133871366168L, this.mVisible);
        proto.write(1133871366153L, this.mVisibleRequested);
        proto.write(1133871366154L, isClientVisible());
        proto.write(1133871366155L, this.mDeferHidingClient);
        proto.write(1133871366156L, this.mReportedDrawn);
        proto.write(1133871366157L, this.reportedVisible);
        proto.write(1120986464270L, this.mNumInterestingWindows);
        proto.write(1120986464271L, this.mNumDrawnWindows);
        proto.write(1133871366160L, this.allDrawn);
        proto.write(1133871366161L, this.mLastAllDrawn);
        if (this.mStartingWindow != null) {
            this.mStartingWindow.writeIdentifierToProto(proto, 1146756268051L);
        }
        proto.write(1133871366164L, isStartingWindowDisplayed());
        proto.write(1133871366345L, this.startingMoved);
        proto.write(1133871366166L, this.mVisibleSetFromTransferredStartingWindow);
        proto.write(1138166333467L, this.mState.toString());
        proto.write(1133871366172L, isRootOfTask());
        if (hasProcess()) {
            proto.write(1120986464285L, this.app.getPid());
        }
        proto.write(1133871366175L, this.pictureInPictureArgs.isAutoEnterEnabled());
        proto.write(1133871366176L, inSizeCompatMode());
        proto.write(1108101562401L, getMinAspectRatio());
        proto.write(1133871366178L, providesMaxBounds());
        proto.write(1133871366179L, this.mEnableRecentsScreenshot);
        proto.write(1120986464292L, this.mLastDropInputMode);
        proto.write(1120986464293L, getOverrideOrientation());
        proto.write(1133871366182L, shouldSendCompatFakeFocus());
        proto.write(1133871366183L, this.mLetterboxUiController.shouldForceRotateForCameraCompat());
        proto.write(1133871366184L, this.mLetterboxUiController.shouldRefreshActivityForCameraCompat());
        proto.write(1133871366185L, this.mLetterboxUiController.shouldRefreshActivityViaPauseForCameraCompat());
        proto.write(1133871366186L, this.mLetterboxUiController.shouldOverrideMinAspectRatio());
        proto.write(1133871366187L, this.mLetterboxUiController.shouldIgnoreOrientationRequestLoop());
        proto.write(1133871366188L, this.mLetterboxUiController.shouldOverrideForceResizeApp());
        proto.write(1133871366189L, this.mLetterboxUiController.shouldEnableUserAspectRatioSettings());
        proto.write(1133871366190L, this.mLetterboxUiController.isUserFullscreenOverrideEnabled());
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268038L;
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        if (logLevel == 2 && !isVisible()) {
            return;
        }
        long token = proto.start(fieldId);
        dumpDebug(proto, logLevel);
        proto.end(token);
    }

    void writeNameToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        proto.write(fieldId, this.shortComponentName);
    }

    @Override // com.android.server.wm.WindowContainer
    void writeIdentifierToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, java.lang.System.identityHashCode(this));
        proto.write(1120986464258L, this.mUserId);
        proto.write(1138166333443L, this.intent.getComponent().flattenToShortString());
        proto.end(token);
    }

    void logLaunchTime() {
        if (this.launchTimeStartOppo != 0) {
            long startupTimeMs = android.os.SystemClock.uptimeMillis() - this.launchTimeStartOppo;
            if (this.info != null && this.info.applicationInfo != null) {
                this.mActivityRecordExt.notifyLaunchTime(this.info.applicationInfo, this.mActivityComponent.getClassName(), startupTimeMs);
            }
            this.launchTimeStartOppo = 0L;
        }
    }

    void setLaunchTimeStart() {
        if (this.launchTimeStartOppo == 0) {
            this.launchTimeStartOppo = android.os.SystemClock.uptimeMillis();
        }
    }

    static class CompatDisplayInsets {
        private final int mHeight;
        final boolean mIsFloating;
        final boolean mIsInFixedOrientationOrAspectRatioLetterbox;
        final int mOriginalRequestedOrientation;
        final int mOriginalRotation;
        private final int mWidth;
        final android.graphics.Rect[] mNonDecorInsets = new android.graphics.Rect[4];
        final android.graphics.Rect[] mStableInsets = new android.graphics.Rect[4];

        CompatDisplayInsets(com.android.server.wm.DisplayContent display, com.android.server.wm.ActivityRecord container, android.graphics.Rect letterboxedContainerBounds, boolean useOverrideInsets) {
            android.graphics.Rect filledContainerBounds;
            int filledContainerRotation;
            int i = 4;
            this.mOriginalRotation = display.getRotation();
            this.mIsFloating = container.getWindowConfiguration().tasksAreFloating();
            this.mOriginalRequestedOrientation = container.getRequestedConfigurationOrientation();
            int i2 = 0;
            if (this.mIsFloating) {
                android.graphics.Rect containerBounds = container.getWindowConfiguration().getBounds();
                this.mWidth = containerBounds.width();
                this.mHeight = containerBounds.height();
                android.graphics.Rect emptyRect = new android.graphics.Rect();
                for (int rotation = 0; rotation < 4; rotation++) {
                    this.mNonDecorInsets[rotation] = emptyRect;
                    this.mStableInsets[rotation] = emptyRect;
                }
                this.mIsInFixedOrientationOrAspectRatioLetterbox = false;
                return;
            }
            com.android.server.wm.Task task = container.getTask();
            int i3 = 1;
            this.mIsInFixedOrientationOrAspectRatioLetterbox = letterboxedContainerBounds != null;
            if (this.mIsInFixedOrientationOrAspectRatioLetterbox) {
                filledContainerBounds = letterboxedContainerBounds;
            } else {
                filledContainerBounds = task != null ? task.getBounds() : display.getBounds();
            }
            boolean useActivityRotation = container.hasFixedRotationTransform() && this.mIsInFixedOrientationOrAspectRatioLetterbox;
            if (useActivityRotation) {
                filledContainerRotation = container.getWindowConfiguration().getRotation();
            } else {
                filledContainerRotation = display.getConfiguration().windowConfiguration.getRotation();
            }
            android.graphics.Point dimensions = getRotationZeroDimensions(filledContainerBounds, filledContainerRotation);
            this.mWidth = dimensions.x;
            this.mHeight = dimensions.y;
            android.graphics.Rect unfilledContainerBounds = filledContainerBounds.equals(display.getBounds()) ? null : new android.graphics.Rect();
            com.android.server.wm.DisplayPolicy policy = display.getDisplayPolicy();
            int rotation2 = 0;
            while (rotation2 < i) {
                this.mNonDecorInsets[rotation2] = new android.graphics.Rect();
                this.mStableInsets[rotation2] = new android.graphics.Rect();
                int i4 = (rotation2 == i3 || rotation2 == 3) ? i3 : i2;
                int dw = i4 != 0 ? display.mBaseDisplayHeight : display.mBaseDisplayWidth;
                int dh = i4 != 0 ? display.mBaseDisplayWidth : display.mBaseDisplayHeight;
                com.android.server.wm.DisplayPolicy.DecorInsets.Info decorInfo = policy.getDecorInsetsInfo(rotation2, dw, dh);
                if (useOverrideInsets) {
                    this.mStableInsets[rotation2].set(decorInfo.mOverrideConfigInsets);
                    this.mNonDecorInsets[rotation2].set(decorInfo.mOverrideNonDecorInsets);
                } else {
                    this.mStableInsets[rotation2].set(decorInfo.mConfigInsets);
                    this.mNonDecorInsets[rotation2].set(decorInfo.mNonDecorInsets);
                }
                if (unfilledContainerBounds != null) {
                    unfilledContainerBounds.set(filledContainerBounds);
                    display.rotateBounds(filledContainerRotation, rotation2, unfilledContainerBounds);
                    updateInsetsForBounds(unfilledContainerBounds, dw, dh, this.mNonDecorInsets[rotation2]);
                    updateInsetsForBounds(unfilledContainerBounds, dw, dh, this.mStableInsets[rotation2]);
                }
                rotation2++;
                i = 4;
                i2 = 0;
                i3 = 1;
            }
        }

        private static android.graphics.Point getRotationZeroDimensions(android.graphics.Rect bounds, int rotation) {
            boolean rotated = true;
            if (rotation != 1 && rotation != 3) {
                rotated = false;
            }
            int width = bounds.width();
            int height = bounds.height();
            return rotated ? new android.graphics.Point(height, width) : new android.graphics.Point(width, height);
        }

        private static void updateInsetsForBounds(android.graphics.Rect bounds, int displayWidth, int displayHeight, android.graphics.Rect inset) {
            inset.left = java.lang.Math.max(0, inset.left - bounds.left);
            inset.top = java.lang.Math.max(0, inset.top - bounds.top);
            inset.right = java.lang.Math.max(0, (bounds.right - displayWidth) + inset.right);
            inset.bottom = java.lang.Math.max(0, (bounds.bottom - displayHeight) + inset.bottom);
        }

        void getBoundsByRotation(android.graphics.Rect outBounds, int rotation) {
            boolean rotated = true;
            if (rotation != 1 && rotation != 3) {
                rotated = false;
            }
            int dw = rotated ? this.mHeight : this.mWidth;
            int dh = rotated ? this.mWidth : this.mHeight;
            outBounds.set(0, 0, dw, dh);
        }

        void getFrameByOrientation(android.graphics.Rect outBounds, int orientation) {
            int longSide = java.lang.Math.max(this.mWidth, this.mHeight);
            int shortSide = java.lang.Math.min(this.mWidth, this.mHeight);
            boolean isLandscape = orientation == 2;
            outBounds.set(0, 0, isLandscape ? longSide : shortSide, isLandscape ? shortSide : longSide);
        }

        void getContainerBounds(android.graphics.Rect outAppBounds, android.graphics.Rect outBounds, int rotation, int orientation, boolean orientationRequested, boolean isFixedToUserRotation) {
            getFrameByOrientation(outBounds, orientation);
            if (this.mIsFloating) {
                outAppBounds.set(outBounds);
                return;
            }
            getBoundsByRotation(outAppBounds, rotation);
            int dW = outAppBounds.width();
            int dH = outAppBounds.height();
            boolean isOrientationMismatched = (outBounds.width() > outBounds.height()) != (dW > dH);
            if (isOrientationMismatched && isFixedToUserRotation && orientationRequested) {
                if (orientation == 2) {
                    outBounds.bottom = (int) ((dW * dW) / dH);
                    outBounds.right = dW;
                } else {
                    outBounds.bottom = dH;
                    outBounds.right = (int) ((dH * dH) / dW);
                }
                outBounds.offset(com.android.server.wm.ActivityRecord.getCenterOffset(this.mWidth, outBounds.width()), 0);
            }
            outAppBounds.set(outBounds);
            if (isOrientationMismatched) {
                android.graphics.Rect insets = this.mNonDecorInsets[rotation];
                outBounds.offset(insets.left, insets.top);
                outAppBounds.offset(insets.left, insets.top);
            } else if (rotation != -1) {
                com.android.server.wm.TaskFragment.intersectWithInsetsIfFits(outAppBounds, outBounds, this.mNonDecorInsets[rotation]);
            }
        }
    }

    private static class AppSaturationInfo {
        float[] mMatrix;
        float[] mTranslation;

        private AppSaturationInfo() {
            this.mMatrix = new float[9];
            this.mTranslation = new float[3];
        }

        void setSaturation(float[] matrix, float[] translation) {
            java.lang.System.arraycopy(matrix, 0, this.mMatrix, 0, this.mMatrix.length);
            java.lang.System.arraycopy(translation, 0, this.mTranslation, 0, this.mTranslation.length);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    android.view.RemoteAnimationTarget createRemoteAnimationTarget(com.android.server.wm.RemoteAnimationController.RemoteAnimationRecord record) {
        com.android.server.wm.WindowState mainWindow = findMainWindow();
        if ((this.task == null || mainWindow == null) && !this.mActivityRecordExt.forceCreateRemoteAnimationTarget(this)) {
            return null;
        }
        android.graphics.Rect defaultInsets = mainWindow != null ? mainWindow.getInsetsStateWithVisibilityOverride().calculateInsets(this.task.getBounds(), android.view.WindowInsets.Type.systemBars(), false).toRect() : new android.graphics.Rect();
        android.graphics.Rect insets = this.mActivityRecordExt.calculateInsetsForAnimationTarget(this, defaultInsets);
        com.android.server.wm.utils.InsetUtils.addInsets(insets, getLetterboxInsets());
        android.view.RemoteAnimationTarget target = new android.view.RemoteAnimationTarget(this.task.mTaskId, record.getMode(), record.mAdapter.mCapturedLeash, !fillsParent(), new android.graphics.Rect(), insets, getPrefixOrderIndex(), record.mAdapter.mPosition, record.mAdapter.mLocalBounds, record.mAdapter.mEndBounds, this.task.getWindowConfiguration(), false, record.mThumbnailAdapter != null ? record.mThumbnailAdapter.mCapturedLeash : null, record.mStartBounds, this.task.getTaskInfo(), checkEnterPictureInPictureAppOpsState());
        target.setShowBackdrop(record.mShowBackdrop);
        target.setWillShowImeOnTarget(this.mStartingData != null && this.mStartingData.hasImeSurface());
        target.hasAnimatingParent = record.hasAnimatingParent();
        return target;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean canCreateRemoteAnimationTarget() {
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    void getAnimationFrames(android.graphics.Rect outFrame, android.graphics.Rect outInsets, android.graphics.Rect outStableInsets, android.graphics.Rect outSurfaceInsets) {
        com.android.server.wm.WindowState win = findMainWindow();
        if (win == null) {
            this.mActivityRecordExt.setupAppFrameForCompatMode(outFrame, getBounds(), this);
        } else {
            win.getAnimationFrames(outFrame, outInsets, outStableInsets, outSurfaceInsets);
        }
    }

    void setPictureInPictureParams(android.app.PictureInPictureParams p) {
        this.pictureInPictureArgs.copyOnlySet(p);
        adjustPictureInPictureParamsIfNeeded(getBounds());
        getTask().getRootTask().onPictureInPictureParamsChanged();
    }

    void setShouldDockBigOverlays(boolean shouldDockBigOverlays) {
        this.shouldDockBigOverlays = shouldDockBigOverlays;
        getTask().getRootTask().onShouldDockBigOverlaysChanged();
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
    boolean isSyncFinished(com.android.server.wm.BLASTSyncEngine.SyncGroup group) {
        com.android.server.wm.WindowState startingWin;
        if (this.mActivityRecordExt.isShowStartingSurfaceLocked(this) || this.mActivityRecordExt.syncFinishedForOptimizeStartup(this)) {
            return true;
        }
        if (this.task != null && this.task.mSharedStartingData != null && (startingWin = this.task.topStartingWindow()) != null && startingWin.mSyncState == 2 && this.mDisplayContent.mUnknownAppVisibilityController.allResolved()) {
            return true;
        }
        if (!super.isSyncFinished(group)) {
            return false;
        }
        if (this.mDisplayContent != null && this.mDisplayContent.mUnknownAppVisibilityController.isVisibilityUnknown(this)) {
            return false;
        }
        if (!isVisibleRequested()) {
            return true;
        }
        if (this.mPendingRelaunchCount > 0 || !isAttached()) {
            return false;
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            if (((com.android.server.wm.WindowState) this.mChildren.get(i)).isVisibleRequested()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    void finishSync(android.view.SurfaceControl.Transaction outMergedTransaction, com.android.server.wm.BLASTSyncEngine.SyncGroup group, boolean cancel) {
        com.android.server.wm.BLASTSyncEngine.SyncGroup syncGroup = getSyncGroup();
        if (syncGroup == null || group == getSyncGroup()) {
            this.mLastAllReadyAtSync = allSyncFinished();
            super.finishSync(outMergedTransaction, group, cancel);
        }
    }

    void clearSizeCompatModeIfNeeded() {
        if (this.mActivityRecordExt.inOplusCompatEnabled()) {
            android.content.res.Configuration resolvedConfig = getResolvedOverrideConfiguration();
            resolvedConfig.unset();
            float lastSizeCompatScale = this.mSizeCompatScale;
            this.mInSizeCompatModeForBounds = false;
            this.mSizeCompatScale = 1.0f;
            this.mSizeCompatBounds = null;
            this.mCompatDisplayInsets = null;
            if (this.mSizeCompatScale != lastSizeCompatScale) {
                forAllWindows((java.util.function.Consumer<com.android.server.wm.WindowState>) new com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda8(), false);
            }
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                android.util.Slog.d(TAG, "clearSizeCompatModeIfNeeded " + this + " " + android.os.Debug.getCallers(5));
            }
        }
    }

    android.graphics.Point getMinDimensions() {
        android.content.pm.ActivityInfo.WindowLayout windowLayout = this.info.windowLayout;
        if (windowLayout == null) {
            return null;
        }
        return new android.graphics.Point(windowLayout.minWidth, windowLayout.minHeight);
    }

    long getLastWindowCreateTime() {
        com.android.server.wm.WindowState window = getWindow(alwaysTruePredicate());
        if (window != null && window.mAttrs.type != 1) {
            return window.getCreateTime();
        }
        return this.createTime;
    }

    private void adjustPictureInPictureParamsIfNeeded(android.graphics.Rect windowBounds) {
        if (this.pictureInPictureArgs != null && this.pictureInPictureArgs.hasSourceBoundsHint()) {
            this.pictureInPictureArgs.getSourceRectHint().offset(windowBounds.left, windowBounds.top);
        }
    }

    private void applyLocaleOverrideIfNeeded(android.content.res.Configuration resolvedConfig) {
        com.android.server.wm.ActivityTaskManagerInternal.PackageConfig appConfig;
        boolean differentPackage = false;
        boolean shouldAlignLocale = isEmbedded() || (this.task != null && this.task.mAlignActivityLocaleWithTask);
        if (!shouldAlignLocale) {
            return;
        }
        if (this.task != null && this.task.realActivity != null && !this.task.realActivity.getPackageName().equals(this.packageName)) {
            differentPackage = true;
        }
        if (differentPackage && (appConfig = this.mAtmService.mPackageConfigPersister.findPackageConfiguration(this.task.realActivity.getPackageName(), this.mUserId)) != null && appConfig.mLocales != null && !appConfig.mLocales.isEmpty()) {
            resolvedConfig.setLocales(appConfig.mLocales);
        }
    }

    boolean shouldSendCompatFakeFocus() {
        return this.mLetterboxUiController.shouldSendFakeFocus() && inMultiWindowMode() && !inPinnedWindowingMode() && !inFreeformWindowingMode();
    }

    boolean canCaptureSnapshot() {
        if (!isSurfaceShowing() || findMainWindow() == null) {
            return false;
        }
        return forAllWindows(new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.wm.ActivityRecord$$ExternalSyntheticLambda25
            public final boolean apply(java.lang.Object obj) {
                return com.android.server.wm.ActivityRecord.lambda$canCaptureSnapshot$29((com.android.server.wm.WindowState) obj);
            }
        }, true);
    }

    static /* synthetic */ boolean lambda$canCaptureSnapshot$29(com.android.server.wm.WindowState ws) {
        return ws.mWinAnimator != null && ws.mWinAnimator.getShown() && ws.mWinAnimator.mLastAlpha > 0.0f;
    }

    void overrideCustomTransition(boolean open, int enterAnim, int exitAnim, int backgroundColor) {
        com.android.server.wm.ActivityRecord.CustomAppTransition transition = getCustomAnimation(open);
        if (transition == null) {
            transition = new com.android.server.wm.ActivityRecord.CustomAppTransition();
            if (open) {
                this.mCustomOpenTransition = transition;
            } else {
                this.mCustomCloseTransition = transition;
            }
        }
        transition.mEnterAnim = enterAnim;
        transition.mExitAnim = exitAnim;
        transition.mBackgroundColor = backgroundColor;
    }

    void clearCustomTransition(boolean open) {
        if (open) {
            this.mCustomOpenTransition = null;
        } else {
            this.mCustomCloseTransition = null;
        }
    }

    com.android.server.wm.ActivityRecord.CustomAppTransition getCustomAnimation(boolean open) {
        return open ? this.mCustomOpenTransition : this.mCustomCloseTransition;
    }

    static class CustomAppTransition {
        int mBackgroundColor;
        int mEnterAnim;
        int mExitAnim;

        CustomAppTransition() {
        }
    }

    static class Builder {
        private android.content.pm.ActivityInfo mActivityInfo;
        private final com.android.server.wm.ActivityTaskManagerService mAtmService;
        private com.android.server.wm.WindowProcessController mCallerApp;
        private boolean mComponentSpecified;
        private android.content.res.Configuration mConfiguration;
        private long mCreateTime;
        private android.content.Intent mIntent;
        private java.lang.String mLaunchedFromFeature;
        private java.lang.String mLaunchedFromPackage;
        private int mLaunchedFromPid;
        private int mLaunchedFromUid;
        private android.app.ActivityOptions mOptions;
        private android.os.PersistableBundle mPersistentState;
        private int mRequestCode;
        private java.lang.String mResolvedType;
        private com.android.server.wm.ActivityRecord mResultTo;
        private java.lang.String mResultWho;
        private boolean mRootVoiceInteraction;
        private com.android.server.wm.ActivityRecord mSourceRecord;
        private android.app.ActivityManager.TaskDescription mTaskDescription;

        Builder(com.android.server.wm.ActivityTaskManagerService service) {
            this.mAtmService = service;
        }

        com.android.server.wm.ActivityRecord.Builder setCaller(com.android.server.wm.WindowProcessController caller) {
            this.mCallerApp = caller;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setLaunchedFromPid(int pid) {
            this.mLaunchedFromPid = pid;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setLaunchedFromUid(int uid) {
            this.mLaunchedFromUid = uid;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setLaunchedFromPackage(java.lang.String fromPackage) {
            this.mLaunchedFromPackage = fromPackage;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setLaunchedFromFeature(java.lang.String fromFeature) {
            this.mLaunchedFromFeature = fromFeature;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setIntent(android.content.Intent intent) {
            this.mIntent = intent;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setResolvedType(java.lang.String resolvedType) {
            this.mResolvedType = resolvedType;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setActivityInfo(android.content.pm.ActivityInfo activityInfo) {
            this.mActivityInfo = activityInfo;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setResultTo(com.android.server.wm.ActivityRecord resultTo) {
            this.mResultTo = resultTo;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setResultWho(java.lang.String resultWho) {
            this.mResultWho = resultWho;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setRequestCode(int reqCode) {
            this.mRequestCode = reqCode;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setComponentSpecified(boolean componentSpecified) {
            this.mComponentSpecified = componentSpecified;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setRootVoiceInteraction(boolean rootVoiceInteraction) {
            this.mRootVoiceInteraction = rootVoiceInteraction;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setActivityOptions(android.app.ActivityOptions options) {
            this.mOptions = options;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setConfiguration(android.content.res.Configuration config) {
            this.mConfiguration = config;
            return this;
        }

        com.android.server.wm.ActivityRecord.Builder setSourceRecord(com.android.server.wm.ActivityRecord source) {
            this.mSourceRecord = source;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.ActivityRecord.Builder setPersistentState(android.os.PersistableBundle persistentState) {
            this.mPersistentState = persistentState;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.ActivityRecord.Builder setTaskDescription(android.app.ActivityManager.TaskDescription taskDescription) {
            this.mTaskDescription = taskDescription;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.ActivityRecord.Builder setCreateTime(long createTime) {
            this.mCreateTime = createTime;
            return this;
        }

        com.android.server.wm.ActivityRecord build() {
            if (this.mConfiguration == null) {
                this.mConfiguration = this.mAtmService.getConfiguration();
            }
            return new com.android.server.wm.ActivityRecord(this.mAtmService, this.mCallerApp, this.mLaunchedFromPid, this.mLaunchedFromUid, this.mLaunchedFromPackage, this.mLaunchedFromFeature, this.mIntent, this.mResolvedType, this.mActivityInfo, this.mConfiguration, this.mResultTo, this.mResultWho, this.mRequestCode, this.mComponentSpecified, this.mRootVoiceInteraction, this.mAtmService.mTaskSupervisor, this.mOptions, this.mSourceRecord, this.mPersistentState, this.mTaskDescription, this.mCreateTime);
        }
    }

    public com.android.server.wm.IActivityRecordWrapper getWrapper() {
        return this.mWrapper;
    }

    private class ActivityRecordWrapper implements com.android.server.wm.IActivityRecordWrapper {
        private ActivityRecordWrapper() {
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public java.lang.String getLaunchedFromPackage() {
            return com.android.server.wm.ActivityRecord.this.launchedFromPackage;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public java.lang.String getPackageName() {
            return com.android.server.wm.ActivityRecord.this.packageName;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getLaunchedFromPid() {
            return com.android.server.wm.ActivityRecord.this.launchedFromPid;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getLaunchedFromUid() {
            return com.android.server.wm.ActivityRecord.this.launchedFromUid;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public android.content.pm.ApplicationInfo getAppliationInfo() {
            return com.android.server.wm.ActivityRecord.this.info.applicationInfo;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public android.content.Intent getIntent() {
            return com.android.server.wm.ActivityRecord.this.intent;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public com.android.server.wm.ActivityRecord getAppToken() {
            return com.android.server.wm.ActivityRecord.this;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public java.lang.String getshortComponentName() {
            return com.android.server.wm.ActivityRecord.this.shortComponentName;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public boolean isActivityTypeHome() {
            return com.android.server.wm.ActivityRecord.this.isActivityTypeHome();
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getResultToUserId() {
            return com.android.server.wm.ActivityRecord.this.resultTo.mUserId;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public java.lang.String getResultToPackageName() {
            return com.android.server.wm.ActivityRecord.this.resultTo.packageName;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public java.lang.String getProcessName() {
            return com.android.server.wm.ActivityRecord.this.processName;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public com.android.server.wm.IActivityRecordExt getExtImpl() {
            return com.android.server.wm.ActivityRecord.this.mActivityRecordExt;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public android.util.MergedConfiguration getLastReportedConfiguration() {
            return com.android.server.wm.ActivityRecord.this.mLastReportedConfiguration;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getConfigurationChanges(android.content.res.Configuration changesConfig) {
            return com.android.server.wm.ActivityRecord.this.getConfigurationChanges(changesConfig);
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public boolean shouldRelaunchLocked(int changes, android.content.res.Configuration changesConfig) {
            return com.android.server.wm.ActivityRecord.this.shouldRelaunchLocked(changes, changesConfig);
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public android.window.RemoteTransition getPendingRemoteTransition() {
            return com.android.server.wm.ActivityRecord.this.mPendingRemoteTransition;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getPid() {
            return com.android.server.wm.ActivityRecord.this.getPid();
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getUid() {
            return com.android.server.wm.ActivityRecord.this.getUid();
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public long getLaunchTickTime() {
            return com.android.server.wm.ActivityRecord.this.launchTickTime;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public boolean isNowVisible() {
            return com.android.server.wm.ActivityRecord.this.nowVisible;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public int getStartingWindowType(boolean newTask, boolean taskSwitch, boolean processRunning, boolean allowTaskSnapshot, boolean activityCreated, boolean activityAllDrawn, android.window.TaskSnapshot snapshot) {
            return com.android.server.wm.ActivityRecord.this.getStartingWindowType(newTask, taskSwitch, processRunning, allowTaskSnapshot, activityCreated, activityAllDrawn, snapshot);
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public void setLaunchSourceType(int type) {
            com.android.server.wm.ActivityRecord.this.mLaunchSourceType = type;
        }

        @Override // com.android.server.wm.IActivityRecordWrapper
        public boolean isOccludeParent() {
            return com.android.server.wm.ActivityRecord.this.mOccludesParent;
        }
    }
}
