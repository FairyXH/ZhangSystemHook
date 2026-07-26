package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class WindowManagerService extends android.view.IWindowManager.Stub implements com.android.server.Watchdog.Monitor, com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs {
    private static final int ANIMATION_COMPLETED_TIMEOUT_MS = 5000;
    private static final int ANIMATION_DURATION_SCALE = 2;
    private static final int BOOT_ANIMATION_POLL_INTERVAL = 50;
    private static final java.lang.String BOOT_ANIMATION_SERVICE = "bootanim";
    private static final java.lang.String DENSITY_OVERRIDE = "ro.config.density_override";
    private static final java.lang.String ENABLE_SHELL_TRANSITIONS = "persist.wm.debug.shell_transit";
    private static final int INPUT_DEVICES_READY_FOR_SAFE_MODE_DETECTION_TIMEOUT_MILLIS = 1000;
    static final int LAST_ANR_LIFETIME_DURATION_MSECS = 7200000;
    static final int LAYOUT_REPEAT_THRESHOLD = 4;
    static final int LOGTAG_INPUT_FOCUS = 62001;
    static final int MAX_ANIMATION_DURATION = 10000;
    static final boolean PROFILE_ORIENTATION = false;
    private static final java.lang.String PROPERTY_EMULATOR_CIRCULAR = "ro.boot.emulator.circular";
    private static final java.lang.String SIZE_OVERRIDE = "ro.config.size_override";
    private static final int SYNC_INPUT_TRANSACTIONS_TIMEOUT_MS = 5000;
    private static final java.lang.String SYSTEM_DEBUGGABLE = "ro.debuggable";
    private static final java.lang.String SYSTEM_SECURE = "ro.secure";
    private static final java.lang.String TAG = "WindowManager";
    private static final int TRACE_MAX_SECTION_NAME_LENGTH = 127;
    private static final int TRANSITION_ANIMATION_SCALE = 1;
    static final int UPDATE_FOCUS_NORMAL = 0;
    static final int UPDATE_FOCUS_PLACING_SURFACES = 2;
    static final int UPDATE_FOCUS_REMOVING_FOCUS = 4;
    static final int UPDATE_FOCUS_WILL_ASSIGN_LAYERS = 1;
    static final int UPDATE_FOCUS_WILL_PLACE_SURFACES = 3;
    static final int WINDOWS_FREEZING_SCREENS_ACTIVE = 1;
    static final int WINDOWS_FREEZING_SCREENS_NONE = 0;
    static final int WINDOWS_FREEZING_SCREENS_TIMEOUT = 2;
    private static final int WINDOW_ANIMATION_SCALE = 0;
    static final int WINDOW_FREEZE_TIMEOUT_DURATION = 2000;
    final com.android.server.wm.AccessibilityController mAccessibilityController;
    final android.app.IActivityManager mActivityManager;
    final boolean mAllowAnimationsInLowPowerMode;
    final boolean mAllowBootMessages;
    boolean mAllowTheaterModeWakeFromLayout;
    final android.app.ActivityManagerInternal mAmInternal;
    private boolean mAnimationsDisabled;
    final com.android.server.wm.WindowAnimator mAnimator;
    final com.android.server.wm.AnrController mAnrController;
    final android.app.AppOpsManager mAppOps;
    final boolean mAssistantOnTopOfDream;
    final com.android.server.wm.ActivityTaskManagerService mAtmService;
    final com.android.server.wm.BlurController mBlurController;
    final int mConfigTypes;
    final com.android.server.wm.WindowManagerConstants mConstants;
    final android.content.Context mContext;
    int mCurrentUserId;
    final int mDecorTypes;
    boolean mDisableTransitionAnimation;
    private final com.android.server.wm.DisplayAreaPolicy.Provider mDisplayAreaPolicyProvider;
    private final com.android.server.wm.DisplayHashController mDisplayHashController;
    final android.hardware.display.DisplayManager mDisplayManager;
    final android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    final com.android.server.wm.DisplayWindowListenerController mDisplayNotificationController;
    boolean mDisplayReady;
    final com.android.server.wm.DisplayWindowSettings mDisplayWindowSettings;
    final com.android.server.wm.DisplayWindowSettingsProvider mDisplayWindowSettingsProvider;
    final com.android.server.wm.DragDropController mDragDropController;
    final long mDrawLockTimeoutMillis;
    final com.android.server.wm.EmbeddedWindowController mEmbeddedWindowController;
    com.android.server.wm.EmulatorDisplayOverlay mEmulatorDisplayOverlay;
    private int mEnterAnimId;
    private boolean mEventDispatchingEnabled;
    private int mExitAnimId;
    final com.android.server.wm.WindowManagerFlags mFlags;
    boolean mFocusMayChange;
    private com.android.server.wm.InputTarget mFocusedInputTarget;
    boolean mForceDesktopModeOnExternalDisplays;
    final com.android.server.wm.WindowManagerGlobalLock mGlobalLock;
    boolean mHardKeyboardAvailable;
    com.android.server.wm.WindowManagerInternal.OnHardKeyboardStatusChangeListener mHardKeyboardStatusChangeListener;
    private boolean mHasHdrSupport;
    final boolean mHasPermanentDpad;
    private boolean mHasWideColorGamutSupport;
    final com.android.server.wm.HighRefreshRateDenylist mHighRefreshRateDenylist;
    com.android.server.wm.ImeTargetChangeListener mImeTargetChangeListener;
    final com.android.server.input.InputManagerService mInputManager;
    boolean mIsFakeTouchDevice;
    private boolean mIsIgnoreOrientationRequestDisabled;
    boolean mIsPc;
    boolean mIsTouchDevice;
    private final com.android.server.wm.KeyguardDisableHandler mKeyguardDisableHandler;
    java.lang.String mLastANRState;
    final com.android.internal.util.LatencyTracker mLatencyTracker;
    final com.android.server.wm.LetterboxConfiguration mLetterboxConfiguration;
    final boolean mLimitedAlphaCompositing;
    final int mMaxUiWidth;
    com.android.server.wm.WindowManagerInternal.OnImeRequestedChangedListener mOnImeRequestedChangedListener;
    final int mOverrideConfigTypes;
    final int mOverrideDecorTypes;
    boolean mPerDisplayFocusEnabled;
    final android.content.pm.PackageManagerInternal mPmInternal;
    com.android.server.policy.WindowManagerPolicy mPolicy;
    final com.android.server.wm.PossibleDisplayInfoMapper mPossibleDisplayInfoMapper;
    android.os.PowerManager mPowerManager;
    android.os.PowerManagerInternal mPowerManagerInternal;
    private com.android.server.wm.RecentsAnimationController mRecentsAnimationController;
    final com.android.server.wm.RootWindowContainer mRoot;
    final com.android.server.wm.RotationWatcherController mRotationWatcherController;
    boolean mSafeMode;
    private final android.os.PowerManager.WakeLock mScreenFrozenLock;
    private final com.android.server.wm.ScreenRecordingCallbackController mScreenRecordingCallbackController;
    com.android.server.wm.WindowManagerService.SettingsObserver mSettingsObserver;
    boolean mSkipActivityRelaunchWhenDocking;
    final com.android.server.wm.SnapshotController mSnapshotController;
    final com.android.server.wm.StartingSurfaceController mStartingSurfaceController;
    com.android.server.wm.StrictModeFlash mStrictModeFlash;
    final boolean mSupportsHighPerfTransitions;
    com.android.server.wm.SurfaceAnimationRunner mSurfaceAnimationRunner;
    java.util.function.Function<android.view.SurfaceSession, android.view.SurfaceControl.Builder> mSurfaceControlFactory;
    final com.android.server.wm.BLASTSyncEngine mSyncEngine;
    android.window.SystemPerformanceHinter mSystemPerformanceHinter;
    final com.android.server.wm.TaskFpsCallbackController mTaskFpsCallbackController;
    final com.android.server.wm.TaskPositioningController mTaskPositioningController;
    final com.android.server.wm.TaskSnapshotController mTaskSnapshotController;
    final com.android.server.wm.TaskSystemBarsListenerController mTaskSystemBarsListenerController;
    private android.view.WindowContentFrameStats mTempWindowRenderStats;
    private final android.content.pm.TestUtilityService mTestUtilityService;
    private final android.view.SurfaceControl.Transaction mTransaction;
    java.util.function.Supplier<android.view.SurfaceControl.Transaction> mTransactionFactory;
    int mTransactionSequence;
    private float mTransitionAnimationScaleSetting;
    final com.android.server.wm.TransitionTracer mTransitionTracer;
    final com.android.server.pm.UserManagerInternal mUmInternal;
    private com.android.server.wm.ViewServer mViewServer;
    com.android.server.wm.Watermark mWatermark;
    private float mWindowAnimationScaleSetting;
    final com.android.server.wm.WindowSurfacePlacer mWindowPlacerLocked;
    final com.android.server.wm.WindowTracing mWindowTracing;
    static final int MY_PID = android.os.Process.myPid();
    static final int MY_UID = android.os.Process.myUid();
    public static final boolean sEnableShellTransitions = getShellTransitEnabled();
    static final boolean ENABLE_FIXED_ROTATION_TRANSFORM = android.os.SystemProperties.getBoolean("persist.wm.fixed_rotation_transform", true);
    static com.android.server.wm.WindowManagerThreadPriorityBooster sThreadPriorityBooster = new com.android.server.wm.WindowManagerThreadPriorityBooster();
    private final android.os.RemoteCallbackList<com.android.internal.policy.IKeyguardLockedStateListener> mKeyguardLockedStateListeners = new android.os.RemoteCallbackList<>();
    private final java.util.List<com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener> mOnWindowRemovedListeners = new java.util.ArrayList();
    private boolean mDispatchedKeyguardLockedState = false;
    private android.os.ITheiaManagerExt mTheiaManagerExt = (android.os.ITheiaManagerExt) system.ext.loader.core.ExtLoader.type(android.os.ITheiaManagerExt.class).create();
    int mVr2dDisplayId = -1;
    boolean mVrModeEnabled = false;
    final java.util.Map<android.os.IBinder, com.android.internal.policy.KeyInterceptionInfo> mKeyInterceptionInfoForToken = java.util.Collections.synchronizedMap(new android.util.ArrayMap());
    private final android.service.vr.IVrStateCallbacks mVrStateCallbacks = new com.android.server.wm.WindowManagerService.AnonymousClass1();
    private final android.content.BroadcastReceiver mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.wm.WindowManagerService.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            byte b;
            java.lang.String action = intent.getAction();
            switch (action.hashCode()) {
                case 988075300:
                    if (action.equals("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED")) {
                        b = 0;
                        break;
                    }
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    com.android.server.wm.WindowManagerService.this.mKeyguardDisableHandler.updateKeyguardEnabled(getSendingUserId());
                    break;
            }
        }
    };
    private final com.android.server.utils.PriorityDump.PriorityDumper mPriorityDumper = new com.android.server.utils.PriorityDump.PriorityDumper() { // from class: com.android.server.wm.WindowManagerService.3
        @Override // com.android.server.utils.PriorityDump.PriorityDumper
        public void dumpCritical(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
            com.android.server.wm.WindowManagerService.this.doDump(fd, pw, new java.lang.String[]{"-a"}, asProto);
        }

        @Override // com.android.server.utils.PriorityDump.PriorityDumper
        public void dumpHigh(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
            if (asProto) {
                return;
            }
            com.android.server.wm.WindowManagerService.this.mAtmService.dumpActivity(fd, pw, "all", new java.lang.String[0], 0, true, true, false, -1, -1, 1000L);
            com.android.server.wm.WindowManagerService.this.dumpVisibleWindowClients(fd, pw, 1000L);
        }

        @Override // com.android.server.utils.PriorityDump.PriorityDumper
        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
            com.android.server.wm.WindowManagerService.this.doDump(fd, pw, args, asProto);
        }
    };
    boolean mShowAlertWindowNotifications = true;
    final android.util.ArraySet<com.android.server.wm.Session> mSessions = new android.util.ArraySet<>();
    final java.util.HashMap<android.os.IBinder, com.android.server.wm.WindowState> mWindowMap = new java.util.HashMap<>();
    final java.util.HashMap<android.os.IBinder, com.android.server.wm.WindowState> mInputToWindowMap = new java.util.HashMap<>();
    final java.util.ArrayList<com.android.server.wm.WindowState> mResizingWindows = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.wm.WindowState> mFrameChangingWindows = new java.util.ArrayList<>();
    volatile java.util.Map<java.lang.Integer, java.lang.Integer> mDisplayImePolicyCache = java.util.Collections.unmodifiableMap(new android.util.ArrayMap());
    final java.util.ArrayList<com.android.server.wm.WindowState> mDestroySurface = new java.util.ArrayList<>();
    final java.util.ArrayList<com.android.server.wm.WindowState> mForceRemoves = new java.util.ArrayList<>();
    final android.util.ArrayMap<com.android.server.wm.WindowContainer<?>, android.os.Message> mWaitingForDrawnCallbacks = new android.util.ArrayMap<>();
    private java.util.ArrayList<com.android.server.wm.WindowState> mHidingNonSystemOverlayWindows = new java.util.ArrayList<>();
    private final android.util.SparseIntArray mOrientationMapping = new android.util.SparseIntArray();
    final android.graphics.Rect mTmpRect = new android.graphics.Rect();
    boolean mDisplayEnabled = false;
    boolean mSystemBooted = false;
    boolean mForceDisplayEnabled = false;
    boolean mShowingBootMessages = false;
    boolean mSystemReady = false;
    boolean mBootAnimationStopped = false;
    long mBootWaitForWindowsStartTime = -1;
    public com.android.server.wm.IWindowManagerServiceSocExt mWindowManagerServiceSocExt = (com.android.server.wm.IWindowManagerServiceSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowManagerServiceSocExt.class).base(this).create();
    final com.android.server.wm.WallpaperVisibilityListeners mWallpaperVisibilityListeners = new com.android.server.wm.WallpaperVisibilityListeners();
    android.view.IDisplayChangeWindowController mDisplayChangeController = null;
    private final android.os.IBinder.DeathRecipient mDisplayChangeControllerDeath = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda20
        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            this.f$0.lambda$new$0();
        }
    };
    boolean mDisplayFrozen = false;
    long mDisplayFreezeTime = 0;
    int mLastDisplayFreezeDuration = 0;
    java.lang.Object mLastFinishedFreezeSource = null;
    boolean mSwitchingUser = false;
    int mWindowsFreezingScreen = 0;
    boolean mClientFreezingScreen = false;
    int mAppsFreezingScreen = 0;
    int mWindowsInsetsChanged = 0;
    final com.android.server.wm.WindowManagerService.H mH = new com.android.server.wm.WindowManagerService.H();
    final android.os.Handler mAnimationHandler = new android.os.Handler(com.android.server.AnimationThread.getHandler().getLooper());
    volatile float mMaximumObscuringOpacityForTouch = 0.8f;
    final com.android.server.wm.WindowContextListenerController mWindowContextListenerController = new com.android.server.wm.WindowContextListenerController();
    final com.android.server.wm.ContentRecordingController mContentRecordingController = new com.android.server.wm.ContentRecordingController();
    private final com.android.server.wm.SurfaceSyncGroupController mSurfaceSyncGroupController = new com.android.server.wm.SurfaceSyncGroupController();
    final com.android.server.wm.TrustedPresentationListenerController mTrustedPresentationListenerController = new com.android.server.wm.TrustedPresentationListenerController();
    private float mAnimatorDurationScaleSetting = 1.0f;
    boolean mPointerLocationEnabled = false;
    private int mFrozenDisplayId = -1;
    final android.util.ArrayMap<com.android.server.wm.AnimationAdapter, com.android.server.wm.SurfaceAnimator> mAnimationTransferMap = new android.util.ArrayMap<>();
    final java.util.ArrayList<com.android.server.wm.WindowManagerService.WindowChangeListener> mWindowChangeListeners = new java.util.ArrayList<>();
    boolean mWindowsChanged = false;
    private long mSensitiveContentProtectionSessionId = 0;
    final com.android.server.wm.SensitiveContentPackages mSensitiveContentPackages = new com.android.server.wm.SensitiveContentPackages();
    private final android.util.IntArray mCaptureBlockedToastShownUids = new android.util.IntArray();
    final com.android.server.wm.WindowManagerInternal.AppTransitionListener mActivityManagerAppTransitionNotifier = new com.android.server.wm.WindowManagerInternal.AppTransitionListener() { // from class: com.android.server.wm.WindowManagerService.4
        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionCancelledLocked(boolean keyguardGoingAwayCancelled) {
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionFinishedLocked(android.os.IBinder token) {
            com.android.server.wm.ActivityRecord atoken = com.android.server.wm.ActivityRecord.forTokenLocked(token);
            if (atoken == null) {
                return;
            }
            if (atoken.mLaunchTaskBehind && !com.android.server.wm.WindowManagerService.this.isRecentsAnimationTarget(atoken) && !com.android.server.wm.WindowManagerService.this.mWindowManagerServiceExt.isGestureAnimationTarget(atoken)) {
                com.android.server.wm.WindowManagerService.this.mAtmService.mTaskSupervisor.scheduleLaunchTaskBehindComplete(atoken.token);
                atoken.mLaunchTaskBehind = false;
                return;
            }
            atoken.updateReportedVisibilityLocked();
            if (atoken.mEnteringAnimation && !com.android.server.wm.WindowManagerService.this.isRecentsAnimationTarget(atoken)) {
                atoken.mEnteringAnimation = false;
                if (atoken.attachedToProcess()) {
                    try {
                        com.android.server.wm.WindowManagerService.this.mWindowManagerServiceExt.handleAppVisible(atoken);
                        atoken.app.getThread().scheduleEnterAnimationComplete(atoken.token);
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
        }
    };
    final java.util.ArrayList<com.android.server.wm.WindowManagerService.AppFreezeListener> mAppFreezeListeners = new java.util.ArrayList<>();
    private volatile boolean mDisableSecureWindows = false;
    final com.android.server.wm.InputManagerCallback mInputManagerCallback = new com.android.server.wm.InputManagerCallback(this);
    private com.android.server.wm.IWindowManagerServiceWrapper mWrapper = new com.android.server.wm.WindowManagerService.WindowManagerServiceWrapper();
    private com.android.server.wm.IWindowManagerServiceExt mWindowManagerServiceExt = (com.android.server.wm.IWindowManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowManagerServiceExt.class).create();

    interface AppFreezeListener {
        void onAppFreezeTimeout();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface UpdateAnimationScaleMode {
    }

    public interface WindowChangeListener {
        void focusChanged();

        void windowsChanged();
    }

    /* JADX INFO: renamed from: com.android.server.wm.WindowManagerService$1, reason: invalid class name */
    class AnonymousClass1 extends android.service.vr.IVrStateCallbacks.Stub {
        AnonymousClass1() {
        }

        public void onVrStateChanged(final boolean enabled) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mVrModeEnabled = enabled;
                    com.android.server.wm.WindowManagerService.this.mRoot.forAllDisplayPolicies(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$1$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.wm.DisplayPolicy) obj).onVrStateChangedLw(enabled);
                        }
                    });
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mDisplayChangeController = null;
    }

    final class SettingsObserver extends android.database.ContentObserver {
        private final android.net.Uri mAnimationDurationScaleUri;
        private final android.net.Uri mDevEnableNonResizableMultiWindowUri;
        private final android.net.Uri mDisableSecureWindowsUri;
        private final android.net.Uri mDisplayInversionEnabledUri;
        private final android.net.Uri mDisplaySettingsPathUri;
        private final android.net.Uri mForceDesktopModeOnExternalDisplaysUri;
        private final android.net.Uri mForceResizableUri;
        private final android.net.Uri mFreeformWindowUri;
        private final android.net.Uri mImmersiveModeConfirmationsUri;
        private final android.net.Uri mMaximumObscuringOpacityForTouchUri;
        private final android.net.Uri mPolicyControlUri;
        private final android.net.Uri mTransitionAnimationScaleUri;
        private final android.net.Uri mWindowAnimationScaleUri;

        public SettingsObserver() {
            super(new android.os.Handler());
            this.mDisplayInversionEnabledUri = android.provider.Settings.Secure.getUriFor("accessibility_display_inversion_enabled");
            this.mWindowAnimationScaleUri = android.provider.Settings.Global.getUriFor("window_animation_scale");
            this.mTransitionAnimationScaleUri = android.provider.Settings.Global.getUriFor("transition_animation_scale");
            this.mAnimationDurationScaleUri = android.provider.Settings.Global.getUriFor("animator_duration_scale");
            this.mImmersiveModeConfirmationsUri = android.provider.Settings.Secure.getUriFor("immersive_mode_confirmations");
            this.mDisableSecureWindowsUri = android.provider.Settings.Secure.getUriFor("disable_secure_windows");
            this.mPolicyControlUri = android.provider.Settings.Global.getUriFor("policy_control");
            this.mForceDesktopModeOnExternalDisplaysUri = android.provider.Settings.Global.getUriFor("force_desktop_mode_on_external_displays");
            this.mFreeformWindowUri = android.provider.Settings.Global.getUriFor("enable_freeform_support");
            this.mForceResizableUri = android.provider.Settings.Global.getUriFor("force_resizable_activities");
            this.mDevEnableNonResizableMultiWindowUri = android.provider.Settings.Global.getUriFor("enable_non_resizable_multi_window");
            this.mDisplaySettingsPathUri = android.provider.Settings.Global.getUriFor("wm_display_settings_path");
            this.mMaximumObscuringOpacityForTouchUri = android.provider.Settings.Global.getUriFor("maximum_obscuring_opacity_for_touch");
            android.content.ContentResolver resolver = com.android.server.wm.WindowManagerService.this.mContext.getContentResolver();
            resolver.registerContentObserver(this.mDisplayInversionEnabledUri, false, this, -1);
            resolver.registerContentObserver(this.mWindowAnimationScaleUri, false, this, -1);
            resolver.registerContentObserver(this.mTransitionAnimationScaleUri, false, this, -1);
            resolver.registerContentObserver(this.mAnimationDurationScaleUri, false, this, -1);
            resolver.registerContentObserver(this.mImmersiveModeConfirmationsUri, false, this, -1);
            resolver.registerContentObserver(this.mDisableSecureWindowsUri, false, this, -1);
            resolver.registerContentObserver(this.mPolicyControlUri, false, this, -1);
            resolver.registerContentObserver(this.mForceDesktopModeOnExternalDisplaysUri, false, this, -1);
            resolver.registerContentObserver(this.mFreeformWindowUri, false, this, -1);
            resolver.registerContentObserver(this.mForceResizableUri, false, this, -1);
            resolver.registerContentObserver(this.mDevEnableNonResizableMultiWindowUri, false, this, -1);
            resolver.registerContentObserver(this.mDisplaySettingsPathUri, false, this, -1);
            resolver.registerContentObserver(this.mMaximumObscuringOpacityForTouchUri, false, this, -1);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            int mode;
            if (uri == null) {
                return;
            }
            if (this.mImmersiveModeConfirmationsUri.equals(uri) || this.mPolicyControlUri.equals(uri)) {
                updateSystemUiSettings(true);
                return;
            }
            if (this.mForceDesktopModeOnExternalDisplaysUri.equals(uri)) {
                updateForceDesktopModeOnExternalDisplays();
                return;
            }
            if (this.mFreeformWindowUri.equals(uri)) {
                updateFreeformWindowManagement();
                return;
            }
            if (this.mForceResizableUri.equals(uri)) {
                updateForceResizableTasks();
                return;
            }
            if (this.mDevEnableNonResizableMultiWindowUri.equals(uri)) {
                updateDevEnableNonResizableMultiWindow();
                return;
            }
            if (this.mDisplaySettingsPathUri.equals(uri)) {
                updateDisplaySettingsLocation();
                return;
            }
            if (this.mMaximumObscuringOpacityForTouchUri.equals(uri)) {
                updateMaximumObscuringOpacityForTouch();
                return;
            }
            if (this.mDisableSecureWindowsUri.equals(uri)) {
                updateDisableSecureWindows();
                return;
            }
            if (this.mWindowAnimationScaleUri.equals(uri)) {
                mode = 0;
            } else if (this.mTransitionAnimationScaleUri.equals(uri)) {
                mode = 1;
            } else if (this.mAnimationDurationScaleUri.equals(uri)) {
                mode = 2;
            } else {
                return;
            }
            android.os.Message m = com.android.server.wm.WindowManagerService.this.mH.obtainMessage(51, mode, 0);
            com.android.server.wm.WindowManagerService.this.mH.sendMessage(m);
        }

        void loadSettings() {
            updateSystemUiSettings(false);
            updateMaximumObscuringOpacityForTouch();
            updateDisableSecureWindows();
        }

        void updateMaximumObscuringOpacityForTouch() {
            android.content.ContentResolver resolver = com.android.server.wm.WindowManagerService.this.mContext.getContentResolver();
            com.android.server.wm.WindowManagerService.this.mMaximumObscuringOpacityForTouch = android.provider.Settings.Global.getFloat(resolver, "maximum_obscuring_opacity_for_touch", 0.8f);
            if (com.android.server.wm.WindowManagerService.this.mMaximumObscuringOpacityForTouch < 0.0f || com.android.server.wm.WindowManagerService.this.mMaximumObscuringOpacityForTouch > 1.0f) {
                com.android.server.wm.WindowManagerService.this.mMaximumObscuringOpacityForTouch = 0.8f;
            }
        }

        void updateSystemUiSettings(boolean handleChange) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                boolean changed = false;
                try {
                    if (handleChange) {
                        changed = com.android.server.wm.WindowManagerService.this.getDefaultDisplayContentLocked().getDisplayPolicy().onSystemUiSettingsChanged();
                    } else {
                        com.android.server.wm.ImmersiveModeConfirmation.loadSetting(com.android.server.wm.WindowManagerService.this.mCurrentUserId, com.android.server.wm.WindowManagerService.this.mContext);
                    }
                    if (changed) {
                        com.android.server.wm.WindowManagerService.this.mWindowPlacerLocked.requestTraversal();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        void updateForceDesktopModeOnExternalDisplays() {
            android.content.ContentResolver resolver = com.android.server.wm.WindowManagerService.this.mContext.getContentResolver();
            boolean enableForceDesktopMode = android.provider.Settings.Global.getInt(resolver, "force_desktop_mode_on_external_displays", 0) != 0;
            if (com.android.server.wm.WindowManagerService.this.mForceDesktopModeOnExternalDisplays == enableForceDesktopMode) {
                return;
            }
            com.android.server.wm.WindowManagerService.this.setForceDesktopModeOnExternalDisplays(enableForceDesktopMode);
        }

        /* JADX WARN: Removed duplicated region for block: B:6:0x0021  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        void updateFreeformWindowManagement() {
            /*
                r4 = this;
                com.android.server.wm.WindowManagerService r0 = com.android.server.wm.WindowManagerService.this
                android.content.Context r0 = r0.mContext
                android.content.ContentResolver r0 = r0.getContentResolver()
                com.android.server.wm.WindowManagerService r1 = com.android.server.wm.WindowManagerService.this
                android.content.Context r1 = r1.mContext
                android.content.pm.PackageManager r1 = r1.getPackageManager()
                java.lang.String r2 = "android.software.freeform_window_management"
                boolean r1 = r1.hasSystemFeature(r2)
                if (r1 != 0) goto L21
                java.lang.String r1 = "enable_freeform_support"
                r2 = 0
                int r1 = android.provider.Settings.Global.getInt(r0, r1, r2)
                if (r1 == 0) goto L22
            L21:
                r2 = 1
            L22:
                r1 = r2
                com.android.server.wm.WindowManagerService r2 = com.android.server.wm.WindowManagerService.this
                com.android.server.wm.ActivityTaskManagerService r2 = r2.mAtmService
                boolean r2 = r2.mSupportsFreeformWindowManagement
                if (r2 == r1) goto L4b
                com.android.server.wm.WindowManagerService r2 = com.android.server.wm.WindowManagerService.this
                com.android.server.wm.ActivityTaskManagerService r2 = r2.mAtmService
                r2.mSupportsFreeformWindowManagement = r1
                com.android.server.wm.WindowManagerService r2 = com.android.server.wm.WindowManagerService.this
                com.android.server.wm.WindowManagerGlobalLock r2 = r2.mGlobalLock
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection()
                monitor-enter(r2)
                com.android.server.wm.WindowManagerService r3 = com.android.server.wm.WindowManagerService.this     // Catch: java.lang.Throwable -> L45
                com.android.server.wm.RootWindowContainer r3 = r3.mRoot     // Catch: java.lang.Throwable -> L45
                r3.onSettingsRetrieved()     // Catch: java.lang.Throwable -> L45
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L45
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
                goto L4b
            L45:
                r3 = move-exception
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L45
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
                throw r3
            L4b:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerService.SettingsObserver.updateFreeformWindowManagement():void");
        }

        void updateForceResizableTasks() {
            android.content.ContentResolver resolver = com.android.server.wm.WindowManagerService.this.mContext.getContentResolver();
            boolean forceResizable = android.provider.Settings.Global.getInt(resolver, "force_resizable_activities", 0) != 0;
            com.android.server.wm.WindowManagerService.this.mAtmService.mForceResizableActivities = forceResizable;
        }

        void updateDevEnableNonResizableMultiWindow() {
            android.content.ContentResolver resolver = com.android.server.wm.WindowManagerService.this.mContext.getContentResolver();
            boolean devEnableNonResizableMultiWindow = android.provider.Settings.Global.getInt(resolver, "enable_non_resizable_multi_window", 0) != 0;
            com.android.server.wm.WindowManagerService.this.mAtmService.mDevEnableNonResizableMultiWindow = devEnableNonResizableMultiWindow;
        }

        void updateDisplaySettingsLocation() {
            android.content.ContentResolver resolver = com.android.server.wm.WindowManagerService.this.mContext.getContentResolver();
            java.lang.String filePath = android.provider.Settings.Global.getString(resolver, "wm_display_settings_path");
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mDisplayWindowSettingsProvider.setBaseSettingsFilePath(filePath);
                    com.android.server.wm.WindowManagerService.this.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$SettingsObserver$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$updateDisplaySettingsLocation$0((com.android.server.wm.DisplayContent) obj);
                        }
                    });
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateDisplaySettingsLocation$0(com.android.server.wm.DisplayContent display) {
            com.android.server.wm.WindowManagerService.this.mDisplayWindowSettings.applySettingsToDisplayLocked(display);
            display.reconfigureDisplayLocked();
        }

        void updateDisableSecureWindows() {
            if (!android.os.SystemProperties.getBoolean(com.android.server.wm.WindowManagerService.SYSTEM_DEBUGGABLE, false)) {
                return;
            }
            try {
                boolean disableSecureWindows = android.provider.Settings.Secure.getIntForUser(com.android.server.wm.WindowManagerService.this.mContext.getContentResolver(), "disable_secure_windows", 0) != 0;
                if (com.android.server.wm.WindowManagerService.this.mDisableSecureWindows == disableSecureWindows) {
                    return;
                }
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        com.android.server.wm.WindowManagerService.this.mDisableSecureWindows = disableSecureWindows;
                        com.android.server.wm.WindowManagerService.this.mRoot.refreshSecureSurfaceState();
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (android.provider.Settings.SettingNotFoundException e) {
            }
        }
    }

    public static void boostPriorityForLockedSection() {
        sThreadPriorityBooster.boost();
    }

    public static void resetPriorityAfterLockedSection() {
        sThreadPriorityBooster.reset();
    }

    public static com.android.server.wm.WindowManagerService main(android.content.Context context, com.android.server.input.InputManagerService im, boolean showBootMsgs, com.android.server.policy.WindowManagerPolicy policy, com.android.server.wm.ActivityTaskManagerService atm) {
        com.android.server.wm.WindowManagerService wms = main(context, im, showBootMsgs, policy, atm, new com.android.server.wm.DisplayWindowSettingsProvider(), new java.util.function.Supplier() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda18
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return new android.view.SurfaceControl.Transaction();
            }
        }, new java.util.function.Function() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda19
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return new android.view.SurfaceControl.Builder((android.view.SurfaceSession) obj);
            }
        });
        android.view.WindowManagerGlobal.setWindowManagerServiceForSystemProcess(wms);
        return wms;
    }

    public static com.android.server.wm.WindowManagerService main(final android.content.Context context, final com.android.server.input.InputManagerService im, final boolean showBootMsgs, final com.android.server.policy.WindowManagerPolicy policy, final com.android.server.wm.ActivityTaskManagerService atm, final com.android.server.wm.DisplayWindowSettingsProvider displayWindowSettingsProvider, final java.util.function.Supplier<android.view.SurfaceControl.Transaction> transactionFactory, final java.util.function.Function<android.view.SurfaceSession, android.view.SurfaceControl.Builder> surfaceControlFactory) {
        final com.android.server.wm.WindowManagerService[] wms = new com.android.server.wm.WindowManagerService[1];
        com.android.server.DisplayThread.getHandler().runWithScissors(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.wm.WindowManagerService.lambda$main$1(wms, context, im, showBootMsgs, policy, atm, displayWindowSettingsProvider, transactionFactory, surfaceControlFactory);
            }
        }, 0L);
        return wms[0];
    }

    static /* synthetic */ void lambda$main$1(com.android.server.wm.WindowManagerService[] wms, android.content.Context context, com.android.server.input.InputManagerService im, boolean showBootMsgs, com.android.server.policy.WindowManagerPolicy policy, com.android.server.wm.ActivityTaskManagerService atm, com.android.server.wm.DisplayWindowSettingsProvider displayWindowSettingsProvider, java.util.function.Supplier transactionFactory, java.util.function.Function surfaceControlFactory) {
        wms[0] = ((com.android.server.wm.IWindowManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowManagerServiceExt.class).create()).getOplusWindowManagerService(context, im, showBootMsgs, policy, atm, displayWindowSettingsProvider, transactionFactory, surfaceControlFactory);
    }

    private void initPolicy() {
        com.android.server.UiThread.getHandler().runWithScissors(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService.5
            @Override // java.lang.Runnable
            public void run() {
                com.android.internal.view.WindowManagerPolicyThread.set(java.lang.Thread.currentThread(), android.os.Looper.myLooper());
                com.android.server.wm.WindowManagerService.this.mPolicy.init(com.android.server.wm.WindowManagerService.this.mContext, com.android.server.wm.WindowManagerService.this);
            }
        }, 0L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver result) {
        new com.android.server.wm.WindowManagerShellCommand(this).exec(this, in, out, err, args, callback, result);
    }

    /* JADX WARN: Multi-variable type inference failed */
    WindowManagerService(android.content.Context context, com.android.server.input.InputManagerService inputManagerService, boolean z, com.android.server.policy.WindowManagerPolicy windowManagerPolicy, com.android.server.wm.ActivityTaskManagerService activityTaskManagerService, com.android.server.wm.DisplayWindowSettingsProvider displayWindowSettingsProvider, java.util.function.Supplier<android.view.SurfaceControl.Transaction> supplier, java.util.function.Function<android.view.SurfaceSession, android.view.SurfaceControl.Builder> function) {
        java.lang.Object[] objArr;
        java.lang.Object[] objArr2 = 0;
        java.lang.Object[] objArr3 = 0;
        this.mWindowAnimationScaleSetting = 1.0f;
        this.mTransitionAnimationScaleSetting = 1.0f;
        this.mAnimationsDisabled = false;
        com.android.server.LockGuard.installLock(this, 5);
        this.mGlobalLock = activityTaskManagerService.getGlobalLock();
        this.mAtmService = activityTaskManagerService;
        this.mContext = context;
        this.mFlags = new com.android.server.wm.WindowManagerFlags();
        this.mIsPc = this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.pc");
        this.mAllowBootMessages = z;
        this.mLimitedAlphaCompositing = context.getResources().getBoolean(android.R.bool.config_quickSettingsSupported);
        this.mHasPermanentDpad = context.getResources().getBoolean(android.R.bool.config_fillMainBuiltInDisplayCutout);
        this.mDrawLockTimeoutMillis = context.getResources().getInteger(android.R.integer.config_displayWhiteBalanceTransitionTimeIncrease);
        this.mAllowAnimationsInLowPowerMode = context.getResources().getBoolean(android.R.bool.config_allowAnimationsInLowPowerMode);
        this.mMaxUiWidth = context.getResources().getInteger(android.R.integer.config_longPressOnBackBehavior);
        this.mSupportsHighPerfTransitions = context.getResources().getBoolean(android.R.bool.config_deviceSupportsWifiUsd);
        this.mDisableTransitionAnimation = context.getResources().getBoolean(android.R.bool.config_disableTaskSnapshots);
        this.mPerDisplayFocusEnabled = context.getResources().getBoolean(android.R.bool.config_perDisplayFocusEnabled);
        this.mAssistantOnTopOfDream = context.getResources().getBoolean(android.R.bool.config_assistantOnTopOfDream);
        this.mSkipActivityRelaunchWhenDocking = context.getResources().getBoolean(android.R.bool.config_satellite_modem_support_concurrent_tn_scanning);
        if (!context.getResources().getBoolean(android.R.bool.config_defaultAdasGnssLocationEnabled) || !this.mFlags.mAllowsScreenSizeDecoupledFromStatusBarAndCutout) {
            objArr = false;
        } else {
            objArr = true;
        }
        java.lang.Object[] objArr4 = objArr;
        if (!this.mFlags.mInsetsDecoupledConfiguration) {
            this.mDecorTypes = android.view.WindowInsets.Type.displayCutout() | android.view.WindowInsets.Type.navigationBars();
            this.mConfigTypes = android.view.WindowInsets.Type.displayCutout() | android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars();
        } else {
            this.mDecorTypes = 0;
            this.mConfigTypes = 0;
        }
        if (objArr4 == false || this.mFlags.mInsetsDecoupledConfiguration) {
            this.mOverrideConfigTypes = android.view.WindowInsets.Type.displayCutout() | android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars();
            this.mOverrideDecorTypes = android.view.WindowInsets.Type.displayCutout() | android.view.WindowInsets.Type.navigationBars();
        } else {
            this.mOverrideConfigTypes = 0;
            this.mOverrideDecorTypes = 0;
        }
        this.mLetterboxConfiguration = new com.android.server.wm.LetterboxConfiguration(android.app.ActivityThread.currentActivityThread().getSystemUiContext());
        this.mInputManager = inputManagerService;
        this.mDisplayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        this.mPossibleDisplayInfoMapper = new com.android.server.wm.PossibleDisplayInfoMapper(this.mDisplayManagerInternal);
        this.mSurfaceControlFactory = function;
        this.mTransactionFactory = supplier;
        this.mTransaction = this.mTransactionFactory.get();
        this.mPolicy = windowManagerPolicy;
        this.mAnimator = new com.android.server.wm.WindowAnimator(this);
        this.mRoot = new com.android.server.wm.RootWindowContainer(this);
        android.content.ContentResolver contentResolver = context.getContentResolver();
        this.mSyncEngine = new com.android.server.wm.BLASTSyncEngine(this);
        this.mWindowPlacerLocked = new com.android.server.wm.WindowSurfacePlacer(this);
        this.mSnapshotController = new com.android.server.wm.SnapshotController(this);
        this.mTaskSnapshotController = this.mSnapshotController.mTaskSnapshotController;
        this.mWindowTracing = com.android.server.wm.WindowTracing.createDefaultAndStartLooper(this, android.view.Choreographer.getInstance());
        if (android.tracing.Flags.perfettoTransitionTracing()) {
            this.mTransitionTracer = new com.android.server.wm.PerfettoTransitionTracer();
        } else {
            this.mTransitionTracer = new com.android.server.wm.LegacyTransitionTracer();
        }
        getWrapper().getExtImpl().initOplusWindowTracing(this, android.view.Choreographer.getInstance());
        com.android.server.LocalServices.addService(com.android.server.policy.WindowManagerPolicy.class, this.mPolicy);
        this.mDisplayManager = (android.hardware.display.DisplayManager) context.getSystemService("display");
        this.mKeyguardDisableHandler = com.android.server.wm.KeyguardDisableHandler.create(this.mContext, this.mPolicy, this.mH);
        this.mPowerManager = (android.os.PowerManager) context.getSystemService("power");
        this.mPowerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
        if (this.mPowerManagerInternal != null) {
            this.mPowerManagerInternal.registerLowPowerModeObserver(new android.os.PowerManagerInternal.LowPowerModeListener() { // from class: com.android.server.wm.WindowManagerService.6
                public int getServiceType() {
                    return 3;
                }

                public void onLowPowerModeChanged(android.os.PowerSaveState result) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            boolean enabled = result.batterySaverEnabled;
                            if (com.android.server.wm.WindowManagerService.this.mAnimationsDisabled != enabled && !com.android.server.wm.WindowManagerService.this.mAllowAnimationsInLowPowerMode) {
                                com.android.server.wm.WindowManagerService.this.mAnimationsDisabled = enabled;
                                com.android.server.wm.WindowManagerService.this.dispatchNewAnimatorScaleLocked(null);
                            }
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            });
            this.mAnimationsDisabled = this.mPowerManagerInternal.getLowPowerState(3).batterySaverEnabled;
        }
        this.mScreenFrozenLock = this.mPowerManager.newWakeLock(1, "SCREEN_FROZEN");
        this.mScreenFrozenLock.setReferenceCounted(false);
        this.mRotationWatcherController = new com.android.server.wm.RotationWatcherController(this);
        this.mDisplayNotificationController = new com.android.server.wm.DisplayWindowListenerController(this);
        this.mTaskSystemBarsListenerController = new com.android.server.wm.TaskSystemBarsListenerController();
        this.mActivityManager = android.app.ActivityManager.getService();
        this.mAmInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mUmInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        this.mAppOps = (android.app.AppOpsManager) context.getSystemService("appops");
        android.app.AppOpsManager.OnOpChangedListener onOpChangedListener = new android.app.AppOpsManager.OnOpChangedInternalListener() { // from class: com.android.server.wm.WindowManagerService.7
            public void onOpChanged(int op, java.lang.String packageName) {
                com.android.server.wm.WindowManagerService.this.updateAppOpsState();
            }
        };
        this.mAppOps.startWatchingMode(24, (java.lang.String) null, onOpChangedListener);
        this.mAppOps.startWatchingMode(45, (java.lang.String) null, onOpChangedListener);
        this.mPmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mTestUtilityService = (android.content.pm.TestUtilityService) com.android.server.LocalServices.getService(android.content.pm.TestUtilityService.class);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGES_SUSPENDED");
        intentFilter.addAction("android.intent.action.PACKAGES_UNSUSPENDED");
        context.registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.wm.WindowManagerService.8
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.lang.String[] affectedPackages = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
                boolean suspended = "android.intent.action.PACKAGES_SUSPENDED".equals(intent.getAction());
                com.android.server.wm.WindowManagerService.this.updateHiddenWhileSuspendedState(new android.util.ArraySet(java.util.Arrays.asList(affectedPackages)), suspended);
            }
        }, android.os.UserHandle.ALL, intentFilter, null, null);
        this.mWindowAnimationScaleSetting = getWindowAnimationScaleSetting();
        this.mTransitionAnimationScaleSetting = getTransitionAnimationScaleSetting();
        setAnimatorDurationScale(getAnimatorDurationScaleSetting());
        this.mForceDesktopModeOnExternalDisplays = android.provider.Settings.Global.getInt(contentResolver, "force_desktop_mode_on_external_displays", 0) != 0;
        java.lang.String string = android.provider.Settings.Global.getString(contentResolver, "wm_display_settings_path");
        this.mDisplayWindowSettingsProvider = displayWindowSettingsProvider;
        if (string != null) {
            this.mDisplayWindowSettingsProvider.setBaseSettingsFilePath(string);
        }
        this.mDisplayWindowSettings = new com.android.server.wm.DisplayWindowSettings(this, this.mDisplayWindowSettingsProvider);
        android.content.IntentFilter intentFilter2 = new android.content.IntentFilter();
        intentFilter2.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        this.mContext.registerReceiverAsUser(this.mBroadcastReceiver, android.os.UserHandle.ALL, intentFilter2, null, null);
        this.mLatencyTracker = com.android.internal.util.LatencyTracker.getInstance(context);
        this.mSettingsObserver = new com.android.server.wm.WindowManagerService.SettingsObserver();
        this.mSurfaceAnimationRunner = new com.android.server.wm.SurfaceAnimationRunner(this.mTransactionFactory, this.mPowerManagerInternal);
        this.mAllowTheaterModeWakeFromLayout = context.getResources().getBoolean(android.R.bool.config_allowTheaterModeWakeFromMotionWhenNotDreaming);
        this.mTaskPositioningController = new com.android.server.wm.TaskPositioningController(this);
        this.mDragDropController = new com.android.server.wm.DragDropController(this, this.mH.getLooper());
        this.mHighRefreshRateDenylist = com.android.server.wm.HighRefreshRateDenylist.create(context.getResources());
        this.mConstants = new com.android.server.wm.WindowManagerConstants(this, android.provider.DeviceConfigInterface.REAL);
        this.mConstants.start(new android.os.HandlerExecutor(this.mH));
        com.android.server.LocalServices.addService(com.android.server.wm.WindowManagerInternal.class, new com.android.server.wm.WindowManagerService.LocalService());
        com.android.server.LocalServices.addService(com.android.server.wm.ImeTargetVisibilityPolicy.class, new com.android.server.wm.WindowManagerService.ImeTargetVisibilityPolicyImpl());
        this.mEmbeddedWindowController = new com.android.server.wm.EmbeddedWindowController(this.mAtmService, inputManagerService);
        this.mDisplayAreaPolicyProvider = com.android.server.wm.DisplayAreaPolicy.Provider.fromResources(this.mContext.getResources());
        this.mDisplayHashController = new com.android.server.wm.DisplayHashController(this.mContext);
        setGlobalShadowSettings();
        this.mAnrController = new com.android.server.wm.AnrController(this);
        this.mStartingSurfaceController = new com.android.server.wm.StartingSurfaceController(this);
        this.mBlurController = new com.android.server.wm.BlurController(this.mContext, this.mPowerManager);
        this.mTaskFpsCallbackController = new com.android.server.wm.TaskFpsCallbackController(this.mContext);
        this.mAccessibilityController = new com.android.server.wm.AccessibilityController(this);
        this.mScreenRecordingCallbackController = new com.android.server.wm.ScreenRecordingCallbackController(this);
        this.mSystemPerformanceHinter = new android.window.SystemPerformanceHinter(this.mContext, new android.window.SystemPerformanceHinter.DisplayRootProvider() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda21
            public final android.view.SurfaceControl getRootForDisplay(int i) {
                return this.f$0.lambda$new$2(i);
            }
        }, this.mTransactionFactory);
        this.mSystemPerformanceHinter.mTraceTag = 32L;
        this.mWindowManagerServiceExt.enableDefaultLogIfNeed(this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.view.SurfaceControl lambda$new$2(int displayId) {
        android.view.SurfaceControl surfaceControl;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(displayId);
                surfaceControl = dc == null ? null : dc.getSurfaceControl();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return surfaceControl;
    }

    com.android.server.wm.DisplayAreaPolicy.Provider getDisplayAreaPolicyProvider() {
        return this.mDisplayAreaPolicyProvider;
    }

    private void setGlobalShadowSettings() {
        android.content.res.TypedArray a = this.mContext.obtainStyledAttributes(null, com.android.internal.R.styleable.Lighting, 0, 0);
        float lightY = a.getDimension(3, 0.0f);
        float lightZ = a.getDimension(4, 0.0f);
        float lightRadius = a.getDimension(2, 0.0f);
        float ambientShadowAlpha = a.getFloat(0, 0.0f);
        float spotShadowAlpha = a.getFloat(1, 0.0f);
        a.recycle();
        float[] ambientColor = {0.0f, 0.0f, 0.0f, ambientShadowAlpha};
        float[] spotColor = {0.0f, 0.0f, 0.0f, spotShadowAlpha};
        android.view.SurfaceControl.setGlobalShadowSettings(ambientColor, spotColor, lightY, lightZ, lightRadius);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getTransitionAnimationScaleSetting() {
        return android.view.WindowManager.fixScale(android.provider.Settings.Global.getFloat(this.mContext.getContentResolver(), "transition_animation_scale", this.mContext.getResources().getFloat(android.R.dimen.cascading_menus_min_smallest_width)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getAnimatorDurationScaleSetting() {
        return android.view.WindowManager.fixScale(android.provider.Settings.Global.getFloat(this.mContext.getContentResolver(), "animator_duration_scale", this.mAnimatorDurationScaleSetting));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getWindowAnimationScaleSetting() {
        return android.view.WindowManager.fixScale(android.provider.Settings.Global.getFloat(this.mContext.getContentResolver(), "window_animation_scale", this.mWindowAnimationScaleSetting));
    }

    public void onInitReady() {
        initPolicy();
        com.android.server.Watchdog.getInstance().addMonitor(this);
        createWatermark();
        this.mWindowManagerServiceExt.oplusOnInitReady(this.mContext);
        showEmulatorDisplayOverlayIfNeeded();
    }

    public com.android.server.wm.InputManagerCallback getInputManagerCallback() {
        return this.mInputManagerCallback;
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        try {
            return super.onTransact(code, data, reply, flags);
        } catch (java.lang.RuntimeException e) {
            if (!(e instanceof java.lang.SecurityException) && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[5]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(e);
                com.android.internal.protolog.ProtoLogImpl_209941506.wtf(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 3655576047584951173L, 0, "Window Manager Crash %s", protoLogParam0);
            }
            throw e;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 29, insn: 0x02b5: MOVE (r14 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r29 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('callingUid' int)]), block:B:129:0x02b1 */
    /* JADX WARN: Not initialized variable reg: 38, insn: 0x02b9: MOVE (r35 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r38 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('isRoundedCornerOverlay' boolean)]), block:B:129:0x02b1 */
    /* JADX WARN: Not initialized variable reg: 41, insn: 0x02bb: MOVE (r25 I:??[OBJECT, ARRAY]) = (r41 I:??[OBJECT, ARRAY] A[D('parentWindow' com.android.server.wm.WindowState)]), block:B:129:0x02b1 */
    /* JADX WARN: Removed duplicated region for block: B:349:0x07ac  */
    /* JADX WARN: Removed duplicated region for block: B:356:0x07b9  */
    /* JADX WARN: Removed duplicated region for block: B:410:0x08a4  */
    /* JADX WARN: Removed duplicated region for block: B:413:0x08d7 A[Catch: all -> 0x088d, TRY_ENTER, TRY_LEAVE, TryCatch #24 {all -> 0x088d, blocks: (B:394:0x084e, B:396:0x086d, B:398:0x0873, B:413:0x08d7, B:403:0x0883), top: B:577:0x0827 }] */
    /* JADX WARN: Removed duplicated region for block: B:418:0x090f A[Catch: all -> 0x0afb, TryCatch #10 {all -> 0x0afb, blocks: (B:416:0x0908, B:418:0x090f, B:419:0x0914, B:423:0x0934, B:427:0x094c, B:429:0x0956, B:448:0x09bb, B:450:0x09cd, B:451:0x09d1, B:453:0x09d7, B:454:0x09db, B:456:0x09df, B:459:0x09eb, B:461:0x09f9, B:466:0x0a0c, B:468:0x0a16, B:471:0x0a34, B:473:0x0a38, B:475:0x0a40, B:478:0x0a51, B:479:0x0a5b, B:481:0x0a90, B:485:0x0a99, B:487:0x0a9f, B:489:0x0aa6, B:490:0x0aa9, B:476:0x0a48, B:458:0x09e7, B:433:0x0977, B:435:0x0981, B:438:0x098a, B:441:0x0994, B:442:0x09a0, B:444:0x09a6, B:445:0x09ad, B:447:0x09b5), top: B:553:0x0908 }] */
    /* JADX WARN: Removed duplicated region for block: B:421:0x0931  */
    /* JADX WARN: Removed duplicated region for block: B:422:0x0933  */
    /* JADX WARN: Removed duplicated region for block: B:436:0x0986  */
    /* JADX WARN: Removed duplicated region for block: B:438:0x098a A[Catch: all -> 0x0afb, TryCatch #10 {all -> 0x0afb, blocks: (B:416:0x0908, B:418:0x090f, B:419:0x0914, B:423:0x0934, B:427:0x094c, B:429:0x0956, B:448:0x09bb, B:450:0x09cd, B:451:0x09d1, B:453:0x09d7, B:454:0x09db, B:456:0x09df, B:459:0x09eb, B:461:0x09f9, B:466:0x0a0c, B:468:0x0a16, B:471:0x0a34, B:473:0x0a38, B:475:0x0a40, B:478:0x0a51, B:479:0x0a5b, B:481:0x0a90, B:485:0x0a99, B:487:0x0a9f, B:489:0x0aa6, B:490:0x0aa9, B:476:0x0a48, B:458:0x09e7, B:433:0x0977, B:435:0x0981, B:438:0x098a, B:441:0x0994, B:442:0x09a0, B:444:0x09a6, B:445:0x09ad, B:447:0x09b5), top: B:553:0x0908 }] */
    /* JADX WARN: Removed duplicated region for block: B:439:0x0990  */
    /* JADX WARN: Removed duplicated region for block: B:450:0x09cd A[Catch: all -> 0x0afb, TryCatch #10 {all -> 0x0afb, blocks: (B:416:0x0908, B:418:0x090f, B:419:0x0914, B:423:0x0934, B:427:0x094c, B:429:0x0956, B:448:0x09bb, B:450:0x09cd, B:451:0x09d1, B:453:0x09d7, B:454:0x09db, B:456:0x09df, B:459:0x09eb, B:461:0x09f9, B:466:0x0a0c, B:468:0x0a16, B:471:0x0a34, B:473:0x0a38, B:475:0x0a40, B:478:0x0a51, B:479:0x0a5b, B:481:0x0a90, B:485:0x0a99, B:487:0x0a9f, B:489:0x0aa6, B:490:0x0aa9, B:476:0x0a48, B:458:0x09e7, B:433:0x0977, B:435:0x0981, B:438:0x098a, B:441:0x0994, B:442:0x09a0, B:444:0x09a6, B:445:0x09ad, B:447:0x09b5), top: B:553:0x0908 }] */
    /* JADX WARN: Removed duplicated region for block: B:453:0x09d7 A[Catch: all -> 0x0afb, TryCatch #10 {all -> 0x0afb, blocks: (B:416:0x0908, B:418:0x090f, B:419:0x0914, B:423:0x0934, B:427:0x094c, B:429:0x0956, B:448:0x09bb, B:450:0x09cd, B:451:0x09d1, B:453:0x09d7, B:454:0x09db, B:456:0x09df, B:459:0x09eb, B:461:0x09f9, B:466:0x0a0c, B:468:0x0a16, B:471:0x0a34, B:473:0x0a38, B:475:0x0a40, B:478:0x0a51, B:479:0x0a5b, B:481:0x0a90, B:485:0x0a99, B:487:0x0a9f, B:489:0x0aa6, B:490:0x0aa9, B:476:0x0a48, B:458:0x09e7, B:433:0x0977, B:435:0x0981, B:438:0x098a, B:441:0x0994, B:442:0x09a0, B:444:0x09a6, B:445:0x09ad, B:447:0x09b5), top: B:553:0x0908 }] */
    /* JADX WARN: Removed duplicated region for block: B:458:0x09e7 A[Catch: all -> 0x0afb, TryCatch #10 {all -> 0x0afb, blocks: (B:416:0x0908, B:418:0x090f, B:419:0x0914, B:423:0x0934, B:427:0x094c, B:429:0x0956, B:448:0x09bb, B:450:0x09cd, B:451:0x09d1, B:453:0x09d7, B:454:0x09db, B:456:0x09df, B:459:0x09eb, B:461:0x09f9, B:466:0x0a0c, B:468:0x0a16, B:471:0x0a34, B:473:0x0a38, B:475:0x0a40, B:478:0x0a51, B:479:0x0a5b, B:481:0x0a90, B:485:0x0a99, B:487:0x0a9f, B:489:0x0aa6, B:490:0x0aa9, B:476:0x0a48, B:458:0x09e7, B:433:0x0977, B:435:0x0981, B:438:0x098a, B:441:0x0994, B:442:0x09a0, B:444:0x09a6, B:445:0x09ad, B:447:0x09b5), top: B:553:0x0908 }] */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0a0c A[Catch: all -> 0x0afb, TryCatch #10 {all -> 0x0afb, blocks: (B:416:0x0908, B:418:0x090f, B:419:0x0914, B:423:0x0934, B:427:0x094c, B:429:0x0956, B:448:0x09bb, B:450:0x09cd, B:451:0x09d1, B:453:0x09d7, B:454:0x09db, B:456:0x09df, B:459:0x09eb, B:461:0x09f9, B:466:0x0a0c, B:468:0x0a16, B:471:0x0a34, B:473:0x0a38, B:475:0x0a40, B:478:0x0a51, B:479:0x0a5b, B:481:0x0a90, B:485:0x0a99, B:487:0x0a9f, B:489:0x0aa6, B:490:0x0aa9, B:476:0x0a48, B:458:0x09e7, B:433:0x0977, B:435:0x0981, B:438:0x098a, B:441:0x0994, B:442:0x09a0, B:444:0x09a6, B:445:0x09ad, B:447:0x09b5), top: B:553:0x0908 }] */
    /* JADX WARN: Removed duplicated region for block: B:470:0x0a30  */
    /* JADX WARN: Removed duplicated region for block: B:476:0x0a48 A[Catch: all -> 0x0afb, TryCatch #10 {all -> 0x0afb, blocks: (B:416:0x0908, B:418:0x090f, B:419:0x0914, B:423:0x0934, B:427:0x094c, B:429:0x0956, B:448:0x09bb, B:450:0x09cd, B:451:0x09d1, B:453:0x09d7, B:454:0x09db, B:456:0x09df, B:459:0x09eb, B:461:0x09f9, B:466:0x0a0c, B:468:0x0a16, B:471:0x0a34, B:473:0x0a38, B:475:0x0a40, B:478:0x0a51, B:479:0x0a5b, B:481:0x0a90, B:485:0x0a99, B:487:0x0a9f, B:489:0x0aa6, B:490:0x0aa9, B:476:0x0a48, B:458:0x09e7, B:433:0x0977, B:435:0x0981, B:438:0x098a, B:441:0x0994, B:442:0x09a0, B:444:0x09a6, B:445:0x09ad, B:447:0x09b5), top: B:553:0x0908 }] */
    /* JADX WARN: Removed duplicated region for block: B:478:0x0a51 A[Catch: all -> 0x0afb, TryCatch #10 {all -> 0x0afb, blocks: (B:416:0x0908, B:418:0x090f, B:419:0x0914, B:423:0x0934, B:427:0x094c, B:429:0x0956, B:448:0x09bb, B:450:0x09cd, B:451:0x09d1, B:453:0x09d7, B:454:0x09db, B:456:0x09df, B:459:0x09eb, B:461:0x09f9, B:466:0x0a0c, B:468:0x0a16, B:471:0x0a34, B:473:0x0a38, B:475:0x0a40, B:478:0x0a51, B:479:0x0a5b, B:481:0x0a90, B:485:0x0a99, B:487:0x0a9f, B:489:0x0aa6, B:490:0x0aa9, B:476:0x0a48, B:458:0x09e7, B:433:0x0977, B:435:0x0981, B:438:0x098a, B:441:0x0994, B:442:0x09a0, B:444:0x09a6, B:445:0x09ad, B:447:0x09b5), top: B:553:0x0908 }] */
    /* JADX WARN: Removed duplicated region for block: B:484:0x0a98  */
    /* JADX WARN: Removed duplicated region for block: B:487:0x0a9f A[Catch: all -> 0x0afb, TryCatch #10 {all -> 0x0afb, blocks: (B:416:0x0908, B:418:0x090f, B:419:0x0914, B:423:0x0934, B:427:0x094c, B:429:0x0956, B:448:0x09bb, B:450:0x09cd, B:451:0x09d1, B:453:0x09d7, B:454:0x09db, B:456:0x09df, B:459:0x09eb, B:461:0x09f9, B:466:0x0a0c, B:468:0x0a16, B:471:0x0a34, B:473:0x0a38, B:475:0x0a40, B:478:0x0a51, B:479:0x0a5b, B:481:0x0a90, B:485:0x0a99, B:487:0x0a9f, B:489:0x0aa6, B:490:0x0aa9, B:476:0x0a48, B:458:0x09e7, B:433:0x0977, B:435:0x0981, B:438:0x098a, B:441:0x0994, B:442:0x09a0, B:444:0x09a6, B:445:0x09ad, B:447:0x09b5), top: B:553:0x0908 }] */
    /* JADX WARN: Removed duplicated region for block: B:489:0x0aa6 A[Catch: all -> 0x0afb, TryCatch #10 {all -> 0x0afb, blocks: (B:416:0x0908, B:418:0x090f, B:419:0x0914, B:423:0x0934, B:427:0x094c, B:429:0x0956, B:448:0x09bb, B:450:0x09cd, B:451:0x09d1, B:453:0x09d7, B:454:0x09db, B:456:0x09df, B:459:0x09eb, B:461:0x09f9, B:466:0x0a0c, B:468:0x0a16, B:471:0x0a34, B:473:0x0a38, B:475:0x0a40, B:478:0x0a51, B:479:0x0a5b, B:481:0x0a90, B:485:0x0a99, B:487:0x0a9f, B:489:0x0aa6, B:490:0x0aa9, B:476:0x0a48, B:458:0x09e7, B:433:0x0977, B:435:0x0981, B:438:0x098a, B:441:0x0994, B:442:0x09a0, B:444:0x09a6, B:445:0x09ad, B:447:0x09b5), top: B:553:0x0908 }] */
    /* JADX WARN: Removed duplicated region for block: B:494:0x0ac0 A[Catch: all -> 0x0af9, TRY_LEAVE, TryCatch #9 {all -> 0x0af9, blocks: (B:492:0x0ab9, B:494:0x0ac0), top: B:551:0x0ab9 }] */
    /* JADX WARN: Removed duplicated region for block: B:500:0x0ae2 A[Catch: all -> 0x0b9c, TryCatch #32 {all -> 0x0b9c, blocks: (B:533:0x0b97, B:496:0x0aca, B:498:0x0ad5, B:501:0x0aeb, B:502:0x0af1, B:500:0x0ae2, B:529:0x0b80, B:530:0x0b87), top: B:590:0x0053 }] */
    /* JADX WARN: Removed duplicated region for block: B:588:0x0829 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:593:0x079a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r25v14 */
    /* JADX WARN: Type inference failed for: r8v14 */
    /* JADX WARN: Type inference failed for: r8v5 */
    /* JADX WARN: Type inference failed for: r8v6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int addWindow(com.android.server.wm.Session r53, android.view.IWindow r54, android.view.WindowManager.LayoutParams r55, int r56, int r57, int r58, int r59, android.view.InputChannel r60, android.view.InsetsState r61, android.view.InsetsSourceControl.Array r62, android.graphics.Rect r63, float[] r64) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2974
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerService.addWindow(com.android.server.wm.Session, android.view.IWindow, android.view.WindowManager$LayoutParams, int, int, int, int, android.view.InputChannel, android.view.InsetsState, android.view.InsetsSourceControl$Array, android.graphics.Rect, float[]):int");
    }

    private boolean unprivilegedAppCanCreateTokenWith(com.android.server.wm.WindowState parentWindow, int callingUid, int type, int rootType, android.os.IBinder tokenForLog, java.lang.String packageName) {
        if (rootType >= 1 && rootType <= 99) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(tokenForLog);
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -7315179333005789167L, 0, "Attempted to add application window with unknown token %s.  Aborting.", protoLogParam0);
            }
            return false;
        }
        if (rootType == 2011) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(tokenForLog);
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -7547709658889961930L, 0, "Attempted to add input method window with unknown token %s.  Aborting.", protoLogParam02);
            }
            return false;
        }
        if (rootType == 2031) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                java.lang.String protoLogParam03 = java.lang.String.valueOf(tokenForLog);
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 3009864422591182484L, 0, "Attempted to add voice interaction window with unknown token %s.  Aborting.", protoLogParam03);
            }
            return false;
        }
        if (rootType == 2013) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                java.lang.String protoLogParam04 = java.lang.String.valueOf(tokenForLog);
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -2639914438438144071L, 0, "Attempted to add wallpaper window with unknown token %s.  Aborting.", protoLogParam04);
            }
            return false;
        }
        if (rootType == 2035) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                java.lang.String protoLogParam05 = java.lang.String.valueOf(tokenForLog);
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -7529563697886120786L, 0, "Attempted to add QS dialog window with unknown token %s.  Aborting.", protoLogParam05);
            }
            return false;
        }
        if (rootType == 2032) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                java.lang.String protoLogParam06 = java.lang.String.valueOf(tokenForLog);
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 4253401518117961686L, 0, "Attempted to add Accessibility overlay window with unknown token %s.  Aborting.", protoLogParam06);
            }
            return false;
        }
        if (type != 2005 || !doesAddToastWindowRequireToken(packageName, callingUid, parentWindow)) {
            return true;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
            java.lang.String protoLogParam07 = java.lang.String.valueOf(tokenForLog);
            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 5834230650841873680L, 0, "Attempted to add a toast window with unknown token %s.  Aborting.", protoLogParam07);
        }
        return false;
    }

    private com.android.server.wm.DisplayContent getDisplayContentOrCreate(int displayId, android.os.IBinder token) {
        com.android.server.wm.WindowToken wToken;
        if (token != null && (wToken = this.mRoot.getWindowToken(token)) != null) {
            return wToken.getDisplayContent();
        }
        return this.mRoot.getDisplayContentOrCreate(displayId);
    }

    private boolean doesAddToastWindowRequireToken(java.lang.String packageName, int callingUid, com.android.server.wm.WindowState attachedWindow) {
        if (attachedWindow != null) {
            return attachedWindow.mActivityRecord != null && attachedWindow.mActivityRecord.mTargetSdk >= 26;
        }
        android.content.pm.ApplicationInfo appInfo = this.mPmInternal.getApplicationInfo(packageName, 0L, 1000, android.os.UserHandle.getUserId(callingUid));
        if (appInfo == null || appInfo.uid != callingUid) {
            throw new java.lang.SecurityException("Package " + packageName + " not in UID " + callingUid);
        }
        return appInfo.targetSdkVersion >= 26;
    }

    public void refreshScreenCaptureDisabled() {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != MY_UID) {
            throw new java.lang.SecurityException("Only system can call refreshScreenCaptureDisabled.");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.refreshSecureSurfaceState();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    void removeClientToken(com.android.server.wm.Session session, android.os.IBinder client) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState win = windowForClientLocked(session, client, false);
                if (win != null) {
                    win.removeIfPossible();
                    resetPriorityAfterLockedSection();
                } else {
                    this.mEmbeddedWindowController.remove(client);
                    resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void postWindowRemoveCleanupLocked(com.android.server.wm.WindowState win) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(win);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 5265273548711408921L, 0, null, protoLogParam0);
        }
        android.os.IBinder client = win.mClient.asBinder();
        this.mWindowMap.remove(client);
        if (android.view.flags.Flags.sensitiveContentAppProtection()) {
            notifyWindowRemovedListeners(client);
        }
        if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE) {
            this.mWindowManagerServiceExt.removeWindow(win);
        }
        com.android.server.wm.DisplayContent dc = win.getDisplayContent();
        dc.getDisplayRotation().markForSeamlessRotation(win, false);
        win.resetAppOpsState();
        if (dc.mCurrentFocus == null) {
            dc.mWinRemovedSinceNullFocus.add(win);
        }
        this.mEmbeddedWindowController.onWindowRemoved(win);
        this.mResizingWindows.remove(win);
        updateNonSystemOverlayWindowsVisibilityIfNeeded(win, false);
        this.mWindowsChanged = true;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_MOVEMENT_enabled[1]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(win);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT, -3847568084407666790L, 0, null, protoLogParam02);
        }
        com.android.server.wm.DisplayContent displayContent = win.getDisplayContent();
        if (displayContent.mInputMethodWindow == win) {
            displayContent.setInputMethodWindowLocked(null);
        }
        com.android.server.wm.WindowToken token = win.mToken;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
            java.lang.String protoLogParam03 = java.lang.String.valueOf(win);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(token);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1419572818243106725L, 0, null, protoLogParam03, protoLogParam1);
        }
        if (token.isEmpty() && !token.mPersistOnEmpty) {
            token.removeImmediately();
        }
        if (win.mActivityRecord != null) {
            win.mActivityRecord.postWindowRemoveStartingWindowCleanup(win);
        }
        if (win.mAttrs.type == 2013) {
            dc.mWallpaperController.clearLastWallpaperTimeoutTime();
            dc.pendingLayoutChanges |= 4;
        } else if (dc.mWallpaperController.isWallpaperTarget(win)) {
            dc.pendingLayoutChanges |= 4;
        }
        if (dc != null && !this.mWindowPlacerLocked.isInLayout()) {
            dc.assignWindowLayers(true);
            if (getFocusedWindow() == win) {
                this.mFocusMayChange = true;
            }
            if (!this.mWindowManagerServiceExt.deferPerformSurfacePlacement(win)) {
                this.mWindowPlacerLocked.performSurfacePlacement();
            }
            if (win.mActivityRecord != null) {
                win.mActivityRecord.updateReportedVisibilityLocked();
            }
        }
        dc.getInputMonitor().updateInputWindowsLw(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHiddenWhileSuspendedState(android.util.ArraySet<java.lang.String> packages, boolean suspended) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.updateHiddenWhileSuspendedState(packages, suspended);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAppOpsState() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.updateAppOpsState();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    static void logSurface(com.android.server.wm.WindowState w, java.lang.String msg, boolean withStackTrace) {
        java.lang.String str = "  SURFACE " + msg + ": " + w;
        if (withStackTrace) {
            logWithStack(TAG, str);
        } else {
            android.util.Slog.i(TAG, str);
        }
    }

    static void logWithStack(java.lang.String tag, java.lang.String s) {
        java.lang.RuntimeException e = null;
        if (com.android.server.wm.WindowManagerDebugConfig.SHOW_STACK_CRAWLS) {
            e = new java.lang.RuntimeException();
            e.fillInStackTrace();
        }
        android.util.Slog.i(tag, s, e);
    }

    void clearTouchableRegion(com.android.server.wm.Session session, android.view.IWindow client) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState w = windowForClientLocked(session, client, false);
                    w.clearClientTouchableRegion();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    void setInsetsWindow(com.android.server.wm.Session session, android.view.IWindow client, int touchableInsets, android.graphics.Rect contentInsets, android.graphics.Rect visibleInsets, android.graphics.Region touchableRegion) {
        int uid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState w = windowForClientLocked(session, client, false);
                    if (w != null) {
                        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
                            android.util.Slog.d(TAG, "setInsetsWindow " + w + ", contentInsets=" + w.mGivenContentInsets + " -> " + contentInsets + ", visibleInsets=" + w.mGivenVisibleInsets + " -> " + visibleInsets + ", touchableRegion=" + w.mGivenTouchableRegion + " -> " + touchableRegion + ", touchableInsets " + w.mTouchableInsets + " -> " + touchableInsets);
                        }
                        boolean wasGivenInsetsPending = w.mGivenInsetsPending;
                        w.mGivenInsetsPending = false;
                        if ((wasGivenInsetsPending && w.hasInsetsSourceProvider()) || w.mTouchableInsets != touchableInsets || !w.mGivenContentInsets.equals(contentInsets) || !w.mGivenVisibleInsets.equals(visibleInsets) || !w.mGivenTouchableRegion.equals(touchableRegion)) {
                            w.mGivenContentInsets.set(contentInsets);
                            w.mGivenVisibleInsets.set(visibleInsets);
                            w.mGivenTouchableRegion.set(touchableRegion);
                            w.mTouchableInsets = touchableInsets;
                            if (w.mGlobalScale != 1.0f) {
                                w.mGivenContentInsets.scale(w.mGlobalScale);
                                w.mGivenVisibleInsets.scale(w.mGlobalScale);
                                w.mGivenTouchableRegion.scale(w.mGlobalScale);
                            }
                            w.setDisplayLayoutNeeded();
                            w.updateSourceFrame(w.getFrame());
                            this.mWindowPlacerLocked.performSurfacePlacement();
                            w.getDisplayContent().getInputMonitor().updateInputWindowsLw(true);
                            if (this.mAccessibilityController.hasCallbacks()) {
                                this.mAccessibilityController.onSomeWindowResizedOrMovedWithCallingUid(uid, w.getDisplayContent().getDisplayId());
                            }
                        } else {
                            resetPriorityAfterLockedSection();
                            return;
                        }
                    }
                    resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void onRectangleOnScreenRequested(android.os.IBinder token, android.graphics.Rect rectangle) {
        com.android.server.wm.WindowState window;
        com.android.server.wm.AccessibilityController.AccessibilityControllerInternalImpl a11yControllerInternal = com.android.server.wm.AccessibilityController.getAccessibilityControllerInternal(this);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (a11yControllerInternal.hasWindowManagerEventDispatcher() && (window = this.mWindowMap.get(token)) != null) {
                    a11yControllerInternal.onRectangleOnScreenRequested(window.getDisplayId(), rectangle);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public android.view.IWindowId getWindowId(android.os.IBinder token) {
        com.android.server.wm.WindowState.WindowId windowId;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState window = this.mWindowMap.get(token);
                windowId = window != null ? window.mWindowId : null;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return windowId;
    }

    public void pokeDrawLock(com.android.server.wm.Session session, android.os.IBinder token) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState window = windowForClientLocked(session, token, false);
                if (window != null) {
                    window.pokeDrawLockLw(this.mDrawLockTimeoutMillis);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    private boolean hasStatusBarPermission(int pid, int uid) {
        return this.mContext.checkPermission("android.permission.STATUS_BAR", pid, uid) == 0;
    }

    public boolean cancelDraw(com.android.server.wm.Session session, android.view.IWindow client) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState win = windowForClientLocked(session, client, false);
                if (win == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean zCancelAndRedraw = win.cancelAndRedraw();
                resetPriorityAfterLockedSection();
                return zCancelAndRedraw;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public int relayoutWindow(com.android.server.wm.Session session, android.view.IWindow client, android.view.WindowManager.LayoutParams attrs, int requestedWidth, int requestedHeight, int viewVisibility, int flags, int seq, int lastSyncSeqId, android.view.WindowRelayoutResult outRelayoutResult) {
        android.window.ClientWindowFrames outFrames;
        android.util.MergedConfiguration outMergedConfiguration;
        android.view.SurfaceControl outSurfaceControl;
        android.view.InsetsState outInsetsState;
        android.view.InsetsSourceControl.Array outActiveControls;
        if (outRelayoutResult != null) {
            android.window.ClientWindowFrames outFrames2 = outRelayoutResult.frames;
            android.util.MergedConfiguration outMergedConfiguration2 = outRelayoutResult.mergedConfiguration;
            android.view.SurfaceControl outSurfaceControl2 = outRelayoutResult.surfaceControl;
            android.view.InsetsState outInsetsState2 = outRelayoutResult.insetsState;
            outFrames = outFrames2;
            outMergedConfiguration = outMergedConfiguration2;
            outSurfaceControl = outSurfaceControl2;
            outInsetsState = outInsetsState2;
            outActiveControls = outRelayoutResult.activeControls;
        } else {
            outFrames = null;
            outMergedConfiguration = null;
            outSurfaceControl = null;
            outInsetsState = null;
            outActiveControls = null;
        }
        return relayoutWindowInner(session, client, attrs, requestedWidth, requestedHeight, viewVisibility, flags, seq, lastSyncSeqId, outFrames, outMergedConfiguration, outSurfaceControl, outInsetsState, outActiveControls, null, outRelayoutResult);
    }

    @java.lang.Deprecated
    public int relayoutWindow(com.android.server.wm.Session session, android.view.IWindow client, android.view.WindowManager.LayoutParams attrs, int requestedWidth, int requestedHeight, int viewVisibility, int flags, int seq, int lastSyncSeqId, android.window.ClientWindowFrames outFrames, android.util.MergedConfiguration outMergedConfiguration, android.view.SurfaceControl outSurfaceControl, android.view.InsetsState outInsetsState, android.view.InsetsSourceControl.Array outActiveControls, android.os.Bundle outBundle) {
        return relayoutWindowInner(session, client, attrs, requestedWidth, requestedHeight, viewVisibility, flags, seq, lastSyncSeqId, outFrames, outMergedConfiguration, outSurfaceControl, outInsetsState, outActiveControls, outBundle, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:173:0x034d  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x03e4  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0448 A[Catch: all -> 0x03bc, TRY_ENTER, TRY_LEAVE, TryCatch #13 {all -> 0x03bc, blocks: (B:180:0x03a6, B:196:0x0448, B:199:0x045b, B:201:0x0463, B:203:0x046b, B:224:0x049b, B:231:0x04a7, B:235:0x04b0, B:239:0x04bb, B:241:0x04c1, B:244:0x04e1, B:247:0x04f7, B:250:0x0539, B:258:0x054b, B:260:0x054f, B:262:0x0555, B:268:0x0564, B:270:0x056a, B:272:0x056e, B:274:0x0572, B:189:0x0408), top: B:547:0x039d }] */
    /* JADX WARN: Removed duplicated region for block: B:205:0x0473  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x048d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0492  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x04a2  */
    /* JADX WARN: Removed duplicated region for block: B:234:0x04af  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x04b6  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x04b9  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x04c1 A[Catch: all -> 0x03bc, TRY_LEAVE, TryCatch #13 {all -> 0x03bc, blocks: (B:180:0x03a6, B:196:0x0448, B:199:0x045b, B:201:0x0463, B:203:0x046b, B:224:0x049b, B:231:0x04a7, B:235:0x04b0, B:239:0x04bb, B:241:0x04c1, B:244:0x04e1, B:247:0x04f7, B:250:0x0539, B:258:0x054b, B:260:0x054f, B:262:0x0555, B:268:0x0564, B:270:0x056a, B:272:0x056e, B:274:0x0572, B:189:0x0408), top: B:547:0x039d }] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x04e1 A[Catch: all -> 0x03bc, TRY_ENTER, TRY_LEAVE, TryCatch #13 {all -> 0x03bc, blocks: (B:180:0x03a6, B:196:0x0448, B:199:0x045b, B:201:0x0463, B:203:0x046b, B:224:0x049b, B:231:0x04a7, B:235:0x04b0, B:239:0x04bb, B:241:0x04c1, B:244:0x04e1, B:247:0x04f7, B:250:0x0539, B:258:0x054b, B:260:0x054f, B:262:0x0555, B:268:0x0564, B:270:0x056a, B:272:0x056e, B:274:0x0572, B:189:0x0408), top: B:547:0x039d }] */
    /* JADX WARN: Removed duplicated region for block: B:247:0x04f7 A[Catch: all -> 0x03bc, TRY_ENTER, TryCatch #13 {all -> 0x03bc, blocks: (B:180:0x03a6, B:196:0x0448, B:199:0x045b, B:201:0x0463, B:203:0x046b, B:224:0x049b, B:231:0x04a7, B:235:0x04b0, B:239:0x04bb, B:241:0x04c1, B:244:0x04e1, B:247:0x04f7, B:250:0x0539, B:258:0x054b, B:260:0x054f, B:262:0x0555, B:268:0x0564, B:270:0x056a, B:272:0x056e, B:274:0x0572, B:189:0x0408), top: B:547:0x039d }] */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0531  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x0539 A[Catch: all -> 0x03bc, TRY_LEAVE, TryCatch #13 {all -> 0x03bc, blocks: (B:180:0x03a6, B:196:0x0448, B:199:0x045b, B:201:0x0463, B:203:0x046b, B:224:0x049b, B:231:0x04a7, B:235:0x04b0, B:239:0x04bb, B:241:0x04c1, B:244:0x04e1, B:247:0x04f7, B:250:0x0539, B:258:0x054b, B:260:0x054f, B:262:0x0555, B:268:0x0564, B:270:0x056a, B:272:0x056e, B:274:0x0572, B:189:0x0408), top: B:547:0x039d }] */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0543  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0545  */
    /* JADX WARN: Removed duplicated region for block: B:265:0x055f  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x05ba  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x05c0  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x0623  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0648 A[Catch: all -> 0x0637, TryCatch #9 {all -> 0x0637, blocks: (B:306:0x0630, B:311:0x0648, B:315:0x065b, B:317:0x0663, B:319:0x0667, B:321:0x066e, B:341:0x06f5, B:355:0x0717, B:357:0x071c, B:359:0x0722, B:362:0x072c, B:365:0x0746, B:367:0x074a, B:370:0x0754, B:326:0x0689, B:328:0x068f, B:329:0x06a0, B:331:0x06a4, B:333:0x06de, B:335:0x06e3, B:336:0x06e9, B:332:0x06bc), top: B:540:0x0630, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:323:0x0678 A[Catch: all -> 0x0967, TRY_ENTER, TRY_LEAVE, TryCatch #4 {all -> 0x0967, blocks: (B:304:0x0628, B:360:0x0728, B:363:0x0733, B:368:0x0750, B:371:0x0759, B:344:0x06ff, B:323:0x0678, B:338:0x06ec), top: B:530:0x0628 }] */
    /* JADX WARN: Removed duplicated region for block: B:341:0x06f5 A[Catch: all -> 0x0637, TRY_ENTER, TRY_LEAVE, TryCatch #9 {all -> 0x0637, blocks: (B:306:0x0630, B:311:0x0648, B:315:0x065b, B:317:0x0663, B:319:0x0667, B:321:0x066e, B:341:0x06f5, B:355:0x0717, B:357:0x071c, B:359:0x0722, B:362:0x072c, B:365:0x0746, B:367:0x074a, B:370:0x0754, B:326:0x0689, B:328:0x068f, B:329:0x06a0, B:331:0x06a4, B:333:0x06de, B:335:0x06e3, B:336:0x06e9, B:332:0x06bc), top: B:540:0x0630, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:343:0x06fd  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x070f  */
    /* JADX WARN: Removed duplicated region for block: B:351:0x0711  */
    /* JADX WARN: Removed duplicated region for block: B:354:0x0716  */
    /* JADX WARN: Removed duplicated region for block: B:359:0x0722 A[Catch: all -> 0x0637, TRY_LEAVE, TryCatch #9 {all -> 0x0637, blocks: (B:306:0x0630, B:311:0x0648, B:315:0x065b, B:317:0x0663, B:319:0x0667, B:321:0x066e, B:341:0x06f5, B:355:0x0717, B:357:0x071c, B:359:0x0722, B:362:0x072c, B:365:0x0746, B:367:0x074a, B:370:0x0754, B:326:0x0689, B:328:0x068f, B:329:0x06a0, B:331:0x06a4, B:333:0x06de, B:335:0x06e3, B:336:0x06e9, B:332:0x06bc), top: B:540:0x0630, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:362:0x072c A[Catch: all -> 0x0637, TRY_ENTER, TRY_LEAVE, TryCatch #9 {all -> 0x0637, blocks: (B:306:0x0630, B:311:0x0648, B:315:0x065b, B:317:0x0663, B:319:0x0667, B:321:0x066e, B:341:0x06f5, B:355:0x0717, B:357:0x071c, B:359:0x0722, B:362:0x072c, B:365:0x0746, B:367:0x074a, B:370:0x0754, B:326:0x0689, B:328:0x068f, B:329:0x06a0, B:331:0x06a4, B:333:0x06de, B:335:0x06e3, B:336:0x06e9, B:332:0x06bc), top: B:540:0x0630, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:365:0x0746 A[Catch: all -> 0x0637, TRY_ENTER, TryCatch #9 {all -> 0x0637, blocks: (B:306:0x0630, B:311:0x0648, B:315:0x065b, B:317:0x0663, B:319:0x0667, B:321:0x066e, B:341:0x06f5, B:355:0x0717, B:357:0x071c, B:359:0x0722, B:362:0x072c, B:365:0x0746, B:367:0x074a, B:370:0x0754, B:326:0x0689, B:328:0x068f, B:329:0x06a0, B:331:0x06a4, B:333:0x06de, B:335:0x06e3, B:336:0x06e9, B:332:0x06bc), top: B:540:0x0630, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:370:0x0754 A[Catch: all -> 0x0637, TRY_ENTER, TRY_LEAVE, TryCatch #9 {all -> 0x0637, blocks: (B:306:0x0630, B:311:0x0648, B:315:0x065b, B:317:0x0663, B:319:0x0667, B:321:0x066e, B:341:0x06f5, B:355:0x0717, B:357:0x071c, B:359:0x0722, B:362:0x072c, B:365:0x0746, B:367:0x074a, B:370:0x0754, B:326:0x0689, B:328:0x068f, B:329:0x06a0, B:331:0x06a4, B:333:0x06de, B:335:0x06e3, B:336:0x06e9, B:332:0x06bc), top: B:540:0x0630, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:373:0x075f  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x0764  */
    /* JADX WARN: Removed duplicated region for block: B:377:0x076a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:387:0x078a  */
    /* JADX WARN: Removed duplicated region for block: B:396:0x07aa  */
    /* JADX WARN: Removed duplicated region for block: B:424:0x081c  */
    /* JADX WARN: Removed duplicated region for block: B:427:0x082b A[Catch: all -> 0x095e, TryCatch #28 {all -> 0x095e, blocks: (B:419:0x0801, B:427:0x082b, B:428:0x082f, B:430:0x0836, B:431:0x0851, B:433:0x0855, B:434:0x0877, B:438:0x0885, B:441:0x088d, B:443:0x08a6, B:450:0x08bb, B:451:0x08c9, B:454:0x08d2, B:457:0x08da, B:415:0x07f2), top: B:575:0x07f2 }] */
    /* JADX WARN: Removed duplicated region for block: B:430:0x0836 A[Catch: all -> 0x095e, TryCatch #28 {all -> 0x095e, blocks: (B:419:0x0801, B:427:0x082b, B:428:0x082f, B:430:0x0836, B:431:0x0851, B:433:0x0855, B:434:0x0877, B:438:0x0885, B:441:0x088d, B:443:0x08a6, B:450:0x08bb, B:451:0x08c9, B:454:0x08d2, B:457:0x08da, B:415:0x07f2), top: B:575:0x07f2 }] */
    /* JADX WARN: Removed duplicated region for block: B:433:0x0855 A[Catch: all -> 0x095e, TryCatch #28 {all -> 0x095e, blocks: (B:419:0x0801, B:427:0x082b, B:428:0x082f, B:430:0x0836, B:431:0x0851, B:433:0x0855, B:434:0x0877, B:438:0x0885, B:441:0x088d, B:443:0x08a6, B:450:0x08bb, B:451:0x08c9, B:454:0x08d2, B:457:0x08da, B:415:0x07f2), top: B:575:0x07f2 }] */
    /* JADX WARN: Removed duplicated region for block: B:436:0x0882  */
    /* JADX WARN: Removed duplicated region for block: B:437:0x0884  */
    /* JADX WARN: Removed duplicated region for block: B:442:0x08a2  */
    /* JADX WARN: Removed duplicated region for block: B:445:0x08b0  */
    /* JADX WARN: Removed duplicated region for block: B:446:0x08b3  */
    /* JADX WARN: Removed duplicated region for block: B:453:0x08d0  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:468:0x08fe  */
    /* JADX WARN: Removed duplicated region for block: B:484:0x093a A[Catch: all -> 0x095c, TRY_LEAVE, TryCatch #26 {all -> 0x095c, blocks: (B:460:0x08e0, B:462:0x08e6, B:464:0x08ea, B:482:0x092e, B:484:0x093a, B:466:0x08f4, B:470:0x0902, B:473:0x090a, B:475:0x090e, B:477:0x0914, B:478:0x0916, B:480:0x091c), top: B:571:0x08ce }] */
    /* JADX WARN: Removed duplicated region for block: B:486:0x094a  */
    /* JADX WARN: Removed duplicated region for block: B:540:0x0630 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:561:0x039f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:567:0x094f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r20v11 */
    /* JADX WARN: Type inference failed for: r20v9 */
    /* JADX WARN: Type inference failed for: r23v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r23v10 */
    /* JADX WARN: Type inference failed for: r23v2 */
    /* JADX WARN: Type inference failed for: r23v20 */
    /* JADX WARN: Type inference failed for: r23v4 */
    /* JADX WARN: Type inference failed for: r23v5 */
    /* JADX WARN: Type inference failed for: r23v6 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int relayoutWindowInner(com.android.server.wm.Session r44, android.view.IWindow r45, android.view.WindowManager.LayoutParams r46, int r47, int r48, int r49, int r50, int r51, int r52, android.window.ClientWindowFrames r53, android.util.MergedConfiguration r54, android.view.SurfaceControl r55, android.view.InsetsState r56, android.view.InsetsSourceControl.Array r57, android.os.Bundle r58, android.view.WindowRelayoutResult r59) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2558
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerService.relayoutWindowInner(com.android.server.wm.Session, android.view.IWindow, android.view.WindowManager$LayoutParams, int, int, int, int, int, int, android.window.ClientWindowFrames, android.util.MergedConfiguration, android.view.SurfaceControl, android.view.InsetsState, android.view.InsetsSourceControl$Array, android.os.Bundle, android.view.WindowRelayoutResult):int");
    }

    private void getInsetsSourceControls(com.android.server.wm.WindowState win, android.view.InsetsSourceControl.Array outArray) {
        win.fillInsetsSourceControls(outArray, true);
        outArray.setParcelableFlags(1);
    }

    private void tryStartExitingAnimation(com.android.server.wm.WindowState win, com.android.server.wm.WindowStateAnimator winAnimator) {
        int transit = 2;
        if (win.mAttrs.type == 3) {
            transit = 5;
        }
        if (win.inTransition()) {
            this.mWindowManagerServiceExt.tryAddActivityToAnimationSourceWhenStartExitingAnimation(win);
        }
        if (win.isVisible() && win.isDisplayed() && win.mDisplayContent.okToAnimate()) {
            java.lang.String reason = null;
            if (winAnimator.applyAnimationLocked(transit, false)) {
                reason = "applyAnimation";
            } else if (win.isSelfAnimating(0, 16)) {
                reason = "selfAnimating";
            } else if (win.mTransitionController.isShellTransitionsEnabled()) {
                if (win.mActivityRecord != null && win.mActivityRecord.inTransition()) {
                    if (this.mWindowManagerServiceExt.checkExitingAnimationRationality(win)) {
                        win.mTransitionController.mAnimatingExitWindows.add(win);
                    }
                    reason = "inTransition";
                }
            } else if (win.isAnimating(3, 9)) {
                reason = "inLegacyTransition";
            }
            if (reason != null) {
                win.mAnimatingExit = true;
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(reason);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(win);
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -255991894956556845L, 0, null, protoLogParam0, protoLogParam1);
                }
            }
        }
        if (!win.mAnimatingExit) {
            boolean stopped = win.mActivityRecord == null || win.mActivityRecord.mAppStopped;
            win.mDestroying = true;
            if (this.mWindowManagerServiceExt.shouldWindowSurfaceSaved(win, win.getDisplayContent())) {
                if (!this.mDestroySurface.contains(win)) {
                    this.mDestroySurface.add(win);
                }
                if (!this.mWindowManagerServiceExt.isWindowSurfaceSaved(win)) {
                    this.mWindowManagerServiceExt.getDestroySavedSurface().add(win);
                }
            } else if (!this.mWindowManagerServiceExt.isWindowSurfaceSaved(win) || !this.mWindowManagerServiceExt.isResidentWindowSurface(win)) {
                win.destroySurface(false, stopped);
            }
        }
        if (this.mAccessibilityController.hasCallbacks()) {
            this.mAccessibilityController.onWindowTransition(win, transit);
        }
    }

    private int createSurfaceControl(android.view.SurfaceControl outSurfaceControl, int result, com.android.server.wm.WindowState win, com.android.server.wm.WindowStateAnimator winAnimator) {
        if (!win.mHasSurface) {
            result |= 2;
        }
        try {
            android.os.Trace.traceBegin(32L, "createSurfaceControl");
            com.android.server.wm.WindowSurfaceController surfaceController = winAnimator.createSurfaceLocked();
            if (surfaceController != null) {
                surfaceController.getSurfaceControl(outSurfaceControl);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(outSurfaceControl);
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, 6555160513135851764L, 0, null, protoLogParam0);
                }
            } else {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(win);
                    com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -196459205494031145L, 0, "Failed to create surface control for %s", protoLogParam02);
                }
                outSurfaceControl.release();
            }
            return result;
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    public boolean outOfMemoryWindow(com.android.server.wm.Session session, android.view.IWindow client) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState win = windowForClientLocked(session, client, false);
                    if (win != null) {
                        boolean zReclaimSomeSurfaceMemory = this.mRoot.reclaimSomeSurfaceMemory(win.mWinAnimator, "from-client", false);
                        resetPriorityAfterLockedSection();
                        return zReclaimSomeSurfaceMemory;
                    }
                    resetPriorityAfterLockedSection();
                    return false;
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    void finishDrawingWindow(com.android.server.wm.Session session, android.view.IWindow client, android.view.SurfaceControl.Transaction postDrawTransaction, int seqId) throws java.lang.Throwable {
        if (postDrawTransaction != null) {
            postDrawTransaction.sanitize(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                        try {
                            com.android.server.wm.WindowState win = windowForClientLocked(session, client, false);
                            this.mAtmService.mTaskSupervisor.getWrapper().getExtImpl().markStartingWindowDrawnIfNeed(win);
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[0]) {
                                java.lang.String protoLogParam0 = java.lang.String.valueOf(win);
                                java.lang.String protoLogParam1 = java.lang.String.valueOf(win != null ? win.mWinAnimator.drawStateToString() : "null");
                                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -5512006943172316333L, 0, null, protoLogParam0, protoLogParam1);
                            }
                            if (win != null && win.finishDrawing(postDrawTransaction, seqId)) {
                                if (win.hasWallpaper()) {
                                    win.getDisplayContent().pendingLayoutChanges |= 4;
                                }
                                win.setDisplayLayoutNeeded();
                                this.mWindowPlacerLocked.requestTraversal();
                            }
                            if (win == null && postDrawTransaction != null) {
                                android.util.Slog.d(TAG, "finishDrawingWindow force apply postDrawTransaction,client=" + client + "," + session);
                                postDrawTransaction.apply();
                            }
                            resetPriorityAfterLockedSection();
                            android.os.Binder.restoreCallingIdentity(origId);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            resetPriorityAfterLockedSection();
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    android.os.Binder.restoreCallingIdentity(origId);
                    throw th;
                }
            }
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
    }

    boolean checkCallingPermission(java.lang.String permission, java.lang.String func) {
        return checkCallingPermission(permission, func, true);
    }

    boolean checkCallingPermission(java.lang.String permission, java.lang.String func, boolean printLog) {
        if (android.os.Binder.getCallingPid() == MY_PID || this.mContext.checkCallingPermission(permission) == 0) {
            return true;
        }
        if (!printLog || !com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
            return false;
        }
        java.lang.String protoLogParam0 = java.lang.String.valueOf(func);
        long protoLogParam1 = android.os.Binder.getCallingPid();
        long protoLogParam2 = android.os.Binder.getCallingUid();
        java.lang.String protoLogParam3 = java.lang.String.valueOf(permission);
        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -2577785761087081584L, 20, "Permission Denial: %s from pid=%d, uid=%d requires %s", protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2), protoLogParam3);
        return false;
    }

    public void addWindowToken(android.os.IBinder binder, int type, int displayId, android.os.Bundle options) throws java.lang.Throwable {
        if (!checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "addWindowToken()")) {
            throw new java.lang.SecurityException("Requires MANAGE_APP_TOKENS permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    com.android.server.wm.DisplayContent dc = getDisplayContentOrCreate(displayId, null);
                    if (dc == null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                            java.lang.String protoLogParam0 = java.lang.String.valueOf(binder);
                            long protoLogParam1 = displayId;
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 4547566763172245740L, 4, "addWindowToken: Attempted to add token: %s for non-exiting displayId=%d", protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.WindowToken token = dc.getWindowToken(binder);
                    if (token != null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                            java.lang.String protoLogParam02 = java.lang.String.valueOf(binder);
                            java.lang.String protoLogParam12 = java.lang.String.valueOf(token);
                            long protoLogParam2 = displayId;
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -972832559831959983L, 16, "addWindowToken: Attempted to add binder token: %s for already created window token: %s displayId=%d", protoLogParam02, protoLogParam12, java.lang.Long.valueOf(protoLogParam2));
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    if (type == 2013) {
                        new com.android.server.wm.WallpaperWindowToken(this, binder, true, dc, true, options);
                    } else {
                        new com.android.server.wm.WindowToken.Builder(this, binder, type).setDisplayContent(dc).setPersistOnEmpty(true).setOwnerCanManageAppTokens(true).setOptions(options).build();
                    }
                    resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    th = th;
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public android.window.WindowContextInfo attachWindowContextToDisplayArea(android.app.IApplicationThread appThread, android.os.IBinder clientToken, int type, int displayId, android.os.Bundle options) throws java.lang.Throwable {
        java.util.Objects.requireNonNull(appThread);
        java.util.Objects.requireNonNull(clientToken);
        boolean callerCanManageAppTokens = checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "attachWindowContextToDisplayArea", false);
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            try {
                synchronized (windowManagerGlobalLock) {
                    try {
                        com.android.server.wm.WindowProcessController wpc = this.mAtmService.getProcessController(appThread);
                        if (wpc == null) {
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                                long protoLogParam0 = callingPid;
                                long protoLogParam1 = callingUid;
                                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 8372202339190060748L, 5, "attachWindowContextToDisplayArea: calling from non-existing process pid=%d uid=%d", java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
                            }
                            resetPriorityAfterLockedSection();
                            android.os.Binder.restoreCallingIdentity(origId);
                            return null;
                        }
                        com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContentOrCreate(displayId);
                        if (dc == null) {
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                                long protoLogParam02 = displayId;
                                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 1904306629015452865L, 1, "attachWindowContextToDisplayArea: trying to attach to a non-existing display:%d", java.lang.Long.valueOf(protoLogParam02));
                            }
                            resetPriorityAfterLockedSection();
                            android.os.Binder.restoreCallingIdentity(origId);
                            return null;
                        }
                        com.android.server.wm.DisplayArea<?> da = dc.findAreaForWindowType(type, options, callerCanManageAppTokens, false);
                        android.content.res.Configuration hookConfiguration = this.mWindowManagerServiceExt.hookRegisterWindowContainerListener(da, this.mWindowContextListenerController, wpc, clientToken, type, options);
                        if (hookConfiguration != null) {
                            android.window.WindowContextInfo windowContextInfo = new android.window.WindowContextInfo(hookConfiguration, displayId);
                            resetPriorityAfterLockedSection();
                            android.os.Binder.restoreCallingIdentity(origId);
                            return windowContextInfo;
                        }
                        this.mWindowContextListenerController.registerWindowContainerListener(wpc, clientToken, da, type, options, false);
                        android.window.WindowContextInfo windowContextInfo2 = new android.window.WindowContextInfo(da.getConfiguration(), displayId);
                        resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(origId);
                        return windowContextInfo2;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        try {
                            resetPriorityAfterLockedSection();
                            throw th;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            android.os.Binder.restoreCallingIdentity(origId);
                            throw th;
                        }
                    }
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
    }

    public android.window.WindowContextInfo attachWindowContextToDisplayContent(android.app.IApplicationThread appThread, android.os.IBinder clientToken, int displayId) throws java.lang.Throwable {
        java.util.Objects.requireNonNull(appThread);
        java.util.Objects.requireNonNull(clientToken);
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            try {
                synchronized (windowManagerGlobalLock) {
                    try {
                        com.android.server.wm.WindowProcessController wpc = this.mAtmService.getProcessController(appThread);
                        if (wpc == null) {
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                                long protoLogParam0 = callingPid;
                                long protoLogParam1 = callingUid;
                                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -6845859096032432107L, 5, "attachWindowContextToDisplayContent: calling from non-existing process pid=%d uid=%d", java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
                            }
                            resetPriorityAfterLockedSection();
                            android.os.Binder.restoreCallingIdentity(origId);
                            return null;
                        }
                        com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(displayId);
                        if (dc == null) {
                            if (callingPid != MY_PID) {
                                throw new android.view.WindowManager.InvalidDisplayException("attachWindowContextToDisplayContent: trying to attach to a non-existing display:" + displayId);
                            }
                            resetPriorityAfterLockedSection();
                            android.os.Binder.restoreCallingIdentity(origId);
                            return null;
                        }
                        this.mWindowContextListenerController.registerWindowContainerListener(wpc, clientToken, dc, -1, null, false);
                        android.window.WindowContextInfo windowContextInfo = new android.window.WindowContextInfo(dc.getConfiguration(), displayId);
                        resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(origId);
                        return windowContextInfo;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        try {
                            resetPriorityAfterLockedSection();
                            throw th;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            android.os.Binder.restoreCallingIdentity(origId);
                            throw th;
                        }
                    }
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
    }

    /* JADX WARN: Finally extract failed */
    public android.window.WindowContextInfo attachWindowContextToWindowToken(android.app.IApplicationThread appThread, android.os.IBinder clientToken, android.os.IBinder token) {
        java.util.Objects.requireNonNull(appThread);
        java.util.Objects.requireNonNull(clientToken);
        java.util.Objects.requireNonNull(token);
        boolean callerCanManageAppTokens = checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "attachWindowContextToWindowToken", false);
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowProcessController wpc = this.mAtmService.getProcessController(appThread);
                    if (wpc == null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                            long protoLogParam0 = callingPid;
                            long protoLogParam1 = callingUid;
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 1473791807245791604L, 5, "attachWindowContextToWindowToken: calling from non-existing process pid=%d uid=%d", java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
                        }
                        resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(origId);
                        return null;
                    }
                    com.android.server.wm.WindowToken windowToken = this.mRoot.getWindowToken(token);
                    if (windowToken == null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                            java.lang.String protoLogParam02 = java.lang.String.valueOf(token);
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -2056866750160555704L, 0, "Then token:%s is invalid. It might be removed", protoLogParam02);
                        }
                        resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(origId);
                        return null;
                    }
                    int type = this.mWindowContextListenerController.getWindowType(clientToken);
                    if (type == -1) {
                        throw new java.lang.IllegalArgumentException("The clientToken:" + clientToken + " should have been attached.");
                    }
                    if (type != windowToken.windowType) {
                        throw new java.lang.IllegalArgumentException("The WindowToken's type should match the created WindowContext's type. WindowToken's type is " + windowToken.windowType + ", while WindowContext's is " + type);
                    }
                    if (!this.mWindowContextListenerController.assertCallerCanModifyListener(clientToken, callerCanManageAppTokens, callingUid)) {
                        resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(origId);
                        return null;
                    }
                    this.mWindowContextListenerController.registerWindowContainerListener(wpc, clientToken, windowToken, windowToken.windowType, windowToken.mOptions, false);
                    android.window.WindowContextInfo windowContextInfo = new android.window.WindowContextInfo(windowToken.getConfiguration(), windowToken.getDisplayContent().getDisplayId());
                    resetPriorityAfterLockedSection();
                    android.os.Binder.restoreCallingIdentity(origId);
                    return windowContextInfo;
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } catch (java.lang.Throwable th2) {
            android.os.Binder.restoreCallingIdentity(origId);
            throw th2;
        }
    }

    public void detachWindowContext(android.os.IBinder clientToken) {
        java.util.Objects.requireNonNull(clientToken);
        boolean callerCanManageAppTokens = checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "detachWindowContext", false);
        int callingUid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (this.mWindowContextListenerController.assertCallerCanModifyListener(clientToken, callerCanManageAppTokens, callingUid)) {
                        com.android.server.wm.WindowContainer<?> container = this.mWindowContextListenerController.getContainer(clientToken);
                        this.mWindowContextListenerController.unregisterWindowContainerListener(clientToken);
                        com.android.server.wm.WindowToken token = container.asWindowToken();
                        if (token != null && token.isFromClient()) {
                            removeWindowToken(token.token, token.getDisplayContent().getDisplayId());
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public boolean isWindowToken(android.os.IBinder binder) {
        boolean z;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                z = this.mRoot.getWindowToken(binder) != null;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return z;
    }

    void removeWindowToken(android.os.IBinder binder, boolean removeWindows, boolean animateExit, int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(displayId);
                if (dc == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(binder);
                        long protoLogParam1 = displayId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -1045756671264607145L, 4, "removeWindowToken: Attempted to remove token: %s for non-exiting displayId=%d", protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                com.android.server.wm.WindowToken token = dc.removeWindowToken(binder, animateExit);
                if (token == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        java.lang.String protoLogParam02 = java.lang.String.valueOf(binder);
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 874825105313641295L, 0, "removeWindowToken: Attempted to remove non-existing token: %s", protoLogParam02);
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (removeWindows) {
                    token.removeAllWindowsIfPossible();
                }
                dc.getInputMonitor().updateInputWindowsLw(true);
                resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void removeWindowToken(android.os.IBinder binder, int displayId) {
        if (!checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "removeWindowToken()")) {
            throw new java.lang.SecurityException("Requires MANAGE_APP_TOKENS permission");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            removeWindowToken(binder, false, true, displayId);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void moveWindowTokenToDisplay(android.os.IBinder binder, int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContentOrCreate(displayId);
                if (dc == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(binder);
                        long protoLogParam1 = displayId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 5128669121055635771L, 4, "moveWindowTokenToDisplay: Attempted to move token: %s to non-exiting displayId=%d", protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                com.android.server.wm.WindowToken token = this.mRoot.getWindowToken(binder);
                if (token == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        java.lang.String protoLogParam02 = java.lang.String.valueOf(binder);
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 6497954191906583839L, 0, "moveWindowTokenToDisplay: Attempted to move non-existing token: %s", protoLogParam02);
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (token.getDisplayContent() == dc) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        java.lang.String protoLogParam03 = java.lang.String.valueOf(binder);
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 2865882097969084039L, 0, "moveWindowTokenToDisplay: Cannot move to the original display for token: %s", protoLogParam03);
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                dc.reParentWindowToken(token);
                resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void prepareAppTransitionNone() {
        if (!checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "prepareAppTransition()")) {
            throw new java.lang.SecurityException("Requires MANAGE_APP_TOKENS permission");
        }
        getDefaultDisplayContentLocked().prepareAppTransition(0);
    }

    public void overridePendingAppTransitionMultiThumbFuture(android.view.IAppTransitionAnimationSpecsFuture specsFuture, android.os.IRemoteCallback callback, boolean scaleUp, int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    android.util.Slog.w(TAG, "Attempted to call overridePendingAppTransitionMultiThumbFuture for the display " + displayId + " that does not exist.");
                    resetPriorityAfterLockedSection();
                } else {
                    displayContent.mAppTransition.overridePendingAppTransitionMultiThumbFuture(specsFuture, callback, scaleUp);
                    resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void overridePendingAppTransitionRemote(android.view.RemoteAnimationAdapter remoteAnimationAdapter, int displayId) {
        if (!checkCallingPermission("android.permission.CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS", "overridePendingAppTransitionRemote()")) {
            throw new java.lang.SecurityException("Requires CONTROL_REMOTE_APP_TRANSITION_ANIMATIONS permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    android.util.Slog.w(TAG, "Attempted to call overridePendingAppTransitionRemote for the display " + displayId + " that does not exist.");
                    resetPriorityAfterLockedSection();
                } else {
                    remoteAnimationAdapter.setCallingPidUid(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
                    displayContent.mAppTransition.overridePendingAppTransitionRemote(remoteAnimationAdapter);
                    resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void endProlongedAnimations() {
    }

    public void executeAppTransition() {
        if (!checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "executeAppTransition()")) {
            throw new java.lang.SecurityException("Requires MANAGE_APP_TOKENS permission");
        }
        getDefaultDisplayContentLocked().executeAppTransition();
    }

    void initializeRecentsAnimation(int targetActivityType, android.view.IRecentsAnimationRunner recentsAnimationRunner, com.android.server.wm.RecentsAnimationController.RecentsAnimationCallbacks callbacks, int displayId, android.util.SparseBooleanArray recentTaskIds, com.android.server.wm.ActivityRecord targetActivity) {
        this.mRecentsAnimationController = new com.android.server.wm.RecentsAnimationController(this, recentsAnimationRunner, callbacks, displayId);
        this.mRoot.getDisplayContent(displayId).mAppTransition.updateBooster();
        this.mRecentsAnimationController.initialize(targetActivityType, recentTaskIds, targetActivity);
    }

    void setRecentsAnimationController(com.android.server.wm.RecentsAnimationController controller) {
        this.mRecentsAnimationController = controller;
    }

    com.android.server.wm.RecentsAnimationController getRecentsAnimationController() {
        return this.mRecentsAnimationController;
    }

    void cancelRecentsAnimation(int reorderMode, java.lang.String reason) {
        if (this.mRecentsAnimationController != null) {
            this.mRecentsAnimationController.cancelAnimation(reorderMode, reason);
        }
    }

    void cleanupRecentsAnimation(int reorderMode) {
        if (this.mRecentsAnimationController != null) {
            com.android.server.wm.RecentsAnimationController controller = this.mRecentsAnimationController;
            this.mRecentsAnimationController = null;
            controller.cleanupAnimation(reorderMode);
            com.android.server.wm.DisplayContent dc = getDefaultDisplayContentLocked();
            if (dc.mAppTransition.isTransitionSet()) {
                dc.mSkipAppTransitionAnimation = true;
            }
            dc.forAllWindowContainers(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.WindowManagerService.lambda$cleanupRecentsAnimation$3((com.android.server.wm.WindowContainer) obj);
                }
            });
        }
    }

    static /* synthetic */ void lambda$cleanupRecentsAnimation$3(com.android.server.wm.WindowContainer wc) {
        if (wc.isAnimating(1, 1)) {
            wc.cancelAnimation();
        }
    }

    boolean isRecentsAnimationTarget(com.android.server.wm.ActivityRecord r) {
        return this.mRecentsAnimationController != null && this.mRecentsAnimationController.isTargetApp(r);
    }

    boolean isValidPictureInPictureAspectRatio(com.android.server.wm.DisplayContent displayContent, float aspectRatio) {
        return displayContent.getPinnedTaskController().isValidPictureInPictureAspectRatio(aspectRatio);
    }

    boolean isValidExpandedPictureInPictureAspectRatio(com.android.server.wm.DisplayContent displayContent, float aspectRatio) {
        return displayContent.getPinnedTaskController().isValidExpandedPictureInPictureAspectRatio(aspectRatio);
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void notifyKeyguardTrustedChanged() {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (this.mAtmService.mKeyguardController.isKeyguardShowing(0)) {
                        this.mRoot.ensureActivitiesVisible();
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void screenTurningOff(int displayId, com.android.server.policy.WindowManagerPolicy.ScreenOffListener listener) {
        this.mWindowManagerServiceExt.beginHookscreenTurningOff();
        this.mTaskSnapshotController.screenTurningOff(displayId, listener);
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void triggerAnimationFailsafe() {
        this.mH.sendEmptyMessage(60);
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void onKeyguardShowingAndNotOccludedChanged() {
        this.mH.sendEmptyMessage(61);
        dispatchKeyguardLockedState();
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void onPowerKeyDown(final boolean isScreenOn) {
        this.mWindowManagerServiceExt.onPowerKeyDown(isScreenOn);
        this.mRoot.forAllDisplayPolicies(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.DisplayPolicy) obj).onPowerKeyDown(isScreenOn);
            }
        });
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void onUserSwitched() {
        this.mSettingsObserver.updateSystemUiSettings(true);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.forAllDisplayPolicies(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda13
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wm.DisplayPolicy) obj).resetSystemBarAttributes();
                    }
                });
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void moveDisplayToTopIfAllowed(int displayId) {
        moveDisplayToTopInternal(displayId);
        syncInputTransactions(true);
    }

    void moveDisplayToTopInternal(int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent != null && this.mRoot.getTopChild() != displayContent) {
                    if (!displayContent.canStealTopFocus()) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[2]) {
                            long protoLogParam0 = displayId;
                            long protoLogParam1 = this.mRoot.getTopFocusedDisplayContent().getDisplayId();
                            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -886583195545553099L, 5, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    displayContent.getParent().positionChildAt(Integer.MAX_VALUE, displayContent, true);
                }
                resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public boolean isAppTransitionStateIdle() {
        return getDefaultDisplayContentLocked().mAppTransition.isIdle();
    }

    public void startFreezingScreen(int exitAnim, int enterAnim) {
        if (!checkCallingPermission("android.permission.FREEZE_SCREEN", "startFreezingScreen()")) {
            throw new java.lang.SecurityException("Requires FREEZE_SCREEN permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!this.mClientFreezingScreen) {
                    this.mClientFreezingScreen = true;
                    long origId = android.os.Binder.clearCallingIdentity();
                    try {
                        startFreezingDisplay(exitAnim, enterAnim);
                        if (!this.mDisplayFrozen) {
                            this.mClientFreezingScreen = false;
                            resetPriorityAfterLockedSection();
                            return;
                        } else {
                            this.mH.removeMessages(30);
                            this.mH.sendEmptyMessageDelayed(30, 5000L);
                            android.os.Binder.restoreCallingIdentity(origId);
                        }
                    } finally {
                        android.os.Binder.restoreCallingIdentity(origId);
                    }
                }
                resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void stopFreezingScreen() {
        if (!checkCallingPermission("android.permission.FREEZE_SCREEN", "stopFreezingScreen()")) {
            throw new java.lang.SecurityException("Requires FREEZE_SCREEN permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mClientFreezingScreen) {
                    this.mClientFreezingScreen = false;
                    this.mLastFinishedFreezeSource = "client";
                    long origId = android.os.Binder.clearCallingIdentity();
                    try {
                        stopFreezingDisplayLocked();
                        android.os.Binder.restoreCallingIdentity(origId);
                    } catch (java.lang.Throwable th) {
                        android.os.Binder.restoreCallingIdentity(origId);
                        throw th;
                    }
                }
            } catch (java.lang.Throwable th2) {
                resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void disableKeyguard(android.os.IBinder token, java.lang.String tag, int userId) {
        int userId2 = this.mAmInternal.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, 2, "disableKeyguard", (java.lang.String) null);
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DISABLE_KEYGUARD") != 0) {
            throw new java.lang.SecurityException("Requires DISABLE_KEYGUARD permission");
        }
        int callingUid = android.os.Binder.getCallingUid();
        long origIdentity = android.os.Binder.clearCallingIdentity();
        try {
            this.mKeyguardDisableHandler.disableKeyguard(token, tag, callingUid, userId2);
        } finally {
            android.os.Binder.restoreCallingIdentity(origIdentity);
        }
    }

    public void reenableKeyguard(android.os.IBinder token, int userId) {
        int userId2 = this.mAmInternal.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, 2, "reenableKeyguard", (java.lang.String) null);
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DISABLE_KEYGUARD") != 0) {
            throw new java.lang.SecurityException("Requires DISABLE_KEYGUARD permission");
        }
        java.util.Objects.requireNonNull(token, "token is null");
        int callingUid = android.os.Binder.getCallingUid();
        long origIdentity = android.os.Binder.clearCallingIdentity();
        try {
            this.mKeyguardDisableHandler.reenableKeyguard(token, callingUid, userId2);
        } finally {
            android.os.Binder.restoreCallingIdentity(origIdentity);
        }
    }

    public void exitKeyguardSecurely(final android.view.IOnKeyguardExitResult callback) {
        exitKeyguardSecurely_enforcePermission();
        if (callback == null) {
            throw new java.lang.IllegalArgumentException("callback == null");
        }
        this.mPolicy.exitKeyguardSecurely(new com.android.server.policy.WindowManagerPolicy.OnKeyguardExitResult() { // from class: com.android.server.wm.WindowManagerService.9
            @Override // com.android.server.policy.WindowManagerPolicy.OnKeyguardExitResult
            public void onKeyguardExitResult(boolean success) {
                try {
                    callback.onKeyguardExitResult(success);
                } catch (android.os.RemoteException e) {
                }
            }
        });
    }

    public boolean isKeyguardLocked() {
        return this.mPolicy.isKeyguardLocked();
    }

    public boolean isKeyguardShowingAndNotOccluded() {
        return this.mPolicy.isKeyguardShowingAndNotOccluded();
    }

    public boolean isKeyguardSecure(int userId) {
        if (userId != android.os.UserHandle.getCallingUserId() && !checkCallingPermission("android.permission.INTERACT_ACROSS_USERS", "isKeyguardSecure")) {
            throw new java.lang.SecurityException("Requires INTERACT_ACROSS_USERS permission");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            return this.mPolicy.isKeyguardSecure(userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void dismissKeyguard(com.android.internal.policy.IKeyguardDismissCallback callback, java.lang.CharSequence message) {
        if (!checkCallingPermission("android.permission.CONTROL_KEYGUARD", "dismissKeyguard")) {
            throw new java.lang.SecurityException("Requires CONTROL_KEYGUARD permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!android.service.dreams.Flags.dreamHandlesConfirmKeys() && getDefaultDisplayContentLocked().getDisplayPolicy().isShowingDreamLw()) {
                    this.mAtmService.mTaskSupervisor.wakeUp("leaveDream");
                }
                this.mPolicy.dismissKeyguardLw(callback, message);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void addKeyguardLockedStateListener(com.android.internal.policy.IKeyguardLockedStateListener listener) {
        enforceSubscribeToKeyguardLockedStatePermission();
        boolean registered = this.mKeyguardLockedStateListeners.register(listener);
        if (!registered) {
            android.util.Slog.w(TAG, "Failed to register listener: " + listener);
        }
    }

    public void removeKeyguardLockedStateListener(com.android.internal.policy.IKeyguardLockedStateListener listener) {
        enforceSubscribeToKeyguardLockedStatePermission();
        this.mKeyguardLockedStateListeners.unregister(listener);
    }

    private void enforceSubscribeToKeyguardLockedStatePermission() {
        if (this.mWindowManagerServiceExt.checkOplusWindowPermission(this)) {
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE", "android.permission.SUBSCRIBE_TO_KEYGUARD_LOCKED_STATE permission required to subscribe to keyguard locked state changes");
    }

    private void dispatchKeyguardLockedState() {
        this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda28
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dispatchKeyguardLockedState$5();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchKeyguardLockedState$5() {
        boolean isKeyguardLocked = this.mPolicy.isKeyguardShowing();
        if (this.mDispatchedKeyguardLockedState == isKeyguardLocked) {
            return;
        }
        int n = this.mKeyguardLockedStateListeners.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                this.mKeyguardLockedStateListeners.getBroadcastItem(i).onKeyguardLockedStateChanged(isKeyguardLocked);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mKeyguardLockedStateListeners.finishBroadcast();
        this.mDispatchedKeyguardLockedState = isKeyguardLocked;
    }

    void dispatchImeTargetOverlayVisibilityChanged(final android.os.IBinder token, final int windowType, final boolean visible, final boolean removed) {
        if (this.mImeTargetChangeListener != null) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                android.util.Slog.d(TAG, "onImeTargetOverlayVisibilityChanged, win=" + this.mWindowMap.get(token) + ", type=" + android.view.ViewDebug.intToString(android.view.WindowManager.LayoutParams.class, "type", windowType) + "visible=" + visible + ", removed=" + removed);
            }
            this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$dispatchImeTargetOverlayVisibilityChanged$6(token, windowType, visible, removed);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchImeTargetOverlayVisibilityChanged$6(android.os.IBinder token, int windowType, boolean visible, boolean removed) {
        this.mImeTargetChangeListener.onImeTargetOverlayVisibilityChanged(token, windowType, visible, removed);
    }

    void dispatchImeInputTargetVisibilityChanged(final android.os.IBinder token, final boolean visible, final boolean removed) {
        if (this.mImeTargetChangeListener != null) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                android.util.Slog.d(TAG, "onImeInputTargetVisibilityChanged, win=" + this.mWindowMap.get(token) + "visible=" + visible + ", removed=" + removed);
            }
            this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$dispatchImeInputTargetVisibilityChanged$7(token, visible, removed);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchImeInputTargetVisibilityChanged$7(android.os.IBinder token, boolean visible, boolean removed) {
        this.mImeTargetChangeListener.onImeInputTargetVisibilityChanged(token, visible, removed);
    }

    public void setSwitchingUser(boolean switching) {
        if (!checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "setSwitchingUser()")) {
            throw new java.lang.SecurityException("Requires INTERACT_ACROSS_USERS_FULL permission");
        }
        this.mPolicy.setSwitchingUser(switching);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mSwitchingUser = switching;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void showGlobalActions() {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "showGlobalActions()")) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        this.mPolicy.showGlobalActions();
    }

    public void closeSystemDialogs(java.lang.String reason) {
        int callingPid = android.os.Binder.getCallingPid();
        int callingUid = android.os.Binder.getCallingUid();
        if (!this.mAtmService.checkCanCloseSystemDialogs(callingPid, callingUid, null)) {
            return;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.closeSystemDialogs(reason);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void setAnimationScale(int which, float scale) {
        if (!checkCallingPermission("android.permission.SET_ANIMATION_SCALE", "setAnimationScale()")) {
            throw new java.lang.SecurityException("Requires SET_ANIMATION_SCALE permission");
        }
        float scale2 = android.view.WindowManager.fixScale(scale);
        switch (which) {
            case 0:
                this.mWindowAnimationScaleSetting = scale2;
                break;
            case 1:
                this.mTransitionAnimationScaleSetting = scale2;
                break;
            case 2:
                this.mAnimatorDurationScaleSetting = scale2;
                break;
        }
        this.mH.sendEmptyMessage(14);
    }

    public void setAnimationScales(float[] scales) {
        if (!checkCallingPermission("android.permission.SET_ANIMATION_SCALE", "setAnimationScale()")) {
            throw new java.lang.SecurityException("Requires SET_ANIMATION_SCALE permission");
        }
        if (scales != null) {
            if (scales.length >= 1) {
                this.mWindowAnimationScaleSetting = android.view.WindowManager.fixScale(scales[0]);
            }
            if (scales.length >= 2) {
                this.mTransitionAnimationScaleSetting = android.view.WindowManager.fixScale(scales[1]);
            }
            if (scales.length >= 3) {
                this.mAnimatorDurationScaleSetting = android.view.WindowManager.fixScale(scales[2]);
                dispatchNewAnimatorScaleLocked(null);
            }
        }
        this.mH.sendEmptyMessage(14);
    }

    private void setAnimatorDurationScale(float scale) {
        this.mAnimatorDurationScaleSetting = scale;
        android.animation.ValueAnimator.setDurationScale(scale);
    }

    private float animationScalesCheck(int which) {
        if (this.mAnimationsDisabled) {
            return 0.0f;
        }
        if (-1.0f != -1.0f) {
            return -1.0f;
        }
        switch (which) {
            case 0:
                float value = this.mWindowAnimationScaleSetting;
                return value;
            case 1:
                float value2 = this.mTransitionAnimationScaleSetting;
                return value2;
            case 2:
                float value3 = this.mAnimatorDurationScaleSetting;
                return value3;
            default:
                return -1.0f;
        }
    }

    public float getWindowAnimationScaleLocked() {
        return animationScalesCheck(0);
    }

    public float getTransitionAnimationScaleLocked() {
        return animationScalesCheck(1);
    }

    public float getAnimationScale(int which) {
        switch (which) {
            case 0:
                return this.mWindowAnimationScaleSetting;
            case 1:
                return this.mTransitionAnimationScaleSetting;
            case 2:
                return this.mAnimatorDurationScaleSetting;
            default:
                return 0.0f;
        }
    }

    public float[] getAnimationScales() {
        return new float[]{this.mWindowAnimationScaleSetting, this.mTransitionAnimationScaleSetting, this.mAnimatorDurationScaleSetting};
    }

    public float getCurrentAnimatorScale() {
        float f;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                f = this.mAnimationsDisabled ? 0.0f : this.mAnimatorDurationScaleSetting;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return f;
    }

    void dispatchNewAnimatorScaleLocked(com.android.server.wm.Session session) {
        this.mH.obtainMessage(34, session).sendToTarget();
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void registerPointerEventListener(android.view.WindowManagerPolicyConstants.PointerEventListener listener, int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent != null) {
                    displayContent.registerPointerEventListener(listener);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void unregisterPointerEventListener(android.view.WindowManagerPolicyConstants.PointerEventListener listener, int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent != null) {
                    displayContent.unregisterPointerEventListener(listener);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public int getLidState() {
        int sw = this.mInputManager.getSwitchState(-1, -256, 0);
        if (sw > 0) {
            return 0;
        }
        return sw == 0 ? 1 : -1;
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void lockDeviceNow() {
        lockNow(null);
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public int getCameraLensCoverState() {
        int sw = this.mInputManager.getSwitchState(-1, -256, 9);
        if (sw > 0) {
            return 1;
        }
        return sw == 0 ? 0 : -1;
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void shutdown(boolean confirm) {
        com.android.server.power.ShutdownThread.shutdown(android.app.ActivityThread.currentActivityThread().getSystemUiContext(), "userrequested", confirm);
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void reboot(boolean confirm) {
        com.android.server.power.ShutdownThread.reboot(android.app.ActivityThread.currentActivityThread().getSystemUiContext(), "userrequested", confirm);
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void rebootSafeMode(boolean confirm) {
        com.android.server.power.ShutdownThread.rebootSafeMode(android.app.ActivityThread.currentActivityThread().getSystemUiContext(), confirm);
    }

    public void setCurrentUser(int newUserId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.TransitionController controller = this.mAtmService.getTransitionController();
                if (!controller.isCollecting() && controller.isShellTransitionsEnabled()) {
                    controller.requestStartTransition(controller.createTransition(1), null, null, null);
                }
                this.mCurrentUserId = newUserId;
                this.mPolicy.setCurrentUserLw(newUserId);
                this.mKeyguardDisableHandler.setCurrentUser(newUserId);
                this.mRoot.switchUser(newUserId);
                this.mWindowPlacerLocked.performSurfacePlacement();
                com.android.server.wm.DisplayContent displayContent = getDefaultDisplayContentLocked();
                if (this.mDisplayReady) {
                    int forcedDensity = getForcedDisplayDensityForUserLocked(newUserId);
                    int targetDensity = forcedDensity != 0 ? forcedDensity : displayContent.getInitialDisplayDensity();
                    int adjustDensity = this.mWindowManagerServiceExt.adjustDensityForUser(targetDensity, newUserId);
                    displayContent.setForcedDensity(-1 != adjustDensity ? adjustDensity : targetDensity, -2);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    boolean isUserVisible(int userId) {
        return this.mUmInternal.isUserVisible(userId);
    }

    int getUserAssignedToDisplay(int displayId) {
        return this.mUmInternal.getUserAssignedToDisplay(displayId);
    }

    boolean shouldPlacePrimaryHomeOnDisplay(int displayId) {
        int userId = this.mUmInternal.getUserAssignedToDisplay(displayId);
        return shouldPlacePrimaryHomeOnDisplay(displayId, userId);
    }

    boolean shouldPlacePrimaryHomeOnDisplay(int displayId, int userId) {
        return this.mUmInternal.getMainDisplayAssignedToUser(userId) == displayId;
    }

    public void enableScreenAfterBoot() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BOOT_enabled[2]) {
                    boolean protoLogParam0 = this.mDisplayEnabled;
                    boolean protoLogParam1 = this.mForceDisplayEnabled;
                    boolean protoLogParam2 = this.mShowingBootMessages;
                    boolean protoLogParam3 = this.mSystemBooted;
                    java.lang.String protoLogParam4 = java.lang.String.valueOf(new java.lang.RuntimeException("here").fillInStackTrace());
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, -1557387535886241553L, 255, null, java.lang.Boolean.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3), protoLogParam4);
                }
                boolean protoLogParam02 = this.mSystemBooted;
                if (protoLogParam02) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                this.mSystemBooted = true;
                hideBootMessagesLocked();
                this.mH.sendEmptyMessageDelayed(23, 30000L);
                resetPriorityAfterLockedSection();
                this.mPolicy.systemBooted();
                performEnableScreen();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void enableScreenIfNeeded() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                enableScreenIfNeededLocked();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    void enableScreenIfNeededLocked() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BOOT_enabled[2]) {
            boolean protoLogParam0 = this.mDisplayEnabled;
            boolean protoLogParam1 = this.mForceDisplayEnabled;
            boolean protoLogParam2 = this.mShowingBootMessages;
            boolean protoLogParam3 = this.mSystemBooted;
            java.lang.String protoLogParam4 = java.lang.String.valueOf(new java.lang.RuntimeException("here").fillInStackTrace());
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, -6467850045030187736L, 255, null, java.lang.Boolean.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3), protoLogParam4);
        }
        boolean protoLogParam02 = this.mDisplayEnabled;
        if (protoLogParam02) {
            return;
        }
        if (!this.mSystemBooted && !this.mShowingBootMessages) {
            return;
        }
        this.mH.sendEmptyMessage(16);
    }

    public void performBootTimeout() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mDisplayEnabled) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 179762478329442868L, 0, "***** BOOT TIMEOUT: forcing display enabled", null);
                }
                this.mForceDisplayEnabled = true;
                resetPriorityAfterLockedSection();
                performEnableScreen();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void onSystemUiStarted() {
        this.mPolicy.onSystemUiStarted();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performEnableScreen() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BOOT_enabled[2]) {
                    boolean protoLogParam0 = this.mDisplayEnabled;
                    boolean protoLogParam1 = this.mForceDisplayEnabled;
                    boolean protoLogParam2 = this.mShowingBootMessages;
                    boolean protoLogParam3 = this.mSystemBooted;
                    java.lang.String protoLogParam4 = java.lang.String.valueOf(new java.lang.RuntimeException("here").fillInStackTrace());
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, -3417569256875279779L, 255, null, java.lang.Boolean.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3), protoLogParam4);
                }
                boolean protoLogParam02 = this.mDisplayEnabled;
                if (protoLogParam02) {
                    return;
                }
                if (!this.mSystemBooted && !this.mShowingBootMessages) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (!this.mShowingBootMessages && !this.mPolicy.canDismissBootAnimation()) {
                    android.util.Slog.i(TAG, " Waiting for mKeyguardDrawComplete");
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (!this.mForceDisplayEnabled) {
                    if (this.mBootWaitForWindowsStartTime < 0) {
                        this.mBootWaitForWindowsStartTime = android.os.SystemClock.elapsedRealtime();
                    }
                    for (int i = this.mRoot.getChildCount() - 1; i >= 0; i--) {
                        if (((com.android.server.wm.DisplayContent) this.mRoot.getChildAt(i)).shouldWaitForSystemDecorWindowsOnBoot()) {
                            android.util.Slog.i(TAG, " Waiting all existing windows have been drawn");
                            resetPriorityAfterLockedSection();
                            return;
                        }
                    }
                    long waitTime = android.os.SystemClock.elapsedRealtime() - this.mBootWaitForWindowsStartTime;
                    this.mBootWaitForWindowsStartTime = -1L;
                    if (waitTime > 10 && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BOOT_enabled[2]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, -7516915153725082358L, 1, null, java.lang.Long.valueOf(waitTime));
                    }
                }
                if (!this.mBootAnimationStopped) {
                    android.os.Trace.asyncTraceBegin(32L, "Stop bootanim", 0);
                    android.os.SystemProperties.set("service.bootanim.exit", "1");
                    android.util.Slog.i(TAG, " Try to stop boot anim");
                    this.mBootAnimationStopped = true;
                }
                if (!this.mForceDisplayEnabled && !checkBootAnimationCompleteLocked()) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BOOT_enabled[2]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, -1541244520024033685L, 0, null, null);
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (!android.view.SurfaceControl.bootFinished()) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 2670150656385758826L, 0, "performEnableScreen: bootFinished() failed.", null);
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                com.android.server.wm.EventLogTags.writeWmBootAnimationDone(android.os.SystemClock.uptimeMillis());
                android.os.Trace.asyncTraceEnd(32L, "Stop bootanim", 0);
                this.mDisplayEnabled = true;
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SCREEN_ON_enabled[2]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, 530628508916855904L, 0, null, null);
                }
                this.mInputManagerCallback.setEventDispatchingLw(this.mEventDispatchingEnabled);
                resetPriorityAfterLockedSection();
                try {
                    this.mActivityManager.bootAnimationComplete();
                } catch (android.os.RemoteException e) {
                }
                this.mPolicy.enableScreenAfterBoot();
                updateRotationUnchecked(false, false);
                this.mWindowManagerServiceExt.endHookperformEnableScreen(this, this.mContext);
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
                boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock2) {
                    try {
                        this.mAtmService.getTransitionController().mIsWaitingForDisplayEnabled = false;
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_TRANSITIONS_enabled[1]) {
                            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_TRANSITIONS, 5477889324043875194L, 0, null, null);
                        }
                    } finally {
                    }
                }
                resetPriorityAfterLockedSection();
            } finally {
                resetPriorityAfterLockedSection();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkBootAnimationCompleteLocked() {
        if (android.os.SystemService.isRunning(BOOT_ANIMATION_SERVICE)) {
            this.mH.removeMessages(37);
            this.mH.sendEmptyMessageDelayed(37, 50L);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BOOT_enabled[2]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, -2061779801633179448L, 0, null, null);
                return false;
            }
            return false;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BOOT_enabled[2]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, -8177456840019985809L, 0, null, null);
            return true;
        }
        return true;
    }

    public void showBootMessage(java.lang.CharSequence msg, boolean always) throws java.lang.Throwable {
        boolean first = false;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BOOT_enabled[2]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(msg);
                        boolean protoLogParam2 = this.mAllowBootMessages;
                        boolean protoLogParam3 = this.mShowingBootMessages;
                        boolean protoLogParam4 = this.mSystemBooted;
                        java.lang.String protoLogParam5 = java.lang.String.valueOf(new java.lang.RuntimeException("here").fillInStackTrace());
                        com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, -333924817004774456L, 1020, null, protoLogParam0, java.lang.Boolean.valueOf(always), java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3), java.lang.Boolean.valueOf(protoLogParam4), protoLogParam5);
                    }
                    boolean protoLogParam1 = this.mAllowBootMessages;
                    if (!protoLogParam1) {
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    if (!this.mShowingBootMessages) {
                        if (!always) {
                            resetPriorityAfterLockedSection();
                            return;
                        }
                        first = true;
                    }
                    if (this.mSystemBooted) {
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    this.mShowingBootMessages = true;
                    this.mPolicy.showBootMessage(msg, always);
                    resetPriorityAfterLockedSection();
                    if (first) {
                        performEnableScreen();
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    public void hideBootMessagesLocked() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BOOT_enabled[2]) {
            boolean protoLogParam0 = this.mDisplayEnabled;
            boolean protoLogParam1 = this.mForceDisplayEnabled;
            boolean protoLogParam2 = this.mShowingBootMessages;
            boolean protoLogParam3 = this.mSystemBooted;
            java.lang.String protoLogParam4 = java.lang.String.valueOf(new java.lang.RuntimeException("here").fillInStackTrace());
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, 2994810644159608200L, 255, null, java.lang.Boolean.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3), protoLogParam4);
        }
        boolean protoLogParam02 = this.mShowingBootMessages;
        if (protoLogParam02) {
            this.mShowingBootMessages = false;
            this.mPolicy.hideBootMessages();
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
    public void setInTouchMode(boolean inTouch, int displayId) throws java.lang.Throwable {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                    if (this.mPerDisplayFocusEnabled && (displayContent == null || displayContent.isInTouchMode() == inTouch)) {
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    boolean displayHasOwnTouchMode = displayContent != null && displayContent.hasOwnFocus();
                    if (displayHasOwnTouchMode && displayContent.isInTouchMode() == inTouch) {
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    int pid = android.os.Binder.getCallingPid();
                    int uid = android.os.Binder.getCallingUid();
                    boolean hasPermission = hasTouchModePermission(pid);
                    long token = android.os.Binder.clearCallingIdentity();
                    try {
                        if (this.mPerDisplayFocusEnabled || displayHasOwnTouchMode) {
                            try {
                                if (this.mInputManager.setInTouchMode(inTouch, pid, uid, hasPermission, displayId)) {
                                    displayContent.setInTouchMode(inTouch);
                                }
                                android.os.Binder.restoreCallingIdentity(token);
                                resetPriorityAfterLockedSection();
                                return;
                            } catch (java.lang.Throwable th) {
                                th = th;
                            }
                        } else {
                            try {
                                int displayCount = this.mRoot.mChildren.size();
                                for (int i = 0; i < displayCount; i++) {
                                    com.android.server.wm.DisplayContent dc = (com.android.server.wm.DisplayContent) this.mRoot.mChildren.get(i);
                                    if (dc.isInTouchMode() != inTouch && !dc.hasOwnFocus() && this.mInputManager.setInTouchMode(inTouch, pid, uid, hasPermission, dc.mDisplayId)) {
                                        dc.setInTouchMode(inTouch);
                                    }
                                }
                                android.os.Binder.restoreCallingIdentity(token);
                                resetPriorityAfterLockedSection();
                                return;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        }
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                    }
                    android.os.Binder.restoreCallingIdentity(token);
                    throw th;
                } catch (java.lang.Throwable th4) {
                    th = th4;
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th5) {
                th = th5;
                resetPriorityAfterLockedSection();
                throw th;
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
    public void setInTouchModeOnAllDisplays(boolean inTouch) {
        int pid = android.os.Binder.getCallingPid();
        int uid = android.os.Binder.getCallingUid();
        boolean hasPermission = hasTouchModePermission(pid);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                for (int i = 0; i < this.mRoot.mChildren.size(); i++) {
                    try {
                        com.android.server.wm.DisplayContent dc = (com.android.server.wm.DisplayContent) this.mRoot.mChildren.get(i);
                        if (dc.isInTouchMode() != inTouch && this.mInputManager.setInTouchMode(inTouch, pid, uid, hasPermission, dc.mDisplayId)) {
                            dc.setInTouchMode(inTouch);
                        }
                    } catch (java.lang.Throwable th) {
                        resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private boolean hasTouchModePermission(int pid) {
        return this.mAtmService.instrumentationSourceHasPermission(pid, "android.permission.MODIFY_TOUCH_MODE_STATE") || checkCallingPermission("android.permission.MODIFY_TOUCH_MODE_STATE", "setInTouchMode()", false);
    }

    public boolean isInTouchMode(int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    boolean z = this.mContext.getResources().getBoolean(android.R.bool.config_defaultPreventScreenTimeoutEnabled);
                    resetPriorityAfterLockedSection();
                    return z;
                }
                boolean zIsInTouchMode = displayContent.isInTouchMode();
                resetPriorityAfterLockedSection();
                return zIsInTouchMode;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void showEmulatorDisplayOverlayIfNeeded() {
        if (this.mContext.getResources().getBoolean(android.R.bool.config_useLegacySplit) && android.os.SystemProperties.getBoolean(PROPERTY_EMULATOR_CIRCULAR, false) && android.os.Build.IS_EMULATOR) {
            this.mH.sendMessage(this.mH.obtainMessage(36));
        }
    }

    public void showEmulatorDisplayOverlay() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (com.android.server.wm.WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
                    android.util.Slog.i(TAG, ">>> showEmulatorDisplayOverlay");
                }
                if (this.mEmulatorDisplayOverlay == null) {
                    this.mEmulatorDisplayOverlay = new com.android.server.wm.EmulatorDisplayOverlay(this.mContext, getDefaultDisplayContentLocked(), (this.mPolicy.getWindowLayerFromTypeLw(2018) * 10000) + 10, this.mTransaction);
                }
                this.mEmulatorDisplayOverlay.setVisibility(true, this.mTransaction);
                this.mTransaction.apply();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void showStrictModeViolation(boolean on) {
        int pid = android.os.Binder.getCallingPid();
        if (!on) {
            this.mH.sendMessage(this.mH.obtainMessage(25, 0, pid));
        } else {
            this.mH.sendMessage(this.mH.obtainMessage(25, 1, pid));
            this.mH.sendMessageDelayed(this.mH.obtainMessage(25, 0, pid), 1000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showStrictModeViolation(int arg, int pid) {
        boolean on = arg != 0;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            if (on) {
                try {
                    if (!this.mRoot.canShowStrictModeViolation(pid)) {
                        resetPriorityAfterLockedSection();
                        return;
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            if (com.android.server.wm.WindowManagerDebugConfig.SHOW_VERBOSE_TRANSACTIONS) {
                android.util.Slog.i(TAG, ">>> showStrictModeViolation");
            }
            if (this.mStrictModeFlash == null) {
                this.mStrictModeFlash = new com.android.server.wm.StrictModeFlash(getDefaultDisplayContentLocked(), this.mTransaction);
            }
            this.mStrictModeFlash.setVisibility(on, this.mTransaction);
            this.mTransaction.apply();
            resetPriorityAfterLockedSection();
        }
    }

    public void setStrictModeVisualIndicatorPreference(java.lang.String value) {
        android.os.SystemProperties.set("persist.sys.strictmode.visual", value);
    }

    public android.graphics.Bitmap screenshotWallpaper() {
        android.graphics.Bitmap bitmapScreenshotWallpaperLocked;
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "screenshotWallpaper()")) {
            throw new java.lang.SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        try {
            android.os.Trace.traceBegin(32L, "screenshotWallpaper");
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(0);
                    bitmapScreenshotWallpaperLocked = dc.mWallpaperController.screenshotWallpaperLocked();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
            return bitmapScreenshotWallpaperLocked;
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    public android.view.SurfaceControl mirrorWallpaperSurface(int displayId) {
        android.view.SurfaceControl surfaceControlMirrorWallpaperSurface;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(displayId);
                surfaceControlMirrorWallpaperSurface = dc.mWallpaperController.mirrorWallpaperSurface();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return surfaceControlMirrorWallpaperSurface;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.window.ScreenCapture.ScreenshotHardwareBuffer takeAssistScreenshot(java.util.Set<java.lang.Integer> windowTypesToExclude) {
        android.window.ScreenCapture.LayerCaptureArgs captureArgs;
        android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer;
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "requestAssistScreenshot()")) {
            throw new java.lang.SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(0);
                if (displayContent == null) {
                    if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_SCREENSHOT) {
                        android.util.Slog.i(TAG, "Screenshot returning null. No Display for displayId=0");
                    }
                    captureArgs = null;
                } else {
                    captureArgs = displayContent.getLayerCaptureArgs(windowTypesToExclude);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        if (captureArgs != null) {
            android.window.ScreenCapture.SynchronousScreenCaptureListener syncScreenCapture = android.window.ScreenCapture.createSyncCaptureListener();
            android.window.ScreenCapture.captureLayers(captureArgs, syncScreenCapture);
            screenshotBuffer = syncScreenCapture.getBuffer();
        } else {
            screenshotBuffer = null;
        }
        if (screenshotBuffer == null) {
            android.util.Slog.w(TAG, "Failed to take screenshot");
        }
        return screenshotBuffer;
    }

    public boolean requestAssistScreenshot(final android.app.IAssistDataReceiver receiver) {
        android.window.ScreenCapture.ScreenshotHardwareBuffer shb = takeAssistScreenshot(java.util.Set.of());
        final android.graphics.Bitmap bm = shb != null ? shb.asBitmap() : null;
        com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                receiver.onHandleAssistScreenshot(bm);
            }
        });
        return true;
    }

    public android.window.TaskSnapshot getTaskSnapshot(int taskId, int userId, boolean isLowResolution, boolean restoreFromDisk) {
        return this.mTaskSnapshotController.getSnapshot(taskId, userId, restoreFromDisk, isLowResolution);
    }

    public android.graphics.Bitmap captureTaskBitmap(int taskId, android.window.ScreenCapture.LayerCaptureArgs.Builder layerCaptureArgsBuilder) {
        if (this.mTaskSnapshotController.shouldDisableSnapshots()) {
            return null;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task task = this.mRoot.anyTaskForId(taskId);
                if (task == null) {
                    resetPriorityAfterLockedSection();
                    return null;
                }
                task.getBounds(this.mTmpRect);
                this.mTmpRect.offsetTo(0, 0);
                android.view.SurfaceControl sc = task.getSurfaceControl();
                android.window.ScreenCapture.ScreenshotHardwareBuffer buffer = android.window.ScreenCapture.captureLayers(layerCaptureArgsBuilder.setLayer(sc).setSourceCrop(this.mTmpRect).build());
                if (buffer == null) {
                    android.util.Slog.w(TAG, "Could not get screenshot buffer for taskId: " + taskId);
                    resetPriorityAfterLockedSection();
                    return null;
                }
                android.graphics.Bitmap bitmapAsBitmap = buffer.asBitmap();
                resetPriorityAfterLockedSection();
                return bitmapAsBitmap;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void removeObsoleteTaskFiles(android.util.ArraySet<java.lang.Integer> persistentTaskIds, int[] runningUserIds) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mTaskSnapshotController.removeObsoleteTaskFiles(persistentTaskIds, runningUserIds);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void setFixedToUserRotation(int displayId, int fixedToUserRotation) {
        if (!checkCallingPermission("android.permission.SET_ORIENTATION", "setFixedToUserRotation()")) {
            throw new java.lang.SecurityException("Requires SET_ORIENTATION permission");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent display = this.mRoot.getDisplayContent(displayId);
                    if (display == null) {
                        android.util.Slog.w(TAG, "Trying to set fixed to user rotation for a missing display.");
                        resetPriorityAfterLockedSection();
                    } else {
                        display.getDisplayRotation().setFixedToUserRotation(fixedToUserRotation);
                        resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    int getFixedToUserRotation(int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent display = this.mRoot.getDisplayContent(displayId);
                if (display == null) {
                    android.util.Slog.w(TAG, "Trying to get fixed to user rotation for a missing display.");
                    resetPriorityAfterLockedSection();
                    return -1;
                }
                int fixedToUserRotationMode = display.getDisplayRotation().getFixedToUserRotationMode();
                resetPriorityAfterLockedSection();
                return fixedToUserRotationMode;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setIgnoreOrientationRequest(int displayId, boolean ignoreOrientationRequest) {
        if (!checkCallingPermission("android.permission.SET_ORIENTATION", "setIgnoreOrientationRequest()")) {
            throw new java.lang.SecurityException("Requires SET_ORIENTATION permission");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent display = this.mRoot.getDisplayContent(displayId);
                    if (display == null) {
                        android.util.Slog.w(TAG, "Trying to setIgnoreOrientationRequest() for a missing display.");
                        resetPriorityAfterLockedSection();
                    } else {
                        display.setIgnoreOrientationRequest(ignoreOrientationRequest);
                        resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    boolean getIgnoreOrientationRequest(int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent display = this.mRoot.getDisplayContent(displayId);
                if (display == null) {
                    android.util.Slog.w(TAG, "Trying to getIgnoreOrientationRequest() for a missing display.");
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean ignoreOrientationRequest = display.getIgnoreOrientationRequest();
                resetPriorityAfterLockedSection();
                return ignoreOrientationRequest;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void setOrientationRequestPolicy(boolean isIgnoreOrientationRequestDisabled, int[] fromOrientations, int[] toOrientations) {
        this.mOrientationMapping.clear();
        if (fromOrientations != null && toOrientations != null && fromOrientations.length == toOrientations.length) {
            for (int i = 0; i < fromOrientations.length; i++) {
                this.mOrientationMapping.put(fromOrientations[i], toOrientations[i]);
            }
        }
        if (isIgnoreOrientationRequestDisabled == this.mIsIgnoreOrientationRequestDisabled) {
            return;
        }
        this.mIsIgnoreOrientationRequestDisabled = isIgnoreOrientationRequestDisabled;
        for (int i2 = this.mRoot.getChildCount() - 1; i2 >= 0; i2--) {
            ((com.android.server.wm.DisplayContent) this.mRoot.getChildAt(i2)).onIsIgnoreOrientationRequestDisabledChanged();
        }
    }

    int mapOrientationRequest(int requestedOrientation) {
        if (!this.mIsIgnoreOrientationRequestDisabled) {
            return requestedOrientation;
        }
        return this.mOrientationMapping.get(requestedOrientation, requestedOrientation);
    }

    boolean isIgnoreOrientationRequestDisabled() {
        return this.mIsIgnoreOrientationRequestDisabled || !this.mLetterboxConfiguration.isIgnoreOrientationRequestAllowed();
    }

    public void freezeRotation(int rotation, java.lang.String caller) {
        freezeDisplayRotation(0, rotation, caller);
    }

    public void freezeDisplayRotation(int displayId, int rotation, java.lang.String caller) {
        if (!checkCallingPermission("android.permission.SET_ORIENTATION", "freezeRotation()")) {
            throw new java.lang.SecurityException("Requires SET_ORIENTATION permission");
        }
        if (rotation < -1 || rotation > 3) {
            throw new java.lang.IllegalArgumentException("Rotation argument must be -1 or a valid rotation constant.");
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
            long protoLogParam0 = getDefaultDisplayRotation();
            long protoLogParam1 = rotation;
            java.lang.String protoLogParam2 = java.lang.String.valueOf(caller);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -6625203651195752178L, 5, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1), protoLogParam2);
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent display = this.mRoot.getDisplayContent(displayId);
                    if (display == null) {
                        android.util.Slog.w(TAG, "Trying to freeze rotation for a missing display.");
                        resetPriorityAfterLockedSection();
                    } else {
                        display.getDisplayRotation().freezeRotation(rotation, caller);
                        resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(origId);
                        updateRotationUnchecked(false, false);
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void thawRotation(java.lang.String caller) {
        thawDisplayRotation(0, caller);
    }

    public void thawDisplayRotation(int displayId, java.lang.String caller) {
        if (!checkCallingPermission("android.permission.SET_ORIENTATION", "thawRotation()")) {
            throw new java.lang.SecurityException("Requires SET_ORIENTATION permission");
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
            long protoLogParam0 = getDefaultDisplayRotation();
            java.lang.String protoLogParam1 = java.lang.String.valueOf(caller);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 8988910478484254861L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent display = this.mRoot.getDisplayContent(displayId);
                    if (display == null) {
                        android.util.Slog.w(TAG, "Trying to thaw rotation for a missing display.");
                        resetPriorityAfterLockedSection();
                    } else {
                        display.getDisplayRotation().thawRotation(caller);
                        resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(origId);
                        updateRotationUnchecked(false, false);
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public boolean isRotationFrozen() {
        return isDisplayRotationFrozen(0);
    }

    public boolean isDisplayRotationFrozen(int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent display = this.mRoot.getDisplayContent(displayId);
                if (display == null) {
                    android.util.Slog.w(TAG, "Trying to check if rotation is frozen on a missing display.");
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean zIsRotationFrozen = display.getDisplayRotation().isRotationFrozen();
                resetPriorityAfterLockedSection();
                return zIsRotationFrozen;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public int getDisplayUserRotation(int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent display = this.mRoot.getDisplayContent(displayId);
                if (display == null) {
                    android.util.Slog.w(TAG, "Trying to get user rotation of a missing display.");
                    resetPriorityAfterLockedSection();
                    return -1;
                }
                int userRotation = display.getDisplayRotation().getUserRotation();
                resetPriorityAfterLockedSection();
                return userRotation;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public void updateRotation(boolean alwaysSendConfiguration, boolean forceRelayout) {
        updateRotationUnchecked(alwaysSendConfiguration, forceRelayout);
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
    private void updateRotationUnchecked(boolean alwaysSendConfiguration, boolean forceRelayout) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 7261084872394224738L, 15, null, java.lang.Boolean.valueOf(alwaysSendConfiguration), java.lang.Boolean.valueOf(forceRelayout));
        }
        android.os.Trace.traceBegin(32L, "updateRotation");
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                boolean layoutNeeded = false;
                try {
                    int displayCount = this.mRoot.mChildren.size();
                    for (int i = 0; i < displayCount; i++) {
                        com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) this.mRoot.mChildren.get(i);
                        android.os.Trace.traceBegin(32L, "updateRotation: display");
                        boolean rotationChanged = displayContent.updateRotationUnchecked();
                        android.os.Trace.traceEnd(32L);
                        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                            android.util.Slog.d(TAG, "updateRotationUnchecked: rotationChanged = " + rotationChanged + ", caller:" + (alwaysSendConfiguration ? android.os.Debug.getCallers(7) : ""));
                        }
                        boolean pendingRemoteDisplayChange = false;
                        if (rotationChanged) {
                            if (((com.android.server.wm.IFlexibleWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IFlexibleWindowManagerExt.class).create()).isInPocketStudio(0)) {
                                com.android.server.inputmethod.InputMethodManagerInternal.get().hideAllInputMethods(51, displayContent.mDisplayId);
                            }
                            this.mAtmService.getTaskChangeNotificationController().notifyOnActivityRotation(displayContent.mDisplayId);
                        }
                        if (rotationChanged && (displayContent.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange() || displayContent.mTransitionController.isCollecting())) {
                            pendingRemoteDisplayChange = true;
                        }
                        if (!pendingRemoteDisplayChange) {
                            if (forceRelayout) {
                                displayContent.setLayoutNeeded();
                                layoutNeeded = true;
                            }
                            if (rotationChanged || alwaysSendConfiguration) {
                                displayContent.sendNewConfiguration();
                            }
                        }
                    }
                    if (layoutNeeded) {
                        android.os.Trace.traceBegin(32L, "updateRotation: performSurfacePlacement");
                        this.mWindowPlacerLocked.performSurfacePlacement();
                        android.os.Trace.traceEnd(32L);
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
            android.os.Trace.traceEnd(32L);
        }
    }

    public int getDefaultDisplayRotation() {
        int rotation;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                rotation = getDefaultDisplayContentLocked().getRotation();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return rotation;
    }

    public void setDisplayChangeWindowController(android.view.IDisplayChangeWindowController controller) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("setDisplayWindowRotationController");
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (this.mDisplayChangeController != null) {
                        this.mDisplayChangeController.asBinder().unlinkToDeath(this.mDisplayChangeControllerDeath, 0);
                        this.mDisplayChangeController = null;
                    }
                    controller.asBinder().linkToDeath(this.mDisplayChangeControllerDeath, 0);
                    this.mDisplayChangeController = controller;
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Unable to set rotation controller", e);
        }
    }

    /* JADX WARN: Finally extract failed */
    public android.view.SurfaceControl addShellRoot(int displayId, android.view.IWindow client, int shellRootLayer) {
        addShellRoot_enforcePermission();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(displayId);
                    if (dc != null) {
                        android.view.SurfaceControl surfaceControlAddShellRoot = dc.addShellRoot(client, shellRootLayer);
                        resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(origId);
                        return surfaceControlAddShellRoot;
                    }
                    resetPriorityAfterLockedSection();
                    android.os.Binder.restoreCallingIdentity(origId);
                    return null;
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } catch (java.lang.Throwable th2) {
            android.os.Binder.restoreCallingIdentity(origId);
            throw th2;
        }
    }

    public void setShellRootAccessibilityWindow(int displayId, int shellRootLayer, android.view.IWindow target) {
        setShellRootAccessibilityWindow_enforcePermission();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(displayId);
                    if (dc == null) {
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.ShellRoot root = dc.mShellRoots.get(shellRootLayer);
                    if (root == null) {
                        resetPriorityAfterLockedSection();
                    } else {
                        root.setAccessibilityWindow(target);
                        resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void setDisplayWindowInsetsController(int displayId, android.view.IDisplayWindowInsetsController insetsController) {
        setDisplayWindowInsetsController_enforcePermission();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(displayId);
                    if (dc != null) {
                        dc.setRemoteInsetsController(insetsController);
                        resetPriorityAfterLockedSection();
                    } else {
                        resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void updateDisplayWindowRequestedVisibleTypes(int displayId, int requestedVisibleTypes) {
        updateDisplayWindowRequestedVisibleTypes_enforcePermission();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(displayId);
                    if (dc != null && dc.mRemoteInsetsControlTarget != null) {
                        dc.mRemoteInsetsControlTarget.setRequestedVisibleTypes(requestedVisibleTypes);
                        dc.getInsetsStateController().onRequestedVisibleTypesChanged(dc.mRemoteInsetsControlTarget);
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public int watchRotation(android.view.IRotationWatcher watcher, int displayId) {
        int rotation;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    throw new java.lang.IllegalArgumentException("Trying to register rotation event for invalid display: " + displayId);
                }
                this.mRotationWatcherController.registerDisplayRotationWatcher(watcher, displayId);
                rotation = displayContent.getRotation();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return rotation;
    }

    public void removeRotationWatcher(android.view.IRotationWatcher watcher) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRotationWatcherController.removeRotationWatcher(watcher);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public int registerProposedRotationListener(android.os.IBinder contextToken, android.view.IRotationWatcher listener) {
        int rotation;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowContainer<?> wc = this.mRotationWatcherController.getAssociatedWindowContainer(contextToken);
                if (wc == null) {
                    android.util.Slog.w(TAG, "Register rotation listener from non-existing token, uid=" + android.os.Binder.getCallingUid());
                    resetPriorityAfterLockedSection();
                    return 0;
                }
                this.mRotationWatcherController.registerProposedRotationListener(listener, contextToken);
                com.android.server.wm.WindowOrientationListener orientationListener = wc.mDisplayContent.getDisplayRotation().getOrientationListener();
                if (orientationListener != null && (rotation = orientationListener.getProposedRotation()) >= 0) {
                    resetPriorityAfterLockedSection();
                    return rotation;
                }
                int rotation2 = wc.getWindowConfiguration().getRotation();
                resetPriorityAfterLockedSection();
                return rotation2;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean registerWallpaperVisibilityListener(android.view.IWallpaperVisibilityListener listener, int displayId) {
        boolean zIsWallpaperVisible;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    throw new java.lang.IllegalArgumentException("Trying to register visibility event for invalid display: " + displayId);
                }
                this.mWallpaperVisibilityListeners.registerWallpaperVisibilityListener(listener, displayId);
                zIsWallpaperVisible = displayContent.mWallpaperController.isWallpaperVisible();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return zIsWallpaperVisible;
    }

    public void unregisterWallpaperVisibilityListener(android.view.IWallpaperVisibilityListener listener, int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mWallpaperVisibilityListeners.unregisterWallpaperVisibilityListener(listener, displayId);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void registerSystemGestureExclusionListener(android.view.ISystemGestureExclusionListener listener, int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    throw new java.lang.IllegalArgumentException("Trying to register system gesture exclusion event for invalid display: " + displayId);
                }
                displayContent.registerSystemGestureExclusionListener(listener);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void unregisterSystemGestureExclusionListener(android.view.ISystemGestureExclusionListener listener, int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    throw new java.lang.IllegalArgumentException("Trying to unregister system gesture exclusion event for invalid display: " + displayId);
                }
                displayContent.unregisterSystemGestureExclusionListener(listener);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void registerDecorViewGestureListener(android.view.IDecorViewGestureListener listener, int displayId) {
        if (!checkCallingPermission("android.permission.MONITOR_INPUT", "registerDecorViewGestureListener()")) {
            throw new java.lang.SecurityException("Requires MONITOR_INPUT permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    throw new java.lang.IllegalArgumentException("Trying to register DecorView gesture event listenerfor invalid display: " + displayId);
                }
                displayContent.registerDecorViewGestureListener(listener);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void unregisterDecorViewGestureListener(android.view.IDecorViewGestureListener listener, int displayId) {
        if (!checkCallingPermission("android.permission.MONITOR_INPUT", "unregisterSystemGestureExclusionListener()")) {
            throw new java.lang.SecurityException("Requires MONITOR_INPUT permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    throw new java.lang.IllegalArgumentException("Trying to unregister DecorView gesture event listenerfor invalid display: " + displayId);
                }
                displayContent.unregisterDecorViewGestureListener(listener);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    void reportDecorViewGestureChanged(com.android.server.wm.Session session, android.view.IWindow window, boolean intercepted) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState win = windowForClientLocked(session, window, false);
                if (win == null) {
                    resetPriorityAfterLockedSection();
                } else {
                    win.getDisplayContent().updateDecorViewGestureIntercepted(win.mToken.token, intercepted);
                    resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void reportSystemGestureExclusionChanged(com.android.server.wm.Session session, android.view.IWindow window, java.util.List<android.graphics.Rect> exclusionRects) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState win = windowForClientLocked(session, window, false);
                if (win == null) {
                    android.util.Slog.i(TAG, "reportSystemGestureExclusionChanged(): No window state for package:" + session.mPackageName);
                    resetPriorityAfterLockedSection();
                } else {
                    if (win.setSystemGestureExclusion(exclusionRects)) {
                        win.getDisplayContent().updateSystemGestureExclusion();
                    }
                    resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void reportKeepClearAreasChanged(com.android.server.wm.Session session, android.view.IWindow window, java.util.List<android.graphics.Rect> restricted, java.util.List<android.graphics.Rect> unrestricted) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState win = windowForClientLocked(session, window, false);
                if (win == null) {
                    android.util.Slog.i(TAG, "reportKeepClearAreasChanged(): No window state for package:" + session.mPackageName);
                    resetPriorityAfterLockedSection();
                } else {
                    if (win.setKeepClearAreas(restricted, unrestricted)) {
                        win.getDisplayContent().updateKeepClearAreas();
                    }
                    resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void registerDisplayFoldListener(android.view.IDisplayFoldListener listener) {
        this.mPolicy.registerDisplayFoldListener(listener);
    }

    public void unregisterDisplayFoldListener(android.view.IDisplayFoldListener listener) {
        this.mPolicy.unregisterDisplayFoldListener(listener);
    }

    void setOverrideFoldedArea(android.graphics.Rect area) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
            throw new java.lang.SecurityException("Must hold permission android.permission.WRITE_SECURE_SETTINGS");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mPolicy.setOverrideFoldedArea(area);
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    android.graphics.Rect getFoldedArea() {
        android.graphics.Rect foldedArea;
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    foldedArea = this.mPolicy.getFoldedArea();
                } finally {
                }
            }
            resetPriorityAfterLockedSection();
            return foldedArea;
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public int[] registerDisplayWindowListener(android.view.IDisplayWindowListener listener) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("registerDisplayWindowListener");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mDisplayNotificationController.registerListener(listener);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void unregisterDisplayWindowListener(android.view.IDisplayWindowListener listener) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("unregisterDisplayWindowListener");
        this.mDisplayNotificationController.unregisterListener(listener);
    }

    public int getPreferredOptionsPanelGravity(int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    resetPriorityAfterLockedSection();
                    return 81;
                }
                int preferredOptionsPanelGravity = displayContent.getPreferredOptionsPanelGravity();
                resetPriorityAfterLockedSection();
                return preferredOptionsPanelGravity;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean startViewServer(int port) {
        if (isSystemSecure() || !checkCallingPermission("android.permission.DUMP", "startViewServer") || port < 1024) {
            return false;
        }
        if (this.mViewServer != null) {
            if (!this.mViewServer.isRunning()) {
                try {
                    return this.mViewServer.start();
                } catch (java.io.IOException e) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 8664813170125714536L, 0, "View server did not start", null);
                    }
                }
            }
            return false;
        }
        try {
            this.mViewServer = new com.android.server.wm.ViewServer(this, port);
            return this.mViewServer.start();
        } catch (java.io.IOException e2) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 8664813170125714536L, 0, "View server did not start", null);
            }
            return false;
        }
    }

    private boolean isSystemSecure() {
        return "1".equals(android.os.SystemProperties.get(SYSTEM_SECURE, "1")) && "0".equals(android.os.SystemProperties.get(SYSTEM_DEBUGGABLE, "0"));
    }

    public boolean stopViewServer() {
        if (isSystemSecure() || !checkCallingPermission("android.permission.DUMP", "stopViewServer") || this.mViewServer == null) {
            return false;
        }
        return this.mViewServer.stop();
    }

    public boolean isViewServerRunning() {
        return !isSystemSecure() && checkCallingPermission("android.permission.DUMP", "isViewServerRunning") && this.mViewServer != null && this.mViewServer.isRunning();
    }

    boolean viewServerListWindows(java.net.Socket client) {
        if (isSystemSecure()) {
            return false;
        }
        final java.util.ArrayList<com.android.server.wm.WindowState> windows = new java.util.ArrayList<>();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda15
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        windows.add((com.android.server.wm.WindowState) obj);
                    }
                }, false);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        java.io.BufferedWriter out = null;
        try {
            try {
                java.io.OutputStream clientStream = client.getOutputStream();
                out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(clientStream), 8192);
                int count = windows.size();
                for (int i = 0; i < count; i++) {
                    com.android.server.wm.WindowState w = windows.get(i);
                    out.write(java.lang.Integer.toHexString(java.lang.System.identityHashCode(w)));
                    out.write(32);
                    out.append(w.mAttrs.getTitle());
                    out.write(10);
                }
                out.write("DONE.\n");
                out.flush();
                out.close();
                return true;
            } catch (java.lang.Exception e) {
                if (out == null) {
                    return false;
                }
                out.close();
                return false;
            } catch (java.lang.Throwable th2) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (java.io.IOException e2) {
                    }
                }
                throw th2;
            }
        } catch (java.io.IOException e3) {
            return false;
        }
    }

    boolean viewServerGetFocusedWindow(java.net.Socket client) {
        if (isSystemSecure()) {
            return false;
        }
        com.android.server.wm.WindowState focusedWindow = getFocusedWindow();
        java.io.BufferedWriter out = null;
        try {
            try {
                java.io.OutputStream clientStream = client.getOutputStream();
                out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(clientStream), 8192);
                if (focusedWindow != null) {
                    out.write(java.lang.Integer.toHexString(java.lang.System.identityHashCode(focusedWindow)));
                    out.write(32);
                    out.append(focusedWindow.mAttrs.getTitle());
                }
                out.write(10);
                out.flush();
                out.close();
                return true;
            } catch (java.io.IOException e) {
                return false;
            }
        } catch (java.lang.Exception e2) {
            if (out == null) {
                return false;
            }
            out.close();
            return false;
        } catch (java.lang.Throwable th) {
            if (out != null) {
                try {
                    out.close();
                } catch (java.io.IOException e3) {
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x00d2 A[Catch: all -> 0x0102, TRY_LEAVE, TryCatch #0 {all -> 0x0102, blocks: (B:30:0x0063, B:32:0x008b, B:52:0x00cb, B:54:0x00d2), top: B:75:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00fd A[Catch: IOException -> 0x00b2, TRY_ENTER, TRY_LEAVE, TryCatch #5 {IOException -> 0x00b2, blocks: (B:38:0x00ae, B:61:0x00fd), top: B:81:0x0010 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean viewServerWindowCommand(java.net.Socket r20, java.lang.String r21, java.lang.String r22) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 278
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerService.viewServerWindowCommand(java.net.Socket, java.lang.String, java.lang.String):boolean");
    }

    public void addWindowChangeListener(com.android.server.wm.WindowManagerService.WindowChangeListener listener) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mWindowChangeListeners.add(listener);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void removeWindowChangeListener(com.android.server.wm.WindowManagerService.WindowChangeListener listener) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mWindowChangeListeners.remove(listener);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    private void notifyWindowRemovedListeners(final android.os.IBinder client) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mOnWindowRemovedListeners.isEmpty()) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                final com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener[] windowRemovedListeners = new com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener[this.mOnWindowRemovedListeners.size()];
                this.mOnWindowRemovedListeners.toArray(windowRemovedListeners);
                resetPriorityAfterLockedSection();
                this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda27
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.wm.WindowManagerService.lambda$notifyWindowRemovedListeners$10(windowRemovedListeners, client);
                    }
                });
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    static /* synthetic */ void lambda$notifyWindowRemovedListeners$10(com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener[] windowRemovedListeners, android.os.IBinder client) {
        for (com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener onWindowRemovedListener : windowRemovedListeners) {
            onWindowRemovedListener.onWindowRemoved(client);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWindowsChanged() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mWindowChangeListeners.isEmpty()) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                com.android.server.wm.WindowManagerService.WindowChangeListener[] windowChangeListeners = (com.android.server.wm.WindowManagerService.WindowChangeListener[]) this.mWindowChangeListeners.toArray(new com.android.server.wm.WindowManagerService.WindowChangeListener[this.mWindowChangeListeners.size()]);
                resetPriorityAfterLockedSection();
                for (com.android.server.wm.WindowManagerService.WindowChangeListener windowChangeListener : windowChangeListeners) {
                    windowChangeListener.windowsChanged();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void notifyFocusChanged() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mWindowChangeListeners.isEmpty()) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                com.android.server.wm.WindowManagerService.WindowChangeListener[] windowChangeListeners = (com.android.server.wm.WindowManagerService.WindowChangeListener[]) this.mWindowChangeListeners.toArray(new com.android.server.wm.WindowManagerService.WindowChangeListener[this.mWindowChangeListeners.size()]);
                resetPriorityAfterLockedSection();
                for (com.android.server.wm.WindowManagerService.WindowChangeListener windowChangeListener : windowChangeListeners) {
                    windowChangeListener.focusChanged();
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private com.android.server.wm.WindowState findWindow(final int hashCode) {
        com.android.server.wm.WindowState window;
        if (hashCode == -1) {
            return getFocusedWindow();
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                window = this.mRoot.getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda36
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.wm.WindowManagerService.lambda$findWindow$11(hashCode, (com.android.server.wm.WindowState) obj);
                    }
                });
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return window;
    }

    static /* synthetic */ boolean lambda$findWindow$11(int hashCode, com.android.server.wm.WindowState w) {
        return java.lang.System.identityHashCode(w) == hashCode;
    }

    public android.content.res.Configuration computeNewConfiguration(int displayId) {
        android.content.res.Configuration configurationComputeNewConfigurationLocked;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                configurationComputeNewConfigurationLocked = computeNewConfigurationLocked(displayId);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return configurationComputeNewConfigurationLocked;
    }

    private android.content.res.Configuration computeNewConfigurationLocked(int displayId) {
        if (!this.mDisplayReady) {
            return null;
        }
        android.content.res.Configuration config = new android.content.res.Configuration();
        com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
        displayContent.computeScreenConfiguration(config);
        return config;
    }

    void notifyHardKeyboardStatusChange() {
        com.android.server.wm.WindowManagerInternal.OnHardKeyboardStatusChangeListener listener;
        boolean available;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                listener = this.mHardKeyboardStatusChangeListener;
                available = this.mHardKeyboardAvailable;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        if (listener != null) {
            listener.onHardKeyboardStatusChange(available);
        }
    }

    public void setEventDispatching(boolean enabled) {
        if (!checkCallingPermission("android.permission.MANAGE_APP_TOKENS", "setEventDispatching()")) {
            throw new java.lang.SecurityException("Requires MANAGE_APP_TOKENS permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mEventDispatchingEnabled = enabled;
                if (this.mDisplayEnabled) {
                    this.mInputManagerCallback.setEventDispatchingLw(enabled);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.wm.WindowState getFocusedWindow() {
        com.android.server.wm.WindowState focusedWindowLocked;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                focusedWindowLocked = getFocusedWindowLocked();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return focusedWindowLocked;
    }

    com.android.server.wm.WindowState getFocusedWindowLocked() {
        return this.mRoot.getTopFocusedDisplayContent().mCurrentFocus;
    }

    com.android.server.wm.Task getImeFocusRootTaskLocked() {
        com.android.server.wm.DisplayContent topFocusedDisplay = this.mRoot.getTopFocusedDisplayContent();
        com.android.server.wm.ActivityRecord focusedApp = topFocusedDisplay.mFocusedApp;
        if (focusedApp == null || focusedApp.getTask() == null) {
            return null;
        }
        return focusedApp.getTask().getRootTask();
    }

    public boolean detectSafeMode() {
        if (!this.mInputManagerCallback.waitForInputDevicesReady(1000L) && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 1893303527772009363L, 1, "Devices still not ready after waiting %d milliseconds before attempting to detect safe mode.", 1000L);
        }
        if (android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "safe_boot_disallowed", 0) != 0) {
            return false;
        }
        int menuState = this.mInputManager.getKeyCodeState(-1, -256, 82);
        int sState = this.mInputManager.getKeyCodeState(-1, -256, 47);
        int dpadState = this.mInputManager.getKeyCodeState(-1, 513, 23);
        int trackballState = this.mInputManager.getScanCodeState(-1, 65540, 272);
        int volumeDownState = this.mInputManager.getKeyCodeState(-1, -256, 25);
        this.mSafeMode = menuState > 0 || sState > 0 || dpadState > 0 || trackballState > 0 || volumeDownState > 0;
        try {
            if (android.os.SystemProperties.getInt(com.android.server.power.ShutdownThread.REBOOT_SAFEMODE_PROPERTY, 0) != 0 || android.os.SystemProperties.getInt(com.android.server.power.ShutdownThread.RO_SAFEMODE_PROPERTY, 0) != 0) {
                this.mSafeMode = true;
                android.os.SystemProperties.set(com.android.server.power.ShutdownThread.REBOOT_SAFEMODE_PROPERTY, "");
            }
        } catch (java.lang.IllegalArgumentException e) {
        }
        if (this.mSafeMode) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[2]) {
                long protoLogParam0 = menuState;
                long protoLogParam1 = sState;
                long protoLogParam2 = dpadState;
                long protoLogParam3 = trackballState;
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -3652974372240081071L, 85, "SAFE MODE ENABLED (menu=%d s=%d dpad=%d trackball=%d)", java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2), java.lang.Long.valueOf(protoLogParam3));
            }
            if (android.os.SystemProperties.getInt(com.android.server.power.ShutdownThread.RO_SAFEMODE_PROPERTY, 0) == 0) {
                android.os.SystemProperties.set(com.android.server.power.ShutdownThread.RO_SAFEMODE_PROPERTY, "1");
            }
        } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[2]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 4945624619344146947L, 0, "SAFE MODE not enabled", null);
        }
        this.mPolicy.setSafeMode(this.mSafeMode);
        return this.mSafeMode;
    }

    public void displayReady() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mMaxUiWidth > 0) {
                    this.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda6
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$displayReady$12((com.android.server.wm.DisplayContent) obj);
                        }
                    });
                }
                applyForcedPropertiesForDefaultDisplay();
                this.mAnimator.ready();
                this.mDisplayReady = true;
                this.mHasWideColorGamutSupport = queryWideColorGamutSupport();
                this.mHasHdrSupport = queryHdrSupport();
                this.mIsTouchDevice = this.mContext.getPackageManager().hasSystemFeature("android.hardware.touchscreen");
                this.mIsFakeTouchDevice = this.mContext.getPackageManager().hasSystemFeature("android.hardware.faketouch");
                this.mWindowManagerServiceExt.hookDisplayReady(this, this.mContext);
                this.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda7
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wm.DisplayContent) obj).reconfigureDisplayLocked();
                    }
                });
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$displayReady$12(com.android.server.wm.DisplayContent dc) {
        if (dc.mDisplay.getType() == 1) {
            dc.setMaxUiWidth(this.mMaxUiWidth);
        }
    }

    public void systemReady() {
        this.mSystemReady = true;
        this.mPolicy.systemReady();
        this.mRoot.forAllDisplayPolicies(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.DisplayPolicy) obj).systemReady();
            }
        });
        this.mSnapshotController.systemReady();
        android.os.Handler handler = com.android.server.UiThread.getHandler();
        final com.android.server.wm.WindowManagerService.SettingsObserver settingsObserver = this.mSettingsObserver;
        java.util.Objects.requireNonNull(settingsObserver);
        handler.post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                settingsObserver.loadSettings();
            }
        });
        android.service.vr.IVrManager vrManager = android.service.vr.IVrManager.Stub.asInterface(android.os.ServiceManager.getService("vrmanager"));
        if (vrManager != null) {
            try {
                boolean vrModeEnabled = vrManager.getVrModeState();
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        vrManager.registerListener(this.mVrStateCallbacks);
                        if (vrModeEnabled) {
                            this.mVrModeEnabled = vrModeEnabled;
                            this.mVrStateCallbacks.onVrStateChanged(vrModeEnabled);
                        }
                    } catch (java.lang.Throwable th) {
                        resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                resetPriorityAfterLockedSection();
            } catch (android.os.RemoteException e) {
            }
        }
        this.mWindowManagerServiceExt.endHookSystemReady();
    }

    private static boolean queryWideColorGamutSupport() {
        java.util.Optional<java.lang.Boolean> hasWideColorProp = android.sysprop.SurfaceFlingerProperties.has_wide_color_display();
        if (hasWideColorProp.isPresent()) {
            return hasWideColorProp.get().booleanValue();
        }
        try {
            android.hardware.configstore.V1_1.ISurfaceFlingerConfigs surfaceFlinger = android.hardware.configstore.V1_1.ISurfaceFlingerConfigs.getService();
            android.hardware.configstore.V1_0.OptionalBool hasWideColor = surfaceFlinger.hasWideColorDisplay();
            if (hasWideColor != null) {
                return hasWideColor.value;
            }
            return false;
        } catch (android.os.RemoteException e) {
            return false;
        } catch (java.util.NoSuchElementException e2) {
            return false;
        }
    }

    private static boolean queryHdrSupport() {
        java.util.Optional<java.lang.Boolean> hasHdrProp = android.sysprop.SurfaceFlingerProperties.has_HDR_display();
        if (hasHdrProp.isPresent()) {
            return hasHdrProp.get().booleanValue();
        }
        try {
            android.hardware.configstore.V1_1.ISurfaceFlingerConfigs surfaceFlinger = android.hardware.configstore.V1_1.ISurfaceFlingerConfigs.getService();
            android.hardware.configstore.V1_0.OptionalBool hasHdr = surfaceFlinger.hasHDRDisplay();
            if (hasHdr != null) {
                return hasHdr.value;
            }
            return false;
        } catch (android.os.RemoteException e) {
            return false;
        } catch (java.util.NoSuchElementException e2) {
            return false;
        }
    }

    com.android.server.wm.InputTarget getInputTargetFromToken(android.os.IBinder inputToken) {
        com.android.server.wm.WindowState windowState = this.mInputToWindowMap.get(inputToken);
        if (windowState != null) {
            return windowState;
        }
        com.android.server.wm.EmbeddedWindowController.EmbeddedWindow embeddedWindow = this.mEmbeddedWindowController.get(inputToken);
        if (embeddedWindow != null) {
            return embeddedWindow;
        }
        return null;
    }

    com.android.server.wm.InputTarget getInputTargetFromWindowTokenLocked(android.os.IBinder windowToken) {
        com.android.server.wm.InputTarget window = this.mWindowMap.get(windowToken);
        if (window != null) {
            return window;
        }
        return this.mEmbeddedWindowController.getByWindowToken(windowToken);
    }

    void reportFocusChanged(android.os.IBinder oldToken, android.os.IBinder newToken) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.InputTarget lastTarget = getInputTargetFromToken(oldToken);
                com.android.server.wm.InputTarget newTarget = getInputTargetFromToken(newToken);
                if (newTarget == null && lastTarget == null) {
                    android.util.Slog.v(TAG, "Unknown focus tokens, dropping reportFocusChanged");
                    resetPriorityAfterLockedSection();
                    return;
                }
                this.mFocusedInputTarget = newTarget;
                this.mAccessibilityController.onFocusChanged(lastTarget, newTarget);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[2]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(lastTarget);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(newTarget);
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -3428027271337724889L, 0, null, protoLogParam0, protoLogParam1);
                }
                resetPriorityAfterLockedSection();
                com.android.server.wm.WindowState newFocusedWindow = newTarget != null ? newTarget.getWindowState() : null;
                if (newFocusedWindow != null && newFocusedWindow.mInputChannelToken == newToken) {
                    this.mAnrController.onFocusChanged(newFocusedWindow);
                    newFocusedWindow.reportFocusChangedSerialized(true);
                    notifyFocusChanged();
                    this.mWindowManagerServiceExt.notifyTouchAppChange(newFocusedWindow.getOwningPackage());
                }
                com.android.server.wm.WindowState lastFocusedWindow = lastTarget != null ? lastTarget.getWindowState() : null;
                if (lastFocusedWindow != null && lastFocusedWindow.mInputChannelToken == oldToken) {
                    lastFocusedWindow.reportFocusChangedSerialized(false);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    final class H extends android.os.Handler {
        public static final int ANIMATION_FAILSAFE = 60;
        public static final int APP_FREEZE_TIMEOUT = 17;
        public static final int BOOT_TIMEOUT = 23;
        public static final int CHECK_IF_BOOT_ANIMATION_FINISHED = 37;
        public static final int CLIENT_FREEZE_TIMEOUT = 30;
        public static final int ENABLE_SCREEN = 16;
        public static final int INSETS_CHANGED = 66;
        public static final int NEW_ANIMATOR_SCALE = 34;
        public static final int NOTIFY_ACTIVITY_DRAWN = 32;
        public static final int ON_POINTER_DOWN_OUTSIDE_FOCUS = 62;
        public static final int PERSIST_ANIMATION_SCALE = 14;
        public static final int RECOMPUTE_FOCUS = 61;
        public static final int REPARENT_TASK_TO_DEFAULT_DISPLAY = 65;
        public static final int REPORT_HARD_KEYBOARD_STATUS_CHANGE = 22;
        public static final int REPORT_WINDOWS_CHANGE = 19;
        public static final int RESET_ANR_MESSAGE = 38;
        public static final int SET_HAS_OVERLAY_UI = 58;
        public static final int SHOW_EMULATOR_DISPLAY_OVERLAY = 36;
        public static final int SHOW_STRICT_MODE_VIOLATION = 25;
        public static final int UNUSED = 0;
        public static final int UPDATE_ANIMATION_SCALE = 51;
        public static final int UPDATE_MULTI_WINDOW_STACKS = 41;
        public static final int WAITING_FOR_DRAWN_TIMEOUT = 24;
        public static final int WALLPAPER_DRAW_PENDING_TIMEOUT = 39;
        public static final int WINDOW_FREEZE_TIMEOUT = 11;
        public static final int WINDOW_HIDE_TIMEOUT = 52;
        public static final int WINDOW_STATE_BLAST_SYNC_TIMEOUT = 64;

        H() {
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            android.os.Message callback;
            boolean bootAnimationComplete;
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WINDOW_TRACE) {
                android.util.Slog.v(com.android.server.wm.WindowManagerService.TAG, "handleMessage: entry what=" + msg.what);
            }
            switch (msg.what) {
                case 11:
                    com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) msg.obj;
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            displayContent.onWindowFreezeTimeout();
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 14:
                    android.provider.Settings.Global.putFloat(com.android.server.wm.WindowManagerService.this.mContext.getContentResolver(), "window_animation_scale", com.android.server.wm.WindowManagerService.this.mWindowAnimationScaleSetting);
                    android.provider.Settings.Global.putFloat(com.android.server.wm.WindowManagerService.this.mContext.getContentResolver(), "transition_animation_scale", com.android.server.wm.WindowManagerService.this.mTransitionAnimationScaleSetting);
                    android.provider.Settings.Global.putFloat(com.android.server.wm.WindowManagerService.this.mContext.getContentResolver(), "animator_duration_scale", com.android.server.wm.WindowManagerService.this.mAnimatorDurationScaleSetting);
                    break;
                case 16:
                    com.android.server.wm.WindowManagerService.this.performEnableScreen();
                    break;
                case 17:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock2) {
                        try {
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 1624328195833150047L, 0, "App freeze timeout expired.", null);
                            }
                            com.android.server.wm.WindowManagerService.this.mWindowsFreezingScreen = 2;
                            try {
                                for (int i = com.android.server.wm.WindowManagerService.this.mAppFreezeListeners.size() - 1; i >= 0; i--) {
                                    com.android.server.wm.WindowManagerService.this.mAppFreezeListeners.get(i).onAppFreezeTimeout();
                                }
                            } catch (java.lang.Exception e) {
                                e.printStackTrace();
                            }
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 19:
                    if (com.android.server.wm.WindowManagerService.this.mWindowsChanged) {
                        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock3 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock3) {
                            try {
                                com.android.server.wm.WindowManagerService.this.mWindowsChanged = false;
                            } finally {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            }
                            break;
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        com.android.server.wm.WindowManagerService.this.notifyWindowsChanged();
                    }
                    break;
                case 22:
                    com.android.server.wm.WindowManagerService.this.notifyHardKeyboardStatusChange();
                    break;
                case 23:
                    com.android.server.wm.WindowManagerService.this.performBootTimeout();
                    break;
                case 24:
                    long ts = android.os.SystemClock.uptimeMillis();
                    com.android.server.wm.WindowManagerService.this.mTheiaManagerExt.sendEvent(260L, ts, 0, 0, 4099L, (java.lang.String) null);
                    com.android.server.wm.WindowContainer<?> container = (com.android.server.wm.WindowContainer) msg.obj;
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock4 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock4) {
                        try {
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                                java.lang.String protoLogParam0 = java.lang.String.valueOf(container.mWaitingForDrawn);
                                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 5830724144971462783L, 0, "Timeout waiting for drawn: undrawn=%s", protoLogParam0);
                            }
                            if (android.os.Trace.isTagEnabled(32L)) {
                                for (int i2 = 0; i2 < container.mWaitingForDrawn.size(); i2++) {
                                    com.android.server.wm.WindowManagerService.this.traceEndWaitingForWindowDrawn(container.mWaitingForDrawn.get(i2));
                                }
                            }
                            container.mWaitingForDrawn.clear();
                            com.android.server.wm.WindowManagerService.this.mWindowManagerServiceExt.clearSkipWaitingForDrawn();
                            callback = com.android.server.wm.WindowManagerService.this.mWaitingForDrawnCallbacks.remove(container);
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    if (callback != null) {
                        callback.sendToTarget();
                    }
                    com.android.server.wm.WindowManagerService.this.mWindowManagerServiceExt.allWindowsDraw();
                    break;
                case 25:
                    com.android.server.wm.WindowManagerService.this.showStrictModeViolation(msg.arg1, msg.arg2);
                    break;
                case 30:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock5 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock5) {
                        try {
                            if (com.android.server.wm.WindowManagerService.this.mClientFreezingScreen) {
                                com.android.server.wm.WindowManagerService.this.mClientFreezingScreen = false;
                                com.android.server.wm.WindowManagerService.this.mLastFinishedFreezeSource = "client-timeout";
                                com.android.server.wm.WindowManagerService.this.stopFreezingDisplayLocked();
                            }
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 32:
                    com.android.server.wm.ActivityRecord activity = (com.android.server.wm.ActivityRecord) msg.obj;
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock6 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock6) {
                        try {
                            if (activity.isAttached()) {
                                activity.getRootTask().notifyActivityDrawnLocked(activity);
                            }
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 34:
                    float scale = com.android.server.wm.WindowManagerService.this.getCurrentAnimatorScale();
                    android.animation.ValueAnimator.setDurationScale(scale);
                    com.android.server.wm.Session session = (com.android.server.wm.Session) msg.obj;
                    if (session != null) {
                        try {
                            session.mCallback.onAnimatorScaleChanged(scale);
                        } catch (android.os.RemoteException e2) {
                        }
                    } else {
                        java.util.ArrayList<android.view.IWindowSessionCallback> callbacks = new java.util.ArrayList<>();
                        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock7 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock7) {
                            for (int i3 = 0; i3 < com.android.server.wm.WindowManagerService.this.mSessions.size(); i3++) {
                                try {
                                    callbacks.add(com.android.server.wm.WindowManagerService.this.mSessions.valueAt(i3).mCallback);
                                } finally {
                                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                }
                            }
                            break;
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        for (int i4 = 0; i4 < callbacks.size(); i4++) {
                            try {
                                callbacks.get(i4).onAnimatorScaleChanged(scale);
                            } catch (android.os.RemoteException e3) {
                            }
                        }
                    }
                    break;
                case 36:
                    com.android.server.wm.WindowManagerService.this.showEmulatorDisplayOverlay();
                    break;
                case 37:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock8 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock8) {
                        try {
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BOOT_enabled[2]) {
                                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, -2240705227895260140L, 0, null, null);
                            }
                            bootAnimationComplete = com.android.server.wm.WindowManagerService.this.checkBootAnimationCompleteLocked();
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    if (bootAnimationComplete) {
                        com.android.server.wm.WindowManagerService.this.performEnableScreen();
                    }
                    break;
                case 38:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock9 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock9) {
                        try {
                            com.android.server.wm.WindowManagerService.this.mLastANRState = null;
                            com.android.server.wm.WindowManagerService.this.mAtmService.mLastANRState = null;
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 39:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock10 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock10) {
                        try {
                            com.android.server.wm.WallpaperController wallpaperController = (com.android.server.wm.WallpaperController) msg.obj;
                            if (wallpaperController != null && wallpaperController.processWallpaperDrawPendingTimeout()) {
                                com.android.server.wm.WindowManagerService.this.mWindowPlacerLocked.performSurfacePlacement();
                            }
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 41:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock11 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock11) {
                        try {
                            com.android.server.wm.DisplayContent displayContent2 = (com.android.server.wm.DisplayContent) msg.obj;
                            if (displayContent2 != null) {
                                displayContent2.adjustForImeIfNeeded();
                            }
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 51:
                    int mode = msg.arg1;
                    switch (mode) {
                        case 0:
                            com.android.server.wm.WindowManagerService.this.mWindowAnimationScaleSetting = com.android.server.wm.WindowManagerService.this.getWindowAnimationScaleSetting();
                            break;
                        case 1:
                            com.android.server.wm.WindowManagerService.this.mTransitionAnimationScaleSetting = com.android.server.wm.WindowManagerService.this.getTransitionAnimationScaleSetting();
                            break;
                        case 2:
                            com.android.server.wm.WindowManagerService.this.mAnimatorDurationScaleSetting = com.android.server.wm.WindowManagerService.this.getAnimatorDurationScaleSetting();
                            com.android.server.wm.WindowManagerService.this.dispatchNewAnimatorScaleLocked(null);
                            break;
                    }
                    break;
                case 52:
                    com.android.server.wm.WindowState window = (com.android.server.wm.WindowState) msg.obj;
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock12 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock12) {
                        try {
                            window.mAttrs.flags &= -129;
                            window.hidePermanentlyLw();
                            window.setDisplayLayoutNeeded();
                            com.android.server.wm.WindowManagerService.this.mWindowPlacerLocked.performSurfacePlacement();
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 58:
                    com.android.server.wm.WindowManagerService.this.mAmInternal.setHasOverlayUi(msg.arg1, msg.arg2 == 1);
                    break;
                case 60:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock13 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock13) {
                        try {
                            if (com.android.server.wm.WindowManagerService.this.mRecentsAnimationController != null) {
                                com.android.server.wm.WindowManagerService.this.mRecentsAnimationController.scheduleFailsafe();
                            }
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 61:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock14 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock14) {
                        try {
                            com.android.server.wm.WindowManagerService.this.updateFocusedWindowLocked(0, true);
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 62:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock15 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock15) {
                        try {
                            android.os.IBinder touchedToken = (android.os.IBinder) msg.obj;
                            com.android.server.wm.WindowManagerService.this.onPointerDownOutsideFocusLocked(com.android.server.wm.WindowManagerService.this.getInputTargetFromToken(touchedToken));
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 64:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock16 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock16) {
                        try {
                            com.android.server.wm.WindowState ws = (com.android.server.wm.WindowState) msg.obj;
                            android.util.Slog.i(com.android.server.wm.WindowManagerService.TAG, "Blast sync timeout: " + ws);
                            ws.immediatelyNotifyBlastSync();
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 65:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock17 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock17) {
                        try {
                            com.android.server.wm.Task task = (com.android.server.wm.Task) msg.obj;
                            task.reparent(com.android.server.wm.WindowManagerService.this.mRoot.getDefaultTaskDisplayArea(), true);
                            task.resumeNextFocusAfterReparent();
                        } finally {
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                case 66:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock18 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock18) {
                        try {
                            if (com.android.server.wm.WindowManagerService.this.mWindowsInsetsChanged > 0) {
                                com.android.server.wm.WindowManagerService.this.mWindowPlacerLocked.performSurfacePlacement();
                            }
                        } finally {
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    break;
                default:
                    com.android.server.wm.WindowManagerService.this.mWindowManagerServiceExt.handleOplusMessage(msg);
                    break;
            }
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WINDOW_TRACE) {
                android.util.Slog.v(com.android.server.wm.WindowManagerService.TAG, "handleMessage: exit");
            }
        }

        void sendNewMessageDelayed(int what, java.lang.Object obj, long delayMillis) {
            removeMessages(what, obj);
            sendMessageDelayed(obtainMessage(what, obj), delayMillis);
        }
    }

    public android.view.IWindowSession openSession(android.view.IWindowSessionCallback callback) {
        return new com.android.server.wm.Session(this, callback);
    }

    public void getInitialDisplaySize(int displayId, android.graphics.Point size) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent != null && displayContent.hasAccess(android.os.Binder.getCallingUid())) {
                    size.x = displayContent.mInitialDisplayWidth;
                    size.y = displayContent.mInitialDisplayHeight;
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void getBaseDisplaySize(int displayId, android.graphics.Point size) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent != null && displayContent.hasAccess(android.os.Binder.getCallingUid())) {
                    size.x = displayContent.mBaseDisplayWidth;
                    size.y = displayContent.mBaseDisplayHeight;
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void setForcedDisplaySize(int displayId, int width, int height) {
        setForcedDisplaySize_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                    if (displayContent != null) {
                        displayContent.setForcedSize(width, height);
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void setForcedDisplayScalingMode(int displayId, int mode) {
        setForcedDisplayScalingMode_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                    if (displayContent != null) {
                        displayContent.setForcedScalingMode(mode);
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    void setSandboxDisplayApis(int displayId, boolean sandboxDisplayApis) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
            throw new java.lang.SecurityException("Must hold permission android.permission.WRITE_SECURE_SETTINGS");
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                    if (displayContent != null) {
                        displayContent.setSandboxDisplayApis(sandboxDisplayApis);
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private boolean applyForcedPropertiesForDefaultDisplay() {
        int pos;
        boolean changed = false;
        com.android.server.wm.DisplayContent displayContent = getDefaultDisplayContentLocked();
        java.lang.String sizeStr = android.provider.Settings.Global.getString(this.mContext.getContentResolver(), "display_size_forced");
        java.lang.String sizeStr2 = (sizeStr == null || sizeStr.length() == 0) ? android.os.SystemProperties.get(SIZE_OVERRIDE, (java.lang.String) null) : sizeStr;
        if (sizeStr2 != null && sizeStr2.length() > 0 && (pos = sizeStr2.indexOf(44)) > 0 && sizeStr2.lastIndexOf(44) == pos) {
            try {
                android.graphics.Point size = displayContent.getValidForcedSize(java.lang.Integer.parseInt(sizeStr2.substring(0, pos)), java.lang.Integer.parseInt(sizeStr2.substring(pos + 1)));
                int width = size.x;
                int height = size.y;
                if (displayContent.mBaseDisplayWidth != width || displayContent.mBaseDisplayHeight != height) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[2]) {
                        long protoLogParam0 = width;
                        long protoLogParam1 = height;
                        com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 8641557333789260779L, 5, "FORCED DISPLAY SIZE: %dx%d", java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
                    }
                    displayContent.updateBaseDisplayMetrics(width, height, displayContent.mBaseDisplayDensity, displayContent.mBaseDisplayPhysicalXDpi, displayContent.mBaseDisplayPhysicalYDpi);
                    changed = true;
                }
            } catch (java.lang.NumberFormatException e) {
            }
        }
        int density = getForcedDisplayDensityForUserLocked(this.mCurrentUserId);
        if (android.view.DynamicLoggerObserver.isLogToolRun()) {
            android.util.Slog.d(TAG, "applyForcedPropertiesForDefaultDisplay density: " + density + " displayContent.mBaseDisplayDensity: " + displayContent.mBaseDisplayDensity + " userId: " + this.mCurrentUserId + " callers: " + android.os.Debug.getCallers(5));
        }
        if (density != 0 && density != displayContent.mBaseDisplayDensity) {
            displayContent.mBaseDisplayDensity = density;
            changed = true;
        }
        int mode = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "display_scaling_force", 0);
        if (displayContent.mDisplayScalingDisabled != (mode != 0)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[2]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 3781141652793604337L, 0, "FORCED DISPLAY SCALING DISABLED", null);
            }
            displayContent.mDisplayScalingDisabled = true;
            return true;
        }
        return changed;
    }

    public void clearForcedDisplaySize(int displayId) {
        clearForcedDisplaySize_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                    if (displayContent != null) {
                        displayContent.setForcedSize(displayContent.mInitialDisplayWidth, displayContent.mInitialDisplayHeight, displayContent.mInitialPhysicalXDpi, displayContent.mInitialPhysicalXDpi);
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public int getInitialDisplayDensity(int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent != null && displayContent.hasAccess(android.os.Binder.getCallingUid())) {
                    int initialDisplayDensity = displayContent.getInitialDisplayDensity();
                    resetPriorityAfterLockedSection();
                    return initialDisplayDensity;
                }
                android.view.DisplayInfo info = this.mDisplayManagerInternal.getDisplayInfo(displayId);
                if (info != null && info.hasAccess(android.os.Binder.getCallingUid())) {
                    int i = info.logicalDensityDpi;
                    resetPriorityAfterLockedSection();
                    return i;
                }
                resetPriorityAfterLockedSection();
                return -1;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public int getBaseDisplayDensity(int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent != null && displayContent.hasAccess(android.os.Binder.getCallingUid())) {
                    int i = displayContent.mBaseDisplayDensity;
                    resetPriorityAfterLockedSection();
                    return i;
                }
                resetPriorityAfterLockedSection();
                return -1;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public int getDisplayIdByUniqueId(java.lang.String uniqueId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(uniqueId);
                if (displayContent != null && displayContent.hasAccess(android.os.Binder.getCallingUid())) {
                    int i = displayContent.mDisplayId;
                    resetPriorityAfterLockedSection();
                    return i;
                }
                resetPriorityAfterLockedSection();
                return -1;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setForcedDisplayDensityForUser(int displayId, int density, int userId) {
        setForcedDisplayDensityForUser_enforcePermission();
        int targetUserId = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "setForcedDisplayDensityForUser", null);
        android.util.Slog.i(TAG, "setForcedDisplayDensityForUser  density:" + density + " pid:" + android.os.Binder.getCallingPid() + " uid:" + android.os.Binder.getCallingUid() + " caller:" + android.os.Debug.getCallers(5));
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                    if (displayContent != null) {
                        displayContent.setForcedDensity(density, targetUserId);
                        this.mWindowManagerServiceExt.onSetDensityForUser(density, userId);
                        android.view.DisplayInfo displayInfo = this.mWindowManagerServiceExt.getNeedForceSetDensityDisplayInfo(this, displayId, -1);
                        if (displayContent.isDefaultDisplay && displayInfo != null) {
                            this.mDisplayWindowSettings.setForcedDensity(displayInfo, density, userId);
                        }
                    } else {
                        android.view.DisplayInfo info = this.mDisplayManagerInternal.getDisplayInfo(displayId);
                        if (info != null) {
                            this.mDisplayWindowSettings.setForcedDensity(info, density, userId);
                        }
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void clearForcedDisplayDensityForUser(int displayId, int userId) {
        clearForcedDisplayDensityForUser_enforcePermission();
        int callingUserId = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "clearForcedDisplayDensityForUser", null);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                    if (displayContent != null) {
                        displayContent.setForcedDensity(displayContent.getInitialDisplayDensity(), callingUserId);
                        this.mWindowManagerServiceExt.onSetDensityForUser(displayContent.mInitialDisplayDensity, userId);
                        android.view.DisplayInfo displayInfo = this.mWindowManagerServiceExt.getNeedForceSetDensityDisplayInfo(this, displayId, -1);
                        if (displayContent.isDefaultDisplay && displayInfo != null) {
                            this.mDisplayWindowSettings.setForcedDensity(displayInfo, displayContent.mInitialDisplayDensity);
                        }
                    } else {
                        android.view.DisplayInfo info = this.mDisplayManagerInternal.getDisplayInfo(displayId);
                        if (info != null) {
                            this.mDisplayWindowSettings.setForcedDensity(info, info.logicalDensityDpi, userId);
                        }
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getForcedDisplayDensityForUserLocked(int userId) {
        java.lang.String densityStr = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "display_density_forced", userId);
        if (android.view.DynamicLoggerObserver.isLogToolRun()) {
            android.util.Slog.d(TAG, "getForcedDisplayDensityForUserLocked from DISPLAY_DENSITY_FORCED densityStr: " + densityStr + " userId: " + userId);
        }
        if (densityStr == null || densityStr.length() == 0) {
            densityStr = android.os.SystemProperties.get(DENSITY_OVERRIDE, (java.lang.String) null);
        }
        if (densityStr != null && densityStr.length() > 0) {
            try {
                return java.lang.Integer.parseInt(densityStr);
            } catch (java.lang.NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public void startWindowTrace() {
        this.mWindowTracing.startTrace(null);
    }

    public void stopWindowTrace() {
        this.mWindowTracing.stopTrace(null);
    }

    public void saveWindowTraceToFile() {
        this.mWindowTracing.saveForBugreport(null);
    }

    public boolean isWindowTraceEnabled() {
        return this.mWindowTracing.isEnabled();
    }

    public void startTransitionTrace() {
        this.mTransitionTracer.startTrace(null);
    }

    public void stopTransitionTrace() {
        this.mTransitionTracer.stopTrace(null);
    }

    public boolean isTransitionTraceEnabled() {
        return this.mTransitionTracer.isTracing();
    }

    public boolean registerCrossWindowBlurEnabledListener(android.view.ICrossWindowBlurEnabledListener listener) {
        return this.mBlurController.registerCrossWindowBlurEnabledListener(listener);
    }

    public void unregisterCrossWindowBlurEnabledListener(android.view.ICrossWindowBlurEnabledListener listener) {
        this.mBlurController.unregisterCrossWindowBlurEnabledListener(listener);
    }

    final com.android.server.wm.WindowState windowForClientLocked(com.android.server.wm.Session session, android.view.IWindow client, boolean throwOnError) {
        return windowForClientLocked(session, client.asBinder(), throwOnError);
    }

    final com.android.server.wm.WindowState windowForClientLocked(com.android.server.wm.Session session, android.os.IBinder client, boolean throwOnError) {
        com.android.server.wm.WindowState win = this.mWindowMap.get(client);
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
            android.util.Slog.v(TAG, "Looking up client " + client + ": " + win);
        }
        if (win == null) {
            if (throwOnError) {
                throw new java.lang.IllegalArgumentException("Requested window " + client + " does not exist");
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(session);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 4117606810523219596L, 0, "Failed looking up window session=%s callers=%s", protoLogParam0, protoLogParam1);
            }
            return null;
        }
        if (session != null && win.mSession != session) {
            if (throwOnError) {
                throw new java.lang.IllegalArgumentException("Requested window " + client + " is in session " + win.mSession + ", not " + session);
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(session);
                java.lang.String protoLogParam12 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 4117606810523219596L, 0, "Failed looking up window session=%s callers=%s", protoLogParam02, protoLogParam12);
            }
            return null;
        }
        return win;
    }

    void makeWindowFreezingScreenIfNeededLocked(com.android.server.wm.WindowState w) {
        if (this.mFrozenDisplayId != -1 && this.mFrozenDisplayId == w.getDisplayId() && this.mWindowsFreezingScreen != 2) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(w);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 1233670725456443473L, 0, null, protoLogParam0);
            }
            if (w.isVisibleRequested()) {
                w.setOrientationChanging(true);
            }
            if (!this.mWindowManagerServiceExt.shouldSkipUnFreezeCheck(w)) {
                this.mRoot.mOrientationChangeComplete = false;
            }
            if (this.mWindowsFreezingScreen == 0 && !this.mWindowManagerServiceExt.shouldSkipUnFreezeCheck(w)) {
                this.mWindowsFreezingScreen = 1;
                this.mH.sendNewMessageDelayed(11, w.getDisplayContent(), 2000L);
            }
        }
    }

    void checkDrawnWindowsLocked() {
        if (this.mWaitingForDrawnCallbacks.isEmpty() || this.mWindowManagerServiceExt.waitDrawForCameraVolumeQuickLaunch()) {
            return;
        }
        for (int i = this.mWaitingForDrawnCallbacks.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowContainer<?> container = this.mWaitingForDrawnCallbacks.keyAt(i);
            for (int j = container.mWaitingForDrawn.size() - 1; j >= 0; j--) {
                com.android.server.wm.WindowState win = container.mWaitingForDrawn.get(j);
                if (!com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON.isLogToLogcat()) {
                    android.util.Slog.i(TAG, java.lang.String.format("Waiting for drawn %s: removed=%b visible=%b mHasSurface=%b drawState=%d", win, java.lang.Boolean.valueOf(win.mRemoved), java.lang.Boolean.valueOf(win.isVisible()), java.lang.Boolean.valueOf(win.mHasSurface), java.lang.Integer.valueOf(win.mWinAnimator.mDrawState)));
                } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SCREEN_ON_enabled[2]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(win);
                    boolean protoLogParam1 = win.mRemoved;
                    boolean protoLogParam2 = win.isVisible();
                    boolean protoLogParam3 = win.mHasSurface;
                    long protoLogParam4 = win.mWinAnimator.mDrawState;
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, -1716033239040181528L, 508, null, protoLogParam0, java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3), java.lang.Long.valueOf(protoLogParam4));
                }
                if (this.mWindowManagerServiceExt.dontWaitDrawForCompactWindow(win) || this.mWindowManagerServiceExt.dontWaitDrawForFlexibleWindow(win)) {
                    container.mWaitingForDrawn.remove(win);
                } else if (!this.mWindowManagerServiceExt.shouldSkipCheckWindowDrawn(win)) {
                    if (win.mRemoved || !win.mHasSurface || !win.isVisibleByPolicy()) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SCREEN_ON_enabled[3]) {
                            java.lang.String protoLogParam02 = java.lang.String.valueOf(win);
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, -4609828204247499633L, 0, null, protoLogParam02);
                        }
                        container.mWaitingForDrawn.remove(win);
                        if (android.os.Trace.isTagEnabled(32L)) {
                            traceEndWaitingForWindowDrawn(win);
                        }
                    } else if (win.hasDrawn()) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SCREEN_ON_enabled[0]) {
                            java.lang.String protoLogParam03 = java.lang.String.valueOf(win);
                            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, -7561054602203220590L, 0, null, protoLogParam03);
                        }
                        container.mWaitingForDrawn.remove(win);
                        if (android.os.Trace.isTagEnabled(32L)) {
                            traceEndWaitingForWindowDrawn(win);
                        }
                    }
                }
            }
            if (container.mWaitingForDrawn.isEmpty()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SCREEN_ON_enabled[0]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, 2809030008663191766L, 0, null, null);
                }
                this.mH.removeMessages(24, container);
                this.mWaitingForDrawnCallbacks.removeAt(i).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void traceStartWaitingForWindowDrawn(com.android.server.wm.WindowState window) {
        java.lang.String traceName = "waitForAllWindowsDrawn#" + ((java.lang.Object) window.getWindowTag());
        java.lang.String shortenedTraceName = traceName.substring(0, java.lang.Math.min(127, traceName.length()));
        android.os.Trace.asyncTraceBegin(32L, shortenedTraceName, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void traceEndWaitingForWindowDrawn(com.android.server.wm.WindowState window) {
        java.lang.String traceName = "waitForAllWindowsDrawn#" + ((java.lang.Object) window.getWindowTag());
        java.lang.String shortenedTraceName = traceName.substring(0, java.lang.Math.min(127, traceName.length()));
        android.os.Trace.asyncTraceEnd(32L, shortenedTraceName, 0);
    }

    void requestTraversal() {
        this.mWindowPlacerLocked.requestTraversal();
    }

    void scheduleAnimationLocked() {
        this.mAnimator.scheduleAnimation();
    }

    boolean updateFocusedWindowLocked(int mode, boolean updateInputWindows) {
        android.os.Trace.traceBegin(32L, "wmUpdateFocus");
        boolean changed = this.mRoot.updateFocusedWindowLocked(mode, updateInputWindows);
        android.os.Trace.traceEnd(32L);
        return changed;
    }

    void startFreezingDisplay(int exitAnim, int enterAnim) {
        startFreezingDisplay(exitAnim, enterAnim, getDefaultDisplayContentLocked());
    }

    void startFreezingDisplay(int exitAnim, int enterAnim, com.android.server.wm.DisplayContent displayContent) {
        startFreezingDisplay(exitAnim, enterAnim, displayContent, -1);
    }

    void startFreezingDisplay(final int exitAnim, final int enterAnim, final com.android.server.wm.DisplayContent displayContent, final int overrideOriginalRotation) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!this.mDisplayFrozen && !displayContent.getDisplayRotation().isRotatingSeamlessly()) {
                    if (displayContent.isReady() && displayContent.getDisplayPolicy().isScreenOnFully() && displayContent.getDisplayInfo().state != 1 && displayContent.okToAnimate()) {
                        if (this.mWindowManagerServiceExt.skipFreezingDisplayIfNeed(displayContent)) {
                            resetPriorityAfterLockedSection();
                            return;
                        }
                        android.util.Slog.i(TAG, "startFreezingDisplay display " + displayContent.getDisplayId());
                        this.mWindowManagerServiceExt.notifySysWindowRotation(com.android.server.wm.WindowManagerService.class, null);
                        displayContent.requestDisplayUpdate(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$startFreezingDisplay$13(exitAnim, enterAnim, displayContent, overrideOriginalRotation);
                            }
                        });
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startFreezingDisplay$13(int exitAnim, int enterAnim, com.android.server.wm.DisplayContent displayContent, int overrideOriginalRotation) {
        android.os.Trace.traceBegin(32L, "WMS.doStartFreezingDisplay");
        doStartFreezingDisplay(exitAnim, enterAnim, displayContent, overrideOriginalRotation);
        android.os.Trace.traceEnd(32L);
    }

    private void doStartFreezingDisplay(int exitAnim, int enterAnim, com.android.server.wm.DisplayContent displayContent, int overrideOriginalRotation) {
        int originalRotation;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[0]) {
            long protoLogParam0 = exitAnim;
            long protoLogParam1 = enterAnim;
            java.lang.String protoLogParam2 = java.lang.String.valueOf(android.os.Debug.getCallers(8));
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -1615905649072328410L, 5, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1), protoLogParam2);
        }
        if (android.os.Binder.getCallingUid() != android.os.Process.myUid()) {
            android.util.Slog.e(TAG, " acquire mScreenFrozenLock , binder callerUid:" + android.os.Binder.getCallingUid() + " caller from: " + android.util.Log.getStackTraceString(new java.lang.Throwable()));
        }
        this.mScreenFrozenLock.acquire();
        this.mAtmService.startPowerMode(2);
        this.mDisplayFrozen = true;
        this.mDisplayFreezeTime = android.os.SystemClock.elapsedRealtime();
        this.mLastFinishedFreezeSource = null;
        this.mFrozenDisplayId = displayContent.getDisplayId();
        this.mInputManagerCallback.freezeInputDispatchingLw();
        if (displayContent.mAppTransition.isTransitionSet()) {
            displayContent.mAppTransition.freeze();
        }
        this.mLatencyTracker.onActionStart(6);
        this.mWindowManagerServiceSocExt.hookStartFreezingDisplay();
        this.mWindowManagerServiceExt.pokeDynamicVsyncAnimation(3500, "FreezingDisplay");
        this.mExitAnimId = exitAnim;
        this.mEnterAnimId = enterAnim;
        if (overrideOriginalRotation != -1) {
            originalRotation = overrideOriginalRotation;
        } else {
            originalRotation = displayContent.getDisplayInfo().rotation;
        }
        displayContent.setRotationAnimation(new com.android.server.wm.ScreenRotationAnimation(displayContent, originalRotation));
        this.mWindowManagerServiceExt.checkScreenFreezingTimeOut(true);
        cancelRecentsAnimation(0, "freeze");
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM) {
            android.util.Slog.d(TAG, "cancelRecentsAnimation by FREEZE");
        }
    }

    void stopFreezingDisplayLocked() {
        boolean waitingForRemoteDisplayChange;
        boolean waitingForConfig;
        int numOpeningApps;
        if (this.mDisplayFrozen) {
            com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(this.mFrozenDisplayId);
            if (displayContent != null) {
                numOpeningApps = displayContent.mOpeningApps.size();
                waitingForConfig = displayContent.mWaitingForConfig;
                waitingForRemoteDisplayChange = displayContent.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange();
            } else {
                waitingForRemoteDisplayChange = false;
                waitingForConfig = false;
                numOpeningApps = 0;
            }
            if ((!waitingForConfig && !waitingForRemoteDisplayChange && this.mAppsFreezingScreen <= 0 && this.mWindowsFreezingScreen != 1 && !this.mClientFreezingScreen && numOpeningApps <= 0 && !this.mWindowManagerServiceExt.shouldwaitingForFolded()) || this.mWindowManagerServiceExt.shouldForceStopFreezingScreen()) {
                android.os.Trace.traceBegin(32L, "WMS.doStopFreezingDisplayLocked-" + this.mLastFinishedFreezeSource);
                doStopFreezingDisplayLocked(displayContent);
                android.os.Trace.traceEnd(32L);
            } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[0]) {
                boolean protoLogParam0 = waitingForConfig;
                boolean protoLogParam1 = waitingForRemoteDisplayChange;
                long protoLogParam2 = this.mAppsFreezingScreen;
                long protoLogParam3 = this.mWindowsFreezingScreen;
                boolean protoLogParam4 = this.mClientFreezingScreen;
                long protoLogParam5 = numOpeningApps;
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 4565793239453546297L, 1887, null, java.lang.Boolean.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2), java.lang.Long.valueOf(protoLogParam3), java.lang.Boolean.valueOf(protoLogParam4), java.lang.Long.valueOf(protoLogParam5));
            }
        }
    }

    private void doStopFreezingDisplayLocked(com.android.server.wm.DisplayContent displayContent) {
        com.android.server.wm.ScreenRotationAnimation screenRotationAnimation;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -6877112251967196129L, 0, null, null);
        }
        this.mWindowManagerServiceExt.speedWallpaperShowIfNeeded(displayContent);
        this.mFrozenDisplayId = -1;
        this.mDisplayFrozen = false;
        this.mInputManagerCallback.thawInputDispatchingLw();
        this.mLastDisplayFreezeDuration = (int) (android.os.SystemClock.elapsedRealtime() - this.mDisplayFreezeTime);
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("Screen frozen for ");
        android.util.TimeUtils.formatDuration(this.mLastDisplayFreezeDuration, sb);
        if (this.mLastFinishedFreezeSource != null) {
            sb.append(" due to ");
            sb.append(this.mLastFinishedFreezeSource);
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(sb.toString());
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 721393258715103117L, 0, "%s", protoLogParam0);
        }
        this.mH.removeMessages(17);
        this.mH.removeMessages(30);
        this.mWindowManagerServiceExt.checkScreenFreezingTimeOut(false);
        boolean updateRotation = false;
        if (displayContent == null) {
            screenRotationAnimation = null;
        } else {
            screenRotationAnimation = displayContent.getRotationAnimation();
        }
        if (screenRotationAnimation != null && screenRotationAnimation.hasScreenshot()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[2]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -5706083447992207254L, 0, null, null);
            }
            android.view.DisplayInfo displayInfo = displayContent.getDisplayInfo();
            if (!displayContent.getDisplayRotation().validateRotationAnimation(this.mExitAnimId, this.mEnterAnimId, false)) {
                this.mEnterAnimId = 0;
                this.mExitAnimId = 0;
            }
            if (screenRotationAnimation.dismiss(this.mTransaction, 10000L, getTransitionAnimationScaleLocked(), displayInfo.logicalWidth, displayInfo.logicalHeight, this.mExitAnimId, this.mEnterAnimId)) {
                this.mTransaction.apply();
            } else {
                screenRotationAnimation.kill();
                displayContent.setRotationAnimation(null);
                updateRotation = true;
                this.mWindowManagerServiceExt.setFrozenByUserSwitching(false);
            }
        } else {
            if (screenRotationAnimation != null) {
                screenRotationAnimation.kill();
                displayContent.setRotationAnimation(null);
            }
            updateRotation = true;
            this.mWindowManagerServiceExt.setFrozenByUserSwitching(false);
        }
        this.mWindowManagerServiceExt.onStopFreezingDisplayLocked();
        boolean configChanged = displayContent != null && displayContent.updateOrientation();
        if (displayContent != null) {
            boolean isNightMode = displayContent.getConfiguration() != null && (displayContent.getConfiguration().uiMode & 48) == 32;
            this.mWindowManagerServiceExt.handleUiModeChanged(java.lang.Boolean.valueOf(isNightMode));
        }
        this.mScreenFrozenLock.release();
        if (updateRotation && displayContent != null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 2233371241933584073L, 0, null, null);
            }
            configChanged |= displayContent.updateRotationUnchecked();
        }
        if (configChanged) {
            displayContent.sendNewConfiguration();
        }
        this.mAtmService.endPowerMode(2);
        this.mLatencyTracker.onActionEnd(6);
        this.mWindowManagerServiceSocExt.hookStopFreezingDisplayLocked();
        this.mWindowManagerServiceExt.endHookstopFreezingDisplayLocked(sb.toString());
    }

    static int getPropertyInt(java.lang.String[] tokens, int index, int defUnits, int defDps, android.util.DisplayMetrics dm) {
        java.lang.String str;
        if (index < tokens.length && (str = tokens[index]) != null && str.length() > 0) {
            try {
                int val = java.lang.Integer.parseInt(str);
                return val;
            } catch (java.lang.Exception e) {
            }
        }
        if (defUnits == 0) {
            return defDps;
        }
        int val2 = (int) android.util.TypedValue.applyDimension(defUnits, defDps, dm);
        return val2;
    }

    public void showCustomizeWatermark(boolean flag) {
        this.mWindowManagerServiceExt.showCustomizeWatermark(flag, this.mContext, getDefaultDisplayContentLocked(), this.mTransaction);
    }

    void createWatermark() {
        java.lang.String[] toks;
        if (this.mWatermark != null) {
            return;
        }
        this.mWatermark = this.mWindowManagerServiceExt.showWatermark(getDefaultDisplayContentLocked(), this.mWatermark, this.mTransaction);
        if (this.mWatermark != null) {
            return;
        }
        java.io.File file = new java.io.File("/system/etc/setup.conf");
        java.io.FileInputStream in = null;
        java.io.DataInputStream ind = null;
        try {
            try {
                try {
                    try {
                        in = new java.io.FileInputStream(file);
                        ind = new java.io.DataInputStream(in);
                        java.lang.String line = ind.readLine();
                        if (line != null && (toks = line.split("%")) != null && toks.length > 0) {
                            com.android.server.wm.DisplayContent displayContent = getDefaultDisplayContentLocked();
                            this.mWatermark = new com.android.server.wm.Watermark(displayContent, displayContent.mRealDisplayMetrics, toks, this.mTransaction);
                            this.mTransaction.apply();
                        }
                        ind.close();
                    } catch (java.io.IOException e) {
                    }
                } catch (java.io.IOException e2) {
                    if (ind != null) {
                        ind.close();
                    } else if (in != null) {
                        in.close();
                    }
                }
            } catch (java.io.FileNotFoundException e3) {
                if (ind != null) {
                    ind.close();
                } else if (in != null) {
                    in.close();
                }
            } catch (java.lang.Throwable th) {
                if (ind != null) {
                    try {
                        ind.close();
                    } catch (java.io.IOException e4) {
                    }
                } else if (in != null) {
                    try {
                        in.close();
                    } catch (java.io.IOException e5) {
                    }
                }
                throw th;
            }
        } catch (java.io.IOException e6) {
        }
    }

    public void setRecentsVisibility(boolean visible) {
        if (!checkCallingPermission("android.permission.STATUS_BAR", "setRecentsVisibility()")) {
            throw new java.lang.SecurityException("Requires STATUS_BAR permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mPolicy.setRecentsVisibilityLw(visible);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void hideTransientBars(int displayId) {
        if (!checkCallingPermission("android.permission.STATUS_BAR", "hideTransientBars()")) {
            throw new java.lang.SecurityException("Requires STATUS_BAR permission");
        }
        android.util.Slog.i(TAG, "hideTransientBars  displayId:" + displayId);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent != null) {
                    displayContent.getInsetsPolicy().hideTransient();
                } else {
                    android.util.Slog.w(TAG, "hideTransientBars with invalid displayId=" + displayId);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void updateStaticPrivacyIndicatorBounds(int displayId, android.graphics.Rect[] staticBounds) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent != null) {
                    displayContent.updatePrivacyIndicatorBounds(staticBounds);
                } else {
                    android.util.Slog.w(TAG, "updateStaticPrivacyIndicatorBounds with invalid displayId=" + displayId);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void setNavBarVirtualKeyHapticFeedbackEnabled(boolean enabled) {
        setNavBarVirtualKeyHapticFeedbackEnabled_enforcePermission();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mPolicy.setNavBarVirtualKeyHapticFeedbackEnabledLw(enabled);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void createInputConsumer(android.os.IBinder token, java.lang.String name, int displayId, android.view.InputChannel inputChannel) {
        if (!this.mAtmService.isCallerRecents(android.os.Binder.getCallingUid()) && this.mContext.checkCallingOrSelfPermission("android.permission.INPUT_CONSUMER") != 0) {
            throw new java.lang.SecurityException("createInputConsumer requires INPUT_CONSUMER permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent display = this.mRoot.getDisplayContent(displayId);
                if (display != null) {
                    display.getInputMonitor().createInputConsumer(token, name, inputChannel, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUserHandle());
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public boolean destroyInputConsumer(android.os.IBinder token, int displayId) {
        if (!this.mAtmService.isCallerRecents(android.os.Binder.getCallingUid()) && this.mContext.checkCallingOrSelfPermission("android.permission.INPUT_CONSUMER") != 0) {
            throw new java.lang.SecurityException("destroyInputConsumer requires INPUT_CONSUMER permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent display = this.mRoot.getDisplayContent(displayId);
                if (display == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean zDestroyInputConsumer = display.getInputMonitor().destroyInputConsumer(token);
                resetPriorityAfterLockedSection();
                return zDestroyInputConsumer;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
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
    public android.graphics.Region getCurrentImeTouchRegion() {
        getCurrentImeTouchRegion_enforcePermission();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                android.graphics.Region r = new android.graphics.Region();
                for (int i = this.mRoot.mChildren.size() - 1; i >= 0; i--) {
                    com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) this.mRoot.mChildren.get(i);
                    if (displayContent.mInputMethodWindow != null) {
                        displayContent.mInputMethodWindow.getTouchableRegion(r);
                        resetPriorityAfterLockedSection();
                        return r;
                    }
                }
                resetPriorityAfterLockedSection();
                return r;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean hasNavigationBar(int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(displayId);
                if (dc == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean zHasNavigationBar = dc.getDisplayPolicy().hasNavigationBar();
                resetPriorityAfterLockedSection();
                return zHasNavigationBar;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void lockNow(android.os.Bundle options) {
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        android.util.Slog.d(TAG, "lockNow with options called by uid: " + callingUid + " pid: " + callingPid + ",call" + android.os.Debug.getCallers(3));
        this.mPolicy.lockNow(options);
    }

    public void showRecentApps() {
        this.mPolicy.showRecentApps();
    }

    public boolean isSafeModeEnabled() {
        return this.mSafeMode;
    }

    public boolean clearWindowContentFrameStats(android.os.IBinder token) {
        if (!checkCallingPermission("android.permission.FRAME_STATS", "clearWindowContentFrameStats()")) {
            throw new java.lang.SecurityException("Requires FRAME_STATS permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState windowState = this.mWindowMap.get(token);
                if (windowState == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                com.android.server.wm.WindowSurfaceController surfaceController = windowState.mWinAnimator.mSurfaceController;
                if (surfaceController == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean zClearWindowContentFrameStats = surfaceController.clearWindowContentFrameStats();
                resetPriorityAfterLockedSection();
                return zClearWindowContentFrameStats;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public android.view.WindowContentFrameStats getWindowContentFrameStats(android.os.IBinder token) {
        if (!checkCallingPermission("android.permission.FRAME_STATS", "getWindowContentFrameStats()")) {
            throw new java.lang.SecurityException("Requires FRAME_STATS permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState windowState = this.mWindowMap.get(token);
                if (windowState == null) {
                    resetPriorityAfterLockedSection();
                    return null;
                }
                com.android.server.wm.WindowSurfaceController surfaceController = windowState.mWinAnimator.mSurfaceController;
                if (surfaceController == null) {
                    resetPriorityAfterLockedSection();
                    return null;
                }
                if (this.mTempWindowRenderStats == null) {
                    this.mTempWindowRenderStats = new android.view.WindowContentFrameStats();
                }
                android.view.WindowContentFrameStats stats = this.mTempWindowRenderStats;
                if (surfaceController.getWindowContentFrameStats(stats)) {
                    resetPriorityAfterLockedSection();
                    return stats;
                }
                resetPriorityAfterLockedSection();
                return null;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void dumpPolicyLocked(java.io.PrintWriter pw, java.lang.String[] args) {
        pw.println("WINDOW MANAGER POLICY STATE (dumpsys window policy)");
        this.mPolicy.dump("    ", pw, args);
    }

    private void dumpAnimatorLocked(java.io.PrintWriter pw, boolean dumpAll) {
        pw.println("WINDOW MANAGER ANIMATOR STATE (dumpsys window animator)");
        this.mAnimator.dumpLocked(pw, "    ", dumpAll);
    }

    private void dumpTokensLocked(java.io.PrintWriter pw, boolean dumpAll) {
        pw.println("WINDOW MANAGER TOKENS (dumpsys window tokens)");
        this.mRoot.dumpTokens(pw, dumpAll);
    }

    private void dumpHighRefreshRateBlacklist(java.io.PrintWriter pw) {
        pw.println("WINDOW MANAGER HIGH REFRESH RATE BLACKLIST (dumpsys window refresh)");
        this.mHighRefreshRateDenylist.dump(pw);
    }

    private void dumpTraceStatus(java.io.PrintWriter pw) {
        pw.println("WINDOW MANAGER TRACE (dumpsys window trace)");
        pw.print(this.mWindowTracing.getStatus() + "\n");
    }

    private void dumpLogStatus(java.io.PrintWriter pw) {
        pw.println("WINDOW MANAGER LOGGING (dumpsys window logging)");
        if (android.tracing.Flags.perfettoProtologTracing()) {
            pw.println("Deprecated legacy command. Use Perfetto commands instead.");
        } else {
            com.android.internal.protolog.ProtoLogImpl_209941506.getSingleInstance().getStatus();
        }
    }

    private void dumpSessionsLocked(java.io.PrintWriter pw) {
        pw.println("WINDOW MANAGER SESSIONS (dumpsys window sessions)");
        for (int i = 0; i < this.mSessions.size(); i++) {
            com.android.server.wm.Session s = this.mSessions.valueAt(i);
            pw.print("  Session ");
            pw.print(s);
            pw.println(':');
            s.dump(pw, "    ");
        }
    }

    void dumpDebugLocked(android.util.proto.ProtoOutputStream proto, int logLevel) {
        this.mPolicy.dumpDebug(proto, 1146756268033L);
        this.mRoot.dumpDebug(proto, 1146756268034L, logLevel);
        com.android.server.wm.DisplayContent topFocusedDisplayContent = this.mRoot.getTopFocusedDisplayContent();
        if (topFocusedDisplayContent.mCurrentFocus != null) {
            topFocusedDisplayContent.mCurrentFocus.writeIdentifierToProto(proto, 1146756268035L);
        }
        if (topFocusedDisplayContent.mFocusedApp != null) {
            topFocusedDisplayContent.mFocusedApp.writeNameToProto(proto, 1138166333444L);
        }
        com.android.server.wm.WindowState imeWindow = this.mRoot.getCurrentInputMethodWindow();
        if (imeWindow != null) {
            imeWindow.writeIdentifierToProto(proto, 1146756268037L);
        }
        proto.write(1133871366150L, this.mDisplayFrozen);
        proto.write(1120986464265L, topFocusedDisplayContent.getDisplayId());
        proto.write(1133871366154L, this.mHardKeyboardAvailable);
        proto.write(1133871366155L, true);
        this.mAtmService.mBackNavigationController.dumpDebug(proto, 1146756268044L);
    }

    private void dumpWindowsLocked(final java.io.PrintWriter pw, boolean dumpAll, java.util.ArrayList<com.android.server.wm.WindowState> windows) {
        pw.println("WINDOW MANAGER WINDOWS (dumpsys window windows)");
        this.mRoot.dumpWindowsNoHeader(pw, dumpAll, windows);
        if (!this.mHidingNonSystemOverlayWindows.isEmpty()) {
            pw.println();
            pw.println("  Hiding System Alert Windows:");
            for (int i = this.mHidingNonSystemOverlayWindows.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowState w = this.mHidingNonSystemOverlayWindows.get(i);
                pw.print("  #");
                pw.print(i);
                pw.print(' ');
                pw.print(w);
                if (dumpAll) {
                    pw.println(":");
                    w.dump(pw, "    ", true);
                } else {
                    pw.println();
                }
            }
        }
        if (this.mForceRemoves != null && !this.mForceRemoves.isEmpty()) {
            pw.println();
            pw.println("  Windows force removing:");
            for (int i2 = this.mForceRemoves.size() - 1; i2 >= 0; i2--) {
                com.android.server.wm.WindowState w2 = this.mForceRemoves.get(i2);
                pw.print("  Removing #");
                pw.print(i2);
                pw.print(' ');
                pw.print(w2);
                if (dumpAll) {
                    pw.println(":");
                    w2.dump(pw, "    ", true);
                } else {
                    pw.println();
                }
            }
        }
        if (!this.mDestroySurface.isEmpty()) {
            pw.println();
            pw.println("  Windows waiting to destroy their surface:");
            for (int i3 = this.mDestroySurface.size() - 1; i3 >= 0; i3--) {
                com.android.server.wm.WindowState w3 = this.mDestroySurface.get(i3);
                if (windows == null || windows.contains(w3)) {
                    pw.print("  Destroy #");
                    pw.print(i3);
                    pw.print(' ');
                    pw.print(w3);
                    if (dumpAll) {
                        pw.println(":");
                        w3.dump(pw, "    ", true);
                    } else {
                        pw.println();
                    }
                }
            }
        }
        if (!this.mResizingWindows.isEmpty()) {
            pw.println();
            pw.println("  Windows waiting to resize:");
            for (int i4 = this.mResizingWindows.size() - 1; i4 >= 0; i4--) {
                com.android.server.wm.WindowState w4 = this.mResizingWindows.get(i4);
                if (windows == null || windows.contains(w4)) {
                    pw.print("  Resizing #");
                    pw.print(i4);
                    pw.print(' ');
                    pw.print(w4);
                    if (dumpAll) {
                        pw.println(":");
                        w4.dump(pw, "    ", true);
                    } else {
                        pw.println();
                    }
                }
            }
        }
        if (!this.mWaitingForDrawnCallbacks.isEmpty()) {
            pw.println();
            pw.println("  Clients waiting for these windows to be drawn:");
            this.mWaitingForDrawnCallbacks.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda8
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    com.android.server.wm.WindowManagerService.lambda$dumpWindowsLocked$14(pw, (com.android.server.wm.WindowContainer) obj, (android.os.Message) obj2);
                }
            });
        }
        pw.println();
        pw.print("  mGlobalConfiguration=");
        pw.println(this.mRoot.getConfiguration());
        pw.print("  mHasPermanentDpad=");
        pw.println(this.mHasPermanentDpad);
        this.mRoot.dumpTopFocusedDisplayId(pw);
        this.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.WindowManagerService.lambda$dumpWindowsLocked$15(pw, (com.android.server.wm.DisplayContent) obj);
            }
        });
        pw.print("  mBlurEnabled=");
        pw.println(this.mBlurController.getBlurEnabled());
        pw.print("  mLastDisplayFreezeDuration=");
        android.util.TimeUtils.formatDuration(this.mLastDisplayFreezeDuration, pw);
        if (this.mLastFinishedFreezeSource != null) {
            pw.print(" due to ");
            pw.print(this.mLastFinishedFreezeSource);
        }
        pw.println();
        pw.print("  mDisableSecureWindows=");
        pw.println(this.mDisableSecureWindows);
        this.mInputManagerCallback.dump(pw, "  ");
        this.mSnapshotController.dump(pw, " ");
        dumpAccessibilityController(pw, false);
        if (dumpAll) {
            com.android.server.wm.WindowState imeWindow = this.mRoot.getCurrentInputMethodWindow();
            if (imeWindow != null) {
                pw.print("  mInputMethodWindow=");
                pw.println(imeWindow);
            }
            this.mWindowPlacerLocked.dump(pw, "  ");
            pw.print("  mSystemBooted=");
            pw.print(this.mSystemBooted);
            pw.print(" mDisplayEnabled=");
            pw.println(this.mDisplayEnabled);
            this.mRoot.dumpLayoutNeededDisplayIds(pw);
            pw.print("  mTransactionSequence=");
            pw.println(this.mTransactionSequence);
            pw.print("  mDisplayFrozen=");
            pw.print(this.mDisplayFrozen);
            pw.print(" windows=");
            pw.print(this.mWindowsFreezingScreen);
            pw.print(" client=");
            pw.print(this.mClientFreezingScreen);
            pw.print(" apps=");
            pw.println(this.mAppsFreezingScreen);
            com.android.server.wm.DisplayContent defaultDisplayContent = getDefaultDisplayContentLocked();
            pw.print("  mRotation=");
            pw.println(defaultDisplayContent.getRotation());
            pw.print("  mLastOrientation=");
            pw.println(defaultDisplayContent.getLastOrientation());
            pw.print("  mWaitingForConfig=");
            pw.println(defaultDisplayContent.mWaitingForConfig);
            pw.print("  mWindowsInsetsChanged=");
            pw.println(this.mWindowsInsetsChanged);
            this.mRotationWatcherController.dump(pw);
            pw.print("  Animation settings: disabled=");
            pw.print(this.mAnimationsDisabled);
            pw.print(" window=");
            pw.print(this.mWindowAnimationScaleSetting);
            pw.print(" transition=");
            pw.print(this.mTransitionAnimationScaleSetting);
            pw.print(" animator=");
            pw.println(this.mAnimatorDurationScaleSetting);
            if (this.mRecentsAnimationController != null) {
                pw.print("  mRecentsAnimationController=");
                pw.println(this.mRecentsAnimationController);
                this.mRecentsAnimationController.dump(pw, "    ");
            }
        }
    }

    static /* synthetic */ void lambda$dumpWindowsLocked$14(java.io.PrintWriter pw, com.android.server.wm.WindowContainer wc, android.os.Message callback) {
        pw.print("  WindowContainer ");
        pw.println(wc.getName());
        for (int i = wc.mWaitingForDrawn.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState win = wc.mWaitingForDrawn.get(i);
            pw.print("  Waiting #");
            pw.print(i);
            pw.print(' ');
            pw.print(win);
        }
    }

    static /* synthetic */ void lambda$dumpWindowsLocked$15(java.io.PrintWriter pw, com.android.server.wm.DisplayContent dc) {
        int displayId = dc.getDisplayId();
        com.android.server.wm.InsetsControlTarget imeLayeringTarget = dc.getImeTarget(0);
        com.android.server.wm.InputTarget imeInputTarget = dc.getImeInputTarget();
        com.android.server.wm.InsetsControlTarget imeControlTarget = dc.getImeTarget(2);
        if (imeLayeringTarget != null) {
            pw.print("  imeLayeringTarget in display# ");
            pw.print(displayId);
            pw.print(' ');
            pw.println(imeLayeringTarget);
        }
        if (imeInputTarget != null) {
            pw.print("  imeInputTarget in display# ");
            pw.print(displayId);
            pw.print(' ');
            pw.println(imeInputTarget);
        }
        if (imeControlTarget != null) {
            pw.print("  imeControlTarget in display# ");
            pw.print(displayId);
            pw.print(' ');
            pw.println(imeControlTarget);
        }
        pw.print("  Minimum task size of display#");
        pw.print(displayId);
        pw.print(' ');
        pw.print(dc.mMinSizeOfResizeableTaskDp);
        pw.print("  displayConfiguration=");
        pw.println(dc.getConfiguration());
    }

    private void dumpAccessibilityController(java.io.PrintWriter pw, boolean force) {
        boolean hasCallbacks = this.mAccessibilityController.hasCallbacks();
        if (!hasCallbacks && !force) {
            return;
        }
        if (!hasCallbacks) {
            pw.println("AccessibilityController doesn't have callbacks, but printing it anways:");
        } else {
            pw.println("AccessibilityController:");
        }
        this.mAccessibilityController.dump(pw, "  ");
    }

    private void dumpAccessibilityLocked(java.io.PrintWriter pw) {
        dumpAccessibilityController(pw, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean dumpWindows(java.io.PrintWriter pw, java.lang.String name, boolean dumpAll) {
        final java.util.ArrayList<com.android.server.wm.WindowState> windows = new java.util.ArrayList<>();
        if ("apps".equals(name) || com.android.server.wm.ActivityTaskManagerService.DUMP_VISIBLE_ACTIVITIES.equals(name) || "visible-apps".equals(name)) {
            final boolean appsOnly = name.contains("apps");
            final boolean visibleOnly = name.contains(com.android.server.wm.ActivityTaskManagerService.DUMP_VISIBLE_ACTIVITIES);
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                if (appsOnly) {
                    try {
                        this.mRoot.dumpDisplayContents(pw);
                    } finally {
                        resetPriorityAfterLockedSection();
                    }
                }
                this.mRoot.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda33
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wm.WindowManagerService.lambda$dumpWindows$16(visibleOnly, appsOnly, windows, (com.android.server.wm.WindowState) obj);
                    }
                }, true);
            }
            resetPriorityAfterLockedSection();
        } else {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock2) {
                try {
                    this.mRoot.getWindowsByName(windows, name);
                } finally {
                }
            }
            resetPriorityAfterLockedSection();
        }
        if (windows.isEmpty()) {
            return false;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock3 = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock3) {
            try {
                dumpWindowsLocked(pw, dumpAll, windows);
            } finally {
            }
        }
        resetPriorityAfterLockedSection();
        return true;
    }

    static /* synthetic */ void lambda$dumpWindows$16(boolean visibleOnly, boolean appsOnly, java.util.ArrayList windows, com.android.server.wm.WindowState w) {
        if (!visibleOnly || w.isVisible()) {
            if (!appsOnly || w.mActivityRecord != null) {
                windows.add(w);
            }
        }
    }

    private void dumpLastANRLocked(java.io.PrintWriter pw) {
        pw.println("WINDOW MANAGER LAST ANR (dumpsys window lastanr)");
        if (this.mLastANRState == null) {
            pw.println("  <no ANR has occurred since boot>");
        } else {
            pw.println(this.mLastANRState);
        }
    }

    void saveANRStateLocked(com.android.server.wm.ActivityRecord activity, com.android.server.wm.WindowState windowState, java.lang.String reason) {
        java.io.StringWriter sw = new java.io.StringWriter();
        final java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(sw, false, 1024);
        pw.println("  ANR time: " + java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));
        if (activity != null) {
            pw.println("  Application at fault: " + activity.stringName);
        }
        if (windowState != null) {
            pw.println("  Window at fault: " + ((java.lang.Object) windowState.mAttrs.getTitle()));
        }
        if (reason != null) {
            pw.println("  Reason: " + reason);
        }
        pw.println();
        final java.util.ArrayList<com.android.server.wm.WindowState> relatedWindows = new java.util.ArrayList<>();
        for (int i = this.mRoot.getChildCount() - 1; i >= 0; i--) {
            com.android.server.wm.DisplayContent dc = (com.android.server.wm.DisplayContent) this.mRoot.getChildAt(i);
            int displayId = dc.getDisplayId();
            final com.android.server.wm.WindowState currentFocus = dc.mCurrentFocus;
            final com.android.server.wm.ActivityRecord focusedApp = dc.mFocusedApp;
            pw.println("  Display #" + displayId + " currentFocus=" + currentFocus + " focusedApp=" + focusedApp);
            if (!dc.mWinAddedSinceNullFocus.isEmpty()) {
                pw.println("  Windows added in display #" + displayId + " since null focus: " + dc.mWinAddedSinceNullFocus);
            }
            if (!dc.mWinRemovedSinceNullFocus.isEmpty()) {
                pw.println("  Windows removed in display #" + displayId + " since null focus: " + dc.mWinRemovedSinceNullFocus);
            }
            pw.println("  Tasks in top down Z order:");
            dc.forAllTaskDisplayAreas(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda25
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wm.TaskDisplayArea) obj).dump(pw, "    ", false);
                }
            });
            dc.getInputMonitor().dump(pw, "  ");
            pw.println();
            dc.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda26
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.WindowManagerService.lambda$saveANRStateLocked$18(currentFocus, focusedApp, relatedWindows, (com.android.server.wm.WindowState) obj);
                }
            }, true);
        }
        if (windowState != null && !relatedWindows.contains(windowState)) {
            relatedWindows.add(windowState);
        }
        this.mRoot.dumpWindowsNoHeader(pw, true, relatedWindows);
        pw.println();
        pw.close();
        this.mLastANRState = sw.toString();
        this.mH.removeMessages(38);
        this.mH.sendEmptyMessageDelayed(38, com.android.server.usage.AppStandbyController.ConstantsObserver.DEFAULT_SYSTEM_UPDATE_TIMEOUT);
    }

    static /* synthetic */ void lambda$saveANRStateLocked$18(com.android.server.wm.WindowState currentFocus, com.android.server.wm.ActivityRecord focusedApp, java.util.ArrayList relatedWindows, com.android.server.wm.WindowState w) {
        if ((currentFocus != null && java.util.Objects.equals(w.mAttrs.packageName, currentFocus.mAttrs.packageName)) || (focusedApp != null && java.util.Objects.equals(w.mAttrs.packageName, focusedApp.packageName))) {
            relatedWindows.add(w);
        }
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        com.android.server.utils.PriorityDump.dump(this.mPriorityDumper, fd, pw, args);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @dalvik.annotation.optimization.NeverCompile
    public void doDump(java.io.FileDescriptor fd, final java.io.PrintWriter pw, java.lang.String[] args, boolean useProto) {
        java.lang.String opt;
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            if (!useProto) {
                pw.println("---- TIME " + java.text.SimpleDateFormat.getDateTimeInstance(3, 2).format(java.lang.Long.valueOf(java.lang.System.currentTimeMillis())) + " ----");
            }
            int opti = 0;
            boolean dumpAll = false;
            while (opti < args.length && (opt = args[opti]) != null && opt.length() > 0 && opt.charAt(0) == '-') {
                opti++;
                if ("-a".equals(opt)) {
                    dumpAll = true;
                } else {
                    if ("-h".equals(opt)) {
                        pw.println("Window manager dump options:");
                        pw.println("  [-a] [-h] [cmd] ...");
                        pw.println("  cmd may be one of:");
                        pw.println("    l[astanr]: last ANR information");
                        pw.println("    p[policy]: policy state");
                        pw.println("    a[animator]: animator state");
                        pw.println("    s[essions]: active sessions");
                        pw.println("    surfaces: active surfaces (debugging enabled only)");
                        pw.println("    d[isplays]: active display contents");
                        pw.println("    t[okens]: token list");
                        pw.println("    w[indows]: window list");
                        pw.println("    a11y[accessibility]: accessibility-related state");
                        pw.println("    package-config: installed packages having app-specific config");
                        pw.println("    trace: print trace status and write Winscope trace to file");
                        pw.println("  cmd may also be a NAME to dump windows.  NAME may");
                        pw.println("    be a partial substring in a window name, a");
                        pw.println("    Window hex object identifier, or");
                        pw.println("    \"all\" for all windows, or");
                        pw.println("    \"visible\" for the visible windows.");
                        pw.println("    \"visible-apps\" for the visible app windows.");
                        pw.println("  -a: include all available server state.");
                        pw.println("  --proto: output dump in protocol buffer format.");
                        return;
                    }
                    pw.println("Unknown argument: " + opt + "; use -h for help");
                }
            }
            if (useProto) {
                android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
                boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        dumpDebugLocked(proto, 0);
                    } finally {
                    }
                }
                resetPriorityAfterLockedSection();
                proto.flush();
                return;
            }
            if (opti < args.length) {
                java.lang.String cmd = args[opti];
                int opti2 = opti + 1;
                if (this.mWindowManagerServiceExt.doDump(this, cmd, fd, pw, args, opti2)) {
                    return;
                }
                if (com.android.server.wm.ActivityTaskManagerService.DUMP_LASTANR_CMD.equals(cmd) || "l".equals(cmd)) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock2) {
                        try {
                            dumpLastANRLocked(pw);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("policy".equals(cmd) || "p".equals(cmd)) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock3 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock3) {
                        try {
                            dumpPolicyLocked(pw, args);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("animator".equals(cmd) || com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD.equals(cmd)) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock4 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock4) {
                        try {
                            dumpAnimatorLocked(pw, true);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("sessions".equals(cmd) || "s".equals(cmd)) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock5 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock5) {
                        try {
                            dumpSessionsLocked(pw);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("displays".equals(cmd) || "d".equals(cmd)) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock6 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock6) {
                        try {
                            this.mRoot.dumpDisplayContents(pw);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("tokens".equals(cmd) || "t".equals(cmd)) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock7 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock7) {
                        try {
                            dumpTokensLocked(pw, true);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("windows".equals(cmd) || "w".equals(cmd)) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock8 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock8) {
                        try {
                            dumpWindowsLocked(pw, true, null);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("accessibility".equals(cmd) || "a11y".equals(cmd)) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock9 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock9) {
                        try {
                            dumpAccessibilityLocked(pw);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("all".equals(cmd)) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock10 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock10) {
                        try {
                            dumpWindowsLocked(pw, true, null);
                        } finally {
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (com.android.server.wm.ActivityTaskManagerService.DUMP_CONTAINERS_CMD.equals(cmd)) {
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock11 = this.mGlobalLock;
                    boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock11) {
                        try {
                            this.mRoot.dumpChildrenNames(pw, "");
                            pw.println(" ");
                            this.mRoot.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda31
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    pw.println((com.android.server.wm.WindowState) obj);
                                }
                            }, true);
                        } finally {
                            resetPriorityAfterLockedSection();
                        }
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                if ("trace".equals(cmd)) {
                    dumpTraceStatus(pw);
                    return;
                }
                if ("logging".equals(cmd)) {
                    dumpLogStatus(pw);
                    return;
                }
                if ("refresh".equals(cmd)) {
                    dumpHighRefreshRateBlacklist(pw);
                    return;
                }
                if ("constants".equals(cmd)) {
                    this.mConstants.dump(pw);
                    return;
                }
                if ("splitscreen".equals(cmd)) {
                    this.mWindowManagerServiceExt.dump(pw, args);
                    return;
                }
                if ("package-config".equals(cmd)) {
                    this.mAtmService.dumpInstalledPackagesConfig(pw);
                    return;
                } else {
                    if (!this.mWindowManagerServiceExt.doDumpWindows(pw, cmd, args, opti2, dumpAll)) {
                        pw.println("Bad window command, or no windows match: " + cmd);
                        pw.println("Use -h for help.");
                        return;
                    }
                    return;
                }
            }
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock12 = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock12) {
                try {
                    pw.println();
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    dumpLastANRLocked(pw);
                    pw.println();
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    dumpPolicyLocked(pw, args);
                    pw.println();
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    dumpAnimatorLocked(pw, dumpAll);
                    pw.println();
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    dumpSessionsLocked(pw);
                    pw.println();
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    this.mRoot.dumpDisplayContents(pw);
                    pw.println();
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    dumpTokensLocked(pw, dumpAll);
                    pw.println();
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    dumpWindowsLocked(pw, dumpAll, null);
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    dumpTraceStatus(pw);
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    dumpLogStatus(pw);
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    dumpHighRefreshRateBlacklist(pw);
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    this.mAtmService.dumpInstalledPackagesConfig(pw);
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    this.mConstants.dump(pw);
                    if (dumpAll) {
                        pw.println("-------------------------------------------------------------------------------");
                    }
                    this.mSystemPerformanceHinter.dump(pw, "");
                    this.mTrustedPresentationListenerController.dump(pw);
                    this.mSensitiveContentPackages.dump(pw);
                    this.mScreenRecordingCallbackController.dump(pw);
                } finally {
                    resetPriorityAfterLockedSection();
                }
            }
            resetPriorityAfterLockedSection();
        }
    }

    @Override // com.android.server.Watchdog.Monitor
    public void monitor() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    com.android.server.wm.DisplayContent getDefaultDisplayContentLocked() {
        return this.mRoot.getDisplayContent(0);
    }

    public void onOverlayChanged() {
        this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onOverlayChanged$21();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onOverlayChanged$21() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAtmService.deferWindowLayout();
                this.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda37
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wm.DisplayContent) obj).getDisplayPolicy().onOverlayChanged();
                    }
                });
                this.mAtmService.continueWindowLayout();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public java.lang.Object getWindowManagerLock() {
        return this.mGlobalLock;
    }

    public int getDockedStackSide() {
        return 0;
    }

    void setForceDesktopModeOnExternalDisplays(boolean forceDesktopModeOnExternalDisplays) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mForceDesktopModeOnExternalDisplays = forceDesktopModeOnExternalDisplays;
                this.mRoot.updateDisplayImePolicyCache();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    void setIsPc(boolean isPc) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mIsPc = isPc;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    static int dipToPixel(int dip, android.util.DisplayMetrics displayMetrics) {
        return (int) android.util.TypedValue.applyDimension(1, dip, displayMetrics);
    }

    public void registerPinnedTaskListener(int displayId, android.view.IPinnedTaskListener listener) {
        if (!checkCallingPermission("android.permission.REGISTER_WINDOW_MANAGER_LISTENERS", "registerPinnedTaskListener()") || !this.mAtmService.mSupportsPictureInPicture) {
            return;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                displayContent.getPinnedTaskController().registerPinnedTaskListener(listener);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public void requestAppKeyboardShortcuts(com.android.internal.os.IResultReceiver receiver, int deviceId) {
        enforceRegisterWindowManagerListenersPermission("requestAppKeyboardShortcuts");
        com.android.server.wm.WindowState focusedWindow = getFocusedWindow();
        if (focusedWindow == null || focusedWindow.mClient == null) {
            notifyReceiverWithEmptyBundle(receiver);
            return;
        }
        try {
            focusedWindow.mClient.requestAppKeyboardShortcuts(receiver, deviceId);
        } catch (android.os.RemoteException e) {
            notifyReceiverWithEmptyBundle(receiver);
        }
    }

    public void requestImeKeyboardShortcuts(com.android.internal.os.IResultReceiver receiver, int deviceId) {
        enforceRegisterWindowManagerListenersPermission("requestImeKeyboardShortcuts");
        com.android.server.wm.WindowState imeWindow = this.mRoot.getCurrentInputMethodWindow();
        if (imeWindow == null || imeWindow.mClient == null) {
            notifyReceiverWithEmptyBundle(receiver);
            return;
        }
        try {
            imeWindow.mClient.requestAppKeyboardShortcuts(receiver, deviceId);
        } catch (android.os.RemoteException e) {
            notifyReceiverWithEmptyBundle(receiver);
        }
    }

    private void enforceRegisterWindowManagerListenersPermission(java.lang.String message) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.REGISTER_WINDOW_MANAGER_LISTENERS", message);
    }

    private static void notifyReceiverWithEmptyBundle(com.android.internal.os.IResultReceiver receiver) {
        try {
            receiver.send(0, android.os.Bundle.EMPTY);
        } catch (android.os.RemoteException e) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[4]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 1010635158502326025L, 0, "unable to call receiver for empty keyboard shortcuts", null);
            }
        }
    }

    public void getStableInsets(int displayId, android.graphics.Rect outInsets) throws android.os.RemoteException {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                getStableInsetsLocked(displayId, outInsets);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    void getStableInsetsLocked(int displayId, android.graphics.Rect outInsets) {
        outInsets.setEmpty();
        com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(displayId);
        if (dc != null) {
            android.view.DisplayInfo di = dc.getDisplayInfo();
            outInsets.set(dc.getDisplayPolicy().getDecorInsetsInfo(di.rotation, di.logicalWidth, di.logicalHeight).mConfigInsets);
        }
    }

    void updateTapExcludeRegion(android.view.IWindow client, android.graphics.Region region) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState callingWin = windowForClientLocked((com.android.server.wm.Session) null, client, false);
                if (callingWin == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(client);
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 1278715281433572858L, 0, "Bad requesting window %s", protoLogParam0);
                    }
                    resetPriorityAfterLockedSection();
                    return;
                }
                callingWin.updateTapExcludeRegion(region);
                resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void requestScrollCapture(int displayId, android.os.IBinder behindClient, int taskId, android.view.IScrollCaptureResponseListener listener) throws java.lang.Throwable {
        android.view.ScrollCaptureResponse.Builder responseBuilder;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock;
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "requestScrollCapture()")) {
            throw new java.lang.SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                responseBuilder = new android.view.ScrollCaptureResponse.Builder();
                windowManagerGlobalLock = this.mGlobalLock;
                boostPriorityForLockedSection();
                try {
                } catch (android.os.RemoteException e) {
                    e = e;
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(e);
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -8972916676375201577L, 0, "requestScrollCapture: caught exception dispatching callback: %s", protoLogParam0);
                    }
                }
            } catch (java.lang.Throwable th) {
                th = th;
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        } catch (android.os.RemoteException e2) {
            e = e2;
        } catch (java.lang.Throwable th2) {
            th = th2;
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(displayId);
                if (dc == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[4]) {
                        long protoLogParam02 = displayId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -6186782212018913664L, 1, "Invalid displayId for requestScrollCapture: %d", java.lang.Long.valueOf(protoLogParam02));
                    }
                    responseBuilder.setDescription(java.lang.String.format("bad displayId: %d", java.lang.Integer.valueOf(displayId)));
                    listener.onScrollCaptureResponse(responseBuilder.build());
                    resetPriorityAfterLockedSection();
                    android.os.Binder.restoreCallingIdentity(token);
                    return;
                }
                com.android.server.wm.WindowState topWindow = behindClient != null ? windowForClientLocked((com.android.server.wm.Session) null, behindClient, false) : null;
                try {
                    com.android.server.wm.WindowState targetWindow = dc.findScrollCaptureTargetWindow(topWindow, taskId);
                    if (targetWindow == null) {
                        responseBuilder.setDescription("findScrollCaptureTargetWindow returned null");
                        listener.onScrollCaptureResponse(responseBuilder.build());
                        resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(token);
                        return;
                    }
                    try {
                        targetWindow.mClient.requestScrollCapture(listener);
                    } catch (android.os.RemoteException e3) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                            java.lang.String protoLogParam03 = java.lang.String.valueOf(targetWindow.mClient.asBinder());
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 51378282333944649L, 0, "requestScrollCapture: caught exception dispatching to window.token=%s", protoLogParam03);
                        }
                        java.lang.String protoLogParam04 = targetWindow.getName();
                        responseBuilder.setWindowTitle(protoLogParam04);
                        responseBuilder.setPackageName(targetWindow.getOwningPackage());
                        responseBuilder.setDescription(java.lang.String.format("caught exception: %s", e3));
                        listener.onScrollCaptureResponse(responseBuilder.build());
                    }
                    resetPriorityAfterLockedSection();
                    android.os.Binder.restoreCallingIdentity(token);
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    public int getWindowingMode(int displayId) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "getWindowingMode()")) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        long protoLogParam0 = displayId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -1875125162673622728L, 1, "Attempted to get windowing mode of a display that does not exist: %d", java.lang.Long.valueOf(protoLogParam0));
                    }
                    resetPriorityAfterLockedSection();
                    return 0;
                }
                int windowingModeLocked = this.mDisplayWindowSettings.getWindowingModeLocked(displayContent);
                resetPriorityAfterLockedSection();
                return windowingModeLocked;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setWindowingMode(int displayId, int mode) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "setWindowingMode()")) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = getDisplayContentOrCreate(displayId, null);
                    if (displayContent == null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                            long protoLogParam0 = displayId;
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 3938331948687900219L, 1, "Attempted to set windowing mode to a display that does not exist: %d", java.lang.Long.valueOf(protoLogParam0));
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    int lastWindowingMode = displayContent.getWindowingMode();
                    this.mDisplayWindowSettings.setWindowingModeLocked(displayContent, mode);
                    displayContent.reconfigureDisplayLocked();
                    if (lastWindowingMode != displayContent.getWindowingMode()) {
                        displayContent.sendNewConfiguration();
                        displayContent.executeAppTransition();
                    }
                    resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public int getRemoveContentMode(int displayId) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "getRemoveContentMode()")) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        long protoLogParam0 = displayId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 4200292050699107329L, 1, "Attempted to get remove mode of a display that does not exist: %d", java.lang.Long.valueOf(protoLogParam0));
                    }
                    resetPriorityAfterLockedSection();
                    return 0;
                }
                int removeContentModeLocked = this.mDisplayWindowSettings.getRemoveContentModeLocked(displayContent);
                resetPriorityAfterLockedSection();
                return removeContentModeLocked;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setRemoveContentMode(int displayId, int mode) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "setRemoveContentMode()")) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = getDisplayContentOrCreate(displayId, null);
                    if (displayContent == null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                            long protoLogParam0 = displayId;
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -5574580669790275797L, 1, "Attempted to set remove mode to a display that does not exist: %d", java.lang.Long.valueOf(protoLogParam0));
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    this.mDisplayWindowSettings.setRemoveContentModeLocked(displayContent, mode);
                    displayContent.reconfigureDisplayLocked();
                    resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public boolean shouldShowWithInsecureKeyguard(int displayId) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "shouldShowWithInsecureKeyguard()")) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        long protoLogParam0 = displayId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 525945815055875796L, 1, "Attempted to get flag of a display that does not exist: %d", java.lang.Long.valueOf(protoLogParam0));
                    }
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean zShouldShowWithInsecureKeyguardLocked = this.mDisplayWindowSettings.shouldShowWithInsecureKeyguardLocked(displayContent);
                resetPriorityAfterLockedSection();
                return zShouldShowWithInsecureKeyguardLocked;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setShouldShowWithInsecureKeyguard(int displayId, boolean shouldShow) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "setShouldShowWithInsecureKeyguard()")) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = getDisplayContentOrCreate(displayId, null);
                    if (displayContent == null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                            long protoLogParam0 = displayId;
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 8186524992939307511L, 1, "Attempted to set flag to a display that does not exist: %d", java.lang.Long.valueOf(protoLogParam0));
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    this.mDisplayWindowSettings.setShouldShowWithInsecureKeyguardLocked(displayContent, shouldShow);
                    displayContent.reconfigureDisplayLocked();
                    resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public boolean shouldShowSystemDecors(int displayId) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "shouldShowSystemDecors()")) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        long protoLogParam0 = displayId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, -600035824255550632L, 1, "Attempted to get system decors flag of a display that does not exist: %d", java.lang.Long.valueOf(protoLogParam0));
                    }
                    resetPriorityAfterLockedSection();
                    return false;
                }
                boolean zSupportsSystemDecorations = displayContent.supportsSystemDecorations();
                resetPriorityAfterLockedSection();
                return zSupportsSystemDecorations;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void setShouldShowSystemDecors(int displayId, boolean shouldShow) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "setShouldShowSystemDecors()")) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = getDisplayContentOrCreate(displayId, null);
                    if (displayContent == null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                            long protoLogParam0 = displayId;
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 3056518663346732662L, 1, "Attempted to set system decors flag to a display that does not exist: %d", java.lang.Long.valueOf(protoLogParam0));
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    if (!displayContent.isTrusted()) {
                        throw new java.lang.SecurityException("Attempted to set system decors flag to an untrusted virtual display: " + displayId);
                    }
                    this.mDisplayWindowSettings.setShouldShowSystemDecorsLocked(displayContent, shouldShow);
                    displayContent.reconfigureDisplayLocked();
                    resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public int getDisplayImePolicy(int displayId) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "getDisplayImePolicy()")) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        java.util.Map<java.lang.Integer, java.lang.Integer> displayImePolicyCache = this.mDisplayImePolicyCache;
        if (!displayImePolicyCache.containsKey(java.lang.Integer.valueOf(displayId))) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                long protoLogParam0 = displayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 5177195624625618567L, 1, "Attempted to get IME policy of a display that does not exist: %d", java.lang.Long.valueOf(protoLogParam0));
                return 1;
            }
            return 1;
        }
        return displayImePolicyCache.get(java.lang.Integer.valueOf(displayId)).intValue();
    }

    public void setDisplayImePolicy(int displayId, int imePolicy) {
        if (!checkCallingPermission("android.permission.INTERNAL_SYSTEM_WINDOW", "setDisplayImePolicy()")) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = getDisplayContentOrCreate(displayId, null);
                    if (displayContent == null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                            long protoLogParam0 = displayId;
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 3932627933834459400L, 1, "Attempted to set IME policy to a display that does not exist: %d", java.lang.Long.valueOf(protoLogParam0));
                        }
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    if (!displayContent.isTrusted()) {
                        throw new java.lang.SecurityException("Attempted to set IME policy to an untrusted virtual display: " + displayId);
                    }
                    this.mDisplayWindowSettings.setDisplayImePolicy(displayContent, imePolicy);
                    displayContent.reconfigureDisplayLocked();
                    resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void registerShortcutKey(long shortcutCode, com.android.internal.policy.IShortcutService shortcutKeyReceiver) throws android.os.RemoteException {
        if (!checkCallingPermission("android.permission.REGISTER_WINDOW_MANAGER_LISTENERS", "registerShortcutKey")) {
            throw new java.lang.SecurityException("Requires REGISTER_WINDOW_MANAGER_LISTENERS permission");
        }
        this.mPolicy.registerShortcutKey(shortcutCode, shortcutKeyReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class LocalService extends com.android.server.wm.WindowManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal getAccessibilityController() {
            return com.android.server.wm.AccessibilityController.getAccessibilityControllerInternal(com.android.server.wm.WindowManagerService.this);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void clearSnapshotCache() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mTaskSnapshotController.clearSnapshotCache();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void requestTraversalFromDisplayManager() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.requestTraversal();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void onDisplayManagerReceivedDeviceState(final int deviceState) {
            com.android.server.wm.WindowManagerService.this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onDisplayManagerReceivedDeviceState$0(deviceState);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDisplayManagerReceivedDeviceState$0(int deviceState) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mRoot.onDisplayManagerReceivedDeviceState(deviceState);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setMagnificationSpec(int displayId, android.view.MagnificationSpec spec) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.WindowManagerService.this.mAccessibilityController.hasCallbacks()) {
                        com.android.server.wm.WindowManagerService.this.mAccessibilityController.setMagnificationSpec(displayId, spec);
                    } else {
                        throw new java.lang.IllegalStateException("Magnification callbacks not set!");
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setFullscreenMagnificationActivated(int displayId, boolean activated) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.WindowManagerService.this.mAccessibilityController.hasCallbacks()) {
                        com.android.server.wm.WindowManagerService.this.mAccessibilityController.setFullscreenMagnificationActivated(displayId, activated);
                    } else {
                        throw new java.lang.IllegalStateException("Magnification callbacks not set!");
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void getMagnificationRegion(int displayId, android.graphics.Region magnificationRegion) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.WindowManagerService.this.mAccessibilityController.hasCallbacks()) {
                        com.android.server.wm.WindowManagerService.this.mAccessibilityController.getMagnificationRegion(displayId, magnificationRegion);
                    } else {
                        throw new java.lang.IllegalStateException("Magnification callbacks not set!");
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean setMagnificationCallbacks(int displayId, com.android.server.wm.WindowManagerInternal.MagnificationCallbacks callbacks) {
            boolean magnificationCallbacks;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    magnificationCallbacks = com.android.server.wm.WindowManagerService.this.mAccessibilityController.setMagnificationCallbacks(displayId, callbacks);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return magnificationCallbacks;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setWindowsForAccessibilityCallback(int displayId, com.android.server.wm.WindowManagerInternal.WindowsForAccessibilityCallback callback) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mAccessibilityController.setWindowsForAccessibilityCallback(displayId, callback);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setInputFilter(android.view.IInputFilter filter) {
            com.android.server.wm.WindowManagerService.this.mInputManager.setInputFilter(filter);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public android.os.IBinder getFocusedWindowToken() {
            android.os.IBinder focusedWindowToken;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    focusedWindowToken = com.android.server.wm.WindowManagerService.this.mAccessibilityController.getFocusedWindowToken();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return focusedWindowToken;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public android.os.IBinder getFocusedWindowTokenFromWindowStates() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState windowState = com.android.server.wm.WindowManagerService.this.getFocusedWindowLocked();
                    if (windowState == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    android.os.IBinder iBinderAsBinder = windowState.mClient.asBinder();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return iBinderAsBinder;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void moveDisplayToTopIfAllowed(int displayId) {
            com.android.server.wm.WindowManagerService.this.moveDisplayToTopIfAllowed(displayId);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void requestWindowFocus(android.os.IBinder windowToken) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.InputTarget inputTarget = com.android.server.wm.WindowManagerService.this.getInputTargetFromWindowTokenLocked(windowToken);
                    com.android.server.wm.WindowManagerService.this.onPointerDownOutsideFocusLocked(inputTarget);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isKeyguardLocked() {
            return com.android.server.wm.WindowManagerService.this.isKeyguardLocked();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isKeyguardShowingAndNotOccluded() {
            return com.android.server.wm.WindowManagerService.this.isKeyguardShowingAndNotOccluded();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isKeyguardSecure(int userId) {
            return com.android.server.wm.WindowManagerService.this.mPolicy.isKeyguardSecure(userId);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void showGlobalActions() {
            com.android.server.wm.WindowManagerService.this.showGlobalActions();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void getWindowFrame(android.os.IBinder token, android.graphics.Rect outBounds) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState windowState = com.android.server.wm.WindowManagerService.this.mWindowMap.get(token);
                    if (windowState != null) {
                        outBounds.set(windowState.getFrame());
                    } else {
                        outBounds.setEmpty();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public android.util.Pair<android.graphics.Matrix, android.view.MagnificationSpec> getWindowTransformationMatrixAndMagnificationSpec(android.os.IBinder token) {
            return com.android.server.wm.WindowManagerService.this.mAccessibilityController.getWindowTransformationMatrixAndMagnificationSpec(token);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void waitForAllWindowsDrawn(android.os.Message message, long timeout, int displayId) {
            com.android.server.wm.WindowContainer<?> container;
            java.util.Objects.requireNonNull(message.getTarget());
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    container = displayId == -1 ? com.android.server.wm.WindowManagerService.this.mRoot : com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(displayId);
                } finally {
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            if (container == null) {
                message.sendToTarget();
                return;
            }
            boolean allWindowsDrawn = false;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock2) {
                try {
                    com.android.server.wm.DisplayContent dc = displayId == -1 ? com.android.server.wm.WindowManagerService.this.mRoot.getDefaultDisplay() : com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(displayId);
                    if (dc == null || dc.mDisplayUpdater == null || !dc.mDisplayUpdater.waitForTransition(message)) {
                        container.waitForAllWindowsDrawn();
                        com.android.server.wm.WindowManagerService.this.mWindowPlacerLocked.requestTraversal();
                        com.android.server.wm.WindowManagerService.this.mH.removeMessages(24, container);
                        if (container.mWaitingForDrawn.isEmpty()) {
                            com.android.server.wm.WindowManagerService.this.mWindowManagerServiceExt.clearSkipWaitingForDrawn();
                            allWindowsDrawn = true;
                        } else {
                            if (android.os.Trace.isTagEnabled(32L)) {
                                for (int i = 0; i < container.mWaitingForDrawn.size(); i++) {
                                    com.android.server.wm.WindowManagerService.this.traceStartWaitingForWindowDrawn(container.mWaitingForDrawn.get(i));
                                }
                            }
                            com.android.server.wm.WindowManagerService.this.mWaitingForDrawnCallbacks.put(container, message);
                            com.android.server.wm.WindowManagerService.this.mH.sendNewMessageDelayed(24, container, timeout);
                            com.android.server.wm.WindowManagerService.this.checkDrawnWindowsLocked();
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        android.util.Slog.d(com.android.server.wm.WindowManagerService.TAG, "waitForAllWindowsDrawn displayId=" + displayId + "," + allWindowsDrawn + ",message=" + message);
                        if (allWindowsDrawn) {
                            message.sendToTarget();
                        }
                    }
                } finally {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setForcedDisplaySize(int displayId, int width, int height) {
            com.android.server.wm.WindowManagerService.this.setForcedDisplaySize(displayId, width, height);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void clearForcedDisplaySize(int displayId) {
            com.android.server.wm.WindowManagerService.this.clearForcedDisplaySize(displayId);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void addWindowToken(android.os.IBinder token, int type, int displayId, android.os.Bundle options) throws java.lang.Throwable {
            com.android.server.wm.WindowManagerService.this.addWindowToken(token, type, displayId, options);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void removeWindowToken(android.os.IBinder binder, boolean removeWindows, boolean animateExit, int displayId) {
            com.android.server.wm.WindowManagerService.this.removeWindowToken(binder, removeWindows, animateExit, displayId);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void moveWindowTokenToDisplay(android.os.IBinder binder, int displayId) {
            com.android.server.wm.WindowManagerService.this.moveWindowTokenToDisplay(binder, displayId);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void registerAppTransitionListener(com.android.server.wm.WindowManagerInternal.AppTransitionListener listener) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.getDefaultDisplayContentLocked().mAppTransition.registerListenerLocked(listener);
                    com.android.server.wm.WindowManagerService.this.mAtmService.getTransitionController().registerLegacyListener(listener);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void registerTaskSystemBarsListener(com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener listener) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mTaskSystemBarsListenerController.registerListener(listener);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void unregisterTaskSystemBarsListener(com.android.server.wm.WindowManagerInternal.TaskSystemBarsListener listener) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mTaskSystemBarsListenerController.unregisterListener(listener);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void reportPasswordChanged(int userId) {
            com.android.server.wm.WindowManagerService.this.mKeyguardDisableHandler.updateKeyguardEnabled(userId);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public int getInputMethodWindowVisibleHeight(int displayId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(displayId);
                    if (dc == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return 0;
                    }
                    int inputMethodWindowVisibleHeight = dc.getInputMethodWindowVisibleHeight();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return inputMethodWindowVisibleHeight;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setDismissImeOnBackKeyPressed(boolean dismissImeOnBackKeyPressed) {
            com.android.server.wm.WindowManagerService.this.mPolicy.setDismissImeOnBackKeyPressed(dismissImeOnBackKeyPressed);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void updateInputMethodTargetWindow(android.os.IBinder imeToken, android.os.IBinder imeTargetWindowToken) {
            com.android.server.wm.WindowState imeWindowState;
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                android.util.Slog.w(com.android.server.wm.WindowManagerService.TAG, "updateInputMethodTargetWindow: imeToken=" + imeToken + " imeTargetWindowToken=" + imeTargetWindowToken);
            }
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.InputTarget imeTarget = com.android.server.wm.WindowManagerService.this.getInputTargetFromWindowTokenLocked(imeTargetWindowToken);
                    if (imeTarget != null) {
                        imeTarget.getDisplayContent().updateImeInputAndControlTarget(imeTarget);
                        if (android.view.inputmethod.Flags.refactorInsetsController() && (imeWindowState = imeTarget.getWindowState()) != null) {
                            com.android.server.wm.InsetsControlTarget fallback = imeTarget.getDisplayContent().getImeHostOrFallback(imeWindowState);
                            if (imeWindowState != fallback) {
                                final int currentDisplayId = imeTarget.getDisplayContent().getDisplayId();
                                com.android.server.wm.WindowManagerService.this.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda7
                                    @Override // java.util.function.Consumer
                                    public final void accept(java.lang.Object obj) {
                                        com.android.server.wm.WindowManagerService.LocalService.lambda$updateInputMethodTargetWindow$1(currentDisplayId, (com.android.server.wm.DisplayContent) obj);
                                    }
                                });
                            }
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        static /* synthetic */ void lambda$updateInputMethodTargetWindow$1(int currentDisplayId, com.android.server.wm.DisplayContent display) {
            if (display.getDisplayId() != currentDisplayId) {
                display.setImeInputTarget(null);
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isHardKeyboardAvailable() {
            boolean z;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    z = com.android.server.wm.WindowManagerService.this.mHardKeyboardAvailable;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return z;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setOnHardKeyboardStatusChangeListener(com.android.server.wm.WindowManagerInternal.OnHardKeyboardStatusChangeListener listener) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mHardKeyboardStatusChangeListener = listener;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setOnImeRequestedChangedListener(com.android.server.wm.WindowManagerInternal.OnImeRequestedChangedListener listener) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mOnImeRequestedChangedListener = listener;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void computeWindowsForAccessibility(int displayId) {
            com.android.server.wm.WindowManagerService.this.mAccessibilityController.performComputeChangedWindowsNot(displayId, true);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setVr2dDisplayId(int vr2dDisplayId) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DISPLAY) {
                android.util.Slog.d(com.android.server.wm.WindowManagerService.TAG, "setVr2dDisplayId called for: " + vr2dDisplayId);
            }
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mVr2dDisplayId = vr2dDisplayId;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void registerDragDropControllerCallback(com.android.server.wm.WindowManagerInternal.IDragDropCallback callback) {
            com.android.server.wm.WindowManagerService.this.mDragDropController.registerCallback(callback);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void lockNow() {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            android.util.Slog.d(com.android.server.wm.WindowManagerService.TAG, "lockNow called by uid: " + callingUid + " pid: " + callingPid);
            com.android.server.wm.WindowManagerService.this.lockNow(null);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public int getWindowOwnerUserId(android.os.IBinder token) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState window = com.android.server.wm.WindowManagerService.this.mWindowMap.get(token);
                    if (window == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return -10000;
                    }
                    int i = window.mShowUserId;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return i;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setWallpaperShowWhenLocked(android.os.IBinder binder, boolean showWhenLocked) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowToken token = com.android.server.wm.WindowManagerService.this.mRoot.getWindowToken(binder);
                    if (token != null && token.asWallpaperToken() != null) {
                        token.asWallpaperToken().setShowWhenLocked(showWhenLocked);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(binder);
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 5770211341769258866L, 0, "setWallpaperShowWhenLocked: non-existent wallpaper token: %s", protoLogParam0);
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setWallpaperCropHints(android.os.IBinder binder, android.util.SparseArray<android.graphics.Rect> cropHints) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowToken token = com.android.server.wm.WindowManagerService.this.mRoot.getWindowToken(binder);
                    if (token != null && token.asWallpaperToken() != null) {
                        token.asWallpaperToken().setCropHints(cropHints);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(binder);
                        com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 698926505694016512L, 0, "setWallpaperCropHints: non-existent wallpaper token: %s", protoLogParam0);
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setWallpaperCropUtils(com.android.server.wallpaper.WallpaperCropper.WallpaperCropUtils wallpaperCropUtils) {
            com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(0).mWallpaperController.setWallpaperCropUtils(wallpaperCropUtils);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isUidFocused(int uid) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    for (int i = com.android.server.wm.WindowManagerService.this.mRoot.getChildCount() - 1; i >= 0; i--) {
                        com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) com.android.server.wm.WindowManagerService.this.mRoot.getChildAt(i);
                        if (displayContent.mCurrentFocus != null && uid == displayContent.mCurrentFocus.getOwningUid()) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return true;
                        }
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public int hasInputMethodClientFocus(android.os.IBinder windowToken, int uid, int pid, int displayId) {
            if (displayId == -1) {
                return -3;
            }
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = com.android.server.wm.WindowManagerService.this.mRoot.getTopFocusedDisplayContent();
                    com.android.server.wm.InputTarget target = com.android.server.wm.WindowManagerService.this.getInputTargetFromWindowTokenLocked(windowToken);
                    if (target == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return -1;
                    }
                    int tokenDisplayId = target.getDisplayContent().getDisplayId();
                    if (tokenDisplayId != displayId) {
                        android.util.Slog.e(com.android.server.wm.WindowManagerService.TAG, "isInputMethodClientFocus: display ID mismatch. from client: " + displayId + " from window: " + tokenDisplayId);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return -2;
                    }
                    if (displayContent != null && displayContent.getDisplayId() == displayId && displayContent.hasAccess(uid)) {
                        if (target.isInputMethodClientFocus(uid, pid)) {
                            if (com.android.server.wm.WindowManagerService.this.mWindowManagerServiceExt.isIMETargetWindowHasFocus(target)) {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return 0;
                            }
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return -1;
                        }
                        com.android.server.wm.WindowState currentFocus = displayContent.mCurrentFocus;
                        if (currentFocus == null || currentFocus.mSession.mUid != uid || currentFocus.mSession.mPid != pid) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return -1;
                        }
                        int i = currentFocus.canBeImeTarget() ? 0 : -1;
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return i;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return -3;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void showImePostLayout(android.os.IBinder imeTargetWindowToken, android.view.inputmethod.ImeTracker.Token statsToken) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.InputTarget imeTarget = com.android.server.wm.WindowManagerService.this.getInputTargetFromWindowTokenLocked(imeTargetWindowToken);
                    if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE.isLogToLogcat()) {
                        android.util.Slog.i(com.android.server.wm.WindowManagerService.TAG, "showImePostLayout:" + imeTarget + " Caller=" + android.os.Debug.getCallers(1));
                    }
                    if (imeTarget == null) {
                        android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 20);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 20);
                    com.android.server.wm.InsetsControlTarget controlTarget = imeTarget.getImeControlTarget();
                    com.android.server.wm.InputTarget imeTarget2 = controlTarget.getWindow();
                    com.android.server.wm.DisplayContent dc = imeTarget2 != null ? imeTarget2.getDisplayContent() : com.android.server.wm.WindowManagerService.this.getDefaultDisplayContentLocked();
                    dc.getInsetsStateController().getImeSourceProvider().scheduleShowImePostLayout(controlTarget, statsToken);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void hideIme(android.os.IBinder imeTargetWindowToken, int displayId, android.view.inputmethod.ImeTracker.Token statsToken) throws java.lang.Throwable {
            android.os.Trace.traceBegin(32L, "WMS.hideIme");
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
                try {
                    com.android.server.wm.WindowState imeTarget = com.android.server.wm.WindowManagerService.this.mWindowMap.get(imeTargetWindowToken);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[0]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(imeTarget);
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, -7428028317216329062L, 0, null, protoLogParam0);
                    }
                    com.android.server.wm.DisplayContent dc = com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(displayId);
                    if (imeTarget != null) {
                        com.android.server.wm.WindowState imeTarget2 = imeTarget.getImeControlTarget().getWindow();
                        if (imeTarget2 != null) {
                            dc = imeTarget2.getDisplayContent();
                        }
                        dc.getInsetsStateController().getImeSourceProvider().abortShowImePostLayout();
                    }
                    if (dc != null && dc.mRemoteInsetsControlTarget != null && dc.getImeTarget(2) != dc.mRemoteInsetsControlTarget && (dc.mRemoteInsetsControlTarget.getRequestedVisibleTypes() & android.view.WindowInsets.Type.ime()) != 0) {
                        android.util.Slog.d(com.android.server.wm.WindowManagerService.TAG, "update mRemoteInsetsControlTarget visible force when hideIme");
                        dc.mRemoteInsetsControlTarget.setRequestedVisibleTypes(android.view.WindowInsets.Type.defaultVisible());
                    }
                    if (dc == null || dc.getImeTarget(2) == null) {
                        android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 20);
                    } else {
                        android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 20);
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[0]) {
                            java.lang.String protoLogParam02 = java.lang.String.valueOf(dc.getImeTarget(2));
                            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, 1006302987953651112L, 0, null, protoLogParam02);
                        }
                        dc.getImeTarget(2).hideInsets(android.view.WindowInsets.Type.ime(), true, statsToken);
                    }
                    if (dc != null) {
                        dc.getInsetsStateController().getImeSourceProvider().setImeShowing(false);
                        dc.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                ((com.android.server.wm.ActivityRecord) obj).mImeInsetsFrozenUntilStartInput = false;
                            }
                        });
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    android.os.Trace.traceEnd(32L);
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isUidAllowedOnDisplay(int displayId, int uid) {
            boolean z = true;
            if (displayId == 0) {
                return true;
            }
            if (displayId == -1) {
                return false;
            }
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(displayId);
                    if (displayContent == null || !displayContent.hasAccess(uid)) {
                        z = false;
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return z;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public int getDisplayIdForWindow(android.os.IBinder windowToken) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState window = com.android.server.wm.WindowManagerService.this.mWindowMap.get(windowToken);
                    if (window == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return -1;
                    }
                    int displayId = window.getDisplayContent().getDisplayId();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return displayId;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public int getTopFocusedDisplayId() {
            int displayId;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    displayId = com.android.server.wm.WindowManagerService.this.mRoot.getTopFocusedDisplayContent().getDisplayId();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return displayId;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public android.content.Context getTopFocusedDisplayUiContext() {
            android.content.Context displayUiContext;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    displayUiContext = com.android.server.wm.WindowManagerService.this.mRoot.getTopFocusedDisplayContent().getDisplayUiContext();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return displayUiContext;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setHomeSupportedOnDisplay(java.lang.String displayUniqueId, int displayType, boolean supported) {
            long origId = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        com.android.server.wm.WindowManagerService.this.mDisplayWindowSettings.setHomeSupportedOnDisplayLocked(displayUniqueId, displayType, supported);
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } finally {
                android.os.Binder.restoreCallingIdentity(origId);
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isHomeSupportedOnDisplay(int displayId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent displayContent = com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(displayId);
                    if (displayContent == null) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[3]) {
                            long protoLogParam0 = displayId;
                            com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 5213970642134448962L, 1, "Attempted to get home support flag of a display that does not exist: %d", java.lang.Long.valueOf(protoLogParam0));
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    boolean zIsHomeSupported = displayContent.isHomeSupported();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return zIsHomeSupported;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void clearDisplaySettings(java.lang.String displayUniqueId, int displayType) {
            long origId = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        com.android.server.wm.WindowManagerService.this.mDisplayWindowSettings.clearDisplaySettings(displayUniqueId, displayType);
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } finally {
                android.os.Binder.restoreCallingIdentity(origId);
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public int getDisplayImePolicy(int displayId) {
            return com.android.server.wm.WindowManagerService.this.getDisplayImePolicy(displayId);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void addRefreshRateRangeForPackage(final java.lang.String packageName, final float minRefreshRate, final float maxRefreshRate) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda3
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.wm.DisplayContent) obj).getDisplayPolicy().getRefreshRatePolicy().addRefreshRateRangeForPackage(packageName, minRefreshRate, maxRefreshRate);
                        }
                    });
                    com.android.server.wm.WindowManagerService.this.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda4
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.wm.DisplayContent) obj).getWrapper().getExtImpl().addRefreshRateRangeForPackage(packageName, minRefreshRate, maxRefreshRate);
                        }
                    });
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void removeRefreshRateRangeForPackage(final java.lang.String packageName) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda5
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.wm.DisplayContent) obj).getDisplayPolicy().getRefreshRatePolicy().removeRefreshRateRangeForPackage(packageName);
                        }
                    });
                    com.android.server.wm.WindowManagerService.this.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda6
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.wm.DisplayContent) obj).getWrapper().getExtImpl().removeRefreshRateRangeForPackage(packageName);
                        }
                    });
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isTouchOrFaketouchDevice() {
            boolean z;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.WindowManagerService.this.mIsTouchDevice && !com.android.server.wm.WindowManagerService.this.mIsFakeTouchDevice) {
                        throw new java.lang.IllegalStateException("touchscreen supported device must report faketouch.");
                    }
                    z = com.android.server.wm.WindowManagerService.this.mIsFakeTouchDevice;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return z;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public com.android.internal.policy.KeyInterceptionInfo getKeyInterceptionInfoFromToken(android.os.IBinder inputToken) {
            return com.android.server.wm.WindowManagerService.this.mKeyInterceptionInfoForToken.get(inputToken);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setAccessibilityIdToSurfaceMetadata(android.os.IBinder windowToken, int accessibilityWindowId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState state = com.android.server.wm.WindowManagerService.this.mWindowMap.get(windowToken);
                    if (state == null) {
                        android.util.Slog.w(com.android.server.wm.WindowManagerService.TAG, "Cannot find window which accessibility connection is added to");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    } else {
                        com.android.server.wm.WindowManagerService.this.mTransaction.setMetadata(state.mSurfaceControl, 5, accessibilityWindowId).apply();
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public java.lang.String getWindowName(android.os.IBinder binder) {
            java.lang.String name;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState w = com.android.server.wm.WindowManagerService.this.mWindowMap.get(binder);
                    name = w != null ? w.getName() : null;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return name;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public com.android.server.wm.WindowManagerInternal.ImeTargetInfo onToggleImeRequested(boolean show, android.os.IBinder focusedToken, android.os.IBinder requestToken, int displayId) throws java.lang.Throwable {
            java.lang.String imeSurfaceParentName;
            java.lang.String imeLayerTargetName;
            java.lang.String imeControlTargetName;
            java.lang.String imeControlTargetName2;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                    } catch (java.lang.Throwable th) {
                        th = th;
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                    try {
                        com.android.server.wm.WindowState focusedWin = com.android.server.wm.WindowManagerService.this.mWindowMap.get(focusedToken);
                        java.lang.String focusedWindowName = focusedWin != null ? focusedWin.getName() : "null";
                        try {
                            com.android.server.wm.WindowState requestWin = com.android.server.wm.WindowManagerService.this.mWindowMap.get(requestToken);
                            java.lang.String requestWindowName = requestWin != null ? requestWin.getName() : "null";
                            com.android.server.wm.DisplayContent dc = com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(displayId);
                            if (dc != null) {
                                com.android.server.wm.InsetsControlTarget controlTarget = dc.getImeTarget(2);
                                if (controlTarget != null) {
                                    com.android.server.wm.WindowState w = com.android.server.wm.InsetsControlTarget.asWindowOrNull(controlTarget);
                                    imeControlTargetName2 = w != null ? w.getName() : controlTarget.toString();
                                } else {
                                    imeControlTargetName2 = "null";
                                }
                                com.android.server.wm.InsetsControlTarget target = dc.getImeTarget(0);
                                imeLayerTargetName = target != null ? target.getWindow().getName() : "null";
                                android.view.SurfaceControl imeParent = dc.mInputMethodSurfaceParent;
                                imeSurfaceParentName = imeParent != null ? imeParent.toString() : "null";
                                if (show) {
                                    dc.onShowImeRequested();
                                }
                                imeControlTargetName = imeControlTargetName2;
                            } else {
                                imeSurfaceParentName = "no-display";
                                imeLayerTargetName = "no-display";
                                imeControlTargetName = "no-display";
                            }
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return new com.android.server.wm.WindowManagerInternal.ImeTargetInfo(focusedWindowName, requestWindowName, imeControlTargetName, imeLayerTargetName, imeSurfaceParentName);
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th4) {
                    th = th4;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean shouldRestoreImeVisibility(android.os.IBinder imeTargetWindowToken) {
            return com.android.server.wm.WindowManagerService.this.shouldRestoreImeVisibility(imeTargetWindowToken);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void addTrustedTaskOverlay(int taskId, android.view.SurfaceControlViewHost.SurfacePackage overlay) {
            if (overlay == null) {
                throw new java.lang.IllegalArgumentException("Invalid overlay passed in for task=" + taskId);
            }
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (overlay.getSurfaceControl() == null || !overlay.getSurfaceControl().isValid()) {
                        throw new java.lang.IllegalArgumentException("Invalid overlay surfacecontrol passed in for task=" + taskId);
                    }
                    com.android.server.wm.Task task = com.android.server.wm.WindowManagerService.this.mRoot.getRootTask(taskId);
                    if (task == null) {
                        throw new java.lang.IllegalArgumentException("no task with taskId" + taskId);
                    }
                    task.addTrustedOverlay(overlay, task.getTopVisibleAppMainWindow());
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void removeTrustedTaskOverlay(int taskId, android.view.SurfaceControlViewHost.SurfacePackage overlay) {
            if (overlay == null) {
                throw new java.lang.IllegalArgumentException("Invalid overlay passed in for task=" + taskId);
            }
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (overlay.getSurfaceControl() == null || !overlay.getSurfaceControl().isValid()) {
                        throw new java.lang.IllegalArgumentException("Invalid overlay surfacecontrol passed in for task=" + taskId);
                    }
                    com.android.server.wm.Task task = com.android.server.wm.WindowManagerService.this.mRoot.getRootTask(taskId);
                    if (task == null) {
                        throw new java.lang.IllegalArgumentException("no task with taskId" + taskId);
                    }
                    task.removeTrustedOverlay(overlay);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public android.view.SurfaceControl getHandwritingSurfaceForDisplay(int displayId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(displayId);
                    if (dc == null) {
                        android.util.Slog.e(com.android.server.wm.WindowManagerService.TAG, "Failed to create a handwriting surface on display: " + displayId + " - DisplayContent not found.");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    android.view.SurfaceControl inputOverlay = dc.getInputOverlayLayer();
                    if (inputOverlay == null) {
                        android.util.Slog.e(com.android.server.wm.WindowManagerService.TAG, "Failed to create a gesture monitor on display: " + displayId + " - Input overlay layer is not initialized.");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return null;
                    }
                    android.view.SurfaceControl surfaceControlBuild = com.android.server.wm.WindowManagerService.this.makeSurfaceBuilder(dc.getSession()).setContainerLayer().setName("IME Handwriting Surface").setCallsite("getHandwritingSurfaceForDisplay").setParent(inputOverlay).build();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return surfaceControlBuild;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean isPointInsideWindow(android.os.IBinder windowToken, int displayId, float displayX, float displayY) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState w = com.android.server.wm.WindowManagerService.this.mWindowMap.get(windowToken);
                    if (w != null && w.getDisplayId() == displayId) {
                        if (w.getWindowingMode() == 100) {
                            boolean zIsFloatingWindow = com.android.server.wm.WindowManagerService.this.mWindowManagerServiceExt.isFloatingWindow(displayX, displayY);
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return zIsFloatingWindow;
                        }
                        if (w.getTask() != null && w.getTask().getWrapper().getExtImpl().getLaunchScenario() == 2) {
                            boolean zIsClickAtPocketStudioArea = com.android.server.wm.WindowManagerService.this.mWindowManagerServiceExt.isClickAtPocketStudioArea(displayId, (int) displayX, (int) displayY);
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return zIsClickAtPocketStudioArea;
                        }
                        boolean zContains = w.getBounds().contains((int) displayX, (int) displayY);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return zContains;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean setContentRecordingSession(android.view.ContentRecordingSession incomingSession) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                if (incomingSession != null) {
                    try {
                        if (incomingSession.getContentToRecord() == 1) {
                            com.android.server.wm.WindowManagerService.WindowContainerInfo wci = com.android.server.wm.WindowManagerService.this.getTaskWindowContainerInfoForRecordingSession(incomingSession);
                            if (wci == null) {
                                android.util.Slog.w(com.android.server.wm.WindowManagerService.TAG, "Handling a new recording session; unable to find the WindowContainerToken");
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return false;
                            }
                            incomingSession.setTokenToRecord(wci.getToken().asBinder());
                            incomingSession.setTargetUid(wci.getUid());
                            com.android.server.wm.WindowManagerService.this.mContentRecordingController.setContentRecordingSessionLocked(incomingSession, com.android.server.wm.WindowManagerService.this);
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return true;
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.this.mContentRecordingController.setContentRecordingSessionLocked(incomingSession, com.android.server.wm.WindowManagerService.this);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return true;
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public android.view.SurfaceControl getA11yOverlayLayer(int displayId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(displayId);
                    if (dc != null) {
                        android.view.SurfaceControl a11yOverlayLayer = dc.getA11yOverlayLayer();
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return a11yOverlayLayer;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean keepSimultaneousDisplay() {
            com.android.server.wm.WindowState win;
            com.android.server.wm.DisplayContent secondaryDisplayContent = com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(1);
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    win = secondaryDisplayContent.getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda2
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.wm.WindowManagerService.LocalService.lambda$keepSimultaneousDisplay$7((com.android.server.wm.WindowState) obj);
                        }
                    });
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            com.android.server.wm.ActivityRecord topActivityRecord = secondaryDisplayContent.topRunningActivity();
            if (topActivityRecord != null) {
                android.util.Slog.d(com.android.server.wm.WindowManagerService.TAG, "win : " + win + " topActivityRecord " + topActivityRecord + "  nowVisible : " + topActivityRecord.nowVisible + " TurnScreenOnFlag = " + topActivityRecord.getTurnScreenOnFlag());
            }
            return win != null || (topActivityRecord != null && topActivityRecord.nowVisible && topActivityRecord.getTurnScreenOnFlag());
        }

        static /* synthetic */ boolean lambda$keepSimultaneousDisplay$7(com.android.server.wm.WindowState w) {
            return w.isVisible() && w.getDisplayId() == 1 && (w.mAttrs.flags & 2097152) != 0;
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void captureDisplay(int displayId, android.window.ScreenCapture.CaptureArgs captureArgs, android.window.ScreenCapture.ScreenCaptureListener listener) {
            com.android.server.wm.WindowManagerService.this.captureDisplay(displayId, captureArgs, listener);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean hasNavigationBar(int displayId) {
            return com.android.server.wm.WindowManagerService.this.hasNavigationBar(displayId);
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setInputMethodTargetChangeListener(com.android.server.wm.ImeTargetChangeListener listener) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mImeTargetChangeListener = listener;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setOrientationRequestPolicy(boolean respected, int[] fromOrientations, int[] toOrientations) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.setOrientationRequestPolicy(respected, fromOrientations, toOrientations);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public android.os.IBinder getTargetWindowTokenFromInputToken(android.os.IBinder inputToken) {
            com.android.server.wm.InputTarget inputTarget = com.android.server.wm.WindowManagerService.this.getInputTargetFromToken(inputToken);
            if (inputTarget == null) {
                return null;
            }
            return inputTarget.getWindowToken();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void setBlockScreenCaptureForAppsSessionId(long sessionId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveContentMetricsBugfix() && com.android.server.wm.WindowManagerService.this.mSensitiveContentProtectionSessionId != sessionId) {
                        com.android.server.wm.WindowManagerService.this.mSensitiveContentProtectionSessionId = sessionId;
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void addBlockScreenCaptureForApps(android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> packageInfos) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    boolean modified = com.android.server.wm.WindowManagerService.this.mSensitiveContentPackages.addBlockScreenCaptureForApps(packageInfos);
                    if (modified) {
                        com.android.server.wm.WindowManagerService.this.refreshScreenCaptureDisabled();
                        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveContentImprovements()) {
                            com.android.server.wm.WindowManagerService.this.mRoot.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$LocalService$$ExternalSyntheticLambda8
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    this.f$0.lambda$addBlockScreenCaptureForApps$8((com.android.server.wm.WindowState) obj);
                                }
                            }, true);
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addBlockScreenCaptureForApps$8(com.android.server.wm.WindowState w) {
            if (w.isVisible()) {
                com.android.server.wm.WindowManagerService.this.showToastIfBlockingScreenCapture(w);
            } else if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveContentRecentsScreenshotBugfix() && shouldInvalidateSnapshot(w)) {
                com.android.server.wm.Task task = w.getTask();
                com.android.server.wm.WindowManagerService.this.mTaskSnapshotController.removeAndDeleteSnapshot(task.mTaskId, task.mUserId);
                task.onSnapshotInvalidated();
            }
        }

        private boolean shouldInvalidateSnapshot(com.android.server.wm.WindowState w) {
            return w.getTask() != null && com.android.server.wm.WindowManagerService.this.mSensitiveContentPackages.shouldBlockScreenCaptureForApp(w.getOwningPackage(), w.getOwningUid(), w.getWindowToken());
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void removeBlockScreenCaptureForApps(android.util.ArraySet<com.android.server.wm.SensitiveContentPackages.PackageInfo> packageInfos) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    boolean modified = com.android.server.wm.WindowManagerService.this.mSensitiveContentPackages.removeBlockScreenCaptureForApps(packageInfos);
                    if (modified) {
                        com.android.server.wm.WindowManagerService.this.refreshScreenCaptureDisabled();
                    }
                    if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveContentImprovements()) {
                        for (int i = 0; i < packageInfos.size(); i++) {
                            int uid = packageInfos.valueAt(i).getUid();
                            if (com.android.server.wm.WindowManagerService.this.mCaptureBlockedToastShownUids.contains(uid)) {
                                com.android.server.wm.WindowManagerService.this.mCaptureBlockedToastShownUids.remove(com.android.server.wm.WindowManagerService.this.mCaptureBlockedToastShownUids.indexOf(uid));
                            }
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void clearBlockedApps() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    boolean modified = com.android.server.wm.WindowManagerService.this.mSensitiveContentPackages.clearBlockedApps();
                    if (modified) {
                        com.android.server.wm.WindowManagerService.this.refreshScreenCaptureDisabled();
                    }
                    if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveContentImprovements()) {
                        com.android.server.wm.WindowManagerService.this.mCaptureBlockedToastShownUids.clear();
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void registerOnWindowRemovedListener(com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener listener) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mOnWindowRemovedListeners.add(listener);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public void unregisterOnWindowRemovedListener(com.android.server.wm.WindowManagerInternal.OnWindowRemovedListener listener) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowManagerService.this.mOnWindowRemovedListeners.remove(listener);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public boolean moveFocusToAdjacentEmbeddedActivityIfNeeded() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState focusedWindow = com.android.server.wm.WindowManagerService.this.getFocusedWindow();
                    if (focusedWindow == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    boolean moveFocusSuccessfully = com.android.server.wm.WindowManagerService.this.moveFocusToAdjacentEmbeddedWindow(focusedWindow);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    if (moveFocusSuccessfully) {
                        com.android.server.wm.WindowManagerService.this.syncInputTransactions(false);
                        return true;
                    }
                    return false;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal
        public android.window.ScreenCapture.ScreenshotHardwareBuffer takeAssistScreenshot(java.util.Set<java.lang.Integer> windowTypesToExclude) {
            return com.android.server.wm.WindowManagerService.this.takeAssistScreenshot(windowTypesToExclude);
        }
    }

    private final class ImeTargetVisibilityPolicyImpl extends com.android.server.wm.ImeTargetVisibilityPolicy {
        private ImeTargetVisibilityPolicyImpl() {
        }

        @Override // com.android.server.wm.ImeTargetVisibilityPolicy
        public boolean showImeScreenshot(android.os.IBinder imeTarget, int displayId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState imeTargetWindow = com.android.server.wm.WindowManagerService.this.mWindowMap.get(imeTarget);
                    if (imeTargetWindow == null) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    com.android.server.wm.DisplayContent dc = com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(displayId);
                    if (dc == null) {
                        android.util.Slog.w(com.android.server.wm.WindowManagerService.TAG, "Invalid displayId:" + displayId + ", fail to show ime screenshot");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    dc.showImeScreenshot(imeTargetWindow);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return true;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        @Override // com.android.server.wm.ImeTargetVisibilityPolicy
        public boolean removeImeScreenshot(int displayId) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowManagerService.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = com.android.server.wm.WindowManagerService.this.mRoot.getDisplayContent(displayId);
                    if (dc == null) {
                        android.util.Slog.w(com.android.server.wm.WindowManagerService.TAG, "Invalid displayId:" + displayId + ", fail to remove ime screenshot");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    dc.removeImeSurfaceImmediately();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return true;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }
    }

    void registerAppFreezeListener(com.android.server.wm.WindowManagerService.AppFreezeListener listener) {
        if (!this.mAppFreezeListeners.contains(listener)) {
            this.mAppFreezeListeners.add(listener);
        }
    }

    void unregisterAppFreezeListener(com.android.server.wm.WindowManagerService.AppFreezeListener listener) {
        this.mAppFreezeListeners.remove(listener);
    }

    public void disableNonVrUi(boolean disable) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            boolean showAlertWindowNotifications = !disable;
            try {
                if (showAlertWindowNotifications == this.mShowAlertWindowNotifications) {
                    resetPriorityAfterLockedSection();
                    return;
                }
                this.mShowAlertWindowNotifications = showAlertWindowNotifications;
                for (int i = this.mSessions.size() - 1; i >= 0; i--) {
                    com.android.server.wm.Session s = this.mSessions.valueAt(i);
                    s.setShowingAlertWindowNotificationAllowed(this.mShowAlertWindowNotifications);
                }
                resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    boolean hasWideColorGamutSupport() {
        return this.mHasWideColorGamutSupport && android.os.SystemProperties.getInt("persist.sys.sf.native_mode", 0) != 1;
    }

    boolean hasHdrSupport() {
        return this.mHasHdrSupport && hasWideColorGamutSupport();
    }

    void updateNonSystemOverlayWindowsVisibilityIfNeeded(com.android.server.wm.WindowState win, boolean surfaceShown) {
        if (!win.hideNonSystemOverlayWindowsWhenVisible() && !this.mHidingNonSystemOverlayWindows.contains(win)) {
            return;
        }
        boolean systemAlertWindowsHidden = !this.mHidingNonSystemOverlayWindows.isEmpty();
        if (surfaceShown && win.hideNonSystemOverlayWindowsWhenVisible()) {
            if (!this.mHidingNonSystemOverlayWindows.contains(win)) {
                this.mHidingNonSystemOverlayWindows.add(win);
            }
        } else {
            this.mHidingNonSystemOverlayWindows.remove(win);
        }
        final boolean hideSystemAlertWindows = !this.mHidingNonSystemOverlayWindows.isEmpty();
        if (systemAlertWindowsHidden == hideSystemAlertWindows) {
            return;
        }
        this.mRoot.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda38
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.wm.WindowState) obj).setForceHideNonSystemOverlayWindowIfNeeded(hideSystemAlertWindows);
            }
        }, false);
    }

    public void applyMagnificationSpecLocked(int displayId, android.view.MagnificationSpec spec) {
        com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
        if (displayContent != null) {
            displayContent.applyMagnificationSpec(spec);
        }
    }

    android.view.SurfaceControl.Builder makeSurfaceBuilder(android.view.SurfaceSession s) {
        return this.mSurfaceControlFactory.apply(s);
    }

    void onLockTaskStateChanged(final int lockTaskState) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.forAllDisplayPolicies(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda12
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wm.DisplayPolicy) obj).onLockTaskStateChangedLw(lockTaskState);
                    }
                });
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0017 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void syncInputTransactions(boolean r8) {
        /*
            r7 = this;
            long r0 = android.os.Binder.clearCallingIdentity()
            if (r8 == 0) goto L9
            r7.waitForAnimationsToComplete()     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
        L9:
            java.util.function.Supplier<android.view.SurfaceControl$Transaction> r2 = r7.mTransactionFactory     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            java.lang.Object r2 = r2.get()     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            android.view.SurfaceControl$Transaction r2 = (android.view.SurfaceControl.Transaction) r2     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            com.android.server.wm.WindowManagerGlobalLock r3 = r7.mGlobalLock     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            boostPriorityForLockedSection()     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            monitor-enter(r3)     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            com.android.server.wm.WindowSurfacePlacer r4 = r7.mWindowPlacerLocked     // Catch: java.lang.Throwable -> L48
            r4.performSurfacePlacementIfScheduled()     // Catch: java.lang.Throwable -> L48
            com.android.server.wm.RootWindowContainer r4 = r7.mRoot     // Catch: java.lang.Throwable -> L48
            com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda24 r5 = new com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda24     // Catch: java.lang.Throwable -> L48
            r5.<init>()     // Catch: java.lang.Throwable -> L48
            r4.forAllDisplays(r5)     // Catch: java.lang.Throwable -> L48
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L48
            resetPriorityAfterLockedSection()     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            java.util.concurrent.CountDownLatch r3 = new java.util.concurrent.CountDownLatch     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            r4 = 1
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            java.util.Objects.requireNonNull(r3)     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            com.android.server.location.settings.SettingsStore$$ExternalSyntheticLambda0 r4 = new com.android.server.location.settings.SettingsStore$$ExternalSyntheticLambda0     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            r4.<init>(r3)     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            android.view.SurfaceControl$Transaction r4 = r2.addWindowInfosReportedListener(r4)     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            r4.apply()     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            java.util.concurrent.TimeUnit r4 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            r5 = 5000(0x1388, double:2.4703E-320)
            r3.await(r5, r4)     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            goto L59
        L48:
            r4 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L48
            resetPriorityAfterLockedSection()     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
            throw r4     // Catch: java.lang.Throwable -> L4e java.lang.InterruptedException -> L50
        L4e:
            r2 = move-exception
            goto L5e
        L50:
            r2 = move-exception
            java.lang.String r3 = "WindowManager"
            java.lang.String r4 = "Exception thrown while waiting for window infos to be reported"
            android.util.Slog.e(r3, r4, r2)     // Catch: java.lang.Throwable -> L4e
        L59:
            android.os.Binder.restoreCallingIdentity(r0)
            return
        L5e:
            android.os.Binder.restoreCallingIdentity(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerService.syncInputTransactions(boolean):void");
    }

    private void waitForAnimationsToComplete() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            long timeoutRemaining = 5000;
            try {
                this.mAnimator.mNotifyWhenNoAnimation = true;
                boolean animateStarting = false;
                while (true) {
                    if (timeoutRemaining > 0) {
                        animateStarting = !this.mAtmService.getTransitionController().isShellTransitionsEnabled() && this.mRoot.forAllActivities(new java.util.function.Predicate() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda4
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj) {
                                return ((com.android.server.wm.ActivityRecord) obj).hasStartingWindow();
                            }
                        });
                        boolean isAnimating = this.mAnimator.isAnimationScheduled() || this.mRoot.isAnimating(5, -1) || animateStarting;
                        if (!isAnimating && !this.mAtmService.getTransitionController().inTransition()) {
                            break;
                        }
                        long startTime = java.lang.System.currentTimeMillis();
                        try {
                            this.mGlobalLock.wait(timeoutRemaining);
                        } catch (java.lang.InterruptedException e) {
                        }
                        timeoutRemaining -= java.lang.System.currentTimeMillis() - startTime;
                    } else {
                        break;
                    }
                }
                this.mAnimator.mNotifyWhenNoAnimation = false;
                com.android.server.wm.WindowContainer animatingContainer = this.mRoot.getAnimatingContainer(5, -1);
                if (this.mAnimator.isAnimationScheduled() || animatingContainer != null || animateStarting) {
                    android.util.Slog.w(TAG, "Timed out waiting for animations to complete, animatingContainer=" + animatingContainer + " animationType=" + com.android.server.wm.SurfaceAnimator.animationTypeToString(animatingContainer != null ? animatingContainer.mSurfaceAnimator.getAnimationType() : 0) + " animateStarting=" + animateStarting);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    void onAnimationFinished() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mGlobalLock.notifyAll();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPointerDownOutsideFocusLocked(com.android.server.wm.InputTarget t) {
        com.android.server.wm.Task task;
        this.mWindowManagerServiceExt.notifyTouchOutsideFocus(t);
        if (t == null || !t.receiveFocusFromTapOutside()) {
            return;
        }
        if (this.mRecentsAnimationController != null && this.mRecentsAnimationController.getTargetAppMainWindow() == t) {
            return;
        }
        com.android.server.wm.WindowState w = t.getWindowState();
        if (w != null && (task = w.getTask()) != null && w.mTransitionController.isTransientHide(task)) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(t);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -2065144681579661392L, 0, null, protoLogParam0);
        }
        if (this.mFocusedInputTarget != t && this.mFocusedInputTarget != null) {
            this.mFocusedInputTarget.handleTapOutsideFocusOutsideSelf();
        }
        this.mAtmService.mTaskSupervisor.mUserLeaving = true;
        t.handleTapOutsideFocusInsideSelf();
        this.mAtmService.mTaskSupervisor.mUserLeaving = false;
        this.mWindowManagerServiceExt.cpuFrequencyBoostIfNeed(t.getActivityRecord());
        this.mWindowManagerServiceExt.handleCompactWindowTouchFocusChange(t.getWindowState());
        this.mWindowManagerServiceExt.handleFlexibleLockAppWindow(t.getWindowState());
    }

    void handleTaskFocusChange(com.android.server.wm.Task task, com.android.server.wm.ActivityRecord touchedActivity) {
        if (task == null) {
            return;
        }
        if (task.isActivityTypeHome()) {
            com.android.server.wm.TaskDisplayArea homeTda = task.getDisplayArea();
            com.android.server.wm.WindowState curFocusedWindow = getFocusedWindow();
            if (curFocusedWindow != null && homeTda != null && curFocusedWindow.isDescendantOf(homeTda) && this.mWindowManagerServiceExt.currentFoucusWindowModeNotZoomMode(curFocusedWindow.getWindowingMode()) && !this.mWindowManagerServiceExt.isFocusChangeWithNonFlexible(curFocusedWindow)) {
                return;
            }
        }
        if (this.mWindowManagerServiceExt.ignoreFingerprintWindow(this.mContext, task)) {
            android.util.Slog.d(TAG, "split screen mode, FP is shown, igonre focus change.");
            return;
        }
        if (this.mWindowManagerServiceExt.isActivityTypeMultiSearch(task)) {
            android.util.Slog.d(TAG, "multisearch task igonre focus change.");
        } else if (task != null && task.getWrapper().getExtImpl().isTaskCanvas() && !this.mWindowManagerServiceExt.isNoCanvasActivity(touchedActivity)) {
            android.util.Slog.d(TAG, "canvas task igonre focus change.");
        } else {
            this.mAtmService.setFocusedTask(task.mTaskId, touchedActivity);
        }
    }

    static class WindowContainerInfo {
        private final android.window.WindowContainerToken mToken;
        private final int mUid;

        private WindowContainerInfo(int uid, android.window.WindowContainerToken token) {
            this.mUid = uid;
            this.mToken = token;
        }

        public int getUid() {
            return this.mUid;
        }

        public android.window.WindowContainerToken getToken() {
            return this.mToken;
        }
    }

    com.android.server.wm.WindowManagerService.WindowContainerInfo getTaskWindowContainerInfoForRecordingSession(android.view.ContentRecordingSession session) {
        android.window.WindowContainerToken taskWindowContainerToken = null;
        com.android.server.wm.Task targetTask = null;
        if (session.getTokenToRecord() != null) {
            final android.os.IBinder launchCookie = session.getTokenToRecord();
            com.android.server.wm.ActivityRecord targetActivity = this.mRoot.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.WindowManagerService.lambda$getTaskWindowContainerInfoForRecordingSession$25(launchCookie, (com.android.server.wm.ActivityRecord) obj);
                }
            });
            if (targetActivity == null) {
                android.util.Slog.w(TAG, "Unable to find the activity for this launch cookie");
            } else if (targetActivity.getTask() == null) {
                android.util.Slog.w(TAG, "Unable to find the task for this launch cookie");
            } else {
                targetTask = targetActivity.getTask();
                taskWindowContainerToken = targetTask.mRemoteToken.toWindowContainerToken();
            }
        }
        if (taskWindowContainerToken == null && session.getTaskId() != -1) {
            final int targetTaskId = session.getTaskId();
            targetTask = this.mRoot.getTask(new java.util.function.Predicate() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.wm.Task) obj).isTaskId(targetTaskId);
                }
            });
            if (targetTask == null) {
                android.util.Slog.w(TAG, "Unable to find the task for this projection");
            } else {
                taskWindowContainerToken = targetTask.mRemoteToken.toWindowContainerToken();
            }
        }
        if (taskWindowContainerToken == null) {
            android.util.Slog.w(TAG, "Unable to find the WindowContainerToken for ContentRecordingSession");
            return null;
        }
        return new com.android.server.wm.WindowManagerService.WindowContainerInfo(targetTask.effectiveUid, taskWindowContainerToken);
    }

    static /* synthetic */ boolean lambda$getTaskWindowContainerInfoForRecordingSession$25(android.os.IBinder launchCookie, com.android.server.wm.ActivityRecord activity) {
        return activity.mLaunchCookie == launchCookie;
    }

    private int sanitizeFlagSlippery(int flags, java.lang.String windowName, int callingUid, int callingPid) {
        if ((536870912 & flags) == 0) {
            return flags;
        }
        int permissionResult = this.mContext.checkPermission("android.permission.ALLOW_SLIPPERY_TOUCHES", callingPid, callingUid);
        if (permissionResult != 0) {
            android.util.Slog.w(TAG, "Removing FLAG_SLIPPERY from '" + windowName + "' because it doesn't have ALLOW_SLIPPERY_TOUCHES permission");
            return (-536870913) & flags;
        }
        return flags;
    }

    private int sanitizeInputFeatures(int inputFeatures, java.lang.String windowName, int callingUid, int callingPid, boolean isTrustedOverlay) {
        if ((inputFeatures & 4) != 0) {
            int permissionResult = this.mContext.checkPermission("android.permission.MONITOR_INPUT", callingPid, callingUid);
            if (permissionResult != 0) {
                throw new java.lang.IllegalArgumentException("Cannot use INPUT_FEATURE_SPY from '" + windowName + "' because it doesn't the have MONITOR_INPUT permission");
            }
        }
        int permissionResult2 = inputFeatures & 8;
        if (permissionResult2 != 0 && !isTrustedOverlay) {
            android.util.Slog.w(TAG, "Removing INPUT_FEATURE_SENSITIVE_FOR_PRIVACY from '" + windowName + "' because it isn't a trusted overlay");
            return inputFeatures & (-9);
        }
        return inputFeatures;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0096  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void grantInputChannel(com.android.server.wm.Session r22, int r23, int r24, int r25, android.view.SurfaceControl r26, android.os.IBinder r27, android.window.InputTransferToken r28, int r29, int r30, int r31, int r32, android.os.IBinder r33, android.window.InputTransferToken r34, java.lang.String r35, android.view.InputChannel r36) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 205
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerService.grantInputChannel(com.android.server.wm.Session, int, int, int, android.view.SurfaceControl, android.os.IBinder, android.window.InputTransferToken, int, int, int, int, android.os.IBinder, android.window.InputTransferToken, java.lang.String, android.view.InputChannel):void");
    }

    public boolean transferTouchGesture(android.window.InputTransferToken transferFromToken, android.window.InputTransferToken transferToToken) {
        boolean didTransfer;
        java.util.Objects.requireNonNull(transferFromToken);
        java.util.Objects.requireNonNull(transferToToken);
        int callingUid = android.os.Binder.getCallingUid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState windowStateTo = this.mInputToWindowMap.get(transferToToken.getToken());
                    if (windowStateTo != null) {
                        didTransfer = this.mEmbeddedWindowController.transferToHost(callingUid, transferFromToken, windowStateTo);
                    } else {
                        com.android.server.wm.WindowState windowStateFrom = this.mInputToWindowMap.get(transferFromToken.getToken());
                        didTransfer = this.mEmbeddedWindowController.transferToEmbedded(callingUid, windowStateFrom, transferToToken);
                    }
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
            return didTransfer;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private void updateInputChannel(android.os.IBinder channelToken, int callingUid, int callingPid, int displayId, android.view.SurfaceControl surface, java.lang.String name, android.view.InputApplicationHandle applicationHandle, int flags, int privateFlags, int inputFeatures, int type, android.graphics.Region region, android.os.IBinder clientToken) {
        android.view.InputWindowHandle h = new android.view.InputWindowHandle(applicationHandle, displayId);
        h.token = channelToken;
        h.setWindowToken(clientToken);
        h.name = name;
        boolean isTrustedOverlay = (privateFlags & 536870912) != 0;
        int flags2 = sanitizeFlagSlippery(flags, name, callingUid, callingPid);
        int inputFeatures2 = sanitizeInputFeatures(inputFeatures, name, callingUid, callingPid, isTrustedOverlay);
        int sanitizedLpFlags = (flags2 & 536870936) | 32;
        h.layoutParamsType = type;
        h.layoutParamsFlags = sanitizedLpFlags;
        h.inputConfig = com.android.server.wm.InputConfigAdapter.getInputConfigFromWindowParams(type, sanitizedLpFlags, inputFeatures2);
        if ((flags2 & 8) != 0) {
            h.inputConfig |= 4;
        }
        if (displayId == 1 && (flags2 & 1048576) != 0) {
            h.inputConfig |= 32;
        }
        h.dispatchingTimeoutMillis = android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        h.ownerUid = callingUid;
        h.ownerPid = callingPid;
        if (region == null) {
            h.replaceTouchableRegionWithCrop((android.view.SurfaceControl) null);
        } else {
            h.touchableRegion.set(region);
            h.replaceTouchableRegionWithCrop = false;
            int permissionResult = this.mContext.checkPermission("android.permission.MANAGE_ACTIVITY_TASKS", callingPid, callingUid);
            if (permissionResult != 0) {
                h.setTouchableRegionCrop(surface);
            }
        }
        android.view.SurfaceControl.Transaction t = this.mTransactionFactory.get();
        h.setTrustedOverlay(t, surface, isTrustedOverlay);
        t.setInputWindowInfo(surface, h);
        t.apply();
        t.close();
        surface.release();
    }

    void updateInputChannel(android.os.IBinder channelToken, int displayId, android.view.SurfaceControl surface, int flags, int privateFlags, int inputFeatures, android.graphics.Region region) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.EmbeddedWindowController.EmbeddedWindow win = this.mEmbeddedWindowController.get(channelToken);
                if (win == null) {
                    android.util.Slog.e(TAG, "Couldn't find window for provided channelToken.");
                    resetPriorityAfterLockedSection();
                    return;
                }
                java.lang.String name = win.toString();
                android.view.InputApplicationHandle applicationHandle = win.getApplicationHandle();
                if (!this.mWindowManagerServiceExt.isSecondaryhomePackageName(win)) {
                    win.setIsFocusable((flags & 8) == 0);
                }
                resetPriorityAfterLockedSection();
                updateInputChannel(channelToken, win.mOwnerUid, win.mOwnerPid, displayId, surface, name, applicationHandle, flags, privateFlags, inputFeatures, win.mWindowType, region, win.mClient);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    boolean moveFocusToAdjacentEmbeddedWindow(com.android.server.wm.WindowState focusedWindow) {
        com.android.server.wm.TaskFragment taskFragment = focusedWindow.getTaskFragment();
        if (taskFragment == null || !com.android.window.flags.Flags.embeddedActivityBackNavFlag() || !focusedWindow.mActivityRecord.isEmbedded()) {
            return false;
        }
        com.android.server.wm.TaskFragment adjacentTaskFragment = taskFragment.getAdjacentTaskFragment();
        com.android.server.wm.ActivityRecord adjacentTopActivity = adjacentTaskFragment != null ? adjacentTaskFragment.topRunningActivity() : null;
        if (adjacentTopActivity == null || adjacentTopActivity.getLastWindowCreateTime() < focusedWindow.mActivityRecord.getLastWindowCreateTime()) {
            return false;
        }
        moveFocusToActivity(adjacentTopActivity);
        return !focusedWindow.isFocused();
    }

    boolean moveFocusToAdjacentWindow(com.android.server.wm.WindowState fromWin, int direction) {
        com.android.server.wm.TaskFragment fromFragment;
        com.android.server.wm.TaskFragment adjacentFragment;
        if (!fromWin.isFocused() || (fromFragment = fromWin.getTaskFragment()) == null || (adjacentFragment = fromFragment.getAdjacentTaskFragment()) == null || adjacentFragment.asTask() != null || adjacentFragment.isIsolatedNav()) {
            return false;
        }
        android.graphics.Rect fromBounds = fromFragment.getBounds();
        android.graphics.Rect adjacentBounds = adjacentFragment.getBounds();
        switch (direction) {
            case 1:
            case 2:
                break;
            case 17:
                if (adjacentBounds.left >= fromBounds.left) {
                    return false;
                }
                break;
            case 33:
                if (adjacentBounds.top >= fromBounds.top) {
                    return false;
                }
                break;
            case 66:
                if (adjacentBounds.right <= fromBounds.right) {
                    return false;
                }
                break;
            case 130:
                if (adjacentBounds.bottom <= fromBounds.bottom) {
                    return false;
                }
                break;
            default:
                return false;
        }
        com.android.server.wm.ActivityRecord topRunningActivity = adjacentFragment.topRunningActivity(true);
        if (topRunningActivity == null) {
            return false;
        }
        moveFocusToActivity(topRunningActivity);
        return !fromWin.isFocused();
    }

    void moveFocusToActivity(com.android.server.wm.ActivityRecord activity) {
        moveDisplayToTopInternal(activity.getDisplayId());
        handleTaskFocusChange(activity.getTask(), activity);
    }

    public boolean isLayerTracing() {
        android.os.IBinder sf;
        if (!checkCallingPermission("android.permission.DUMP", "isLayerTracing()")) {
            throw new java.lang.SecurityException("Requires DUMP permission");
        }
        long token = android.os.Binder.clearCallingIdentity();
        android.os.Parcel data = null;
        android.os.Parcel reply = null;
        try {
            try {
                try {
                    sf = android.os.ServiceManager.getService("SurfaceFlinger");
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Failed to get layer tracing");
                    if (data != null) {
                        data.recycle();
                    }
                    if (reply != null) {
                    }
                }
                if (sf == null) {
                    if (0 != 0) {
                        data.recycle();
                    }
                    if (0 != 0) {
                    }
                    android.os.Binder.restoreCallingIdentity(token);
                    return false;
                }
                reply = android.os.Parcel.obtain();
                data = android.os.Parcel.obtain();
                data.writeInterfaceToken("android.ui.ISurfaceComposer");
                sf.transact(1026, data, reply, 0);
                boolean z = reply.readBoolean();
                if (data != null) {
                    data.recycle();
                }
                if (reply != null) {
                    reply.recycle();
                }
                android.os.Binder.restoreCallingIdentity(token);
                return z;
                reply.recycle();
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            } catch (java.lang.Throwable th) {
                if (data != null) {
                    data.recycle();
                }
                if (reply != null) {
                    reply.recycle();
                }
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th2;
        }
    }

    public void setLayerTracing(boolean enabled) {
        if (!checkCallingPermission("android.permission.DUMP", "setLayerTracing()")) {
            throw new java.lang.SecurityException("Requires DUMP permission");
        }
        long token = android.os.Binder.clearCallingIdentity();
        android.os.Parcel data = null;
        try {
            try {
                try {
                    android.os.IBinder sf = android.os.ServiceManager.getService("SurfaceFlinger");
                    if (sf != null) {
                        data = android.os.Parcel.obtain();
                        data.writeInterfaceToken("android.ui.ISurfaceComposer");
                        data.writeInt(enabled ? 1 : 0);
                        sf.transact(1025, data, null, 0);
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Failed to set layer tracing");
                    if (data != null) {
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        } finally {
            if (data != null) {
                data.recycle();
            }
        }
    }

    public void setLayerTracingFlags(int flags) {
        if (!checkCallingPermission("android.permission.DUMP", "setLayerTracingFlags")) {
            throw new java.lang.SecurityException("Requires DUMP permission");
        }
        long token = android.os.Binder.clearCallingIdentity();
        android.os.Parcel data = null;
        try {
            try {
                try {
                    android.os.IBinder sf = android.os.ServiceManager.getService("SurfaceFlinger");
                    if (sf != null) {
                        data = android.os.Parcel.obtain();
                        data.writeInterfaceToken("android.ui.ISurfaceComposer");
                        data.writeInt(flags);
                        sf.transact(1033, data, null, 0);
                    }
                } finally {
                    if (data != null) {
                        data.recycle();
                    }
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to set layer tracing flags");
                if (data != null) {
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setActiveTransactionTracing(boolean active) {
        if (!checkCallingPermission("android.permission.DUMP", "setActiveTransactionTracing()")) {
            throw new java.lang.SecurityException("Requires DUMP permission");
        }
        long token = android.os.Binder.clearCallingIdentity();
        android.os.Parcel data = null;
        try {
            try {
                try {
                    android.os.IBinder sf = android.os.ServiceManager.getService("SurfaceFlinger");
                    if (sf != null) {
                        data = android.os.Parcel.obtain();
                        data.writeInterfaceToken("android.ui.ISurfaceComposer");
                        data.writeInt(active ? 1 : 0);
                        sf.transact(1041, data, null, 0);
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Failed to set transaction tracing");
                    if (data != null) {
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        } finally {
            if (data != null) {
                data.recycle();
            }
        }
    }

    public boolean mirrorDisplay(int displayId, android.view.SurfaceControl outSurfaceControl) {
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "mirrorDisplay()")) {
            throw new java.lang.SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    android.util.Slog.e(TAG, "Invalid displayId " + displayId + " for mirrorDisplay");
                    resetPriorityAfterLockedSection();
                    return false;
                }
                android.view.SurfaceControl displaySc = displayContent.getWindowingLayer();
                resetPriorityAfterLockedSection();
                android.view.SurfaceControl mirror = android.view.SurfaceControl.mirrorSurface(displaySc);
                outSurfaceControl.copyFrom(mirror, "WMS.mirrorDisplay");
                mirror.release();
                return true;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean getWindowInsets(int displayId, android.os.IBinder token, android.view.InsetsState outInsetsState) {
        boolean zAreSystemBarsForcedConsumedLw;
        com.android.server.wm.WindowState mainWin;
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = getDisplayContentOrCreate(displayId, token);
                    if (dc == null) {
                        throw new android.view.WindowManager.InvalidDisplayException("Display#" + displayId + "could not be found!");
                    }
                    com.android.server.wm.WindowToken winToken = dc.getWindowToken(token);
                    dc.getInsetsPolicy().getInsetsForWindowMetrics(winToken, outInsetsState);
                    if ((winToken instanceof com.android.server.wm.ActivityRecord) && winToken.getWindowingMode() == 120 && (mainWin = ((com.android.server.wm.ActivityRecord) winToken).findMainWindow()) != null) {
                        mainWin.getWrapper().getExtImpl().hookGetCompatInsetsState(outInsetsState);
                    }
                    zAreSystemBarsForcedConsumedLw = dc.getDisplayPolicy().areSystemBarsForcedConsumedLw();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
            return zAreSystemBarsForcedConsumedLw;
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public java.util.List<android.view.DisplayInfo> getPossibleDisplayInfo(int displayId) {
        int callingUid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (!this.mAtmService.isCallerRecents(callingUid) && (!com.android.window.flags.Flags.multiCrop() || callingUid != 1000)) {
                        android.util.Slog.e(TAG, "Unable to verify uid for getPossibleDisplayInfo on uid " + callingUid);
                        java.util.ArrayList arrayList = new java.util.ArrayList();
                        resetPriorityAfterLockedSection();
                        return arrayList;
                    }
                    java.util.List<android.view.DisplayInfo> possibleDisplayInfos = this.mPossibleDisplayInfoMapper.getPossibleDisplayInfos(displayId);
                    resetPriorityAfterLockedSection();
                    return possibleDisplayInfos;
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    java.util.List<android.view.DisplayInfo> getPossibleDisplayInfoLocked(int displayId) {
        return this.mPossibleDisplayInfoMapper.getPossibleDisplayInfos(displayId);
    }

    void grantEmbeddedWindowFocus(com.android.server.wm.Session session, android.window.InputTransferToken inputTransferToken, boolean grantFocus) throws java.lang.Throwable {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    com.android.server.wm.EmbeddedWindowController.EmbeddedWindow embeddedWindow = this.mEmbeddedWindowController.getByInputTransferToken(inputTransferToken);
                    if (embeddedWindow == null) {
                        android.util.Slog.e(TAG, "Embedded window not found");
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    if (embeddedWindow.mSession != session) {
                        android.util.Slog.e(TAG, "Window not in session:" + session);
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    android.os.IBinder inputToken = embeddedWindow.getInputChannelToken();
                    if (inputToken == null) {
                        android.util.Slog.e(TAG, "Focus token found but input channel token not found");
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    android.view.SurfaceControl.Transaction t = this.mTransactionFactory.get();
                    int displayId = embeddedWindow.mDisplayId;
                    if (grantFocus) {
                        t.setFocusedWindow(inputToken, embeddedWindow.toString(), displayId).apply();
                        android.util.EventLog.writeEvent(LOGTAG_INPUT_FOCUS, "Focus request " + embeddedWindow, "reason=grantEmbeddedWindowFocus(true)");
                    } else {
                        com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                        com.android.server.wm.WindowState newFocusTarget = displayContent == null ? null : displayContent.findFocusedWindow();
                        if (newFocusTarget == null) {
                            t.setFocusedWindow(null, null, displayId).apply();
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[1]) {
                                java.lang.String protoLogParam0 = java.lang.String.valueOf(embeddedWindow);
                                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, -7394143854567081754L, 0, null, protoLogParam0);
                            }
                            resetPriorityAfterLockedSection();
                            return;
                        }
                        t.setFocusedWindow(newFocusTarget.mInputChannelToken, newFocusTarget.getName(), displayId).apply();
                        android.util.EventLog.writeEvent(LOGTAG_INPUT_FOCUS, "Focus request " + newFocusTarget, "reason=grantEmbeddedWindowFocus(false)");
                    }
                    if (!com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS.isLogToLogcat()) {
                        android.util.Slog.d(TAG, "grantEmbeddedWindowFocus embeddedWindow=" + embeddedWindow.toString() + ",grantFocus=" + grantFocus + ",inputToken=" + inputToken);
                    }
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[1]) {
                        java.lang.String protoLogParam02 = java.lang.String.valueOf(embeddedWindow);
                        java.lang.String protoLogParam1 = java.lang.String.valueOf(grantFocus);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, -6056928081282320632L, 0, null, protoLogParam02, protoLogParam1);
                    }
                    resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    th = th;
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void grantEmbeddedWindowFocus(com.android.server.wm.Session session, android.view.IWindow callingWindow, android.window.InputTransferToken inputTransferToken, boolean grantFocus) throws java.lang.Throwable {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    com.android.server.wm.WindowState hostWindow = windowForClientLocked(session, callingWindow, false);
                    if (hostWindow == null) {
                        android.util.Slog.e(TAG, "Host window not found");
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    if (hostWindow.mInputChannel == null) {
                        android.util.Slog.e(TAG, "Host window does not have an input channel");
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.wm.EmbeddedWindowController.EmbeddedWindow embeddedWindow = this.mEmbeddedWindowController.getByInputTransferToken(inputTransferToken);
                    if (embeddedWindow == null) {
                        android.util.Slog.e(TAG, "Embedded window not found");
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    if (embeddedWindow.mHostWindowState != hostWindow) {
                        android.util.Slog.e(TAG, "Embedded window does not belong to the host");
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    if (grantFocus) {
                        hostWindow.mInputWindowHandle.setFocusTransferTarget(embeddedWindow.getInputChannelToken());
                        android.util.EventLog.writeEvent(LOGTAG_INPUT_FOCUS, "Transfer focus request " + embeddedWindow, "reason=grantEmbeddedWindowFocus(true)");
                    } else {
                        hostWindow.mInputWindowHandle.setFocusTransferTarget(null);
                        android.util.EventLog.writeEvent(LOGTAG_INPUT_FOCUS, "Transfer focus request " + hostWindow, "reason=grantEmbeddedWindowFocus(false)");
                    }
                    com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContent(hostWindow.getDisplayId());
                    if (dc != null) {
                        dc.getInputMonitor().updateInputWindowsLw(true);
                    }
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[1]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(embeddedWindow);
                        java.lang.String protoLogParam1 = java.lang.String.valueOf(grantFocus);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, -6056928081282320632L, 0, null, protoLogParam0, protoLogParam1);
                    }
                    resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    th = th;
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void holdLock(android.os.IBinder token, int durationMs) {
        this.mTestUtilityService.verifyHoldLockToken(token);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                android.os.SystemClock.sleep(durationMs);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    public java.lang.String[] getSupportedDisplayHashAlgorithms() {
        return this.mDisplayHashController.getSupportedHashAlgorithms();
    }

    public android.view.displayhash.VerifiedDisplayHash verifyDisplayHash(android.view.displayhash.DisplayHash displayHash) {
        return this.mDisplayHashController.verifyDisplayHash(displayHash);
    }

    public void setDisplayHashThrottlingEnabled(boolean enable) {
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "setDisplayHashThrottle()")) {
            throw new java.lang.SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        this.mDisplayHashController.setDisplayHashThrottlingEnabled(enable);
    }

    public boolean isTaskSnapshotSupported() {
        boolean z;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                z = !this.mTaskSnapshotController.shouldDisableSnapshots();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return z;
    }

    void generateDisplayHash(com.android.server.wm.Session session, android.view.IWindow window, android.graphics.Rect boundsInWindow, java.lang.String hashAlgorithm, android.os.RemoteCallback callback) {
        android.graphics.Rect boundsInDisplay = new android.graphics.Rect(boundsInWindow);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState win = windowForClientLocked(session, window, false);
                if (win == null) {
                    android.util.Slog.w(TAG, "Failed to generate DisplayHash. Invalid window");
                    this.mDisplayHashController.sendDisplayHashError(callback, -3);
                    resetPriorityAfterLockedSection();
                    return;
                }
                if (win.mActivityRecord != null && win.mActivityRecord.isState(com.android.server.wm.ActivityRecord.State.RESUMED)) {
                    com.android.server.wm.DisplayContent displayContent = win.getDisplayContent();
                    if (displayContent == null) {
                        android.util.Slog.w(TAG, "Failed to generate DisplayHash. Window is not on a display");
                        this.mDisplayHashController.sendDisplayHashError(callback, -4);
                        resetPriorityAfterLockedSection();
                        return;
                    }
                    android.view.SurfaceControl displaySurfaceControl = displayContent.getSurfaceControl();
                    this.mDisplayHashController.calculateDisplayHashBoundsLocked(win, boundsInWindow, boundsInDisplay);
                    if (boundsInDisplay.isEmpty()) {
                        android.util.Slog.w(TAG, "Failed to generate DisplayHash. Bounds are not on screen");
                        this.mDisplayHashController.sendDisplayHashError(callback, -4);
                        resetPriorityAfterLockedSection();
                        return;
                    } else {
                        resetPriorityAfterLockedSection();
                        int uid = session.mUid;
                        android.window.ScreenCapture.LayerCaptureArgs.Builder args = new android.window.ScreenCapture.LayerCaptureArgs.Builder(displaySurfaceControl).setUid(uid).setSourceCrop(boundsInDisplay);
                        this.mDisplayHashController.generateDisplayHash(args, boundsInWindow, hashAlgorithm, uid, callback);
                        return;
                    }
                }
                this.mDisplayHashController.sendDisplayHashError(callback, -3);
                resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    boolean shouldRestoreImeVisibility(android.os.IBinder imeTargetWindowToken) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState imeTargetWindow = this.mWindowMap.get(imeTargetWindowToken);
                if (imeTargetWindow == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                com.android.server.wm.Task imeTargetWindowTask = imeTargetWindow.getTask();
                if (imeTargetWindowTask == null) {
                    resetPriorityAfterLockedSection();
                    return false;
                }
                if (imeTargetWindow.mActivityRecord != null && imeTargetWindow.mActivityRecord.mLastImeShown) {
                    resetPriorityAfterLockedSection();
                    return true;
                }
                resetPriorityAfterLockedSection();
                android.window.TaskSnapshot snapshot = getTaskSnapshot(imeTargetWindowTask.mTaskId, imeTargetWindowTask.mUserId, false, false);
                boolean mHasImeSurface = snapshot != null && snapshot.hasImeSurface();
                if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE.isLogToLogcat()) {
                    android.util.Slog.i(TAG, "shouldRestoreImeVisibility mHasImeSurface:" + mHasImeSurface);
                }
                return snapshot != null && snapshot.hasImeSurface();
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public int getImeDisplayId() {
        int displayId;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent dc = this.mRoot.getTopFocusedDisplayContent();
                displayId = dc.getImePolicy() == 0 ? dc.getDisplayId() : 0;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return displayId;
    }

    public void setTaskSnapshotEnabled(boolean enabled) {
        this.mTaskSnapshotController.setSnapshotEnabled(enabled);
    }

    public void registerTaskFpsCallback(int taskId, android.window.ITaskFpsCallback callback) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_FPS_COUNTER") != 0) {
            int pid = android.os.Binder.getCallingPid();
            throw new java.lang.SecurityException("Access denied to process: " + pid + ", must have permission android.permission.ACCESS_FPS_COUNTER");
        }
        if (this.mRoot.anyTaskForId(taskId) == null) {
            throw new java.lang.IllegalArgumentException("no task with taskId: " + taskId);
        }
        this.mTaskFpsCallbackController.registerListener(taskId, callback);
    }

    public void unregisterTaskFpsCallback(android.window.ITaskFpsCallback callback) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.ACCESS_FPS_COUNTER") != 0) {
            int pid = android.os.Binder.getCallingPid();
            throw new java.lang.SecurityException("Access denied to process: " + pid + ", must have permission android.permission.ACCESS_FPS_COUNTER");
        }
        this.mTaskFpsCallbackController.lambda$registerListener$0(callback);
    }

    public android.graphics.Bitmap snapshotTaskForRecents(int taskId) {
        android.window.TaskSnapshot taskSnapshot;
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "snapshotTaskForRecents()")) {
            throw new java.lang.SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.Task task = this.mRoot.anyTaskForId(taskId, 1);
                    if (task == null) {
                        throw new java.lang.IllegalArgumentException("Failed to find matching task for taskId=" + taskId);
                    }
                    taskSnapshot = this.mTaskSnapshotController.captureSnapshot(task);
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
            if (taskSnapshot == null || taskSnapshot.getHardwareBuffer() == null) {
                return null;
            }
            return android.graphics.Bitmap.wrapHardwareBuffer(taskSnapshot.getHardwareBuffer(), taskSnapshot.getColorSpace());
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void setRecentsAppBehindSystemBars(boolean behindSystemBars) {
        if (!checkCallingPermission("android.permission.START_TASKS_FROM_RECENTS", "setRecentsAppBehindSystemBars()")) {
            throw new java.lang.SecurityException("Requires START_TASKS_FROM_RECENTS permission");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.Task recentsApp = this.mRoot.getTask(new java.util.function.Predicate() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda35
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.wm.WindowManagerService.lambda$setRecentsAppBehindSystemBars$27((com.android.server.wm.Task) obj);
                        }
                    });
                    if (recentsApp != null) {
                        recentsApp.getTask().setCanAffectSystemUiFlags(behindSystemBars);
                        this.mWindowPlacerLocked.requestTraversal();
                    }
                    com.android.server.inputmethod.InputMethodManagerInternal.get().maybeFinishStylusHandwriting();
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    static /* synthetic */ boolean lambda$setRecentsAppBehindSystemBars$27(com.android.server.wm.Task task) {
        return task.isActivityTypeHomeOrRecents() && task.getTopVisibleActivity() != null;
    }

    public int getLetterboxBackgroundColorInArgb() {
        return this.mLetterboxConfiguration.getLetterboxBackgroundColor().toArgb();
    }

    public boolean isLetterboxBackgroundMultiColored() {
        int letterboxBackgroundType = this.mLetterboxConfiguration.getLetterboxBackgroundType();
        switch (letterboxBackgroundType) {
            case 0:
                return false;
            case 1:
            case 2:
            case 3:
                return true;
            default:
                throw new java.lang.AssertionError("Unexpected letterbox background type: " + letterboxBackgroundType);
        }
    }

    public void captureDisplay(int displayId, android.window.ScreenCapture.CaptureArgs captureArgs, android.window.ScreenCapture.ScreenCaptureListener listener) {
        android.util.Slog.d(TAG, "captureDisplay");
        if (!checkCallingPermission("android.permission.READ_FRAME_BUFFER", "captureDisplay()")) {
            throw new java.lang.SecurityException("Requires READ_FRAME_BUFFER permission");
        }
        android.window.ScreenCapture.LayerCaptureArgs layerCaptureArgs = getCaptureArgs(displayId, captureArgs);
        android.window.ScreenCapture.captureLayers(layerCaptureArgs, listener);
        if (android.os.Binder.getCallingUid() != 1000) {
            layerCaptureArgs.release();
        }
    }

    android.window.ScreenCapture.LayerCaptureArgs getCaptureArgs(int displayId, android.window.ScreenCapture.CaptureArgs captureArgs) {
        android.view.SurfaceControl displaySurfaceControl;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    throw new java.lang.IllegalArgumentException("Trying to screenshot and invalid display: " + displayId);
                }
                displaySurfaceControl = displayContent.getSurfaceControl();
                if (captureArgs == null) {
                    captureArgs = new android.window.ScreenCapture.CaptureArgs.Builder().build();
                }
                if (captureArgs.mSourceCrop.isEmpty()) {
                    displayContent.getBounds(this.mTmpRect);
                    this.mTmpRect.offsetTo(0, 0);
                } else {
                    this.mTmpRect.set(captureArgs.mSourceCrop);
                }
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        return new android.window.ScreenCapture.LayerCaptureArgs.Builder(displaySurfaceControl, captureArgs).setSourceCrop(this.mTmpRect).build();
    }

    public boolean isGlobalKey(int keyCode) {
        return this.mPolicy.isGlobalKey(keyCode);
    }

    private int sanitizeWindowType(com.android.server.wm.Session session, int displayId, android.os.IBinder windowToken, int type) {
        boolean isTypeValid;
        if (type == 2032 && windowToken != null) {
            com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
            com.android.server.wm.WindowToken token = displayContent.getWindowToken(windowToken);
            if (token != null && type == token.getWindowType()) {
                isTypeValid = true;
            } else {
                isTypeValid = false;
            }
        } else if (!session.mCanAddInternalSystemWindow && type != 0) {
            android.util.Slog.w(TAG, "Requires INTERNAL_SYSTEM_WINDOW permission if assign type to input. New type will be 0.");
            isTypeValid = false;
        } else {
            isTypeValid = true;
        }
        if (!isTypeValid) {
            return 0;
        }
        return type;
    }

    public boolean addToSurfaceSyncGroup(android.os.IBinder syncGroupToken, boolean parentSyncGroupMerge, android.window.ISurfaceSyncGroupCompletedListener completedListener, android.window.AddToSurfaceSyncGroupResult outAddToSyncGroupResult) {
        return this.mSurfaceSyncGroupController.addToSyncGroup(syncGroupToken, parentSyncGroupMerge, completedListener, outAddToSyncGroupResult);
    }

    public void markSurfaceSyncGroupReady(android.os.IBinder syncGroupToken) {
        this.mSurfaceSyncGroupController.markSyncGroupReady(syncGroupToken);
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs
    public java.util.List<android.content.ComponentName> notifyScreenshotListeners(int displayId) {
        if (!checkCallingPermission("android.permission.STATUS_BAR_SERVICE", "notifyScreenshotListeners()")) {
            throw new java.lang.SecurityException("Requires STATUS_BAR_SERVICE permission");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent displayContent = this.mRoot.getDisplayContent(displayId);
                if (displayContent == null) {
                    java.util.ArrayList arrayList = new java.util.ArrayList();
                    resetPriorityAfterLockedSection();
                    return arrayList;
                }
                final android.util.ArraySet<android.content.ComponentName> notifiedApps = new android.util.ArraySet<>();
                displayContent.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda29
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wm.WindowManagerService.lambda$notifyScreenshotListeners$28(notifiedApps, (com.android.server.wm.ActivityRecord) obj);
                    }
                }, true);
                java.util.List<android.content.ComponentName> listCopyOf = java.util.List.copyOf(notifiedApps);
                resetPriorityAfterLockedSection();
                return listCopyOf;
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    static /* synthetic */ void lambda$notifyScreenshotListeners$28(android.util.ArraySet notifiedApps, com.android.server.wm.ActivityRecord ar) {
        if (!notifiedApps.contains(ar.mActivityComponent) && ar.isVisible() && ar.isRegisteredForScreenCaptureCallback()) {
            ar.reportScreenCaptured();
            notifiedApps.add(ar.mActivityComponent);
        }
    }

    /* JADX WARN: Finally extract failed */
    public boolean replaceContentOnDisplay(int displayId, android.view.SurfaceControl sc) {
        if (!checkCallingPermission("android.permission.ACCESS_SURFACE_FLINGER", "replaceDisplayContent()")) {
            throw new java.lang.SecurityException("Requires ACCESS_SURFACE_FLINGER permission");
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.DisplayContent dc = this.mRoot.getDisplayContentOrCreate(displayId);
                    if (dc != null) {
                        dc.replaceContent(sc);
                        resetPriorityAfterLockedSection();
                        android.os.Binder.restoreCallingIdentity(origId);
                        return true;
                    }
                    resetPriorityAfterLockedSection();
                    android.os.Binder.restoreCallingIdentity(origId);
                    return false;
                } catch (java.lang.Throwable th) {
                    resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } catch (java.lang.Throwable th2) {
            android.os.Binder.restoreCallingIdentity(origId);
            throw th2;
        }
    }

    public void registerTrustedPresentationListener(android.os.IBinder window, android.window.ITrustedPresentationListener listener, android.window.TrustedPresentationThresholds thresholds, int id) {
        this.mTrustedPresentationListenerController.registerListener(window, listener, thresholds, id);
    }

    public void unregisterTrustedPresentationListener(android.window.ITrustedPresentationListener listener, int id) {
        this.mTrustedPresentationListenerController.unregisterListener(listener, id);
    }

    public boolean registerScreenRecordingCallback(android.window.IScreenRecordingCallback callback) {
        registerScreenRecordingCallback_enforcePermission();
        return this.mScreenRecordingCallbackController.register(callback);
    }

    public void unregisterScreenRecordingCallback(android.window.IScreenRecordingCallback callback) {
        unregisterScreenRecordingCallback_enforcePermission();
        this.mScreenRecordingCallbackController.unregister(callback);
    }

    void onProcessActivityVisibilityChanged(int uid, boolean visible) {
        this.mScreenRecordingCallbackController.onProcessActivityVisibilityChanged(uid, visible);
    }

    public void setGlobalDragListener(android.window.IGlobalDragListener listener) throws android.os.RemoteException {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("setUnhandledDragListener");
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mDragDropController.setGlobalDragListener(listener);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
    }

    boolean getDisableSecureWindows() {
        return this.mDisableSecureWindows;
    }

    private void onWindowVisible(com.android.server.wm.WindowState w) {
        showToastIfBlockingScreenCapture(w);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showToastIfBlockingScreenCapture(com.android.server.wm.WindowState w) {
        int uid = w.getOwningUid();
        if (!this.mCaptureBlockedToastShownUids.contains(uid) && this.mSensitiveContentPackages.shouldBlockScreenCaptureForApp(w.getOwningPackage(), uid, w.getWindowToken())) {
            this.mCaptureBlockedToastShownUids.add(uid);
            this.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$showToastIfBlockingScreenCapture$29();
                }
            });
            if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.sensitiveContentMetricsBugfix() && this.mSensitiveContentPackages.shouldBlockScreenCaptureForApp(w.getOwningPackage(), uid, null)) {
                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SENSITIVE_NOTIFICATION_APP_PROTECTION_APPLIED, this.mSensitiveContentProtectionSessionId, uid);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showToastIfBlockingScreenCapture$29() {
        android.widget.Toast.makeText(this.mContext, android.os.Looper.getMainLooper(), this.mContext.getString(android.R.string.roamingText7), 0).show();
    }

    public com.android.server.wm.IWindowManagerServiceWrapper getWrapper() {
        return this.mWrapper;
    }

    private class WindowManagerServiceWrapper implements com.android.server.wm.IWindowManagerServiceWrapper {
        private WindowManagerServiceWrapper() {
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public com.android.server.wm.WindowState getFocusedWindow() {
            return com.android.server.wm.WindowManagerService.this.getFocusedWindow();
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public int getForcedDisplayDensityForUserLocked(int userId) {
            return com.android.server.wm.WindowManagerService.this.getForcedDisplayDensityForUserLocked(userId);
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public void updateAppOpsState() {
            com.android.server.wm.WindowManagerService.this.updateAppOpsState();
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public boolean dumpWindows(java.io.PrintWriter pw, java.lang.String name, boolean dumpAll) {
            return com.android.server.wm.WindowManagerService.this.dumpWindows(pw, name, dumpAll);
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public void setWindowAnimationScaleSetting(float value) {
            com.android.server.wm.WindowManagerService.this.mWindowAnimationScaleSetting = value;
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public void setTransitionAnimationScaleSetting(float value) {
            com.android.server.wm.WindowManagerService.this.mTransitionAnimationScaleSetting = value;
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public com.android.server.wm.IWindowManagerServiceExt getExtImpl() {
            return com.android.server.wm.WindowManagerService.this.mWindowManagerServiceExt;
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public void transferTouchFocus(android.os.IBinder fromChannelToken, android.os.IBinder toChannelToken) {
            com.android.server.wm.WindowManagerService.this.mInputManager.transferTouchGesture(fromChannelToken, toChannelToken);
        }

        @Override // com.android.server.wm.IWindowManagerServiceWrapper
        public com.android.internal.protolog.common.IProtoLog getSingleInstance() {
            return com.android.internal.protolog.ProtoLogImpl_209941506.getSingleInstance();
        }
    }

    private static boolean getShellTransitEnabled() {
        android.content.pm.FeatureInfo autoFeature = com.android.server.SystemConfig.getInstance().getAvailableFeatures().get("android.hardware.type.automotive");
        if (autoFeature == null || autoFeature.version < 0) {
            return true;
        }
        return android.os.SystemProperties.getBoolean(ENABLE_SHELL_TRANSITIONS, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpVisibleWindowClients(final java.io.FileDescriptor fd, final java.io.PrintWriter pw, final long timeout) {
        final java.util.ArrayList<com.android.server.wm.WindowState> systemWindows = new java.util.ArrayList<>();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
        boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRoot.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda10
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wm.WindowManagerService.lambda$dumpVisibleWindowClients$30(systemWindows, (com.android.server.wm.WindowState) obj);
                    }
                }, false);
            } catch (java.lang.Throwable th) {
                resetPriorityAfterLockedSection();
                throw th;
            }
        }
        resetPriorityAfterLockedSection();
        systemWindows.forEach(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerService$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.WindowManagerService.lambda$dumpVisibleWindowClients$31(pw, fd, timeout, (com.android.server.wm.WindowState) obj);
            }
        });
    }

    static /* synthetic */ void lambda$dumpVisibleWindowClients$30(java.util.ArrayList systemWindows, com.android.server.wm.WindowState w) {
        if (!w.isActivityWindow() && w.isVisibleNow()) {
            systemWindows.add(w);
        }
    }

    static /* synthetic */ void lambda$dumpVisibleWindowClients$31(java.io.PrintWriter pw, java.io.FileDescriptor fd, long timeout, com.android.server.wm.WindowState w) {
        pw.println("---------------------------------");
        pw.println(w.toString());
        pw.flush();
        try {
            com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
            try {
                w.mClient.dumpWindow(tp.getWriteFd());
                tp.go(fd, timeout);
                tp.close();
            } catch (java.lang.Throwable th) {
                try {
                    tp.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (android.os.RemoteException e) {
            pw.println("Got a RemoteException while dumping the window");
        } catch (java.io.IOException e2) {
            pw.println("Failure while dumping the window: " + e2);
        }
    }
}
