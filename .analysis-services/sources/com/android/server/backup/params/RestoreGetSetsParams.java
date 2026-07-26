package com.android.server.backup.params;

/* JADX INFO: loaded from: classes.dex */
public class RestoreGetSetsParams {
    public final com.android.server.backup.internal.OnTaskFinishedListener listener;
    public final com.android.server.backup.transport.TransportConnection mTransportConnection;
    public final android.app.backup.IBackupManagerMonitor monitor;
    public final android.app.backup.IRestoreObserver observer;
    public final com.android.server.backup.restore.ActiveRestoreSession session;

    public RestoreGetSetsParams(com.android.server.backup.transport.TransportConnection _transportConnection, com.android.server.backup.restore.ActiveRestoreSession _session, android.app.backup.IRestoreObserver _observer, android.app.backup.IBackupManagerMonitor _monitor, com.android.server.backup.internal.OnTaskFinishedListener _listener) {
        this.mTransportConnection = _transportConnection;
        this.session = _session;
        this.observer = _observer;
        this.monitor = _monitor;
        this.listener = _listener;
    }
}
