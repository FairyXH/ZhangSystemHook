package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WindowContainerThumbnail implements com.android.server.wm.SurfaceAnimator.Animatable {
    private static final java.lang.String TAG = "WindowManager";
    private final int mHeight;
    private final com.android.server.wm.SurfaceAnimator mSurfaceAnimator;
    private android.view.SurfaceControl mSurfaceControl;
    private final int mWidth;
    private final com.android.server.wm.WindowContainer mWindowContainer;

    WindowContainerThumbnail(android.view.SurfaceControl.Transaction t, com.android.server.wm.WindowContainer container, android.hardware.HardwareBuffer thumbnailHeader) {
        this(t, container, thumbnailHeader, null);
    }

    WindowContainerThumbnail(android.view.SurfaceControl.Transaction t, com.android.server.wm.WindowContainer container, android.hardware.HardwareBuffer thumbnailHeader, com.android.server.wm.SurfaceAnimator animator) {
        this.mWindowContainer = container;
        if (animator != null) {
            this.mSurfaceAnimator = animator;
        } else {
            this.mSurfaceAnimator = new com.android.server.wm.SurfaceAnimator(this, new com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.WindowContainerThumbnail$$ExternalSyntheticLambda0
                @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
                public final void onAnimationFinished(int i, com.android.server.wm.AnimationAdapter animationAdapter) {
                    this.f$0.onAnimationFinished(i, animationAdapter);
                }
            }, container.mWmService);
        }
        this.mWidth = thumbnailHeader.getWidth();
        this.mHeight = thumbnailHeader.getHeight();
        this.mSurfaceControl = this.mWindowContainer.makeChildSurface(this.mWindowContainer.getTopChild()).setName("thumbnail anim: " + this.mWindowContainer.toString()).setBLASTLayer().setFormat(-3).setMetadata(2, this.mWindowContainer.getWindowingMode()).setMetadata(1, com.android.server.wm.WindowManagerService.MY_UID).setCallsite("WindowContainerThumbnail").build();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mSurfaceControl);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, -131600102855790053L, 0, null, protoLogParam0);
        }
        android.graphics.GraphicBuffer graphicBuffer = android.graphics.GraphicBuffer.createFromHardwareBuffer(thumbnailHeader);
        t.setBuffer(this.mSurfaceControl, graphicBuffer);
        t.setColorSpace(this.mSurfaceControl, android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB));
        t.show(this.mSurfaceControl);
        t.setLayer(this.mSurfaceControl, Integer.MAX_VALUE);
    }

    void startAnimation(android.view.SurfaceControl.Transaction t, android.view.animation.Animation anim) {
        startAnimation(t, anim, (android.graphics.Point) null);
    }

    void startAnimation(android.view.SurfaceControl.Transaction t, android.view.animation.Animation anim, android.graphics.Point position) {
        anim.restrictDuration(10000L);
        anim.scaleCurrentDuration(this.mWindowContainer.mWmService.getTransitionAnimationScaleLocked());
        this.mSurfaceAnimator.startAnimation(t, new com.android.server.wm.LocalAnimationAdapter(new com.android.server.wm.WindowAnimationSpec(anim, position, this.mWindowContainer.getDisplayContent().mAppTransition.canSkipFirstFrame(), this.mWindowContainer.getDisplayContent().getWindowCornerRadius()), this.mWindowContainer.mWmService.mSurfaceAnimationRunner), false, 8);
    }

    void startAnimation(android.view.SurfaceControl.Transaction t, com.android.server.wm.AnimationAdapter anim, boolean hidden) {
        this.mSurfaceAnimator.startAnimation(t, anim, hidden, 8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAnimationFinished(int type, com.android.server.wm.AnimationAdapter anim) {
    }

    void setShowing(android.view.SurfaceControl.Transaction pendingTransaction, boolean show) {
        if (show) {
            pendingTransaction.show(this.mSurfaceControl);
        } else {
            pendingTransaction.hide(this.mSurfaceControl);
        }
    }

    void destroy() {
        this.mSurfaceAnimator.cancelAnimation();
        getPendingTransaction().remove(this.mSurfaceControl);
        this.mSurfaceControl = null;
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.mWidth);
        proto.write(1120986464258L, this.mHeight);
        if (this.mSurfaceAnimator.isAnimating()) {
            this.mSurfaceAnimator.dumpDebug(proto, 1146756268035L);
        }
        proto.end(token);
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl.Transaction getSyncTransaction() {
        return this.mWindowContainer.getSyncTransaction();
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl.Transaction getPendingTransaction() {
        return this.mWindowContainer.getPendingTransaction();
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public void commitPendingTransaction() {
        this.mWindowContainer.commitPendingTransaction();
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public void onAnimationLeashCreated(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
        t.setLayer(leash, Integer.MAX_VALUE);
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public void onAnimationLeashLost(android.view.SurfaceControl.Transaction t) {
        t.hide(this.mSurfaceControl);
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl.Builder makeAnimationLeash() {
        return this.mWindowContainer.makeChildSurface(this.mWindowContainer.getTopChild());
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl getSurfaceControl() {
        return this.mSurfaceControl;
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl getAnimationLeashParent() {
        return this.mWindowContainer.getAnimationLeashParent();
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl getParentSurfaceControl() {
        return this.mWindowContainer.getParentSurfaceControl();
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public int getSurfaceWidth() {
        return this.mWidth;
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public int getSurfaceHeight() {
        return this.mHeight;
    }
}
