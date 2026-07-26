package com.android.server.backup.transport;

/* JADX INFO: loaded from: classes.dex */
public class TransportConnection {
    private static final int LOG_BUFFER_SIZE = 5;
    static final java.lang.String TAG = "TransportConnection";
    private final android.content.Intent mBindIntent;
    private final dalvik.system.CloseGuard mCloseGuard;
    private final android.content.ServiceConnection mConnection;
    private final android.content.Context mContext;
    private final java.lang.String mCreatorLogString;
    private final java.lang.String mIdentifier;
    private final android.os.Handler mListenerHandler;
    private final java.util.Map<com.android.server.backup.transport.TransportConnectionListener, java.lang.String> mListeners;
    private final java.util.List<java.lang.String> mLogBuffer;
    private final java.lang.Object mLogBufferLock;
    private final java.lang.String mPrefixForLog;
    private int mState;
    private final java.lang.Object mStateLock;
    private volatile com.android.server.backup.transport.BackupTransportClient mTransport;
    private final android.content.ComponentName mTransportComponent;
    private final com.android.server.backup.transport.TransportStats mTransportStats;
    private final int mUserId;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface State {
        public static final int BOUND_AND_CONNECTING = 2;
        public static final int CONNECTED = 3;
        public static final int IDLE = 1;
        public static final int UNUSABLE = 0;
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface Transition {
        public static final int DOWN = -1;
        public static final int NO_TRANSITION = 0;
        public static final int UP = 1;
    }

    TransportConnection(int userId, android.content.Context context, com.android.server.backup.transport.TransportStats transportStats, android.content.Intent bindIntent, android.content.ComponentName transportComponent, java.lang.String identifier, java.lang.String caller) {
        this(userId, context, transportStats, bindIntent, transportComponent, identifier, caller, new android.os.Handler(android.os.Looper.getMainLooper()));
    }

    TransportConnection(int userId, android.content.Context context, com.android.server.backup.transport.TransportStats transportStats, android.content.Intent bindIntent, android.content.ComponentName transportComponent, java.lang.String identifier, java.lang.String caller, android.os.Handler listenerHandler) {
        this.mStateLock = new java.lang.Object();
        this.mLogBufferLock = new java.lang.Object();
        this.mCloseGuard = dalvik.system.CloseGuard.get();
        this.mLogBuffer = new java.util.LinkedList();
        this.mListeners = new android.util.ArrayMap();
        this.mState = 1;
        this.mUserId = userId;
        this.mContext = context;
        this.mTransportStats = transportStats;
        this.mTransportComponent = transportComponent;
        this.mBindIntent = bindIntent;
        this.mIdentifier = identifier;
        this.mCreatorLogString = caller;
        this.mListenerHandler = listenerHandler;
        this.mConnection = new com.android.server.backup.transport.TransportConnection.TransportConnectionMonitor(context, this);
        java.lang.String classNameForLog = this.mTransportComponent.getShortClassName().replaceFirst(".*\\.", "");
        this.mPrefixForLog = classNameForLog + "#" + this.mIdentifier + ":";
        this.mCloseGuard.open("markAsDisposed");
    }

    public android.content.ComponentName getTransportComponent() {
        return this.mTransportComponent;
    }

    public void connectAsync(com.android.server.backup.transport.TransportConnectionListener listener, java.lang.String caller) {
        synchronized (this.mStateLock) {
            checkStateIntegrityLocked();
            switch (this.mState) {
                case 0:
                    log(5, caller, "Async connect: UNUSABLE client");
                    notifyListener(listener, null, caller);
                    break;
                case 1:
                    boolean hasBound = this.mContext.bindServiceAsUser(this.mBindIntent, this.mConnection, 1, android.os.UserHandle.of(this.mUserId));
                    if (hasBound) {
                        log(3, caller, "Async connect: service bound, connecting");
                        setStateLocked(2, null);
                        this.mListeners.put(listener, caller);
                    } else {
                        log(6, "Async connect: bindService returned false");
                        this.mContext.unbindService(this.mConnection);
                        notifyListener(listener, null, caller);
                    }
                    break;
                case 2:
                    log(3, caller, "Async connect: already connecting, adding listener");
                    this.mListeners.put(listener, caller);
                    break;
                case 3:
                    log(3, caller, "Async connect: reusing transport");
                    notifyListener(listener, this.mTransport, caller);
                    break;
            }
        }
    }

    public void unbind(java.lang.String caller) {
        synchronized (this.mStateLock) {
            checkStateIntegrityLocked();
            log(3, caller, "Unbind requested (was " + stateToString(this.mState) + ")");
            switch (this.mState) {
                case 2:
                    setStateLocked(1, null);
                    this.mContext.unbindService(this.mConnection);
                    notifyListenersAndClearLocked(null);
                    break;
                case 3:
                    setStateLocked(1, null);
                    this.mContext.unbindService(this.mConnection);
                    break;
            }
        }
    }

    public void markAsDisposed() {
        synchronized (this.mStateLock) {
            com.android.internal.util.Preconditions.checkState(this.mState < 2, "Can't mark as disposed if still bound");
            this.mCloseGuard.close();
        }
    }

    public com.android.server.backup.transport.BackupTransportClient connect(java.lang.String caller) {
        com.android.internal.util.Preconditions.checkState(!android.os.Looper.getMainLooper().isCurrentThread(), "Can't call connect() on main thread");
        com.android.server.backup.transport.BackupTransportClient transport = this.mTransport;
        if (transport != null) {
            log(3, caller, "Sync connect: reusing transport");
            return transport;
        }
        synchronized (this.mStateLock) {
            if (this.mState == 0) {
                log(5, caller, "Sync connect: UNUSABLE client");
                return null;
            }
            final java.util.concurrent.CompletableFuture<com.android.server.backup.transport.BackupTransportClient> transportFuture = new java.util.concurrent.CompletableFuture<>();
            com.android.server.backup.transport.TransportConnectionListener requestListener = new com.android.server.backup.transport.TransportConnectionListener() { // from class: com.android.server.backup.transport.TransportConnection$$ExternalSyntheticLambda0
                @Override // com.android.server.backup.transport.TransportConnectionListener
                public final void onTransportConnectionResult(com.android.server.backup.transport.BackupTransportClient backupTransportClient, com.android.server.backup.transport.TransportConnection transportConnection) {
                    transportFuture.complete(backupTransportClient);
                }
            };
            long requestTime = android.os.SystemClock.elapsedRealtime();
            log(3, caller, "Sync connect: calling async");
            connectAsync(requestListener, caller);
            try {
                com.android.server.backup.transport.BackupTransportClient transport2 = transportFuture.get(60L, java.util.concurrent.TimeUnit.SECONDS);
                long time = android.os.SystemClock.elapsedRealtime() - requestTime;
                this.mTransportStats.registerConnectionTime(this.mTransportComponent, time);
                log(3, caller, java.lang.String.format(java.util.Locale.US, "Connect took %d ms", java.lang.Long.valueOf(time)));
                return transport2;
            } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
                java.lang.String error = e.getClass().getSimpleName();
                log(6, caller, error + " while waiting for transport: " + e.getMessage());
                return null;
            }
        }
    }

    public com.android.server.backup.transport.BackupTransportClient connectOrThrow(java.lang.String caller) throws com.android.server.backup.transport.TransportNotAvailableException {
        com.android.server.backup.transport.BackupTransportClient transport = connect(caller);
        if (transport == null) {
            log(6, caller, "Transport connection failed");
            throw new com.android.server.backup.transport.TransportNotAvailableException();
        }
        return transport;
    }

    public com.android.server.backup.transport.BackupTransportClient getConnectedTransport(java.lang.String caller) throws com.android.server.backup.transport.TransportNotAvailableException {
        com.android.server.backup.transport.BackupTransportClient transport = this.mTransport;
        if (transport == null) {
            log(6, caller, "Transport not connected");
            throw new com.android.server.backup.transport.TransportNotAvailableException();
        }
        return transport;
    }

    public java.lang.String toString() {
        return "TransportClient{" + this.mTransportComponent.flattenToShortString() + "#" + this.mIdentifier + "}";
    }

    protected void finalize() throws java.lang.Throwable {
        synchronized (this.mStateLock) {
            this.mCloseGuard.warnIfOpen();
            if (this.mState >= 2) {
                log(6, "TransportClient.finalize()", "Dangling TransportClient created in [" + this.mCreatorLogString + "] being GC'ed. Left bound, unbinding...");
                try {
                    unbind("TransportClient.finalize()");
                } catch (java.lang.IllegalArgumentException | java.lang.IllegalStateException e) {
                    log(5, "Exception trying to unbind finalize(): " + e.getMessage());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onServiceConnected(android.os.IBinder binder) {
        com.android.internal.backup.IBackupTransport transportBinder = com.android.internal.backup.IBackupTransport.Stub.asInterface(binder);
        com.android.server.backup.transport.BackupTransportClient transport = new com.android.server.backup.transport.BackupTransportClient(transportBinder);
        synchronized (this.mStateLock) {
            checkStateIntegrityLocked();
            if (this.mState != 0) {
                log(3, "Transport connected");
                setStateLocked(3, transport);
                notifyListenersAndClearLocked(transport);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onServiceDisconnected() {
        synchronized (this.mStateLock) {
            log(6, "Service disconnected: client UNUSABLE");
            if (this.mTransport != null) {
                this.mTransport.onBecomingUnusable();
            }
            setStateLocked(0, null);
            try {
                this.mContext.unbindService(this.mConnection);
            } catch (java.lang.IllegalArgumentException e) {
                log(5, "Exception trying to unbind onServiceDisconnected(): " + e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBindingDied() {
        synchronized (this.mStateLock) {
            checkStateIntegrityLocked();
            log(6, "Binding died: client UNUSABLE");
            if (this.mTransport != null) {
                this.mTransport.onBecomingUnusable();
            }
            switch (this.mState) {
                case 1:
                    log(6, "Unexpected state transition IDLE => UNUSABLE");
                    setStateLocked(0, null);
                    break;
                case 2:
                    setStateLocked(0, null);
                    this.mContext.unbindService(this.mConnection);
                    notifyListenersAndClearLocked(null);
                    break;
                case 3:
                    setStateLocked(0, null);
                    this.mContext.unbindService(this.mConnection);
                    break;
            }
        }
    }

    private void notifyListener(final com.android.server.backup.transport.TransportConnectionListener listener, final com.android.server.backup.transport.BackupTransportClient transport, java.lang.String caller) {
        java.lang.String transportString = transport != null ? "BackupTransportClient" : "null";
        log(4, "Notifying [" + caller + "] transport = " + transportString);
        this.mListenerHandler.post(new java.lang.Runnable() { // from class: com.android.server.backup.transport.TransportConnection$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyListener$1(listener, transport);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyListener$1(com.android.server.backup.transport.TransportConnectionListener listener, com.android.server.backup.transport.BackupTransportClient transport) {
        listener.onTransportConnectionResult(transport, this);
    }

    private void notifyListenersAndClearLocked(com.android.server.backup.transport.BackupTransportClient transport) {
        for (java.util.Map.Entry<com.android.server.backup.transport.TransportConnectionListener, java.lang.String> entry : this.mListeners.entrySet()) {
            com.android.server.backup.transport.TransportConnectionListener listener = entry.getKey();
            java.lang.String caller = entry.getValue();
            notifyListener(listener, transport, caller);
        }
        this.mListeners.clear();
    }

    private void setStateLocked(int state, com.android.server.backup.transport.BackupTransportClient transport) {
        log(2, "State: " + stateToString(this.mState) + " => " + stateToString(state));
        onStateTransition(this.mState, state);
        this.mState = state;
        this.mTransport = transport;
    }

    private void onStateTransition(int oldState, int newState) {
        java.lang.String transport = this.mTransportComponent.flattenToShortString();
        int bound = transitionThroughState(oldState, newState, 2);
        int connected = transitionThroughState(oldState, newState, 3);
        if (bound != 0) {
            int value = bound == 1 ? 1 : 0;
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_TRANSPORT_LIFECYCLE, transport, java.lang.Integer.valueOf(value));
        }
        if (connected != 0) {
            int value2 = connected == 1 ? 1 : 0;
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.BACKUP_TRANSPORT_CONNECTION, transport, java.lang.Integer.valueOf(value2));
        }
    }

    private int transitionThroughState(int oldState, int newState, int stateReference) {
        if (oldState < stateReference && stateReference <= newState) {
            return 1;
        }
        if (oldState >= stateReference && stateReference > newState) {
            return -1;
        }
        return 0;
    }

    private void checkStateIntegrityLocked() {
        switch (this.mState) {
            case 0:
                checkState(this.mListeners.isEmpty(), "Unexpected listeners when state = UNUSABLE");
                checkState(this.mTransport == null, "Transport expected to be null when state = UNUSABLE");
                break;
            case 1:
                break;
            case 2:
                checkState(this.mTransport == null, "Transport expected to be null when state = BOUND_AND_CONNECTING");
                return;
            case 3:
                checkState(this.mListeners.isEmpty(), "Unexpected listeners when state = CONNECTED");
                checkState(this.mTransport != null, "Transport expected to be non-null when state = CONNECTED");
                return;
            default:
                checkState(false, "Unexpected state = " + stateToString(this.mState));
                return;
        }
        checkState(this.mListeners.isEmpty(), "Unexpected listeners when state = IDLE");
        checkState(this.mTransport == null, "Transport expected to be null when state = IDLE");
    }

    private void checkState(boolean assertion, java.lang.String message) {
        if (!assertion) {
            log(6, message);
        }
    }

    private java.lang.String stateToString(int state) {
        switch (state) {
            case 0:
                return "UNUSABLE";
            case 1:
                return "IDLE";
            case 2:
                return "BOUND_AND_CONNECTING";
            case 3:
                return "CONNECTED";
            default:
                return "<UNKNOWN = " + state + ">";
        }
    }

    private void log(int priority, java.lang.String message) {
        com.android.server.backup.transport.TransportUtils.log(priority, TAG, com.android.server.backup.transport.TransportUtils.formatMessage(this.mPrefixForLog, null, message));
        saveLogEntry(com.android.server.backup.transport.TransportUtils.formatMessage(null, null, message));
    }

    private void log(int priority, java.lang.String caller, java.lang.String message) {
        com.android.server.backup.transport.TransportUtils.log(priority, TAG, com.android.server.backup.transport.TransportUtils.formatMessage(this.mPrefixForLog, caller, message));
        saveLogEntry(com.android.server.backup.transport.TransportUtils.formatMessage(null, caller, message));
    }

    private void saveLogEntry(java.lang.String message) {
        java.lang.CharSequence time = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", java.lang.System.currentTimeMillis());
        java.lang.String message2 = ((java.lang.Object) time) + " " + message;
        synchronized (this.mLogBufferLock) {
            if (this.mLogBuffer.size() == 5) {
                this.mLogBuffer.remove(this.mLogBuffer.size() - 1);
            }
            this.mLogBuffer.add(0, message2);
        }
    }

    java.util.List<java.lang.String> getLogBuffer() {
        java.util.List<java.lang.String> listUnmodifiableList;
        synchronized (this.mLogBufferLock) {
            listUnmodifiableList = java.util.Collections.unmodifiableList(this.mLogBuffer);
        }
        return listUnmodifiableList;
    }

    static class TransportConnectionMonitor implements android.content.ServiceConnection {
        private final android.content.Context mContext;
        private final java.lang.ref.WeakReference<com.android.server.backup.transport.TransportConnection> mTransportClientRef;

        TransportConnectionMonitor(android.content.Context context, com.android.server.backup.transport.TransportConnection transportConnection) {
            this.mContext = context;
            this.mTransportClientRef = new java.lang.ref.WeakReference<>(transportConnection);
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName transportComponent, android.os.IBinder binder) {
            com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportClientRef.get();
            if (transportConnection == null) {
                referenceLost("TransportConnection.onServiceConnected()");
            } else {
                android.os.Binder.allowBlocking(binder);
                transportConnection.onServiceConnected(binder);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName transportComponent) {
            com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportClientRef.get();
            if (transportConnection == null) {
                referenceLost("TransportConnection.onServiceDisconnected()");
            } else {
                transportConnection.onServiceDisconnected();
            }
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(android.content.ComponentName transportComponent) {
            com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportClientRef.get();
            if (transportConnection == null) {
                referenceLost("TransportConnection.onBindingDied()");
            } else {
                transportConnection.onBindingDied();
            }
        }

        private void referenceLost(java.lang.String caller) {
            try {
                this.mContext.unbindService(this);
                com.android.server.backup.transport.TransportUtils.log(4, com.android.server.backup.transport.TransportConnection.TAG, caller + " called but TransportClient reference has been GC'ed");
            } catch (java.lang.IllegalArgumentException e) {
                com.android.server.backup.transport.TransportUtils.log(5, com.android.server.backup.transport.TransportConnection.TAG, caller + " called but unbindService failed: " + e.getMessage());
            }
        }
    }
}
