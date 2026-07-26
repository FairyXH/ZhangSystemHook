package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ScreenRotationAnimation {
    private static final int CAPTIRE_LAYERS_FOR_SCREEN_ROTATION = -222;
    private static final java.lang.String TAG = "WindowManager";
    private boolean mAnimRunning;
    private android.view.SurfaceControl mBackColorSurface;
    private final android.content.Context mContext;
    private int mCurRotation;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private float mEndLuma;
    private android.view.SurfaceControl mEnterBlackFrameLayer;
    private com.android.server.wm.BlackFrame mEnteringBlackFrame;
    private boolean mFinishAnimReady;
    private long mFinishAnimStartTime;
    private final int mOriginalHeight;
    private final int mOriginalRotation;
    private final int mOriginalWidth;
    private android.view.animation.Animation mRotateAlphaAnimation;
    private android.view.animation.Animation mRotateEnterAnimation;
    private android.view.animation.Animation mRotateExitAnimation;
    private android.view.SurfaceControl[] mRoundedCornerOverlay;
    private android.view.SurfaceControl mScreenshotLayer;
    private final com.android.server.wm.WindowManagerService mService;
    private float mStartLuma;
    private boolean mStarted;
    private com.android.server.wm.ScreenRotationAnimation.SurfaceRotationAnimationController mSurfaceRotationAnimationController;
    private final float[] mTmpFloats = new float[9];
    private final android.view.animation.Transformation mRotateExitTransformation = new android.view.animation.Transformation();
    private final android.view.animation.Transformation mRotateEnterTransformation = new android.view.animation.Transformation();
    private final android.graphics.Matrix mSnapshotInitialMatrix = new android.graphics.Matrix();
    private com.android.server.wm.ScreenRotationAnimation.ScreenRotationAnimationWrapper mSRAWrapper = new com.android.server.wm.ScreenRotationAnimation.ScreenRotationAnimationWrapper();
    private com.android.server.wm.IScreenRotationAnimationExt mSRAExt = (com.android.server.wm.IScreenRotationAnimationExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IScreenRotationAnimationExt.class).base(this).create();
    private com.android.server.wm.IScreenRotationAnimationSocExt mAnimationSocExt = (com.android.server.wm.IScreenRotationAnimationSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IScreenRotationAnimationSocExt.class).base(this).create();

    /* JADX WARN: Removed duplicated region for block: B:57:0x0193  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x02fb  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x02fd  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x034a A[Catch: OutOfResourcesException -> 0x0361, TryCatch #6 {OutOfResourcesException -> 0x0361, blocks: (B:92:0x0317, B:94:0x034a, B:96:0x0350, B:98:0x0358), top: B:141:0x0317 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    ScreenRotationAnimation(com.android.server.wm.DisplayContent r34, int r35) {
        /*
            Method dump skipped, instruction units count: 1031
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.ScreenRotationAnimation.<init>(com.android.server.wm.DisplayContent, int):void");
    }

    void setSkipScreenshotForRoundedCornerOverlays(final boolean skipScreenshot, final android.view.SurfaceControl.Transaction t) {
        this.mDisplayContent.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.ScreenRotationAnimation$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.ScreenRotationAnimation.lambda$setSkipScreenshotForRoundedCornerOverlays$0(t, skipScreenshot, (com.android.server.wm.WindowState) obj);
            }
        }, false);
        if (!skipScreenshot) {
            t.apply(true);
        }
    }

    static /* synthetic */ void lambda$setSkipScreenshotForRoundedCornerOverlays$0(android.view.SurfaceControl.Transaction t, boolean skipScreenshot, com.android.server.wm.WindowState w) {
        if (!w.mToken.mRoundedCornerOverlay || !w.isVisible() || !w.mWinAnimator.hasSurface()) {
            return;
        }
        t.setSkipScreenshot(w.mWinAnimator.mSurfaceController.mSurfaceControl, skipScreenshot);
    }

    public void updateAnimationForFolding(android.view.SurfaceControl.Transaction t, boolean isNeedShowAndApply) {
        this.mSRAWrapper.getExtImpl().updateAnimationForFolding(t, this.mBackColorSurface, this.mScreenshotLayer, this.mDisplayContent, isNeedShowAndApply);
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1133871366145L, this.mStarted);
        proto.write(1133871366146L, this.mAnimRunning);
        proto.end(token);
    }

    boolean hasScreenshot() {
        return this.mScreenshotLayer != null;
    }

    private void setRotationTransform(android.view.SurfaceControl.Transaction t, android.graphics.Matrix matrix) {
        if (this.mScreenshotLayer == null) {
            return;
        }
        matrix.getValues(this.mTmpFloats);
        float x = this.mTmpFloats[2];
        float y = this.mTmpFloats[5];
        t.setPosition(this.mScreenshotLayer, x, y);
        t.setMatrix(this.mScreenshotLayer, this.mTmpFloats[0], this.mTmpFloats[3], this.mTmpFloats[1], this.mTmpFloats[4]);
        t.setAlpha(this.mScreenshotLayer, 1.0f);
        if (this.mSRAWrapper.getExtImpl().getDeviceFolding()) {
            t.setAlpha(this.mScreenshotLayer, 0.0f);
        }
        t.show(this.mScreenshotLayer);
    }

    public void printTo(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("mSurface=");
        pw.print(this.mScreenshotLayer);
        pw.print(prefix);
        pw.print("mEnteringBlackFrame=");
        pw.println(this.mEnteringBlackFrame);
        if (this.mEnteringBlackFrame != null) {
            this.mEnteringBlackFrame.printTo(prefix + "  ", pw);
        }
        pw.print(prefix);
        pw.print("mCurRotation=");
        pw.print(this.mCurRotation);
        pw.print(" mOriginalRotation=");
        pw.println(this.mOriginalRotation);
        pw.print(prefix);
        pw.print("mOriginalWidth=");
        pw.print(this.mOriginalWidth);
        pw.print(" mOriginalHeight=");
        pw.println(this.mOriginalHeight);
        pw.print(prefix);
        pw.print("mStarted=");
        pw.print(this.mStarted);
        pw.print(" mAnimRunning=");
        pw.print(this.mAnimRunning);
        pw.print(" mFinishAnimReady=");
        pw.print(this.mFinishAnimReady);
        pw.print(" mFinishAnimStartTime=");
        pw.println(this.mFinishAnimStartTime);
        pw.print(prefix);
        pw.print("mRotateExitAnimation=");
        pw.print(this.mRotateExitAnimation);
        pw.print(" ");
        this.mRotateExitTransformation.printShortString(pw);
        pw.println();
        pw.print(prefix);
        pw.print("mRotateEnterAnimation=");
        pw.print(this.mRotateEnterAnimation);
        pw.print(" ");
        this.mRotateEnterTransformation.printShortString(pw);
        pw.println();
        pw.print(prefix);
        pw.print("mSnapshotInitialMatrix=");
        this.mSnapshotInitialMatrix.dump(pw);
        pw.println();
    }

    public void setRotation(android.view.SurfaceControl.Transaction t, int rotation) {
        this.mCurRotation = rotation;
        int delta = android.util.RotationUtils.deltaRotation(rotation, this.mOriginalRotation);
        com.android.server.wm.utils.CoordinateTransforms.computeRotationMatrix(delta, this.mOriginalWidth, this.mOriginalHeight, this.mSnapshotInitialMatrix);
        setRotationTransform(t, this.mSnapshotInitialMatrix);
    }

    private boolean startAnimation(android.view.SurfaceControl.Transaction t, long maxAnimationDuration, float animationScale, int finalWidth, int finalHeight, int exitAnim, int enterAnim) {
        boolean z;
        boolean customAnim;
        if (this.mScreenshotLayer == null) {
            return false;
        }
        if (this.mStarted) {
            return true;
        }
        this.mStarted = true;
        int delta = android.util.RotationUtils.deltaRotation(this.mCurRotation, this.mOriginalRotation);
        if (exitAnim == 0 || enterAnim == 0 || this.mSRAWrapper.getExtImpl().getDeviceFolding()) {
            z = true;
            if (!this.mSRAWrapper.getExtImpl().hookLoadAnimation(delta, this.mOriginalWidth, this.mOriginalHeight, finalWidth, finalHeight)) {
                switch (delta) {
                    case 0:
                        this.mRotateExitAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.recents_fade_out);
                        this.mRotateEnterAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.push_down_out_no_alpha);
                        break;
                    case 1:
                        this.mRotateExitAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.screen_rotate_180_exit);
                        this.mRotateEnterAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.screen_rotate_180_enter);
                        break;
                    case 2:
                        this.mRotateExitAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.resolver_launch_anim);
                        this.mRotateEnterAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.resolver_close_anim);
                        break;
                    case 3:
                        this.mRotateExitAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.screen_rotate_0_exit);
                        this.mRotateEnterAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.screen_rotate_0_enter);
                        break;
                }
            }
            customAnim = false;
        } else {
            this.mRotateExitAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mContext, exitAnim);
            this.mRotateEnterAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mContext, enterAnim);
            this.mRotateAlphaAnimation = android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.rotation_animation_jump_exit);
            this.mSRAWrapper.getExtImpl().changeRotateAnimation(this.mRotateExitAnimation, this.mRotateEnterAnimation, this.mRotateAlphaAnimation, this.mContext);
            customAnim = true;
            z = true;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(customAnim);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(android.view.Surface.rotationToString(this.mCurRotation));
            java.lang.String protoLogParam2 = java.lang.String.valueOf(android.view.Surface.rotationToString(this.mOriginalRotation));
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -6586462455018013482L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2);
        }
        this.mRotateExitAnimation.initialize(finalWidth, finalHeight, this.mOriginalWidth, this.mOriginalHeight);
        this.mRotateExitAnimation.restrictDuration(maxAnimationDuration);
        this.mRotateExitAnimation.scaleCurrentDuration(animationScale);
        if (!this.mSRAWrapper.getExtImpl().enterAnimationinitialize(this.mRotateEnterAnimation, this.mOriginalWidth, this.mOriginalHeight, finalWidth, finalHeight)) {
            this.mRotateEnterAnimation.initialize(finalWidth, finalHeight, this.mOriginalWidth, this.mOriginalHeight);
        }
        this.mRotateEnterAnimation.restrictDuration(maxAnimationDuration);
        this.mRotateEnterAnimation.scaleCurrentDuration(animationScale);
        this.mAnimRunning = false;
        this.mFinishAnimReady = false;
        this.mFinishAnimStartTime = -1L;
        if (customAnim) {
            this.mRotateAlphaAnimation.restrictDuration(maxAnimationDuration);
            this.mRotateAlphaAnimation.scaleCurrentDuration(animationScale);
        }
        if (customAnim && this.mEnteringBlackFrame == null) {
            try {
                android.graphics.Rect outer = new android.graphics.Rect(-finalWidth, -finalHeight, finalWidth * 2, finalHeight * 2);
                android.graphics.Rect inner = new android.graphics.Rect(0, 0, finalWidth, finalHeight);
                this.mEnteringBlackFrame = new com.android.server.wm.BlackFrame(this.mService.mTransactionFactory, t, outer, inner, 2010000, this.mDisplayContent, false, this.mEnterBlackFrameLayer);
            } catch (android.view.Surface.OutOfResourcesException e) {
                android.util.Slog.w(TAG, "Unable to allocate black surface", e);
            }
        }
        if (customAnim) {
            this.mSurfaceRotationAnimationController.startCustomAnimation();
        } else {
            this.mSurfaceRotationAnimationController.startScreenRotationAnimation();
        }
        return z;
    }

    public boolean dismiss(android.view.SurfaceControl.Transaction t, long maxAnimationDuration, float animationScale, int finalWidth, int finalHeight, int exitAnim, int enterAnim) {
        if (this.mScreenshotLayer == null) {
            return false;
        }
        if (!this.mStarted) {
            this.mEndLuma = this.mSRAWrapper.getExtImpl().getLuma(false);
            if (this.mEndLuma == Float.MIN_VALUE) {
                this.mEndLuma = com.android.internal.policy.TransitionAnimation.getBorderLuma(this.mDisplayContent.getWindowingLayer(), finalWidth, finalHeight);
            }
            this.mSRAWrapper.getExtImpl().hookComputStartLumaForDismiss(this.mCurRotation, this.mOriginalRotation, this.mDisplayContent);
            startAnimation(t, maxAnimationDuration, animationScale, finalWidth, finalHeight, exitAnim, enterAnim);
            this.mSRAWrapper.getSocExtImpl().hookPerfLockAcquired();
        }
        if (!this.mStarted) {
            return false;
        }
        this.mFinishAnimReady = true;
        return true;
    }

    public void kill() {
        if (this.mSurfaceRotationAnimationController != null) {
            this.mSurfaceRotationAnimationController.cancel();
            this.mSurfaceRotationAnimationController = null;
        }
        if (this.mScreenshotLayer != null) {
            if (!com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC.isLogToLogcat()) {
                android.util.Slog.i(TAG, "  FREEZE " + this.mScreenshotLayer + ": DESTROY");
            } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_SURFACE_ALLOC_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mScreenshotLayer);
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, -5825336546511998057L, 0, null, protoLogParam0);
            }
            android.view.SurfaceControl.Transaction t = this.mService.mTransactionFactory.get();
            if (this.mScreenshotLayer.isValid()) {
                t.remove(this.mScreenshotLayer);
            }
            this.mScreenshotLayer = null;
            if (this.mEnterBlackFrameLayer != null) {
                if (this.mEnterBlackFrameLayer.isValid()) {
                    t.remove(this.mEnterBlackFrameLayer);
                }
                this.mEnterBlackFrameLayer = null;
            }
            if (this.mBackColorSurface != null) {
                if (this.mBackColorSurface.isValid()) {
                    t.remove(this.mBackColorSurface);
                }
                this.mBackColorSurface = null;
            }
            if (this.mRoundedCornerOverlay != null) {
                if (this.mDisplayContent.getRotationAnimation() == null || this.mDisplayContent.getRotationAnimation() == this) {
                    setSkipScreenshotForRoundedCornerOverlays(true, t);
                    for (android.view.SurfaceControl sc : this.mRoundedCornerOverlay) {
                        if (sc.isValid()) {
                            t.show(sc);
                        }
                    }
                }
                this.mRoundedCornerOverlay = null;
            }
            t.apply();
        }
        if (this.mEnteringBlackFrame != null) {
            this.mEnteringBlackFrame.kill();
            this.mEnteringBlackFrame = null;
        }
        if (this.mRotateExitAnimation != null) {
            this.mRotateExitAnimation.cancel();
            this.mRotateExitAnimation = null;
        }
        if (this.mRotateEnterAnimation != null) {
            this.mRotateEnterAnimation.cancel();
            this.mRotateEnterAnimation = null;
        }
        if (this.mRotateAlphaAnimation != null) {
            this.mRotateAlphaAnimation.cancel();
            this.mRotateAlphaAnimation = null;
        }
        this.mSRAWrapper.getSocExtImpl().hookPerfLockRelease();
    }

    public boolean isAnimating() {
        return this.mSurfaceRotationAnimationController != null && this.mSurfaceRotationAnimationController.isAnimating();
    }

    public boolean isRotating() {
        return this.mCurRotation != this.mOriginalRotation;
    }

    class SurfaceRotationAnimationController {
        private com.android.server.wm.SurfaceAnimator mDisplayAnimator;
        private com.android.server.wm.SurfaceAnimator mEnterBlackFrameAnimator;
        private com.android.server.wm.SurfaceAnimator mRotateScreenAnimator;
        private com.android.server.wm.SurfaceAnimator mScreenshotRotationAnimator;

        SurfaceRotationAnimationController() {
        }

        void startCustomAnimation() {
            try {
                com.android.server.wm.ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner.deferStartingAnimations();
                this.mRotateScreenAnimator = startScreenshotAlphaAnimation();
                this.mDisplayAnimator = startDisplayRotation();
                if (com.android.server.wm.ScreenRotationAnimation.this.mEnteringBlackFrame != null) {
                    this.mEnterBlackFrameAnimator = startEnterBlackFrameAnimation();
                }
                com.android.server.wm.ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().notifyScreenshotAnimationStart();
            } finally {
                com.android.server.wm.ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner.continueStartingAnimations();
            }
        }

        void startScreenRotationAnimation() {
            try {
                com.android.server.wm.ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner.deferStartingAnimations();
                this.mDisplayAnimator = startDisplayRotation();
                this.mScreenshotRotationAnimator = startScreenshotRotationAnimation();
                if (!com.android.server.wm.ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().startScreenRotateBackColorAnimation(new float[]{com.android.server.wm.ScreenRotationAnimation.this.mEndLuma, com.android.server.wm.ScreenRotationAnimation.this.mEndLuma, com.android.server.wm.ScreenRotationAnimation.this.mEndLuma}, com.android.server.wm.ScreenRotationAnimation.this.mRotateEnterAnimation, com.android.server.wm.ScreenRotationAnimation.this.mBackColorSurface, com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent)) {
                    startColorAnimation();
                }
            } finally {
                com.android.server.wm.ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner.continueStartingAnimations();
            }
        }

        private com.android.server.wm.SimpleSurfaceAnimatable.Builder initializeBuilder() {
            com.android.server.wm.SimpleSurfaceAnimatable.Builder builder = new com.android.server.wm.SimpleSurfaceAnimatable.Builder();
            final com.android.server.wm.DisplayContent displayContent = com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent;
            java.util.Objects.requireNonNull(displayContent);
            com.android.server.wm.SimpleSurfaceAnimatable.Builder syncTransactionSupplier = builder.setSyncTransactionSupplier(new java.util.function.Supplier() { // from class: com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return displayContent.getSyncTransaction();
                }
            });
            final com.android.server.wm.DisplayContent displayContent2 = com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent;
            java.util.Objects.requireNonNull(displayContent2);
            com.android.server.wm.SimpleSurfaceAnimatable.Builder pendingTransactionSupplier = syncTransactionSupplier.setPendingTransactionSupplier(new java.util.function.Supplier() { // from class: com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda2
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return displayContent2.getPendingTransaction();
                }
            });
            final com.android.server.wm.DisplayContent displayContent3 = com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent;
            java.util.Objects.requireNonNull(displayContent3);
            com.android.server.wm.SimpleSurfaceAnimatable.Builder commitTransactionRunnable = pendingTransactionSupplier.setCommitTransactionRunnable(new java.lang.Runnable() { // from class: com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    displayContent3.commitPendingTransaction();
                }
            });
            final com.android.server.wm.DisplayContent displayContent4 = com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent;
            java.util.Objects.requireNonNull(displayContent4);
            return commitTransactionRunnable.setAnimationLeashSupplier(new java.util.function.Supplier() { // from class: com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda4
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return displayContent4.makeOverlay();
                }
            });
        }

        private com.android.server.wm.SurfaceAnimator startDisplayRotation() {
            com.android.server.wm.SurfaceAnimator animator = startAnimation(initializeBuilder().setAnimationLeashParent(com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getSurfaceControl()).setSurfaceControl(com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getWindowingLayer()).setParentSurfaceControl(com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getSurfaceControl()).setWidth(com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getSurfaceWidth()).setHeight(com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getSurfaceHeight()).build(), createWindowAnimationSpec(com.android.server.wm.ScreenRotationAnimation.this.mRotateEnterAnimation), new com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda0(this));
            android.graphics.Rect displayBounds = com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getBounds();
            com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getPendingTransaction().setWindowCrop(animator.mLeash, displayBounds.width(), displayBounds.height());
            return animator;
        }

        private com.android.server.wm.SurfaceAnimator startScreenshotAlphaAnimation() {
            return startAnimation(initializeBuilder().setSurfaceControl(com.android.server.wm.ScreenRotationAnimation.this.mScreenshotLayer).setAnimationLeashParent(com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getOverlayLayer()).setWidth(com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getSurfaceWidth()).setHeight(com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getSurfaceHeight()).build(), createWindowAnimationSpec(com.android.server.wm.ScreenRotationAnimation.this.mRotateAlphaAnimation), new com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda0(this));
        }

        private com.android.server.wm.SurfaceAnimator startEnterBlackFrameAnimation() {
            return startAnimation(initializeBuilder().setSurfaceControl(com.android.server.wm.ScreenRotationAnimation.this.mEnterBlackFrameLayer).setAnimationLeashParent(com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getOverlayLayer()).build(), createWindowAnimationSpec(com.android.server.wm.ScreenRotationAnimation.this.mRotateEnterAnimation), new com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda0(this));
        }

        private com.android.server.wm.SurfaceAnimator startScreenshotRotationAnimation() {
            return startAnimation(initializeBuilder().setSurfaceControl(com.android.server.wm.ScreenRotationAnimation.this.mScreenshotLayer).setAnimationLeashParent(com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getOverlayLayer()).build(), createWindowAnimationSpec(com.android.server.wm.ScreenRotationAnimation.this.mRotateExitAnimation), new com.android.server.wm.ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda0(this));
        }

        private void startColorAnimation() {
            final int colorTransitionMs = com.android.server.wm.ScreenRotationAnimation.this.mContext.getResources().getInteger(android.R.integer.config_reevaluate_bootstrap_sim_data_usage_millis);
            com.android.server.wm.SurfaceAnimationRunner runner = com.android.server.wm.ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner;
            final float[] rgbTmpFloat = new float[3];
            final int startColor = android.graphics.Color.rgb(com.android.server.wm.ScreenRotationAnimation.this.mStartLuma, com.android.server.wm.ScreenRotationAnimation.this.mStartLuma, com.android.server.wm.ScreenRotationAnimation.this.mStartLuma);
            final int endColor = android.graphics.Color.rgb(com.android.server.wm.ScreenRotationAnimation.this.mEndLuma, com.android.server.wm.ScreenRotationAnimation.this.mEndLuma, com.android.server.wm.ScreenRotationAnimation.this.mEndLuma);
            final long duration = ((long) colorTransitionMs) * ((long) com.android.server.wm.ScreenRotationAnimation.this.mService.getCurrentAnimatorScale());
            final android.animation.ArgbEvaluator va = android.animation.ArgbEvaluator.getInstance();
            runner.startAnimation(new com.android.server.wm.LocalAnimationAdapter.AnimationSpec() { // from class: com.android.server.wm.ScreenRotationAnimation.SurfaceRotationAnimationController.1
                @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
                public long getDuration() {
                    return duration;
                }

                @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
                public void apply(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash, long currentPlayTime) {
                    float fraction = getFraction(currentPlayTime);
                    int color = ((java.lang.Integer) va.evaluate(fraction, java.lang.Integer.valueOf(startColor), java.lang.Integer.valueOf(endColor))).intValue();
                    android.graphics.Color middleColor = android.graphics.Color.valueOf(color);
                    rgbTmpFloat[0] = middleColor.red();
                    rgbTmpFloat[1] = middleColor.green();
                    rgbTmpFloat[2] = middleColor.blue();
                    if (leash.isValid()) {
                        t.setColor(leash, rgbTmpFloat);
                    }
                }

                @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
                public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
                    pw.println(prefix + "startLuma=" + com.android.server.wm.ScreenRotationAnimation.this.mStartLuma + " endLuma=" + com.android.server.wm.ScreenRotationAnimation.this.mEndLuma + " durationMs=" + colorTransitionMs);
                }

                @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
                public void dumpDebugInner(android.util.proto.ProtoOutputStream proto) {
                    long token = proto.start(1146756268036L);
                    proto.write(1108101562369L, com.android.server.wm.ScreenRotationAnimation.this.mStartLuma);
                    proto.write(1108101562370L, com.android.server.wm.ScreenRotationAnimation.this.mEndLuma);
                    proto.write(1112396529667L, colorTransitionMs);
                    proto.end(token);
                }
            }, com.android.server.wm.ScreenRotationAnimation.this.mBackColorSurface, com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getPendingTransaction(), null);
        }

        private com.android.server.wm.WindowAnimationSpec createWindowAnimationSpec(android.view.animation.Animation mAnimation) {
            return new com.android.server.wm.WindowAnimationSpec(mAnimation, new android.graphics.Point(0, 0), false, com.android.server.wm.ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().getWindowCornerRadius());
        }

        private com.android.server.wm.SurfaceAnimator startAnimation(com.android.server.wm.SurfaceAnimator.Animatable animatable, com.android.server.wm.LocalAnimationAdapter.AnimationSpec animationSpec, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback animationFinishedCallback) {
            com.android.server.wm.SurfaceAnimator animator = new com.android.server.wm.SurfaceAnimator(animatable, animationFinishedCallback, com.android.server.wm.ScreenRotationAnimation.this.mService);
            com.android.server.wm.LocalAnimationAdapter localAnimationAdapter = new com.android.server.wm.LocalAnimationAdapter(animationSpec, com.android.server.wm.ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner);
            if (!com.android.server.wm.ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().hookAdjustScreenshotInitialRotation(localAnimationAdapter, animator, com.android.server.wm.ScreenRotationAnimation.this.mOriginalWidth, com.android.server.wm.ScreenRotationAnimation.this.mOriginalHeight, false, com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent, com.android.server.wm.ScreenRotationAnimation.this.mScreenshotLayer, com.android.server.wm.ScreenRotationAnimation.this.mCurRotation)) {
                animator.startAnimation(com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getPendingTransaction(), localAnimationAdapter, false, 2);
            }
            return animator;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onAnimationEnd(int type, com.android.server.wm.AnimationAdapter anim) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.ScreenRotationAnimation.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (isAnimating()) {
                        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                            long protoLogParam0 = type;
                            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mDisplayAnimator != null ? java.lang.Boolean.valueOf(this.mDisplayAnimator.isAnimating()) : null);
                            java.lang.String protoLogParam2 = java.lang.String.valueOf(this.mEnterBlackFrameAnimator != null ? java.lang.Boolean.valueOf(this.mEnterBlackFrameAnimator.isAnimating()) : null);
                            java.lang.String protoLogParam3 = java.lang.String.valueOf(this.mRotateScreenAnimator != null ? java.lang.Boolean.valueOf(this.mRotateScreenAnimator.isAnimating()) : null);
                            java.lang.String protoLogParam4 = java.lang.String.valueOf(this.mScreenshotRotationAnimator != null ? java.lang.Boolean.valueOf(this.mScreenshotRotationAnimator.isAnimating()) : null);
                            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 6883897856740637908L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1, protoLogParam2, protoLogParam3, protoLogParam4);
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (!com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                        android.util.Slog.d(com.android.server.wm.ScreenRotationAnimation.TAG, "ScreenRotationAnimation onAnimationEnd");
                    } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[0]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -3943622313307983155L, 0, null, null);
                    }
                    this.mEnterBlackFrameAnimator = null;
                    this.mScreenshotRotationAnimator = null;
                    this.mRotateScreenAnimator = null;
                    com.android.server.wm.WindowAnimator windowAnimator = com.android.server.wm.ScreenRotationAnimation.this.mService.mAnimator;
                    windowAnimator.mBulkUpdateParams = 1 | windowAnimator.mBulkUpdateParams;
                    if (com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.getRotationAnimation() == com.android.server.wm.ScreenRotationAnimation.this) {
                        com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.setRotationAnimation(null);
                        if (com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.mDisplayRotationCompatPolicy != null) {
                            com.android.server.wm.ScreenRotationAnimation.this.mDisplayContent.mDisplayRotationCompatPolicy.onScreenRotationAnimationFinished();
                        }
                    } else {
                        com.android.server.wm.ScreenRotationAnimation.this.kill();
                    }
                    com.android.server.wm.ScreenRotationAnimation.this.mService.updateRotation(false, false);
                    com.android.server.wm.ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().setFrozenByUserSwitching(false);
                    com.android.server.wm.ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().adjustBlurBackgroundLayer();
                    com.android.server.wm.ScreenRotationAnimation.this.mSRAWrapper.getExtImpl().onScreenRotationAnimationEnd();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }

        public void cancel() {
            if (this.mEnterBlackFrameAnimator != null) {
                this.mEnterBlackFrameAnimator.cancelAnimation();
            }
            if (this.mScreenshotRotationAnimator != null) {
                this.mScreenshotRotationAnimator.cancelAnimation();
            }
            if (this.mRotateScreenAnimator != null) {
                this.mRotateScreenAnimator.cancelAnimation();
            }
            if (this.mDisplayAnimator != null) {
                this.mDisplayAnimator.cancelAnimation();
            }
            if (com.android.server.wm.ScreenRotationAnimation.this.mBackColorSurface != null) {
                com.android.server.wm.ScreenRotationAnimation.this.mService.mSurfaceAnimationRunner.onAnimationCancelled(com.android.server.wm.ScreenRotationAnimation.this.mBackColorSurface);
            }
        }

        public boolean isAnimating() {
            return (this.mDisplayAnimator != null && this.mDisplayAnimator.isAnimating()) || (this.mEnterBlackFrameAnimator != null && this.mEnterBlackFrameAnimator.isAnimating()) || ((this.mRotateScreenAnimator != null && this.mRotateScreenAnimator.isAnimating()) || (this.mScreenshotRotationAnimator != null && this.mScreenshotRotationAnimator.isAnimating()));
        }
    }

    public com.android.server.wm.IScreenRotationAnimationWrapper getWrapper() {
        return this.mSRAWrapper;
    }

    private class ScreenRotationAnimationWrapper implements com.android.server.wm.IScreenRotationAnimationWrapper {
        private ScreenRotationAnimationWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.IScreenRotationAnimationExt getExtImpl() {
            return com.android.server.wm.ScreenRotationAnimation.this.mSRAExt;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.wm.IScreenRotationAnimationSocExt getSocExtImpl() {
            return com.android.server.wm.ScreenRotationAnimation.this.mAnimationSocExt;
        }

        @Override // com.android.server.wm.IScreenRotationAnimationWrapper
        public void setRotateExitAnimation(android.view.animation.Animation rotateExitAnimation) {
            com.android.server.wm.ScreenRotationAnimation.this.mRotateExitAnimation = rotateExitAnimation;
        }

        @Override // com.android.server.wm.IScreenRotationAnimationWrapper
        public void setRotateEnterAnimation(android.view.animation.Animation rotateEnterAnimation) {
            com.android.server.wm.ScreenRotationAnimation.this.mRotateEnterAnimation = rotateEnterAnimation;
        }

        @Override // com.android.server.wm.IScreenRotationAnimationWrapper
        public void setCurRotation(int rotation) {
            com.android.server.wm.ScreenRotationAnimation.this.mCurRotation = rotation;
        }

        @Override // com.android.server.wm.IScreenRotationAnimationWrapper
        public void setEndLuma(float endLuma) {
            com.android.server.wm.ScreenRotationAnimation.this.mEndLuma = endLuma;
        }
    }
}
