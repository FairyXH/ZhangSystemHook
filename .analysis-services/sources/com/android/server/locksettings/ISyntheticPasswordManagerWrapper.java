package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public interface ISyntheticPasswordManagerWrapper {
    default com.android.server.locksettings.ISyntheticPasswordManagerExt getExtImpl() {
        return null;
    }

    default android.hardware.weaver.IWeaver getWeaver() {
        return null;
    }
}
