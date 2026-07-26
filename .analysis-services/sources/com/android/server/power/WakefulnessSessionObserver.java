package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class WakefulnessSessionObserver {
    private static final int DEFAULT_USER_ACTIVITY = 0;
    protected static final int OFF_REASON_POWER_BUTTON = 2;
    private static final int OFF_REASON_TIMEOUT = 1;
    private static final int OFF_REASON_UNKNOWN = 0;
    private static final int OVERRIDE_OUTCOME_CANCEL_CLIENT_API_CALL = 3;
    private static final int OVERRIDE_OUTCOME_CANCEL_CLIENT_DISCONNECT = 6;
    private static final int OVERRIDE_OUTCOME_CANCEL_OTHER = 7;
    protected static final int OVERRIDE_OUTCOME_CANCEL_POWER_BUTTON = 5;
    protected static final int OVERRIDE_OUTCOME_CANCEL_USER_INTERACTION = 4;
    protected static final int OVERRIDE_OUTCOME_TIMEOUT_SUCCESS = 1;
    protected static final int OVERRIDE_OUTCOME_TIMEOUT_USER_INITIATED_REVERT = 2;
    private static final int OVERRIDE_OUTCOME_UNKNOWN = 0;
    private static final long SEND_OVERRIDE_TIMEOUT_LOG_THRESHOLD_MILLIS = 1000;
    private static final java.lang.String TAG = "WakefulnessSessionObserver";
    private static final long TIMEOUT_USER_INITIATED_REVERT_THRESHOLD_MILLIS = 5000;
    private final com.android.server.power.WakefulnessSessionObserver.Clock mClock;
    private android.content.Context mContext;
    private int mOverrideTimeoutMs;
    private int mScreenOffTimeoutMs;
    protected com.android.server.power.WakefulnessSessionObserver.WakefulnessSessionFrameworkStatsLogger mWakefulnessSessionFrameworkStatsLogger;
    protected final android.util.SparseArray<com.android.server.power.WakefulnessSessionObserver.WakefulnessSessionPowerGroup> mPowerGroups = new android.util.SparseArray<>();
    private final java.lang.Object mLock = new java.lang.Object();

    interface Clock {
        long uptimeMillis();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface OffReason {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface OverrideOutcome {
    }

    public WakefulnessSessionObserver(android.content.Context context, com.android.server.power.WakefulnessSessionObserver.Injector injector) {
        this.mOverrideTimeoutMs = 0;
        injector = injector == null ? new com.android.server.power.WakefulnessSessionObserver.Injector() : injector;
        this.mContext = context;
        this.mWakefulnessSessionFrameworkStatsLogger = injector.getWakefulnessSessionFrameworkStatsLogger();
        this.mClock = injector.getClock();
        updateSettingScreenOffTimeout(context);
        try {
            com.android.server.power.WakefulnessSessionObserver.UserSwitchObserver observer = new com.android.server.power.WakefulnessSessionObserver.UserSwitchObserver();
            android.app.ActivityManager.getService().registerUserSwitchObserver(observer, TAG);
        } catch (android.os.RemoteException e) {
        }
        this.mOverrideTimeoutMs = this.mContext.getResources().getInteger(android.R.integer.config_reduceBrightColorsStrengthMax);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.System.getUriFor("screen_off_timeout"), false, new android.database.ContentObserver(new android.os.Handler(this.mContext.getMainLooper())) { // from class: com.android.server.power.WakefulnessSessionObserver.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.power.WakefulnessSessionObserver.this.updateSettingScreenOffTimeout(com.android.server.power.WakefulnessSessionObserver.this.mContext);
            }
        }, -1);
        this.mPowerGroups.append(0, new com.android.server.power.WakefulnessSessionObserver.WakefulnessSessionPowerGroup(0));
    }

    public void notifyUserActivity(long eventTime, int powerGroupId, int event) {
        if (!this.mPowerGroups.contains(powerGroupId)) {
            this.mPowerGroups.append(powerGroupId, new com.android.server.power.WakefulnessSessionObserver.WakefulnessSessionPowerGroup(powerGroupId));
        }
        this.mPowerGroups.get(powerGroupId).notifyUserActivity(eventTime, event);
    }

    public void onWakefulnessChangeStarted(int powerGroupId, int wakefulness, int changeReason, long eventTime) {
        if (!this.mPowerGroups.contains(powerGroupId)) {
            this.mPowerGroups.append(powerGroupId, new com.android.server.power.WakefulnessSessionObserver.WakefulnessSessionPowerGroup(powerGroupId));
        }
        this.mPowerGroups.get(powerGroupId).onWakefulnessChangeStarted(wakefulness, changeReason, eventTime);
    }

    public void onWakeLockAcquired(int flags) {
        int maskedFlag = 65535 & flags;
        if (maskedFlag == 256) {
            for (int idx = 0; idx < this.mPowerGroups.size(); idx++) {
                this.mPowerGroups.valueAt(idx).acquireTimeoutOverrideWakeLock();
            }
        }
    }

    public void onWakeLockReleased(int flags, int releaseReason) {
        int maskedFlag = 65535 & flags;
        if (maskedFlag == 256) {
            for (int idx = 0; idx < this.mPowerGroups.size(); idx++) {
                this.mPowerGroups.valueAt(idx).releaseTimeoutOverrideWakeLock(releaseReason);
            }
        }
    }

    public void removePowerGroup(int powerGroupId) {
        if (this.mPowerGroups.contains(powerGroupId)) {
            this.mPowerGroups.delete(powerGroupId);
        }
    }

    void dump(java.io.PrintWriter writer) {
        writer.println();
        writer.println("Wakefulness Session Observer:");
        writer.println("default timeout: " + this.mScreenOffTimeoutMs);
        writer.println("override timeout: " + this.mOverrideTimeoutMs);
        android.util.IndentingPrintWriter indentingPrintWriter = new android.util.IndentingPrintWriter(writer);
        indentingPrintWriter.increaseIndent();
        for (int idx = 0; idx < this.mPowerGroups.size(); idx++) {
            this.mPowerGroups.valueAt(idx).dump(indentingPrintWriter);
        }
        writer.println();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSettingScreenOffTimeout(android.content.Context context) {
        synchronized (this.mLock) {
            this.mScreenOffTimeoutMs = android.provider.Settings.System.getIntForUser(context.getContentResolver(), "screen_off_timeout", com.android.server.am.ProcessList.PSS_MIN_TIME_FROM_STATE_CHANGE, -2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getScreenOffTimeout() {
        int i;
        synchronized (this.mLock) {
            i = this.mScreenOffTimeoutMs;
        }
        return i;
    }

    protected class WakefulnessSessionPowerGroup {
        private static final long TIMEOUT_OFF_RESET_TIMESTAMP = -1;
        private int mCurrentWakefulness;
        private long mInteractiveStateOnStartTimestamp;
        private int mPowerGroupId;
        private long mSendOverrideTimeoutLogTimestamp;
        private long mTimeoutOffTimestamp;
        private int mTimeoutOverrideReleaseReason;
        private boolean mIsInteractive = false;
        private int mTimeoutOverrideWakeLockCounter = 0;
        protected int mCurrentUserActivityEvent = 0;
        protected long mCurrentUserActivityTimestamp = -1;
        protected int mPrevUserActivityEvent = 0;
        protected long mPrevUserActivityTimestamp = -1;

        public WakefulnessSessionPowerGroup(int powerGroupId) {
            this.mPowerGroupId = powerGroupId;
        }

        public void notifyUserActivity(long eventTime, int event) {
            if (event == this.mCurrentUserActivityEvent) {
                return;
            }
            this.mPrevUserActivityEvent = this.mCurrentUserActivityEvent;
            this.mCurrentUserActivityEvent = event;
            this.mPrevUserActivityTimestamp = this.mCurrentUserActivityTimestamp;
            this.mCurrentUserActivityTimestamp = eventTime;
        }

        public void onWakefulnessChangeStarted(int wakefulness, int changeReason, long eventTime) {
            this.mCurrentWakefulness = wakefulness;
            if (this.mIsInteractive != android.os.PowerManagerInternal.isInteractive(wakefulness)) {
                this.mIsInteractive = android.os.PowerManagerInternal.isInteractive(wakefulness);
                if (this.mIsInteractive) {
                    this.mInteractiveStateOnStartTimestamp = eventTime;
                    if (this.mTimeoutOffTimestamp != -1) {
                        long offToOnDurationMs = eventTime - this.mTimeoutOffTimestamp;
                        if (offToOnDurationMs < com.android.server.power.WakefulnessSessionObserver.TIMEOUT_USER_INITIATED_REVERT_THRESHOLD_MILLIS) {
                            com.android.server.power.WakefulnessSessionObserver.this.mWakefulnessSessionFrameworkStatsLogger.logTimeoutOverrideEvent(this.mPowerGroupId, 2, com.android.server.power.WakefulnessSessionObserver.this.mOverrideTimeoutMs, com.android.server.power.WakefulnessSessionObserver.this.getScreenOffTimeout());
                            this.mSendOverrideTimeoutLogTimestamp = eventTime;
                        }
                        this.mTimeoutOffTimestamp = -1L;
                        return;
                    }
                    return;
                }
                int lastUserActivity = this.mCurrentUserActivityEvent;
                long lastUserActivityDurationMs = eventTime - this.mCurrentUserActivityTimestamp;
                int interactiveStateOffReason = 0;
                int reducedInteractiveStateOnDurationMs = 0;
                if (changeReason == 4) {
                    interactiveStateOffReason = 2;
                    lastUserActivity = this.mPrevUserActivityEvent;
                    lastUserActivityDurationMs = eventTime - this.mPrevUserActivityTimestamp;
                    if (isInOverrideTimeout() || this.mTimeoutOverrideReleaseReason == 5) {
                        com.android.server.power.WakefulnessSessionObserver.this.mWakefulnessSessionFrameworkStatsLogger.logTimeoutOverrideEvent(this.mPowerGroupId, 5, com.android.server.power.WakefulnessSessionObserver.this.mOverrideTimeoutMs, com.android.server.power.WakefulnessSessionObserver.this.getScreenOffTimeout());
                        this.mSendOverrideTimeoutLogTimestamp = eventTime;
                        this.mTimeoutOverrideReleaseReason = -1;
                    }
                } else if (changeReason == 2) {
                    interactiveStateOffReason = 1;
                    lastUserActivity = this.mCurrentUserActivityEvent;
                    lastUserActivityDurationMs = eventTime - this.mCurrentUserActivityTimestamp;
                    if (isInOverrideTimeout()) {
                        reducedInteractiveStateOnDurationMs = com.android.server.power.WakefulnessSessionObserver.this.getScreenOffTimeout() - com.android.server.power.WakefulnessSessionObserver.this.mOverrideTimeoutMs;
                        com.android.server.power.WakefulnessSessionObserver.this.mWakefulnessSessionFrameworkStatsLogger.logTimeoutOverrideEvent(this.mPowerGroupId, 1, com.android.server.power.WakefulnessSessionObserver.this.mOverrideTimeoutMs, com.android.server.power.WakefulnessSessionObserver.this.getScreenOffTimeout());
                        this.mSendOverrideTimeoutLogTimestamp = eventTime;
                        this.mTimeoutOffTimestamp = eventTime;
                    }
                }
                long interactiveStateOnDurationMs = eventTime - this.mInteractiveStateOnStartTimestamp;
                com.android.server.power.WakefulnessSessionObserver.this.mWakefulnessSessionFrameworkStatsLogger.logSessionEvent(this.mPowerGroupId, interactiveStateOffReason, interactiveStateOnDurationMs, lastUserActivity, lastUserActivityDurationMs, reducedInteractiveStateOnDurationMs);
            }
        }

        public void acquireTimeoutOverrideWakeLock() {
            synchronized (com.android.server.power.WakefulnessSessionObserver.this.mLock) {
                this.mTimeoutOverrideWakeLockCounter++;
            }
        }

        public void releaseTimeoutOverrideWakeLock(int releaseReason) {
            int outcome;
            synchronized (com.android.server.power.WakefulnessSessionObserver.this.mLock) {
                this.mTimeoutOverrideWakeLockCounter--;
            }
            if (!isInOverrideTimeout()) {
                this.mTimeoutOverrideReleaseReason = releaseReason;
                long now = com.android.server.power.WakefulnessSessionObserver.this.mClock.uptimeMillis();
                long sendOverrideTimeoutLogDuration = now - this.mSendOverrideTimeoutLogTimestamp;
                boolean sendOverrideTimeoutLogSoon = sendOverrideTimeoutLogDuration < 1000;
                if (!sendOverrideTimeoutLogSoon) {
                    switch (releaseReason) {
                        case 1:
                        case 2:
                            outcome = 6;
                            break;
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            outcome = 4;
                            break;
                        default:
                            outcome = 0;
                            break;
                    }
                    com.android.server.power.WakefulnessSessionObserver.this.mWakefulnessSessionFrameworkStatsLogger.logTimeoutOverrideEvent(this.mPowerGroupId, outcome, com.android.server.power.WakefulnessSessionObserver.this.mOverrideTimeoutMs, com.android.server.power.WakefulnessSessionObserver.this.getScreenOffTimeout());
                }
            }
        }

        protected boolean isInOverrideTimeout() {
            boolean z;
            synchronized (com.android.server.power.WakefulnessSessionObserver.this.mLock) {
                z = this.mTimeoutOverrideWakeLockCounter > 0;
            }
            return z;
        }

        void dump(android.util.IndentingPrintWriter writer) {
            long now = com.android.server.power.WakefulnessSessionObserver.this.mClock.uptimeMillis();
            writer.println("Wakefulness Session Power Group powerGroupId: " + this.mPowerGroupId);
            writer.increaseIndent();
            writer.println("current wakefulness: " + this.mCurrentWakefulness);
            writer.println("current user activity event: " + this.mCurrentUserActivityEvent);
            long currentUserActivityDurationMs = now - this.mCurrentUserActivityTimestamp;
            writer.println("current user activity duration: " + currentUserActivityDurationMs);
            writer.println("previous user activity event: " + this.mPrevUserActivityEvent);
            long prevUserActivityDurationMs = now - this.mPrevUserActivityTimestamp;
            writer.println("previous user activity duration: " + prevUserActivityDurationMs);
            writer.println("is in override timeout: " + isInOverrideTimeout());
            writer.decreaseIndent();
        }
    }

    protected static class WakefulnessSessionFrameworkStatsLogger {
        private static final int USER_ACTIVITY_ACCESSIBILITY = 3;
        private static final int USER_ACTIVITY_ATTENTION = 4;
        private static final int USER_ACTIVITY_BUTTON = 1;
        private static final int USER_ACTIVITY_DEVICE_STATE = 6;
        private static final int USER_ACTIVITY_FACE_DOWN = 5;
        private static final int USER_ACTIVITY_OTHER = 0;
        private static final int USER_ACTIVITY_TOUCH = 2;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        private @interface UserActivityEvent {
        }

        protected WakefulnessSessionFrameworkStatsLogger() {
        }

        public void logSessionEvent(int powerGroupId, int interactiveStateOffReason, long interactiveStateOnDurationMs, int userActivityEvent, long lastUserActivityEventDurationMs, int reducedInteractiveStateOnDurationMs) {
            int logUserActivityEvent = convertToLogUserActivityEvent(userActivityEvent);
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SCREEN_INTERACTIVE_SESSION_REPORTED, powerGroupId, interactiveStateOffReason, interactiveStateOnDurationMs, logUserActivityEvent, lastUserActivityEventDurationMs, reducedInteractiveStateOnDurationMs);
        }

        public void logTimeoutOverrideEvent(int powerGroupId, int overrideOutcome, int overrideTimeoutMs, int defaultTimeoutMs) {
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.SCREEN_TIMEOUT_OVERRIDE_REPORTED, powerGroupId, overrideOutcome, overrideTimeoutMs, defaultTimeoutMs);
        }

        private int convertToLogUserActivityEvent(int userActivity) {
            switch (userActivity) {
            }
            return 0;
        }
    }

    private final class UserSwitchObserver extends android.app.SynchronousUserSwitchObserver {
        private UserSwitchObserver() {
        }

        public void onUserSwitching(int newUserId) throws android.os.RemoteException {
            com.android.server.power.WakefulnessSessionObserver.this.updateSettingScreenOffTimeout(com.android.server.power.WakefulnessSessionObserver.this.mContext);
        }
    }

    static class Injector {
        Injector() {
        }

        com.android.server.power.WakefulnessSessionObserver.WakefulnessSessionFrameworkStatsLogger getWakefulnessSessionFrameworkStatsLogger() {
            return new com.android.server.power.WakefulnessSessionObserver.WakefulnessSessionFrameworkStatsLogger();
        }

        com.android.server.power.WakefulnessSessionObserver.Clock getClock() {
            return new com.android.server.power.WakefulnessSessionObserver.Clock() { // from class: com.android.server.power.WakefulnessSessionObserver$Injector$$ExternalSyntheticLambda0
                @Override // com.android.server.power.WakefulnessSessionObserver.Clock
                public final long uptimeMillis() {
                    return android.os.SystemClock.uptimeMillis();
                }
            };
        }
    }
}
