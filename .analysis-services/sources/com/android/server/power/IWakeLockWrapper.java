package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public interface IWakeLockWrapper {
    default com.android.server.power.IWakeLockExt getExtImpl() {
        return new com.android.server.power.IWakeLockExt() { // from class: com.android.server.power.IWakeLockWrapper.1
        };
    }

    default java.lang.String getLockLevelString() {
        return "";
    }
}
