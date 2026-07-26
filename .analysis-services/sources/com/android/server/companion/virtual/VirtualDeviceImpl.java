package com.android.server.companion.virtual;

/* JADX INFO: loaded from: classes.dex */
final class VirtualDeviceImpl extends android.companion.virtual.IVirtualDevice.Stub implements android.os.IBinder.DeathRecipient, com.android.server.companion.virtual.GenericWindowPolicyController.RunningAppsChangedListener {
    private static final int DEFAULT_VIRTUAL_DISPLAY_FLAGS = 24896;
    private static final int DEFAULT_VIRTUAL_DISPLAY_FLAGS_PRE_VIC = 137;
    private static final long PENDING_TRAMPOLINE_TIMEOUT_MS = 5000;
    private static final java.lang.String PERSISTENT_ID_PREFIX_CDM_ASSOCIATION = "companion:";
    private static final java.lang.String TAG = "VirtualDeviceImpl";
    private final android.companion.virtual.IVirtualDeviceActivityListener mActivityListener;
    private final java.util.Set<android.content.ComponentName> mActivityPolicyExemptions;
    private final android.os.IBinder mAppToken;
    private final android.companion.AssociationInfo mAssociationInfo;
    private final android.content.AttributionSource mAttributionSource;
    private final int mBaseVirtualDisplayFlags;
    private final com.android.server.companion.virtual.CameraAccessController mCameraAccessController;
    private final android.content.Context mContext;
    private boolean mDefaultShowPointerIcon;
    private final int mDeviceId;
    private final android.util.SparseIntArray mDevicePolicies;
    private final android.hardware.display.DisplayManagerGlobal mDisplayManager;
    private final android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    private final com.android.server.companion.virtual.InputController mInputController;
    private final java.util.Map<android.os.IBinder, android.content.IntentFilter> mIntentInterceptors;
    private android.os.LocaleList mLocaleList;
    private final java.lang.String mOwnerPackageName;
    private final int mOwnerUid;
    private final android.companion.virtual.VirtualDeviceParams mParams;
    private final com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampolineCallback mPendingTrampolineCallback;
    private final android.content.ComponentName mPermissionDialogComponent;
    private final java.lang.String mPersistentDeviceId;
    private final android.companion.virtual.VirtualDevice mPublicVirtualDeviceObject;
    private final java.util.function.Consumer<android.util.ArraySet<java.lang.Integer>> mRunningAppsChangedCallback;
    private final com.android.server.companion.virtual.SensorController mSensorController;
    private final com.android.server.companion.virtual.VirtualDeviceManagerService mService;
    private final android.companion.virtual.IVirtualDeviceSoundEffectListener mSoundEffectListener;
    private com.android.server.companion.virtual.audio.VirtualAudioController mVirtualAudioController;
    private final com.android.server.companion.virtual.camera.VirtualCameraController mVirtualCameraController;
    private final java.lang.Object mVirtualDeviceLock;
    private final com.android.server.companion.virtual.VirtualDeviceLog mVirtualDeviceLog;
    private final android.util.SparseArray<com.android.server.companion.virtual.VirtualDeviceImpl.VirtualDisplayWrapper> mVirtualDisplays;

    interface PendingTrampolineCallback {
        void startWaitingForPendingTrampoline(com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline pendingTrampoline);

        void stopWaitingForPendingTrampoline(com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline pendingTrampoline);
    }

    @dalvik.annotation.optimization.FastNative
    private static native boolean nativeVirtualCameraServiceBuildFlagEnabled();

    private android.companion.virtual.VirtualDeviceManager.ActivityListener createListenerAdapter() {
        return new android.companion.virtual.VirtualDeviceManager.ActivityListener() { // from class: com.android.server.companion.virtual.VirtualDeviceImpl.1
            public void onTopActivityChanged(int displayId, android.content.ComponentName topActivity) {
                try {
                    com.android.server.companion.virtual.VirtualDeviceImpl.this.mActivityListener.onTopActivityChanged(displayId, topActivity, -10000);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.companion.virtual.VirtualDeviceImpl.TAG, "Unable to call mActivityListener for display: " + displayId, e);
                }
            }

            public void onTopActivityChanged(int displayId, android.content.ComponentName topActivity, int userId) {
                try {
                    com.android.server.companion.virtual.VirtualDeviceImpl.this.mActivityListener.onTopActivityChanged(displayId, topActivity, userId);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.companion.virtual.VirtualDeviceImpl.TAG, "Unable to call mActivityListener for display: " + displayId, e);
                }
            }

            public void onDisplayEmpty(int displayId) {
                try {
                    com.android.server.companion.virtual.VirtualDeviceImpl.this.mActivityListener.onDisplayEmpty(displayId);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.companion.virtual.VirtualDeviceImpl.TAG, "Unable to call mActivityListener for display: " + displayId, e);
                }
            }
        };
    }

    VirtualDeviceImpl(android.content.Context context, android.companion.AssociationInfo associationInfo, com.android.server.companion.virtual.VirtualDeviceManagerService service, com.android.server.companion.virtual.VirtualDeviceLog virtualDeviceLog, android.os.IBinder token, android.content.AttributionSource attributionSource, int deviceId, com.android.server.companion.virtual.CameraAccessController cameraAccessController, com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampolineCallback pendingTrampolineCallback, android.companion.virtual.IVirtualDeviceActivityListener activityListener, android.companion.virtual.IVirtualDeviceSoundEffectListener soundEffectListener, java.util.function.Consumer<android.util.ArraySet<java.lang.Integer>> runningAppsChangedCallback, android.companion.virtual.VirtualDeviceParams params) {
        this(context, associationInfo, service, virtualDeviceLog, token, attributionSource, deviceId, null, cameraAccessController, pendingTrampolineCallback, activityListener, soundEffectListener, runningAppsChangedCallback, params, android.hardware.display.DisplayManagerGlobal.getInstance(), isVirtualCameraEnabled() ? new com.android.server.companion.virtual.camera.VirtualCameraController(params.getDevicePolicy(5), deviceId) : null);
    }

    VirtualDeviceImpl(android.content.Context context, android.companion.AssociationInfo associationInfo, com.android.server.companion.virtual.VirtualDeviceManagerService service, com.android.server.companion.virtual.VirtualDeviceLog virtualDeviceLog, android.os.IBinder token, android.content.AttributionSource attributionSource, int deviceId, com.android.server.companion.virtual.InputController inputController, com.android.server.companion.virtual.CameraAccessController cameraAccessController, com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampolineCallback pendingTrampolineCallback, android.companion.virtual.IVirtualDeviceActivityListener activityListener, android.companion.virtual.IVirtualDeviceSoundEffectListener soundEffectListener, java.util.function.Consumer<android.util.ArraySet<java.lang.Integer>> runningAppsChangedCallback, android.companion.virtual.VirtualDeviceParams params, android.hardware.display.DisplayManagerGlobal displayManager, com.android.server.companion.virtual.camera.VirtualCameraController virtualCameraController) {
        java.util.Set<android.content.ComponentName> allowedActivities;
        java.util.Set allowedActivities2;
        super(android.os.PermissionEnforcer.fromContext(context));
        this.mVirtualDeviceLock = new java.lang.Object();
        this.mVirtualDisplays = new android.util.SparseArray<>();
        this.mIntentInterceptors = new android.util.ArrayMap();
        this.mDefaultShowPointerIcon = true;
        this.mLocaleList = null;
        this.mVirtualDeviceLog = virtualDeviceLog;
        this.mOwnerPackageName = attributionSource.getPackageName();
        this.mAttributionSource = attributionSource;
        android.os.UserHandle ownerUserHandle = android.os.UserHandle.getUserHandleForUid(attributionSource.getUid());
        this.mContext = context.createContextAsUser(ownerUserHandle, 0);
        this.mAssociationInfo = associationInfo;
        this.mPersistentDeviceId = createPersistentDeviceId(associationInfo.getId());
        this.mService = service;
        this.mPendingTrampolineCallback = pendingTrampolineCallback;
        this.mActivityListener = activityListener;
        this.mSoundEffectListener = soundEffectListener;
        this.mRunningAppsChangedCallback = runningAppsChangedCallback;
        this.mOwnerUid = attributionSource.getUid();
        this.mDeviceId = deviceId;
        this.mAppToken = token;
        this.mParams = params;
        this.mDevicePolicies = params.getDevicePolicies();
        this.mDisplayManager = displayManager;
        this.mDisplayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        if (inputController == null) {
            this.mInputController = new com.android.server.companion.virtual.InputController(context.getMainThreadHandler(), (android.view.WindowManager) context.getSystemService(android.view.WindowManager.class), this.mAttributionSource);
        } else {
            this.mInputController = inputController;
        }
        this.mSensorController = new com.android.server.companion.virtual.SensorController(this, this.mDeviceId, this.mAttributionSource, this.mParams.getVirtualSensorCallback(), this.mParams.getVirtualSensorConfigs());
        this.mCameraAccessController = cameraAccessController;
        if (this.mCameraAccessController != null) {
            this.mCameraAccessController.startObservingIfNeeded();
        }
        if (!android.companion.virtual.flags.Flags.streamPermissions()) {
            this.mPermissionDialogComponent = getPermissionDialogComponent();
        } else {
            this.mPermissionDialogComponent = null;
        }
        this.mVirtualCameraController = virtualCameraController;
        try {
            token.linkToDeath(this, 0);
            this.mVirtualDeviceLog.logCreated(deviceId, this.mOwnerUid);
            if (android.companion.virtual.flags.Flags.vdmPublicApis()) {
                this.mPublicVirtualDeviceObject = new android.companion.virtual.VirtualDevice(this, getDeviceId(), getPersistentDeviceId(), this.mParams.getName(), getDisplayName());
            } else {
                this.mPublicVirtualDeviceObject = new android.companion.virtual.VirtualDevice(this, getDeviceId(), getPersistentDeviceId(), this.mParams.getName());
            }
            if (android.companion.virtual.flags.Flags.dynamicPolicy()) {
                if (this.mParams.getDevicePolicy(3) == 0) {
                    allowedActivities2 = this.mParams.getBlockedActivities();
                } else {
                    allowedActivities2 = this.mParams.getAllowedActivities();
                }
                this.mActivityPolicyExemptions = new android.util.ArraySet(allowedActivities2);
            } else {
                if (this.mParams.getDefaultActivityPolicy() == 0) {
                    allowedActivities = this.mParams.getBlockedActivities();
                } else {
                    allowedActivities = this.mParams.getAllowedActivities();
                }
                this.mActivityPolicyExemptions = allowedActivities;
            }
            int flags = DEFAULT_VIRTUAL_DISPLAY_FLAGS;
            flags = android.companion.virtual.flags.Flags.consistentDisplayFlags() ? flags : DEFAULT_VIRTUAL_DISPLAY_FLAGS | 137;
            this.mBaseVirtualDisplayFlags = this.mParams.getLockState() == 1 ? flags | 4096 : flags;
            if (android.companion.virtual.flags.Flags.vdmCustomIme() && this.mParams.getInputMethodComponent() != null) {
                java.lang.String imeId = this.mParams.getInputMethodComponent().flattenToShortString();
                android.util.Slog.d(TAG, "Setting custom input method " + imeId + " as default for virtual device " + deviceId);
                com.android.server.inputmethod.InputMethodManagerInternal.get().setVirtualDeviceInputMethodForAllUsers(this.mDeviceId, imeId);
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    com.android.server.companion.virtual.SensorController getSensorControllerForTest() {
        return this.mSensorController;
    }

    static java.lang.String createPersistentDeviceId(int associationId) {
        return PERSISTENT_ID_PREFIX_CDM_ASSOCIATION + associationId;
    }

    int getBaseVirtualDisplayFlags() {
        return this.mBaseVirtualDisplayFlags;
    }

    com.android.server.companion.virtual.CameraAccessController getCameraAccessController() {
        return this.mCameraAccessController;
    }

    java.lang.CharSequence getDisplayName() {
        return this.mAssociationInfo.getDisplayName();
    }

    android.companion.virtual.VirtualDevice getPublicVirtualDeviceObject() {
        return this.mPublicVirtualDeviceObject;
    }

    android.os.LocaleList getDeviceLocaleList() {
        android.os.LocaleList localeList;
        synchronized (this.mVirtualDeviceLock) {
            localeList = this.mLocaleList;
        }
        return localeList;
    }

    public int getDevicePolicy(int policyType) {
        int i;
        if (android.companion.virtual.flags.Flags.dynamicPolicy()) {
            synchronized (this.mVirtualDeviceLock) {
                i = this.mDevicePolicies.get(policyType, 0);
            }
            return i;
        }
        return this.mParams.getDevicePolicy(policyType);
    }

    public int getAudioPlaybackSessionId() {
        return this.mParams.getAudioPlaybackSessionId();
    }

    public int getAudioRecordingSessionId() {
        return this.mParams.getAudioRecordingSessionId();
    }

    public int getDeviceId() {
        return this.mDeviceId;
    }

    public java.lang.String getPersistentDeviceId() {
        return this.mPersistentDeviceId;
    }

    public int getAssociationId() {
        return this.mAssociationInfo.getId();
    }

    public void launchPendingIntent(int displayId, android.app.PendingIntent pendingIntent, android.os.ResultReceiver resultReceiver) {
        java.util.Objects.requireNonNull(pendingIntent);
        synchronized (this.mVirtualDeviceLock) {
            if (!this.mVirtualDisplays.contains(displayId)) {
                throw new java.lang.SecurityException("Display ID " + displayId + " not found for this virtual device");
            }
        }
        if (pendingIntent.isActivity()) {
            try {
                sendPendingIntent(displayId, pendingIntent);
                resultReceiver.send(0, null);
                return;
            } catch (android.app.PendingIntent.CanceledException e) {
                android.util.Slog.w(TAG, "Pending intent canceled", e);
                resultReceiver.send(1, null);
                return;
            }
        }
        final com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline pendingTrampoline = new com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline(pendingIntent, resultReceiver, displayId);
        this.mPendingTrampolineCallback.startWaitingForPendingTrampoline(pendingTrampoline);
        this.mContext.getMainThreadHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.companion.virtual.VirtualDeviceImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$launchPendingIntent$0(pendingTrampoline);
            }
        }, PENDING_TRAMPOLINE_TIMEOUT_MS);
        try {
            sendPendingIntent(displayId, pendingIntent);
        } catch (android.app.PendingIntent.CanceledException e2) {
            android.util.Slog.w(TAG, "Pending intent canceled", e2);
            resultReceiver.send(1, null);
            this.mPendingTrampolineCallback.stopWaitingForPendingTrampoline(pendingTrampoline);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$launchPendingIntent$0(com.android.server.companion.virtual.VirtualDeviceImpl.PendingTrampoline pendingTrampoline) {
        pendingTrampoline.mResultReceiver.send(2, null);
        this.mPendingTrampolineCallback.stopWaitingForPendingTrampoline(pendingTrampoline);
    }

    public void addActivityPolicyExemption(android.content.ComponentName componentName) {
        super.addActivityPolicyExemption_enforcePermission();
        synchronized (this.mVirtualDeviceLock) {
            if (this.mActivityPolicyExemptions.add(componentName)) {
                for (int i = 0; i < this.mVirtualDisplays.size(); i++) {
                    this.mVirtualDisplays.valueAt(i).getWindowPolicyController().addActivityPolicyExemption(componentName);
                }
            }
        }
    }

    public void removeActivityPolicyExemption(android.content.ComponentName componentName) {
        super.removeActivityPolicyExemption_enforcePermission();
        synchronized (this.mVirtualDeviceLock) {
            if (this.mActivityPolicyExemptions.remove(componentName)) {
                for (int i = 0; i < this.mVirtualDisplays.size(); i++) {
                    this.mVirtualDisplays.valueAt(i).getWindowPolicyController().removeActivityPolicyExemption(componentName);
                }
            }
        }
    }

    private void sendPendingIntent(int displayId, android.app.PendingIntent pendingIntent) throws android.app.PendingIntent.CanceledException {
        android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic().setLaunchDisplayId(displayId);
        options.setPendingIntentBackgroundActivityLaunchAllowed(true);
        options.setPendingIntentBackgroundActivityLaunchAllowedByPermission(true);
        pendingIntent.send(this.mContext, 0, null, null, null, null, options.toBundle());
    }

    public void close() {
        com.android.server.companion.virtual.VirtualDeviceImpl.VirtualDisplayWrapper[] virtualDisplaysToBeReleased;
        super.close_enforcePermission();
        if (!this.mService.removeVirtualDevice(this.mDeviceId)) {
            return;
        }
        this.mVirtualDeviceLog.logClosed(this.mDeviceId, this.mOwnerUid);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mVirtualDeviceLock) {
                if (this.mVirtualAudioController != null) {
                    this.mVirtualAudioController.stopListening();
                    this.mVirtualAudioController = null;
                }
                this.mLocaleList = null;
                virtualDisplaysToBeReleased = new com.android.server.companion.virtual.VirtualDeviceImpl.VirtualDisplayWrapper[this.mVirtualDisplays.size()];
                for (int i = 0; i < this.mVirtualDisplays.size(); i++) {
                    virtualDisplaysToBeReleased[i] = this.mVirtualDisplays.valueAt(i);
                }
                this.mVirtualDisplays.clear();
            }
            for (com.android.server.companion.virtual.VirtualDeviceImpl.VirtualDisplayWrapper virtualDisplayWrapper : virtualDisplaysToBeReleased) {
                this.mDisplayManager.releaseVirtualDisplay(virtualDisplayWrapper.getToken());
                releaseOwnedVirtualDisplayResources(virtualDisplayWrapper);
            }
            this.mAppToken.unlinkToDeath(this, 0);
            if (this.mCameraAccessController != null) {
                this.mCameraAccessController.stopObservingIfNeeded();
            }
            if (android.companion.virtual.flags.Flags.vdmCustomIme() && this.mParams.getInputMethodComponent() != null) {
                com.android.server.inputmethod.InputMethodManagerInternal.get().setVirtualDeviceInputMethodForAllUsers(this.mDeviceId, null);
            }
            this.mInputController.close();
            this.mSensorController.close();
            android.os.Binder.restoreCallingIdentity(ident);
            if (this.mVirtualCameraController != null) {
                this.mVirtualCameraController.close();
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        close();
    }

    @Override // com.android.server.companion.virtual.GenericWindowPolicyController.RunningAppsChangedListener
    public void onRunningAppsChanged(android.util.ArraySet<java.lang.Integer> runningUids) {
        if (this.mCameraAccessController != null) {
            this.mCameraAccessController.blockCameraAccessIfNeeded(runningUids);
        }
        this.mRunningAppsChangedCallback.accept(runningUids);
    }

    com.android.server.companion.virtual.audio.VirtualAudioController getVirtualAudioControllerForTesting() {
        return this.mVirtualAudioController;
    }

    public void onAudioSessionStarting(int displayId, android.companion.virtual.audio.IAudioRoutingCallback routingCallback, android.companion.virtual.audio.IAudioConfigChangedCallback configChangedCallback) {
        super.onAudioSessionStarting_enforcePermission();
        synchronized (this.mVirtualDeviceLock) {
            if (!this.mVirtualDisplays.contains(displayId)) {
                throw new java.lang.SecurityException("Cannot start audio session for a display not associated with this virtual device");
            }
            if (this.mVirtualAudioController == null) {
                this.mVirtualAudioController = new com.android.server.companion.virtual.audio.VirtualAudioController(this.mContext, this.mAttributionSource);
                com.android.server.companion.virtual.GenericWindowPolicyController gwpc = this.mVirtualDisplays.get(displayId).getWindowPolicyController();
                this.mVirtualAudioController.startListening(gwpc, routingCallback, configChangedCallback);
            }
        }
    }

    public void onAudioSessionEnded() {
        super.onAudioSessionEnded_enforcePermission();
        synchronized (this.mVirtualDeviceLock) {
            if (this.mVirtualAudioController != null) {
                this.mVirtualAudioController.stopListening();
                this.mVirtualAudioController = null;
            }
        }
    }

    public void setDevicePolicy(int policyType, int devicePolicy) {
        super.setDevicePolicy_enforcePermission();
        if (!android.companion.virtual.flags.Flags.dynamicPolicy()) {
            return;
        }
        switch (policyType) {
            case 2:
                synchronized (this.mVirtualDeviceLock) {
                    this.mDevicePolicies.put(policyType, devicePolicy);
                    for (int i = 0; i < this.mVirtualDisplays.size(); i++) {
                        this.mVirtualDisplays.valueAt(i).getWindowPolicyController().setShowInHostDeviceRecents(devicePolicy == 0);
                    }
                    break;
                }
                return;
            case 3:
                synchronized (this.mVirtualDeviceLock) {
                    this.mDevicePolicies.put(policyType, devicePolicy);
                    for (int i2 = 0; i2 < this.mVirtualDisplays.size(); i2++) {
                        this.mVirtualDisplays.valueAt(i2).getWindowPolicyController().setActivityLaunchDefaultAllowed(devicePolicy == 0);
                    }
                    break;
                }
                return;
            case 4:
                if (android.companion.virtual.flags.Flags.crossDeviceClipboard()) {
                    synchronized (this.mVirtualDeviceLock) {
                        this.mDevicePolicies.put(policyType, devicePolicy);
                        break;
                    }
                    return;
                }
                return;
            default:
                throw new java.lang.IllegalArgumentException("Device policy " + policyType + " cannot be changed at runtime. ");
        }
    }

    public void createVirtualDpad(android.hardware.input.VirtualDpadConfig config, android.os.IBinder deviceToken) {
        super.createVirtualDpad_enforcePermission();
        java.util.Objects.requireNonNull(config);
        checkVirtualInputDeviceDisplayIdAssociation(config.getAssociatedDisplayId());
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mInputController.createDpad(config.getInputDeviceName(), config.getVendorId(), config.getProductId(), deviceToken, getTargetDisplayIdForInput(config.getAssociatedDisplayId()));
            } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e) {
                throw new java.lang.IllegalArgumentException(e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void createVirtualKeyboard(android.hardware.input.VirtualKeyboardConfig config, android.os.IBinder deviceToken) {
        super.createVirtualKeyboard_enforcePermission();
        java.util.Objects.requireNonNull(config);
        checkVirtualInputDeviceDisplayIdAssociation(config.getAssociatedDisplayId());
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mInputController.createKeyboard(config.getInputDeviceName(), config.getVendorId(), config.getProductId(), deviceToken, getTargetDisplayIdForInput(config.getAssociatedDisplayId()), config.getLanguageTag(), config.getLayoutType());
                synchronized (this.mVirtualDeviceLock) {
                    this.mLocaleList = android.os.LocaleList.forLanguageTags(config.getLanguageTag());
                }
            } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e) {
                throw new java.lang.IllegalArgumentException(e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void createVirtualMouse(android.hardware.input.VirtualMouseConfig config, android.os.IBinder deviceToken) {
        super.createVirtualMouse_enforcePermission();
        java.util.Objects.requireNonNull(config);
        checkVirtualInputDeviceDisplayIdAssociation(config.getAssociatedDisplayId());
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mInputController.createMouse(config.getInputDeviceName(), config.getVendorId(), config.getProductId(), deviceToken, config.getAssociatedDisplayId());
            } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e) {
                throw new java.lang.IllegalArgumentException(e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void createVirtualTouchscreen(android.hardware.input.VirtualTouchscreenConfig config, android.os.IBinder deviceToken) {
        super.createVirtualTouchscreen_enforcePermission();
        java.util.Objects.requireNonNull(config);
        checkVirtualInputDeviceDisplayIdAssociation(config.getAssociatedDisplayId());
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mInputController.createTouchscreen(config.getInputDeviceName(), config.getVendorId(), config.getProductId(), deviceToken, config.getAssociatedDisplayId(), config.getHeight(), config.getWidth());
            } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e) {
                throw new java.lang.IllegalArgumentException(e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void createVirtualNavigationTouchpad(android.hardware.input.VirtualNavigationTouchpadConfig config, android.os.IBinder deviceToken) {
        super.createVirtualNavigationTouchpad_enforcePermission();
        java.util.Objects.requireNonNull(config);
        checkVirtualInputDeviceDisplayIdAssociation(config.getAssociatedDisplayId());
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mInputController.createNavigationTouchpad(config.getInputDeviceName(), config.getVendorId(), config.getProductId(), deviceToken, getTargetDisplayIdForInput(config.getAssociatedDisplayId()), config.getHeight(), config.getWidth());
            } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e) {
                throw new java.lang.IllegalArgumentException(e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void createVirtualStylus(android.hardware.input.VirtualStylusConfig config, android.os.IBinder deviceToken) {
        super.createVirtualStylus_enforcePermission();
        java.util.Objects.requireNonNull(config);
        java.util.Objects.requireNonNull(deviceToken);
        checkVirtualInputDeviceDisplayIdAssociation(config.getAssociatedDisplayId());
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mInputController.createStylus(config.getInputDeviceName(), config.getVendorId(), config.getProductId(), deviceToken, config.getAssociatedDisplayId(), config.getHeight(), config.getWidth());
            } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e) {
                throw new java.lang.IllegalArgumentException(e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void unregisterInputDevice(android.os.IBinder token) {
        super.unregisterInputDevice_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mInputController.unregisterInputDevice(token);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public int getInputDeviceId(android.os.IBinder token) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mInputController.getInputDeviceId(token);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean sendDpadKeyEvent(android.os.IBinder token, android.hardware.input.VirtualKeyEvent event) {
        super.sendDpadKeyEvent_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mInputController.sendDpadKeyEvent(token, event);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean sendKeyEvent(android.os.IBinder token, android.hardware.input.VirtualKeyEvent event) {
        super.sendKeyEvent_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mInputController.sendKeyEvent(token, event);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean sendButtonEvent(android.os.IBinder token, android.hardware.input.VirtualMouseButtonEvent event) {
        super.sendButtonEvent_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mInputController.sendButtonEvent(token, event);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean sendTouchEvent(android.os.IBinder token, android.hardware.input.VirtualTouchEvent event) {
        super.sendTouchEvent_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mInputController.sendTouchEvent(token, event);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean sendRelativeEvent(android.os.IBinder token, android.hardware.input.VirtualMouseRelativeEvent event) {
        super.sendRelativeEvent_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mInputController.sendRelativeEvent(token, event);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean sendScrollEvent(android.os.IBinder token, android.hardware.input.VirtualMouseScrollEvent event) {
        super.sendScrollEvent_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mInputController.sendScrollEvent(token, event);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public android.graphics.PointF getCursorPosition(android.os.IBinder token) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mInputController.getCursorPosition(token);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean sendStylusMotionEvent(android.os.IBinder token, android.hardware.input.VirtualStylusMotionEvent event) {
        super.sendStylusMotionEvent_enforcePermission();
        java.util.Objects.requireNonNull(token);
        java.util.Objects.requireNonNull(event);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mInputController.sendStylusMotionEvent(token, event);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean sendStylusButtonEvent(android.os.IBinder token, android.hardware.input.VirtualStylusButtonEvent event) {
        super.sendStylusButtonEvent_enforcePermission();
        java.util.Objects.requireNonNull(token);
        java.util.Objects.requireNonNull(event);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mInputController.sendStylusButtonEvent(token, event);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void setShowPointerIcon(boolean showPointerIcon) {
        super.setShowPointerIcon_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mVirtualDeviceLock) {
                this.mDefaultShowPointerIcon = showPointerIcon;
            }
            int[] displayIds = getDisplayIds();
            for (int i : displayIds) {
                this.mInputController.setShowPointerIcon(showPointerIcon, i);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void setDisplayImePolicy(int displayId, int policy) {
        super.setDisplayImePolicy_enforcePermission();
        synchronized (this.mVirtualDeviceLock) {
            if (!this.mVirtualDisplays.contains(displayId)) {
                throw new java.lang.SecurityException("Display ID " + displayId + " not found for this virtual device");
            }
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mInputController.setDisplayImePolicy(displayId, policy);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public java.util.List<android.companion.virtual.sensor.VirtualSensor> getVirtualSensorList() {
        super.getVirtualSensorList_enforcePermission();
        return this.mSensorController.getSensorList();
    }

    android.companion.virtual.sensor.VirtualSensor getVirtualSensorByHandle(int handle) {
        return this.mSensorController.getSensorByHandle(handle);
    }

    public boolean sendSensorEvent(android.os.IBinder token, android.companion.virtual.sensor.VirtualSensorEvent event) {
        super.sendSensorEvent_enforcePermission();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mSensorController.sendSensorEvent(token, event);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void registerIntentInterceptor(android.companion.virtual.IVirtualDeviceIntentInterceptor intentInterceptor, android.content.IntentFilter filter) {
        super.registerIntentInterceptor_enforcePermission();
        java.util.Objects.requireNonNull(intentInterceptor);
        java.util.Objects.requireNonNull(filter);
        synchronized (this.mVirtualDeviceLock) {
            this.mIntentInterceptors.put(intentInterceptor.asBinder(), filter);
        }
    }

    public void unregisterIntentInterceptor(android.companion.virtual.IVirtualDeviceIntentInterceptor intentInterceptor) {
        super.unregisterIntentInterceptor_enforcePermission();
        java.util.Objects.requireNonNull(intentInterceptor);
        synchronized (this.mVirtualDeviceLock) {
            this.mIntentInterceptors.remove(intentInterceptor.asBinder());
        }
    }

    public void registerVirtualCamera(android.companion.virtual.camera.VirtualCameraConfig cameraConfig) throws android.os.RemoteException {
        super.registerVirtualCamera_enforcePermission();
        java.util.Objects.requireNonNull(cameraConfig);
        if (this.mVirtualCameraController == null) {
            throw new java.lang.UnsupportedOperationException("Virtual camera controller is not available");
        }
        this.mVirtualCameraController.registerCamera(cameraConfig, this.mAttributionSource);
    }

    public void unregisterVirtualCamera(android.companion.virtual.camera.VirtualCameraConfig cameraConfig) throws android.os.RemoteException {
        super.unregisterVirtualCamera_enforcePermission();
        java.util.Objects.requireNonNull(cameraConfig);
        if (this.mVirtualCameraController == null) {
            throw new java.lang.UnsupportedOperationException("Virtual camera controller is not available");
        }
        this.mVirtualCameraController.unregisterCamera(cameraConfig);
    }

    public java.lang.String getVirtualCameraId(android.companion.virtual.camera.VirtualCameraConfig cameraConfig) throws android.os.RemoteException {
        super.getVirtualCameraId_enforcePermission();
        java.util.Objects.requireNonNull(cameraConfig);
        if (this.mVirtualCameraController == null) {
            throw new java.lang.UnsupportedOperationException("Virtual camera controller is not available");
        }
        return this.mVirtualCameraController.getCameraId(cameraConfig);
    }

    public boolean hasCustomAudioInputSupport() throws android.os.RemoteException {
        return hasCustomAudioInputSupportInternal();
    }

    private boolean hasCustomAudioInputSupportInternal() {
        if (!android.companion.virtual.flags.Flags.vdmPublicApis() || !android.media.audiopolicy.Flags.audioMixTestApi() || !android.media.audiopolicy.Flags.recordAudioDeviceAwarePermission()) {
            return false;
        }
        if (getDevicePolicy(1) == 1) {
            return true;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.media.AudioManager audioManager = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class);
            for (android.media.audiopolicy.AudioMix mix : audioManager.getRegisteredPolicyMixes()) {
                if (mix.matchesVirtualDeviceId(getDeviceId()) && mix.getMixType() == 1) {
                    return true;
                }
            }
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) {
        fout.println("  VirtualDevice: ");
        fout.println("    mDeviceId: " + this.mDeviceId);
        fout.println("    mAssociationId: " + this.mAssociationInfo.getId());
        fout.println("    mOwnerPackageName: " + this.mOwnerPackageName);
        fout.println("    mParams: ");
        this.mParams.dump(fout, "        ");
        fout.println("    mVirtualDisplayIds: ");
        synchronized (this.mVirtualDeviceLock) {
            for (int i = 0; i < this.mVirtualDisplays.size(); i++) {
                fout.println("      " + this.mVirtualDisplays.keyAt(i));
            }
            fout.println("    mDevicePolicies: " + this.mDevicePolicies);
            fout.println("    mDefaultShowPointerIcon: " + this.mDefaultShowPointerIcon);
        }
        this.mInputController.dump(fout);
        this.mSensorController.dump(fout);
        if (this.mVirtualCameraController != null) {
            this.mVirtualCameraController.dump(fout, "    ");
        }
        fout.println("    hasCustomAudioInputSupport: " + hasCustomAudioInputSupportInternal());
    }

    private int getTargetDisplayIdForInput(int displayId) {
        if (!android.companion.virtual.flags.Flags.interactiveScreenMirror()) {
            return displayId;
        }
        android.hardware.display.DisplayManagerInternal displayManager = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        int mirroredDisplayId = displayManager.getDisplayIdToMirror(displayId);
        return mirroredDisplayId == -1 ? displayId : mirroredDisplayId;
    }

    private com.android.server.companion.virtual.GenericWindowPolicyController createWindowPolicyControllerLocked(java.util.Set<java.lang.String> displayCategories) {
        boolean activityLaunchAllowedByDefault;
        java.util.Set allowedCrossTaskNavigations;
        if (android.companion.virtual.flags.Flags.dynamicPolicy()) {
            activityLaunchAllowedByDefault = getDevicePolicy(3) == 0;
        } else {
            activityLaunchAllowedByDefault = this.mParams.getDefaultActivityPolicy() == 0;
        }
        boolean crossTaskNavigationAllowedByDefault = this.mParams.getDefaultNavigationPolicy() == 0;
        boolean showTasksInHostDeviceRecents = getDevicePolicy(2) == 0;
        android.content.ComponentName homeComponent = android.companion.virtual.flags.Flags.vdmCustomHome() ? this.mParams.getHomeComponent() : null;
        android.content.AttributionSource attributionSource = this.mAttributionSource;
        android.util.ArraySet<android.os.UserHandle> allowedUserHandles = getAllowedUserHandles();
        java.util.Set<android.content.ComponentName> set = this.mActivityPolicyExemptions;
        if (crossTaskNavigationAllowedByDefault) {
            allowedCrossTaskNavigations = this.mParams.getBlockedCrossTaskNavigations();
        } else {
            allowedCrossTaskNavigations = this.mParams.getAllowedCrossTaskNavigations();
        }
        com.android.server.companion.virtual.GenericWindowPolicyController gwpc = new com.android.server.companion.virtual.GenericWindowPolicyController(8192, 524288, attributionSource, allowedUserHandles, activityLaunchAllowedByDefault, set, crossTaskNavigationAllowedByDefault, allowedCrossTaskNavigations, this.mPermissionDialogComponent, createListenerAdapter(), new com.android.server.companion.virtual.GenericWindowPolicyController.PipBlockedCallback() { // from class: com.android.server.companion.virtual.VirtualDeviceImpl$$ExternalSyntheticLambda2
            @Override // com.android.server.companion.virtual.GenericWindowPolicyController.PipBlockedCallback
            public final void onEnteringPipBlocked(int i) {
                this.f$0.onEnteringPipBlocked(i);
            }
        }, new com.android.server.companion.virtual.GenericWindowPolicyController.ActivityBlockedCallback() { // from class: com.android.server.companion.virtual.VirtualDeviceImpl$$ExternalSyntheticLambda3
            @Override // com.android.server.companion.virtual.GenericWindowPolicyController.ActivityBlockedCallback
            public final void onActivityBlocked(int i, android.content.pm.ActivityInfo activityInfo) {
                this.f$0.onActivityBlocked(i, activityInfo);
            }
        }, new com.android.server.companion.virtual.GenericWindowPolicyController.SecureWindowCallback() { // from class: com.android.server.companion.virtual.VirtualDeviceImpl$$ExternalSyntheticLambda4
            @Override // com.android.server.companion.virtual.GenericWindowPolicyController.SecureWindowCallback
            public final void onSecureWindowShown(int i, int i2) {
                this.f$0.onSecureWindowShown(i, i2);
            }
        }, new com.android.server.companion.virtual.GenericWindowPolicyController.IntentListenerCallback() { // from class: com.android.server.companion.virtual.VirtualDeviceImpl$$ExternalSyntheticLambda5
            @Override // com.android.server.companion.virtual.GenericWindowPolicyController.IntentListenerCallback
            public final boolean shouldInterceptIntent(android.content.Intent intent) {
                return this.f$0.shouldInterceptIntent(intent);
            }
        }, displayCategories, showTasksInHostDeviceRecents, homeComponent);
        gwpc.registerRunningAppsChangedListener(this);
        return gwpc;
    }

    private android.content.ComponentName getPermissionDialogComponent() {
        android.content.Intent intent = new android.content.Intent("android.content.pm.action.REQUEST_PERMISSIONS");
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        intent.setPackage(packageManager.getPermissionControllerPackageName());
        return intent.resolveActivity(packageManager);
    }

    int createVirtualDisplay(android.hardware.display.VirtualDisplayConfig virtualDisplayConfig, android.hardware.display.IVirtualDisplayCallback callback, java.lang.String packageName) {
        com.android.server.companion.virtual.GenericWindowPolicyController gwpc;
        boolean showPointer;
        synchronized (this.mVirtualDeviceLock) {
            gwpc = createWindowPolicyControllerLocked(virtualDisplayConfig.getDisplayCategories());
        }
        int displayId = this.mDisplayManagerInternal.createVirtualDisplay(virtualDisplayConfig, callback, this, gwpc, packageName);
        gwpc.setDisplayId(displayId, android.companion.virtual.flags.Flags.interactiveScreenMirror() && this.mDisplayManagerInternal.getDisplayIdToMirror(displayId) != -1);
        synchronized (this.mVirtualDeviceLock) {
            if (this.mVirtualDisplays.contains(displayId)) {
                gwpc.unregisterRunningAppsChangedListener(this);
                throw new java.lang.IllegalStateException("Virtual device already has a virtual display with ID " + displayId);
            }
            android.os.PowerManager.WakeLock wakeLock = createAndAcquireWakeLockForDisplay(displayId);
            this.mVirtualDisplays.put(displayId, new com.android.server.companion.virtual.VirtualDeviceImpl.VirtualDisplayWrapper(callback, gwpc, wakeLock));
            showPointer = this.mDefaultShowPointerIcon;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            this.mInputController.setShowPointerIcon(showPointer, displayId);
            this.mInputController.setMousePointerAccelerationEnabled(false, displayId);
            this.mInputController.setDisplayEligibilityForPointerCapture(false, displayId);
            if ((this.mDisplayManagerInternal.getDisplayInfo(displayId).flags & 128) == 128) {
                this.mInputController.setDisplayImePolicy(displayId, 0);
            }
            android.os.Binder.restoreCallingIdentity(token);
            if (android.companion.virtualdevice.flags.Flags.metricsCollection()) {
                com.android.modules.expresslog.Counter.logIncrementWithUid("virtual_devices.value_virtual_display_created_count", this.mAttributionSource.getUid());
            }
            return displayId;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    private android.os.PowerManager.WakeLock createAndAcquireWakeLockForDisplay(int displayId) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.os.PowerManager powerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
            android.os.PowerManager.WakeLock wakeLock = powerManager.newWakeLock(10, "VirtualDeviceImpl:" + displayId, displayId);
            wakeLock.acquire();
            return wakeLock;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onActivityBlocked(int displayId, android.content.pm.ActivityInfo activityInfo) {
        android.content.Intent intent = com.android.internal.app.BlockedAppStreamingActivity.createIntent(activityInfo, this.mAssociationInfo.getDisplayName());
        this.mContext.startActivityAsUser(intent.addFlags(268468224), android.app.ActivityOptions.makeBasic().setLaunchDisplayId(displayId).toBundle(), android.os.UserHandle.SYSTEM);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSecureWindowShown(int displayId, int uid) {
        synchronized (this.mVirtualDeviceLock) {
            if (this.mVirtualDisplays.contains(displayId)) {
                android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class);
                android.view.Display display = displayManager.getDisplay(displayId);
                if ((display.getFlags() & 2) == 0) {
                    showToastWhereUidIsRunning(uid, android.R.string.time_picker_prompt_label, 1, this.mContext.getMainLooper());
                    if (android.companion.virtualdevice.flags.Flags.metricsCollection()) {
                        com.android.modules.expresslog.Counter.logIncrementWithUid("virtual_devices.value_secure_window_blocked_count", this.mAttributionSource.getUid());
                    }
                }
            }
        }
    }

    private android.util.ArraySet<android.os.UserHandle> getAllowedUserHandles() {
        android.util.ArraySet<android.os.UserHandle> result = new android.util.ArraySet<>();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) this.mContext.getSystemService(android.app.admin.DevicePolicyManager.class);
            android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
            for (android.os.UserHandle profile : userManager.getAllProfiles()) {
                int nearbyAppStreamingPolicy = dpm.getNearbyAppStreamingPolicy(profile.getIdentifier());
                if (nearbyAppStreamingPolicy == 2 || nearbyAppStreamingPolicy == 0) {
                    result.add(profile);
                } else if (nearbyAppStreamingPolicy == 3 && this.mParams.getUsersWithMatchingAccounts().contains(profile)) {
                    result.add(profile);
                }
            }
            return result;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    void onVirtualDisplayRemoved(int displayId) {
        com.android.server.companion.virtual.VirtualDeviceImpl.VirtualDisplayWrapper virtualDisplayWrapper;
        synchronized (this.mVirtualDeviceLock) {
            virtualDisplayWrapper = (com.android.server.companion.virtual.VirtualDeviceImpl.VirtualDisplayWrapper) this.mVirtualDisplays.removeReturnOld(displayId);
        }
        if (virtualDisplayWrapper == null) {
            android.util.Slog.w(TAG, "Virtual device " + this.mDeviceId + " doesn't have a virtual display with ID " + displayId);
            return;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            releaseOwnedVirtualDisplayResources(virtualDisplayWrapper);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void checkVirtualInputDeviceDisplayIdAssociation(int displayId) {
        if (this.mContext.checkCallingPermission("android.permission.INJECT_EVENTS") == 0) {
            return;
        }
        synchronized (this.mVirtualDeviceLock) {
            if (!this.mVirtualDisplays.contains(displayId)) {
                throw new java.lang.SecurityException("Cannot create a virtual input device for display " + displayId + " which not associated with this virtual device");
            }
        }
    }

    private void releaseOwnedVirtualDisplayResources(com.android.server.companion.virtual.VirtualDeviceImpl.VirtualDisplayWrapper virtualDisplayWrapper) {
        virtualDisplayWrapper.getWakeLock().release();
        virtualDisplayWrapper.getWindowPolicyController().unregisterRunningAppsChangedListener(this);
    }

    int getOwnerUid() {
        return this.mOwnerUid;
    }

    public int[] getDisplayIds() {
        int[] displayIds;
        synchronized (this.mVirtualDeviceLock) {
            int size = this.mVirtualDisplays.size();
            displayIds = new int[size];
            for (int i = 0; i < size; i++) {
                displayIds[i] = this.mVirtualDisplays.keyAt(i);
            }
        }
        return displayIds;
    }

    com.android.server.companion.virtual.GenericWindowPolicyController getDisplayWindowPolicyControllerForTest(int displayId) {
        com.android.server.companion.virtual.VirtualDeviceImpl.VirtualDisplayWrapper virtualDisplayWrapper;
        synchronized (this.mVirtualDeviceLock) {
            virtualDisplayWrapper = this.mVirtualDisplays.get(displayId);
        }
        if (virtualDisplayWrapper != null) {
            return virtualDisplayWrapper.getWindowPolicyController();
        }
        return null;
    }

    boolean isAppRunningOnVirtualDevice(int uid) {
        synchronized (this.mVirtualDeviceLock) {
            for (int i = 0; i < this.mVirtualDisplays.size(); i++) {
                if (this.mVirtualDisplays.valueAt(i).getWindowPolicyController().containsUid(uid)) {
                    return true;
                }
            }
            return false;
        }
    }

    void showToastWhereUidIsRunning(int uid, int resId, int duration, android.os.Looper looper) {
        showToastWhereUidIsRunning(uid, this.mContext.getString(resId), duration, looper);
    }

    void showToastWhereUidIsRunning(int uid, java.lang.String text, int duration, android.os.Looper looper) {
        android.util.IntArray displayIdsForUid = getDisplayIdsWhereUidIsRunning(uid);
        if (displayIdsForUid.size() == 0) {
            return;
        }
        android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class);
        for (int i = 0; i < displayIdsForUid.size(); i++) {
            android.view.Display display = displayManager.getDisplay(displayIdsForUid.get(i));
            if (display != null && display.isValid()) {
                android.widget.Toast.makeText(this.mContext.createDisplayContext(display), looper, text, duration).show();
            }
        }
    }

    private android.util.IntArray getDisplayIdsWhereUidIsRunning(int uid) {
        android.util.IntArray displayIdsForUid = new android.util.IntArray();
        synchronized (this.mVirtualDeviceLock) {
            for (int i = 0; i < this.mVirtualDisplays.size(); i++) {
                if (this.mVirtualDisplays.valueAt(i).getWindowPolicyController().containsUid(uid)) {
                    displayIdsForUid.add(this.mVirtualDisplays.keyAt(i));
                }
            }
        }
        return displayIdsForUid;
    }

    boolean isDisplayOwnedByVirtualDevice(int displayId) {
        boolean zContains;
        synchronized (this.mVirtualDeviceLock) {
            zContains = this.mVirtualDisplays.contains(displayId);
        }
        return zContains;
    }

    boolean isInputDeviceOwnedByVirtualDevice(final int inputDeviceId) {
        return this.mInputController.getInputDeviceDescriptors().values().stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.companion.virtual.VirtualDeviceImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.companion.virtual.VirtualDeviceImpl.lambda$isInputDeviceOwnedByVirtualDevice$1(inputDeviceId, (com.android.server.companion.virtual.InputController.InputDeviceDescriptor) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$isInputDeviceOwnedByVirtualDevice$1(int inputDeviceId, com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor) {
        return inputDeviceDescriptor.getInputDeviceId() == inputDeviceId;
    }

    void onEnteringPipBlocked(int uid) {
    }

    void playSoundEffect(int effectType) {
        try {
            this.mSoundEffectListener.onPlaySoundEffect(effectType);
        } catch (android.os.RemoteException exception) {
            android.util.Slog.w(TAG, "Unable to invoke sound effect listener", exception);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldInterceptIntent(android.content.Intent intent) {
        boolean hasInterceptedIntent;
        synchronized (this.mVirtualDeviceLock) {
            hasInterceptedIntent = false;
            for (java.util.Map.Entry<android.os.IBinder, android.content.IntentFilter> interceptor : this.mIntentInterceptors.entrySet()) {
                android.content.IntentFilter intentFilter = interceptor.getValue();
                boolean explicitActionMatch = (android.companion.virtualdevice.flags.Flags.intentInterceptionActionMatchingFix() && intent.getAction() == null && intentFilter.countActions() != 0) ? false : true;
                if (explicitActionMatch && intentFilter.match(intent.getAction(), intent.getType(), intent.getScheme(), intent.getData(), intent.getCategories(), TAG) >= 0) {
                    try {
                        android.companion.virtual.IVirtualDeviceIntentInterceptor.Stub.asInterface(interceptor.getKey()).onIntentIntercepted(new android.content.Intent(intent.getAction(), intent.getData()));
                        hasInterceptedIntent = true;
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(TAG, "Unable to call mVirtualDeviceIntentInterceptor", e);
                    }
                }
            }
        }
        return hasInterceptedIntent;
    }

    static class PendingTrampoline {
        final int mDisplayId;
        final android.app.PendingIntent mPendingIntent;
        final android.os.ResultReceiver mResultReceiver;

        private PendingTrampoline(android.app.PendingIntent pendingIntent, android.os.ResultReceiver resultReceiver, int displayId) {
            this.mPendingIntent = pendingIntent;
            this.mResultReceiver = resultReceiver;
            this.mDisplayId = displayId;
        }

        public java.lang.String toString() {
            return "PendingTrampoline{pendingIntent=" + this.mPendingIntent + ", resultReceiver=" + this.mResultReceiver + ", displayId=" + this.mDisplayId + "}";
        }
    }

    private static final class VirtualDisplayWrapper {
        private final android.hardware.display.IVirtualDisplayCallback mToken;
        private final android.os.PowerManager.WakeLock mWakeLock;
        private final com.android.server.companion.virtual.GenericWindowPolicyController mWindowPolicyController;

        VirtualDisplayWrapper(android.hardware.display.IVirtualDisplayCallback token, com.android.server.companion.virtual.GenericWindowPolicyController windowPolicyController, android.os.PowerManager.WakeLock wakeLock) {
            this.mToken = (android.hardware.display.IVirtualDisplayCallback) java.util.Objects.requireNonNull(token);
            this.mWindowPolicyController = (com.android.server.companion.virtual.GenericWindowPolicyController) java.util.Objects.requireNonNull(windowPolicyController);
            this.mWakeLock = (android.os.PowerManager.WakeLock) java.util.Objects.requireNonNull(wakeLock);
        }

        com.android.server.companion.virtual.GenericWindowPolicyController getWindowPolicyController() {
            return this.mWindowPolicyController;
        }

        android.os.PowerManager.WakeLock getWakeLock() {
            return this.mWakeLock;
        }

        android.hardware.display.IVirtualDisplayCallback getToken() {
            return this.mToken;
        }
    }

    private static boolean isVirtualCameraEnabled() {
        return android.companion.virtual.flags.Flags.virtualCamera() && android.companion.virtualdevice.flags.Flags.virtualCameraServiceDiscovery() && nativeVirtualCameraServiceBuildFlagEnabled();
    }
}
