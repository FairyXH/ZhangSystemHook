package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
public class LockSettingsStrongAuth {
    private static final boolean DEBUG;
    private static final boolean DEBUG_LOCK;
    public static final long DEFAULT_NON_STRONG_BIOMETRIC_IDLE_TIMEOUT_MS = 14400000;
    public static final long DEFAULT_NON_STRONG_BIOMETRIC_TIMEOUT_MS = 86400000;
    private static final int MSG_NO_LONGER_REQUIRE_STRONG_AUTH = 6;
    private static final int MSG_REFRESH_STRONG_AUTH_TIMEOUT = 10;
    private static final int MSG_REGISTER_TRACKER = 2;
    private static final int MSG_REMOVE_USER = 4;
    private static final int MSG_REQUIRE_STRONG_AUTH = 1;
    private static final int MSG_SCHEDULE_NON_STRONG_BIOMETRIC_IDLE_TIMEOUT = 9;
    private static final int MSG_SCHEDULE_NON_STRONG_BIOMETRIC_TIMEOUT = 7;
    private static final int MSG_SCHEDULE_STRONG_AUTH_TIMEOUT = 5;
    private static final int MSG_STRONG_BIOMETRIC_UNLOCK = 8;
    private static final int MSG_UNREGISTER_TRACKER = 3;
    protected static final java.lang.String NON_STRONG_BIOMETRIC_IDLE_TIMEOUT_ALARM_TAG = "LockSettingsPrimaryAuth.nonStrongBiometricIdleTimeoutForUser";
    protected static final java.lang.String NON_STRONG_BIOMETRIC_TIMEOUT_ALARM_TAG = "LockSettingsPrimaryAuth.nonStrongBiometricTimeoutForUser";
    protected static final java.lang.String STRONG_AUTH_TIMEOUT_ALARM_TAG = "LockSettingsStrongAuth.timeoutForUser";
    private static final java.lang.String TAG = "LockSettingsStrongAuth";
    private final android.app.AlarmManager mAlarmManager;
    private final android.content.Context mContext;
    private final boolean mDefaultIsNonStrongBiometricAllowed;
    private final int mDefaultStrongAuthFlags;
    protected final android.os.Handler mHandler;
    private final com.android.server.locksettings.LockSettingsStrongAuth.Injector mInjector;
    protected final android.util.SparseBooleanArray mIsNonStrongBiometricAllowedForUser;
    protected final android.util.ArrayMap<java.lang.Integer, com.android.server.locksettings.LockSettingsStrongAuth.NonStrongBiometricIdleTimeoutAlarmListener> mNonStrongBiometricIdleTimeoutAlarmListener;
    protected final android.util.ArrayMap<java.lang.Integer, com.android.server.locksettings.LockSettingsStrongAuth.NonStrongBiometricTimeoutAlarmListener> mNonStrongBiometricTimeoutAlarmListener;
    protected final android.util.SparseIntArray mStrongAuthForUser;
    protected final android.util.ArrayMap<java.lang.Integer, com.android.server.locksettings.LockSettingsStrongAuth.StrongAuthTimeoutAlarmListener> mStrongAuthTimeoutAlarmListenerForUser;
    private final android.os.RemoteCallbackList<android.app.trust.IStrongAuthTracker> mTrackers;

    static {
        boolean z = true;
        DEBUG = android.os.Build.IS_DEBUGGABLE && android.util.Log.isLoggable(TAG, 3);
        if (!android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false) && !android.os.SystemProperties.getBoolean("persist.sys.alwayson.enable", false)) {
            z = false;
        }
        DEBUG_LOCK = z;
    }

    public LockSettingsStrongAuth(android.content.Context context) {
        this(context, new com.android.server.locksettings.LockSettingsStrongAuth.Injector());
    }

    protected LockSettingsStrongAuth(android.content.Context context, com.android.server.locksettings.LockSettingsStrongAuth.Injector injector) {
        this.mTrackers = new android.os.RemoteCallbackList<>();
        this.mStrongAuthForUser = new android.util.SparseIntArray();
        this.mIsNonStrongBiometricAllowedForUser = new android.util.SparseBooleanArray();
        this.mStrongAuthTimeoutAlarmListenerForUser = new android.util.ArrayMap<>();
        this.mNonStrongBiometricTimeoutAlarmListener = new android.util.ArrayMap<>();
        this.mNonStrongBiometricIdleTimeoutAlarmListener = new android.util.ArrayMap<>();
        this.mDefaultIsNonStrongBiometricAllowed = true;
        this.mHandler = new android.os.Handler(android.os.Looper.getMainLooper()) { // from class: com.android.server.locksettings.LockSettingsStrongAuth.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        com.android.server.locksettings.LockSettingsStrongAuth.this.handleRequireStrongAuth(msg.arg1, msg.arg2);
                        break;
                    case 2:
                        com.android.server.locksettings.LockSettingsStrongAuth.this.handleAddStrongAuthTracker((android.app.trust.IStrongAuthTracker) msg.obj);
                        break;
                    case 3:
                        com.android.server.locksettings.LockSettingsStrongAuth.this.handleRemoveStrongAuthTracker((android.app.trust.IStrongAuthTracker) msg.obj);
                        break;
                    case 4:
                        com.android.server.locksettings.LockSettingsStrongAuth.this.handleRemoveUser(msg.arg1);
                        break;
                    case 5:
                        com.android.server.locksettings.LockSettingsStrongAuth.this.handleScheduleStrongAuthTimeout(msg.arg1);
                        break;
                    case 6:
                        com.android.server.locksettings.LockSettingsStrongAuth.this.handleNoLongerRequireStrongAuth(msg.arg1, msg.arg2);
                        break;
                    case 7:
                        com.android.server.locksettings.LockSettingsStrongAuth.this.handleScheduleNonStrongBiometricTimeout(msg.arg1);
                        break;
                    case 8:
                        com.android.server.locksettings.LockSettingsStrongAuth.this.handleStrongBiometricUnlock(msg.arg1);
                        break;
                    case 9:
                        com.android.server.locksettings.LockSettingsStrongAuth.this.handleScheduleNonStrongBiometricIdleTimeout(msg.arg1);
                        break;
                    case 10:
                        com.android.server.locksettings.LockSettingsStrongAuth.this.handleRefreshStrongAuthTimeout(msg.arg1);
                        break;
                }
            }
        };
        this.mContext = context;
        this.mInjector = injector;
        this.mDefaultStrongAuthFlags = this.mInjector.getDefaultStrongAuthFlags(context);
        this.mAlarmManager = this.mInjector.getAlarmManager(context);
    }

    public static class Injector {
        public android.app.AlarmManager getAlarmManager(android.content.Context context) {
            return (android.app.AlarmManager) context.getSystemService(android.app.AlarmManager.class);
        }

        public int getDefaultStrongAuthFlags(android.content.Context context) {
            return com.android.internal.widget.LockPatternUtils.StrongAuthTracker.getDefaultFlags(context);
        }

        public long getNextAlarmTimeMs(long timeout) {
            return android.os.SystemClock.elapsedRealtime() + timeout;
        }

        public long getElapsedRealtimeMs() {
            return android.os.SystemClock.elapsedRealtime();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAddStrongAuthTracker(android.app.trust.IStrongAuthTracker tracker) {
        this.mTrackers.register(tracker);
        for (int i = 0; i < this.mStrongAuthForUser.size(); i++) {
            int key = this.mStrongAuthForUser.keyAt(i);
            int value = this.mStrongAuthForUser.valueAt(i);
            try {
                tracker.onStrongAuthRequiredChanged(value, key);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Exception while adding StrongAuthTracker.", e);
            }
        }
        for (int i2 = 0; i2 < this.mIsNonStrongBiometricAllowedForUser.size(); i2++) {
            int key2 = this.mIsNonStrongBiometricAllowedForUser.keyAt(i2);
            boolean value2 = this.mIsNonStrongBiometricAllowedForUser.valueAt(i2);
            try {
                tracker.onIsNonStrongBiometricAllowedChanged(value2, key2);
            } catch (android.os.RemoteException e2) {
                android.util.Slog.e(TAG, "Exception while adding StrongAuthTracker: IsNonStrongBiometricAllowedChanged.", e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRemoveStrongAuthTracker(android.app.trust.IStrongAuthTracker tracker) {
        this.mTrackers.unregister(tracker);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRequireStrongAuth(int strongAuthReason, int userId) {
        if (userId == -1) {
            for (int i = 0; i < this.mStrongAuthForUser.size(); i++) {
                int key = this.mStrongAuthForUser.keyAt(i);
                handleRequireStrongAuthOneUser(strongAuthReason, key);
            }
            return;
        }
        handleRequireStrongAuthOneUser(strongAuthReason, userId);
    }

    private void handleRequireStrongAuthOneUser(int strongAuthReason, int userId) {
        int newValue;
        int oldValue = this.mStrongAuthForUser.get(userId, this.mDefaultStrongAuthFlags);
        if (strongAuthReason == 0) {
            newValue = 0;
        } else {
            newValue = oldValue | strongAuthReason;
        }
        android.util.Slog.d("LockSettingsService", "handleRequireStrongAuthOneUser oldValue: " + oldValue + " newValue: " + newValue + " strongAuthReason: " + strongAuthReason + " userId: " + userId);
        if (oldValue != newValue) {
            this.mStrongAuthForUser.put(userId, newValue);
            notifyStrongAuthTrackers(newValue, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNoLongerRequireStrongAuth(int strongAuthReason, int userId) {
        if (userId == -1) {
            for (int i = 0; i < this.mStrongAuthForUser.size(); i++) {
                int key = this.mStrongAuthForUser.keyAt(i);
                handleNoLongerRequireStrongAuthOneUser(strongAuthReason, key);
            }
            return;
        }
        handleNoLongerRequireStrongAuthOneUser(strongAuthReason, userId);
    }

    private void handleNoLongerRequireStrongAuthOneUser(int strongAuthReason, int userId) {
        int oldValue = this.mStrongAuthForUser.get(userId, this.mDefaultStrongAuthFlags);
        int newValue = (~strongAuthReason) & oldValue;
        if (DEBUG_LOCK) {
            android.util.Slog.d("LockSettingsService", "handleNoLongerRequireStrongAuthOneUser oldValue:" + oldValue + " newValue:" + newValue + " strongAuthReason:" + strongAuthReason + " userId:" + userId);
        }
        if (oldValue != newValue) {
            this.mStrongAuthForUser.put(userId, newValue);
            notifyStrongAuthTrackers(newValue, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRemoveUser(int userId) {
        int index = this.mStrongAuthForUser.indexOfKey(userId);
        if (index >= 0) {
            this.mStrongAuthForUser.removeAt(index);
            notifyStrongAuthTrackers(this.mDefaultStrongAuthFlags, userId);
        }
        int index2 = this.mIsNonStrongBiometricAllowedForUser.indexOfKey(userId);
        if (index2 >= 0) {
            this.mIsNonStrongBiometricAllowedForUser.removeAt(index2);
            notifyStrongAuthTrackersForIsNonStrongBiometricAllowed(true, userId);
        }
    }

    private void rescheduleStrongAuthTimeoutAlarm(long strongAuthTime, int userId) {
        android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) this.mContext.getSystemService("device_policy");
        com.android.server.locksettings.LockSettingsStrongAuth.StrongAuthTimeoutAlarmListener alarm = this.mStrongAuthTimeoutAlarmListenerForUser.get(java.lang.Integer.valueOf(userId));
        if (alarm != null) {
            this.mAlarmManager.cancel(alarm);
            alarm.setLatestStrongAuthTime(strongAuthTime);
        } else {
            alarm = new com.android.server.locksettings.LockSettingsStrongAuth.StrongAuthTimeoutAlarmListener(strongAuthTime, userId);
            this.mStrongAuthTimeoutAlarmListenerForUser.put(java.lang.Integer.valueOf(userId), alarm);
        }
        long nextAlarmTime = dpm.getRequiredStrongAuthTimeout(null, userId) + strongAuthTime;
        this.mAlarmManager.setExact(2, nextAlarmTime, STRONG_AUTH_TIMEOUT_ALARM_TAG, alarm, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScheduleStrongAuthTimeout(int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "handleScheduleStrongAuthTimeout for userId=" + userId);
        }
        rescheduleStrongAuthTimeoutAlarm(this.mInjector.getElapsedRealtimeMs(), userId);
        cancelNonStrongBiometricAlarmListener(userId);
        cancelNonStrongBiometricIdleAlarmListener(userId);
        setIsNonStrongBiometricAllowed(true, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRefreshStrongAuthTimeout(int userId) {
        com.android.server.locksettings.LockSettingsStrongAuth.StrongAuthTimeoutAlarmListener alarm = this.mStrongAuthTimeoutAlarmListenerForUser.get(java.lang.Integer.valueOf(userId));
        if (alarm != null) {
            rescheduleStrongAuthTimeoutAlarm(alarm.getLatestStrongAuthTime(), userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScheduleNonStrongBiometricTimeout(int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "handleScheduleNonStrongBiometricTimeout for userId=" + userId);
        }
        long nextAlarmTime = this.mInjector.getNextAlarmTimeMs(86400000L);
        if (this.mNonStrongBiometricTimeoutAlarmListener.get(java.lang.Integer.valueOf(userId)) != null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "There is an existing alarm for non-strong biometric fallback timeout, so do not re-schedule");
            }
        } else {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Schedule a new alarm for non-strong biometric fallback timeout");
            }
            com.android.server.locksettings.LockSettingsStrongAuth.NonStrongBiometricTimeoutAlarmListener alarm = new com.android.server.locksettings.LockSettingsStrongAuth.NonStrongBiometricTimeoutAlarmListener(userId);
            this.mNonStrongBiometricTimeoutAlarmListener.put(java.lang.Integer.valueOf(userId), alarm);
            this.mAlarmManager.setExact(2, nextAlarmTime, NON_STRONG_BIOMETRIC_TIMEOUT_ALARM_TAG, alarm, this.mHandler);
        }
        cancelNonStrongBiometricIdleAlarmListener(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStrongBiometricUnlock(int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "handleStrongBiometricUnlock for userId=" + userId);
        }
        cancelNonStrongBiometricAlarmListener(userId);
        cancelNonStrongBiometricIdleAlarmListener(userId);
        setIsNonStrongBiometricAllowed(true, userId);
    }

    private void cancelNonStrongBiometricAlarmListener(int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "cancelNonStrongBiometricAlarmListener for userId=" + userId);
        }
        com.android.server.locksettings.LockSettingsStrongAuth.NonStrongBiometricTimeoutAlarmListener alarm = this.mNonStrongBiometricTimeoutAlarmListener.get(java.lang.Integer.valueOf(userId));
        if (alarm != null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Cancel alarm for non-strong biometric fallback timeout");
            }
            this.mAlarmManager.cancel(alarm);
            this.mNonStrongBiometricTimeoutAlarmListener.remove(java.lang.Integer.valueOf(userId));
        }
    }

    private void cancelNonStrongBiometricIdleAlarmListener(int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "cancelNonStrongBiometricIdleAlarmListener for userId=" + userId);
        }
        com.android.server.locksettings.LockSettingsStrongAuth.NonStrongBiometricIdleTimeoutAlarmListener alarm = this.mNonStrongBiometricIdleTimeoutAlarmListener.get(java.lang.Integer.valueOf(userId));
        if (alarm != null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Cancel alarm for non-strong biometric idle timeout");
            }
            this.mAlarmManager.cancel(alarm);
        }
    }

    protected void setIsNonStrongBiometricAllowed(boolean allowed, int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "setIsNonStrongBiometricAllowed for allowed=" + allowed + ", userId=" + userId);
        }
        if (userId == -1) {
            for (int i = 0; i < this.mIsNonStrongBiometricAllowedForUser.size(); i++) {
                int key = this.mIsNonStrongBiometricAllowedForUser.keyAt(i);
                setIsNonStrongBiometricAllowedOneUser(allowed, key);
            }
            return;
        }
        setIsNonStrongBiometricAllowedOneUser(allowed, userId);
    }

    private void setIsNonStrongBiometricAllowedOneUser(boolean allowed, int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "setIsNonStrongBiometricAllowedOneUser for allowed=" + allowed + ", userId=" + userId);
        }
        boolean oldValue = this.mIsNonStrongBiometricAllowedForUser.get(userId, true);
        if (allowed != oldValue) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "mIsNonStrongBiometricAllowedForUser value changed: oldValue=" + oldValue + ", allowed=" + allowed);
            }
            this.mIsNonStrongBiometricAllowedForUser.put(userId, allowed);
            notifyStrongAuthTrackersForIsNonStrongBiometricAllowed(allowed, userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScheduleNonStrongBiometricIdleTimeout(int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "handleScheduleNonStrongBiometricIdleTimeout for userId=" + userId);
        }
        long nextAlarmTime = this.mInjector.getNextAlarmTimeMs(14400000L);
        com.android.server.locksettings.LockSettingsStrongAuth.NonStrongBiometricIdleTimeoutAlarmListener alarm = this.mNonStrongBiometricIdleTimeoutAlarmListener.get(java.lang.Integer.valueOf(userId));
        if (alarm != null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Cancel existing alarm for non-strong biometric idle timeout");
            }
            this.mAlarmManager.cancel(alarm);
        } else {
            alarm = new com.android.server.locksettings.LockSettingsStrongAuth.NonStrongBiometricIdleTimeoutAlarmListener(userId);
            this.mNonStrongBiometricIdleTimeoutAlarmListener.put(java.lang.Integer.valueOf(userId), alarm);
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Schedule a new alarm for non-strong biometric idle timeout");
        }
        this.mAlarmManager.setExact(2, nextAlarmTime, NON_STRONG_BIOMETRIC_IDLE_TIMEOUT_ALARM_TAG, alarm, this.mHandler);
    }

    private void notifyStrongAuthTrackers(int strongAuthReason, int userId) {
        int i = this.mTrackers.beginBroadcast();
        while (i > 0) {
            i--;
            try {
                try {
                    this.mTrackers.getBroadcastItem(i).onStrongAuthRequiredChanged(strongAuthReason, userId);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Exception while notifying StrongAuthTracker.", e);
                }
            } finally {
                this.mTrackers.finishBroadcast();
            }
        }
    }

    private void notifyStrongAuthTrackersForIsNonStrongBiometricAllowed(boolean allowed, int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "notifyStrongAuthTrackersForIsNonStrongBiometricAllowed for allowed=" + allowed + ", userId=" + userId);
        }
        int i = this.mTrackers.beginBroadcast();
        while (i > 0) {
            i--;
            try {
                try {
                    this.mTrackers.getBroadcastItem(i).onIsNonStrongBiometricAllowedChanged(allowed, userId);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Exception while notifying StrongAuthTracker: IsNonStrongBiometricAllowedChanged.", e);
                }
            } finally {
                this.mTrackers.finishBroadcast();
            }
        }
    }

    public void registerStrongAuthTracker(android.app.trust.IStrongAuthTracker tracker) {
        this.mHandler.obtainMessage(2, tracker).sendToTarget();
    }

    public void unregisterStrongAuthTracker(android.app.trust.IStrongAuthTracker tracker) {
        this.mHandler.obtainMessage(3, tracker).sendToTarget();
    }

    public void removeUser(int userId) {
        this.mHandler.obtainMessage(4, userId, 0).sendToTarget();
    }

    public void requireStrongAuth(int strongAuthReason, int userId) {
        if (DEBUG_LOCK) {
            android.util.Slog.d("LockSettingsService", "requireStrongAuth  strongAuthReason:" + strongAuthReason + " userId:" + userId + " (" + android.os.Debug.getCallers(8) + ")");
        }
        if (userId == -1 || userId >= 0) {
            this.mHandler.obtainMessage(1, strongAuthReason, userId).sendToTarget();
            return;
        }
        throw new java.lang.IllegalArgumentException("userId must be an explicit user id or USER_ALL");
    }

    void noLongerRequireStrongAuth(int strongAuthReason, int userId) {
        if (DEBUG_LOCK) {
            android.util.Slog.d("LockSettingsService", "noLongerRequireStrongAuth  strongAuthReason:" + strongAuthReason + " userId:" + userId);
        }
        if (userId == -1 || userId >= 0) {
            this.mHandler.obtainMessage(6, strongAuthReason, userId).sendToTarget();
            return;
        }
        throw new java.lang.IllegalArgumentException("userId must be an explicit user id or USER_ALL");
    }

    public void reportUnlock(int userId) {
        requireStrongAuth(0, userId);
    }

    public void reportSuccessfulStrongAuthUnlock(int userId) {
        this.mHandler.obtainMessage(5, userId, 0).sendToTarget();
    }

    public void refreshStrongAuthTimeout(int userId) {
        this.mHandler.obtainMessage(10, userId, 0).sendToTarget();
    }

    public void reportSuccessfulBiometricUnlock(boolean isStrongBiometric, int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "reportSuccessfulBiometricUnlock for isStrongBiometric=" + isStrongBiometric + ", userId=" + userId);
        }
        if (isStrongBiometric) {
            this.mHandler.obtainMessage(8, userId, 0).sendToTarget();
        } else {
            this.mHandler.obtainMessage(7, userId, 0).sendToTarget();
        }
    }

    public void scheduleNonStrongBiometricIdleTimeout(int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "scheduleNonStrongBiometricIdleTimeout for userId=" + userId);
        }
        this.mHandler.obtainMessage(9, userId, 0).sendToTarget();
    }

    protected class StrongAuthTimeoutAlarmListener implements android.app.AlarmManager.OnAlarmListener {
        private long mLatestStrongAuthTime;
        private final int mUserId;

        public StrongAuthTimeoutAlarmListener(long latestStrongAuthTime, int userId) {
            this.mLatestStrongAuthTime = latestStrongAuthTime;
            this.mUserId = userId;
        }

        public void setLatestStrongAuthTime(long strongAuthTime) {
            this.mLatestStrongAuthTime = strongAuthTime;
        }

        public long getLatestStrongAuthTime() {
            return this.mLatestStrongAuthTime;
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            com.android.server.locksettings.LockSettingsStrongAuth.this.requireStrongAuth(16, this.mUserId);
        }
    }

    protected class NonStrongBiometricTimeoutAlarmListener implements android.app.AlarmManager.OnAlarmListener {
        private final int mUserId;

        NonStrongBiometricTimeoutAlarmListener(int userId) {
            this.mUserId = userId;
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            com.android.server.locksettings.LockSettingsStrongAuth.this.requireStrongAuth(128, this.mUserId);
        }
    }

    protected class NonStrongBiometricIdleTimeoutAlarmListener implements android.app.AlarmManager.OnAlarmListener {
        private final int mUserId;

        NonStrongBiometricIdleTimeoutAlarmListener(int userId) {
            this.mUserId = userId;
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            com.android.server.locksettings.LockSettingsStrongAuth.this.setIsNonStrongBiometricAllowed(false, this.mUserId);
        }
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("PrimaryAuthFlags state:");
        pw.increaseIndent();
        for (int i = 0; i < this.mStrongAuthForUser.size(); i++) {
            int key = this.mStrongAuthForUser.keyAt(i);
            int value = this.mStrongAuthForUser.valueAt(i);
            pw.println("userId=" + key + ", primaryAuthFlags=" + java.lang.Integer.toHexString(value));
        }
        pw.println();
        pw.decreaseIndent();
        pw.println("NonStrongBiometricAllowed state:");
        pw.increaseIndent();
        for (int i2 = 0; i2 < this.mIsNonStrongBiometricAllowedForUser.size(); i2++) {
            int key2 = this.mIsNonStrongBiometricAllowedForUser.keyAt(i2);
            boolean value2 = this.mIsNonStrongBiometricAllowedForUser.valueAt(i2);
            pw.println("userId=" + key2 + ", allowed=" + value2);
        }
        pw.println();
        pw.decreaseIndent();
    }
}
