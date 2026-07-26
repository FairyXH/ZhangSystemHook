package com.android.server.oemlock;

/* JADX INFO: loaded from: classes2.dex */
abstract class OemLock {
    abstract java.lang.String getLockName();

    abstract boolean isOemUnlockAllowedByCarrier();

    abstract boolean isOemUnlockAllowedByDevice();

    abstract void setOemUnlockAllowedByCarrier(boolean z, byte[] bArr);

    abstract void setOemUnlockAllowedByDevice(boolean z);

    OemLock() {
    }
}
