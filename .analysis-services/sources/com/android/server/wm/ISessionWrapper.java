package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ISessionWrapper {
    default com.android.server.wm.ISessionExt getExtImpl() {
        return new com.android.server.wm.ISessionExt() { // from class: com.android.server.wm.ISessionWrapper.1
        };
    }
}
