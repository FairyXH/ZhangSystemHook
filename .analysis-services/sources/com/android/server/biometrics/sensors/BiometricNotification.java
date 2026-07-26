package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface BiometricNotification {
    void sendFaceEnrollNotification(android.content.Context context);

    void sendFpEnrollNotification(android.content.Context context);
}
