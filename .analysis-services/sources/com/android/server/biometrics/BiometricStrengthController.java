package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class BiometricStrengthController {
    private static final java.lang.String KEY_BIOMETRIC_STRENGTHS = "biometric_strengths";
    private static final java.lang.String TAG = "BiometricStrengthController";
    private android.provider.DeviceConfig.OnPropertiesChangedListener mDeviceConfigListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.biometrics.BiometricStrengthController$$ExternalSyntheticLambda0
        public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            this.f$0.lambda$new$0(properties);
        }
    };
    private final com.android.server.biometrics.BiometricService mService;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.provider.DeviceConfig.Properties properties) {
        if (properties.getKeyset().contains(KEY_BIOMETRIC_STRENGTHS)) {
            updateStrengths();
        }
    }

    public BiometricStrengthController(com.android.server.biometrics.BiometricService service) {
        this.mService = service;
    }

    public void startListening() {
        android.provider.DeviceConfig.addOnPropertiesChangedListener("biometrics", com.android.internal.os.BackgroundThread.getExecutor(), this.mDeviceConfigListener);
    }

    public void updateStrengths() {
        java.lang.String newValue = android.provider.DeviceConfig.getString("biometrics", KEY_BIOMETRIC_STRENGTHS, "null");
        if ("null".equals(newValue) || newValue.isEmpty()) {
            revertStrengths();
        } else {
            updateStrengths(newValue);
        }
    }

    private void updateStrengths(java.lang.String flags) {
        java.util.Map<java.lang.Integer, java.lang.Integer> idToStrength = getIdToStrengthMap(flags);
        if (idToStrength == null) {
            return;
        }
        for (com.android.server.biometrics.BiometricSensor sensor : this.mService.mSensors) {
            int id = sensor.id;
            if (idToStrength.containsKey(java.lang.Integer.valueOf(id))) {
                int newStrength = idToStrength.get(java.lang.Integer.valueOf(id)).intValue();
                android.util.Slog.d(TAG, "updateStrengths: update sensorId=" + id + " to newStrength=" + newStrength);
                sensor.updateStrength(newStrength);
            }
        }
    }

    private void revertStrengths() {
        for (com.android.server.biometrics.BiometricSensor sensor : this.mService.mSensors) {
            android.util.Slog.d(TAG, "updateStrengths: revert sensorId=" + sensor.id + " to oemStrength=" + sensor.oemStrength);
            sensor.updateStrength(sensor.oemStrength);
        }
    }

    private static java.util.Map<java.lang.Integer, java.lang.Integer> getIdToStrengthMap(java.lang.String flags) {
        if (flags == null || flags.isEmpty()) {
            android.util.Slog.d(TAG, "Flags are null or empty");
            return null;
        }
        java.util.Map<java.lang.Integer, java.lang.Integer> map = new java.util.HashMap<>();
        try {
            for (java.lang.String item : flags.split(",")) {
                java.lang.String[] elems = item.split(":");
                int id = java.lang.Integer.parseInt(elems[0]);
                int strength = java.lang.Integer.parseInt(elems[1]);
                map.put(java.lang.Integer.valueOf(id), java.lang.Integer.valueOf(strength));
            }
            return map;
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Can't parse flag: " + flags);
            return null;
        }
    }
}
