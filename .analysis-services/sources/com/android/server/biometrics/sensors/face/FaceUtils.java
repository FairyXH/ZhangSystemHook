package com.android.server.biometrics.sensors.face;

/* JADX INFO: loaded from: classes.dex */
public class FaceUtils implements com.android.server.biometrics.sensors.BiometricUtils<android.hardware.face.Face> {
    private static final java.lang.String LEGACY_FACE_FILE = "settings_face.xml";
    private static final java.lang.Object sInstanceLock = new java.lang.Object();
    private static android.util.SparseArray<com.android.server.biometrics.sensors.face.FaceUtils> sInstances;
    private final java.lang.String mFileName;
    private final android.util.SparseArray<com.android.server.biometrics.sensors.face.FaceUserState> mUserStates = new android.util.SparseArray<>();

    public static com.android.server.biometrics.sensors.face.FaceUtils getInstance(int sensorId) {
        return getInstance(sensorId, LEGACY_FACE_FILE);
    }

    private static com.android.server.biometrics.sensors.face.FaceUtils getInstance(int sensorId, java.lang.String fileName) {
        com.android.server.biometrics.sensors.face.FaceUtils utils;
        synchronized (sInstanceLock) {
            if (sInstances == null) {
                sInstances = new android.util.SparseArray<>();
            }
            if (sInstances.get(sensorId) == null) {
                if (fileName == null) {
                    fileName = "settings_face_" + sensorId + ".xml";
                }
                sInstances.put(sensorId, new com.android.server.biometrics.sensors.face.FaceUtils(fileName));
            }
            utils = sInstances.get(sensorId);
        }
        return utils;
    }

    public static com.android.server.biometrics.sensors.face.FaceUtils getLegacyInstance(int sensorId) {
        return getInstance(sensorId, LEGACY_FACE_FILE);
    }

    private FaceUtils(java.lang.String fileName) {
        this.mFileName = fileName;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public java.util.List<android.hardware.face.Face> getBiometricsForUser(android.content.Context ctx, int userId) {
        return getStateForUser(ctx, userId).getBiometrics();
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void addBiometricForUser(android.content.Context ctx, int userId, android.hardware.face.Face face) {
        getStateForUser(ctx, userId).addBiometric(face);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void removeBiometricForUser(android.content.Context ctx, int userId, int faceId) {
        getStateForUser(ctx, userId).removeBiometric(faceId);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void renameBiometricForUser(android.content.Context ctx, int userId, int faceId, java.lang.CharSequence name) {
        if (android.text.TextUtils.isEmpty(name)) {
            return;
        }
        getStateForUser(ctx, userId).renameBiometric(faceId, name);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public java.lang.CharSequence getUniqueName(android.content.Context context, int userId) {
        return getStateForUser(context, userId).getUniqueName();
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void setInvalidationInProgress(android.content.Context context, int userId, boolean inProgress) {
        getStateForUser(context, userId).setInvalidationInProgress(inProgress);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public boolean isInvalidationInProgress(android.content.Context context, int userId) {
        return getStateForUser(context, userId).isInvalidationInProgress();
    }

    private com.android.server.biometrics.sensors.face.FaceUserState getStateForUser(android.content.Context ctx, int userId) {
        com.android.server.biometrics.sensors.face.FaceUserState state;
        synchronized (this) {
            state = this.mUserStates.get(userId);
            if (state == null) {
                state = new com.android.server.biometrics.sensors.face.FaceUserState(ctx, userId, this.mFileName);
                this.mUserStates.put(userId, state);
            }
        }
        return state;
    }
}
