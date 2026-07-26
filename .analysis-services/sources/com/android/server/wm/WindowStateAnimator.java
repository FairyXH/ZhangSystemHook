package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WindowStateAnimator {
    static final int COMMIT_DRAW_PENDING = 2;
    static final int DRAW_PENDING = 1;
    static final int HAS_DRAWN = 4;
    static final int NO_SURFACE = 0;
    static final int PRESERVED_SURFACE_LAYER = 1;
    static final int READY_TO_SHOW = 3;
    static final int ROOT_TASK_CLIP_AFTER_ANIM = 0;
    static final int ROOT_TASK_CLIP_NONE = 1;
    static final java.lang.String TAG = "WindowManager";
    boolean mAnimationIsEntrance;
    final com.android.server.wm.WindowAnimator mAnimator;
    int mAttrType;
    final android.content.Context mContext;
    int mDrawState;
    boolean mEnterAnimationPending;
    boolean mEnteringAnimation;
    final boolean mIsWallpaper;
    boolean mLastHidden;
    final com.android.server.policy.WindowManagerPolicy mPolicy;
    final com.android.server.wm.WindowManagerService mService;
    final com.android.server.wm.Session mSession;
    com.android.server.wm.WindowSurfaceController mSurfaceController;
    private final com.android.server.wm.WallpaperController mWallpaperControllerLocked;
    final com.android.server.wm.WindowState mWin;
    float mShownAlpha = 0.0f;
    float mAlpha = 0.0f;
    float mLastAlpha = 0.0f;
    private final android.graphics.Rect mSystemDecorRect = new android.graphics.Rect();
    public com.android.server.wm.IWindowStateAnimatorExt mStateAnimatorExt = (com.android.server.wm.IWindowStateAnimatorExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowStateAnimatorExt.class).base(this).create();

    java.lang.String drawStateToString() {
        return drawStateToString(this.mDrawState);
    }

    java.lang.String drawStateToString(int state) {
        switch (state) {
            case 0:
                return "NO_SURFACE";
            case 1:
                return "DRAW_PENDING";
            case 2:
                return "COMMIT_DRAW_PENDING";
            case 3:
                return "READY_TO_SHOW";
            case 4:
                return "HAS_DRAWN";
            default:
                return java.lang.Integer.toString(this.mDrawState);
        }
    }

    void printWindowState(int preState, int curState, com.android.server.wm.WindowState win, java.lang.String reason) {
        if (preState != curState) {
            android.util.Slog.i(TAG, win + " state from " + drawStateToString(preState) + " to " + drawStateToString(curState) + "; reason: " + reason);
        }
    }

    WindowStateAnimator(com.android.server.wm.WindowState win) {
        com.android.server.wm.WindowManagerService service = win.mWmService;
        this.mService = service;
        this.mAnimator = service.mAnimator;
        this.mPolicy = service.mPolicy;
        this.mContext = service.mContext;
        this.mWin = win;
        this.mSession = win.mSession;
        this.mAttrType = win.mAttrs.type;
        this.mIsWallpaper = win.mIsWallpaper;
        this.mWallpaperControllerLocked = win.getDisplayContent().mWallpaperController;
    }

    void onAnimationFinished() {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            boolean protoLogParam1 = this.mWin.mAnimatingExit;
            boolean protoLogParam2 = this.mWin.mActivityRecord != null && this.mWin.mActivityRecord.reportedVisible;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -1495677286613044867L, 60, null, protoLogParam0, java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2));
        }
        this.mWin.checkPolicyVisibilityChange();
        com.android.server.wm.DisplayContent displayContent = this.mWin.getDisplayContent();
        if ((this.mAttrType == 2000 || this.mAttrType == 2040) && this.mWin.isVisibleByPolicy()) {
            displayContent.setLayoutNeeded();
        }
        this.mWin.onExitAnimationDone();
        displayContent.pendingLayoutChanges |= 8;
        if (displayContent.mWallpaperController.isWallpaperTarget(this.mWin)) {
            displayContent.pendingLayoutChanges |= 4;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
            this.mService.mWindowPlacerLocked.debugLayoutRepeats("WindowStateAnimator", displayContent.pendingLayoutChanges);
        }
        if (this.mWin.mActivityRecord != null) {
            this.mWin.mActivityRecord.updateReportedVisibilityLocked();
        }
    }

    void hide(android.view.SurfaceControl.Transaction transaction, java.lang.String reason) {
        if (!this.mLastHidden) {
            this.mLastHidden = true;
            if (this.mSurfaceController != null) {
                this.mSurfaceController.hide(transaction, reason);
            }
            this.mWin.getWCWrapper().getExtImpl().enablePendingApplyTransition(this.mWin, transaction);
            this.mWin.getWCWrapper().getExtImpl().recordSyncHideForCollecting(this.mWin, transaction);
        }
    }

    boolean finishDrawingLocked(android.view.SurfaceControl.Transaction postDrawTransaction) {
        boolean startingWindow = this.mWin.mAttrs.type == 3;
        if (startingWindow && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mWin);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(drawStateToString());
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, 3436877176443058520L, 0, null, protoLogParam0, protoLogParam1);
        }
        boolean layoutNeeded = false;
        if (this.mDrawState == 1) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_DRAW_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this.mWin);
                java.lang.String protoLogParam12 = java.lang.String.valueOf(this.mSurfaceController);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DRAW, 345647873457403698L, 0, null, protoLogParam02, protoLogParam12);
            }
            if (startingWindow && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_STARTING_WINDOW_enabled[1]) {
                java.lang.String protoLogParam03 = java.lang.String.valueOf(this.mWin);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_STARTING_WINDOW, -2385558637577093121L, 0, null, protoLogParam03);
            }
            printWindowState(this.mDrawState, 2, this.mWin, "finishDrawingLocked");
            this.mDrawState = 2;
            layoutNeeded = true;
        }
        if (postDrawTransaction != null) {
            if (this.mWin.getWrapper().getExtImpl().needMaintainVisibleSate(this.mWin)) {
                this.mWin.getPendingTransaction().merge(postDrawTransaction);
            } else {
                this.mWin.getSyncTransaction().merge(postDrawTransaction);
            }
            layoutNeeded = true;
        }
        com.android.server.wm.DisplayContent displayContent = this.mWin.getDisplayContent();
        if (displayContent.mWaitingForDrawn.contains(this.mWin) || this.mService.mRoot.mWaitingForDrawn.contains(this.mWin)) {
            return true;
        }
        return layoutNeeded;
    }

    boolean commitFinishDrawingLocked() {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_STARTING_WINDOW_VERBOSE && this.mWin.mAttrs.type == 3) {
            android.util.Slog.i(TAG, "commitFinishDrawingLocked: " + this.mWin + " cur mDrawState=" + drawStateToString());
        }
        if ((this.mDrawState != 2 && this.mDrawState != 3) || this.mWin.getWrapper().getExtImpl().syncEmbeddedWindowDrawStateIfNeeded(this.mWin)) {
            return false;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mSurfaceController);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -3490933626936411542L, 0, null, protoLogParam0);
        }
        printWindowState(this.mDrawState, 3, this.mWin, "commitFinishDrawingLocked");
        this.mDrawState = 3;
        com.android.server.wm.ActivityRecord activity = this.mWin.mActivityRecord;
        if (activity != null && !activity.canShowWindows() && this.mWin.mAttrs.type != 3) {
            return false;
        }
        boolean result = this.mWin.performShowLocked();
        return result;
    }

    void resetDrawState() {
        printWindowState(this.mDrawState, 1, this.mWin, "resetDrawState");
        this.mDrawState = 1;
        if (this.mWin.mActivityRecord != null && !this.mWin.mActivityRecord.isAnimating(1)) {
            this.mWin.mActivityRecord.clearAllDrawn();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x007a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    com.android.server.wm.WindowSurfaceController createSurfaceLocked() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 517
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowStateAnimator.createSurfaceLocked():com.android.server.wm.WindowSurfaceController");
    }

    boolean hasSurface() {
        return this.mSurfaceController != null && this.mSurfaceController.hasSurface();
    }

    void destroySurfaceLocked(android.view.SurfaceControl.Transaction t) {
        if (this.mSurfaceController == null) {
            return;
        }
        this.mWin.mHidden = true;
        try {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_VISIBILITY) {
                com.android.server.wm.WindowManagerService.logWithStack(TAG, "Window " + this + " destroying surface " + this.mSurfaceController + ", session " + this.mSession);
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_SURFACE_ALLOC_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mWin);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(new java.lang.RuntimeException().fillInStackTrace());
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, -4491856282178275074L, 0, null, protoLogParam0, protoLogParam1);
            }
            destroySurface(t);
            if (com.android.window.flags.Flags.ensureWallpaperInTransitions() || (this.mWin.getDisplayContent() != null && this.mWin.getDisplayContent().isKeyguardOccluded())) {
                if (this.mWallpaperControllerLocked.isWallpaperTarget(this.mWin)) {
                    this.mWin.requestUpdateWallpaperIfNeeded();
                }
            } else {
                this.mWallpaperControllerLocked.hideWallpapers(this.mWin);
            }
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.w(TAG, "Exception thrown when destroying Window " + this + " surface " + this.mSurfaceController + " session " + this.mSession + ": " + e.toString());
        }
        this.mWin.setHasSurface(false);
        if (this.mSurfaceController != null) {
            this.mSurfaceController.setShown(false);
        }
        this.mSurfaceController = null;
        printWindowState(this.mDrawState, 0, this.mWin, "destroySurfaceLocked");
        this.mDrawState = 0;
    }

    void computeShownFrameLocked() {
        if ((this.mIsWallpaper && this.mService.mRoot.mWallpaperActionPending) || this.mWin.isDragResizeChanged()) {
            return;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
            android.util.Slog.v(TAG, "computeShownFrameLocked: " + this + " not attached, mAlpha=" + this.mAlpha);
        }
        this.mShownAlpha = this.mAlpha;
    }

    void prepareSurfaceLocked(android.view.SurfaceControl.Transaction t) {
        com.android.server.wm.WindowState w = this.mWin;
        if (!hasSurface()) {
            if (w.getOrientationChanging() && w.isGoneForLayout()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(w);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 8602950884833508970L, 0, null, protoLogParam0);
                }
                w.setOrientationChanging(false);
                return;
            }
            return;
        }
        computeShownFrameLocked();
        if ((w.isOnScreen() || this.mStateAnimatorExt.prepareSurfaceLocked(w)) && !this.mStateAnimatorExt.hideForUnFolded(w)) {
            if (this.mLastAlpha != this.mShownAlpha || this.mLastHidden) {
                this.mLastAlpha = this.mShownAlpha;
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(this.mSurfaceController);
                    double protoLogParam1 = this.mShownAlpha;
                    double protoLogParam2 = w.mHScale;
                    double protoLogParam3 = w.mVScale;
                    java.lang.String protoLogParam4 = java.lang.String.valueOf(w);
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, -5079712802591263622L, 168, null, protoLogParam02, java.lang.Double.valueOf(protoLogParam1), java.lang.Double.valueOf(protoLogParam2), java.lang.Double.valueOf(protoLogParam3), protoLogParam4);
                }
                boolean prepared = this.mSurfaceController.prepareToShowInTransaction(t, this.mShownAlpha);
                if (prepared && this.mDrawState == 4) {
                    boolean tmpLastHidden = this.mLastHidden;
                    if (this.mLastHidden && !this.mStateAnimatorExt.hideForUnFolded(w)) {
                        this.mSurfaceController.showRobustly(t);
                        this.mLastHidden = false;
                        this.mStateAnimatorExt.notifyWindowSurfaceShown(w);
                        com.android.server.wm.DisplayContent displayContent = w.getDisplayContent();
                        if (!displayContent.getLastHasContent()) {
                            displayContent.pendingLayoutChanges |= 8;
                            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_LAYOUT_REPEATS) {
                                this.mService.mWindowPlacerLocked.debugLayoutRepeats("showSurfaceRobustlyLocked " + w, displayContent.pendingLayoutChanges);
                            }
                        }
                    }
                    if (this.mSurfaceController.getShown()) {
                        this.mStateAnimatorExt.notifyWinSurfaceShow(this.mWin, tmpLastHidden);
                    }
                }
            }
        } else {
            hide(t, "prepareSurfaceLocked");
            if (!w.mIsWallpaper || !com.android.window.flags.Flags.ensureWallpaperInTransitions()) {
                this.mWallpaperControllerLocked.hideWallpapers(w);
            }
            if (w.getOrientationChanging() && w.isGoneForLayout()) {
                w.setOrientationChanging(false);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                    java.lang.String protoLogParam03 = java.lang.String.valueOf(w);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 8602950884833508970L, 0, null, protoLogParam03);
                }
            }
        }
        boolean prepared2 = w.getOrientationChanging();
        if (prepared2) {
            if (!w.isDrawn()) {
                if (w.mDisplayContent.shouldSyncRotationChange(w) && this.mStateAnimatorExt.waitDrawingCompleted(w, this.mContext)) {
                    w.mWmService.mRoot.mOrientationChangeComplete = false;
                    this.mAnimator.mLastWindowFreezeSource = w;
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                    java.lang.String protoLogParam04 = java.lang.String.valueOf(w);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -2824875917893878016L, 0, null, protoLogParam04);
                    return;
                }
                return;
            }
            w.setOrientationChanging(false);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                java.lang.String protoLogParam05 = java.lang.String.valueOf(w);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 7457181879495900576L, 0, null, protoLogParam05);
            }
        }
    }

    void setOpaqueLocked(boolean isOpaque) {
        if (this.mSurfaceController == null) {
            return;
        }
        this.mSurfaceController.setOpaque(isOpaque);
    }

    void setColorSpaceAgnosticLocked(boolean agnostic) {
        if (this.mSurfaceController == null) {
            return;
        }
        this.mSurfaceController.setColorSpaceAgnostic(this.mWin.getPendingTransaction(), agnostic);
    }

    void applyEnterAnimationLocked() {
        int transit;
        if (this.mEnterAnimationPending) {
            this.mEnterAnimationPending = false;
            transit = 1;
        } else {
            transit = 3;
        }
        if (this.mAttrType != 1 && !this.mIsWallpaper && (this.mWin.mActivityRecord == null || !this.mWin.mActivityRecord.hasStartingWindow())) {
            applyAnimationLocked(transit, true);
        }
        if (this.mService.mAccessibilityController.hasCallbacks()) {
            this.mService.mAccessibilityController.onWindowTransition(this.mWin, transit);
        }
    }

    boolean applyAnimationLocked(int transit, boolean isEntrance) {
        if (this.mWin.isAnimating() && this.mAnimationIsEntrance == isEntrance) {
            return true;
        }
        if (this.mWin.mAttrs.type == 2011) {
            this.mWin.getDisplayContent().adjustForImeIfNeeded();
            if (isEntrance) {
                this.mWin.setDisplayLayoutNeeded();
                this.mService.mWindowPlacerLocked.requestTraversal();
            }
        }
        if (this.mWin.mControllableInsetProvider != null) {
            return false;
        }
        if (this.mWin.mToken.okToAnimate()) {
            if (!this.mStateAnimatorExt.setStartingWindowExitAnimation(transit, this.mWin)) {
                int anim = this.mWin.getDisplayContent().getDisplayPolicy().selectAnimation(this.mWin, transit);
                int attr = -1;
                android.view.animation.Animation a = null;
                if (anim != 0) {
                    if (anim != -1) {
                        android.os.Trace.traceBegin(32L, "WSA#loadAnimation");
                        a = android.view.animation.AnimationUtils.loadAnimation(this.mContext, anim);
                        android.os.Trace.traceEnd(32L);
                    }
                } else {
                    switch (transit) {
                        case 1:
                            attr = 0;
                            break;
                        case 2:
                            attr = 1;
                            break;
                        case 3:
                            attr = 2;
                            break;
                        case 4:
                            attr = 3;
                            break;
                    }
                    if (attr >= 0) {
                        a = this.mWin.getDisplayContent().mAppTransition.loadAnimationAttr(this.mWin.mAttrs, attr, 0);
                    }
                }
                if (this.mStateAnimatorExt.skipWindowAnimationIfNeed(transit, isEntrance, this.mWin)) {
                    a = null;
                }
                if (com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, com.android.internal.protolog.common.LogLevel.VERBOSE) && com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[1]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                    long protoLogParam1 = anim;
                    long protoLogParam2 = attr;
                    java.lang.String protoLogParam3 = java.lang.String.valueOf(a);
                    long protoLogParam4 = transit;
                    long protoLogParam5 = this.mAttrType;
                    java.lang.String protoLogParam7 = java.lang.String.valueOf(android.os.Debug.getCallers(20));
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -5668794009329913533L, 13588, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2), protoLogParam3, java.lang.Long.valueOf(protoLogParam4), java.lang.Long.valueOf(protoLogParam5), java.lang.Boolean.valueOf(isEntrance), protoLogParam7);
                }
                if (a != null) {
                    android.os.Trace.traceBegin(32L, "WSA#startAnimation");
                    this.mStateAnimatorExt.skipWindowAnimation(isEntrance, this.mWin, a);
                    this.mStateAnimatorExt.adjustMultiSearchAnimation(isEntrance, this.mWin, a);
                    this.mWin.startAnimation(a);
                    android.os.Trace.traceEnd(32L);
                    this.mAnimationIsEntrance = isEntrance;
                }
            } else {
                android.os.Trace.traceEnd(32L);
                return true;
            }
        } else {
            this.mWin.cancelAnimation();
        }
        return this.mWin.isAnimating(0, 16);
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        if (this.mSurfaceController != null) {
            this.mSurfaceController.dumpDebug(proto, 1146756268034L);
        }
        proto.write(1159641169923L, this.mDrawState);
        this.mSystemDecorRect.dumpDebug(proto, 1146756268036L);
        proto.end(token);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        if (this.mAnimationIsEntrance) {
            pw.print(prefix);
            pw.print(" mAnimationIsEntrance=");
            pw.print(this.mAnimationIsEntrance);
        }
        if (this.mSurfaceController != null) {
            this.mSurfaceController.dump(pw, prefix, dumpAll);
        }
        if (dumpAll) {
            pw.print(prefix);
            pw.print("mDrawState=");
            pw.print(drawStateToString());
            pw.print(prefix);
            pw.print(" mLastHidden=");
            pw.println(this.mLastHidden);
            pw.print(prefix);
            pw.print("mEnterAnimationPending=" + this.mEnterAnimationPending);
            pw.print(prefix);
            pw.print("mSystemDecorRect=");
            this.mSystemDecorRect.printShortString(pw);
            pw.println();
        }
        if (this.mShownAlpha != 1.0f || this.mAlpha != 1.0f || this.mLastAlpha != 1.0f) {
            pw.print(prefix);
            pw.print("mShownAlpha=");
            pw.print(this.mShownAlpha);
            pw.print(" mAlpha=");
            pw.print(this.mAlpha);
            pw.print(" mLastAlpha=");
            pw.println(this.mLastAlpha);
        }
        if (this.mWin.mGlobalScale != 1.0f) {
            pw.print(prefix);
            pw.print("mGlobalScale=");
            pw.print(this.mWin.mGlobalScale);
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuffer sb = new java.lang.StringBuffer("WindowStateAnimator{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        sb.append(this.mWin.mAttrs.getTitle());
        sb.append('}');
        return sb.toString();
    }

    boolean getShown() {
        if (this.mSurfaceController != null) {
            return this.mSurfaceController.getShown();
        }
        return false;
    }

    void destroySurface(android.view.SurfaceControl.Transaction t) {
        java.lang.StringBuilder sb;
        try {
            try {
                this.mStateAnimatorExt.destoryCompactDimmer(this.mWin);
                if (this.mSurfaceController != null) {
                    id = this.mSurfaceController.mSurfaceControl != null ? this.mSurfaceController.mSurfaceControl.getLayerId() : 0;
                    this.mSurfaceController.destroy(t);
                }
                this.mWin.setHasSurface(false);
                this.mSurfaceController = null;
                printWindowState(this.mDrawState, 0, this.mWin, "destroySurface");
                this.mDrawState = 0;
                if (!this.mWin.getWrapper().getExtImpl().isResidentWindowSurface() || this.mWin.mActivityRecord == null || this.mWin.mActivityRecord.finishing) {
                    return;
                }
                this.mWin.getWrapper().getExtImpl().setResidentWindowSurface(false);
                try {
                    this.mWin.mClient.dispatchCachedSurfaceRemoved(id);
                } catch (android.os.RemoteException e) {
                    e = e;
                    sb = new java.lang.StringBuilder();
                    android.util.Slog.e(TAG, sb.append("dispatchCachedSurfaceRemoved e:").append(e).toString());
                }
            } catch (java.lang.RuntimeException e2) {
                android.util.Slog.w(TAG, "Exception thrown when destroying surface " + this + " surface " + this.mSurfaceController + " session " + this.mSession + ": " + e2);
                this.mWin.setHasSurface(false);
                this.mSurfaceController = null;
                printWindowState(this.mDrawState, 0, this.mWin, "destroySurface");
                this.mDrawState = 0;
                if (!this.mWin.getWrapper().getExtImpl().isResidentWindowSurface() || this.mWin.mActivityRecord == null || this.mWin.mActivityRecord.finishing) {
                    return;
                }
                this.mWin.getWrapper().getExtImpl().setResidentWindowSurface(false);
                try {
                    this.mWin.mClient.dispatchCachedSurfaceRemoved(id);
                } catch (android.os.RemoteException e3) {
                    e = e3;
                    sb = new java.lang.StringBuilder();
                    android.util.Slog.e(TAG, sb.append("dispatchCachedSurfaceRemoved e:").append(e).toString());
                }
            }
        } catch (java.lang.Throwable th) {
            this.mWin.setHasSurface(false);
            this.mSurfaceController = null;
            printWindowState(this.mDrawState, 0, this.mWin, "destroySurface");
            this.mDrawState = 0;
            if (this.mWin.getWrapper().getExtImpl().isResidentWindowSurface() && this.mWin.mActivityRecord != null && !this.mWin.mActivityRecord.finishing) {
                this.mWin.getWrapper().getExtImpl().setResidentWindowSurface(false);
                try {
                    this.mWin.mClient.dispatchCachedSurfaceRemoved(id);
                } catch (android.os.RemoteException e4) {
                    android.util.Slog.e(TAG, "dispatchCachedSurfaceRemoved e:" + e4);
                }
            }
            throw th;
        }
    }

    android.view.SurfaceControl getSurfaceControl() {
        if (!hasSurface()) {
            return null;
        }
        return this.mSurfaceController.mSurfaceControl;
    }
}
