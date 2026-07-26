package com.android.server.companion.virtual.camera;

/* JADX INFO: loaded from: classes.dex */
public final class VirtualCameraController implements android.os.IBinder.DeathRecipient {
    private static final java.lang.String TAG = "VirtualCameraController";
    private static final java.lang.String VIRTUAL_CAMERA_SERVICE_NAME = "virtual_camera";
    private final int mCameraPolicy;
    private final java.util.Map<android.os.IBinder, com.android.server.companion.virtual.camera.VirtualCameraController.CameraDescriptor> mCameras;
    private final int mDeviceId;
    private final java.lang.Object mServiceLock;
    private android.companion.virtualcamera.IVirtualCameraService mVirtualCameraService;

    public VirtualCameraController(int cameraPolicy, int deviceId) {
        this(null, cameraPolicy, deviceId);
    }

    VirtualCameraController(android.companion.virtualcamera.IVirtualCameraService virtualCameraService, int cameraPolicy, int deviceId) {
        this.mServiceLock = new java.lang.Object();
        this.mCameras = new android.util.ArrayMap();
        this.mVirtualCameraService = virtualCameraService;
        this.mCameraPolicy = cameraPolicy;
        this.mDeviceId = deviceId;
    }

    public void registerCamera(android.companion.virtual.camera.VirtualCameraConfig cameraConfig, android.content.AttributionSource attributionSource) {
        checkConfigByPolicy(cameraConfig);
        connectVirtualCameraServiceIfNeeded();
        try {
            if (registerCameraWithService(cameraConfig)) {
                com.android.server.companion.virtual.camera.VirtualCameraController.CameraDescriptor cameraDescriptor = new com.android.server.companion.virtual.camera.VirtualCameraController.CameraDescriptor(cameraConfig);
                android.os.IBinder binder = cameraConfig.getCallback().asBinder();
                binder.linkToDeath(cameraDescriptor, 0);
                synchronized (this.mCameras) {
                    this.mCameras.put(binder, cameraDescriptor);
                }
            } else {
                throw new java.lang.RuntimeException("Failed to register virtual camera.");
            }
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
        }
        if (android.companion.virtualdevice.flags.Flags.metricsCollection()) {
            com.android.modules.expresslog.Counter.logIncrementWithUid("virtual_devices.value_virtual_camera_created_count", attributionSource.getUid());
        }
    }

    public void unregisterCamera(android.companion.virtual.camera.VirtualCameraConfig cameraConfig) {
        synchronized (this.mCameras) {
            android.os.IBinder binder = cameraConfig.getCallback().asBinder();
            if (!this.mCameras.containsKey(binder)) {
                android.util.Slog.w(TAG, "Virtual camera was not registered.");
            } else {
                connectVirtualCameraServiceIfNeeded();
                try {
                    synchronized (this.mServiceLock) {
                        this.mVirtualCameraService.unregisterCamera(binder);
                    }
                    this.mCameras.remove(binder);
                } catch (android.os.RemoteException e) {
                    e.rethrowFromSystemServer();
                }
            }
        }
    }

    public java.lang.String getCameraId(android.companion.virtual.camera.VirtualCameraConfig cameraConfig) {
        java.lang.String cameraId;
        connectVirtualCameraServiceIfNeeded();
        try {
            synchronized (this.mServiceLock) {
                cameraId = this.mVirtualCameraService.getCameraId(cameraConfig.getCallback().asBinder());
            }
            return cameraId;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        android.util.Slog.d(TAG, "Virtual camera service died.");
        synchronized (this.mServiceLock) {
            this.mVirtualCameraService = null;
        }
        synchronized (this.mCameras) {
            this.mCameras.clear();
        }
    }

    public void close() {
        synchronized (this.mCameras) {
            if (!this.mCameras.isEmpty()) {
                connectVirtualCameraServiceIfNeeded();
                synchronized (this.mServiceLock) {
                    for (android.os.IBinder binder : this.mCameras.keySet()) {
                        try {
                            this.mVirtualCameraService.unregisterCamera(binder);
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.w(TAG, "close(): Camera failed to be removed on camera service.", e);
                        }
                    }
                }
                this.mCameras.clear();
            }
        }
        synchronized (this.mServiceLock) {
            this.mVirtualCameraService = null;
        }
    }

    public void dump(java.io.PrintWriter fout, java.lang.String indent) {
        fout.println(indent + "VirtualCameraController:");
        java.lang.String indent2 = indent + indent;
        synchronized (this.mCameras) {
            fout.println(indent2 + "Registered cameras: " + this.mCameras.size());
            for (com.android.server.companion.virtual.camera.VirtualCameraController.CameraDescriptor descriptor : this.mCameras.values()) {
                fout.println(indent2 + " token: " + descriptor.mConfig);
            }
        }
    }

    private void checkConfigByPolicy(android.companion.virtual.camera.VirtualCameraConfig config) {
        if (this.mCameraPolicy == 0) {
            throw new java.lang.IllegalArgumentException("Cannot create virtual camera with DEVICE_POLICY_DEFAULT for POLICY_TYPE_CAMERA");
        }
        if (isLensFacingAlreadyPresent(config.getLensFacing())) {
            throw new java.lang.IllegalArgumentException("Only a single virtual camera can be created with lens facing " + config.getLensFacing());
        }
    }

    private boolean isLensFacingAlreadyPresent(int lensFacing) {
        synchronized (this.mCameras) {
            for (com.android.server.companion.virtual.camera.VirtualCameraController.CameraDescriptor cameraDescriptor : this.mCameras.values()) {
                if (cameraDescriptor.mConfig.getLensFacing() == lensFacing) {
                    return true;
                }
            }
            return false;
        }
    }

    private void connectVirtualCameraServiceIfNeeded() {
        synchronized (this.mServiceLock) {
            if (this.mVirtualCameraService == null) {
                connectVirtualCameraService();
            }
            if (this.mVirtualCameraService == null) {
                throw new java.lang.IllegalStateException("Virtual camera service is not connected.");
            }
        }
    }

    private void connectVirtualCameraService() {
        android.os.IBinder virtualCameraBinder;
        long callingId = android.os.Binder.clearCallingIdentity();
        try {
            try {
                virtualCameraBinder = android.os.ServiceManager.waitForService(VIRTUAL_CAMERA_SERVICE_NAME);
            } catch (android.os.RemoteException e) {
                e.rethrowFromSystemServer();
            }
            if (virtualCameraBinder == null) {
                android.util.Slog.e(TAG, "connectVirtualCameraService: Failed to connect to the virtual camera service");
            } else {
                virtualCameraBinder.linkToDeath(this, 0);
                this.mVirtualCameraService = android.companion.virtualcamera.IVirtualCameraService.Stub.asInterface(virtualCameraBinder);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(callingId);
        }
    }

    private boolean registerCameraWithService(android.companion.virtual.camera.VirtualCameraConfig config) throws android.os.RemoteException {
        boolean zRegisterCamera;
        android.companion.virtualcamera.VirtualCameraConfiguration serviceConfiguration = com.android.server.companion.virtual.camera.VirtualCameraConversionUtil.getServiceCameraConfiguration(config);
        synchronized (this.mServiceLock) {
            zRegisterCamera = this.mVirtualCameraService.registerCamera(config.getCallback().asBinder(), serviceConfiguration, this.mDeviceId);
        }
        return zRegisterCamera;
    }

    private final class CameraDescriptor implements android.os.IBinder.DeathRecipient {
        private final android.companion.virtual.camera.VirtualCameraConfig mConfig;

        CameraDescriptor(android.companion.virtual.camera.VirtualCameraConfig config) {
            this.mConfig = config;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Slog.d(com.android.server.companion.virtual.camera.VirtualCameraController.TAG, "Virtual camera binder died");
            com.android.server.companion.virtual.camera.VirtualCameraController.this.unregisterCamera(this.mConfig);
        }
    }
}
