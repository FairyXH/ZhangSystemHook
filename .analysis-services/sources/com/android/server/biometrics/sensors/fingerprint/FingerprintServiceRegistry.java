package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintServiceRegistry extends com.android.server.biometrics.sensors.BiometricServiceRegistry<com.android.server.biometrics.sensors.fingerprint.ServiceProvider, android.hardware.fingerprint.FingerprintSensorPropertiesInternal, android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback> {
    private static final java.lang.String TAG = "FingerprintServiceRegistry";
    private final android.hardware.fingerprint.IFingerprintService mService;

    @Override // com.android.server.biometrics.sensors.BiometricServiceRegistry
    protected /* bridge */ /* synthetic */ void invokeRegisteredCallback(android.os.IInterface iInterface, java.util.List list) throws android.os.RemoteException {
        invokeRegisteredCallback((android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback) iInterface, (java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal>) list);
    }

    public FingerprintServiceRegistry(android.hardware.fingerprint.IFingerprintService service, java.util.function.Supplier<android.hardware.biometrics.IBiometricService> biometricSupplier) {
        super(biometricSupplier);
        this.mService = service;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.biometrics.sensors.BiometricServiceRegistry
    public void registerService(android.hardware.biometrics.IBiometricService service, android.hardware.fingerprint.FingerprintSensorPropertiesInternal props) {
        int strength = com.android.server.biometrics.Utils.propertyStrengthToAuthenticatorStrength(props.sensorStrength);
        try {
            service.registerAuthenticator(props.sensorId, 2, strength, new com.android.server.biometrics.sensors.fingerprint.FingerprintAuthenticator(this.mService, props.sensorId));
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote exception when registering sensorId: " + props.sensorId);
        }
    }

    protected void invokeRegisteredCallback(android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback callback, java.util.List<android.hardware.fingerprint.FingerprintSensorPropertiesInternal> allProps) throws android.os.RemoteException {
        callback.onAllAuthenticatorsRegistered(allProps);
    }
}
