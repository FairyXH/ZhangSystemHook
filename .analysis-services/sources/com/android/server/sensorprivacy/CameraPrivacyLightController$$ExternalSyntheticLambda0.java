package com.android.server.sensorprivacy;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class CameraPrivacyLightController$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ com.android.server.sensorprivacy.CameraPrivacyLightController f$0;

    public /* synthetic */ CameraPrivacyLightController$$ExternalSyntheticLambda0(com.android.server.sensorprivacy.CameraPrivacyLightController cameraPrivacyLightController) {
        this.f$0 = cameraPrivacyLightController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.updateLightSession();
    }
}
