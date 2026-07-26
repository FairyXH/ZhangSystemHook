package com.android.server.wm;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class AppTransition$$ExternalSyntheticLambda1 implements java.util.function.BiPredicate {
    @Override // java.util.function.BiPredicate
    public final boolean test(java.lang.Object obj, java.lang.Object obj2) {
        return ((com.android.server.wm.Task) obj).isTaskId(((java.lang.Integer) obj2).intValue());
    }
}
