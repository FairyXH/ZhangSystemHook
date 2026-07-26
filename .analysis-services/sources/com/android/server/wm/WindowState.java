package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class WindowState extends com.android.server.wm.WindowContainer<com.android.server.wm.WindowState> implements com.android.server.policy.WindowManagerPolicy.WindowState, com.android.server.wm.InsetsControlTarget, com.android.server.wm.InputTarget {
    static final int BLAST_TIMEOUT_DURATION = 5000;
    private static final int DYNAMIC_FRAME_RATE_DIALOG_TYPE_ID = 20102;
    static final int EXCLUSION_LEFT = 0;
    static final int EXCLUSION_RIGHT = 1;
    static final int EXIT_ANIMATING_TYPES = 25;
    static final int LEGACY_POLICY_VISIBILITY = 1;
    static final int MINIMUM_VISIBLE_HEIGHT_IN_DP = 32;
    static final int MINIMUM_VISIBLE_WIDTH_IN_DP = 48;
    private static final int POLICY_VISIBILITY_ALL = 3;
    static final int RESIZE_HANDLE_WIDTH_IN_DP = 30;
    static final java.lang.String TAG = "WindowManager";
    private static final int VISIBLE_FOR_USER = 2;
    final android.view.InsetsState mAboveInsetsState;
    com.android.server.wm.ActivityRecord mActivityRecord;
    boolean mAnimatingExit;
    boolean mAppFreezing;
    final int mAppOp;
    private boolean mAppOpVisibility;
    final android.view.WindowManager.LayoutParams mAttrs;
    final int mBaseLayer;
    final android.view.IWindow mClient;
    float mCompatScale;
    final android.content.Context mContext;
    private long mCreateTime;
    boolean mDestroying;
    int mDisableFlags;
    private boolean mDragResizing;
    private boolean mDragResizingChangeReported;
    private final java.util.List<com.android.server.wm.WindowState.DrawHandler> mDrawHandlers;
    private android.os.PowerManager.WakeLock mDrawLock;
    private boolean mDrawnStateEvaluated;
    private final java.util.List<android.graphics.Rect> mExclusionRects;
    private android.os.RemoteCallbackList<android.view.IWindowFocusObserver> mFocusCallbacks;
    private boolean mForceHideNonSystemOverlayWindow;
    final boolean mForceSeamlesslyRotate;
    int mFrameRateSelectionPriority;
    com.android.server.wm.RefreshRatePolicy.FrameRateVote mFrameRateVote;
    private android.view.InsetsState mFrozenInsetsState;
    final android.graphics.Rect mGivenContentInsets;
    boolean mGivenInsetsPending;
    final android.graphics.Region mGivenTouchableRegion;
    final android.graphics.Rect mGivenVisibleInsets;
    float mGlobalScale;
    float mHScale;
    boolean mHasSurface;
    boolean mHaveFrame;
    boolean mHidden;
    private boolean mHiddenWhileSuspended;
    boolean mImeInsetsConsumed;
    boolean mInRelayout;
    android.view.InputChannel mInputChannel;
    android.os.IBinder mInputChannelToken;
    final com.android.server.wm.InputWindowHandleWrapper mInputWindowHandle;
    float mInvGlobalScale;
    private boolean mIsChildWindow;
    private boolean mIsDimming;
    private final boolean mIsFloatingLayer;
    final boolean mIsImWindow;
    boolean mIsSurfacePositionPaused;
    final boolean mIsWallpaper;
    private final java.util.List<android.graphics.Rect> mKeepClearAreas;
    private com.android.internal.policy.KeyInterceptionInfo mKeyInterceptionInfo;
    private boolean mLastConfigReportedToClient;
    private final long[] mLastExclusionLogUptimeMillis;
    int mLastFreezeDuration;
    private final int[] mLastGrantedExclusionHeight;
    float mLastHScale;
    private final android.view.InsetsSourceControl.Array mLastReportedActiveControls;
    final android.window.ActivityWindowInfo mLastReportedActivityWindowInfo;
    private final android.util.MergedConfiguration mLastReportedConfiguration;
    private final android.window.ClientWindowFrames mLastReportedFrames;
    private final android.view.InsetsState mLastReportedInsetsState;
    private final int[] mLastRequestedExclusionHeight;
    private int mLastRequestedHeight;
    private int mLastRequestedWidth;
    private boolean mLastShownChangedReported;
    final android.graphics.Rect mLastSurfaceInsets;
    private java.lang.CharSequence mLastTitle;
    float mLastVScale;
    int mLastVisibleLayoutRotation;
    int mLayer;
    final boolean mLayoutAttached;
    boolean mLayoutNeeded;
    int mLayoutSeq;
    boolean mLegacyPolicyVisibilityAfterAnim;
    android.util.SparseArray<android.view.InsetsSource> mMergedLocalInsetsSources;
    private boolean mMovedByResize;
    boolean mObscured;
    private android.window.OnBackInvokedCallbackInfo mOnBackInvokedCallbackInfo;
    private long mOrientationChangeRedrawRequestTime;
    private boolean mOrientationChangeTimedOut;
    private boolean mOrientationChanging;
    final float mOverrideScale;
    final boolean mOwnerCanAddInternalSystemWindow;
    final int mOwnerUid;
    com.android.server.wm.SeamlessRotator mPendingSeamlessRotate;
    boolean mPermanentlyHidden;
    final com.android.server.policy.WindowManagerPolicy mPolicy;
    public int mPolicyVisibility;
    int mPrepareSyncSeqId;
    private boolean mRedrawForSyncReported;
    boolean mRelayoutCalled;
    int mRelayoutSeq;
    boolean mRemoveOnExit;
    boolean mRemoved;
    int mRequestedHeight;
    private int mRequestedVisibleTypes;
    int mRequestedWidth;
    private final java.util.function.Consumer<android.view.SurfaceControl.Transaction> mSeamlessRotationFinishedConsumer;
    boolean mSeamlesslyRotated;
    final com.android.server.wm.Session mSession;
    private final java.util.function.Consumer<android.view.SurfaceControl.Transaction> mSetSurfacePositionConsumer;
    boolean mShouldScaleWallpaper;
    final int mShowUserId;
    com.android.server.wm.StartingData mStartingData;
    private java.lang.String mStringNameCache;
    final int mSubLayer;
    boolean mSurfacePlacementNeeded;
    final android.graphics.Point mSurfacePosition;
    private int mSurfaceTranslationY;
    int mSyncSeqId;
    private final android.graphics.Region mTapExcludeRegion;
    private final android.content.res.Configuration mTempConfiguration;
    final android.graphics.Matrix mTmpMatrix;
    final float[] mTmpMatrixArray;
    private final android.graphics.Point mTmpPoint;
    private final android.graphics.Rect mTmpRect;
    private final android.graphics.Region mTmpRegion;
    private final android.view.SurfaceControl.Transaction mTmpTransaction;
    com.android.server.wm.WindowToken mToken;
    int mTouchableInsets;
    private final java.util.List<android.graphics.Rect> mUnrestrictedKeepClearAreas;
    float mVScale;
    int mViewVisibility;
    int mWallpaperDisplayOffsetX;
    int mWallpaperDisplayOffsetY;
    float mWallpaperScale;
    float mWallpaperX;
    float mWallpaperXStep;
    float mWallpaperY;
    float mWallpaperYStep;
    float mWallpaperZoomOut;
    private boolean mWasExiting;
    final com.android.server.wm.WindowStateAnimator mWinAnimator;
    private final com.android.server.wm.WindowFrames mWindowFrames;
    final com.android.server.wm.WindowState.WindowId mWindowId;
    boolean mWindowRemovalAllowed;
    private com.android.server.wm.IWindowStateExt mWindowStateExt;
    private com.android.server.wm.WindowState.WindowStateWrapper mWindowStateWrapper;
    com.android.server.wm.IWindowManagerServiceExt mWmsExt;
    int mXOffset;
    int mYOffset;
    static boolean DEBUG_PANIC = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final java.lang.StringBuilder sTmpSB = new java.lang.StringBuilder();
    private static final java.util.Comparator<com.android.server.wm.WindowState> sWindowSubLayerComparator = new java.util.Comparator<com.android.server.wm.WindowState>() { // from class: com.android.server.wm.WindowState.1
        @Override // java.util.Comparator
        public int compare(com.android.server.wm.WindowState w1, com.android.server.wm.WindowState w2) {
            int layer1 = w1.mSubLayer;
            int layer2 = w2.mSubLayer;
            if (layer1 < layer2) {
                return -1;
            }
            if (layer1 == layer2 && layer2 < 0) {
                return -1;
            }
            return 1;
        }
    };

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

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public /* bridge */ /* synthetic */ void onRequestedOverrideConfigurationChanged(android.content.res.Configuration configuration) {
        super.onRequestedOverrideConfigurationChanged(configuration);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceFreezer.Freezable
    public /* bridge */ /* synthetic */ void onUnfrozen() {
        super.onUnfrozen();
    }

    class DrawHandler {
        java.util.function.Consumer<android.view.SurfaceControl.Transaction> mConsumer;
        int mSeqId;

        DrawHandler(int seqId, java.util.function.Consumer<android.view.SurfaceControl.Transaction> consumer) {
            this.mSeqId = seqId;
            this.mConsumer = consumer;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.view.SurfaceControl.Transaction t) {
        finishSeamlessRotation(t);
        updateSurfacePosition(t);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(android.view.SurfaceControl.Transaction t) {
        if (this.mSurfaceControl != null && this.mSurfaceControl.isValid() && !this.mSurfaceAnimator.hasLeash()) {
            t.setPosition(this.mSurfaceControl, this.mSurfacePosition.x, this.mSurfacePosition.y);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.WindowState asWindowState() {
        return this;
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public boolean isRequestedVisible(int types) {
        return (this.mRequestedVisibleTypes & types) != 0;
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public int getRequestedVisibleTypes() {
        return this.mRequestedVisibleTypes;
    }

    void setRequestedVisibleTypes(int requestedVisibleTypes) {
        if (this.mRequestedVisibleTypes != requestedVisibleTypes) {
            this.mRequestedVisibleTypes = requestedVisibleTypes;
            getWrapper().getExtImpl().setRequestedVisibleTypes(getDisplayContent());
        }
    }

    void setRequestedVisibleTypes(int requestedVisibleTypes, int mask) {
        setRequestedVisibleTypes((this.mRequestedVisibleTypes & (~mask)) | (requestedVisibleTypes & mask));
    }

    void freezeInsetsState() {
        if (this.mFrozenInsetsState == null) {
            this.mFrozenInsetsState = new android.view.InsetsState(getInsetsState(), true);
        }
    }

    void clearFrozenInsetsState() {
        this.mFrozenInsetsState = null;
    }

    android.view.InsetsState getFrozenInsetsState() {
        return this.mFrozenInsetsState;
    }

    boolean isReadyToDispatchInsetsState() {
        if (this.mStartingData != null) {
            return false;
        }
        boolean visible = shouldCheckTokenVisibleRequested() ? isVisibleRequested() : isVisible();
        return visible && this.mFrozenInsetsState == null;
    }

    void seamlesslyRotateIfAllowed(android.view.SurfaceControl.Transaction transaction, int oldRotation, int rotation, boolean requested) {
        if (!isVisibleNow()) {
            return;
        }
        if ((this.mIsWallpaper && !this.mWindowStateExt.wallpaperSeamlesslyRotate(this)) || this.mToken.hasFixedRotationTransform()) {
            return;
        }
        com.android.server.wm.Task task = getTask();
        if (task != null && task.inPinnedWindowingMode()) {
            return;
        }
        if (this.mPendingSeamlessRotate != null) {
            oldRotation = this.mPendingSeamlessRotate.getOldRotation();
        }
        if (this.mControllableInsetProvider != null && this.mControllableInsetProvider.getSource().getType() == android.view.WindowInsets.Type.ime()) {
            return;
        }
        if ((!this.mForceSeamlesslyRotate && !requested) || this.mWindowStateExt.blockSeamlesslyRotateForFingerPrintWindow(this)) {
            return;
        }
        if (this.mControllableInsetProvider != null) {
            this.mControllableInsetProvider.startSeamlessRotation();
        }
        this.mPendingSeamlessRotate = new com.android.server.wm.SeamlessRotator(oldRotation, rotation, getDisplayInfo(), false);
        this.mLastSurfacePosition.set(this.mSurfacePosition.x, this.mSurfacePosition.y);
        this.mPendingSeamlessRotate.unrotate(transaction, this);
        getDisplayContent().getDisplayRotation().markForSeamlessRotation(this, true);
        applyWithNextDraw(this.mSeamlessRotationFinishedConsumer);
    }

    void cancelSeamlessRotation() {
        finishSeamlessRotation(getPendingTransaction());
    }

    void finishSeamlessRotation(android.view.SurfaceControl.Transaction t) {
        if (this.mPendingSeamlessRotate == null) {
            return;
        }
        this.mPendingSeamlessRotate.finish(t, this);
        this.mPendingSeamlessRotate = null;
        getDisplayContent().getDisplayRotation().markForSeamlessRotation(this, false);
        if (this.mControllableInsetProvider != null) {
            this.mControllableInsetProvider.finishSeamlessRotation();
        }
        if (this.mLastHScale != 1.0f || this.mLastVScale != 1.0f) {
            this.mLastHScale = 1.0f;
            this.mLastVScale = 1.0f;
            android.util.Slog.d(TAG, "finishSeamlessRotation force update mLastHScale this=" + this);
        }
    }

    java.util.List<android.graphics.Rect> getSystemGestureExclusion() {
        return this.mExclusionRects;
    }

    boolean setSystemGestureExclusion(java.util.List<android.graphics.Rect> exclusionRects) {
        if (this.mExclusionRects.equals(exclusionRects)) {
            return false;
        }
        this.mExclusionRects.clear();
        this.mExclusionRects.addAll(exclusionRects);
        return true;
    }

    boolean isImplicitlyExcludingAllSystemGestures() {
        boolean stickyHideNav = this.mAttrs.insetsFlags.behavior == 2 && !isRequestedVisible(android.view.WindowInsets.Type.navigationBars());
        return stickyHideNav && this.mWmService.mConstants.mSystemGestureExcludedByPreQStickyImmersive && this.mActivityRecord != null && this.mActivityRecord.mTargetSdk < 29;
    }

    void setLastExclusionHeights(int side, int requested, int granted) {
        boolean changed = (this.mLastGrantedExclusionHeight[side] == granted && this.mLastRequestedExclusionHeight[side] == requested) ? false : true;
        if (changed) {
            if (this.mLastShownChangedReported) {
                logExclusionRestrictions(side);
            }
            this.mLastGrantedExclusionHeight[side] = granted;
            this.mLastRequestedExclusionHeight[side] = requested;
        }
    }

    void getKeepClearAreas(java.util.Collection<android.graphics.Rect> outRestricted, java.util.Collection<android.graphics.Rect> outUnrestricted) {
        android.graphics.Matrix tmpMatrix = new android.graphics.Matrix();
        float[] tmpFloat9 = new float[9];
        getKeepClearAreas(outRestricted, outUnrestricted, tmpMatrix, tmpFloat9);
    }

    void getKeepClearAreas(java.util.Collection<android.graphics.Rect> outRestricted, java.util.Collection<android.graphics.Rect> outUnrestricted, android.graphics.Matrix tmpMatrix, float[] float9) {
        outRestricted.addAll(getRectsInScreenSpace(this.mKeepClearAreas, tmpMatrix, float9));
        outUnrestricted.addAll(getRectsInScreenSpace(this.mUnrestrictedKeepClearAreas, tmpMatrix, float9));
    }

    java.util.List<android.graphics.Rect> getRectsInScreenSpace(java.util.List<android.graphics.Rect> rects, android.graphics.Matrix tmpMatrix, float[] float9) {
        getTransformationMatrix(float9, tmpMatrix);
        java.util.List<android.graphics.Rect> transformedRects = new java.util.ArrayList<>();
        android.graphics.RectF tmpRect = new android.graphics.RectF();
        for (android.graphics.Rect r : rects) {
            tmpRect.set(r);
            tmpMatrix.mapRect(tmpRect);
            android.graphics.Rect curr = new android.graphics.Rect();
            tmpRect.roundOut(curr);
            transformedRects.add(curr);
        }
        return transformedRects;
    }

    boolean setKeepClearAreas(java.util.List<android.graphics.Rect> restricted, java.util.List<android.graphics.Rect> unrestricted) {
        boolean newRestrictedAreas = !this.mKeepClearAreas.equals(restricted);
        boolean newUnrestrictedAreas = !this.mUnrestrictedKeepClearAreas.equals(unrestricted);
        if (!newRestrictedAreas && !newUnrestrictedAreas) {
            return false;
        }
        if (newRestrictedAreas) {
            this.mKeepClearAreas.clear();
            this.mKeepClearAreas.addAll(restricted);
        }
        if (newUnrestrictedAreas) {
            this.mUnrestrictedKeepClearAreas.clear();
            this.mUnrestrictedKeepClearAreas.addAll(unrestricted);
        }
        return true;
    }

    void setOnBackInvokedCallbackInfo(android.window.OnBackInvokedCallbackInfo callbackInfo) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_BACK_PREVIEW_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(callbackInfo);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_BACK_PREVIEW, -7237767461056267619L, 0, "%s: Setting back callback %s", protoLogParam0, protoLogParam1);
        }
        this.mOnBackInvokedCallbackInfo = callbackInfo;
    }

    android.window.OnBackInvokedCallbackInfo getOnBackInvokedCallbackInfo() {
        return this.mOnBackInvokedCallbackInfo;
    }

    /* JADX WARN: Multi-variable type inference failed */
    WindowState(com.android.server.wm.WindowManagerService windowManagerService, com.android.server.wm.Session session, android.view.IWindow iWindow, com.android.server.wm.WindowToken windowToken, com.android.server.wm.WindowState windowState, int i, android.view.WindowManager.LayoutParams layoutParams, int i2, int i3, int i4, boolean z) {
        android.window.ActivityWindowInfo activityWindowInfo;
        boolean z2;
        super(windowManagerService);
        this.mAttrs = new android.view.WindowManager.LayoutParams();
        this.mPolicyVisibility = 3;
        this.mLegacyPolicyVisibilityAfterAnim = true;
        this.mAppOpVisibility = true;
        this.mHidden = true;
        this.mDragResizingChangeReported = true;
        this.mRedrawForSyncReported = true;
        this.mCreateTime = java.lang.System.currentTimeMillis();
        this.mSyncSeqId = 0;
        this.mPrepareSyncSeqId = 0;
        this.mRelayoutSeq = -1;
        this.mLayoutSeq = -1;
        this.mLastReportedConfiguration = new android.util.MergedConfiguration();
        this.mLastReportedFrames = new android.window.ClientWindowFrames();
        this.mLastReportedInsetsState = new android.view.InsetsState();
        this.mLastReportedActiveControls = new android.view.InsetsSourceControl.Array();
        this.mTempConfiguration = new android.content.res.Configuration();
        this.mGivenContentInsets = new android.graphics.Rect();
        this.mGivenVisibleInsets = new android.graphics.Rect();
        this.mGivenTouchableRegion = new android.graphics.Region();
        this.mTouchableInsets = 0;
        this.mGlobalScale = 1.0f;
        this.mInvGlobalScale = 1.0f;
        this.mCompatScale = 1.0f;
        this.mHScale = 1.0f;
        this.mVScale = 1.0f;
        this.mLastHScale = 1.0f;
        this.mLastVScale = 1.0f;
        this.mXOffset = 0;
        this.mYOffset = 0;
        this.mWallpaperScale = 1.0f;
        this.mTmpMatrix = new android.graphics.Matrix();
        this.mTmpMatrixArray = new float[9];
        this.mWindowFrames = new com.android.server.wm.WindowFrames();
        this.mExclusionRects = new java.util.ArrayList();
        this.mKeepClearAreas = new java.util.ArrayList();
        this.mUnrestrictedKeepClearAreas = new java.util.ArrayList();
        this.mLastRequestedExclusionHeight = new int[]{0, 0};
        this.mLastGrantedExclusionHeight = new int[]{0, 0};
        this.mLastExclusionLogUptimeMillis = new long[]{0, 0};
        this.mWallpaperX = -1.0f;
        this.mWallpaperY = -1.0f;
        this.mWallpaperZoomOut = -1.0f;
        this.mWallpaperXStep = -1.0f;
        this.mWallpaperYStep = -1.0f;
        this.mWallpaperDisplayOffsetX = Integer.MIN_VALUE;
        this.mWallpaperDisplayOffsetY = Integer.MIN_VALUE;
        this.mLastVisibleLayoutRotation = -1;
        this.mHasSurface = false;
        this.mTmpRect = new android.graphics.Rect();
        this.mTmpPoint = new android.graphics.Point();
        this.mTmpRegion = new android.graphics.Region();
        this.mSeamlesslyRotated = false;
        this.mImeInsetsConsumed = false;
        this.mAboveInsetsState = new android.view.InsetsState();
        this.mMergedLocalInsetsSources = null;
        this.mLastSurfaceInsets = new android.graphics.Rect();
        this.mSurfacePosition = new android.graphics.Point();
        this.mTapExcludeRegion = new android.graphics.Region();
        this.mIsDimming = false;
        this.mRequestedVisibleTypes = android.view.WindowInsets.Type.defaultVisible();
        this.mFrameRateSelectionPriority = -1;
        this.mFrameRateVote = new com.android.server.wm.RefreshRatePolicy.FrameRateVote();
        this.mDrawHandlers = new java.util.ArrayList();
        this.mSeamlessRotationFinishedConsumer = new java.util.function.Consumer() { // from class: com.android.server.wm.WindowState$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$0((android.view.SurfaceControl.Transaction) obj);
            }
        };
        this.mSetSurfacePositionConsumer = new java.util.function.Consumer() { // from class: com.android.server.wm.WindowState$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$1((android.view.SurfaceControl.Transaction) obj);
            }
        };
        this.mWmsExt = (com.android.server.wm.IWindowManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowManagerServiceExt.class).create();
        this.mWindowStateWrapper = new com.android.server.wm.WindowState.WindowStateWrapper();
        this.mWindowStateExt = (com.android.server.wm.IWindowStateExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowStateExt.class).base(this).create();
        this.mTmpTransaction = windowManagerService.mTransactionFactory.get();
        this.mSession = session;
        this.mClient = iWindow;
        this.mAppOp = i;
        this.mToken = windowToken;
        this.mDisplayContent = windowToken.mDisplayContent;
        this.mActivityRecord = this.mToken.asActivityRecord();
        this.mOwnerUid = i3;
        this.mShowUserId = i4;
        this.mOwnerCanAddInternalSystemWindow = z;
        this.mWindowId = new com.android.server.wm.WindowState.WindowId();
        this.mAttrs.copyFrom(layoutParams);
        this.mLastSurfaceInsets.set(this.mAttrs.surfaceInsets);
        this.mViewVisibility = i2;
        this.mPolicy = this.mWmService.mPolicy;
        this.mContext = this.mWmService.mContext;
        if (com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE) {
            this.mForceSeamlesslyRotate = windowToken.mRoundedCornerOverlay;
        } else {
            if (windowToken.mRoundedCornerOverlay || this.mAttrs.type == 2099) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.mForceSeamlesslyRotate = z2;
        }
        if (com.android.window.flags.Flags.activityWindowInfoFlag() && this.mActivityRecord != null) {
            activityWindowInfo = new android.window.ActivityWindowInfo();
        } else {
            activityWindowInfo = null;
        }
        this.mLastReportedActivityWindowInfo = activityWindowInfo;
        this.mInputWindowHandle = new com.android.server.wm.InputWindowHandleWrapper(new android.view.InputWindowHandle(this.mActivityRecord != null ? this.mActivityRecord.getInputApplicationHandle(false) : null, getDisplayId()));
        this.mInputWindowHandle.setFocusable(false);
        this.mInputWindowHandle.setOwnerPid(session.mPid);
        this.mInputWindowHandle.setOwnerUid(session.mUid);
        this.mInputWindowHandle.setName(getName());
        this.mInputWindowHandle.setPackageName(this.mAttrs.packageName);
        this.mInputWindowHandle.setLayoutParamsType(this.mAttrs.type);
        if (!com.android.window.flags.Flags.surfaceTrustedOverlay()) {
            this.mInputWindowHandle.setTrustedOverlay(isWindowTrustedOverlay());
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
            android.util.Slog.v(TAG, "Window " + this + " client=" + iWindow.asBinder() + " token=" + windowToken + " (" + this.mAttrs.token + ") params=" + layoutParams);
        }
        if (this.mAttrs.type < 1000 || this.mAttrs.type > 1999) {
            this.mBaseLayer = (this.mPolicy.getWindowLayerLw(this) * 10000) + 1000;
            this.mSubLayer = 0;
            this.mIsChildWindow = false;
            this.mLayoutAttached = false;
            this.mIsImWindow = this.mAttrs.type == 2011 || this.mAttrs.type == 2012;
            this.mIsWallpaper = this.mAttrs.type == 2013;
        } else {
            this.mBaseLayer = (this.mPolicy.getWindowLayerLw(windowState) * 10000) + 1000;
            this.mSubLayer = this.mPolicy.getSubWindowLayerFromTypeLw(layoutParams.type);
            this.mIsChildWindow = true;
            this.mLayoutAttached = this.mAttrs.type != 1003;
            this.mIsImWindow = windowState.mAttrs.type == 2011 || windowState.mAttrs.type == 2012;
            this.mIsWallpaper = windowState.mAttrs.type == 2013;
        }
        this.mIsFloatingLayer = this.mIsImWindow || this.mIsWallpaper;
        if (this.mActivityRecord != null && this.mActivityRecord.mShowForAllUsers) {
            this.mAttrs.flags |= 524288;
        }
        this.mWinAnimator = new com.android.server.wm.WindowStateAnimator(this);
        this.mWinAnimator.mAlpha = layoutParams.alpha;
        this.mRequestedWidth = -1;
        this.mRequestedHeight = -1;
        this.mLastRequestedWidth = -1;
        this.mLastRequestedHeight = -1;
        this.mLayer = 0;
        this.mOverrideScale = this.mWmService.mAtmService.mCompatModePackages.getCompatScale(this.mAttrs.packageName, session.mUid);
        updateGlobalScale();
        this.mWindowStateExt.onWindowStateCreated(this);
        if (this.mIsChildWindow) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 8135615413833185273L, 0, null, java.lang.String.valueOf(this), java.lang.String.valueOf(windowState));
            }
            windowState.addChild(this, sWindowSubLayerComparator);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void setInitialSurfaceControlProperties(android.view.SurfaceControl.Builder b) {
        super.setInitialSurfaceControlProperties(b);
        if (com.android.window.flags.Flags.surfaceTrustedOverlay() && isWindowTrustedOverlay()) {
            getPendingTransaction().setTrustedOverlay(this.mSurfaceControl, true);
        }
        if (com.android.window.flags.Flags.secureWindowState()) {
            getPendingTransaction().setSecure(this.mSurfaceControl, isSecureLocked());
        }
        boolean canOccludePresentation = !this.mSession.mCanAddInternalSystemWindow;
        getPendingTransaction().setCanOccludePresentation(this.mSurfaceControl, canOccludePresentation);
    }

    void updateTrustedOverlay() {
        this.mInputWindowHandle.setTrustedOverlay(getPendingTransaction(), this.mSurfaceControl, isWindowTrustedOverlay());
        this.mInputWindowHandle.forceChange();
    }

    boolean isWindowTrustedOverlay() {
        return com.android.server.wm.InputMonitor.isTrustedOverlay(this.mAttrs.type) || ((this.mAttrs.privateFlags & 536870912) != 0 && this.mSession.mCanAddInternalSystemWindow) || this.mWindowStateExt.isOplusTrustedWindow(this.mAttrs) || ((this.mAttrs.privateFlags & 8) != 0 && this.mSession.mCanCreateSystemApplicationOverlay);
    }

    int getTouchOcclusionMode() {
        return (android.view.WindowManager.LayoutParams.isSystemAlertWindowType(this.mAttrs.type) || isAnimating(3, -1) || inTransition()) ? 1 : 0;
    }

    void updateGlobalScale() {
        float compatScale;
        if (hasCompatScale()) {
            if (this.mOverrideScale == 1.0f || this.mToken.hasSizeCompatBounds()) {
                compatScale = this.mToken.getCompatScale();
            } else {
                compatScale = 1.0f;
            }
            this.mCompatScale = compatScale;
            this.mGlobalScale = this.mCompatScale * this.mOverrideScale;
            if (this.mToken instanceof com.android.server.wm.ActivityRecord) {
                this.mGlobalScale *= ((com.android.server.wm.ActivityRecord) this.mToken).getWrapper().getExtImpl().getCompatScaleInOplusCompatMode();
            }
            this.mInvGlobalScale = 1.0f / this.mGlobalScale;
            return;
        }
        this.mCompatScale = 1.0f;
        this.mInvGlobalScale = 1.0f;
        this.mGlobalScale = 1.0f;
    }

    float getCompatScaleForClient() {
        if (this.mToken.hasSizeCompatBounds()) {
            return 1.0f;
        }
        return this.mCompatScale;
    }

    boolean hasCompatScale() {
        if (this.mAttrs.type == 3) {
            return false;
        }
        if ((this.mToken == null || !(this.mToken instanceof com.android.server.wm.ActivityRecord) || ((com.android.server.wm.ActivityRecord) this.mToken).getWrapper().getExtImpl().getCompatScaleInOplusCompatMode() == 1.0f) && !this.mWmService.mAtmService.mCompatModePackages.useLegacyScreenCompatMode(this.mSession.mProcess.mInfo.packageName)) {
            return (this.mActivityRecord != null && this.mActivityRecord.hasSizeCompatBounds()) || this.mOverrideScale != 1.0f;
        }
        return true;
    }

    boolean getDrawnStateEvaluated() {
        return this.mDrawnStateEvaluated;
    }

    void setDrawnStateEvaluated(boolean evaluated) {
        this.mDrawnStateEvaluated = evaluated;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    void onParentChanged(com.android.server.wm.ConfigurationContainer newParent, com.android.server.wm.ConfigurationContainer oldParent) {
        super.onParentChanged(newParent, oldParent);
        setDrawnStateEvaluated(false);
        getDisplayContent().reapplyMagnificationSpec();
        this.mWindowStateExt.createCompactDimmer(this);
    }

    int getOwningUid() {
        return this.mOwnerUid;
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowState
    public java.lang.String getOwningPackage() {
        return this.mAttrs.packageName;
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowState
    public boolean canAddInternalSystemWindow() {
        return this.mOwnerCanAddInternalSystemWindow;
    }

    boolean skipLayout() {
        return this.mActivityRecord != null && this.mActivityRecord.mWaitForEnteringPinnedMode;
    }

    void setFrames(android.window.ClientWindowFrames clientWindowFrames, int requestedWidth, int requestedHeight) {
        com.android.server.wm.WindowFrames windowFrames = this.mWindowFrames;
        this.mTmpRect.set(windowFrames.mParentFrame);
        windowFrames.mDisplayFrame.set(clientWindowFrames.displayFrame);
        windowFrames.mParentFrame.set(clientWindowFrames.parentFrame);
        windowFrames.mFrame.set(clientWindowFrames.frame);
        windowFrames.mCompatFrame.set(windowFrames.mFrame);
        if (this.mInvGlobalScale != 1.0f) {
            windowFrames.mCompatFrame.scale(this.mInvGlobalScale);
        }
        windowFrames.setParentFrameWasClippedByDisplayCutout(clientWindowFrames.isParentFrameClippedByDisplayCutout);
        windowFrames.mRelFrame.set(windowFrames.mFrame);
        com.android.server.wm.WindowContainer<?> parent = getParent();
        int parentLeft = 0;
        int parentTop = 0;
        if (this.mIsChildWindow) {
            parentLeft = ((com.android.server.wm.WindowState) parent).mWindowFrames.mFrame.left;
            parentTop = ((com.android.server.wm.WindowState) parent).mWindowFrames.mFrame.top;
        } else if (parent != null) {
            android.graphics.Rect parentBounds = parent.getBounds();
            parentLeft = parentBounds.left;
            parentTop = parentBounds.top;
        }
        windowFrames.mRelFrame.offsetTo(windowFrames.mFrame.left - parentLeft, windowFrames.mFrame.top - parentTop);
        if (this.mWindowStateExt.supportTransWindowAnim(this, windowFrames)) {
            this.mMovedByResize = true;
        }
        if (requestedWidth != this.mLastRequestedWidth || requestedHeight != this.mLastRequestedHeight || !this.mTmpRect.equals(windowFrames.mParentFrame)) {
            this.mLastRequestedWidth = requestedWidth;
            this.mLastRequestedHeight = requestedHeight;
            windowFrames.setContentChanged(true);
        }
        if (!windowFrames.mFrame.equals(windowFrames.mLastFrame) || !windowFrames.mRelFrame.equals(windowFrames.mLastRelFrame)) {
            this.mWmService.mFrameChangingWindows.add(this);
        }
        if (this.mAttrs.type == 2034 && !windowFrames.mFrame.equals(windowFrames.mLastFrame)) {
            this.mMovedByResize = true;
        }
        if (this.mIsWallpaper) {
            android.graphics.Rect lastFrame = windowFrames.mLastFrame;
            android.graphics.Rect frame = windowFrames.mFrame;
            if (lastFrame.width() != frame.width() || lastFrame.height() != frame.height() || this.mWindowStateExt.forceUpdateWallpaperOffset(this)) {
                this.mDisplayContent.mWallpaperController.updateWallpaperOffset(this, false);
            }
        }
        updateSourceFrame(windowFrames.mFrame);
        if (this.mActivityRecord != null && !this.mIsChildWindow) {
            this.mActivityRecord.layoutLetterboxIfNeeded(this);
        }
        this.mSurfacePlacementNeeded = true;
        this.mHaveFrame = true;
    }

    void updateSourceFrame(android.graphics.Rect winFrame) {
        if (!hasInsetsSourceProvider() || this.mGivenInsetsPending) {
            return;
        }
        android.util.SparseArray<com.android.server.wm.InsetsSourceProvider> providers = getInsetsSourceProviders();
        for (int i = providers.size() - 1; i >= 0; i--) {
            providers.valueAt(i).updateSourceFrame(winFrame);
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public android.graphics.Rect getBounds() {
        if ((this.mToken instanceof com.android.server.wm.ActivityRecord) && ((com.android.server.wm.ActivityRecord) this.mToken).getWrapper().getExtImpl().hasSizeCompatBoundsInOplusCompatMode()) {
            return this.mToken.getBounds();
        }
        if (!getWrapper().getExtImpl().layoutFullscreenInEmbedding() || getTask() == null) {
            return this.mToken.hasSizeCompatBounds() ? this.mToken.getBounds() : super.getBounds();
        }
        return getTask().getBounds();
    }

    android.graphics.Rect getFrame() {
        return this.mWindowFrames.mFrame;
    }

    android.graphics.Rect getRelativeFrame() {
        return this.mWindowFrames.mRelFrame;
    }

    android.graphics.Rect getDisplayFrame() {
        return this.mWindowFrames.mDisplayFrame;
    }

    android.graphics.Rect getParentFrame() {
        return this.mWindowFrames.mParentFrame;
    }

    public android.view.WindowManager.LayoutParams getAttrs() {
        return this.mAttrs;
    }

    int getDisableFlags() {
        return this.mDisableFlags;
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowState
    public int getBaseType() {
        return getTopParentWindow().mAttrs.type;
    }

    boolean setReportResizeHints() {
        return this.mWindowFrames.setReportResizeHints();
    }

    void updateResizingWindowIfNeeded() {
        boolean insetsChanged = this.mWindowFrames.hasInsetsChanged();
        if ((!this.mHasSurface || getDisplayContent().mLayoutSeq != this.mLayoutSeq || isGoneForLayout()) && !insetsChanged) {
            return;
        }
        com.android.server.wm.WindowStateAnimator winAnimator = this.mWinAnimator;
        boolean didFrameInsetsChange = setReportResizeHints();
        boolean configChanged = (this.mInRelayout || isLastConfigReportedToClient()) ? false : true;
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION && configChanged) {
            android.util.Slog.v(TAG, "Win " + this + " config changed: " + getConfiguration());
        }
        boolean dragResizingChanged = !this.mDragResizingChangeReported && isDragResizeChanged();
        boolean attachedFrameChanged = this.mLayoutAttached && getParentWindow().frameChanged();
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
            android.util.Slog.v(TAG, "Resizing " + this + ": configChanged=" + configChanged + " last=" + this.mWindowFrames.mLastFrame + " frame=" + this.mWindowFrames.mFrame);
        }
        boolean contentChanged = didFrameInsetsChange || configChanged || dragResizingChanged || attachedFrameChanged;
        if (!contentChanged && !this.mRedrawForSyncReported && this.mPrepareSyncSeqId <= 0 && this.mDrawHandlers.isEmpty()) {
            this.mRedrawForSyncReported = true;
        }
        if (insetsChanged && this.mWindowStateExt.getDeviceFolding() && (isActivityTypeHome() || this.mIsWallpaper)) {
            insetsChanged = false;
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
                android.util.Slog.i(TAG, "ignore updateResizingWindowIfNeeded:" + this);
            }
        }
        if (contentChanged || insetsChanged || shouldSendRedrawForSync()) {
            if (DEBUG_PANIC && !com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE.isLogToLogcat()) {
                android.util.Slog.v(TAG, "Resize reasons for w = " + this + "getInsetsChangedInfo() = " + this.mWindowFrames.getInsetsChangedInfo() + "configChanged = " + configChanged + "didFrameInsetsChange = " + didFrameInsetsChange + "insetsChanged = " + insetsChanged + "attachedFrameChanged = " + attachedFrameChanged + "shouldSendRedrawForSync() = " + shouldSendRedrawForSync());
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RESIZE_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mWindowFrames.getInsetsChangedInfo());
                boolean protoLogParam2 = configChanged;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE, 8842744325264128950L, com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED, null, protoLogParam0, protoLogParam1, java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(didFrameInsetsChange));
            }
            consumeInsetsChange();
            onResizeHandled();
            this.mWmService.makeWindowFreezingScreenIfNeededLocked(this);
            if (((configChanged && !this.mWindowStateExt.skipReportDrawWallpaper()) || getOrientationChanging() || dragResizingChanged) && isVisibleRequested()) {
                this.mWinAnimator.printWindowState(this.mWinAnimator.mDrawState, 1, this, "updateResizingWindowIfNeeded");
                winAnimator.mDrawState = 1;
                if (this.mActivityRecord != null) {
                    this.mActivityRecord.clearAllDrawn();
                    if (this.mAttrs.type == 3 && this.mActivityRecord.mStartingData != null) {
                        this.mActivityRecord.mStartingData.mIsDisplayed = false;
                    }
                }
            }
            if (!this.mWmService.mResizingWindows.contains(this)) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RESIZE_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE, -8636590597069784069L, 0, null, protoLogParam02);
                }
                this.mWmService.mResizingWindows.add(this);
                return;
            }
            return;
        }
        if (getOrientationChanging() && isDrawn()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                java.lang.String protoLogParam03 = java.lang.String.valueOf(this);
                java.lang.String protoLogParam12 = java.lang.String.valueOf(winAnimator.mSurfaceController);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -2710188685736986208L, 0, null, protoLogParam03, protoLogParam12);
            }
            setOrientationChanging(false);
            this.mLastFreezeDuration = (int) (android.os.SystemClock.elapsedRealtime() - this.mWmService.mDisplayFreezeTime);
            this.mWindowStateExt.updateOrientationChangeIfNeeded(this, this.mActivityRecord, this.mWmService);
        }
    }

    private boolean frameChanged() {
        return !this.mWindowFrames.mFrame.equals(this.mWindowFrames.mLastFrame);
    }

    boolean getOrientationChanging() {
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            return false;
        }
        return ((!this.mOrientationChanging && (!isVisible() || getConfiguration().orientation == getLastReportedConfiguration().orientation)) || this.mSeamlesslyRotated || this.mOrientationChangeTimedOut) ? false : true;
    }

    void setOrientationChanging(boolean changing) {
        this.mOrientationChangeTimedOut = false;
        if (this.mOrientationChanging == changing) {
            return;
        }
        this.mOrientationChanging = changing;
        if (changing) {
            this.mLastFreezeDuration = 0;
            if (this.mWmService.mRoot.mOrientationChangeComplete && this.mDisplayContent.shouldSyncRotationChange(this)) {
                this.mWmService.mRoot.mOrientationChangeComplete = false;
                return;
            }
            return;
        }
        this.mDisplayContent.finishAsyncRotation(this.mToken);
    }

    void orientationChangeTimedOut() {
        this.mOrientationChangeTimedOut = true;
    }

    @Override // com.android.server.wm.WindowContainer
    void onDisplayChanged(com.android.server.wm.DisplayContent dc) {
        if (dc != null && this.mDisplayContent != null && dc != this.mDisplayContent && this.mDisplayContent.getImeInputTarget() == this) {
            this.mWindowStateExt.onDisplayImeChanged(dc, this.mDisplayContent, this);
            dc.updateImeInputAndControlTarget(getImeInputTarget());
            this.mDisplayContent.setImeInputTarget(null);
        }
        com.android.server.wm.DisplayContent prevDisplayContent = this.mDisplayContent;
        super.onDisplayChanged(dc);
        this.mWindowStateExt.onDisplayChanged(dc, prevDisplayContent, this);
        if (dc != null && this.mInputWindowHandle.getDisplayId() != dc.getDisplayId()) {
            this.mLayoutSeq = dc.mLayoutSeq - 1;
            this.mInputWindowHandle.setDisplayId(dc.getDisplayId());
        }
        this.mWindowStateExt.onDisplayChangedEnd(this.mInputWindowHandle);
    }

    com.android.server.wm.DisplayFrames getDisplayFrames(com.android.server.wm.DisplayFrames originalFrames) {
        com.android.server.wm.DisplayFrames displayFrames = this.mToken.getFixedRotationTransformDisplayFrames();
        if (displayFrames != null) {
            return displayFrames;
        }
        return originalFrames;
    }

    android.view.DisplayInfo getDisplayInfo() {
        android.view.DisplayInfo displayInfo = this.mToken.getFixedRotationTransformDisplayInfo();
        if (displayInfo != null) {
            return displayInfo;
        }
        return getDisplayContent().getDisplayInfo();
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public android.graphics.Rect getMaxBounds() {
        android.graphics.Rect maxBounds = this.mToken.getFixedRotationTransformMaxBounds();
        if (maxBounds != null) {
            return maxBounds;
        }
        return super.getMaxBounds();
    }

    android.view.InsetsState getInsetsState() {
        return getInsetsState(false);
    }

    android.view.InsetsState getInsetsState(boolean includeTransient) {
        android.view.InsetsState rotatedState = this.mToken.getFixedRotationTransformInsetsState();
        com.android.server.wm.InsetsPolicy insetsPolicy = getDisplayContent().getInsetsPolicy();
        if (rotatedState != null) {
            android.view.InsetsState insetsState = insetsPolicy.adjustInsetsForWindow(this, rotatedState);
            return this.mWindowStateExt.hookGetInsetsState(insetsState, includeTransient);
        }
        android.view.InsetsState insetsState2 = this.mFrozenInsetsState;
        android.view.InsetsState rawInsetsState = insetsState2 != null ? this.mFrozenInsetsState : getMergedInsetsState();
        android.view.InsetsState insetsStateForWindow = insetsPolicy.enforceInsetsPolicyForTarget(this.mAttrs, getWindowingMode(), isAlwaysOnTop(), rawInsetsState);
        android.view.InsetsState insetsState3 = insetsPolicy.adjustInsetsForWindow(this, insetsStateForWindow, includeTransient);
        return this.mWindowStateExt.hookGetInsetsState(insetsState3, includeTransient);
    }

    private android.view.InsetsState getMergedInsetsState() {
        android.view.InsetsState globalInsetsState;
        if (this.mAttrs.receiveInsetsIgnoringZOrder) {
            globalInsetsState = getDisplayContent().getInsetsStateController().getRawInsetsState();
        } else {
            globalInsetsState = this.mAboveInsetsState;
        }
        if (this.mMergedLocalInsetsSources == null) {
            return globalInsetsState;
        }
        android.view.InsetsState mergedInsetsState = new android.view.InsetsState(globalInsetsState);
        for (int i = 0; i < this.mMergedLocalInsetsSources.size(); i++) {
            mergedInsetsState.addSource(this.mMergedLocalInsetsSources.valueAt(i));
        }
        return mergedInsetsState;
    }

    android.view.InsetsState getCompatInsetsState() {
        android.view.InsetsState state = getInsetsState();
        if (this.mInvGlobalScale != 1.0f) {
            state = new android.view.InsetsState(state, true);
            state.scale(this.mInvGlobalScale);
        }
        return this.mWindowStateExt.hookGetCompatInsetsState(state);
    }

    android.view.InsetsState getInsetsStateWithVisibilityOverride() {
        android.view.InsetsState state = new android.view.InsetsState(getInsetsState(), true);
        for (int i = state.sourceSize() - 1; i >= 0; i--) {
            android.view.InsetsSource source = state.sourceAt(i);
            boolean requestedVisible = isRequestedVisible(source.getType());
            if (source.isVisible() != requestedVisible) {
                source.setVisible(requestedVisible);
            }
        }
        return state;
    }

    @Override // com.android.server.wm.InputTarget
    public int getDisplayId() {
        com.android.server.wm.DisplayContent displayContent = getDisplayContent();
        if (displayContent == null) {
            return -1;
        }
        return displayContent.getDisplayId();
    }

    @Override // com.android.server.wm.InputTarget
    public com.android.server.wm.WindowState getWindowState() {
        return this;
    }

    @Override // com.android.server.wm.InputTarget
    public android.os.IBinder getWindowToken() {
        return this.mClient.asBinder();
    }

    @Override // com.android.server.wm.InputTarget
    public int getPid() {
        return this.mSession.mPid;
    }

    @Override // com.android.server.wm.InputTarget
    public int getUid() {
        return this.mSession.mUid;
    }

    com.android.server.wm.Task getTask() {
        if (this.mActivityRecord != null) {
            return this.mActivityRecord.getTask();
        }
        return null;
    }

    com.android.server.wm.TaskFragment getTaskFragment() {
        if (this.mActivityRecord != null) {
            return this.mActivityRecord.getTaskFragment();
        }
        return null;
    }

    com.android.server.wm.Task getRootTask() {
        com.android.server.wm.Task task = getTask();
        if (task != null) {
            return task.getRootTask();
        }
        com.android.server.wm.DisplayContent dc = getDisplayContent();
        if (this.mAttrs.type < 2000 || dc == null) {
            return null;
        }
        return dc.getDefaultTaskDisplayArea().getRootHomeTask();
    }

    void getVisibleBounds(android.graphics.Rect bounds) {
        com.android.server.wm.Task task = getTask();
        boolean intersectWithRootTaskBounds = task != null && task.cropWindowsToRootTaskBounds();
        bounds.setEmpty();
        this.mTmpRect.setEmpty();
        if (intersectWithRootTaskBounds) {
            com.android.server.wm.Task rootTask = task.getRootTask();
            if (rootTask != null) {
                rootTask.getDimBounds(this.mTmpRect);
            } else {
                intersectWithRootTaskBounds = false;
            }
        }
        bounds.set(this.mWindowFrames.mFrame);
        bounds.inset(getInsetsStateWithVisibilityOverride().calculateVisibleInsets(bounds, this.mAttrs.type, getActivityType(), this.mAttrs.softInputMode, this.mAttrs.flags));
        if (intersectWithRootTaskBounds) {
            bounds.intersect(this.mTmpRect);
        }
    }

    public long getInputDispatchingTimeoutMillis() {
        if (this.mActivityRecord != null) {
            return this.mActivityRecord.mInputDispatchingTimeoutMillis;
        }
        return android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
    }

    long getCreateTime() {
        return this.mCreateTime;
    }

    boolean hasAppShownWindows() {
        return this.mActivityRecord != null && (this.mActivityRecord.firstWindowDrawn || this.mActivityRecord.isStartingWindowDisplayed());
    }

    @Override // com.android.server.wm.WindowContainer
    boolean hasContentToDisplay() {
        if (!this.mAppFreezing && isDrawn()) {
            if (this.mViewVisibility != 0) {
                if (isAnimating(3) && !getDisplayContent().mAppTransition.isTransitionSet()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return super.hasContentToDisplay();
    }

    private boolean isVisibleByPolicyOrInsets() {
        return isVisibleByPolicy() && (this.mControllableInsetProvider == null || this.mControllableInsetProvider.isClientVisible());
    }

    @Override // com.android.server.wm.WindowContainer
    public boolean isVisible() {
        return wouldBeVisibleIfPolicyIgnored() && isVisibleByPolicyOrInsets();
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isVisibleRequested() {
        boolean localVisibleRequested = wouldBeVisibleRequestedIfPolicyIgnored() && isVisibleByPolicyOrInsets();
        if (localVisibleRequested && shouldCheckTokenVisibleRequested()) {
            return this.mToken.isVisibleRequested();
        }
        return localVisibleRequested;
    }

    boolean shouldCheckTokenVisibleRequested() {
        return (this.mActivityRecord == null && this.mToken.asWallpaperToken() == null) ? false : true;
    }

    boolean isVisibleByPolicy() {
        return (this.mPolicyVisibility & 3) == 3;
    }

    boolean providesDisplayDecorInsets() {
        if (this.mInsetsSourceProviders == null) {
            return false;
        }
        int decorInsetsTypes = this.mWmService.mConfigTypes | this.mWmService.mOverrideConfigTypes;
        for (int i = this.mInsetsSourceProviders.size() - 1; i >= 0; i--) {
            android.view.InsetsSource source = this.mInsetsSourceProviders.valueAt(i).getSource();
            if ((source.getType() & decorInsetsTypes) != 0) {
                return true;
            }
        }
        return false;
    }

    void clearPolicyVisibilityFlag(int policyVisibilityFlag) {
        this.mPolicyVisibility &= ~policyVisibilityFlag;
        this.mWmService.scheduleAnimationLocked();
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT || com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
            android.util.Slog.i(TAG, "clearPolicyVisibilityFlag =  " + java.lang.Integer.toHexString(policyVisibilityFlag) + " this = " + this + " from stack callers=" + android.os.Debug.getCallers(5));
        }
    }

    void setPolicyVisibilityFlag(int policyVisibilityFlag) {
        this.mPolicyVisibility |= policyVisibilityFlag;
        this.mWmService.scheduleAnimationLocked();
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT || com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
            android.util.Slog.i(TAG, "setPolicyVisibilityFlag = " + java.lang.Integer.toHexString(policyVisibilityFlag) + " this = " + this + " from stack callers=" + android.os.Debug.getCallers(5));
        }
    }

    private boolean isLegacyPolicyVisibility() {
        return (this.mPolicyVisibility & 1) != 0;
    }

    boolean wouldBeVisibleIfPolicyIgnored() {
        if (!this.mHasSurface || isParentWindowHidden() || this.mAnimatingExit || this.mDestroying) {
            return false;
        }
        boolean isWallpaper = this.mToken.asWallpaperToken() != null;
        return !isWallpaper || this.mToken.isVisible();
    }

    private boolean wouldBeVisibleRequestedIfPolicyIgnored() {
        com.android.server.wm.WindowState parent = getParentWindow();
        boolean isParentHiddenRequested = (parent == null || parent.isVisibleRequested()) ? false : true;
        if (isParentHiddenRequested || this.mAnimatingExit || this.mDestroying) {
            return false;
        }
        boolean isWallpaper = this.mToken.asWallpaperToken() != null;
        return !isWallpaper || this.mToken.isVisibleRequested();
    }

    boolean isVisibleNow() {
        return (this.mToken.isVisible() || this.mAttrs.type == 3) && isVisible();
    }

    boolean isPotentialDragTarget(boolean targetInterceptsGlobalDrag) {
        return ((!targetInterceptsGlobalDrag && !isVisibleNow()) || this.mRemoved || this.mInputChannel == null || this.mInputWindowHandle == null) ? false : true;
    }

    boolean isVisibleRequestedOrAdding() {
        com.android.server.wm.ActivityRecord atoken = this.mActivityRecord;
        return (this.mHasSurface || (!this.mRelayoutCalled && this.mViewVisibility == 0)) && isVisibleByPolicy() && !isParentWindowHidden() && !((atoken != null && !atoken.isVisibleRequested()) || this.mAnimatingExit || this.mDestroying);
    }

    boolean isOnScreen() {
        if (!this.mHasSurface || this.mDestroying || !isVisibleByPolicy() || !this.mWindowStateExt.canShowInLockDeviceMode(this.mAttrs.type)) {
            return false;
        }
        com.android.server.wm.ActivityRecord atoken = this.mActivityRecord;
        if (atoken != null) {
            boolean isVisible = isStartingWindowAssociatedToTask() ? this.mStartingData.mAssociatedTask.isVisible() : atoken.isVisible();
            return (!isParentWindowHidden() && isVisible) || isAnimationRunningSelfOrParent();
        }
        com.android.server.wm.WallpaperWindowToken wtoken = this.mToken.asWallpaperToken();
        return wtoken != null ? (!isParentWindowHidden() && wtoken.isVisible()) || this.mWindowStateExt.inKeyguardAppearingTransit() : !isParentWindowHidden() || isAnimating(3);
    }

    boolean isDreamWindow() {
        return this.mActivityRecord != null && this.mActivityRecord.getActivityType() == 5;
    }

    boolean isSecureLocked() {
        if (this.mWmService.getDisableSecureWindows()) {
            return false;
        }
        if ((this.mAttrs.flags & 8192) == 0 && !this.mWmService.mSensitiveContentPackages.shouldBlockScreenCaptureForApp(getOwningPackage(), getOwningUid(), getWindowToken())) {
            return !android.app.admin.DevicePolicyCache.getInstance().isScreenCaptureAllowed(this.mShowUserId);
        }
        return true;
    }

    boolean mightAffectAllDrawn() {
        boolean isAppType = this.mWinAnimator.mAttrType == 1 || this.mWinAnimator.mAttrType == 4;
        return ((!isOnScreen() && !isAppType) || this.mAnimatingExit || this.mDestroying) ? false : true;
    }

    boolean isInteresting() {
        com.android.server.wm.RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
        return this.mActivityRecord != null && !(this.mActivityRecord.isFreezingScreen() && this.mAppFreezing && !this.mWindowStateExt.getDeviceFolding()) && this.mViewVisibility == 0 && (recentsAnimationController == null || recentsAnimationController.isInterestingForAllDrawn(this));
    }

    boolean isReadyForDisplay() {
        if (this.mWindowStateExt.isNotReadyForDisplayDuringFixedRotation(this, getDisplayContent(), this.mWindowFrames.mFrame)) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM) {
                android.util.Slog.d(TAG, "Block isReadyForDisplay of " + this);
            }
            return false;
        }
        boolean parentAndClientVisible = !isParentWindowHidden() && this.mViewVisibility == 0 && (this.mToken.isVisible() || this.mToken.mChildren.isEmpty());
        if (this.mHasSurface && isVisibleByPolicy() && !this.mDestroying) {
            return parentAndClientVisible || isAnimating(3);
        }
        return false;
    }

    boolean isFullyTransparent() {
        return this.mAttrs.alpha == 0.0f;
    }

    boolean canAffectSystemUiFlags() {
        if (isFullyTransparent()) {
            return false;
        }
        if (this.mActivityRecord == null) {
            boolean shown = this.mWinAnimator.getShown();
            boolean exiting = this.mAnimatingExit || this.mDestroying;
            return shown && !exiting;
        }
        if (this.mActivityRecord.canAffectSystemUiFlags()) {
            return (this.mAttrs.type == 3 && (this.mStartingData instanceof com.android.server.wm.SnapshotStartingData)) ? false : true;
        }
        return false;
    }

    boolean isDisplayed() {
        com.android.server.wm.ActivityRecord atoken = this.mActivityRecord;
        return isDrawn() && isVisibleByPolicy() && ((!isParentWindowHidden() && (atoken == null || atoken.isVisibleRequested())) || isAnimationRunningSelfOrParent());
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowState
    public boolean isAnimatingLw() {
        return isAnimating(3);
    }

    public boolean isGoneForLayout() {
        com.android.server.wm.ActivityRecord atoken = this.mActivityRecord;
        return this.mViewVisibility == 8 || !this.mRelayoutCalled || (atoken == null && !(wouldBeVisibleIfPolicyIgnored() && isVisibleByPolicy())) || (!(atoken == null || atoken.isVisibleRequested()) || isParentWindowGoneForLayout() || ((this.mAnimatingExit && !isAnimatingLw()) || this.mDestroying));
    }

    public boolean isDrawFinishedLw() {
        return this.mHasSurface && !this.mDestroying && (this.mWinAnimator.mDrawState == 2 || this.mWinAnimator.mDrawState == 3 || this.mWinAnimator.mDrawState == 4);
    }

    public boolean isDrawn() {
        return this.mHasSurface && !this.mDestroying && (this.mWinAnimator.mDrawState == 3 || this.mWinAnimator.mDrawState == 4);
    }

    private boolean isOpaqueDrawn() {
        boolean isWallpaper = this.mToken.asWallpaperToken() != null;
        return ((!isWallpaper && this.mAttrs.format == -1) || (isWallpaper && this.mToken.isVisible())) && isDrawn() && !isAnimating(3);
    }

    void requestDrawIfNeeded(java.util.List<com.android.server.wm.WindowState> outWaitingForDrawn) {
        if (!isVisible()) {
            return;
        }
        com.android.server.wm.WallpaperWindowToken wallpaperToken = this.mToken.asWallpaperToken();
        if (wallpaperToken != null) {
            if (wallpaperToken.hasVisibleNotDrawnWallpaper()) {
                outWaitingForDrawn.add(this);
                return;
            }
            return;
        }
        if (this.mActivityRecord != null) {
            if (!this.mActivityRecord.isVisibleRequested() || this.mActivityRecord.allDrawn || this.mWindowStateExt.checkIfHasDrawn(this)) {
                return;
            }
            if (this.mAttrs.type == 3) {
                if (isDrawn()) {
                    return;
                }
            } else if (this.mActivityRecord.mStartingWindow != null) {
                return;
            }
        } else if (!this.mPolicy.isKeyguardHostWindow(this.mAttrs)) {
            return;
        }
        this.mWinAnimator.printWindowState(this.mWinAnimator.mDrawState, 1, this, "requestDrawIfNeeded");
        this.mWinAnimator.mDrawState = 1;
        forceReportingResized();
        if (outWaitingForDrawn.contains(this) || this.mWindowStateExt.isInSkipWaitingForDrawn(this)) {
            return;
        }
        outWaitingForDrawn.add(this);
    }

    @Override // com.android.server.wm.WindowContainer
    void onMovedByResize() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RESIZE_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE, 5236278969232209904L, 0, null, protoLogParam0);
        }
        this.mMovedByResize = true;
        super.onMovedByResize();
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
    void onAppVisibilityChanged(boolean visible, boolean runningAppAnimation) {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            ((com.android.server.wm.WindowState) this.mChildren.get(i)).onAppVisibilityChanged(visible, runningAppAnimation);
        }
        boolean isVisibleNow = isVisibleNow();
        if (this.mAttrs.type == 3) {
            if (!visible && isVisibleNow && this.mActivityRecord.isAnimating(3)) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, 7646042751617940718L, 0, null, protoLogParam0);
                }
                this.mAnimatingExit = true;
                this.mRemoveOnExit = true;
                this.mWindowRemovalAllowed = true;
                return;
            }
            return;
        }
        if (visible != isVisibleNow) {
            if (!runningAppAnimation && isVisibleNow) {
                com.android.server.wm.AccessibilityController accessibilityController = this.mWmService.mAccessibilityController;
                this.mWinAnimator.applyAnimationLocked(2, false);
                if (accessibilityController.hasCallbacks()) {
                    accessibilityController.onWindowTransition(this, 2);
                }
            }
            setDisplayLayoutNeeded();
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
    boolean onSetAppExiting(boolean animateExit) {
        com.android.server.wm.DisplayContent displayContent = getDisplayContent();
        boolean changed = false;
        if (!animateExit) {
            this.mPermanentlyHidden = true;
            hide(false, false);
        }
        if (isVisibleNow() && animateExit) {
            this.mWinAnimator.applyAnimationLocked(2, false);
            if (this.mWmService.mAccessibilityController.hasCallbacks()) {
                this.mWmService.mAccessibilityController.onWindowTransition(this, 2);
            }
            changed = true;
            if (displayContent != null) {
                displayContent.setLayoutNeeded();
            }
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState c = (com.android.server.wm.WindowState) this.mChildren.get(i);
            changed |= c.onSetAppExiting(animateExit);
        }
        return changed;
    }

    @Override // com.android.server.wm.WindowContainer
    void onResize() {
        java.util.ArrayList<com.android.server.wm.WindowState> resizingWindows = this.mWmService.mResizingWindows;
        if (this.mHasSurface && !isGoneForLayout() && !resizingWindows.contains(this)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RESIZE_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE, 1783521309242112490L, 0, null, protoLogParam0);
            }
            if (this.mWindowStateExt.loggingWhenFolding()) {
                android.util.Slog.d(TAG, "FSS_onResize: Resizing " + this);
            }
            resizingWindows.add(this);
            this.mWindowStateExt.needResetDrawStateOnResize(this, this.mWindowFrames.mFrame);
        }
        super.onResize();
    }

    void handleWindowMovedIfNeeded() {
        if (!hasMoved()) {
            return;
        }
        int left = this.mWindowFrames.mFrame.left;
        int top = this.mWindowFrames.mFrame.top;
        if ((canPlayMoveAnimation() && !this.mWindowStateExt.isNoMoveAnimationOnFlexibleWindow()) || this.mWindowStateExt.forcePlayMoveAnimation(this)) {
            startMoveAnimation(left, top);
        }
        if (this.mWmService.mAccessibilityController.hasCallbacks()) {
            this.mWmService.mAccessibilityController.onSomeWindowResizedOrMoved(getDisplayId());
        }
        try {
            this.mClient.moved(left, top);
        } catch (android.os.RemoteException e) {
        }
        this.mMovedByResize = false;
    }

    private boolean canPlayMoveAnimation() {
        boolean hasMovementAnimation;
        if (getTask() == null) {
            hasMovementAnimation = getWindowConfiguration().hasMovementAnimations();
        } else {
            hasMovementAnimation = getTask().getWindowConfiguration().hasMovementAnimations();
        }
        return this.mToken.okToAnimate() && (this.mAttrs.privateFlags & 64) == 0 && !isDragResizing() && hasMovementAnimation && !this.mWinAnimator.mLastHidden && !this.mSeamlesslyRotated;
    }

    private boolean hasMoved() {
        return this.mHasSurface && !((!this.mWindowFrames.hasContentChanged() && !this.mMovedByResize) || this.mAnimatingExit || ((this.mWindowFrames.mRelFrame.top == this.mWindowFrames.mLastRelFrame.top && this.mWindowFrames.mRelFrame.left == this.mWindowFrames.mLastRelFrame.left) || ((this.mIsChildWindow && getParentWindow().hasMoved()) || this.mTransitionController.isCollecting())));
    }

    boolean isObscuringDisplay() {
        com.android.server.wm.Task task = getTask();
        return (task == null || task.fillsParent()) && isOpaqueDrawn() && fillsDisplay();
    }

    boolean fillsDisplay() {
        android.view.DisplayInfo displayInfo = getDisplayInfo();
        return this.mWindowFrames.mFrame.left <= 0 && this.mWindowFrames.mFrame.top <= 0 && this.mWindowFrames.mFrame.right >= displayInfo.appWidth && this.mWindowFrames.mFrame.bottom >= displayInfo.appHeight;
    }

    boolean matchesDisplayAreaBounds() {
        android.graphics.Rect rotatedDisplayBounds = this.mToken.getFixedRotationTransformDisplayBounds();
        if (rotatedDisplayBounds != null) {
            return rotatedDisplayBounds.equals(getBounds());
        }
        com.android.server.wm.DisplayArea displayArea = getDisplayArea();
        if (displayArea == null) {
            return getDisplayContent().getBounds().equals(getBounds());
        }
        return displayArea.getBounds().equals(getBounds());
    }

    boolean isLastConfigReportedToClient() {
        return this.mLastConfigReportedToClient;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(android.content.res.Configuration newParentConfig) {
        android.content.res.Configuration selfConfiguration = super.getConfiguration();
        this.mTempConfiguration.setTo(selfConfiguration);
        super.onConfigurationChanged(newParentConfig);
        int diff = selfConfiguration.diff(this.mTempConfiguration);
        if (diff != 0) {
            this.mLastConfigReportedToClient = false;
        }
        if ((getDisplayContent().getImeInputTarget() == this || isImeLayeringTarget()) && (536870912 & diff) != 0) {
            this.mDisplayContent.updateImeControlTarget(isImeLayeringTarget());
            if (this.mStartingData != null && this.mStartingData.mAssociatedTask == null && this.mTempConfiguration.windowConfiguration.getRotation() == selfConfiguration.windowConfiguration.getRotation() && !this.mTempConfiguration.windowConfiguration.getBounds().equals(getBounds())) {
                this.mStartingData.mResizedFromTransfer = true;
                this.mActivityRecord.associateStartingWindowWithTaskIfNeeded();
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void removeImmediately() {
        this.mWindowStateExt.removeImmediately(this);
        if (this.mRemoved) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1351053513466395411L, 0, null, protoLogParam0);
                return;
            }
            return;
        }
        this.mRemoved = true;
        this.mWinAnimator.destroySurfaceLocked(getSyncTransaction());
        if (!this.mDrawHandlers.isEmpty()) {
            this.mWmService.mH.removeMessages(64, this);
        }
        super.removeImmediately();
        this.mWindowStateExt.cancelFadeAnimationIfNeed(this);
        if (isImeOverlayLayeringTarget()) {
            this.mWmService.dispatchImeTargetOverlayVisibilityChanged(this.mClient.asBinder(), this.mAttrs.type, false, true);
        }
        com.android.server.wm.DisplayContent dc = getDisplayContent();
        if (isImeLayeringTarget()) {
            dc.removeImeSurfaceByTarget(this);
            dc.setImeLayeringTarget(null);
            dc.computeImeTarget(true);
        }
        if (dc.getImeInputTarget() == this && !inRelaunchingActivity()) {
            this.mWmService.dispatchImeInputTargetVisibilityChanged(this.mClient.asBinder(), false, true);
            dc.updateImeInputAndControlTarget(null);
        }
        int type = this.mAttrs.type;
        if (type == 2037 || type == 2030) {
            this.mWmService.mDisplayManagerInternal.onPresentation(dc.getDisplay().getDisplayId(), false);
        }
        dc.getDisplayPolicy().removeWindowLw(this);
        disposeInputChannel();
        this.mOnBackInvokedCallbackInfo = null;
        this.mSession.onWindowRemoved(this);
        this.mWindowStateExt.cancelSplashScreenAnimation(this);
        this.mWmService.postWindowRemoveCleanupLocked(this);
        this.mWindowStateExt.setSimultaneousDisplayState(false);
        consumeInsetsChange();
    }

    @Override // com.android.server.wm.WindowContainer
    void removeIfPossible() {
        this.mWindowRemovalAllowed = true;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(android.os.Debug.getCallers(5));
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 3927343382258792268L, 0, null, protoLogParam0, protoLogParam1);
        }
        boolean startingWindow = this.mStartingData != null;
        if (startingWindow) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[0]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -4831815184899821371L, 0, null, protoLogParam02);
            }
            if (this.mActivityRecord != null) {
                this.mActivityRecord.forAllWindows(new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.wm.WindowState$$ExternalSyntheticLambda3
                    public final boolean apply(java.lang.Object obj) {
                        return com.android.server.wm.WindowState.lambda$removeIfPossible$2((com.android.server.wm.WindowState) obj);
                    }
                }, true);
            }
            this.mTransitionController.mTransitionTracer.logRemovingStartingWindow(this.mStartingData);
        } else if (this.mAttrs.type == 1 && isSelfAnimating(0, 128)) {
            cancelAnimation();
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[1]) {
            long protoLogParam03 = java.lang.System.identityHashCode(this.mClient.asBinder());
            java.lang.String protoLogParam12 = java.lang.String.valueOf(this.mWinAnimator.mSurfaceController);
            java.lang.String protoLogParam2 = java.lang.String.valueOf(android.os.Debug.getCallers(5));
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, -5803097884846965819L, 1, null, java.lang.Long.valueOf(protoLogParam03), protoLogParam12, protoLogParam2);
        }
        com.android.server.wm.DisplayContent displayContent = getDisplayContent();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            disposeInputChannel();
            this.mOnBackInvokedCallbackInfo = null;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
                java.lang.String protoLogParam04 = java.lang.String.valueOf(this);
                java.lang.String protoLogParam13 = java.lang.String.valueOf(this.mWinAnimator.mSurfaceController);
                boolean protoLogParam22 = this.mAnimatingExit;
                boolean protoLogParam3 = this.mRemoveOnExit;
                boolean protoLogParam4 = this.mHasSurface;
                boolean protoLogParam5 = this.mWinAnimator.getShown();
                boolean protoLogParam6 = isAnimating(3);
                boolean protoLogParam7 = this.mActivityRecord != null && this.mActivityRecord.isAnimating(3);
                boolean protoLogParam8 = this.mWmService.mDisplayFrozen;
                java.lang.String protoLogParam9 = java.lang.String.valueOf(android.os.Debug.getCallers(6));
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -2547748024041128829L, 262128, null, protoLogParam04, protoLogParam13, java.lang.Boolean.valueOf(protoLogParam22), java.lang.Boolean.valueOf(protoLogParam3), java.lang.Boolean.valueOf(protoLogParam4), java.lang.Boolean.valueOf(protoLogParam5), java.lang.Boolean.valueOf(protoLogParam6), java.lang.Boolean.valueOf(protoLogParam7), java.lang.Boolean.valueOf(protoLogParam8), protoLogParam9);
            }
            boolean wasVisible = false;
            if (this.mHasSurface && this.mToken.okToAnimate()) {
                wasVisible = isVisible();
                boolean allowExitAnimation = (displayContent.inTransition() || inRelaunchingActivity()) ? false : true;
                if (wasVisible && isDisplayed()) {
                    int transit = startingWindow ? 5 : 2;
                    if (allowExitAnimation && this.mWinAnimator.applyAnimationLocked(transit, false)) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[1]) {
                            java.lang.String protoLogParam05 = java.lang.String.valueOf(this);
                            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, 7789778354950913237L, 0, null, protoLogParam05);
                        }
                        if (startingWindow && this.mSurfaceAnimator.hasLeash()) {
                            getPendingTransaction().setLayer(this.mSurfaceAnimator.mLeash, Integer.MAX_VALUE);
                        }
                        this.mAnimatingExit = true;
                        setDisplayLayoutNeeded();
                        this.mWmService.requestTraversal();
                    }
                    if (this.mWmService.mAccessibilityController.hasCallbacks()) {
                        this.mWmService.mAccessibilityController.onWindowTransition(this, transit);
                    }
                }
                boolean isAnimating = allowExitAnimation && (this.mAnimatingExit || isAnimationRunningSelfOrParent());
                boolean lastWindowIsStartingWindow = startingWindow && this.mActivityRecord != null && this.mActivityRecord.isLastWindow(this);
                if (this.mWinAnimator.getShown() && !lastWindowIsStartingWindow && isAnimating) {
                    this.mAnimatingExit = true;
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
                        java.lang.String protoLogParam06 = java.lang.String.valueOf(this);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -4143841388126586338L, 0, null, protoLogParam06);
                    }
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[1]) {
                        java.lang.String protoLogParam07 = java.lang.String.valueOf(this);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, 4419190702135590390L, 0, null, protoLogParam07);
                    }
                    setupWindowForRemoveOnExit();
                    if (this.mActivityRecord != null) {
                        this.mActivityRecord.updateReportedVisibilityLocked();
                    }
                    return;
                }
            }
            boolean windowProvidesDisplayDecorInsets = providesDisplayDecorInsets();
            removeImmediately();
            boolean needToSendNewConfiguration = wasVisible && displayContent.updateOrientation();
            if (windowProvidesDisplayDecorInsets) {
                needToSendNewConfiguration |= displayContent.getDisplayPolicy().updateDecorInsetsInfo();
            }
            if (needToSendNewConfiguration) {
                displayContent.sendNewConfiguration();
            }
            this.mWmService.updateFocusedWindowLocked(isFocused() ? 4 : 0, true);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    static /* synthetic */ boolean lambda$removeIfPossible$2(com.android.server.wm.WindowState w) {
        if (!w.isSelfAnimating(0, 128)) {
            return false;
        }
        w.cancelAnimation();
        return true;
    }

    private void setupWindowForRemoveOnExit() {
        this.mRemoveOnExit = true;
        setDisplayLayoutNeeded();
        getDisplayContent().getDisplayPolicy().removeWindowLw(this);
        boolean focusChanged = this.mWmService.updateFocusedWindowLocked(3, false);
        this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
        if (focusChanged) {
            getDisplayContent().getInputMonitor().updateInputWindowsLw(false);
        }
    }

    void setHasSurface(boolean hasSurface) {
        this.mHasSurface = hasSurface;
        this.mWindowStateExt.updateWindowState(this, this.mSession, this.mWinAnimator, this.mAttrs.type, hasSurface);
    }

    boolean canBeImeTarget() {
        int fl;
        if (this.mIsImWindow || inPinnedWindowingMode() || this.mAttrs.type == 2036 || this.mWindowStateExt.isMinimizedPocketStudio()) {
            return false;
        }
        boolean windowsAreFocusable = this.mActivityRecord == null || this.mActivityRecord.windowsAreFocusable();
        if (!windowsAreFocusable) {
            return false;
        }
        com.android.server.wm.Task rootTask = getRootTask();
        if (rootTask != null && !rootTask.isFocusable()) {
            return false;
        }
        if (this.mAttrs.type != 3 && (fl = this.mAttrs.flags & 131080) != 0 && fl != 131080) {
            return false;
        }
        if (rootTask != null && this.mActivityRecord != null && this.mTransitionController.isTransientLaunch(this.mActivityRecord)) {
            return false;
        }
        if (this.mWindowStateExt.getInputShowStatus() && getImeInputTarget() != null && getImeInputTarget().isVisible() && getImeInputTarget().mActivityRecord != this.mActivityRecord && this.mActivityRecord != null && this.mActivityRecord.finishing) {
            android.util.Slog.d(TAG, this.mActivityRecord + " is finishing, return");
            return false;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT_METHOD) {
            android.util.Slog.i(TAG, "isVisibleRequestedOrAdding " + this + ": " + isVisibleRequestedOrAdding() + " isVisible: " + (isVisible() && this.mActivityRecord != null && this.mActivityRecord.isVisible()));
            if (!isVisibleRequestedOrAdding()) {
                android.util.Slog.i(TAG, "  mSurfaceController=" + this.mWinAnimator.mSurfaceController + " relayoutCalled=" + this.mRelayoutCalled + " viewVis=" + this.mViewVisibility + " policyVis=" + isVisibleByPolicy() + " policyVisAfterAnim=" + this.mLegacyPolicyVisibilityAfterAnim + " parentHidden=" + isParentWindowHidden() + " exiting=" + this.mAnimatingExit + " destroying=" + this.mDestroying);
                if (this.mActivityRecord != null) {
                    android.util.Slog.i(TAG, "  mActivityRecord.visibleRequested=" + this.mActivityRecord.isVisibleRequested());
                }
            }
        }
        if (this.mWindowStateExt.cannotBeImeTarget()) {
            return false;
        }
        return isVisibleRequestedOrAdding() || (isVisible() && this.mActivityRecord != null && this.mActivityRecord.isVisible());
    }

    void openInputChannel(android.view.InputChannel outInputChannel) {
        if (this.mInputChannel != null) {
            throw new java.lang.IllegalStateException("Window already has an input channel.");
        }
        java.lang.String name = getName();
        this.mInputChannel = this.mWmService.mInputManager.createInputChannel(name);
        this.mInputChannelToken = this.mInputChannel.getToken();
        this.mInputWindowHandle.setToken(this.mInputChannelToken);
        this.mWmService.mInputToWindowMap.put(this.mInputChannelToken, this);
        this.mInputChannel.copyTo(outInputChannel);
    }

    @java.lang.Deprecated
    public boolean transferTouch() {
        return this.mWmService.mInputManager.transferTouch(this.mInputChannelToken, getDisplayId());
    }

    void disposeInputChannel() {
        if (this.mInputChannelToken != null) {
            this.mWmService.mInputManager.removeInputChannel(this.mInputChannelToken);
            this.mWmService.mKeyInterceptionInfoForToken.remove(this.mInputChannelToken);
            this.mWmService.mInputToWindowMap.remove(this.mInputChannelToken);
            this.mInputChannelToken = null;
        }
        if (this.mInputChannel != null) {
            this.mInputChannel.dispose();
            this.mInputChannel = null;
        }
        this.mInputWindowHandle.setToken(null);
    }

    void setDisplayLayoutNeeded() {
        com.android.server.wm.DisplayContent dc = getDisplayContent();
        if (dc != null) {
            dc.setLayoutNeeded();
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void switchUser(int userId) {
        super.switchUser(userId);
        if (showToCurrentUser()) {
            setPolicyVisibilityFlag(2);
            return;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.w(TAG, "user changing, hiding " + this + ", attrs=" + this.mAttrs.type + ", belonging to " + this.mOwnerUid);
        }
        clearPolicyVisibilityFlag(2);
    }

    void getSurfaceTouchableRegion(android.graphics.Region region, android.view.WindowManager.LayoutParams attrs) {
        boolean modal = attrs.isModal();
        if (modal) {
            if (this.mActivityRecord != null) {
                updateRegionForModalActivityWindow(region);
                this.mWindowStateExt.resizeTouchRegionForSpecial(this.mActivityRecord, this.mWindowFrames, region, this);
                this.mWindowStateExt.resizeTouchableRegionForBracketMode(region, this.mActivityRecord, this);
            } else {
                getDisplayContent().getBounds(this.mTmpRect);
                int dw = this.mTmpRect.width();
                int dh = this.mTmpRect.height();
                region.set(-dw, -dh, dw + dw, dh + dh);
            }
            subtractTouchExcludeRegionIfNeeded(region);
        } else {
            getTouchableRegion(region);
        }
        this.mWindowStateExt.resizeTouchableRegionInOplusCompatMode(this, region);
        this.mWindowStateExt.resizeTouchableRegionForBracketPanelWindow(region, this);
        this.mWindowStateExt.resizeExpandTouchRegionForWindowState(region, this, getAttrs());
        android.graphics.Rect frame = this.mWindowFrames.mFrame;
        if ((frame.left != 0 || frame.top != 0) && !this.mWindowStateExt.translateTouchableRegionInOplusCompatMode(this, region)) {
            region.translate(-frame.left, -frame.top);
        }
        if (modal && this.mTouchableInsets == 3) {
            this.mTmpRegion.set(0, 0, frame.right, frame.bottom);
            this.mTmpRegion.op(this.mGivenTouchableRegion, android.graphics.Region.Op.DIFFERENCE);
            region.op(this.mTmpRegion, android.graphics.Region.Op.DIFFERENCE);
        }
        if (this.mInvGlobalScale != 1.0f) {
            region.scale(this.mInvGlobalScale);
        }
    }

    private void adjustRegionInFreefromWindowMode(android.graphics.Rect inOutRect) {
        if (!inFreeformWindowingMode() && !this.mWindowStateExt.checkIfWindowingModeZoom(getWindowingMode())) {
            return;
        }
        android.util.DisplayMetrics displayMetrics = getDisplayContent().getDisplayMetrics();
        int delta = com.android.server.wm.WindowManagerService.dipToPixel(30, displayMetrics);
        inOutRect.inset(-delta, -delta);
    }

    private void updateRegionForModalActivityWindow(android.graphics.Region outRegion) {
        this.mActivityRecord.getLetterboxInnerBounds(this.mTmpRect);
        if (this.mTmpRect.isEmpty()) {
            android.graphics.Rect transformedBounds = this.mActivityRecord.getFixedRotationTransformDisplayBounds();
            if (transformedBounds != null) {
                this.mTmpRect.set(transformedBounds);
            } else {
                com.android.server.wm.TaskFragment taskFragment = getTaskFragment();
                if (taskFragment != null) {
                    taskFragment.getDimBounds(this.mTmpRect);
                } else if (getRootTask() != null) {
                    getRootTask().getDimBounds(this.mTmpRect);
                }
            }
        }
        adjustRegionInFreefromWindowMode(this.mTmpRect);
        getWrapper().getExtImpl().adjustTouchableRegionInActivityEmbedding(this, this.mTmpRect);
        outRegion.set(this.mTmpRect);
        cropRegionToRootTaskBoundsIfNeeded(outRegion);
    }

    void checkPolicyVisibilityChange() {
        if (isLegacyPolicyVisibility() != this.mLegacyPolicyVisibilityAfterAnim) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG, "Policy visibility changing after anim in " + this.mWinAnimator + ": " + this.mLegacyPolicyVisibilityAfterAnim);
            }
            if (this.mLegacyPolicyVisibilityAfterAnim) {
                setPolicyVisibilityFlag(1);
            } else {
                clearPolicyVisibilityFlag(1);
            }
            if (!isVisibleByPolicy()) {
                this.mWinAnimator.hide(getPendingTransaction(), "checkPolicyVisibilityChange");
                if (isFocused()) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[2]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -6167820560758523840L, 0, null, null);
                    }
                    this.mWmService.mFocusMayChange = true;
                }
                setDisplayLayoutNeeded();
                this.mWmService.enableScreenIfNeededLocked();
            }
        }
    }

    void setRequestedSize(int requestedWidth, int requestedHeight) {
        if (this.mRequestedWidth != requestedWidth || this.mRequestedHeight != requestedHeight) {
            this.mLayoutNeeded = true;
            this.mRequestedWidth = requestedWidth;
            this.mRequestedHeight = requestedHeight;
        }
    }

    void prepareWindowToDisplayDuringRelayout(boolean wasVisible) {
        boolean hasTurnScreenOnFlag = (this.mAttrs.flags & 2097152) != 0 || (this.mActivityRecord != null && this.mActivityRecord.canTurnScreenOn());
        if (hasTurnScreenOnFlag) {
            boolean allowTheaterMode = this.mWmService.mAllowTheaterModeWakeFromLayout || android.provider.Settings.Global.getInt(this.mWmService.mContext.getContentResolver(), "theater_mode_on", 0) == 0;
            boolean canTurnScreenOn = this.mActivityRecord == null || this.mActivityRecord.currentLaunchCanTurnScreenOn();
            if (allowTheaterMode && canTurnScreenOn && (this.mWmService.mAtmService.isDreaming() || !this.mWmService.mPowerManager.isInteractive())) {
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY || com.android.server.wm.WindowManagerDebugConfig.DEBUG_POWER) {
                    android.util.Slog.v(TAG, "Relayout window turning screen on: " + this);
                }
                this.mWindowStateExt.wakeupInPrepareWindowToDisplayDuringRelayout(this.mAttrs.getTitle() != null ? this.mAttrs.getTitle().toString() : null);
            } else {
                this.mWindowStateExt.setSimultaneousDisplayState(true);
            }
            if (this.mActivityRecord != null) {
                this.mActivityRecord.setCurrentLaunchCanTurnScreenOn(false);
            }
        }
        if (wasVisible) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG, "Already visible and does not turn on screen, skip preparing: " + this);
            }
        } else {
            if ((this.mAttrs.softInputMode & com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED) == 16) {
                this.mLayoutNeeded = true;
            }
            if (isDrawn() && this.mToken.okToAnimate()) {
                this.mWinAnimator.applyEnterAnimationLocked();
            }
        }
    }

    private android.content.res.Configuration getProcessGlobalConfiguration() {
        com.android.server.wm.WindowState parentWindow = getParentWindow();
        com.android.server.wm.Session session = parentWindow != null ? parentWindow.mSession : this.mSession;
        return session.mPid == com.android.server.wm.WindowManagerService.MY_PID ? this.mWmService.mRoot.getConfiguration() : session.mProcess.getConfiguration();
    }

    private android.content.res.Configuration getLastReportedConfiguration() {
        return this.mLastReportedConfiguration.getMergedConfiguration();
    }

    void adjustStartingWindowFlags() {
        if (this.mAttrs.type == 1 && this.mActivityRecord != null && this.mActivityRecord.mStartingWindow != null) {
            android.view.WindowManager.LayoutParams sa = this.mActivityRecord.mStartingWindow.mAttrs;
            sa.flags = (sa.flags & (-4718594)) | (this.mAttrs.flags & 4718593);
        }
    }

    void setWindowScale(int requestedWidth, int requestedHeight) {
        float f;
        boolean scaledWindow = (this.mAttrs.flags & 16384) != 0;
        float f2 = 1.0f;
        if (scaledWindow) {
            if (this.mAttrs.width == requestedWidth) {
                f = 1.0f;
            } else {
                f = this.mAttrs.width / requestedWidth;
            }
            this.mHScale = f;
            if (this.mAttrs.height != requestedHeight) {
                f2 = this.mAttrs.height / requestedHeight;
            }
            this.mVScale = f2;
            return;
        }
        this.mVScale = 1.0f;
        this.mHScale = 1.0f;
    }

    boolean canReceiveKeys() {
        return canReceiveKeys(false);
    }

    public java.lang.String canReceiveKeysReason(boolean fromUserTouch) {
        boolean z = false;
        java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("fromTouch= ").append(fromUserTouch).append(" isVisibleRequestedOrAdding=").append(isVisibleRequestedOrAdding()).append(" mViewVisibility=").append(this.mViewVisibility).append(" mRemoveOnExit=").append(this.mRemoveOnExit).append(" flags=").append(this.mAttrs.flags).append(" appWindowsAreFocusable=").append(this.mActivityRecord == null || this.mActivityRecord.windowsAreFocusable(fromUserTouch)).append(" canReceiveTouchInput=").append(canReceiveTouchInput()).append(" displayIsOnTop=").append(getDisplayContent().isOnTop()).append(" displayIsTrusted=").append(getDisplayContent().isTrusted()).append(" transitShouldKeepFocus=");
        if (this.mActivityRecord != null && this.mTransitionController.shouldKeepFocus(this.mActivityRecord)) {
            z = true;
        }
        return sbAppend.append(z).toString();
    }

    public boolean canReceiveKeys(boolean fromUserTouch) {
        if (this.mActivityRecord != null && this.mTransitionController.shouldKeepFocus(this.mActivityRecord)) {
            return true;
        }
        boolean canReceiveKeys = isVisibleRequestedOrAdding() && this.mViewVisibility == 0 && !this.mRemoveOnExit && (this.mAttrs.flags & 8) == 0 && (this.mActivityRecord == null || this.mActivityRecord.windowsAreFocusable(fromUserTouch)) && (this.mActivityRecord == null || this.mActivityRecord.getTask() == null || !this.mActivityRecord.getTask().getRootTask().shouldIgnoreInput());
        if (!canReceiveKeys) {
            return false;
        }
        if (!this.mWindowStateExt.hookCanReceiveKeys(this.mAttrs.type, this.mWinAnimator)) {
            return fromUserTouch || getDisplayContent().isOnTop() || getDisplayContent().isTrusted() || getDisplayContent().getWrapper().getNonStaticExtImpl().isPuttDisplay() || this.mWindowStateExt.isMirageDisplay(getDisplayId());
        }
        android.util.Slog.v(TAG, "canReceiveKeys: false if in lock device mode and win =" + this + "not show");
        return false;
    }

    @Override // com.android.server.policy.WindowManagerPolicy.WindowState
    public boolean canShowWhenLocked() {
        if (this.mActivityRecord != null) {
            return this.mActivityRecord.canShowWhenLocked();
        }
        return (this.mAttrs.flags & 524288) != 0;
    }

    void applySizeOverride(android.content.res.Configuration newParentConfig, android.content.res.Configuration resolvedConfig) {
        applySizeOverrideIfNeeded(getDisplayContent(), this.mSession.mProcess.mInfo, newParentConfig, resolvedConfig, (this.mAttrs.privateFlags & 67108864) != 0, false, false);
    }

    boolean canReceiveTouchInput() {
        if (this.mActivityRecord == null || this.mActivityRecord.getTask() == null || this.mTransitionController.shouldKeepFocus(this.mActivityRecord)) {
            return true;
        }
        if (this.mWmService.mAtmService.mBackNavigationController.shouldPauseTouch(this.mActivityRecord)) {
            return false;
        }
        return !this.mActivityRecord.getTask().getRootTask().shouldIgnoreInput() && this.mActivityRecord.isVisibleRequested();
    }

    @java.lang.Deprecated
    public boolean hasDrawn() {
        return this.mWinAnimator.mDrawState == 4;
    }

    boolean show(boolean doAnimation, boolean requestAnim) {
        if ((isLegacyPolicyVisibility() && this.mLegacyPolicyVisibilityAfterAnim) || !showToCurrentUser() || !this.mAppOpVisibility || this.mPermanentlyHidden || this.mHiddenWhileSuspended || this.mForceHideNonSystemOverlayWindow) {
            return false;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v(TAG, "Policy visibility true: " + this);
        }
        if (doAnimation) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG, "doAnimation: mPolicyVisibility=" + isLegacyPolicyVisibility() + " animating=" + isAnimating(3));
            }
            if (!this.mToken.okToAnimate()) {
                doAnimation = false;
            } else if (isLegacyPolicyVisibility() && !isAnimating(3)) {
                doAnimation = false;
            }
        }
        setPolicyVisibilityFlag(1);
        this.mLegacyPolicyVisibilityAfterAnim = true;
        if (doAnimation) {
            this.mWinAnimator.applyAnimationLocked(1, true);
        }
        if (requestAnim) {
            this.mWmService.scheduleAnimationLocked();
        }
        if ((this.mAttrs.flags & 8) == 0) {
            this.mWmService.updateFocusedWindowLocked(0, false);
        }
        return true;
    }

    boolean hide(boolean doAnimation, boolean requestAnim) {
        if (doAnimation && !this.mToken.okToAnimate()) {
            doAnimation = false;
        }
        boolean current = doAnimation ? this.mLegacyPolicyVisibilityAfterAnim : isLegacyPolicyVisibility();
        if (!current) {
            return false;
        }
        if (doAnimation && !this.mWinAnimator.applyAnimationLocked(2, false)) {
            doAnimation = false;
        }
        this.mLegacyPolicyVisibilityAfterAnim = false;
        boolean isFocused = isFocused();
        if (!doAnimation) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG, "Policy visibility false: " + this);
            }
            clearPolicyVisibilityFlag(1);
            this.mWmService.enableScreenIfNeededLocked();
            if (isFocused) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[2]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -208079497999140637L, 0, null, null);
                }
                this.mWmService.mFocusMayChange = true;
            }
        }
        if (requestAnim) {
            this.mWmService.scheduleAnimationLocked();
        }
        if (isFocused) {
            this.mWmService.updateFocusedWindowLocked(0, false);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForceHideNonSystemOverlayWindowIfNeeded(boolean forceHide) {
        int baseType = getBaseType();
        if (!this.mSession.mCanAddInternalSystemWindow) {
            if (!android.view.WindowManager.LayoutParams.isSystemAlertWindowType(baseType) && baseType != 2005) {
                return;
            }
            if ((baseType == 2038 && this.mAttrs.isSystemApplicationOverlay() && this.mSession.mCanCreateSystemApplicationOverlay) || this.mWindowStateExt.canOverlayWindows() || this.mForceHideNonSystemOverlayWindow == forceHide) {
                return;
            }
            this.mForceHideNonSystemOverlayWindow = forceHide;
            if (forceHide) {
                hide(true, true);
            } else {
                show(true, true);
            }
        }
    }

    void setHiddenWhileSuspended(boolean hide) {
        if (!this.mOwnerCanAddInternalSystemWindow) {
            if ((!android.view.WindowManager.LayoutParams.isSystemAlertWindowType(this.mAttrs.type) && this.mAttrs.type != 2005) || this.mHiddenWhileSuspended == hide) {
                return;
            }
            this.mHiddenWhileSuspended = hide;
            if (hide) {
                hide(true, true);
            } else {
                show(true, true);
            }
        }
    }

    private void setAppOpVisibilityLw(boolean state) {
        if (!this.mWindowStateExt.canSetAppOpVisibilityLw(getOwningPackage(), getOwningUid())) {
            state = false;
        }
        if (this.mAppOpVisibility != state) {
            this.mAppOpVisibility = state;
            if (state) {
                show(true, true);
            } else {
                hide(true, true);
            }
        }
    }

    void initAppOpsState() {
        if (this.mAppOp == -1 || !this.mAppOpVisibility) {
            return;
        }
        if (!this.mWindowStateExt.canInitAppOpVisibilityLw(getOwningPackage(), getOwningUid(), this.mSession.mPid)) {
            setAppOpVisibilityLw(false);
            return;
        }
        int mode = this.mWmService.mAppOps.startOpNoThrow(this.mAppOp, getOwningUid(), getOwningPackage(), true, null, "init-default-visibility");
        if (mode != 0 && mode != 3) {
            setAppOpVisibilityLw(false);
        }
    }

    void resetAppOpsState() {
        if (this.mAppOp != -1 && this.mAppOpVisibility) {
            this.mWmService.mAppOps.finishOp(this.mAppOp, getOwningUid(), getOwningPackage(), (java.lang.String) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAppOpsState() {
        if (this.mAppOp == -1) {
            return;
        }
        int uid = getOwningUid();
        java.lang.String packageName = getOwningPackage();
        if (this.mAppOpVisibility) {
            int mode = this.mWmService.mAppOps.checkOpNoThrow(this.mAppOp, uid, packageName);
            if (mode != 0 && mode != 3) {
                this.mWmService.mAppOps.finishOp(this.mAppOp, uid, packageName, (java.lang.String) null);
                setAppOpVisibilityLw(false);
                return;
            }
            return;
        }
        int mode2 = this.mWmService.mAppOps.startOpNoThrow(this.mAppOp, uid, packageName, true, null, "attempt-to-be-visible");
        if (mode2 == 0 || mode2 == 3) {
            setAppOpVisibilityLw(true);
        }
    }

    public void hidePermanentlyLw() {
        if (!this.mPermanentlyHidden) {
            this.mPermanentlyHidden = true;
            hide(true, true);
        }
    }

    public void pokeDrawLockLw(long timeout) {
        if (isVisibleRequestedOrAdding()) {
            if (this.mDrawLock == null) {
                java.lang.CharSequence tag = getWindowTag();
                this.mDrawLock = this.mWmService.mPowerManager.newWakeLock(128, "Window:" + ((java.lang.Object) tag));
                this.mDrawLock.setReferenceCounted(false);
                this.mDrawLock.setWorkSource(new android.os.WorkSource(this.mOwnerUid, this.mAttrs.packageName));
            }
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_POWER) {
                android.util.Slog.d(TAG, "pokeDrawLock: poking draw lock on behalf of visible window owned by " + this.mAttrs.packageName);
            }
            this.mDrawLock.acquire(timeout);
            return;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_POWER) {
            android.util.Slog.d(TAG, "pokeDrawLock: suppressed draw lock request for invisible window owned by " + this.mAttrs.packageName);
        }
    }

    boolean isAlive() {
        return this.mClient.asBinder().isBinderAlive();
    }

    @Override // com.android.server.wm.WindowContainer
    void sendAppVisibilityToClients() {
        super.sendAppVisibilityToClients();
        boolean clientVisible = this.mToken.isClientVisible();
        if (this.mAttrs.type == 3 && !clientVisible) {
            return;
        }
        try {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                android.util.Slog.v(TAG, "Setting visibility of " + this + ": " + clientVisible);
            }
            this.mWindowStateExt.hookSetBinderUxFlag(-1, 1);
            this.mClient.dispatchAppVisibility(clientVisible);
            this.mWindowStateExt.hookSetBinderUxFlag(-1, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Exception thrown during dispatchAppVisibility " + this, e);
            if (android.os.Process.getUidForPid(this.mSession.mPid) == this.mSession.mUid) {
                android.os.Process.killProcess(this.mSession.mPid);
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
    void onStartFreezingScreen() {
        if (this.mWindowStateExt.shouldSkipFreezingWhenFolding(this)) {
            return;
        }
        this.mAppFreezing = true;
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState c = (com.android.server.wm.WindowState) this.mChildren.get(i);
            c.onStartFreezingScreen();
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
    boolean onStopFreezingScreen() {
        boolean unfrozeWindows = false;
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState c = (com.android.server.wm.WindowState) this.mChildren.get(i);
            unfrozeWindows |= c.onStopFreezingScreen();
        }
        if (!this.mAppFreezing) {
            return unfrozeWindows;
        }
        this.mAppFreezing = false;
        if (this.mHasSurface && !getOrientationChanging() && this.mWmService.mWindowsFreezingScreen != 2) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 8812513438749898553L, 0, null, protoLogParam0);
            }
            setOrientationChanging(true);
        }
        this.mLastFreezeDuration = 0;
        setDisplayLayoutNeeded();
        return true;
    }

    boolean destroySurface(boolean cleanupOnResume, boolean appStopped) {
        boolean destroyedSomething = false;
        java.util.ArrayList<com.android.server.wm.WindowState> childWindows = new java.util.ArrayList<>(this.mChildren);
        for (int i = childWindows.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState c = childWindows.get(i);
            destroyedSomething |= c.destroySurface(cleanupOnResume, appStopped);
        }
        if (!appStopped && !this.mWindowRemovalAllowed && !cleanupOnResume) {
            return destroyedSomething;
        }
        if (this.mDestroying) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[4]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                boolean protoLogParam2 = this.mWindowRemovalAllowed;
                boolean protoLogParam3 = this.mRemoveOnExit;
                com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, -2964267636425934067L, android.hardware.audio.common.V2_0.AudioChannelMask.IN_6, null, protoLogParam0, java.lang.Boolean.valueOf(appStopped), java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3));
            }
            if (!cleanupOnResume || this.mRemoveOnExit) {
                destroySurfaceUnchecked();
            }
            if (this.mRemoveOnExit) {
                removeImmediately();
            }
            if (cleanupOnResume) {
                requestUpdateWallpaperIfNeeded();
            }
            this.mDestroying = false;
            destroyedSomething = true;
            if (getDisplayContent().mAppTransition.isTransitionSet() && getDisplayContent().mOpeningApps.contains(this.mActivityRecord)) {
                this.mWmService.mWindowPlacerLocked.requestTraversal();
            }
        }
        return destroyedSomething;
    }

    void destroySurfaceUnchecked() {
        this.mWinAnimator.destroySurfaceLocked(this.mTmpTransaction);
        this.mTmpTransaction.apply();
        this.mAnimatingExit = false;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, 7336961102428192483L, 0, null, protoLogParam0);
        }
        this.mWindowStateExt.setHideByKeyguardExitAnim(false);
        if (syncNextBuffer()) {
            immediatelyNotifyBlastSync();
        }
    }

    void onSurfaceShownChanged(boolean shown) {
        if (this.mLastShownChangedReported == shown) {
            return;
        }
        this.mLastShownChangedReported = shown;
        if (shown) {
            initExclusionRestrictions();
        } else {
            logExclusionRestrictions(0);
            logExclusionRestrictions(1);
            getDisplayContent().removeImeSurfaceByTarget(this);
        }
        if (this.mAttrs.type >= 2000 && this.mAttrs.type != 2005 && this.mAttrs.type != 2030 && ((this.mAttrs.type != 2037 || !isOnVirtualDisplay()) && (this.mAttrs.type != 2037 || !this.mWindowStateExt.inRemapViceDisplay(this)))) {
            this.mWmService.mAtmService.mActiveUids.onNonAppSurfaceVisibilityChanged(this.mOwnerUid, shown);
        }
        this.mWindowStateExt.onNonAppSurfaceVisibilityChanged(shown);
    }

    private boolean isOnVirtualDisplay() {
        return getDisplayContent().mDisplay.getType() == 5;
    }

    private void logExclusionRestrictions(int side) {
        if (!com.android.server.wm.DisplayContent.logsGestureExclusionRestrictions(this) || android.os.SystemClock.uptimeMillis() < this.mLastExclusionLogUptimeMillis[side] + this.mWmService.mConstants.mSystemGestureExclusionLogDebounceTimeoutMillis) {
            return;
        }
        long now = android.os.SystemClock.uptimeMillis();
        long duration = now - this.mLastExclusionLogUptimeMillis[side];
        this.mLastExclusionLogUptimeMillis[side] = now;
        int requested = this.mLastRequestedExclusionHeight[side];
        int granted = this.mLastGrantedExclusionHeight[side];
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED, this.mAttrs.packageName, requested, requested - granted, side + 1, getConfiguration().orientation == 2, false, (int) duration);
    }

    private void initExclusionRestrictions() {
        long now = android.os.SystemClock.uptimeMillis();
        this.mLastExclusionLogUptimeMillis[0] = now;
        this.mLastExclusionLogUptimeMillis[1] = now;
    }

    boolean showForAllUsers() {
        switch (this.mAttrs.type) {
            case 3:
            case 2000:
            case 2001:
            case com.android.server.camera.ICameraServiceProxyExt.MSG_FLOAT_WINDOW_SHOW /* 2002 */:
            case 2007:
            case 2008:
            case 2009:
            case 2017:
            case 2018:
            case 2019:
            case 2020:
            case 2021:
            case 2022:
            case 2024:
            case 2026:
            case 2027:
            case 2030:
            case 2034:
            case 2037:
            case 2039:
            case 2040:
            case 2041:
                break;
            default:
                if ((this.mAttrs.privateFlags & 16) == 0) {
                    return false;
                }
                break;
        }
        return this.mOwnerCanAddInternalSystemWindow;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean showToCurrentUser() {
        com.android.server.wm.WindowState win = getTopParentWindow();
        return (win.mAttrs.type < 2000 && win.mActivityRecord != null && win.mActivityRecord.mShowForAllUsers && win.getFrame().left <= win.getDisplayFrame().left && win.getFrame().top <= win.getDisplayFrame().top && win.getFrame().right >= win.getDisplayFrame().right && win.getFrame().bottom >= win.getDisplayFrame().bottom) || win.showForAllUsers() || this.mWmService.isUserVisible(win.mShowUserId);
    }

    private static void applyInsets(android.graphics.Region outRegion, android.graphics.Rect frame, android.graphics.Rect inset) {
        outRegion.set(frame.left + inset.left, frame.top + inset.top, frame.right - inset.right, frame.bottom - inset.bottom);
    }

    void getTouchableRegion(android.graphics.Region outRegion) {
        android.graphics.Rect frame = this.mWindowFrames.mFrame;
        switch (this.mTouchableInsets) {
            case 1:
                applyInsets(outRegion, frame, this.mGivenContentInsets);
                break;
            case 2:
                applyInsets(outRegion, frame, this.mGivenVisibleInsets);
                break;
            case 3:
                outRegion.set(this.mGivenTouchableRegion);
                if (frame.left != 0 || frame.top != 0) {
                    outRegion.translate(frame.left, frame.top);
                }
                break;
            default:
                outRegion.set(frame);
                break;
        }
        cropRegionToRootTaskBoundsIfNeeded(outRegion);
        subtractTouchExcludeRegionIfNeeded(outRegion);
    }

    void getEffectiveTouchableRegion(android.graphics.Region outRegion) {
        com.android.server.wm.DisplayContent dc = getDisplayContent();
        if (this.mAttrs.isModal() && dc != null) {
            outRegion.set(dc.getBounds());
            if (this.mWindowStateExt.isFlexibleWindowMinimized()) {
                return;
            }
            cropRegionToRootTaskBoundsIfNeeded(outRegion);
            subtractTouchExcludeRegionIfNeeded(outRegion);
            return;
        }
        getTouchableRegion(outRegion);
    }

    private void cropRegionToRootTaskBoundsIfNeeded(android.graphics.Region region) {
        com.android.server.wm.Task rootTask;
        com.android.server.wm.Task task = getTask();
        if (task == null || !task.cropWindowsToRootTaskBounds() || this.mWindowStateExt.isCompactScaledWindowingMode(this) || (rootTask = task.getRootTask()) == null || rootTask.mCreatedByOrganizer) {
            return;
        }
        rootTask.getDimBounds(this.mTmpRect);
        adjustRegionInFreefromWindowMode(this.mTmpRect);
        region.op(this.mTmpRect, android.graphics.Region.Op.INTERSECT);
    }

    private void subtractTouchExcludeRegionIfNeeded(android.graphics.Region touchableRegion) {
        if (this.mTapExcludeRegion.isEmpty()) {
            return;
        }
        android.graphics.Region touchExcludeRegion = android.graphics.Region.obtain();
        getTapExcludeRegion(touchExcludeRegion);
        if (!touchExcludeRegion.isEmpty()) {
            touchableRegion.op(touchExcludeRegion, android.graphics.Region.Op.DIFFERENCE);
        }
        touchExcludeRegion.recycle();
    }

    void reportFocusChangedSerialized(boolean focused) {
        if (this.mFocusCallbacks != null) {
            int N = this.mFocusCallbacks.beginBroadcast();
            for (int i = 0; i < N; i++) {
                android.view.IWindowFocusObserver obs = this.mFocusCallbacks.getBroadcastItem(i);
                if (focused) {
                    try {
                        obs.focusGained(this.mWindowId.asBinder());
                    } catch (android.os.RemoteException e) {
                    }
                } else {
                    obs.focusLost(this.mWindowId.asBinder());
                }
            }
            this.mFocusCallbacks.finishBroadcast();
        }
    }

    @Override // com.android.server.wm.ConfigurationContainer
    public android.content.res.Configuration getConfiguration() {
        if (!registeredForDisplayAreaConfigChanges()) {
            return super.getConfiguration();
        }
        this.mTempConfiguration.setTo(getProcessGlobalConfiguration());
        this.mTempConfiguration.updateFrom(getMergedOverrideConfiguration());
        return this.mTempConfiguration;
    }

    private boolean registeredForDisplayAreaConfigChanges() {
        com.android.server.wm.WindowState parentWindow = getParentWindow();
        com.android.server.wm.Session session = parentWindow != null ? parentWindow.mSession : this.mSession;
        if (session.mPid == com.android.server.wm.WindowManagerService.MY_PID) {
            return false;
        }
        return session.mProcess.registeredForDisplayAreaConfigChanges();
    }

    com.android.server.wm.WindowProcessController getProcess() {
        return this.mSession.mProcess;
    }

    void fillClientWindowFramesAndConfiguration(android.window.ClientWindowFrames outFrames, android.util.MergedConfiguration outMergedConfiguration, android.window.ActivityWindowInfo outActivityWindowInfo, boolean useLatestConfig, boolean relayoutVisible) {
        outFrames.frame.set(this.mWindowFrames.mCompatFrame);
        outFrames.displayFrame.set(this.mWindowFrames.mDisplayFrame);
        if (this.mInvGlobalScale != 1.0f) {
            outFrames.displayFrame.scale(this.mInvGlobalScale);
        }
        if (this.mLayoutAttached) {
            if (outFrames.attachedFrame == null) {
                outFrames.attachedFrame = new android.graphics.Rect();
            }
            outFrames.attachedFrame.set(getParentWindow().getFrame());
            if (this.mInvGlobalScale != 1.0f) {
                outFrames.attachedFrame.scale(this.mInvGlobalScale);
            }
        }
        outFrames.compatScale = getCompatScaleForClient();
        if (this.mLastReportedFrames != outFrames) {
            this.mLastReportedFrames.setTo(outFrames);
        }
        if (useLatestConfig || (relayoutVisible && (this.mActivityRecord == null || this.mActivityRecord.isVisibleRequested()))) {
            android.content.res.Configuration globalConfig = getProcessGlobalConfiguration();
            android.content.res.Configuration overrideConfig = getMergedOverrideConfiguration();
            outMergedConfiguration.setConfiguration(globalConfig, overrideConfig);
            if (outMergedConfiguration != this.mLastReportedConfiguration) {
                this.mLastReportedConfiguration.setTo(outMergedConfiguration);
            }
            if (outActivityWindowInfo != null && this.mLastReportedActivityWindowInfo != null) {
                outActivityWindowInfo.set(this.mActivityRecord.getActivityWindowInfo());
                if (getTask() != null && getTask().getWrapper().getExtImpl().isFlexibleTaskAndHasCaption(getTask())) {
                    outActivityWindowInfo.setTaskScale(getTask().getWrapper().getExtImpl().getScale());
                }
                this.mLastReportedActivityWindowInfo.set(outActivityWindowInfo);
            }
        } else {
            outMergedConfiguration.setTo(this.mLastReportedConfiguration);
            if (outActivityWindowInfo != null && this.mLastReportedActivityWindowInfo != null) {
                outActivityWindowInfo.set(this.mLastReportedActivityWindowInfo);
            }
        }
        this.mLastConfigReportedToClient = true;
    }

    void fillInsetsState(android.view.InsetsState outInsetsState, boolean copySources) {
        outInsetsState.set(getCompatInsetsState(), copySources);
        if (outInsetsState != this.mLastReportedInsetsState) {
            this.mLastReportedInsetsState.set(outInsetsState, false);
        }
    }

    void fillInsetsSourceControls(android.view.InsetsSourceControl.Array outArray, boolean copyControls) {
        android.view.InsetsSourceControl[] controls = getDisplayContent().getInsetsStateController().getControlsForDispatch(this);
        outArray.set(controls, copyControls);
        if (outArray != this.mLastReportedActiveControls) {
            this.mLastReportedActiveControls.setTo(outArray, false);
        }
    }

    void reportResized() {
        if (inRelaunchingActivity()) {
            return;
        }
        if ((shouldCheckTokenVisibleRequested() && !this.mToken.isVisibleRequested()) || this.mWindowStateExt.shouldSkipResizeWindow(this)) {
            return;
        }
        if (android.os.Trace.isTagEnabled(32L)) {
            android.os.Trace.traceBegin(32L, "wm.reportResized_" + ((java.lang.Object) getWindowTag()));
        }
        boolean z = true;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_RESIZE_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mWindowFrames.mCompatFrame);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_RESIZE, -6920306331987525705L, 0, null, protoLogParam0, protoLogParam1);
        }
        boolean drawPending = this.mWinAnimator.mDrawState == 1;
        if (drawPending && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[2]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 2714651498627020992L, 0, null, protoLogParam02);
        }
        this.mDragResizingChangeReported = true;
        this.mWindowFrames.clearReportResizeHints();
        int prevRotation = this.mLastReportedConfiguration.getMergedConfiguration().windowConfiguration.getRotation();
        fillClientWindowFramesAndConfiguration(this.mLastReportedFrames, this.mLastReportedConfiguration, this.mLastReportedActivityWindowInfo, true, false);
        fillInsetsState(this.mLastReportedInsetsState, false);
        boolean syncRedraw = shouldSendRedrawForSync();
        boolean syncWithBuffers = syncRedraw && shouldSyncWithBuffers();
        boolean reportDraw = syncRedraw || drawPending;
        boolean isDragResizeChanged = isDragResizeChanged();
        if (!syncRedraw && !isDragResizeChanged && !this.mWindowStateExt.getDeviceFolding()) {
            z = false;
        }
        boolean forceRelayout = z;
        com.android.server.wm.DisplayContent displayContent = getDisplayContent();
        boolean alwaysConsumeSystemBars = displayContent.getDisplayPolicy().areSystemBarsForcedConsumedLw();
        int displayId = displayContent.getDisplayId();
        if (isDragResizeChanged) {
            setDragResizing();
        }
        boolean isDragResizing = isDragResizing();
        markRedrawForSyncReported();
        if (!this.mIsWallpaper || reportDraw || forceRelayout || this.mWindowStateExt.hasWallpaperFrameOrConfigChanged(this, this.mLastReportedFrames, this.mLastReportedConfiguration)) {
            this.mWindowStateExt.setLastFinishDrawDp(-1);
        }
        if (this.mWindowStateExt.loggingWhenFolding()) {
            android.util.Slog.d(TAG, "FSS_client resizing, window = " + this + ", mLastReportedFrames = " + this.mLastReportedFrames + ", reportDraw = " + reportDraw + ", mLastReportedConfiguration = " + this.mLastReportedConfiguration + ", mLastReportedInsetsState = " + this.mLastReportedInsetsState + ", mLastReportedActivityWindowInfo = " + this.mLastReportedActivityWindowInfo);
        }
        if (com.android.window.flags.Flags.bundleClientTransactionFlag()) {
            getProcess().scheduleClientTransactionItem(android.app.servertransaction.WindowStateResizeItem.obtain(this.mClient, this.mLastReportedFrames, reportDraw, this.mLastReportedConfiguration, this.mLastReportedInsetsState, forceRelayout, alwaysConsumeSystemBars, displayId, syncWithBuffers ? this.mSyncSeqId : -1, isDragResizing, this.mLastReportedActivityWindowInfo));
            onResizePostDispatched(drawPending, prevRotation, displayId);
        } else {
            try {
                this.mClient.resized(this.mLastReportedFrames, reportDraw, this.mLastReportedConfiguration, this.mLastReportedInsetsState, forceRelayout, alwaysConsumeSystemBars, displayId, syncWithBuffers ? this.mSyncSeqId : -1, isDragResizing, this.mLastReportedActivityWindowInfo);
                onResizePostDispatched(drawPending, prevRotation, displayId);
            } catch (android.os.RemoteException e) {
                setOrientationChanging(false);
                this.mLastFreezeDuration = (int) (android.os.SystemClock.elapsedRealtime() - this.mWmService.mDisplayFreezeTime);
                android.util.Slog.w(TAG, "Failed to report 'resized' to " + this + " due to " + e);
            }
        }
        android.os.Trace.traceEnd(32L);
    }

    private void onResizePostDispatched(boolean drawPending, int prevRotation, int displayId) {
        if (drawPending && prevRotation >= 0 && prevRotation != this.mLastReportedConfiguration.getMergedConfiguration().windowConfiguration.getRotation()) {
            this.mOrientationChangeRedrawRequestTime = android.os.SystemClock.elapsedRealtime();
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -5755338358883139945L, 0, null, protoLogParam0);
            }
        }
        if (this.mWmService.mAccessibilityController.hasCallbacks()) {
            this.mWmService.mAccessibilityController.onSomeWindowResizedOrMoved(displayId);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void checkCachedSurfaceBufferRelease() {
        if (this.mWindowStateExt.checkCachedSurfaceBufferRelease(this)) {
            return;
        }
        super.checkCachedSurfaceBufferRelease();
    }

    boolean inRelaunchingActivity() {
        return this.mActivityRecord != null && this.mActivityRecord.isRelaunching();
    }

    boolean isClientLocal() {
        return this.mClient instanceof android.view.IWindow.Stub;
    }

    private void consumeInsetsChange() {
        if (this.mWindowFrames.hasInsetsChanged()) {
            this.mWindowFrames.setInsetsChanged(false);
            com.android.server.wm.WindowManagerService windowManagerService = this.mWmService;
            windowManagerService.mWindowsInsetsChanged--;
            if (this.mWmService.mWindowsInsetsChanged == 0) {
                this.mWmService.mH.removeMessages(66);
            }
        }
    }

    void notifyInsetsChanged() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_INSETS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, -5211036212243647844L, 0, null, protoLogParam0);
        }
        if (!this.mWindowFrames.hasInsetsChanged()) {
            this.mWindowFrames.setInsetsChanged(true);
            this.mWmService.mWindowsInsetsChanged++;
            this.mWmService.mH.removeMessages(66);
            this.mWmService.mH.sendEmptyMessage(66);
        }
        com.android.server.wm.WindowContainer p = getParent();
        if (p != null) {
            p.updateOverlayInsetsState(this);
        }
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public void notifyInsetsControlChanged(int displayId) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_INSETS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, -3186229270467822891L, 0, null, protoLogParam0);
        }
        if (this.mRemoved) {
            return;
        }
        fillInsetsState(this.mLastReportedInsetsState, false);
        fillInsetsSourceControls(this.mLastReportedActiveControls, false);
        if (com.android.window.flags.Flags.insetsControlChangedItem()) {
            getProcess().scheduleClientTransactionItem(android.app.servertransaction.WindowStateInsetsControlChangeItem.obtain(this.mClient, this.mLastReportedInsetsState, this.mLastReportedActiveControls));
            return;
        }
        try {
            this.mClient.insetsControlChanged(this.mLastReportedInsetsState, this.mLastReportedActiveControls);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to deliver inset control state change to w=" + this, e);
        }
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public com.android.server.wm.WindowState getWindow() {
        return this;
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public void showInsets(int types, boolean fromIme, android.view.inputmethod.ImeTracker.Token statsToken) {
        try {
            android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 21);
            this.mClient.showInsets(types, fromIme, statsToken);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to deliver showInsets", e);
            android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 21);
        }
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public void hideInsets(int types, boolean fromIme, android.view.inputmethod.ImeTracker.Token statsToken) {
        try {
            android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 22);
            this.mClient.hideInsets(types, fromIme, statsToken);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to deliver hideInsets", e);
            android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 22);
        }
    }

    @Override // com.android.server.wm.InsetsControlTarget
    public boolean canShowTransient() {
        return (this.mAttrs.insetsFlags.behavior & 2) != 0;
    }

    boolean canBeHiddenByKeyguard() {
        if (this.mActivityRecord != null || this.mWindowStateExt.isOnMirageDisplay(this)) {
            return false;
        }
        switch (this.mAttrs.type) {
            case 2000:
            case 2013:
            case 2019:
            case 2040:
                break;
            default:
                if (this.mPolicy.getWindowLayerLw(this) < this.mPolicy.getWindowLayerFromTypeLw(2040)) {
                }
                break;
        }
        return false;
    }

    private int getRootTaskId() {
        com.android.server.wm.Task rootTask = getRootTask();
        if (rootTask == null) {
            return -1;
        }
        return rootTask.mTaskId;
    }

    public void registerFocusObserver(android.view.IWindowFocusObserver observer) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mFocusCallbacks == null) {
                    this.mFocusCallbacks = new android.os.RemoteCallbackList<>();
                }
                this.mFocusCallbacks.register(observer);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void unregisterFocusObserver(android.view.IWindowFocusObserver observer) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mFocusCallbacks != null) {
                    this.mFocusCallbacks.unregister(observer);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    boolean isFocused() {
        return getDisplayContent().mCurrentFocus == this;
    }

    boolean areAppWindowBoundsLetterboxed() {
        return (this.mActivityRecord == null || isStartingWindowAssociatedToTask() || (!this.mActivityRecord.areBoundsLetterboxed() && !isLetterboxedForDisplayCutout())) ? false : true;
    }

    boolean isLetterboxedForDisplayCutout() {
        if (this.mActivityRecord == null || !this.mWindowFrames.parentFrameWasClippedByDisplayCutout() || this.mAttrs.layoutInDisplayCutoutMode == 3 || !this.mAttrs.isFullscreen() || this.mWindowStateExt.checkIfWindowingModeZoom(getWindowingMode())) {
            return false;
        }
        return !frameCoversEntireAppTokenBounds();
    }

    private boolean frameCoversEntireAppTokenBounds() {
        this.mTmpRect.set(this.mActivityRecord.getBounds());
        this.mTmpRect.intersectUnchecked(this.mWindowFrames.mFrame);
        return this.mActivityRecord.getBounds().equals(this.mTmpRect);
    }

    boolean isFullyTransparentBarAllowed(android.graphics.Rect frame) {
        return this.mActivityRecord == null || this.mActivityRecord.isFullyTransparentBarAllowed(frame);
    }

    boolean isDragResizeChanged() {
        return this.mDragResizing != computeDragResizing();
    }

    @Override // com.android.server.wm.WindowContainer
    void resetDragResizingChangeReported() {
        this.mDragResizingChangeReported = false;
        super.resetDragResizingChangeReported();
    }

    private boolean computeDragResizing() {
        com.android.server.wm.Task task = getTask();
        if (task == null) {
            return false;
        }
        if ((!inFreeformWindowingMode() && !task.getRootTask().mCreatedByOrganizer) || task.getActivityType() == 2 || this.mAttrs.width != -1 || this.mAttrs.height != -1 || !task.isDragResizing()) {
            return false;
        }
        return true;
    }

    void setDragResizing() {
        boolean resizing = computeDragResizing();
        if (resizing == this.mDragResizing) {
            return;
        }
        this.mDragResizing = resizing;
    }

    boolean isDragResizing() {
        return this.mDragResizing;
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        boolean isVisible = isVisible();
        if (logLevel == 2 && !isVisible) {
            return;
        }
        long token = proto.start(fieldId);
        super.dumpDebug(proto, 1146756268033L, logLevel);
        proto.write(1120986464259L, getDisplayId());
        proto.write(1120986464260L, getRootTaskId());
        this.mAttrs.dumpDebug(proto, 1146756268037L);
        this.mGivenContentInsets.dumpDebug(proto, 1146756268038L);
        this.mWindowFrames.dumpDebug(proto, 1146756268073L);
        this.mAttrs.surfaceInsets.dumpDebug(proto, 1146756268044L);
        android.graphics.GraphicsProtos.dumpPointProto(this.mSurfacePosition, proto, 1146756268048L);
        this.mWinAnimator.dumpDebug(proto, 1146756268045L);
        proto.write(1133871366158L, this.mAnimatingExit);
        proto.write(1120986464274L, this.mRequestedWidth);
        proto.write(1120986464275L, this.mRequestedHeight);
        proto.write(1120986464276L, this.mViewVisibility);
        proto.write(1133871366166L, this.mHasSurface);
        proto.write(1133871366167L, isReadyForDisplay());
        proto.write(1133871366178L, this.mRemoveOnExit);
        proto.write(1133871366179L, this.mDestroying);
        proto.write(1133871366180L, this.mRemoved);
        proto.write(1133871366181L, isOnScreen());
        proto.write(1133871366182L, isVisible);
        proto.write(1133871366183L, this.mPendingSeamlessRotate != null);
        proto.write(1133871366186L, this.mForceSeamlesslyRotate);
        proto.write(1133871366187L, hasCompatScale());
        proto.write(1108101562412L, this.mGlobalScale);
        proto.write(1120986464304L, this.mRequestedVisibleTypes);
        for (android.graphics.Rect r : this.mKeepClearAreas) {
            r.dumpDebug(proto, 2246267895853L);
        }
        for (android.graphics.Rect r2 : this.mUnrestrictedKeepClearAreas) {
            r2.dumpDebug(proto, 2246267895854L);
        }
        if (this.mMergedLocalInsetsSources != null) {
            for (int i = 0; i < this.mMergedLocalInsetsSources.size(); i++) {
                this.mMergedLocalInsetsSources.valueAt(i).dumpDebug(proto, 2246267895855L);
            }
        }
        proto.end(token);
    }

    @Override // com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268040L;
    }

    @Override // com.android.server.wm.WindowContainer
    public void writeIdentifierToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, java.lang.System.identityHashCode(this));
        proto.write(1120986464258L, this.mShowUserId);
        java.lang.CharSequence title = getWindowTag();
        if (title != null) {
            proto.write(1138166333443L, title.toString());
        }
        proto.end(token);
    }

    @Override // com.android.server.wm.WindowContainer
    @dalvik.annotation.optimization.NeverCompile
    void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        pw.print(prefix + "mDisplayId=" + getDisplayId());
        if (getRootTask() != null) {
            pw.print(" rootTaskId=" + getRootTaskId());
        }
        pw.println(" mSession=" + this.mSession + " mClient=" + this.mClient.asBinder());
        pw.println(prefix + "mOwnerUid=" + this.mOwnerUid + " showForAllUsers=" + showForAllUsers() + " package=" + this.mAttrs.packageName + " appop=" + android.app.AppOpsManager.opToName(this.mAppOp));
        pw.println(prefix + "mAttrs=" + this.mAttrs.toString(prefix));
        pw.println(prefix + "Requested w=" + this.mRequestedWidth + " h=" + this.mRequestedHeight + " mLayoutSeq=" + this.mLayoutSeq);
        if (this.mRequestedWidth != this.mLastRequestedWidth || this.mRequestedHeight != this.mLastRequestedHeight) {
            pw.println(prefix + "LastRequested w=" + this.mLastRequestedWidth + " h=" + this.mLastRequestedHeight);
        }
        if (this.mIsChildWindow || this.mLayoutAttached) {
            pw.println(prefix + "mParentWindow=" + getParentWindow() + " mLayoutAttached=" + this.mLayoutAttached);
        }
        if (this.mIsImWindow || this.mIsWallpaper || this.mIsFloatingLayer) {
            pw.println(prefix + "mIsImWindow=" + this.mIsImWindow + " mIsWallpaper=" + this.mIsWallpaper + " mIsFloatingLayer=" + this.mIsFloatingLayer);
        }
        if (dumpAll) {
            pw.print(prefix);
            pw.print("mBaseLayer=");
            pw.print(this.mBaseLayer);
            pw.print(" mSubLayer=");
            pw.print(this.mSubLayer);
        }
        if (dumpAll) {
            pw.println(prefix + "mToken=" + this.mToken);
            if (this.mActivityRecord != null) {
                pw.println(prefix + "mActivityRecord=" + this.mActivityRecord);
                pw.print(prefix + "drawnStateEvaluated=" + getDrawnStateEvaluated());
                pw.println(prefix + "mightAffectAllDrawn=" + mightAffectAllDrawn());
            }
            pw.println(prefix + "mViewVisibility=0x" + java.lang.Integer.toHexString(this.mViewVisibility) + " mHaveFrame=" + this.mHaveFrame + " mObscured=" + this.mObscured);
            if (this.mDisableFlags != 0) {
                pw.println(prefix + "mDisableFlags=" + android.view.ViewDebug.flagsToString(android.view.View.class, "mSystemUiVisibility", this.mDisableFlags));
            }
        }
        if (!isVisibleByPolicy() || !this.mLegacyPolicyVisibilityAfterAnim || !this.mAppOpVisibility || isParentWindowHidden() || this.mPermanentlyHidden || this.mForceHideNonSystemOverlayWindow || this.mHiddenWhileSuspended) {
            pw.println(prefix + "mPolicyVisibility=" + isVisibleByPolicy() + " mLegacyPolicyVisibilityAfterAnim=" + this.mLegacyPolicyVisibilityAfterAnim + " mAppOpVisibility=" + this.mAppOpVisibility + " parentHidden=" + isParentWindowHidden() + " mPermanentlyHidden=" + this.mPermanentlyHidden + " mHiddenWhileSuspended=" + this.mHiddenWhileSuspended + " mForceHideNonSystemOverlayWindow=" + this.mForceHideNonSystemOverlayWindow);
        }
        if (!this.mRelayoutCalled || this.mLayoutNeeded) {
            pw.println(prefix + "mRelayoutCalled=" + this.mRelayoutCalled + " mLayoutNeeded=" + this.mLayoutNeeded);
        }
        if (dumpAll) {
            pw.println(prefix + "mGivenContentInsets=" + this.mGivenContentInsets.toShortString(sTmpSB) + " mGivenVisibleInsets=" + this.mGivenVisibleInsets.toShortString(sTmpSB));
            if (this.mTouchableInsets != 0 || this.mGivenInsetsPending) {
                pw.println(prefix + "mTouchableInsets=" + this.mTouchableInsets + " mGivenInsetsPending=" + this.mGivenInsetsPending);
                android.graphics.Region region = new android.graphics.Region();
                getTouchableRegion(region);
                pw.println(prefix + "touchable region=" + region);
            }
            pw.println(prefix + "mFullConfiguration=" + getConfiguration());
            pw.println(prefix + "mLastReportedConfiguration=" + getLastReportedConfiguration());
            if (this.mLastReportedActivityWindowInfo != null) {
                pw.println(prefix + "mLastReportedActivityWindowInfo=" + this.mLastReportedActivityWindowInfo);
            }
        }
        pw.println(prefix + "mHasSurface=" + this.mHasSurface + " isReadyForDisplay()=" + isReadyForDisplay() + " mWindowRemovalAllowed=" + this.mWindowRemovalAllowed);
        if (this.mIsSurfacePositionPaused) {
            pw.println(prefix + "mIsSurfacePositionPaused=true");
        }
        if (this.mInvGlobalScale != 1.0f) {
            pw.println(prefix + "mCompatFrame=" + this.mWindowFrames.mCompatFrame.toShortString(sTmpSB));
        }
        if (dumpAll) {
            this.mWindowFrames.dump(pw, prefix);
            pw.println(prefix + " surface=" + this.mAttrs.surfaceInsets.toShortString(sTmpSB));
        }
        super.dump(pw, prefix, dumpAll);
        pw.println(prefix + this.mWinAnimator + ":");
        this.mWinAnimator.dump(pw, prefix + "  ", dumpAll);
        if (this.mAnimatingExit || this.mRemoveOnExit || this.mDestroying || this.mRemoved) {
            pw.println(prefix + "mAnimatingExit=" + this.mAnimatingExit + " mRemoveOnExit=" + this.mRemoveOnExit + " mDestroying=" + this.mDestroying + " mRemoved=" + this.mRemoved);
        }
        if (getOrientationChanging() || this.mAppFreezing) {
            pw.println(prefix + "mOrientationChanging=" + this.mOrientationChanging + " configOrientationChanging=" + (getLastReportedConfiguration().orientation != getConfiguration().orientation) + " mAppFreezing=" + this.mAppFreezing);
        }
        if (this.mLastFreezeDuration != 0) {
            pw.print(prefix + "mLastFreezeDuration=");
            android.util.TimeUtils.formatDuration(this.mLastFreezeDuration, pw);
            pw.println();
        }
        pw.print(prefix + "mForceSeamlesslyRotate=" + this.mForceSeamlesslyRotate + " seamlesslyRotate: pending=");
        if (this.mPendingSeamlessRotate != null) {
            this.mPendingSeamlessRotate.dump(pw);
        } else {
            pw.print("null");
        }
        pw.println();
        if (this.mXOffset != 0 || this.mYOffset != 0) {
            pw.println(prefix + "mXOffset=" + this.mXOffset + " mYOffset=" + this.mYOffset);
        }
        if (this.mHScale != 1.0f || this.mVScale != 1.0f) {
            pw.println(prefix + "mHScale=" + this.mHScale + " mVScale=" + this.mVScale);
        }
        if (this.mWallpaperX != -1.0f || this.mWallpaperY != -1.0f) {
            pw.println(prefix + "mWallpaperX=" + this.mWallpaperX + " mWallpaperY=" + this.mWallpaperY);
        }
        pw.println(prefix + "mXOffset=" + this.mXOffset + " mYOffset=" + this.mYOffset);
        pw.println(prefix + "mSeamlesslyRotated=" + this.mSeamlesslyRotated);
        pw.println(prefix + "mSyncSeqId=" + this.mSyncSeqId);
        pw.println(prefix + "mSyncState=" + this.mSyncState);
        if (this.mWallpaperXStep != -1.0f || this.mWallpaperYStep != -1.0f) {
            pw.println(prefix + "mWallpaperXStep=" + this.mWallpaperXStep + " mWallpaperYStep=" + this.mWallpaperYStep);
        }
        if (this.mIsWallpaper) {
            pw.println(prefix + "mShouldScaleWallpaper=" + this.mShouldScaleWallpaper);
            pw.println(prefix + "mWallpaperScale=" + this.mWallpaperScale);
        }
        if (this.mWallpaperZoomOut != -1.0f) {
            pw.println(prefix + "mWallpaperZoomOut=" + this.mWallpaperZoomOut);
        }
        if (this.mWallpaperDisplayOffsetX != Integer.MIN_VALUE || this.mWallpaperDisplayOffsetY != Integer.MIN_VALUE) {
            pw.println(prefix + "mWallpaperDisplayOffsetX=" + this.mWallpaperDisplayOffsetX + " mWallpaperDisplayOffsetY=" + this.mWallpaperDisplayOffsetY);
        }
        if (this.mDrawLock != null) {
            pw.println(prefix + "mDrawLock=" + this.mDrawLock);
        }
        if (isDragResizing()) {
            pw.println(prefix + "isDragResizing=" + isDragResizing());
        }
        if (computeDragResizing()) {
            pw.println(prefix + "computeDragResizing=" + computeDragResizing());
        }
        if (this.mImeInsetsConsumed) {
            pw.println(prefix + "mImeInsetsConsumed=true");
        }
        pw.println(prefix + "isOnScreen=" + isOnScreen());
        pw.println(prefix + "isVisible=" + isVisible());
        pw.println(prefix + "keepClearAreas: restricted=" + this.mKeepClearAreas + ", unrestricted=" + this.mUnrestrictedKeepClearAreas);
        if (dumpAll && this.mRequestedVisibleTypes != android.view.WindowInsets.Type.defaultVisible()) {
            pw.println(prefix + "Requested non-default-visibility types: " + android.view.WindowInsets.Type.toString(this.mRequestedVisibleTypes ^ android.view.WindowInsets.Type.defaultVisible()));
        }
        pw.println(prefix + "mPrepareSyncSeqId=" + this.mPrepareSyncSeqId);
    }

    @Override // com.android.server.wm.ConfigurationContainer
    java.lang.String getName() {
        return java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + ((java.lang.Object) getWindowTag());
    }

    java.lang.CharSequence getWindowTag() {
        java.lang.CharSequence tag = this.mAttrs.getTitle();
        if (tag == null || tag.length() <= 0) {
            return this.mAttrs.packageName;
        }
        return tag;
    }

    public java.lang.String toString() {
        java.lang.CharSequence title = getWindowTag();
        if (this.mStringNameCache == null || this.mLastTitle != title || this.mWasExiting != this.mAnimatingExit) {
            this.mLastTitle = title;
            this.mWasExiting = this.mAnimatingExit;
            boolean canShowWhenLocked = false;
            if (this.mIsWallpaper) {
                com.android.server.wm.WallpaperWindowToken wToken = this.mToken.asWallpaperToken();
                canShowWhenLocked = wToken != null && wToken.canShowWhenLocked();
            }
            this.mStringNameCache = "Window{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " u" + this.mShowUserId + (this.mIsWallpaper ? " " + canShowWhenLocked : "") + " " + ((java.lang.Object) this.mLastTitle) + (this.mAnimatingExit ? " EXITING}" : "}");
        }
        return this.mStringNameCache;
    }

    boolean isChildWindow() {
        return this.mIsChildWindow;
    }

    boolean hideNonSystemOverlayWindowsWhenVisible() {
        return (this.mAttrs.privateFlags & 524288) != 0 && this.mSession.mCanHideNonSystemOverlayWindows;
    }

    com.android.server.wm.WindowState getParentWindow() {
        if (this.mIsChildWindow) {
            return (com.android.server.wm.WindowState) super.getParent();
        }
        return null;
    }

    com.android.server.wm.WindowState getTopParentWindow() {
        com.android.server.wm.WindowState current = this;
        com.android.server.wm.WindowState topParent = current;
        while (current != null && current.mIsChildWindow) {
            current = current.getParentWindow();
            if (current != null) {
                topParent = current;
            }
        }
        return topParent;
    }

    boolean isParentWindowHidden() {
        com.android.server.wm.WindowState parent = getParentWindow();
        return parent != null && parent.mHidden;
    }

    private boolean isParentWindowGoneForLayout() {
        com.android.server.wm.WindowState parent = getParentWindow();
        return parent != null && parent.isGoneForLayout();
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
        com.android.server.wm.DisplayContent dc = getDisplayContent();
        if (dc != null && ((this.mIsWallpaper && !this.mLastConfigReportedToClient) || hasWallpaper())) {
            dc.pendingLayoutChanges |= 4;
            dc.setLayoutNeeded();
            this.mWmService.mWindowPlacerLocked.requestTraversal();
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState c = (com.android.server.wm.WindowState) this.mChildren.get(i);
            c.requestUpdateWallpaperIfNeeded();
        }
    }

    float translateToWindowX(float x) {
        float winX = x - this.mWindowFrames.mFrame.left;
        if (this.mGlobalScale != 1.0f) {
            return winX * this.mInvGlobalScale;
        }
        return winX;
    }

    float translateToWindowY(float y) {
        float winY = y - this.mWindowFrames.mFrame.top;
        if (this.mGlobalScale != 1.0f) {
            return winY * this.mInvGlobalScale;
        }
        return winY;
    }

    int getRotationAnimationHint() {
        if (this.mActivityRecord != null) {
            return this.mActivityRecord.mRotationAnimationHint;
        }
        return -1;
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
    boolean commitFinishDrawing(android.view.SurfaceControl.Transaction t) {
        boolean committed = this.mWinAnimator.commitFinishDrawingLocked();
        if (committed) {
            this.mWinAnimator.prepareSurfaceLocked(t);
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            committed |= ((com.android.server.wm.WindowState) this.mChildren.get(i)).commitFinishDrawing(t);
        }
        if (getAnimationLeash() != null) {
            t.merge(getSyncTransaction());
        }
        return committed;
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
    boolean performShowLocked() {
        if (!showToCurrentUser()) {
            android.util.Slog.w(TAG, "hiding " + this + ", belonging to " + this.mOwnerUid);
            clearPolicyVisibilityFlag(2);
            return false;
        }
        logPerformShow("performShow on ");
        int drawState = this.mWinAnimator.mDrawState;
        if ((drawState == 4 || drawState == 3) && this.mActivityRecord != null) {
            if (this.mAttrs.type != 3 && !this.mWindowStateExt.shouldDeferCallOnFirstWindowDrawn(this)) {
                this.mActivityRecord.onFirstWindowDrawn(this);
            } else if (this.mAttrs.type == 3) {
                this.mActivityRecord.onStartingWindowDrawn();
            } else {
                android.util.Slog.i(TAG, "defer call onFirstWindowDrawn until mainwindow drawn:" + this);
            }
        }
        if (this.mWinAnimator.mDrawState != 3 || !isReadyForDisplay() || this.mWindowStateExt.shouldSkipShowWindow(this)) {
            return false;
        }
        logPerformShow("Showing ");
        this.mWmService.enableScreenIfNeededLocked();
        this.mWinAnimator.applyEnterAnimationLocked();
        this.mWinAnimator.mLastAlpha = -1.0f;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -7413136364930452718L, 0, null, protoLogParam0);
        }
        this.mWindowStateWrapper.getExtImpl().cancelFlexibleAppInnerScreenAnimationIfNeed(this);
        this.mWindowStateExt.onWindowStateHasDrawn(this);
        this.mWinAnimator.printWindowState(this.mWinAnimator.mDrawState, 4, this, "performShowLocked");
        this.mWindowStateWrapper.getExtImpl().performShowLocked(this);
        this.mWinAnimator.mDrawState = 4;
        this.mWmService.scheduleAnimationLocked();
        if (this.mHidden) {
            this.mHidden = false;
            com.android.server.wm.DisplayContent displayContent = getDisplayContent();
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowState c = (com.android.server.wm.WindowState) this.mChildren.get(i);
                if (c.mWinAnimator.mSurfaceController != null) {
                    c.performShowLocked();
                    if (displayContent != null) {
                        displayContent.setLayoutNeeded();
                    }
                }
            }
        }
        if (this.mAttrs.type == 3) {
            this.mWindowStateExt.removeStartingBackColorLayerIfNeed(this);
        }
        return true;
    }

    private void logPerformShow(java.lang.String prefix) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY || (com.android.server.wm.WindowManagerDebugConfig.DEBUG_STARTING_WINDOW_VERBOSE && this.mAttrs.type == 3)) {
            android.util.Slog.v(TAG, prefix + this + ": mDrawState=" + this.mWinAnimator.drawStateToString() + " readyForDisplay=" + isReadyForDisplay() + " starting=" + (this.mAttrs.type == 3) + " during animation: policyVis=" + isVisibleByPolicy() + " parentHidden=" + isParentWindowHidden() + " tok.visibleRequested=" + (this.mActivityRecord != null && this.mActivityRecord.isVisibleRequested()) + " tok.visible=" + (this.mActivityRecord != null && this.mActivityRecord.isVisible()) + " animating=" + isAnimating(3) + " tok animating=" + (this.mActivityRecord != null && this.mActivityRecord.isAnimating(3)) + " Callers=" + android.os.Debug.getCallers(4));
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
    android.view.WindowInfo getWindowInfo() {
        android.view.WindowInfo windowInfo = android.view.WindowInfo.obtain();
        windowInfo.displayId = getDisplayId();
        windowInfo.type = this.mAttrs.type;
        windowInfo.layer = this.mLayer;
        windowInfo.token = this.mClient.asBinder();
        if (this.mActivityRecord != null) {
            windowInfo.activityToken = this.mActivityRecord.token;
        }
        windowInfo.accessibilityIdOfAnchor = this.mAttrs.accessibilityIdOfAnchor;
        windowInfo.focused = isFocused();
        com.android.server.wm.Task task = getTask();
        windowInfo.inPictureInPicture = task != null && task.inPinnedWindowingMode();
        windowInfo.taskId = task == null ? -1 : task.mTaskId;
        windowInfo.hasFlagWatchOutsideTouch = (this.mAttrs.flags & 262144) != 0;
        if (this.mIsChildWindow) {
            windowInfo.parentToken = getParentWindow().mClient.asBinder();
        }
        int childCount = this.mChildren.size();
        if (childCount > 0) {
            if (windowInfo.childTokens == null) {
                windowInfo.childTokens = new java.util.ArrayList(childCount);
            }
            for (int j = 0; j < childCount; j++) {
                com.android.server.wm.WindowState child = (com.android.server.wm.WindowState) this.mChildren.get(j);
                windowInfo.childTokens.add(child.mClient.asBinder());
            }
        }
        return windowInfo;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean forAllWindows(com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> callback, boolean traverseTopToBottom) {
        if (this.mChildren.isEmpty()) {
            return applyInOrderWithImeWindows(callback, traverseTopToBottom);
        }
        if (traverseTopToBottom) {
            return forAllWindowTopToBottom(callback);
        }
        return forAllWindowBottomToTop(callback);
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
    private boolean forAllWindowBottomToTop(com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> callback) {
        com.android.server.wm.WindowState child;
        int i = 0;
        int count = this.mChildren.size();
        java.lang.Object obj = this.mChildren.get(0);
        while (true) {
            child = (com.android.server.wm.WindowState) obj;
            if (i >= count || child.mSubLayer >= 0) {
                break;
            }
            if (child.applyInOrderWithImeWindows(callback, false)) {
                return true;
            }
            i++;
            if (i >= count) {
                break;
            }
            obj = this.mChildren.get(i);
        }
        if (applyInOrderWithImeWindows(callback, false)) {
            return true;
        }
        while (i < count) {
            if (child.applyInOrderWithImeWindows(callback, false)) {
                return true;
            }
            i++;
            if (i >= count) {
                break;
            }
            child = (com.android.server.wm.WindowState) this.mChildren.get(i);
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    void updateAboveInsetsState(final android.view.InsetsState aboveInsetsState, android.util.SparseArray<android.view.InsetsSource> localInsetsSourcesFromParent, final android.util.ArraySet<com.android.server.wm.WindowState> insetsChangedWindows) {
        final android.util.SparseArray<android.view.InsetsSource> mergedLocalInsetsSources = createMergedSparseArray(localInsetsSourcesFromParent, this.mLocalInsetsSources);
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowState$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.WindowState.lambda$updateAboveInsetsState$3(aboveInsetsState, insetsChangedWindows, mergedLocalInsetsSources, (com.android.server.wm.WindowState) obj);
            }
        }, true);
    }

    static /* synthetic */ void lambda$updateAboveInsetsState$3(android.view.InsetsState aboveInsetsState, android.util.ArraySet insetsChangedWindows, android.util.SparseArray mergedLocalInsetsSources, com.android.server.wm.WindowState w) {
        if (!w.mAboveInsetsState.equals(aboveInsetsState)) {
            w.mAboveInsetsState.set(aboveInsetsState);
            insetsChangedWindows.add(w);
        }
        if (!mergedLocalInsetsSources.contentEquals(w.mMergedLocalInsetsSources)) {
            w.mMergedLocalInsetsSources = mergedLocalInsetsSources;
            insetsChangedWindows.add(w);
        }
        android.util.SparseArray<com.android.server.wm.InsetsSourceProvider> providers = w.mInsetsSourceProviders;
        if (providers != null) {
            for (int i = providers.size() - 1; i >= 0; i--) {
                aboveInsetsState.addSource(providers.valueAt(i).getSource());
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
    private boolean forAllWindowTopToBottom(com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> callback) {
        int i = this.mChildren.size() - 1;
        com.android.server.wm.WindowState child = (com.android.server.wm.WindowState) this.mChildren.get(i);
        while (i >= 0 && child.mSubLayer >= 0) {
            if (child.applyInOrderWithImeWindows(callback, true)) {
                return true;
            }
            i--;
            if (i < 0) {
                break;
            }
            child = (com.android.server.wm.WindowState) this.mChildren.get(i);
        }
        if (applyInOrderWithImeWindows(callback, true)) {
            return true;
        }
        while (i >= 0) {
            if (child.applyInOrderWithImeWindows(callback, true)) {
                return true;
            }
            i--;
            if (i >= 0) {
                child = (com.android.server.wm.WindowState) this.mChildren.get(i);
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean applyImeWindowsIfNeeded(com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> callback, boolean traverseTopToBottom) {
        if (!isImeLayeringTarget()) {
            return false;
        }
        com.android.server.wm.WindowState imeInputTarget = getImeInputTarget();
        if (imeInputTarget == null || imeInputTarget.isDrawn() || imeInputTarget.isVisibleRequested()) {
            return this.mDisplayContent.forAllImeWindows(callback, traverseTopToBottom);
        }
        return false;
    }

    private boolean applyInOrderWithImeWindows(com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> callback, boolean traverseTopToBottom) {
        if (traverseTopToBottom) {
            if (applyImeWindowsIfNeeded(callback, traverseTopToBottom) || callback.apply(this)) {
                return true;
            }
            return false;
        }
        if (callback.apply(this) || applyImeWindowsIfNeeded(callback, traverseTopToBottom)) {
            return true;
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
    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.WindowState getWindow(java.util.function.Predicate<com.android.server.wm.WindowState> callback) {
        if (this.mChildren.isEmpty()) {
            if (callback.test(this)) {
                return this;
            }
            return null;
        }
        int i = this.mChildren.size() - 1;
        com.android.server.wm.WindowState child = (com.android.server.wm.WindowState) this.mChildren.get(i);
        while (i >= 0 && child.mSubLayer >= 0) {
            if (callback.test(child)) {
                return child;
            }
            i--;
            if (i < 0) {
                break;
            }
            child = (com.android.server.wm.WindowState) this.mChildren.get(i);
        }
        if (callback.test(this)) {
            return this;
        }
        while (i >= 0) {
            if (callback.test(child)) {
                return child;
            }
            i--;
            if (i < 0) {
                break;
            }
            child = (com.android.server.wm.WindowState) this.mChildren.get(i);
        }
        return null;
    }

    boolean isSelfOrAncestorWindowAnimatingExit() {
        com.android.server.wm.WindowState window = this;
        while (!window.mAnimatingExit) {
            window = window.getParentWindow();
            if (window == null) {
                return false;
            }
        }
        return true;
    }

    boolean isAnimationRunningSelfOrParent() {
        return inTransitionSelfOrParent() || isAnimating(0, 16);
    }

    private boolean shouldFinishAnimatingExit() {
        if (inTransition()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 7624470121297688739L, 0, null, protoLogParam0);
            }
            return false;
        }
        if (!this.mDisplayContent.okToAnimate()) {
            return true;
        }
        if (isAnimationRunningSelfOrParent()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[0]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 810267895099109466L, 0, null, protoLogParam02);
            }
            return false;
        }
        if (!this.mDisplayContent.mWallpaperController.isWallpaperTarget(this)) {
            return true;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[0]) {
            java.lang.String protoLogParam03 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -1760879391350377377L, 0, null, protoLogParam03);
        }
        return false;
    }

    void cleanupAnimatingExitWindow() {
        if (this.mAnimatingExit && shouldFinishAnimatingExit()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[3]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 272960397873328729L, 0, null, protoLogParam0);
            }
            onExitAnimationDone();
        }
    }

    void onExitAnimationDone() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, com.android.internal.protolog.common.LogLevel.VERBOSE)) {
            com.android.server.wm.AnimationAdapter animationAdapter = this.mSurfaceAnimator.getAnimation();
            java.io.StringWriter sw = new java.io.StringWriter();
            if (animationAdapter != null) {
                java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                animationAdapter.dump(pw, "");
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                boolean protoLogParam1 = this.mAnimatingExit;
                boolean protoLogParam2 = this.mRemoveOnExit;
                boolean protoLogParam3 = isAnimating();
                java.lang.String protoLogParam4 = java.lang.String.valueOf(sw);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -1007526574020149845L, android.hardware.audio.common.V2_0.AudioChannelMask.IN_6, null, protoLogParam0, java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3), protoLogParam4);
            }
        }
        if (!this.mChildren.isEmpty()) {
            java.util.ArrayList<com.android.server.wm.WindowState> childWindows = new java.util.ArrayList<>(this.mChildren);
            for (int i = childWindows.size() - 1; i >= 0; i--) {
                childWindows.get(i).onExitAnimationDone();
            }
        }
        if (this.mWinAnimator.mEnteringAnimation) {
            this.mWinAnimator.mEnteringAnimation = false;
            this.mWmService.requestTraversal();
            if (this.mActivityRecord == null) {
                try {
                    this.mClient.dispatchWindowShown();
                } catch (android.os.RemoteException e) {
                }
            }
        }
        if (isAnimating()) {
            return;
        }
        this.mWindowStateExt.putSnapshotWhenStartingWindowExit(this.mAttrs.type, this.mRemoveOnExit, this);
        if (this.mWindowStateExt.getHideByKeyguardExitAnim()) {
            this.mWinAnimator.hide(getPendingTransaction(), "hideByKeyguardExitAnim");
        }
        if (this.mWindowStateExt.hideForUnFolded(this)) {
            this.mWinAnimator.hide(getPendingTransaction(), "hide ScreenRelayWindow for unfolded");
        }
        if (!isSelfOrAncestorWindowAnimatingExit()) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(this);
            boolean protoLogParam12 = this.mRemoveOnExit;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 1738645946553610841L, 12, null, protoLogParam02, java.lang.Boolean.valueOf(protoLogParam12));
        }
        this.mDestroying = true;
        boolean hasSurface = this.mWinAnimator.hasSurface();
        this.mWinAnimator.hide(getPendingTransaction(), "onExitAnimationDone");
        if (this.mActivityRecord != null) {
            if (this.mAttrs.type == 1) {
                this.mActivityRecord.destroySurfaces();
            } else {
                destroySurface(false, this.mActivityRecord.mAppStopped);
            }
        } else if (hasSurface) {
            this.mWmService.mDestroySurface.add(this);
        }
        this.mAnimatingExit = false;
        this.mWindowStateExt.setHideByKeyguardExitAnim(false);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[0]) {
            java.lang.String protoLogParam03 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -7737516306844862315L, 0, null, protoLogParam03);
        }
        getDisplayContent().mWallpaperController.hideWallpapers(this);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean handleCompleteDeferredRemoval() {
        if (this.mRemoveOnExit && !isSelfAnimating(0, 16)) {
            this.mRemoveOnExit = false;
            removeImmediately();
        }
        return super.handleCompleteDeferredRemoval();
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
    boolean clearAnimatingFlags() {
        boolean didSomething = false;
        if (!this.mRemoveOnExit) {
            if (this.mAnimatingExit) {
                this.mAnimatingExit = false;
                this.mWindowStateExt.setHideByKeyguardExitAnim(false);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -3153130647145726082L, 0, null, protoLogParam0);
                }
                didSomething = true;
            }
            if (this.mDestroying) {
                this.mDestroying = false;
                this.mWmService.mDestroySurface.remove(this);
                this.mWmsExt.getDestroySavedSurface().remove(this);
                didSomething = true;
            }
        }
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            didSomething |= ((com.android.server.wm.WindowState) this.mChildren.get(i)).clearAnimatingFlags();
        }
        return didSomething;
    }

    public boolean isRtl() {
        return getConfiguration().getLayoutDirection() == 1;
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
    void updateReportedVisibility(com.android.server.wm.WindowState.UpdateReportedVisibilityResults results) {
        for (int i = this.mChildren.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowState c = (com.android.server.wm.WindowState) this.mChildren.get(i);
            c.updateReportedVisibility(results);
        }
        if (this.mAppFreezing || this.mViewVisibility != 0 || this.mAttrs.type == 3 || this.mDestroying) {
            return;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v(TAG, "Win " + this + ": isDrawn=" + isDrawn() + ", animating=" + isAnimating(3));
            if (!isDrawn()) {
                android.util.Slog.v(TAG, "Not displayed: s=" + this.mWinAnimator.mSurfaceController + " pv=" + isVisibleByPolicy() + " mDrawState=" + this.mWinAnimator.mDrawState + " ph=" + isParentWindowHidden() + " th=" + (this.mActivityRecord != null && this.mActivityRecord.isVisibleRequested()) + " a=" + isAnimating(3));
            }
        }
        results.numInteresting++;
        if (isDrawn()) {
            results.numDrawn++;
            if (!isAnimating(3)) {
                results.numVisible++;
            }
            results.nowGone = false;
            return;
        }
        if (isAnimating(3)) {
            results.nowGone = false;
        }
    }

    boolean surfaceInsetsChanging() {
        return !this.mLastSurfaceInsets.equals(this.mAttrs.surfaceInsets);
    }

    int relayoutVisibleWindow(int result) {
        boolean wasVisible = isVisible();
        int result2 = result | ((wasVisible && isDrawn()) ? 0 : 1);
        if (this.mAnimatingExit) {
            android.util.Slog.d(TAG, "relayoutVisibleWindow: " + this + " mAnimatingExit=true, mRemoveOnExit=" + this.mRemoveOnExit + ", mDestroying=" + this.mDestroying);
            this.mAnimatingExit = false;
            if (isAnimating()) {
                cancelAnimation();
            }
            this.mWindowStateExt.setHideByKeyguardExitAnim(false);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -5202247309108694583L, 0, null, protoLogParam0);
            }
        }
        if (this.mDestroying) {
            this.mDestroying = false;
            this.mWmService.mDestroySurface.remove(this);
            this.mWmsExt.getDestroySavedSurface().remove(this);
        }
        if (!wasVisible) {
            this.mWinAnimator.mEnterAnimationPending = true;
        }
        this.mLastVisibleLayoutRotation = getDisplayContent().getRotation();
        this.mWinAnimator.mEnteringAnimation = true;
        android.os.Trace.traceBegin(32L, "prepareToDisplay");
        try {
            prepareWindowToDisplayDuringRelayout(wasVisible);
            return result2;
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    boolean isLaidOut() {
        return this.mLayoutSeq != -1;
    }

    void updateLastFrames() {
        this.mWindowFrames.mLastFrame.set(this.mWindowFrames.mFrame);
        this.mWindowFrames.mLastRelFrame.set(this.mWindowFrames.mRelFrame);
    }

    void onResizeHandled() {
        this.mWindowFrames.onResizeHandled();
    }

    @Override // com.android.server.wm.WindowContainer
    protected boolean isSelfAnimating(int flags, int typesToCheck) {
        if (this.mControllableInsetProvider != null) {
            return false;
        }
        return super.isSelfAnimating(flags, typesToCheck);
    }

    void startAnimation(android.view.animation.Animation anim) {
        if (this.mControllableInsetProvider != null) {
            return;
        }
        android.view.DisplayInfo displayInfo = getDisplayInfo();
        anim.initialize(this.mWindowFrames.mFrame.width(), this.mWindowFrames.mFrame.height(), displayInfo.appWidth, displayInfo.appHeight);
        anim.restrictDuration(10000L);
        anim.scaleCurrentDuration(this.mWmService.getWindowAnimationScaleLocked());
        if (this.mWindowStateExt.startAnimationWithRoundedCorners(this, anim, this.mSurfacePosition, this.mWindowFrames.mFrame)) {
            return;
        }
        android.graphics.Point position = new android.graphics.Point();
        if (com.android.window.flags.Flags.removePrepareSurfaceInPlacement()) {
            transformFrameToSurfacePosition(this.mWindowFrames.mFrame.left, this.mWindowFrames.mFrame.top, position);
        } else {
            position.set(this.mSurfacePosition);
        }
        com.android.server.wm.AnimationAdapter adapter = new com.android.server.wm.LocalAnimationAdapter(new com.android.server.wm.WindowAnimationSpec(anim, position, false, 0.0f), this.mWmService.mSurfaceAnimationRunner);
        android.view.SurfaceControl.Transaction t = this.mActivityRecord != null ? getSyncTransaction() : getPendingTransaction();
        startAnimation(t, adapter);
        commitPendingTransaction();
    }

    private void startMoveAnimation(int left, int top) {
        if (this.mControllableInsetProvider != null) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, 6291563604478341956L, 0, null, protoLogParam0);
        }
        android.graphics.Point oldPosition = new android.graphics.Point();
        android.graphics.Point newPosition = new android.graphics.Point();
        transformFrameToSurfacePosition(this.mWindowFrames.mLastFrame.left, this.mWindowFrames.mLastFrame.top, oldPosition);
        transformFrameToSurfacePosition(left, top, newPosition);
        com.android.server.wm.AnimationAdapter adapter = new com.android.server.wm.LocalAnimationAdapter(new com.android.server.wm.WindowState.MoveAnimationSpec(oldPosition.x, oldPosition.y, newPosition.x, newPosition.y), this.mWmService.mSurfaceAnimationRunner);
        if (this.mWindowStateExt.shouldBlockWindowMoveAnimation(this)) {
            return;
        }
        if (this.mWindowStateExt.forcePlayMoveAnimation(this)) {
            startAnimation(getPendingTransaction(), adapter, false, 16);
        } else {
            startAnimation(getPendingTransaction(), adapter);
        }
    }

    private void startAnimation(android.view.SurfaceControl.Transaction t, com.android.server.wm.AnimationAdapter adapter) {
        startAnimation(t, adapter, this.mWinAnimator.mLastHidden, 16);
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public void onLeashAnimationStarting(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
        if (!com.oplus.dynamicframerate.DynamicFrameRateManager.isTypeEnable(DYNAMIC_FRAME_RATE_DIALOG_TYPE_ID)) {
            android.util.Slog.d(TAG, "setFrameRateStart invalid typeId return!");
        } else if (com.oplus.dynamicframerate.DynamicFrameRateManager.getDynamicFrameRateType() != 0 && getWindowType() == 2 && (this.mAttrs.flags & 2) != 0) {
            this.mWindowStateExt.dynamicFrameRateStartAnimForDialog(getSurfaceControl());
        }
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public void onAnimationLeashLost(android.view.SurfaceControl.Transaction t) {
        boolean disableScene = !com.oplus.dynamicframerate.DynamicFrameRateManager.isTypeEnable(DYNAMIC_FRAME_RATE_DIALOG_TYPE_ID) || com.oplus.dynamicframerate.DynamicFrameRateManager.getDynamicFrameRateType() == 0;
        if (!disableScene && getWindowType() == 2 && (this.mAttrs.flags & 2) != 0) {
            this.mWindowStateExt.dynamicFrameRateFinishAnimForDialog(getSurfaceControl());
        }
        super.onAnimationLeashLost(t);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public void onAnimationFinished(int type, com.android.server.wm.AnimationAdapter anim) {
        super.onAnimationFinished(type, anim);
        this.mWinAnimator.onAnimationFinished();
    }

    void getTransformationMatrix(float[] float9, android.graphics.Matrix outMatrix) {
        float9[0] = this.mGlobalScale;
        float9[3] = 0.0f;
        float9[1] = 0.0f;
        float9[4] = this.mGlobalScale;
        transformSurfaceInsetsPosition(this.mTmpPoint, this.mAttrs.surfaceInsets);
        int x = this.mSurfacePosition.x + this.mTmpPoint.x;
        int y = this.mSurfacePosition.y + this.mTmpPoint.y;
        com.android.server.wm.WindowContainer parent = getParent();
        if (isChildWindow()) {
            com.android.server.wm.WindowState parentWindow = getParentWindow();
            x += parentWindow.mWindowFrames.mFrame.left - parentWindow.mAttrs.surfaceInsets.left;
            y += parentWindow.mWindowFrames.mFrame.top - parentWindow.mAttrs.surfaceInsets.top;
        } else if (parent != null) {
            android.graphics.Rect parentBounds = parent.getBounds();
            x += parentBounds.left;
            y += parentBounds.top;
        }
        float9[2] = x;
        float9[5] = y;
        float9[6] = 0.0f;
        float9[7] = 0.0f;
        float9[8] = 1.0f;
        outMatrix.setValues(float9);
    }

    static final class UpdateReportedVisibilityResults {
        boolean nowGone = true;
        int numDrawn;
        int numInteresting;
        int numVisible;

        UpdateReportedVisibilityResults() {
        }

        void reset() {
            this.numInteresting = 0;
            this.numVisible = 0;
            this.numDrawn = 0;
            this.nowGone = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class WindowId extends android.view.IWindowId.Stub {
        private final java.lang.ref.WeakReference<com.android.server.wm.WindowState> mOuter;

        private WindowId(com.android.server.wm.WindowState outer) {
            this.mOuter = new java.lang.ref.WeakReference<>(outer);
        }

        public void registerFocusObserver(android.view.IWindowFocusObserver observer) {
            com.android.server.wm.WindowState outer = this.mOuter.get();
            if (outer != null) {
                outer.registerFocusObserver(observer);
            }
        }

        public void unregisterFocusObserver(android.view.IWindowFocusObserver observer) {
            com.android.server.wm.WindowState outer = this.mOuter.get();
            if (outer != null) {
                outer.unregisterFocusObserver(observer);
            }
        }

        public boolean isFocused() {
            boolean zIsFocused;
            com.android.server.wm.WindowState outer = this.mOuter.get();
            if (outer != null) {
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = outer.mWmService.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        zIsFocused = outer.isFocused();
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return zIsFocused;
            }
            return false;
        }
    }

    @Override // com.android.server.wm.WindowContainer
    boolean shouldMagnify() {
        return (this.mAttrs.type == 2039 || this.mAttrs.type == 2011 || this.mAttrs.type == 2012 || this.mAttrs.type == 2027 || this.mAttrs.type == 2019 || this.mAttrs.type == 2024 || (this.mAttrs.privateFlags & 4194304) != 0) ? false : true;
    }

    @Override // com.android.server.wm.WindowContainer
    android.view.SurfaceSession getSession() {
        if (this.mSession.mSurfaceSession != null) {
            return this.mSession.mSurfaceSession;
        }
        return getParent().getSession();
    }

    @Override // com.android.server.wm.WindowContainer
    boolean needsZBoost() {
        com.android.server.wm.ActivityRecord activity;
        com.android.server.wm.InsetsControlTarget target = getDisplayContent().getImeTarget(0);
        if (!this.mIsImWindow || target == null || (activity = target.getWindow().mActivityRecord) == null) {
            return false;
        }
        return activity.needsZBoost();
    }

    private boolean isStartingWindowAssociatedToTask() {
        return (this.mStartingData == null || this.mStartingData.mAssociatedTask == null) ? false : true;
    }

    private void applyDims() {
        if (((this.mAttrs.flags & 2) == 0 && !shouldDrawBlurBehind()) || !isVisibleNow() || this.mHidden || !this.mTransitionController.canApplyDim(getTask()) || this.mWindowStateExt.isVisibleRequestedForActivity(getActivityRecord(), this.mToken)) {
            return;
        }
        this.mIsDimming = true;
        float dimAmount = (this.mWindowStateExt.canApplyDimInEmbedding(this) && (this.mAttrs.flags & 2) != 0) ? this.mAttrs.dimAmount : 0.0f;
        int blurRadius = shouldDrawBlurBehind() ? this.mAttrs.getBlurBehindRadius() : 0;
        if (isVisibleNow()) {
            getDimmer().adjustAppearance(this, dimAmount, blurRadius);
        }
        getDimmer().adjustRelativeLayer(this, -1);
    }

    private boolean shouldDrawBlurBehind() {
        return (this.mAttrs.flags & 4) != 0 && this.mWmService.mBlurController.getBlurEnabled();
    }

    void updateFrameRateSelectionPriorityIfNeeded() {
        com.android.server.wm.RefreshRatePolicy refreshRatePolicy = getDisplayContent().getDisplayPolicy().getRefreshRatePolicy();
        int priority = refreshRatePolicy.calculatePriority(this);
        if (this.mFrameRateSelectionPriority != priority) {
            this.mFrameRateSelectionPriority = priority;
            getPendingTransaction().setFrameRateSelectionPriority(this.mSurfaceControl, this.mFrameRateSelectionPriority);
        }
        boolean voteChanged = refreshRatePolicy.updateFrameRateVote(this);
        if (voteChanged) {
            getPendingTransaction().setFrameRate(this.mSurfaceControl, this.mFrameRateVote.mRefreshRate, this.mFrameRateVote.mCompatibility, 1);
            if (com.android.window.flags.Flags.explicitRefreshRateHints()) {
                getPendingTransaction().setFrameRateSelectionStrategy(this.mSurfaceControl, this.mFrameRateVote.mSelectionStrategy);
            }
        }
    }

    private void updateScaleIfNeeded() {
        if (!isVisibleRequested() && (!this.mIsWallpaper || !this.mToken.isVisible())) {
            return;
        }
        this.mWindowStateExt.expandFingerPrintDimLayerSurface(this, this.mWmService.mDisplayFrozen);
        float globalScale = this.mGlobalScale;
        com.android.server.wm.WindowState parent = getParentWindow();
        if (parent != null) {
            globalScale *= parent.mInvGlobalScale;
        }
        float newHScale = this.mHScale * globalScale * this.mWallpaperScale;
        float newVScale = this.mVScale * globalScale * this.mWallpaperScale;
        if (this.mLastHScale != newHScale || this.mLastVScale != newVScale) {
            getSyncTransaction().setMatrix(this.mSurfaceControl, newHScale, 0.0f, 0.0f, newVScale);
            this.mLastHScale = newHScale;
            this.mLastVScale = newVScale;
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void prepareSurfaces() {
        this.mIsDimming = false;
        if (this.mHasSurface) {
            if (!com.android.server.wm.Dimmer.DIMMER_REFACTOR) {
                applyDims();
            }
            this.mWindowStateExt.calculateForceUpdateWallpaperPosition();
            updateSurfacePositionNonOrganized();
            updateFrameRateSelectionPriorityIfNeeded();
            updateScaleIfNeeded();
            this.mWindowStateExt.setForceUpdateWallpaperPosition(false);
            this.mWindowStateExt.applyCornersAndShadowIfNeed(getSyncTransaction());
            this.mWinAnimator.prepareSurfaceLocked(getSyncTransaction());
            if (com.android.server.wm.Dimmer.DIMMER_REFACTOR) {
                applyDims();
            }
        }
        super.prepareSurfaces();
    }

    @Override // com.android.server.wm.WindowContainer
    void updateSurfacePosition(android.view.SurfaceControl.Transaction t) {
        if (this.mSurfaceControl == null || this.mIsSurfacePositionPaused) {
            return;
        }
        if (this.mActivityRecord != null && this.mActivityRecord.isConfigurationDispatchPaused()) {
            return;
        }
        if ((this.mWmService.mWindowPlacerLocked.isLayoutDeferred() || isGoneForLayout()) && !this.mSurfacePlacementNeeded) {
            return;
        }
        boolean isLetterboxedAndRelaunching = false;
        this.mSurfacePlacementNeeded = false;
        if (this.mWindowStateExt.isIgnoreImeTargetBottomOverlapFlexibleTask(this)) {
            transformFrameToSurfacePosition(getBounds().left, getBounds().top, this.mSurfacePosition);
        } else {
            transformFrameToSurfacePosition(this.mWindowFrames.mFrame.left, this.mWindowFrames.mFrame.top, this.mSurfacePosition);
        }
        if (this.mWallpaperScale != 1.0f) {
            android.graphics.Rect bounds = getParentFrame();
            android.graphics.Matrix matrix = this.mTmpMatrix;
            matrix.setTranslate(this.mXOffset, this.mYOffset);
            matrix.postScale(this.mWallpaperScale, this.mWallpaperScale, bounds.exactCenterX(), bounds.exactCenterY());
            matrix.getValues(this.mTmpMatrixArray);
            this.mSurfacePosition.offset(java.lang.Math.round(this.mTmpMatrixArray[2]), java.lang.Math.round(this.mTmpMatrixArray[5]));
        } else {
            this.mSurfacePosition.offset(this.mXOffset, this.mYOffset);
        }
        com.android.server.wm.AsyncRotationController asyncRotationController = this.mDisplayContent.getAsyncRotationController();
        if ((asyncRotationController == null || !asyncRotationController.hasSeamlessOperation(this.mToken)) && this.mPendingSeamlessRotate == null && !this.mSurfaceAnimator.hasLeash()) {
            if (!this.mLastSurfacePosition.equals(this.mSurfacePosition) || this.mWindowStateExt.isForceUpdateWallpaperPosition()) {
                boolean frameSizeChanged = this.mWindowFrames.isFrameSizeChangeReported() && !this.mWindowStateExt.shouldUpdateWinPos(this.mWindowFrames);
                boolean surfaceInsetsChanged = surfaceInsetsChanging();
                boolean surfaceSizeChanged = frameSizeChanged || surfaceInsetsChanged;
                this.mLastSurfacePosition.set(this.mSurfacePosition.x, this.mSurfacePosition.y);
                if (surfaceInsetsChanged) {
                    this.mLastSurfaceInsets.set(this.mAttrs.surfaceInsets);
                }
                boolean surfaceResizedWithoutMoveAnimation = surfaceSizeChanged && this.mWinAnimator.getShown() && !canPlayMoveAnimation() && okToDisplay() && this.mSyncState == 0;
                com.android.server.wm.ActivityRecord activityRecord = getActivityRecord();
                if (activityRecord != null && activityRecord.areBoundsLetterboxed() && activityRecord.mLetterboxUiController.getIsRelaunchingAfterRequestedOrientationChanged()) {
                    isLetterboxedAndRelaunching = true;
                }
                this.mWindowStateExt.updateSurfacePosition(this.mSurfacePosition);
                if (this.mWindowStateExt.skipUpdateWallpaperPosition()) {
                    return;
                }
                if (surfaceResizedWithoutMoveAnimation || isLetterboxedAndRelaunching) {
                    applyWithNextDraw(this.mSetSurfacePositionConsumer);
                } else {
                    this.mSetSurfacePositionConsumer.accept(t);
                }
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void transformFrameToSurfacePosition(int left, int top, android.graphics.Point outPoint) {
        android.graphics.Rect parentBounds;
        outPoint.set(left, top);
        com.android.server.wm.WindowContainer parentWindowContainer = getParent();
        if (isChildWindow()) {
            com.android.server.wm.WindowState parent = getParentWindow();
            outPoint.offset(-parent.mWindowFrames.mFrame.left, -parent.mWindowFrames.mFrame.top);
            if (this.mInvGlobalScale != 1.0f) {
                outPoint.x = (int) ((outPoint.x * this.mInvGlobalScale) + 0.5f);
                outPoint.y = (int) ((outPoint.y * this.mInvGlobalScale) + 0.5f);
            }
            transformSurfaceInsetsPosition(this.mTmpPoint, parent.mAttrs.surfaceInsets);
            outPoint.offset(this.mTmpPoint.x, this.mTmpPoint.y);
        } else if (parentWindowContainer != null) {
            if (isStartingWindowAssociatedToTask()) {
                parentBounds = this.mStartingData.mAssociatedTask.getBounds();
            } else {
                parentBounds = parentWindowContainer.getBounds();
            }
            this.mWindowStateExt.changeStartingWindowParentBounds(this, parentBounds);
            outPoint.offset(-parentBounds.left, -parentBounds.top);
        }
        transformSurfaceInsetsPosition(this.mTmpPoint, this.mAttrs.surfaceInsets);
        outPoint.offset(-this.mTmpPoint.x, -this.mTmpPoint.y);
        outPoint.y += this.mSurfaceTranslationY;
    }

    private void transformSurfaceInsetsPosition(android.graphics.Point outPos, android.graphics.Rect surfaceInsets) {
        if (this.mGlobalScale == 1.0f || this.mIsChildWindow) {
            outPos.x = surfaceInsets.left;
            outPos.y = surfaceInsets.top;
        } else {
            outPos.x = (int) ((surfaceInsets.left * this.mGlobalScale) + 0.5f);
            outPos.y = (int) ((surfaceInsets.top * this.mGlobalScale) + 0.5f);
        }
    }

    boolean needsRelativeLayeringToIme() {
        boolean needsRelativeLayeringToIme = false;
        if (this.mDisplayContent.shouldImeAttachedToApp() || !getDisplayContent().getImeContainer().isVisible()) {
            return false;
        }
        if (isChildWindow()) {
            if (getParentWindow().isImeLayeringTarget()) {
                if (this.mWindowStateExt.isLogToolRun()) {
                    android.util.Slog.d(TAG, "parent is imeLayeringTarget, relative to ime:" + this);
                }
                return true;
            }
        } else if (this.mActivityRecord != null) {
            com.android.server.wm.WindowState imeTarget = getImeLayeringTarget();
            boolean inTokenWithAndAboveImeTarget = (imeTarget == null || imeTarget == this || imeTarget.mToken != this.mToken || this.mAttrs.type == 3 || getParent() == null || imeTarget.compareTo((com.android.server.wm.WindowContainer) this) > 0) ? false : true;
            if (this.mAttrs.format == -2 && (this.mAttrs.flags & 8) == 8 && this.mWindowStateExt.shouldOrderLayerToImeInTablet(this.mActivityRecord)) {
                android.util.Slog.d(TAG, "needsRelativeLayeringToIme: return false");
                return false;
            }
            if (inTokenWithAndAboveImeTarget && this.mWindowStateExt.cannotRelativeLayeringToIme()) {
                return false;
            }
            if (this.mWindowStateExt.isLogToolRun()) {
                android.util.Slog.d(TAG, "inTokenWithAndAboveImeTarget:" + inTokenWithAndAboveImeTarget + " this:" + this);
            }
            return inTokenWithAndAboveImeTarget;
        }
        if ((this.mAttrs.flags & 131080) == 131072 && isTrustedOverlay() && canAddInternalSystemWindow() && this.mWindowStateExt.shouldRelativeLayerInSplitScreenMode(this) && this.mWindowStateExt.shouldRelativeLayerToImeInCompactWindow(this, getImeLayeringTarget())) {
            com.android.server.wm.WindowState imeTarget2 = getImeLayeringTarget();
            if (imeTarget2 != null && imeTarget2 != this && imeTarget2.compareTo((com.android.server.wm.WindowContainer) this) <= 0) {
                needsRelativeLayeringToIme = true;
            }
            if (this.mWindowStateExt.isLogToolRun()) {
                android.util.Slog.d(TAG, "needsRelativeLayeringToIme:" + needsRelativeLayeringToIme + " this:" + this);
            }
            return needsRelativeLayeringToIme;
        }
        if (this.mWindowStateExt.isLogToolRun()) {
            android.util.Slog.d(TAG, "not needsRelativeLayeringToIme this:" + this);
        }
        return false;
    }

    @Override // com.android.server.wm.InputTarget
    public com.android.server.wm.InsetsControlTarget getImeControlTarget() {
        return getDisplayContent().getImeHostOrFallback(this);
    }

    @Override // com.android.server.wm.WindowContainer
    void assignLayer(android.view.SurfaceControl.Transaction t, int layer) {
        if (this.mStartingData != null) {
            t.setLayer(this.mSurfaceControl, Integer.MAX_VALUE);
        } else if (needsRelativeLayeringToIme()) {
            getDisplayContent().assignRelativeLayerForImeTargetChild(t, this);
        } else {
            super.assignLayer(t, layer);
        }
    }

    boolean isDimming() {
        return this.mIsDimming;
    }

    @Override // com.android.server.wm.WindowContainer
    protected void reparentSurfaceControl(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl newParent) {
        if (isStartingWindowAssociatedToTask()) {
            return;
        }
        super.reparentSurfaceControl(t, newParent);
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl getAnimationLeashParent() {
        if (isStartingWindowAssociatedToTask()) {
            return this.mStartingData.mAssociatedTask.mSurfaceControl;
        }
        return super.getAnimationLeashParent();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.SurfaceAnimator.Animatable
    public void onAnimationLeashCreated(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
        super.onAnimationLeashCreated(t, leash);
        if (isStartingWindowAssociatedToTask() || this.mWindowStateExt.isWindowShownAnimationLeash(leash)) {
            t.setLayer(leash, Integer.MAX_VALUE);
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
    public void assignChildLayers(android.view.SurfaceControl.Transaction t) {
        int layer = 2;
        for (int i = 0; i < this.mChildren.size(); i++) {
            com.android.server.wm.WindowState w = (com.android.server.wm.WindowState) this.mChildren.get(i);
            if (w.mAttrs.type == 1001) {
                if (this.mWinAnimator.hasSurface()) {
                    w.assignRelativeLayer(t, this.mWinAnimator.mSurfaceController.mSurfaceControl, -2);
                } else {
                    w.assignLayer(t, -2);
                }
            } else if (w.mAttrs.type == 1004) {
                if (this.mWinAnimator.hasSurface()) {
                    w.assignRelativeLayer(t, this.mWinAnimator.mSurfaceController.mSurfaceControl, -1);
                } else {
                    w.assignLayer(t, -1);
                }
            } else {
                w.assignLayer(t, layer);
            }
            w.assignChildLayers(t);
            layer++;
        }
    }

    void updateTapExcludeRegion(android.graphics.Region region) {
        com.android.server.wm.DisplayContent currentDisplay = getDisplayContent();
        if (currentDisplay == null) {
            throw new java.lang.IllegalStateException("Trying to update window not attached to any display.");
        }
        if (region == null || region.isEmpty()) {
            this.mTapExcludeRegion.setEmpty();
        } else {
            this.mTapExcludeRegion.set(region);
        }
        currentDisplay.getWrapper().getExtImpl().updateWindowTapExcludeRegion(currentDisplay, null);
        currentDisplay.getInputMonitor().updateInputWindowsLw(true);
    }

    void getTapExcludeRegion(android.graphics.Region outRegion) {
        this.mTmpRect.set(this.mWindowFrames.mFrame);
        this.mTmpRect.offsetTo(0, 0);
        outRegion.set(this.mTapExcludeRegion);
        outRegion.op(this.mTmpRect, android.graphics.Region.Op.INTERSECT);
        outRegion.translate(this.mWindowFrames.mFrame.left, this.mWindowFrames.mFrame.top);
    }

    boolean isImeLayeringTarget() {
        return getDisplayContent().getImeTarget(0) == this;
    }

    boolean isImeOverlayLayeringTarget() {
        return isImeLayeringTarget() && (this.mAttrs.flags & 131080) != 0;
    }

    com.android.server.wm.WindowState getImeLayeringTarget() {
        com.android.server.wm.InsetsControlTarget target = getDisplayContent().getImeTarget(0);
        if (target != null) {
            return target.getWindow();
        }
        return null;
    }

    com.android.server.wm.WindowState getImeInputTarget() {
        com.android.server.wm.InputTarget target = this.mDisplayContent.getImeInputTarget();
        if (target != null) {
            return target.getWindowState();
        }
        return null;
    }

    void forceReportingResized() {
        this.mWindowFrames.forceReportingResized();
    }

    com.android.server.wm.WindowFrames getWindowFrames() {
        return this.mWindowFrames;
    }

    void resetContentChanged() {
        this.mWindowFrames.setContentChanged(false);
    }

    private final class MoveAnimationSpec implements com.android.server.wm.LocalAnimationAdapter.AnimationSpec {
        private final long mDuration;
        private android.graphics.Point mFrom;
        private android.view.animation.Interpolator mInterpolator;
        private android.graphics.Point mTo;

        private MoveAnimationSpec(int fromX, int fromY, int toX, int toY) {
            this.mFrom = new android.graphics.Point();
            this.mTo = new android.graphics.Point();
            android.view.animation.Animation anim = android.view.animation.AnimationUtils.loadAnimation(com.android.server.wm.WindowState.this.mContext, android.R.anim.wallpaper_close_exit);
            this.mDuration = (long) (anim.computeDurationHint() * com.android.server.wm.WindowState.this.mWmService.getWindowAnimationScaleLocked());
            this.mInterpolator = anim.getInterpolator();
            this.mFrom.set(fromX, fromY);
            this.mTo.set(toX, toY);
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public long getDuration() {
            return this.mDuration;
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void apply(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash, long currentPlayTime) {
            float fraction = getFraction(currentPlayTime);
            float v = this.mInterpolator.getInterpolation(fraction);
            t.setPosition(leash, this.mFrom.x + ((this.mTo.x - this.mFrom.x) * v), this.mFrom.y + ((this.mTo.y - this.mFrom.y) * v));
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "from=" + this.mFrom + " to=" + this.mTo + " duration=" + this.mDuration);
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void dumpDebugInner(android.util.proto.ProtoOutputStream proto) {
            long token = proto.start(1146756268034L);
            android.graphics.GraphicsProtos.dumpPointProto(this.mFrom, proto, 1146756268033L);
            android.graphics.GraphicsProtos.dumpPointProto(this.mTo, proto, 1146756268034L);
            proto.write(1112396529667L, this.mDuration);
            proto.end(token);
        }
    }

    com.android.internal.policy.KeyInterceptionInfo getKeyInterceptionInfo() {
        if (this.mKeyInterceptionInfo == null || this.mKeyInterceptionInfo.layoutParamsPrivateFlags != getAttrs().privateFlags || this.mKeyInterceptionInfo.layoutParamsType != getAttrs().type || this.mKeyInterceptionInfo.windowTitle != getWindowTag() || this.mKeyInterceptionInfo.windowOwnerUid != getOwningUid()) {
            this.mKeyInterceptionInfo = new com.android.internal.policy.KeyInterceptionInfo(getAttrs().type, getAttrs().privateFlags, getWindowTag().toString(), getOwningUid());
        }
        return this.mKeyInterceptionInfo;
    }

    @Override // com.android.server.wm.WindowContainer
    void getAnimationFrames(android.graphics.Rect outFrame, android.graphics.Rect outInsets, android.graphics.Rect outStableInsets, android.graphics.Rect outSurfaceInsets) {
        if (inFreeformWindowingMode()) {
            outFrame.set(getFrame());
        } else if (areAppWindowBoundsLetterboxed() || this.mToken.isFixedRotationTransforming()) {
            outFrame.set(getTask().getBounds());
        } else {
            outFrame.set(getParentFrame());
        }
        outSurfaceInsets.set(getAttrs().surfaceInsets);
        android.view.InsetsState state = getInsetsStateWithVisibilityOverride();
        outInsets.set(state.calculateInsets(outFrame, android.view.WindowInsets.Type.systemBars(), false).toRect());
        outStableInsets.set(state.calculateInsets(outFrame, android.view.WindowInsets.Type.systemBars(), true).toRect());
    }

    void setViewVisibility(int viewVisibility) {
        this.mViewVisibility = viewVisibility;
    }

    android.view.SurfaceControl getClientViewRootSurface() {
        return this.mWinAnimator.getSurfaceControl();
    }

    private void dropBufferFrom(android.view.SurfaceControl.Transaction t) {
        android.view.SurfaceControl viewSurface = getClientViewRootSurface();
        if (viewSurface == null) {
            return;
        }
        t.unsetBuffer(viewSurface);
        android.util.Slog.d(TAG, " dropBufferFrom w=" + this);
    }

    @Override // com.android.server.wm.WindowContainer
    protected boolean shouldUpdateSyncOnReparent() {
        return (this.mSyncState == 0 || this.mLastConfigReportedToClient) ? false : true;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean prepareSync() {
        if (!this.mDrawHandlers.isEmpty()) {
            android.util.Slog.w(TAG, "prepareSync with mDrawHandlers, " + this + ", " + android.os.Debug.getCallers(8));
        }
        if (!super.prepareSync()) {
            return false;
        }
        if (this.mIsWallpaper) {
            return true;
        }
        if (this.mActivityRecord != null && this.mViewVisibility != 0 && this.mWinAnimator.mAttrType != 1 && this.mWinAnimator.mAttrType != 3 && !this.mWindowStateExt.hasFullSubWinOnLauncher(this)) {
            return false;
        }
        this.mSyncState = 1;
        if (this.mPrepareSyncSeqId > 0) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -5774445199273871848L, 0, null, protoLogParam0);
            }
            dropBufferFrom(this.mSyncTransaction);
        }
        this.mSyncSeqId++;
        if (getSyncMethod() == 1) {
            this.mPrepareSyncSeqId = this.mSyncSeqId;
            requestRedrawForSync();
        } else if (this.mHasSurface && this.mWinAnimator.mDrawState != 1) {
            requestRedrawForSync();
        }
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isSyncFinished(com.android.server.wm.BLASTSyncEngine.SyncGroup group) {
        if (!isVisibleRequested() || isFullyTransparent()) {
            return true;
        }
        if (this.mWindowStateExt.isSyncFinished(this, this.mSyncState, this.mWmsExt, this.mWmService.mDestroySurface)) {
            android.util.Slog.i(TAG, "window surface saved sync finished win=" + this);
            return true;
        }
        if (this.mSyncState == 1 && this.mLastConfigReportedToClient && isDrawn() && this.mPrepareSyncSeqId <= 0 && this.mWindowStateExt.isSyncFinishedDrawing(this, this.mWinAnimator.mDrawState)) {
            onSyncFinishedDrawing();
        }
        return super.isSyncFinished(group);
    }

    @Override // com.android.server.wm.WindowContainer
    void finishSync(android.view.SurfaceControl.Transaction outMergedTransaction, com.android.server.wm.BLASTSyncEngine.SyncGroup group, boolean cancel) {
        com.android.server.wm.BLASTSyncEngine.SyncGroup syncGroup = getSyncGroup();
        if (syncGroup == null || group == syncGroup) {
            this.mPrepareSyncSeqId = 0;
            if (cancel) {
                dropBufferFrom(this.mSyncTransaction);
            }
            super.finishSync(outMergedTransaction, group, cancel);
        }
    }

    boolean finishDrawing(android.view.SurfaceControl.Transaction postDrawTransaction, int syncSeqId) {
        android.view.SurfaceControl.Transaction postDrawTransaction2 = postDrawTransaction;
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, "finishDrawing :" + this + "syncSeqId:" + syncSeqId + " prepareSyncSeqId:" + this.mPrepareSyncSeqId + " postDrawTransaction:" + postDrawTransaction2);
        }
        if (this.mOrientationChangeRedrawRequestTime > 0) {
            long duration = android.os.SystemClock.elapsedRealtime() - this.mOrientationChangeRedrawRequestTime;
            android.util.Slog.i(TAG, "finishDrawing of orientation change: " + this + " " + duration + "ms");
            this.mOrientationChangeRedrawRequestTime = 0L;
        } else if (this.mActivityRecord != null && this.mActivityRecord.mRelaunchStartTime != 0 && this.mActivityRecord.findMainWindow(false) == this) {
            long duration2 = android.os.SystemClock.elapsedRealtime() - this.mActivityRecord.mRelaunchStartTime;
            android.util.Slog.i(TAG, "finishDrawing of relaunch: " + this + " " + duration2 + "ms");
            this.mActivityRecord.finishOrAbortReplacingWindow();
        }
        if (this.mActivityRecord != null && this.mAttrs.type == 3) {
            this.mWmService.mAtmService.mTaskSupervisor.getActivityMetricsLogger().notifyStartingWindowDrawn(this.mActivityRecord);
        }
        boolean syncActive = this.mPrepareSyncSeqId > 0;
        boolean syncStillPending = syncActive && this.mPrepareSyncSeqId > syncSeqId;
        if (syncStillPending && postDrawTransaction2 != null) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[0]) {
                long protoLogParam0 = syncSeqId;
                long protoLogParam1 = this.mPrepareSyncSeqId;
                java.lang.String protoLogParam2 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, 8097934579596343476L, 5, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Long.valueOf(protoLogParam1), protoLogParam2);
            }
            dropBufferFrom(postDrawTransaction);
        }
        boolean hasSyncHandlers = executeDrawHandlers(postDrawTransaction, syncSeqId);
        if (this.mWindowStateExt.finishDrawingApplyPostDraw(this, postDrawTransaction2)) {
            postDrawTransaction2 = null;
        }
        boolean skipLayout = false;
        boolean layoutNeeded = false;
        com.android.server.wm.AsyncRotationController asyncRotationController = this.mDisplayContent.getAsyncRotationController();
        if (asyncRotationController != null && asyncRotationController.handleFinishDrawing(this, postDrawTransaction2)) {
            android.util.Slog.d(TAG, "asyncRotationController handleFinishDrawing");
            postDrawTransaction2 = null;
            skipLayout = true;
        } else if (syncActive) {
            if (!syncStillPending) {
                layoutNeeded = onSyncFinishedDrawing();
                android.util.Slog.d(TAG, "not syncStillPending, layoutNeeded:" + layoutNeeded + ",syncSeqId=" + syncSeqId + ",mPrepareSyncSeqId=" + this.mPrepareSyncSeqId);
            }
            if (postDrawTransaction2 != null && !this.mWindowStateExt.needMaintainVisibleSate(this)) {
                android.util.Slog.d(TAG, "postDrawTransaction merge to syncTransaction:" + layoutNeeded + ",syncSeqId=" + syncSeqId + ",mPrepareSyncSeqId=" + this.mPrepareSyncSeqId + ",this=" + this);
                this.mSyncTransaction.merge(postDrawTransaction2);
                postDrawTransaction2 = null;
            }
        } else if (syncNextBuffer()) {
            layoutNeeded = onSyncFinishedDrawing();
            android.util.Slog.i(TAG, "finishDrawing skipLayout:false,syncSeqId=" + syncSeqId + ",mPrepareSyncSeqId=" + this.mPrepareSyncSeqId + " " + this + " " + android.os.Debug.getCallers(3));
        }
        return !this.mWindowStateExt.finishDrawing(skipLayout) && (hasSyncHandlers || (this.mWinAnimator.finishDrawingLocked(postDrawTransaction2) || layoutNeeded));
    }

    void immediatelyNotifyBlastSync() {
        finishDrawing(null, Integer.MAX_VALUE);
        this.mWmService.mH.removeMessages(64, this);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean fillsParent() {
        return this.mAttrs.type == 3;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean showWallpaper() {
        if (!isVisibleRequested() || inMultiWindowMode()) {
            return false;
        }
        return hasWallpaper();
    }

    @Override // com.android.server.wm.WindowContainer
    boolean hasWallpaper() {
        return (this.mAttrs.flags & 1048576) != 0 || hasWallpaperForLetterboxBackground();
    }

    boolean hasWallpaperForLetterboxBackground() {
        return this.mActivityRecord != null && this.mActivityRecord.hasWallpaperBackgroundForLetterbox();
    }

    private boolean shouldSendRedrawForSync() {
        if (this.mRedrawForSyncReported) {
            return false;
        }
        if (!this.mInRelayout || (this.mPrepareSyncSeqId <= 0 && !(this.mViewVisibility == 0 && this.mWinAnimator.mDrawState == 1))) {
            return syncNextBuffer();
        }
        return false;
    }

    int getSyncMethod() {
        com.android.server.wm.BLASTSyncEngine.SyncGroup syncGroup = getSyncGroup();
        if (syncGroup == null) {
            return 0;
        }
        return this.mSyncMethodOverride != -1 ? this.mSyncMethodOverride : syncGroup.mSyncMethod;
    }

    boolean shouldSyncWithBuffers() {
        return !this.mDrawHandlers.isEmpty() || this.mWindowStateExt.shouldSyncWithBuffersIfNeeded(this) || getSyncMethod() == 1;
    }

    void requestRedrawForSync() {
        this.mRedrawForSyncReported = false;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean syncNextBuffer() {
        return super.syncNextBuffer() || this.mDrawHandlers.size() != 0;
    }

    void applyWithNextDraw(java.util.function.Consumer<android.view.SurfaceControl.Transaction> consumer) {
        if (this.mSyncState != 0) {
            android.util.Slog.w(TAG, "applyWithNextDraw with mSyncState=" + this.mSyncState + ", " + this + ", " + android.os.Debug.getCallers(8));
        }
        this.mSyncSeqId++;
        this.mDrawHandlers.add(new com.android.server.wm.WindowState.DrawHandler(this.mSyncSeqId, consumer));
        requestRedrawForSync();
        this.mWmService.mH.sendNewMessageDelayed(64, this, 5000L);
    }

    boolean executeDrawHandlers(android.view.SurfaceControl.Transaction t, int seqId) {
        boolean hadHandlers = false;
        boolean applyHere = false;
        if (t == null) {
            t = this.mTmpTransaction;
            applyHere = true;
        }
        java.util.List<com.android.server.wm.WindowState.DrawHandler> handlersToRemove = new java.util.ArrayList<>();
        for (int i = 0; i < this.mDrawHandlers.size(); i++) {
            com.android.server.wm.WindowState.DrawHandler h = this.mDrawHandlers.get(i);
            if (h.mSeqId <= seqId) {
                h.mConsumer.accept(t);
                handlersToRemove.add(h);
                hadHandlers = true;
            }
        }
        for (int i2 = 0; i2 < handlersToRemove.size(); i2++) {
            this.mDrawHandlers.remove(handlersToRemove.get(i2));
        }
        if (hadHandlers) {
            this.mWmService.mH.removeMessages(64, this);
        }
        if (applyHere) {
            t.apply();
        }
        return hadHandlers;
    }

    void setSurfaceTranslationY(int translationY) {
        this.mSurfaceTranslationY = translationY;
    }

    @Override // com.android.server.wm.WindowContainer
    int getWindowType() {
        return this.mAttrs.type;
    }

    void markRedrawForSyncReported() {
        this.mRedrawForSyncReported = true;
    }

    boolean setWallpaperOffset(int dx, int dy, float scale) {
        if (this.mXOffset == dx && this.mYOffset == dy && java.lang.Float.compare(this.mWallpaperScale, scale) == 0) {
            return false;
        }
        this.mXOffset = dx;
        this.mYOffset = dy;
        this.mWallpaperScale = scale;
        scheduleAnimation();
        return true;
    }

    boolean isTrustedOverlay() {
        if (com.android.window.flags.Flags.surfaceTrustedOverlay()) {
            com.android.server.wm.WindowState parentWindow = getParentWindow();
            return isWindowTrustedOverlay() || (parentWindow != null && parentWindow.isWindowTrustedOverlay());
        }
        return this.mInputWindowHandle.isTrustedOverlay();
    }

    @Override // com.android.server.wm.InputTarget
    public boolean receiveFocusFromTapOutside() {
        return canReceiveKeys(true);
    }

    @Override // com.android.server.wm.InputTarget
    public void handleTapOutsideFocusOutsideSelf() {
    }

    @Override // com.android.server.wm.InputTarget
    public void handleTapOutsideFocusInsideSelf() {
        this.mWmService.moveDisplayToTopInternal(getDisplayId());
        this.mWmService.handleTaskFocusChange(getTask(), this.mActivityRecord);
    }

    void clearClientTouchableRegion() {
        this.mTouchableInsets = 0;
        this.mGivenTouchableRegion.setEmpty();
    }

    @Override // com.android.server.wm.InputTarget
    public boolean shouldControlIme() {
        return (!inMultiWindowMode() || getWrapper().getExtImpl().layoutFullscreenInEmbedding()) && !this.mWindowStateExt.isNotFullScreenCompactWindow(this);
    }

    @Override // com.android.server.wm.InputTarget
    public boolean canScreenshotIme() {
        return !isSecureLocked();
    }

    @Override // com.android.server.wm.InputTarget
    public com.android.server.wm.ActivityRecord getActivityRecord() {
        return this.mActivityRecord;
    }

    @Override // com.android.server.wm.InputTarget
    public boolean isInputMethodClientFocus(int uid, int pid) {
        return getDisplayContent().isInputMethodClientFocus(uid, pid);
    }

    @Override // com.android.server.wm.InputTarget
    public void dumpProto(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        dumpDebug(proto, fieldId, logLevel);
    }

    public boolean cancelAndRedraw() {
        return this.mPrepareSyncSeqId > 0;
    }

    public boolean isActivityWindow() {
        return this.mActivityRecord != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSecureLocked(boolean isSecure) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
            java.lang.String protoLogParam1 = java.lang.String.valueOf(getName());
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, 8269653477215188641L, 3, null, java.lang.Boolean.valueOf(isSecure), protoLogParam1);
        }
        boolean protoLogParam0 = com.android.window.flags.Flags.secureWindowState();
        if (protoLogParam0) {
            if (this.mSurfaceControl == null) {
                return;
            } else {
                getPendingTransaction().setSecure(this.mSurfaceControl, isSecure);
            }
        } else if (this.mWinAnimator.mSurfaceController == null || this.mWinAnimator.mSurfaceController.mSurfaceControl == null) {
            return;
        } else {
            getPendingTransaction().setSecure(this.mWinAnimator.mSurfaceController.mSurfaceControl, isSecure);
        }
        if (this.mDisplayContent != null) {
            this.mDisplayContent.refreshImeSecureFlag(getSyncTransaction());
        }
        this.mWmService.scheduleAnimationLocked();
        if (this.mWinAnimator != null && this.mWinAnimator.mWin != null) {
            this.mWindowStateExt.onSecurityPageFlagChanged(this.mWinAnimator.mWin, isSecure && isVisible(), true);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.Dimmer getDimmer() {
        com.android.server.wm.Task task;
        com.android.server.wm.TaskFragment tf = getTaskFragment();
        if (tf != null && !tf.isDimmingOnParentTask() && getWrapper().getExtImpl().layoutFullscreenInEmbedding() && (task = getTask()) != null) {
            return task.getDimmer();
        }
        return super.getDimmer();
    }

    public com.android.server.wm.IWindowStateWrapper getWrapper() {
        return this.mWindowStateWrapper;
    }

    private class WindowStateWrapper implements com.android.server.wm.IWindowStateWrapper {
        private WindowStateWrapper() {
        }

        @Override // com.android.server.wm.IWindowStateWrapper
        public com.android.server.wm.IWindowStateExt getExtImpl() {
            return com.android.server.wm.WindowState.this.mWindowStateExt;
        }

        @Override // com.android.server.wm.IWindowStateWrapper
        public boolean getAppOpVisibility() {
            return com.android.server.wm.WindowState.this.mAppOpVisibility;
        }
    }
}
