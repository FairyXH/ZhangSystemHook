package com.android.server.companion.transport;

/* JADX INFO: loaded from: classes.dex */
public class CompanionTransportManager {
    private static final java.lang.String TAG = "CDM_CompanionTransportManager";
    private final com.android.server.companion.association.AssociationStore mAssociationStore;
    private final android.content.Context mContext;
    private boolean mSecureTransportEnabled = true;
    private final android.util.SparseArray<com.android.server.companion.transport.Transport> mTransports = new android.util.SparseArray<>();
    private final android.os.RemoteCallbackList<android.companion.IOnTransportsChangedListener> mTransportsListeners = new android.os.RemoteCallbackList<>();
    private final android.util.SparseArray<android.companion.IOnMessageReceivedListener> mMessageListeners = new android.util.SparseArray<>();

    public CompanionTransportManager(android.content.Context context, com.android.server.companion.association.AssociationStore associationStore) {
        this.mContext = context;
        this.mAssociationStore = associationStore;
    }

    public void addListener(int message, android.companion.IOnMessageReceivedListener listener) {
        this.mMessageListeners.put(message, listener);
        synchronized (this.mTransports) {
            for (int i = 0; i < this.mTransports.size(); i++) {
                this.mTransports.valueAt(i).addListener(message, listener);
            }
        }
    }

    public void addListener(final android.companion.IOnTransportsChangedListener listener) {
        android.util.Slog.i(TAG, "Registering OnTransportsChangedListener");
        synchronized (this.mTransportsListeners) {
            this.mTransportsListeners.register(listener);
            this.mTransportsListeners.broadcast(new java.util.function.Consumer() { // from class: com.android.server.companion.transport.CompanionTransportManager$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$addListener$0(listener, (android.companion.IOnTransportsChangedListener) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addListener$0(android.companion.IOnTransportsChangedListener listener, android.companion.IOnTransportsChangedListener listener1) {
        if (listener1 == listener) {
            try {
                listener.onTransportsChanged(getAssociationsWithTransport());
            } catch (android.os.RemoteException e) {
            }
        }
    }

    public void removeListener(android.companion.IOnTransportsChangedListener listener) {
        synchronized (this.mTransportsListeners) {
            this.mTransportsListeners.unregister(listener);
        }
    }

    public void removeListener(int messageType, android.companion.IOnMessageReceivedListener listener) {
        this.mMessageListeners.remove(messageType);
    }

    public void sendMessage(int message, byte[] data, int[] associationIds) {
        android.util.Slog.d(TAG, "Sending message 0x" + java.lang.Integer.toHexString(message) + " data length " + data.length);
        synchronized (this.mTransports) {
            for (int i = 0; i < associationIds.length; i++) {
                if (this.mTransports.contains(associationIds[i])) {
                    this.mTransports.get(associationIds[i]).sendMessage(message, data);
                }
            }
        }
    }

    public void attachSystemDataTransport(int associationId, android.os.ParcelFileDescriptor fd) {
        android.util.Slog.i(TAG, "Attaching transport for association id=[" + associationId + "]...");
        this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        synchronized (this.mTransports) {
            if (this.mTransports.contains(associationId)) {
                detachSystemDataTransport(associationId);
            }
            initializeTransport(associationId, fd, null);
            notifyOnTransportsChanged();
        }
        android.util.Slog.i(TAG, "Transport attached.");
    }

    public void detachSystemDataTransport(int associationId) {
        android.util.Slog.i(TAG, "Detaching transport for association id=[" + associationId + "]...");
        this.mAssociationStore.getAssociationWithCallerChecks(associationId);
        synchronized (this.mTransports) {
            com.android.server.companion.transport.Transport transport = (com.android.server.companion.transport.Transport) this.mTransports.removeReturnOld(associationId);
            if (transport == null) {
                return;
            }
            transport.stop();
            notifyOnTransportsChanged();
            android.util.Slog.i(TAG, "Transport detached.");
        }
    }

    private java.util.List<android.companion.AssociationInfo> getAssociationsWithTransport() {
        java.util.List<android.companion.AssociationInfo> associations = new java.util.ArrayList<>();
        synchronized (this.mTransports) {
            for (int i = 0; i < this.mTransports.size(); i++) {
                android.companion.AssociationInfo association = this.mAssociationStore.getAssociationById(this.mTransports.keyAt(i));
                if (association != null) {
                    associations.add(association);
                }
            }
        }
        return associations;
    }

    private void notifyOnTransportsChanged() {
        synchronized (this.mTransportsListeners) {
            this.mTransportsListeners.broadcast(new java.util.function.Consumer() { // from class: com.android.server.companion.transport.CompanionTransportManager$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$notifyOnTransportsChanged$1((android.companion.IOnTransportsChangedListener) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyOnTransportsChanged$1(android.companion.IOnTransportsChangedListener listener) {
        try {
            listener.onTransportsChanged(getAssociationsWithTransport());
        } catch (android.os.RemoteException e) {
        }
    }

    private void initializeTransport(int associationId, android.os.ParcelFileDescriptor fd, byte[] preSharedKey) {
        com.android.server.companion.transport.Transport transport;
        android.util.Slog.i(TAG, "Initializing transport");
        if (!isSecureTransportEnabled()) {
            android.util.Slog.i(TAG, "Secure channel is disabled. Creating raw transport");
            transport = new com.android.server.companion.transport.RawTransport(associationId, fd, this.mContext);
        } else if (android.os.Build.isDebuggable()) {
            android.util.Slog.d(TAG, "Creating an unauthenticated secure channel");
            byte[] testKey = "CDM".getBytes(java.nio.charset.StandardCharsets.UTF_8);
            transport = new com.android.server.companion.transport.SecureTransport(associationId, fd, this.mContext, testKey, null);
        } else if (preSharedKey != null) {
            android.util.Slog.d(TAG, "Creating a PSK-authenticated secure channel");
            transport = new com.android.server.companion.transport.SecureTransport(associationId, fd, this.mContext, preSharedKey, null);
        } else {
            android.util.Slog.d(TAG, "Creating a secure channel");
            transport = new com.android.server.companion.transport.SecureTransport(associationId, fd, this.mContext);
        }
        addMessageListenersToTransport(transport);
        transport.setOnTransportClosedListener(new com.android.server.companion.transport.Transport.OnTransportClosedListener() { // from class: com.android.server.companion.transport.CompanionTransportManager$$ExternalSyntheticLambda2
            @Override // com.android.server.companion.transport.Transport.OnTransportClosedListener
            public final void onClosed(com.android.server.companion.transport.Transport transport2) {
                this.f$0.detachSystemDataTransport(transport2);
            }
        });
        transport.start();
        synchronized (this.mTransports) {
            this.mTransports.put(associationId, transport);
        }
    }

    public java.util.concurrent.Future<?> requestPermissionRestore(int associationId, byte[] data) {
        synchronized (this.mTransports) {
            com.android.server.companion.transport.Transport transport = this.mTransports.get(associationId);
            if (transport == null) {
                return java.util.concurrent.CompletableFuture.failedFuture(new java.io.IOException("Missing transport"));
            }
            return transport.sendMessage(1669491075, data);
        }
    }

    public void dump(java.io.PrintWriter out) {
        synchronized (this.mTransports) {
            out.append("System Data Transports: ");
            if (this.mTransports.size() == 0) {
                out.append("<empty>\n");
            } else {
                out.append("\n");
                for (int i = 0; i < this.mTransports.size(); i++) {
                    int associationId = this.mTransports.keyAt(i);
                    com.android.server.companion.transport.Transport transport = this.mTransports.get(associationId);
                    out.append("  ").append((java.lang.CharSequence) transport.toString()).append('\n');
                }
            }
        }
    }

    public void enableSecureTransport(boolean enabled) {
        this.mSecureTransportEnabled = enabled;
    }

    public com.android.server.companion.transport.CompanionTransportManager.EmulatedTransport createEmulatedTransport(int associationId) {
        com.android.server.companion.transport.CompanionTransportManager.EmulatedTransport transport;
        synchronized (this.mTransports) {
            java.io.FileDescriptor fd = new java.io.FileDescriptor();
            android.os.ParcelFileDescriptor pfd = new android.os.ParcelFileDescriptor(fd);
            transport = new com.android.server.companion.transport.CompanionTransportManager.EmulatedTransport(associationId, pfd, this.mContext);
            addMessageListenersToTransport(transport);
            this.mTransports.put(associationId, transport);
            notifyOnTransportsChanged();
        }
        return transport;
    }

    public static class EmulatedTransport extends com.android.server.companion.transport.RawTransport {
        @Override // com.android.server.companion.transport.RawTransport
        public /* bridge */ /* synthetic */ java.lang.String toString() {
            return super.toString();
        }

        EmulatedTransport(int associationId, android.os.ParcelFileDescriptor fd, android.content.Context context) {
            super(associationId, fd, context);
        }

        public void processMessage(int messageType, int sequence, byte[] data) throws java.io.IOException {
            handleMessage(messageType, sequence, data);
        }

        @Override // com.android.server.companion.transport.RawTransport, com.android.server.companion.transport.Transport
        protected void sendMessage(int messageType, int sequence, byte[] data) throws java.io.IOException {
            android.util.Slog.e("CDM_CompanionTransport", "Black-holing emulated message type 0x" + java.lang.Integer.toHexString(messageType) + " sequence " + sequence + " length " + data.length + " to association " + this.mAssociationId);
        }
    }

    private boolean isSecureTransportEnabled() {
        return this.mSecureTransportEnabled;
    }

    private void addMessageListenersToTransport(com.android.server.companion.transport.Transport transport) {
        for (int i = 0; i < this.mMessageListeners.size(); i++) {
            transport.addListener(this.mMessageListeners.keyAt(i), this.mMessageListeners.valueAt(i));
        }
    }

    void detachSystemDataTransport(com.android.server.companion.transport.Transport transport) {
        int associationId = transport.mAssociationId;
        android.companion.AssociationInfo association = this.mAssociationStore.getAssociationById(associationId);
        if (association != null) {
            detachSystemDataTransport(association.getId());
        }
    }
}
