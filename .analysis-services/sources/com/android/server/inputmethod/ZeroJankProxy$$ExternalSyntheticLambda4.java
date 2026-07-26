package com.android.server.inputmethod;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class ZeroJankProxy$$ExternalSyntheticLambda4 implements java.util.concurrent.Executor {
    public final /* synthetic */ com.android.server.inputmethod.ZeroJankProxy f$0;

    public /* synthetic */ ZeroJankProxy$$ExternalSyntheticLambda4(com.android.server.inputmethod.ZeroJankProxy zeroJankProxy) {
        this.f$0 = zeroJankProxy;
    }

    @Override // java.util.concurrent.Executor
    public final void execute(java.lang.Runnable runnable) {
        this.f$0.offload(runnable);
    }
}
