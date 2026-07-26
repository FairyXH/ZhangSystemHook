package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class SystemGesturesPointerEventListenerSocExtImpl implements com.android.server.wm.ISystemGesturesPointerEventListenerSocExt {
    private boolean mScrollFired;
    com.android.server.wm.SystemGesturesPointerEventListener mSystemGesturesPointerEventListener;

    public SystemGesturesPointerEventListenerSocExtImpl(java.lang.Object service) {
        this.mSystemGesturesPointerEventListener = (com.android.server.wm.SystemGesturesPointerEventListener) service;
    }

    @Override // com.android.server.wm.ISystemGesturesPointerEventListenerSocExt
    public void hookOnFling(com.android.server.wm.SystemGesturesPointerEventListener.Callbacks callbacks, float x, float y, int durationMs) {
        if (java.lang.Math.abs(y) >= java.lang.Math.abs(x)) {
            callbacks.onVerticalFling(durationMs);
        } else {
            callbacks.onHorizontalFling(durationMs);
        }
    }

    @Override // com.android.server.wm.ISystemGesturesPointerEventListenerSocExt
    public void hookSetScrollFired(boolean fired) {
        this.mScrollFired = fired;
    }

    @Override // com.android.server.wm.ISystemGesturesPointerEventListenerSocExt
    public boolean hookGetScrollFired() {
        return this.mScrollFired;
    }
}
