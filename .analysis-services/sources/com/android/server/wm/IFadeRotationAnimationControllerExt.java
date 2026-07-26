package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IFadeRotationAnimationControllerExt {
    default boolean hasSize(com.android.server.wm.WindowState w) {
        return true;
    }

    default boolean allowFadeRotationAnimation(com.android.server.wm.WindowState w) {
        return true;
    }
}
