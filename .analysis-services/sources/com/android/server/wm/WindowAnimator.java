package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class WindowAnimator {
    private static final java.lang.String TAG = "WindowManager";
    final android.view.Choreographer.FrameCallback mAnimationFrameCallback;
    private boolean mAnimationFrameCallbackScheduled;
    private android.view.Choreographer mChoreographer;
    final android.content.Context mContext;
    long mCurrentTime;
    private boolean mInExecuteAfterPrepareSurfacesRunnables;
    private boolean mLastRootAnimating;
    java.lang.Object mLastWindowFreezeSource;
    final com.android.server.policy.WindowManagerPolicy mPolicy;
    private boolean mRunningExpensiveAnimations;
    final com.android.server.wm.WindowManagerService mService;
    private final android.view.SurfaceControl.Transaction mTransaction;
    int mBulkUpdateParams = 0;
    private com.android.server.wm.IWindowAnimatorExt mWinAnimatorExt = (com.android.server.wm.IWindowAnimatorExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowAnimatorExt.class).base(this).create();
    private boolean mInitialized = false;
    boolean mNotifyWhenNoAnimation = false;
    private final java.util.ArrayList<java.lang.Runnable> mAfterPrepareSurfacesRunnables = new java.util.ArrayList<>();

    WindowAnimator(com.android.server.wm.WindowManagerService service) {
        this.mService = service;
        this.mContext = service.mContext;
        this.mPolicy = service.mPolicy;
        this.mTransaction = service.mTransactionFactory.get();
        service.mAnimationHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.wm.WindowAnimator$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        }, 0L);
        this.mAnimationFrameCallback = new android.view.Choreographer.FrameCallback() { // from class: com.android.server.wm.WindowAnimator$$ExternalSyntheticLambda1
            @Override // android.view.Choreographer.FrameCallback
            public final void doFrame(long j) {
                this.f$0.lambda$new$1(j);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mChoreographer = android.view.Choreographer.getSfInstance();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(long frameTimeNs) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAnimationFrameCallbackScheduled = false;
                animate(frameTimeNs);
                if (this.mNotifyWhenNoAnimation && !this.mLastRootAnimating) {
                    this.mService.mGlobalLock.notifyAll();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void ready() {
        this.mInitialized = true;
    }

    private void animate(long frameTimeNs) {
        if (!this.mInitialized) {
            return;
        }
        scheduleAnimation();
        com.android.server.wm.RootWindowContainer root = this.mService.mRoot;
        boolean useShellTransition = root.mTransitionController.isShellTransitionsEnabled();
        int animationFlags = useShellTransition ? 4 : 5;
        boolean rootAnimating = false;
        this.mCurrentTime = frameTimeNs / 1000000;
        this.mBulkUpdateParams = 0;
        boolean doRequest = true;
        root.mOrientationChangeComplete = true;
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WINDOW_TRACE) {
            android.util.Slog.i(TAG, "!!! animate: entry time=" + this.mCurrentTime);
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, -5360147928134631656L, 0, null, null);
        }
        try {
            root.handleCompleteDeferredRemoval();
            com.android.server.wm.AccessibilityController accessibilityController = this.mService.mAccessibilityController;
            int numDisplays = root.getChildCount();
            for (int i = 0; i < numDisplays; i++) {
                com.android.server.wm.DisplayContent dc = (com.android.server.wm.DisplayContent) root.getChildAt(i);
                dc.updateWindowsForAnimator();
                dc.prepareSurfaces();
            }
            for (int i2 = 0; i2 < numDisplays; i2++) {
                com.android.server.wm.DisplayContent dc2 = (com.android.server.wm.DisplayContent) root.getChildAt(i2);
                if (!useShellTransition) {
                    dc2.checkAppWindowsReadyToShow();
                }
                if (accessibilityController.hasCallbacks()) {
                    accessibilityController.recomputeMagnifiedRegionAndDrawMagnifiedRegionBorderIfNeeded(dc2.mDisplayId);
                }
                if (dc2.isAnimating(animationFlags, -1)) {
                    rootAnimating = true;
                    if (!dc2.mLastContainsRunningSurfaceAnimator) {
                        dc2.mLastContainsRunningSurfaceAnimator = true;
                        dc2.enableHighFrameRate(true);
                    }
                } else if (dc2.mLastContainsRunningSurfaceAnimator) {
                    dc2.mLastContainsRunningSurfaceAnimator = false;
                    dc2.enableHighFrameRate(false);
                }
                this.mTransaction.merge(dc2.getPendingTransaction());
            }
            cancelAnimation();
            if (this.mService.mWatermark != null) {
                this.mService.mWatermark.drawIfNeeded();
            }
            this.mWinAnimatorExt.animate();
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.wtf(TAG, "Unhandled exception in Window Manager", e);
        }
        boolean hasPendingLayoutChanges = root.hasPendingLayoutChanges(this);
        if ((this.mBulkUpdateParams == 0 && !root.mOrientationChangeComplete) || !root.copyAnimToLayoutParams()) {
            doRequest = false;
        }
        if (hasPendingLayoutChanges || doRequest) {
            this.mService.mWindowPlacerLocked.requestTraversal();
        }
        if (rootAnimating && !this.mLastRootAnimating) {
            android.os.Trace.asyncTraceBegin(32L, "animating", 0);
        }
        if (!rootAnimating && this.mLastRootAnimating) {
            this.mService.mWindowPlacerLocked.requestTraversal();
            android.os.Trace.asyncTraceEnd(32L, "animating", 0);
        }
        this.mLastRootAnimating = rootAnimating;
        if (!useShellTransition) {
            updateRunningExpensiveAnimationsLegacy();
        }
        android.os.Trace.traceBegin(32L, "applyTransaction");
        this.mTransaction.apply();
        android.os.Trace.traceEnd(32L);
        this.mService.mWindowTracing.logState("WindowAnimator");
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, -3993586364046165922L, 0, null, null);
        }
        this.mService.mAtmService.mTaskOrganizerController.dispatchPendingEvents();
        executeAfterPrepareSurfacesRunnables();
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_WINDOW_TRACE) {
            android.util.Slog.i(TAG, "!!! animate: exit mBulkUpdateParams=" + java.lang.Integer.toHexString(this.mBulkUpdateParams) + " hasPendingLayoutChanges=" + hasPendingLayoutChanges);
        }
    }

    private void updateRunningExpensiveAnimationsLegacy() {
        boolean runningExpensiveAnimations = this.mService.mRoot.isAnimating(5, 11);
        if (runningExpensiveAnimations && !this.mRunningExpensiveAnimations) {
            this.mService.mSnapshotController.setPause(true);
            this.mTransaction.setEarlyWakeupStart();
        } else if (!runningExpensiveAnimations && this.mRunningExpensiveAnimations) {
            this.mService.mSnapshotController.setPause(false);
            this.mTransaction.setEarlyWakeupEnd();
        }
        this.mRunningExpensiveAnimations = runningExpensiveAnimations;
    }

    private static java.lang.String bulkUpdateParamsToString(int bulkUpdateParams) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder(128);
        if ((bulkUpdateParams & 1) != 0) {
            builder.append(" UPDATE_ROTATION");
        }
        if ((bulkUpdateParams & 2) != 0) {
            builder.append(" SET_WALLPAPER_ACTION_PENDING");
        }
        return builder.toString();
    }

    public void dumpLocked(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        java.lang.String subPrefix = "  " + prefix;
        for (int i = 0; i < this.mService.mRoot.getChildCount(); i++) {
            com.android.server.wm.DisplayContent dc = (com.android.server.wm.DisplayContent) this.mService.mRoot.getChildAt(i);
            pw.print(prefix);
            pw.print(dc);
            pw.println(":");
            dc.dumpWindowAnimators(pw, subPrefix);
            pw.println();
        }
        pw.println();
        if (dumpAll) {
            pw.print(prefix);
            pw.print("mCurrentTime=");
            pw.println(android.util.TimeUtils.formatUptime(this.mCurrentTime));
        }
        if (this.mBulkUpdateParams != 0) {
            pw.print(prefix);
            pw.print("mBulkUpdateParams=0x");
            pw.print(java.lang.Integer.toHexString(this.mBulkUpdateParams));
            pw.println(bulkUpdateParamsToString(this.mBulkUpdateParams));
        }
    }

    void scheduleAnimation() {
        if (!this.mAnimationFrameCallbackScheduled) {
            this.mAnimationFrameCallbackScheduled = true;
            this.mChoreographer.postFrameCallback(this.mAnimationFrameCallback);
        }
    }

    private void cancelAnimation() {
        if (this.mAnimationFrameCallbackScheduled) {
            this.mAnimationFrameCallbackScheduled = false;
            this.mChoreographer.removeFrameCallback(this.mAnimationFrameCallback);
        }
    }

    boolean isAnimationScheduled() {
        return this.mAnimationFrameCallbackScheduled;
    }

    android.view.Choreographer getChoreographer() {
        return this.mChoreographer;
    }

    void addAfterPrepareSurfacesRunnable(java.lang.Runnable r) {
        if (this.mInExecuteAfterPrepareSurfacesRunnables) {
            r.run();
        } else {
            this.mAfterPrepareSurfacesRunnables.add(r);
            scheduleAnimation();
        }
    }

    void executeAfterPrepareSurfacesRunnables() {
        if (this.mInExecuteAfterPrepareSurfacesRunnables) {
            return;
        }
        this.mInExecuteAfterPrepareSurfacesRunnables = true;
        int size = this.mAfterPrepareSurfacesRunnables.size();
        for (int i = 0; i < size; i++) {
            this.mAfterPrepareSurfacesRunnables.get(i).run();
        }
        this.mAfterPrepareSurfacesRunnables.clear();
        this.mInExecuteAfterPrepareSurfacesRunnables = false;
    }
}
