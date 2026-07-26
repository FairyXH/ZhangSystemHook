package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class Notifier {
    private static final boolean DEBUG = false;
    private static final int INTERACTIVE_STATE_ASLEEP = 2;
    private static final int INTERACTIVE_STATE_AWAKE = 1;
    private static final int INTERACTIVE_STATE_UNKNOWN = 0;
    private static final int MSG_BROADCAST = 2;
    private static final int MSG_BROADCAST_ENHANCED_PREDICTION = 4;
    private static final int MSG_PROFILE_TIMED_OUT = 5;
    private static final int MSG_SCREEN_POLICY = 7;
    private static final int MSG_USER_ACTIVITY = 1;
    private static final int MSG_WIRED_CHARGING_STARTED = 6;
    private static final int MSG_WIRELESS_CHARGING_STARTED = 3;
    private static final java.lang.String TAG = "PowerManagerNotifier";
    private final android.app.AppOpsManager mAppOps;
    private final java.util.concurrent.Executor mBackgroundExecutor;
    private final com.android.internal.app.IBatteryStats mBatteryStats;
    private boolean mBroadcastInProgress;
    private long mBroadcastStartTime;
    private int mBroadcastedInteractiveState;
    private final android.content.Context mContext;
    private final com.android.server.power.FaceDownDetector mFaceDownDetector;
    private final com.android.server.power.feature.PowerManagerFlags mFlags;
    private final com.android.server.power.Notifier.NotifierHandler mHandler;
    private final com.android.server.power.Notifier.Injector mInjector;
    private boolean mPendingGoToSleepBroadcast;
    private int mPendingInteractiveState;
    private boolean mPendingWakeUpBroadcast;
    private final com.android.server.policy.WindowManagerPolicy mPolicy;
    private int mProcessPid;
    private final android.content.Intent mScreenOffIntent;
    private final android.os.Bundle mScreenOnOffOptions;
    private final com.android.server.power.ScreenUndimDetector mScreenUndimDetector;
    private final boolean mShowWirelessChargingAnimationConfig;
    private final com.android.server.power.SuspendBlocker mSuspendBlocker;
    private final boolean mSuspendWhenScreenOffDueToProximityConfig;
    private final android.app.trust.TrustManager mTrustManager;
    private boolean mUserActivityPending;
    private final android.os.Vibrator mVibrator;
    private final com.android.server.power.WakeLockLog mWakeLockLog;
    private final com.android.server.power.WakefulnessSessionObserver mWakefulnessSessionObserver;
    static boolean DEBUG_PANIC = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final long[] CHARGING_VIBRATION_TIME = {40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40};
    private static final int[] CHARGING_VIBRATION_AMPLITUDE = {1, 4, 11, 25, 44, 67, 91, 114, 123, 103, 79, 55, 34, 17, 7, 2};
    private static final android.os.VibrationEffect CHARGING_VIBRATION_EFFECT = android.os.VibrationEffect.createWaveform(CHARGING_VIBRATION_TIME, CHARGING_VIBRATION_AMPLITUDE, -1);
    private static final android.os.VibrationAttributes HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES = android.os.VibrationAttributes.createForUsage(50);
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<com.android.server.power.Notifier.Interactivity> mInteractivityByGroupId = new android.util.SparseArray<>();
    private com.android.server.power.Notifier.Interactivity mGlobalInteractivity = new com.android.server.power.Notifier.Interactivity();
    private final java.util.concurrent.atomic.AtomicBoolean mIsPlayingChargingStartedFeedback = new java.util.concurrent.atomic.AtomicBoolean(false);
    private final android.content.IIntentReceiver mWakeUpBroadcastDone = new android.content.IIntentReceiver.Stub() { // from class: com.android.server.power.Notifier.2
        public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
            if (com.android.server.power.Notifier.DEBUG_PANIC) {
                android.util.Slog.d(com.android.server.power.Notifier.TAG, "mWakeUpBroadcastDone - sendNextBroadcast");
            }
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.POWER_SCREEN_BROADCAST_DONE, 1, java.lang.Long.valueOf(android.os.SystemClock.uptimeMillis() - com.android.server.power.Notifier.this.mBroadcastStartTime), 1);
            com.android.server.power.Notifier.this.sendNextBroadcast();
        }
    };
    private final android.content.IIntentReceiver mGoToSleepBroadcastDone = new android.content.IIntentReceiver.Stub() { // from class: com.android.server.power.Notifier.3
        public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
            if (com.android.server.power.Notifier.DEBUG_PANIC) {
                android.util.Slog.d(com.android.server.power.Notifier.TAG, "mGoToSleepBroadcastDone - sendNextBroadcast");
            }
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.POWER_SCREEN_BROADCAST_DONE, 0, java.lang.Long.valueOf(android.os.SystemClock.uptimeMillis() - com.android.server.power.Notifier.this.mBroadcastStartTime), 1);
            com.android.server.power.Notifier.this.sendNextBroadcast();
        }
    };
    private com.android.server.power.Notifier.NotifierWrapper mNotifierWrapper = new com.android.server.power.Notifier.NotifierWrapper();
    private com.android.server.power.INotifierExt mNotifierExt = (com.android.server.power.INotifierExt) system.ext.loader.core.ExtLoader.type(com.android.server.power.INotifierExt.class).base(this).create();
    private final android.app.ActivityManagerInternal mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
    private final com.android.server.input.InputManagerInternal mInputManagerInternal = (com.android.server.input.InputManagerInternal) com.android.server.LocalServices.getService(com.android.server.input.InputManagerInternal.class);
    private final com.android.server.inputmethod.InputMethodManagerInternal mInputMethodManagerInternal = (com.android.server.inputmethod.InputMethodManagerInternal) com.android.server.LocalServices.getService(com.android.server.inputmethod.InputMethodManagerInternal.class);
    private final com.android.server.statusbar.StatusBarManagerInternal mStatusBarManagerInternal = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
    private final android.hardware.display.DisplayManagerInternal mDisplayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
    private final android.content.Intent mScreenOnIntent = new android.content.Intent("android.intent.action.SCREEN_ON");

    public interface Injector {
        long currentTimeMillis();

        com.android.server.power.WakeLockLog getWakeLockLog(android.content.Context context);
    }

    private static class Interactivity {
        public int changeReason;
        public long changeStartTime;
        public boolean isChanging;
        public boolean isInteractive;

        private Interactivity() {
            this.isInteractive = true;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Notifier(android.os.Looper looper, android.content.Context context, com.android.internal.app.IBatteryStats iBatteryStats, com.android.server.power.SuspendBlocker suspendBlocker, com.android.server.policy.WindowManagerPolicy windowManagerPolicy, com.android.server.power.FaceDownDetector faceDownDetector, com.android.server.power.ScreenUndimDetector screenUndimDetector, java.util.concurrent.Executor executor, com.android.server.power.feature.PowerManagerFlags powerManagerFlags, com.android.server.power.Notifier.Injector injector) {
        this.mContext = context;
        this.mFlags = powerManagerFlags;
        this.mBatteryStats = iBatteryStats;
        this.mAppOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        this.mSuspendBlocker = suspendBlocker;
        this.mPolicy = windowManagerPolicy;
        this.mFaceDownDetector = faceDownDetector;
        this.mScreenUndimDetector = screenUndimDetector;
        this.mWakefulnessSessionObserver = new com.android.server.power.WakefulnessSessionObserver(this.mContext, null);
        this.mTrustManager = (android.app.trust.TrustManager) this.mContext.getSystemService(android.app.trust.TrustManager.class);
        this.mVibrator = (android.os.Vibrator) this.mContext.getSystemService(android.os.Vibrator.class);
        this.mHandler = new com.android.server.power.Notifier.NotifierHandler(looper);
        this.mBackgroundExecutor = executor;
        this.mScreenOnIntent.addFlags(1344274432);
        this.mScreenOnIntent.addFlags(134217728);
        this.mScreenOffIntent = new android.content.Intent("android.intent.action.SCREEN_OFF");
        this.mScreenOffIntent.addFlags(1344274432);
        this.mScreenOffIntent.addFlags(134217728);
        this.mScreenOnOffOptions = createScreenOnOffBroadcastOptions();
        this.mProcessPid = android.os.Process.myPid();
        this.mSuspendWhenScreenOffDueToProximityConfig = context.getResources().getBoolean(android.R.bool.config_startDreamImmediatelyOnDock);
        this.mShowWirelessChargingAnimationConfig = context.getResources().getBoolean(android.R.bool.config_repairModeSupported);
        this.mInjector = injector == null ? new com.android.server.power.Notifier.RealInjector() : injector;
        this.mWakeLockLog = this.mInjector.getWakeLockLog(context);
        try {
            this.mBatteryStats.noteInteractive(true);
        } catch (android.os.RemoteException e) {
        }
        com.android.internal.util.FrameworkStatsLog.write(33, 1);
    }

    private android.os.Bundle createScreenOnOffBroadcastOptions() {
        android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
        options.setDeliveryGroupPolicy(1);
        options.setDeliveryGroupMatchingKey(java.util.UUID.randomUUID().toString(), "android.intent.action.SCREEN_ON");
        options.setDeferralPolicy(2);
        return options.toBundle();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onWakeLockAcquired(int i, java.lang.String str, java.lang.String str2, int i2, int i3, android.os.WorkSource workSource, java.lang.String str3, android.os.IWakeLockCallback iWakeLockCallback) {
        boolean z = i2;
        notifyWakeLockListener(iWakeLockCallback, str, true, i2, i);
        int batteryStatsWakeLockMonitorType = getBatteryStatsWakeLockMonitorType(i);
        if (batteryStatsWakeLockMonitorType >= 0) {
            boolean z2 = z == 1000 && (1073741824 & i) != 0;
            try {
                if (workSource != null) {
                    this.mBatteryStats.noteStartWakelockFromSource(workSource, i3, str, str3, batteryStatsWakeLockMonitorType, z2);
                } else {
                    z = z2;
                    try {
                        this.mBatteryStats.noteStartWakelock(i2, i3, str, str3, batteryStatsWakeLockMonitorType, z);
                        try {
                            this.mAppOps.startOpNoThrow(40, z ? 1 : 0, str2);
                        } catch (android.os.RemoteException e) {
                        }
                    } catch (android.os.RemoteException e2) {
                    }
                }
            } catch (android.os.RemoteException e3) {
            }
        }
        if (!this.mFlags.improveWakelockLatency()) {
            this.mWakeLockLog.onWakeLockAcquired(str, i2, i, -1L);
        }
        this.mWakefulnessSessionObserver.onWakeLockAcquired(i);
    }

    public void onLongPartialWakeLockStart(java.lang.String tag, int ownerUid, android.os.WorkSource workSource, java.lang.String historyTag) {
        try {
            if (workSource != null) {
                this.mBatteryStats.noteLongPartialWakelockStartFromSource(tag, historyTag, workSource);
                com.android.internal.util.FrameworkStatsLog.write(11, workSource, tag, historyTag, 1);
            } else {
                this.mBatteryStats.noteLongPartialWakelockStart(tag, historyTag, ownerUid);
                com.android.internal.util.FrameworkStatsLog.write_non_chained(11, ownerUid, (java.lang.String) null, tag, historyTag, 1);
            }
        } catch (android.os.RemoteException e) {
        }
    }

    public void onLongPartialWakeLockFinish(java.lang.String tag, int ownerUid, android.os.WorkSource workSource, java.lang.String historyTag) {
        try {
            if (workSource != null) {
                this.mBatteryStats.noteLongPartialWakelockFinishFromSource(tag, historyTag, workSource);
                com.android.internal.util.FrameworkStatsLog.write(11, workSource, tag, historyTag, 0);
            } else {
                this.mBatteryStats.noteLongPartialWakelockFinish(tag, historyTag, ownerUid);
                com.android.internal.util.FrameworkStatsLog.write_non_chained(11, ownerUid, (java.lang.String) null, tag, historyTag, 0);
            }
        } catch (android.os.RemoteException e) {
        }
    }

    public void onWakeLockChanging(int flags, java.lang.String tag, java.lang.String packageName, int ownerUid, int ownerPid, android.os.WorkSource workSource, java.lang.String historyTag, android.os.IWakeLockCallback callback, int newFlags, java.lang.String newTag, java.lang.String newPackageName, int newOwnerUid, int newOwnerPid, android.os.WorkSource newWorkSource, java.lang.String newHistoryTag, android.os.IWakeLockCallback newCallback) {
        int monitorType = getBatteryStatsWakeLockMonitorType(flags);
        int newMonitorType = getBatteryStatsWakeLockMonitorType(newFlags);
        if (workSource != null && newWorkSource != null && monitorType >= 0 && newMonitorType >= 0) {
            boolean unimportantForLogging = newOwnerUid == 1000 && (1073741824 & newFlags) != 0;
            try {
                this.mBatteryStats.noteChangeWakelockFromSource(workSource, ownerPid, tag, historyTag, monitorType, newWorkSource, newOwnerPid, newTag, newHistoryTag, newMonitorType, unimportantForLogging);
            } catch (android.os.RemoteException e) {
            }
        } else if (!com.android.server.power.PowerManagerService.isSameCallback(callback, newCallback)) {
            onWakeLockReleased(flags, tag, packageName, ownerUid, ownerPid, workSource, historyTag, null);
            onWakeLockAcquired(newFlags, newTag, newPackageName, newOwnerUid, newOwnerPid, newWorkSource, newHistoryTag, newCallback);
        } else {
            onWakeLockReleased(flags, tag, packageName, ownerUid, ownerPid, workSource, historyTag, callback);
            onWakeLockAcquired(newFlags, newTag, newPackageName, newOwnerUid, newOwnerPid, newWorkSource, newHistoryTag, newCallback);
        }
    }

    public void onWakeLockReleased(int flags, java.lang.String tag, java.lang.String packageName, int ownerUid, int ownerPid, android.os.WorkSource workSource, java.lang.String historyTag, android.os.IWakeLockCallback callback) {
        onWakeLockReleased(flags, tag, packageName, ownerUid, ownerPid, workSource, historyTag, callback, -1);
    }

    public void onWakeLockReleased(int flags, java.lang.String tag, java.lang.String packageName, int ownerUid, int ownerPid, android.os.WorkSource workSource, java.lang.String historyTag, android.os.IWakeLockCallback callback, int releaseReason) {
        notifyWakeLockListener(callback, tag, false, ownerUid, flags);
        int monitorType = getBatteryStatsWakeLockMonitorType(flags);
        if (monitorType >= 0) {
            try {
                if (workSource != null) {
                    this.mBatteryStats.noteStopWakelockFromSource(workSource, ownerPid, tag, historyTag, monitorType);
                } else {
                    this.mBatteryStats.noteStopWakelock(ownerUid, ownerPid, tag, historyTag, monitorType);
                    try {
                        this.mAppOps.finishOp(40, ownerUid, packageName);
                    } catch (android.os.RemoteException e) {
                    }
                }
            } catch (android.os.RemoteException e2) {
            }
        }
        if (!this.mFlags.improveWakelockLatency()) {
            this.mWakeLockLog.onWakeLockReleased(tag, ownerUid, -1L);
        }
        this.mWakefulnessSessionObserver.onWakeLockReleased(flags, releaseReason);
    }

    public void showDismissibleKeyguard() {
        this.mPolicy.showDismissibleKeyguard();
    }

    private int getBatteryStatsWakeLockMonitorType(int flags) {
        switch (65535 & flags) {
            case 1:
                break;
            case 6:
            case 10:
                break;
            case 32:
                if (this.mSuspendWhenScreenOffDueToProximityConfig) {
                }
                break;
            case 64:
                break;
            case 128:
                break;
        }
        return -1;
    }

    public void onGlobalWakefulnessChangeStarted(final int wakefulness, int reason, long eventTime) {
        int i;
        boolean interactive = android.os.PowerManagerInternal.isInteractive(wakefulness);
        this.mNotifierExt.notifyOnWakefulnessChangeStartedEnter(interactive, reason);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.Notifier.1
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.power.Notifier.this.mActivityManagerInternal.onWakefulnessChanged(wakefulness);
                com.android.server.power.Notifier.this.mNotifierExt.noteSysStateChanged(android.os.PowerManagerInternal.isInteractive(wakefulness) ? 1 : 0, 1);
                com.android.server.power.Notifier.this.mNotifierExt.onWakefulnessChanged(wakefulness);
            }
        });
        if (this.mGlobalInteractivity.isInteractive != interactive) {
            if (this.mGlobalInteractivity.isChanging) {
                handleLateGlobalInteractiveChange();
            }
            if (this.mNotifierExt.isNeedActiveInput()) {
                this.mInputManagerInternal.setInteractive(interactive);
            }
            this.mInputMethodManagerInternal.setInteractive(interactive);
            try {
                this.mBatteryStats.noteInteractive(interactive);
            } catch (android.os.RemoteException e) {
            }
            if (interactive) {
                i = 1;
            } else {
                i = 0;
            }
            com.android.internal.util.FrameworkStatsLog.write(33, i);
            this.mGlobalInteractivity.isInteractive = interactive;
            this.mGlobalInteractivity.isChanging = true;
            this.mGlobalInteractivity.changeReason = reason;
            this.mGlobalInteractivity.changeStartTime = eventTime;
            handleEarlyGlobalInteractiveChange();
        }
    }

    public void onWakefulnessChangeFinished() {
        this.mNotifierExt.notifyOnWakefulnessChangeFinishedEnter(this.mInputManagerInternal, this.mGlobalInteractivity.isInteractive, this.mGlobalInteractivity.isChanging);
        for (int i = 0; i < this.mInteractivityByGroupId.size(); i++) {
            int groupId = this.mInteractivityByGroupId.keyAt(i);
            com.android.server.power.Notifier.Interactivity interactivity = this.mInteractivityByGroupId.valueAt(i);
            if (interactivity.isChanging) {
                interactivity.isChanging = false;
                handleLateInteractiveChange(groupId);
            }
        }
        if (this.mGlobalInteractivity.isChanging) {
            this.mGlobalInteractivity.isChanging = false;
            handleLateGlobalInteractiveChange();
        }
    }

    private void handleEarlyInteractiveChange(final int groupId) {
        synchronized (this.mLock) {
            com.android.server.power.Notifier.Interactivity interactivity = this.mInteractivityByGroupId.get(groupId);
            if (interactivity == null) {
                android.util.Slog.e(TAG, "no Interactivity entry for groupId:" + groupId);
                return;
            }
            final int changeReason = interactivity.changeReason;
            if (interactivity.isInteractive) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleEarlyInteractiveChange$0(groupId, changeReason);
                    }
                });
            } else {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleEarlyInteractiveChange$1(groupId, changeReason);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEarlyInteractiveChange$0(int groupId, int changeReason) {
        this.mPolicy.startedWakingUp(groupId, changeReason);
        if (groupId == 0) {
            this.mDisplayManagerInternal.onEarlyInteractivityChange(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEarlyInteractiveChange$1(int groupId, int changeReason) {
        this.mPolicy.startedGoingToSleep(groupId, changeReason);
        if (groupId == 0) {
            this.mDisplayManagerInternal.onEarlyInteractivityChange(false);
        }
    }

    private void handleEarlyGlobalInteractiveChange() {
        synchronized (this.mLock) {
            if (this.mGlobalInteractivity.isInteractive) {
                final int interactiveChangeOn = this.mGlobalInteractivity.changeReason;
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleEarlyGlobalInteractiveChange$2(interactiveChangeOn);
                    }
                });
                this.mPendingInteractiveState = 1;
                if (this.mNotifierExt.handleEarlyInteractiveChangeInActive()) {
                    return;
                }
                this.mPendingWakeUpBroadcast = true;
                updatePendingBroadcastLocked();
            } else {
                final int interactiveChangeOff = this.mGlobalInteractivity.changeReason;
                if (DEBUG_PANIC) {
                    android.util.Slog.d(TAG, "startedGoingToSleep: offReason=" + android.view.WindowManagerPolicyConstants.offReasonToString(android.view.WindowManagerPolicyConstants.translateSleepReasonToOffReason(interactiveChangeOff)) + ", SleepReason = " + interactiveChangeOff);
                }
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleEarlyGlobalInteractiveChange$3(interactiveChangeOff);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEarlyGlobalInteractiveChange$2(int interactiveChangeOn) {
        this.mDisplayManagerInternal.onEarlyInteractivityChange(true);
        this.mPolicy.startedWakingUpGlobal(interactiveChangeOn);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEarlyGlobalInteractiveChange$3(int interactiveChangeOff) {
        this.mDisplayManagerInternal.onEarlyInteractivityChange(false);
        this.mPolicy.startedGoingToSleepGlobal(interactiveChangeOff);
    }

    private void handleLateGlobalInteractiveChange() {
        synchronized (this.mLock) {
            final int interactiveChangeLatency = (int) (android.os.SystemClock.uptimeMillis() - this.mGlobalInteractivity.changeStartTime);
            if (this.mGlobalInteractivity.isInteractive) {
                this.mNotifierExt.handleLateInteractiveChangeInActive();
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleLateGlobalInteractiveChange$4(interactiveChangeLatency);
                    }
                });
            } else {
                if (this.mUserActivityPending) {
                    this.mUserActivityPending = false;
                    this.mHandler.removeMessages(1);
                }
                final int offReason = android.view.WindowManagerPolicyConstants.translateSleepReasonToOffReason(this.mGlobalInteractivity.changeReason);
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleLateGlobalInteractiveChange$5(offReason, interactiveChangeLatency);
                    }
                });
                this.mPendingInteractiveState = 2;
                if (this.mNotifierExt.handleLateInteractiveChangeUnActive()) {
                    return;
                }
                this.mPendingGoToSleepBroadcast = true;
                updatePendingBroadcastLocked();
            }
            com.oplus.android.internal.util.OplusFrameworkStatsLog.write(com.android.bluetooth.BluetoothStatsLog.BLUETOOTH_CODE_PATH_COUNTER__KEY__L2CAP_CONFIG_RSP_NEG, java.lang.System.currentTimeMillis(), this.mGlobalInteractivity.isInteractive, interactiveChangeLatency, this.mGlobalInteractivity.changeReason, this.mProcessPid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLateGlobalInteractiveChange$4(int interactiveChangeLatency) {
        android.metrics.LogMaker log = new android.metrics.LogMaker(com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_USB_DATA_SIGNALING);
        log.setType(1);
        log.setSubtype(android.view.WindowManagerPolicyConstants.translateWakeReasonToOnReason(this.mGlobalInteractivity.changeReason));
        log.setLatency(interactiveChangeLatency);
        log.addTaggedData(1694, java.lang.Integer.valueOf(this.mGlobalInteractivity.changeReason));
        com.android.internal.logging.MetricsLogger.action(log);
        com.android.server.EventLogTags.writePowerScreenState(1, 0, 0L, 0, interactiveChangeLatency);
        this.mPolicy.finishedWakingUpGlobal(this.mGlobalInteractivity.changeReason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLateGlobalInteractiveChange$5(int offReason, int interactiveChangeLatency) {
        android.metrics.LogMaker log = new android.metrics.LogMaker(com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__SET_USB_DATA_SIGNALING);
        log.setType(2);
        log.setSubtype(offReason);
        log.setLatency(interactiveChangeLatency);
        log.addTaggedData(1695, java.lang.Integer.valueOf(this.mGlobalInteractivity.changeReason));
        com.android.internal.logging.MetricsLogger.action(log);
        com.android.server.EventLogTags.writePowerScreenState(0, offReason, 0L, 0, interactiveChangeLatency);
        this.mPolicy.finishedGoingToSleepGlobal(this.mGlobalInteractivity.changeReason);
    }

    private void handleLateInteractiveChange(final int groupId) {
        synchronized (this.mLock) {
            com.android.server.power.Notifier.Interactivity interactivity = this.mInteractivityByGroupId.get(groupId);
            if (interactivity == null) {
                android.util.Slog.e(TAG, "no Interactivity entry for groupId:" + groupId);
                return;
            }
            final int changeReason = interactivity.changeReason;
            if (interactivity.isInteractive) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleLateInteractiveChange$6(groupId, changeReason);
                    }
                });
            } else {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleLateInteractiveChange$7(groupId, changeReason);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLateInteractiveChange$6(int groupId, int changeReason) {
        this.mPolicy.finishedWakingUp(groupId, changeReason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLateInteractiveChange$7(int groupId, int changeReason) {
        this.mPolicy.finishedGoingToSleep(groupId, changeReason);
    }

    public void onGroupWakefulnessChangeStarted(int groupId, int wakefulness, int changeReason, long eventTime) {
        boolean isInteractive = android.os.PowerManagerInternal.isInteractive(wakefulness);
        boolean isNewGroup = false;
        com.android.server.power.Notifier.Interactivity interactivity = this.mInteractivityByGroupId.get(groupId);
        if (interactivity == null) {
            isNewGroup = true;
            interactivity = new com.android.server.power.Notifier.Interactivity();
            this.mInteractivityByGroupId.put(groupId, interactivity);
        }
        if (isNewGroup || interactivity.isInteractive != isInteractive) {
            if (interactivity.isChanging) {
                handleLateInteractiveChange(groupId);
            }
            interactivity.isInteractive = isInteractive;
            interactivity.changeReason = changeReason;
            interactivity.changeStartTime = eventTime;
            interactivity.isChanging = true;
            handleEarlyInteractiveChange(groupId);
            this.mWakefulnessSessionObserver.onWakefulnessChangeStarted(groupId, wakefulness, changeReason, eventTime);
        }
    }

    public void onGroupRemoved(int groupId) {
        this.mInteractivityByGroupId.remove(groupId);
        this.mWakefulnessSessionObserver.removePowerGroup(groupId);
    }

    public void onUserActivity(int displayGroupId, int event, int uid) {
        try {
            this.mBatteryStats.noteUserActivity(uid, event);
            this.mWakefulnessSessionObserver.notifyUserActivity(android.os.SystemClock.uptimeMillis(), displayGroupId, event);
        } catch (android.os.RemoteException e) {
        }
        synchronized (this.mLock) {
            if (!this.mUserActivityPending) {
                this.mUserActivityPending = true;
                android.os.Message msg = this.mHandler.obtainMessage(1);
                msg.arg1 = displayGroupId;
                msg.arg2 = event;
                msg.setAsynchronous(true);
                this.mHandler.sendMessage(msg);
            }
        }
    }

    public void onWakeUp(int reason, java.lang.String details, int reasonUid, java.lang.String opPackageName, int opUid) {
        try {
            this.mBatteryStats.noteWakeUp(details, reasonUid);
            if (opPackageName != null) {
                this.mAppOps.noteOpNoThrow(61, opUid, opPackageName);
            }
        } catch (android.os.RemoteException e) {
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DISPLAY_WAKE_REPORTED, reason, reasonUid);
    }

    public void onProfileTimeout(int userId) {
        android.os.Message msg = this.mHandler.obtainMessage(5);
        msg.setAsynchronous(true);
        msg.arg1 = userId;
        this.mHandler.sendMessage(msg);
    }

    public void onWirelessChargingStarted(int batteryLevel, int userId) {
        this.mSuspendBlocker.acquire();
        android.os.Message msg = this.mHandler.obtainMessage(3);
        msg.setAsynchronous(true);
        msg.arg1 = batteryLevel;
        msg.arg2 = userId;
        this.mHandler.sendMessage(msg);
    }

    public void onWiredChargingStarted(int userId) {
        this.mSuspendBlocker.acquire();
        android.os.Message msg = this.mHandler.obtainMessage(6);
        msg.setAsynchronous(true);
        msg.arg1 = userId;
        this.mHandler.sendMessage(msg);
    }

    public void onScreenPolicyUpdate(int displayGroupId, int newPolicy) {
        synchronized (this.mLock) {
            android.os.Message msg = this.mHandler.obtainMessage(7);
            msg.arg1 = displayGroupId;
            msg.arg2 = newPolicy;
            msg.setAsynchronous(true);
            this.mHandler.sendMessage(msg);
        }
    }

    public void dump(java.io.PrintWriter pw) {
        if (this.mWakeLockLog != null) {
            this.mWakeLockLog.dump(pw);
        }
        this.mWakefulnessSessionObserver.dump(pw);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePendingBroadcastLocked() {
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, "updatePendingBroadcastLocked mBroadcastInProgress = " + this.mBroadcastInProgress + ", mPendingInteractiveState = " + this.mPendingInteractiveState + ", mPendingWakeUpBroadcast = " + this.mPendingWakeUpBroadcast + ", mPendingGoToSleepBroadcast = " + this.mPendingGoToSleepBroadcast + ", mBroadcastedInteractiveState = " + this.mBroadcastedInteractiveState);
        }
        if (this.mBroadcastInProgress || this.mPendingInteractiveState == 0) {
            return;
        }
        if (this.mPendingWakeUpBroadcast || this.mPendingGoToSleepBroadcast || this.mPendingInteractiveState != this.mBroadcastedInteractiveState) {
            this.mBroadcastInProgress = true;
            this.mSuspendBlocker.acquire();
            android.os.Message msg = this.mHandler.obtainMessage(2);
            msg.setAsynchronous(true);
            this.mHandler.sendMessage(msg);
            this.mNotifierExt.updatePendingBroadcastLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishPendingBroadcastLocked() {
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, "finishPendingBroadcastLocked");
        }
        this.mBroadcastInProgress = false;
        this.mSuspendBlocker.release();
        this.mNotifierExt.finishPendingBroadcastLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUserActivity(int displayGroupId, int event) {
        synchronized (this.mLock) {
            if (this.mUserActivityPending) {
                this.mUserActivityPending = false;
                android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
                tm.notifyUserActivity();
                this.mInputManagerInternal.notifyUserActivity();
                this.mPolicy.userActivity(displayGroupId, event);
                this.mFaceDownDetector.userActivity(event);
                this.mScreenUndimDetector.userActivity(displayGroupId);
            }
        }
    }

    void postEnhancedDischargePredictionBroadcast(long delayMs) {
        this.mHandler.sendEmptyMessageDelayed(4, delayMs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendEnhancedDischargePredictionBroadcast() {
        android.content.Intent intent = new android.content.Intent("android.os.action.ENHANCED_DISCHARGE_PREDICTION_CHANGED").addFlags(1073741824);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendNextBroadcast() {
        synchronized (this.mLock) {
            if (DEBUG_PANIC) {
                android.util.Slog.d(TAG, "sendNextBroadcast, mBroadcastedInteractiveState=" + this.mBroadcastedInteractiveState + ", mPendingInteractiveState=" + this.mPendingInteractiveState + ", mPendingWakeUpBroadcast=" + this.mPendingWakeUpBroadcast + ", mPendingGoToSleepBroadcast=" + this.mPendingGoToSleepBroadcast + ", mSkipWakeUpBroadcast=" + this.mNotifierExt.isSkipWakeupBroadcast() + ", mSkipGoToSleepBroadcast=" + this.mNotifierExt.isSkipGotoSleepBroadcast());
            }
            if (this.mBroadcastedInteractiveState == 0) {
                switch (this.mPendingInteractiveState) {
                    case 2:
                        this.mPendingGoToSleepBroadcast = false;
                        this.mBroadcastedInteractiveState = 2;
                        break;
                    default:
                        this.mPendingWakeUpBroadcast = false;
                        this.mBroadcastedInteractiveState = 1;
                        break;
                }
            } else if (this.mBroadcastedInteractiveState == 1) {
                if (!this.mPendingWakeUpBroadcast && !this.mPendingGoToSleepBroadcast && this.mPendingInteractiveState != 2) {
                    finishPendingBroadcastLocked();
                    return;
                }
                this.mPendingGoToSleepBroadcast = false;
                this.mBroadcastedInteractiveState = 2;
            } else {
                if (!this.mPendingWakeUpBroadcast && !this.mPendingGoToSleepBroadcast && (this.mPendingInteractiveState != 1 || this.mNotifierExt.isSkipWakeupBroadcast())) {
                    finishPendingBroadcastLocked();
                    return;
                }
                this.mPendingWakeUpBroadcast = false;
                this.mBroadcastedInteractiveState = 1;
            }
            this.mBroadcastStartTime = android.os.SystemClock.uptimeMillis();
            int powerState = this.mBroadcastedInteractiveState;
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.POWER_SCREEN_BROADCAST_SEND, 1);
            if (powerState == 1) {
                sendWakeUpBroadcast();
            } else {
                sendGoToSleepBroadcast();
            }
        }
    }

    private void sendWakeUpBroadcast() {
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, "Sending wake up broadcast.");
        }
        if (this.mActivityManagerInternal.isSystemReady()) {
            this.mNotifierExt.notifyScreenOnOff(true);
            this.mActivityManagerInternal.broadcastIntentWithCallback(this.mScreenOnIntent, this.mWakeUpBroadcastDone, (java.lang.String[]) null, -1, (int[]) null, (java.util.function.BiFunction) null, this.mScreenOnOffOptions);
        } else {
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.POWER_SCREEN_BROADCAST_STOP, 2, 1);
            sendNextBroadcast();
        }
    }

    private void sendGoToSleepBroadcast() {
        if (DEBUG_PANIC) {
            android.util.Slog.d(TAG, "Sending go to sleep broadcast.");
        }
        if (this.mActivityManagerInternal.isSystemReady()) {
            this.mNotifierExt.notifyScreenOnOff(false);
            this.mActivityManagerInternal.broadcastIntentWithCallback(this.mScreenOffIntent, this.mGoToSleepBroadcastDone, (java.lang.String[]) null, -1, (int[]) null, (java.util.function.BiFunction) null, this.mScreenOnOffOptions);
        } else {
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.POWER_SCREEN_BROADCAST_STOP, 3, 1);
            sendNextBroadcast();
        }
    }

    private void playChargingStartedFeedback(final int userId, final boolean wireless) {
        if (this.mNotifierExt.playChargingStartedFeedback() || !isChargingFeedbackEnabled(userId) || !this.mIsPlayingChargingStartedFeedback.compareAndSet(false, true)) {
            return;
        }
        this.mBackgroundExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$playChargingStartedFeedback$8(userId, wireless);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playChargingStartedFeedback$8(int userId, boolean wireless) {
        android.media.Ringtone sfx;
        boolean vibrate = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "charging_vibration_enabled", 1, userId) != 0;
        if (vibrate) {
            this.mVibrator.vibrate(1000, this.mContext.getOpPackageName(), CHARGING_VIBRATION_EFFECT, "Charging started", HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES);
        }
        java.lang.String soundPath = android.provider.Settings.Global.getString(this.mContext.getContentResolver(), wireless ? "wireless_charging_started_sound" : "charging_started_sound");
        android.net.Uri soundUri = android.net.Uri.parse("file://" + soundPath);
        if (soundUri != null && (sfx = android.media.RingtoneManager.getRingtone(this.mContext, soundUri)) != null) {
            sfx.setStreamType(1);
            sfx.play();
        }
        this.mIsPlayingChargingStartedFeedback.set(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWirelessChargingStarted(int batteryLevel, int userId) {
        playChargingStartedFeedback(userId, true);
        if (this.mShowWirelessChargingAnimationConfig && this.mStatusBarManagerInternal != null) {
            this.mStatusBarManagerInternal.showChargingAnimation(batteryLevel);
        }
        this.mSuspendBlocker.release();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWiredChargingStarted(int userId) {
        playChargingStartedFeedback(userId, false);
        this.mSuspendBlocker.release();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void screenPolicyChanging(int displayGroupId, int screenPolicy) {
        this.mScreenUndimDetector.recordScreenPolicy(displayGroupId, screenPolicy);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lockProfile(int userId) {
        this.mTrustManager.setDeviceLockedForUser(userId, true);
    }

    private boolean isChargingFeedbackEnabled(int userId) {
        boolean enabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "charging_sounds_enabled", 1, userId) != 0;
        boolean dndOff = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "zen_mode", 1) == 0;
        return enabled && dndOff;
    }

    private void notifyWakeLockListener(final android.os.IWakeLockCallback callback, final java.lang.String tag, final boolean isEnabled, final int ownerUid, final int flags) {
        if (callback != null) {
            final long currentTime = this.mInjector.currentTimeMillis();
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyWakeLockListener$9(isEnabled, tag, ownerUid, flags, currentTime, callback);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyWakeLockListener$9(boolean isEnabled, java.lang.String tag, int ownerUid, int flags, long currentTime, android.os.IWakeLockCallback callback) {
        try {
            if (this.mFlags.improveWakelockLatency()) {
                if (isEnabled) {
                    this.mWakeLockLog.onWakeLockAcquired(tag, ownerUid, flags, currentTime);
                } else {
                    this.mWakeLockLog.onWakeLockReleased(tag, ownerUid, currentTime);
                }
            }
            callback.onStateChanged(isEnabled);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Wakelock.mCallback [" + tag + "] is already dead.", e);
        }
    }

    private final class NotifierHandler extends android.os.Handler {
        public NotifierHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.power.Notifier.this.sendUserActivity(msg.arg1, msg.arg2);
                    break;
                case 2:
                    com.android.server.power.Notifier.this.sendNextBroadcast();
                    break;
                case 3:
                    com.android.server.power.Notifier.this.showWirelessChargingStarted(msg.arg1, msg.arg2);
                    break;
                case 4:
                    removeMessages(4);
                    com.android.server.power.Notifier.this.sendEnhancedDischargePredictionBroadcast();
                    break;
                case 5:
                    com.android.server.power.Notifier.this.lockProfile(msg.arg1);
                    break;
                case 6:
                    com.android.server.power.Notifier.this.showWiredChargingStarted(msg.arg1);
                    break;
                case 7:
                    com.android.server.power.Notifier.this.screenPolicyChanging(msg.arg1, msg.arg2);
                    break;
            }
        }
    }

    static class RealInjector implements com.android.server.power.Notifier.Injector {
        RealInjector() {
        }

        @Override // com.android.server.power.Notifier.Injector
        public long currentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }

        @Override // com.android.server.power.Notifier.Injector
        public com.android.server.power.WakeLockLog getWakeLockLog(android.content.Context context) {
            return new com.android.server.power.WakeLockLog(context);
        }
    }

    public com.android.server.power.INotifierWrapper getWrapper() {
        return this.mNotifierWrapper;
    }

    private class NotifierWrapper implements com.android.server.power.INotifierWrapper {
        private NotifierWrapper() {
        }

        @Override // com.android.server.power.INotifierWrapper
        public void setPendingWakeUpBroadcast(boolean value) {
            synchronized (com.android.server.power.Notifier.this.mLock) {
                com.android.server.power.Notifier.this.mPendingWakeUpBroadcast = value;
            }
        }

        @Override // com.android.server.power.INotifierWrapper
        public java.lang.Object getLock() {
            return com.android.server.power.Notifier.this.mLock;
        }

        @Override // com.android.server.power.INotifierWrapper
        public void updatePendingBroadcastLocked() {
            com.android.server.power.Notifier.this.updatePendingBroadcastLocked();
        }

        @Override // com.android.server.power.INotifierWrapper
        public void finishPendingBroadcastLocked() {
            com.android.server.power.Notifier.this.finishPendingBroadcastLocked();
        }
    }
}
