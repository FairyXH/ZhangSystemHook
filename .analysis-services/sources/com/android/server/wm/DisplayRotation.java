package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class DisplayRotation {
    private static final int ALLOW_ALL_ROTATIONS_DISABLED = 0;
    private static final int ALLOW_ALL_ROTATIONS_ENABLED = 1;
    private static final int ALLOW_ALL_ROTATIONS_UNDEFINED = -1;
    private static final int CAMERA_ROTATION_DISABLED = 0;
    private static final int CAMERA_ROTATION_ENABLED = 1;
    private static final int FOLDING_RECOMPUTE_CONFIG_DELAY_MS = 800;
    private static final int ROTATION_UNDEFINED = -1;
    private static final java.lang.String TAG = "WindowManager";
    public final boolean isDefaultDisplay;
    private int mAllowAllRotations;
    private final boolean mAllowRotationResolver;
    private boolean mAllowSeamlessRotationDespiteNavBarMoving;
    private int mCameraRotationMode;
    private final int mCarDockRotation;
    private final com.android.server.wm.DisplayRotationImmersiveAppCompatPolicy mCompatPolicyForImmersiveApps;
    private final android.content.Context mContext;
    private int mCurrentAppOrientation;
    final java.lang.Runnable mDefaultDisplayRotationChangedCallback;
    private boolean mDefaultFixedToUserRotation;
    private int mDeferredRotationPauseCount;
    private int mDemoHdmiRotation;
    private boolean mDemoHdmiRotationLock;
    private int mDemoRotation;
    private boolean mDemoRotationLock;
    private final int mDeskDockRotation;
    private final com.android.server.wm.DeviceStateController mDeviceStateController;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private final com.android.server.wm.DisplayPolicy mDisplayPolicy;
    private final com.android.server.wm.DisplayRotationCoordinator mDisplayRotationCoordinator;
    private com.android.server.wm.IDisplayRotationExt mDisplayRotationExt;
    public com.android.server.wm.IDisplayRotationSocExt mDisplayRotationSocExt;
    private final com.android.server.wm.DisplayWindowSettings mDisplayWindowSettings;
    private int mFixedToUserRotation;
    final com.android.server.wm.DisplayRotation.FoldController mFoldController;
    int mLandscapeRotation;
    private int mLastOrientation;
    int mLastSensorRotation;
    private final int mLidOpenRotation;
    private final java.lang.Object mLock;
    private com.android.server.wm.DisplayRotation.OrientationListener mOrientationListener;
    int mPortraitRotation;
    private boolean mRotatingSeamlessly;
    private int mRotation;
    private int mRotationChoiceShownToUserForConfirmation;
    private final com.android.server.wm.DisplayRotation.RotationHistory mRotationHistory;
    private final com.android.server.wm.DisplayRotation.RotationLockHistory mRotationLockHistory;
    private int mSeamlessRotationCount;
    int mSeascapeRotation;
    private final com.android.server.wm.WindowManagerService mService;
    private com.android.server.wm.DisplayRotation.SettingsObserver mSettingsObserver;
    private int mShowRotationSuggestions;
    private com.android.server.statusbar.StatusBarManagerInternal mStatusBarManagerInternal;
    private final boolean mSupportAutoRotation;
    private final com.android.server.wm.DisplayRotation.RotationAnimationPair mTmpRotationAnim;
    private final int mUndockedHdmiRotation;
    int mUpsideDownRotation;
    private int mUserRotation;
    private int mUserRotationMode;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface AllowAllRotations {
    }

    private static class RotationAnimationPair {
        int mEnter;
        int mExit;

        private RotationAnimationPair() {
        }
    }

    DisplayRotation(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent, android.view.DisplayAddress displayAddress, com.android.server.wm.DeviceStateController deviceStateController, com.android.server.wm.DisplayRotationCoordinator displayRotationCoordinator) {
        this(service, displayContent, displayAddress, displayContent.getDisplayPolicy(), service.mDisplayWindowSettings, service.mContext, service.getWindowManagerLock(), deviceStateController, displayRotationCoordinator);
    }

    /* JADX WARN: Multi-variable type inference failed */
    DisplayRotation(com.android.server.wm.WindowManagerService windowManagerService, com.android.server.wm.DisplayContent displayContent, android.view.DisplayAddress displayAddress, com.android.server.wm.DisplayPolicy displayPolicy, com.android.server.wm.DisplayWindowSettings displayWindowSettings, android.content.Context context, java.lang.Object obj, com.android.server.wm.DeviceStateController deviceStateController, com.android.server.wm.DisplayRotationCoordinator displayRotationCoordinator) {
        this.mDisplayRotationExt = (com.android.server.wm.IDisplayRotationExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDisplayRotationExt.class).base(this).create();
        this.mDisplayRotationSocExt = (com.android.server.wm.IDisplayRotationSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDisplayRotationSocExt.class).base(this).create();
        this.mTmpRotationAnim = new com.android.server.wm.DisplayRotation.RotationAnimationPair();
        this.mRotationHistory = new com.android.server.wm.DisplayRotation.RotationHistory();
        this.mRotationLockHistory = new com.android.server.wm.DisplayRotation.RotationLockHistory();
        this.mCurrentAppOrientation = -1;
        this.mLastOrientation = -1;
        this.mLastSensorRotation = -1;
        this.mRotationChoiceShownToUserForConfirmation = -1;
        this.mAllowAllRotations = -1;
        this.mUserRotationMode = 0;
        this.mUserRotation = 0;
        this.mCameraRotationMode = 0;
        this.mFixedToUserRotation = 0;
        this.mService = windowManagerService;
        this.mDisplayContent = displayContent;
        this.mDisplayPolicy = displayPolicy;
        this.mDisplayWindowSettings = displayWindowSettings;
        this.mContext = context;
        this.mLock = obj;
        this.mDeviceStateController = deviceStateController;
        this.isDefaultDisplay = displayContent.isDefaultDisplay;
        this.mCompatPolicyForImmersiveApps = initImmersiveAppCompatPolicy(windowManagerService, displayContent);
        this.mSupportAutoRotation = this.mContext.getResources().getBoolean(android.R.bool.config_showNotificationForBackgroundUserAlarms);
        this.mAllowRotationResolver = this.mContext.getResources().getBoolean(android.R.bool.config_allowFloatingWindowsFillScreen);
        this.mLidOpenRotation = readRotation(android.R.integer.config_jobSchedulerInactivityIdleThreshold);
        this.mCarDockRotation = readRotation(android.R.integer.config_cameraLaunchGestureSensorType);
        this.mDeskDockRotation = readRotation(android.R.integer.config_defaultRingVibrationIntensity);
        this.mUndockedHdmiRotation = readRotation(android.R.integer.config_settingsKeyBehavior);
        int defaultDisplayRotation = readDefaultDisplayRotation(displayAddress, displayContent);
        this.mRotation = defaultDisplayRotation;
        this.mDisplayRotationCoordinator = displayRotationCoordinator;
        if (this.isDefaultDisplay) {
            this.mDisplayRotationCoordinator.setDefaultDisplayDefaultRotation(this.mRotation);
        }
        this.mDefaultDisplayRotationChangedCallback = new java.lang.Runnable() { // from class: com.android.server.wm.DisplayRotation$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.updateRotationAndSendNewConfigIfChanged();
            }
        };
        if (com.android.server.wm.DisplayRotationCoordinator.isSecondaryInternalDisplay(displayContent) && this.mDeviceStateController.shouldMatchBuiltInDisplayOrientationToReverseDefaultDisplay()) {
            this.mDisplayRotationCoordinator.setDefaultDisplayRotationChangedCallback(this.mDefaultDisplayRotationChangedCallback);
        }
        if (this.isDefaultDisplay) {
            android.os.Handler handler = com.android.server.UiThread.getHandler();
            this.mOrientationListener = new com.android.server.wm.DisplayRotation.OrientationListener(this.mContext, handler, defaultDisplayRotation);
            this.mOrientationListener.setCurrentRotation(this.mRotation);
            this.mSettingsObserver = new com.android.server.wm.DisplayRotation.SettingsObserver(handler);
            this.mSettingsObserver.observe();
            if (this.mSupportAutoRotation && isFoldable(this.mContext)) {
                this.mFoldController = new com.android.server.wm.DisplayRotation.FoldController();
            } else {
                this.mFoldController = null;
            }
        } else {
            if (this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent)) {
                android.os.Handler handler2 = com.android.server.UiThread.getHandler();
                this.mOrientationListener = new com.android.server.wm.DisplayRotation.OrientationListener(this.mContext, handler2, defaultDisplayRotation);
                this.mOrientationListener.setCurrentRotation(this.mRotation);
                this.mSettingsObserver = new com.android.server.wm.DisplayRotation.SettingsObserver(handler2);
                this.mSettingsObserver.observe();
                this.mDisplayRotationExt.registerFoldStateListener(this.mContext, handler2, this.mLock);
            }
            this.mFoldController = null;
        }
        if (this.isDefaultDisplay) {
            this.mRotation = android.os.SystemProperties.getInt("ro.panel.pad_orientation", 0) / 90;
            this.mUserRotation = this.mRotation;
            this.mDisplayRotationSocExt.hookRegisterWifiDisplay(this.mContext, this.mService);
        }
        int mirageInitialRotation = this.mDisplayRotationExt.getMirageInitialRotation(this.mDisplayContent.getDisplayId());
        if (mirageInitialRotation != -1) {
            android.util.Slog.d(TAG, "Set init rotation " + mirageInitialRotation + " for display " + this.mDisplayContent.mDisplayId);
            this.mRotation = mirageInitialRotation;
        }
    }

    private static boolean isFoldable(android.content.Context context) {
        return context.getResources().getIntArray(android.R.array.config_face_acquire_vendor_keyguard_ignorelist).length > 0;
    }

    com.android.server.wm.DisplayRotationImmersiveAppCompatPolicy initImmersiveAppCompatPolicy(com.android.server.wm.WindowManagerService service, com.android.server.wm.DisplayContent displayContent) {
        return com.android.server.wm.DisplayRotationImmersiveAppCompatPolicy.createIfNeeded(service.mLetterboxConfiguration, this, displayContent);
    }

    private int readDefaultDisplayRotation(android.view.DisplayAddress displayAddress, com.android.server.wm.DisplayContent displayContent) {
        java.lang.String syspropValue = "";
        if (displayAddress instanceof android.view.DisplayAddress.Physical) {
            android.view.DisplayAddress.Physical physicalAddress = (android.view.DisplayAddress.Physical) displayAddress;
            syspropValue = android.os.SystemProperties.get("ro.bootanim.set_orientation_" + physicalAddress.getPhysicalDisplayId(), "");
        }
        if ("".equals(syspropValue) && displayContent.isDefaultDisplay) {
            syspropValue = android.os.SystemProperties.get("ro.bootanim.set_orientation_logical_" + displayContent.getDisplayId(), "");
        }
        if (syspropValue.equals("ORIENTATION_90")) {
            return 1;
        }
        if (syspropValue.equals("ORIENTATION_180")) {
            return 2;
        }
        if (syspropValue.equals("ORIENTATION_270")) {
            return 3;
        }
        return 0;
    }

    private int readRotation(int resID) {
        try {
            int rotation = this.mContext.getResources().getInteger(resID);
            switch (rotation) {
                case 0:
                    return 0;
                case 90:
                    return 1;
                case 180:
                    return 2;
                case 270:
                    return 3;
                default:
                    return -1;
            }
        } catch (android.content.res.Resources.NotFoundException e) {
            return -1;
        }
    }

    boolean useDefaultSettingsProvider() {
        return this.isDefaultDisplay;
    }

    void updateUserDependentConfiguration(android.content.res.Resources currentUserRes) {
        this.mAllowSeamlessRotationDespiteNavBarMoving = currentUserRes.getBoolean(android.R.bool.config_allowNormalBrightnessForDozePolicy);
    }

    void configure(int width, int height) {
        android.content.res.Resources res = this.mContext.getResources();
        if (width > height) {
            this.mLandscapeRotation = 0;
            this.mSeascapeRotation = 2;
            if (res.getBoolean(android.R.bool.config_permissionsIndividuallyControlled)) {
                this.mPortraitRotation = 1;
                this.mUpsideDownRotation = 3;
            } else {
                this.mPortraitRotation = 3;
                this.mUpsideDownRotation = 1;
            }
        } else {
            this.mPortraitRotation = 0;
            this.mUpsideDownRotation = 2;
            if (res.getBoolean(android.R.bool.config_permissionsIndividuallyControlled)) {
                this.mLandscapeRotation = 3;
                this.mSeascapeRotation = 1;
            } else {
                this.mLandscapeRotation = 1;
                this.mSeascapeRotation = 3;
            }
        }
        if ("portrait".equals(android.os.SystemProperties.get("persist.demo.hdmirotation"))) {
            this.mDemoHdmiRotation = this.mPortraitRotation;
        } else {
            this.mDemoHdmiRotation = this.mLandscapeRotation;
        }
        this.mDemoHdmiRotationLock = android.os.SystemProperties.getBoolean("persist.demo.hdmirotationlock", false);
        if ("portrait".equals(android.os.SystemProperties.get("persist.demo.remoterotation"))) {
            this.mDemoRotation = this.mPortraitRotation;
        } else {
            this.mDemoRotation = this.mLandscapeRotation;
        }
        this.mDemoRotationLock = android.os.SystemProperties.getBoolean("persist.demo.rotationlock", false);
        boolean isCar = this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
        boolean isTv = this.mContext.getPackageManager().hasSystemFeature("android.software.leanback");
        this.mDefaultFixedToUserRotation = (isCar || isTv || this.mService.mIsPc || this.mDisplayContent.forceDesktopMode() || !this.mDisplayContent.shouldRotateWithContent()) && !"true".equals(android.os.SystemProperties.get("config.override_forced_orient"));
    }

    void applyCurrentRotation(int rotation) {
        this.mRotationHistory.addRecord(this, rotation);
        if (this.mOrientationListener != null) {
            this.mOrientationListener.setCurrentRotation(rotation);
        }
    }

    void setRotation(int rotation) {
        this.mRotation = rotation;
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            android.util.Slog.w(TAG, "setRotation = " + rotation + "; callers:" + android.os.Debug.getCallers(30));
        }
    }

    int getRotation() {
        return this.mRotation;
    }

    int getLastOrientation() {
        return this.mLastOrientation;
    }

    boolean updateOrientation(int newOrientation, boolean forceUpdate) {
        if (newOrientation == this.mLastOrientation && !forceUpdate) {
            if (this.mDisplayRotationExt.checkForceUpdate(this.mDisplayContent)) {
                android.util.Slog.d(TAG, "still update rotation even if orientation no changed.");
            } else {
                return false;
            }
        }
        com.android.server.wm.utils.LogUtil.sDebugD(TAG, "updateOrientation: mLastOrientation = " + android.content.pm.ActivityInfo.screenOrientationToString(this.mLastOrientation) + "(" + this.mLastOrientation + ") newOrientation = " + android.content.pm.ActivityInfo.screenOrientationToString(newOrientation) + "(" + newOrientation + ") forceUpdate = " + forceUpdate + " LastOrientationSource = " + this.mDisplayContent.getLastOrientationSource());
        this.mLastOrientation = newOrientation;
        if (newOrientation != this.mCurrentAppOrientation) {
            this.mCurrentAppOrientation = newOrientation;
            if (this.isDefaultDisplay) {
                updateOrientationListenerLw();
            }
        }
        return updateRotationUnchecked(forceUpdate);
    }

    boolean updateRotationAndSendNewConfigIfChanged() {
        boolean changed = updateRotationUnchecked(false);
        if (changed) {
            this.mDisplayContent.sendNewConfiguration();
        }
        return changed;
    }

    boolean updateRotationUnchecked(boolean forceUpdate) {
        java.lang.String str;
        int displayId = this.mDisplayContent.getDisplayId();
        if (this.mDisplayRotationExt.hasMaskAnimation()) {
            android.util.Slog.v(TAG, "Deferring rotation, mask animation in progress.");
            return false;
        }
        if (this.mDisplayRotationExt.hasFlexibleAnimation()) {
            android.util.Slog.v(TAG, "Deferring rotation, flexible animation(eg.drag,close) in progress.");
            return false;
        }
        if (!forceUpdate) {
            if (this.mDeferredRotationPauseCount > 0) {
                if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -6776561147903919733L, 0, null, null);
                    }
                } else {
                    android.util.Slog.d(TAG, "Deferring rotation, rotation is paused.");
                }
                return false;
            }
            if (this.mDisplayContent.inTransition() && this.mDisplayContent.getDisplayPolicy().isScreenOnFully() && !this.mDisplayContent.mTransitionController.useShellTransitionsRotation()) {
                if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 7439675997626642740L, 0, null, null);
                    }
                } else {
                    android.util.Slog.d(TAG, "Deferring rotation, animation in progress.");
                }
                android.util.Slog.d(TAG, "animation is in process, force update in next orientation update.");
                this.mDisplayRotationExt.setForceUpdateRotation(true);
                return false;
            }
            if (this.mService.mDisplayFrozen && !this.mDisplayRotationExt.enableRequestOrientationWhenDeviceFolding(this.mDisplayContent)) {
                if (com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION.isLogToLogcat()) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 1104181226551849840L, 0, null, null);
                    }
                } else {
                    android.util.Slog.d(TAG, "Deferring rotation, still finishing previous rotation");
                }
                android.util.Slog.d(TAG, "previous rotation is finishing, force update in next orientation update.");
                this.mDisplayRotationExt.setForceUpdateRotation(true);
                return false;
            }
            if (!this.mDisplayContent.mFixedRotationTransitionListener.shouldDeferRotation()) {
                if (this.mDisplayRotationExt.shouldDeferRotation(this.mDisplayContent, this.mLastOrientation)) {
                    this.mLastOrientation = -2;
                    return false;
                }
            } else {
                this.mLastOrientation = -2;
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                    android.util.Slog.d(TAG, "updateRotationUnchecked :ignore rotation, recents animation running");
                }
                return false;
            }
        }
        if (this.mService.getWrapper().getExtImpl().isRotationLockForBootAnimation()) {
            android.util.Slog.v(TAG, "Do not rotation when shutdown");
            return false;
        }
        if (!this.mService.mDisplayEnabled) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -2222079183499215612L, 0, null, null);
            }
            return false;
        }
        int oldRotation = this.mRotation;
        int lastOrientation = this.mLastOrientation;
        int rotation = rotationForOrientation(lastOrientation, oldRotation);
        if (this.mFoldController != null && this.mFoldController.shouldRevertOverriddenRotation()) {
            rotation = this.mFoldController.revertOverriddenRotation();
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(android.view.Surface.rotationToString(rotation));
                java.lang.String protoLogParam1 = java.lang.String.valueOf(android.view.Surface.rotationToString(oldRotation));
                java.lang.String protoLogParam2 = java.lang.String.valueOf(android.view.Surface.rotationToString(rotation));
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 662988298513100908L, 0, null, protoLogParam0, protoLogParam1, protoLogParam2);
            }
        }
        if (com.android.server.wm.DisplayRotationCoordinator.isSecondaryInternalDisplay(this.mDisplayContent) && this.mDeviceStateController.shouldMatchBuiltInDisplayOrientationToReverseDefaultDisplay() && !this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent)) {
            rotation = android.util.RotationUtils.reverseRotationDirectionAroundZAxis(this.mDisplayRotationCoordinator.getDefaultDisplayCurrentRotation());
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
            java.lang.String protoLogParam02 = java.lang.String.valueOf(android.view.Surface.rotationToString(rotation));
            long protoLogParam12 = rotation;
            long protoLogParam22 = displayId;
            java.lang.String protoLogParam3 = java.lang.String.valueOf(android.content.pm.ActivityInfo.screenOrientationToString(lastOrientation));
            long protoLogParam4 = lastOrientation;
            java.lang.String protoLogParam5 = java.lang.String.valueOf(android.view.Surface.rotationToString(oldRotation));
            str = TAG;
            long protoLogParam6 = oldRotation;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -7113483678655694375L, 4372, null, protoLogParam02, java.lang.Long.valueOf(protoLogParam12), java.lang.Long.valueOf(protoLogParam22), protoLogParam3, java.lang.Long.valueOf(protoLogParam4), protoLogParam5, java.lang.Long.valueOf(protoLogParam6));
        } else {
            str = TAG;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
            long protoLogParam03 = displayId;
            java.lang.String protoLogParam13 = java.lang.String.valueOf(android.content.pm.ActivityInfo.screenOrientationToString(lastOrientation));
            long protoLogParam23 = lastOrientation;
            java.lang.String protoLogParam32 = java.lang.String.valueOf(android.view.Surface.rotationToString(rotation));
            long protoLogParam42 = rotation;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -8809129029906317617L, 273, null, java.lang.Long.valueOf(protoLogParam03), protoLogParam13, java.lang.Long.valueOf(protoLogParam23), protoLogParam32, java.lang.Long.valueOf(protoLogParam42));
        }
        if (oldRotation == rotation) {
            android.util.Slog.d(str, "rotation(" + android.view.Surface.rotationToString(rotation) + ") no changed  displayId = " + displayId + ", lastOrientation = " + lastOrientation);
            this.mDisplayRotationExt.setSensorRotationChanged(this.mDisplayContent, false);
            return false;
        }
        java.lang.String str2 = str;
        if (this.isDefaultDisplay) {
            this.mDisplayRotationCoordinator.onDefaultDisplayRotationChanged(rotation);
        }
        com.android.server.wm.RecentsAnimationController recentsAnimationController = this.mService.getRecentsAnimationController();
        if (!this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent) && recentsAnimationController != null) {
            recentsAnimationController.cancelAnimationForDisplayChange();
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            android.util.Slog.e(str2, "updateRotationUnchecked :rotation changed by orientationSource:" + this.mDisplayContent.getLastOrientationSource() + ", caller:" + android.os.Debug.getCallers(30));
        }
        if (!this.mDisplayRotationExt.stopRotationInGame(this.mDisplayContent.getLastOrientationSource())) {
            if (this.mDisplayRotationExt.stopRotationInPutt(this.mDisplayContent.getLastOrientationSource(), displayId)) {
                android.util.Slog.d(str2, "updateRotationUnchecked: ignore rotation under one putt");
                return false;
            }
            android.util.Slog.i(str2, "Display id " + displayId + " rotation changed to " + android.view.Surface.rotationToString(rotation) + " (" + rotation + ") from " + android.view.Surface.rotationToString(oldRotation) + " (" + oldRotation + "), lastOrientation = " + android.content.pm.ActivityInfo.screenOrientationToString(lastOrientation) + " (" + lastOrientation + ") lastOrientationSource = " + this.mDisplayContent.getLastOrientationSource());
            this.mRotation = rotation;
            this.mDisplayRotationExt.updateRotation(this.mRotation, this.mDisplayContent);
            this.mDisplayContent.setLayoutNeeded();
            this.mDisplayContent.mWaitingForConfig = true;
            if (this.mDisplayContent.mTransitionController.isShellTransitionsEnabled()) {
                boolean wasCollecting = this.mDisplayContent.mTransitionController.isCollecting();
                if (wasCollecting && this.mDisplayRotationExt.abortPocketStudioCloseAnimation(this.mDisplayContent.mTransitionController.getCollectingTransition())) {
                    wasCollecting = this.mDisplayContent.mTransitionController.isCollecting();
                }
                if (!wasCollecting) {
                    if (this.mDisplayContent.getLastHasContent()) {
                        android.window.TransitionRequestInfo.DisplayChange change = new android.window.TransitionRequestInfo.DisplayChange(this.mDisplayContent.getDisplayId(), oldRotation, this.mRotation);
                        this.mDisplayContent.requestChangeTransition(536870912, change);
                        return true;
                    }
                    return true;
                }
                this.mDisplayContent.collectDisplayChange(this.mDisplayContent.mTransitionController.getCollectingTransition());
                startRemoteRotation(oldRotation, this.mRotation);
                return true;
            }
            this.mService.mWindowsFreezingScreen = 1;
            this.mService.mH.sendNewMessageDelayed(11, this.mDisplayContent, 2000L);
            if (shouldRotateSeamlessly(oldRotation, rotation, forceUpdate)) {
                prepareSeamlessRotation();
            } else {
                prepareNormalRotationAnimation();
            }
            startRemoteRotation(oldRotation, this.mRotation);
            return true;
        }
        android.util.Slog.d(str2, "updateRotationUnchecked: ignore rotation under GameSpace control");
        return false;
    }

    private void startRemoteRotation(int fromRotation, final int toRotation) {
        if (this.mDisplayContent.getWrapper().getExtImpl().isActivityPreloadDisplay(this.mDisplayContent)) {
            android.util.Slog.v("OAPM", "startRemoteRotation : temp return! ");
            return;
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM) {
            android.util.Slog.d(TAG, "Start remote rotation : fromRotation : " + fromRotation + "; toRotation : " + toRotation + ";callers:" + android.os.Debug.getCallers(20));
        }
        this.mDisplayContent.mRemoteDisplayChangeController.performRemoteDisplayChange(fromRotation, toRotation, null, new com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback() { // from class: com.android.server.wm.DisplayRotation$$ExternalSyntheticLambda1
            @Override // com.android.server.wm.RemoteDisplayChangeController.ContinueRemoteDisplayChangeCallback
            public final void onContinueRemoteDisplayChange(android.window.WindowContainerTransaction windowContainerTransaction) {
                this.f$0.lambda$startRemoteRotation$0(toRotation, windowContainerTransaction);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: continueRotation, reason: merged with bridge method [inline-methods] */
    public void lambda$startRemoteRotation$0(int targetRotation, android.window.WindowContainerTransaction t) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_ANIM) {
            android.util.Slog.d(TAG, "Continue rotation : targetRotation : " + targetRotation + "; mRotation : " + this.mRotation + "; callers:" + android.os.Debug.getCallers(10));
        }
        if (targetRotation != this.mRotation) {
            if (this.mDisplayContent.mWaitingForConfig) {
                android.util.Slog.e(TAG, "continueRotation  mWaitingForConfig is true,targetRotation=" + targetRotation + " mRotation=" + this.mRotation + ",callers:" + android.os.Debug.getCallers(3));
            }
        } else {
            if (this.mDisplayContent.getSurfaceControl() == null) {
                android.util.Slog.e(TAG, "continueRotation error as display is removed" + targetRotation + " mRotation=" + this.mRotation + ",callers:" + android.os.Debug.getCallers(15));
                return;
            }
            if (this.mDisplayContent.mTransitionController.isShellTransitionsEnabled()) {
                if (!this.mDisplayContent.mTransitionController.isCollecting()) {
                    android.util.Slog.e(TAG, "Trying to continue rotation outside a transition");
                }
                this.mDisplayContent.mTransitionController.collect(this.mDisplayContent);
            }
            this.mService.mAtmService.deferWindowLayout();
            try {
                this.mDisplayContent.sendNewConfiguration();
                if (t != null) {
                    this.mService.mAtmService.mWindowOrganizerController.applyTransaction(t);
                }
                this.mDisplayRotationExt.continueRotation();
            } finally {
                this.mService.mAtmService.continueWindowLayout();
            }
        }
    }

    void prepareNormalRotationAnimation() {
        cancelSeamlessRotation();
        com.android.server.wm.DisplayRotation.RotationAnimationPair anim = selectRotationAnimation();
        this.mService.startFreezingDisplay(anim.mExit, anim.mEnter, this.mDisplayContent);
    }

    void cancelSeamlessRotation() {
        if (!this.mRotatingSeamlessly) {
            return;
        }
        this.mDisplayContent.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayRotation$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wm.DisplayRotation.lambda$cancelSeamlessRotation$1((com.android.server.wm.WindowState) obj);
            }
        }, true);
        this.mSeamlessRotationCount = 0;
        this.mRotatingSeamlessly = false;
        this.mDisplayContent.finishAsyncRotationIfPossible();
    }

    static /* synthetic */ void lambda$cancelSeamlessRotation$1(com.android.server.wm.WindowState w) {
        if (w.mSeamlesslyRotated) {
            w.cancelSeamlessRotation();
            w.mSeamlesslyRotated = false;
        }
    }

    private void prepareSeamlessRotation() {
        this.mSeamlessRotationCount = 0;
        this.mRotatingSeamlessly = true;
    }

    boolean isRotatingSeamlessly() {
        return this.mRotatingSeamlessly;
    }

    boolean hasSeamlessRotatingWindow() {
        return this.mSeamlessRotationCount > 0;
    }

    boolean shouldRotateSeamlessly(int oldRotation, int newRotation, boolean forceUpdate) {
        if (this.mDisplayContent.hasTopFixedRotationLaunchingApp()) {
            return true;
        }
        com.android.server.wm.WindowState w = this.mDisplayPolicy.getTopFullscreenOpaqueWindow();
        if (w == null || w != this.mDisplayContent.mCurrentFocus) {
            if (this.mDisplayRotationExt.forceSeamlesslyRotated(w, "camera launch from keyguard")) {
                return true;
            }
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                android.util.Slog.e(TAG, "not allow seamless rotate, for fullscreen win:" + w + " not match current focus:" + this.mDisplayContent.mCurrentFocus);
            }
            return false;
        }
        if (this.mDisplayContent.getWrapper().getExtImpl().isMirageDisplay()) {
            return true;
        }
        if (w.getAttrs().rotationAnimation != 3 || w.inMultiWindowMode() || w.isAnimatingLw()) {
            return false;
        }
        if (w.getTask() != null && w.getTask().getWrapper().getExtImpl().isFlexibleWindowScenario(new int[0])) {
            return false;
        }
        if (!canRotateSeamlessly(oldRotation, newRotation)) {
            return this.mDisplayRotationExt.forceSeamlesslyRotated(w, "upside down rotation");
        }
        if (w.mActivityRecord == null || w.mActivityRecord.matchParentBounds()) {
            return (this.mDisplayContent.getDefaultTaskDisplayArea().hasPinnedTask() || this.mDisplayContent.hasAlertWindowSurfaces()) ? this.mDisplayRotationExt.forceSeamlesslyRotated(w, "PIP or System Alert windows") : forceUpdate || this.mDisplayContent.getWindow(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayRotation$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.wm.WindowState) obj).mSeamlesslyRotated;
                }
            }) == null || this.mDisplayRotationExt.forceSeamlesslyRotated(w, "waiting for last seamless");
        }
        return false;
    }

    boolean canRotateSeamlessly(int oldRotation, int newRotation) {
        if (this.mAllowSeamlessRotationDespiteNavBarMoving || this.mDisplayPolicy.navigationBarCanMove()) {
            return true;
        }
        return (oldRotation == 2 || newRotation == 2) ? false : true;
    }

    void markForSeamlessRotation(com.android.server.wm.WindowState w, boolean seamlesslyRotated) {
        if (seamlesslyRotated == w.mSeamlesslyRotated || w.mForceSeamlesslyRotate) {
            return;
        }
        w.mSeamlesslyRotated = seamlesslyRotated;
        if (seamlesslyRotated) {
            this.mSeamlessRotationCount++;
        } else {
            this.mSeamlessRotationCount--;
        }
        if (this.mSeamlessRotationCount == 0) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[2]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -1216224951455892544L, 0, null, null);
            }
            this.mRotatingSeamlessly = false;
            this.mDisplayContent.finishAsyncRotationIfPossible();
            updateRotationAndSendNewConfigIfChanged();
        }
    }

    private com.android.server.wm.DisplayRotation.RotationAnimationPair selectRotationAnimation() {
        boolean forceJumpcut = !(this.mDisplayPolicy.isScreenOnFully() && this.mService.mPolicy.okToAnimate(false)) && this.mDisplayContent.isDefaultDisplay;
        com.android.server.wm.WindowState topFullscreen = this.mDisplayPolicy.getTopFullscreenOpaqueWindow();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ANIM_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(topFullscreen);
            long protoLogParam1 = topFullscreen == null ? 0L : topFullscreen.getAttrs().rotationAnimation;
            boolean protoLogParam2 = forceJumpcut;
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ANIM, -7672508047849737424L, 52, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), java.lang.Boolean.valueOf(protoLogParam2));
        }
        if (forceJumpcut) {
            this.mTmpRotationAnim.mExit = android.R.anim.push_up_in;
            this.mTmpRotationAnim.mEnter = android.R.anim.push_down_out_no_alpha;
            return this.mTmpRotationAnim;
        }
        if (topFullscreen != null) {
            int animationHint = topFullscreen.getRotationAnimationHint();
            if (animationHint < 0 && this.mDisplayPolicy.isTopLayoutFullscreen()) {
                animationHint = topFullscreen.getAttrs().rotationAnimation;
            }
            switch (animationHint) {
                case 1:
                case 3:
                    this.mTmpRotationAnim.mExit = android.R.anim.push_up_out;
                    this.mTmpRotationAnim.mEnter = android.R.anim.push_down_out_no_alpha;
                    break;
                case 2:
                    this.mTmpRotationAnim.mExit = android.R.anim.push_up_in;
                    this.mTmpRotationAnim.mEnter = android.R.anim.push_down_out_no_alpha;
                    break;
                default:
                    com.android.server.wm.DisplayRotation.RotationAnimationPair rotationAnimationPair = this.mTmpRotationAnim;
                    this.mTmpRotationAnim.mEnter = 0;
                    rotationAnimationPair.mExit = 0;
                    break;
            }
        } else {
            com.android.server.wm.DisplayRotation.RotationAnimationPair rotationAnimationPair2 = this.mTmpRotationAnim;
            this.mTmpRotationAnim.mEnter = 0;
            rotationAnimationPair2.mExit = 0;
        }
        return this.mTmpRotationAnim;
    }

    boolean validateRotationAnimation(int exitAnimId, int enterAnimId, boolean forceDefault) {
        switch (exitAnimId) {
            case android.R.anim.push_up_in:
            case android.R.anim.push_up_out:
                if (forceDefault) {
                    return false;
                }
                com.android.server.wm.DisplayRotation.RotationAnimationPair anim = selectRotationAnimation();
                if (exitAnimId == anim.mExit && enterAnimId == anim.mEnter) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    void restoreSettings(int userRotationMode, int userRotation, int fixedToUserRotation) {
        this.mFixedToUserRotation = fixedToUserRotation;
        if (this.isDefaultDisplay || this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent)) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                android.util.Slog.e(TAG, "restoreSettings return DisplayId = " + this.mDisplayContent.getDisplayId());
                return;
            }
            return;
        }
        if (userRotationMode != 0 && userRotationMode != 1) {
            android.util.Slog.w(TAG, "Trying to restore an invalid user rotation mode " + userRotationMode + " for " + this.mDisplayContent);
            userRotationMode = 0;
        }
        if (userRotation < 0 || userRotation > 3) {
            android.util.Slog.w(TAG, "Trying to restore an invalid user rotation " + userRotation + " for " + this.mDisplayContent);
            userRotation = 0;
        }
        int userRotationOverride = getUserRotationOverride();
        if (userRotationOverride != 0) {
            userRotationMode = 1;
            userRotation = userRotationOverride;
        }
        this.mUserRotationMode = userRotationMode;
        this.mUserRotation = userRotation;
    }

    void setFixedToUserRotation(int fixedToUserRotation) {
        if (this.mFixedToUserRotation == fixedToUserRotation) {
            return;
        }
        this.mFixedToUserRotation = fixedToUserRotation;
        this.mDisplayWindowSettings.setFixedToUserRotation(this.mDisplayContent, fixedToUserRotation);
        if (this.mDisplayContent.mFocusedApp != null) {
            this.mDisplayContent.onLastFocusedTaskDisplayAreaChanged(this.mDisplayContent.mFocusedApp.getDisplayArea());
        }
        this.mDisplayContent.updateOrientation();
    }

    void setUserRotation(int userRotationMode, int userRotation, java.lang.String caller) {
        this.mRotationLockHistory.addRecord(userRotationMode, userRotation, caller);
        this.mRotationChoiceShownToUserForConfirmation = -1;
        if (useDefaultSettingsProvider()) {
            android.content.ContentResolver res = this.mContext.getContentResolver();
            int accelerometerRotation = userRotationMode != 1 ? 1 : 0;
            android.provider.Settings.System.putIntForUser(res, "accelerometer_rotation", accelerometerRotation, -2);
            android.provider.Settings.System.putIntForUser(res, "user_rotation", userRotation, -2);
            return;
        }
        if (this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent)) {
            android.content.ContentResolver res2 = this.mContext.getContentResolver();
            int accelerometerRotation2 = userRotationMode == 1 ? 0 : 1;
            android.provider.Settings.System.putIntForUser(res2, "accelerometer_rotation_secondary", accelerometerRotation2, -2);
        }
        boolean changed = false;
        if (this.mUserRotationMode != userRotationMode) {
            this.mUserRotationMode = userRotationMode;
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                android.util.Slog.e(TAG, "setUserRotation DisplayId = " + this.mDisplayContent.getDisplayId() + " mUserRotationMode = " + this.mUserRotationMode);
            }
            changed = true;
        }
        if (this.mUserRotation != userRotation) {
            this.mUserRotation = userRotation;
            changed = true;
        }
        this.mDisplayWindowSettings.setUserRotation(this.mDisplayContent, userRotationMode, userRotation);
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            android.util.Slog.w(TAG, "setUserRotation rotation " + userRotation + "; userRotationMode = " + userRotationMode + ";changed = " + changed + " for " + this.mDisplayContent + "; callers:" + android.os.Debug.getCallers(10));
        }
        if (changed) {
            this.mService.updateRotation(false, false);
            this.mDisplayContent.onMirrorOutputSurfaceOrientationChanged();
        }
    }

    void freezeRotation(int rotation, java.lang.String caller) {
        if (this.mDeviceStateController.shouldReverseRotationDirectionAroundZAxis(this.mDisplayContent)) {
            rotation = android.util.RotationUtils.reverseRotationDirectionAroundZAxis(rotation);
        }
        setUserRotation(1, this.mDisplayRotationExt.modifyFreezeRotationWhenDeviceFolding(rotation == -1 ? this.mRotation : rotation), caller);
    }

    void thawRotation(java.lang.String caller) {
        setUserRotation(0, this.mUserRotation, caller);
    }

    boolean isRotationFrozen() {
        return !this.isDefaultDisplay ? this.mUserRotationMode == 1 : android.provider.Settings.System.getIntForUser(this.mContext.getContentResolver(), "accelerometer_rotation", 0, -2) == 0;
    }

    boolean isFixedToUserRotation() {
        switch (this.mFixedToUserRotation) {
            case 1:
                return false;
            case 2:
                return true;
            case 3:
                return false;
            default:
                return this.mDefaultFixedToUserRotation;
        }
    }

    int getFixedToUserRotationMode() {
        return this.mFixedToUserRotation;
    }

    public int getLandscapeRotation() {
        return this.mLandscapeRotation;
    }

    public int getSeascapeRotation() {
        return this.mSeascapeRotation;
    }

    public int getPortraitRotation() {
        return this.mPortraitRotation;
    }

    public int getUpsideDownRotation() {
        return this.mUpsideDownRotation;
    }

    public int getCurrentAppOrientation() {
        return this.mCurrentAppOrientation;
    }

    public com.android.server.wm.DisplayPolicy getDisplayPolicy() {
        return this.mDisplayPolicy;
    }

    public com.android.server.wm.WindowOrientationListener getOrientationListener() {
        return this.mOrientationListener;
    }

    public int getUserRotation() {
        return this.mUserRotation;
    }

    public int getUserRotationMode() {
        return this.mUserRotationMode;
    }

    public void updateOrientationListener() {
        synchronized (this.mLock) {
            updateOrientationListenerLw();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pause() {
        this.mDeferredRotationPauseCount++;
        this.mDisplayRotationExt.pauseRotation(this.mDeferredRotationPauseCount);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resume() {
        if (this.mDeferredRotationPauseCount <= 0) {
            return;
        }
        this.mDeferredRotationPauseCount--;
        this.mDisplayRotationExt.resumeRotation(this.mDeferredRotationPauseCount);
        if (this.mDeferredRotationPauseCount == 0) {
            updateRotationAndSendNewConfigIfChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOrientationListenerLw() {
        if (this.mOrientationListener == null || !this.mOrientationListener.canDetectOrientation()) {
            return;
        }
        boolean screenOnEarly = this.mDisplayPolicy.isScreenOnEarly();
        boolean awake = this.mDisplayPolicy.isAwake();
        boolean keyguardDrawComplete = this.mDisplayPolicy.isKeyguardDrawComplete();
        boolean windowManagerDrawComplete = this.mDisplayPolicy.isWindowManagerDrawComplete();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
            long protoLogParam2 = this.mCurrentAppOrientation;
            boolean protoLogParam3 = this.mOrientationListener.mEnabled;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -2426404033822048710L, 4063, null, java.lang.Boolean.valueOf(screenOnEarly), java.lang.Boolean.valueOf(awake), java.lang.Long.valueOf(protoLogParam2), java.lang.Boolean.valueOf(protoLogParam3), java.lang.Boolean.valueOf(keyguardDrawComplete), java.lang.Boolean.valueOf(windowManagerDrawComplete));
        }
        boolean disable = true;
        if (screenOnEarly && ((this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent) || ((awake || this.mOrientationListener.shouldStayEnabledWhileDreaming()) && keyguardDrawComplete && windowManagerDrawComplete)) && needSensorRunning())) {
            disable = false;
            if (!this.mOrientationListener.mEnabled) {
                this.mOrientationListener.enable();
                this.mDisplayRotationExt.updateOrientationSensorRunningState(true);
            }
        }
        if (disable) {
            this.mOrientationListener.disable();
            this.mDisplayRotationExt.updateOrientationSensorRunningState(false);
        }
    }

    private boolean needSensorRunning() {
        if (this.mDisplayRotationExt.shouldFreezeScreenOrientation() || isFixedToUserRotation()) {
            return false;
        }
        if (this.mFoldController != null && this.mFoldController.shouldDisableRotationSensor()) {
            return false;
        }
        if (this.mSupportAutoRotation && (this.mCurrentAppOrientation == 4 || this.mCurrentAppOrientation == 10 || this.mCurrentAppOrientation == 7 || this.mCurrentAppOrientation == 6)) {
            return true;
        }
        int dockMode = this.mDisplayPolicy.getDockMode();
        if ((this.mDisplayPolicy.isCarDockEnablesAccelerometer() && dockMode == 2) || (this.mDisplayPolicy.isDeskDockEnablesAccelerometer() && (dockMode == 1 || dockMode == 3 || dockMode == 4))) {
            return true;
        }
        if (this.mUserRotationMode == 1) {
            return this.mSupportAutoRotation && this.mShowRotationSuggestions == 1;
        }
        return this.mSupportAutoRotation;
    }

    boolean needsUpdate() {
        int oldRotation = this.mRotation;
        int rotation = rotationForOrientation(this.mLastOrientation, oldRotation);
        return oldRotation != rotation;
    }

    void resetAllowAllRotations() {
        this.mAllowAllRotations = -1;
    }

    int rotationForOrientation(int orientation, int lastRotation) {
        int sensorRotation;
        int sensorRotation2;
        int preferredRotation;
        int result;
        int result2;
        int result3;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(android.content.pm.ActivityInfo.screenOrientationToString(orientation));
            long protoLogParam1 = orientation;
            java.lang.String protoLogParam2 = java.lang.String.valueOf(android.view.Surface.rotationToString(lastRotation));
            long protoLogParam3 = lastRotation;
            java.lang.String protoLogParam4 = java.lang.String.valueOf(android.view.Surface.rotationToString(this.mUserRotation));
            long protoLogParam5 = this.mUserRotation;
            java.lang.String protoLogParam6 = java.lang.String.valueOf(this.mUserRotationMode == 1 ? "USER_ROTATION_LOCKED" : "");
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 7339471241580327852L, 1092, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), protoLogParam2, java.lang.Long.valueOf(protoLogParam3), protoLogParam4, java.lang.Long.valueOf(protoLogParam5), protoLogParam6);
        }
        int mirageFixedRotation = this.mDisplayRotationExt.getMirageFixedRotation(this.mDisplayContent.getDisplayId());
        if (mirageFixedRotation == -1) {
            int fixedRotation = this.mDisplayRotationExt.getFixedRotationForOrientation(orientation, this.mDisplayContent, lastRotation);
            if (fixedRotation != -1) {
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                    android.util.Slog.d(TAG, "rotationForOrientation fixedRotation = " + android.view.Surface.rotationToString(fixedRotation));
                }
                return fixedRotation;
            }
            if (isFixedToUserRotation()) {
                if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                    android.util.Slog.d(TAG, "rotationForOrientation: isFixedToUserRotation, mUserRotation = " + this.mUserRotation);
                }
                return this.mUserRotation;
            }
            if (this.mOrientationListener != null) {
                sensorRotation = this.mOrientationListener.getProposedRotation();
            } else {
                sensorRotation = -1;
            }
            if (this.mFoldController != null && this.mFoldController.shouldIgnoreSensorRotation()) {
                sensorRotation = -1;
            }
            if (this.mDeviceStateController.shouldReverseRotationDirectionAroundZAxis(this.mDisplayContent)) {
                sensorRotation = android.util.RotationUtils.reverseRotationDirectionAroundZAxis(sensorRotation);
            }
            if (this.mDisplayRotationExt.shouldKeepSensorRotationInFixRotation(this.mDisplayContent, orientation, sensorRotation, this.mLastSensorRotation)) {
                sensorRotation = this.mLastSensorRotation;
            }
            this.mLastSensorRotation = sensorRotation;
            int sensorRotation3 = this.mDisplayRotationExt.hookUpdateSensorRotation(sensorRotation, this.mDisplayContent);
            if (sensorRotation3 < 0) {
                sensorRotation3 = lastRotation;
            }
            if (this.mDisplayContent.getWrapper().getExtImpl().isMirageDisplay() && !((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageCarMode(this.mDisplayContent.getDisplayId())) {
                int sensorRotation4 = this.mDisplayRotationExt.getMirageDisplaySensorRotation(this.mDisplayContent.getDisplayId());
                sensorRotation2 = sensorRotation4;
            } else {
                sensorRotation2 = sensorRotation3;
            }
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
                android.util.Slog.d(TAG, "rotationForOrientation sensorRotation = " + sensorRotation2 + " displayId = " + this.mDisplayContent.getDisplayId() + "; " + android.os.Debug.getCallers(8));
            }
            int lidState = this.mDisplayPolicy.getLidState();
            int dockMode = this.mDisplayPolicy.getDockMode();
            boolean hdmiPlugged = this.mDisplayPolicy.isHdmiPlugged();
            boolean carDockEnablesAccelerometer = this.mDisplayPolicy.isCarDockEnablesAccelerometer();
            boolean deskDockEnablesAccelerometer = this.mDisplayPolicy.isDeskDockEnablesAccelerometer();
            if (!this.isDefaultDisplay && !this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent) && !this.mDisplayContent.getWrapper().getExtImpl().isMirageDisplay()) {
                preferredRotation = this.mUserRotation;
            } else if (lidState == 1 && this.mLidOpenRotation >= 0) {
                preferredRotation = this.mLidOpenRotation;
            } else if (dockMode != 2 || (!carDockEnablesAccelerometer && this.mCarDockRotation < 0)) {
                if ((dockMode == 1 || dockMode == 3 || dockMode == 4) && ((deskDockEnablesAccelerometer || this.mDeskDockRotation >= 0) && orientation != 14 && orientation != 5)) {
                    preferredRotation = deskDockEnablesAccelerometer ? sensorRotation2 : this.mDeskDockRotation;
                } else if ((hdmiPlugged || this.mDisplayRotationSocExt.hookIsWifiDisplayConnected()) && this.mDemoHdmiRotationLock) {
                    preferredRotation = this.mDemoHdmiRotation;
                } else if (this.mDisplayRotationSocExt.hookIsWifiDisplayConnected() && this.mDisplayRotationSocExt.hookGetWifiDisplayRotation() > -1) {
                    preferredRotation = this.mDisplayRotationSocExt.hookGetWifiDisplayRotation();
                } else if (hdmiPlugged && dockMode == 0 && this.mUndockedHdmiRotation >= 0) {
                    preferredRotation = this.mUndockedHdmiRotation;
                } else if (this.mDemoRotationLock) {
                    preferredRotation = this.mDemoRotation;
                } else if (this.mDisplayPolicy.isPersistentVrModeEnabled()) {
                    preferredRotation = this.mPortraitRotation;
                } else if (orientation == 14) {
                    preferredRotation = lastRotation;
                } else if (!this.mSupportAutoRotation) {
                    if (this.mFixedToUserRotation == 3) {
                        preferredRotation = this.mUserRotation;
                    } else {
                        preferredRotation = -1;
                    }
                } else if (((this.mUserRotationMode == 0 || isTabletopAutoRotateOverrideEnabled()) && (orientation == 2 || orientation == -1 || orientation == 11 || orientation == 12 || orientation == 13)) || orientation == 4 || orientation == 10 || orientation == 6 || orientation == 7) {
                    if (this.mDisplayRotationExt.isSecondDisplay(this.mDisplayContent)) {
                        preferredRotation = this.mDisplayRotationExt.resolvePreferredRotationInSecondary(sensorRotation2, lastRotation, orientation);
                    } else if (sensorRotation2 != 2 || this.mDisplayRotationExt.isForceAllowAllOrientation(this.mDisplayContent) || getAllowAllRotations() == this.mDisplayRotationExt.blockAllowAllRotationsInTable(1, this.mDisplayContent) || orientation == 10 || orientation == 13) {
                        int preferredRotation2 = sensorRotation2;
                        preferredRotation = preferredRotation2;
                    } else {
                        preferredRotation = lastRotation;
                    }
                } else if (this.mUserRotationMode == 1 && orientation != 5 && orientation != 0 && orientation != 1 && orientation != 8 && orientation != 9) {
                    preferredRotation = this.mDisplayRotationExt.hookLockedRotation(this.mUserRotation, this.mDisplayContent);
                } else {
                    preferredRotation = -1;
                }
            } else {
                preferredRotation = carDockEnablesAccelerometer ? sensorRotation2 : this.mCarDockRotation;
            }
            switch (orientation) {
                case 0:
                    int preferredRotation3 = preferredRotation;
                    if (isLandscapeOrSeascape(preferredRotation3)) {
                        return preferredRotation3;
                    }
                    return this.mLandscapeRotation;
                case 1:
                    int preferredRotation4 = preferredRotation;
                    if (this.mDisplayContent.getDisplayId() == 0 && (result = this.mDisplayRotationExt.getSuggestRotationForBracketMode()) != -1) {
                        return result;
                    }
                    if (!isAnyPortrait(preferredRotation4)) {
                        return this.mDisplayRotationExt.hookActivityOrientation(sensorRotation2, this.mUserRotationMode, orientation, this.mPortraitRotation, this.mDisplayContent);
                    }
                    return preferredRotation4;
                case 2:
                case 3:
                case 4:
                case 5:
                case 10:
                default:
                    int preferredRotation5 = preferredRotation;
                    if (this.mDisplayContent.getDisplayId() == 0 && (result3 = this.mDisplayRotationExt.shouldSuggestEnterBracketMode()) != -1) {
                        return result3;
                    }
                    if (preferredRotation5 >= 0) {
                        return this.mDisplayRotationExt.forceLauncherRotate(preferredRotation5, this.mDisplayContent.getLastOrientationSource());
                    }
                    return 0;
                case 6:
                case 11:
                    int preferredRotation6 = preferredRotation;
                    if (isLandscapeOrSeascape(preferredRotation6)) {
                        return preferredRotation6;
                    }
                    return isLandscapeOrSeascape(lastRotation) ? lastRotation : this.mLandscapeRotation;
                case 7:
                case 12:
                    int preferredRotation7 = preferredRotation;
                    if (isAnyPortrait(preferredRotation7)) {
                        return preferredRotation7;
                    }
                    return isAnyPortrait(lastRotation) ? lastRotation : this.mPortraitRotation;
                case 8:
                    int preferredRotation8 = preferredRotation;
                    int adjustRotation = this.mDisplayRotationExt.adjustRotationForReverseLandscape(this.mDisplayContent, this.mSeascapeRotation);
                    if (adjustRotation != -1) {
                        return adjustRotation;
                    }
                    if (isLandscapeOrSeascape(preferredRotation8)) {
                        return preferredRotation8;
                    }
                    return this.mSeascapeRotation;
                case 9:
                    if (this.mDisplayContent.getDisplayId() == 0 && (result2 = this.mDisplayRotationExt.getSuggestRotationForBracketMode()) != -1) {
                        return result2;
                    }
                    android.view.DisplayInfo displayInfo = this.mDisplayContent.getDisplayInfo();
                    if (displayInfo != null && displayInfo.displayId != 0 && displayInfo.type == 1) {
                        android.util.Slog.d(TAG, "mDisplayContent: " + this.mDisplayContent);
                        return this.mUpsideDownRotation;
                    }
                    if (!isAnyPortrait(preferredRotation)) {
                        return this.mDisplayRotationExt.hookActivityOrientation(sensorRotation2, this.mUserRotationMode, orientation, this.mUpsideDownRotation, this.mDisplayContent);
                    }
                    return preferredRotation;
            }
        }
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_CONFIGURATION) {
            android.util.Slog.d(TAG, "rotationForOrientation mirageFixedRotation = " + android.view.Surface.rotationToString(mirageFixedRotation));
        }
        return mirageFixedRotation;
    }

    private int getAllowAllRotations() {
        int i;
        if (this.mAllowAllRotations == -1) {
            if (this.mContext.getResources().getBoolean(android.R.bool.config_allowAllRotations)) {
                i = 1;
            } else {
                i = 0;
            }
            this.mAllowAllRotations = i;
        }
        return this.mAllowAllRotations;
    }

    boolean isLandscapeOrSeascape(int rotation) {
        return rotation == this.mLandscapeRotation || rotation == this.mSeascapeRotation;
    }

    boolean isAnyPortrait(int rotation) {
        return rotation == this.mPortraitRotation || rotation == this.mUpsideDownRotation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidRotationChoice(int preferredRotation) {
        switch (this.mCurrentAppOrientation) {
            case -1:
            case 2:
                if (getAllowAllRotations() == 1) {
                    return preferredRotation >= 0;
                }
                return this.mDisplayRotationExt.isValidRotationChoice(preferredRotation, this.mUpsideDownRotation);
            case 11:
                return isLandscapeOrSeascape(preferredRotation);
            case 12:
                return preferredRotation == this.mPortraitRotation;
            case 13:
                return preferredRotation >= 0;
            default:
                return false;
        }
    }

    private boolean isTabletopAutoRotateOverrideEnabled() {
        return this.mFoldController != null && this.mFoldController.overrideFrozenRotation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRotationChoiceAllowed(int proposedRotation) {
        int dockMode;
        boolean isRotationLockEnforced = this.mCompatPolicyForImmersiveApps != null && this.mCompatPolicyForImmersiveApps.isRotationLockEnforced(proposedRotation);
        if ((!isRotationLockEnforced && this.mUserRotationMode != 1) || isTabletopAutoRotateOverrideEnabled() || isFixedToUserRotation()) {
            return false;
        }
        int lidState = this.mDisplayPolicy.getLidState();
        if ((lidState == 1 && this.mLidOpenRotation >= 0) || (dockMode = this.mDisplayPolicy.getDockMode()) == 2) {
            return false;
        }
        boolean deskDockEnablesAccelerometer = this.mDisplayPolicy.isDeskDockEnablesAccelerometer();
        if ((dockMode == 1 || dockMode == 3 || dockMode == 4) && !deskDockEnablesAccelerometer) {
            return false;
        }
        boolean hdmiPlugged = this.mDisplayPolicy.isHdmiPlugged();
        if (hdmiPlugged && this.mDemoHdmiRotationLock) {
            return false;
        }
        if ((hdmiPlugged && dockMode == 0 && this.mUndockedHdmiRotation >= 0) || this.mDemoRotationLock || this.mDisplayPolicy.isPersistentVrModeEnabled() || !this.mSupportAutoRotation) {
            return false;
        }
        switch (this.mCurrentAppOrientation) {
            case -1:
            case 2:
            case 11:
            case 12:
            case 13:
                break;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendProposedRotationChangeToStatusBarInternal(int rotation, boolean isValid) {
        if (this.mStatusBarManagerInternal == null) {
            this.mStatusBarManagerInternal = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
        }
        if (this.mStatusBarManagerInternal != null) {
            this.mStatusBarManagerInternal.onProposedRotationChanged(rotation, isValid);
        }
    }

    void dispatchProposedRotation(int rotation) {
        if (this.mService.mRotationWatcherController.hasProposedRotationListeners()) {
            synchronized (this.mLock) {
                this.mService.mRotationWatcherController.dispatchProposedRotation(this.mDisplayContent, rotation);
            }
        }
    }

    private static java.lang.String allowAllRotationsToString(int allowAll) {
        switch (allowAll) {
            case -1:
                return "unknown";
            case 0:
                return "false";
            case 1:
                return "true";
            default:
                return java.lang.Integer.toString(allowAll);
        }
    }

    public void onUserSwitch() {
        if (this.mSettingsObserver != null) {
            this.mSettingsObserver.onChange(false);
        }
    }

    void onDisplayRemoved() {
        removeDefaultDisplayRotationChangedCallback();
        if (this.mFoldController != null) {
            this.mFoldController.onDisplayRemoved();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0056 A[Catch: all -> 0x006e, TryCatch #0 {, blocks: (B:5:0x000b, B:9:0x001d, B:11:0x0022, B:12:0x0025, B:14:0x0030, B:15:0x0033, B:17:0x003d, B:24:0x0051, B:26:0x0056, B:28:0x005c, B:29:0x005f, B:31:0x0069, B:32:0x006c, B:20:0x0046, B:8:0x0016), top: B:37:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x005c A[Catch: all -> 0x006e, TryCatch #0 {, blocks: (B:5:0x000b, B:9:0x001d, B:11:0x0022, B:12:0x0025, B:14:0x0030, B:15:0x0033, B:17:0x003d, B:24:0x0051, B:26:0x0056, B:28:0x005c, B:29:0x005f, B:31:0x0069, B:32:0x006c, B:20:0x0046, B:8:0x0016), top: B:37:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0069 A[Catch: all -> 0x006e, TryCatch #0 {, blocks: (B:5:0x000b, B:9:0x001d, B:11:0x0022, B:12:0x0025, B:14:0x0030, B:15:0x0033, B:17:0x003d, B:24:0x0051, B:26:0x0056, B:28:0x005c, B:29:0x005f, B:31:0x0069, B:32:0x006c, B:20:0x0046, B:8:0x0016), top: B:37:0x000b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean updateSettings() {
        /*
            r11 = this;
            android.content.Context r0 = r11.mContext
            android.content.ContentResolver r0 = r0.getContentResolver()
            r1 = 0
            java.lang.Object r2 = r11.mLock
            monitor-enter(r2)
            r3 = 0
            boolean r4 = android.app.ActivityManager.isLowRamDeviceStatic()     // Catch: java.lang.Throwable -> L6e
            r5 = 1
            r6 = -2
            r7 = 0
            if (r4 == 0) goto L16
            r4 = r7
            goto L1d
        L16:
            java.lang.String r4 = "show_rotation_suggestions"
            int r4 = android.provider.Settings.Secure.getIntForUser(r0, r4, r5, r6)     // Catch: java.lang.Throwable -> L6e
        L1d:
            int r8 = r11.mShowRotationSuggestions     // Catch: java.lang.Throwable -> L6e
            if (r8 == r4) goto L25
            r11.mShowRotationSuggestions = r4     // Catch: java.lang.Throwable -> L6e
            r3 = 1
        L25:
            java.lang.String r8 = "user_rotation"
            int r8 = android.provider.Settings.System.getIntForUser(r0, r8, r7, r6)     // Catch: java.lang.Throwable -> L6e
            int r9 = r11.mUserRotation     // Catch: java.lang.Throwable -> L6e
            if (r9 == r8) goto L33
            r11.mUserRotation = r8     // Catch: java.lang.Throwable -> L6e
            r1 = 1
        L33:
            com.android.server.wm.IDisplayRotationExt r9 = r11.mDisplayRotationExt     // Catch: java.lang.Throwable -> L6e
            com.android.server.wm.DisplayContent r10 = r11.mDisplayContent     // Catch: java.lang.Throwable -> L6e
            boolean r9 = r9.isSecondDisplay(r10)     // Catch: java.lang.Throwable -> L6e
            if (r9 == 0) goto L46
            java.lang.String r9 = "accelerometer_rotation_secondary"
            int r9 = android.provider.Settings.System.getIntForUser(r0, r9, r7, r6)     // Catch: java.lang.Throwable -> L6e
            if (r9 == 0) goto L50
            goto L4e
        L46:
            java.lang.String r9 = "accelerometer_rotation"
            int r9 = android.provider.Settings.System.getIntForUser(r0, r9, r7, r6)     // Catch: java.lang.Throwable -> L6e
            if (r9 == 0) goto L50
        L4e:
            r5 = r7
            goto L51
        L50:
        L51:
            int r9 = r11.mUserRotationMode     // Catch: java.lang.Throwable -> L6e
            if (r9 == r5) goto L5a
            r11.mUserRotationMode = r5     // Catch: java.lang.Throwable -> L6e
            r3 = 1
            r1 = 1
        L5a:
            if (r3 == 0) goto L5f
            r11.updateOrientationListenerLw()     // Catch: java.lang.Throwable -> L6e
        L5f:
            java.lang.String r9 = "camera_autorotate"
            int r6 = android.provider.Settings.Secure.getIntForUser(r0, r9, r7, r6)     // Catch: java.lang.Throwable -> L6e
            int r7 = r11.mCameraRotationMode     // Catch: java.lang.Throwable -> L6e
            if (r7 == r6) goto L6c
            r11.mCameraRotationMode = r6     // Catch: java.lang.Throwable -> L6e
            r1 = 1
        L6c:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L6e
            return r1
        L6e:
            r3 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L6e
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.DisplayRotation.updateSettings():boolean");
    }

    void removeDefaultDisplayRotationChangedCallback() {
        if (com.android.server.wm.DisplayRotationCoordinator.isSecondaryInternalDisplay(this.mDisplayContent)) {
            this.mDisplayRotationCoordinator.removeDefaultDisplayRotationChangedCallback();
        }
    }

    void onSetRequestedOrientation() {
        if (this.mCompatPolicyForImmersiveApps == null || this.mRotationChoiceShownToUserForConfirmation == -1) {
            return;
        }
        this.mOrientationListener.onProposedRotationChanged(this.mRotationChoiceShownToUserForConfirmation);
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.println(prefix + "DisplayRotation");
        pw.println(prefix + "  mCurrentAppOrientation=" + android.content.pm.ActivityInfo.screenOrientationToString(this.mCurrentAppOrientation));
        pw.println(prefix + "  mLastOrientation=" + this.mLastOrientation);
        pw.print(prefix + "  mRotation=" + this.mRotation);
        pw.println(" mDeferredRotationPauseCount=" + this.mDeferredRotationPauseCount);
        pw.print(prefix + "  mLandscapeRotation=" + android.view.Surface.rotationToString(this.mLandscapeRotation));
        pw.println(" mSeascapeRotation=" + android.view.Surface.rotationToString(this.mSeascapeRotation));
        pw.print(prefix + "  mPortraitRotation=" + android.view.Surface.rotationToString(this.mPortraitRotation));
        pw.println(" mUpsideDownRotation=" + android.view.Surface.rotationToString(this.mUpsideDownRotation));
        pw.println(prefix + "  mSupportAutoRotation=" + this.mSupportAutoRotation);
        pw.println(prefix + "  mSeamlessRotationCount=" + this.mSeamlessRotationCount);
        pw.println(prefix + "  mDemoRotationLock=" + this.mDemoRotationLock);
        pw.println(prefix + "  mDemoRotation=" + this.mDemoRotation);
        pw.println(prefix + "  hookIsWifiDisplayConnected=" + this.mDisplayRotationSocExt.hookIsWifiDisplayConnected());
        pw.println(prefix + "  hookGetWifiDisplayRotation()=" + this.mDisplayRotationSocExt.hookGetWifiDisplayRotation());
        if (this.mOrientationListener != null) {
            this.mOrientationListener.dump(pw, prefix + "  ");
        }
        pw.println();
        pw.print(prefix + "  mCarDockRotation=" + android.view.Surface.rotationToString(this.mCarDockRotation));
        pw.println(" mDeskDockRotation=" + android.view.Surface.rotationToString(this.mDeskDockRotation));
        pw.print(prefix + "  mUserRotationMode=" + com.android.server.policy.WindowManagerPolicy.userRotationModeToString(this.mUserRotationMode));
        pw.print(" mUserRotation=" + android.view.Surface.rotationToString(this.mUserRotation));
        pw.print(" mCameraRotationMode=" + this.mCameraRotationMode);
        pw.println(" mAllowAllRotations=" + allowAllRotationsToString(this.mAllowAllRotations));
        pw.print(prefix + "  mDemoHdmiRotation=" + android.view.Surface.rotationToString(this.mDemoHdmiRotation));
        pw.print(" mDemoHdmiRotationLock=" + this.mDemoHdmiRotationLock);
        pw.println(" mUndockedHdmiRotation=" + android.view.Surface.rotationToString(this.mUndockedHdmiRotation));
        pw.println(prefix + "  mLidOpenRotation=" + android.view.Surface.rotationToString(this.mLidOpenRotation));
        pw.println(prefix + "  mFixedToUserRotation=" + isFixedToUserRotation());
        if (this.mFoldController != null) {
            pw.println(prefix + "FoldController");
            pw.println(prefix + "  mPauseAutorotationDuringUnfolding=" + this.mFoldController.mPauseAutorotationDuringUnfolding);
            pw.println(prefix + "  mShouldDisableRotationSensor=" + this.mFoldController.mShouldDisableRotationSensor);
            pw.println(prefix + "  mShouldIgnoreSensorRotation=" + this.mFoldController.mShouldIgnoreSensorRotation);
            pw.println(prefix + "  mLastDisplaySwitchTime=" + this.mFoldController.mLastDisplaySwitchTime);
            pw.println(prefix + "  mLastHingeAngleEventTime=" + this.mFoldController.mLastHingeAngleEventTime);
            pw.println(prefix + "  mDeviceState=" + this.mFoldController.mDeviceState);
        }
        this.mDisplayRotationExt.dumpRotationPauseRecord(prefix, pw);
        if (!this.mRotationHistory.mRecords.isEmpty()) {
            pw.println();
            pw.println(prefix + "  RotationHistory");
            prefix = "    " + prefix;
            for (com.android.server.wm.DisplayRotation.RotationHistory.Record r : this.mRotationHistory.mRecords) {
                r.dump(prefix, pw);
            }
        }
        if (!this.mRotationLockHistory.mRecords.isEmpty()) {
            pw.println();
            pw.println(prefix + "  RotationLockHistory");
            java.lang.String prefix2 = "    " + prefix;
            for (com.android.server.wm.DisplayRotation.RotationLockHistory.Record r2 : this.mRotationLockHistory.mRecords) {
                r2.dump(prefix2, pw);
            }
        }
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, getRotation());
        proto.write(1133871366146L, isRotationFrozen());
        proto.write(1120986464259L, getUserRotation());
        proto.write(1120986464260L, this.mFixedToUserRotation);
        proto.write(1120986464261L, this.mLastOrientation);
        proto.write(1133871366150L, isFixedToUserRotation());
        proto.end(token);
    }

    boolean isDeviceInPosture(com.android.server.wm.DeviceStateController.DeviceState state, boolean isTabletop) {
        if (this.mFoldController == null) {
            return false;
        }
        return this.mFoldController.isDeviceInPosture(state, isTabletop);
    }

    boolean isDisplaySeparatingHinge() {
        return this.mFoldController != null && this.mFoldController.isSeparatingHinge();
    }

    void foldStateChanged(com.android.server.wm.DeviceStateController.DeviceState deviceState) {
        if (this.mFoldController != null) {
            synchronized (this.mLock) {
                this.mFoldController.foldStateChanged(deviceState);
            }
        }
    }

    void physicalDisplayChanged() {
        if (this.mFoldController != null) {
            this.mFoldController.onPhysicalDisplayChanged();
        }
    }

    int getDemoUserRotationOverride() {
        return android.os.SystemProperties.getInt("persist.demo.userrotation", 0);
    }

    java.lang.String getDemoUserRotationPackage() {
        return android.os.SystemProperties.get("persist.demo.userrotation.package_name");
    }

    private int getUserRotationOverride() {
        int userRotationOverride = getDemoUserRotationOverride();
        if (userRotationOverride == 0) {
            return userRotationOverride;
        }
        android.view.Display display = this.mDisplayContent.getDisplay();
        if (display.getType() == 2 || display.getType() == 4) {
            return userRotationOverride;
        }
        if (display.getType() == 5) {
            java.lang.String packageName = getDemoUserRotationPackage();
            if (!packageName.isEmpty() && packageName.equals(display.getOwnerPackageName())) {
                return userRotationOverride;
            }
            return 0;
        }
        return 0;
    }

    long uptimeMillis() {
        return android.os.SystemClock.uptimeMillis();
    }

    class FoldController {
        private final java.lang.Runnable mActivityBoundsUpdateCallback;
        private final boolean mAllowHalfFoldAutoRotationOverride;
        private int mDisplaySwitchRotationBlockTimeMs;
        private int mHingeAngleRotationBlockTimeMs;
        private android.hardware.SensorEventListener mHingeAngleSensorEventListener;
        private final boolean mIsDisplayAlwaysSeparatingHinge;
        private int mMaxHingeAngle;
        private final boolean mPauseAutorotationDuringUnfolding;
        private android.hardware.SensorManager mSensorManager;
        private boolean mShouldDisableRotationSensor;
        private boolean mShouldIgnoreSensorRotation;
        private int mHalfFoldSavedRotation = -1;
        private com.android.server.wm.DeviceStateController.DeviceState mDeviceState = com.android.server.wm.DeviceStateController.DeviceState.UNKNOWN;
        private long mLastHingeAngleEventTime = 0;
        private long mLastDisplaySwitchTime = 0;
        private boolean mInHalfFoldTransition = false;
        private final java.util.Set<java.lang.Integer> mTabletopRotations = new android.util.ArraySet();

        FoldController() {
            this.mAllowHalfFoldAutoRotationOverride = com.android.server.wm.DisplayRotation.this.mContext.getResources().getBoolean(android.R.bool.config_useSmsAppService);
            int[] tabletop_rotations = com.android.server.wm.DisplayRotation.this.mContext.getResources().getIntArray(android.R.array.config_deviceStatesAvailableForAppRequests);
            if (tabletop_rotations != null) {
                for (int angle : tabletop_rotations) {
                    switch (angle) {
                        case 0:
                            this.mTabletopRotations.add(0);
                            break;
                        case 90:
                            this.mTabletopRotations.add(1);
                            break;
                        case 180:
                            this.mTabletopRotations.add(2);
                            break;
                        case 270:
                            this.mTabletopRotations.add(3);
                            break;
                        default:
                            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[4]) {
                                long protoLogParam0 = angle;
                                com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 5325136615007859122L, 1, null, java.lang.Long.valueOf(protoLogParam0));
                            }
                            break;
                    }
                }
            } else if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[3]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 4616480353797749295L, 0, null, null);
            }
            this.mIsDisplayAlwaysSeparatingHinge = com.android.server.wm.DisplayRotation.this.mContext.getResources().getBoolean(android.R.bool.config_guestUserAutoCreated);
            this.mActivityBoundsUpdateCallback = new com.android.server.wm.DisplayRotation.FoldController.AnonymousClass1(com.android.server.wm.DisplayRotation.this);
            this.mPauseAutorotationDuringUnfolding = com.android.server.wm.DisplayRotation.this.mContext.getResources().getBoolean(android.R.bool.config_useSystemProvidedLauncherForSecondary);
            if (this.mPauseAutorotationDuringUnfolding) {
                this.mDisplaySwitchRotationBlockTimeMs = com.android.server.wm.DisplayRotation.this.mContext.getResources().getInteger(android.R.integer.config_notificationServiceArchiveSize);
                this.mHingeAngleRotationBlockTimeMs = com.android.server.wm.DisplayRotation.this.mContext.getResources().getInteger(android.R.integer.config_notificationStripRemoteViewSizeBytes);
                this.mMaxHingeAngle = com.android.server.wm.DisplayRotation.this.mContext.getResources().getInteger(android.R.integer.config_notificationWarnRemoteViewSizeBytes);
                registerSensorManager();
            }
        }

        /* JADX INFO: renamed from: com.android.server.wm.DisplayRotation$FoldController$1, reason: invalid class name */
        class AnonymousClass1 implements java.lang.Runnable {
            final /* synthetic */ com.android.server.wm.DisplayRotation val$this$0;

            AnonymousClass1(com.android.server.wm.DisplayRotation displayRotation) {
                this.val$this$0 = displayRotation;
            }

            @Override // java.lang.Runnable
            public void run() {
                com.android.server.wm.ActivityRecord top;
                if (com.android.server.wm.DisplayRotation.FoldController.this.mDeviceState == com.android.server.wm.DeviceStateController.DeviceState.OPEN || com.android.server.wm.DisplayRotation.FoldController.this.mDeviceState == com.android.server.wm.DeviceStateController.DeviceState.HALF_FOLDED) {
                    synchronized (com.android.server.wm.DisplayRotation.this.mLock) {
                        com.android.server.wm.Task topFullscreenTask = com.android.server.wm.DisplayRotation.this.mDisplayContent.getTask(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayRotation$FoldController$1$$ExternalSyntheticLambda0
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj) {
                                return com.android.server.wm.DisplayRotation.FoldController.AnonymousClass1.lambda$run$0((com.android.server.wm.Task) obj);
                            }
                        });
                        if (topFullscreenTask != null && (top = topFullscreenTask.topRunningActivity()) != null) {
                            top.recomputeConfiguration();
                        }
                    }
                }
            }

            static /* synthetic */ boolean lambda$run$0(com.android.server.wm.Task t) {
                return t.getWindowingMode() == 1;
            }
        }

        private void registerSensorManager() {
            android.hardware.Sensor hingeAngleSensor;
            this.mSensorManager = (android.hardware.SensorManager) com.android.server.wm.DisplayRotation.this.mContext.getSystemService(android.hardware.SensorManager.class);
            if (this.mSensorManager != null && (hingeAngleSensor = this.mSensorManager.getDefaultSensor(36)) != null) {
                this.mHingeAngleSensorEventListener = new android.hardware.SensorEventListener() { // from class: com.android.server.wm.DisplayRotation.FoldController.2
                    @Override // android.hardware.SensorEventListener
                    public void onSensorChanged(android.hardware.SensorEvent event) {
                        com.android.server.wm.DisplayRotation.FoldController.this.onHingeAngleChanged(event.values[0]);
                    }

                    @Override // android.hardware.SensorEventListener
                    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
                    }
                };
                this.mSensorManager.registerListener(this.mHingeAngleSensorEventListener, hingeAngleSensor, 0, com.android.server.wm.DisplayRotation.this.getHandler());
            }
        }

        void onDisplayRemoved() {
            if (this.mSensorManager != null && this.mHingeAngleSensorEventListener != null) {
                this.mSensorManager.unregisterListener(this.mHingeAngleSensorEventListener);
            }
        }

        boolean isDeviceInPosture(com.android.server.wm.DeviceStateController.DeviceState state, boolean isTabletop) {
            if (state != this.mDeviceState) {
                return false;
            }
            return this.mDeviceState != com.android.server.wm.DeviceStateController.DeviceState.HALF_FOLDED || isTabletop == this.mTabletopRotations.contains(java.lang.Integer.valueOf(com.android.server.wm.DisplayRotation.this.mRotation));
        }

        com.android.server.wm.DeviceStateController.DeviceState getFoldState() {
            return this.mDeviceState;
        }

        boolean isSeparatingHinge() {
            return this.mDeviceState == com.android.server.wm.DeviceStateController.DeviceState.HALF_FOLDED || (this.mDeviceState == com.android.server.wm.DeviceStateController.DeviceState.OPEN && this.mIsDisplayAlwaysSeparatingHinge);
        }

        boolean overrideFrozenRotation() {
            return this.mAllowHalfFoldAutoRotationOverride && this.mDeviceState == com.android.server.wm.DeviceStateController.DeviceState.HALF_FOLDED;
        }

        boolean shouldRevertOverriddenRotation() {
            return this.mAllowHalfFoldAutoRotationOverride && this.mDeviceState == com.android.server.wm.DeviceStateController.DeviceState.OPEN && !this.mShouldIgnoreSensorRotation && this.mInHalfFoldTransition && com.android.server.wm.DisplayRotation.this.mDisplayContent.getRotationReversionController().isOverrideActive(2) && com.android.server.wm.DisplayRotation.this.mUserRotationMode == 1;
        }

        int revertOverriddenRotation() {
            int savedRotation = this.mHalfFoldSavedRotation;
            this.mHalfFoldSavedRotation = -1;
            com.android.server.wm.DisplayRotation.this.mDisplayContent.getRotationReversionController().revertOverride(2);
            this.mInHalfFoldTransition = false;
            return savedRotation;
        }

        void foldStateChanged(com.android.server.wm.DeviceStateController.DeviceState newState) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                long protoLogParam0 = com.android.server.wm.DisplayRotation.this.mDisplayContent.getDisplayId();
                java.lang.String protoLogParam1 = java.lang.String.valueOf(newState.name());
                long protoLogParam2 = this.mHalfFoldSavedRotation;
                long protoLogParam3 = com.android.server.wm.DisplayRotation.this.mUserRotation;
                long protoLogParam4 = com.android.server.wm.DisplayRotation.this.mLastSensorRotation;
                long protoLogParam5 = com.android.server.wm.DisplayRotation.this.mLastOrientation;
                long protoLogParam6 = com.android.server.wm.DisplayRotation.this.mRotation;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 8852346340572084230L, 5457, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1, java.lang.Long.valueOf(protoLogParam2), java.lang.Long.valueOf(protoLogParam3), java.lang.Long.valueOf(protoLogParam4), java.lang.Long.valueOf(protoLogParam5), java.lang.Long.valueOf(protoLogParam6));
            }
            if (this.mDeviceState == com.android.server.wm.DeviceStateController.DeviceState.UNKNOWN) {
                this.mDeviceState = newState;
                return;
            }
            if (newState == com.android.server.wm.DeviceStateController.DeviceState.HALF_FOLDED && this.mDeviceState != com.android.server.wm.DeviceStateController.DeviceState.HALF_FOLDED) {
                com.android.server.wm.DisplayRotation.this.mDisplayContent.getRotationReversionController().beforeOverrideApplied(2);
                this.mHalfFoldSavedRotation = com.android.server.wm.DisplayRotation.this.mRotation;
                this.mDeviceState = newState;
                com.android.server.wm.DisplayRotation.this.mService.updateRotation(false, false);
            } else {
                this.mInHalfFoldTransition = true;
                this.mDeviceState = newState;
                com.android.server.wm.DisplayRotation.this.mService.updateRotation(false, false);
            }
            com.android.server.UiThread.getHandler().removeCallbacks(this.mActivityBoundsUpdateCallback);
            com.android.server.UiThread.getHandler().postDelayed(this.mActivityBoundsUpdateCallback, 800L);
        }

        boolean shouldIgnoreSensorRotation() {
            return this.mShouldIgnoreSensorRotation;
        }

        boolean shouldDisableRotationSensor() {
            return this.mShouldDisableRotationSensor;
        }

        private void updateSensorRotationBlockIfNeeded() {
            long currentTime = com.android.server.wm.DisplayRotation.this.uptimeMillis();
            boolean newShouldIgnoreRotation = currentTime - this.mLastDisplaySwitchTime < ((long) this.mDisplaySwitchRotationBlockTimeMs) || currentTime - this.mLastHingeAngleEventTime < ((long) this.mHingeAngleRotationBlockTimeMs);
            if (newShouldIgnoreRotation != this.mShouldIgnoreSensorRotation) {
                this.mShouldIgnoreSensorRotation = newShouldIgnoreRotation;
                if (!this.mShouldIgnoreSensorRotation) {
                    if (this.mShouldDisableRotationSensor) {
                        this.mShouldDisableRotationSensor = false;
                        com.android.server.wm.DisplayRotation.this.updateOrientationListenerLw();
                    } else {
                        com.android.server.wm.DisplayRotation.this.updateRotationAndSendNewConfigIfChanged();
                    }
                }
            }
        }

        void onPhysicalDisplayChanged() {
            if (this.mPauseAutorotationDuringUnfolding) {
                this.mLastDisplaySwitchTime = com.android.server.wm.DisplayRotation.this.uptimeMillis();
                boolean isUnfolding = this.mDeviceState == com.android.server.wm.DeviceStateController.DeviceState.OPEN || this.mDeviceState == com.android.server.wm.DeviceStateController.DeviceState.HALF_FOLDED;
                if (isUnfolding) {
                    this.mShouldDisableRotationSensor = true;
                    com.android.server.wm.DisplayRotation.this.updateOrientationListenerLw();
                }
                updateSensorRotationBlockIfNeeded();
                com.android.server.wm.DisplayRotation.this.getHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayRotation$FoldController$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onPhysicalDisplayChanged$0();
                    }
                }, this.mDisplaySwitchRotationBlockTimeMs);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPhysicalDisplayChanged$0() {
            synchronized (com.android.server.wm.DisplayRotation.this.mLock) {
                updateSensorRotationBlockIfNeeded();
            }
        }

        void onHingeAngleChanged(float hingeAngle) {
            if (hingeAngle < this.mMaxHingeAngle) {
                this.mLastHingeAngleEventTime = com.android.server.wm.DisplayRotation.this.uptimeMillis();
                updateSensorRotationBlockIfNeeded();
                com.android.server.wm.DisplayRotation.this.getHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.wm.DisplayRotation$FoldController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onHingeAngleChanged$1();
                    }
                }, this.mHingeAngleRotationBlockTimeMs);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHingeAngleChanged$1() {
            synchronized (com.android.server.wm.DisplayRotation.this.mLock) {
                updateSensorRotationBlockIfNeeded();
            }
        }
    }

    android.os.Handler getHandler() {
        return this.mService.mH;
    }

    private class OrientationListener extends com.android.server.wm.WindowOrientationListener implements java.lang.Runnable {
        transient boolean mEnabled;

        OrientationListener(android.content.Context context, android.os.Handler handler, int defaultRotation) {
            super(context, handler, defaultRotation);
        }

        @Override // com.android.server.wm.WindowOrientationListener
        public boolean isKeyguardShowingAndNotOccluded() {
            return com.android.server.wm.DisplayRotation.this.mService.isKeyguardShowingAndNotOccluded();
        }

        @Override // com.android.server.wm.WindowOrientationListener
        public boolean isRotationResolverEnabled() {
            return com.android.server.wm.DisplayRotation.this.mAllowRotationResolver && com.android.server.wm.DisplayRotation.this.mUserRotationMode == 0 && com.android.server.wm.DisplayRotation.this.mCameraRotationMode == 1 && !com.android.server.wm.DisplayRotation.this.mService.mPowerManager.isPowerSaveMode();
        }

        @Override // com.android.server.wm.WindowOrientationListener
        public void onProposedRotationChanged(int rotation) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                long protoLogParam0 = rotation;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, -8674269704471038429L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
            com.android.server.wm.DisplayRotation.this.mDisplayRotationExt.onProposedRotationChanged(rotation, com.android.server.wm.DisplayRotation.this.mUserRotationMode, com.android.server.wm.DisplayRotation.this.mDisplayContent);
            if (this.mEnabled && com.android.server.wm.DisplayRotation.this.mRotation != rotation && com.android.server.wm.DisplayRotation.this.mUserRotationMode != 1) {
                com.android.server.wm.DisplayRotation.this.mDisplayRotationExt.setSensorRotationChanged(com.android.server.wm.DisplayRotation.this.mDisplayContent, true);
            }
            com.android.server.wm.DisplayRotation.this.mService.mPowerManagerInternal.setPowerBoost(0, 0);
            com.android.server.wm.DisplayRotation.this.dispatchProposedRotation(rotation);
            if (com.android.server.wm.DisplayRotation.this.isRotationChoiceAllowed(rotation)) {
                if (com.android.server.wm.DisplayRotation.this.mDisplayRotationExt.skipSendProposedRotationChangeToStatusBar(com.android.server.wm.DisplayRotation.this.mCurrentAppOrientation, com.android.server.wm.DisplayRotation.this.mDisplayContent)) {
                    return;
                }
                com.android.server.wm.DisplayRotation.this.mRotationChoiceShownToUserForConfirmation = rotation;
                boolean isValid = com.android.server.wm.DisplayRotation.this.isValidRotationChoice(rotation);
                com.android.server.wm.DisplayRotation.this.sendProposedRotationChangeToStatusBarInternal(rotation, isValid);
                return;
            }
            com.android.server.wm.DisplayRotation.this.mRotationChoiceShownToUserForConfirmation = -1;
            com.android.server.wm.DisplayRotation.this.mService.updateRotation(false, false);
            if (com.android.server.wm.DisplayRotation.this.mService.mPolicy instanceof com.android.server.policy.PhoneWindowManager) {
                ((com.android.server.policy.PhoneWindowManager) com.android.server.wm.DisplayRotation.this.mService.mPolicy).getWrapper().getExtImpl().sendWindowDrawCompleteMsg(com.android.server.wm.DisplayRotation.this.mDisplayContent.getDisplayId());
            }
        }

        @Override // com.android.server.wm.WindowOrientationListener
        public void enable() {
            this.mEnabled = true;
            getHandler().post(this);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 418312772547457152L, 0, null, null);
            }
        }

        @Override // com.android.server.wm.WindowOrientationListener
        public void disable() {
            this.mEnabled = false;
            getHandler().post(this);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_ORIENTATION_enabled[1]) {
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_ORIENTATION, 4641814558273780952L, 0, null, null);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mEnabled) {
                super.enable();
            } else {
                super.disable();
            }
        }
    }

    private class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        void observe() {
            android.content.ContentResolver resolver = com.android.server.wm.DisplayRotation.this.mContext.getContentResolver();
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("show_rotation_suggestions"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.System.getUriFor("accelerometer_rotation"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.System.getUriFor("user_rotation"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("camera_autorotate"), false, this, -1);
            if (com.android.server.wm.DisplayRotation.this.mDisplayRotationExt.isSecondDisplay(com.android.server.wm.DisplayRotation.this.mDisplayContent)) {
                resolver.registerContentObserver(android.provider.Settings.System.getUriFor("accelerometer_rotation_secondary"), false, this, -1);
            }
            com.android.server.wm.DisplayRotation.this.updateSettings();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            if (com.android.server.wm.DisplayRotation.this.updateSettings()) {
                com.android.server.wm.DisplayRotation.this.mDisplayRotationExt.forceUpdateRotationForCanvas(true);
                com.android.server.wm.DisplayRotation.this.mService.updateRotation(false, false);
            }
        }
    }

    private static class RotationLockHistory {
        private static final int MAX_SIZE = 8;
        private final java.util.ArrayDeque<com.android.server.wm.DisplayRotation.RotationLockHistory.Record> mRecords;

        private RotationLockHistory() {
            this.mRecords = new java.util.ArrayDeque<>(8);
        }

        private static class Record {
            final java.lang.String mCaller;
            final long mTimestamp;
            final int mUserRotation;
            final int mUserRotationMode;

            private Record(int userRotationMode, int userRotation, java.lang.String caller) {
                this.mTimestamp = java.lang.System.currentTimeMillis();
                this.mUserRotationMode = userRotationMode;
                this.mUserRotation = userRotation;
                this.mCaller = caller;
            }

            void dump(java.lang.String prefix, java.io.PrintWriter pw) {
                pw.println(prefix + android.util.TimeUtils.logTimeOfDay(this.mTimestamp) + ": mode=" + com.android.server.policy.WindowManagerPolicy.userRotationModeToString(this.mUserRotationMode) + ", rotation=" + android.view.Surface.rotationToString(this.mUserRotation) + ", caller=" + this.mCaller);
            }
        }

        void addRecord(int userRotationMode, int userRotation, java.lang.String caller) {
            if (this.mRecords.size() >= 8) {
                this.mRecords.removeFirst();
            }
            this.mRecords.addLast(new com.android.server.wm.DisplayRotation.RotationLockHistory.Record(userRotationMode, userRotation, caller));
        }
    }

    private static class RotationHistory {
        private static final int MAX_SIZE = 8;
        private static final int NO_FOLD_CONTROLLER = -2;
        final java.util.ArrayDeque<com.android.server.wm.DisplayRotation.RotationHistory.Record> mRecords;

        private RotationHistory() {
            this.mRecords = new java.util.ArrayDeque<>(8);
        }

        private static class Record {
            final com.android.server.wm.DeviceStateController.DeviceState mDeviceState;
            final java.lang.String mDisplayRotationCompatPolicySummary;
            final int mFromRotation;
            final int mHalfFoldSavedRotation;
            final boolean mIgnoreOrientationRequest;
            final boolean mInHalfFoldTransition;
            final java.lang.String mLastOrientationSource;
            final java.lang.String mNonDefaultRequestingTaskDisplayArea;
            final boolean[] mRotationReversionSlots;
            final int mSensorRotation;
            final int mSourceOrientation;
            final long mTimestamp = java.lang.System.currentTimeMillis();
            final int mToRotation;
            final int mUserRotation;
            final int mUserRotationMode;

            Record(com.android.server.wm.DisplayRotation dr, int fromRotation, int toRotation) {
                int i;
                java.lang.String string;
                int overrideOrientation;
                this.mFromRotation = fromRotation;
                this.mToRotation = toRotation;
                this.mUserRotation = dr.mUserRotation;
                this.mUserRotationMode = dr.mUserRotationMode;
                com.android.server.wm.DisplayRotation.OrientationListener listener = dr.mOrientationListener;
                if (listener == null || !listener.mEnabled) {
                    i = -2;
                } else {
                    i = dr.mLastSensorRotation;
                }
                this.mSensorRotation = i;
                com.android.server.wm.DisplayContent dc = dr.mDisplayContent;
                this.mIgnoreOrientationRequest = dc.getIgnoreOrientationRequest();
                com.android.server.wm.TaskDisplayArea requestingTda = dc.getOrientationRequestingTaskDisplayArea();
                if (requestingTda == null) {
                    string = "none";
                } else if (requestingTda == dc.getDefaultTaskDisplayArea()) {
                    string = null;
                } else {
                    string = requestingTda.toString();
                }
                this.mNonDefaultRequestingTaskDisplayArea = string;
                com.android.server.wm.WindowContainer<?> source = dc.getLastOrientationSource();
                if (source != null) {
                    this.mLastOrientationSource = source.toString();
                    com.android.server.wm.WindowState w = source.asWindowState();
                    if (w != null) {
                        overrideOrientation = w.mAttrs.screenOrientation;
                    } else {
                        overrideOrientation = source.getOverrideOrientation();
                    }
                    this.mSourceOrientation = overrideOrientation;
                } else {
                    this.mLastOrientationSource = null;
                    this.mSourceOrientation = -2;
                }
                if (dr.mFoldController != null) {
                    this.mHalfFoldSavedRotation = dr.mFoldController.mHalfFoldSavedRotation;
                    this.mInHalfFoldTransition = dr.mFoldController.mInHalfFoldTransition;
                    this.mDeviceState = dr.mFoldController.mDeviceState;
                } else {
                    this.mHalfFoldSavedRotation = -2;
                    this.mInHalfFoldTransition = false;
                    this.mDeviceState = com.android.server.wm.DeviceStateController.DeviceState.UNKNOWN;
                }
                this.mDisplayRotationCompatPolicySummary = dc.mDisplayRotationCompatPolicy != null ? dc.mDisplayRotationCompatPolicy.getSummaryForDisplayRotationHistoryRecord() : null;
                this.mRotationReversionSlots = dr.mDisplayContent.getRotationReversionController().getSlotsCopy();
            }

            void dump(java.lang.String prefix, java.io.PrintWriter pw) {
                pw.println(prefix + android.util.TimeUtils.logTimeOfDay(this.mTimestamp) + " " + android.view.Surface.rotationToString(this.mFromRotation) + " to " + android.view.Surface.rotationToString(this.mToRotation));
                pw.println(prefix + "  source=" + this.mLastOrientationSource + " " + android.content.pm.ActivityInfo.screenOrientationToString(this.mSourceOrientation));
                pw.println(prefix + "  mode=" + com.android.server.policy.WindowManagerPolicy.userRotationModeToString(this.mUserRotationMode) + " user=" + android.view.Surface.rotationToString(this.mUserRotation) + " sensor=" + android.view.Surface.rotationToString(this.mSensorRotation));
                if (this.mIgnoreOrientationRequest) {
                    pw.println(prefix + "  ignoreRequest=true");
                }
                if (this.mNonDefaultRequestingTaskDisplayArea != null) {
                    pw.println(prefix + "  requestingTda=" + this.mNonDefaultRequestingTaskDisplayArea);
                }
                if (this.mHalfFoldSavedRotation != -2) {
                    pw.println(prefix + " halfFoldSavedRotation=" + this.mHalfFoldSavedRotation + " mInHalfFoldTransition=" + this.mInHalfFoldTransition + " mFoldState=" + this.mDeviceState);
                }
                if (this.mDisplayRotationCompatPolicySummary != null) {
                    pw.println(prefix + this.mDisplayRotationCompatPolicySummary);
                }
                if (this.mRotationReversionSlots != null) {
                    pw.println(prefix + " reversionSlots= NOSENSOR " + this.mRotationReversionSlots[0] + ", CAMERA " + this.mRotationReversionSlots[1] + " HALF_FOLD " + this.mRotationReversionSlots[2]);
                }
            }
        }

        void addRecord(com.android.server.wm.DisplayRotation dr, int toRotation) {
            if (this.mRecords.size() >= 8) {
                this.mRecords.removeFirst();
            }
            int fromRotation = dr.mDisplayContent.getWindowConfiguration().getRotation();
            this.mRecords.addLast(new com.android.server.wm.DisplayRotation.RotationHistory.Record(dr, fromRotation, toRotation));
        }
    }
}
