package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class WirelessChargerDetector {
    private static final boolean DEBUG = false;
    private static final double MAX_GRAVITY = 10.806650161743164d;
    private static final double MIN_GRAVITY = 8.806650161743164d;
    private static final int MIN_SAMPLES = 3;
    private static final double MOVEMENT_ANGLE_COS_THRESHOLD = java.lang.Math.cos(0.08726646259971647d);
    private static final int SAMPLING_INTERVAL_MILLIS = 50;
    private static final long SETTLE_TIME_MILLIS = 800;
    private static final java.lang.String TAG = "WirelessChargerDetector";
    private boolean mAtRest;
    private boolean mDetectionInProgress;
    private long mDetectionStartTime;
    private float mFirstSampleX;
    private float mFirstSampleY;
    private float mFirstSampleZ;
    private android.hardware.Sensor mGravitySensor;
    private final android.os.Handler mHandler;
    private float mLastSampleX;
    private float mLastSampleY;
    private float mLastSampleZ;
    private int mMovingSamples;
    private boolean mMustUpdateRestPosition;
    private boolean mPoweredWirelessly;
    private float mRestX;
    private float mRestY;
    private float mRestZ;
    private final android.hardware.SensorManager mSensorManager;
    private final com.android.server.power.SuspendBlocker mSuspendBlocker;
    private int mTotalSamples;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.hardware.SensorEventListener mListener = new android.hardware.SensorEventListener() { // from class: com.android.server.power.WirelessChargerDetector.1
        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(android.hardware.SensorEvent event) {
            synchronized (com.android.server.power.WirelessChargerDetector.this.mLock) {
                com.android.server.power.WirelessChargerDetector.this.processSampleLocked(event.values[0], event.values[1], event.values[2]);
            }
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        }
    };
    private final java.lang.Runnable mSensorTimeout = new java.lang.Runnable() { // from class: com.android.server.power.WirelessChargerDetector.2
        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.power.WirelessChargerDetector.this.mLock) {
                com.android.server.power.WirelessChargerDetector.this.finishDetectionLocked();
            }
        }
    };

    public WirelessChargerDetector(android.hardware.SensorManager sensorManager, com.android.server.power.SuspendBlocker suspendBlocker, android.os.Handler handler) {
        this.mSensorManager = sensorManager;
        this.mSuspendBlocker = suspendBlocker;
        this.mHandler = handler;
        this.mGravitySensor = sensorManager.getDefaultSensor(9);
    }

    public void dump(java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.println();
            pw.println("Wireless Charger Detector State:");
            pw.println("  mGravitySensor=" + this.mGravitySensor);
            pw.println("  mPoweredWirelessly=" + this.mPoweredWirelessly);
            pw.println("  mAtRest=" + this.mAtRest);
            pw.println("  mRestX=" + this.mRestX + ", mRestY=" + this.mRestY + ", mRestZ=" + this.mRestZ);
            pw.println("  mDetectionInProgress=" + this.mDetectionInProgress);
            pw.println("  mDetectionStartTime=" + (this.mDetectionStartTime == 0 ? "0 (never)" : android.util.TimeUtils.formatUptime(this.mDetectionStartTime)));
            pw.println("  mMustUpdateRestPosition=" + this.mMustUpdateRestPosition);
            pw.println("  mTotalSamples=" + this.mTotalSamples);
            pw.println("  mMovingSamples=" + this.mMovingSamples);
            pw.println("  mFirstSampleX=" + this.mFirstSampleX + ", mFirstSampleY=" + this.mFirstSampleY + ", mFirstSampleZ=" + this.mFirstSampleZ);
            pw.println("  mLastSampleX=" + this.mLastSampleX + ", mLastSampleY=" + this.mLastSampleY + ", mLastSampleZ=" + this.mLastSampleZ);
        }
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long wcdToken = proto.start(fieldId);
        synchronized (this.mLock) {
            proto.write(1133871366145L, this.mPoweredWirelessly);
            proto.write(1133871366146L, this.mAtRest);
            long restVectorToken = proto.start(1146756268035L);
            proto.write(1108101562369L, this.mRestX);
            proto.write(1108101562370L, this.mRestY);
            proto.write(1108101562371L, this.mRestZ);
            proto.end(restVectorToken);
            proto.write(1133871366148L, this.mDetectionInProgress);
            proto.write(1112396529669L, this.mDetectionStartTime);
            proto.write(1133871366150L, this.mMustUpdateRestPosition);
            proto.write(1120986464263L, this.mTotalSamples);
            proto.write(1120986464264L, this.mMovingSamples);
            long firstSampleVectorToken = proto.start(1146756268041L);
            proto.write(1108101562369L, this.mFirstSampleX);
            proto.write(1108101562370L, this.mFirstSampleY);
            proto.write(1108101562371L, this.mFirstSampleZ);
            proto.end(firstSampleVectorToken);
            long lastSampleVectorToken = proto.start(1146756268042L);
            proto.write(1108101562369L, this.mLastSampleX);
            proto.write(1108101562370L, this.mLastSampleY);
            proto.write(1108101562371L, this.mLastSampleZ);
            proto.end(lastSampleVectorToken);
        }
        proto.end(wcdToken);
    }

    public boolean update(boolean isPowered, int plugType) {
        boolean z;
        synchronized (this.mLock) {
            boolean wasPoweredWirelessly = this.mPoweredWirelessly;
            z = true;
            if (isPowered && plugType == 4) {
                this.mPoweredWirelessly = true;
                this.mMustUpdateRestPosition = true;
                startDetectionLocked();
            } else {
                this.mPoweredWirelessly = false;
                if (this.mAtRest) {
                    if (plugType != 0 && plugType != 4) {
                        this.mMustUpdateRestPosition = false;
                        clearAtRestLocked();
                    } else {
                        startDetectionLocked();
                    }
                }
            }
            if (!this.mPoweredWirelessly || wasPoweredWirelessly || this.mAtRest) {
                z = false;
            }
        }
        return z;
    }

    private void startDetectionLocked() {
        if (!this.mDetectionInProgress && this.mGravitySensor != null && this.mSensorManager.registerListener(this.mListener, this.mGravitySensor, com.android.server.usage.IntervalStats.MAX_EVENTS_LIGHT)) {
            this.mSuspendBlocker.acquire();
            this.mDetectionInProgress = true;
            this.mDetectionStartTime = android.os.SystemClock.uptimeMillis();
            this.mTotalSamples = 0;
            this.mMovingSamples = 0;
            android.os.Message msg = android.os.Message.obtain(this.mHandler, this.mSensorTimeout);
            msg.setAsynchronous(true);
            this.mHandler.sendMessageDelayed(msg, SETTLE_TIME_MILLIS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishDetectionLocked() {
        if (this.mDetectionInProgress) {
            this.mSensorManager.unregisterListener(this.mListener);
            this.mHandler.removeCallbacks(this.mSensorTimeout);
            if (this.mMustUpdateRestPosition) {
                clearAtRestLocked();
                if (this.mTotalSamples < 3) {
                    android.util.Slog.w(TAG, "Wireless charger detector is broken.  Only received " + this.mTotalSamples + " samples from the gravity sensor but we need at least 3 and we expect to see about 16 on average.");
                } else if (this.mMovingSamples == 0) {
                    this.mAtRest = true;
                    this.mRestX = this.mLastSampleX;
                    this.mRestY = this.mLastSampleY;
                    this.mRestZ = this.mLastSampleZ;
                }
                this.mMustUpdateRestPosition = false;
            }
            this.mDetectionInProgress = false;
            this.mSuspendBlocker.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processSampleLocked(float x, float y, float z) {
        if (this.mDetectionInProgress) {
            this.mLastSampleX = x;
            this.mLastSampleY = y;
            this.mLastSampleZ = z;
            this.mTotalSamples++;
            if (this.mTotalSamples == 1) {
                this.mFirstSampleX = x;
                this.mFirstSampleY = y;
                this.mFirstSampleZ = z;
            } else if (hasMoved(this.mFirstSampleX, this.mFirstSampleY, this.mFirstSampleZ, x, y, z)) {
                this.mMovingSamples++;
            }
            if (this.mAtRest && hasMoved(this.mRestX, this.mRestY, this.mRestZ, x, y, z)) {
                clearAtRestLocked();
            }
        }
    }

    private void clearAtRestLocked() {
        this.mAtRest = false;
        this.mRestX = 0.0f;
        this.mRestY = 0.0f;
        this.mRestZ = 0.0f;
    }

    private static boolean hasMoved(float x1, float y1, float z1, float x2, float y2, float z2) {
        double dotProduct = (x1 * x2) + (y1 * y2) + (z1 * z2);
        double mag1 = java.lang.Math.sqrt((x1 * x1) + (y1 * y1) + (z1 * z1));
        double mag2 = java.lang.Math.sqrt((x2 * x2) + (y2 * y2) + (z2 * z2));
        if (mag1 < MIN_GRAVITY || mag1 > MAX_GRAVITY || mag2 < MIN_GRAVITY || mag2 > MAX_GRAVITY) {
            return true;
        }
        boolean moved = dotProduct < (mag1 * mag2) * MOVEMENT_ANGLE_COS_THRESHOLD;
        return moved;
    }
}
