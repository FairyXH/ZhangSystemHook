package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class PowerGroup {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = com.android.server.power.PowerGroup.class.getSimpleName();
    private final android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    final android.hardware.display.DisplayManagerInternal.DisplayPowerRequest mDisplayPowerRequest;
    private final int mGroupId;
    private boolean mIsSandmanSummoned;
    private long mLastPowerOnTime;
    private int mLastSleepReason;
    private long mLastSleepTime;
    private int mLastUserActivityEvent;
    private long mLastUserActivityTime;
    private long mLastUserActivityTimeNoChangeLights;
    private long mLastWakeTime;
    private final com.android.server.power.Notifier mNotifier;
    private boolean mPoweringOn;
    private boolean mReady;
    private final boolean mSupportsSandman;
    private int mUserActivitySummary;
    private int mWakeLockSummary;
    private int mWakefulness;
    private final com.android.server.power.PowerGroup.PowerGroupListener mWakefulnessListener;

    protected interface PowerGroupListener {
        void onWakefulnessChangedLocked(int i, int i2, long j, int i3, int i4, int i5, java.lang.String str, java.lang.String str2);
    }

    PowerGroup(int groupId, com.android.server.power.PowerGroup.PowerGroupListener wakefulnessListener, com.android.server.power.Notifier notifier, android.hardware.display.DisplayManagerInternal displayManagerInternal, int wakefulness, boolean ready, boolean supportsSandman, long eventTime) {
        this.mDisplayPowerRequest = new android.hardware.display.DisplayManagerInternal.DisplayPowerRequest();
        this.mLastSleepReason = -1;
        this.mGroupId = groupId;
        this.mWakefulnessListener = wakefulnessListener;
        this.mNotifier = notifier;
        this.mDisplayManagerInternal = displayManagerInternal;
        this.mWakefulness = wakefulness;
        this.mReady = ready;
        this.mSupportsSandman = supportsSandman;
        this.mLastWakeTime = eventTime;
        this.mLastSleepTime = eventTime;
    }

    PowerGroup(int wakefulness, com.android.server.power.PowerGroup.PowerGroupListener wakefulnessListener, com.android.server.power.Notifier notifier, android.hardware.display.DisplayManagerInternal displayManagerInternal, long eventTime) {
        this.mDisplayPowerRequest = new android.hardware.display.DisplayManagerInternal.DisplayPowerRequest();
        this.mLastSleepReason = -1;
        this.mGroupId = 0;
        this.mWakefulnessListener = wakefulnessListener;
        this.mNotifier = notifier;
        this.mDisplayManagerInternal = displayManagerInternal;
        this.mWakefulness = wakefulness;
        this.mReady = false;
        this.mSupportsSandman = true;
        this.mLastWakeTime = eventTime;
        this.mLastSleepTime = eventTime;
    }

    long getLastWakeTimeLocked() {
        return this.mLastWakeTime;
    }

    long getLastSleepTimeLocked() {
        return this.mLastSleepTime;
    }

    int getWakefulnessLocked() {
        return this.mWakefulness;
    }

    int getGroupId() {
        return this.mGroupId;
    }

    boolean setWakefulnessLocked(int newWakefulness, long eventTime, int uid, int reason, int opUid, java.lang.String opPackageName, java.lang.String details) {
        if (this.mWakefulness == newWakefulness) {
            return false;
        }
        if (newWakefulness == 1) {
            setLastPowerOnTimeLocked(eventTime);
            setIsPoweringOnLocked(true);
            this.mLastWakeTime = eventTime;
        } else if (android.os.PowerManagerInternal.isInteractive(this.mWakefulness) && !android.os.PowerManagerInternal.isInteractive(newWakefulness)) {
            this.mLastSleepTime = eventTime;
            this.mLastSleepReason = reason;
        }
        this.mWakefulness = newWakefulness;
        this.mWakefulnessListener.onWakefulnessChangedLocked(this.mGroupId, this.mWakefulness, eventTime, reason, uid, opUid, opPackageName, details);
        return true;
    }

    boolean isReadyLocked() {
        return this.mReady;
    }

    boolean setReadyLocked(boolean isReady) {
        if (this.mReady != isReady) {
            this.mReady = isReady;
            return true;
        }
        return false;
    }

    long getLastPowerOnTimeLocked() {
        return this.mLastPowerOnTime;
    }

    void setLastPowerOnTimeLocked(long time) {
        this.mLastPowerOnTime = time;
    }

    boolean isPoweringOnLocked() {
        return this.mPoweringOn;
    }

    void setIsPoweringOnLocked(boolean isPoweringOnNew) {
        this.mPoweringOn = isPoweringOnNew;
    }

    boolean isSandmanSummonedLocked() {
        return this.mIsSandmanSummoned;
    }

    void setSandmanSummonedLocked(boolean isSandmanSummoned) {
        this.mIsSandmanSummoned = isSandmanSummoned;
    }

    void wakeUpLocked(long eventTime, int reason, java.lang.String details, int uid, java.lang.String opPackageName, int opUid, com.android.internal.util.LatencyTracker latencyTracker) throws java.lang.Throwable {
        if (eventTime >= this.mLastSleepTime && this.mWakefulness != 1) {
            android.os.Trace.traceBegin(131072L, "wakePowerGroup" + this.mGroupId);
            try {
                try {
                    try {
                        android.util.Slog.i(TAG, "Waking up power group from " + android.os.PowerManagerInternal.wakefulnessToString(this.mWakefulness) + " (groupId=" + this.mGroupId + ", uid=" + uid + ", reason=" + android.os.PowerManager.wakeReasonToString(reason) + ", details=" + details + ")...");
                        android.os.Trace.asyncTraceBegin(131072L, "Screen turning on", this.mGroupId);
                        try {
                            latencyTracker.onActionStart(5, java.lang.String.valueOf(this.mGroupId));
                            setWakefulnessLocked(1, eventTime, uid, reason, opUid, opPackageName, details);
                            android.os.Trace.traceEnd(131072L);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            android.os.Trace.traceEnd(131072L);
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        android.os.Trace.traceEnd(131072L);
                        throw th;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    boolean dreamLocked(long eventTime, int uid, boolean allowWake) throws java.lang.Throwable {
        if (eventTime >= this.mLastWakeTime) {
            if (!allowWake && this.mWakefulness != 1) {
                return false;
            }
            android.os.Trace.traceBegin(131072L, "dreamPowerGroup" + getGroupId());
            try {
                try {
                    android.util.Slog.i(TAG, "Napping power group (groupId=" + getGroupId() + ", uid=" + uid + ")...");
                    setSandmanSummonedLocked(true);
                    setWakefulnessLocked(2, eventTime, uid, 0, 0, null, null);
                    android.os.Trace.traceEnd(131072L);
                    return true;
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Trace.traceEnd(131072L);
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        } else {
            return false;
        }
    }

    boolean dozeLocked(long eventTime, int uid, int reason) throws java.lang.Throwable {
        if (eventTime < getLastWakeTimeLocked() || !android.os.PowerManagerInternal.isInteractive(this.mWakefulness)) {
            com.android.server.power.PowerManagerService.mPmsExt.notePowerkeyProcessStagePoint("CANCELED_dozeLocked");
            return false;
        }
        android.os.Trace.traceBegin(131072L, "powerOffDisplay");
        try {
            int reason2 = java.lang.Math.min(16, java.lang.Math.max(reason, 0));
            try {
                long millisSinceLastUserActivity = eventTime - java.lang.Math.max(this.mLastUserActivityTimeNoChangeLights, this.mLastUserActivityTime);
                try {
                    android.util.Slog.i(TAG, "Powering off display group due to " + android.os.PowerManager.sleepReasonToString(reason2) + " (groupId= " + getGroupId() + ", uid= " + uid + ", millisSinceLastUserActivity=" + millisSinceLastUserActivity + ", lastUserActivityEvent=" + android.os.PowerManager.userActivityEventToString(this.mLastUserActivityEvent) + ")...");
                    setSandmanSummonedLocked(true);
                    setWakefulnessLocked(3, eventTime, uid, reason2, 0, null, null);
                    android.os.Trace.traceEnd(131072L);
                    return true;
                } catch (java.lang.Throwable th) {
                    th = th;
                    android.os.Trace.traceEnd(131072L);
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        } catch (java.lang.Throwable th3) {
            th = th3;
        }
    }

    boolean sleepLocked(long eventTime, int uid, int reason) throws java.lang.Throwable {
        if (eventTime < this.mLastWakeTime || getWakefulnessLocked() == 0) {
            com.android.server.power.PowerManagerService.mPmsExt.notePowerkeyProcessStagePoint("CANCELED_sleepLocked");
            return false;
        }
        android.os.Trace.traceBegin(131072L, "sleepPowerGroup");
        try {
            try {
                android.util.Slog.i(TAG, "Sleeping power group (groupId=" + getGroupId() + ", uid=" + uid + ", reason=" + android.os.PowerManager.sleepReasonToString(reason) + ")...");
                setSandmanSummonedLocked(true);
                setWakefulnessLocked(0, eventTime, uid, reason, 0, null, null);
                android.os.Trace.traceEnd(131072L);
                return true;
            } catch (java.lang.Throwable th) {
                th = th;
                android.os.Trace.traceEnd(131072L);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    long getLastUserActivityTimeLocked() {
        return this.mLastUserActivityTime;
    }

    void setLastUserActivityTimeLocked(long lastUserActivityTime, int event) {
        this.mLastUserActivityTime = lastUserActivityTime;
        this.mLastUserActivityEvent = event;
    }

    public long getLastUserActivityTimeNoChangeLightsLocked() {
        return this.mLastUserActivityTimeNoChangeLights;
    }

    public void setLastUserActivityTimeNoChangeLightsLocked(long time, int event) {
        this.mLastUserActivityTimeNoChangeLights = time;
        this.mLastUserActivityEvent = event;
    }

    public int getUserActivitySummaryLocked() {
        return this.mUserActivitySummary;
    }

    public boolean isPolicyBrightLocked() {
        return this.mDisplayPowerRequest.policy == 3;
    }

    public boolean isPolicyDimLocked() {
        return this.mDisplayPowerRequest.policy == 2;
    }

    public boolean isBrightOrDimLocked() {
        return this.mDisplayPowerRequest.isBrightOrDim();
    }

    public void setUserActivitySummaryLocked(int summary) {
        this.mUserActivitySummary = summary;
    }

    public int getWakeLockSummaryLocked() {
        return this.mWakeLockSummary;
    }

    public boolean hasWakeLockKeepingScreenOnLocked() {
        return (this.mWakeLockSummary & 38) != 0;
    }

    public void setWakeLockSummaryLocked(int summary) {
        this.mWakeLockSummary = summary;
    }

    public boolean supportsSandmanLocked() {
        return this.mSupportsSandman;
    }

    boolean needSuspendBlockerLocked(boolean proximityPositive, boolean suspendWhenScreenOffDueToProximityConfig) {
        if (!isBrightOrDimLocked() || (this.mDisplayPowerRequest.useProximitySensor && proximityPositive && suspendWhenScreenOffDueToProximityConfig)) {
            return this.mDisplayPowerRequest.policy == 1 && this.mDisplayPowerRequest.dozeScreenState == 2;
        }
        return true;
    }

    int getDesiredScreenPolicyLocked(boolean quiescent, boolean dozeAfterScreenOff, boolean bootCompleted, boolean screenBrightnessBoostInProgress, boolean brightWhenDozing) {
        int wakefulness = getWakefulnessLocked();
        int wakeLockSummary = getWakeLockSummaryLocked();
        if (wakefulness == 0 || quiescent) {
            return 0;
        }
        if (wakefulness == 3) {
            if ((wakeLockSummary & 64) != 0) {
                return 1;
            }
            if (dozeAfterScreenOff && com.android.server.power.PowerManagerService.mPmsExt.getOplusDozeAfterOff(this.mLastSleepReason)) {
                return 0;
            }
            if (brightWhenDozing) {
                return 3;
            }
            if (com.android.server.power.PowerManagerService.mPmsExt.isShouldGoAod()) {
                if (com.android.server.power.PowerManagerService.DEBUG_PANIC) {
                    android.util.Slog.d(TAG, "on to doze open, dozing policy doze.");
                }
                return 1;
            }
        }
        return ((wakeLockSummary & 2) != 0 || !bootCompleted || (getUserActivitySummaryLocked() & 1) != 0 || screenBrightnessBoostInProgress || com.android.server.power.PowerManagerService.mPmsExt.getOnDozeSwitch() || com.android.server.power.PowerManagerService.mPmsExt.getCastMode()) ? 3 : 2;
    }

    int getPolicyLocked() {
        return this.mDisplayPowerRequest.policy;
    }

    boolean updateLocked(float screenBrightnessOverride, boolean useProximitySensor, boolean boostScreenBrightness, int dozeScreenState, int dozeScreenStateReason, float dozeScreenBrightness, boolean overrideDrawWakeLock, android.os.PowerSaveState powerSaverState, boolean quiescent, boolean dozeAfterScreenOff, boolean bootCompleted, boolean screenBrightnessBoostInProgress, boolean waitForNegativeProximity, boolean brightWhenDozing) {
        this.mDisplayPowerRequest.policy = getDesiredScreenPolicyLocked(quiescent, dozeAfterScreenOff, bootCompleted, screenBrightnessBoostInProgress, brightWhenDozing);
        this.mDisplayPowerRequest.screenBrightnessOverride = screenBrightnessOverride;
        this.mDisplayPowerRequest.useProximitySensor = useProximitySensor;
        this.mDisplayPowerRequest.boostScreenBrightness = boostScreenBrightness;
        if (this.mDisplayPowerRequest.policy != 1) {
            this.mDisplayPowerRequest.dozeScreenState = 0;
            this.mDisplayPowerRequest.dozeScreenBrightness = Float.NaN;
            this.mDisplayPowerRequest.dozeScreenStateReason = 1;
        } else {
            this.mDisplayPowerRequest.dozeScreenState = dozeScreenState;
            if ((getWakeLockSummaryLocked() & 64) == 0 && com.android.server.power.PowerManagerService.mPmsExt.isShouldGoAod()) {
                if (com.android.server.power.PowerManagerService.DEBUG_PANIC) {
                    android.util.Slog.d(TAG, "on to doze open, policy doze without doze wakelock");
                }
                this.mDisplayPowerRequest.dozeScreenState = 0;
            }
            this.mDisplayPowerRequest.dozeScreenStateReason = dozeScreenStateReason;
            if ((getWakeLockSummaryLocked() & 128) != 0 && !overrideDrawWakeLock) {
                if (this.mDisplayPowerRequest.dozeScreenState == 4) {
                    this.mDisplayPowerRequest.dozeScreenState = 3;
                    this.mDisplayPowerRequest.dozeScreenStateReason = 2;
                }
                if (this.mDisplayPowerRequest.dozeScreenState == 6) {
                    this.mDisplayPowerRequest.dozeScreenState = 2;
                    this.mDisplayPowerRequest.dozeScreenStateReason = 2;
                }
            }
            this.mDisplayPowerRequest.dozeScreenBrightness = dozeScreenBrightness;
        }
        this.mDisplayPowerRequest.lowPowerMode = powerSaverState.batterySaverEnabled;
        this.mDisplayPowerRequest.screenLowPowerBrightnessFactor = powerSaverState.brightnessFactor;
        if (powerSaverState.batterySaverEnabled) {
            this.mDisplayPowerRequest.batteryLevel = com.android.server.power.PowerManagerService.mPmsExt.getBatteryLevel();
        }
        boolean ready = this.mDisplayManagerInternal.requestPowerState(this.mGroupId, this.mDisplayPowerRequest, waitForNegativeProximity);
        this.mNotifier.onScreenPolicyUpdate(this.mGroupId, this.mDisplayPowerRequest.policy);
        return ready;
    }
}
