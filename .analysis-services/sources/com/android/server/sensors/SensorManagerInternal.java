package com.android.server.sensors;

/* JADX INFO: loaded from: classes3.dex */
public abstract class SensorManagerInternal {

    public interface ProximityActiveListener {
        void onProximityActive(boolean z);
    }

    public interface RuntimeSensorCallback {
        int onConfigurationChanged(int i, boolean z, int i2, int i3);

        int onDirectChannelConfigured(int i, int i2, int i3);

        int onDirectChannelCreated(android.os.ParcelFileDescriptor parcelFileDescriptor);

        void onDirectChannelDestroyed(int i);
    }

    public abstract void addProximityActiveListener(java.util.concurrent.Executor executor, com.android.server.sensors.SensorManagerInternal.ProximityActiveListener proximityActiveListener);

    public abstract int createRuntimeSensor(int i, int i2, java.lang.String str, java.lang.String str2, float f, float f2, float f3, int i3, int i4, int i5, com.android.server.sensors.SensorManagerInternal.RuntimeSensorCallback runtimeSensorCallback);

    public abstract void notifyApplicationLaunchStage(java.lang.String str, int i, int i2, int i3);

    public abstract void notifyProxWakeLockAcquired(java.lang.String str);

    public abstract void notifyProxWakeLockReleased(java.lang.String str);

    public abstract void removeProximityActiveListener(com.android.server.sensors.SensorManagerInternal.ProximityActiveListener proximityActiveListener);

    public abstract void removeRuntimeSensor(int i);

    public abstract boolean sendSensorEvent(int i, int i2, long j, float[] fArr);
}
