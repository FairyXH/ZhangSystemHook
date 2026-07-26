package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WindowSurfaceController {
    static final java.lang.String TAG = "WindowManager";
    final com.android.server.wm.WindowStateAnimator mAnimator;
    boolean mChildrenDetached;
    long mLastVisibleTimeStamp;
    private final com.android.server.wm.WindowManagerService mService;
    android.view.SurfaceControl mSurfaceControl;
    private boolean mSurfaceShown = false;
    private final com.android.server.wm.Session mWindowSession;
    private com.android.server.wm.IWindowSurfaceControllerExt mWindowSurfaceControllerExtImpl;
    private final int mWindowType;
    private final java.lang.String title;

    WindowSurfaceController(java.lang.String name, int format, int flags, com.android.server.wm.WindowStateAnimator animator, int windowType) {
        this.mAnimator = animator;
        this.title = name;
        this.mService = animator.mService;
        com.android.server.wm.WindowState win = animator.mWin;
        this.mWindowType = windowType;
        this.mWindowSession = win.mSession;
        this.mWindowSurfaceControllerExtImpl = (com.android.server.wm.IWindowSurfaceControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowSurfaceControllerExt.class).base(this).create();
        android.os.Trace.traceBegin(32L, "new SurfaceControl");
        this.mSurfaceControl = win.makeSurface().setParent(win.getSurfaceControl()).setName(name).setFormat(format).setFlags(flags).setMetadata(2, windowType).setMetadata(1, this.mWindowSession.mUid).setMetadata(6, this.mWindowSession.mPid).setCallsite("WindowSurfaceController").setBLASTLayer().build();
        android.os.Trace.traceEnd(32L);
    }

    void hide(android.view.SurfaceControl.Transaction transaction, java.lang.String reason) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(reason);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.title);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, -2055407587764455051L, 0, null, protoLogParam0, protoLogParam1);
        }
        if (this.mSurfaceShown) {
            hideSurface(transaction);
        }
    }

    private void hideSurface(android.view.SurfaceControl.Transaction transaction) {
        if (this.mSurfaceControl == null) {
            return;
        }
        setShown(false);
        try {
            transaction.hide(this.mSurfaceControl);
            if (this.mAnimator.mIsWallpaper) {
                com.android.server.wm.DisplayContent dc = this.mAnimator.mWin.getDisplayContent();
                android.util.EventLog.writeEvent(com.android.server.wm.EventLogTags.WM_WALLPAPER_SURFACE, java.lang.Integer.valueOf(dc.mDisplayId), 0, java.lang.String.valueOf(dc.mWallpaperController.getWallpaperTarget()));
            }
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.w(TAG, "Exception hiding surface in " + this);
        }
    }

    void destroy(android.view.SurfaceControl.Transaction t) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_SURFACE_ALLOC_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(android.os.Debug.getCallers(8));
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, -5854683348829455340L, 0, null, protoLogParam0, protoLogParam1);
        }
        try {
            try {
                if (this.mSurfaceControl != null) {
                    if (this.mAnimator.mIsWallpaper && !this.mAnimator.mWin.mWindowRemovalAllowed && !this.mAnimator.mWin.mRemoveOnExit) {
                        android.util.Slog.e(TAG, "Unexpected removing wallpaper surface of " + this.mAnimator.mWin + " by " + android.os.Debug.getCallers(8));
                    }
                    t.remove(this.mSurfaceControl);
                }
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.w(TAG, "Error destroying surface in: " + this, e);
            }
        } finally {
            setShown(false);
            this.mSurfaceControl = null;
        }
    }

    boolean prepareToShowInTransaction(android.view.SurfaceControl.Transaction t, float alpha) {
        if (this.mSurfaceControl == null) {
            return false;
        }
        t.setAlpha(this.mSurfaceControl, alpha);
        return true;
    }

    void setOpaque(boolean isOpaque) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.title);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, 7813672046338784579L, 3, null, java.lang.Boolean.valueOf(isOpaque), protoLogParam1);
        }
        if (this.mSurfaceControl == null) {
            return;
        }
        this.mAnimator.mWin.getPendingTransaction().setOpaque(this.mSurfaceControl, isOpaque);
        this.mService.scheduleAnimationLocked();
    }

    void setColorSpaceAgnostic(android.view.SurfaceControl.Transaction t, boolean agnostic) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.title);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, -8864150640874799238L, 3, null, java.lang.Boolean.valueOf(agnostic), protoLogParam1);
        }
        if (this.mSurfaceControl == null) {
            return;
        }
        t.setColorSpaceAgnostic(this.mSurfaceControl, agnostic);
    }

    void showRobustly(android.view.SurfaceControl.Transaction t) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.title);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, -8398940245851553814L, 0, null, protoLogParam0);
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY) {
            android.util.Slog.v(TAG, "Showing " + this + " during relayout");
        }
        if (this.mSurfaceShown) {
            return;
        }
        setShown(true);
        t.show(this.mSurfaceControl);
        if (this.mAnimator.mIsWallpaper) {
            com.android.server.wm.DisplayContent dc = this.mAnimator.mWin.getDisplayContent();
            android.util.EventLog.writeEvent(com.android.server.wm.EventLogTags.WM_WALLPAPER_SURFACE, java.lang.Integer.valueOf(dc.mDisplayId), 1, java.lang.String.valueOf(dc.mWallpaperController.getWallpaperTarget()));
        }
    }

    boolean clearWindowContentFrameStats() {
        if (this.mSurfaceControl == null) {
            return false;
        }
        return this.mSurfaceControl.clearContentFrameStats();
    }

    boolean getWindowContentFrameStats(android.view.WindowContentFrameStats outStats) {
        if (this.mSurfaceControl == null) {
            return false;
        }
        return this.mSurfaceControl.getContentFrameStats(outStats);
    }

    boolean hasSurface() {
        return this.mSurfaceControl != null;
    }

    void getSurfaceControl(android.view.SurfaceControl outSurfaceControl) {
        outSurfaceControl.copyFrom(this.mSurfaceControl, "WindowSurfaceController.getSurfaceControl");
    }

    boolean getShown() {
        return this.mSurfaceShown;
    }

    void setShown(boolean surfaceShown) {
        boolean surfaceShownChanged = this.mSurfaceShown != surfaceShown;
        this.mSurfaceShown = surfaceShown;
        this.mService.updateNonSystemOverlayWindowsVisibilityIfNeeded(this.mAnimator.mWin, surfaceShown);
        this.mAnimator.mWin.onSurfaceShownChanged(surfaceShown);
        if (this.mWindowSession != null) {
            this.mWindowSession.onWindowSurfaceVisibilityChanged(this, this.mSurfaceShown, this.mWindowType);
        }
        if (this.mAnimator.mWin != null && this.mWindowSession != null && this.mAnimator.mWin.mAttrs != null && this.mWindowSession.mUid > 10000) {
            this.mWindowSurfaceControllerExtImpl.updateWindowState(this.mWindowSession.mUid, this.mWindowSession.mPid, this.mAnimator.mWin.hashCode(), this.mAnimator.mWin.mAttrs.type, this.mAnimator.mWin.mHasSurface, surfaceShown);
        }
        this.mWindowSurfaceControllerExtImpl.setShown(surfaceShown, this.mAnimator.mWin, this.mSurfaceControl);
        if (this.mAnimator != null && this.mAnimator.mWin != null) {
            ((com.android.server.wm.IMirageWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IMirageWindowManagerExt.class).create()).applySurfacePrivacyProtectionPolicy(surfaceShownChanged, this.mSurfaceShown, this.mAnimator.mWin);
        }
        if (this.mAnimator != null && this.mAnimator.mWin != null) {
            this.mWindowSurfaceControllerExtImpl.onSecurityPageFlagChanged(this.mAnimator.mWin, surfaceShown, false);
        }
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1133871366145L, this.mSurfaceShown);
        proto.end(token);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        if (dumpAll) {
            pw.print(prefix);
            pw.print("mSurface=");
            pw.println(this.mSurfaceControl);
        }
        pw.print(prefix);
        pw.print("Surface: shown=");
        pw.print(this.mSurfaceShown);
    }

    public java.lang.String toString() {
        return this.mSurfaceControl.toString();
    }
}
