package com.android.server.appop;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda5 implements com.android.internal.util.function.QuadFunction {
    public final /* synthetic */ com.android.server.appop.AppOpsService f$0;

    public /* synthetic */ AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda5(com.android.server.appop.AppOpsService appOpsService) {
        this.f$0 = appOpsService;
    }

    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
        return java.lang.Integer.valueOf(this.f$0.checkAudioOperationImpl(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), (java.lang.String) obj4));
    }
}
