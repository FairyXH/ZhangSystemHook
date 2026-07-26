package com.android.server.people.data;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class AbstractProtoDiskReadWriter$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ com.android.server.people.data.AbstractProtoDiskReadWriter f$0;

    public /* synthetic */ AbstractProtoDiskReadWriter$$ExternalSyntheticLambda0(com.android.server.people.data.AbstractProtoDiskReadWriter abstractProtoDiskReadWriter) {
        this.f$0 = abstractProtoDiskReadWriter;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.flushScheduledData();
    }
}
