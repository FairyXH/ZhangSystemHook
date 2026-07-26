package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class WindowAnimationSpec implements com.android.server.wm.LocalAnimationAdapter.AnimationSpec {
    private android.view.animation.Animation mAnimation;
    private final boolean mCanSkipFirstFrame;
    private final boolean mIsAppAnimation;
    private final android.graphics.Point mPosition;
    private final android.graphics.Rect mRootTaskBounds;
    private int mRootTaskClipMode;
    private final java.lang.ThreadLocal<com.android.server.wm.WindowAnimationSpec.TmpValues> mThreadLocalTmps;
    private final android.graphics.Rect mTmpRect;
    public com.android.server.wm.IWindowAnimationSpecExt mWindowAnimationSpecExt;
    private final float mWindowCornerRadius;

    /* JADX INFO: renamed from: $r8$lambda$O4wc4-tRjiP9nCMbsYU_dS1zsf4, reason: not valid java name */
    public static /* synthetic */ com.android.server.wm.WindowAnimationSpec.TmpValues m11196$r8$lambda$O4wc4tRjiP9nCMbsYU_dS1zsf4() {
        return new com.android.server.wm.WindowAnimationSpec.TmpValues();
    }

    public WindowAnimationSpec(android.view.animation.Animation animation, android.graphics.Point position, boolean canSkipFirstFrame, float windowCornerRadius) {
        this(animation, position, null, canSkipFirstFrame, 1, false, windowCornerRadius);
    }

    public WindowAnimationSpec(android.view.animation.Animation animation, android.graphics.Point position, android.graphics.Rect rootTaskBounds, boolean canSkipFirstFrame, int rootTaskClipMode, boolean isAppAnimation, float windowCornerRadius) {
        this.mPosition = new android.graphics.Point();
        this.mThreadLocalTmps = java.lang.ThreadLocal.withInitial(new java.util.function.Supplier() { // from class: com.android.server.wm.WindowAnimationSpec$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.wm.WindowAnimationSpec.m11196$r8$lambda$O4wc4tRjiP9nCMbsYU_dS1zsf4();
            }
        });
        this.mRootTaskBounds = new android.graphics.Rect();
        this.mTmpRect = new android.graphics.Rect();
        this.mWindowAnimationSpecExt = (com.android.server.wm.IWindowAnimationSpecExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowAnimationSpecExt.class).base(this).create();
        this.mAnimation = animation;
        if (position != null) {
            this.mPosition.set(position.x, position.y);
        }
        this.mWindowCornerRadius = windowCornerRadius;
        this.mCanSkipFirstFrame = canSkipFirstFrame;
        this.mIsAppAnimation = isAppAnimation;
        this.mRootTaskClipMode = rootTaskClipMode;
        if (rootTaskBounds != null) {
            this.mRootTaskBounds.set(rootTaskBounds);
        }
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public com.android.server.wm.WindowAnimationSpec asWindowAnimationSpec() {
        return this;
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public boolean getShowWallpaper() {
        return this.mAnimation.getShowWallpaper();
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public boolean getShowBackground() {
        return this.mAnimation.getShowBackdrop();
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public int getBackgroundColor() {
        return this.mAnimation.getBackdropColor();
    }

    public boolean hasExtension() {
        if (!this.mWindowAnimationSpecExt.useExtendAnimation()) {
            return false;
        }
        return this.mAnimation.hasExtension();
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public long getDuration() {
        return this.mAnimation.computeDurationHint();
    }

    public android.graphics.Rect getRootTaskBounds() {
        return this.mRootTaskBounds;
    }

    public android.view.animation.Animation getAnimation() {
        return this.mAnimation;
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public void apply(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash, long currentPlayTime) {
        com.android.server.wm.WindowAnimationSpec.TmpValues tmp = this.mThreadLocalTmps.get();
        tmp.transformation.clear();
        this.mAnimation.getTransformation(currentPlayTime, tmp.transformation);
        tmp.transformation.getMatrix().postTranslate(this.mPosition.x, this.mPosition.y);
        t.setMatrix(leash, tmp.transformation.getMatrix(), tmp.floats);
        t.setAlpha(leash, tmp.transformation.getAlpha());
        boolean cropSet = false;
        if (this.mRootTaskClipMode == 1) {
            if (tmp.transformation.hasClipRect()) {
                android.graphics.Rect clipRect = tmp.transformation.getClipRect();
                accountForExtension(tmp.transformation, clipRect);
                t.setWindowCrop(leash, clipRect);
                cropSet = true;
            }
        } else {
            this.mTmpRect.set(this.mRootTaskBounds);
            this.mWindowAnimationSpecExt.clipTmpRect(this.mWindowAnimationSpecExt.getmClipSide(), this.mTmpRect, tmp.floats[2], t, leash);
            if (tmp.transformation.hasClipRect()) {
                this.mTmpRect.intersect(tmp.transformation.getClipRect());
            }
            accountForExtension(tmp.transformation, this.mTmpRect);
            this.mWindowAnimationSpecExt.adjustCropRect(this.mAnimation, this.mTmpRect, tmp.transformation, t);
            t.setWindowCrop(leash, this.mTmpRect);
            cropSet = true;
        }
        if (cropSet && this.mAnimation.hasRoundedCorners() && this.mWindowCornerRadius > 0.0f) {
            t.setCornerRadius(leash, this.mWindowCornerRadius);
        }
    }

    private void accountForExtension(android.view.animation.Transformation transformation, android.graphics.Rect clipRect) {
        android.graphics.Insets extensionInsets = android.graphics.Insets.min(transformation.getInsets(), android.graphics.Insets.NONE);
        if (!extensionInsets.equals(android.graphics.Insets.NONE)) {
            clipRect.inset(extensionInsets);
        }
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public long calculateStatusBarTransitionStartTime() {
        android.view.animation.TranslateAnimation openTranslateAnimation = findTranslateAnimation(this.mAnimation);
        if (openTranslateAnimation != null) {
            if (openTranslateAnimation.isXAxisTransition() && openTranslateAnimation.isFullWidthTranslate()) {
                float t = findMiddleOfTranslationFraction(openTranslateAnimation.getInterpolator());
                return ((android.os.SystemClock.uptimeMillis() + openTranslateAnimation.getStartOffset()) + ((long) (openTranslateAnimation.getDuration() * t))) - 60;
            }
            float t2 = findAlmostThereFraction(openTranslateAnimation.getInterpolator());
            return ((android.os.SystemClock.uptimeMillis() + openTranslateAnimation.getStartOffset()) + ((long) (openTranslateAnimation.getDuration() * t2))) - 120;
        }
        return android.os.SystemClock.uptimeMillis();
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public boolean canSkipFirstFrame() {
        return this.mCanSkipFirstFrame;
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public boolean needsEarlyWakeup() {
        return this.mIsAppAnimation;
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.println(this.mAnimation);
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public void dumpDebugInner(android.util.proto.ProtoOutputStream proto) {
        long token = proto.start(1146756268033L);
        proto.write(1138166333441L, this.mAnimation.toString());
        proto.end(token);
    }

    private static android.view.animation.TranslateAnimation findTranslateAnimation(android.view.animation.Animation animation) {
        if (animation instanceof android.view.animation.TranslateAnimation) {
            return (android.view.animation.TranslateAnimation) animation;
        }
        if (animation instanceof android.view.animation.AnimationSet) {
            android.view.animation.AnimationSet set = (android.view.animation.AnimationSet) animation;
            for (int i = 0; i < set.getAnimations().size(); i++) {
                android.view.animation.Animation a = set.getAnimations().get(i);
                if (a instanceof android.view.animation.TranslateAnimation) {
                    return (android.view.animation.TranslateAnimation) a;
                }
            }
            return null;
        }
        return null;
    }

    private static float findAlmostThereFraction(android.view.animation.Interpolator interpolator) {
        return findInterpolationAdjustedTargetFraction(interpolator, 0.99f, 0.01f);
    }

    private float findMiddleOfTranslationFraction(android.view.animation.Interpolator interpolator) {
        return findInterpolationAdjustedTargetFraction(interpolator, 0.5f, 0.01f);
    }

    private static float findInterpolationAdjustedTargetFraction(android.view.animation.Interpolator interpolator, float target, float epsilon) {
        float val = 0.5f;
        for (float adj = 0.25f; adj >= epsilon; adj /= 2.0f) {
            if (interpolator.getInterpolation(val) < target) {
                val += adj;
            } else {
                val -= adj;
            }
        }
        return val;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class TmpValues {
        final float[] floats;
        final android.view.animation.Transformation transformation;

        private TmpValues() {
            this.transformation = new android.view.animation.Transformation();
            this.floats = new float[9];
        }
    }
}
