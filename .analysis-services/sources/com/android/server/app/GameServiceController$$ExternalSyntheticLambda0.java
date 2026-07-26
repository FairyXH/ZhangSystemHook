package com.android.server.app;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class GameServiceController$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ com.android.server.app.GameServiceController f$0;

    public /* synthetic */ GameServiceController$$ExternalSyntheticLambda0(com.android.server.app.GameServiceController gameServiceController) {
        this.f$0 = gameServiceController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.evaluateActiveGameServiceProvider();
    }
}
