package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IActiveServicesWrapper {
    default com.android.server.am.IActiveServicesExt getExtImpl() {
        return new com.android.server.am.IActiveServicesExt() { // from class: com.android.server.am.IActiveServicesWrapper.1
        };
    }

    default void setDynamicalLogEnable(boolean on) {
    }
}
