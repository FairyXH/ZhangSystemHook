package com.android.server.soundtrigger_middleware;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class SoundTriggerHalConcurrentCaptureHandler$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ com.android.server.soundtrigger_middleware.ISoundTriggerHal.GlobalCallback f$0;

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.onResourcesAvailable();
    }
}
