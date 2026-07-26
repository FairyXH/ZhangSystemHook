package com.android.server.sensorprivacy;

/* JADX INFO: loaded from: classes3.dex */
abstract class SensorPrivacyStateController {
    private static com.android.server.sensorprivacy.SensorPrivacyStateController sInstance;
    com.android.server.sensorprivacy.AllSensorStateController mAllSensorStateController = com.android.server.sensorprivacy.AllSensorStateController.getInstance();
    private final java.lang.Object mLock = new java.lang.Object();

    interface AllSensorPrivacyListener {
        void onAllSensorPrivacyChanged(boolean z);
    }

    interface SensorPrivacyListener {
        void onSensorPrivacyChanged(int i, int i2, int i3, com.android.server.sensorprivacy.SensorState sensorState);
    }

    interface SensorPrivacyStateConsumer {
        void accept(int i, int i2, int i3, com.android.server.sensorprivacy.SensorState sensorState);
    }

    interface SetStateResultCallback {
        void callback(boolean z);
    }

    abstract void dumpLocked(com.android.internal.util.dump.DualDumpOutputStream dualDumpOutputStream);

    abstract void forEachStateLocked(com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyStateConsumer sensorPrivacyStateConsumer);

    abstract com.android.server.sensorprivacy.SensorState getStateLocked(int i, int i2, int i3);

    abstract void resetForTestingImpl();

    abstract void schedulePersistLocked();

    abstract void setSensorPrivacyListenerLocked(android.os.Handler handler, com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyListener sensorPrivacyListener);

    abstract void setStateLocked(int i, int i2, int i3, int i4, android.os.Handler handler, com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback setStateResultCallback);

    abstract void setStateLocked(int i, int i2, int i3, boolean z, android.os.Handler handler, com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback setStateResultCallback);

    SensorPrivacyStateController() {
    }

    static com.android.server.sensorprivacy.SensorPrivacyStateController getInstance() {
        if (sInstance == null) {
            sInstance = com.android.server.sensorprivacy.SensorPrivacyStateControllerImpl.getInstance();
        }
        return sInstance;
    }

    com.android.server.sensorprivacy.SensorState getState(int toggleType, int userId, int sensor) {
        com.android.server.sensorprivacy.SensorState stateLocked;
        synchronized (this.mLock) {
            stateLocked = getStateLocked(toggleType, userId, sensor);
        }
        return stateLocked;
    }

    void setState(int toggleType, int userId, int sensor, boolean enabled, android.os.Handler callbackHandler, com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback callback) {
        synchronized (this.mLock) {
            setStateLocked(toggleType, userId, sensor, enabled, callbackHandler, callback);
        }
    }

    void setState(int toggleType, int userId, int sensor, int state, android.os.Handler callbackHandler, com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback callback) {
        synchronized (this.mLock) {
            setStateLocked(toggleType, userId, sensor, state, callbackHandler, callback);
        }
    }

    void setSensorPrivacyListener(android.os.Handler handler, com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyListener listener) {
        synchronized (this.mLock) {
            setSensorPrivacyListenerLocked(handler, listener);
        }
    }

    boolean getAllSensorState() {
        boolean allSensorStateLocked;
        synchronized (this.mLock) {
            allSensorStateLocked = this.mAllSensorStateController.getAllSensorStateLocked();
        }
        return allSensorStateLocked;
    }

    void setAllSensorState(boolean enable) {
        synchronized (this.mLock) {
            this.mAllSensorStateController.setAllSensorStateLocked(enable);
        }
    }

    void setAllSensorPrivacyListener(android.os.Handler handler, com.android.server.sensorprivacy.SensorPrivacyStateController.AllSensorPrivacyListener listener) {
        synchronized (this.mLock) {
            this.mAllSensorStateController.setAllSensorPrivacyListenerLocked(handler, listener);
        }
    }

    void persistAll() {
        synchronized (this.mLock) {
            this.mAllSensorStateController.schedulePersistLocked();
            schedulePersistLocked();
        }
    }

    void forEachState(com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyStateConsumer consumer) {
        synchronized (this.mLock) {
            forEachStateLocked(consumer);
        }
    }

    void dump(com.android.internal.util.dump.DualDumpOutputStream dumpStream) {
        synchronized (this.mLock) {
            this.mAllSensorStateController.dumpLocked(dumpStream);
            dumpLocked(dumpStream);
        }
        dumpStream.flush();
    }

    public void atomic(java.lang.Runnable r) {
        synchronized (this.mLock) {
            r.run();
        }
    }

    static void sendSetStateCallback(android.os.Handler callbackHandler, com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback callback, boolean success) {
        callbackHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyStateController$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback) obj).callback(((java.lang.Boolean) obj2).booleanValue());
            }
        }, callback, java.lang.Boolean.valueOf(success)));
    }

    void resetForTesting() {
        this.mAllSensorStateController.resetForTesting();
        resetForTestingImpl();
        sInstance = null;
    }
}
