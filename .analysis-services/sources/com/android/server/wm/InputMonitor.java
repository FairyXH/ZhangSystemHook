package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class InputMonitor {
    public static final boolean RECENT_REF_TDA = android.os.SystemProperties.getBoolean("persist.wm.debug.recent_ref_tda", true);
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private int mDisplayHeight;
    private final int mDisplayId;
    private boolean mDisplayRemoved;
    private int mDisplayWidth;
    private final android.os.Handler mHandler;
    private final android.view.SurfaceControl.Transaction mInputTransaction;
    private final com.android.server.wm.WindowManagerService mService;
    private boolean mUpdateInputWindowsImmediately;
    private boolean mUpdateInputWindowsPending;
    android.os.IBinder mInputFocus = null;
    long mInputFocusRequestTimeMillis = 0;
    private boolean mUpdateInputWindowsNeeded = true;
    private final android.graphics.Region mTmpRegion = new android.graphics.Region();
    private final java.util.ArrayList<com.android.server.wm.InputConsumerImpl> mInputConsumers = new java.util.ArrayList<>();
    private java.lang.ref.WeakReference<com.android.server.wm.ActivityRecord> mActiveRecentsActivity = null;
    private java.lang.ref.WeakReference<com.android.server.wm.Task> mActiveRecentsLayerRef = null;
    private java.lang.ref.WeakReference<com.android.server.wm.TaskDisplayArea> mActiveRecentsLayerDisplayRef = null;
    private com.android.server.wm.IInputMonitorExt mInputMonitorExt = (com.android.server.wm.IInputMonitorExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IInputMonitorExt.class).base(this).create();
    private final com.android.server.wm.InputMonitor.UpdateInputWindows mUpdateInputWindows = new com.android.server.wm.InputMonitor.UpdateInputWindows();
    private final com.android.server.wm.InputMonitor.UpdateInputForAllWindowsConsumer mUpdateInputForAllWindowsConsumer = new com.android.server.wm.InputMonitor.UpdateInputForAllWindowsConsumer();

    private class UpdateInputWindows implements java.lang.Runnable {
        private UpdateInputWindows() {
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.InputMonitor.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.InputMonitor.this.mUpdateInputWindowsPending = false;
                    com.android.server.wm.InputMonitor.this.mUpdateInputWindowsNeeded = false;
                    if (com.android.server.wm.InputMonitor.this.mDisplayRemoved) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    boolean inDrag = com.android.server.wm.InputMonitor.this.mService.mDragDropController.dragDropActiveLocked();
                    com.android.server.wm.InputMonitor.this.mUpdateInputForAllWindowsConsumer.updateInputWindows(inDrag);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    InputMonitor(com.android.server.wm.WindowManagerService windowManagerService, com.android.server.wm.DisplayContent displayContent) {
        this.mService = windowManagerService;
        this.mDisplayContent = displayContent;
        this.mDisplayId = displayContent.getDisplayId();
        this.mInputTransaction = this.mService.mTransactionFactory.get();
        this.mHandler = this.mService.mAnimationHandler;
    }

    void onDisplayRemoved() {
        this.mHandler.removeCallbacks(this.mUpdateInputWindows);
        this.mService.mTransactionFactory.get().addWindowInfosReportedListener(new java.lang.Runnable() { // from class: com.android.server.wm.InputMonitor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onDisplayRemoved$0();
            }
        }).apply();
        this.mDisplayRemoved = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayRemoved$0() {
        this.mService.mInputManager.onDisplayRemoved(this.mDisplayId);
    }

    private void addInputConsumer(com.android.server.wm.InputConsumerImpl consumer) {
        this.mInputConsumers.add(consumer);
        consumer.linkToDeathRecipient();
        consumer.layout(this.mInputTransaction, this.mDisplayWidth, this.mDisplayHeight);
        updateInputWindowsLw(true);
    }

    boolean destroyInputConsumer(android.os.IBinder token) {
        for (int i = 0; i < this.mInputConsumers.size(); i++) {
            com.android.server.wm.InputConsumerImpl consumer = this.mInputConsumers.get(i);
            if (consumer != null && consumer.mToken == token) {
                consumer.disposeChannelsLw(this.mInputTransaction);
                this.mInputConsumers.remove(consumer);
                updateInputWindowsLw(true);
                return true;
            }
        }
        return false;
    }

    com.android.server.wm.InputConsumerImpl getInputConsumer(java.lang.String name) {
        for (int i = this.mInputConsumers.size() - 1; i >= 0; i--) {
            com.android.server.wm.InputConsumerImpl consumer = this.mInputConsumers.get(i);
            if (consumer.mName.equals(name)) {
                return consumer;
            }
        }
        return null;
    }

    void layoutInputConsumers(int dw, int dh) {
        if (this.mDisplayWidth == dw && this.mDisplayHeight == dh) {
            return;
        }
        this.mDisplayWidth = dw;
        this.mDisplayHeight = dh;
        try {
            android.os.Trace.traceBegin(32L, "layoutInputConsumer");
            for (int i = this.mInputConsumers.size() - 1; i >= 0; i--) {
                this.mInputConsumers.get(i).layout(this.mInputTransaction, dw, dh);
            }
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    void resetInputConsumers(android.view.SurfaceControl.Transaction t) {
        for (int i = this.mInputConsumers.size() - 1; i >= 0; i--) {
            this.mInputConsumers.get(i).hide(t);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0082  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void createInputConsumer(android.os.IBinder r16, java.lang.String r17, android.view.InputChannel r18, int r19, android.os.UserHandle r20) {
        /*
            Method dump skipped, instruction units count: 216
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.InputMonitor.createInputConsumer(android.os.IBinder, java.lang.String, android.view.InputChannel, int, android.os.UserHandle):void");
    }

    void populateInputWindowHandle(com.android.server.wm.InputWindowHandleWrapper inputWindowHandle, com.android.server.wm.WindowState w) {
        boolean hasWallpaper = false;
        inputWindowHandle.setInputApplicationHandle(w.mActivityRecord != null ? w.mActivityRecord.getInputApplicationHandle(false) : null);
        inputWindowHandle.setToken(w.mInputChannelToken);
        inputWindowHandle.setDispatchingTimeoutMillis(w.getInputDispatchingTimeoutMillis());
        inputWindowHandle.setTouchOcclusionMode(w.getTouchOcclusionMode());
        inputWindowHandle.setPaused(w.mActivityRecord != null && w.mActivityRecord.paused);
        inputWindowHandle.setWindowToken(w.mClient.asBinder());
        inputWindowHandle.setName(w.getName());
        int flags = w.mAttrs.flags;
        if (w.mAttrs.isModal()) {
            flags |= 32;
        }
        inputWindowHandle.setLayoutParamsFlags(flags);
        inputWindowHandle.setInputConfigMasked(com.android.server.wm.InputConfigAdapter.getInputConfigFromWindowParams(w.mAttrs.type, flags, w.mAttrs.inputFeatures), com.android.server.wm.InputConfigAdapter.getMask());
        this.mInputMonitorExt.setOplusInputConfig(inputWindowHandle, w);
        boolean focusable = w.canReceiveKeys() && (this.mDisplayContent.hasOwnFocus() || this.mDisplayContent.isOnTop() || ((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageDisplayEnabled() || this.mDisplayContent.getWrapper().getExtImpl().isActivityPreloadDisplay(this.mDisplayContent));
        inputWindowHandle.setFocusable(focusable);
        if (this.mDisplayContent.mWallpaperController.isWallpaperTarget(w) && w.mAttrs.areWallpaperTouchEventsEnabled()) {
            hasWallpaper = true;
        }
        inputWindowHandle.setHasWallpaper(hasWallpaper);
        inputWindowHandle.setSurfaceInset(w.mAttrs.surfaceInsets.left);
        ((com.android.server.wm.IZoomWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IZoomWindowManagerExt.class).create()).adjustInputWindowHandle(this, w, inputWindowHandle);
        inputWindowHandle.setScaleFactor(w.mGlobalScale != 1.0f ? 1.0f / w.mGlobalScale : 1.0f);
        boolean useSurfaceBoundsAsTouchRegion = false;
        android.view.SurfaceControl touchableRegionCrop = null;
        com.android.server.wm.Task task = w.getTask();
        if (task != null && !android.app.WindowConfiguration.sExtImpl.isWindowingZoomMode(task.getWindowingMode()) && !((com.android.server.wm.IActivityTaskManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IActivityTaskManagerServiceExt.class).create()).inSplitRootTask(task) && !android.app.WindowConfiguration.sExtImpl.isWindowingComactMode(task.getWindowingMode())) {
            if (task.isOrganized() && task.getWindowingMode() != 1) {
                if (w.mTouchableInsets != 3) {
                    useSurfaceBoundsAsTouchRegion = true;
                }
                if (w.mAttrs.isModal()) {
                    com.android.server.wm.TaskFragment parent = w.getTaskFragment();
                    touchableRegionCrop = parent != null ? parent.getSurfaceControl() : null;
                }
            } else if (task.cropWindowsToRootTaskBounds() && !w.inFreeformWindowingMode()) {
                touchableRegionCrop = task.getRootTask().getSurfaceControl();
            }
        }
        inputWindowHandle.setReplaceTouchableRegionWithCrop(useSurfaceBoundsAsTouchRegion);
        inputWindowHandle.setTouchableRegionCrop(touchableRegionCrop);
        if (!useSurfaceBoundsAsTouchRegion) {
            w.getSurfaceTouchableRegion(this.mTmpRegion, w.mAttrs);
            inputWindowHandle.setTouchableRegion(this.mTmpRegion);
        }
    }

    void setUpdateInputWindowsNeededLw() {
        this.mUpdateInputWindowsNeeded = true;
    }

    void updateInputWindowsLw(boolean force) {
        if (!force && !this.mUpdateInputWindowsNeeded) {
            return;
        }
        scheduleUpdateInputWindows();
    }

    private void scheduleUpdateInputWindows() {
        if (!this.mDisplayRemoved && !this.mUpdateInputWindowsPending) {
            this.mUpdateInputWindowsPending = true;
            this.mHandler.post(this.mUpdateInputWindows);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateInputWindowsImmediately(android.view.SurfaceControl.Transaction t) {
        this.mHandler.removeCallbacks(this.mUpdateInputWindows);
        this.mUpdateInputWindowsImmediately = true;
        this.mUpdateInputWindows.run();
        this.mUpdateInputWindowsImmediately = false;
        t.merge(this.mInputTransaction);
    }

    void setInputFocusLw(com.android.server.wm.WindowState newWindow, boolean updateInputWindows) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(newWindow);
            long protoLogParam1 = this.mDisplayId;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -8553129529717081823L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
        }
        android.os.IBinder focus = newWindow != null ? newWindow.mInputChannelToken : null;
        if (focus == this.mInputFocus) {
            return;
        }
        if (newWindow != null && newWindow.canReceiveKeys()) {
            newWindow.mToken.paused = false;
        }
        setUpdateInputWindowsNeededLw();
        if (updateInputWindows) {
            updateInputWindowsLw(false);
        }
    }

    void setActiveRecents(com.android.server.wm.ActivityRecord activity, com.android.server.wm.Task layer) {
        boolean clear = activity == null;
        boolean wasActive = (this.mActiveRecentsActivity == null || this.mActiveRecentsLayerRef == null) ? false : true;
        this.mActiveRecentsActivity = clear ? null : new java.lang.ref.WeakReference<>(activity);
        this.mActiveRecentsLayerRef = clear ? null : new java.lang.ref.WeakReference<>(layer);
        if (clear && wasActive) {
            setUpdateInputWindowsNeededLw();
        }
        if (layer != null && layer.getDisplayContent() != null && layer.getDisplayContent().isDefaultDisplay && layer.getTaskDisplayArea() != null) {
            this.mActiveRecentsLayerDisplayRef = new java.lang.ref.WeakReference<>(layer.getTaskDisplayArea());
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT) {
                android.util.Slog.d("WindowManager", "setActiveRecents use TDA layer instead of activity layer:" + layer);
                return;
            }
            return;
        }
        this.mActiveRecentsLayerDisplayRef = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> T getWeak(java.lang.ref.WeakReference<T> ref) {
        if (ref != null) {
            return ref.get();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInputFocusRequest(com.android.server.wm.InputConsumerImpl recentsAnimationInputConsumer) {
        com.android.server.wm.WindowState focus = this.mDisplayContent.mCurrentFocus;
        com.android.server.wm.ActivityRecord app = null;
        if (recentsAnimationInputConsumer != null && focus != null) {
            com.android.server.wm.RecentsAnimationController recentsAnimationController = this.mService.getRecentsAnimationController();
            boolean shouldApplyRecentsInputConsumer = (recentsAnimationController != null && recentsAnimationController.shouldApplyInputConsumer(focus.mActivityRecord)) || (getWeak(this.mActiveRecentsActivity) != null && focus.inTransition() && focus.isActivityTypeHomeOrRecents());
            if (shouldApplyRecentsInputConsumer) {
                if (this.mInputFocus != recentsAnimationInputConsumer.mWindowHandle.token) {
                    requestFocus(recentsAnimationInputConsumer.mWindowHandle.token, recentsAnimationInputConsumer.mName);
                }
                if (this.mDisplayContent.mInputMethodWindow != null && this.mDisplayContent.mInputMethodWindow.isVisible() && this.mInputMonitorExt.getInputConsumerEnabled()) {
                    boolean isImeAttachedToApp = this.mDisplayContent.isImeAttachedToApp();
                    if (!isImeAttachedToApp) {
                        com.android.server.inputmethod.InputMethodManagerInternal inputMethodManagerInternal = (com.android.server.inputmethod.InputMethodManagerInternal) com.android.server.LocalServices.getService(com.android.server.inputmethod.InputMethodManagerInternal.class);
                        if (inputMethodManagerInternal != null) {
                            inputMethodManagerInternal.hideAllInputMethods(19, this.mDisplayContent.getDisplayId());
                        }
                        if (this.mDisplayContent.getImeInputTarget() != null) {
                            app = this.mDisplayContent.getImeInputTarget().getActivityRecord();
                        }
                        if (app != null) {
                            this.mDisplayContent.removeImeSurfaceImmediately();
                            if (app.getTask() != null) {
                                this.mDisplayContent.mAtmService.takeTaskSnapshot(app.getTask().mTaskId, true);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    com.android.server.inputmethod.InputMethodManagerInternal.get().updateImeWindowStatus(true, this.mDisplayContent.getDisplayId());
                    return;
                }
                return;
            }
        }
        android.os.IBinder focusToken = focus != null ? focus.mInputChannelToken : null;
        if (focusToken == null) {
            if (recentsAnimationInputConsumer != null && recentsAnimationInputConsumer.mWindowHandle != null && this.mInputFocus == recentsAnimationInputConsumer.mWindowHandle.token) {
                return;
            }
            if (this.mDisplayContent.mFocusedApp != null && this.mInputFocus != null) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mDisplayContent.mFocusedApp.getName());
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, 4027486077547983902L, 0, null, protoLogParam0);
                }
                android.util.EventLog.writeEvent(62001, "Requesting to set focus to null window", "reason=UpdateInputWindows");
                this.mInputTransaction.removeCurrentInputFocus(this.mDisplayId);
            }
            this.mInputFocus = null;
            return;
        }
        if (!focus.mWinAnimator.hasSurface() || !focus.mInputWindowHandle.isFocusable()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(focus);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -8537908614386667236L, 0, null, protoLogParam02);
            }
            this.mInputFocus = null;
            return;
        }
        requestFocus(focusToken, focus.getName());
    }

    private void requestFocus(android.os.IBinder focusToken, java.lang.String windowName) {
        if (focusToken == this.mInputFocus) {
            return;
        }
        this.mInputFocus = focusToken;
        this.mInputFocusRequestTimeMillis = android.os.SystemClock.uptimeMillis();
        this.mInputTransaction.setFocusedWindow(this.mInputFocus, windowName, this.mDisplayId);
        android.util.EventLog.writeEvent(62001, "Focus request " + windowName, "reason=UpdateInputWindows");
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_FOCUS_LIGHT_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(windowName);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_FOCUS_LIGHT, -6346673514571615151L, 0, null, protoLogParam0);
        }
    }

    void setFocusedAppLw(com.android.server.wm.ActivityRecord newApp) {
        this.mService.mInputManager.setFocusedApplication(this.mDisplayId, newApp != null ? newApp.getInputApplicationHandle(true) : null);
    }

    public void pauseDispatchingLw(com.android.server.wm.WindowToken window) {
        if (!window.paused) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT) {
                android.util.Slog.v("WindowManager", "Pausing WindowToken " + window);
            }
            window.paused = true;
            updateInputWindowsLw(true);
        }
    }

    public void resumeDispatchingLw(com.android.server.wm.WindowToken window) {
        if (window.paused) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT) {
                android.util.Slog.v("WindowManager", "Resuming WindowToken " + window);
            }
            window.paused = false;
            updateInputWindowsLw(true);
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        if (!this.mInputConsumers.isEmpty()) {
            pw.println(prefix + "InputConsumers:");
            for (int i = 0; i < this.mInputConsumers.size(); i++) {
                com.android.server.wm.InputConsumerImpl consumer = this.mInputConsumers.get(i);
                consumer.dump(pw, consumer.mName, prefix);
            }
        }
    }

    private final class UpdateInputForAllWindowsConsumer implements java.util.function.Consumer<com.android.server.wm.WindowState> {
        private boolean mAddPipInputConsumerHandle;
        private boolean mAddRecentsAnimationInputConsumerHandle;
        private boolean mAddWallpaperInputConsumerHandle;
        private boolean mInDrag;
        com.android.server.wm.InputConsumerImpl mPipInputConsumer;
        com.android.server.wm.InputConsumerImpl mRecentsAnimationInputConsumer;
        private final android.graphics.Rect mTmpRect;
        com.android.server.wm.InputConsumerImpl mWallpaperInputConsumer;

        private UpdateInputForAllWindowsConsumer() {
            this.mTmpRect = new android.graphics.Rect();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateInputWindows(boolean inDrag) {
            com.android.server.wm.WindowContainer layer;
            android.os.Trace.traceBegin(32L, "updateInputWindows");
            this.mPipInputConsumer = com.android.server.wm.InputMonitor.this.getInputConsumer("pip_input_consumer");
            this.mWallpaperInputConsumer = com.android.server.wm.InputMonitor.this.getInputConsumer("wallpaper_input_consumer");
            this.mRecentsAnimationInputConsumer = com.android.server.wm.InputMonitor.this.getInputConsumer("recents_animation_input_consumer");
            this.mAddPipInputConsumerHandle = this.mPipInputConsumer != null;
            this.mAddWallpaperInputConsumerHandle = this.mWallpaperInputConsumer != null;
            this.mAddRecentsAnimationInputConsumerHandle = this.mRecentsAnimationInputConsumer != null;
            this.mInDrag = inDrag;
            com.android.server.wm.InputMonitor.this.resetInputConsumers(com.android.server.wm.InputMonitor.this.mInputTransaction);
            com.android.server.wm.ActivityRecord activeRecents = (com.android.server.wm.ActivityRecord) com.android.server.wm.InputMonitor.getWeak(com.android.server.wm.InputMonitor.this.mActiveRecentsActivity);
            if (this.mAddRecentsAnimationInputConsumerHandle && activeRecents != null && activeRecents.getSurfaceControl() != null && com.android.server.wm.InputMonitor.this.mInputMonitorExt.getInputConsumerEnabled()) {
                if (com.android.server.wm.InputMonitor.RECENT_REF_TDA) {
                    com.android.server.wm.WindowContainer layer2 = (com.android.server.wm.WindowContainer) com.android.server.wm.InputMonitor.getWeak(com.android.server.wm.InputMonitor.this.mActiveRecentsLayerDisplayRef);
                    layer = layer2 != null ? layer2 : (com.android.server.wm.WindowContainer) com.android.server.wm.InputMonitor.getWeak(com.android.server.wm.InputMonitor.this.mActiveRecentsLayerRef);
                } else {
                    layer = (com.android.server.wm.WindowContainer) com.android.server.wm.InputMonitor.getWeak(com.android.server.wm.InputMonitor.this.mActiveRecentsLayerRef);
                }
                com.android.server.wm.WindowContainer layer3 = layer != null ? layer : activeRecents;
                if (layer3.getSurfaceControl() != null) {
                    com.android.server.wm.WindowState targetAppMainWindow = activeRecents.findMainWindow();
                    if (targetAppMainWindow != null) {
                        targetAppMainWindow.getBounds(this.mTmpRect);
                        com.android.server.wm.InputMonitor.this.mInputMonitorExt.adjustTouchableRegion(com.android.server.wm.InputMonitor.this.mDisplayContent.getRotation(), this.mTmpRect);
                        this.mRecentsAnimationInputConsumer.mWindowHandle.touchableRegion.set(this.mTmpRect);
                    }
                    if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT) {
                        android.util.Slog.d("WindowManager", "updateInputWindows mRecentsAnimationInputConsumer.show: " + layer3 + " mRecentsAnimationInputConsumer:" + this.mRecentsAnimationInputConsumer);
                    }
                    this.mRecentsAnimationInputConsumer.show(com.android.server.wm.InputMonitor.this.mInputTransaction, layer3);
                    this.mAddRecentsAnimationInputConsumerHandle = false;
                }
            }
            com.android.server.wm.InputMonitor.this.mDisplayContent.forAllWindows((java.util.function.Consumer<com.android.server.wm.WindowState>) this, true);
            com.android.server.wm.InputMonitor.this.updateInputFocusRequest(this.mRecentsAnimationInputConsumer);
            if (!com.android.server.wm.InputMonitor.this.mUpdateInputWindowsImmediately) {
                com.android.server.wm.InputMonitor.this.mDisplayContent.getPendingTransaction().merge(com.android.server.wm.InputMonitor.this.mInputTransaction);
                com.android.server.wm.InputMonitor.this.mDisplayContent.scheduleAnimation();
            }
            android.os.Trace.traceEnd(32L);
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.wm.WindowState w) {
            com.android.server.wm.DisplayArea targetDA;
            com.android.server.wm.InputWindowHandleWrapper inputWindowHandle = w.mInputWindowHandle;
            if (w.mInputChannelToken == null || w.mRemoved || !w.canReceiveTouchInput()) {
                if (w.mWinAnimator.hasSurface()) {
                    com.android.server.wm.InputMonitor.populateOverlayInputInfo(inputWindowHandle, w);
                    com.android.server.wm.InputMonitor.setInputWindowInfoIfNeeded(com.android.server.wm.InputMonitor.this.mInputTransaction, w.mWinAnimator.mSurfaceController.mSurfaceControl, inputWindowHandle);
                    return;
                }
                return;
            }
            com.android.server.wm.RecentsAnimationController recentsAnimationController = com.android.server.wm.InputMonitor.this.mService.getRecentsAnimationController();
            boolean shouldApplyRecentsInputConsumer = recentsAnimationController != null && recentsAnimationController.shouldApplyInputConsumer(w.mActivityRecord);
            if (this.mAddRecentsAnimationInputConsumerHandle && shouldApplyRecentsInputConsumer && recentsAnimationController.updateInputConsumerForApp(this.mRecentsAnimationInputConsumer.mWindowHandle) && (targetDA = recentsAnimationController.getTargetAppDisplayArea()) != null) {
                this.mRecentsAnimationInputConsumer.reparent(com.android.server.wm.InputMonitor.this.mInputTransaction, targetDA);
                this.mRecentsAnimationInputConsumer.show(com.android.server.wm.InputMonitor.this.mInputTransaction, 2147483645);
                this.mAddRecentsAnimationInputConsumerHandle = false;
            }
            if (w.inPinnedWindowingMode() && this.mAddPipInputConsumerHandle) {
                com.android.server.wm.Task rootTask = w.getTask().getRootTask();
                this.mPipInputConsumer.mWindowHandle.replaceTouchableRegionWithCrop(rootTask.getSurfaceControl());
                com.android.server.wm.DisplayArea targetDA2 = rootTask.getDisplayArea();
                if (targetDA2 != null) {
                    this.mPipInputConsumer.layout(com.android.server.wm.InputMonitor.this.mInputTransaction, rootTask.getBounds());
                    this.mPipInputConsumer.reparent(com.android.server.wm.InputMonitor.this.mInputTransaction, targetDA2);
                    this.mPipInputConsumer.show(com.android.server.wm.InputMonitor.this.mInputTransaction, 2147483646);
                    this.mAddPipInputConsumerHandle = false;
                }
            }
            if (this.mAddWallpaperInputConsumerHandle && w.mAttrs.type == 2013 && w.isVisible()) {
                this.mWallpaperInputConsumer.mWindowHandle.replaceTouchableRegionWithCrop((android.view.SurfaceControl) null);
                this.mWallpaperInputConsumer.show(com.android.server.wm.InputMonitor.this.mInputTransaction, w);
                this.mAddWallpaperInputConsumerHandle = false;
            }
            if (this.mInDrag && w.isVisible() && w.getDisplayContent().isDefaultDisplay) {
                com.android.server.wm.InputMonitor.this.mService.mDragDropController.sendDragStartedIfNeededLocked(w);
            }
            com.android.server.wm.InputMonitor.this.mService.mKeyInterceptionInfoForToken.put(w.mInputChannelToken, w.getKeyInterceptionInfo());
            if (w.mWinAnimator.hasSurface()) {
                com.android.server.wm.InputMonitor.this.populateInputWindowHandle(inputWindowHandle, w);
                com.android.server.wm.InputMonitor.setInputWindowInfoIfNeeded(com.android.server.wm.InputMonitor.this.mInputTransaction, w.mWinAnimator.mSurfaceController.mSurfaceControl, inputWindowHandle);
            }
        }
    }

    static void setInputWindowInfoIfNeeded(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl sc, com.android.server.wm.InputWindowHandleWrapper inputWindowHandle) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT) {
            android.util.Slog.d("WindowManager", "Update InputWindowHandle: " + inputWindowHandle);
        }
        if (inputWindowHandle.isChanged()) {
            inputWindowHandle.applyChangesToSurface(t, sc);
        }
    }

    static void populateOverlayInputInfo(com.android.server.wm.InputWindowHandleWrapper inputWindowHandle, com.android.server.wm.WindowState w) {
        populateOverlayInputInfo(inputWindowHandle);
        inputWindowHandle.setTouchOcclusionMode(w.getTouchOcclusionMode());
    }

    static void populateOverlayInputInfo(com.android.server.wm.InputWindowHandleWrapper inputWindowHandle) {
        inputWindowHandle.setDispatchingTimeoutMillis(0L);
        inputWindowHandle.setFocusable(false);
        inputWindowHandle.setToken(null);
        inputWindowHandle.setScaleFactor(1.0f);
        inputWindowHandle.setLayoutParamsType(2);
        inputWindowHandle.setInputConfigMasked(com.android.server.wm.InputConfigAdapter.getInputConfigFromWindowParams(2, 16, 1), com.android.server.wm.InputConfigAdapter.getMask());
        inputWindowHandle.clearTouchableRegion();
        inputWindowHandle.setTouchableRegionCrop(null);
    }

    static void setTrustedOverlayInputInfo(android.view.SurfaceControl sc, android.view.SurfaceControl.Transaction t, int displayId, java.lang.String name) {
        com.android.server.wm.InputWindowHandleWrapper inputWindowHandle = new com.android.server.wm.InputWindowHandleWrapper(new android.view.InputWindowHandle((android.view.InputApplicationHandle) null, displayId));
        inputWindowHandle.setName(name);
        inputWindowHandle.setLayoutParamsType(2015);
        inputWindowHandle.setTrustedOverlay(t, sc, true);
        populateOverlayInputInfo(inputWindowHandle);
        setInputWindowInfoIfNeeded(t, sc, inputWindowHandle);
    }

    static boolean isTrustedOverlay(int type) {
        return type == 2039 || type == 2011 || type == 2012 || type == 2027 || type == 2000 || type == 2040 || type == 2019 || type == 2024 || type == 2015 || type == 2034 || type == 2032 || type == 2022 || type == 2031 || type == 2041 || type == 2014 || type == 2100 || type >= 2300 || type == 2098 || type == 2099;
    }
}
