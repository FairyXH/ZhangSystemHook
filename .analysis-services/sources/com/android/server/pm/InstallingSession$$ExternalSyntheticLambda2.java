package com.android.server.pm;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class InstallingSession$$ExternalSyntheticLambda2 implements java.lang.Runnable {
    public final /* synthetic */ com.android.server.pm.InstallingSession f$0;

    public /* synthetic */ InstallingSession$$ExternalSyntheticLambda2(com.android.server.pm.InstallingSession installingSession) {
        this.f$0 = installingSession;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.start();
    }
}
