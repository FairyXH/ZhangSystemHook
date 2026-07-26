package com.android.server.biometrics.sensors.fingerprint;

/* JADX INFO: loaded from: classes.dex */
public class FingerprintUtils implements com.android.server.biometrics.sensors.BiometricUtils<android.hardware.fingerprint.Fingerprint> {
    private static final java.lang.String LEGACY_FINGERPRINT_FILE = "settings_fingerprint.xml";
    private static final java.lang.Object sInstanceLock = new java.lang.Object();
    private static android.util.SparseArray<com.android.server.biometrics.sensors.fingerprint.FingerprintUtils> sInstances;
    private final java.lang.String mFileName;
    private com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.FingerprintUtilsWrapper mWrapper = new com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.FingerprintUtilsWrapper();
    private com.android.server.biometrics.sensors.fingerprint.IFingerprintUtilsExt mFingerprintUtilsExt = (com.android.server.biometrics.sensors.fingerprint.IFingerprintUtilsExt) system.ext.loader.core.ExtLoader.type(com.android.server.biometrics.sensors.fingerprint.IFingerprintUtilsExt.class).base(this).create();
    private final android.util.SparseArray<com.android.server.biometrics.sensors.fingerprint.FingerprintUserState> mUserStates = new android.util.SparseArray<>();

    public static com.android.server.biometrics.sensors.fingerprint.FingerprintUtils getInstance(int sensorId) {
        return getInstance(sensorId, LEGACY_FINGERPRINT_FILE);
    }

    private static com.android.server.biometrics.sensors.fingerprint.FingerprintUtils getInstance(int sensorId, java.lang.String fileName) {
        com.android.server.biometrics.sensors.fingerprint.FingerprintUtils utils;
        synchronized (sInstanceLock) {
            if (sInstances == null) {
                sInstances = new android.util.SparseArray<>();
            }
            if (sInstances.get(sensorId) == null) {
                if (fileName == null) {
                    fileName = "settings_fingerprint_" + sensorId + ".xml";
                }
                sInstances.put(sensorId, new com.android.server.biometrics.sensors.fingerprint.FingerprintUtils(fileName));
            }
            utils = sInstances.get(sensorId);
        }
        return utils;
    }

    public static com.android.server.biometrics.sensors.fingerprint.FingerprintUtils getLegacyInstance(int sensorId) {
        return getInstance(sensorId, LEGACY_FINGERPRINT_FILE);
    }

    private FingerprintUtils(java.lang.String fileName) {
        this.mFileName = fileName;
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public java.util.List<android.hardware.fingerprint.Fingerprint> getBiometricsForUser(android.content.Context ctx, int userId) {
        return getStateForUser(ctx, userId).getBiometrics();
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void addBiometricForUser(android.content.Context context, int userId, android.hardware.fingerprint.Fingerprint fingerprint) {
        getStateForUser(context, userId).addBiometric(fingerprint);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void removeBiometricForUser(android.content.Context context, int userId, int fingerId) {
        getStateForUser(context, userId).removeBiometric(fingerId);
    }

    @Override // com.android.server.biometrics.sensors.BiometricUtils
    public void renameBiometricForUser(android.content.Context context, int userId, int fingerId, java.lang.CharSequence name) {
        if (android.text.TextUtils.isEmpty(name)) {
            return;
        }
        getStateForUser(context, userId).renameBiometric(fingerId, name);
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

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.biometrics.sensors.fingerprint.FingerprintUserState getStateForUser(android.content.Context ctx, int userId) {
        com.android.server.biometrics.sensors.fingerprint.FingerprintUserState state;
        int userId2 = this.mWrapper.getExtImpl().hookTargetUserId(userId);
        synchronized (this) {
            state = this.mUserStates.get(userId2);
            if (state == null) {
                state = new com.android.server.biometrics.sensors.fingerprint.FingerprintUserState(ctx, userId2, this.mFileName);
                this.mUserStates.put(userId2, state);
            }
        }
        return state;
    }

    public static boolean isKnownErrorCode(int errorCode) {
        switch (errorCode) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return true;
            default:
                return false;
        }
    }

    public static boolean isKnownAcquiredCode(int acquiredCode) {
        switch (acquiredCode) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
                return true;
            case 8:
            default:
                return false;
        }
    }

    public com.android.server.biometrics.sensors.fingerprint.IFingerprintUtilsWrapper getWrapper() {
        return this.mWrapper;
    }

    private class FingerprintUtilsWrapper implements com.android.server.biometrics.sensors.fingerprint.IFingerprintUtilsWrapper {
        private FingerprintUtilsWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.biometrics.sensors.fingerprint.IFingerprintUtilsExt getExtImpl() {
            return com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.this.mFingerprintUtilsExt;
        }

        @Override // com.android.server.biometrics.sensors.fingerprint.IFingerprintUtilsWrapper
        public com.android.server.biometrics.sensors.fingerprint.FingerprintUserState getStateForUser(android.content.Context ctx, int userId) {
            return com.android.server.biometrics.sensors.fingerprint.FingerprintUtils.this.getStateForUser(ctx, userId);
        }
    }
}
