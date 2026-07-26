package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
final class AmbientKeyboardBacklightController implements android.hardware.display.DisplayManager.DisplayListener, android.hardware.SensorEventListener {
    public static final int HYSTERESIS_THRESHOLD = 2;
    private static final int MSG_BRIGHTNESS_CALLBACK = 0;
    private static final int MSG_SETUP_DISPLAY_AND_SENSOR = 1;
    private com.android.server.input.AmbientKeyboardBacklightController.BrightnessStep[] mBrightnessSteps;
    private final android.content.Context mContext;
    private int mCurrentBrightnessStepIndex;
    private java.lang.String mCurrentDefaultDisplayUniqueId;
    private final android.os.Handler mHandler;
    private com.android.server.input.AmbientKeyboardBacklightController.HysteresisState mHysteresisState;
    private android.hardware.Sensor mLightSensor;
    private int mSmoothedLux;
    private int mSmoothedLuxAtLastAdjustment;
    private float mSmoothingConstant;
    private static final java.lang.String TAG = "KbdBacklightController";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final java.lang.Object sAmbientControllerLock = new java.lang.Object();
    private final java.util.List<com.android.server.input.AmbientKeyboardBacklightController.AmbientKeyboardBacklightListener> mAmbientKeyboardBacklightListeners = new java.util.ArrayList();
    private int mHysteresisCount = 0;

    public interface AmbientKeyboardBacklightListener {
        void onKeyboardBacklightValueChanged(int i);
    }

    private enum HysteresisState {
        STABLE,
        DECREASING,
        INCREASING,
        IMMEDIATE
    }

    AmbientKeyboardBacklightController(android.content.Context context, android.os.Looper looper) {
        this.mContext = context;
        this.mHandler = new android.os.Handler(looper, new android.os.Handler.Callback() { // from class: com.android.server.input.AmbientKeyboardBacklightController$$ExternalSyntheticLambda0
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(android.os.Message message) {
                return this.f$0.handleMessage(message);
            }
        });
        initConfiguration();
    }

    public void systemRunning() {
        this.mHandler.sendEmptyMessage(1);
        android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) java.util.Objects.requireNonNull((android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class));
        displayManager.registerDisplayListener(this, this.mHandler);
    }

    public void registerAmbientBacklightListener(com.android.server.input.AmbientKeyboardBacklightController.AmbientKeyboardBacklightListener listener) {
        synchronized (sAmbientControllerLock) {
            if (this.mAmbientKeyboardBacklightListeners.contains(listener)) {
                throw new java.lang.IllegalStateException("AmbientKeyboardBacklightListener was already registered, listener = " + listener);
            }
            if (this.mAmbientKeyboardBacklightListeners.isEmpty()) {
                addSensorListener(this.mLightSensor);
            }
            this.mAmbientKeyboardBacklightListeners.add(listener);
        }
    }

    public void unregisterAmbientBacklightListener(com.android.server.input.AmbientKeyboardBacklightController.AmbientKeyboardBacklightListener listener) {
        synchronized (sAmbientControllerLock) {
            if (!this.mAmbientKeyboardBacklightListeners.contains(listener)) {
                throw new java.lang.IllegalStateException("AmbientKeyboardBacklightListener was never registered, listener = " + listener);
            }
            this.mAmbientKeyboardBacklightListeners.remove(listener);
            if (this.mAmbientKeyboardBacklightListeners.isEmpty()) {
                removeSensorListener(this.mLightSensor);
            }
        }
    }

    private void sendBrightnessAdjustment(int brightnessValue) {
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 0, java.lang.Integer.valueOf(brightnessValue));
        this.mHandler.sendMessage(msg);
    }

    private void handleBrightnessCallback(int brightnessValue) {
        synchronized (sAmbientControllerLock) {
            for (com.android.server.input.AmbientKeyboardBacklightController.AmbientKeyboardBacklightListener listener : this.mAmbientKeyboardBacklightListeners) {
                listener.onKeyboardBacklightValueChanged(brightnessValue);
            }
        }
    }

    private void handleAmbientLuxChange(float rawLux) {
        if (rawLux < 0.0f) {
            android.util.Slog.w(TAG, "Light sensor doesn't have valid value");
            return;
        }
        updateSmoothedLux(rawLux);
        if (this.mHysteresisState != com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.IMMEDIATE && this.mSmoothedLux == this.mSmoothedLuxAtLastAdjustment) {
            this.mHysteresisState = com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.STABLE;
            return;
        }
        int newStepIndex = java.lang.Math.max(0, this.mCurrentBrightnessStepIndex);
        int numSteps = this.mBrightnessSteps.length;
        if (this.mSmoothedLux > this.mSmoothedLuxAtLastAdjustment) {
            if (this.mHysteresisState != com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.IMMEDIATE && this.mHysteresisState != com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.INCREASING) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "ALS transitioned to brightness increasing state");
                }
                this.mHysteresisState = com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.INCREASING;
                this.mHysteresisCount = 0;
            }
            while (newStepIndex < numSteps && this.mSmoothedLux >= this.mBrightnessSteps[newStepIndex].mIncreaseLuxThreshold) {
                newStepIndex++;
            }
        } else if (this.mSmoothedLux < this.mSmoothedLuxAtLastAdjustment) {
            if (this.mHysteresisState != com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.IMMEDIATE && this.mHysteresisState != com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.DECREASING) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "ALS transitioned to brightness decreasing state");
                }
                this.mHysteresisState = com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.DECREASING;
                this.mHysteresisCount = 0;
            }
            while (newStepIndex >= 0 && this.mSmoothedLux <= this.mBrightnessSteps[newStepIndex].mDecreaseLuxThreshold) {
                newStepIndex--;
            }
        }
        if (this.mHysteresisState == com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.IMMEDIATE) {
            this.mCurrentBrightnessStepIndex = newStepIndex;
            this.mSmoothedLuxAtLastAdjustment = this.mSmoothedLux;
            this.mHysteresisState = com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.STABLE;
            this.mHysteresisCount = 0;
            sendBrightnessAdjustment(this.mBrightnessSteps[newStepIndex].mBrightnessValue);
            return;
        }
        if (newStepIndex == this.mCurrentBrightnessStepIndex) {
            return;
        }
        this.mHysteresisCount++;
        if (DEBUG) {
            android.util.Slog.d(TAG, "Incremented hysteresis count to " + this.mHysteresisCount + " (lux went from " + this.mSmoothedLuxAtLastAdjustment + " to " + this.mSmoothedLux + ")");
        }
        if (this.mHysteresisCount >= 2) {
            this.mCurrentBrightnessStepIndex = newStepIndex;
            this.mSmoothedLuxAtLastAdjustment = this.mSmoothedLux;
            this.mHysteresisCount = 1;
            sendBrightnessAdjustment(this.mBrightnessSteps[newStepIndex].mBrightnessValue);
        }
    }

    private void handleDisplayChange() {
        android.hardware.display.DisplayManagerInternal displayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        android.view.DisplayInfo displayInfo = displayManagerInternal.getDisplayInfo(0);
        if (displayInfo == null) {
            return;
        }
        synchronized (sAmbientControllerLock) {
            if (java.util.Objects.equals(this.mCurrentDefaultDisplayUniqueId, displayInfo.uniqueId)) {
                return;
            }
            if (DEBUG) {
                android.util.Slog.d(TAG, "Default display changed: resetting the light sensor");
            }
            this.mCurrentDefaultDisplayUniqueId = displayInfo.uniqueId;
            if (!this.mAmbientKeyboardBacklightListeners.isEmpty()) {
                removeSensorListener(this.mLightSensor);
            }
            this.mLightSensor = getAmbientLightSensor(displayManagerInternal.getAmbientLightSensorData(0));
            if (!this.mAmbientKeyboardBacklightListeners.isEmpty()) {
                addSensorListener(this.mLightSensor);
            }
        }
    }

    private android.hardware.Sensor getAmbientLightSensor(android.hardware.display.DisplayManagerInternal.AmbientLightSensorData ambientSensor) {
        android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) java.util.Objects.requireNonNull((android.hardware.SensorManager) this.mContext.getSystemService(android.hardware.SensorManager.class));
        if (DEBUG) {
            android.util.Slog.d(TAG, "Ambient Light sensor data: " + ambientSensor);
        }
        return com.android.server.display.utils.SensorUtils.findSensor(sensorManager, ambientSensor.sensorType, ambientSensor.sensorName, 5);
    }

    private void updateSmoothedLux(float rawLux) {
        if (this.mHysteresisState == com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.IMMEDIATE) {
            this.mSmoothedLux = (int) rawLux;
        } else {
            this.mSmoothedLux = (int) ((this.mSmoothingConstant * rawLux) + ((1.0f - this.mSmoothingConstant) * this.mSmoothedLux));
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Current smoothed lux from ALS = " + this.mSmoothedLux);
        }
    }

    public void addSensorListener(android.hardware.Sensor sensor) {
        android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) this.mContext.getSystemService(android.hardware.SensorManager.class);
        if (sensorManager == null || sensor == null) {
            return;
        }
        reset();
        sensorManager.registerListener(this, sensor, 3, this.mHandler);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Registering ALS listener");
        }
    }

    private void removeSensorListener(android.hardware.Sensor sensor) {
        android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) this.mContext.getSystemService(android.hardware.SensorManager.class);
        if (sensorManager == null || sensor == null) {
            return;
        }
        sensorManager.unregisterListener(this, sensor);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Unregistering ALS listener");
        }
    }

    private void initConfiguration() {
        android.content.res.Resources res = this.mContext.getResources();
        int[] brightnessValueArray = res.getIntArray(android.R.array.config_autoBrightnessLevelsIdle);
        int[] decreaseThresholdArray = res.getIntArray(android.R.array.config_autoKeyboardBacklightBrightnessValues);
        int[] increaseThresholdArray = res.getIntArray(android.R.array.config_autoKeyboardBacklightDecreaseLuxThreshold);
        if (brightnessValueArray.length != decreaseThresholdArray.length || decreaseThresholdArray.length != increaseThresholdArray.length) {
            throw new java.lang.IllegalArgumentException("The config files for auto keyboard backlight brightness must contain arrays of equal lengths");
        }
        int size = brightnessValueArray.length;
        this.mBrightnessSteps = new com.android.server.input.AmbientKeyboardBacklightController.BrightnessStep[size];
        int i = 0;
        while (true) {
            int decreaseThreshold = Integer.MIN_VALUE;
            if (i >= size) {
                break;
            }
            int increaseThreshold = increaseThresholdArray[i] >= 0 ? increaseThresholdArray[i] : Integer.MAX_VALUE;
            if (decreaseThresholdArray[i] >= 0) {
                decreaseThreshold = decreaseThresholdArray[i];
            }
            this.mBrightnessSteps[i] = new com.android.server.input.AmbientKeyboardBacklightController.BrightnessStep(brightnessValueArray[i], increaseThreshold, decreaseThreshold);
            i++;
        }
        int numSteps = this.mBrightnessSteps.length;
        if (numSteps == 0 || this.mBrightnessSteps[0].mDecreaseLuxThreshold != Integer.MIN_VALUE || this.mBrightnessSteps[numSteps - 1].mIncreaseLuxThreshold != Integer.MAX_VALUE) {
            throw new java.lang.IllegalArgumentException("The config files for auto keyboard backlight brightness must contain arrays of length > 0 and have -1 or Integer.MIN_VALUE as lower bound for decrease thresholds and -1 or Integer.MAX_VALUE as upper bound for increase thresholds");
        }
        android.util.TypedValue smoothingConstantValue = new android.util.TypedValue();
        res.getValue(android.R.dimen.chooser_action_button_icon_size, smoothingConstantValue, true);
        this.mSmoothingConstant = smoothingConstantValue.getFloat();
        if (this.mSmoothingConstant <= 0.0d || this.mSmoothingConstant > 1.0d) {
            throw new java.lang.IllegalArgumentException("The config files for auto keyboard backlight brightness must contain smoothing constant in range (0.0, 1.0].");
        }
        if (DEBUG) {
            android.util.Log.d(TAG, "Brightness steps: " + java.util.Arrays.toString(this.mBrightnessSteps) + " Smoothing constant = " + this.mSmoothingConstant);
        }
    }

    private void reset() {
        this.mHysteresisState = com.android.server.input.AmbientKeyboardBacklightController.HysteresisState.IMMEDIATE;
        this.mSmoothedLux = 0;
        this.mSmoothedLuxAtLastAdjustment = 0;
        this.mCurrentBrightnessStepIndex = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case 0:
                handleBrightnessCallback(((java.lang.Integer) msg.obj).intValue());
                break;
            case 1:
                handleDisplayChange();
                break;
        }
        return true;
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(android.hardware.SensorEvent event) {
        handleAmbientLuxChange(event.values[0]);
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayAdded(int displayId) {
        handleDisplayChange();
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayRemoved(int displayId) {
        handleDisplayChange();
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayChanged(int displayId) {
        handleDisplayChange();
    }

    private static class BrightnessStep {
        private final int mBrightnessValue;
        private final int mDecreaseLuxThreshold;
        private final int mIncreaseLuxThreshold;

        private BrightnessStep(int brightnessValue, int increaseLuxThreshold, int decreaseLuxThreshold) {
            this.mBrightnessValue = brightnessValue;
            this.mIncreaseLuxThreshold = increaseLuxThreshold;
            this.mDecreaseLuxThreshold = decreaseLuxThreshold;
        }

        public java.lang.String toString() {
            return "BrightnessStep{mBrightnessValue=" + this.mBrightnessValue + ", mIncreaseThreshold=" + this.mIncreaseLuxThreshold + ", mDecreaseThreshold=" + this.mDecreaseLuxThreshold + '}';
        }
    }
}
