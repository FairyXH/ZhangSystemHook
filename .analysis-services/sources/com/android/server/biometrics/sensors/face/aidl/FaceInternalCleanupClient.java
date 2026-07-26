package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public class FaceInternalCleanupClient extends com.android.server.biometrics.sensors.InternalCleanupClient<android.hardware.face.Face, com.android.server.biometrics.sensors.face.aidl.AidlSession> {
    public FaceInternalCleanupClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, int userId, java.lang.String owner, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, com.android.server.biometrics.sensors.BiometricUtils<android.hardware.face.Face> utils, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        super(context, lazyDaemon, userId, owner, sensorId, logger, biometricContext, utils, authenticatorIds);
    }

    @Override // com.android.server.biometrics.sensors.InternalCleanupClient
    protected com.android.server.biometrics.sensors.InternalEnumerateClient<com.android.server.biometrics.sensors.face.aidl.AidlSession> getEnumerateClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.os.IBinder token, int userId, java.lang.String owner, java.util.List<android.hardware.face.Face> enrolledList, com.android.server.biometrics.sensors.BiometricUtils<android.hardware.face.Face> utils, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext) {
        return new com.android.server.biometrics.sensors.face.aidl.FaceInternalEnumerateClient(context, lazyDaemon, token, userId, owner, enrolledList, utils, sensorId, logger, biometricContext);
    }

    @Override // com.android.server.biometrics.sensors.InternalCleanupClient
    protected com.android.server.biometrics.sensors.RemovalClient<android.hardware.face.Face, com.android.server.biometrics.sensors.face.aidl.AidlSession> getRemovalClient(android.content.Context context, java.util.function.Supplier<com.android.server.biometrics.sensors.face.aidl.AidlSession> lazyDaemon, android.os.IBinder token, int biometricId, int userId, java.lang.String owner, com.android.server.biometrics.sensors.BiometricUtils<android.hardware.face.Face> utils, int sensorId, com.android.server.biometrics.log.BiometricLogger logger, com.android.server.biometrics.log.BiometricContext biometricContext, java.util.Map<java.lang.Integer, java.lang.Long> authenticatorIds) {
        return new com.android.server.biometrics.sensors.face.aidl.FaceRemovalClient(context, lazyDaemon, token, null, new int[]{biometricId}, userId, owner, utils, sensorId, logger, biometricContext, authenticatorIds);
    }

    @Override // com.android.server.biometrics.sensors.InternalCleanupClient
    protected void onAddUnknownTemplate(int userId, android.hardware.biometrics.BiometricAuthenticator.Identifier identifier) {
        com.android.server.biometrics.sensors.face.FaceUtils.getInstance(getSensorId()).addBiometricForUser(getContext(), getTargetUserId(), (android.hardware.face.Face) identifier);
    }
}
