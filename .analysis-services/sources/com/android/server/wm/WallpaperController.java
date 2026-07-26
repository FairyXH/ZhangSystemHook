package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WallpaperController {
    private static final java.lang.String TAG = "WindowManager";
    private static final int WALLPAPER_DRAW_NORMAL = 0;
    private static final int WALLPAPER_DRAW_PENDING = 1;
    private static final long WALLPAPER_DRAW_PENDING_TIMEOUT_DURATION = 500;
    private static final int WALLPAPER_DRAW_TIMEOUT = 2;
    private static final long WALLPAPER_TIMEOUT = 150;
    private static final long WALLPAPER_TIMEOUT_RECOVERY = 10000;
    private com.android.server.wm.DisplayContent mDisplayContent;
    private volatile boolean mIsWallpaperNotifiedOnDisplaySwitch;
    private long mLastWallpaperTimeoutTime;
    private float mMaxWallpaperScale;
    private float mMinWallpaperScale;
    private com.android.server.wm.WindowManagerService mService;
    private boolean mShouldOffsetWallpaperCenter;
    private com.android.server.wm.WindowState mWaitingOnWallpaper;
    private com.android.server.wallpaper.WallpaperCropper.WallpaperCropUtils mWallpaperCropUtils = null;
    private final java.util.ArrayList<com.android.server.wm.WallpaperWindowToken> mWallpaperTokens = new java.util.ArrayList<>();
    private com.android.server.wm.WindowState mWallpaperTarget = null;
    private com.android.server.wm.WindowState mPrevWallpaperTarget = null;
    private float mLastWallpaperZoomOut = 1.0f;
    private boolean mLastFrozen = false;
    private int mWallpaperDrawState = 0;
    private android.graphics.Point mLargestDisplaySize = null;
    public com.android.server.wm.IWallpaperControllerExt mWallpaperControllerExt = (com.android.server.wm.IWallpaperControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWallpaperControllerExt.class).base(this).create();
    private final com.android.server.wm.WallpaperController.FindWallpaperTargetResult mFindResults = new com.android.server.wm.WallpaperController.FindWallpaperTargetResult();
    private final com.android.internal.util.ToBooleanFunction<com.android.server.wm.WindowState> mFindWallpaperTargetFunction = new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.wm.WallpaperController$$ExternalSyntheticLambda0
        public final boolean apply(java.lang.Object obj) {
            return this.f$0.lambda$new$0((com.android.server.wm.WindowState) obj);
        }
    };
    private java.util.function.Consumer<com.android.server.wm.WindowState> mComputeMaxZoomOutFunction = new java.util.function.Consumer() { // from class: com.android.server.wm.WallpaperController$$ExternalSyntheticLambda1
        @Override // java.util.function.Consumer
        public final void accept(java.lang.Object obj) {
            this.f$0.lambda$new$1((com.android.server.wm.WindowState) obj);
        }
    };
    private com.android.server.wm.IWallpaperControllerWrapper mWallpaperControllerWrapper = new com.android.server.wm.WallpaperController.WallpaperControllerWrapperImpl();

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(com.android.server.wm.WindowState w) {
        boolean useShellTransition = w.mTransitionController.isShellTransitionsEnabled();
        if (!useShellTransition) {
            if (w.mActivityRecord != null && !w.mActivityRecord.isVisible() && !w.mActivityRecord.isAnimating(3)) {
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                    android.util.Slog.v(TAG, "Skipping hidden and not animating token: " + w);
                }
                return false;
            }
        } else {
            com.android.server.wm.ActivityRecord ar = w.mActivityRecord;
            if (ar != null && !ar.isVisibleRequested() && !ar.isVisible()) {
                return false;
            }
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WALLPAPER) {
            android.util.Slog.v(TAG, "Win " + w + ": isOnScreen=" + w.isOnScreen() + " mDrawState=" + w.mWinAnimator.mDrawState + " mSurfaceShown=" + w.mWinAnimator.getShown());
        }
        com.android.server.wm.WindowContainer animatingContainer = w.mActivityRecord != null ? w.mActivityRecord.getAnimatingContainer() : null;
        if (!useShellTransition && animatingContainer != null && animatingContainer.isAnimating(3) && com.android.server.wm.AppTransition.isKeyguardGoingAwayTransitOld(animatingContainer.mTransit) && (animatingContainer.mTransitFlags & 4) != 0) {
            this.mFindResults.setUseTopWallpaperAsTarget(true);
        }
        if (this.mService.mPolicy.isKeyguardLocked()) {
            if (w.canShowWhenLocked()) {
                if (this.mService.mPolicy.isKeyguardOccluded() || (!useShellTransition ? this.mService.mPolicy.isKeyguardUnoccluding() : w.inTransition())) {
                    this.mFindResults.mNeedsShowWhenLockedWallpaper = (isFullscreen(w.mAttrs) && (w.mActivityRecord == null || w.mActivityRecord.fillsParent())) ? false : true;
                }
            } else if (w.hasWallpaper() && this.mService.mPolicy.isKeyguardHostWindow(w.mAttrs) && w.mTransitionController.hasTransientLaunch(this.mDisplayContent)) {
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                    android.util.Slog.v(TAG, "Found keyguard as wallpaper target: " + w);
                }
                this.mFindResults.setWallpaperTarget(w);
                return false;
            }
        }
        boolean animationWallpaper = (animatingContainer == null || animatingContainer.getAnimation() == null || !animatingContainer.getAnimation().getShowWallpaper()) ? false : true;
        boolean hasWallpaper = w.hasWallpaper() || animationWallpaper;
        if (isRecentsTransitionTarget(w) || isBackNavigationTarget(w)) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                android.util.Slog.v(TAG, "Found recents animation wallpaper target: " + w);
            }
            this.mFindResults.setWallpaperTarget(w);
            return true;
        }
        if (this.mService.getWrapper().getExtImpl().isGestureAnimationWapaperTarget(w)) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                android.util.Slog.v(TAG, "Found gesture animation wallpaper target: " + w);
            }
            this.mFindResults.setWallpaperTarget(w);
            return true;
        }
        if (!hasWallpaper || !w.isOnScreen() || (this.mWallpaperTarget != w && !w.isDrawFinishedLw())) {
            return false;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WALLPAPER) {
            android.util.Slog.v(TAG, "Found wallpaper target: " + w);
        }
        this.mFindResults.setWallpaperTarget(w);
        this.mFindResults.setIsWallpaperTargetForLetterbox(w.hasWallpaperForLetterboxBackground());
        if (w == this.mWallpaperTarget && w.isAnimating(3) && com.android.server.wm.WindowManagerDebugConfig.DEBUG_WALLPAPER) {
            android.util.Slog.v(TAG, "Win " + w + ": token animating, looking behind.");
        }
        return (w.mActivityRecord == null && this.mDisplayContent.isKeyguardGoingAway()) ? false : true;
    }

    private boolean isRecentsTransitionTarget(com.android.server.wm.WindowState w) {
        if (w.mTransitionController.isShellTransitionsEnabled()) {
            return this.mWallpaperControllerExt.isRecentsTargetShellEnable(w);
        }
        com.android.server.wm.RecentsAnimationController controller = this.mService.getRecentsAnimationController();
        return controller != null && controller.isWallpaperVisible(w);
    }

    private boolean isBackNavigationTarget(com.android.server.wm.WindowState w) {
        return this.mService.mAtmService.mBackNavigationController.isWallpaperVisible(w);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(com.android.server.wm.WindowState windowState) {
        if (!windowState.mIsWallpaper && java.lang.Float.compare(windowState.mWallpaperZoomOut, this.mLastWallpaperZoomOut) > 0) {
            this.mLastWallpaperZoomOut = windowState.mWallpaperZoomOut;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    WallpaperController(com.android.server.wm.WindowManagerService windowManagerService, com.android.server.wm.DisplayContent displayContent) {
        this.mService = windowManagerService;
        this.mDisplayContent = displayContent;
        android.content.res.Resources resources = windowManagerService.mContext.getResources();
        this.mMinWallpaperScale = resources.getFloat(android.R.dimen.config_rotaryEncoderAxisScrollTickInterval);
        this.mMaxWallpaperScale = resources.getFloat(android.R.dimen.config_resActivitySnapshotScale);
        this.mWallpaperControllerExt.handleWallpaperCreated(displayContent);
        this.mShouldOffsetWallpaperCenter = resources.getBoolean(android.R.bool.config_mainBuiltInDisplayIsRound);
    }

    void resetLargestDisplay(android.view.Display display) {
        if (display != null && display.getType() == 1) {
            this.mLargestDisplaySize = null;
        }
    }

    void setMinWallpaperScale(float minScale) {
        this.mMinWallpaperScale = minScale;
    }

    void setMaxWallpaperScale(float maxScale) {
        this.mMaxWallpaperScale = maxScale;
    }

    void setShouldOffsetWallpaperCenter(boolean shouldOffset) {
        this.mShouldOffsetWallpaperCenter = shouldOffset;
    }

    private android.graphics.Point findLargestDisplaySize() {
        if (!this.mShouldOffsetWallpaperCenter || com.android.window.flags.Flags.multiCrop()) {
            return null;
        }
        android.graphics.Point largestDisplaySize = new android.graphics.Point();
        float largestWidth = 0.0f;
        java.util.List<android.view.DisplayInfo> possibleDisplayInfo = this.mService.getPossibleDisplayInfoLocked(0);
        for (int i = 0; i < possibleDisplayInfo.size(); i++) {
            android.view.DisplayInfo displayInfo = possibleDisplayInfo.get(i);
            float width = displayInfo.logicalWidth / displayInfo.physicalXDpi;
            if (displayInfo.type == 1 && width > largestWidth) {
                largestWidth = width;
                largestDisplaySize.set(displayInfo.logicalWidth, displayInfo.logicalHeight);
            }
        }
        return largestDisplaySize;
    }

    void setWallpaperCropUtils(com.android.server.wallpaper.WallpaperCropper.WallpaperCropUtils wallpaperCropUtils) {
        this.mWallpaperCropUtils = wallpaperCropUtils;
    }

    com.android.server.wm.WindowState getWallpaperTarget() {
        return this.mWallpaperTarget;
    }

    com.android.server.wm.WindowState getPrevWallpaperTarget() {
        return this.mPrevWallpaperTarget;
    }

    boolean isWallpaperTarget(com.android.server.wm.WindowState win) {
        return win == this.mWallpaperTarget;
    }

    boolean isBelowWallpaperTarget(com.android.server.wm.WindowState win) {
        return this.mWallpaperTarget != null && this.mWallpaperTarget.mLayer >= win.mBaseLayer;
    }

    boolean isWallpaperVisible() {
        for (int i = this.mWallpaperTokens.size() - 1; i >= 0; i--) {
            if (this.mWallpaperTokens.get(i).isVisible()) {
                return true;
            }
        }
        return false;
    }

    boolean isWallpaperTargetAnimating() {
        return this.mWallpaperTarget != null && this.mWallpaperTarget.isAnimating(3) && (this.mWallpaperTarget.mActivityRecord == null || !this.mWallpaperTarget.mActivityRecord.isWaitingForTransitionStart());
    }

    void hideDeferredWallpapersIfNeededLegacy() {
        for (int i = this.mWallpaperTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.WallpaperWindowToken token = this.mWallpaperTokens.get(i);
            if (!token.isVisibleRequested()) {
                token.commitVisibility(false);
            }
        }
    }

    void hideWallpapers(com.android.server.wm.WindowState winGoingAway) {
        if ((this.mWallpaperTarget != null && (this.mWallpaperTarget != winGoingAway || this.mPrevWallpaperTarget != null)) || this.mWallpaperControllerExt.skipHideSecondaryWallpaper(this.mService, this.mDisplayContent) || this.mFindResults.useTopWallpaperAsTarget) {
            return;
        }
        if (this.mWallpaperControllerExt.skipHideWallpaper(this.mDisplayContent)) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                android.util.Slog.d(TAG, "Home is visible, skipHideWallpaper");
                return;
            }
            return;
        }
        for (int i = this.mWallpaperTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.WallpaperWindowToken token = this.mWallpaperTokens.get(i);
            token.setVisibility(false);
            if (token.isVisible() && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(token);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(winGoingAway);
                java.lang.String protoLogParam2 = java.lang.String.valueOf(this.mWallpaperTarget);
                java.lang.String protoLogParam3 = java.lang.String.valueOf(this.mPrevWallpaperTarget);
                java.lang.String protoLogParam4 = java.lang.String.valueOf(android.os.Debug.getCallers(5));
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, -5254364639040552989L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2, protoLogParam3, protoLogParam4);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x0261  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x026c  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0273  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x027f  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x03ce  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01c6  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01e5  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01ea  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0224  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x022e  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0240  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x024c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean updateWallpaperOffset(com.android.server.wm.WindowState r45, boolean r46) {
        /*
            Method dump skipped, instruction units count: 989
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WallpaperController.updateWallpaperOffset(com.android.server.wm.WindowState, boolean):boolean");
    }

    private int getDisplayWidthOffset(int availWidth, android.graphics.Rect displayFrame, boolean isRtl) {
        int pageWidth;
        if (!this.mShouldOffsetWallpaperCenter || com.android.window.flags.Flags.multiCrop()) {
            return 0;
        }
        if (this.mLargestDisplaySize == null) {
            this.mLargestDisplaySize = findLargestDisplaySize();
        }
        if (this.mLargestDisplaySize == null || this.mLargestDisplaySize.x == (pageWidth = displayFrame.width()) || displayFrame.width() >= displayFrame.height()) {
            return 0;
        }
        float sizeRatio = displayFrame.height() / this.mLargestDisplaySize.y;
        int adjustedLargestWidth = java.lang.Math.round(this.mLargestDisplaySize.x * sizeRatio);
        if (isRtl) {
            return adjustedLargestWidth - ((adjustedLargestWidth + pageWidth) / 2);
        }
        return java.lang.Math.min(adjustedLargestWidth - pageWidth, availWidth) / 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowWallpaperPosition(com.android.server.wm.WindowState window, float x, float y, float xStep, float yStep) {
        if (window.mWallpaperX != x || window.mWallpaperY != y) {
            window.mWallpaperX = x;
            window.mWallpaperY = y;
            window.mWallpaperXStep = xStep;
            window.mWallpaperYStep = yStep;
            updateWallpaperOffsetLocked(window, !this.mService.mFlags.mWallpaperOffsetAsync);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWallpaperZoomOut(com.android.server.wm.WindowState window, float zoom) {
        if (java.lang.Float.compare(window.mWallpaperZoomOut, zoom) != 0) {
            window.mWallpaperZoomOut = zoom;
            computeLastWallpaperZoomOut();
            for (int i = this.mWallpaperTokens.size() - 1; i >= 0; i--) {
                com.android.server.wm.WallpaperWindowToken token = this.mWallpaperTokens.get(i);
                token.updateWallpaperOffset(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShouldZoomOutWallpaper(com.android.server.wm.WindowState window, boolean shouldZoom) {
        if (shouldZoom != window.mShouldScaleWallpaper) {
            window.mShouldScaleWallpaper = shouldZoom;
            updateWallpaperOffsetLocked(window, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowWallpaperDisplayOffset(com.android.server.wm.WindowState window, int x, int y) {
        if (window.mWallpaperDisplayOffsetX != x || window.mWallpaperDisplayOffsetY != y) {
            window.mWallpaperDisplayOffsetX = x;
            window.mWallpaperDisplayOffsetY = y;
            updateWallpaperOffsetLocked(window, !this.mService.mFlags.mWallpaperOffsetAsync);
        }
    }

    void sendWindowWallpaperCommandUnchecked(com.android.server.wm.WindowState window, java.lang.String action, int x, int y, int z, android.os.Bundle extras, boolean sync) {
        if (this.mWallpaperControllerExt.sendWindowWallpaperCommand(window, action, extras, sync)) {
            return;
        }
        sendWindowWallpaperCommand(action, x, y, z, extras, sync);
    }

    private void sendWindowWallpaperCommand(java.lang.String action, int x, int y, int z, android.os.Bundle extras, boolean sync) {
        for (int curTokenNdx = this.mWallpaperTokens.size() - 1; curTokenNdx >= 0; curTokenNdx--) {
            com.android.server.wm.WallpaperWindowToken token = this.mWallpaperTokens.get(curTokenNdx);
            token.sendWindowWallpaperCommand(action, x, y, z, extras, sync);
        }
    }

    private void updateWallpaperOffsetLocked(com.android.server.wm.WindowState changingTarget, boolean sync) {
        com.android.server.wm.WindowState target = this.mWallpaperTarget;
        if (target == null && changingTarget.mToken.isVisible() && changingTarget.mTransitionController.inTransition()) {
            target = changingTarget;
        }
        com.android.server.wm.WallpaperWindowToken token = getTokenForTarget(target);
        if (token == null) {
            return;
        }
        if (target.mWallpaperX >= 0.0f) {
            token.mWallpaperX = target.mWallpaperX;
        } else if (changingTarget.mWallpaperX >= 0.0f) {
            token.mWallpaperX = changingTarget.mWallpaperX;
        }
        if (target.mWallpaperY >= 0.0f) {
            token.mWallpaperY = target.mWallpaperY;
        } else if (changingTarget.mWallpaperY >= 0.0f) {
            token.mWallpaperY = changingTarget.mWallpaperY;
        }
        if (target.mWallpaperDisplayOffsetX != Integer.MIN_VALUE) {
            token.mWallpaperDisplayOffsetX = target.mWallpaperDisplayOffsetX;
        } else if (changingTarget.mWallpaperDisplayOffsetX != Integer.MIN_VALUE) {
            token.mWallpaperDisplayOffsetX = changingTarget.mWallpaperDisplayOffsetX;
        }
        if (target.mWallpaperDisplayOffsetY != Integer.MIN_VALUE) {
            token.mWallpaperDisplayOffsetY = target.mWallpaperDisplayOffsetY;
        } else if (changingTarget.mWallpaperDisplayOffsetY != Integer.MIN_VALUE) {
            token.mWallpaperDisplayOffsetY = changingTarget.mWallpaperDisplayOffsetY;
        }
        if (target.mWallpaperXStep >= 0.0f) {
            token.mWallpaperXStep = target.mWallpaperXStep;
        } else if (changingTarget.mWallpaperXStep >= 0.0f) {
            token.mWallpaperXStep = changingTarget.mWallpaperXStep;
        }
        if (target.mWallpaperYStep >= 0.0f) {
            token.mWallpaperYStep = target.mWallpaperYStep;
        } else if (changingTarget.mWallpaperYStep >= 0.0f) {
            token.mWallpaperYStep = changingTarget.mWallpaperYStep;
        }
        token.updateWallpaperOffset(sync);
    }

    private com.android.server.wm.WallpaperWindowToken getTokenForTarget(com.android.server.wm.WindowState target) {
        if (target == null) {
            return null;
        }
        com.android.server.wm.WindowState window = this.mFindResults.getTopWallpaper(target.canShowWhenLocked() && this.mService.isKeyguardLocked());
        if (window == null) {
            return null;
        }
        return window.mToken.asWallpaperToken();
    }

    void clearLastWallpaperTimeoutTime() {
        this.mLastWallpaperTimeoutTime = 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void wallpaperCommandComplete(android.os.IBinder window) {
        if (this.mWaitingOnWallpaper != null && this.mWaitingOnWallpaper.mClient.asBinder() == window) {
            this.mWaitingOnWallpaper = null;
            this.mService.mGlobalLock.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void wallpaperOffsetsComplete(android.os.IBinder window) {
        if (this.mWaitingOnWallpaper != null && this.mWaitingOnWallpaper.mClient.asBinder() == window) {
            this.mWaitingOnWallpaper = null;
            this.mService.mGlobalLock.notifyAll();
        }
    }

    private void findWallpaperTarget() {
        this.mFindResults.reset();
        if (this.mService.mAtmService.mSupportsFreeformWindowManagement && this.mDisplayContent.getDefaultTaskDisplayArea().isRootTaskVisible(5)) {
            this.mFindResults.setUseTopWallpaperAsTarget(true);
        }
        findWallpapers();
        this.mDisplayContent.forAllWindows(this.mFindWallpaperTargetFunction, true);
        if (this.mFindResults.mNeedsShowWhenLockedWallpaper) {
            this.mFindResults.setUseTopWallpaperAsTarget(true);
        }
        if (this.mFindResults.wallpaperTarget == null && this.mFindResults.useTopWallpaperAsTarget) {
            this.mFindResults.setWallpaperTarget(this.mFindResults.getTopWallpaper(this.mDisplayContent.isKeyguardLocked()));
        }
    }

    private void findWallpapers() {
        for (int i = this.mWallpaperTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.WallpaperWindowToken token = this.mWallpaperTokens.get(i);
            boolean canShowWhenLocked = token.canShowWhenLocked();
            for (int j = token.getChildCount() - 1; j >= 0; j--) {
                com.android.server.wm.WindowState w = (com.android.server.wm.WindowState) token.getChildAt(j);
                if (w.mIsWallpaper) {
                    if (canShowWhenLocked && !this.mFindResults.hasTopShowWhenLockedWallpaper()) {
                        this.mFindResults.setTopShowWhenLockedWallpaper(w);
                    } else if (!canShowWhenLocked && !this.mFindResults.hasTopHideWhenLockedWallpaper()) {
                        this.mFindResults.setTopHideWhenLockedWallpaper(w);
                    }
                }
            }
        }
    }

    void collectTopWallpapers(com.android.server.wm.Transition transition) {
        if (this.mFindResults.hasTopShowWhenLockedWallpaper()) {
            transition.collect(this.mFindResults.mTopWallpaper.mTopShowWhenLockedWallpaper.mToken);
        }
        if (this.mFindResults.hasTopHideWhenLockedWallpaper()) {
            transition.collect(this.mFindResults.mTopWallpaper.mTopHideWhenLockedWallpaper.mToken);
        }
    }

    private boolean isFullscreen(android.view.WindowManager.LayoutParams attrs) {
        return attrs.x == 0 && attrs.y == 0 && attrs.width == -1 && attrs.height == -1;
    }

    private void updateWallpaperWindowsTarget(com.android.server.wm.WallpaperController.FindWallpaperTargetResult result) {
        com.android.server.wm.WindowState wallpaperTarget = result.wallpaperTarget;
        if (this.mWallpaperTarget == wallpaperTarget || (this.mPrevWallpaperTarget != null && this.mPrevWallpaperTarget == wallpaperTarget)) {
            if (this.mPrevWallpaperTarget != null && !this.mPrevWallpaperTarget.isAnimatingLw()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[1]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, -3477087868568520027L, 0, null, null);
                }
                this.mPrevWallpaperTarget = null;
                this.mWallpaperTarget = wallpaperTarget;
                return;
            }
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(wallpaperTarget);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mWallpaperTarget);
            java.lang.String protoLogParam2 = java.lang.String.valueOf(android.os.Debug.getCallers(5));
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, -3751289048117070874L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2);
        }
        this.mPrevWallpaperTarget = null;
        final com.android.server.wm.WindowState prevWallpaperTarget = this.mWallpaperTarget;
        this.mWallpaperTarget = wallpaperTarget;
        boolean oldTargetHidden = false;
        if (prevWallpaperTarget == null && wallpaperTarget != null) {
            updateWallpaperOffsetLocked(this.mWallpaperTarget, false);
        }
        if (wallpaperTarget == null || prevWallpaperTarget == null) {
            return;
        }
        boolean oldAnim = prevWallpaperTarget.isAnimatingLw();
        boolean foundAnim = wallpaperTarget.isAnimatingLw();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[1]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(foundAnim);
            java.lang.String protoLogParam12 = java.lang.String.valueOf(oldAnim);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, 5625223922466895079L, 0, null, protoLogParam02, protoLogParam12);
        }
        if (!foundAnim || !oldAnim || this.mDisplayContent.getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.WallpaperController$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.WallpaperController.lambda$updateWallpaperWindowsTarget$2(prevWallpaperTarget, (com.android.server.wm.WindowState) obj);
            }
        }) == null) {
            return;
        }
        boolean newTargetHidden = (wallpaperTarget.mActivityRecord == null || wallpaperTarget.mActivityRecord.isVisibleRequested()) ? false : true;
        if (prevWallpaperTarget.mActivityRecord != null && !prevWallpaperTarget.mActivityRecord.isVisibleRequested()) {
            oldTargetHidden = true;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[1]) {
            java.lang.String protoLogParam03 = java.lang.String.valueOf(prevWallpaperTarget);
            boolean protoLogParam13 = oldTargetHidden;
            java.lang.String protoLogParam22 = java.lang.String.valueOf(wallpaperTarget);
            boolean protoLogParam3 = newTargetHidden;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, 7634524672408826188L, 204, null, protoLogParam03, java.lang.Boolean.valueOf(protoLogParam13), protoLogParam22, java.lang.Boolean.valueOf(protoLogParam3));
        }
        this.mPrevWallpaperTarget = prevWallpaperTarget;
        if (newTargetHidden && !oldTargetHidden) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, -4345077332231178044L, 0, null, null);
            }
            this.mWallpaperTarget = prevWallpaperTarget;
        } else if (newTargetHidden == oldTargetHidden && !this.mDisplayContent.mOpeningApps.contains(wallpaperTarget.mActivityRecord) && (this.mDisplayContent.mOpeningApps.contains(prevWallpaperTarget.mActivityRecord) || this.mDisplayContent.mClosingApps.contains(prevWallpaperTarget.mActivityRecord))) {
            this.mWallpaperTarget = prevWallpaperTarget;
        }
        result.setWallpaperTarget(wallpaperTarget);
    }

    static /* synthetic */ boolean lambda$updateWallpaperWindowsTarget$2(com.android.server.wm.WindowState prevWallpaperTarget, com.android.server.wm.WindowState w) {
        return w == prevWallpaperTarget;
    }

    private void updateWallpaperTokens(boolean visibility, boolean keyguardLocked) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[1]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, 257349083882992098L, 15, null, java.lang.Boolean.valueOf(visibility), java.lang.Boolean.valueOf(keyguardLocked));
        }
        com.android.server.wm.WindowState topWallpaper = this.mFindResults.getTopWallpaper(keyguardLocked);
        com.android.server.wm.WallpaperWindowToken topWallpaperToken = topWallpaper == null ? null : topWallpaper.mToken.asWallpaperToken();
        for (int curTokenNdx = this.mWallpaperTokens.size() - 1; curTokenNdx >= 0; curTokenNdx--) {
            com.android.server.wm.WallpaperWindowToken token = this.mWallpaperTokens.get(curTokenNdx);
            if (!token.hasChild()) {
                android.util.Slog.d(TAG, "token have not add any child");
            } else {
                token.updateWallpaperWindows(visibility && token == topWallpaperToken);
            }
        }
    }

    void adjustWallpaperWindows() {
        this.mDisplayContent.mWallpaperMayChange = false;
        findWallpaperTarget();
        updateWallpaperWindowsTarget(this.mFindResults);
        com.android.server.wm.WallpaperWindowToken token = getTokenForTarget(this.mWallpaperTarget);
        boolean visible = token != null;
        if (visible) {
            if (this.mWallpaperTarget.mWallpaperX >= 0.0f) {
                token.mWallpaperX = this.mWallpaperTarget.mWallpaperX;
                token.mWallpaperXStep = this.mWallpaperTarget.mWallpaperXStep;
            }
            if (this.mWallpaperTarget.mWallpaperY >= 0.0f) {
                token.mWallpaperY = this.mWallpaperTarget.mWallpaperY;
                token.mWallpaperYStep = this.mWallpaperTarget.mWallpaperYStep;
            }
            if (this.mWallpaperTarget.mWallpaperDisplayOffsetX != Integer.MIN_VALUE) {
                token.mWallpaperDisplayOffsetX = this.mWallpaperTarget.mWallpaperDisplayOffsetX;
            }
            if (this.mWallpaperTarget.mWallpaperDisplayOffsetY != Integer.MIN_VALUE) {
                token.mWallpaperDisplayOffsetY = this.mWallpaperTarget.mWallpaperDisplayOffsetY;
            }
        }
        this.mWallpaperControllerExt.dispatchWallpaperWindowsTarget(this.mWallpaperTarget, this.mDisplayContent, visible);
        updateWallpaperTokens(visible, this.mDisplayContent.isKeyguardLocked());
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[1]) {
            long protoLogParam0 = this.mDisplayContent.getDisplayId();
            boolean protoLogParam1 = visible;
            boolean protoLogParam2 = this.mDisplayContent.isKeyguardLocked();
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, 7408402065665963407L, 61, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2));
        }
        if (visible && this.mLastFrozen != this.mFindResults.isWallpaperTargetForLetterbox) {
            this.mLastFrozen = this.mFindResults.isWallpaperTargetForLetterbox;
            sendWindowWallpaperCommand(this.mFindResults.isWallpaperTargetForLetterbox ? "android.wallpaper.freeze" : "android.wallpaper.unfreeze", 0, 0, 0, null, false);
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[0]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(this.mWallpaperTarget);
            java.lang.String protoLogParam12 = java.lang.String.valueOf(this.mPrevWallpaperTarget);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, -8598497865499265448L, 0, null, protoLogParam02, protoLogParam12);
        }
    }

    boolean processWallpaperDrawPendingTimeout() {
        if (this.mWallpaperDrawState == 1) {
            this.mWallpaperDrawState = 2;
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WALLPAPER) {
                android.util.Slog.v(TAG, "*** WALLPAPER DRAW TIMEOUT");
            }
            if (this.mService.getRecentsAnimationController() != null) {
                this.mService.getRecentsAnimationController().startAnimation();
            }
            this.mService.mAtmService.mBackNavigationController.startAnimation();
            return true;
        }
        return false;
    }

    boolean wallpaperTransitionReady() {
        boolean transitionReady = true;
        boolean wallpaperReady = true;
        int curTokenIndex = this.mWallpaperTokens.size() - 1;
        while (true) {
            if (curTokenIndex < 0 || 1 == 0) {
                break;
            }
            com.android.server.wm.WallpaperWindowToken token = this.mWallpaperTokens.get(curTokenIndex);
            if (!token.hasVisibleNotDrawnWallpaper()) {
                curTokenIndex--;
            } else {
                wallpaperReady = false;
                if (this.mWallpaperDrawState != 2) {
                    transitionReady = false;
                }
                if (this.mWallpaperDrawState == 0) {
                    this.mWallpaperDrawState = 1;
                    this.mService.mH.removeMessages(39, this);
                    this.mService.mH.sendMessageDelayed(this.mService.mH.obtainMessage(39, this), 500L);
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[1]) {
                    long protoLogParam0 = this.mWallpaperDrawState;
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, -5402010429724738603L, 1, null, java.lang.Long.valueOf(protoLogParam0));
                }
            }
        }
        if (wallpaperReady) {
            this.mWallpaperDrawState = 0;
            this.mService.mH.removeMessages(39, this);
        }
        return transitionReady;
    }

    void adjustWallpaperWindowsForAppTransitionIfNeeded(android.util.ArraySet<com.android.server.wm.ActivityRecord> openingApps) {
        boolean adjust = false;
        if ((this.mDisplayContent.pendingLayoutChanges & 4) != 0) {
            adjust = true;
        } else {
            int i = openingApps.size() - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                com.android.server.wm.ActivityRecord activity = openingApps.valueAt(i);
                if (!activity.windowsCanBeWallpaperTarget()) {
                    i--;
                } else {
                    adjust = true;
                    break;
                }
            }
        }
        if (adjust) {
            adjustWallpaperWindows();
        }
    }

    void addWallpaperToken(com.android.server.wm.WallpaperWindowToken token) {
        this.mWallpaperTokens.add(token);
    }

    void removeWallpaperToken(com.android.server.wm.WallpaperWindowToken token) {
        this.mWallpaperTokens.remove(token);
        this.mWallpaperControllerExt.removeWallpaperWindows();
    }

    void onWallpaperTokenReordered() {
        if (this.mWallpaperTokens.size() > 1) {
            this.mWallpaperTokens.sort(null);
        }
    }

    boolean canScreenshotWallpaper() {
        return canScreenshotWallpaper(getTopVisibleWallpaper());
    }

    private boolean canScreenshotWallpaper(com.android.server.wm.WindowState wallpaperWindowState) {
        if (!this.mService.mPolicy.isScreenOn()) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_SCREENSHOT) {
                android.util.Slog.i(TAG, "Attempted to take screenshot while display was off.");
            }
            return false;
        }
        if (wallpaperWindowState == null) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_SCREENSHOT) {
                android.util.Slog.i(TAG, "No visible wallpaper to screenshot");
            }
            return false;
        }
        return true;
    }

    android.graphics.Bitmap screenshotWallpaperLocked() {
        com.android.server.wm.WindowState wallpaperWindowState = getTopVisibleWallpaper();
        if (!canScreenshotWallpaper(wallpaperWindowState)) {
            return null;
        }
        android.graphics.Rect bounds = wallpaperWindowState.getBounds();
        bounds.offsetTo(0, 0);
        android.window.ScreenCapture.ScreenshotHardwareBuffer wallpaperBuffer = android.window.ScreenCapture.captureLayers(wallpaperWindowState.getSurfaceControl(), bounds, 1.0f);
        if (wallpaperBuffer == null) {
            android.util.Slog.w(TAG, "Failed to screenshot wallpaper");
            return null;
        }
        return android.graphics.Bitmap.wrapHardwareBuffer(wallpaperBuffer.getHardwareBuffer(), wallpaperBuffer.getColorSpace());
    }

    android.view.SurfaceControl mirrorWallpaperSurface() {
        com.android.server.wm.WindowState wallpaperWindowState = getTopVisibleWallpaper();
        if (wallpaperWindowState != null) {
            return android.view.SurfaceControl.mirrorSurface(wallpaperWindowState.getSurfaceControl());
        }
        return null;
    }

    com.android.server.wm.WindowState getTopVisibleWallpaper() {
        for (int curTokenNdx = this.mWallpaperTokens.size() - 1; curTokenNdx >= 0; curTokenNdx--) {
            com.android.server.wm.WallpaperWindowToken token = this.mWallpaperTokens.get(curTokenNdx);
            for (int i = token.getChildCount() - 1; i >= 0; i--) {
                com.android.server.wm.WindowState w = (com.android.server.wm.WindowState) token.getChildAt(i);
                if (w.mWinAnimator.getShown() && w.mWinAnimator.mLastAlpha > 0.0f) {
                    return w;
                }
            }
        }
        return null;
    }

    void onDisplaySwitchStarted() {
        this.mIsWallpaperNotifiedOnDisplaySwitch = notifyDisplaySwitch(true);
    }

    void onDisplaySwitchFinished() {
        if (this.mIsWallpaperNotifiedOnDisplaySwitch) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mIsWallpaperNotifiedOnDisplaySwitch = false;
                    notifyDisplaySwitch(false);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    private boolean notifyDisplaySwitch(boolean start) {
        boolean notified = false;
        for (int curTokenNdx = this.mWallpaperTokens.size() - 1; curTokenNdx >= 0; curTokenNdx--) {
            com.android.server.wm.WallpaperWindowToken token = this.mWallpaperTokens.get(curTokenNdx);
            for (int i = token.getChildCount() - 1; i >= 0; i--) {
                com.android.server.wm.WindowState w = (com.android.server.wm.WindowState) token.getChildAt(i);
                if (!start || w.mWinAnimator.getShown()) {
                    try {
                        w.mClient.dispatchWallpaperCommand("android.wallpaper.displayswitch", 0, 0, start ? 1 : 0, (android.os.Bundle) null, false);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(TAG, "Failed to dispatch COMMAND_DISPLAY_SWITCH " + e);
                    }
                    notified = true;
                }
            }
        }
        return notified;
    }

    private void computeLastWallpaperZoomOut() {
        this.mLastWallpaperZoomOut = 0.0f;
        this.mDisplayContent.forAllWindows(this.mComputeMaxZoomOutFunction, true);
    }

    private float zoomOutToScale(float zoomOut) {
        return android.util.MathUtils.lerp(this.mMinWallpaperScale, this.mMaxWallpaperScale, 1.0f - zoomOut);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("displayId=");
        pw.println(this.mDisplayContent.getDisplayId());
        pw.print(prefix);
        pw.print("mWallpaperTarget=");
        pw.println(this.mWallpaperTarget);
        pw.print(prefix);
        pw.print("mLastWallpaperZoomOut=");
        pw.println(this.mLastWallpaperZoomOut);
        if (this.mPrevWallpaperTarget != null) {
            pw.print(prefix);
            pw.print("mPrevWallpaperTarget=");
            pw.println(this.mPrevWallpaperTarget);
        }
        for (int i = this.mWallpaperTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.WallpaperWindowToken t = this.mWallpaperTokens.get(i);
            pw.print(prefix);
            pw.println("token " + t + ":");
            pw.print(prefix);
            pw.print("  canShowWhenLocked=");
            pw.println(t.canShowWhenLocked());
            dumpValue(pw, prefix, "mWallpaperX", t.mWallpaperX);
            dumpValue(pw, prefix, "mWallpaperY", t.mWallpaperY);
            dumpValue(pw, prefix, "mWallpaperXStep", t.mWallpaperXStep);
            dumpValue(pw, prefix, "mWallpaperYStep", t.mWallpaperYStep);
            dumpValue(pw, prefix, "mWallpaperDisplayOffsetX", t.mWallpaperDisplayOffsetX);
            dumpValue(pw, prefix, "mWallpaperDisplayOffsetY", t.mWallpaperDisplayOffsetY);
        }
    }

    private void dumpValue(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String valueName, float value) {
        pw.print(prefix);
        pw.print("  " + valueName + "=");
        pw.println(value >= 0.0f ? java.lang.Float.valueOf(value) : "NA");
    }

    private static final class FindWallpaperTargetResult {
        boolean isWallpaperTargetForLetterbox;
        boolean mNeedsShowWhenLockedWallpaper;
        com.android.server.wm.WallpaperController.FindWallpaperTargetResult.TopWallpaper mTopWallpaper;
        boolean useTopWallpaperAsTarget;
        com.android.server.wm.WindowState wallpaperTarget;

        private FindWallpaperTargetResult() {
            this.mTopWallpaper = new com.android.server.wm.WallpaperController.FindWallpaperTargetResult.TopWallpaper();
            this.useTopWallpaperAsTarget = false;
            this.wallpaperTarget = null;
            this.isWallpaperTargetForLetterbox = false;
        }

        static final class TopWallpaper {
            com.android.server.wm.WindowState mTopHideWhenLockedWallpaper = null;
            com.android.server.wm.WindowState mTopShowWhenLockedWallpaper = null;

            TopWallpaper() {
            }

            void reset() {
                this.mTopHideWhenLockedWallpaper = null;
                this.mTopShowWhenLockedWallpaper = null;
            }
        }

        void setTopHideWhenLockedWallpaper(com.android.server.wm.WindowState win) {
            if (this.mTopWallpaper.mTopHideWhenLockedWallpaper != win && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(win);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mTopWallpaper.mTopHideWhenLockedWallpaper);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, 4151327328872447804L, 0, null, protoLogParam0, protoLogParam1);
            }
            this.mTopWallpaper.mTopHideWhenLockedWallpaper = win;
        }

        void setTopShowWhenLockedWallpaper(com.android.server.wm.WindowState win) {
            if (this.mTopWallpaper.mTopShowWhenLockedWallpaper != win && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(win);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mTopWallpaper.mTopShowWhenLockedWallpaper);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, 6943105284590482059L, 0, null, protoLogParam0, protoLogParam1);
            }
            this.mTopWallpaper.mTopShowWhenLockedWallpaper = win;
        }

        boolean hasTopHideWhenLockedWallpaper() {
            return this.mTopWallpaper.mTopHideWhenLockedWallpaper != null;
        }

        boolean hasTopShowWhenLockedWallpaper() {
            return this.mTopWallpaper.mTopShowWhenLockedWallpaper != null;
        }

        com.android.server.wm.WindowState getTopWallpaper(boolean isKeyguardLocked) {
            if (!isKeyguardLocked && hasTopHideWhenLockedWallpaper()) {
                return this.mTopWallpaper.mTopHideWhenLockedWallpaper;
            }
            return this.mTopWallpaper.mTopShowWhenLockedWallpaper;
        }

        void setWallpaperTarget(com.android.server.wm.WindowState win) {
            this.wallpaperTarget = win;
        }

        void setUseTopWallpaperAsTarget(boolean topWallpaperAsTarget) {
            this.useTopWallpaperAsTarget = topWallpaperAsTarget;
        }

        void setIsWallpaperTargetForLetterbox(boolean isWallpaperTargetForLetterbox) {
            this.isWallpaperTargetForLetterbox = isWallpaperTargetForLetterbox;
        }

        void reset() {
            this.mTopWallpaper.reset();
            this.mNeedsShowWhenLockedWallpaper = false;
            this.wallpaperTarget = null;
            this.useTopWallpaperAsTarget = false;
            this.isWallpaperTargetForLetterbox = false;
        }
    }

    public com.android.server.wm.IWallpaperControllerWrapper getWrapper() {
        return this.mWallpaperControllerWrapper;
    }

    private class WallpaperControllerWrapperImpl implements com.android.server.wm.IWallpaperControllerWrapper {
        private WallpaperControllerWrapperImpl() {
        }

        @Override // com.android.server.wm.IWallpaperControllerWrapper
        public java.util.List<com.android.server.wm.WindowState> getAllTopWallpapers() {
            java.util.ArrayList<com.android.server.wm.WindowState> wallpapers = new java.util.ArrayList<>(2);
            if (com.android.server.wm.WallpaperController.this.mFindResults.hasTopShowWhenLockedWallpaper()) {
                wallpapers.add(com.android.server.wm.WallpaperController.this.mFindResults.mTopWallpaper.mTopShowWhenLockedWallpaper);
            }
            if (com.android.server.wm.WallpaperController.this.mFindResults.hasTopHideWhenLockedWallpaper()) {
                wallpapers.add(com.android.server.wm.WallpaperController.this.mFindResults.mTopWallpaper.mTopHideWhenLockedWallpaper);
            }
            return wallpapers;
        }
    }
}
