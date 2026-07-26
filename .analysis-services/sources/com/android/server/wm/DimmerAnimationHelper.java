package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class DimmerAnimationHelper {
    private static final int DEFAULT_DIM_ANIM_DURATION_MS = 200;
    private static final java.lang.String TAG = "WindowManager";
    private static final java.lang.String THEME_SYSTEMUI_DIALOG = "OplusThemeSystemUiDialog";
    private com.android.server.wm.DimmerAnimationHelper.AnimationSpec mAlphaAnimationSpec;
    private final com.android.server.wm.DimmerAnimationHelper.AnimationAdapterFactory mAnimationAdapterFactory;
    private com.android.server.wm.AnimationAdapter mLocalAnimationAdapter;
    private com.android.server.wm.DimmerAnimationHelper.Change mCurrentProperties = new com.android.server.wm.DimmerAnimationHelper.Change();
    private com.android.server.wm.DimmerAnimationHelper.Change mRequestedProperties = new com.android.server.wm.DimmerAnimationHelper.Change();

    static class Change {
        private static final float EPSILON = 1.0E-4f;
        private float mAlpha;
        private int mBlurRadius;
        private com.android.server.wm.WindowContainer mDimmingContainer;
        private int mRelativeLayer;

        Change() {
            this.mAlpha = -1.0f;
            this.mBlurRadius = -1;
            this.mDimmingContainer = null;
            this.mRelativeLayer = -1;
        }

        Change(com.android.server.wm.DimmerAnimationHelper.Change other) {
            this.mAlpha = -1.0f;
            this.mBlurRadius = -1;
            this.mDimmingContainer = null;
            this.mRelativeLayer = -1;
            this.mAlpha = other.mAlpha;
            this.mBlurRadius = other.mBlurRadius;
            this.mDimmingContainer = other.mDimmingContainer;
            this.mRelativeLayer = other.mRelativeLayer;
        }

        boolean hasSameVisualProperties(com.android.server.wm.DimmerAnimationHelper.Change other) {
            return java.lang.Math.abs(this.mAlpha - other.mAlpha) < EPSILON && this.mBlurRadius == other.mBlurRadius;
        }

        boolean hasSameDimmingContainer(com.android.server.wm.DimmerAnimationHelper.Change other) {
            return this.mDimmingContainer != null && this.mDimmingContainer == other.mDimmingContainer;
        }

        void inheritPropertiesFromAnimation(com.android.server.wm.DimmerAnimationHelper.AnimationSpec anim) {
            this.mAlpha = anim.mCurrentAlpha;
            this.mBlurRadius = anim.mCurrentBlur;
        }

        public java.lang.String toString() {
            return "Dim state: alpha=" + this.mAlpha + ", blur=" + this.mBlurRadius + ", container=" + this.mDimmingContainer + ", relativePosition=" + this.mRelativeLayer;
        }
    }

    DimmerAnimationHelper(com.android.server.wm.DimmerAnimationHelper.AnimationAdapterFactory animationFactory) {
        this.mAnimationAdapterFactory = animationFactory;
    }

    void setExitParameters() {
        setRequestedRelativeParent(this.mRequestedProperties.mDimmingContainer, -1);
        setRequestedAppearance(0.0f, 0);
    }

    void setRequestedRelativeParent(com.android.server.wm.WindowContainer relativeParent, int relativeLayer) {
        this.mRequestedProperties.mDimmingContainer = relativeParent;
        this.mRequestedProperties.mRelativeLayer = relativeLayer;
    }

    void setRequestedAppearance(float alpha, int blurRadius) {
        this.mRequestedProperties.mAlpha = alpha;
        this.mRequestedProperties.mBlurRadius = blurRadius;
    }

    void applyChanges(android.view.SurfaceControl.Transaction t, com.android.server.wm.SmoothDimmer.DimState dim) {
        if (this.mRequestedProperties.mDimmingContainer == null) {
            android.util.Log.e(TAG, this + " does not have a dimming container. Have you forgotten to call adjustRelativeLayer?");
            return;
        }
        if (this.mRequestedProperties.mDimmingContainer.mSurfaceControl == null) {
            android.util.Log.w(TAG, "container " + this.mRequestedProperties.mDimmingContainer + "does not have a surface");
            dim.remove(t);
            return;
        }
        dim.ensureVisible(t);
        relativeReparent(dim.mDimSurface, this.mRequestedProperties.mDimmingContainer.getSurfaceControl(), this.mRequestedProperties.mRelativeLayer, t);
        if (!this.mCurrentProperties.hasSameVisualProperties(this.mRequestedProperties)) {
            stopCurrentAnimation(dim.mDimSurface);
            if (dim.mSkipAnimation || (this.mRequestedProperties.hasSameDimmingContainer(this.mCurrentProperties) && dim.isDimming())) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_DIMMER_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(dim);
                    double protoLogParam1 = this.mRequestedProperties.mAlpha;
                    long protoLogParam2 = this.mRequestedProperties.mBlurRadius;
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DIMMER, 3778139410556664218L, 24, null, protoLogParam0, java.lang.Double.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2));
                }
                setAlphaBlur(dim.mDimSurface, this.mRequestedProperties.mAlpha, this.mRequestedProperties.mBlurRadius, t);
                dim.mSkipAnimation = false;
            } else if (this.mCurrentProperties.mAlpha == -1.0f && this.mCurrentProperties.mBlurRadius == -1 && this.mRequestedProperties.mAlpha == 0.0f && this.mRequestedProperties.mBlurRadius == 0) {
                android.util.Log.d(TAG, " applyChanges skipped animation: " + this.mCurrentProperties + " to " + this.mRequestedProperties);
            } else {
                startAnimation(t, dim);
            }
        } else if (!dim.isDimming()) {
            dim.remove(t);
        }
        this.mCurrentProperties = new com.android.server.wm.DimmerAnimationHelper.Change(this.mRequestedProperties);
    }

    private void startAnimation(final android.view.SurfaceControl.Transaction t, final com.android.server.wm.SmoothDimmer.DimState dim) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_DIMMER_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(dim);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DIMMER, -6357087772993832060L, 0, null, protoLogParam0);
        }
        this.mAlphaAnimationSpec = getRequestedAnimationSpec();
        this.mLocalAnimationAdapter = this.mAnimationAdapterFactory.get(this.mAlphaAnimationSpec, dim.mHostContainer.mWmService.mSurfaceAnimationRunner);
        final float targetAlpha = this.mRequestedProperties.mAlpha;
        final int targetBlur = this.mRequestedProperties.mBlurRadius;
        this.mLocalAnimationAdapter.startAnimation(dim.mDimSurface, t, 4, new com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.DimmerAnimationHelper$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
            public final void onAnimationFinished(int i, com.android.server.wm.AnimationAdapter animationAdapter) {
                this.f$0.lambda$startAnimation$0(dim, targetAlpha, targetBlur, t, i, animationAdapter);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$0(com.android.server.wm.SmoothDimmer.DimState dim, float targetAlpha, int targetBlur, android.view.SurfaceControl.Transaction t, int type, com.android.server.wm.AnimationAdapter animator) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = dim.mHostContainer.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                if (this.mLocalAnimationAdapter != animator) {
                    android.util.Log.i(TAG, dim.mDimSurface + " has start new animation, skip finishCallback.");
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                setAlphaBlur(dim.mDimSurface, targetAlpha, targetBlur, t);
                if (targetAlpha == 0.0f && !dim.isDimming()) {
                    dim.remove(t);
                }
                this.mLocalAnimationAdapter = null;
                this.mAlphaAnimationSpec = null;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private boolean isAnimating() {
        return this.mAlphaAnimationSpec != null;
    }

    void stopCurrentAnimation(android.view.SurfaceControl surface) {
        if (this.mLocalAnimationAdapter != null && isAnimating()) {
            this.mCurrentProperties.inheritPropertiesFromAnimation(this.mAlphaAnimationSpec);
            this.mLocalAnimationAdapter.onAnimationCancelled(surface);
            this.mLocalAnimationAdapter = null;
            this.mAlphaAnimationSpec = null;
        }
    }

    private com.android.server.wm.DimmerAnimationHelper.AnimationSpec getRequestedAnimationSpec() {
        float startAlpha = java.lang.Math.max(this.mCurrentProperties.mAlpha, 0.0f);
        boolean z = false;
        int startBlur = java.lang.Math.max(this.mCurrentProperties.mBlurRadius, 0);
        if (startAlpha == 0.0f && startBlur == 0 && this.mRequestedProperties.mAlpha == 0.0f && this.mRequestedProperties.mBlurRadius > 0) {
            z = true;
        }
        boolean isDrawBlurBehind = z;
        long duration = isDrawBlurBehind ? 0L : getDimDuration(this.mRequestedProperties.mDimmingContainer);
        com.android.server.wm.DimmerAnimationHelper.AnimationSpec spec = new com.android.server.wm.DimmerAnimationHelper.AnimationSpec(new com.android.server.wm.DimmerAnimationHelper.AnimationSpec.AnimationExtremes(java.lang.Float.valueOf(startAlpha), java.lang.Float.valueOf(this.mRequestedProperties.mAlpha)), new com.android.server.wm.DimmerAnimationHelper.AnimationSpec.AnimationExtremes(java.lang.Integer.valueOf(startBlur), java.lang.Integer.valueOf(this.mRequestedProperties.mBlurRadius)), duration);
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_DIMMER_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(spec);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DIMMER, -1187783168730646350L, 0, null, protoLogParam0);
        }
        return spec;
    }

    void relativeReparent(android.view.SurfaceControl dimLayer, android.view.SurfaceControl relativeParent, int relativePosition, android.view.SurfaceControl.Transaction t) {
        try {
            t.setRelativeLayer(dimLayer, relativeParent, relativePosition);
        } catch (java.lang.NullPointerException e) {
            android.util.Log.w(TAG, "Tried to change parent of dim " + dimLayer + " after remove", e);
        }
    }

    void setAlphaBlur(android.view.SurfaceControl sc, float alpha, int blur, android.view.SurfaceControl.Transaction t) {
        try {
            t.setAlpha(sc, alpha);
            t.setBackgroundBlurRadius(sc, blur);
        } catch (java.lang.NullPointerException e) {
            android.util.Log.w(TAG, "Tried to change look of dim " + sc + " after remove", e);
        }
    }

    private long getDimDuration(com.android.server.wm.WindowContainer container) {
        com.android.server.wm.AnimationAdapter animationAdapter = container.mSurfaceAnimator.getAnimation();
        float durationScale = container.mWmService.getTransitionAnimationScaleLocked();
        return animationAdapter == null ? (long) (200.0f * durationScale) : animationAdapter.getDurationHint();
    }

    static class AnimationSpec implements com.android.server.wm.LocalAnimationAdapter.AnimationSpec {
        private static final java.lang.String TAG = "WindowManager";
        private final com.android.server.wm.DimmerAnimationHelper.AnimationSpec.AnimationExtremes<java.lang.Float> mAlpha;
        private final com.android.server.wm.DimmerAnimationHelper.AnimationSpec.AnimationExtremes<java.lang.Integer> mBlur;
        private final long mDuration;
        float mCurrentAlpha = 0.0f;
        int mCurrentBlur = 0;
        boolean mStarted = false;

        static class AnimationExtremes<T> {
            final T mFinishValue;
            final T mStartValue;

            AnimationExtremes(T fromValue, T toValue) {
                this.mStartValue = fromValue;
                this.mFinishValue = toValue;
            }

            public java.lang.String toString() {
                return "[" + this.mStartValue + "->" + this.mFinishValue + "]";
            }
        }

        AnimationSpec(com.android.server.wm.DimmerAnimationHelper.AnimationSpec.AnimationExtremes<java.lang.Float> alpha, com.android.server.wm.DimmerAnimationHelper.AnimationSpec.AnimationExtremes<java.lang.Integer> blur, long duration) {
            this.mAlpha = alpha;
            this.mBlur = blur;
            this.mDuration = duration;
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public long getDuration() {
            return this.mDuration;
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void apply(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl sc, long currentPlayTime) {
            if (!this.mStarted) {
                this.mStarted = true;
                return;
            }
            float fraction = getFraction(currentPlayTime);
            this.mCurrentAlpha = ((this.mAlpha.mFinishValue.floatValue() - this.mAlpha.mStartValue.floatValue()) * fraction) + this.mAlpha.mStartValue.floatValue();
            this.mCurrentBlur = (((int) fraction) * (this.mBlur.mFinishValue.intValue() - this.mBlur.mStartValue.intValue())) + this.mBlur.mStartValue.intValue();
            if (sc.isValid()) {
                t.setAlpha(sc, this.mCurrentAlpha);
                t.setBackgroundBlurRadius(sc, this.mCurrentBlur);
            } else {
                android.util.Log.w(TAG, "Dimmer#AnimationSpec tried to access " + sc + " after release");
            }
        }

        public java.lang.String toString() {
            return "Animation spec: alpha=" + this.mAlpha + ", blur=" + this.mBlur;
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix);
            pw.print("from_alpha=");
            pw.print(this.mAlpha.mStartValue);
            pw.print(" to_alpha=");
            pw.print(this.mAlpha.mFinishValue);
            pw.print(prefix);
            pw.print("from_blur=");
            pw.print(this.mBlur.mStartValue);
            pw.print(" to_blur=");
            pw.print(this.mBlur.mFinishValue);
            pw.print(" duration=");
            pw.println(this.mDuration);
        }

        @Override // com.android.server.wm.LocalAnimationAdapter.AnimationSpec
        public void dumpDebugInner(android.util.proto.ProtoOutputStream proto) {
            long token = proto.start(1146756268035L);
            proto.write(1108101562369L, this.mAlpha.mStartValue.floatValue());
            proto.write(1108101562370L, this.mAlpha.mFinishValue.floatValue());
            proto.write(1112396529667L, this.mDuration);
            proto.end(token);
        }
    }

    static class AnimationAdapterFactory {
        AnimationAdapterFactory() {
        }

        public com.android.server.wm.AnimationAdapter get(com.android.server.wm.LocalAnimationAdapter.AnimationSpec alphaAnimationSpec, com.android.server.wm.SurfaceAnimationRunner runner) {
            return new com.android.server.wm.LocalAnimationAdapter(alphaAnimationSpec, runner);
        }
    }
}
