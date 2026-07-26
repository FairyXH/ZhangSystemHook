package com.android.server.pm;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class LauncherAppsService$LauncherAppsImpl$$ExternalSyntheticLambda2 implements java.util.function.BiConsumer {
    public final /* synthetic */ com.android.server.pm.LauncherAppsService.LauncherAppsImpl f$0;

    public /* synthetic */ LauncherAppsService$LauncherAppsImpl$$ExternalSyntheticLambda2(com.android.server.pm.LauncherAppsService.LauncherAppsImpl launcherAppsImpl) {
        this.f$0 = launcherAppsImpl;
    }

    @Override // java.util.function.BiConsumer
    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
        this.f$0.dumpViewCaptureDataToWmTrace((java.lang.String) obj, (java.io.InputStream) obj2);
    }
}
