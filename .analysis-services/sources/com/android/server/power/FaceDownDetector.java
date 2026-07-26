package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class FaceDownDetector implements android.hardware.SensorEventListener {
    private static final boolean DEBUG = false;
    static final float DEFAULT_ACCELERATION_THRESHOLD = 0.2f;
    private static final boolean DEFAULT_FEATURE_ENABLED = true;
    private static final long DEFAULT_INTERACTION_BACKOFF = 60000;
    static final long DEFAULT_TIME_THRESHOLD_MILLIS = 1000;
    static final float DEFAULT_Z_ACCELERATION_THRESHOLD = -9.5f;
    static final java.lang.String KEY_ACCELERATION_THRESHOLD = "acceleration_threshold";
    static final java.lang.String KEY_FEATURE_ENABLED = "enable_flip_to_screen_off";
    private static final java.lang.String KEY_INTERACTION_BACKOFF = "face_down_interaction_backoff_millis";
    static final java.lang.String KEY_TIME_THRESHOLD_MILLIS = "time_threshold_millis";
    static final java.lang.String KEY_Z_ACCELERATION_THRESHOLD = "z_acceleration_threshold";
    private static final float MOVING_AVERAGE_WEIGHT = 0.5f;
    private static final int SCREEN_OFF_RESULT = 4;
    private static final java.lang.String TAG = "FaceDownDetector";
    private static final int UNFLIP = 2;
    private static final int UNKNOWN = 1;
    private static final int USER_INTERACTION = 3;
    private float mAccelerationThreshold;
    private android.hardware.Sensor mAccelerometer;
    private android.content.Context mContext;
    private boolean mIsEnabled;
    private final java.util.function.Consumer<java.lang.Boolean> mOnFlip;
    private android.hardware.SensorManager mSensorManager;
    private int mSensorMaxLatencyMicros;
    private java.time.Duration mTimeThreshold;
    private long mUserInteractionBackoffMillis;
    private float mZAccelerationThreshold;
    private float mZAccelerationThresholdLenient;
    private boolean mEnabledOverride = true;
    private long mLastFlipTime = 0;
    public int mPreviousResultType = 1;
    public long mPreviousResultTime = 0;
    private long mMillisSaved = 0;
    private final com.android.server.power.FaceDownDetector.ExponentialMovingAverage mCurrentXYAcceleration = new com.android.server.power.FaceDownDetector.ExponentialMovingAverage(this, 0.5f);
    private final com.android.server.power.FaceDownDetector.ExponentialMovingAverage mCurrentZAcceleration = new com.android.server.power.FaceDownDetector.ExponentialMovingAverage(this, 0.5f);
    private boolean mFaceDown = false;
    private boolean mInteractive = false;
    private boolean mActive = false;
    private float mPrevAcceleration = 0.0f;
    private long mPrevAccelerationTime = 0;
    private boolean mZAccelerationIsFaceDown = false;
    private long mZAccelerationFaceDownTime = 0;
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    final android.content.BroadcastReceiver mScreenReceiver = new com.android.server.power.FaceDownDetector.ScreenStateReceiver();
    private final java.lang.Runnable mUserActivityRunnable = new java.lang.Runnable() { // from class: com.android.server.power.FaceDownDetector$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };

    public FaceDownDetector(java.util.function.Consumer<java.lang.Boolean> onFlip) {
        this.mOnFlip = (java.util.function.Consumer) java.util.Objects.requireNonNull(onFlip);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.mFaceDown) {
            exitFaceDown(3, android.os.SystemClock.uptimeMillis() - this.mLastFlipTime);
            updateActiveState();
        }
    }

    public void systemReady(android.content.Context context) {
        this.mContext = context;
        this.mSensorManager = (android.hardware.SensorManager) context.getSystemService(android.hardware.SensorManager.class);
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(1);
        readValuesFromDeviceConfig();
        android.provider.DeviceConfig.addOnPropertiesChangedListener("attention_manager_service", android.app.ActivityThread.currentApplication().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.power.FaceDownDetector$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$systemReady$1(properties);
            }
        });
        updateActiveState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$1(android.provider.DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    private void registerScreenReceiver(android.content.Context context) {
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.setPriority(1000);
        context.registerReceiver(this.mScreenReceiver, intentFilter, null, com.android.server.OplusIoThread.getHandler());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActiveState() {
        long currentTime = android.os.SystemClock.uptimeMillis();
        boolean sawRecentInteraction = this.mPreviousResultType == 3 && currentTime - this.mPreviousResultTime < this.mUserInteractionBackoffMillis;
        boolean shouldBeActive = this.mInteractive && this.mIsEnabled && !sawRecentInteraction;
        if (this.mActive != shouldBeActive) {
            if (shouldBeActive) {
                this.mSensorManager.registerListener(this, this.mAccelerometer, 3, this.mSensorMaxLatencyMicros, com.android.server.OplusIoThread.getHandler());
                if (this.mPreviousResultType == 4) {
                    logScreenOff();
                }
            } else {
                if (this.mFaceDown && !this.mInteractive) {
                    this.mPreviousResultType = 4;
                    this.mPreviousResultTime = currentTime;
                }
                this.mSensorManager.unregisterListener(this);
                this.mFaceDown = false;
                this.mOnFlip.accept(false);
            }
            this.mActive = shouldBeActive;
        }
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("FaceDownDetector:");
        pw.println("  mFaceDown=" + this.mFaceDown);
        pw.println("  mActive=" + this.mActive);
        pw.println("  mLastFlipTime=" + this.mLastFlipTime);
        pw.println("  mSensorMaxLatencyMicros=" + this.mSensorMaxLatencyMicros);
        pw.println("  mUserInteractionBackoffMillis=" + this.mUserInteractionBackoffMillis);
        pw.println("  mPreviousResultTime=" + this.mPreviousResultTime);
        pw.println("  mPreviousResultType=" + this.mPreviousResultType);
        pw.println("  mMillisSaved=" + this.mMillisSaved);
        pw.println("  mZAccelerationThreshold=" + this.mZAccelerationThreshold);
        pw.println("  mAccelerationThreshold=" + this.mAccelerationThreshold);
        pw.println("  mTimeThreshold=" + this.mTimeThreshold);
        pw.println("  mEnabledOverride=" + this.mEnabledOverride);
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(android.hardware.SensorEvent event) {
        if (event.sensor.getType() != 1 || !this.mActive || !this.mIsEnabled) {
            return;
        }
        float x = event.values[0];
        float y = event.values[1];
        this.mCurrentXYAcceleration.updateMovingAverage((x * x) + (y * y));
        this.mCurrentZAcceleration.updateMovingAverage(event.values[2]);
        long curTime = event.timestamp;
        if (java.lang.Math.abs(this.mCurrentXYAcceleration.mMovingAverage - this.mPrevAcceleration) > this.mAccelerationThreshold) {
            this.mPrevAcceleration = this.mCurrentXYAcceleration.mMovingAverage;
            this.mPrevAccelerationTime = curTime;
        }
        boolean moving = curTime - this.mPrevAccelerationTime <= this.mTimeThreshold.toNanos();
        float zAccelerationThreshold = this.mFaceDown ? this.mZAccelerationThresholdLenient : this.mZAccelerationThreshold;
        boolean isCurrentlyFaceDown = this.mCurrentZAcceleration.mMovingAverage < zAccelerationThreshold;
        boolean isFaceDownForPeriod = isCurrentlyFaceDown && this.mZAccelerationIsFaceDown && curTime - this.mZAccelerationFaceDownTime > this.mTimeThreshold.toNanos();
        if (isCurrentlyFaceDown && !this.mZAccelerationIsFaceDown) {
            this.mZAccelerationFaceDownTime = curTime;
            this.mZAccelerationIsFaceDown = true;
        } else if (!isCurrentlyFaceDown) {
            this.mZAccelerationIsFaceDown = false;
        }
        if (!moving && isFaceDownForPeriod && !this.mFaceDown) {
            faceDownDetected();
        } else if (!isFaceDownForPeriod && this.mFaceDown) {
            unFlipDetected();
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    }

    private void faceDownDetected() {
        this.mLastFlipTime = android.os.SystemClock.uptimeMillis();
        this.mFaceDown = true;
        this.mOnFlip.accept(true);
    }

    private void unFlipDetected() {
        exitFaceDown(2, android.os.SystemClock.uptimeMillis() - this.mLastFlipTime);
    }

    public void userActivity(int event) {
        if (event != 5) {
            com.android.server.OplusIoThread.getHandler().post(this.mUserActivityRunnable);
        }
    }

    private void exitFaceDown(int resultType, long millisSinceFlip) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.FACE_DOWN_REPORTED, resultType, millisSinceFlip, 0L, 0L);
        this.mFaceDown = false;
        this.mLastFlipTime = 0L;
        this.mPreviousResultType = resultType;
        this.mPreviousResultTime = android.os.SystemClock.uptimeMillis();
        this.mOnFlip.accept(false);
    }

    private void logScreenOff() {
        long currentTime = android.os.SystemClock.uptimeMillis();
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.FACE_DOWN_REPORTED, 4, this.mPreviousResultTime - this.mLastFlipTime, this.mMillisSaved, currentTime - this.mPreviousResultTime);
        this.mPreviousResultType = 1;
    }

    private boolean isEnabled() {
        return this.mEnabledOverride && android.provider.DeviceConfig.getBoolean("attention_manager_service", KEY_FEATURE_ENABLED, true) && this.mContext.getResources().getBoolean(android.R.bool.config_enable_a11y_magnification_single_panning);
    }

    private float getAccelerationThreshold() {
        return getFloatFlagValue(KEY_ACCELERATION_THRESHOLD, DEFAULT_ACCELERATION_THRESHOLD, -2.0f, 2.0f);
    }

    private float getZAccelerationThreshold() {
        return getFloatFlagValue(KEY_Z_ACCELERATION_THRESHOLD, DEFAULT_Z_ACCELERATION_THRESHOLD, -15.0f, 0.0f);
    }

    private long getUserInteractionBackoffMillis() {
        return getLongFlagValue(KEY_INTERACTION_BACKOFF, 60000L, 0L, 3600000L);
    }

    private int getSensorMaxLatencyMicros() {
        return this.mContext.getResources().getInteger(android.R.integer.config_dynamicPowerSavingsDefaultDisableThreshold);
    }

    private float getFloatFlagValue(java.lang.String key, float defaultValue, float min, float max) {
        float value = android.provider.DeviceConfig.getFloat("attention_manager_service", key, defaultValue);
        if (value < min || value > max) {
            android.util.Slog.w(TAG, "Bad flag value supplied for: " + key);
            return defaultValue;
        }
        return value;
    }

    private long getLongFlagValue(java.lang.String key, long defaultValue, long min, long max) {
        long value = android.provider.DeviceConfig.getLong("attention_manager_service", key, defaultValue);
        if (value < min || value > max) {
            android.util.Slog.w(TAG, "Bad flag value supplied for: " + key);
            return defaultValue;
        }
        return value;
    }

    private java.time.Duration getTimeThreshold() {
        long millis = android.provider.DeviceConfig.getLong("attention_manager_service", KEY_TIME_THRESHOLD_MILLIS, 1000L);
        if (millis < 0 || millis > 15000) {
            android.util.Slog.w(TAG, "Bad flag value supplied for: time_threshold_millis");
            return java.time.Duration.ofMillis(1000L);
        }
        return java.time.Duration.ofMillis(millis);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0042  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void onDeviceConfigChange(java.util.Set<java.lang.String> r5) {
        /*
            r4 = this;
            java.util.Iterator r0 = r5.iterator()
        L4:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L66
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            int r2 = r1.hashCode()
            switch(r2) {
                case -1974380596: goto L37;
                case -1762356372: goto L2d;
                case -1566292150: goto L23;
                case 941263057: goto L18;
                default: goto L17;
            }
        L17:
            goto L42
        L18:
            java.lang.String r2 = "z_acceleration_threshold"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L17
            r2 = 1
            goto L43
        L23:
            java.lang.String r2 = "enable_flip_to_screen_off"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L17
            r2 = 3
            goto L43
        L2d:
            java.lang.String r2 = "acceleration_threshold"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L17
            r2 = 0
            goto L43
        L37:
            java.lang.String r2 = "time_threshold_millis"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L17
            r2 = 2
            goto L43
        L42:
            r2 = -1
        L43:
            switch(r2) {
                case 0: goto L5f;
                case 1: goto L5f;
                case 2: goto L5f;
                case 3: goto L5f;
                default: goto L46;
            }
        L46:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Ignoring change on "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r1)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "FaceDownDetector"
            android.util.Slog.i(r3, r2)
            goto L4
        L5f:
            r4.readValuesFromDeviceConfig()
            r4.updateActiveState()
            return
        L66:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.FaceDownDetector.onDeviceConfigChange(java.util.Set):void");
    }

    private void readValuesFromDeviceConfig() {
        this.mAccelerationThreshold = getAccelerationThreshold();
        this.mZAccelerationThreshold = getZAccelerationThreshold();
        this.mZAccelerationThresholdLenient = this.mZAccelerationThreshold + 1.0f;
        this.mTimeThreshold = getTimeThreshold();
        this.mSensorMaxLatencyMicros = getSensorMaxLatencyMicros();
        this.mUserInteractionBackoffMillis = getUserInteractionBackoffMillis();
        boolean oldEnabled = this.mIsEnabled;
        this.mIsEnabled = isEnabled();
        if (oldEnabled != this.mIsEnabled) {
            if (!this.mIsEnabled) {
                this.mContext.unregisterReceiver(this.mScreenReceiver);
                this.mInteractive = false;
            } else {
                registerScreenReceiver(this.mContext);
                this.mInteractive = ((android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class)).isInteractive();
            }
        }
        android.util.Slog.i(TAG, "readValuesFromDeviceConfig():\nmAccelerationThreshold=" + this.mAccelerationThreshold + "\nmZAccelerationThreshold=" + this.mZAccelerationThreshold + "\nmTimeThreshold=" + this.mTimeThreshold + "\nmIsEnabled=" + this.mIsEnabled);
    }

    public void setEnabledOverride(boolean enabled) {
        this.mEnabledOverride = enabled;
        this.mIsEnabled = isEnabled();
    }

    public void setMillisSaved(long millisSaved) {
        this.mMillisSaved = millisSaved;
    }

    private final class ScreenStateReceiver extends android.content.BroadcastReceiver {
        private ScreenStateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                com.android.server.power.FaceDownDetector.this.mInteractive = false;
                com.android.server.power.FaceDownDetector.this.updateActiveState();
            } else if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                com.android.server.power.FaceDownDetector.this.mInteractive = true;
                com.android.server.power.FaceDownDetector.this.updateActiveState();
            }
        }
    }

    private final class ExponentialMovingAverage {
        private final float mAlpha;
        private final float mInitialAverage;
        private float mMovingAverage;

        ExponentialMovingAverage(com.android.server.power.FaceDownDetector faceDownDetector, float alpha) {
            this(alpha, 0.0f);
        }

        ExponentialMovingAverage(float alpha, float initialAverage) {
            this.mAlpha = alpha;
            this.mInitialAverage = initialAverage;
            this.mMovingAverage = initialAverage;
        }

        void updateMovingAverage(float newValue) {
            this.mMovingAverage = (this.mAlpha * (this.mMovingAverage - newValue)) + newValue;
        }

        void reset() {
            this.mMovingAverage = this.mInitialAverage;
        }
    }
}
