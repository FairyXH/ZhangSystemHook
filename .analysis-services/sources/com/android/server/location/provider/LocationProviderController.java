package com.android.server.location.provider;

/* JADX INFO: loaded from: classes2.dex */
interface LocationProviderController {
    void flush(java.lang.Runnable runnable);

    boolean isStarted();

    void sendExtraCommand(int i, int i2, java.lang.String str, android.os.Bundle bundle);

    com.android.server.location.provider.AbstractLocationProvider.State setListener(com.android.server.location.provider.AbstractLocationProvider.Listener listener);

    void setRequest(android.location.provider.ProviderRequest providerRequest);

    void start();

    void stop();
}
