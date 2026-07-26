package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class AsyncRotationController extends com.android.server.wm.FadeAnimationController implements java.util.function.Consumer<com.android.server.wm.WindowState> {
    private static final int DEBUG_DEPTH = 8;
    private static final int OP_APP_SWITCH = 1;
    private static final int OP_CHANGE = 2;
    private static final int OP_CHANGE_MAY_SEAMLESS = 3;
    private static final int OP_LEGACY = 0;
    private static final java.lang.String TAG = "AsyncRotation";
    private com.android.server.wm.AsyncRotationController.AsyncRotationControllerWrapper mARCWrapper;
    private com.android.server.wm.IAsyncRotationControllerSocExt mAsyncRotationControllerSocExt;
    private com.android.server.wm.IFadeRotationAnimationControllerExt mExt;
    private final boolean mHasScreenRotationAnimation;
    private boolean mHideImmediately;
    private boolean mIsStartTransactionCommitted;
    private boolean mIsStartTransactionPrepared;
    private boolean mIsSyncDrawRequested;
    private com.android.server.wm.WindowToken mNavBarToken;
    private java.lang.Runnable mOnShowRunnable;
    private int mOriginalRotation;
    private com.android.server.wm.SeamlessRotator mRotator;
    private final com.android.server.wm.WindowManagerService mService;
    private final android.util.ArrayMap<com.android.server.wm.WindowToken, com.android.server.wm.AsyncRotationController.Operation> mTargetWindowTokens;
    private java.lang.Runnable mTimeoutRunnable;
    private final int mTransitionOp;
    private static boolean DEBUG_PANIC = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final boolean DEBUG = DEBUG_PANIC;
    private static com.android.server.wm.IAsyncRotationControllerExt sAsyncRotationControllerExt = (com.android.server.wm.IAsyncRotationControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IAsyncRotationControllerExt.class).create();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface TransitionOp {
    }

    AsyncRotationController(com.android.server.wm.DisplayContent displayContent) {
        super(displayContent);
        this.mTargetWindowTokens = new android.util.ArrayMap<>();
        this.mAsyncRotationControllerSocExt = (com.android.server.wm.IAsyncRotationControllerSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IAsyncRotationControllerSocExt.class).base(this).create();
        this.mARCWrapper = new com.android.server.wm.AsyncRotationController.AsyncRotationControllerWrapper();
        this.mExt = (com.android.server.wm.IFadeRotationAnimationControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IFadeRotationAnimationControllerExt.class).base(this).create();
        this.mService = displayContent.mWmService;
        this.mOriginalRotation = displayContent.getWindowConfiguration().getRotation();
        this.mAsyncRotationControllerSocExt.hookInitPerf();
        int transitionType = displayContent.mTransitionController.getCollectingTransitionType();
        if (transitionType == 6) {
            com.android.server.wm.DisplayRotation dr = displayContent.getDisplayRotation();
            com.android.server.wm.WindowState w = displayContent.getDisplayPolicy().getTopFullscreenOpaqueWindow();
            if (w != null && w.mAttrs.rotationAnimation == 3 && w.getTask() != null && dr.canRotateSeamlessly(this.mOriginalRotation, dr.getRotation())) {
                this.mTransitionOp = 3;
            } else {
                this.mTransitionOp = 2;
            }
        } else if (displayContent.mTransitionController.isShellTransitionsEnabled()) {
            this.mTransitionOp = 1;
        } else {
            this.mTransitionOp = 0;
        }
        this.mHasScreenRotationAnimation = displayContent.getRotationAnimation() != null || this.mTransitionOp == 2;
        if (this.mHasScreenRotationAnimation) {
            this.mHideImmediately = true;
        }
        displayContent.forAllWindows((java.util.function.Consumer<com.android.server.wm.WindowState>) this, true);
        if (this.mTransitionOp == 0) {
            this.mIsStartTransactionCommitted = true;
        } else if (displayContent.mTransitionController.isCollecting(displayContent)) {
            keepAppearanceInPreviousRotation();
        }
    }

    @Override // java.util.function.Consumer
    public void accept(com.android.server.wm.WindowState w) {
        if (!w.mHasSurface || !canBeAsync(w.mToken)) {
            return;
        }
        if (this.mTransitionOp == 0 && w.mForceSeamlesslyRotate) {
            return;
        }
        if (!this.mExt.allowFadeRotationAnimation(w)) {
            android.util.Slog.d(TAG, w + " not allowFadeRotationAnimation");
            return;
        }
        if (w.mAttrs.type == 2019) {
            int action = 2;
            boolean navigationBarCanMove = this.mDisplayContent.getDisplayPolicy().navigationBarCanMove();
            if (this.mTransitionOp == 0) {
                this.mNavBarToken = w.mToken;
                if (navigationBarCanMove) {
                    return;
                }
                com.android.server.wm.RecentsAnimationController recents = this.mService.getRecentsAnimationController();
                if (recents != null && recents.isNavigationBarAttachedToApp()) {
                    return;
                }
            } else if (navigationBarCanMove || this.mTransitionOp == 3 || this.mDisplayContent.mTransitionController.mNavigationBarAttachedToApp) {
                action = 1;
            }
            this.mTargetWindowTokens.put(w.mToken, new com.android.server.wm.AsyncRotationController.Operation(action));
            return;
        }
        int action2 = this.mTransitionOp;
        int action3 = (action2 == 3 || w.mForceSeamlesslyRotate) ? 1 : 2;
        this.mTargetWindowTokens.put(w.mToken, new com.android.server.wm.AsyncRotationController.Operation(action3));
    }

    static boolean canBeAsync(com.android.server.wm.WindowToken token) {
        int type = token.windowType;
        return (type <= 99 || type == 2011 || type == 2013 || type == 2040 || !sAsyncRotationControllerExt.canBeAsync(token)) ? false : true;
    }

    void keepAppearanceInPreviousRotation() {
        if (this.mIsSyncDrawRequested) {
            return;
        }
        for (int i = this.mTargetWindowTokens.size() - 1; i >= 0; i--) {
            if (!canDrawBeforeStartTransaction(this.mTargetWindowTokens.valueAt(i))) {
                com.android.server.wm.WindowToken token = this.mTargetWindowTokens.keyAt(i);
                for (int j = token.getChildCount() - 1; j >= 0; j--) {
                    ((com.android.server.wm.WindowState) token.getChildAt(j)).applyWithNextDraw(new java.util.function.Consumer() { // from class: com.android.server.wm.AsyncRotationController$$ExternalSyntheticLambda3
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            com.android.server.wm.AsyncRotationController.lambda$keepAppearanceInPreviousRotation$0((android.view.SurfaceControl.Transaction) obj);
                        }
                    });
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Sync draw for " + token.getChildAt(j));
                    }
                }
            }
        }
        this.mIsSyncDrawRequested = true;
        if (DEBUG) {
            android.util.Slog.d(TAG, "Requested to sync draw transaction");
        }
        scheduleTimeout();
    }

    static /* synthetic */ void lambda$keepAppearanceInPreviousRotation$0(android.view.SurfaceControl.Transaction t) {
    }

    void updateTargetWindows() {
        if (this.mTransitionOp == 0) {
            return;
        }
        if (!this.mIsStartTransactionCommitted) {
            if ((this.mTimeoutRunnable == null || !this.mIsStartTransactionPrepared) && !this.mDisplayContent.hasTopFixedRotationLaunchingApp() && !this.mDisplayContent.isRotationChanging() && !this.mDisplayContent.inTransition()) {
                android.util.Slog.d(TAG, "Cancel for no change");
                this.mDisplayContent.finishAsyncRotationIfPossible();
                return;
            }
            return;
        }
        for (int i = this.mTargetWindowTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.AsyncRotationController.Operation op = this.mTargetWindowTokens.valueAt(i);
            if (!op.mIsCompletionPending && op.mAction != 1) {
                com.android.server.wm.WindowToken token = this.mTargetWindowTokens.keyAt(i);
                int readyCount = 0;
                int childCount = token.getChildCount();
                for (int j = childCount - 1; j >= 0; j--) {
                    com.android.server.wm.WindowState w = (com.android.server.wm.WindowState) token.getChildAt(j);
                    if (w.isDrawn() || !w.mWinAnimator.getShown()) {
                        readyCount++;
                    }
                }
                if (readyCount == childCount) {
                    this.mDisplayContent.finishAsyncRotation(token);
                }
            }
        }
    }

    private void finishOp(com.android.server.wm.WindowToken windowToken) {
        com.android.server.wm.AsyncRotationController.Operation op = this.mTargetWindowTokens.remove(windowToken);
        if (op == null) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, " finishOp action=" + op.mAction + ", leash=" + op.mLeash + ", windowToken=" + windowToken + ", this=" + this + ", call by=" + android.os.Debug.getCallers(8));
        }
        if (op.mDrawTransaction != null) {
            windowToken.getSyncTransaction().merge(op.mDrawTransaction);
            op.mDrawTransaction = null;
            if (DEBUG) {
                android.util.Slog.d(TAG, "finishOp merge transaction " + windowToken.getTopChild());
            }
        }
        if (op.mAction == 3) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "finishOp fade-in IME " + windowToken.getTopChild());
            }
            fadeWindowToken(true, windowToken, 64, new com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.AsyncRotationController$$ExternalSyntheticLambda0
                @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
                public final void onAnimationFinished(int i, com.android.server.wm.AnimationAdapter animationAdapter) {
                    this.f$0.lambda$finishOp$1(i, animationAdapter);
                }
            });
        } else if (op.mAction == 2) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "finishOp fade-in " + windowToken.getTopChild());
            }
            fadeWindowToken(true, windowToken, 64);
        } else if (op.isValidSeamless()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "finishOp undo seamless " + windowToken.getTopChild());
            }
            android.view.SurfaceControl.Transaction t = windowToken.getSyncTransaction();
            clearTransform(t, op.mLeash);
        }
        if (isSeamlessTransition()) {
            for (int i = windowToken.getChildCount() - 1; i >= 0; i--) {
                com.android.server.wm.WindowState w = (com.android.server.wm.WindowState) windowToken.getChildAt(i);
                com.android.server.wm.InsetsSourceProvider insetsProvider = w.getControllableInsetProvider();
                if (insetsProvider != null) {
                    insetsProvider.updateInsetsControlPosition(w);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishOp$1(int type, com.android.server.wm.AnimationAdapter anim) {
        this.mDisplayContent.getInsetsStateController().getImeSourceProvider().reportImeDrawnForOrganizer();
    }

    private static void clearTransform(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl sc) {
        t.setMatrix(sc, 1.0f, 0.0f, 0.0f, 1.0f);
        t.setPosition(sc, 0.0f, 0.0f);
    }

    void completeAll() {
        for (int i = this.mTargetWindowTokens.size() - 1; i >= 0; i--) {
            finishOp(this.mTargetWindowTokens.keyAt(i));
        }
        this.mTargetWindowTokens.clear();
        onAllCompleted();
    }

    private void onAllCompleted() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onAllCompleted");
        }
        if (this.mTimeoutRunnable != null) {
            this.mService.mH.removeCallbacks(this.mTimeoutRunnable);
        }
        if (this.mOnShowRunnable != null) {
            this.mOnShowRunnable.run();
            this.mOnShowRunnable = null;
        }
    }

    boolean completeRotation(com.android.server.wm.WindowToken token) {
        com.android.server.wm.AsyncRotationController.Operation op;
        if (!this.mIsStartTransactionCommitted) {
            com.android.server.wm.AsyncRotationController.Operation op2 = this.mTargetWindowTokens.get(token);
            if (op2 != null) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Complete set pending " + token.getTopChild());
                }
                op2.mIsCompletionPending = true;
            }
            return false;
        }
        if (this.mTransitionOp == 1 && token.mTransitionController.inTransition() && (op = this.mTargetWindowTokens.get(token)) != null && op.mAction == 2) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Defer completion " + token.getTopChild());
            }
            return false;
        }
        if (!isTargetToken(token)) {
            return false;
        }
        if (this.mHasScreenRotationAnimation || this.mTransitionOp != 0) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Complete directly " + token.getTopChild());
            }
            finishOp(token);
            if (this.mTargetWindowTokens.isEmpty()) {
                this.mAsyncRotationControllerSocExt.hookPerfLockRelease();
                onAllCompleted();
                return true;
            }
        }
        return false;
    }

    void start() {
        this.mAsyncRotationControllerSocExt.hookPerfHint();
        for (int i = this.mTargetWindowTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowToken windowToken = this.mTargetWindowTokens.keyAt(i);
            com.android.server.wm.AsyncRotationController.Operation op = this.mTargetWindowTokens.valueAt(i);
            if (op.mAction == 2 || op.mAction == 3) {
                fadeWindowToken(false, windowToken, 64);
                op.mLeash = windowToken.getAnimationLeash();
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Start fade-out " + windowToken.getTopChild());
                }
            } else if (op.mAction == 1) {
                op.mLeash = windowToken.mSurfaceControl;
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Start seamless " + windowToken.getTopChild());
                }
            }
        }
        if (this.mHasScreenRotationAnimation) {
            scheduleTimeout();
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, " start mTransitionOp=" + this.mTransitionOp + ", targetWindowTokens=" + this.mTargetWindowTokens + ", this=" + this + ", call by=" + android.os.Debug.getCallers(8));
        }
    }

    void updateRotation() {
        int currentRotation;
        if (this.mRotator == null || this.mOriginalRotation == (currentRotation = this.mDisplayContent.getWindowConfiguration().getRotation())) {
            return;
        }
        android.util.Slog.d(TAG, "Update original rotation " + currentRotation);
        this.mOriginalRotation = currentRotation;
        this.mDisplayContent.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.AsyncRotationController$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$updateRotation$2((com.android.server.wm.WindowState) obj);
            }
        }, true);
        this.mRotator = null;
        this.mIsStartTransactionCommitted = false;
        this.mIsSyncDrawRequested = false;
        this.mService.mH.removeCallbacks(this.mTimeoutRunnable);
        this.mIsStartTransactionPrepared = false;
        keepAppearanceInPreviousRotation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRotation$2(com.android.server.wm.WindowState w) {
        if (w.mForceSeamlesslyRotate && w.mHasSurface && !this.mTargetWindowTokens.containsKey(w.mToken)) {
            com.android.server.wm.AsyncRotationController.Operation op = new com.android.server.wm.AsyncRotationController.Operation(1);
            op.mLeash = w.mToken.mSurfaceControl;
            this.mTargetWindowTokens.put(w.mToken, op);
        }
    }

    private void scheduleTimeout() {
        if (this.mTimeoutRunnable != null && this.mService.mH.hasCallbacks(this.mTimeoutRunnable)) {
            android.util.Slog.d(TAG, " don't scheduleTimeout again ,this=" + this);
            return;
        }
        if (this.mTimeoutRunnable == null) {
            this.mTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.AsyncRotationController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleTimeout$3();
                }
            };
        }
        this.mService.mH.postDelayed(this.mTimeoutRunnable, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleTimeout$3() {
        java.lang.String reason;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!this.mIsStartTransactionCommitted) {
                    if (!this.mIsStartTransactionPrepared) {
                        reason = "setupStartTransaction is not called";
                    } else {
                        reason = "start transaction is not committed";
                    }
                } else {
                    reason = "unfinished windows " + this.mTargetWindowTokens;
                }
                android.util.Slog.i(TAG, "Async rotation timeout: " + reason);
                if (!this.mIsStartTransactionCommitted && this.mIsStartTransactionPrepared) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                this.mDisplayContent.finishAsyncRotationIfPossible();
                this.mService.mWindowPlacerLocked.performSurfacePlacement();
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void hideImeImmediately() {
        if (this.mDisplayContent.mInputMethodWindow == null) {
            return;
        }
        com.android.server.wm.WindowToken imeWindowToken = this.mDisplayContent.mInputMethodWindow.mToken;
        if (isTargetToken(imeWindowToken)) {
            return;
        }
        hideImmediately(imeWindowToken, 3);
        if (DEBUG) {
            android.util.Slog.d(TAG, "hideImeImmediately " + imeWindowToken.getTopChild());
        }
    }

    private void hideImmediately(com.android.server.wm.WindowToken token, int action) {
        boolean original = this.mHideImmediately;
        this.mHideImmediately = true;
        com.android.server.wm.AsyncRotationController.Operation op = new com.android.server.wm.AsyncRotationController.Operation(action);
        this.mTargetWindowTokens.put(token, op);
        fadeWindowToken(false, token, 64);
        op.mLeash = token.getAnimationLeash();
        this.mHideImmediately = original;
    }

    boolean isAsync(com.android.server.wm.WindowState w) {
        return w.mToken == this.mNavBarToken || (w.mForceSeamlesslyRotate && this.mTransitionOp == 0) || isTargetToken(w.mToken);
    }

    boolean isTargetToken(com.android.server.wm.WindowToken token) {
        return this.mTargetWindowTokens.containsKey(token);
    }

    boolean hasFadeOperation(com.android.server.wm.WindowToken token) {
        com.android.server.wm.AsyncRotationController.Operation op = this.mTargetWindowTokens.get(token);
        return op != null && op.mAction == 2;
    }

    boolean hasSeamlessOperation(com.android.server.wm.WindowToken token) {
        com.android.server.wm.AsyncRotationController.Operation op = this.mTargetWindowTokens.get(token);
        return op != null && op.mAction == 1;
    }

    boolean shouldFreezeInsetsPosition(com.android.server.wm.WindowState w) {
        return this.mTransitionOp != 0 && (isSeamlessTransition() || com.android.server.wm.TransitionController.SYNC_METHOD == 1) && canBeAsync(w.mToken) && isTargetToken(w.mToken);
    }

    private boolean isSeamlessTransition() {
        return this.mTransitionOp == 1 || this.mTransitionOp == 3;
    }

    android.view.SurfaceControl.Transaction getDrawTransaction(com.android.server.wm.WindowToken token) {
        com.android.server.wm.AsyncRotationController.Operation op;
        if (this.mTransitionOp == 0 || (op = this.mTargetWindowTokens.get(token)) == null) {
            return null;
        }
        if (op.mDrawTransaction == null) {
            op.mDrawTransaction = new android.view.SurfaceControl.Transaction();
        }
        return op.mDrawTransaction;
    }

    void setOnShowRunnable(java.lang.Runnable onShowRunnable) {
        this.mOnShowRunnable = onShowRunnable;
    }

    void setupStartTransaction(android.view.SurfaceControl.Transaction t) {
        if (this.mIsStartTransactionCommitted) {
            return;
        }
        for (int i = this.mTargetWindowTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.AsyncRotationController.Operation op = this.mTargetWindowTokens.valueAt(i);
            android.view.SurfaceControl leash = op.mLeash;
            if (leash != null && leash.isValid()) {
                if (this.mHasScreenRotationAnimation && op.mAction == 2) {
                    t.setAlpha(leash, 0.0f);
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Setup alpha0 " + this.mTargetWindowTokens.keyAt(i).getTopChild());
                    }
                } else {
                    if (this.mRotator == null) {
                        this.mRotator = new com.android.server.wm.SeamlessRotator(this.mOriginalRotation, this.mDisplayContent.getWindowConfiguration().getRotation(), this.mDisplayContent.getDisplayInfo(), false);
                    }
                    this.mRotator.applyTransform(t, leash);
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Setup unrotate " + this.mTargetWindowTokens.keyAt(i).getTopChild());
                    }
                }
            }
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, " setupStartTransaction " + this + ", targetWindowTokens=" + this.mTargetWindowTokens + ", call by=" + android.os.Debug.getCallers(8));
        }
        t.addTransactionCommittedListener(new android.os.HandlerExecutor(this.mService.mH), new android.view.SurfaceControl.TransactionCommittedListener() { // from class: com.android.server.wm.AsyncRotationController$$ExternalSyntheticLambda4
            @Override // android.view.SurfaceControl.TransactionCommittedListener
            public final void onTransactionCommitted() {
                this.f$0.lambda$setupStartTransaction$4();
            }
        });
        this.mIsStartTransactionPrepared = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupStartTransaction$4() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Start transaction is committed");
                }
                this.mIsStartTransactionCommitted = true;
                for (int i = this.mTargetWindowTokens.size() - 1; i >= 0; i--) {
                    if (this.mTargetWindowTokens.valueAt(i).mIsCompletionPending) {
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "Continue pending completion " + this.mTargetWindowTokens.keyAt(i).getTopChild());
                        }
                        this.mDisplayContent.finishAsyncRotation(this.mTargetWindowTokens.keyAt(i));
                    }
                }
                if (!this.mTargetWindowTokens.isEmpty()) {
                    scheduleTimeout();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void onTransactionCommitTimeout(android.view.SurfaceControl.Transaction t) {
        if (this.mIsStartTransactionCommitted) {
            return;
        }
        for (int i = this.mTargetWindowTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.AsyncRotationController.Operation op = this.mTargetWindowTokens.valueAt(i);
            op.mIsCompletionPending = true;
            if (op.isValidSeamless()) {
                android.util.Slog.d(TAG, "Transaction timeout. Clear transform for " + this.mTargetWindowTokens.keyAt(i).getTopChild());
                clearTransform(t, op.mLeash);
            }
        }
    }

    void onTransitionFinished() {
        if (this.mTransitionOp == 2) {
            if (this.mTargetWindowTokens.isEmpty()) {
                this.mDisplayContent.finishAsyncRotationIfPossible();
                return;
            }
            return;
        }
        if (DEBUG || DEBUG_PANIC) {
            android.util.Slog.d(TAG, "onTransitionFinished " + this.mTargetWindowTokens);
        }
        for (int i = this.mTargetWindowTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowToken token = this.mTargetWindowTokens.keyAt(i);
            if (token.isVisible()) {
                int j = token.getChildCount() - 1;
                while (true) {
                    if (j < 0) {
                        break;
                    }
                    if (((com.android.server.wm.WindowState) token.getChildAt(j)).isDrawFinishedLw()) {
                        this.mDisplayContent.finishAsyncRotation(token);
                        break;
                    }
                    j--;
                }
            } else {
                this.mDisplayContent.finishAsyncRotation(token);
            }
        }
        if (!this.mTargetWindowTokens.isEmpty()) {
            scheduleTimeout();
        }
    }

    boolean handleFinishDrawing(com.android.server.wm.WindowState w, android.view.SurfaceControl.Transaction postDrawTransaction) {
        if (this.mTransitionOp == 0) {
            return false;
        }
        com.android.server.wm.AsyncRotationController.Operation op = this.mTargetWindowTokens.get(w.mToken);
        if (op == null) {
            if (this.mTransitionOp == 1 && !this.mIsStartTransactionCommitted && canBeAsync(w.mToken) && !this.mDisplayContent.hasFixedRotationTransientLaunch() && sAsyncRotationControllerExt.canBeHide(w.mToken)) {
                hideImmediately(w.mToken, 2);
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Hide on finishDrawing " + w.mToken.getTopChild());
                }
            }
            return false;
        }
        if (DEBUG || DEBUG_PANIC) {
            android.util.Slog.d(TAG, "handleFinishDrawing " + w + ", postDrawTransaction=" + postDrawTransaction + ", targetWindowTokens=" + this.mTargetWindowTokens + ", call by=" + android.os.Debug.getCallers(8));
        }
        if (postDrawTransaction == null || !this.mIsSyncDrawRequested || canDrawBeforeStartTransaction(op)) {
            this.mDisplayContent.finishAsyncRotation(w.mToken);
            return false;
        }
        if (op.mDrawTransaction == null) {
            if (w.isClientLocal()) {
                op.mDrawTransaction = this.mService.mTransactionFactory.get();
                op.mDrawTransaction.merge(postDrawTransaction);
            } else {
                op.mDrawTransaction = postDrawTransaction;
            }
        } else {
            op.mDrawTransaction.merge(postDrawTransaction);
        }
        this.mDisplayContent.finishAsyncRotation(w.mToken);
        return true;
    }

    @Override // com.android.server.wm.FadeAnimationController
    public android.view.animation.Animation getFadeInAnimation() {
        if (this.mHasScreenRotationAnimation) {
            return android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.recents_fade_in);
        }
        return super.getFadeInAnimation();
    }

    @Override // com.android.server.wm.FadeAnimationController
    public android.view.animation.Animation getFadeOutAnimation() {
        if (this.mHideImmediately) {
            float alpha = this.mTransitionOp == 2 ? 1.0f : 0.0f;
            return new android.view.animation.AlphaAnimation(alpha, alpha);
        }
        return super.getFadeOutAnimation();
    }

    private boolean canDrawBeforeStartTransaction(com.android.server.wm.AsyncRotationController.Operation op) {
        return op.mAction != 1;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "AsyncRotationController");
        java.lang.String prefix2 = prefix + "  ";
        pw.println(prefix2 + "mTransitionOp=" + this.mTransitionOp);
        pw.println(prefix2 + "mIsStartTransactionCommitted=" + this.mIsStartTransactionCommitted);
        pw.println(prefix2 + "mIsSyncDrawRequested=" + this.mIsSyncDrawRequested);
        pw.println(prefix2 + "mOriginalRotation=" + this.mOriginalRotation);
        pw.println(prefix2 + "mTargetWindowTokens=" + this.mTargetWindowTokens);
    }

    private static class Operation {
        static final int ACTION_FADE = 2;
        static final int ACTION_SEAMLESS = 1;
        static final int ACTION_TOGGLE_IME = 3;
        final int mAction;
        android.view.SurfaceControl.Transaction mDrawTransaction;
        boolean mIsCompletionPending;
        android.view.SurfaceControl mLeash;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @interface Action {
        }

        Operation(int action) {
            this.mAction = action;
        }

        boolean isValidSeamless() {
            return this.mAction == 1 && this.mLeash != null && this.mLeash.isValid();
        }

        public java.lang.String toString() {
            return "Operation{a=" + this.mAction + " pending=" + this.mIsCompletionPending + '}';
        }
    }

    public com.android.server.wm.IAsyncRotationControllerWrapper getWrapper() {
        return this.mARCWrapper;
    }

    private class AsyncRotationControllerWrapper implements com.android.server.wm.IAsyncRotationControllerWrapper {
        private AsyncRotationControllerWrapper() {
        }

        @Override // com.android.server.wm.IAsyncRotationControllerWrapper
        public java.util.Set<com.android.server.wm.WindowToken> getTargetWindowTokens() {
            return com.android.server.wm.AsyncRotationController.this.mTargetWindowTokens.keySet();
        }

        @Override // com.android.server.wm.IAsyncRotationControllerWrapper
        public java.lang.String getAsyncRotationInfo() {
            java.lang.StringBuilder builer = new java.lang.StringBuilder();
            builer.append("AsyncRotationController{");
            builer.append(",mTransitionOp =" + com.android.server.wm.AsyncRotationController.this.mTransitionOp);
            builer.append(",mIsStartTransactionCommitted =" + com.android.server.wm.AsyncRotationController.this.mIsStartTransactionCommitted);
            builer.append(",mIsSyncDrawRequested =" + com.android.server.wm.AsyncRotationController.this.mIsSyncDrawRequested);
            builer.append(",mHasScreenRotationAnimation =" + com.android.server.wm.AsyncRotationController.this.mHasScreenRotationAnimation);
            builer.append(",mTargetWindowTokens size =" + com.android.server.wm.AsyncRotationController.this.mTargetWindowTokens.size());
            builer.append(",mTargetWindowTokens=" + com.android.server.wm.AsyncRotationController.this.mTargetWindowTokens);
            builer.append("}");
            return builer.toString();
        }

        @Override // com.android.server.wm.IAsyncRotationControllerWrapper
        public void forceRemoveOp(com.android.server.wm.WindowToken windowToken) {
            com.android.server.wm.AsyncRotationController.Operation op = (com.android.server.wm.AsyncRotationController.Operation) com.android.server.wm.AsyncRotationController.this.mTargetWindowTokens.remove(windowToken);
            if (op != null && op.mDrawTransaction != null) {
                com.android.server.wm.AsyncRotationController.this.mDisplayContent.getPendingTransaction().merge(op.mDrawTransaction);
                op.mDrawTransaction = null;
                android.util.Slog.d(com.android.server.wm.AsyncRotationController.TAG, "forceRemoveOp merge transaction " + windowToken.getTopChild());
            }
        }

        @Override // com.android.server.wm.IAsyncRotationControllerWrapper
        public android.view.animation.Animation getFadeOutAnimation(com.android.server.wm.WindowToken token) {
            com.android.server.wm.WindowState topChild;
            if ((token.windowType == 2314 || token.windowType == 2000) && (topChild = token.getTopChild()) != null && (topChild.getName().contains("OplusOSEdgeFloatBar") || topChild.getName().contains("StatusBar"))) {
                return new android.view.animation.AlphaAnimation(0.0f, 0.0f);
            }
            return com.android.server.wm.AsyncRotationController.this.getFadeOutAnimation();
        }
    }
}
