package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class WindowChangeAnimationSpec implements com.android.server.wm.LocalAnimationAdapter.AnimationSpec {
    static final int ANIMATION_DURATION = 336;
    private android.view.animation.Animation mAnimation;
    private final android.graphics.Rect mEndBounds;
    private final boolean mIsAppAnimation;
    private final boolean mIsThumbnail;
    private final android.graphics.Rect mStartBounds;
    private final java.lang.ThreadLocal<com.android.server.wm.WindowChangeAnimationSpec.TmpValues> mThreadLocalTmps = java.lang.ThreadLocal.withInitial(new java.util.function.Supplier() { // from class: com.android.server.wm.WindowChangeAnimationSpec$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final java.lang.Object get() {
            return com.android.server.wm.WindowChangeAnimationSpec.$r8$lambda$gy8ZErChYYFfmhoyiQOPrPdJIOA();
        }
    });
    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

    public static /* synthetic */ com.android.server.wm.WindowChangeAnimationSpec.TmpValues $r8$lambda$gy8ZErChYYFfmhoyiQOPrPdJIOA() {
        return new com.android.server.wm.WindowChangeAnimationSpec.TmpValues();
    }

    public WindowChangeAnimationSpec(android.graphics.Rect startBounds, android.graphics.Rect endBounds, android.view.DisplayInfo displayInfo, float durationScale, boolean isAppAnimation, boolean isThumbnail) {
        this.mStartBounds = new android.graphics.Rect(startBounds);
        this.mEndBounds = new android.graphics.Rect(endBounds);
        this.mIsAppAnimation = isAppAnimation;
        this.mIsThumbnail = isThumbnail;
        createBoundsInterpolator((int) (336.0f * durationScale), displayInfo);
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public boolean getShowWallpaper() {
        return false;
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public long getDuration() {
        return this.mAnimation.getDuration();
    }

    private void createBoundsInterpolator(long duration, android.view.DisplayInfo displayInfo) {
        boolean growing = ((this.mEndBounds.width() - this.mStartBounds.width()) + this.mEndBounds.height()) - this.mStartBounds.height() >= 0;
        long scalePeriod = (long) (duration * 0.7f);
        float startScaleX = ((this.mStartBounds.width() * 0.7f) / this.mEndBounds.width()) + (1.0f - 0.7f);
        float startScaleY = ((this.mStartBounds.height() * 0.7f) / this.mEndBounds.height()) + (1.0f - 0.7f);
        if (this.mIsThumbnail) {
            android.view.animation.AnimationSet animSet = new android.view.animation.AnimationSet(true);
            android.view.animation.Animation anim = new android.view.animation.AlphaAnimation(1.0f, 0.0f);
            anim.setDuration(scalePeriod);
            if (!growing) {
                anim.setStartOffset(duration - scalePeriod);
            }
            animSet.addAnimation(anim);
            float endScaleX = 1.0f / startScaleX;
            float endScaleY = 1.0f / startScaleY;
            android.view.animation.Animation anim2 = new android.view.animation.ScaleAnimation(endScaleX, endScaleX, endScaleY, endScaleY);
            anim2.setDuration(duration);
            animSet.addAnimation(anim2);
            this.mAnimation = animSet;
            this.mAnimation.initialize(this.mStartBounds.width(), this.mStartBounds.height(), this.mEndBounds.width(), this.mEndBounds.height());
            return;
        }
        android.view.animation.AnimationSet animSet2 = new android.view.animation.AnimationSet(true);
        android.view.animation.Animation scaleAnim = new android.view.animation.ScaleAnimation(startScaleX, 1.0f, startScaleY, 1.0f);
        scaleAnim.setDuration(scalePeriod);
        if (!growing) {
            scaleAnim.setStartOffset(duration - scalePeriod);
        }
        animSet2.addAnimation(scaleAnim);
        android.view.animation.Animation translateAnim = new android.view.animation.TranslateAnimation(this.mStartBounds.left, this.mEndBounds.left, this.mStartBounds.top, this.mEndBounds.top);
        translateAnim.setDuration(duration);
        animSet2.addAnimation(translateAnim);
        android.graphics.Rect startClip = new android.graphics.Rect(this.mStartBounds);
        android.graphics.Rect endClip = new android.graphics.Rect(this.mEndBounds);
        startClip.offsetTo(0, 0);
        endClip.offsetTo(0, 0);
        android.view.animation.ClipRectAnimation clipRectAnimation = new android.view.animation.ClipRectAnimation(startClip, endClip);
        clipRectAnimation.setDuration(duration);
        animSet2.addAnimation(clipRectAnimation);
        this.mAnimation = animSet2;
        this.mAnimation.initialize(this.mStartBounds.width(), this.mStartBounds.height(), displayInfo.appWidth, displayInfo.appHeight);
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public void apply(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash, long currentPlayTime) {
        com.android.server.wm.WindowChangeAnimationSpec.TmpValues tmp = this.mThreadLocalTmps.get();
        if (this.mIsThumbnail) {
            this.mAnimation.getTransformation(currentPlayTime, tmp.mTransformation);
            t.setMatrix(leash, tmp.mTransformation.getMatrix(), tmp.mFloats);
            t.setAlpha(leash, tmp.mTransformation.getAlpha());
            return;
        }
        this.mAnimation.getTransformation(currentPlayTime, tmp.mTransformation);
        android.graphics.Matrix matrix = tmp.mTransformation.getMatrix();
        t.setMatrix(leash, matrix, tmp.mFloats);
        float[] fArr = tmp.mVecs;
        tmp.mVecs[2] = 0.0f;
        fArr[1] = 0.0f;
        float[] fArr2 = tmp.mVecs;
        tmp.mVecs[3] = 1.0f;
        fArr2[0] = 1.0f;
        matrix.mapVectors(tmp.mVecs);
        tmp.mVecs[0] = 1.0f / tmp.mVecs[0];
        tmp.mVecs[3] = 1.0f / tmp.mVecs[3];
        android.graphics.Rect clipRect = tmp.mTransformation.getClipRect();
        this.mTmpRect.left = (int) ((clipRect.left * tmp.mVecs[0]) + 0.5f);
        this.mTmpRect.right = (int) ((clipRect.right * tmp.mVecs[0]) + 0.5f);
        this.mTmpRect.top = (int) ((clipRect.top * tmp.mVecs[3]) + 0.5f);
        this.mTmpRect.bottom = (int) ((clipRect.bottom * tmp.mVecs[3]) + 0.5f);
        t.setWindowCrop(leash, this.mTmpRect);
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public long calculateStatusBarTransitionStartTime() {
        long uptime = android.os.SystemClock.uptimeMillis();
        return java.lang.Math.max(uptime, (((long) (this.mAnimation.getDuration() * 0.99f)) + uptime) - 120);
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public boolean canSkipFirstFrame() {
        return false;
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public boolean needsEarlyWakeup() {
        return this.mIsAppAnimation;
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.println(this.mAnimation.getDuration());
    }

    @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
    public void dumpDebugInner(android.util.proto.ProtoOutputStream proto) {
        long token = proto.start(1146756268033L);
        proto.write(1138166333441L, this.mAnimation.toString());
        proto.end(token);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class TmpValues {
        final float[] mFloats;
        final android.view.animation.Transformation mTransformation;
        final float[] mVecs;

        private TmpValues() {
            this.mTransformation = new android.view.animation.Transformation();
            this.mFloats = new float[9];
            this.mVecs = new float[4];
        }
    }
}
