package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public abstract class WindowOrientationListener {
    private static final int DEFAULT_BATCH_LATENCY = 100000;
    private static final long DEFAULT_ROTATION_MEMORIZATION_TIMEOUT_MILLIS = 3000;
    private static final long DEFAULT_ROTATION_RESOLVER_TIMEOUT_MILLIS = 700;
    private static final java.lang.String KEY_ROTATION_MEMORIZATION_TIMEOUT = "rotation_memorization_timeout_millis";
    private static final java.lang.String KEY_ROTATION_RESOLVER_TIMEOUT = "rotation_resolver_timeout_millis";
    private static final boolean LOG = android.os.SystemProperties.getBoolean("debug.orientation.log", false);
    private static final java.lang.String TAG = "WindowOrientationListener";
    private static final int TYPE_UI_DEVICE_ORIENTATION = 268369948;
    private static final boolean USE_GRAVITY_SENSOR = false;
    private final android.content.Context mContext;
    private int mCurrentRotation;
    private final int mDefaultRotation;
    private boolean mEnabled;
    private android.os.Handler mHandler;
    private final java.lang.Object mLock;
    com.android.server.wm.WindowOrientationListener.OrientationJudge mOrientationJudge;
    private int mRate;
    android.rotationresolver.RotationResolverInternal mRotationResolverService;
    private android.hardware.Sensor mSensor;
    private android.hardware.SensorManager mSensorManager;
    private java.lang.String mSensorType;
    com.android.server.wm.IWindowOrientationListenerExt mWindowOrientationListenerExt;

    public abstract boolean isKeyguardShowingAndNotOccluded();

    abstract boolean isRotationResolverEnabled();

    public abstract void onProposedRotationChanged(int i);

    public WindowOrientationListener(android.content.Context context, android.os.Handler handler, int defaultRotation) {
        this(context, handler, defaultRotation, 2);
    }

    private WindowOrientationListener(android.content.Context context, android.os.Handler handler, int defaultRotation, int rate) {
        this.mCurrentRotation = -1;
        this.mLock = new java.lang.Object();
        this.mWindowOrientationListenerExt = (com.android.server.wm.IWindowOrientationListenerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IWindowOrientationListenerExt.class).base(this).create();
        this.mContext = context;
        this.mHandler = handler;
        this.mDefaultRotation = defaultRotation;
        this.mSensorManager = (android.hardware.SensorManager) context.getSystemService(com.android.server.am.IOplusSceneManager.APP_SCENE_SENSOR);
        this.mRate = rate;
        java.util.List<android.hardware.Sensor> l = this.mWindowOrientationListenerExt.isFlipDevice() ? this.mSensorManager.getSensorList(TYPE_UI_DEVICE_ORIENTATION) : null;
        android.hardware.Sensor wakeUpDeviceOrientationSensor = null;
        android.hardware.Sensor nonWakeUpDeviceOrientationSensor = null;
        for (android.hardware.Sensor s : (l == null || l.isEmpty()) ? this.mSensorManager.getSensorList(27) : l) {
            if (s.isWakeUpSensor()) {
                wakeUpDeviceOrientationSensor = s;
            } else {
                nonWakeUpDeviceOrientationSensor = s;
            }
        }
        if (wakeUpDeviceOrientationSensor != null) {
            this.mSensor = wakeUpDeviceOrientationSensor;
        } else {
            this.mSensor = nonWakeUpDeviceOrientationSensor;
        }
        if (this.mSensor != null) {
            this.mOrientationJudge = new com.android.server.wm.WindowOrientationListener.OrientationSensorJudge();
        }
        if (this.mOrientationJudge == null) {
            this.mSensor = this.mSensorManager.getDefaultSensor(1);
            if (this.mSensor != null) {
                this.mOrientationJudge = new com.android.server.wm.WindowOrientationListener.AccelSensorJudge(context);
            }
        }
    }

    public void enable() {
        enable(true);
    }

    public void enable(boolean clearCurrentRotation) {
        synchronized (this.mLock) {
            if (this.mSensor == null) {
                android.util.Slog.w(TAG, "Cannot detect sensors. Not enabled");
                return;
            }
            if (this.mEnabled) {
                return;
            }
            if (LOG) {
                android.util.Slog.d(TAG, "WindowOrientationListener enabled clearCurrentRotation=" + clearCurrentRotation);
            }
            this.mOrientationJudge.resetLocked(clearCurrentRotation);
            if (this.mSensor.getType() == 1) {
                this.mSensorManager.registerListener(this.mOrientationJudge, this.mSensor, this.mRate, 100000, this.mHandler);
            } else {
                this.mSensorManager.registerListener(this.mOrientationJudge, this.mSensor, this.mRate, this.mHandler);
            }
            this.mEnabled = true;
        }
    }

    public void disable() {
        synchronized (this.mLock) {
            if (this.mSensor == null) {
                android.util.Slog.w(TAG, "Cannot detect sensors. Invalid disable");
                return;
            }
            if (this.mEnabled) {
                if (LOG) {
                    android.util.Slog.d(TAG, "WindowOrientationListener disabled");
                }
                this.mSensorManager.unregisterListener(this.mOrientationJudge);
                this.mEnabled = false;
            }
        }
    }

    public void onTouchStart() {
        synchronized (this.mLock) {
            if (this.mOrientationJudge != null) {
                this.mOrientationJudge.onTouchStartLocked();
            }
        }
    }

    public void onTouchEnd() {
        long whenElapsedNanos = android.os.SystemClock.elapsedRealtimeNanos();
        synchronized (this.mLock) {
            if (this.mOrientationJudge != null) {
                this.mOrientationJudge.onTouchEndLocked(whenElapsedNanos);
            }
        }
    }

    public android.os.Handler getHandler() {
        return this.mHandler;
    }

    public void setCurrentRotation(int rotation) {
        synchronized (this.mLock) {
            this.mCurrentRotation = rotation;
        }
    }

    public int getProposedRotation() {
        synchronized (this.mLock) {
            if (!this.mEnabled) {
                return -1;
            }
            return this.mOrientationJudge.getProposedRotationLocked();
        }
    }

    public boolean canDetectOrientation() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSensor != null;
        }
        return z;
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        synchronized (this.mLock) {
            proto.write(1133871366145L, this.mEnabled);
            proto.write(1159641169922L, this.mCurrentRotation);
        }
        proto.end(token);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        synchronized (this.mLock) {
            pw.println(prefix + TAG);
            java.lang.String prefix2 = prefix + "  ";
            pw.println(prefix2 + "mEnabled=" + this.mEnabled);
            pw.println(prefix2 + "mCurrentRotation=" + android.view.Surface.rotationToString(this.mCurrentRotation));
            pw.println(prefix2 + "mSensorType=" + this.mSensorType);
            pw.println(prefix2 + "mSensor=" + this.mSensor);
            pw.println(prefix2 + "mRate=" + this.mRate);
            if (this.mOrientationJudge != null) {
                this.mOrientationJudge.dumpLocked(pw, prefix2);
            }
        }
    }

    public boolean shouldStayEnabledWhileDreaming() {
        if (this.mContext.getResources().getBoolean(android.R.bool.config_enable_iwlan_handover_policy)) {
            return true;
        }
        return this.mSensor.getType() == 27 && this.mSensor.isWakeUpSensor();
    }

    abstract class OrientationJudge implements android.hardware.SensorEventListener {
        protected static final float MILLIS_PER_NANO = 1.0E-6f;
        protected static final long NANOS_PER_MS = 1000000;
        protected static final long PROPOSAL_MIN_TIME_SINCE_TOUCH_END_NANOS = 500000000;

        public abstract void dumpLocked(java.io.PrintWriter printWriter, java.lang.String str);

        public abstract int getProposedRotationLocked();

        @Override // android.hardware.SensorEventListener
        public abstract void onAccuracyChanged(android.hardware.Sensor sensor, int i);

        @Override // android.hardware.SensorEventListener
        public abstract void onSensorChanged(android.hardware.SensorEvent sensorEvent);

        public abstract void onTouchEndLocked(long j);

        public abstract void onTouchStartLocked();

        public abstract void resetLocked(boolean z);

        OrientationJudge() {
        }
    }

    final class AccelSensorJudge extends com.android.server.wm.WindowOrientationListener.OrientationJudge {
        private static final float ACCELERATION_TOLERANCE = 4.0f;
        private static final int ACCELEROMETER_DATA_X = 0;
        private static final int ACCELEROMETER_DATA_Y = 1;
        private static final int ACCELEROMETER_DATA_Z = 2;
        private static final int ADJACENT_ORIENTATION_ANGLE_GAP = 45;
        private static final float FILTER_TIME_CONSTANT_MS = 200.0f;
        private static final float FLAT_ANGLE = 80.0f;
        private static final long FLAT_TIME_NANOS = 1000000000;
        private static final float MAX_ACCELERATION_MAGNITUDE = 13.80665f;
        private static final long MAX_FILTER_DELTA_TIME_NANOS = 1000000000;
        private static final int MAX_TILT = 80;
        private static final float MIN_ACCELERATION_MAGNITUDE = 5.80665f;
        private static final float NEAR_ZERO_MAGNITUDE = 1.0f;
        private static final long PROPOSAL_MIN_TIME_SINCE_ACCELERATION_ENDED_NANOS = 500000000;
        private static final long PROPOSAL_MIN_TIME_SINCE_FLAT_ENDED_NANOS = 500000000;
        private static final long PROPOSAL_MIN_TIME_SINCE_SWING_ENDED_NANOS = 300000000;
        private static final long PROPOSAL_SETTLE_TIME_NANOS = 40000000;
        private static final float RADIANS_TO_DEGREES = 57.29578f;
        private static final float SWING_AWAY_ANGLE_DELTA = 20.0f;
        private static final long SWING_TIME_NANOS = 300000000;
        private static final int TILT_HISTORY_SIZE = 200;
        private static final int TILT_OVERHEAD_ENTER = -40;
        private static final int TILT_OVERHEAD_EXIT = -15;
        private boolean mAccelerating;
        private long mAccelerationTimestampNanos;
        private boolean mFlat;
        private long mFlatTimestampNanos;
        private long mLastFilteredTimestampNanos;
        private float mLastFilteredX;
        private float mLastFilteredY;
        private float mLastFilteredZ;
        private boolean mOverhead;
        private int mPredictedRotation;
        private long mPredictedRotationTimestampNanos;
        private int mProposedRotation;
        private long mSwingTimestampNanos;
        private boolean mSwinging;
        private float[] mTiltHistory;
        private int mTiltHistoryIndex;
        private long[] mTiltHistoryTimestampNanos;
        private final int[][] mTiltToleranceConfig;
        private long mTouchEndedTimestampNanos;
        private boolean mTouched;

        public AccelSensorJudge(android.content.Context context) {
            super();
            this.mTiltToleranceConfig = new int[][]{new int[]{-25, 70}, new int[]{-25, 65}, new int[]{-25, 60}, new int[]{-25, 65}};
            this.mTouchEndedTimestampNanos = Long.MIN_VALUE;
            this.mTiltHistory = new float[200];
            this.mTiltHistoryTimestampNanos = new long[200];
            int[] tiltTolerance = context.getResources().getIntArray(android.R.array.config_autoKeyboardBacklightIncreaseLuxThreshold);
            if (tiltTolerance.length == 8) {
                for (int i = 0; i < 4; i++) {
                    int min = tiltTolerance[i * 2];
                    int max = tiltTolerance[(i * 2) + 1];
                    if (min >= -90 && min <= max && max <= 90) {
                        this.mTiltToleranceConfig[i][0] = min;
                        this.mTiltToleranceConfig[i][1] = max;
                    } else {
                        android.util.Slog.wtf(com.android.server.wm.WindowOrientationListener.TAG, "config_autoRotationTiltTolerance contains invalid range: min=" + min + ", max=" + max);
                    }
                }
                return;
            }
            android.util.Slog.wtf(com.android.server.wm.WindowOrientationListener.TAG, "config_autoRotationTiltTolerance should have exactly 8 elements");
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public int getProposedRotationLocked() {
            return this.mProposedRotation;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void dumpLocked(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "AccelSensorJudge");
            java.lang.String prefix2 = prefix + "  ";
            pw.println(prefix2 + "mProposedRotation=" + this.mProposedRotation);
            pw.println(prefix2 + "mPredictedRotation=" + this.mPredictedRotation);
            pw.println(prefix2 + "mLastFilteredX=" + this.mLastFilteredX);
            pw.println(prefix2 + "mLastFilteredY=" + this.mLastFilteredY);
            pw.println(prefix2 + "mLastFilteredZ=" + this.mLastFilteredZ);
            long delta = android.os.SystemClock.elapsedRealtimeNanos() - this.mLastFilteredTimestampNanos;
            pw.println(prefix2 + "mLastFilteredTimestampNanos=" + this.mLastFilteredTimestampNanos + " (" + (delta * 1.0E-6f) + "ms ago)");
            pw.println(prefix2 + "mTiltHistory={last: " + getLastTiltLocked() + "}");
            pw.println(prefix2 + "mFlat=" + this.mFlat);
            pw.println(prefix2 + "mSwinging=" + this.mSwinging);
            pw.println(prefix2 + "mAccelerating=" + this.mAccelerating);
            pw.println(prefix2 + "mOverhead=" + this.mOverhead);
            pw.println(prefix2 + "mTouched=" + this.mTouched);
            pw.print(prefix2 + "mTiltToleranceConfig=[");
            for (int i = 0; i < 4; i++) {
                if (i != 0) {
                    pw.print(", ");
                }
                pw.print("[");
                pw.print(this.mTiltToleranceConfig[i][0]);
                pw.print(", ");
                pw.print(this.mTiltToleranceConfig[i][1]);
                pw.print("]");
            }
            pw.println("]");
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge, android.hardware.SensorEventListener
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge, android.hardware.SensorEventListener
        public void onSensorChanged(android.hardware.SensorEvent event) {
            boolean skipSample;
            int oldProposedRotation;
            int proposedRotation;
            boolean isFlat;
            boolean isSwinging;
            float z;
            synchronized (com.android.server.wm.WindowOrientationListener.this.mLock) {
                float x = event.values[0];
                float y = event.values[1];
                float z2 = event.values[2];
                if (com.android.server.wm.WindowOrientationListener.LOG) {
                    android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "Raw acceleration vector: x=" + x + ", y=" + y + ", z=" + z2 + ", magnitude=" + java.lang.Math.sqrt((x * x) + (y * y) + (z2 * z2)));
                }
                long now = event.timestamp;
                long then = this.mLastFilteredTimestampNanos;
                float timeDeltaMS = (now - then) * 1.0E-6f;
                if (now < then || now > 1000000000 + then || (x == 0.0f && y == 0.0f && z2 == 0.0f)) {
                    boolean skipSample2 = com.android.server.wm.WindowOrientationListener.LOG;
                    if (skipSample2) {
                        android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "Resetting orientation listener.");
                    }
                    resetLocked(true);
                    skipSample = true;
                } else {
                    float alpha = timeDeltaMS / (FILTER_TIME_CONSTANT_MS + timeDeltaMS);
                    x = ((x - this.mLastFilteredX) * alpha) + this.mLastFilteredX;
                    y = ((y - this.mLastFilteredY) * alpha) + this.mLastFilteredY;
                    float z3 = ((z2 - this.mLastFilteredZ) * alpha) + this.mLastFilteredZ;
                    if (!com.android.server.wm.WindowOrientationListener.LOG) {
                        z = z3;
                    } else {
                        z = z3;
                        android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "Filtered acceleration vector: x=" + x + ", y=" + y + ", z=" + z3 + ", magnitude=" + java.lang.Math.sqrt((x * x) + (y * y) + (z3 * z3)));
                    }
                    skipSample = false;
                    z2 = z;
                }
                this.mLastFilteredTimestampNanos = now;
                this.mLastFilteredX = x;
                this.mLastFilteredY = y;
                this.mLastFilteredZ = z2;
                boolean isAccelerating = false;
                boolean isFlat2 = false;
                boolean isSwinging2 = false;
                if (!skipSample) {
                    float magnitude = (float) java.lang.Math.sqrt((x * x) + (y * y) + (z2 * z2));
                    if (magnitude < 1.0f) {
                        if (com.android.server.wm.WindowOrientationListener.LOG) {
                            android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "Ignoring sensor data, magnitude too close to zero.");
                        }
                        clearPredictedRotationLocked();
                    } else {
                        if (isAcceleratingLocked(magnitude)) {
                            isAccelerating = true;
                            this.mAccelerationTimestampNanos = now;
                        }
                        boolean isAccelerating2 = isAccelerating;
                        int tiltAngle = (int) java.lang.Math.round(java.lang.Math.asin(z2 / magnitude) * 57.295780181884766d);
                        addTiltHistoryEntryLocked(now, tiltAngle);
                        if (isFlatLocked(now)) {
                            isFlat2 = true;
                            this.mFlatTimestampNanos = now;
                        }
                        if (isSwingingLocked(now, tiltAngle)) {
                            isSwinging2 = true;
                            this.mSwingTimestampNanos = now;
                        }
                        if (tiltAngle <= TILT_OVERHEAD_ENTER) {
                            this.mOverhead = true;
                        } else if (tiltAngle >= -15) {
                            this.mOverhead = false;
                        }
                        if (this.mOverhead) {
                            if (com.android.server.wm.WindowOrientationListener.LOG) {
                                android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "Ignoring sensor data, device is overhead: tiltAngle=" + tiltAngle);
                            }
                            clearPredictedRotationLocked();
                            isFlat = isFlat2;
                            isSwinging = isSwinging2;
                        } else if (java.lang.Math.abs(tiltAngle) > 80) {
                            if (com.android.server.wm.WindowOrientationListener.LOG) {
                                android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "Ignoring sensor data, tilt angle too high: tiltAngle=" + tiltAngle);
                            }
                            clearPredictedRotationLocked();
                            isFlat = isFlat2;
                            isSwinging = isSwinging2;
                        } else {
                            isFlat = isFlat2;
                            isSwinging = isSwinging2;
                            int orientationAngle = (int) java.lang.Math.round((-java.lang.Math.atan2(-x, y)) * 57.295780181884766d);
                            if (orientationAngle < 0) {
                                orientationAngle += 360;
                            }
                            int nearestRotation = (orientationAngle + 45) / 90;
                            if (nearestRotation == 4) {
                                nearestRotation = 0;
                            }
                            if (isTiltAngleAcceptableLocked(nearestRotation, tiltAngle) && isOrientationAngleAcceptableLocked(nearestRotation, orientationAngle)) {
                                updatePredictedRotationLocked(now, nearestRotation);
                                if (com.android.server.wm.WindowOrientationListener.LOG) {
                                    android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "Predicted: tiltAngle=" + tiltAngle + ", orientationAngle=" + orientationAngle + ", predictedRotation=" + this.mPredictedRotation + ", predictedRotationAgeMS=" + ((now - this.mPredictedRotationTimestampNanos) * 1.0E-6f));
                                }
                            } else {
                                if (com.android.server.wm.WindowOrientationListener.LOG) {
                                    android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "Ignoring sensor data, no predicted rotation: tiltAngle=" + tiltAngle + ", orientationAngle=" + orientationAngle);
                                }
                                clearPredictedRotationLocked();
                            }
                        }
                        isFlat2 = isFlat;
                        isSwinging2 = isSwinging;
                        isAccelerating = isAccelerating2;
                    }
                }
                this.mFlat = isFlat2;
                this.mSwinging = isSwinging2;
                this.mAccelerating = isAccelerating;
                oldProposedRotation = this.mProposedRotation;
                if (this.mPredictedRotation < 0 || isPredictedRotationAcceptableLocked(now)) {
                    this.mProposedRotation = this.mPredictedRotation;
                }
                proposedRotation = this.mProposedRotation;
                if (com.android.server.wm.WindowOrientationListener.LOG) {
                    android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "Result: currentRotation=" + com.android.server.wm.WindowOrientationListener.this.mCurrentRotation + ", proposedRotation=" + proposedRotation + ", predictedRotation=" + this.mPredictedRotation + ", timeDeltaMS=" + timeDeltaMS + ", isAccelerating=" + isAccelerating + ", isFlat=" + isFlat2 + ", isSwinging=" + isSwinging2 + ", isOverhead=" + this.mOverhead + ", isTouched=" + this.mTouched + ", timeUntilSettledMS=" + remainingMS(now, this.mPredictedRotationTimestampNanos + PROPOSAL_SETTLE_TIME_NANOS) + ", timeUntilAccelerationDelayExpiredMS=" + remainingMS(now, this.mAccelerationTimestampNanos + 500000000) + ", timeUntilFlatDelayExpiredMS=" + remainingMS(now, this.mFlatTimestampNanos + 500000000) + ", timeUntilSwingDelayExpiredMS=" + remainingMS(now, this.mSwingTimestampNanos + 300000000) + ", timeUntilTouchDelayExpiredMS=" + remainingMS(now, this.mTouchEndedTimestampNanos + 500000000));
                }
            }
            if (proposedRotation != oldProposedRotation && proposedRotation >= 0) {
                if (com.android.server.wm.WindowOrientationListener.LOG) {
                    android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "Proposed rotation changed!  proposedRotation=" + proposedRotation + ", oldProposedRotation=" + oldProposedRotation);
                }
                com.android.server.wm.WindowOrientationListener.this.onProposedRotationChanged(proposedRotation);
            }
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void onTouchStartLocked() {
            this.mTouched = true;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void onTouchEndLocked(long whenElapsedNanos) {
            this.mTouched = false;
            this.mTouchEndedTimestampNanos = whenElapsedNanos;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void resetLocked(boolean clearCurrentRotation) {
            this.mLastFilteredTimestampNanos = Long.MIN_VALUE;
            if (clearCurrentRotation) {
                this.mProposedRotation = -1;
            }
            this.mFlatTimestampNanos = Long.MIN_VALUE;
            this.mFlat = false;
            this.mSwingTimestampNanos = Long.MIN_VALUE;
            this.mSwinging = false;
            this.mAccelerationTimestampNanos = Long.MIN_VALUE;
            this.mAccelerating = false;
            this.mOverhead = false;
            clearPredictedRotationLocked();
            clearTiltHistoryLocked();
        }

        private boolean isTiltAngleAcceptableLocked(int rotation, int tiltAngle) {
            return tiltAngle >= this.mTiltToleranceConfig[rotation][0] && tiltAngle <= this.mTiltToleranceConfig[rotation][1];
        }

        private boolean isOrientationAngleAcceptableLocked(int rotation, int orientationAngle) {
            int currentRotation = com.android.server.wm.WindowOrientationListener.this.mCurrentRotation;
            if (currentRotation >= 0) {
                if (rotation == currentRotation || rotation == (currentRotation + 1) % 4) {
                    int lowerBound = ((rotation * 90) - 45) + 22;
                    if (rotation == 0) {
                        if (orientationAngle >= 315 && orientationAngle < lowerBound + 360) {
                            return false;
                        }
                    } else if (orientationAngle < lowerBound) {
                        return false;
                    }
                }
                if (rotation == currentRotation || rotation == (currentRotation + 3) % 4) {
                    int upperBound = ((rotation * 90) + 45) - 22;
                    return rotation == 0 ? orientationAngle > 45 || orientationAngle <= upperBound : orientationAngle <= upperBound;
                }
                return true;
            }
            return true;
        }

        private boolean isPredictedRotationAcceptableLocked(long now) {
            return now >= this.mPredictedRotationTimestampNanos + PROPOSAL_SETTLE_TIME_NANOS && now >= this.mFlatTimestampNanos + 500000000 && now >= this.mSwingTimestampNanos + 300000000 && now >= this.mAccelerationTimestampNanos + 500000000 && !this.mTouched && now >= this.mTouchEndedTimestampNanos + 500000000;
        }

        private void clearPredictedRotationLocked() {
            this.mPredictedRotation = -1;
            this.mPredictedRotationTimestampNanos = Long.MIN_VALUE;
        }

        private void updatePredictedRotationLocked(long now, int rotation) {
            if (this.mPredictedRotation != rotation) {
                this.mPredictedRotation = rotation;
                this.mPredictedRotationTimestampNanos = now;
            }
        }

        private boolean isAcceleratingLocked(float magnitude) {
            return magnitude < MIN_ACCELERATION_MAGNITUDE || magnitude > MAX_ACCELERATION_MAGNITUDE;
        }

        private void clearTiltHistoryLocked() {
            this.mTiltHistoryTimestampNanos[0] = Long.MIN_VALUE;
            this.mTiltHistoryIndex = 1;
        }

        private void addTiltHistoryEntryLocked(long now, float tilt) {
            this.mTiltHistory[this.mTiltHistoryIndex] = tilt;
            this.mTiltHistoryTimestampNanos[this.mTiltHistoryIndex] = now;
            this.mTiltHistoryIndex = (this.mTiltHistoryIndex + 1) % 200;
            this.mTiltHistoryTimestampNanos[this.mTiltHistoryIndex] = Long.MIN_VALUE;
        }

        private boolean isFlatLocked(long now) {
            int i = this.mTiltHistoryIndex;
            do {
                int iNextTiltHistoryIndexLocked = nextTiltHistoryIndexLocked(i);
                i = iNextTiltHistoryIndexLocked;
                if (iNextTiltHistoryIndexLocked < 0 || this.mTiltHistory[i] < FLAT_ANGLE) {
                    return false;
                }
            } while (this.mTiltHistoryTimestampNanos[i] + 1000000000 > now);
            return true;
        }

        private boolean isSwingingLocked(long now, float tilt) {
            int i = this.mTiltHistoryIndex;
            do {
                int iNextTiltHistoryIndexLocked = nextTiltHistoryIndexLocked(i);
                i = iNextTiltHistoryIndexLocked;
                if (iNextTiltHistoryIndexLocked < 0 || this.mTiltHistoryTimestampNanos[i] + 300000000 < now) {
                    return false;
                }
            } while (this.mTiltHistory[i] + SWING_AWAY_ANGLE_DELTA > tilt);
            return true;
        }

        private int nextTiltHistoryIndexLocked(int index) {
            int index2 = (index == 0 ? 200 : index) - 1;
            if (this.mTiltHistoryTimestampNanos[index2] != Long.MIN_VALUE) {
                return index2;
            }
            return -1;
        }

        private float getLastTiltLocked() {
            int index = nextTiltHistoryIndexLocked(this.mTiltHistoryIndex);
            if (index >= 0) {
                return this.mTiltHistory[index];
            }
            return Float.NaN;
        }

        private float remainingMS(long now, long until) {
            if (now >= until) {
                return 0.0f;
            }
            return (until - now) * 1.0E-6f;
        }
    }

    final class OrientationSensorJudge extends com.android.server.wm.WindowOrientationListener.OrientationJudge {
        private static final int ROTATION_UNSET = -1;
        private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;
        private java.lang.Runnable mCancelRotationResolverRequest;
        private int mCurrentCallbackId;
        private int mDesiredRotation;
        private int mLastRotationResolution;
        private long mLastRotationResolutionTimeStamp;
        private int mProposedRotation;
        private boolean mRotationEvaluationScheduled;
        private java.lang.Runnable mRotationEvaluator;
        private long mRotationMemorizationTimeoutMillis;
        private long mRotationResolverTimeoutMillis;
        private long mTouchEndedTimestampNanos;
        private boolean mTouching;

        OrientationSensorJudge() {
            super();
            this.mTouchEndedTimestampNanos = Long.MIN_VALUE;
            this.mProposedRotation = -1;
            this.mDesiredRotation = -1;
            this.mLastRotationResolution = -1;
            this.mCurrentCallbackId = 0;
            this.mRotationEvaluator = new java.lang.Runnable() { // from class: com.android.server.wm.WindowOrientationListener.OrientationSensorJudge.2
                @Override // java.lang.Runnable
                public void run() {
                    int newRotation;
                    synchronized (com.android.server.wm.WindowOrientationListener.this.mLock) {
                        com.android.server.wm.WindowOrientationListener.OrientationSensorJudge.this.mRotationEvaluationScheduled = false;
                        newRotation = com.android.server.wm.WindowOrientationListener.OrientationSensorJudge.this.evaluateRotationChangeLocked();
                    }
                    if (newRotation >= 0) {
                        com.android.server.wm.WindowOrientationListener.this.onProposedRotationChanged(newRotation);
                    }
                }
            };
            setupRotationResolverParameters();
            this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        }

        private void setupRotationResolverParameters() {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("window_manager", android.app.ActivityThread.currentApplication().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wm.WindowOrientationListener$OrientationSensorJudge$$ExternalSyntheticLambda1
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.lambda$setupRotationResolverParameters$0(properties);
                }
            });
            readRotationResolverParameters();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setupRotationResolverParameters$0(android.provider.DeviceConfig.Properties properties) {
            java.util.Set<java.lang.String> keys = properties.getKeyset();
            if (keys.contains(com.android.server.wm.WindowOrientationListener.KEY_ROTATION_RESOLVER_TIMEOUT) || keys.contains(com.android.server.wm.WindowOrientationListener.KEY_ROTATION_MEMORIZATION_TIMEOUT)) {
                readRotationResolverParameters();
            }
        }

        private void readRotationResolverParameters() {
            this.mRotationResolverTimeoutMillis = android.provider.DeviceConfig.getLong("window_manager", com.android.server.wm.WindowOrientationListener.KEY_ROTATION_RESOLVER_TIMEOUT, com.android.server.wm.WindowOrientationListener.DEFAULT_ROTATION_RESOLVER_TIMEOUT_MILLIS);
            this.mRotationMemorizationTimeoutMillis = android.provider.DeviceConfig.getLong("window_manager", com.android.server.wm.WindowOrientationListener.KEY_ROTATION_MEMORIZATION_TIMEOUT, 3000L);
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public int getProposedRotationLocked() {
            return this.mProposedRotation;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void onTouchStartLocked() {
            this.mTouching = true;
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void onTouchEndLocked(long whenElapsedNanos) {
            this.mTouching = false;
            this.mTouchEndedTimestampNanos = whenElapsedNanos;
            if (this.mDesiredRotation != this.mProposedRotation) {
                long now = android.os.SystemClock.elapsedRealtimeNanos();
                scheduleRotationEvaluationIfNecessaryLocked(now);
            }
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge, android.hardware.SensorEventListener
        public void onSensorChanged(android.hardware.SensorEvent event) {
            java.lang.String packageName;
            com.android.server.wm.WindowProcessController controller;
            final int reportedRotation = (int) event.values[0];
            if (reportedRotation == 4) {
                android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "onSensorChanged mDesiredRotation = 4");
                resetLocked(true);
                return;
            }
            if (reportedRotation < 0 || reportedRotation > 3) {
                return;
            }
            if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_AMS) {
                android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "onSensorChanged mDesiredRotation = " + reportedRotation);
            }
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DEVICE_ROTATED, event.timestamp, rotationToLogEnum(reportedRotation), 2);
            if (com.android.server.wm.WindowOrientationListener.this.isRotationResolverEnabled()) {
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_AMS) {
                    android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "isRotationResolverEnabled() = trueisKeyguardShowingAndNotOccluded() = " + com.android.server.wm.WindowOrientationListener.this.isKeyguardShowingAndNotOccluded());
                }
                if (com.android.server.wm.WindowOrientationListener.this.isKeyguardShowingAndNotOccluded()) {
                    if (this.mLastRotationResolution != -1 && android.os.SystemClock.uptimeMillis() - this.mLastRotationResolutionTimeStamp < this.mRotationMemorizationTimeoutMillis) {
                        android.util.Slog.d(com.android.server.wm.WindowOrientationListener.TAG, "Reusing the last rotation resolution: " + this.mLastRotationResolution);
                        finalizeRotation(this.mLastRotationResolution);
                        return;
                    } else {
                        finalizeRotation(com.android.server.wm.WindowOrientationListener.this.mDefaultRotation);
                        return;
                    }
                }
                if (com.android.server.wm.WindowOrientationListener.this.mRotationResolverService == null) {
                    com.android.server.wm.WindowOrientationListener.this.mRotationResolverService = (android.rotationresolver.RotationResolverInternal) com.android.server.LocalServices.getService(android.rotationresolver.RotationResolverInternal.class);
                    if (com.android.server.wm.WindowOrientationListener.this.mRotationResolverService == null) {
                        finalizeRotation(reportedRotation);
                        return;
                    }
                }
                if (com.android.server.wm.ActivityTaskManagerDebugConfig.DEBUG_AMS && com.android.server.wm.WindowOrientationListener.this.mRotationResolverService == null) {
                    android.util.Slog.v(com.android.server.wm.WindowOrientationListener.TAG, "mRotationResolverService = null");
                }
                if (this.mActivityTaskManagerInternal != null && (controller = this.mActivityTaskManagerInternal.getTopApp()) != null && controller.mInfo != null && controller.mInfo.packageName != null) {
                    java.lang.String packageName2 = controller.mInfo.packageName;
                    packageName = packageName2;
                } else {
                    packageName = null;
                }
                this.mCurrentCallbackId++;
                if (this.mCancelRotationResolverRequest != null) {
                    com.android.server.wm.WindowOrientationListener.this.getHandler().removeCallbacks(this.mCancelRotationResolverRequest);
                }
                final android.os.CancellationSignal cancellationSignal = new android.os.CancellationSignal();
                java.util.Objects.requireNonNull(cancellationSignal);
                this.mCancelRotationResolverRequest = new java.lang.Runnable() { // from class: com.android.server.wm.WindowOrientationListener$OrientationSensorJudge$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        cancellationSignal.cancel();
                    }
                };
                com.android.server.wm.WindowOrientationListener.this.getHandler().postDelayed(this.mCancelRotationResolverRequest, this.mRotationResolverTimeoutMillis);
                if (com.android.server.wm.WindowOrientationListener.this.mRotationResolverService == null) {
                    com.android.server.wm.WindowOrientationListener.this.getHandler().removeCallbacks(this.mCancelRotationResolverRequest);
                    finalizeRotation(reportedRotation);
                    return;
                } else {
                    com.android.server.wm.WindowOrientationListener.this.mRotationResolverService.resolveRotation(new android.rotationresolver.RotationResolverInternal.RotationResolverCallbackInternal() { // from class: com.android.server.wm.WindowOrientationListener.OrientationSensorJudge.1
                        private final int mCallbackId;

                        {
                            this.mCallbackId = com.android.server.wm.WindowOrientationListener.OrientationSensorJudge.this.mCurrentCallbackId;
                        }

                        public void onSuccess(int result) {
                            finalizeRotationIfFresh(result);
                        }

                        public void onFailure(int error) {
                            finalizeRotationIfFresh(reportedRotation);
                        }

                        private void finalizeRotationIfFresh(int rotation) {
                            if (this.mCallbackId == com.android.server.wm.WindowOrientationListener.OrientationSensorJudge.this.mCurrentCallbackId) {
                                com.android.server.wm.WindowOrientationListener.this.getHandler().removeCallbacks(com.android.server.wm.WindowOrientationListener.OrientationSensorJudge.this.mCancelRotationResolverRequest);
                                com.android.server.wm.WindowOrientationListener.OrientationSensorJudge.this.finalizeRotation(rotation);
                            } else {
                                android.util.Slog.d(com.android.server.wm.WindowOrientationListener.TAG, java.lang.String.format("An outdated callback received [%s vs. %s]. Ignoring it.", java.lang.Integer.valueOf(this.mCallbackId), java.lang.Integer.valueOf(com.android.server.wm.WindowOrientationListener.OrientationSensorJudge.this.mCurrentCallbackId)));
                            }
                        }
                    }, packageName, reportedRotation, com.android.server.wm.WindowOrientationListener.this.mCurrentRotation, this.mRotationResolverTimeoutMillis, cancellationSignal);
                    return;
                }
            }
            finalizeRotation(reportedRotation);
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge, android.hardware.SensorEventListener
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void dumpLocked(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.println(prefix + "OrientationSensorJudge");
            java.lang.String prefix2 = prefix + "  ";
            pw.println(prefix2 + "mDesiredRotation=" + android.view.Surface.rotationToString(this.mDesiredRotation));
            pw.println(prefix2 + "mProposedRotation=" + android.view.Surface.rotationToString(this.mProposedRotation));
            pw.println(prefix2 + "mTouching=" + this.mTouching);
            pw.println(prefix2 + "mTouchEndedTimestampNanos=" + this.mTouchEndedTimestampNanos);
            pw.println(prefix2 + "mLastRotationResolution=" + this.mLastRotationResolution);
        }

        @Override // com.android.server.wm.WindowOrientationListener.OrientationJudge
        public void resetLocked(boolean clearCurrentRotation) {
            if (clearCurrentRotation) {
                this.mProposedRotation = -1;
                this.mDesiredRotation = -1;
            }
            this.mTouching = false;
            this.mTouchEndedTimestampNanos = Long.MIN_VALUE;
            unscheduleRotationEvaluationLocked();
        }

        public int evaluateRotationChangeLocked() {
            unscheduleRotationEvaluationLocked();
            if (this.mDesiredRotation == this.mProposedRotation) {
                return -1;
            }
            long now = android.os.SystemClock.elapsedRealtimeNanos();
            if (isDesiredRotationAcceptableLocked(now)) {
                this.mProposedRotation = this.mDesiredRotation;
                return this.mProposedRotation;
            }
            scheduleRotationEvaluationIfNecessaryLocked(now);
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void finalizeRotation(int reportedRotation) {
            int newRotation;
            synchronized (com.android.server.wm.WindowOrientationListener.this.mLock) {
                this.mDesiredRotation = reportedRotation;
                newRotation = evaluateRotationChangeLocked();
            }
            if (newRotation >= 0) {
                this.mLastRotationResolution = newRotation;
                this.mLastRotationResolutionTimeStamp = android.os.SystemClock.uptimeMillis();
                com.android.server.wm.WindowOrientationListener.this.onProposedRotationChanged(newRotation);
            }
        }

        private boolean isDesiredRotationAcceptableLocked(long now) {
            return !this.mTouching && now >= this.mTouchEndedTimestampNanos + 500000000;
        }

        private void scheduleRotationEvaluationIfNecessaryLocked(long now) {
            if (this.mRotationEvaluationScheduled || this.mDesiredRotation == this.mProposedRotation) {
                if (com.android.server.wm.WindowOrientationListener.LOG) {
                    android.util.Slog.d(com.android.server.wm.WindowOrientationListener.TAG, "scheduleRotationEvaluationLocked: ignoring, an evaluation is already scheduled or is unnecessary.");
                }
            } else {
                if (this.mTouching) {
                    if (com.android.server.wm.WindowOrientationListener.LOG) {
                        android.util.Slog.d(com.android.server.wm.WindowOrientationListener.TAG, "scheduleRotationEvaluationLocked: ignoring, user is still touching the screen.");
                        return;
                    }
                    return;
                }
                long timeOfNextPossibleRotationNanos = this.mTouchEndedTimestampNanos + 500000000;
                if (now >= timeOfNextPossibleRotationNanos) {
                    if (com.android.server.wm.WindowOrientationListener.LOG) {
                        android.util.Slog.d(com.android.server.wm.WindowOrientationListener.TAG, "scheduleRotationEvaluationLocked: ignoring, already past the next possible time of rotation.");
                    }
                } else {
                    long delayMs = (long) java.lang.Math.ceil((timeOfNextPossibleRotationNanos - now) * 1.0E-6f);
                    com.android.server.wm.WindowOrientationListener.this.mHandler.postDelayed(this.mRotationEvaluator, delayMs);
                    this.mRotationEvaluationScheduled = true;
                }
            }
        }

        private void unscheduleRotationEvaluationLocked() {
            if (!this.mRotationEvaluationScheduled) {
                return;
            }
            com.android.server.wm.WindowOrientationListener.this.mHandler.removeCallbacks(this.mRotationEvaluator);
            this.mRotationEvaluationScheduled = false;
        }

        private int rotationToLogEnum(int rotation) {
            switch (rotation) {
                case 0:
                    return 1;
                case 1:
                    return 2;
                case 2:
                    return 3;
                case 3:
                    return 4;
                default:
                    return 0;
            }
        }
    }
}
