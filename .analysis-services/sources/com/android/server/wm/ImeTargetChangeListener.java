package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ImeTargetChangeListener {
    default void onImeTargetOverlayVisibilityChanged(android.os.IBinder overlayWindowToken, int windowType, boolean visible, boolean removed) {
    }

    default void onImeInputTargetVisibilityChanged(android.os.IBinder imeInputTarget, boolean visible, boolean removed) {
    }
}
