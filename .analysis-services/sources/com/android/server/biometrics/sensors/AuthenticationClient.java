package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class AuthenticationClient<T, O extends android.hardware.biometrics.AuthenticateOptions> extends com.android.server.biometrics.sensors.AcquisitionClient<T> implements com.android.server.biometrics.sensors.AuthenticationConsumer {
    public static final int STATE_NEW = 0;
    public static final int STATE_STARTED = 1;
    public static final int STATE_STARTED_PAUSED = 2;
    public static final int STATE_STARTED_PAUSED_ATTEMPTED = 3;
    public static final int STATE_STOPPED = 4;
    private static final java.lang.String TAG = "Biometrics/AuthenticationClient";
    private final android.app.ActivityTaskManager mActivityTaskManager;
    private final boolean mAllowBackgroundAuthentication;
    private boolean mAuthAttempted;
    private boolean mAuthSuccess;
    private final android.hardware.biometrics.BiometricManager mBiometricManager;
    private final boolean mIsRestricted;
    private final boolean mIsStrongBiometric;
    private final com.android.server.biometrics.sensors.LockoutTracker mLockoutTracker;
    protected final long mOperationId;
    private final O mOptions;
    private final boolean mRequireConfirmation;
    private final int mSensorStrength;
    private final boolean mShouldUseLockoutTracker;
    private long mStartTimeMs;
    protected int mState;
    private final android.app.TaskStackListener mTaskStackListener;

    @interface State {
    }

    protected abstract void handleLifecycleAfterAuth(boolean z);

    public abstract boolean wasUserDetected();

    public AuthenticationClient(android.content.Context context, java.util.function.Supplier<T> lazyDaemon, android.os.IBinder token, com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener, long operationId, boolean restricted, O options, int cookie, boolean requireConfirmation, com.android.server.biometrics.log.BiometricLogger biometricLogger, com.android.server.biometrics.log.BiometricContext biometricContext, boolean isStrongBiometric, android.app.TaskStackListener taskStackListener, com.android.server.biometrics.sensors.LockoutTracker lockoutTracker, boolean allowBackgroundAuthentication, boolean shouldVibrate, int sensorStrength) {
        super(context, lazyDaemon, token, listener, options.getUserId(), options.getOpPackageName(), cookie, options.getSensorId(), shouldVibrate, biometricLogger, biometricContext);
        this.mState = 0;
        this.mAuthSuccess = false;
        this.mIsStrongBiometric = isStrongBiometric;
        this.mOperationId = operationId;
        this.mRequireConfirmation = requireConfirmation;
        this.mActivityTaskManager = getActivityTaskManager();
        this.mBiometricManager = (android.hardware.biometrics.BiometricManager) context.getSystemService(android.hardware.biometrics.BiometricManager.class);
        this.mTaskStackListener = taskStackListener;
        this.mLockoutTracker = lockoutTracker;
        this.mIsRestricted = restricted;
        this.mAllowBackgroundAuthentication = allowBackgroundAuthentication;
        this.mShouldUseLockoutTracker = lockoutTracker != null;
        this.mSensorStrength = sensorStrength;
        this.mOptions = options;
    }

    public int handleFailedAttempt(int userId) {
        if (this.mLockoutTracker != null) {
            this.mLockoutTracker.addFailedAttemptForUser(getTargetUserId());
        }
        int lockoutMode = getLockoutTracker().getLockoutModeForUser(userId);
        com.android.server.biometrics.sensors.PerformanceTracker performanceTracker = com.android.server.biometrics.sensors.PerformanceTracker.getInstanceForSensorId(getSensorId());
        if (lockoutMode == 2) {
            performanceTracker.incrementPermanentLockoutForUser(userId);
        } else if (lockoutMode == 1) {
            performanceTracker.incrementTimedLockoutForUser(userId);
        }
        return lockoutMode;
    }

    protected long getStartTimeMs() {
        return this.mStartTimeMs;
    }

    protected android.app.ActivityTaskManager getActivityTaskManager() {
        return android.app.ActivityTaskManager.getInstance();
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor, android.os.IBinder.DeathRecipient
    public void binderDied() {
        boolean clearListener = !isBiometricPrompt();
        binderDiedInternal(clearListener);
    }

    public long getOperationId() {
        return this.mOperationId;
    }

    public boolean isRestricted() {
        return this.mIsRestricted;
    }

    public boolean isKeyguard() {
        return com.android.server.biometrics.Utils.isKeyguard(getContext(), getOwnerString());
    }

    private boolean isSettings() {
        return com.android.server.biometrics.Utils.isSettings(getContext(), getOwnerString());
    }

    protected O getOptions() {
        return this.mOptions;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    protected boolean isCryptoOperation() {
        return this.mOperationId != 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v10 */
    /* JADX WARN: Type inference failed for: r8v11 */
    /* JADX WARN: Type inference failed for: r8v13 */
    /* JADX WARN: Type inference failed for: r8v14 */
    /* JADX WARN: Type inference failed for: r8v15 */
    /* JADX WARN: Type inference failed for: r8v16 */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v5, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v6 */
    /* JADX WARN: Type inference failed for: r8v7 */
    /* JADX WARN: Type inference failed for: r8v8 */
    @Override // com.android.server.biometrics.sensors.AuthenticationConsumer
    public void onAuthenticated(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, boolean z, java.util.ArrayList<java.lang.Byte> arrayList) {
        boolean z2;
        getLogger().logOnAuthenticated(getContext(), getOperationContext(), z, this.mRequireConfirmation, getTargetUserId(), isBiometricPrompt());
        com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener = getListener();
        android.util.Slog.v(TAG, "onAuthenticated(" + z + "), ID:" + identifier.getBiometricId() + ", Owner: " + getOwnerString() + ", isBP: " + isBiometricPrompt() + ", listener: " + listener + ", requireConfirmation: " + this.mRequireConfirmation + ", user: " + getTargetUserId() + ", clientMonitor: " + this);
        com.android.server.biometrics.sensors.PerformanceTracker instanceForSensorId = com.android.server.biometrics.sensors.PerformanceTracker.getInstanceForSensorId(getSensorId());
        if (isCryptoOperation()) {
            instanceForSensorId.incrementCryptoAuthForUser(getTargetUserId(), z);
        } else if (instanceForSensorId != null) {
            instanceForSensorId.incrementAuthForUser(getTargetUserId(), z);
        }
        if (this.mAllowBackgroundAuthentication) {
            android.util.Slog.w(TAG, "Allowing background authentication, this is allowed only for platform or test invocations");
        }
        boolean zIsBackground = false;
        if (!this.mAllowBackgroundAuthentication && z && !com.android.server.biometrics.Utils.isKeyguard(getContext(), getOwnerString()) && !com.android.server.biometrics.Utils.isSystem(getContext(), getOwnerString())) {
            zIsBackground = com.android.server.biometrics.Utils.isBackground(getOwnerString());
        }
        ?? r8 = 1397638484;
        ?? r82 = 1397638484;
        if (zIsBackground) {
            android.util.Slog.e(TAG, "Failing possible background authentication");
            android.content.pm.ApplicationInfo applicationInfo = getContext().getApplicationInfo();
            android.util.EventLog.writeEvent(1397638484, "159249069", java.lang.Integer.valueOf(applicationInfo != null ? applicationInfo.uid : -1), "Attempted background authentication");
            z2 = false;
        } else {
            z2 = z;
        }
        if (z2) {
            if (zIsBackground) {
                android.content.pm.ApplicationInfo applicationInfo2 = getContext().getApplicationInfo();
                android.util.EventLog.writeEvent(1397638484, "159249069", java.lang.Integer.valueOf(applicationInfo2 != null ? applicationInfo2.uid : -1), "Successful background authentication!");
            }
            this.mAuthSuccess = true;
            markAlreadyDone();
            if (this.mTaskStackListener != null) {
                this.mActivityTaskManager.unregisterTaskStackListener(this.mTaskStackListener);
            }
            byte[] bArr = new byte[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                bArr[i] = arrayList.get(i).byteValue();
            }
            if (isBiometricPrompt()) {
                android.util.Slog.d(TAG, "Skipping addAuthToken");
            } else {
                if (this.mIsStrongBiometric) {
                    android.hardware.biometrics.BiometricManager biometricManager = this.mBiometricManager;
                    android.os.IBinder token = getToken();
                    java.lang.String opPackageName = getContext().getOpPackageName();
                    biometricManager.resetLockoutTimeBound(token, opPackageName, getSensorId(), getTargetUserId(), bArr);
                    r82 = opPackageName;
                }
                int iAddAuthToken = android.security.KeyStoreAuthorization.getInstance().addAuthToken(bArr);
                if (iAddAuthToken != 0) {
                    android.util.Slog.d(TAG, "Error adding auth token : " + iAddAuthToken);
                    r8 = r82;
                } else {
                    android.util.Slog.d(TAG, "addAuthToken succeeded");
                    r8 = r82;
                }
            }
            try {
                try {
                    if (!this.mIsRestricted) {
                        r8 = "Unable to notify listener";
                        listener.onAuthenticationSucceeded(getSensorId(), identifier, bArr, getTargetUserId(), this.mIsStrongBiometric);
                    } else {
                        r8 = "Unable to notify listener";
                        listener.onAuthenticationSucceeded(getSensorId(), null, bArr, getTargetUserId(), this.mIsStrongBiometric);
                    }
                    if (this.mShouldVibrate) {
                        vibrateSuccess();
                    }
                } catch (android.os.RemoteException e) {
                    e = e;
                    android.util.Slog.e(TAG, (java.lang.String) r8, e);
                    this.mCallback.onClientFinished(this, false);
                    return;
                }
            } catch (android.os.RemoteException e2) {
                e = e2;
                r8 = "Unable to notify listener";
            }
        } else if (zIsBackground) {
            android.util.Slog.e(TAG, "Sending cancel to client(Due to background auth)");
            if (this.mTaskStackListener != null) {
                this.mActivityTaskManager.unregisterTaskStackListener(this.mTaskStackListener);
            }
            sendCancelOnly(getListener());
            this.mCallback.onClientFinished(this, false);
        } else {
            if (this.mShouldUseLockoutTracker && handleFailedAttempt(getTargetUserId()) != 0) {
                markAlreadyDone();
            }
            try {
                listener.onAuthenticationFailed(getSensorId());
                if (this.mShouldVibrate) {
                    vibrateError();
                }
            } catch (android.os.RemoteException e3) {
                android.util.Slog.e(TAG, "Unable to notify listener", e3);
                this.mCallback.onClientFinished(this, false);
                return;
            }
        }
        handleLifecycleAfterAuth(z2);
    }

    private void sendCancelOnly(com.android.server.biometrics.sensors.ClientMonitorCallbackConverter listener) {
        if (listener == null) {
            android.util.Slog.e(TAG, "Unable to sendAuthenticationCanceled, listener null");
            return;
        }
        try {
            listener.onError(getSensorId(), getCookie(), 5, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception", e);
        }
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient
    public void onAcquired(int acquiredInfo, int vendorCode) {
        super.onAcquired(acquiredInfo, vendorCode);
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.ErrorConsumer
    public void onError(int errorCode, int vendorCode) {
        super.onError(errorCode, vendorCode);
        this.mState = 4;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public void start(com.android.server.biometrics.sensors.ClientMonitorCallback callback) {
        int lockoutMode;
        int errorCode;
        super.start(callback);
        if (this.mShouldUseLockoutTracker) {
            lockoutMode = this.mLockoutTracker.getLockoutModeForUser(getTargetUserId());
        } else {
            lockoutMode = getBiometricContext().getAuthSessionCoordinator().getLockoutStateFor(getTargetUserId(), this.mSensorStrength);
        }
        if (lockoutMode != 0) {
            android.util.Slog.v(TAG, "In lockout mode(" + lockoutMode + ") ; disallowing authentication");
            if (lockoutMode == 1) {
                errorCode = 7;
            } else {
                errorCode = 9;
            }
            onError(errorCode, 0);
            return;
        }
        if (this.mTaskStackListener != null) {
            this.mActivityTaskManager.registerTaskStackListener(this.mTaskStackListener);
        }
        android.util.Slog.d(TAG, "Requesting auth for " + getOwnerString());
        this.mStartTimeMs = java.lang.System.currentTimeMillis();
        this.mAuthAttempted = true;
        startHalOperation();
    }

    @Override // com.android.server.biometrics.sensors.AcquisitionClient, com.android.server.biometrics.sensors.BaseClientMonitor
    public void cancel() {
        super.cancel();
        if (this.mTaskStackListener != null) {
            this.mActivityTaskManager.unregisterTaskStackListener(this.mTaskStackListener);
        }
    }

    public int getState() {
        return this.mState;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public int getProtoEnum() {
        return 3;
    }

    @Override // com.android.server.biometrics.sensors.BaseClientMonitor
    public boolean interruptsPrecedingClients() {
        return true;
    }

    public boolean wasAuthAttempted() {
        return this.mAuthAttempted;
    }

    public boolean wasAuthSuccessful() {
        return this.mAuthSuccess;
    }

    protected int getSensorStrength() {
        return this.mSensorStrength;
    }

    protected com.android.server.biometrics.sensors.LockoutTracker getLockoutTracker() {
        return this.mLockoutTracker;
    }

    protected int getRequestReason() {
        if (isKeyguard()) {
            return 4;
        }
        if (isBiometricPrompt()) {
            return 3;
        }
        if (isSettings()) {
            return 6;
        }
        return 5;
    }
}
