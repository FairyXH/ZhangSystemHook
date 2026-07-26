package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface IBiometricSchedulerWrapper {
    default com.android.server.biometrics.sensors.IBiometricSchedulerExt getExtImpl() {
        return new com.android.server.biometrics.sensors.IBiometricSchedulerExt() { // from class: com.android.server.biometrics.sensors.IBiometricSchedulerWrapper.1
        };
    }

    default com.android.server.biometrics.sensors.BiometricSchedulerOperation getCurrentOperationWrapper() {
        return null;
    }

    default java.util.Deque<com.android.server.biometrics.sensors.BiometricSchedulerOperation> getPendingOperationWrapper() {
        return null;
    }
}
