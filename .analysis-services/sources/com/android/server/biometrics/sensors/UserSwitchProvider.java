package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface UserSwitchProvider<T, U> {
    com.android.server.biometrics.sensors.StartUserClient<T, U> getStartUserClient(int i);

    com.android.server.biometrics.sensors.StopUserClient<U> getStopUserClient(int i);
}
