package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DisplayContent extends com.android.server.wm.RootDisplayArea implements com.android.server.policy.WindowManagerPolicy.DisplayContentInfo {
    private static final long FIXED_ROTATION_HIDE_ANIMATION_DEBOUNCE_DELAY_MS = 250;
    static final int FORCE_SCALING_MODE_AUTO = 0;
    static final int FORCE_SCALING_MODE_DISABLED = 1;
    static final int IME_TARGET_CONTROL = 2;
    static final int IME_TARGET_LAYERING = 0;
    static final float INVALID_DPI = 0.0f;
    private static final java.lang.String TAG = "WindowManager";
    private static final java.lang.String TAG_FIXED = "TestFixed";
    boolean isDefaultDisplay;
    private android.view.SurfaceControl mA11yOverlayLayer;
    private java.util.Set<com.android.server.wm.ActivityRecord> mActiveSizeCompatActivities;
    final com.android.server.wm.ActivityRefresher mActivityRefresher;
    final java.util.ArrayList<com.android.server.wm.RootWindowContainer.SleepToken> mAllSleepTokens;
    final com.android.server.wm.AppTransition mAppTransition;
    final com.android.server.wm.AppTransitionController mAppTransitionController;
    private final java.util.function.Consumer<com.android.server.wm.WindowState> mApplyPostLayoutPolicy;
    private final java.util.function.Consumer<com.android.server.wm.WindowState> mApplySurfaceChangesTransaction;
    private com.android.server.wm.AsyncRotationController mAsyncRotationController;
    private java.lang.Runnable mAsyncRotationRunnable;
    final com.android.server.wm.ActivityTaskManagerService mAtmService;
    android.view.DisplayCutout mBaseDisplayCutout;
    int mBaseDisplayDensity;
    int mBaseDisplayHeight;
    float mBaseDisplayPhysicalXDpi;
    float mBaseDisplayPhysicalYDpi;
    int mBaseDisplayWidth;
    android.view.RoundedCorners mBaseRoundedCorners;
    final com.android.server.wm.CameraCompatFreeformPolicy mCameraCompatFreeformPolicy;
    final com.android.server.wm.CameraStateMonitor mCameraStateMonitor;
    final android.util.ArraySet<com.android.server.wm.WindowContainer> mChangingContainers;
    final float mCloseToSquareMaxAspectRatio;
    final android.util.ArraySet<com.android.server.wm.ActivityRecord> mClosingApps;
    final android.util.ArrayMap<com.android.server.wm.WindowContainer, android.graphics.Rect> mClosingChangingContainers;
    private final android.util.DisplayMetrics mCompatDisplayMetrics;
    float mCompatibleScreenScale;
    private final java.util.function.Predicate<com.android.server.wm.WindowState> mComputeImeTargetPredicate;
    private com.android.server.wm.ContentRecorder mContentRecorder;
    com.android.server.wm.WindowState mCurrentFocus;
    private int mCurrentOverrideConfigurationChanges;
    android.view.PrivacyIndicatorBounds mCurrentPrivacyIndicatorBounds;
    java.lang.String mCurrentUniqueDisplayId;
    private final android.os.RemoteCallbackList<android.view.IDecorViewGestureListener> mDecorViewGestureListener;
    private int mDeferUpdateImeTargetCount;
    private boolean mDeferredRemoval;
    final java.util.function.Consumer<com.android.server.wm.DeviceStateController.DeviceState> mDeviceStateConsumer;
    final com.android.server.wm.DeviceStateController mDeviceStateController;
    final android.view.Display mDisplay;
    private android.util.IntArray mDisplayAccessUIDs;
    final com.android.server.wm.DisplayAreaPolicy mDisplayAreaPolicy;
    private com.android.server.wm.IDisplayContentExt mDisplayContentExt;
    private com.android.server.wm.DisplayContent.DisplayContentWrapper mDisplayContentWrapper;
    private final com.android.server.wm.utils.RotationCache<android.view.DisplayCutout, com.android.server.wm.utils.WmDisplayCutout> mDisplayCutoutCache;
    com.android.server.wm.DisplayFrames mDisplayFrames;
    final int mDisplayId;
    private final android.view.DisplayInfo mDisplayInfo;
    private final android.util.DisplayMetrics mDisplayMetrics;
    private final com.android.server.wm.DisplayPolicy mDisplayPolicy;
    private boolean mDisplayReady;
    private final com.android.server.wm.DisplayRotation mDisplayRotation;
    final com.android.server.wm.DisplayRotationCompatPolicy mDisplayRotationCompatPolicy;
    boolean mDisplayScalingDisabled;
    private final com.android.server.wm.utils.RotationCache<android.view.DisplayShape, android.view.DisplayShape> mDisplayShapeCache;
    final com.android.server.wm.PhysicalDisplaySwitchTransitionLauncher mDisplaySwitchTransitionLauncher;
    final com.android.server.wm.DisplayUpdater mDisplayUpdater;
    boolean mDontMoveToTop;
    com.android.server.wm.DisplayWindowPolicyControllerHelper mDwpcHelper;
    private final com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> mFindFocusedWindow;
    private com.android.server.wm.ActivityRecord mFixedRotationLaunchingApp;
    final com.android.server.wm.DisplayContent.FixedRotationTransitionListener mFixedRotationTransitionListener;
    com.android.server.wm.ActivityRecord mFocusedApp;
    private android.window.SystemPerformanceHinter.HighPerfSession mHighFrameRateSession;
    private android.os.PowerManager.WakeLock mHoldScreenWakeLock;
    private com.android.server.wm.WindowState mHoldScreenWindow;
    boolean mIgnoreDisplayCutout;
    private com.android.server.wm.InsetsControlTarget mImeControlTarget;
    private com.android.server.wm.InputTarget mImeInputTarget;
    private com.android.server.wm.WindowState mImeLayeringTarget;
    com.android.server.wm.DisplayContent.ImeScreenshot mImeScreenshot;
    private android.util.Pair<android.os.IBinder, com.android.server.wm.WindowContainerListener> mImeTargetTokenListenerPair;
    private final com.android.server.wm.DisplayContent.ImeContainer mImeWindowsContainer;
    private boolean mInEnsureActivitiesVisible;
    private boolean mInTouchMode;
    android.view.DisplayCutout mInitialDisplayCutout;
    int mInitialDisplayDensity;
    int mInitialDisplayHeight;
    android.view.DisplayShape mInitialDisplayShape;
    int mInitialDisplayWidth;
    float mInitialPhysicalXDpi;
    float mInitialPhysicalYDpi;
    android.view.RoundedCorners mInitialRoundedCorners;
    android.view.SurfaceControl mInputMethodSurfaceParent;
    com.android.server.wm.WindowState mInputMethodWindow;
    private com.android.server.wm.InputMonitor mInputMonitor;
    private android.view.SurfaceControl mInputOverlayLayer;
    private final com.android.server.wm.InsetsPolicy mInsetsPolicy;
    private final com.android.server.wm.InsetsStateController mInsetsStateController;
    boolean mIsDensityForced;
    boolean mIsSizeForced;
    boolean mLastContainsRunningSurfaceAnimator;
    private android.view.DisplayInfo mLastDisplayInfoOverride;
    private boolean mLastHasContent;
    private com.android.server.wm.InputTarget mLastImeInputTarget;
    private com.android.server.wm.WindowState mLastWakeLockHoldingWindow;
    private com.android.server.wm.WindowState mLastWakeLockObscuringWindow;
    private boolean mLastWallpaperVisible;
    private boolean mLayoutNeeded;
    int mLayoutSeq;
    private android.view.MagnificationSpec mMagnificationSpec;
    private int mMaxUiWidth;
    private com.android.internal.logging.MetricsLogger mMetricsLogger;
    int mMinSizeOfResizeableTaskDp;
    final java.util.List<android.os.IBinder> mNoAnimationNotifyOnTransitionFinished;
    private com.android.server.wm.INonStaticDisplayContentExt mNonStaticExt;
    private com.android.server.wm.WindowState mObscuringWindow;
    final android.util.ArraySet<com.android.server.wm.ActivityRecord> mOpeningApps;
    private com.android.server.wm.TaskDisplayArea mOrientationRequestingTaskDisplayArea;
    private android.view.SurfaceControl mOverlayLayer;
    private final java.util.function.Consumer<com.android.server.wm.WindowState> mPerformLayout;
    private final java.util.function.Consumer<com.android.server.wm.WindowState> mPerformLayoutAttached;
    private android.graphics.Point mPhysicalDisplaySize;
    final com.android.server.wm.PinnedTaskController mPinnedTaskController;
    private final com.android.server.wm.PointerEventDispatcher mPointerEventDispatcher;
    private final com.android.server.wm.utils.RotationCache<android.view.PrivacyIndicatorBounds, android.view.PrivacyIndicatorBounds> mPrivacyIndicatorBoundsCache;
    final android.util.DisplayMetrics mRealDisplayMetrics;
    final com.android.server.wm.RemoteDisplayChangeController mRemoteDisplayChangeController;
    com.android.server.wm.DisplayContent.RemoteInsetsControlTarget mRemoteInsetsControlTarget;
    private final android.os.IBinder.DeathRecipient mRemoteInsetsDeath;
    private boolean mRemoved;
    private boolean mRemoving;
    private java.util.Set<android.graphics.Rect> mRestrictedKeepClearAreas;
    private com.android.server.wm.RootWindowContainer mRootWindowContainer;
    private final com.android.server.wm.DisplayRotationReversionController mRotationReversionController;
    private final com.android.server.wm.utils.RotationCache<android.view.RoundedCorners, android.view.RoundedCorners> mRoundedCornerCache;
    private boolean mSandboxDisplayApis;
    private final java.util.function.Consumer<com.android.server.wm.WindowState> mScheduleToastTimeout;
    private com.android.server.wm.ScreenRotationAnimation mScreenRotationAnimation;
    private final android.view.SurfaceSession mSession;
    final android.util.SparseArray<com.android.server.wm.ShellRoot> mShellRoots;
    boolean mSkipAppTransitionAnimation;
    private boolean mSleeping;
    private final android.graphics.Region mSystemGestureExclusion;
    private int mSystemGestureExclusionLimit;
    private final android.os.RemoteCallbackList<android.view.ISystemGestureExclusionListener> mSystemGestureExclusionListeners;
    private final android.graphics.Region mSystemGestureExclusionUnrestricted;
    private boolean mSystemGestureExclusionWasRestricted;
    private final android.graphics.Rect mSystemGestureFrameLeft;
    private final android.graphics.Rect mSystemGestureFrameRight;
    private final android.content.res.Configuration mTempConfig;
    final android.view.WindowManagerPolicyConstants.PointerEventListener mTheiaPanicDetector;
    private final com.android.server.wm.DisplayContent.ApplySurfaceChangesTransactionState mTmpApplySurfaceChangesTransactionState;
    private final android.content.res.Configuration mTmpConfiguration;
    private final android.util.DisplayMetrics mTmpDisplayMetrics;
    private com.android.server.wm.WindowState mTmpHoldScreenWindow;
    private boolean mTmpInitial;
    private final android.graphics.Rect mTmpRect;
    private final android.graphics.Rect mTmpRect2;
    private final android.graphics.Region mTmpRegion;
    private final com.android.server.wm.DisplayContent.TaskForResizePointSearchResult mTmpTaskForResizePointSearchResult;
    private final java.util.LinkedList<com.android.server.wm.ActivityRecord> mTmpUpdateAllDrawn;
    private com.android.server.wm.WindowState mTmpWindow;
    private final java.util.HashMap<android.os.IBinder, com.android.server.wm.WindowToken> mTokenMap;
    private android.window.SystemPerformanceHinter.HighPerfSession mTransitionPrefSession;
    final com.android.server.wm.UnknownAppVisibilityController mUnknownAppVisibilityController;
    private java.util.Set<android.graphics.Rect> mUnrestrictedKeepClearAreas;
    private boolean mUpdateImeRequestedWhileDeferred;
    private boolean mUpdateImeTarget;
    private final java.util.function.Consumer<com.android.server.wm.WindowState> mUpdateWindowsForAnimator;
    private final boolean mVisibleBackgroundUserEnabled;
    boolean mWaitingForConfig;
    com.android.server.wm.WallpaperController mWallpaperController;
    boolean mWallpaperMayChange;
    final java.util.ArrayList<com.android.server.wm.WindowState> mWinAddedSinceNullFocus;
    final java.util.ArrayList<com.android.server.wm.WindowState> mWinRemovedSinceNullFocus;
    private final float mWindowCornerRadius;
    int pendingLayoutChanges;
    private static boolean DEBUG_PANIC = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final android.view.InsetsState.OnTraverseCallbacks COPY_SOURCE_VISIBILITY = new android.view.InsetsState.OnTraverseCallbacks() { // from class: com.android.server.wm.DisplayContent.1
        public void onIdMatch(android.view.InsetsSource source1, android.view.InsetsSource source2) {
            source1.setVisible(source2.isVisible());
        }
    };
    private static com.android.server.wm.IDisplayContentExt.IGestureStaticExt mStaticDisplayContentExt = (com.android.server.wm.IDisplayContentExt.IGestureStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDisplayContentExt.IGestureStaticExt.class).create();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface ForceScalingMode {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface InputMethodTarget {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mRemoteInsetsControlTarget = null;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(com.android.server.wm.WindowState w) {
        com.android.server.wm.WindowStateAnimator winAnimator = w.mWinAnimator;
        com.android.server.wm.ActivityRecord activity = w.mActivityRecord;
        if (winAnimator.mDrawState == 3) {
            if ((activity == null || activity.canShowWindows()) && w.performShowLocked()) {
                this.pendingLayoutChanges |= 8;
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                    this.mWmService.mWindowPlacerLocked.debugLayoutRepeats("updateWindowsAndWallpaperLocked 5", this.pendingLayoutChanges);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(com.android.server.wm.WindowState w) {
        int lostFocusUid = this.mTmpWindow.mOwnerUid;
        android.os.Handler handler = this.mWmService.mH;
        if (w.mAttrs.type == 2005 && w.mOwnerUid == lostFocusUid && !handler.hasMessages(52, w)) {
            handler.sendMessageDelayed(handler.obtainMessage(52, w), w.mAttrs.hideTimeoutMilliseconds);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$3(com.android.server.wm.WindowState w) {
        com.android.server.wm.ActivityRecord focusedApp = this.mFocusedApp;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(w);
            long protoLogParam1 = w.mAttrs.flags;
            boolean protoLogParam2 = w.canReceiveKeys();
            java.lang.String protoLogParam3 = java.lang.String.valueOf(w.canReceiveKeysReason(false));
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, 1432179297701477868L, 52, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2), protoLogParam3);
        }
        if (!com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS.isLogToLogcat() && android.view.DynamicLoggerObserver.isLogToolRun() && focusedApp != null && w.getActivityRecord() == focusedApp && w.mAttrs.type != 3) {
            android.util.Slog.d(TAG, "Looking for focus:" + w + " flags:" + w.mAttrs.flags + " canReceive:" + w.canReceiveKeys() + " reason:" + w.canReceiveKeysReason(false));
        }
        if (!w.canReceiveKeys()) {
            return false;
        }
        if (w.mIsImWindow && w.isChildWindow() && (this.mImeLayeringTarget == null || !this.mImeLayeringTarget.isRequestedVisible(android.view.WindowInsets.Type.ime()))) {
            return false;
        }
        if (w.mAttrs.type == 2012 && (this.mImeLayeringTarget == null || (!this.mImeLayeringTarget.isRequestedVisible(android.view.WindowInsets.Type.ime()) && (!this.mImeLayeringTarget.isVisibleRequested() || (!this.mTransitionController.isShellTransitionsEnabled() ? !this.mImeLayeringTarget.isAnimating(3, 1) : !this.mImeLayeringTarget.inTransition()))))) {
            return false;
        }
        com.android.server.wm.ActivityRecord activity = w.mActivityRecord;
        if (focusedApp == null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(w);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -1998969924927409574L, 0, null, protoLogParam02);
            }
            this.mTmpWindow = w;
            return true;
        }
        if (!focusedApp.windowsAreFocusable()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[1]) {
                java.lang.String protoLogParam03 = java.lang.String.valueOf(w);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -1513212297283619351L, 0, null, protoLogParam03);
            }
            this.mTmpWindow = w;
            return true;
        }
        if (activity != null && w.mAttrs.type != 3) {
            if (focusedApp.compareTo((com.android.server.wm.WindowContainer) activity) > 0 && !this.mDisplayContentExt.hookFocusWindowInQuickBack(null, focusedApp.findMainWindow(false))) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[1]) {
                    java.lang.String protoLogParam04 = java.lang.String.valueOf(focusedApp);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 271075236829935631L, 0, null, protoLogParam04);
                }
                this.mTmpWindow = null;
                return true;
            }
            if (this.mDisplayContentExt.setFocusedAppToNormalWindow(focusedApp, w)) {
                return false;
            }
            if (focusedApp.getTask() != null && !focusedApp.getTask().getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0]) && w.getTask() != null && w.getTask().getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
                return false;
            }
            com.android.server.wm.TaskFragment parent = activity.getTaskFragment();
            if (parent != null && parent.isEmbedded() && activity.getTask() == focusedApp.getTask() && activity.getTaskFragment() != focusedApp.getTaskFragment()) {
                return false;
            }
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[1]) {
            java.lang.String protoLogParam05 = java.lang.String.valueOf(w);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 3066566560703920191L, 0, null, protoLogParam05);
        }
        this.mTmpWindow = w;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(com.android.server.wm.WindowState w) {
        if (w.mLayoutAttached) {
            return;
        }
        boolean gone = w.isGoneForLayout();
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
            android.util.Slog.v(TAG, "1ST PASS " + w + ": gone=" + gone + " mHaveFrame=" + w.mHaveFrame + " config reported=" + w.isLastConfigReportedToClient());
            com.android.server.wm.ActivityRecord activity = w.mActivityRecord;
            if (gone) {
                android.util.Slog.v(TAG, "  GONE: mViewVisibility=" + w.mViewVisibility + " mRelayoutCalled=" + w.mRelayoutCalled + " visible=" + w.mToken.isVisible() + " visibleRequested=" + (activity != null && activity.isVisibleRequested()) + " parentHidden=" + w.isParentWindowHidden());
            } else {
                android.util.Slog.v(TAG, "  VIS: mViewVisibility=" + w.mViewVisibility + " mRelayoutCalled=" + w.mRelayoutCalled + " visible=" + w.mToken.isVisible() + " visibleRequested=" + (activity != null && activity.isVisibleRequested()) + " parentHidden=" + w.isParentWindowHidden());
            }
        }
        if (!gone || !w.mHaveFrame || w.mLayoutNeeded) {
            this.mDisplayContentExt.hookPerformLayout(this, w, this.mCurrentFocus);
            if (this.mTmpInitial) {
                w.resetContentChanged();
            }
            w.mSurfacePlacementNeeded = true;
            w.mLayoutNeeded = false;
            boolean firstLayout = !w.isLaidOut();
            getDisplayPolicy().layoutWindowLw(w, null, this.mDisplayFrames);
            w.mLayoutSeq = this.mLayoutSeq;
            if (firstLayout && !w.mHasSurface) {
                if (!w.getFrame().isEmpty()) {
                    w.updateLastFrames();
                    this.mWmService.mFrameChangingWindows.remove(w);
                }
                w.onResizeHandled();
            }
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
                android.util.Slog.v(TAG, "  LAYOUT: mFrame=" + w.getFrame() + " mParentFrame=" + w.getParentFrame() + " mDisplayFrame=" + w.getDisplayFrame());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(com.android.server.wm.WindowState w) {
        this.mDisplayContentExt.mayAddFloatingWindow(w);
        if (!w.mLayoutAttached) {
            return;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
            android.util.Slog.v(TAG, "2ND PASS " + w + " mHaveFrame=" + w.mHaveFrame + " mViewVisibility=" + w.mViewVisibility + " mRelayoutCalled=" + w.mRelayoutCalled);
        }
        if ((w.mViewVisibility != 8 && w.mRelayoutCalled) || !w.mHaveFrame || w.mLayoutNeeded) {
            if (this.mTmpInitial) {
                w.resetContentChanged();
            }
            w.mSurfacePlacementNeeded = true;
            w.mLayoutNeeded = false;
            getDisplayPolicy().layoutWindowLw(w, w.getParentWindow(), this.mDisplayFrames);
            w.mLayoutSeq = this.mLayoutSeq;
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
                android.util.Slog.v(TAG, " LAYOUT: mFrame=" + w.getFrame() + " mParentFrame=" + w.getParentFrame() + " mDisplayFrame=" + w.getDisplayFrame());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$6(com.android.server.wm.WindowState w) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD && this.mUpdateImeTarget) {
            android.util.Slog.i(TAG, "Checking window @" + w + " fl=0x" + java.lang.Integer.toHexString(w.mAttrs.flags));
        }
        return w.canBeImeTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(com.android.server.wm.WindowState w) {
        getDisplayPolicy().applyPostLayoutPolicyLw(w, w.mAttrs, w.getParentWindow(), this.mImeLayeringTarget);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(com.android.server.wm.WindowState w) {
        com.android.server.wm.WindowSurfacePlacer surfacePlacer = this.mWmService.mWindowPlacerLocked;
        com.android.server.wm.RootWindowContainer root = this.mWmService.mRoot;
        if (w.mHasSurface) {
            boolean committed = w.mWinAnimator.commitFinishDrawingLocked();
            if (this.isDefaultDisplay && committed && w.hasWallpaper()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(w);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, -8667452489821572603L, 0, null, protoLogParam0);
                }
                this.mWallpaperMayChange = true;
                this.pendingLayoutChanges |= 4;
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                    surfacePlacer.debugLayoutRepeats("wallpaper and commitFinishDrawingLocked true", this.pendingLayoutChanges);
                }
            }
        }
        w.mObscured = this.mTmpApplySurfaceChangesTransactionState.obscured;
        if (!this.mTmpApplySurfaceChangesTransactionState.obscured) {
            boolean isDisplayed = w.isDisplayed();
            if (isDisplayed && w.isObscuringDisplay()) {
                this.mObscuringWindow = w;
                this.mTmpApplySurfaceChangesTransactionState.obscured = true;
            }
            boolean displayHasContent = root.handleNotObscuredLocked(w, this.mTmpApplySurfaceChangesTransactionState.obscured, this.mTmpApplySurfaceChangesTransactionState.syswin);
            if (!this.mTmpApplySurfaceChangesTransactionState.displayHasContent && !getDisplayPolicy().isWindowExcludedFromContent(w)) {
                this.mTmpApplySurfaceChangesTransactionState.displayHasContent |= displayHasContent;
            }
            if (w.mHasSurface && isDisplayed) {
                if ((w.mAttrs.flags & 128) != 0) {
                    this.mTmpHoldScreenWindow = w;
                } else if (w == this.mLastWakeLockHoldingWindow && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_KEEP_SCREEN_ON_enabled[0]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(w);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(android.os.Debug.getCallers(10));
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, 6283995720623600346L, 0, null, protoLogParam02, protoLogParam1);
                }
                int type = w.mAttrs.type;
                if (type == 2008 || type == 2010 || (type == 2040 && this.mWmService.mPolicy.isKeyguardShowing())) {
                    this.mTmpApplySurfaceChangesTransactionState.syswin = true;
                }
                if (this.mTmpApplySurfaceChangesTransactionState.preferredRefreshRate == INVALID_DPI && w.mAttrs.preferredRefreshRate != INVALID_DPI) {
                    this.mTmpApplySurfaceChangesTransactionState.preferredRefreshRate = w.mAttrs.preferredRefreshRate;
                }
                this.mTmpApplySurfaceChangesTransactionState.preferMinimalPostProcessing |= w.mAttrs.preferMinimalPostProcessing;
                this.mTmpApplySurfaceChangesTransactionState.disableHdrConversion |= !w.mAttrs.isHdrConversionEnabled();
                int preferredModeId = getDisplayPolicy().getRefreshRatePolicy().getPreferredModeId(w);
                if (w.getWindowingMode() != 2 && this.mTmpApplySurfaceChangesTransactionState.preferredModeId == 0 && preferredModeId != 0) {
                    this.mTmpApplySurfaceChangesTransactionState.preferredModeId = preferredModeId;
                }
                float preferredMinRefreshRate = getDisplayPolicy().getRefreshRatePolicy().getPreferredMinRefreshRate(w);
                if (this.mTmpApplySurfaceChangesTransactionState.preferredMinRefreshRate == INVALID_DPI && preferredMinRefreshRate != INVALID_DPI) {
                    this.mTmpApplySurfaceChangesTransactionState.preferredMinRefreshRate = preferredMinRefreshRate;
                }
                float preferredMaxRefreshRate = getDisplayPolicy().getRefreshRatePolicy().getPreferredMaxRefreshRate(w);
                if (this.mTmpApplySurfaceChangesTransactionState.preferredMaxRefreshRate == INVALID_DPI && preferredMaxRefreshRate != INVALID_DPI) {
                    this.mTmpApplySurfaceChangesTransactionState.preferredMaxRefreshRate = preferredMaxRefreshRate;
                }
                this.mDisplayContentExt.applyPreferredMode(w.getWrapper().getExtImpl(), w, w == getDisplayPolicy().getTopFullscreenOpaqueWindow(), preferredModeId, preferredMaxRefreshRate);
            }
        }
        w.handleWindowMovedIfNeeded();
        w.resetContentChanged();
        com.android.server.wm.ActivityRecord activity = w.mActivityRecord;
        if (activity != null && activity.isVisibleRequested()) {
            activity.updateLetterboxSurfaceIfNeeded(w);
            boolean updateAllDrawn = activity.updateDrawnWindowStates(w);
            if (updateAllDrawn && !this.mTmpUpdateAllDrawn.contains(activity)) {
                this.mTmpUpdateAllDrawn.add(activity);
            }
        }
        w.updateResizingWindowIfNeeded();
        this.mDisplayContentExt.updateSurfacePosition(com.android.window.flags.Flags.removePrepareSurfaceInPlacement(), w);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    DisplayContent(android.view.Display display, com.android.server.wm.RootWindowContainer rootWindowContainer, com.android.server.wm.DeviceStateController deviceStateController) {
        super(rootWindowContainer.mWindowManager, "DisplayContent", 0);
        boolean z = false;
        this.mVisibleBackgroundUserEnabled = android.os.UserManager.isVisibleBackgroundUsersEnabled();
        this.mMinSizeOfResizeableTaskDp = -1;
        this.mImeWindowsContainer = new com.android.server.wm.DisplayContent.ImeContainer(this.mWmService);
        this.mMaxUiWidth = 0;
        this.mSkipAppTransitionAnimation = false;
        this.mOpeningApps = new android.util.ArraySet<>();
        this.mClosingApps = new android.util.ArraySet<>();
        this.mChangingContainers = new android.util.ArraySet<>();
        this.mClosingChangingContainers = new android.util.ArrayMap<>();
        this.mNoAnimationNotifyOnTransitionFinished = new java.util.ArrayList();
        this.mTokenMap = new java.util.HashMap<>();
        this.mInitialDisplayWidth = 0;
        this.mInitialDisplayHeight = 0;
        this.mInitialPhysicalXDpi = INVALID_DPI;
        this.mInitialPhysicalYDpi = INVALID_DPI;
        this.mInitialDisplayDensity = 0;
        this.mDisplayCutoutCache = new com.android.server.wm.utils.RotationCache<>(new com.android.server.wm.utils.RotationCache.RotationDependentComputation() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda39
            @Override // com.android.server.wm.utils.RotationCache.RotationDependentComputation
            public final java.lang.Object compute(java.lang.Object obj, int i) {
                return this.f$0.calculateDisplayCutoutForRotationUncached((android.view.DisplayCutout) obj, i);
            }
        });
        this.mRoundedCornerCache = new com.android.server.wm.utils.RotationCache<>(new com.android.server.wm.utils.RotationCache.RotationDependentComputation() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda45
            @Override // com.android.server.wm.utils.RotationCache.RotationDependentComputation
            public final java.lang.Object compute(java.lang.Object obj, int i) {
                return this.f$0.calculateRoundedCornersForRotationUncached((android.view.RoundedCorners) obj, i);
            }
        });
        this.mCurrentPrivacyIndicatorBounds = new android.view.PrivacyIndicatorBounds();
        this.mPrivacyIndicatorBoundsCache = new com.android.server.wm.utils.RotationCache<>(new com.android.server.wm.utils.RotationCache.RotationDependentComputation() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda46
            @Override // com.android.server.wm.utils.RotationCache.RotationDependentComputation
            public final java.lang.Object compute(java.lang.Object obj, int i) {
                return this.f$0.calculatePrivacyIndicatorBoundsForRotationUncached((android.view.PrivacyIndicatorBounds) obj, i);
            }
        });
        this.mDisplayShapeCache = new com.android.server.wm.utils.RotationCache<>(new com.android.server.wm.utils.RotationCache.RotationDependentComputation() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda47
            @Override // com.android.server.wm.utils.RotationCache.RotationDependentComputation
            public final java.lang.Object compute(java.lang.Object obj, int i) {
                return this.f$0.calculateDisplayShapeForRotationUncached((android.view.DisplayShape) obj, i);
            }
        });
        this.mBaseDisplayWidth = 0;
        this.mBaseDisplayHeight = 0;
        this.mIsSizeForced = false;
        this.mSandboxDisplayApis = true;
        this.mBaseDisplayDensity = 0;
        this.mIsDensityForced = false;
        this.mBaseDisplayPhysicalXDpi = INVALID_DPI;
        this.mBaseDisplayPhysicalYDpi = INVALID_DPI;
        this.mDisplayInfo = new android.view.DisplayInfo();
        this.mDisplayMetrics = new android.util.DisplayMetrics();
        this.mSystemGestureExclusionListeners = new android.os.RemoteCallbackList<>();
        this.mDecorViewGestureListener = new android.os.RemoteCallbackList<>();
        this.mSystemGestureExclusion = new android.graphics.Region();
        this.mSystemGestureExclusionWasRestricted = false;
        this.mSystemGestureExclusionUnrestricted = new android.graphics.Region();
        this.mSystemGestureFrameLeft = new android.graphics.Rect();
        this.mSystemGestureFrameRight = new android.graphics.Rect();
        this.mRestrictedKeepClearAreas = new android.util.ArraySet();
        this.mUnrestrictedKeepClearAreas = new android.util.ArraySet();
        this.mRealDisplayMetrics = new android.util.DisplayMetrics();
        this.mTmpDisplayMetrics = new android.util.DisplayMetrics();
        this.mCompatDisplayMetrics = new android.util.DisplayMetrics();
        this.mLastWallpaperVisible = false;
        this.mTmpRect = new android.graphics.Rect();
        this.mTmpRect2 = new android.graphics.Rect();
        this.mTmpRegion = new android.graphics.Region();
        this.mTmpConfiguration = new android.content.res.Configuration();
        this.mTmpUpdateAllDrawn = new java.util.LinkedList<>();
        this.mTmpTaskForResizePointSearchResult = new com.android.server.wm.DisplayContent.TaskForResizePointSearchResult();
        this.mTmpApplySurfaceChangesTransactionState = new com.android.server.wm.DisplayContent.ApplySurfaceChangesTransactionState();
        this.mDisplayReady = false;
        this.mWallpaperMayChange = false;
        this.mSession = new android.view.SurfaceSession();
        this.mCurrentFocus = null;
        this.mFocusedApp = null;
        this.mOrientationRequestingTaskDisplayArea = null;
        this.mFixedRotationTransitionListener = new com.android.server.wm.DisplayContent.FixedRotationTransitionListener();
        this.mWinAddedSinceNullFocus = new java.util.ArrayList<>();
        this.mWinRemovedSinceNullFocus = new java.util.ArrayList<>();
        this.mLayoutSeq = 0;
        this.mDisplayContentWrapper = new com.android.server.wm.DisplayContent.DisplayContentWrapper();
        this.mDisplayContentExt = (com.android.server.wm.IDisplayContentExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDisplayContentExt.class).base(this).create();
        this.mNonStaticExt = (com.android.server.wm.INonStaticDisplayContentExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.INonStaticDisplayContentExt.class).base(this).create();
        this.mShellRoots = new android.util.SparseArray<>();
        this.mRemoteInsetsControlTarget = null;
        this.mRemoteInsetsDeath = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda48
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                this.f$0.lambda$new$0();
            }
        };
        this.mDisplayAccessUIDs = new android.util.IntArray();
        this.mAllSleepTokens = new java.util.ArrayList<>();
        this.mActiveSizeCompatActivities = new android.util.ArraySet();
        this.mTempConfig = new android.content.res.Configuration();
        this.mInEnsureActivitiesVisible = false;
        this.mTheiaPanicDetector = new android.view.WindowManagerPolicyConstants.PointerEventListener() { // from class: com.android.server.wm.DisplayContent.2
            public void onPointerEvent(android.view.MotionEvent ev) {
                if (ev.getActionMasked() == 0) {
                    if (com.android.server.wm.DisplayContent.this.mInputMethodWindow != null && com.android.server.wm.DisplayContent.this.mInputMethodWindow.isVisible()) {
                        return;
                    }
                    if (com.android.server.wm.DisplayContent.this.mCurrentFocus != null && (com.android.server.wm.DisplayContent.this.mCurrentFocus.mWinAnimator.mDrawState == 3 || com.android.server.wm.DisplayContent.this.mCurrentFocus.mWinAnimator.mDrawState == 4)) {
                        return;
                    }
                    com.android.server.wm.DisplayContent.this.mDisplayContentExt.onPointerEventForTheia(ev);
                }
            }
        };
        this.mUpdateWindowsForAnimator = new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda49
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$1((com.android.server.wm.WindowState) obj);
            }
        };
        this.mScheduleToastTimeout = new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda50
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$2((com.android.server.wm.WindowState) obj);
            }
        };
        this.mFindFocusedWindow = new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda51
            public final boolean apply(java.lang.Object obj) {
                return this.f$0.lambda$new$3((com.android.server.wm.WindowState) obj);
            }
        };
        this.mPerformLayout = new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda52
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$4((com.android.server.wm.WindowState) obj);
            }
        };
        this.mPerformLayoutAttached = new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda53
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$5((com.android.server.wm.WindowState) obj);
            }
        };
        this.mComputeImeTargetPredicate = new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda40
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$new$6((com.android.server.wm.WindowState) obj);
            }
        };
        this.mApplyPostLayoutPolicy = new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda41
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$7((com.android.server.wm.WindowState) obj);
            }
        };
        this.mApplySurfaceChangesTransaction = new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda42
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$8((com.android.server.wm.WindowState) obj);
            }
        };
        this.mAsyncRotationRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$12();
            }
        };
        if (this.mWmService.mRoot.getDisplayContent(display.getDisplayId()) != null) {
            throw new java.lang.IllegalArgumentException("Display with ID=" + display.getDisplayId() + " already exists=" + this.mWmService.mRoot.getDisplayContent(display.getDisplayId()) + " new=" + display);
        }
        this.mRootWindowContainer = rootWindowContainer;
        this.mAtmService = this.mWmService.mAtmService;
        this.mDisplay = display;
        this.mDisplayId = display.getDisplayId();
        this.mCurrentUniqueDisplayId = display.getUniqueId();
        this.mWallpaperController = new com.android.server.wm.WallpaperController(this.mWmService, this);
        this.mWallpaperController.resetLargestDisplay(display);
        display.getDisplayInfo(this.mDisplayInfo);
        display.getMetrics(this.mDisplayMetrics);
        if (com.android.window.flags.Flags.deferDisplayUpdates()) {
            this.mDisplayUpdater = new com.android.server.wm.DeferredDisplayUpdater(this);
        } else {
            this.mDisplayUpdater = new com.android.server.wm.ImmediateDisplayUpdater(this);
        }
        this.mSystemGestureExclusionLimit = (this.mDisplayMetrics.densityDpi * this.mDisplayContentExt.adjustConstantSystemGestureExclusionLimitDp(this.mWmService.mConstants.mSystemGestureExclusionLimitDp, this)) / 160;
        this.isDefaultDisplay = this.mDisplayId == 0;
        this.mInsetsStateController = new com.android.server.wm.InsetsStateController(this);
        initializeDisplayBaseInfo();
        this.mDisplayFrames = new com.android.server.wm.DisplayFrames(this.mInsetsStateController.getRawInsetsState(), this.mDisplayInfo, calculateDisplayCutoutForRotation(this.mDisplayInfo.rotation), calculateRoundedCornersForRotation(this.mDisplayInfo.rotation), calculatePrivacyIndicatorBoundsForRotation(this.mDisplayInfo.rotation), calculateDisplayShapeForRotation(this.mDisplayInfo.rotation));
        this.mHoldScreenWakeLock = this.mWmService.mPowerManager.newWakeLock(536870922, "WindowManager/displayId:" + this.mDisplayId, this.mDisplayId);
        this.mHoldScreenWakeLock.setReferenceCounted(false);
        this.mAppTransition = new com.android.server.wm.AppTransition(this.mWmService.mContext, this.mWmService, this);
        this.mAppTransition.registerListenerLocked(this.mWmService.mActivityManagerAppTransitionNotifier);
        this.mAppTransition.registerListenerLocked(this.mFixedRotationTransitionListener);
        this.mAppTransitionController = new com.android.server.wm.AppTransitionController(this.mWmService, this);
        this.mTransitionController.registerLegacyListener(this.mFixedRotationTransitionListener);
        this.mUnknownAppVisibilityController = new com.android.server.wm.UnknownAppVisibilityController(this.mWmService, this);
        this.mDisplaySwitchTransitionLauncher = new com.android.server.wm.PhysicalDisplaySwitchTransitionLauncher(this, this.mTransitionController);
        this.mRemoteDisplayChangeController = new com.android.server.wm.RemoteDisplayChangeController(this);
        this.mPointerEventDispatcher = new com.android.server.wm.PointerEventDispatcher(this.mWmService.mInputManager.monitorInput("PointerEventDispatcher" + this.mDisplayId, this.mDisplayId));
        if (this.mWmService.mAtmService.getRecentTasks() != null) {
            registerPointerEventListener(this.mWmService.mAtmService.getRecentTasks().getInputListener());
        }
        this.mDeviceStateController = deviceStateController;
        this.mDisplayContentExt.isCommercialVersion();
        this.mDisplayPolicy = this.mDisplayContentExt.createDisplayPolicy(this.mWmService, this);
        this.mDisplayRotation = new com.android.server.wm.DisplayRotation(this.mWmService, this, this.mDisplayInfo.address, this.mDeviceStateController, rootWindowContainer.getDisplayRotationCoordinator());
        this.mDeviceStateConsumer = new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda44
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$9((com.android.server.wm.DeviceStateController.DeviceState) obj);
            }
        };
        this.mDeviceStateController.registerDeviceStateCallback(this.mDeviceStateConsumer, new android.os.HandlerExecutor(this.mWmService.mH));
        this.mCloseToSquareMaxAspectRatio = this.mWmService.mContext.getResources().getFloat(android.R.dimen.chooser_grid_padding);
        if (this.isDefaultDisplay) {
            this.mWmService.mPolicy.setDefaultDisplay(this);
        }
        this.mDisplayContentExt.setSecondDefaultDisplay(this, this.mWmService);
        if (this.mWmService.mDisplayReady) {
            this.mDisplayPolicy.onConfigurationChanged();
        }
        if (this.mWmService.mSystemReady) {
            this.mDisplayPolicy.systemReady();
        }
        this.mWindowCornerRadius = this.mDisplayPolicy.getWindowCornerRadius();
        this.mPinnedTaskController = new com.android.server.wm.PinnedTaskController(this.mWmService, this);
        this.mDisplayAreaPolicy = this.mWmService.getDisplayAreaPolicyProvider().instantiate(this.mWmService, this, this, this.mImeWindowsContainer);
        android.view.SurfaceControl.Transaction pendingTransaction = getPendingTransaction();
        configureSurfaces(pendingTransaction);
        pendingTransaction.apply();
        onDisplayChanged(this);
        updateDisplayAreaOrganizers();
        boolean zIsCameraCompatTreatmentEnabledAtBuildTime = this.mWmService.mLetterboxConfiguration.isCameraCompatTreatmentEnabledAtBuildTime();
        if (com.android.window.flags.Flags.cameraCompatForFreeform() && com.android.server.wm.DesktopModeLaunchParamsModifier.canEnterDesktopMode(this.mWmService.mContext)) {
            z = true;
        }
        boolean z2 = z;
        if (zIsCameraCompatTreatmentEnabledAtBuildTime || z2) {
            this.mCameraStateMonitor = new com.android.server.wm.CameraStateMonitor(this, this.mWmService.mH);
            this.mActivityRefresher = new com.android.server.wm.ActivityRefresher(this.mWmService, this.mWmService.mH);
            if (zIsCameraCompatTreatmentEnabledAtBuildTime) {
                this.mDisplayRotationCompatPolicy = new com.android.server.wm.DisplayRotationCompatPolicy(this, this.mCameraStateMonitor, this.mActivityRefresher);
                this.mDisplayRotationCompatPolicy.start();
            } else {
                this.mDisplayRotationCompatPolicy = null;
            }
            if (z2) {
                this.mCameraCompatFreeformPolicy = new com.android.server.wm.CameraCompatFreeformPolicy(this, this.mCameraStateMonitor, this.mActivityRefresher);
                this.mCameraCompatFreeformPolicy.start();
            } else {
                this.mCameraCompatFreeformPolicy = null;
            }
            this.mCameraStateMonitor.startListeningToCameraState();
        } else {
            this.mCameraStateMonitor = null;
            this.mActivityRefresher = null;
            this.mDisplayRotationCompatPolicy = null;
            this.mCameraCompatFreeformPolicy = null;
        }
        this.mRotationReversionController = new com.android.server.wm.DisplayRotationReversionController(this);
        this.mInputMonitor = new com.android.server.wm.InputMonitor(this.mWmService, this);
        this.mInsetsPolicy = new com.android.server.wm.InsetsPolicy(this.mInsetsStateController, this);
        this.mMinSizeOfResizeableTaskDp = getMinimalTaskSizeDp();
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DISPLAY) {
            android.util.Slog.v(TAG, "Creating display=" + display);
        }
        setWindowingMode(1);
        this.mDisplayContentExt.adjustDisplayConfig(getConfiguration());
        this.mWmService.mDisplayWindowSettings.applySettingsToDisplayLocked(this);
        this.mInTouchMode = this.mWmService.mContext.getResources().getBoolean(android.R.bool.config_defaultPreventScreenTimeoutEnabled);
        com.android.server.input.InputManagerService inputManagerService = this.mWmService.mInputManager;
        boolean z3 = this.mInTouchMode;
        com.android.server.wm.WindowManagerService windowManagerService = this.mWmService;
        int i = com.android.server.wm.WindowManagerService.MY_PID;
        com.android.server.wm.WindowManagerService windowManagerService2 = this.mWmService;
        inputManagerService.setInTouchMode(z3, i, com.android.server.wm.WindowManagerService.MY_UID, true, this.mDisplayId);
        this.mDisplayContentExt.initOplusRefreshRatePolicy(new java.lang.Object[]{this.mWmService.mContext, this, this.mDisplayInfo});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9(com.android.server.wm.DeviceStateController.DeviceState newFoldState) {
        this.mDisplaySwitchTransitionLauncher.foldStateChanged(newFoldState);
        this.mDisplayRotation.foldStateChanged(newFoldState);
    }

    private void beginHoldScreenUpdate() {
        this.mTmpHoldScreenWindow = null;
        this.mObscuringWindow = null;
    }

    private void finishHoldScreenUpdate() {
        boolean hold = this.mTmpHoldScreenWindow != null;
        if (hold && this.mTmpHoldScreenWindow != this.mHoldScreenWindow) {
            this.mHoldScreenWakeLock.setWorkSource(new android.os.WorkSource(this.mTmpHoldScreenWindow.mSession.mUid, this.mTmpHoldScreenWindow.mSession.mPackageName));
        }
        this.mHoldScreenWindow = this.mTmpHoldScreenWindow;
        this.mTmpHoldScreenWindow = null;
        boolean state = this.mHoldScreenWakeLock.isHeld();
        if (hold != state) {
            if (hold) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_KEEP_SCREEN_ON_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mHoldScreenWindow);
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, 1959209522588955826L, 0, null, protoLogParam0);
                }
                this.mLastWakeLockHoldingWindow = this.mHoldScreenWindow;
                this.mLastWakeLockObscuringWindow = null;
                this.mHoldScreenWakeLock.acquire();
                return;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_KEEP_SCREEN_ON_enabled[0]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this.mObscuringWindow);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_KEEP_SCREEN_ON, 352937214222086717L, 0, null, protoLogParam02);
            }
            this.mLastWakeLockHoldingWindow = null;
            this.mLastWakeLockObscuringWindow = this.mObscuringWindow;
            this.mHoldScreenWakeLock.release();
        }
    }

    com.android.server.wm.DisplayRotationCompatPolicy getDisplayRotationCompatPolicy() {
        return this.mDisplayRotationCompatPolicy;
    }

    @Override // com.android.server.wm.WindowContainer
    void migrateToNewSurfaceControl(android.view.SurfaceControl.Transaction t) {
        t.remove(this.mSurfaceControl);
        this.mLastSurfacePosition.set(0, 0);
        this.mLastDeltaRotation = 0;
        configureSurfaces(t);
        scheduleAnimation();
    }

    private void configureSurfaces(android.view.SurfaceControl.Transaction transaction) {
        android.view.SurfaceControl.Builder b = this.mWmService.makeSurfaceBuilder(this.mSession).setOpaque(true).setContainerLayer().setCallsite("DisplayContent");
        this.mSurfaceControl = b.setName(getName()).setContainerLayer().build();
        for (int i = getChildCount() - 1; i >= 0; i--) {
            android.view.SurfaceControl sc = ((com.android.server.wm.DisplayArea) getChildAt(i)).mSurfaceControl;
            if (sc != null) {
                transaction.reparent(sc, this.mSurfaceControl);
                if (this.mTransitionController.inTransition(this)) {
                    this.mDisplayContentExt.setDisplayChanged(true);
                }
            }
        }
        if (this.mOverlayLayer == null) {
            this.mOverlayLayer = b.setName("Display Overlays").setParent(this.mSurfaceControl).build();
        } else {
            transaction.reparent(this.mOverlayLayer, this.mSurfaceControl);
        }
        if (this.mInputOverlayLayer == null) {
            this.mInputOverlayLayer = b.setName("Input Overlays").setParent(this.mSurfaceControl).build();
        } else {
            transaction.reparent(this.mInputOverlayLayer, this.mSurfaceControl);
        }
        if (this.mA11yOverlayLayer == null) {
            this.mA11yOverlayLayer = b.setName("Accessibility Overlays").setParent(this.mSurfaceControl).build();
        } else {
            transaction.reparent(this.mA11yOverlayLayer, this.mSurfaceControl);
        }
        transaction.setLayerStack(this.mSurfaceControl, this.mDisplayId).show(this.mSurfaceControl).setLayer(this.mOverlayLayer, Integer.MAX_VALUE).show(this.mOverlayLayer).setLayer(this.mInputOverlayLayer, 2147483646).show(this.mInputOverlayLayer).setLayer(this.mA11yOverlayLayer, 2147483645).show(this.mA11yOverlayLayer);
        this.mDisplayContentExt.initOplusRefreshRatePolicy(new java.lang.Object[]{this.mWmService.mContext, this, this.mDisplayInfo});
    }

    com.android.server.wm.DisplayRotationReversionController getRotationReversionController() {
        return this.mRotationReversionController;
    }

    boolean isReady() {
        return this.mWmService.mDisplayReady && this.mDisplayReady;
    }

    boolean setInTouchMode(boolean inTouchMode) {
        if (this.mInTouchMode == inTouchMode) {
            return false;
        }
        this.mInTouchMode = inTouchMode;
        return true;
    }

    boolean isInTouchMode() {
        return this.mInTouchMode;
    }

    int getDisplayId() {
        return this.mDisplayId;
    }

    float getWindowCornerRadius() {
        return this.mWindowCornerRadius;
    }

    com.android.server.wm.WindowToken getWindowToken(android.os.IBinder binder) {
        return this.mTokenMap.get(binder);
    }

    com.android.server.wm.ActivityRecord getActivityRecord(android.os.IBinder binder) {
        com.android.server.wm.WindowToken token = getWindowToken(binder);
        if (token == null) {
            return null;
        }
        return token.asActivityRecord();
    }

    void addWindowToken(android.os.IBinder binder, com.android.server.wm.WindowToken token) {
        com.android.server.wm.DisplayContent dc = this.mWmService.mRoot.getWindowTokenDisplay(token);
        if (dc != null) {
            throw new java.lang.IllegalArgumentException("Can't map token=" + token + " to display=" + getName() + " already mapped to display=" + dc + " tokens=" + dc.mTokenMap);
        }
        if (binder == null) {
            throw new java.lang.IllegalArgumentException("Can't map token=" + token + " to display=" + getName() + " binder is null");
        }
        if (token == null) {
            throw new java.lang.IllegalArgumentException("Can't map null token to display=" + getName() + " binder=" + binder);
        }
        this.mTokenMap.put(binder, token);
        if (token.asActivityRecord() == null) {
            token.mDisplayContent = this;
            com.android.server.wm.DisplayArea.Tokens da = findAreaForToken(token).asTokens();
            da.addChild(token);
        }
    }

    com.android.server.wm.WindowToken removeWindowToken(android.os.IBinder binder, boolean animateExit) {
        com.android.server.wm.WindowToken token = this.mTokenMap.remove(binder);
        if (token != null && token.asActivityRecord() == null) {
            token.setExiting(animateExit);
        }
        return token;
    }

    android.view.SurfaceControl addShellRoot(android.view.IWindow client, int shellRootLayer) {
        com.android.server.wm.ShellRoot root = this.mShellRoots.get(shellRootLayer);
        if (root != null) {
            if (root.getClient() == client) {
                return root.getSurfaceControl();
            }
            root.clear();
            this.mShellRoots.remove(shellRootLayer);
        }
        com.android.server.wm.ShellRoot root2 = new com.android.server.wm.ShellRoot(client, this, shellRootLayer);
        android.view.SurfaceControl rootLeash = root2.getSurfaceControl();
        if (rootLeash == null) {
            root2.clear();
            return null;
        }
        this.mShellRoots.put(shellRootLayer, root2);
        android.view.SurfaceControl out = new android.view.SurfaceControl(rootLeash, "DisplayContent.addShellRoot");
        return out;
    }

    void removeShellRoot(int windowType) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.ShellRoot root = this.mShellRoots.get(windowType);
                if (root == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                root.clear();
                this.mShellRoots.remove(windowType);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void setRemoteInsetsController(android.view.IDisplayWindowInsetsController controller) {
        if (this.mRemoteInsetsControlTarget != null) {
            this.mRemoteInsetsControlTarget.mRemoteInsetsController.asBinder().unlinkToDeath(this.mRemoteInsetsDeath, 0);
            this.mRemoteInsetsControlTarget = null;
        }
        if (controller != null) {
            try {
                controller.asBinder().linkToDeath(this.mRemoteInsetsDeath, 0);
                this.mRemoteInsetsControlTarget = new com.android.server.wm.DisplayContent.RemoteInsetsControlTarget(controller);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    void reParentWindowToken(com.android.server.wm.WindowToken token) {
        com.android.server.wm.DisplayContent prevDc = token.getDisplayContent();
        if (prevDc == this) {
            return;
        }
        if (prevDc != null && prevDc.mTokenMap.remove(token.token) != null && token.asActivityRecord() == null) {
            token.getParent().removeChild(token);
        }
        addWindowToken(token.token, token);
        if (this.mWmService.mAccessibilityController.hasCallbacks()) {
            int prevDisplayId = prevDc != null ? prevDc.getDisplayId() : -1;
            this.mWmService.mAccessibilityController.onSomeWindowResizedOrMoved(prevDisplayId, getDisplayId());
        }
    }

    void removeAppToken(android.os.IBinder binder) {
        com.android.server.wm.WindowToken token = removeWindowToken(binder, true);
        if (token == null) {
            android.util.Slog.w(TAG, "removeAppToken: Attempted to remove non-existing token: " + binder);
            return;
        }
        com.android.server.wm.ActivityRecord activity = token.asActivityRecord();
        if (activity == null) {
            android.util.Slog.w(TAG, "Attempted to remove non-App token: " + binder + " token=" + token);
            return;
        }
        activity.onRemovedFromDisplay();
        if (activity == this.mFixedRotationLaunchingApp) {
            activity.finishFixedRotationTransform();
            setFixedRotationLaunchingAppUnchecked(null);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy.DisplayContentInfo
    public android.view.Display getDisplay() {
        return this.mDisplay;
    }

    android.view.DisplayInfo getDisplayInfo() {
        return this.mDisplayInfo;
    }

    android.util.DisplayMetrics getDisplayMetrics() {
        return this.mDisplayMetrics;
    }

    com.android.server.wm.DisplayPolicy getDisplayPolicy() {
        return this.mDisplayPolicy;
    }

    @Override // com.android.server.policy.WindowManagerPolicy.DisplayContentInfo
    public com.android.server.wm.DisplayRotation getDisplayRotation() {
        return this.mDisplayRotation;
    }

    com.android.server.wm.InsetsStateController getInsetsStateController() {
        return this.mInsetsStateController;
    }

    com.android.server.wm.InsetsPolicy getInsetsPolicy() {
        return this.mInsetsPolicy;
    }

    int getRotation() {
        return this.mDisplayRotation.getRotation();
    }

    int getLastOrientation() {
        return this.mDisplayRotation.getLastOrientation();
    }

    void registerRemoteAnimations(android.view.RemoteAnimationDefinition definition) {
        this.mAppTransitionController.registerRemoteAnimations(definition);
    }

    void reconfigureDisplayLocked() {
        boolean configChanged;
        if (!isReady()) {
            return;
        }
        configureDisplayPolicy();
        setLayoutNeeded();
        if (this.isDefaultDisplay && this.mDisplayContentExt.waitingPhysicalDisplayChanged(this)) {
            android.util.Slog.d(TAG, "force update orientation when device folding.");
            configChanged = updateOrientation(true);
        } else {
            configChanged = updateOrientation();
        }
        android.content.res.Configuration currentDisplayConfig = getConfiguration();
        this.mTmpConfiguration.setTo(currentDisplayConfig);
        computeScreenConfiguration(this.mTmpConfiguration);
        int changes = currentDisplayConfig.diff(this.mTmpConfiguration);
        if (configChanged | (changes != 0)) {
            this.mWaitingForConfig = true;
            if (this.mLastHasContent && this.mTransitionController.isShellTransitionsEnabled()) {
                android.graphics.Rect startBounds = currentDisplayConfig.windowConfiguration.getBounds();
                android.graphics.Rect endBounds = this.mTmpConfiguration.windowConfiguration.getBounds();
                if (!this.mTransitionController.isCollecting()) {
                    android.window.TransitionRequestInfo.DisplayChange change = new android.window.TransitionRequestInfo.DisplayChange(this.mDisplayId);
                    change.setStartAbsBounds(startBounds);
                    change.setEndAbsBounds(endBounds);
                    requestChangeTransition(changes, change);
                } else {
                    com.android.server.wm.Transition transition = this.mTransitionController.getCollectingTransition();
                    transition.setKnownConfigChanges(this, changes);
                    this.mTransitionController.setDisplaySyncMethod(startBounds, endBounds, this);
                    collectDisplayChange(transition);
                }
            } else if (this.mLastHasContent) {
                this.mWmService.startFreezingDisplay(0, 0, this);
            }
            forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda54
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.DisplayContent.lambda$reconfigureDisplayLocked$10((com.android.server.wm.ActivityRecord) obj);
                }
            });
            sendNewConfiguration();
            getWrapper().getExtImpl().scheduleFoldableDeviceDisplaySwitch(this.isDefaultDisplay, this.mAtmService, this.mWmService, this.mDisplayInfo.address);
        }
        this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
    }

    static /* synthetic */ void lambda$reconfigureDisplayLocked$10(com.android.server.wm.ActivityRecord activity) {
        if (activity.getWindowingMode() == 1) {
            activity.clearSizeCompatModeIfNeeded();
        }
    }

    boolean sendNewConfiguration() {
        if (!isReady() || this.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange()) {
            return false;
        }
        com.android.server.wm.Transition.ReadyCondition displayConfig = this.mTransitionController.isCollecting() ? new com.android.server.wm.Transition.ReadyCondition("displayConfig", this) : null;
        if (displayConfig != null) {
            this.mTransitionController.waitFor(displayConfig);
        } else if (this.mTransitionController.isShellTransitionsEnabled() && this.mLastHasContent) {
            android.util.Slog.e(TAG, "Display reconfigured outside of a transition: " + this);
        }
        boolean configUpdated = updateDisplayOverrideConfigurationLocked();
        if (displayConfig != null) {
            displayConfig.meet();
        }
        if (configUpdated) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                android.util.Slog.d(TAG, "sendNewConfiguration: finished configUpdated.");
                return true;
            }
            return true;
        }
        if (!this.mTransitionController.mExt.isFixedTransitionCollectingOrPlaying()) {
            clearFixedRotationLaunchingApp();
        }
        if (this.mWaitingForConfig) {
            this.mWaitingForConfig = false;
            this.mWmService.mLastFinishedFreezeSource = "config-unchanged";
            setLayoutNeeded();
            this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
        }
        return false;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    boolean onDescendantOrientationChanged(com.android.server.wm.WindowContainer requestingContainer) {
        int orientation;
        android.content.res.Configuration config = updateOrientation(requestingContainer, false);
        if (requestingContainer != null) {
            orientation = requestingContainer.getOverrideOrientation();
        } else {
            orientation = -2;
        }
        boolean handled = handlesOrientationChangeFromDescendant(orientation);
        if (config == null) {
            return handled;
        }
        if (!handled || !(requestingContainer instanceof com.android.server.wm.ActivityRecord)) {
            updateDisplayOverrideConfigurationLocked(config, null, false);
        } else {
            com.android.server.wm.ActivityRecord activityRecord = (com.android.server.wm.ActivityRecord) requestingContainer;
            boolean kept = updateDisplayOverrideConfigurationLocked(config, activityRecord, false);
            if (!kept) {
                this.mRootWindowContainer.resumeFocusedTasksTopActivities();
            }
        }
        return handled;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    boolean handlesOrientationChangeFromDescendant(int orientation) {
        return (shouldIgnoreOrientationRequest(orientation) || getDisplayRotation().isFixedToUserRotation()) ? false : true;
    }

    boolean updateOrientation() {
        return updateOrientation(false);
    }

    android.content.res.Configuration updateOrientation(com.android.server.wm.WindowContainer<?> freezeDisplayWindow, boolean forceUpdate) {
        com.android.server.wm.ActivityRecord activity;
        if (!this.mDisplayReady || !updateOrientation(forceUpdate)) {
            return null;
        }
        if (freezeDisplayWindow != null && !this.mWmService.mRoot.mOrientationChangeComplete && (activity = freezeDisplayWindow.asActivityRecord()) != null && activity.mayFreezeScreenLocked()) {
            activity.startFreezingScreen();
        }
        android.content.res.Configuration config = new android.content.res.Configuration();
        computeScreenConfiguration(config);
        return config;
    }

    private int getMinimalTaskSizeDp() {
        android.content.res.Resources res = getDisplayUiContext().getResources();
        android.util.TypedValue value = new android.util.TypedValue();
        res.getValue(android.R.dimen.datepicker_dialog_width, value, true);
        int valueUnit = (value.data >> 0) & 15;
        if (value.type != 5 || valueUnit != 1) {
            throw new java.lang.IllegalArgumentException("Resource ID #0x" + java.lang.Integer.toHexString(android.R.dimen.datepicker_dialog_width) + " is not in valid type or unit");
        }
        return (int) android.util.TypedValue.complexToFloat(value.data);
    }

    private boolean updateOrientation(boolean forceUpdate) {
        com.android.server.wm.WindowContainer prevOrientationSource = this.mLastOrientationSource;
        int orientation = getOrientation();
        com.android.server.wm.WindowContainer orientationSource = getLastOrientationSource();
        if (orientationSource != prevOrientationSource && this.mRotationReversionController.isRotationReversionEnabled()) {
            this.mRotationReversionController.updateForNoSensorOverride();
        }
        com.android.server.wm.ActivityRecord r = orientationSource != null ? orientationSource.asActivityRecord() : null;
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            android.util.Slog.d(TAG, "updateOrientation: orientation = " + android.content.pm.ActivityInfo.screenOrientationToString(orientation) + "(" + orientation + ") is request by orientationSource = " + orientationSource);
        }
        if (r != null) {
            com.android.server.wm.Task task = r.getTask();
            if (task != null && orientation != task.mLastReportedRequestedOrientation) {
                task.mLastReportedRequestedOrientation = orientation;
                this.mAtmService.getTaskChangeNotificationController().notifyTaskRequestedOrientationChanged(task.mTaskId, orientation);
            }
            com.android.server.wm.ActivityRecord topCandidate = (!r.isVisibleRequested() || this.mDisplayContentExt.getTopRunningArForInterruptIfNeeded(r)) ? topRunningActivity() : r;
            if (!this.mDisplayContentExt.waitingPhysicalDisplayChanged(this) && topCandidate != null && handleTopActivityLaunchingInDifferentOrientation(topCandidate, r, true)) {
                this.mDisplayContentExt.checkSetFixedRotationLaunchingApp(this, topCandidate);
                android.util.Slog.d(TAG, "updateOrientation: stop for launching different orientation app. topCandidate = " + topCandidate + " hasFixedRotationTransform = " + topCandidate.hasFixedRotationTransform() + " rotation = " + topCandidate.getWindowConfiguration().getRotation() + " r = " + r + " hasFixedRotationTransform = " + r.hasFixedRotationTransform() + " rotation = " + r.getWindowConfiguration().getRotation() + " mFixedRotationLaunchingApp = " + this.mFixedRotationLaunchingApp + " curRotation = " + getRotation());
                return false;
            }
        }
        if (this.mDisplayContentExt.shouldBlockUpdateOrientationDuringFixedRotation(orientationSource, this)) {
            android.util.Slog.d(TAG, "updateOrientation: stop for fingerprint window during fixed rotation or waiting remote animation.");
            return false;
        }
        return this.mDisplayRotation.updateOrientation(orientation, forceUpdate);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isSyncFinished(com.android.server.wm.BLASTSyncEngine.SyncGroup group) {
        return !this.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange();
    }

    int rotationForActivityInDifferentOrientation(com.android.server.wm.ActivityRecord r) {
        int currentRotation;
        int rotation;
        com.android.server.wm.ActivityRecord nextCandidate;
        if (this.mTransitionController.useShellTransitionsRotation()) {
            return -1;
        }
        int activityOrientation = r.getOverrideOrientation();
        if (!com.android.server.wm.WindowManagerService.ENABLE_FIXED_ROTATION_TRANSFORM || shouldIgnoreOrientationRequest(activityOrientation)) {
            return -1;
        }
        if (activityOrientation == 3 && (nextCandidate = getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda20
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.wm.ActivityRecord) obj).canDefineOrientationForActivitiesAbove();
            }
        }, r, false, true)) != null) {
            r = nextCandidate;
        }
        if (r.inMultiWindowMode() || r.getRequestedConfigurationOrientation(true) == getConfiguration().orientation) {
            return -1;
        }
        if ((r.getTask() == null || !r.getTask().getWrapper().getExtImpl().shouldSkipRotationForFlexibleWindow()) && (rotation = this.mDisplayRotation.rotationForOrientation(this.mDisplayContentExt.getFixedScreenOrientationForFixedRotation(r, r.getRequestedOrientation()), (currentRotation = getRotation()))) != currentRotation) {
            return rotation;
        }
        return -1;
    }

    boolean handleTopActivityLaunchingInDifferentOrientation(com.android.server.wm.ActivityRecord r, boolean checkOpening) {
        return handleTopActivityLaunchingInDifferentOrientation(r, r, checkOpening);
    }

    private boolean handleTopActivityLaunchingInDifferentOrientation(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityRecord orientationSrc, boolean checkOpening) {
        if (!com.android.server.wm.WindowManagerService.ENABLE_FIXED_ROTATION_TRANSFORM) {
            return false;
        }
        boolean rInTransition = this.mDisplayContentExt.isActivityInTransition(this, r, checkOpening);
        if (DEBUG_PANIC) {
            android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation-11, checkOpening = " + checkOpening + ", r = " + r + ", orientationSrc = " + orientationSrc + ", !rInTransition = " + (!rInTransition) + ", config.orientation = " + getConfiguration().orientation + ", call by=" + android.os.Debug.getCallers(12));
        }
        if (r.isFinishingFixedRotationTransform()) {
            if (DEBUG_PANIC) {
                android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation, for isFinishingFixedRotationTransform");
            }
            return false;
        }
        if (r.hasFixedRotationTransform()) {
            if (this.mDisplayContentExt.shouldExitFixedRotation(this, r)) {
                if (DEBUG_PANIC) {
                    android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation, but compact window need to clearFixedRotationLaunchingApp");
                }
                clearFixedRotationLaunchingApp();
                return false;
            }
            if (this.mFixedRotationLaunchingApp != r) {
                setFixedRotationLaunchingApp(r, r.getWindowConfiguration().getRotation());
            }
            return true;
        }
        if (this.mDisplayContentExt.shouldPuttFixedRotation(this, r)) {
            if (DEBUG_PANIC) {
                android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation, shouldPuttFixedRotation");
            }
            return true;
        }
        if ((!r.occludesParent() && this.mDisplayContentExt.doRotationAnimation(r)) || (r.isReportedDrawn() && r.isVisible() && !this.mDisplayContentExt.isShowStartingSurfaceLocked(r))) {
            if (DEBUG_PANIC) {
                android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation, !r.occludesParent = " + (true ^ r.occludesParent()) + " doRotationAnimation = " + this.mDisplayContentExt.doRotationAnimation(r) + " r.isReportedDrawn() = " + r.isReportedDrawn() + " r.isVisible() = " + r.isVisible());
            }
            return false;
        }
        if (checkOpening) {
            if (this.mTransitionController.isShellTransitionsEnabled()) {
                if (!this.mTransitionController.isCollecting(r) && !rInTransition && !this.mDisplayContentExt.isShowStartingSurfaceLocked(r)) {
                    if (DEBUG_PANIC) {
                        android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation, !mTransitionController.isCollecting(r) = true !rInTransition = " + (!rInTransition));
                    }
                    return false;
                }
            } else if (!this.mAppTransition.isTransitionSet() || (!this.mOpeningApps.contains(r) && !this.mDisplayContentExt.suggestUseFixedRotationAnimation(this, r))) {
                if (DEBUG_PANIC) {
                    android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation, !isTransitionSet " + (!this.mAppTransition.isTransitionSet()) + " !mOpeningApps.contains(r) = " + (true ^ this.mOpeningApps.contains(r)));
                }
                return false;
            }
            if (r.isState(com.android.server.wm.ActivityRecord.State.RESUMED) && !r.getTask().mInResumeTopActivity && !rInTransition && r != this.mDisplayContentExt.getFixedRotationAppForMirage()) {
                if (DEBUG_PANIC) {
                    android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation , r.isState(RESUMED) = true, !r.getTask().mInResumeTopActivity = true");
                }
                return false;
            }
        } else if (r != topRunningActivity()) {
            if (DEBUG_PANIC) {
                android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation, topRunningActivity = " + topRunningActivity());
            }
            return false;
        }
        if (this.mLastWallpaperVisible && r.windowsCanBeWallpaperTarget() && this.mFixedRotationTransitionListener.mAnimatingRecents == null && !this.mTransitionController.isTransientLaunch(r)) {
            if (DEBUG_PANIC) {
                android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation, !isTransientLaunch(r) = true");
            }
            if (!rInTransition && !this.mDisplayContentExt.isLauncherActivity(r)) {
                return false;
            }
        }
        int rotation = rotationForActivityInDifferentOrientation(orientationSrc);
        if (DEBUG_PANIC) {
            android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation, calculate rotation = " + rotation);
        }
        if (rotation == -1) {
            return false;
        }
        if (!r.getDisplayArea().matchParentBounds()) {
            if (DEBUG_PANIC) {
                android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation, for !matchParentBounds");
            }
            return false;
        }
        if (this.mDisplayContentExt.dontDoFixedRotatinAnimation(this, r)) {
            return false;
        }
        if (!this.mDisplayContentExt.shouldSetFixedRotationForTargetLaunchingApp(orientationSrc, r)) {
            if (DEBUG_PANIC) {
                android.util.Slog.e(TAG_FIXED, "handleTopActivityLaunchingInDifferentOrientation, for split screen shouldSetFixedRotationForTargetLaunchingApp");
            }
            return false;
        }
        setFixedRotationLaunchingApp(r, rotation);
        return true;
    }

    boolean hasTopFixedRotationLaunchingApp() {
        return (this.mFixedRotationLaunchingApp == null || this.mFixedRotationLaunchingApp == this.mFixedRotationTransitionListener.mAnimatingRecents) ? false : true;
    }

    boolean hasFixedRotationTransientLaunch() {
        return this.mFixedRotationLaunchingApp != null && this.mTransitionController.isTransientLaunch(this.mFixedRotationLaunchingApp) && this.mDisplayContentExt.shouldDeferRotationForFixedRotation(this.mFixedRotationLaunchingApp);
    }

    boolean isFixedRotationLaunchingApp(com.android.server.wm.ActivityRecord r) {
        return this.mFixedRotationLaunchingApp == r;
    }

    com.android.server.wm.AsyncRotationController getAsyncRotationController() {
        return this.mAsyncRotationController;
    }

    void setFixedRotationLaunchingAppUnchecked(com.android.server.wm.ActivityRecord r) {
        setFixedRotationLaunchingAppUnchecked(r, -1);
    }

    void setFixedRotationLaunchingAppUnchecked(com.android.server.wm.ActivityRecord r, int rotation) {
        if (this.mFixedRotationLaunchingApp == null && r != null) {
            this.mWmService.mDisplayNotificationController.dispatchFixedRotationStarted(this, rotation);
            boolean shouldDebounce = r == this.mFixedRotationTransitionListener.mAnimatingRecents || this.mTransitionController.isTransientLaunch(r);
            startAsyncRotation(shouldDebounce);
        } else if (this.mFixedRotationLaunchingApp != null && r == null) {
            this.mWmService.mDisplayNotificationController.dispatchFixedRotationFinished(this);
            if (!this.mTransitionController.hasCollectingRotationChange(this, getRotation())) {
                finishAsyncRotationIfPossible();
            }
        }
        this.mDisplayContentExt.positionAnimation(this.mFixedRotationLaunchingApp, r);
        this.mFixedRotationLaunchingApp = r;
        this.mDisplayContentExt.setFixedRotationForScreenshot(r, rotation);
        this.mDisplayContentExt.setIsFixedRotationBlocked(false);
    }

    void setFixedRotationLaunchingApp(com.android.server.wm.ActivityRecord r, int rotation) {
        com.android.server.wm.ActivityRecord prevRotatedLaunchingApp = this.mFixedRotationLaunchingApp;
        if (prevRotatedLaunchingApp == r && r.getWindowConfiguration().getRotation() == rotation) {
            return;
        }
        if (prevRotatedLaunchingApp != null && prevRotatedLaunchingApp.getWindowConfiguration().getRotation() == rotation && this.mDisplayContentExt.isAnimating(prevRotatedLaunchingApp, this.mDisplayRotation)) {
            r.linkFixedRotationTransform(prevRotatedLaunchingApp);
            if (r != this.mFixedRotationTransitionListener.mAnimatingRecents) {
                setFixedRotationLaunchingAppUnchecked(r, rotation);
                return;
            }
            return;
        }
        if (!r.hasFixedRotationTransform()) {
            startFixedRotationTransform(r, rotation);
            this.mDisplayContentExt.linkFixedRotationTransform(r, rotation);
        }
        setFixedRotationLaunchingAppUnchecked(r, rotation);
        if (prevRotatedLaunchingApp != null) {
            prevRotatedLaunchingApp.finishFixedRotationTransform();
        }
    }

    void continueUpdateOrientationForDiffOrienLaunchingApp() {
        android.util.Slog.d(TAG, "Enter continueUpdateOrientationForDiffOrienLaunchingApp fixed rotation app = " + this.mFixedRotationLaunchingApp + (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM ? " callers: " + android.os.Debug.getCallers(3) : null));
        if (this.mFixedRotationLaunchingApp == null || this.mPinnedTaskController.shouldDeferOrientationChange()) {
            return;
        }
        if (this.mDisplayRotation.updateOrientation(getOrientation(), false)) {
            sendNewConfiguration();
        } else {
            if (this.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange()) {
                return;
            }
            clearFixedRotationLaunchingApp();
        }
    }

    private void clearFixedRotationLaunchingApp() {
        if (this.mFixedRotationLaunchingApp == null) {
            return;
        }
        this.mFixedRotationLaunchingApp.finishFixedRotationTransform();
        setFixedRotationLaunchingAppUnchecked(null);
    }

    private void startFixedRotationTransform(com.android.server.wm.WindowToken token, int rotation) {
        this.mTmpConfiguration.unset();
        android.view.DisplayInfo info = computeScreenConfiguration(this.mTmpConfiguration, rotation);
        android.view.DisplayCutout cutout = calculateDisplayCutoutForRotation(rotation);
        android.view.RoundedCorners roundedCorners = calculateRoundedCornersForRotation(rotation);
        android.view.PrivacyIndicatorBounds indicatorBounds = calculatePrivacyIndicatorBoundsForRotation(rotation);
        android.view.DisplayShape displayShape = calculateDisplayShapeForRotation(rotation);
        com.android.server.wm.DisplayFrames displayFrames = new com.android.server.wm.DisplayFrames(new android.view.InsetsState(), info, cutout, roundedCorners, indicatorBounds, displayShape);
        token.applyFixedRotationTransform(info, displayFrames, this.mTmpConfiguration);
    }

    void rotateInDifferentOrientationIfNeeded(com.android.server.wm.ActivityRecord activityRecord) {
        int rotation = rotationForActivityInDifferentOrientation(activityRecord);
        if (rotation != -1) {
            startFixedRotationTransform(activityRecord, rotation);
        }
    }

    boolean isRotationChanging() {
        return this.mDisplayRotation.getRotation() != getWindowConfiguration().getRotation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAsyncRotationIfNeeded() {
        if (isRotationChanging()) {
            startAsyncRotation(false);
        }
    }

    private boolean startAsyncRotation(boolean shouldDebounce) {
        if (shouldDebounce) {
            this.mWmService.mH.postDelayed(this.mAsyncRotationRunnable, FIXED_ROTATION_HIDE_ANIMATION_DEBOUNCE_DELAY_MS);
            return false;
        }
        if (this.mAsyncRotationController != null) {
            return false;
        }
        this.mAsyncRotationController = new com.android.server.wm.AsyncRotationController(this);
        this.mDisplayContentExt.setRotationChange(this, true);
        this.mAsyncRotationController.start();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$12() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mFixedRotationLaunchingApp != null && startAsyncRotation(false)) {
                    this.mDisplayContent.getPendingTransaction().apply();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void finishAsyncRotationIfPossible() {
        com.android.server.wm.AsyncRotationController controller = this.mAsyncRotationController;
        if (controller != null && !this.mDisplayRotation.hasSeamlessRotatingWindow()) {
            controller.completeAll();
            this.mDisplayContentExt.setRotationChange(this, false);
            this.mAsyncRotationController = null;
        }
    }

    void finishAsyncRotation(com.android.server.wm.WindowToken windowToken) {
        com.android.server.wm.AsyncRotationController controller = this.mAsyncRotationController;
        if (controller != null && controller.completeRotation(windowToken)) {
            this.mDisplayContentExt.setRotationChange(this, false);
            this.mAsyncRotationController = null;
            this.mDisplayContentExt.requestTraversalWhenAsyncRotationFinished();
        }
    }

    boolean shouldSyncRotationChange(com.android.server.wm.WindowState w) {
        com.android.server.wm.AsyncRotationController controller = this.mAsyncRotationController;
        return controller == null || !controller.isAsync(w);
    }

    void notifyInsetsChanged(java.util.function.Consumer<com.android.server.wm.WindowState> dispatchInsetsChanged) {
        android.view.InsetsState rotatedState;
        if (this.mFixedRotationLaunchingApp != null && (rotatedState = this.mFixedRotationLaunchingApp.getFixedRotationTransformInsetsState()) != null) {
            android.view.InsetsState state = this.mInsetsStateController.getRawInsetsState();
            android.view.InsetsState.traverse(rotatedState, state, COPY_SOURCE_VISIBILITY);
        }
        forAllWindows(dispatchInsetsChanged, true);
        if (this.mRemoteInsetsControlTarget != null) {
            this.mRemoteInsetsControlTarget.notifyInsetsChanged();
        }
        if (this.mWmService.mAccessibilityController.hasCallbacks()) {
            boolean isImeShow = this.mImeControlTarget != null && this.mImeControlTarget.isRequestedVisible(android.view.WindowInsets.Type.ime());
            this.mWmService.mAccessibilityController.updateImeVisibilityIfNeeded(this.mDisplayId, isImeShow);
        }
        this.mDisplayContentExt.notifyInsetsChangedLw();
    }

    boolean updateRotationUnchecked() {
        if (this.mDisplayContentExt.updateRotationUnchecked((hasTopFixedRotationLaunchingApp() && this.mFixedRotationLaunchingApp.isFixedRotationTransforming() && this.mFixedRotationLaunchingApp.hasAnimatingFixedRotationTransition() && this.mDisplayContentExt.isProhibitUpdateApp(this.mFixedRotationLaunchingApp)) || this.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange())) {
            android.util.Slog.d(TAG, "updateRotationUnchecked: stop for fixed rotation or waiting remote rotation, mFixedRotationLaunchingApp = " + this.mFixedRotationLaunchingApp);
            return false;
        }
        if (this.mDisplayContentExt.hasGestureAnimationController()) {
            android.util.Slog.d(TAG, "block update orientation during gesture animation when updateRotationUnchecked");
            return false;
        }
        return this.mDisplayRotation.updateRotationUnchecked(false);
    }

    boolean canShowTasksInHostDeviceRecents() {
        if (this.mDwpcHelper == null) {
            return true;
        }
        return this.mDwpcHelper.canShowTasksInHostDeviceRecents();
    }

    android.content.ComponentName getCustomHomeComponent() {
        if (!isHomeSupported() || this.mDwpcHelper == null) {
            return null;
        }
        return this.mDwpcHelper.getCustomHomeComponent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: applyRotation, reason: merged with bridge method [inline-methods] */
    public void lambda$applyRotationAndFinishFixedRotation$42(final int oldRotation, final int rotation) {
        this.mDisplayContentExt.applyRotation(oldRotation, rotation, this.mDisplayId);
        this.mDisplayRotation.applyCurrentRotation(rotation);
        final boolean rotateSeamlessly = false;
        boolean shellTransitions = this.mTransitionController.getTransitionPlayer() != null;
        if (this.mDisplayRotation.isRotatingSeamlessly() && !shellTransitions) {
            rotateSeamlessly = true;
        }
        final android.view.SurfaceControl.Transaction transaction = shellTransitions ? getSyncTransaction() : getPendingTransaction();
        com.android.server.wm.ScreenRotationAnimation screenRotationAnimation = rotateSeamlessly ? null : getRotationAnimation();
        updateDisplayAndOrientation(null);
        if (screenRotationAnimation != null && screenRotationAnimation.hasScreenshot()) {
            screenRotationAnimation.setRotation(transaction, rotation);
        }
        if (!shellTransitions) {
            forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.DisplayContent.lambda$applyRotation$13(transaction, oldRotation, rotation, rotateSeamlessly, (com.android.server.wm.WindowState) obj);
                }
            }, true);
            this.mPinnedTaskController.startSeamlessRotationIfNeeded(transaction, oldRotation, rotation);
            if (!this.mDisplayRotation.hasSeamlessRotatingWindow()) {
                this.mDisplayRotation.cancelSeamlessRotation();
            }
        }
        if (shellTransitions) {
            getPendingTransaction().setFixedTransformHint(this.mSurfaceControl, rotation);
            transaction.unsetFixedTransformHint(this.mSurfaceControl);
        }
        scheduleAnimation();
        this.mWmService.mRotationWatcherController.dispatchDisplayRotationChange(this.mDisplayId, rotation);
    }

    static /* synthetic */ void lambda$applyRotation$13(android.view.SurfaceControl.Transaction transaction, int oldRotation, int rotation, boolean rotateSeamlessly, com.android.server.wm.WindowState w) {
        w.seamlesslyRotateIfAllowed(transaction, oldRotation, rotation, rotateSeamlessly);
        if (!rotateSeamlessly && w.mHasSurface) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(w);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 2632363530212357762L, 0, null, protoLogParam0);
            }
            w.setOrientationChanging(true);
        }
    }

    void configureDisplayPolicy() {
        this.mRootWindowContainer.updateDisplayImePolicyCache();
        this.mDisplayPolicy.updateConfigurationAndScreenSizeDependentBehaviors();
        this.mDisplayRotation.configure(this.mBaseDisplayWidth, this.mBaseDisplayHeight);
    }

    android.view.DisplayInfo updateDisplayAndOrientation(android.content.res.Configuration outConfig) {
        int rotation = getRotation();
        boolean rotated = this.mDisplayContentExt.shouldDisplayRotated(rotation, this.mDisplayInfo.name, this);
        int dw = rotated ? this.mBaseDisplayHeight : this.mBaseDisplayWidth;
        int dh = rotated ? this.mBaseDisplayWidth : this.mBaseDisplayHeight;
        android.view.DisplayCutout displayCutout = calculateDisplayCutoutForRotation(rotation);
        android.view.RoundedCorners roundedCorners = calculateRoundedCornersForRotation(rotation);
        android.view.DisplayShape displayShape = calculateDisplayShapeForRotation(rotation);
        android.graphics.Rect appFrame = this.mDisplayPolicy.getDecorInsetsInfo(rotation, dw, dh).mNonDecorFrame;
        this.mDisplayInfo.rotation = rotation;
        this.mDisplayInfo.logicalWidth = dw;
        this.mDisplayInfo.logicalHeight = dh;
        this.mDisplayInfo.logicalDensityDpi = this.mBaseDisplayDensity;
        this.mDisplayInfo.physicalXDpi = this.mBaseDisplayPhysicalXDpi;
        this.mDisplayInfo.physicalYDpi = this.mBaseDisplayPhysicalYDpi;
        this.mDisplayInfo.appWidth = appFrame.width();
        this.mDisplayInfo.appHeight = appFrame.height();
        if (this.isDefaultDisplay) {
            this.mDisplayInfo.getLogicalMetrics(this.mRealDisplayMetrics, android.content.res.CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO, (android.content.res.Configuration) null);
        }
        this.mDisplayInfo.displayCutout = displayCutout.isEmpty() ? null : displayCutout;
        this.mDisplayInfo.roundedCorners = roundedCorners;
        this.mDisplayInfo.displayShape = displayShape;
        this.mDisplayInfo.getAppMetrics(this.mDisplayMetrics);
        if (this.mDisplayScalingDisabled) {
            this.mDisplayInfo.flags |= 1073741824;
        } else {
            this.mDisplayInfo.flags &= -1073741825;
        }
        computeSizeRanges(this.mDisplayInfo, rotated, dw, dh, this.mDisplayMetrics.density, outConfig, false);
        setDisplayInfoOverride();
        if (this.isDefaultDisplay) {
            this.mCompatibleScreenScale = android.content.res.CompatibilityInfo.computeCompatibleScaling(this.mDisplayMetrics, this.mCompatDisplayMetrics);
        }
        onDisplayInfoChanged();
        return this.mDisplayInfo;
    }

    private void setDisplayInfoOverride() {
        this.mWmService.mDisplayManagerInternal.setDisplayInfoOverrideFromWindowManager(this.mDisplayId, this.mDisplayInfo);
        if (this.mLastDisplayInfoOverride == null) {
            this.mLastDisplayInfoOverride = new android.view.DisplayInfo();
        }
        this.mLastDisplayInfoOverride.copyFrom(this.mDisplayInfo);
    }

    android.view.DisplayCutout calculateDisplayCutoutForRotation(int rotation) {
        if (this.mDisplayContentExt.isReturnNoCutoutForFullScreenDisplay(rotation, this)) {
            return android.view.DisplayCutout.NO_CUTOUT;
        }
        return this.mDisplayCutoutCache.getOrCompute(this.mIsSizeForced ? this.mBaseDisplayCutout : this.mInitialDisplayCutout, this.mDisplayContentExt.correctRotationParam(rotation)).getDisplayCutout();
    }

    static com.android.server.wm.utils.WmDisplayCutout calculateDisplayCutoutForRotationAndDisplaySizeUncached(android.view.DisplayCutout cutout, int rotation, int displayWidth, int displayHeight) {
        if (cutout == null || cutout == android.view.DisplayCutout.NO_CUTOUT) {
            return com.android.server.wm.utils.WmDisplayCutout.NO_CUTOUT;
        }
        if (displayWidth == displayHeight) {
            android.util.Slog.w(TAG, "Ignore cutout because display size is square: " + displayWidth);
            return com.android.server.wm.utils.WmDisplayCutout.NO_CUTOUT;
        }
        if (rotation == 0) {
            return com.android.server.wm.utils.WmDisplayCutout.computeSafeInsets(cutout, displayWidth, displayHeight);
        }
        boolean rotated = false;
        android.view.DisplayCutout rotatedCutout = cutout.getRotated(displayWidth, displayHeight, 0, rotation);
        if (rotation == 1 || rotation == 3) {
            rotated = true;
        }
        return new com.android.server.wm.utils.WmDisplayCutout(rotatedCutout, new android.util.Size(rotated ? displayHeight : displayWidth, rotated ? displayWidth : displayHeight));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.wm.utils.WmDisplayCutout calculateDisplayCutoutForRotationUncached(android.view.DisplayCutout cutout, int rotation) {
        return calculateDisplayCutoutForRotationAndDisplaySizeUncached(cutout, rotation, this.mIsSizeForced ? this.mBaseDisplayWidth : this.mInitialDisplayWidth, this.mIsSizeForced ? this.mBaseDisplayHeight : this.mInitialDisplayHeight);
    }

    android.view.RoundedCorners calculateRoundedCornersForRotation(int rotation) {
        return this.mRoundedCornerCache.getOrCompute(this.mIsSizeForced ? this.mBaseRoundedCorners : this.mInitialRoundedCorners, rotation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.view.RoundedCorners calculateRoundedCornersForRotationUncached(android.view.RoundedCorners roundedCorners, int rotation) {
        if (roundedCorners == null || roundedCorners == android.view.RoundedCorners.NO_ROUNDED_CORNERS) {
            return android.view.RoundedCorners.NO_ROUNDED_CORNERS;
        }
        if (rotation == 0) {
            return roundedCorners;
        }
        return roundedCorners.rotate(rotation, this.mIsSizeForced ? this.mBaseDisplayWidth : this.mInitialDisplayWidth, this.mIsSizeForced ? this.mBaseDisplayHeight : this.mInitialDisplayHeight);
    }

    android.view.PrivacyIndicatorBounds calculatePrivacyIndicatorBoundsForRotation(int rotation) {
        return this.mPrivacyIndicatorBoundsCache.getOrCompute(this.mCurrentPrivacyIndicatorBounds, rotation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.view.PrivacyIndicatorBounds calculatePrivacyIndicatorBoundsForRotationUncached(android.view.PrivacyIndicatorBounds bounds, int rotation) {
        if (bounds == null) {
            return new android.view.PrivacyIndicatorBounds(new android.graphics.Rect[4], rotation);
        }
        return bounds.rotate(rotation);
    }

    android.view.DisplayShape calculateDisplayShapeForRotation(int rotation) {
        return this.mDisplayShapeCache.getOrCompute(this.mInitialDisplayShape, rotation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.view.DisplayShape calculateDisplayShapeForRotationUncached(android.view.DisplayShape displayShape, int rotation) {
        if (displayShape == null) {
            return android.view.DisplayShape.NONE;
        }
        if (rotation == 0) {
            return displayShape;
        }
        return displayShape.setRotation(rotation);
    }

    android.view.DisplayInfo computeScreenConfiguration(android.content.res.Configuration outConfig, int rotation) {
        boolean z = true;
        if (rotation != 1 && rotation != 3) {
            z = false;
        }
        boolean rotated = z;
        int dw = rotated ? this.mBaseDisplayHeight : this.mBaseDisplayWidth;
        int dh = rotated ? this.mBaseDisplayWidth : this.mBaseDisplayHeight;
        outConfig.windowConfiguration.setMaxBounds(0, 0, dw, dh);
        outConfig.windowConfiguration.setBounds(outConfig.windowConfiguration.getMaxBounds());
        computeScreenAppConfiguration(outConfig, dw, dh, rotation);
        android.view.DisplayInfo displayInfo = new android.view.DisplayInfo(this.mDisplayInfo);
        displayInfo.rotation = rotation;
        displayInfo.logicalWidth = dw;
        displayInfo.logicalHeight = dh;
        android.graphics.Rect appBounds = outConfig.windowConfiguration.getAppBounds();
        displayInfo.appWidth = appBounds.width();
        displayInfo.appHeight = appBounds.height();
        android.view.DisplayCutout displayCutout = calculateDisplayCutoutForRotation(rotation);
        displayInfo.displayCutout = displayCutout.isEmpty() ? null : displayCutout;
        computeSizeRanges(displayInfo, rotated, dw, dh, this.mDisplayMetrics.density, outConfig, false);
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            android.util.Slog.d(TAG, "computeScreenConfiguration: AppBounds:" + appBounds + ", rotation:" + displayInfo.rotation);
        }
        return displayInfo;
    }

    private void computeScreenAppConfiguration(android.content.res.Configuration outConfig, int dw, int dh, int rotation) {
        com.android.server.wm.DisplayPolicy.DecorInsets.Info info = this.mDisplayPolicy.getDecorInsetsInfo(rotation, dw, dh);
        outConfig.windowConfiguration.setAppBounds(info.mNonDecorFrame);
        outConfig.windowConfiguration.setRotation(rotation);
        float density = this.mDisplayMetrics.density;
        outConfig.screenWidthDp = (int) ((info.mConfigFrame.width() / density) + 0.5f);
        outConfig.screenHeightDp = (int) ((info.mConfigFrame.height() / density) + 0.5f);
        outConfig.compatScreenWidthDp = (int) (outConfig.screenWidthDp / this.mCompatibleScreenScale);
        outConfig.compatScreenHeightDp = (int) (outConfig.screenHeightDp / this.mCompatibleScreenScale);
        boolean z = true;
        outConfig.orientation = outConfig.screenWidthDp <= outConfig.screenHeightDp ? 1 : 2;
        outConfig.screenLayout = computeScreenLayout(android.content.res.Configuration.resetScreenLayout(outConfig.screenLayout), outConfig.screenWidthDp, outConfig.screenHeightDp);
        if (rotation != 1 && rotation != 3) {
            z = false;
        }
        boolean rotated = z;
        outConfig.compatSmallestScreenWidthDp = computeCompatSmallestWidth(rotated, dw, dh);
        outConfig.windowConfiguration.setDisplayRotation(rotation);
        this.mDisplayContentExt.adjustDisplayConfig(outConfig);
        this.mDisplayContentExt.adjustScreenConfigurationForCarLink(this.mDisplayContent, outConfig, density);
        if (density == INVALID_DPI) {
            android.util.Slog.i(TAG, "computeScreenAppConfiguration  dw:" + dw + " dh:" + dh + " rotation:" + rotation + " configFrameWidth:" + info.mConfigFrame.width() + " configFrameHeight:" + info.mConfigFrame.height() + " density:" + density + " outConfig:" + outConfig.toString() + " " + android.os.Debug.getCallers(5));
        }
    }

    void computeScreenConfiguration(android.content.res.Configuration config) {
        int i;
        int i2;
        int i3;
        int i4;
        android.view.DisplayInfo displayInfo = updateDisplayAndOrientation(config);
        int dw = displayInfo.logicalWidth;
        int dh = displayInfo.logicalHeight;
        this.mTmpRect.set(0, 0, dw, dh);
        config.windowConfiguration.setBounds(this.mTmpRect);
        config.windowConfiguration.setMaxBounds(this.mTmpRect);
        config.windowConfiguration.setWindowingMode(getWindowingMode());
        computeScreenAppConfiguration(config, dw, dh, displayInfo.rotation);
        int i5 = config.screenLayout & (-769);
        if ((displayInfo.flags & 16) != 0) {
            i = 512;
        } else {
            i = 256;
        }
        config.screenLayout = i5 | i;
        config.densityDpi = displayInfo.logicalDensityDpi;
        if (displayInfo.isHdr() && this.mWmService.hasHdrSupport()) {
            i2 = 8;
        } else {
            i2 = 4;
        }
        int i6 = 1;
        if (displayInfo.isWideColorGamut() && this.mWmService.hasWideColorGamutSupport()) {
            i3 = 2;
        } else {
            i3 = 1;
        }
        config.colorMode = i2 | i3;
        config.touchscreen = 1;
        config.keyboard = 1;
        config.navigation = 1;
        int keyboardPresence = 0;
        int navigationPresence = 0;
        android.view.InputDevice[] devices = this.mWmService.mInputManager.getInputDevices();
        int len = devices != null ? devices.length : 0;
        int i7 = 0;
        while (i7 < len) {
            android.view.InputDevice device = devices[i7];
            if (!device.isVirtual() && this.mWmService.mInputManager.canDispatchToDisplay(device.getId(), this.mDisplayId)) {
                int sources = device.getSources();
                int presenceFlag = device.isExternal() ? 2 : i6;
                if (this.mWmService.mIsTouchDevice) {
                    if ((sources & 4098) == 4098) {
                        config.touchscreen = 3;
                    }
                } else {
                    config.touchscreen = 1;
                }
                if ((sources & 65540) == 65540) {
                    config.navigation = 3;
                    navigationPresence |= presenceFlag;
                    i4 = 2;
                } else if ((sources & 513) != 513 || config.navigation != 1) {
                    i4 = 2;
                } else {
                    i4 = 2;
                    config.navigation = 2;
                    navigationPresence |= presenceFlag;
                }
                if (device.getKeyboardType() == i4) {
                    config.keyboard = i4;
                    keyboardPresence |= presenceFlag;
                }
            }
            i7++;
            i6 = 1;
        }
        if (config.navigation == 1 && this.mWmService.mHasPermanentDpad) {
            config.navigation = 2;
            navigationPresence |= 1;
        }
        boolean hardKeyboardAvailable = config.keyboard != 1;
        if (hardKeyboardAvailable != this.mWmService.mHardKeyboardAvailable) {
            this.mWmService.mHardKeyboardAvailable = hardKeyboardAvailable;
            this.mWmService.mH.removeMessages(22);
            this.mWmService.mH.sendEmptyMessage(22);
        }
        this.mDisplayPolicy.updateConfigurationAndScreenSizeDependentBehaviors();
        config.keyboardHidden = 1;
        config.hardKeyboardHidden = 1;
        config.navigationHidden = 1;
        this.mWmService.mPolicy.adjustConfigurationLw(config, keyboardPresence, navigationPresence);
    }

    private int computeCompatSmallestWidth(boolean rotated, int dw, int dh) {
        int unrotDw;
        int unrotDh;
        this.mTmpDisplayMetrics.setTo(this.mDisplayMetrics);
        android.util.DisplayMetrics tmpDm = this.mTmpDisplayMetrics;
        if (rotated) {
            unrotDw = dh;
            unrotDh = dw;
        } else {
            unrotDw = dw;
            unrotDh = dh;
        }
        int sw = reduceCompatConfigWidthSize(0, 0, tmpDm, unrotDw, unrotDh);
        return reduceCompatConfigWidthSize(reduceCompatConfigWidthSize(reduceCompatConfigWidthSize(sw, 1, tmpDm, unrotDh, unrotDw), 2, tmpDm, unrotDw, unrotDh), 3, tmpDm, unrotDh, unrotDw);
    }

    private int reduceCompatConfigWidthSize(int curSize, int rotation, android.util.DisplayMetrics dm, int dw, int dh) {
        android.graphics.Rect nonDecorSize = this.mDisplayPolicy.getDecorInsetsInfo(rotation, dw, dh).mNonDecorFrame;
        dm.noncompatWidthPixels = nonDecorSize.width();
        dm.noncompatHeightPixels = nonDecorSize.height();
        float scale = android.content.res.CompatibilityInfo.computeCompatibleScaling(dm, (android.util.DisplayMetrics) null);
        int size = (int) (((dm.noncompatWidthPixels / scale) / dm.density) + 0.5f);
        if (curSize == 0 || size < curSize) {
            return size;
        }
        return curSize;
    }

    void computeSizeRanges(android.view.DisplayInfo displayInfo, boolean rotated, int dw, int dh, float density, android.content.res.Configuration outConfig, boolean overrideConfig) {
        int unrotDw;
        int unrotDh;
        if (rotated) {
            unrotDw = dh;
            unrotDh = dw;
        } else {
            unrotDw = dw;
            unrotDh = dh;
        }
        displayInfo.smallestNominalAppWidth = 1073741824;
        displayInfo.smallestNominalAppHeight = 1073741824;
        displayInfo.largestNominalAppWidth = 0;
        displayInfo.largestNominalAppHeight = 0;
        adjustDisplaySizeRanges(displayInfo, 0, unrotDw, unrotDh, overrideConfig);
        adjustDisplaySizeRanges(displayInfo, 1, unrotDh, unrotDw, overrideConfig);
        adjustDisplaySizeRanges(displayInfo, 2, unrotDw, unrotDh, overrideConfig);
        adjustDisplaySizeRanges(displayInfo, 3, unrotDh, unrotDw, overrideConfig);
        if (outConfig == null) {
            return;
        }
        outConfig.smallestScreenWidthDp = (int) ((displayInfo.smallestNominalAppWidth / density) + 0.5f);
    }

    private void adjustDisplaySizeRanges(android.view.DisplayInfo displayInfo, int rotation, int dw, int dh, boolean overrideConfig) {
        int w;
        int h;
        com.android.server.wm.DisplayPolicy.DecorInsets.Info info = this.mDisplayPolicy.getDecorInsetsInfo(rotation, dw, dh);
        if (!overrideConfig) {
            w = info.mConfigFrame.width();
            h = info.mConfigFrame.height();
        } else {
            w = info.mOverrideConfigFrame.width();
            h = info.mOverrideConfigFrame.height();
        }
        if (w < displayInfo.smallestNominalAppWidth) {
            displayInfo.smallestNominalAppWidth = w;
        }
        if (w > displayInfo.largestNominalAppWidth) {
            displayInfo.largestNominalAppWidth = w;
        }
        if (h < displayInfo.smallestNominalAppHeight) {
            displayInfo.smallestNominalAppHeight = h;
        }
        if (h > displayInfo.largestNominalAppHeight) {
            displayInfo.largestNominalAppHeight = h;
        }
    }

    int getPreferredOptionsPanelGravity() {
        int rotation = getRotation();
        if (this.mInitialDisplayWidth < this.mInitialDisplayHeight) {
            switch (rotation) {
                case 1:
                    return 85;
                case 2:
                    return 81;
                case 3:
                    return 8388691;
                default:
                    return 81;
            }
        }
        switch (rotation) {
            case 1:
                return 81;
            case 2:
                return 8388691;
            case 3:
                return 81;
            default:
                return 85;
        }
    }

    com.android.server.wm.PinnedTaskController getPinnedTaskController() {
        return this.mPinnedTaskController;
    }

    boolean hasAccess(int uid) {
        if (!this.mDisplay.hasAccess(uid)) {
            return false;
        }
        if (!this.mVisibleBackgroundUserEnabled || isPrivate()) {
            return true;
        }
        int userId = android.os.UserHandle.getUserId(uid);
        return userId == 0 || this.mWmService.mUmInternal.isUserVisible(userId, this.mDisplayId);
    }

    boolean isPrivate() {
        return (this.mDisplay.getFlags() & 4) != 0;
    }

    boolean isTrusted() {
        return this.mDisplay.isTrusted();
    }

    com.android.server.wm.Task getRootTask(final int windowingMode, final int activityType) {
        return (com.android.server.wm.Task) getItemFromTaskDisplayAreas(new java.util.function.Function() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda34
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.wm.TaskDisplayArea) obj).getRootTask(windowingMode, activityType);
            }
        });
    }

    static /* synthetic */ boolean lambda$getRootTask$15(int rootTaskId, com.android.server.wm.Task rootTask) {
        return rootTask.getRootTaskId() == rootTaskId;
    }

    com.android.server.wm.Task getRootTask(final int rootTaskId) {
        return getRootTask(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda18
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.DisplayContent.lambda$getRootTask$15(rootTaskId, (com.android.server.wm.Task) obj);
            }
        });
    }

    int getRootTaskCount() {
        final int[] count = new int[1];
        forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda24
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.DisplayContent.lambda$getRootTaskCount$16(count, (com.android.server.wm.Task) obj);
            }
        });
        return count[0];
    }

    static /* synthetic */ void lambda$getRootTaskCount$16(int[] count, com.android.server.wm.Task task) {
        count[0] = count[0] + 1;
    }

    com.android.server.wm.Task getTopRootTask() {
        return getRootTask(alwaysTruePredicate());
    }

    int getCurrentOverrideConfigurationChanges() {
        return this.mCurrentOverrideConfigurationChanges;
    }

    int getInitialDisplayDensity() {
        int density = this.mInitialDisplayDensity;
        if (this.mMaxUiWidth > 0 && this.mInitialDisplayWidth > this.mMaxUiWidth) {
            return (int) ((this.mMaxUiWidth * density) / this.mInitialDisplayWidth);
        }
        return density;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(android.content.res.Configuration newParentConfig) {
        int lastOrientation = getConfiguration().orientation;
        int lastWindowingMode = getWindowingMode();
        super.onConfigurationChanged(newParentConfig);
        if (this.mDisplayPolicy != null) {
            this.mDisplayPolicy.onConfigurationChanged();
            this.mPinnedTaskController.onPostDisplayConfigurationChanged();
            this.mMinSizeOfResizeableTaskDp = getMinimalTaskSizeDp();
        }
        this.mDisplayContentExt.updateRotation(this.mDisplayContent, true);
        updateImeParent();
        if (this.mContentRecorder != null) {
            this.mContentRecorder.onConfigurationChanged(lastOrientation, lastWindowingMode);
        }
        if (lastOrientation != getConfiguration().orientation) {
            getMetricsLogger().write(new android.metrics.LogMaker(1659).setSubtype(getConfiguration().orientation).addTaggedData(1660, java.lang.Integer.valueOf(getDisplayId())));
            this.mDisplayContentExt.onConfigurationChanged(newParentConfig);
        }
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    boolean fillsParent() {
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isVisible() {
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isVisibleRequested() {
        return (!isVisible() || this.mRemoved || this.mRemoving) ? false : true;
    }

    @Override // com.android.server.wm.WindowContainer
    void onAppTransitionDone() {
        super.onAppTransitionDone();
        this.mWmService.mWindowsChanged = true;
        onTransitionFinished();
    }

    void onTransitionFinished() {
        if (this.mFixedRotationLaunchingApp != null && !this.mFixedRotationLaunchingApp.isVisibleRequested() && !this.mFixedRotationLaunchingApp.isVisible() && !this.mDisplayRotation.isRotatingSeamlessly()) {
            clearFixedRotationLaunchingApp();
        }
        this.mDisplayContentExt.onAppTransitionDone();
    }

    boolean forAllImeWindows(com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> callback, boolean traverseTopToBottom) {
        return this.mImeWindowsContainer.forAllWindowForce(callback, traverseTopToBottom);
    }

    @Override // com.android.server.wm.WindowContainer
    int getOrientation() {
        int compatOrientation;
        if (((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageFixedOrientation(this.mDisplayId)) {
            if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                android.util.Slog.d(TAG, "getOrientation MirageCarMode user");
                return 2;
            }
            return 2;
        }
        int splitRequestedOrientation = this.mDisplayContentExt.getSplitRequestedOrientation();
        if (splitRequestedOrientation != -2) {
            if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                android.util.Slog.d(TAG, "getOrientation splitRequestedOrientation " + splitRequestedOrientation);
            }
            return splitRequestedOrientation;
        }
        if (this.mWmService.mDisplayFrozen && this.mWmService.mPolicy.isKeyguardLocked()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                long protoLogParam0 = this.mDisplayId;
                long protoLogParam1 = getLastOrientation();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -9191821315942566105L, 5, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1));
            }
            return getLastOrientation();
        }
        if (this.mDisplayRotationCompatPolicy != null && (compatOrientation = this.mDisplayRotationCompatPolicy.getOrientation()) != -1) {
            this.mLastOrientationSource = null;
            return compatOrientation;
        }
        int orientation = super.getOrientation();
        if (!handlesOrientationChangeFromDescendant(orientation)) {
            com.android.server.wm.ActivityRecord topActivity = topRunningActivity(true);
            if (topActivity != null && topActivity.mLetterboxUiController.shouldUseDisplayLandscapeNaturalOrientation()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                    long protoLogParam02 = this.mDisplayId;
                    long protoLogParam12 = orientation;
                    java.lang.String protoLogParam3 = java.lang.String.valueOf(topActivity);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -74384795669614579L, 21, null, java.lang.Long.valueOf(protoLogParam02), java.lang.Long.valueOf(protoLogParam12), 0L, protoLogParam3);
                    return 0;
                }
                return 0;
            }
            this.mLastOrientationSource = null;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                long protoLogParam03 = this.mDisplayId;
                long protoLogParam13 = orientation;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -3395592185328682328L, 21, null, java.lang.Long.valueOf(protoLogParam03), java.lang.Long.valueOf(protoLogParam13), -1L);
            }
            return -1;
        }
        if (orientation == -2) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                long protoLogParam14 = this.mDisplayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 3438870491084701232L, 5, null, -1L, java.lang.Long.valueOf(protoLogParam14));
            }
            return -1;
        }
        com.android.server.wm.WindowContainer orientationSource = getLastOrientationSource();
        int newOrientation = this.mDisplayContentExt.getFixedScreenOrientation(orientationSource, orientation);
        if (newOrientation != orientation) {
            if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                android.util.Slog.d(TAG, "getOrientation " + orientationSource + " origin " + orientation + " newOrientation " + newOrientation);
            }
            return newOrientation;
        }
        return orientation;
    }

    void updateDisplayInfo(android.view.DisplayInfo newDisplayInfo) {
        updateBaseDisplayMetricsIfNeeded(newDisplayInfo);
        com.android.server.wm.utils.DisplayInfoOverrides.copyDisplayInfoFields(this.mDisplayInfo, newDisplayInfo, this.mLastDisplayInfoOverride, com.android.server.wm.utils.DisplayInfoOverrides.WM_OVERRIDE_FIELDS);
        this.mDisplayInfo.getAppMetrics(this.mDisplayMetrics, this.mDisplay.getDisplayAdjustments());
        onDisplayInfoChanged();
        onDisplayChanged(this);
    }

    void updatePrivacyIndicatorBounds(android.graphics.Rect[] staticBounds) {
        android.view.PrivacyIndicatorBounds oldBounds = this.mCurrentPrivacyIndicatorBounds;
        this.mCurrentPrivacyIndicatorBounds = this.mCurrentPrivacyIndicatorBounds.updateStaticBounds(staticBounds);
        if (!java.util.Objects.equals(oldBounds, this.mCurrentPrivacyIndicatorBounds)) {
            updateDisplayFrames(true);
        }
    }

    void onDisplayInfoChanged() {
        updateDisplayFrames(false);
        this.mInputMonitor.layoutInputConsumers(this.mDisplayInfo.logicalWidth, this.mDisplayInfo.logicalHeight);
        this.mDisplayPolicy.onDisplayInfoChanged(this.mDisplayInfo);
    }

    private void updateDisplayFrames(boolean notifyInsetsChange) {
        if (updateDisplayFrames(this.mDisplayFrames, this.mDisplayInfo.rotation, this.mDisplayInfo.logicalWidth, this.mDisplayInfo.logicalHeight)) {
            this.mInsetsStateController.onDisplayFramesUpdated(notifyInsetsChange);
        }
    }

    boolean updateDisplayFrames(com.android.server.wm.DisplayFrames displayFrames, int rotation, int w, int h) {
        return displayFrames.update(rotation, w, h, calculateDisplayCutoutForRotation(rotation), calculateRoundedCornersForRotation(rotation), calculatePrivacyIndicatorBoundsForRotation(rotation), calculateDisplayShapeForRotation(rotation));
    }

    @Override // com.android.server.wm.WindowContainer
    void onDisplayChanged(com.android.server.wm.DisplayContent dc) {
        super.onDisplayChanged(dc);
        updateSystemGestureExclusionLimit();
        this.mDisplayContentExt.hookOnDisplayChanged(dc);
    }

    void updateSystemGestureExclusionLimit() {
        int newConstantsLimitDp = this.mDisplayContentExt.adjustConstantSystemGestureExclusionLimitDp(this.mWmService.mConstants.mSystemGestureExclusionLimitDp, this);
        this.mSystemGestureExclusionLimit = (this.mDisplayMetrics.densityDpi * newConstantsLimitDp) / 160;
        updateSystemGestureExclusion();
    }

    void initializeDisplayBaseInfo() {
        android.hardware.display.DisplayManagerInternal displayManagerInternal = this.mWmService.mDisplayManagerInternal;
        if (displayManagerInternal != null) {
            android.view.DisplayInfo newDisplayInfo = displayManagerInternal.getDisplayInfo(this.mDisplayId);
            if (newDisplayInfo != null) {
                this.mDisplayInfo.copyFrom(newDisplayInfo);
            }
            this.mDwpcHelper = new com.android.server.wm.DisplayWindowPolicyControllerHelper(this);
        }
        updateBaseDisplayMetrics(this.mDisplayInfo.logicalWidth, this.mDisplayInfo.logicalHeight, this.mDisplayInfo.logicalDensityDpi, this.mDisplayInfo.physicalXDpi, this.mDisplayInfo.physicalYDpi);
        this.mInitialDisplayWidth = this.mDisplayInfo.logicalWidth;
        this.mInitialDisplayHeight = this.mDisplayInfo.logicalHeight;
        this.mInitialDisplayDensity = this.mDisplayInfo.logicalDensityDpi;
        this.mInitialPhysicalXDpi = this.mDisplayInfo.physicalXDpi;
        this.mInitialPhysicalYDpi = this.mDisplayInfo.physicalYDpi;
        this.mInitialDisplayCutout = this.mDisplayInfo.displayCutout;
        this.mInitialRoundedCorners = this.mDisplayInfo.roundedCorners;
        this.mCurrentPrivacyIndicatorBounds = new android.view.PrivacyIndicatorBounds(new android.graphics.Rect[4], this.mDisplayInfo.rotation);
        this.mInitialDisplayShape = this.mDisplayInfo.displayShape;
        android.view.Display.Mode maxDisplayMode = android.util.DisplayUtils.getMaximumResolutionDisplayMode(this.mDisplayInfo.supportedModes);
        this.mPhysicalDisplaySize = new android.graphics.Point(maxDisplayMode == null ? this.mInitialDisplayWidth : maxDisplayMode.getPhysicalWidth(), maxDisplayMode == null ? this.mInitialDisplayHeight : maxDisplayMode.getPhysicalHeight());
    }

    private void updateBaseDisplayMetricsIfNeeded(android.view.DisplayInfo newDisplayInfo) {
        android.view.DisplayCutout displayCutout;
        float newXDpi;
        int newDensity;
        android.view.DisplayShape newDisplayShape;
        android.view.RoundedCorners newRoundedCorners;
        java.lang.String newUniqueId;
        android.view.DisplayCutout newCutout;
        float newYDpi;
        this.mDisplayInfo.copyFrom(newDisplayInfo);
        int currentRotation = getRotation();
        int orientation = this.mDisplayInfo.rotation;
        boolean rotated = orientation == 1 || orientation == 3;
        android.view.DisplayInfo displayInfo = this.mDisplayInfo;
        int newWidth = rotated ? displayInfo.logicalHeight : displayInfo.logicalWidth;
        android.view.DisplayInfo displayInfo2 = this.mDisplayInfo;
        int newHeight = rotated ? displayInfo2.logicalWidth : displayInfo2.logicalHeight;
        int newDensity2 = this.mDisplayInfo.logicalDensityDpi;
        float newXDpi2 = this.mDisplayInfo.physicalXDpi;
        float newYDpi2 = this.mDisplayInfo.physicalYDpi;
        if (!this.mIgnoreDisplayCutout) {
            displayCutout = this.mDisplayInfo.displayCutout;
        } else {
            displayCutout = android.view.DisplayCutout.NO_CUTOUT;
        }
        android.view.DisplayCutout newCutout2 = displayCutout;
        java.lang.String newUniqueId2 = this.mDisplayInfo.uniqueId;
        android.view.RoundedCorners newRoundedCorners2 = this.mDisplayInfo.roundedCorners;
        android.view.DisplayShape newDisplayShape2 = this.mDisplayInfo.displayShape;
        boolean displayMetricsChanged = (this.mInitialDisplayWidth == newWidth && this.mInitialDisplayHeight == newHeight && this.mInitialDisplayDensity == newDensity2 && this.mInitialPhysicalXDpi == newXDpi2 && this.mInitialPhysicalYDpi == newYDpi2 && java.util.Objects.equals(this.mInitialDisplayCutout, newCutout2) && java.util.Objects.equals(this.mInitialRoundedCorners, newRoundedCorners2) && java.util.Objects.equals(this.mInitialDisplayShape, newDisplayShape2)) ? false : true;
        boolean physicalDisplayChanged = !newUniqueId2.equals(this.mCurrentUniqueDisplayId);
        if (displayMetricsChanged || physicalDisplayChanged) {
            if (physicalDisplayChanged) {
                this.mWmService.mDisplayWindowSettings.applySettingsToDisplayLocked(this, false);
                newXDpi = newXDpi2;
                newDensity = newDensity2;
                newDisplayShape = newDisplayShape2;
                newRoundedCorners = newRoundedCorners2;
                newUniqueId = newUniqueId2;
                newCutout = newCutout2;
                newYDpi = newYDpi2;
                this.mDisplayUpdater.onDisplayContentDisplayPropertiesPreChanged(this.mDisplayId, this.mInitialDisplayWidth, this.mInitialDisplayHeight, newWidth, newHeight);
                this.mDisplayRotation.physicalDisplayChanged();
                this.mDisplayPolicy.physicalDisplayChanged();
            } else {
                newXDpi = newXDpi2;
                newDensity = newDensity2;
                newDisplayShape = newDisplayShape2;
                newRoundedCorners = newRoundedCorners2;
                newUniqueId = newUniqueId2;
                newCutout = newCutout2;
                newYDpi = newYDpi2;
            }
            android.view.RoundedCorners newRoundedCorners3 = newRoundedCorners;
            java.lang.String newUniqueId3 = newUniqueId;
            int i = this.mIsSizeForced ? this.mBaseDisplayWidth : newWidth;
            float newXDpi3 = newXDpi;
            android.view.DisplayCutout newCutout3 = newCutout;
            int newDensity3 = newDensity;
            float newYDpi3 = newYDpi;
            updateBaseDisplayMetrics(i, this.mIsSizeForced ? this.mBaseDisplayHeight : newHeight, this.mIsDensityForced ? this.mBaseDisplayDensity : newDensity, this.mIsSizeForced ? this.mBaseDisplayPhysicalXDpi : newXDpi, this.mIsSizeForced ? this.mBaseDisplayPhysicalYDpi : newYDpi);
            configureDisplayPolicy();
            if (physicalDisplayChanged) {
                this.mWmService.mDisplayWindowSettings.applyRotationSettingsToDisplayLocked(this);
                this.mDisplayContentExt.physicalDisplayChanged(this);
            }
            this.mInitialDisplayWidth = newWidth;
            this.mInitialDisplayHeight = newHeight;
            this.mInitialDisplayDensity = newDensity3;
            this.mInitialPhysicalXDpi = newXDpi3;
            this.mInitialPhysicalYDpi = newYDpi3;
            this.mInitialDisplayCutout = newCutout3;
            this.mInitialRoundedCorners = newRoundedCorners3;
            this.mInitialDisplayShape = newDisplayShape;
            this.mCurrentUniqueDisplayId = newUniqueId3;
            reconfigureDisplayLocked();
            if (physicalDisplayChanged) {
                this.mDisplayContentExt.physicalDisplayChangedAfterConfig(this);
                android.util.Slog.d(TAG, "physicalDisplayChange to " + newUniqueId3 + " size:" + newWidth + "x" + newHeight);
                this.mDisplayPolicy.physicalDisplayUpdated();
                this.mDisplayUpdater.onDisplayContentDisplayPropertiesPostChanged(currentRotation, getRotation(), getDisplayAreaInfo());
            }
        }
    }

    void setMaxUiWidth(int width) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DISPLAY) {
            android.util.Slog.v(TAG, "Setting max ui width:" + width + " on display:" + getDisplayId());
        }
        this.mMaxUiWidth = width;
        updateBaseDisplayMetrics(this.mBaseDisplayWidth, this.mBaseDisplayHeight, this.mBaseDisplayDensity, this.mBaseDisplayPhysicalXDpi, this.mBaseDisplayPhysicalYDpi);
    }

    void updateBaseDisplayMetrics(int baseWidth, int baseHeight, int baseDensity, float baseXDpi, float baseYDpi) {
        this.mBaseDisplayWidth = baseWidth;
        this.mBaseDisplayHeight = baseHeight;
        if (android.view.DynamicLoggerObserver.isLogToolRun() && this.mBaseDisplayDensity != baseDensity) {
            android.util.Slog.d(TAG, "updateBaseDisplayMetrics density: " + this.mBaseDisplayDensity + " callers: " + android.os.Debug.getCallers(5));
        }
        this.mBaseDisplayDensity = baseDensity;
        this.mBaseDisplayPhysicalXDpi = baseXDpi;
        this.mBaseDisplayPhysicalYDpi = baseYDpi;
        if (this.mIsSizeForced) {
            this.mBaseDisplayCutout = loadDisplayCutout(baseWidth, baseHeight);
            this.mBaseRoundedCorners = loadRoundedCorners(baseWidth, baseHeight);
        }
        if (this.mMaxUiWidth > 0 && this.mBaseDisplayWidth > this.mMaxUiWidth) {
            float ratio = this.mMaxUiWidth / this.mBaseDisplayWidth;
            this.mBaseDisplayHeight = (int) (this.mBaseDisplayHeight * ratio);
            this.mBaseDisplayWidth = this.mMaxUiWidth;
            this.mBaseDisplayPhysicalXDpi *= ratio;
            this.mBaseDisplayPhysicalYDpi *= ratio;
            if (!this.mIsDensityForced) {
                this.mBaseDisplayDensity = (int) (this.mBaseDisplayDensity * ratio);
                if (android.view.DynamicLoggerObserver.isLogToolRun()) {
                    android.util.Slog.d(TAG, "updateBaseDisplayMetrics mIsDensityForced: false, density: " + this.mBaseDisplayDensity);
                }
            }
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DISPLAY) {
                android.util.Slog.v(TAG, "Applying config restraints:" + this.mBaseDisplayWidth + "x" + this.mBaseDisplayHeight + " on display:" + getDisplayId());
            }
        }
        if (this.mDisplayReady && !this.mDisplayPolicy.shouldKeepCurrentDecorInsets()) {
            this.mDisplayPolicy.mDecorInsets.invalidate();
        }
    }

    void setForcedDensity(int density, int userId) {
        this.mIsDensityForced = density != getInitialDisplayDensity();
        boolean updateCurrent = userId == -2;
        if (this.mWmService.mCurrentUserId == userId || updateCurrent) {
            this.mBaseDisplayDensity = density;
            reconfigureDisplayLocked();
        }
        if (updateCurrent) {
            return;
        }
        if (density == getInitialDisplayDensity()) {
            density = 0;
        }
        this.mWmService.mDisplayWindowSettings.setForcedDensity(getDisplayInfo(), density, userId);
    }

    void setForcedScalingMode(int mode) {
        if (mode != 1) {
            mode = 0;
        }
        this.mDisplayScalingDisabled = mode != 0;
        android.util.Slog.i(TAG, "Using display scaling mode: " + (this.mDisplayScalingDisabled ? kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF : "auto"));
        reconfigureDisplayLocked();
        this.mWmService.mDisplayWindowSettings.setForcedScalingMode(this, mode);
    }

    void setForcedSize(int width, int height) {
        setForcedSize(width, height, INVALID_DPI, INVALID_DPI);
    }

    void setForcedSize(int width, int height, float xDPI, float yDPI) {
        if (this.mMaxUiWidth > 0 && width > this.mMaxUiWidth) {
            float ratio = this.mMaxUiWidth / width;
            height = (int) (height * ratio);
            width = this.mMaxUiWidth;
        }
        this.mIsSizeForced = (this.mInitialDisplayWidth == width && this.mInitialDisplayHeight == height) ? false : true;
        if (this.mIsSizeForced) {
            android.graphics.Point size = getValidForcedSize(width, height);
            width = size.x;
            height = size.y;
        }
        android.util.Slog.i(TAG, "Using new display size: " + width + "x" + height);
        updateBaseDisplayMetrics(width, height, this.mBaseDisplayDensity, xDPI != INVALID_DPI ? xDPI : this.mBaseDisplayPhysicalXDpi, yDPI != INVALID_DPI ? yDPI : this.mBaseDisplayPhysicalYDpi);
        reconfigureDisplayLocked();
        if (!this.mIsSizeForced) {
            height = 0;
            width = 0;
        }
        this.mWmService.mDisplayWindowSettings.setForcedSize(this, width, height);
    }

    android.graphics.Point getValidForcedSize(int w, int h) {
        int maxSize = java.lang.Math.max(this.mInitialDisplayWidth, this.mInitialDisplayHeight) * 3;
        return new android.graphics.Point(java.lang.Math.min(java.lang.Math.max(w, 200), maxSize), java.lang.Math.min(java.lang.Math.max(h, 200), maxSize));
    }

    android.view.DisplayCutout loadDisplayCutout(int displayWidth, int displayHeight) {
        if (this.mDisplayPolicy == null || this.mInitialDisplayCutout == null) {
            return null;
        }
        return android.view.DisplayCutout.fromResourcesRectApproximation(this.mDisplayPolicy.getSystemUiContext().getResources(), this.mDisplayInfo.uniqueId, this.mPhysicalDisplaySize.x, this.mPhysicalDisplaySize.y, displayWidth, displayHeight);
    }

    android.view.RoundedCorners loadRoundedCorners(int displayWidth, int displayHeight) {
        if (this.mDisplayPolicy == null || this.mInitialRoundedCorners == null) {
            return null;
        }
        return android.view.RoundedCorners.fromResources(this.mDisplayPolicy.getSystemUiContext().getResources(), this.mDisplayInfo.uniqueId, this.mPhysicalDisplaySize.x, this.mPhysicalDisplaySize.y, displayWidth, displayHeight);
    }

    @Override // com.android.server.wm.DisplayArea
    void getStableRect(android.graphics.Rect out) {
        android.view.InsetsState state = this.mDisplayContent.getInsetsStateController().getRawInsetsState();
        out.set(state.getDisplayFrame());
        out.inset(state.calculateInsets(out, android.view.WindowInsets.Type.systemBars(), true));
    }

    com.android.server.wm.TaskDisplayArea getDefaultTaskDisplayArea() {
        return this.mDisplayAreaPolicy.getDefaultTaskDisplayArea();
    }

    void updateDisplayAreaOrganizers() {
        if (!isTrusted()) {
            return;
        }
        forAllDisplayAreas(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda56
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$updateDisplayAreaOrganizers$17((com.android.server.wm.DisplayArea) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDisplayAreaOrganizers$17(com.android.server.wm.DisplayArea displayArea) {
        android.window.IDisplayAreaOrganizer organizer;
        if (!displayArea.isOrganized() && (organizer = this.mAtmService.mWindowOrganizerController.mDisplayAreaOrganizerController.getOrganizerByFeature(displayArea.mFeatureId)) != null) {
            displayArea.setOrganizer(organizer);
        }
    }

    com.android.server.wm.Task findTaskForResizePoint(final int x, final int y) {
        final int delta = com.android.server.wm.WindowManagerService.dipToPixel(30, this.mDisplayMetrics);
        return (com.android.server.wm.Task) getItemFromTaskDisplayAreas(new java.util.function.Function() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda55
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$findTaskForResizePoint$18(x, y, delta, (com.android.server.wm.TaskDisplayArea) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.wm.Task lambda$findTaskForResizePoint$18(int x, int y, int delta, com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        return this.mTmpTaskForResizePointSearchResult.process(taskDisplayArea, x, y, delta);
    }

    @Override // com.android.server.wm.WindowContainer
    void switchUser(int userId) {
        super.switchUser(userId);
        this.mWmService.mWindowsChanged = true;
        this.mDisplayPolicy.switchUser();
    }

    private boolean shouldDeferRemoval() {
        return isAnimating(3) || this.mTransitionController.isTransitionOnDisplay(this);
    }

    @Override // com.android.server.wm.WindowContainer
    void removeIfPossible() {
        if (shouldDeferRemoval()) {
            this.mDeferredRemoval = true;
        } else {
            removeImmediately();
        }
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    void removeImmediately() {
        this.mDeferredRemoval = false;
        try {
            this.mOpeningApps.clear();
            this.mClosingApps.clear();
            this.mChangingContainers.clear();
            this.mUnknownAppVisibilityController.clear();
            this.mAppTransition.removeAppTransitionTimeoutCallbacks();
            this.mTransitionController.unregisterLegacyListener(this.mFixedRotationTransitionListener);
            handleAnimatingStoppedAndTransition();
            this.mWmService.stopFreezingDisplayLocked();
            this.mDeviceStateController.unregisterDeviceStateCallback(this.mDeviceStateConsumer);
            super.removeImmediately();
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DISPLAY) {
                android.util.Slog.v(TAG, "Removing display=" + this);
            }
            this.mPointerEventDispatcher.dispose();
            setRotationAnimation(null);
            setRemoteInsetsController(null);
            this.mOverlayLayer.release();
            this.mInputOverlayLayer.release();
            this.mA11yOverlayLayer.release();
            this.mInputMonitor.onDisplayRemoved();
            this.mWmService.mDisplayNotificationController.dispatchDisplayRemoved(this);
            this.mDisplayRotation.onDisplayRemoved();
            this.mWmService.mAccessibilityController.onDisplayRemoved(this.mDisplayId);
            this.mRootWindowContainer.mTaskSupervisor.getKeyguardController().onDisplayRemoved(this.mDisplayId);
            this.mWallpaperController.resetLargestDisplay(this.mDisplay);
            this.mWmService.mDisplayWindowSettings.onDisplayRemoved(this);
            this.mDisplayReady = false;
            getPendingTransaction().apply();
            this.mWmService.mWindowPlacerLocked.requestTraversal();
            if (this.mDisplayRotationCompatPolicy != null) {
                this.mDisplayRotationCompatPolicy.dispose();
            }
            if (this.mCameraCompatFreeformPolicy != null) {
                this.mCameraCompatFreeformPolicy.dispose();
            }
            if (this.mCameraStateMonitor != null) {
                this.mCameraStateMonitor.dispose();
            }
        } catch (java.lang.Throwable th) {
            this.mDisplayReady = false;
            throw th;
        }
    }

    @Override // com.android.server.wm.WindowContainer
    boolean handleCompleteDeferredRemoval() {
        boolean stillDeferringRemoval = super.handleCompleteDeferredRemoval() || shouldDeferRemoval();
        if (!stillDeferringRemoval && this.mDeferredRemoval) {
            removeImmediately();
            return false;
        }
        return stillDeferringRemoval;
    }

    void adjustForImeIfNeeded() {
        com.android.server.wm.WindowState imeWin = this.mInputMethodWindow;
        boolean imeVisible = imeWin != null && imeWin.isVisible() && imeWin.isDisplayed();
        int imeHeight = getInputMethodWindowVisibleHeight();
        this.mPinnedTaskController.setAdjustedForIme(imeVisible, imeHeight);
        this.mDisplayContentExt.hookAdjustForImeIfNeeded(imeWin, imeVisible, imeHeight, this.mImeInputTarget == null ? null : this.mImeInputTarget.getWindowState(), this.mCurrentFocus);
    }

    int getInputMethodWindowVisibleHeight() {
        android.view.InsetsState state = getInsetsStateController().getRawInsetsState();
        android.view.InsetsSource imeSource = state.peekSource(android.view.InsetsSource.ID_IME);
        if (imeSource == null || !imeSource.isVisible()) {
            this.mDisplayContentExt.notifyIMELayoutChanged(false, 0, 0);
            return 0;
        }
        android.graphics.Rect imeFrame = imeSource.getVisibleFrame() != null ? imeSource.getVisibleFrame() : imeSource.getFrame();
        android.graphics.Rect dockFrame = this.mTmpRect;
        dockFrame.set(state.getDisplayFrame());
        dockFrame.inset(state.calculateInsets(dockFrame, android.view.WindowInsets.Type.systemBars() | android.view.WindowInsets.Type.displayCutout(), false));
        this.mDisplayContentExt.notifyIMELayoutChanged(true, imeFrame.top, dockFrame.bottom);
        return dockFrame.bottom - imeFrame.top;
    }

    void enableHighPerfTransition(boolean enable) {
        if (!this.mWmService.mSupportsHighPerfTransitions) {
            return;
        }
        if (!com.android.window.flags.Flags.explicitRefreshRateHints()) {
            if (enable) {
                getPendingTransaction().setEarlyWakeupStart();
                return;
            } else {
                getPendingTransaction().setEarlyWakeupEnd();
                return;
            }
        }
        if (enable) {
            if (this.mTransitionPrefSession == null) {
                this.mTransitionPrefSession = this.mWmService.mSystemPerformanceHinter.createSession(3, this.mDisplayId, "Transition");
            }
            this.mTransitionPrefSession.start();
        } else if (this.mTransitionPrefSession != null) {
            this.mTransitionPrefSession.close();
        }
    }

    void enableHighFrameRate(boolean enable) {
        if (!com.android.window.flags.Flags.explicitRefreshRateHints()) {
            return;
        }
        if (enable) {
            if (this.mHighFrameRateSession == null) {
                this.mHighFrameRateSession = this.mWmService.mSystemPerformanceHinter.createSession(2, this.mDisplayId, "WindowAnimation");
            }
            this.mHighFrameRateSession.start();
        } else if (this.mHighFrameRateSession != null) {
            this.mHighFrameRateSession.close();
        }
    }

    void rotateBounds(int oldRotation, int newRotation, android.graphics.Rect inOutBounds) {
        getBounds(this.mTmpRect, oldRotation);
        android.util.RotationUtils.rotateBounds(inOutBounds, this.mTmpRect, oldRotation, newRotation);
    }

    public void setRotationAnimation(com.android.server.wm.ScreenRotationAnimation screenRotationAnimation) {
        com.android.server.wm.ScreenRotationAnimation prev = this.mScreenRotationAnimation;
        this.mScreenRotationAnimation = screenRotationAnimation;
        if (prev != null) {
            prev.kill();
        }
        if (screenRotationAnimation != null && screenRotationAnimation.hasScreenshot()) {
            startAsyncRotationIfNeeded();
        }
    }

    public com.android.server.wm.ScreenRotationAnimation getRotationAnimation() {
        return this.mScreenRotationAnimation;
    }

    void collectDisplayChange(com.android.server.wm.Transition transition) {
        if (this.mLastHasContent && transition.isCollecting()) {
            if (!transition.mParticipants.contains(this)) {
                transition.collect(this);
                startAsyncRotationIfNeeded();
                if (this.mFixedRotationLaunchingApp != null) {
                    setSeamlessTransitionForFixedRotation(transition);
                    return;
                }
                return;
            }
            if (this.mAsyncRotationController != null && !isRotationChanging()) {
                android.util.Slog.i(TAG, "Finish AsyncRotation for previous intermediate change");
                finishAsyncRotationIfPossible();
            }
        }
    }

    void requestChangeTransition(int changes, android.window.TransitionRequestInfo.DisplayChange displayChange) {
        if (this.mDisplayContentExt.skipTransitionAnimationIfNeed(changes, displayChange, this.mTransitionController, this.mFocusedApp)) {
            android.util.Slog.d(TAG, "requestChangeTransition skip");
            return;
        }
        com.android.server.wm.TransitionController controller = this.mTransitionController;
        com.android.server.wm.Transition t = controller.requestStartDisplayTransition(6, 0, this, null, displayChange);
        t.collect(this);
        this.mAtmService.startPowerMode(2);
        if (this.mAsyncRotationController != null) {
            this.mAsyncRotationController.updateRotation();
        }
        if (this.mFixedRotationLaunchingApp != null) {
            setSeamlessTransitionForFixedRotation(t);
        } else if (isRotationChanging()) {
            if (displayChange != null) {
                boolean seamless = this.mDisplayRotation.shouldRotateSeamlessly(displayChange.getStartRotation(), displayChange.getEndRotation(), false);
                if (seamless) {
                    if (this.mDisplayContentExt.requestSeamlessExplicit()) {
                        t.setSeamlessRotation(this);
                    } else {
                        t.onSeamlessRotating(this);
                    }
                }
            }
            this.mWmService.mLatencyTracker.onActionStart(6);
            controller.mTransitionMetricsReporter.associate(t.getToken(), new java.util.function.LongConsumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda27
                @Override // java.util.function.LongConsumer
                public final void accept(long j) {
                    this.f$0.lambda$requestChangeTransition$19(j);
                }
            });
            startAsyncRotation(false);
        }
        t.setKnownConfigChanges(this, changes);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestChangeTransition$19(long startTime) {
        this.mWmService.mLatencyTracker.onActionEnd(6);
    }

    private void setSeamlessTransitionForFixedRotation(com.android.server.wm.Transition t) {
        t.setSeamlessRotation(this);
        if (this.mAsyncRotationController != null) {
            this.mAsyncRotationController.keepAppearanceInPreviousRotation();
        } else {
            android.util.Slog.d(TAG, "No AsyncRotationController , so start it right now.");
            this.mDisplayContentExt.startAsyncRotationIfNeeded();
        }
    }

    @Override // com.android.server.wm.WindowContainer
    boolean inTransition() {
        return this.mScreenRotationAnimation != null || super.inTransition();
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        if (logLevel == 2 && !isVisible()) {
            return;
        }
        long token = proto.start(fieldId);
        super.dumpDebug(proto, 1146756268053L, logLevel);
        proto.write(1120986464258L, this.mDisplayId);
        proto.write(1120986464265L, this.mBaseDisplayDensity);
        this.mDisplayInfo.dumpDebug(proto, 1146756268042L);
        this.mDisplayRotation.dumpDebug(proto, 1146756268065L);
        com.android.server.wm.ScreenRotationAnimation screenRotationAnimation = getRotationAnimation();
        if (screenRotationAnimation != null) {
            screenRotationAnimation.dumpDebug(proto, 1146756268044L);
        }
        this.mDisplayFrames.dumpDebug(proto, 1146756268045L);
        proto.write(1120986464295L, this.mMinSizeOfResizeableTaskDp);
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            this.mTransitionController.dumpDebugLegacy(proto, 1146756268048L);
        } else {
            this.mAppTransition.dumpDebug(proto, 1146756268048L);
        }
        if (this.mFocusedApp != null) {
            this.mFocusedApp.writeNameToProto(proto, 1138166333455L);
        }
        for (int i = this.mOpeningApps.size() - 1; i >= 0; i--) {
            this.mOpeningApps.valueAt(i).writeIdentifierToProto(proto, 2246267895825L);
        }
        for (int i2 = this.mClosingApps.size() - 1; i2 >= 0; i2--) {
            this.mClosingApps.valueAt(i2).writeIdentifierToProto(proto, 2246267895826L);
        }
        com.android.server.wm.Task focusedRootTask = getFocusedRootTask();
        if (focusedRootTask != null) {
            proto.write(1120986464279L, focusedRootTask.getRootTaskId());
            com.android.server.wm.ActivityRecord focusedActivity = focusedRootTask.getDisplayArea().getFocusedActivity();
            if (focusedActivity != null) {
                focusedActivity.writeIdentifierToProto(proto, 1146756268056L);
            }
        } else {
            proto.write(1120986464279L, -1);
        }
        proto.write(1133871366170L, isReady());
        proto.write(1133871366180L, isSleeping());
        for (int i3 = 0; i3 < this.mAllSleepTokens.size(); i3++) {
            this.mAllSleepTokens.get(i3).writeTagToProto(proto, 2237677961253L);
        }
        if (this.mImeLayeringTarget != null) {
            this.mImeLayeringTarget.dumpDebug(proto, 1146756268059L, logLevel);
        }
        if (this.mImeInputTarget != null) {
            this.mImeInputTarget.dumpProto(proto, 1146756268060L, logLevel);
        }
        if (this.mImeControlTarget != null && this.mImeControlTarget.getWindow() != null) {
            this.mImeControlTarget.getWindow().dumpDebug(proto, 1146756268061L, logLevel);
        }
        if (this.mCurrentFocus != null) {
            this.mCurrentFocus.dumpDebug(proto, 1146756268062L, logLevel);
        }
        if (this.mInsetsStateController != null) {
            this.mInsetsStateController.dumpDebug(proto, logLevel);
        }
        proto.write(1120986464290L, getImePolicy());
        for (android.graphics.Rect r : getKeepClearAreas()) {
            r.dumpDebug(proto, 2246267895846L);
        }
        proto.end(token);
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268035L;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    public void dump(final java.io.PrintWriter pw, final java.lang.String prefix, final boolean dumpAll) {
        pw.print(prefix);
        pw.println("Display: mDisplayId=" + this.mDisplayId + (isOrganized() ? " (organized)" : ""));
        java.lang.String subPrefix = "  " + prefix;
        pw.print(subPrefix);
        pw.print("init=");
        pw.print(this.mInitialDisplayWidth);
        pw.print("x");
        pw.print(this.mInitialDisplayHeight);
        pw.print(" ");
        pw.print(this.mInitialDisplayDensity);
        pw.print("dpi");
        pw.print(" mMinSizeOfResizeableTaskDp=");
        pw.print(this.mMinSizeOfResizeableTaskDp);
        if (this.mInitialDisplayWidth != this.mBaseDisplayWidth || this.mInitialDisplayHeight != this.mBaseDisplayHeight || this.mInitialDisplayDensity != this.mBaseDisplayDensity) {
            pw.print(" base=");
            pw.print(this.mBaseDisplayWidth);
            pw.print("x");
            pw.print(this.mBaseDisplayHeight);
            pw.print(" ");
            pw.print(this.mBaseDisplayDensity);
            pw.print("dpi");
        }
        if (this.mDisplayScalingDisabled) {
            pw.println(" noscale");
        }
        pw.print(" cur=");
        pw.print(this.mDisplayInfo.logicalWidth);
        pw.print("x");
        pw.print(this.mDisplayInfo.logicalHeight);
        pw.print(" app=");
        pw.print(this.mDisplayInfo.appWidth);
        pw.print("x");
        pw.print(this.mDisplayInfo.appHeight);
        pw.print(" rng=");
        pw.print(this.mDisplayInfo.smallestNominalAppWidth);
        pw.print("x");
        pw.print(this.mDisplayInfo.smallestNominalAppHeight);
        pw.print("-");
        pw.print(this.mDisplayInfo.largestNominalAppWidth);
        pw.print("x");
        pw.println(this.mDisplayInfo.largestNominalAppHeight);
        pw.print("mIsDensityForced=");
        pw.println(this.mIsDensityForced);
        pw.print(subPrefix + "deferred=" + this.mDeferredRemoval + " mLayoutNeeded=" + this.mLayoutNeeded);
        pw.println();
        super.dump(pw, prefix, dumpAll);
        pw.print(prefix);
        pw.print("mLayoutSeq=");
        pw.println(this.mLayoutSeq);
        pw.print("  mCurrentFocus=");
        pw.println(this.mCurrentFocus);
        pw.print("  mFocusedApp=");
        pw.println(this.mFocusedApp);
        if (this.mFixedRotationLaunchingApp != null) {
            pw.println("  mFixedRotationLaunchingApp=" + this.mFixedRotationLaunchingApp);
        }
        if (this.mAsyncRotationController != null) {
            this.mAsyncRotationController.dump(pw, prefix);
        }
        pw.println();
        pw.print(prefix + "mHoldScreenWindow=");
        pw.print(this.mHoldScreenWindow);
        pw.println();
        pw.print(prefix + "mObscuringWindow=");
        pw.print(this.mObscuringWindow);
        pw.println();
        pw.print(prefix + "mLastWakeLockHoldingWindow=");
        pw.print(this.mLastWakeLockHoldingWindow);
        pw.println();
        pw.print(prefix + "mLastWakeLockObscuringWindow=");
        pw.println(this.mLastWakeLockObscuringWindow);
        pw.println();
        this.mWallpaperController.dump(pw, "  ");
        if (this.mSystemGestureExclusionListeners.getRegisteredCallbackCount() > 0) {
            pw.println();
            pw.print("  mSystemGestureExclusion=");
            pw.println(this.mSystemGestureExclusion);
        }
        java.util.Set<android.graphics.Rect> keepClearAreas = getKeepClearAreas();
        if (!keepClearAreas.isEmpty()) {
            pw.println();
            pw.print("  keepClearAreas=");
            pw.println(keepClearAreas);
        }
        pw.println();
        pw.println(prefix + "Display areas in top down Z order:");
        dumpChildDisplayArea(pw, subPrefix, dumpAll);
        pw.println();
        pw.println(prefix + "Task display areas in top down Z order:");
        forAllTaskDisplayAreas(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.TaskDisplayArea taskDisplayArea = (com.android.server.wm.TaskDisplayArea) obj;
                taskDisplayArea.dump(pw, prefix + "  ", dumpAll);
            }
        });
        pw.println();
        com.android.server.wm.ScreenRotationAnimation rotationAnimation = getRotationAnimation();
        if (rotationAnimation != null) {
            pw.println("  mScreenRotationAnimation:");
            rotationAnimation.printTo(subPrefix, pw);
        } else if (dumpAll) {
            pw.println("  no ScreenRotationAnimation ");
        }
        pw.println();
        com.android.server.wm.Task rootHomeTask = getDefaultTaskDisplayArea().getRootHomeTask();
        if (rootHomeTask != null) {
            pw.println(prefix + "rootHomeTask=" + rootHomeTask.getName());
        }
        com.android.server.wm.Task rootPinnedTask = getDefaultTaskDisplayArea().getRootPinnedTask();
        if (rootPinnedTask != null) {
            pw.println(prefix + "rootPinnedTask=" + rootPinnedTask.getName());
        }
        com.android.server.wm.Task rootRecentsTask = getDefaultTaskDisplayArea().getRootTask(0, 3);
        if (rootRecentsTask != null) {
            pw.println(prefix + "rootRecentsTask=" + rootRecentsTask.getName());
        }
        com.android.server.wm.Task rootDreamTask = getRootTask(0, 5);
        if (rootDreamTask != null) {
            pw.println(prefix + "rootDreamTask=" + rootDreamTask.getName());
        }
        pw.println();
        this.mPinnedTaskController.dump(prefix, pw);
        pw.println();
        this.mDisplayFrames.dump(prefix, pw);
        pw.println();
        this.mDisplayPolicy.dump(prefix, pw);
        pw.println();
        this.mDisplayRotation.dump(prefix, pw);
        pw.println();
        this.mInputMonitor.dump(pw, "  ");
        pw.println();
        this.mInsetsStateController.dump(prefix, pw);
        this.mInsetsPolicy.dump(prefix, pw);
        this.mDwpcHelper.dump(prefix, pw);
        pw.println();
        this.mAppTransition.dump(pw, subPrefix);
        if (this.mAsyncRotationController != null) {
            this.mAsyncRotationController.dump(pw, subPrefix);
        }
        if (this.mAllSleepTokens.size() > 0) {
            pw.println(prefix + " mAllSleepTokens:");
            for (int i = 0; i < this.mAllSleepTokens.size(); i++) {
                pw.println(prefix + "  " + this.mAllSleepTokens.get(i));
            }
        }
        pw.println(prefix + "isWaitingForRemoteDisplayChange=" + this.mRemoteDisplayChangeController.isWaitingForRemoteDisplayChange());
        this.mTransitionController.dump(pw, subPrefix, dumpAll);
        java.lang.Object flexibleActivityImeAnimationState = this.mDisplayContentExt.getFlexibleActivityImeAnimationState();
        if (flexibleActivityImeAnimationState != null) {
            pw.println(prefix + "flexibleActivityImeAnimationState=" + flexibleActivityImeAnimationState);
        }
    }

    @Override // com.android.server.wm.DisplayArea
    public java.lang.String toString() {
        return "Display{#" + this.mDisplayId + " state=" + android.view.Display.stateToString(this.mDisplayInfo.state) + " size=" + this.mDisplayInfo.logicalWidth + "x" + this.mDisplayInfo.logicalHeight + " " + android.view.Surface.rotationToString(this.mDisplayInfo.rotation) + "}";
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.ConfigurationContainer
    java.lang.String getName() {
        return "Display " + this.mDisplayId + " name=\"" + this.mDisplayInfo.name + "\"";
    }

    com.android.server.wm.WindowState getTouchableWinAtPointLocked(float xf, float yf) {
        final int x = (int) xf;
        final int y = (int) yf;
        com.android.server.wm.WindowState touchedWin = getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda37
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getTouchableWinAtPointLocked$21(x, y, (com.android.server.wm.WindowState) obj);
            }
        });
        return touchedWin;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getTouchableWinAtPointLocked$21(int x, int y, com.android.server.wm.WindowState w) {
        int flags = w.mAttrs.flags;
        if (!w.isVisible() || (flags & 16) != 0) {
            return false;
        }
        w.getVisibleBounds(this.mTmpRect);
        if (!this.mTmpRect.contains(x, y)) {
            return false;
        }
        w.getTouchableRegion(this.mTmpRegion);
        int touchFlags = flags & 40;
        return this.mTmpRegion.contains(x, y) || touchFlags == 0;
    }

    boolean canAddToastWindowForUid(final int uid) {
        com.android.server.wm.WindowState focusedWindowForUid = getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda32
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.DisplayContent.lambda$canAddToastWindowForUid$22(uid, (com.android.server.wm.WindowState) obj);
            }
        });
        if (focusedWindowForUid != null) {
            return true;
        }
        com.android.server.wm.WindowState win = getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda33
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.DisplayContent.lambda$canAddToastWindowForUid$23(uid, (com.android.server.wm.WindowState) obj);
            }
        });
        return win == null;
    }

    static /* synthetic */ boolean lambda$canAddToastWindowForUid$22(int uid, com.android.server.wm.WindowState w) {
        return w.mOwnerUid == uid && w.isFocused();
    }

    static /* synthetic */ boolean lambda$canAddToastWindowForUid$23(int uid, com.android.server.wm.WindowState w) {
        return w.mAttrs.type == 2005 && w.mOwnerUid == uid && !w.mPermanentlyHidden && !w.mWindowRemovalAllowed;
    }

    void scheduleToastWindowsTimeoutIfNeededLocked(com.android.server.wm.WindowState oldFocus, com.android.server.wm.WindowState newFocus) {
        if (oldFocus != null) {
            if (newFocus != null && newFocus.mOwnerUid == oldFocus.mOwnerUid) {
                return;
            }
            this.mTmpWindow = oldFocus;
            forAllWindows(this.mScheduleToastTimeout, false);
        }
    }

    boolean canStealTopFocus() {
        return (this.mDisplayInfo.flags & 4096) == 0;
    }

    com.android.server.wm.WindowState findFocusedWindowIfNeeded(int topFocusedDisplayId) {
        if (hasOwnFocus() || topFocusedDisplayId == -1 || ((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageDisplayEnabled() || this.mDisplayContentExt.isActivityPreloadDisplay(this)) {
            return findFocusedWindow();
        }
        return null;
    }

    com.android.server.wm.WindowState findFocusedWindow() {
        this.mTmpWindow = null;
        forAllWindows(this.mFindFocusedWindow, true);
        com.android.server.wm.WindowState findFocusWin = this.mTmpWindow;
        this.mDisplayContentExt.hookFocusWindowInQuickBack(this.mFindFocusedWindow, findFocusWin);
        if (this.mTmpWindow == null) {
            this.mTmpWindow = findFocusWin;
        }
        if (this.mTmpWindow == null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[1]) {
                long protoLogParam0 = getDisplayId();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -1123818872155982592L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
            return null;
        }
        return this.mTmpWindow;
    }

    boolean updateFocusedWindowLocked(int mode, boolean updateInputWindows, int topFocusedDisplayId) {
        if (this.mCurrentFocus != null && this.mTransitionController.shouldKeepFocus(this.mCurrentFocus) && this.mFocusedApp != null && this.mCurrentFocus.isDescendantOf(this.mFocusedApp) && this.mCurrentFocus.isVisible() && this.mCurrentFocus.isFocusable()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, -2192125645150932161L, 0, null, null);
            }
            return false;
        }
        com.android.server.wm.WindowState newFocus = findFocusedWindowIfNeeded(topFocusedDisplayId);
        android.util.Slog.d(TAG, "NFW_findFocusedWindowIfNeeded:" + newFocus + " mCurrentFocus:" + this.mCurrentFocus);
        if (this.mCurrentFocus != newFocus) {
            this.mDisplayContentExt.beginHookUpdateFocusedWindowLocked(this, newFocus, this.mWmService);
            boolean imWindowChanged = false;
            com.android.server.wm.WindowState imWindow = this.mInputMethodWindow;
            if (imWindow != null) {
                com.android.server.wm.WindowState prevTarget = this.mImeLayeringTarget;
                com.android.server.wm.WindowState newTarget = computeImeTarget(true);
                imWindowChanged = prevTarget != newTarget;
                if (mode != 1 && mode != 3) {
                    assignWindowLayers(false);
                }
                if (imWindowChanged) {
                    this.mWmService.mWindowsChanged = true;
                    setLayoutNeeded();
                    newFocus = findFocusedWindowIfNeeded(topFocusedDisplayId);
                }
            }
            if (!com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT.isLogToLogcat()) {
                android.util.Slog.v(TAG, "Changing focus from " + this.mCurrentFocus + " to " + newFocus + ",diplayid=" + getDisplayId());
            } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mCurrentFocus);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(newFocus);
                long protoLogParam2 = getDisplayId();
                java.lang.String protoLogParam3 = java.lang.String.valueOf(android.os.Debug.getCallers(4));
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 3101160328044493048L, 16, null, protoLogParam0, protoLogParam1, java.lang.Long.valueOf(protoLogParam2), protoLogParam3);
            }
            com.android.server.wm.WindowState oldFocus = this.mCurrentFocus;
            this.mCurrentFocus = newFocus;
            if (newFocus != null) {
                this.mWinAddedSinceNullFocus.clear();
                this.mWinRemovedSinceNullFocus.clear();
                if (newFocus.canReceiveKeys()) {
                    newFocus.mToken.paused = false;
                }
            }
            this.mDisplayContentExt.disableStatusBarForSystem(this.mWmService, newFocus);
            getDisplayPolicy().focusChangedLw(oldFocus, newFocus);
            this.mAtmService.mBackNavigationController.onFocusChanged(newFocus);
            if (imWindowChanged && oldFocus != this.mInputMethodWindow) {
                if (mode == 2) {
                    performLayout(true, updateInputWindows);
                } else if (mode == 3) {
                    assignWindowLayers(false);
                }
            }
            if (mode != 1) {
                getInputMonitor().setInputFocusLw(newFocus, updateInputWindows);
            }
            adjustForImeIfNeeded();
            scheduleToastWindowsTimeoutIfNeededLocked(oldFocus, newFocus);
            if (mode == 2) {
                this.pendingLayoutChanges |= 8;
            }
            if (this.mWmService.mAccessibilityController.hasCallbacks()) {
                this.mWmService.mH.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda4
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.updateAccessibilityOnWindowFocusChanged((com.android.server.wm.AccessibilityController) obj);
                    }
                }, this.mWmService.mAccessibilityController));
            }
            this.mDisplayContentExt.endHookUpdateFocusedWindowLocked(this, this.mWmService, topFocusedDisplayId);
            return true;
        }
        this.mDisplayContentExt.hookForComplexScene(newFocus, updateInputWindows);
        return false;
    }

    void updateAccessibilityOnWindowFocusChanged(com.android.server.wm.AccessibilityController accessibilityController) {
        accessibilityController.onWindowFocusChangedNot(getDisplayId());
    }

    boolean setFocusedApp(com.android.server.wm.ActivityRecord newFocus) {
        if (newFocus != null) {
            com.android.server.wm.DisplayContent appDisplay = newFocus.getDisplayContent();
            if (appDisplay != this) {
                if (getWrapper().getNonStaticExtImpl().isPuttDisplay()) {
                    java.lang.StringBuilder errorMsg = new java.lang.StringBuilder(newFocus + " is not on " + getName() + " but " + (appDisplay != null ? appDisplay.getName() : "none"));
                    for (com.android.server.wm.WindowContainer errorParent = newFocus.getParent(); errorParent != null; errorParent = errorParent.getParent()) {
                        errorMsg.append(" p: " + errorParent.toString());
                    }
                    android.util.Slog.e(TAG, errorMsg.toString());
                    return false;
                }
                throw new java.lang.IllegalStateException(newFocus + " is not on " + getName() + " but " + (appDisplay != null ? appDisplay.getName() : "none"));
            }
            onLastFocusedTaskDisplayAreaChanged(newFocus.getDisplayArea());
        }
        if (this.mFocusedApp == newFocus) {
            return false;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(newFocus);
            long protoLogParam1 = getDisplayId();
            java.lang.String protoLogParam2 = java.lang.String.valueOf(android.os.Debug.getCallers(4));
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 7634130879993688940L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), protoLogParam2);
        }
        com.android.server.wm.Task oldTask = this.mFocusedApp != null ? this.mFocusedApp.getTask() : null;
        com.android.server.wm.Task newTask = newFocus != null ? newFocus.getTask() : null;
        this.mDisplayContentExt.savedSurface(this.mWmService);
        this.mFocusedApp = newFocus;
        this.mWmService.getWrapper().getExtImpl().cpuFrequencyBoostIfNeed(newFocus);
        if (oldTask != newTask) {
            if (oldTask != null) {
                oldTask.onAppFocusChanged(false);
            }
            if (newTask != null) {
                newTask.onAppFocusChanged(true);
            }
        }
        this.mDisplayContentExt.onDisplayFocusedAppChanged(this, newFocus);
        getInputMonitor().setFocusedAppLw(newFocus);
        return true;
    }

    void onRunningActivityChanged() {
        this.mDwpcHelper.onRunningActivityChanged();
    }

    void onLastFocusedTaskDisplayAreaChanged(com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        this.mOrientationRequestingTaskDisplayArea = taskDisplayArea;
    }

    com.android.server.wm.TaskDisplayArea getOrientationRequestingTaskDisplayArea() {
        return this.mOrientationRequestingTaskDisplayArea;
    }

    void assignWindowLayers(boolean setLayoutNeeded) {
        android.os.Trace.traceBegin(32L, "assignWindowLayers");
        assignChildLayers(getSyncTransaction());
        if (setLayoutNeeded) {
            setLayoutNeeded();
        }
        scheduleAnimation();
        android.os.Trace.traceEnd(32L);
    }

    boolean destroyLeakedSurfaces() {
        this.mTmpWindow = null;
        final android.view.SurfaceControl.Transaction t = this.mWmService.mTransactionFactory.get();
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda35
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$destroyLeakedSurfaces$24(t, (com.android.server.wm.WindowState) obj);
            }
        }, false);
        t.apply();
        return this.mTmpWindow != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$destroyLeakedSurfaces$24(android.view.SurfaceControl.Transaction t, com.android.server.wm.WindowState w) {
        com.android.server.wm.WindowStateAnimator wsa = w.mWinAnimator;
        if (wsa.mSurfaceController == null) {
            return;
        }
        if (!this.mWmService.mSessions.contains(wsa.mSession)) {
            android.util.Slog.w(TAG, "LEAKED SURFACE (session doesn't exist): " + w + " surface=" + wsa.mSurfaceController + " token=" + w.mToken + " pid=" + w.mSession.mPid + " uid=" + w.mSession.mUid);
            wsa.destroySurface(t);
            this.mWmService.mForceRemoves.add(w);
            this.mTmpWindow = w;
            return;
        }
        if (w.mActivityRecord != null && !w.mActivityRecord.isClientVisible()) {
            android.util.Slog.w(TAG, "LEAKED SURFACE (app token hidden): " + w + " surface=" + wsa.mSurfaceController + " token=" + w.mActivityRecord);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(w);
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, -4130402450005935184L, 0, null, protoLogParam0);
            }
            wsa.destroySurface(t);
            this.mTmpWindow = w;
        }
    }

    boolean hasAlertWindowSurfaces() {
        for (int i = this.mWmService.mSessions.size() - 1; i >= 0; i--) {
            if (this.mWmService.mSessions.valueAt(i).hasAlertWindowSurfaces(this)) {
                return true;
            }
        }
        return false;
    }

    void setInputMethodWindowLocked(com.android.server.wm.WindowState win) {
        this.mInputMethodWindow = win;
        this.mInsetsStateController.getImeSourceProvider().setWindowContainer(win, this.mDisplayPolicy.getImeSourceFrameProvider(), null);
        computeImeTarget(true);
        updateImeControlTarget();
    }

    com.android.server.wm.WindowState computeImeTarget(boolean updateImeTarget) {
        if (this.mInputMethodWindow == null) {
            if (updateImeTarget) {
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                    android.util.Slog.w(TAG, "Moving IM target from " + this.mImeLayeringTarget + " to null since mInputMethodWindow is null");
                }
                setImeLayeringTargetInner(null);
            }
            return null;
        }
        com.android.server.wm.WindowState curTarget = this.mImeLayeringTarget;
        if (!canUpdateImeTarget()) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                android.util.Slog.w(TAG, "Defer updating IME target");
            }
            this.mUpdateImeRequestedWhileDeferred = true;
            return curTarget;
        }
        this.mUpdateImeTarget = updateImeTarget;
        com.android.server.wm.WindowState target = getWindow(this.mComputeImeTargetPredicate);
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD && updateImeTarget) {
            android.util.Slog.v(TAG, "Proposed new IME target: " + target + " for display: " + getDisplayId());
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
            android.util.Slog.v(TAG, "Desired input method target=" + target + " updateImeTarget=" + updateImeTarget);
        }
        if (target == null) {
            if (updateImeTarget) {
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                    android.util.Slog.w(TAG, "Moving IM target from " + curTarget + " to null." + (com.android.server.wm.WindowManagerDebugConfig.SHOW_STACK_CRAWLS ? " Callers=" + android.os.Debug.getCallers(4) : ""));
                }
                setImeLayeringTargetInner(null);
            }
            return null;
        }
        if (updateImeTarget) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                android.util.Slog.w(TAG, "Moving IM target from " + curTarget + " to " + target + (com.android.server.wm.WindowManagerDebugConfig.SHOW_STACK_CRAWLS ? " Callers=" + android.os.Debug.getCallers(4) : ""));
            }
            setImeLayeringTargetInner(target);
        }
        return target;
    }

    void computeImeTargetIfNeeded(com.android.server.wm.ActivityRecord candidate) {
        if (this.mImeLayeringTarget != null && this.mImeLayeringTarget.mActivityRecord == candidate) {
            computeImeTarget(true);
        }
    }

    private boolean isImeControlledByApp() {
        return this.mImeInputTarget != null && this.mImeInputTarget.shouldControlIme();
    }

    boolean shouldImeAttachedToApp() {
        if (this.mImeWindowsContainer.isOrganized()) {
            return false;
        }
        boolean allowAttachToApp = this.mMagnificationSpec == null;
        return allowAttachToApp && isImeControlledByApp() && this.mImeLayeringTarget != null && this.mImeLayeringTarget.mActivityRecord != null && this.mImeLayeringTarget.getWindowingMode() == 1 && this.mImeLayeringTarget.getBounds().equals(this.mImeWindowsContainer.getBounds()) && this.mDisplayContentExt.shouldImeAttachedToApp(this.mImeLayeringTarget) && this.mImeLayeringTarget.matchesDisplayAreaBounds();
    }

    boolean isImeAttachedToApp() {
        return shouldImeAttachedToApp() && this.mInputMethodSurfaceParent != null && this.mInputMethodSurfaceParent.isSameSurface(this.mImeLayeringTarget.mActivityRecord.getSurfaceControl());
    }

    com.android.server.wm.InsetsControlTarget getImeHostOrFallback(com.android.server.wm.WindowState target) {
        if (target != null && target.getDisplayContent().getImePolicy() == 0) {
            return target;
        }
        if (android.view.inputmethod.Flags.refactorInsetsController()) {
            com.android.server.wm.DisplayContent defaultDc = this.mWmService.getDefaultDisplayContentLocked();
            return defaultDc.mRemoteInsetsControlTarget;
        }
        return getImeFallback();
    }

    com.android.server.wm.InsetsControlTarget getImeFallback() {
        com.android.server.wm.DisplayContent defaultDc = this.mWmService.getDefaultDisplayContentLocked();
        com.android.server.wm.WindowState statusBar = defaultDc.getDisplayPolicy().getStatusBar();
        return statusBar != null ? statusBar : defaultDc.mRemoteInsetsControlTarget;
    }

    com.android.server.wm.InsetsControlTarget getImeTarget(int type) {
        switch (type) {
            case 0:
                return this.mImeLayeringTarget;
            case 1:
            default:
                return null;
            case 2:
                return this.mImeControlTarget;
        }
    }

    com.android.server.wm.InputTarget getImeInputTarget() {
        return this.mImeInputTarget;
    }

    int getImePolicy() {
        if (((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageDisplay(getDisplayId()) || getWrapper().getNonStaticExtImpl().isPuttDisplay() || this.mDisplayContentExt.isSupportedIMEOnSecondDisplay(this.mDisplayContent)) {
            return 0;
        }
        if (!isTrusted()) {
            return 1;
        }
        int imePolicy = this.mWmService.mDisplayWindowSettings.getImePolicyLocked(this);
        if (imePolicy == 1 && forceDesktopMode()) {
            return 0;
        }
        return imePolicy;
    }

    boolean forceDesktopMode() {
        return (!this.mWmService.mForceDesktopModeOnExternalDisplays || this.isDefaultDisplay || isPrivate()) ? false : true;
    }

    void onShowImeRequested() {
        if (this.mInputMethodWindow == null) {
            return;
        }
        this.mDisplayContentExt.onShowImeRequested();
        if (this.mFixedRotationLaunchingApp != null) {
            if (this.mImeLayeringTarget != null && this.mImeLayeringTarget.mAttrs.type != 3 && this.mImeLayeringTarget.mSession.mPid != this.mFixedRotationLaunchingApp.getPid()) {
                return;
            }
            this.mInputMethodWindow.mToken.linkFixedRotationTransform(this.mFixedRotationLaunchingApp);
            if (this.mAsyncRotationController != null) {
                this.mAsyncRotationController.hideImeImmediately();
            }
        }
    }

    void setImeLayeringTarget(com.android.server.wm.WindowState target) {
        this.mImeLayeringTarget = target;
    }

    private void setImeLayeringTargetInner(com.android.server.wm.WindowState target) {
        com.android.server.wm.RootDisplayArea targetRoot;
        if (target == this.mImeLayeringTarget && this.mLastImeInputTarget == this.mImeInputTarget) {
            return;
        }
        this.mLastImeInputTarget = this.mImeInputTarget;
        if (this.mImeLayeringTarget != null && this.mImeLayeringTarget == this.mImeInputTarget) {
            boolean nonAppImeTargetAnimatingExit = this.mImeLayeringTarget.mAnimatingExit && this.mImeLayeringTarget.mAttrs.type != 1 && this.mImeLayeringTarget.isSelfAnimating(0, 16);
            if (this.mImeLayeringTarget.inTransitionSelfOrParent() || nonAppImeTargetAnimatingExit) {
                showImeScreenshot();
            }
        }
        this.mDisplayContentExt.removeImeSurfaceImmediately(this, target);
        this.mDisplayContentExt.hideInputMethodMenuIfNeed(this.mImeLayeringTarget, target);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(target);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, 4464269036743635127L, 0, null, protoLogParam0);
        }
        boolean shouldUpdateImeParent = target != this.mImeLayeringTarget;
        this.mImeLayeringTarget = target;
        if (target != null && !this.mImeWindowsContainer.isOrganized() && (targetRoot = target.getRootDisplayArea()) != null && targetRoot != this.mImeWindowsContainer.getRootDisplayArea() && targetRoot.placeImeContainer(this.mImeWindowsContainer)) {
            shouldUpdateImeParent = true;
            if (this.mInputMethodWindow != null) {
                this.mInputMethodWindow.hide(false, false);
            }
        }
        assignWindowLayers(true);
        this.mInsetsStateController.updateAboveInsetsState(this.mInsetsStateController.getRawInsetsState().isSourceOrDefaultVisible(android.view.InsetsSource.ID_IME, android.view.WindowInsets.Type.ime()));
        updateImeControlTarget(shouldUpdateImeParent);
    }

    void setImeInputTarget(com.android.server.wm.InputTarget target) {
        final com.android.server.wm.WindowState targetWin;
        if (this.mImeTargetTokenListenerPair != null) {
            com.android.server.wm.WindowToken oldToken = this.mTokenMap.get(this.mImeTargetTokenListenerPair.first);
            if (oldToken != null) {
                oldToken.unregisterWindowContainerListener((com.android.server.wm.WindowContainerListener) this.mImeTargetTokenListenerPair.second);
            }
            this.mImeTargetTokenListenerPair = null;
        }
        this.mImeInputTarget = target;
        if (target != null && (targetWin = target.getWindowState()) != null) {
            this.mImeTargetTokenListenerPair = new android.util.Pair<>(targetWin.mToken.token, new com.android.server.wm.WindowContainerListener() { // from class: com.android.server.wm.DisplayContent.3
                @Override // com.android.server.wm.WindowContainerListener
                public void onVisibleRequestedChanged(boolean isVisibleRequested) {
                    com.android.server.wm.DisplayContent.this.mWmService.dispatchImeInputTargetVisibilityChanged(targetWin.mClient.asBinder(), isVisibleRequested, targetWin.mActivityRecord != null && targetWin.mActivityRecord.finishing);
                }
            });
            targetWin.mToken.registerWindowContainerListener((com.android.server.wm.WindowContainerListener) this.mImeTargetTokenListenerPair.second);
            this.mWmService.dispatchImeInputTargetVisibilityChanged(targetWin.mClient.asBinder(), targetWin.isVisible(), false);
        }
        if (refreshImeSecureFlag(getPendingTransaction())) {
            this.mWmService.requestTraversal();
        }
    }

    boolean refreshImeSecureFlag(android.view.SurfaceControl.Transaction t) {
        boolean canScreenshot = this.mImeInputTarget == null || this.mImeInputTarget.canScreenshotIme();
        return this.mImeWindowsContainer.setCanScreenshot(t, canScreenshot);
    }

    void setImeControlTarget(com.android.server.wm.InsetsControlTarget target) {
        this.mImeControlTarget = target;
    }

    static final class ImeScreenshot {
        private android.view.SurfaceControl mImeSurface;
        private android.graphics.Point mImeSurfacePosition;
        private com.android.server.wm.WindowState mImeTarget;
        private android.view.SurfaceControl.Builder mSurfaceBuilder;

        ImeScreenshot(android.view.SurfaceControl.Builder surfaceBuilder, com.android.server.wm.WindowState imeTarget) {
            this.mSurfaceBuilder = surfaceBuilder;
            this.mImeTarget = imeTarget;
        }

        com.android.server.wm.WindowState getImeTarget() {
            return this.mImeTarget;
        }

        android.view.SurfaceControl getImeScreenshotSurface() {
            return this.mImeSurface;
        }

        private android.view.SurfaceControl createImeSurface(android.window.ScreenCapture.ScreenshotHardwareBuffer b, android.view.SurfaceControl.Transaction t) {
            android.view.SurfaceControl imeParent;
            android.hardware.HardwareBuffer buffer = b.getHardwareBuffer();
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mImeTarget);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(buffer.getWidth());
                java.lang.String protoLogParam2 = java.lang.String.valueOf(buffer.getHeight());
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, 4835192778854186097L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2);
            }
            com.android.server.wm.WindowState imeWindow = this.mImeTarget.getDisplayContent().mInputMethodWindow;
            com.android.server.wm.ActivityRecord activity = this.mImeTarget.mActivityRecord;
            if (this.mImeTarget.mAttrs.type == 1) {
                imeParent = activity.getSurfaceControl();
            } else {
                imeParent = this.mImeTarget.getSurfaceControl();
            }
            android.view.SurfaceControl imeSurface = this.mSurfaceBuilder.setName("IME-snapshot-surface").setBLASTLayer().setFormat(buffer.getFormat()).setParent(imeParent).setCallsite("DisplayContent.attachAndShowImeScreenshotOnTarget").build();
            com.android.server.wm.InputMonitor.setTrustedOverlayInputInfo(imeSurface, t, imeWindow.getDisplayId(), "IME-snapshot-surface");
            t.setBuffer(imeSurface, buffer);
            t.setColorSpace(activity.mSurfaceControl, android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB));
            t.setLayer(imeSurface, 1);
            android.graphics.Point surfacePosition = new android.graphics.Point(imeWindow.getFrame().left, imeWindow.getFrame().top);
            if (imeParent != activity.getSurfaceControl()) {
                surfacePosition.offset(-this.mImeTarget.getFrame().left, -this.mImeTarget.getFrame().top);
                surfacePosition.offset(this.mImeTarget.mAttrs.surfaceInsets.left, this.mImeTarget.mAttrs.surfaceInsets.top);
                t.setPosition(imeSurface, surfacePosition.x, surfacePosition.y);
            } else {
                t.setPosition(imeSurface, surfacePosition.x, surfacePosition.y);
            }
            this.mImeSurfacePosition = surfacePosition;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[2]) {
                long protoLogParam02 = surfacePosition.x;
                long protoLogParam12 = surfacePosition.y;
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, 2408509162360028352L, 5, null, java.lang.Long.valueOf(protoLogParam02), java.lang.Long.valueOf(protoLogParam12));
            }
            return imeSurface;
        }

        private void removeImeSurface(android.view.SurfaceControl.Transaction t) {
            if (this.mImeSurface != null) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[2]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(android.os.Debug.getCallers(6));
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, 2005731931732324688L, 0, null, protoLogParam0);
                }
                t.remove(this.mImeSurface);
                this.mImeSurface = null;
            }
            if (android.view.inputmethod.ImeTracker.DEBUG_IME_VISIBILITY) {
                android.util.EventLog.writeEvent(com.android.server.wm.EventLogTags.IMF_REMOVE_IME_SCREENSHOT, this.mImeTarget.toString());
            }
        }

        void attachAndShow(android.view.SurfaceControl.Transaction t, boolean anyTargetTask) {
            android.window.ScreenCapture.ScreenshotHardwareBuffer imeBuffer;
            com.android.server.wm.DisplayContent dc = this.mImeTarget.getDisplayContent();
            com.android.server.wm.Task task = this.mImeTarget.getTask();
            boolean isValidSnapshot = false;
            boolean renewImeSurface = (this.mImeSurface != null && this.mImeSurface.getWidth() == dc.mInputMethodWindow.getFrame().width() && this.mImeSurface.getHeight() == dc.mInputMethodWindow.getFrame().height()) ? false : true;
            if (task != null && (anyTargetTask || !task.isActivityTypeHomeOrRecents())) {
                if (renewImeSurface) {
                    imeBuffer = dc.mWmService.mTaskSnapshotController.snapshotImeFromAttachedTask(task);
                } else {
                    imeBuffer = null;
                }
                if (imeBuffer != null) {
                    removeImeSurface(t);
                    this.mImeSurface = createImeSurface(imeBuffer, t);
                }
            }
            if (this.mImeSurface != null && this.mImeSurface.isValid()) {
                isValidSnapshot = true;
            }
            if (isValidSnapshot && dc.getInsetsStateController().getImeSourceProvider().isImeShowing()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[2]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mImeTarget);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(android.os.Debug.getCallers(6));
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, -6495118720675662641L, 0, null, protoLogParam0, protoLogParam1);
                }
                t.show(this.mImeSurface);
                if (android.view.inputmethod.ImeTracker.DEBUG_IME_VISIBILITY) {
                    android.util.EventLog.writeEvent(com.android.server.wm.EventLogTags.IMF_SHOW_IME_SCREENSHOT, this.mImeTarget.toString(), java.lang.Integer.valueOf(dc.mInputMethodWindow.mTransitFlags), this.mImeSurfacePosition.toString());
                    return;
                }
                return;
            }
            if (!isValidSnapshot) {
                removeImeSurface(t);
            }
        }

        void detach(android.view.SurfaceControl.Transaction t) {
            removeImeSurface(t);
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(64);
            sb.append("ImeScreenshot{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(" imeTarget=" + this.mImeTarget);
            sb.append(" surface=" + this.mImeSurface);
            sb.append('}');
            return sb.toString();
        }
    }

    private void attachImeScreenshotOnTargetIfNeeded() {
        if (shouldImeAttachedToApp() && this.mWmService.mPolicy.isScreenOn() && this.mInputMethodWindow != null && this.mInputMethodWindow.isVisible() && !this.mInputMethodWindow.isSecureLocked()) {
            attachImeScreenshotOnTarget(this.mImeLayeringTarget);
        }
    }

    private void attachImeScreenshotOnTarget(com.android.server.wm.WindowState imeTarget) {
        attachImeScreenshotOnTarget(imeTarget, false);
    }

    private void attachImeScreenshotOnTarget(com.android.server.wm.WindowState imeTarget, boolean hideImeWindow) {
        if (this.mDisplayContentExt.shouldNotShowImeScreenshot(imeTarget)) {
            return;
        }
        android.view.SurfaceControl.Transaction t = getPendingTransaction();
        removeImeSurfaceImmediately();
        this.mImeScreenshot = new com.android.server.wm.DisplayContent.ImeScreenshot(this.mWmService.mSurfaceControlFactory.apply(null), imeTarget);
        this.mImeScreenshot.attachAndShow(t, hideImeWindow);
        this.mDisplayContentExt.setLastImeLayeringTarget(this.mImeLayeringTarget);
        if (this.mInputMethodWindow != null && hideImeWindow) {
            this.mInputMethodWindow.hide(false, false);
        }
    }

    void showImeScreenshot() {
        attachImeScreenshotOnTargetIfNeeded();
    }

    void showImeScreenshot(com.android.server.wm.WindowState imeTarget) {
        attachImeScreenshotOnTarget(imeTarget, true);
    }

    void removeImeSurfaceByTarget(com.android.server.wm.WindowContainer win) {
        if (this.mImeScreenshot == null || win == null) {
            return;
        }
        if (win.asWindowState() != null && win.asWindowState().mAttrs.type == 3) {
            return;
        }
        final com.android.server.wm.WindowState screenshotTarget = this.mImeScreenshot.getImeTarget();
        boolean winIsOrContainsScreenshotTarget = win == screenshotTarget || win.getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.DisplayContent.lambda$removeImeSurfaceByTarget$25(screenshotTarget, obj);
            }
        }) != null;
        if (winIsOrContainsScreenshotTarget) {
            removeImeSurfaceImmediately();
        }
    }

    static /* synthetic */ boolean lambda$removeImeSurfaceByTarget$25(com.android.server.wm.WindowState screenshotTarget, java.lang.Object w) {
        return w == screenshotTarget;
    }

    void removeImeSurfaceImmediately() {
        if (this.mImeScreenshot != null) {
            this.mImeScreenshot.detach(getSyncTransaction());
            this.mImeScreenshot = null;
            this.mDisplayContentExt.setLastImeLayeringTarget(null);
        }
    }

    void updateImeInputAndControlTarget(com.android.server.wm.InputTarget target) {
        if (this.mImeInputTarget != target) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(target);
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, -4354595179162289537L, 0, null, protoLogParam0);
            }
            if (target == this.mImeInputTarget) {
                if (this.mDisplayContentExt.updateImeTarget(this.mImeInputTarget == null ? null : this.mImeInputTarget.getWindowState())) {
                    return;
                }
            }
            setImeInputTarget(target);
            this.mInsetsStateController.updateAboveInsetsState(this.mInsetsStateController.getRawInsetsState().isSourceOrDefaultVisible(android.view.InsetsSource.ID_IME, android.view.WindowInsets.Type.ime()));
            boolean forceUpdateImeParent = (this.mImeControlTarget != this.mRemoteInsetsControlTarget || this.mInputMethodSurfaceParent == null || this.mInputMethodSurfaceParent.isSameSurface(this.mImeWindowsContainer.getParent().mSurfaceControl)) ? false : true;
            updateImeControlTarget(forceUpdateImeParent);
            if (android.view.inputmethod.Flags.refactorInsetsController()) {
                this.mInsetsStateController.getImeSourceProvider().onInputTargetChanged(target);
                return;
            }
            return;
        }
        if (this.mImeInputTarget != null && this.mImeInputTarget.getWindowState() != this.mImeLayeringTarget) {
            this.mInsetsStateController.updateAboveInsetsState(this.mInsetsStateController.getRawInsetsState().isSourceOrDefaultVisible(android.view.InsetsSource.ID_IME, android.view.WindowInsets.Type.ime()));
        }
    }

    boolean onImeInsetsClientVisibilityUpdate() {
        final boolean[] changed = new boolean[1];
        com.android.server.wm.ActivityRecord inputTargetActivity = this.mImeInputTarget != null ? this.mImeInputTarget.getActivityRecord() : null;
        boolean targetChanged = this.mImeInputTarget != this.mLastImeInputTarget;
        if (targetChanged || (inputTargetActivity != null && inputTargetActivity.isVisibleRequested() && inputTargetActivity.mImeInsetsFrozenUntilStartInput)) {
            forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda38
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.DisplayContent.lambda$onImeInsetsClientVisibilityUpdate$26(changed, (com.android.server.wm.ActivityRecord) obj);
                }
            });
        }
        return changed[0];
    }

    static /* synthetic */ void lambda$onImeInsetsClientVisibilityUpdate$26(boolean[] changed, com.android.server.wm.ActivityRecord r) {
        if (r.mImeInsetsFrozenUntilStartInput && r.isVisibleRequested()) {
            r.mImeInsetsFrozenUntilStartInput = false;
            changed[0] = true;
        }
    }

    void updateImeControlTarget() {
        updateImeControlTarget(false);
    }

    void updateImeControlTarget(boolean forceUpdateImeParent) {
        com.android.server.wm.InsetsControlTarget prevImeControlTarget = this.mImeControlTarget;
        this.mImeControlTarget = computeImeControlTarget();
        this.mInsetsStateController.onImeControlTargetChanged(this.mImeControlTarget);
        boolean imeControlChanged = prevImeControlTarget != this.mImeControlTarget;
        if (imeControlChanged || forceUpdateImeParent) {
            updateImeParent();
        }
        com.android.server.wm.WindowState win = com.android.server.wm.InsetsControlTarget.asWindowOrNull(this.mImeControlTarget);
        final android.os.IBinder token = win != null ? win.mClient.asBinder() : null;
        this.mWmService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.inputmethod.InputMethodManagerInternal.get().reportImeControl(token);
            }
        });
    }

    void updateImeParent() {
        android.view.SurfaceControl lastRelativeLayer;
        if (this.mImeWindowsContainer.isOrganized()) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                android.util.Slog.i(TAG, "ImeContainer is organized. Skip updateImeParent.");
            }
            this.mInputMethodSurfaceParent = null;
            return;
        }
        android.view.SurfaceControl newParent = computeImeParent();
        if (newParent != null && newParent != this.mInputMethodSurfaceParent) {
            this.mInputMethodSurfaceParent = newParent;
            getSyncTransaction().reparent(this.mImeWindowsContainer.mSurfaceControl, newParent);
            if (android.view.inputmethod.ImeTracker.DEBUG_IME_VISIBILITY) {
                android.util.EventLog.writeEvent(com.android.server.wm.EventLogTags.IMF_UPDATE_IME_PARENT, newParent.toString());
            }
            assignRelativeLayerForIme(getSyncTransaction(), true);
            scheduleAnimation();
            this.mWmService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateImeParent$28();
                }
            });
            return;
        }
        if (this.mImeControlTarget != null && this.mImeControlTarget == this.mImeLayeringTarget && (lastRelativeLayer = this.mImeWindowsContainer.getLastRelativeLayer()) != this.mImeLayeringTarget.mSurfaceControl) {
            assignRelativeLayerForIme(getSyncTransaction(), false);
            if (lastRelativeLayer != this.mImeWindowsContainer.getLastRelativeLayer()) {
                scheduleAnimation();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateImeParent$28() {
        com.android.server.inputmethod.InputMethodManagerInternal.get().onImeParentChanged(getDisplayId());
    }

    com.android.server.wm.InsetsControlTarget computeImeControlTarget() {
        if (this.mImeInputTarget == null) {
            if (android.view.inputmethod.Flags.refactorInsetsController() && this.isDefaultDisplay && this.mRemoteInsetsControlTarget != null) {
                return this.mRemoteInsetsControlTarget;
            }
            return null;
        }
        com.android.server.wm.WindowState imeInputTarget = this.mImeInputTarget.getWindowState();
        if ((!isImeControlledByApp() && this.mRemoteInsetsControlTarget != null) || getImeHostOrFallback(imeInputTarget) == this.mRemoteInsetsControlTarget) {
            return this.mRemoteInsetsControlTarget;
        }
        return imeInputTarget;
    }

    /* JADX WARN: Multi-variable type inference failed */
    android.view.SurfaceControl computeImeParent() {
        if (!com.android.server.wm.ImeTargetVisibilityPolicy.canComputeImeParent(this.mImeLayeringTarget, this.mImeInputTarget) && !this.mDisplayContentExt.HasZoomWindowAboveImeInputTarget(this.mImeLayeringTarget, this.mImeInputTarget, this.mImeWindowsContainer.getParent())) {
            return null;
        }
        if (shouldImeAttachedToApp()) {
            return this.mImeLayeringTarget.mActivityRecord.getSurfaceControl();
        }
        if (this.mImeWindowsContainer.getParent() != null) {
            return this.mImeWindowsContainer.getParent().getSurfaceControl();
        }
        return null;
    }

    void setLayoutNeeded() {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
            android.util.Slog.w(TAG, "setLayoutNeeded: callers=" + android.os.Debug.getCallers(3));
        }
        this.mLayoutNeeded = true;
    }

    private void clearLayoutNeeded() {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
            android.util.Slog.w(TAG, "clearLayoutNeeded: callers=" + android.os.Debug.getCallers(3));
        }
        this.mLayoutNeeded = false;
    }

    boolean isLayoutNeeded() {
        return this.mLayoutNeeded;
    }

    void dumpTokens(java.io.PrintWriter pw, boolean dumpAll) {
        if (this.mTokenMap.isEmpty()) {
            return;
        }
        pw.println("  Display #" + this.mDisplayId);
        pw.println("    mInTouchMode=" + this.mInTouchMode);
        for (com.android.server.wm.WindowToken token : this.mTokenMap.values()) {
            pw.print("  ");
            pw.print(token);
            if (dumpAll) {
                pw.println(':');
                token.dump(pw, "    ", dumpAll);
            } else {
                pw.println();
            }
        }
        if (!this.mOpeningApps.isEmpty() || !this.mClosingApps.isEmpty() || !this.mChangingContainers.isEmpty()) {
            pw.println();
            if (this.mOpeningApps.size() > 0) {
                pw.print("  mOpeningApps=");
                pw.println(this.mOpeningApps);
            }
            if (this.mClosingApps.size() > 0) {
                pw.print("  mClosingApps=");
                pw.println(this.mClosingApps);
            }
            if (this.mChangingContainers.size() > 0) {
                pw.print("  mChangingApps=");
                pw.println(this.mChangingContainers);
            }
        }
        this.mUnknownAppVisibilityController.dump(pw, "  ");
    }

    void dumpWindowAnimators(final java.io.PrintWriter pw, final java.lang.String subPrefix) {
        final int[] index = new int[1];
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.DisplayContent.lambda$dumpWindowAnimators$29(pw, subPrefix, index, (com.android.server.wm.WindowState) obj);
            }
        }, false);
    }

    static /* synthetic */ void lambda$dumpWindowAnimators$29(java.io.PrintWriter pw, java.lang.String subPrefix, int[] index, com.android.server.wm.WindowState w) {
        com.android.server.wm.WindowStateAnimator wAnim = w.mWinAnimator;
        pw.println(subPrefix + "Window #" + index[0] + ": " + wAnim);
        index[0] = index[0] + 1;
    }

    void startKeyguardExitOnNonAppWindows(final boolean onWallpaper, final boolean goingToShade, final boolean subtle) {
        final com.android.server.policy.WindowManagerPolicy policy = this.mWmService.mPolicy;
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$startKeyguardExitOnNonAppWindows$30(onWallpaper, goingToShade, subtle, policy, (com.android.server.wm.WindowState) obj);
            }
        }, true);
        for (int i = this.mShellRoots.size() - 1; i >= 0; i--) {
            this.mShellRoots.valueAt(i).startAnimation(policy.createHiddenByKeyguardExit(onWallpaper, goingToShade, subtle));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startKeyguardExitOnNonAppWindows$30(boolean onWallpaper, boolean goingToShade, boolean subtle, com.android.server.policy.WindowManagerPolicy policy, com.android.server.wm.WindowState w) {
        if (w.mActivityRecord != null || !w.canBeHiddenByKeyguard() || !w.wouldBeVisibleIfPolicyIgnored() || w.isVisible() || this.mDisplayContentExt.startKeyguardExitOnNonAppWindows(w, onWallpaper, goingToShade, subtle)) {
            return;
        }
        w.startAnimation(policy.createHiddenByKeyguardExit(onWallpaper, goingToShade, subtle));
    }

    boolean shouldWaitForSystemDecorWindowsOnBoot() {
        if ((!this.isDefaultDisplay && !supportsSystemDecorations()) || this.mDisplayContentExt.shouldNotWaitForDisplayOnBoot(this)) {
            return false;
        }
        final android.util.SparseBooleanArray drawnWindowTypes = new android.util.SparseBooleanArray();
        drawnWindowTypes.put(2040, true);
        com.android.server.wm.WindowState visibleNotDrawnWindow = getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda36
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$shouldWaitForSystemDecorWindowsOnBoot$31(drawnWindowTypes, (com.android.server.wm.WindowState) obj);
            }
        });
        if (visibleNotDrawnWindow != null) {
            this.mDisplayContentExt.debugForBootTime(TAG, visibleNotDrawnWindow);
            return true;
        }
        boolean wallpaperEnabled = this.mWmService.mContext.getResources().getBoolean(android.R.bool.config_enableSafetyCenter) && this.mWmService.mContext.getResources().getBoolean(android.R.bool.config_closeDialogWhenTouchOutside);
        boolean haveBootMsg = drawnWindowTypes.get(2021);
        boolean haveApp = drawnWindowTypes.get(1);
        boolean haveWallpaper = drawnWindowTypes.get(2013);
        boolean haveKeyguard = drawnWindowTypes.get(2040);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SCREEN_ON_enabled[2]) {
            boolean protoLogParam0 = this.mWmService.mSystemBooted;
            boolean protoLogParam1 = this.mWmService.mShowingBootMessages;
            boolean protoLogParam5 = wallpaperEnabled;
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SCREEN_ON, 5683557566110711213L, 16383, null, java.lang.Boolean.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(haveBootMsg), java.lang.Boolean.valueOf(haveApp), java.lang.Boolean.valueOf(haveWallpaper), java.lang.Boolean.valueOf(protoLogParam5), java.lang.Boolean.valueOf(haveKeyguard));
        }
        if (this.mWmService.mSystemBooted || haveBootMsg) {
            return this.mWmService.mSystemBooted && (!(haveApp || haveKeyguard) || (wallpaperEnabled && !haveWallpaper));
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public /* synthetic */ boolean lambda$shouldWaitForSystemDecorWindowsOnBoot$31(android.util.SparseBooleanArray drawnWindowTypes, com.android.server.wm.WindowState w) {
        boolean isVisible = w.isVisible() && !w.mObscured;
        boolean isDrawn = w.isDrawn();
        if (isVisible && !isDrawn) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BOOT_enabled[0]) {
                long protoLogParam0 = w.mAttrs.type;
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BOOT, 2432701541536053712L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
            return true;
        }
        if (isDrawn) {
            switch (w.mAttrs.type) {
                case 1:
                case 2013:
                case 2021:
                    drawnWindowTypes.put(w.mAttrs.type, true);
                    break;
                case 2040:
                    drawnWindowTypes.put(2040, this.mWmService.mPolicy.isKeyguardDrawnLw());
                    break;
            }
        }
        return false;
    }

    void updateWindowsForAnimator() {
        forAllWindows(this.mUpdateWindowsForAnimator, true);
        if (this.mAsyncRotationController != null) {
            this.mAsyncRotationController.updateTargetWindows();
        }
    }

    boolean isInputMethodClientFocus(int uid, int pid) {
        com.android.server.wm.WindowState imFocus = computeImeTarget(false);
        if (imFocus == null) {
            return false;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
            android.util.Slog.i(TAG, "Desired input method target: " + imFocus);
            android.util.Slog.i(TAG, "Current focus: " + this.mCurrentFocus + " displayId=" + this.mDisplayId);
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
            android.util.Slog.i(TAG, "IM target uid/pid: " + imFocus.mSession.mUid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + imFocus.mSession.mPid);
            android.util.Slog.i(TAG, "Requesting client uid/pid: " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pid);
        }
        return imFocus.mSession.mUid == uid && imFocus.mSession.mPid == pid;
    }

    static /* synthetic */ boolean lambda$hasSecureWindowOnScreen$32(com.android.server.wm.WindowState w) {
        return w.isOnScreen() && w.isSecureLocked();
    }

    boolean hasSecureWindowOnScreen() {
        com.android.server.wm.WindowState win = getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda28
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.DisplayContent.lambda$hasSecureWindowOnScreen$32((com.android.server.wm.WindowState) obj);
            }
        });
        return win != null;
    }

    void onWindowFreezeTimeout() {
        android.util.Slog.w(TAG, "Window freeze timeout expired.");
        this.mWmService.mWindowsFreezingScreen = 2;
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onWindowFreezeTimeout$33((com.android.server.wm.WindowState) obj);
            }
        }, true);
        if (this.mWaitingForConfig) {
            this.mWaitingForConfig = false;
            this.mWmService.mLastFinishedFreezeSource = "config-wait-timeout";
            android.util.Slog.w(TAG, "onWindowFreezeTimeout set mWaitingForConfig to false");
        }
        this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onWindowFreezeTimeout$33(com.android.server.wm.WindowState w) {
        if (!w.getOrientationChanging()) {
            return;
        }
        w.orientationChangeTimedOut();
        w.mLastFreezeDuration = (int) (android.os.SystemClock.elapsedRealtime() - this.mWmService.mDisplayFreezeTime);
        android.util.Slog.w(TAG, "Force clearing orientation change: " + w);
    }

    void onWindowAnimationFinished(com.android.server.wm.WindowContainer wc, int type) {
        if (this.mImeScreenshot != null && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(wc);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(com.android.server.wm.SurfaceAnimator.animationTypeToString(type));
            java.lang.String protoLogParam2 = java.lang.String.valueOf(this.mImeScreenshot);
            java.lang.String protoLogParam3 = java.lang.String.valueOf(this.mImeScreenshot.getImeTarget());
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, -124113386733162358L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2, protoLogParam3);
        }
        if ((type & 25) != 0) {
            removeImeSurfaceByTarget(wc);
        }
    }

    void applySurfaceChangesTransaction() {
        com.android.server.wm.WindowSurfacePlacer surfacePlacer = this.mWmService.mWindowPlacerLocked;
        beginHoldScreenUpdate();
        this.mTmpUpdateAllDrawn.clear();
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
            surfacePlacer.debugLayoutRepeats("On entry to LockedInner", this.pendingLayoutChanges);
        }
        if ((this.pendingLayoutChanges & 4) != 0) {
            this.mWallpaperController.adjustWallpaperWindows();
        }
        if ((this.pendingLayoutChanges & 2) != 0) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
                android.util.Slog.v(TAG, "Computing new config from layout");
            }
            if (updateOrientation()) {
                setLayoutNeeded();
                sendNewConfiguration();
            }
        }
        if ((this.pendingLayoutChanges & 1) != 0) {
            setLayoutNeeded();
        }
        performLayout(true, false);
        this.pendingLayoutChanges = 0;
        android.os.Trace.traceBegin(32L, "applyPostLayoutPolicy");
        try {
            this.mDisplayPolicy.beginPostLayoutPolicyLw();
            forAllWindows(this.mApplyPostLayoutPolicy, true);
            this.mDisplayPolicy.finishPostLayoutPolicyLw();
            android.os.Trace.traceEnd(32L);
            this.mInsetsStateController.onPostLayout();
            this.mTmpApplySurfaceChangesTransactionState.reset();
            android.os.Trace.traceBegin(32L, "applyWindowSurfaceChanges");
            try {
                forAllWindows(this.mApplySurfaceChangesTransaction, true);
                android.os.Trace.traceEnd(32L);
                if (!com.android.window.flags.Flags.removePrepareSurfaceInPlacement()) {
                    prepareSurfaces();
                }
                this.mWallpaperController.mWallpaperControllerExt.dispatchWallpaperWindowsTarget(this.mWallpaperController.getWallpaperTarget(), this, this.mWallpaperController.getWallpaperTarget() != null);
                android.os.Trace.traceBegin(32L, "refreshRate");
                this.mTmpApplySurfaceChangesTransactionState.preferredModeId = this.mDisplayContentExt.getPreferredModeId(this.mTmpApplySurfaceChangesTransactionState.preferredRefreshRate, this.mTmpApplySurfaceChangesTransactionState.preferredModeId);
                this.mTmpApplySurfaceChangesTransactionState.preferredMinRefreshRate = this.mDisplayContentExt.getPreferredMinRefreshRate(this.mTmpApplySurfaceChangesTransactionState.preferredMinRefreshRate);
                this.mTmpApplySurfaceChangesTransactionState.preferredMaxRefreshRate = this.mDisplayContentExt.getPreferredMaxRefreshRate(this.mTmpApplySurfaceChangesTransactionState.preferredMaxRefreshRate);
                android.os.Trace.traceEnd(32L);
                this.mInsetsStateController.getImeSourceProvider().checkAndStartShowImePostLayout();
                this.mLastHasContent = this.mTmpApplySurfaceChangesTransactionState.displayHasContent;
                if (!inTransition() && !this.mDisplayRotation.isRotatingSeamlessly()) {
                    android.os.Trace.traceBegin(32L, "setDisplayProperties");
                    this.mWmService.mDisplayManagerInternal.setDisplayProperties(this.mDisplayId, this.mLastHasContent, this.mTmpApplySurfaceChangesTransactionState.preferredRefreshRate, this.mTmpApplySurfaceChangesTransactionState.preferredModeId, this.mTmpApplySurfaceChangesTransactionState.preferredMinRefreshRate, this.mTmpApplySurfaceChangesTransactionState.preferredMaxRefreshRate, this.mTmpApplySurfaceChangesTransactionState.preferMinimalPostProcessing, this.mTmpApplySurfaceChangesTransactionState.disableHdrConversion, true);
                    android.os.Trace.traceEnd(32L);
                }
                updateRecording();
                android.os.Trace.traceBegin(32L, "notifyWallpaperVisibilityChanged");
                boolean wallpaperVisible = this.mWallpaperController.isWallpaperVisible();
                if (wallpaperVisible != this.mLastWallpaperVisible) {
                    this.mLastWallpaperVisible = wallpaperVisible;
                    this.mDisplayContentExt.dispatchWallpaperVisibility(wallpaperVisible);
                    this.mWmService.mWallpaperVisibilityListeners.notifyWallpaperVisibilityChanged(this);
                }
                android.os.Trace.traceEnd(32L);
                android.os.Trace.traceBegin(32L, "mTmpUpdateAllDrawn:" + this.mTmpUpdateAllDrawn.isEmpty());
                while (!this.mTmpUpdateAllDrawn.isEmpty()) {
                    com.android.server.wm.ActivityRecord activity = this.mTmpUpdateAllDrawn.removeLast();
                    activity.updateAllDrawn();
                }
                finishHoldScreenUpdate();
            } finally {
            }
        } finally {
        }
    }

    private void getBounds(android.graphics.Rect out, int rotation) {
        getBounds(out);
        int currentRotation = this.mDisplayInfo.rotation;
        int rotationDelta = android.util.RotationUtils.deltaRotation(currentRotation, rotation);
        if (rotationDelta == 1 || rotationDelta == 3) {
            out.set(0, 0, out.height(), out.width());
        }
    }

    int getNaturalOrientation() {
        return this.mBaseDisplayWidth <= this.mBaseDisplayHeight ? 1 : 2;
    }

    int getNaturalConfigurationOrientation() {
        android.content.res.Configuration config = getConfiguration();
        if (config.windowConfiguration.getDisplayRotation() == 0) {
            return config.orientation;
        }
        android.graphics.Rect frame = this.mDisplayPolicy.getDecorInsetsInfo(0, this.mBaseDisplayWidth, this.mBaseDisplayHeight).mConfigFrame;
        return frame.width() <= frame.height() ? 1 : 2;
    }

    void performLayout(boolean initial, boolean updateInputWindows) {
        android.os.Trace.traceBegin(32L, "performLayout");
        try {
            performLayoutNoTrace(initial, updateInputWindows);
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    private void performLayoutNoTrace(boolean initial, boolean updateInputWindows) {
        if (!isLayoutNeeded()) {
            return;
        }
        clearLayoutNeeded();
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT) {
            android.util.Slog.v(TAG, "-------------------------------------");
            android.util.Slog.v(TAG, "performLayout: dw=" + this.mDisplayInfo.logicalWidth + " dh=" + this.mDisplayInfo.logicalHeight);
        }
        this.mDisplayContentExt.performLayoutNoTrace(this.mDisplayPolicy, this.mDisplayFrames, getConfiguration().uiMode);
        int seq = this.mLayoutSeq + 1;
        if (seq < 0) {
            seq = 0;
        }
        this.mLayoutSeq = seq;
        this.mTmpInitial = initial;
        forAllWindows(this.mPerformLayout, true);
        forAllWindows(this.mPerformLayoutAttached, true);
        this.mDisplayContentExt.onFindFocusedWindow();
        this.mInputMonitor.setUpdateInputWindowsNeededLw();
        if (updateInputWindows) {
            this.mInputMonitor.updateInputWindowsLw(false);
        }
    }

    android.window.ScreenCapture.LayerCaptureArgs getLayerCaptureArgs(final java.util.Set<java.lang.Integer> windowTypesToExclude) {
        if (!this.mWmService.mPolicy.isScreenOn()) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_SCREENSHOT) {
                android.util.Slog.i(TAG, "Attempted to take screenshot while display was off.");
                return null;
            }
            return null;
        }
        getBounds(this.mTmpRect);
        this.mTmpRect.offsetTo(0, 0);
        android.window.ScreenCapture.LayerCaptureArgs.Builder builder = new android.window.ScreenCapture.LayerCaptureArgs.Builder(getSurfaceControl()).setSourceCrop(this.mTmpRect);
        if (!windowTypesToExclude.isEmpty()) {
            final java.util.ArrayList<android.view.SurfaceControl> surfaceControls = new java.util.ArrayList<>();
            forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda13
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.DisplayContent.lambda$getLayerCaptureArgs$34(windowTypesToExclude, surfaceControls, (com.android.server.wm.WindowState) obj);
                }
            }, true);
            if (!surfaceControls.isEmpty()) {
                builder.setExcludeLayers((android.view.SurfaceControl[]) surfaceControls.toArray(new android.view.SurfaceControl[0]));
            }
        }
        return builder.build();
    }

    static /* synthetic */ void lambda$getLayerCaptureArgs$34(java.util.Set windowTypesToExclude, java.util.ArrayList surfaceControls, com.android.server.wm.WindowState window) {
        if (windowTypesToExclude.contains(java.lang.Integer.valueOf(window.getWindowType()))) {
            surfaceControls.add(window.mSurfaceControl);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void onDescendantOverrideConfigurationChanged() {
        setLayoutNeeded();
        this.mWmService.requestTraversal();
    }

    @Override // com.android.server.wm.WindowContainer
    boolean okToDisplay() {
        return okToDisplay(false, false);
    }

    boolean okToDisplay(boolean ignoreFrozen, boolean ignoreScreenOn) {
        return this.mDisplayId == 0 ? (!this.mWmService.mDisplayFrozen || ignoreFrozen) && this.mWmService.mDisplayEnabled && (ignoreScreenOn || this.mWmService.mPolicy.isScreenOn()) : this.mDisplayInfo.state == 2;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean okToAnimate(boolean ignoreFrozen, boolean ignoreScreenOn) {
        return okToDisplay(ignoreFrozen, ignoreScreenOn) && (this.mDisplayId != 0 || this.mWmService.mPolicy.okToAnimate(ignoreScreenOn)) && (ignoreFrozen || this.mDisplayPolicy.isScreenOnFully());
    }

    static final class TaskForResizePointSearchResult implements java.util.function.Predicate<com.android.server.wm.Task> {
        private int delta;
        private android.graphics.Rect mTmpRect = new android.graphics.Rect();
        private com.android.server.wm.Task taskForResize;
        private int x;
        private int y;

        TaskForResizePointSearchResult() {
        }

        com.android.server.wm.Task process(com.android.server.wm.WindowContainer root, int x, int y, int delta) {
            this.taskForResize = null;
            this.x = x;
            this.y = y;
            this.delta = delta;
            this.mTmpRect.setEmpty();
            root.forAllTasks(this);
            return this.taskForResize;
        }

        @Override // java.util.function.Predicate
        public boolean test(com.android.server.wm.Task task) {
            if (!task.getRootTask().getWindowConfiguration().canResizeTask() || task.getWindowingMode() == 1 || task.isOrganized()) {
                return true;
            }
            task.getDimBounds(this.mTmpRect);
            this.mTmpRect.inset(-this.delta, -this.delta);
            if (this.mTmpRect.contains(this.x, this.y)) {
                this.mTmpRect.inset(this.delta, this.delta);
                if (this.mTmpRect.contains(this.x, this.y)) {
                    return true;
                }
                this.taskForResize = task;
                return true;
            }
            return false;
        }
    }

    private static final class ApplySurfaceChangesTransactionState {
        public boolean disableHdrConversion;
        public boolean displayHasContent;
        public boolean obscured;
        public boolean preferMinimalPostProcessing;
        public float preferredMaxRefreshRate;
        public float preferredMinRefreshRate;
        public int preferredModeId;
        public float preferredRefreshRate;
        public boolean syswin;

        private ApplySurfaceChangesTransactionState() {
        }

        void reset() {
            this.displayHasContent = false;
            this.obscured = false;
            this.syswin = false;
            this.preferMinimalPostProcessing = false;
            this.preferredRefreshRate = com.android.server.wm.DisplayContent.INVALID_DPI;
            this.preferredModeId = 0;
            this.preferredMinRefreshRate = com.android.server.wm.DisplayContent.INVALID_DPI;
            this.preferredMaxRefreshRate = com.android.server.wm.DisplayContent.INVALID_DPI;
            this.disableHdrConversion = false;
        }
    }

    private static class ImeContainer extends com.android.server.wm.DisplayArea.Tokens {
        boolean mNeedsLayer;

        ImeContainer(com.android.server.wm.WindowManagerService wms) {
            super(wms, com.android.server.wm.DisplayArea.Type.ABOVE_TASKS, "ImeContainer", 8);
            this.mNeedsLayer = false;
        }

        public void setNeedsLayer() {
            this.mNeedsLayer = true;
        }

        @Override // com.android.server.wm.DisplayArea.Tokens, com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
        int getOrientation(int candidate) {
            if (shouldIgnoreOrientationRequest(candidate)) {
                return -2;
            }
            return candidate;
        }

        @Override // com.android.server.wm.WindowContainer
        void updateAboveInsetsState(android.view.InsetsState aboveInsetsState, android.util.SparseArray<android.view.InsetsSource> localInsetsSourcesFromParent, android.util.ArraySet<com.android.server.wm.WindowState> insetsChangedWindows) {
            if (skipImeWindowsDuringTraversal(this.mDisplayContent)) {
                return;
            }
            super.updateAboveInsetsState(aboveInsetsState, localInsetsSourcesFromParent, insetsChangedWindows);
        }

        @Override // com.android.server.wm.WindowContainer
        boolean forAllWindows(com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> callback, boolean traverseTopToBottom) {
            com.android.server.wm.DisplayContent dc = this.mDisplayContent;
            if (skipImeWindowsDuringTraversal(dc)) {
                return false;
            }
            return super.forAllWindows(callback, traverseTopToBottom);
        }

        private static boolean skipImeWindowsDuringTraversal(com.android.server.wm.DisplayContent dc) {
            return (dc.mImeLayeringTarget == null || dc.mWmService.mDisplayFrozen) ? false : true;
        }

        boolean forAllWindowForce(com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> callback, boolean traverseTopToBottom) {
            return super.forAllWindows(callback, traverseTopToBottom);
        }

        @Override // com.android.server.wm.WindowContainer
        void assignLayer(android.view.SurfaceControl.Transaction t, int layer) {
            if (!this.mNeedsLayer) {
                return;
            }
            super.assignLayer(t, layer);
            this.mNeedsLayer = false;
        }

        @Override // com.android.server.wm.WindowContainer
        void assignRelativeLayer(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl relativeTo, int layer, boolean forceUpdate) {
            if (!this.mNeedsLayer) {
                return;
            }
            super.assignRelativeLayer(t, relativeTo, layer, forceUpdate);
            this.mNeedsLayer = false;
        }

        @Override // com.android.server.wm.DisplayArea
        void setOrganizer(android.window.IDisplayAreaOrganizer organizer, boolean skipDisplayAreaAppeared) {
            super.setOrganizer(organizer, skipDisplayAreaAppeared);
            this.mDisplayContent.updateImeParent();
            if (organizer != null) {
                android.view.SurfaceControl imeParentSurfaceControl = getParentSurfaceControl();
                if (this.mSurfaceControl != null && imeParentSurfaceControl != null) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[2]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(imeParentSurfaceControl);
                        com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, -1556099709547629010L, 0, null, protoLogParam0);
                    }
                    getPendingTransaction().reparent(this.mSurfaceControl, imeParentSurfaceControl);
                    return;
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_IME_enabled[4]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(this.mSurfaceControl);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(imeParentSurfaceControl);
                    com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME, 1119786654111970652L, 0, null, protoLogParam02, protoLogParam1);
                }
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer
    android.view.SurfaceSession getSession() {
        return this.mSession;
    }

    @Override // com.android.server.wm.WindowContainer
    android.view.SurfaceControl.Builder makeChildSurface(com.android.server.wm.WindowContainer child) {
        android.view.SurfaceSession s = child != null ? child.getSession() : getSession();
        android.view.SurfaceControl.Builder b = this.mWmService.makeSurfaceBuilder(s).setContainerLayer();
        if (child == null) {
            return b;
        }
        return b.setName(child.getName()).setParent(this.mSurfaceControl);
    }

    android.view.SurfaceControl.Builder makeOverlay() {
        return this.mWmService.makeSurfaceBuilder(this.mSession).setParent(getOverlayLayer());
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl.Builder makeAnimationLeash() {
        return this.mWmService.makeSurfaceBuilder(this.mSession).setParent(this.mSurfaceControl).setContainerLayer();
    }

    void reparentToOverlay(android.view.SurfaceControl.Transaction transaction, android.view.SurfaceControl surface) {
        transaction.reparent(surface, getOverlayLayer());
    }

    void applyMagnificationSpec(android.view.MagnificationSpec spec) {
        if (spec.scale != 1.0d) {
            this.mMagnificationSpec = spec;
        } else {
            this.mMagnificationSpec = null;
        }
        updateImeParent();
        if (spec.scale != 1.0d) {
            applyMagnificationSpec(getPendingTransaction(), spec);
        } else {
            clearMagnificationSpec(getPendingTransaction());
        }
        getPendingTransaction().apply();
    }

    void reapplyMagnificationSpec() {
        if (this.mMagnificationSpec != null) {
            applyMagnificationSpec(getPendingTransaction(), this.mMagnificationSpec);
        }
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    void onParentChanged(com.android.server.wm.ConfigurationContainer newParent, com.android.server.wm.ConfigurationContainer oldParent) {
        if (!isReady()) {
            this.mDisplayReady = true;
            if (this.mWmService.mDisplayManagerInternal != null) {
                setDisplayInfoOverride();
                configureDisplayPolicy();
            }
            if (!this.isDefaultDisplay) {
                this.mDisplayRotation.updateRotationUnchecked(true);
            }
            reconfigureDisplayLocked();
            onRequestedOverrideConfigurationChanged(getRequestedOverrideConfiguration());
            this.mWmService.mDisplayNotificationController.dispatchDisplayAdded(this);
            com.android.server.wm.WindowProcessController wpc = this.mAtmService.getProcessController(getDisplayUiContext().getIApplicationThread());
            this.mWmService.mWindowContextListenerController.registerWindowContainerListener(wpc, getDisplayUiContext().getWindowContextToken(), this, -1, null);
        }
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer
    void assignChildLayers(android.view.SurfaceControl.Transaction t) {
        assignRelativeLayerForIme(t, false);
        super.assignChildLayers(t);
    }

    private void assignRelativeLayerForIme(android.view.SurfaceControl.Transaction t, boolean forceUpdate) {
        if (this.mImeWindowsContainer.isOrganized()) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
                android.util.Slog.i(TAG, "ImeContainer is organized. Skip assignRelativeLayerForIme.");
                return;
            }
            return;
        }
        this.mImeWindowsContainer.setNeedsLayer();
        com.android.server.wm.WindowState imeTarget = this.mImeLayeringTarget;
        boolean imeTargetIsMain = this.mDisplayContentExt.imeTargetIsMainWindow(imeTarget);
        if (imeTarget != null && (imeTarget.mActivityRecord == null || !imeTarget.mActivityRecord.hasStartingWindow() || !imeTargetIsMain)) {
            com.android.server.wm.WindowToken imeControlTargetToken = (this.mImeControlTarget == null || this.mImeControlTarget.getWindow() == null) ? null : this.mImeControlTarget.getWindow().mToken;
            boolean canImeTargetSetRelativeLayer = imeTarget.getSurfaceControl() != null && imeTarget.mToken == imeControlTargetToken && this.mDisplayContentExt.shouldAssignRelativeLayerForIme(imeTarget) && (!imeTarget.inMultiWindowMode() || imeTarget.getWrapper().getExtImpl().layoutFullscreenInEmbedding());
            if (canImeTargetSetRelativeLayer) {
                this.mImeWindowsContainer.assignRelativeLayer(t, imeTarget.getSurfaceControl(), 1, forceUpdate);
                return;
            }
        }
        if (this.mInputMethodSurfaceParent != null) {
            this.mImeWindowsContainer.assignRelativeLayer(t, this.mInputMethodSurfaceParent, 1, forceUpdate);
        }
    }

    void assignRelativeLayerForImeTargetChild(android.view.SurfaceControl.Transaction t, com.android.server.wm.WindowContainer child) {
        child.assignRelativeLayer(t, this.mImeWindowsContainer.getSurfaceControl(), 1);
    }

    @Override // com.android.server.wm.DisplayArea.Dimmable, com.android.server.wm.WindowContainer
    void prepareSurfaces() {
        android.os.Trace.traceBegin(32L, "prepareSurfaces");
        try {
            android.view.SurfaceControl.Transaction transaction = getPendingTransaction();
            this.mDisplayContentExt.hookPrepareSurfaces(this, transaction);
            super.prepareSurfaces();
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    void deferUpdateImeTarget() {
        if (this.mDeferUpdateImeTargetCount == 0) {
            this.mUpdateImeRequestedWhileDeferred = false;
        }
        this.mDeferUpdateImeTargetCount++;
    }

    void continueUpdateImeTarget() {
        if (this.mDeferUpdateImeTargetCount == 0) {
            return;
        }
        this.mDeferUpdateImeTargetCount--;
        if (this.mDeferUpdateImeTargetCount == 0 && this.mUpdateImeRequestedWhileDeferred) {
            computeImeTarget(true);
        }
    }

    private boolean canUpdateImeTarget() {
        return this.mDeferUpdateImeTargetCount == 0;
    }

    com.android.server.wm.InputMonitor getInputMonitor() {
        return this.mInputMonitor;
    }

    boolean getLastHasContent() {
        return this.mLastHasContent;
    }

    void setLastHasContent() {
        this.mLastHasContent = true;
    }

    void registerPointerEventListener(android.view.WindowManagerPolicyConstants.PointerEventListener listener) {
        this.mPointerEventDispatcher.registerInputEventListener(listener);
    }

    void unregisterPointerEventListener(android.view.WindowManagerPolicyConstants.PointerEventListener listener) {
        this.mPointerEventDispatcher.unregisterInputEventListener(listener);
    }

    void transferAppTransitionFrom(com.android.server.wm.DisplayContent from) {
        boolean prepared = this.mAppTransition.transferFrom(from.mAppTransition);
        if (prepared && okToAnimate()) {
            this.mSkipAppTransitionAnimation = false;
        }
    }

    @java.lang.Deprecated
    void prepareAppTransition(int transit) {
        prepareAppTransition(transit, 0);
    }

    @java.lang.Deprecated
    void prepareAppTransition(int transit, int flags) {
        boolean prepared = this.mAppTransition.prepareAppTransition(transit, flags);
        if (!prepared || !okToAnimate() || transit == 0 || this.mDisplayContentExt.skipAppTransitionAnimation()) {
            return;
        }
        this.mDisplayContent.mSkipAppTransitionAnimation = false;
    }

    void requestTransitionAndLegacyPrepare(int transit, int flags) {
        prepareAppTransition(transit, flags);
        this.mTransitionController.requestTransitionIfNeeded(transit, flags, null, this);
    }

    void executeAppTransition() {
        if (this.mAtmService.getWrapper().getFlexibleExtImpl().isEmbbeddingTaskAnimating()) {
            return;
        }
        this.mTransitionController.setReady(this);
        if (this.mAppTransition.isTransitionSet()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[3]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mAppTransition);
                long protoLogParam1 = this.mDisplayId;
                java.lang.String protoLogParam2 = java.lang.String.valueOf(android.os.Debug.getCallers(5));
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 7019634211809476510L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), protoLogParam2);
            }
            this.mAppTransition.setReady();
            this.mWmService.mWindowPlacerLocked.requestTraversal();
        }
        this.mDisplayContentExt.onPreReady();
    }

    void handleAnimatingStoppedAndTransition() {
        this.mAppTransition.setIdle();
        for (int i = this.mNoAnimationNotifyOnTransitionFinished.size() - 1; i >= 0; i--) {
            android.os.IBinder token = this.mNoAnimationNotifyOnTransitionFinished.get(i);
            this.mAppTransition.notifyAppTransitionFinishedLocked(token);
        }
        this.mNoAnimationNotifyOnTransitionFinished.clear();
        this.mWallpaperController.hideDeferredWallpapersIfNeededLegacy();
        onAppTransitionDone();
        int changes = 0 | 1;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[1]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, -3219913508985161450L, 0, null, null);
        }
        computeImeTarget(true);
        this.mWallpaperMayChange = true;
        this.mWmService.mFocusMayChange = true;
        this.pendingLayoutChanges |= changes;
    }

    boolean isNextTransitionForward() {
        if (!this.mTransitionController.isShellTransitionsEnabled()) {
            return this.mAppTransition.containsTransitRequest(1) || this.mAppTransition.containsTransitRequest(3);
        }
        int type = this.mTransitionController.getCollectingTransitionType();
        return type == 1 || type == 3;
    }

    boolean supportsSystemDecorations() {
        boolean forceDesktopModeOnDisplay = forceDesktopMode();
        if (com.android.window.flags.Flags.rearDisplayDisableForceDesktopSystemDecorations()) {
            forceDesktopModeOnDisplay = forceDesktopModeOnDisplay && (this.mDisplay.getFlags() & 8192) == 0;
        }
        return (this.mWmService.mDisplayWindowSettings.shouldShowSystemDecorsLocked(this) || (this.mDisplay.getFlags() & 64) != 0 || forceDesktopModeOnDisplay || this.mDisplayContentExt.supportDesktopModeOnExternalDisplays(this)) && this.mDisplayId != this.mWmService.mVr2dDisplayId && isTrusted();
    }

    boolean isHomeSupported() {
        return (this.mWmService.mDisplayWindowSettings.isHomeSupportedLocked(this) && isTrusted()) || supportsSystemDecorations();
    }

    android.view.SurfaceControl getWindowingLayer() {
        return this.mDisplayAreaPolicy.getWindowingArea().mSurfaceControl;
    }

    com.android.server.wm.DisplayArea.Tokens getImeContainer() {
        return this.mImeWindowsContainer;
    }

    android.view.SurfaceControl getOverlayLayer() {
        return this.mOverlayLayer;
    }

    android.view.SurfaceControl getInputOverlayLayer() {
        return this.mInputOverlayLayer;
    }

    android.view.SurfaceControl getA11yOverlayLayer() {
        return this.mA11yOverlayLayer;
    }

    android.view.SurfaceControl[] findRoundedCornerOverlays() {
        java.util.List<android.view.SurfaceControl> roundedCornerOverlays = new java.util.ArrayList<>();
        for (com.android.server.wm.WindowToken token : this.mTokenMap.values()) {
            if (token.mRoundedCornerOverlay && token.isVisible()) {
                roundedCornerOverlays.add(token.mSurfaceControl);
            }
        }
        return (android.view.SurfaceControl[]) roundedCornerOverlays.toArray(new android.view.SurfaceControl[0]);
    }

    boolean updateSystemGestureExclusion() {
        if (this.mSystemGestureExclusionListeners.getRegisteredCallbackCount() == 0) {
            return false;
        }
        android.graphics.Region systemGestureExclusion = android.graphics.Region.obtain();
        this.mSystemGestureExclusionWasRestricted = calculateSystemGestureExclusion(systemGestureExclusion, this.mSystemGestureExclusionUnrestricted);
        try {
            if (this.mSystemGestureExclusion.equals(systemGestureExclusion)) {
                return false;
            }
            this.mSystemGestureExclusion.set(systemGestureExclusion);
            android.graphics.Region unrestrictedOrNull = this.mSystemGestureExclusionWasRestricted ? this.mSystemGestureExclusionUnrestricted : null;
            for (int i = this.mSystemGestureExclusionListeners.beginBroadcast() - 1; i >= 0; i--) {
                try {
                    this.mSystemGestureExclusionListeners.getBroadcastItem(i).onSystemGestureExclusionChanged(this.mDisplayId, systemGestureExclusion, unrestrictedOrNull);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Failed to notify SystemGestureExclusionListener", e);
                }
            }
            this.mSystemGestureExclusionListeners.finishBroadcast();
            return true;
        } finally {
            systemGestureExclusion.recycle();
        }
    }

    boolean calculateSystemGestureExclusion(final android.graphics.Region outExclusion, final android.graphics.Region outExclusionUnrestricted) {
        outExclusion.setEmpty();
        if (outExclusionUnrestricted != null) {
            outExclusionUnrestricted.setEmpty();
        }
        final android.graphics.Region unhandled = android.graphics.Region.obtain();
        unhandled.set(0, 0, this.mDisplayFrames.mWidth, this.mDisplayFrames.mHeight);
        android.view.InsetsState state = this.mInsetsStateController.getRawInsetsState();
        android.graphics.Rect df = state.getDisplayFrame();
        android.graphics.Insets gestureInsets = state.calculateInsets(df, android.view.WindowInsets.Type.systemGestures(), false);
        this.mSystemGestureFrameLeft.set(df.left, df.top, df.left + gestureInsets.left, df.bottom);
        this.mSystemGestureFrameRight.set(df.right - gestureInsets.right, df.top, df.right, df.bottom);
        final android.graphics.Region touchableRegion = android.graphics.Region.obtain();
        final android.graphics.Region local = android.graphics.Region.obtain();
        final int[] remainingLeftRight = {this.mSystemGestureExclusionLimit, this.mSystemGestureExclusionLimit};
        final com.android.server.wm.RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
        final boolean isPlayingRecents = this.mTransitionController.mExt.getRecentsTransition() != null;
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$calculateSystemGestureExclusion$35(isPlayingRecents, unhandled, recentsAnimationController, touchableRegion, local, remainingLeftRight, outExclusion, outExclusionUnrestricted, (com.android.server.wm.WindowState) obj);
            }
        }, true);
        local.recycle();
        touchableRegion.recycle();
        unhandled.recycle();
        return remainingLeftRight[0] < this.mSystemGestureExclusionLimit || remainingLeftRight[1] < this.mSystemGestureExclusionLimit;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$calculateSystemGestureExclusion$35(boolean isPlayingRecents, android.graphics.Region unhandled, com.android.server.wm.RecentsAnimationController recentsAnimationController, android.graphics.Region touchableRegion, android.graphics.Region local, int[] remainingLeftRight, android.graphics.Region outExclusion, android.graphics.Region outExclusionUnrestricted, com.android.server.wm.WindowState w) {
        if (isPlayingRecents) {
            if (!w.isActivityTypeHomeOrRecents() || unhandled.isEmpty()) {
                return;
            }
        } else {
            boolean ignoreRecentsAnimationTarget = recentsAnimationController != null && recentsAnimationController.shouldApplyInputConsumer(w.getActivityRecord());
            if (!w.canReceiveTouchInput() || !w.isVisible() || (w.getAttrs().flags & 16) != 0 || unhandled.isEmpty() || ignoreRecentsAnimationTarget) {
                return;
            }
        }
        if (w.getWrapper().getExtImpl().checkIfWindowingModeZoom(w.getWindowingMode())) {
            return;
        }
        w.getEffectiveTouchableRegion(touchableRegion);
        touchableRegion.op(unhandled, android.graphics.Region.Op.INTERSECT);
        if (w.isImplicitlyExcludingAllSystemGestures()) {
            local.set(touchableRegion);
        } else {
            com.android.server.wm.utils.RegionUtils.rectListToRegion(w.getSystemGestureExclusion(), local);
            local.scale(w.mGlobalScale);
            android.graphics.Rect frame = w.getWindowFrames().mFrame;
            local.translate(frame.left, frame.top);
            local.op(touchableRegion, android.graphics.Region.Op.INTERSECT);
        }
        if (needsGestureExclusionRestrictions(w, false) || this.mDisplayContentExt.forceUpdateGestureExclusion(w)) {
            remainingLeftRight[0] = addToGlobalAndConsumeLimit(local, outExclusion, this.mSystemGestureFrameLeft, remainingLeftRight[0], w, 0);
            remainingLeftRight[1] = addToGlobalAndConsumeLimit(local, outExclusion, this.mSystemGestureFrameRight, remainingLeftRight[1], w, 1);
            android.graphics.Region middle = android.graphics.Region.obtain(local);
            middle.op(this.mSystemGestureFrameLeft, android.graphics.Region.Op.DIFFERENCE);
            middle.op(this.mSystemGestureFrameRight, android.graphics.Region.Op.DIFFERENCE);
            outExclusion.op(middle, android.graphics.Region.Op.UNION);
            middle.recycle();
        } else {
            boolean loggable = needsGestureExclusionRestrictions(w, true);
            if (loggable) {
                addToGlobalAndConsumeLimit(local, outExclusion, this.mSystemGestureFrameLeft, Integer.MAX_VALUE, w, 0);
                addToGlobalAndConsumeLimit(local, outExclusion, this.mSystemGestureFrameRight, Integer.MAX_VALUE, w, 1);
            }
            outExclusion.op(local, android.graphics.Region.Op.UNION);
        }
        if (outExclusionUnrestricted != null) {
            outExclusionUnrestricted.op(local, android.graphics.Region.Op.UNION);
        }
        unhandled.op(touchableRegion, android.graphics.Region.Op.DIFFERENCE);
    }

    private static boolean needsGestureExclusionRestrictions(com.android.server.wm.WindowState win, boolean ignoreRequest) {
        int type = win.mAttrs.type;
        int privateFlags = win.mAttrs.privateFlags;
        boolean stickyHideNav = !win.isRequestedVisible(android.view.WindowInsets.Type.navigationBars()) && win.mAttrs.insetsFlags.behavior == 2;
        return (!stickyHideNav || ignoreRequest) && type != 2011 && type != 2040 && win.getActivityType() != 2 && (privateFlags & 32) == 0 && mStaticDisplayContentExt.isWinHasGestureExclusionRestrictions(win);
    }

    static boolean logsGestureExclusionRestrictions(com.android.server.wm.WindowState win) {
        android.view.WindowManager.LayoutParams attrs;
        int type;
        return win.mWmService.mConstants.mSystemGestureExclusionLogDebounceTimeoutMillis > 0 && (type = (attrs = win.getAttrs()).type) != 2013 && type != 3 && type != 2019 && (attrs.flags & 16) == 0 && needsGestureExclusionRestrictions(win, true) && win.getDisplayContent().mDisplayPolicy.hasSideGestures();
    }

    private static int addToGlobalAndConsumeLimit(android.graphics.Region local, final android.graphics.Region global, android.graphics.Rect edge, int limit, com.android.server.wm.WindowState win, int side) {
        android.graphics.Region r = android.graphics.Region.obtain(local);
        r.op(edge, android.graphics.Region.Op.INTERSECT);
        final int[] remaining = {limit};
        final int[] requestedExclusion = {0};
        com.android.server.wm.utils.RegionUtils.forEachRectReverse(r, new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.DisplayContent.lambda$addToGlobalAndConsumeLimit$36(remaining, requestedExclusion, global, (android.graphics.Rect) obj);
            }
        });
        int grantedExclusion = limit - remaining[0];
        win.setLastExclusionHeights(side, requestedExclusion[0], grantedExclusion);
        r.recycle();
        return remaining[0];
    }

    static /* synthetic */ void lambda$addToGlobalAndConsumeLimit$36(int[] remaining, int[] requestedExclusion, android.graphics.Region global, android.graphics.Rect rect) {
        if (remaining[0] <= 0) {
            return;
        }
        int height = rect.height();
        requestedExclusion[0] = requestedExclusion[0] + height;
        if (height > remaining[0]) {
            rect.top = rect.bottom - remaining[0];
        }
        remaining[0] = remaining[0] - height;
        global.op(rect, android.graphics.Region.Op.UNION);
    }

    void registerSystemGestureExclusionListener(android.view.ISystemGestureExclusionListener listener) {
        boolean changed;
        this.mSystemGestureExclusionListeners.register(listener);
        if (this.mSystemGestureExclusionListeners.getRegisteredCallbackCount() == 1) {
            changed = updateSystemGestureExclusion();
        } else {
            changed = false;
        }
        if (!changed) {
            android.graphics.Region unrestrictedOrNull = this.mSystemGestureExclusionWasRestricted ? this.mSystemGestureExclusionUnrestricted : null;
            try {
                listener.onSystemGestureExclusionChanged(this.mDisplayId, this.mSystemGestureExclusion, unrestrictedOrNull);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to notify SystemGestureExclusionListener during register", e);
            }
        }
    }

    void unregisterSystemGestureExclusionListener(android.view.ISystemGestureExclusionListener listener) {
        this.mSystemGestureExclusionListeners.unregister(listener);
    }

    void registerDecorViewGestureListener(android.view.IDecorViewGestureListener listener) {
        this.mDecorViewGestureListener.register(listener);
    }

    void unregisterDecorViewGestureListener(android.view.IDecorViewGestureListener listener) {
        this.mDecorViewGestureListener.unregister(listener);
    }

    void updateDecorViewGestureIntercepted(android.os.IBinder token, boolean intercepted) {
        for (int i = this.mDecorViewGestureListener.beginBroadcast() - 1; i >= 0; i--) {
            try {
                this.mDecorViewGestureListener.getBroadcastItem(i).onInterceptionChanged(token, intercepted);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to notify DecorViewGestureListener", e);
            }
        }
        this.mDecorViewGestureListener.finishBroadcast();
    }

    void updateKeepClearAreas() {
        java.util.Set<android.graphics.Rect> restrictedKeepClearAreas = new android.util.ArraySet<>();
        java.util.Set<android.graphics.Rect> unrestrictedKeepClearAreas = new android.util.ArraySet<>();
        getKeepClearAreas(restrictedKeepClearAreas, unrestrictedKeepClearAreas);
        if (!this.mRestrictedKeepClearAreas.equals(restrictedKeepClearAreas) || !this.mUnrestrictedKeepClearAreas.equals(unrestrictedKeepClearAreas)) {
            this.mRestrictedKeepClearAreas = restrictedKeepClearAreas;
            this.mUnrestrictedKeepClearAreas = unrestrictedKeepClearAreas;
            this.mWmService.mDisplayNotificationController.dispatchKeepClearAreasChanged(this, restrictedKeepClearAreas, unrestrictedKeepClearAreas);
        }
    }

    void getKeepClearAreas(final java.util.Set<android.graphics.Rect> outRestricted, final java.util.Set<android.graphics.Rect> outUnrestricted) {
        final android.graphics.Matrix tmpMatrix = new android.graphics.Matrix();
        final float[] tmpFloat9 = new float[9];
        final com.android.server.wm.RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
        forAllWindows(new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda5
            public final boolean apply(java.lang.Object obj) {
                return com.android.server.wm.DisplayContent.lambda$getKeepClearAreas$38(recentsAnimationController, outRestricted, outUnrestricted, tmpMatrix, tmpFloat9, (com.android.server.wm.WindowState) obj);
            }
        }, true);
    }

    static /* synthetic */ boolean lambda$getKeepClearAreas$38(com.android.server.wm.RecentsAnimationController recentsAnimationController, java.util.Set outRestricted, final java.util.Set outUnrestricted, android.graphics.Matrix tmpMatrix, float[] tmpFloat9, com.android.server.wm.WindowState w) {
        boolean ignoreRecentsAnimationTarget = recentsAnimationController != null && recentsAnimationController.shouldApplyInputConsumer(w.getActivityRecord());
        if (ignoreRecentsAnimationTarget) {
            return false;
        }
        if (w.isVisible() && !w.inPinnedWindowingMode()) {
            w.getKeepClearAreas(outRestricted, outUnrestricted, tmpMatrix, tmpFloat9);
            if (w.mIsImWindow) {
                android.graphics.Region touchableRegion = android.graphics.Region.obtain();
                w.getEffectiveTouchableRegion(touchableRegion);
                com.android.server.wm.utils.RegionUtils.forEachRect(touchableRegion, new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda10
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        outUnrestricted.add((android.graphics.Rect) obj);
                    }
                });
                touchableRegion.recycle();
            }
        }
        return w.getWindowType() == 1 && w.getWindowingMode() == 1;
    }

    java.util.Set<android.graphics.Rect> getKeepClearAreas() {
        java.util.Set<android.graphics.Rect> keepClearAreas = new android.util.ArraySet<>();
        getKeepClearAreas(keepClearAreas, keepClearAreas);
        return keepClearAreas;
    }

    protected com.android.internal.logging.MetricsLogger getMetricsLogger() {
        if (this.mMetricsLogger == null) {
            this.mMetricsLogger = new com.android.internal.logging.MetricsLogger();
        }
        return this.mMetricsLogger;
    }

    void requestDisplayUpdate(java.lang.Runnable onDisplayChangeApplied) {
        this.mAtmService.deferWindowLayout();
        try {
            this.mDisplayUpdater.updateDisplayInfo(onDisplayChangeApplied);
        } finally {
            this.mAtmService.continueWindowLayout();
        }
    }

    void onDisplayInfoUpdated(android.view.DisplayInfo newDisplayInfo) {
        int lastDisplayState = this.mDisplayInfo.state;
        updateDisplayInfo(newDisplayInfo);
        int displayId = this.mDisplay.getDisplayId();
        int displayState = this.mDisplayInfo.state;
        if (displayId != 0) {
            if (this.mDisplayContentExt.isSecondDisplay(this)) {
                if (displayState == 2) {
                    this.mRootWindowContainer.mDisplayOffTokenAcquirer.release(this.mDisplayId);
                } else {
                    this.mRootWindowContainer.mDisplayOffTokenAcquirer.acquire(this.mDisplayId);
                }
            } else if (displayState == 1) {
                this.mRootWindowContainer.mDisplayOffTokenAcquirer.acquire(this.mDisplayId);
            } else if (displayState == 2) {
                this.mRootWindowContainer.mDisplayOffTokenAcquirer.release(this.mDisplayId);
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
                long protoLogParam0 = this.mDisplayId;
                long protoLogParam1 = lastDisplayState;
                long protoLogParam2 = displayState;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -8165317816061445169L, 21, "Content Recording: Display %d state was (%d), is now (%d), so update recording?", java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2));
            }
            if (lastDisplayState != displayState) {
                updateRecording();
            }
        }
        this.mWallpaperController.resetLargestDisplay(this.mDisplay);
        if (android.view.Display.isSuspendedState(lastDisplayState) && !android.view.Display.isSuspendedState(displayState) && displayState != 0) {
            this.mWmService.mWindowContextListenerController.dispatchPendingConfigurationIfNeeded(this.mDisplayId);
        }
        this.mDisplayContentExt.triggerIntoComapct();
        if (displayState == 2) {
            this.mDisplayContentExt.displayChangeToOn();
        }
        this.mWmService.requestTraversal();
    }

    static boolean alwaysCreateRootTask(int windowingMode, int activityType) {
        return ((activityType == 1 || activityType == 3) && (windowingMode == 1 || windowingMode == 5 || windowingMode == 100 || windowingMode == 2 || windowingMode == 6)) || windowingMode == 120;
    }

    com.android.server.wm.Task getFocusedRootTask() {
        return (com.android.server.wm.Task) getItemFromTaskDisplayAreas(new java.util.function.Function() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda25
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.wm.TaskDisplayArea) obj).getFocusedRootTask();
            }
        });
    }

    void removeRootTasksInWindowingModes(final int... windowingModes) {
        if (windowingModes == null || windowingModes.length == 0) {
            return;
        }
        final java.util.ArrayList<com.android.server.wm.Task> rootTasks = new java.util.ArrayList<>();
        forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.DisplayContent.lambda$removeRootTasksInWindowingModes$39(windowingModes, rootTasks, (com.android.server.wm.Task) obj);
            }
        });
        for (int i = rootTasks.size() - 1; i >= 0; i--) {
            this.mRootWindowContainer.mTaskSupervisor.removeRootTask(rootTasks.get(i));
        }
    }

    static /* synthetic */ void lambda$removeRootTasksInWindowingModes$39(int[] windowingModes, java.util.ArrayList rootTasks, com.android.server.wm.Task rootTask) {
        for (int windowingMode : windowingModes) {
            if (!rootTask.mCreatedByOrganizer && rootTask.getWindowingMode() == windowingMode && rootTask.isActivityTypeStandardOrUndefined()) {
                rootTasks.add(rootTask);
            }
        }
    }

    void removeRootTasksWithActivityTypes(final int... activityTypes) {
        if (activityTypes == null || activityTypes.length == 0) {
            return;
        }
        final java.util.ArrayList<com.android.server.wm.Task> rootTasks = new java.util.ArrayList<>();
        forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda26
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.DisplayContent.lambda$removeRootTasksWithActivityTypes$40(activityTypes, rootTasks, (com.android.server.wm.Task) obj);
            }
        });
        for (int i = rootTasks.size() - 1; i >= 0; i--) {
            this.mRootWindowContainer.mTaskSupervisor.removeRootTask(rootTasks.get(i));
        }
    }

    static /* synthetic */ void lambda$removeRootTasksWithActivityTypes$40(int[] activityTypes, java.util.ArrayList rootTasks, com.android.server.wm.Task rootTask) {
        for (int activityType : activityTypes) {
            if (rootTask.mCreatedByOrganizer) {
                for (int k = rootTask.getChildCount() - 1; k >= 0; k--) {
                    com.android.server.wm.Task task = (com.android.server.wm.Task) rootTask.getChildAt(k);
                    if (task.getActivityType() == activityType) {
                        rootTasks.add(task);
                    }
                }
            } else if (rootTask.getActivityType() == activityType) {
                rootTasks.add(rootTask);
            }
        }
    }

    com.android.server.wm.ActivityRecord topRunningActivity() {
        return topRunningActivity(false);
    }

    com.android.server.wm.ActivityRecord topRunningActivity(final boolean considerKeyguardState) {
        return (com.android.server.wm.ActivityRecord) getItemFromTaskDisplayAreas(new java.util.function.Function() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda17
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.wm.TaskDisplayArea) obj).topRunningActivity(considerKeyguardState);
            }
        });
    }

    boolean updateDisplayOverrideConfigurationLocked() {
        com.android.server.wm.RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
        if (recentsAnimationController != null && this.mDisplayContentExt.shouldCancelRecentAnimation(this)) {
            recentsAnimationController.cancelAnimationForDisplayChange();
        }
        android.content.res.Configuration values = new android.content.res.Configuration();
        computeScreenConfiguration(values);
        this.mAtmService.mH.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.wm.ActivityTaskManagerService$$ExternalSyntheticLambda24(), this.mAtmService.mAmInternal, java.lang.Integer.valueOf(this.mDisplayId)));
        android.provider.Settings.System.clearConfiguration(values);
        updateDisplayOverrideConfigurationLocked(values, null, false);
        return this.mAtmService.mTmpUpdateConfigurationResult.changes != 0;
    }

    boolean updateDisplayOverrideConfigurationLocked(android.content.res.Configuration values, com.android.server.wm.ActivityRecord starting, boolean deferResume) {
        int changes = 0;
        boolean kept = true;
        this.mAtmService.deferWindowLayout();
        if (values != null) {
            try {
                if (this.mDisplayId == 0) {
                    changes = this.mAtmService.updateGlobalConfigurationLocked(values, false, false, -10000);
                } else {
                    changes = performDisplayOverrideConfigUpdate(values);
                }
                this.mAtmService.mTmpUpdateConfigurationResult.changes = changes;
                this.mAtmService.mTmpUpdateConfigurationResult.mIsUpdating = true;
            } catch (java.lang.Throwable th) {
                this.mAtmService.mTmpUpdateConfigurationResult.mIsUpdating = false;
                this.mAtmService.continueWindowLayout();
                throw th;
            }
        }
        if (!deferResume) {
            kept = this.mAtmService.ensureConfigAndVisibilityAfterUpdate(starting, changes);
        }
        this.mAtmService.mTmpUpdateConfigurationResult.mIsUpdating = false;
        this.mAtmService.continueWindowLayout();
        this.mAtmService.mTmpUpdateConfigurationResult.activityRelaunched = kept ? false : true;
        return kept;
    }

    int performDisplayOverrideConfigUpdate(android.content.res.Configuration values) {
        this.mTempConfig.setTo(getRequestedOverrideConfiguration());
        int changes = this.mTempConfig.updateFrom(values);
        if (changes != 0) {
            android.util.Slog.i(TAG, "Override config changes=" + java.lang.Integer.toHexString(changes) + " " + this.mTempConfig + " for displayId=" + this.mDisplayId);
            if (isReady() && this.mTransitionController.isShellTransitionsEnabled() && this.mLastHasContent) {
                com.android.server.wm.Transition transition = this.mTransitionController.getCollectingTransition();
                if (transition != null) {
                    collectDisplayChange(transition);
                } else {
                    requestChangeTransition(changes, null);
                }
            }
            onRequestedOverrideConfigurationChanged(this.mTempConfig);
            boolean isDensityChange = (changes & 4096) != 0;
            if (isDensityChange && this.mDisplayId == 0) {
                this.mAtmService.mAppWarnings.onDensityChanged();
                android.os.Message msg = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda12
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((android.app.ActivityManagerInternal) obj).killAllBackgroundProcessesExcept(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue());
                    }
                }, this.mAtmService.mAmInternal, 24, 6);
                this.mAtmService.mH.sendMessage(msg);
            }
            this.mWmService.mDisplayNotificationController.dispatchDisplayChanged(this, getConfiguration());
        }
        return changes;
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onRequestedOverrideConfigurationChanged(android.content.res.Configuration overrideConfiguration) {
        android.content.res.Configuration currOverrideConfig = getRequestedOverrideConfiguration();
        int currRotation = currOverrideConfig.windowConfiguration.getRotation();
        int overrideRotation = overrideConfiguration.windowConfiguration.getRotation();
        if (currRotation != -1 && overrideRotation != -1 && currRotation != overrideRotation) {
            applyRotationAndFinishFixedRotation(currRotation, overrideRotation);
        }
        this.mCurrentOverrideConfigurationChanges = currOverrideConfig.diff(overrideConfiguration);
        this.mNonStaticExt.updateRequestedOverrideConfiguration(overrideConfiguration);
        super.onRequestedOverrideConfigurationChanged(overrideConfiguration);
        this.mCurrentOverrideConfigurationChanges = 0;
        if (this.mWaitingForConfig) {
            this.mWaitingForConfig = false;
            this.mWmService.mLastFinishedFreezeSource = "new-config";
        }
        this.mAtmService.addWindowLayoutReasons(1);
    }

    @Override // com.android.server.wm.WindowContainer
    void onResize() {
        super.onResize();
        if (this.mWmService.mAccessibilityController.hasCallbacks()) {
            this.mWmService.mAccessibilityController.onDisplaySizeChanged(this);
        }
    }

    private void applyRotationAndFinishFixedRotation(final int oldRotation, final int newRotation) {
        com.android.server.wm.WindowToken rotatedLaunchingApp = this.mFixedRotationLaunchingApp;
        if (rotatedLaunchingApp == null) {
            lambda$applyRotationAndFinishFixedRotation$42(oldRotation, newRotation);
        } else {
            rotatedLaunchingApp.finishFixedRotationTransform(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$applyRotationAndFinishFixedRotation$42(oldRotation, newRotation);
                }
            });
            setFixedRotationLaunchingAppUnchecked(null);
        }
    }

    void handleActivitySizeCompatModeIfNeeded(com.android.server.wm.ActivityRecord r) {
        com.android.server.wm.Task organizedTask = r.getOrganizedTask();
        if (organizedTask == null) {
            this.mActiveSizeCompatActivities.remove(r);
            return;
        }
        if (r.isState(com.android.server.wm.ActivityRecord.State.RESUMED) && (r.inSizeCompatMode() || r.getWrapper().getExtImpl().inOplusCompatMode())) {
            if (this.mActiveSizeCompatActivities.add(r)) {
                organizedTask.onSizeCompatActivityChanged();
            }
        } else if (this.mActiveSizeCompatActivities.remove(r)) {
            organizedTask.onSizeCompatActivityChanged();
        }
    }

    boolean isUidPresent(int uid) {
        java.util.function.Predicate<com.android.server.wm.ActivityRecord> predicateObtainPredicate = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new com.android.server.wm.DisplayContent$$ExternalSyntheticLambda11(), com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.ActivityRecord.class), java.lang.Integer.valueOf(uid));
        boolean isUidPresent = this.mDisplayContent.getActivity(predicateObtainPredicate) != null;
        predicateObtainPredicate.recycle();
        return isUidPresent;
    }

    boolean isRemoved() {
        return this.mRemoved;
    }

    boolean isRemoving() {
        return this.mRemoving;
    }

    void remove() {
        this.mRemoving = true;
        this.mRootWindowContainer.mTaskSupervisor.beginDeferResume();
        try {
            com.android.server.wm.Task lastReparentedRootTask = (com.android.server.wm.Task) reduceOnAllTaskDisplayAreas(new java.util.function.BiFunction() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda6
                @Override // java.util.function.BiFunction
                public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.wm.DisplayContent.lambda$remove$43((com.android.server.wm.TaskDisplayArea) obj, (com.android.server.wm.Task) obj2);
                }
            }, null, false);
            this.mRootWindowContainer.mTaskSupervisor.endDeferResume();
            this.mRemoved = true;
            if (this.mContentRecorder != null) {
                this.mContentRecorder.stopRecording();
            }
            if (lastReparentedRootTask != null) {
                lastReparentedRootTask.resumeNextFocusAfterReparent();
            }
            releaseSelfIfNeeded();
            this.mDisplayPolicy.release();
            if (!this.mAllSleepTokens.isEmpty()) {
                this.mAllSleepTokens.forEach(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda7
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$remove$44((com.android.server.wm.RootWindowContainer.SleepToken) obj);
                    }
                });
                this.mAllSleepTokens.clear();
                this.mAtmService.updateSleepIfNeededLocked();
            }
        } catch (java.lang.Throwable th) {
            this.mRootWindowContainer.mTaskSupervisor.endDeferResume();
            throw th;
        }
    }

    static /* synthetic */ com.android.server.wm.Task lambda$remove$43(com.android.server.wm.TaskDisplayArea taskDisplayArea, com.android.server.wm.Task rootTask) {
        com.android.server.wm.Task lastReparentedRootTaskFromArea = taskDisplayArea.remove();
        if (lastReparentedRootTaskFromArea != null) {
            return lastReparentedRootTaskFromArea;
        }
        return rootTask;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$remove$44(com.android.server.wm.RootWindowContainer.SleepToken token) {
        this.mRootWindowContainer.mSleepTokens.remove(token.mHashKey);
    }

    void releaseSelfIfNeeded() {
        if (!this.mRemoved) {
            return;
        }
        boolean hasNonEmptyHomeRootTask = forAllRootTasks(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda21
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.DisplayContent.lambda$releaseSelfIfNeeded$45((com.android.server.wm.Task) obj);
            }
        });
        if (!hasNonEmptyHomeRootTask && getRootTaskCount() > 0) {
            forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda22
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wm.Task) obj).removeIfPossible("releaseSelfIfNeeded");
                }
            });
        } else if (getTopRootTask() == null) {
            removeIfPossible();
        }
    }

    static /* synthetic */ boolean lambda$releaseSelfIfNeeded$45(com.android.server.wm.Task rootTask) {
        return !rootTask.isActivityTypeHome() || rootTask.hasChild();
    }

    android.util.IntArray getPresentUIDs() {
        this.mDisplayAccessUIDs.clear();
        this.mDisplayContent.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda31
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$getPresentUIDs$47((com.android.server.wm.ActivityRecord) obj);
            }
        });
        return this.mDisplayAccessUIDs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getPresentUIDs$47(com.android.server.wm.ActivityRecord r) {
        this.mDisplayAccessUIDs.add(r.getUid());
    }

    boolean shouldDestroyContentOnRemove() {
        return this.mDisplay.getRemoveMode() == 1;
    }

    boolean shouldSleep() {
        return (getRootTaskCount() == 0 || !this.mAllSleepTokens.isEmpty()) && this.mAtmService.mRunningVoice == null && this.mDisplayContentExt.onGoingToSleep(getDisplayId());
    }

    void ensureActivitiesVisible(final com.android.server.wm.ActivityRecord starting, final boolean notifyClients) {
        if (this.mInEnsureActivitiesVisible) {
            return;
        }
        this.mAtmService.mTaskSupervisor.beginActivityVisibilityUpdate();
        try {
            this.mInEnsureActivitiesVisible = true;
            forAllRootTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda29
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wm.Task) obj).ensureActivitiesVisible(starting, notifyClients);
                }
            });
            if (this.mTransitionController.useShellTransitionsRotation() && this.mTransitionController.isCollecting() && this.mWallpaperController.getWallpaperTarget() != null) {
                this.mWallpaperController.adjustWallpaperWindows();
            }
        } finally {
            this.mAtmService.mTaskSupervisor.endActivityVisibilityUpdate();
            this.mInEnsureActivitiesVisible = false;
        }
    }

    boolean isSleeping() {
        return this.mSleeping;
    }

    void setIsSleeping(boolean asleep) {
        android.util.Slog.d(TAG, " setIsSleeping from  " + this.mSleeping + " to " + asleep + " this=" + this);
        this.mSleeping = asleep;
    }

    void notifyKeyguardFlagsChanged() {
        if (!isKeyguardLocked()) {
            return;
        }
        boolean wasTransitionSet = this.mAppTransition.isTransitionSet();
        if (!wasTransitionSet) {
            prepareAppTransition(0);
        }
        this.mRootWindowContainer.ensureActivitiesVisible();
        if (!wasTransitionSet) {
            executeAppTransition();
        }
    }

    boolean canShowWithInsecureKeyguard() {
        int flags = this.mDisplay.getFlags();
        return (flags & 32) != 0;
    }

    boolean isKeyguardLocked() {
        return this.mRootWindowContainer.mTaskSupervisor.getKeyguardController().isKeyguardLocked(this.mDisplayId);
    }

    boolean isKeyguardGoingAway() {
        return this.mRootWindowContainer.mTaskSupervisor.getKeyguardController().isKeyguardGoingAway(this.mDisplayId);
    }

    boolean isKeyguardAlwaysUnlocked() {
        return (this.mDisplayInfo.flags & 512) != 0;
    }

    boolean shouldRotateWithContent() {
        return (this.mDisplayInfo.flags & 16384) != 0;
    }

    boolean hasOwnFocus() {
        return this.mWmService.mPerDisplayFocusEnabled || (this.mDisplayInfo.flags & 2048) != 0;
    }

    boolean isKeyguardOccluded() {
        return this.mRootWindowContainer.mTaskSupervisor.getKeyguardController().isKeyguardOccluded(this.mDisplayId);
    }

    com.android.server.wm.Task getTaskOccludingKeyguard() {
        com.android.server.wm.KeyguardController keyguardController = this.mRootWindowContainer.mTaskSupervisor.getKeyguardController();
        if (keyguardController.getTopOccludingActivity(this.mDisplayId) != null) {
            return keyguardController.getTopOccludingActivity(this.mDisplayId).getRootTask();
        }
        if (keyguardController.getDismissKeyguardActivity(this.mDisplayId) != null) {
            return keyguardController.getDismissKeyguardActivity(this.mDisplayId).getRootTask();
        }
        return null;
    }

    void removeAllTasks() {
        forAllTasks(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayContent$$ExternalSyntheticLambda30
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.Task task = (com.android.server.wm.Task) obj;
                task.getRootTask().removeChild(task, "removeAllTasks");
            }
        });
    }

    android.content.Context getDisplayUiContext() {
        return this.mDisplayPolicy.getSystemUiContext();
    }

    @Override // com.android.server.wm.DisplayArea
    boolean setIgnoreOrientationRequest(boolean ignoreOrientationRequest) {
        if (this.mSetIgnoreOrientationRequest == ignoreOrientationRequest) {
            return false;
        }
        boolean rotationChanged = super.setIgnoreOrientationRequest(ignoreOrientationRequest);
        this.mWmService.mDisplayWindowSettings.setIgnoreOrientationRequest(this, this.mSetIgnoreOrientationRequest);
        return rotationChanged;
    }

    void onIsIgnoreOrientationRequestDisabledChanged() {
        if (this.mFocusedApp != null) {
            onLastFocusedTaskDisplayAreaChanged(this.mFocusedApp.getDisplayArea());
        }
        if (this.mSetIgnoreOrientationRequest) {
            updateOrientation();
        }
    }

    com.android.server.wm.WindowState findScrollCaptureTargetWindow(final com.android.server.wm.WindowState searchBehind, final int taskId) {
        return getWindow(new java.util.function.Predicate<com.android.server.wm.WindowState>() { // from class: com.android.server.wm.DisplayContent.4
            boolean behindTopWindow;

            {
                this.behindTopWindow = searchBehind == null;
            }

            @Override // java.util.function.Predicate
            public boolean test(com.android.server.wm.WindowState nextWindow) {
                if (!this.behindTopWindow) {
                    if (nextWindow == searchBehind) {
                        this.behindTopWindow = true;
                    }
                    return false;
                }
                if (taskId == -1) {
                    if (!nextWindow.canReceiveKeys()) {
                        return false;
                    }
                } else {
                    com.android.server.wm.Task task = nextWindow.getTask();
                    if (task == null || !task.isTaskId(taskId)) {
                        return false;
                    }
                }
                return !nextWindow.isSecureLocked();
            }
        });
    }

    @Override // com.android.server.wm.DisplayArea, com.android.server.wm.ConfigurationContainer
    public boolean providesMaxBounds() {
        return true;
    }

    void setSandboxDisplayApis(boolean sandboxDisplayApis) {
        this.mSandboxDisplayApis = sandboxDisplayApis;
    }

    boolean sandboxDisplayApis() {
        return this.mSandboxDisplayApis;
    }

    private com.android.server.wm.ContentRecorder getContentRecorder() {
        if (this.mContentRecorder == null) {
            this.mContentRecorder = new com.android.server.wm.ContentRecorder(this);
        }
        return this.mContentRecorder;
    }

    void onMirrorOutputSurfaceOrientationChanged() {
        if (this.mContentRecorder != null) {
            this.mContentRecorder.onMirrorOutputSurfaceOrientationChanged();
        }
    }

    void pauseRecording() {
        if (this.mContentRecorder != null) {
            this.mContentRecorder.pauseRecording();
        }
    }

    void setContentRecordingSession(android.view.ContentRecordingSession session) {
        getContentRecorder().setContentRecordingSession(session);
    }

    boolean setDisplayMirroring() {
        int mirrorDisplayId = this.mWmService.mDisplayManagerInternal.getDisplayIdToMirror(this.mDisplayId);
        if (mirrorDisplayId == -1) {
            return false;
        }
        if (mirrorDisplayId == this.mDisplayId) {
            if (this.mDisplayId != 0 && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[3]) {
                long protoLogParam0 = mirrorDisplayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 4162342172327950908L, 1, "Content Recording: Attempting to mirror self on %d", java.lang.Long.valueOf(protoLogParam0));
            }
            return false;
        }
        com.android.server.wm.DisplayContent mirrorDc = this.mRootWindowContainer.getDisplayContentOrCreate(mirrorDisplayId);
        if (mirrorDc == null && this.mDisplayId == 0) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[3]) {
                long protoLogParam02 = mirrorDisplayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 5489691866309868814L, 1, "Content Recording: Found no matching mirror display for id=%d for DEFAULT_DISPLAY. Nothing to mirror.", java.lang.Long.valueOf(protoLogParam02));
            }
            return false;
        }
        if (mirrorDc == null) {
            mirrorDc = this.mRootWindowContainer.getDefaultDisplay();
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[3]) {
                long protoLogParam03 = mirrorDisplayId;
                long protoLogParam1 = this.mDisplayId;
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, -39794010824230928L, 5, "Content Recording: Attempting to mirror %d from %d but no DisplayContent associated. Changing to mirror default display.", java.lang.Long.valueOf(protoLogParam03), java.lang.Long.valueOf(protoLogParam1));
            }
        }
        if (this.mDisplayContentExt.isFoldExternalScreen(this.mRootWindowContainer, this.mDisplayId)) {
            return false;
        }
        android.view.ContentRecordingSession session = android.view.ContentRecordingSession.createDisplaySession(mirrorDc.getDisplayId()).setVirtualDisplayId(this.mDisplayId);
        setContentRecordingSession(session);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_CONTENT_RECORDING_enabled[1]) {
            long protoLogParam04 = this.mDisplayId;
            long protoLogParam12 = mirrorDisplayId;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_CONTENT_RECORDING, 6545352723229848841L, 5, "Content Recording: Successfully created a ContentRecordingSession for displayId=%d to mirror content from displayId=%d", java.lang.Long.valueOf(protoLogParam04), java.lang.Long.valueOf(protoLogParam12));
        }
        return true;
    }

    void updateRecording() {
        if ((this.mContentRecorder == null || !this.mContentRecorder.isContentRecordingSessionSet()) && !setDisplayMirroring()) {
            return;
        }
        this.mContentRecorder.updateRecording();
    }

    boolean isCurrentlyRecording() {
        return this.mContentRecorder != null && this.mContentRecorder.isCurrentlyRecording();
    }

    class FixedRotationTransitionListener extends com.android.server.wm.WindowManagerInternal.AppTransitionListener {
        private com.android.server.wm.ActivityRecord mAnimatingRecents;
        private boolean mRecentsWillBeTop;

        FixedRotationTransitionListener() {
        }

        void onStartRecentsAnimation(com.android.server.wm.ActivityRecord r) {
            this.mAnimatingRecents = r;
            if (r.isVisible() && com.android.server.wm.DisplayContent.this.mFocusedApp != null && !com.android.server.wm.DisplayContent.this.mFocusedApp.occludesParent()) {
                android.util.Slog.d(com.android.server.wm.DisplayContent.TAG, "onStartRecentsAnimation: focused app( " + com.android.server.wm.DisplayContent.this.mFocusedApp + " ) not occludes parent.");
                return;
            }
            com.android.server.wm.DisplayContent.this.rotateInDifferentOrientationIfNeeded(r);
            if (r.hasFixedRotationTransform()) {
                com.android.server.wm.DisplayContent.this.setFixedRotationLaunchingApp(r, r.getWindowConfiguration().getRotation());
            }
        }

        void onFinishRecentsAnimation() {
            com.android.server.wm.ActivityRecord animatingRecents = this.mAnimatingRecents;
            boolean recentsWillBeTop = this.mRecentsWillBeTop;
            this.mAnimatingRecents = null;
            this.mRecentsWillBeTop = false;
            if (recentsWillBeTop) {
                return;
            }
            if (animatingRecents != null && animatingRecents == com.android.server.wm.DisplayContent.this.mFixedRotationLaunchingApp && animatingRecents.isVisible() && animatingRecents != com.android.server.wm.DisplayContent.this.topRunningActivity()) {
                com.android.server.wm.DisplayContent.this.setFixedRotationLaunchingAppUnchecked(null);
            } else {
                com.android.server.wm.DisplayContent.this.continueUpdateOrientationForDiffOrienLaunchingApp();
            }
        }

        void notifyRecentsWillBeTop() {
            this.mRecentsWillBeTop = true;
        }

        boolean shouldDeferRotation() {
            com.android.server.wm.ActivityRecord source = null;
            if (com.android.server.wm.DisplayContent.this.mTransitionController.isShellTransitionsEnabled()) {
                if (com.android.server.wm.DisplayContent.this.hasFixedRotationTransientLaunch()) {
                    source = com.android.server.wm.DisplayContent.this.mFixedRotationLaunchingApp;
                }
            } else if (this.mAnimatingRecents != null && !com.android.server.wm.DisplayContent.this.hasTopFixedRotationLaunchingApp()) {
                source = this.mAnimatingRecents;
            }
            if (source == null || source.getRequestedConfigurationOrientation(true) == 0) {
                return false;
            }
            return com.android.server.wm.DisplayContent.this.mWmService.mPolicy.okToAnimate(false);
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionFinishedLocked(android.os.IBinder token) {
            com.android.server.wm.ActivityRecord r = com.android.server.wm.DisplayContent.this.getActivityRecord(token);
            if (r == null || r == this.mAnimatingRecents || r.getDisplayId() != com.android.server.wm.DisplayContent.this.mDisplayId) {
                return;
            }
            if (this.mAnimatingRecents != null && this.mRecentsWillBeTop && (r.getWindowingMode() != 100 || com.android.server.wm.DisplayContent.this.mFixedRotationLaunchingApp == null || !com.android.server.wm.DisplayContent.this.mFixedRotationLaunchingApp.hasFixedRotationTransform(r))) {
                return;
            }
            if (com.android.server.wm.DisplayContent.this.mFixedRotationLaunchingApp == null) {
                r.finishFixedRotationTransform();
                return;
            }
            if (com.android.server.wm.DisplayContent.this.mFixedRotationLaunchingApp.hasFixedRotationTransform(r)) {
                if (com.android.server.wm.DisplayContent.this.mFixedRotationLaunchingApp.hasAnimatingFixedRotationTransition() || com.android.server.wm.DisplayContent.this.mDisplayContentExt.isToHomeAnimationPlaying(r.mTransitionController)) {
                    android.util.Slog.d(com.android.server.wm.DisplayContent.TAG, "onAppTransitionFinishedLocked return for fixed rotation app in transition " + com.android.server.wm.DisplayContent.this.mFixedRotationLaunchingApp);
                    com.android.server.wm.DisplayContent.this.mDisplayContentExt.setIsFixedRotationBlocked(true);
                    return;
                }
            } else {
                com.android.server.wm.Task task = r.getTask();
                if (task == null || task != com.android.server.wm.DisplayContent.this.mFixedRotationLaunchingApp.getTask() || task.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayContent$FixedRotationTransitionListener$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return ((com.android.server.wm.ActivityRecord) obj).isInTransition();
                    }
                }) != null) {
                    return;
                }
            }
            com.android.server.wm.DisplayContent.this.continueUpdateOrientationForDiffOrienLaunchingApp();
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionCancelledLocked(boolean keyguardGoingAwayCancelled) {
            if (com.android.server.wm.DisplayContent.this.mTransitionController.isShellTransitionsEnabled()) {
                return;
            }
            com.android.server.wm.DisplayContent.this.continueUpdateOrientationForDiffOrienLaunchingApp();
        }

        @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
        public void onAppTransitionTimeoutLocked() {
            com.android.server.wm.DisplayContent.this.continueUpdateOrientationForDiffOrienLaunchingApp();
        }
    }

    class RemoteInsetsControlTarget implements com.android.server.wm.InsetsControlTarget {
        private final boolean mCanShowTransient;
        private final android.view.IDisplayWindowInsetsController mRemoteInsetsController;
        private int mRequestedVisibleTypes = android.view.WindowInsets.Type.defaultVisible();

        RemoteInsetsControlTarget(android.view.IDisplayWindowInsetsController controller) {
            this.mRemoteInsetsController = controller;
            this.mCanShowTransient = com.android.server.wm.DisplayContent.this.mWmService.mContext.getResources().getBoolean(android.R.bool.config_omnipresentCommunalUser);
        }

        void topFocusedWindowChanged(android.content.ComponentName component, int requestedVisibleTypes) {
            try {
                this.mRemoteInsetsController.topFocusedWindowChanged(component, requestedVisibleTypes);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.wm.DisplayContent.TAG, "Failed to deliver package in top focused window change", e);
            }
        }

        void notifyInsetsChanged() {
            try {
                this.mRemoteInsetsController.insetsChanged(com.android.server.wm.DisplayContent.this.getInsetsStateController().getRawInsetsState());
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.wm.DisplayContent.TAG, "Failed to deliver inset state change", e);
            }
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public void notifyInsetsControlChanged(int displayId) {
            com.android.server.wm.InsetsStateController stateController = com.android.server.wm.DisplayContent.this.getInsetsStateController();
            try {
                this.mRemoteInsetsController.insetsControlChanged(stateController.getRawInsetsState(), stateController.getControlsForDispatch(this));
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.wm.DisplayContent.TAG, "Failed to deliver inset control state change", e);
            }
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public void showInsets(int types, boolean fromIme, android.view.inputmethod.ImeTracker.Token statsToken) {
            try {
                android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 23);
                this.mRemoteInsetsController.showInsets(types, fromIme, statsToken);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.wm.DisplayContent.TAG, "Failed to deliver showInsets", e);
                android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 23);
            }
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public void hideInsets(int types, boolean fromIme, android.view.inputmethod.ImeTracker.Token statsToken) {
            try {
                android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 24);
                this.mRemoteInsetsController.hideInsets(types, fromIme, statsToken);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.wm.DisplayContent.TAG, "Failed to deliver hideInsets", e);
                android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 24);
            }
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public boolean canShowTransient() {
            return this.mCanShowTransient;
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public boolean isRequestedVisible(int types) {
            return android.view.inputmethod.Flags.refactorInsetsController() ? (this.mRequestedVisibleTypes & types) != 0 : ((android.view.WindowInsets.Type.ime() & types) != 0 && com.android.server.wm.DisplayContent.this.getInsetsStateController().getImeSourceProvider().isImeShowing()) || (this.mRequestedVisibleTypes & types) != 0;
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public int getRequestedVisibleTypes() {
            return this.mRequestedVisibleTypes;
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public void setImeInputTargetRequestedVisibility(boolean visible) {
            if (android.view.inputmethod.Flags.refactorInsetsController()) {
                try {
                    this.mRemoteInsetsController.setImeInputTargetRequestedVisibility(visible);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.wm.DisplayContent.TAG, "Failed to deliver setImeInputTargetRequestedVisibility", e);
                }
            }
        }

        void setRequestedVisibleTypes(int requestedVisibleTypes) {
            if (this.mRequestedVisibleTypes != requestedVisibleTypes) {
                this.mRequestedVisibleTypes = requestedVisibleTypes;
            }
        }
    }

    android.view.MagnificationSpec getMagnificationSpec() {
        return this.mMagnificationSpec;
    }

    com.android.server.wm.DisplayArea findAreaForWindowType(int windowType, android.os.Bundle options, boolean ownerCanManageAppToken, boolean roundedCornerOverlay) {
        if (windowType >= 1 && windowType <= 99) {
            return this.mDisplayAreaPolicy.getTaskDisplayArea(options);
        }
        if (windowType == 2011 || windowType == 2012) {
            return getImeContainer();
        }
        return this.mDisplayAreaPolicy.findAreaForWindowType(windowType, options, ownerCanManageAppToken, roundedCornerOverlay);
    }

    com.android.server.wm.DisplayArea findAreaForToken(com.android.server.wm.WindowToken windowToken) {
        return findAreaForWindowType(windowToken.getWindowType(), windowToken.mOptions, windowToken.mOwnerCanManageAppTokens, windowToken.mRoundedCornerOverlay);
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.DisplayContent asDisplayContent() {
        return this;
    }

    @Override // com.android.server.wm.WindowContainer
    int getRelativeDisplayRotation() {
        return 0;
    }

    public void replaceContent(android.view.SurfaceControl sc) {
        new android.view.SurfaceControl.Transaction().reparent(sc, getSurfaceControl()).reparent(this.mOverlayLayer, null).reparent(this.mInputOverlayLayer, null).reparent(this.mA11yOverlayLayer, null).apply();
    }

    public com.android.server.wm.IDisplayContentWrapper getWrapper() {
        return this.mDisplayContentWrapper;
    }

    private class DisplayContentWrapper implements com.android.server.wm.IDisplayContentWrapper {
        private DisplayContentWrapper() {
        }

        @Override // com.android.server.wm.IDisplayContentWrapper
        public com.android.server.wm.IDisplayContentExt getExtImpl() {
            return com.android.server.wm.DisplayContent.this.mDisplayContentExt;
        }

        @Override // com.android.server.wm.IDisplayContentWrapper
        public com.android.server.wm.INonStaticDisplayContentExt getNonStaticExtImpl() {
            return com.android.server.wm.DisplayContent.this.mNonStaticExt;
        }

        @Override // com.android.server.wm.IDisplayContentWrapper
        public com.android.server.wm.ActivityRecord getFixedRotationLaunchingApp() {
            return com.android.server.wm.DisplayContent.this.mFixedRotationLaunchingApp;
        }

        @Override // com.android.server.wm.IDisplayContentWrapper
        public void startAsyncRotationIfNeeded() {
            com.android.server.wm.DisplayContent.this.startAsyncRotationIfNeeded();
        }

        @Override // com.android.server.wm.IDisplayContentWrapper
        public java.lang.Runnable getAsyncRotationStartRunnable() {
            return com.android.server.wm.DisplayContent.this.mAsyncRotationRunnable;
        }
    }
}
