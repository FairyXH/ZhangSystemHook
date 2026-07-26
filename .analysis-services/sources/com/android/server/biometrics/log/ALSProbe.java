package com.android.server.biometrics.log;

/* JADX INFO: loaded from: classes.dex */
final class ALSProbe implements com.android.server.biometrics.log.Probe {
    private static final java.lang.String TAG = "ALSProbe";
    private boolean mDestroyRequested;
    private boolean mDestroyed;
    private boolean mDisableRequested;
    private boolean mEnabled;
    private volatile float mLastAmbientLux;
    private final android.hardware.Sensor mLightSensor;
    private final android.hardware.SensorEventListener mLightSensorListener;
    private long mMaxSubscriptionTime;
    private com.android.server.biometrics.log.ALSProbe.NextConsumer mNextConsumer;
    private final android.hardware.SensorManager mSensorManager;
    private final android.os.Handler mTimer;

    ALSProbe(android.hardware.SensorManager sensorManager) {
        this(sensorManager, new android.os.Handler(android.os.Looper.getMainLooper()), java.util.concurrent.TimeUnit.MINUTES.toMillis(1L));
    }

    ALSProbe(android.hardware.SensorManager sensorManager, android.os.Handler handler, long maxTime) {
        this.mMaxSubscriptionTime = -1L;
        this.mEnabled = false;
        this.mDestroyed = false;
        this.mDestroyRequested = false;
        this.mDisableRequested = false;
        this.mNextConsumer = null;
        this.mLastAmbientLux = -1.0f;
        this.mLightSensorListener = new android.hardware.SensorEventListener() { // from class: com.android.server.biometrics.log.ALSProbe.1
            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(android.hardware.SensorEvent event) {
                com.android.server.biometrics.log.ALSProbe.this.onNext(event.values[0]);
            }

            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
            }
        };
        this.mSensorManager = sensorManager;
        this.mLightSensor = sensorManager != null ? sensorManager.getDefaultSensor(5) : null;
        this.mTimer = handler;
        this.mMaxSubscriptionTime = maxTime;
        if (this.mSensorManager == null || this.mLightSensor == null) {
            android.util.Slog.w(TAG, "No sensor - probe disabled");
            this.mDestroyed = true;
        }
    }

    @Override // com.android.server.biometrics.log.Probe
    public synchronized void enable() {
        if (!this.mDestroyed && !this.mDestroyRequested) {
            this.mDisableRequested = false;
            com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.log.ALSProbe$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$enable$0();
                }
            });
        }
    }

    @Override // com.android.server.biometrics.log.Probe
    public synchronized void disable() {
        this.mDisableRequested = true;
        if (!this.mDestroyed && this.mNextConsumer == null) {
            com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.log.ALSProbe$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$disable$1();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$disable$1() {
        disableLightSensorLoggingLocked(false);
    }

    @Override // com.android.server.biometrics.log.Probe
    public synchronized void destroy() {
        this.mDestroyRequested = true;
        if (!this.mDestroyed && this.mNextConsumer == null) {
            com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.log.ALSProbe$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$destroy$2();
                }
            });
            this.mDestroyed = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$destroy$2() {
        disableLightSensorLoggingLocked(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onNext(float value) {
        this.mLastAmbientLux = value;
        com.android.server.biometrics.log.ALSProbe.NextConsumer consumer = this.mNextConsumer;
        this.mNextConsumer = null;
        if (consumer != null) {
            android.util.Slog.v(TAG, "Finishing next consumer");
            if (this.mDestroyRequested) {
                destroy();
            } else if (this.mDisableRequested) {
                disable();
            }
            consumer.consume(value);
        }
    }

    public float getMostRecentLux() {
        return this.mLastAmbientLux;
    }

    public synchronized void awaitNextLux(java.util.function.Consumer<java.lang.Float> consumer, android.os.Handler handler) {
        com.android.server.biometrics.log.ALSProbe.NextConsumer nextConsumer = new com.android.server.biometrics.log.ALSProbe.NextConsumer(consumer, handler);
        float current = this.mLastAmbientLux;
        if (current > -1.0f) {
            nextConsumer.consume(current);
        } else if (this.mNextConsumer != null) {
            this.mNextConsumer.add(nextConsumer);
        } else if (this.mLightSensor != null) {
            this.mDestroyed = false;
            this.mNextConsumer = nextConsumer;
            com.android.server.OplusBackgroundThread.get().getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.biometrics.log.ALSProbe$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$awaitNextLux$3();
                }
            });
        } else {
            android.util.Slog.w(TAG, "No light sensor - use current to consume");
            nextConsumer.consume(current);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: enableLightSensorLoggingLocked, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$enable$0() {
        if (!this.mEnabled && this.mLightSensor != null) {
            this.mEnabled = true;
            this.mLastAmbientLux = -1.0f;
            this.mSensorManager.registerListener(this.mLightSensorListener, this.mLightSensor, 3);
            android.util.Slog.v(TAG, "Enable ALS: " + this.mLightSensorListener.hashCode());
        }
        resetTimerLocked(true);
    }

    private void disableLightSensorLoggingLocked(boolean destroying) {
        resetTimerLocked(false);
        if (this.mEnabled && this.mLightSensor != null) {
            this.mEnabled = false;
            if (!destroying) {
                this.mLastAmbientLux = -1.0f;
            }
            this.mSensorManager.unregisterListener(this.mLightSensorListener);
            android.util.Slog.v(TAG, "Disable ALS: " + this.mLightSensorListener.hashCode());
        }
    }

    private void resetTimerLocked(boolean start) {
        this.mTimer.removeCallbacksAndMessages(this);
        if (start && this.mMaxSubscriptionTime > 0) {
            this.mTimer.postDelayed(new java.lang.Runnable() { // from class: com.android.server.biometrics.log.ALSProbe$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.onTimeout();
                }
            }, this, this.mMaxSubscriptionTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onTimeout() {
        android.util.Slog.e(TAG, "Max time exceeded for ALS logger - disabling: " + this.mLightSensorListener.hashCode());
        onNext(this.mLastAmbientLux);
        disable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class NextConsumer {
        private final java.util.function.Consumer<java.lang.Float> mConsumer;
        private final android.os.Handler mHandler;
        private final java.util.List<com.android.server.biometrics.log.ALSProbe.NextConsumer> mOthers;

        private NextConsumer(java.util.function.Consumer<java.lang.Float> consumer, android.os.Handler handler) {
            this.mOthers = new java.util.ArrayList();
            this.mConsumer = consumer;
            this.mHandler = handler;
        }

        public void consume(final float value) {
            if (this.mHandler != null) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.log.ALSProbe$NextConsumer$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$consume$0(value);
                    }
                });
            } else {
                this.mConsumer.accept(java.lang.Float.valueOf(value));
            }
            for (com.android.server.biometrics.log.ALSProbe.NextConsumer c : this.mOthers) {
                c.consume(value);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$consume$0(float value) {
            this.mConsumer.accept(java.lang.Float.valueOf(value));
        }

        public void add(com.android.server.biometrics.log.ALSProbe.NextConsumer consumer) {
            this.mOthers.add(consumer);
        }
    }
}
