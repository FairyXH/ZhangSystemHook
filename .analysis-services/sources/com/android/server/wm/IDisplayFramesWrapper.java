package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDisplayFramesWrapper extends com.android.server.wm.IDisplayFramesStaticWrapper {
    default com.android.server.wm.IDisplayFramesExt getExtImpl() {
        return new com.android.server.wm.IDisplayFramesExt() { // from class: com.android.server.wm.IDisplayFramesWrapper.1
        };
    }
}
