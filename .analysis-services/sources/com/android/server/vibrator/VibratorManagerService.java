package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public class VibratorManagerService extends android.os.IVibratorManagerService.Stub {
    private static final int ATTRIBUTES_ALL_BYPASS_FLAGS = 19;
    private static final long BATTERY_STATS_REPEATING_VIBRATION_DURATION = 5000;
    private static boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final android.os.VibrationAttributes DEFAULT_ATTRIBUTES = new android.os.VibrationAttributes.Builder().build();
    private static final java.lang.String EXTERNAL_VIBRATOR_SERVICE = "external_vibrator_service";
    private static final java.lang.String TAG = "VibratorManagerService";
    private static final long VIBRATION_CANCEL_WAIT_MILLIS = 5000;
    private static final java.lang.String VIBRATOR_CONTROL_SERVICE = "android.frameworks.vibrator.IVibratorControlService/default";
    private final android.app.AppOpsManager mAppOps;
    private final com.android.internal.app.IBatteryStats mBatteryStatsService;
    private final long mCapabilities;
    private android.os.VibratorInfo mCombinedVibratorInfo;
    private final android.content.Context mContext;
    private com.android.server.vibrator.VibratorManagerService.ExternalVibrationHolder mCurrentExternalVibration;
    private com.android.server.vibrator.VibrationStepConductor mCurrentVibration;
    private final com.android.server.vibrator.DeviceAdapter mDeviceAdapter;
    private final com.android.server.vibrator.VibratorFrameworkStatsLogger mFrameworkStatsLogger;
    private final android.os.Handler mHandler;
    private com.android.server.vibrator.HapticFeedbackVibrationProvider mHapticFeedbackVibrationProvider;
    private final com.android.server.vibrator.VibratorManagerService.Injector mInjector;
    private final com.android.server.vibrator.InputDeviceDelegate mInputDeviceDelegate;
    private final com.android.server.vibrator.VibratorManagerService.NativeWrapper mNativeWrapper;
    private com.android.server.vibrator.VibrationStepConductor mNextVibration;
    private boolean mServiceReady;
    private final com.android.server.vibrator.VibrationScaler mVibrationScaler;
    private final com.android.server.vibrator.VibrationSettings mVibrationSettings;
    private final com.android.server.vibrator.VibrationThread mVibrationThread;
    private final com.android.server.vibrator.VibratorControlService mVibratorControlService;
    private final int[] mVibratorIds;
    private final com.android.server.vibrator.VibratorManagerService.VibratorManagerRecords mVibratorManagerRecords;
    private final android.util.SparseArray<com.android.server.vibrator.VibratorController> mVibrators;
    private final android.os.PowerManager.WakeLock mWakeLock;
    private final java.lang.Object mLock = new java.lang.Object();
    private final com.android.server.vibrator.VibratorManagerService.VibrationThreadCallbacks mVibrationThreadCallbacks = new com.android.server.vibrator.VibratorManagerService.VibrationThreadCallbacks();
    private final android.util.SparseArray<com.android.server.vibrator.VibratorManagerService.AlwaysOnVibration> mAlwaysOnEffects = new android.util.SparseArray<>();
    private android.content.BroadcastReceiver mIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.vibrator.VibratorManagerService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                synchronized (com.android.server.vibrator.VibratorManagerService.this.mLock) {
                    if (com.android.server.vibrator.VibratorManagerService.this.shouldCancelOnScreenOffLocked(com.android.server.vibrator.VibratorManagerService.this.mNextVibration)) {
                        com.android.server.vibrator.VibratorManagerService.this.clearNextVibrationLocked(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.CANCELLED_BY_SCREEN_OFF));
                    }
                    if (com.android.server.vibrator.VibratorManagerService.this.shouldCancelOnScreenOffLocked(com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration)) {
                        com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration.notifyCancelled(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.CANCELLED_BY_SCREEN_OFF), false);
                    }
                }
            }
        }
    };
    private com.android.server.vibrator.IVibratorManagerServiceWrapper mVibratorManagerServiceWrapper = new com.android.server.vibrator.VibratorManagerService.VibratorManagerServiceWrapper();
    private com.android.server.zenmode.IZenModeManagerExt mZenModeManagerExt = (com.android.server.zenmode.IZenModeManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.zenmode.IZenModeManagerExt.class).create();

    interface OnSyncedVibrationCompleteListener {
        void onComplete(long j);
    }

    static native void nativeCancelSynced(long j);

    static native long nativeGetCapabilities(long j);

    static native long nativeGetFinalizer();

    static native int[] nativeGetVibratorIds(long j);

    static native long nativeInit(com.android.server.vibrator.VibratorManagerService.OnSyncedVibrationCompleteListener onSyncedVibrationCompleteListener);

    static native boolean nativePrepareSynced(long j, int[] iArr);

    static native boolean nativeTriggerSynced(long j, long j2);

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.vibrator.VibratorManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mService = new com.android.server.vibrator.VibratorManagerService(getContext(), new com.android.server.vibrator.VibratorManagerService.Injector());
            this.mService.getWrapper().getExtImpl().init(getContext());
            publishBinderService("vibrator_manager", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 500) {
                this.mService.systemReady();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    VibratorManagerService(android.content.Context context, com.android.server.vibrator.VibratorManagerService.Injector injector) {
        this.mContext = context;
        this.mInjector = injector;
        this.mHandler = injector.createHandler(android.os.Looper.myLooper());
        this.mFrameworkStatsLogger = injector.getFrameworkStatsLogger(this.mHandler);
        this.mVibrationSettings = new com.android.server.vibrator.VibrationSettings(this.mContext, this.mHandler);
        this.mVibrationScaler = new com.android.server.vibrator.VibrationScaler(this.mContext, this.mVibrationSettings);
        this.mVibratorControlService = new com.android.server.vibrator.VibratorControlService(this.mContext, injector.createVibratorControllerHolder(), this.mVibrationScaler, this.mVibrationSettings, this.mFrameworkStatsLogger, this.mLock);
        this.mInputDeviceDelegate = new com.android.server.vibrator.InputDeviceDelegate(this.mContext, this.mHandler);
        com.android.server.vibrator.VibratorManagerService.VibrationCompleteListener vibrationCompleteListener = new com.android.server.vibrator.VibratorManagerService.VibrationCompleteListener(this);
        this.mNativeWrapper = injector.getNativeWrapper();
        this.mNativeWrapper.init(vibrationCompleteListener);
        this.mVibratorManagerRecords = new com.android.server.vibrator.VibratorManagerService.VibratorManagerRecords(this.mContext.getResources().getInteger(android.R.integer.config_ntpTimeout), this.mContext.getResources().getInteger(android.R.integer.config_ntpPollingInterval), this.mContext.getResources().getInteger(android.R.integer.config_notificationsBatteryNearlyFullLevel));
        this.mBatteryStatsService = injector.getBatteryStatsService();
        this.mAppOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        this.mWakeLock = ((android.os.PowerManager) context.getSystemService(android.os.PowerManager.class)).newWakeLock(1, "*vibrator*");
        this.mWakeLock.setReferenceCounted(true);
        this.mVibrationThread = new com.android.server.vibrator.VibrationThread(this.mWakeLock, this.mVibrationThreadCallbacks);
        this.mVibrationThread.start();
        this.mCapabilities = this.mNativeWrapper.getCapabilities();
        int[] vibratorIds = this.mNativeWrapper.getVibratorIds();
        if (vibratorIds == null) {
            this.mVibratorIds = new int[0];
            this.mVibrators = new android.util.SparseArray<>(0);
        } else {
            this.mVibratorIds = vibratorIds;
            this.mVibrators = new android.util.SparseArray<>(this.mVibratorIds.length);
            for (int i : vibratorIds) {
                this.mVibrators.put(i, injector.createVibratorController(i, vibrationCompleteListener));
            }
        }
        this.mDeviceAdapter = new com.android.server.vibrator.DeviceAdapter(this.mVibrationSettings, this.mVibrators);
        this.mNativeWrapper.cancelSynced();
        for (int i2 = 0; i2 < this.mVibrators.size(); i2++) {
            this.mVibrators.valueAt(i2).reset();
        }
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        context.registerReceiver(this.mIntentReceiver, intentFilter, 4);
        injector.addService(EXTERNAL_VIBRATOR_SERVICE, new com.android.server.vibrator.VibratorManagerService.ExternalVibratorService());
        if (injector.isServiceDeclared(VIBRATOR_CONTROL_SERVICE)) {
            injector.addService(VIBRATOR_CONTROL_SERVICE, this.mVibratorControlService);
        }
    }

    void systemReady() {
        android.util.Slog.v(TAG, "Initializing VibratorManager service...");
        android.os.Trace.traceBegin(8388608L, "systemReady");
        for (int i = 0; i < this.mVibrators.size(); i++) {
            try {
                this.mVibrators.valueAt(i).reloadVibratorInfoIfNeeded();
            } catch (java.lang.Throwable th) {
                synchronized (this.mLock) {
                    this.mServiceReady = true;
                    android.util.Slog.v(TAG, "VibratorManager service initialized");
                    android.os.Trace.traceEnd(8388608L);
                    throw th;
                }
            }
        }
        this.mVibrationSettings.onSystemReady();
        this.mInputDeviceDelegate.onSystemReady();
        getWrapper().getExtImpl().onSystemReady();
        this.mVibrationSettings.addListener(new com.android.server.vibrator.VibrationSettings.OnVibratorSettingsChanged() { // from class: com.android.server.vibrator.VibratorManagerService$$ExternalSyntheticLambda2
            @Override // com.android.server.vibrator.VibrationSettings.OnVibratorSettingsChanged
            public final void onChange() {
                this.f$0.updateServiceState();
            }
        });
        updateServiceState();
        getWrapper().getExtImpl().cancelScreenOffReceiver(this.mContext, this.mIntentReceiver);
        synchronized (this.mLock) {
            this.mServiceReady = true;
        }
        android.util.Slog.v(TAG, "VibratorManager service initialized");
        android.os.Trace.traceEnd(8388608L);
    }

    public int[] getVibratorIds() {
        return java.util.Arrays.copyOf(this.mVibratorIds, this.mVibratorIds.length);
    }

    public android.os.VibratorInfo getVibratorInfo(int vibratorId) {
        com.android.server.vibrator.VibratorController controller = this.mVibrators.get(vibratorId);
        if (controller == null) {
            return null;
        }
        android.os.VibratorInfo info = controller.getVibratorInfo();
        synchronized (this.mLock) {
            if (this.mServiceReady) {
                return info;
            }
            if (controller.isVibratorInfoLoadSuccessful()) {
                return info;
            }
            return null;
        }
    }

    public boolean isVibrating(int vibratorId) {
        isVibrating_enforcePermission();
        com.android.server.vibrator.VibratorController controller = this.mVibrators.get(vibratorId);
        return controller != null && controller.isVibrating();
    }

    public boolean registerVibratorStateListener(int vibratorId, android.os.IVibratorStateListener listener) {
        registerVibratorStateListener_enforcePermission();
        com.android.server.vibrator.VibratorController controller = this.mVibrators.get(vibratorId);
        if (controller == null) {
            return false;
        }
        return controller.registerVibratorStateListener(listener);
    }

    public boolean unregisterVibratorStateListener(int vibratorId, android.os.IVibratorStateListener listener) {
        unregisterVibratorStateListener_enforcePermission();
        com.android.server.vibrator.VibratorController controller = this.mVibrators.get(vibratorId);
        if (controller == null) {
            return false;
        }
        return controller.unregisterVibratorStateListener(listener);
    }

    public boolean setAlwaysOnEffect(int uid, java.lang.String opPkg, final int alwaysOnId, android.os.CombinedVibration effect, android.os.VibrationAttributes attrs) throws java.lang.Throwable {
        android.os.Trace.traceBegin(8388608L, "setAlwaysOnEffect");
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.VIBRATE_ALWAYS_ON", "setAlwaysOnEffect");
            if (effect == null) {
                synchronized (this.mLock) {
                    this.mAlwaysOnEffects.delete(alwaysOnId);
                    onAllVibratorsLocked(new java.util.function.Consumer() { // from class: com.android.server.vibrator.VibratorManagerService$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            com.android.server.vibrator.VibratorManagerService.lambda$setAlwaysOnEffect$0(alwaysOnId, (com.android.server.vibrator.VibratorController) obj);
                        }
                    });
                }
                android.os.Trace.traceEnd(8388608L);
                return true;
            }
            if (!isEffectValid(effect)) {
                android.os.Trace.traceEnd(8388608L);
                return false;
            }
            try {
                android.os.VibrationAttributes attrs2 = fixupVibrationAttributes(attrs, effect);
                try {
                    synchronized (this.mLock) {
                        android.util.SparseArray<android.os.vibrator.PrebakedSegment> effects = fixupAlwaysOnEffectsLocked(effect);
                        if (effects == null) {
                            android.os.Trace.traceEnd(8388608L);
                            return false;
                        }
                        com.android.server.vibrator.VibratorManagerService.AlwaysOnVibration alwaysOnVibration = new com.android.server.vibrator.VibratorManagerService.AlwaysOnVibration(alwaysOnId, new com.android.server.vibrator.Vibration.CallerInfo(attrs2, uid, 0, opPkg, null), effects);
                        this.mAlwaysOnEffects.put(alwaysOnId, alwaysOnVibration);
                        updateAlwaysOnLocked(alwaysOnVibration);
                        android.os.Trace.traceEnd(8388608L);
                        return true;
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Trace.traceEnd(8388608L);
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        } catch (java.lang.Throwable th3) {
            th = th3;
            android.os.Trace.traceEnd(8388608L);
            throw th;
        }
    }

    static /* synthetic */ void lambda$setAlwaysOnEffect$0(int alwaysOnId, com.android.server.vibrator.VibratorController v) {
        if (v.hasCapability(64L)) {
            v.updateAlwaysOn(alwaysOnId, null);
        }
    }

    public void vibrate(int uid, int deviceId, java.lang.String opPkg, android.os.CombinedVibration effect, android.os.VibrationAttributes attrs, java.lang.String reason, android.os.IBinder token) throws java.lang.Throwable {
        vibrateWithPermissionCheck(uid, deviceId, opPkg, effect, attrs, reason, token);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void performHapticFeedback(int uid, int deviceId, java.lang.String opPkg, int constant, boolean always, java.lang.String reason, boolean fromIme) {
        if (!getWrapper().getExtImpl().shouldPerformHapticFeedback()) {
            return;
        }
        performHapticFeedbackInternal(uid, deviceId, opPkg, constant, always, reason, this, fromIme);
    }

    com.android.server.vibrator.HalVibration performHapticFeedbackInternal(int uid, int deviceId, java.lang.String opPkg, int constant, boolean always, java.lang.String reason, android.os.IBinder token, boolean fromIme) {
        com.android.server.vibrator.HapticFeedbackVibrationProvider hapticVibrationProvider = getHapticVibrationProvider();
        if (hapticVibrationProvider == null) {
            android.util.Slog.e(TAG, "performHapticFeedback; haptic vibration provider not ready.");
            return null;
        }
        if (hapticVibrationProvider.isRestrictedHapticFeedback(constant) && !hasPermission("android.permission.VIBRATE_SYSTEM_CONSTANTS")) {
            android.util.Slog.w(TAG, "performHapticFeedback; no permission for system constant " + constant);
            return null;
        }
        android.os.VibrationEffect effect = hapticVibrationProvider.getVibrationForHapticFeedback(constant);
        if (effect == null) {
            android.util.Slog.w(TAG, "performHapticFeedback; vibration absent for constant " + constant);
            return null;
        }
        android.os.CombinedVibration vib = android.os.CombinedVibration.createParallel(effect);
        android.os.VibrationAttributes attrs = hapticVibrationProvider.getVibrationAttributesForHapticFeedback(constant, always, fromIme);
        java.lang.String reason2 = "performHapticFeedback(constant=" + constant + "): " + reason;
        com.android.server.vibrator.VibratorFrameworkStatsLogger.logPerformHapticsFeedbackIfKeyboard(uid, constant);
        return vibrateWithoutPermissionCheck(uid, deviceId, opPkg, vib, attrs, reason2, token);
    }

    com.android.server.vibrator.HalVibration vibrateWithPermissionCheck(int uid, int deviceId, java.lang.String opPkg, android.os.CombinedVibration effect, android.os.VibrationAttributes attrs, java.lang.String reason, android.os.IBinder token) throws java.lang.Throwable {
        android.os.Trace.traceBegin(8388608L, "vibrate, reason = " + reason);
        try {
            android.os.VibrationAttributes attrs2 = fixupVibrationAttributes(attrs, effect);
            try {
                this.mContext.enforceCallingOrSelfPermission("android.permission.VIBRATE", "vibrate");
                com.android.server.vibrator.HalVibration halVibrationVibrateInternal = vibrateInternal(uid, deviceId, opPkg, effect, attrs2, reason, token);
                android.os.Trace.traceEnd(8388608L);
                return halVibrationVibrateInternal;
            } catch (java.lang.Throwable th) {
                th = th;
                android.os.Trace.traceEnd(8388608L);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    com.android.server.vibrator.HalVibration vibrateWithoutPermissionCheck(int uid, int deviceId, java.lang.String opPkg, android.os.CombinedVibration effect, android.os.VibrationAttributes attrs, java.lang.String reason, android.os.IBinder token) {
        android.os.Trace.traceBegin(8388608L, "vibrate no perm check, reason = " + reason);
        try {
            return vibrateInternal(uid, deviceId, opPkg, effect, attrs, reason, token);
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    private com.android.server.vibrator.HalVibration vibrateInternal(int uid, int deviceId, java.lang.String opPkg, android.os.CombinedVibration effect, android.os.VibrationAttributes attrs, java.lang.String reason, android.os.IBinder token) {
        java.lang.String reason2;
        com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo;
        java.lang.String reason3;
        if (token == null) {
            android.util.Slog.e(TAG, "token must not be null");
            return null;
        }
        enforceUpdateAppOpsStatsPermission(uid);
        if (!isEffectValid(effect)) {
            return null;
        }
        getWrapper().getExtImpl().vibrate(uid, opPkg, effect, attrs, reason, token);
        if (getWrapper().getExtImpl().ignoreVibrateForRichTapVibrationEffect(effect) || getWrapper().getExtImpl().disposeRichtapEffectParams(effect) || getWrapper().getExtImpl().ignoreVibrationForCamera(uid, opPkg, effect)) {
            return null;
        }
        android.os.CombinedVibration effect2 = getWrapper().getExtImpl().transferEffectToWaveform(effect);
        android.os.CombinedVibration combinedVibration = getWrapper().getExtImpl().convertVibrationEffect(effect2, attrs, uid, opPkg, reason);
        if (combinedVibration == null) {
            reason2 = reason;
        } else {
            if (!android.text.TextUtils.isEmpty(reason)) {
                reason3 = reason;
            } else {
                reason3 = getWrapper().getExtImpl().getConvertVibrationReason(effect2);
            }
            effect2 = combinedVibration;
            reason2 = reason3;
        }
        android.os.CombinedVibration effect3 = getWrapper().getExtImpl().fixVibrationEffect(getWrapper().getExtImpl().fixVibrationEffectDuration(getWrapper().getExtImpl().fixImeVibrationStrength(effect2, opPkg)));
        android.os.VibrationAttributes attrs2 = fixupVibrationAttributes(attrs, effect3);
        getWrapper().getExtImpl().fixVibrationEffectStrength(effect3, attrs2);
        if (!isEffectValid(effect3)) {
            return null;
        }
        com.android.server.vibrator.HalVibration vib = new com.android.server.vibrator.HalVibration(token, effect3, new com.android.server.vibrator.Vibration.CallerInfo(attrs2, uid, deviceId, opPkg, reason2));
        fillVibrationFallbacks(vib, effect3);
        vib.getWrapper().setVibrationPid(android.os.Binder.getCallingPid());
        if (attrs2.isFlagSet(4)) {
            this.mVibrationSettings.update();
        }
        synchronized (this.mLock) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Starting vibrate for vibration " + vib.id + ", " + (vib.getEffectToPlay() == null ? "invalid" : vib.getEffectToPlay().toString()) + ", " + vib.callerInfo);
            }
            getWrapper().getExtImpl().noteVibration(vib);
            com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo2 = shouldIgnoreVibrationLocked(vib.callerInfo);
            if (vibrationEndInfo2 != null) {
                vibrationEndInfo = vibrationEndInfo2;
            } else {
                vibrationEndInfo = shouldIgnoreVibrationForOngoingLocked(vib);
            }
            if (vibrationEndInfo == null) {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    if (this.mCurrentVibration == null && !getWrapper().getExtImpl().checkIfRichtapPatternHeEffect(vib.getEffectToPlay())) {
                        getWrapper().getExtImpl().stopRichtapVibration();
                    }
                    if (this.mCurrentExternalVibration != null) {
                        this.mCurrentExternalVibration.mute();
                        vib.stats.reportInterruptedAnotherVibration(this.mCurrentExternalVibration.callerInfo);
                        endExternalVibrateLocked(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.CANCELLED_SUPERSEDED, vib.callerInfo), false);
                    } else if (this.mCurrentVibration != null) {
                        if (this.mCurrentVibration.getVibration().canPipelineWith(vib)) {
                            if (DEBUG) {
                                android.util.Slog.d(TAG, "Pipelining vibration " + vib.id);
                            }
                        } else {
                            vib.stats.reportInterruptedAnotherVibration(this.mCurrentVibration.getVibration().callerInfo);
                            this.mCurrentVibration.notifyCancelled(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.CANCELLED_SUPERSEDED, vib.callerInfo), false);
                        }
                    }
                    vibrationEndInfo = startVibrationLocked(vib);
                    android.os.Binder.restoreCallingIdentity(ident);
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(ident);
                    throw th;
                }
            }
            if (vibrationEndInfo != null) {
                endVibrationLocked(vib, vibrationEndInfo, true);
            }
        }
        return vib;
    }

    public void cancelVibrate(int usageFilter, android.os.IBinder token) {
        android.os.Trace.traceBegin(8388608L, "cancelVibrate");
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.VIBRATE", "cancelVibrate");
            synchronized (this.mLock) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Canceling vibration");
                }
                com.android.server.vibrator.Vibration.EndInfo cancelledByUserInfo = new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.CANCELLED_BY_USER);
                getWrapper().getExtImpl().cancelVibrate(android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), usageFilter, token, this.mCurrentVibration);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    if (this.mNextVibration != null && shouldCancelVibration(this.mNextVibration.getVibration(), usageFilter, token)) {
                        clearNextVibrationLocked(cancelledByUserInfo);
                    }
                    if (this.mCurrentVibration != null && shouldCancelVibration(this.mCurrentVibration.getVibration(), usageFilter, token)) {
                        this.mCurrentVibration.notifyCancelled(cancelledByUserInfo, false);
                    }
                    if (this.mCurrentExternalVibration != null && shouldCancelVibration(this.mCurrentExternalVibration.externalVibration.getVibrationAttributes(), usageFilter)) {
                        this.mCurrentExternalVibration.mute();
                        endExternalVibrateLocked(cancelledByUserInfo, false);
                    }
                    getWrapper().getExtImpl().stopRichtapVibration();
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            long ident = android.os.Binder.clearCallingIdentity();
            boolean isDumpProto = false;
            for (java.lang.String arg : args) {
                if (arg.equals("--proto")) {
                    isDumpProto = true;
                }
            }
            try {
                if (isDumpProto) {
                    dumpProto(fd);
                } else {
                    dumpText(pw);
                }
                android.os.Binder.restoreCallingIdentity(ident);
                getWrapper().getExtImpl().dynamicallyConfigLogTag(pw, args);
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(ident);
                throw th;
            }
        }
    }

    private void dumpText(java.io.PrintWriter w) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Dumping vibrator manager service to text...");
        }
        android.util.IndentingPrintWriter pw = new android.util.IndentingPrintWriter(w, "  ");
        synchronized (this.mLock) {
            pw.println("VibratorManagerService:");
            pw.increaseIndent();
            this.mVibrationSettings.dump(pw);
            pw.println();
            this.mVibrationScaler.dump(pw);
            pw.println();
            pw.println("Vibrators:");
            pw.increaseIndent();
            for (int i = 0; i < this.mVibrators.size(); i++) {
                this.mVibrators.valueAt(i).dump(pw);
            }
            pw.decreaseIndent();
            pw.println();
            pw.println("CurrentVibration:");
            pw.increaseIndent();
            if (this.mCurrentVibration != null) {
                this.mCurrentVibration.getVibration().getDebugInfo().dump(pw);
            } else {
                pw.println("null");
            }
            pw.decreaseIndent();
            pw.println();
            pw.println("NextVibration:");
            pw.increaseIndent();
            if (this.mNextVibration != null) {
                this.mNextVibration.getVibration().getDebugInfo().dump(pw);
            } else {
                pw.println("null");
            }
            pw.decreaseIndent();
            pw.println();
            pw.println("CurrentExternalVibration:");
            pw.increaseIndent();
            if (this.mCurrentExternalVibration != null) {
                this.mCurrentExternalVibration.getDebugInfo().dump(pw);
            } else {
                pw.println("null");
            }
            pw.decreaseIndent();
        }
        pw.println();
        pw.println();
        this.mVibratorManagerRecords.dump(pw);
        pw.println();
        pw.println();
        this.mVibratorControlService.dump(pw);
    }

    private void dumpProto(java.io.FileDescriptor fd) {
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Dumping vibrator manager service to proto...");
        }
        synchronized (this.mLock) {
            this.mVibrationSettings.dump(proto);
            this.mVibrationScaler.dump(proto);
            if (this.mCurrentVibration != null) {
                this.mCurrentVibration.getVibration().getDebugInfo().dump(proto, 1146756268034L);
            }
            if (this.mCurrentExternalVibration != null) {
                this.mCurrentExternalVibration.getDebugInfo().dump(proto, 1146756268036L);
            }
            boolean isVibrating = false;
            boolean isUnderExternalControl = false;
            for (int i = 0; i < this.mVibrators.size(); i++) {
                proto.write(2220498092033L, this.mVibrators.keyAt(i));
                isVibrating |= this.mVibrators.valueAt(i).isVibrating();
                isUnderExternalControl |= this.mVibrators.valueAt(i).isUnderExternalControl();
            }
            proto.write(1133871366147L, isVibrating);
            proto.write(1133871366149L, isUnderExternalControl);
        }
        this.mVibratorManagerRecords.dump(proto);
        this.mVibratorControlService.dump(proto);
        proto.flush();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback cb, android.os.ResultReceiver resultReceiver) {
        new com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand(cb.getShellCallbackBinder()).exec(this, in, out, err, args, cb, resultReceiver);
    }

    void updateServiceState() {
        synchronized (this.mLock) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Updating device state...");
            }
            boolean inputDevicesChanged = this.mInputDeviceDelegate.updateInputDeviceVibrators(this.mVibrationSettings.shouldVibrateInputDevices());
            getWrapper().getExtImpl().updateVibrator();
            for (int i = 0; i < this.mAlwaysOnEffects.size(); i++) {
                updateAlwaysOnLocked(this.mAlwaysOnEffects.valueAt(i));
            }
            if (this.mCurrentVibration == null) {
                return;
            }
            com.android.server.vibrator.HalVibration vib = this.mCurrentVibration.getVibration();
            com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo = shouldIgnoreVibrationLocked(vib.callerInfo);
            if (inputDevicesChanged || vibrationEndInfo != null) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Canceling vibration because settings changed: " + (inputDevicesChanged ? "input devices changed" : vibrationEndInfo.status));
                }
                this.mCurrentVibration.notifyCancelled(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.CANCELLED_BY_SETTINGS_UPDATE), false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setExternalControl(boolean externalControl, com.android.server.vibrator.VibrationStats vibrationStats) {
        for (int i = 0; i < this.mVibrators.size(); i++) {
            this.mVibrators.valueAt(i).setExternalControl(externalControl);
            vibrationStats.reportSetExternalControl();
        }
    }

    private void updateAlwaysOnLocked(com.android.server.vibrator.VibratorManagerService.AlwaysOnVibration vib) {
        android.os.vibrator.PrebakedSegment effect;
        for (int i = 0; i < vib.effects.size(); i++) {
            com.android.server.vibrator.VibratorController vibrator = this.mVibrators.get(vib.effects.keyAt(i));
            android.os.vibrator.PrebakedSegment effect2 = vib.effects.valueAt(i);
            if (vibrator != null) {
                com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo = shouldIgnoreVibrationLocked(vib.callerInfo);
                if (vibrationEndInfo == null) {
                    effect = this.mVibrationScaler.scale(effect2, vib.callerInfo.attrs.getUsage());
                } else {
                    effect = null;
                }
                vibrator.updateAlwaysOn(vib.alwaysOnId, effect);
            }
        }
    }

    private com.android.server.vibrator.Vibration.EndInfo startVibrationLocked(com.android.server.vibrator.HalVibration vib) {
        android.os.Trace.traceBegin(8388608L, "startVibrationLocked");
        try {
            if (this.mInputDeviceDelegate.isAvailable()) {
                return startVibrationOnInputDevicesLocked(vib);
            }
            com.android.server.vibrator.VibrationStepConductor conductor = createVibrationStepConductor(vib);
            if (this.mCurrentVibration == null) {
                return startVibrationOnThreadLocked(conductor);
            }
            clearNextVibrationLocked(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_SUPERSEDED, vib.callerInfo));
            this.mNextVibration = conductor;
            android.os.Trace.traceEnd(8388608L);
            return null;
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.vibrator.Vibration.EndInfo startVibrationOnThreadLocked(com.android.server.vibrator.VibrationStepConductor conductor) {
        android.os.Trace.traceBegin(8388608L, "startVibrationThreadLocked");
        try {
            com.android.server.vibrator.HalVibration vib = conductor.getVibration();
            int mode = startAppOpModeLocked(vib.callerInfo);
            switch (mode) {
                case 0:
                    android.os.Trace.asyncTraceBegin(8388608L, "vibration", 0);
                    if (getWrapper().getExtImpl().checkIfRichtapPatternHeEffect(vib.getEffectToPlay())) {
                        getWrapper().getExtImpl().startRichTapVibratorLocked(vib);
                        return null;
                    }
                    this.mCurrentVibration = conductor;
                    if (this.mVibrationThread.runVibrationOnVibrationThread(this.mCurrentVibration)) {
                        return null;
                    }
                    this.mCurrentVibration = null;
                    return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_ERROR_SCHEDULING);
                case 1:
                default:
                    return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_APP_OPS);
                case 2:
                    android.util.Slog.w(TAG, "Start AppOpsManager operation errored for uid " + vib.callerInfo.uid);
                    return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_ERROR_APP_OPS);
            }
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endVibrationLocked(com.android.server.vibrator.HalVibration vib, com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo, boolean shouldWriteStats) {
        vib.end(vibrationEndInfo);
        logAndRecordVibration(vib.getDebugInfo());
        if (shouldWriteStats) {
            this.mFrameworkStatsLogger.writeVibrationReportedAsync(vib.getStatsInfo(android.os.SystemClock.uptimeMillis()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endVibrationAndWriteStatsLocked(com.android.server.vibrator.VibratorManagerService.ExternalVibrationHolder vib, com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo) {
        vib.end(vibrationEndInfo);
        logAndRecordVibration(vib.getDebugInfo());
        this.mFrameworkStatsLogger.writeVibrationReportedAsync(vib.getStatsInfo(android.os.SystemClock.uptimeMillis()));
    }

    private com.android.server.vibrator.VibrationStepConductor createVibrationStepConductor(com.android.server.vibrator.HalVibration vib) {
        java.util.concurrent.CompletableFuture<java.lang.Void> requestVibrationParamsFuture = null;
        if (android.os.vibrator.Flags.adaptiveHapticsEnabled() && !vib.callerInfo.attrs.isFlagSet(16) && this.mVibratorControlService.shouldRequestVibrationParams(vib.callerInfo.attrs.getUsage())) {
            requestVibrationParamsFuture = this.mVibratorControlService.triggerVibrationParamsRequest(vib.callerInfo.uid, vib.callerInfo.attrs.getUsage(), this.mVibrationSettings.getRequestVibrationParamsTimeoutMs());
        }
        return new com.android.server.vibrator.VibrationStepConductor(vib, this.mVibrationSettings, this.mDeviceAdapter, this.mVibrationScaler, this.mFrameworkStatsLogger, requestVibrationParamsFuture, this.mVibrationThreadCallbacks);
    }

    private com.android.server.vibrator.Vibration.EndInfo startVibrationOnInputDevicesLocked(com.android.server.vibrator.HalVibration vib) {
        if (!vib.callerInfo.attrs.isFlagSet(16)) {
            vib.scaleEffects(this.mVibrationScaler);
        } else {
            vib.resolveEffects(this.mVibrationScaler.getDefaultVibrationAmplitude());
        }
        this.mInputDeviceDelegate.vibrateIfAvailable(vib.callerInfo, vib.getEffectToPlay());
        return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.FORWARDED_TO_INPUT_DEVICES);
    }

    private void logAndRecordVibration(com.android.server.vibrator.Vibration.DebugInfo info) {
        info.logMetrics(this.mFrameworkStatsLogger);
        logVibrationStatus(info.mCallerInfo.uid, info.mCallerInfo.attrs, info.mStatus);
        this.mVibratorManagerRecords.record(info);
    }

    private void logVibrationStatus(int uid, android.os.VibrationAttributes attrs, com.android.server.vibrator.Vibration.Status status) {
        switch (status) {
            case IGNORED_BACKGROUND:
                android.util.Slog.e(TAG, "Ignoring incoming vibration as process with uid= " + uid + " is background, attrs= " + attrs);
                break;
            case IGNORED_ERROR_APP_OPS:
                android.util.Slog.w(TAG, "Would be an error: vibrate from uid " + uid);
                break;
            case IGNORED_FOR_EXTERNAL:
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Ignoring incoming vibration for current external vibration");
                }
                break;
            case IGNORED_FOR_HIGHER_IMPORTANCE:
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Ignoring incoming vibration in favor of ongoing vibration with higher importance");
                }
                break;
            case IGNORED_FOR_ONGOING:
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Ignoring incoming vibration in favor of repeating vibration");
                }
                break;
            case IGNORED_FOR_RINGER_MODE:
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Ignoring incoming vibration because of ringer mode, attrs=" + attrs);
                }
                break;
            case IGNORED_FROM_VIRTUAL_DEVICE:
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Ignoring incoming vibration because it came from a virtual device, attrs= " + attrs);
                }
                break;
            default:
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Vibration for uid=" + uid + " and with attrs=" + attrs + " ended with status " + status);
                }
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportFinishedVibrationLocked(com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo) {
        android.os.Trace.traceBegin(8388608L, "reportFinishVibrationLocked");
        android.os.Trace.asyncTraceEnd(8388608L, "vibration", 0);
        try {
            com.android.server.vibrator.HalVibration vib = this.mCurrentVibration.getVibration();
            if (DEBUG) {
                android.util.Slog.d(TAG, "Reporting vibration " + vib.id + " finished with " + vibrationEndInfo);
            }
            endVibrationLocked(vib, vibrationEndInfo, false);
            finishAppOpModeLocked(vib.callerInfo);
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSyncedVibrationComplete(long vibrationId) {
        synchronized (this.mLock) {
            if (this.mCurrentVibration != null && this.mCurrentVibration.getVibration().id == vibrationId) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Synced vibration " + vibrationId + " complete, notifying thread");
                }
                this.mCurrentVibration.notifySyncedVibrationComplete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVibrationComplete(int vibratorId, long vibrationId) {
        synchronized (this.mLock) {
            if (this.mCurrentVibration != null && this.mCurrentVibration.getVibration().id == vibrationId) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Vibration " + vibrationId + " on vibrator " + vibratorId + " complete, notifying thread");
                }
                this.mCurrentVibration.notifyVibratorComplete(vibratorId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.vibrator.Vibration.EndInfo shouldIgnoreVibrationForOngoingLocked(com.android.server.vibrator.Vibration vib) {
        if (this.mCurrentExternalVibration != null) {
            return shouldIgnoreVibrationForOngoing(vib, this.mCurrentExternalVibration);
        }
        if (this.mNextVibration != null) {
            com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo = shouldIgnoreVibrationForOngoing(vib, this.mNextVibration.getVibration());
            if (vibrationEndInfo != null) {
                return vibrationEndInfo;
            }
            return getWrapper().getExtImpl().shouldIgnoreVibrationForOngoing(vib, this.mNextVibration.getVibration());
        }
        if (this.mCurrentVibration == null) {
            return null;
        }
        com.android.server.vibrator.HalVibration currentVibration = this.mCurrentVibration.getVibration();
        if (currentVibration.hasEnded() || this.mCurrentVibration.wasNotifiedToCancel()) {
            return null;
        }
        com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo2 = shouldIgnoreVibrationForOngoing(vib, currentVibration);
        if (vibrationEndInfo2 == null) {
            return getWrapper().getExtImpl().shouldIgnoreVibrationForOngoing(vib, currentVibration);
        }
        return vibrationEndInfo2;
    }

    private static com.android.server.vibrator.Vibration.EndInfo shouldIgnoreVibrationForOngoing(com.android.server.vibrator.Vibration newVibration, com.android.server.vibrator.Vibration ongoingVibration) {
        int newVibrationImportance = getVibrationImportance(newVibration);
        int ongoingVibrationImportance = getVibrationImportance(ongoingVibration);
        if (newVibrationImportance > ongoingVibrationImportance) {
            return null;
        }
        if (ongoingVibrationImportance > newVibrationImportance) {
            return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_FOR_HIGHER_IMPORTANCE, ongoingVibration.callerInfo);
        }
        if (!ongoingVibration.isRepeating() || newVibration.isRepeating()) {
            return null;
        }
        return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_FOR_ONGOING, ongoingVibration.callerInfo);
    }

    private static int getVibrationImportance(com.android.server.vibrator.Vibration vibration) {
        int usage = vibration.callerInfo.attrs.getUsage();
        if (usage == 0) {
            if (vibration.isRepeating()) {
                usage = 33;
            } else {
                usage = 18;
            }
        }
        switch (usage) {
            case 17:
                return 4;
            case 33:
                return 5;
            case 34:
            case 50:
                return 1;
            case 49:
                return 3;
            case 65:
            case 66:
                return 2;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.vibrator.Vibration.EndInfo shouldIgnoreVibrationLocked(com.android.server.vibrator.Vibration.CallerInfo callerInfo) {
        com.android.server.vibrator.Vibration.Status statusFromSettings = this.mVibrationSettings.shouldIgnoreVibration(callerInfo);
        if (statusFromSettings != null) {
            return new com.android.server.vibrator.Vibration.EndInfo(statusFromSettings);
        }
        if (!this.mZenModeManagerExt.canVibrationGo(callerInfo.opPkg)) {
            return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_FOR_SETTINGS);
        }
        if (getWrapper().getExtImpl().isBlockedByApplicationLocked()) {
            return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_FOR_HIGHER_IMPORTANCE);
        }
        int mode = checkAppOpModeLocked(callerInfo);
        if (mode != 0) {
            if (mode == 2) {
                return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_ERROR_APP_OPS);
            }
            return new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_APP_OPS);
        }
        return null;
    }

    private boolean shouldCancelVibration(com.android.server.vibrator.HalVibration vib, int usageFilter, android.os.IBinder token) {
        return vib.callerToken == token && shouldCancelVibration(vib.callerInfo.attrs, usageFilter);
    }

    private boolean shouldCancelVibration(android.os.VibrationAttributes attrs, int usageFilter) {
        return attrs.getUsage() == 0 ? usageFilter == 0 || usageFilter == -1 : (attrs.getUsage() & usageFilter) == attrs.getUsage();
    }

    private int checkAppOpModeLocked(com.android.server.vibrator.Vibration.CallerInfo callerInfo) {
        int mode = this.mAppOps.checkAudioOpNoThrow(3, callerInfo.attrs.getAudioUsage(), callerInfo.uid, callerInfo.opPkg);
        int fixedMode = fixupAppOpModeLocked(mode, callerInfo.attrs);
        if (mode != fixedMode && fixedMode == 0) {
            android.util.Slog.d(TAG, "Bypassing DND for vibrate from uid " + callerInfo.uid);
        }
        return fixedMode;
    }

    private int startAppOpModeLocked(com.android.server.vibrator.Vibration.CallerInfo callerInfo) {
        return fixupAppOpModeLocked(this.mAppOps.startOpNoThrow(3, callerInfo.uid, callerInfo.opPkg), callerInfo.attrs);
    }

    private void finishAppOpModeLocked(com.android.server.vibrator.Vibration.CallerInfo callerInfo) {
        this.mAppOps.finishOp(3, callerInfo.uid, callerInfo.opPkg);
    }

    private void enforceUpdateAppOpsStatsPermission(int uid) {
        if (uid == android.os.Binder.getCallingUid() || android.os.Binder.getCallingPid() == android.os.Process.myPid()) {
            return;
        }
        this.mContext.enforcePermission("android.permission.UPDATE_APP_OPS_STATS", android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), null);
    }

    private static boolean isEffectValid(android.os.CombinedVibration effect) {
        if (effect == null) {
            android.util.Slog.wtf(TAG, "effect must not be null");
            return false;
        }
        try {
            effect.validate();
            return true;
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(TAG, "Encountered issue when verifying CombinedVibrationEffect.", e);
            return false;
        }
    }

    private void fillVibrationFallbacks(com.android.server.vibrator.HalVibration vib, android.os.CombinedVibration effect) {
        if (effect instanceof android.os.CombinedVibration.Mono) {
            fillVibrationFallbacks(vib, ((android.os.CombinedVibration.Mono) effect).getEffect());
            return;
        }
        if (effect instanceof android.os.CombinedVibration.Stereo) {
            android.util.SparseArray<android.os.VibrationEffect> effects = ((android.os.CombinedVibration.Stereo) effect).getEffects();
            for (int i = 0; i < effects.size(); i++) {
                fillVibrationFallbacks(vib, effects.valueAt(i));
            }
            return;
        }
        if (effect instanceof android.os.CombinedVibration.Sequential) {
            java.util.List<android.os.CombinedVibration> effects2 = ((android.os.CombinedVibration.Sequential) effect).getEffects();
            for (int i2 = 0; i2 < effects2.size(); i2++) {
                fillVibrationFallbacks(vib, effects2.get(i2));
            }
        }
    }

    private void fillVibrationFallbacks(com.android.server.vibrator.HalVibration vib, android.os.VibrationEffect effect) {
        android.os.VibrationEffect.Composed composed = (android.os.VibrationEffect.Composed) effect;
        int segmentCount = composed.getSegments().size();
        for (int i = 0; i < segmentCount; i++) {
            android.os.vibrator.PrebakedSegment prebakedSegment = (android.os.vibrator.VibrationEffectSegment) composed.getSegments().get(i);
            if (prebakedSegment instanceof android.os.vibrator.PrebakedSegment) {
                android.os.vibrator.PrebakedSegment prebaked = prebakedSegment;
                android.os.VibrationEffect fallback = this.mVibrationSettings.getFallbackEffect(prebaked.getEffectId());
                if (prebaked.shouldFallback() && fallback != null) {
                    vib.addFallback(prebaked.getEffectId(), fallback);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.VibrationAttributes fixupVibrationAttributes(android.os.VibrationAttributes attrs, android.os.CombinedVibration effect) {
        if (attrs == null) {
            attrs = DEFAULT_ATTRIBUTES;
        }
        attrs.getUsage();
        int usage = getWrapper().getExtImpl().fixupVibrationAttributes(attrs, effect);
        if (usage == 0 && effect != null && effect.isHapticFeedbackCandidate()) {
            usage = 18;
        }
        int flags = attrs.getFlags();
        if ((flags & 19) != 0 && !hasPermission("android.permission.WRITE_SECURE_SETTINGS") && !hasPermission("android.permission.MODIFY_PHONE_STATE") && !hasPermission("android.permission.MODIFY_AUDIO_ROUTING")) {
            flags &= -20;
        }
        if (usage == attrs.getUsage() && flags == attrs.getFlags()) {
            return attrs;
        }
        return new android.os.VibrationAttributes.Builder(attrs).setUsage(usage).setFlags(flags, attrs.getFlags()).build();
    }

    private android.util.SparseArray<android.os.vibrator.PrebakedSegment> fixupAlwaysOnEffectsLocked(android.os.CombinedVibration effect) {
        android.util.SparseArray<android.os.VibrationEffect> effects;
        android.os.Trace.traceBegin(8388608L, "fixupAlwaysOnEffectsLocked");
        try {
            if (effect instanceof android.os.CombinedVibration.Mono) {
                final android.os.VibrationEffect syncedEffect = ((android.os.CombinedVibration.Mono) effect).getEffect();
                effects = transformAllVibratorsLocked(new java.util.function.Function() { // from class: com.android.server.vibrator.VibratorManagerService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.vibrator.VibratorManagerService.lambda$fixupAlwaysOnEffectsLocked$1(syncedEffect, (com.android.server.vibrator.VibratorController) obj);
                    }
                });
            } else {
                if (!(effect instanceof android.os.CombinedVibration.Stereo)) {
                    return null;
                }
                effects = ((android.os.CombinedVibration.Stereo) effect).getEffects();
            }
            android.util.SparseArray<android.os.vibrator.PrebakedSegment> result = new android.util.SparseArray<>();
            for (int i = 0; i < effects.size(); i++) {
                android.os.vibrator.PrebakedSegment prebaked = extractPrebakedSegment(effects.valueAt(i));
                if (prebaked == null) {
                    android.util.Slog.e(TAG, "Only prebaked effects supported for always-on.");
                    return null;
                }
                int vibratorId = effects.keyAt(i);
                com.android.server.vibrator.VibratorController vibrator = this.mVibrators.get(vibratorId);
                if (vibrator != null && vibrator.hasCapability(64L)) {
                    result.put(vibratorId, prebaked);
                }
            }
            int i2 = result.size();
            if (i2 == 0) {
                return null;
            }
            return result;
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    static /* synthetic */ android.os.VibrationEffect lambda$fixupAlwaysOnEffectsLocked$1(android.os.VibrationEffect syncedEffect, com.android.server.vibrator.VibratorController unused) {
        return syncedEffect;
    }

    private static android.os.vibrator.PrebakedSegment extractPrebakedSegment(android.os.VibrationEffect effect) {
        if (effect instanceof android.os.VibrationEffect.Composed) {
            android.os.VibrationEffect.Composed composed = (android.os.VibrationEffect.Composed) effect;
            if (composed.getSegments().size() == 1) {
                android.os.vibrator.PrebakedSegment prebakedSegment = (android.os.vibrator.VibrationEffectSegment) composed.getSegments().get(0);
                if (prebakedSegment instanceof android.os.vibrator.PrebakedSegment) {
                    return prebakedSegment;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    private int fixupAppOpModeLocked(int mode, android.os.VibrationAttributes attrs) {
        if (mode == 1 && attrs.isFlagSet(1)) {
            return 0;
        }
        return mode;
    }

    private boolean hasPermission(java.lang.String permission) {
        return this.mContext.checkCallingOrSelfPermission(permission) == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldCancelOnScreenOffLocked(com.android.server.vibrator.VibrationStepConductor conductor) {
        if (conductor == null) {
            return false;
        }
        com.android.server.vibrator.HalVibration vib = conductor.getVibration();
        return this.mVibrationSettings.shouldCancelVibrationOnScreenOff(vib.callerInfo, vib.stats.getCreateUptimeMillis());
    }

    private void onAllVibratorsLocked(java.util.function.Consumer<com.android.server.vibrator.VibratorController> consumer) {
        for (int i = 0; i < this.mVibrators.size(); i++) {
            consumer.accept(this.mVibrators.valueAt(i));
        }
    }

    private <T> android.util.SparseArray<T> transformAllVibratorsLocked(java.util.function.Function<com.android.server.vibrator.VibratorController, T> fn) {
        android.util.SparseArray<T> ret = new android.util.SparseArray<>(this.mVibrators.size());
        for (int i = 0; i < this.mVibrators.size(); i++) {
            ret.put(this.mVibrators.keyAt(i), fn.apply(this.mVibrators.valueAt(i)));
        }
        return ret;
    }

    static class Injector {
        Injector() {
        }

        com.android.server.vibrator.VibratorManagerService.NativeWrapper getNativeWrapper() {
            return new com.android.server.vibrator.VibratorManagerService.NativeWrapper();
        }

        android.os.Handler createHandler(android.os.Looper looper) {
            return new android.os.Handler(looper);
        }

        com.android.internal.app.IBatteryStats getBatteryStatsService() {
            return com.android.internal.app.IBatteryStats.Stub.asInterface(android.os.ServiceManager.getService("batterystats"));
        }

        com.android.server.vibrator.VibratorFrameworkStatsLogger getFrameworkStatsLogger(android.os.Handler handler) {
            return new com.android.server.vibrator.VibratorFrameworkStatsLogger(handler);
        }

        com.android.server.vibrator.VibratorController createVibratorController(int vibratorId, com.android.server.vibrator.VibratorController.OnVibrationCompleteListener listener) {
            return new com.android.server.vibrator.VibratorController(vibratorId, listener);
        }

        com.android.server.vibrator.HapticFeedbackVibrationProvider createHapticFeedbackVibrationProvider(android.content.res.Resources resources, android.os.VibratorInfo vibratorInfo) {
            return new com.android.server.vibrator.HapticFeedbackVibrationProvider(resources, vibratorInfo);
        }

        void addService(java.lang.String name, android.os.IBinder service) {
            android.os.ServiceManager.addService(name, service);
        }

        com.android.server.vibrator.VibratorControllerHolder createVibratorControllerHolder() {
            return new com.android.server.vibrator.VibratorControllerHolder();
        }

        boolean isServiceDeclared(java.lang.String name) {
            return android.os.ServiceManager.isDeclared(name);
        }
    }

    private final class VibrationThreadCallbacks implements com.android.server.vibrator.VibrationThread.VibratorManagerHooks {
        private VibrationThreadCallbacks() {
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public boolean prepareSyncedVibration(long requiredCapabilities, int[] vibratorIds) {
            if ((com.android.server.vibrator.VibratorManagerService.this.mCapabilities & requiredCapabilities) != requiredCapabilities) {
                return false;
            }
            return com.android.server.vibrator.VibratorManagerService.this.mNativeWrapper.prepareSynced(vibratorIds);
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public boolean triggerSyncedVibration(long vibrationId) {
            return com.android.server.vibrator.VibratorManagerService.this.mNativeWrapper.triggerSynced(vibrationId);
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public void cancelSyncedVibration() {
            com.android.server.vibrator.VibratorManagerService.this.mNativeWrapper.cancelSynced();
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public void noteVibratorOn(int uid, long duration) {
            if (duration <= 0) {
                return;
            }
            if (duration == Long.MAX_VALUE) {
                duration = 5000;
            }
            try {
                com.android.server.vibrator.VibratorManagerService.this.mBatteryStatsService.noteVibratorOn(uid, duration);
                com.android.server.vibrator.VibratorManagerService.this.mFrameworkStatsLogger.writeVibratorStateOnAsync(uid, duration);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.vibrator.VibratorManagerService.TAG, "Error logging VibratorStateChanged to ON", e);
            }
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public void noteVibratorOff(int uid) {
            try {
                com.android.server.vibrator.VibratorManagerService.this.mBatteryStatsService.noteVibratorOff(uid);
                com.android.server.vibrator.VibratorManagerService.this.mFrameworkStatsLogger.writeVibratorStateOffAsync(uid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.vibrator.VibratorManagerService.TAG, "Error logging VibratorStateChanged to OFF", e);
            }
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public void onVibrationCompleted(long vibrationId, com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo) {
            if (com.android.server.vibrator.VibratorManagerService.DEBUG) {
                android.util.Slog.d(com.android.server.vibrator.VibratorManagerService.TAG, "Vibration " + vibrationId + " finished with " + vibrationEndInfo);
            }
            synchronized (com.android.server.vibrator.VibratorManagerService.this.mLock) {
                if (com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration != null && com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration.getVibration().id == vibrationId) {
                    com.android.server.vibrator.VibratorManagerService.this.reportFinishedVibrationLocked(vibrationEndInfo);
                }
            }
        }

        @Override // com.android.server.vibrator.VibrationThread.VibratorManagerHooks
        public void onVibrationThreadReleased(long vibrationId) {
            if (com.android.server.vibrator.VibratorManagerService.DEBUG) {
                android.util.Slog.d(com.android.server.vibrator.VibratorManagerService.TAG, "VibrationThread released after finished vibration");
            }
            synchronized (com.android.server.vibrator.VibratorManagerService.this.mLock) {
                if (com.android.server.vibrator.VibratorManagerService.DEBUG) {
                    android.util.Slog.d(com.android.server.vibrator.VibratorManagerService.TAG, "Processing VibrationThread released callback");
                }
                if (android.os.Build.IS_DEBUGGABLE && com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration != null && com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration.getVibration().id != vibrationId) {
                    android.util.Slog.wtf(com.android.server.vibrator.VibratorManagerService.TAG, android.text.TextUtils.formatSimple("VibrationId mismatch on release. expected=%d, released=%d", new java.lang.Object[]{java.lang.Long.valueOf(com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration.getVibration().id), java.lang.Long.valueOf(vibrationId)}));
                }
                if (com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration != null) {
                    com.android.server.vibrator.VibratorManagerService.this.mFrameworkStatsLogger.writeVibrationReportedAsync(com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration.getVibration().getStatsInfo(android.os.SystemClock.uptimeMillis()));
                    com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration = null;
                }
                if (com.android.server.vibrator.VibratorManagerService.this.mNextVibration != null) {
                    com.android.server.vibrator.VibrationStepConductor nextConductor = com.android.server.vibrator.VibratorManagerService.this.mNextVibration;
                    com.android.server.vibrator.VibratorManagerService.this.mNextVibration = null;
                    com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo = com.android.server.vibrator.VibratorManagerService.this.startVibrationOnThreadLocked(nextConductor);
                    if (vibrationEndInfo != null) {
                        com.android.server.vibrator.VibratorManagerService.this.endVibrationLocked(nextConductor.getVibration(), vibrationEndInfo, true);
                    }
                }
            }
        }
    }

    private static final class VibrationCompleteListener implements com.android.server.vibrator.VibratorController.OnVibrationCompleteListener, com.android.server.vibrator.VibratorManagerService.OnSyncedVibrationCompleteListener {
        private java.lang.ref.WeakReference<com.android.server.vibrator.VibratorManagerService> mServiceRef;

        VibrationCompleteListener(com.android.server.vibrator.VibratorManagerService service) {
            this.mServiceRef = new java.lang.ref.WeakReference<>(service);
        }

        @Override // com.android.server.vibrator.VibratorManagerService.OnSyncedVibrationCompleteListener
        public void onComplete(long vibrationId) {
            com.android.server.vibrator.VibratorManagerService service = this.mServiceRef.get();
            if (service != null) {
                service.onSyncedVibrationComplete(vibrationId);
            }
        }

        @Override // com.android.server.vibrator.VibratorController.OnVibrationCompleteListener
        public void onComplete(int vibratorId, long vibrationId) {
            com.android.server.vibrator.VibratorManagerService service = this.mServiceRef.get();
            if (service != null) {
                service.onVibrationComplete(vibratorId, vibrationId);
            }
        }
    }

    private static final class AlwaysOnVibration {
        public final int alwaysOnId;
        public final com.android.server.vibrator.Vibration.CallerInfo callerInfo;
        public final android.util.SparseArray<android.os.vibrator.PrebakedSegment> effects;

        AlwaysOnVibration(int alwaysOnId, com.android.server.vibrator.Vibration.CallerInfo callerInfo, android.util.SparseArray<android.os.vibrator.PrebakedSegment> effects) {
            this.alwaysOnId = alwaysOnId;
            this.callerInfo = callerInfo;
            this.effects = effects;
        }
    }

    private final class ExternalVibrationHolder extends com.android.server.vibrator.Vibration implements android.os.IBinder.DeathRecipient {
        public final android.os.ExternalVibration externalVibration;
        private com.android.server.vibrator.Vibration.Status mStatus;
        public android.os.ExternalVibrationScale scale;

        private ExternalVibrationHolder(android.os.ExternalVibration externalVibration) {
            super(externalVibration.getToken(), new com.android.server.vibrator.Vibration.CallerInfo(externalVibration.getVibrationAttributes(), externalVibration.getUid(), -1, externalVibration.getPackage(), null));
            this.scale = new android.os.ExternalVibrationScale();
            this.externalVibration = externalVibration;
            this.mStatus = com.android.server.vibrator.Vibration.Status.RUNNING;
        }

        public void scale(com.android.server.vibrator.VibrationScaler scaler, int usage) {
            this.scale.scaleLevel = scaler.getScaleLevel(usage);
            this.scale.adaptiveHapticsScale = scaler.getAdaptiveHapticsScale(usage);
            this.stats.reportAdaptiveScale(this.scale.adaptiveHapticsScale);
        }

        public void mute() {
            this.externalVibration.mute();
        }

        public void linkToDeath() {
            this.externalVibration.linkToDeath(this);
        }

        public void unlinkToDeath() {
            this.externalVibration.unlinkToDeath(this);
        }

        public boolean isHoldingSameVibration(android.os.ExternalVibration externalVibration) {
            return this.externalVibration.equals(externalVibration);
        }

        public void end(com.android.server.vibrator.Vibration.EndInfo info) {
            if (this.mStatus != com.android.server.vibrator.Vibration.Status.RUNNING) {
                return;
            }
            this.mStatus = info.status;
            this.stats.reportEnded(info.endedBy);
            if (this.stats.hasStarted()) {
                this.stats.reportVibratorOn(this.stats.getEndUptimeMillis() - this.stats.getStartUptimeMillis());
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.vibrator.VibratorManagerService.this.mLock) {
                if (com.android.server.vibrator.VibratorManagerService.this.mCurrentExternalVibration != null) {
                    if (com.android.server.vibrator.VibratorManagerService.DEBUG) {
                        android.util.Slog.d(com.android.server.vibrator.VibratorManagerService.TAG, "External vibration finished because binder died");
                    }
                    com.android.server.vibrator.VibratorManagerService.this.endExternalVibrateLocked(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.CANCELLED_BINDER_DIED), false);
                }
            }
        }

        public com.android.server.vibrator.Vibration.DebugInfo getDebugInfo() {
            return new com.android.server.vibrator.Vibration.DebugInfo(this.mStatus, this.stats, null, null, this.scale.scaleLevel, this.scale.adaptiveHapticsScale, this.callerInfo);
        }

        public com.android.server.vibrator.VibrationStats.StatsInfo getStatsInfo(long completionUptimeMillis) {
            return new com.android.server.vibrator.VibrationStats.StatsInfo(this.externalVibration.getUid(), 3, this.externalVibration.getVibrationAttributes().getUsage(), this.mStatus, this.stats, completionUptimeMillis);
        }

        @Override // com.android.server.vibrator.Vibration
        boolean isRepeating() {
            int usage = this.externalVibration.getVibrationAttributes().getUsage();
            return usage == 33 || usage == 17;
        }
    }

    public static class NativeWrapper {
        private long mNativeServicePtr = 0;

        public void init(com.android.server.vibrator.VibratorManagerService.OnSyncedVibrationCompleteListener listener) {
            this.mNativeServicePtr = com.android.server.vibrator.VibratorManagerService.nativeInit(listener);
            long finalizerPtr = com.android.server.vibrator.VibratorManagerService.nativeGetFinalizer();
            if (finalizerPtr != 0) {
                libcore.util.NativeAllocationRegistry registry = libcore.util.NativeAllocationRegistry.createMalloced(com.android.server.vibrator.VibratorManagerService.class.getClassLoader(), finalizerPtr);
                registry.registerNativeAllocation(this, this.mNativeServicePtr);
            }
        }

        public long getCapabilities() {
            return com.android.server.vibrator.VibratorManagerService.nativeGetCapabilities(this.mNativeServicePtr);
        }

        public int[] getVibratorIds() {
            return com.android.server.vibrator.VibratorManagerService.nativeGetVibratorIds(this.mNativeServicePtr);
        }

        public boolean prepareSynced(int[] vibratorIds) {
            return com.android.server.vibrator.VibratorManagerService.nativePrepareSynced(this.mNativeServicePtr, vibratorIds);
        }

        public boolean triggerSynced(long vibrationId) {
            return com.android.server.vibrator.VibratorManagerService.nativeTriggerSynced(this.mNativeServicePtr, vibrationId);
        }

        public void cancelSynced() {
            com.android.server.vibrator.VibratorManagerService.nativeCancelSynced(this.mNativeServicePtr);
        }
    }

    private static final class VibratorManagerRecords {
        private final com.android.server.vibrator.VibratorManagerService.VibrationRecords mAggregatedVibrationHistory;
        private final com.android.server.vibrator.VibratorManagerService.VibrationRecords mRecentVibrations;

        VibratorManagerRecords(int recentVibrationSizeLimit, int aggregationSizeLimit, int aggregationTimeLimit) {
            this.mAggregatedVibrationHistory = new com.android.server.vibrator.VibratorManagerService.VibrationRecords(aggregationSizeLimit, aggregationTimeLimit);
            this.mRecentVibrations = new com.android.server.vibrator.VibratorManagerService.VibrationRecords(recentVibrationSizeLimit, 0);
        }

        synchronized void record(com.android.server.vibrator.Vibration.DebugInfo info) {
            com.android.server.vibrator.GroupedAggregatedLogRecords.AggregatedLogRecord<com.android.server.vibrator.VibratorManagerService.VibrationRecord> droppedRecord = this.mRecentVibrations.add(new com.android.server.vibrator.VibratorManagerService.VibrationRecord(info));
            if (droppedRecord != null) {
                this.mAggregatedVibrationHistory.add((com.android.server.vibrator.VibratorManagerService.VibrationRecord) droppedRecord.getLatest());
            }
        }

        synchronized void dump(android.util.IndentingPrintWriter pw) {
            pw.println("Recent vibrations:");
            pw.increaseIndent();
            this.mRecentVibrations.dump(pw);
            pw.decreaseIndent();
            pw.println();
            pw.println();
            pw.println("Aggregated vibration history:");
            pw.increaseIndent();
            this.mAggregatedVibrationHistory.dump(pw);
            pw.decreaseIndent();
        }

        synchronized void dump(android.util.proto.ProtoOutputStream proto) {
            this.mRecentVibrations.dump(proto);
        }
    }

    private static final class VibrationRecords extends com.android.server.vibrator.GroupedAggregatedLogRecords<com.android.server.vibrator.VibratorManagerService.VibrationRecord> {
        VibrationRecords(int sizeLimit, int aggregationTimeLimit) {
            super(sizeLimit, aggregationTimeLimit);
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords
        void dumpGroupHeader(android.util.IndentingPrintWriter pw, int usage) {
            pw.println(android.os.VibrationAttributes.usageToString(usage) + ":");
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords
        long findGroupKeyProtoFieldId(int usage) {
            switch (usage) {
                case 17:
                    return 2246267895823L;
                case 33:
                    return 2246267895821L;
                case 49:
                    return 2246267895822L;
                default:
                    return 2246267895824L;
            }
        }
    }

    private static final class VibrationRecord implements com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord {
        private final com.android.server.vibrator.Vibration.DebugInfo mInfo;

        VibrationRecord(com.android.server.vibrator.Vibration.DebugInfo info) {
            this.mInfo = info;
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord
        public int getGroupKey() {
            return this.mInfo.mCallerInfo.attrs.getUsage();
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord
        public long getCreateUptimeMs() {
            return this.mInfo.mCreateTime;
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord
        public boolean mayAggregate(com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord record) {
            if (!(record instanceof com.android.server.vibrator.VibratorManagerService.VibrationRecord)) {
                return false;
            }
            com.android.server.vibrator.Vibration.DebugInfo info = ((com.android.server.vibrator.VibratorManagerService.VibrationRecord) record).mInfo;
            return this.mInfo.mCallerInfo.uid == info.mCallerInfo.uid && java.util.Objects.equals(this.mInfo.mCallerInfo.attrs, info.mCallerInfo.attrs) && java.util.Objects.equals(this.mInfo.mPlayedEffect, info.mPlayedEffect);
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord
        public void dump(android.util.IndentingPrintWriter pw) {
            this.mInfo.dumpCompact(pw);
        }

        @Override // com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord
        public void dump(android.util.proto.ProtoOutputStream proto, long fieldId) {
            this.mInfo.dump(proto, fieldId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearNextVibrationLocked(com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo) {
        if (this.mNextVibration != null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Dropping pending vibration " + this.mNextVibration.getVibration().id + " with end info: " + vibrationEndInfo);
            }
            endVibrationLocked(this.mNextVibration.getVibration(), vibrationEndInfo, true);
            this.mNextVibration = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endExternalVibrateLocked(com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo, boolean continueExternalControl) {
        android.os.Trace.traceBegin(8388608L, "endExternalVibrateLocked");
        try {
            if (this.mCurrentExternalVibration == null) {
                return;
            }
            this.mCurrentExternalVibration.unlinkToDeath();
            if (!continueExternalControl) {
                setExternalControl(false, this.mCurrentExternalVibration.stats);
            }
            endVibrationAndWriteStatsLocked(this.mCurrentExternalVibration, vibrationEndInfo);
            this.mCurrentExternalVibration = null;
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    private com.android.server.vibrator.HapticFeedbackVibrationProvider getHapticVibrationProvider() {
        synchronized (this.mLock) {
            if (this.mHapticFeedbackVibrationProvider != null) {
                return this.mHapticFeedbackVibrationProvider;
            }
            android.os.VibratorInfo combinedVibratorInfo = getCombinedVibratorInfo();
            if (combinedVibratorInfo == null) {
                return null;
            }
            com.android.server.vibrator.HapticFeedbackVibrationProvider hapticFeedbackVibrationProviderCreateHapticFeedbackVibrationProvider = this.mInjector.createHapticFeedbackVibrationProvider(this.mContext.getResources(), combinedVibratorInfo);
            this.mHapticFeedbackVibrationProvider = hapticFeedbackVibrationProviderCreateHapticFeedbackVibrationProvider;
            return hapticFeedbackVibrationProviderCreateHapticFeedbackVibrationProvider;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.VibratorInfo getCombinedVibratorInfo() {
        synchronized (this.mLock) {
            if (this.mCombinedVibratorInfo != null) {
                return this.mCombinedVibratorInfo;
            }
            if (this.mVibratorIds.length == 0) {
                android.os.VibratorInfo vibratorInfo = android.os.VibratorInfo.EMPTY_VIBRATOR_INFO;
                this.mCombinedVibratorInfo = vibratorInfo;
                return vibratorInfo;
            }
            android.os.VibratorInfo[] infos = new android.os.VibratorInfo[this.mVibratorIds.length];
            for (int i = 0; i < this.mVibratorIds.length; i++) {
                android.os.VibratorInfo info = getVibratorInfo(this.mVibratorIds[i]);
                if (info == null) {
                    return null;
                }
                infos[i] = info;
            }
            android.os.VibratorInfo vibratorInfoCreate = android.os.vibrator.VibratorInfoFactory.create(-1, infos);
            this.mCombinedVibratorInfo = vibratorInfoCreate;
            return vibratorInfoCreate;
        }
    }

    final class ExternalVibratorService extends android.os.IExternalVibratorService.Stub {
        ExternalVibratorService() {
        }

        public android.os.ExternalVibrationScale onExternalVibrationStart(android.os.ExternalVibration vib) {
            com.android.server.vibrator.VibratorManagerService.ExternalVibrationHolder vibHolder = new com.android.server.vibrator.VibratorManagerService.ExternalVibrationHolder(vib);
            vibHolder.scale.scaleLevel = -100;
            boolean alreadyUnderExternalControl = false;
            boolean waitForCompletion = false;
            synchronized (com.android.server.vibrator.VibratorManagerService.this.mLock) {
                if (!hasExternalControlCapability()) {
                    com.android.server.vibrator.VibratorManagerService.this.endVibrationAndWriteStatsLocked(vibHolder, new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_UNSUPPORTED));
                    return vibHolder.scale;
                }
                if (android.app.ActivityManager.checkComponentPermission("android.permission.VIBRATE", vib.getUid(), -1, true) != 0) {
                    android.util.Slog.w(com.android.server.vibrator.VibratorManagerService.TAG, "pkg=" + vib.getPackage() + ", uid=" + vib.getUid() + " tried to play externally controlled vibration without VIBRATE permission, ignoring.");
                    com.android.server.vibrator.VibratorManagerService.this.endVibrationAndWriteStatsLocked(vibHolder, new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_MISSING_PERMISSION));
                    return vibHolder.scale;
                }
                com.android.server.vibrator.Vibration.EndInfo vibrationEndInfo = com.android.server.vibrator.VibratorManagerService.this.shouldIgnoreVibrationLocked(vibHolder.callerInfo);
                if (vibrationEndInfo == null && com.android.server.vibrator.VibratorManagerService.this.mCurrentExternalVibration != null && com.android.server.vibrator.VibratorManagerService.this.mCurrentExternalVibration.isHoldingSameVibration(vib)) {
                    return com.android.server.vibrator.VibratorManagerService.this.mCurrentExternalVibration.scale;
                }
                if (vibrationEndInfo == null) {
                    vibrationEndInfo = com.android.server.vibrator.VibratorManagerService.this.shouldIgnoreVibrationForOngoingLocked(vibHolder);
                }
                if (vibrationEndInfo != null) {
                    com.android.server.vibrator.VibratorManagerService.this.endVibrationAndWriteStatsLocked(vibHolder, vibrationEndInfo);
                    return vibHolder.scale;
                }
                if (com.android.server.vibrator.VibratorManagerService.this.mCurrentExternalVibration == null) {
                    if (com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration != null) {
                        vibHolder.stats.reportInterruptedAnotherVibration(com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration.getVibration().callerInfo);
                        com.android.server.vibrator.VibratorManagerService.this.clearNextVibrationLocked(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_FOR_EXTERNAL, vibHolder.callerInfo));
                        com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration.notifyCancelled(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.CANCELLED_SUPERSEDED, vibHolder.callerInfo), true);
                        waitForCompletion = true;
                    }
                    if (com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration == null) {
                        com.android.server.vibrator.VibratorManagerService.this.getWrapper().getExtImpl().stopRichtapVibration();
                    }
                } else {
                    alreadyUnderExternalControl = true;
                    com.android.server.vibrator.VibratorManagerService.this.mCurrentExternalVibration.mute();
                    vibHolder.stats.reportInterruptedAnotherVibration(com.android.server.vibrator.VibratorManagerService.this.mCurrentExternalVibration.callerInfo);
                    com.android.server.vibrator.VibratorManagerService.this.endExternalVibrateLocked(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.CANCELLED_SUPERSEDED, vibHolder.callerInfo), true);
                }
                android.os.VibrationAttributes attrs = com.android.server.vibrator.VibratorManagerService.this.fixupVibrationAttributes(vib.getVibrationAttributes(), null);
                if (attrs.isFlagSet(4)) {
                    com.android.server.vibrator.VibratorManagerService.this.mVibrationSettings.update();
                }
                com.android.server.vibrator.VibratorManagerService.this.mCurrentExternalVibration = vibHolder;
                vibHolder.linkToDeath();
                vibHolder.scale(com.android.server.vibrator.VibratorManagerService.this.mVibrationScaler, attrs.getUsage());
                if (waitForCompletion && !com.android.server.vibrator.VibratorManagerService.this.mVibrationThread.waitForThreadIdle(5000L)) {
                    android.util.Slog.e(com.android.server.vibrator.VibratorManagerService.TAG, "Timed out waiting for vibration to cancel");
                    synchronized (com.android.server.vibrator.VibratorManagerService.this.mLock) {
                        com.android.server.vibrator.VibratorManagerService.this.endExternalVibrateLocked(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.IGNORED_ERROR_CANCELLING), false);
                        vibHolder.scale.scaleLevel = -100;
                    }
                    return vibHolder.scale;
                }
                if (!alreadyUnderExternalControl) {
                    if (com.android.server.vibrator.VibratorManagerService.DEBUG) {
                        android.util.Slog.d(com.android.server.vibrator.VibratorManagerService.TAG, "Vibrator going under external control.");
                    }
                    com.android.server.vibrator.VibratorManagerService.this.setExternalControl(true, vibHolder.stats);
                }
                if (com.android.server.vibrator.VibratorManagerService.DEBUG) {
                    android.util.Slog.d(com.android.server.vibrator.VibratorManagerService.TAG, "Playing external vibration: " + vib);
                }
                vibHolder.stats.reportStarted();
                return vibHolder.scale;
            }
        }

        public void onExternalVibrationStop(android.os.ExternalVibration vib) {
            synchronized (com.android.server.vibrator.VibratorManagerService.this.mLock) {
                if (com.android.server.vibrator.VibratorManagerService.this.mCurrentExternalVibration != null && com.android.server.vibrator.VibratorManagerService.this.mCurrentExternalVibration.isHoldingSameVibration(vib)) {
                    if (com.android.server.vibrator.VibratorManagerService.DEBUG) {
                        android.util.Slog.d(com.android.server.vibrator.VibratorManagerService.TAG, "Stopping external vibration: " + vib);
                    }
                    com.android.server.vibrator.VibratorManagerService.this.endExternalVibrateLocked(new com.android.server.vibrator.Vibration.EndInfo(com.android.server.vibrator.Vibration.Status.FINISHED), false);
                }
            }
        }

        private boolean hasExternalControlCapability() {
            for (int i = 0; i < com.android.server.vibrator.VibratorManagerService.this.mVibrators.size(); i++) {
                if (((com.android.server.vibrator.VibratorController) com.android.server.vibrator.VibratorManagerService.this.mVibrators.valueAt(i)).hasCapability(8L)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final class VibratorManagerShellCommand extends android.os.ShellCommand {
        public static final java.lang.String SHELL_PACKAGE_NAME = "com.android.shell";
        public static final long VIBRATION_END_TIMEOUT_MS = 500;
        private final android.os.IBinder mShellCallbacksToken;

        private final class CommonOptions {
            public boolean background;
            public java.lang.String description;
            public boolean force;

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:17:0x003c  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            CommonOptions() {
                /*
                    r4 = this;
                    com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.this = r5
                    r4.<init>()
                    r0 = 0
                    r4.force = r0
                    java.lang.String r1 = "Shell command"
                    r4.description = r1
                    r4.background = r0
                Le:
                    java.lang.String r1 = r5.peekNextArg()
                    r2 = r1
                    if (r1 == 0) goto L58
                    int r1 = r2.hashCode()
                    r3 = 1
                    switch(r1) {
                        case 1461: goto L32;
                        case 1495: goto L28;
                        case 1497: goto L1e;
                        default: goto L1d;
                    }
                L1d:
                    goto L3c
                L1e:
                    java.lang.String r1 = "-f"
                    boolean r1 = r2.equals(r1)
                    if (r1 == 0) goto L1d
                    r1 = r0
                    goto L3d
                L28:
                    java.lang.String r1 = "-d"
                    boolean r1 = r2.equals(r1)
                    if (r1 == 0) goto L1d
                    r1 = 2
                    goto L3d
                L32:
                    java.lang.String r1 = "-B"
                    boolean r1 = r2.equals(r1)
                    if (r1 == 0) goto L1d
                    r1 = r3
                    goto L3d
                L3c:
                    r1 = -1
                L3d:
                    switch(r1) {
                        case 0: goto L51;
                        case 1: goto L4b;
                        case 2: goto L41;
                        default: goto L40;
                    }
                L40:
                    return
                L41:
                    r5.getNextArgRequired()
                    java.lang.String r1 = r5.getNextArgRequired()
                    r4.description = r1
                    goto L57
                L4b:
                    r5.getNextArgRequired()
                    r4.background = r3
                    goto L57
                L51:
                    r5.getNextArgRequired()
                    r4.force = r3
                L57:
                    goto Le
                L58:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions.<init>(com.android.server.vibrator.VibratorManagerService$VibratorManagerShellCommand):void");
            }
        }

        private VibratorManagerShellCommand(android.os.IBinder shellCallbacksToken) {
            this.mShellCallbacksToken = shellCallbacksToken;
        }

        public int onCommand(java.lang.String cmd) {
            android.os.Trace.traceBegin(8388608L, "onCommand " + cmd);
            try {
                return "list".equals(cmd) ? runListVibrators() : "synced".equals(cmd) ? runMono() : "combined".equals(cmd) ? runStereo() : "sequential".equals(cmd) ? runSequential() : "xml".equals(cmd) ? runXml() : "cancel".equals(cmd) ? runCancel() : "feedback".equals(cmd) ? runHapticFeedback() : handleDefaultCommands(cmd);
            } finally {
                android.os.Trace.traceEnd(8388608L);
            }
        }

        private int runListVibrators() {
            java.io.PrintWriter pw = getOutPrintWriter();
            try {
                if (com.android.server.vibrator.VibratorManagerService.this.mVibratorIds.length == 0) {
                    pw.println("No vibrator found");
                } else {
                    for (int id : com.android.server.vibrator.VibratorManagerService.this.mVibratorIds) {
                        pw.println(id);
                    }
                }
                pw.println("");
                if (pw != null) {
                    pw.close();
                }
                return 0;
            } catch (java.lang.Throwable th) {
                if (pw != null) {
                    try {
                        pw.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v3, types: [com.android.server.vibrator.VibratorManagerService] */
        /* JADX WARN: Type inference failed for: r7v0 */
        /* JADX WARN: Type inference failed for: r7v1, types: [android.os.IBinder] */
        /* JADX WARN: Type inference failed for: r7v2 */
        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        private void runVibrate(com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions commonOptions, android.os.CombinedVibration combined) throws java.lang.Throwable {
            android.os.VibrationAttributes attrs = createVibrationAttributes(commonOptions);
            ?? r7 = commonOptions.background ? com.android.server.vibrator.VibratorManagerService.this : this.mShellCallbacksToken;
            int uid = android.os.Binder.getCallingUid();
            java.lang.String resolvedPackageName = android.app.AppOpsManager.resolvePackageName(uid, "com.android.shell");
            com.android.server.vibrator.HalVibration vib = com.android.server.vibrator.VibratorManagerService.this.vibrateWithPermissionCheck(uid, 0, resolvedPackageName, combined, attrs, commonOptions.description, r7);
            maybeWaitOnVibration(vib, commonOptions);
        }

        private int runMono() throws java.lang.Throwable {
            runVibrate(new com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions(this), android.os.CombinedVibration.createParallel(nextEffect()));
            return 0;
        }

        private int runStereo() throws java.lang.Throwable {
            com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions commonOptions = new com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions(this);
            android.os.CombinedVibration.ParallelCombination combination = android.os.CombinedVibration.startParallel();
            while ("-v".equals(getNextOption())) {
                int vibratorId = java.lang.Integer.parseInt(getNextArgRequired());
                combination.addVibrator(vibratorId, nextEffect());
            }
            runVibrate(commonOptions, combination.combine());
            return 0;
        }

        private int runSequential() throws java.lang.Throwable {
            com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions commonOptions = new com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions(this);
            android.os.CombinedVibration.SequentialCombination combination = android.os.CombinedVibration.startSequential();
            while ("-v".equals(getNextOption())) {
                int vibratorId = java.lang.Integer.parseInt(getNextArgRequired());
                combination.addNext(vibratorId, nextEffect());
            }
            runVibrate(commonOptions, combination.combine());
            return 0;
        }

        private int runXml() throws java.lang.Throwable {
            com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions commonOptions = new com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions(this);
            java.lang.String xml = getNextArgRequired();
            android.os.CombinedVibration vibration = parseXml(xml);
            runVibrate(commonOptions, vibration);
            return 0;
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        private int runCancel() {
            com.android.server.vibrator.VibratorManagerService.this.cancelVibrate(-1, com.android.server.vibrator.VibratorManagerService.this);
            return 0;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r2v2, types: [com.android.server.vibrator.VibratorManagerService] */
        /* JADX WARN: Type inference failed for: r9v0 */
        /* JADX WARN: Type inference failed for: r9v1, types: [android.os.IBinder] */
        /* JADX WARN: Type inference failed for: r9v2 */
        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        private int runHapticFeedback() {
            com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions commonOptions = new com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions(this);
            int constant = java.lang.Integer.parseInt(getNextArgRequired());
            com.android.server.vibrator.HalVibration vib = com.android.server.vibrator.VibratorManagerService.this.performHapticFeedbackInternal(android.os.Binder.getCallingUid(), 0, "com.android.shell", constant, commonOptions.force, commonOptions.description, commonOptions.background ? com.android.server.vibrator.VibratorManagerService.this : this.mShellCallbacksToken, false);
            maybeWaitOnVibration(vib, commonOptions);
            return 0;
        }

        private android.os.VibrationEffect nextEffect() {
            android.os.VibrationEffect.Composition composition = android.os.VibrationEffect.startComposition();
            while (true) {
                java.lang.String nextArg = peekNextArg();
                if (nextArg != null) {
                    if ("oneshot".equals(nextArg)) {
                        addOneShotToComposition(composition);
                    } else if ("waveform".equals(nextArg)) {
                        addWaveformToComposition(composition);
                    } else if ("prebaked".equals(nextArg)) {
                        addPrebakedToComposition(composition);
                    } else {
                        if (!"primitives".equals(nextArg)) {
                            break;
                        }
                        addPrimitivesToComposition(composition);
                    }
                } else {
                    break;
                }
            }
            return composition.compose();
        }

        private void addOneShotToComposition(android.os.VibrationEffect.Composition composition) {
            boolean hasAmplitude = false;
            int delay = 0;
            getNextArgRequired();
            while (true) {
                java.lang.String nextOption = getNextOption();
                if (nextOption == null) {
                    break;
                }
                if ("-a".equals(nextOption)) {
                    hasAmplitude = true;
                } else if ("-w".equals(nextOption)) {
                    delay = java.lang.Integer.parseInt(getNextArgRequired());
                }
            }
            long duration = java.lang.Long.parseLong(getNextArgRequired());
            int amplitude = hasAmplitude ? java.lang.Integer.parseInt(getNextArgRequired()) : -1;
            composition.addOffDuration(java.time.Duration.ofMillis(delay));
            composition.addEffect(android.os.VibrationEffect.createOneShot(duration, amplitude));
        }

        private void addWaveformToComposition(android.os.VibrationEffect.Composition composition) {
            java.lang.String nextOption;
            java.time.Duration transitionDuration;
            int delay;
            java.lang.String nextOption2;
            java.time.Duration durationOfMillis;
            getNextArgRequired();
            int delay2 = 0;
            int repeat = -1;
            int repeat2 = 0;
            boolean hasFrequencies = false;
            boolean hasFrequencies2 = false;
            while (true) {
                java.lang.String nextOption3 = getNextOption();
                nextOption = nextOption3;
                if (nextOption3 == null) {
                    break;
                }
                if ("-a".equals(nextOption)) {
                    hasFrequencies2 = true;
                } else if ("-r".equals(nextOption)) {
                    repeat = java.lang.Integer.parseInt(getNextArgRequired());
                } else if ("-w".equals(nextOption)) {
                    delay2 = java.lang.Integer.parseInt(getNextArgRequired());
                } else if ("-f".equals(nextOption)) {
                    hasFrequencies = true;
                } else if ("-c".equals(nextOption)) {
                    repeat2 = 1;
                }
            }
            java.util.List<java.lang.Integer> durations = new java.util.ArrayList<>();
            java.util.List<java.lang.Float> amplitudes = new java.util.ArrayList<>();
            java.util.List<java.lang.Float> frequencies = new java.util.ArrayList<>();
            float nextAmplitude = 0.0f;
            while (true) {
                java.lang.String nextArg = peekNextArg();
                if (nextArg == null) {
                    break;
                }
                try {
                    durations.add(java.lang.Integer.valueOf(java.lang.Integer.parseInt(nextArg)));
                    getNextArgRequired();
                    if (hasFrequencies2) {
                        amplitudes.add(java.lang.Float.valueOf(java.lang.Float.parseFloat(getNextArgRequired()) / 255.0f));
                    } else {
                        amplitudes.add(java.lang.Float.valueOf(nextAmplitude));
                        nextAmplitude = 1.0f - nextAmplitude;
                    }
                    if (hasFrequencies) {
                        frequencies.add(java.lang.Float.valueOf(java.lang.Float.parseFloat(getNextArgRequired())));
                    }
                } catch (java.lang.NumberFormatException e) {
                }
            }
            composition.addOffDuration(java.time.Duration.ofMillis(delay2));
            android.os.VibrationEffect.WaveformBuilder waveform = android.os.VibrationEffect.startWaveform();
            int i = 0;
            while (i < durations.size()) {
                if (repeat2 != 0) {
                    transitionDuration = java.time.Duration.ofMillis(durations.get(i).intValue());
                } else {
                    transitionDuration = java.time.Duration.ZERO;
                }
                if (repeat2 != 0) {
                    durationOfMillis = java.time.Duration.ZERO;
                    delay = delay2;
                    nextOption2 = nextOption;
                } else {
                    delay = delay2;
                    nextOption2 = nextOption;
                    durationOfMillis = java.time.Duration.ofMillis(durations.get(i).intValue());
                }
                java.time.Duration sustainDuration = durationOfMillis;
                if (hasFrequencies) {
                    waveform.addTransition(transitionDuration, android.os.VibrationEffect.VibrationParameter.targetAmplitude(amplitudes.get(i).floatValue()), android.os.VibrationEffect.VibrationParameter.targetFrequency(frequencies.get(i).floatValue()));
                } else {
                    waveform.addTransition(transitionDuration, android.os.VibrationEffect.VibrationParameter.targetAmplitude(amplitudes.get(i).floatValue()));
                }
                if (!sustainDuration.isZero()) {
                    waveform.addSustain(sustainDuration);
                }
                if (i > 0 && i == repeat) {
                    composition.addEffect(waveform.build());
                    if (hasFrequencies) {
                        waveform = android.os.VibrationEffect.startWaveform(android.os.VibrationEffect.VibrationParameter.targetAmplitude(amplitudes.get(i).floatValue()), android.os.VibrationEffect.VibrationParameter.targetFrequency(frequencies.get(i).floatValue()));
                    } else {
                        waveform = android.os.VibrationEffect.startWaveform(android.os.VibrationEffect.VibrationParameter.targetAmplitude(amplitudes.get(i).floatValue()));
                    }
                }
                i++;
                nextOption = nextOption2;
                delay2 = delay;
            }
            if (repeat < 0) {
                composition.addEffect(waveform.build());
            } else {
                composition.repeatEffectIndefinitely(waveform.build());
            }
        }

        private void addPrebakedToComposition(android.os.VibrationEffect.Composition composition) {
            boolean shouldFallback = false;
            int delay = 0;
            getNextArgRequired();
            while (true) {
                java.lang.String nextOption = getNextOption();
                if (nextOption != null) {
                    if ("-b".equals(nextOption)) {
                        shouldFallback = true;
                    } else if ("-w".equals(nextOption)) {
                        delay = java.lang.Integer.parseInt(getNextArgRequired());
                    }
                } else {
                    int effectId = java.lang.Integer.parseInt(getNextArgRequired());
                    composition.addOffDuration(java.time.Duration.ofMillis(delay));
                    composition.addEffect(android.os.VibrationEffect.get(effectId, shouldFallback));
                    return;
                }
            }
        }

        private void addPrimitivesToComposition(android.os.VibrationEffect.Composition composition) {
            getNextArgRequired();
            while (true) {
                java.lang.String strPeekNextArg = peekNextArg();
                java.lang.String nextArg = strPeekNextArg;
                if (strPeekNextArg != null) {
                    int delay = 0;
                    if ("-w".equals(nextArg)) {
                        getNextArgRequired();
                        delay = java.lang.Integer.parseInt(getNextArgRequired());
                        nextArg = peekNextArg();
                    }
                    try {
                        composition.addPrimitive(java.lang.Integer.parseInt(nextArg), 1.0f, delay);
                        getNextArgRequired();
                    } catch (java.lang.NullPointerException | java.lang.NumberFormatException e) {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        private android.os.VibrationAttributes createVibrationAttributes(com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions commonOptions) {
            int flags = commonOptions.force ? 19 : 0;
            return new android.os.VibrationAttributes.Builder().setFlags(flags).setUsage(65).build();
        }

        private android.os.CombinedVibration parseXml(java.lang.String xml) {
            try {
                android.os.vibrator.persistence.ParsedVibration parsedVibration = android.os.vibrator.persistence.VibrationXmlParser.parseDocument(new java.io.StringReader(xml));
                if (parsedVibration == null) {
                    throw new java.lang.IllegalArgumentException("Error parsing vibration XML " + xml);
                }
                android.os.VibratorInfo combinedVibratorInfo = com.android.server.vibrator.VibratorManagerService.this.getCombinedVibratorInfo();
                if (combinedVibratorInfo == null) {
                    throw new java.lang.IllegalStateException("No combined vibrator info to parse vibration XML " + xml);
                }
                android.os.VibrationEffect effect = parsedVibration.resolve(combinedVibratorInfo);
                if (effect == null) {
                    throw new java.lang.IllegalArgumentException("Parsed vibration cannot be resolved for vibration XML " + xml);
                }
                return android.os.CombinedVibration.createParallel(effect);
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException("Error parsing vibration XML " + xml, e);
            }
        }

        private void maybeWaitOnVibration(com.android.server.vibrator.HalVibration vib, com.android.server.vibrator.VibratorManagerService.VibratorManagerShellCommand.CommonOptions commonOptions) {
            if (vib != null && !commonOptions.background) {
                try {
                    vib.waitForEnd();
                    com.android.server.vibrator.VibratorManagerService.this.mVibrationThread.waitForThreadIdle(((long) com.android.server.vibrator.VibratorManagerService.this.mVibrationSettings.getRampDownDuration()) + 500);
                } catch (java.lang.InterruptedException e) {
                }
            }
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            try {
                pw.println("Vibrator Manager commands:");
                pw.println("  help");
                pw.println("    Prints this help text.");
                pw.println("");
                pw.println("  list");
                pw.println("    Prints the id of device vibrators. This does not include any ");
                pw.println("    connected input device.");
                pw.println("  synced [options] <effect>...");
                pw.println("    Vibrates effect on all vibrators in sync.");
                pw.println("  combined [options] (-v <vibrator-id> <effect>...)...");
                pw.println("    Vibrates different effects on each vibrator in sync.");
                pw.println("  sequential [options] (-v <vibrator-id> <effect>...)...");
                pw.println("    Vibrates different effects on each vibrator in sequence.");
                pw.println("  xml [options] <xml>");
                pw.println("    Vibrates using combined vibration described in given XML string");
                pw.println("    on all vibrators in sync. The XML could be:");
                pw.println("        XML containing a single effect, or");
                pw.println("        A vibration select XML containing multiple effects.");
                pw.println("    Vibrates using combined vibration described in given XML string.");
                pw.println("    XML containing a single effect it runs on all vibrators in sync.");
                pw.println("  cancel");
                pw.println("    Cancels any active vibration");
                pw.println("  feedback [-f] [-d <description>] <constant>");
                pw.println("    Performs a haptic feedback with the given constant.");
                pw.println("    The force (-f) option enables the `always` configuration, which");
                pw.println("    plays the haptic irrespective of the vibration intensity settings");
                pw.println("");
                pw.println("Effect commands:");
                pw.println("  oneshot [-w delay] [-a] <duration> [<amplitude>]");
                pw.println("    Vibrates for duration milliseconds; ignored when device is on ");
                pw.println("    DND (Do Not Disturb) mode; touch feedback strength user setting ");
                pw.println("    will be used to scale amplitude.");
                pw.println("    If -w is provided, the effect will be played after the specified");
                pw.println("    wait time in milliseconds.");
                pw.println("    If -a is provided, the command accepts a second argument for ");
                pw.println("    amplitude, in a scale of 1-255.");
                pw.print("  waveform [-w delay] [-r index] [-a] [-f] [-c] ");
                pw.println("(<duration> [<amplitude>] [<frequency>])...");
                pw.println("    Vibrates for durations and amplitudes in list; ignored when ");
                pw.println("    device is on DND (Do Not Disturb) mode; touch feedback strength ");
                pw.println("    user setting will be used to scale amplitude.");
                pw.println("    If -w is provided, the effect will be played after the specified");
                pw.println("    wait time in milliseconds.");
                pw.println("    If -r is provided, the waveform loops back to the specified");
                pw.println("    index (e.g. 0 loops from the beginning)");
                pw.println("    If -a is provided, the command expects amplitude to follow each");
                pw.println("    duration; otherwise, it accepts durations only and alternates");
                pw.println("    off/on");
                pw.println("    If -f is provided, the command expects frequency to follow each");
                pw.println("    amplitude or duration; otherwise, it uses resonant frequency");
                pw.println("    If -c is provided, the waveform is continuous and will ramp");
                pw.println("    between values; otherwise each entry is a fixed step.");
                pw.println("    Duration is in milliseconds; amplitude is a scale of 1-255;");
                pw.println("    frequency is an absolute value in hertz;");
                pw.println("  prebaked [-w delay] [-b] <effect-id>");
                pw.println("    Vibrates with prebaked effect; ignored when device is on DND ");
                pw.println("    (Do Not Disturb) mode; touch feedback strength user setting ");
                pw.println("    will be used to scale amplitude.");
                pw.println("    If -w is provided, the effect will be played after the specified");
                pw.println("    wait time in milliseconds.");
                pw.println("    If -b is provided, the prebaked fallback effect will be played if");
                pw.println("    the device doesn't support the given effect-id.");
                pw.println("  primitives ([-w delay] <primitive-id>)...");
                pw.println("    Vibrates with a composed effect; ignored when device is on DND ");
                pw.println("    (Do Not Disturb) mode; touch feedback strength user setting ");
                pw.println("    will be used to scale primitive intensities.");
                pw.println("    If -w is provided, the next primitive will be played after the ");
                pw.println("    specified wait time in milliseconds.");
                pw.println("");
                pw.println("Common Options:");
                pw.println("  -f");
                pw.println("    Force. Ignore Do Not Disturb setting.");
                pw.println("  -B");
                pw.println("    Run in the background; without this option the shell cmd will");
                pw.println("    block until the vibration has completed.");
                pw.println("  -d <description>");
                pw.println("    Add description to the vibration.");
                pw.println("");
                if (pw != null) {
                    pw.close();
                }
            } catch (java.lang.Throwable th) {
                if (pw != null) {
                    try {
                        pw.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    public com.android.server.vibrator.IVibratorManagerServiceWrapper getWrapper() {
        return this.mVibratorManagerServiceWrapper;
    }

    private class VibratorManagerServiceWrapper implements com.android.server.vibrator.IVibratorManagerServiceWrapper {
        private com.android.server.vibrator.IVibratorManagerServiceExt mVibratorManagerServiceExt;

        private VibratorManagerServiceWrapper() {
            this.mVibratorManagerServiceExt = (com.android.server.vibrator.IVibratorManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.vibrator.IVibratorManagerServiceExt.class).base(com.android.server.vibrator.VibratorManagerService.this).create();
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public com.android.server.vibrator.IVibratorManagerServiceExt getExtImpl() {
            return this.mVibratorManagerServiceExt;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public void setDebuggable(boolean enable) {
            com.android.server.vibrator.VibratorManagerService.DEBUG = enable;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public boolean isDebuggable() {
            return com.android.server.vibrator.VibratorManagerService.DEBUG;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public com.android.server.vibrator.VibrationStepConductor getCurrentVibrationStepConductor() {
            return com.android.server.vibrator.VibratorManagerService.this.mCurrentVibration;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public android.util.SparseArray<com.android.server.vibrator.VibratorController> getVibrators() {
            return com.android.server.vibrator.VibratorManagerService.this.mVibrators;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public com.android.server.vibrator.InputDeviceDelegate getInputDeviceDelegate() {
            return com.android.server.vibrator.VibratorManagerService.this.mInputDeviceDelegate;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public java.lang.Object getSyncLock() {
            return com.android.server.vibrator.VibratorManagerService.this.mLock;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public android.os.PowerManager.WakeLock getVibratorPartialWakeLock() {
            return com.android.server.vibrator.VibratorManagerService.this.mWakeLock;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public android.os.Handler getHandler() {
            return com.android.server.vibrator.VibratorManagerService.this.mHandler;
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public void noteVibratorOnExtImpl(int uid, long duration) {
            com.android.server.vibrator.VibratorManagerService.this.mVibrationThreadCallbacks.noteVibratorOn(uid, duration);
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public void noteVibratorOffExtImpl(int uid) {
            com.android.server.vibrator.VibratorManagerService.this.mVibrationThreadCallbacks.noteVibratorOff(uid);
        }

        @Override // com.android.server.vibrator.IVibratorManagerServiceWrapper
        public com.android.server.vibrator.VibrationSettings getVibrationSettings() {
            return com.android.server.vibrator.VibratorManagerService.this.mVibrationSettings;
        }
    }

    public void updateVibrationAmplitude(int uid, java.lang.String opPkg, float amplitudeRatio) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.VIBRATE", "updateVibrationAmplitude");
        getWrapper().getExtImpl().updateVibrationAmplitude(uid, opPkg, amplitudeRatio);
    }

    public boolean blockVibrationForApplication(java.lang.String opPkg, boolean block, android.os.IBinder token) {
        boolean zBlockVibrationForApplicationLocked;
        this.mContext.enforceCallingOrSelfPermission("android.permission.VIBRATE", "blockVibrationForApplication");
        this.mContext.enforceCallingOrSelfPermission("com.oplus.permission.safe.CAMERA", "blockVibrationForApplication");
        synchronized (this.mLock) {
            zBlockVibrationForApplicationLocked = getWrapper().getExtImpl().blockVibrationForApplicationLocked(opPkg, block, token);
        }
        return zBlockVibrationForApplicationLocked;
    }

    public int getWaveformIndex(int effectId) {
        return getWrapper().getExtImpl().getWaveformIndex(effectId);
    }

    public int getEffectDuration(int effectId) {
        return getWrapper().getExtImpl().getEffectDuration(effectId);
    }

    public int getEffectType(int effectId) {
        return getWrapper().getExtImpl().getEffectType(effectId);
    }

    public int getRingtoneEffectId(java.lang.String ringtonePath) {
        return getWrapper().getExtImpl().getRingtoneEffectId(ringtonePath);
    }
}
