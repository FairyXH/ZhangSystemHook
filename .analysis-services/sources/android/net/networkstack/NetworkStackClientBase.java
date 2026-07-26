package android.net.networkstack;

/* JADX INFO: loaded from: classes.dex */
public abstract class NetworkStackClientBase {
    private android.net.INetworkStackConnector mConnector;
    private final java.util.ArrayList<java.util.function.Consumer<android.net.INetworkStackConnector>> mPendingNetStackRequests = new java.util.ArrayList<>();

    public void makeDhcpServer(final java.lang.String ifName, final android.net.dhcp.DhcpServingParamsParcel params, final android.net.dhcp.IDhcpServerCallbacks cb) {
        requestConnector(new java.util.function.Consumer() { // from class: android.net.networkstack.NetworkStackClientBase$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                android.net.networkstack.NetworkStackClientBase.lambda$makeDhcpServer$0(ifName, params, cb, (android.net.INetworkStackConnector) obj);
            }
        });
    }

    static /* synthetic */ void lambda$makeDhcpServer$0(java.lang.String ifName, android.net.dhcp.DhcpServingParamsParcel params, android.net.dhcp.IDhcpServerCallbacks cb, android.net.INetworkStackConnector connector) {
        try {
            connector.makeDhcpServer(ifName, params, cb);
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Could not create DhcpServer", e);
        }
    }

    public void makeIpClient(final java.lang.String ifName, final android.net.ip.IIpClientCallbacks cb) {
        requestConnector(new java.util.function.Consumer() { // from class: android.net.networkstack.NetworkStackClientBase$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                android.net.networkstack.NetworkStackClientBase.lambda$makeIpClient$1(ifName, cb, (android.net.INetworkStackConnector) obj);
            }
        });
    }

    static /* synthetic */ void lambda$makeIpClient$1(java.lang.String ifName, android.net.ip.IIpClientCallbacks cb, android.net.INetworkStackConnector connector) {
        try {
            connector.makeIpClient(ifName, cb);
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Could not create IpClient", e);
        }
    }

    public void makeNetworkMonitor(final android.net.Network network, final java.lang.String name, final android.net.INetworkMonitorCallbacks cb) {
        requestConnector(new java.util.function.Consumer() { // from class: android.net.networkstack.NetworkStackClientBase$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                android.net.networkstack.NetworkStackClientBase.lambda$makeNetworkMonitor$2(network, name, cb, (android.net.INetworkStackConnector) obj);
            }
        });
    }

    static /* synthetic */ void lambda$makeNetworkMonitor$2(android.net.Network network, java.lang.String name, android.net.INetworkMonitorCallbacks cb, android.net.INetworkStackConnector connector) {
        try {
            connector.makeNetworkMonitor(network, name, cb);
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Could not create NetworkMonitor", e);
        }
    }

    public void fetchIpMemoryStore(final android.net.IIpMemoryStoreCallbacks cb) {
        requestConnector(new java.util.function.Consumer() { // from class: android.net.networkstack.NetworkStackClientBase$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                android.net.networkstack.NetworkStackClientBase.lambda$fetchIpMemoryStore$3(cb, (android.net.INetworkStackConnector) obj);
            }
        });
    }

    static /* synthetic */ void lambda$fetchIpMemoryStore$3(android.net.IIpMemoryStoreCallbacks cb, android.net.INetworkStackConnector connector) {
        try {
            connector.fetchIpMemoryStore(cb);
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Could not fetch IpMemoryStore", e);
        }
    }

    protected void requestConnector(java.util.function.Consumer<android.net.INetworkStackConnector> request) {
        synchronized (this.mPendingNetStackRequests) {
            android.net.INetworkStackConnector connector = this.mConnector;
            if (connector == null) {
                this.mPendingNetStackRequests.add(request);
            } else {
                request.accept(connector);
            }
        }
    }

    protected void onNetworkStackConnected(android.net.INetworkStackConnector connector) {
        java.util.ArrayList<java.util.function.Consumer<android.net.INetworkStackConnector>> requests;
        while (true) {
            synchronized (this.mPendingNetStackRequests) {
                requests = new java.util.ArrayList<>(this.mPendingNetStackRequests);
                this.mPendingNetStackRequests.clear();
            }
            for (java.util.function.Consumer<android.net.INetworkStackConnector> consumer : requests) {
                consumer.accept(connector);
            }
            synchronized (this.mPendingNetStackRequests) {
                if (this.mPendingNetStackRequests.size() == 0) {
                    this.mConnector = connector;
                    return;
                }
            }
        }
    }

    protected int getQueueLength() {
        int size;
        synchronized (this.mPendingNetStackRequests) {
            size = this.mPendingNetStackRequests.size();
        }
        return size;
    }
}
