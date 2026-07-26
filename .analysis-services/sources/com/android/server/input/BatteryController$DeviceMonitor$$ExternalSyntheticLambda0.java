package com.android.server.input;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class BatteryController$DeviceMonitor$$ExternalSyntheticLambda0 implements java.util.function.Consumer {
    public final /* synthetic */ com.android.server.input.BatteryController.DeviceMonitor f$0;

    public /* synthetic */ BatteryController$DeviceMonitor$$ExternalSyntheticLambda0(com.android.server.input.BatteryController.DeviceMonitor deviceMonitor) {
        this.f$0 = deviceMonitor;
    }

    @Override // java.util.function.Consumer
    public final void accept(java.lang.Object obj) {
        this.f$0.updateBatteryStateFromNative(((java.lang.Long) obj).longValue());
    }
}
