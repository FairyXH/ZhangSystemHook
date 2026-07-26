package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public interface WakeLockWrapper {
    void acquire();

    void acquire(long j);

    boolean isHeld();

    void release();

    void setReferenceCounted(boolean z);
}
