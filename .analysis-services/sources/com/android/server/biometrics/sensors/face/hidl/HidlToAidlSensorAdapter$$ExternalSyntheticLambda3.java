package com.android.server.biometrics.sensors.face.hidl;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class HidlToAidlSensorAdapter$$ExternalSyntheticLambda3 implements java.util.function.Supplier {
    public final /* synthetic */ com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter f$0;

    public /* synthetic */ HidlToAidlSensorAdapter$$ExternalSyntheticLambda3(com.android.server.biometrics.sensors.face.hidl.HidlToAidlSensorAdapter hidlToAidlSensorAdapter) {
        this.f$0 = hidlToAidlSensorAdapter;
    }

    @Override // java.util.function.Supplier
    public final java.lang.Object get() {
        return this.f$0.getIBiometricsFace();
    }
}
