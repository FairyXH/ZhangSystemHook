package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class SurfaceAnimator {
    public static final int ANIMATION_TYPE_ALL = -1;
    public static final int ANIMATION_TYPE_APP_TRANSITION = 1;
    public static final int ANIMATION_TYPE_DIMMER = 4;
    static final int ANIMATION_TYPE_GESTURE = 256;
    public static final int ANIMATION_TYPE_INSETS_CONTROL = 32;
    public static final int ANIMATION_TYPE_NONE = 0;
    public static final int ANIMATION_TYPE_PREDICT_BACK = 256;
    public static final int ANIMATION_TYPE_RECENTS = 8;
    public static final int ANIMATION_TYPE_SCREEN_ROTATION = 2;
    public static final int ANIMATION_TYPE_STARTING_REVEAL = 128;
    public static final int ANIMATION_TYPE_TOKEN_TRANSFORM = 64;
    public static final int ANIMATION_TYPE_WINDOW_ANIMATION = 16;
    private static final java.lang.String TAG = "WindowManager";
    private static com.android.server.wm.ISurfaceAnimatorExt.IStaticExt mStaticExt = (com.android.server.wm.ISurfaceAnimatorExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ISurfaceAnimatorExt.IStaticExt.class).create();
    final com.android.server.wm.SurfaceAnimator.Animatable mAnimatable;
    private com.android.server.wm.AnimationAdapter mAnimation;
    private java.lang.Runnable mAnimationCancelledCallback;
    private boolean mAnimationFinished;
    private boolean mAnimationStartDelayed;
    private int mAnimationType;
    final com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback mInnerAnimationFinishedCallback;
    android.view.SurfaceControl mLeash;
    private final com.android.server.wm.WindowManagerService mService;
    com.android.server.wm.SurfaceFreezer.Snapshot mSnapshot;
    final com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback mStaticAnimationFinishedCallback;
    private com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback mSurfaceAnimationFinishedCallback;
    private com.android.server.wm.SurfaceAnimator.SurfaceAnimatorWrapper mSAWrapper = new com.android.server.wm.SurfaceAnimator.SurfaceAnimatorWrapper();
    private com.android.server.wm.ISurfaceAnimatorExt mSurfaceAnimatorExt = (com.android.server.wm.ISurfaceAnimatorExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ISurfaceAnimatorExt.class).base(this).create();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface AnimationType {
    }

    public interface OnAnimationFinishedCallback {
        void onAnimationFinished(int i, com.android.server.wm.AnimationAdapter animationAdapter);
    }

    SurfaceAnimator(com.android.server.wm.SurfaceAnimator.Animatable animatable, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback staticAnimationFinishedCallback, com.android.server.wm.WindowManagerService service) {
        this.mAnimatable = animatable;
        this.mService = service;
        this.mStaticAnimationFinishedCallback = staticAnimationFinishedCallback;
        this.mInnerAnimationFinishedCallback = getFinishedCallback(staticAnimationFinishedCallback);
    }

    private com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback getFinishedCallback(final com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback staticAnimationFinishedCallback) {
        return new com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback() { // from class: com.android.server.wm.SurfaceAnimator$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback
            public final void onAnimationFinished(int i, com.android.server.wm.AnimationAdapter animationAdapter) {
                this.f$0.lambda$getFinishedCallback$1(staticAnimationFinishedCallback, i, animationAdapter);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getFinishedCallback$1(final com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback staticAnimationFinishedCallback, final int type, final com.android.server.wm.AnimationAdapter anim) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.SurfaceAnimator target = this.mService.mAnimationTransferMap.remove(anim);
                if (target != null) {
                    target.mInnerAnimationFinishedCallback.onAnimationFinished(type, anim);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } else {
                    if (anim != this.mAnimation) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    java.lang.Runnable resetAndInvokeFinish = new java.lang.Runnable() { // from class: com.android.server.wm.SurfaceAnimator$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$getFinishedCallback$0(anim, staticAnimationFinishedCallback, type);
                        }
                    };
                    if (!this.mAnimatable.shouldDeferAnimationFinish(resetAndInvokeFinish) && !anim.shouldDeferAnimationFinish(resetAndInvokeFinish)) {
                        resetAndInvokeFinish.run();
                    } else {
                        this.mSurfaceAnimatorExt.setDeferAnimationFinish(this.mAnimatable, true);
                    }
                    this.mAnimationFinished = true;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getFinishedCallback$0(com.android.server.wm.AnimationAdapter anim, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback staticAnimationFinishedCallback, int type) {
        if (anim != this.mAnimation) {
            return;
        }
        com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback animationFinishCallback = this.mSurfaceAnimationFinishedCallback;
        if (!this.mSurfaceAnimatorExt.hookResetForTask(this, false)) {
            reset(this.mAnimatable.getSyncTransaction(), true);
        }
        if (staticAnimationFinishedCallback != null) {
            staticAnimationFinishedCallback.onAnimationFinished(type, anim);
        }
        if (animationFinishCallback != null) {
            animationFinishCallback.onAnimationFinished(type, anim);
        }
        this.mSurfaceAnimatorExt.setDeferAnimationFinish(this.mAnimatable, false);
        if (this.mSurfaceAnimatorExt.cancelAnimThreadUxIfNeed(this.mAnimatable, type)) {
            this.mSurfaceAnimatorExt.callOrmsSetSceneActionForRemoteAnimation(false, this.mLeash, this.mAnimatable.getPendingTransaction(), type);
        }
    }

    void startAnimation(android.view.SurfaceControl.Transaction t, com.android.server.wm.AnimationAdapter anim, boolean hidden, int type, com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback animationFinishedCallback, java.lang.Runnable animationCancelledCallback, com.android.server.wm.AnimationAdapter snapshotAnim, com.android.server.wm.SurfaceFreezer freezer) {
        java.lang.String str;
        boolean z;
        boolean useGesturePosition = false;
        android.graphics.Point gesturePosition = new android.graphics.Point(0, 0);
        this.mSurfaceAnimatorExt.setReuseLeash(this);
        cancelAnimation(t, true, true);
        this.mAnimation = anim;
        this.mAnimationType = type;
        this.mSurfaceAnimationFinishedCallback = animationFinishedCallback;
        this.mAnimationCancelledCallback = animationCancelledCallback;
        android.view.SurfaceControl surface = this.mAnimatable.getSurfaceControl();
        if (surface != null && surface.isValid()) {
            if (!this.mSurfaceAnimatorExt.hookSetLeash(this.mAnimatable, this.mLeash)) {
                this.mLeash = freezer != null ? freezer.takeLeashForAnimation() : null;
            }
            if (this.mLeash == null) {
                boolean useGesturePosition2 = this.mSurfaceAnimatorExt.useGesturePosition(this, gesturePosition, false);
                boolean useGesturePosition3 = com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM;
                if (useGesturePosition3) {
                    android.util.Slog.i(TAG, "useGesturePosition:" + useGesturePosition2 + " gesturePosition:" + gesturePosition);
                }
                com.android.server.wm.SurfaceAnimator.Animatable animatable = this.mAnimatable;
                int surfaceWidth = this.mAnimatable.getSurfaceWidth();
                int surfaceHeight = this.mAnimatable.getSurfaceHeight();
                int i = gesturePosition.x;
                int i2 = gesturePosition.y;
                java.util.function.Supplier<android.view.SurfaceControl.Transaction> supplier = this.mService.mTransactionFactory;
                str = TAG;
                z = true;
                this.mLeash = createAnimationLeash(animatable, surface, t, type, surfaceWidth, surfaceHeight, i, i2, hidden, supplier);
                this.mAnimatable.onAnimationLeashCreated(t, this.mLeash);
                this.mSAWrapper.getExtImpl().showTaskIfNeed(this.mAnimatable, t);
                this.mSAWrapper.getExtImpl().boostLeashLayerIfNeed(this.mAnimatable, type, t, this.mLeash);
                useGesturePosition = useGesturePosition2;
            } else {
                str = TAG;
                z = true;
            }
            this.mAnimatable.onLeashAnimationStarting(t, this.mLeash);
            if (!this.mAnimationStartDelayed) {
                this.mSurfaceAnimatorExt.callOrmsSetSceneActionForRemoteAnimation(z, this.mLeash, t, type);
                this.mAnimation.startAnimation(this.mLeash, t, type, this.mInnerAnimationFinishedCallback);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, com.android.internal.protolog.common.LogLevel.DEBUG)) {
                    java.io.StringWriter sw = new java.io.StringWriter();
                    java.io.PrintWriter pw = new java.io.PrintWriter(sw);
                    this.mAnimation.dump(pw, "");
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[0]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mAnimatable);
                        java.lang.String protoLogParam1 = java.lang.String.valueOf(sw);
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, 1371702561758591499L, 0, null, protoLogParam0, protoLogParam1);
                    }
                }
                if (snapshotAnim != null) {
                    this.mSnapshot = freezer.takeSnapshotForAnimation();
                    if (this.mSnapshot == null) {
                        android.util.Slog.e(str, "No snapshot target to start animation on for " + this.mAnimatable);
                        return;
                    }
                    this.mSnapshot.startAnimation(t, snapshotAnim, type);
                }
                if (useGesturePosition) {
                    t.setPosition(this.mLeash, gesturePosition.x, gesturePosition.y);
                    return;
                }
                return;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[2]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(this.mAnimatable);
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -820649637734629482L, 0, null, protoLogParam02);
                return;
            }
            return;
        }
        java.lang.String str2 = TAG;
        android.util.Slog.w(str2, "Unable to start animation, surface is null or no children.");
        cancelAnimation();
    }

    void startAnimation(android.view.SurfaceControl.Transaction t, com.android.server.wm.AnimationAdapter anim, boolean hidden, int type) {
        startAnimation(t, anim, hidden, type, null, null, null, null);
    }

    void startDelayingAnimationStart() {
        if (!isAnimating()) {
            this.mAnimationStartDelayed = true;
        }
    }

    void endDelayingAnimationStart() {
        boolean delayed = this.mAnimationStartDelayed;
        this.mAnimationStartDelayed = false;
        if (delayed && this.mAnimation != null) {
            this.mAnimation.startAnimation(this.mLeash, this.mAnimatable.getSyncTransaction(), this.mAnimationType, this.mInnerAnimationFinishedCallback);
            this.mAnimatable.commitPendingTransaction();
        }
    }

    boolean isAnimating() {
        return this.mAnimation != null;
    }

    int getAnimationType() {
        return this.mAnimationType;
    }

    com.android.server.wm.AnimationAdapter getAnimation() {
        return this.mAnimation;
    }

    void cancelAnimation() {
        cancelAnimation(this.mAnimatable.getSyncTransaction(), false, true);
        this.mAnimatable.commitPendingTransaction();
    }

    void setLayer(android.view.SurfaceControl.Transaction t, int layer) {
        if (this.mLeash != null && (this.mSurfaceAnimatorExt.isDragZoomToSplitLeash(this.mLeash) || this.mSurfaceAnimatorExt.isDragSplitToFullLeash(this.mLeash))) {
            android.util.Slog.d(TAG, "don't change layer when drag zoom to split:" + this.mLeash);
        } else {
            t.setLayer(this.mLeash != null ? this.mLeash : this.mAnimatable.getSurfaceControl(), layer);
        }
    }

    void setRelativeLayer(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl relativeTo, int layer) {
        t.setRelativeLayer(this.mLeash != null ? this.mLeash : this.mAnimatable.getSurfaceControl(), relativeTo, layer);
    }

    void reparent(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl newParent) {
        t.reparent(this.mLeash != null ? this.mLeash : this.mAnimatable.getSurfaceControl(), newParent);
    }

    boolean hasLeash() {
        return this.mLeash != null;
    }

    void transferAnimation(com.android.server.wm.SurfaceAnimator from) {
        if (from.mLeash == null) {
            return;
        }
        android.view.SurfaceControl surface = this.mAnimatable.getSurfaceControl();
        android.view.SurfaceControl parent = this.mAnimatable.getAnimationLeashParent();
        if (surface == null || parent == null) {
            android.util.Slog.w(TAG, "Unable to transfer animation, surface or parent is null");
            cancelAnimation();
            return;
        }
        if (from.mAnimationFinished) {
            android.util.Slog.w(TAG, "Unable to transfer animation, because " + from + " animation is finished");
            return;
        }
        endDelayingAnimationStart();
        android.view.SurfaceControl.Transaction t = this.mAnimatable.getSyncTransaction();
        cancelAnimation(t, true, true);
        this.mLeash = from.mLeash;
        this.mAnimation = from.mAnimation;
        this.mAnimationType = from.mAnimationType;
        this.mSurfaceAnimationFinishedCallback = from.mSurfaceAnimationFinishedCallback;
        this.mAnimationCancelledCallback = from.mAnimationCancelledCallback;
        from.cancelAnimation(t, false, false);
        if (this.mLeash == null) {
            return;
        }
        t.reparent(surface, this.mLeash);
        t.reparent(this.mLeash, parent);
        this.mAnimatable.onAnimationLeashCreated(t, this.mLeash);
        this.mService.mAnimationTransferMap.put(this.mAnimation, this);
    }

    boolean isAnimationStartDelayed() {
        return this.mAnimationStartDelayed;
    }

    private void cancelAnimation(android.view.SurfaceControl.Transaction t, boolean restarting, boolean forwardCancel) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[2]) {
            java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mAnimatable);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -5370506662233296228L, 3, null, java.lang.Boolean.valueOf(restarting), protoLogParam1);
        }
        android.view.SurfaceControl leash = this.mLeash;
        com.android.server.wm.AnimationAdapter animation = this.mAnimation;
        int animationType = this.mAnimationType;
        com.android.server.wm.SurfaceAnimator.OnAnimationFinishedCallback animationFinishedCallback = this.mSurfaceAnimationFinishedCallback;
        java.lang.Runnable animationCancelledCallback = this.mAnimationCancelledCallback;
        com.android.server.wm.SurfaceFreezer.Snapshot snapshot = this.mSnapshot;
        reset(t, false);
        if (animation != null) {
            if (!this.mAnimationStartDelayed && forwardCancel) {
                animation.onAnimationCancelled(leash);
                if (animationCancelledCallback != null) {
                    animationCancelledCallback.run();
                }
            }
            if (!restarting) {
                if (this.mStaticAnimationFinishedCallback != null) {
                    this.mStaticAnimationFinishedCallback.onAnimationFinished(animationType, animation);
                }
                if (animationFinishedCallback != null) {
                    animationFinishedCallback.onAnimationFinished(animationType, animation);
                }
            }
        }
        if (forwardCancel) {
            if (snapshot != null) {
                snapshot.cancelAnimation(t, false);
            }
            if (leash != null && !this.mSurfaceAnimatorExt.isReuseLeash() && leash.isValid()) {
                this.mLeash = null;
                t.remove(leash);
                this.mService.scheduleAnimationLocked();
            }
        }
        if (!restarting) {
            this.mAnimationStartDelayed = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset(android.view.SurfaceControl.Transaction t, boolean destroyLeash) {
        this.mService.mAnimationTransferMap.remove(this.mAnimation);
        this.mAnimation = null;
        this.mSurfaceAnimationFinishedCallback = null;
        this.mAnimationType = 0;
        com.android.server.wm.SurfaceFreezer.Snapshot snapshot = this.mSnapshot;
        this.mSnapshot = null;
        if (snapshot != null) {
            snapshot.cancelAnimation(t, !destroyLeash);
        }
        if (this.mLeash == null) {
            return;
        }
        android.view.SurfaceControl leash = this.mLeash;
        if (this.mSurfaceAnimatorExt.hookReset(this, t)) {
            return;
        }
        this.mLeash = null;
        boolean scheduleAnim = removeLeash(t, this.mAnimatable, leash, destroyLeash);
        this.mAnimationFinished = false;
        if (scheduleAnim) {
            this.mService.scheduleAnimationLocked();
            this.mSurfaceAnimatorExt.resetIfNeeded(this);
        }
    }

    static boolean removeLeash(android.view.SurfaceControl.Transaction t, com.android.server.wm.SurfaceAnimator.Animatable animatable, android.view.SurfaceControl leash, boolean destroy) {
        boolean scheduleAnim = false;
        android.view.SurfaceControl surface = animatable.getSurfaceControl();
        android.view.SurfaceControl parent = animatable.getParentSurfaceControl();
        android.view.SurfaceControl curAnimationLeash = animatable.getAnimationLeash();
        boolean reparent = surface != null && (curAnimationLeash == null || curAnimationLeash.equals(leash));
        if (animatable != null && (animatable instanceof com.android.server.wm.Task)) {
            com.android.server.wm.Task task = (com.android.server.wm.Task) animatable;
            if (((com.android.server.wm.IMirageWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IMirageWindowManagerExt.class).create()).shouldReparentToNull(task.mTaskId)) {
                reparent = false;
            }
            if (task.getWrapper().getExtImpl().isTaskEmbedded()) {
                ((com.android.server.wm.IFlexibleWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IFlexibleWindowManagerExt.class).create()).onFlexibleWindowTaskAppeared(task, task.getTaskInfo());
                reparent = false;
            }
        }
        if (reparent) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(parent);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(animatable);
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -3045933321063743917L, 0, null, protoLogParam0, protoLogParam1);
            }
            if (surface.isValid() && parent != null && parent.isValid()) {
                t.reparent(surface, parent);
                scheduleAnim = true;
            }
        }
        if (destroy) {
            t.remove(leash);
            scheduleAnim = true;
        }
        if (reparent) {
            animatable.onAnimationLeashLost(t);
            return true;
        }
        return scheduleAnim;
    }

    static android.view.SurfaceControl createAnimationLeash(com.android.server.wm.SurfaceAnimator.Animatable animatable, android.view.SurfaceControl surface, android.view.SurfaceControl.Transaction t, int type, int width, int height, int x, int y, boolean hidden, java.util.function.Supplier<android.view.SurfaceControl.Transaction> transactionFactory) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(animatable);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -855083149623806053L, 0, null, protoLogParam0);
        }
        android.view.SurfaceControl.Builder builder = animatable.makeAnimationLeash().setParent(animatable.getAnimationLeashParent()).setName(surface + " - animation-leash of " + animationTypeToString(type)).setHidden(hidden).setEffectLayer().setCallsite("SurfaceAnimator.createAnimationLeash");
        android.view.SurfaceControl leash = builder.build();
        mStaticExt.adjustAnimationLeashLayerIfNeeded(t, animatable, leash);
        t.setWindowCrop(leash, width, height);
        t.setPosition(leash, x, y);
        t.show(leash);
        t.setAlpha(leash, hidden ? 0.0f : 1.0f);
        t.reparent(surface, leash);
        return leash;
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        if (this.mAnimation != null) {
            this.mAnimation.dumpDebug(proto, 1146756268035L);
        }
        if (this.mLeash != null) {
            this.mLeash.dumpDebug(proto, 1146756268033L);
        }
        proto.write(1133871366146L, this.mAnimationStartDelayed);
        proto.end(token);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("mLeash=");
        pw.print(this.mLeash);
        pw.print(" mAnimationType=" + animationTypeToString(this.mAnimationType));
        pw.println(this.mAnimationStartDelayed ? " mAnimationStartDelayed=true" : "");
        pw.print(prefix);
        pw.print("Animation: ");
        pw.println(this.mAnimation);
        if (this.mAnimation != null) {
            this.mAnimation.dump(pw, prefix + "  ");
        }
    }

    static java.lang.String animationTypeToString(int type) {
        switch (type) {
            case 0:
                return "none";
            case 1:
                return "app_transition";
            case 2:
                return "screen_rotation";
            case 4:
                return "dimmer";
            case 8:
                return "recents_animation";
            case 16:
                return "window_animation";
            case 32:
                return "insets_animation";
            case 64:
                return "token_transform";
            case 128:
                return "starting_reveal";
            case 256:
                return "predict_back";
            default:
                return "unknown type:" + type;
        }
    }

    interface Animatable {
        void commitPendingTransaction();

        android.view.SurfaceControl getAnimationLeashParent();

        android.view.SurfaceControl getParentSurfaceControl();

        android.view.SurfaceControl.Transaction getPendingTransaction();

        android.view.SurfaceControl getSurfaceControl();

        int getSurfaceHeight();

        int getSurfaceWidth();

        android.view.SurfaceControl.Transaction getSyncTransaction();

        android.view.SurfaceControl.Builder makeAnimationLeash();

        void onAnimationLeashCreated(android.view.SurfaceControl.Transaction transaction, android.view.SurfaceControl surfaceControl);

        void onAnimationLeashLost(android.view.SurfaceControl.Transaction transaction);

        default void onLeashAnimationStarting(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl leash) {
        }

        default android.view.SurfaceControl getAnimationLeash() {
            return null;
        }

        default boolean shouldDeferAnimationFinish(java.lang.Runnable endDeferFinishCallback) {
            return false;
        }
    }

    public com.android.server.wm.ISurfaceAnimatorWrapper getWrapper() {
        return this.mSAWrapper;
    }

    private class SurfaceAnimatorWrapper implements com.android.server.wm.ISurfaceAnimatorWrapper {
        private SurfaceAnimatorWrapper() {
        }

        @Override // com.android.server.wm.ISurfaceAnimatorWrapper
        public com.android.server.wm.ISurfaceAnimatorExt getExtImpl() {
            return com.android.server.wm.SurfaceAnimator.this.mSurfaceAnimatorExt;
        }

        @Override // com.android.server.wm.ISurfaceAnimatorWrapper
        public void reset(android.view.SurfaceControl.Transaction t, boolean destroyLeash) {
            com.android.server.wm.SurfaceAnimator.this.reset(t, destroyLeash);
        }
    }
}
