package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class SurfaceAnimationRunner {
    private static final int ANIMATION_SUPRESSION_TIME = 500;
    private static final long EDGEEXTENSION_TIME_OUT_DURATION = 5000;
    private static final int SUPRESSION_ANIMATION = 2;
    private static final java.lang.String TAG = "SurfaceAnimationRunner";
    private final android.animation.AnimationHandler mAnimationHandler;
    private boolean mAnimationStartDeferred;
    private final android.os.Handler mAnimationThreadHandler;
    private final com.android.server.wm.SurfaceAnimationRunner.AnimatorFactory mAnimatorFactory;
    private boolean mApplyScheduled;
    private final java.lang.Runnable mApplyTransactionRunnable;
    private final java.lang.Object mCancelLock;
    android.view.Choreographer mChoreographer;
    private final java.util.concurrent.ExecutorService mEdgeExtensionExecutor;
    private final java.lang.Object mEdgeExtensionLock;
    private final android.util.ArrayMap<android.view.SurfaceControl, java.util.ArrayList<android.view.SurfaceControl>> mEdgeExtensions;
    private final android.view.SurfaceControl.Transaction mFrameTransaction;
    private final java.lang.Object mLock;
    final android.util.ArrayMap<android.view.SurfaceControl, com.android.server.wm.SurfaceAnimationRunner.RunningAnimation> mPendingAnimations;
    private final android.os.PowerManagerInternal mPowerManagerInternal;
    final android.util.ArrayMap<android.view.SurfaceControl, com.android.server.wm.SurfaceAnimationRunner.RunningAnimation> mPreProcessingAnimations;
    final android.util.ArrayMap<android.view.SurfaceControl, com.android.server.wm.SurfaceAnimationRunner.RunningAnimation> mRunningAnimations;
    private final android.os.Handler mSurfaceAnimationHandler;
    private com.android.server.wm.ISurfaceAnimationRunnerExt mSurfaceAnimationRunnerExt;

    interface AnimatorFactory {
        android.animation.ValueAnimator makeAnimator();
    }

    SurfaceAnimationRunner(java.util.function.Supplier<android.view.SurfaceControl.Transaction> transactionFactory, android.os.PowerManagerInternal powerManagerInternal) {
        this(null, null, transactionFactory.get(), powerManagerInternal);
    }

    SurfaceAnimationRunner(android.animation.AnimationHandler.AnimationFrameCallbackProvider callbackProvider, com.android.server.wm.SurfaceAnimationRunner.AnimatorFactory animatorFactory, android.view.SurfaceControl.Transaction frameTransaction, android.os.PowerManagerInternal powerManagerInternal) {
        android.animation.AnimationHandler.AnimationFrameCallbackProvider sfVsyncFrameCallbackProvider;
        com.android.server.wm.SurfaceAnimationRunner.AnimatorFactory animatorFactory2;
        this.mLock = new java.lang.Object();
        this.mCancelLock = new java.lang.Object();
        this.mEdgeExtensionLock = new java.lang.Object();
        this.mAnimationThreadHandler = com.android.server.AnimationThread.getHandler();
        this.mSurfaceAnimationHandler = com.android.server.wm.SurfaceAnimationThread.getHandler();
        this.mApplyTransactionRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.applyTransaction();
            }
        };
        this.mEdgeExtensionExecutor = java.util.concurrent.Executors.newFixedThreadPool(2);
        this.mPendingAnimations = new android.util.ArrayMap<>();
        this.mPreProcessingAnimations = new android.util.ArrayMap<>();
        this.mRunningAnimations = new android.util.ArrayMap<>();
        this.mEdgeExtensions = new android.util.ArrayMap<>();
        this.mSurfaceAnimationRunnerExt = (com.android.server.wm.ISurfaceAnimationRunnerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ISurfaceAnimationRunnerExt.class).base(this).create();
        this.mSurfaceAnimationHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        }, 0L);
        this.mFrameTransaction = frameTransaction;
        this.mAnimationHandler = new android.animation.AnimationHandler();
        android.animation.AnimationHandler animationHandler = this.mAnimationHandler;
        if (callbackProvider != null) {
            sfVsyncFrameCallbackProvider = callbackProvider;
        } else {
            sfVsyncFrameCallbackProvider = new com.android.internal.graphics.SfVsyncFrameCallbackProvider(this.mChoreographer);
        }
        animationHandler.setProvider(sfVsyncFrameCallbackProvider);
        if (animatorFactory != null) {
            animatorFactory2 = animatorFactory;
        } else {
            animatorFactory2 = new com.android.server.wm.SurfaceAnimationRunner.AnimatorFactory() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda4
                @Override // com.android.server.wm.SurfaceAnimationRunner.AnimatorFactory
                public final android.animation.ValueAnimator makeAnimator() {
                    return this.f$0.lambda$new$1();
                }
            };
        }
        this.mAnimatorFactory = animatorFactory2;
        this.mPowerManagerInternal = powerManagerInternal;
        if (this.mChoreographer != null && this.mChoreographer.mChoreographerExt != null) {
            this.mChoreographer.mChoreographerExt.setIsSFChoregrapher(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mChoreographer = android.view.Choreographer.getSfInstance();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.animation.ValueAnimator lambda$new$1() {
        return new com.android.server.wm.SurfaceAnimationRunner.SfValueAnimator();
    }

    void deferStartingAnimations() {
        synchronized (this.mLock) {
            this.mAnimationStartDeferred = true;
        }
    }

    void continueStartingAnimations() {
        synchronized (this.mLock) {
            this.mAnimationStartDeferred = false;
            if (!this.mPendingAnimations.isEmpty() && this.mPreProcessingAnimations.isEmpty()) {
                this.mChoreographer.postFrameCallback(new com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda0(this));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTransactionCommitted(com.android.server.wm.LocalAnimationAdapter.AnimationSpec a, android.view.SurfaceControl animationLeash, com.android.server.wm.SurfaceAnimationRunner.RunningAnimation runningAnim) throws java.lang.Throwable {
        if (a == null || animationLeash == null || runningAnim == null) {
            return;
        }
        com.android.server.wm.WindowAnimationSpec animationSpec = a.asWindowAnimationSpec();
        android.view.SurfaceControl.Transaction edgeExtensionCreationTransaction = new android.view.SurfaceControl.Transaction();
        edgeExtendWindow(animationLeash, animationSpec.getRootTaskBounds(), animationSpec.getAnimation(), edgeExtensionCreationTransaction);
        synchronized (this.mLock) {
            android.util.Slog.i(TAG, "startAnimation EdgeExtensionExecutor onTransactionCommitted timeout, animationLeash=" + animationLeash + ", runningAnim=" + runningAnim + ", mPreProcessingAnimations.get(animationLeash)=" + this.mPreProcessingAnimations.get(animationLeash) + ", mPreProcessingAnimations.containsValue(runningAnim)=" + this.mPreProcessingAnimations.containsValue(runningAnim) + ", mPreProcessingAnimations=" + this.mPreProcessingAnimations);
            if (this.mPreProcessingAnimations.get(animationLeash) == runningAnim || this.mPreProcessingAnimations.containsValue(runningAnim)) {
                synchronized (this.mEdgeExtensionLock) {
                    if (!this.mEdgeExtensions.isEmpty()) {
                        edgeExtensionCreationTransaction.apply();
                    }
                }
                this.mPreProcessingAnimations.remove(animationLeash);
                this.mPendingAnimations.put(animationLeash, runningAnim);
                if (!this.mAnimationStartDeferred && this.mPreProcessingAnimations.isEmpty()) {
                    this.mChoreographer.postFrameCallback(new com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda0(this));
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v16, types: [com.android.server.wm.SurfaceAnimationRunner$1CommitCallback, java.lang.Runnable] */
    void startAnimation(final com.android.server.wm.LocalAnimationAdapter.AnimationSpec a, final android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, java.lang.Runnable finishCallback) {
        synchronized (this.mLock) {
            if (animationLeash != null) {
                if (animationLeash.isValid()) {
                    final com.android.server.wm.SurfaceAnimationRunner.RunningAnimation runningAnim = new com.android.server.wm.SurfaceAnimationRunner.RunningAnimation(a, animationLeash, finishCallback);
                    boolean requiresEdgeExtension = requiresEdgeExtension(a);
                    com.android.server.wm.utils.LogUtil.sDebugI(TAG, "startAnimation requiresEdgeExtension=" + requiresEdgeExtension + ", duration=" + a.getDuration() + ", animationLeash=" + animationLeash + ", runningAnim=" + runningAnim + ", mPreProcessingAnimations=" + this.mPreProcessingAnimations);
                    if (requiresEdgeExtension) {
                        java.util.ArrayList<android.view.SurfaceControl> extensionSurfaces = new java.util.ArrayList<>();
                        synchronized (this.mEdgeExtensionLock) {
                            this.mEdgeExtensions.put(animationLeash, extensionSurfaces);
                        }
                        this.mPreProcessingAnimations.put(animationLeash, runningAnim);
                        final ?? r0 = new java.lang.Runnable() { // from class: com.android.server.wm.SurfaceAnimationRunner.1CommitCallback
                            @Override // java.lang.Runnable
                            public void run() throws java.lang.Throwable {
                                com.android.server.wm.SurfaceAnimationRunner.this.onTransactionCommitted(a, animationLeash, runningAnim);
                            }
                        };
                        this.mSurfaceAnimationHandler.postDelayed(r0, EDGEEXTENSION_TIME_OUT_DURATION);
                        t.addTransactionCommittedListener(this.mEdgeExtensionExecutor, new android.view.SurfaceControl.TransactionCommittedListener() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda1
                            @Override // android.view.SurfaceControl.TransactionCommittedListener
                            public final void onTransactionCommitted() throws java.lang.Throwable {
                                this.f$0.lambda$startAnimation$2(r0, animationLeash, a, runningAnim);
                            }
                        });
                    }
                    if (!requiresEdgeExtension) {
                        this.mPendingAnimations.put(animationLeash, runningAnim);
                        if (!this.mAnimationStartDeferred && this.mPreProcessingAnimations.isEmpty()) {
                            this.mChoreographer.postFrameCallback(new com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda0(this));
                        }
                        this.mSurfaceAnimationRunnerExt.trySaveAnimationLeashHashAndReinitializeAnimParams(a, animationLeash.hashCode());
                    }
                    applyTransformation(runningAnim, t, 0L);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$2(com.android.server.wm.SurfaceAnimationRunner.C1CommitCallback callback, android.view.SurfaceControl animationLeash, com.android.server.wm.LocalAnimationAdapter.AnimationSpec a, com.android.server.wm.SurfaceAnimationRunner.RunningAnimation runningAnim) throws java.lang.Throwable {
        this.mSurfaceAnimationHandler.removeCallbacks(callback);
        if (!animationLeash.isValid()) {
            android.util.Log.e(TAG, "Animation leash is not valid");
            synchronized (this.mEdgeExtensionLock) {
                this.mEdgeExtensions.remove(animationLeash);
            }
            synchronized (this.mLock) {
                this.mPreProcessingAnimations.remove(animationLeash);
            }
            return;
        }
        com.android.server.wm.WindowAnimationSpec animationSpec = a.asWindowAnimationSpec();
        android.view.SurfaceControl.Transaction edgeExtensionCreationTransaction = new android.view.SurfaceControl.Transaction();
        edgeExtendWindow(animationLeash, animationSpec.getRootTaskBounds(), animationSpec.getAnimation(), edgeExtensionCreationTransaction);
        synchronized (this.mLock) {
            com.android.server.wm.utils.LogUtil.sDebugI(TAG, "startAnimation EdgeExtensionExecutor, animationLeash=" + animationLeash + ", runningAnim=" + runningAnim + ", mPreProcessingAnimations.get(animationLeash)=" + this.mPreProcessingAnimations.get(animationLeash) + ", mPreProcessingAnimations.containsValue(runningAnim)=" + this.mPreProcessingAnimations.containsValue(runningAnim) + ", mPreProcessingAnimations=" + this.mPreProcessingAnimations);
            if (this.mPreProcessingAnimations.get(animationLeash) == runningAnim || this.mPreProcessingAnimations.containsValue(runningAnim)) {
                synchronized (this.mEdgeExtensionLock) {
                    if (!this.mEdgeExtensions.isEmpty()) {
                        edgeExtensionCreationTransaction.apply();
                    }
                }
                this.mPreProcessingAnimations.remove(animationLeash);
                this.mPendingAnimations.put(animationLeash, runningAnim);
                if (!this.mAnimationStartDeferred && this.mPreProcessingAnimations.isEmpty()) {
                    this.mChoreographer.postFrameCallback(new com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda0(this));
                }
            }
        }
    }

    private boolean requiresEdgeExtension(com.android.server.wm.LocalAnimationAdapter.AnimationSpec a) {
        return a.asWindowAnimationSpec() != null && a.asWindowAnimationSpec().hasExtension();
    }

    void onAnimationCancelled(android.view.SurfaceControl leash) {
        synchronized (this.mLock) {
            if (this.mPendingAnimations.containsKey(leash)) {
                this.mPendingAnimations.remove(leash);
                return;
            }
            if (this.mPreProcessingAnimations.containsKey(leash)) {
                this.mPreProcessingAnimations.remove(leash);
                return;
            }
            final com.android.server.wm.SurfaceAnimationRunner.RunningAnimation anim = this.mRunningAnimations.get(leash);
            if (anim != null) {
                this.mRunningAnimations.remove(leash);
                synchronized (this.mCancelLock) {
                    anim.mCancelled = true;
                }
                if (anim.mAnimSpec != null) {
                    this.mSurfaceAnimationRunnerExt.tryClearAnimPointsWhenCancelled(anim.mAnimSpec, leash.hashCode());
                }
                if (anim.mAnimSpec != null) {
                    this.mSurfaceAnimationRunnerExt.onAnimationEnd(anim.mAnimSpec, this.mChoreographer);
                }
                android.util.Slog.d(TAG, "onAnimationCancelled:" + leash + " " + android.os.Debug.getCallers(5));
                this.mSurfaceAnimationHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onAnimationCancelled$3(anim);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAnimationCancelled$3(com.android.server.wm.SurfaceAnimationRunner.RunningAnimation anim) {
        anim.mAnim.cancel();
        applyTransaction();
    }

    private void startPendingAnimationsLocked() {
        for (int i = this.mPendingAnimations.size() - 1; i >= 0; i--) {
            startAnimationLocked(this.mPendingAnimations.valueAt(i));
        }
        this.mPendingAnimations.clear();
    }

    private void startAnimationLocked(final com.android.server.wm.SurfaceAnimationRunner.RunningAnimation a) {
        final android.animation.ValueAnimator anim = this.mAnimatorFactory.makeAnimator();
        this.mSurfaceAnimationRunnerExt.computeAnimHashForstartAnimationLocked(a.mAnimSpec);
        this.mSurfaceAnimationRunnerExt.callGcSupression(2, 500);
        this.mSurfaceAnimationRunnerExt.onAnimationStart(a.mAnimSpec, a.mAnimSpec.getDuration(), this.mChoreographer);
        anim.overrideDurationScale(1.0f);
        anim.setDuration(a.mAnimSpec.getDuration());
        anim.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() { // from class: com.android.server.wm.SurfaceAnimationRunner$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(android.animation.ValueAnimator valueAnimator) {
                this.f$0.lambda$startAnimationLocked$4(a, anim, valueAnimator);
            }
        });
        anim.addListener(new android.animation.AnimatorListenerAdapter() { // from class: com.android.server.wm.SurfaceAnimationRunner.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(android.animation.Animator animation) {
                synchronized (com.android.server.wm.SurfaceAnimationRunner.this.mCancelLock) {
                    if (!a.mCancelled) {
                        android.util.Slog.d(com.android.server.wm.SurfaceAnimationRunner.TAG, "onAnimationStart:" + a.mLeash);
                        com.android.server.wm.SurfaceAnimationRunner.this.mFrameTransaction.setAlpha(a.mLeash, 1.0f);
                    }
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(android.animation.Animator animation) {
                synchronized (com.android.server.wm.SurfaceAnimationRunner.this.mLock) {
                    if (!com.android.server.wm.SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.hookonAnimationEndRemove(true, a.mLeash)) {
                        com.android.server.wm.SurfaceAnimationRunner.this.mRunningAnimations.remove(a.mLeash);
                    }
                    synchronized (com.android.server.wm.SurfaceAnimationRunner.this.mCancelLock) {
                        if (!a.mCancelled) {
                            com.android.server.wm.SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.hookonAnimationEndRemove(false, a.mLeash);
                            com.android.server.wm.SurfaceAnimationRunner.this.mAnimationThreadHandler.post(a.mFinishCallback);
                            com.android.server.wm.SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.onWindowAnimationEnded(a.mLeash.hashCode());
                            com.android.server.wm.SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.onAnimationEnd(a.mAnimSpec, com.android.server.wm.SurfaceAnimationRunner.this.mChoreographer);
                            com.android.server.wm.SurfaceAnimationRunner.this.mSurfaceAnimationRunnerExt.callGcDesupression(2);
                        }
                    }
                    android.util.Slog.d(com.android.server.wm.SurfaceAnimationRunner.TAG, "onAnimationEnd:" + a.mLeash);
                }
            }
        });
        a.mAnim = anim;
        this.mRunningAnimations.put(a.mLeash, a);
        anim.start();
        if (a.mAnimSpec.canSkipFirstFrame()) {
            anim.setCurrentPlayTime(this.mChoreographer.getFrameIntervalNanos() / 1000000);
        }
        anim.doAnimationFrame(this.mChoreographer.getFrameTime());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimationLocked$4(com.android.server.wm.SurfaceAnimationRunner.RunningAnimation a, android.animation.ValueAnimator anim, android.animation.ValueAnimator animation) {
        synchronized (this.mCancelLock) {
            if (!a.mCancelled) {
                long duration = anim.getDuration();
                long currentPlayTime = anim.getCurrentPlayTime();
                if (currentPlayTime > duration) {
                    currentPlayTime = duration;
                }
                applyTransformation(a, this.mFrameTransaction, currentPlayTime);
                this.mSurfaceAnimationRunnerExt.recordCurrentAnimationPoints(currentPlayTime);
            }
        }
        scheduleApplyTransaction();
    }

    private void applyTransformation(com.android.server.wm.SurfaceAnimationRunner.RunningAnimation a, android.view.SurfaceControl.Transaction t, long currentPlayTime) {
        if (a != null && a.mLeash != null && a.mLeash.isValid()) {
            a.mAnimSpec.apply(t, a.mLeash, currentPlayTime);
        } else {
            android.util.Slog.e(TAG, "applyTransformation failed, RunningAnimation = " + a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimations(long frameTimeNanos) {
        synchronized (this.mLock) {
            if (!this.mPreProcessingAnimations.isEmpty()) {
                android.util.Slog.d(TAG, "startAnimations mPreProcessingAnimations is not empty");
            } else {
                startPendingAnimationsLocked();
                this.mPowerManagerInternal.setPowerBoost(0, 0);
            }
        }
    }

    private void scheduleApplyTransaction() {
        if (!this.mApplyScheduled) {
            this.mChoreographer.postCallback(3, this.mApplyTransactionRunnable, null);
            this.mApplyScheduled = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyTransaction() {
        this.mFrameTransaction.setAnimationTransaction();
        this.mFrameTransaction.setFrameTimelineVsync(this.mChoreographer.getVsyncId());
        this.mFrameTransaction.apply();
        this.mApplyScheduled = false;
    }

    private void edgeExtendWindow(android.view.SurfaceControl leash, android.graphics.Rect bounds, android.view.animation.Animation a, android.view.SurfaceControl.Transaction transaction) throws java.lang.Throwable {
        android.view.animation.Transformation transformationAtStart = new android.view.animation.Transformation();
        a.getTransformationAt(0.0f, transformationAtStart);
        android.view.animation.Transformation transformationAtEnd = new android.view.animation.Transformation();
        a.getTransformationAt(1.0f, transformationAtEnd);
        android.graphics.Insets maxExtensionInsets = android.graphics.Insets.min(transformationAtStart.getInsets(), transformationAtEnd.getInsets());
        int targetSurfaceHeight = bounds.height();
        int targetSurfaceWidth = bounds.width();
        if (maxExtensionInsets.left < 0) {
            android.graphics.Rect edgeBounds = new android.graphics.Rect(bounds.left, bounds.top, bounds.left + 1, bounds.bottom);
            android.graphics.Rect extensionRect = new android.graphics.Rect(0, 0, -maxExtensionInsets.left, targetSurfaceHeight);
            int xPos = bounds.left + maxExtensionInsets.left;
            int yPos = bounds.top;
            createExtensionSurface(leash, edgeBounds, extensionRect, xPos, yPos, "Left Edge Extension", transaction);
        }
        int xPos2 = maxExtensionInsets.top;
        if (xPos2 < 0) {
            android.graphics.Rect edgeBounds2 = new android.graphics.Rect(bounds.left, bounds.top, targetSurfaceWidth, bounds.top + 1);
            android.graphics.Rect extensionRect2 = new android.graphics.Rect(0, 0, targetSurfaceWidth, -maxExtensionInsets.top);
            int xPos3 = bounds.left;
            int yPos2 = bounds.top + maxExtensionInsets.top;
            createExtensionSurface(leash, edgeBounds2, extensionRect2, xPos3, yPos2, "Top Edge Extension", transaction);
        }
        int xPos4 = maxExtensionInsets.right;
        if (xPos4 < 0) {
            android.graphics.Rect edgeBounds3 = new android.graphics.Rect(bounds.right - 1, bounds.top, bounds.right, bounds.bottom);
            android.graphics.Rect extensionRect3 = new android.graphics.Rect(0, 0, -maxExtensionInsets.right, targetSurfaceHeight);
            int xPos5 = bounds.right;
            int yPos3 = bounds.top;
            createExtensionSurface(leash, edgeBounds3, extensionRect3, xPos5, yPos3, "Right Edge Extension", transaction);
        }
        int xPos6 = maxExtensionInsets.bottom;
        if (xPos6 < 0) {
            android.graphics.Rect edgeBounds4 = new android.graphics.Rect(bounds.left, bounds.bottom - 1, bounds.right, bounds.bottom);
            android.graphics.Rect extensionRect4 = new android.graphics.Rect(0, 0, targetSurfaceWidth, -maxExtensionInsets.bottom);
            int xPos7 = bounds.left;
            int yPos4 = bounds.bottom;
            createExtensionSurface(leash, edgeBounds4, extensionRect4, xPos7, yPos4, "Bottom Edge Extension", transaction);
        }
    }

    private void createExtensionSurface(android.view.SurfaceControl leash, android.graphics.Rect edgeBounds, android.graphics.Rect extensionRect, int xPos, int yPos, java.lang.String layerName, android.view.SurfaceControl.Transaction startTransaction) throws java.lang.Throwable {
        android.os.Trace.traceBegin(32L, "createExtensionSurface");
        doCreateExtensionSurface(leash, edgeBounds, extensionRect, xPos, yPos, layerName, startTransaction);
        android.os.Trace.traceEnd(32L);
    }

    private void doCreateExtensionSurface(android.view.SurfaceControl leash, android.graphics.Rect edgeBounds, android.graphics.Rect extensionRect, int xPos, int yPos, java.lang.String layerName, android.view.SurfaceControl.Transaction startTransaction) throws java.lang.Throwable {
        android.window.ScreenCapture.LayerCaptureArgs captureArgs = new android.window.ScreenCapture.LayerCaptureArgs.Builder(leash).setSourceCrop(edgeBounds).setFrameScale(1.0f).setPixelFormat(1).setChildrenOnly(true).setAllowProtected(true).setCaptureSecureLayers(true).build();
        android.window.ScreenCapture.ScreenshotHardwareBuffer edgeBuffer = android.window.ScreenCapture.captureLayers(captureArgs);
        if (edgeBuffer == null) {
            android.util.Log.e(TAG, "Failed to create edge extension - edge buffer is null");
            return;
        }
        android.view.SurfaceControl edgeExtensionLayer = new android.view.SurfaceControl.Builder().setName(layerName).setHidden(true).setCallsite("DefaultTransitionHandler#startAnimation").setOpaque(true).setBufferSize(extensionRect.width(), extensionRect.height()).build();
        android.graphics.BitmapShader shader = new android.graphics.BitmapShader(edgeBuffer.asBitmap(), android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setShader(shader);
        android.view.Surface surface = new android.view.Surface(edgeExtensionLayer);
        android.graphics.Canvas c = surface.lockHardwareCanvas();
        c.drawRect(extensionRect, paint);
        surface.unlockCanvasAndPost(c);
        surface.release();
        synchronized (this.mEdgeExtensionLock) {
            try {
                try {
                    if (!this.mEdgeExtensions.containsKey(leash)) {
                        startTransaction.remove(edgeExtensionLayer);
                        return;
                    }
                    startTransaction.reparent(edgeExtensionLayer, leash);
                    startTransaction.setLayer(edgeExtensionLayer, Integer.MIN_VALUE);
                    startTransaction.setPosition(edgeExtensionLayer, xPos, yPos);
                    startTransaction.setVisibility(edgeExtensionLayer, true);
                    this.mEdgeExtensions.get(leash).add(edgeExtensionLayer);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    private float getScaleXForExtensionSurface(android.graphics.Rect edgeBounds, android.graphics.Rect extensionRect) {
        if (edgeBounds.width() == extensionRect.width()) {
            return 1.0f;
        }
        if (edgeBounds.width() == 1) {
            return extensionRect.width();
        }
        throw new java.lang.RuntimeException("Unexpected edgeBounds and extensionRect widths");
    }

    private float getScaleYForExtensionSurface(android.graphics.Rect edgeBounds, android.graphics.Rect extensionRect) {
        if (edgeBounds.height() == extensionRect.height()) {
            return 1.0f;
        }
        if (edgeBounds.height() == 1) {
            return extensionRect.height();
        }
        throw new java.lang.RuntimeException("Unexpected edgeBounds and extensionRect heights");
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class RunningAnimation {
        android.animation.ValueAnimator mAnim;
        final com.android.server.wm.LocalAnimationAdapter.AnimationSpec mAnimSpec;
        private boolean mCancelled;
        final java.lang.Runnable mFinishCallback;
        final android.view.SurfaceControl mLeash;

        RunningAnimation(com.android.server.wm.LocalAnimationAdapter.AnimationSpec animSpec, android.view.SurfaceControl leash, java.lang.Runnable finishCallback) {
            this.mAnimSpec = animSpec;
            this.mLeash = leash;
            this.mFinishCallback = finishCallback;
        }
    }

    protected void onAnimationLeashLost(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t) {
        synchronized (this.mEdgeExtensionLock) {
            if (this.mEdgeExtensions.containsKey(animationLeash)) {
                java.util.ArrayList<android.view.SurfaceControl> edgeExtensions = this.mEdgeExtensions.get(animationLeash);
                for (int i = 0; i < edgeExtensions.size(); i++) {
                    android.view.SurfaceControl extension = edgeExtensions.get(i);
                    t.remove(extension);
                }
                this.mEdgeExtensions.remove(animationLeash);
            }
        }
    }

    private class SfValueAnimator extends android.animation.ValueAnimator {
        SfValueAnimator() {
            setFloatValues(0.0f, 1.0f);
        }

        public android.animation.AnimationHandler getAnimationHandler() {
            return com.android.server.wm.SurfaceAnimationRunner.this.mAnimationHandler;
        }
    }
}
