package com.android.server.sensorprivacy;

/* JADX INFO: loaded from: classes3.dex */
class SensorState {
    private long mLastChange;
    private int mStateType;

    SensorState(int stateType) {
        this.mStateType = stateType;
        this.mLastChange = com.android.server.sensorprivacy.SensorPrivacyService.getCurrentTimeMillis();
    }

    SensorState(int stateType, long lastChange) {
        this.mStateType = stateType;
        this.mLastChange = java.lang.Math.min(com.android.server.sensorprivacy.SensorPrivacyService.getCurrentTimeMillis(), lastChange);
    }

    SensorState(com.android.server.sensorprivacy.SensorState sensorState) {
        this.mStateType = sensorState.getState();
        this.mLastChange = sensorState.getLastChange();
    }

    boolean setState(int stateType) {
        if (this.mStateType != stateType) {
            this.mStateType = stateType;
            this.mLastChange = com.android.server.sensorprivacy.SensorPrivacyService.getCurrentTimeMillis();
            return true;
        }
        return false;
    }

    int getState() {
        return this.mStateType;
    }

    long getLastChange() {
        return this.mLastChange;
    }

    private static int enabledToState(boolean enabled) {
        return enabled ? 1 : 2;
    }

    SensorState(boolean enabled) {
        this(enabledToState(enabled));
    }

    boolean setEnabled(boolean enabled) {
        return setState(enabledToState(enabled));
    }

    boolean isEnabled() {
        return getState() == 1;
    }
}
