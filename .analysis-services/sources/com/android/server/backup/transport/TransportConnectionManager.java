package com.android.server.backup.transport;

/* JADX INFO: loaded from: classes.dex */
public class TransportConnectionManager {
    private static final java.lang.String TAG = "TransportConnectionManager";
    private final android.content.Context mContext;
    private final java.util.function.Function<android.content.ComponentName, android.content.Intent> mIntentFunction;
    private java.util.Map<com.android.server.backup.transport.TransportConnection, java.lang.String> mTransportClientsCallerMap;
    private int mTransportClientsCreated;
    private final java.lang.Object mTransportClientsLock;
    private final com.android.server.backup.transport.TransportStats mTransportStats;
    private final int mUserId;

    /* JADX INFO: Access modifiers changed from: private */
    public static android.content.Intent getRealTransportIntent(android.content.ComponentName transportComponent) {
        return new android.content.Intent(com.android.server.backup.TransportManager.SERVICE_ACTION_TRANSPORT_HOST).setComponent(transportComponent);
    }

    public TransportConnectionManager(int userId, android.content.Context context, com.android.server.backup.transport.TransportStats transportStats) {
        this(userId, context, transportStats, new java.util.function.Function() { // from class: com.android.server.backup.transport.TransportConnectionManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.backup.transport.TransportConnectionManager.getRealTransportIntent((android.content.ComponentName) obj);
            }
        });
    }

    private TransportConnectionManager(int userId, android.content.Context context, com.android.server.backup.transport.TransportStats transportStats, java.util.function.Function<android.content.ComponentName, android.content.Intent> intentFunction) {
        this.mTransportClientsLock = new java.lang.Object();
        this.mTransportClientsCreated = 0;
        this.mTransportClientsCallerMap = new java.util.WeakHashMap();
        this.mUserId = userId;
        this.mContext = context;
        this.mTransportStats = transportStats;
        this.mIntentFunction = intentFunction;
    }

    public com.android.server.backup.transport.TransportConnection getTransportClient(android.content.ComponentName transportComponent, java.lang.String caller) {
        return getTransportClient(transportComponent, (android.os.Bundle) null, caller);
    }

    public com.android.server.backup.transport.TransportConnection getTransportClient(android.content.ComponentName transportComponent, android.os.Bundle extras, java.lang.String caller) {
        android.content.Intent bindIntent = this.mIntentFunction.apply(transportComponent);
        if (extras != null) {
            bindIntent.putExtras(extras);
        }
        return getTransportClient(transportComponent, caller, bindIntent);
    }

    private com.android.server.backup.transport.TransportConnection getTransportClient(android.content.ComponentName transportComponent, java.lang.String caller, android.content.Intent bindIntent) {
        com.android.server.backup.transport.TransportConnection transportConnection;
        synchronized (this.mTransportClientsLock) {
            transportConnection = new com.android.server.backup.transport.TransportConnection(this.mUserId, this.mContext, this.mTransportStats, bindIntent, transportComponent, java.lang.Integer.toString(this.mTransportClientsCreated), caller);
            this.mTransportClientsCallerMap.put(transportConnection, caller);
            this.mTransportClientsCreated++;
            com.android.server.backup.transport.TransportUtils.log(3, TAG, com.android.server.backup.transport.TransportUtils.formatMessage(null, caller, "Retrieving " + transportConnection));
        }
        return transportConnection;
    }

    public void disposeOfTransportClient(com.android.server.backup.transport.TransportConnection transportConnection, java.lang.String caller) {
        if (transportConnection == null) {
            android.util.Log.d(TAG, "caller:" + caller);
            return;
        }
        transportConnection.unbind(caller);
        transportConnection.markAsDisposed();
        synchronized (this.mTransportClientsLock) {
            com.android.server.backup.transport.TransportUtils.log(3, TAG, com.android.server.backup.transport.TransportUtils.formatMessage(null, caller, "Disposing of " + transportConnection));
            this.mTransportClientsCallerMap.remove(transportConnection);
        }
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("Transport clients created: " + this.mTransportClientsCreated);
        synchronized (this.mTransportClientsLock) {
            pw.println("Current transport clients: " + this.mTransportClientsCallerMap.size());
            for (com.android.server.backup.transport.TransportConnection transportConnection : this.mTransportClientsCallerMap.keySet()) {
                java.lang.String caller = this.mTransportClientsCallerMap.get(transportConnection);
                pw.println("    " + transportConnection + " [" + caller + "]");
                for (java.lang.String logEntry : transportConnection.getLogBuffer()) {
                    pw.println("        " + logEntry);
                }
            }
        }
    }
}
