package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WallpaperWindowToken extends com.android.server.wm.WindowToken {
    private static final java.lang.String TAG = "WindowManager";
    private boolean forceUpdate;
    private android.util.SparseArray<android.graphics.Rect> mCropHints;
    private boolean mShowWhenLocked;
    int mWallpaperDisplayOffsetX;
    int mWallpaperDisplayOffsetY;
    float mWallpaperX;
    float mWallpaperXStep;
    float mWallpaperY;
    float mWallpaperYStep;

    WallpaperWindowToken(com.android.server.wm.WindowManagerService service, android.os.IBinder token, boolean explicit, com.android.server.wm.DisplayContent dc, boolean ownerCanManageAppTokens) {
        this(service, token, explicit, dc, ownerCanManageAppTokens, null);
    }

    WallpaperWindowToken(com.android.server.wm.WindowManagerService service, android.os.IBinder token, boolean explicit, com.android.server.wm.DisplayContent dc, boolean ownerCanManageAppTokens, android.os.Bundle options) {
        super(service, token, 2013, explicit, dc, ownerCanManageAppTokens, false, false, options);
        this.mShowWhenLocked = false;
        this.forceUpdate = false;
        this.mWallpaperX = -1.0f;
        this.mWallpaperY = -1.0f;
        this.mWallpaperXStep = -1.0f;
        this.mWallpaperYStep = -1.0f;
        this.mWallpaperDisplayOffsetX = Integer.MIN_VALUE;
        this.mWallpaperDisplayOffsetY = Integer.MIN_VALUE;
        this.mCropHints = new android.util.SparseArray<>();
        dc.mWallpaperController.addWallpaperToken(this);
        setWindowingMode(1);
    }

    @Override // com.android.server.wm.WindowContainer
    com.android.server.wm.WallpaperWindowToken asWallpaperToken() {
        return this;
    }

    @Override // com.android.server.wm.WindowToken
    void setExiting(boolean animateExit) {
        super.setExiting(animateExit);
        this.mDisplayContent.mWallpaperController.removeWallpaperToken(this);
    }

    @Override // com.android.server.wm.WindowToken, com.android.server.wm.WindowContainer
    public void prepareSurfaces() {
        super.prepareSurfaces();
        if (com.android.window.flags.Flags.ensureWallpaperInTransitions() && !this.mTransitionController.inTransition(this)) {
            getSyncTransaction().setVisibility(this.mSurfaceControl, isVisible());
        }
    }

    void setShowWhenLocked(boolean showWhenLocked) {
        if (showWhenLocked == this.mShowWhenLocked) {
            if (showWhenLocked && !this.mChildren.isEmpty()) {
                this.mDisplayContent.mWallpaperController.mWallpaperControllerExt.forceRestoreWallpaperScale((com.android.server.wm.WindowState) this.mChildren.get(0));
                return;
            }
            return;
        }
        this.mShowWhenLocked = showWhenLocked;
        int position = showWhenLocked ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        getParent().positionChildAt(position, this, false);
        this.mDisplayContent.mWallpaperController.onWallpaperTokenReordered();
        this.forceUpdate = true;
    }

    boolean canShowWhenLocked() {
        return this.mShowWhenLocked;
    }

    void setCropHints(android.util.SparseArray<android.graphics.Rect> cropHints) {
        this.mCropHints = cropHints.clone();
    }

    android.util.SparseArray<android.graphics.Rect> getCropHints() {
        return this.mCropHints;
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
    void sendWindowWallpaperCommand(java.lang.String action, int x, int y, int z, android.os.Bundle extras, boolean sync) {
        for (int wallpaperNdx = this.mChildren.size() - 1; wallpaperNdx >= 0; wallpaperNdx--) {
            com.android.server.wm.WindowState wallpaper = (com.android.server.wm.WindowState) this.mChildren.get(wallpaperNdx);
            try {
                wallpaper.mClient.dispatchWallpaperCommand(action, x, y, z, extras, sync);
                sync = false;
            } catch (android.os.RemoteException e) {
            }
        }
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
    void updateWallpaperOffset(boolean sync) {
        com.android.server.wm.WallpaperController wallpaperController = this.mDisplayContent.mWallpaperController;
        for (int wallpaperNdx = this.mChildren.size() - 1; wallpaperNdx >= 0; wallpaperNdx--) {
            com.android.server.wm.WindowState wallpaper = (com.android.server.wm.WindowState) this.mChildren.get(wallpaperNdx);
            if (wallpaperController.updateWallpaperOffset(wallpaper, sync && !this.mWmService.mFlags.mWallpaperOffsetAsync)) {
                sync = false;
            }
        }
    }

    void updateWallpaperWindows(boolean visible) {
        if (this.mVisibleRequested != visible || isVisibleRequested() != visible) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WALLPAPER_enabled[0]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.token);
                com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WALLPAPER, -7936547457136708587L, 12, null, protoLogParam0, java.lang.Boolean.valueOf(visible));
            }
            setVisibility(visible);
        }
        com.android.server.wm.WindowState wallpaperTarget = this.mDisplayContent.mWallpaperController.getWallpaperTarget();
        if (visible && wallpaperTarget != null) {
            com.android.server.wm.RecentsAnimationController recentsAnimationController = this.mWmService.getRecentsAnimationController();
            if (recentsAnimationController != null && recentsAnimationController.isAnimatingTask(wallpaperTarget.getTask())) {
                recentsAnimationController.linkFixedRotationTransformIfNeeded(this);
            } else if ((wallpaperTarget.mActivityRecord == null || wallpaperTarget.mActivityRecord.isVisibleRequested()) && wallpaperTarget.mToken.hasFixedRotationTransform()) {
                linkFixedRotationTransform(wallpaperTarget.mToken);
            }
        }
        if (this.mTransitionController.inTransition(this)) {
            return;
        }
        setVisible(visible);
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
    private void setVisible(boolean visible) {
        boolean wasClientVisible = isClientVisible();
        setClientVisible(visible);
        if (visible && !wasClientVisible) {
            for (int i = this.mChildren.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowState wallpaper = (com.android.server.wm.WindowState) this.mChildren.get(i);
                wallpaper.requestUpdateWallpaperIfNeeded();
                this.mDisplayContent.mWallpaperController.mWallpaperControllerExt.forceReLayoutWhenVisible();
            }
        }
    }

    void setVisibility(boolean visible) {
        if (this.mVisibleRequested != visible) {
            com.android.server.wm.WindowState wpTarget = this.mDisplayContent.mWallpaperController.getWallpaperTarget();
            boolean isTargetNotCollectedActivity = wpTarget == null || !(wpTarget.mActivityRecord == null || this.mTransitionController.isCollecting(wpTarget.mActivityRecord));
            if (!isTargetNotCollectedActivity || visible) {
                this.mTransitionController.collect(this);
            }
            setVisibleRequested(visible);
        }
        if ((!visible && (this.mTransitionController.inTransition() || getDisplayContent().mAppTransition.isRunning())) || this.mTransitionController.mExt.skipUpdateWallpaperVisibility(visible, getDisplayContent())) {
            return;
        }
        commitVisibility(visible);
    }

    void commitVisibility(boolean visible) {
        if (visible == isVisible()) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
            boolean protoLogParam1 = isVisible();
            boolean protoLogParam2 = this.mVisibleRequested;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 7214407534407465113L, 60, null, protoLogParam0, java.lang.Boolean.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2));
        }
        setVisibleRequested(visible);
        setVisible(visible);
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
    boolean hasVisibleNotDrawnWallpaper() {
        if (!isVisible()) {
            return false;
        }
        for (int j = this.mChildren.size() - 1; j >= 0; j--) {
            com.android.server.wm.WindowState wallpaper = (com.android.server.wm.WindowState) this.mChildren.get(j);
            if (!wallpaper.isDrawn() && wallpaper.isVisible()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    void forAllWallpaperWindows(java.util.function.Consumer<com.android.server.wm.WallpaperWindowToken> callback) {
        callback.accept(this);
    }

    @Override // com.android.server.wm.WindowContainer
    boolean fillsParent() {
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean showWallpaper() {
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    protected boolean setVisibleRequested(boolean visible) {
        if (!super.setVisibleRequested(visible)) {
            return false;
        }
        setInsetsFrozen(!visible);
        return true;
    }

    @Override // com.android.server.wm.WindowContainer
    protected boolean onChildVisibleRequestedChanged(com.android.server.wm.WindowContainer child) {
        return false;
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isVisible() {
        return isClientVisible();
    }

    @Override // com.android.server.wm.WindowContainer
    boolean isSyncFinished(com.android.server.wm.BLASTSyncEngine.SyncGroup group) {
        return (this.mVisibleRequested && hasVisibleNotDrawnWallpaper()) ? false : true;
    }

    @Override // com.android.server.wm.WindowToken
    public java.lang.String toString() {
        if (this.stringName == null || this.forceUpdate) {
            if (this.forceUpdate) {
                this.forceUpdate = false;
            }
            this.stringName = "WallpaperWindowToken{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " token=" + this.token + " showWhenLocked=" + this.mShowWhenLocked + '}';
        }
        return this.stringName;
    }
}
