package com.android.server.timezonedetector;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class ServiceConfigAccessorImpl$$ExternalSyntheticLambda1 implements java.lang.Runnable {
    public final /* synthetic */ com.android.server.timezonedetector.ServiceConfigAccessorImpl f$0;

    public /* synthetic */ ServiceConfigAccessorImpl$$ExternalSyntheticLambda1(com.android.server.timezonedetector.ServiceConfigAccessorImpl serviceConfigAccessorImpl) {
        this.f$0 = serviceConfigAccessorImpl;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.handleConfigurationInternalChangeOnMainThread();
    }
}
