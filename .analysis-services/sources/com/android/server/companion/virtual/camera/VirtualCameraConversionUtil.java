package com.android.server.companion.virtual.camera;

/* JADX INFO: loaded from: classes.dex */
public final class VirtualCameraConversionUtil {
    public static android.companion.virtualcamera.VirtualCameraConfiguration getServiceCameraConfiguration(android.companion.virtual.camera.VirtualCameraConfig cameraConfig) throws android.os.RemoteException {
        android.companion.virtualcamera.VirtualCameraConfiguration serviceConfiguration = new android.companion.virtualcamera.VirtualCameraConfiguration();
        serviceConfiguration.supportedStreamConfigs = (android.companion.virtualcamera.SupportedStreamConfiguration[]) cameraConfig.getStreamConfigs().stream().map(new java.util.function.Function() { // from class: com.android.server.companion.virtual.camera.VirtualCameraConversionUtil$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.companion.virtual.camera.VirtualCameraConversionUtil.convertSupportedStreamConfiguration((android.companion.virtual.camera.VirtualCameraStreamConfig) obj);
            }
        }).toArray(new java.util.function.IntFunction() { // from class: com.android.server.companion.virtual.camera.VirtualCameraConversionUtil$$ExternalSyntheticLambda1
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.companion.virtual.camera.VirtualCameraConversionUtil.lambda$getServiceCameraConfiguration$0(i);
            }
        });
        serviceConfiguration.sensorOrientation = cameraConfig.getSensorOrientation();
        serviceConfiguration.lensFacing = cameraConfig.getLensFacing();
        serviceConfiguration.virtualCameraCallback = convertCallback(cameraConfig.getCallback());
        return serviceConfiguration;
    }

    static /* synthetic */ android.companion.virtualcamera.SupportedStreamConfiguration[] lambda$getServiceCameraConfiguration$0(int x$0) {
        return new android.companion.virtualcamera.SupportedStreamConfiguration[x$0];
    }

    private static android.companion.virtualcamera.IVirtualCameraCallback convertCallback(final android.companion.virtual.camera.IVirtualCameraCallback camera) {
        return new android.companion.virtualcamera.IVirtualCameraCallback.Stub() { // from class: com.android.server.companion.virtual.camera.VirtualCameraConversionUtil.1
            @Override // android.companion.virtualcamera.IVirtualCameraCallback
            public void onStreamConfigured(int streamId, android.view.Surface surface, int width, int height, int format) throws android.os.RemoteException {
                camera.onStreamConfigured(streamId, surface, width, height, com.android.server.companion.virtual.camera.VirtualCameraConversionUtil.convertToJavaFormat(format));
            }

            @Override // android.companion.virtualcamera.IVirtualCameraCallback
            public void onProcessCaptureRequest(int streamId, int frameId) throws android.os.RemoteException {
                camera.onProcessCaptureRequest(streamId, frameId);
            }

            @Override // android.companion.virtualcamera.IVirtualCameraCallback
            public void onStreamClosed(int streamId) throws android.os.RemoteException {
                camera.onStreamClosed(streamId);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.companion.virtualcamera.SupportedStreamConfiguration convertSupportedStreamConfiguration(android.companion.virtual.camera.VirtualCameraStreamConfig stream) {
        android.companion.virtualcamera.SupportedStreamConfiguration supportedConfig = new android.companion.virtualcamera.SupportedStreamConfiguration();
        supportedConfig.height = stream.getHeight();
        supportedConfig.width = stream.getWidth();
        supportedConfig.pixelFormat = convertToHalFormat(stream.getFormat());
        supportedConfig.maxFps = stream.getMaximumFramesPerSecond();
        return supportedConfig;
    }

    private static int convertToHalFormat(int javaFormat) {
        switch (javaFormat) {
            case 1:
                return 1;
            case 35:
                return 35;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int convertToJavaFormat(int halFormat) {
        switch (halFormat) {
            case 1:
                return 1;
            case 35:
                return 35;
            default:
                return 0;
        }
    }

    private VirtualCameraConversionUtil() {
    }
}
