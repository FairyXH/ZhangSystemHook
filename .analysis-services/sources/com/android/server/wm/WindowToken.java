package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WindowToken extends com.android.server.wm.WindowContainer<com.android.server.wm.WindowState> {
    private static final java.lang.String TAG = "WindowManager";
    private boolean mClientVisible;
    private android.view.SurfaceControl mFixedRotationTransformLeash;
    private com.android.server.wm.WindowToken.FixedRotationTransformState mFixedRotationTransformState;
    private final boolean mFromClientToken;
    final android.os.Bundle mOptions;
    final boolean mOwnerCanManageAppTokens;
    boolean mPersistOnEmpty;
    final boolean mRoundedCornerOverlay;
    private final java.util.Comparator<com.android.server.wm.WindowState> mWindowComparator;
    public com.android.server.wm.IWindowTokenExt mWindowTokenExt;
    boolean paused;
    java.lang.String stringName;
    final android.os.IBinder token;
    final int windowType;

    private static class FixedRotationTransformState {
        final com.android.server.wm.DisplayFrames mDisplayFrames;
        final android.view.DisplayInfo mDisplayInfo;
        final android.content.res.Configuration mRotatedOverrideConfiguration;
        final java.util.ArrayList<com.android.server.wm.WindowToken> mAssociatedTokens = new java.util.ArrayList<>(3);
        boolean mIsTransforming = true;

        FixedRotationTransformState(android.view.DisplayInfo rotatedDisplayInfo, com.android.server.wm.DisplayFrames rotatedDisplayFrames, android.content.res.Configuration rotatedConfig) {
            this.mDisplayInfo = rotatedDisplayInfo;
            this.mDisplayFrames = rotatedDisplayFrames;
            this.mRotatedOverrideConfiguration = rotatedConfig;
        }

        void transform(com.android.server.wm.WindowContainer<?> container) {
        }

        void resetTransform() {
            for (int i = this.mAssociatedTokens.size() - 1; i >= 0; i--) {
                this.mAssociatedTokens.get(i).removeFixedRotationLeash();
            }
        }

        void disassociate(com.android.server.wm.WindowToken token) {
            this.mAssociatedTokens.remove(token);
        }
    }

    private static class FixedRotationTransformStateLegacy extends com.android.server.wm.WindowToken.FixedRotationTransformState {
        final java.util.ArrayList<com.android.server.wm.WindowContainer<?>> mRotatedContainers;
        final com.android.server.wm.SeamlessRotator mRotator;

        FixedRotationTransformStateLegacy(android.view.DisplayInfo rotatedDisplayInfo, com.android.server.wm.DisplayFrames rotatedDisplayFrames, android.content.res.Configuration rotatedConfig, int currentRotation) {
            super(rotatedDisplayInfo, rotatedDisplayFrames, rotatedConfig);
            this.mRotatedContainers = new java.util.ArrayList<>(3);
            this.mRotator = new com.android.server.wm.SeamlessRotator(rotatedDisplayInfo.rotation, currentRotation, rotatedDisplayInfo, true);
        }

        @Override // com.android.server.wm.WindowToken.FixedRotationTransformState
        void transform(com.android.server.wm.WindowContainer<?> container) {
            this.mRotator.unrotate(container.getPendingTransaction(), container);
            if (!this.mRotatedContainers.contains(container)) {
                this.mRotatedContainers.add(container);
            }
        }

        @Override // com.android.server.wm.WindowToken.FixedRotationTransformState
        void resetTransform() {
            for (int i = this.mRotatedContainers.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowContainer<?> c = this.mRotatedContainers.get(i);
                if (c.getParent() != null) {
                    this.mRotator.finish(c.getPendingTransaction(), c);
                }
            }
        }

        @Override // com.android.server.wm.WindowToken.FixedRotationTransformState
        void disassociate(com.android.server.wm.WindowToken token) {
            super.disassociate(token);
            this.mRotatedContainers.remove(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$new$0(com.android.server.wm.WindowState newWindow, com.android.server.wm.WindowState existingWindow) {
        if (newWindow.mToken == this) {
            if (existingWindow.mToken == this) {
                return isFirstChildWindowGreaterThanSecond(newWindow, existingWindow) ? 1 : -1;
            }
            throw new java.lang.IllegalArgumentException("existingWindow=" + existingWindow + " is not a child of token=" + this);
        }
        throw new java.lang.IllegalArgumentException("newWindow=" + newWindow + " is not a child of token=" + this);
    }

    protected WindowToken(com.android.server.wm.WindowManagerService service, android.os.IBinder _token, int type, boolean persistOnEmpty, com.android.server.wm.DisplayContent dc, boolean ownerCanManageAppTokens) {
        this(service, _token, type, persistOnEmpty, dc, ownerCanManageAppTokens, false, false, null);
    }

    protected WindowToken(com.android.server.wm.WindowManagerService service, android.os.IBinder _token, int type, boolean persistOnEmpty, com.android.server.wm.DisplayContent dc, boolean ownerCanManageAppTokens, boolean roundedCornerOverlay, boolean fromClientToken, android.os.Bundle options) {
        super(service);
        this.mWindowTokenExt = (com.android.server.wm.IWindowTokenExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowTokenExt.class).base(this).create();
        this.paused = false;
        this.mWindowComparator = new java.util.Comparator() { // from class: com.android.server.wm.WindowToken$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return this.f$0.lambda$new$0((com.android.server.wm.WindowState) obj, (com.android.server.wm.WindowState) obj2);
            }
        };
        this.token = _token;
        this.windowType = type;
        this.mOptions = options;
        this.mPersistOnEmpty = persistOnEmpty;
        this.mOwnerCanManageAppTokens = ownerCanManageAppTokens;
        this.mRoundedCornerOverlay = roundedCornerOverlay;
        this.mFromClientToken = fromClientToken;
        if (dc != null) {
            dc.addWindowToken(this.token, this);
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
    void removeAllWindowsIfPossible() {
        int i = this.mChildren.size() - 1;
        while (i >= 0) {
            if (i >= this.mChildren.size()) {
                android.util.Slog.d(TAG, "removeAllWindowsIfPossible IndexOutOfBoundsE this = " + this);
            } else {
                com.android.server.wm.WindowState win = (com.android.server.wm.WindowState) this.mChildren.get(i);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_MOVEMENT_enabled[3]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(win);
                    com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT, 8174298531248485625L, 0, null, protoLogParam0);
                }
                win.removeIfPossible();
                if (i > this.mChildren.size()) {
                    i = this.mChildren.size();
                }
            }
            i--;
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
    void setExiting(boolean animateExit) {
        if (isEmpty()) {
            super.removeImmediately();
            return;
        }
        this.mPersistOnEmpty = false;
        if (!isVisible()) {
            return;
        }
        int count = this.mChildren.size();
        boolean changed = false;
        for (int i = 0; i < count; i++) {
            com.android.server.wm.WindowState win = (com.android.server.wm.WindowState) this.mChildren.get(i);
            changed |= win.onSetAppExiting(animateExit);
        }
        if (changed) {
            this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
            this.mWmService.updateFocusedWindowLocked(0, false);
        }
    }

    float getCompatScale() {
        return this.mDisplayContent.mCompatibleScreenScale;
    }

    boolean hasSizeCompatBounds() {
        return false;
    }

    protected boolean isFirstChildWindowGreaterThanSecond(com.android.server.wm.WindowState newWindow, com.android.server.wm.WindowState existingWindow) {
        return newWindow.mBaseLayer >= existingWindow.mBaseLayer;
    }

    void addWindow(com.android.server.wm.WindowState win) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(win);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(android.os.Debug.getCallers(5));
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS, 2740931087734487464L, 0, null, protoLogParam0, protoLogParam1);
        }
        if (win.isChildWindow()) {
            return;
        }
        if (this.mSurfaceControl == null) {
            createSurfaceControl(true);
            reassignLayer(getSyncTransaction());
        }
        if (!this.mChildren.contains(win)) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ADD_REMOVE_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(win);
                java.lang.String protoLogParam12 = java.lang.String.valueOf(this);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 2382798629637143561L, 0, null, protoLogParam02, protoLogParam12);
            }
            addChild(win, this.mWindowComparator);
            this.mWmService.mWindowsChanged = true;
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void createSurfaceControl(boolean force) {
        if (!this.mFromClientToken || force) {
            super.createSurfaceControl(force);
        }
    }

    boolean isEmpty() {
        return this.mChildren.isEmpty();
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
    boolean windowsCanBeWallpaperTarget() {
        for (int j = this.mChildren.size() - 1; j >= 0; j--) {
            com.android.server.wm.WindowState w = (com.android.server.wm.WindowState) this.mChildren.get(j);
            if (w.hasWallpaper()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    void removeImmediately() {
        if (this.mDisplayContent != null) {
            this.mDisplayContent.removeWindowToken(this.token, true);
        }
        super.removeImmediately();
    }

    @Override // com.android.server.wm.WindowContainer
    void onDisplayChanged(com.android.server.wm.DisplayContent dc) {
        dc.reParentWindowToken(this);
        super.onDisplayChanged(dc);
    }

    @Override // com.android.server.wm.WindowContainer
    void assignLayer(android.view.SurfaceControl.Transaction t, int layer) {
        if (this.mRoundedCornerOverlay) {
            super.assignLayer(t, 1073741826);
        } else if (this.mWindowTokenExt.canAssigFingerPrintLayer(this, this.windowType)) {
            super.assignLayer(t, 1073741827);
        } else {
            super.assignLayer(t, layer);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    android.view.SurfaceControl.Builder makeSurface() {
        android.view.SurfaceControl.Builder builder = super.makeSurface();
        if (!com.android.server.wm.ActivityTaskManagerService.LTW_DISABLE) {
            if (this.mRoundedCornerOverlay || this.windowType == 2099) {
                builder.setParent(null);
            }
        } else if (this.mRoundedCornerOverlay) {
            builder.setParent(null);
        }
        this.mWindowTokenExt.makeSurface(builder, this.windowType);
        return builder;
    }

    boolean isClientVisible() {
        return this.mClientVisible;
    }

    void setClientVisible(boolean clientVisible) {
        if (this.mClientVisible == clientVisible) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam2 = java.lang.String.valueOf(android.os.Debug.getCallers(5));
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -7314975896738778749L, 12, null, protoLogParam0, java.lang.Boolean.valueOf(clientVisible), protoLogParam2);
        }
        android.util.Slog.d(TAG, "NFW_setClientVisible:" + this + " clientVisible:" + clientVisible);
        this.mClientVisible = clientVisible;
        sendAppVisibilityToClients();
    }

    boolean hasFixedRotationTransform() {
        return this.mFixedRotationTransformState != null;
    }

    boolean hasFixedRotationTransform(com.android.server.wm.WindowToken token) {
        if (this.mFixedRotationTransformState == null || token == null) {
            return false;
        }
        return this == token || this.mFixedRotationTransformState == token.mFixedRotationTransformState;
    }

    boolean isFinishingFixedRotationTransform() {
        return (this.mFixedRotationTransformState == null || this.mFixedRotationTransformState.mIsTransforming) ? false : true;
    }

    boolean isFixedRotationTransforming() {
        return this.mFixedRotationTransformState != null && this.mFixedRotationTransformState.mIsTransforming;
    }

    android.view.DisplayInfo getFixedRotationTransformDisplayInfo() {
        if (isFixedRotationTransforming()) {
            return this.mFixedRotationTransformState.mDisplayInfo;
        }
        return null;
    }

    com.android.server.wm.DisplayFrames getFixedRotationTransformDisplayFrames() {
        if (isFixedRotationTransforming()) {
            return this.mFixedRotationTransformState.mDisplayFrames;
        }
        return null;
    }

    android.graphics.Rect getFixedRotationTransformMaxBounds() {
        if (isFixedRotationTransforming()) {
            return this.mFixedRotationTransformState.mRotatedOverrideConfiguration.windowConfiguration.getMaxBounds();
        }
        return null;
    }

    android.graphics.Rect getFixedRotationTransformDisplayBounds() {
        if (isFixedRotationTransforming()) {
            return this.mFixedRotationTransformState.mRotatedOverrideConfiguration.windowConfiguration.getBounds();
        }
        return null;
    }

    android.view.InsetsState getFixedRotationTransformInsetsState() {
        if (isFixedRotationTransforming()) {
            return this.mFixedRotationTransformState.mDisplayFrames.mInsetsState;
        }
        return null;
    }

    void applyFixedRotationTransform(android.view.DisplayInfo info, com.android.server.wm.DisplayFrames displayFrames, android.content.res.Configuration config) {
        com.android.server.wm.WindowToken.FixedRotationTransformState fixedRotationTransformStateLegacy;
        if (this.mFixedRotationTransformState != null) {
            this.mFixedRotationTransformState.disassociate(this);
        }
        if (config != null && this.mDisplayContent != null && info != null) {
            android.util.Slog.w(TAG, "Apply fixed rotation transform to " + this + ". Preferred rotation:" + config.windowConfiguration.getRotation() + ", Current rotation:" + this.mDisplayContent.getRotation() + ", info.rotation:" + info.rotation);
        }
        android.content.res.Configuration config2 = new android.content.res.Configuration(config);
        this.mWindowTokenExt.resolveScreenConfigInSecondary(this, config2, info);
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            fixedRotationTransformStateLegacy = new com.android.server.wm.WindowToken.FixedRotationTransformState(info, displayFrames, config2);
        } else {
            fixedRotationTransformStateLegacy = new com.android.server.wm.WindowToken.FixedRotationTransformStateLegacy(info, displayFrames, config2, this.mDisplayContent.getRotation());
        }
        this.mFixedRotationTransformState = fixedRotationTransformStateLegacy;
        this.mWindowTokenExt.resolveScreenConfigInSecondary(this, config2, info);
        this.mFixedRotationTransformState.mAssociatedTokens.add(this);
        this.mDisplayContent.getDisplayPolicy().simulateLayoutDisplay(displayFrames);
        onFixedRotationStatePrepared();
    }

    void linkFixedRotationTransform(com.android.server.wm.WindowToken other) {
        com.android.server.wm.WindowToken.FixedRotationTransformState fixedRotationState = other.mFixedRotationTransformState;
        if (fixedRotationState == null || this.mFixedRotationTransformState == fixedRotationState) {
            return;
        }
        if (this.mFixedRotationTransformState != null) {
            this.mFixedRotationTransformState.disassociate(this);
        }
        this.mFixedRotationTransformState = fixedRotationState;
        fixedRotationState.mAssociatedTokens.add(this);
        onFixedRotationStatePrepared();
    }

    private void onFixedRotationStatePrepared() {
        onConfigurationChanged(getParent().getConfiguration());
        com.android.server.wm.ActivityRecord r = asActivityRecord();
        if (r != null && r.hasProcess()) {
            r.app.registerActivityConfigurationListener(r);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void prepareSurfaces() {
        this.mWindowTokenExt.updateSurfaceIfNeed();
        super.prepareSurfaces();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(android.content.res.Configuration newParentConfig) {
        this.mWindowTokenExt.onConfigurationChanged(newParentConfig);
        super.onConfigurationChanged(newParentConfig);
    }

    boolean hasAnimatingFixedRotationTransition() {
        if (this.mFixedRotationTransformState == null) {
            return false;
        }
        for (int i = this.mFixedRotationTransformState.mAssociatedTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.ActivityRecord r = this.mFixedRotationTransformState.mAssociatedTokens.get(i).asActivityRecord();
            if (r != null && r.inTransitionSelfOrParent() && !r.mDisplayContent.inTransition()) {
                return true;
            }
        }
        return false;
    }

    void finishFixedRotationTransform() {
        finishFixedRotationTransform(null);
    }

    void finishFixedRotationTransform(java.lang.Runnable applyDisplayRotation) {
        com.android.server.wm.WindowToken.FixedRotationTransformState state = this.mFixedRotationTransformState;
        if (state == null) {
            return;
        }
        android.util.Slog.w(TAG, "Finish fixed rotation transform of " + this + " applyDisplayRotation = " + applyDisplayRotation + "; callers:" + android.os.Debug.getCallers(com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM ? 10 : 6));
        this.mWindowTokenExt.recoveryFixedRotationConfig(this, this.mFixedRotationTransformState.mRotatedOverrideConfiguration);
        state.resetTransform();
        state.mIsTransforming = false;
        if (applyDisplayRotation != null) {
            applyDisplayRotation.run();
        }
        for (int i = state.mAssociatedTokens.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowToken token = state.mAssociatedTokens.get(i);
            token.mFixedRotationTransformState = null;
            if (applyDisplayRotation == null) {
                token.cancelFixedRotationTransform();
            }
        }
    }

    private void cancelFixedRotationTransform() {
        com.android.server.wm.WindowContainer<?> parent = getParent();
        if (parent == null) {
            return;
        }
        if (this.mTransitionController.isShellTransitionsEnabled() && asActivityRecord() != null && isVisible()) {
            com.android.server.wm.Transition transition = this.mTransitionController.getCollectingTransition();
            if (transition == null) {
                transition = this.mTransitionController.requestStartTransition(this.mTransitionController.createTransition(6), null, null, null);
            }
            transition.collect(this);
            transition.collectVisibleChange(this);
            transition.setReady(this.mDisplayContent, true);
        }
        int originalRotation = getWindowConfiguration().getRotation();
        onConfigurationChanged(parent.getConfiguration());
        onCancelFixedRotationTransform(originalRotation);
    }

    android.view.SurfaceControl getOrCreateFixedRotationLeash(android.view.SurfaceControl.Transaction t) {
        if (!this.mTransitionController.isShellTransitionsEnabled()) {
            return null;
        }
        int rotation = getRelativeDisplayRotation();
        if (rotation != 0 && this.mFixedRotationTransformLeash == null) {
            android.view.SurfaceControl leash = makeSurface().setContainerLayer().setParent(getParentSurfaceControl()).setName(getSurfaceControl() + " - rotation-leash").setHidden(false).setCallsite("WindowToken.getOrCreateFixedRotationLeash").build();
            t.setPosition(leash, this.mLastSurfacePosition.x, this.mLastSurfacePosition.y);
            t.reparent(getSurfaceControl(), leash);
            getPendingTransaction().setFixedTransformHint(leash, getWindowConfiguration().getDisplayRotation());
            this.mFixedRotationTransformLeash = leash;
            updateSurfaceRotation(t, rotation, this.mFixedRotationTransformLeash);
            return this.mFixedRotationTransformLeash;
        }
        return this.mFixedRotationTransformLeash;
    }

    android.view.SurfaceControl getFixedRotationLeash() {
        return this.mFixedRotationTransformLeash;
    }

    void removeFixedRotationLeash() {
        if (this.mFixedRotationTransformLeash == null) {
            return;
        }
        android.view.SurfaceControl.Transaction t = getSyncTransaction();
        if (this.mSurfaceControl != null) {
            t.reparent(this.mSurfaceControl, getParentSurfaceControl());
        }
        t.remove(this.mFixedRotationTransformLeash);
        this.mFixedRotationTransformLeash = null;
    }

    void onCancelFixedRotationTransform(int originalDisplayRotation) {
    }

    @Override // com.android.server.wm.ConfigurationContainer
    void resolveOverrideConfiguration(android.content.res.Configuration newParentConfig) {
        com.android.server.wm.WindowState win;
        super.resolveOverrideConfiguration(newParentConfig);
        if (isFixedRotationTransforming()) {
            getResolvedOverrideConfiguration().updateFrom(this.mFixedRotationTransformState.mRotatedOverrideConfiguration);
        }
        if (asActivityRecord() == null && (win = getTopChild()) != null) {
            android.content.res.Configuration resolvedConfig = getResolvedOverrideConfiguration();
            win.applySizeOverride(newParentConfig, resolvedConfig);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    void updateSurfacePosition(android.view.SurfaceControl.Transaction t) {
        com.android.server.wm.ActivityRecord r = asActivityRecord();
        if (r != null && r.isConfigurationDispatchPaused()) {
            return;
        }
        super.updateSurfacePosition(t);
        if (!this.mTransitionController.isShellTransitionsEnabled() && isFixedRotationTransforming()) {
            com.android.server.wm.Task rootTask = r != null ? r.getRootTask() : null;
            if (rootTask == null || !rootTask.inPinnedWindowingMode()) {
                this.mFixedRotationTransformState.transform(this);
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer
    protected void updateSurfaceRotation(android.view.SurfaceControl.Transaction t, int deltaRotation, android.view.SurfaceControl positionLeash) {
        com.android.server.wm.Task rootTask;
        com.android.server.wm.ActivityRecord r = asActivityRecord();
        if (r != null && (rootTask = r.getRootTask()) != null && this.mTransitionController.getWindowingModeAtStart(rootTask) == 2) {
            return;
        }
        super.updateSurfaceRotation(t, deltaRotation, positionLeash);
    }

    @Override // com.android.server.wm.WindowContainer
    void resetSurfacePositionForAnimationLeash(android.view.SurfaceControl.Transaction t) {
        if (!isFixedRotationTransforming()) {
            super.resetSurfacePositionForAnimationLeash(t);
        }
    }

    @Override // com.android.server.wm.WindowContainer
    boolean prepareSync() {
        if (this.mDisplayContent != null && this.mDisplayContent.isRotationChanging() && com.android.server.wm.AsyncRotationController.canBeAsync(this)) {
            return false;
        }
        return super.prepareSync();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, int logLevel) {
        if (logLevel == 2 && !isVisible()) {
            return;
        }
        long token = proto.start(fieldId);
        super.dumpDebug(proto, 1146756268033L, logLevel);
        proto.write(1120986464258L, java.lang.System.identityHashCode(this));
        proto.write(1133871366150L, this.paused);
        proto.end(token);
    }

    @Override // com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268039L;
    }

    @Override // com.android.server.wm.WindowContainer
    void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        super.dump(pw, prefix, dumpAll);
        pw.print(prefix);
        pw.print("windows=");
        pw.println(this.mChildren);
        pw.print(prefix);
        pw.print("windowType=");
        pw.print(this.windowType);
        pw.println();
        if (hasFixedRotationTransform()) {
            pw.print(prefix);
            pw.print("fixedRotationConfig=");
            pw.println(this.mFixedRotationTransformState.mRotatedOverrideConfiguration);
        }
    }

    public java.lang.String toString() {
        if (this.stringName == null) {
            this.stringName = "WindowToken{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " type=" + this.windowType + " " + this.token + "}";
        }
        return this.stringName;
    }

    @Override // com.android.server.wm.ConfigurationContainer
    java.lang.String getName() {
        return toString();
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.WindowToken asWindowToken() {
        return this;
    }

    int getWindowLayerFromType() {
        return this.mWmService.mPolicy.getWindowLayerFromTypeLw(this.windowType, this.mOwnerCanManageAppTokens, this.mRoundedCornerOverlay);
    }

    boolean isFromClient() {
        return this.mFromClientToken;
    }

    void setInsetsFrozen(final boolean freeze) {
        forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowToken$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$setInsetsFrozen$1(freeze, (com.android.server.wm.WindowState) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setInsetsFrozen$1(boolean freeze, com.android.server.wm.WindowState w) {
        if (w.mToken == this) {
            if (freeze) {
                w.freezeInsetsState();
            } else {
                w.clearFrozenInsetsState();
            }
        }
    }

    @Override // com.android.server.wm.WindowContainer
    int getWindowType() {
        return this.windowType;
    }

    static class Builder {
        private com.android.server.wm.DisplayContent mDisplayContent;
        private boolean mFromClientToken;
        private android.os.Bundle mOptions;
        private boolean mOwnerCanManageAppTokens;
        private boolean mPersistOnEmpty;
        private boolean mRoundedCornerOverlay;
        private final com.android.server.wm.WindowManagerService mService;
        private final android.os.IBinder mToken;
        private final int mType;

        Builder(com.android.server.wm.WindowManagerService service, android.os.IBinder token, int type) {
            this.mService = service;
            this.mToken = token;
            this.mType = type;
        }

        com.android.server.wm.WindowToken.Builder setPersistOnEmpty(boolean persistOnEmpty) {
            this.mPersistOnEmpty = persistOnEmpty;
            return this;
        }

        com.android.server.wm.WindowToken.Builder setDisplayContent(com.android.server.wm.DisplayContent dc) {
            this.mDisplayContent = dc;
            return this;
        }

        com.android.server.wm.WindowToken.Builder setOwnerCanManageAppTokens(boolean ownerCanManageAppTokens) {
            this.mOwnerCanManageAppTokens = ownerCanManageAppTokens;
            return this;
        }

        com.android.server.wm.WindowToken.Builder setRoundedCornerOverlay(boolean roundedCornerOverlay) {
            this.mRoundedCornerOverlay = roundedCornerOverlay;
            return this;
        }

        com.android.server.wm.WindowToken.Builder setFromClientToken(boolean fromClientToken) {
            this.mFromClientToken = fromClientToken;
            return this;
        }

        com.android.server.wm.WindowToken.Builder setOptions(android.os.Bundle options) {
            this.mOptions = options;
            return this;
        }

        com.android.server.wm.WindowToken build() {
            return new com.android.server.wm.WindowToken(this.mService, this.mToken, this.mType, this.mPersistOnEmpty, this.mDisplayContent, this.mOwnerCanManageAppTokens, this.mRoundedCornerOverlay, this.mFromClientToken, this.mOptions);
        }
    }
}
