package com.android.server.adaptiveauth;

/* JADX INFO: loaded from: classes.dex */
public class AdaptiveAuthService extends com.android.server.SystemService {
    private static final int AUTH_FAILURE = 0;
    private static final int AUTH_SUCCESS = 1;
    private static final boolean DEBUG;
    static final int MAX_ALLOWED_FAILED_AUTH_ATTEMPTS = 15;
    private static final int MSG_REPORT_BIOMETRIC_AUTH_ATTEMPT = 2;
    private static final int MSG_REPORT_PRIMARY_AUTH_ATTEMPT = 1;
    private static final java.lang.String TAG = "AdaptiveAuthService";
    private static final int TYPE_BIOMETRIC_AUTH = 1;
    private static final int TYPE_PRIMARY_AUTH = 0;
    private final android.app.ActivityManagerInternal mAmInternal;
    private final android.hardware.biometrics.AuthenticationStateListener mAuthenticationStateListener;
    private final android.hardware.biometrics.BiometricManager mBiometricManager;
    final android.util.SparseIntArray mFailedAttemptsForUser;
    private final android.os.Handler mHandler;
    private final android.app.KeyguardManager mKeyguardManager;
    private final android.util.SparseLongArray mLastLockedTimestamp;
    private final com.android.internal.widget.LockPatternUtils mLockPatternUtils;
    private final com.android.internal.widget.LockSettingsInternal mLockSettings;
    private final com.android.internal.widget.LockSettingsStateListener mLockSettingsStateListener;
    private final android.os.PowerManager mPowerManager;
    private android.hardware.IRedLoggerExt mRedLoggerExt;
    private final com.android.server.pm.UserManagerInternal mUserManager;
    private final com.android.server.wm.WindowManagerInternal mWindowManager;

    static {
        DEBUG = android.os.Build.IS_DEBUGGABLE && android.util.Log.isLoggable(TAG, 3);
    }

    public AdaptiveAuthService(android.content.Context context) {
        this(context, new com.android.internal.widget.LockPatternUtils(context));
    }

    public AdaptiveAuthService(android.content.Context context, com.android.internal.widget.LockPatternUtils lockPatternUtils) {
        super(context);
        this.mRedLoggerExt = (android.hardware.IRedLoggerExt) system.ext.loader.core.ExtLoader.type(android.hardware.IRedLoggerExt.class).create();
        this.mFailedAttemptsForUser = new android.util.SparseIntArray();
        this.mLastLockedTimestamp = new android.util.SparseLongArray();
        this.mLockSettingsStateListener = new com.android.server.adaptiveauth.AdaptiveAuthService.AnonymousClass1();
        this.mAuthenticationStateListener = new com.android.server.adaptiveauth.AdaptiveAuthService.AnonymousClass2();
        this.mHandler = new android.os.Handler(android.os.Looper.getMainLooper()) { // from class: com.android.server.adaptiveauth.AdaptiveAuthService.3
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        com.android.server.adaptiveauth.AdaptiveAuthService.this.handleReportPrimaryAuthAttempt(msg.arg1 != 0, msg.arg2);
                        break;
                    case 2:
                        com.android.server.adaptiveauth.AdaptiveAuthService.this.handleReportBiometricAuthAttempt(msg.arg1 != 0, msg.arg2);
                        break;
                }
            }
        };
        this.mLockPatternUtils = lockPatternUtils;
        this.mLockSettings = (com.android.internal.widget.LockSettingsInternal) java.util.Objects.requireNonNull((com.android.internal.widget.LockSettingsInternal) com.android.server.LocalServices.getService(com.android.internal.widget.LockSettingsInternal.class));
        this.mBiometricManager = (android.hardware.biometrics.BiometricManager) java.util.Objects.requireNonNull((android.hardware.biometrics.BiometricManager) context.getSystemService(android.hardware.biometrics.BiometricManager.class));
        this.mKeyguardManager = (android.app.KeyguardManager) java.util.Objects.requireNonNull((android.app.KeyguardManager) context.getSystemService(android.app.KeyguardManager.class));
        this.mPowerManager = (android.os.PowerManager) java.util.Objects.requireNonNull((android.os.PowerManager) context.getSystemService(android.os.PowerManager.class));
        this.mWindowManager = (com.android.server.wm.WindowManagerInternal) java.util.Objects.requireNonNull((com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class));
        this.mUserManager = (com.android.server.pm.UserManagerInternal) java.util.Objects.requireNonNull((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class));
        this.mAmInternal = (android.app.ActivityManagerInternal) java.util.Objects.requireNonNull((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class));
    }

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            init();
        }
    }

    void init() {
        this.mLockSettings.registerLockSettingsStateListener(this.mLockSettingsStateListener);
        this.mBiometricManager.registerAuthenticationStateListener(this.mAuthenticationStateListener);
    }

    /* JADX INFO: renamed from: com.android.server.adaptiveauth.AdaptiveAuthService$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.internal.widget.LockSettingsStateListener {
        AnonymousClass1() {
        }

        public void onAuthenticationSucceeded(final int userId) {
            if (com.android.server.adaptiveauth.AdaptiveAuthService.DEBUG) {
                android.util.Slog.d(com.android.server.adaptiveauth.AdaptiveAuthService.TAG, "LockSettingsStateListener#onAuthenticationSucceeded");
            }
            com.android.server.adaptiveauth.AdaptiveAuthService.this.mHandler.obtainMessage(1, 1, userId).sendToTarget();
            if (com.android.server.adaptiveauth.AdaptiveAuthService.this.mRedLoggerExt != null) {
                com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.adaptiveauth.AdaptiveAuthService$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onAuthenticationSucceeded$0(userId);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticationSucceeded$0(int userId) {
            com.android.server.adaptiveauth.AdaptiveAuthService.this.mRedLoggerExt.saveREDLog("PASSWORD", userId, "auth", 1);
        }

        public void onAuthenticationFailed(final int userId) {
            android.util.Slog.i(com.android.server.adaptiveauth.AdaptiveAuthService.TAG, "LockSettingsStateListener#onAuthenticationFailed");
            com.android.server.adaptiveauth.AdaptiveAuthService.this.mHandler.obtainMessage(1, 0, userId).sendToTarget();
            if (com.android.server.adaptiveauth.AdaptiveAuthService.this.mRedLoggerExt != null) {
                com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.adaptiveauth.AdaptiveAuthService$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onAuthenticationFailed$1(userId);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticationFailed$1(int userId) {
            com.android.server.adaptiveauth.AdaptiveAuthService.this.mRedLoggerExt.saveREDLog("PASSWORD", userId, "auth", 0);
        }
    }

    /* JADX INFO: renamed from: com.android.server.adaptiveauth.AdaptiveAuthService$2, reason: invalid class name */
    class AnonymousClass2 extends android.hardware.biometrics.AuthenticationStateListener.Stub {
        AnonymousClass2() {
        }

        public void onAuthenticationAcquired(android.hardware.biometrics.events.AuthenticationAcquiredInfo authInfo) {
        }

        public void onAuthenticationError(android.hardware.biometrics.events.AuthenticationErrorInfo authInfo) {
        }

        public void onAuthenticationFailed(final android.hardware.biometrics.events.AuthenticationFailedInfo authInfo) {
            android.util.Slog.i(com.android.server.adaptiveauth.AdaptiveAuthService.TAG, "AuthenticationStateListener#onAuthenticationFailed");
            com.android.server.adaptiveauth.AdaptiveAuthService.this.mHandler.obtainMessage(2, 0, authInfo.getUserId()).sendToTarget();
            if (com.android.server.adaptiveauth.AdaptiveAuthService.this.mRedLoggerExt != null) {
                com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.adaptiveauth.AdaptiveAuthService$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onAuthenticationFailed$0(authInfo);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticationFailed$0(android.hardware.biometrics.events.AuthenticationFailedInfo authInfo) {
            com.android.server.adaptiveauth.AdaptiveAuthService.this.mRedLoggerExt.saveREDLog(authInfo.getBiometricSourceType().toString(), authInfo.getUserId(), "auth", 0);
        }

        public void onAuthenticationHelp(android.hardware.biometrics.events.AuthenticationHelpInfo authInfo) {
        }

        public void onAuthenticationStarted(android.hardware.biometrics.events.AuthenticationStartedInfo authInfo) {
        }

        public void onAuthenticationStopped(android.hardware.biometrics.events.AuthenticationStoppedInfo authInfo) {
        }

        public void onAuthenticationSucceeded(final android.hardware.biometrics.events.AuthenticationSucceededInfo authInfo) {
            if (com.android.server.adaptiveauth.AdaptiveAuthService.DEBUG) {
                android.util.Slog.d(com.android.server.adaptiveauth.AdaptiveAuthService.TAG, "AuthenticationStateListener#onAuthenticationSucceeded");
            }
            com.android.server.adaptiveauth.AdaptiveAuthService.this.mHandler.obtainMessage(2, 1, authInfo.getUserId()).sendToTarget();
            if (com.android.server.adaptiveauth.AdaptiveAuthService.this.mRedLoggerExt != null) {
                com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.adaptiveauth.AdaptiveAuthService$2$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onAuthenticationSucceeded$1(authInfo);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAuthenticationSucceeded$1(android.hardware.biometrics.events.AuthenticationSucceededInfo authInfo) {
            com.android.server.adaptiveauth.AdaptiveAuthService.this.mRedLoggerExt.saveREDLog(authInfo.getBiometricSourceType().toString(), authInfo.getUserId(), "auth", 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleReportPrimaryAuthAttempt(boolean success, int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "handleReportPrimaryAuthAttempt: success=" + success + ", userId=" + userId);
        }
        reportAuthAttempt(0, success, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleReportBiometricAuthAttempt(boolean success, int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "handleReportBiometricAuthAttempt: success=" + success + ", userId=" + userId);
        }
        reportAuthAttempt(1, success, userId);
    }

    private void reportAuthAttempt(int authType, boolean success, int userId) {
        if (getContext().getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            return;
        }
        if (success) {
            this.mFailedAttemptsForUser.delete(userId);
            if (this.mLastLockedTimestamp.indexOfKey(userId) >= 0) {
                long lastLockedTime = this.mLastLockedTimestamp.get(userId);
                collectTimeElapsedSinceLastLocked(lastLockedTime, android.os.SystemClock.elapsedRealtime(), authType);
                this.mLastLockedTimestamp.delete(userId);
                return;
            }
            return;
        }
        int numFailedAttempts = this.mFailedAttemptsForUser.get(userId, 0) + 1;
        android.util.Slog.i(TAG, "reportAuthAttempt: numFailedAttempts=" + numFailedAttempts + ", userId=" + userId);
        this.mFailedAttemptsForUser.put(userId, numFailedAttempts);
        if (this.mKeyguardManager.isDeviceLocked(userId) && this.mKeyguardManager.isKeyguardLocked()) {
            android.util.Slog.d(TAG, "Not locking the device because the device is already locked.");
            return;
        }
        if (numFailedAttempts < 15) {
            android.util.Slog.d(TAG, "Not locking the device because the number of failed attempts is below the threshold.");
        } else if (userId != this.mAmInternal.getCurrentUserId()) {
            android.util.Slog.d(TAG, "skip lockDevice for system clone.");
        } else {
            lockDevice(userId);
        }
    }

    private static void collectTimeElapsedSinceLastLocked(long lastLockedTime, long authTime, int authType) {
        int unlockType;
        switch (authType) {
            case 0:
                unlockType = 1;
                break;
            case 1:
                unlockType = 2;
                break;
            default:
                unlockType = 0;
                break;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "collectTimeElapsedSinceLastLockedForUser: lastLockedTime=" + lastLockedTime + ", authTime=" + authTime + ", unlockType=" + unlockType);
        }
        if (lastLockedTime > authTime) {
            return;
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.ADAPTIVE_AUTH_UNLOCK_AFTER_LOCK_REPORTED, lastLockedTime, authTime, unlockType);
    }

    private void lockDevice(int userId) {
        this.mLockPatternUtils.requireStrongAuth(512, userId);
        int parentUserId = this.mUserManager.getProfileParentId(userId);
        android.util.Slog.i(TAG, "lockDevice: userId=" + userId + ", parentUserId=" + parentUserId);
        if (parentUserId != userId) {
            this.mLockPatternUtils.requireStrongAuth(512, parentUserId);
        }
        this.mPowerManager.goToSleep(android.os.SystemClock.uptimeMillis());
        this.mWindowManager.lockNow();
        this.mLastLockedTimestamp.put(userId, android.os.SystemClock.elapsedRealtime());
    }
}
