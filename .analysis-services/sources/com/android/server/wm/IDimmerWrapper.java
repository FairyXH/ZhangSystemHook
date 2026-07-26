package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDimmerWrapper {
    default com.android.server.wm.IDimmerExt getExtImpl() {
        return new com.android.server.wm.IDimmerExt() { // from class: com.android.server.wm.IDimmerWrapper.1
        };
    }

    default com.android.server.wm.WindowContainer getLastRequestedDimContainer() {
        return null;
    }
}
