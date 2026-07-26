package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DragDropController {
    private static final int A11Y_DRAG_TIMEOUT_DEFAULT_MS = 60000;
    private static final float DRAG_SHADOW_ALPHA_TRANSPARENT = 0.7071f;
    static final long DRAG_TIMEOUT_MS = 5000;
    static final int MSG_ANIMATION_END = 2;
    static final int MSG_DRAG_END_TIMEOUT = 0;
    static final int MSG_REMOVE_DRAG_SURFACE_TIMEOUT = 3;
    static final int MSG_TEAR_DOWN_DRAG_AND_DROP_INPUT = 1;
    static final int MSG_UNHANDLED_DROP_LISTENER_TIMEOUT = 4;
    private com.android.server.wm.DragState mDragState;
    private android.window.IGlobalDragListener mGlobalDragListener;
    private final android.os.Handler mHandler;
    private com.android.server.wm.WindowManagerService mService;
    public com.android.server.wm.IDragDropControllerExt mDragDropControllerExt = (com.android.server.wm.IDragDropControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDragDropControllerExt.class).base(this).create();
    private final android.os.IBinder.DeathRecipient mGlobalDragListenerDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.wm.DragDropController.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.DragDropController.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.DragDropController.this.hasPendingUnhandledDropCallback()) {
                        com.android.server.wm.DragDropController.this.onUnhandledDropCallback(false);
                    }
                    com.android.server.wm.DragDropController.this.setGlobalDragListener(null);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    };
    private java.util.concurrent.atomic.AtomicReference<com.android.server.wm.WindowManagerInternal.IDragDropCallback> mCallback = new java.util.concurrent.atomic.AtomicReference<>(new com.android.server.wm.WindowManagerInternal.IDragDropCallback() { // from class: com.android.server.wm.DragDropController.2
    });

    DragDropController(com.android.server.wm.WindowManagerService service, android.os.Looper looper) {
        this.mService = service;
        this.mHandler = new com.android.server.wm.DragDropController.DragHandler(service, looper);
    }

    android.os.Handler getHandler() {
        return this.mHandler;
    }

    boolean dragDropActiveLocked() {
        return (this.mDragState == null || this.mDragState.isClosing()) ? false : true;
    }

    boolean dragSurfaceRelinquishedToDropTarget() {
        return this.mDragState != null && this.mDragState.mRelinquishDragSurfaceToDropTarget;
    }

    void registerCallback(com.android.server.wm.WindowManagerInternal.IDragDropCallback callback) {
        java.util.Objects.requireNonNull(callback);
        this.mCallback.set(callback);
        if (callback instanceof com.android.server.wm.IDragDropControllerExt.IOplusDragDropControllerExtCallback) {
            android.util.Slog.d("WindowManager", " controllerExt registerCallback ");
            this.mDragDropControllerExt.registerCallback((com.android.server.wm.IDragDropControllerExt.IOplusDragDropControllerExtCallback) callback);
        }
    }

    public void setGlobalDragListener(android.window.IGlobalDragListener listener) {
        if (this.mGlobalDragListener != null && this.mGlobalDragListener.asBinder() != null) {
            this.mGlobalDragListener.asBinder().unlinkToDeath(this.mGlobalDragListenerDeathRecipient, 0);
        }
        this.mGlobalDragListener = listener;
        if (listener != null && listener.asBinder() != null) {
            try {
                this.mGlobalDragListener.asBinder().linkToDeath(this.mGlobalDragListenerDeathRecipient, 0);
            } catch (android.os.RemoteException e) {
                this.mGlobalDragListener = null;
            }
        }
    }

    void sendDragStartedIfNeededLocked(com.android.server.wm.WindowState window) {
        this.mDragState.sendDragStartedIfNeededLocked(window);
    }

    /* JADX WARN: Removed duplicated region for block: B:335:0x04b5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    android.os.IBinder performDrag(int r27, int r28, android.view.IWindow r29, int r30, android.view.SurfaceControl r31, int r32, int r33, int r34, float r35, float r36, float r37, float r38, android.content.ClipData r39) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1277
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.DragDropController.performDrag(int, int, android.view.IWindow, int, android.view.SurfaceControl, int, int, int, float, float, float, float, android.content.ClipData):android.os.IBinder");
    }

    void reportDropResult(android.view.IWindow window, boolean consumed) {
        java.util.concurrent.atomic.AtomicReference<com.android.server.wm.WindowManagerInternal.IDragDropCallback> atomicReference;
        com.android.server.wm.WindowManagerInternal.IDragDropCallback iDragDropCallback;
        com.android.server.wm.WindowManagerInternal.IDragDropCallback iDragDropCallback2;
        android.os.IBinder token = window.asBinder();
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
            android.util.Slog.d("WindowManager", "Drop result=" + consumed + " reported by " + token);
        }
        this.mCallback.get().preReportDropResult(window, consumed);
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (this.mDragState == null) {
                        android.util.Slog.w("WindowManager", "Drop result given but no drag in progress");
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    if (this.mDragState.mToken != token) {
                        android.util.Slog.w("WindowManager", "Invalid drop-result claim by " + window);
                        throw new java.lang.IllegalStateException("reportDropResult() by non-recipient");
                    }
                    this.mHandler.removeMessages(0, window.asBinder());
                    com.android.server.wm.WindowState callingWin = this.mService.windowForClientLocked((com.android.server.wm.Session) null, window, false);
                    if (callingWin == null) {
                        android.util.Slog.w("WindowManager", "Bad result-reporting window " + window);
                        this.mDragState.endDragLocked(false, false);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    boolean res = this.mDragDropControllerExt.getConsumedResult();
                    if (res != consumed) {
                        consumed = res;
                    }
                    if (!consumed && notifyUnhandledDrop(this.mDragState.mUnhandledDropEvent, "window-drop")) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    boolean relinquishDragSurfaceToDropTarget = consumed && this.mDragState.targetInterceptsGlobalDrag(callingWin);
                    boolean isCrossWindowDrag = this.mDragState.mLocalWin.equals(token) ? false : true;
                    this.mDragState.endDragLocked(consumed, relinquishDragSurfaceToDropTarget);
                    com.android.server.wm.Task droppedWindowTask = callingWin.getTask();
                    if (com.android.window.flags.Flags.delegateUnhandledDrags() && this.mGlobalDragListener != null && droppedWindowTask != null && consumed && isCrossWindowDrag) {
                        try {
                            this.mGlobalDragListener.onCrossWindowDrop(droppedWindowTask.getTaskInfo());
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e("WindowManager", "Failed to call global drag listener for cross-window drop", e);
                        }
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            this.mCallback.get().postReportDropResult();
        }
    }

    boolean notifyUnhandledDrop(android.view.DragEvent dropEvent, java.lang.String reason) {
        boolean isLocalDrag = (this.mDragState.mFlags & com.android.server.wm.IActivityRecordExt.REASON_TASK_DESTROYED) == 0;
        boolean shouldDelegateUnhandledDrag = (this.mDragState.mFlags & 8192) != 0;
        if (!com.android.window.flags.Flags.delegateUnhandledDrags() || this.mGlobalDragListener == null || !shouldDelegateUnhandledDrag || isLocalDrag) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                android.util.Slog.d("WindowManager", "Skipping unhandled listener (listener=" + this.mGlobalDragListener + ", flags=" + this.mDragState.mFlags + ")");
            }
            return false;
        }
        final int traceCookie = new java.util.Random().nextInt();
        android.os.Trace.asyncTraceBegin(32L, "DragDropController#notifyUnhandledDrop", traceCookie);
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
            android.util.Slog.d("WindowManager", "Sending DROP to unhandled listener (" + reason + ")");
        }
        try {
            sendTimeoutMessage(4, null, DRAG_TIMEOUT_MS);
            this.mGlobalDragListener.onUnhandledDrop(dropEvent, new android.window.IUnhandledDragCallback.Stub() { // from class: com.android.server.wm.DragDropController.3
                public void notifyUnhandledDropComplete(boolean consumedByListener) {
                    if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                        android.util.Slog.d("WindowManager", "Unhandled listener finished handling DROP");
                    }
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.DragDropController.this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            com.android.server.wm.DragDropController.this.onUnhandledDropCallback(consumedByListener);
                            android.os.Trace.asyncTraceEnd(32L, "DragDropController#notifyUnhandledDrop", traceCookie);
                        } catch (java.lang.Throwable th) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            });
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e("WindowManager", "Failed to call global drag listener for unhandled drop", e);
            return false;
        }
    }

    void onUnhandledDropCallback(boolean consumedByListener) {
        this.mHandler.removeMessages(4, null);
        this.mDragState.mDragResult = consumedByListener;
        this.mDragState.mRelinquishDragSurfaceToDropTarget = consumedByListener;
        this.mDragState.closeLocked();
    }

    boolean hasPendingUnhandledDropCallback() {
        return this.mHandler.hasMessages(4);
    }

    void cancelDragAndDrop(android.os.IBinder dragToken, boolean skipAnimation) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
            android.util.Slog.d("WindowManager", "cancelDragAndDrop");
        }
        this.mCallback.get().preCancelDragAndDrop(dragToken);
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (this.mDragState == null) {
                        android.util.Slog.w("WindowManager", "cancelDragAndDrop() without prepareDrag()");
                        throw new java.lang.IllegalStateException("cancelDragAndDrop() without prepareDrag()");
                    }
                    if (this.mDragState.mToken != dragToken) {
                        android.util.Slog.w("WindowManager", "cancelDragAndDrop() does not match prepareDrag()");
                        throw new java.lang.IllegalStateException("cancelDragAndDrop() does not match prepareDrag()");
                    }
                    this.mDragState.mDragResult = false;
                    this.mDragState.cancelDragLocked(skipAnimation);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            this.mCallback.get().postCancelDragAndDrop();
        }
    }

    void handleMotionEvent(boolean keepHandling, float newX, float newY) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (!dragDropActiveLocked()) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } else {
                    this.mDragState.updateDragSurfaceLocked(keepHandling, newX, newY);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void dragRecipientEntered(android.view.IWindow window) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
            android.util.Slog.d("WindowManager", "Drag into new candidate view @ " + window.asBinder());
        }
        this.mCallback.get().dragRecipientEntered(window);
    }

    void dragRecipientExited(android.view.IWindow window) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
            android.util.Slog.d("WindowManager", "Drag from old candidate view @ " + window.asBinder());
        }
        this.mCallback.get().dragRecipientExited(window);
    }

    void sendHandlerMessage(int what, java.lang.Object arg) {
        this.mHandler.obtainMessage(what, arg).sendToTarget();
    }

    void sendTimeoutMessage(int what, java.lang.Object arg, long timeoutMs) {
        this.mHandler.removeMessages(what, arg);
        android.os.Message msg = this.mHandler.obtainMessage(what, arg);
        this.mHandler.sendMessageDelayed(msg, timeoutMs);
    }

    void onDragStateClosedLocked(com.android.server.wm.DragState dragState) {
        if (this.mDragState != dragState) {
            android.util.Slog.wtf("WindowManager", "Unknown drag state is closed");
        } else {
            this.mDragState = null;
            this.mDragDropControllerExt.postEndDrag();
        }
    }

    void reportDropWindow(android.os.IBinder token, float x, float y) {
        if (this.mDragState == null) {
            android.util.Slog.w("WindowManager", "Drag state is closed.");
            return;
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mDragState != null) {
                    this.mDragState.reportDropWindowLock(token, x, y);
                } else {
                    android.util.Slog.w("WindowManager", "reportDropWindow mDragState is null!");
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    boolean dropForAccessibility(android.view.IWindow window, float x, float y) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                boolean isA11yEnabled = getAccessibilityManager().isEnabled();
                if (!dragDropActiveLocked()) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                if (!this.mDragState.isAccessibilityDragDrop() || !isA11yEnabled) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                com.android.server.wm.WindowState winState = this.mService.windowForClientLocked((com.android.server.wm.Session) null, window, false);
                if (!this.mDragState.isWindowNotified(winState)) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
                android.os.IBinder token = winState.mInputChannelToken;
                boolean zReportDropWindowLock = this.mDragState.reportDropWindowLock(token, x, y);
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                return zReportDropWindowLock;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    android.view.accessibility.AccessibilityManager getAccessibilityManager() {
        return (android.view.accessibility.AccessibilityManager) this.mService.mContext.getSystemService("accessibility");
    }

    private class DragHandler extends android.os.Handler {
        private final com.android.server.wm.WindowManagerService mService;

        DragHandler(com.android.server.wm.WindowManagerService service, android.os.Looper looper) {
            super(looper);
            this.mService = service;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    android.os.IBinder win = (android.os.IBinder) msg.obj;
                    if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                        android.util.Slog.w("WindowManager", "Timeout ending drag to win " + win);
                    }
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock) {
                        try {
                            if (com.android.server.wm.DragDropController.this.mDragState != null) {
                                com.android.server.wm.DragDropController.this.mDragState.endDragLocked(false, false);
                            }
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                case 1:
                    if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                        android.util.Slog.d("WindowManager", "Drag ending; tearing down input channel");
                    }
                    com.android.server.wm.DragState.InputInterceptor interceptor = (com.android.server.wm.DragState.InputInterceptor) msg.obj;
                    if (interceptor == null) {
                        return;
                    }
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock2) {
                        try {
                            interceptor.tearDown();
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                case 2:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock3 = this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock3) {
                        try {
                            if (com.android.server.wm.DragDropController.this.mDragState == null) {
                                android.util.Slog.wtf("WindowManager", "mDragState unexpectedly became null while playing animation");
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            if (!com.android.server.wm.DragDropController.this.mDragState.isClosing() && (com.android.server.wm.DragDropController.this.mDragState.mAnimator == null || com.android.server.wm.DragDropController.this.mDragState.mAnimationCompleted)) {
                                com.android.server.wm.DragDropController.this.mDragState.closeLocked();
                                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                                return;
                            }
                            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                                android.util.Slog.w("WindowManager", "If mDragState is closing or mAnimator is not completed, return ");
                            }
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        } finally {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        }
                    }
                case 3:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock4 = this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock4) {
                        try {
                            this.mService.mTransactionFactory.get().remove((android.view.SurfaceControl) msg.obj).apply();
                        } finally {
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                case 4:
                    com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock5 = this.mService.mGlobalLock;
                    com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                    synchronized (windowManagerGlobalLock5) {
                        try {
                            com.android.server.wm.DragDropController.this.onUnhandledDropCallback(false);
                        } finally {
                        }
                        break;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                default:
                    return;
            }
        }
    }
}
