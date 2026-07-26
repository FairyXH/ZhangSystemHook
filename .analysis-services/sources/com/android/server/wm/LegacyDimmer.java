package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class LegacyDimmer extends com.android.server.wm.Dimmer {
    private static final int DEFAULT_DIM_ANIM_DURATION = 200;
    private static final java.lang.String TAG = "WindowManager";
    com.android.server.wm.LegacyDimmer.DimState mDimState;
    private com.android.server.wm.IDimmerExt mDimmerExt;
    private com.android.server.wm.LegacyDimmer.DimmerWrapper mDimmerWrapper;
    private com.android.server.wm.WindowContainer mLastRequestedDimContainer;
    private final com.android.server.wm.LegacyDimmer.SurfaceAnimatorStarter mSurfaceAnimatorStarter;

    interface SurfaceAnimatorStarter {
        void startAnimation(com.android.server.wm.SurfaceAnimator surfaceAnimator, android.view.SurfaceControl.Transaction transaction, com.android.server.wm.AnimationAdapter animationAdapter, boolean z, int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DimAnimatable implements com.android.server.wm.SurfaceAnimator.Animatable {
        private android.view.SurfaceControl mDimLayer;

        private DimAnimatable(android.view.SurfaceControl dimLayer) {
            this.mDimLayer = dimLayer;
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public android.view.SurfaceControl.Transaction getSyncTransaction() {
            return com.android.server.wm.LegacyDimmer.this.mHost.getSyncTransaction();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public android.view.SurfaceControl.Transaction getPendingTransaction() {
            return com.android.server.wm.LegacyDimmer.this.mHost.getPendingTransaction();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public void commitPendingTransaction() {
            com.android.server.wm.LegacyDimmer.this.mHost.commitPendingTransaction();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public void onAnimationLeashCreated(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public void onAnimationLeashLost(android.view.SurfaceControl.Transaction t) {
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public android.view.SurfaceControl.Builder makeAnimationLeash() {
            return com.android.server.wm.LegacyDimmer.this.mHost.makeAnimationLeash();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public android.view.SurfaceControl getAnimationLeashParent() {
            return com.android.server.wm.LegacyDimmer.this.mHost.getSurfaceControl();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public android.view.SurfaceControl getSurfaceControl() {
            return this.mDimLayer;
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public android.view.SurfaceControl getParentSurfaceControl() {
            return com.android.server.wm.LegacyDimmer.this.mHost.getSurfaceControl();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public int getSurfaceWidth() {
            return com.android.server.wm.LegacyDimmer.this.mHost.getSurfaceWidth();
        }

        @Override // com.android.server.wm.SurfaceAnimator.Animatable
        public int getSurfaceHeight() {
            return com.android.server.wm.LegacyDimmer.this.mHost.getSurfaceHeight();
        }

        void removeSurface() {
            if (this.mDimLayer != null && this.mDimLayer.isValid()) {
                getSyncTransaction().remove(this.mDimLayer);
            }
            this.mDimLayer = null;
        }
    }

    class DimState {
        android.view.SurfaceControl mDimLayer;
        boolean mDontReset;
        boolean mIsVisible;
        com.android.server.wm.SurfaceAnimator mSurfaceAnimator;
        final android.graphics.Rect mDimBounds = new android.graphics.Rect();
        boolean mAnimateExit = true;
        boolean mDimming = true;

        DimState(android.view.SurfaceControl dimLayer) {
            this.mDimLayer = dimLayer;
            final com.android.server.wm.LegacyDimmer.DimAnimatable dimAnimatable = new com.android.server.wm.LegacyDimmer.DimAnimatable(dimLayer);
            this.mSurfaceAnimator = new com.android.server.wm.SurfaceAnimator(dimAnimatable, new com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.LegacyDimmer$DimState$$ExternalSyntheticLambda0
                @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
                public final void onAnimationFinished(int i, com.android.server.wm.AnimationAdapter animationAdapter) {
                    this.f$0.lambda$new$0(dimAnimatable, i, animationAdapter);
                }
            }, com.android.server.wm.LegacyDimmer.this.mHost.mWmService);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(com.android.server.wm.LegacyDimmer.DimAnimatable dimAnimatable, int type, com.android.server.wm.AnimationAdapter anim) {
            if (!this.mDimming) {
                dimAnimatable.removeSurface();
            }
        }
    }

    protected LegacyDimmer(com.android.server.wm.WindowContainer host) {
        this(host, new com.android.server.wm.LegacyDimmer.SurfaceAnimatorStarter() { // from class: com.android.server.wm.LegacyDimmer$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.LegacyDimmer.SurfaceAnimatorStarter
            public final void startAnimation(com.android.server.wm.SurfaceAnimator surfaceAnimator, android.view.SurfaceControl.Transaction transaction, com.android.server.wm.AnimationAdapter animationAdapter, boolean z, int i) {
                surfaceAnimator.startAnimation(transaction, animationAdapter, z, i);
            }
        });
    }

    LegacyDimmer(com.android.server.wm.WindowContainer host, com.android.server.wm.LegacyDimmer.SurfaceAnimatorStarter surfaceAnimatorStarter) {
        super(host);
        this.mDimmerExt = (com.android.server.wm.IDimmerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDimmerExt.class).base(this).create();
        this.mDimmerWrapper = new com.android.server.wm.LegacyDimmer.DimmerWrapper();
        this.mSurfaceAnimatorStarter = surfaceAnimatorStarter;
    }

    private com.android.server.wm.LegacyDimmer.DimState obtainDimState(com.android.server.wm.WindowContainer container) {
        if (this.mDimState == null) {
            try {
                android.view.SurfaceControl ctl = makeDimLayer();
                this.mDimState = new com.android.server.wm.LegacyDimmer.DimState(ctl);
            } catch (android.view.Surface.OutOfResourcesException e) {
                android.util.Log.w(TAG, "OutOfResourcesException creating dim surface");
            }
        }
        this.mLastRequestedDimContainer = container;
        return this.mDimState;
    }

    private android.view.SurfaceControl makeDimLayer() {
        return this.mHost.makeChildSurface(null).setParent(this.mHost.getSurfaceControl()).setColorLayer().setName("Dim Layer for - " + this.mHost.getName()).setCallsite("Dimmer.makeDimLayer").build();
    }

    @Override // com.android.server.wm.Dimmer
    android.view.SurfaceControl getDimLayer() {
        if (this.mDimState != null) {
            return this.mDimState.mDimLayer;
        }
        return null;
    }

    @Override // com.android.server.wm.Dimmer
    void resetDimStates() {
        if (this.mDimState != null && !this.mDimState.mDontReset) {
            this.mDimState.mDimming = false;
        }
    }

    @Override // com.android.server.wm.Dimmer
    android.graphics.Rect getDimBounds() {
        if (this.mDimState != null) {
            return this.mDimState.mDimBounds;
        }
        return null;
    }

    @Override // com.android.server.wm.Dimmer
    void dontAnimateExit() {
        if (this.mDimState != null) {
            this.mDimState.mAnimateExit = false;
        }
    }

    @Override // com.android.server.wm.Dimmer
    protected void adjustAppearance(com.android.server.wm.WindowContainer container, float alpha, int blurRadius) {
        com.android.server.wm.LegacyDimmer.DimState d = obtainDimState(container);
        if (d == null) {
            return;
        }
        android.view.SurfaceControl.Transaction t = this.mHost.getPendingTransaction();
        t.setAlpha(d.mDimLayer, alpha);
        t.setBackgroundBlurRadius(d.mDimLayer, blurRadius);
        d.mDimming = true;
    }

    @Override // com.android.server.wm.Dimmer
    protected void adjustRelativeLayer(com.android.server.wm.WindowContainer container, int relativeLayer) {
        com.android.server.wm.LegacyDimmer.DimState d = this.mDimState;
        if (d != null) {
            android.view.SurfaceControl.Transaction t = this.mHost.getPendingTransaction();
            t.setRelativeLayer(d.mDimLayer, container.getSurfaceControl(), relativeLayer);
        }
    }

    @Override // com.android.server.wm.Dimmer
    boolean updateDims(android.view.SurfaceControl.Transaction t) {
        if (this.mDimState == null) {
            return false;
        }
        if (!this.mDimState.mDimming) {
            if (!this.mDimState.mAnimateExit || this.mDimmerExt.skipDimAnimation(this.mHost)) {
                if (this.mDimState.mDimLayer.isValid()) {
                    t.remove(this.mDimState.mDimLayer);
                }
            } else {
                startDimExit(this.mLastRequestedDimContainer, this.mDimState.mSurfaceAnimator, t);
            }
            this.mDimState = null;
            return false;
        }
        android.graphics.Rect bounds = this.mDimState.mDimBounds;
        this.mDimmerExt.updateDims(this.mLastRequestedDimContainer, bounds, this.mDimState.mDimLayer, t);
        t.setPosition(this.mDimState.mDimLayer, bounds.left, bounds.top);
        t.setWindowCrop(this.mDimState.mDimLayer, bounds.width(), bounds.height());
        if (!this.mDimState.mIsVisible) {
            this.mDimState.mIsVisible = true;
            t.show(this.mDimState.mDimLayer);
            com.android.server.wm.WindowState ws = this.mLastRequestedDimContainer.asWindowState();
            if (ws == null || ws.mActivityRecord == null || ws.mActivityRecord.mStartingData == null) {
                startDimEnter(this.mLastRequestedDimContainer, this.mDimState.mSurfaceAnimator, t);
            }
        }
        return true;
    }

    private long getDimDuration(com.android.server.wm.WindowContainer container) {
        com.android.server.wm.AnimationAdapter animationAdapter = container.mSurfaceAnimator.getAnimation();
        float durationScale = container.mWmService.getTransitionAnimationScaleLocked();
        return animationAdapter == null ? (long) (200.0f * durationScale) : animationAdapter.getDurationHint();
    }

    private void startDimEnter(com.android.server.wm.WindowContainer container, com.android.server.wm.SurfaceAnimator animator, android.view.SurfaceControl.Transaction t) {
        startAnim(container, animator, t, 0.0f, 1.0f);
    }

    private void startDimExit(com.android.server.wm.WindowContainer container, com.android.server.wm.SurfaceAnimator animator, android.view.SurfaceControl.Transaction t) {
        startAnim(container, animator, t, 1.0f, 0.0f);
    }

    private void startAnim(com.android.server.wm.WindowContainer container, com.android.server.wm.SurfaceAnimator animator, android.view.SurfaceControl.Transaction t, float startAlpha, float endAlpha) {
        boolean useDuration = this.mDimmerExt.useSpeceficDurationForDim(container, this.mHost, endAlpha);
        this.mSurfaceAnimatorStarter.startAnimation(animator, t, new com.android.server.wm.LocalAnimationAdapter(new com.android.server.wm.LegacyDimmer.AlphaAnimationSpec(startAlpha, endAlpha, useDuration ? 0L : getDimDuration(container)), this.mHost.mWmService.mSurfaceAnimationRunner), false, 4);
    }

    private static class AlphaAnimationSpec implements com.android.server.wm.LocalAnimationAdapter.AnimationSpec {
        private final long mDuration;
        private final float mFromAlpha;
        private final float mToAlpha;

        AlphaAnimationSpec(float fromAlpha, float toAlpha, long duration) {
            this.mFromAlpha = fromAlpha;
            this.mToAlpha = toAlpha;
            this.mDuration = duration;
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public long getDuration() {
            return this.mDuration;
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void apply(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl sc, long currentPlayTime) {
            float fraction = getFraction(currentPlayTime);
            float alpha = ((this.mToAlpha - this.mFromAlpha) * fraction) + this.mFromAlpha;
            t.setAlpha(sc, alpha);
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix);
            pw.print("from=");
            pw.print(this.mFromAlpha);
            pw.print(" to=");
            pw.print(this.mToAlpha);
            pw.print(" duration=");
            pw.println(this.mDuration);
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void dumpDebugInner(android.util.proto.ProtoOutputStream proto) {
            long token = proto.start(1146756268035L);
            proto.write(1108101562369L, this.mFromAlpha);
            proto.write(1108101562370L, this.mToAlpha);
            proto.write(1112396529667L, this.mDuration);
            proto.end(token);
        }
    }

    public com.android.server.wm.IDimmerWrapper getWrapper() {
        return this.mDimmerWrapper;
    }

    private class DimmerWrapper implements com.android.server.wm.IDimmerWrapper {
        private DimmerWrapper() {
        }

        @Override // com.android.server.wm.IDimmerWrapper
        public com.android.server.wm.IDimmerExt getExtImpl() {
            return com.android.server.wm.LegacyDimmer.this.mDimmerExt;
        }

        @Override // com.android.server.wm.IDimmerWrapper
        public com.android.server.wm.WindowContainer getLastRequestedDimContainer() {
            return com.android.server.wm.LegacyDimmer.this.mLastRequestedDimContainer;
        }
    }
}
