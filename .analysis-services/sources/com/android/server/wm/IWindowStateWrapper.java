package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowStateWrapper {
    default com.android.server.wm.IWindowStateExt getExtImpl() {
        return new com.android.server.wm.IWindowStateExt() { // from class: com.android.server.wm.IWindowStateWrapper.1
        };
    }

    default boolean getAppOpVisibility() {
        return false;
    }
}
