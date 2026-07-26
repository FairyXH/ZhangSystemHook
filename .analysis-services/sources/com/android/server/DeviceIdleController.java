package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class DeviceIdleController extends com.android.server.SystemService implements com.android.server.AnyMotionDetector.DeviceIdleCallback {
    private static final int ACTIVE_REASON_ALARM = 7;
    private static final int ACTIVE_REASON_CHARGING = 3;
    private static final int ACTIVE_REASON_EMERGENCY_CALL = 8;
    private static final int ACTIVE_REASON_FORCED = 6;
    private static final int ACTIVE_REASON_FROM_BINDER_CALL = 5;
    private static final int ACTIVE_REASON_MODE_MANAGER = 9;
    private static final int ACTIVE_REASON_MOTION = 1;
    private static final int ACTIVE_REASON_ONBODY = 10;
    private static final int ACTIVE_REASON_SCREEN = 2;
    private static final int ACTIVE_REASON_UNKNOWN = 0;
    private static final int ACTIVE_REASON_UNLOCKED = 4;
    private static final boolean COMPRESS_TIME = false;
    private static final boolean DEBUG = false;
    private static final int EVENT_BUFFER_SIZE = 100;
    private static final int EVENT_DEEP_IDLE = 4;
    private static final int EVENT_DEEP_MAINTENANCE = 5;
    private static final int EVENT_LIGHT_IDLE = 2;
    private static final int EVENT_LIGHT_MAINTENANCE = 3;
    private static final int EVENT_NORMAL = 1;
    private static final int EVENT_NULL = 0;
    static final int LIGHT_STATE_ACTIVE = 0;
    static final int LIGHT_STATE_IDLE = 4;
    static final int LIGHT_STATE_IDLE_MAINTENANCE = 6;
    static final int LIGHT_STATE_INACTIVE = 1;
    static final int LIGHT_STATE_OVERRIDE = 7;
    static final int LIGHT_STATE_WAITING_FOR_NETWORK = 5;
    private static final int MSG_FINISH_IDLE_OP = 8;
    private static final int MSG_REPORT_ACTIVE = 5;
    private static final int MSG_REPORT_IDLE_OFF = 4;
    private static final int MSG_REPORT_IDLE_ON = 2;
    private static final int MSG_REPORT_IDLE_ON_LIGHT = 3;
    static final int MSG_REPORT_STATIONARY_STATUS = 7;
    private static final int MSG_REPORT_TEMP_APP_WHITELIST_ADDED_TO_NPMS = 14;
    private static final int MSG_REPORT_TEMP_APP_WHITELIST_CHANGED = 13;
    private static final int MSG_REPORT_TEMP_APP_WHITELIST_REMOVED_TO_NPMS = 15;
    private static final int MSG_SEND_CONSTRAINT_MONITORING = 10;
    private static final int MSG_TEMP_APP_WHITELIST_TIMEOUT = 6;
    private static final int MSG_WRITE_CONFIG = 1;
    static final int STATE_ACTIVE = 0;
    static final int STATE_IDLE = 5;
    static final int STATE_IDLE_MAINTENANCE = 6;
    static final int STATE_IDLE_PENDING = 2;
    static final int STATE_INACTIVE = 1;
    static final int STATE_LOCATING = 4;
    static final int STATE_QUICK_DOZE_DELAY = 7;
    static final int STATE_SENSING = 3;
    private static final java.lang.String TAG = "DeviceIdleController";
    private static final java.lang.String USER_ALLOWLIST_ADDITION_METRIC_ID = "battery.value_app_added_to_power_allowlist";
    private static final java.lang.String USER_ALLOWLIST_REMOVAL_METRIC_ID = "battery.value_app_removed_from_power_allowlist";
    private int mActiveIdleOpCount;
    private android.os.PowerManager.WakeLock mActiveIdleWakeLock;
    private int mActiveReason;
    private android.app.AlarmManager mAlarmManager;
    private boolean mAlarmsActive;
    private com.android.server.AnyMotionDetector mAnyMotionDetector;
    private final com.android.server.AppStateTrackerImpl mAppStateTracker;
    private boolean mBatterySaverEnabled;
    private com.android.internal.app.IBatteryStats mBatteryStats;
    com.android.server.DeviceIdleController.BinderService mBinderService;
    private boolean mCharging;
    public final android.util.AtomicFile mConfigFile;
    private com.android.server.DeviceIdleController.Constants mConstants;
    private com.android.server.deviceidle.ConstraintController mConstraintController;
    private final android.util.ArrayMap<com.android.server.deviceidle.IDeviceIdleConstraint, com.android.server.deviceidle.DeviceIdleConstraintTracker> mConstraints;
    private long mCurLightIdleBudget;
    final android.app.AlarmManager.OnAlarmListener mDeepAlarmListener;
    private boolean mDeepEnabled;
    private com.android.server.DeviceIdleController.DeviceIdleControllerWrapper mDeviceIdleControllerWrapper;
    private com.android.server.IDeviceIdleControllerExt mDeviceIdleExt;
    private final com.android.server.DeviceIdleController.EmergencyCallListener mEmergencyCallListener;
    private final int[] mEventCmds;
    private final java.lang.String[] mEventReasons;
    private final long[] mEventTimes;
    private boolean mForceIdle;
    private boolean mForceModeManagerOffBodyState;
    private boolean mForceModeManagerQuickDozeRequest;
    private final android.location.LocationListener mGenericLocationListener;
    private android.os.PowerManager.WakeLock mGoingIdleWakeLock;
    private final android.location.LocationListener mGpsLocationListener;
    final com.android.server.DeviceIdleController.MyHandler mHandler;
    private boolean mHasFusedLocation;
    private boolean mHasGps;
    private android.content.Intent mIdleIntent;
    private android.os.Bundle mIdleIntentOptions;
    private final android.content.IIntentReceiver mIdleStartedDoneReceiver;
    private long mInactiveTimeout;
    private final com.android.server.DeviceIdleController.Injector mInjector;
    private final android.content.BroadcastReceiver mInteractivityReceiver;
    private final boolean mIsLocationPrefetchEnabled;
    private boolean mIsOffBody;
    private boolean mJobsActive;
    private android.location.Location mLastGenericLocation;
    private android.location.Location mLastGpsLocation;
    private long mLastMotionEventElapsed;
    private final android.app.AlarmManager.OnAlarmListener mLightAlarmListener;
    private boolean mLightEnabled;
    private android.content.Intent mLightIdleIntent;
    private android.os.Bundle mLightIdleIntentOptions;
    private int mLightState;
    private android.app.ActivityManagerInternal mLocalActivityManager;
    private com.android.server.wm.ActivityTaskManagerInternal mLocalActivityTaskManager;
    private com.android.server.AlarmManagerInternal mLocalAlarmManager;
    private android.os.PowerManagerInternal mLocalPowerManager;
    private com.android.server.DeviceIdleInternal mLocalService;
    private boolean mLocated;
    private boolean mLocating;
    private android.location.LocationRequest mLocationRequest;
    private long mMaintenanceStartTime;
    final com.android.server.DeviceIdleController.ModeManagerOffBodyStateConsumer mModeManagerOffBodyStateConsumer;
    final com.android.server.DeviceIdleController.ModeManagerQuickDozeRequestConsumer mModeManagerQuickDozeRequestConsumer;
    private boolean mModeManagerRequestedQuickDoze;
    final com.android.server.DeviceIdleController.MotionListener mMotionListener;
    private final android.app.AlarmManager.OnAlarmListener mMotionRegistrationAlarmListener;
    private android.hardware.Sensor mMotionSensor;
    private final android.app.AlarmManager.OnAlarmListener mMotionTimeoutAlarmListener;
    private boolean mNetworkConnected;
    private android.net.INetworkPolicyManager mNetworkPolicyManager;
    private com.android.server.net.NetworkPolicyManagerInternal mNetworkPolicyManagerInternal;
    private long mNextAlarmTime;
    private long mNextIdleDelay;
    private long mNextIdlePendingDelay;
    private long mNextLightAlarmTime;
    private long mNextLightIdleDelay;
    private long mNextLightIdleDelayFlex;
    private long mNextSensingTimeoutAlarmTime;
    private boolean mNotMoving;
    private int mNumBlockingConstraints;
    private android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private android.os.PowerManager mPowerManager;
    private android.os.Bundle mPowerSaveTempWhilelistChangedOptions;
    private android.content.Intent mPowerSaveTempWhitelistChangedIntent;
    private int[] mPowerSaveWhitelistAllAppIdArray;
    private final android.util.SparseBooleanArray mPowerSaveWhitelistAllAppIds;
    private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mPowerSaveWhitelistApps;
    private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mPowerSaveWhitelistAppsExceptIdle;
    private android.content.Intent mPowerSaveWhitelistChangedIntent;
    private android.os.Bundle mPowerSaveWhitelistChangedOptions;
    private int[] mPowerSaveWhitelistExceptIdleAppIdArray;
    private final android.util.SparseBooleanArray mPowerSaveWhitelistExceptIdleAppIds;
    private final android.util.SparseBooleanArray mPowerSaveWhitelistSystemAppIds;
    private final android.util.SparseBooleanArray mPowerSaveWhitelistSystemAppIdsExceptIdle;
    private int[] mPowerSaveWhitelistUserAppIdArray;
    private final android.util.SparseBooleanArray mPowerSaveWhitelistUserAppIds;
    private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mPowerSaveWhitelistUserApps;
    private final android.util.ArraySet<java.lang.String> mPowerSaveWhitelistUserAppsExceptIdle;
    private boolean mQuickDozeActivated;
    private boolean mQuickDozeActivatedWhileIdling;
    private final android.content.BroadcastReceiver mReceiver;
    private android.util.ArrayMap<java.lang.String, java.lang.Integer> mRemovedFromSystemWhitelistApps;
    private boolean mScreenLocked;
    private com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver mScreenObserver;
    private boolean mScreenOn;
    private final android.app.AlarmManager.OnAlarmListener mSensingTimeoutAlarmListener;
    private android.hardware.SensorManager mSensorManager;
    private int mState;
    private final android.util.ArraySet<com.android.server.DeviceIdleInternal.StationaryListener> mStationaryListeners;
    private final android.util.ArraySet<com.android.server.PowerAllowlistInternal.TempAllowlistChangeListener> mTempAllowlistChangeListeners;
    private int[] mTempWhitelistAppIdArray;
    private final android.util.SparseArray<android.util.Pair<android.util.MutableLong, java.lang.String>> mTempWhitelistAppIdEndTimes;
    private final boolean mUseMotionSensor;

    static java.lang.String stateToString(int state) {
        switch (state) {
            case 0:
                return "ACTIVE";
            case 1:
                return "INACTIVE";
            case 2:
                return "IDLE_PENDING";
            case 3:
                return "SENSING";
            case 4:
                return "LOCATING";
            case 5:
                return "IDLE";
            case 6:
                return "IDLE_MAINTENANCE";
            case 7:
                return "QUICK_DOZE_DELAY";
            default:
                return java.lang.Integer.toString(state);
        }
    }

    static java.lang.String lightStateToString(int state) {
        switch (state) {
            case 0:
                return "ACTIVE";
            case 1:
                return "INACTIVE";
            case 2:
            case 3:
            default:
                return java.lang.Integer.toString(state);
            case 4:
                return "IDLE";
            case 5:
                return "WAITING_FOR_NETWORK";
            case 6:
                return "IDLE_MAINTENANCE";
            case 7:
                return "OVERRIDE";
        }
    }

    private void addEvent(int cmd, java.lang.String reason) {
        if (this.mEventCmds[0] != cmd) {
            java.lang.System.arraycopy(this.mEventCmds, 0, this.mEventCmds, 1, 99);
            java.lang.System.arraycopy(this.mEventTimes, 0, this.mEventTimes, 1, 99);
            java.lang.System.arraycopy(this.mEventReasons, 0, this.mEventReasons, 1, 99);
            this.mEventCmds[0] = cmd;
            this.mEventTimes[0] = android.os.SystemClock.elapsedRealtime();
            this.mEventReasons[0] = reason;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        synchronized (this) {
            stepLightIdleStateLocked("s:alarm");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        synchronized (this) {
            if (this.mStationaryListeners.size() > 0) {
                startMonitoringMotionLocked();
                scheduleMotionTimeoutAlarmLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        synchronized (this) {
            if (!isStationaryLocked()) {
                android.util.Slog.w(TAG, "motion timeout went off and device isn't stationary");
            } else {
                postStationaryStatusUpdated();
            }
        }
    }

    private void postStationaryStatus(com.android.server.DeviceIdleInternal.StationaryListener listener) {
        this.mHandler.obtainMessage(7, listener).sendToTarget();
    }

    private void postStationaryStatusUpdated() {
        this.mHandler.sendEmptyMessage(7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isStationaryLocked() {
        long now = this.mInjector.getElapsedRealtime();
        return this.mMotionListener.active && now - java.lang.Math.max(this.mMotionListener.activatedTimeElapsed, this.mLastMotionEventElapsed) >= this.mConstants.MOTION_INACTIVE_TIMEOUT;
    }

    void registerStationaryListener(com.android.server.DeviceIdleInternal.StationaryListener listener) {
        synchronized (this) {
            if (this.mStationaryListeners.add(listener)) {
                postStationaryStatus(listener);
                if (this.mMotionListener.active) {
                    if (!isStationaryLocked() && this.mStationaryListeners.size() == 1) {
                        scheduleMotionTimeoutAlarmLocked();
                    }
                } else {
                    startMonitoringMotionLocked();
                    scheduleMotionTimeoutAlarmLocked();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterStationaryListener(com.android.server.DeviceIdleInternal.StationaryListener listener) {
        synchronized (this) {
            if (this.mStationaryListeners.remove(listener) && this.mStationaryListeners.size() == 0 && (this.mState == 0 || this.mState == 1 || this.mQuickDozeActivated)) {
                maybeStopMonitoringMotionLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerTempAllowlistChangeListener(com.android.server.PowerAllowlistInternal.TempAllowlistChangeListener listener) {
        synchronized (this) {
            this.mTempAllowlistChangeListeners.add(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterTempAllowlistChangeListener(com.android.server.PowerAllowlistInternal.TempAllowlistChangeListener listener) {
        synchronized (this) {
            this.mTempAllowlistChangeListeners.remove(listener);
        }
    }

    class ModeManagerQuickDozeRequestConsumer implements java.util.function.Consumer<java.lang.Boolean> {
        ModeManagerQuickDozeRequestConsumer() {
        }

        @Override // java.util.function.Consumer
        public void accept(java.lang.Boolean enabled) {
            android.util.Slog.i(com.android.server.DeviceIdleController.TAG, "Mode manager quick doze request: " + enabled);
            synchronized (com.android.server.DeviceIdleController.this) {
                if (!com.android.server.DeviceIdleController.this.mForceModeManagerQuickDozeRequest && com.android.server.DeviceIdleController.this.mModeManagerRequestedQuickDoze != enabled.booleanValue()) {
                    com.android.server.DeviceIdleController.this.mModeManagerRequestedQuickDoze = enabled.booleanValue();
                    onModeManagerRequestChangedLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onModeManagerRequestChangedLocked() {
            com.android.server.DeviceIdleController.this.maybeBecomeActiveOnModeManagerEventsLocked();
            com.android.server.DeviceIdleController.this.updateQuickDozeFlagLocked();
        }
    }

    class ModeManagerOffBodyStateConsumer implements java.util.function.Consumer<java.lang.Boolean> {
        ModeManagerOffBodyStateConsumer() {
        }

        @Override // java.util.function.Consumer
        public void accept(java.lang.Boolean isOffBody) {
            android.util.Slog.i(com.android.server.DeviceIdleController.TAG, "Offbody event from mode manager: " + isOffBody);
            synchronized (com.android.server.DeviceIdleController.this) {
                if (!com.android.server.DeviceIdleController.this.mForceModeManagerOffBodyState && com.android.server.DeviceIdleController.this.mIsOffBody != isOffBody.booleanValue()) {
                    com.android.server.DeviceIdleController.this.mIsOffBody = isOffBody.booleanValue();
                    onModeManagerOffBodyChangedLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onModeManagerOffBodyChangedLocked() {
            com.android.server.DeviceIdleController.this.maybeBecomeActiveOnModeManagerEventsLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeBecomeActiveOnModeManagerEventsLocked() {
        synchronized (this) {
            if (this.mQuickDozeActivated) {
                return;
            }
            if (!this.mIsOffBody && !this.mForceIdle) {
                this.mActiveReason = 10;
                becomeActiveLocked("on_body", android.os.Process.myUid());
            }
        }
    }

    final class MotionListener extends android.hardware.TriggerEventListener implements android.hardware.SensorEventListener {
        long activatedTimeElapsed;
        boolean active = false;

        MotionListener() {
        }

        public boolean isActive() {
            return this.active;
        }

        @Override // android.hardware.TriggerEventListener
        public void onTrigger(android.hardware.TriggerEvent event) {
            synchronized (com.android.server.DeviceIdleController.this) {
                this.active = false;
                com.android.server.DeviceIdleController.this.motionLocked();
            }
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(android.hardware.SensorEvent event) {
            synchronized (com.android.server.DeviceIdleController.this) {
                com.android.server.DeviceIdleController.this.mSensorManager.unregisterListener(this, com.android.server.DeviceIdleController.this.mMotionSensor);
                this.active = false;
                com.android.server.DeviceIdleController.this.motionLocked();
            }
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        }

        public boolean registerLocked() {
            boolean success;
            if (com.android.server.DeviceIdleController.this.mMotionSensor.getReportingMode() == 2) {
                success = com.android.server.DeviceIdleController.this.mSensorManager.requestTriggerSensor(com.android.server.DeviceIdleController.this.mMotionListener, com.android.server.DeviceIdleController.this.mMotionSensor);
            } else {
                success = com.android.server.DeviceIdleController.this.mSensorManager.registerListener(com.android.server.DeviceIdleController.this.mMotionListener, com.android.server.DeviceIdleController.this.mMotionSensor, 3);
            }
            if (success) {
                this.active = true;
                this.activatedTimeElapsed = com.android.server.DeviceIdleController.this.mInjector.getElapsedRealtime();
            } else {
                android.util.Slog.e(com.android.server.DeviceIdleController.TAG, "Unable to register for " + com.android.server.DeviceIdleController.this.mMotionSensor);
            }
            return success;
        }

        public void unregisterLocked() {
            if (com.android.server.DeviceIdleController.this.mMotionSensor.getReportingMode() == 2) {
                com.android.server.DeviceIdleController.this.mSensorManager.cancelTriggerSensor(com.android.server.DeviceIdleController.this.mMotionListener, com.android.server.DeviceIdleController.this.mMotionSensor);
            } else {
                com.android.server.DeviceIdleController.this.mSensorManager.unregisterListener(com.android.server.DeviceIdleController.this.mMotionListener);
            }
            this.active = false;
        }
    }

    public final class Constants extends android.database.ContentObserver implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private static final long DEFAULT_IDLE_AFTER_INACTIVE_TIMEOUT_SMALL_BATTERY = 60000;
        private static final long DEFAULT_INACTIVE_TIMEOUT_SMALL_BATTERY = 60000;
        private static final java.lang.String KEY_FLEX_TIME_SHORT = "flex_time_short";
        private static final java.lang.String KEY_IDLE_AFTER_INACTIVE_TIMEOUT = "idle_after_inactive_to";
        private static final java.lang.String KEY_IDLE_FACTOR = "idle_factor";
        private static final java.lang.String KEY_IDLE_PENDING_FACTOR = "idle_pending_factor";
        private static final java.lang.String KEY_IDLE_PENDING_TIMEOUT = "idle_pending_to";
        private static final java.lang.String KEY_IDLE_TIMEOUT = "idle_to";
        private static final java.lang.String KEY_INACTIVE_TIMEOUT = "inactive_to";
        private static final java.lang.String KEY_LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT = "light_after_inactive_to";
        private static final java.lang.String KEY_LIGHT_IDLE_FACTOR = "light_idle_factor";
        private static final java.lang.String KEY_LIGHT_IDLE_FLEX_LINEAR_INCREASE_FACTOR_MS = "light_idle_flex_linear_increase_factor_ms";
        private static final java.lang.String KEY_LIGHT_IDLE_INCREASE_LINEARLY = "light_idle_increase_linearly";
        private static final java.lang.String KEY_LIGHT_IDLE_LINEAR_INCREASE_FACTOR_MS = "light_idle_linear_increase_factor_ms";
        private static final java.lang.String KEY_LIGHT_IDLE_MAINTENANCE_MAX_BUDGET = "light_idle_maintenance_max_budget";
        private static final java.lang.String KEY_LIGHT_IDLE_MAINTENANCE_MIN_BUDGET = "light_idle_maintenance_min_budget";
        private static final java.lang.String KEY_LIGHT_IDLE_TIMEOUT = "light_idle_to";
        private static final java.lang.String KEY_LIGHT_IDLE_TIMEOUT_INITIAL_FLEX = "light_idle_to_initial_flex";
        private static final java.lang.String KEY_LIGHT_IDLE_TIMEOUT_MAX_FLEX = "light_max_idle_to_flex";
        private static final java.lang.String KEY_LIGHT_MAX_IDLE_TIMEOUT = "light_max_idle_to";
        private static final java.lang.String KEY_LOCATING_TIMEOUT = "locating_to";
        private static final java.lang.String KEY_LOCATION_ACCURACY = "location_accuracy";
        private static final java.lang.String KEY_MAX_IDLE_PENDING_TIMEOUT = "max_idle_pending_to";
        private static final java.lang.String KEY_MAX_IDLE_TIMEOUT = "max_idle_to";
        private static final java.lang.String KEY_MAX_TEMP_APP_ALLOWLIST_DURATION_MS = "max_temp_app_allowlist_duration_ms";
        private static final java.lang.String KEY_MIN_DEEP_MAINTENANCE_TIME = "min_deep_maintenance_time";
        private static final java.lang.String KEY_MIN_LIGHT_MAINTENANCE_TIME = "min_light_maintenance_time";
        private static final java.lang.String KEY_MIN_TIME_TO_ALARM = "min_time_to_alarm";
        private static final java.lang.String KEY_MMS_TEMP_APP_ALLOWLIST_DURATION_MS = "mms_temp_app_allowlist_duration_ms";
        private static final java.lang.String KEY_MOTION_INACTIVE_TIMEOUT = "motion_inactive_to";
        private static final java.lang.String KEY_MOTION_INACTIVE_TIMEOUT_FLEX = "motion_inactive_to_flex";
        private static final java.lang.String KEY_NOTIFICATION_ALLOWLIST_DURATION_MS = "notification_allowlist_duration_ms";
        private static final java.lang.String KEY_QUICK_DOZE_DELAY_TIMEOUT = "quick_doze_delay_to";
        private static final java.lang.String KEY_SENSING_TIMEOUT = "sensing_to";
        private static final java.lang.String KEY_SMS_TEMP_APP_ALLOWLIST_DURATION_MS = "sms_temp_app_allowlist_duration_ms";
        private static final java.lang.String KEY_USE_MODE_MANAGER = "use_mode_manager";
        private static final java.lang.String KEY_USE_WINDOW_ALARMS = "use_window_alarms";
        private static final java.lang.String KEY_WAIT_FOR_UNLOCK = "wait_for_unlock";
        public long FLEX_TIME_SHORT;
        public long IDLE_AFTER_INACTIVE_TIMEOUT;
        public float IDLE_FACTOR;
        public float IDLE_PENDING_FACTOR;
        public long IDLE_PENDING_TIMEOUT;
        public long IDLE_TIMEOUT;
        public long INACTIVE_TIMEOUT;
        public long LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT;
        public float LIGHT_IDLE_FACTOR;
        public long LIGHT_IDLE_FLEX_LINEAR_INCREASE_FACTOR_MS;
        public boolean LIGHT_IDLE_INCREASE_LINEARLY;
        public long LIGHT_IDLE_LINEAR_INCREASE_FACTOR_MS;
        public long LIGHT_IDLE_MAINTENANCE_MAX_BUDGET;
        public long LIGHT_IDLE_MAINTENANCE_MIN_BUDGET;
        public long LIGHT_IDLE_TIMEOUT;
        public long LIGHT_IDLE_TIMEOUT_INITIAL_FLEX;
        public long LIGHT_IDLE_TIMEOUT_MAX_FLEX;
        public long LIGHT_MAX_IDLE_TIMEOUT;
        public long LOCATING_TIMEOUT;
        public float LOCATION_ACCURACY;
        public long MAX_IDLE_PENDING_TIMEOUT;
        public long MAX_IDLE_TIMEOUT;
        public long MAX_TEMP_APP_ALLOWLIST_DURATION_MS;
        public long MIN_DEEP_MAINTENANCE_TIME;
        public long MIN_LIGHT_MAINTENANCE_TIME;
        public long MIN_TIME_TO_ALARM;
        public long MMS_TEMP_APP_ALLOWLIST_DURATION_MS;
        public long MOTION_INACTIVE_TIMEOUT;
        public long MOTION_INACTIVE_TIMEOUT_FLEX;
        public long NOTIFICATION_ALLOWLIST_DURATION_MS;
        public long QUICK_DOZE_DELAY_TIMEOUT;
        public long SENSING_TIMEOUT;
        public long SMS_TEMP_APP_ALLOWLIST_DURATION_MS;
        public boolean USE_MODE_MANAGER;
        public boolean USE_WINDOW_ALARMS;
        public boolean WAIT_FOR_UNLOCK;
        private long mDefaultFlexTimeShort;
        private long mDefaultIdleAfterInactiveTimeout;
        private float mDefaultIdleFactor;
        private float mDefaultIdlePendingFactor;
        private long mDefaultIdlePendingTimeout;
        private long mDefaultIdleTimeout;
        private long mDefaultInactiveTimeout;
        private long mDefaultLightIdleAfterInactiveTimeout;
        private float mDefaultLightIdleFactor;
        private long mDefaultLightIdleFlexLinearIncreaseFactorMs;
        private boolean mDefaultLightIdleIncreaseLinearly;
        private long mDefaultLightIdleLinearIncreaseFactorMs;
        private long mDefaultLightIdleMaintenanceMaxBudget;
        private long mDefaultLightIdleMaintenanceMinBudget;
        private long mDefaultLightIdleTimeout;
        private long mDefaultLightIdleTimeoutInitialFlex;
        private long mDefaultLightIdleTimeoutMaxFlex;
        private long mDefaultLightMaxIdleTimeout;
        private long mDefaultLocatingTimeout;
        private float mDefaultLocationAccuracy;
        private long mDefaultMaxIdlePendingTimeout;
        private long mDefaultMaxIdleTimeout;
        private long mDefaultMaxTempAppAllowlistDurationMs;
        private long mDefaultMinDeepMaintenanceTime;
        private long mDefaultMinLightMaintenanceTime;
        private long mDefaultMinTimeToAlarm;
        private long mDefaultMmsTempAppAllowlistDurationMs;
        private long mDefaultMotionInactiveTimeout;
        private long mDefaultMotionInactiveTimeoutFlex;
        private long mDefaultNotificationAllowlistDurationMs;
        private long mDefaultQuickDozeDelayTimeout;
        private long mDefaultSensingTimeout;
        private long mDefaultSmsTempAppAllowlistDurationMs;
        private boolean mDefaultUseModeManager;
        private boolean mDefaultUseWindowAlarms;
        private boolean mDefaultWaitForUnlock;
        private final android.content.ContentResolver mResolver;
        private final boolean mSmallBatteryDevice;
        private final com.android.server.utils.UserSettingDeviceConfigMediator mUserSettingDeviceConfigMediator;

        public Constants(android.os.Handler handler, android.content.ContentResolver resolver) {
            super(handler);
            this.mDefaultFlexTimeShort = 60000L;
            this.mDefaultLightIdleAfterInactiveTimeout = 240000L;
            this.mDefaultLightIdleTimeout = 300000L;
            this.mDefaultLightIdleTimeoutInitialFlex = 60000L;
            this.mDefaultLightIdleTimeoutMaxFlex = 900000L;
            this.mDefaultLightIdleFactor = 2.0f;
            this.mDefaultLightIdleLinearIncreaseFactorMs = this.mDefaultLightIdleTimeout;
            this.mDefaultLightIdleFlexLinearIncreaseFactorMs = this.mDefaultLightIdleTimeoutInitialFlex;
            this.mDefaultLightMaxIdleTimeout = 900000L;
            this.mDefaultLightIdleMaintenanceMinBudget = 60000L;
            this.mDefaultLightIdleMaintenanceMaxBudget = 300000L;
            this.mDefaultMinLightMaintenanceTime = 5000L;
            this.mDefaultMinDeepMaintenanceTime = 30000L;
            this.mDefaultInactiveTimeout = 1800000L;
            this.mDefaultSensingTimeout = 240000L;
            this.mDefaultLocatingTimeout = 30000L;
            this.mDefaultLocationAccuracy = 20.0f;
            this.mDefaultMotionInactiveTimeout = 600000L;
            this.mDefaultMotionInactiveTimeoutFlex = 60000L;
            this.mDefaultIdleAfterInactiveTimeout = 1800000L;
            this.mDefaultIdlePendingTimeout = 300000L;
            this.mDefaultMaxIdlePendingTimeout = 600000L;
            this.mDefaultIdlePendingFactor = 2.0f;
            this.mDefaultQuickDozeDelayTimeout = 60000L;
            this.mDefaultIdleTimeout = 3600000L;
            this.mDefaultMaxIdleTimeout = 21600000L;
            this.mDefaultIdleFactor = 2.0f;
            this.mDefaultMinTimeToAlarm = 1800000L;
            this.mDefaultMaxTempAppAllowlistDurationMs = 300000L;
            this.mDefaultMmsTempAppAllowlistDurationMs = 60000L;
            this.mDefaultSmsTempAppAllowlistDurationMs = 20000L;
            this.mDefaultNotificationAllowlistDurationMs = 30000L;
            this.mDefaultWaitForUnlock = true;
            this.mDefaultUseWindowAlarms = true;
            this.mDefaultUseModeManager = false;
            this.FLEX_TIME_SHORT = this.mDefaultFlexTimeShort;
            this.LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT = this.mDefaultLightIdleAfterInactiveTimeout;
            this.LIGHT_IDLE_TIMEOUT = this.mDefaultLightIdleTimeout;
            this.LIGHT_IDLE_TIMEOUT_INITIAL_FLEX = this.mDefaultLightIdleTimeoutInitialFlex;
            this.LIGHT_IDLE_TIMEOUT_MAX_FLEX = this.mDefaultLightIdleTimeoutMaxFlex;
            this.LIGHT_IDLE_FACTOR = this.mDefaultLightIdleFactor;
            this.LIGHT_IDLE_INCREASE_LINEARLY = this.mDefaultLightIdleIncreaseLinearly;
            this.LIGHT_IDLE_LINEAR_INCREASE_FACTOR_MS = this.mDefaultLightIdleLinearIncreaseFactorMs;
            this.LIGHT_IDLE_FLEX_LINEAR_INCREASE_FACTOR_MS = this.mDefaultLightIdleFlexLinearIncreaseFactorMs;
            this.LIGHT_MAX_IDLE_TIMEOUT = this.mDefaultLightMaxIdleTimeout;
            this.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET = this.mDefaultLightIdleMaintenanceMinBudget;
            this.LIGHT_IDLE_MAINTENANCE_MAX_BUDGET = this.mDefaultLightIdleMaintenanceMaxBudget;
            this.MIN_LIGHT_MAINTENANCE_TIME = this.mDefaultMinLightMaintenanceTime;
            this.MIN_DEEP_MAINTENANCE_TIME = this.mDefaultMinDeepMaintenanceTime;
            this.INACTIVE_TIMEOUT = this.mDefaultInactiveTimeout;
            this.SENSING_TIMEOUT = this.mDefaultSensingTimeout;
            this.LOCATING_TIMEOUT = this.mDefaultLocatingTimeout;
            this.LOCATION_ACCURACY = this.mDefaultLocationAccuracy;
            this.MOTION_INACTIVE_TIMEOUT = this.mDefaultMotionInactiveTimeout;
            this.MOTION_INACTIVE_TIMEOUT_FLEX = this.mDefaultMotionInactiveTimeoutFlex;
            this.IDLE_AFTER_INACTIVE_TIMEOUT = this.mDefaultIdleAfterInactiveTimeout;
            this.IDLE_PENDING_TIMEOUT = this.mDefaultIdlePendingTimeout;
            this.MAX_IDLE_PENDING_TIMEOUT = this.mDefaultMaxIdlePendingTimeout;
            this.IDLE_PENDING_FACTOR = this.mDefaultIdlePendingFactor;
            this.QUICK_DOZE_DELAY_TIMEOUT = this.mDefaultQuickDozeDelayTimeout;
            this.IDLE_TIMEOUT = this.mDefaultIdleTimeout;
            this.MAX_IDLE_TIMEOUT = this.mDefaultMaxIdleTimeout;
            this.IDLE_FACTOR = this.mDefaultIdleFactor;
            this.MIN_TIME_TO_ALARM = this.mDefaultMinTimeToAlarm;
            this.MAX_TEMP_APP_ALLOWLIST_DURATION_MS = this.mDefaultMaxTempAppAllowlistDurationMs;
            this.MMS_TEMP_APP_ALLOWLIST_DURATION_MS = this.mDefaultMmsTempAppAllowlistDurationMs;
            this.SMS_TEMP_APP_ALLOWLIST_DURATION_MS = this.mDefaultSmsTempAppAllowlistDurationMs;
            this.NOTIFICATION_ALLOWLIST_DURATION_MS = this.mDefaultNotificationAllowlistDurationMs;
            this.WAIT_FOR_UNLOCK = this.mDefaultWaitForUnlock;
            this.USE_WINDOW_ALARMS = this.mDefaultUseWindowAlarms;
            this.USE_MODE_MANAGER = this.mDefaultUseModeManager;
            this.mUserSettingDeviceConfigMediator = new com.android.server.utils.UserSettingDeviceConfigMediator.SettingsOverridesIndividualMediator(',');
            this.mResolver = resolver;
            initDefault();
            this.mSmallBatteryDevice = android.app.ActivityManager.isSmallBatteryDevice();
            if (this.mSmallBatteryDevice) {
                this.INACTIVE_TIMEOUT = 60000L;
                this.IDLE_AFTER_INACTIVE_TIMEOUT = 60000L;
            }
            com.android.server.DeviceIdleController.this.mDeviceIdleExt.initArgs(this, com.android.server.DeviceIdleController.this.getContext(), com.android.server.DeviceIdleController.this.mHandler, com.android.server.DeviceIdleController.this);
            android.provider.DeviceConfig.addOnPropertiesChangedListener("device_idle", com.android.server.AppSchedulingModuleThread.getExecutor(), this);
            this.mResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("device_idle_constants"), false, this);
            updateSettingsConstantLocked();
            this.mUserSettingDeviceConfigMediator.setDeviceConfigProperties(android.provider.DeviceConfig.getProperties("device_idle", new java.lang.String[0]));
            updateConstantsLocked();
        }

        private void initDefault() {
            android.content.res.Resources res = com.android.server.DeviceIdleController.this.getContext().getResources();
            this.mDefaultFlexTimeShort = getTimeout(res.getInteger(android.R.integer.config_valid_wappush_index), this.mDefaultFlexTimeShort);
            this.mDefaultLightIdleAfterInactiveTimeout = getTimeout(res.getInteger(android.R.integer.config_vibratorControlServiceDumpSizeLimit), this.mDefaultLightIdleAfterInactiveTimeout);
            this.mDefaultLightIdleTimeout = getTimeout(res.getInteger(android.R.integer.config_wakeUpToLastStateTimeoutMillis), this.mDefaultLightIdleTimeout);
            this.mDefaultLightIdleTimeoutInitialFlex = getTimeout(res.getInteger(android.R.integer.config_wait_for_datagram_sending_response_timeout_millis), this.mDefaultLightIdleTimeoutInitialFlex);
            this.mDefaultLightIdleTimeoutMaxFlex = getTimeout(res.getInteger(android.R.integer.config_wait_for_satellite_enabling_response_timeout_millis), this.mDefaultLightIdleTimeoutMaxFlex);
            this.mDefaultLightIdleFactor = res.getFloat(android.R.integer.config_virtualDisplayLimit);
            this.mDefaultLightIdleIncreaseLinearly = res.getBoolean(android.R.bool.config_use_voip_mode_for_ims);
            this.mDefaultLightIdleLinearIncreaseFactorMs = getTimeout(res.getInteger(android.R.integer.config_virtualKeyQuietTimeMillis), this.mDefaultLightIdleLinearIncreaseFactorMs);
            this.mDefaultLightIdleFlexLinearIncreaseFactorMs = getTimeout(res.getInteger(android.R.integer.config_virtualDisplayLimitPerPackage), this.mDefaultLightIdleFlexLinearIncreaseFactorMs);
            this.mDefaultLightMaxIdleTimeout = getTimeout(res.getInteger(android.R.integer.config_wallpaperFrameRateCompatibility), this.mDefaultLightMaxIdleTimeout);
            this.mDefaultLightIdleMaintenanceMinBudget = getTimeout(res.getInteger(android.R.integer.config_wait_for_datagram_sending_response_for_last_message_timeout_millis), this.mDefaultLightIdleMaintenanceMinBudget);
            this.mDefaultLightIdleMaintenanceMaxBudget = getTimeout(res.getInteger(android.R.integer.config_volte_replacement_rat), this.mDefaultLightIdleMaintenanceMaxBudget);
            this.mDefaultMinLightMaintenanceTime = getTimeout(res.getInteger(android.R.integer.date_picker_mode_material), this.mDefaultMinLightMaintenanceTime);
            this.mDefaultMinDeepMaintenanceTime = getTimeout(res.getInteger(android.R.integer.date_picker_mode), this.mDefaultMinDeepMaintenanceTime);
            this.mDefaultInactiveTimeout = getTimeout(res.getInteger(android.R.integer.config_vibratorControlServiceDumpAggregationTimeMillisLimit), this.mDefaultInactiveTimeout);
            this.mDefaultSensingTimeout = getTimeout(res.getInteger(android.R.integer.default_reserved_data_coding_scheme), this.mDefaultSensingTimeout);
            this.mDefaultLocatingTimeout = getTimeout(res.getInteger(android.R.integer.config_whenToStartHubModeDefault), this.mDefaultLocatingTimeout);
            this.mDefaultLocationAccuracy = res.getFloat(android.R.integer.config_windowOutsetBottom);
            this.mDefaultMotionInactiveTimeout = getTimeout(res.getInteger(android.R.integer.db_wal_autocheckpoint), this.mDefaultMotionInactiveTimeout);
            this.mDefaultMotionInactiveTimeoutFlex = getTimeout(res.getInteger(android.R.integer.db_journal_size_limit), this.mDefaultMotionInactiveTimeoutFlex);
            this.mDefaultIdleAfterInactiveTimeout = getTimeout(res.getInteger(android.R.integer.config_veryLongPressOnPowerBehavior), this.mDefaultIdleAfterInactiveTimeout);
            this.mDefaultIdlePendingTimeout = getTimeout(res.getInteger(android.R.integer.config_vibrationWaveformRampDownDuration), this.mDefaultIdlePendingTimeout);
            this.mDefaultMaxIdlePendingTimeout = getTimeout(res.getInteger(android.R.integer.config_zen_repeat_callers_threshold), this.mDefaultMaxIdlePendingTimeout);
            this.mDefaultIdlePendingFactor = res.getFloat(android.R.integer.config_vibrationPipelineMaxDuration);
            this.mDefaultQuickDozeDelayTimeout = getTimeout(res.getInteger(android.R.integer.default_data_warning_level_mb), this.mDefaultQuickDozeDelayTimeout);
            this.mDefaultIdleTimeout = getTimeout(res.getInteger(android.R.integer.config_vibrationWaveformRampStepDuration), this.mDefaultIdleTimeout);
            this.mDefaultMaxIdleTimeout = getTimeout(res.getInteger(android.R.integer.config_zoomControlsTimeoutMillis), this.mDefaultMaxIdleTimeout);
            this.mDefaultIdleFactor = res.getFloat(android.R.integer.config_veryLongPressTimeout);
            this.mDefaultMinTimeToAlarm = getTimeout(res.getInteger(android.R.integer.db_connection_pool_size), this.mDefaultMinTimeToAlarm);
            this.mDefaultMaxTempAppAllowlistDurationMs = res.getInteger(android.R.integer.date_picker_header_max_lines_material);
            this.mDefaultMmsTempAppAllowlistDurationMs = res.getInteger(android.R.integer.db_default_idle_connection_timeout);
            this.mDefaultSmsTempAppAllowlistDurationMs = res.getInteger(android.R.integer.device_idle_flex_time_short_ms);
            this.mDefaultNotificationAllowlistDurationMs = res.getInteger(android.R.integer.db_wal_truncate_size);
            this.mDefaultWaitForUnlock = res.getBoolean(android.R.bool.config_viewBasedRotaryEncoderHapticsEnabled);
            this.mDefaultUseWindowAlarms = res.getBoolean(android.R.bool.config_vehicleInternalNetworkAlwaysRequested);
            this.mDefaultUseModeManager = res.getBoolean(android.R.bool.config_user_notification_of_restrictied_mobile_access);
            this.FLEX_TIME_SHORT = this.mDefaultFlexTimeShort;
            this.LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT = this.mDefaultLightIdleAfterInactiveTimeout;
            this.LIGHT_IDLE_TIMEOUT = this.mDefaultLightIdleTimeout;
            this.LIGHT_IDLE_TIMEOUT_INITIAL_FLEX = this.mDefaultLightIdleTimeoutInitialFlex;
            this.LIGHT_IDLE_TIMEOUT_MAX_FLEX = this.mDefaultLightIdleTimeoutMaxFlex;
            this.LIGHT_IDLE_FACTOR = this.mDefaultLightIdleFactor;
            this.LIGHT_IDLE_INCREASE_LINEARLY = this.mDefaultLightIdleIncreaseLinearly;
            this.LIGHT_IDLE_LINEAR_INCREASE_FACTOR_MS = this.mDefaultLightIdleLinearIncreaseFactorMs;
            this.LIGHT_IDLE_FLEX_LINEAR_INCREASE_FACTOR_MS = this.mDefaultLightIdleFlexLinearIncreaseFactorMs;
            this.LIGHT_MAX_IDLE_TIMEOUT = this.mDefaultLightMaxIdleTimeout;
            this.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET = this.mDefaultLightIdleMaintenanceMinBudget;
            this.LIGHT_IDLE_MAINTENANCE_MAX_BUDGET = this.mDefaultLightIdleMaintenanceMaxBudget;
            this.MIN_LIGHT_MAINTENANCE_TIME = this.mDefaultMinLightMaintenanceTime;
            this.MIN_DEEP_MAINTENANCE_TIME = this.mDefaultMinDeepMaintenanceTime;
            this.INACTIVE_TIMEOUT = this.mDefaultInactiveTimeout;
            this.SENSING_TIMEOUT = this.mDefaultSensingTimeout;
            this.LOCATING_TIMEOUT = this.mDefaultLocatingTimeout;
            this.LOCATION_ACCURACY = this.mDefaultLocationAccuracy;
            this.MOTION_INACTIVE_TIMEOUT = this.mDefaultMotionInactiveTimeout;
            this.MOTION_INACTIVE_TIMEOUT_FLEX = this.mDefaultMotionInactiveTimeoutFlex;
            this.IDLE_AFTER_INACTIVE_TIMEOUT = this.mDefaultIdleAfterInactiveTimeout;
            this.IDLE_PENDING_TIMEOUT = this.mDefaultIdlePendingTimeout;
            this.MAX_IDLE_PENDING_TIMEOUT = this.mDefaultMaxIdlePendingTimeout;
            this.IDLE_PENDING_FACTOR = this.mDefaultIdlePendingFactor;
            this.QUICK_DOZE_DELAY_TIMEOUT = this.mDefaultQuickDozeDelayTimeout;
            this.IDLE_TIMEOUT = this.mDefaultIdleTimeout;
            this.MAX_IDLE_TIMEOUT = this.mDefaultMaxIdleTimeout;
            this.IDLE_FACTOR = this.mDefaultIdleFactor;
            this.MIN_TIME_TO_ALARM = this.mDefaultMinTimeToAlarm;
            this.MAX_TEMP_APP_ALLOWLIST_DURATION_MS = this.mDefaultMaxTempAppAllowlistDurationMs;
            this.MMS_TEMP_APP_ALLOWLIST_DURATION_MS = this.mDefaultMmsTempAppAllowlistDurationMs;
            this.SMS_TEMP_APP_ALLOWLIST_DURATION_MS = this.mDefaultSmsTempAppAllowlistDurationMs;
            this.NOTIFICATION_ALLOWLIST_DURATION_MS = this.mDefaultNotificationAllowlistDurationMs;
            this.WAIT_FOR_UNLOCK = this.mDefaultWaitForUnlock;
            this.USE_WINDOW_ALARMS = this.mDefaultUseWindowAlarms;
            this.USE_MODE_MANAGER = this.mDefaultUseModeManager;
        }

        private long getTimeout(long defTimeout, long compTimeout) {
            return defTimeout;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            synchronized (com.android.server.DeviceIdleController.this) {
                updateSettingsConstantLocked();
                updateConstantsLocked();
            }
        }

        private void updateSettingsConstantLocked() {
            try {
                this.mUserSettingDeviceConfigMediator.setSettingsString(android.provider.Settings.Global.getString(this.mResolver, "device_idle_constants"));
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.e(com.android.server.DeviceIdleController.TAG, "Bad device idle settings", e);
            }
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            synchronized (com.android.server.DeviceIdleController.this) {
                this.mUserSettingDeviceConfigMediator.setDeviceConfigProperties(properties);
                updateConstantsLocked();
            }
        }

        private void updateConstantsLocked() {
            long defaultInactiveTimeout;
            if (this.mSmallBatteryDevice) {
                return;
            }
            this.FLEX_TIME_SHORT = this.mUserSettingDeviceConfigMediator.getLong(KEY_FLEX_TIME_SHORT, this.mDefaultFlexTimeShort);
            this.LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT, this.mDefaultLightIdleAfterInactiveTimeout);
            this.LIGHT_IDLE_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_LIGHT_IDLE_TIMEOUT, this.mDefaultLightIdleTimeout);
            this.LIGHT_IDLE_TIMEOUT_INITIAL_FLEX = this.mUserSettingDeviceConfigMediator.getLong(KEY_LIGHT_IDLE_TIMEOUT_INITIAL_FLEX, this.mDefaultLightIdleTimeoutInitialFlex);
            this.LIGHT_IDLE_TIMEOUT_MAX_FLEX = this.mUserSettingDeviceConfigMediator.getLong(KEY_LIGHT_IDLE_TIMEOUT_MAX_FLEX, this.mDefaultLightIdleTimeoutMaxFlex);
            this.LIGHT_IDLE_FACTOR = java.lang.Math.max(1.0f, this.mUserSettingDeviceConfigMediator.getFloat(KEY_LIGHT_IDLE_FACTOR, this.mDefaultLightIdleFactor));
            this.LIGHT_IDLE_INCREASE_LINEARLY = this.mUserSettingDeviceConfigMediator.getBoolean(KEY_LIGHT_IDLE_INCREASE_LINEARLY, this.mDefaultLightIdleIncreaseLinearly);
            this.LIGHT_IDLE_LINEAR_INCREASE_FACTOR_MS = this.mUserSettingDeviceConfigMediator.getLong(KEY_LIGHT_IDLE_LINEAR_INCREASE_FACTOR_MS, this.mDefaultLightIdleLinearIncreaseFactorMs);
            this.LIGHT_IDLE_FLEX_LINEAR_INCREASE_FACTOR_MS = this.mUserSettingDeviceConfigMediator.getLong(KEY_LIGHT_IDLE_FLEX_LINEAR_INCREASE_FACTOR_MS, this.mDefaultLightIdleFlexLinearIncreaseFactorMs);
            this.LIGHT_MAX_IDLE_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_LIGHT_MAX_IDLE_TIMEOUT, this.mDefaultLightMaxIdleTimeout);
            this.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET = this.mUserSettingDeviceConfigMediator.getLong(KEY_LIGHT_IDLE_MAINTENANCE_MIN_BUDGET, this.mDefaultLightIdleMaintenanceMinBudget);
            this.LIGHT_IDLE_MAINTENANCE_MAX_BUDGET = this.mUserSettingDeviceConfigMediator.getLong(KEY_LIGHT_IDLE_MAINTENANCE_MAX_BUDGET, this.mDefaultLightIdleMaintenanceMaxBudget);
            this.MIN_LIGHT_MAINTENANCE_TIME = this.mUserSettingDeviceConfigMediator.getLong(KEY_MIN_LIGHT_MAINTENANCE_TIME, this.mDefaultMinLightMaintenanceTime);
            this.MIN_DEEP_MAINTENANCE_TIME = this.mUserSettingDeviceConfigMediator.getLong(KEY_MIN_DEEP_MAINTENANCE_TIME, this.mDefaultMinDeepMaintenanceTime);
            if (this.mSmallBatteryDevice) {
                defaultInactiveTimeout = 60000;
            } else {
                defaultInactiveTimeout = this.mDefaultInactiveTimeout;
            }
            this.INACTIVE_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_INACTIVE_TIMEOUT, defaultInactiveTimeout);
            this.SENSING_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_SENSING_TIMEOUT, this.mDefaultSensingTimeout);
            this.LOCATING_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_LOCATING_TIMEOUT, this.mDefaultLocatingTimeout);
            this.LOCATION_ACCURACY = this.mUserSettingDeviceConfigMediator.getFloat(KEY_LOCATION_ACCURACY, this.mDefaultLocationAccuracy);
            this.MOTION_INACTIVE_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_MOTION_INACTIVE_TIMEOUT, this.mDefaultMotionInactiveTimeout);
            this.MOTION_INACTIVE_TIMEOUT_FLEX = this.mUserSettingDeviceConfigMediator.getLong(KEY_MOTION_INACTIVE_TIMEOUT_FLEX, this.mDefaultMotionInactiveTimeoutFlex);
            long defaultIdleAfterInactiveTimeout = this.mSmallBatteryDevice ? 60000L : this.mDefaultIdleAfterInactiveTimeout;
            this.IDLE_AFTER_INACTIVE_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_IDLE_AFTER_INACTIVE_TIMEOUT, defaultIdleAfterInactiveTimeout);
            this.IDLE_PENDING_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_IDLE_PENDING_TIMEOUT, this.mDefaultIdlePendingTimeout);
            this.MAX_IDLE_PENDING_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_MAX_IDLE_PENDING_TIMEOUT, this.mDefaultMaxIdlePendingTimeout);
            this.IDLE_PENDING_FACTOR = this.mUserSettingDeviceConfigMediator.getFloat(KEY_IDLE_PENDING_FACTOR, this.mDefaultIdlePendingFactor);
            this.QUICK_DOZE_DELAY_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_QUICK_DOZE_DELAY_TIMEOUT, this.mDefaultQuickDozeDelayTimeout);
            this.IDLE_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_IDLE_TIMEOUT, this.mDefaultIdleTimeout);
            this.MAX_IDLE_TIMEOUT = this.mUserSettingDeviceConfigMediator.getLong(KEY_MAX_IDLE_TIMEOUT, this.mDefaultMaxIdleTimeout);
            this.IDLE_FACTOR = this.mUserSettingDeviceConfigMediator.getFloat(KEY_IDLE_FACTOR, this.mDefaultIdleFactor);
            this.MIN_TIME_TO_ALARM = this.mUserSettingDeviceConfigMediator.getLong(KEY_MIN_TIME_TO_ALARM, this.mDefaultMinTimeToAlarm);
            this.MAX_TEMP_APP_ALLOWLIST_DURATION_MS = this.mUserSettingDeviceConfigMediator.getLong(KEY_MAX_TEMP_APP_ALLOWLIST_DURATION_MS, this.mDefaultMaxTempAppAllowlistDurationMs);
            this.MMS_TEMP_APP_ALLOWLIST_DURATION_MS = this.mUserSettingDeviceConfigMediator.getLong(KEY_MMS_TEMP_APP_ALLOWLIST_DURATION_MS, this.mDefaultMmsTempAppAllowlistDurationMs);
            this.SMS_TEMP_APP_ALLOWLIST_DURATION_MS = this.mUserSettingDeviceConfigMediator.getLong(KEY_SMS_TEMP_APP_ALLOWLIST_DURATION_MS, this.mDefaultSmsTempAppAllowlistDurationMs);
            this.NOTIFICATION_ALLOWLIST_DURATION_MS = this.mUserSettingDeviceConfigMediator.getLong(KEY_NOTIFICATION_ALLOWLIST_DURATION_MS, this.mDefaultNotificationAllowlistDurationMs);
            this.WAIT_FOR_UNLOCK = this.mUserSettingDeviceConfigMediator.getBoolean(KEY_WAIT_FOR_UNLOCK, this.mDefaultWaitForUnlock);
            this.USE_WINDOW_ALARMS = this.mUserSettingDeviceConfigMediator.getBoolean(KEY_USE_WINDOW_ALARMS, this.mDefaultUseWindowAlarms);
            this.USE_MODE_MANAGER = this.mUserSettingDeviceConfigMediator.getBoolean(KEY_USE_MODE_MANAGER, this.mDefaultUseModeManager);
        }

        void dump(java.io.PrintWriter pw) {
            pw.println("  Settings:");
            pw.print("    ");
            pw.print(KEY_FLEX_TIME_SHORT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.FLEX_TIME_SHORT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LIGHT_IDLE_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.LIGHT_IDLE_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LIGHT_IDLE_TIMEOUT_INITIAL_FLEX);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.LIGHT_IDLE_TIMEOUT_INITIAL_FLEX, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LIGHT_IDLE_TIMEOUT_MAX_FLEX);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.LIGHT_IDLE_TIMEOUT_MAX_FLEX, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LIGHT_IDLE_FACTOR);
            pw.print("=");
            pw.print(this.LIGHT_IDLE_FACTOR);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LIGHT_IDLE_INCREASE_LINEARLY);
            pw.print("=");
            pw.print(this.LIGHT_IDLE_INCREASE_LINEARLY);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LIGHT_IDLE_LINEAR_INCREASE_FACTOR_MS);
            pw.print("=");
            pw.print(this.LIGHT_IDLE_LINEAR_INCREASE_FACTOR_MS);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LIGHT_IDLE_FLEX_LINEAR_INCREASE_FACTOR_MS);
            pw.print("=");
            pw.print(this.LIGHT_IDLE_FLEX_LINEAR_INCREASE_FACTOR_MS);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LIGHT_MAX_IDLE_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.LIGHT_MAX_IDLE_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LIGHT_IDLE_MAINTENANCE_MIN_BUDGET);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LIGHT_IDLE_MAINTENANCE_MAX_BUDGET);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.LIGHT_IDLE_MAINTENANCE_MAX_BUDGET, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_MIN_LIGHT_MAINTENANCE_TIME);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MIN_LIGHT_MAINTENANCE_TIME, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_MIN_DEEP_MAINTENANCE_TIME);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MIN_DEEP_MAINTENANCE_TIME, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_INACTIVE_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.INACTIVE_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_SENSING_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.SENSING_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LOCATING_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.LOCATING_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_LOCATION_ACCURACY);
            pw.print("=");
            pw.print(this.LOCATION_ACCURACY);
            pw.print("m");
            pw.println();
            pw.print("    ");
            pw.print(KEY_MOTION_INACTIVE_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MOTION_INACTIVE_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_MOTION_INACTIVE_TIMEOUT_FLEX);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MOTION_INACTIVE_TIMEOUT_FLEX, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_IDLE_AFTER_INACTIVE_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.IDLE_AFTER_INACTIVE_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_IDLE_PENDING_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.IDLE_PENDING_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_MAX_IDLE_PENDING_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MAX_IDLE_PENDING_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_IDLE_PENDING_FACTOR);
            pw.print("=");
            pw.println(this.IDLE_PENDING_FACTOR);
            pw.print("    ");
            pw.print(KEY_QUICK_DOZE_DELAY_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.QUICK_DOZE_DELAY_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_IDLE_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.IDLE_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_MAX_IDLE_TIMEOUT);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MAX_IDLE_TIMEOUT, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_IDLE_FACTOR);
            pw.print("=");
            pw.println(this.IDLE_FACTOR);
            pw.print("    ");
            pw.print(KEY_MIN_TIME_TO_ALARM);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MIN_TIME_TO_ALARM, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_MAX_TEMP_APP_ALLOWLIST_DURATION_MS);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MAX_TEMP_APP_ALLOWLIST_DURATION_MS, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_MMS_TEMP_APP_ALLOWLIST_DURATION_MS);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.MMS_TEMP_APP_ALLOWLIST_DURATION_MS, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_SMS_TEMP_APP_ALLOWLIST_DURATION_MS);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.SMS_TEMP_APP_ALLOWLIST_DURATION_MS, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_NOTIFICATION_ALLOWLIST_DURATION_MS);
            pw.print("=");
            android.util.TimeUtils.formatDuration(this.NOTIFICATION_ALLOWLIST_DURATION_MS, pw);
            pw.println();
            pw.print("    ");
            pw.print(KEY_WAIT_FOR_UNLOCK);
            pw.print("=");
            pw.println(this.WAIT_FOR_UNLOCK);
            pw.print("    ");
            pw.print(KEY_USE_WINDOW_ALARMS);
            pw.print("=");
            pw.println(this.USE_WINDOW_ALARMS);
            pw.print("    ");
            pw.print(KEY_USE_MODE_MANAGER);
            pw.print("=");
            pw.println(this.USE_MODE_MANAGER);
        }
    }

    @Override // com.android.server.AnyMotionDetector.DeviceIdleCallback
    public void onAnyMotionResult(int result) {
        synchronized (this) {
            if (result != -1) {
                try {
                    cancelSensingTimeoutAlarmLocked();
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
            if (result == 1 || result == -1) {
                handleMotionDetectedLocked(this.mConstants.INACTIVE_TIMEOUT, "non_stationary");
            } else if (result == 0) {
                if (this.mState == 3) {
                    this.mNotMoving = true;
                    stepIdleStateLocked("s:stationary");
                } else if (this.mState == 4) {
                    this.mNotMoving = true;
                    if (this.mLocated) {
                        stepIdleStateLocked("s:stationary");
                    }
                }
            }
        }
    }

    final class MyHandler extends android.os.Handler {
        MyHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            boolean deepChanged;
            boolean lightChanged;
            int i;
            boolean isStationary;
            com.android.server.DeviceIdleInternal.StationaryListener[] listeners;
            com.android.server.PowerAllowlistInternal.TempAllowlistChangeListener[] listeners2;
            int i2 = 0;
            switch (msg.what) {
                case 1:
                    com.android.server.DeviceIdleController.this.handleWriteConfigFile();
                    return;
                case 2:
                case 3:
                    com.android.server.EventLogTags.writeDeviceIdleOnStart();
                    if (msg.what == 2) {
                        deepChanged = com.android.server.DeviceIdleController.this.mLocalPowerManager.setDeviceIdleMode(true);
                        lightChanged = com.android.server.DeviceIdleController.this.mLocalPowerManager.setLightDeviceIdleMode(false);
                    } else {
                        deepChanged = com.android.server.DeviceIdleController.this.mLocalPowerManager.setDeviceIdleMode(false);
                        lightChanged = com.android.server.DeviceIdleController.this.mLocalPowerManager.setLightDeviceIdleMode(true);
                    }
                    try {
                        com.android.server.DeviceIdleController.this.mNetworkPolicyManager.setDeviceIdleMode(true);
                        com.android.internal.app.IBatteryStats iBatteryStats = com.android.server.DeviceIdleController.this.mBatteryStats;
                        if (msg.what == 2) {
                            i = 2;
                        } else {
                            i = 1;
                        }
                        iBatteryStats.noteDeviceIdleMode(i, (java.lang.String) null, android.os.Process.myUid());
                        break;
                    } catch (android.os.RemoteException e) {
                    }
                    if (deepChanged) {
                        com.android.server.DeviceIdleController.this.getContext().sendBroadcastAsUser(com.android.server.DeviceIdleController.this.mIdleIntent, android.os.UserHandle.ALL, null, com.android.server.DeviceIdleController.this.mIdleIntentOptions);
                    }
                    if (lightChanged) {
                        com.android.server.DeviceIdleController.this.getContext().sendBroadcastAsUser(com.android.server.DeviceIdleController.this.mLightIdleIntent, android.os.UserHandle.ALL, null, com.android.server.DeviceIdleController.this.mLightIdleIntentOptions);
                    }
                    com.android.server.EventLogTags.writeDeviceIdleOnComplete();
                    com.android.server.DeviceIdleController.this.mDeviceIdleExt.onIdleOn(com.android.server.DeviceIdleController.this.mPowerSaveWhitelistUserApps, msg.what == 2);
                    com.android.server.DeviceIdleController.this.mGoingIdleWakeLock.release();
                    return;
                case 4:
                    com.android.server.EventLogTags.writeDeviceIdleOffStart("unknown");
                    boolean deepChanged2 = com.android.server.DeviceIdleController.this.mLocalPowerManager.setDeviceIdleMode(false);
                    boolean lightChanged2 = com.android.server.DeviceIdleController.this.mLocalPowerManager.setLightDeviceIdleMode(false);
                    try {
                        com.android.server.DeviceIdleController.this.mNetworkPolicyManager.setDeviceIdleMode(false);
                        com.android.server.DeviceIdleController.this.mBatteryStats.noteDeviceIdleMode(0, (java.lang.String) null, android.os.Process.myUid());
                        break;
                    } catch (android.os.RemoteException e2) {
                    }
                    if (deepChanged2) {
                        com.android.server.DeviceIdleController.this.incActiveIdleOps();
                        com.android.server.DeviceIdleController.this.mLocalActivityManager.broadcastIntentWithCallback(com.android.server.DeviceIdleController.this.mIdleIntent, com.android.server.DeviceIdleController.this.mIdleStartedDoneReceiver, (java.lang.String[]) null, -1, (int[]) null, (java.util.function.BiFunction) null, com.android.server.DeviceIdleController.this.mIdleIntentOptions);
                        if (com.android.server.DeviceIdleController.this.mState == 6) {
                            com.android.server.DeviceIdleController.this.mDeviceIdleExt.onBroadcastIdleState();
                        }
                    }
                    if (lightChanged2) {
                        com.android.server.DeviceIdleController.this.incActiveIdleOps();
                        com.android.server.DeviceIdleController.this.mLocalActivityManager.broadcastIntentWithCallback(com.android.server.DeviceIdleController.this.mLightIdleIntent, com.android.server.DeviceIdleController.this.mIdleStartedDoneReceiver, (java.lang.String[]) null, -1, (int[]) null, (java.util.function.BiFunction) null, com.android.server.DeviceIdleController.this.mLightIdleIntentOptions);
                    }
                    com.android.server.DeviceIdleController.this.decActiveIdleOps();
                    com.android.server.EventLogTags.writeDeviceIdleOffComplete();
                    return;
                case 5:
                    java.lang.String activeReason = (java.lang.String) msg.obj;
                    int activeUid = msg.arg1;
                    com.android.server.EventLogTags.writeDeviceIdleOffStart(activeReason != null ? activeReason : "unknown");
                    boolean deepChanged3 = com.android.server.DeviceIdleController.this.mLocalPowerManager.setDeviceIdleMode(false);
                    boolean lightChanged3 = com.android.server.DeviceIdleController.this.mLocalPowerManager.setLightDeviceIdleMode(false);
                    try {
                        com.android.server.DeviceIdleController.this.mNetworkPolicyManager.setDeviceIdleMode(false);
                        com.android.server.DeviceIdleController.this.mBatteryStats.noteDeviceIdleMode(0, activeReason, activeUid);
                        break;
                    } catch (android.os.RemoteException e3) {
                    }
                    if (deepChanged3) {
                        com.android.server.DeviceIdleController.this.getContext().sendBroadcastAsUser(com.android.server.DeviceIdleController.this.mIdleIntent, android.os.UserHandle.ALL, null, com.android.server.DeviceIdleController.this.mIdleIntentOptions);
                    }
                    if (lightChanged3) {
                        com.android.server.DeviceIdleController.this.getContext().sendBroadcastAsUser(com.android.server.DeviceIdleController.this.mLightIdleIntent, android.os.UserHandle.ALL, null, com.android.server.DeviceIdleController.this.mLightIdleIntentOptions);
                    }
                    com.android.server.DeviceIdleController.this.mDeviceIdleExt.onIdleExit();
                    com.android.server.EventLogTags.writeDeviceIdleOffComplete();
                    return;
                case 6:
                    com.android.server.DeviceIdleController.this.checkTempAppWhitelistTimeout(msg.arg1);
                    return;
                case 7:
                    com.android.server.DeviceIdleInternal.StationaryListener newListener = (com.android.server.DeviceIdleInternal.StationaryListener) msg.obj;
                    synchronized (com.android.server.DeviceIdleController.this) {
                        isStationary = com.android.server.DeviceIdleController.this.isStationaryLocked();
                        if (newListener == null) {
                            listeners = (com.android.server.DeviceIdleInternal.StationaryListener[]) com.android.server.DeviceIdleController.this.mStationaryListeners.toArray(new com.android.server.DeviceIdleInternal.StationaryListener[com.android.server.DeviceIdleController.this.mStationaryListeners.size()]);
                        } else {
                            listeners = null;
                        }
                        break;
                    }
                    if (listeners != null) {
                        int length = listeners.length;
                        while (i2 < length) {
                            listeners[i2].onDeviceStationaryChanged(isStationary);
                            i2++;
                        }
                    }
                    if (newListener != null) {
                        newListener.onDeviceStationaryChanged(isStationary);
                        return;
                    }
                    return;
                case 8:
                    com.android.server.DeviceIdleController.this.decActiveIdleOps();
                    return;
                case 9:
                case 11:
                case 12:
                default:
                    return;
                case 10:
                    com.android.server.deviceidle.IDeviceIdleConstraint constraint = (com.android.server.deviceidle.IDeviceIdleConstraint) msg.obj;
                    boolean monitoring = msg.arg1 == 1;
                    if (monitoring) {
                        constraint.startMonitoring();
                        return;
                    } else {
                        constraint.stopMonitoring();
                        return;
                    }
                case 13:
                    int uid = msg.arg1;
                    boolean added = msg.arg2 == 1;
                    synchronized (com.android.server.DeviceIdleController.this) {
                        listeners2 = (com.android.server.PowerAllowlistInternal.TempAllowlistChangeListener[]) com.android.server.DeviceIdleController.this.mTempAllowlistChangeListeners.toArray(new com.android.server.PowerAllowlistInternal.TempAllowlistChangeListener[com.android.server.DeviceIdleController.this.mTempAllowlistChangeListeners.size()]);
                        break;
                    }
                    int length2 = listeners2.length;
                    while (i2 < length2) {
                        com.android.server.PowerAllowlistInternal.TempAllowlistChangeListener listener = listeners2[i2];
                        if (added) {
                            listener.onAppAdded(uid);
                        } else {
                            listener.onAppRemoved(uid);
                        }
                        i2++;
                    }
                    return;
                case 14:
                    int appId = msg.arg1;
                    int reasonCode = msg.arg2;
                    java.lang.String reason = (java.lang.String) msg.obj;
                    com.android.server.DeviceIdleController.this.mNetworkPolicyManagerInternal.onTempPowerSaveWhitelistChange(appId, true, reasonCode, reason);
                    return;
                case 15:
                    int appId2 = msg.arg1;
                    com.android.server.DeviceIdleController.this.mNetworkPolicyManagerInternal.onTempPowerSaveWhitelistChange(appId2, false, 0, null);
                    return;
            }
        }
    }

    private final class BinderService extends android.os.IDeviceIdleController.Stub {
        private BinderService() {
        }

        public void addPowerSaveWhitelistApp(java.lang.String name) {
            addPowerSaveWhitelistApps(java.util.Collections.singletonList(name));
        }

        public int addPowerSaveWhitelistApps(java.util.List<java.lang.String> packageNames) {
            com.android.server.DeviceIdleController.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            com.android.server.DeviceIdleController.this.mDeviceIdleExt.addInvalidDozeWhitelist(packageNames);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.DeviceIdleController.this.addPowerSaveWhitelistAppsInternal(packageNames);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void removePowerSaveWhitelistApp(java.lang.String name) {
            com.android.server.DeviceIdleController.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                if (!com.android.server.DeviceIdleController.this.removePowerSaveWhitelistAppInternal(name) && com.android.server.DeviceIdleController.this.mPowerSaveWhitelistAppsExceptIdle.containsKey(name)) {
                    throw new java.lang.UnsupportedOperationException("Cannot remove system whitelisted app");
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void removeSystemPowerWhitelistApp(java.lang.String name) {
            com.android.server.DeviceIdleController.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.DeviceIdleController.this.removeSystemPowerWhitelistAppInternal(name);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public void restoreSystemPowerWhitelistApp(java.lang.String name) {
            com.android.server.DeviceIdleController.this.getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.DeviceIdleController.this.restoreSystemPowerWhitelistAppInternal(name);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        public java.lang.String[] getRemovedSystemPowerWhitelistApps() {
            return com.android.server.DeviceIdleController.this.getRemovedSystemPowerWhitelistAppsInternal(android.os.Binder.getCallingUid(), android.os.UserHandle.getCallingUserId());
        }

        public java.lang.String[] getSystemPowerWhitelistExceptIdle() {
            return com.android.server.DeviceIdleController.this.getSystemPowerWhitelistExceptIdleInternal(android.os.Binder.getCallingUid(), android.os.UserHandle.getCallingUserId());
        }

        public java.lang.String[] getSystemPowerWhitelist() {
            return com.android.server.DeviceIdleController.this.getSystemPowerWhitelistInternal(android.os.Binder.getCallingUid(), android.os.UserHandle.getCallingUserId());
        }

        public java.lang.String[] getUserPowerWhitelist() {
            return com.android.server.DeviceIdleController.this.getUserPowerWhitelistInternal(android.os.Binder.getCallingUid(), android.os.UserHandle.getCallingUserId());
        }

        public java.lang.String[] getFullPowerWhitelistExceptIdle() {
            return com.android.server.DeviceIdleController.this.getFullPowerWhitelistExceptIdleInternal(android.os.Binder.getCallingUid(), android.os.UserHandle.getCallingUserId());
        }

        public java.lang.String[] getFullPowerWhitelist() {
            return com.android.server.DeviceIdleController.this.getFullPowerWhitelistInternal(android.os.Binder.getCallingUid(), android.os.UserHandle.getCallingUserId());
        }

        public int[] getAppIdWhitelistExceptIdle() {
            return com.android.server.DeviceIdleController.this.getAppIdWhitelistExceptIdleInternal();
        }

        public int[] getAppIdWhitelist() {
            return com.android.server.DeviceIdleController.this.getAppIdWhitelistInternal();
        }

        public int[] getAppIdUserWhitelist() {
            return com.android.server.DeviceIdleController.this.getAppIdUserWhitelistInternal();
        }

        public int[] getAppIdTempWhitelist() {
            return com.android.server.DeviceIdleController.this.getAppIdTempWhitelistInternal();
        }

        public boolean isPowerSaveWhitelistExceptIdleApp(java.lang.String name) {
            if (com.android.server.DeviceIdleController.this.mPackageManagerInternal.filterAppAccess(name, android.os.Binder.getCallingUid(), android.os.UserHandle.getCallingUserId())) {
                return false;
            }
            return com.android.server.DeviceIdleController.this.isPowerSaveWhitelistExceptIdleAppInternal(name);
        }

        public boolean isPowerSaveWhitelistApp(java.lang.String name) {
            if (com.android.server.DeviceIdleController.this.mPackageManagerInternal.filterAppAccess(name, android.os.Binder.getCallingUid(), android.os.UserHandle.getCallingUserId())) {
                return false;
            }
            return com.android.server.DeviceIdleController.this.isPowerSaveWhitelistAppInternal(name);
        }

        public long whitelistAppTemporarily(java.lang.String packageName, int userId, int reasonCode, java.lang.String reason) throws android.os.RemoteException {
            long durationMs = java.lang.Math.max(10000L, com.android.server.DeviceIdleController.this.mConstants.MAX_TEMP_APP_ALLOWLIST_DURATION_MS / 2);
            com.android.server.DeviceIdleController.this.addPowerSaveTempAllowlistAppChecked(packageName, durationMs, userId, reasonCode, reason);
            return durationMs;
        }

        public void addPowerSaveTempWhitelistApp(java.lang.String packageName, long duration, int userId, int reasonCode, java.lang.String reason) throws android.os.RemoteException {
            com.android.server.DeviceIdleController.this.addPowerSaveTempAllowlistAppChecked(packageName, duration, userId, reasonCode, reason);
        }

        public long addPowerSaveTempWhitelistAppForMms(java.lang.String packageName, int userId, int reasonCode, java.lang.String reason) throws android.os.RemoteException {
            long durationMs = com.android.server.DeviceIdleController.this.mConstants.MMS_TEMP_APP_ALLOWLIST_DURATION_MS;
            com.android.server.DeviceIdleController.this.addPowerSaveTempAllowlistAppChecked(packageName, durationMs, userId, reasonCode, reason);
            return durationMs;
        }

        public long addPowerSaveTempWhitelistAppForSms(java.lang.String packageName, int userId, int reasonCode, java.lang.String reason) throws android.os.RemoteException {
            long durationMs = com.android.server.DeviceIdleController.this.mConstants.SMS_TEMP_APP_ALLOWLIST_DURATION_MS;
            com.android.server.DeviceIdleController.this.addPowerSaveTempAllowlistAppChecked(packageName, durationMs, userId, reasonCode, reason);
            return durationMs;
        }

        public void exitIdle(java.lang.String reason) {
            exitIdle_enforcePermission();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.DeviceIdleController.this.exitIdleInternal(reason);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            com.android.server.DeviceIdleController.this.dump(fd, pw, args);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            com.android.server.DeviceIdleController.this.new Shell().exec(this, in, out, err, args, callback, resultReceiver);
        }
    }

    private class LocalService implements com.android.server.DeviceIdleInternal {
        private LocalService() {
        }

        public void onConstraintStateChanged(com.android.server.deviceidle.IDeviceIdleConstraint constraint, boolean active) {
            synchronized (com.android.server.DeviceIdleController.this) {
                com.android.server.DeviceIdleController.this.onConstraintStateChangedLocked(constraint, active);
            }
        }

        public void registerDeviceIdleConstraint(com.android.server.deviceidle.IDeviceIdleConstraint constraint, java.lang.String name, int minState) {
            com.android.server.DeviceIdleController.this.registerDeviceIdleConstraintInternal(constraint, name, minState);
        }

        public void unregisterDeviceIdleConstraint(com.android.server.deviceidle.IDeviceIdleConstraint constraint) {
            com.android.server.DeviceIdleController.this.unregisterDeviceIdleConstraintInternal(constraint);
        }

        public void exitIdle(java.lang.String reason) {
            com.android.server.DeviceIdleController.this.exitIdleInternal(reason);
        }

        public void addPowerSaveTempWhitelistApp(int callingUid, java.lang.String packageName, long durationMs, int userId, boolean sync, int reasonCode, java.lang.String reason) throws java.lang.Throwable {
            com.android.server.DeviceIdleController.this.addPowerSaveTempAllowlistAppInternal(callingUid, packageName, durationMs, 0, userId, sync, reasonCode, reason);
        }

        public void addPowerSaveTempWhitelistApp(int callingUid, java.lang.String packageName, long durationMs, int tempAllowListType, int userId, boolean sync, int reasonCode, java.lang.String reason) throws java.lang.Throwable {
            com.android.server.DeviceIdleController.this.addPowerSaveTempAllowlistAppInternal(callingUid, packageName, durationMs, tempAllowListType, userId, sync, reasonCode, reason);
        }

        public void addPowerSaveTempWhitelistAppDirect(int uid, long durationMs, int tempAllowListType, boolean sync, int reasonCode, java.lang.String reason, int callingUid) throws java.lang.Throwable {
            com.android.server.DeviceIdleController.this.addPowerSaveTempWhitelistAppDirectInternal(callingUid, uid, durationMs, tempAllowListType, sync, reasonCode, reason);
        }

        public long getNotificationAllowlistDuration() {
            return com.android.server.DeviceIdleController.this.mConstants.NOTIFICATION_ALLOWLIST_DURATION_MS;
        }

        public void setJobsActive(boolean active) {
            com.android.server.DeviceIdleController.this.setJobsActive(active);
        }

        public void setAlarmsActive(boolean active) {
            com.android.server.DeviceIdleController.this.setAlarmsActive(active);
        }

        public boolean isAppOnWhitelist(int appid) {
            return com.android.server.DeviceIdleController.this.isAppOnWhitelistInternal(appid);
        }

        public java.lang.String[] getFullPowerWhitelistExceptIdle() {
            return com.android.server.DeviceIdleController.this.getFullPowerWhitelistInternalUnchecked();
        }

        public int[] getPowerSaveWhitelistUserAppIds() {
            return com.android.server.DeviceIdleController.this.getPowerSaveWhitelistUserAppIds();
        }

        public int[] getPowerSaveTempWhitelistAppIds() {
            return com.android.server.DeviceIdleController.this.getAppIdTempWhitelistInternal();
        }

        public void registerStationaryListener(com.android.server.DeviceIdleInternal.StationaryListener listener) {
            com.android.server.DeviceIdleController.this.registerStationaryListener(listener);
        }

        public void unregisterStationaryListener(com.android.server.DeviceIdleInternal.StationaryListener listener) {
            com.android.server.DeviceIdleController.this.unregisterStationaryListener(listener);
        }

        public int getTempAllowListType(int reasonCode, int defaultType) {
            return com.android.server.DeviceIdleController.this.getTempAllowListType(reasonCode, defaultType);
        }
    }

    private class LocalPowerAllowlistService implements com.android.server.PowerAllowlistInternal {
        private LocalPowerAllowlistService() {
        }

        public void registerTempAllowlistChangeListener(com.android.server.PowerAllowlistInternal.TempAllowlistChangeListener listener) {
            com.android.server.DeviceIdleController.this.registerTempAllowlistChangeListener(listener);
        }

        public void unregisterTempAllowlistChangeListener(com.android.server.PowerAllowlistInternal.TempAllowlistChangeListener listener) {
            com.android.server.DeviceIdleController.this.unregisterTempAllowlistChangeListener(listener);
        }
    }

    private class EmergencyCallListener extends android.telephony.TelephonyCallback implements android.telephony.TelephonyCallback.OutgoingEmergencyCallListener, android.telephony.TelephonyCallback.CallStateListener {
        private volatile boolean mIsEmergencyCallActive;

        private EmergencyCallListener() {
        }

        public void onOutgoingEmergencyCall(android.telephony.emergency.EmergencyNumber placedEmergencyNumber, int subscriptionId) {
            this.mIsEmergencyCallActive = true;
            synchronized (com.android.server.DeviceIdleController.this) {
                com.android.server.DeviceIdleController.this.mActiveReason = 8;
                com.android.server.DeviceIdleController.this.becomeActiveLocked("emergency call", android.os.Process.myUid());
            }
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int state) {
            if (state == 0 && this.mIsEmergencyCallActive) {
                this.mIsEmergencyCallActive = false;
                synchronized (com.android.server.DeviceIdleController.this) {
                    com.android.server.DeviceIdleController.this.becomeInactiveIfAppropriateLocked();
                }
            }
        }

        boolean isEmergencyCallActive() {
            return this.mIsEmergencyCallActive;
        }
    }

    static class Injector {
        private android.net.ConnectivityManager mConnectivityManager;
        private com.android.server.DeviceIdleController.Constants mConstants;
        private final android.content.Context mContext;
        private android.location.LocationManager mLocationManager;

        Injector(android.content.Context ctx) {
            this.mContext = ctx.createAttributionContext(com.android.server.DeviceIdleController.TAG);
        }

        android.app.AlarmManager getAlarmManager() {
            return (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        }

        com.android.server.AnyMotionDetector getAnyMotionDetector(android.os.Handler handler, android.hardware.SensorManager sm, com.android.server.AnyMotionDetector.DeviceIdleCallback callback, float angleThreshold) {
            return new com.android.server.AnyMotionDetector(getPowerManager(), handler, sm, callback, angleThreshold);
        }

        com.android.server.AppStateTrackerImpl getAppStateTracker(android.content.Context ctx, android.os.Looper looper) {
            return new com.android.server.AppStateTrackerImpl(ctx, looper);
        }

        android.net.ConnectivityManager getConnectivityManager() {
            if (this.mConnectivityManager == null) {
                this.mConnectivityManager = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
            }
            return this.mConnectivityManager;
        }

        com.android.server.DeviceIdleController.Constants getConstants(com.android.server.DeviceIdleController controller, android.os.Handler handler, android.content.ContentResolver resolver) {
            if (this.mConstants == null) {
                java.util.Objects.requireNonNull(controller);
                this.mConstants = controller.new Constants(handler, resolver);
            }
            return this.mConstants;
        }

        long getElapsedRealtime() {
            return android.os.SystemClock.elapsedRealtime();
        }

        android.location.LocationManager getLocationManager() {
            if (this.mLocationManager == null) {
                this.mLocationManager = (android.location.LocationManager) this.mContext.getSystemService(android.location.LocationManager.class);
            }
            return this.mLocationManager;
        }

        com.android.server.DeviceIdleController.MyHandler getHandler(com.android.server.DeviceIdleController controller) {
            java.util.Objects.requireNonNull(controller);
            return controller.new MyHandler(com.android.server.AppSchedulingModuleThread.getHandler().getLooper());
        }

        android.hardware.Sensor getMotionSensor() {
            android.hardware.SensorManager sensorManager = getSensorManager();
            android.hardware.Sensor motionSensor = null;
            int sigMotionSensorId = this.mContext.getResources().getInteger(android.R.integer.config_autoBrightnessBrighteningLightDebounce);
            if (sigMotionSensorId > 0) {
                motionSensor = sensorManager.getDefaultSensor(sigMotionSensorId, true);
            }
            if (motionSensor == null && this.mContext.getResources().getBoolean(android.R.bool.config_attachNavBarToAppDuringTransition)) {
                motionSensor = sensorManager.getDefaultSensor(26, true);
            }
            if (motionSensor == null) {
                android.hardware.Sensor motionSensor2 = sensorManager.getDefaultSensor(17, true);
                return motionSensor2;
            }
            return motionSensor;
        }

        android.os.PowerManager getPowerManager() {
            return (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
        }

        android.hardware.SensorManager getSensorManager() {
            return (android.hardware.SensorManager) this.mContext.getSystemService(android.hardware.SensorManager.class);
        }

        android.telephony.TelephonyManager getTelephonyManager() {
            return (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
        }

        com.android.server.deviceidle.ConstraintController getConstraintController(android.os.Handler handler, com.android.server.DeviceIdleInternal localService) {
            if (this.mContext.getPackageManager().hasSystemFeature("android.software.leanback_only")) {
                return new com.android.server.deviceidle.TvConstraintController(this.mContext, handler);
            }
            return null;
        }

        boolean isLocationPrefetchEnabled() {
            return !com.android.server.deviceidle.Flags.removeIdleLocation() && this.mContext.getResources().getBoolean(android.R.bool.config_audio_ringer_mode_affects_alarm_stream);
        }

        boolean useMotionSensor() {
            return this.mContext.getResources().getBoolean(android.R.bool.config_autoBrightnessResetAmbientLuxAfterWarmUp);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    DeviceIdleController(android.content.Context context, com.android.server.DeviceIdleController.Injector injector) {
        super(context);
        this.mNumBlockingConstraints = 0;
        this.mConstraints = new android.util.ArrayMap<>();
        this.mPowerSaveWhitelistAppsExceptIdle = new android.util.ArrayMap<>();
        this.mPowerSaveWhitelistUserAppsExceptIdle = new android.util.ArraySet<>();
        this.mPowerSaveWhitelistApps = new android.util.ArrayMap<>();
        this.mPowerSaveWhitelistUserApps = new android.util.ArrayMap<>();
        this.mPowerSaveWhitelistSystemAppIdsExceptIdle = new android.util.SparseBooleanArray();
        this.mPowerSaveWhitelistSystemAppIds = new android.util.SparseBooleanArray();
        this.mPowerSaveWhitelistExceptIdleAppIds = new android.util.SparseBooleanArray();
        this.mPowerSaveWhitelistExceptIdleAppIdArray = new int[0];
        this.mPowerSaveWhitelistAllAppIds = new android.util.SparseBooleanArray();
        this.mPowerSaveWhitelistAllAppIdArray = new int[0];
        this.mPowerSaveWhitelistUserAppIds = new android.util.SparseBooleanArray();
        this.mPowerSaveWhitelistUserAppIdArray = new int[0];
        this.mTempWhitelistAppIdEndTimes = new android.util.SparseArray<>();
        this.mTempWhitelistAppIdArray = new int[0];
        this.mRemovedFromSystemWhitelistApps = new android.util.ArrayMap<>();
        this.mStationaryListeners = new android.util.ArraySet<>();
        this.mTempAllowlistChangeListeners = new android.util.ArraySet<>();
        this.mEventCmds = new int[100];
        this.mEventTimes = new long[100];
        this.mEventReasons = new java.lang.String[100];
        this.mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.DeviceIdleController.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:14:0x002c  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r7, android.content.Intent r8) {
                /*
                    r6 = this;
                    java.lang.String r0 = r8.getAction()
                    int r1 = r0.hashCode()
                    r2 = 1
                    r3 = 0
                    switch(r1) {
                        case -1538406691: goto L22;
                        case -1172645946: goto L18;
                        case 525384130: goto Le;
                        default: goto Ld;
                    }
                Ld:
                    goto L2c
                Le:
                    java.lang.String r1 = "android.intent.action.PACKAGE_REMOVED"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Ld
                    r0 = 2
                    goto L2d
                L18:
                    java.lang.String r1 = "android.net.conn.CONNECTIVITY_CHANGE"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Ld
                    r0 = r3
                    goto L2d
                L22:
                    java.lang.String r1 = "android.intent.action.BATTERY_CHANGED"
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto Ld
                    r0 = r2
                    goto L2d
                L2c:
                    r0 = -1
                L2d:
                    switch(r0) {
                        case 0: goto L7b;
                        case 1: goto L55;
                        case 2: goto L31;
                        default: goto L30;
                    }
                L30:
                    goto L81
                L31:
                    com.android.server.DeviceIdleController r0 = com.android.server.DeviceIdleController.this
                    com.android.server.IDeviceIdleControllerExt r0 = com.android.server.DeviceIdleController.m124$$Nest$fgetmDeviceIdleExt(r0)
                    r0.removePackage(r8)
                    java.lang.String r0 = "android.intent.extra.REPLACING"
                    boolean r0 = r8.getBooleanExtra(r0, r3)
                    if (r0 != 0) goto L81
                    android.net.Uri r0 = r8.getData()
                    if (r0 == 0) goto L81
                    java.lang.String r1 = r0.getSchemeSpecificPart()
                    r2 = r1
                    if (r1 == 0) goto L81
                    com.android.server.DeviceIdleController r1 = com.android.server.DeviceIdleController.this
                    r1.removePowerSaveWhitelistAppInternal(r2)
                    goto L81
                L55:
                    java.lang.String r0 = "present"
                    boolean r0 = r8.getBooleanExtra(r0, r2)
                    java.lang.String r1 = "plugged"
                    int r1 = r8.getIntExtra(r1, r3)
                    if (r1 == 0) goto L67
                    r1 = r2
                    goto L68
                L67:
                    r1 = r3
                L68:
                    com.android.server.DeviceIdleController r4 = com.android.server.DeviceIdleController.this
                    monitor-enter(r4)
                    com.android.server.DeviceIdleController r5 = com.android.server.DeviceIdleController.this     // Catch: java.lang.Throwable -> L78
                    if (r0 == 0) goto L72
                    if (r1 == 0) goto L72
                    goto L73
                L72:
                    r2 = r3
                L73:
                    r5.updateChargingLocked(r2)     // Catch: java.lang.Throwable -> L78
                    monitor-exit(r4)     // Catch: java.lang.Throwable -> L78
                    goto L81
                L78:
                    r2 = move-exception
                    monitor-exit(r4)     // Catch: java.lang.Throwable -> L78
                    throw r2
                L7b:
                    com.android.server.DeviceIdleController r0 = com.android.server.DeviceIdleController.this
                    r0.updateConnectivityState(r8)
                L81:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.DeviceIdleController.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        this.mLightAlarmListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda2
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.lambda$new$0();
            }
        };
        this.mMotionRegistrationAlarmListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda3
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.lambda$new$1();
            }
        };
        this.mMotionTimeoutAlarmListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda4
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.lambda$new$2();
            }
        };
        this.mSensingTimeoutAlarmListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.DeviceIdleController.2
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                synchronized (com.android.server.DeviceIdleController.this) {
                    if (com.android.server.DeviceIdleController.this.mState == 3) {
                        com.android.server.DeviceIdleController.this.becomeInactiveIfAppropriateLocked();
                    }
                }
            }
        };
        this.mDeepAlarmListener = new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.DeviceIdleController.3
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                synchronized (com.android.server.DeviceIdleController.this) {
                    com.android.server.DeviceIdleController.this.stepIdleStateLocked("s:alarm");
                }
            }
        };
        this.mIdleStartedDoneReceiver = new android.content.IIntentReceiver.Stub() { // from class: com.android.server.DeviceIdleController.4
            public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
                if ("android.os.action.DEVICE_IDLE_MODE_CHANGED".equals(intent.getAction())) {
                    com.android.server.DeviceIdleController.this.mHandler.sendEmptyMessageDelayed(8, com.android.server.DeviceIdleController.this.mConstants.MIN_DEEP_MAINTENANCE_TIME);
                } else {
                    com.android.server.DeviceIdleController.this.mHandler.sendEmptyMessageDelayed(8, com.android.server.DeviceIdleController.this.mConstants.MIN_LIGHT_MAINTENANCE_TIME);
                }
            }
        };
        this.mInteractivityReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.DeviceIdleController.5
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                synchronized (com.android.server.DeviceIdleController.this) {
                    com.android.server.DeviceIdleController.this.updateInteractivityLocked();
                }
            }
        };
        this.mEmergencyCallListener = new com.android.server.DeviceIdleController.EmergencyCallListener();
        this.mModeManagerQuickDozeRequestConsumer = new com.android.server.DeviceIdleController.ModeManagerQuickDozeRequestConsumer();
        this.mModeManagerOffBodyStateConsumer = new com.android.server.DeviceIdleController.ModeManagerOffBodyStateConsumer();
        this.mMotionListener = new com.android.server.DeviceIdleController.MotionListener();
        this.mGenericLocationListener = new android.location.LocationListener() { // from class: com.android.server.DeviceIdleController.6
            @Override // android.location.LocationListener
            public void onLocationChanged(android.location.Location location) {
                synchronized (com.android.server.DeviceIdleController.this) {
                    com.android.server.DeviceIdleController.this.receivedGenericLocationLocked(location);
                }
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(java.lang.String provider, int status, android.os.Bundle extras) {
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(java.lang.String provider) {
            }

            @Override // android.location.LocationListener
            public void onProviderDisabled(java.lang.String provider) {
            }
        };
        this.mGpsLocationListener = new android.location.LocationListener() { // from class: com.android.server.DeviceIdleController.7
            @Override // android.location.LocationListener
            public void onLocationChanged(android.location.Location location) {
                synchronized (com.android.server.DeviceIdleController.this) {
                    com.android.server.DeviceIdleController.this.receivedGpsLocationLocked(location);
                }
            }

            @Override // android.location.LocationListener
            public void onStatusChanged(java.lang.String provider, int status, android.os.Bundle extras) {
            }

            @Override // android.location.LocationListener
            public void onProviderEnabled(java.lang.String provider) {
            }

            @Override // android.location.LocationListener
            public void onProviderDisabled(java.lang.String provider) {
            }
        };
        this.mScreenObserver = new com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver() { // from class: com.android.server.DeviceIdleController.8
            @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
            public void onAwakeStateChanged(boolean isAwake) {
            }

            @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
            public void onKeyguardStateChanged(boolean isShowing) {
                synchronized (com.android.server.DeviceIdleController.this) {
                    com.android.server.DeviceIdleController.this.keyguardShowingLocked(isShowing);
                }
            }
        };
        this.mDeviceIdleControllerWrapper = new com.android.server.DeviceIdleController.DeviceIdleControllerWrapper();
        this.mDeviceIdleExt = (com.android.server.IDeviceIdleControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.IDeviceIdleControllerExt.class).base(this).create();
        this.mInjector = injector;
        this.mConfigFile = new android.util.AtomicFile(new java.io.File(getSystemDir(), "deviceidle.xml"));
        this.mHandler = this.mInjector.getHandler(this);
        this.mAppStateTracker = this.mInjector.getAppStateTracker(context, com.android.server.AppSchedulingModuleThread.get().getLooper());
        com.android.server.LocalServices.addService(com.android.server.AppStateTracker.class, this.mAppStateTracker);
        this.mIsLocationPrefetchEnabled = this.mInjector.isLocationPrefetchEnabled();
        this.mUseMotionSensor = this.mInjector.useMotionSensor();
        this.mDeviceIdleExt.init(context);
    }

    public DeviceIdleController(android.content.Context context) {
        this(context, new com.android.server.DeviceIdleController.Injector(context));
    }

    boolean isAppOnWhitelistInternal(int appid) {
        boolean z;
        synchronized (this) {
            z = java.util.Arrays.binarySearch(this.mPowerSaveWhitelistAllAppIdArray, appid) >= 0;
        }
        return z;
    }

    int[] getPowerSaveWhitelistUserAppIds() {
        int[] iArr;
        synchronized (this) {
            iArr = this.mPowerSaveWhitelistUserAppIdArray;
        }
        return iArr;
    }

    private static java.io.File getSystemDir() {
        return new java.io.File(android.os.Environment.getDataDirectory(), "system");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.SystemService
    public void onStart() {
        android.content.pm.PackageManager packageManager = getContext().getPackageManager();
        synchronized (this) {
            boolean z = getContext().getResources().getBoolean(android.R.bool.config_enableActivityRecognitionHardwareOverlay);
            this.mDeepEnabled = z;
            this.mLightEnabled = z;
            com.android.server.SystemConfig systemConfig = com.android.server.SystemConfig.getInstance();
            android.util.ArraySet<java.lang.String> allowInPowerSaveExceptIdle = systemConfig.getAllowInPowerSaveExceptIdle();
            for (int i = 0; i < allowInPowerSaveExceptIdle.size(); i++) {
                try {
                    android.content.pm.ApplicationInfo applicationInfo = packageManager.getApplicationInfo(allowInPowerSaveExceptIdle.valueAt(i), 1048576);
                    int appId = android.os.UserHandle.getAppId(applicationInfo.uid);
                    this.mPowerSaveWhitelistAppsExceptIdle.put(applicationInfo.packageName, java.lang.Integer.valueOf(appId));
                    this.mPowerSaveWhitelistSystemAppIdsExceptIdle.put(appId, true);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                }
            }
            android.util.ArraySet<java.lang.String> allowInPowerSave = systemConfig.getAllowInPowerSave();
            for (int i2 = 0; i2 < allowInPowerSave.size(); i2++) {
                try {
                    android.content.pm.ApplicationInfo applicationInfo2 = packageManager.getApplicationInfo(allowInPowerSave.valueAt(i2), 1048576);
                    int appId2 = android.os.UserHandle.getAppId(applicationInfo2.uid);
                    this.mPowerSaveWhitelistAppsExceptIdle.put(applicationInfo2.packageName, java.lang.Integer.valueOf(appId2));
                    this.mPowerSaveWhitelistSystemAppIdsExceptIdle.put(appId2, true);
                    this.mPowerSaveWhitelistApps.put(applicationInfo2.packageName, java.lang.Integer.valueOf(appId2));
                    this.mPowerSaveWhitelistSystemAppIds.put(appId2, true);
                } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                }
            }
            this.mConstants = this.mInjector.getConstants(this, this.mHandler, getContext().getContentResolver());
            readConfigFileLocked();
            updateWhitelistAppIdsLocked();
            this.mNetworkConnected = true;
            this.mScreenOn = true;
            this.mScreenLocked = false;
            this.mCharging = true;
            this.mActiveReason = 0;
            moveToStateLocked(0, "boot");
            moveToLightStateLocked(0, "boot");
            this.mInactiveTimeout = this.mConstants.INACTIVE_TIMEOUT;
        }
        this.mBinderService = new com.android.server.DeviceIdleController.BinderService();
        publishBinderService("deviceidle", this.mBinderService);
        this.mLocalService = new com.android.server.DeviceIdleController.LocalService();
        publishLocalService(com.android.server.DeviceIdleInternal.class, this.mLocalService);
        publishLocalService(com.android.server.PowerAllowlistInternal.class, new com.android.server.DeviceIdleController.LocalPowerAllowlistService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        android.os.WearModeManagerInternal modeManagerInternal;
        if (phase == 500) {
            synchronized (this) {
                android.util.Slog.d(TAG, "onBootPhase PHASE_SYSTEM_SERVICES_READY");
                this.mAlarmManager = this.mInjector.getAlarmManager();
                this.mLocalAlarmManager = (com.android.server.AlarmManagerInternal) getLocalService(com.android.server.AlarmManagerInternal.class);
                this.mBatteryStats = com.android.server.am.BatteryStatsService.getService();
                this.mLocalActivityManager = (android.app.ActivityManagerInternal) getLocalService(android.app.ActivityManagerInternal.class);
                this.mLocalActivityTaskManager = (com.android.server.wm.ActivityTaskManagerInternal) getLocalService(com.android.server.wm.ActivityTaskManagerInternal.class);
                this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) getLocalService(android.content.pm.PackageManagerInternal.class);
                this.mLocalPowerManager = (android.os.PowerManagerInternal) getLocalService(android.os.PowerManagerInternal.class);
                this.mPowerManager = this.mInjector.getPowerManager();
                this.mActiveIdleWakeLock = this.mPowerManager.newWakeLock(1, "deviceidle_maint");
                this.mActiveIdleWakeLock.setReferenceCounted(false);
                this.mGoingIdleWakeLock = this.mPowerManager.newWakeLock(1, "deviceidle_going_idle");
                this.mGoingIdleWakeLock.setReferenceCounted(true);
                this.mNetworkPolicyManager = android.net.INetworkPolicyManager.Stub.asInterface(android.os.ServiceManager.getService("netpolicy"));
                this.mNetworkPolicyManagerInternal = (com.android.server.net.NetworkPolicyManagerInternal) getLocalService(com.android.server.net.NetworkPolicyManagerInternal.class);
                this.mSensorManager = this.mInjector.getSensorManager();
                if (this.mUseMotionSensor) {
                    this.mMotionSensor = this.mInjector.getMotionSensor();
                }
                if (this.mIsLocationPrefetchEnabled) {
                    this.mLocationRequest = new android.location.LocationRequest.Builder(0L).setQuality(100).setMaxUpdates(1).build();
                }
                this.mConstraintController = this.mInjector.getConstraintController(this.mHandler, (com.android.server.DeviceIdleInternal) getLocalService(com.android.server.DeviceIdleController.LocalService.class));
                if (this.mConstraintController != null) {
                    this.mConstraintController.start();
                }
                float angleThreshold = getContext().getResources().getInteger(android.R.integer.config_autoBrightnessDarkeningLightDebounce) / 100.0f;
                this.mAnyMotionDetector = this.mInjector.getAnyMotionDetector(this.mHandler, this.mSensorManager, this, angleThreshold);
                this.mAppStateTracker.onSystemServicesReady();
                this.mDeviceIdleExt.initCustomizeDozeModeState();
                android.os.Bundle mostRecentDeliveryOptions = android.app.BroadcastOptions.makeBasic().setDeliveryGroupPolicy(1).setDeferralPolicy(2).toBundle();
                this.mIdleIntent = new android.content.Intent("android.os.action.DEVICE_IDLE_MODE_CHANGED");
                this.mIdleIntent.addFlags(1342177280);
                this.mLightIdleIntent = new android.content.Intent("android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED");
                this.mLightIdleIntent.addFlags(1342177280);
                this.mLightIdleIntentOptions = mostRecentDeliveryOptions;
                this.mIdleIntentOptions = mostRecentDeliveryOptions;
                this.mPowerSaveWhitelistChangedIntent = new android.content.Intent("android.os.action.POWER_SAVE_WHITELIST_CHANGED");
                this.mPowerSaveWhitelistChangedIntent.addFlags(1073741824);
                this.mPowerSaveTempWhitelistChangedIntent = new android.content.Intent("android.os.action.POWER_SAVE_TEMP_WHITELIST_CHANGED");
                this.mPowerSaveTempWhitelistChangedIntent.addFlags(1073741824);
                this.mPowerSaveWhitelistChangedOptions = mostRecentDeliveryOptions;
                this.mPowerSaveTempWhilelistChangedOptions = mostRecentDeliveryOptions;
                android.content.IntentFilter filter = new android.content.IntentFilter();
                filter.addAction("android.intent.action.BATTERY_CHANGED");
                getContext().registerReceiver(this.mReceiver, filter);
                android.content.IntentFilter filter2 = new android.content.IntentFilter();
                filter2.addAction("android.intent.action.PACKAGE_REMOVED");
                filter2.addDataScheme("package");
                getContext().registerReceiver(this.mReceiver, filter2);
                android.content.IntentFilter filter3 = new android.content.IntentFilter();
                filter3.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                getContext().registerReceiver(this.mReceiver, filter3);
                android.content.IntentFilter filter4 = new android.content.IntentFilter();
                filter4.addAction("android.intent.action.SCREEN_OFF");
                filter4.addAction("android.intent.action.SCREEN_ON");
                getContext().registerReceiver(this.mInteractivityReceiver, filter4);
                this.mLocalActivityManager.setDeviceIdleAllowlist(this.mPowerSaveWhitelistAllAppIdArray, this.mPowerSaveWhitelistExceptIdleAppIdArray);
                this.mLocalPowerManager.setDeviceIdleWhitelist(this.mPowerSaveWhitelistAllAppIdArray);
                if (this.mConstants.USE_MODE_MANAGER && (modeManagerInternal = (android.os.WearModeManagerInternal) com.android.server.LocalServices.getService(android.os.WearModeManagerInternal.class)) != null) {
                    modeManagerInternal.addActiveStateChangeListener("quick_doze_request", com.android.server.AppSchedulingModuleThread.getExecutor(), this.mModeManagerQuickDozeRequestConsumer);
                    modeManagerInternal.addActiveStateChangeListener("off_body", com.android.server.AppSchedulingModuleThread.getExecutor(), this.mModeManagerOffBodyStateConsumer);
                }
                this.mLocalPowerManager.registerLowPowerModeObserver(15, new java.util.function.Consumer() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda13
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$onBootPhase$3((android.os.PowerSaveState) obj);
                    }
                });
                this.mBatterySaverEnabled = this.mLocalPowerManager.getLowPowerState(15).batterySaverEnabled;
                updateQuickDozeFlagLocked();
                this.mLocalActivityTaskManager.registerScreenObserver(this.mScreenObserver);
                this.mInjector.getTelephonyManager().registerTelephonyCallback(com.android.server.AppSchedulingModuleThread.getExecutor(), this.mEmergencyCallListener);
                passWhiteListsToForceAppStandbyTrackerLocked();
                updateInteractivityLocked();
            }
            updateConnectivityState(null);
        }
        this.mDeviceIdleExt.hookonBootPhase(phase, getContext(), this, this.mConstants);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$3(android.os.PowerSaveState state) {
        synchronized (this) {
            this.mBatterySaverEnabled = state.batterySaverEnabled;
            updateQuickDozeFlagLocked();
        }
    }

    boolean hasMotionSensor() {
        return this.mUseMotionSensor && this.mMotionSensor != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDeviceIdleConstraintInternal(com.android.server.deviceidle.IDeviceIdleConstraint constraint, java.lang.String name, int type) {
        int minState;
        switch (type) {
            case 0:
                minState = 0;
                break;
            case 1:
                minState = 3;
                break;
            default:
                android.util.Slog.wtf(TAG, "Registering device-idle constraint with invalid type: " + type);
                return;
        }
        synchronized (this) {
            if (this.mConstraints.containsKey(constraint)) {
                android.util.Slog.e(TAG, "Re-registering device-idle constraint: " + constraint + ".");
                return;
            }
            com.android.server.deviceidle.DeviceIdleConstraintTracker tracker = new com.android.server.deviceidle.DeviceIdleConstraintTracker(name, minState);
            this.mConstraints.put(constraint, tracker);
            updateActiveConstraintsLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterDeviceIdleConstraintInternal(com.android.server.deviceidle.IDeviceIdleConstraint constraint) {
        synchronized (this) {
            onConstraintStateChangedLocked(constraint, false);
            setConstraintMonitoringLocked(constraint, false);
            this.mConstraints.remove(constraint);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConstraintStateChangedLocked(com.android.server.deviceidle.IDeviceIdleConstraint constraint, boolean active) {
        com.android.server.deviceidle.DeviceIdleConstraintTracker tracker = this.mConstraints.get(constraint);
        if (tracker == null) {
            android.util.Slog.e(TAG, "device-idle constraint " + constraint + " has not been registered.");
            return;
        }
        if (active != tracker.active && tracker.monitoring) {
            tracker.active = active;
            this.mNumBlockingConstraints += tracker.active ? 1 : -1;
            if (this.mNumBlockingConstraints == 0) {
                if (this.mState == 0) {
                    becomeInactiveIfAppropriateLocked();
                } else if (this.mNextAlarmTime == 0 || this.mNextAlarmTime < android.os.SystemClock.elapsedRealtime()) {
                    stepIdleStateLocked("s:" + tracker.name);
                }
            }
        }
    }

    private void setConstraintMonitoringLocked(com.android.server.deviceidle.IDeviceIdleConstraint iDeviceIdleConstraint, boolean z) {
        com.android.server.deviceidle.DeviceIdleConstraintTracker deviceIdleConstraintTracker = this.mConstraints.get(iDeviceIdleConstraint);
        if (deviceIdleConstraintTracker.monitoring != z) {
            deviceIdleConstraintTracker.monitoring = z;
            updateActiveConstraintsLocked();
            this.mHandler.obtainMessage(10, z ? 1 : 0, -1, iDeviceIdleConstraint).sendToTarget();
        }
    }

    private void updateActiveConstraintsLocked() {
        this.mNumBlockingConstraints = 0;
        for (int i = 0; i < this.mConstraints.size(); i++) {
            com.android.server.deviceidle.IDeviceIdleConstraint constraint = this.mConstraints.keyAt(i);
            com.android.server.deviceidle.DeviceIdleConstraintTracker tracker = this.mConstraints.valueAt(i);
            boolean monitoring = tracker.minState == this.mState;
            if (monitoring != tracker.monitoring) {
                setConstraintMonitoringLocked(constraint, monitoring);
                tracker.active = monitoring;
            }
            if (tracker.monitoring && tracker.active) {
                this.mNumBlockingConstraints++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int addPowerSaveWhitelistAppsInternal(java.util.List<java.lang.String> pkgNames) {
        int numAdded = 0;
        int numErrors = 0;
        synchronized (this) {
            for (int i = pkgNames.size() - 1; i >= 0; i--) {
                java.lang.String name = pkgNames.get(i);
                if (name == null) {
                    numErrors++;
                } else {
                    try {
                        android.content.pm.ApplicationInfo ai = getContext().getPackageManager().getApplicationInfo(name, 4194304);
                        if (this.mPowerSaveWhitelistUserApps.put(name, java.lang.Integer.valueOf(android.os.UserHandle.getAppId(ai.uid))) == null) {
                            numAdded++;
                            com.android.modules.expresslog.Counter.logIncrement(USER_ALLOWLIST_ADDITION_METRIC_ID);
                        }
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        android.util.Slog.e(TAG, "Tried to add unknown package to power save whitelist: " + name);
                        numErrors++;
                    }
                }
            }
            if (numAdded > 0) {
                reportPowerSaveWhitelistChangedLocked();
                updateWhitelistAppIdsLocked();
                writeConfigFileLocked();
            }
        }
        return pkgNames.size() - numErrors;
    }

    public boolean removePowerSaveWhitelistAppInternal(java.lang.String name) {
        synchronized (this) {
            if (this.mPowerSaveWhitelistUserApps.remove(name) != null) {
                reportPowerSaveWhitelistChangedLocked();
                updateWhitelistAppIdsLocked();
                writeConfigFileLocked();
                com.android.modules.expresslog.Counter.logIncrement(USER_ALLOWLIST_REMOVAL_METRIC_ID);
                return true;
            }
            return false;
        }
    }

    public boolean getPowerSaveWhitelistAppInternal(java.lang.String name) {
        boolean zContainsKey;
        synchronized (this) {
            zContainsKey = this.mPowerSaveWhitelistUserApps.containsKey(name);
        }
        return zContainsKey;
    }

    void resetSystemPowerWhitelistInternal() {
        synchronized (this) {
            this.mPowerSaveWhitelistApps.putAll((android.util.ArrayMap<? extends java.lang.String, ? extends java.lang.Integer>) this.mRemovedFromSystemWhitelistApps);
            this.mRemovedFromSystemWhitelistApps.clear();
            reportPowerSaveWhitelistChangedLocked();
            updateWhitelistAppIdsLocked();
            writeConfigFileLocked();
        }
    }

    public boolean restoreSystemPowerWhitelistAppInternal(java.lang.String name) {
        synchronized (this) {
            if (!this.mRemovedFromSystemWhitelistApps.containsKey(name)) {
                return false;
            }
            this.mPowerSaveWhitelistApps.put(name, this.mRemovedFromSystemWhitelistApps.remove(name));
            reportPowerSaveWhitelistChangedLocked();
            updateWhitelistAppIdsLocked();
            writeConfigFileLocked();
            return true;
        }
    }

    public boolean removeSystemPowerWhitelistAppInternal(java.lang.String name) {
        synchronized (this) {
            if (!this.mPowerSaveWhitelistApps.containsKey(name)) {
                return false;
            }
            this.mRemovedFromSystemWhitelistApps.put(name, this.mPowerSaveWhitelistApps.remove(name));
            reportPowerSaveWhitelistChangedLocked();
            updateWhitelistAppIdsLocked();
            writeConfigFileLocked();
            return true;
        }
    }

    public boolean addPowerSaveWhitelistExceptIdleInternal(java.lang.String name) {
        synchronized (this) {
            try {
                try {
                    android.content.pm.ApplicationInfo ai = getContext().getPackageManager().getApplicationInfo(name, 4194304);
                    if (this.mPowerSaveWhitelistAppsExceptIdle.put(name, java.lang.Integer.valueOf(android.os.UserHandle.getAppId(ai.uid))) == null) {
                        this.mPowerSaveWhitelistUserAppsExceptIdle.add(name);
                        reportPowerSaveWhitelistChangedLocked();
                        this.mPowerSaveWhitelistExceptIdleAppIdArray = buildAppIdArray(this.mPowerSaveWhitelistAppsExceptIdle, this.mPowerSaveWhitelistUserApps, this.mPowerSaveWhitelistExceptIdleAppIds);
                        passWhiteListsToForceAppStandbyTrackerLocked();
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    return false;
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
        return true;
    }

    public void resetPowerSaveWhitelistExceptIdleInternal() {
        synchronized (this) {
            if (this.mPowerSaveWhitelistAppsExceptIdle.removeAll(this.mPowerSaveWhitelistUserAppsExceptIdle)) {
                reportPowerSaveWhitelistChangedLocked();
                this.mPowerSaveWhitelistExceptIdleAppIdArray = buildAppIdArray(this.mPowerSaveWhitelistAppsExceptIdle, this.mPowerSaveWhitelistUserApps, this.mPowerSaveWhitelistExceptIdleAppIds);
                this.mPowerSaveWhitelistUserAppsExceptIdle.clear();
                passWhiteListsToForceAppStandbyTrackerLocked();
            }
        }
    }

    public boolean getPowerSaveWhitelistExceptIdleInternal(java.lang.String name) {
        boolean zContainsKey;
        synchronized (this) {
            zContainsKey = this.mPowerSaveWhitelistAppsExceptIdle.containsKey(name);
        }
        return zContainsKey;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String[] getSystemPowerWhitelistExceptIdleInternal(final int callingUid, final int callingUserId) {
        java.lang.String[] apps;
        synchronized (this) {
            int size = this.mPowerSaveWhitelistAppsExceptIdle.size();
            apps = new java.lang.String[size];
            for (int i = 0; i < size; i++) {
                apps[i] = this.mPowerSaveWhitelistAppsExceptIdle.keyAt(i);
            }
        }
        return (java.lang.String[]) com.android.internal.util.ArrayUtils.filter(apps, new java.util.function.IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i2) {
                return com.android.server.DeviceIdleController.lambda$getSystemPowerWhitelistExceptIdleInternal$4(i2);
            }
        }, new java.util.function.Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getSystemPowerWhitelistExceptIdleInternal$5(callingUid, callingUserId, (java.lang.String) obj);
            }
        });
    }

    static /* synthetic */ java.lang.String[] lambda$getSystemPowerWhitelistExceptIdleInternal$4(int x$0) {
        return new java.lang.String[x$0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getSystemPowerWhitelistExceptIdleInternal$5(int callingUid, int callingUserId, java.lang.String pkg) {
        return !this.mPackageManagerInternal.filterAppAccess(pkg, callingUid, callingUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String[] getSystemPowerWhitelistInternal(final int callingUid, final int callingUserId) {
        java.lang.String[] apps;
        synchronized (this) {
            int size = this.mPowerSaveWhitelistApps.size();
            apps = new java.lang.String[size];
            for (int i = 0; i < size; i++) {
                apps[i] = this.mPowerSaveWhitelistApps.keyAt(i);
            }
        }
        return (java.lang.String[]) com.android.internal.util.ArrayUtils.filter(apps, new java.util.function.IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda9
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i2) {
                return com.android.server.DeviceIdleController.lambda$getSystemPowerWhitelistInternal$6(i2);
            }
        }, new java.util.function.Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getSystemPowerWhitelistInternal$7(callingUid, callingUserId, (java.lang.String) obj);
            }
        });
    }

    static /* synthetic */ java.lang.String[] lambda$getSystemPowerWhitelistInternal$6(int x$0) {
        return new java.lang.String[x$0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getSystemPowerWhitelistInternal$7(int callingUid, int callingUserId, java.lang.String pkg) {
        return !this.mPackageManagerInternal.filterAppAccess(pkg, callingUid, callingUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String[] getRemovedSystemPowerWhitelistAppsInternal(final int callingUid, final int callingUserId) {
        java.lang.String[] apps;
        synchronized (this) {
            int size = this.mRemovedFromSystemWhitelistApps.size();
            apps = new java.lang.String[size];
            for (int i = 0; i < size; i++) {
                apps[i] = this.mRemovedFromSystemWhitelistApps.keyAt(i);
            }
        }
        return (java.lang.String[]) com.android.internal.util.ArrayUtils.filter(apps, new java.util.function.IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda7
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i2) {
                return com.android.server.DeviceIdleController.lambda$getRemovedSystemPowerWhitelistAppsInternal$8(i2);
            }
        }, new java.util.function.Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getRemovedSystemPowerWhitelistAppsInternal$9(callingUid, callingUserId, (java.lang.String) obj);
            }
        });
    }

    static /* synthetic */ java.lang.String[] lambda$getRemovedSystemPowerWhitelistAppsInternal$8(int x$0) {
        return new java.lang.String[x$0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getRemovedSystemPowerWhitelistAppsInternal$9(int callingUid, int callingUserId, java.lang.String pkg) {
        return !this.mPackageManagerInternal.filterAppAccess(pkg, callingUid, callingUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String[] getUserPowerWhitelistInternal(final int callingUid, final int callingUserId) {
        java.lang.String[] apps;
        synchronized (this) {
            int size = this.mPowerSaveWhitelistUserApps.size();
            apps = new java.lang.String[size];
            for (int i = 0; i < this.mPowerSaveWhitelistUserApps.size(); i++) {
                apps[i] = this.mPowerSaveWhitelistUserApps.keyAt(i);
            }
        }
        return (java.lang.String[]) com.android.internal.util.ArrayUtils.filter(apps, new java.util.function.IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda11
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i2) {
                return com.android.server.DeviceIdleController.lambda$getUserPowerWhitelistInternal$10(i2);
            }
        }, new java.util.function.Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getUserPowerWhitelistInternal$11(callingUid, callingUserId, (java.lang.String) obj);
            }
        });
    }

    static /* synthetic */ java.lang.String[] lambda$getUserPowerWhitelistInternal$10(int x$0) {
        return new java.lang.String[x$0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getUserPowerWhitelistInternal$11(int callingUid, int callingUserId, java.lang.String pkg) {
        return !this.mPackageManagerInternal.filterAppAccess(pkg, callingUid, callingUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String[] getFullPowerWhitelistExceptIdleInternal(final int callingUid, final int callingUserId) {
        java.lang.String[] apps;
        synchronized (this) {
            int size = this.mPowerSaveWhitelistAppsExceptIdle.size() + this.mPowerSaveWhitelistUserApps.size();
            apps = new java.lang.String[size];
            int cur = 0;
            for (int i = 0; i < this.mPowerSaveWhitelistAppsExceptIdle.size(); i++) {
                apps[cur] = this.mPowerSaveWhitelistAppsExceptIdle.keyAt(i);
                cur++;
            }
            for (int i2 = 0; i2 < this.mPowerSaveWhitelistUserApps.size(); i2++) {
                apps[cur] = this.mPowerSaveWhitelistUserApps.keyAt(i2);
                cur++;
            }
        }
        return (java.lang.String[]) com.android.internal.util.ArrayUtils.filter(apps, new java.util.function.IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda5
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i3) {
                return com.android.server.DeviceIdleController.lambda$getFullPowerWhitelistExceptIdleInternal$12(i3);
            }
        }, new java.util.function.Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getFullPowerWhitelistExceptIdleInternal$13(callingUid, callingUserId, (java.lang.String) obj);
            }
        });
    }

    static /* synthetic */ java.lang.String[] lambda$getFullPowerWhitelistExceptIdleInternal$12(int x$0) {
        return new java.lang.String[x$0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getFullPowerWhitelistExceptIdleInternal$13(int callingUid, int callingUserId, java.lang.String pkg) {
        return !this.mPackageManagerInternal.filterAppAccess(pkg, callingUid, callingUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String[] getFullPowerWhitelistInternal(final int callingUid, final int callingUserId) {
        return (java.lang.String[]) com.android.internal.util.ArrayUtils.filter(getFullPowerWhitelistInternalUnchecked(), new java.util.function.IntFunction() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda14
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.DeviceIdleController.lambda$getFullPowerWhitelistInternal$14(i);
            }
        }, new java.util.function.Predicate() { // from class: com.android.server.DeviceIdleController$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getFullPowerWhitelistInternal$15(callingUid, callingUserId, (java.lang.String) obj);
            }
        });
    }

    static /* synthetic */ java.lang.String[] lambda$getFullPowerWhitelistInternal$14(int x$0) {
        return new java.lang.String[x$0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getFullPowerWhitelistInternal$15(int callingUid, int callingUserId, java.lang.String pkg) {
        return !this.mPackageManagerInternal.filterAppAccess(pkg, callingUid, callingUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String[] getFullPowerWhitelistInternalUnchecked() {
        java.lang.String[] apps;
        synchronized (this) {
            int size = this.mPowerSaveWhitelistApps.size() + this.mPowerSaveWhitelistUserApps.size();
            apps = new java.lang.String[size];
            int cur = 0;
            for (int i = 0; i < this.mPowerSaveWhitelistApps.size(); i++) {
                apps[cur] = this.mPowerSaveWhitelistApps.keyAt(i);
                cur++;
            }
            for (int i2 = 0; i2 < this.mPowerSaveWhitelistUserApps.size(); i2++) {
                apps[cur] = this.mPowerSaveWhitelistUserApps.keyAt(i2);
                cur++;
            }
        }
        return apps;
    }

    public boolean isPowerSaveWhitelistExceptIdleAppInternal(java.lang.String packageName) {
        boolean z;
        synchronized (this) {
            z = this.mPowerSaveWhitelistAppsExceptIdle.containsKey(packageName) || this.mPowerSaveWhitelistUserApps.containsKey(packageName);
        }
        return z;
    }

    public boolean isPowerSaveWhitelistAppInternal(java.lang.String packageName) {
        boolean z;
        synchronized (this) {
            z = this.mPowerSaveWhitelistApps.containsKey(packageName) || this.mPowerSaveWhitelistUserApps.containsKey(packageName);
        }
        return z;
    }

    public int[] getAppIdWhitelistExceptIdleInternal() {
        int[] iArr;
        synchronized (this) {
            iArr = this.mPowerSaveWhitelistExceptIdleAppIdArray;
        }
        return iArr;
    }

    public int[] getAppIdWhitelistInternal() {
        int[] iArr;
        synchronized (this) {
            iArr = this.mPowerSaveWhitelistAllAppIdArray;
        }
        return iArr;
    }

    public int[] getAppIdUserWhitelistInternal() {
        int[] iArr;
        synchronized (this) {
            iArr = this.mPowerSaveWhitelistUserAppIdArray;
        }
        return iArr;
    }

    public int[] getAppIdTempWhitelistInternal() {
        int[] iArr;
        synchronized (this) {
            iArr = this.mTempWhitelistAppIdArray;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTempAllowListType(int reasonCode, int defaultType) {
        switch (reasonCode) {
            case -1:
                return -1;
            case 102:
                return this.mLocalActivityManager.getPushMessagingOverQuotaBehavior();
            default:
                return defaultType;
        }
    }

    void addPowerSaveTempAllowlistAppChecked(java.lang.String packageName, long duration, int userId, int reasonCode, java.lang.String reason) throws android.os.RemoteException {
        getContext().enforceCallingOrSelfPermission("android.permission.CHANGE_DEVICE_IDLE_TEMP_WHITELIST", "No permission to change device idle whitelist");
        int callingUid = android.os.Binder.getCallingUid();
        int userId2 = android.app.ActivityManager.getService().handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, false, "addPowerSaveTempWhitelistApp", (java.lang.String) null);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            int type = getTempAllowListType(reasonCode, 0);
            if (type != -1) {
                addPowerSaveTempAllowlistAppInternal(callingUid, packageName, duration, type, userId2, true, reasonCode, reason);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    void removePowerSaveTempAllowlistAppChecked(java.lang.String packageName, int userId) throws android.os.RemoteException {
        getContext().enforceCallingOrSelfPermission("android.permission.CHANGE_DEVICE_IDLE_TEMP_WHITELIST", "No permission to change device idle whitelist");
        int callingUid = android.os.Binder.getCallingUid();
        int userId2 = android.app.ActivityManager.getService().handleIncomingUser(android.os.Binder.getCallingPid(), callingUid, userId, false, false, "removePowerSaveTempWhitelistApp", (java.lang.String) null);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            removePowerSaveTempAllowlistAppInternal(packageName, userId2);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    void addPowerSaveTempAllowlistAppInternal(int callingUid, java.lang.String packageName, long durationMs, int tempAllowListType, int userId, boolean sync, int reasonCode, java.lang.String reason) throws java.lang.Throwable {
        try {
            try {
                int uid = getContext().getPackageManager().getPackageUidAsUser(packageName, userId);
                addPowerSaveTempWhitelistAppDirectInternal(callingUid, uid, durationMs, tempAllowListType, sync, reasonCode, reason);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x00ca  */
    /* JADX WARN: Removed duplicated region for block: B:76:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void addPowerSaveTempWhitelistAppDirectInternal(int r29, int r30, long r31, int r33, boolean r34, int r35, java.lang.String r36) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 239
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.DeviceIdleController.addPowerSaveTempWhitelistAppDirectInternal(int, int, long, int, boolean, int, java.lang.String):void");
    }

    private void removePowerSaveTempAllowlistAppInternal(java.lang.String packageName, int userId) {
        try {
            int uid = getContext().getPackageManager().getPackageUidAsUser(packageName, userId);
            removePowerSaveTempWhitelistAppDirectInternal(uid);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
    }

    private void removePowerSaveTempWhitelistAppDirectInternal(int uid) {
        int appId = android.os.UserHandle.getAppId(uid);
        synchronized (this) {
            int idx = this.mTempWhitelistAppIdEndTimes.indexOfKey(appId);
            if (idx < 0) {
                return;
            }
            java.lang.String reason = (java.lang.String) this.mTempWhitelistAppIdEndTimes.valueAt(idx).second;
            this.mTempWhitelistAppIdEndTimes.removeAt(idx);
            onAppRemovedFromTempWhitelistLocked(uid, reason);
        }
    }

    private void postTempActiveTimeoutMessage(int uid, long delay) {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(6, uid, 0), delay);
    }

    void checkTempAppWhitelistTimeout(int uid) {
        long timeNow = android.os.SystemClock.elapsedRealtime();
        int appId = android.os.UserHandle.getAppId(uid);
        synchronized (this) {
            android.util.Pair<android.util.MutableLong, java.lang.String> entry = this.mTempWhitelistAppIdEndTimes.get(appId);
            if (entry == null) {
                return;
            }
            if (timeNow >= ((android.util.MutableLong) entry.first).value) {
                this.mTempWhitelistAppIdEndTimes.delete(appId);
                onAppRemovedFromTempWhitelistLocked(uid, (java.lang.String) entry.second);
            } else {
                postTempActiveTimeoutMessage(uid, ((android.util.MutableLong) entry.first).value - timeNow);
            }
        }
    }

    private void onAppRemovedFromTempWhitelistLocked(int uid, java.lang.String reason) {
        int appId = android.os.UserHandle.getAppId(uid);
        updateTempWhitelistAppIdsLocked(uid, false, 0L, 0, 0, reason, -1);
        this.mHandler.obtainMessage(15, appId, 0).sendToTarget();
        reportTempWhitelistChangedLocked(uid, false);
        try {
            this.mBatteryStats.noteEvent(16401, reason, uid);
        } catch (android.os.RemoteException e) {
        }
    }

    public void exitIdleInternal(java.lang.String reason) {
        synchronized (this) {
            this.mActiveReason = 5;
            becomeActiveLocked(reason, android.os.Binder.getCallingUid());
        }
    }

    boolean isNetworkConnected() {
        boolean z;
        synchronized (this) {
            z = this.mNetworkConnected;
        }
        return z;
    }

    void updateConnectivityState(android.content.Intent connIntent) {
        android.net.ConnectivityManager cm;
        boolean conn;
        synchronized (this) {
            cm = this.mInjector.getConnectivityManager();
        }
        if (cm == null) {
            return;
        }
        android.net.NetworkInfo ni = cm.getActiveNetworkInfo();
        synchronized (this) {
            if (ni == null) {
                conn = false;
            } else if (connIntent == null) {
                conn = ni.isConnected();
            } else {
                int networkType = connIntent.getIntExtra("networkType", -1);
                if (ni.getType() != networkType) {
                    return;
                } else {
                    conn = !connIntent.getBooleanExtra("noConnectivity", false);
                }
            }
            if (conn != this.mNetworkConnected) {
                this.mNetworkConnected = conn;
                if (conn && this.mLightState == 5) {
                    stepLightIdleStateLocked("network");
                }
            }
        }
    }

    boolean isScreenOn() {
        boolean z;
        synchronized (this) {
            z = this.mScreenOn;
        }
        return z;
    }

    void updateInteractivityLocked() {
        boolean screenOn = this.mPowerManager.isInteractive();
        if (!screenOn && this.mScreenOn) {
            this.mScreenOn = false;
            if (!this.mForceIdle) {
                becomeInactiveIfAppropriateLocked();
                return;
            }
            return;
        }
        if (screenOn) {
            this.mScreenOn = true;
            if (this.mForceIdle) {
                return;
            }
            if (!this.mScreenLocked || !this.mConstants.WAIT_FOR_UNLOCK) {
                this.mActiveReason = 2;
                becomeActiveLocked("screen", android.os.Process.myUid());
            }
        }
    }

    boolean isCharging() {
        boolean z;
        synchronized (this) {
            z = this.mCharging;
        }
        return z;
    }

    void updateChargingLocked(boolean charging) {
        if (!charging && this.mCharging) {
            this.mCharging = false;
            if (!this.mForceIdle) {
                becomeInactiveIfAppropriateLocked();
                return;
            }
            return;
        }
        if (charging) {
            this.mCharging = charging;
            if (!this.mForceIdle) {
                this.mActiveReason = 3;
                becomeActiveLocked("charging", android.os.Process.myUid());
            }
        }
    }

    boolean isQuickDozeEnabled() {
        boolean z;
        synchronized (this) {
            z = this.mQuickDozeActivated;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateQuickDozeFlagLocked() {
        if (this.mConstants.USE_MODE_MANAGER) {
            updateQuickDozeFlagLocked(this.mModeManagerRequestedQuickDoze || this.mBatterySaverEnabled);
        } else {
            updateQuickDozeFlagLocked(this.mBatterySaverEnabled);
        }
    }

    void updateQuickDozeFlagLocked(boolean enabled) {
        this.mQuickDozeActivated = enabled;
        this.mQuickDozeActivatedWhileIdling = this.mQuickDozeActivated && (this.mState == 5 || this.mState == 6);
        if (enabled) {
            becomeInactiveIfAppropriateLocked();
        }
    }

    boolean isKeyguardShowing() {
        boolean z;
        synchronized (this) {
            z = this.mScreenLocked;
        }
        return z;
    }

    void keyguardShowingLocked(boolean showing) {
        if (this.mScreenLocked != showing) {
            this.mScreenLocked = showing;
            if (this.mScreenOn && !this.mForceIdle && !this.mScreenLocked) {
                this.mActiveReason = 4;
                becomeActiveLocked("unlocked", android.os.Process.myUid());
            }
        }
    }

    void scheduleReportActiveLocked(java.lang.String activeReason, int activeUid) {
        android.os.Message msg = this.mHandler.obtainMessage(5, activeUid, 0, activeReason);
        this.mHandler.sendMessage(msg);
    }

    void becomeActiveLocked(java.lang.String activeReason, int activeUid) {
        becomeActiveLocked(activeReason, activeUid, this.mConstants.INACTIVE_TIMEOUT, true);
    }

    private void becomeActiveLocked(java.lang.String activeReason, int activeUid, long newInactiveTimeout, boolean changeLightIdle) {
        if (this.mState != 0 || this.mLightState != 0) {
            moveToStateLocked(0, activeReason);
            this.mInactiveTimeout = newInactiveTimeout;
            resetIdleManagementLocked();
            if (this.mLightState != 6) {
                this.mMaintenanceStartTime = 0L;
            }
            if (changeLightIdle) {
                moveToLightStateLocked(0, activeReason);
                resetLightIdleManagementLocked();
                scheduleReportActiveLocked(activeReason, activeUid);
                addEvent(1, activeReason);
            }
        }
    }

    void setDeepEnabledForTest(boolean enabled) {
        synchronized (this) {
            this.mDeepEnabled = enabled;
        }
    }

    void setLightEnabledForTest(boolean enabled) {
        synchronized (this) {
            this.mLightEnabled = enabled;
        }
    }

    private void verifyAlarmStateLocked() {
        if (this.mState == 0 && this.mNextAlarmTime != 0) {
            android.util.Slog.wtf(TAG, "mState=ACTIVE but mNextAlarmTime=" + this.mNextAlarmTime);
        }
        if (this.mState != 5 && this.mLocalAlarmManager.isIdling()) {
            android.util.Slog.wtf(TAG, "mState=" + stateToString(this.mState) + " but AlarmManager is idling");
        }
        if (this.mState == 5 && !this.mLocalAlarmManager.isIdling()) {
            android.util.Slog.wtf(TAG, "mState=IDLE but AlarmManager is not idling");
        }
        if (this.mLightState == 0 && this.mNextLightAlarmTime != 0) {
            android.util.Slog.wtf(TAG, "mLightState=ACTIVE but mNextLightAlarmTime is " + android.util.TimeUtils.formatDuration(this.mNextLightAlarmTime - android.os.SystemClock.elapsedRealtime()) + " from now");
        }
    }

    void becomeInactiveIfAppropriateLocked() {
        verifyAlarmStateLocked();
        boolean isScreenBlockingInactive = this.mScreenOn && !(this.mConstants.WAIT_FOR_UNLOCK && this.mScreenLocked);
        boolean isEmergencyCallActive = this.mEmergencyCallListener.isEmergencyCallActive();
        if (!this.mForceIdle && (this.mCharging || isScreenBlockingInactive || isEmergencyCallActive)) {
            return;
        }
        if (this.mDeepEnabled) {
            if (this.mQuickDozeActivated) {
                if (this.mState == 7 || this.mState == 5 || this.mState == 6) {
                    return;
                }
                moveToStateLocked(7, "no activity");
                resetIdleManagementLocked();
                if (isUpcomingAlarmClock()) {
                    scheduleAlarmLocked((this.mAlarmManager.getNextWakeFromIdleTime() - this.mInjector.getElapsedRealtime()) + this.mConstants.QUICK_DOZE_DELAY_TIMEOUT);
                } else {
                    scheduleAlarmLocked(this.mConstants.QUICK_DOZE_DELAY_TIMEOUT);
                }
            } else if (this.mState == 0) {
                moveToStateLocked(1, "no activity");
                resetIdleManagementLocked();
                long delay = this.mInactiveTimeout;
                if (isUpcomingAlarmClock()) {
                    scheduleAlarmLocked((this.mAlarmManager.getNextWakeFromIdleTime() - this.mInjector.getElapsedRealtime()) + delay);
                } else {
                    scheduleAlarmLocked(delay);
                }
            }
        }
        if (this.mLightState == 0 && this.mLightEnabled) {
            moveToLightStateLocked(1, "no activity");
            resetLightIdleManagementLocked();
            scheduleLightAlarmLocked(this.mConstants.LIGHT_IDLE_AFTER_INACTIVE_TIMEOUT, this.mConstants.FLEX_TIME_SHORT, true);
        }
    }

    private void resetIdleManagementLocked() {
        this.mNextIdlePendingDelay = 0L;
        this.mNextIdleDelay = 0L;
        this.mQuickDozeActivatedWhileIdling = false;
        cancelAlarmLocked();
        cancelSensingTimeoutAlarmLocked();
        cancelLocatingLocked();
        maybeStopMonitoringMotionLocked();
        this.mAnyMotionDetector.stop();
        updateActiveConstraintsLocked();
    }

    private void resetLightIdleManagementLocked() {
        this.mNextLightIdleDelay = this.mConstants.LIGHT_IDLE_TIMEOUT;
        this.mMaintenanceStartTime = 0L;
        this.mNextLightIdleDelayFlex = this.mConstants.LIGHT_IDLE_TIMEOUT_INITIAL_FLEX;
        this.mCurLightIdleBudget = this.mConstants.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET;
        cancelLightAlarmLocked();
    }

    void exitForceIdleLocked() {
        if (this.mForceIdle) {
            this.mForceIdle = false;
            if (this.mScreenOn || this.mCharging) {
                this.mActiveReason = 6;
                becomeActiveLocked("exit-force", android.os.Process.myUid());
            }
        }
    }

    void setLightStateForTest(int lightState) {
        synchronized (this) {
            this.mLightState = lightState;
        }
    }

    int getLightState() {
        int i;
        synchronized (this) {
            i = this.mLightState;
        }
        return i;
    }

    void stepLightIdleStateLocked(java.lang.String reason) {
        if (this.mLightState == 0 || this.mLightState == 7) {
            return;
        }
        com.android.server.EventLogTags.writeDeviceIdleLightStep();
        if (!this.mEmergencyCallListener.isEmergencyCallActive()) {
            switch (this.mLightState) {
                case 1:
                    this.mCurLightIdleBudget = this.mConstants.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET;
                    this.mNextLightIdleDelay = this.mConstants.LIGHT_IDLE_TIMEOUT;
                    this.mNextLightIdleDelayFlex = this.mConstants.LIGHT_IDLE_TIMEOUT_INITIAL_FLEX;
                    this.mMaintenanceStartTime = 0L;
                    break;
                case 2:
                case 3:
                default:
                    return;
                case 4:
                case 5:
                    if (this.mNetworkConnected || this.mLightState == 5) {
                        this.mActiveIdleOpCount = 1;
                        this.mActiveIdleWakeLock.acquire();
                        this.mMaintenanceStartTime = android.os.SystemClock.elapsedRealtime();
                        if (this.mCurLightIdleBudget < this.mConstants.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET) {
                            this.mCurLightIdleBudget = this.mConstants.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET;
                        } else if (this.mCurLightIdleBudget > this.mConstants.LIGHT_IDLE_MAINTENANCE_MAX_BUDGET) {
                            this.mCurLightIdleBudget = this.mConstants.LIGHT_IDLE_MAINTENANCE_MAX_BUDGET;
                        }
                        scheduleLightAlarmLocked(this.mCurLightIdleBudget, this.mConstants.FLEX_TIME_SHORT, true);
                        moveToLightStateLocked(6, reason);
                        addEvent(3, null);
                        this.mHandler.sendEmptyMessage(4);
                        return;
                    }
                    scheduleLightAlarmLocked(this.mNextLightIdleDelay, this.mNextLightIdleDelayFlex / 2, true);
                    moveToLightStateLocked(5, reason);
                    return;
                case 6:
                    break;
            }
            if (this.mMaintenanceStartTime != 0) {
                long duration = android.os.SystemClock.elapsedRealtime() - this.mMaintenanceStartTime;
                if (duration < this.mConstants.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET) {
                    this.mCurLightIdleBudget += this.mConstants.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET - duration;
                } else {
                    this.mCurLightIdleBudget -= duration - this.mConstants.LIGHT_IDLE_MAINTENANCE_MIN_BUDGET;
                }
            }
            this.mMaintenanceStartTime = 0L;
            scheduleLightAlarmLocked(this.mNextLightIdleDelay, this.mNextLightIdleDelayFlex, true);
            if (!this.mConstants.LIGHT_IDLE_INCREASE_LINEARLY) {
                this.mNextLightIdleDelay = java.lang.Math.min(this.mConstants.LIGHT_MAX_IDLE_TIMEOUT, (long) (this.mNextLightIdleDelay * this.mConstants.LIGHT_IDLE_FACTOR));
                this.mNextLightIdleDelayFlex = java.lang.Math.min(this.mConstants.LIGHT_IDLE_TIMEOUT_MAX_FLEX, (long) (this.mNextLightIdleDelayFlex * this.mConstants.LIGHT_IDLE_FACTOR));
            } else {
                this.mNextLightIdleDelay = java.lang.Math.min(this.mConstants.LIGHT_MAX_IDLE_TIMEOUT, this.mNextLightIdleDelay + this.mConstants.LIGHT_IDLE_LINEAR_INCREASE_FACTOR_MS);
                this.mNextLightIdleDelayFlex = java.lang.Math.min(this.mConstants.LIGHT_IDLE_TIMEOUT_MAX_FLEX, this.mNextLightIdleDelayFlex + this.mConstants.LIGHT_IDLE_FLEX_LINEAR_INCREASE_FACTOR_MS);
            }
            moveToLightStateLocked(4, reason);
            addEvent(2, null);
            this.mGoingIdleWakeLock.acquire();
            this.mHandler.sendEmptyMessage(3);
            return;
        }
        android.util.Slog.wtf(TAG, "stepLightIdleStateLocked called when emergency call is active");
        if (this.mLightState != 0) {
            this.mActiveReason = 8;
            becomeActiveLocked("emergency", android.os.Process.myUid());
        }
    }

    int getState() {
        int i;
        synchronized (this) {
            i = this.mState;
        }
        return i;
    }

    private boolean isUpcomingAlarmClock() {
        return this.mInjector.getElapsedRealtime() + this.mConstants.MIN_TIME_TO_ALARM >= this.mAlarmManager.getNextWakeFromIdleTime();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0176  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0180  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void stepIdleStateLocked(java.lang.String r18) {
        /*
            Method dump skipped, instruction units count: 440
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.DeviceIdleController.stepIdleStateLocked(java.lang.String):void");
    }

    private void moveToLightStateLocked(int state, java.lang.String reason) {
        this.mLightState = state;
        com.android.server.EventLogTags.writeDeviceIdleLight(this.mLightState, reason);
        android.os.Trace.traceCounter(524288L, "DozeLightState", state);
    }

    private void moveToStateLocked(int state, java.lang.String reason) {
        this.mState = state;
        com.android.server.EventLogTags.writeDeviceIdle(this.mState, reason);
        android.os.Trace.traceCounter(524288L, "DozeDeepState", state);
        updateActiveConstraintsLocked();
    }

    void incActiveIdleOps() {
        synchronized (this) {
            this.mActiveIdleOpCount++;
        }
    }

    void decActiveIdleOps() {
        synchronized (this) {
            this.mActiveIdleOpCount--;
            if (this.mActiveIdleOpCount <= 0) {
                exitMaintenanceEarlyIfNeededLocked();
                this.mActiveIdleWakeLock.release();
            }
        }
    }

    void setActiveIdleOpsForTest(int count) {
        synchronized (this) {
            this.mActiveIdleOpCount = count;
        }
    }

    void setJobsActive(boolean active) {
        synchronized (this) {
            this.mJobsActive = active;
            if (!active) {
                exitMaintenanceEarlyIfNeededLocked();
            }
        }
    }

    void setAlarmsActive(boolean active) {
        synchronized (this) {
            this.mAlarmsActive = active;
            if (!active) {
                exitMaintenanceEarlyIfNeededLocked();
            }
        }
    }

    long getNextAlarmTime() {
        long j;
        synchronized (this) {
            j = this.mNextAlarmTime;
        }
        return j;
    }

    boolean isEmergencyCallActive() {
        return this.mEmergencyCallListener.isEmergencyCallActive();
    }

    boolean isOpsInactiveLocked() {
        return (this.mActiveIdleOpCount > 0 || this.mJobsActive || this.mAlarmsActive) ? false : true;
    }

    void exitMaintenanceEarlyIfNeededLocked() {
        if ((this.mState == 6 || this.mLightState == 6) && isOpsInactiveLocked()) {
            android.os.SystemClock.elapsedRealtime();
            if (this.mState == 6) {
                stepIdleStateLocked("s:early");
            } else {
                stepLightIdleStateLocked("s:early");
            }
        }
    }

    void motionLocked() {
        this.mLastMotionEventElapsed = this.mInjector.getElapsedRealtime();
        handleMotionDetectedLocked(this.mConstants.MOTION_INACTIVE_TIMEOUT, "motion");
    }

    void handleMotionDetectedLocked(long timeout, java.lang.String type) {
        if (this.mStationaryListeners.size() > 0) {
            postStationaryStatusUpdated();
            cancelMotionTimeoutAlarmLocked();
            scheduleMotionRegistrationAlarmLocked();
        }
        if ((!this.mQuickDozeActivated || this.mQuickDozeActivatedWhileIdling) && !this.mDeviceIdleExt.isInSmartDozeMode(this.mState)) {
            maybeStopMonitoringMotionLocked();
            boolean becomeInactive = this.mState != 0 || this.mLightState == 7;
            becomeActiveLocked(type, android.os.Process.myUid(), timeout, this.mLightState == 7);
            if (becomeInactive) {
                becomeInactiveIfAppropriateLocked();
            }
        }
    }

    void receivedGenericLocationLocked(android.location.Location location) {
        if (this.mState != 4) {
            cancelLocatingLocked();
            return;
        }
        this.mLastGenericLocation = new android.location.Location(location);
        if (location.getAccuracy() > this.mConstants.LOCATION_ACCURACY && this.mHasGps) {
            return;
        }
        this.mLocated = true;
        if (this.mNotMoving) {
            stepIdleStateLocked("s:location");
        }
    }

    void receivedGpsLocationLocked(android.location.Location location) {
        if (this.mState != 4) {
            cancelLocatingLocked();
            return;
        }
        this.mLastGpsLocation = new android.location.Location(location);
        if (location.getAccuracy() > this.mConstants.LOCATION_ACCURACY) {
            return;
        }
        this.mLocated = true;
        if (this.mNotMoving) {
            stepIdleStateLocked("s:gps");
        }
    }

    void startMonitoringMotionLocked() {
        if (this.mMotionSensor != null && !this.mMotionListener.active) {
            this.mMotionListener.registerLocked();
        }
    }

    private void maybeStopMonitoringMotionLocked() {
        if (this.mMotionSensor != null && this.mStationaryListeners.size() == 0) {
            if (this.mMotionListener.active) {
                this.mMotionListener.unregisterLocked();
                cancelMotionTimeoutAlarmLocked();
            }
            cancelMotionRegistrationAlarmLocked();
        }
    }

    void cancelAlarmLocked() {
        if (this.mNextAlarmTime != 0) {
            this.mNextAlarmTime = 0L;
            this.mAlarmManager.cancel(this.mDeepAlarmListener);
        }
    }

    private void cancelLightAlarmLocked() {
        if (this.mNextLightAlarmTime != 0) {
            this.mNextLightAlarmTime = 0L;
            this.mAlarmManager.cancel(this.mLightAlarmListener);
        }
    }

    void cancelLocatingLocked() {
        if (this.mLocating) {
            android.location.LocationManager locationManager = this.mInjector.getLocationManager();
            locationManager.removeUpdates(this.mGenericLocationListener);
            locationManager.removeUpdates(this.mGpsLocationListener);
            this.mLocating = false;
        }
    }

    private void cancelMotionTimeoutAlarmLocked() {
        this.mAlarmManager.cancel(this.mMotionTimeoutAlarmListener);
    }

    private void cancelMotionRegistrationAlarmLocked() {
        this.mAlarmManager.cancel(this.mMotionRegistrationAlarmListener);
    }

    void cancelSensingTimeoutAlarmLocked() {
        if (this.mNextSensingTimeoutAlarmTime != 0) {
            this.mNextSensingTimeoutAlarmTime = 0L;
            this.mAlarmManager.cancel(this.mSensingTimeoutAlarmListener);
        }
    }

    void scheduleAlarmLocked(long delay) {
        if (!this.mUseMotionSensor || this.mMotionSensor != null || this.mState == 7 || this.mState == 5 || this.mState == 6) {
            this.mNextAlarmTime = android.os.SystemClock.elapsedRealtime() + delay;
            if (this.mState == 5) {
                this.mAlarmManager.setIdleUntil(2, this.mNextAlarmTime, "DeviceIdleController.deep", this.mDeepAlarmListener, this.mHandler);
                return;
            }
            if (this.mState == 4) {
                this.mAlarmManager.setExact(2, this.mNextAlarmTime, "DeviceIdleController.deep", this.mDeepAlarmListener, this.mHandler);
            } else if (this.mConstants.USE_WINDOW_ALARMS) {
                this.mAlarmManager.setWindow(2, this.mNextAlarmTime, this.mConstants.FLEX_TIME_SHORT, "DeviceIdleController.deep", this.mDeepAlarmListener, this.mHandler);
            } else {
                this.mAlarmManager.set(2, this.mNextAlarmTime, "DeviceIdleController.deep", this.mDeepAlarmListener, this.mHandler);
            }
        }
    }

    void scheduleLightAlarmLocked(long delay, long flex, boolean wakeup) {
        this.mNextLightAlarmTime = this.mInjector.getElapsedRealtime() + delay;
        if (this.mConstants.USE_WINDOW_ALARMS) {
            this.mAlarmManager.setWindow(wakeup ? 2 : 3, this.mNextLightAlarmTime, flex, "DeviceIdleController.light", this.mLightAlarmListener, this.mHandler);
        } else {
            this.mAlarmManager.set(wakeup ? 2 : 3, this.mNextLightAlarmTime, "DeviceIdleController.light", this.mLightAlarmListener, this.mHandler);
        }
    }

    long getNextLightAlarmTimeForTesting() {
        long j;
        synchronized (this) {
            j = this.mNextLightAlarmTime;
        }
        return j;
    }

    private void scheduleMotionRegistrationAlarmLocked() {
        long nextMotionRegistrationAlarmTime = this.mInjector.getElapsedRealtime() + (this.mConstants.MOTION_INACTIVE_TIMEOUT / 2);
        if (this.mConstants.USE_WINDOW_ALARMS) {
            this.mAlarmManager.setWindow(2, nextMotionRegistrationAlarmTime, this.mConstants.MOTION_INACTIVE_TIMEOUT_FLEX, "DeviceIdleController.motion_registration", this.mMotionRegistrationAlarmListener, this.mHandler);
        } else {
            this.mAlarmManager.set(2, nextMotionRegistrationAlarmTime, "DeviceIdleController.motion_registration", this.mMotionRegistrationAlarmListener, this.mHandler);
        }
    }

    private void scheduleMotionTimeoutAlarmLocked() {
        long nextMotionTimeoutAlarmTime = this.mInjector.getElapsedRealtime() + this.mConstants.MOTION_INACTIVE_TIMEOUT;
        if (this.mConstants.USE_WINDOW_ALARMS) {
            this.mAlarmManager.setWindow(2, nextMotionTimeoutAlarmTime, this.mConstants.MOTION_INACTIVE_TIMEOUT_FLEX, "DeviceIdleController.motion", this.mMotionTimeoutAlarmListener, this.mHandler);
        } else {
            this.mAlarmManager.set(2, nextMotionTimeoutAlarmTime, "DeviceIdleController.motion", this.mMotionTimeoutAlarmListener, this.mHandler);
        }
    }

    void scheduleSensingTimeoutAlarmLocked(long delay) {
        this.mNextSensingTimeoutAlarmTime = android.os.SystemClock.elapsedRealtime() + delay;
        if (this.mConstants.USE_WINDOW_ALARMS) {
            this.mAlarmManager.setWindow(2, this.mNextSensingTimeoutAlarmTime, this.mConstants.FLEX_TIME_SHORT, "DeviceIdleController.sensing", this.mSensingTimeoutAlarmListener, this.mHandler);
        } else {
            this.mAlarmManager.set(2, this.mNextSensingTimeoutAlarmTime, "DeviceIdleController.sensing", this.mSensingTimeoutAlarmListener, this.mHandler);
        }
    }

    private static int[] buildAppIdArray(android.util.ArrayMap<java.lang.String, java.lang.Integer> systemApps, android.util.ArrayMap<java.lang.String, java.lang.Integer> userApps, android.util.SparseBooleanArray outAppIds) {
        outAppIds.clear();
        if (systemApps != null) {
            for (int i = 0; i < systemApps.size(); i++) {
                outAppIds.put(systemApps.valueAt(i).intValue(), true);
            }
        }
        if (userApps != null) {
            for (int i2 = 0; i2 < userApps.size(); i2++) {
                outAppIds.put(userApps.valueAt(i2).intValue(), true);
            }
        }
        int size = outAppIds.size();
        int[] appids = new int[size];
        for (int i3 = 0; i3 < size; i3++) {
            appids[i3] = outAppIds.keyAt(i3);
        }
        return appids;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWhitelistAppIdsLocked() {
        this.mPowerSaveWhitelistExceptIdleAppIdArray = buildAppIdArray(this.mPowerSaveWhitelistAppsExceptIdle, this.mPowerSaveWhitelistUserApps, this.mPowerSaveWhitelistExceptIdleAppIds);
        this.mPowerSaveWhitelistAllAppIdArray = buildAppIdArray(this.mPowerSaveWhitelistApps, this.mPowerSaveWhitelistUserApps, this.mPowerSaveWhitelistAllAppIds);
        this.mPowerSaveWhitelistUserAppIdArray = buildAppIdArray(null, this.mPowerSaveWhitelistUserApps, this.mPowerSaveWhitelistUserAppIds);
        if (this.mLocalActivityManager != null) {
            this.mLocalActivityManager.setDeviceIdleAllowlist(this.mPowerSaveWhitelistAllAppIdArray, this.mPowerSaveWhitelistExceptIdleAppIdArray);
        }
        if (this.mLocalPowerManager != null) {
            this.mLocalPowerManager.setDeviceIdleWhitelist(this.mPowerSaveWhitelistAllAppIdArray);
        }
        passWhiteListsToForceAppStandbyTrackerLocked();
    }

    private void updateTempWhitelistAppIdsLocked(int uid, boolean adding, long durationMs, int type, int reasonCode, java.lang.String reason, int callingUid) {
        int size = this.mTempWhitelistAppIdEndTimes.size();
        if (this.mTempWhitelistAppIdArray.length != size) {
            this.mTempWhitelistAppIdArray = new int[size];
        }
        for (int i = 0; i < size; i++) {
            this.mTempWhitelistAppIdArray[i] = this.mTempWhitelistAppIdEndTimes.keyAt(i);
        }
        if (this.mLocalActivityManager != null) {
            this.mLocalActivityManager.updateDeviceIdleTempAllowlist(this.mTempWhitelistAppIdArray, uid, adding, durationMs, type, reasonCode, reason, callingUid);
        }
        if (this.mDeviceIdleExt.shouldIgnoreTempWhitelistChange(uid, adding, isAppOnWhitelistInternal(android.os.UserHandle.getAppId(uid)))) {
            return;
        }
        if (this.mLocalPowerManager != null) {
            this.mLocalPowerManager.setDeviceIdleTempWhitelist(this.mTempWhitelistAppIdArray);
        }
        passWhiteListsToForceAppStandbyTrackerLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportPowerSaveWhitelistChangedLocked() {
        getContext().sendBroadcastAsUser(this.mPowerSaveWhitelistChangedIntent, android.os.UserHandle.SYSTEM, null, this.mPowerSaveWhitelistChangedOptions);
    }

    private void reportTempWhitelistChangedLocked(int i, boolean z) {
        if (this.mDeviceIdleExt.shouldIgnoreTempWhitelistChange(i, z, isAppOnWhitelistInternal(android.os.UserHandle.getAppId(i)))) {
            return;
        }
        this.mHandler.obtainMessage(13, i, z ? 1 : 0).sendToTarget();
        getContext().sendBroadcastAsUser(this.mPowerSaveTempWhitelistChangedIntent, android.os.UserHandle.SYSTEM, null, this.mPowerSaveTempWhilelistChangedOptions);
    }

    private void passWhiteListsToForceAppStandbyTrackerLocked() {
        this.mAppStateTracker.setPowerSaveExemptionListAppIds(this.mPowerSaveWhitelistExceptIdleAppIdArray, this.mPowerSaveWhitelistUserAppIdArray, this.mTempWhitelistAppIdArray);
    }

    void readConfigFileLocked() {
        this.mPowerSaveWhitelistUserApps.clear();
        try {
            try {
                java.io.FileInputStream stream = this.mConfigFile.openRead();
                try {
                    org.xmlpull.v1.XmlPullParser parser = android.util.Xml.newPullParser();
                    parser.setInput(stream, java.nio.charset.StandardCharsets.UTF_8.name());
                    readConfigFileLocked(parser);
                    stream.close();
                } catch (org.xmlpull.v1.XmlPullParserException e) {
                    stream.close();
                } catch (java.lang.Throwable th) {
                    try {
                        stream.close();
                    } catch (java.io.IOException e2) {
                    }
                    throw th;
                }
            } catch (java.io.FileNotFoundException e3) {
            }
        } catch (java.io.IOException e4) {
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:21:0x003f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readConfigFileLocked(org.xmlpull.v1.XmlPullParser r13) {
        /*
            Method dump skipped, instruction units count: 344
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.DeviceIdleController.readConfigFileLocked(org.xmlpull.v1.XmlPullParser):void");
    }

    void writeConfigFileLocked() {
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessageDelayed(1, 5000L);
    }

    void handleWriteConfigFile() {
        java.io.ByteArrayOutputStream memStream = new java.io.ByteArrayOutputStream();
        try {
            synchronized (this) {
                org.xmlpull.v1.XmlSerializer out = new com.android.internal.util.FastXmlSerializer();
                out.setOutput(memStream, java.nio.charset.StandardCharsets.UTF_8.name());
                writeConfigFileLocked(out);
            }
        } catch (java.io.IOException e) {
        }
        synchronized (this.mConfigFile) {
            java.io.FileOutputStream stream = null;
            try {
                stream = this.mConfigFile.startWrite();
                memStream.writeTo(stream);
                this.mConfigFile.finishWrite(stream);
            } catch (java.io.IOException e2) {
                android.util.Slog.w(TAG, "Error writing config file", e2);
                this.mConfigFile.failWrite(stream);
            }
        }
    }

    void writeConfigFileLocked(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
        out.startDocument(null, true);
        out.startTag(null, "config");
        for (int i = 0; i < this.mPowerSaveWhitelistUserApps.size(); i++) {
            java.lang.String name = this.mPowerSaveWhitelistUserApps.keyAt(i);
            out.startTag(null, "wl");
            out.attribute(null, "n", name);
            out.endTag(null, "wl");
        }
        for (int i2 = 0; i2 < this.mRemovedFromSystemWhitelistApps.size(); i2++) {
            out.startTag(null, "un-wl");
            out.attribute(null, "n", this.mRemovedFromSystemWhitelistApps.keyAt(i2));
            out.endTag(null, "un-wl");
        }
        out.endTag(null, "config");
        out.endDocument();
    }

    static void dumpHelp(java.io.PrintWriter pw) {
        pw.println("Device idle controller (deviceidle) commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("  step [light|deep]");
        pw.println("    Immediately step to next state, without waiting for alarm.");
        pw.println("  force-idle [light|deep]");
        pw.println("    Force directly into idle mode, regardless of other device state.");
        pw.println("  force-inactive");
        pw.println("    Force to be inactive, ready to freely step idle states.");
        pw.println("  unforce");
        pw.println("    Resume normal functioning after force-idle or force-inactive or force-modemanager-quickdoze.");
        pw.println("  get [light|deep|force|screen|charging|network|offbody|forceoffbody]");
        pw.println("    Retrieve the current given state.");
        pw.println("  disable [light|deep|all]");
        pw.println("    Completely disable device idle mode.");
        pw.println("  enable [light|deep|all]");
        pw.println("    Re-enable device idle mode after it had previously been disabled.");
        pw.println("  enabled [light|deep|all]");
        pw.println("    Print 1 if device idle mode is currently enabled, else 0.");
        pw.println("  whitelist");
        pw.println("    Print currently whitelisted apps.");
        pw.println("  whitelist [package ...]");
        pw.println("    Add (prefix with +) or remove (prefix with -) packages.");
        pw.println("  sys-whitelist [package ...|reset]");
        pw.println("    Prefix the package with '-' to remove it from the system whitelist or '+' to put it back in the system whitelist.");
        pw.println("    Note that only packages that were earlier removed from the system whitelist can be added back.");
        pw.println("    reset will reset the whitelist to the original state");
        pw.println("    Prints the system whitelist if no arguments are specified");
        pw.println("  except-idle-whitelist [package ...|reset]");
        pw.println("    Prefix the package with '+' to add it to whitelist or '=' to check if it is already whitelisted");
        pw.println("    [reset] will reset the whitelist to it's original state");
        pw.println("    Note that unlike <whitelist> cmd, changes made using this won't be persisted across boots");
        pw.println("  tempwhitelist");
        pw.println("    Print packages that are temporarily whitelisted.");
        pw.println("  tempwhitelist [-u USER] [-d DURATION] [-r] [package]");
        pw.println("    Temporarily place package in whitelist for DURATION milliseconds.");
        pw.println("    If no DURATION is specified, 10 seconds is used");
        pw.println("    If [-r] option is used, then the package is removed from temp whitelist and any [-d] is ignored");
        pw.println("  motion");
        pw.println("    Simulate a motion event to bring the device out of deep doze");
        pw.println("  force-modemanager-quickdoze [true|false]");
        pw.println("    Simulate mode manager request to enable (true) or disable (false) quick doze. Mode manager changes will be ignored until unforce is called.");
        pw.println("  force-modemanager-offbody [true|false]");
        pw.println("    Force mode manager offbody state, this can be used to simulate device being off-body (true) or on-body (false). Mode manager changes will be ignored until unforce is called.");
    }

    class Shell extends android.os.ShellCommand {
        int userId = 0;

        Shell() {
        }

        public int onCommand(java.lang.String cmd) {
            return com.android.server.DeviceIdleController.this.onShellCommand(this, cmd);
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            com.android.server.DeviceIdleController.dumpHelp(pw);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:131:0x02a2  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x03ef A[Catch: all -> 0x043a, TryCatch #10 {all -> 0x043a, blocks: (B:198:0x03ba, B:200:0x03c2, B:206:0x03d9, B:208:0x03e2, B:214:0x03f9, B:218:0x0406, B:220:0x041d, B:210:0x03ea, B:212:0x03ef, B:202:0x03ca, B:204:0x03cf), top: B:591:0x03ba, outer: #13 }] */
    /* JADX WARN: Removed duplicated region for block: B:214:0x03f9 A[Catch: all -> 0x043a, TryCatch #10 {all -> 0x043a, blocks: (B:198:0x03ba, B:200:0x03c2, B:206:0x03d9, B:208:0x03e2, B:214:0x03f9, B:218:0x0406, B:220:0x041d, B:210:0x03ea, B:212:0x03ef, B:202:0x03ca, B:204:0x03cf), top: B:591:0x03ba, outer: #13 }] */
    /* JADX WARN: Removed duplicated region for block: B:220:0x041d A[Catch: all -> 0x043a, TRY_LEAVE, TryCatch #10 {all -> 0x043a, blocks: (B:198:0x03ba, B:200:0x03c2, B:206:0x03d9, B:208:0x03e2, B:214:0x03f9, B:218:0x0406, B:220:0x041d, B:210:0x03ea, B:212:0x03ef, B:202:0x03ca, B:204:0x03cf), top: B:591:0x03ba, outer: #13 }] */
    /* JADX WARN: Removed duplicated region for block: B:255:0x04a6 A[Catch: all -> 0x04d2, TryCatch #19 {all -> 0x04d2, blocks: (B:241:0x0471, B:243:0x0479, B:249:0x0490, B:251:0x0499, B:257:0x04b0, B:259:0x04b5, B:253:0x04a1, B:255:0x04a6, B:245:0x0481, B:247:0x0486), top: B:605:0x0471, outer: #27 }] */
    /* JADX WARN: Removed duplicated region for block: B:257:0x04b0 A[Catch: all -> 0x04d2, TryCatch #19 {all -> 0x04d2, blocks: (B:241:0x0471, B:243:0x0479, B:249:0x0490, B:251:0x0499, B:257:0x04b0, B:259:0x04b5, B:253:0x04a1, B:255:0x04a6, B:245:0x0481, B:247:0x0486), top: B:605:0x0471, outer: #27 }] */
    /* JADX WARN: Removed duplicated region for block: B:259:0x04b5 A[Catch: all -> 0x04d2, TRY_LEAVE, TryCatch #19 {all -> 0x04d2, blocks: (B:241:0x0471, B:243:0x0479, B:249:0x0490, B:251:0x0499, B:257:0x04b0, B:259:0x04b5, B:253:0x04a1, B:255:0x04a6, B:245:0x0481, B:247:0x0486), top: B:605:0x0471, outer: #27 }] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0164 A[Catch: all -> 0x01aa, TRY_LEAVE, TryCatch #2 {all -> 0x01aa, blocks: (B:48:0x0103, B:51:0x010c, B:53:0x0115, B:55:0x011e, B:57:0x0128, B:61:0x013f, B:62:0x0143, B:63:0x0149, B:64:0x0160, B:66:0x0164, B:70:0x016f, B:72:0x0178, B:74:0x0182, B:78:0x0199, B:79:0x019d), top: B:578:0x0103, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x016f A[Catch: all -> 0x01aa, TRY_ENTER, TryCatch #2 {all -> 0x01aa, blocks: (B:48:0x0103, B:51:0x010c, B:53:0x0115, B:55:0x011e, B:57:0x0128, B:61:0x013f, B:62:0x0143, B:63:0x0149, B:64:0x0160, B:66:0x0164, B:70:0x016f, B:72:0x0178, B:74:0x0182, B:78:0x0199, B:79:0x019d), top: B:578:0x0103, outer: #4 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int onShellCommand(com.android.server.DeviceIdleController.Shell r22, java.lang.String r23) {
        /*
            Method dump skipped, instruction units count: 2828
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.DeviceIdleController.onShellCommand(com.android.server.DeviceIdleController$Shell, java.lang.String):int");
    }

    void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        java.lang.String label;
        if (com.android.internal.util.DumpUtils.checkDumpPermission(getContext(), TAG, pw)) {
            int size = 0;
            if (args != null) {
                int userId = 0;
                int i = 0;
                while (i < args.length) {
                    java.lang.String arg = args[i];
                    if ("-h".equals(arg)) {
                        dumpHelp(pw);
                        return;
                    }
                    if ("-u".equals(arg)) {
                        i++;
                        if (i < args.length) {
                            userId = java.lang.Integer.parseInt(args[i]);
                        }
                    } else if (!"-a".equals(arg)) {
                        if (arg.length() > 0 && arg.charAt(0) == '-') {
                            pw.println("Unknown option: " + arg);
                            return;
                        }
                        if (!"custom-whitelist".equals(arg)) {
                            com.android.server.DeviceIdleController.Shell shell = new com.android.server.DeviceIdleController.Shell();
                            shell.userId = userId;
                            java.lang.String[] newArgs = new java.lang.String[args.length - i];
                            java.lang.System.arraycopy(args, i, newArgs, 0, args.length - i);
                            shell.exec(this.mBinderService, null, fd, null, newArgs, null, new android.os.ResultReceiver(null));
                            return;
                        }
                        this.mDeviceIdleExt.dump(pw);
                        return;
                    }
                    i++;
                }
            }
            synchronized (this) {
                this.mConstants.dump(pw);
                if (this.mEventCmds[0] != 0) {
                    pw.println("  Idling history:");
                    long now = android.os.SystemClock.elapsedRealtime();
                    for (int i2 = 99; i2 >= 0; i2--) {
                        int cmd = this.mEventCmds[i2];
                        if (cmd != 0) {
                            switch (this.mEventCmds[i2]) {
                                case 1:
                                    label = "     normal";
                                    break;
                                case 2:
                                    label = " light-idle";
                                    break;
                                case 3:
                                    label = "light-maint";
                                    break;
                                case 4:
                                    label = "  deep-idle";
                                    break;
                                case 5:
                                    label = " deep-maint";
                                    break;
                                default:
                                    label = "         ??";
                                    break;
                            }
                            pw.print("    ");
                            pw.print(label);
                            pw.print(": ");
                            android.util.TimeUtils.formatDuration(this.mEventTimes[i2], now, pw);
                            if (this.mEventReasons[i2] != null) {
                                pw.print(" (");
                                pw.print(this.mEventReasons[i2]);
                                pw.print(")");
                            }
                            pw.println();
                        }
                    }
                }
                int size2 = this.mPowerSaveWhitelistAppsExceptIdle.size();
                if (size2 > 0) {
                    pw.println("  Whitelist (except idle) system apps:");
                    for (int i3 = 0; i3 < size2; i3++) {
                        pw.print("    ");
                        pw.println(this.mPowerSaveWhitelistAppsExceptIdle.keyAt(i3));
                    }
                }
                int size3 = this.mPowerSaveWhitelistApps.size();
                if (size3 > 0) {
                    pw.println("  Whitelist system apps:");
                    for (int i4 = 0; i4 < size3; i4++) {
                        pw.print("    ");
                        pw.println(this.mPowerSaveWhitelistApps.keyAt(i4));
                    }
                }
                int size4 = this.mRemovedFromSystemWhitelistApps.size();
                if (size4 > 0) {
                    pw.println("  Removed from whitelist system apps:");
                    for (int i5 = 0; i5 < size4; i5++) {
                        pw.print("    ");
                        pw.println(this.mRemovedFromSystemWhitelistApps.keyAt(i5));
                    }
                }
                int size5 = this.mPowerSaveWhitelistUserApps.size();
                if (size5 > 0) {
                    pw.println("  Whitelist user apps:");
                    for (int i6 = 0; i6 < size5; i6++) {
                        pw.print("    ");
                        pw.println(this.mPowerSaveWhitelistUserApps.keyAt(i6));
                    }
                }
                int size6 = this.mPowerSaveWhitelistExceptIdleAppIds.size();
                if (size6 > 0) {
                    pw.println("  Whitelist (except idle) all app ids:");
                    for (int i7 = 0; i7 < size6; i7++) {
                        pw.print("    ");
                        pw.print(this.mPowerSaveWhitelistExceptIdleAppIds.keyAt(i7));
                        pw.println();
                    }
                }
                int size7 = this.mPowerSaveWhitelistUserAppIds.size();
                if (size7 > 0) {
                    pw.println("  Whitelist user app ids:");
                    for (int i8 = 0; i8 < size7; i8++) {
                        pw.print("    ");
                        pw.print(this.mPowerSaveWhitelistUserAppIds.keyAt(i8));
                        pw.println();
                    }
                }
                int size8 = this.mPowerSaveWhitelistAllAppIds.size();
                if (size8 > 0) {
                    pw.println("  Whitelist all app ids:");
                    for (int i9 = 0; i9 < size8; i9++) {
                        pw.print("    ");
                        pw.print(this.mPowerSaveWhitelistAllAppIds.keyAt(i9));
                        pw.println();
                    }
                }
                dumpTempWhitelistScheduleLocked(pw, true);
                if (this.mTempWhitelistAppIdArray != null) {
                    size = this.mTempWhitelistAppIdArray.length;
                }
                if (size > 0) {
                    pw.println("  Temp whitelist app ids:");
                    for (int i10 = 0; i10 < size; i10++) {
                        pw.print("    ");
                        pw.print(this.mTempWhitelistAppIdArray[i10]);
                        pw.println();
                    }
                }
                pw.print("  mLightEnabled=");
                pw.print(this.mLightEnabled);
                pw.print("  mDeepEnabled=");
                pw.println(this.mDeepEnabled);
                pw.print("  mForceIdle=");
                pw.println(this.mForceIdle);
                pw.print("  mUseMotionSensor=");
                pw.print(this.mUseMotionSensor);
                if (this.mUseMotionSensor) {
                    pw.print(" mMotionSensor=");
                    pw.println(this.mMotionSensor);
                } else {
                    pw.println();
                }
                pw.print("  mScreenOn=");
                pw.println(this.mScreenOn);
                pw.print("  mScreenLocked=");
                pw.println(this.mScreenLocked);
                pw.print("  mNetworkConnected=");
                pw.println(this.mNetworkConnected);
                pw.print("  mCharging=");
                pw.println(this.mCharging);
                pw.print("  activeEmergencyCall=");
                pw.println(this.mEmergencyCallListener.isEmergencyCallActive());
                if (this.mConstraints.size() != 0) {
                    pw.println("  mConstraints={");
                    for (int i11 = 0; i11 < this.mConstraints.size(); i11++) {
                        com.android.server.deviceidle.DeviceIdleConstraintTracker tracker = this.mConstraints.valueAt(i11);
                        pw.print("    \"");
                        pw.print(tracker.name);
                        pw.print("\"=");
                        if (tracker.minState == this.mState) {
                            pw.println(tracker.active);
                        } else {
                            pw.print("ignored <mMinState=");
                            pw.print(stateToString(tracker.minState));
                            pw.println(">");
                        }
                    }
                    pw.println("  }");
                }
                if (this.mUseMotionSensor || this.mStationaryListeners.size() > 0) {
                    pw.print("  mMotionActive=");
                    pw.println(this.mMotionListener.active);
                    pw.print("  mNotMoving=");
                    pw.println(this.mNotMoving);
                    pw.print("  mMotionListener.activatedTimeElapsed=");
                    pw.println(this.mMotionListener.activatedTimeElapsed);
                    pw.print("  mLastMotionEventElapsed=");
                    pw.println(this.mLastMotionEventElapsed);
                    pw.print("  ");
                    pw.print(this.mStationaryListeners.size());
                    pw.println(" stationary listeners registered");
                }
                if (this.mIsLocationPrefetchEnabled) {
                    pw.print("  mLocating=");
                    pw.print(this.mLocating);
                    pw.print(" mHasGps=");
                    pw.print(this.mHasGps);
                    pw.print(" mHasFused=");
                    pw.print(this.mHasFusedLocation);
                    pw.print(" mLocated=");
                    pw.println(this.mLocated);
                    if (this.mLastGenericLocation != null) {
                        pw.print("  mLastGenericLocation=");
                        pw.println(this.mLastGenericLocation);
                    }
                    if (this.mLastGpsLocation != null) {
                        pw.print("  mLastGpsLocation=");
                        pw.println(this.mLastGpsLocation);
                    }
                } else {
                    pw.println("  Location prefetching disabled");
                }
                pw.print("  mState=");
                pw.print(stateToString(this.mState));
                pw.print(" mLightState=");
                pw.println(lightStateToString(this.mLightState));
                pw.print("  mInactiveTimeout=");
                android.util.TimeUtils.formatDuration(this.mInactiveTimeout, pw);
                pw.println();
                if (this.mActiveIdleOpCount != 0) {
                    pw.print("  mActiveIdleOpCount=");
                    pw.println(this.mActiveIdleOpCount);
                }
                if (this.mNextAlarmTime != 0) {
                    pw.print("  mNextAlarmTime=");
                    android.util.TimeUtils.formatDuration(this.mNextAlarmTime, android.os.SystemClock.elapsedRealtime(), pw);
                    pw.println();
                }
                if (this.mNextIdlePendingDelay != 0) {
                    pw.print("  mNextIdlePendingDelay=");
                    android.util.TimeUtils.formatDuration(this.mNextIdlePendingDelay, pw);
                    pw.println();
                }
                if (this.mNextIdleDelay != 0) {
                    pw.print("  mNextIdleDelay=");
                    android.util.TimeUtils.formatDuration(this.mNextIdleDelay, pw);
                    pw.println();
                }
                if (this.mNextLightIdleDelay != 0) {
                    pw.print("  mNextLightIdleDelay=");
                    android.util.TimeUtils.formatDuration(this.mNextLightIdleDelay, pw);
                    if (this.mConstants.USE_WINDOW_ALARMS) {
                        pw.print(" (flex=");
                        android.util.TimeUtils.formatDuration(this.mNextLightIdleDelayFlex, pw);
                        pw.println(")");
                    } else {
                        pw.println();
                    }
                }
                if (this.mNextLightAlarmTime != 0) {
                    pw.print("  mNextLightAlarmTime=");
                    android.util.TimeUtils.formatDuration(this.mNextLightAlarmTime, android.os.SystemClock.elapsedRealtime(), pw);
                    pw.println();
                }
                if (this.mCurLightIdleBudget != 0) {
                    pw.print("  mCurLightIdleBudget=");
                    android.util.TimeUtils.formatDuration(this.mCurLightIdleBudget, pw);
                    pw.println();
                }
                if (this.mMaintenanceStartTime != 0) {
                    pw.print("  mMaintenanceStartTime=");
                    android.util.TimeUtils.formatDuration(this.mMaintenanceStartTime, android.os.SystemClock.elapsedRealtime(), pw);
                    pw.println();
                }
                if (this.mJobsActive) {
                    pw.print("  mJobsActive=");
                    pw.println(this.mJobsActive);
                }
                if (this.mAlarmsActive) {
                    pw.print("  mAlarmsActive=");
                    pw.println(this.mAlarmsActive);
                }
                if (this.mConstants.USE_MODE_MANAGER) {
                    pw.print("  mModeManagerRequestedQuickDoze=");
                    pw.println(this.mModeManagerRequestedQuickDoze);
                    pw.print("  mIsOffBody=");
                    pw.println(this.mIsOffBody);
                }
            }
        }
    }

    void dumpTempWhitelistScheduleLocked(java.io.PrintWriter pw, boolean printTitle) {
        int size = this.mTempWhitelistAppIdEndTimes.size();
        if (size > 0) {
            java.lang.String prefix = "";
            if (printTitle) {
                pw.println("  Temp whitelist schedule:");
                prefix = "    ";
            }
            long timeNow = android.os.SystemClock.elapsedRealtime();
            for (int i = 0; i < size; i++) {
                pw.print(prefix);
                pw.print("UID=");
                pw.print(this.mTempWhitelistAppIdEndTimes.keyAt(i));
                pw.print(": ");
                android.util.Pair<android.util.MutableLong, java.lang.String> entry = this.mTempWhitelistAppIdEndTimes.valueAt(i);
                android.util.TimeUtils.formatDuration(((android.util.MutableLong) entry.first).value, timeNow, pw);
                pw.print(" - ");
                pw.println((java.lang.String) entry.second);
            }
        }
    }

    public com.android.server.IDeviceIdleControllerWrapper getWrapper() {
        return this.mDeviceIdleControllerWrapper;
    }

    private class DeviceIdleControllerWrapper implements com.android.server.IDeviceIdleControllerWrapper {
        private DeviceIdleControllerWrapper() {
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public int addPowerSaveWhitelistAppsInternal(java.util.List<java.lang.String> pkgNames) {
            return com.android.server.DeviceIdleController.this.addPowerSaveWhitelistAppsInternal(pkgNames);
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public int getState() {
            return com.android.server.DeviceIdleController.this.mState;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public void setState(int state) {
            com.android.server.DeviceIdleController.this.mState = state;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public android.util.ArrayMap<java.lang.String, java.lang.Integer> getPowerSaveWhitelistUserApps() {
            return com.android.server.DeviceIdleController.this.mPowerSaveWhitelistUserApps;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public boolean getDeepEnabled() {
            return com.android.server.DeviceIdleController.this.mDeepEnabled;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public void setDeepEnabled(boolean enabled) {
            com.android.server.DeviceIdleController.this.mDeepEnabled = enabled;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public boolean getLightEnabled() {
            return com.android.server.DeviceIdleController.this.mLightEnabled;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public void setLightEnabled(boolean enabled) {
            com.android.server.DeviceIdleController.this.mLightEnabled = enabled;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public void setActiveReason(int reason) {
            com.android.server.DeviceIdleController.this.mActiveReason = reason;
        }

        @Override // com.android.server.IDeviceIdleControllerWrapper
        public void addPowerSaveWhitelistApps(android.util.ArrayMap<java.lang.String, java.lang.Integer> powerSaveList) {
            if (powerSaveList == null) {
                return;
            }
            synchronized (com.android.server.DeviceIdleController.this) {
                com.android.server.DeviceIdleController.this.mPowerSaveWhitelistAppsExceptIdle.putAll((android.util.ArrayMap) powerSaveList);
                com.android.server.DeviceIdleController.this.mPowerSaveWhitelistApps.putAll((android.util.ArrayMap) powerSaveList);
                com.android.server.DeviceIdleController.this.reportPowerSaveWhitelistChangedLocked();
                com.android.server.DeviceIdleController.this.updateWhitelistAppIdsLocked();
            }
        }
    }
}
