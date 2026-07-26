package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
interface AuthSessionListener {
    void authEndedFor(int i, int i2, int i3, long j, boolean z);

    void authStartedFor(int i, int i2, long j);

    void lockOutTimed(int i, int i2, int i3, long j, long j2);

    void lockedOutFor(int i, int i2, int i3, long j);

    void resetLockoutFor(int i, int i2, long j);
}
