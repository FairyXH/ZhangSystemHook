package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintInternalCleanupClient extends com.android.server.biometrics.sensors.InternalCleanupClient<android.hardware.fingerprint.Fingerprint, com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> {
    public FingerprintInternalCleanupClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.fingerprint.FingerprintUtils utils, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        super(context, lazyDaemon, userId, owner, sensorId, logger, biometricContext, utils, authenticatorIds);
    }

    @Override // com.android.server.biometrics.sensors.InternalCleanupClient
    protected com.android.server.biometrics.sensors.InternalEnumerateClient<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> getEnumerateClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, android.os.IBinder token, int userId, java.lang.String owner, java.util.List<android.hardware.fingerprint.Fingerprint> enrolledList, com.android.server.biometrics.sensors.BiometricUtils<android.hardware.fingerprint.Fingerprint> utils, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext) {
        return new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintInternalEnumerateClient(context, lazyDaemon, token, userId, owner, enrolledList, utils, sensorId, logger.swapAction(context, 3), biometricContext);
    }

    @Override // com.android.server.biometrics.sensors.InternalCleanupClient
    protected com.android.server.biometrics.sensors.RemovalClient<android.hardware.fingerprint.Fingerprint, com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> getRemovalClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.fingerprint.aidl.AidlSession> lazyDaemon, android.os.IBinder token, int biometricId, int userId, java.lang.String owner, com.android.server.biometrics.sensors.BiometricUtils<android.hardware.fingerprint.Fingerprint> utils, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        return new com.android.server.biometrics.sensors.fingerprint.aidl.FingerprintRemovalClient(context, lazyDaemon, token, null, new int[]{biometricId}, userId, owner, utils, sensorId, logger.swapAction(context, 4), biometricContext, authenticatorIds);
    }

    @Override // com.android.server.biometrics.sensors.InternalCleanupClient
    protected void onAddUnknownTemplate(int userId, android.hardware.biometrics.BiometricAuthenticator.Identifier identifier) {
        com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.getInstance(getSensorId()).addBiometricForUser(getContext(), getTargetUserId(), (android.hardware.fingerprint.Fingerprint) identifier);
    }
}
