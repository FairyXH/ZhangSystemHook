package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class SmoothDimmer extends com.android.server.wm.Dimmer {
    private static final java.lang.String TAG = "WindowManager";
    final com.android.server.wm.DimmerAnimationHelper.AnimationAdapterFactory mAnimationAdapterFactory;
    com.android.server.wm.SmoothDimmer.DimState mDimState;
    private com.android.server.wm.IDimmerExt mDimmerExt;
    private com.android.server.wm.SmoothDimmer.DimmerWrapper mDimmerWrapper;

    class DimState {
        private final com.android.server.wm.DimmerAnimationHelper mAnimationHelper;
        android.view.SurfaceControl mDimSurface;
        final com.android.server.wm.WindowContainer mHostContainer;
        private com.android.server.wm.WindowContainer mLastRequestedDimContainer;
        boolean mSkipAnimation = false;
        boolean mAnimateExit = true;
        private boolean mIsVisible = false;
        final android.graphics.Rect mDimBounds = new android.graphics.Rect();

        DimState() {
            this.mHostContainer = com.android.server.wm.SmoothDimmer.this.mHost;
            this.mAnimationHelper = new com.android.server.wm.DimmerAnimationHelper(com.android.server.wm.SmoothDimmer.this.mAnimationAdapterFactory);
            try {
                this.mDimSurface = makeDimLayer();
            } catch (android.view.Surface.OutOfResourcesException e) {
                android.util.Log.w(com.android.server.wm.SmoothDimmer.TAG, "OutOfResourcesException creating dim surface");
            }
        }

        void ensureVisible(android.view.SurfaceControl.Transaction t) {
            if (!this.mIsVisible) {
                t.show(this.mDimSurface);
                t.setAlpha(this.mDimSurface, 0.0f);
                this.mIsVisible = true;
            }
        }

        void adjustSurfaceLayout(android.view.SurfaceControl.Transaction t) {
            t.setPosition(this.mDimSurface, this.mDimBounds.left, this.mDimBounds.top);
            t.setWindowCrop(this.mDimSurface, this.mDimBounds.width(), this.mDimBounds.height());
        }

        void prepareLookChange(float alpha, int blurRadius) {
            this.mAnimationHelper.setRequestedAppearance(alpha, blurRadius);
        }

        void exit(android.view.SurfaceControl.Transaction t) {
            if (!this.mAnimateExit || com.android.server.wm.SmoothDimmer.this.mDimmerExt.skipDimAnimation(com.android.server.wm.SmoothDimmer.this.mHost)) {
                remove(t);
            } else {
                this.mAnimationHelper.setExitParameters();
                setReady(t);
            }
        }

        void remove(android.view.SurfaceControl.Transaction t) {
            this.mAnimationHelper.stopCurrentAnimation(this.mDimSurface);
            if (this.mDimSurface.isValid()) {
                t.remove(this.mDimSurface);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_DIMMER_enabled[0]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(t);
                    com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_DIMMER, 5380455212389185829L, 0, null, protoLogParam0, protoLogParam1);
                    return;
                }
                return;
            }
            android.util.Log.w(com.android.server.wm.SmoothDimmer.TAG, "Tried to remove " + this.mDimSurface + " multiple times\n");
        }

        public java.lang.String toString() {
            return "SmoothDimmer#DimState with host=" + this.mHostContainer + ", surface=" + this.mDimSurface;
        }

        void prepareReparent(com.android.server.wm.WindowContainer relativeParent, int relativeLayer) {
            this.mAnimationHelper.setRequestedRelativeParent(relativeParent, relativeLayer);
        }

        void setReady(android.view.SurfaceControl.Transaction t) {
            this.mAnimationHelper.applyChanges(t, this);
        }

        boolean isDimming() {
            return this.mLastRequestedDimContainer != null;
        }

        private android.view.SurfaceControl makeDimLayer() {
            return com.android.server.wm.SmoothDimmer.this.mHost.makeChildSurface(null).setParent(com.android.server.wm.SmoothDimmer.this.mHost.getSurfaceControl()).setColorLayer().setName("Dim Layer for - " + com.android.server.wm.SmoothDimmer.this.mHost.getName()).setCallsite("DimLayer.makeDimLayer").build();
        }
    }

    protected SmoothDimmer(com.android.server.wm.WindowContainer host) {
        this(host, new com.android.server.wm.DimmerAnimationHelper.AnimationAdapterFactory());
    }

    SmoothDimmer(com.android.server.wm.WindowContainer host, com.android.server.wm.DimmerAnimationHelper.AnimationAdapterFactory animationFactory) {
        super(host);
        this.mDimmerExt = (com.android.server.wm.IDimmerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDimmerExt.class).base(this).create();
        this.mDimmerWrapper = new com.android.server.wm.SmoothDimmer.DimmerWrapper();
        this.mAnimationAdapterFactory = animationFactory;
    }

    @Override // com.android.server.wm.Dimmer
    void resetDimStates() {
        if (this.mDimState != null) {
            this.mDimState.mLastRequestedDimContainer = null;
        }
    }

    @Override // com.android.server.wm.Dimmer
    protected void adjustAppearance(com.android.server.wm.WindowContainer container, float alpha, int blurRadius) {
        com.android.server.wm.SmoothDimmer.DimState d = obtainDimState(container);
        d.prepareLookChange(alpha, blurRadius);
    }

    @Override // com.android.server.wm.Dimmer
    protected void adjustRelativeLayer(com.android.server.wm.WindowContainer container, int relativeLayer) {
        if (this.mDimState != null) {
            this.mDimState.prepareReparent(container, relativeLayer);
        }
    }

    @Override // com.android.server.wm.Dimmer
    boolean updateDims(android.view.SurfaceControl.Transaction t) {
        if (this.mDimState == null) {
            return false;
        }
        if (!this.mDimState.isDimming()) {
            this.mDimState.exit(t);
            this.mDimState = null;
            return false;
        }
        this.mDimmerExt.updateDims(this.mDimState.mLastRequestedDimContainer, this.mDimState.mDimBounds, this.mDimState.mDimSurface, t);
        this.mDimState.adjustSurfaceLayout(t);
        com.android.server.wm.WindowState ws = this.mDimState.mLastRequestedDimContainer.asWindowState();
        if (!this.mDimState.mIsVisible && ws != null && ((ws.mActivityRecord != null && ws.mActivityRecord.mStartingData != null && !(ws.mActivityRecord.mStartingData instanceof com.android.server.wm.SplashScreenStartingData)) || this.mDimmerExt.shouldSkipDimAnimation(ws))) {
            this.mDimState.mSkipAnimation = true;
        }
        this.mDimState.setReady(t);
        return true;
    }

    private com.android.server.wm.SmoothDimmer.DimState obtainDimState(com.android.server.wm.WindowContainer container) {
        if (this.mDimState == null) {
            this.mDimState = new com.android.server.wm.SmoothDimmer.DimState();
        }
        this.mDimState.mLastRequestedDimContainer = container;
        return this.mDimState;
    }

    @Override // com.android.server.wm.Dimmer
    android.view.SurfaceControl getDimLayer() {
        if (this.mDimState != null) {
            return this.mDimState.mDimSurface;
        }
        return null;
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

    public com.android.server.wm.IDimmerWrapper getWrapper() {
        return this.mDimmerWrapper;
    }

    private class DimmerWrapper implements com.android.server.wm.IDimmerWrapper {
        private DimmerWrapper() {
        }

        @Override // com.android.server.wm.IDimmerWrapper
        public com.android.server.wm.IDimmerExt getExtImpl() {
            return com.android.server.wm.SmoothDimmer.this.mDimmerExt;
        }

        @Override // com.android.server.wm.IDimmerWrapper
        public com.android.server.wm.WindowContainer getLastRequestedDimContainer() {
            if (com.android.server.wm.SmoothDimmer.this.mDimState != null) {
                return com.android.server.wm.SmoothDimmer.this.mDimState.mLastRequestedDimContainer;
            }
            return null;
        }
    }
}
