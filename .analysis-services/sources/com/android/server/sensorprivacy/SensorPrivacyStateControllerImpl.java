package com.android.server.sensorprivacy;

/* JADX INFO: loaded from: classes3.dex */
class SensorPrivacyStateControllerImpl extends com.android.server.sensorprivacy.SensorPrivacyStateController {
    private static final java.lang.String SENSOR_PRIVACY_XML_FILE = "sensor_privacy_impl.xml";
    private static com.android.server.sensorprivacy.SensorPrivacyStateControllerImpl sInstance;
    private com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyListener mListener;
    private android.os.Handler mListenerHandler;
    private com.android.server.sensorprivacy.PersistedState mPersistedState = com.android.server.sensorprivacy.PersistedState.fromFile(SENSOR_PRIVACY_XML_FILE);

    static com.android.server.sensorprivacy.SensorPrivacyStateController getInstance() {
        if (sInstance == null) {
            sInstance = new com.android.server.sensorprivacy.SensorPrivacyStateControllerImpl();
        }
        return sInstance;
    }

    private SensorPrivacyStateControllerImpl() {
        persistAll();
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    com.android.server.sensorprivacy.SensorState getStateLocked(int toggleType, int userId, int sensor) {
        com.android.server.sensorprivacy.SensorState sensorState = this.mPersistedState.getState(toggleType, userId, sensor);
        if (sensorState != null) {
            return new com.android.server.sensorprivacy.SensorState(sensorState);
        }
        return getDefaultSensorState();
    }

    private static com.android.server.sensorprivacy.SensorState getDefaultSensorState() {
        return new com.android.server.sensorprivacy.SensorState(false);
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void setStateLocked(int toggleType, int userId, int sensor, boolean enabled, android.os.Handler callbackHandler, com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback callback) {
        com.android.server.sensorprivacy.SensorState lastState = this.mPersistedState.getState(toggleType, userId, sensor);
        if (lastState == null) {
            if (!enabled) {
                sendSetStateCallback(callbackHandler, callback, false);
                return;
            } else if (enabled) {
                com.android.server.sensorprivacy.SensorState sensorState = new com.android.server.sensorprivacy.SensorState(true);
                this.mPersistedState.setState(toggleType, userId, sensor, sensorState);
                notifyStateChangeLocked(toggleType, userId, sensor, sensorState);
                sendSetStateCallback(callbackHandler, callback, true);
                return;
            }
        }
        if (lastState.setEnabled(enabled)) {
            notifyStateChangeLocked(toggleType, userId, sensor, lastState);
            sendSetStateCallback(callbackHandler, callback, true);
        } else {
            sendSetStateCallback(callbackHandler, callback, false);
        }
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void setStateLocked(int toggleType, int userId, int sensor, int state, android.os.Handler callbackHandler, com.android.server.sensorprivacy.SensorPrivacyStateController.SetStateResultCallback callback) {
        com.android.server.sensorprivacy.SensorState lastState = this.mPersistedState.getState(toggleType, userId, sensor);
        if (lastState == null) {
            if (state == 2) {
                sendSetStateCallback(callbackHandler, callback, false);
                return;
            }
            com.android.server.sensorprivacy.SensorState sensorState = new com.android.server.sensorprivacy.SensorState(state);
            this.mPersistedState.setState(toggleType, userId, sensor, sensorState);
            notifyStateChangeLocked(toggleType, userId, sensor, sensorState);
            sendSetStateCallback(callbackHandler, callback, true);
            return;
        }
        if (lastState.setState(state)) {
            notifyStateChangeLocked(toggleType, userId, sensor, lastState);
            sendSetStateCallback(callbackHandler, callback, true);
        } else {
            sendSetStateCallback(callbackHandler, callback, false);
        }
    }

    private void notifyStateChangeLocked(int toggleType, int userId, int sensor, com.android.server.sensorprivacy.SensorState sensorState) {
        if (this.mListenerHandler != null && this.mListener != null) {
            this.mListenerHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyStateControllerImpl$$ExternalSyntheticLambda1
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                    ((com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyListener) obj).onSensorPrivacyChanged(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), ((java.lang.Integer) obj4).intValue(), (com.android.server.sensorprivacy.SensorState) obj5);
                }
            }, this.mListener, java.lang.Integer.valueOf(toggleType), java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(sensor), new com.android.server.sensorprivacy.SensorState(sensorState)));
        }
        schedulePersistLocked();
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void setSensorPrivacyListenerLocked(android.os.Handler handler, com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyListener listener) {
        java.util.Objects.requireNonNull(handler);
        java.util.Objects.requireNonNull(listener);
        if (this.mListener != null) {
            throw new java.lang.IllegalStateException("Listener is already set");
        }
        this.mListener = listener;
        this.mListenerHandler = handler;
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void schedulePersistLocked() {
        this.mPersistedState.schedulePersist();
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void forEachStateLocked(final com.android.server.sensorprivacy.SensorPrivacyStateController.SensorPrivacyStateConsumer consumer) {
        com.android.server.sensorprivacy.PersistedState persistedState = this.mPersistedState;
        java.util.Objects.requireNonNull(consumer);
        persistedState.forEachKnownState(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.sensorprivacy.SensorPrivacyStateControllerImpl$$ExternalSyntheticLambda0
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                consumer.accept(((java.lang.Integer) obj).intValue(), ((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), (com.android.server.sensorprivacy.SensorState) obj4);
            }
        });
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void resetForTestingImpl() {
        this.mPersistedState.resetForTesting();
        this.mListener = null;
        this.mListenerHandler = null;
        sInstance = null;
    }

    @Override // com.android.server.sensorprivacy.SensorPrivacyStateController
    void dumpLocked(com.android.internal.util.dump.DualDumpOutputStream dumpStream) {
        this.mPersistedState.dump(dumpStream);
    }
}
