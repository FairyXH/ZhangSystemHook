package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public class GestureAvailabilityDispatcher {
    private static final java.lang.String TAG = "GestureAvailabilityTracker";
    private boolean mIsActive;
    private final java.util.concurrent.CopyOnWriteArrayList<android.hardware.fingerprint.IFingerprintClientActiveCallback> mClientActiveCallbacks = new java.util.concurrent.CopyOnWriteArrayList<>();
    private final java.util.Map<java.lang.Integer, java.lang.Boolean> mActiveSensors = new java.util.HashMap();

    GestureAvailabilityDispatcher() {
    }

    public boolean isAnySensorActive() {
        return this.mIsActive;
    }

    public void markSensorActive(int sensorId, boolean active) {
        this.mActiveSensors.put(java.lang.Integer.valueOf(sensorId), java.lang.Boolean.valueOf(active));
        boolean wasActive = this.mIsActive;
        boolean isActive = false;
        java.util.Iterator<java.lang.Boolean> it = this.mActiveSensors.values().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            java.lang.Boolean b = it.next();
            if (b.booleanValue()) {
                isActive = true;
                break;
            }
        }
        if (wasActive != isActive) {
            android.util.Slog.d(TAG, "Notifying gesture availability, active=" + this.mIsActive);
            this.mIsActive = isActive;
            notifyClientActiveCallbacks(this.mIsActive);
        }
    }

    void registerCallback(android.hardware.fingerprint.IFingerprintClientActiveCallback callback) {
        this.mClientActiveCallbacks.add(callback);
    }

    void removeCallback(android.hardware.fingerprint.IFingerprintClientActiveCallback callback) {
        this.mClientActiveCallbacks.remove(callback);
    }

    private void notifyClientActiveCallbacks(boolean isActive) {
        for (android.hardware.fingerprint.IFingerprintClientActiveCallback callback : this.mClientActiveCallbacks) {
            try {
                callback.onClientActiveChanged(isActive);
            } catch (android.os.RemoteException e) {
                this.mClientActiveCallbacks.remove(callback);
            }
        }
    }
}
