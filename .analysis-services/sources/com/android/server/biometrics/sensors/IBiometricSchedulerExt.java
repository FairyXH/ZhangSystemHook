package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface IBiometricSchedulerExt {
    default void cancelExpandClientIfNeed(com.android.server.biometrics.sensors.BaseClientMonitor clientMonitor, com.android.server.biometrics.sensors.BiometricSchedulerOperation mCurrentOperation) {
    }

    default void setPendingClientToCancelState(com.android.server.biometrics.sensors.BiometricSchedulerOperation mCurrentOperation, java.util.Deque<com.android.server.biometrics.sensors.BiometricSchedulerOperation> mPendingOperations, android.os.IBinder token) {
    }

    default void tryToCancelPendingClient(java.util.Deque<com.android.server.biometrics.sensors.BiometricSchedulerOperation> mPendingOperations, android.os.IBinder token) {
    }

    default android.os.Handler createHandlerWithNewLooper() {
        return null;
    }
}
