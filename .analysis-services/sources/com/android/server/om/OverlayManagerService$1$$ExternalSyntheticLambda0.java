package com.android.server.om;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class OverlayManagerService$1$$ExternalSyntheticLambda0 implements java.util.function.Consumer {
    public final /* synthetic */ com.android.server.om.OverlayManagerService f$0;

    public /* synthetic */ OverlayManagerService$1$$ExternalSyntheticLambda0(com.android.server.om.OverlayManagerService overlayManagerService) {
        this.f$0 = overlayManagerService;
    }

    @Override // java.util.function.Consumer
    public final void accept(java.lang.Object obj) {
        this.f$0.updateTargetPackagesLocked((android.content.pm.UserPackage) obj);
    }
}
