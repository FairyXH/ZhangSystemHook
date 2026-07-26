package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class InsetsPolicy {
    public static final int CONTROLLABLE_TYPES = (android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars()) | android.view.WindowInsets.Type.ime();
    private static final boolean DBG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final java.lang.String TAG = "WindowManager";
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private com.android.server.wm.InsetsControlTarget mFakeNavControlTarget;
    private com.android.server.wm.InsetsControlTarget mFakeStatusControlTarget;
    private com.android.server.wm.WindowState mFocusedWin;
    private int mForcedShowingTypes;
    private final boolean mHideNavBarForKeyboard;
    private final com.android.server.wm.InsetsControlTarget mPermanentControlTarget;
    private final com.android.server.wm.DisplayPolicy mPolicy;
    private int mShowingTransientTypes;
    private final com.android.server.wm.InsetsStateController mStateController;
    private final com.android.server.wm.InsetsControlTarget mTransientControlTarget;
    public com.android.server.wm.IInsetsPolicyExt insetsPolicyExt = (com.android.server.wm.IInsetsPolicyExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IInsetsPolicyExt.class).base(this).create();
    private final com.android.server.wm.InsetsPolicy.BarWindow mStatusBar = new com.android.server.wm.InsetsPolicy.BarWindow(1);
    private final com.android.server.wm.InsetsPolicy.BarWindow mNavBar = new com.android.server.wm.InsetsPolicy.BarWindow(2);

    InsetsPolicy(com.android.server.wm.InsetsStateController stateController, com.android.server.wm.DisplayContent displayContent) {
        this.mStateController = stateController;
        this.mDisplayContent = displayContent;
        this.mPolicy = displayContent.getDisplayPolicy();
        android.content.res.Resources r = this.mPolicy.getContext().getResources();
        this.mHideNavBarForKeyboard = r.getBoolean(android.R.bool.config_focusScrollContainersInTouchMode);
        this.mTransientControlTarget = new com.android.server.wm.InsetsPolicy.ControlTarget(displayContent, "TransientControlTarget");
        this.mPermanentControlTarget = new com.android.server.wm.InsetsPolicy.ControlTarget(displayContent, "PermanentControlTarget");
    }

    void updateBarControlTarget(com.android.server.wm.WindowState focusedWin) {
        com.android.server.wm.InsetsControlTarget statusControlTarget;
        if (this.mFocusedWin != focusedWin) {
            abortTransient();
        }
        this.mFocusedWin = focusedWin;
        com.android.server.wm.WindowState notificationShade = this.mPolicy.getNotificationShade();
        com.android.server.wm.WindowState topApp = this.mPolicy.getTopFullscreenOpaqueWindow();
        com.android.server.wm.InsetsControlTarget statusControlTarget2 = getStatusControlTarget(focusedWin, false);
        com.android.server.wm.InsetsControlTarget navControlTarget = null;
        if (statusControlTarget2 == this.mTransientControlTarget) {
            statusControlTarget = getStatusControlTarget(focusedWin, true);
        } else if (statusControlTarget2 == notificationShade) {
            statusControlTarget = getStatusControlTarget(topApp, true);
        } else {
            statusControlTarget = null;
        }
        this.mFakeStatusControlTarget = statusControlTarget;
        com.android.server.wm.InsetsControlTarget navControlTarget2 = getNavControlTarget(focusedWin, false);
        if (navControlTarget2 == this.mTransientControlTarget) {
            navControlTarget = getNavControlTarget(focusedWin, true);
        } else if (navControlTarget2 == notificationShade) {
            navControlTarget = getNavControlTarget(topApp, true);
        }
        this.mFakeNavControlTarget = navControlTarget;
        this.mStateController.onBarControlTargetChanged(statusControlTarget2, this.mFakeStatusControlTarget, navControlTarget2, this.mFakeNavControlTarget);
        this.mStatusBar.updateVisibility(statusControlTarget2, android.view.WindowInsets.Type.statusBars());
        this.mNavBar.updateVisibility(navControlTarget2, android.view.WindowInsets.Type.navigationBars());
    }

    boolean hasHiddenSources(int types) {
        android.view.InsetsState state = this.mStateController.getRawInsetsState();
        for (int i = state.sourceSize() - 1; i >= 0; i--) {
            android.view.InsetsSource source = state.sourceAt(i);
            if (source != null && (source.getType() & types) != 0 && !source.getFrame().isEmpty() && !source.isVisible()) {
                return true;
            }
        }
        return false;
    }

    void showTransient(int types, boolean isGestureOnSystemBar) {
        if (this.insetsPolicyExt.showTransient()) {
            android.util.Slog.d(TAG, "Zen mode, abort showTransient");
            return;
        }
        int showingTransientTypes = this.mShowingTransientTypes;
        android.view.InsetsState rawState = this.mStateController.getRawInsetsState();
        for (int i = rawState.sourceSize() - 1; i >= 0; i--) {
            android.view.InsetsSource source = rawState.sourceAt(i);
            if (!source.isVisible()) {
                int type = source.getType();
                if ((source.getType() & types) != 0) {
                    showingTransientTypes |= type;
                }
            }
        }
        android.util.Slog.d(TAG, "request show transient bar, mShowingTransientTypes = " + this.mShowingTransientTypes + " showingTransientTypes = " + showingTransientTypes);
        if (this.mShowingTransientTypes != showingTransientTypes) {
            this.mShowingTransientTypes = showingTransientTypes;
            com.android.server.statusbar.StatusBarManagerInternal statusBarManagerInternal = this.mPolicy.getStatusBarManagerInternal();
            if (statusBarManagerInternal != null) {
                statusBarManagerInternal.showTransient(this.mDisplayContent.getDisplayId(), showingTransientTypes, isGestureOnSystemBar);
            }
            updateBarControlTarget(this.mFocusedWin);
            dispatchTransientSystemBarsVisibilityChanged(this.mFocusedWin, ((android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars()) & showingTransientTypes) != 0, isGestureOnSystemBar);
        }
    }

    com.android.server.wm.InsetsControlTarget getTransientControlTarget() {
        return this.mTransientControlTarget;
    }

    com.android.server.wm.InsetsControlTarget getPermanentControlTarget() {
        return this.mPermanentControlTarget;
    }

    void hideTransient() {
        if (this.mShowingTransientTypes == 0) {
            return;
        }
        dispatchTransientSystemBarsVisibilityChanged(this.mFocusedWin, false, false);
        this.mShowingTransientTypes = 0;
        updateBarControlTarget(this.mFocusedWin);
    }

    boolean isTransient(int type) {
        return (this.mShowingTransientTypes & type) != 0;
    }

    android.view.InsetsState adjustInsetsForWindow(com.android.server.wm.WindowState target, android.view.InsetsState originalState, boolean includesTransient) {
        android.view.InsetsState state;
        if (!includesTransient) {
            state = adjustVisibilityForFakeControllingSources(originalState);
        } else {
            state = originalState;
        }
        android.view.InsetsState state2 = adjustVisibilityForIme(target, state, state == originalState);
        return adjustInsetsForRoundedCorners(target.mToken, state2, state2 == originalState);
    }

    android.view.InsetsState adjustInsetsForWindow(com.android.server.wm.WindowState target, android.view.InsetsState originalState) {
        return adjustInsetsForWindow(target, originalState, false);
    }

    void getInsetsForWindowMetrics(com.android.server.wm.WindowToken token, android.view.InsetsState outInsetsState) {
        android.view.InsetsState srcState;
        if (token != null && token.isFixedRotationTransforming()) {
            srcState = token.getFixedRotationTransformInsetsState();
        } else {
            srcState = this.mStateController.getRawInsetsState();
        }
        outInsetsState.set(srcState, true);
        for (int i = outInsetsState.sourceSize() - 1; i >= 0; i--) {
            android.view.InsetsSource source = outInsetsState.sourceAt(i);
            if (isTransient(source.getType())) {
                source.setVisible(false);
            }
        }
        adjustInsetsForRoundedCorners(token, outInsetsState, false);
        if (token != null && token.hasSizeCompatBounds()) {
            outInsetsState.scale(1.0f / token.getCompatScale());
            if (token instanceof com.android.server.wm.ActivityRecord) {
                float extraCompatScale = ((com.android.server.wm.ActivityRecord) token).getWrapper().getExtImpl().getCompatScaleInOplusCompatMode();
                if (extraCompatScale != 1.0f) {
                    outInsetsState.scale(1.0f / extraCompatScale);
                }
            }
        }
    }

    android.view.InsetsState enforceInsetsPolicyForTarget(android.view.WindowManager.LayoutParams attrs, int windowingMode, boolean isAlwaysOnTop, android.view.InsetsState state) {
        if (attrs.type == 2011) {
            state = new android.view.InsetsState(state);
            state.removeSource(android.view.InsetsSource.ID_IME);
        } else if (attrs.providedInsets != null) {
            for (android.view.InsetsFrameProvider provider : attrs.providedInsets) {
                if ((provider.getType() & android.view.WindowInsets.Type.systemBars()) != 0) {
                    if (state == state) {
                        state = new android.view.InsetsState(state);
                    }
                    android.view.InsetsSource insetsSource = state.peekSource(provider.getId());
                    if (insetsSource != null) {
                        state.removeSource(provider.getId());
                    } else {
                        state.getWrapper().getExtImpl().removeSourceByType(provider.getType());
                    }
                }
            }
        }
        if (!attrs.isFullscreen() || attrs.getFitInsetsTypes() != 0) {
            if (state == state) {
                state = new android.view.InsetsState(state);
            }
            for (int i = state.sourceSize() - 1; i >= 0; i--) {
                if (state.sourceAt(i).getType() == android.view.WindowInsets.Type.captionBar()) {
                    state.removeSourceAt(i);
                }
            }
        }
        android.util.SparseArray<com.android.server.wm.InsetsSourceProvider> providers = this.mStateController.getSourceProviders();
        int windowType = attrs.type;
        for (int i2 = providers.size() - 1; i2 >= 0; i2--) {
            com.android.server.wm.InsetsSourceProvider otherProvider = providers.valueAt(i2);
            if (otherProvider.overridesFrame(windowType)) {
                if (state == state) {
                    state = new android.view.InsetsState(state);
                }
                android.view.InsetsSource override = new android.view.InsetsSource(otherProvider.getSource());
                override.setFrame(otherProvider.getOverriddenFrame(windowType));
                state.addSource(override);
            }
        }
        if (android.app.WindowConfiguration.isFloating(windowingMode) || (windowingMode == 6 && isAlwaysOnTop)) {
            int types = android.view.WindowInsets.Type.captionBar();
            if (windowingMode != 2) {
                types |= android.view.WindowInsets.Type.ime();
            }
            android.view.InsetsState newState = new android.view.InsetsState();
            newState.set(state, types);
            state = newState;
        }
        if (this.insetsPolicyExt.hasFoldRemapDisplayDisableFeature()) {
            this.insetsPolicyExt.removeSource(state, this.mDisplayContent);
        }
        return state;
    }

    private android.view.InsetsState adjustVisibilityForFakeControllingSources(android.view.InsetsState originalState) {
        if (this.mFakeStatusControlTarget == null && this.mFakeNavControlTarget == null) {
            return originalState;
        }
        android.view.InsetsState state = originalState;
        for (int i = state.sourceSize() - 1; i >= 0; i--) {
            android.view.InsetsSource source = state.sourceAt(i);
            state = adjustVisibilityForFakeControllingSource(adjustVisibilityForFakeControllingSource(state, android.view.WindowInsets.Type.statusBars(), source, this.mFakeStatusControlTarget), android.view.WindowInsets.Type.navigationBars(), source, this.mFakeNavControlTarget);
        }
        return state;
    }

    private static android.view.InsetsState adjustVisibilityForFakeControllingSource(android.view.InsetsState originalState, int type, android.view.InsetsSource source, com.android.server.wm.InsetsControlTarget target) {
        boolean isRequestedVisible;
        if (source.getType() != type || target == null || source.isVisible() == (isRequestedVisible = target.isRequestedVisible(type))) {
            return originalState;
        }
        android.view.InsetsState state = new android.view.InsetsState(originalState);
        android.view.InsetsSource outSource = new android.view.InsetsSource(source);
        outSource.setVisible(isRequestedVisible);
        state.addSource(outSource);
        return state;
    }

    private android.view.InsetsState adjustVisibilityForIme(com.android.server.wm.WindowState w, android.view.InsetsState originalState, boolean copyState) {
        android.view.InsetsSource originalImeSource;
        android.view.InsetsState state;
        android.view.InsetsState state2;
        if (w.mIsImWindow) {
            android.view.InsetsState state3 = originalState;
            boolean navVisible = !this.mHideNavBarForKeyboard;
            for (int i = originalState.sourceSize() - 1; i >= 0; i--) {
                android.view.InsetsSource source = originalState.sourceAt(i);
                if (source.getType() == android.view.WindowInsets.Type.navigationBars() && source.isVisible() != navVisible) {
                    if (state3 == originalState && copyState) {
                        state3 = new android.view.InsetsState(originalState);
                    }
                    android.view.InsetsSource navSource = new android.view.InsetsSource(source);
                    navSource.setVisible(navVisible);
                    state3.addSource(navSource);
                }
            }
            return state3;
        }
        if (w.mActivityRecord != null && w.mActivityRecord.mImeInsetsFrozenUntilStartInput) {
            android.view.InsetsSource originalImeSource2 = originalState.peekSource(android.view.InsetsSource.ID_IME);
            if (originalImeSource2 != null) {
                boolean imeVisibility = w.isRequestedVisible(android.view.WindowInsets.Type.ime());
                if (copyState) {
                    state2 = new android.view.InsetsState(originalState);
                } else {
                    state2 = originalState;
                }
                android.view.InsetsSource imeSource = new android.view.InsetsSource(originalImeSource2);
                imeSource.setVisible(imeVisibility);
                state2.addSource(imeSource);
                return state2;
            }
        } else if (w.mImeInsetsConsumed && (originalImeSource = originalState.peekSource(android.view.InsetsSource.ID_IME)) != null && originalImeSource.isVisible()) {
            if (copyState) {
                state = new android.view.InsetsState(originalState);
            } else {
                state = originalState;
            }
            android.view.InsetsSource imeSource2 = new android.view.InsetsSource(originalImeSource);
            imeSource2.setVisible(false);
            state.addSource(imeSource2);
            return state;
        }
        return originalState;
    }

    private android.view.InsetsState adjustInsetsForRoundedCorners(com.android.server.wm.WindowToken token, android.view.InsetsState originalState, boolean copyState) {
        android.graphics.Rect bounds;
        if (token != null) {
            com.android.server.wm.ActivityRecord activityRecord = token.asActivityRecord();
            com.android.server.wm.Task task = activityRecord != null ? activityRecord.getTask() : null;
            if (task != null && !task.getWindowConfiguration().tasksAreFloating()) {
                android.view.InsetsState state = copyState ? new android.view.InsetsState(originalState) : originalState;
                if (token.isFixedRotationTransforming()) {
                    bounds = token.getFixedRotationTransformDisplayBounds();
                } else {
                    bounds = task.getBounds();
                }
                state.setRoundedCornerFrame(bounds);
                return state;
            }
        }
        return originalState;
    }

    void onRequestedVisibleTypesChanged(com.android.server.wm.InsetsControlTarget caller) {
        this.mStateController.onRequestedVisibleTypesChanged(caller);
        checkAbortTransient(caller);
        updateBarControlTarget(this.mFocusedWin);
    }

    private void checkAbortTransient(com.android.server.wm.InsetsControlTarget caller) {
        if (this.mShowingTransientTypes == 0) {
            return;
        }
        boolean isImeVisible = this.mStateController.getImeSourceProvider().isClientVisible();
        int fakeControllingTypes = this.mStateController.getFakeControllingTypes(caller);
        int abortTypes = (caller.getRequestedVisibleTypes() & fakeControllingTypes) | (isImeVisible ? android.view.WindowInsets.Type.navigationBars() : 0);
        this.mShowingTransientTypes &= ~abortTypes;
        if (abortTypes != 0) {
            this.mDisplayContent.setLayoutNeeded();
            this.mDisplayContent.mWmService.requestTraversal();
            com.android.server.statusbar.StatusBarManagerInternal statusBarManager = this.mPolicy.getStatusBarManagerInternal();
            if (statusBarManager != null) {
                statusBarManager.abortTransient(this.mDisplayContent.getDisplayId(), abortTypes);
            }
        }
    }

    private void abortTransient() {
        if (this.mShowingTransientTypes == 0) {
            return;
        }
        com.android.server.statusbar.StatusBarManagerInternal statusBarManager = this.mPolicy.getStatusBarManagerInternal();
        if (statusBarManager != null) {
            statusBarManager.abortTransient(this.mDisplayContent.getDisplayId(), this.mShowingTransientTypes);
        }
        this.mShowingTransientTypes = 0;
        this.mDisplayContent.setLayoutNeeded();
        this.mDisplayContent.mWmService.requestTraversal();
        dispatchTransientSystemBarsVisibilityChanged(this.mFocusedWin, false, false);
    }

    private com.android.server.wm.InsetsControlTarget getStatusControlTarget(com.android.server.wm.WindowState focusedWin, boolean fake) {
        if (!fake && isTransient(android.view.WindowInsets.Type.statusBars())) {
            return this.mTransientControlTarget;
        }
        com.android.server.wm.WindowState notificationShade = this.mPolicy.getNotificationShade();
        if (focusedWin == notificationShade) {
            return focusedWin;
        }
        if (remoteInsetsControllerControlsSystemBars(focusedWin)) {
            android.content.ComponentName component = focusedWin.mActivityRecord != null ? focusedWin.mActivityRecord.mActivityComponent : null;
            this.mDisplayContent.mRemoteInsetsControlTarget.topFocusedWindowChanged(component, focusedWin.getRequestedVisibleTypes());
            return this.mDisplayContent.mRemoteInsetsControlTarget;
        }
        if (areTypesForciblyShowing(android.view.WindowInsets.Type.statusBars()) && this.insetsPolicyExt.shouldForceShowStatusBar(this.mDisplayContent)) {
            return this.mPermanentControlTarget;
        }
        com.android.server.wm.IFlexibleWindowManagerExt fwmExt = this.mDisplayContent.mWmService.mAtmService.getWrapper().getFlexibleExtImpl();
        if (fwmExt.isInPocketStudio(this.mDisplayContent.getDisplayId()) && !fwmExt.needInterceptControlTargetForFlexiblePort()) {
            return this.insetsPolicyExt.getContainerWindow(focusedWin, this.mDisplayContent, true);
        }
        com.android.server.wm.WindowState splitWin = this.insetsPolicyExt.getStatusControlTargetInSplit(focusedWin);
        if (splitWin != null && !this.insetsPolicyExt.shouldForceShowStatusBar(this.mDisplayContent)) {
            return splitWin;
        }
        if (this.mPolicy.areTypesForciblyShownTransiently(android.view.WindowInsets.Type.statusBars()) && !fake) {
            return this.mTransientControlTarget;
        }
        if ((!canBeTopFullscreenOpaqueWindow(focusedWin) && this.mPolicy.topAppHidesSystemBar(android.view.WindowInsets.Type.statusBars()) && (notificationShade == null || !notificationShade.canReceiveKeys())) || this.insetsPolicyExt.isWindowingZoomMode(focusedWin) || this.insetsPolicyExt.isFlexibleTaskIgnoreSysBar(focusedWin)) {
            return this.mPolicy.getTopFullscreenOpaqueWindow();
        }
        return focusedWin;
    }

    private static boolean canBeTopFullscreenOpaqueWindow(com.android.server.wm.WindowState win) {
        boolean nonAttachedAppWindow = win != null && win.mAttrs.type >= 1 && win.mAttrs.type <= 99;
        return nonAttachedAppWindow && win.mAttrs.isFullscreen() && !win.isFullyTransparent() && !win.inMultiWindowMode();
    }

    private com.android.server.wm.InsetsControlTarget getNavControlTarget(com.android.server.wm.WindowState focusedWin, boolean fake) {
        com.android.server.wm.WindowState topFullscreenOpaqueWindowState;
        com.android.server.wm.InsetsSourceProvider provider;
        com.android.server.wm.WindowState imeWin = this.mDisplayContent.mInputMethodWindow;
        if (imeWin != null && imeWin.isVisible() && !this.mHideNavBarForKeyboard) {
            return this.mPermanentControlTarget;
        }
        if (!fake && isTransient(android.view.WindowInsets.Type.navigationBars())) {
            return this.mTransientControlTarget;
        }
        if (focusedWin == this.mPolicy.getNotificationShade()) {
            if (focusedWin != null && focusedWin.getDisplayContent() != null && focusedWin.getDisplayContent().isKeyguardGoingAway() && this.mDisplayContent.mFocusedApp != null) {
                com.android.server.wm.WindowState appWin = this.mDisplayContent.mFocusedApp.findMainWindow(false);
                if (appWin == null) {
                    android.util.Slog.d(TAG, "getNavControlTarget skip focus on NotificationShade for going away, appWin==null");
                }
                return appWin;
            }
            return focusedWin;
        }
        if (focusedWin != null && (provider = focusedWin.getControllableInsetProvider()) != null && provider.getSource().getType() == android.view.WindowInsets.Type.navigationBars()) {
            return focusedWin;
        }
        if (remoteInsetsControllerControlsSystemBars(focusedWin)) {
            android.content.ComponentName component = focusedWin.mActivityRecord != null ? focusedWin.mActivityRecord.mActivityComponent : null;
            this.mDisplayContent.mRemoteInsetsControlTarget.topFocusedWindowChanged(component, focusedWin.getRequestedVisibleTypes());
            return this.mDisplayContent.mRemoteInsetsControlTarget;
        }
        if (areTypesForciblyShowing(android.view.WindowInsets.Type.navigationBars())) {
            return this.mPermanentControlTarget;
        }
        if (this.mPolicy.areTypesForciblyShownTransiently(android.view.WindowInsets.Type.navigationBars()) && !fake) {
            return this.mTransientControlTarget;
        }
        if (!this.mPolicy.topAppHidesSystemBar(android.view.WindowInsets.Type.navigationBars()) && ((this.insetsPolicyExt.isWindowingZoomMode(focusedWin) || this.insetsPolicyExt.isFlexibleTaskIgnoreSysBar(focusedWin)) && (topFullscreenOpaqueWindowState = this.mPolicy.getTopFullscreenOpaqueWindow()) != null)) {
            return topFullscreenOpaqueWindowState;
        }
        if (this.insetsPolicyExt.shouldTopFullOpqWinForceCtrlNavBar(focusedWin)) {
            return this.mPolicy.getTopFullscreenOpaqueWindow();
        }
        if (this.mDisplayContent.mWmService.mAtmService.getWrapper().getFlexibleExtImpl().isInPocketStudio(this.mDisplayContent.getDisplayId())) {
            return this.insetsPolicyExt.getContainerWindow(focusedWin, this.mDisplayContent, false);
        }
        if (this.mPolicy.getWrapper().getExtImpl().isForceShowNavbar()) {
            return null;
        }
        return focusedWin;
    }

    boolean areTypesForciblyShowing(int types) {
        return (this.mForcedShowingTypes & types) == types;
    }

    void updateSystemBars(com.android.server.wm.WindowState win, boolean inSplitScreenMode, boolean inFreeformMode) {
        int iStatusBars;
        int iStatusBars2 = 0;
        if (inSplitScreenMode || inFreeformMode) {
            iStatusBars = android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars();
        } else if (forceShowingNavigationBars(win)) {
            iStatusBars = android.view.WindowInsets.Type.navigationBars();
        } else {
            iStatusBars = 0;
        }
        this.mForcedShowingTypes = iStatusBars;
        com.android.server.wm.InsetsStateController insetsStateController = this.mStateController;
        int i = this.mForcedShowingTypes;
        if (remoteInsetsControllerControlsSystemBars(win)) {
            iStatusBars2 = android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars();
        }
        insetsStateController.setForcedConsumingTypes(iStatusBars2 | i);
        updateBarControlTarget(win);
    }

    private boolean forceShowingNavigationBars(com.android.server.wm.WindowState win) {
        return this.mPolicy.isForceShowNavigationBarEnabled() && win != null && win.getActivityType() == 1;
    }

    boolean remoteInsetsControllerControlsSystemBars(com.android.server.wm.WindowState focusedWin) {
        return focusedWin != null && this.mPolicy.isRemoteInsetsControllerControllingSystemBars() && this.mDisplayContent != null && this.mDisplayContent.mRemoteInsetsControlTarget != null && focusedWin.getAttrs().type >= 1 && focusedWin.getAttrs().type <= 99;
    }

    private void dispatchTransientSystemBarsVisibilityChanged(com.android.server.wm.WindowState focusedWindow, boolean areVisible, boolean wereRevealedFromSwipeOnSystemBar) {
        com.android.server.wm.Task task;
        if (focusedWindow == null || (task = focusedWindow.getTask()) == null) {
            return;
        }
        int taskId = task.mTaskId;
        boolean isValidTaskId = taskId != -1;
        if (!isValidTaskId) {
            return;
        }
        this.mDisplayContent.mWmService.mTaskSystemBarsListenerController.dispatchTransientSystemBarVisibilityChanged(taskId, areVisible, wereRevealedFromSwipeOnSystemBar);
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.println(prefix + "InsetsPolicy");
        java.lang.String prefix2 = prefix + "  ";
        pw.println(prefix2 + "status: " + android.app.StatusBarManager.windowStateToString(this.mStatusBar.mState));
        pw.println(prefix2 + "nav: " + android.app.StatusBarManager.windowStateToString(this.mNavBar.mState));
        if (this.mShowingTransientTypes != 0) {
            pw.println(prefix2 + "mShowingTransientTypes=" + android.view.WindowInsets.Type.toString(this.mShowingTransientTypes));
        }
        if (this.mForcedShowingTypes != 0) {
            pw.println(prefix2 + "mForcedShowingTypes=" + android.view.WindowInsets.Type.toString(this.mForcedShowingTypes));
        }
    }

    private class BarWindow {
        private final int mId;
        private int mState = 0;

        BarWindow(int id) {
            this.mId = id;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateVisibility(com.android.server.wm.InsetsControlTarget controlTarget, int type) {
            setVisible(controlTarget == null || controlTarget.isRequestedVisible(type));
        }

        private void setVisible(boolean visible) {
            int state = visible ? 0 : 2;
            if (this.mState != state) {
                android.util.Slog.d(com.android.server.wm.InsetsPolicy.TAG, "BarWindow--setVisible: id=" + this.mId + ", oldState=" + this.mState + ", state=" + state + ", mFocuseWindow= " + com.android.server.wm.InsetsPolicy.this.mFocusedWin + " call: " + (com.android.server.wm.InsetsPolicy.DBG ? android.os.Debug.getCallers(8) : ""));
                this.mState = state;
                com.android.server.statusbar.StatusBarManagerInternal statusBarManagerInternal = com.android.server.wm.InsetsPolicy.this.mPolicy.getStatusBarManagerInternal();
                if (statusBarManagerInternal != null) {
                    statusBarManagerInternal.setWindowState(com.android.server.wm.InsetsPolicy.this.mDisplayContent.getDisplayId(), this.mId, state);
                    com.android.server.wm.InsetsPolicy.this.mPolicy.getWrapper().getExtImpl().notifyWindowStateChanged(this.mId, state, com.android.server.wm.InsetsPolicy.this.mDisplayContent.getDisplayId());
                }
            }
        }
    }

    private static class ControlTarget implements com.android.server.wm.InsetsControlTarget, java.lang.Runnable {
        private final java.lang.Object mGlobalLock;
        private final android.os.Handler mHandler;
        private final android.view.InsetsController mInsetsController;
        private final java.lang.String mName;
        private final android.view.InsetsState mState = new android.view.InsetsState();
        private final com.android.server.wm.InsetsStateController mStateController;

        ControlTarget(com.android.server.wm.DisplayContent displayContent, java.lang.String name) {
            this.mHandler = displayContent.mWmService.mH;
            this.mGlobalLock = displayContent.mWmService.mGlobalLock;
            this.mStateController = displayContent.getInsetsStateController();
            this.mInsetsController = new android.view.InsetsController(new com.android.server.wm.InsetsPolicy.Host(this.mHandler, name));
            this.mName = name;
        }

        @Override // com.android.server.wm.InsetsControlTarget
        public void notifyInsetsControlChanged(int displayId) {
            this.mHandler.post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this.mGlobalLock) {
                this.mState.set(this.mStateController.getRawInsetsState(), true);
                this.mInsetsController.onStateChanged(this.mState);
                this.mInsetsController.onControlsChanged(this.mStateController.getControlsForDispatch(this));
            }
        }

        public java.lang.String toString() {
            return this.mName;
        }
    }

    private static class Host implements android.view.InsetsController.Host {
        private final android.os.Handler mHandler;
        private boolean mInsetsAnimationRunning;
        private final java.lang.String mName;
        private final float[] mTmpFloat9 = new float[9];

        Host(android.os.Handler handler, java.lang.String name) {
            this.mHandler = handler;
            this.mName = name;
        }

        public android.os.Handler getHandler() {
            return this.mHandler;
        }

        public void notifyInsetsChanged() {
        }

        public void dispatchWindowInsetsAnimationPrepare(android.view.WindowInsetsAnimation animation) {
        }

        public android.view.WindowInsetsAnimation.Bounds dispatchWindowInsetsAnimationStart(android.view.WindowInsetsAnimation animation, android.view.WindowInsetsAnimation.Bounds bounds) {
            return bounds;
        }

        public android.view.WindowInsets dispatchWindowInsetsAnimationProgress(android.view.WindowInsets insets, java.util.List<android.view.WindowInsetsAnimation> runningAnimations) {
            return insets;
        }

        public void dispatchWindowInsetsAnimationEnd(android.view.WindowInsetsAnimation animation) {
        }

        public void applySurfaceParams(android.view.SyncRtSurfaceTransactionApplier.SurfaceParams... p) {
            android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction();
            for (int i = p.length - 1; i >= 0; i--) {
                android.view.SyncRtSurfaceTransactionApplier.applyParams(t, p[i], this.mTmpFloat9);
            }
            t.apply();
            t.close();
        }

        public void updateRequestedVisibleTypes(int types) {
        }

        public boolean hasAnimationCallbacks() {
            return false;
        }

        public void setSystemBarsAppearance(int appearance, int mask) {
        }

        public int getSystemBarsAppearance() {
            return 0;
        }

        public void setSystemBarsBehavior(int behavior) {
        }

        public int getSystemBarsBehavior() {
            return 2;
        }

        public void releaseSurfaceControlFromRt(android.view.SurfaceControl surfaceControl) {
            surfaceControl.release();
        }

        public void addOnPreDrawRunnable(java.lang.Runnable r) {
        }

        public void postInsetsAnimationCallback(java.lang.Runnable r) {
        }

        public android.view.inputmethod.InputMethodManager getInputMethodManager() {
            return null;
        }

        public java.lang.String getRootViewTitle() {
            return this.mName;
        }

        public int dipToPx(int dips) {
            return 0;
        }

        public android.os.IBinder getWindowToken() {
            return null;
        }

        public void notifyAnimationRunningStateChanged(boolean running) {
            this.mInsetsAnimationRunning = running;
        }
    }
}
