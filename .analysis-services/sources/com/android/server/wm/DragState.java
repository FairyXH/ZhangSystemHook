package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DragState {
    private static final java.lang.String ANIMATED_PROPERTY_ALPHA = "alpha";
    private static final java.lang.String ANIMATED_PROPERTY_SCALE = "scale";
    private static final java.lang.String ANIMATED_PROPERTY_X = "x";
    private static final java.lang.String ANIMATED_PROPERTY_Y = "y";
    private static final int DRAG_FLAGS_URI_ACCESS = 3;
    private static final int DRAG_FLAGS_URI_PERMISSIONS = 195;
    private static final long MAX_ANIMATION_DURATION_MS = 375;
    private static final long MIN_ANIMATION_DURATION_MS = 195;
    android.animation.ValueAnimator mAnimator;
    boolean mCrossProfileCopyAllowed;
    float mCurrentX;
    float mCurrentY;
    android.content.ClipData mData;
    android.content.ClipDescription mDataDescription;
    com.android.server.wm.DisplayContent mDisplayContent;
    final com.android.server.wm.DragDropController mDragDropController;
    boolean mDragInProgress;
    boolean mDragResult;
    int mFlags;
    com.android.server.wm.DragState.InputInterceptor mInputInterceptor;
    android.view.SurfaceControl mInputSurface;
    private boolean mIsClosing;
    android.os.IBinder mLocalWin;
    float mOriginalAlpha;
    float mOriginalX;
    float mOriginalY;
    int mPid;
    boolean mRelinquishDragSurfaceToDropTarget;
    final com.android.server.wm.WindowManagerService mService;
    int mSourceUserId;
    android.view.SurfaceControl mSurfaceControl;
    float mThumbOffsetX;
    float mThumbOffsetY;
    android.os.IBinder mToken;
    final android.view.SurfaceControl.Transaction mTransaction;
    int mUid;
    android.view.DragEvent mUnhandledDropEvent;
    public int mWindowFlag;
    float mAnimatedScale = 1.0f;
    volatile boolean mAnimationCompleted = false;
    private final android.view.animation.Interpolator mCubicEaseOutInterpolator = new android.view.animation.DecelerateInterpolator(1.5f);
    private final android.graphics.Point mDisplaySize = new android.graphics.Point();
    private final android.graphics.Rect mTmpClipRect = new android.graphics.Rect();
    private com.android.server.wm.DragState.DragStateWrapper mDragStateWrapper = new com.android.server.wm.DragState.DragStateWrapper();
    java.util.ArrayList<com.android.server.wm.WindowState> mNotifiedWindows = new java.util.ArrayList<>();

    DragState(com.android.server.wm.WindowManagerService service, com.android.server.wm.DragDropController controller, android.os.IBinder token, android.view.SurfaceControl surface, int flags, android.os.IBinder localWin) {
        this.mService = service;
        this.mDragDropController = controller;
        this.mToken = token;
        this.mSurfaceControl = surface;
        this.mFlags = flags;
        this.mLocalWin = localWin;
        this.mTransaction = service.mTransactionFactory.get();
        android.view.WindowManager.LayoutParams params = this.mService.mWindowMap.get(this.mLocalWin).getAttrs();
        if (params != null && android.view.OplusBaseLayoutParams.class.isInstance(params)) {
            this.mWindowFlag = ((android.view.OplusBaseLayoutParams) params).oplusFlags;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
            android.util.Slog.d("WindowManager", "get oplusFlags from LayoutParams, mWindowFlag = " + this.mWindowFlag);
        }
    }

    public android.graphics.Point getDisplaySize() {
        return this.mDisplaySize;
    }

    boolean isClosing() {
        return this.mIsClosing;
    }

    private java.util.concurrent.CompletableFuture<java.lang.Void> showInputSurface() {
        if (this.mInputSurface == null) {
            this.mInputSurface = this.mService.makeSurfaceBuilder(this.mDisplayContent.getSession()).setContainerLayer().setName("Drag and Drop Input Consumer").setCallsite("DragState.showInputSurface").setParent(this.mDisplayContent.getOverlayLayer()).build();
        }
        android.view.InputWindowHandle h = getInputWindowHandle();
        if (h == null) {
            android.util.Slog.w("WindowManager", "Drag is in progress but there is no drag window handle.");
            return java.util.concurrent.CompletableFuture.completedFuture(null);
        }
        this.mTmpClipRect.set(0, 0, this.mDisplaySize.x, this.mDisplaySize.y);
        h.setTrustedOverlay(this.mTransaction, this.mInputSurface, true);
        this.mTransaction.show(this.mInputSurface).setInputWindowInfo(this.mInputSurface, h).setLayer(this.mInputSurface, Integer.MAX_VALUE).setCrop(this.mInputSurface, this.mTmpClipRect);
        final java.util.concurrent.CompletableFuture<java.lang.Void> result = new java.util.concurrent.CompletableFuture<>();
        this.mTransaction.addWindowInfosReportedListener(new java.lang.Runnable() { // from class: com.android.server.wm.DragState$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                result.complete(null);
            }
        }).apply();
        return result;
    }

    void closeLocked() {
        android.view.SurfaceControl dragSurface;
        float y;
        float y2;
        this.mIsClosing = true;
        if (this.mInputInterceptor != null) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                android.util.Slog.d("WindowManager", "Unregistering drag input channel");
            }
            this.mDragDropController.sendHandlerMessage(1, this.mInputInterceptor);
            this.mInputInterceptor = null;
        }
        if (this.mDragInProgress) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                android.util.Slog.d("WindowManager", "Broadcasting DRAG_ENDED");
            }
            for (com.android.server.wm.WindowState ws : this.mNotifiedWindows) {
                if (!this.mDragResult && ws.mSession.mPid == this.mPid) {
                    float x = ws.translateToWindowX(this.mCurrentX);
                    float y3 = ws.translateToWindowY(this.mCurrentY);
                    if (!relinquishDragSurfaceToDragSource()) {
                        dragSurface = null;
                        y = y3;
                        y2 = x;
                    } else {
                        android.view.SurfaceControl dragSurface2 = this.mSurfaceControl;
                        dragSurface = dragSurface2;
                        y = y3;
                        y2 = x;
                    }
                } else {
                    dragSurface = null;
                    y = 0.0f;
                    y2 = 0.0f;
                }
                android.view.DragEvent event = android.view.DragEvent.obtain(4, y2, y, this.mThumbOffsetX, this.mThumbOffsetY, this.mFlags, null, null, null, dragSurface, null, this.mDragResult);
                try {
                    if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                        android.util.Slog.d("WindowManager", "Sending DRAG_ENDED to " + ws);
                    }
                    ws.mClient.dispatchDragEvent(event);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w("WindowManager", "Unable to drag-end window " + ws);
                }
                if (com.android.server.wm.WindowManagerService.MY_PID != ws.mSession.mPid) {
                    event.recycle();
                }
            }
            this.mNotifiedWindows.clear();
            this.mDragInProgress = false;
            android.os.Trace.instant(32L, "DragDropController#DRAG_ENDED");
        }
        if (this.mInputSurface != null) {
            this.mTransaction.remove(this.mInputSurface).apply();
            this.mInputSurface = null;
        }
        this.mDragDropController.mDragDropControllerExt.notifyDnDSplitScreenCloseIfNeed();
        if (this.mSurfaceControl != null) {
            if (this.mRelinquishDragSurfaceToDropTarget || relinquishDragSurfaceToDragSource()) {
                this.mDragDropController.sendTimeoutMessage(3, this.mSurfaceControl, 5000L);
            } else {
                this.mTransaction.remove(this.mSurfaceControl).apply();
            }
            this.mSurfaceControl = null;
        }
        if (this.mAnimator != null && !this.mAnimationCompleted) {
            android.util.Slog.wtf("WindowManager", "Unexpectedly destroying mSurfaceControl while animation is running");
        }
        this.mFlags = 0;
        this.mLocalWin = null;
        this.mToken = null;
        this.mData = null;
        this.mThumbOffsetY = 0.0f;
        this.mThumbOffsetX = 0.0f;
        this.mNotifiedWindows = null;
        if (this.mUnhandledDropEvent != null) {
            this.mUnhandledDropEvent.recycle();
            this.mUnhandledDropEvent = null;
        }
        this.mDragDropController.onDragStateClosedLocked(this);
    }

    private android.view.DragEvent createDropEvent(float x, float y, com.android.server.wm.WindowState touchedWin, boolean includePrivateInfo) {
        com.android.server.wm.DragAndDropPermissionsHandler dragAndDropPermissions;
        if (touchedWin != null) {
            int targetUserId = android.os.UserHandle.getUserId(touchedWin.getOwningUid());
            if ((this.mFlags & 256) != 0 && (this.mFlags & 3) != 0 && this.mData != null) {
                dragAndDropPermissions = new com.android.server.wm.DragAndDropPermissionsHandler(this.mService.mGlobalLock, this.mData, this.mUid, touchedWin.getOwningPackage(), this.mFlags & 195, this.mSourceUserId, targetUserId);
            } else {
                dragAndDropPermissions = null;
            }
            if (this.mSourceUserId != targetUserId && this.mData != null) {
                this.mData.fixUris(this.mSourceUserId);
            }
            boolean targetInterceptsGlobalDrag = targetInterceptsGlobalDrag(touchedWin);
            return obtainDragEvent(3, x, y, this.mData, targetInterceptsGlobalDrag, targetInterceptsGlobalDrag, dragAndDropPermissions);
        }
        return obtainDragEvent(3, x, y, this.mData, includePrivateInfo, includePrivateInfo, null);
    }

    boolean reportDropWindowLock(android.os.IBinder token, float x, float y) {
        if (this.mAnimator != null || this.mIsClosing) {
            return false;
        }
        try {
            android.os.Trace.traceBegin(32L, "DragDropController#DROP");
            return reportDropWindowLockInner(token, x, y);
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    private boolean reportDropWindowLockInner(android.os.IBinder token, float x, float y) {
        if (this.mAnimator != null) {
            return false;
        }
        com.android.server.wm.WindowState touchedWin = this.mService.mInputToWindowMap.get(token);
        android.view.DragEvent unhandledDropEvent = createDropEvent(x, y, null, true);
        if (!isWindowNotified(touchedWin)) {
            if (this.mDragDropController.notifyUnhandledDrop(unhandledDropEvent, "unhandled-drop")) {
                return true;
            }
            android.os.Trace.traceBegin(32L, "DragDropController#noWindow");
            endDragLocked(false, false);
            this.mDragDropController.mDragDropControllerExt.postCancelDragAndDrop();
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                android.util.Slog.d("WindowManager", "Drop outside a valid window " + touchedWin);
            }
            android.os.Trace.traceEnd(32L);
            return false;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
            android.util.Slog.d("WindowManager", "Sending DROP to " + touchedWin);
        }
        android.os.IBinder clientToken = touchedWin.mClient.asBinder();
        this.mDragDropController.mDragDropControllerExt.adjustYForZoomWinIfNeed(touchedWin, x, y);
        android.view.DragEvent event = createDropEvent(x, y, touchedWin, false);
        this.mDragDropController.mDragDropControllerExt.notifyDnDSplitScreenDrop(x, y);
        try {
            try {
                android.os.Trace.traceBegin(32L, "DragDropController#dispatchDrop");
                touchedWin.mClient.dispatchDragEvent(event);
                this.mDragDropController.sendTimeoutMessage(0, clientToken, 5000L);
                if (com.android.server.wm.WindowManagerService.MY_PID != touchedWin.mSession.mPid) {
                    event.recycle();
                }
                android.os.Trace.traceEnd(32L);
                this.mToken = clientToken;
                this.mUnhandledDropEvent = unhandledDropEvent;
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w("WindowManager", "can't send drop notification to win " + touchedWin);
                endDragLocked(false, false);
                if (com.android.server.wm.WindowManagerService.MY_PID != touchedWin.mSession.mPid) {
                    event.recycle();
                }
                android.os.Trace.traceEnd(32L);
                return false;
            }
        } catch (java.lang.Throwable th) {
            if (com.android.server.wm.WindowManagerService.MY_PID != touchedWin.mSession.mPid) {
                event.recycle();
            }
            android.os.Trace.traceEnd(32L);
            throw th;
        }
    }

    class InputInterceptor {
        android.view.InputChannel mClientChannel;
        android.view.InputApplicationHandle mDragApplicationHandle = new android.view.InputApplicationHandle(new android.os.Binder(), "drag", android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS);
        android.view.InputWindowHandle mDragWindowHandle;
        com.android.server.wm.DragInputEventReceiver mInputEventReceiver;

        InputInterceptor(android.view.Display display) {
            this.mClientChannel = com.android.server.wm.DragState.this.mService.mInputManager.createInputChannel("drag");
            this.mInputEventReceiver = new com.android.server.wm.DragInputEventReceiver(this.mClientChannel, com.android.server.wm.DragState.this.mService.mH.getLooper(), com.android.server.wm.DragState.this.mDragDropController);
            this.mDragWindowHandle = new android.view.InputWindowHandle(this.mDragApplicationHandle, display.getDisplayId());
            this.mDragWindowHandle.name = "drag";
            this.mDragWindowHandle.token = this.mClientChannel.getToken();
            this.mDragWindowHandle.layoutParamsType = 2016;
            this.mDragWindowHandle.dispatchingTimeoutMillis = android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
            this.mDragWindowHandle.ownerPid = com.android.server.wm.WindowManagerService.MY_PID;
            this.mDragWindowHandle.ownerUid = com.android.server.wm.WindowManagerService.MY_UID;
            this.mDragWindowHandle.scaleFactor = 1.0f;
            this.mDragWindowHandle.touchableRegion.setEmpty();
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 7928129513685401229L, 0, null, null);
            }
            com.android.server.wm.DragState.this.mService.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.DragState$InputInterceptor$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wm.DisplayContent) obj).getDisplayRotation().pause();
                }
            });
        }

        void tearDown() {
            com.android.server.wm.DragState.this.mService.mInputManager.removeInputChannel(this.mClientChannel.getToken());
            this.mInputEventReceiver.dispose();
            this.mInputEventReceiver = null;
            this.mClientChannel.dispose();
            this.mClientChannel = null;
            this.mDragWindowHandle = null;
            this.mDragApplicationHandle = null;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[0]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 8231481023986546563L, 0, null, null);
            }
            com.android.server.wm.DragState.this.mService.mRoot.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.DragState$InputInterceptor$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wm.DisplayContent) obj).getDisplayRotation().resume();
                }
            });
        }
    }

    android.view.InputChannel getInputChannel() {
        if (this.mInputInterceptor == null) {
            return null;
        }
        return this.mInputInterceptor.mClientChannel;
    }

    android.view.InputWindowHandle getInputWindowHandle() {
        if (this.mInputInterceptor == null) {
            return null;
        }
        return this.mInputInterceptor.mDragWindowHandle;
    }

    android.os.IBinder getInputToken() {
        if (this.mInputInterceptor == null || this.mInputInterceptor.mClientChannel == null) {
            return null;
        }
        return this.mInputInterceptor.mClientChannel.getToken();
    }

    java.util.concurrent.CompletableFuture<java.lang.Void> register(android.view.Display display) {
        display.getRealSize(this.mDisplaySize);
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
            android.util.Slog.d("WindowManager", "Registering drag input channel");
        }
        if (this.mInputInterceptor != null) {
            android.util.Slog.e("WindowManager", "Duplicate register of drag input channel");
            return java.util.concurrent.CompletableFuture.completedFuture(null);
        }
        this.mInputInterceptor = new com.android.server.wm.DragState.InputInterceptor(display);
        return showInputSurface();
    }

    void broadcastDragStartedLocked(final float touchX, final float touchY) {
        android.os.Trace.instant(32L, "DragDropController#DRAG_STARTED");
        this.mCurrentX = touchX;
        this.mOriginalX = touchX;
        this.mCurrentY = touchY;
        this.mOriginalY = touchY;
        this.mDataDescription = this.mData != null ? this.mData.getDescription() : null;
        this.mNotifiedWindows.clear();
        this.mDragInProgress = true;
        this.mDragDropController.mDragDropControllerExt.notifyDnDSplitScreenStartIfNeed(this);
        this.mSourceUserId = android.os.UserHandle.getUserId(this.mUid);
        com.android.server.pm.UserManagerInternal userManager = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        this.mCrossProfileCopyAllowed = true ^ userManager.getUserRestriction(this.mSourceUserId, "no_cross_profile_copy_paste");
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
            android.util.Slog.d("WindowManager", "Broadcasting DRAG_STARTED at (" + touchX + ", " + touchY + ")");
        }
        final boolean containsAppExtras = containsApplicationExtras(this.mDataDescription);
        this.mService.mRoot.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.DragState$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$broadcastDragStartedLocked$1(touchX, touchY, containsAppExtras, (com.android.server.wm.WindowState) obj);
            }
        }, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: sendDragStartedLocked, reason: merged with bridge method [inline-methods] */
    public void lambda$broadcastDragStartedLocked$1(com.android.server.wm.WindowState newWin, float touchX, float touchY, boolean containsAppExtras) {
        boolean interceptsGlobalDrag = targetInterceptsGlobalDrag(newWin);
        if (this.mDragInProgress && isValidDropTarget(newWin, containsAppExtras, interceptsGlobalDrag)) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                android.util.Slog.d("WindowManager", "Sending DRAG_STARTED to new window " + newWin);
            }
            android.content.ClipData data = interceptsGlobalDrag ? this.mData.copyForTransferWithActivityInfo() : null;
            android.view.DragEvent event = obtainDragEvent(1, newWin.translateToWindowX(touchX), newWin.translateToWindowY(touchY), data, interceptsGlobalDrag, true, null);
            try {
                try {
                    newWin.mClient.dispatchDragEvent(event);
                    this.mNotifiedWindows.add(newWin);
                    if (com.android.server.wm.WindowManagerService.MY_PID == newWin.mSession.mPid) {
                        return;
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w("WindowManager", "Unable to drag-start window " + newWin);
                    if (com.android.server.wm.WindowManagerService.MY_PID == newWin.mSession.mPid) {
                        return;
                    }
                }
                event.recycle();
            } catch (java.lang.Throwable th) {
                if (com.android.server.wm.WindowManagerService.MY_PID != newWin.mSession.mPid) {
                    event.recycle();
                }
                throw th;
            }
        }
    }

    private boolean containsApplicationExtras(android.content.ClipDescription desc) {
        if (desc == null) {
            return false;
        }
        return desc.hasMimeType("application/vnd.android.activity") || desc.hasMimeType("application/vnd.android.shortcut") || desc.hasMimeType("application/vnd.android.task");
    }

    private boolean isValidDropTarget(com.android.server.wm.WindowState targetWin, boolean containsAppExtras, boolean interceptsGlobalDrag) {
        if (targetWin == null) {
            return false;
        }
        boolean isLocalWindow = this.mLocalWin == targetWin.mClient.asBinder();
        if ((!isLocalWindow && !interceptsGlobalDrag && containsAppExtras) || !targetWin.isPotentialDragTarget(interceptsGlobalDrag)) {
            return false;
        }
        boolean isGlobalSameAppDrag = (this.mFlags & 4096) != 0;
        boolean isGlobalDrag = (this.mFlags & 256) != 0;
        boolean isAnyGlobalDrag = isGlobalDrag || isGlobalSameAppDrag;
        if ((!isAnyGlobalDrag || !targetWindowSupportsGlobalDrag(targetWin)) && !isLocalWindow) {
            return false;
        }
        if (!isGlobalSameAppDrag || interceptsGlobalDrag || this.mUid == targetWin.getUid()) {
            return interceptsGlobalDrag || this.mCrossProfileCopyAllowed || this.mSourceUserId == android.os.UserHandle.getUserId(targetWin.getOwningUid());
        }
        return false;
    }

    private boolean targetWindowSupportsGlobalDrag(com.android.server.wm.WindowState targetWin) {
        return targetWin.mActivityRecord == null || targetWin.mActivityRecord.mTargetSdk >= 24;
    }

    public boolean targetInterceptsGlobalDrag(com.android.server.wm.WindowState targetWin) {
        return (targetWin == null || (targetWin.mAttrs.privateFlags & Integer.MIN_VALUE) == 0) ? false : true;
    }

    void sendDragStartedIfNeededLocked(com.android.server.wm.WindowState newWin) {
        if (!this.mDragInProgress || isWindowNotified(newWin)) {
            return;
        }
        if (this.mData == null) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_DRAG) {
                android.util.Slog.i("WindowManager", "mData is null.");
                return;
            }
            return;
        }
        lambda$broadcastDragStartedLocked$1(newWin, this.mCurrentX, this.mCurrentY, containsApplicationExtras(this.mDataDescription));
    }

    boolean isWindowNotified(com.android.server.wm.WindowState newWin) {
        for (com.android.server.wm.WindowState ws : this.mNotifiedWindows) {
            if (ws == newWin) {
                return true;
            }
        }
        return false;
    }

    void endDragLocked(boolean dropConsumed, boolean relinquishDragSurfaceToDropTarget) {
        this.mDragResult = dropConsumed;
        this.mRelinquishDragSurfaceToDropTarget = relinquishDragSurfaceToDropTarget;
        if (this.mAnimator != null) {
            return;
        }
        this.mAnimator = this.mDragDropController.mDragDropControllerExt.createCustormAnimatorIfNeed(Integer.MIN_VALUE, this);
        if (this.mAnimator != null) {
            return;
        }
        if (!this.mDragResult && !isAccessibilityDragDrop() && !relinquishDragSurfaceToDragSource()) {
            if ((this.mWindowFlag & 2048) != 0) {
                this.mAnimator = this.mDragDropController.mDragDropControllerExt.createReturnAnimationIfNeed(this);
                return;
            } else {
                this.mAnimator = createReturnAnimationLocked();
                return;
            }
        }
        closeLocked();
    }

    void cancelDragLocked(boolean skipAnimation) {
        if (this.mAnimator != null) {
            return;
        }
        if (!this.mDragInProgress || skipAnimation || isAccessibilityDragDrop()) {
            closeLocked();
            return;
        }
        this.mAnimator = this.mDragDropController.mDragDropControllerExt.createCustormAnimatorIfNeed(1073741824, this);
        if (this.mAnimator != null) {
            return;
        }
        this.mAnimator = createCancelAnimationLocked();
    }

    void updateDragSurfaceLocked(boolean keepHandling, float x, float y) {
        if (this.mAnimator != null) {
            return;
        }
        this.mCurrentX = x;
        this.mCurrentY = y;
        if (!keepHandling) {
            return;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
            android.util.Slog.i("WindowManager", ">>> OPEN TRANSACTION notifyMoveLocked");
        }
        this.mTransaction.setPosition(this.mSurfaceControl, x - this.mThumbOffsetX, y - this.mThumbOffsetY).apply();
        this.mDragDropController.mDragDropControllerExt.notifyDnDSplitScreenLocation(x, y);
        this.mDragDropController.mDragDropControllerExt.handleZoomDrag(x, y);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mSurfaceControl);
            long protoLogParam1 = (int) (x - this.mThumbOffsetX);
            long protoLogParam2 = (int) (y - this.mThumbOffsetY);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, 12662399232325663L, 20, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2));
        }
    }

    boolean isInProgress() {
        return this.mDragInProgress;
    }

    private android.view.DragEvent obtainDragEvent(int action, float x, float y, android.content.ClipData data, boolean includeDragSurface, boolean includeDragFlags, com.android.internal.view.IDragAndDropPermissions dragAndDropPermissions) {
        return android.view.DragEvent.obtain(action, x, y, this.mThumbOffsetX, this.mThumbOffsetY, includeDragFlags ? this.mFlags : 0, null, this.mDataDescription, data, includeDragSurface ? this.mSurfaceControl : null, dragAndDropPermissions, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.animation.ValueAnimator createReturnAnimationLocked() {
        final android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofPropertyValuesHolder(android.animation.PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_X, this.mCurrentX - this.mThumbOffsetX, this.mOriginalX - this.mThumbOffsetX), android.animation.PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_Y, this.mCurrentY - this.mThumbOffsetY, this.mOriginalY - this.mThumbOffsetY), android.animation.PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_SCALE, this.mAnimatedScale, this.mAnimatedScale), android.animation.PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_ALPHA, this.mOriginalAlpha, this.mOriginalAlpha / 2.0f));
        float translateX = this.mOriginalX - this.mCurrentX;
        float translateY = this.mOriginalY - this.mCurrentY;
        double travelDistance = java.lang.Math.sqrt((translateX * translateX) + (translateY * translateY));
        double displayDiagonal = java.lang.Math.sqrt((this.mDisplaySize.x * this.mDisplaySize.x) + (this.mDisplaySize.y * this.mDisplaySize.y));
        long duration = ((long) ((travelDistance / displayDiagonal) * 180.0d)) + MIN_ANIMATION_DURATION_MS;
        com.android.server.wm.DragState.AnimationListener listener = new com.android.server.wm.DragState.AnimationListener();
        animator.setDuration(duration);
        animator.setInterpolator(this.mCubicEaseOutInterpolator);
        animator.addListener(listener);
        animator.addUpdateListener(listener);
        this.mService.mAnimationHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.DragState$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                animator.start();
            }
        });
        return animator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.animation.ValueAnimator createCancelAnimationLocked() {
        final android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofPropertyValuesHolder(android.animation.PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_X, this.mCurrentX - this.mThumbOffsetX, this.mCurrentX), android.animation.PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_Y, this.mCurrentY - this.mThumbOffsetY, this.mCurrentY), android.animation.PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_SCALE, this.mAnimatedScale, 0.0f), android.animation.PropertyValuesHolder.ofFloat(ANIMATED_PROPERTY_ALPHA, this.mOriginalAlpha, 0.0f));
        com.android.server.wm.DragState.AnimationListener listener = new com.android.server.wm.DragState.AnimationListener();
        animator.setDuration(MIN_ANIMATION_DURATION_MS);
        animator.setInterpolator(this.mCubicEaseOutInterpolator);
        animator.addListener(listener);
        animator.addUpdateListener(listener);
        this.mService.mAnimationHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.DragState$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                animator.start();
            }
        });
        return animator;
    }

    private class AnimationListener implements android.animation.ValueAnimator.AnimatorUpdateListener, android.animation.Animator.AnimatorListener {
        private AnimationListener() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(android.animation.ValueAnimator animation) {
            android.view.SurfaceControl.Transaction transaction = com.android.server.wm.DragState.this.mService.mTransactionFactory.get();
            try {
                if (com.android.server.wm.DragState.this.mSurfaceControl != null && com.android.server.wm.DragState.this.mSurfaceControl.isValid()) {
                    transaction.setPosition(com.android.server.wm.DragState.this.mSurfaceControl, ((java.lang.Float) animation.getAnimatedValue(com.android.server.wm.DragState.ANIMATED_PROPERTY_X)).floatValue(), ((java.lang.Float) animation.getAnimatedValue(com.android.server.wm.DragState.ANIMATED_PROPERTY_Y)).floatValue());
                    transaction.setAlpha(com.android.server.wm.DragState.this.mSurfaceControl, ((java.lang.Float) animation.getAnimatedValue(com.android.server.wm.DragState.ANIMATED_PROPERTY_ALPHA)).floatValue());
                    transaction.setMatrix(com.android.server.wm.DragState.this.mSurfaceControl, ((java.lang.Float) animation.getAnimatedValue(com.android.server.wm.DragState.ANIMATED_PROPERTY_SCALE)).floatValue(), 0.0f, 0.0f, ((java.lang.Float) animation.getAnimatedValue(com.android.server.wm.DragState.ANIMATED_PROPERTY_SCALE)).floatValue());
                    transaction.apply();
                }
                if (transaction != null) {
                    transaction.close();
                }
            } catch (java.lang.Throwable th) {
                if (transaction != null) {
                    try {
                        transaction.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(android.animation.Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(android.animation.Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(android.animation.Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(android.animation.Animator animator) {
            com.android.server.wm.DragState.this.mAnimationCompleted = true;
            com.android.server.wm.DragState.this.mDragDropController.sendHandlerMessage(2, null);
        }
    }

    boolean isAccessibilityDragDrop() {
        return (this.mFlags & 1024) != 0;
    }

    private boolean relinquishDragSurfaceToDragSource() {
        return (this.mFlags & 2048) != 0;
    }

    public com.android.server.wm.IDragStateWrapper getWrapper() {
        return this.mDragStateWrapper;
    }

    private class DragStateWrapper implements com.android.server.wm.IDragStateWrapper {
        private DragStateWrapper() {
        }

        @Override // com.android.server.wm.IDragStateWrapper
        public android.animation.ValueAnimator createCancelAnimationLocked() {
            return com.android.server.wm.DragState.this.createCancelAnimationLocked();
        }

        @Override // com.android.server.wm.IDragStateWrapper
        public android.animation.ValueAnimator createReturnAnimationLocked() {
            return com.android.server.wm.DragState.this.createReturnAnimationLocked();
        }
    }
}
