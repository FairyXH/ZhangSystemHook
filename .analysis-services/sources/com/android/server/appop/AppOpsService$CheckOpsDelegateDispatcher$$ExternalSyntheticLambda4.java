package com.android.server.appop;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda4 implements com.android.internal.util.function.HexConsumer {
    public final /* synthetic */ com.android.server.appop.AppOpsService f$0;

    public /* synthetic */ AppOpsService$CheckOpsDelegateDispatcher$$ExternalSyntheticLambda4(com.android.server.appop.AppOpsService appOpsService) {
        this.f$0 = appOpsService;
    }

    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6) throws java.lang.Throwable {
        this.f$0.finishOperationImpl((android.os.IBinder) obj, ((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), (java.lang.String) obj4, (java.lang.String) obj5, ((java.lang.Integer) obj6).intValue());
    }
}
