package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IFadeAnimationControllerExt {
    default void hookFadeWindowToken(com.android.server.wm.WindowToken token, boolean show, android.view.animation.Animation animation) {
    }

    default android.view.animation.Animation getFadeOutAnimation(com.android.server.wm.FadeAnimationController controller, com.android.server.wm.WindowToken token, android.view.animation.Animation animation) {
        return animation;
    }
}
