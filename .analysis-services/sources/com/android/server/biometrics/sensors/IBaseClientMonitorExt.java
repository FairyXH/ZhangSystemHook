package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface IBaseClientMonitorExt {
    default int hookTargetUserId(int defaultUserId) {
        return defaultUserId;
    }
}
