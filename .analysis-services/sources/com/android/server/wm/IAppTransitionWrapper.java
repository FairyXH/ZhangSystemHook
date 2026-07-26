package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IAppTransitionWrapper {
    default com.android.server.wm.IAppTransitionExt getExtImpl() {
        return new com.android.server.wm.IAppTransitionExt() { // from class: com.android.server.wm.IAppTransitionWrapper.1
        };
    }

    default java.lang.String getNextAppTransitionPackage() {
        return null;
    }

    default int getNextAppTransitionType() {
        return 0;
    }
}
