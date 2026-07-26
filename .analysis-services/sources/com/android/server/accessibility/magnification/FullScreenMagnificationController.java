package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class FullScreenMagnificationController implements com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_SET_MAGNIFICATION_SPEC = false;
    private static final java.lang.String LOG_TAG = "FullScreenMagnificationController";
    private boolean mAlwaysOnMagnificationEnabled;
    private final com.android.server.accessibility.magnification.FullScreenMagnificationController.ControllerContext mControllerCtx;
    private final android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    private final android.util.SparseArray<com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification> mDisplays;
    private final java.lang.Object mLock;
    private final java.util.function.Supplier<java.lang.Boolean> mMagnificationConnectionStateSupplier;
    private boolean mMagnificationFollowTypingEnabled;
    private final java.util.ArrayList<com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback> mMagnificationInfoChangedCallbacks;
    private final com.android.server.accessibility.magnification.MagnificationThumbnailFeatureFlag mMagnificationThumbnailFeatureFlag;
    private final long mMainThreadId;
    private final com.android.server.accessibility.magnification.MagnificationScaleProvider mScaleProvider;
    private final com.android.server.accessibility.magnification.FullScreenMagnificationController.ScreenStateObserver mScreenStateObserver;
    private final java.util.function.Supplier<android.widget.Scroller> mScrollerSupplier;
    private final android.graphics.Rect mTempRect;
    private final java.util.function.Supplier<com.android.server.accessibility.magnification.MagnificationThumbnail> mThumbnailSupplier;
    private final java.util.function.Supplier<android.animation.TimeAnimator> mTimeAnimatorSupplier;

    interface MagnificationInfoChangedCallback {
        void onFullScreenMagnificationActivationState(int i, boolean z);

        void onFullScreenMagnificationChanged(int i, android.graphics.Region region, android.accessibilityservice.MagnificationConfig magnificationConfig);

        void onImeWindowVisibilityChanged(int i, boolean z);

        void onRequestMagnificationSpec(int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class DisplayMagnification implements com.android.server.wm.WindowManagerInternal.MagnificationCallbacks {
        private boolean mDeleteAfterUnregister;
        private final int mDisplayId;
        private com.android.server.accessibility.magnification.MagnificationThumbnail mMagnificationThumbnail;
        private boolean mRegistered;
        private final com.android.server.accessibility.magnification.FullScreenMagnificationController.SpecAnimationBridge mSpecAnimationBridge;
        private boolean mUnregisterPending;
        private final android.view.MagnificationSpec mCurrentMagnificationSpec = new android.view.MagnificationSpec();
        private final android.graphics.Region mMagnificationRegion = android.graphics.Region.obtain();
        private final android.graphics.Rect mMagnificationBounds = new android.graphics.Rect();
        private final android.graphics.Rect mTempRect = new android.graphics.Rect();
        private final android.graphics.Rect mTempRect1 = new android.graphics.Rect();
        private int mIdOfLastServiceToMagnify = -1;
        private boolean mMagnificationActivated = false;
        private boolean mZoomedOutFromService = false;

        DisplayMagnification(int displayId) {
            this.mDisplayId = displayId;
            this.mSpecAnimationBridge = new com.android.server.accessibility.magnification.FullScreenMagnificationController.SpecAnimationBridge(com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx, com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mLock, this.mDisplayId, com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mScrollerSupplier, com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mTimeAnimatorSupplier);
        }

        boolean register() {
            if (com.android.server.accessibility.magnification.FullScreenMagnificationController.this.traceEnabled()) {
                com.android.server.accessibility.magnification.FullScreenMagnificationController.this.logTrace("setMagnificationCallbacks", "displayID=" + this.mDisplayId + ";callback=" + this);
            }
            this.mRegistered = com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx.getWindowManager().setMagnificationCallbacks(this.mDisplayId, this);
            if (!this.mRegistered) {
                android.util.Slog.w(com.android.server.accessibility.magnification.FullScreenMagnificationController.LOG_TAG, "set magnification callbacks fail, displayId:" + this.mDisplayId);
                return false;
            }
            this.mSpecAnimationBridge.setEnabled(true);
            if (com.android.server.accessibility.magnification.FullScreenMagnificationController.this.traceEnabled()) {
                com.android.server.accessibility.magnification.FullScreenMagnificationController.this.logTrace("getMagnificationRegion", "displayID=" + this.mDisplayId + ";region=" + this.mMagnificationRegion);
            }
            com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx.getWindowManager().getMagnificationRegion(this.mDisplayId, this.mMagnificationRegion);
            this.mMagnificationRegion.getBounds(this.mMagnificationBounds);
            createThumbnailIfSupported();
            return true;
        }

        void unregister(boolean delete) {
            if (this.mRegistered) {
                this.mSpecAnimationBridge.setEnabled(false);
                if (com.android.server.accessibility.magnification.FullScreenMagnificationController.this.traceEnabled()) {
                    com.android.server.accessibility.magnification.FullScreenMagnificationController.this.logTrace("setMagnificationCallbacks", "displayID=" + this.mDisplayId + ";callback=null");
                }
                com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx.getWindowManager().setMagnificationCallbacks(this.mDisplayId, null);
                this.mMagnificationRegion.setEmpty();
                this.mRegistered = false;
                com.android.server.accessibility.magnification.FullScreenMagnificationController.this.unregisterCallbackLocked(this.mDisplayId, delete);
                destroyThumbnail();
            }
            this.mUnregisterPending = false;
        }

        void unregisterPending(boolean delete) {
            this.mDeleteAfterUnregister = delete;
            this.mUnregisterPending = true;
            reset(true);
        }

        boolean isRegistered() {
            return this.mRegistered;
        }

        boolean isActivated() {
            return this.mMagnificationActivated;
        }

        float getScale() {
            return this.mCurrentMagnificationSpec.scale;
        }

        float getOffsetX() {
            return this.mCurrentMagnificationSpec.offsetX;
        }

        float getOffsetY() {
            return this.mCurrentMagnificationSpec.offsetY;
        }

        boolean isAtEdge() {
            return isAtLeftEdge(0.0f) || isAtRightEdge(0.0f) || isAtTopEdge(0.0f) || isAtBottomEdge(0.0f);
        }

        boolean isAtLeftEdge(float slop) {
            return android.util.MathUtils.abs(getOffsetX() - getMaxOffsetXLocked()) <= slop;
        }

        boolean isAtRightEdge(float slop) {
            return android.util.MathUtils.abs(getOffsetX() - getMinOffsetXLocked()) <= slop;
        }

        boolean isAtTopEdge(float slop) {
            return android.util.MathUtils.abs(getOffsetY() - getMaxOffsetYLocked()) <= slop;
        }

        boolean isAtBottomEdge(float slop) {
            return android.util.MathUtils.abs(getOffsetY() - getMinOffsetYLocked()) <= slop;
        }

        float getCenterX() {
            return (((this.mMagnificationBounds.width() / 2.0f) + this.mMagnificationBounds.left) - getOffsetX()) / getScale();
        }

        float getCenterY() {
            return (((this.mMagnificationBounds.height() / 2.0f) + this.mMagnificationBounds.top) - getOffsetY()) / getScale();
        }

        float getSentScale() {
            return this.mSpecAnimationBridge.mSentMagnificationSpec.scale;
        }

        float getSentOffsetX() {
            return this.mSpecAnimationBridge.mSentMagnificationSpec.offsetX;
        }

        float getSentOffsetY() {
            return this.mSpecAnimationBridge.mSentMagnificationSpec.offsetY;
        }

        @Override // com.android.server.wm.WindowManagerInternal.MagnificationCallbacks
        public void onMagnificationRegionChanged(android.graphics.Region magnificationRegion) {
            android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$DisplayMagnification$$ExternalSyntheticLambda4
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification) obj).updateMagnificationRegion((android.graphics.Region) obj2);
                }
            }, this, android.graphics.Region.obtain(magnificationRegion));
            com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx.getHandler().sendMessage(m);
        }

        @Override // com.android.server.wm.WindowManagerInternal.MagnificationCallbacks
        public void onRectangleOnScreenRequested(int left, int top, int right, int bottom) {
            android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$DisplayMagnification$$ExternalSyntheticLambda8
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                    ((com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification) obj).requestRectangleOnScreen(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), ((java.lang.Integer) obj5).intValue());
                }
            }, this, java.lang.Integer.valueOf(left), java.lang.Integer.valueOf(top), java.lang.Integer.valueOf(right), java.lang.Integer.valueOf(bottom));
            com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx.getHandler().sendMessage(m);
        }

        @Override // com.android.server.wm.WindowManagerInternal.MagnificationCallbacks
        public void onDisplaySizeChanged() {
            onUserContextChanged();
        }

        @Override // com.android.server.wm.WindowManagerInternal.MagnificationCallbacks
        public void onUserContextChanged() {
            android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$DisplayMagnification$$ExternalSyntheticLambda2
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.accessibility.magnification.FullScreenMagnificationController) obj).onUserContextChanged(((java.lang.Integer) obj2).intValue());
                }
            }, com.android.server.accessibility.magnification.FullScreenMagnificationController.this, java.lang.Integer.valueOf(this.mDisplayId));
            com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx.getHandler().sendMessage(m);
            synchronized (com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mLock) {
                refreshThumbnail();
            }
        }

        @Override // com.android.server.wm.WindowManagerInternal.MagnificationCallbacks
        public void onImeWindowVisibilityChanged(boolean shown) {
            android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$DisplayMagnification$$ExternalSyntheticLambda1
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                    ((com.android.server.accessibility.magnification.FullScreenMagnificationController) obj).notifyImeWindowVisibilityChanged(((java.lang.Integer) obj2).intValue(), ((java.lang.Boolean) obj3).booleanValue());
                }
            }, com.android.server.accessibility.magnification.FullScreenMagnificationController.this, java.lang.Integer.valueOf(this.mDisplayId), java.lang.Boolean.valueOf(shown));
            com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx.getHandler().sendMessage(m);
        }

        void updateMagnificationRegion(android.graphics.Region magnified) {
            synchronized (com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mLock) {
                if (this.mRegistered) {
                    if (!this.mMagnificationRegion.equals(magnified)) {
                        this.mMagnificationRegion.set(magnified);
                        this.mMagnificationRegion.getBounds(this.mMagnificationBounds);
                        refreshThumbnail();
                        if (updateCurrentSpecWithOffsetsLocked(this.mCurrentMagnificationSpec.offsetX, this.mCurrentMagnificationSpec.offsetY)) {
                            sendSpecToAnimation(this.mCurrentMagnificationSpec, null);
                        }
                        onMagnificationChangedLocked();
                    }
                    magnified.recycle();
                }
            }
        }

        void sendSpecToAnimation(android.view.MagnificationSpec spec, android.view.accessibility.MagnificationAnimationCallback animationCallback) {
            if (java.lang.Thread.currentThread().getId() == com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mMainThreadId) {
                this.mSpecAnimationBridge.updateSentSpecMainThread(spec, animationCallback);
            } else {
                android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$DisplayMagnification$$ExternalSyntheticLambda6
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((com.android.server.accessibility.magnification.FullScreenMagnificationController.SpecAnimationBridge) obj).updateSentSpecMainThread((android.view.MagnificationSpec) obj2, (android.view.accessibility.MagnificationAnimationCallback) obj3);
                    }
                }, this.mSpecAnimationBridge, spec, animationCallback);
                com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx.getHandler().sendMessage(m);
            }
        }

        void startFlingAnimation(float xPixelsPerSecond, float yPixelsPerSecond, android.view.accessibility.MagnificationAnimationCallback animationCallback) {
            if (java.lang.Thread.currentThread().getId() == com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mMainThreadId) {
                this.mSpecAnimationBridge.startFlingAnimation(xPixelsPerSecond, yPixelsPerSecond, getMinOffsetXLocked(), getMaxOffsetXLocked(), getMinOffsetYLocked(), getMaxOffsetYLocked(), animationCallback);
            } else {
                android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.OctConsumer() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$DisplayMagnification$$ExternalSyntheticLambda0
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6, java.lang.Object obj7, java.lang.Object obj8) {
                        ((com.android.server.accessibility.magnification.FullScreenMagnificationController.SpecAnimationBridge) obj).startFlingAnimation(((java.lang.Float) obj2).floatValue(), ((java.lang.Float) obj3).floatValue(), ((java.lang.Float) obj4).floatValue(), ((java.lang.Float) obj5).floatValue(), ((java.lang.Float) obj6).floatValue(), ((java.lang.Float) obj7).floatValue(), (android.view.accessibility.MagnificationAnimationCallback) obj8);
                    }
                }, this.mSpecAnimationBridge, java.lang.Float.valueOf(xPixelsPerSecond), java.lang.Float.valueOf(yPixelsPerSecond), java.lang.Float.valueOf(getMinOffsetXLocked()), java.lang.Float.valueOf(getMaxOffsetXLocked()), java.lang.Float.valueOf(getMinOffsetYLocked()), java.lang.Float.valueOf(getMaxOffsetYLocked()), animationCallback);
                com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx.getHandler().sendMessage(m);
            }
        }

        void cancelFlingAnimation() {
            if (java.lang.Thread.currentThread().getId() == com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mMainThreadId) {
                this.mSpecAnimationBridge.cancelFlingAnimation();
                return;
            }
            android.os.Handler handler = com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx.getHandler();
            final com.android.server.accessibility.magnification.FullScreenMagnificationController.SpecAnimationBridge specAnimationBridge = this.mSpecAnimationBridge;
            java.util.Objects.requireNonNull(specAnimationBridge);
            handler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$DisplayMagnification$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    specAnimationBridge.cancelFlingAnimation();
                }
            });
        }

        int getIdOfLastServiceToMagnify() {
            return this.mIdOfLastServiceToMagnify;
        }

        void onMagnificationChangedLocked() {
            float scale = getScale();
            float centerX = getCenterX();
            float centerY = getCenterY();
            final android.accessibilityservice.MagnificationConfig config = new android.accessibilityservice.MagnificationConfig.Builder().setMode(1).setActivated(this.mMagnificationActivated).setScale(scale).setCenterX(centerX).setCenterY(centerY).build();
            com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mMagnificationInfoChangedCallbacks.forEach(new java.util.function.Consumer() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$DisplayMagnification$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onMagnificationChangedLocked$0(config, (com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback) obj);
                }
            });
            if (this.mUnregisterPending && !isActivated()) {
                unregister(this.mDeleteAfterUnregister);
            }
            if (isActivated()) {
                updateThumbnail(scale, centerX, centerY);
            } else {
                hideThumbnail();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMagnificationChangedLocked$0(android.accessibilityservice.MagnificationConfig config, com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback callback) {
            callback.onFullScreenMagnificationChanged(this.mDisplayId, this.mMagnificationRegion, config);
        }

        boolean magnificationRegionContains(float x, float y) {
            return this.mMagnificationRegion.contains((int) x, (int) y);
        }

        void getMagnificationBounds(android.graphics.Rect outBounds) {
            outBounds.set(this.mMagnificationBounds);
        }

        void getMagnificationRegion(android.graphics.Region outRegion) {
            outRegion.set(this.mMagnificationRegion);
        }

        private android.util.DisplayMetrics getDisplayMetricsForId() {
            android.util.DisplayMetrics outMetrics = new android.util.DisplayMetrics();
            android.view.DisplayInfo displayInfo = com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mDisplayManagerInternal.getDisplayInfo(this.mDisplayId);
            if (displayInfo != null) {
                displayInfo.getLogicalMetrics(outMetrics, android.content.res.CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO, (android.content.res.Configuration) null);
            } else {
                outMetrics.setToDefaults();
            }
            return outMetrics;
        }

        void requestRectangleOnScreen(int left, int top, int right, int bottom) {
            float scrollX;
            float scrollY;
            synchronized (com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mLock) {
                android.graphics.Rect magnifiedFrame = this.mTempRect;
                getMagnificationBounds(magnifiedFrame);
                if (magnifiedFrame.intersects(left, top, right, bottom)) {
                    android.graphics.Rect magnifFrameInScreenCoords = this.mTempRect1;
                    getMagnifiedFrameInContentCoordsLocked(magnifFrameInScreenCoords);
                    android.util.DisplayMetrics metrics = getDisplayMetricsForId();
                    float offsetViewportX = magnifFrameInScreenCoords.width() / 4.0f;
                    float offsetViewportY = android.util.TypedValue.applyDimension(1, 10.0f, metrics);
                    if (right - left > magnifFrameInScreenCoords.width()) {
                        int direction = android.text.TextUtils.getLayoutDirectionFromLocale(java.util.Locale.getDefault());
                        if (direction == 0) {
                            scrollX = left - magnifFrameInScreenCoords.left;
                        } else {
                            scrollX = right - magnifFrameInScreenCoords.right;
                        }
                    } else if (left < magnifFrameInScreenCoords.left) {
                        scrollX = (left - magnifFrameInScreenCoords.left) - offsetViewportX;
                    } else if (right > magnifFrameInScreenCoords.right) {
                        scrollX = (right - magnifFrameInScreenCoords.right) + offsetViewportX;
                    } else {
                        scrollX = 0.0f;
                    }
                    if (bottom - top > magnifFrameInScreenCoords.height()) {
                        scrollY = top - magnifFrameInScreenCoords.top;
                    } else if (top < magnifFrameInScreenCoords.top) {
                        scrollY = (top - magnifFrameInScreenCoords.top) - offsetViewportY;
                    } else if (bottom > magnifFrameInScreenCoords.bottom) {
                        scrollY = (bottom - magnifFrameInScreenCoords.bottom) + offsetViewportY;
                    } else {
                        scrollY = 0.0f;
                    }
                    float scale = getScale();
                    offsetMagnifiedRegion(scrollX * scale, scrollY * scale, -1);
                }
            }
        }

        void getMagnifiedFrameInContentCoordsLocked(android.graphics.Rect outFrame) {
            float scale = getSentScale();
            float offsetX = getSentOffsetX();
            float offsetY = getSentOffsetY();
            getMagnificationBounds(outFrame);
            outFrame.offset((int) (-offsetX), (int) (-offsetY));
            outFrame.scale(1.0f / scale);
        }

        private boolean setActivated(boolean activated) {
            boolean changed = this.mMagnificationActivated != activated;
            if (changed) {
                this.mMagnificationActivated = activated;
                com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mMagnificationInfoChangedCallbacks.forEach(new java.util.function.Consumer() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$DisplayMagnification$$ExternalSyntheticLambda7
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$setActivated$1((com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback) obj);
                    }
                });
                com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mControllerCtx.getWindowManager().setFullscreenMagnificationActivated(this.mDisplayId, activated);
            }
            return changed;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setActivated$1(com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback callback) {
            callback.onFullScreenMagnificationActivationState(this.mDisplayId, this.mMagnificationActivated);
        }

        void zoomOutFromService() {
            setScaleAndCenter(1.0f, Float.NaN, Float.NaN, com.android.server.accessibility.magnification.FullScreenMagnificationController.transformToStubCallback(true), 0);
            this.mZoomedOutFromService = true;
        }

        boolean isZoomedOutFromService() {
            return this.mZoomedOutFromService;
        }

        boolean reset(boolean animate) {
            return reset(com.android.server.accessibility.magnification.FullScreenMagnificationController.transformToStubCallback(animate));
        }

        boolean reset(android.view.accessibility.MagnificationAnimationCallback animationCallback) {
            if (!this.mRegistered) {
                return false;
            }
            android.view.MagnificationSpec spec = this.mCurrentMagnificationSpec;
            boolean changed = isActivated();
            setActivated(false);
            if (changed) {
                spec.clear();
                onMagnificationChangedLocked();
            }
            this.mIdOfLastServiceToMagnify = -1;
            sendSpecToAnimation(spec, animationCallback);
            hideThumbnail();
            return changed;
        }

        boolean setScale(float scale, float pivotX, float pivotY, boolean animate, int id) {
            if (!this.mRegistered) {
                return false;
            }
            float scale2 = com.android.server.accessibility.magnification.MagnificationScaleProvider.constrainScale(scale);
            android.graphics.Rect viewport = this.mTempRect;
            this.mMagnificationRegion.getBounds(viewport);
            android.view.MagnificationSpec spec = this.mCurrentMagnificationSpec;
            float oldScale = spec.scale;
            float oldCenterX = (((viewport.width() / 2.0f) - spec.offsetX) + viewport.left) / oldScale;
            float oldCenterY = (((viewport.height() / 2.0f) - spec.offsetY) + viewport.top) / oldScale;
            float normPivotX = (pivotX - spec.offsetX) / oldScale;
            float normPivotY = (pivotY - spec.offsetY) / oldScale;
            float offsetX = (oldCenterX - normPivotX) * (oldScale / scale2);
            float offsetY = (oldCenterY - normPivotY) * (oldScale / scale2);
            float centerX = normPivotX + offsetX;
            float centerY = normPivotY + offsetY;
            this.mIdOfLastServiceToMagnify = id;
            return setScaleAndCenter(scale2, centerX, centerY, com.android.server.accessibility.magnification.FullScreenMagnificationController.transformToStubCallback(animate), id);
        }

        boolean setScaleAndCenter(float scale, float centerX, float centerY, android.view.accessibility.MagnificationAnimationCallback animationCallback, int id) {
            if (!this.mRegistered) {
                return false;
            }
            if (com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder() && !((java.lang.Boolean) com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mMagnificationConnectionStateSupplier.get()).booleanValue()) {
                return false;
            }
            boolean changed = setActivated(true);
            boolean changed2 = changed | updateMagnificationSpecLocked(scale, centerX, centerY);
            sendSpecToAnimation(this.mCurrentMagnificationSpec, animationCallback);
            if (isActivated() && id != -1) {
                this.mIdOfLastServiceToMagnify = id;
                com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mMagnificationInfoChangedCallbacks.forEach(new java.util.function.Consumer() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$DisplayMagnification$$ExternalSyntheticLambda5
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$setScaleAndCenter$2((com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback) obj);
                    }
                });
            }
            this.mZoomedOutFromService = false;
            return changed2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setScaleAndCenter$2(com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback callback) {
            callback.onRequestMagnificationSpec(this.mDisplayId, this.mIdOfLastServiceToMagnify);
        }

        void updateThumbnail(float scale, float centerX, float centerY) {
            if (this.mMagnificationThumbnail != null) {
                this.mMagnificationThumbnail.updateThumbnail(scale, centerX, centerY);
            }
        }

        void refreshThumbnail() {
            if (this.mMagnificationThumbnail != null) {
                this.mMagnificationThumbnail.setThumbnailBounds(this.mMagnificationBounds, getScale(), getCenterX(), getCenterY());
            }
        }

        void hideThumbnail() {
            if (this.mMagnificationThumbnail != null) {
                this.mMagnificationThumbnail.hideThumbnail();
            }
        }

        void createThumbnailIfSupported() {
            if (this.mMagnificationThumbnail == null) {
                this.mMagnificationThumbnail = (com.android.server.accessibility.magnification.MagnificationThumbnail) com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mThumbnailSupplier.get();
                refreshThumbnail();
            }
        }

        void destroyThumbnail() {
            if (this.mMagnificationThumbnail != null) {
                hideThumbnail();
                this.mMagnificationThumbnail = null;
            }
        }

        void onThumbnailFeatureFlagChanged() {
            synchronized (com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mLock) {
                destroyThumbnail();
                createThumbnailIfSupported();
            }
        }

        boolean updateMagnificationSpecLocked(float scale, float centerX, float centerY) {
            if (java.lang.Float.isNaN(centerX)) {
                centerX = getCenterX();
            }
            if (java.lang.Float.isNaN(centerY)) {
                centerY = getCenterY();
            }
            if (java.lang.Float.isNaN(scale)) {
                scale = getScale();
            }
            boolean changed = false;
            float normScale = com.android.server.accessibility.magnification.MagnificationScaleProvider.constrainScale(scale);
            if (java.lang.Float.compare(this.mCurrentMagnificationSpec.scale, normScale) != 0) {
                this.mCurrentMagnificationSpec.scale = normScale;
                changed = true;
            }
            float nonNormOffsetX = ((this.mMagnificationBounds.width() / 2.0f) + this.mMagnificationBounds.left) - (centerX * normScale);
            float nonNormOffsetY = ((this.mMagnificationBounds.height() / 2.0f) + this.mMagnificationBounds.top) - (centerY * normScale);
            boolean changed2 = changed | updateCurrentSpecWithOffsetsLocked(nonNormOffsetX, nonNormOffsetY);
            if (changed2) {
                onMagnificationChangedLocked();
            }
            return changed2;
        }

        void offsetMagnifiedRegion(float offsetX, float offsetY, int id) {
            if (!this.mRegistered) {
                return;
            }
            float nonNormOffsetX = this.mCurrentMagnificationSpec.offsetX - offsetX;
            float nonNormOffsetY = this.mCurrentMagnificationSpec.offsetY - offsetY;
            if (updateCurrentSpecWithOffsetsLocked(nonNormOffsetX, nonNormOffsetY)) {
                onMagnificationChangedLocked();
            }
            if (id != -1) {
                this.mIdOfLastServiceToMagnify = id;
            }
            sendSpecToAnimation(this.mCurrentMagnificationSpec, null);
        }

        void startFling(float xPixelsPerSecond, float yPixelsPerSecond, int id) {
            if (!this.mRegistered || !isActivated()) {
                return;
            }
            if (id != -1) {
                this.mIdOfLastServiceToMagnify = id;
            }
            startFlingAnimation(xPixelsPerSecond, yPixelsPerSecond, new android.view.accessibility.MagnificationAnimationCallback() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification.1
                public void onResult(boolean success) {
                }

                public void onResult(boolean success, android.view.MagnificationSpec lastSpecSent) {
                    synchronized (com.android.server.accessibility.magnification.FullScreenMagnificationController.this.mLock) {
                        com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification.this.mCurrentMagnificationSpec.setTo(lastSpecSent);
                        com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification.this.onMagnificationChangedLocked();
                    }
                }
            });
        }

        void cancelFling(int id) {
            if (!this.mRegistered) {
                return;
            }
            if (id != -1) {
                this.mIdOfLastServiceToMagnify = id;
            }
            cancelFlingAnimation();
        }

        boolean updateCurrentSpecWithOffsetsLocked(float nonNormOffsetX, float nonNormOffsetY) {
            boolean changed = false;
            float offsetX = android.util.MathUtils.constrain(nonNormOffsetX, getMinOffsetXLocked(), getMaxOffsetXLocked());
            if (java.lang.Float.compare(this.mCurrentMagnificationSpec.offsetX, offsetX) != 0) {
                this.mCurrentMagnificationSpec.offsetX = offsetX;
                changed = true;
            }
            float offsetY = android.util.MathUtils.constrain(nonNormOffsetY, getMinOffsetYLocked(), getMaxOffsetYLocked());
            if (java.lang.Float.compare(this.mCurrentMagnificationSpec.offsetY, offsetY) != 0) {
                this.mCurrentMagnificationSpec.offsetY = offsetY;
                return true;
            }
            return changed;
        }

        float getMinOffsetXLocked() {
            float viewportWidth = this.mMagnificationBounds.width();
            float viewportLeft = this.mMagnificationBounds.left;
            return (viewportLeft + viewportWidth) - ((viewportLeft + viewportWidth) * this.mCurrentMagnificationSpec.scale);
        }

        float getMaxOffsetXLocked() {
            return this.mMagnificationBounds.left - (this.mMagnificationBounds.left * this.mCurrentMagnificationSpec.scale);
        }

        float getMinOffsetYLocked() {
            float viewportHeight = this.mMagnificationBounds.height();
            float viewportTop = this.mMagnificationBounds.top;
            return (viewportTop + viewportHeight) - ((viewportTop + viewportHeight) * this.mCurrentMagnificationSpec.scale);
        }

        float getMaxOffsetYLocked() {
            return this.mMagnificationBounds.top - (this.mMagnificationBounds.top * this.mCurrentMagnificationSpec.scale);
        }

        public java.lang.String toString() {
            return "DisplayMagnification[mCurrentMagnificationSpec=" + this.mCurrentMagnificationSpec + ", mMagnificationRegion=" + this.mMagnificationRegion + ", mMagnificationBounds=" + this.mMagnificationBounds + ", mDisplayId=" + this.mDisplayId + ", mIdOfLastServiceToMagnify=" + this.mIdOfLastServiceToMagnify + ", mRegistered=" + this.mRegistered + ", mUnregisterPending=" + this.mUnregisterPending + ']';
        }
    }

    public FullScreenMagnificationController(final android.content.Context context, com.android.server.accessibility.AccessibilityTraceManager traceManager, java.lang.Object lock, com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback magnificationInfoChangedCallback, com.android.server.accessibility.magnification.MagnificationScaleProvider scaleProvider, java.util.concurrent.Executor backgroundExecutor, java.util.function.Supplier<java.lang.Boolean> magnificationConnectionStateSupplier) {
        this(new com.android.server.accessibility.magnification.FullScreenMagnificationController.ControllerContext(context, traceManager, (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class), new android.os.Handler(context.getMainLooper()), context.getResources().getInteger(android.R.integer.config_longAnimTime)), lock, magnificationInfoChangedCallback, scaleProvider, null, backgroundExecutor, new java.util.function.Supplier() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$$ExternalSyntheticLambda4
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.accessibility.magnification.FullScreenMagnificationController.lambda$new$0(context);
            }
        }, new java.util.function.Supplier() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return new android.animation.TimeAnimator();
            }
        }, magnificationConnectionStateSupplier);
    }

    static /* synthetic */ android.widget.Scroller lambda$new$0(android.content.Context context) {
        return new android.widget.Scroller(context);
    }

    public FullScreenMagnificationController(final com.android.server.accessibility.magnification.FullScreenMagnificationController.ControllerContext ctx, java.lang.Object lock, com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback magnificationInfoChangedCallback, com.android.server.accessibility.magnification.MagnificationScaleProvider scaleProvider, java.util.function.Supplier<com.android.server.accessibility.magnification.MagnificationThumbnail> thumbnailSupplier, java.util.concurrent.Executor backgroundExecutor, java.util.function.Supplier<android.widget.Scroller> scrollerSupplier, java.util.function.Supplier<android.animation.TimeAnimator> timeAnimatorSupplier, java.util.function.Supplier<java.lang.Boolean> magnificationConnectionStateSupplier) {
        this.mMagnificationInfoChangedCallbacks = new java.util.ArrayList<>();
        this.mDisplays = new android.util.SparseArray<>(0);
        this.mTempRect = new android.graphics.Rect();
        this.mMagnificationFollowTypingEnabled = true;
        this.mAlwaysOnMagnificationEnabled = false;
        this.mControllerCtx = ctx;
        this.mLock = lock;
        this.mScrollerSupplier = scrollerSupplier;
        this.mTimeAnimatorSupplier = timeAnimatorSupplier;
        this.mMagnificationConnectionStateSupplier = magnificationConnectionStateSupplier;
        this.mMainThreadId = this.mControllerCtx.getContext().getMainLooper().getThread().getId();
        this.mScreenStateObserver = new com.android.server.accessibility.magnification.FullScreenMagnificationController.ScreenStateObserver(this.mControllerCtx.getContext(), this);
        addInfoChangedCallback(magnificationInfoChangedCallback);
        this.mScaleProvider = scaleProvider;
        this.mDisplayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        this.mMagnificationThumbnailFeatureFlag = new com.android.server.accessibility.magnification.MagnificationThumbnailFeatureFlag();
        this.mMagnificationThumbnailFeatureFlag.addOnChangedListener(backgroundExecutor, new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.onMagnificationThumbnailFeatureFlagChanged();
            }
        });
        if (thumbnailSupplier != null) {
            this.mThumbnailSupplier = thumbnailSupplier;
        } else {
            this.mThumbnailSupplier = new java.util.function.Supplier() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$$ExternalSyntheticLambda3
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$new$1(ctx);
                }
            };
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.accessibility.magnification.MagnificationThumbnail lambda$new$1(com.android.server.accessibility.magnification.FullScreenMagnificationController.ControllerContext ctx) {
        if (this.mMagnificationThumbnailFeatureFlag.isFeatureFlagEnabled()) {
            return new com.android.server.accessibility.magnification.MagnificationThumbnail(ctx.getContext(), (android.view.WindowManager) ctx.getContext().getSystemService(android.view.WindowManager.class), new android.os.Handler(ctx.getContext().getMainLooper()));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMagnificationThumbnailFeatureFlagChanged() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mDisplays.size(); i++) {
                onMagnificationThumbnailFeatureFlagChanged(this.mDisplays.keyAt(i));
            }
        }
    }

    private void onMagnificationThumbnailFeatureFlagChanged(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return;
            }
            display.onThumbnailFeatureFlagChanged();
        }
    }

    public void register(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                display = new com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification(displayId);
            }
            if (display.isRegistered()) {
                return;
            }
            if (display.register()) {
                this.mDisplays.put(displayId, display);
                this.mScreenStateObserver.registerIfNecessary();
            }
        }
    }

    public void unregister(int displayId) {
        synchronized (this.mLock) {
            unregisterLocked(displayId, false);
        }
    }

    public void unregisterAll() {
        synchronized (this.mLock) {
            android.util.SparseArray<com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification> displays = this.mDisplays.clone();
            for (int i = 0; i < displays.size(); i++) {
                unregisterLocked(displays.keyAt(i), false);
            }
        }
    }

    @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks
    public void onRectangleOnScreenRequested(int displayId, int left, int top, int right, int bottom) {
        synchronized (this.mLock) {
            if (this.mMagnificationFollowTypingEnabled) {
                com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
                if (display == null) {
                    return;
                }
                if (display.isActivated()) {
                    android.graphics.Rect magnifiedRegionBounds = this.mTempRect;
                    display.getMagnifiedFrameInContentCoordsLocked(magnifiedRegionBounds);
                    if (magnifiedRegionBounds.contains(left, top, right, bottom)) {
                        return;
                    }
                    display.onRectangleOnScreenRequested(left, top, right, bottom);
                }
            }
        }
    }

    void setMagnificationFollowTypingEnabled(boolean enabled) {
        this.mMagnificationFollowTypingEnabled = enabled;
    }

    boolean isMagnificationFollowTypingEnabled() {
        return this.mMagnificationFollowTypingEnabled;
    }

    void setAlwaysOnMagnificationEnabled(boolean enabled) {
        this.mAlwaysOnMagnificationEnabled = enabled;
    }

    boolean isAlwaysOnMagnificationEnabled() {
        return this.mAlwaysOnMagnificationEnabled;
    }

    void onUserContextChanged(int displayId) {
        synchronized (this.mLock) {
            if (isActivated(displayId)) {
                if (isAlwaysOnMagnificationEnabled()) {
                    zoomOutFromService(displayId);
                } else {
                    reset(displayId, true);
                }
            }
        }
    }

    public void onDisplayRemoved(int displayId) {
        synchronized (this.mLock) {
            unregisterLocked(displayId, true);
        }
    }

    public boolean isRegistered(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.isRegistered();
        }
    }

    public boolean isActivated(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.isActivated();
        }
    }

    public boolean magnificationRegionContains(int displayId, float x, float y) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.magnificationRegionContains(x, y);
        }
    }

    public void getMagnificationBounds(int displayId, android.graphics.Rect outBounds) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return;
            }
            display.getMagnificationBounds(outBounds);
        }
    }

    public void getMagnificationRegion(int displayId, android.graphics.Region outRegion) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return;
            }
            display.getMagnificationRegion(outRegion);
        }
    }

    public float getScale(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return 1.0f;
            }
            return display.getScale();
        }
    }

    protected float getLastActivatedScale(int displayId) {
        return getScale(displayId);
    }

    public float getOffsetX(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return 0.0f;
            }
            return display.getOffsetX();
        }
    }

    public float getCenterX(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return 0.0f;
            }
            return display.getCenterX();
        }
    }

    public boolean isAtEdge(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.isAtEdge();
        }
    }

    public boolean isAtLeftEdge(int displayId, float slop) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.isAtLeftEdge(slop);
        }
    }

    public boolean isAtRightEdge(int displayId, float slop) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.isAtRightEdge(slop);
        }
    }

    public boolean isAtTopEdge(int displayId, float slop) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.isAtTopEdge(slop);
        }
    }

    public boolean isAtBottomEdge(int displayId, float slop) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.isAtBottomEdge(slop);
        }
    }

    public float getOffsetY(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return 0.0f;
            }
            return display.getOffsetY();
        }
    }

    public float getCenterY(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return 0.0f;
            }
            return display.getCenterY();
        }
    }

    public boolean reset(int displayId, boolean animate) {
        return reset(displayId, animate ? android.view.accessibility.MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK : null);
    }

    public boolean reset(int displayId, android.view.accessibility.MagnificationAnimationCallback animationCallback) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.reset(animationCallback);
        }
    }

    public boolean setScale(int displayId, float scale, float pivotX, float pivotY, boolean animate, int id) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.setScale(scale, pivotX, pivotY, animate, id);
        }
    }

    public boolean setCenter(int displayId, float centerX, float centerY, boolean animate, int id) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.setScaleAndCenter(Float.NaN, centerX, centerY, animate ? android.view.accessibility.MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK : null, id);
        }
    }

    public boolean setScaleAndCenter(int displayId, float scale, float centerX, float centerY, boolean animate, int id) {
        return setScaleAndCenter(displayId, scale, centerX, centerY, transformToStubCallback(animate), id);
    }

    public boolean setScaleAndCenter(int displayId, float scale, float centerX, float centerY, android.view.accessibility.MagnificationAnimationCallback animationCallback, int id) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return false;
            }
            return display.setScaleAndCenter(scale, centerX, centerY, animationCallback, id);
        }
    }

    public void offsetMagnifiedRegion(int displayId, float offsetX, float offsetY, int id) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return;
            }
            display.offsetMagnifiedRegion(offsetX, offsetY, id);
        }
    }

    public void startFling(int displayId, float xPixelsPerSecond, float yPixelsPerSecond, int id) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return;
            }
            display.startFling(xPixelsPerSecond, yPixelsPerSecond, id);
        }
    }

    public void cancelFling(int displayId, int id) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return;
            }
            display.cancelFling(id);
        }
    }

    public int getIdOfLastServiceToMagnify(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display == null) {
                return -1;
            }
            return display.getIdOfLastServiceToMagnify();
        }
    }

    public void persistScale(int displayId) {
        float scale = getScale(0);
        if (scale < 1.3f) {
            return;
        }
        this.mScaleProvider.putScale(scale, displayId);
    }

    public float getPersistedScale(int displayId) {
        return android.util.MathUtils.constrain(this.mScaleProvider.getScale(displayId), 1.3f, com.android.server.accessibility.magnification.MagnificationScaleProvider.MAX_SCALE);
    }

    private void zoomOutFromService(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display != null && display.isActivated()) {
                display.zoomOutFromService();
            }
        }
    }

    public boolean isZoomedOutFromService(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display != null && display.isActivated()) {
                return display.isZoomedOutFromService();
            }
            return false;
        }
    }

    public void resetAllIfNeeded(int connectionId) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mDisplays.size(); i++) {
                resetIfNeeded(this.mDisplays.keyAt(i), connectionId);
            }
        }
    }

    boolean resetIfNeeded(int displayId, boolean animate) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display != null && display.isActivated()) {
                display.reset(animate);
                return true;
            }
            return false;
        }
    }

    boolean resetIfNeeded(int displayId, int connectionId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
            if (display != null && display.isActivated() && connectionId == display.getIdOfLastServiceToMagnify()) {
                display.reset(true);
                return true;
            }
            return false;
        }
    }

    void notifyImeWindowVisibilityChanged(final int displayId, final boolean shown) {
        synchronized (this.mLock) {
            this.mMagnificationInfoChangedCallbacks.forEach(new java.util.function.Consumer() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback) obj).onImeWindowVisibilityChanged(displayId, shown);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onScreenTurnedOff() {
        android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.accessibility.magnification.FullScreenMagnificationController) obj).resetAllIfNeeded(((java.lang.Boolean) obj2).booleanValue());
            }
        }, this, false);
        this.mControllerCtx.getHandler().sendMessage(m);
    }

    void resetAllIfNeeded(boolean animate) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mDisplays.size(); i++) {
                resetIfNeeded(this.mDisplays.keyAt(i), animate);
            }
        }
    }

    private void unregisterLocked(int displayId, boolean delete) {
        com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.get(displayId);
        if (display == null) {
            return;
        }
        if (!display.isRegistered()) {
            if (delete) {
                this.mDisplays.remove(displayId);
            }
        } else if (!display.isActivated()) {
            display.unregister(delete);
        } else {
            display.unregisterPending(delete);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterCallbackLocked(int displayId, boolean delete) {
        if (delete) {
            this.mDisplays.remove(displayId);
        }
        boolean hasRegister = false;
        for (int i = 0; i < this.mDisplays.size(); i++) {
            com.android.server.accessibility.magnification.FullScreenMagnificationController.DisplayMagnification display = this.mDisplays.valueAt(i);
            hasRegister = display.isRegistered();
            if (hasRegister) {
                break;
            }
        }
        if (!hasRegister) {
            this.mScreenStateObserver.unregister();
        }
    }

    void addInfoChangedCallback(com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback callback) {
        synchronized (this.mLock) {
            this.mMagnificationInfoChangedCallbacks.add(callback);
        }
    }

    void removeInfoChangedCallback(com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback callback) {
        synchronized (this.mLock) {
            this.mMagnificationInfoChangedCallbacks.remove(callback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean traceEnabled() {
        return this.mControllerCtx.getTraceManager().isA11yTracingEnabledForTypes(512L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logTrace(java.lang.String methodName, java.lang.String params) {
        this.mControllerCtx.getTraceManager().logTrace("WindowManagerInternal." + methodName, 512L, params);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("MagnificationController[");
        builder.append(", mDisplays=").append(this.mDisplays);
        builder.append(", mScaleProvider=").append(this.mScaleProvider);
        builder.append("]");
        return builder.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class SpecAnimationBridge implements android.animation.ValueAnimator.AnimatorUpdateListener, android.animation.Animator.AnimatorListener {
        private android.view.accessibility.MagnificationAnimationCallback mAnimationCallback;
        private final com.android.server.accessibility.magnification.FullScreenMagnificationController.ControllerContext mControllerCtx;
        private final int mDisplayId;
        private boolean mEnabled;
        private final android.view.MagnificationSpec mEndMagnificationSpec;
        private final java.lang.Object mLock;
        private final android.animation.TimeAnimator mScrollAnimator;
        private final android.widget.Scroller mScroller;
        private final android.view.MagnificationSpec mSentMagnificationSpec;
        private final android.view.MagnificationSpec mStartMagnificationSpec;
        private final android.animation.ValueAnimator mValueAnimator;

        private SpecAnimationBridge(com.android.server.accessibility.magnification.FullScreenMagnificationController.ControllerContext ctx, java.lang.Object lock, int displayId, java.util.function.Supplier<android.widget.Scroller> scrollerSupplier, java.util.function.Supplier<android.animation.TimeAnimator> timeAnimatorSupplier) {
            this.mSentMagnificationSpec = new android.view.MagnificationSpec();
            this.mStartMagnificationSpec = new android.view.MagnificationSpec();
            this.mEndMagnificationSpec = new android.view.MagnificationSpec();
            this.mEnabled = false;
            this.mControllerCtx = ctx;
            this.mLock = lock;
            this.mDisplayId = displayId;
            long animationDuration = this.mControllerCtx.getAnimationDuration();
            this.mValueAnimator = this.mControllerCtx.newValueAnimator();
            this.mValueAnimator.setDuration(animationDuration);
            this.mValueAnimator.setInterpolator(new android.view.animation.DecelerateInterpolator(2.5f));
            this.mValueAnimator.setFloatValues(0.0f, 1.0f);
            this.mValueAnimator.addUpdateListener(this);
            this.mValueAnimator.addListener(this);
            if (com.android.server.accessibility.Flags.fullscreenFlingGesture()) {
                this.mScroller = scrollerSupplier.get();
                this.mScrollAnimator = timeAnimatorSupplier.get();
                this.mScrollAnimator.addListener(this);
                this.mScrollAnimator.setTimeListener(new android.animation.TimeAnimator.TimeListener() { // from class: com.android.server.accessibility.magnification.FullScreenMagnificationController$SpecAnimationBridge$$ExternalSyntheticLambda0
                    @Override // android.animation.TimeAnimator.TimeListener
                    public final void onTimeUpdate(android.animation.TimeAnimator timeAnimator, long j, long j2) {
                        this.f$0.lambda$new$0(timeAnimator, j, j2);
                    }
                });
                return;
            }
            this.mScroller = null;
            this.mScrollAnimator = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(android.animation.TimeAnimator animation, long totalTime, long deltaTime) {
            synchronized (this.mLock) {
                if (this.mEnabled) {
                    if (!this.mScroller.computeScrollOffset()) {
                        animation.end();
                        return;
                    }
                    this.mEndMagnificationSpec.offsetX = this.mScroller.getCurrX();
                    this.mEndMagnificationSpec.offsetY = this.mScroller.getCurrY();
                    setMagnificationSpecLocked(this.mEndMagnificationSpec);
                }
            }
        }

        public void setEnabled(boolean enabled) {
            synchronized (this.mLock) {
                if (enabled != this.mEnabled) {
                    this.mEnabled = enabled;
                    if (!this.mEnabled) {
                        this.mSentMagnificationSpec.clear();
                        if (this.mControllerCtx.getTraceManager().isA11yTracingEnabledForTypes(512L)) {
                            this.mControllerCtx.getTraceManager().logTrace("WindowManagerInternal.setMagnificationSpec", 512L, "displayID=" + this.mDisplayId + ";spec=" + this.mSentMagnificationSpec);
                        }
                        this.mControllerCtx.getWindowManager().setMagnificationSpec(this.mDisplayId, this.mSentMagnificationSpec);
                    }
                }
            }
        }

        void updateSentSpecMainThread(android.view.MagnificationSpec spec, android.view.accessibility.MagnificationAnimationCallback animationCallback) {
            cancelAnimations();
            this.mAnimationCallback = animationCallback;
            synchronized (this.mLock) {
                boolean changed = !this.mSentMagnificationSpec.equals(spec);
                if (changed) {
                    if (this.mAnimationCallback != null) {
                        animateMagnificationSpecLocked(spec);
                    } else {
                        setMagnificationSpecLocked(spec);
                    }
                } else {
                    sendEndCallbackMainThread(true);
                }
            }
        }

        private void sendEndCallbackMainThread(boolean success) {
            if (this.mAnimationCallback != null) {
                this.mAnimationCallback.onResult(success, this.mSentMagnificationSpec);
                this.mAnimationCallback = null;
            }
        }

        private void setMagnificationSpecLocked(android.view.MagnificationSpec spec) {
            if (this.mEnabled) {
                this.mSentMagnificationSpec.setTo(spec);
                if (this.mControllerCtx.getTraceManager().isA11yTracingEnabledForTypes(512L)) {
                    this.mControllerCtx.getTraceManager().logTrace("WindowManagerInternal.setMagnificationSpec", 512L, "displayID=" + this.mDisplayId + ";spec=" + this.mSentMagnificationSpec);
                }
                this.mControllerCtx.getWindowManager().setMagnificationSpec(this.mDisplayId, this.mSentMagnificationSpec);
            }
        }

        private void animateMagnificationSpecLocked(android.view.MagnificationSpec toSpec) {
            this.mEndMagnificationSpec.setTo(toSpec);
            this.mStartMagnificationSpec.setTo(this.mSentMagnificationSpec);
            this.mValueAnimator.start();
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(android.animation.ValueAnimator animation) {
            synchronized (this.mLock) {
                if (this.mEnabled) {
                    float fract = animation.getAnimatedFraction();
                    android.view.MagnificationSpec magnificationSpec = new android.view.MagnificationSpec();
                    magnificationSpec.scale = this.mStartMagnificationSpec.scale + ((this.mEndMagnificationSpec.scale - this.mStartMagnificationSpec.scale) * fract);
                    magnificationSpec.offsetX = this.mStartMagnificationSpec.offsetX + ((this.mEndMagnificationSpec.offsetX - this.mStartMagnificationSpec.offsetX) * fract);
                    magnificationSpec.offsetY = this.mStartMagnificationSpec.offsetY + ((this.mEndMagnificationSpec.offsetY - this.mStartMagnificationSpec.offsetY) * fract);
                    setMagnificationSpecLocked(magnificationSpec);
                }
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(android.animation.Animator animation) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(android.animation.Animator animation) {
            sendEndCallbackMainThread(true);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(android.animation.Animator animation) {
            sendEndCallbackMainThread(false);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(android.animation.Animator animation) {
        }

        public void startFlingAnimation(float xPixelsPerSecond, float yPixelsPerSecond, float minX, float maxX, float minY, float maxY, android.view.accessibility.MagnificationAnimationCallback animationCallback) {
            if (!com.android.server.accessibility.Flags.fullscreenFlingGesture()) {
                return;
            }
            cancelAnimations();
            this.mAnimationCallback = animationCallback;
            this.mEndMagnificationSpec.setTo(this.mSentMagnificationSpec);
            this.mScroller.fling((int) this.mSentMagnificationSpec.offsetX, (int) this.mSentMagnificationSpec.offsetY, (int) xPixelsPerSecond, (int) yPixelsPerSecond, (int) minX, (int) maxX, (int) minY, (int) maxY);
            this.mScrollAnimator.start();
        }

        void cancelAnimations() {
            if (this.mValueAnimator.isRunning()) {
                this.mValueAnimator.cancel();
            }
            cancelFlingAnimation();
        }

        void cancelFlingAnimation() {
            if (!com.android.server.accessibility.Flags.fullscreenFlingGesture()) {
                return;
            }
            if (this.mScrollAnimator.isRunning()) {
                this.mScrollAnimator.cancel();
            }
            this.mScroller.forceFinished(true);
        }
    }

    private static class ScreenStateObserver extends android.content.BroadcastReceiver {
        private final android.content.Context mContext;
        private final com.android.server.accessibility.magnification.FullScreenMagnificationController mController;
        private boolean mRegistered = false;

        ScreenStateObserver(android.content.Context context, com.android.server.accessibility.magnification.FullScreenMagnificationController controller) {
            this.mContext = context;
            this.mController = controller;
        }

        public void registerIfNecessary() {
            if (!this.mRegistered) {
                this.mContext.registerReceiver(this, new android.content.IntentFilter("android.intent.action.SCREEN_OFF"));
                this.mRegistered = true;
            }
        }

        public void unregister() {
            if (this.mRegistered) {
                this.mContext.unregisterReceiver(this);
                this.mRegistered = false;
            }
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            this.mController.onScreenTurnedOff();
        }
    }

    public static class ControllerContext {
        private final java.lang.Long mAnimationDuration;
        private final android.content.Context mContext;
        private final android.os.Handler mHandler;
        private final com.android.server.accessibility.AccessibilityTraceManager mTrace;
        private final com.android.server.wm.WindowManagerInternal mWindowManager;

        public ControllerContext(android.content.Context context, com.android.server.accessibility.AccessibilityTraceManager traceManager, com.android.server.wm.WindowManagerInternal windowManager, android.os.Handler handler, long animationDuration) {
            this.mContext = context;
            this.mTrace = traceManager;
            this.mWindowManager = windowManager;
            this.mHandler = handler;
            this.mAnimationDuration = java.lang.Long.valueOf(animationDuration);
        }

        public android.content.Context getContext() {
            return this.mContext;
        }

        public com.android.server.accessibility.AccessibilityTraceManager getTraceManager() {
            return this.mTrace;
        }

        public com.android.server.wm.WindowManagerInternal getWindowManager() {
            return this.mWindowManager;
        }

        public android.os.Handler getHandler() {
            return this.mHandler;
        }

        public android.animation.ValueAnimator newValueAnimator() {
            return new android.animation.ValueAnimator();
        }

        public long getAnimationDuration() {
            return this.mAnimationDuration.longValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.view.accessibility.MagnificationAnimationCallback transformToStubCallback(boolean animate) {
        if (animate) {
            return android.view.accessibility.MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK;
        }
        return null;
    }
}
