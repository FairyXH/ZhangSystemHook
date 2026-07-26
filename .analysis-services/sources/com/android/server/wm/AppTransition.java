package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class AppTransition implements com.android.internal.util.DumpUtils.Dump {
    private static final int APP_STATE_IDLE = 0;
    private static final int APP_STATE_READY = 1;
    private static final int APP_STATE_RUNNING = 2;
    private static final int APP_STATE_TIMEOUT = 3;
    private static final long APP_TRANSITION_TIMEOUT_MS = 5000;
    static final int DEFAULT_APP_TRANSITION_DURATION = 336;
    static final int MAX_APP_TRANSITION_DURATION = 3000;
    private static final int NEXT_TRANSIT_TYPE_CLIP_REVEAL = 8;
    private static final int NEXT_TRANSIT_TYPE_CUSTOM = 1;
    private static final int NEXT_TRANSIT_TYPE_CUSTOM_IN_PLACE = 7;
    private static final int NEXT_TRANSIT_TYPE_NONE = 0;
    private static final int NEXT_TRANSIT_TYPE_OPEN_CROSS_PROFILE_APPS = 9;
    private static final int NEXT_TRANSIT_TYPE_REMOTE = 10;
    private static final int NEXT_TRANSIT_TYPE_SCALE_UP = 2;
    private static final int NEXT_TRANSIT_TYPE_THUMBNAIL_ASPECT_SCALE_DOWN = 6;
    private static final int NEXT_TRANSIT_TYPE_THUMBNAIL_ASPECT_SCALE_UP = 5;
    private static final int NEXT_TRANSIT_TYPE_THUMBNAIL_SCALE_DOWN = 4;
    private static final int NEXT_TRANSIT_TYPE_THUMBNAIL_SCALE_UP = 3;
    private static final java.lang.String TAG = "WindowManager";
    private static final java.util.ArrayList<android.util.Pair<java.lang.Integer, java.lang.String>> sFlagToString = new java.util.ArrayList<>();
    private android.os.IRemoteCallback mAnimationFinishedCallback;
    private final android.content.Context mContext;
    private android.view.AppTransitionAnimationSpec mDefaultNextAppTransitionAnimationSpec;
    private final int mDefaultWindowAnimationStyleResId;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    final android.os.Handler mHandler;
    private java.lang.String mLastChangingApp;
    private java.lang.String mLastClosingApp;
    private java.lang.String mLastOpeningApp;
    private android.view.IAppTransitionAnimationSpecsFuture mNextAppTransitionAnimationsSpecsFuture;
    private boolean mNextAppTransitionAnimationsSpecsPending;
    private int mNextAppTransitionBackgroundColor;
    private android.os.IRemoteCallback mNextAppTransitionCallback;
    private int mNextAppTransitionEnter;
    private int mNextAppTransitionExit;
    private android.os.IRemoteCallback mNextAppTransitionFutureCallback;
    private int mNextAppTransitionInPlace;
    private boolean mNextAppTransitionIsSync;
    private boolean mNextAppTransitionOverrideRequested;
    private java.lang.String mNextAppTransitionPackage;
    private boolean mNextAppTransitionScaleUp;
    private boolean mOverrideTaskTransition;
    private com.android.server.wm.RemoteAnimationController mRemoteAnimationController;
    private final com.android.server.wm.WindowManagerService mService;
    private android.os.ITheiaManagerExt mTheiaManagerExt;
    final com.android.internal.policy.TransitionAnimation mTransitionAnimation;
    private int mNextAppTransitionFlags = 0;
    private final java.util.ArrayList<java.lang.Integer> mNextAppTransitionRequests = new java.util.ArrayList<>();
    private int mLastUsedAppTransition = -1;
    private int mNextAppTransitionType = 0;
    private final android.util.SparseArray<android.view.AppTransitionAnimationSpec> mNextAppTransitionAnimationsSpecs = new android.util.SparseArray<>();
    private final android.graphics.Rect mTmpRect = new android.graphics.Rect();
    private int mAppTransitionState = 0;
    private final java.util.ArrayList<com.android.server.wm.WindowManagerInternal.AppTransitionListener> mListeners = new java.util.ArrayList<>();
    private final java.util.concurrent.ExecutorService mDefaultExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();
    final java.lang.Runnable mHandleAppTransitionTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.AppTransition$$ExternalSyntheticLambda2
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };
    private com.android.server.wm.AppTransition.AppTransitionWrapper mAppTransitionWrapper = new com.android.server.wm.AppTransition.AppTransitionWrapper();

    AppTransition(android.content.Context context, com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent) {
        this.mTheiaManagerExt = null;
        this.mContext = context;
        this.mService = service;
        this.mHandler = new android.os.Handler(service.mH.getLooper());
        this.mDisplayContent = displayContent;
        this.mTransitionAnimation = new com.android.internal.policy.TransitionAnimation(context, com.android.internal.protolog.ProtoLogImpl_209941506.isEnabled(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, com.android.internal.protolog.common.LogLevel.DEBUG), TAG);
        android.content.res.TypedArray windowStyle = this.mContext.getTheme().obtainStyledAttributes(com.android.internal.R.styleable.Window);
        this.mDefaultWindowAnimationStyleResId = windowStyle.getResourceId(8, 0);
        windowStyle.recycle();
        this.mTheiaManagerExt = (android.os.ITheiaManagerExt) system.ext.loader.core.ExtLoader.type(android.os.ITheiaManagerExt.class).base(context).create();
    }

    boolean isTransitionSet() {
        return !this.mNextAppTransitionRequests.isEmpty();
    }

    boolean isUnoccluding() {
        return this.mNextAppTransitionRequests.contains(9);
    }

    boolean transferFrom(com.android.server.wm.AppTransition other) {
        this.mNextAppTransitionRequests.addAll(other.mNextAppTransitionRequests);
        return prepare();
    }

    void setLastAppTransition(int transit, com.android.server.wm.ActivityRecord openingApp, com.android.server.wm.ActivityRecord closingApp, com.android.server.wm.ActivityRecord changingApp) {
        this.mLastUsedAppTransition = transit;
        this.mLastOpeningApp = "" + openingApp;
        this.mLastClosingApp = "" + closingApp;
        this.mLastChangingApp = "" + changingApp;
    }

    boolean isReady() {
        return this.mAppTransitionState == 1 || this.mAppTransitionState == 3;
    }

    void setReady() {
        setAppTransitionState(1);
        fetchAppTransitionSpecsFromFuture();
    }

    boolean isRunning() {
        return this.mAppTransitionState == 2;
    }

    void setIdle() {
        setAppTransitionState(0);
    }

    boolean isIdle() {
        return this.mAppTransitionState == 0;
    }

    boolean isTimeout() {
        return this.mAppTransitionState == 3;
    }

    void setTimeout() {
        setAppTransitionState(3);
    }

    android.view.animation.Animation getNextAppRequestedAnimation(boolean enter) {
        android.view.animation.Animation a = this.mTransitionAnimation.loadAppTransitionAnimation(this.mNextAppTransitionPackage, enter ? this.mNextAppTransitionEnter : this.mNextAppTransitionExit);
        if (this.mNextAppTransitionBackgroundColor != 0 && a != null) {
            a.setBackdropColor(this.mNextAppTransitionBackgroundColor);
        }
        return a;
    }

    int getNextAppTransitionBackgroundColor() {
        return this.mNextAppTransitionBackgroundColor;
    }

    boolean isNextAppTransitionOverrideRequested() {
        return this.mNextAppTransitionOverrideRequested;
    }

    android.hardware.HardwareBuffer getAppTransitionThumbnailHeader(com.android.server.wm.WindowContainer container) {
        android.view.AppTransitionAnimationSpec spec = this.mNextAppTransitionAnimationsSpecs.get(container.hashCode());
        if (spec == null) {
            spec = this.mDefaultNextAppTransitionAnimationSpec;
        }
        if (spec != null) {
            return spec.buffer;
        }
        return null;
    }

    boolean isNextThumbnailTransitionAspectScaled() {
        return this.mNextAppTransitionType == 5 || this.mNextAppTransitionType == 6;
    }

    boolean isNextThumbnailTransitionScaleUp() {
        return this.mNextAppTransitionScaleUp;
    }

    boolean isNextAppTransitionThumbnailUp() {
        return this.mNextAppTransitionType == 3 || this.mNextAppTransitionType == 5;
    }

    boolean isNextAppTransitionThumbnailDown() {
        return this.mNextAppTransitionType == 4 || this.mNextAppTransitionType == 6;
    }

    boolean isNextAppTransitionOpenCrossProfileApps() {
        return this.mNextAppTransitionType == 9;
    }

    boolean isFetchingAppTransitionsSpecs() {
        return this.mNextAppTransitionAnimationsSpecsPending;
    }

    private boolean prepare() {
        if (isRunning()) {
            return false;
        }
        setAppTransitionState(0);
        notifyAppTransitionPendingLocked();
        return true;
    }

    int goodToGo(int transit, com.android.server.wm.ActivityRecord topOpeningApp) {
        long jUptimeMillis;
        this.mAppTransitionWrapper.getExtImpl().hookgoodToGo(this.mDisplayContent, transit);
        this.mNextAppTransitionFlags = 0;
        this.mNextAppTransitionRequests.clear();
        setAppTransitionState(2);
        com.android.server.wm.WindowContainer wc = topOpeningApp != null ? topOpeningApp.getAnimatingContainer() : null;
        com.android.server.wm.AnimationAdapter topOpeningAnim = wc != null ? wc.getAnimation() : null;
        if (topOpeningAnim != null) {
            jUptimeMillis = topOpeningAnim.getStatusBarTransitionsStartTime();
        } else {
            jUptimeMillis = android.os.SystemClock.uptimeMillis();
        }
        int redoLayout = notifyAppTransitionStartingLocked(jUptimeMillis, 120L);
        if (this.mRemoteAnimationController != null) {
            this.mRemoteAnimationController.goodToGo(transit);
        } else if ((isTaskOpenTransitOld(transit) || transit == 12) && topOpeningAnim != null && this.mDisplayContent.getDisplayPolicy().shouldAttachNavBarToAppDuringTransition() && this.mService.getRecentsAnimationController() == null) {
            com.android.server.wm.NavBarFadeAnimationController controller = new com.android.server.wm.NavBarFadeAnimationController(this.mDisplayContent);
            controller.fadeOutAndInSequentially(topOpeningAnim.getDurationHint(), null, topOpeningApp.getSurfaceControl());
        }
        return redoLayout;
    }

    void clear() {
        clear(true);
    }

    private void clear(boolean clearAppOverride) {
        this.mNextAppTransitionType = 0;
        this.mNextAppTransitionOverrideRequested = false;
        this.mNextAppTransitionAnimationsSpecs.clear();
        this.mRemoteAnimationController = null;
        this.mNextAppTransitionAnimationsSpecsFuture = null;
        this.mDefaultNextAppTransitionAnimationSpec = null;
        this.mAnimationFinishedCallback = null;
        this.mOverrideTaskTransition = false;
        this.mNextAppTransitionIsSync = false;
        if (clearAppOverride) {
            this.mNextAppTransitionPackage = null;
            this.mNextAppTransitionEnter = 0;
            this.mNextAppTransitionExit = 0;
            this.mNextAppTransitionBackgroundColor = 0;
            this.mAppTransitionWrapper.getExtImpl().clearOverrideTransitionForResumed();
        }
    }

    void freeze() {
        boolean keyguardGoingAwayCancelled = this.mNextAppTransitionRequests.contains(7);
        if (this.mRemoteAnimationController != null) {
            this.mRemoteAnimationController.cancelAnimation("freeze");
        }
        if (this.mRemoteAnimationController == null && isUnoccluding()) {
            android.util.Slog.d(TAG, "freeze mRemoteAnimationController is null!!! " + toString() + ",call=" + android.os.Debug.getCallers(3));
        }
        this.mNextAppTransitionRequests.clear();
        clear();
        setReady();
        notifyAppTransitionCancelledLocked(keyguardGoingAwayCancelled);
    }

    private void setAppTransitionState(int state) {
        this.mAppTransitionState = state;
        updateBooster();
    }

    void updateBooster() {
        com.android.server.wm.WindowManagerService.sThreadPriorityBooster.setAppTransitionRunning(needsBoosting());
    }

    private boolean needsBoosting() {
        boolean recentsAnimRunning = this.mService.getRecentsAnimationController() != null;
        return !this.mNextAppTransitionRequests.isEmpty() || this.mAppTransitionState == 1 || this.mAppTransitionState == 2 || recentsAnimRunning;
    }

    void registerListenerLocked(com.android.server.wm.WindowManagerInternal.AppTransitionListener listener) {
        this.mListeners.add(listener);
    }

    void unregisterListener(com.android.server.wm.WindowManagerInternal.AppTransitionListener listener) {
        this.mListeners.remove(listener);
    }

    public void notifyAppTransitionFinishedLocked(android.os.IBinder token) {
        for (int i = 0; i < this.mListeners.size(); i++) {
            this.mListeners.get(i).onAppTransitionFinishedLocked(token);
        }
    }

    private void notifyAppTransitionPendingLocked() {
        for (int i = 0; i < this.mListeners.size(); i++) {
            this.mListeners.get(i).onAppTransitionPendingLocked();
        }
    }

    private void notifyAppTransitionCancelledLocked(boolean keyguardGoingAwayCancelled) {
        for (int i = 0; i < this.mListeners.size(); i++) {
            this.mListeners.get(i).onAppTransitionCancelledLocked(keyguardGoingAwayCancelled);
        }
    }

    private void notifyAppTransitionTimeoutLocked() {
        for (int i = 0; i < this.mListeners.size(); i++) {
            this.mListeners.get(i).onAppTransitionTimeoutLocked();
        }
    }

    private int notifyAppTransitionStartingLocked(long statusBarAnimationStartTime, long statusBarAnimationDuration) {
        int redoLayout = 0;
        for (int i = 0; i < this.mListeners.size(); i++) {
            redoLayout |= this.mListeners.get(i).onAppTransitionStartingLocked(statusBarAnimationStartTime, statusBarAnimationDuration);
        }
        return redoLayout;
    }

    int getDefaultWindowAnimationStyleResId() {
        return this.mDefaultWindowAnimationStyleResId;
    }

    int getAnimationStyleResId(android.view.WindowManager.LayoutParams lp) {
        return this.mTransitionAnimation.getAnimationStyleResId(lp);
    }

    android.view.animation.Animation loadAnimationSafely(android.content.Context context, int resId) {
        try {
            return this.mAppTransitionWrapper.getExtImpl().hookloadAnimationSafely(context, this.mNextAppTransitionType == 1, resId, this.mNextAppTransitionPackage, TAG);
        } catch (android.content.res.Resources.NotFoundException e) {
            android.util.Slog.w(TAG, "Unable to load animation resource", e);
            return null;
        } catch (java.lang.Exception e2) {
            android.util.Slog.e(TAG, "Unable to load animation resource", e2);
            return null;
        }
    }

    private static int mapOpenCloseTransitTypes(int transit, boolean enter) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12 = 6;
        switch (transit) {
            case 6:
            case 24:
                int animAttr = enter ? 4 : 5;
                return animAttr;
            case 7:
            case 25:
                if (!enter) {
                    i12 = 7;
                }
                int animAttr2 = i12;
                return animAttr2;
            case 8:
                if (enter) {
                    i = 8;
                } else {
                    i = 9;
                }
                int animAttr3 = i;
                return animAttr3;
            case 9:
                if (enter) {
                    i2 = 10;
                } else {
                    i2 = 11;
                }
                int animAttr4 = i2;
                return animAttr4;
            case 10:
                if (enter) {
                    i3 = 12;
                } else {
                    i3 = 13;
                }
                int animAttr5 = i3;
                return animAttr5;
            case 11:
                if (enter) {
                    i4 = 14;
                } else {
                    i4 = 15;
                }
                int animAttr6 = i4;
                return animAttr6;
            case 12:
                if (enter) {
                    i5 = 18;
                } else {
                    i5 = 19;
                }
                int animAttr7 = i5;
                return animAttr7;
            case 13:
                if (enter) {
                    i6 = 16;
                } else {
                    i6 = 17;
                }
                int animAttr8 = i6;
                return animAttr8;
            case 14:
                if (enter) {
                    i7 = 20;
                } else {
                    i7 = 21;
                }
                int animAttr9 = i7;
                return animAttr9;
            case 15:
                if (enter) {
                    i8 = 22;
                } else {
                    i8 = 23;
                }
                int animAttr10 = i8;
                return animAttr10;
            case 16:
                if (enter) {
                    i9 = 25;
                } else {
                    i9 = 24;
                }
                int animAttr11 = i9;
                return animAttr11;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 26:
            case 27:
            case 30:
            default:
                return 0;
            case 28:
                int animAttr12 = enter ? 4 : 5;
                return animAttr12;
            case 29:
                if (!enter) {
                    i12 = 7;
                }
                int animAttr13 = i12;
                return animAttr13;
            case 31:
                if (enter) {
                    i10 = 28;
                } else {
                    i10 = 29;
                }
                int animAttr14 = i10;
                return animAttr14;
            case 32:
                if (enter) {
                    i11 = 0;
                } else {
                    i11 = 27;
                }
                int animAttr15 = i11;
                return animAttr15;
        }
    }

    android.view.animation.Animation loadAnimationAttr(android.view.WindowManager.LayoutParams lp, int animAttr, int transit) {
        return this.mTransitionAnimation.loadAnimationAttr(lp, animAttr, transit);
    }

    private void getDefaultNextAppTransitionStartRect(android.graphics.Rect rect) {
        if (this.mDefaultNextAppTransitionAnimationSpec == null || this.mDefaultNextAppTransitionAnimationSpec.rect == null) {
            android.util.Slog.e(TAG, "Starting rect for app requested, but none available", new java.lang.Throwable());
            rect.setEmpty();
        } else {
            rect.set(this.mDefaultNextAppTransitionAnimationSpec.rect);
        }
    }

    private void putDefaultNextAppTransitionCoordinates(int left, int top, int width, int height, android.hardware.HardwareBuffer buffer) {
        this.mDefaultNextAppTransitionAnimationSpec = new android.view.AppTransitionAnimationSpec(-1, buffer, new android.graphics.Rect(left, top, left + width, top + height));
    }

    android.hardware.HardwareBuffer createCrossProfileAppsThumbnail(android.graphics.drawable.Drawable thumbnailDrawable, android.graphics.Rect frame) {
        return this.mTransitionAnimation.createCrossProfileAppsThumbnail(thumbnailDrawable, frame);
    }

    android.view.animation.Animation createCrossProfileAppsThumbnailAnimationLocked(android.graphics.Rect appRect) {
        return this.mTransitionAnimation.createCrossProfileAppsThumbnailAnimationLocked(appRect);
    }

    android.view.animation.Animation createThumbnailAspectScaleAnimationLocked(android.graphics.Rect appRect, android.graphics.Rect contentInsets, android.hardware.HardwareBuffer thumbnailHeader, com.android.server.wm.WindowContainer container, int orientation) {
        android.view.AppTransitionAnimationSpec spec = this.mNextAppTransitionAnimationsSpecs.get(container.hashCode());
        com.android.internal.policy.TransitionAnimation transitionAnimation = this.mTransitionAnimation;
        android.graphics.Rect rect = null;
        android.graphics.Rect rect2 = spec != null ? spec.rect : null;
        if (this.mDefaultNextAppTransitionAnimationSpec != null) {
            rect = this.mDefaultNextAppTransitionAnimationSpec.rect;
        }
        return transitionAnimation.createThumbnailAspectScaleAnimationLocked(appRect, contentInsets, thumbnailHeader, orientation, rect2, rect, this.mNextAppTransitionScaleUp);
    }

    private android.view.animation.AnimationSet createAspectScaledThumbnailFreeformAnimationLocked(android.graphics.Rect sourceFrame, android.graphics.Rect destFrame, android.graphics.Rect surfaceInsets, boolean enter) {
        float sourceWidth = sourceFrame.width();
        float sourceHeight = sourceFrame.height();
        float destWidth = destFrame.width();
        float destHeight = destFrame.height();
        float scaleH = enter ? sourceWidth / destWidth : destWidth / sourceWidth;
        float scaleV = enter ? sourceHeight / destHeight : destHeight / sourceHeight;
        android.view.animation.AnimationSet set = new android.view.animation.AnimationSet(true);
        int surfaceInsetsH = surfaceInsets == null ? 0 : surfaceInsets.left + surfaceInsets.right;
        int surfaceInsetsV = surfaceInsets != null ? surfaceInsets.top + surfaceInsets.bottom : 0;
        float scaleHCenter = ((enter ? destWidth : sourceWidth) + surfaceInsetsH) / 2.0f;
        float scaleVCenter = ((enter ? destHeight : sourceHeight) + surfaceInsetsV) / 2.0f;
        android.view.animation.ScaleAnimation scale = enter ? new android.view.animation.ScaleAnimation(scaleH, 1.0f, scaleV, 1.0f, scaleHCenter, scaleVCenter) : new android.view.animation.ScaleAnimation(1.0f, scaleH, 1.0f, scaleV, scaleHCenter, scaleVCenter);
        int sourceHCenter = sourceFrame.left + (sourceFrame.width() / 2);
        int sourceVCenter = sourceFrame.top + (sourceFrame.height() / 2);
        int destHCenter = destFrame.left + (destFrame.width() / 2);
        int destVCenter = destFrame.top + (destFrame.height() / 2);
        int fromX = enter ? sourceHCenter - destHCenter : destHCenter - sourceHCenter;
        int fromY = enter ? sourceVCenter - destVCenter : destVCenter - sourceVCenter;
        android.view.animation.TranslateAnimation translation = enter ? new android.view.animation.TranslateAnimation(fromX, 0.0f, fromY, 0.0f) : new android.view.animation.TranslateAnimation(0.0f, fromX, 0.0f, fromY);
        set.addAnimation(scale);
        set.addAnimation(translation);
        setAppTransitionFinishedCallbackIfNeeded(set);
        return set;
    }

    boolean canSkipFirstFrame() {
        return (this.mNextAppTransitionType == 1 || this.mNextAppTransitionOverrideRequested || this.mNextAppTransitionType == 7 || this.mNextAppTransitionType == 8 || this.mNextAppTransitionRequests.contains(7)) ? false : true;
    }

    com.android.server.wm.RemoteAnimationController getRemoteAnimationController() {
        return this.mRemoteAnimationController;
    }

    /* JADX WARN: Multi-variable type inference failed */
    android.view.animation.Animation loadAnimation(android.view.WindowManager.LayoutParams lp, int transit, boolean enter, int uiMode, int orientation, android.graphics.Rect frame, android.graphics.Rect displayFrame, android.graphics.Rect insets, android.graphics.Rect surfaceInsets, android.graphics.Rect stableInsets, boolean isVoiceInteraction, boolean freeform, com.android.server.wm.WindowContainer container) {
        android.view.animation.Animation a;
        boolean canCustomizeAppTransition = container.canCustomizeAppTransition();
        if (this.mNextAppTransitionOverrideRequested) {
            if (canCustomizeAppTransition || this.mOverrideTaskTransition || this.mAppTransitionWrapper.getExtImpl().canCustomizeAppTransition(lp, transit, enter, container, this.mNextAppTransitionPackage)) {
                this.mNextAppTransitionType = 1;
            } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[4]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -4049608245387511746L, 0, null, null);
            }
        }
        if (isKeyguardGoingAwayTransitOld(transit) && enter) {
            android.view.animation.Animation a2 = this.mAppTransitionWrapper.getExtImpl().createHiddenByKeyguardExit(this.mNextAppTransitionFlags, transit == 21, this.mService.mAtmService.mKeyguardController.getWrapper().getExtImpl().getKeyguardGoingAwayFlags(), container.isActivityTypeHome());
            if (a2 == null) {
                a = this.mTransitionAnimation.loadKeyguardExitAnimation(this.mNextAppTransitionFlags, transit == 21);
            } else {
                setAppTransitionFinishedCallbackIfNeeded(a2);
                return a2;
            }
        } else if (transit == 22 || transit == 33) {
            a = null;
        } else if (transit == 23 && !enter) {
            android.view.animation.Animation a3 = this.mAppTransitionWrapper.getExtImpl().loadKeyguardUnoccludeAnimation(container);
            if (a3 != null) {
                setAppTransitionFinishedCallbackIfNeeded(a3);
                return a3;
            }
            a = this.mTransitionAnimation.loadKeyguardUnoccludeAnimation();
        } else if (transit == 26) {
            a = null;
        } else if (isVoiceInteraction && (transit == 6 || transit == 8 || transit == 10)) {
            a = this.mTransitionAnimation.loadVoiceActivityOpenAnimation(enter);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(a);
                java.lang.String protoLogParam1 = java.lang.String.valueOf(appTransitionOldToString(transit));
                java.lang.String protoLogParam3 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -2133100418670643322L, 48, null, protoLogParam0, protoLogParam1, java.lang.Boolean.valueOf(enter), protoLogParam3);
            }
        } else if (isVoiceInteraction && (transit == 7 || transit == 9 || transit == 11)) {
            a = this.mTransitionAnimation.loadVoiceActivityExitAnimation(enter);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(a);
                java.lang.String protoLogParam12 = java.lang.String.valueOf(appTransitionOldToString(transit));
                java.lang.String protoLogParam32 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -2133100418670643322L, 48, null, protoLogParam02, protoLogParam12, java.lang.Boolean.valueOf(enter), protoLogParam32);
            }
        } else {
            if (transit != 18) {
                if (this.mNextAppTransitionType == 1) {
                    android.view.animation.Animation a4 = this.mAppTransitionWrapper.getExtImpl().checkAndLoadCustomAnimation(this.mNextAppTransitionPackage, transit, enter, enter ? this.mNextAppTransitionEnter : this.mNextAppTransitionExit);
                    if (a4 != null) {
                        setAppTransitionFinishedCallbackIfNeeded(a4);
                        return a4;
                    }
                    android.view.animation.Animation a5 = this.mAppTransitionWrapper.getExtImpl().loadTransitCustomCompactWindowAnimation(lp, transit, enter, container);
                    if (a5 != null) {
                        setAppTransitionFinishedCallbackIfNeeded(a5);
                        return a5;
                    }
                    getNextAppRequestedAnimation(enter);
                    android.view.animation.Animation a6 = this.mTransitionAnimation.loadAppTransitionAnimation(this.mNextAppTransitionPackage, enter ? this.mNextAppTransitionEnter : this.mNextAppTransitionExit);
                    if (this.mNextAppTransitionBackgroundColor != 0) {
                        a6.setBackdropColor(this.mNextAppTransitionBackgroundColor);
                    }
                    a = this.mAppTransitionWrapper.getExtImpl().loadCustomZoomAnimation(transit, container, a6);
                    this.mAppTransitionWrapper.getExtImpl().setRoundedCornersForCustomAnim(a);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                        java.lang.String protoLogParam03 = java.lang.String.valueOf(a);
                        java.lang.String protoLogParam13 = java.lang.String.valueOf(appTransitionOldToString(transit));
                        java.lang.String protoLogParam33 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -8382864384468306610L, 48, null, protoLogParam03, protoLogParam13, java.lang.Boolean.valueOf(enter), protoLogParam33);
                    }
                } else if (this.mNextAppTransitionType == 7) {
                    a = this.mTransitionAnimation.loadAppTransitionAnimation(this.mNextAppTransitionPackage, this.mNextAppTransitionInPlace);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                        java.lang.String protoLogParam04 = java.lang.String.valueOf(a);
                        java.lang.String protoLogParam14 = java.lang.String.valueOf(appTransitionOldToString(transit));
                        java.lang.String protoLogParam2 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 222576013987954454L, 0, null, protoLogParam04, protoLogParam14, protoLogParam2);
                    }
                } else if (this.mNextAppTransitionType == 8) {
                    a = this.mTransitionAnimation.createClipRevealAnimationLockedCompat(transit, enter, frame, displayFrame, this.mDefaultNextAppTransitionAnimationSpec != null ? this.mDefaultNextAppTransitionAnimationSpec.rect : null);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                        java.lang.String protoLogParam05 = java.lang.String.valueOf(a);
                        java.lang.String protoLogParam15 = java.lang.String.valueOf(appTransitionOldToString(transit));
                        java.lang.String protoLogParam22 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 4808089291562562413L, 0, null, protoLogParam05, protoLogParam15, protoLogParam22);
                    }
                } else if (this.mNextAppTransitionType == 2) {
                    a = this.mTransitionAnimation.createScaleUpAnimationLockedCompat(transit, enter, frame, this.mDefaultNextAppTransitionAnimationSpec != null ? this.mDefaultNextAppTransitionAnimationSpec.rect : null);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                        java.lang.String protoLogParam06 = java.lang.String.valueOf(a);
                        java.lang.String protoLogParam16 = java.lang.String.valueOf(appTransitionOldToString(transit));
                        java.lang.String protoLogParam23 = java.lang.String.valueOf(enter);
                        java.lang.String protoLogParam34 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -1463563572526433695L, 0, null, protoLogParam06, protoLogParam16, protoLogParam23, protoLogParam34);
                    }
                } else if (this.mNextAppTransitionType == 3 || this.mNextAppTransitionType == 4) {
                    int i = 3;
                    char c = 1;
                    com.android.server.wm.WindowContainer windowContainer = container;
                    this.mNextAppTransitionScaleUp = this.mNextAppTransitionType == i ? c : 0;
                    android.hardware.HardwareBuffer thumbnailHeader = getAppTransitionThumbnailHeader(windowContainer);
                    android.view.animation.Animation a7 = this.mTransitionAnimation.createThumbnailEnterExitAnimationLockedCompat(enter, this.mNextAppTransitionScaleUp, frame, transit, thumbnailHeader, this.mDefaultNextAppTransitionAnimationSpec != null ? this.mDefaultNextAppTransitionAnimationSpec.rect : null);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[c]) {
                        java.lang.String protoLogParam07 = java.lang.String.valueOf(a7);
                        java.lang.String protoLogParam17 = java.lang.String.valueOf(this.mNextAppTransitionScaleUp ? "ANIM_THUMBNAIL_SCALE_UP" : "ANIM_THUMBNAIL_SCALE_DOWN");
                        java.lang.String protoLogParam24 = java.lang.String.valueOf(appTransitionOldToString(transit));
                        java.lang.String protoLogParam4 = java.lang.String.valueOf(android.os.Debug.getCallers(i));
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -8749850292010208926L, 192, null, protoLogParam07, protoLogParam17, protoLogParam24, java.lang.Boolean.valueOf(enter), protoLogParam4);
                    }
                    a = a7;
                } else if (this.mNextAppTransitionType == 5 || this.mNextAppTransitionType == 6) {
                    this.mNextAppTransitionScaleUp = this.mNextAppTransitionType == 5;
                    android.view.AppTransitionAnimationSpec spec = this.mNextAppTransitionAnimationsSpecs.get(container.hashCode());
                    android.view.animation.Animation a8 = this.mTransitionAnimation.createAspectScaledThumbnailEnterExitAnimationLocked(enter, this.mNextAppTransitionScaleUp, orientation, transit, frame, insets, surfaceInsets, stableInsets, freeform, spec != null ? spec.rect : null, this.mDefaultNextAppTransitionAnimationSpec != null ? this.mDefaultNextAppTransitionAnimationSpec.rect : null);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                        java.lang.String protoLogParam08 = java.lang.String.valueOf(a8);
                        java.lang.String protoLogParam18 = java.lang.String.valueOf(this.mNextAppTransitionScaleUp ? "ANIM_THUMBNAIL_ASPECT_SCALE_UP" : "ANIM_THUMBNAIL_ASPECT_SCALE_DOWN");
                        java.lang.String protoLogParam25 = java.lang.String.valueOf(appTransitionOldToString(transit));
                        java.lang.String protoLogParam42 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -8749850292010208926L, 192, null, protoLogParam08, protoLogParam18, protoLogParam25, java.lang.Boolean.valueOf(enter), protoLogParam42);
                    }
                    a = a8;
                } else if (this.mNextAppTransitionType == 9 && enter) {
                    a = this.mTransitionAnimation.loadCrossProfileAppEnterAnimation();
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                        java.lang.String protoLogParam09 = java.lang.String.valueOf(a);
                        java.lang.String protoLogParam19 = java.lang.String.valueOf(appTransitionOldToString(transit));
                        java.lang.String protoLogParam26 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 5939232373291430513L, 0, null, protoLogParam09, protoLogParam19, protoLogParam26);
                    }
                } else if (!isChangeTransitOld(transit)) {
                    android.view.animation.Animation a9 = this.mAppTransitionWrapper.getExtImpl().loadFlexibleTaskTransitionAnimation(transit, enter, container, this.mDisplayContent.mOpeningApps, this.mDisplayContent.mClosingApps);
                    if (a9 != null) {
                        return a9;
                    }
                    android.view.animation.Animation a10 = this.mAppTransitionWrapper.getExtImpl().loadFlexibleActivityTransitionAnimation(transit, enter, container, this.mDisplayContent.mOpeningApps, this.mDisplayContent.mClosingApps);
                    if (a10 != null) {
                        return a10;
                    }
                    android.view.animation.Animation a11 = this.mAppTransitionWrapper.getExtImpl().loadOnePuttTransitionAnimation(transit, enter, container);
                    if (a11 != null) {
                        return a11;
                    }
                    android.view.animation.Animation a12 = this.mAppTransitionWrapper.getExtImpl().loadCompactWindowAnimation(lp, transit, enter, container);
                    if (a12 != null) {
                        return a12;
                    }
                    android.view.animation.Animation a13 = this.mAppTransitionWrapper.getExtImpl().loadOplusStyleAnimation(lp, transit, enter);
                    if (a13 != null) {
                        setAppTransitionFinishedCallbackIfNeeded(a13);
                        return a13;
                    }
                    int animAttr = mapOpenCloseTransitTypes(transit, enter);
                    if (animAttr != 0) {
                        com.android.server.wm.ActivityRecord.CustomAppTransition customAppTransition = getCustomAppTransition(animAttr, container);
                        if (customAppTransition != null) {
                            a = loadCustomActivityAnimation(customAppTransition, enter, container);
                        } else if (canCustomizeAppTransition) {
                            a = loadAnimationAttr(lp, animAttr, transit);
                        } else {
                            a = this.mTransitionAnimation.loadDefaultAnimationAttr(animAttr, transit);
                        }
                    } else {
                        a = null;
                    }
                    this.mAppTransitionWrapper.getExtImpl().updateAnimationForZoom(transit, container, a);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                        java.lang.String protoLogParam010 = java.lang.String.valueOf(a);
                        long protoLogParam110 = animAttr;
                        java.lang.String protoLogParam27 = java.lang.String.valueOf(appTransitionOldToString(transit));
                        java.lang.String protoLogParam5 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -1218632020771063497L, 964, null, protoLogParam010, java.lang.Long.valueOf(protoLogParam110), protoLogParam27, java.lang.Boolean.valueOf(enter), java.lang.Boolean.valueOf(canCustomizeAppTransition), protoLogParam5);
                    }
                } else {
                    a = new android.view.animation.AlphaAnimation(1.0f, 1.0f);
                    a.setDuration(336L);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                        java.lang.String protoLogParam011 = java.lang.String.valueOf(a);
                        java.lang.String protoLogParam111 = java.lang.String.valueOf(appTransitionOldToString(transit));
                        java.lang.String protoLogParam35 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 9082776604722675018L, 48, null, protoLogParam011, protoLogParam111, java.lang.Boolean.valueOf(enter), protoLogParam35);
                    }
                }
            } else {
                a = this.mTransitionAnimation.createRelaunchAnimation(frame, insets, this.mDefaultNextAppTransitionAnimationSpec != null ? this.mDefaultNextAppTransitionAnimationSpec.rect : null);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled[1]) {
                    java.lang.String protoLogParam012 = java.lang.String.valueOf(a);
                    java.lang.String protoLogParam112 = java.lang.String.valueOf(appTransitionOldToString(transit));
                    java.lang.String protoLogParam28 = java.lang.String.valueOf(android.os.Debug.getCallers(3));
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 6121116119545820299L, 0, null, protoLogParam012, protoLogParam112, protoLogParam28);
                }
            }
        }
        setAppTransitionFinishedCallbackIfNeeded(a);
        return a;
    }

    com.android.server.wm.ActivityRecord.CustomAppTransition getCustomAppTransition(int animAttr, com.android.server.wm.WindowContainer container) {
        com.android.server.wm.ActivityRecord customAnimationSource = container.asActivityRecord();
        if (customAnimationSource == null) {
            return null;
        }
        if ((animAttr == 5 || animAttr == 6) && (customAnimationSource = customAnimationSource.getTask().getActivityAbove(customAnimationSource)) == null) {
            return null;
        }
        switch (animAttr) {
        }
        return null;
    }

    private android.view.animation.Animation loadCustomActivityAnimation(com.android.server.wm.ActivityRecord.CustomAppTransition custom, boolean enter, com.android.server.wm.WindowContainer container) {
        com.android.server.wm.ActivityRecord customAnimationSource = container.asActivityRecord();
        android.view.animation.Animation a = this.mTransitionAnimation.loadAppTransitionAnimation(customAnimationSource.packageName, enter ? custom.mEnterAnim : custom.mExitAnim);
        if (a != null && custom.mBackgroundColor != 0) {
            a.setBackdropColor(custom.mBackgroundColor);
            a.setShowBackdrop(true);
        }
        return a;
    }

    int getAppRootTaskClipMode() {
        if (this.mNextAppTransitionRequests.contains(5) || this.mNextAppTransitionRequests.contains(7) || this.mNextAppTransitionType == 8) {
            return 1;
        }
        return 0;
    }

    public int getTransitFlags() {
        return this.mNextAppTransitionFlags;
    }

    void postAnimationCallback() {
        if (this.mNextAppTransitionCallback != null) {
            this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.wm.AppTransition$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.AppTransition.doAnimationCallback((android.os.IRemoteCallback) obj);
                }
            }, this.mNextAppTransitionCallback));
            this.mNextAppTransitionCallback = null;
        }
    }

    void overridePendingAppTransition(java.lang.String packageName, int enterAnim, int exitAnim, int backgroundColor, android.os.IRemoteCallback startedCallback, android.os.IRemoteCallback endedCallback, boolean overrideTaskTransaction) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionOverrideRequested = true;
            this.mNextAppTransitionPackage = packageName;
            this.mNextAppTransitionEnter = enterAnim;
            this.mNextAppTransitionExit = exitAnim;
            this.mNextAppTransitionBackgroundColor = backgroundColor;
            postAnimationCallback();
            this.mNextAppTransitionCallback = startedCallback;
            this.mAnimationFinishedCallback = endedCallback;
            this.mOverrideTaskTransition = overrideTaskTransaction;
        }
    }

    void overridePendingAppTransitionScaleUp(int startX, int startY, int startWidth, int startHeight) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = 2;
            putDefaultNextAppTransitionCoordinates(startX, startY, startWidth, startHeight, null);
            postAnimationCallback();
        }
    }

    void overridePendingAppTransitionClipReveal(int startX, int startY, int startWidth, int startHeight) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = 8;
            putDefaultNextAppTransitionCoordinates(startX, startY, startWidth, startHeight, null);
            postAnimationCallback();
        }
    }

    void overridePendingAppTransitionThumb(android.hardware.HardwareBuffer srcThumb, int startX, int startY, android.os.IRemoteCallback startedCallback, boolean scaleUp) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = scaleUp ? 3 : 4;
            this.mNextAppTransitionScaleUp = scaleUp;
            putDefaultNextAppTransitionCoordinates(startX, startY, 0, 0, srcThumb);
            postAnimationCallback();
            this.mNextAppTransitionCallback = startedCallback;
        }
    }

    void overridePendingAppTransitionAspectScaledThumb(android.hardware.HardwareBuffer srcThumb, int startX, int startY, int targetWidth, int targetHeight, android.os.IRemoteCallback startedCallback, boolean scaleUp) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = scaleUp ? 5 : 6;
            this.mNextAppTransitionScaleUp = scaleUp;
            putDefaultNextAppTransitionCoordinates(startX, startY, targetWidth, targetHeight, srcThumb);
            postAnimationCallback();
            this.mNextAppTransitionCallback = startedCallback;
        }
    }

    void overridePendingAppTransitionMultiThumb(android.view.AppTransitionAnimationSpec[] specs, android.os.IRemoteCallback onAnimationStartedCallback, android.os.IRemoteCallback onAnimationFinishedCallback, boolean scaleUp) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = scaleUp ? 5 : 6;
            this.mNextAppTransitionScaleUp = scaleUp;
            if (specs != null) {
                for (int i = 0; i < specs.length; i++) {
                    android.view.AppTransitionAnimationSpec spec = specs[i];
                    if (spec != null) {
                        java.util.function.Predicate<com.android.server.wm.Task> predicateObtainPredicate = com.android.internal.util.function.pooled.PooledLambda.obtainPredicate(new com.android.server.wm.AppTransition$$ExternalSyntheticLambda1(), com.android.internal.util.function.pooled.PooledLambda.__(com.android.server.wm.Task.class), java.lang.Integer.valueOf(spec.taskId));
                        com.android.server.wm.WindowContainer container = this.mDisplayContent.getTask(predicateObtainPredicate);
                        predicateObtainPredicate.recycle();
                        if (container != null) {
                            this.mNextAppTransitionAnimationsSpecs.put(container.hashCode(), spec);
                            if (i == 0) {
                                android.graphics.Rect rect = spec.rect;
                                putDefaultNextAppTransitionCoordinates(rect.left, rect.top, rect.width(), rect.height(), spec.buffer);
                            }
                        }
                    }
                }
            }
            postAnimationCallback();
            this.mNextAppTransitionCallback = onAnimationStartedCallback;
            this.mAnimationFinishedCallback = onAnimationFinishedCallback;
        }
    }

    void overridePendingAppTransitionMultiThumbFuture(android.view.IAppTransitionAnimationSpecsFuture specsFuture, android.os.IRemoteCallback callback, boolean scaleUp) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = scaleUp ? 5 : 6;
            this.mNextAppTransitionAnimationsSpecsFuture = specsFuture;
            this.mNextAppTransitionScaleUp = scaleUp;
            this.mNextAppTransitionFutureCallback = callback;
            if (isReady()) {
                fetchAppTransitionSpecsFromFuture();
            }
        }
    }

    void overridePendingAppTransitionRemote(android.view.RemoteAnimationAdapter remoteAnimationAdapter) {
        overridePendingAppTransitionRemote(remoteAnimationAdapter, false, false);
    }

    void overridePendingAppTransitionRemote(android.view.RemoteAnimationAdapter remoteAnimationAdapter, boolean sync, boolean isActivityEmbedding) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[2]) {
            boolean protoLogParam0 = isTransitionSet();
            java.lang.String protoLogParam1 = java.lang.String.valueOf(remoteAnimationAdapter);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 6217525691846442213L, 3, null, java.lang.Boolean.valueOf(protoLogParam0), protoLogParam1);
        }
        boolean protoLogParam02 = isTransitionSet();
        if (protoLogParam02 && !this.mNextAppTransitionIsSync) {
            clear(!isActivityEmbedding);
            this.mNextAppTransitionType = 10;
            this.mRemoteAnimationController = new com.android.server.wm.RemoteAnimationController(this.mService, this.mDisplayContent, remoteAnimationAdapter, this.mHandler, isActivityEmbedding);
            android.util.Slog.e(TAG, "create RemoteAnimationController " + this.mRemoteAnimationController);
            this.mNextAppTransitionIsSync = sync;
        }
    }

    void overrideInPlaceAppTransition(java.lang.String packageName, int anim) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = 7;
            this.mNextAppTransitionPackage = packageName;
            this.mNextAppTransitionInPlace = anim;
        }
    }

    void overridePendingAppTransitionStartCrossProfileApps() {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = 9;
            postAnimationCallback();
        }
    }

    private boolean canOverridePendingAppTransition() {
        return isTransitionSet() && this.mNextAppTransitionType != 10;
    }

    private void fetchAppTransitionSpecsFromFuture() {
        if (this.mNextAppTransitionAnimationsSpecsFuture != null) {
            this.mNextAppTransitionAnimationsSpecsPending = true;
            final android.view.IAppTransitionAnimationSpecsFuture future = this.mNextAppTransitionAnimationsSpecsFuture;
            this.mNextAppTransitionAnimationsSpecsFuture = null;
            this.mDefaultExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.wm.AppTransition$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$fetchAppTransitionSpecsFromFuture$1(future);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchAppTransitionSpecsFromFuture$1(android.view.IAppTransitionAnimationSpecsFuture future) {
        android.view.AppTransitionAnimationSpec[] specs = null;
        try {
            android.os.Binder.allowBlocking(future.asBinder());
            specs = future.get();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to fetch app transition specs: " + e);
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mNextAppTransitionAnimationsSpecsPending = false;
                overridePendingAppTransitionMultiThumb(specs, this.mNextAppTransitionFutureCallback, null, this.mNextAppTransitionScaleUp);
                this.mNextAppTransitionFutureCallback = null;
                this.mService.requestTraversal();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("mNextAppTransitionRequests=[");
        boolean separator = false;
        for (java.lang.Integer transit : this.mNextAppTransitionRequests) {
            if (separator) {
                sb.append(", ");
            }
            sb.append(appTransitionToString(transit.intValue()));
            separator = true;
        }
        sb.append("]");
        sb.append(", mNextAppTransitionFlags=" + appTransitionFlagsToString(this.mNextAppTransitionFlags));
        sb.append(",mAppTransitionState=" + appStateToString());
        return sb.toString();
    }

    public static java.lang.String appTransitionOldToString(int transition) {
        switch (transition) {
            case -1:
                return "TRANSIT_OLD_UNSET";
            case 0:
                return "TRANSIT_OLD_NONE";
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 17:
            case 19:
            case 27:
            default:
                return "<UNKNOWN: " + transition + ">";
            case 6:
                return "TRANSIT_OLD_ACTIVITY_OPEN";
            case 7:
                return "TRANSIT_OLD_ACTIVITY_CLOSE";
            case 8:
                return "TRANSIT_OLD_TASK_OPEN";
            case 9:
                return "TRANSIT_OLD_TASK_CLOSE";
            case 10:
                return "TRANSIT_OLD_TASK_TO_FRONT";
            case 11:
                return "TRANSIT_OLD_TASK_TO_BACK";
            case 12:
                return "TRANSIT_OLD_WALLPAPER_CLOSE";
            case 13:
                return "TRANSIT_OLD_WALLPAPER_OPEN";
            case 14:
                return "TRANSIT_OLD_WALLPAPER_INTRA_OPEN";
            case 15:
                return "TRANSIT_OLD_WALLPAPER_INTRA_CLOSE";
            case 16:
                return "TRANSIT_OLD_TASK_OPEN_BEHIND";
            case 18:
                return "TRANSIT_OLD_ACTIVITY_RELAUNCH";
            case 20:
                return "TRANSIT_OLD_KEYGUARD_GOING_AWAY";
            case 21:
                return "TRANSIT_OLD_KEYGUARD_GOING_AWAY_ON_WALLPAPER";
            case 22:
                return "TRANSIT_OLD_KEYGUARD_OCCLUDE";
            case 23:
                return "TRANSIT_OLD_KEYGUARD_UNOCCLUDE";
            case 24:
                return "TRANSIT_OLD_TRANSLUCENT_ACTIVITY_OPEN";
            case 25:
                return "TRANSIT_OLD_TRANSLUCENT_ACTIVITY_CLOSE";
            case 26:
                return "TRANSIT_OLD_CRASHING_ACTIVITY_CLOSE";
            case 28:
                return "TRANSIT_OLD_TASK_FRAGMENT_OPEN";
            case 29:
                return "TRANSIT_OLD_TASK_FRAGMENT_CLOSE";
            case 30:
                return "TRANSIT_OLD_TASK_FRAGMENT_CHANGE";
            case 31:
                return "TRANSIT_OLD_DREAM_ACTIVITY_OPEN";
            case 32:
                return "TRANSIT_OLD_DREAM_ACTIVITY_CLOSE";
            case 33:
                return "TRANSIT_OLD_KEYGUARD_OCCLUDE_BY_DREAM";
        }
    }

    public static java.lang.String appTransitionToString(int transition) {
        switch (transition) {
            case 0:
                return "TRANSIT_NONE";
            case 1:
                return "TRANSIT_OPEN";
            case 2:
                return "TRANSIT_CLOSE";
            case 3:
                return "TRANSIT_TO_FRONT";
            case 4:
                return "TRANSIT_TO_BACK";
            case 5:
                return "TRANSIT_RELAUNCH";
            case 6:
                return "TRANSIT_CHANGE";
            case 7:
                return "TRANSIT_KEYGUARD_GOING_AWAY";
            case 8:
                return "TRANSIT_KEYGUARD_OCCLUDE";
            case 9:
                return "TRANSIT_KEYGUARD_UNOCCLUDE";
            default:
                return "<UNKNOWN: " + transition + ">";
        }
    }

    private java.lang.String appStateToString() {
        switch (this.mAppTransitionState) {
            case 0:
                return "APP_STATE_IDLE";
            case 1:
                return "APP_STATE_READY";
            case 2:
                return "APP_STATE_RUNNING";
            case 3:
                return "APP_STATE_TIMEOUT";
            default:
                return "unknown state=" + this.mAppTransitionState;
        }
    }

    private java.lang.String transitTypeToString() {
        switch (this.mNextAppTransitionType) {
            case 0:
                return "NEXT_TRANSIT_TYPE_NONE";
            case 1:
                return "NEXT_TRANSIT_TYPE_CUSTOM";
            case 2:
                return "NEXT_TRANSIT_TYPE_SCALE_UP";
            case 3:
                return "NEXT_TRANSIT_TYPE_THUMBNAIL_SCALE_UP";
            case 4:
                return "NEXT_TRANSIT_TYPE_THUMBNAIL_SCALE_DOWN";
            case 5:
                return "NEXT_TRANSIT_TYPE_THUMBNAIL_ASPECT_SCALE_UP";
            case 6:
                return "NEXT_TRANSIT_TYPE_THUMBNAIL_ASPECT_SCALE_DOWN";
            case 7:
                return "NEXT_TRANSIT_TYPE_CUSTOM_IN_PLACE";
            case 8:
            default:
                return "unknown type=" + this.mNextAppTransitionType;
            case 9:
                return "NEXT_TRANSIT_TYPE_OPEN_CROSS_PROFILE_APPS";
        }
    }

    static {
        sFlagToString.add(new android.util.Pair<>(1, "TRANSIT_FLAG_KEYGUARD_GOING_AWAY_TO_SHADE"));
        sFlagToString.add(new android.util.Pair<>(2, "TRANSIT_FLAG_KEYGUARD_GOING_AWAY_NO_ANIMATION"));
        sFlagToString.add(new android.util.Pair<>(4, "TRANSIT_FLAG_KEYGUARD_GOING_AWAY_WITH_WALLPAPER"));
        sFlagToString.add(new android.util.Pair<>(8, "TRANSIT_FLAG_KEYGUARD_GOING_AWAY_SUBTLE_ANIMATION"));
        sFlagToString.add(new android.util.Pair<>(512, "TRANSIT_FLAG_KEYGUARD_GOING_AWAY_TO_LAUNCHER_WITH_IN_WINDOW_ANIMATIONS"));
        sFlagToString.add(new android.util.Pair<>(16, "TRANSIT_FLAG_APP_CRASHED"));
        sFlagToString.add(new android.util.Pair<>(32, "TRANSIT_FLAG_OPEN_BEHIND"));
    }

    public static java.lang.String appTransitionFlagsToString(int flags) {
        java.lang.String sep = "";
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (android.util.Pair<java.lang.Integer, java.lang.String> pair : sFlagToString) {
            if ((((java.lang.Integer) pair.first).intValue() & flags) != 0) {
                sb.append(sep);
                sb.append((java.lang.String) pair.second);
                sep = " | ";
            }
        }
        return sb.toString();
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1159641169921L, this.mAppTransitionState);
        proto.write(1159641169922L, this.mLastUsedAppTransition);
        proto.end(token);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.println(this);
        pw.print(prefix);
        pw.print("mAppTransitionState=");
        pw.println(appStateToString());
        if (this.mNextAppTransitionType != 0) {
            pw.print(prefix);
            pw.print("mNextAppTransitionType=");
            pw.println(transitTypeToString());
        }
        if (this.mNextAppTransitionOverrideRequested || this.mNextAppTransitionType == 1) {
            pw.print(prefix);
            pw.print("mNextAppTransitionPackage=");
            pw.println(this.mNextAppTransitionPackage);
            pw.print(prefix);
            pw.print("mNextAppTransitionEnter=0x");
            pw.print(java.lang.Integer.toHexString(this.mNextAppTransitionEnter));
            pw.print(" mNextAppTransitionExit=0x");
            pw.println(java.lang.Integer.toHexString(this.mNextAppTransitionExit));
            pw.print(" mNextAppTransitionBackgroundColor=0x");
            pw.println(java.lang.Integer.toHexString(this.mNextAppTransitionBackgroundColor));
        }
        switch (this.mNextAppTransitionType) {
            case 2:
                getDefaultNextAppTransitionStartRect(this.mTmpRect);
                pw.print(prefix);
                pw.print("mNextAppTransitionStartX=");
                pw.print(this.mTmpRect.left);
                pw.print(" mNextAppTransitionStartY=");
                pw.println(this.mTmpRect.top);
                pw.print(prefix);
                pw.print("mNextAppTransitionStartWidth=");
                pw.print(this.mTmpRect.width());
                pw.print(" mNextAppTransitionStartHeight=");
                pw.println(this.mTmpRect.height());
                break;
            case 3:
            case 4:
            case 5:
            case 6:
                pw.print(prefix);
                pw.print("mDefaultNextAppTransitionAnimationSpec=");
                pw.println(this.mDefaultNextAppTransitionAnimationSpec);
                pw.print(prefix);
                pw.print("mNextAppTransitionAnimationsSpecs=");
                pw.println(this.mNextAppTransitionAnimationsSpecs);
                pw.print(prefix);
                pw.print("mNextAppTransitionScaleUp=");
                pw.println(this.mNextAppTransitionScaleUp);
                break;
            case 7:
                pw.print(prefix);
                pw.print("mNextAppTransitionPackage=");
                pw.println(this.mNextAppTransitionPackage);
                pw.print(prefix);
                pw.print("mNextAppTransitionInPlace=0x");
                pw.print(java.lang.Integer.toHexString(this.mNextAppTransitionInPlace));
                break;
        }
        if (this.mNextAppTransitionCallback != null) {
            pw.print(prefix);
            pw.print("mNextAppTransitionCallback=");
            pw.println(this.mNextAppTransitionCallback);
        }
        if (this.mLastUsedAppTransition != 0) {
            pw.print(prefix);
            pw.print("mLastUsedAppTransition=");
            pw.println(appTransitionOldToString(this.mLastUsedAppTransition));
            pw.print(prefix);
            pw.print("mLastOpeningApp=");
            pw.println(this.mLastOpeningApp);
            pw.print(prefix);
            pw.print("mLastClosingApp=");
            pw.println(this.mLastClosingApp);
            pw.print(prefix);
            pw.print("mLastChangingApp=");
            pw.println(this.mLastChangingApp);
        }
    }

    boolean prepareAppTransition(int transit, int flags) {
        if (this.mDisplayContent.mTransitionController.isShellTransitionsEnabled()) {
            return false;
        }
        this.mNextAppTransitionRequests.add(java.lang.Integer.valueOf(transit));
        this.mNextAppTransitionFlags |= flags;
        updateBooster();
        removeAppTransitionTimeoutCallbacks();
        this.mHandler.postDelayed(this.mHandleAppTransitionTimeoutRunnable, APP_TRANSITION_TIMEOUT_MS);
        this.mAppTransitionWrapper.getExtImpl().postAppTransitionDelayedCallback(this.mHandler, transit, this.mRemoteAnimationController, this.mDisplayContent);
        return prepare();
    }

    public static boolean isKeyguardGoingAwayTransitOld(int transit) {
        return transit == 20 || transit == 21;
    }

    static boolean isKeyguardOccludeTransitOld(int transit) {
        return transit == 22 || transit == 33 || transit == 23;
    }

    static boolean isKeyguardTransitOld(int transit) {
        return isKeyguardGoingAwayTransitOld(transit) || isKeyguardOccludeTransitOld(transit);
    }

    static boolean isTaskTransitOld(int transit) {
        return isTaskOpenTransitOld(transit) || isTaskCloseTransitOld(transit);
    }

    static boolean isTaskCloseTransitOld(int transit) {
        return transit == 9 || transit == 11;
    }

    private static boolean isTaskOpenTransitOld(int transit) {
        return transit == 8 || transit == 16 || transit == 10;
    }

    static boolean isActivityTransitOld(int transit) {
        return transit == 6 || transit == 7 || transit == 18;
    }

    static boolean isTaskFragmentTransitOld(int transit) {
        return transit == 28 || transit == 29 || transit == 30;
    }

    static boolean isChangeTransitOld(int transit) {
        return transit == 27 || transit == 30;
    }

    static boolean isClosingTransitOld(int transit) {
        return transit == 7 || transit == 9 || transit == 12 || transit == 15 || transit == 25 || transit == 26;
    }

    static boolean isNormalTransit(int transit) {
        return transit == 1 || transit == 2 || transit == 3 || transit == 4;
    }

    static boolean isKeyguardTransit(int transit) {
        return transit == 7 || transit == 8 || transit == 9;
    }

    int getKeyguardTransition() {
        if (this.mNextAppTransitionRequests.indexOf(7) != -1) {
            return 7;
        }
        int unoccludeIndex = this.mNextAppTransitionRequests.indexOf(9);
        int occludeIndex = this.mNextAppTransitionRequests.indexOf(8);
        if (unoccludeIndex == -1 && occludeIndex == -1) {
            return 0;
        }
        if (unoccludeIndex == -1 || unoccludeIndex >= occludeIndex) {
            return unoccludeIndex != -1 ? 9 : 8;
        }
        return 0;
    }

    int getFirstAppTransition() {
        for (int i = 0; i < this.mNextAppTransitionRequests.size(); i++) {
            int transit = this.mNextAppTransitionRequests.get(i).intValue();
            if (transit != 0 && !isKeyguardTransit(transit)) {
                return transit;
            }
        }
        return 0;
    }

    boolean containsTransitRequest(int transit) {
        return this.mNextAppTransitionRequests.contains(java.lang.Integer.valueOf(transit));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleAppTransitionTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.DisplayContent dc = this.mDisplayContent;
                if (dc == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                notifyAppTransitionTimeoutLocked();
                if (isTransitionSet() || !dc.mOpeningApps.isEmpty() || !dc.mClosingApps.isEmpty() || !dc.mChangingContainers.isEmpty()) {
                    this.mAppTransitionWrapper.getExtImpl().appTransitionTimeout(this.mService, dc);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_APP_TRANSITIONS_enabled[1]) {
                        long protoLogParam0 = dc.getDisplayId();
                        boolean protoLogParam1 = dc.mAppTransition.isTransitionSet();
                        long protoLogParam2 = dc.mOpeningApps.size();
                        long protoLogParam3 = dc.mClosingApps.size();
                        long protoLogParam4 = dc.mChangingContainers.size();
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 5233255302148535928L, 349, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Boolean.valueOf(protoLogParam1), java.lang.Long.valueOf(protoLogParam2), java.lang.Long.valueOf(protoLogParam3), java.lang.Long.valueOf(protoLogParam4));
                    }
                    long ts = java.lang.System.currentTimeMillis();
                    java.lang.String packageName = null;
                    if (dc.mFocusedApp != null) {
                        packageName = dc.mFocusedApp.packageName;
                    }
                    this.mTheiaManagerExt.sendEvent(259L, ts, 0, 0, 4099L, packageName);
                    setTimeout();
                    this.mService.mWindowPlacerLocked.performSurfacePlacement();
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doAnimationCallback(android.os.IRemoteCallback callback) {
        try {
            callback.sendResult((android.os.Bundle) null);
        } catch (android.os.RemoteException e) {
        }
    }

    private void setAppTransitionFinishedCallbackIfNeeded(android.view.animation.Animation anim) {
        android.os.IRemoteCallback callback = this.mAnimationFinishedCallback;
        if (callback != null && anim != null) {
            anim.setAnimationListener(new com.android.server.wm.AppTransition.AnonymousClass1(callback));
        }
    }

    /* JADX INFO: renamed from: com.android.server.wm.AppTransition$1, reason: invalid class name */
    class AnonymousClass1 implements android.view.animation.Animation.AnimationListener {
        final /* synthetic */ android.os.IRemoteCallback val$callback;

        AnonymousClass1(android.os.IRemoteCallback iRemoteCallback) {
            this.val$callback = iRemoteCallback;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(android.view.animation.Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(android.view.animation.Animation animation) {
            com.android.server.wm.AppTransition.this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.wm.AppTransition$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.AppTransition.doAnimationCallback((android.os.IRemoteCallback) obj);
                }
            }, this.val$callback));
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(android.view.animation.Animation animation) {
        }
    }

    void removeAppTransitionTimeoutCallbacks() {
        this.mHandler.removeCallbacks(this.mHandleAppTransitionTimeoutRunnable);
        this.mAppTransitionWrapper.getExtImpl().removeAppTransitionDelayedCallback(this.mHandler);
    }

    public com.android.server.wm.IAppTransitionWrapper getWrapper() {
        return this.mAppTransitionWrapper;
    }

    private class AppTransitionWrapper implements com.android.server.wm.IAppTransitionWrapper {
        private com.android.server.wm.IAppTransitionExt mAppTransitionExt;

        private AppTransitionWrapper() {
            this.mAppTransitionExt = (com.android.server.wm.IAppTransitionExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IAppTransitionExt.class).base(com.android.server.wm.AppTransition.this).create();
        }

        @Override // com.android.server.wm.IAppTransitionWrapper
        public com.android.server.wm.IAppTransitionExt getExtImpl() {
            return this.mAppTransitionExt;
        }

        @Override // com.android.server.wm.IAppTransitionWrapper
        public java.lang.String getNextAppTransitionPackage() {
            return com.android.server.wm.AppTransition.this.mNextAppTransitionPackage;
        }

        @Override // com.android.server.wm.IAppTransitionWrapper
        public int getNextAppTransitionType() {
            return com.android.server.wm.AppTransition.this.mNextAppTransitionType;
        }
    }
}
