package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class ForegroundServiceDelegation {
    public final android.os.IBinder mBinder = new android.os.Binder();
    public final android.content.ServiceConnection mConnection;
    public final android.app.ForegroundServiceDelegationOptions mOptions;

    public ForegroundServiceDelegation(android.app.ForegroundServiceDelegationOptions options, android.content.ServiceConnection connection) {
        this.mOptions = options;
        this.mConnection = connection;
    }
}
