package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class SurfaceFreezer {
    private static final java.lang.String TAG = "SurfaceFreezer";
    private final com.android.server.wm.SurfaceFreezer.Freezable mAnimatable;
    android.view.SurfaceControl mLeash;
    private final com.android.server.wm.WindowManagerService mWmService;
    com.android.server.wm.SurfaceFreezer.Snapshot mSnapshot = null;
    final android.graphics.Rect mFreezeBounds = new android.graphics.Rect();
    com.android.server.wm.ISurfaceFreezerExt mSurfaceFreezerExt = (com.android.server.wm.ISurfaceFreezerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ISurfaceFreezerExt.class).base(this).create();

    public interface Freezable extends com.android.server.wm.SurfaceAnimator.Animatable {
        android.view.SurfaceControl getFreezeSnapshotTarget();

        void onUnfrozen();
    }

    SurfaceFreezer(com.android.server.wm.SurfaceFreezer.Freezable animatable, com.android.server.wm.WindowManagerService service) {
        this.mAnimatable = animatable;
        this.mWmService = service;
    }

    void freeze(android.view.SurfaceControl.Transaction t, android.graphics.Rect startBounds, android.graphics.Point relativePosition, android.view.SurfaceControl freezeTarget) {
        reset(t);
        this.mFreezeBounds.set(startBounds);
        this.mLeash = com.android.server.wm.SurfaceAnimator.createAnimationLeash(this.mAnimatable, this.mAnimatable.getSurfaceControl(), t, 2, startBounds.width(), startBounds.height(), relativePosition.x, relativePosition.y, false, this.mWmService.mTransactionFactory);
        this.mAnimatable.onAnimationLeashCreated(t, this.mLeash);
        android.view.SurfaceControl freezeTarget2 = freezeTarget != null ? freezeTarget : this.mAnimatable.getFreezeSnapshotTarget();
        if (freezeTarget2 != null) {
            android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer = this.mSurfaceFreezerExt.createFlexibleTaskSnapshotBuffer(freezeTarget2);
            if (screenshotBuffer == null) {
                screenshotBuffer = createSnapshotBufferInner(freezeTarget2, startBounds);
            }
            android.hardware.HardwareBuffer buffer = screenshotBuffer == null ? null : screenshotBuffer.getHardwareBuffer();
            if (buffer == null || buffer.getWidth() <= 1 || buffer.getHeight() <= 1) {
                android.util.Slog.w(TAG, "Failed to capture screenshot for " + this.mAnimatable);
                unfreeze(t);
            } else {
                this.mSnapshot = new com.android.server.wm.SurfaceFreezer.Snapshot(t, screenshotBuffer, this.mLeash);
            }
        }
    }

    android.view.SurfaceControl takeLeashForAnimation() {
        android.view.SurfaceControl out = this.mLeash;
        this.mLeash = null;
        return out;
    }

    com.android.server.wm.SurfaceFreezer.Snapshot takeSnapshotForAnimation() {
        com.android.server.wm.SurfaceFreezer.Snapshot out = this.mSnapshot;
        this.mSnapshot = null;
        return out;
    }

    void unfreeze(android.view.SurfaceControl.Transaction t) {
        unfreezeInner(t);
        this.mAnimatable.onUnfrozen();
        this.mSurfaceFreezerExt.resetFlexibleTaskInfo();
    }

    private void unfreezeInner(android.view.SurfaceControl.Transaction t) {
        if (this.mSnapshot != null) {
            this.mSnapshot.cancelAnimation(t, false);
            this.mSnapshot = null;
        }
        if (this.mLeash == null) {
            return;
        }
        android.view.SurfaceControl leash = this.mLeash;
        this.mLeash = null;
        boolean scheduleAnim = com.android.server.wm.SurfaceAnimator.removeLeash(t, this.mAnimatable, leash, true);
        if (scheduleAnim) {
            this.mWmService.scheduleAnimationLocked();
        }
    }

    private void reset(android.view.SurfaceControl.Transaction t) {
        if (this.mSnapshot != null) {
            this.mSnapshot.destroy(t);
            this.mSnapshot = null;
        }
        if (this.mLeash != null) {
            t.remove(this.mLeash);
            this.mLeash = null;
        }
    }

    void setLayer(android.view.SurfaceControl.Transaction t, int layer) {
        if (this.mLeash != null) {
            t.setLayer(this.mLeash, layer);
        }
    }

    void setRelativeLayer(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl relativeTo, int layer) {
        if (this.mLeash != null) {
            t.setRelativeLayer(this.mLeash, relativeTo, layer);
        }
    }

    boolean hasLeash() {
        return this.mLeash != null;
    }

    private static android.window.ScreenCapture.ScreenshotHardwareBuffer createSnapshotBuffer(android.view.SurfaceControl target, android.graphics.Rect bounds) {
        android.graphics.Rect cropBounds = null;
        if (bounds != null) {
            cropBounds = new android.graphics.Rect(bounds);
            cropBounds.offsetTo(0, 0);
        }
        android.window.ScreenCapture.LayerCaptureArgs captureArgs = new android.window.ScreenCapture.LayerCaptureArgs.Builder(target).setSourceCrop(cropBounds).setCaptureSecureLayers(true).setAllowProtected(true).build();
        return android.window.ScreenCapture.captureLayers(captureArgs);
    }

    android.window.ScreenCapture.ScreenshotHardwareBuffer createSnapshotBufferInner(android.view.SurfaceControl target, android.graphics.Rect bounds) {
        return createSnapshotBuffer(target, bounds);
    }

    android.graphics.GraphicBuffer createFromHardwareBufferInner(android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer) {
        return android.graphics.GraphicBuffer.createFromHardwareBuffer(screenshotBuffer.getHardwareBuffer());
    }

    class Snapshot {
        private com.android.server.wm.AnimationAdapter mAnimation;
        private android.view.SurfaceControl mSurfaceControl;

        Snapshot(android.view.SurfaceControl.Transaction t, android.window.ScreenCapture.ScreenshotHardwareBuffer screenshotBuffer, android.view.SurfaceControl parent) {
            android.graphics.GraphicBuffer graphicBuffer = com.android.server.wm.SurfaceFreezer.this.createFromHardwareBufferInner(screenshotBuffer);
            this.mSurfaceControl = com.android.server.wm.SurfaceFreezer.this.mAnimatable.makeAnimationLeash().setName("snapshot anim: " + com.android.server.wm.SurfaceFreezer.this.mAnimatable.toString()).setFormat(-3).setParent(parent).setSecure(screenshotBuffer.containsSecureLayers()).setCallsite("SurfaceFreezer.Snapshot").setBLASTLayer().build();
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mSurfaceControl);
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, -2595923278763115975L, 0, null, protoLogParam0);
            }
            t.setBuffer(this.mSurfaceControl, graphicBuffer);
            t.setColorSpace(this.mSurfaceControl, screenshotBuffer.getColorSpace());
            t.show(this.mSurfaceControl);
            t.setLayer(this.mSurfaceControl, Integer.MAX_VALUE);
        }

        void destroy(android.view.SurfaceControl.Transaction t) {
            if (this.mSurfaceControl == null) {
                return;
            }
            t.remove(this.mSurfaceControl);
            this.mSurfaceControl = null;
        }

        void startAnimation(android.view.SurfaceControl.Transaction t, com.android.server.wm.AnimationAdapter anim, int type) {
            cancelAnimation(t, true);
            this.mAnimation = anim;
            if (this.mSurfaceControl == null) {
                cancelAnimation(t, false);
            } else {
                this.mAnimation.startAnimation(this.mSurfaceControl, t, type, new com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.SurfaceFreezer$Snapshot$$ExternalSyntheticLambda0
                    @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
                    public final void onAnimationFinished(int i, com.android.server.wm.AnimationAdapter animationAdapter) {
                        com.android.server.wm.SurfaceFreezer.Snapshot.lambda$startAnimation$0(i, animationAdapter);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$startAnimation$0(int typ, com.android.server.wm.AnimationAdapter ani) {
        }

        void cancelAnimation(android.view.SurfaceControl.Transaction t, boolean restarting) {
            android.view.SurfaceControl leash = this.mSurfaceControl;
            com.android.server.wm.AnimationAdapter animation = this.mAnimation;
            this.mAnimation = null;
            if (animation != null) {
                animation.onAnimationCancelled(leash);
            }
            if (!restarting) {
                destroy(t);
            }
        }
    }
}
