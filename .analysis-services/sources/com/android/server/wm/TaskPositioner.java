package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TaskPositioner implements android.os.IBinder.DeathRecipient {
    private static final boolean DEBUG_ORIENTATION_VIOLATIONS = false;
    public static final float RESIZING_HINT_ALPHA = 0.5f;
    public static final int RESIZING_HINT_DURATION_MS = 0;
    private static final java.lang.String TAG = "WindowManager";
    private static final java.lang.String TAG_LOCAL = "TaskPositioner";
    private static com.android.server.wm.TaskPositioner.Factory sFactory;
    android.os.IBinder mClientCallback;
    android.view.InputChannel mClientChannel;
    private com.android.server.wm.DisplayContent mDisplayContent;
    android.view.InputApplicationHandle mDragApplicationHandle;
    boolean mDragEnded;
    android.view.InputWindowHandle mDragWindowHandle;
    private android.view.InputEventReceiver mInputEventReceiver;
    private int mMinVisibleHeight;
    private int mMinVisibleWidth;
    private boolean mPreserveOrientation;
    private boolean mResizing;
    private final com.android.server.wm.WindowManagerService mService;
    private float mStartDragX;
    private float mStartDragY;
    private boolean mStartOrientationWasLandscape;
    com.android.server.wm.Task mTask;
    com.android.server.wm.WindowState mWindow;
    private android.graphics.Rect mTmpRect = new android.graphics.Rect();
    private final android.graphics.Rect mWindowOriginalBounds = new android.graphics.Rect();
    private final android.graphics.Rect mWindowDragBounds = new android.graphics.Rect();
    private final android.graphics.Point mMaxVisibleSize = new android.graphics.Point();
    private int mCtrlType = 0;

    TaskPositioner(com.android.server.wm.WindowManagerService service) {
        this.mService = service;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onInputEvent(android.view.InputEvent event) {
        if (!(event instanceof android.view.MotionEvent) || (event.getSource() & 2) == 0) {
            return false;
        }
        android.view.MotionEvent motionEvent = (android.view.MotionEvent) event;
        if (this.mDragEnded) {
            return true;
        }
        float newX = motionEvent.getRawX();
        float newY = motionEvent.getRawY();
        switch (motionEvent.getAction()) {
            case 0:
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
                    android.util.Slog.w(TAG, "ACTION_DOWN @ {" + newX + ", " + newY + "}");
                }
                break;
            case 1:
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
                    android.util.Slog.w(TAG, "ACTION_UP @ {" + newX + ", " + newY + "}");
                }
                this.mDragEnded = true;
                break;
            case 2:
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
                    android.util.Slog.w(TAG, "ACTION_MOVE @ {" + newX + ", " + newY + "}");
                }
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        this.mDragEnded = notifyMoveLocked(newX, newY);
                        this.mTask.getDimBounds(this.mTmpRect);
                    } finally {
                    }
                    break;
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                if (!this.mTmpRect.equals(this.mWindowDragBounds)) {
                    android.os.Trace.traceBegin(32L, "wm.TaskPositioner.resizeTask");
                    this.mService.mAtmService.resizeTask(this.mTask.mTaskId, this.mWindowDragBounds, 1);
                    android.os.Trace.traceEnd(32L);
                }
                break;
            case 3:
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
                    android.util.Slog.w(TAG, "ACTION_CANCEL @ {" + newX + ", " + newY + "}");
                }
                this.mDragEnded = true;
                break;
        }
        if (this.mDragEnded) {
            boolean wasResizing = this.mResizing;
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock2 = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock2) {
                try {
                    endDragLocked();
                    this.mTask.getDimBounds(this.mTmpRect);
                } finally {
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            if (wasResizing && !this.mTmpRect.equals(this.mWindowDragBounds)) {
                this.mService.mAtmService.resizeTask(this.mTask.mTaskId, this.mWindowDragBounds, 3);
            }
            this.mService.mTaskPositioningController.finishTaskPositioning();
        }
        return true;
    }

    android.graphics.Rect getWindowDragBounds() {
        return this.mWindowDragBounds;
    }

    java.util.concurrent.CompletableFuture<java.lang.Void> register(final com.android.server.wm.DisplayContent displayContent, final com.android.server.wm.WindowState win) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            android.util.Slog.d(TAG, "Registering task positioner");
        }
        if (this.mClientChannel != null) {
            android.util.Slog.e(TAG, "Task positioner already registered");
            return java.util.concurrent.CompletableFuture.completedFuture(null);
        }
        this.mDisplayContent = displayContent;
        this.mClientChannel = this.mService.mInputManager.createInputChannel(TAG);
        this.mInputEventReceiver = new android.view.BatchedInputEventReceiver.SimpleBatchedInputEventReceiver(this.mClientChannel, this.mService.mAnimationHandler.getLooper(), this.mService.mAnimator.getChoreographer(), new android.view.BatchedInputEventReceiver.SimpleBatchedInputEventReceiver.InputEventListener() { // from class: com.android.server.wm.TaskPositioner$$ExternalSyntheticLambda0
            public final boolean onInputEvent(android.view.InputEvent inputEvent) {
                return this.f$0.onInputEvent(inputEvent);
            }
        });
        this.mDragApplicationHandle = new android.view.InputApplicationHandle(new android.os.Binder(), TAG, android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS);
        this.mDragWindowHandle = new android.view.InputWindowHandle(this.mDragApplicationHandle, displayContent.getDisplayId());
        this.mDragWindowHandle.name = TAG;
        this.mDragWindowHandle.token = this.mClientChannel.getToken();
        this.mDragWindowHandle.layoutParamsType = 2016;
        this.mDragWindowHandle.dispatchingTimeoutMillis = android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
        this.mDragWindowHandle.ownerPid = com.android.server.wm.WindowManagerService.MY_PID;
        this.mDragWindowHandle.ownerUid = com.android.server.wm.WindowManagerService.MY_UID;
        this.mDragWindowHandle.scaleFactor = 1.0f;
        this.mDragWindowHandle.inputConfig = 4;
        this.mDragWindowHandle.touchableRegion.setEmpty();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 3007492640459931179L, 0, null, null);
        }
        this.mDisplayContent.getDisplayRotation().pause();
        return this.mService.mTaskPositioningController.showInputSurface(win.getDisplayId()).thenRun(new java.lang.Runnable() { // from class: com.android.server.wm.TaskPositioner$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$register$0(displayContent, win);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$register$0(com.android.server.wm.DisplayContent displayContent, com.android.server.wm.WindowState win) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                android.graphics.Rect displayBounds = this.mTmpRect;
                displayContent.getBounds(displayBounds);
                android.util.DisplayMetrics displayMetrics = displayContent.getDisplayMetrics();
                this.mMinVisibleWidth = com.android.server.wm.WindowManagerService.dipToPixel(48, displayMetrics);
                this.mMinVisibleHeight = com.android.server.wm.WindowManagerService.dipToPixel(32, displayMetrics);
                this.mMaxVisibleSize.set(displayBounds.width(), displayBounds.height());
                this.mDragEnded = false;
                try {
                    this.mClientCallback = win.mClient.asBinder();
                    this.mClientCallback.linkToDeath(this, 0);
                    this.mWindow = win;
                    this.mTask = win.getTask();
                } catch (android.os.RemoteException e) {
                    this.mService.mTaskPositioningController.finishTaskPositioning();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void unregister() {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            android.util.Slog.d(TAG, "Unregistering task positioner");
        }
        if (this.mClientChannel == null) {
            android.util.Slog.e(TAG, "Task positioner not registered");
            return;
        }
        this.mService.mTaskPositioningController.hideInputSurface(this.mDisplayContent.getDisplayId());
        this.mService.mInputManager.removeInputChannel(this.mClientChannel.getToken());
        this.mInputEventReceiver.dispose();
        this.mInputEventReceiver = null;
        this.mClientChannel.dispose();
        this.mClientChannel = null;
        this.mDragWindowHandle = null;
        this.mDragApplicationHandle = null;
        this.mDragEnded = true;
        this.mDisplayContent.getInputMonitor().updateInputWindowsLw(true);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[0]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 5478864901888225320L, 0, null, null);
        }
        this.mDisplayContent.getDisplayRotation().resume();
        this.mDisplayContent = null;
        if (this.mClientCallback != null) {
            this.mClientCallback.unlinkToDeath(this, 0);
        }
        this.mWindow = null;
    }

    void startDrag(boolean resize, boolean preserveOrientation, float startX, float startY) {
        boolean z;
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            android.util.Slog.d(TAG, "startDrag: win=" + this.mWindow + ", resize=" + resize + ", preserveOrientation=" + preserveOrientation + ", {" + startX + ", " + startY + "}");
        }
        final android.graphics.Rect startBounds = this.mTmpRect;
        this.mTask.getBounds(startBounds);
        boolean z2 = false;
        this.mCtrlType = 0;
        this.mStartDragX = startX;
        this.mStartDragY = startY;
        this.mPreserveOrientation = preserveOrientation;
        if (resize) {
            if (startX < startBounds.left) {
                this.mCtrlType |= 1;
            }
            if (startX > startBounds.right) {
                this.mCtrlType |= 2;
            }
            if (startY < startBounds.top) {
                this.mCtrlType |= 4;
            }
            if (startY > startBounds.bottom) {
                this.mCtrlType |= 8;
            }
            if (this.mCtrlType == 0) {
                z = false;
            } else {
                z = true;
            }
            this.mResizing = z;
        }
        if (startBounds.width() >= startBounds.height()) {
            z2 = true;
        }
        this.mStartOrientationWasLandscape = z2;
        this.mWindowOriginalBounds.set(startBounds);
        if (this.mResizing) {
            notifyMoveLocked(startX, startY);
            this.mService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.TaskPositioner$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startDrag$1(startBounds);
                }
            });
        }
        this.mWindowDragBounds.set(startBounds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDrag$1(android.graphics.Rect startBounds) {
        this.mService.mAtmService.resizeTask(this.mTask.mTaskId, startBounds, 3);
    }

    private void endDragLocked() {
        this.mResizing = false;
        this.mTask.setDragResizing(false);
    }

    boolean notifyMoveLocked(float x, float y) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            android.util.Slog.d(TAG, "notifyMoveLocked: {" + x + "," + y + "}");
        }
        if (this.mCtrlType != 0) {
            resizeDrag(x, y);
            this.mTask.setDragResizing(true);
            return false;
        }
        this.mDisplayContent.getStableRect(this.mTmpRect);
        this.mTmpRect.intersect(this.mTask.getRootTask().getParent().getBounds());
        int nX = (int) x;
        int nY = (int) y;
        if (!this.mTmpRect.contains(nX, nY)) {
            nX = java.lang.Math.min(java.lang.Math.max(nX, this.mTmpRect.left), this.mTmpRect.right);
            nY = java.lang.Math.min(java.lang.Math.max(nY, this.mTmpRect.top), this.mTmpRect.bottom);
        }
        updateWindowDragBounds(nX, nY, this.mTmpRect);
        return false;
    }

    void resizeDrag(float x, float y) {
        updateDraggedBounds(com.android.internal.policy.TaskResizingAlgorithm.resizeDrag(x, y, this.mStartDragX, this.mStartDragY, this.mWindowOriginalBounds, this.mCtrlType, this.mMinVisibleWidth, this.mMinVisibleHeight, this.mMaxVisibleSize, this.mPreserveOrientation, this.mStartOrientationWasLandscape));
    }

    private void updateDraggedBounds(android.graphics.Rect newBounds) {
        this.mWindowDragBounds.set(newBounds);
        checkBoundsForOrientationViolations(this.mWindowDragBounds);
    }

    private void checkBoundsForOrientationViolations(android.graphics.Rect bounds) {
    }

    private void updateWindowDragBounds(int x, int y, android.graphics.Rect rootTaskBounds) {
        int offsetX = java.lang.Math.round(x - this.mStartDragX);
        int offsetY = java.lang.Math.round(y - this.mStartDragY);
        this.mWindowDragBounds.set(this.mWindowOriginalBounds);
        int maxLeft = rootTaskBounds.right - this.mMinVisibleWidth;
        int minLeft = (rootTaskBounds.left + this.mMinVisibleWidth) - this.mWindowOriginalBounds.width();
        int minTop = rootTaskBounds.top;
        int maxTop = rootTaskBounds.bottom - this.mMinVisibleHeight;
        this.mWindowDragBounds.offsetTo(java.lang.Math.min(java.lang.Math.max(this.mWindowOriginalBounds.left + offsetX, minLeft), maxLeft), java.lang.Math.min(java.lang.Math.max(this.mWindowOriginalBounds.top + offsetY, minTop), maxTop));
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            android.util.Slog.d(TAG, "updateWindowDragBounds: " + this.mWindowDragBounds);
        }
    }

    public java.lang.String toShortString() {
        return TAG;
    }

    static void setFactory(com.android.server.wm.TaskPositioner.Factory factory) {
        sFactory = factory;
    }

    static com.android.server.wm.TaskPositioner create(com.android.server.wm.WindowManagerService service) {
        if (sFactory == null) {
            sFactory = new com.android.server.wm.TaskPositioner.Factory() { // from class: com.android.server.wm.TaskPositioner.1
            };
        }
        return sFactory.create(service);
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        this.mService.mTaskPositioningController.finishTaskPositioning();
    }

    interface Factory {
        default com.android.server.wm.TaskPositioner create(com.android.server.wm.WindowManagerService service) {
            return new com.android.server.wm.TaskPositioner(service);
        }
    }
}
