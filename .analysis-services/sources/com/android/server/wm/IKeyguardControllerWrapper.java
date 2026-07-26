package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IKeyguardControllerWrapper {
    default com.android.server.wm.IKeyguardControllerExt getExtImpl() {
        return new com.android.server.wm.IKeyguardControllerExt() { // from class: com.android.server.wm.IKeyguardControllerWrapper.1
        };
    }
}
