package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface AuthenticationConsumer {
    void onAuthenticated(android.hardware.biometrics.BiometricAuthenticator.Identifier identifier, boolean z, java.util.ArrayList<java.lang.Byte> arrayList);
}
