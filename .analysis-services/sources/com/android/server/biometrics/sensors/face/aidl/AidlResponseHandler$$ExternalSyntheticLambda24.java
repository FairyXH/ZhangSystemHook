package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AidlResponseHandler$$ExternalSyntheticLambda24 implements java.lang.Runnable {
    public final /* synthetic */ com.android.server.biometrics.sensors.BiometricScheduler f$0;

    public /* synthetic */ AidlResponseHandler$$ExternalSyntheticLambda24(com.android.server.biometrics.sensors.BiometricScheduler biometricScheduler) {
        this.f$0 = biometricScheduler;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.onUserStopped();
    }
}
