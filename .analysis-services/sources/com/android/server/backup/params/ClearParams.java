package com.android.server.backup.params;

/* JADX INFO: loaded from: classes.dex */
public class ClearParams {
    public com.android.server.backup.internal.OnTaskFinishedListener listener;
    public com.android.server.backup.transport.TransportConnection mTransportConnection;
    public android.content.pm.PackageInfo packageInfo;

    public ClearParams(com.android.server.backup.transport.TransportConnection transportConnection, android.content.pm.PackageInfo packageInfo, com.android.server.backup.internal.OnTaskFinishedListener listener) {
        this.mTransportConnection = transportConnection;
        this.packageInfo = packageInfo;
        this.listener = listener;
    }
}
