package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ISystemGesturesPointerEventListenerSocExt {
    default void hookOnFling(com.android.server.wm.SystemGesturesPointerEventListener.Callbacks callbacks, float x, float y, int durationMs) {
    }

    default void hookSetScrollFired(boolean fired) {
    }

    default boolean hookGetScrollFired() {
        return false;
    }
}
