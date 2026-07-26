package com.android.server.biometrics.log;

/* JADX INFO: loaded from: classes.dex */
public interface BiometricContext {
    com.android.server.biometrics.sensors.AuthSessionCoordinator getAuthSessionCoordinator();

    com.android.server.biometrics.log.BiometricContextSessionInfo getBiometricPromptSessionInfo();

    int getCurrentRotation();

    int getDisplayState();

    int getDockedState();

    int getFoldState();

    com.android.server.biometrics.log.BiometricContextSessionInfo getKeyguardEntrySessionInfo();

    boolean isAod();

    boolean isAwake();

    boolean isDisplayOn();

    boolean isHardwareIgnoringTouches();

    void subscribe(com.android.server.biometrics.log.OperationContextExt operationContextExt, java.util.function.Consumer<android.hardware.biometrics.common.OperationContext> consumer, java.util.function.Consumer<android.hardware.biometrics.common.OperationContext> consumer2, android.hardware.biometrics.AuthenticateOptions authenticateOptions);

    void unsubscribe(com.android.server.biometrics.log.OperationContextExt operationContextExt);

    com.android.server.biometrics.log.OperationContextExt updateContext(com.android.server.biometrics.log.OperationContextExt operationContextExt, boolean z);

    static com.android.server.biometrics.log.BiometricContext getInstance(android.content.Context context) {
        return com.android.server.biometrics.log.BiometricContextProvider.defaultProvider(context);
    }
}
