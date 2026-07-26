package com.android.server.sensorprivacy;

/* JADX INFO: loaded from: classes3.dex */
class CameraPrivacyLightController implements android.app.AppOpsManager.OnOpActiveChangedListener, android.hardware.SensorEventListener {
    private static final double LIGHT_VALUE_MULTIPLIER = 1.0d / java.lang.Math.log(1.1d);
    private final java.util.Set<java.lang.String> mActivePackages;
    private final java.util.Set<java.lang.String> mActivePhonePackages;
    private long mAlvSum;
    private final java.util.ArrayDeque<android.util.Pair<java.lang.Long, java.lang.Integer>> mAmbientLightValues;
    private final android.app.AppOpsManager mAppOpsManager;
    private final java.util.List<android.hardware.lights.Light> mCameraLights;
    private final int[] mColors;
    private final android.content.Context mContext;
    private final java.lang.Object mDelayedUpdateToken;
    private long mElapsedRealTime;
    private long mElapsedTimeStartedReading;
    private final java.util.concurrent.Executor mExecutor;
    private final android.os.Handler mHandler;
    private boolean mIsAmbientLightListenerRegistered;
    private int mLastLightColor;
    private final android.hardware.Sensor mLightSensor;
    private final android.hardware.lights.LightsManager mLightsManager;
    private android.hardware.lights.LightsManager.LightsSession mLightsSession;
    private final long mMovingAverageIntervalMillis;
    private final android.hardware.SensorManager mSensorManager;
    private final long[] mThresholds;

    CameraPrivacyLightController(android.content.Context context) {
        this(context, com.android.server.FgThread.get().getLooper());
    }

    CameraPrivacyLightController(android.content.Context context, android.os.Looper looper) {
        this.mActivePackages = new android.util.ArraySet();
        this.mActivePhonePackages = new android.util.ArraySet();
        this.mCameraLights = new java.util.ArrayList();
        this.mLightsSession = null;
        this.mIsAmbientLightListenerRegistered = false;
        this.mAmbientLightValues = new java.util.ArrayDeque<>();
        this.mAlvSum = 0L;
        this.mLastLightColor = 0;
        this.mDelayedUpdateToken = new java.lang.Object();
        this.mElapsedRealTime = -1L;
        this.mColors = context.getResources().getIntArray(android.R.array.config_builtInDisplayIsRoundArray);
        if (com.android.internal.util.ArrayUtils.isEmpty(this.mColors)) {
            this.mHandler = null;
            this.mExecutor = null;
            this.mContext = null;
            this.mAppOpsManager = null;
            this.mLightsManager = null;
            this.mSensorManager = null;
            this.mLightSensor = null;
            this.mMovingAverageIntervalMillis = 0L;
            this.mThresholds = null;
            return;
        }
        this.mContext = context;
        this.mHandler = new android.os.Handler(looper);
        this.mExecutor = new android.os.HandlerExecutor(this.mHandler);
        this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        this.mLightsManager = (android.hardware.lights.LightsManager) this.mContext.getSystemService(android.hardware.lights.LightsManager.class);
        this.mSensorManager = (android.hardware.SensorManager) this.mContext.getSystemService(android.hardware.SensorManager.class);
        this.mMovingAverageIntervalMillis = this.mContext.getResources().getInteger(android.R.integer.config_burnInProtectionMinHorizontalOffset);
        int[] thresholdsLux = this.mContext.getResources().getIntArray(android.R.array.config_brightnessThresholdsOfPeakRefreshRate);
        if (thresholdsLux.length != this.mColors.length - 1) {
            throw new java.lang.IllegalStateException("There must be exactly one more color than thresholds. Found " + this.mColors.length + " colors and " + thresholdsLux.length + " thresholds.");
        }
        this.mThresholds = new long[thresholdsLux.length];
        for (int i = 0; i < thresholdsLux.length; i++) {
            int luxValue = thresholdsLux[i];
            this.mThresholds[i] = (long) (java.lang.Math.log(luxValue) * LIGHT_VALUE_MULTIPLIER);
        }
        java.util.List<android.hardware.lights.Light> lights = this.mLightsManager.getLights();
        for (int i2 = 0; i2 < lights.size(); i2++) {
            android.hardware.lights.Light light = lights.get(i2);
            if (light.getType() == 9) {
                this.mCameraLights.add(light);
            }
        }
        if (this.mCameraLights.isEmpty()) {
            this.mLightSensor = null;
        } else {
            this.mAppOpsManager.startWatchingActive(new java.lang.String[]{"android:camera", "android:phone_call_camera"}, this.mExecutor, this);
            this.mLightSensor = this.mSensorManager.getDefaultSensor(5);
        }
    }

    private void addElement(long time, int value) {
        if (this.mAmbientLightValues.isEmpty()) {
            this.mAmbientLightValues.add(new android.util.Pair<>(java.lang.Long.valueOf((time - getCurrentIntervalMillis()) - 1), java.lang.Integer.valueOf(value)));
        }
        android.util.Pair<java.lang.Long, java.lang.Integer> lastElement = this.mAmbientLightValues.peekLast();
        this.mAmbientLightValues.add(new android.util.Pair<>(java.lang.Long.valueOf(time), java.lang.Integer.valueOf(value)));
        this.mAlvSum += (time - ((java.lang.Long) lastElement.first).longValue()) * ((long) ((java.lang.Integer) lastElement.second).intValue());
        removeObsoleteData(time);
    }

    private void removeObsoleteData(long time) {
        while (this.mAmbientLightValues.size() > 1) {
            android.util.Pair<java.lang.Long, java.lang.Integer> element0 = this.mAmbientLightValues.pollFirst();
            android.util.Pair<java.lang.Long, java.lang.Integer> element1 = this.mAmbientLightValues.peekFirst();
            if (((java.lang.Long) element1.first).longValue() > time - getCurrentIntervalMillis()) {
                this.mAmbientLightValues.addFirst(element0);
                return;
            }
            this.mAlvSum -= (((java.lang.Long) element1.first).longValue() - ((java.lang.Long) element0.first).longValue()) * ((long) ((java.lang.Integer) element0.second).intValue());
        }
    }

    private long getLiveAmbientLightTotal() {
        if (this.mAmbientLightValues.isEmpty()) {
            return this.mAlvSum;
        }
        long time = getElapsedRealTime();
        removeObsoleteData(time);
        android.util.Pair<java.lang.Long, java.lang.Integer> firstElement = this.mAmbientLightValues.peekFirst();
        android.util.Pair<java.lang.Long, java.lang.Integer> lastElement = this.mAmbientLightValues.peekLast();
        return (this.mAlvSum - (java.lang.Math.max(0L, (time - getCurrentIntervalMillis()) - ((java.lang.Long) firstElement.first).longValue()) * ((long) ((java.lang.Integer) firstElement.second).intValue()))) + ((time - ((java.lang.Long) lastElement.first).longValue()) * ((long) ((java.lang.Integer) lastElement.second).intValue()));
    }

    @Override // android.app.AppOpsManager.OnOpActiveChangedListener
    public void onOpActiveChanged(java.lang.String op, int uid, java.lang.String packageName, boolean active) {
        java.util.Set<java.lang.String> activePackages;
        if ("android:camera".equals(op)) {
            activePackages = this.mActivePackages;
        } else if ("android:phone_call_camera".equals(op)) {
            activePackages = this.mActivePhonePackages;
        } else {
            return;
        }
        if (active) {
            activePackages.add(packageName);
        } else {
            activePackages.remove(packageName);
        }
        updateLightSession();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLightSession() {
        if (android.os.Looper.myLooper() != this.mHandler.getLooper()) {
            this.mHandler.post(new com.android.server.sensorprivacy.CameraPrivacyLightController$$ExternalSyntheticLambda0(this));
            return;
        }
        java.util.Set<java.lang.String> exemptedPackages = android.permission.PermissionManager.getIndicatorExemptedPackages(this.mContext);
        boolean shouldSessionEnd = exemptedPackages.containsAll(this.mActivePackages) && exemptedPackages.containsAll(this.mActivePhonePackages);
        updateSensorListener(shouldSessionEnd);
        if (shouldSessionEnd) {
            if (this.mLightsSession == null) {
                return;
            }
            this.mLightsSession.close();
            this.mLightsSession = null;
            return;
        }
        int lightColor = this.mLightSensor == null ? this.mColors[this.mColors.length - 1] : computeCurrentLightColor();
        if (this.mLastLightColor == lightColor && this.mLightsSession != null) {
            return;
        }
        this.mLastLightColor = lightColor;
        android.hardware.lights.LightsRequest.Builder requestBuilder = new android.hardware.lights.LightsRequest.Builder();
        for (int i = 0; i < this.mCameraLights.size(); i++) {
            requestBuilder.addLight(this.mCameraLights.get(i), new android.hardware.lights.LightState.Builder().setColor(lightColor).build());
        }
        if (this.mLightsSession == null) {
            this.mLightsSession = this.mLightsManager.openSession(Integer.MAX_VALUE);
        }
        this.mLightsSession.requestLights(requestBuilder.build());
    }

    private int computeCurrentLightColor() {
        long liveAmbientLightTotal = getLiveAmbientLightTotal();
        long currentInterval = getCurrentIntervalMillis();
        for (int i = 0; i < this.mThresholds.length; i++) {
            if (liveAmbientLightTotal < this.mThresholds[i] * currentInterval) {
                return this.mColors[i];
            }
        }
        return this.mColors[this.mColors.length - 1];
    }

    private void updateSensorListener(boolean shouldSessionEnd) {
        if (shouldSessionEnd && this.mIsAmbientLightListenerRegistered) {
            this.mSensorManager.unregisterListener(this);
            this.mIsAmbientLightListenerRegistered = false;
        }
        if (!shouldSessionEnd && !this.mIsAmbientLightListenerRegistered && this.mLightSensor != null) {
            this.mSensorManager.registerListener(this, this.mLightSensor, 3, this.mHandler);
            this.mIsAmbientLightListenerRegistered = true;
            this.mElapsedTimeStartedReading = getElapsedRealTime();
        }
    }

    private long getElapsedRealTime() {
        return this.mElapsedRealTime == -1 ? android.os.SystemClock.elapsedRealtime() : this.mElapsedRealTime;
    }

    void setElapsedRealTime(long time) {
        this.mElapsedRealTime = time;
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(android.hardware.SensorEvent event) {
        addElement(java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(event.timestamp), java.lang.Math.max(0, (int) (java.lang.Math.log(event.values[0]) * LIGHT_VALUE_MULTIPLIER)));
        updateLightSession();
        this.mHandler.removeCallbacksAndMessages(this.mDelayedUpdateToken);
        this.mHandler.postDelayed(new com.android.server.sensorprivacy.CameraPrivacyLightController$$ExternalSyntheticLambda0(this), this.mDelayedUpdateToken, this.mMovingAverageIntervalMillis);
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    }

    private long getCurrentIntervalMillis() {
        return java.lang.Math.min(this.mMovingAverageIntervalMillis, getElapsedRealTime() - this.mElapsedTimeStartedReading);
    }
}
