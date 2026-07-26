package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface LockoutConsumer {
    void onLockoutPermanent();

    void onLockoutTimed(long j);
}
