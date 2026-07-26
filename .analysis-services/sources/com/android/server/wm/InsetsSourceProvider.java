package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class InsetsSourceProvider {
    private static final java.lang.String TAG = "InsetsSourceProvider";
    private com.android.server.wm.InsetsSourceProvider.ControlAdapter mAdapter;
    private boolean mClientVisible;
    protected android.view.InsetsSourceControl mControl;
    protected com.android.server.wm.InsetsControlTarget mControlTarget;
    private final boolean mControllable;
    protected final com.android.server.wm.DisplayContent mDisplayContent;
    private final android.view.InsetsSourceControl mFakeControl;
    private com.android.server.wm.InsetsControlTarget mFakeControlTarget;
    private int mFlagsFromFrameProvider;
    private int mFlagsFromServer;
    private com.android.internal.util.function.TriFunction<com.android.server.wm.DisplayFrames, com.android.server.wm.WindowContainer, android.graphics.Rect, java.lang.Integer> mFrameProvider;
    private boolean mHasPendingPosition;
    protected boolean mIsLeashReadyForDispatching;
    private android.util.SparseArray<com.android.internal.util.function.TriFunction<com.android.server.wm.DisplayFrames, com.android.server.wm.WindowContainer, android.graphics.Rect, java.lang.Integer>> mOverrideFrameProviders;
    private com.android.server.wm.InsetsControlTarget mPendingControlTarget;
    private boolean mSeamlessRotating;
    private boolean mServerVisible;
    private final java.util.function.Consumer<android.view.SurfaceControl.Transaction> mSetLeashPositionConsumer;
    protected final android.view.InsetsSource mSource;
    protected final com.android.server.wm.InsetsStateController mStateController;
    protected com.android.server.wm.WindowContainer mWindowContainer;
    private static final android.graphics.Rect EMPTY_RECT = new android.graphics.Rect();
    static boolean DEBUG_PANIC = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();
    private final android.util.SparseArray<android.graphics.Rect> mOverrideFrames = new android.util.SparseArray<>();
    private final android.graphics.Rect mSourceFrame = new android.graphics.Rect();
    private final android.graphics.Rect mLastSourceFrame = new android.graphics.Rect();
    private android.graphics.Insets mInsetsHint = android.graphics.Insets.NONE;
    private boolean mInsetsHintStale = true;
    private boolean mCropToProvidingInsets = false;
    public com.android.server.wm.IInsetsSourceProviderExt mInsetsSourceProviderExt = (com.android.server.wm.IInsetsSourceProviderExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IInsetsSourceProviderExt.class).base(this).create();

    InsetsSourceProvider(android.view.InsetsSource source, com.android.server.wm.InsetsStateController stateController, com.android.server.wm.DisplayContent displayContent) {
        boolean z;
        if ((android.view.WindowInsets.Type.defaultVisible() & source.getType()) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mClientVisible = z;
        this.mSource = source;
        this.mDisplayContent = displayContent;
        this.mStateController = stateController;
        this.mFakeControl = new android.view.InsetsSourceControl(source.getId(), source.getType(), (android.view.SurfaceControl) null, false, new android.graphics.Point(), android.graphics.Insets.NONE);
        this.mControllable = (com.android.server.wm.InsetsPolicy.CONTROLLABLE_TYPES & source.getType()) != 0;
        this.mSetLeashPositionConsumer = new java.util.function.Consumer() { // from class: com.android.server.wm.InsetsSourceProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$0((android.view.SurfaceControl.Transaction) obj);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.view.SurfaceControl.Transaction t) {
        android.view.SurfaceControl leash;
        if (this.mControl != null && (leash = this.mControl.getLeash()) != null && leash.isValid()) {
            android.graphics.Point position = this.mControl.getSurfacePosition();
            if (DEBUG_PANIC && this.mControl.getType() == android.view.WindowInsets.Type.navigationBars()) {
                android.util.Slog.d(TAG, "set leash position, the mControl: " + this.mControl.toString());
            }
            t.setPosition(leash, position.x, position.y);
        }
        if (this.mHasPendingPosition) {
            this.mHasPendingPosition = false;
            this.mInsetsSourceProviderExt.setHasPendingPosition(false);
            if (this.mPendingControlTarget != this.mControlTarget || this.mInsetsSourceProviderExt.shouldIgnoreTargetCheck()) {
                this.mStateController.notifyControlTargetChanged(this.mPendingControlTarget, this);
            }
        }
    }

    android.view.InsetsSource getSource() {
        return this.mSource;
    }

    boolean isControllable() {
        return this.mControllable;
    }

    void setWindowContainer(com.android.server.wm.WindowContainer windowContainer, com.android.internal.util.function.TriFunction<com.android.server.wm.DisplayFrames, com.android.server.wm.WindowContainer, android.graphics.Rect, java.lang.Integer> frameProvider, android.util.SparseArray<com.android.internal.util.function.TriFunction<com.android.server.wm.DisplayFrames, com.android.server.wm.WindowContainer, android.graphics.Rect, java.lang.Integer>> overrideFrameProviders) {
        if (DEBUG_PANIC && this.mSource.getType() == android.view.WindowInsets.Type.navigationBars()) {
            android.util.Slog.d(TAG, "InsetsSource setWin: " + windowContainer + ", type: " + android.view.WindowInsets.Type.toString(this.mSource.getType()) + ", mWindowContainer: " + this.mWindowContainer + ", mPendingControlTarget: " + this.mPendingControlTarget + ", caller: " + android.os.Debug.getCallers(5));
        }
        if (this.mWindowContainer != null) {
            if (this.mControllable) {
                this.mWindowContainer.setControllableInsetProvider(null);
            }
            this.mWindowContainer.cancelAnimation();
            this.mWindowContainer.getInsetsSourceProviders().remove(this.mSource.getId());
            this.mSeamlessRotating = false;
            this.mHasPendingPosition = false;
            this.mInsetsSourceProviderExt.setHasPendingPosition(false);
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_INSETS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(windowContainer);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(android.view.WindowInsets.Type.toString(this.mSource.getType()));
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, 1522894362518893789L, 0, null, protoLogParam0, protoLogParam1);
        }
        this.mWindowContainer = windowContainer;
        this.mFrameProvider = frameProvider;
        this.mOverrideFrames.clear();
        this.mOverrideFrameProviders = overrideFrameProviders;
        if (windowContainer == null) {
            setServerVisible(false);
            this.mSource.setVisibleFrame((android.graphics.Rect) null);
            this.mSource.setFlags(0, -1);
            this.mSourceFrame.setEmpty();
            return;
        }
        this.mWindowContainer.getInsetsSourceProviders().put(this.mSource.getId(), this);
        if (this.mControllable) {
            this.mWindowContainer.setControllableInsetProvider(this);
            if (this.mPendingControlTarget != this.mControlTarget) {
                this.mStateController.notifyControlTargetChanged(this.mPendingControlTarget, this);
            }
        }
    }

    boolean setFlags(int flags, int mask) {
        this.mFlagsFromServer = (this.mFlagsFromServer & (~mask)) | (flags & mask);
        int mergedFlags = this.mFlagsFromFrameProvider | this.mFlagsFromServer;
        if (this.mSource.getFlags() != mergedFlags) {
            this.mSource.setFlags(mergedFlags);
            return true;
        }
        return false;
    }

    void updateSourceFrame(android.graphics.Rect frame) {
        android.graphics.Rect overrideFrame;
        if (this.mWindowContainer == null) {
            return;
        }
        com.android.server.wm.WindowState win = this.mWindowContainer.asWindowState();
        if (win == null) {
            if (this.mServerVisible) {
                this.mTmpRect.set(this.mWindowContainer.getBounds());
                if (this.mFrameProvider != null) {
                    this.mFrameProvider.apply(this.mWindowContainer.getDisplayContent().mDisplayFrames, this.mWindowContainer, this.mTmpRect);
                }
            } else {
                this.mTmpRect.setEmpty();
            }
            this.mSource.setFrame(this.mTmpRect);
            this.mSource.setVisibleFrame((android.graphics.Rect) null);
            return;
        }
        this.mSourceFrame.set(frame);
        if (this.mFrameProvider != null) {
            this.mFlagsFromFrameProvider = ((java.lang.Integer) this.mFrameProvider.apply(this.mWindowContainer.getDisplayContent().mDisplayFrames, this.mWindowContainer, this.mSourceFrame)).intValue();
            this.mSource.setFlags(this.mFlagsFromFrameProvider | this.mFlagsFromServer);
        }
        updateSourceFrameForServerVisibility();
        if (!this.mLastSourceFrame.equals(this.mSourceFrame)) {
            this.mLastSourceFrame.set(this.mSourceFrame);
            this.mInsetsHintStale = true;
        }
        if (this.mOverrideFrameProviders != null) {
            for (int i = this.mOverrideFrameProviders.size() - 1; i >= 0; i--) {
                int windowType = this.mOverrideFrameProviders.keyAt(i);
                if (this.mOverrideFrames.contains(windowType)) {
                    overrideFrame = this.mOverrideFrames.get(windowType);
                    overrideFrame.set(frame);
                } else {
                    overrideFrame = new android.graphics.Rect(frame);
                }
                com.android.internal.util.function.TriFunction<com.android.server.wm.DisplayFrames, com.android.server.wm.WindowContainer, android.graphics.Rect, java.lang.Integer> provider = this.mOverrideFrameProviders.get(windowType);
                if (provider != null) {
                    this.mOverrideFrameProviders.get(windowType).apply(this.mWindowContainer.getDisplayContent().mDisplayFrames, this.mWindowContainer, overrideFrame);
                }
                this.mOverrideFrames.put(windowType, overrideFrame);
            }
        }
        if (win.mGivenVisibleInsets.left == 0 && win.mGivenVisibleInsets.top == 0 && win.mGivenVisibleInsets.right == 0 && win.mGivenVisibleInsets.bottom == 0) {
            this.mSource.setVisibleFrame((android.graphics.Rect) null);
            return;
        }
        this.mTmpRect.set(frame);
        this.mTmpRect.inset(win.mGivenVisibleInsets);
        this.mSource.setVisibleFrame(this.mTmpRect);
    }

    private void updateSourceFrameForServerVisibility() {
        android.graphics.Rect frame = this.mServerVisible ? this.mSourceFrame : EMPTY_RECT;
        if (this.mSource.getFrame().equals(frame)) {
            return;
        }
        this.mSource.setFrame(frame);
        if (this.mWindowContainer != null) {
            this.mSource.updateSideHint(this.mWindowContainer.getBounds());
        }
    }

    void onWindowContainerBoundsChanged() {
        this.mInsetsHintStale = true;
    }

    android.graphics.Insets getInsetsHint() {
        if (!this.mServerVisible) {
            return this.mInsetsHint;
        }
        com.android.server.wm.WindowState win = this.mWindowContainer.asWindowState();
        if (win != null && win.mGivenInsetsPending) {
            return this.mInsetsHint;
        }
        if (this.mInsetsHintStale) {
            android.graphics.Rect bounds = this.mWindowContainer.getBounds();
            this.mInsetsHint = this.mSource.calculateInsets(bounds, true);
            this.mInsetsHintStale = false;
        }
        return this.mInsetsHint;
    }

    android.view.InsetsSource createSimulatedSource(com.android.server.wm.DisplayFrames displayFrames, android.graphics.Rect frame) {
        android.view.InsetsSource source = new android.view.InsetsSource(this.mSource);
        this.mTmpRect.set(frame);
        if (this.mFrameProvider != null) {
            this.mFrameProvider.apply(displayFrames, this.mWindowContainer, this.mTmpRect);
        }
        com.android.server.wm.WindowState win = this.mWindowContainer.asWindowState();
        if (win != null) {
            android.view.WindowManager.LayoutParams lp = win.mAttrs.forRotation(displayFrames.mRotation);
            com.android.server.wm.DisplayContent dc = win.getDisplayContent();
            if (lp.type == 2019 && dc != null && dc.getDisplayPolicy() != null && dc.getDisplayPolicy().getWrapper() != null && dc.getDisplayPolicy().getWrapper().getExtImpl() != null && dc.getDisplayPolicy().getWrapper().getExtImpl().isHideNavBarGestureMode()) {
                dc.getDisplayPolicy().getWrapper().getExtImpl().reCalculateNavRectdo(displayFrames, this.mTmpRect);
            }
        }
        source.setFrame(this.mTmpRect);
        source.setVisibleFrame((android.graphics.Rect) null);
        return source;
    }

    void onPostLayout() {
        boolean isServerVisible;
        if (this.mWindowContainer == null) {
            return;
        }
        com.android.server.wm.WindowState windowState = this.mWindowContainer.asWindowState();
        if (windowState != null) {
            isServerVisible = windowState.wouldBeVisibleIfPolicyIgnored() && windowState.isVisibleByPolicy();
        } else {
            isServerVisible = this.mWindowContainer.isVisibleRequested();
        }
        if (android.view.inputmethod.Flags.refactorInsetsController() && this.mControl != null && this.mControl.getType() == android.view.WindowInsets.Type.ime() && !this.mServerVisible && isServerVisible && windowState != null) {
            isServerVisible = windowState.isDrawn() && !windowState.mGivenInsetsPending;
        }
        boolean serverVisibleChanged = this.mServerVisible != isServerVisible;
        setServerVisible(isServerVisible);
        updateInsetsControlPosition(windowState, serverVisibleChanged);
    }

    void updateInsetsControlPosition(com.android.server.wm.WindowState windowState) {
        updateInsetsControlPosition(windowState, false);
    }

    private void updateInsetsControlPosition(com.android.server.wm.WindowState windowState, boolean serverVisibleChanged) {
        com.android.server.wm.AsyncRotationController rotationController;
        android.view.SurfaceControl.Transaction drawT;
        if (this.mControl == null) {
            return;
        }
        boolean changed = false;
        android.graphics.Point position = getWindowFrameSurfacePosition();
        if (this.mControl.setSurfacePosition(position.x, position.y) && this.mControlTarget != null) {
            changed = true;
            boolean ignoreOkToDisplay = this.mControl.getType() == android.view.WindowInsets.Type.navigationBars() && this.mDisplayContent.getAsyncRotationController() != null && windowState != null && this.mDisplayContent.getAsyncRotationController().isTargetToken(windowState.mToken);
            if (windowState != null && windowState.getWindowFrames().didFrameSizeChange() && windowState.mWinAnimator.getShown() && (this.mWindowContainer.okToDisplay() || ignoreOkToDisplay)) {
                this.mHasPendingPosition = true;
                this.mInsetsSourceProviderExt.setHasPendingPosition(true);
                windowState.applyWithNextDraw(this.mSetLeashPositionConsumer);
            } else {
                android.view.SurfaceControl.Transaction t = this.mWindowContainer.getSyncTransaction();
                if (windowState != null && (rotationController = this.mDisplayContent.getAsyncRotationController()) != null && (drawT = rotationController.getDrawTransaction(windowState.mToken)) != null) {
                    t = drawT;
                }
                this.mSetLeashPositionConsumer.accept(t);
            }
        }
        android.graphics.Insets insetsHint = getInsetsHint();
        if (this.mControl.getInsetsHint() != null && !this.mControl.getInsetsHint().equals(insetsHint)) {
            this.mControl.setInsetsHint(insetsHint);
            changed = true;
        }
        if (android.view.inputmethod.Flags.refactorInsetsController() && serverVisibleChanged) {
            changed = true;
        }
        if (changed) {
            this.mStateController.notifyControlChanged(this.mControlTarget);
        }
    }

    private android.graphics.Point getWindowFrameSurfacePosition() {
        com.android.server.wm.AsyncRotationController controller;
        com.android.server.wm.WindowState win = this.mWindowContainer.asWindowState();
        if (win != null && this.mControl != null && (controller = this.mDisplayContent.getAsyncRotationController()) != null && controller.shouldFreezeInsetsPosition(win)) {
            return this.mControl.getSurfacePosition();
        }
        android.graphics.Rect frame = win != null ? win.getFrame() : this.mWindowContainer.getBounds();
        android.graphics.Point position = new android.graphics.Point();
        this.mWindowContainer.transformFrameToSurfacePosition(frame.left, frame.top, position);
        return position;
    }

    void updateFakeControlTarget(com.android.server.wm.InsetsControlTarget fakeTarget) {
        if (fakeTarget == this.mFakeControlTarget) {
            return;
        }
        this.mFakeControlTarget = fakeTarget;
    }

    void setCropToProvidingInsetsBounds(android.view.SurfaceControl.Transaction t) {
        this.mCropToProvidingInsets = true;
        if (this.mWindowContainer != null && this.mWindowContainer.mSurfaceAnimator.hasLeash()) {
            t.setWindowCrop(this.mWindowContainer.mSurfaceAnimator.mLeash, getProvidingInsetsBoundsCropRect());
        }
    }

    void removeCropToProvidingInsetsBounds(android.view.SurfaceControl.Transaction t) {
        this.mCropToProvidingInsets = false;
        if (this.mWindowContainer != null && this.mWindowContainer.mSurfaceAnimator.hasLeash()) {
            t.setWindowCrop(this.mWindowContainer.mSurfaceAnimator.mLeash, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.graphics.Rect getProvidingInsetsBoundsCropRect() {
        android.graphics.Rect sourceWindowFrame;
        if (this.mWindowContainer.asWindowState() != null) {
            sourceWindowFrame = this.mWindowContainer.asWindowState().getFrame();
        } else {
            sourceWindowFrame = this.mWindowContainer.getBounds();
        }
        android.graphics.Rect insetFrame = getSource().getFrame();
        return new android.graphics.Rect(insetFrame.left - sourceWindowFrame.left, insetFrame.top - sourceWindowFrame.top, insetFrame.right - sourceWindowFrame.left, insetFrame.bottom - sourceWindowFrame.top);
    }

    void updateControlForTarget(com.android.server.wm.InsetsControlTarget target, boolean force) {
        android.view.SurfaceControl.Transaction t;
        boolean initiallyVisible;
        boolean force2 = this.mInsetsSourceProviderExt.adjustForceUpdateControlForTarget(target, force);
        if (this.mSeamlessRotating) {
            return;
        }
        this.mPendingControlTarget = target;
        if (this.mWindowContainer != null && this.mWindowContainer.getSurfaceControl() == null) {
            setWindowContainer(null, null, null);
        }
        if (this.mWindowContainer == null) {
            return;
        }
        if ((target != this.mControlTarget || force2) && !this.mHasPendingPosition) {
            if (target == null) {
                this.mWindowContainer.cancelAnimation();
                setClientVisible((android.view.WindowInsets.Type.defaultVisible() & this.mSource.getType()) != 0);
                return;
            }
            android.graphics.Point surfacePosition = getWindowFrameSurfacePosition();
            this.mAdapter = new com.android.server.wm.InsetsSourceProvider.ControlAdapter(surfacePosition);
            if (this.mSource.getType() == android.view.WindowInsets.Type.ime()) {
                setClientVisible(target.isRequestedVisible(android.view.WindowInsets.Type.ime()));
            }
            android.view.SurfaceControl.Transaction t2 = this.mWindowContainer.getSyncTransaction();
            if (!this.mInsetsSourceProviderExt.changeStatusBarTransaction(this.mWindowContainer, this.mSource, this.mSeamlessRotating)) {
                t = t2;
            } else {
                android.view.SurfaceControl.Transaction t3 = this.mWindowContainer.getPendingTransaction();
                t = t3;
            }
            this.mWindowContainer.startAnimation(t, this.mAdapter, !this.mClientVisible, 32);
            this.mIsLeashReadyForDispatching = false;
            android.view.SurfaceControl leash = this.mAdapter.mCapturedLeash;
            this.mControlTarget = target;
            if (getSource().getType() == android.view.WindowInsets.Type.ime() && this.mInsetsSourceProviderExt.getInputShowStatus() && (this.mControlTarget == this.mDisplayContent.mRemoteInsetsControlTarget || this.mControlTarget.isRequestedVisible(android.view.WindowInsets.Type.ime()))) {
                setClientVisible(true);
                if (leash != null) {
                    t.setAlpha(leash, 1.0f);
                    t.show(leash);
                }
            }
            this.mInsetsSourceProviderExt.showImeLeashInCarDisplayIfNeed(this, this.mControlTarget, t, leash);
            updateVisibility();
            boolean initiallyVisible2 = this.mClientVisible;
            if (this.mSource.getType() != android.view.WindowInsets.Type.ime()) {
                initiallyVisible = initiallyVisible2;
            } else {
                initiallyVisible = false;
            }
            this.mControl = new android.view.InsetsSourceControl(this.mSource.getId(), this.mSource.getType(), leash, initiallyVisible, surfacePosition, getInsetsHint());
            if (DEBUG_PANIC || com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_IME.isLogToLogcat()) {
                int controlType = this.mControl != null ? this.mControl.getType() : -1;
                android.util.Slog.d(TAG, java.lang.String.format("InsetsSource type %s Control %s for target %s", android.view.WindowInsets.Type.toString(controlType), this.mControl, this.mControlTarget) + ", surfacePosition: " + surfacePosition + ", caller: " + android.os.Debug.getCallers(8));
            }
        }
    }

    void startSeamlessRotation() {
        if (!this.mSeamlessRotating) {
            this.mSeamlessRotating = true;
            this.mWindowContainer.cancelAnimation();
        }
    }

    void finishSeamlessRotation() {
        this.mSeamlessRotating = false;
    }

    boolean updateClientVisibility(com.android.server.wm.InsetsControlTarget caller) {
        boolean requestedVisible = caller.isRequestedVisible(this.mSource.getType());
        if (caller != this.mControlTarget || requestedVisible == this.mClientVisible) {
            return false;
        }
        setClientVisible(requestedVisible);
        return true;
    }

    void onSurfaceTransactionApplied() {
        this.mIsLeashReadyForDispatching = true;
    }

    void setClientVisible(boolean clientVisible) {
        if (this.mClientVisible == clientVisible) {
            return;
        }
        this.mClientVisible = clientVisible;
        updateVisibility();
        this.mDisplayContent.setLayoutNeeded();
        this.mDisplayContent.mWmService.mWindowPlacerLocked.requestTraversal();
    }

    void setServerVisible(boolean serverVisible) {
        this.mServerVisible = serverVisible;
        updateSourceFrameForServerVisibility();
        updateVisibility();
    }

    protected void updateVisibility() {
        this.mSource.setVisible(this.mServerVisible && this.mClientVisible);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_INSETS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(android.view.WindowInsets.Type.toString(this.mSource.getType()));
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mServerVisible);
            java.lang.String protoLogParam2 = java.lang.String.valueOf(this.mClientVisible);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, -8234068212532234206L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2);
        }
    }

    protected boolean isLeashReadyForDispatching() {
        return this.mIsLeashReadyForDispatching;
    }

    android.view.InsetsSourceControl getControl(com.android.server.wm.InsetsControlTarget target) {
        if (target == this.mControlTarget) {
            if (!isLeashReadyForDispatching() && this.mControl != null) {
                return new android.view.InsetsSourceControl(this.mControl.getId(), this.mControl.getType(), (android.view.SurfaceControl) null, this.mControl.isInitiallyVisible(), this.mControl.getSurfacePosition(), this.mControl.getInsetsHint());
            }
            return this.mControl;
        }
        if (target == this.mFakeControlTarget) {
            return this.mFakeControl;
        }
        return null;
    }

    protected android.view.SurfaceControl getLeash(com.android.server.wm.InsetsControlTarget target) {
        if (target == this.mControlTarget && this.mIsLeashReadyForDispatching && this.mControl != null) {
            return this.mControl.getLeash();
        }
        return null;
    }

    com.android.server.wm.InsetsControlTarget getControlTarget() {
        return this.mControlTarget;
    }

    com.android.server.wm.InsetsControlTarget getFakeControlTarget() {
        return this.mFakeControlTarget;
    }

    boolean isClientVisible() {
        return this.mClientVisible;
    }

    boolean overridesFrame(int windowType) {
        return this.mOverrideFrames.contains(windowType);
    }

    android.graphics.Rect getOverriddenFrame(int windowType) {
        return this.mOverrideFrames.get(windowType);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + getClass().getSimpleName());
        java.lang.String prefix2 = prefix + "  ";
        pw.print(prefix2 + "mSource=");
        this.mSource.dump("", pw);
        pw.print(prefix2 + "mSourceFrame=");
        pw.println(this.mSourceFrame);
        if (this.mOverrideFrames.size() > 0) {
            pw.print(prefix2 + "mOverrideFrames=");
            pw.println(this.mOverrideFrames);
        }
        if (this.mControl != null) {
            pw.print(prefix2 + "mControl=");
            this.mControl.dump("", pw);
        }
        if (this.mControllable) {
            pw.print(prefix2 + "mInsetsHint=");
            pw.print(this.mInsetsHint);
            if (this.mInsetsHintStale) {
                pw.print(" stale");
            }
            pw.println();
        }
        pw.print(prefix2);
        pw.print("mIsLeashReadyForDispatching=");
        pw.print(this.mIsLeashReadyForDispatching);
        pw.print(" mHasPendingPosition=");
        pw.print(this.mHasPendingPosition);
        pw.println();
        if (this.mWindowContainer != null) {
            pw.print(prefix2 + "mWindowContainer=");
            pw.println(this.mWindowContainer);
        }
        if (this.mAdapter != null) {
            pw.print(prefix2 + "mAdapter=");
            this.mAdapter.dump(pw, "");
        }
        if (this.mControlTarget != null) {
            pw.print(prefix2 + "mControlTarget=");
            pw.println(this.mControlTarget);
        }
        if (this.mPendingControlTarget != this.mControlTarget) {
            pw.print(prefix2 + "mPendingControlTarget=");
            pw.println(this.mPendingControlTarget);
        }
        if (this.mFakeControlTarget != null) {
            pw.print(prefix2 + "mFakeControlTarget=");
            pw.println(this.mFakeControlTarget);
        }
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        long token = proto.start(fieldId);
        this.mSource.dumpDebug(proto, 1146756268033L);
        this.mTmpRect.dumpDebug(proto, 1146756268034L);
        this.mFakeControl.dumpDebug(proto, 1146756268035L);
        if (this.mControl != null) {
            this.mControl.dumpDebug(proto, 1146756268036L);
        }
        if (this.mControlTarget != null && this.mControlTarget.getWindow() != null) {
            this.mControlTarget.getWindow().dumpDebug(proto, 1146756268037L, logLevel);
        }
        if (this.mPendingControlTarget != null && this.mPendingControlTarget != this.mControlTarget && this.mPendingControlTarget.getWindow() != null) {
            this.mPendingControlTarget.getWindow().dumpDebug(proto, 1146756268038L, logLevel);
        }
        if (this.mFakeControlTarget != null && this.mFakeControlTarget.getWindow() != null) {
            this.mFakeControlTarget.getWindow().dumpDebug(proto, 1146756268039L, logLevel);
        }
        if (this.mAdapter != null && this.mAdapter.mCapturedLeash != null) {
            this.mAdapter.mCapturedLeash.dumpDebug(proto, 1146756268040L);
        }
        proto.write(1133871366154L, this.mIsLeashReadyForDispatching);
        proto.write(1133871366155L, this.mClientVisible);
        proto.write(1133871366156L, this.mServerVisible);
        proto.write(1133871366157L, this.mSeamlessRotating);
        proto.write(1133871366159L, this.mControllable);
        if (this.mWindowContainer != null && this.mWindowContainer.asWindowState() != null) {
            this.mWindowContainer.asWindowState().dumpDebug(proto, 1146756268048L, logLevel);
        }
        proto.end(token);
    }

    private class ControlAdapter implements com.android.server.wm.AnimationAdapter {
        private android.view.SurfaceControl mCapturedLeash;
        private final android.graphics.Point mSurfacePosition;

        ControlAdapter(android.graphics.Point surfacePosition) {
            this.mSurfacePosition = surfacePosition;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public boolean getShowWallpaper() {
            return false;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void startAnimation(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, int type, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishCallback) {
            if (com.android.server.wm.InsetsSourceProvider.this.mSource.getType() == android.view.WindowInsets.Type.ime() && (!android.view.inputmethod.Flags.refactorInsetsController() || !com.android.server.wm.InsetsSourceProvider.this.mClientVisible)) {
                t.setAlpha(animationLeash, 1.0f);
                t.hide(animationLeash);
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_INSETS_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(com.android.server.wm.InsetsSourceProvider.this.mSource);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(com.android.server.wm.InsetsSourceProvider.this.mControlTarget);
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, -8601070090234611338L, 0, null, protoLogParam0, protoLogParam1);
            }
            if (com.android.server.wm.InsetsSourceProvider.DEBUG_PANIC && com.android.server.wm.InsetsSourceProvider.this.mSource.getType() == android.view.WindowInsets.Type.navigationBars()) {
                android.util.Slog.d(com.android.server.wm.InsetsSourceProvider.TAG, "startAnimation, source: " + com.android.server.wm.InsetsSourceProvider.this.mSource.toString() + ", mControlTarget: " + com.android.server.wm.InsetsSourceProvider.this.mControlTarget + ", mSurfacePosition: " + this.mSurfacePosition);
            }
            this.mCapturedLeash = animationLeash;
            t.setPosition(this.mCapturedLeash, this.mSurfacePosition.x, this.mSurfacePosition.y);
            if (com.android.server.wm.InsetsSourceProvider.this.mCropToProvidingInsets) {
                t.setWindowCrop(this.mCapturedLeash, com.android.server.wm.InsetsSourceProvider.this.getProvidingInsetsBoundsCropRect());
            }
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void onAnimationCancelled(android.view.SurfaceControl animationLeash) {
            if (com.android.server.wm.InsetsSourceProvider.this.mAdapter == this) {
                com.android.server.wm.InsetsSourceProvider.this.mStateController.notifyControlRevoked(com.android.server.wm.InsetsSourceProvider.this.mControlTarget, com.android.server.wm.InsetsSourceProvider.this);
                com.android.server.wm.InsetsSourceProvider.this.mControl = null;
                com.android.server.wm.InsetsSourceProvider.this.mControlTarget = null;
                com.android.server.wm.InsetsSourceProvider.this.mAdapter = null;
                com.android.server.wm.InsetsSourceProvider.this.setClientVisible((android.view.WindowInsets.Type.defaultVisible() & com.android.server.wm.InsetsSourceProvider.this.mSource.getType()) != 0);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_INSETS_enabled[2]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(com.android.server.wm.InsetsSourceProvider.this.mSource);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(com.android.server.wm.InsetsSourceProvider.this.mControlTarget);
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_INSETS, -6857870589074001153L, 0, null, protoLogParam0, protoLogParam1);
                }
            }
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getDurationHint() {
            return 0L;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public long getStatusBarTransitionsStartTime() {
            return 0L;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix + "ControlAdapter mCapturedLeash=");
            pw.print(this.mCapturedLeash);
            pw.println();
        }

        @Override // com.android.server.wm.AnimationAdapter
        public void dumpDebug(android.util.proto.ProtoOutputStream proto) {
        }
    }
}
