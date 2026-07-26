package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class MagnificationController implements com.android.server.accessibility.magnification.MagnificationConnectionManager.Callback, com.android.server.accessibility.magnification.MagnificationGestureHandler.Callback, com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback, com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "MagnificationController";
    private final android.util.SparseArray<com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks> mAccessibilityCallbacksDelegateArray;
    private final com.android.server.accessibility.magnification.AlwaysOnMagnificationFeatureFlag mAlwaysOnMagnificationFeatureFlag;
    private final com.android.server.accessibility.AccessibilityManagerService mAms;
    private final java.util.concurrent.Executor mBackgroundExecutor;
    private final android.content.Context mContext;
    private final android.util.SparseIntArray mCurrentMagnificationModeArray;
    private com.android.server.accessibility.magnification.FullScreenMagnificationController mFullScreenMagnificationController;
    private final android.util.SparseLongArray mFullScreenModeEnabledTimeArray;
    private final android.util.SparseBooleanArray mIsImeVisibleArray;
    private final android.util.SparseIntArray mLastMagnificationActivatedModeArray;
    private final java.lang.Object mLock;
    private int mMagnificationCapabilities;
    private com.android.server.accessibility.magnification.MagnificationConnectionManager mMagnificationConnectionManager;
    private final android.util.SparseArray<com.android.server.accessibility.magnification.MagnificationController.DisableMagnificationCallback> mMagnificationEndRunnableSparseArray;
    private final com.android.server.accessibility.magnification.MagnificationScaleProvider mScaleProvider;
    private final boolean mSupportWindowMagnification;
    private final android.graphics.PointF mTempPoint;
    private final android.util.SparseArray<java.lang.Integer> mTransitionModes;
    private int mUserId;
    private final android.util.SparseLongArray mWindowModeEnabledTimeArray;

    public interface TransitionCallBack {
        void onResult(int i, boolean z);
    }

    public MagnificationController(com.android.server.accessibility.AccessibilityManagerService ams, java.lang.Object lock, android.content.Context context, com.android.server.accessibility.magnification.MagnificationScaleProvider scaleProvider, java.util.concurrent.Executor backgroundExecutor) {
        this.mTempPoint = new android.graphics.PointF();
        this.mMagnificationEndRunnableSparseArray = new android.util.SparseArray<>();
        this.mMagnificationCapabilities = 1;
        this.mCurrentMagnificationModeArray = new android.util.SparseIntArray();
        this.mLastMagnificationActivatedModeArray = new android.util.SparseIntArray();
        this.mUserId = 0;
        this.mIsImeVisibleArray = new android.util.SparseBooleanArray();
        this.mWindowModeEnabledTimeArray = new android.util.SparseLongArray();
        this.mFullScreenModeEnabledTimeArray = new android.util.SparseLongArray();
        this.mTransitionModes = new android.util.SparseArray<>();
        this.mAccessibilityCallbacksDelegateArray = new android.util.SparseArray<>();
        this.mAms = ams;
        this.mLock = lock;
        this.mContext = context;
        this.mScaleProvider = scaleProvider;
        this.mBackgroundExecutor = backgroundExecutor;
        ((com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class)).getAccessibilityController().setUiChangesForAccessibilityCallbacks(this);
        this.mSupportWindowMagnification = context.getPackageManager().hasSystemFeature("android.software.window_magnification");
        this.mAlwaysOnMagnificationFeatureFlag = new com.android.server.accessibility.magnification.AlwaysOnMagnificationFeatureFlag(context);
        com.android.server.accessibility.magnification.AlwaysOnMagnificationFeatureFlag alwaysOnMagnificationFeatureFlag = this.mAlwaysOnMagnificationFeatureFlag;
        java.util.concurrent.Executor executor = this.mBackgroundExecutor;
        final com.android.server.accessibility.AccessibilityManagerService accessibilityManagerService = this.mAms;
        java.util.Objects.requireNonNull(accessibilityManagerService);
        alwaysOnMagnificationFeatureFlag.addOnChangedListener(executor, new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                accessibilityManagerService.updateAlwaysOnMagnification();
            }
        });
    }

    public MagnificationController(com.android.server.accessibility.AccessibilityManagerService ams, java.lang.Object lock, android.content.Context context, com.android.server.accessibility.magnification.FullScreenMagnificationController fullScreenMagnificationController, com.android.server.accessibility.magnification.MagnificationConnectionManager magnificationConnectionManager, com.android.server.accessibility.magnification.MagnificationScaleProvider scaleProvider, java.util.concurrent.Executor backgroundExecutor) {
        this(ams, lock, context, scaleProvider, backgroundExecutor);
        this.mFullScreenMagnificationController = fullScreenMagnificationController;
        this.mMagnificationConnectionManager = magnificationConnectionManager;
    }

    @Override // com.android.server.accessibility.magnification.MagnificationConnectionManager.Callback
    public void onPerformScaleAction(int displayId, float scale, boolean updatePersistence) {
        if (getFullScreenMagnificationController().isActivated(displayId)) {
            getFullScreenMagnificationController().setScaleAndCenter(displayId, scale, Float.NaN, Float.NaN, false, 0);
            if (updatePersistence) {
                getFullScreenMagnificationController().persistScale(displayId);
                return;
            }
            return;
        }
        if (getMagnificationConnectionManager().isWindowMagnifierEnabled(displayId)) {
            getMagnificationConnectionManager().setScale(displayId, scale);
            if (updatePersistence) {
                getMagnificationConnectionManager().persistScale(displayId);
            }
        }
    }

    @Override // com.android.server.accessibility.magnification.MagnificationConnectionManager.Callback
    public void onAccessibilityActionPerformed(int displayId) {
        updateMagnificationUIControls(displayId, 2);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationGestureHandler.Callback
    public void onTouchInteractionStart(int displayId, int mode) {
        handleUserInteractionChanged(displayId, mode);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationGestureHandler.Callback
    public void onTouchInteractionEnd(int displayId, int mode) {
        handleUserInteractionChanged(displayId, mode);
    }

    private void handleUserInteractionChanged(int displayId, int mode) {
        if (this.mMagnificationCapabilities != 3) {
            return;
        }
        updateMagnificationUIControls(displayId, mode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void updateMagnificationUIControls(int r8, int r9) {
        /*
            r7 = this;
            boolean r0 = r7.isActivated(r8, r9)
            java.lang.Object r1 = r7.mLock
            monitor-enter(r1)
            r2 = 1
            r3 = 3
            r4 = 0
            if (r0 == 0) goto L14
            int r5 = r7.mMagnificationCapabilities     // Catch: java.lang.Throwable -> L12
            if (r5 != r3) goto L14
            r5 = r2
            goto L15
        L12:
            r2 = move-exception
            goto L3e
        L14:
            r5 = r4
        L15:
            if (r0 == 0) goto L21
            int r6 = r7.mMagnificationCapabilities     // Catch: java.lang.Throwable -> L12
            if (r6 == r3) goto L20
            int r3 = r7.mMagnificationCapabilities     // Catch: java.lang.Throwable -> L12
            r6 = 2
            if (r3 != r6) goto L21
        L20:
            goto L22
        L21:
            r2 = r4
        L22:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L12
            if (r5 == 0) goto L2d
            com.android.server.accessibility.magnification.MagnificationConnectionManager r1 = r7.getMagnificationConnectionManager()
            r1.showMagnificationButton(r8, r9)
            goto L34
        L2d:
            com.android.server.accessibility.magnification.MagnificationConnectionManager r1 = r7.getMagnificationConnectionManager()
            r1.removeMagnificationButton(r8)
        L34:
            if (r2 != 0) goto L3d
            com.android.server.accessibility.magnification.MagnificationConnectionManager r1 = r7.getMagnificationConnectionManager()
            r1.removeMagnificationSettingsPanel(r8)
        L3d:
            return
        L3e:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L12
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.accessibility.magnification.MagnificationController.updateMagnificationUIControls(int, int):void");
    }

    public boolean supportWindowMagnification() {
        return this.mSupportWindowMagnification;
    }

    public void transitionMagnificationModeLocked(int displayId, int targetMode, com.android.server.accessibility.magnification.MagnificationController.TransitionCallBack transitionCallBack) {
        if (isActivated(displayId, targetMode)) {
            transitionCallBack.onResult(displayId, true);
            return;
        }
        android.graphics.PointF currentCenter = getCurrentMagnificationCenterLocked(displayId, targetMode);
        com.android.server.accessibility.magnification.MagnificationController.DisableMagnificationCallback animationCallback = getDisableMagnificationEndRunnableLocked(displayId);
        if (currentCenter == null && animationCallback == null) {
            transitionCallBack.onResult(displayId, true);
            return;
        }
        if (animationCallback != null) {
            if (animationCallback.mCurrentMode != targetMode) {
                android.util.Slog.w(TAG, "discard duplicate request");
                return;
            } else {
                animationCallback.restoreToCurrentMagnificationMode();
                return;
            }
        }
        if (currentCenter != null) {
            setTransitionState(java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(targetMode));
            com.android.server.accessibility.magnification.FullScreenMagnificationController screenMagnificationController = getFullScreenMagnificationController();
            com.android.server.accessibility.magnification.MagnificationConnectionManager magnificationConnectionManager = getMagnificationConnectionManager();
            float scale = getTargetModeScaleFromCurrentMagnification(displayId, targetMode);
            com.android.server.accessibility.magnification.MagnificationController.DisableMagnificationCallback animationEndCallback = new com.android.server.accessibility.magnification.MagnificationController.DisableMagnificationCallback(transitionCallBack, displayId, targetMode, scale, currentCenter, true);
            setDisableMagnificationCallbackLocked(displayId, animationEndCallback);
            if (targetMode == 2) {
                screenMagnificationController.reset(displayId, animationEndCallback);
                return;
            } else {
                magnificationConnectionManager.disableWindowMagnification(displayId, false, animationEndCallback);
                return;
            }
        }
        android.util.Slog.w(TAG, "Invalid center, ignore it");
        transitionCallBack.onResult(displayId, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:70:0x0162  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x016b  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0184 A[Catch: all -> 0x0197, TryCatch #5 {all -> 0x0197, blocks: (B:71:0x0165, B:73:0x016e, B:74:0x0176, B:84:0x0195, B:79:0x0184, B:80:0x0189, B:81:0x0191), top: B:96:0x0007 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void transitionMagnificationConfigMode(final int r21, android.accessibilityservice.MagnificationConfig r22, boolean r23, int r24) {
        /*
            Method dump skipped, instruction units count: 409
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.accessibility.magnification.MagnificationController.transitionMagnificationConfigMode(int, android.accessibilityservice.MagnificationConfig, boolean, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$transitionMagnificationConfigMode$0(int displayId, int targetMode, boolean success) {
        this.mAms.changeMagnificationMode(displayId, targetMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTransitionState(java.lang.Integer displayId, java.lang.Integer targetMode) {
        synchronized (this.mLock) {
            if (targetMode == null && displayId == null) {
                this.mTransitionModes.clear();
            } else {
                this.mTransitionModes.put(displayId.intValue(), targetMode);
            }
        }
    }

    private float getTargetModeScaleFromCurrentMagnification(int displayId, int targetMode) {
        if (targetMode == 2) {
            return getFullScreenMagnificationController().getScale(displayId);
        }
        return getMagnificationConnectionManager().getScale(displayId);
    }

    public boolean hasDisableMagnificationCallback(int displayId) {
        synchronized (this.mLock) {
            com.android.server.accessibility.magnification.MagnificationController.DisableMagnificationCallback animationCallback = getDisableMagnificationEndRunnableLocked(displayId);
            return animationCallback != null;
        }
    }

    private void setCurrentMagnificationModeAndSwitchDelegate(int displayId, int mode) {
        this.mCurrentMagnificationModeArray.put(displayId, mode);
        assignMagnificationWindowManagerDelegateByMode(displayId, mode);
    }

    private void assignMagnificationWindowManagerDelegateByMode(int displayId, int mode) {
        if (mode == 1) {
            this.mAccessibilityCallbacksDelegateArray.put(displayId, getFullScreenMagnificationController());
        } else if (mode == 2) {
            this.mAccessibilityCallbacksDelegateArray.put(displayId, getMagnificationConnectionManager());
        } else {
            this.mAccessibilityCallbacksDelegateArray.delete(displayId);
        }
    }

    @Override // com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks
    public void onRectangleOnScreenRequested(int displayId, int left, int top, int right, int bottom) {
        com.android.server.wm.WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks delegate;
        synchronized (this.mLock) {
            delegate = this.mAccessibilityCallbacksDelegateArray.get(displayId);
        }
        if (delegate != null) {
            delegate.onRectangleOnScreenRequested(displayId, left, top, right, bottom);
        }
    }

    @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
    public void onRequestMagnificationSpec(int displayId, int serviceId) {
        com.android.server.accessibility.magnification.MagnificationConnectionManager magnificationConnectionManager;
        synchronized (this.mLock) {
            updateMagnificationUIControls(displayId, 1);
            magnificationConnectionManager = this.mMagnificationConnectionManager;
        }
        if (magnificationConnectionManager != null) {
            this.mMagnificationConnectionManager.disableWindowMagnification(displayId, false);
        }
    }

    @Override // com.android.server.accessibility.magnification.MagnificationConnectionManager.Callback
    public void onWindowMagnificationActivationState(int displayId, boolean activated) {
        long duration;
        float scale;
        if (activated) {
            synchronized (this.mLock) {
                this.mWindowModeEnabledTimeArray.put(displayId, android.os.SystemClock.uptimeMillis());
                setCurrentMagnificationModeAndSwitchDelegate(displayId, 2);
                this.mLastMagnificationActivatedModeArray.put(displayId, 2);
            }
            logMagnificationModeWithImeOnIfNeeded(displayId);
            disableFullScreenMagnificationIfNeeded(displayId);
        } else {
            synchronized (this.mLock) {
                setCurrentMagnificationModeAndSwitchDelegate(displayId, 0);
                duration = android.os.SystemClock.uptimeMillis() - this.mWindowModeEnabledTimeArray.get(displayId);
                scale = this.mMagnificationConnectionManager.getLastActivatedScale(displayId);
            }
            logMagnificationUsageState(2, duration, scale);
        }
        updateMagnificationUIControls(displayId, 2);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationConnectionManager.Callback
    public void onChangeMagnificationMode(int displayId, int magnificationMode) {
        this.mAms.changeMagnificationMode(displayId, magnificationMode);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationConnectionManager.Callback
    public void onSourceBoundsChanged(int displayId, android.graphics.Rect bounds) {
        if (shouldNotifyMagnificationChange(displayId, 2)) {
            this.mMagnificationConnectionManager.onUserMagnificationScaleChanged(this.mUserId, displayId, getMagnificationConnectionManager().getScale(displayId));
            android.accessibilityservice.MagnificationConfig config = new android.accessibilityservice.MagnificationConfig.Builder().setMode(2).setActivated(getMagnificationConnectionManager().isWindowMagnifierEnabled(displayId)).setScale(getMagnificationConnectionManager().getScale(displayId)).setCenterX(bounds.exactCenterX()).setCenterY(bounds.exactCenterY()).build();
            this.mAms.notifyMagnificationChanged(displayId, new android.graphics.Region(bounds), config);
        }
    }

    @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
    public void onFullScreenMagnificationChanged(int displayId, android.graphics.Region region, android.accessibilityservice.MagnificationConfig config) {
        if (shouldNotifyMagnificationChange(displayId, 1)) {
            this.mMagnificationConnectionManager.onUserMagnificationScaleChanged(this.mUserId, displayId, config.getScale());
            this.mAms.notifyMagnificationChanged(displayId, region, config);
        }
    }

    private boolean shouldNotifyMagnificationChange(int displayId, int changeMode) {
        synchronized (this.mLock) {
            boolean fullScreenActivated = this.mFullScreenMagnificationController != null && this.mFullScreenMagnificationController.isActivated(displayId);
            boolean windowEnabled = this.mMagnificationConnectionManager != null && this.mMagnificationConnectionManager.isWindowMagnifierEnabled(displayId);
            java.lang.Integer transitionMode = this.mTransitionModes.get(displayId);
            if (((changeMode == 1 && fullScreenActivated) || (changeMode == 2 && windowEnabled)) && transitionMode == null) {
                return true;
            }
            if (fullScreenActivated || windowEnabled || transitionMode != null) {
                return transitionMode != null && changeMode == transitionMode.intValue();
            }
            return true;
        }
    }

    private void disableFullScreenMagnificationIfNeeded(int displayId) {
        com.android.server.accessibility.magnification.FullScreenMagnificationController fullScreenMagnificationController = getFullScreenMagnificationController();
        boolean isMagnifyByExternalRequest = fullScreenMagnificationController.getIdOfLastServiceToMagnify(displayId) > 0;
        if (isMagnifyByExternalRequest || isActivated(displayId, 1)) {
            fullScreenMagnificationController.reset(displayId, false);
        }
    }

    @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
    public void onFullScreenMagnificationActivationState(int displayId, boolean activated) {
        long duration;
        float scale;
        if (com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder()) {
            getMagnificationConnectionManager().onFullscreenMagnificationActivationChanged(displayId, activated);
        }
        if (activated) {
            synchronized (this.mLock) {
                this.mFullScreenModeEnabledTimeArray.put(displayId, android.os.SystemClock.uptimeMillis());
                setCurrentMagnificationModeAndSwitchDelegate(displayId, 1);
                this.mLastMagnificationActivatedModeArray.put(displayId, 1);
            }
            logMagnificationModeWithImeOnIfNeeded(displayId);
            disableWindowMagnificationIfNeeded(displayId);
        } else {
            synchronized (this.mLock) {
                setCurrentMagnificationModeAndSwitchDelegate(displayId, 0);
                duration = android.os.SystemClock.uptimeMillis() - this.mFullScreenModeEnabledTimeArray.get(displayId);
                scale = this.mFullScreenMagnificationController.getLastActivatedScale(displayId);
            }
            logMagnificationUsageState(1, duration, scale);
        }
        updateMagnificationUIControls(displayId, 1);
    }

    private void disableWindowMagnificationIfNeeded(int displayId) {
        com.android.server.accessibility.magnification.MagnificationConnectionManager magnificationConnectionManager = getMagnificationConnectionManager();
        if (isActivated(displayId, 2)) {
            magnificationConnectionManager.disableWindowMagnification(displayId, false);
        }
    }

    @Override // com.android.server.accessibility.magnification.FullScreenMagnificationController.MagnificationInfoChangedCallback
    public void onImeWindowVisibilityChanged(int displayId, boolean shown) {
        synchronized (this.mLock) {
            this.mIsImeVisibleArray.put(displayId, shown);
        }
        getMagnificationConnectionManager().onImeWindowVisibilityChanged(displayId, shown);
        logMagnificationModeWithImeOnIfNeeded(displayId);
    }

    public int getLastMagnificationActivatedMode(int displayId) {
        int i;
        synchronized (this.mLock) {
            i = this.mLastMagnificationActivatedModeArray.get(displayId, 1);
        }
        return i;
    }

    public void logMagnificationUsageState(int mode, long duration, float scale) {
        com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logMagnificationUsageState(mode, duration, scale);
    }

    public void logMagnificationModeWithIme(int mode) {
        com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logMagnificationModeWithImeOn(mode);
    }

    public void updateUserIdIfNeeded(int userId) {
        com.android.server.accessibility.magnification.FullScreenMagnificationController fullMagnificationController;
        com.android.server.accessibility.magnification.MagnificationConnectionManager magnificationConnectionManager;
        if (this.mUserId == userId) {
            return;
        }
        this.mUserId = userId;
        synchronized (this.mLock) {
            fullMagnificationController = this.mFullScreenMagnificationController;
            magnificationConnectionManager = this.mMagnificationConnectionManager;
            this.mAccessibilityCallbacksDelegateArray.clear();
            this.mCurrentMagnificationModeArray.clear();
            this.mLastMagnificationActivatedModeArray.clear();
            this.mIsImeVisibleArray.clear();
        }
        this.mScaleProvider.onUserChanged(userId);
        if (fullMagnificationController != null) {
            fullMagnificationController.resetAllIfNeeded(false);
        }
        if (magnificationConnectionManager != null) {
            magnificationConnectionManager.disableAllWindowMagnifiers();
        }
    }

    public void onDisplayRemoved(int displayId) {
        synchronized (this.mLock) {
            if (this.mFullScreenMagnificationController != null) {
                this.mFullScreenMagnificationController.onDisplayRemoved(displayId);
            }
            if (this.mMagnificationConnectionManager != null) {
                this.mMagnificationConnectionManager.onDisplayRemoved(displayId);
            }
            this.mAccessibilityCallbacksDelegateArray.delete(displayId);
            this.mCurrentMagnificationModeArray.delete(displayId);
            this.mLastMagnificationActivatedModeArray.delete(displayId);
            this.mIsImeVisibleArray.delete(displayId);
        }
        this.mScaleProvider.onDisplayRemoved(displayId);
    }

    public void onUserRemoved(int userId) {
        this.mScaleProvider.onUserRemoved(userId);
    }

    public void setMagnificationCapabilities(int capabilities) {
        this.mMagnificationCapabilities = capabilities;
    }

    public void setMagnificationFollowTypingEnabled(boolean enabled) {
        getMagnificationConnectionManager().setMagnificationFollowTypingEnabled(enabled);
        getFullScreenMagnificationController().setMagnificationFollowTypingEnabled(enabled);
    }

    public void setAlwaysOnMagnificationEnabled(boolean enabled) {
        getFullScreenMagnificationController().setAlwaysOnMagnificationEnabled(enabled);
    }

    public boolean isAlwaysOnMagnificationFeatureFlagEnabled() {
        return this.mAlwaysOnMagnificationFeatureFlag.isFeatureFlagEnabled();
    }

    private com.android.server.accessibility.magnification.MagnificationController.DisableMagnificationCallback getDisableMagnificationEndRunnableLocked(int displayId) {
        return this.mMagnificationEndRunnableSparseArray.get(displayId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisableMagnificationCallbackLocked(int displayId, com.android.server.accessibility.magnification.MagnificationController.DisableMagnificationCallback callback) {
        this.mMagnificationEndRunnableSparseArray.put(displayId, callback);
    }

    private void logMagnificationModeWithImeOnIfNeeded(int displayId) {
        synchronized (this.mLock) {
            int currentActivateMode = this.mCurrentMagnificationModeArray.get(displayId, 0);
            if (this.mIsImeVisibleArray.get(displayId, false) && currentActivateMode != 0) {
                logMagnificationModeWithIme(currentActivateMode);
            }
        }
    }

    public com.android.server.accessibility.magnification.FullScreenMagnificationController getFullScreenMagnificationController() {
        synchronized (this.mLock) {
            if (this.mFullScreenMagnificationController == null) {
                this.mFullScreenMagnificationController = new com.android.server.accessibility.magnification.FullScreenMagnificationController(this.mContext, this.mAms.getTraceManager(), this.mLock, this, this.mScaleProvider, this.mBackgroundExecutor, new java.util.function.Supplier() { // from class: com.android.server.accessibility.magnification.MagnificationController$$ExternalSyntheticLambda0
                    @Override // java.util.function.Supplier
                    public final java.lang.Object get() {
                        return this.f$0.lambda$getFullScreenMagnificationController$1();
                    }
                });
            }
        }
        return this.mFullScreenMagnificationController;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$getFullScreenMagnificationController$1() {
        return java.lang.Boolean.valueOf(isMagnificationSystemUIConnectionReady());
    }

    private boolean isMagnificationSystemUIConnectionReady() {
        return isMagnificationConnectionManagerInitialized() && getMagnificationConnectionManager().waitConnectionWithTimeoutIfNeeded();
    }

    public boolean isFullScreenMagnificationControllerInitialized() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mFullScreenMagnificationController != null;
        }
        return z;
    }

    public com.android.server.accessibility.magnification.MagnificationConnectionManager getMagnificationConnectionManager() {
        com.android.server.accessibility.magnification.MagnificationConnectionManager magnificationConnectionManager;
        synchronized (this.mLock) {
            if (this.mMagnificationConnectionManager == null) {
                this.mMagnificationConnectionManager = new com.android.server.accessibility.magnification.MagnificationConnectionManager(this.mContext, this.mLock, this, this.mAms.getTraceManager(), this.mScaleProvider);
            }
            magnificationConnectionManager = this.mMagnificationConnectionManager;
        }
        return magnificationConnectionManager;
    }

    private boolean isMagnificationConnectionManagerInitialized() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mMagnificationConnectionManager != null;
        }
        return z;
    }

    private android.graphics.PointF getCurrentMagnificationCenterLocked(int displayId, int targetMode) {
        if (targetMode == 1) {
            if (this.mMagnificationConnectionManager == null || !this.mMagnificationConnectionManager.isWindowMagnifierEnabled(displayId)) {
                return null;
            }
            this.mTempPoint.set(this.mMagnificationConnectionManager.getCenterX(displayId), this.mMagnificationConnectionManager.getCenterY(displayId));
        } else {
            if (this.mFullScreenMagnificationController == null || !this.mFullScreenMagnificationController.isActivated(displayId)) {
                return null;
            }
            this.mTempPoint.set(this.mFullScreenMagnificationController.getCenterX(displayId), this.mFullScreenMagnificationController.getCenterY(displayId));
        }
        return this.mTempPoint;
    }

    public boolean isActivated(int displayId, int mode) {
        boolean isActivated = false;
        if (mode == 1) {
            synchronized (this.mLock) {
                if (this.mFullScreenMagnificationController == null) {
                    return false;
                }
                isActivated = this.mFullScreenMagnificationController.isActivated(displayId);
            }
        } else if (mode == 2) {
            synchronized (this.mLock) {
                if (this.mMagnificationConnectionManager == null) {
                    return false;
                }
                isActivated = this.mMagnificationConnectionManager.isWindowMagnifierEnabled(displayId);
            }
        }
        return isActivated;
    }

    private final class DisableMagnificationCallback implements android.view.accessibility.MagnificationAnimationCallback {
        private final boolean mAnimate;
        private final int mCurrentMode;
        private final float mCurrentScale;
        private final int mDisplayId;
        private final int mTargetMode;
        private final com.android.server.accessibility.magnification.MagnificationController.TransitionCallBack mTransitionCallBack;
        private boolean mExpired = false;
        private final android.graphics.PointF mCurrentCenter = new android.graphics.PointF();

        DisableMagnificationCallback(com.android.server.accessibility.magnification.MagnificationController.TransitionCallBack transitionCallBack, int displayId, int targetMode, float scale, android.graphics.PointF currentCenter, boolean animate) {
            this.mTransitionCallBack = transitionCallBack;
            this.mDisplayId = displayId;
            this.mTargetMode = targetMode;
            this.mCurrentMode = this.mTargetMode ^ 3;
            this.mCurrentScale = scale;
            this.mCurrentCenter.set(currentCenter);
            this.mAnimate = animate;
        }

        public void onResult(boolean success) {
            synchronized (com.android.server.accessibility.magnification.MagnificationController.this.mLock) {
                if (this.mExpired) {
                    return;
                }
                setExpiredAndRemoveFromListLocked();
                com.android.server.accessibility.magnification.MagnificationController.this.setTransitionState(java.lang.Integer.valueOf(this.mDisplayId), null);
                if (success) {
                    adjustCurrentCenterIfNeededLocked();
                    applyMagnificationModeLocked(this.mTargetMode);
                } else {
                    com.android.server.accessibility.magnification.FullScreenMagnificationController screenMagnificationController = com.android.server.accessibility.magnification.MagnificationController.this.getFullScreenMagnificationController();
                    if (this.mCurrentMode == 1 && !screenMagnificationController.isActivated(this.mDisplayId)) {
                        android.accessibilityservice.MagnificationConfig.Builder configBuilder = new android.accessibilityservice.MagnificationConfig.Builder();
                        android.graphics.Region region = new android.graphics.Region();
                        configBuilder.setMode(1).setActivated(screenMagnificationController.isActivated(this.mDisplayId)).setScale(screenMagnificationController.getScale(this.mDisplayId)).setCenterX(screenMagnificationController.getCenterX(this.mDisplayId)).setCenterY(screenMagnificationController.getCenterY(this.mDisplayId));
                        screenMagnificationController.getMagnificationRegion(this.mDisplayId, region);
                        com.android.server.accessibility.magnification.MagnificationController.this.mAms.notifyMagnificationChanged(this.mDisplayId, region, configBuilder.build());
                    }
                }
                com.android.server.accessibility.magnification.MagnificationController.this.updateMagnificationUIControls(this.mDisplayId, this.mTargetMode);
                if (this.mTransitionCallBack != null) {
                    this.mTransitionCallBack.onResult(this.mDisplayId, success);
                }
            }
        }

        private void adjustCurrentCenterIfNeededLocked() {
            if (this.mTargetMode == 2) {
                return;
            }
            android.graphics.Region outRegion = new android.graphics.Region();
            com.android.server.accessibility.magnification.MagnificationController.this.getFullScreenMagnificationController().getMagnificationRegion(this.mDisplayId, outRegion);
            if (outRegion.contains((int) this.mCurrentCenter.x, (int) this.mCurrentCenter.y)) {
                return;
            }
            android.graphics.Rect bounds = outRegion.getBounds();
            this.mCurrentCenter.set(bounds.exactCenterX(), bounds.exactCenterY());
        }

        void restoreToCurrentMagnificationMode() {
            synchronized (com.android.server.accessibility.magnification.MagnificationController.this.mLock) {
                if (this.mExpired) {
                    return;
                }
                setExpiredAndRemoveFromListLocked();
                com.android.server.accessibility.magnification.MagnificationController.this.setTransitionState(java.lang.Integer.valueOf(this.mDisplayId), null);
                applyMagnificationModeLocked(this.mCurrentMode);
                com.android.server.accessibility.magnification.MagnificationController.this.updateMagnificationUIControls(this.mDisplayId, this.mCurrentMode);
                if (this.mTransitionCallBack != null) {
                    this.mTransitionCallBack.onResult(this.mDisplayId, true);
                }
            }
        }

        void setExpiredAndRemoveFromListLocked() {
            this.mExpired = true;
            com.android.server.accessibility.magnification.MagnificationController.this.setDisableMagnificationCallbackLocked(this.mDisplayId, null);
        }

        private void applyMagnificationModeLocked(int mode) {
            if (mode == 1) {
                com.android.server.accessibility.magnification.FullScreenMagnificationController fullScreenMagnificationController = com.android.server.accessibility.magnification.MagnificationController.this.getFullScreenMagnificationController();
                if (!fullScreenMagnificationController.isRegistered(this.mDisplayId)) {
                    fullScreenMagnificationController.register(this.mDisplayId);
                }
                fullScreenMagnificationController.setScaleAndCenter(this.mDisplayId, this.mCurrentScale, this.mCurrentCenter.x, this.mCurrentCenter.y, this.mAnimate, 0);
                return;
            }
            com.android.server.accessibility.magnification.MagnificationController.this.getMagnificationConnectionManager().enableWindowMagnification(this.mDisplayId, this.mCurrentScale, this.mCurrentCenter.x, this.mCurrentCenter.y, this.mAnimate ? STUB_ANIMATION_CALLBACK : null, 0);
        }
    }
}
