package com.android.server.ambientcontext;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class RemoteWearableSensingService$$ExternalSyntheticLambda1 implements java.util.function.Function {
    @Override // java.util.function.Function
    public final java.lang.Object apply(java.lang.Object obj) {
        return android.service.wearable.IWearableSensingService.Stub.asInterface((android.os.IBinder) obj);
    }
}
