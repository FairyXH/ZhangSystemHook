package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface BiometricServiceProvider<T extends android.hardware.biometrics.SensorPropertiesInternal> {
    boolean containsSensor(int i);

    void dumpInternal(int i, java.io.PrintWriter printWriter);

    void dumpProtoMetrics(int i, java.io.FileDescriptor fileDescriptor);

    void dumpProtoState(int i, android.util.proto.ProtoOutputStream protoOutputStream, boolean z);

    long getAuthenticatorId(int i, int i2);

    int getLockoutModeForUser(int i, int i2);

    T getSensorProperties(int i);

    java.util.List<T> getSensorProperties();

    boolean hasEnrollments(int i, int i2);

    boolean isHardwareDetected(int i);
}
