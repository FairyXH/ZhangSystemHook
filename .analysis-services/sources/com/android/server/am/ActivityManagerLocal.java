package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public interface ActivityManagerLocal {
    boolean bindSdkSandboxService(android.content.Intent intent, android.content.ServiceConnection serviceConnection, int i, android.os.IBinder iBinder, java.lang.String str, java.lang.String str2, int i2) throws android.os.RemoteException;

    boolean bindSdkSandboxService(android.content.Intent intent, android.content.ServiceConnection serviceConnection, int i, android.os.IBinder iBinder, java.lang.String str, java.lang.String str2, android.content.Context.BindServiceFlags bindServiceFlags) throws android.os.RemoteException;

    boolean bindSdkSandboxService(android.content.Intent intent, android.content.ServiceConnection serviceConnection, int i, java.lang.String str, java.lang.String str2, int i2) throws android.os.RemoteException;

    boolean canAllowWhileInUsePermissionInFgs(int i, int i2, java.lang.String str);

    boolean canStartForegroundService(int i, int i2, java.lang.String str);

    void killSdkSandboxClientAppProcess(android.os.IBinder iBinder);

    android.content.ComponentName startSdkSandboxService(android.content.Intent intent, int i, java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    boolean stopSdkSandboxService(android.content.Intent intent, int i, java.lang.String str, java.lang.String str2);

    void tempAllowWhileInUsePermissionInFgs(int i, long j);
}
