package android.net;

/* JADX INFO: loaded from: classes.dex */
public abstract class IpMemoryStoreClient {
    private static final java.lang.String TAG = android.net.IpMemoryStoreClient.class.getSimpleName();
    private final android.content.Context mContext;

    /* JADX INFO: Access modifiers changed from: private */
    @java.lang.FunctionalInterface
    interface ThrowingRunnable {
        void run() throws android.os.RemoteException;
    }

    protected abstract void runWhenServiceReady(java.util.function.Consumer<android.net.IIpMemoryStore> consumer) throws java.util.concurrent.ExecutionException;

    public IpMemoryStoreClient(android.content.Context context) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("missing context");
        }
        this.mContext = context;
    }

    private void ignoringRemoteException(android.net.IpMemoryStoreClient.ThrowingRunnable r) {
        ignoringRemoteException("Failed to execute remote procedure call", r);
    }

    private void ignoringRemoteException(java.lang.String message, android.net.IpMemoryStoreClient.ThrowingRunnable r) {
        try {
            r.run();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, message, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$storeNetworkAttributes$1(final java.lang.String l2Key, final android.net.ipmemorystore.NetworkAttributes attributes, final android.net.ipmemorystore.OnStatusListener listener, final android.net.IIpMemoryStore service) {
        ignoringRemoteException(new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda12
            @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
            public final void run() throws android.os.RemoteException {
                service.storeNetworkAttributes(l2Key, attributes.toParcelable(), android.net.ipmemorystore.OnStatusListener.toAIDL(listener));
            }
        });
    }

    public void storeNetworkAttributes(final java.lang.String l2Key, final android.net.ipmemorystore.NetworkAttributes attributes, final android.net.ipmemorystore.OnStatusListener listener) {
        try {
            runWhenServiceReady(new java.util.function.Consumer() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$storeNetworkAttributes$1(l2Key, attributes, listener, (android.net.IIpMemoryStore) obj);
                }
            });
        } catch (java.util.concurrent.ExecutionException e) {
            if (listener == null) {
                return;
            }
            ignoringRemoteException("Error storing network attributes", new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda1
                @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
                public final void run() {
                    listener.onComplete(new android.net.ipmemorystore.Status(-5));
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$storeBlob$4(final java.lang.String l2Key, final java.lang.String clientId, final java.lang.String name, final android.net.ipmemorystore.Blob data, final android.net.ipmemorystore.OnStatusListener listener, final android.net.IIpMemoryStore service) {
        ignoringRemoteException(new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda19
            @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
            public final void run() throws android.os.RemoteException {
                service.storeBlob(l2Key, clientId, name, data, android.net.ipmemorystore.OnStatusListener.toAIDL(listener));
            }
        });
    }

    public void storeBlob(final java.lang.String l2Key, final java.lang.String clientId, final java.lang.String name, final android.net.ipmemorystore.Blob data, final android.net.ipmemorystore.OnStatusListener listener) {
        try {
            runWhenServiceReady(new java.util.function.Consumer() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda17
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$storeBlob$4(l2Key, clientId, name, data, listener, (android.net.IIpMemoryStore) obj);
                }
            });
        } catch (java.util.concurrent.ExecutionException e) {
            if (listener == null) {
                return;
            }
            ignoringRemoteException("Error storing blob", new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda18
                @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
                public final void run() {
                    listener.onComplete(new android.net.ipmemorystore.Status(-5));
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$findL2Key$7(final android.net.ipmemorystore.NetworkAttributes attributes, final android.net.ipmemorystore.OnL2KeyResponseListener listener, final android.net.IIpMemoryStore service) {
        ignoringRemoteException(new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda9
            @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
            public final void run() throws android.os.RemoteException {
                service.findL2Key(attributes.toParcelable(), android.net.ipmemorystore.OnL2KeyResponseListener.toAIDL(listener));
            }
        });
    }

    public void findL2Key(final android.net.ipmemorystore.NetworkAttributes attributes, final android.net.ipmemorystore.OnL2KeyResponseListener listener) {
        try {
            runWhenServiceReady(new java.util.function.Consumer() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda20
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$findL2Key$7(attributes, listener, (android.net.IIpMemoryStore) obj);
                }
            });
        } catch (java.util.concurrent.ExecutionException e) {
            ignoringRemoteException("Error finding L2 Key", new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda21
                @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
                public final void run() {
                    listener.onL2KeyResponse(new android.net.ipmemorystore.Status(-5), null);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$isSameNetwork$10(final java.lang.String l2Key1, final java.lang.String l2Key2, final android.net.ipmemorystore.OnSameL3NetworkResponseListener listener, final android.net.IIpMemoryStore service) {
        ignoringRemoteException(new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda15
            @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
            public final void run() throws android.os.RemoteException {
                service.isSameNetwork(l2Key1, l2Key2, android.net.ipmemorystore.OnSameL3NetworkResponseListener.toAIDL(listener));
            }
        });
    }

    public void isSameNetwork(final java.lang.String l2Key1, final java.lang.String l2Key2, final android.net.ipmemorystore.OnSameL3NetworkResponseListener listener) {
        try {
            runWhenServiceReady(new java.util.function.Consumer() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$isSameNetwork$10(l2Key1, l2Key2, listener, (android.net.IIpMemoryStore) obj);
                }
            });
        } catch (java.util.concurrent.ExecutionException e) {
            ignoringRemoteException("Error checking for network sameness", new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda5
                @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
                public final void run() {
                    listener.onSameL3NetworkResponse(new android.net.ipmemorystore.Status(-5), null);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$retrieveNetworkAttributes$13(final java.lang.String l2Key, final android.net.ipmemorystore.OnNetworkAttributesRetrievedListener listener, final android.net.IIpMemoryStore service) {
        ignoringRemoteException(new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda6
            @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
            public final void run() throws android.os.RemoteException {
                service.retrieveNetworkAttributes(l2Key, android.net.ipmemorystore.OnNetworkAttributesRetrievedListener.toAIDL(listener));
            }
        });
    }

    public void retrieveNetworkAttributes(final java.lang.String l2Key, final android.net.ipmemorystore.OnNetworkAttributesRetrievedListener listener) {
        try {
            runWhenServiceReady(new java.util.function.Consumer() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda24
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$retrieveNetworkAttributes$13(l2Key, listener, (android.net.IIpMemoryStore) obj);
                }
            });
        } catch (java.util.concurrent.ExecutionException e) {
            ignoringRemoteException("Error retrieving network attributes", new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda25
                @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
                public final void run() {
                    listener.onNetworkAttributesRetrieved(new android.net.ipmemorystore.Status(-5), null, null);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$retrieveBlob$16(final java.lang.String l2Key, final java.lang.String clientId, final java.lang.String name, final android.net.ipmemorystore.OnBlobRetrievedListener listener, final android.net.IIpMemoryStore service) {
        ignoringRemoteException(new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda13
            @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
            public final void run() throws android.os.RemoteException {
                service.retrieveBlob(l2Key, clientId, name, android.net.ipmemorystore.OnBlobRetrievedListener.toAIDL(listener));
            }
        });
    }

    public void retrieveBlob(final java.lang.String l2Key, final java.lang.String clientId, final java.lang.String name, final android.net.ipmemorystore.OnBlobRetrievedListener listener) {
        try {
            runWhenServiceReady(new java.util.function.Consumer() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$retrieveBlob$16(l2Key, clientId, name, listener, (android.net.IIpMemoryStore) obj);
                }
            });
        } catch (java.util.concurrent.ExecutionException e) {
            ignoringRemoteException("Error retrieving blob", new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda8
                @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
                public final void run() {
                    listener.onBlobRetrieved(new android.net.ipmemorystore.Status(-5), null, null, null);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$delete$19(final java.lang.String l2Key, final boolean needWipe, final android.net.ipmemorystore.OnDeleteStatusListener listener, final android.net.IIpMemoryStore service) {
        ignoringRemoteException(new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda10
            @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
            public final void run() throws android.os.RemoteException {
                service.delete(l2Key, needWipe, android.net.ipmemorystore.OnDeleteStatusListener.toAIDL(listener));
            }
        });
    }

    public void delete(final java.lang.String l2Key, final boolean needWipe, final android.net.ipmemorystore.OnDeleteStatusListener listener) {
        try {
            runWhenServiceReady(new java.util.function.Consumer() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$delete$19(l2Key, needWipe, listener, (android.net.IIpMemoryStore) obj);
                }
            });
        } catch (java.util.concurrent.ExecutionException e) {
            if (listener == null) {
                return;
            }
            ignoringRemoteException("Error deleting from the memory store", new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda3
                @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
                public final void run() {
                    listener.onComplete(new android.net.ipmemorystore.Status(-5), 0);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteCluster$22(final java.lang.String cluster, final boolean needWipe, final android.net.ipmemorystore.OnDeleteStatusListener listener, final android.net.IIpMemoryStore service) {
        ignoringRemoteException(new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda16
            @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
            public final void run() throws android.os.RemoteException {
                service.deleteCluster(cluster, needWipe, android.net.ipmemorystore.OnDeleteStatusListener.toAIDL(listener));
            }
        });
    }

    public void deleteCluster(final java.lang.String cluster, final boolean needWipe, final android.net.ipmemorystore.OnDeleteStatusListener listener) {
        try {
            runWhenServiceReady(new java.util.function.Consumer() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda22
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$deleteCluster$22(cluster, needWipe, listener, (android.net.IIpMemoryStore) obj);
                }
            });
        } catch (java.util.concurrent.ExecutionException e) {
            if (listener == null) {
                return;
            }
            ignoringRemoteException("Error deleting from the memory store", new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda23
                @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
                public final void run() {
                    listener.onComplete(new android.net.ipmemorystore.Status(-5), 0);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$factoryReset$25(final android.net.IIpMemoryStore service) {
        ignoringRemoteException(new android.net.IpMemoryStoreClient.ThrowingRunnable() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda14
            @Override // android.net.IpMemoryStoreClient.ThrowingRunnable
            public final void run() throws android.os.RemoteException {
                service.factoryReset();
            }
        });
    }

    public void factoryReset() {
        try {
            runWhenServiceReady(new java.util.function.Consumer() { // from class: android.net.IpMemoryStoreClient$$ExternalSyntheticLambda11
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$factoryReset$25((android.net.IIpMemoryStore) obj);
                }
            });
        } catch (java.util.concurrent.ExecutionException m) {
            android.util.Log.e(TAG, "Error executing factory reset", m);
        }
    }
}
