package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public final class PowerManagerService extends com.android.server.SystemService implements com.android.server.Watchdog.Monitor {
    private static final java.text.SimpleDateFormat DATE_FORMAT;
    static boolean DEBUG_PANIC = false;
    private static final android.util.IntArray DEFAULT_DISPLAY_GROUP_IDS;
    private static final int DEFAULT_DOUBLE_TAP_TO_WAKE = 0;
    static final int DEFAULT_SCREEN_OFF_TIMEOUT = 15000;
    private static final int DEFAULT_SLEEP_TIMEOUT = -1;
    private static final int DIRTY_ACTUAL_DISPLAY_POWER_STATE_UPDATED = 8;
    private static final int DIRTY_ATTENTIVE = 16384;
    private static final int DIRTY_BATTERY_STATE = 256;
    private static final int DIRTY_BOOT_COMPLETED = 16;
    private static final int DIRTY_DISPLAY_GROUP_WAKEFULNESS = 65536;
    private static final int DIRTY_DOCK_STATE = 1024;
    private static final int DIRTY_IS_POWERED = 64;
    private static final int DIRTY_PROXIMITY_POSITIVE = 512;
    private static final int DIRTY_QUIESCENT = 4096;
    private static final int DIRTY_SCREEN_BRIGHTNESS_BOOST = 2048;
    private static final int DIRTY_SETTINGS = 32;
    private static final int DIRTY_STAY_ON = 128;
    private static final int DIRTY_USER_ACTIVITY = 4;
    private static final int DIRTY_WAKEFULNESS = 2;
    private static final int DIRTY_WAKE_LOCKS = 1;
    private static final long ENHANCED_DISCHARGE_PREDICTION_BROADCAST_MIN_DELAY_MS = 60000;
    private static final long ENHANCED_DISCHARGE_PREDICTION_TIMEOUT_MS = 1800000;
    private static final int HALT_MODE_REBOOT = 1;
    private static final int HALT_MODE_REBOOT_SAFE_MODE = 2;
    private static final int HALT_MODE_SHUTDOWN = 0;
    private static final java.lang.String HOLDING_DISPLAY_SUSPEND_BLOCKER = "holding display";
    private static final float INVALID_BRIGHTNESS_IN_CONFIG = -2.0f;
    static final long MIN_LONG_WAKE_CHECK_INTERVAL = 60000;
    private static final int MSG_ATTENTIVE_TIMEOUT = 5;
    private static final int MSG_CHECK_FOR_LONG_WAKELOCKS = 4;
    private static final int MSG_RELEASE_ALL_OVERRIDE_WAKE_LOCKS = 6;
    private static final int MSG_SANDMAN = 2;
    private static final int MSG_SCREEN_BRIGHTNESS_BOOST_TIMEOUT = 3;
    private static final int MSG_USER_ACTIVITY_TIMEOUT = 1;
    private static final long ONE_DAY = 86400000;
    private static final java.lang.String REASON_BATTERY_THERMAL_STATE = "shutdown,thermal,battery";
    private static final java.lang.String REASON_LOW_BATTERY = "shutdown,battery";
    private static final java.lang.String REASON_REBOOT = "reboot";
    private static final java.lang.String REASON_SHUTDOWN = "shutdown";
    private static final java.lang.String REASON_THERMAL_SHUTDOWN = "shutdown,thermal";
    private static final java.lang.String REASON_USERREQUESTED = "shutdown,userrequested";
    public static final long REQUIRE_TURN_SCREEN_ON_PERMISSION = 216114297;
    private static final int SCREEN_BRIGHTNESS_BOOST_TIMEOUT = 5000;
    private static final int SCREEN_ON_LATENCY_WARNING_MS = 200;
    private static final java.lang.String SYSTEM_PROPERTY_QUIESCENT = "ro.boot.quiescent";
    private static final java.lang.String SYSTEM_PROPERTY_REBOOT_REASON = "sys.boot.reason";
    private static final java.lang.String SYSTEM_PROPERTY_RETAIL_DEMO_ENABLED = "sys.retaildemo.enabled";
    private static final java.lang.String TAG = "PowerManagerService";
    static final java.lang.String TRACE_SCREEN_ON = "Screen turning on";
    static final int USER_ACTIVITY_SCREEN_BRIGHT = 1;
    static final int USER_ACTIVITY_SCREEN_DIM = 2;
    static final int USER_ACTIVITY_SCREEN_DREAM = 4;
    static final int WAKE_LOCK_BUTTON_BRIGHT = 8;
    static final int WAKE_LOCK_CPU = 1;
    static final int WAKE_LOCK_DOZE = 64;
    static final int WAKE_LOCK_DRAW = 128;
    static final int WAKE_LOCK_PROXIMITY_SCREEN_OFF = 16;
    static final int WAKE_LOCK_SCREEN_BRIGHT = 2;
    static final int WAKE_LOCK_SCREEN_DIM = 4;
    static final int WAKE_LOCK_SCREEN_TIMEOUT_OVERRIDE = 256;
    static final int WAKE_LOCK_STAY_AWAKE = 32;
    static com.android.server.power.IPowerManagerServiceExt mPmsExt;
    private static android.os.IAnrLogEnhancementHelperExt sAnrLogEnhancementHelper;
    private static boolean sQuiescent;
    private boolean mAlwaysOnEnabled;
    private final android.hardware.display.AmbientDisplayConfiguration mAmbientDisplayConfiguration;
    private final com.android.server.power.AmbientDisplaySuppressionController mAmbientDisplaySuppressionController;
    private final com.android.server.power.AmbientDisplaySuppressionController.AmbientDisplaySuppressionChangedCallback mAmbientSuppressionChangedCallback;
    private final com.android.server.power.AttentionDetector mAttentionDetector;
    private com.android.server.lights.LogicalLight mAttentionLight;
    private int mAttentiveTimeoutConfig;
    private long mAttentiveTimeoutSetting;
    private long mAttentiveWarningDurationConfig;
    private int mBatteryLevel;
    private boolean mBatteryLevelLow;
    private android.os.BatteryManagerInternal mBatteryManagerInternal;
    private final com.android.server.power.batterysaver.BatterySaverStateMachine mBatterySaverStateMachine;
    private final boolean mBatterySaverSupported;
    private com.android.internal.app.IBatteryStats mBatteryStats;
    private final com.android.server.power.PowerManagerService.BinderService mBinderService;
    private boolean mBootCompleted;
    private final com.android.server.power.SuspendBlocker mBootingSuspendBlocker;
    private boolean mBrightWhenDozingConfig;
    private final com.android.server.power.PowerManagerService.Clock mClock;
    final com.android.server.power.PowerManagerService.Constants mConstants;
    private final android.content.Context mContext;
    private boolean mDecoupleHalAutoSuspendModeFromDisplayConfig;
    private boolean mDecoupleHalInteractiveModeFromDisplayConfig;
    private final com.android.server.display.feature.DeviceConfigParameterProvider mDeviceConfigProvider;
    private boolean mDeviceIdleMode;
    int[] mDeviceIdleTempWhitelist;
    int[] mDeviceIdleWhitelist;
    private int mDirty;
    private boolean mDisableScreenWakeLocksWhileCached;
    private android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    private final android.hardware.display.DisplayManagerInternal.DisplayPowerCallbacks mDisplayPowerCallbacks;
    private final com.android.server.power.SuspendBlocker mDisplaySuspendBlocker;
    private int mDockState;
    private boolean mDoubleTapWakeEnabled;
    private boolean mDozeAfterScreenOff;
    private int mDozeScreenBrightnessOverrideFromDreamManager;
    private float mDozeScreenBrightnessOverrideFromDreamManagerFloat;
    private int mDozeScreenStateOverrideFromDreamManager;
    private int mDozeScreenStateOverrideReasonFromDreamManager;
    private boolean mDozeStartInProgress;
    private boolean mDrawWakeLockOverrideFromSidekick;
    private android.service.dreams.DreamManagerInternal mDreamManager;
    private boolean mDreamsActivateOnDockSetting;
    private boolean mDreamsActivateOnSleepSetting;
    private boolean mDreamsActivatedOnDockByDefaultConfig;
    private boolean mDreamsActivatedOnSleepByDefaultConfig;
    private int mDreamsBatteryLevelDrain;
    private int mDreamsBatteryLevelDrainCutoffConfig;
    private int mDreamsBatteryLevelMinimumWhenNotPoweredConfig;
    private int mDreamsBatteryLevelMinimumWhenPoweredConfig;
    private boolean mDreamsDisabledByAmbientModeSuppressionConfig;
    private boolean mDreamsEnabledByDefaultConfig;
    private boolean mDreamsEnabledOnBatteryConfig;
    private boolean mDreamsEnabledSetting;
    private boolean mDreamsSupportedConfig;
    private boolean mEnhancedDischargePredictionIsPersonalized;
    private long mEnhancedDischargeTimeElapsed;
    private final java.lang.Object mEnhancedDischargeTimeLock;
    private final com.android.server.power.FaceDownDetector mFaceDownDetector;
    private final com.android.server.power.feature.PowerManagerFlags mFeatureFlags;
    private final com.android.internal.foldables.FoldGracePeriodProvider mFoldGracePeriodProvider;
    private boolean mForceSuspendActive;
    private int mForegroundProfile;
    private boolean mHalAutoSuspendModeEnabled;
    private boolean mHalInteractiveModeEnabled;
    private final android.os.Handler mHandler;
    private final com.android.server.ServiceThread mHandlerThread;
    private boolean mHoldingBootingSuspendBlocker;
    private boolean mHoldingDisplaySuspendBlocker;
    private boolean mHoldingWakeLockSuspendBlocker;
    private final com.android.server.power.InattentiveSleepWarningController mInattentiveSleepWarningOverlayController;
    private final com.android.server.power.PowerManagerService.Injector mInjector;
    private boolean mInterceptedPowerKeyForProximity;
    boolean mIsFaceDown;
    private boolean mIsPowered;
    private boolean mKeepDreamingWhenUnplugging;
    private long mLastEnhancedDischargeTimeUpdatedElapsed;
    private long mLastFlipTime;
    private int mLastGlobalSleepReason;
    private long mLastGlobalSleepTime;
    private long mLastGlobalSleepTimeRealtime;
    private int mLastGlobalWakeReason;
    private long mLastGlobalWakeTime;
    private long mLastGlobalWakeTimeRealtime;
    private long mLastInteractivePowerHintTime;
    private long mLastScreenBrightnessBoostTime;
    private long mLastWarningAboutUserActivityPermission;
    private boolean mLightDeviceIdleMode;
    private com.android.server.lights.LightsManager mLightsManager;
    private final com.android.server.power.PowerManagerService.LocalService mLocalService;
    private final java.lang.Object mLock;
    private boolean mLowPowerStandbyActive;
    int[] mLowPowerStandbyAllowlist;
    private final com.android.server.power.LowPowerStandbyController mLowPowerStandbyController;
    private long mMaximumScreenDimDurationConfig;
    private float mMaximumScreenDimRatioConfig;
    private long mMaximumScreenOffTimeoutFromDeviceAdmin;
    private long mMinimumScreenOffTimeoutConfig;
    private final com.android.server.power.PowerManagerService.NativeWrapper mNativeWrapper;
    private com.android.server.power.Notifier mNotifier;
    private long mNotifyLongDispatched;
    private long mNotifyLongNextCheck;
    private long mNotifyLongScheduled;
    private long mOverriddenTimeout;
    private final com.android.server.power.PowerManagerService.PermissionCheckerWrapper mPermissionCheckerWrapper;
    private int mPlugType;
    private com.android.server.power.PowerManagerService.PowerManagerServiceWrapper mPmsWrapper;
    private com.android.server.policy.WindowManagerPolicy mPolicy;
    private final com.android.server.power.PowerManagerService.PowerGroupWakefulnessChangeListener mPowerGroupWakefulnessChangeListener;
    private final android.util.SparseArray<com.android.server.power.PowerGroup> mPowerGroups;
    private final com.android.server.power.PowerManagerService.PowerPropertiesWrapper mPowerPropertiesWrapper;
    private final android.util.SparseArray<com.android.server.power.PowerManagerService.ProfilePowerState> mProfilePowerState;
    private boolean mProximityPositive;
    private boolean mRequestWaitForNegativeProximity;
    private boolean mSandmanScheduled;
    private boolean mScreenBrightnessBoostInProgress;
    public final float mScreenBrightnessDefault;
    public final float mScreenBrightnessDim;
    public final float mScreenBrightnessDoze;
    public final float mScreenBrightnessMaximum;
    public final float mScreenBrightnessMinimum;
    private float mScreenBrightnessOverrideFromWindowManager;
    private float mScreenBrightnessSettingDefault;
    private float mScreenBrightnessSettingMaximum;
    private float mScreenBrightnessSettingMinimum;
    private long mScreenOffTimeoutSetting;
    private com.android.server.power.ScreenTimeoutOverridePolicy mScreenTimeoutOverridePolicy;
    private final com.android.server.power.ScreenUndimDetector mScreenUndimDetector;
    private com.android.server.power.PowerManagerService.SettingsObserver mSettingsObserver;
    private long mSleepTimeoutSetting;
    private boolean mStayOn;
    private int mStayOnWhilePluggedInSetting;
    private boolean mSupportsDoubleTapWakeConfig;
    private final java.util.ArrayList<com.android.server.power.SuspendBlocker> mSuspendBlockers;
    private boolean mSuspendWhenScreenOffDueToProximityConfig;
    private final com.android.server.power.SystemPropertiesWrapper mSystemProperties;
    private boolean mSystemReady;
    private boolean mTheaterModeEnabled;
    private final android.util.SparseArray<com.android.server.power.PowerManagerService.UidState> mUidState;
    private boolean mUidsChanged;
    private boolean mUidsChanging;
    private boolean mUpdatePowerStateInProgress;
    private final boolean mUseAutoSuspend;
    private long mUserActivityTimeoutOverrideFromWindowManager;
    private int mUserId;
    private boolean mUserInactiveOverrideFromWindowManager;
    private int mWakeLockSummary;
    private final com.android.server.power.SuspendBlocker mWakeLockSuspendBlocker;
    private final java.util.ArrayList<com.android.server.power.PowerManagerService.WakeLock> mWakeLocks;
    private boolean mWakeUpWhenPluggedOrUnpluggedConfig;
    private boolean mWakeUpWhenPluggedOrUnpluggedInTheaterModeConfig;
    private boolean mWakefulnessChanging;
    private int mWakefulnessRaw;
    private com.android.server.power.WirelessChargerDetector mWirelessChargerDetector;
    static boolean DEBUG = false;
    static boolean DEBUG_SPEW = DEBUG;

    interface Clock {
        long elapsedRealtime();

        long uptimeMillis();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface HaltMode {
    }

    interface PermissionCheckerWrapper {
        int checkPermissionForDataDelivery(android.content.Context context, java.lang.String str, int i, android.content.AttributionSource attributionSource, java.lang.String str2);
    }

    interface PowerPropertiesWrapper {
        boolean permissionless_turn_screen_on();

        boolean waive_target_sdk_check_for_turn_screen_on();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeAcquireSuspendBlocker(java.lang.String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeForceSuspend();

    /* JADX INFO: Access modifiers changed from: private */
    public native void nativeInit();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeReleaseSuspendBlocker(java.lang.String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeSetAutoSuspend(boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeSetPowerBoost(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeSetPowerMode(int i, boolean z);

    static {
        DEBUG_PANIC = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false) && "0".equals(android.os.SystemProperties.get("persist.sys.agingtest", "0"));
        DATE_FORMAT = new java.text.SimpleDateFormat("MM-dd HH:mm:ss.SSS");
        DEFAULT_DISPLAY_GROUP_IDS = android.util.IntArray.wrap(new int[]{0});
        sAnrLogEnhancementHelper = (android.os.IAnrLogEnhancementHelperExt) system.ext.loader.core.ExtLoader.type(android.os.IAnrLogEnhancementHelperExt.class).create();
    }

    private final class DreamManagerStateListener implements android.service.dreams.DreamManagerInternal.DreamManagerStateListener {
        private DreamManagerStateListener() {
        }

        public void onKeepDreamingWhenUnpluggingChanged(boolean keepDreaming) {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                com.android.server.power.PowerManagerService.this.mKeepDreamingWhenUnplugging = keepDreaming;
            }
        }
    }

    private final class PowerGroupWakefulnessChangeListener implements com.android.server.power.PowerGroup.PowerGroupListener {
        private PowerGroupWakefulnessChangeListener() {
        }

        @Override // com.android.server.power.PowerGroup.PowerGroupListener
        public void onWakefulnessChangedLocked(int groupId, int wakefulness, long eventTime, int reason, int uid, int opUid, java.lang.String opPackageName, java.lang.String details) throws java.lang.Throwable {
            int oldWakefulness = com.android.server.power.PowerManagerService.this.getGlobalWakefulnessLocked();
            com.android.server.power.PowerManagerService.this.mWakefulnessChanging = true;
            com.android.server.power.PowerManagerService.this.mDirty |= 2;
            if (wakefulness == 1) {
                int flags = reason == 13 ? 1 : 0;
                com.android.server.power.PowerManagerService.this.userActivityNoUpdateLocked((com.android.server.power.PowerGroup) com.android.server.power.PowerManagerService.this.mPowerGroups.get(groupId), eventTime, 0, flags, uid);
            }
            if (com.android.server.power.PowerManagerService.this.mScreenTimeoutOverridePolicy != null && groupId == 0) {
                com.android.server.power.PowerManagerService.this.mScreenTimeoutOverridePolicy.onWakefulnessChange(com.android.server.power.PowerManagerService.this.mWakeLockSummary, wakefulness);
            }
            com.android.server.power.PowerManagerService.this.mDirty |= 65536;
            com.android.server.power.PowerManagerService.this.mNotifier.onGroupWakefulnessChangeStarted(groupId, wakefulness, reason, eventTime);
            com.android.server.power.PowerManagerService.this.updateGlobalWakefulnessLocked(eventTime, reason, uid, opUid, opPackageName, details);
            if (wakefulness == 1) {
                com.android.server.power.PowerManagerService.mPmsExt.wakeDisplayGroupNoUpdateLockedEnd(groupId, android.os.PowerManager.wakeReasonToString(reason));
                if (reason == 1) {
                    com.android.server.power.PowerManagerService.mPmsExt.notePowerkeyProcessStagePoint("POWER_wakeUpInternal");
                } else {
                    com.android.server.power.PowerManagerService.mPmsExt.notePowerkeyProcessStagePoint("CANCELED_wakeUpByOther");
                }
            } else if (wakefulness == 3 || (wakefulness == 0 && oldWakefulness != 3 && oldWakefulness != 0)) {
                com.android.server.power.PowerManagerService.mPmsExt.onSleepDisplayGroupNoUpdateLockedEnd(groupId, reason);
                if (reason == 4) {
                    com.android.server.power.PowerManagerService.mPmsExt.notePowerkeyProcessStagePoint("POWER_goToSleepInternal");
                } else {
                    com.android.server.power.PowerManagerService.mPmsExt.notePowerkeyProcessStagePoint("CANCELED_goToSleepByOther");
                }
            }
            com.android.server.power.PowerManagerService.this.updatePowerStateLocked();
        }
    }

    private final class DisplayGroupPowerChangeListener implements android.hardware.display.DisplayManagerInternal.DisplayGroupListener {
        static final int DISPLAY_GROUP_ADDED = 0;
        static final int DISPLAY_GROUP_CHANGED = 2;
        static final int DISPLAY_GROUP_REMOVED = 1;

        private DisplayGroupPowerChangeListener() {
        }

        public void onDisplayGroupAdded(int groupId) {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                if (com.android.server.power.PowerManagerService.this.mPowerGroups.contains(groupId)) {
                    android.util.Slog.e(com.android.server.power.PowerManagerService.TAG, "Tried to add already existing group:" + groupId);
                    return;
                }
                boolean supportsSandman = groupId == 0;
                com.android.server.power.PowerGroup powerGroup = new com.android.server.power.PowerGroup(groupId, com.android.server.power.PowerManagerService.this.mPowerGroupWakefulnessChangeListener, com.android.server.power.PowerManagerService.this.mNotifier, com.android.server.power.PowerManagerService.this.mDisplayManagerInternal, 1, false, supportsSandman, com.android.server.power.PowerManagerService.this.mClock.uptimeMillis());
                com.android.server.power.PowerManagerService.this.mPowerGroups.append(groupId, powerGroup);
                com.android.server.power.PowerManagerService.this.onPowerGroupEventLocked(0, powerGroup);
            }
        }

        public void onDisplayGroupRemoved(int groupId) {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                if (groupId == 0) {
                    android.util.Slog.wtf(com.android.server.power.PowerManagerService.TAG, "Tried to remove default display group: " + groupId);
                } else if (!com.android.server.power.PowerManagerService.this.mPowerGroups.contains(groupId)) {
                    android.util.Slog.e(com.android.server.power.PowerManagerService.TAG, "Tried to remove non-existent group:" + groupId);
                } else {
                    com.android.server.power.PowerManagerService.this.onPowerGroupEventLocked(1, (com.android.server.power.PowerGroup) com.android.server.power.PowerManagerService.this.mPowerGroups.get(groupId));
                }
            }
        }

        public void onDisplayGroupChanged(int groupId) {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                if (!com.android.server.power.PowerManagerService.this.mPowerGroups.contains(groupId)) {
                    android.util.Slog.e(com.android.server.power.PowerManagerService.TAG, "Tried to change non-existent group: " + groupId);
                } else {
                    com.android.server.power.PowerManagerService.this.onPowerGroupEventLocked(2, (com.android.server.power.PowerGroup) com.android.server.power.PowerManagerService.this.mPowerGroups.get(groupId));
                }
            }
        }
    }

    private final class ForegroundProfileObserver extends android.app.SynchronousUserSwitchObserver {
        private ForegroundProfileObserver() {
        }

        public void onUserSwitching(int newUserId) throws android.os.RemoteException {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                com.android.server.power.PowerManagerService.this.mUserId = newUserId;
            }
        }

        public void onForegroundProfileSwitch(int newProfileId) throws android.os.RemoteException {
            long now = com.android.server.power.PowerManagerService.this.mClock.uptimeMillis();
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                com.android.server.power.PowerManagerService.this.mForegroundProfile = newProfileId;
                com.android.server.power.PowerManagerService.this.maybeUpdateForegroundProfileLastActivityLocked(now);
            }
        }
    }

    private static final class ProfilePowerState {
        long mLastUserActivityTime;
        boolean mLockingNotified;
        long mScreenOffTimeout;
        final int mUserId;
        int mWakeLockSummary;

        public ProfilePowerState(int userId, long screenOffTimeout, long now) {
            this.mUserId = userId;
            this.mScreenOffTimeout = screenOffTimeout;
            this.mLastUserActivityTime = now;
        }
    }

    private final class Constants extends android.database.ContentObserver {
        private static final boolean DEFAULT_NO_CACHED_WAKE_LOCKS = true;
        private static final java.lang.String KEY_NO_CACHED_WAKE_LOCKS = "no_cached_wake_locks";
        public boolean NO_CACHED_WAKE_LOCKS;
        private final android.util.KeyValueListParser mParser;
        private android.content.ContentResolver mResolver;

        public Constants(android.os.Handler handler) {
            super(handler);
            this.NO_CACHED_WAKE_LOCKS = true;
            this.mParser = new android.util.KeyValueListParser(',');
        }

        public void start(android.content.ContentResolver resolver) {
            this.mResolver = resolver;
            this.mResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("power_manager_constants"), false, this);
            updateConstants();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            updateConstants();
        }

        private void updateConstants() {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                try {
                    this.mParser.setString(android.provider.Settings.Global.getString(this.mResolver, "power_manager_constants"));
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Slog.e(com.android.server.power.PowerManagerService.TAG, "Bad alarm manager settings", e);
                }
                this.NO_CACHED_WAKE_LOCKS = this.mParser.getBoolean(KEY_NO_CACHED_WAKE_LOCKS, true);
            }
        }

        void dump(java.io.PrintWriter pw) {
            pw.println("  Settings power_manager_constants:");
            pw.print("    ");
            pw.print(KEY_NO_CACHED_WAKE_LOCKS);
            pw.print("=");
            pw.println(this.NO_CACHED_WAKE_LOCKS);
        }

        void dumpProto(android.util.proto.ProtoOutputStream proto) {
            long constantsToken = proto.start(1146756268033L);
            proto.write(1133871366145L, this.NO_CACHED_WAKE_LOCKS);
            proto.end(constantsToken);
        }
    }

    public static class NativeWrapper {
        public void nativeInit(com.android.server.power.PowerManagerService service) {
            service.nativeInit();
        }

        public void nativeAcquireSuspendBlocker(java.lang.String name) {
            com.android.server.power.PowerManagerService.nativeAcquireSuspendBlocker(name);
        }

        public void nativeReleaseSuspendBlocker(java.lang.String name) {
            com.android.server.power.PowerManagerService.nativeReleaseSuspendBlocker(name);
        }

        public void nativeSetAutoSuspend(boolean enable) {
            com.android.server.power.PowerManagerService.nativeSetAutoSuspend(enable);
        }

        public void nativeSetPowerBoost(int boost, int durationMs) {
            com.android.server.power.PowerManagerService.nativeSetPowerBoost(boost, durationMs);
        }

        public boolean nativeSetPowerMode(int mode, boolean enabled) {
            return com.android.server.power.PowerManagerService.nativeSetPowerMode(mode, enabled);
        }

        public boolean nativeForceSuspend() {
            return com.android.server.power.PowerManagerService.nativeForceSuspend();
        }
    }

    static class Injector {
        Injector() {
        }

        com.android.server.power.Notifier createNotifier(android.os.Looper looper, android.content.Context context, com.android.internal.app.IBatteryStats batteryStats, com.android.server.power.SuspendBlocker suspendBlocker, com.android.server.policy.WindowManagerPolicy policy, com.android.server.power.FaceDownDetector faceDownDetector, com.android.server.power.ScreenUndimDetector screenUndimDetector, java.util.concurrent.Executor backgroundExecutor, com.android.server.power.feature.PowerManagerFlags powerManagerFlags) {
            return new com.android.server.power.Notifier(looper, context, batteryStats, suspendBlocker, policy, faceDownDetector, screenUndimDetector, backgroundExecutor, powerManagerFlags, null);
        }

        com.android.server.power.SuspendBlocker createSuspendBlocker(com.android.server.power.PowerManagerService service, java.lang.String name) {
            java.util.Objects.requireNonNull(service);
            com.android.server.power.PowerManagerService.SuspendBlockerImpl suspendBlockerImpl = service.new SuspendBlockerImpl(name);
            service.mSuspendBlockers.add(suspendBlockerImpl);
            return suspendBlockerImpl;
        }

        com.android.server.power.batterysaver.BatterySaverStateMachine createBatterySaverStateMachine(java.lang.Object lock, android.content.Context context) {
            com.android.server.power.batterysaver.BatterySavingStats batterySavingStats = new com.android.server.power.batterysaver.BatterySavingStats(lock);
            com.android.server.power.batterysaver.BatterySaverPolicy batterySaverPolicy = new com.android.server.power.batterysaver.BatterySaverPolicy(lock, context, batterySavingStats);
            com.android.server.power.batterysaver.BatterySaverController batterySaverController = new com.android.server.power.batterysaver.BatterySaverController(lock, context, com.android.server.power.PowerManagerService.mPmsExt.getCustomPowerManagerLooper(), batterySaverPolicy, batterySavingStats);
            return new com.android.server.power.batterysaver.BatterySaverStateMachine(lock, context, batterySaverController);
        }

        com.android.server.power.PowerManagerService.NativeWrapper createNativeWrapper() {
            return new com.android.server.power.PowerManagerService.NativeWrapper();
        }

        com.android.server.power.WirelessChargerDetector createWirelessChargerDetector(android.hardware.SensorManager sensorManager, com.android.server.power.SuspendBlocker suspendBlocker, android.os.Handler handler) {
            return new com.android.server.power.WirelessChargerDetector(sensorManager, suspendBlocker, handler);
        }

        android.hardware.display.AmbientDisplayConfiguration createAmbientDisplayConfiguration(android.content.Context context) {
            return new android.hardware.display.AmbientDisplayConfiguration(context);
        }

        com.android.server.power.AmbientDisplaySuppressionController createAmbientDisplaySuppressionController(com.android.server.power.AmbientDisplaySuppressionController.AmbientDisplaySuppressionChangedCallback callback) {
            return new com.android.server.power.AmbientDisplaySuppressionController(callback);
        }

        com.android.server.power.InattentiveSleepWarningController createInattentiveSleepWarningController() {
            return new com.android.server.power.InattentiveSleepWarningController();
        }

        com.android.internal.foldables.FoldGracePeriodProvider createFoldGracePeriodProvider() {
            return new com.android.internal.foldables.FoldGracePeriodProvider();
        }

        public com.android.server.power.SystemPropertiesWrapper createSystemPropertiesWrapper() {
            return new com.android.server.power.SystemPropertiesWrapper() { // from class: com.android.server.power.PowerManagerService.Injector.1
                @Override // com.android.server.power.SystemPropertiesWrapper
                public java.lang.String get(java.lang.String key, java.lang.String def) {
                    return android.os.SystemProperties.get(key, def);
                }

                @Override // com.android.server.power.SystemPropertiesWrapper
                public void set(java.lang.String key, java.lang.String val) {
                    android.os.SystemProperties.set(key, val);
                }
            };
        }

        com.android.server.power.PowerManagerService.Clock createClock() {
            return new com.android.server.power.PowerManagerService.Clock() { // from class: com.android.server.power.PowerManagerService.Injector.2
                @Override // com.android.server.power.PowerManagerService.Clock
                public long uptimeMillis() {
                    return android.os.SystemClock.uptimeMillis();
                }

                @Override // com.android.server.power.PowerManagerService.Clock
                public long elapsedRealtime() {
                    return android.os.SystemClock.elapsedRealtime();
                }
            };
        }

        android.os.Handler createHandler(android.os.Looper looper, android.os.Handler.Callback callback) {
            return new android.os.Handler(looper, callback, true);
        }

        void invalidateIsInteractiveCaches() {
            android.os.PowerManager.invalidateIsInteractiveCaches();
        }

        com.android.server.power.LowPowerStandbyController createLowPowerStandbyController(android.content.Context context, android.os.Looper looper) {
            return new com.android.server.power.LowPowerStandbyController(context, looper);
        }

        com.android.server.power.PowerManagerService.PermissionCheckerWrapper createPermissionCheckerWrapper() {
            return new com.android.server.power.PowerManagerService.PermissionCheckerWrapper() { // from class: com.android.server.power.PowerManagerService$Injector$$ExternalSyntheticLambda0
                @Override // com.android.server.power.PowerManagerService.PermissionCheckerWrapper
                public final int checkPermissionForDataDelivery(android.content.Context context, java.lang.String str, int i, android.content.AttributionSource attributionSource, java.lang.String str2) {
                    return android.content.PermissionChecker.checkPermissionForDataDelivery(context, str, i, attributionSource, str2);
                }
            };
        }

        com.android.server.power.PowerManagerService.PowerPropertiesWrapper createPowerPropertiesWrapper() {
            return new com.android.server.power.PowerManagerService.PowerPropertiesWrapper() { // from class: com.android.server.power.PowerManagerService.Injector.3
                @Override // com.android.server.power.PowerManagerService.PowerPropertiesWrapper
                public boolean waive_target_sdk_check_for_turn_screen_on() {
                    return ((java.lang.Boolean) android.sysprop.PowerProperties.waive_target_sdk_check_for_turn_screen_on().orElse(false)).booleanValue();
                }

                @Override // com.android.server.power.PowerManagerService.PowerPropertiesWrapper
                public boolean permissionless_turn_screen_on() {
                    return ((java.lang.Boolean) android.sysprop.PowerProperties.permissionless_turn_screen_on().orElse(false)).booleanValue();
                }
            };
        }

        com.android.server.display.feature.DeviceConfigParameterProvider createDeviceConfigParameterProvider() {
            return new com.android.server.display.feature.DeviceConfigParameterProvider(android.provider.DeviceConfigInterface.REAL);
        }

        com.android.server.power.feature.PowerManagerFlags getFlags() {
            return new com.android.server.power.feature.PowerManagerFlags();
        }
    }

    public PowerManagerService(android.content.Context context) {
        this(context, new com.android.server.power.PowerManagerService.Injector());
    }

    /* JADX WARN: Multi-variable type inference failed */
    PowerManagerService(android.content.Context context, com.android.server.power.PowerManagerService.Injector injector) {
        boolean z;
        super(context);
        this.mLock = com.android.server.LockGuard.installNewLock(1);
        this.mSuspendBlockers = new java.util.ArrayList<>();
        this.mWakeLocks = new java.util.ArrayList<>();
        this.mEnhancedDischargeTimeLock = new java.lang.Object();
        this.mDockState = 0;
        this.mMaximumScreenOffTimeoutFromDeviceAdmin = Long.MAX_VALUE;
        this.mIsFaceDown = false;
        this.mLastFlipTime = 0L;
        this.mScreenBrightnessOverrideFromWindowManager = Float.NaN;
        this.mOverriddenTimeout = -1L;
        this.mUserActivityTimeoutOverrideFromWindowManager = -1L;
        this.mDozeScreenStateOverrideFromDreamManager = 0;
        this.mDozeScreenStateOverrideReasonFromDreamManager = 0;
        this.mDozeScreenBrightnessOverrideFromDreamManager = -1;
        this.mDozeScreenBrightnessOverrideFromDreamManagerFloat = Float.NaN;
        this.mLastWarningAboutUserActivityPermission = Long.MIN_VALUE;
        this.mDeviceIdleWhitelist = new int[0];
        this.mDeviceIdleTempWhitelist = new int[0];
        this.mLowPowerStandbyAllowlist = new int[0];
        this.mUidState = new android.util.SparseArray<>();
        this.mPowerGroups = new android.util.SparseArray<>();
        this.mProfilePowerState = new android.util.SparseArray<>();
        this.mDisplayPowerCallbacks = new android.hardware.display.DisplayManagerInternal.DisplayPowerCallbacks() { // from class: com.android.server.power.PowerManagerService.1
            public void onStateChanged() {
                synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                    com.android.server.power.PowerManagerService.this.mDirty |= 8;
                    com.android.server.power.PowerManagerService.this.updatePowerStateLocked();
                }
            }

            public void onProximityPositive() {
                synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                    android.util.Slog.i(com.android.server.power.PowerManagerService.TAG, "onProximityPositive");
                    com.android.server.power.PowerManagerService.mPmsExt.setScreenOffPositive("1");
                    com.android.server.power.PowerManagerService.this.mProximityPositive = true;
                    com.android.server.power.PowerManagerService.this.mInterceptedPowerKeyForProximity = false;
                    com.android.server.power.PowerManagerService.this.mDirty |= 512;
                    com.android.server.power.PowerManagerService.this.updatePowerStateLocked();
                }
            }

            public void onProximityNegative() {
                synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                    android.util.Slog.i(com.android.server.power.PowerManagerService.TAG, "onProximityNegative");
                    com.android.server.power.PowerManagerService.mPmsExt.setScreenOffPositive("0");
                    com.android.server.power.PowerManagerService.this.mProximityPositive = false;
                    com.android.server.power.PowerManagerService.this.mInterceptedPowerKeyForProximity = false;
                    com.android.server.power.PowerManagerService.this.mDirty |= 512;
                    com.android.server.power.PowerManagerService.this.userActivityNoUpdateLocked((com.android.server.power.PowerGroup) com.android.server.power.PowerManagerService.this.mPowerGroups.get(0), com.android.server.power.PowerManagerService.this.mClock.uptimeMillis(), 0, 0, 1000);
                    com.android.server.power.PowerManagerService.this.updatePowerStateLocked();
                }
            }

            public void onDisplayStateChange(boolean allInactive, boolean allOff) {
                synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                    com.android.server.power.PowerManagerService.mPmsExt.onDisplayStateChange(allOff);
                    com.android.server.power.PowerManagerService.this.setPowerModeInternal(9, allInactive);
                    if (allOff) {
                        if (!com.android.server.power.PowerManagerService.this.mDecoupleHalInteractiveModeFromDisplayConfig) {
                            com.android.server.power.PowerManagerService.this.setHalInteractiveModeLocked(false);
                        }
                        if (!com.android.server.power.PowerManagerService.this.mDecoupleHalAutoSuspendModeFromDisplayConfig) {
                            com.android.server.power.PowerManagerService.this.setHalAutoSuspendModeLocked(true);
                        }
                    } else {
                        if (!com.android.server.power.PowerManagerService.this.mDecoupleHalAutoSuspendModeFromDisplayConfig) {
                            com.android.server.power.PowerManagerService.this.setHalAutoSuspendModeLocked(false);
                        }
                        if (!com.android.server.power.PowerManagerService.this.mDecoupleHalInteractiveModeFromDisplayConfig) {
                            com.android.server.power.PowerManagerService.this.setHalInteractiveModeLocked(true);
                        }
                    }
                }
            }

            public void acquireSuspendBlocker(java.lang.String name) {
                com.android.server.power.PowerManagerService.this.mDisplaySuspendBlocker.acquire(name);
            }

            public void releaseSuspendBlocker(java.lang.String name) {
                com.android.server.power.PowerManagerService.this.mDisplaySuspendBlocker.release(name);
            }
        };
        this.mAmbientSuppressionChangedCallback = new com.android.server.power.AmbientDisplaySuppressionController.AmbientDisplaySuppressionChangedCallback() { // from class: com.android.server.power.PowerManagerService.4
            @Override // com.android.server.power.AmbientDisplaySuppressionController.AmbientDisplaySuppressionChangedCallback
            public void onSuppressionChanged(boolean isSuppressed) {
                synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                    com.android.server.power.PowerManagerService.this.onDreamSuppressionChangedLocked(isSuppressed);
                }
            }
        };
        this.mPmsWrapper = new com.android.server.power.PowerManagerService.PowerManagerServiceWrapper();
        this.mContext = context;
        mPmsExt = (com.android.server.power.IPowerManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.power.IPowerManagerServiceExt.class).base(this).create();
        this.mBinderService = new com.android.server.power.PowerManagerService.BinderService(this.mContext);
        this.mLocalService = new com.android.server.power.PowerManagerService.LocalService();
        this.mNativeWrapper = injector.createNativeWrapper();
        this.mSystemProperties = injector.createSystemPropertiesWrapper();
        this.mClock = injector.createClock();
        this.mFeatureFlags = injector.getFlags();
        this.mInjector = injector;
        this.mHandlerThread = new com.android.server.ServiceThread(TAG, -4, false);
        this.mHandlerThread.start();
        this.mHandler = injector.createHandler(this.mHandlerThread.getLooper(), new com.android.server.power.PowerManagerService.PowerManagerHandlerCallback());
        this.mConstants = new com.android.server.power.PowerManagerService.Constants(this.mHandler);
        this.mFoldGracePeriodProvider = injector.createFoldGracePeriodProvider();
        this.mAmbientDisplayConfiguration = this.mInjector.createAmbientDisplayConfiguration(context);
        this.mAmbientDisplaySuppressionController = this.mInjector.createAmbientDisplaySuppressionController(this.mAmbientSuppressionChangedCallback);
        this.mAttentionDetector = new com.android.server.power.AttentionDetector(new java.lang.Runnable() { // from class: com.android.server.power.PowerManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.onUserAttention();
            }
        }, this.mLock);
        this.mFaceDownDetector = new com.android.server.power.FaceDownDetector(new java.util.function.Consumer() { // from class: com.android.server.power.PowerManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) throws java.lang.Throwable {
                this.f$0.onFlip(((java.lang.Boolean) obj).booleanValue());
            }
        });
        this.mScreenUndimDetector = new com.android.server.power.ScreenUndimDetector();
        this.mBatterySaverSupported = this.mContext.getResources().getBoolean(android.R.bool.config_avoidGfxAccel);
        this.mBatterySaverStateMachine = this.mBatterySaverSupported ? this.mInjector.createBatterySaverStateMachine(this.mLock, this.mContext) : null;
        this.mLowPowerStandbyController = this.mInjector.createLowPowerStandbyController(this.mContext, android.os.Looper.getMainLooper());
        this.mInattentiveSleepWarningOverlayController = this.mInjector.createInattentiveSleepWarningController();
        this.mPermissionCheckerWrapper = this.mInjector.createPermissionCheckerWrapper();
        this.mPowerPropertiesWrapper = this.mInjector.createPowerPropertiesWrapper();
        this.mDeviceConfigProvider = this.mInjector.createDeviceConfigParameterProvider();
        this.mPowerGroupWakefulnessChangeListener = new com.android.server.power.PowerManagerService.PowerGroupWakefulnessChangeListener();
        mPmsExt.init(context);
        this.mUseAutoSuspend = this.mContext.getResources().getBoolean(android.R.bool.config_supportsHardwareCamToggle);
        float f = this.mContext.getResources().getFloat(android.R.dimen.config_mediaMetadataBitmapMaxSize);
        float f2 = this.mContext.getResources().getFloat(android.R.dimen.config_lowResTaskSnapshotScale);
        float f3 = this.mContext.getResources().getFloat(android.R.dimen.config_letterboxVerticalPositionMultiplier);
        float f4 = this.mContext.getResources().getFloat(android.R.dimen.config_letterboxThinLetterboxHeightDp);
        float f5 = this.mContext.getResources().getFloat(android.R.dimen.config_letterboxTabletopModePositionMultiplier);
        if (f == INVALID_BRIGHTNESS_IN_CONFIG || f2 == INVALID_BRIGHTNESS_IN_CONFIG || f3 == INVALID_BRIGHTNESS_IN_CONFIG) {
            this.mScreenBrightnessMinimum = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mContext.getResources().getInteger(android.R.integer.config_reduceBrightColorsStrengthDefault));
            this.mScreenBrightnessMaximum = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mContext.getResources().getInteger(android.R.integer.config_recentVibrationsDumpSizeLimit));
            this.mScreenBrightnessDefault = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mContext.getResources().getInteger(android.R.integer.config_radioScanningTimeout));
        } else {
            this.mScreenBrightnessMinimum = f;
            this.mScreenBrightnessMaximum = f2;
            this.mScreenBrightnessDefault = f3;
        }
        if (f4 == INVALID_BRIGHTNESS_IN_CONFIG) {
            this.mScreenBrightnessDoze = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mContext.getResources().getInteger(android.R.integer.config_progressTimeoutFallbackHome));
        } else {
            this.mScreenBrightnessDoze = f4;
        }
        if (f5 == INVALID_BRIGHTNESS_IN_CONFIG) {
            this.mScreenBrightnessDim = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mContext.getResources().getInteger(android.R.integer.config_previousVibrationsDumpSizeLimit));
        } else {
            this.mScreenBrightnessDim = f5;
        }
        synchronized (this.mLock) {
            this.mBootingSuspendBlocker = this.mInjector.createSuspendBlocker(this, "PowerManagerService.Booting");
            this.mWakeLockSuspendBlocker = this.mInjector.createSuspendBlocker(this, "PowerManagerService.WakeLocks");
            this.mDisplaySuspendBlocker = this.mInjector.createSuspendBlocker(this, "PowerManagerService.Display");
            if (this.mBootingSuspendBlocker != null) {
                this.mBootingSuspendBlocker.acquire();
                this.mHoldingBootingSuspendBlocker = true;
            }
            if (this.mDisplaySuspendBlocker != null) {
                this.mDisplaySuspendBlocker.acquire(HOLDING_DISPLAY_SUSPEND_BLOCKER);
                this.mHoldingDisplaySuspendBlocker = true;
            }
            this.mHalAutoSuspendModeEnabled = false;
            this.mHalInteractiveModeEnabled = true;
            this.mWakefulnessRaw = 1;
            if (this.mSystemProperties.get(SYSTEM_PROPERTY_QUIESCENT, "0").equals("1") || ((java.lang.Boolean) android.sysprop.InitProperties.userspace_reboot_in_progress().orElse(false)).booleanValue()) {
                z = true;
            } else {
                z = false;
            }
            sQuiescent = z;
            this.mNativeWrapper.nativeInit(this);
            this.mNativeWrapper.nativeSetAutoSuspend(false);
            this.mNativeWrapper.nativeSetPowerMode(7, true);
            this.mNativeWrapper.nativeSetPowerMode(0, false);
            this.mInjector.invalidateIsInteractiveCaches();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFlip(boolean isFaceDown) throws java.lang.Throwable {
        long currentTime;
        synchronized (this.mLock) {
            try {
                if (this.mBootCompleted) {
                    android.util.Slog.i(TAG, "onFlip(): Face " + (isFaceDown ? "down." : "up."));
                    this.mIsFaceDown = isFaceDown;
                    if (!isFaceDown) {
                        currentTime = 0;
                    } else {
                        long currentTime2 = this.mClock.uptimeMillis();
                        this.mLastFlipTime = currentTime2;
                        long sleepTimeout = getSleepTimeoutLocked(-1L);
                        long screenOffTimeout = getScreenOffTimeoutLocked(sleepTimeout, -1L);
                        com.android.server.power.PowerGroup powerGroup = this.mPowerGroups.get(0);
                        long millisUntilNormalTimeout = (powerGroup.getLastUserActivityTimeLocked() + screenOffTimeout) - currentTime2;
                        try {
                            userActivityInternal(0, currentTime2, 5, 1, 1000);
                            currentTime = millisUntilNormalTimeout;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                    try {
                        android.util.Slog.d(TAG, "onFlip: isFaceDown=" + isFaceDown);
                        if (!isFaceDown) {
                            userActivityInternal(0, this.mClock.uptimeMillis(), 0, 0, 1000);
                        }
                        if (isFaceDown) {
                            this.mFaceDownDetector.setMillisSaved(currentTime);
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("power", this.mBinderService, false, 1);
        publishLocalService(android.os.PowerManagerInternal.class, this.mLocalService);
        com.android.server.Watchdog.getInstance().addMonitor(this);
        com.android.server.Watchdog.getInstance().addThread(this.mHandler);
        mPmsExt.onStart();
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            systemReady();
            return;
        }
        if (phase == 600) {
            incrementBootCount();
            return;
        }
        if (phase == 1000) {
            synchronized (this.mLock) {
                long now = this.mClock.uptimeMillis();
                this.mBootCompleted = true;
                this.mDirty |= 16;
                if (this.mBatterySaverSupported) {
                    this.mBatterySaverStateMachine.onBootCompleted();
                }
                userActivityNoUpdateLocked(now, 0, 0, 1000);
                updatePowerStateLocked();
                if (sQuiescent) {
                    sleepPowerGroupLocked(this.mPowerGroups.get(0), this.mClock.uptimeMillis(), 10, 1000);
                }
                ((android.hardware.devicestate.DeviceStateManager) this.mContext.getSystemService(android.hardware.devicestate.DeviceStateManager.class)).registerCallback(new android.os.HandlerExecutor(this.mHandler), new com.android.server.power.PowerManagerService.DeviceStateListener());
                mPmsExt.onBootComplete();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void systemReady() {
        java.lang.Object[] objArr;
        java.lang.Object[] objArr2;
        synchronized (this.mLock) {
            this.mSystemReady = true;
            this.mDreamManager = (android.service.dreams.DreamManagerInternal) getLocalService(android.service.dreams.DreamManagerInternal.class);
            this.mDisplayManagerInternal = (android.hardware.display.DisplayManagerInternal) getLocalService(android.hardware.display.DisplayManagerInternal.class);
            this.mPolicy = (com.android.server.policy.WindowManagerPolicy) getLocalService(com.android.server.policy.WindowManagerPolicy.class);
            this.mBatteryManagerInternal = (android.os.BatteryManagerInternal) getLocalService(android.os.BatteryManagerInternal.class);
            this.mAttentionDetector.systemReady(this.mContext);
            mPmsExt.systemReady();
            android.hardware.SensorManager systemSensorManager = new android.hardware.SystemSensorManager(this.mContext, this.mHandler.getLooper());
            this.mBatteryStats = com.android.server.am.BatteryStatsService.getService();
            this.mNotifier = this.mInjector.createNotifier(mPmsExt.getCustomPowerManagerLooper(), this.mContext, this.mBatteryStats, this.mInjector.createSuspendBlocker(this, "PowerManagerService.Broadcasts"), this.mPolicy, this.mFaceDownDetector, this.mScreenUndimDetector, com.android.internal.os.BackgroundThread.getExecutor(), this.mFeatureFlags);
            this.mPowerGroups.append(0, new com.android.server.power.PowerGroup(1, this.mPowerGroupWakefulnessChangeListener, this.mNotifier, this.mDisplayManagerInternal, this.mClock.uptimeMillis()));
            objArr2 = 0;
            objArr = 0;
            java.lang.Object[] objArr3 = 0;
            this.mDisplayManagerInternal.registerDisplayGroupListener(new com.android.server.power.PowerManagerService.DisplayGroupPowerChangeListener());
            this.mDreamManager.registerDreamManagerStateListener(new com.android.server.power.PowerManagerService.DreamManagerStateListener());
            this.mWirelessChargerDetector = this.mInjector.createWirelessChargerDetector(systemSensorManager, this.mInjector.createSuspendBlocker(this, "PowerManagerService.WirelessChargerDetector"), this.mHandler);
            this.mSettingsObserver = new com.android.server.power.PowerManagerService.SettingsObserver(this.mHandler);
            this.mLightsManager = (com.android.server.lights.LightsManager) getLocalService(com.android.server.lights.LightsManager.class);
            this.mAttentionLight = this.mLightsManager.getLight(5);
            updateDeviceConfigLocked();
            this.mDeviceConfigProvider.addOnPropertiesChangedListener(com.android.internal.os.BackgroundThread.getExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.power.PowerManagerService$$ExternalSyntheticLambda2
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.lambda$systemReady$0(properties);
                }
            });
            this.mDisplayManagerInternal.initPowerManagement(this.mDisplayPowerCallbacks, this.mHandler, systemSensorManager);
            addPowerGroupsForNonDefaultDisplayGroupLocked();
            try {
                android.app.ActivityManager.getService().registerUserSwitchObserver(new com.android.server.power.PowerManagerService.ForegroundProfileObserver(), TAG);
            } catch (android.os.RemoteException e) {
            }
            this.mLowPowerStandbyController.systemReady();
            readConfigurationLocked();
            updateSettingsLocked();
            mPmsExt.handleAodChanged();
            if (this.mFeatureFlags.isEarlyScreenTimeoutDetectorEnabled()) {
                this.mScreenTimeoutOverridePolicy = new com.android.server.power.ScreenTimeoutOverridePolicy(this.mContext, this.mMinimumScreenOffTimeoutConfig, new com.android.server.power.ScreenTimeoutOverridePolicy.PolicyCallback() { // from class: com.android.server.power.PowerManagerService$$ExternalSyntheticLambda3
                    @Override // com.android.server.power.ScreenTimeoutOverridePolicy.PolicyCallback
                    public final void releaseAllScreenTimeoutOverrideWakelocks(int i) {
                        this.f$0.lambda$systemReady$1(i);
                    }
                });
            }
            this.mDirty |= 256;
            updatePowerStateLocked();
        }
        android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
        this.mConstants.start(contentResolver);
        if (this.mBatterySaverSupported) {
            this.mBatterySaverStateMachine.systemReady();
        }
        this.mFaceDownDetector.systemReady(this.mContext);
        this.mScreenUndimDetector.systemReady(this.mContext);
        contentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("screensaver_enabled"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("screensaver_activate_on_sleep"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("screensaver_activate_on_dock"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.System.getUriFor("screen_off_timeout"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("sleep_timeout"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("attentive_timeout"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("stay_on_while_plugged_in"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.System.getUriFor("screen_brightness_mode"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.System.getUriFor("screen_auto_brightness_adj"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("theater_mode_on"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("doze_always_on"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("double_tap_to_wake"), false, this.mSettingsObserver, -1);
        contentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("device_demo_mode"), false, this.mSettingsObserver, 0);
        mPmsExt.registerOtherContentObserver(contentResolver, this.mSettingsObserver);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.setPriority(1000);
        this.mContext.registerReceiver(new com.android.server.power.PowerManagerService.BatteryReceiver(), intentFilter, null, this.mHandler);
        android.content.IntentFilter intentFilter2 = new android.content.IntentFilter();
        intentFilter2.addAction("android.intent.action.DREAMING_STARTED");
        intentFilter2.addAction("android.intent.action.DREAMING_STOPPED");
        this.mContext.registerReceiver(new com.android.server.power.PowerManagerService.DreamReceiver(), intentFilter2, null, this.mHandler);
        android.content.IntentFilter intentFilter3 = new android.content.IntentFilter();
        intentFilter3.addAction("android.intent.action.USER_SWITCHED");
        this.mContext.registerReceiver(new com.android.server.power.PowerManagerService.UserSwitchedReceiver(), intentFilter3, null, this.mHandler);
        android.content.IntentFilter intentFilter4 = new android.content.IntentFilter();
        intentFilter4.addAction("android.intent.action.DOCK_EVENT");
        this.mContext.registerReceiver(new com.android.server.power.PowerManagerService.DockReceiver(), intentFilter4, null, this.mHandler);
        mPmsExt.systemReady(this.mInjector.createSuspendBlocker(this, "WakeLockCheck"));
        mPmsExt.onAodsystemReady();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$0(android.provider.DeviceConfig.Properties properties) {
        synchronized (this.mLock) {
            updateDeviceConfigLocked();
            updateWakeLockDisabledStatesLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$1(int releaseReason) {
        android.os.Message msg = this.mHandler.obtainMessage(6);
        msg.arg1 = releaseReason;
        this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
    }

    void readConfigurationLocked() {
        android.content.res.Resources resources = this.mContext.getResources();
        this.mDecoupleHalAutoSuspendModeFromDisplayConfig = resources.getBoolean(android.R.bool.config_navBarDefaultTransparent);
        this.mDecoupleHalInteractiveModeFromDisplayConfig = resources.getBoolean(android.R.bool.config_navBarNeedsScrim);
        this.mWakeUpWhenPluggedOrUnpluggedConfig = resources.getBoolean(android.R.bool.config_supportTelephonyTimeZoneFallback);
        this.mWakeUpWhenPluggedOrUnpluggedInTheaterModeConfig = resources.getBoolean(android.R.bool.config_allowTheaterModeWakeFromMotion);
        this.mSuspendWhenScreenOffDueToProximityConfig = resources.getBoolean(android.R.bool.config_startDreamImmediatelyOnDock);
        this.mAttentiveTimeoutConfig = resources.getInteger(android.R.integer.config_aggregatedPowerStatsSpanDuration);
        this.mAttentiveWarningDurationConfig = resources.getInteger(android.R.integer.config_alertDialogController);
        this.mDreamsSupportedConfig = resources.getBoolean(android.R.bool.config_dreamsDisabledByAmbientModeSuppressionConfig);
        this.mDreamsEnabledByDefaultConfig = resources.getBoolean(android.R.bool.config_dreamsActivatedOnDockByDefault);
        this.mDreamsActivatedOnSleepByDefaultConfig = resources.getBoolean(android.R.bool.config_dozeWakeLockScreenSensorAvailable);
        this.mDreamsActivatedOnDockByDefaultConfig = resources.getBoolean(android.R.bool.config_dozeSupportsAodWallpaper);
        this.mDreamsEnabledOnBatteryConfig = resources.getBoolean(android.R.bool.config_dreamsActivatedOnPosturedByDefault);
        this.mDreamsBatteryLevelMinimumWhenPoweredConfig = resources.getInteger(android.R.integer.config_doubleTapPowerGestureMultiTargetDefaultAction);
        this.mDreamsBatteryLevelMinimumWhenNotPoweredConfig = resources.getInteger(android.R.integer.config_doubleTapPowerGestureMode);
        this.mDreamsBatteryLevelDrainCutoffConfig = resources.getInteger(android.R.integer.config_doubleTapOnHomeBehavior);
        this.mDreamsDisabledByAmbientModeSuppressionConfig = resources.getBoolean(android.R.bool.config_dragToMaximizeInDesktopMode);
        this.mDozeAfterScreenOff = resources.getBoolean(android.R.bool.config_dockedStackDividerFreeSnapMode);
        this.mBrightWhenDozingConfig = resources.getBoolean(android.R.bool.config_built_in_sip_phone);
        this.mMinimumScreenOffTimeoutConfig = resources.getInteger(android.R.integer.config_maxNumVisibleRecentTasks);
        this.mMaximumScreenDimDurationConfig = resources.getInteger(android.R.integer.config_longPressOnPowerDurationMs);
        this.mMaximumScreenDimRatioConfig = resources.getFraction(android.R.fraction.config_maximumScreenDimRatio, 1, 1);
        this.mSupportsDoubleTapWakeConfig = resources.getBoolean(android.R.bool.config_showSysuiShutdown);
        mPmsExt.onReadConfigurationLocked();
    }

    private void updateSettingsLocked() {
        android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
        this.mDreamsEnabledSetting = android.provider.Settings.Secure.getIntForUser(contentResolver, "screensaver_enabled", this.mDreamsEnabledByDefaultConfig ? 1 : 0, -2) != 0;
        this.mDreamsActivateOnSleepSetting = android.provider.Settings.Secure.getIntForUser(contentResolver, "screensaver_activate_on_sleep", this.mDreamsActivatedOnSleepByDefaultConfig ? 1 : 0, -2) != 0;
        this.mDreamsActivateOnDockSetting = android.provider.Settings.Secure.getIntForUser(contentResolver, "screensaver_activate_on_dock", this.mDreamsActivatedOnDockByDefaultConfig ? 1 : 0, -2) != 0;
        this.mScreenOffTimeoutSetting = android.provider.Settings.System.getIntForUser(contentResolver, "screen_off_timeout", 15000, -2);
        this.mSleepTimeoutSetting = android.provider.Settings.Secure.getIntForUser(contentResolver, "sleep_timeout", -1, -2);
        this.mAttentiveTimeoutSetting = android.provider.Settings.Secure.getIntForUser(contentResolver, "attentive_timeout", this.mAttentiveTimeoutConfig, -2);
        this.mStayOnWhilePluggedInSetting = android.provider.Settings.Global.getInt(contentResolver, "stay_on_while_plugged_in", 1);
        this.mTheaterModeEnabled = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "theater_mode_on", 0) == 1;
        this.mAlwaysOnEnabled = this.mAmbientDisplayConfiguration.alwaysOnEnabled(-2);
        if (this.mSupportsDoubleTapWakeConfig) {
            boolean z = android.provider.Settings.Secure.getIntForUser(contentResolver, "double_tap_to_wake", 0, -2) != 0;
            if (z != this.mDoubleTapWakeEnabled) {
                this.mDoubleTapWakeEnabled = z;
                this.mNativeWrapper.nativeSetPowerMode(0, this.mDoubleTapWakeEnabled);
            }
        }
        java.lang.String str = android.os.UserManager.isDeviceInDemoMode(this.mContext) ? "1" : "0";
        if (!str.equals(this.mSystemProperties.get(SYSTEM_PROPERTY_RETAIL_DEMO_ENABLED, null))) {
            this.mSystemProperties.set(SYSTEM_PROPERTY_RETAIL_DEMO_ENABLED, str);
        }
        this.mDirty |= 32;
        mPmsExt.updateSettingsLocked(contentResolver);
        android.util.Slog.d(TAG, "updateSettingsLocked: mScreenOffTimeoutSetting=" + this.mScreenOffTimeoutSetting);
    }

    void handleSettingsChangedLocked() {
        mPmsExt.handleAodChanged();
        updateSettingsLocked();
        updatePowerStateLocked();
    }

    private void updateDeviceConfigLocked() {
        this.mDisableScreenWakeLocksWhileCached = this.mDeviceConfigProvider.isDisableScreenWakeLocksWhileCachedFeatureEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0057 A[Catch: all -> 0x01b5, DONT_GENERATE, TryCatch #0 {all -> 0x01b5, blocks: (B:6:0x000c, B:8:0x0010, B:11:0x001a, B:12:0x0032, B:14:0x0034, B:17:0x003b, B:18:0x0042, B:19:0x0043, B:21:0x0057, B:23:0x0059), top: B:69:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0059 A[Catch: all -> 0x01b5, TRY_LEAVE, TryCatch #0 {all -> 0x01b5, blocks: (B:6:0x000c, B:8:0x0010, B:11:0x001a, B:12:0x0032, B:14:0x0034, B:17:0x003b, B:18:0x0042, B:19:0x0043, B:21:0x0057, B:23:0x0059), top: B:69:0x000c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void acquireWakeLockInternal(android.os.IBinder r25, int r26, int r27, java.lang.String r28, java.lang.String r29, android.os.WorkSource r30, java.lang.String r31, int r32, int r33, android.os.IWakeLockCallback r34) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 444
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.PowerManagerService.acquireWakeLockInternal(android.os.IBinder, int, int, java.lang.String, java.lang.String, android.os.WorkSource, java.lang.String, int, int, android.os.IWakeLockCallback):void");
    }

    private static boolean isScreenLock(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        switch (wakeLock.mFlags & 65535) {
            case 6:
            case 10:
            case 26:
                return true;
            default:
                return false;
        }
    }

    private static android.os.WorkSource.WorkChain getFirstNonEmptyWorkChain(android.os.WorkSource workSource) {
        if (workSource.getWorkChains() == null) {
            return null;
        }
        for (android.os.WorkSource.WorkChain workChain : workSource.getWorkChains()) {
            if (workChain.getSize() > 0) {
                return workChain;
            }
        }
        return null;
    }

    private boolean isAcquireCausesWakeupFlagAllowed(java.lang.String opPackageName, int opUid, int opPid) {
        if (opPackageName == null) {
            return false;
        }
        if (this.mPermissionCheckerWrapper.checkPermissionForDataDelivery(this.mContext, "android.permission.TURN_SCREEN_ON", opPid, new android.content.AttributionSource(opUid, opPackageName, null), "ACQUIRE_CAUSES_WAKEUP for " + opPackageName) == 0) {
            android.util.Slog.i(TAG, "Allowing device wake-up from app " + opPackageName);
            return true;
        }
        if (!android.app.compat.CompatChanges.isChangeEnabled(REQUIRE_TURN_SCREEN_ON_PERMISSION, opUid) && !this.mPowerPropertiesWrapper.waive_target_sdk_check_for_turn_screen_on()) {
            android.util.Slog.i(TAG, "Allowing device wake-up without android.permission.TURN_SCREEN_ON for " + opPackageName);
            return true;
        }
        if (this.mPowerPropertiesWrapper.permissionless_turn_screen_on()) {
            android.util.Slog.d(TAG, "Device wake-up allowed by debug.power.permissionless_turn_screen_on");
            return true;
        }
        android.util.Slog.w(TAG, "Not allowing device wake-up for " + opPackageName);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyWakeLockFlagsOnAcquireLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) throws java.lang.Throwable {
        int opPid;
        java.lang.String opPackageName;
        int opUid;
        int opUid2;
        java.lang.String opPackageName2;
        if ((wakeLock.mFlags & 268435456) == 0 || !isScreenLock(wakeLock) || mPmsExt.onApplyWakeLockFlagsOnAcquireLocked(wakeLock, wakeLock.mOwnerUid)) {
            return;
        }
        if (wakeLock.mWorkSource != null && !wakeLock.mWorkSource.isEmpty()) {
            android.os.WorkSource workSource = wakeLock.mWorkSource;
            android.os.WorkSource.WorkChain workChain = getFirstNonEmptyWorkChain(workSource);
            if (workChain != null) {
                opPackageName2 = workChain.getAttributionTag();
                opUid2 = workChain.getAttributionUid();
            } else {
                java.lang.String opPackageName3 = workSource.getPackageName(0) != null ? workSource.getPackageName(0) : wakeLock.mPackageName;
                java.lang.String str = opPackageName3;
                opUid2 = workSource.getUid(0);
                opPackageName2 = str;
            }
            opPid = -1;
            opPackageName = opPackageName2;
            opUid = opUid2;
        } else {
            java.lang.String opPackageName4 = wakeLock.mPackageName;
            int opUid3 = wakeLock.mOwnerUid;
            int opPid2 = wakeLock.mOwnerPid;
            opPid = opPid2;
            opPackageName = opPackageName4;
            opUid = opUid3;
        }
        java.lang.Integer powerGroupId = wakeLock.getPowerGroupId();
        if (powerGroupId != null && isAcquireCausesWakeupFlagAllowed(opPackageName, opUid, opPid)) {
            if (powerGroupId.intValue() == -1) {
                if (DEBUG_SPEW) {
                    android.util.Slog.d(TAG, "Waking up all power groups");
                }
                for (int idx = 0; idx < this.mPowerGroups.size(); idx++) {
                    wakePowerGroupLocked(this.mPowerGroups.valueAt(idx), this.mClock.uptimeMillis(), 2, wakeLock.mTag, opUid, opPackageName, opUid);
                }
                return;
            }
            if (this.mPowerGroups.contains(powerGroupId.intValue())) {
                if (DEBUG_SPEW) {
                    android.util.Slog.d(TAG, "Waking up power group " + powerGroupId);
                }
                wakePowerGroupLocked(this.mPowerGroups.get(powerGroupId.intValue()), this.mClock.uptimeMillis(), 2, wakeLock.mTag, opUid, opPackageName, opUid);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseWakeLockInternal(android.os.IBinder lock, int flags) {
        mPmsExt.releaseBaseProxyedWakeLockInternalLocked(lock);
        synchronized (this.mLock) {
            int index = findWakeLockIndexLocked(lock);
            if (index < 0) {
                if (DEBUG_PANIC || DEBUG_SPEW) {
                    android.util.Slog.d(TAG, "releaseWakeLockInternal: lock=" + java.util.Objects.hashCode(lock) + " [not found], flags=0x" + java.lang.Integer.toHexString(flags));
                }
                return;
            }
            com.android.server.power.PowerManagerService.WakeLock wakeLock = this.mWakeLocks.get(index);
            mPmsExt.releaseWakeLockInternalLocked(wakeLock, flags);
            if (DEBUG_SPEW) {
                android.util.Slog.d(TAG, "releaseWakeLockInternal: lock=" + java.util.Objects.hashCode(lock) + " [" + wakeLock.toString() + "], flags=0x" + java.lang.Integer.toHexString(flags) + ", total_time=" + wakeLock.mWakeLockExt.getTotalTime() + "ms");
            }
            if ((flags & 1) != 0) {
                this.mRequestWaitForNegativeProximity = true;
            }
            wakeLock.unlinkToDeath();
            wakeLock.setDisabled(true);
            removeWakeLockLocked(wakeLock, index);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleWakeLockDeath(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        synchronized (this.mLock) {
            if (DEBUG_SPEW) {
                android.util.Slog.d(TAG, "handleWakeLockDeath: lock=" + java.util.Objects.hashCode(wakeLock.mLock) + " [" + wakeLock.mTag + "]");
            }
            mPmsExt.handleBaseWakeLockDeath(wakeLock);
            int index = this.mWakeLocks.indexOf(wakeLock);
            if (index < 0) {
                return;
            }
            removeWakeLockLocked(wakeLock, index);
        }
    }

    private void removeWakeLockNoUpdateLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, int index) {
        removeWakeLockNoUpdateLocked(wakeLock, index, -1);
    }

    private void removeWakeLockNoUpdateLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, int index, int releaseReason) {
        this.mWakeLocks.remove(index);
        mPmsExt.onRemoveWakeLockLocked(wakeLock.mOwnerPid, wakeLock);
        com.android.server.power.PowerManagerService.UidState state = wakeLock.mUidState;
        state.mNumWakeLocks--;
        if (state.mNumWakeLocks <= 0 && state.mProcState == 20) {
            this.mUidState.remove(state.mUid);
        }
        notifyWakeLockReleasedLocked(wakeLock, releaseReason);
        applyWakeLockFlagsOnReleaseLocked(wakeLock);
        this.mDirty |= 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeWakeLockLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, int index) {
        removeWakeLockNoUpdateLocked(wakeLock, index);
        updatePowerStateLocked();
    }

    private void applyWakeLockFlagsOnReleaseLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        if ((wakeLock.mFlags & 536870912) != 0 && isScreenLock(wakeLock) && !mPmsExt.isWakelockNeedIgnoreOnAfterRelease(wakeLock)) {
            userActivityNoUpdateLocked(this.mClock.uptimeMillis(), 0, 1, wakeLock.mOwnerUid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWakeLockWorkSourceInternal(android.os.IBinder lock, android.os.WorkSource ws, java.lang.String historyTag, int callingUid) {
        synchronized (this.mLock) {
            int index = findWakeLockIndexLocked(lock);
            if (index < 0) {
                if (DEBUG_SPEW) {
                    android.util.Slog.d(TAG, "updateWakeLockWorkSourceInternal: lock=" + java.util.Objects.hashCode(lock) + " [not found], ws=" + ws);
                }
                mPmsExt.updateProxyedWakeLockWorkSource(lock, ws, historyTag, null);
                return;
            }
            com.android.server.power.PowerManagerService.WakeLock wakeLock = this.mWakeLocks.get(index);
            if (mPmsExt.updateProxyedWakeLockWorkSource(lock, ws, historyTag, wakeLock)) {
                return;
            }
            if (DEBUG_SPEW) {
                android.util.Slog.d(TAG, "updateWakeLockWorkSourceInternal: lock=" + java.util.Objects.hashCode(lock) + " [" + wakeLock.mTag + "], ws=" + ws);
            }
            if (!wakeLock.hasSameWorkSource(ws)) {
                notifyWakeLockChangingLocked(wakeLock, wakeLock.mFlags, wakeLock.mTag, wakeLock.mPackageName, wakeLock.mOwnerUid, wakeLock.mOwnerPid, ws, historyTag, null);
                wakeLock.mHistoryTag = historyTag;
                wakeLock.updateWorkSource(ws);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWakeLockCallbackInternal(android.os.IBinder lock, android.os.IWakeLockCallback callback, int callingUid) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    int index = findWakeLockIndexLocked(lock);
                    if (index >= 0) {
                        com.android.server.power.PowerManagerService.WakeLock wakeLock = this.mWakeLocks.get(index);
                        if (DEBUG_SPEW) {
                            android.util.Slog.d(TAG, "updateWakeLockCallbackInternal: lock=" + java.util.Objects.hashCode(lock) + " [" + wakeLock.mTag + "]");
                        }
                        if (!isSameCallback(callback, wakeLock.mCallback)) {
                            notifyWakeLockChangingLocked(wakeLock, wakeLock.mFlags, wakeLock.mTag, wakeLock.mPackageName, wakeLock.mOwnerUid, wakeLock.mOwnerPid, wakeLock.mWorkSource, wakeLock.mHistoryTag, callback);
                            wakeLock.mCallback = callback;
                        }
                        return;
                    }
                    if (DEBUG_SPEW) {
                        android.util.Slog.d(TAG, "updateWakeLockCallbackInternal: lock=" + java.util.Objects.hashCode(lock) + " [not found]");
                    }
                    try {
                        throw new java.lang.IllegalArgumentException("Wake lock not active: " + lock + " from uid " + callingUid);
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findWakeLockIndexLocked(android.os.IBinder lock) {
        int count = this.mWakeLocks.size();
        for (int i = 0; i < count; i++) {
            if (this.mWakeLocks.get(i).mLock == lock) {
                return i;
            }
        }
        return -1;
    }

    com.android.server.power.PowerManagerService.WakeLock findWakeLockLocked(android.os.IBinder lock) {
        int index = findWakeLockIndexLocked(lock);
        if (index == -1) {
            return null;
        }
        return this.mWakeLocks.get(index);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWakeLockAcquiredLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        if (this.mSystemReady && !wakeLock.mDisabled) {
            wakeLock.mNotifiedAcquired = true;
            this.mNotifier.onWakeLockAcquired(wakeLock.mFlags, wakeLock.mTag, wakeLock.mPackageName, wakeLock.mOwnerUid, wakeLock.mOwnerPid, wakeLock.mWorkSource, wakeLock.mHistoryTag, wakeLock.mCallback);
            restartNofifyLongTimerLocked(wakeLock);
            mPmsExt.noteWakeLockChange(wakeLock, true);
        }
    }

    private void enqueueNotifyLongMsgLocked(long time) {
        this.mNotifyLongScheduled = time;
        android.os.Message msg = this.mHandler.obtainMessage(4);
        msg.setAsynchronous(true);
        this.mHandler.sendMessageAtTime(msg, time);
    }

    private void restartNofifyLongTimerLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        wakeLock.mAcquireTime = this.mClock.uptimeMillis();
        if ((wakeLock.mFlags & 65535) == 1 && this.mNotifyLongScheduled == 0) {
            enqueueNotifyLongMsgLocked(wakeLock.mAcquireTime + 60000);
        }
    }

    private void notifyWakeLockLongStartedLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        if (this.mSystemReady && !wakeLock.mDisabled) {
            wakeLock.mNotifiedLong = true;
            this.mNotifier.onLongPartialWakeLockStart(wakeLock.mTag, wakeLock.mOwnerUid, wakeLock.mWorkSource, wakeLock.mHistoryTag);
        }
    }

    private void notifyWakeLockLongFinishedLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        if (wakeLock.mNotifiedLong) {
            wakeLock.mNotifiedLong = false;
            this.mNotifier.onLongPartialWakeLockFinish(wakeLock.mTag, wakeLock.mOwnerUid, wakeLock.mWorkSource, wakeLock.mHistoryTag);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWakeLockChangingLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, int flags, java.lang.String tag, java.lang.String packageName, int uid, int pid, android.os.WorkSource ws, java.lang.String historyTag, android.os.IWakeLockCallback callback) {
        if (this.mSystemReady && wakeLock.mNotifiedAcquired) {
            this.mNotifier.onWakeLockChanging(wakeLock.mFlags, wakeLock.mTag, wakeLock.mPackageName, wakeLock.mOwnerUid, wakeLock.mOwnerPid, wakeLock.mWorkSource, wakeLock.mHistoryTag, wakeLock.mCallback, flags, tag, packageName, uid, pid, ws, historyTag, callback);
            notifyWakeLockLongFinishedLocked(wakeLock);
            restartNofifyLongTimerLocked(wakeLock);
            mPmsExt.noteWorkSourceChange(wakeLock, ws);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWakeLockReleasedLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        notifyWakeLockReleasedLocked(wakeLock, -1);
    }

    private void notifyWakeLockReleasedLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, int releaseReason) {
        if (this.mSystemReady && wakeLock.mNotifiedAcquired) {
            wakeLock.mNotifiedAcquired = false;
            wakeLock.mAcquireTime = 0L;
            this.mNotifier.onWakeLockReleased(wakeLock.mFlags, wakeLock.mTag, wakeLock.mPackageName, wakeLock.mOwnerUid, wakeLock.mOwnerPid, wakeLock.mWorkSource, wakeLock.mHistoryTag, wakeLock.mCallback, releaseReason);
            notifyWakeLockLongFinishedLocked(wakeLock);
            mPmsExt.noteWakeLockChange(wakeLock, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isWakeLockLevelSupportedInternal(int level) {
        synchronized (this.mLock) {
            boolean z = false;
            try {
                switch (level) {
                    case 1:
                    case 6:
                    case 10:
                    case 26:
                    case 64:
                    case 128:
                        return true;
                    case 32:
                        if (this.mSystemReady && this.mDisplayManagerInternal.isProximitySensorAvailable()) {
                            z = true;
                        }
                        return z;
                    case 256:
                        if (this.mSystemReady && this.mFeatureFlags.isEarlyScreenTimeoutDetectorEnabled() && this.mScreenTimeoutOverridePolicy != null) {
                            z = true;
                        }
                        return z;
                    default:
                        return false;
                }
            } finally {
            }
        }
    }

    private void userActivityFromNative(long eventTime, int event, int displayId, int flags) {
        mPmsExt.oplusUserActivityInternal(displayId, eventTime, event, flags, 1000, sAnrLogEnhancementHelper, "input_pokeUserActivity");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void userActivityInternal(int displayId, long eventTime, int event, int flags, int uid) {
        synchronized (this.mLock) {
            if (displayId == -1) {
                if (userActivityNoUpdateLocked(eventTime, event, flags, uid)) {
                    updatePowerStateLocked();
                }
                return;
            }
            android.view.DisplayInfo displayInfo = this.mDisplayManagerInternal.getDisplayInfo(displayId);
            if (displayInfo == null) {
                return;
            }
            int groupId = displayInfo.displayGroupId;
            if (groupId == -1) {
                return;
            }
            mPmsExt.userActivity(displayId, eventTime, event, flags, uid);
            if (userActivityNoUpdateLocked(this.mPowerGroups.get(groupId), eventTime, event, flags, uid)) {
                updatePowerStateLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void napInternal(long eventTime, int uid, boolean allowWake) {
        synchronized (this.mLock) {
            dreamPowerGroupLocked(this.mPowerGroups.get(0), eventTime, uid, allowWake);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserAttention() {
        synchronized (this.mLock) {
            if (userActivityNoUpdateLocked(this.mPowerGroups.get(0), this.mClock.uptimeMillis(), 4, 0, 1000)) {
                mPmsExt.uploadAttentionChangeTimeout(java.lang.System.currentTimeMillis());
                updatePowerStateLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean userActivityNoUpdateLocked(long eventTime, int event, int flags, int uid) {
        boolean updatePowerState = false;
        for (int idx = 0; idx < this.mPowerGroups.size(); idx++) {
            if (userActivityNoUpdateLocked(this.mPowerGroups.valueAt(idx), eventTime, event, flags, uid)) {
                updatePowerState = true;
            }
        }
        return updatePowerState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean userActivityNoUpdateLocked(com.android.server.power.PowerGroup powerGroup, long eventTime, int event, int flags, int uid) {
        int groupId = powerGroup.getGroupId();
        android.util.Slog.d(TAG, "userActivityNoUpdateLocked: groupId=" + groupId + ", eventTime=" + eventTime + ", event=" + android.os.PowerManager.userActivityEventToString(event) + ", flags=0x" + java.lang.Integer.toHexString(flags) + ", uid=" + uid);
        if (eventTime < powerGroup.getLastSleepTimeLocked() || eventTime < powerGroup.getLastWakeTimeLocked() || !this.mSystemReady) {
            return false;
        }
        int injectEvent = event - 100;
        if (injectEvent >= 0 && injectEvent <= 6) {
            return false;
        }
        if (powerGroup.getGroupId() == 0) {
            mPmsExt.onUserActivityNoUpdateLocked(isGloballyInteractiveInternal(), event, uid);
        }
        android.os.Trace.traceBegin(131072L, "userActivity");
        try {
            if (eventTime > this.mLastInteractivePowerHintTime) {
                setPowerBoostInternal(0, 0);
                this.mLastInteractivePowerHintTime = eventTime;
            }
            this.mNotifier.onUserActivity(powerGroup.getGroupId(), event, uid);
            this.mAttentionDetector.onUserActivity(eventTime, event);
            if (this.mScreenTimeoutOverridePolicy != null) {
                this.mScreenTimeoutOverridePolicy.onUserActivity(this.mWakeLockSummary, event);
            }
            if (this.mUserInactiveOverrideFromWindowManager) {
                this.mUserInactiveOverrideFromWindowManager = false;
                this.mOverriddenTimeout = -1L;
            }
            int wakefulness = powerGroup.getWakefulnessLocked();
            if (wakefulness != 0 && wakefulness != 3 && (flags & 2) == 0) {
                maybeUpdateForegroundProfileLastActivityLocked(eventTime);
                if ((flags & 1) != 0) {
                    if (eventTime > powerGroup.getLastUserActivityTimeNoChangeLightsLocked() && eventTime > powerGroup.getLastUserActivityTimeLocked()) {
                        powerGroup.setLastUserActivityTimeNoChangeLightsLocked(eventTime, event);
                        this.mDirty |= 4;
                        if (event == 1) {
                            this.mDirty |= 4096;
                        }
                        return true;
                    }
                } else if (eventTime > powerGroup.getLastUserActivityTimeLocked()) {
                    powerGroup.setLastUserActivityTimeLocked(eventTime, event);
                    this.mDirty |= 4;
                    if (event == 1) {
                        this.mDirty |= 4096;
                    }
                    if (powerGroup.getGroupId() == 0) {
                        mPmsExt.userActivityNoUpdateChangeLightsLocked();
                    }
                    return true;
                }
                return false;
            }
            return false;
        } finally {
            android.os.Trace.traceEnd(131072L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeUpdateForegroundProfileLastActivityLocked(long eventTime) {
        com.android.server.power.PowerManagerService.ProfilePowerState profile = this.mProfilePowerState.get(this.mForegroundProfile);
        if (profile != null && eventTime > profile.mLastUserActivityTime) {
            profile.mLastUserActivityTime = eventTime;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakePowerGroupLocked(com.android.server.power.PowerGroup powerGroup, long eventTime, int reason, java.lang.String details, int uid, java.lang.String opPackageName, int opUid) throws java.lang.Throwable {
        java.lang.String details2;
        int reason2;
        if (DEBUG_SPEW) {
            android.util.Slog.d(TAG, "wakePowerGroupLocked: eventTime=" + eventTime + ", groupId=" + powerGroup.getGroupId() + ", reason=" + android.os.PowerManager.wakeReasonToString(reason) + ", uid=" + uid);
        }
        if (this.mForceSuspendActive || !this.mSystemReady) {
            android.util.Slog.d(TAG, "wakePowerGroupLocked ignore, mForceSuspendActive=" + this.mForceSuspendActive + " mSystemReady=" + this.mSystemReady);
            return;
        }
        if (powerGroup.getGroupId() != 0) {
            details2 = details;
            reason2 = reason;
        } else {
            java.lang.String details3 = mPmsExt.handleWakeUpdetailsEarly(details, uid, opPackageName, opUid);
            details2 = details3;
            reason2 = mPmsExt.handleWakeUpReasonEarly(reason, details3);
        }
        mPmsExt.cancelCheck(details2);
        java.lang.String details4 = details2;
        if (mPmsExt.interceptWakeDisplayGroupNoUpdateLocked(powerGroup.getGroupId(), eventTime, reason2, details2, uid, opPackageName, opUid, DEBUG_SPEW)) {
            return;
        }
        if (eventTime < powerGroup.getLastSleepTimeLocked() || powerGroup.getWakefulnessLocked() == 1) {
            mPmsExt.notePowerkeyProcessStagePoint("CANCELED_wakePowerGroupLocked");
            return;
        }
        mPmsExt.wakeDisplayGroupNoUpdateLockedStart(powerGroup.getGroupId(), eventTime, details4, uid, opPackageName, opUid);
        powerGroup.wakeUpLocked(eventTime, reason2, details4, uid, opPackageName, opUid, com.android.internal.util.LatencyTracker.getInstance(this.mContext));
    }

    private boolean dreamPowerGroupLocked(com.android.server.power.PowerGroup powerGroup, long eventTime, int uid, boolean allowWake) {
        if (DEBUG_SPEW) {
            android.util.Slog.d(TAG, "dreamPowerGroup: groupId=" + powerGroup.getGroupId() + ", eventTime=" + eventTime + ", uid=" + uid);
        }
        if (!this.mBootCompleted || !this.mSystemReady) {
            return false;
        }
        return powerGroup.dreamLocked(eventTime, uid, allowWake);
    }

    private boolean dozePowerGroupLocked(com.android.server.power.PowerGroup powerGroup, long eventTime, int reason, int uid) {
        if (DEBUG_SPEW) {
            mPmsExt.printStackTraceInfo();
        }
        if (DEBUG_PANIC || DEBUG_SPEW) {
            android.util.Slog.d(TAG, "dozePowerGroup: eventTime=" + eventTime + ", groupId=" + powerGroup.getGroupId() + ", reason=" + android.os.PowerManager.sleepReasonToString(reason) + ", uid=" + uid);
        }
        if (this.mSystemReady && this.mBootCompleted && !mPmsExt.interceptSleepDisplayGroupNoUpdateLocked(powerGroup.getGroupId(), eventTime, reason, 0, uid)) {
            return powerGroup.dozeLocked(eventTime, uid, reason);
        }
        return false;
    }

    private boolean sleepPowerGroupLocked(com.android.server.power.PowerGroup powerGroup, long eventTime, int reason, int uid) {
        if (DEBUG_SPEW) {
            mPmsExt.printStackTraceInfo();
        }
        if (DEBUG_PANIC || DEBUG_SPEW) {
            android.util.Slog.d(TAG, "sleepPowerGroup: eventTime=" + eventTime + ", groupId=" + powerGroup.getGroupId() + ", reason=" + android.os.PowerManager.sleepReasonToString(reason) + ", uid=" + uid);
        }
        if (!this.mBootCompleted || !this.mSystemReady) {
            return false;
        }
        if (getGlobalWakefulnessLocked() == 3 || getGlobalWakefulnessLocked() == 0 || !mPmsExt.interceptSleepDisplayGroupNoUpdateLocked(powerGroup.getGroupId(), eventTime, reason, 0, uid)) {
            return powerGroup.sleepLocked(eventTime, uid, reason);
        }
        return false;
    }

    void setWakefulnessLocked(int groupId, int wakefulness, long eventTime, int uid, int reason, int opUid, java.lang.String opPackageName, java.lang.String details) {
        this.mPowerGroups.get(groupId).setWakefulnessLocked(wakefulness, eventTime, uid, reason, opUid, opPackageName, details);
        this.mInjector.invalidateIsInteractiveCaches();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0189  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x018a A[Catch: all -> 0x01d4, TryCatch #0 {all -> 0x01d4, blocks: (B:29:0x017e, B:30:0x0186, B:32:0x018a, B:34:0x019d, B:35:0x01a4, B:38:0x01ab, B:40:0x01b5, B:41:0x01c3, B:44:0x01c9, B:43:0x01c7, B:45:0x01cc), top: B:54:0x017e }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01a4 A[Catch: all -> 0x01d4, TryCatch #0 {all -> 0x01d4, blocks: (B:29:0x017e, B:30:0x0186, B:32:0x018a, B:34:0x019d, B:35:0x01a4, B:38:0x01ab, B:40:0x01b5, B:41:0x01c3, B:44:0x01c9, B:43:0x01c7, B:45:0x01cc), top: B:54:0x017e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void updateGlobalWakefulnessLocked(long r20, int r22, int r23, int r24, java.lang.String r25, java.lang.String r26) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 518
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.PowerManagerService.updateGlobalWakefulnessLocked(long, int, int, int, java.lang.String, java.lang.String):void");
    }

    int getGlobalWakefulnessLocked() {
        return this.mWakefulnessRaw;
    }

    int getWakefulnessLocked(int groupId) {
        return this.mPowerGroups.get(groupId).getWakefulnessLocked();
    }

    int recalculateGlobalWakefulnessLocked() {
        int deviceWakefulness = 0;
        for (int i = 0; i < this.mPowerGroups.size(); i++) {
            int wakefulness = this.mPowerGroups.valueAt(i).getWakefulnessLocked();
            if (wakefulness == 1) {
                return 1;
            }
            if (wakefulness == 2 && (deviceWakefulness == 0 || deviceWakefulness == 3)) {
                deviceWakefulness = 2;
            } else if (wakefulness == 3 && deviceWakefulness == 0) {
                deviceWakefulness = 3;
            }
        }
        return deviceWakefulness;
    }

    void onPowerGroupEventLocked(int event, com.android.server.power.PowerGroup powerGroup) throws java.lang.Throwable {
        int reason;
        this.mWakefulnessChanging = true;
        this.mDirty |= 2;
        int groupId = powerGroup.getGroupId();
        if (event == 1) {
            this.mPowerGroups.delete(groupId);
        }
        int oldWakefulness = getGlobalWakefulnessLocked();
        int newWakefulness = recalculateGlobalWakefulnessLocked();
        if (event == 0 && newWakefulness == 1) {
            android.util.Slog.d(TAG, "onPowerGroupEventLocked: userActivityNoUpdateLocked.  groupId = " + groupId);
            userActivityNoUpdateLocked(powerGroup, this.mClock.uptimeMillis(), 0, 0, 1000);
            this.mNotifier.onGroupWakefulnessChangeStarted(groupId, powerGroup.getWakefulnessLocked(), 10, this.mClock.uptimeMillis());
        } else if (event == 1) {
            this.mNotifier.onGroupRemoved(groupId);
        }
        if (oldWakefulness != newWakefulness) {
            int reason2 = 11;
            switch (newWakefulness) {
                case 1:
                    if (event == 0) {
                        reason2 = 10;
                    }
                    reason = reason2;
                    break;
                case 2:
                default:
                    reason = 0;
                    break;
                case 3:
                    if (event != 1) {
                        reason2 = 12;
                    }
                    reason = reason2;
                    break;
            }
            updateGlobalWakefulnessLocked(this.mClock.uptimeMillis(), reason, 1000, 1000, this.mContext.getOpPackageName(), "groupId: " + groupId);
        }
        this.mDirty |= 65536;
        updatePowerStateLocked();
    }

    private void logSleepTimeoutRecapturedLocked() {
        long now = this.mClock.uptimeMillis();
        long savedWakeTimeMs = this.mOverriddenTimeout - now;
        if (savedWakeTimeMs >= 0) {
            com.android.server.EventLogTags.writePowerSoftSleepRequested(savedWakeTimeMs);
            this.mOverriddenTimeout = -1L;
        }
    }

    private void finishWakefulnessChangeIfNeededLocked() {
        if (this.mWakefulnessChanging && areAllPowerGroupsReadyLocked()) {
            if (getGlobalWakefulnessLocked() == 3 && (this.mWakeLockSummary & 64) == 0) {
                return;
            }
            this.mDozeStartInProgress = false;
            if (getGlobalWakefulnessLocked() == 3 || getGlobalWakefulnessLocked() == 0) {
                logSleepTimeoutRecapturedLocked();
            }
            this.mWakefulnessChanging = false;
            mPmsExt.onWakefulnessChangeFinished(this.mPowerGroups.get(0).getWakefulnessLocked());
            this.mNotifier.onWakefulnessChangeFinished();
        }
    }

    private boolean areAllPowerGroupsReadyLocked() {
        int size = this.mPowerGroups.size();
        for (int i = 0; i < size; i++) {
            if (!this.mPowerGroups.valueAt(i).isReadyLocked()) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePowerStateLocked() {
        int dirtyPhase1;
        if (!this.mSystemReady || this.mDirty == 0 || this.mUpdatePowerStateInProgress) {
            return;
        }
        if (!java.lang.Thread.holdsLock(this.mLock)) {
            android.util.Slog.wtf(TAG, "Power manager lock was not held when calling updatePowerStateLocked");
        }
        android.os.Trace.traceBegin(131072L, "updatePowerState");
        this.mUpdatePowerStateInProgress = true;
        try {
            mPmsExt.setAodSettingStatus();
            updateIsPoweredLocked(this.mDirty);
            updateStayOnLocked(this.mDirty);
            updateScreenBrightnessBoostLocked(this.mDirty);
            long now = this.mClock.uptimeMillis();
            int dirtyPhase2 = 0;
            do {
                dirtyPhase1 = this.mDirty;
                dirtyPhase2 |= dirtyPhase1;
                this.mDirty = 0;
                updateWakeLockSummaryLocked(dirtyPhase1);
                updateUserActivitySummaryLocked(now, dirtyPhase1);
                updateAttentiveStateLocked(now, dirtyPhase1);
            } while (updateWakefulnessLocked(dirtyPhase1));
            updateProfilesLocked(now);
            boolean powerGroupsBecameReady = updatePowerGroupsLocked(dirtyPhase2);
            updateDreamLocked(dirtyPhase2, powerGroupsBecameReady);
            finishWakefulnessChangeIfNeededLocked();
            updateSuspendBlockerLocked();
        } finally {
            android.os.Trace.traceEnd(131072L);
            this.mUpdatePowerStateInProgress = false;
        }
    }

    private void updateProfilesLocked(long now) {
        int numProfiles = this.mProfilePowerState.size();
        for (int i = 0; i < numProfiles; i++) {
            com.android.server.power.PowerManagerService.ProfilePowerState profile = this.mProfilePowerState.valueAt(i);
            if (isProfileBeingKeptAwakeLocked(profile, now)) {
                profile.mLockingNotified = false;
            } else if (!profile.mLockingNotified) {
                profile.mLockingNotified = true;
                this.mNotifier.onProfileTimeout(profile.mUserId);
            }
        }
    }

    private boolean isProfileBeingKeptAwakeLocked(com.android.server.power.PowerManagerService.ProfilePowerState profile, long now) {
        return profile.mLastUserActivityTime + profile.mScreenOffTimeout > now || (profile.mWakeLockSummary & 32) != 0 || (this.mProximityPositive && (profile.mWakeLockSummary & 16) != 0);
    }

    private void updateIsPoweredLocked(int dirty) throws java.lang.Throwable {
        boolean dockedOnWirelessCharger;
        if ((dirty & 256) != 0) {
            boolean wasPowered = this.mIsPowered;
            int oldPlugType = this.mPlugType;
            this.mIsPowered = this.mBatteryManagerInternal.isPowered(15);
            this.mPlugType = this.mBatteryManagerInternal.getPlugType();
            int oldBatteryLevel = this.mBatteryLevel;
            this.mBatteryLevel = this.mBatteryManagerInternal.getBatteryLevel();
            this.mBatteryLevelLow = this.mBatteryManagerInternal.getBatteryLevelLow();
            boolean isOverheat = this.mBatteryManagerInternal.getBatteryHealth() == 3;
            if (DEBUG_SPEW) {
                android.util.Slog.d(TAG, "updateIsPoweredLocked: wasPowered=" + wasPowered + ", mIsPowered=" + this.mIsPowered + ", oldPlugType=" + oldPlugType + ", mPlugType=" + this.mPlugType + ", oldBatteryLevel=" + oldBatteryLevel + ", mBatteryLevel=" + this.mBatteryLevel + ", isOverheat=" + isOverheat);
            }
            if (!isOverheat && oldBatteryLevel > 0 && getGlobalWakefulnessLocked() == 2) {
                this.mDreamsBatteryLevelDrain += oldBatteryLevel - this.mBatteryLevel;
            }
            if (wasPowered != this.mIsPowered || oldPlugType != this.mPlugType) {
                this.mDirty |= 64;
                boolean dockedOnWirelessCharger2 = this.mWirelessChargerDetector.update(this.mIsPowered, this.mPlugType);
                long now = this.mClock.uptimeMillis();
                if (!shouldWakeUpWhenPluggedOrUnpluggedLocked(wasPowered, oldPlugType, dockedOnWirelessCharger2)) {
                    dockedOnWirelessCharger = dockedOnWirelessCharger2;
                } else {
                    dockedOnWirelessCharger = dockedOnWirelessCharger2;
                    wakePowerGroupLocked(this.mPowerGroups.get(0), now, 3, "android.server.power:PLUGGED:" + this.mIsPowered, 1000, this.mContext.getOpPackageName(), 1000);
                }
                userActivityNoUpdateLocked(this.mPowerGroups.get(0), now, 0, 0, 1000);
                if (this.mBootCompleted) {
                    if (this.mIsPowered && !android.os.BatteryManager.isPlugWired(oldPlugType) && android.os.BatteryManager.isPlugWired(this.mPlugType)) {
                        this.mNotifier.onWiredChargingStarted(this.mUserId);
                    } else if (dockedOnWirelessCharger) {
                        this.mNotifier.onWirelessChargingStarted(this.mBatteryLevel, this.mUserId);
                    }
                }
            }
            if (this.mBatterySaverSupported) {
                this.mBatterySaverStateMachine.setBatteryStatus(this.mIsPowered, this.mBatteryLevel, this.mBatteryLevelLow);
            }
        }
    }

    private boolean shouldWakeUpWhenPluggedOrUnpluggedLocked(boolean wasPowered, int oldPlugType, boolean dockedOnWirelessCharger) {
        if (!this.mWakeUpWhenPluggedOrUnpluggedConfig) {
            return false;
        }
        if (this.mKeepDreamingWhenUnplugging && getGlobalWakefulnessLocked() == 2 && wasPowered && !this.mIsPowered) {
            return false;
        }
        if (this.mIsPowered && getGlobalWakefulnessLocked() == 2) {
            return false;
        }
        if (!this.mTheaterModeEnabled || this.mWakeUpWhenPluggedOrUnpluggedInTheaterModeConfig) {
            return (this.mAlwaysOnEnabled && getGlobalWakefulnessLocked() == 3) ? false : true;
        }
        return false;
    }

    private void updateStayOnLocked(int dirty) {
        if ((dirty & 288) != 0) {
            boolean wasStayOn = this.mStayOn;
            if (this.mStayOnWhilePluggedInSetting != 0 && !isMaximumScreenOffTimeoutFromDeviceAdminEnforcedLocked()) {
                this.mStayOn = this.mBatteryManagerInternal.isPowered(this.mStayOnWhilePluggedInSetting);
            } else {
                this.mStayOn = false;
            }
            if (this.mStayOn != wasStayOn) {
                this.mDirty |= 128;
            }
        }
    }

    private void updateWakeLockSummaryLocked(int dirty) {
        if ((65539 & dirty) != 0) {
            this.mWakeLockSummary = 0;
            mPmsExt.updateWakeLockSummaryLockedStart();
            int numProfiles = this.mProfilePowerState.size();
            for (int i = 0; i < numProfiles; i++) {
                this.mProfilePowerState.valueAt(i).mWakeLockSummary = 0;
            }
            for (int idx = 0; idx < this.mPowerGroups.size(); idx++) {
                this.mPowerGroups.valueAt(idx).setWakeLockSummaryLocked(0);
            }
            int invalidGroupWakeLockSummary = 0;
            int numWakeLocks = this.mWakeLocks.size();
            for (int i2 = 0; i2 < numWakeLocks; i2++) {
                com.android.server.power.PowerManagerService.WakeLock wakeLock = this.mWakeLocks.get(i2);
                java.lang.Integer groupId = wakeLock.getPowerGroupId();
                if (groupId != null && (groupId.intValue() == -1 || this.mPowerGroups.contains(groupId.intValue()))) {
                    com.android.server.power.PowerGroup powerGroup = this.mPowerGroups.get(groupId.intValue());
                    int wakeLockFlags = getWakeLockSummaryFlags(wakeLock);
                    this.mWakeLockSummary |= wakeLockFlags;
                    if (groupId.intValue() != -1) {
                        int wakeLockSummary = powerGroup.getWakeLockSummaryLocked();
                        powerGroup.setWakeLockSummaryLocked(wakeLockSummary | wakeLockFlags);
                    } else {
                        invalidGroupWakeLockSummary |= wakeLockFlags;
                    }
                    for (int j = 0; j < numProfiles; j++) {
                        com.android.server.power.PowerManagerService.ProfilePowerState profile = this.mProfilePowerState.valueAt(j);
                        if (wakeLockAffectsUser(wakeLock, profile.mUserId)) {
                            profile.mWakeLockSummary |= wakeLockFlags;
                        }
                    }
                }
            }
            if (this.mScreenTimeoutOverridePolicy != null) {
                this.mScreenTimeoutOverridePolicy.checkScreenWakeLock(this.mWakeLockSummary);
            }
            for (int idx2 = 0; idx2 < this.mPowerGroups.size(); idx2++) {
                com.android.server.power.PowerGroup powerGroup2 = this.mPowerGroups.valueAt(idx2);
                int wakeLockSummary2 = adjustWakeLockSummary(powerGroup2.getWakefulnessLocked(), powerGroup2.getWakeLockSummaryLocked() | invalidGroupWakeLockSummary);
                powerGroup2.setWakeLockSummaryLocked(wakeLockSummary2);
            }
            int idx3 = getGlobalWakefulnessLocked();
            this.mWakeLockSummary = adjustWakeLockSummary(idx3, this.mWakeLockSummary);
            for (int i3 = 0; i3 < numProfiles; i3++) {
                com.android.server.power.PowerManagerService.ProfilePowerState profile2 = this.mProfilePowerState.valueAt(i3);
                profile2.mWakeLockSummary = adjustWakeLockSummary(getGlobalWakefulnessLocked(), profile2.mWakeLockSummary);
            }
            if (DEBUG_SPEW) {
                android.util.Slog.d(TAG, "updateWakeLockSummaryLocked: mWakefulness=" + android.os.PowerManagerInternal.wakefulnessToString(getGlobalWakefulnessLocked()) + ", mWakeLockSummary=0x" + java.lang.Integer.toHexString(this.mWakeLockSummary));
            }
        }
    }

    private static int adjustWakeLockSummary(int wakefulness, int wakeLockSummary) {
        if (wakefulness != 3) {
            wakeLockSummary &= -193;
        }
        if (wakefulness == 0 || (wakeLockSummary & 64) != 0) {
            wakeLockSummary &= -15;
            if (wakefulness == 0) {
                wakeLockSummary &= -17;
            }
        }
        if (mPmsExt.getProximityLockFromInCallUiValueLocked()) {
            wakeLockSummary |= 16;
        }
        if ((wakeLockSummary & 6) != 0) {
            if (wakefulness == 1) {
                wakeLockSummary |= 33;
            } else if (wakefulness == 2) {
                wakeLockSummary |= 1;
            }
        }
        if ((wakeLockSummary & 128) != 0) {
            return wakeLockSummary | 1;
        }
        return wakeLockSummary;
    }

    private int getWakeLockSummaryFlags(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        if (wakeLock.mDisabled) {
            return 0;
        }
        switch (wakeLock.mFlags & 65535) {
            case 1:
                break;
            case 6:
                if (mPmsExt.getIgnoreBright(wakeLock)) {
                }
                break;
            case 10:
                if (mPmsExt.getIgnoreBright(wakeLock)) {
                }
                break;
            case 26:
                if (mPmsExt.getIgnoreBright(wakeLock)) {
                }
                break;
            case 32:
                mPmsExt.getWakeLockSummaryFlags(wakeLock);
                if (wakeLock.mDisabled) {
                }
                break;
            case 64:
                break;
            case 128:
                break;
            case 256:
                break;
        }
        return 0;
    }

    private boolean wakeLockAffectsUser(com.android.server.power.PowerManagerService.WakeLock wakeLock, int userId) {
        if (wakeLock.mWorkSource != null) {
            for (int k = 0; k < wakeLock.mWorkSource.size(); k++) {
                int uid = wakeLock.mWorkSource.getUid(k);
                if (userId == android.os.UserHandle.getUserId(uid)) {
                    return true;
                }
            }
            java.util.List<android.os.WorkSource.WorkChain> workChains = wakeLock.mWorkSource.getWorkChains();
            if (workChains != null) {
                for (int k2 = 0; k2 < workChains.size(); k2++) {
                    int uid2 = workChains.get(k2).getAttributionUid();
                    if (userId == android.os.UserHandle.getUserId(uid2)) {
                        return true;
                    }
                }
            }
        }
        return userId == android.os.UserHandle.getUserId(wakeLock.mOwnerUid);
    }

    void checkForLongWakeLocks() {
        synchronized (this.mLock) {
            long now = this.mClock.uptimeMillis();
            this.mNotifyLongDispatched = now;
            long when = now - 60000;
            long nextCheckTime = Long.MAX_VALUE;
            int numWakeLocks = this.mWakeLocks.size();
            for (int i = 0; i < numWakeLocks; i++) {
                com.android.server.power.PowerManagerService.WakeLock wakeLock = this.mWakeLocks.get(i);
                if ((wakeLock.mFlags & 65535) == 1 && wakeLock.mNotifiedAcquired && !wakeLock.mNotifiedLong) {
                    if (wakeLock.mAcquireTime >= when) {
                        long checkTime = wakeLock.mAcquireTime + 60000;
                        if (checkTime < nextCheckTime) {
                            nextCheckTime = checkTime;
                        }
                    } else {
                        notifyWakeLockLongStartedLocked(wakeLock);
                    }
                }
            }
            this.mNotifyLongScheduled = 0L;
            this.mHandler.removeMessages(4);
            if (nextCheckTime != Long.MAX_VALUE) {
                this.mNotifyLongNextCheck = nextCheckTime;
                enqueueNotifyLongMsgLocked(nextCheckTime);
            } else {
                this.mNotifyLongNextCheck = 0L;
            }
        }
    }

    private void updateUserActivitySummaryLocked(long now, int dirty) {
        long nextTimeout;
        long defaultScreenOffTimeout;
        long screenOffTimeout;
        long screenOffTimeout2;
        long screenDimDuration;
        int idx;
        long sleepTimeout;
        long lastUserActivityTime;
        int groupUserActivitySummary;
        int groupUserActivitySummary2;
        long groupNextTimeout;
        long j = now;
        if ((dirty & 81959) == 0) {
            return;
        }
        this.mHandler.removeMessages(1);
        long attentiveTimeout = getAttentiveTimeoutLocked();
        long sleepTimeout2 = getSleepTimeoutLocked(attentiveTimeout);
        long defaultScreenOffTimeout2 = getScreenOffTimeoutLocked(sleepTimeout2, attentiveTimeout);
        long defaultScreenDimDuration = getScreenDimDurationLocked(defaultScreenOffTimeout2);
        boolean userInactiveOverride = this.mUserInactiveOverrideFromWindowManager;
        long lastUserActivityTimeNoChangeLights = -1;
        boolean hasUserActivitySummary = false;
        int idx2 = 0;
        while (true) {
            long attentiveTimeout2 = attentiveTimeout;
            if (idx2 >= this.mPowerGroups.size()) {
                break;
            }
            int groupUserActivitySummary3 = 0;
            long groupNextTimeout2 = 0;
            com.android.server.power.PowerGroup powerGroup = this.mPowerGroups.valueAt(idx2);
            int wakefulness = powerGroup.getWakefulnessLocked();
            long screenOffTimeout3 = defaultScreenOffTimeout2;
            long screenDimDuration2 = defaultScreenDimDuration;
            if (powerGroup.getGroupId() != 0) {
                defaultScreenOffTimeout = defaultScreenOffTimeout2;
                screenOffTimeout = screenOffTimeout3;
                screenOffTimeout2 = defaultScreenDimDuration;
                screenDimDuration = screenDimDuration2;
            } else {
                defaultScreenOffTimeout = defaultScreenOffTimeout2;
                screenOffTimeout2 = defaultScreenDimDuration;
                screenOffTimeout = getScreenOffTimeoutOverrideLocked(screenOffTimeout3, screenDimDuration2);
                screenDimDuration = getScreenDimDurationLocked(screenOffTimeout);
            }
            if (wakefulness == 0) {
                idx = idx2;
                sleepTimeout = sleepTimeout2;
            } else {
                idx = idx2;
                int groupUserActivitySummary4 = 0;
                long lastUserActivityTime2 = powerGroup.getLastUserActivityTimeLocked();
                long nextTimeout2 = lastUserActivityTimeNoChangeLights;
                long lastUserActivityTimeNoChangeLights2 = powerGroup.getLastUserActivityTimeNoChangeLightsLocked();
                if (lastUserActivityTime2 >= powerGroup.getLastWakeTimeLocked()) {
                    groupNextTimeout2 = (lastUserActivityTime2 + screenOffTimeout) - screenDimDuration;
                    if (j < groupNextTimeout2) {
                        groupUserActivitySummary4 = 1;
                    } else {
                        groupNextTimeout2 = lastUserActivityTime2 + screenOffTimeout;
                        if (j < groupNextTimeout2) {
                            groupUserActivitySummary4 = 2;
                        }
                    }
                }
                if (groupUserActivitySummary4 == 0 && lastUserActivityTimeNoChangeLights2 >= powerGroup.getLastWakeTimeLocked()) {
                    groupNextTimeout2 = lastUserActivityTimeNoChangeLights2 + screenOffTimeout;
                    if (j < groupNextTimeout2) {
                        if (powerGroup.isPolicyBrightLocked()) {
                            groupUserActivitySummary4 = 1;
                        } else if (powerGroup.isPolicyDimLocked()) {
                            groupUserActivitySummary4 = 2;
                        }
                    }
                }
                if (groupUserActivitySummary4 != 0) {
                    sleepTimeout = sleepTimeout2;
                    lastUserActivityTime = groupNextTimeout2;
                    groupUserActivitySummary = groupUserActivitySummary4;
                } else if (sleepTimeout2 >= 0) {
                    long anyUserActivity = java.lang.Math.max(lastUserActivityTime2, lastUserActivityTimeNoChangeLights2);
                    if (anyUserActivity >= powerGroup.getLastWakeTimeLocked()) {
                        groupNextTimeout2 = anyUserActivity + sleepTimeout2;
                        if (j < groupNextTimeout2) {
                            groupUserActivitySummary4 = 4;
                        }
                    }
                    sleepTimeout = sleepTimeout2;
                    lastUserActivityTime = groupNextTimeout2;
                    groupUserActivitySummary = groupUserActivitySummary4;
                } else {
                    sleepTimeout = sleepTimeout2;
                    lastUserActivityTime = -1;
                    groupUserActivitySummary = 4;
                }
                if (groupUserActivitySummary != 4 && userInactiveOverride) {
                    if ((groupUserActivitySummary & 3) != 0 && this.mOverriddenTimeout == -1) {
                        this.mOverriddenTimeout = lastUserActivityTime;
                    }
                    groupUserActivitySummary2 = 4;
                    lastUserActivityTime = -1;
                } else {
                    groupUserActivitySummary2 = groupUserActivitySummary;
                }
                if ((groupUserActivitySummary2 & 1) != 0 && (powerGroup.getWakeLockSummaryLocked() & 32) == 0 && !this.mIsFaceDown) {
                    lastUserActivityTime = this.mAttentionDetector.updateUserActivity(lastUserActivityTime, screenDimDuration);
                }
                if (!isAttentiveTimeoutExpired(powerGroup, j)) {
                    groupUserActivitySummary3 = groupUserActivitySummary2;
                    groupNextTimeout = lastUserActivityTime;
                } else {
                    groupUserActivitySummary3 = 0;
                    groupNextTimeout = -1;
                }
                boolean hasUserActivitySummary2 = hasUserActivitySummary | (groupUserActivitySummary3 != 0);
                if (nextTimeout2 == -1) {
                    hasUserActivitySummary = hasUserActivitySummary2;
                    groupNextTimeout2 = groupNextTimeout;
                    lastUserActivityTimeNoChangeLights = groupNextTimeout;
                } else if (groupNextTimeout == -1) {
                    lastUserActivityTimeNoChangeLights = nextTimeout2;
                    hasUserActivitySummary = hasUserActivitySummary2;
                    groupNextTimeout2 = groupNextTimeout;
                } else {
                    lastUserActivityTimeNoChangeLights = java.lang.Math.min(nextTimeout2, groupNextTimeout);
                    hasUserActivitySummary = hasUserActivitySummary2;
                    groupNextTimeout2 = groupNextTimeout;
                }
            }
            powerGroup.setUserActivitySummaryLocked(groupUserActivitySummary3);
            if (DEBUG_SPEW) {
                android.util.Slog.d(TAG, "updateUserActivitySummaryLocked: groupId=" + powerGroup.getGroupId() + ", mWakefulness=" + android.os.PowerManagerInternal.wakefulnessToString(wakefulness) + ", mUserActivitySummary=0x" + java.lang.Integer.toHexString(groupUserActivitySummary3) + ", nextTimeout=" + android.util.TimeUtils.formatUptime(groupNextTimeout2));
            }
            idx2 = idx + 1;
            j = now;
            attentiveTimeout = attentiveTimeout2;
            defaultScreenDimDuration = screenOffTimeout2;
            defaultScreenOffTimeout2 = defaultScreenOffTimeout;
            sleepTimeout2 = sleepTimeout;
        }
        long nextTimeout3 = lastUserActivityTimeNoChangeLights;
        long nextProfileTimeout = getNextProfileTimeoutLocked(now);
        if (nextProfileTimeout <= 0) {
            nextTimeout = nextTimeout3;
        } else {
            nextTimeout = java.lang.Math.min(nextTimeout3, nextProfileTimeout);
        }
        if (hasUserActivitySummary && nextTimeout >= 0) {
            scheduleUserInactivityTimeout(nextTimeout);
            mPmsExt.setNextTimeout(nextTimeout);
        }
        mPmsExt.screenOnWakelockCheck(getGlobalWakefulnessLocked(), this.mPowerGroups.get(0).getUserActivitySummaryLocked() == 4, this.mHandler.hasMessages(1));
    }

    private void scheduleUserInactivityTimeout(long timeMs) {
        android.os.Message msg = this.mHandler.obtainMessage(1);
        msg.setAsynchronous(true);
        this.mHandler.sendMessageAtTime(msg, timeMs);
    }

    private void scheduleAttentiveTimeout(long timeMs) {
        android.os.Message msg = this.mHandler.obtainMessage(5);
        msg.setAsynchronous(true);
        this.mHandler.sendMessageAtTime(msg, timeMs);
    }

    private long getNextProfileTimeoutLocked(long now) {
        long nextTimeout = -1;
        int numProfiles = this.mProfilePowerState.size();
        for (int i = 0; i < numProfiles; i++) {
            com.android.server.power.PowerManagerService.ProfilePowerState profile = this.mProfilePowerState.valueAt(i);
            long timeout = profile.mLastUserActivityTime + profile.mScreenOffTimeout;
            if (timeout > now && (nextTimeout == -1 || timeout < nextTimeout)) {
                nextTimeout = timeout;
            }
        }
        return nextTimeout;
    }

    private void updateAttentiveStateLocked(long now, int dirty) {
        long nextTimeout;
        long attentiveTimeout = getAttentiveTimeoutLocked();
        long goToSleepTime = this.mPowerGroups.get(0).getLastUserActivityTimeLocked() + attentiveTimeout;
        long showWarningTime = goToSleepTime - this.mAttentiveWarningDurationConfig;
        boolean warningDismissed = maybeHideInattentiveSleepWarningLocked(now, showWarningTime);
        if (attentiveTimeout >= 0) {
            if (!warningDismissed && (dirty & 19122) == 0) {
                return;
            }
            if (DEBUG_SPEW) {
                android.util.Slog.d(TAG, "Updating attentive state");
            }
            this.mHandler.removeMessages(5);
            if (getGlobalWakefulnessLocked() == 0 || isBeingKeptFromInattentiveSleepLocked()) {
                return;
            }
            if (now < showWarningTime) {
                nextTimeout = showWarningTime;
            } else if (now >= goToSleepTime) {
                nextTimeout = -1;
            } else {
                if (DEBUG) {
                    long timeToSleep = goToSleepTime - now;
                    android.util.Slog.d(TAG, "Going to sleep in " + timeToSleep + "ms if there is no user activity");
                }
                this.mInattentiveSleepWarningOverlayController.show();
                nextTimeout = goToSleepTime;
            }
            if (nextTimeout >= 0) {
                scheduleAttentiveTimeout(nextTimeout);
            }
        }
    }

    private boolean maybeHideInattentiveSleepWarningLocked(long now, long showWarningTime) {
        long attentiveTimeout = getAttentiveTimeoutLocked();
        if (!this.mInattentiveSleepWarningOverlayController.isShown()) {
            return false;
        }
        if (getGlobalWakefulnessLocked() == 0) {
            this.mInattentiveSleepWarningOverlayController.dismiss(false);
            return true;
        }
        if (attentiveTimeout >= 0 && !isBeingKeptFromInattentiveSleepLocked() && now >= showWarningTime) {
            return false;
        }
        this.mInattentiveSleepWarningOverlayController.dismiss(true);
        return true;
    }

    private boolean isAttentiveTimeoutExpired(com.android.server.power.PowerGroup powerGroup, long now) {
        long attentiveTimeout = getAttentiveTimeoutLocked();
        return powerGroup.getGroupId() == 0 && attentiveTimeout >= 0 && now >= powerGroup.getLastUserActivityTimeLocked() + attentiveTimeout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUserActivityTimeout() {
        synchronized (this.mLock) {
            if (DEBUG_PANIC || DEBUG_SPEW) {
                android.util.Slog.d(TAG, "handleUserActivityTimeout");
            }
            this.mDirty |= 4;
            updatePowerStateLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAttentiveTimeout() {
        synchronized (this.mLock) {
            if (DEBUG_SPEW) {
                android.util.Slog.d(TAG, "handleAttentiveTimeout");
            }
            this.mDirty |= 16384;
            updatePowerStateLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getAttentiveTimeoutLocked() {
        long timeout = this.mAttentiveTimeoutSetting;
        if (timeout <= 0) {
            return -1L;
        }
        return java.lang.Math.max(timeout, this.mMinimumScreenOffTimeoutConfig);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getSleepTimeoutLocked(long attentiveTimeout) {
        long timeout = this.mSleepTimeoutSetting;
        if (timeout <= 0) {
            return -1L;
        }
        if (attentiveTimeout >= 0) {
            timeout = java.lang.Math.min(timeout, attentiveTimeout);
        }
        return java.lang.Math.max(timeout, this.mMinimumScreenOffTimeoutConfig);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getScreenOffTimeoutLocked(long sleepTimeout, long attentiveTimeout) {
        long timeout = this.mScreenOffTimeoutSetting;
        if (isMaximumScreenOffTimeoutFromDeviceAdminEnforcedLocked()) {
            timeout = java.lang.Math.min(timeout, this.mMaximumScreenOffTimeoutFromDeviceAdmin);
        }
        if (this.mUserActivityTimeoutOverrideFromWindowManager >= 0) {
            timeout = java.lang.Math.min(timeout, this.mUserActivityTimeoutOverrideFromWindowManager);
        }
        if (sleepTimeout >= 0) {
            timeout = java.lang.Math.min(timeout, sleepTimeout);
        }
        if (attentiveTimeout >= 0) {
            timeout = java.lang.Math.min(timeout, attentiveTimeout);
        }
        return java.lang.Math.max(mPmsExt.getScreenOffTimeoutLocked(timeout), this.mMinimumScreenOffTimeoutConfig);
    }

    private long getScreenDimDurationLocked(long screenOffTimeout) {
        long screenDimDuration = mPmsExt.getScreenDimDurationLocked(screenOffTimeout, this.mIsFaceDown);
        if (screenDimDuration > 0) {
            return screenDimDuration;
        }
        return java.lang.Math.min(this.mMaximumScreenDimDurationConfig, (long) (screenOffTimeout * this.mMaximumScreenDimRatioConfig));
    }

    long getScreenOffTimeoutOverrideLocked(long screenOffTimeout, long screenDimDuration) {
        long shortestScreenOffTimeout = screenOffTimeout;
        if (this.mScreenTimeoutOverridePolicy != null) {
            shortestScreenOffTimeout = this.mScreenTimeoutOverridePolicy.getScreenTimeoutOverrideLocked(this.mWakeLockSummary, screenOffTimeout);
        }
        if (!this.mIsFaceDown) {
            return shortestScreenOffTimeout;
        }
        if (screenOffTimeout == 86400000) {
            return screenOffTimeout;
        }
        return java.lang.Math.min(screenDimDuration, shortestScreenOffTimeout);
    }

    private boolean updateWakefulnessLocked(int dirty) {
        boolean changed = false;
        if ((dirty & 20151) == 0) {
            return false;
        }
        long time = this.mClock.uptimeMillis();
        for (int idx = 0; idx < this.mPowerGroups.size(); idx++) {
            com.android.server.power.PowerGroup powerGroup = this.mPowerGroups.valueAt(idx);
            if (powerGroup.getWakefulnessLocked() == 1 && isItBedTimeYetLocked(powerGroup)) {
                if (DEBUG_PANIC || DEBUG_SPEW) {
                    android.util.Slog.d(TAG, "updateWakefulnessLocked: Bed time for group " + powerGroup.getGroupId());
                }
                if (isAttentiveTimeoutExpired(powerGroup, time)) {
                    if (DEBUG) {
                        android.util.Slog.i(TAG, "Going to sleep now due to long user inactivity");
                    }
                    changed = sleepPowerGroupLocked(powerGroup, time, 9, 1000);
                } else if (shouldNapAtBedTimeLocked()) {
                    changed = dreamPowerGroupLocked(powerGroup, time, 1000, false);
                } else {
                    changed = dozePowerGroupLocked(powerGroup, time, 2, 1000);
                }
            }
        }
        return changed;
    }

    private boolean shouldNapAtBedTimeLocked() {
        return !mPmsExt.getCastMode() && (this.mDreamsActivateOnSleepSetting || (this.mDreamsActivateOnDockSetting && this.mDockState != 0));
    }

    private boolean isItBedTimeYetLocked(com.android.server.power.PowerGroup powerGroup) {
        if (!this.mBootCompleted) {
            return false;
        }
        long now = this.mClock.uptimeMillis();
        if (isAttentiveTimeoutExpired(powerGroup, now)) {
            return !isBeingKeptFromInattentiveSleepLocked();
        }
        return !isBeingKeptAwakeLocked(powerGroup);
    }

    private boolean isBeingKeptAwakeLocked(com.android.server.power.PowerGroup powerGroup) {
        return this.mStayOn || mPmsExt.isBeingKeptAwakeLocked(powerGroup.getGroupId(), this.mProximityPositive) || (powerGroup.getWakeLockSummaryLocked() & 32) != 0 || (powerGroup.getUserActivitySummaryLocked() & 3) != 0 || this.mScreenBrightnessBoostInProgress;
    }

    private boolean isBeingKeptFromInattentiveSleepLocked() {
        return this.mStayOn || this.mScreenBrightnessBoostInProgress || this.mProximityPositive || !this.mBootCompleted;
    }

    private void updateDreamLocked(int dirty, boolean powerGroupBecameReady) {
        if (((dirty & 17407) != 0 || powerGroupBecameReady) && areAllPowerGroupsReadyLocked()) {
            scheduleSandmanLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleSandmanLocked() {
        if (!this.mSandmanScheduled) {
            this.mSandmanScheduled = true;
            for (int idx = 0; idx < this.mPowerGroups.size(); idx++) {
                com.android.server.power.PowerGroup powerGroup = this.mPowerGroups.valueAt(idx);
                if (powerGroup.supportsSandmanLocked()) {
                    android.os.Message msg = this.mHandler.obtainMessage(2);
                    msg.arg1 = powerGroup.getGroupId();
                    msg.setAsynchronous(true);
                    this.mHandler.sendMessageAtTime(msg, this.mClock.uptimeMillis());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSandman(int groupId) {
        boolean startDreaming;
        boolean isDreaming;
        synchronized (this.mLock) {
            this.mSandmanScheduled = false;
            if (this.mPowerGroups.contains(groupId)) {
                com.android.server.power.PowerGroup powerGroup = this.mPowerGroups.get(groupId);
                int wakefulness = powerGroup.getWakefulnessLocked();
                if (powerGroup.isSandmanSummonedLocked() && powerGroup.isReadyLocked()) {
                    boolean startDreaming2 = (canDreamLocked(powerGroup) || canDozeLocked(powerGroup)) && mPmsExt.isShouldGoAod();
                    powerGroup.setSandmanSummonedLocked(false);
                    startDreaming = startDreaming2;
                } else {
                    startDreaming = false;
                }
                if (this.mDreamManager != null) {
                    if (startDreaming) {
                        mPmsExt.stopDreamByMessage(this.mDreamManager);
                        this.mDreamManager.startDream(wakefulness == 3, "power manager request");
                        this.mDozeStartInProgress = false;
                    }
                    isDreaming = this.mDreamManager.isDreaming();
                } else {
                    isDreaming = false;
                }
                synchronized (this.mLock) {
                    if (this.mPowerGroups.contains(groupId)) {
                        if (startDreaming && isDreaming) {
                            this.mDreamsBatteryLevelDrain = 0;
                            if (wakefulness == 3) {
                                android.util.Slog.i(TAG, "Dozing...");
                            } else {
                                android.util.Slog.i(TAG, "Dreaming...");
                            }
                        }
                        com.android.server.power.PowerGroup powerGroup2 = this.mPowerGroups.get(groupId);
                        if (!powerGroup2.isSandmanSummonedLocked() && powerGroup2.getWakefulnessLocked() == wakefulness) {
                            long now = this.mClock.uptimeMillis();
                            if (wakefulness == 2) {
                                if (isDreaming && canDreamLocked(powerGroup2)) {
                                    if (this.mDreamsBatteryLevelDrainCutoffConfig < 0 || this.mDreamsBatteryLevelDrain <= this.mDreamsBatteryLevelDrainCutoffConfig || isBeingKeptAwakeLocked(powerGroup2)) {
                                        return;
                                    } else {
                                        android.util.Slog.i(TAG, "Stopping dream because the battery appears to be draining faster than it is charging.  Battery level drained while dreaming: " + this.mDreamsBatteryLevelDrain + "%.  Battery level now: " + this.mBatteryLevel + "%.");
                                    }
                                }
                                if (isItBedTimeYetLocked(powerGroup2)) {
                                    if (isAttentiveTimeoutExpired(powerGroup2, now)) {
                                        android.util.Slog.i(TAG, "handleSandman: TIMEOUT sleepPowerGroupLocked");
                                        sleepPowerGroupLocked(powerGroup2, now, 2, 1000);
                                    } else {
                                        android.util.Slog.i(TAG, "handleSandman: TIMEOUT dozePowerGroupLocked");
                                        dozePowerGroupLocked(powerGroup2, now, 2, 1000);
                                    }
                                } else {
                                    android.util.Slog.i(TAG, "handleSandman: DREAM_FINISHED wakePowerGroupLocked");
                                    wakePowerGroupLocked(powerGroup2, now, 13, "android.server.power:DREAM_FINISHED", 1000, this.mContext.getOpPackageName(), 1000);
                                }
                            } else if (wakefulness == 3) {
                                if (isDreaming) {
                                    return;
                                } else {
                                    sleepPowerGroupLocked(powerGroup2, now, 2, 1000);
                                }
                            }
                            if (isDreaming) {
                                mPmsExt.stopDream(this.mDreamManager);
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDreamSuppressionChangedLocked(boolean isSuppressed) {
        if (!this.mDreamsDisabledByAmbientModeSuppressionConfig) {
            return;
        }
        if (!isSuppressed && this.mIsPowered && this.mDreamsSupportedConfig && this.mDreamsEnabledSetting && shouldNapAtBedTimeLocked() && isItBedTimeYetLocked(this.mPowerGroups.get(0))) {
            napInternal(android.os.SystemClock.uptimeMillis(), 1000, true);
        } else if (isSuppressed) {
            this.mDirty |= 32;
            updatePowerStateLocked();
        }
    }

    private boolean canDreamLocked(com.android.server.power.PowerGroup powerGroup) {
        boolean dreamsSuppressed = this.mDreamsDisabledByAmbientModeSuppressionConfig && this.mAmbientDisplaySuppressionController.isSuppressed();
        if (!this.mBootCompleted || dreamsSuppressed || getGlobalWakefulnessLocked() != 2 || !this.mDreamsSupportedConfig || !this.mDreamsEnabledSetting || !powerGroup.isBrightOrDimLocked() || (powerGroup.getUserActivitySummaryLocked() & 7) == 0) {
            return false;
        }
        if (isBeingKeptAwakeLocked(powerGroup)) {
            return true;
        }
        if (!this.mIsPowered && !this.mDreamsEnabledOnBatteryConfig) {
            return false;
        }
        if (this.mIsPowered || this.mDreamsBatteryLevelMinimumWhenNotPoweredConfig < 0 || this.mBatteryLevel >= this.mDreamsBatteryLevelMinimumWhenNotPoweredConfig) {
            return !this.mIsPowered || this.mDreamsBatteryLevelMinimumWhenPoweredConfig < 0 || this.mBatteryLevel >= this.mDreamsBatteryLevelMinimumWhenPoweredConfig;
        }
        return false;
    }

    private boolean canDozeLocked(com.android.server.power.PowerGroup powerGroup) {
        return powerGroup.supportsSandmanLocked() && powerGroup.getWakefulnessLocked() == 3;
    }

    private boolean updatePowerGroupsLocked(int dirty) {
        boolean oldPowerGroupsReady;
        boolean z;
        boolean z2;
        float screenBrightnessOverride;
        android.os.PowerSaveState powerSaveStateBuild;
        boolean oldPowerGroupsReady2 = areAllPowerGroupsReadyLocked();
        if ((71743 & dirty) == 0) {
            oldPowerGroupsReady = oldPowerGroupsReady2;
            z = false;
            z2 = true;
        } else {
            if ((dirty & 4096) != 0) {
                if (!areAllPowerGroupsReadyLocked()) {
                    this.mDirty |= 4096;
                } else {
                    sQuiescent = false;
                }
            }
            mPmsExt.updateDisplayPowerStateLockedStart();
            int idx = 0;
            while (idx < this.mPowerGroups.size()) {
                com.android.server.power.PowerGroup powerGroup = this.mPowerGroups.valueAt(idx);
                int groupId = powerGroup.getGroupId();
                if (!this.mBootCompleted) {
                    screenBrightnessOverride = this.mScreenBrightnessDefault;
                } else {
                    float screenBrightnessOverride2 = this.mScreenBrightnessOverrideFromWindowManager;
                    if (isValidBrightness(screenBrightnessOverride2)) {
                        screenBrightnessOverride = this.mScreenBrightnessOverrideFromWindowManager;
                    } else {
                        screenBrightnessOverride = Float.NaN;
                    }
                }
                float screenBrightnessOverride3 = mPmsExt.updateAutoBrightness(screenBrightnessOverride);
                boolean zShouldUseProximitySensorLocked = shouldUseProximitySensorLocked();
                boolean zShouldBoostScreenBrightness = shouldBoostScreenBrightness();
                int i = this.mDozeScreenStateOverrideFromDreamManager;
                int i2 = this.mDozeScreenStateOverrideReasonFromDreamManager;
                float f = this.mDozeScreenBrightnessOverrideFromDreamManagerFloat;
                boolean z3 = this.mDrawWakeLockOverrideFromSidekick;
                if (this.mBatterySaverSupported) {
                    powerSaveStateBuild = this.mBatterySaverStateMachine.getBatterySaverPolicy().getBatterySaverPolicy(7);
                } else {
                    powerSaveStateBuild = new android.os.PowerSaveState.Builder().build();
                }
                boolean oldPowerGroupsReady3 = oldPowerGroupsReady2;
                int idx2 = idx;
                boolean ready = powerGroup.updateLocked(screenBrightnessOverride3, zShouldUseProximitySensorLocked, zShouldBoostScreenBrightness, i, i2, f, z3, powerSaveStateBuild, sQuiescent, this.mDozeAfterScreenOff, this.mBootCompleted, this.mScreenBrightnessBoostInProgress, this.mRequestWaitForNegativeProximity, this.mBrightWhenDozingConfig);
                int wakefulness = powerGroup.getWakefulnessLocked();
                mPmsExt.updateDisplayPowerStateLocked(groupId, wakefulness, this.mDozeScreenStateOverrideFromDreamManager, powerGroup.getPolicyLocked());
                if (DEBUG_SPEW) {
                    android.util.Slog.d(TAG, "updateDisplayPowerStateLocked: displayReady=" + ready + ", groupId=" + groupId + ", policy=" + android.hardware.display.DisplayManagerInternal.DisplayPowerRequest.policyToString(powerGroup.getPolicyLocked()) + ", mWakefulness=" + android.os.PowerManagerInternal.wakefulnessToString(wakefulness) + ", mWakeLockSummary=0x" + java.lang.Integer.toHexString(powerGroup.getWakeLockSummaryLocked()) + ", mUserActivitySummary=0x" + java.lang.Integer.toHexString(powerGroup.getUserActivitySummaryLocked()) + ", mBootCompleted=" + this.mBootCompleted + ", screenBrightnessOverride=" + screenBrightnessOverride3 + ", mScreenBrightnessBoostInProgress=" + this.mScreenBrightnessBoostInProgress + ", sQuiescent=" + sQuiescent);
                }
                boolean displayReadyStateChanged = powerGroup.setReadyLocked(ready);
                boolean poweringOn = powerGroup.isPoweringOnLocked();
                if (ready && displayReadyStateChanged && poweringOn) {
                    if (wakefulness == 1) {
                        powerGroup.setIsPoweringOnLocked(false);
                        com.android.internal.util.LatencyTracker.getInstance(this.mContext).onActionEnd(5);
                        android.os.Trace.asyncTraceEnd(131072L, TRACE_SCREEN_ON, groupId);
                        int latencyMs = (int) (this.mClock.uptimeMillis() - powerGroup.getLastPowerOnTimeLocked());
                        android.util.Slog.w(TAG, "Screen on took " + latencyMs + " ms. groupId = " + groupId);
                    }
                }
                idx = idx2 + 1;
                oldPowerGroupsReady2 = oldPowerGroupsReady3;
            }
            oldPowerGroupsReady = oldPowerGroupsReady2;
            z2 = true;
            z = false;
            this.mRequestWaitForNegativeProximity = false;
        }
        return (!areAllPowerGroupsReadyLocked() || oldPowerGroupsReady) ? z : z2;
    }

    private void updateScreenBrightnessBoostLocked(int dirty) {
        if ((dirty & 2048) != 0 && this.mScreenBrightnessBoostInProgress) {
            long now = this.mClock.uptimeMillis();
            this.mHandler.removeMessages(3);
            if (this.mLastScreenBrightnessBoostTime > this.mLastGlobalSleepTime) {
                long boostTimeout = this.mLastScreenBrightnessBoostTime + 5000;
                if (boostTimeout > now) {
                    android.os.Message msg = this.mHandler.obtainMessage(3);
                    msg.setAsynchronous(true);
                    this.mHandler.sendMessageAtTime(msg, boostTimeout);
                    return;
                }
            }
            this.mScreenBrightnessBoostInProgress = false;
            userActivityNoUpdateLocked(now, 0, 0, 1000);
        }
    }

    private boolean shouldBoostScreenBrightness() {
        return this.mScreenBrightnessBoostInProgress;
    }

    private static boolean isValidBrightness(float value) {
        return value >= 0.0f && mPmsExt.isValidBrightness((int) value);
    }

    int getDesiredScreenPolicyLocked(int groupId) {
        return this.mPowerGroups.get(groupId).getDesiredScreenPolicyLocked(sQuiescent, this.mDozeAfterScreenOff, this.mBootCompleted, this.mScreenBrightnessBoostInProgress, this.mBrightWhenDozingConfig);
    }

    int getDreamsBatteryLevelDrain() {
        return this.mDreamsBatteryLevelDrain;
    }

    private boolean shouldUseProximitySensorLocked() {
        return (this.mPowerGroups.get(0).getWakeLockSummaryLocked() & 16) != 0;
    }

    private void updateSuspendBlockerLocked() {
        boolean needWakeLockSuspendBlocker = (this.mWakeLockSummary & 1) != 0;
        boolean needDisplaySuspendBlocker = needSuspendBlockerLocked();
        boolean autoSuspend = !needDisplaySuspendBlocker;
        boolean interactive = false;
        for (int idx = 0; idx < this.mPowerGroups.size() && !interactive; idx++) {
            interactive = this.mPowerGroups.valueAt(idx).isBrightOrDimLocked();
        }
        if (!autoSuspend && this.mDecoupleHalAutoSuspendModeFromDisplayConfig) {
            setHalAutoSuspendModeLocked(false);
        }
        if (!this.mBootCompleted && !this.mHoldingBootingSuspendBlocker) {
            this.mBootingSuspendBlocker.acquire();
            this.mHoldingBootingSuspendBlocker = true;
        }
        if (needWakeLockSuspendBlocker && !this.mHoldingWakeLockSuspendBlocker) {
            this.mWakeLockSuspendBlocker.acquire();
            this.mHoldingWakeLockSuspendBlocker = true;
        }
        if (needDisplaySuspendBlocker && !this.mHoldingDisplaySuspendBlocker) {
            this.mDisplaySuspendBlocker.acquire(HOLDING_DISPLAY_SUSPEND_BLOCKER);
            this.mHoldingDisplaySuspendBlocker = true;
        }
        if (this.mDecoupleHalInteractiveModeFromDisplayConfig && (interactive || areAllPowerGroupsReadyLocked())) {
            setHalInteractiveModeLocked(interactive);
        }
        if (this.mBootCompleted && this.mHoldingBootingSuspendBlocker) {
            this.mBootingSuspendBlocker.release();
            this.mHoldingBootingSuspendBlocker = false;
        }
        if (!needWakeLockSuspendBlocker && this.mHoldingWakeLockSuspendBlocker) {
            this.mWakeLockSuspendBlocker.release();
            this.mHoldingWakeLockSuspendBlocker = false;
        }
        if (!needDisplaySuspendBlocker && this.mHoldingDisplaySuspendBlocker) {
            this.mDisplaySuspendBlocker.release(HOLDING_DISPLAY_SUSPEND_BLOCKER);
            this.mHoldingDisplaySuspendBlocker = false;
        }
        if (autoSuspend && this.mDecoupleHalAutoSuspendModeFromDisplayConfig) {
            setHalAutoSuspendModeLocked(true);
        }
    }

    private boolean needSuspendBlockerLocked() {
        if (!areAllPowerGroupsReadyLocked() || this.mScreenBrightnessBoostInProgress) {
            return true;
        }
        if (getGlobalWakefulnessLocked() == 3 && this.mDozeStartInProgress) {
            return true;
        }
        for (int idx = 0; idx < this.mPowerGroups.size(); idx++) {
            com.android.server.power.PowerGroup powerGroup = this.mPowerGroups.valueAt(idx);
            if (powerGroup.needSuspendBlockerLocked(this.mProximityPositive, this.mSuspendWhenScreenOffDueToProximityConfig)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHalAutoSuspendModeLocked(boolean enable) {
        if (this.mUseAutoSuspend && enable != this.mHalAutoSuspendModeEnabled) {
            if (DEBUG_PANIC || DEBUG) {
                android.util.Slog.d(TAG, "Setting HAL auto-suspend mode to " + enable);
            }
            this.mHalAutoSuspendModeEnabled = enable;
            android.os.Trace.traceBegin(131072L, "setHalAutoSuspend(" + enable + ")");
            try {
                this.mNativeWrapper.nativeSetAutoSuspend(enable);
            } finally {
                android.os.Trace.traceEnd(131072L);
                if (DEBUG_PANIC || DEBUG) {
                    android.util.Slog.d(TAG, "Setting HAL auto-suspend mode to " + enable + " done");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHalInteractiveModeLocked(boolean enable) {
        if (enable != this.mHalInteractiveModeEnabled) {
            if (DEBUG_PANIC || DEBUG) {
                android.util.Slog.d(TAG, "Setting HAL interactive mode to " + enable);
            }
            this.mHalInteractiveModeEnabled = enable;
            android.os.Trace.traceBegin(131072L, "setHalInteractive(" + enable + ")");
            try {
                this.mNativeWrapper.nativeSetPowerMode(7, enable);
            } finally {
                android.os.Trace.traceEnd(131072L);
                if (DEBUG_PANIC || DEBUG) {
                    android.util.Slog.d(TAG, "Setting HAL interactive mode to " + enable + " done");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGloballyInteractiveInternal() {
        boolean zIsInteractive;
        synchronized (this.mLock) {
            zIsInteractive = android.os.PowerManagerInternal.isInteractive(getGlobalWakefulnessLocked());
        }
        return zIsInteractive;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInteractiveInternal(int displayId, int uid) {
        synchronized (this.mLock) {
            android.view.DisplayInfo displayInfo = this.mDisplayManagerInternal.getDisplayInfo(displayId);
            if (displayInfo == null) {
                android.util.Slog.w(TAG, "Did not find DisplayInfo for displayId " + displayId);
                return false;
            }
            if (!displayInfo.hasAccess(uid)) {
                throw new java.lang.SecurityException("uid " + uid + " does not have access to display " + displayId);
            }
            com.android.server.power.PowerGroup powerGroup = this.mPowerGroups.get(displayInfo.displayGroupId);
            if (powerGroup == null) {
                android.util.Slog.w(TAG, "Did not find PowerGroup for displayId " + displayId);
                return false;
            }
            return android.os.PowerManagerInternal.isInteractive(powerGroup.getWakefulnessLocked());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setLowPowerModeInternal(boolean enabled) {
        if (enabled && mPmsExt.isCustomPowerSaveModeDisabled()) {
            return false;
        }
        synchronized (this.mLock) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "setLowPowerModeInternal " + enabled + " mIsPowered=" + this.mIsPowered);
            }
            if ((this.mIsPowered || !this.mBatterySaverSupported) && mPmsExt.interceptSetLowPowerModeInternalIsPowered()) {
                return false;
            }
            this.mBatterySaverStateMachine.setBatterySaverEnabledManually(enabled);
            mPmsExt.setLowPowerModeInternalEnd(enabled);
            return true;
        }
    }

    boolean isDeviceIdleModeInternal() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mDeviceIdleMode;
        }
        return z;
    }

    boolean isLightDeviceIdleModeInternal() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mLightDeviceIdleMode;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBatteryStateChangedLocked() {
        this.mDirty |= 256;
        updatePowerStateLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shutdownOrRebootInternal(final int haltMode, final boolean confirm, final java.lang.String reason, boolean wait) {
        if (mPmsExt.interceptShutdownOrRebootInternal(reason)) {
            return;
        }
        if ("userspace".equals(reason)) {
            if (!android.os.PowerManager.isRebootingUserspaceSupportedImpl()) {
                throw new java.lang.UnsupportedOperationException("Attempted userspace reboot on a device that doesn't support it");
            }
            com.android.server.UserspaceRebootLogger.noteUserspaceRebootWasRequested();
        }
        if (this.mHandler == null || !this.mSystemReady) {
            if (com.android.server.RescueParty.isRecoveryTriggeredReboot()) {
                lowLevelReboot(reason);
            } else {
                throw new java.lang.IllegalStateException("Too early to call shutdown() or reboot()");
            }
        }
        java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.power.PowerManagerService.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (this) {
                    if (haltMode == 2) {
                        com.android.server.power.ShutdownThread.rebootSafeMode(com.android.server.power.PowerManagerService.this.getUiContext(), confirm);
                    } else if (haltMode == 1) {
                        com.android.server.power.ShutdownThread.reboot(com.android.server.power.PowerManagerService.this.getUiContext(), reason, confirm);
                    } else {
                        com.android.server.power.ShutdownThread.shutdown(com.android.server.power.PowerManagerService.this.getUiContext(), reason, confirm);
                    }
                }
            }
        };
        android.os.Message msg = android.os.Message.obtain(com.android.server.UiThread.getHandler(), runnable);
        msg.setAsynchronous(true);
        com.android.server.UiThread.getHandler().sendMessage(msg);
        if (wait) {
            synchronized (runnable) {
                while (true) {
                    try {
                        runnable.wait();
                    } catch (java.lang.InterruptedException e) {
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void crashInternal(final java.lang.String message) {
        java.lang.Thread t = new java.lang.Thread("PowerManagerService.crash()") { // from class: com.android.server.power.PowerManagerService.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                throw new java.lang.RuntimeException(message);
            }
        };
        try {
            t.start();
            t.join();
        } catch (java.lang.InterruptedException e) {
            android.util.Slog.wtf(TAG, e);
        }
    }

    void setStayOnSettingInternal(int val) {
        android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "stay_on_while_plugged_in", val);
    }

    void setMaximumScreenOffTimeoutFromDeviceAdminInternal(int userId, long timeMs) {
        if (userId < 0) {
            android.util.Slog.wtf(TAG, "Attempt to set screen off timeout for invalid user: " + userId);
            return;
        }
        synchronized (this.mLock) {
            try {
                if (userId == 0) {
                    this.mMaximumScreenOffTimeoutFromDeviceAdmin = timeMs;
                } else if (timeMs == Long.MAX_VALUE || timeMs == 0) {
                    this.mProfilePowerState.delete(userId);
                } else {
                    com.android.server.power.PowerManagerService.ProfilePowerState profile = this.mProfilePowerState.get(userId);
                    if (profile != null) {
                        profile.mScreenOffTimeout = timeMs;
                    } else {
                        this.mProfilePowerState.put(userId, new com.android.server.power.PowerManagerService.ProfilePowerState(userId, timeMs, this.mClock.uptimeMillis()));
                        this.mDirty |= 1;
                    }
                }
                this.mDirty |= 32;
                updatePowerStateLocked();
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    boolean setDeviceIdleModeInternal(boolean enabled) {
        synchronized (this.mLock) {
            if (this.mDeviceIdleMode == enabled) {
                return false;
            }
            this.mDeviceIdleMode = enabled;
            updateWakeLockDisabledStatesLocked();
            setPowerModeInternal(8, this.mDeviceIdleMode || this.mLightDeviceIdleMode);
            if (enabled) {
                com.android.server.EventLogTags.writeDeviceIdleOnPhase("power");
            } else {
                com.android.server.EventLogTags.writeDeviceIdleOffPhase("power");
            }
            if (enabled) {
                mPmsExt.onDeviceIdle();
            }
            return true;
        }
    }

    boolean setLightDeviceIdleModeInternal(boolean enabled) {
        synchronized (this.mLock) {
            if (this.mLightDeviceIdleMode == enabled) {
                return false;
            }
            this.mLightDeviceIdleMode = enabled;
            if (!this.mDeviceIdleMode && com.android.server.deviceidle.Flags.disableWakelocksInLightIdle()) {
                updateWakeLockDisabledStatesLocked();
            }
            setPowerModeInternal(8, this.mDeviceIdleMode || this.mLightDeviceIdleMode);
            return true;
        }
    }

    void setDeviceIdleWhitelistInternal(int[] appids) {
        synchronized (this.mLock) {
            this.mDeviceIdleWhitelist = appids;
            if (doesIdleStateBlockWakeLocksLocked()) {
                updateWakeLockDisabledStatesLocked();
            }
        }
    }

    void setDeviceIdleTempWhitelistInternal(int[] appids) {
        synchronized (this.mLock) {
            this.mDeviceIdleTempWhitelist = appids;
            if (doesIdleStateBlockWakeLocksLocked()) {
                updateWakeLockDisabledStatesLocked();
            }
        }
    }

    void setLowPowerStandbyAllowlistInternal(int[] uids) {
        synchronized (this.mLock) {
            this.mLowPowerStandbyAllowlist = uids;
            if (this.mLowPowerStandbyActive) {
                updateWakeLockDisabledStatesLocked();
            }
        }
    }

    void setLowPowerStandbyActiveInternal(boolean active) {
        synchronized (this.mLock) {
            if (this.mLowPowerStandbyActive != active) {
                this.mLowPowerStandbyActive = active;
                updateWakeLockDisabledStatesLocked();
            }
        }
    }

    void startUidChangesInternal() {
        synchronized (this.mLock) {
            this.mUidsChanging = true;
        }
    }

    void finishUidChangesInternal() {
        synchronized (this.mLock) {
            this.mUidsChanging = false;
            if (this.mUidsChanged) {
                updateWakeLockDisabledStatesLocked();
                this.mUidsChanged = false;
            }
        }
    }

    private void handleUidStateChangeLocked() {
        if (this.mUidsChanging) {
            this.mUidsChanged = true;
        } else {
            updateWakeLockDisabledStatesLocked();
        }
    }

    void updateUidProcStateInternal(int uid, int procState) {
        synchronized (this.mLock) {
            com.android.server.power.PowerManagerService.UidState state = this.mUidState.get(uid);
            if (state == null) {
                state = new com.android.server.power.PowerManagerService.UidState(uid);
                this.mUidState.put(uid, state);
            }
            boolean z = true;
            boolean oldShouldAllow = state.mProcState <= 11;
            state.mProcState = procState;
            if (state.mNumWakeLocks > 0) {
                if (doesIdleStateBlockWakeLocksLocked() || this.mLowPowerStandbyActive) {
                    handleUidStateChangeLocked();
                } else if (!state.mActive) {
                    if (procState > 11) {
                        z = false;
                    }
                    if (oldShouldAllow != z) {
                        handleUidStateChangeLocked();
                    }
                }
            }
        }
    }

    void uidGoneInternal(int uid) {
        synchronized (this.mLock) {
            int index = this.mUidState.indexOfKey(uid);
            if (index >= 0) {
                com.android.server.power.PowerManagerService.UidState state = this.mUidState.valueAt(index);
                state.mProcState = 20;
                state.mActive = false;
                this.mUidState.removeAt(index);
                if ((doesIdleStateBlockWakeLocksLocked() || this.mLowPowerStandbyActive) && state.mNumWakeLocks > 0) {
                    handleUidStateChangeLocked();
                }
            }
        }
    }

    void uidActiveInternal(int uid) {
        synchronized (this.mLock) {
            com.android.server.power.PowerManagerService.UidState state = this.mUidState.get(uid);
            if (state == null) {
                state = new com.android.server.power.PowerManagerService.UidState(uid);
                state.mProcState = 19;
                this.mUidState.put(uid, state);
            }
            state.mActive = true;
            if (state.mNumWakeLocks > 0) {
                handleUidStateChangeLocked();
            }
        }
    }

    void uidIdleInternal(int uid) {
        synchronized (this.mLock) {
            com.android.server.power.PowerManagerService.UidState state = this.mUidState.get(uid);
            if (state != null) {
                state.mActive = false;
                if (state.mNumWakeLocks > 0) {
                    handleUidStateChangeLocked();
                }
            }
        }
    }

    private boolean doesIdleStateBlockWakeLocksLocked() {
        return this.mDeviceIdleMode || (this.mLightDeviceIdleMode && com.android.server.deviceidle.Flags.disableWakelocksInLightIdle());
    }

    private void updateWakeLockDisabledStatesLocked() {
        boolean changed = false;
        int numWakeLocks = this.mWakeLocks.size();
        for (int i = 0; i < numWakeLocks; i++) {
            com.android.server.power.PowerManagerService.WakeLock wakeLock = this.mWakeLocks.get(i);
            if (((wakeLock.mFlags & 65535) == 1 || isScreenLock(wakeLock)) && setWakeLockDisabledStateLocked(wakeLock)) {
                changed = true;
                if (wakeLock.mDisabled) {
                    notifyWakeLockReleasedLocked(wakeLock);
                } else {
                    notifyWakeLockAcquiredLocked(wakeLock);
                }
            }
        }
        if (changed) {
            this.mDirty |= 1;
            updatePowerStateLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setWakeLockDisabledStateLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
        boolean z = false;
        if ((wakeLock.mFlags & 65535) == 1) {
            boolean disabled = false;
            int appid = android.os.UserHandle.getAppId(wakeLock.mOwnerUid);
            if (appid >= 10000) {
                if (this.mConstants.NO_CACHED_WAKE_LOCKS) {
                    if (this.mForceSuspendActive || (!wakeLock.mUidState.mActive && wakeLock.mUidState.mProcState != 20 && wakeLock.mUidState.mProcState > 11)) {
                        z = true;
                    }
                    disabled = z;
                }
                if (doesIdleStateBlockWakeLocksLocked()) {
                    com.android.server.power.PowerManagerService.UidState state = wakeLock.mUidState;
                    if (java.util.Arrays.binarySearch(this.mDeviceIdleWhitelist, appid) < 0 && java.util.Arrays.binarySearch(this.mDeviceIdleTempWhitelist, appid) < 0 && state.mProcState != 20 && state.mProcState > 5) {
                        disabled = true;
                    }
                }
                if (this.mLowPowerStandbyActive) {
                    com.android.server.power.PowerManagerService.UidState state2 = wakeLock.mUidState;
                    if (java.util.Arrays.binarySearch(this.mLowPowerStandbyAllowlist, wakeLock.mOwnerUid) < 0 && state2.mProcState != 20 && state2.mProcState > 3) {
                        disabled = true;
                    }
                }
            }
            if (wakeLock.mWakeLockExt.getDisabledByHans()) {
                disabled = true;
            }
            return wakeLock.setDisabled(disabled);
        }
        boolean disabled2 = this.mDisableScreenWakeLocksWhileCached;
        if (disabled2 && isScreenLock(wakeLock)) {
            boolean disabled3 = false;
            int appid2 = android.os.UserHandle.getAppId(wakeLock.mOwnerUid);
            com.android.server.power.PowerManagerService.UidState state3 = wakeLock.mUidState;
            if (this.mConstants.NO_CACHED_WAKE_LOCKS && appid2 >= 10000 && !state3.mActive && state3.mProcState != 20 && state3.mProcState >= 12) {
                if (DEBUG_SPEW) {
                    android.util.Slog.d(TAG, "disabling full wakelock " + wakeLock);
                }
                disabled3 = true;
            }
            return wakeLock.setDisabled(disabled3);
        }
        boolean disabled4 = this.mDisableScreenWakeLocksWhileCached;
        if (!disabled4 || !isScreenLock(wakeLock)) {
            return false;
        }
        boolean disabled5 = false;
        int appid3 = android.os.UserHandle.getAppId(wakeLock.mOwnerUid);
        com.android.server.power.PowerManagerService.UidState state4 = wakeLock.mUidState;
        if (this.mConstants.NO_CACHED_WAKE_LOCKS && appid3 >= 10000 && !state4.mActive && state4.mProcState != 20 && state4.mProcState >= 12) {
            if (DEBUG_SPEW) {
                android.util.Slog.d(TAG, "disabling full wakelock " + wakeLock);
            }
            disabled5 = true;
        }
        return wakeLock.setDisabled(disabled5);
    }

    private boolean isMaximumScreenOffTimeoutFromDeviceAdminEnforcedLocked() {
        return this.mMaximumScreenOffTimeoutFromDeviceAdmin >= 0 && this.mMaximumScreenOffTimeoutFromDeviceAdmin < Long.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAttentionLightInternal(boolean on, int color) {
        synchronized (this.mLock) {
            if (this.mSystemReady) {
                com.android.server.lights.LogicalLight light = this.mAttentionLight;
                if (light != null) {
                    light.setFlashing(color, 2, on ? 3 : 0, 0);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDozeAfterScreenOffInternal(boolean on) {
        synchronized (this.mLock) {
            this.mDozeAfterScreenOff = on;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void boostScreenBrightnessInternal(long eventTime, int uid) {
        synchronized (this.mLock) {
            if (this.mSystemReady && getGlobalWakefulnessLocked() != 0 && eventTime >= this.mLastScreenBrightnessBoostTime) {
                android.util.Slog.i(TAG, "Brightness boost activated (uid " + uid + ")...");
                this.mLastScreenBrightnessBoostTime = eventTime;
                this.mScreenBrightnessBoostInProgress = true;
                this.mDirty |= 2048;
                userActivityNoUpdateLocked(this.mPowerGroups.get(0), eventTime, 0, 0, uid);
                updatePowerStateLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isScreenBrightnessBoostedInternal() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mScreenBrightnessBoostInProgress;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScreenBrightnessBoostTimeout() {
        synchronized (this.mLock) {
            if (DEBUG_SPEW) {
                android.util.Slog.d(TAG, "handleScreenBrightnessBoostTimeout");
            }
            this.mDirty |= 2048;
            updatePowerStateLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScreenBrightnessOverrideFromWindowManagerInternal(float brightness) {
        synchronized (this.mLock) {
            if (!com.android.internal.display.BrightnessSynchronizer.floatEquals(this.mScreenBrightnessOverrideFromWindowManager, brightness) || mPmsExt.shouldCommitScreenBrightnessOverrideMap()) {
                mPmsExt.setScreenBrightnessOverrideFromWindowManager(brightness);
                this.mDirty |= 32;
                updatePowerStateLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUserInactiveOverrideFromWindowManagerInternal() {
        synchronized (this.mLock) {
            this.mUserInactiveOverrideFromWindowManager = true;
            this.mDirty |= 4;
            updatePowerStateLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUserActivityTimeoutOverrideFromWindowManagerInternal(long timeoutMillis) {
        synchronized (this.mLock) {
            if (this.mPowerGroups != null && this.mPowerGroups.get(0) != null && mPmsExt.notAllowedSetUserActivityTimeoutOverrideFromWindowManager(this.mUserActivityTimeoutOverrideFromWindowManager, android.os.PowerManagerInternal.isInteractive(getGlobalWakefulnessLocked()), this.mPowerGroups.get(0).getUserActivitySummaryLocked())) {
                android.util.Slog.d(TAG, "setUserActivityTimeoutOverrideFromWindowManagerInternal: timeoutMillis = " + timeoutMillis + ", mUserActivitySummary=" + this.mPowerGroups.get(0).getUserActivitySummaryLocked() + ", mWakefulness=" + getGlobalWakefulnessLocked());
                return;
            }
            if (this.mUserActivityTimeoutOverrideFromWindowManager != timeoutMillis) {
                if (DEBUG_PANIC || DEBUG) {
                    android.util.Slog.d(TAG, "UA TimeoutOverrideFromWindowManagerInternal = " + timeoutMillis);
                }
                this.mUserActivityTimeoutOverrideFromWindowManager = timeoutMillis;
                com.android.server.EventLogTags.writeUserActivityTimeoutOverride(timeoutMillis);
                this.mDirty |= 32;
                updatePowerStateLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDozeOverrideFromDreamManagerInternal(int screenState, int reason, int screenBrightness) {
        int screenState2 = mPmsExt.setDozeOverrideFromDreamManagerInternal(screenState, screenBrightness);
        synchronized (this.mLock) {
            if (this.mDozeScreenStateOverrideFromDreamManager != screenState2 || this.mDozeScreenBrightnessOverrideFromDreamManager != screenBrightness) {
                this.mDozeScreenStateOverrideFromDreamManager = screenState2;
                this.mDozeScreenStateOverrideReasonFromDreamManager = reason;
                this.mDozeScreenBrightnessOverrideFromDreamManager = screenBrightness;
                this.mDozeScreenBrightnessOverrideFromDreamManagerFloat = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(this.mDozeScreenBrightnessOverrideFromDreamManager);
                this.mDirty |= 32;
                updatePowerStateLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDrawWakeLockOverrideFromSidekickInternal(boolean keepState) {
        synchronized (this.mLock) {
            if (this.mDrawWakeLockOverrideFromSidekick != keepState) {
                this.mDrawWakeLockOverrideFromSidekick = keepState;
                this.mDirty |= 32;
                updatePowerStateLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPowerBoostInternal(int boost, int durationMs) {
        this.mNativeWrapper.nativeSetPowerBoost(boost, durationMs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setPowerModeInternal(int mode, boolean enabled) {
        if (mode == 5 && enabled && this.mBatterySaverStateMachine != null && this.mBatterySaverStateMachine.getBatterySaverController().isLaunchBoostDisabled()) {
            return false;
        }
        return this.mNativeWrapper.nativeSetPowerMode(mode, enabled);
    }

    boolean wasDeviceIdleForInternal(long ms) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPowerGroups.get(0).getLastUserActivityTimeLocked() + ms < this.mClock.uptimeMillis();
        }
        return z;
    }

    void onUserActivity() {
        synchronized (this.mLock) {
            this.mPowerGroups.get(0).setLastUserActivityTimeLocked(this.mClock.uptimeMillis(), 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean forceSuspendInternal(int uid) {
        boolean success;
        synchronized (this.mLock) {
            try {
                this.mForceSuspendActive = true;
                for (int idx = 0; idx < this.mPowerGroups.size(); idx++) {
                    sleepPowerGroupLocked(this.mPowerGroups.valueAt(idx), this.mClock.uptimeMillis(), 8, uid);
                }
                updateWakeLockDisabledStatesLocked();
                android.util.Slog.i(TAG, "Force-Suspending (uid " + uid + ")...");
                success = this.mNativeWrapper.nativeForceSuspend();
                if (!success) {
                    android.util.Slog.i(TAG, "Force-Suspending failed in native.");
                }
            } finally {
                this.mForceSuspendActive = false;
                updateWakeLockDisabledStatesLocked();
            }
        }
        return success;
    }

    private void addPowerGroupsForNonDefaultDisplayGroupLocked() {
        android.util.IntArray displayGroupIds = this.mDisplayManagerInternal.getDisplayGroupIds();
        if (displayGroupIds == null) {
            return;
        }
        for (int i = 0; i < displayGroupIds.size(); i++) {
            int displayGroupId = displayGroupIds.get(i);
            if (displayGroupId != 0) {
                if (this.mPowerGroups.contains(displayGroupId)) {
                    android.util.Slog.e(TAG, "Tried to add already existing group:" + displayGroupId);
                } else {
                    com.android.server.power.PowerGroup powerGroup = new com.android.server.power.PowerGroup(displayGroupId, this.mPowerGroupWakefulnessChangeListener, this.mNotifier, this.mDisplayManagerInternal, 1, false, false, this.mClock.uptimeMillis());
                    this.mPowerGroups.append(displayGroupId, powerGroup);
                }
            }
        }
        int i2 = this.mDirty;
        this.mDirty = i2 | 65536;
    }

    public static void lowLevelShutdown(java.lang.String reason) {
        if (reason == null) {
            reason = "";
        }
        java.lang.Boolean enable = java.lang.Boolean.valueOf("true".equals(android.os.SystemProperties.get("persist.sys.oplus.recorder.enable", "false")));
        if (enable.booleanValue()) {
            android.os.SystemProperties.set("sys.oplus.powerctl.recorder", "shutdown," + reason);
            android.util.Slog.d(TAG, "Recorder enable in lowLevelShutdown , reason is " + reason);
        }
        android.os.SystemProperties.set("sys.powerctl", "shutdown," + reason);
        android.util.Slog.d(TAG, "lowLevelShutdown, reason=" + reason);
    }

    public static void lowLevelReboot(java.lang.String reason) {
        if (reason == null) {
            reason = "";
        }
        if (reason.equals("quiescent")) {
            sQuiescent = true;
            reason = "";
        } else if (reason.endsWith(",quiescent")) {
            sQuiescent = true;
            reason = reason.substring(0, (reason.length() - "quiescent".length()) - 1);
        }
        if (reason.equals("recovery") || reason.equals("recovery-update")) {
            reason = "recovery";
        }
        java.lang.Boolean enable = java.lang.Boolean.valueOf("true".equals(android.os.SystemProperties.get("persist.sys.oplus.recorder.enable", "false")));
        if (enable.booleanValue()) {
            android.os.SystemProperties.set("sys.oplus.powerctl.recorder", "reboot," + reason);
            android.util.Slog.d(TAG, "Recorder enable in lowLevelReboot , reason before filter is " + reason);
        }
        if (sQuiescent) {
            if (!"".equals(reason)) {
                reason = reason + ",";
            }
            reason = reason + "quiescent";
        }
        android.os.SystemProperties.set("sys.powerctl", "reboot," + reason);
        android.util.Slog.d(TAG, "lowLevelReboot,shutdown, reboot reason is " + reason);
        try {
            java.lang.Thread.sleep(20000L);
        } catch (java.lang.InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
        }
        android.util.Slog.wtf(TAG, "Unexpected return from lowLevelReboot!");
    }

    @Override // com.android.server.Watchdog.Monitor
    public void monitor() {
        synchronized (this.mLock) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @dalvik.annotation.optimization.NeverCompile
    public void dumpInternal(java.io.PrintWriter pw) {
        com.android.server.power.WirelessChargerDetector wcd;
        pw.println("POWER MANAGER (dumpsys power)\n");
        synchronized (this.mLock) {
            pw.println("Power Manager State:");
            this.mConstants.dump(pw);
            pw.println("  mDirty=0x" + java.lang.Integer.toHexString(this.mDirty));
            pw.println("  mWakefulness=" + android.os.PowerManagerInternal.wakefulnessToString(getGlobalWakefulnessLocked()));
            pw.println("  mWakefulnessChanging=" + this.mWakefulnessChanging);
            pw.println("  mIsPowered=" + this.mIsPowered);
            pw.println("  mPlugType=" + this.mPlugType);
            pw.println("  mBatteryLevel=" + this.mBatteryLevel);
            pw.println("  mDreamsBatteryLevelDrain=" + this.mDreamsBatteryLevelDrain);
            pw.println("  mDockState=" + this.mDockState);
            pw.println("  mStayOn=" + this.mStayOn);
            pw.println("  mProximityPositive=" + this.mProximityPositive);
            pw.println("  mBootCompleted=" + this.mBootCompleted);
            pw.println("  mSystemReady=" + this.mSystemReady);
            synchronized (this.mEnhancedDischargeTimeLock) {
                pw.println("  mEnhancedDischargeTimeElapsed=" + this.mEnhancedDischargeTimeElapsed);
                pw.println("  mLastEnhancedDischargeTimeUpdatedElapsed=" + this.mLastEnhancedDischargeTimeUpdatedElapsed);
                pw.println("  mEnhancedDischargePredictionIsPersonalized=" + this.mEnhancedDischargePredictionIsPersonalized);
            }
            pw.println("  mUseAutoSuspend=" + this.mUseAutoSuspend);
            pw.println("  mHalAutoSuspendModeEnabled=" + this.mHalAutoSuspendModeEnabled);
            pw.println("  mHalInteractiveModeEnabled=" + this.mHalInteractiveModeEnabled);
            pw.println("  mWakeLockSummary=0x" + java.lang.Integer.toHexString(this.mWakeLockSummary));
            pw.print("  mNotifyLongScheduled=");
            if (this.mNotifyLongScheduled != 0) {
                android.util.TimeUtils.formatDuration(this.mNotifyLongScheduled, this.mClock.uptimeMillis(), pw);
            } else {
                pw.print("(none)");
            }
            pw.println();
            pw.print("  mNotifyLongDispatched=");
            if (this.mNotifyLongDispatched != 0) {
                android.util.TimeUtils.formatDuration(this.mNotifyLongDispatched, this.mClock.uptimeMillis(), pw);
            } else {
                pw.print("(none)");
            }
            pw.println();
            pw.print("  mNotifyLongNextCheck=");
            if (this.mNotifyLongNextCheck != 0) {
                android.util.TimeUtils.formatDuration(this.mNotifyLongNextCheck, this.mClock.uptimeMillis(), pw);
            } else {
                pw.print("(none)");
            }
            pw.println();
            pw.println("  mRequestWaitForNegativeProximity=" + this.mRequestWaitForNegativeProximity);
            pw.println("  mInterceptedPowerKeyForProximity=" + this.mInterceptedPowerKeyForProximity);
            pw.println("  mSandmanScheduled=" + this.mSandmanScheduled);
            pw.println("  mBatteryLevelLow=" + this.mBatteryLevelLow);
            pw.println("  mLightDeviceIdleMode=" + this.mLightDeviceIdleMode);
            pw.println("  mDeviceIdleMode=" + this.mDeviceIdleMode);
            pw.println("  mDeviceIdleWhitelist=" + java.util.Arrays.toString(this.mDeviceIdleWhitelist));
            pw.println("  mDeviceIdleTempWhitelist=" + java.util.Arrays.toString(this.mDeviceIdleTempWhitelist));
            pw.println("  mLowPowerStandbyActive=" + this.mLowPowerStandbyActive);
            pw.println("  mLastWakeTime=" + android.util.TimeUtils.formatUptime(this.mLastGlobalWakeTime));
            pw.println("  mLastSleepTime=" + android.util.TimeUtils.formatUptime(this.mLastGlobalSleepTime));
            pw.println("  mLastSleepReason=" + android.os.PowerManager.sleepReasonToString(this.mLastGlobalSleepReason));
            pw.println("  mLastGlobalWakeTimeRealtime=" + android.util.TimeUtils.formatUptime(this.mLastGlobalWakeTimeRealtime));
            pw.println("  mLastGlobalSleepTimeRealtime=" + android.util.TimeUtils.formatUptime(this.mLastGlobalSleepTimeRealtime));
            pw.println("  mLastInteractivePowerHintTime=" + android.util.TimeUtils.formatUptime(this.mLastInteractivePowerHintTime));
            pw.println("  mLastScreenBrightnessBoostTime=" + android.util.TimeUtils.formatUptime(this.mLastScreenBrightnessBoostTime));
            pw.println("  mScreenBrightnessBoostInProgress=" + this.mScreenBrightnessBoostInProgress);
            pw.println("  mHoldingWakeLockSuspendBlocker=" + this.mHoldingWakeLockSuspendBlocker);
            pw.println("  mHoldingDisplaySuspendBlocker=" + this.mHoldingDisplaySuspendBlocker);
            pw.println("  mLastFlipTime=" + this.mLastFlipTime);
            pw.println("  mIsFaceDown=" + this.mIsFaceDown);
            pw.println();
            pw.println("Settings and Configuration:");
            pw.println("  mDecoupleHalAutoSuspendModeFromDisplayConfig=" + this.mDecoupleHalAutoSuspendModeFromDisplayConfig);
            pw.println("  mDecoupleHalInteractiveModeFromDisplayConfig=" + this.mDecoupleHalInteractiveModeFromDisplayConfig);
            pw.println("  mWakeUpWhenPluggedOrUnpluggedConfig=" + this.mWakeUpWhenPluggedOrUnpluggedConfig);
            pw.println("  mWakeUpWhenPluggedOrUnpluggedInTheaterModeConfig=" + this.mWakeUpWhenPluggedOrUnpluggedInTheaterModeConfig);
            pw.println("  mTheaterModeEnabled=" + this.mTheaterModeEnabled);
            pw.println("  mKeepDreamingWhenUnplugging=" + this.mKeepDreamingWhenUnplugging);
            pw.println("  mSuspendWhenScreenOffDueToProximityConfig=" + this.mSuspendWhenScreenOffDueToProximityConfig);
            pw.println("  mDreamsSupportedConfig=" + this.mDreamsSupportedConfig);
            pw.println("  mDreamsEnabledByDefaultConfig=" + this.mDreamsEnabledByDefaultConfig);
            pw.println("  mDreamsActivatedOnSleepByDefaultConfig=" + this.mDreamsActivatedOnSleepByDefaultConfig);
            pw.println("  mDreamsActivatedOnDockByDefaultConfig=" + this.mDreamsActivatedOnDockByDefaultConfig);
            pw.println("  mDreamsEnabledOnBatteryConfig=" + this.mDreamsEnabledOnBatteryConfig);
            pw.println("  mDreamsBatteryLevelMinimumWhenPoweredConfig=" + this.mDreamsBatteryLevelMinimumWhenPoweredConfig);
            pw.println("  mDreamsBatteryLevelMinimumWhenNotPoweredConfig=" + this.mDreamsBatteryLevelMinimumWhenNotPoweredConfig);
            pw.println("  mDreamsBatteryLevelDrainCutoffConfig=" + this.mDreamsBatteryLevelDrainCutoffConfig);
            pw.println("  mDreamsEnabledSetting=" + this.mDreamsEnabledSetting);
            pw.println("  mDreamsActivateOnSleepSetting=" + this.mDreamsActivateOnSleepSetting);
            pw.println("  mDreamsActivateOnDockSetting=" + this.mDreamsActivateOnDockSetting);
            pw.println("  mDozeAfterScreenOff=" + this.mDozeAfterScreenOff);
            pw.println("  mBrightWhenDozingConfig=" + this.mBrightWhenDozingConfig);
            pw.println("  mMinimumScreenOffTimeoutConfig=" + this.mMinimumScreenOffTimeoutConfig);
            pw.println("  mMaximumScreenDimDurationConfig=" + this.mMaximumScreenDimDurationConfig);
            pw.println("  mMaximumScreenDimRatioConfig=" + this.mMaximumScreenDimRatioConfig);
            pw.println("  mAttentiveTimeoutConfig=" + this.mAttentiveTimeoutConfig);
            pw.println("  mAttentiveTimeoutSetting=" + this.mAttentiveTimeoutSetting);
            pw.println("  mAttentiveWarningDurationConfig=" + this.mAttentiveWarningDurationConfig);
            pw.println("  mScreenOffTimeoutSetting=" + this.mScreenOffTimeoutSetting);
            pw.println("  mSleepTimeoutSetting=" + this.mSleepTimeoutSetting);
            pw.println("  mMaximumScreenOffTimeoutFromDeviceAdmin=" + this.mMaximumScreenOffTimeoutFromDeviceAdmin + " (enforced=" + isMaximumScreenOffTimeoutFromDeviceAdminEnforcedLocked() + ")");
            pw.println("  mStayOnWhilePluggedInSetting=" + this.mStayOnWhilePluggedInSetting);
            pw.println("  mScreenBrightnessOverrideFromWindowManager=" + this.mScreenBrightnessOverrideFromWindowManager);
            pw.println("  mUserActivityTimeoutOverrideFromWindowManager=" + this.mUserActivityTimeoutOverrideFromWindowManager);
            pw.println("  mUserInactiveOverrideFromWindowManager=" + this.mUserInactiveOverrideFromWindowManager);
            pw.println("  mDozeScreenStateOverrideFromDreamManager=" + this.mDozeScreenStateOverrideFromDreamManager);
            pw.println("  mDrawWakeLockOverrideFromSidekick=" + this.mDrawWakeLockOverrideFromSidekick);
            pw.println("  mDozeScreenBrightnessOverrideFromDreamManager=" + this.mDozeScreenBrightnessOverrideFromDreamManager);
            pw.println("  mScreenBrightnessMinimum=" + this.mScreenBrightnessMinimum);
            pw.println("  mScreenBrightnessMaximum=" + this.mScreenBrightnessMaximum);
            pw.println("  mScreenBrightnessDefault=" + this.mScreenBrightnessDefault);
            pw.println("  mDoubleTapWakeEnabled=" + this.mDoubleTapWakeEnabled);
            pw.println("  mForegroundProfile=" + this.mForegroundProfile);
            pw.println("  mUserId=" + this.mUserId);
            pw.println("  mAlwaysOnEnabled=" + this.mAlwaysOnEnabled);
            mPmsExt.dumpSmartLauncher(pw);
            long attentiveTimeout = getAttentiveTimeoutLocked();
            long sleepTimeout = getSleepTimeoutLocked(attentiveTimeout);
            long screenOffTimeout = getScreenOffTimeoutLocked(sleepTimeout, attentiveTimeout);
            long screenDimDuration = getScreenDimDurationLocked(screenOffTimeout);
            pw.println();
            pw.println("Attentive timeout: " + attentiveTimeout + " ms");
            pw.println("Sleep timeout: " + sleepTimeout + " ms");
            pw.println("Screen off timeout: " + screenOffTimeout + " ms");
            pw.println("Screen dim duration: " + screenDimDuration + " ms");
            pw.println();
            pw.print("UID states (changing=");
            pw.print(this.mUidsChanging);
            pw.print(" changed=");
            pw.print(this.mUidsChanged);
            pw.println("):");
            for (int i = 0; i < this.mUidState.size(); i++) {
                com.android.server.power.PowerManagerService.UidState state = this.mUidState.valueAt(i);
                pw.print("  UID ");
                android.os.UserHandle.formatUid(pw, this.mUidState.keyAt(i));
                pw.print(": ");
                if (state.mActive) {
                    pw.print("  ACTIVE ");
                } else {
                    pw.print("INACTIVE ");
                }
                pw.print(" count=");
                pw.print(state.mNumWakeLocks);
                pw.print(" state=");
                pw.println(state.mProcState);
            }
            pw.println();
            pw.println("Looper state:");
            this.mHandler.getLooper().dump(new android.util.PrintWriterPrinter(pw), "  ");
            pw.println();
            pw.println("Wake Locks: size=" + this.mWakeLocks.size());
            for (com.android.server.power.PowerManagerService.WakeLock wl : this.mWakeLocks) {
                pw.println("  " + wl);
            }
            mPmsExt.dumpBaseProxyWakeLock(pw);
            pw.println();
            pw.println("Suspend Blockers: size=" + this.mSuspendBlockers.size());
            for (com.android.server.power.SuspendBlocker sb : this.mSuspendBlockers) {
                pw.println("  " + sb);
            }
            pw.println();
            pw.println("Display Power: " + this.mDisplayPowerCallbacks);
            if (this.mBatterySaverSupported) {
                this.mBatterySaverStateMachine.getBatterySaverPolicy().dump(pw);
                this.mBatterySaverStateMachine.dump(pw);
            } else {
                pw.println("Battery Saver: DISABLED");
            }
            this.mAttentionDetector.dump(pw);
            pw.println();
            int numProfiles = this.mProfilePowerState.size();
            pw.println("Profile power states: size=" + numProfiles);
            int i2 = 0;
            while (i2 < numProfiles) {
                com.android.server.power.PowerManagerService.ProfilePowerState profile = this.mProfilePowerState.valueAt(i2);
                pw.print("  mUserId=");
                pw.print(profile.mUserId);
                pw.print(" mScreenOffTimeout=");
                pw.print(profile.mScreenOffTimeout);
                pw.print(" mWakeLockSummary=");
                pw.print(profile.mWakeLockSummary);
                pw.print(" mLastUserActivityTime=");
                pw.print(profile.mLastUserActivityTime);
                pw.print(" mLockingNotified=");
                pw.println(profile.mLockingNotified);
                i2++;
                attentiveTimeout = attentiveTimeout;
            }
            pw.println("Display Group User Activity:");
            for (int idx = 0; idx < this.mPowerGroups.size(); idx++) {
                com.android.server.power.PowerGroup powerGroup = this.mPowerGroups.valueAt(idx);
                pw.println("  displayGroupId=" + powerGroup.getGroupId());
                pw.println("  userActivitySummary=0x" + java.lang.Integer.toHexString(powerGroup.getUserActivitySummaryLocked()));
                pw.println("  lastUserActivityTime=" + android.util.TimeUtils.formatUptime(powerGroup.getLastUserActivityTimeLocked()));
                pw.println("  lastUserActivityTimeNoChangeLights=" + android.util.TimeUtils.formatUptime(powerGroup.getLastUserActivityTimeNoChangeLightsLocked()));
                pw.println("  mWakeLockSummary=0x" + java.lang.Integer.toHexString(powerGroup.getWakeLockSummaryLocked()));
            }
            wcd = this.mWirelessChargerDetector;
        }
        if (wcd != null) {
            wcd.dump(pw);
        }
        if (this.mNotifier != null) {
            this.mNotifier.dump(pw);
        }
        this.mFaceDownDetector.dump(pw);
        this.mAmbientDisplaySuppressionController.dump(pw);
        this.mLowPowerStandbyController.dump(pw);
        synchronized (this.mLock) {
            if (this.mScreenTimeoutOverridePolicy != null) {
                this.mScreenTimeoutOverridePolicy.dump(pw);
            }
        }
        this.mFeatureFlags.dump(pw);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpProto(java.io.FileDescriptor fd) {
        com.android.server.power.WirelessChargerDetector wcd;
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
        synchronized (this.mLock) {
            this.mConstants.dumpProto(proto);
            proto.write(1120986464258L, this.mDirty);
            proto.write(1159641169923L, getGlobalWakefulnessLocked());
            proto.write(1133871366148L, this.mWakefulnessChanging);
            proto.write(1133871366149L, this.mIsPowered);
            proto.write(1159641169926L, this.mPlugType);
            proto.write(1120986464263L, this.mBatteryLevel);
            proto.write(1120986464313L, this.mDreamsBatteryLevelDrain);
            proto.write(1159641169929L, this.mDockState);
            proto.write(1133871366154L, this.mStayOn);
            proto.write(1133871366155L, this.mProximityPositive);
            proto.write(1133871366156L, this.mBootCompleted);
            proto.write(1133871366157L, this.mSystemReady);
            synchronized (this.mEnhancedDischargeTimeLock) {
                proto.write(1112396529716L, this.mEnhancedDischargeTimeElapsed);
                proto.write(1112396529717L, this.mLastEnhancedDischargeTimeUpdatedElapsed);
                proto.write(1133871366198L, this.mEnhancedDischargePredictionIsPersonalized);
            }
            proto.write(1133871366158L, this.mHalAutoSuspendModeEnabled);
            proto.write(1133871366159L, this.mHalInteractiveModeEnabled);
            long activeWakeLocksToken = proto.start(1146756268048L);
            boolean z = true;
            long j = 1133871366145L;
            proto.write(1133871366145L, (this.mWakeLockSummary & 1) != 0);
            proto.write(1133871366146L, (this.mWakeLockSummary & 2) != 0);
            proto.write(1133871366147L, (this.mWakeLockSummary & 4) != 0);
            proto.write(1133871366148L, (this.mWakeLockSummary & 8) != 0);
            proto.write(1133871366149L, (this.mWakeLockSummary & 16) != 0);
            proto.write(1133871366150L, (this.mWakeLockSummary & 32) != 0);
            proto.write(1133871366151L, (this.mWakeLockSummary & 64) != 0);
            proto.write(1133871366152L, (this.mWakeLockSummary & 128) != 0);
            proto.end(activeWakeLocksToken);
            proto.write(1112396529681L, this.mNotifyLongScheduled);
            proto.write(1112396529682L, this.mNotifyLongDispatched);
            proto.write(1112396529683L, this.mNotifyLongNextCheck);
            int idx = 0;
            while (idx < this.mPowerGroups.size()) {
                com.android.server.power.PowerGroup powerGroup = this.mPowerGroups.valueAt(idx);
                long userActivityToken = proto.start(2246267895828L);
                proto.write(1120986464262L, powerGroup.getGroupId());
                long userActivitySummary = powerGroup.getUserActivitySummaryLocked();
                proto.write(j, (userActivitySummary & 1) != 0);
                proto.write(1133871366146L, (userActivitySummary & 2) != 0);
                proto.write(1133871366147L, (4 & userActivitySummary) != 0);
                proto.write(1112396529668L, powerGroup.getLastUserActivityTimeLocked());
                proto.write(1112396529669L, powerGroup.getLastUserActivityTimeNoChangeLightsLocked());
                proto.end(userActivityToken);
                idx++;
                j = 1133871366145L;
            }
            proto.write(1133871366165L, this.mRequestWaitForNegativeProximity);
            proto.write(1133871366166L, this.mSandmanScheduled);
            proto.write(1133871366168L, this.mBatteryLevelLow);
            proto.write(1133871366169L, this.mLightDeviceIdleMode);
            proto.write(1133871366170L, this.mDeviceIdleMode);
            for (int id : this.mDeviceIdleWhitelist) {
                proto.write(2220498092059L, id);
            }
            for (int id2 : this.mDeviceIdleTempWhitelist) {
                proto.write(2220498092060L, id2);
            }
            proto.write(1133871366199L, this.mLowPowerStandbyActive);
            proto.write(1112396529693L, this.mLastGlobalWakeTime);
            proto.write(1112396529694L, this.mLastGlobalSleepTime);
            proto.write(1112396529697L, this.mLastInteractivePowerHintTime);
            proto.write(1112396529698L, this.mLastScreenBrightnessBoostTime);
            proto.write(1133871366179L, this.mScreenBrightnessBoostInProgress);
            proto.write(1133871366181L, this.mHoldingWakeLockSuspendBlocker);
            proto.write(1133871366182L, this.mHoldingDisplaySuspendBlocker);
            long settingsAndConfigurationToken = proto.start(1146756268071L);
            proto.write(1133871366145L, this.mDecoupleHalAutoSuspendModeFromDisplayConfig);
            proto.write(1133871366146L, this.mDecoupleHalInteractiveModeFromDisplayConfig);
            proto.write(1133871366147L, this.mWakeUpWhenPluggedOrUnpluggedConfig);
            proto.write(1133871366148L, this.mWakeUpWhenPluggedOrUnpluggedInTheaterModeConfig);
            proto.write(1133871366149L, this.mTheaterModeEnabled);
            proto.write(1133871366150L, this.mSuspendWhenScreenOffDueToProximityConfig);
            proto.write(1133871366151L, this.mDreamsSupportedConfig);
            proto.write(1133871366152L, this.mDreamsEnabledByDefaultConfig);
            proto.write(1133871366153L, this.mDreamsActivatedOnSleepByDefaultConfig);
            proto.write(1133871366154L, this.mDreamsActivatedOnDockByDefaultConfig);
            proto.write(1133871366155L, this.mDreamsEnabledOnBatteryConfig);
            proto.write(1172526071820L, this.mDreamsBatteryLevelMinimumWhenPoweredConfig);
            proto.write(1172526071821L, this.mDreamsBatteryLevelMinimumWhenNotPoweredConfig);
            proto.write(1172526071822L, this.mDreamsBatteryLevelDrainCutoffConfig);
            proto.write(1133871366159L, this.mDreamsEnabledSetting);
            proto.write(1133871366160L, this.mDreamsActivateOnSleepSetting);
            proto.write(1133871366161L, this.mDreamsActivateOnDockSetting);
            proto.write(1133871366162L, this.mDozeAfterScreenOff);
            proto.write(1120986464275L, this.mMinimumScreenOffTimeoutConfig);
            proto.write(1120986464276L, this.mMaximumScreenDimDurationConfig);
            proto.write(1108101562389L, this.mMaximumScreenDimRatioConfig);
            proto.write(1120986464278L, this.mScreenOffTimeoutSetting);
            proto.write(1172526071831L, this.mSleepTimeoutSetting);
            proto.write(1172526071845L, this.mAttentiveTimeoutSetting);
            proto.write(1172526071846L, this.mAttentiveTimeoutConfig);
            proto.write(1172526071847L, this.mAttentiveWarningDurationConfig);
            proto.write(1120986464280L, java.lang.Math.min(this.mMaximumScreenOffTimeoutFromDeviceAdmin, 2147483647L));
            proto.write(1133871366169L, isMaximumScreenOffTimeoutFromDeviceAdminEnforcedLocked());
            long stayOnWhilePluggedInToken = proto.start(1146756268058L);
            proto.write(1133871366145L, (this.mStayOnWhilePluggedInSetting & 1) != 0);
            proto.write(1133871366146L, (this.mStayOnWhilePluggedInSetting & 2) != 0);
            proto.write(1133871366147L, (this.mStayOnWhilePluggedInSetting & 4) != 0);
            if ((this.mStayOnWhilePluggedInSetting & 8) == 0) {
                z = false;
            }
            proto.write(1133871366148L, z);
            proto.end(stayOnWhilePluggedInToken);
            proto.write(1172526071836L, this.mScreenBrightnessOverrideFromWindowManager);
            proto.write(1176821039133L, this.mUserActivityTimeoutOverrideFromWindowManager);
            proto.write(1133871366174L, this.mUserInactiveOverrideFromWindowManager);
            proto.write(1159641169951L, this.mDozeScreenStateOverrideFromDreamManager);
            proto.write(1133871366180L, this.mDrawWakeLockOverrideFromSidekick);
            proto.write(1108101562400L, this.mDozeScreenBrightnessOverrideFromDreamManager);
            long screenBrightnessSettingLimitsToken = proto.start(1146756268065L);
            proto.write(1108101562372L, this.mScreenBrightnessMinimum);
            proto.write(1108101562373L, this.mScreenBrightnessMaximum);
            proto.write(1108101562374L, this.mScreenBrightnessDefault);
            proto.end(screenBrightnessSettingLimitsToken);
            proto.write(1133871366178L, this.mDoubleTapWakeEnabled);
            proto.end(settingsAndConfigurationToken);
            long attentiveTimeout = getAttentiveTimeoutLocked();
            long sleepTimeout = getSleepTimeoutLocked(attentiveTimeout);
            long screenOffTimeout = getScreenOffTimeoutLocked(sleepTimeout, attentiveTimeout);
            long screenDimDuration = getScreenDimDurationLocked(screenOffTimeout);
            proto.write(1172526071859L, attentiveTimeout);
            proto.write(1172526071848L, sleepTimeout);
            proto.write(1120986464297L, screenOffTimeout);
            long screenDimDuration2 = screenDimDuration;
            proto.write(1120986464298L, screenDimDuration2);
            proto.write(1133871366187L, this.mUidsChanging);
            proto.write(1133871366188L, this.mUidsChanged);
            int i = 0;
            while (i < this.mUidState.size()) {
                com.android.server.power.PowerManagerService.UidState state = this.mUidState.valueAt(i);
                long screenDimDuration3 = screenDimDuration2;
                long uIDToken = proto.start(2246267895853L);
                int uid = this.mUidState.keyAt(i);
                proto.write(1120986464257L, uid);
                proto.write(1138166333442L, android.os.UserHandle.formatUid(uid));
                proto.write(1133871366147L, state.mActive);
                proto.write(1120986464260L, state.mNumWakeLocks);
                proto.write(1159641169925L, android.app.ActivityManager.processStateAmToProto(state.mProcState));
                proto.end(uIDToken);
                i++;
                screenDimDuration2 = screenDimDuration3;
                screenOffTimeout = screenOffTimeout;
                attentiveTimeout = attentiveTimeout;
            }
            if (this.mBatterySaverSupported) {
                this.mBatterySaverStateMachine.dumpProto(proto, 1146756268082L);
            }
            this.mHandler.getLooper().dumpDebug(proto, 1146756268078L);
            for (com.android.server.power.PowerManagerService.WakeLock wl : this.mWakeLocks) {
                wl.dumpDebug(proto, 2246267895855L);
            }
            for (com.android.server.power.SuspendBlocker sb : this.mSuspendBlockers) {
                sb.dumpDebug(proto, 2246267895856L);
            }
            wcd = this.mWirelessChargerDetector;
        }
        if (wcd != null) {
            wcd.dumpDebug(proto, 1146756268081L);
        }
        this.mLowPowerStandbyController.dumpProto(proto, 1146756268088L);
        proto.flush();
    }

    private void incrementBootCount() {
        int count;
        synchronized (this.mLock) {
            try {
                count = android.provider.Settings.Global.getInt(getContext().getContentResolver(), "boot_count");
            } catch (android.provider.Settings.SettingNotFoundException e) {
                count = 0;
            }
            android.provider.Settings.Global.putInt(getContext().getContentResolver(), "boot_count", count + 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.os.WorkSource copyWorkSource(android.os.WorkSource workSource) {
        if (workSource != null) {
            return new android.os.WorkSource(workSource);
        }
        return null;
    }

    final class BatteryReceiver extends android.content.BroadcastReceiver {
        BatteryReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                com.android.server.power.PowerManagerService.this.handleBatteryStateChangedLocked();
            }
        }
    }

    private final class DreamReceiver extends android.content.BroadcastReceiver {
        private DreamReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                com.android.server.power.PowerManagerService.this.scheduleSandmanLocked();
            }
        }
    }

    final class UserSwitchedReceiver extends android.content.BroadcastReceiver {
        UserSwitchedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                com.android.server.power.PowerManagerService.this.handleSettingsChangedLocked();
            }
        }
    }

    private final class DockReceiver extends android.content.BroadcastReceiver {
        private DockReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                int dockState = intent.getIntExtra("android.intent.extra.DOCK_STATE", 0);
                if (com.android.server.power.PowerManagerService.this.mDockState != dockState) {
                    com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DOCK_STATE_CHANGED, dockState);
                    com.android.server.power.PowerManagerService.this.mDockState = dockState;
                    com.android.server.power.PowerManagerService.this.mDirty |= 1024;
                    com.android.server.power.PowerManagerService.this.updatePowerStateLocked();
                }
            }
        }
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        public SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                com.android.server.power.PowerManagerService.this.handleSettingsChangedLocked();
            }
        }
    }

    private final class PowerManagerHandlerCallback implements android.os.Handler.Callback {
        private PowerManagerHandlerCallback() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.power.PowerManagerService.this.handleUserActivityTimeout();
                    break;
                case 2:
                    com.android.server.power.PowerManagerService.this.handleSandman(msg.arg1);
                    break;
                case 3:
                    com.android.server.power.PowerManagerService.this.handleScreenBrightnessBoostTimeout();
                    break;
                case 4:
                    com.android.server.power.PowerManagerService.this.checkForLongWakeLocks();
                    break;
                case 5:
                    com.android.server.power.PowerManagerService.this.handleAttentiveTimeout();
                    break;
                case 6:
                    com.android.server.power.PowerManagerService.this.releaseAllOverrideWakeLocks(msg.arg1);
                    break;
            }
            com.android.server.power.PowerManagerService.mPmsExt.onPowerManagerHandlerHandleMessage(msg);
            return true;
        }
    }

    final class WakeLock implements android.os.IBinder.DeathRecipient {
        public long mAcquireTime;
        public android.os.IWakeLockCallback mCallback;
        public boolean mCallerPrivileged;
        public boolean mDisabled;
        public final int mDisplayId;
        public int mFlags;
        public java.lang.String mHistoryTag;
        public final android.os.IBinder mLock;
        public boolean mNotifiedAcquired;
        public boolean mNotifiedLong;
        public final int mOwnerPid;
        public final int mOwnerUid;
        public final java.lang.String mPackageName;
        public java.lang.String mTag;
        public final com.android.server.power.PowerManagerService.UidState mUidState;
        public android.os.WorkSource mWorkSource;
        private com.android.server.power.PowerManagerService.WakeLock.WakeLockWrapper mWakeLockWrapper = new com.android.server.power.PowerManagerService.WakeLock.WakeLockWrapper();
        private com.android.server.power.IWakeLockExt mWakeLockExt = (com.android.server.power.IWakeLockExt) system.ext.loader.core.ExtLoader.type(com.android.server.power.IWakeLockExt.class).base(this).create();

        public WakeLock(android.os.IBinder lock, int displayId, int flags, java.lang.String tag, java.lang.String packageName, android.os.WorkSource workSource, java.lang.String historyTag, int ownerUid, int ownerPid, com.android.server.power.PowerManagerService.UidState uidState, android.os.IWakeLockCallback callback) {
            this.mLock = lock;
            this.mDisplayId = displayId;
            this.mFlags = flags;
            this.mTag = tag;
            this.mPackageName = packageName;
            this.mWorkSource = com.android.server.power.PowerManagerService.copyWorkSource(workSource);
            this.mHistoryTag = historyTag;
            this.mOwnerUid = ownerUid;
            this.mOwnerPid = ownerPid;
            this.mUidState = uidState;
            this.mCallback = callback;
            linkToDeath();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            unlinkToDeath();
            com.android.server.power.PowerManagerService.this.handleWakeLockDeath(this);
        }

        private void linkToDeath() {
            try {
                this.mLock.linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                throw new java.lang.IllegalArgumentException("Wakelock.mLock is already dead.");
            }
        }

        void unlinkToDeath() {
            try {
                this.mLock.unlinkToDeath(this, 0);
            } catch (java.util.NoSuchElementException e) {
                android.util.Slog.wtf(com.android.server.power.PowerManagerService.TAG, "Failed to unlink Wakelock.mLock", e);
            }
        }

        public boolean setDisabled(boolean disabled) {
            if (this.mDisabled != disabled) {
                this.mDisabled = disabled;
                return true;
            }
            return false;
        }

        public boolean hasSameProperties(int flags, java.lang.String tag, android.os.WorkSource workSource, int ownerUid, int ownerPid, android.os.IWakeLockCallback callback) {
            return this.mFlags == flags && this.mTag.equals(tag) && hasSameWorkSource(workSource) && this.mOwnerUid == ownerUid && this.mOwnerPid == ownerPid;
        }

        public void updateProperties(int flags, java.lang.String tag, java.lang.String packageName, android.os.WorkSource workSource, java.lang.String historyTag, int ownerUid, int ownerPid, android.os.IWakeLockCallback callback) {
            if (!this.mPackageName.equals(packageName)) {
                throw new java.lang.IllegalStateException("Existing wake lock package name changed: " + this.mPackageName + " to " + packageName);
            }
            if (this.mOwnerUid != ownerUid) {
                throw new java.lang.IllegalStateException("Existing wake lock uid changed: " + this.mOwnerUid + " to " + ownerUid);
            }
            if (this.mOwnerPid != ownerPid) {
                throw new java.lang.IllegalStateException("Existing wake lock pid changed: " + this.mOwnerPid + " to " + ownerPid);
            }
            this.mFlags = flags;
            this.mTag = tag;
            updateWorkSource(workSource);
            this.mHistoryTag = historyTag;
            this.mCallback = callback;
        }

        public boolean hasSameWorkSource(android.os.WorkSource workSource) {
            return java.util.Objects.equals(this.mWorkSource, workSource);
        }

        public void updateWorkSource(android.os.WorkSource workSource) {
            this.mWorkSource = com.android.server.power.PowerManagerService.copyWorkSource(workSource);
        }

        public java.lang.Integer getPowerGroupId() {
            if (!com.android.server.power.PowerManagerService.this.mSystemReady || this.mDisplayId == -1) {
                return -1;
            }
            android.view.DisplayInfo displayInfo = com.android.server.power.PowerManagerService.this.mDisplayManagerInternal.getDisplayInfo(this.mDisplayId);
            if (displayInfo != null) {
                return java.lang.Integer.valueOf(displayInfo.displayGroupId);
            }
            return null;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(getLockLevelString());
            sb.append(" '");
            sb.append(this.mTag);
            sb.append("'");
            sb.append(getLockFlagsString());
            if (this.mDisabled) {
                sb.append(" DISABLED");
            }
            if (this.mNotifiedAcquired) {
                sb.append(" ACQ=");
                android.util.TimeUtils.formatDuration(this.mAcquireTime - com.android.server.power.PowerManagerService.this.mClock.uptimeMillis(), sb);
            }
            if (this.mNotifiedLong) {
                sb.append(" LONG");
            }
            sb.append(" (uid=");
            sb.append(this.mOwnerUid);
            if (this.mOwnerPid != 0) {
                sb.append(" pid=");
                sb.append(this.mOwnerPid);
            }
            if (this.mWorkSource != null) {
                sb.append(" ws=");
                sb.append(this.mWorkSource);
            }
            sb.append(")");
            return sb.toString();
        }

        public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long wakeLockToken = proto.start(fieldId);
            proto.write(1159641169921L, this.mFlags & 65535);
            proto.write(1138166333442L, this.mTag);
            long wakeLockFlagsToken = proto.start(1146756268035L);
            proto.write(1133871366145L, (this.mFlags & 268435456) != 0);
            proto.write(1133871366146L, (this.mFlags & 536870912) != 0);
            proto.write(1133871366147L, (this.mFlags & Integer.MIN_VALUE) != 0);
            proto.end(wakeLockFlagsToken);
            proto.write(1133871366148L, this.mDisabled);
            if (this.mNotifiedAcquired) {
                proto.write(1112396529669L, this.mAcquireTime);
            }
            proto.write(1133871366150L, this.mNotifiedLong);
            proto.write(1120986464263L, this.mOwnerUid);
            proto.write(1120986464264L, this.mOwnerPid);
            if (this.mWorkSource != null) {
                this.mWorkSource.dumpDebug(proto, 1146756268041L);
            }
            proto.end(wakeLockToken);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String getLockLevelString() {
            switch (this.mFlags & 65535) {
                case 1:
                    return "PARTIAL_WAKE_LOCK                ";
                case 6:
                    return "SCREEN_DIM_WAKE_LOCK             ";
                case 10:
                    return "SCREEN_BRIGHT_WAKE_LOCK          ";
                case 26:
                    return "FULL_WAKE_LOCK                   ";
                case 32:
                    return "PROXIMITY_SCREEN_OFF_WAKE_LOCK   ";
                case 64:
                    return "DOZE_WAKE_LOCK                   ";
                case 128:
                    return "DRAW_WAKE_LOCK                   ";
                case 256:
                    return "SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK";
                default:
                    return "???                              ";
            }
        }

        private java.lang.String getLockFlagsString() {
            java.lang.String result = (this.mFlags & 268435456) != 0 ? " ACQUIRE_CAUSES_WAKEUP" : "";
            if ((this.mFlags & 536870912) != 0) {
                result = result + " ON_AFTER_RELEASE";
            }
            if ((this.mFlags & Integer.MIN_VALUE) != 0) {
                return result + " SYSTEM_WAKELOCK";
            }
            return result;
        }

        public void setCallerPrivileged(boolean isCallerPrivileged) {
            this.mCallerPrivileged = isCallerPrivileged;
        }

        public com.android.server.power.IWakeLockWrapper getWrapper() {
            return this.mWakeLockWrapper;
        }

        private class WakeLockWrapper implements com.android.server.power.IWakeLockWrapper {
            private WakeLockWrapper() {
            }

            @Override // com.android.server.power.IWakeLockWrapper
            public com.android.server.power.IWakeLockExt getExtImpl() {
                return com.android.server.power.PowerManagerService.WakeLock.this.mWakeLockExt;
            }

            @Override // com.android.server.power.IWakeLockWrapper
            public java.lang.String getLockLevelString() {
                return com.android.server.power.PowerManagerService.WakeLock.this.getLockLevelString();
            }
        }
    }

    private final class SuspendBlockerImpl implements com.android.server.power.SuspendBlocker {
        private static final java.lang.String UNKNOWN_ID = "unknown";
        private final java.lang.String mName;
        private final int mNameHash;
        private final android.util.ArrayMap<java.lang.String, android.util.LongArray> mOpenReferenceTimes = new android.util.ArrayMap<>();
        private int mReferenceCount;

        public SuspendBlockerImpl(java.lang.String name) {
            this.mName = name;
            this.mNameHash = this.mName.hashCode();
        }

        protected void finalize() throws java.lang.Throwable {
            try {
                if (this.mReferenceCount != 0) {
                    android.util.Slog.wtf(com.android.server.power.PowerManagerService.TAG, "Suspend blocker \"" + this.mName + "\" was finalized without being released!");
                    this.mReferenceCount = 0;
                    com.android.server.power.PowerManagerService.this.mNativeWrapper.nativeReleaseSuspendBlocker(this.mName);
                    android.os.Trace.asyncTraceForTrackEnd(131072L, "SuspendBlockers", this.mNameHash);
                }
            } finally {
                super.finalize();
            }
        }

        @Override // com.android.server.power.SuspendBlocker
        public void acquire() {
            acquire("unknown");
        }

        @Override // com.android.server.power.SuspendBlocker
        public void acquire(java.lang.String id) {
            synchronized (this) {
                recordReferenceLocked(id);
                this.mReferenceCount++;
                if (this.mReferenceCount == 1) {
                    if (com.android.server.power.PowerManagerService.DEBUG_SPEW) {
                        android.util.Slog.d(com.android.server.power.PowerManagerService.TAG, "Acquiring suspend blocker \"" + this.mName + "\".");
                    }
                    android.os.Trace.asyncTraceForTrackBegin(131072L, "SuspendBlockers", this.mName, this.mNameHash);
                    if (com.android.server.power.PowerManagerService.mPmsExt != null) {
                        com.android.server.power.PowerManagerService.mPmsExt.acquireSuspendBlockerStart();
                    }
                    com.android.server.power.PowerManagerService.this.mNativeWrapper.nativeAcquireSuspendBlocker(this.mName);
                    if (com.android.server.power.PowerManagerService.mPmsExt != null) {
                        com.android.server.power.PowerManagerService.mPmsExt.acquireSuspendBlockerEnd(this.mName);
                    }
                }
            }
        }

        @Override // com.android.server.power.SuspendBlocker
        public void release() {
            release("unknown");
        }

        @Override // com.android.server.power.SuspendBlocker
        public void release(java.lang.String id) {
            synchronized (this) {
                removeReferenceLocked(id);
                this.mReferenceCount--;
                if (this.mReferenceCount == 0) {
                    if (com.android.server.power.PowerManagerService.DEBUG_SPEW) {
                        android.util.Slog.d(com.android.server.power.PowerManagerService.TAG, "Releasing suspend blocker \"" + this.mName + "\".");
                    }
                    if (com.android.server.power.PowerManagerService.mPmsExt != null) {
                        com.android.server.power.PowerManagerService.mPmsExt.releaseSuspendBlocker(this.mName);
                    }
                    com.android.server.power.PowerManagerService.this.mNativeWrapper.nativeReleaseSuspendBlocker(this.mName);
                    if (android.os.Trace.isTagEnabled(131072L)) {
                        android.os.Trace.asyncTraceForTrackEnd(131072L, "SuspendBlockers", this.mNameHash);
                    }
                } else if (this.mReferenceCount < 0) {
                    android.util.Slog.wtf(com.android.server.power.PowerManagerService.TAG, "Suspend blocker \"" + this.mName + "\" was released without being acquired!", new java.lang.Throwable());
                    this.mReferenceCount = 0;
                }
            }
        }

        public java.lang.String toString() {
            java.lang.String string;
            synchronized (this) {
                java.lang.StringBuilder builder = new java.lang.StringBuilder();
                builder.append(this.mName);
                builder.append(": ref count=").append(this.mReferenceCount);
                builder.append(" [");
                int size = this.mOpenReferenceTimes.size();
                for (int i = 0; i < size; i++) {
                    java.lang.String id = this.mOpenReferenceTimes.keyAt(i);
                    android.util.LongArray times = this.mOpenReferenceTimes.valueAt(i);
                    if (times != null && times.size() != 0) {
                        if (i > 0) {
                            builder.append(", ");
                        }
                        builder.append(id).append(": (");
                        for (int j = 0; j < times.size(); j++) {
                            if (j > 0) {
                                builder.append(", ");
                            }
                            builder.append(com.android.server.power.PowerManagerService.DATE_FORMAT.format(new java.util.Date(times.get(j))));
                        }
                        builder.append(")");
                    }
                }
                builder.append("]");
                string = builder.toString();
            }
            return string;
        }

        @Override // com.android.server.power.SuspendBlocker
        public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long sbToken = proto.start(fieldId);
            synchronized (this) {
                proto.write(1138166333441L, this.mName);
                proto.write(1120986464258L, this.mReferenceCount);
            }
            proto.end(sbToken);
        }

        private void recordReferenceLocked(java.lang.String id) {
            android.util.LongArray times = this.mOpenReferenceTimes.get(id);
            if (times == null) {
                times = new android.util.LongArray(2);
                this.mOpenReferenceTimes.put(id, times);
            }
            times.add(java.lang.System.currentTimeMillis());
        }

        private void removeReferenceLocked(java.lang.String id) {
            android.util.LongArray times = this.mOpenReferenceTimes.get(id);
            if (times != null && times.size() > 0) {
                times.remove(times.size() - 1);
                if (times.size() == 0) {
                    this.mOpenReferenceTimes.remove(id);
                }
            }
        }
    }

    static final class UidState {
        boolean mActive;
        int mNumWakeLocks;
        int mProcState;
        final int mUid;

        UidState(int uid) {
            this.mUid = uid;
        }
    }

    final class BinderService extends android.os.IPowerManager.Stub {
        private final com.android.server.power.PowerManagerShellCommand mShellCommand;

        BinderService(android.content.Context context) {
            this.mShellCommand = new com.android.server.power.PowerManagerShellCommand(context, this);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            this.mShellCommand.exec(this, in, out, err, args, callback, resultReceiver);
        }

        public void acquireWakeLockWithUid(android.os.IBinder lock, int flags, java.lang.String tag, java.lang.String packageName, int uid, int displayId, android.os.IWakeLockCallback callback) {
            int uid2;
            if (uid >= 0) {
                uid2 = uid;
            } else {
                uid2 = android.os.Binder.getCallingUid();
            }
            acquireWakeLock(lock, flags, tag, packageName, new android.os.WorkSource(uid2), null, displayId, callback);
        }

        public void setPowerBoost(int boost, int durationMs) {
            if (!com.android.server.power.PowerManagerService.this.mSystemReady) {
                return;
            }
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            com.android.server.power.PowerManagerService.this.setPowerBoostInternal(boost, durationMs);
        }

        public void setPowerMode(int mode, boolean enabled) {
            if (!com.android.server.power.PowerManagerService.this.mSystemReady) {
                return;
            }
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            com.android.server.power.PowerManagerService.this.setPowerModeInternal(mode, enabled);
        }

        public boolean setPowerModeChecked(int mode, boolean enabled) {
            if (!com.android.server.power.PowerManagerService.this.mSystemReady) {
                return false;
            }
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            return com.android.server.power.PowerManagerService.this.setPowerModeInternal(mode, enabled);
        }

        public void acquireWakeLock(android.os.IBinder lock, int flags, java.lang.String tag, java.lang.String packageName, android.os.WorkSource ws, java.lang.String historyTag, int displayId, android.os.IWakeLockCallback callback) {
            android.os.WorkSource ws2;
            android.os.WorkSource ws3;
            int uid;
            int pid;
            if (lock == null) {
                throw new java.lang.IllegalArgumentException("lock must not be null");
            }
            if (packageName == null) {
                throw new java.lang.IllegalArgumentException("packageName must not be null");
            }
            android.os.PowerManager.validateWakeLockParameters(flags, tag);
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.WAKE_LOCK", null);
            if ((flags & 64) != 0) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            }
            if ((flags & 256) != 0) {
                if (!com.android.server.power.PowerManagerService.this.mFeatureFlags.isEarlyScreenTimeoutDetectorEnabled()) {
                    throw new java.lang.IllegalArgumentException("Acquiring an unsupported wake lock: flags=" + flags + ", tag=" + tag);
                }
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.SCREEN_TIMEOUT_OVERRIDE", null);
            }
            if (ws != null && !ws.isEmpty()) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.UPDATE_DEVICE_STATS", null);
                ws2 = ws;
            } else {
                ws2 = null;
            }
            int uid2 = android.os.Binder.getCallingUid();
            int pid2 = android.os.Binder.getCallingPid();
            if ((Integer.MIN_VALUE & flags) == 0) {
                ws3 = ws2;
                uid = uid2;
                pid = pid2;
            } else {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                android.os.WorkSource workSource = new android.os.WorkSource(android.os.Binder.getCallingUid(), packageName);
                if (ws2 != null && !ws2.isEmpty()) {
                    workSource.add(ws2);
                }
                int uid3 = android.os.Process.myUid();
                int pid3 = android.os.Process.myPid();
                ws3 = workSource;
                uid = uid3;
                pid = pid3;
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.acquireWakeLockInternal(lock, displayId, flags, tag, packageName, ws3, historyTag, uid, pid, callback);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void acquireWakeLockAsync(android.os.IBinder lock, int flags, java.lang.String tag, java.lang.String packageName, android.os.WorkSource ws, java.lang.String historyTag) {
            acquireWakeLock(lock, flags, tag, packageName, ws, historyTag, -1, null);
        }

        public void releaseWakeLock(android.os.IBinder lock, int flags) {
            if (lock == null) {
                throw new java.lang.IllegalArgumentException("lock must not be null");
            }
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.WAKE_LOCK", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.releaseWakeLockInternal(lock, flags);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void releaseWakeLockAsync(android.os.IBinder lock, int flags) {
            releaseWakeLock(lock, flags);
        }

        public void updateWakeLockUids(android.os.IBinder lock, int[] uids) {
            android.os.WorkSource ws = null;
            if (uids != null) {
                ws = new android.os.WorkSource();
                for (int uid : uids) {
                    ws.add(uid);
                }
            }
            updateWakeLockWorkSource(lock, ws, null);
        }

        public void updateWakeLockUidsAsync(android.os.IBinder lock, int[] uids) {
            updateWakeLockUids(lock, uids);
        }

        public void updateWakeLockWorkSource(android.os.IBinder lock, android.os.WorkSource ws, java.lang.String historyTag) {
            if (lock == null) {
                throw new java.lang.IllegalArgumentException("lock must not be null");
            }
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.WAKE_LOCK", null);
            if (ws != null && !ws.isEmpty()) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.UPDATE_DEVICE_STATS", null);
            } else {
                ws = null;
            }
            int callingUid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.updateWakeLockWorkSourceInternal(lock, ws, historyTag, callingUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void updateWakeLockCallback(android.os.IBinder lock, android.os.IWakeLockCallback callback) {
            if (lock == null) {
                throw new java.lang.IllegalArgumentException("lock must not be null");
            }
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.WAKE_LOCK", null);
            int callingUid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.updateWakeLockCallbackInternal(lock, callback, callingUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isWakeLockLevelSupported(int level) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.isWakeLockLevelSupportedInternal(level);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void userActivity(int displayId, long eventTime, int event, int flags) {
            long now = com.android.server.power.PowerManagerService.this.mClock.uptimeMillis();
            if (com.android.server.power.PowerManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.DEVICE_POWER") != 0 && com.android.server.power.PowerManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.USER_ACTIVITY") != 0) {
                synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                    if (now >= com.android.server.power.PowerManagerService.this.mLastWarningAboutUserActivityPermission + 300000) {
                        com.android.server.power.PowerManagerService.this.mLastWarningAboutUserActivityPermission = now;
                        android.util.Slog.w(com.android.server.power.PowerManagerService.TAG, "Ignoring call to PowerManager.userActivity() because the caller does not have DEVICE_POWER or USER_ACTIVITY permission.  Please fix your app!   pid=" + android.os.Binder.getCallingPid() + " uid=" + android.os.Binder.getCallingUid());
                    }
                }
                return;
            }
            if (eventTime > now) {
                android.util.Slog.wtf(com.android.server.power.PowerManagerService.TAG, "Event cannot be newer than the current time (now=" + now + ", eventTime=" + eventTime + ", displayId=" + displayId + ", event=" + android.os.PowerManager.userActivityEventToString(event) + ", flags=" + flags + ")");
                return;
            }
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.userActivityInternal(displayId, eventTime, event, flags, uid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void wakeUp(long eventTime, int reason, java.lang.String details, java.lang.String opPackageName) {
            long now = com.android.server.power.PowerManagerService.this.mClock.uptimeMillis();
            if (eventTime > now) {
                android.util.Slog.e(com.android.server.power.PowerManagerService.TAG, "Event time " + eventTime + " cannot be newer than " + now);
                throw new java.lang.IllegalArgumentException("event time must not be in the future");
            }
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                    if (com.android.server.power.PowerManagerService.this.mBootCompleted || !com.android.server.power.PowerManagerService.sQuiescent) {
                        com.android.server.power.PowerManagerService.this.wakePowerGroupLocked((com.android.server.power.PowerGroup) com.android.server.power.PowerManagerService.this.mPowerGroups.get(0), eventTime, reason, details, uid, opPackageName, uid);
                        return;
                    }
                    com.android.server.power.PowerManagerService.this.mDirty |= 4096;
                    com.android.server.power.PowerManagerService.this.updatePowerStateLocked();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void goToSleep(long eventTime, int reason, int flags) {
            com.android.server.power.PowerManagerService.this.goToSleepInternal(com.android.server.power.PowerManagerService.DEFAULT_DISPLAY_GROUP_IDS, eventTime, reason, flags);
        }

        public void goToSleepWithDisplayId(int displayId, long eventTime, int reason, int flags) {
            android.util.IntArray groupIds;
            if (displayId == -1) {
                groupIds = com.android.server.power.PowerManagerService.this.mDisplayManagerInternal.getDisplayGroupIds();
            } else {
                android.view.DisplayInfo displayInfo = com.android.server.power.PowerManagerService.this.mDisplayManagerInternal.getDisplayInfo(displayId);
                com.android.internal.util.Preconditions.checkArgument(displayInfo != null, "display ID(%d) doesn't exist", new java.lang.Object[]{java.lang.Integer.valueOf(displayId)});
                int groupId = displayInfo.displayGroupId;
                if (groupId == -1) {
                    throw new java.lang.IllegalArgumentException("invalid display group ID");
                }
                groupIds = android.util.IntArray.wrap(new int[]{groupId});
            }
            com.android.server.power.PowerManagerService.this.goToSleepInternal(groupIds, eventTime, reason, flags);
        }

        public void nap(long eventTime) {
            long now = com.android.server.power.PowerManagerService.this.mClock.uptimeMillis();
            if (eventTime > now) {
                android.util.Slog.e(com.android.server.power.PowerManagerService.TAG, "Event time " + eventTime + " cannot be newer than " + now);
                throw new java.lang.IllegalArgumentException("event time must not be in the future");
            }
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.napInternal(eventTime, uid, false);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public float getBrightnessConstraint(int constraint) {
            switch (constraint) {
                case 0:
                    return com.android.server.power.PowerManagerService.this.mScreenBrightnessMinimum;
                case 1:
                    return com.android.server.power.PowerManagerService.this.mScreenBrightnessMaximum;
                case 2:
                    return com.android.server.power.PowerManagerService.this.mScreenBrightnessDefault;
                case 3:
                    return com.android.server.power.PowerManagerService.this.mScreenBrightnessDim;
                case 4:
                    return com.android.server.power.PowerManagerService.this.mScreenBrightnessDoze;
                default:
                    return Float.NaN;
            }
        }

        public boolean isInteractive() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.isGloballyInteractiveInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isDisplayInteractive(int displayId) {
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.isInteractiveInternal(displayId, uid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean areAutoPowerSaveModesEnabled() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.mContext.getResources().getBoolean(android.R.bool.config_enableActivityRecognitionHardwareOverlay);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x001e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean isPowerSaveMode() {
            /*
                r3 = this;
                long r0 = android.os.Binder.clearCallingIdentity()
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L23
                boolean r2 = com.android.server.power.PowerManagerService.m8266$$Nest$fgetmBatterySaverSupported(r2)     // Catch: java.lang.Throwable -> L23
                if (r2 == 0) goto L1e
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L23
                com.android.server.power.batterysaver.BatterySaverStateMachine r2 = com.android.server.power.PowerManagerService.m8265$$Nest$fgetmBatterySaverStateMachine(r2)     // Catch: java.lang.Throwable -> L23
                com.android.server.power.batterysaver.BatterySaverController r2 = r2.getBatterySaverController()     // Catch: java.lang.Throwable -> L23
                boolean r2 = r2.isEnabled()     // Catch: java.lang.Throwable -> L23
                if (r2 == 0) goto L1e
                r2 = 1
                goto L1f
            L1e:
                r2 = 0
            L1f:
                android.os.Binder.restoreCallingIdentity(r0)
                return r2
            L23:
                r2 = move-exception
                android.os.Binder.restoreCallingIdentity(r0)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.PowerManagerService.BinderService.isPowerSaveMode():boolean");
        }

        public android.os.PowerSaveState getPowerSaveState(int serviceType) {
            android.os.PowerSaveState powerSaveStateBuild;
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.power.PowerManagerService.this.mBatterySaverSupported) {
                    powerSaveStateBuild = com.android.server.power.PowerManagerService.this.mBatterySaverStateMachine.getBatterySaverPolicy().getBatterySaverPolicy(serviceType);
                } else {
                    powerSaveStateBuild = new android.os.PowerSaveState.Builder().build();
                }
                return powerSaveStateBuild;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean setPowerSaveModeEnabled(boolean enabled) {
            if (com.android.server.power.PowerManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.POWER_SAVER") != 0) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.setLowPowerModeInternal(enabled);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isBatterySaverSupported() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.mBatterySaverSupported;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public android.os.BatterySaverPolicyConfig getFullPowerSavePolicy() {
            android.os.BatterySaverPolicyConfig batterySaverPolicyConfigBuild;
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.power.PowerManagerService.this.mBatterySaverSupported) {
                    batterySaverPolicyConfigBuild = com.android.server.power.PowerManagerService.this.mBatterySaverStateMachine.getFullBatterySaverPolicy();
                } else {
                    batterySaverPolicyConfigBuild = new android.os.BatterySaverPolicyConfig.Builder().build();
                }
                return batterySaverPolicyConfigBuild;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0036  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean setFullPowerSavePolicy(android.os.BatterySaverPolicyConfig r4) {
            /*
                r3 = this;
                com.android.server.power.PowerManagerService r0 = com.android.server.power.PowerManagerService.this
                android.content.Context r0 = com.android.server.power.PowerManagerService.m8270$$Nest$fgetmContext(r0)
                java.lang.String r1 = "android.permission.POWER_SAVER"
                int r0 = r0.checkCallingOrSelfPermission(r1)
                if (r0 == 0) goto L1c
                com.android.server.power.PowerManagerService r0 = com.android.server.power.PowerManagerService.this
                android.content.Context r0 = com.android.server.power.PowerManagerService.m8270$$Nest$fgetmContext(r0)
                java.lang.String r1 = "android.permission.DEVICE_POWER"
                java.lang.String r2 = "setFullPowerSavePolicy"
                r0.enforceCallingOrSelfPermission(r1, r2)
            L1c:
                long r0 = android.os.Binder.clearCallingIdentity()
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L3b
                boolean r2 = com.android.server.power.PowerManagerService.m8266$$Nest$fgetmBatterySaverSupported(r2)     // Catch: java.lang.Throwable -> L3b
                if (r2 == 0) goto L36
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L3b
                com.android.server.power.batterysaver.BatterySaverStateMachine r2 = com.android.server.power.PowerManagerService.m8265$$Nest$fgetmBatterySaverStateMachine(r2)     // Catch: java.lang.Throwable -> L3b
                boolean r2 = r2.setFullBatterySaverPolicy(r4)     // Catch: java.lang.Throwable -> L3b
                if (r2 == 0) goto L36
                r2 = 1
                goto L37
            L36:
                r2 = 0
            L37:
                android.os.Binder.restoreCallingIdentity(r0)
                return r2
            L3b:
                r2 = move-exception
                android.os.Binder.restoreCallingIdentity(r0)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.PowerManagerService.BinderService.setFullPowerSavePolicy(android.os.BatterySaverPolicyConfig):boolean");
        }

        public boolean setDynamicPowerSaveHint(boolean powerSaveHint, int disableThreshold) {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.POWER_SAVER", "updateDynamicPowerSavings");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.content.ContentResolver resolver = com.android.server.power.PowerManagerService.this.mContext.getContentResolver();
                boolean success = android.provider.Settings.Global.putInt(resolver, "dynamic_power_savings_disable_threshold", disableThreshold);
                if (success) {
                    success &= android.provider.Settings.Global.putInt(resolver, "dynamic_power_savings_enabled", powerSaveHint ? 1 : 0);
                }
                return success;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0036  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean setAdaptivePowerSavePolicy(android.os.BatterySaverPolicyConfig r4) {
            /*
                r3 = this;
                com.android.server.power.PowerManagerService r0 = com.android.server.power.PowerManagerService.this
                android.content.Context r0 = com.android.server.power.PowerManagerService.m8270$$Nest$fgetmContext(r0)
                java.lang.String r1 = "android.permission.POWER_SAVER"
                int r0 = r0.checkCallingOrSelfPermission(r1)
                if (r0 == 0) goto L1c
                com.android.server.power.PowerManagerService r0 = com.android.server.power.PowerManagerService.this
                android.content.Context r0 = com.android.server.power.PowerManagerService.m8270$$Nest$fgetmContext(r0)
                java.lang.String r1 = "android.permission.DEVICE_POWER"
                java.lang.String r2 = "setAdaptivePowerSavePolicy"
                r0.enforceCallingOrSelfPermission(r1, r2)
            L1c:
                long r0 = android.os.Binder.clearCallingIdentity()
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L3b
                boolean r2 = com.android.server.power.PowerManagerService.m8266$$Nest$fgetmBatterySaverSupported(r2)     // Catch: java.lang.Throwable -> L3b
                if (r2 == 0) goto L36
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L3b
                com.android.server.power.batterysaver.BatterySaverStateMachine r2 = com.android.server.power.PowerManagerService.m8265$$Nest$fgetmBatterySaverStateMachine(r2)     // Catch: java.lang.Throwable -> L3b
                boolean r2 = r2.setAdaptiveBatterySaverPolicy(r4)     // Catch: java.lang.Throwable -> L3b
                if (r2 == 0) goto L36
                r2 = 1
                goto L37
            L36:
                r2 = 0
            L37:
                android.os.Binder.restoreCallingIdentity(r0)
                return r2
            L3b:
                r2 = move-exception
                android.os.Binder.restoreCallingIdentity(r0)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.PowerManagerService.BinderService.setAdaptivePowerSavePolicy(android.os.BatterySaverPolicyConfig):boolean");
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0036  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean setAdaptivePowerSaveEnabled(boolean r4) {
            /*
                r3 = this;
                com.android.server.power.PowerManagerService r0 = com.android.server.power.PowerManagerService.this
                android.content.Context r0 = com.android.server.power.PowerManagerService.m8270$$Nest$fgetmContext(r0)
                java.lang.String r1 = "android.permission.POWER_SAVER"
                int r0 = r0.checkCallingOrSelfPermission(r1)
                if (r0 == 0) goto L1c
                com.android.server.power.PowerManagerService r0 = com.android.server.power.PowerManagerService.this
                android.content.Context r0 = com.android.server.power.PowerManagerService.m8270$$Nest$fgetmContext(r0)
                java.lang.String r1 = "android.permission.DEVICE_POWER"
                java.lang.String r2 = "setAdaptivePowerSaveEnabled"
                r0.enforceCallingOrSelfPermission(r1, r2)
            L1c:
                long r0 = android.os.Binder.clearCallingIdentity()
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L3b
                boolean r2 = com.android.server.power.PowerManagerService.m8266$$Nest$fgetmBatterySaverSupported(r2)     // Catch: java.lang.Throwable -> L3b
                if (r2 == 0) goto L36
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L3b
                com.android.server.power.batterysaver.BatterySaverStateMachine r2 = com.android.server.power.PowerManagerService.m8265$$Nest$fgetmBatterySaverStateMachine(r2)     // Catch: java.lang.Throwable -> L3b
                boolean r2 = r2.setAdaptiveBatterySaverEnabled(r4)     // Catch: java.lang.Throwable -> L3b
                if (r2 == 0) goto L36
                r2 = 1
                goto L37
            L36:
                r2 = 0
            L37:
                android.os.Binder.restoreCallingIdentity(r0)
                return r2
            L3b:
                r2 = move-exception
                android.os.Binder.restoreCallingIdentity(r0)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.PowerManagerService.BinderService.setAdaptivePowerSaveEnabled(boolean):boolean");
        }

        public int getPowerSaveModeTrigger() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return android.provider.Settings.Global.getInt(com.android.server.power.PowerManagerService.this.mContext.getContentResolver(), "automatic_power_save_mode", 0);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setBatteryDischargePrediction(android.os.ParcelDuration timeRemaining, boolean isPersonalized) {
            long nowElapsed = com.android.server.power.PowerManagerService.this.mClock.elapsedRealtime();
            if (com.android.server.power.PowerManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.BATTERY_PREDICTION") != 0) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", "setBatteryDischargePrediction");
            }
            long timeRemainingMs = timeRemaining.getDuration().toMillis();
            com.android.internal.util.Preconditions.checkArgumentPositive(timeRemainingMs, "Given time remaining is not positive: " + timeRemainingMs);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                    if (com.android.server.power.PowerManagerService.this.mIsPowered) {
                        throw new java.lang.IllegalStateException("Discharge prediction can't be set while the device is charging");
                    }
                }
                synchronized (com.android.server.power.PowerManagerService.this.mEnhancedDischargeTimeLock) {
                    if (com.android.server.power.PowerManagerService.this.mLastEnhancedDischargeTimeUpdatedElapsed > nowElapsed) {
                        return;
                    }
                    long broadcastDelayMs = java.lang.Math.max(0L, 60000 - (nowElapsed - com.android.server.power.PowerManagerService.this.mLastEnhancedDischargeTimeUpdatedElapsed));
                    com.android.server.power.PowerManagerService.this.mEnhancedDischargeTimeElapsed = nowElapsed + timeRemainingMs;
                    com.android.server.power.PowerManagerService.this.mEnhancedDischargePredictionIsPersonalized = isPersonalized;
                    com.android.server.power.PowerManagerService.this.mLastEnhancedDischargeTimeUpdatedElapsed = nowElapsed;
                    com.android.server.power.PowerManagerService.this.mNotifier.postEnhancedDischargePredictionBroadcast(broadcastDelayMs);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        private boolean isEnhancedDischargePredictionValidLocked(long nowElapsed) {
            return com.android.server.power.PowerManagerService.this.mLastEnhancedDischargeTimeUpdatedElapsed > 0 && nowElapsed < com.android.server.power.PowerManagerService.this.mEnhancedDischargeTimeElapsed && nowElapsed - com.android.server.power.PowerManagerService.this.mLastEnhancedDischargeTimeUpdatedElapsed < 1800000;
        }

        public android.os.ParcelDuration getBatteryDischargePrediction() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                    if (com.android.server.power.PowerManagerService.this.mIsPowered) {
                        return null;
                    }
                    synchronized (com.android.server.power.PowerManagerService.this.mEnhancedDischargeTimeLock) {
                        long nowElapsed = com.android.server.power.PowerManagerService.this.mClock.elapsedRealtime();
                        if (!isEnhancedDischargePredictionValidLocked(nowElapsed)) {
                            return new android.os.ParcelDuration(com.android.server.power.PowerManagerService.this.mBatteryStats.computeBatteryTimeRemaining());
                        }
                        return new android.os.ParcelDuration(com.android.server.power.PowerManagerService.this.mEnhancedDischargeTimeElapsed - nowElapsed);
                    }
                }
            } catch (android.os.RemoteException e) {
                return null;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isBatteryDischargePredictionPersonalized() {
            boolean z;
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.power.PowerManagerService.this.mEnhancedDischargeTimeLock) {
                    z = isEnhancedDischargePredictionValidLocked(com.android.server.power.PowerManagerService.this.mClock.elapsedRealtime()) && com.android.server.power.PowerManagerService.this.mEnhancedDischargePredictionIsPersonalized;
                }
                return z;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isDeviceIdleMode() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.isDeviceIdleModeInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isLightDeviceIdleMode() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.isLightDeviceIdleModeInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isLowPowerStandbySupported() {
            if (com.android.server.power.PowerManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.DEVICE_POWER") != 0) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_LOW_POWER_STANDBY", "isLowPowerStandbySupported");
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.isSupported();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isLowPowerStandbyEnabled() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.isEnabled();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setLowPowerStandbyEnabled(boolean enabled) {
            if (com.android.server.power.PowerManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.DEVICE_POWER") != 0) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_LOW_POWER_STANDBY", "setLowPowerStandbyEnabled");
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.setEnabled(enabled);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setLowPowerStandbyActiveDuringMaintenance(boolean activeDuringMaintenance) {
            if (com.android.server.power.PowerManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.DEVICE_POWER") != 0) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_LOW_POWER_STANDBY", "setLowPowerStandbyActiveDuringMaintenance");
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.setActiveDuringMaintenance(activeDuringMaintenance);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void forceLowPowerStandbyActive(boolean active) {
            if (com.android.server.power.PowerManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.DEVICE_POWER") != 0) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_LOW_POWER_STANDBY", "forceLowPowerStandbyActive");
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.forceActive(active);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setLowPowerStandbyPolicy(android.os.IPowerManager.LowPowerStandbyPolicy policy) {
            if (com.android.server.power.PowerManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.DEVICE_POWER") != 0) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_LOW_POWER_STANDBY", "setLowPowerStandbyPolicy");
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.setPolicy(android.os.PowerManager.LowPowerStandbyPolicy.fromParcelable(policy));
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public android.os.IPowerManager.LowPowerStandbyPolicy getLowPowerStandbyPolicy() {
            if (com.android.server.power.PowerManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.DEVICE_POWER") != 0) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_LOW_POWER_STANDBY", "getLowPowerStandbyPolicy");
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return android.os.PowerManager.LowPowerStandbyPolicy.toParcelable(com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.getPolicy());
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isExemptFromLowPowerStandby() {
            int callingUid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.isPackageExempt(callingUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isReasonAllowedInLowPowerStandby(int reason) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.isAllowed(reason);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isFeatureAllowedInLowPowerStandby(java.lang.String feature) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.isAllowed(feature);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void acquireLowPowerStandbyPorts(android.os.IBinder token, java.util.List<android.os.IPowerManager.LowPowerStandbyPortDescription> ports) {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.SET_LOW_POWER_STANDBY_PORTS", "acquireLowPowerStandbyPorts");
            int callingUid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.acquireStandbyPorts(token, callingUid, android.os.PowerManager.LowPowerStandbyPortDescription.fromParcelable(ports));
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void releaseLowPowerStandbyPorts(android.os.IBinder token) {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.SET_LOW_POWER_STANDBY_PORTS", "releaseLowPowerStandbyPorts");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.releaseStandbyPorts(token);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public java.util.List<android.os.IPowerManager.LowPowerStandbyPortDescription> getActiveLowPowerStandbyPorts() {
            if (com.android.server.power.PowerManagerService.this.mContext.checkCallingOrSelfPermission("android.permission.DEVICE_POWER") != 0) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_LOW_POWER_STANDBY", "getActiveLowPowerStandbyPorts");
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return android.os.PowerManager.LowPowerStandbyPortDescription.toParcelable(com.android.server.power.PowerManagerService.this.mLowPowerStandbyController.getActiveStandbyPorts());
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public int getLastShutdownReason() {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.getLastShutdownReasonInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public int getLastSleepReason() {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.getLastSleepReasonInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void reboot(boolean confirm, java.lang.String reason, boolean wait) {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.REBOOT", null);
            if ("recovery".equals(reason) || "recovery-update".equals(reason)) {
                com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.RECOVERY", null);
            }
            com.android.server.power.ShutdownCheckPoints.recordCheckPoint(android.os.Binder.getCallingPid(), reason);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.shutdownOrRebootInternal(1, confirm, reason, wait);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void rebootSafeMode(boolean confirm, boolean wait) {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.REBOOT", null);
            com.android.server.power.ShutdownCheckPoints.recordCheckPoint(android.os.Binder.getCallingPid(), "safemode");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.shutdownOrRebootInternal(2, confirm, "safemode", wait);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void shutdown(boolean confirm, java.lang.String reason, boolean wait) {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.REBOOT", null);
            com.android.server.power.ShutdownCheckPoints.recordCheckPoint(android.os.Binder.getCallingPid(), reason);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.shutdownOrRebootInternal(0, confirm, reason, wait);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void crash(java.lang.String message) {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.REBOOT", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.crashInternal(message);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setStayOnSetting(int val) {
            int uid = android.os.Binder.getCallingUid();
            if (uid != 0 && !android.provider.Settings.checkAndNoteWriteSettingsOperation(com.android.server.power.PowerManagerService.this.mContext, uid, android.provider.Settings.getPackageNameForUid(com.android.server.power.PowerManagerService.this.mContext, uid), null, true)) {
                return;
            }
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.setStayOnSettingInternal(val);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setAttentionLight(boolean on, int color) {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.setAttentionLightInternal(on, color);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setDozeAfterScreenOff(boolean on) {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.setDozeAfterScreenOffInternal(on);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isAmbientDisplayAvailable() {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.READ_DREAM_STATE", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.mAmbientDisplayConfiguration.ambientDisplayAvailable();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void suppressAmbientDisplay(java.lang.String token, boolean suppress) {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.WRITE_DREAM_STATE", null);
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.mAmbientDisplaySuppressionController.suppress(token, uid, suppress);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isAmbientDisplaySuppressedForToken(java.lang.String token) {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.READ_DREAM_STATE", null);
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.mAmbientDisplaySuppressionController.isSuppressed(token, uid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x002f  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean isAmbientDisplaySuppressedForTokenByApp(java.lang.String r4, int r5) {
            /*
                r3 = this;
                com.android.server.power.PowerManagerService r0 = com.android.server.power.PowerManagerService.this
                android.content.Context r0 = com.android.server.power.PowerManagerService.m8270$$Nest$fgetmContext(r0)
                java.lang.String r1 = "android.permission.READ_DREAM_STATE"
                r2 = 0
                r0.enforceCallingOrSelfPermission(r1, r2)
                com.android.server.power.PowerManagerService r0 = com.android.server.power.PowerManagerService.this
                android.content.Context r0 = com.android.server.power.PowerManagerService.m8270$$Nest$fgetmContext(r0)
                java.lang.String r1 = "android.permission.READ_DREAM_SUPPRESSION"
                r0.enforceCallingOrSelfPermission(r1, r2)
                long r0 = android.os.Binder.clearCallingIdentity()
                boolean r2 = r3.isAmbientDisplayAvailable()     // Catch: java.lang.Throwable -> L34
                if (r2 == 0) goto L2f
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L34
                com.android.server.power.AmbientDisplaySuppressionController r2 = com.android.server.power.PowerManagerService.m8263$$Nest$fgetmAmbientDisplaySuppressionController(r2)     // Catch: java.lang.Throwable -> L34
                boolean r2 = r2.isSuppressed(r4, r5)     // Catch: java.lang.Throwable -> L34
                if (r2 == 0) goto L2f
                r2 = 1
                goto L30
            L2f:
                r2 = 0
            L30:
                android.os.Binder.restoreCallingIdentity(r0)
                return r2
            L34:
                r2 = move-exception
                android.os.Binder.restoreCallingIdentity(r0)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.PowerManagerService.BinderService.isAmbientDisplaySuppressedForTokenByApp(java.lang.String, int):boolean");
        }

        public boolean isAmbientDisplaySuppressed() {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.READ_DREAM_STATE", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.mAmbientDisplaySuppressionController.isSuppressed();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void boostScreenBrightness(long eventTime) {
            long now = com.android.server.power.PowerManagerService.this.mClock.uptimeMillis();
            if (eventTime > com.android.server.power.PowerManagerService.this.mClock.uptimeMillis()) {
                android.util.Slog.e(com.android.server.power.PowerManagerService.TAG, "Event time " + eventTime + " cannot be newer than " + now);
                throw new java.lang.IllegalArgumentException("event time must not be in the future");
            }
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.boostScreenBrightnessInternal(eventTime, uid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean isScreenBrightnessBoosted() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.isScreenBrightnessBoostedInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public boolean forceSuspend() {
            com.android.server.power.PowerManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.forceSuspendInternal(uid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        @java.lang.Deprecated
        public int getMinimumScreenBrightnessSetting() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return (int) com.android.server.power.PowerManagerService.this.mScreenBrightnessSettingMinimum;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        @java.lang.Deprecated
        public int getMaximumScreenBrightnessSetting() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return (int) com.android.server.power.PowerManagerService.this.mScreenBrightnessSettingMaximum;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        @java.lang.Deprecated
        public int getDefaultScreenBrightnessSetting() {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return (int) com.android.server.power.PowerManagerService.this.mScreenBrightnessSettingDefault;
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:9:0x001d A[Catch: all -> 0x0031, TryCatch #0 {all -> 0x0031, blocks: (B:3:0x0004, B:5:0x000c, B:7:0x0014, B:10:0x001f, B:9:0x001d), top: B:16:0x0004 }] */
        @java.lang.Deprecated
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void setFlashing(int r4, int r5, int r6, int r7, int r8) {
            /*
                r3 = this;
                long r0 = android.os.Binder.clearCallingIdentity()
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L31
                com.android.server.power.PowerManagerService.m8291$$Nest$fgetmLightsManager(r2)     // Catch: java.lang.Throwable -> L31
                r2 = 6
                if (r4 == r2) goto L1d
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L31
                com.android.server.power.PowerManagerService.m8291$$Nest$fgetmLightsManager(r2)     // Catch: java.lang.Throwable -> L31
                r2 = 7
                if (r4 == r2) goto L1d
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L31
                com.android.server.power.PowerManagerService.m8291$$Nest$fgetmLightsManager(r2)     // Catch: java.lang.Throwable -> L31
                r2 = 8
                if (r4 != r2) goto L1f
            L1d:
                int r4 = r4 + (-1)
            L1f:
                com.android.server.power.PowerManagerService r2 = com.android.server.power.PowerManagerService.this     // Catch: java.lang.Throwable -> L31
                com.android.server.lights.LightsManager r2 = com.android.server.power.PowerManagerService.m8291$$Nest$fgetmLightsManager(r2)     // Catch: java.lang.Throwable -> L31
                com.android.server.lights.LogicalLight r2 = r2.getLight(r4)     // Catch: java.lang.Throwable -> L31
                r2.setFlashing(r5, r8, r6, r7)     // Catch: java.lang.Throwable -> L31
                android.os.Binder.restoreCallingIdentity(r0)
                return
            L31:
                r2 = move-exception
                android.os.Binder.restoreCallingIdentity(r0)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.PowerManagerService.BinderService.setFlashing(int, int, int, int, int):void");
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.power.PowerManagerService.this.mContext, com.android.server.power.PowerManagerService.TAG, pw)) {
                long ident = android.os.Binder.clearCallingIdentity();
                boolean isDumpProto = false;
                int length = args.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    java.lang.String arg = args[i];
                    if (!arg.equals("--proto")) {
                        i++;
                    } else {
                        isDumpProto = true;
                        break;
                    }
                }
                if (com.android.server.power.PowerManagerService.mPmsExt.dump(fd, pw, args)) {
                    return;
                }
                try {
                    if (isDumpProto) {
                        com.android.server.power.PowerManagerService.this.dumpProto(fd);
                    } else {
                        com.android.server.power.PowerManagerService.this.dumpInternal(pw);
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }

        public java.util.List<java.lang.String> getAmbientDisplaySuppressionTokens() {
            int uid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.power.PowerManagerService.this.mAmbientDisplaySuppressionController.getSuppressionTokens(uid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void setUseFaceDownDetector(boolean enable) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.power.PowerManagerService.this.mFaceDownDetector.setEnabledOverride(enable);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    com.android.server.power.PowerManagerService.BinderService getBinderServiceInstance() {
        return this.mBinderService;
    }

    com.android.server.power.PowerManagerService.LocalService getLocalServiceInstance() {
        return this.mLocalService;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x005a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int getLastShutdownReasonInternal() {
        /*
            r8 = this;
            com.android.server.power.SystemPropertiesWrapper r0 = r8.mSystemProperties
            java.lang.String r1 = "sys.boot.reason"
            r2 = 0
            java.lang.String r0 = r0.get(r1, r2)
            int r1 = r0.hashCode()
            r2 = 0
            r3 = 5
            r4 = 4
            r5 = 3
            r6 = 2
            r7 = 1
            switch(r1) {
                case -2117951935: goto L4f;
                case -1099647817: goto L44;
                case -934938715: goto L39;
                case -852189395: goto L2e;
                case -169343402: goto L23;
                case 1218064802: goto L18;
                default: goto L17;
            }
        L17:
            goto L5a
        L18:
            java.lang.String r1 = "shutdown,thermal,battery"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L17
            r1 = r3
            goto L5b
        L23:
            java.lang.String r1 = "shutdown"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L17
            r1 = r2
            goto L5b
        L2e:
            java.lang.String r1 = "shutdown,userrequested"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L17
            r1 = r6
            goto L5b
        L39:
            java.lang.String r1 = "reboot"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L17
            r1 = r7
            goto L5b
        L44:
            java.lang.String r1 = "shutdown,battery"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L17
            r1 = r4
            goto L5b
        L4f:
            java.lang.String r1 = "shutdown,thermal"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L17
            r1 = r5
            goto L5b
        L5a:
            r1 = -1
        L5b:
            switch(r1) {
                case 0: goto L65;
                case 1: goto L64;
                case 2: goto L63;
                case 3: goto L62;
                case 4: goto L61;
                case 5: goto L5f;
                default: goto L5e;
            }
        L5e:
            return r2
        L5f:
            r1 = 6
            return r1
        L61:
            return r3
        L62:
            return r4
        L63:
            return r5
        L64:
            return r6
        L65:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.PowerManagerService.getLastShutdownReasonInternal():int");
    }

    int getPowerGroupSize() {
        int size;
        synchronized (this.mLock) {
            size = this.mPowerGroups.size();
        }
        return size;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getLastSleepReasonInternal() {
        int i;
        synchronized (this.mLock) {
            i = this.mLastGlobalSleepReason;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.PowerManager.WakeData getLastWakeupInternal() {
        android.os.PowerManager.WakeData wakeData;
        synchronized (this.mLock) {
            wakeData = new android.os.PowerManager.WakeData(this.mLastGlobalWakeTime, this.mLastGlobalWakeReason, this.mLastGlobalWakeTimeRealtime - this.mLastGlobalSleepTimeRealtime);
        }
        return wakeData;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.PowerManager.SleepData getLastGoToSleepInternal() {
        android.os.PowerManager.SleepData sleepData;
        synchronized (this.mLock) {
            sleepData = new android.os.PowerManager.SleepData(this.mLastGlobalSleepTime, this.mLastGlobalSleepReason);
        }
        return sleepData;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean interceptPowerKeyDownInternal(android.view.KeyEvent event) {
        synchronized (this.mLock) {
            if (!mPmsExt.isBeingKeptAwakeLocked(0, this.mProximityPositive) || this.mInterceptedPowerKeyForProximity) {
                return false;
            }
            this.mDisplayManagerInternal.ignoreProximitySensorUntilChanged();
            this.mInterceptedPowerKeyForProximity = true;
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goToSleepInternal(android.util.IntArray groupIds, long eventTime, int reason, int flags) {
        java.lang.Object obj;
        com.android.server.power.PowerManagerService powerManagerService = this;
        long now = powerManagerService.mClock.uptimeMillis();
        if (eventTime > now) {
            android.util.Slog.e(TAG, "Event time " + eventTime + " cannot be newer than " + now);
            throw new java.lang.IllegalArgumentException("event time must not be in the future");
        }
        powerManagerService.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
        boolean isNoDoze = (flags & 1) != 0;
        int uid = android.os.Binder.getCallingUid();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.Object obj2 = powerManagerService.mLock;
            synchronized (obj2) {
                int i = 0;
                while (i < groupIds.size()) {
                    try {
                        int groupId = groupIds.get(i);
                        com.android.server.power.PowerGroup powerGroup = powerManagerService.mPowerGroups.get(groupId);
                        if (powerGroup == null) {
                            throw new java.lang.IllegalArgumentException("power group(" + groupId + ") doesn't exist");
                        }
                        if ((flags & 2) != 0) {
                            if (powerManagerService.mFoldGracePeriodProvider.isEnabled()) {
                                if (powerGroup.hasWakeLockKeepingScreenOnLocked()) {
                                    obj = obj2;
                                } else {
                                    powerManagerService.mNotifier.showDismissibleKeyguard();
                                    obj = obj2;
                                }
                            } else if (powerGroup.hasWakeLockKeepingScreenOnLocked()) {
                                obj = obj2;
                            }
                            i++;
                            powerManagerService = this;
                            obj2 = obj;
                        }
                        if (isNoDoze) {
                            obj = obj2;
                            try {
                                sleepPowerGroupLocked(powerGroup, eventTime, reason, uid);
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        } else {
                            obj = obj2;
                            dozePowerGroupLocked(powerGroup, eventTime, reason, uid);
                        }
                        i++;
                        powerManagerService = this;
                        obj2 = obj;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        obj = obj2;
                    }
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    final class LocalService extends android.os.PowerManagerInternal {
        LocalService() {
        }

        public void setScreenBrightnessOverrideFromWindowManager(float screenBrightness) {
            if (screenBrightness < 0.0f || screenBrightness > 1.0f) {
                screenBrightness = Float.NaN;
            }
            com.android.server.power.PowerManagerService.this.setScreenBrightnessOverrideFromWindowManagerInternal(screenBrightness);
        }

        public void setDozeOverrideFromDreamManager(int screenState, int reason, int screenBrightness) {
            switch (screenState) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 6:
                    break;
                case 5:
                default:
                    screenState = 0;
                    break;
            }
            if (screenBrightness < -1 || screenBrightness > 255) {
                screenBrightness = -1;
            }
            com.android.server.power.PowerManagerService.mPmsExt.setDozeOverrideFromDreamManager(screenState, screenBrightness);
            com.android.server.power.PowerManagerService.this.setDozeOverrideFromDreamManagerInternal(screenState, reason, screenBrightness);
        }

        public void setUserInactiveOverrideFromWindowManager() {
            com.android.server.power.PowerManagerService.this.setUserInactiveOverrideFromWindowManagerInternal();
        }

        public void setUserActivityTimeoutOverrideFromWindowManager(long timeoutMillis) {
            com.android.server.power.PowerManagerService.this.setUserActivityTimeoutOverrideFromWindowManagerInternal(timeoutMillis);
        }

        public void setDrawWakeLockOverrideFromSidekick(boolean keepState) {
            com.android.server.power.PowerManagerService.this.setDrawWakeLockOverrideFromSidekickInternal(keepState);
        }

        public void setMaximumScreenOffTimeoutFromDeviceAdmin(int userId, long timeMs) {
            com.android.server.power.PowerManagerService.this.setMaximumScreenOffTimeoutFromDeviceAdminInternal(userId, timeMs);
        }

        public android.os.PowerSaveState getLowPowerState(int serviceType) {
            if (com.android.server.power.PowerManagerService.this.mBatterySaverSupported) {
                return com.android.server.power.PowerManagerService.this.mBatterySaverStateMachine.getBatterySaverPolicy().getBatterySaverPolicy(serviceType);
            }
            return new android.os.PowerSaveState.Builder().build();
        }

        public void registerLowPowerModeObserver(android.os.PowerManagerInternal.LowPowerModeListener listener) {
            if (com.android.server.power.PowerManagerService.this.mBatterySaverSupported) {
                com.android.server.power.PowerManagerService.this.mBatterySaverStateMachine.getBatterySaverController().addListener(listener);
            } else {
                android.util.Slog.w(com.android.server.power.PowerManagerService.TAG, "Battery saver is not supported, no low power mode observer registered");
            }
        }

        public boolean setDeviceIdleMode(boolean enabled) {
            return com.android.server.power.PowerManagerService.this.setDeviceIdleModeInternal(enabled);
        }

        public boolean setLightDeviceIdleMode(boolean enabled) {
            return com.android.server.power.PowerManagerService.this.setLightDeviceIdleModeInternal(enabled);
        }

        public void setDeviceIdleWhitelist(int[] appids) {
            com.android.server.power.PowerManagerService.this.setDeviceIdleWhitelistInternal(appids);
        }

        public void setDeviceIdleTempWhitelist(int[] appids) {
            com.android.server.power.PowerManagerService.this.setDeviceIdleTempWhitelistInternal(appids);
        }

        public void setLowPowerStandbyAllowlist(int[] appids) {
            com.android.server.power.PowerManagerService.this.setLowPowerStandbyAllowlistInternal(appids);
        }

        public void setLowPowerStandbyActive(boolean enabled) {
            com.android.server.power.PowerManagerService.this.setLowPowerStandbyActiveInternal(enabled);
        }

        public void startUidChanges() {
            com.android.server.power.PowerManagerService.this.startUidChangesInternal();
        }

        public void finishUidChanges() {
            com.android.server.power.PowerManagerService.this.finishUidChangesInternal();
        }

        public void updateUidProcState(int uid, int procState) {
            com.android.server.power.PowerManagerService.this.updateUidProcStateInternal(uid, procState);
        }

        public void uidGone(int uid) {
            com.android.server.power.PowerManagerService.this.uidGoneInternal(uid);
        }

        public void uidActive(int uid) {
            com.android.server.power.PowerManagerService.this.uidActiveInternal(uid);
        }

        public void uidIdle(int uid) {
            com.android.server.power.PowerManagerService.this.uidIdleInternal(uid);
        }

        public void setPowerBoost(int boost, int durationMs) {
            com.android.server.power.PowerManagerService.this.setPowerBoostInternal(boost, durationMs);
        }

        public void setPowerMode(int mode, boolean enabled) {
            com.android.server.power.PowerManagerService.this.setPowerModeInternal(mode, enabled);
        }

        public boolean wasDeviceIdleFor(long ms) {
            return com.android.server.power.PowerManagerService.this.wasDeviceIdleForInternal(ms);
        }

        public android.os.PowerManager.WakeData getLastWakeup() {
            return com.android.server.power.PowerManagerService.this.getLastWakeupInternal();
        }

        public android.os.PowerManager.SleepData getLastGoToSleep() {
            return com.android.server.power.PowerManagerService.this.getLastGoToSleepInternal();
        }

        public boolean interceptPowerKeyDown(android.view.KeyEvent event) {
            return com.android.server.power.PowerManagerService.this.interceptPowerKeyDownInternal(event);
        }

        public void nap(long eventTime, boolean allowWake) {
            com.android.server.power.PowerManagerService.this.napInternal(eventTime, 1000, allowWake);
        }

        public boolean isAmbientDisplaySuppressed() {
            return com.android.server.power.PowerManagerService.this.mAmbientDisplaySuppressionController.isSuppressed();
        }
    }

    class DeviceStateListener implements android.hardware.devicestate.DeviceStateManager.DeviceStateCallback {
        private int mDeviceState = -1;

        DeviceStateListener() {
        }

        public void onDeviceStateChanged(android.hardware.devicestate.DeviceState deviceState) {
            int stateIdentifier = deviceState.getIdentifier();
            if (this.mDeviceState != stateIdentifier) {
                this.mDeviceState = stateIdentifier;
                com.android.server.power.PowerManagerService.mPmsExt.setDeviceState(this.mDeviceState);
                com.android.server.power.PowerManagerService.this.userActivityInternal(0, com.android.server.power.PowerManagerService.this.mClock.uptimeMillis(), 6, 0, 1000);
            }
        }
    }

    static boolean isSameCallback(android.os.IWakeLockCallback callback1, android.os.IWakeLockCallback callback2) {
        if (callback1 == callback2) {
            return true;
        }
        if (callback1 != null && callback2 != null && callback1.asBinder() == callback2.asBinder()) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseAllOverrideWakeLocks(int releaseReason) {
        synchronized (this.mLock) {
            int size = this.mWakeLocks.size();
            boolean change = false;
            for (int i = size - 1; i >= 0; i--) {
                com.android.server.power.PowerManagerService.WakeLock wakeLock = this.mWakeLocks.get(i);
                if ((wakeLock.mFlags & 65535) == 256) {
                    removeWakeLockNoUpdateLocked(wakeLock, i, releaseReason);
                    change = true;
                }
            }
            if (change) {
                updatePowerStateLocked();
            }
        }
    }

    public com.android.server.power.IPowerManagerServiceWrapper getWrapper() {
        return this.mPmsWrapper;
    }

    private class PowerManagerServiceWrapper implements com.android.server.power.IPowerManagerServiceWrapper {
        private PowerManagerServiceWrapper() {
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public java.lang.Object getLock() {
            return com.android.server.power.PowerManagerService.this.mLock;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public com.android.server.power.IPowerManagerServiceExt getPmsExt() {
            return com.android.server.power.PowerManagerService.mPmsExt;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public int getDozeScreenStateOverrideFromDreamManager() {
            int i;
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                i = com.android.server.power.PowerManagerService.this.mDozeScreenStateOverrideFromDreamManager;
            }
            return i;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public float getScreenBrightnessSettingMinimum() {
            return com.android.server.power.PowerManagerService.this.mScreenBrightnessSettingMinimum;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setScreenBrightnessSettingMinimum(float value) {
            com.android.server.power.PowerManagerService.this.mScreenBrightnessSettingMinimum = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public float getScreenBrightnessSettingMaximum() {
            return com.android.server.power.PowerManagerService.this.mScreenBrightnessSettingMaximum;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setScreenBrightnessSettingMaximum(float value) {
            com.android.server.power.PowerManagerService.this.mScreenBrightnessSettingMaximum = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public float getScreenBrightnessSettingDefault() {
            return com.android.server.power.PowerManagerService.this.mScreenBrightnessSettingDefault;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setScreenBrightnessSettingDefault(float value) {
            com.android.server.power.PowerManagerService.this.mScreenBrightnessSettingDefault = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public com.android.server.lights.LightsManager getLightsManager() {
            return com.android.server.power.PowerManagerService.this.mLightsManager;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public boolean getProximityPositive() {
            return com.android.server.power.PowerManagerService.this.mProximityPositive;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setProximityPositive(boolean value) {
            com.android.server.power.PowerManagerService.this.mProximityPositive = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setInterceptedPowerKeyForProximity(boolean value) {
            synchronized (com.android.server.power.PowerManagerService.this.mLock) {
                com.android.server.power.PowerManagerService.this.mInterceptedPowerKeyForProximity = value;
            }
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public boolean getDreamsEnabledOnBatteryConfig() {
            return com.android.server.power.PowerManagerService.this.mDreamsEnabledOnBatteryConfig;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setDreamsEnabledOnBatteryConfig(boolean value) {
            com.android.server.power.PowerManagerService.this.mDreamsEnabledOnBatteryConfig = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public int getDreamsBatteryLevelMinimumWhenNotPoweredConfig() {
            return com.android.server.power.PowerManagerService.this.mDreamsBatteryLevelMinimumWhenNotPoweredConfig;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setDreamsBatteryLevelMinimumWhenNotPoweredConfig(int value) {
            com.android.server.power.PowerManagerService.this.mDreamsBatteryLevelMinimumWhenNotPoweredConfig = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public int getMSG_SCREEN_BRIGHTNESS_BOOST_TIMEOUT() {
            return 3;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public int getMSG_USER_ACTIVITY_TIMEOUT() {
            return 1;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public android.os.Handler getHandler() {
            return com.android.server.power.PowerManagerService.this.mHandler;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public com.android.server.ServiceThread getHandlerThread() {
            return com.android.server.power.PowerManagerService.this.mHandlerThread;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public float getScreenBrightnessOverrideFromWindowManager() {
            return com.android.server.power.PowerManagerService.this.mScreenBrightnessOverrideFromWindowManager;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setScreenBrightnessOverrideFromWindowManager(float value) {
            com.android.server.power.PowerManagerService.this.mScreenBrightnessOverrideFromWindowManager = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public int getWakeLockSummary() {
            return com.android.server.power.PowerManagerService.this.mWakeLockSummary;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public long getUserActivityTimeoutOverrideFromWindowManager() {
            return com.android.server.power.PowerManagerService.this.mUserActivityTimeoutOverrideFromWindowManager;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setUserActivityTimeoutOverrideFromWindowManager(long value) {
            com.android.server.power.PowerManagerService.this.mUserActivityTimeoutOverrideFromWindowManager = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public long getScreenOffTimeoutSetting() {
            return com.android.server.power.PowerManagerService.this.mScreenOffTimeoutSetting;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public java.util.ArrayList<com.android.server.power.PowerManagerService.WakeLock> getWakeLocks() {
            return com.android.server.power.PowerManagerService.this.mWakeLocks;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public int getDirty() {
            return com.android.server.power.PowerManagerService.this.mDirty;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setDirty(int value) {
            com.android.server.power.PowerManagerService.this.mDirty = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public int getDIRTY_WAKE_LOCKS() {
            return 1;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public int getDIRTY_USER_ACTIVITY() {
            return 4;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public android.util.SparseArray<com.android.server.power.PowerManagerService.UidState> getUidState() {
            return com.android.server.power.PowerManagerService.this.mUidState;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public android.util.SparseArray<com.android.server.power.PowerGroup> getPowerGroups() {
            return com.android.server.power.PowerManagerService.this.mPowerGroups;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public android.service.dreams.DreamManagerInternal getDreamManager() {
            return com.android.server.power.PowerManagerService.this.mDreamManager;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public boolean getBootCompleted() {
            return com.android.server.power.PowerManagerService.this.mBootCompleted;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setDozeAfterScreenOff(boolean value) {
            com.android.server.power.PowerManagerService.this.mDozeAfterScreenOff = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setDecoupleHalAutoSuspendModeFromDisplayConfig(boolean value) {
            com.android.server.power.PowerManagerService.this.mDecoupleHalAutoSuspendModeFromDisplayConfig = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setDecoupleHalInteractiveModeFromDisplayConfig(boolean value) {
            com.android.server.power.PowerManagerService.this.mDecoupleHalInteractiveModeFromDisplayConfig = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setDreamsActivateOnSleepSetting(boolean value) {
            com.android.server.power.PowerManagerService.this.mDreamsActivateOnSleepSetting = value;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void updatePowerStateLocked() {
            com.android.server.power.PowerManagerService.this.updatePowerStateLocked();
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public boolean userActivityNoUpdateLocked(long eventTime, int event, int flags, int uid) {
            return com.android.server.power.PowerManagerService.this.userActivityNoUpdateLocked(eventTime, event, flags, uid);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public boolean isInteractiveInternal() {
            return com.android.server.power.PowerManagerService.this.isGloballyInteractiveInternal();
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public long getAttentiveTimeoutLocked() {
            return com.android.server.power.PowerManagerService.this.getAttentiveTimeoutLocked();
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public long getSleepTimeoutLocked(long attentiveTimeout) {
            return com.android.server.power.PowerManagerService.this.getSleepTimeoutLocked(attentiveTimeout);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public long getScreenOffTimeoutLocked(long sleepTimeout, long attentiveTimeout) {
            return com.android.server.power.PowerManagerService.this.getScreenOffTimeoutLocked(sleepTimeout, attentiveTimeout);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public boolean setWakeLockDisabledStateLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
            return com.android.server.power.PowerManagerService.this.setWakeLockDisabledStateLocked(wakeLock);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void notifyWakeLockReleasedLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
            com.android.server.power.PowerManagerService.this.notifyWakeLockReleasedLocked(wakeLock);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void notifyWakeLockAcquiredLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock) {
            com.android.server.power.PowerManagerService.this.notifyWakeLockAcquiredLocked(wakeLock);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void releaseWakeLockInternal(android.os.IBinder lock, int flags) {
            com.android.server.power.PowerManagerService.this.releaseWakeLockInternal(lock, flags);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void userActivityInternal(int displayId, long eventTime, int event, int flags, int uid) {
            com.android.server.power.PowerManagerService.this.userActivityInternal(displayId, eventTime, event, flags, uid);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void notifyWakeLockChangingLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, int flags, java.lang.String tag, java.lang.String packageName, int uid, int pid, android.os.WorkSource ws, java.lang.String historyTag, android.os.IWakeLockCallback callback) {
            com.android.server.power.PowerManagerService.this.notifyWakeLockChangingLocked(wakeLock, flags, tag, packageName, uid, pid, ws, historyTag, callback);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public int findWakeLockIndexLocked(android.os.IBinder lock) {
            return com.android.server.power.PowerManagerService.this.findWakeLockIndexLocked(lock);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void applyWakeLockFlagsOnAcquireLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, boolean isCallerPrivileged) throws java.lang.Throwable {
            com.android.server.power.PowerManagerService.this.applyWakeLockFlagsOnAcquireLocked(wakeLock);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void removeWakeLockLocked(com.android.server.power.PowerManagerService.WakeLock wakeLock, int index) {
            com.android.server.power.PowerManagerService.this.removeWakeLockLocked(wakeLock, index);
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public void setRequestWaitForNegativeProximity(boolean requestWaitForNegativeProximity) {
            com.android.server.power.PowerManagerService.this.mRequestWaitForNegativeProximity = requestWaitForNegativeProximity;
        }

        @Override // com.android.server.power.IPowerManagerServiceWrapper
        public int getBatteryLevel() {
            return com.android.server.power.PowerManagerService.this.mBatteryLevel;
        }
    }
}
