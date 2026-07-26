package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class NavBarFadeAnimationController extends com.android.server.wm.FadeAnimationController {
    private static final int FADE_IN_DURATION = 266;
    private static final int FADE_OUT_DURATION = 133;
    private android.view.animation.Animation mFadeInAnimation;
    private android.view.SurfaceControl mFadeInParent;
    private android.view.animation.Animation mFadeOutAnimation;
    private android.view.SurfaceControl mFadeOutParent;
    private final com.android.server.wm.WindowState mNavigationBar;
    private boolean mPlaySequentially;
    private static final android.view.animation.Interpolator FADE_IN_INTERPOLATOR = new android.view.animation.PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f);
    private static final android.view.animation.Interpolator FADE_OUT_INTERPOLATOR = new android.view.animation.PathInterpolator(0.2f, 0.0f, 1.0f, 1.0f);

    public NavBarFadeAnimationController(com.android.server.wm.DisplayContent displayContent) {
        super(displayContent);
        this.mPlaySequentially = false;
        this.mNavigationBar = displayContent.getDisplayPolicy().getNavigationBar();
        this.mFadeInAnimation = new android.view.animation.AlphaAnimation(0.0f, 1.0f);
        this.mFadeInAnimation.setDuration(266L);
        this.mFadeInAnimation.setInterpolator(FADE_IN_INTERPOLATOR);
        this.mFadeOutAnimation = new android.view.animation.AlphaAnimation(1.0f, 0.0f);
        this.mFadeOutAnimation.setDuration(133L);
        this.mFadeOutAnimation.setInterpolator(FADE_OUT_INTERPOLATOR);
    }

    @Override // com.android.server.wm.FadeAnimationController
    public android.view.animation.Animation getFadeInAnimation() {
        return this.mFadeInAnimation;
    }

    @Override // com.android.server.wm.FadeAnimationController
    public android.view.animation.Animation getFadeOutAnimation() {
        return this.mFadeOutAnimation;
    }

    @Override // com.android.server.wm.FadeAnimationController
    protected com.android.server.wm.FadeAnimationController.FadeAnimationAdapter createAdapter(com.android.server.wm.LocalAnimationAdapter.AnimationSpec animationSpec, boolean show, com.android.server.wm.WindowToken windowToken) {
        return new com.android.server.wm.NavBarFadeAnimationController.NavFadeAnimationAdapter(animationSpec, windowToken.getSurfaceAnimationRunner(), show, windowToken, show ? this.mFadeInParent : this.mFadeOutParent);
    }

    public void fadeWindowToken(final boolean show) {
        com.android.server.wm.AsyncRotationController controller = this.mDisplayContent.getAsyncRotationController();
        java.lang.Runnable fadeAnim = new java.lang.Runnable() { // from class: com.android.server.wm.NavBarFadeAnimationController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$fadeWindowToken$0(show);
            }
        };
        if (controller == null) {
            fadeAnim.run();
        } else if (!controller.hasFadeOperation(this.mNavigationBar.mToken)) {
            if (show) {
                controller.setOnShowRunnable(fadeAnim);
            } else {
                fadeAnim.run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fadeWindowToken$0(boolean show) {
        fadeWindowToken(show, this.mNavigationBar.mToken, 64);
    }

    void fadeOutAndInSequentially(long totalDuration, android.view.SurfaceControl fadeOutParent, android.view.SurfaceControl fadeInParent) {
        this.mPlaySequentially = true;
        if (totalDuration > 0) {
            long fadeInDuration = (2 * totalDuration) / 3;
            this.mFadeOutAnimation.setDuration(totalDuration - fadeInDuration);
            this.mFadeInAnimation.setDuration(fadeInDuration);
        }
        this.mFadeOutParent = fadeOutParent;
        this.mFadeInParent = fadeInParent;
        fadeWindowToken(false);
    }

    protected class NavFadeAnimationAdapter extends com.android.server.wm.FadeAnimationController.FadeAnimationAdapter {
        private android.view.SurfaceControl mParent;

        NavFadeAnimationAdapter(com.android.server.wm.LocalAnimationAdapter.AnimationSpec windowAnimationSpec, com.android.server.wm.SurfaceAnimationRunner surfaceAnimationRunner, boolean show, com.android.server.wm.WindowToken token, android.view.SurfaceControl parent) {
            super(windowAnimationSpec, surfaceAnimationRunner, show, token);
            this.mParent = parent;
        }

        @Override // com.android.server.wm.LocalAnimationAdapter, com.android.server.wm.AnimationAdapter
        public void startAnimation(android.view.SurfaceControl animationLeash, android.view.SurfaceControl.Transaction t, int type, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishCallback) {
            super.startAnimation(animationLeash, t, type, finishCallback);
            if (this.mParent != null && this.mParent.isValid()) {
                t.reparent(animationLeash, this.mParent);
                t.setLayer(animationLeash, Integer.MAX_VALUE);
            }
        }

        @Override // com.android.server.wm.FadeAnimationController.FadeAnimationAdapter, com.android.server.wm.AnimationAdapter
        public boolean shouldDeferAnimationFinish(java.lang.Runnable endDeferFinishCallback) {
            if (com.android.server.wm.NavBarFadeAnimationController.this.mPlaySequentially) {
                if (!this.mShow) {
                    com.android.server.wm.NavBarFadeAnimationController.this.fadeWindowToken(true);
                    return false;
                }
                return false;
            }
            return super.shouldDeferAnimationFinish(endDeferFinishCallback);
        }
    }
}
