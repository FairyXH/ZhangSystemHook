package com.android.server.appop;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda1 implements com.android.internal.util.function.HexFunction {
    public final /* synthetic */ com.android.server.appop.AppOpsService f$0;

    public /* synthetic */ AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda1(com.android.server.appop.AppOpsService appOpsService) {
        this.f$0 = appOpsService;
    }

    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6) {
        return this.f$0.noteProxyOperationImpl(((java.lang.Integer) obj).intValue(), (android.content.AttributionSource) obj2, ((java.lang.Boolean) obj3).booleanValue(), (java.lang.String) obj4, ((java.lang.Boolean) obj5).booleanValue(), ((java.lang.Boolean) obj6).booleanValue());
    }
}
