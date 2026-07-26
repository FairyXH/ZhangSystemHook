package com.android.server.wm.animation;

/* JADX INFO: loaded from: classes3.dex */
public class CurvedTranslateAnimation extends android.view.animation.Animation {
    private final android.animation.PathKeyframes mKeyframes;

    public CurvedTranslateAnimation(android.graphics.Path path) {
        this.mKeyframes = android.animation.KeyframeSet.ofPath(path);
    }

    @Override // android.view.animation.Animation
    protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
        android.graphics.PointF location = (android.graphics.PointF) this.mKeyframes.getValue(interpolatedTime);
        t.getMatrix().setTranslate(location.x, location.y);
    }
}
