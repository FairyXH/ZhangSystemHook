package com.android.server.wm;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes3.dex */
public final class InputManagerCallback implements com.android.server.input.InputManagerService.WindowManagerCallbacks {
    private static final java.lang.String TAG = "WindowManager";
    private boolean mInputDevicesReady;
    private boolean mInputDispatchEnabled;
    private boolean mInputDispatchFrozen;
    private final com.android.server.wm.WindowManagerService mService;
    private final java.lang.Object mInputDevicesReadyMonitor = new java.lang.Object();
    private java.lang.String mInputFreezeReason = null;

    public InputManagerCallback(com.android.server.wm.WindowManagerService service) {
        this.mService = service;
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public void notifyInputChannelBroken(android.os.IBinder token) {
        if (token == null) {
            return;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState windowState = this.mService.mInputToWindowMap.get(token);
                if (windowState != null) {
                    android.util.Slog.i(TAG, "WINDOW DIED " + windowState);
                    windowState.removeIfPossible();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public void notifyNoFocusedWindowAnr(android.view.InputApplicationHandle applicationHandle) {
        com.android.internal.os.TimeoutRecord timeoutRecord = com.android.internal.os.TimeoutRecord.forInputDispatchNoFocusedWindow(timeoutMessage(java.util.OptionalInt.empty(), "Application does not have a focused window"));
        this.mService.mAnrController.notifyAppUnresponsive(applicationHandle, timeoutRecord);
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public void notifyWindowUnresponsive(android.os.IBinder token, java.util.OptionalInt pid, java.lang.String reason) {
        com.android.internal.os.TimeoutRecord timeoutRecord = com.android.internal.os.TimeoutRecord.forInputDispatchWindowUnresponsive(timeoutMessage(pid, reason));
        this.mService.mAnrController.notifyWindowUnresponsive(token, pid, timeoutRecord);
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public void notifyWindowResponsive(android.os.IBinder token, java.util.OptionalInt pid) {
        this.mService.mAnrController.notifyWindowResponsive(token, pid);
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public void notifyConfigurationChanged() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mService.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.InputManagerCallback$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.wm.DisplayContent) obj).sendNewConfiguration();
                    }
                });
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        synchronized (this.mInputDevicesReadyMonitor) {
            if (!this.mInputDevicesReady) {
                this.mInputDevicesReady = true;
                this.mInputDevicesReadyMonitor.notifyAll();
            }
        }
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public void notifyPointerLocationChanged(boolean pointerLocationEnabled) {
        if (this.mService.mPointerLocationEnabled == pointerLocationEnabled) {
            return;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mService.mPointerLocationEnabled = pointerLocationEnabled;
                this.mService.mRoot.forAllDisplayPolicies(new java.util.function.Consumer() { // from class: com.android.server.wm.InputManagerCallback$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$notifyPointerLocationChanged$0((com.android.server.wm.DisplayPolicy) obj);
                    }
                });
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyPointerLocationChanged$0(com.android.server.wm.DisplayPolicy p) {
        p.setPointerLocationEnabled(this.mService.mPointerLocationEnabled);
    }

    @Override // com.android.server.input.InputManagerInternal.LidSwitchCallback
    public void notifyLidSwitchChanged(long whenNanos, boolean lidOpen) {
        this.mService.mPolicy.notifyLidSwitchChanged(whenNanos, lidOpen);
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public void notifyCameraLensCoverSwitchChanged(long whenNanos, boolean lensCovered) {
        this.mService.mPolicy.notifyCameraLensCoverSwitchChanged(whenNanos, lensCovered);
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public int interceptKeyBeforeQueueing(android.view.KeyEvent event, int policyFlags) {
        return this.mService.mPolicy.interceptKeyBeforeQueueing(event, policyFlags);
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public int interceptMotionBeforeQueueingNonInteractive(int displayId, int source, int action, long whenNanos, int policyFlags) {
        return this.mService.mPolicy.interceptMotionBeforeQueueingNonInteractive(displayId, source, action, whenNanos, policyFlags);
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public long interceptKeyBeforeDispatching(android.os.IBinder focusedToken, android.view.KeyEvent event, int policyFlags) {
        return this.mService.mPolicy.interceptKeyBeforeDispatching(focusedToken, event, policyFlags);
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public android.view.KeyEvent dispatchUnhandledKey(android.os.IBinder focusedToken, android.view.KeyEvent event, int policyFlags) {
        return this.mService.mPolicy.dispatchUnhandledKey(focusedToken, event, policyFlags);
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public int getPointerLayer() {
        return (this.mService.mPolicy.getWindowLayerFromTypeLw(2018) * 10000) + 1000;
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
    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public int getPointerDisplayId() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!this.mService.mForceDesktopModeOnExternalDisplays) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return 0;
                }
                int firstExternalDisplayId = 0;
                for (int i = this.mService.mRoot.mChildren.size() - 1; i >= 0; i--) {
                    com.android.server.wm.DisplayContent displayContent = (com.android.server.wm.DisplayContent) this.mService.mRoot.mChildren.get(i);
                    if (displayContent.getDisplayInfo().state != 1) {
                        if (displayContent.getWindowingMode() == 5) {
                            int displayId = displayContent.getDisplayId();
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return displayId;
                        }
                        if (firstExternalDisplayId == 0 && displayContent.getDisplayId() != 0) {
                            firstExternalDisplayId = displayContent.getDisplayId();
                        }
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return firstExternalDisplayId;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public void onPointerDownOutsideFocus(android.os.IBinder touchedToken) {
        this.mService.mH.obtainMessage(62, touchedToken).sendToTarget();
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public void notifyFocusChanged(android.os.IBinder oldToken, android.os.IBinder newToken) {
        com.android.server.wm.WindowManagerService.H h = this.mService.mH;
        final com.android.server.wm.WindowManagerService windowManagerService = this.mService;
        java.util.Objects.requireNonNull(windowManagerService);
        h.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.wm.InputManagerCallback$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                windowManagerService.reportFocusChanged((android.os.IBinder) obj, (android.os.IBinder) obj2);
            }
        }, oldToken, newToken));
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public void notifyDropWindow(android.os.IBinder token, float x, float y) {
        com.android.server.wm.WindowManagerService.H h = this.mService.mH;
        final com.android.server.wm.DragDropController dragDropController = this.mService.mDragDropController;
        java.util.Objects.requireNonNull(dragDropController);
        h.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.wm.InputManagerCallback$$ExternalSyntheticLambda3
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                dragDropController.reportDropWindow((android.os.IBinder) obj, ((java.lang.Float) obj2).floatValue(), ((java.lang.Float) obj3).floatValue());
            }
        }, token, java.lang.Float.valueOf(x), java.lang.Float.valueOf(y)));
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public android.view.SurfaceControl getParentSurfaceForPointers(int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent dc = this.mService.mRoot.getDisplayContent(displayId);
                if (dc == null) {
                    android.util.Slog.e(TAG, "Failed to get parent surface for pointers on display " + displayId + " - DisplayContent not found.");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                android.view.SurfaceControl overlayLayer = dc.getOverlayLayer();
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return overlayLayer;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public boolean isAnimating() {
        boolean isTransitionRunning;
        com.android.server.wm.TransitionController transitionController = this.mService.mRoot.getDefaultDisplay().mTransitionController;
        boolean isAppTransition = false;
        if (transitionController.isShellTransitionsEnabled()) {
            isTransitionRunning = transitionController.inTransition();
        } else {
            isTransitionRunning = this.mService.mRoot.getDefaultDisplay().mAppTransition.isRunning();
            isAppTransition = this.mService.mRoot.isAppTransitioning();
        }
        android.util.Slog.d(TAG, "isAnimating: isTransitionRunning " + isTransitionRunning + " isAppTransition: " + isAppTransition);
        return isTransitionRunning || isAppTransition;
    }

    @Override // com.android.server.input.InputManagerService.WindowManagerCallbacks
    public android.view.SurfaceControl createSurfaceForGestureMonitor(java.lang.String name, int displayId) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent dc = this.mService.mRoot.getDisplayContent(displayId);
                if (dc == null) {
                    android.util.Slog.e(TAG, "Failed to create a gesture monitor on display: " + displayId + " - DisplayContent not found.");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                android.view.SurfaceControl inputOverlay = dc.getInputOverlayLayer();
                if (inputOverlay == null) {
                    android.util.Slog.e(TAG, "Failed to create a gesture monitor on display: " + displayId + " - Input overlay layer is not initialized.");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return null;
                }
                android.view.SurfaceControl surfaceControlBuild = this.mService.makeSurfaceBuilder(dc.getSession()).setContainerLayer().setName(name).setCallsite("createSurfaceForGestureMonitor").setParent(inputOverlay).setCallsite("InputManagerCallback.createSurfaceForGestureMonitor").build();
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return surfaceControlBuild;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public boolean waitForInputDevicesReady(long timeoutMillis) {
        boolean z;
        synchronized (this.mInputDevicesReadyMonitor) {
            if (!this.mInputDevicesReady) {
                try {
                    this.mInputDevicesReadyMonitor.wait(timeoutMillis);
                } catch (java.lang.InterruptedException e) {
                }
            }
            z = this.mInputDevicesReady;
        }
        return z;
    }

    public void freezeInputDispatchingLw() {
        if (!this.mInputDispatchFrozen) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT) {
                android.util.Slog.v(TAG, "Freezing input dispatching");
            }
            this.mInputDispatchFrozen = true;
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT) {
                this.mInputFreezeReason = android.os.Debug.getCallers(6);
            }
            updateInputDispatchModeLw();
        }
    }

    public void thawInputDispatchingLw() {
        if (this.mInputDispatchFrozen) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT) {
                android.util.Slog.v(TAG, "Thawing input dispatching");
            }
            this.mInputDispatchFrozen = false;
            this.mInputFreezeReason = null;
            updateInputDispatchModeLw();
        }
    }

    public void setEventDispatchingLw(boolean enabled) {
        if (this.mInputDispatchEnabled != enabled) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_INPUT) {
                android.util.Slog.v(TAG, "Setting event dispatching to " + enabled);
            }
            this.mInputDispatchEnabled = enabled;
            updateInputDispatchModeLw();
        }
    }

    private void updateInputDispatchModeLw() {
        this.mService.mInputManager.setInputDispatchMode(this.mInputDispatchEnabled, this.mInputDispatchFrozen);
    }

    private java.lang.String timeoutMessage(java.util.OptionalInt pid, java.lang.String reason) {
        android.gui.StalledTransactionInfo stalledTransactionInfo;
        java.lang.String message = reason == null ? "Input dispatching timed out." : java.lang.String.format("Input dispatching timed out (%s).", reason);
        if (pid.isEmpty() || (stalledTransactionInfo = android.view.SurfaceControl.getStalledTransactionInfo(pid.getAsInt())) == null) {
            return message;
        }
        return java.lang.String.format("%s Buffer processing for the associated surface is stuck due to an unsignaled fence (window=%s, bufferId=0x%016X, frameNumber=%s). This potentially indicates a GPU hang.", message, stalledTransactionInfo.layerName, java.lang.Long.valueOf(stalledTransactionInfo.bufferId), java.lang.Long.valueOf(stalledTransactionInfo.frameNumber));
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        if (this.mInputFreezeReason != null) {
            pw.println(prefix + "mInputFreezeReason=" + this.mInputFreezeReason);
        }
    }
}
