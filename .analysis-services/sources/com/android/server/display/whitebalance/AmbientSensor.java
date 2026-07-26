package com.android.server.display.whitebalance;

/* JADX INFO: loaded from: classes2.dex */
abstract class AmbientSensor {
    private static final int HISTORY_SIZE = 50;
    private boolean mEnabled;
    private int mEventsCount;
    private com.android.server.display.utils.History mEventsHistory;
    private final android.os.Handler mHandler;
    private android.hardware.SensorEventListener mListener = new android.hardware.SensorEventListener() { // from class: com.android.server.display.whitebalance.AmbientSensor.1
        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(android.hardware.SensorEvent event) {
            float value = event.values[0];
            com.android.server.display.whitebalance.AmbientSensor.this.handleNewEvent(value);
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        }
    };
    protected boolean mLoggingEnabled;
    private int mRate;
    protected android.hardware.Sensor mSensor;
    protected final android.hardware.SensorManager mSensorManager;
    protected java.lang.String mTag;

    protected abstract void update(float f);

    AmbientSensor(java.lang.String tag, android.os.Handler handler, android.hardware.SensorManager sensorManager, int rate) {
        validateArguments(handler, sensorManager, rate);
        this.mTag = tag;
        this.mLoggingEnabled = false;
        this.mHandler = handler;
        this.mSensorManager = sensorManager;
        this.mEnabled = false;
        this.mRate = rate;
        this.mEventsCount = 0;
        this.mEventsHistory = new com.android.server.display.utils.History(50);
    }

    public boolean setEnabled(boolean enabled) {
        if (enabled) {
            return enable();
        }
        return disable();
    }

    public boolean setLoggingEnabled(boolean loggingEnabled) {
        if (this.mLoggingEnabled == loggingEnabled) {
            return false;
        }
        this.mLoggingEnabled = loggingEnabled;
        return true;
    }

    public void dump(java.io.PrintWriter writer) {
        writer.println("  " + this.mTag);
        writer.println("    mLoggingEnabled=" + this.mLoggingEnabled);
        writer.println("    mHandler=" + this.mHandler);
        writer.println("    mSensorManager=" + this.mSensorManager);
        writer.println("    mSensor=" + this.mSensor);
        writer.println("    mEnabled=" + this.mEnabled);
        writer.println("    mRate=" + this.mRate);
        writer.println("    mEventsCount=" + this.mEventsCount);
        writer.println("    mEventsHistory=" + this.mEventsHistory);
    }

    private static void validateArguments(android.os.Handler handler, android.hardware.SensorManager sensorManager, int rate) {
        java.util.Objects.requireNonNull(handler, "handler cannot be null");
        java.util.Objects.requireNonNull(sensorManager, "sensorManager cannot be null");
        if (rate <= 0) {
            throw new java.lang.IllegalArgumentException("rate must be positive");
        }
    }

    private boolean enable() {
        if (this.mEnabled) {
            return false;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(this.mTag, "enabling");
        }
        this.mEnabled = true;
        startListening();
        return true;
    }

    private boolean disable() {
        if (!this.mEnabled) {
            return false;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(this.mTag, "disabling");
        }
        this.mEnabled = false;
        this.mEventsCount = 0;
        stopListening();
        return true;
    }

    private void startListening() {
        if (this.mSensorManager == null) {
            return;
        }
        this.mSensorManager.registerListener(this.mListener, this.mSensor, this.mRate * 1000, this.mHandler);
    }

    private void stopListening() {
        if (this.mSensorManager == null) {
            return;
        }
        this.mSensorManager.unregisterListener(this.mListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleNewEvent(float value) {
        if (!this.mEnabled) {
            return;
        }
        if (this.mLoggingEnabled) {
            android.util.Slog.d(this.mTag, "handle new event: " + value);
        }
        this.mEventsCount++;
        this.mEventsHistory.add(value);
        update(value);
    }

    static class AmbientBrightnessSensor extends com.android.server.display.whitebalance.AmbientSensor {
        private static final java.lang.String TAG = "AmbientBrightnessSensor";
        private com.android.server.display.whitebalance.AmbientSensor.AmbientBrightnessSensor.Callbacks mCallbacks;

        interface Callbacks {
            void onAmbientBrightnessChanged(float f);
        }

        AmbientBrightnessSensor(android.os.Handler handler, android.hardware.SensorManager sensorManager, int rate) {
            super(TAG, handler, sensorManager, rate);
            this.mSensor = this.mSensorManager.getDefaultSensor(5);
            if (this.mSensor == null) {
                throw new java.lang.IllegalStateException("cannot find light sensor");
            }
            this.mCallbacks = null;
        }

        public boolean setCallbacks(com.android.server.display.whitebalance.AmbientSensor.AmbientBrightnessSensor.Callbacks callbacks) {
            if (this.mCallbacks == callbacks) {
                return false;
            }
            this.mCallbacks = callbacks;
            return true;
        }

        @Override // com.android.server.display.whitebalance.AmbientSensor
        public void dump(java.io.PrintWriter writer) {
            super.dump(writer);
            writer.println("    mCallbacks=" + this.mCallbacks);
        }

        @Override // com.android.server.display.whitebalance.AmbientSensor
        protected void update(float value) {
            if (this.mCallbacks != null) {
                this.mCallbacks.onAmbientBrightnessChanged(value);
            }
        }
    }

    static class AmbientColorTemperatureSensor extends com.android.server.display.whitebalance.AmbientSensor {
        private static final java.lang.String TAG = "AmbientColorTemperatureSensor";
        private com.android.server.display.whitebalance.AmbientSensor.AmbientColorTemperatureSensor.Callbacks mCallbacks;

        interface Callbacks {
            void onAmbientColorTemperatureChanged(float f);
        }

        AmbientColorTemperatureSensor(android.os.Handler handler, android.hardware.SensorManager sensorManager, java.lang.String name, int rate) {
            super(TAG, handler, sensorManager, rate);
            this.mSensor = null;
            java.util.Iterator<android.hardware.Sensor> it = this.mSensorManager.getSensorList(-1).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                android.hardware.Sensor sensor = it.next();
                if (sensor.getStringType().equals(name)) {
                    this.mSensor = sensor;
                    break;
                }
            }
            if (this.mSensor == null) {
                throw new java.lang.IllegalStateException("cannot find sensor " + name);
            }
            this.mCallbacks = null;
        }

        public boolean setCallbacks(com.android.server.display.whitebalance.AmbientSensor.AmbientColorTemperatureSensor.Callbacks callbacks) {
            if (this.mCallbacks == callbacks) {
                return false;
            }
            this.mCallbacks = callbacks;
            return true;
        }

        @Override // com.android.server.display.whitebalance.AmbientSensor
        public void dump(java.io.PrintWriter writer) {
            super.dump(writer);
            writer.println("    mCallbacks=" + this.mCallbacks);
        }

        @Override // com.android.server.display.whitebalance.AmbientSensor
        protected void update(float value) {
            if (this.mCallbacks != null) {
                this.mCallbacks.onAmbientColorTemperatureChanged(value);
            }
        }
    }
}
