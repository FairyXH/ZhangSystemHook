package com.android.server.media;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes2.dex */
public final /* synthetic */ class MediaRoute2ProviderWatcher$$ExternalSyntheticLambda0 implements java.lang.Runnable {
    public final /* synthetic */ com.android.server.media.MediaRoute2ProviderWatcher f$0;

    public /* synthetic */ MediaRoute2ProviderWatcher$$ExternalSyntheticLambda0(com.android.server.media.MediaRoute2ProviderWatcher mediaRoute2ProviderWatcher) {
        this.f$0 = mediaRoute2ProviderWatcher;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.scanPackages();
    }
}
