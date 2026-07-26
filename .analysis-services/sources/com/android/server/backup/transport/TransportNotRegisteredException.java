package com.android.server.backup.transport;

/* JADX INFO: loaded from: classes.dex */
public class TransportNotRegisteredException extends android.util.AndroidException {
    public TransportNotRegisteredException(java.lang.String transportName) {
        super("Transport " + transportName + " not registered");
    }

    public TransportNotRegisteredException(android.content.ComponentName transportComponent) {
        super("Transport for host " + transportComponent + " not registered");
    }
}
