package com.android.server.biometrics.sensors.fingerprint.hidl;

/* JADX INFO: loaded from: classes.dex */
public class LockoutFrameworkImpl implements com.android.server.biometrics.sensors.LockoutTracker {
    private static final java.lang.String ACTION_LOCKOUT_RESET = "com.android.server.biometrics.sensors.fingerprint.ACTION_LOCKOUT_RESET";
    private static final long FAIL_LOCKOUT_TIMEOUT_MS = 30000;
    private static final java.lang.String KEY_LOCKOUT_RESET_USER = "lockout_reset_user";
    private static final int MAX_FAILED_ATTEMPTS_LOCKOUT_PERMANENT = 20;
    private static final int MAX_FAILED_ATTEMPTS_LOCKOUT_TIMED = 5;
    private static final java.lang.String TAG = "LockoutFrameworkImpl";
    private static com.android.server.biometrics.sensors.fingerprint.IOplusFingerUtilsExt mOplusFingerUtilsExt = (com.android.server.biometrics.sensors.fingerprint.IOplusFingerUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.IOplusFingerUtilsExt.class).create();
    private final android.app.AlarmManager mAlarmManager;
    private boolean mCancelLockoutAlarm;
    private final android.util.SparseIntArray mFailedAttempts;
    private final android.os.Handler mHandler;
    private final com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.LockoutReceiver mLockoutReceiver;
    private final com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.LockoutResetCallback mLockoutResetCallback;
    private final java.util.function.Function<java.lang.Integer, android.app.PendingIntent> mLockoutResetIntent;
    private boolean mSetLockoutAlarm;
    private final android.util.SparseBooleanArray mTimedLockoutCleared;

    public interface LockoutResetCallback {
        void onLockoutReset(int i);
    }

    private final class LockoutReceiver extends android.content.BroadcastReceiver {
        private LockoutReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            android.util.Slog.v(com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.TAG, "Resetting lockout: " + intent.getAction());
            if (com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.ACTION_LOCKOUT_RESET.equals(intent.getAction())) {
                int user = intent.getIntExtra(com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.KEY_LOCKOUT_RESET_USER, 0);
                com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.this.mCancelLockoutAlarm = true;
                com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.this.cancelLockoutResetForUser(user);
                com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.this.resetFailedAttemptsForUser(false, user);
            }
        }
    }

    public LockoutFrameworkImpl(final android.content.Context context, com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.LockoutResetCallback lockoutResetCallback) {
        this(context, lockoutResetCallback, new java.util.function.Function() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                java.lang.Integer num = (java.lang.Integer) obj;
                return android.app.PendingIntent.getBroadcast(context, num.intValue(), new android.content.Intent(com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.ACTION_LOCKOUT_RESET).putExtra(com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.KEY_LOCKOUT_RESET_USER, num), android.hardware.audio.common.V2_0.AudioFormat.DTS_HD);
            }
        }, null);
    }

    public LockoutFrameworkImpl(final android.content.Context context, com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.LockoutResetCallback lockoutResetCallback, android.os.Handler handler) {
        this(context, lockoutResetCallback, new java.util.function.Function() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                java.lang.Integer num = (java.lang.Integer) obj;
                return android.app.PendingIntent.getBroadcast(context, num.intValue(), new android.content.Intent(com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.ACTION_LOCKOUT_RESET).putExtra(com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.KEY_LOCKOUT_RESET_USER, num), android.hardware.audio.common.V2_0.AudioFormat.DTS_HD);
            }
        }, handler);
    }

    LockoutFrameworkImpl(android.content.Context context, com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.LockoutResetCallback lockoutResetCallback, java.util.function.Function<java.lang.Integer, android.app.PendingIntent> lockoutResetIntent, android.os.Handler handler) {
        this.mSetLockoutAlarm = false;
        this.mCancelLockoutAlarm = false;
        this.mLockoutResetCallback = lockoutResetCallback;
        this.mTimedLockoutCleared = new android.util.SparseBooleanArray();
        this.mFailedAttempts = new android.util.SparseIntArray();
        this.mAlarmManager = (android.app.AlarmManager) context.getSystemService(android.app.AlarmManager.class);
        this.mLockoutReceiver = new com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl.LockoutReceiver();
        this.mHandler = handler == null ? new android.os.Handler(android.os.Looper.getMainLooper()) : handler;
        this.mLockoutResetIntent = lockoutResetIntent;
        context.registerReceiver(this.mLockoutReceiver, new android.content.IntentFilter(ACTION_LOCKOUT_RESET), "android.permission.RESET_FINGERPRINT_LOCKOUT", null, 2);
    }

    @Override // com.android.server.biometrics.sensors.LockoutTracker
    public void resetFailedAttemptsForUser(boolean clearAttemptCounter, int userId) {
        if (getLockoutModeForUser(userId) != 0) {
            android.util.Slog.v(TAG, "Reset biometric lockout for user: " + userId + ", clearAttemptCounter: " + clearAttemptCounter);
        }
        if (clearAttemptCounter) {
            this.mFailedAttempts.put(userId, 0);
        }
        this.mTimedLockoutCleared.put(userId, true);
        if (this.mSetLockoutAlarm && !this.mCancelLockoutAlarm) {
            cancelLockoutResetForUser(userId);
        }
        this.mCancelLockoutAlarm = false;
        this.mSetLockoutAlarm = false;
        this.mLockoutResetCallback.onLockoutReset(userId);
        mOplusFingerUtilsExt.notifyResetLockoutAttemptDeadline(0L, userId);
    }

    @Override // com.android.server.biometrics.sensors.LockoutTracker
    public void addFailedAttemptForUser(int userId) {
        this.mFailedAttempts.put(userId, this.mFailedAttempts.get(userId, 0) + 1);
        this.mTimedLockoutCleared.put(userId, false);
        if (getLockoutModeForUser(userId) != 0) {
            scheduleLockoutResetForUser(userId);
        }
    }

    @Override // com.android.server.biometrics.sensors.LockoutTracker
    public int getLockoutModeForUser(int userId) {
        int failedAttempts = this.mFailedAttempts.get(userId, 0);
        if (failedAttempts >= 20) {
            return 2;
        }
        return (failedAttempts <= 0 || this.mTimedLockoutCleared.get(userId, false) || failedAttempts % 5 != 0) ? 0 : 1;
    }

    @Override // com.android.server.biometrics.sensors.LockoutTracker
    public void setLockoutModeForUser(int userId, int mode) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelLockoutResetForUser(int userId) {
        this.mAlarmManager.cancel(this.mLockoutResetIntent.apply(java.lang.Integer.valueOf(userId)));
    }

    private void scheduleLockoutResetForUser(final int userId) {
        this.mSetLockoutAlarm = true;
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.fingerprint.hidl.LockoutFrameworkImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleLockoutResetForUser$2(userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleLockoutResetForUser$2(int userId) {
        this.mAlarmManager.setExact(2, android.os.SystemClock.elapsedRealtime() + 30000, this.mLockoutResetIntent.apply(java.lang.Integer.valueOf(userId)));
    }
}
