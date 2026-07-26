package com.android.server.people.data;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class UserData$$ExternalSyntheticLambda0 implements java.util.function.Predicate {
    public final /* synthetic */ com.android.server.people.data.UserData f$0;

    public /* synthetic */ UserData$$ExternalSyntheticLambda0(com.android.server.people.data.UserData userData) {
        this.f$0 = userData;
    }

    @Override // java.util.function.Predicate
    public final boolean test(java.lang.Object obj) {
        return this.f$0.isDefaultDialer((java.lang.String) obj);
    }
}
