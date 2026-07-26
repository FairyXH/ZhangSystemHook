package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TaskPositioningController {
    private android.view.SurfaceControl mInputSurface;
    private com.android.server.wm.DisplayContent mPositioningDisplay;
    private final com.android.server.wm.WindowManagerService mService;
    private com.android.server.wm.TaskPositioner mTaskPositioner;
    final android.view.SurfaceControl.Transaction mTransaction;
    private final android.graphics.Rect mTmpClipRect = new android.graphics.Rect();
    private com.android.server.wm.ITaskPositioningControllerExt mTaskPositioningControllerExt = (com.android.server.wm.ITaskPositioningControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ITaskPositioningControllerExt.class).base(this).create();

    boolean isPositioningLocked() {
        return this.mTaskPositioner != null;
    }

    android.view.InputWindowHandle getDragWindowHandleLocked() {
        if (this.mTaskPositioner != null) {
            return this.mTaskPositioner.mDragWindowHandle;
        }
        return null;
    }

    TaskPositioningController(com.android.server.wm.WindowManagerService service) {
        this.mService = service;
        this.mTransaction = service.mTransactionFactory.get();
    }

    void hideInputSurface(int displayId) {
        if (this.mPositioningDisplay != null && this.mPositioningDisplay.getDisplayId() == displayId && this.mInputSurface != null) {
            this.mTransaction.hide(this.mInputSurface).apply();
        }
    }

    java.util.concurrent.CompletableFuture<java.lang.Void> showInputSurface(int displayId) {
        if (this.mPositioningDisplay == null || this.mPositioningDisplay.getDisplayId() != displayId) {
            return java.util.concurrent.CompletableFuture.completedFuture(null);
        }
        com.android.server.wm.DisplayContent dc = this.mService.mRoot.getDisplayContent(displayId);
        if (this.mInputSurface == null) {
            this.mInputSurface = this.mService.makeSurfaceBuilder(dc.getSession()).setContainerLayer().setName("Drag and Drop Input Consumer").setCallsite("TaskPositioningController.showInputSurface").setParent(dc.getOverlayLayer()).build();
        }
        android.view.InputWindowHandle h = getDragWindowHandleLocked();
        if (h == null) {
            android.util.Slog.w("WindowManager", "Drag is in progress but there is no drag window handle.");
            return java.util.concurrent.CompletableFuture.completedFuture(null);
        }
        android.view.Display display = dc.getDisplay();
        android.graphics.Point p = new android.graphics.Point();
        display.getRealSize(p);
        this.mTmpClipRect.set(0, 0, p.x, p.y);
        final java.util.concurrent.CompletableFuture<java.lang.Void> result = new java.util.concurrent.CompletableFuture<>();
        this.mTransaction.show(this.mInputSurface).setInputWindowInfo(this.mInputSurface, h).setLayer(this.mInputSurface, Integer.MAX_VALUE).setPosition(this.mInputSurface, 0.0f, 0.0f).setCrop(this.mInputSurface, this.mTmpClipRect).addWindowInfosReportedListener(new java.lang.Runnable() { // from class: com.android.server.wm.TaskPositioningController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                result.complete(null);
            }
        }).apply();
        return result;
    }

    boolean startMovingTask(android.view.IWindow window, float startX, float startY) throws java.lang.Throwable {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    com.android.server.wm.WindowState win = this.mService.windowForClientLocked((com.android.server.wm.Session) null, window, false);
                    java.util.concurrent.CompletableFuture<java.lang.Boolean> startPositioningLockedFuture = startPositioningLocked(win, false, false, startX, startY);
                    try {
                        if (!startPositioningLockedFuture.get().booleanValue()) {
                            return false;
                        }
                        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
                        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock2) {
                            try {
                                this.mService.mAtmService.setFocusedTask(win.getTask().mTaskId);
                            } finally {
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            }
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return true;
                    } catch (java.lang.Exception exception) {
                        android.util.Slog.e("WindowManager", "Exception thrown while waiting for startPositionLocked future", exception);
                        return false;
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    void handleTapOutsideTask(final com.android.server.wm.DisplayContent displayContent, final int x, final int y) {
        this.mService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.TaskPositioningController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleTapOutsideTask$1(displayContent, x, y);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleTapOutsideTask$1(com.android.server.wm.DisplayContent displayContent, int x, int y) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.Task task = displayContent.findTaskForResizePoint(x, y);
                if (task != null && task.isResizeable()) {
                    java.util.concurrent.CompletableFuture<java.lang.Boolean> startPositioningLockedFuture = startPositioningLocked(task.getTopVisibleAppMainWindow(), true, task.preserveOrientationOnResize(), x, y);
                    try {
                        if (!startPositioningLockedFuture.get().booleanValue()) {
                            return;
                        }
                        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
                        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                        synchronized (windowManagerGlobalLock2) {
                            try {
                                this.mService.mAtmService.setFocusedTask(task.mTaskId);
                            } finally {
                            }
                        }
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    } catch (java.lang.Exception exception) {
                        android.util.Slog.e("WindowManager", "Exception thrown while waiting for startPositionLocked future", exception);
                        return;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } finally {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
        }
    }

    private java.util.concurrent.CompletableFuture<java.lang.Boolean> startPositioningLocked(final com.android.server.wm.WindowState win, final boolean resize, final boolean preserveOrientation, final float startX, final float startY) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            android.util.Slog.d("WindowManager", "startPositioningLocked: win=" + win + ", resize=" + resize + ", preserveOrientation=" + preserveOrientation + ", {" + startX + ", " + startY + "}");
        }
        if (win == null || win.mActivityRecord == null) {
            android.util.Slog.w("WindowManager", "startPositioningLocked: Bad window " + win);
            return java.util.concurrent.CompletableFuture.completedFuture(false);
        }
        if (win.mInputChannel == null) {
            android.util.Slog.wtf("WindowManager", "startPositioningLocked: " + win + " has no input channel,  probably being removed");
            return java.util.concurrent.CompletableFuture.completedFuture(false);
        }
        final com.android.server.wm.DisplayContent displayContent = win.getDisplayContent();
        if (displayContent == null) {
            android.util.Slog.w("WindowManager", "startPositioningLocked: Invalid display content " + win);
            return java.util.concurrent.CompletableFuture.completedFuture(false);
        }
        this.mPositioningDisplay = displayContent;
        this.mTaskPositioner = com.android.server.wm.TaskPositioner.create(this.mService);
        return this.mTaskPositioner.register(displayContent, win).thenApply(new java.util.function.Function() { // from class: com.android.server.wm.TaskPositioningController$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$startPositioningLocked$2(win, displayContent, resize, preserveOrientation, startX, startY, (java.lang.Void) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$startPositioningLocked$2(com.android.server.wm.WindowState win, com.android.server.wm.DisplayContent displayContent, boolean resize, boolean preserveOrientation, float startX, float startY, java.lang.Void unused) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            com.android.server.wm.WindowState transferTouchFromWin = win;
            try {
                if (displayContent.mCurrentFocus != null && displayContent.mCurrentFocus != win && displayContent.mCurrentFocus.mActivityRecord == win.mActivityRecord) {
                    transferTouchFromWin = displayContent.mCurrentFocus;
                }
                if (!this.mService.mInputManager.transferTouchGesture(transferTouchFromWin.mInputChannel.getToken(), this.mTaskPositioner.mClientChannel.getToken())) {
                    android.util.Slog.e("WindowManager", "startPositioningLocked: Unable to transfer touch focus");
                    cleanUpTaskPositioner();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                this.mTaskPositioner.startDrag(resize, preserveOrientation, startX, startY);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return true;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void finishTaskPositioning(android.view.IWindow window) {
        if (this.mTaskPositioner != null && this.mTaskPositioner.mClientCallback == window.asBinder()) {
            finishTaskPositioning();
        }
    }

    void finishTaskPositioning() {
        this.mService.mAnimationHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.TaskPositioningController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$finishTaskPositioning$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishTaskPositioning$3() {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            android.util.Slog.d("WindowManager", "finishPositioning");
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                cleanUpTaskPositioner();
                this.mPositioningDisplay = null;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    private void cleanUpTaskPositioner() {
        com.android.server.wm.TaskPositioner positioner = this.mTaskPositioner;
        if (positioner == null) {
            return;
        }
        this.mTaskPositioner = null;
        positioner.unregister();
    }
}
