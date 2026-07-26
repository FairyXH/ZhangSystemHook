package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public class BiometricNotificationImpl implements com.android.server.biometrics.sensors.BiometricNotification {
    @Override // com.android.server.biometrics.sensors.BiometricNotification
    public void sendFaceEnrollNotification(android.content.Context context) {
        com.android.server.biometrics.sensors.BiometricNotificationUtils.showFaceEnrollNotification(context);
    }

    @Override // com.android.server.biometrics.sensors.BiometricNotification
    public void sendFpEnrollNotification(android.content.Context context) {
        com.android.server.biometrics.sensors.BiometricNotificationUtils.showFingerprintEnrollNotification(context);
    }
}
