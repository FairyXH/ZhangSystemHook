package com.android.server.pm;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class PackageManagerService$$ExternalSyntheticLambda14 implements com.android.server.pm.ApkChecksums.Injector.Producer {
    public final /* synthetic */ com.android.server.pm.PackageManagerServiceInjector f$0;

    public /* synthetic */ PackageManagerService$$ExternalSyntheticLambda14(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector) {
        this.f$0 = packageManagerServiceInjector;
    }

    @Override // com.android.server.pm.ApkChecksums.Injector.Producer
    public final java.lang.Object produce() {
        return this.f$0.getIncrementalManager();
    }
}
