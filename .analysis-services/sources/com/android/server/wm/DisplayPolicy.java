package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class DisplayPolicy {
    static final int ANIMATION_NONE = -1;
    static final int ANIMATION_STYLEABLE = 0;
    private static final int INSETS_OVERRIDE_INDEX_INVALID = -1;
    private static final int MSG_DISABLE_POINTER_LOCATION = 5;
    private static final int MSG_ENABLE_POINTER_LOCATION = 4;
    private static final int NAV_BAR_FORCE_TRANSPARENT = 2;
    private static final int NAV_BAR_OPAQUE_WHEN_FREEFORM_OR_DOCKED = 0;
    private static final int NAV_BAR_TRANSLUCENT_WHEN_FREEFORM_OPAQUE_OTHERWISE = 1;
    private static final long PANIC_GESTURE_EXPIRATION = 30000;
    private static final java.lang.String TAG = "WindowManager";
    private final android.view.accessibility.AccessibilityManager mAccessibilityManager;
    private boolean mAllowLockscreenWhenOn;
    private final com.android.server.wm.WindowManagerInternal.AppTransitionListener mAppTransitionListener;
    private volatile boolean mAwake;
    private int mBottomGestureAdditionalInset;
    private com.android.server.wm.WindowState mBottomGestureHost;
    private com.android.server.wm.DisplayPolicy.DecorInsets.Cache mCachedDecorInsets;
    private boolean mCanSystemBarsBeShownByUser;
    private final boolean mCarDockEnablesAccelerometer;
    private final android.content.Context mContext;
    private android.content.res.Resources mCurrentUserResources;
    final com.android.server.wm.DisplayPolicy.DecorInsets mDecorInsets;
    private final boolean mDeskDockEnablesAccelerometer;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private boolean mDreamingLockscreen;
    private java.lang.String mFocusedApp;
    private com.android.server.wm.WindowState mFocusedWindow;
    private final com.android.internal.policy.ForceShowNavBarSettingsObserver mForceShowNavBarSettingsObserver;
    private boolean mForceShowNavigationBarEnabled;
    private int mForciblyShownTypes;
    private final com.android.internal.policy.GestureNavigationSettingsObserver mGestureNavigationSettingsObserver;
    private final android.os.Handler mHandler;
    private volatile boolean mHasNavigationBar;
    private volatile boolean mHasStatusBar;
    private volatile boolean mHdmiPlugged;
    private boolean mImeInsetsConsumed;
    private boolean mImmersiveConfirmationWindowExists;
    private final com.android.server.wm.ImmersiveModeConfirmation mImmersiveModeConfirmation;
    private boolean mIsFreeformWindowOverlappingWithNavBar;
    private boolean mIsImmersiveMode;
    private volatile boolean mKeyguardDrawComplete;
    private int mLastAppearance;
    private int mLastBehavior;
    private int mLastDisableFlags;
    private com.android.server.wm.WindowState mLastFocusedWindow;
    private com.android.internal.statusbar.LetterboxDetails[] mLastLetterboxDetails;
    private boolean mLastShowingDream;
    private com.android.internal.view.AppearanceRegion[] mLastStatusBarAppearanceRegions;
    private com.android.server.wm.WindowState mLeftGestureHost;
    private int mLeftGestureInset;
    private final java.lang.Object mLock;
    private com.android.server.wm.WindowState mNavBarBackgroundWindowCandidate;
    private com.android.server.wm.WindowState mNavBarColorWindowCandidate;
    private volatile boolean mNavigationBarAlwaysShowOnSideGesture;
    private volatile boolean mNavigationBarCanMove;
    private volatile com.android.server.wm.WindowState mNotificationShade;
    private final long mPanicThresholdMs;
    private long mPanicTime;
    private long mPendingPanicGestureUptime;
    private volatile boolean mPersistentVrModeEnabled;
    private com.android.internal.widget.PointerLocationView mPointerLocationView;
    private com.android.server.wm.RefreshRatePolicy mRefreshRatePolicy;
    private boolean mRemoteInsetsControllerControlsSystemBars;
    private com.android.server.wm.WindowState mRightGestureHost;
    private int mRightGestureInset;
    private volatile boolean mScreenOnEarly;
    private volatile boolean mScreenOnFully;
    private volatile com.android.server.policy.WindowManagerPolicy.ScreenOnListener mScreenOnListener;
    private final com.android.internal.util.ScreenshotHelper mScreenshotHelper;
    private final com.android.server.wm.WindowManagerService mService;
    private boolean mShouldAttachNavBarToAppDuringTransition;
    private boolean mShowingDream;
    private com.android.server.statusbar.StatusBarManagerInternal mStatusBarManagerInternal;
    private com.android.server.wm.SystemGesturesPointerEventListener mSystemGestures;
    private com.android.server.wm.WindowState mSystemUiControllingWindow;
    private com.android.server.wm.WindowState mTopFullscreenOpaqueWindowState;
    private com.android.server.wm.WindowState mTopGestureHost;
    private boolean mTopIsFullscreen;
    private final android.content.Context mUiContext;
    private volatile boolean mWindowManagerDrawComplete;
    private static final int SHOW_TYPES_FOR_SWIPE = android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars();
    private static final int SHOW_TYPES_FOR_PANIC = android.view.WindowInsets.Type.navigationBars();
    private static final boolean USE_CACHED_INSETS_FOR_DISPLAY_SWITCH = android.os.SystemProperties.getBoolean("persist.wm.debug.cached_insets_switch", true);
    private static final android.graphics.Rect sTmpRect = new android.graphics.Rect();
    private static final android.graphics.Rect sTmpRect2 = new android.graphics.Rect();
    private static final android.graphics.Rect sTmpDisplayCutoutSafe = new android.graphics.Rect();
    private static final android.window.ClientWindowFrames sTmpClientFrames = new android.window.ClientWindowFrames();
    public com.android.server.zenmode.IZenModeManagerExt mZenModeManagerExt = (com.android.server.zenmode.IZenModeManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.zenmode.IZenModeManagerExt.class).create();
    private final java.lang.Object mServiceAcquireLock = new java.lang.Object();
    private volatile int mLidState = -1;
    private volatile int mDockMode = 0;
    private com.android.server.wm.WindowState mStatusBar = null;
    private com.android.server.wm.WindowState mNavigationBar = null;
    private int mNavigationBarPosition = 4;
    private final android.util.ArraySet<com.android.server.wm.WindowState> mInsetsSourceWindowsExceptIme = new android.util.ArraySet<>();
    private final android.util.ArraySet<com.android.server.wm.ActivityRecord> mSystemBarColorApps = new android.util.ArraySet<>();
    private final android.util.ArraySet<com.android.server.wm.ActivityRecord> mRelaunchingSystemBarColorApps = new android.util.ArraySet<>();
    private final java.util.ArrayList<com.android.internal.view.AppearanceRegion> mStatusBarAppearanceRegionList = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.WindowState> mStatusBarBackgroundWindows = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.internal.statusbar.LetterboxDetails> mLetterboxDetails = new java.util.ArrayList<>();
    private int mLastRequestedVisibleTypes = android.view.WindowInsets.Type.defaultVisible();
    private final android.graphics.Rect mStatusBarColorCheckedBounds = new android.graphics.Rect();
    private final android.graphics.Rect mStatusBarBackgroundCheckedBounds = new android.graphics.Rect();
    private boolean mLastFocusIsFullscreen = false;
    private final android.view.WindowLayout mWindowLayout = new android.view.WindowLayout();
    private int mNavBarOpacityMode = 0;
    private final java.lang.Runnable mHiddenNavPanic = new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy.3
        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.wm.DisplayPolicy.this.mLock) {
                if (com.android.server.wm.DisplayPolicy.this.mService.mPolicy.isUserSetupComplete()) {
                    com.android.server.wm.DisplayPolicy.this.mPendingPanicGestureUptime = android.os.SystemClock.uptimeMillis();
                    com.android.server.wm.DisplayPolicy.this.updateSystemBarAttributes();
                }
            }
        }
    };
    private com.android.server.wm.DisplayPolicy.DisplayPolicyWrapper mDisplayPolicyWrapper = new com.android.server.wm.DisplayPolicy.DisplayPolicyWrapper();

    com.android.server.statusbar.StatusBarManagerInternal getStatusBarManagerInternal() {
        com.android.server.statusbar.StatusBarManagerInternal statusBarManagerInternal;
        synchronized (this.mServiceAcquireLock) {
            if (this.mStatusBarManagerInternal == null) {
                this.mStatusBarManagerInternal = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
            }
            statusBarManagerInternal = this.mStatusBarManagerInternal;
        }
        return statusBarManagerInternal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class PolicyHandler extends android.os.Handler {
        PolicyHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 4:
                    com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getExtImpl().getOplusUIHandler(this).post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$PolicyHandler$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$handleMessage$0();
                        }
                    });
                    break;
                case 5:
                    com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getExtImpl().getOplusUIHandler(this).post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$PolicyHandler$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$handleMessage$1();
                        }
                    });
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$0() {
            com.android.server.wm.DisplayPolicy.this.enablePointerLocation();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$1() {
            com.android.server.wm.DisplayPolicy.this.disablePointerLocation();
        }
    }

    DisplayPolicy(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent) {
        this.mService = service;
        this.mContext = displayContent.isDefaultDisplay ? service.mContext : service.mContext.createDisplayContext(displayContent.getDisplay());
        this.mUiContext = displayContent.isDefaultDisplay ? service.mAtmService.getUiContext() : service.mAtmService.mSystemThread.getSystemUiContext(displayContent.getDisplayId());
        this.mDisplayContent = displayContent;
        this.mDecorInsets = new com.android.server.wm.DisplayPolicy.DecorInsets(displayContent);
        this.mLock = service.getWindowManagerLock();
        int displayId = displayContent.getDisplayId();
        android.content.res.Resources r = this.mContext.getResources();
        this.mCarDockEnablesAccelerometer = r.getBoolean(android.R.bool.config_carrier_volte_available);
        this.mDeskDockEnablesAccelerometer = r.getBoolean(android.R.bool.config_deviceSupportsHighPerfTransitions);
        this.mCanSystemBarsBeShownByUser = !r.getBoolean(android.R.bool.config_remoteInsetsControllerControlsSystemBars) || r.getBoolean(android.R.bool.config_omnipresentCommunalUser);
        this.mPanicThresholdMs = r.getInteger(android.R.integer.config_externalDisplayPeakHeight);
        this.mAccessibilityManager = (android.view.accessibility.AccessibilityManager) this.mContext.getSystemService("accessibility");
        if (!displayContent.isDefaultDisplay) {
            this.mAwake = true;
            this.mScreenOnEarly = true;
            this.mScreenOnFully = true;
        }
        this.mDisplayPolicyWrapper.getSocExtImpl().loadConfig();
        android.os.Looper looper = com.android.server.UiThread.getHandler().getLooper();
        this.mDisplayPolicyWrapper.getExtImpl().initOplusDisplayPolicy(this);
        this.mHandler = this.mDisplayPolicyWrapper.getExtImpl().createPolicyHandler(looper, new com.android.server.wm.DisplayPolicy.PolicyHandler(looper));
        this.mDisplayPolicyWrapper.getExtImpl().initOplusDisplayPolicyEx(service, this);
        if (!android.view.ViewRootImpl.CLIENT_TRANSIENT) {
            com.android.server.wm.SystemGesturesPointerEventListener.Callbacks gesturesPointerEventCallbacks = new com.android.server.wm.DisplayPolicy.AnonymousClass1(displayId);
            this.mSystemGestures = new com.android.server.wm.SystemGesturesPointerEventListener(this.mUiContext, this.mHandler, gesturesPointerEventCallbacks);
            displayContent.registerPointerEventListener(this.mSystemGestures);
        }
        this.mAppTransitionListener = new com.android.server.wm.DisplayPolicy.AnonymousClass2(displayId);
        displayContent.mAppTransition.registerListenerLocked(this.mAppTransitionListener);
        displayContent.mTransitionController.registerLegacyListener(this.mAppTransitionListener);
        if (android.view.ViewRootImpl.CLIENT_TRANSIENT || android.view.ViewRootImpl.CLIENT_IMMERSIVE_CONFIRMATION) {
            this.mImmersiveModeConfirmation = null;
        } else {
            this.mImmersiveModeConfirmation = new com.android.server.wm.ImmersiveModeConfirmation(this.mContext, looper, this.mService.mVrModeEnabled, this.mCanSystemBarsBeShownByUser);
        }
        this.mScreenshotHelper = displayContent.isDefaultDisplay ? new com.android.internal.util.ScreenshotHelper(this.mContext) : null;
        if (this.mDisplayContent.isDefaultDisplay) {
            this.mHasStatusBar = true;
            this.mHasNavigationBar = this.mContext.getResources().getBoolean(android.R.bool.config_restartRadioAfterProvisioning);
            java.lang.String navBarOverride = android.os.SystemProperties.get("qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                this.mHasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                this.mHasNavigationBar = true;
            }
        } else {
            this.mHasStatusBar = false;
            this.mHasNavigationBar = this.mDisplayContent.supportsSystemDecorations();
        }
        this.mRefreshRatePolicy = new com.android.server.wm.RefreshRatePolicy(this.mService, this.mDisplayContent.getDisplayInfo(), this.mService.mHighRefreshRateDenylist);
        this.mGestureNavigationSettingsObserver = new com.android.internal.policy.GestureNavigationSettingsObserver(this.mHandler, com.android.internal.os.BackgroundThread.getHandler(), this.mContext, new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        });
        android.os.Handler handler = this.mHandler;
        final com.android.internal.policy.GestureNavigationSettingsObserver gestureNavigationSettingsObserver = this.mGestureNavigationSettingsObserver;
        java.util.Objects.requireNonNull(gestureNavigationSettingsObserver);
        handler.post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                gestureNavigationSettingsObserver.register();
            }
        });
        this.mForceShowNavBarSettingsObserver = new com.android.internal.policy.ForceShowNavBarSettingsObserver(this.mHandler, this.mContext);
        this.mForceShowNavBarSettingsObserver.setOnChangeRunnable(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.updateForceShowNavBarSettings();
            }
        });
        this.mForceShowNavigationBarEnabled = this.mForceShowNavBarSettingsObserver.isEnabled();
        android.os.Handler handler2 = this.mHandler;
        final com.android.internal.policy.ForceShowNavBarSettingsObserver forceShowNavBarSettingsObserver = this.mForceShowNavBarSettingsObserver;
        java.util.Objects.requireNonNull(forceShowNavBarSettingsObserver);
        handler2.post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                forceShowNavBarSettingsObserver.register();
            }
        });
    }

    /* JADX INFO: renamed from: com.android.server.wm.DisplayPolicy$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.wm.SystemGesturesPointerEventListener.Callbacks {
        private static final long MOUSE_GESTURE_DELAY_MS = 500;
        final /* synthetic */ int val$displayId;
        private java.lang.Runnable mOnSwipeFromLeft = new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.onSwipeFromLeft();
            }
        };
        private java.lang.Runnable mOnSwipeFromTop = new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.onSwipeFromTop();
            }
        };
        private java.lang.Runnable mOnSwipeFromRight = new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.onSwipeFromRight();
            }
        };
        private java.lang.Runnable mOnSwipeFromBottom = new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.onSwipeFromBottom();
            }
        };

        AnonymousClass1(int i) {
            this.val$displayId = i;
        }

        private android.graphics.Insets getControllableInsets(com.android.server.wm.WindowState win) {
            if (win == null) {
                return android.graphics.Insets.NONE;
            }
            com.android.server.wm.InsetsSourceProvider provider = win.getControllableInsetProvider();
            if (provider == null) {
                return android.graphics.Insets.NONE;
            }
            return provider.getSource().calculateInsets(win.getBounds(), true);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onSwipeFromTop() {
            synchronized (com.android.server.wm.DisplayPolicy.this.mLock) {
                com.android.server.wm.DisplayPolicy.this.requestTransientBars(com.android.server.wm.DisplayPolicy.this.mTopGestureHost, getControllableInsets(com.android.server.wm.DisplayPolicy.this.mTopGestureHost).top > 0);
            }
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onSwipeFromBottom() {
            synchronized (com.android.server.wm.DisplayPolicy.this.mLock) {
                com.android.server.wm.DisplayPolicy.this.requestTransientBars(com.android.server.wm.DisplayPolicy.this.mBottomGestureHost, getControllableInsets(com.android.server.wm.DisplayPolicy.this.mBottomGestureHost).bottom > 0);
            }
            com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getExtImpl().handleSwipeUpFromBottom();
        }

        private boolean allowsSideSwipe(android.graphics.Region excludedRegion) {
            return (!com.android.server.wm.DisplayPolicy.this.mNavigationBarAlwaysShowOnSideGesture || com.android.server.wm.DisplayPolicy.this.mSystemGestures.currentGestureStartedInRegion(excludedRegion) || com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getExtImpl().isInPocketStudio(this.val$displayId)) ? false : true;
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onSwipeFromRight() {
            android.graphics.Region excludedRegion = android.graphics.Region.obtain();
            synchronized (com.android.server.wm.DisplayPolicy.this.mLock) {
                com.android.server.wm.DisplayPolicy.this.mDisplayContent.calculateSystemGestureExclusion(excludedRegion, null);
                boolean hasWindow = getControllableInsets(com.android.server.wm.DisplayPolicy.this.mRightGestureHost).right > 0;
                if (hasWindow || allowsSideSwipe(excludedRegion)) {
                    com.android.server.wm.DisplayPolicy.this.requestTransientBars(com.android.server.wm.DisplayPolicy.this.mRightGestureHost, hasWindow);
                }
            }
            excludedRegion.recycle();
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onSwipeFromLeft() {
            android.graphics.Region excludedRegion = android.graphics.Region.obtain();
            synchronized (com.android.server.wm.DisplayPolicy.this.mLock) {
                com.android.server.wm.DisplayPolicy.this.mDisplayContent.calculateSystemGestureExclusion(excludedRegion, null);
                boolean hasWindow = getControllableInsets(com.android.server.wm.DisplayPolicy.this.mLeftGestureHost).left > 0;
                if (hasWindow || allowsSideSwipe(excludedRegion)) {
                    com.android.server.wm.DisplayPolicy.this.requestTransientBars(com.android.server.wm.DisplayPolicy.this.mLeftGestureHost, hasWindow);
                }
                ((com.android.server.wm.IZoomWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IZoomWindowManagerExt.class).create()).gestureSwipeFromBottom();
            }
            excludedRegion.recycle();
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onFling(int duration) {
            if (com.android.server.wm.DisplayPolicy.this.mService.mPowerManagerInternal != null) {
                com.android.server.wm.DisplayPolicy.this.mService.mPowerManagerInternal.setPowerBoost(0, duration);
            }
            if (duration > 1000) {
                com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getExtImpl().pokeDynamicVsyncAnimation(duration + 1000, "OnFling");
            }
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onVerticalFling(final int duration) {
            if (com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getSocExtImpl().isSupportPerfBoost()) {
                com.android.server.OplusIoThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onVerticalFling$0(duration);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onVerticalFling$0(int duration) {
            com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getSocExtImpl().hookOnVerticalFling(duration);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onHorizontalFling(final int duration) {
            if (com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getSocExtImpl().isSupportPerfBoost()) {
                com.android.server.OplusIoThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onHorizontalFling$1(duration);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHorizontalFling$1(int duration) {
            com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getSocExtImpl().hookOnHorizontalFling(duration);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onScroll(final boolean started) {
            if (com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getSocExtImpl().isSupportPerfBoost()) {
                com.android.server.OplusIoThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onScroll$2(started);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onScroll$2(boolean started) {
            com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getSocExtImpl().hookOnScroll(started);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onDebug() {
        }

        private com.android.server.wm.WindowOrientationListener getOrientationListener() {
            com.android.server.wm.DisplayRotation rotation = com.android.server.wm.DisplayPolicy.this.mDisplayContent.getDisplayRotation();
            if (rotation != null) {
                return rotation.getOrientationListener();
            }
            return null;
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onDown() {
            com.android.server.wm.WindowOrientationListener listener = getOrientationListener();
            if (listener != null) {
                listener.onTouchStart();
            }
            com.android.server.wm.DisplayPolicy.this.mDisplayPolicyWrapper.getSocExtImpl().hookOnDown();
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onUpOrCancel() {
            com.android.server.wm.WindowOrientationListener listener = getOrientationListener();
            if (listener != null) {
                listener.onTouchEnd();
            }
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseHoverAtLeft() {
            com.android.server.wm.DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromLeft);
            com.android.server.wm.DisplayPolicy.this.mHandler.postDelayed(this.mOnSwipeFromLeft, 500L);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseHoverAtTop() {
            com.android.server.wm.DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromTop);
            com.android.server.wm.DisplayPolicy.this.mHandler.postDelayed(this.mOnSwipeFromTop, 500L);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseHoverAtRight() {
            com.android.server.wm.DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromRight);
            com.android.server.wm.DisplayPolicy.this.mHandler.postDelayed(this.mOnSwipeFromRight, 500L);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseHoverAtBottom() {
            com.android.server.wm.DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromBottom);
            com.android.server.wm.DisplayPolicy.this.mHandler.postDelayed(this.mOnSwipeFromBottom, 500L);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseLeaveFromLeft() {
            com.android.server.wm.DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromLeft);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseLeaveFromTop() {
            com.android.server.wm.DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromTop);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseLeaveFromRight() {
            com.android.server.wm.DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromRight);
        }

        @Override // com.android.server.wm.SystemGesturesPointerEventListener.Callbacks
        public void onMouseLeaveFromBottom() {
            com.android.server.wm.DisplayPolicy.this.mHandler.removeCallbacks(this.mOnSwipeFromBottom);
        }
    }

    /* JADX INFO: renamed from: com.android.server.wm.DisplayPolicy$2, reason: invalid class name */
    class AnonymousClass2 extends com.android.server.wm.WindowManagerInternal.AppTransitionListener {
        private java.lang.Runnable mAppTransitionCancelled;
        private java.lang.Runnable mAppTransitionFinished;
        private java.lang.Runnable mAppTransitionPending;
        final /* synthetic */ int val$displayId;

        AnonymousClass2(int i) {
            this.val$displayId = i;
            final int i2 = this.val$displayId;
            this.mAppTransitionPending = new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$$0(i2);
                }
            };
            final int i3 = this.val$displayId;
            this.mAppTransitionCancelled = new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$2$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$$1(i3);
                }
            };
            final int i4 = this.val$displayId;
            this.mAppTransitionFinished = new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$2$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$$2(i4);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$$0(int displayId) {
            com.android.server.statusbar.StatusBarManagerInternal statusBar = com.android.server.wm.DisplayPolicy.this.getStatusBarManagerInternal();
            if (statusBar != null) {
                statusBar.appTransitionPending(displayId);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$$1(int displayId) {
            com.android.server.statusbar.StatusBarManagerInternal statusBar = com.android.server.wm.DisplayPolicy.this.getStatusBarManagerInternal();
            if (statusBar != null) {
                statusBar.appTransitionCancelled(displayId);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$$2(int displayId) {
            com.android.server.statusbar.StatusBarManagerInternal statusBar = com.android.server.wm.DisplayPolicy.this.getStatusBarManagerInternal();
            if (statusBar != null) {
                statusBar.appTransitionFinished(displayId);
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionPendingLocked() {
            com.android.server.wm.DisplayPolicy.this.mHandler.post(this.mAppTransitionPending);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public int onAppTransitionStartingLocked(final long statusBarAnimationStartTime, final long statusBarAnimationDuration) {
            com.android.server.wm.DisplayPolicy.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onAppTransitionStartingLocked$3(statusBarAnimationStartTime, statusBarAnimationDuration);
                }
            });
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAppTransitionStartingLocked$3(long statusBarAnimationStartTime, long statusBarAnimationDuration) {
            com.android.server.statusbar.StatusBarManagerInternal statusBar = com.android.server.wm.DisplayPolicy.this.getStatusBarManagerInternal();
            if (statusBar != null) {
                statusBar.appTransitionStarting(com.android.server.wm.DisplayPolicy.this.mContext.getDisplayId(), statusBarAnimationStartTime, statusBarAnimationDuration);
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionCancelledLocked(boolean keyguardGoingAwayCancelled) {
            com.android.server.wm.DisplayPolicy.this.mHandler.post(this.mAppTransitionCancelled);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionFinishedLocked(android.os.IBinder token) {
            com.android.server.wm.DisplayPolicy.this.mHandler.post(this.mAppTransitionFinished);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        synchronized (this.mLock) {
            onConfigurationChanged();
            if (!android.view.ViewRootImpl.CLIENT_TRANSIENT) {
                this.mSystemGestures.onConfigurationChanged();
            }
            this.mDisplayContent.updateSystemGestureExclusion();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateForceShowNavBarSettings() {
        synchronized (this.mLock) {
            this.mForceShowNavigationBarEnabled = this.mForceShowNavBarSettingsObserver.isEnabled();
            updateSystemBarAttributes();
        }
    }

    void systemReady() {
        if (!android.view.ViewRootImpl.CLIENT_TRANSIENT) {
            this.mSystemGestures.systemReady();
        }
        if (this.mService.mPointerLocationEnabled) {
            setPointerLocationEnabled(true);
        }
    }

    private int getDisplayId() {
        return this.mDisplayContent.getDisplayId();
    }

    public void setHdmiPlugged(boolean plugged) {
        setHdmiPlugged(plugged, false);
    }

    public void setHdmiPlugged(boolean plugged, boolean force) {
        if (force || this.mHdmiPlugged != plugged) {
            this.mHdmiPlugged = plugged;
            this.mService.updateRotation(true, true);
            android.content.Intent intent = new android.content.Intent("android.intent.action.HDMI_PLUGGED");
            intent.addFlags(67108864);
            intent.putExtra("state", plugged);
            this.mContext.sendStickyBroadcastAsUser(intent, android.os.UserHandle.ALL);
        }
    }

    boolean isHdmiPlugged() {
        return this.mHdmiPlugged;
    }

    boolean isCarDockEnablesAccelerometer() {
        return this.mCarDockEnablesAccelerometer;
    }

    boolean isDeskDockEnablesAccelerometer() {
        return this.mDeskDockEnablesAccelerometer;
    }

    public void setPersistentVrModeEnabled(boolean persistentVrModeEnabled) {
        this.mPersistentVrModeEnabled = persistentVrModeEnabled;
    }

    public boolean isPersistentVrModeEnabled() {
        return this.mPersistentVrModeEnabled;
    }

    public void setDockMode(int dockMode) {
        this.mDockMode = dockMode;
    }

    public int getDockMode() {
        return this.mDockMode;
    }

    public boolean hasNavigationBar() {
        return this.mHasNavigationBar;
    }

    public boolean hasStatusBar() {
        return this.mHasStatusBar;
    }

    boolean hasSideGestures() {
        return this.mHasNavigationBar && (this.mLeftGestureInset > 0 || this.mRightGestureInset > 0);
    }

    public boolean navigationBarCanMove() {
        return this.mNavigationBarCanMove;
    }

    public void setLidState(int lidState) {
        this.mLidState = lidState;
    }

    public int getLidState() {
        return this.mLidState;
    }

    private void onDisplaySwitchFinished() {
        this.mDisplayContent.mWallpaperController.onDisplaySwitchFinished();
        this.mDisplayContent.mDisplayUpdater.onDisplaySwitching(false);
    }

    public void setAwake(boolean awake) {
        synchronized (this.mLock) {
            if (awake == this.mAwake) {
                return;
            }
            this.mAwake = awake;
            if (this.mDisplayContent.isDefaultDisplay) {
                if (awake) {
                    this.mService.mAtmService.mVisibleDozeUiProcess = null;
                } else if (this.mScreenOnFully && this.mNotificationShade != null) {
                    this.mService.mAtmService.mVisibleDozeUiProcess = this.mNotificationShade.getProcess();
                }
                this.mService.mAtmService.mKeyguardController.updateDeferTransitionForAod(this.mAwake);
                if (!awake) {
                    onDisplaySwitchFinished();
                    if (!this.mScreenOnEarly && !this.mScreenOnFully && !this.mDisplayContent.isSleeping()) {
                        android.util.Slog.w(TAG, "Late acquire sleep token for " + this.mDisplayContent);
                        this.mService.mRoot.mDisplayOffTokenAcquirer.acquire(this.mDisplayContent.mDisplayId);
                    }
                }
            }
        }
    }

    public boolean isAwake() {
        return this.mAwake;
    }

    public boolean isScreenOnEarly() {
        return this.mScreenOnEarly;
    }

    public boolean isScreenOnFully() {
        return this.mScreenOnFully;
    }

    public boolean isKeyguardDrawComplete() {
        return this.mKeyguardDrawComplete;
    }

    public boolean isWindowManagerDrawComplete() {
        return this.mWindowManagerDrawComplete;
    }

    public boolean isForceShowNavigationBarEnabled() {
        return this.mForceShowNavigationBarEnabled;
    }

    public com.android.server.policy.WindowManagerPolicy.ScreenOnListener getScreenOnListener() {
        return this.mScreenOnListener;
    }

    boolean isRemoteInsetsControllerControllingSystemBars() {
        return this.mRemoteInsetsControllerControlsSystemBars;
    }

    void setRemoteInsetsControllerControlsSystemBars(boolean remoteInsetsControllerControlsSystemBars) {
        this.mRemoteInsetsControllerControlsSystemBars = remoteInsetsControllerControlsSystemBars;
    }

    public void screenTurningOn(com.android.server.policy.WindowManagerPolicy.ScreenOnListener screenOnListener) {
        android.util.Slog.d(TAG, "screenTurnedOn  " + this.mDisplayContent);
        com.android.server.wm.WindowProcessController visibleDozeUiProcess = null;
        synchronized (this.mLock) {
            this.mService.mRoot.mDisplayOffTokenAcquirer.release(this.mDisplayContent.mDisplayId);
            this.mScreenOnEarly = true;
            this.mScreenOnFully = false;
            this.mKeyguardDrawComplete = false;
            this.mWindowManagerDrawComplete = false;
            this.mScreenOnListener = screenOnListener;
            if (!this.mAwake && this.mNotificationShade != null) {
                visibleDozeUiProcess = this.mNotificationShade.getProcess();
                this.mService.mAtmService.mVisibleDozeUiProcess = visibleDozeUiProcess;
            }
        }
        if (visibleDozeUiProcess != null) {
            android.os.Trace.instant(32L, "screenTurnedOnWhileDozing");
            this.mService.mAtmService.setProcessAnimatingWhileDozing(visibleDozeUiProcess);
        }
    }

    public void screenTurnedOn() {
        onDisplaySwitchFinished();
    }

    public void screenTurnedOff(boolean acquireSleepToken) {
        synchronized (this.mLock) {
            if (acquireSleepToken) {
                this.mService.mRoot.mDisplayOffTokenAcquirer.acquire(this.mDisplayContent.mDisplayId);
                this.mScreenOnEarly = false;
                this.mScreenOnFully = false;
                this.mKeyguardDrawComplete = false;
                this.mWindowManagerDrawComplete = false;
                this.mScreenOnListener = null;
                this.mService.mAtmService.mVisibleDozeUiProcess = null;
            } else {
                this.mScreenOnEarly = false;
                this.mScreenOnFully = false;
                this.mKeyguardDrawComplete = false;
                this.mWindowManagerDrawComplete = false;
                this.mScreenOnListener = null;
                this.mService.mAtmService.mVisibleDozeUiProcess = null;
            }
        }
        android.util.Slog.d(TAG, "screenTurnedOff  " + this.mDisplayContent);
    }

    public boolean finishKeyguardDrawn() {
        synchronized (this.mLock) {
            if (this.mScreenOnEarly && !this.mKeyguardDrawComplete) {
                this.mKeyguardDrawComplete = true;
                this.mWindowManagerDrawComplete = false;
                return true;
            }
            android.util.Slog.d(TAG, "finishKeyguardDrawn  mScreenOnEarly:" + this.mScreenOnEarly + "," + this.mDisplayContent);
            return false;
        }
    }

    public boolean finishWindowsDrawn() {
        synchronized (this.mLock) {
            if (this.mScreenOnEarly && !this.mWindowManagerDrawComplete) {
                this.mWindowManagerDrawComplete = true;
                return true;
            }
            android.util.Slog.d(TAG, "finishWindowsDrawn  mScreenOnEarly:" + this.mScreenOnEarly + "," + this.mDisplayContent);
            return false;
        }
    }

    public boolean finishScreenTurningOn() {
        synchronized (this.mLock) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SCREEN_ON_enabled[0]) {
                boolean protoLogParam0 = this.mAwake;
                boolean protoLogParam1 = this.mScreenOnEarly;
                boolean protoLogParam2 = this.mScreenOnFully;
                boolean protoLogParam3 = this.mKeyguardDrawComplete;
                boolean protoLogParam4 = this.mWindowManagerDrawComplete;
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, -6228339285356824882L, 1023, null, java.lang.Boolean.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3), java.lang.Boolean.valueOf(protoLogParam4));
            }
            boolean protoLogParam02 = this.mScreenOnFully;
            if (!protoLogParam02 && this.mScreenOnEarly && this.mWindowManagerDrawComplete && (!this.mAwake || this.mKeyguardDrawComplete || getDisplayId() != 0)) {
                if (!com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON.isLogToLogcat()) {
                    android.util.Slog.i(TAG, "Finished screen turning on..." + this.mDisplayContent.getDisplayId());
                } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SCREEN_ON_enabled[2]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, -6028033043540330282L, 0, null, null);
                }
                this.mScreenOnListener = null;
                this.mScreenOnFully = true;
                this.mDisplayPolicyWrapper.getExtImpl().finishScreenTurningOn();
                return true;
            }
            android.util.Slog.d(TAG, "finishScreenTurningOn  " + this.mScreenOnFully + "," + this.mScreenOnEarly + "," + this.mWindowManagerDrawComplete + "," + this.mDisplayContent);
            return false;
        }
    }

    public void adjustWindowParamsLw(com.android.server.wm.WindowState win, android.view.WindowManager.LayoutParams attrs) {
        switch (attrs.type) {
            case 1:
                if (attrs.isFullscreen() && win.mActivityRecord != null && win.mActivityRecord.fillsParent() && (attrs.privateFlags & 32768) != 0 && attrs.getFitInsetsTypes() != 0) {
                    throw new java.lang.IllegalArgumentException("Illegal attributes: Main window of " + win.mActivityRecord.getName() + " that isn't translucent trying to fit insets. fitInsetsTypes=" + android.view.WindowInsets.Type.toString(attrs.getFitInsetsTypes()));
                }
                break;
            case 2005:
                if (attrs.hideTimeoutMilliseconds < 0 || attrs.hideTimeoutMilliseconds > 4100) {
                    attrs.hideTimeoutMilliseconds = 4100L;
                }
                attrs.hideTimeoutMilliseconds = this.mAccessibilityManager.getRecommendedTimeoutMillis((int) attrs.hideTimeoutMilliseconds, 2);
                attrs.flags |= 16;
                break;
            case 2006:
            case 2015:
                attrs.flags |= 24;
                attrs.flags &= -262145;
                break;
            case 2013:
                attrs.layoutInDisplayCutoutMode = 3;
                break;
        }
        if ((attrs.insetsFlags.appearance & 512) != 0) {
            attrs.insetsFlags.appearance |= 16;
        }
        if (android.view.WindowManager.LayoutParams.isSystemAlertWindowType(attrs.type)) {
            float maxOpacity = this.mService.mMaximumObscuringOpacityForTouch;
            if (attrs.alpha > maxOpacity && (attrs.flags & 16) != 0 && !win.getWrapper().getExtImpl().isOplusTrustedWindow(attrs) && !win.isTrustedOverlay()) {
                android.util.Slog.w(TAG, java.lang.String.format("App %s has a system alert window (type = %d) with FLAG_NOT_TOUCHABLE and LayoutParams.alpha = %.2f > %.2f, setting alpha to %.2f to let touches pass through (if this is isn't desirable, remove flag FLAG_NOT_TOUCHABLE).", attrs.packageName, java.lang.Integer.valueOf(attrs.type), java.lang.Float.valueOf(attrs.alpha), java.lang.Float.valueOf(maxOpacity), java.lang.Float.valueOf(maxOpacity)));
                attrs.alpha = maxOpacity;
                win.mWinAnimator.mAlpha = maxOpacity;
            }
        }
        if (!win.mSession.mCanSetUnrestrictedGestureExclusion) {
            attrs.privateFlags &= -33;
        }
        this.mDisplayPolicyWrapper.getExtImpl().adjustWindowParamsLw(win, attrs);
    }

    public void setDropInputModePolicy(com.android.server.wm.WindowState win, android.view.WindowManager.LayoutParams attrs) {
        if (attrs.type == 2005 && (attrs.privateFlags & 536870912) == 0) {
            this.mService.mTransactionFactory.get().setDropInputMode(win.getSurfaceControl(), 1).apply();
        }
    }

    int validateAddingWindowLw(android.view.WindowManager.LayoutParams attrs, int callingPid, int callingUid) {
        if ((attrs.privateFlags & 536870912) != 0) {
            this.mContext.enforcePermission("android.permission.INTERNAL_SYSTEM_WINDOW", callingPid, callingUid, "DisplayPolicy");
        }
        if ((attrs.privateFlags & Integer.MIN_VALUE) != 0) {
            com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission("DisplayPolicy");
        }
        switch (attrs.type) {
            case 2000:
                this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", callingPid, callingUid, "DisplayPolicy");
                if (this.mStatusBar != null && this.mStatusBar.isAlive()) {
                    return -7;
                }
                break;
            case 2014:
                return -10;
            case 2017:
            case 2033:
            case 2041:
                this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", callingPid, callingUid, "DisplayPolicy");
                break;
            case 2019:
                this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", callingPid, callingUid, "DisplayPolicy");
                if (this.mNavigationBar != null && this.mNavigationBar.isAlive()) {
                    return -7;
                }
                break;
            case 2024:
                this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", callingPid, callingUid, "DisplayPolicy");
                break;
            case 2040:
                this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", callingPid, callingUid, "DisplayPolicy");
                if (this.mNotificationShade != null && this.mNotificationShade.isAlive()) {
                    return -7;
                }
                break;
        }
        if (attrs.providedInsets != null && !this.mService.mAtmService.isCallerRecents(callingUid)) {
            this.mContext.enforcePermission("android.permission.STATUS_BAR_SERVICE", callingPid, callingUid, "DisplayPolicy");
            return 0;
        }
        return 0;
    }

    void addWindowLw(com.android.server.wm.WindowState win, android.view.WindowManager.LayoutParams attrs) {
        android.util.SparseArray<com.android.internal.util.function.TriFunction<com.android.server.wm.DisplayFrames, com.android.server.wm.WindowContainer, android.graphics.Rect, java.lang.Integer>> overrideProviders;
        switch (attrs.type) {
            case 2000:
                this.mStatusBar = win;
                break;
            case 2019:
                this.mNavigationBar = win;
                break;
            case 2024:
                this.mDisplayPolicyWrapper.getExtImpl().addTaskBar(win, true);
                break;
            case 2040:
                this.mNotificationShade = win;
                break;
        }
        if ((attrs.privateFlags & 131072) != 0) {
            this.mImmersiveConfirmationWindowExists = true;
        }
        if (attrs.providedInsets != null) {
            for (int i = attrs.providedInsets.length - 1; i >= 0; i--) {
                android.view.InsetsFrameProvider provider = attrs.providedInsets[i];
                com.android.internal.util.function.TriFunction<com.android.server.wm.DisplayFrames, com.android.server.wm.WindowContainer, android.graphics.Rect, java.lang.Integer> frameProvider = getFrameProvider(win, i, -1);
                android.view.InsetsFrameProvider.InsetsSizeOverride[] overrides = provider.getInsetsSizeOverrides();
                if (overrides != null) {
                    overrideProviders = new android.util.SparseArray<>();
                    for (int j = overrides.length - 1; j >= 0; j--) {
                        overrideProviders.put(overrides[j].getWindowType(), getFrameProvider(win, i, j));
                    }
                } else {
                    overrideProviders = null;
                }
                com.android.server.wm.InsetsSourceProvider sourceProvider = this.mDisplayContent.getInsetsStateController().getOrCreateSourceProvider(provider.getId(), provider.getType());
                sourceProvider.getSource().setFlags(provider.getFlags());
                sourceProvider.setWindowContainer(win, frameProvider, overrideProviders);
                this.mInsetsSourceWindowsExceptIme.add(win);
            }
        }
    }

    private static com.android.internal.util.function.TriFunction<com.android.server.wm.DisplayFrames, com.android.server.wm.WindowContainer, android.graphics.Rect, java.lang.Integer> getFrameProvider(final com.android.server.wm.WindowState win, final int index, final int overrideIndex) {
        return new com.android.internal.util.function.TriFunction() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda0
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                return com.android.server.wm.DisplayPolicy.lambda$getFrameProvider$1(win, index, overrideIndex, (com.android.server.wm.DisplayFrames) obj, (com.android.server.wm.WindowContainer) obj2, (android.graphics.Rect) obj3);
            }
        };
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    static /* synthetic */ java.lang.Integer lambda$getFrameProvider$1(com.android.server.wm.WindowState win, int index, int overrideIndex, com.android.server.wm.DisplayFrames displayFrames, com.android.server.wm.WindowContainer windowContainer, android.graphics.Rect inOutFrame) {
        boolean extendByCutout;
        android.graphics.Insets insetsSize;
        android.view.WindowManager.LayoutParams lp = win.mAttrs.forRotation(displayFrames.mRotation);
        android.view.InsetsFrameProvider ifp = lp.providedInsets[index];
        android.graphics.Rect displayFrame = displayFrames.mUnrestricted;
        android.graphics.Rect safe = displayFrames.mDisplayCutoutSafe;
        switch (ifp.getSource()) {
            case 0:
                inOutFrame.set(displayFrame);
                extendByCutout = false;
                break;
            case 1:
                inOutFrame.set(windowContainer.getBounds());
                extendByCutout = false;
                break;
            case 2:
                boolean extendByCutout2 = (lp.privateFlags & 4096) != 0;
                extendByCutout = extendByCutout2;
                break;
            case 3:
                inOutFrame.set(ifp.getArbitraryRectangle());
                extendByCutout = false;
                break;
            default:
                extendByCutout = false;
                break;
        }
        if (overrideIndex != -1) {
            insetsSize = ifp.getInsetsSizeOverrides()[overrideIndex].getInsetsSize();
        } else {
            insetsSize = ifp.getInsetsSize();
        }
        android.graphics.Insets insetsSize2 = insetsSize;
        if (ifp.getMinimalInsetsSizeInDisplayCutoutSafe() != null) {
            sTmpRect2.set(inOutFrame);
        }
        calculateInsetsFrame(inOutFrame, insetsSize2);
        if (extendByCutout && insetsSize2 != null) {
            android.view.WindowLayout.extendFrameByCutout(safe, displayFrame, inOutFrame, sTmpRect);
        }
        if (ifp.getMinimalInsetsSizeInDisplayCutoutSafe() != null) {
            calculateInsetsFrame(sTmpRect2, ifp.getMinimalInsetsSizeInDisplayCutoutSafe());
            android.view.WindowLayout.extendFrameByCutout(safe, displayFrame, sTmpRect2, sTmpRect);
            if (sTmpRect2.contains(inOutFrame)) {
                inOutFrame.set(sTmpRect2);
            }
        }
        com.android.server.wm.DisplayContent displayContent = win.getDisplayContent();
        if (displayContent != null && displayContent.getDisplayPolicy() != null) {
            displayContent.getDisplayPolicy().mDisplayPolicyWrapper.getExtImpl().updateFrameProvider(displayFrames, win, inOutFrame, lp, ifp);
        } else if (displayContent == null) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
                android.util.Slog.d(TAG, "getFrameProvider() DisplayContent is null");
            }
        } else if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
            android.util.Slog.d(TAG, "getFrameProvider() DisplayPolicy is null");
        }
        return java.lang.Integer.valueOf(ifp.getFlags());
    }

    private static void calculateInsetsFrame(android.graphics.Rect inOutFrame, android.graphics.Insets insetsSize) {
        if (insetsSize == null) {
            return;
        }
        if (insetsSize.left != 0) {
            inOutFrame.right = inOutFrame.left + insetsSize.left;
            return;
        }
        if (insetsSize.top != 0) {
            inOutFrame.bottom = inOutFrame.top + insetsSize.top;
            return;
        }
        if (insetsSize.right != 0) {
            inOutFrame.left = inOutFrame.right - insetsSize.right;
        } else if (insetsSize.bottom != 0) {
            inOutFrame.top = inOutFrame.bottom - insetsSize.bottom;
        } else {
            inOutFrame.setEmpty();
        }
    }

    com.android.internal.util.function.TriFunction<com.android.server.wm.DisplayFrames, com.android.server.wm.WindowContainer, android.graphics.Rect, java.lang.Integer> getImeSourceFrameProvider() {
        return new com.android.internal.util.function.TriFunction() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda14
            public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                return this.f$0.lambda$getImeSourceFrameProvider$2((com.android.server.wm.DisplayFrames) obj, (com.android.server.wm.WindowContainer) obj2, (android.graphics.Rect) obj3);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$getImeSourceFrameProvider$2(com.android.server.wm.DisplayFrames displayFrames, com.android.server.wm.WindowContainer windowContainer, android.graphics.Rect inOutFrame) {
        com.android.server.wm.WindowState windowState = windowContainer.asWindowState();
        if (windowState == null) {
            throw new java.lang.IllegalArgumentException("IME insets must be provided by a window.");
        }
        inOutFrame.inset(windowState.mGivenContentInsets);
        inOutFrame.top = java.lang.Math.min(inOutFrame.top, inOutFrame.bottom);
        return 0;
    }

    void removeWindowLw(com.android.server.wm.WindowState win) {
        if (this.mStatusBar == win) {
            this.mStatusBar = null;
        } else if (this.mNavigationBar == win) {
            this.mNavigationBar = null;
        } else if (this.mNotificationShade == win) {
            this.mNotificationShade = null;
        }
        if (this.mDisplayPolicyWrapper.getExtImpl().getTaskBar() == win) {
            this.mDisplayPolicyWrapper.getExtImpl().addTaskBar(win, false);
        }
        if (this.mLastFocusedWindow == win) {
            this.mLastFocusedWindow = null;
        }
        if (win.hasInsetsSourceProvider()) {
            android.util.SparseArray<com.android.server.wm.InsetsSourceProvider> providers = win.getInsetsSourceProviders();
            com.android.server.wm.InsetsStateController controller = this.mDisplayContent.getInsetsStateController();
            for (int index = providers.size() - 1; index >= 0; index--) {
                com.android.server.wm.InsetsSourceProvider provider = providers.valueAt(index);
                provider.setWindowContainer(null, null, null);
                controller.removeSourceProvider(provider.getSource().getId());
            }
        }
        this.mInsetsSourceWindowsExceptIme.remove(win);
        if ((win.mAttrs.privateFlags & 131072) != 0) {
            this.mImmersiveConfirmationWindowExists = false;
        }
    }

    com.android.server.wm.WindowState getStatusBar() {
        return this.mStatusBar;
    }

    com.android.server.wm.WindowState getNotificationShade() {
        return this.mNotificationShade;
    }

    com.android.server.wm.WindowState getNavigationBar() {
        return this.mNavigationBar;
    }

    boolean isImmersiveMode() {
        return this.mIsImmersiveMode;
    }

    int selectAnimation(com.android.server.wm.WindowState win, int transit) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(win);
            long protoLogParam1 = transit;
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -7427596081878257508L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
        }
        if (transit == 5 && win.hasAppShownWindows()) {
            if (win.isActivityTypeHome()) {
                return -1;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[2]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -6269658847003264525L, 0, null, null);
                return android.R.anim.app_starting_exit;
            }
            return android.R.anim.app_starting_exit;
        }
        return 0;
    }

    public boolean areSystemBarsForcedConsumedLw() {
        return false;
    }

    void simulateLayoutDisplay(com.android.server.wm.DisplayFrames displayFrames) {
        sTmpClientFrames.attachedFrame = null;
        for (int i = this.mInsetsSourceWindowsExceptIme.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState win = this.mInsetsSourceWindowsExceptIme.valueAt(i);
            this.mWindowLayout.computeFrames(win.mAttrs.forRotation(displayFrames.mRotation), displayFrames.mInsetsState, displayFrames.mDisplayCutoutSafe, displayFrames.mUnrestricted, win.getWindowingMode(), -1, -1, win.getRequestedVisibleTypes(), win.mGlobalScale, sTmpClientFrames);
            android.util.SparseArray<com.android.server.wm.InsetsSourceProvider> providers = win.getInsetsSourceProviders();
            android.view.InsetsState state = displayFrames.mInsetsState;
            for (int index = providers.size() - 1; index >= 0; index--) {
                state.addSource(providers.valueAt(index).createSimulatedSource(displayFrames, sTmpClientFrames.frame));
            }
        }
    }

    void onDisplayInfoChanged(android.view.DisplayInfo info) {
        if (!android.view.ViewRootImpl.CLIENT_TRANSIENT) {
            this.mSystemGestures.onDisplayInfoChanged(info);
        }
    }

    public void layoutWindowLw(com.android.server.wm.WindowState win, com.android.server.wm.WindowState attached, com.android.server.wm.DisplayFrames displayFrames) {
        if (win.skipLayout()) {
            return;
        }
        com.android.server.wm.DisplayFrames displayFrames2 = win.getDisplayFrames(displayFrames);
        android.view.WindowManager.LayoutParams attrs = win.mAttrs.forRotation(displayFrames2.mRotation);
        win.getWrapper().getExtImpl().updateAttrsBeforeCompute(attrs);
        sTmpClientFrames.attachedFrame = attached != null ? attached.getFrame() : null;
        boolean trustedSize = attrs == win.mAttrs;
        int requestedWidth = trustedSize ? win.mRequestedWidth : -1;
        int requestedHeight = trustedSize ? win.mRequestedHeight : -1;
        this.mDisplayPolicyWrapper.getExtImpl().layoutInFullScreen(win, null);
        this.mWindowLayout.computeFrames(attrs, win.getInsetsState(), displayFrames2.mDisplayCutoutSafe, win.getBounds(), win.getWindowingMode(), requestedWidth, requestedHeight, win.getRequestedVisibleTypes(), win.mGlobalScale, sTmpClientFrames);
        ((com.android.server.wm.IOplusCarModeManager) android.common.OplusFeatureCache.get(com.android.server.wm.IOplusCarModeManager.DEFAULT)).layoutCarDockBar(this.mDisplayContent, displayFrames2);
        ((com.android.server.wm.IOplusCarModeManager) android.common.OplusFeatureCache.get(com.android.server.wm.IOplusCarModeManager.DEFAULT)).adjustWindowFrameForCarDockBarInsets(this.mDisplayContent, win, sTmpClientFrames);
        if (this.mDisplayPolicyWrapper.getExtImpl().restrictFullScreenActivityRectInCompactWindow(win, attrs.flags, attrs.type, attrs.systemUiVisibility | attrs.subtreeSystemUiVisibility)) {
            com.android.server.wm.DisplayContent defaultDisplay = this.mService.getDefaultDisplayContentLocked();
            android.graphics.Rect stableBounds = new android.graphics.Rect();
            defaultDisplay.getStableRect(stableBounds);
            sTmpClientFrames.frame.bottom = stableBounds.bottom;
        }
        this.mService.mAtmService.getWrapper().getFlexibleExtImpl().adjustInputMethodTargetFrame(this.mDisplayContent, win, sTmpClientFrames);
        win.setFrames(sTmpClientFrames, win.mRequestedWidth, win.mRequestedHeight);
    }

    com.android.server.wm.WindowState getTopFullscreenOpaqueWindow() {
        return this.mTopFullscreenOpaqueWindowState;
    }

    boolean isTopLayoutFullscreen() {
        return this.mTopIsFullscreen;
    }

    public void beginPostLayoutPolicyLw() {
        this.mLeftGestureHost = null;
        this.mTopGestureHost = null;
        this.mRightGestureHost = null;
        this.mBottomGestureHost = null;
        this.mTopFullscreenOpaqueWindowState = null;
        this.mNavBarColorWindowCandidate = null;
        this.mNavBarBackgroundWindowCandidate = null;
        this.mStatusBarAppearanceRegionList.clear();
        this.mLetterboxDetails.clear();
        this.mStatusBarBackgroundWindows.clear();
        this.mStatusBarColorCheckedBounds.setEmpty();
        this.mStatusBarBackgroundCheckedBounds.setEmpty();
        this.mSystemBarColorApps.clear();
        this.mAllowLockscreenWhenOn = false;
        this.mShowingDream = false;
        this.mIsFreeformWindowOverlappingWithNavBar = false;
        this.mForciblyShownTypes = 0;
        this.mImeInsetsConsumed = false;
    }

    public void applyPostLayoutPolicyLw(com.android.server.wm.WindowState win, android.view.WindowManager.LayoutParams attrs, com.android.server.wm.WindowState attached, com.android.server.wm.WindowState imeTarget) {
        com.android.internal.statusbar.LetterboxDetails currentLetterboxDetails;
        if (attrs.type == 2019) {
            com.android.server.wm.DisplayFrames displayFrames = this.mDisplayContent.mDisplayFrames;
            this.mNavigationBarPosition = navigationBarPosition(displayFrames.mRotation);
        }
        boolean affectsSystemUi = win.canAffectSystemUiFlags();
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
            android.util.Slog.i(TAG, "Win " + win + ": affectsSystemUi=" + affectsSystemUi);
        }
        applyKeyguardPolicy(win, imeTarget);
        if (!this.mIsFreeformWindowOverlappingWithNavBar && win.inFreeformWindowingMode() && win.mActivityRecord != null && isOverlappingWithNavBar(win)) {
            this.mIsFreeformWindowOverlappingWithNavBar = true;
        }
        if (win.hasInsetsSourceProvider()) {
            android.util.SparseArray<com.android.server.wm.InsetsSourceProvider> providers = win.getInsetsSourceProviders();
            android.graphics.Rect bounds = win.getBounds();
            for (int index = providers.size() - 1; index >= 0; index--) {
                com.android.server.wm.InsetsSourceProvider provider = providers.valueAt(index);
                android.view.InsetsSource source = provider.getSource();
                if ((source.getType() & (android.view.WindowInsets.Type.systemGestures() | android.view.WindowInsets.Type.mandatorySystemGestures())) != 0 && (this.mLeftGestureHost == null || this.mTopGestureHost == null || this.mRightGestureHost == null || this.mBottomGestureHost == null)) {
                    android.graphics.Insets insets = source.calculateInsets(bounds, false);
                    if (this.mLeftGestureHost == null && insets.left > 0) {
                        this.mLeftGestureHost = win;
                    }
                    if (this.mTopGestureHost == null && insets.top > 0) {
                        this.mTopGestureHost = win;
                    }
                    if (this.mRightGestureHost == null && insets.right > 0) {
                        this.mRightGestureHost = win;
                    }
                    if (this.mBottomGestureHost == null && insets.bottom > 0) {
                        this.mBottomGestureHost = win;
                    }
                }
            }
        }
        if (win.mSession.mCanForceShowingInsets) {
            this.mForciblyShownTypes |= win.mAttrs.forciblyShownTypes;
        }
        if (win.mImeInsetsConsumed != this.mImeInsetsConsumed) {
            win.mImeInsetsConsumed = this.mImeInsetsConsumed;
            com.android.server.wm.WindowState imeWin = this.mDisplayContent.mInputMethodWindow;
            if (win.isReadyToDispatchInsetsState() && imeWin != null && imeWin.isVisible()) {
                win.notifyInsetsChanged();
            }
        }
        if ((attrs.privateFlags & 33554432) != 0 && win.isVisible()) {
            this.mImeInsetsConsumed = true;
        }
        if (!affectsSystemUi) {
            return;
        }
        boolean appWindow = this.mDisplayPolicyWrapper.getExtImpl().isSpecialAppWindow(attrs.type >= 1 && attrs.type < 2000, attrs);
        if (this.mTopFullscreenOpaqueWindowState == null) {
            int fl = attrs.flags;
            if (win.isDreamWindow() && (!this.mDreamingLockscreen || (win.isVisible() && win.hasDrawn()))) {
                this.mShowingDream = true;
                appWindow = true;
            }
            if (appWindow && attached == null && attrs.isFullscreen() && (fl & 1) != 0) {
                this.mAllowLockscreenWhenOn = true;
            }
        }
        if ((appWindow && attached == null && attrs.isFullscreen() && this.mDisplayPolicyWrapper.getExtImpl().canBeTopFullscreenOpqWin(win)) || attrs.type == 2031) {
            boolean exitingStartingWindow = attrs.type == 3 && win.mAnimatingExit;
            if (this.mTopFullscreenOpaqueWindowState == null && !exitingStartingWindow && !this.mDisplayPolicyWrapper.getExtImpl().judgeWindowModeZoom(win) && !this.mDisplayPolicyWrapper.getExtImpl().isFlexibleTaskIgnoreSysBar(win)) {
                this.mTopFullscreenOpaqueWindowState = win;
                this.mDisplayPolicyWrapper.getExtImpl().onTopFullscreenOpaqueWindowUpdated(this, this.mTopFullscreenOpaqueWindowState);
            }
            if (this.mStatusBar != null && ((sTmpRect.setIntersect(win.getFrame(), this.mStatusBar.getFrame()) || this.mDisplayPolicyWrapper.getExtImpl().intersectInCompactWindow(win, this.mStatusBar.getFrame(), sTmpRect)) && !this.mStatusBarBackgroundCheckedBounds.contains(sTmpRect) && !this.mDisplayPolicyWrapper.getExtImpl().judgeWindowModeZoom(win) && !this.mDisplayPolicyWrapper.getExtImpl().affectsSystemUiInTransition(win, this.mLastAppearance))) {
                win = this.mDisplayPolicyWrapper.getExtImpl().updateSystemBarWindow(win, win);
                this.mStatusBarBackgroundWindows.add(win);
                this.mStatusBarBackgroundCheckedBounds.union(sTmpRect);
                if (!this.mStatusBarColorCheckedBounds.contains(sTmpRect)) {
                    this.mStatusBarAppearanceRegionList.add(new com.android.internal.view.AppearanceRegion(win.mAttrs.insetsFlags.appearance & 8, new android.graphics.Rect(win.getFrame())));
                    this.mStatusBarColorCheckedBounds.union(sTmpRect);
                    addSystemBarColorApp(win);
                }
            }
            if (isOverlappingWithNavBar(win)) {
                if (this.mNavBarColorWindowCandidate == null) {
                    this.mNavBarColorWindowCandidate = win;
                    addSystemBarColorApp(win);
                }
                if (this.mNavBarBackgroundWindowCandidate == null) {
                    this.mNavBarBackgroundWindowCandidate = win;
                }
            }
            com.android.server.wm.ActivityRecord currentActivity = win.getActivityRecord();
            if (currentActivity != null && (currentLetterboxDetails = currentActivity.mLetterboxUiController.getLetterboxDetails()) != null) {
                this.mLetterboxDetails.add(currentLetterboxDetails);
                return;
            }
            return;
        }
        if (win.isDimming() && !this.mDisplayPolicyWrapper.getExtImpl().judgeWindowModeZoom(win)) {
            if (this.mStatusBar != null) {
                int statusBarLayer = this.mStatusBar.mToken.getWindowLayerFromType();
                int targetWindowLayer = win.mToken.getWindowLayerFromType();
                if (targetWindowLayer < statusBarLayer && addStatusBarAppearanceRegionsForDimmingWindow(win.mAttrs.insetsFlags.appearance & 8, this.mStatusBar.getFrame(), win.getBounds(), win.getFrame())) {
                    addSystemBarColorApp(win);
                }
            }
            if (isOverlappingWithNavBar(win) && this.mNavBarColorWindowCandidate == null) {
                this.mNavBarColorWindowCandidate = win;
                addSystemBarColorApp(win);
                return;
            }
            return;
        }
        if (appWindow && attached == null) {
            if ((this.mNavBarColorWindowCandidate == null || this.mNavBarBackgroundWindowCandidate == null) && win.getFrame().contains(getBarContentFrameForWindow(win, android.view.WindowInsets.Type.navigationBars()))) {
                if (this.mNavBarColorWindowCandidate == null) {
                    this.mNavBarColorWindowCandidate = win;
                    addSystemBarColorApp(win);
                }
                if (this.mNavBarBackgroundWindowCandidate == null) {
                    this.mNavBarBackgroundWindowCandidate = win;
                }
            }
        }
    }

    private boolean addStatusBarAppearanceRegionsForDimmingWindow(int appearance, android.graphics.Rect statusBarFrame, android.graphics.Rect winBounds, android.graphics.Rect winFrame) {
        if (!sTmpRect.setIntersect(winBounds, statusBarFrame) || this.mStatusBarColorCheckedBounds.contains(sTmpRect)) {
            return false;
        }
        if (appearance == 0 || !sTmpRect2.setIntersect(winFrame, statusBarFrame)) {
            this.mStatusBarAppearanceRegionList.add(new com.android.internal.view.AppearanceRegion(0, new android.graphics.Rect(winBounds)));
            this.mStatusBarColorCheckedBounds.union(sTmpRect);
            return true;
        }
        this.mStatusBarAppearanceRegionList.add(new com.android.internal.view.AppearanceRegion(appearance, new android.graphics.Rect(winFrame)));
        if (!sTmpRect.equals(sTmpRect2) && sTmpRect.height() == sTmpRect2.height()) {
            if (sTmpRect.left != sTmpRect2.left) {
                this.mStatusBarAppearanceRegionList.add(new com.android.internal.view.AppearanceRegion(0, new android.graphics.Rect(winBounds.left, winBounds.top, sTmpRect2.left, winBounds.bottom)));
            }
            if (sTmpRect.right != sTmpRect2.right) {
                this.mStatusBarAppearanceRegionList.add(new com.android.internal.view.AppearanceRegion(0, new android.graphics.Rect(sTmpRect2.right, winBounds.top, winBounds.right, winBounds.bottom)));
            }
        }
        this.mStatusBarColorCheckedBounds.union(sTmpRect);
        return true;
    }

    private void addSystemBarColorApp(com.android.server.wm.WindowState win) {
        com.android.server.wm.ActivityRecord app = win.mActivityRecord;
        if (app != null) {
            this.mSystemBarColorApps.add(app);
        }
    }

    public void finishPostLayoutPolicyLw() {
        if (!this.mShowingDream) {
            this.mDreamingLockscreen = this.mService.mPolicy.isKeyguardShowingAndNotOccluded();
        }
        updateSystemBarAttributes();
        if (this.mShowingDream != this.mLastShowingDream) {
            this.mLastShowingDream = this.mShowingDream;
            this.mDisplayContent.notifyKeyguardFlagsChanged();
        }
        this.mService.mPolicy.setAllowLockscreenWhenOn(getDisplayId(), this.mAllowLockscreenWhenOn);
    }

    boolean areTypesForciblyShownTransiently(int types) {
        return (this.mForciblyShownTypes & types) == types;
    }

    private void applyKeyguardPolicy(com.android.server.wm.WindowState win, com.android.server.wm.WindowState imeTarget) {
        if (win.canBeHiddenByKeyguard()) {
            boolean shouldBeHiddenByKeyguard = shouldBeHiddenByKeyguard(win, imeTarget);
            if (win.mIsImWindow) {
                this.mDisplayContent.getInsetsStateController().getImeSourceProvider().setFrozen(shouldBeHiddenByKeyguard);
            }
            if (shouldBeHiddenByKeyguard) {
                win.hide(false, true);
            } else {
                win.show(false, true);
            }
        }
    }

    private boolean shouldBeHiddenByKeyguard(com.android.server.wm.WindowState win, com.android.server.wm.WindowState imeTarget) {
        if (!this.mDisplayContent.isDefaultDisplay || !isKeyguardShowing()) {
            return false;
        }
        boolean showImeOverKeyguard = imeTarget != null && imeTarget.isVisible() && imeTarget.isOnScreen() && win.mIsImWindow && (imeTarget.canShowWhenLocked() || !imeTarget.canBeHiddenByKeyguard());
        if (showImeOverKeyguard) {
            return false;
        }
        boolean allowShowWhenLocked = isKeyguardOccluded() && (win.canShowWhenLocked() || (win.mAttrs.privateFlags & 256) != 0);
        return !allowShowWhenLocked;
    }

    boolean topAppHidesSystemBar(int type) {
        if (this.mDisplayPolicyWrapper.getExtImpl().isSplitTaskVisible(this.mDisplayContent)) {
            return (type == android.view.WindowInsets.Type.navigationBars() && this.mTopFullscreenOpaqueWindowState == null) ? false : true;
        }
        if (this.mTopFullscreenOpaqueWindowState == null || getInsetsPolicy().areTypesForciblyShowing(type)) {
            return false;
        }
        if (this.mDisplayPolicyWrapper.getExtImpl().updateSpecialSystemBar(this.mTopFullscreenOpaqueWindowState.getAttrs())) {
            return this.mTopIsFullscreen;
        }
        return !this.mTopFullscreenOpaqueWindowState.isRequestedVisible(type);
    }

    public void switchUser() {
        updateCurrentUserResources();
        updateForceShowNavBarSettings();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onOverlayChanged() {
        updateCurrentUserResources();
        this.mDisplayContent.requestDisplayUpdate(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onOverlayChanged$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onOverlayChanged$3() {
        onConfigurationChanged();
        if (!android.view.ViewRootImpl.CLIENT_TRANSIENT) {
            this.mSystemGestures.onConfigurationChanged();
        }
    }

    public void onConfigurationChanged() {
        android.content.res.Resources res = getCurrentUserResources();
        this.mNavBarOpacityMode = res.getInteger(android.R.integer.config_maximumCallLogEntriesPerSim);
        this.mLeftGestureInset = this.mGestureNavigationSettingsObserver.getLeftSensitivity(res);
        this.mRightGestureInset = this.mGestureNavigationSettingsObserver.getRightSensitivity(res);
        this.mNavigationBarAlwaysShowOnSideGesture = res.getBoolean(android.R.bool.config_letterboxIsVerticalReachabilityEnabled);
        this.mRemoteInsetsControllerControlsSystemBars = res.getBoolean(android.R.bool.config_remoteInsetsControllerControlsSystemBars);
        updateConfigurationAndScreenSizeDependentBehaviors();
        boolean shouldAttach = res.getBoolean(android.R.bool.config_appCompatUserAppAspectRatioSettingsIsEnabled) && !com.android.wm.shell.Flags.enableTinyTaskbar();
        if (this.mShouldAttachNavBarToAppDuringTransition != shouldAttach) {
            this.mShouldAttachNavBarToAppDuringTransition = shouldAttach;
        }
    }

    void updateConfigurationAndScreenSizeDependentBehaviors() {
        android.content.res.Resources res = getCurrentUserResources();
        this.mNavigationBarCanMove = this.mDisplayContent.mBaseDisplayWidth != this.mDisplayContent.mBaseDisplayHeight && res.getBoolean(android.R.bool.config_lidControlsScreenLock);
        if (this.mDisplayContent.getDisplayRotation() == null) {
            android.util.Slog.w(TAG, "DisplayRotation is null of display:" + this.mDisplayContent);
        } else {
            this.mDisplayContent.getDisplayRotation().updateUserDependentConfiguration(res);
        }
    }

    private void updateCurrentUserResources() {
        int userId = this.mService.mAmInternal.getCurrentUserId();
        android.content.Context uiContext = getSystemUiContext();
        if (userId == 0) {
            this.mCurrentUserResources = uiContext.getResources();
        } else {
            android.app.LoadedApk pi = android.app.ActivityThread.currentActivityThread().getPackageInfo(uiContext.getPackageName(), (android.content.res.CompatibilityInfo) null, 0, userId);
            this.mCurrentUserResources = android.app.ResourcesManager.getInstance().getResources(uiContext.getWindowContextToken(), pi.getResDir(), (java.lang.String[]) null, pi.getOverlayDirs(), pi.getOverlayPaths(), pi.getApplicationInfo().sharedLibraryFiles, java.lang.Integer.valueOf(this.mDisplayContent.getDisplayId()), (android.content.res.Configuration) null, uiContext.getResources().getCompatibilityInfo(), (java.lang.ClassLoader) null, (java.util.List) null);
        }
    }

    android.content.res.Resources getCurrentUserResources() {
        if (this.mCurrentUserResources == null) {
            updateCurrentUserResources();
        }
        return this.mCurrentUserResources;
    }

    android.content.Context getContext() {
        return this.mContext;
    }

    android.content.Context getSystemUiContext() {
        return this.mUiContext;
    }

    void setCanSystemBarsBeShownByUser(boolean canBeShown) {
        this.mCanSystemBarsBeShownByUser = canBeShown;
    }

    void notifyDisplayReady() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyDisplayReady$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyDisplayReady$4() {
        int displayId = getDisplayId();
        com.android.server.statusbar.StatusBarManagerInternal statusBar = getStatusBarManagerInternal();
        if (statusBar != null) {
            statusBar.onDisplayReady(displayId);
        }
        com.android.server.wallpaper.WallpaperManagerInternal wpMgr = (com.android.server.wallpaper.WallpaperManagerInternal) com.android.server.LocalServices.getService(com.android.server.wallpaper.WallpaperManagerInternal.class);
        if (wpMgr != null) {
            wpMgr.onDisplayReady(displayId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNavigationBarFrameHeight(int rotation) {
        if (this.mNavigationBar == null) {
            return 0;
        }
        return this.mNavigationBar.mAttrs.forRotation(rotation).height;
    }

    float getWindowCornerRadius() {
        if (this.mDisplayContent.getDisplay().getType() == 1) {
            return com.android.internal.policy.ScreenDecorationsUtils.getWindowCornerRadius(this.mContext);
        }
        return 0.0f;
    }

    boolean isShowingDreamLw() {
        return this.mShowingDream;
    }

    static class DecorInsets {
        private final com.android.server.wm.DisplayContent mDisplayContent;
        private final com.android.server.wm.DisplayPolicy.DecorInsets.Info[] mInfoForRotation = new com.android.server.wm.DisplayPolicy.DecorInsets.Info[4];
        final com.android.server.wm.DisplayPolicy.DecorInsets.Info mTmpInfo = new com.android.server.wm.DisplayPolicy.DecorInsets.Info();

        static class Info {
            final android.graphics.Rect mNonDecorInsets = new android.graphics.Rect();
            final android.graphics.Rect mConfigInsets = new android.graphics.Rect();
            final android.graphics.Rect mOverrideConfigInsets = new android.graphics.Rect();
            final android.graphics.Rect mOverrideNonDecorInsets = new android.graphics.Rect();
            final android.graphics.Rect mNonDecorFrame = new android.graphics.Rect();
            final android.graphics.Rect mConfigFrame = new android.graphics.Rect();
            final android.graphics.Rect mOverrideConfigFrame = new android.graphics.Rect();
            final android.graphics.Rect mOverrideNonDecorFrame = new android.graphics.Rect();
            private boolean mNeedUpdate = true;

            Info() {
            }

            android.view.InsetsState update(com.android.server.wm.DisplayContent dc, int rotation, int w, int h) {
                android.graphics.Insets configInsets;
                android.graphics.Insets overrideConfigInsets;
                android.graphics.Insets overrideDecorInsets;
                com.android.server.wm.DisplayFrames df = new com.android.server.wm.DisplayFrames();
                dc.updateDisplayFrames(df, rotation, w, h);
                dc.getDisplayPolicy().simulateLayoutDisplay(df);
                android.view.InsetsState insetsState = df.mInsetsState;
                android.graphics.Rect displayFrame = insetsState.getDisplayFrame();
                displayFrame.bottom -= dc.getDisplayPolicy().getWrapper().getExtImpl().getBootInvalidNavigationBarHeight(rotation);
                android.graphics.Insets decor = insetsState.calculateInsets(displayFrame, dc.mWmService.mDecorTypes, true);
                if (dc.mWmService.mConfigTypes == dc.mWmService.mDecorTypes) {
                    configInsets = decor;
                } else {
                    configInsets = insetsState.calculateInsets(displayFrame, dc.mWmService.mConfigTypes, true);
                }
                if (dc.mWmService.mConfigTypes == dc.mWmService.mOverrideConfigTypes) {
                    overrideConfigInsets = configInsets;
                } else {
                    overrideConfigInsets = insetsState.calculateInsets(displayFrame, dc.mWmService.mOverrideConfigTypes, true);
                }
                if (dc.mWmService.mDecorTypes == dc.mWmService.mOverrideDecorTypes) {
                    overrideDecorInsets = decor;
                } else {
                    overrideDecorInsets = insetsState.calculateInsets(displayFrame, dc.mWmService.mOverrideDecorTypes, true);
                }
                this.mNonDecorInsets.set(decor.left, decor.top, decor.right, decor.bottom);
                this.mConfigInsets.set(configInsets.left, configInsets.top, configInsets.right, configInsets.bottom);
                this.mOverrideConfigInsets.set(overrideConfigInsets.left, overrideConfigInsets.top, overrideConfigInsets.right, overrideConfigInsets.bottom);
                this.mOverrideNonDecorInsets.set(overrideDecorInsets.left, overrideDecorInsets.top, overrideDecorInsets.right, overrideDecorInsets.bottom);
                this.mNonDecorFrame.set(displayFrame);
                this.mNonDecorFrame.inset(this.mNonDecorInsets);
                this.mConfigFrame.set(displayFrame);
                this.mConfigFrame.inset(this.mConfigInsets);
                this.mOverrideConfigFrame.set(displayFrame);
                dc.getDisplayPolicy().getWrapper().getExtImpl().getTaskBarDecorInsets(dc.getDisplayPolicy().getWrapper().getNavBar(), rotation, this.mOverrideConfigInsets);
                this.mOverrideConfigFrame.inset(this.mOverrideConfigInsets);
                this.mOverrideNonDecorFrame.set(displayFrame);
                dc.getDisplayPolicy().getWrapper().getExtImpl().getTaskBarDecorInsets(dc.getDisplayPolicy().getWrapper().getNavBar(), rotation, this.mOverrideNonDecorInsets);
                this.mOverrideNonDecorFrame.inset(this.mOverrideNonDecorInsets);
                this.mNeedUpdate = false;
                return insetsState;
            }

            void set(com.android.server.wm.DisplayPolicy.DecorInsets.Info other) {
                this.mNonDecorInsets.set(other.mNonDecorInsets);
                this.mConfigInsets.set(other.mConfigInsets);
                this.mOverrideConfigInsets.set(other.mOverrideConfigInsets);
                this.mOverrideNonDecorInsets.set(other.mOverrideNonDecorInsets);
                this.mNonDecorFrame.set(other.mNonDecorFrame);
                this.mConfigFrame.set(other.mConfigFrame);
                this.mOverrideConfigFrame.set(other.mOverrideConfigFrame);
                this.mOverrideNonDecorFrame.set(other.mOverrideNonDecorFrame);
                this.mNeedUpdate = false;
            }

            public java.lang.String toString() {
                java.lang.StringBuilder tmpSb = new java.lang.StringBuilder(32);
                return "{nonDecorInsets=" + this.mNonDecorInsets.toShortString(tmpSb) + ", overrideNonDecorInsets=" + this.mOverrideNonDecorInsets.toShortString(tmpSb) + ", configInsets=" + this.mConfigInsets.toShortString(tmpSb) + ", overrideConfigInsets=" + this.mOverrideConfigInsets.toShortString(tmpSb) + ", nonDecorFrame=" + this.mNonDecorFrame.toShortString(tmpSb) + ", overrideNonDecorFrame=" + this.mOverrideNonDecorFrame.toShortString(tmpSb) + ", configFrame=" + this.mConfigFrame.toShortString(tmpSb) + ", overrideConfigFrame=" + this.mOverrideConfigFrame.toShortString(tmpSb) + '}';
            }
        }

        DecorInsets(com.android.server.wm.DisplayContent dc) {
            this.mDisplayContent = dc;
            for (int i = this.mInfoForRotation.length - 1; i >= 0; i--) {
                this.mInfoForRotation[i] = new com.android.server.wm.DisplayPolicy.DecorInsets.Info();
            }
        }

        com.android.server.wm.DisplayPolicy.DecorInsets.Info get(int rotation, int w, int h) {
            com.android.server.wm.DisplayPolicy.DecorInsets.Info info = this.mInfoForRotation[rotation];
            if (info.mNeedUpdate) {
                info.update(this.mDisplayContent, rotation, w, h);
            }
            return info;
        }

        void invalidate() {
            for (com.android.server.wm.DisplayPolicy.DecorInsets.Info info : this.mInfoForRotation) {
                info.mNeedUpdate = true;
            }
        }

        void setTo(com.android.server.wm.DisplayPolicy.DecorInsets src) {
            for (int i = this.mInfoForRotation.length - 1; i >= 0; i--) {
                this.mInfoForRotation[i].set(src.mInfoForRotation[i]);
            }
        }

        void dump(java.lang.String prefix, java.io.PrintWriter pw) {
            for (int rotation = 0; rotation < this.mInfoForRotation.length; rotation++) {
                com.android.server.wm.DisplayPolicy.DecorInsets.Info info = this.mInfoForRotation[rotation];
                pw.println(prefix + android.view.Surface.rotationToString(rotation) + "=" + info);
            }
        }

        static boolean hasInsetsFrameDiff(android.view.InsetsState s1, android.view.InsetsState s2, int insetsTypes) {
            int insetsCount1 = 0;
            for (int i = s1.sourceSize() - 1; i >= 0; i--) {
                android.view.InsetsSource source1 = s1.sourceAt(i);
                if ((source1.getType() & insetsTypes) != 0) {
                    insetsCount1++;
                    android.view.InsetsSource source2 = s2.peekSource(source1.getId());
                    if (source2 == null || !source2.getFrame().equals(source1.getFrame())) {
                        return true;
                    }
                }
            }
            int insetsCount2 = 0;
            for (int i2 = s2.sourceSize() - 1; i2 >= 0; i2--) {
                if ((s2.sourceAt(i2).getType() & insetsTypes) != 0) {
                    insetsCount2++;
                }
            }
            return insetsCount1 != insetsCount2;
        }

        private static class Cache {
            static final int ID_UPDATING_CONFIG = -1;
            boolean mActive;
            final com.android.server.wm.DisplayPolicy.DecorInsets mDecorInsets;
            int mPreserveId;

            Cache(com.android.server.wm.DisplayContent dc) {
                this.mDecorInsets = new com.android.server.wm.DisplayPolicy.DecorInsets(dc);
            }

            boolean canPreserve() {
                return this.mPreserveId == -1 || this.mDecorInsets.mDisplayContent.mTransitionController.inTransition(this.mPreserveId);
            }
        }
    }

    boolean updateDecorInsetsInfo() {
        if (shouldKeepCurrentDecorInsets()) {
            return false;
        }
        com.android.server.wm.DisplayFrames displayFrames = this.mDisplayContent.mDisplayFrames;
        int rotation = displayFrames.mRotation;
        int dw = displayFrames.mWidth;
        int dh = displayFrames.mHeight;
        com.android.server.wm.DisplayPolicy.DecorInsets.Info newInfo = this.mDecorInsets.mTmpInfo;
        android.view.InsetsState newInsetsState = newInfo.update(this.mDisplayContent, rotation, dw, dh);
        com.android.server.wm.DisplayPolicy.DecorInsets.Info currentInfo = getDecorInsetsInfo(rotation, dw, dh);
        int i = 1;
        if (newInfo.mConfigFrame.equals(currentInfo.mConfigFrame) && newInfo.mOverrideConfigFrame.equals(currentInfo.mOverrideConfigFrame)) {
            android.view.InsetsState currentInsetsState = this.mDisplayContent.mDisplayFrames.mInsetsState;
            if (com.android.server.wm.DisplayPolicy.DecorInsets.hasInsetsFrameDiff(newInsetsState, currentInsetsState, this.mService.mConfigTypes)) {
                int i2 = this.mDecorInsets.mInfoForRotation.length - 1;
                while (i2 >= 0) {
                    if (i2 != rotation) {
                        int i3 = (i2 + rotation) % 2 == i ? i : 0;
                        int w = i3 != 0 ? dh : dw;
                        int h = i3 != 0 ? dw : dh;
                        this.mDecorInsets.mInfoForRotation[i2].update(this.mDisplayContent, i2, w, h);
                    }
                    i2--;
                    i = 1;
                }
                this.mDecorInsets.mInfoForRotation[rotation].set(newInfo);
            }
            return false;
        }
        if (this.mCachedDecorInsets != null && !this.mCachedDecorInsets.canPreserve() && this.mScreenOnFully) {
            this.mCachedDecorInsets = null;
        }
        this.mDecorInsets.invalidate();
        this.mDecorInsets.mInfoForRotation[rotation].set(newInfo);
        return true;
    }

    com.android.server.wm.DisplayPolicy.DecorInsets.Info getDecorInsetsInfo(int rotation, int w, int h) {
        return this.mDecorInsets.get(rotation, w, h);
    }

    boolean shouldKeepCurrentDecorInsets() {
        return this.mCachedDecorInsets != null && this.mCachedDecorInsets.mActive && this.mCachedDecorInsets.canPreserve();
    }

    void physicalDisplayChanged() {
        if (USE_CACHED_INSETS_FOR_DISPLAY_SWITCH) {
            updateCachedDecorInsets();
        }
    }

    void updateCachedDecorInsets() {
        com.android.server.wm.DisplayPolicy.DecorInsets prevCache = null;
        if (this.mCachedDecorInsets == null) {
            this.mCachedDecorInsets = new com.android.server.wm.DisplayPolicy.DecorInsets.Cache(this.mDisplayContent);
        } else {
            prevCache = new com.android.server.wm.DisplayPolicy.DecorInsets(this.mDisplayContent);
            prevCache.setTo(this.mCachedDecorInsets.mDecorInsets);
        }
        this.mCachedDecorInsets.mPreserveId = -1;
        this.mCachedDecorInsets.mDecorInsets.setTo(this.mDecorInsets);
        if (prevCache != null) {
            this.mDecorInsets.setTo(prevCache);
            this.mCachedDecorInsets.mActive = true;
        }
    }

    void physicalDisplayUpdated() {
        if (this.mCachedDecorInsets == null) {
            return;
        }
        if (!this.mDisplayContent.mTransitionController.isCollecting()) {
            this.mCachedDecorInsets = null;
        } else {
            this.mCachedDecorInsets.mPreserveId = this.mDisplayContent.mTransitionController.getCollectingTransitionId();
        }
    }

    public void onDisplaySwitchStart() {
        this.mDisplayContent.mDisplayUpdater.onDisplaySwitching(true);
    }

    int navigationBarPosition(int displayRotation) {
        if (this.mNavigationBar != null) {
            if (this.mDisplayPolicyWrapper.getExtImpl().adjustNavigationBarToBottom(this.mDisplayContent)) {
                return 4;
            }
            int gravity = this.mNavigationBar.mAttrs.forRotation(displayRotation).gravity;
            switch (gravity) {
            }
            return 4;
        }
        return -1;
    }

    public void focusChangedLw(com.android.server.wm.WindowState lastFocus, com.android.server.wm.WindowState newFocus) {
        this.mFocusedWindow = newFocus;
        this.mLastFocusedWindow = lastFocus;
        if (this.mDisplayContent.isDefaultDisplay) {
            this.mService.mPolicy.onDefaultDisplayFocusChangedLw(newFocus);
        }
        updateSystemBarAttributes();
    }

    void requestTransientBars(com.android.server.wm.WindowState swipeTarget, boolean isGestureOnSystemBar) {
        com.android.server.wm.InsetsControlTarget controlTarget;
        com.android.server.wm.WindowState win;
        if (android.view.ViewRootImpl.CLIENT_TRANSIENT) {
            return;
        }
        if (swipeTarget == null || !this.mService.mPolicy.isUserSetupComplete()) {
            android.util.Slog.d(TAG, "request show transient bar, swipeTarget = " + swipeTarget);
            return;
        }
        if (!this.mCanSystemBarsBeShownByUser) {
            android.util.Slog.d(TAG, "Remote insets controller disallows showing system bars - ignoring request");
            return;
        }
        if ((swipeTarget == this.mNavigationBar || swipeTarget == this.mDisplayPolicyWrapper.getExtImpl().getTaskBar()) && this.mDisplayPolicyWrapper.getExtImpl().isDisableExpendNavBar()) {
            android.util.Slog.d(TAG, "NAVIGATIONBAR GESTURE Mode Not showing Navigation bar");
            return;
        }
        if (swipeTarget == this.mStatusBar && this.mDisplayPolicyWrapper.getExtImpl().isDisableExpendStatusBar()) {
            android.util.Slog.d(TAG, "NAVIGATIONBAR GESTURE Mode Not showing status bar");
            return;
        }
        if (this.mZenModeManagerExt.isZenModeOn()) {
            android.util.Slog.d(TAG, "Zen mode, not showing bar");
            return;
        }
        com.android.server.wm.InsetsSourceProvider provider = swipeTarget.getControllableInsetProvider();
        if (provider == null) {
            controlTarget = null;
        } else {
            controlTarget = provider.getControlTarget();
        }
        if (controlTarget == null || controlTarget == getNotificationShade()) {
            android.util.Slog.d(TAG, "request show transient bar, provider = " + provider + " controlTarget = " + controlTarget);
            return;
        }
        if (controlTarget != null && (win = controlTarget.getWindow()) != null && win.isActivityTypeDream()) {
            android.util.Slog.d(TAG, "request show transient bar, is activity type dream");
            return;
        }
        int restorePositionTypes = (android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars()) & controlTarget.getRequestedVisibleTypes();
        com.android.server.wm.InsetsSourceProvider sp = swipeTarget.getControllableInsetProvider();
        if (sp != null && sp.getSource().getType() == android.view.WindowInsets.Type.navigationBars() && (android.view.WindowInsets.Type.navigationBars() & restorePositionTypes) != 0) {
            controlTarget.showInsets(android.view.WindowInsets.Type.navigationBars(), false, null);
            android.util.Slog.d(TAG, "request show transient bar, navigationBar already visible");
            return;
        }
        if (!controlTarget.canShowTransient()) {
            controlTarget.showInsets(android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars(), false, null);
            if (swipeTarget == this.mStatusBar && this.mNotificationShade != null && this.mNotificationShade.mViewVisibility != 0) {
                boolean transferred = this.mStatusBar.transferTouch();
                if (!transferred) {
                    android.util.Slog.i(TAG, "Could not transfer touch to the status bar");
                }
            }
        } else {
            this.mDisplayContent.getInsetsPolicy().showTransient(SHOW_TYPES_FOR_SWIPE, isGestureOnSystemBar);
            controlTarget.showInsets(restorePositionTypes, false, null);
        }
        if (android.view.ViewRootImpl.CLIENT_IMMERSIVE_CONFIRMATION || android.view.ViewRootImpl.CLIENT_TRANSIENT) {
            this.mStatusBarManagerInternal.confirmImmersivePrompt();
        } else {
            this.mImmersiveModeConfirmation.confirmCurrentPrompt();
        }
    }

    boolean isKeyguardShowing() {
        return this.mService.mPolicy.isKeyguardShowing();
    }

    private boolean isKeyguardOccluded() {
        return this.mService.mPolicy.isKeyguardOccluded();
    }

    com.android.server.wm.InsetsPolicy getInsetsPolicy() {
        return this.mDisplayContent.getInsetsPolicy();
    }

    void addRelaunchingApp(com.android.server.wm.ActivityRecord app) {
        if (this.mSystemBarColorApps.contains(app) && !app.hasStartingWindow()) {
            this.mRelaunchingSystemBarColorApps.add(app);
        }
    }

    void removeRelaunchingApp(com.android.server.wm.ActivityRecord app) {
        boolean removed = this.mRelaunchingSystemBarColorApps.remove(app);
        if (this.mRelaunchingSystemBarColorApps.isEmpty() & removed) {
            updateSystemBarAttributes();
        }
    }

    void resetSystemBarAttributes() {
        this.mLastDisableFlags = 0;
        updateSystemBarAttributes();
    }

    void updateSystemBarAttributes() {
        com.android.server.wm.WindowState windowState;
        com.android.server.wm.WindowState windowState2;
        if (this.mFocusedWindow == null && this.mTopFullscreenOpaqueWindowState != null && ((this.mTopFullscreenOpaqueWindowState.mAttrs.flags & 8) == 0 || this.mDisplayPolicyWrapper.getExtImpl().shouldNoFocusWindowUpdateSystemBarAttributes(this.mTopFullscreenOpaqueWindowState))) {
            com.android.server.wm.WindowState winCandidate = this.mTopFullscreenOpaqueWindowState;
        }
        if (this.mFocusedWindow != null) {
            windowState = this.mFocusedWindow;
        } else {
            windowState = this.mTopFullscreenOpaqueWindowState;
        }
        com.android.server.wm.WindowState winCandidate2 = windowState;
        if (winCandidate2 != null) {
            com.android.server.wm.WindowState winCandidate3 = this.mDisplayPolicyWrapper.getExtImpl().updateSystemBarWindow(this.mFocusedWindow, winCandidate2);
            if ((winCandidate3.getAttrs().privateFlags & 131072) != 0) {
                if (this.mNotificationShade != null && this.mNotificationShade.canReceiveKeys()) {
                    winCandidate3 = this.mNotificationShade;
                } else if (this.mLastFocusedWindow != null && this.mLastFocusedWindow.canReceiveKeys()) {
                    winCandidate3 = this.mLastFocusedWindow;
                } else {
                    winCandidate3 = this.mTopFullscreenOpaqueWindowState;
                }
                if (winCandidate3 == null) {
                    return;
                }
            }
            com.android.server.wm.WindowState win = winCandidate3;
            if (this.mDisplayPolicyWrapper.getExtImpl().skipSystemUiVisibility(win.getAttrs()) || this.mDisplayPolicyWrapper.getExtImpl().isDreamWindow(win.isDreamWindow())) {
                return;
            }
            this.mSystemUiControllingWindow = win;
            final int displayId = getDisplayId();
            final int disableFlags = win.getDisableFlags();
            int opaqueAppearance = updateSystemBarsLw(win, disableFlags);
            if (this.mRelaunchingSystemBarColorApps.isEmpty()) {
                com.android.server.wm.WindowState navColorWin = chooseNavigationColorWindowLw(this.mNavBarColorWindowCandidate, this.mDisplayContent.mInputMethodWindow, this.mNavigationBarPosition);
                final boolean isNavbarColorManagedByIme = navColorWin != null && navColorWin == this.mDisplayContent.mInputMethodWindow;
                final int appearance = updateLightNavigationBarLw(win.mAttrs.insetsFlags.appearance, navColorWin) | opaqueAppearance;
                if (topAppHidesSystemBar(android.view.WindowInsets.Type.navigationBars())) {
                    windowState2 = this.mTopFullscreenOpaqueWindowState;
                } else {
                    windowState2 = win;
                }
                com.android.server.wm.WindowState navBarControlWin = windowState2;
                final int behavior = navBarControlWin.mAttrs.insetsFlags.behavior;
                final java.lang.String focusedApp = win.mAttrs.packageName;
                boolean isFullscreen = (win.isRequestedVisible(android.view.WindowInsets.Type.statusBars()) && win.isRequestedVisible(android.view.WindowInsets.Type.navigationBars())) ? false : true;
                final com.android.internal.view.AppearanceRegion[] statusBarAppearanceRegions = new com.android.internal.view.AppearanceRegion[this.mStatusBarAppearanceRegionList.size()];
                this.mStatusBarAppearanceRegionList.toArray(statusBarAppearanceRegions);
                if (this.mLastDisableFlags != disableFlags) {
                    this.mLastDisableFlags = disableFlags;
                    final java.lang.String cause = win.toString();
                    callStatusBarSafely(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda8
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.statusbar.StatusBarManagerInternal) obj).setDisableFlags(displayId, disableFlags, cause);
                        }
                    });
                }
                final int requestedVisibleTypes = win.getRequestedVisibleTypes();
                final com.android.internal.statusbar.LetterboxDetails[] letterboxDetails = new com.android.internal.statusbar.LetterboxDetails[this.mLetterboxDetails.size()];
                this.mLetterboxDetails.toArray(letterboxDetails);
                if (this.mLastAppearance == appearance && this.mLastBehavior == behavior && this.mLastRequestedVisibleTypes == requestedVisibleTypes && java.util.Objects.equals(this.mFocusedApp, focusedApp) && this.mLastFocusIsFullscreen == isFullscreen && java.util.Arrays.equals(this.mLastStatusBarAppearanceRegions, statusBarAppearanceRegions) && java.util.Arrays.equals(this.mLastLetterboxDetails, letterboxDetails)) {
                    return;
                }
                if (this.mDisplayContent.isDefaultDisplay && (this.mLastFocusIsFullscreen != isFullscreen || ((this.mLastAppearance ^ appearance) & 4) != 0)) {
                    this.mService.mInputManager.setSystemUiLightsOut(isFullscreen || (appearance & 4) != 0);
                }
                this.mLastAppearance = appearance;
                this.mLastBehavior = behavior;
                this.mLastRequestedVisibleTypes = requestedVisibleTypes;
                this.mFocusedApp = focusedApp;
                this.mLastFocusIsFullscreen = isFullscreen;
                this.mLastStatusBarAppearanceRegions = statusBarAppearanceRegions;
                this.mLastLetterboxDetails = letterboxDetails;
                callStatusBarSafely(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda9
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.statusbar.StatusBarManagerInternal) obj).onSystemBarAttributesChanged(displayId, appearance, statusBarAppearanceRegions, isNavbarColorManagedByIme, behavior, requestedVisibleTypes, focusedApp, letterboxDetails);
                    }
                });
                this.mDisplayPolicyWrapper.getExtImpl().updateTaskBarAppearanceIfNeed(this.mTopFullscreenOpaqueWindowState);
            }
        }
    }

    private void callStatusBarSafely(final java.util.function.Consumer<com.android.server.statusbar.StatusBarManagerInternal> consumer) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$callStatusBarSafely$7(consumer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$callStatusBarSafely$7(java.util.function.Consumer consumer) {
        com.android.server.statusbar.StatusBarManagerInternal statusBar = getStatusBarManagerInternal();
        if (statusBar != null) {
            consumer.accept(statusBar);
        }
    }

    static com.android.server.wm.WindowState chooseNavigationColorWindowLw(com.android.server.wm.WindowState candidate, com.android.server.wm.WindowState imeWindow, int navBarPosition) {
        boolean imeWindowCanNavColorWindow = imeWindow != null && imeWindow.isVisible() && navBarPosition == 4 && (imeWindow.mAttrs.flags & Integer.MIN_VALUE) != 0;
        if (!imeWindowCanNavColorWindow) {
            return candidate;
        }
        if (candidate == null || !candidate.isDimming() || android.view.WindowManager.LayoutParams.mayUseInputMethod(candidate.mAttrs.flags)) {
            return imeWindow;
        }
        return candidate;
    }

    int updateLightNavigationBarLw(int appearance, com.android.server.wm.WindowState navColorWin) {
        if (navColorWin == null || !isLightBarAllowed(navColorWin, android.view.WindowInsets.Type.navigationBars())) {
            return appearance & (-17);
        }
        return (appearance & (-17)) | (navColorWin.mAttrs.insetsFlags.appearance & 16);
    }

    private int updateSystemBarsLw(com.android.server.wm.WindowState win, int disableFlags) {
        com.android.server.statusbar.StatusBarManagerInternal statusBar;
        com.android.server.wm.TaskDisplayArea defaultTaskDisplayArea = this.mDisplayContent.getDefaultTaskDisplayArea();
        boolean adjacentTasksVisible = defaultTaskDisplayArea.getRootTask(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$updateSystemBarsLw$8((com.android.server.wm.Task) obj);
            }
        }) != null;
        boolean freeformRootTaskVisible = defaultTaskDisplayArea.isRootTaskVisible(5);
        getInsetsPolicy().updateSystemBars(win, adjacentTasksVisible, freeformRootTaskVisible);
        boolean topAppHidesStatusBar = topAppHidesSystemBar(android.view.WindowInsets.Type.statusBars());
        if (getStatusBar() != null && (statusBar = getStatusBarManagerInternal()) != null) {
            statusBar.setTopAppHidesStatusBar(topAppHidesStatusBar);
        }
        this.mTopIsFullscreen = topAppHidesStatusBar && (this.mNotificationShade == null || !this.mNotificationShade.isVisible());
        int appearance = configureNavBarOpacity(configureStatusBarOpacity(3), adjacentTasksVisible, freeformRootTaskVisible);
        if (this.mDisplayPolicyWrapper.getExtImpl().opaqueNavBar(this.mTopFullscreenOpaqueWindowState)) {
            appearance |= 2;
        }
        boolean wasImmersiveMode = this.mIsImmersiveMode;
        final boolean isImmersiveMode = isImmersiveMode(win);
        if (wasImmersiveMode != isImmersiveMode) {
            this.mIsImmersiveMode = isImmersiveMode;
            com.android.server.wm.RootDisplayArea root = win.getRootDisplayArea();
            final int rootDisplayAreaId = root == null ? -1 : root.mFeatureId;
            if (android.view.ViewRootImpl.CLIENT_TRANSIENT || android.view.ViewRootImpl.CLIENT_IMMERSIVE_CONFIRMATION) {
                callStatusBarSafely(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda13
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.statusbar.StatusBarManagerInternal) obj).immersiveModeChanged(rootDisplayAreaId, isImmersiveMode);
                    }
                });
            } else {
                this.mImmersiveModeConfirmation.immersiveModeChangedLw(rootDisplayAreaId, isImmersiveMode, this.mService.mPolicy.isUserSetupComplete(), isNavBarEmpty(disableFlags));
            }
        }
        boolean requestHideNavBar = !win.isRequestedVisible(android.view.WindowInsets.Type.navigationBars());
        long now = android.os.SystemClock.uptimeMillis();
        boolean pendingPanic = this.mPendingPanicGestureUptime != 0 && now - this.mPendingPanicGestureUptime <= 30000;
        com.android.server.wm.DisplayPolicy defaultDisplayPolicy = this.mService.getDefaultDisplayContentLocked().getDisplayPolicy();
        if (pendingPanic && requestHideNavBar && isImmersiveMode && defaultDisplayPolicy.isKeyguardDrawComplete()) {
            this.mPendingPanicGestureUptime = 0L;
            if (!isNavBarEmpty(disableFlags)) {
                this.mDisplayContent.getInsetsPolicy().showTransient(SHOW_TYPES_FOR_PANIC, true);
            }
        }
        return appearance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateSystemBarsLw$8(com.android.server.wm.Task task) {
        return task.isVisible() && !task.getWrapper().getExtImpl().getLaunchedFromMultiSearch() && this.mDisplayPolicyWrapper.getExtImpl().isNeedForceShowSystemBarsWhenSplit() && task.getTopLeafTask().getWindowingMode() == 6 && task.getTopLeafTask().getAdjacentTask() != null;
    }

    private static boolean isLightBarAllowed(com.android.server.wm.WindowState win, int type) {
        if (win == null) {
            return false;
        }
        return intersectsAnyInsets(win.getFrame(), win.getInsetsState(), type);
    }

    private android.graphics.Rect getBarContentFrameForWindow(com.android.server.wm.WindowState win, int type) {
        com.android.server.wm.DisplayFrames displayFrames = win.getDisplayFrames(this.mDisplayContent.mDisplayFrames);
        android.view.InsetsState state = displayFrames.mInsetsState;
        android.graphics.Rect df = displayFrames.mUnrestricted;
        android.graphics.Rect safe = sTmpDisplayCutoutSafe;
        android.graphics.Insets waterfallInsets = state.getDisplayCutout().getWaterfallInsets();
        android.graphics.Rect outRect = new android.graphics.Rect();
        android.graphics.Rect sourceContent = sTmpRect;
        safe.set(displayFrames.mDisplayCutoutSafe);
        for (int i = state.sourceSize() - 1; i >= 0; i--) {
            android.view.InsetsSource source = state.sourceAt(i);
            if (source != null && source.getType() == type) {
                if (type == android.view.WindowInsets.Type.statusBars()) {
                    safe.set(displayFrames.mDisplayCutoutSafe);
                    android.graphics.Insets insets = source.calculateInsets(df, true);
                    if (insets.left > 0) {
                        safe.left = java.lang.Math.max(df.left + waterfallInsets.left, df.left);
                    } else if (insets.top > 0) {
                        safe.top = java.lang.Math.max(df.top + waterfallInsets.top, df.top);
                    } else if (insets.right > 0) {
                        safe.right = java.lang.Math.max(df.right - waterfallInsets.right, df.right);
                    } else if (insets.bottom > 0) {
                        safe.bottom = java.lang.Math.max(df.bottom - waterfallInsets.bottom, df.bottom);
                    }
                }
                sourceContent.set(source.getFrame());
                sourceContent.intersect(safe);
                outRect.union(sourceContent);
            }
        }
        return outRect;
    }

    boolean isFullyTransparentAllowed(com.android.server.wm.WindowState win, int type) {
        if (win == null) {
            return true;
        }
        return win.isFullyTransparentBarAllowed(getBarContentFrameForWindow(win, type));
    }

    private static boolean drawsBarBackground(com.android.server.wm.WindowState win) {
        if (win == null) {
            return true;
        }
        boolean drawsSystemBars = (win.getAttrs().flags & Integer.MIN_VALUE) != 0;
        boolean forceDrawsSystemBars = (win.getAttrs().privateFlags & 32768) != 0;
        return forceDrawsSystemBars || drawsSystemBars;
    }

    private int configureStatusBarOpacity(int appearance) {
        boolean drawBackground = true;
        boolean isFullyTransparentAllowed = true;
        for (int i = this.mStatusBarBackgroundWindows.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState window = this.mStatusBarBackgroundWindows.get(i);
            drawBackground &= drawsBarBackground(window);
            isFullyTransparentAllowed &= isFullyTransparentAllowed(window, android.view.WindowInsets.Type.statusBars());
        }
        if (drawBackground && !this.mDisplayPolicyWrapper.getExtImpl().makeStatusBarOpaque(this.mDisplayContent)) {
            appearance &= -2;
        }
        if (!isFullyTransparentAllowed) {
            return appearance | 32;
        }
        return appearance;
    }

    private int configureNavBarOpacity(int appearance, boolean multiWindowTaskVisible, boolean freeformRootTaskVisible) {
        com.android.server.wm.WindowState navBackgroundWin = chooseNavigationBackgroundWindow(this.mNavBarBackgroundWindowCandidate, this.mDisplayContent.mInputMethodWindow, this.mNavigationBarPosition);
        boolean drawBackground = navBackgroundWin != null || this.mNavBarBackgroundWindowCandidate == null;
        if (this.mNavBarOpacityMode == 2) {
            if (drawBackground) {
                appearance = clearNavBarOpaqueFlag(appearance);
            }
        } else if (this.mNavBarOpacityMode != 0) {
            if (this.mNavBarOpacityMode == 1 && freeformRootTaskVisible) {
                appearance = clearNavBarOpaqueFlag(appearance);
            }
        } else if (multiWindowTaskVisible || freeformRootTaskVisible || this.mDisplayPolicyWrapper.getExtImpl().isSplitTaskVisible(this.mDisplayContent)) {
            if (this.mIsFreeformWindowOverlappingWithNavBar || this.mDisplayPolicyWrapper.getExtImpl().isMinimized(this.mDisplayContent) || this.mDisplayPolicyWrapper.getExtImpl().isWaitForExitSplit()) {
                appearance = clearNavBarOpaqueFlag(appearance);
            }
        } else if (drawBackground) {
            appearance = clearNavBarOpaqueFlag(appearance);
        }
        if (!isFullyTransparentAllowed(navBackgroundWin, android.view.WindowInsets.Type.navigationBars())) {
            return appearance | 64;
        }
        return appearance;
    }

    private int clearNavBarOpaqueFlag(int appearance) {
        return appearance & (-3);
    }

    static com.android.server.wm.WindowState chooseNavigationBackgroundWindow(com.android.server.wm.WindowState candidate, com.android.server.wm.WindowState imeWindow, int navBarPosition) {
        if (imeWindow != null && imeWindow.isVisible() && navBarPosition == 4 && drawsBarBackground(imeWindow)) {
            return imeWindow;
        }
        if (drawsBarBackground(candidate)) {
            return candidate;
        }
        return null;
    }

    private boolean isImmersiveMode(com.android.server.wm.WindowState win) {
        if (win == null || win.mPolicy.getWindowLayerLw(win) > win.mPolicy.getWindowLayerFromTypeLw(2000) || win.isActivityTypeDream()) {
            return false;
        }
        return getInsetsPolicy().hasHiddenSources(android.view.WindowInsets.Type.navigationBars());
    }

    private static boolean isNavBarEmpty(int systemUiFlags) {
        return (systemUiFlags & 23068672) == 23068672;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPowerKeyDown(boolean isScreenOn) {
        boolean panic;
        if (!android.view.ViewRootImpl.CLIENT_TRANSIENT && !android.view.ViewRootImpl.CLIENT_IMMERSIVE_CONFIRMATION) {
            panic = this.mImmersiveModeConfirmation.onPowerKeyDown(isScreenOn, android.os.SystemClock.elapsedRealtime(), isImmersiveMode(this.mSystemUiControllingWindow), isNavBarEmpty(this.mLastDisableFlags));
        } else {
            panic = isPowerKeyDownPanic(isScreenOn, android.os.SystemClock.elapsedRealtime(), isImmersiveMode(this.mSystemUiControllingWindow), isNavBarEmpty(this.mLastDisableFlags));
        }
        if (panic) {
            this.mHandler.post(this.mHiddenNavPanic);
        }
    }

    private boolean isPowerKeyDownPanic(boolean isScreenOn, long time, boolean inImmersiveMode, boolean navBarEmpty) {
        if (!isScreenOn && time - this.mPanicTime < this.mPanicThresholdMs) {
            return !this.mImmersiveConfirmationWindowExists;
        }
        if (isScreenOn && inImmersiveMode && !navBarEmpty) {
            this.mPanicTime = time;
            return false;
        }
        this.mPanicTime = 0L;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onVrStateChangedLw(boolean enabled) {
        if (!android.view.ViewRootImpl.CLIENT_TRANSIENT && !android.view.ViewRootImpl.CLIENT_IMMERSIVE_CONFIRMATION) {
            this.mImmersiveModeConfirmation.onVrStateChangedLw(enabled);
        }
    }

    public void onLockTaskStateChangedLw(int lockTaskState) {
        if (!android.view.ViewRootImpl.CLIENT_TRANSIENT && !android.view.ViewRootImpl.CLIENT_IMMERSIVE_CONFIRMATION) {
            this.mImmersiveModeConfirmation.onLockTaskModeChangedLw(lockTaskState);
        }
    }

    public void onUserActivityEventTouch() {
        if (this.mAwake) {
            return;
        }
        com.android.server.wm.WindowState w = this.mNotificationShade;
        this.mService.mAtmService.setProcessAnimatingWhileDozing(w != null ? w.getProcess() : null);
    }

    boolean onSystemUiSettingsChanged() {
        if (android.view.ViewRootImpl.CLIENT_TRANSIENT || android.view.ViewRootImpl.CLIENT_IMMERSIVE_CONFIRMATION) {
            return false;
        }
        return this.mImmersiveModeConfirmation.onSettingChanged(this.mService.mCurrentUserId);
    }

    public void takeScreenshot(int screenshotType, int source) {
        if (this.mScreenshotHelper != null) {
            com.android.internal.util.ScreenshotRequest request = new com.android.internal.util.ScreenshotRequest.Builder(screenshotType, source).build();
            this.mScreenshotHelper.takeScreenshot(request, this.mHandler, (java.util.function.Consumer) null);
        }
    }

    com.android.server.wm.RefreshRatePolicy getRefreshRatePolicy() {
        return this.mRefreshRatePolicy;
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.println("DisplayPolicy");
        java.lang.String prefix2 = prefix + "  ";
        java.lang.String prefixInner = prefix2 + "  ";
        pw.print(prefix2);
        pw.print("mCarDockEnablesAccelerometer=");
        pw.print(this.mCarDockEnablesAccelerometer);
        pw.print(" mDeskDockEnablesAccelerometer=");
        pw.println(this.mDeskDockEnablesAccelerometer);
        pw.print(prefix2);
        pw.print("mDockMode=");
        pw.print(android.content.Intent.dockStateToString(this.mDockMode));
        pw.print(" mLidState=");
        pw.println(com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs.lidStateToString(this.mLidState));
        pw.print(prefix2);
        pw.print("mAwake=");
        pw.print(this.mAwake);
        pw.print(" mScreenOnEarly=");
        pw.print(this.mScreenOnEarly);
        pw.print(" mScreenOnFully=");
        pw.println(this.mScreenOnFully);
        pw.print(prefix2);
        pw.print("mKeyguardDrawComplete=");
        pw.print(this.mKeyguardDrawComplete);
        pw.print(" mWindowManagerDrawComplete=");
        pw.println(this.mWindowManagerDrawComplete);
        pw.print(prefix2);
        pw.print("mHdmiPlugged=");
        pw.println(this.mHdmiPlugged);
        pw.println(" mPersistentVrModeEnabled=" + this.mPersistentVrModeEnabled);
        if (this.mLastDisableFlags != 0) {
            pw.print(prefix2);
            pw.print("mLastDisableFlags=0x");
            pw.println(java.lang.Integer.toHexString(this.mLastDisableFlags));
        }
        if (this.mLastAppearance != 0) {
            pw.print(prefix2);
            pw.print("mLastAppearance=");
            pw.println(android.view.ViewDebug.flagsToString(android.view.InsetsFlags.class, "appearance", this.mLastAppearance));
        }
        if (this.mLastBehavior != 0) {
            pw.print(prefix2);
            pw.print("mLastBehavior=");
            pw.println(android.view.ViewDebug.flagsToString(android.view.InsetsFlags.class, "behavior", this.mLastBehavior));
        }
        pw.print(prefix2);
        pw.print("mShowingDream=");
        pw.print(this.mShowingDream);
        pw.print(" mDreamingLockscreen=");
        pw.println(this.mDreamingLockscreen);
        if (this.mStatusBar != null) {
            pw.print(prefix2);
            pw.print("mStatusBar=");
            pw.println(this.mStatusBar);
        }
        if (this.mNotificationShade != null) {
            pw.print(prefix2);
            pw.print("mExpandedPanel=");
            pw.println(this.mNotificationShade);
        }
        pw.print(prefix2);
        pw.print("isKeyguardShowing=");
        pw.println(isKeyguardShowing());
        if (this.mNavigationBar != null) {
            pw.print(prefix2);
            pw.print("mNavigationBar=");
            pw.println(this.mNavigationBar);
            pw.print(prefix2);
            pw.print("mNavBarOpacityMode=");
            pw.println(this.mNavBarOpacityMode);
            pw.print(prefix2);
            pw.print("mNavigationBarCanMove=");
            pw.println(this.mNavigationBarCanMove);
            pw.print(prefix2);
            pw.print("mNavigationBarPosition=");
            pw.println(this.mNavigationBarPosition);
        }
        if (this.mLeftGestureHost != null) {
            pw.print(prefix2);
            pw.print("mLeftGestureHost=");
            pw.println(this.mLeftGestureHost);
        }
        if (this.mTopGestureHost != null) {
            pw.print(prefix2);
            pw.print("mTopGestureHost=");
            pw.println(this.mTopGestureHost);
        }
        if (this.mRightGestureHost != null) {
            pw.print(prefix2);
            pw.print("mRightGestureHost=");
            pw.println(this.mRightGestureHost);
        }
        if (this.mBottomGestureHost != null) {
            pw.print(prefix2);
            pw.print("mBottomGestureHost=");
            pw.println(this.mBottomGestureHost);
        }
        if (this.mFocusedWindow != null) {
            pw.print(prefix2);
            pw.print("mFocusedWindow=");
            pw.println(this.mFocusedWindow);
        }
        if (this.mTopFullscreenOpaqueWindowState != null) {
            pw.print(prefix2);
            pw.print("mTopFullscreenOpaqueWindowState=");
            pw.println(this.mTopFullscreenOpaqueWindowState);
        }
        if (!this.mSystemBarColorApps.isEmpty()) {
            pw.print(prefix2);
            pw.print("mSystemBarColorApps=");
            pw.println(this.mSystemBarColorApps);
        }
        if (!this.mRelaunchingSystemBarColorApps.isEmpty()) {
            pw.print(prefix2);
            pw.print("mRelaunchingSystemBarColorApps=");
            pw.println(this.mRelaunchingSystemBarColorApps);
        }
        if (this.mNavBarColorWindowCandidate != null) {
            pw.print(prefix2);
            pw.print("mNavBarColorWindowCandidate=");
            pw.println(this.mNavBarColorWindowCandidate);
        }
        if (this.mNavBarBackgroundWindowCandidate != null) {
            pw.print(prefix2);
            pw.print("mNavBarBackgroundWindowCandidate=");
            pw.println(this.mNavBarBackgroundWindowCandidate);
        }
        if (this.mLastStatusBarAppearanceRegions != null) {
            pw.print(prefix2);
            pw.println("mLastStatusBarAppearanceRegions=");
            for (int i = this.mLastStatusBarAppearanceRegions.length - 1; i >= 0; i--) {
                pw.print(prefixInner);
                pw.println(this.mLastStatusBarAppearanceRegions[i]);
            }
        }
        if (this.mLastLetterboxDetails != null) {
            pw.print(prefix2);
            pw.println("mLastLetterboxDetails=");
            for (int i2 = this.mLastLetterboxDetails.length - 1; i2 >= 0; i2--) {
                pw.print(prefixInner);
                pw.println(this.mLastLetterboxDetails[i2]);
            }
        }
        if (!this.mStatusBarBackgroundWindows.isEmpty()) {
            pw.print(prefix2);
            pw.println("mStatusBarBackgroundWindows=");
            for (int i3 = this.mStatusBarBackgroundWindows.size() - 1; i3 >= 0; i3--) {
                com.android.server.wm.WindowState win = this.mStatusBarBackgroundWindows.get(i3);
                pw.print(prefixInner);
                pw.println(win);
            }
        }
        pw.print(prefix2);
        pw.print("mTopIsFullscreen=");
        pw.println(this.mTopIsFullscreen);
        pw.print(prefix2);
        pw.print("mImeInsetsConsumed=");
        pw.println(this.mImeInsetsConsumed);
        pw.print(prefix2);
        pw.print("mForceShowNavigationBarEnabled=");
        pw.print(this.mForceShowNavigationBarEnabled);
        pw.print(" mAllowLockscreenWhenOn=");
        pw.println(this.mAllowLockscreenWhenOn);
        pw.print(prefix2);
        pw.print("mRemoteInsetsControllerControlsSystemBars=");
        pw.println(this.mRemoteInsetsControllerControlsSystemBars);
        pw.print(prefix2);
        pw.println("mDecorInsetsInfo:");
        this.mDecorInsets.dump(prefixInner, pw);
        if (this.mCachedDecorInsets != null) {
            pw.print(prefix2);
            pw.println("mCachedDecorInsets:");
            this.mCachedDecorInsets.mDecorInsets.dump(prefixInner, pw);
        }
        if (!android.view.ViewRootImpl.CLIENT_TRANSIENT) {
            this.mSystemGestures.dump(pw, prefix2);
        }
    }

    private boolean supportsPointerLocation() {
        if (!this.mDisplayContent.getWrapper().getExtImpl().isTwoScreenShown()) {
            return this.mDisplayContent.isDefaultDisplay || !this.mDisplayContent.isPrivate();
        }
        android.util.Slog.d(TAG, "supportsPointerLocation false as Two Screen Shown");
        return false;
    }

    void setPointerLocationEnabled(boolean pointerLocationEnabled) {
        if (!supportsPointerLocation()) {
            return;
        }
        this.mHandler.sendEmptyMessage(pointerLocationEnabled ? 4 : 5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enablePointerLocation() {
        if (this.mPointerLocationView != null) {
            return;
        }
        if (this.mDisplayContent.getWrapper().getExtImpl().interceptPointerLocationEnable(this.mDisplayContent)) {
            android.util.Slog.d(TAG, "interceptPointerLocationEnable");
            return;
        }
        this.mPointerLocationView = new com.android.internal.widget.PointerLocationView(this.mContext);
        this.mPointerLocationView.setPrintCoords(false);
        android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams();
        lp.type = 2015;
        lp.flags = 280;
        lp.privateFlags |= 16;
        lp.setFitInsetsTypes(0);
        lp.layoutInDisplayCutoutMode = 3;
        if (android.app.ActivityManager.isHighEndGfx()) {
            lp.flags |= 16777216;
            lp.privateFlags |= 2;
        }
        lp.format = -3;
        lp.setTitle("PointerLocation - display " + getDisplayId());
        lp.inputFeatures |= 1;
        android.view.WindowManager wm = (android.view.WindowManager) this.mContext.getSystemService(android.view.WindowManager.class);
        try {
            wm.addView(this.mPointerLocationView, lp);
            this.mDisplayContent.registerPointerEventListener(this.mPointerLocationView);
        } catch (android.view.WindowManager.InvalidDisplayException e) {
            android.util.Slog.d(TAG, "enablePointerLocation InvalidDisplayException!");
            this.mPointerLocationView = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disablePointerLocation() {
        if (this.mPointerLocationView == null) {
            return;
        }
        if (!this.mDisplayContent.isRemoved()) {
            this.mDisplayContent.unregisterPointerEventListener(this.mPointerLocationView);
        }
        android.view.WindowManager wm = (android.view.WindowManager) this.mContext.getSystemService(android.view.WindowManager.class);
        wm.removeView(this.mPointerLocationView);
        this.mPointerLocationView = null;
    }

    boolean isWindowExcludedFromContent(com.android.server.wm.WindowState w) {
        if (w != null && this.mPointerLocationView != null) {
            try {
                return w.mClient == this.mPointerLocationView.getWindowToken();
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    void release() {
        this.mDisplayContent.mTransitionController.unregisterLegacyListener(this.mAppTransitionListener);
        android.os.Handler handler = this.mHandler;
        final com.android.internal.policy.GestureNavigationSettingsObserver gestureNavigationSettingsObserver = this.mGestureNavigationSettingsObserver;
        java.util.Objects.requireNonNull(gestureNavigationSettingsObserver);
        handler.post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                gestureNavigationSettingsObserver.unregister();
            }
        });
        android.os.Handler handler2 = this.mHandler;
        final com.android.internal.policy.ForceShowNavBarSettingsObserver forceShowNavBarSettingsObserver = this.mForceShowNavBarSettingsObserver;
        java.util.Objects.requireNonNull(forceShowNavBarSettingsObserver);
        handler2.post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayPolicy$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                forceShowNavBarSettingsObserver.unregister();
            }
        });
        if (!android.view.ViewRootImpl.CLIENT_TRANSIENT && !android.view.ViewRootImpl.CLIENT_IMMERSIVE_CONFIRMATION) {
            this.mImmersiveModeConfirmation.release();
        }
        if (this.mService.mPointerLocationEnabled) {
            setPointerLocationEnabled(false);
        }
    }

    static boolean isOverlappingWithNavBar(com.android.server.wm.WindowState win) {
        if (!win.isVisible()) {
            return false;
        }
        return intersectsAnyInsets(win.isDimming() ? win.getBounds() : win.getFrame(), win.getInsetsState(), android.view.WindowInsets.Type.navigationBars());
    }

    private static boolean intersectsAnyInsets(android.graphics.Rect bounds, android.view.InsetsState insetsState, int insetsType) {
        for (int i = insetsState.sourceSize() - 1; i >= 0; i--) {
            android.view.InsetsSource source = insetsState.sourceAt(i);
            if ((source.getType() & insetsType) != 0 && source.isVisible() && android.graphics.Rect.intersects(bounds, source.getFrame())) {
                return true;
            }
        }
        return false;
    }

    boolean shouldAttachNavBarToAppDuringTransition() {
        return this.mShouldAttachNavBarToAppDuringTransition && this.mNavigationBar != null;
    }

    public com.android.server.wm.IDisplayPolicyWrapper getWrapper() {
        return this.mDisplayPolicyWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DisplayPolicyWrapper implements com.android.server.wm.IDisplayPolicyWrapper {
        private com.android.server.wm.IDisplayPolicyExt mDisplayPolicyExt;
        private com.android.server.wm.IDisplayPolicySocExt mDisplayPolicySocExt;

        private DisplayPolicyWrapper() {
            this.mDisplayPolicyExt = (com.android.server.wm.IDisplayPolicyExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDisplayPolicyExt.class).base(com.android.server.wm.DisplayPolicy.this).create();
            this.mDisplayPolicySocExt = (com.android.server.wm.IDisplayPolicySocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDisplayPolicySocExt.class).base(com.android.server.wm.DisplayPolicy.this).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.IDisplayPolicySocExt getSocExtImpl() {
            return this.mDisplayPolicySocExt;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public com.android.server.wm.IDisplayPolicyExt getExtImpl() {
            return this.mDisplayPolicyExt;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public com.android.internal.util.ScreenshotHelper getScreenshotHelper() {
            return com.android.server.wm.DisplayPolicy.this.mScreenshotHelper;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public java.lang.Object getServiceAcquireLock() {
            return com.android.server.wm.DisplayPolicy.this.mServiceAcquireLock;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public android.os.Handler getHandler() {
            return com.android.server.wm.DisplayPolicy.this.mHandler;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public com.android.server.wm.WindowManagerService getWindowManagerService() {
            return com.android.server.wm.DisplayPolicy.this.mService;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public com.android.server.wm.WindowState getFocusedWindow() {
            return com.android.server.wm.DisplayPolicy.this.mFocusedWindow;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public android.view.WindowLayout getWindowLayout() {
            return com.android.server.wm.DisplayPolicy.this.mWindowLayout;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public android.window.ClientWindowFrames getTmpClientFrames() {
            return com.android.server.wm.DisplayPolicy.sTmpClientFrames;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public com.android.server.wm.DisplayContent getDisplayContent() {
            return com.android.server.wm.DisplayPolicy.this.mDisplayContent;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public void setBottomGestureAdditionalInset(int height) {
            com.android.server.wm.DisplayPolicy.this.mBottomGestureAdditionalInset = height;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public int getBottomGestureAdditionalInset() {
            return com.android.server.wm.DisplayPolicy.this.mBottomGestureAdditionalInset;
        }

        @Override // com.android.server.wm.IDisplayPolicyWrapper
        public int getNavBarFrameHeight(int rotation) {
            return com.android.server.wm.DisplayPolicy.this.getNavigationBarFrameHeight(rotation);
        }
    }
}
