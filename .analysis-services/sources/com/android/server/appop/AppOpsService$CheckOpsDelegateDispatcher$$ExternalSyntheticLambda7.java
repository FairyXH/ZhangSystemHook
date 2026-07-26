package com.android.server.appop;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda7 implements com.android.internal.util.function.UndecFunction {
    public final /* synthetic */ com.android.server.appop.AppOpsService f$0;

    public /* synthetic */ AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda7(com.android.server.appop.AppOpsService appOpsService) {
        this.f$0 = appOpsService;
    }

    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8, java.lang.Object obj9, java.lang.Object obj10, java.lang.Object obj11) {
        return this.f$0.startProxyOperationImpl((android.os.IBinder) obj, ((java.lang.Integer) obj2).intValue(), (android.content.AttributionSource) obj3, ((java.lang.Boolean) obj4).booleanValue(), ((java.lang.Boolean) obj5).booleanValue(), (java.lang.String) obj6, ((java.lang.Boolean) obj7).booleanValue(), ((java.lang.Boolean) obj8).booleanValue(), ((java.lang.Integer) obj9).intValue(), ((java.lang.Integer) obj10).intValue(), ((java.lang.Integer) obj11).intValue());
    }
}
