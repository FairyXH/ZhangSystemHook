package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WindowSurfacePlacer {
    static final int SET_UPDATE_ROTATION = 1;
    static final int SET_WALLPAPER_ACTION_PENDING = 2;
    private static final java.lang.String TAG = "WindowManager";
    private int mDeferredRequests;
    private int mLayoutRepeatCount;
    private final com.android.server.wm.WindowManagerService mService;
    private boolean mTraversalScheduled;
    private boolean mInLayout = false;
    private int mDeferDepth = 0;
    private final com.android.server.wm.WindowSurfacePlacer.Traverser mPerformSurfacePlacement = new com.android.server.wm.WindowSurfacePlacer.Traverser();

    private class Traverser implements java.lang.Runnable {
        private Traverser() {
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.WindowSurfacePlacer.this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowSurfacePlacer.this.performSurfacePlacement();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }
    }

    WindowSurfacePlacer(com.android.server.wm.WindowManagerService service) {
        this.mService = service;
    }

    void deferLayout() {
        this.mDeferDepth++;
    }

    void continueLayout(boolean hasChanges) {
        this.mDeferDepth--;
        if (this.mDeferDepth > 0) {
            return;
        }
        if (hasChanges || this.mDeferredRequests > 0) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
                android.util.Slog.i(TAG, "continueLayout hasChanges=" + hasChanges + " deferredRequests=" + this.mDeferredRequests + " " + android.os.Debug.getCallers(2, 3));
            }
            performSurfacePlacement();
            this.mDeferredRequests = 0;
            return;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
            android.util.Slog.i(TAG, "Cancel continueLayout " + android.os.Debug.getCallers(2, 3));
        }
    }

    boolean isLayoutDeferred() {
        return this.mDeferDepth > 0;
    }

    void performSurfacePlacementIfScheduled() {
        if (this.mTraversalScheduled) {
            performSurfacePlacement();
        }
    }

    final void performSurfacePlacement() {
        performSurfacePlacement(false);
    }

    final void performSurfacePlacement(boolean force) {
        if (this.mDeferDepth > 0 && !force) {
            this.mDeferredRequests++;
            return;
        }
        int loopCount = 6;
        do {
            this.mTraversalScheduled = false;
            performSurfacePlacementLoop();
            this.mService.mAnimationHandler.removeCallbacks(this.mPerformSurfacePlacement);
            loopCount--;
            if (!this.mTraversalScheduled) {
                break;
            }
        } while (loopCount > 0);
        this.mService.mRoot.mWallpaperActionPending = false;
    }

    private void performSurfacePlacementLoop() {
        if (this.mInLayout) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
                throw new java.lang.RuntimeException("Recursive call!");
            }
            android.util.Slog.w(TAG, "performLayoutAndPlaceSurfacesLocked called while in layout. Callers=" + android.os.Debug.getCallers(3));
            return;
        }
        com.android.server.wm.DisplayContent defaultDisplay = this.mService.getDefaultDisplayContentLocked();
        if (defaultDisplay == null || defaultDisplay.mWaitingForConfig || !this.mService.mDisplayReady) {
            return;
        }
        this.mInLayout = true;
        if (!this.mService.mForceRemoves.isEmpty()) {
            while (!this.mService.mForceRemoves.isEmpty()) {
                com.android.server.wm.WindowState ws = this.mService.mForceRemoves.remove(0);
                android.util.Slog.i(TAG, "Force removing: " + ws);
                ws.removeImmediately();
            }
            android.util.Slog.w(TAG, "Due to memory failure, waiting a bit for next layout");
            java.lang.Object tmp = new java.lang.Object();
            synchronized (tmp) {
                try {
                    tmp.wait(250L);
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
        try {
            this.mService.mRoot.performSurfacePlacement();
            this.mInLayout = false;
            if (this.mService.mRoot.isLayoutNeeded()) {
                int i = this.mLayoutRepeatCount + 1;
                this.mLayoutRepeatCount = i;
                if (i < 6) {
                    requestTraversal();
                } else {
                    android.util.Slog.e(TAG, "Performed 6 layouts in a row. Skipping");
                    this.mLayoutRepeatCount = 0;
                }
            } else {
                this.mLayoutRepeatCount = 0;
            }
            if (this.mService.mWindowsChanged && !this.mService.mWindowChangeListeners.isEmpty()) {
                this.mService.mH.removeMessages(19);
                this.mService.mH.sendEmptyMessage(19);
            }
        } catch (java.lang.RuntimeException e2) {
            this.mInLayout = false;
            android.util.Slog.wtf(TAG, "Unhandled exception while laying out windows", e2);
        }
    }

    void debugLayoutRepeats(java.lang.String msg, int pendingLayoutChanges) {
        if (this.mLayoutRepeatCount >= 4) {
            android.util.Slog.v(TAG, "Layouts looping: " + msg + ", mPendingLayoutChanges = 0x" + java.lang.Integer.toHexString(pendingLayoutChanges));
        }
    }

    boolean isInLayout() {
        return this.mInLayout;
    }

    boolean isTraversalScheduled() {
        return this.mTraversalScheduled;
    }

    void requestTraversal() {
        if (this.mTraversalScheduled) {
            return;
        }
        this.mTraversalScheduled = true;
        if (this.mDeferDepth > 0) {
            this.mDeferredRequests++;
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
                android.util.Slog.i(TAG, "Defer requestTraversal " + android.os.Debug.getCallers(3));
                return;
            }
            return;
        }
        if (android.os.Trace.isTagEnabled(32L)) {
            android.os.Trace.traceBegin(32L, "requestTraversal:" + android.os.Debug.getCallers(5));
        }
        this.mService.mAnimationHandler.post(this.mPerformSurfacePlacement);
        if (android.os.Trace.isTagEnabled(32L)) {
            android.os.Trace.traceEnd(32L);
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "mTraversalScheduled=" + this.mTraversalScheduled);
        pw.println(prefix + "mDeferDepth=" + this.mDeferDepth);
        pw.println(prefix + "mInLayout=" + this.mInLayout);
        pw.println(prefix + "mDisplayReady=" + this.mService.mDisplayReady);
        pw.println(prefix + "mDeferredRequests=" + this.mDeferredRequests);
    }
}
