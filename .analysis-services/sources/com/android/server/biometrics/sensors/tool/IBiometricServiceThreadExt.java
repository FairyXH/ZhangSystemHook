package com.android.server.biometrics.sensors.tool;

/* JADX INFO: loaded from: classes.dex */
public interface IBiometricServiceThreadExt {

    public interface IStaticExt {
        default android.os.Looper getLooperInstance() {
            return null;
        }
    }
}
