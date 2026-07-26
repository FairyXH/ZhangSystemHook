package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class SensorList<T> {
    private static final java.lang.String TAG = "SensorList";
    private final android.app.IActivityManager mActivityManager;
    private final android.util.SparseArray<T> mSensors = new android.util.SparseArray<>();

    public SensorList(android.app.IActivityManager activityManager) {
        this.mActivityManager = activityManager;
    }

    public void addSensor(int sensorId, T sensor, int sessionUserId, android.app.SynchronousUserSwitchObserver userSwitchObserver) {
        this.mSensors.put(sensorId, sensor);
        registerUserSwitchObserver(sessionUserId, userSwitchObserver);
    }

    private void registerUserSwitchObserver(int sessionUserId, android.app.SynchronousUserSwitchObserver userSwitchObserver) {
        try {
            this.mActivityManager.registerUserSwitchObserver(userSwitchObserver, TAG);
            if (sessionUserId == -10000) {
                userSwitchObserver.onUserSwitching(0);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to register user switch observer");
        }
    }

    public T valueAt(int position) {
        return this.mSensors.valueAt(position);
    }

    public T get(int sensorId) {
        return this.mSensors.get(sensorId);
    }

    public int keyAt(int position) {
        return this.mSensors.keyAt(position);
    }

    public int size() {
        return this.mSensors.size();
    }

    public boolean contains(int sensorId) {
        return this.mSensors.contains(sensorId);
    }
}
