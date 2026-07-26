package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public class FaceServiceRegistry extends com.android.server.biometrics.sensors.BiometricServiceRegistry<com.android.server.biometrics.sensors.face.ServiceProvider, android.hardware.face.FaceSensorPropertiesInternal, android.hardware.face.IFaceAuthenticatorsRegisteredCallback> {
    private static final java.lang.String TAG = "FaceServiceRegistry";
    private final android.hardware.face.IFaceService mService;

    @Override // com.android.server.biometrics.sensors.BiometricServiceRegistry
    protected /* bridge */ /* synthetic */ void invokeRegisteredCallback(android.os.IInterface iInterface, java.util.List list) throws android.os.RemoteException {
        invokeRegisteredCallback((android.hardware.face.IFaceAuthenticatorsRegisteredCallback) iInterface, (java.util.List<android.hardware.face.FaceSensorPropertiesInternal>) list);
    }

    public FaceServiceRegistry(android.hardware.face.IFaceService service, java.util.function.Supplier<android.hardware.biometrics.IBiometricService> biometricSupplier) {
        super(biometricSupplier);
        this.mService = service;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.BiometricServiceRegistry
    public void registerService(android.hardware.biometrics.IBiometricService service, android.hardware.face.FaceSensorPropertiesInternal props) {
        int strength = com.android.server.biometrics.Utils.propertyStrengthToAuthenticatorStrength(props.sensorStrength);
        try {
            service.registerAuthenticator(props.sensorId, 8, strength, new com.android.server.biometrics.sensors.face.FaceAuthenticator(this.mService, props.sensorId));
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception when registering sensorId: " + props.sensorId);
        }
    }

    protected void invokeRegisteredCallback(android.hardware.face.IFaceAuthenticatorsRegisteredCallback callback, java.util.List<android.hardware.face.FaceSensorPropertiesInternal> allProps) throws android.os.RemoteException {
        callback.onAllAuthenticatorsRegistered(allProps);
    }
}
