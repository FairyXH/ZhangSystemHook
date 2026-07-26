package com.android.server.wm;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class SurfaceAnimationRunner$$ExternalSyntheticLambda0 implements android.view.Choreographer.FrameCallback {
    public final /* synthetic */ com.android.server.wm.SurfaceAnimationRunner f$0;

    public /* synthetic */ SurfaceAnimationRunner$$ExternalSyntheticLambda0(com.android.server.wm.SurfaceAnimationRunner surfaceAnimationRunner) {
        this.f$0 = surfaceAnimationRunner;
    }

    @Override // android.view.Choreographer.FrameCallback
    public final void doFrame(long j) {
        this.f$0.startAnimations(j);
    }
}
