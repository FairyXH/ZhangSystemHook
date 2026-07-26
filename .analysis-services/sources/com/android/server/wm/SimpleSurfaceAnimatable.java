package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class SimpleSurfaceAnimatable implements com.android.server.wm.SurfaceAnimator.Animatable {
    private final java.util.function.Supplier<android.view.SurfaceControl.Builder> mAnimationLeashFactory;
    private final android.view.SurfaceControl mAnimationLeashParent;
    private final java.lang.Runnable mCommitTransactionRunnable;
    private final int mHeight;
    private final java.util.function.Consumer<java.lang.Runnable> mOnAnimationFinished;
    private final java.util.function.BiConsumer<android.view.SurfaceControl.Transaction, android.view.SurfaceControl> mOnAnimationLeashCreated;
    private final java.util.function.Consumer<android.view.SurfaceControl.Transaction> mOnAnimationLeashLost;
    private final android.view.SurfaceControl mParentSurfaceControl;
    private final java.util.function.Supplier<android.view.SurfaceControl.Transaction> mPendingTransaction;
    private final boolean mShouldDeferAnimationFinish;
    private final android.view.SurfaceControl mSurfaceControl;
    private final java.util.function.Supplier<android.view.SurfaceControl.Transaction> mSyncTransaction;
    private final int mWidth;

    private SimpleSurfaceAnimatable(com.android.server.wm.SimpleSurfaceAnimatable.Builder builder) {
        this.mWidth = builder.mWidth;
        this.mHeight = builder.mHeight;
        this.mShouldDeferAnimationFinish = builder.mShouldDeferAnimationFinish;
        this.mAnimationLeashParent = builder.mAnimationLeashParent;
        this.mSurfaceControl = builder.mSurfaceControl;
        this.mParentSurfaceControl = builder.mParentSurfaceControl;
        this.mCommitTransactionRunnable = builder.mCommitTransactionRunnable;
        this.mAnimationLeashFactory = builder.mAnimationLeashFactory;
        this.mOnAnimationLeashCreated = builder.mOnAnimationLeashCreated;
        this.mOnAnimationLeashLost = builder.mOnAnimationLeashLost;
        this.mSyncTransaction = builder.mSyncTransactionSupplier;
        this.mPendingTransaction = builder.mPendingTransactionSupplier;
        this.mOnAnimationFinished = builder.mOnAnimationFinished;
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl.Transaction getSyncTransaction() {
        return this.mSyncTransaction.get();
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl.Transaction getPendingTransaction() {
        return this.mPendingTransaction.get();
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public void commitPendingTransaction() {
        this.mCommitTransactionRunnable.run();
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public void onAnimationLeashCreated(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
        if (this.mOnAnimationLeashCreated != null) {
            this.mOnAnimationLeashCreated.accept(t, leash);
        }
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public void onAnimationLeashLost(android.view.SurfaceControl.Transaction t) {
        if (this.mOnAnimationLeashLost != null) {
            this.mOnAnimationLeashLost.accept(t);
        }
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl.Builder makeAnimationLeash() {
        return this.mAnimationLeashFactory.get();
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl getAnimationLeashParent() {
        return this.mAnimationLeashParent;
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl getSurfaceControl() {
        return this.mSurfaceControl;
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public android.view.SurfaceControl getParentSurfaceControl() {
        return this.mParentSurfaceControl;
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public int getSurfaceWidth() {
        return this.mWidth;
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public int getSurfaceHeight() {
        return this.mHeight;
    }

    @Override // com.android.server.wm.SurfaceAnimator.Animatable
    public boolean shouldDeferAnimationFinish(java.lang.Runnable endDeferFinishCallback) {
        if (this.mOnAnimationFinished != null) {
            this.mOnAnimationFinished.accept(endDeferFinishCallback);
        }
        return this.mShouldDeferAnimationFinish;
    }

    static class Builder {
        private java.util.function.Supplier<android.view.SurfaceControl.Builder> mAnimationLeashFactory;
        private java.lang.Runnable mCommitTransactionRunnable;
        private java.util.function.Supplier<android.view.SurfaceControl.Transaction> mPendingTransactionSupplier;
        private java.util.function.Supplier<android.view.SurfaceControl.Transaction> mSyncTransactionSupplier;
        private int mWidth = -1;
        private int mHeight = -1;
        private boolean mShouldDeferAnimationFinish = false;
        private android.view.SurfaceControl mAnimationLeashParent = null;
        private android.view.SurfaceControl mSurfaceControl = null;
        private android.view.SurfaceControl mParentSurfaceControl = null;
        private java.util.function.BiConsumer<android.view.SurfaceControl.Transaction, android.view.SurfaceControl> mOnAnimationLeashCreated = null;
        private java.util.function.Consumer<android.view.SurfaceControl.Transaction> mOnAnimationLeashLost = null;
        private java.util.function.Consumer<java.lang.Runnable> mOnAnimationFinished = null;

        Builder() {
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setCommitTransactionRunnable(java.lang.Runnable commitTransactionRunnable) {
            this.mCommitTransactionRunnable = commitTransactionRunnable;
            return this;
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setOnAnimationLeashCreated(java.util.function.BiConsumer<android.view.SurfaceControl.Transaction, android.view.SurfaceControl> onAnimationLeashCreated) {
            this.mOnAnimationLeashCreated = onAnimationLeashCreated;
            return this;
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setOnAnimationLeashLost(java.util.function.Consumer<android.view.SurfaceControl.Transaction> onAnimationLeashLost) {
            this.mOnAnimationLeashLost = onAnimationLeashLost;
            return this;
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setSyncTransactionSupplier(java.util.function.Supplier<android.view.SurfaceControl.Transaction> syncTransactionSupplier) {
            this.mSyncTransactionSupplier = syncTransactionSupplier;
            return this;
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setPendingTransactionSupplier(java.util.function.Supplier<android.view.SurfaceControl.Transaction> pendingTransactionSupplier) {
            this.mPendingTransactionSupplier = pendingTransactionSupplier;
            return this;
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setAnimationLeashSupplier(java.util.function.Supplier<android.view.SurfaceControl.Builder> animationLeashFactory) {
            this.mAnimationLeashFactory = animationLeashFactory;
            return this;
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setAnimationLeashParent(android.view.SurfaceControl animationLeashParent) {
            this.mAnimationLeashParent = animationLeashParent;
            return this;
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setSurfaceControl(android.view.SurfaceControl surfaceControl) {
            this.mSurfaceControl = surfaceControl;
            return this;
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setParentSurfaceControl(android.view.SurfaceControl parentSurfaceControl) {
            this.mParentSurfaceControl = parentSurfaceControl;
            return this;
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setWidth(int width) {
            this.mWidth = width;
            return this;
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setHeight(int height) {
            this.mHeight = height;
            return this;
        }

        public com.android.server.wm.SimpleSurfaceAnimatable.Builder setShouldDeferAnimationFinish(boolean shouldDeferAnimationFinish, java.util.function.Consumer<java.lang.Runnable> onAnimationFinish) {
            this.mShouldDeferAnimationFinish = shouldDeferAnimationFinish;
            this.mOnAnimationFinished = onAnimationFinish;
            return this;
        }

        public com.android.server.wm.SurfaceAnimator.Animatable build() {
            if (this.mSyncTransactionSupplier == null) {
                throw new java.lang.IllegalArgumentException("mSyncTransactionSupplier cannot be null");
            }
            if (this.mPendingTransactionSupplier == null) {
                throw new java.lang.IllegalArgumentException("mPendingTransactionSupplier cannot be null");
            }
            if (this.mAnimationLeashFactory == null) {
                throw new java.lang.IllegalArgumentException("mAnimationLeashFactory cannot be null");
            }
            if (this.mCommitTransactionRunnable == null) {
                throw new java.lang.IllegalArgumentException("mCommitTransactionRunnable cannot be null");
            }
            if (this.mSurfaceControl == null) {
                throw new java.lang.IllegalArgumentException("mSurfaceControl cannot be null");
            }
            return new com.android.server.wm.SimpleSurfaceAnimatable(this);
        }
    }
}
