package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public final class SensorOverlays {
    private static final java.lang.String TAG = "SensorOverlays";
    private static com.android.server.biometrics.sensors.fingerprint.IUdfpsHelperExt mUdfpsHelperExt = (com.android.server.biometrics.sensors.fingerprint.IUdfpsHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.IUdfpsHelperExt.class).create();
    private java.util.Optional<android.hardware.fingerprint.IUdfpsOverlayController> mUdfpsOverlayController;

    @java.lang.FunctionalInterface
    public interface OverlayControllerConsumer<T> {
        void accept(T t) throws android.os.RemoteException;
    }

    public SensorOverlays(android.hardware.fingerprint.IUdfpsOverlayController udfpsOverlayController) {
        this.mUdfpsOverlayController = java.util.Optional.ofNullable(udfpsOverlayController);
    }

    public void show(int sensorId, int reason, final com.android.server.biometrics.sensors.AcquisitionClient<?> client) {
        if (this.mUdfpsOverlayController.isPresent()) {
            android.hardware.fingerprint.IUdfpsOverlayControllerCallback.Stub stub = new android.hardware.fingerprint.IUdfpsOverlayControllerCallback.Stub() { // from class: com.android.server.biometrics.sensors.SensorOverlays.1
                public void onUserCanceled() {
                    client.onUserCanceled();
                }
            };
            try {
                mUdfpsHelperExt.preShowUdfpsOverlay(sensorId, reason);
                this.mUdfpsOverlayController.get().showUdfpsOverlay(client.getRequestId(), sensorId, reason, stub);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception when showing the UDFPS overlay", e);
            }
        }
    }

    public void hide(int sensorId) {
        if (this.mUdfpsOverlayController.isPresent()) {
            try {
                mUdfpsHelperExt.preHideUdfpsOverlay(sensorId);
                this.mUdfpsOverlayController.get().hideUdfpsOverlay(sensorId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception when hiding the UDFPS overlay", e);
            }
        }
    }

    public void ifUdfps(com.android.server.biometrics.sensors.SensorOverlays.OverlayControllerConsumer<android.hardware.fingerprint.IUdfpsOverlayController> consumer) {
        if (this.mUdfpsOverlayController.isPresent()) {
            try {
                consumer.accept(this.mUdfpsOverlayController.get());
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote exception using overlay controller", e);
            }
        }
    }

    public void setUdfpsOverlayController(android.hardware.fingerprint.IUdfpsOverlayController controller) {
        this.mUdfpsOverlayController = java.util.Optional.ofNullable(controller);
    }

    public void setContext(android.content.Context context) {
        mUdfpsHelperExt.init(context);
    }
}
