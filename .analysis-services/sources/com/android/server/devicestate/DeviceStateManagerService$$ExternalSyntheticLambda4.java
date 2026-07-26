package com.android.server.devicestate;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class DeviceStateManagerService$$ExternalSyntheticLambda4 implements java.util.function.Consumer {
    public final /* synthetic */ com.android.server.devicestate.OverrideRequestController f$0;

    public /* synthetic */ DeviceStateManagerService$$ExternalSyntheticLambda4(com.android.server.devicestate.OverrideRequestController overrideRequestController) {
        this.f$0 = overrideRequestController;
    }

    @Override // java.util.function.Consumer
    public final void accept(java.lang.Object obj) {
        this.f$0.cancelRequest((com.android.server.devicestate.OverrideRequest) obj);
    }
}
