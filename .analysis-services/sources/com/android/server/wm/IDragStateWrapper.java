package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDragStateWrapper {
    default android.animation.ValueAnimator createCancelAnimationLocked() {
        return null;
    }

    default android.animation.ValueAnimator createReturnAnimationLocked() {
        return null;
    }
}
