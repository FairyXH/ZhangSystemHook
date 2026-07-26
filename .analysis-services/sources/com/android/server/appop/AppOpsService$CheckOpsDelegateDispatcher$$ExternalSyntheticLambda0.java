package com.android.server.appop;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda0 implements com.android.internal.util.function.OctFunction {
    public final /* synthetic */ com.android.server.appop.AppOpsService f$0;

    public /* synthetic */ AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda0(com.android.server.appop.AppOpsService appOpsService) {
        this.f$0 = appOpsService;
    }

    public final java.lang.Object apply(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8) {
        return this.f$0.noteOperationImpl(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue(), (java.lang.String) obj3, (java.lang.String) obj4, ((java.lang.Integer) obj5).intValue(), ((java.lang.Boolean) obj6).booleanValue(), (java.lang.String) obj7, ((java.lang.Boolean) obj8).booleanValue());
    }
}
