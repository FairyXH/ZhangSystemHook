package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IAsyncRotationControllerWrapper {
    default java.util.Set<com.android.server.wm.WindowToken> getTargetWindowTokens() {
        return null;
    }

    default java.lang.String getAsyncRotationInfo() {
        return "";
    }

    default void forceRemoveOp(com.android.server.wm.WindowToken windowToken) {
    }

    default android.view.animation.Animation getFadeOutAnimation(com.android.server.wm.WindowToken windowToken) {
        return null;
    }
}
