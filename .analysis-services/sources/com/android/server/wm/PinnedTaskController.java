package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class PinnedTaskController {
    private static final int DEFER_ORIENTATION_CHANGE_TIMEOUT_MS = 1000;
    private static final java.lang.String TAG = "WindowManager";
    private boolean mDeferOrientationChanging;
    private android.graphics.Rect mDestRotatedBounds;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private boolean mFreezingTaskConfig;
    private int mImeHeight;
    private boolean mIsImeShowing;
    private float mMaxAspectRatio;
    private float mMinAspectRatio;
    private android.view.IPinnedTaskListener mPinnedTaskListener;
    private android.window.PictureInPictureSurfaceTransaction mPipTransaction;
    private final com.android.server.wm.WindowManagerService mService;
    private final com.android.server.wm.PinnedTaskController.PinnedTaskListenerDeathHandler mPinnedTaskListenerDeathHandler = new com.android.server.wm.PinnedTaskController.PinnedTaskListenerDeathHandler();
    private final java.lang.Runnable mDeferOrientationTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.PinnedTaskController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };

    private class PinnedTaskListenerDeathHandler implements android.os.IBinder.DeathRecipient {
        private PinnedTaskListenerDeathHandler() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.PinnedTaskController.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.PinnedTaskController.this.mPinnedTaskListener = null;
                    com.android.server.wm.PinnedTaskController.this.mFreezingTaskConfig = false;
                    com.android.server.wm.PinnedTaskController.this.mDeferOrientationTimeoutRunnable.run();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    PinnedTaskController(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent) {
        this.mService = service;
        this.mDisplayContent = displayContent;
        reloadResources();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mDeferOrientationChanging) {
                    continueOrientationChange();
                    this.mService.mWindowPlacerLocked.requestTraversal();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void onPostDisplayConfigurationChanged() {
        reloadResources();
        this.mFreezingTaskConfig = false;
    }

    private void reloadResources() {
        android.content.res.Resources res = this.mService.mContext.getResources();
        this.mMinAspectRatio = res.getFloat(android.R.dimen.config_hoverTapSlop);
        this.mMaxAspectRatio = res.getFloat(android.R.dimen.config_horizontalScrollFactor);
    }

    void registerPinnedTaskListener(android.view.IPinnedTaskListener listener) {
        try {
            listener.asBinder().linkToDeath(this.mPinnedTaskListenerDeathHandler, 0);
            this.mPinnedTaskListener = listener;
            notifyImeVisibilityChanged(this.mIsImeShowing, this.mImeHeight);
            notifyMovementBoundsChanged(false);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Failed to register pinned task listener", e);
        }
    }

    public boolean isValidPictureInPictureAspectRatio(float aspectRatio) {
        return java.lang.Float.compare(this.mMinAspectRatio, aspectRatio) <= 0 && java.lang.Float.compare(aspectRatio, this.mMaxAspectRatio) <= 0;
    }

    public boolean isValidExpandedPictureInPictureAspectRatio(float aspectRatio) {
        return java.lang.Float.compare(this.mMinAspectRatio, aspectRatio) > 0 || java.lang.Float.compare(aspectRatio, this.mMaxAspectRatio) > 0;
    }

    void deferOrientationChangeForEnteringPipFromFullScreenIfNeeded() {
        if (this.mDisplayContent.getWrapper().getExtImpl().skipDeferOrientationChangeForEnteringPipFromFullScreen()) {
            return;
        }
        com.android.server.wm.ActivityRecord topFullscreen = this.mDisplayContent.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.PinnedTaskController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.PinnedTaskController.lambda$deferOrientationChangeForEnteringPipFromFullScreenIfNeeded$1((com.android.server.wm.ActivityRecord) obj);
            }
        });
        com.android.server.wm.utils.LogUtil.d(TAG, "topFullscreen = " + topFullscreen + ", the top activity determines the display orientation of the PiP window");
        if (topFullscreen == null || topFullscreen.hasFixedRotationTransform()) {
            return;
        }
        int rotation = this.mDisplayContent.rotationForActivityInDifferentOrientation(topFullscreen);
        if (rotation == -1) {
            com.android.server.wm.utils.LogUtil.d(TAG, "Orientation has not changed");
            return;
        }
        this.mDisplayContent.setFixedRotationLaunchingApp(topFullscreen, rotation);
        this.mDeferOrientationChanging = true;
        this.mService.mH.removeCallbacks(this.mDeferOrientationTimeoutRunnable);
        float animatorScale = java.lang.Math.max(1.0f, this.mService.getCurrentAnimatorScale());
        this.mService.mH.postDelayed(this.mDeferOrientationTimeoutRunnable, (int) (1000.0f * animatorScale));
    }

    static /* synthetic */ boolean lambda$deferOrientationChangeForEnteringPipFromFullScreenIfNeeded$1(com.android.server.wm.ActivityRecord a) {
        return a.providesOrientation() && !a.getTask().inMultiWindowMode() && a.getWindowingMode() != 100 && (a.getWrapper().getExtImpl() == null || !a.getWrapper().getExtImpl().isCompactWindowingMode(a.getWindowingMode()));
    }

    boolean shouldDeferOrientationChange() {
        return this.mDeferOrientationChanging;
    }

    void setEnterPipBounds(android.graphics.Rect bounds) {
        if (!this.mDeferOrientationChanging) {
            return;
        }
        this.mFreezingTaskConfig = true;
        this.mDestRotatedBounds = new android.graphics.Rect(bounds);
        if (!this.mDisplayContent.mTransitionController.isShellTransitionsEnabled()) {
            continueOrientationChange();
        }
    }

    void setEnterPipTransaction(android.window.PictureInPictureSurfaceTransaction tx) {
        this.mFreezingTaskConfig = true;
        this.mPipTransaction = tx;
    }

    private void continueOrientationChange() {
        this.mDeferOrientationChanging = false;
        this.mService.mH.removeCallbacks(this.mDeferOrientationTimeoutRunnable);
        com.android.server.wm.WindowContainer<?> orientationSource = this.mDisplayContent.getLastOrientationSource();
        if (orientationSource != null && !orientationSource.isAppTransitioning()) {
            this.mDisplayContent.continueUpdateOrientationForDiffOrienLaunchingApp();
        }
    }

    void startSeamlessRotationIfNeeded(android.view.SurfaceControl.Transaction t, int oldRotation, int newRotation) {
        com.android.server.wm.TaskDisplayArea taskArea;
        com.android.server.wm.Task pinnedTask;
        android.graphics.Rect bounds = this.mDestRotatedBounds;
        android.window.PictureInPictureSurfaceTransaction pipTx = this.mPipTransaction;
        boolean emptyPipPositionTx = pipTx == null || pipTx.mPosition == null;
        if ((bounds == null && emptyPipPositionTx) || (pinnedTask = (taskArea = this.mDisplayContent.getDefaultTaskDisplayArea()).getRootPinnedTask()) == null) {
            return;
        }
        android.graphics.Rect sourceHintRect = null;
        this.mDestRotatedBounds = null;
        this.mPipTransaction = null;
        android.graphics.Rect areaBounds = taskArea.getBounds();
        if (!emptyPipPositionTx) {
            float dx = pipTx.mPosition.x;
            float dy = pipTx.mPosition.y;
            android.graphics.Matrix matrix = pipTx.getMatrix();
            if (pipTx.mRotation == 90.0f) {
                dx = pipTx.mPosition.y;
                dy = areaBounds.right - pipTx.mPosition.x;
                matrix.postRotate(-90.0f);
            } else if (pipTx.mRotation == -90.0f) {
                dx = areaBounds.bottom - pipTx.mPosition.y;
                dy = pipTx.mPosition.x;
                matrix.postRotate(90.0f);
            }
            matrix.postTranslate(dx, dy);
            android.view.SurfaceControl leash = pinnedTask.getSurfaceControl();
            t.setMatrix(leash, matrix, new float[9]);
            if (pipTx.hasCornerRadiusSet()) {
                t.setCornerRadius(leash, pipTx.mCornerRadius);
            }
            android.util.Slog.i(TAG, "Seamless rotation PiP tx=" + pipTx + " pos=" + dx + "," + dy);
            return;
        }
        android.app.PictureInPictureParams params = pinnedTask.getPictureInPictureParams();
        if (params != null && params.hasSourceBoundsHint()) {
            sourceHintRect = params.getSourceRectHint();
        }
        android.util.Slog.i(TAG, "Seamless rotation PiP bounds=" + bounds + " hintRect=" + sourceHintRect);
        int rotationDelta = android.util.RotationUtils.deltaRotation(oldRotation, newRotation);
        if (sourceHintRect != null && rotationDelta == 3 && pinnedTask.getDisplayCutoutInsets() != null) {
            int rotationBackDelta = android.util.RotationUtils.deltaRotation(newRotation, oldRotation);
            android.graphics.Rect displayCutoutInsets = android.util.RotationUtils.rotateInsets(android.graphics.Insets.of(pinnedTask.getDisplayCutoutInsets()), rotationBackDelta).toRect();
            sourceHintRect.offset(displayCutoutInsets.left, displayCutoutInsets.top);
        }
        android.graphics.Rect contentBounds = (sourceHintRect == null || !areaBounds.contains(sourceHintRect)) ? areaBounds : sourceHintRect;
        int w = contentBounds.width();
        int h = contentBounds.height();
        float scale = w <= h ? bounds.width() / w : bounds.height() / h;
        int insetLeft = (int) (((contentBounds.left - areaBounds.left) * scale) + 0.5f);
        int insetTop = (int) (((contentBounds.top - areaBounds.top) * scale) + 0.5f);
        android.graphics.Matrix matrix2 = new android.graphics.Matrix();
        matrix2.setScale(scale, scale);
        float f = bounds.left - insetLeft;
        int insetLeft2 = bounds.top;
        matrix2.postTranslate(f, insetLeft2 - insetTop);
        t.setMatrix(pinnedTask.getSurfaceControl(), matrix2, new float[9]);
    }

    boolean isFreezingTaskConfig(com.android.server.wm.Task task) {
        return this.mFreezingTaskConfig && task == this.mDisplayContent.getDefaultTaskDisplayArea().getRootPinnedTask();
    }

    void onCancelFixedRotationTransform() {
        this.mFreezingTaskConfig = false;
        this.mDeferOrientationChanging = false;
        this.mDestRotatedBounds = null;
        this.mPipTransaction = null;
    }

    void onActivityHidden(android.content.ComponentName componentName) {
        if (this.mPinnedTaskListener == null) {
            return;
        }
        try {
            this.mPinnedTaskListener.onActivityHidden(componentName);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error delivering reset reentry fraction event.", e);
        }
    }

    void setAdjustedForIme(boolean adjustedForIme, int imeHeight) {
        boolean imeShowing = adjustedForIme && imeHeight > 0;
        int imeHeight2 = imeShowing ? imeHeight : 0;
        if (imeShowing == this.mIsImeShowing && imeHeight2 == this.mImeHeight) {
            return;
        }
        this.mIsImeShowing = imeShowing;
        this.mImeHeight = imeHeight2;
        notifyImeVisibilityChanged(imeShowing, imeHeight2);
        notifyMovementBoundsChanged(true);
    }

    private void notifyImeVisibilityChanged(boolean imeVisible, int imeHeight) {
        if (this.mPinnedTaskListener != null) {
            try {
                this.mPinnedTaskListener.onImeVisibilityChanged(imeVisible, imeHeight);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error delivering bounds changed event.", e);
            }
        }
    }

    private void notifyMovementBoundsChanged(boolean fromImeAdjustment) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mPinnedTaskListener == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                try {
                    this.mPinnedTaskListener.onMovementBoundsChanged(fromImeAdjustment);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Error delivering actions changed event.", e);
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.println(prefix + "PinnedTaskController");
        if (this.mDeferOrientationChanging) {
            pw.println(prefix + "  mDeferOrientationChanging=true");
        }
        if (this.mFreezingTaskConfig) {
            pw.println(prefix + "  mFreezingTaskConfig=true");
        }
        if (this.mDestRotatedBounds != null) {
            pw.println(prefix + "  mPendingBounds=" + this.mDestRotatedBounds);
        }
        if (this.mPipTransaction != null) {
            pw.println(prefix + "  mPipTransaction=" + this.mPipTransaction);
        }
        pw.println(prefix + "  mIsImeShowing=" + this.mIsImeShowing);
        pw.println(prefix + "  mImeHeight=" + this.mImeHeight);
        pw.println(prefix + "  mMinAspectRatio=" + this.mMinAspectRatio);
        pw.println(prefix + "  mMaxAspectRatio=" + this.mMaxAspectRatio);
    }
}
