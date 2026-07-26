package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class GestureLauncherService extends com.android.server.SystemService {
    static final long CAMERA_POWER_DOUBLE_TAP_MAX_TIME_MS = 300;
    private static final int CAMERA_POWER_TAP_COUNT_THRESHOLD = 2;
    private static final boolean DBG = false;
    private static final boolean DBG_CAMERA_LIFT = false;
    private static final int EMERGENCY_GESTURE_POWER_BUTTON_COOLDOWN_PERIOD_MS_DEFAULT = 3000;
    static final int EMERGENCY_GESTURE_POWER_BUTTON_COOLDOWN_PERIOD_MS_MAX = 5000;
    private static final int EMERGENCY_GESTURE_POWER_TAP_COUNT_THRESHOLD = 5;
    static final long POWER_SHORT_TAP_SEQUENCE_MAX_INTERVAL_MS = 500;
    private static final java.lang.String TAG = "GestureLauncherService";
    private boolean mCameraDoubleTapPowerEnabled;
    private long mCameraGestureLastEventTime;
    private long mCameraGestureOnTimeMs;
    private long mCameraGestureSensor1LastOnTimeMs;
    private long mCameraGestureSensor2LastOnTimeMs;
    private int mCameraLaunchLastEventExtra;
    private boolean mCameraLaunchRegistered;
    private android.hardware.Sensor mCameraLaunchSensor;
    private boolean mCameraLiftRegistered;
    private final com.android.server.GestureLauncherService.CameraLiftTriggerEventListener mCameraLiftTriggerListener;
    private android.hardware.Sensor mCameraLiftTriggerSensor;
    private android.content.Context mContext;
    private boolean mEmergencyGestureEnabled;
    private int mEmergencyGesturePowerButtonCooldownPeriodMs;
    private long mFirstPowerDown;
    com.android.server.IGestureLauncherServiceExt mGestureLauncherServiceExt;
    private final com.android.server.GestureLauncherService.GestureEventListener mGestureListener;
    private boolean mHasFeatureWatch;
    private long mLastEmergencyGestureTriggered;
    private long mLastPowerDown;
    private final com.android.internal.logging.MetricsLogger mMetricsLogger;
    private int mPowerButtonConsecutiveTaps;
    private int mPowerButtonSlowConsecutiveTaps;
    private android.os.PowerManager mPowerManager;
    private final android.database.ContentObserver mSettingObserver;
    private final com.android.internal.logging.UiEventLogger mUiEventLogger;
    private int mUserId;
    private final android.content.BroadcastReceiver mUserReceiver;
    private android.os.PowerManager.WakeLock mWakeLock;
    private com.android.server.wm.WindowManagerInternal mWindowManagerInternal;

    public enum GestureLauncherEvent implements com.android.internal.logging.UiEventLogger.UiEventEnum {
        GESTURE_CAMERA_LIFT(com.android.internal.util.FrameworkStatsLog.EXPRESS_UID_HISTOGRAM_SAMPLE_REPORTED),
        GESTURE_CAMERA_WIGGLE(com.android.internal.util.FrameworkStatsLog.AUTOFILL_FIELD_CLASSIFICATION_EVENT_REPORTED),
        GESTURE_CAMERA_DOUBLE_TAP_POWER(660),
        GESTURE_EMERGENCY_TAP_POWER(661);

        private final int mId;

        GestureLauncherEvent(int id) {
            this.mId = id;
        }

        public int getId() {
            return this.mId;
        }
    }

    public GestureLauncherService(android.content.Context context) {
        this(context, new com.android.internal.logging.MetricsLogger(), new com.android.internal.logging.UiEventLoggerImpl());
    }

    /* JADX WARN: Multi-variable type inference failed */
    GestureLauncherService(android.content.Context context, com.android.internal.logging.MetricsLogger metricsLogger, com.android.internal.logging.UiEventLogger uiEventLogger) {
        super(context);
        this.mGestureListener = new com.android.server.GestureLauncherService.GestureEventListener();
        this.mCameraLiftTriggerListener = new com.android.server.GestureLauncherService.CameraLiftTriggerEventListener();
        this.mCameraGestureOnTimeMs = 0L;
        this.mCameraGestureLastEventTime = 0L;
        this.mCameraGestureSensor1LastOnTimeMs = 0L;
        this.mCameraGestureSensor2LastOnTimeMs = 0L;
        this.mCameraLaunchLastEventExtra = 0;
        this.mGestureLauncherServiceExt = (com.android.server.IGestureLauncherServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.IGestureLauncherServiceExt.class).create();
        this.mUserReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.GestureLauncherService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                    com.android.server.GestureLauncherService.this.mUserId = intent.getIntExtra("android.intent.extra.user_handle", 0);
                    com.android.server.GestureLauncherService.this.mContext.getContentResolver().unregisterContentObserver(com.android.server.GestureLauncherService.this.mSettingObserver);
                    com.android.server.GestureLauncherService.this.registerContentObservers();
                    com.android.server.GestureLauncherService.this.updateCameraRegistered();
                    com.android.server.GestureLauncherService.this.updateCameraDoubleTapPowerEnabled();
                    com.android.server.GestureLauncherService.this.updateEmergencyGestureEnabled();
                    com.android.server.GestureLauncherService.this.updateEmergencyGesturePowerButtonCooldownPeriodMs();
                }
            }
        };
        this.mSettingObserver = new android.database.ContentObserver(new android.os.Handler()) { // from class: com.android.server.GestureLauncherService.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
                if (userId == com.android.server.GestureLauncherService.this.mUserId) {
                    com.android.server.GestureLauncherService.this.updateCameraRegistered();
                    com.android.server.GestureLauncherService.this.updateCameraDoubleTapPowerEnabled();
                    com.android.server.GestureLauncherService.this.updateEmergencyGestureEnabled();
                    com.android.server.GestureLauncherService.this.updateEmergencyGesturePowerButtonCooldownPeriodMs();
                }
            }
        };
        this.mContext = context;
        this.mMetricsLogger = metricsLogger;
        this.mUiEventLogger = uiEventLogger;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        com.android.server.LocalServices.addService(com.android.server.GestureLauncherService.class, this);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 600) {
            android.content.res.Resources resources = this.mContext.getResources();
            if (!isGestureLauncherEnabled(resources)) {
                return;
            }
            this.mWindowManagerInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
            this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService("power");
            this.mWakeLock = this.mPowerManager.newWakeLock(1, TAG);
            updateCameraRegistered();
            updateCameraDoubleTapPowerEnabled();
            updateEmergencyGestureEnabled();
            updateEmergencyGesturePowerButtonCooldownPeriodMs();
            this.mUserId = android.app.ActivityManager.getCurrentUser();
            this.mContext.registerReceiver(this.mUserReceiver, new android.content.IntentFilter("android.intent.action.USER_SWITCHED"));
            registerContentObservers();
            this.mHasFeatureWatch = this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.watch");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerContentObservers() {
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("camera_gesture_disabled"), false, this.mSettingObserver, this.mUserId);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("camera_double_tap_power_gesture_disabled"), false, this.mSettingObserver, this.mUserId);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("camera_lift_trigger_enabled"), false, this.mSettingObserver, this.mUserId);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("emergency_gesture_enabled"), false, this.mSettingObserver, this.mUserId);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("emergency_gesture_power_button_cooldown_period_ms"), false, this.mSettingObserver, this.mUserId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCameraRegistered() {
        android.content.res.Resources resources = this.mContext.getResources();
        if (isCameraLaunchSettingEnabled(this.mContext, this.mUserId)) {
            registerCameraLaunchGesture(resources);
        } else {
            unregisterCameraLaunchGesture();
        }
        if (isCameraLiftTriggerSettingEnabled(this.mContext, this.mUserId)) {
            registerCameraLiftTrigger(resources);
        } else {
            unregisterCameraLiftTrigger();
        }
    }

    void updateCameraDoubleTapPowerEnabled() {
        boolean enabled = isCameraDoubleTapPowerSettingEnabled(this.mContext, this.mUserId);
        synchronized (this) {
            this.mCameraDoubleTapPowerEnabled = enabled;
        }
    }

    void updateEmergencyGestureEnabled() {
        synchronized (this) {
            this.mEmergencyGestureEnabled = false;
        }
    }

    void updateEmergencyGesturePowerButtonCooldownPeriodMs() {
        int cooldownPeriodMs = getEmergencyGesturePowerButtonCooldownPeriodMs(this.mContext, this.mUserId);
        synchronized (this) {
            this.mEmergencyGesturePowerButtonCooldownPeriodMs = cooldownPeriodMs;
        }
    }

    private void unregisterCameraLaunchGesture() {
        if (this.mCameraLaunchRegistered) {
            this.mCameraLaunchRegistered = false;
            this.mCameraGestureOnTimeMs = 0L;
            this.mCameraGestureLastEventTime = 0L;
            this.mCameraGestureSensor1LastOnTimeMs = 0L;
            this.mCameraGestureSensor2LastOnTimeMs = 0L;
            this.mCameraLaunchLastEventExtra = 0;
            android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) this.mContext.getSystemService(com.android.server.am.IOplusSceneManager.APP_SCENE_SENSOR);
            sensorManager.unregisterListener(this.mGestureListener);
        }
    }

    private void registerCameraLaunchGesture(android.content.res.Resources resources) {
        if (this.mCameraLaunchRegistered) {
            return;
        }
        this.mCameraGestureOnTimeMs = android.os.SystemClock.elapsedRealtime();
        this.mCameraGestureLastEventTime = this.mCameraGestureOnTimeMs;
        android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) this.mContext.getSystemService(com.android.server.am.IOplusSceneManager.APP_SCENE_SENSOR);
        int cameraLaunchGestureId = resources.getInteger(android.R.integer.config_burnInProtectionMaxRadius);
        if (cameraLaunchGestureId != -1) {
            this.mCameraLaunchRegistered = false;
            java.lang.String sensorName = resources.getString(android.R.string.config_controlsPackage);
            this.mCameraLaunchSensor = sensorManager.getDefaultSensor(cameraLaunchGestureId, true);
            if (this.mCameraLaunchSensor != null) {
                if (sensorName.equals(this.mCameraLaunchSensor.getStringType())) {
                    this.mCameraLaunchRegistered = sensorManager.registerListener(this.mGestureListener, this.mCameraLaunchSensor, 0);
                } else {
                    java.lang.String message = java.lang.String.format("Wrong configuration. Sensor type and sensor string type don't match: %s in resources, %s in the sensor.", sensorName, this.mCameraLaunchSensor.getStringType());
                    throw new java.lang.RuntimeException(message);
                }
            }
        }
    }

    private void unregisterCameraLiftTrigger() {
        if (this.mCameraLiftRegistered) {
            this.mCameraLiftRegistered = false;
            android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) this.mContext.getSystemService(com.android.server.am.IOplusSceneManager.APP_SCENE_SENSOR);
            sensorManager.cancelTriggerSensor(this.mCameraLiftTriggerListener, this.mCameraLiftTriggerSensor);
        }
    }

    private void registerCameraLiftTrigger(android.content.res.Resources resources) {
        if (this.mCameraLiftRegistered) {
            return;
        }
        android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) this.mContext.getSystemService(com.android.server.am.IOplusSceneManager.APP_SCENE_SENSOR);
        int cameraLiftTriggerId = resources.getInteger(android.R.integer.config_burnInProtectionMaxVerticalOffset);
        if (cameraLiftTriggerId != -1) {
            this.mCameraLiftRegistered = false;
            java.lang.String sensorName = resources.getString(android.R.string.config_credentialManagerReceiverComponent);
            this.mCameraLiftTriggerSensor = sensorManager.getDefaultSensor(cameraLiftTriggerId, true);
            if (this.mCameraLiftTriggerSensor != null) {
                if (sensorName.equals(this.mCameraLiftTriggerSensor.getStringType())) {
                    this.mCameraLiftRegistered = sensorManager.requestTriggerSensor(this.mCameraLiftTriggerListener, this.mCameraLiftTriggerSensor);
                } else {
                    java.lang.String message = java.lang.String.format("Wrong configuration. Sensor type and sensor string type don't match: %s in resources, %s in the sensor.", sensorName, this.mCameraLiftTriggerSensor.getStringType());
                    throw new java.lang.RuntimeException(message);
                }
            }
        }
    }

    public static boolean isCameraLaunchSettingEnabled(android.content.Context context, int userId) {
        return isCameraLaunchEnabled(context.getResources()) && android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "camera_gesture_disabled", 0, userId) == 0;
    }

    public static boolean isCameraDoubleTapPowerSettingEnabled(android.content.Context context, int userId) {
        return isCameraDoubleTapPowerEnabled(context.getResources()) && android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "camera_double_tap_power_gesture_disabled", 0, userId) == 0;
    }

    public static boolean isCameraLiftTriggerSettingEnabled(android.content.Context context, int userId) {
        return isCameraLiftTriggerEnabled(context.getResources()) && android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "camera_lift_trigger_enabled", 1, userId) != 0;
    }

    public static boolean isEmergencyGestureSettingEnabled(android.content.Context context, int i) {
        return isEmergencyGestureEnabled(context.getResources()) && android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "emergency_gesture_enabled", isDefaultEmergencyGestureEnabled(context.getResources()) ? 1 : 0, i) != 0;
    }

    static int getEmergencyGesturePowerButtonCooldownPeriodMs(android.content.Context context, int userId) {
        int cooldown = android.provider.Settings.Global.getInt(context.getContentResolver(), "emergency_gesture_power_button_cooldown_period_ms", 3000);
        return java.lang.Math.min(cooldown, 5000);
    }

    private static boolean isCameraLaunchEnabled(android.content.res.Resources resources) {
        boolean configSet = resources.getInteger(android.R.integer.config_burnInProtectionMaxRadius) != -1;
        return configSet && !android.os.SystemProperties.getBoolean("gesture.disable_camera_launch", false);
    }

    static boolean isCameraDoubleTapPowerEnabled(android.content.res.Resources resources) {
        return resources.getBoolean(android.R.bool.config_camera_sound_forced);
    }

    private static boolean isCameraLiftTriggerEnabled(android.content.res.Resources resources) {
        return resources.getInteger(android.R.integer.config_burnInProtectionMaxVerticalOffset) != -1;
    }

    private static boolean isEmergencyGestureEnabled(android.content.res.Resources resources) {
        return resources.getBoolean(android.R.bool.config_earcFeatureDisabled_default);
    }

    private static boolean isDefaultEmergencyGestureEnabled(android.content.res.Resources resources) {
        return resources.getBoolean(android.R.bool.config_defaultEmergencyGestureSoundEnabled);
    }

    public static boolean isGestureLauncherEnabled(android.content.res.Resources resources) {
        return isCameraLaunchEnabled(resources) || isCameraDoubleTapPowerEnabled(resources) || isCameraLiftTriggerEnabled(resources) || isEmergencyGestureEnabled(resources);
    }

    public boolean interceptPowerKeyDown(android.view.KeyEvent event, boolean interactive, android.util.MutableBoolean outLaunched) {
        long powerTapInterval;
        boolean z;
        if (this.mEmergencyGestureEnabled && this.mEmergencyGesturePowerButtonCooldownPeriodMs >= 0 && event.getEventTime() - this.mLastEmergencyGestureTriggered < this.mEmergencyGesturePowerButtonCooldownPeriodMs) {
            android.util.Slog.i(TAG, java.lang.String.format("Suppressing power button: within %dms cooldown period after Emergency Gesture. Begin=%dms, end=%dms.", java.lang.Integer.valueOf(this.mEmergencyGesturePowerButtonCooldownPeriodMs), java.lang.Long.valueOf(this.mLastEmergencyGestureTriggered), java.lang.Long.valueOf(this.mLastEmergencyGestureTriggered + ((long) this.mEmergencyGesturePowerButtonCooldownPeriodMs))));
            outLaunched.value = false;
            return true;
        }
        if (event.isLongPress()) {
            outLaunched.value = false;
            return false;
        }
        boolean launchCamera = false;
        boolean launchEmergencyGesture = false;
        boolean intercept = false;
        synchronized (this) {
            powerTapInterval = event.getEventTime() - this.mLastPowerDown;
            this.mLastPowerDown = event.getEventTime();
            if (powerTapInterval >= 500) {
                this.mFirstPowerDown = event.getEventTime();
                this.mPowerButtonConsecutiveTaps = 1;
                this.mPowerButtonSlowConsecutiveTaps = 1;
            } else if (powerTapInterval >= CAMERA_POWER_DOUBLE_TAP_MAX_TIME_MS) {
                this.mFirstPowerDown = event.getEventTime();
                this.mPowerButtonConsecutiveTaps = 1;
                this.mPowerButtonSlowConsecutiveTaps++;
            } else {
                this.mPowerButtonConsecutiveTaps++;
                this.mPowerButtonSlowConsecutiveTaps++;
            }
            if (this.mEmergencyGestureEnabled) {
                if (this.mPowerButtonConsecutiveTaps > (this.mHasFeatureWatch ? 5 : 1)) {
                    intercept = interactive;
                }
                if (this.mPowerButtonConsecutiveTaps == 5) {
                    long emergencyGestureSpentTime = event.getEventTime() - this.mFirstPowerDown;
                    long emergencyGestureTapDetectionMinTimeMs = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "emergency_gesture_tap_detection_min_time_ms", this.mContext.getResources().getInteger(android.R.integer.config_defaultBinderHeavyHitterAutoSamplerBatchSize));
                    if (emergencyGestureSpentTime <= emergencyGestureTapDetectionMinTimeMs) {
                        android.util.Slog.i(TAG, "Emergency gesture detected but it's too fast. Gesture time: " + emergencyGestureSpentTime + " ms");
                        this.mFirstPowerDown = event.getEventTime();
                        this.mPowerButtonConsecutiveTaps = 1;
                        this.mPowerButtonSlowConsecutiveTaps = 1;
                    } else {
                        android.util.Slog.i(TAG, "Emergency gesture detected. Gesture time: " + emergencyGestureSpentTime + " ms");
                        launchEmergencyGesture = true;
                        this.mMetricsLogger.histogram("emergency_gesture_spent_time", (int) emergencyGestureSpentTime);
                    }
                }
            }
            if (this.mCameraDoubleTapPowerEnabled && powerTapInterval < CAMERA_POWER_DOUBLE_TAP_MAX_TIME_MS && this.mPowerButtonConsecutiveTaps == 2) {
                launchCamera = true;
                intercept = interactive;
            }
        }
        if (this.mPowerButtonConsecutiveTaps > 1 || this.mPowerButtonSlowConsecutiveTaps > 1) {
            android.util.Slog.i(TAG, java.lang.Long.valueOf(this.mPowerButtonConsecutiveTaps) + " consecutive power button taps detected, " + java.lang.Long.valueOf(this.mPowerButtonSlowConsecutiveTaps) + " consecutive slow power button taps detected");
        }
        if (launchCamera) {
            if (this.mGestureLauncherServiceExt.interceptPowerKeyDownForCamera()) {
                z = false;
            } else {
                android.util.Slog.i(TAG, "Power button double tap gesture detected, launching camera. Interval=" + powerTapInterval + "ms");
                z = false;
                launchCamera = handleCameraGesture(false, 1);
                if (launchCamera) {
                    this.mMetricsLogger.action(255, (int) powerTapInterval);
                    this.mUiEventLogger.log(com.android.server.GestureLauncherService.GestureLauncherEvent.GESTURE_CAMERA_DOUBLE_TAP_POWER);
                }
            }
        } else {
            z = false;
            if (launchEmergencyGesture) {
                android.util.Slog.i(TAG, "Emergency gesture detected, launching.");
                launchEmergencyGesture = handleEmergencyGesture();
                this.mUiEventLogger.log(com.android.server.GestureLauncherService.GestureLauncherEvent.GESTURE_EMERGENCY_TAP_POWER);
                if (launchEmergencyGesture) {
                    synchronized (this) {
                        this.mLastEmergencyGestureTriggered = event.getEventTime();
                    }
                }
            }
        }
        this.mMetricsLogger.histogram("power_consecutive_short_tap_count", this.mPowerButtonSlowConsecutiveTaps);
        this.mMetricsLogger.histogram("power_double_tap_interval", (int) powerTapInterval);
        outLaunched.value = (launchCamera || launchEmergencyGesture) ? true : z;
        if (intercept && isUserSetupComplete()) {
            return true;
        }
        return z;
    }

    boolean handleCameraGesture(boolean useWakelock, int source) {
        android.os.Trace.traceBegin(64L, "GestureLauncher:handleCameraGesture");
        try {
            boolean userSetupComplete = isUserSetupComplete();
            if (userSetupComplete) {
                if (useWakelock) {
                    this.mWakeLock.acquire(500L);
                }
                com.android.server.statusbar.StatusBarManagerInternal service = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
                service.onCameraLaunchGestureDetected(source);
                android.os.Trace.traceEnd(64L);
                return true;
            }
            android.os.Trace.traceEnd(64L);
            return false;
        } catch (java.lang.Throwable th) {
            android.os.Trace.traceEnd(64L);
            throw th;
        }
    }

    boolean handleEmergencyGesture() {
        android.os.Trace.traceBegin(64L, "GestureLauncher:handleEmergencyGesture");
        try {
            boolean userSetupComplete = isUserSetupComplete();
            if (userSetupComplete) {
                com.android.server.statusbar.StatusBarManagerInternal service = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
                service.onEmergencyActionLaunchGestureDetected();
                android.os.Trace.traceEnd(64L);
                return true;
            }
            android.os.Trace.traceEnd(64L);
            return false;
        } catch (java.lang.Throwable th) {
            android.os.Trace.traceEnd(64L);
            throw th;
        }
    }

    private boolean isUserSetupComplete() {
        return android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "user_setup_complete", 0, -2) != 0;
    }

    private final class GestureEventListener implements android.hardware.SensorEventListener {
        private GestureEventListener() {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(android.hardware.SensorEvent event) {
            if (com.android.server.GestureLauncherService.this.mCameraLaunchRegistered && event.sensor == com.android.server.GestureLauncherService.this.mCameraLaunchSensor && com.android.server.GestureLauncherService.this.handleCameraGesture(true, 0)) {
                com.android.server.GestureLauncherService.this.mMetricsLogger.action(256);
                com.android.server.GestureLauncherService.this.mUiEventLogger.log(com.android.server.GestureLauncherService.GestureLauncherEvent.GESTURE_CAMERA_WIGGLE);
                trackCameraLaunchEvent(event);
            }
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        }

        private void trackCameraLaunchEvent(android.hardware.SensorEvent event) {
            long now = android.os.SystemClock.elapsedRealtime();
            long totalDuration = now - com.android.server.GestureLauncherService.this.mCameraGestureOnTimeMs;
            float[] values = event.values;
            long sensor1OnTime = (long) (totalDuration * ((double) values[0]));
            long sensor2OnTime = (long) (totalDuration * ((double) values[1]));
            int extra = (int) values[2];
            long gestureOnTimeDiff = now - com.android.server.GestureLauncherService.this.mCameraGestureLastEventTime;
            long sensor1OnTimeDiff = sensor1OnTime - com.android.server.GestureLauncherService.this.mCameraGestureSensor1LastOnTimeMs;
            long sensor2OnTimeDiff = sensor2OnTime - com.android.server.GestureLauncherService.this.mCameraGestureSensor2LastOnTimeMs;
            int extraDiff = extra - com.android.server.GestureLauncherService.this.mCameraLaunchLastEventExtra;
            if (gestureOnTimeDiff < 0 || sensor1OnTimeDiff < 0 || sensor2OnTimeDiff < 0) {
                return;
            }
            com.android.server.EventLogTags.writeCameraGestureTriggered(gestureOnTimeDiff, sensor1OnTimeDiff, sensor2OnTimeDiff, extraDiff);
            com.android.server.GestureLauncherService.this.mCameraGestureLastEventTime = now;
            com.android.server.GestureLauncherService.this.mCameraGestureSensor1LastOnTimeMs = sensor1OnTime;
            com.android.server.GestureLauncherService.this.mCameraGestureSensor2LastOnTimeMs = sensor2OnTime;
            com.android.server.GestureLauncherService.this.mCameraLaunchLastEventExtra = extra;
        }
    }

    private final class CameraLiftTriggerEventListener extends android.hardware.TriggerEventListener {
        private CameraLiftTriggerEventListener() {
        }

        @Override // android.hardware.TriggerEventListener
        public void onTrigger(android.hardware.TriggerEvent event) {
            if (com.android.server.GestureLauncherService.this.mCameraLiftRegistered && event.sensor == com.android.server.GestureLauncherService.this.mCameraLiftTriggerSensor) {
                com.android.server.GestureLauncherService.this.mContext.getResources();
                android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) com.android.server.GestureLauncherService.this.mContext.getSystemService(com.android.server.am.IOplusSceneManager.APP_SCENE_SENSOR);
                boolean keyguardShowingAndNotOccluded = com.android.server.GestureLauncherService.this.mWindowManagerInternal.isKeyguardShowingAndNotOccluded();
                boolean interactive = com.android.server.GestureLauncherService.this.mPowerManager.isInteractive();
                if ((keyguardShowingAndNotOccluded || !interactive) && com.android.server.GestureLauncherService.this.handleCameraGesture(true, 2)) {
                    com.android.internal.logging.MetricsLogger.action(com.android.server.GestureLauncherService.this.mContext, 989);
                    com.android.server.GestureLauncherService.this.mUiEventLogger.log(com.android.server.GestureLauncherService.GestureLauncherEvent.GESTURE_CAMERA_LIFT);
                }
                com.android.server.GestureLauncherService.this.mCameraLiftRegistered = sensorManager.requestTriggerSensor(com.android.server.GestureLauncherService.this.mCameraLiftTriggerListener, com.android.server.GestureLauncherService.this.mCameraLiftTriggerSensor);
            }
        }
    }
}
