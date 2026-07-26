package com.android.server.wm;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda0 implements com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback {
    public final /* synthetic */ com.android.server.wm.ScreenRotationAnimation.SurfaceRotationAnimationController f$0;

    public /* synthetic */ ScreenRotationAnimation$SurfaceRotationAnimationController$$ExternalSyntheticLambda0(com.android.server.wm.ScreenRotationAnimation.SurfaceRotationAnimationController surfaceRotationAnimationController) {
        this.f$0 = surfaceRotationAnimationController;
    }

    @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
    public final void onAnimationFinished(int i, com.android.server.wm.AnimationAdapter animationAdapter) {
        this.f$0.onAnimationEnd(i, animationAdapter);
    }
}
