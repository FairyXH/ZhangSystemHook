package com.android.server.twilight;

/* JADX INFO: loaded from: classes3.dex */
public interface TwilightManager {
    com.android.server.twilight.TwilightState getLastTwilightState();

    void registerListener(com.android.server.twilight.TwilightListener twilightListener, android.os.Handler handler);

    void unregisterListener(com.android.server.twilight.TwilightListener twilightListener);
}
