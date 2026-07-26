package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class LetterboxUiController {
    static final int MIN_COUNT_TO_IGNORE_REQUEST_IN_LOOP = 2;
    static final int SET_ORIENTATION_REQUEST_COUNTER_TIMEOUT_MS = 1000;
    private static final java.lang.String TAG = "ActivityTaskManager";
    private final com.android.server.wm.ActivityRecord mActivityRecord;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mAllowDisplayOrientationOverrideOptProp;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mAllowForceResizeOverrideOptProp;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mAllowIgnoringOrientationRequestWhenLoopDetectedOptProp;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mAllowMinAspectRatioOverrideOptProp;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mAllowOrientationOverrideOptProp;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mAllowUserAspectRatioFullscreenOverrideOptProp;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mAllowUserAspectRatioOverrideOptProp;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mCameraCompatAllowForceRotationOptProp;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mCameraCompatAllowRefreshOptProp;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mCameraCompatEnableRefreshViaPauseOptProp;
    private boolean mDoubleTapEvent;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mFakeFocusOptProp;
    private final com.android.server.wm.utils.OptPropFactory.OptProp mIgnoreRequestedOrientationOptProp;
    private final boolean mIsOverrideAnyOrientationEnabled;
    private final boolean mIsOverrideOrientationOnlyForCameraEnabled;
    private final boolean mIsOverrideRespectRequestedOrientationEnabled;
    private final boolean mIsOverrideToNosensorOrientationEnabled;
    private final boolean mIsOverrideToPortraitOrientationEnabled;
    private final boolean mIsOverrideToReverseLandscapeOrientationEnabled;
    private boolean mIsRefreshRequested;
    private boolean mIsRelaunchingAfterRequestedOrientationChanged;
    private final boolean mIsSystemOverrideToFullscreenEnabled;
    private boolean mLastShouldShowLetterboxUi;
    private com.android.server.wm.Letterbox mLetterbox;
    private final com.android.server.wm.LetterboxConfiguration mLetterboxConfiguration;
    private boolean mShowWallpaperForLetterboxBackground;
    private final android.graphics.Point mTmpPoint = new android.graphics.Point();
    private long mTimeMsLastSetOrientationRequest = 0;
    private int mSetOrientationRequestCounter = 0;
    private int mUserAspectRatio = 0;
    private final com.android.server.wm.ILetterboxUiControllerExt mExt = (com.android.server.wm.ILetterboxUiControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ILetterboxUiControllerExt.class).base(this).create();
    private int mFreeformCameraCompatMode = 0;

    LetterboxUiController(com.android.server.wm.WindowManagerService wmService, com.android.server.wm.ActivityRecord activityRecord) {
        this.mLetterboxConfiguration = wmService.mLetterboxConfiguration;
        this.mActivityRecord = activityRecord;
        android.content.pm.PackageManager packageManager = wmService.mContext.getPackageManager();
        com.android.server.wm.utils.OptPropFactory optPropBuilder = new com.android.server.wm.utils.OptPropFactory(packageManager, activityRecord.packageName);
        final com.android.server.wm.LetterboxConfiguration letterboxConfiguration = this.mLetterboxConfiguration;
        java.util.Objects.requireNonNull(letterboxConfiguration);
        java.util.function.BooleanSupplier isPolicyForIgnoringRequestedOrientationEnabled = asLazy(new java.util.function.BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda0
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return letterboxConfiguration.isPolicyForIgnoringRequestedOrientationEnabled();
            }
        });
        this.mIgnoreRequestedOrientationOptProp = optPropBuilder.create("android.window.PROPERTY_COMPAT_IGNORE_REQUESTED_ORIENTATION", isPolicyForIgnoringRequestedOrientationEnabled);
        this.mAllowIgnoringOrientationRequestWhenLoopDetectedOptProp = optPropBuilder.create("android.window.PROPERTY_COMPAT_ALLOW_IGNORING_ORIENTATION_REQUEST_WHEN_LOOP_DETECTED", isPolicyForIgnoringRequestedOrientationEnabled);
        final com.android.server.wm.LetterboxConfiguration letterboxConfiguration2 = this.mLetterboxConfiguration;
        java.util.Objects.requireNonNull(letterboxConfiguration2);
        this.mFakeFocusOptProp = optPropBuilder.create("android.window.PROPERTY_COMPAT_ENABLE_FAKE_FOCUS", new java.util.function.BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda1
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return letterboxConfiguration2.isCompatFakeFocusEnabled();
            }
        });
        final com.android.server.wm.LetterboxConfiguration letterboxConfiguration3 = this.mLetterboxConfiguration;
        java.util.Objects.requireNonNull(letterboxConfiguration3);
        java.util.function.BooleanSupplier isCameraCompatTreatmentEnabled = asLazy(new java.util.function.BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda2
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return letterboxConfiguration3.isCameraCompatTreatmentEnabled();
            }
        });
        this.mCameraCompatAllowForceRotationOptProp = optPropBuilder.create("android.window.PROPERTY_CAMERA_COMPAT_ALLOW_FORCE_ROTATION", isCameraCompatTreatmentEnabled);
        this.mCameraCompatAllowRefreshOptProp = optPropBuilder.create("android.window.PROPERTY_CAMERA_COMPAT_ALLOW_REFRESH", isCameraCompatTreatmentEnabled);
        this.mCameraCompatEnableRefreshViaPauseOptProp = optPropBuilder.create("android.window.PROPERTY_CAMERA_COMPAT_ENABLE_REFRESH_VIA_PAUSE", isCameraCompatTreatmentEnabled);
        this.mAllowOrientationOverrideOptProp = optPropBuilder.create("android.window.PROPERTY_COMPAT_ALLOW_ORIENTATION_OVERRIDE");
        this.mAllowDisplayOrientationOverrideOptProp = optPropBuilder.create("android.window.PROPERTY_COMPAT_ALLOW_DISPLAY_ORIENTATION_OVERRIDE", new java.util.function.BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda3
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return this.f$0.lambda$new$0();
            }
        });
        this.mAllowMinAspectRatioOverrideOptProp = optPropBuilder.create("android.window.PROPERTY_COMPAT_ALLOW_MIN_ASPECT_RATIO_OVERRIDE");
        this.mAllowForceResizeOverrideOptProp = optPropBuilder.create("android.window.PROPERTY_COMPAT_ALLOW_RESIZEABLE_ACTIVITY_OVERRIDES");
        final com.android.server.wm.LetterboxConfiguration letterboxConfiguration4 = this.mLetterboxConfiguration;
        java.util.Objects.requireNonNull(letterboxConfiguration4);
        this.mAllowUserAspectRatioOverrideOptProp = optPropBuilder.create("android.window.PROPERTY_COMPAT_ALLOW_USER_ASPECT_RATIO_OVERRIDE", new java.util.function.BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda4
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return letterboxConfiguration4.isUserAppAspectRatioSettingsEnabled();
            }
        });
        final com.android.server.wm.LetterboxConfiguration letterboxConfiguration5 = this.mLetterboxConfiguration;
        java.util.Objects.requireNonNull(letterboxConfiguration5);
        this.mAllowUserAspectRatioFullscreenOverrideOptProp = optPropBuilder.create("android.window.PROPERTY_COMPAT_ALLOW_USER_ASPECT_RATIO_FULLSCREEN_OVERRIDE", new java.util.function.BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda5
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return letterboxConfiguration5.isUserAppAspectRatioFullscreenEnabled();
            }
        });
        this.mIsOverrideAnyOrientationEnabled = isCompatChangeEnabled(265464455L);
        this.mIsSystemOverrideToFullscreenEnabled = isCompatChangeEnabled(310816437L);
        this.mIsOverrideToPortraitOrientationEnabled = isCompatChangeEnabled(265452344L);
        this.mIsOverrideToReverseLandscapeOrientationEnabled = isCompatChangeEnabled(266124927L);
        this.mIsOverrideToNosensorOrientationEnabled = isCompatChangeEnabled(265451093L);
        this.mIsOverrideOrientationOnlyForCameraEnabled = isCompatChangeEnabled(265456536L);
        this.mIsOverrideRespectRequestedOrientationEnabled = isCompatChangeEnabled(236283604L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0() {
        return (this.mActivityRecord.mDisplayContent == null || this.mActivityRecord.getTask() == null || !this.mActivityRecord.mDisplayContent.getIgnoreOrientationRequest() || this.mActivityRecord.getTask().inMultiWindowMode() || this.mActivityRecord.mDisplayContent.getNaturalOrientation() != 2) ? false : true;
    }

    void destroy() {
        if (this.mLetterbox != null) {
            this.mLetterbox.destroy();
            this.mLetterbox = null;
        }
        this.mActivityRecord.mTransparentPolicy.stop();
    }

    void onMovedToDisplay(int displayId) {
        if (this.mLetterbox != null) {
            this.mLetterbox.onMovedToDisplay(displayId);
        }
    }

    boolean shouldIgnoreRequestedOrientation(int requestedOrientation) {
        if (this.mIgnoreRequestedOrientationOptProp.shouldEnableWithOverrideAndProperty(isCompatChangeEnabled(254631730L))) {
            if (this.mIsRelaunchingAfterRequestedOrientationChanged) {
                android.util.Slog.w(TAG, "Ignoring orientation update to " + android.content.pm.ActivityInfo.screenOrientationToString(requestedOrientation) + " due to relaunching after setRequestedOrientation for " + this.mActivityRecord);
                return true;
            }
            if (isCameraCompatTreatmentActive()) {
                android.util.Slog.w(TAG, "Ignoring orientation update to " + android.content.pm.ActivityInfo.screenOrientationToString(requestedOrientation) + " due to camera compat treatment for " + this.mActivityRecord);
                return true;
            }
        }
        if (shouldIgnoreOrientationRequestLoop()) {
            android.util.Slog.w(TAG, "Ignoring orientation update to " + android.content.pm.ActivityInfo.screenOrientationToString(requestedOrientation) + " as orientation request loop was detected for " + this.mActivityRecord);
            return true;
        }
        return false;
    }

    boolean shouldIgnoreOrientationRequestLoop() {
        boolean loopDetectionEnabled = isCompatChangeEnabled(273509367L);
        if (!this.mAllowIgnoringOrientationRequestWhenLoopDetectedOptProp.shouldEnableWithOptInOverrideAndOptOutProperty(loopDetectionEnabled)) {
            return false;
        }
        long currTimeMs = java.lang.System.currentTimeMillis();
        if (currTimeMs - this.mTimeMsLastSetOrientationRequest < 1000) {
            this.mSetOrientationRequestCounter++;
        } else {
            this.mSetOrientationRequestCounter = 0;
        }
        this.mTimeMsLastSetOrientationRequest = currTimeMs;
        return this.mSetOrientationRequestCounter >= 2 && !this.mActivityRecord.isLetterboxedForFixedOrientationAndAspectRatio();
    }

    int getSetOrientationRequestCounter() {
        return this.mSetOrientationRequestCounter;
    }

    boolean shouldSendFakeFocus() {
        return this.mFakeFocusOptProp.shouldEnableWithOverrideAndProperty(isCompatChangeEnabled(263259275L));
    }

    boolean shouldOverrideMinAspectRatio() {
        return this.mAllowMinAspectRatioOverrideOptProp.shouldEnableWithOptInOverrideAndOptOutProperty(isCompatChangeEnabled(174042980L));
    }

    boolean shouldOverrideMinAspectRatioForCamera() {
        return this.mActivityRecord.isCameraActive() && this.mAllowMinAspectRatioOverrideOptProp.shouldEnableWithOptInOverrideAndOptOutProperty(isCompatChangeEnabled(325586858L));
    }

    boolean shouldOverrideForceResizeApp() {
        return this.mAllowForceResizeOverrideOptProp.shouldEnableWithOptInOverrideAndOptOutProperty(isCompatChangeEnabled(174042936L));
    }

    boolean shouldOverrideForceNonResizeApp() {
        return this.mAllowForceResizeOverrideOptProp.shouldEnableWithOptInOverrideAndOptOutProperty(isCompatChangeEnabled(181136395L));
    }

    void setRelaunchingAfterRequestedOrientationChanged(boolean isRelaunching) {
        this.mIsRelaunchingAfterRequestedOrientationChanged = isRelaunching;
    }

    boolean isRefreshRequested() {
        return this.mIsRefreshRequested;
    }

    void setIsRefreshRequested(boolean isRequested) {
        this.mIsRefreshRequested = isRequested;
    }

    boolean isOverrideRespectRequestedOrientationEnabled() {
        return this.mIsOverrideRespectRequestedOrientationEnabled;
    }

    boolean shouldUseDisplayLandscapeNaturalOrientation() {
        return this.mAllowDisplayOrientationOverrideOptProp.shouldEnableWithOptInOverrideAndOptOutProperty(isCompatChangeEnabled(255940284L));
    }

    int overrideOrientationIfNeeded(int candidate) {
        com.android.server.wm.DisplayContent displayContent = this.mActivityRecord.mDisplayContent;
        boolean isIgnoreOrientationRequestEnabled = displayContent != null && displayContent.getIgnoreOrientationRequest();
        if (shouldApplyUserFullscreenOverride() && isIgnoreOrientationRequestEnabled && !this.mActivityRecord.isCameraActive()) {
            android.util.Slog.v(TAG, "Requested orientation " + android.content.pm.ActivityInfo.screenOrientationToString(candidate) + " for " + this.mActivityRecord + " is overridden to " + android.content.pm.ActivityInfo.screenOrientationToString(2) + " by user aspect ratio settings.");
            return 2;
        }
        int candidate2 = this.mActivityRecord.mWmService.mapOrientationRequest(candidate);
        if (shouldApplyUserMinAspectRatioOverride() && (!android.content.pm.ActivityInfo.isFixedOrientation(candidate2) || candidate2 == 14)) {
            android.util.Slog.v(TAG, "Requested orientation " + android.content.pm.ActivityInfo.screenOrientationToString(candidate2) + " for " + this.mActivityRecord + " is overridden to " + android.content.pm.ActivityInfo.screenOrientationToString(1) + " by user aspect ratio settings.");
            return 1;
        }
        if (this.mAllowOrientationOverrideOptProp.isFalse()) {
            return candidate2;
        }
        if (this.mIsOverrideOrientationOnlyForCameraEnabled && displayContent != null && (displayContent.mDisplayRotationCompatPolicy == null || !displayContent.mDisplayRotationCompatPolicy.isActivityEligibleForOrientationOverride(this.mActivityRecord))) {
            return candidate2;
        }
        if (isSystemOverrideToFullscreenEnabled() && isIgnoreOrientationRequestEnabled && !this.mActivityRecord.isCameraActive()) {
            android.util.Slog.v(TAG, "Requested orientation  " + android.content.pm.ActivityInfo.screenOrientationToString(candidate2) + " for " + this.mActivityRecord + " is overridden to " + android.content.pm.ActivityInfo.screenOrientationToString(2));
            return 2;
        }
        if (this.mIsOverrideToReverseLandscapeOrientationEnabled && (android.content.pm.ActivityInfo.isFixedOrientationLandscape(candidate2) || this.mIsOverrideAnyOrientationEnabled)) {
            android.util.Slog.w(TAG, "Requested orientation  " + android.content.pm.ActivityInfo.screenOrientationToString(candidate2) + " for " + this.mActivityRecord + " is overridden to " + android.content.pm.ActivityInfo.screenOrientationToString(8));
            return 8;
        }
        if (!this.mIsOverrideAnyOrientationEnabled && android.content.pm.ActivityInfo.isFixedOrientation(candidate2)) {
            return candidate2;
        }
        if (this.mIsOverrideToPortraitOrientationEnabled) {
            android.util.Slog.w(TAG, "Requested orientation  " + android.content.pm.ActivityInfo.screenOrientationToString(candidate2) + " for " + this.mActivityRecord + " is overridden to " + android.content.pm.ActivityInfo.screenOrientationToString(1));
            return 1;
        }
        if (this.mIsOverrideToNosensorOrientationEnabled) {
            android.util.Slog.w(TAG, "Requested orientation  " + android.content.pm.ActivityInfo.screenOrientationToString(candidate2) + " for " + this.mActivityRecord + " is overridden to " + android.content.pm.ActivityInfo.screenOrientationToString(5));
            return 5;
        }
        return candidate2;
    }

    boolean isOverrideOrientationOnlyForCameraEnabled() {
        return this.mIsOverrideOrientationOnlyForCameraEnabled;
    }

    boolean shouldRefreshActivityForCameraCompat() {
        return this.mCameraCompatAllowRefreshOptProp.shouldEnableWithOptOutOverrideAndProperty(isCompatChangeEnabled(264304459L));
    }

    boolean shouldRefreshActivityViaPauseForCameraCompat() {
        return this.mCameraCompatEnableRefreshViaPauseOptProp.shouldEnableWithOverrideAndProperty(isCompatChangeEnabled(264301586L));
    }

    boolean shouldForceRotateForCameraCompat() {
        return this.mCameraCompatAllowForceRotationOptProp.shouldEnableWithOptOutOverrideAndProperty(isCompatChangeEnabled(263959004L));
    }

    boolean shouldApplyFreeformTreatmentForCameraCompat() {
        return com.android.window.flags.Flags.cameraCompatForFreeform() && !isCompatChangeEnabled(314961188L);
    }

    private boolean isCameraCompatTreatmentActive() {
        com.android.server.wm.DisplayContent displayContent = this.mActivityRecord.mDisplayContent;
        return (displayContent == null || displayContent.mDisplayRotationCompatPolicy == null || !displayContent.mDisplayRotationCompatPolicy.isTreatmentEnabledForActivity(this.mActivityRecord)) ? false : true;
    }

    int getFreeformCameraCompatMode() {
        return this.mFreeformCameraCompatMode;
    }

    void setFreeformCameraCompatMode(int freeformCameraCompatMode) {
        this.mFreeformCameraCompatMode = freeformCameraCompatMode;
    }

    private boolean isCompatChangeEnabled(long overrideChangeId) {
        return this.mActivityRecord.info.isChangeEnabled(overrideChangeId);
    }

    boolean hasWallpaperBackgroundForLetterbox() {
        return this.mShowWallpaperForLetterboxBackground;
    }

    android.graphics.Rect getLetterboxInsets() {
        if (this.mLetterbox != null) {
            return this.mLetterbox.getInsets();
        }
        return new android.graphics.Rect();
    }

    void getLetterboxInnerBounds(android.graphics.Rect outBounds) {
        if (this.mLetterbox != null) {
            outBounds.set(this.mLetterbox.getInnerFrame());
            com.android.server.wm.WindowState w = this.mActivityRecord.findMainWindow();
            if (w != null) {
                adjustBoundsForTaskbar(w, outBounds);
                return;
            }
            return;
        }
        outBounds.setEmpty();
    }

    private void getLetterboxOuterBounds(android.graphics.Rect outBounds) {
        if (this.mLetterbox != null) {
            outBounds.set(this.mLetterbox.getOuterFrame());
        } else {
            outBounds.setEmpty();
        }
    }

    boolean isFullyTransparentBarAllowed(android.graphics.Rect rect) {
        return this.mLetterbox == null || this.mLetterbox.notIntersectsOrFullyContains(rect);
    }

    void updateLetterboxSurfaceIfNeeded(com.android.server.wm.WindowState winHint) {
        updateLetterboxSurfaceIfNeeded(winHint, this.mActivityRecord.getSyncTransaction(), this.mActivityRecord.getPendingTransaction());
    }

    void updateLetterboxSurfaceIfNeeded(com.android.server.wm.WindowState winHint, android.view.SurfaceControl.Transaction t, android.view.SurfaceControl.Transaction inputT) {
        if (shouldNotLayoutLetterbox(winHint)) {
            return;
        }
        layoutLetterboxIfNeeded(winHint);
        if (this.mLetterbox != null && this.mLetterbox.needsApplySurfaceChanges()) {
            this.mLetterbox.applySurfaceChanges(t, inputT);
        }
    }

    void layoutLetterboxIfNeeded(com.android.server.wm.WindowState w) {
        android.graphics.Rect spaceToFill;
        if (shouldNotLayoutLetterbox(w)) {
            return;
        }
        updateRoundedCornersIfNeeded(w);
        updateWallpaperForLetterbox(w);
        if (shouldShowLetterboxUi(w)) {
            if (this.mLetterbox == null) {
                this.mLetterbox = new com.android.server.wm.Letterbox(new java.util.function.Supplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda7
                    @Override // java.util.function.Supplier
                    public final java.lang.Object get() {
                        return this.f$0.lambda$layoutLetterboxIfNeeded$1();
                    }
                }, this.mActivityRecord.mWmService.mTransactionFactory, new java.util.function.BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda8
                    @Override // java.util.function.BooleanSupplier
                    public final boolean getAsBoolean() {
                        return this.f$0.shouldLetterboxHaveRoundedCorners();
                    }
                }, new java.util.function.Supplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda9
                    @Override // java.util.function.Supplier
                    public final java.lang.Object get() {
                        return this.f$0.getLetterboxBackgroundColor();
                    }
                }, new java.util.function.BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda10
                    @Override // java.util.function.BooleanSupplier
                    public final boolean getAsBoolean() {
                        return this.f$0.hasWallpaperBackgroundForLetterbox();
                    }
                }, new java.util.function.IntSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda11
                    @Override // java.util.function.IntSupplier
                    public final int getAsInt() {
                        return this.f$0.getLetterboxWallpaperBlurRadiusPx();
                    }
                }, new java.util.function.DoubleSupplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda12
                    @Override // java.util.function.DoubleSupplier
                    public final double getAsDouble() {
                        return this.f$0.getLetterboxWallpaperDarkScrimAlpha();
                    }
                }, new java.util.function.IntConsumer() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda13
                    @Override // java.util.function.IntConsumer
                    public final void accept(int i) {
                        this.f$0.handleHorizontalDoubleTap(i);
                    }
                }, new java.util.function.IntConsumer() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda14
                    @Override // java.util.function.IntConsumer
                    public final void accept(int i) {
                        this.f$0.handleVerticalDoubleTap(i);
                    }
                }, new java.util.function.Supplier() { // from class: com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda15
                    @Override // java.util.function.Supplier
                    public final java.lang.Object get() {
                        return this.f$0.getLetterboxParentSurface();
                    }
                });
                this.mLetterbox.attachInput(w);
            }
            if (this.mActivityRecord.isInLetterboxAnimation()) {
                this.mActivityRecord.getTask().getPosition(this.mTmpPoint);
            } else {
                this.mActivityRecord.getPosition(this.mTmpPoint);
            }
            android.graphics.Rect transformedBounds = this.mActivityRecord.getFixedRotationTransformDisplayBounds();
            if (transformedBounds != null) {
                spaceToFill = transformedBounds;
            } else if (this.mActivityRecord.inMultiWindowMode() || (this.mActivityRecord.getRootTask() != null && this.mActivityRecord.getRootTask().getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0]))) {
                spaceToFill = this.mActivityRecord.getTaskFragment().getBounds();
            } else {
                spaceToFill = this.mActivityRecord.getRootTask().getParent().getBounds();
            }
            android.graphics.Rect innerFrame = this.mActivityRecord.mTransparentPolicy.isRunning() ? this.mActivityRecord.getBounds() : w.getFrame();
            this.mLetterbox.layout(spaceToFill, innerFrame, this.mTmpPoint);
            this.mExt.interceptLayoutLetterbox(spaceToFill, innerFrame, this.mTmpPoint, w, this.mLetterbox);
            if (this.mDoubleTapEvent) {
                this.mActivityRecord.getTask().dispatchTaskInfoChangedIfNeeded(true);
                return;
            }
            return;
        }
        if (this.mLetterbox != null) {
            this.mLetterbox.hide();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.view.SurfaceControl.Builder lambda$layoutLetterboxIfNeeded$1() {
        return this.mActivityRecord.makeChildSurface(null);
    }

    boolean isFromDoubleTap() {
        boolean isFromDoubleTap = this.mDoubleTapEvent;
        this.mDoubleTapEvent = false;
        return isFromDoubleTap;
    }

    android.view.SurfaceControl getLetterboxParentSurface() {
        if (this.mActivityRecord.isInLetterboxAnimation()) {
            return this.mActivityRecord.getTask().getSurfaceControl();
        }
        return this.mActivityRecord.getSurfaceControl();
    }

    private static boolean shouldNotLayoutLetterbox(com.android.server.wm.WindowState w) {
        if (w == null) {
            return true;
        }
        int type = w.mAttrs.type;
        return !(type == 1 || type == 3) || w.mAnimatingExit;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldLetterboxHaveRoundedCorners() {
        return this.mLetterboxConfiguration.isLetterboxActivityCornersRounded() && this.mActivityRecord.fillsParent();
    }

    private boolean isDisplayFullScreenAndInPosture(boolean isTabletop) {
        com.android.server.wm.Task task = this.mActivityRecord.getTask();
        return (this.mActivityRecord.mDisplayContent == null || task == null || !this.mActivityRecord.mDisplayContent.getDisplayRotation().isDeviceInPosture(com.android.server.wm.DeviceStateController.DeviceState.HALF_FOLDED, isTabletop) || this.mActivityRecord.mDisplayContent.inTransition() || task.getWindowingMode() != 1) ? false : true;
    }

    private boolean isDisplayFullScreenAndSeparatingHinge() {
        com.android.server.wm.Task task = this.mActivityRecord.getTask();
        return this.mActivityRecord.mDisplayContent != null && this.mActivityRecord.mDisplayContent.getDisplayRotation().isDisplaySeparatingHinge() && task != null && task.getWindowingMode() == 1;
    }

    float getHorizontalPositionMultiplier(android.content.res.Configuration parentConfiguration) {
        boolean bookModeEnabled = isFullScreenAndBookModeEnabled();
        if (isHorizontalReachabilityEnabled(parentConfiguration)) {
            return this.mLetterboxConfiguration.getHorizontalMultiplierForReachability(bookModeEnabled);
        }
        return this.mLetterboxConfiguration.getLetterboxHorizontalPositionMultiplier(bookModeEnabled);
    }

    private boolean isFullScreenAndBookModeEnabled() {
        return isDisplayFullScreenAndInPosture(false) && this.mLetterboxConfiguration.getIsAutomaticReachabilityInBookModeEnabled();
    }

    float getVerticalPositionMultiplier(android.content.res.Configuration parentConfiguration) {
        boolean tabletopMode = isDisplayFullScreenAndInPosture(true);
        if (isVerticalReachabilityEnabled(parentConfiguration)) {
            return this.mLetterboxConfiguration.getVerticalMultiplierForReachability(tabletopMode);
        }
        return this.mLetterboxConfiguration.getLetterboxVerticalPositionMultiplier(tabletopMode);
    }

    float getFixedOrientationLetterboxAspectRatio(android.content.res.Configuration parentConfiguration) {
        if (shouldUseSplitScreenAspectRatio(parentConfiguration)) {
            return getSplitScreenAspectRatio();
        }
        if (this.mActivityRecord.shouldCreateCompatDisplayInsets()) {
            return getDefaultMinAspectRatioForUnresizableApps();
        }
        return getDefaultMinAspectRatio();
    }

    void recomputeConfigurationForCameraCompatIfNeeded() {
        if (isOverrideOrientationOnlyForCameraEnabled() || isCameraCompatSplitScreenAspectRatioAllowed() || shouldOverrideMinAspectRatioForCamera()) {
            this.mActivityRecord.recomputeConfiguration();
        }
    }

    boolean isLetterboxEducationEnabled() {
        return this.mLetterboxConfiguration.getIsEducationEnabled();
    }

    boolean isCameraCompatSplitScreenAspectRatioAllowed() {
        return this.mLetterboxConfiguration.isCameraCompatSplitScreenAspectRatioEnabled() && !this.mActivityRecord.shouldCreateCompatDisplayInsets();
    }

    private boolean shouldUseSplitScreenAspectRatio(android.content.res.Configuration parentConfiguration) {
        boolean isBookMode = isDisplayFullScreenAndInPosture(false);
        boolean isNotCenteredHorizontally = getHorizontalPositionMultiplier(parentConfiguration) != 0.5f;
        boolean isTabletopMode = isDisplayFullScreenAndInPosture(true);
        boolean isLandscape = android.content.pm.ActivityInfo.isFixedOrientationLandscape(this.mActivityRecord.getOverrideOrientation());
        return (isBookMode && isNotCenteredHorizontally) || (isTabletopMode && isLandscape) || (isCameraCompatSplitScreenAspectRatioAllowed() && isCameraCompatTreatmentActive());
    }

    private float getDefaultMinAspectRatioForUnresizableApps() {
        if (this.mLetterboxConfiguration.getIsSplitScreenAspectRatioForUnresizableAppsEnabled() && this.mActivityRecord.getDisplayArea() != null) {
            return getSplitScreenAspectRatio();
        }
        if (this.mLetterboxConfiguration.getDefaultMinAspectRatioForUnresizableApps() > 1.0f) {
            return this.mLetterboxConfiguration.getDefaultMinAspectRatioForUnresizableApps();
        }
        return getDefaultMinAspectRatio();
    }

    boolean isVerticalThinLetterboxed() {
        com.android.server.wm.Task task;
        int thinHeight = this.mLetterboxConfiguration.getThinLetterboxHeightPx();
        if (thinHeight < 0 || (task = this.mActivityRecord.getTask()) == null) {
            return false;
        }
        int padding = java.lang.Math.abs(task.getBounds().height() - this.mActivityRecord.getBounds().height()) / 2;
        return padding <= thinHeight;
    }

    boolean isHorizontalThinLetterboxed() {
        com.android.server.wm.Task task;
        int thinWidth = this.mLetterboxConfiguration.getThinLetterboxWidthPx();
        if (thinWidth < 0 || (task = this.mActivityRecord.getTask()) == null) {
            return false;
        }
        int padding = java.lang.Math.abs(task.getBounds().width() - this.mActivityRecord.getBounds().width()) / 2;
        return padding <= thinWidth;
    }

    boolean allowVerticalReachabilityForThinLetterbox() {
        if (com.android.window.flags.Flags.disableThinLetterboxingPolicy()) {
            return !isVerticalThinLetterboxed();
        }
        return true;
    }

    boolean allowHorizontalReachabilityForThinLetterbox() {
        if (com.android.window.flags.Flags.disableThinLetterboxingPolicy()) {
            return !isHorizontalThinLetterboxed();
        }
        return true;
    }

    float getSplitScreenAspectRatio() {
        com.android.server.wm.DisplayArea displayArea = this.mActivityRecord.getDisplayArea();
        if (displayArea == null) {
            return getDefaultMinAspectRatioForUnresizableApps();
        }
        int dividerWindowWidth = getResources().getDimensionPixelSize(android.R.dimen.default_magnifier_corner_radius);
        int dividerInsets = com.android.internal.policy.DockedDividerUtils.getDividerInsets(getResources());
        int dividerSize = dividerWindowWidth - (dividerInsets * 2);
        android.graphics.Rect bounds = new android.graphics.Rect(displayArea.getWindowConfiguration().getAppBounds());
        if (bounds.width() >= bounds.height()) {
            bounds.inset(dividerSize / 2, 0);
            bounds.right = bounds.centerX();
        } else {
            bounds.inset(0, dividerSize / 2);
            bounds.bottom = bounds.centerY();
        }
        return com.android.server.wm.ActivityRecord.computeAspectRatio(bounds);
    }

    boolean shouldEnableUserAspectRatioSettings() {
        return !this.mAllowUserAspectRatioOverrideOptProp.isFalse() && this.mLetterboxConfiguration.isUserAppAspectRatioSettingsEnabled() && this.mActivityRecord.mDisplayContent != null && this.mActivityRecord.mDisplayContent.getIgnoreOrientationRequest();
    }

    boolean shouldApplyUserMinAspectRatioOverride() {
        if (!shouldEnableUserAspectRatioSettings()) {
            return false;
        }
        this.mUserAspectRatio = getUserMinAspectRatioOverrideCode();
        return (this.mUserAspectRatio == 0 || this.mUserAspectRatio == 7 || this.mUserAspectRatio == 6) ? false : true;
    }

    boolean isUserFullscreenOverrideEnabled() {
        if (this.mAllowUserAspectRatioOverrideOptProp.isFalse() || this.mAllowUserAspectRatioFullscreenOverrideOptProp.isFalse() || !this.mLetterboxConfiguration.isUserAppAspectRatioFullscreenEnabled()) {
            return false;
        }
        return true;
    }

    boolean shouldApplyUserFullscreenOverride() {
        if (!isUserFullscreenOverrideEnabled()) {
            return false;
        }
        this.mUserAspectRatio = getUserMinAspectRatioOverrideCode();
        return this.mUserAspectRatio == 6;
    }

    boolean isSystemOverrideToFullscreenEnabled() {
        return this.mIsSystemOverrideToFullscreenEnabled && !this.mAllowOrientationOverrideOptProp.isFalse() && (this.mUserAspectRatio == 0 || this.mUserAspectRatio == 6);
    }

    boolean hasFullscreenOverride() {
        return shouldApplyUserFullscreenOverride() || isSystemOverrideToFullscreenEnabled();
    }

    float getUserMinAspectRatio() {
        switch (this.mUserAspectRatio) {
            case 1:
                return getSplitScreenAspectRatio();
            case 2:
                return getDisplaySizeMinAspectRatio();
            case 3:
                return 1.3333334f;
            case 4:
                return 1.7777778f;
            case 5:
                return 1.5f;
            default:
                throw new java.lang.AssertionError("Unexpected user min aspect ratio override: " + this.mUserAspectRatio);
        }
    }

    int getUserMinAspectRatioOverrideCode() {
        try {
            return this.mActivityRecord.mAtmService.getPackageManager().getUserMinAspectRatio(this.mActivityRecord.packageName, this.mActivityRecord.mUserId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Exception thrown retrieving aspect ratio user override " + this, e);
            return this.mUserAspectRatio;
        }
    }

    private float getDisplaySizeMinAspectRatio() {
        com.android.server.wm.DisplayArea displayArea = this.mActivityRecord.getDisplayArea();
        if (displayArea == null) {
            return this.mActivityRecord.info.getMinAspectRatio();
        }
        android.graphics.Rect bounds = new android.graphics.Rect(displayArea.getWindowConfiguration().getAppBounds());
        return com.android.server.wm.ActivityRecord.computeAspectRatio(bounds);
    }

    private float getDefaultMinAspectRatio() {
        if (this.mActivityRecord.getDisplayArea() == null || !this.mLetterboxConfiguration.getIsDisplayAspectRatioEnabledForFixedOrientationLetterbox()) {
            return this.mLetterboxConfiguration.getFixedOrientationLetterboxAspectRatio();
        }
        return getDisplaySizeMinAspectRatio();
    }

    android.content.res.Resources getResources() {
        return this.mActivityRecord.mWmService.mContext.getResources();
    }

    int getLetterboxPositionForVerticalReachability() {
        boolean isInFullScreenTabletopMode = isDisplayFullScreenAndSeparatingHinge();
        return this.mLetterboxConfiguration.getLetterboxPositionForVerticalReachability(isInFullScreenTabletopMode);
    }

    int getLetterboxPositionForHorizontalReachability() {
        boolean isInFullScreenBookMode = isFullScreenAndBookModeEnabled();
        return this.mLetterboxConfiguration.getLetterboxPositionForHorizontalReachability(isInFullScreenBookMode);
    }

    void handleHorizontalDoubleTap(int x) {
        int changeToLog;
        int changeToLog2;
        if (!isHorizontalReachabilityEnabled() || this.mActivityRecord.isInTransition()) {
            return;
        }
        if (this.mLetterbox.getInnerFrame().left <= x && this.mLetterbox.getInnerFrame().right >= x) {
            return;
        }
        boolean isInFullScreenBookMode = isDisplayFullScreenAndSeparatingHinge() && this.mLetterboxConfiguration.getIsAutomaticReachabilityInBookModeEnabled();
        int letterboxPositionForHorizontalReachability = this.mLetterboxConfiguration.getLetterboxPositionForHorizontalReachability(isInFullScreenBookMode);
        if (this.mLetterbox.getInnerFrame().left > x) {
            this.mLetterboxConfiguration.movePositionForHorizontalReachabilityToNextLeftStop(isInFullScreenBookMode);
            if (letterboxPositionForHorizontalReachability == 1) {
                changeToLog2 = 1;
            } else {
                changeToLog2 = 4;
            }
            logLetterboxPositionChange(changeToLog2);
            this.mDoubleTapEvent = true;
        } else if (this.mLetterbox.getInnerFrame().right < x) {
            this.mLetterboxConfiguration.movePositionForHorizontalReachabilityToNextRightStop(isInFullScreenBookMode);
            if (letterboxPositionForHorizontalReachability == 1) {
                changeToLog = 3;
            } else {
                changeToLog = 2;
            }
            logLetterboxPositionChange(changeToLog);
            this.mDoubleTapEvent = true;
        }
        this.mActivityRecord.recomputeConfiguration();
    }

    void handleVerticalDoubleTap(int y) {
        int changeToLog;
        int changeToLog2;
        if (!isVerticalReachabilityEnabled() || this.mActivityRecord.isInTransition()) {
            return;
        }
        if (this.mLetterbox.getInnerFrame().top <= y && this.mLetterbox.getInnerFrame().bottom >= y) {
            return;
        }
        boolean isInFullScreenTabletopMode = isDisplayFullScreenAndSeparatingHinge();
        int letterboxPositionForVerticalReachability = this.mLetterboxConfiguration.getLetterboxPositionForVerticalReachability(isInFullScreenTabletopMode);
        if (this.mLetterbox.getInnerFrame().top > y) {
            this.mLetterboxConfiguration.movePositionForVerticalReachabilityToNextTopStop(isInFullScreenTabletopMode);
            if (letterboxPositionForVerticalReachability == 1) {
                changeToLog2 = 5;
            } else {
                changeToLog2 = 8;
            }
            logLetterboxPositionChange(changeToLog2);
            this.mDoubleTapEvent = true;
        } else if (this.mLetterbox.getInnerFrame().bottom < y) {
            this.mLetterboxConfiguration.movePositionForVerticalReachabilityToNextBottomStop(isInFullScreenTabletopMode);
            if (letterboxPositionForVerticalReachability == 1) {
                changeToLog = 7;
            } else {
                changeToLog = 6;
            }
            logLetterboxPositionChange(changeToLog);
            this.mDoubleTapEvent = true;
        }
        this.mActivityRecord.recomputeConfiguration();
    }

    private boolean isHorizontalReachabilityEnabled(android.content.res.Configuration parentConfiguration) {
        if (!allowHorizontalReachabilityForThinLetterbox()) {
            return false;
        }
        android.graphics.Rect parentAppBoundsOverride = this.mActivityRecord.getParentAppBoundsOverride();
        android.graphics.Rect parentAppBounds = parentAppBoundsOverride != null ? parentAppBoundsOverride : parentConfiguration.windowConfiguration.getAppBounds();
        android.graphics.Rect opaqueActivityBounds = (android.graphics.Rect) this.mActivityRecord.mTransparentPolicy.getFirstOpaqueActivity().map(new com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda6()).orElse(this.mActivityRecord.getScreenResolvedBounds());
        return this.mLetterboxConfiguration.getIsHorizontalReachabilityEnabled() && parentConfiguration.windowConfiguration.getWindowingMode() == 1 && parentAppBounds.height() <= opaqueActivityBounds.height() && parentAppBounds.width() > opaqueActivityBounds.width();
    }

    boolean isHorizontalReachabilityEnabled() {
        return isHorizontalReachabilityEnabled(this.mActivityRecord.getParent().getConfiguration());
    }

    boolean isLetterboxDoubleTapEducationEnabled() {
        return isHorizontalReachabilityEnabled() || isVerticalReachabilityEnabled();
    }

    private boolean isVerticalReachabilityEnabled(android.content.res.Configuration parentConfiguration) {
        if (!allowVerticalReachabilityForThinLetterbox()) {
            return false;
        }
        android.graphics.Rect parentAppBoundsOverride = this.mActivityRecord.getParentAppBoundsOverride();
        android.graphics.Rect parentAppBounds = parentAppBoundsOverride != null ? parentAppBoundsOverride : parentConfiguration.windowConfiguration.getAppBounds();
        android.graphics.Rect opaqueActivityBounds = (android.graphics.Rect) this.mActivityRecord.mTransparentPolicy.getFirstOpaqueActivity().map(new com.android.server.wm.LetterboxUiController$$ExternalSyntheticLambda6()).orElse(this.mActivityRecord.getScreenResolvedBounds());
        return this.mLetterboxConfiguration.getIsVerticalReachabilityEnabled() && parentConfiguration.windowConfiguration.getWindowingMode() == 1 && parentAppBounds.width() <= opaqueActivityBounds.width() && parentAppBounds.height() > opaqueActivityBounds.height();
    }

    boolean isVerticalReachabilityEnabled() {
        return isVerticalReachabilityEnabled(this.mActivityRecord.getParent().getConfiguration());
    }

    boolean shouldShowLetterboxUi(com.android.server.wm.WindowState mainWindow) {
        boolean shouldShowLetterboxUi = false;
        if (!mainWindow.getWrapper().getExtImpl().letterBoxEnabledForCompactWin(mainWindow)) {
            return false;
        }
        boolean surfaceReady = this.mActivityRecord.isVisible() || this.mActivityRecord.isVisibleRequested();
        if (mainWindow.getWrapper().getExtImpl().needLetterBoxSurface(surfaceReady, this.mActivityRecord, mainWindow)) {
            return true;
        }
        if (this.mIsRelaunchingAfterRequestedOrientationChanged) {
            return this.mLastShouldShowLetterboxUi;
        }
        if ((this.mActivityRecord.isInLetterboxAnimation() || this.mActivityRecord.isVisible() || mainWindow.getWrapper().getExtImpl().shouldShowLetterboxUi(mainWindow) || this.mActivityRecord.isVisibleRequested()) && mainWindow.areAppWindowBoundsLetterboxed() && (mainWindow.getAttrs().flags & 1048576) == 0) {
            shouldShowLetterboxUi = true;
        }
        this.mLastShouldShowLetterboxUi = shouldShowLetterboxUi;
        return shouldShowLetterboxUi;
    }

    android.graphics.Color getLetterboxBackgroundColor() {
        com.android.server.wm.WindowState w = this.mActivityRecord.findMainWindow();
        if (w == null || w.isLetterboxedForDisplayCutout() || this.mExt.shouldUseBlackLetterboxBackground(this.mActivityRecord)) {
            return android.graphics.Color.valueOf(android.hardware.audio.common.V2_0.AudioFormat.MAIN_MASK);
        }
        int letterboxBackgroundType = this.mLetterboxConfiguration.getLetterboxBackgroundType();
        android.app.ActivityManager.TaskDescription taskDescription = this.mActivityRecord.taskDescription;
        switch (letterboxBackgroundType) {
            case 0:
                return this.mLetterboxConfiguration.getLetterboxBackgroundColor();
            case 1:
                if (taskDescription != null && taskDescription.getBackgroundColor() != 0) {
                    return android.graphics.Color.valueOf(taskDescription.getBackgroundColor());
                }
                break;
            case 2:
                if (taskDescription != null && taskDescription.getBackgroundColorFloating() != 0) {
                    return android.graphics.Color.valueOf(taskDescription.getBackgroundColorFloating());
                }
                break;
            case 3:
                if (hasWallpaperBackgroundForLetterbox()) {
                    return this.mLetterboxConfiguration.getLetterboxBackgroundColor();
                }
                android.util.Slog.w(TAG, "Wallpaper option is selected for letterbox background but blur is not supported by a device or not supported in the current window configuration or both alpha scrim and blur radius aren't provided so using solid color background");
                break;
                break;
            default:
                throw new java.lang.AssertionError("Unexpected letterbox background type: " + letterboxBackgroundType);
        }
        return this.mLetterboxConfiguration.getLetterboxBackgroundColor();
    }

    private void updateRoundedCornersIfNeeded(com.android.server.wm.WindowState mainWindow) {
        android.view.SurfaceControl windowSurface = mainWindow.getSurfaceControl();
        if (windowSurface == null || !windowSurface.isValid()) {
            return;
        }
        this.mActivityRecord.getSyncTransaction().setCrop(windowSurface, getCropBoundsIfNeeded(mainWindow)).setCornerRadius(windowSurface, getRoundedCornersRadius(mainWindow));
    }

    android.graphics.Rect getCropBoundsIfNeeded(com.android.server.wm.WindowState mainWindow) {
        if (!requiresRoundedCorners(mainWindow) || this.mActivityRecord.isInLetterboxAnimation()) {
            return null;
        }
        android.graphics.Rect cropBounds = new android.graphics.Rect(this.mActivityRecord.getBounds());
        if (this.mActivityRecord.mTransparentPolicy.isRunning() && (cropBounds.width() != mainWindow.mRequestedWidth || cropBounds.height() != mainWindow.mRequestedHeight)) {
            return null;
        }
        adjustBoundsForTaskbar(mainWindow, cropBounds);
        float scale = mainWindow.mInvGlobalScale;
        if (scale != 1.0f && scale > 0.0f) {
            cropBounds.scale(scale);
        }
        cropBounds.offsetTo(0, 0);
        return cropBounds;
    }

    private boolean requiresRoundedCorners(com.android.server.wm.WindowState mainWindow) {
        return isLetterboxedNotForDisplayCutout(mainWindow) && this.mLetterboxConfiguration.isLetterboxActivityCornersRounded();
    }

    int getRoundedCornersRadius(com.android.server.wm.WindowState mainWindow) {
        int radius;
        if (!requiresRoundedCorners(mainWindow)) {
            return 0;
        }
        if (this.mLetterboxConfiguration.getLetterboxActivityCornersRadius() >= 0) {
            radius = this.mLetterboxConfiguration.getLetterboxActivityCornersRadius();
        } else {
            android.view.InsetsState insetsState = mainWindow.getInsetsState();
            radius = java.lang.Math.min(getInsetsStateCornerRadius(insetsState, 3), getInsetsStateCornerRadius(insetsState, 2));
        }
        float scale = mainWindow.mInvGlobalScale;
        return (scale == 1.0f || scale <= 0.0f) ? radius : (int) (radius * scale);
    }

    android.view.InsetsSource getExpandedTaskbarOrNull(com.android.server.wm.WindowState mainWindow) {
        android.view.InsetsState state = mainWindow.getInsetsState();
        for (int i = state.sourceSize() - 1; i >= 0; i--) {
            android.view.InsetsSource source = state.sourceAt(i);
            if (source.getType() == android.view.WindowInsets.Type.navigationBars() && source.hasFlags(2) && source.isVisible()) {
                return source;
            }
        }
        return null;
    }

    boolean getIsRelaunchingAfterRequestedOrientationChanged() {
        return this.mIsRelaunchingAfterRequestedOrientationChanged;
    }

    private void adjustBoundsForTaskbar(com.android.server.wm.WindowState mainWindow, android.graphics.Rect bounds) {
        android.view.InsetsSource expandedTaskbarOrNull = getExpandedTaskbarOrNull(mainWindow);
        if (expandedTaskbarOrNull != null) {
            bounds.bottom = java.lang.Math.min(bounds.bottom, expandedTaskbarOrNull.getFrame().top);
        }
    }

    private int getInsetsStateCornerRadius(android.view.InsetsState insetsState, int position) {
        android.view.RoundedCorner corner = insetsState.getRoundedCorners().getRoundedCorner(position);
        if (corner == null) {
            return 0;
        }
        return corner.getRadius();
    }

    private boolean isLetterboxedNotForDisplayCutout(com.android.server.wm.WindowState mainWindow) {
        return shouldShowLetterboxUi(mainWindow) && !mainWindow.isLetterboxedForDisplayCutout();
    }

    private void updateWallpaperForLetterbox(com.android.server.wm.WindowState mainWindow) {
        int letterboxBackgroundType = this.mLetterboxConfiguration.getLetterboxBackgroundType();
        boolean wallpaperShouldBeShown = letterboxBackgroundType == 3 && isLetterboxedNotForDisplayCutout(mainWindow) && (getLetterboxWallpaperBlurRadiusPx() > 0 || getLetterboxWallpaperDarkScrimAlpha() > 0.0f) && (getLetterboxWallpaperBlurRadiusPx() <= 0 || isLetterboxWallpaperBlurSupported());
        if (this.mShowWallpaperForLetterboxBackground != wallpaperShouldBeShown) {
            this.mShowWallpaperForLetterboxBackground = wallpaperShouldBeShown;
            this.mActivityRecord.requestUpdateWallpaperIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getLetterboxWallpaperBlurRadiusPx() {
        int blurRadius = this.mLetterboxConfiguration.getLetterboxBackgroundWallpaperBlurRadiusPx();
        return java.lang.Math.max(blurRadius, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getLetterboxWallpaperDarkScrimAlpha() {
        float alpha = this.mLetterboxConfiguration.getLetterboxBackgroundWallpaperDarkScrimAlpha();
        if (alpha < 0.0f || alpha >= 1.0f) {
            return 0.0f;
        }
        return alpha;
    }

    private boolean isLetterboxWallpaperBlurSupported() {
        return ((android.view.WindowManager) this.mLetterboxConfiguration.mContext.getSystemService(android.view.WindowManager.class)).isCrossWindowBlurEnabled();
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        com.android.server.wm.WindowState mainWin = this.mActivityRecord.findMainWindow();
        if (mainWin == null) {
            return;
        }
        boolean areBoundsLetterboxed = mainWin.areAppWindowBoundsLetterboxed();
        pw.println(prefix + "areBoundsLetterboxed=" + areBoundsLetterboxed);
        if (!areBoundsLetterboxed) {
            return;
        }
        pw.println(prefix + "  letterboxReason=" + getLetterboxReasonString(mainWin));
        pw.println(prefix + "  activityAspectRatio=" + com.android.server.wm.ActivityRecord.computeAspectRatio(this.mActivityRecord.getBounds()));
        boolean shouldShowLetterboxUi = shouldShowLetterboxUi(mainWin);
        pw.println(prefix + "shouldShowLetterboxUi=" + shouldShowLetterboxUi);
        if (!shouldShowLetterboxUi) {
            return;
        }
        pw.println(prefix + "  isVerticalThinLetterboxed=" + isVerticalThinLetterboxed());
        pw.println(prefix + "  isHorizontalThinLetterboxed=" + isHorizontalThinLetterboxed());
        pw.println(prefix + "  letterboxBackgroundColor=" + java.lang.Integer.toHexString(getLetterboxBackgroundColor().toArgb()));
        pw.println(prefix + "  letterboxBackgroundType=" + com.android.server.wm.LetterboxConfiguration.letterboxBackgroundTypeToString(this.mLetterboxConfiguration.getLetterboxBackgroundType()));
        pw.println(prefix + "  letterboxCornerRadius=" + getRoundedCornersRadius(mainWin));
        if (this.mLetterboxConfiguration.getLetterboxBackgroundType() == 3) {
            pw.println(prefix + "  isLetterboxWallpaperBlurSupported=" + isLetterboxWallpaperBlurSupported());
            pw.println(prefix + "  letterboxBackgroundWallpaperDarkScrimAlpha=" + getLetterboxWallpaperDarkScrimAlpha());
            pw.println(prefix + "  letterboxBackgroundWallpaperBlurRadius=" + getLetterboxWallpaperBlurRadiusPx());
        }
        pw.println(prefix + "  isHorizontalReachabilityEnabled=" + isHorizontalReachabilityEnabled());
        pw.println(prefix + "  isVerticalReachabilityEnabled=" + isVerticalReachabilityEnabled());
        pw.println(prefix + "  letterboxHorizontalPositionMultiplier=" + getHorizontalPositionMultiplier(this.mActivityRecord.getParent().getConfiguration()));
        pw.println(prefix + "  letterboxVerticalPositionMultiplier=" + getVerticalPositionMultiplier(this.mActivityRecord.getParent().getConfiguration()));
        pw.println(prefix + "  letterboxPositionForHorizontalReachability=" + com.android.server.wm.LetterboxConfiguration.letterboxHorizontalReachabilityPositionToString(this.mLetterboxConfiguration.getLetterboxPositionForHorizontalReachability(false)));
        pw.println(prefix + "  letterboxPositionForVerticalReachability=" + com.android.server.wm.LetterboxConfiguration.letterboxVerticalReachabilityPositionToString(this.mLetterboxConfiguration.getLetterboxPositionForVerticalReachability(false)));
        pw.println(prefix + "  fixedOrientationLetterboxAspectRatio=" + this.mLetterboxConfiguration.getFixedOrientationLetterboxAspectRatio());
        pw.println(prefix + "  defaultMinAspectRatioForUnresizableApps=" + this.mLetterboxConfiguration.getDefaultMinAspectRatioForUnresizableApps());
        pw.println(prefix + "  isSplitScreenAspectRatioForUnresizableAppsEnabled=" + this.mLetterboxConfiguration.getIsSplitScreenAspectRatioForUnresizableAppsEnabled());
        pw.println(prefix + "  isDisplayAspectRatioEnabledForFixedOrientationLetterbox=" + this.mLetterboxConfiguration.getIsDisplayAspectRatioEnabledForFixedOrientationLetterbox());
    }

    private java.lang.String getLetterboxReasonString(com.android.server.wm.WindowState mainWin) {
        if (this.mActivityRecord.inSizeCompatMode()) {
            return "SIZE_COMPAT_MODE";
        }
        if (this.mActivityRecord.isLetterboxedForFixedOrientationAndAspectRatio()) {
            return "FIXED_ORIENTATION";
        }
        if (mainWin.isLetterboxedForDisplayCutout()) {
            return "DISPLAY_CUTOUT";
        }
        if (this.mActivityRecord.isLetterboxedForAspectRatioOnly()) {
            return "ASPECT_RATIO";
        }
        return "UNKNOWN_REASON";
    }

    private int letterboxHorizontalReachabilityPositionToLetterboxPosition(int position) {
        switch (position) {
            case 0:
                return 3;
            case 1:
                return 2;
            case 2:
                return 4;
            default:
                throw new java.lang.AssertionError("Unexpected letterbox horizontal reachability position type: " + position);
        }
    }

    private int letterboxVerticalReachabilityPositionToLetterboxPosition(int position) {
        switch (position) {
            case 0:
                return 5;
            case 1:
                return 2;
            case 2:
                return 6;
            default:
                throw new java.lang.AssertionError("Unexpected letterbox vertical reachability position type: " + position);
        }
    }

    int getLetterboxPositionForLogging() {
        if (isHorizontalReachabilityEnabled()) {
            int letterboxPositionForHorizontalReachability = getLetterboxConfiguration().getLetterboxPositionForHorizontalReachability(isDisplayFullScreenAndInPosture(false));
            int positionToLog = letterboxHorizontalReachabilityPositionToLetterboxPosition(letterboxPositionForHorizontalReachability);
            return positionToLog;
        }
        if (!isVerticalReachabilityEnabled()) {
            return 0;
        }
        int letterboxPositionForVerticalReachability = getLetterboxConfiguration().getLetterboxPositionForVerticalReachability(isDisplayFullScreenAndInPosture(true));
        int positionToLog2 = letterboxVerticalReachabilityPositionToLetterboxPosition(letterboxPositionForVerticalReachability);
        return positionToLog2;
    }

    private com.android.server.wm.LetterboxConfiguration getLetterboxConfiguration() {
        return this.mLetterboxConfiguration;
    }

    private void logLetterboxPositionChange(int letterboxPositionChange) {
        this.mActivityRecord.mTaskSupervisor.getActivityMetricsLogger().logLetterboxPositionChange(this.mActivityRecord, letterboxPositionChange);
    }

    com.android.internal.statusbar.LetterboxDetails getLetterboxDetails() {
        com.android.server.wm.WindowState w = this.mActivityRecord.findMainWindow();
        if (this.mLetterbox == null || w == null || w.isLetterboxedForDisplayCutout()) {
            return null;
        }
        android.graphics.Rect letterboxInnerBounds = new android.graphics.Rect();
        android.graphics.Rect letterboxOuterBounds = new android.graphics.Rect();
        getLetterboxInnerBounds(letterboxInnerBounds);
        getLetterboxOuterBounds(letterboxOuterBounds);
        if (letterboxInnerBounds.isEmpty() || letterboxOuterBounds.isEmpty()) {
            return null;
        }
        return new com.android.internal.statusbar.LetterboxDetails(letterboxInnerBounds, letterboxOuterBounds, w.mAttrs.insetsFlags.appearance);
    }

    private static java.util.function.BooleanSupplier asLazy(final java.util.function.BooleanSupplier supplier) {
        return new java.util.function.BooleanSupplier() { // from class: com.android.server.wm.LetterboxUiController.1
            private boolean mRead;
            private boolean mValue;

            @Override // java.util.function.BooleanSupplier
            public boolean getAsBoolean() {
                if (!this.mRead) {
                    this.mRead = true;
                    this.mValue = supplier.getAsBoolean();
                }
                return this.mValue;
            }
        };
    }
}
