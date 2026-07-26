package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class ScreenOffBrightnessSensorController implements android.hardware.SensorEventListener {
    private static final int SENSOR_INVALID_VALUE = -1;
    private static final long SENSOR_VALUE_VALID_TIME_MILLIS = 1500;
    private static final java.lang.String TAG = "ScreenOffBrightnessSensorController";
    private final com.android.server.display.BrightnessMappingStrategy mBrightnessMapper;
    private final com.android.server.display.ScreenOffBrightnessSensorController.Clock mClock;
    private final android.os.Handler mHandler;
    private final android.hardware.Sensor mLightSensor;
    private boolean mRegistered;
    private final android.hardware.SensorManager mSensorManager;
    private final int[] mSensorValueToLux;
    private int mLastSensorValue = -1;
    private long mSensorDisableTime = -1;

    public interface Clock {
        long uptimeMillis();
    }

    public ScreenOffBrightnessSensorController(android.hardware.SensorManager sensorManager, android.hardware.Sensor lightSensor, android.os.Handler handler, com.android.server.display.ScreenOffBrightnessSensorController.Clock clock, int[] sensorValueToLux, com.android.server.display.BrightnessMappingStrategy brightnessMapper) {
        this.mSensorManager = sensorManager;
        this.mLightSensor = lightSensor;
        this.mHandler = handler;
        this.mClock = clock;
        this.mSensorValueToLux = sensorValueToLux;
        this.mBrightnessMapper = brightnessMapper;
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(android.hardware.SensorEvent event) {
        if (this.mRegistered) {
            this.mLastSensorValue = (int) event.values[0];
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
    }

    public void setLightSensorEnabled(boolean enabled) {
        if (enabled && !this.mRegistered) {
            this.mRegistered = this.mSensorManager.registerListener(this, this.mLightSensor, 3, this.mHandler);
            this.mLastSensorValue = -1;
        } else if (!enabled && this.mRegistered) {
            this.mSensorManager.unregisterListener(this);
            this.mRegistered = false;
            this.mSensorDisableTime = this.mClock.uptimeMillis();
        }
    }

    public void stop() {
        setLightSensorEnabled(false);
    }

    public float getAutomaticScreenBrightness() {
        int lux;
        if (this.mLastSensorValue < 0 || this.mLastSensorValue >= this.mSensorValueToLux.length || ((!this.mRegistered && this.mClock.uptimeMillis() - this.mSensorDisableTime > SENSOR_VALUE_VALID_TIME_MILLIS) || (lux = this.mSensorValueToLux[this.mLastSensorValue]) < 0)) {
            return Float.NaN;
        }
        return this.mBrightnessMapper.getBrightness(lux);
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("Screen Off Brightness Sensor Controller:");
        android.util.IndentingPrintWriter idpw = new android.util.IndentingPrintWriter(pw);
        idpw.increaseIndent();
        idpw.println("registered=" + this.mRegistered);
        idpw.println("lastSensorValue=" + this.mLastSensorValue);
    }
}
