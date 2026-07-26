package com.android.server.biometrics.sensors.iris;

/* JADX INFO: loaded from: classes.dex */
public class IrisService extends com.android.server.SystemService {
    private static final java.lang.String TAG = "IrisService";
    private final com.android.server.biometrics.sensors.iris.IrisService.IrisServiceWrapper mServiceWrapper;

    /* JADX INFO: Access modifiers changed from: private */
    final class IrisServiceWrapper extends android.hardware.iris.IIrisService.Stub {
        private IrisServiceWrapper() {
        }

        public void registerAuthenticators(final java.util.List<android.hardware.biometrics.SensorPropertiesInternal> hidlSensors) {
            super.registerAuthenticators_enforcePermission();
            com.android.server.ServiceThread thread = new com.android.server.ServiceThread(com.android.server.biometrics.sensors.iris.IrisService.TAG, 10, true);
            thread.start();
            android.os.Handler handler = new android.os.Handler(thread.getLooper());
            handler.post(new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.iris.IrisService$IrisServiceWrapper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$registerAuthenticators$0(hidlSensors);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$registerAuthenticators$0(java.util.List hidlSensors) {
            android.hardware.biometrics.IBiometricService biometricService = android.hardware.biometrics.IBiometricService.Stub.asInterface(android.os.ServiceManager.getService("biometric"));
            java.util.Iterator it = hidlSensors.iterator();
            while (it.hasNext()) {
                android.hardware.biometrics.SensorPropertiesInternal hidlSensor = (android.hardware.biometrics.SensorPropertiesInternal) it.next();
                int sensorId = hidlSensor.sensorId;
                int strength = com.android.server.biometrics.Utils.propertyStrengthToAuthenticatorStrength(hidlSensor.sensorStrength);
                com.android.server.biometrics.sensors.iris.IrisAuthenticator authenticator = new com.android.server.biometrics.sensors.iris.IrisAuthenticator(com.android.server.biometrics.sensors.iris.IrisService.this.mServiceWrapper, sensorId);
                try {
                    biometricService.registerAuthenticator(sensorId, 4, strength, authenticator);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.biometrics.sensors.iris.IrisService.TAG, "Remote exception when registering sensorId: " + sensorId);
                }
            }
        }
    }

    public IrisService(android.content.Context context) {
        super(context);
        this.mServiceWrapper = new com.android.server.biometrics.sensors.iris.IrisService.IrisServiceWrapper();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("iris", this.mServiceWrapper);
    }
}
