package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IAsyncRotationControllerExt {
    default boolean canBeAsync(com.android.server.wm.WindowToken token) {
        return true;
    }

    default boolean canBeHide(com.android.server.wm.WindowToken token) {
        return true;
    }
}
