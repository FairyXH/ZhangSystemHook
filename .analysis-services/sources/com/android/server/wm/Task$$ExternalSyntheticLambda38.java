package com.android.server.wm;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class Task$$ExternalSyntheticLambda38 implements java.util.function.Consumer {
    public final /* synthetic */ java.util.ArrayList f$0;

    public /* synthetic */ Task$$ExternalSyntheticLambda38(java.util.ArrayList arrayList) {
        this.f$0 = arrayList;
    }

    @Override // java.util.function.Consumer
    public final void accept(java.lang.Object obj) {
        this.f$0.add((com.android.server.wm.ActivityRecord) obj);
    }
}
