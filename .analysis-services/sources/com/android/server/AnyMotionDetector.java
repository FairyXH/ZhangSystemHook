package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class AnyMotionDetector {
    private static final long ACCELEROMETER_DATA_TIMEOUT_MILLIS = 3000;
    private static final boolean DEBUG = false;
    private static final long ORIENTATION_MEASUREMENT_DURATION_MILLIS = 2500;
    private static final long ORIENTATION_MEASUREMENT_INTERVAL_MILLIS = 5000;
    public static final int RESULT_MOVED = 1;
    public static final int RESULT_STATIONARY = 0;
    public static final int RESULT_UNKNOWN = -1;
    private static final int SAMPLING_INTERVAL_MILLIS = 40;
    private static final int STALE_MEASUREMENT_TIMEOUT_MILLIS = 120000;
    private static final int STATE_ACTIVE = 1;
    private static final int STATE_INACTIVE = 0;
    private static final java.lang.String TAG = "AnyMotionDetector";
    private static final long WAKELOCK_TIMEOUT_MILLIS = 30000;
    private final android.hardware.Sensor mAccelSensor;
    private final com.android.server.AnyMotionDetector.DeviceIdleCallback mCallback;
    private final android.os.Handler mHandler;
    private boolean mMeasurementInProgress;
    private boolean mMeasurementTimeoutIsActive;
    private int mNumSufficientSamples;
    private final com.android.server.AnyMotionDetector.RunningSignalStats mRunningStats;
    private final android.hardware.SensorManager mSensorManager;
    private boolean mSensorRestartIsActive;
    private int mState;
    private final float mThresholdAngle;
    private final android.os.PowerManager.WakeLock mWakeLock;
    private volatile boolean mWakelockTimeoutIsActive;
    private final float THRESHOLD_ENERGY = 5.0f;
    private final java.lang.Object mLock = new java.lang.Object();
    private com.android.server.AnyMotionDetector.Vector3 mCurrentGravityVector = null;
    private com.android.server.AnyMotionDetector.Vector3 mPreviousGravityVector = null;
    private final android.hardware.SensorEventListener mListener = new android.hardware.SensorEventListener() { // from class: com.android.server.AnyMotionDetector.1
        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(android.hardware.SensorEvent event) {
            int status = -1;
            synchronized (com.android.server.AnyMotionDetector.this.mLock) {
                com.android.server.AnyMotionDetector.Vector3 accelDatum = new com.android.server.AnyMotionDetector.Vector3(android.os.SystemClock.elapsedRealtime(), event.values[0], event.values[1], event.values[2]);
                com.android.server.AnyMotionDetector.this.mRunningStats.accumulate(accelDatum);
                if (com.android.server.AnyMotionDetector.this.mRunningStats.getSampleCount() >= com.android.server.AnyMotionDetector.this.mNumSufficientSamples) {
                    status = com.android.server.AnyMotionDetector.this.stopOrientationMeasurementLocked();
                }
            }
            if (status != -1) {
                com.android.server.AnyMotionDetector.this.mHandler.removeCallbacks(com.android.server.AnyMotionDetector.this.mWakelockTimeout);
                com.android.server.AnyMotionDetector.this.mWakelockTimeoutIsActive = false;
                com.android.server.AnyMotionDetector.this.mCallback.onAnyMotionResult(status);
            }
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        }
    };
    private final java.lang.Runnable mSensorRestart = new java.lang.Runnable() { // from class: com.android.server.AnyMotionDetector.2
        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.AnyMotionDetector.this.mLock) {
                if (com.android.server.AnyMotionDetector.this.mSensorRestartIsActive) {
                    com.android.server.AnyMotionDetector.this.mSensorRestartIsActive = false;
                    com.android.server.AnyMotionDetector.this.startOrientationMeasurementLocked();
                }
            }
        }
    };
    private final java.lang.Runnable mMeasurementTimeout = new java.lang.Runnable() { // from class: com.android.server.AnyMotionDetector.3
        @Override // java.lang.Runnable
        public void run() {
            int status = -1;
            synchronized (com.android.server.AnyMotionDetector.this.mLock) {
                if (com.android.server.AnyMotionDetector.this.mMeasurementTimeoutIsActive) {
                    com.android.server.AnyMotionDetector.this.mMeasurementTimeoutIsActive = false;
                    status = com.android.server.AnyMotionDetector.this.stopOrientationMeasurementLocked();
                }
            }
            if (status != -1) {
                com.android.server.AnyMotionDetector.this.mHandler.removeCallbacks(com.android.server.AnyMotionDetector.this.mWakelockTimeout);
                com.android.server.AnyMotionDetector.this.mWakelockTimeoutIsActive = false;
                com.android.server.AnyMotionDetector.this.mCallback.onAnyMotionResult(status);
            }
        }
    };
    private final java.lang.Runnable mWakelockTimeout = new java.lang.Runnable() { // from class: com.android.server.AnyMotionDetector.4
        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.AnyMotionDetector.this.mLock) {
                if (com.android.server.AnyMotionDetector.this.mWakelockTimeoutIsActive) {
                    com.android.server.AnyMotionDetector.this.mWakelockTimeoutIsActive = false;
                    com.android.server.AnyMotionDetector.this.stop();
                }
            }
        }
    };

    interface DeviceIdleCallback {
        void onAnyMotionResult(int i);
    }

    public AnyMotionDetector(android.os.PowerManager pm, android.os.Handler handler, android.hardware.SensorManager sm, com.android.server.AnyMotionDetector.DeviceIdleCallback callback, float thresholdAngle) {
        synchronized (this.mLock) {
            this.mWakeLock = pm.newWakeLock(1, TAG);
            this.mWakeLock.setReferenceCounted(false);
            this.mHandler = handler;
            this.mSensorManager = sm;
            this.mAccelSensor = this.mSensorManager.getDefaultSensor(1);
            this.mMeasurementInProgress = false;
            this.mMeasurementTimeoutIsActive = false;
            this.mWakelockTimeoutIsActive = false;
            this.mSensorRestartIsActive = false;
            this.mState = 0;
            this.mCallback = callback;
            this.mThresholdAngle = thresholdAngle;
            this.mRunningStats = new com.android.server.AnyMotionDetector.RunningSignalStats();
            this.mNumSufficientSamples = (int) java.lang.Math.ceil(62.5d);
        }
    }

    public boolean hasSensor() {
        return this.mAccelSensor != null;
    }

    public void checkForAnyMotion() {
        synchronized (this.mLock) {
            if (this.mState != 1) {
                this.mState = 1;
                this.mCurrentGravityVector = null;
                this.mPreviousGravityVector = null;
                this.mWakeLock.acquire();
                android.os.Message wakelockTimeoutMsg = android.os.Message.obtain(this.mHandler, this.mWakelockTimeout);
                this.mHandler.sendMessageDelayed(wakelockTimeoutMsg, 30000L);
                this.mWakelockTimeoutIsActive = true;
                startOrientationMeasurementLocked();
            }
        }
    }

    public void stop() {
        synchronized (this.mLock) {
            if (this.mState == 1) {
                this.mState = 0;
            }
            this.mHandler.removeCallbacks(this.mMeasurementTimeout);
            this.mHandler.removeCallbacks(this.mSensorRestart);
            this.mMeasurementTimeoutIsActive = false;
            this.mSensorRestartIsActive = false;
            if (this.mMeasurementInProgress) {
                this.mMeasurementInProgress = false;
                this.mSensorManager.unregisterListener(this.mListener);
            }
            this.mCurrentGravityVector = null;
            this.mPreviousGravityVector = null;
            if (this.mWakeLock.isHeld()) {
                this.mHandler.removeCallbacks(this.mWakelockTimeout);
                this.mWakelockTimeoutIsActive = false;
                this.mWakeLock.release();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOrientationMeasurementLocked() {
        if (!this.mMeasurementInProgress && this.mAccelSensor != null) {
            if (this.mSensorManager.registerListener(this.mListener, this.mAccelSensor, com.android.server.EventLogTags.VOLUME_CHANGED)) {
                this.mMeasurementInProgress = true;
                this.mRunningStats.reset();
            }
            android.os.Message measurementTimeoutMsg = android.os.Message.obtain(this.mHandler, this.mMeasurementTimeout);
            this.mHandler.sendMessageDelayed(measurementTimeoutMsg, 3000L);
            this.mMeasurementTimeoutIsActive = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int stopOrientationMeasurementLocked() {
        int status = -1;
        if (this.mMeasurementInProgress) {
            this.mHandler.removeCallbacks(this.mMeasurementTimeout);
            this.mMeasurementTimeoutIsActive = false;
            this.mSensorManager.unregisterListener(this.mListener);
            this.mMeasurementInProgress = false;
            this.mPreviousGravityVector = this.mCurrentGravityVector;
            this.mCurrentGravityVector = this.mRunningStats.getRunningAverage();
            if (this.mRunningStats.getSampleCount() == 0) {
                android.util.Slog.w(TAG, "No accelerometer data acquired for orientation measurement.");
            }
            status = getStationaryStatusLocked();
            this.mRunningStats.reset();
            if (status != -1) {
                if (this.mWakeLock.isHeld()) {
                    this.mHandler.removeCallbacks(this.mWakelockTimeout);
                    this.mWakelockTimeoutIsActive = false;
                    this.mWakeLock.release();
                }
                this.mState = 0;
            } else {
                android.os.Message msg = android.os.Message.obtain(this.mHandler, this.mSensorRestart);
                this.mHandler.sendMessageDelayed(msg, ORIENTATION_MEASUREMENT_INTERVAL_MILLIS);
                this.mSensorRestartIsActive = true;
            }
        }
        return status;
    }

    private int getStationaryStatusLocked() {
        if (this.mPreviousGravityVector == null || this.mCurrentGravityVector == null) {
            return -1;
        }
        com.android.server.AnyMotionDetector.Vector3 previousGravityVectorNormalized = this.mPreviousGravityVector.normalized();
        com.android.server.AnyMotionDetector.Vector3 currentGravityVectorNormalized = this.mCurrentGravityVector.normalized();
        float angle = previousGravityVectorNormalized.angleBetween(currentGravityVectorNormalized);
        if (angle < this.mThresholdAngle && this.mRunningStats.getEnergy() < 5.0f) {
            return 0;
        }
        if (java.lang.Float.isNaN(angle)) {
            return 1;
        }
        long diffTime = this.mCurrentGravityVector.timeMillisSinceBoot - this.mPreviousGravityVector.timeMillisSinceBoot;
        return diffTime > 120000 ? -1 : 1;
    }

    public static final class Vector3 {
        public long timeMillisSinceBoot;
        public float x;
        public float y;
        public float z;

        public Vector3(long timeMillisSinceBoot, float x, float y, float z) {
            this.timeMillisSinceBoot = timeMillisSinceBoot;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float norm() {
            return (float) java.lang.Math.sqrt(dotProduct(this));
        }

        public com.android.server.AnyMotionDetector.Vector3 normalized() {
            float mag = norm();
            return new com.android.server.AnyMotionDetector.Vector3(this.timeMillisSinceBoot, this.x / mag, this.y / mag, this.z / mag);
        }

        public float angleBetween(com.android.server.AnyMotionDetector.Vector3 other) {
            com.android.server.AnyMotionDetector.Vector3 crossVector = cross(other);
            float degrees = java.lang.Math.abs((float) java.lang.Math.toDegrees(java.lang.Math.atan2(crossVector.norm(), dotProduct(other))));
            android.util.Slog.d(com.android.server.AnyMotionDetector.TAG, "angleBetween: this = " + toString() + ", other = " + other.toString() + ", degrees = " + degrees);
            return degrees;
        }

        public com.android.server.AnyMotionDetector.Vector3 cross(com.android.server.AnyMotionDetector.Vector3 v) {
            return new com.android.server.AnyMotionDetector.Vector3(v.timeMillisSinceBoot, (this.y * v.z) - (this.z * v.y), (this.z * v.x) - (this.x * v.z), (this.x * v.y) - (this.y * v.x));
        }

        public java.lang.String toString() {
            java.lang.String msg = "timeMillisSinceBoot=" + this.timeMillisSinceBoot;
            return ((msg + " | x=" + this.x) + ", y=" + this.y) + ", z=" + this.z;
        }

        public float dotProduct(com.android.server.AnyMotionDetector.Vector3 v) {
            return (this.x * v.x) + (this.y * v.y) + (this.z * v.z);
        }

        public com.android.server.AnyMotionDetector.Vector3 times(float val) {
            return new com.android.server.AnyMotionDetector.Vector3(this.timeMillisSinceBoot, this.x * val, this.y * val, this.z * val);
        }

        public com.android.server.AnyMotionDetector.Vector3 plus(com.android.server.AnyMotionDetector.Vector3 v) {
            return new com.android.server.AnyMotionDetector.Vector3(v.timeMillisSinceBoot, v.x + this.x, v.y + this.y, v.z + this.z);
        }

        public com.android.server.AnyMotionDetector.Vector3 minus(com.android.server.AnyMotionDetector.Vector3 v) {
            return new com.android.server.AnyMotionDetector.Vector3(v.timeMillisSinceBoot, this.x - v.x, this.y - v.y, this.z - v.z);
        }
    }

    private static class RunningSignalStats {
        com.android.server.AnyMotionDetector.Vector3 currentVector;
        float energy;
        com.android.server.AnyMotionDetector.Vector3 previousVector;
        com.android.server.AnyMotionDetector.Vector3 runningSum;
        int sampleCount;

        public RunningSignalStats() {
            reset();
        }

        public void reset() {
            this.previousVector = null;
            this.currentVector = null;
            this.runningSum = new com.android.server.AnyMotionDetector.Vector3(0L, 0.0f, 0.0f, 0.0f);
            this.energy = 0.0f;
            this.sampleCount = 0;
        }

        public void accumulate(com.android.server.AnyMotionDetector.Vector3 v) {
            if (v == null) {
                return;
            }
            this.sampleCount++;
            this.runningSum = this.runningSum.plus(v);
            this.previousVector = this.currentVector;
            this.currentVector = v;
            if (this.previousVector != null) {
                com.android.server.AnyMotionDetector.Vector3 dv = this.currentVector.minus(this.previousVector);
                float incrementalEnergy = (dv.x * dv.x) + (dv.y * dv.y) + (dv.z * dv.z);
                this.energy += incrementalEnergy;
            }
        }

        public com.android.server.AnyMotionDetector.Vector3 getRunningAverage() {
            if (this.sampleCount > 0) {
                return this.runningSum.times(1.0f / this.sampleCount);
            }
            return null;
        }

        public float getEnergy() {
            return this.energy;
        }

        public int getSampleCount() {
            return this.sampleCount;
        }

        public java.lang.String toString() {
            java.lang.String currentVectorString = this.currentVector == null ? "null" : this.currentVector.toString();
            java.lang.String previousVectorString = this.previousVector != null ? this.previousVector.toString() : "null";
            java.lang.String msg = "previousVector = " + previousVectorString;
            return ((msg + ", currentVector = " + currentVectorString) + ", sampleCount = " + this.sampleCount) + ", energy = " + this.energy;
        }
    }
}
