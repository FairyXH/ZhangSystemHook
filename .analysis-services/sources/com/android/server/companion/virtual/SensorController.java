package com.android.server.companion.virtual;

/* JADX INFO: loaded from: classes.dex */
public class SensorController {
    private static final int BAD_VALUE = -22;
    private static final int OK = 0;
    private static final java.lang.String TAG = "SensorController";
    private static final int UNKNOWN_ERROR = Integer.MIN_VALUE;
    private static java.util.concurrent.atomic.AtomicInteger sNextDirectChannelHandle = new java.util.concurrent.atomic.AtomicInteger(1);
    private final android.content.AttributionSource mAttributionSource;
    private final com.android.server.sensors.SensorManagerInternal.RuntimeSensorCallback mRuntimeSensorCallback;
    private final int mVirtualDeviceId;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.companion.virtual.SensorController.SensorDescriptor> mSensorDescriptors = new android.util.ArrayMap<>();
    private android.util.SparseArray<android.companion.virtual.sensor.VirtualSensor> mVirtualSensors = new android.util.SparseArray<>();
    private java.util.List<android.companion.virtual.sensor.VirtualSensor> mVirtualSensorList = null;
    private final com.android.server.sensors.SensorManagerInternal mSensorManagerInternal = (com.android.server.sensors.SensorManagerInternal) com.android.server.LocalServices.getService(com.android.server.sensors.SensorManagerInternal.class);
    private final com.android.server.companion.virtual.VirtualDeviceManagerInternal mVdmInternal = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);

    public SensorController(android.companion.virtual.IVirtualDevice virtualDevice, int virtualDeviceId, android.content.AttributionSource attributionSource, android.companion.virtual.sensor.IVirtualSensorCallback virtualSensorCallback, java.util.List<android.companion.virtual.sensor.VirtualSensorConfig> sensors) {
        this.mVirtualDeviceId = virtualDeviceId;
        this.mAttributionSource = attributionSource;
        this.mRuntimeSensorCallback = new com.android.server.companion.virtual.SensorController.RuntimeSensorCallbackWrapper(virtualSensorCallback);
        createSensors(virtualDevice, sensors);
    }

    void close() {
        synchronized (this.mLock) {
            this.mSensorDescriptors.values().forEach(new java.util.function.Consumer() { // from class: com.android.server.companion.virtual.SensorController$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$close$0((com.android.server.companion.virtual.SensorController.SensorDescriptor) obj);
                }
            });
            this.mSensorDescriptors.clear();
            this.mVirtualSensors.clear();
            this.mVirtualSensorList = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$close$0(com.android.server.companion.virtual.SensorController.SensorDescriptor descriptor) {
        this.mSensorManagerInternal.removeRuntimeSensor(descriptor.mHandle);
    }

    private void createSensors(android.companion.virtual.IVirtualDevice virtualDevice, java.util.List<android.companion.virtual.sensor.VirtualSensorConfig> configs) {
        java.util.Objects.requireNonNull(virtualDevice);
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                for (android.companion.virtual.sensor.VirtualSensorConfig config : configs) {
                    createSensorInternal(virtualDevice, config);
                }
            } catch (com.android.server.companion.virtual.SensorController.SensorCreationException e) {
                throw new java.lang.RuntimeException("Failed to create virtual sensor", e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void createSensorInternal(android.companion.virtual.IVirtualDevice virtualDevice, android.companion.virtual.sensor.VirtualSensorConfig config) throws com.android.server.companion.virtual.SensorController.SensorCreationException {
        java.util.Objects.requireNonNull(config);
        if (config.getType() <= 0) {
            throw new com.android.server.companion.virtual.SensorController.SensorCreationException("Received an invalid virtual sensor type (config name '" + config.getName() + "').");
        }
        int handle = this.mSensorManagerInternal.createRuntimeSensor(this.mVirtualDeviceId, config.getType(), config.getName(), config.getVendor() == null ? "" : config.getVendor(), config.getMaximumRange(), config.getResolution(), config.getPower(), config.getMinDelay(), config.getMaxDelay(), config.getFlags(), this.mRuntimeSensorCallback);
        if (handle <= 0) {
            throw new com.android.server.companion.virtual.SensorController.SensorCreationException("Received an invalid virtual sensor handle '" + config.getName() + "'.");
        }
        com.android.server.companion.virtual.SensorController.SensorDescriptor sensorDescriptor = new com.android.server.companion.virtual.SensorController.SensorDescriptor(handle, config.getType(), config.getName());
        android.os.IBinder sensorToken = new android.os.Binder("android.hardware.sensor.VirtualSensor:" + config.getName());
        android.companion.virtual.sensor.VirtualSensor sensor = new android.companion.virtual.sensor.VirtualSensor(handle, config.getType(), config.getName(), virtualDevice, sensorToken);
        synchronized (this.mLock) {
            this.mSensorDescriptors.put(sensorToken, sensorDescriptor);
            this.mVirtualSensors.put(handle, sensor);
        }
        if (android.companion.virtualdevice.flags.Flags.metricsCollection()) {
            com.android.modules.expresslog.Counter.logIncrementWithUid("virtual_devices.value_virtual_sensors_created_count", this.mAttributionSource.getUid());
        }
    }

    boolean sendSensorEvent(android.os.IBinder token, android.companion.virtual.sensor.VirtualSensorEvent event) {
        boolean zSendSensorEvent;
        java.util.Objects.requireNonNull(token);
        java.util.Objects.requireNonNull(event);
        synchronized (this.mLock) {
            com.android.server.companion.virtual.SensorController.SensorDescriptor sensorDescriptor = this.mSensorDescriptors.get(token);
            if (sensorDescriptor == null) {
                throw new java.lang.IllegalArgumentException("Could not send sensor event for given token");
            }
            zSendSensorEvent = this.mSensorManagerInternal.sendSensorEvent(sensorDescriptor.getHandle(), sensorDescriptor.getType(), event.getTimestampNanos(), event.getValues());
        }
        return zSendSensorEvent;
    }

    android.companion.virtual.sensor.VirtualSensor getSensorByHandle(int handle) {
        android.companion.virtual.sensor.VirtualSensor virtualSensor;
        synchronized (this.mLock) {
            virtualSensor = this.mVirtualSensors.get(handle);
        }
        return virtualSensor;
    }

    java.util.List<android.companion.virtual.sensor.VirtualSensor> getSensorList() {
        java.util.List<android.companion.virtual.sensor.VirtualSensor> list;
        synchronized (this.mLock) {
            if (this.mVirtualSensorList == null) {
                this.mVirtualSensorList = new java.util.ArrayList(this.mVirtualSensors.size());
                for (int i = 0; i < this.mVirtualSensors.size(); i++) {
                    this.mVirtualSensorList.add(this.mVirtualSensors.valueAt(i));
                }
                this.mVirtualSensorList = java.util.Collections.unmodifiableList(this.mVirtualSensorList);
            }
            list = this.mVirtualSensorList;
        }
        return list;
    }

    void dump(java.io.PrintWriter fout) {
        fout.println("    SensorController: ");
        synchronized (this.mLock) {
            fout.println("      Active descriptors: ");
            for (com.android.server.companion.virtual.SensorController.SensorDescriptor sensorDescriptor : this.mSensorDescriptors.values()) {
                fout.println("        handle: " + sensorDescriptor.getHandle());
                fout.println("          type: " + sensorDescriptor.getType());
                fout.println("          name: " + sensorDescriptor.getName());
            }
        }
    }

    void addSensorForTesting(android.os.IBinder deviceToken, int handle, int type, java.lang.String name) {
        synchronized (this.mLock) {
            this.mSensorDescriptors.put(deviceToken, new com.android.server.companion.virtual.SensorController.SensorDescriptor(handle, type, name));
        }
    }

    java.util.Map<android.os.IBinder, com.android.server.companion.virtual.SensorController.SensorDescriptor> getSensorDescriptors() {
        android.util.ArrayMap arrayMap;
        synchronized (this.mLock) {
            arrayMap = new android.util.ArrayMap(this.mSensorDescriptors);
        }
        return arrayMap;
    }

    private final class RuntimeSensorCallbackWrapper implements com.android.server.sensors.SensorManagerInternal.RuntimeSensorCallback {
        private android.companion.virtual.sensor.IVirtualSensorCallback mCallback;

        RuntimeSensorCallbackWrapper(android.companion.virtual.sensor.IVirtualSensorCallback callback) {
            this.mCallback = callback;
        }

        @Override // com.android.server.sensors.SensorManagerInternal.RuntimeSensorCallback
        public int onConfigurationChanged(int handle, boolean enabled, int samplingPeriodMicros, int batchReportLatencyMicros) {
            if (this.mCallback == null) {
                android.util.Slog.e(com.android.server.companion.virtual.SensorController.TAG, "No sensor callback configured for sensor handle " + handle);
                return -22;
            }
            android.companion.virtual.sensor.VirtualSensor sensor = com.android.server.companion.virtual.SensorController.this.mVdmInternal.getVirtualSensor(com.android.server.companion.virtual.SensorController.this.mVirtualDeviceId, handle);
            if (sensor == null) {
                android.util.Slog.e(com.android.server.companion.virtual.SensorController.TAG, "No sensor found for deviceId=" + com.android.server.companion.virtual.SensorController.this.mVirtualDeviceId + " and sensor handle=" + handle);
                return -22;
            }
            try {
                this.mCallback.onConfigurationChanged(sensor, enabled, samplingPeriodMicros, batchReportLatencyMicros);
                return 0;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.companion.virtual.SensorController.TAG, "Failed to call sensor callback: " + e);
                return Integer.MIN_VALUE;
            }
        }

        @Override // com.android.server.sensors.SensorManagerInternal.RuntimeSensorCallback
        public int onDirectChannelCreated(android.os.ParcelFileDescriptor fd) {
            if (this.mCallback == null) {
                android.util.Slog.e(com.android.server.companion.virtual.SensorController.TAG, "No sensor callback for virtual deviceId " + com.android.server.companion.virtual.SensorController.this.mVirtualDeviceId);
                return -22;
            }
            if (fd == null) {
                android.util.Slog.e(com.android.server.companion.virtual.SensorController.TAG, "Received invalid ParcelFileDescriptor");
                return -22;
            }
            int channelHandle = com.android.server.companion.virtual.SensorController.sNextDirectChannelHandle.getAndIncrement();
            android.os.SharedMemory sharedMemory = android.os.SharedMemory.fromFileDescriptor(fd);
            try {
                this.mCallback.onDirectChannelCreated(channelHandle, sharedMemory);
                return channelHandle;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.companion.virtual.SensorController.TAG, "Failed to call sensor callback: " + e);
                return Integer.MIN_VALUE;
            }
        }

        @Override // com.android.server.sensors.SensorManagerInternal.RuntimeSensorCallback
        public void onDirectChannelDestroyed(int channelHandle) {
            if (this.mCallback == null) {
                android.util.Slog.e(com.android.server.companion.virtual.SensorController.TAG, "No sensor callback for virtual deviceId " + com.android.server.companion.virtual.SensorController.this.mVirtualDeviceId);
                return;
            }
            try {
                this.mCallback.onDirectChannelDestroyed(channelHandle);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.companion.virtual.SensorController.TAG, "Failed to call sensor callback: " + e);
            }
        }

        @Override // com.android.server.sensors.SensorManagerInternal.RuntimeSensorCallback
        public int onDirectChannelConfigured(int channelHandle, int sensorHandle, int rateLevel) {
            if (this.mCallback == null) {
                android.util.Slog.e(com.android.server.companion.virtual.SensorController.TAG, "No runtime sensor callback configured.");
                return -22;
            }
            android.companion.virtual.sensor.VirtualSensor sensor = com.android.server.companion.virtual.SensorController.this.mVdmInternal.getVirtualSensor(com.android.server.companion.virtual.SensorController.this.mVirtualDeviceId, sensorHandle);
            if (sensor == null) {
                android.util.Slog.e(com.android.server.companion.virtual.SensorController.TAG, "No sensor found for deviceId=" + com.android.server.companion.virtual.SensorController.this.mVirtualDeviceId + " and sensor handle=" + sensorHandle);
                return -22;
            }
            try {
                this.mCallback.onDirectChannelConfigured(channelHandle, sensor, rateLevel, sensorHandle);
                if (rateLevel == 0) {
                    return 0;
                }
                return sensorHandle;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.companion.virtual.SensorController.TAG, "Failed to call sensor callback: " + e);
                return Integer.MIN_VALUE;
            }
        }
    }

    static final class SensorDescriptor {
        private final int mHandle;
        private final java.lang.String mName;
        private final int mType;

        SensorDescriptor(int handle, int type, java.lang.String name) {
            this.mHandle = handle;
            this.mType = type;
            this.mName = name;
        }

        public int getHandle() {
            return this.mHandle;
        }

        public int getType() {
            return this.mType;
        }

        public java.lang.String getName() {
            return this.mName;
        }
    }

    private static class SensorCreationException extends java.lang.Exception {
        SensorCreationException(java.lang.String message) {
            super(message);
        }
    }
}
