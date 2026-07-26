package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class FadeAnimationController {
    protected final android.content.Context mContext;
    protected final com.android.server.wm.DisplayContent mDisplayContent;
    private com.android.server.wm.IFadeAnimationControllerExt mExt = (com.android.server.wm.IFadeAnimationControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IFadeAnimationControllerExt.class).base(this).create();

    public FadeAnimationController(com.android.server.wm.DisplayContent displayContent) {
        this.mDisplayContent = displayContent;
        this.mContext = displayContent.mWmService.mContext;
    }

    public android.view.animation.Animation getFadeInAnimation() {
        return android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.fade_in);
    }

    public android.view.animation.Animation getFadeOutAnimation() {
        return android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.fade_out);
    }

    public void fadeWindowToken(boolean show, com.android.server.wm.WindowToken windowToken, int animationType) {
        fadeWindowToken(show, windowToken, animationType, null);
    }

    public void fadeWindowToken(boolean show, com.android.server.wm.WindowToken windowToken, int animationType, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback finishedCallback) {
        if (windowToken == null || windowToken.getParent() == null) {
            return;
        }
        android.view.animation.Animation animation = show ? getFadeInAnimation() : this.mExt.getFadeOutAnimation(this, windowToken, getFadeOutAnimation());
        if (this instanceof com.android.server.wm.AsyncRotationController) {
            this.mExt.hookFadeWindowToken(windowToken, show, animation);
        }
        com.android.server.wm.FadeAnimationController.FadeAnimationAdapter animationAdapter = animation != null ? createAdapter(createAnimationSpec(animation), show, windowToken) : null;
        if (animationAdapter == null) {
            return;
        }
        windowToken.startAnimation(windowToken.getPendingTransaction(), animationAdapter, show, animationType, finishedCallback);
    }

    protected com.android.server.wm.FadeAnimationController.FadeAnimationAdapter createAdapter(com.android.server.wm.LocalAnimationAdapter.AnimationSpec animationSpec, boolean show, com.android.server.wm.WindowToken windowToken) {
        return new com.android.server.wm.FadeAnimationController.FadeAnimationAdapter(animationSpec, windowToken.getSurfaceAnimationRunner(), show, windowToken);
    }

    protected com.android.server.wm.LocalAnimationAdapter.AnimationSpec createAnimationSpec(final android.view.animation.Animation animation) {
        return new com.android.server.wm.LocalAnimationAdapter.AnimationSpec() { // from class: com.android.server.wm.FadeAnimationController.1
            final android.view.animation.Transformation mTransformation = new android.view.animation.Transformation();

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public boolean getShowWallpaper() {
                return true;
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public long getDuration() {
                return animation.getDuration();
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public void apply(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash, long currentPlayTime) {
                this.mTransformation.clear();
                animation.getTransformation(currentPlayTime, this.mTransformation);
                t.setAlpha(leash, this.mTransformation.getAlpha());
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
                pw.print(prefix);
                pw.println(animation);
            }

            @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
            public void dumpDebugInner(android.util.proto.ProtoOutputStream proto) {
                long token = proto.start(1146756268033L);
                proto.write(1138166333441L, animation.toString());
                proto.end(token);
            }
        };
    }

    protected static class FadeAnimationAdapter extends com.android.server.wm.LocalAnimationAdapter {
        protected final boolean mShow;
        protected final com.android.server.wm.WindowToken mToken;

        FadeAnimationAdapter(com.android.server.wm.LocalAnimationAdapter.AnimationSpec windowAnimationSpec, com.android.server.wm.SurfaceAnimationRunner surfaceAnimationRunner, boolean show, com.android.server.wm.WindowToken token) {
            super(windowAnimationSpec, surfaceAnimationRunner);
            this.mShow = show;
            this.mToken = token;
        }

        @Override // com.android.server.wm.AnimationAdapter
        public boolean shouldDeferAnimationFinish(java.lang.Runnable endDeferFinishCallback) {
            return !this.mShow;
        }
    }
}
