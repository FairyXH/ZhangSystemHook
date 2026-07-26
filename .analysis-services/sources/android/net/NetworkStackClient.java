package android.net;

/* JADX INFO: loaded from: classes.dex */
public class NetworkStackClient {
    private static final int NETWORKSTACK_TIMEOUT_MS = 10000;
    private static final java.lang.String TAG = android.net.NetworkStackClient.class.getSimpleName();
    private static android.net.NetworkStackClient sInstance;
    private android.net.INetworkStackConnector mConnector;
    private final android.net.NetworkStackClient.Dependencies mDependencies;
    private final java.util.ArrayList<android.net.NetworkStackClient.NetworkStackCallback> mPendingNetStackRequests;
    private volatile boolean mWasSystemServerInitialized;

    protected interface Dependencies {
        void addToServiceManager(android.os.IBinder iBinder);

        void checkCallerUid();

        android.net.ConnectivityModuleConnector getConnectivityModuleConnector();
    }

    /* JADX INFO: Access modifiers changed from: private */
    interface NetworkStackCallback {
        void onNetworkStackConnected(android.net.INetworkStackConnector iNetworkStackConnector);
    }

    protected NetworkStackClient(android.net.NetworkStackClient.Dependencies dependencies) {
        this.mPendingNetStackRequests = new java.util.ArrayList<>();
        this.mWasSystemServerInitialized = false;
        this.mDependencies = dependencies;
    }

    private NetworkStackClient() {
        this(new android.net.NetworkStackClient.DependenciesImpl());
    }

    private static class DependenciesImpl implements android.net.NetworkStackClient.Dependencies {
        private DependenciesImpl() {
        }

        @Override // android.net.NetworkStackClient.Dependencies
        public void addToServiceManager(android.os.IBinder service) {
            android.os.ServiceManager.addService("network_stack", service, false, 6);
        }

        @Override // android.net.NetworkStackClient.Dependencies
        public void checkCallerUid() {
            int caller = android.os.Binder.getCallingUid();
            if (caller != 1000 && caller != 1073 && android.os.UserHandle.getAppId(caller) != 1002) {
                throw new java.lang.SecurityException("Only the system server should try to bind to the network stack.");
            }
        }

        @Override // android.net.NetworkStackClient.Dependencies
        public android.net.ConnectivityModuleConnector getConnectivityModuleConnector() {
            return android.net.ConnectivityModuleConnector.getInstance();
        }
    }

    public static synchronized android.net.NetworkStackClient getInstance() {
        if (sInstance == null) {
            sInstance = new android.net.NetworkStackClient();
        }
        return sInstance;
    }

    public void makeDhcpServer(final java.lang.String ifName, final android.net.dhcp.DhcpServingParamsParcel params, final android.net.dhcp.IDhcpServerCallbacks cb) {
        requestConnector(new android.net.NetworkStackClient.NetworkStackCallback() { // from class: android.net.NetworkStackClient$$ExternalSyntheticLambda3
            @Override // android.net.NetworkStackClient.NetworkStackCallback
            public final void onNetworkStackConnected(android.net.INetworkStackConnector iNetworkStackConnector) {
                android.net.NetworkStackClient.lambda$makeDhcpServer$0(ifName, params, cb, iNetworkStackConnector);
            }
        });
    }

    static /* synthetic */ void lambda$makeDhcpServer$0(java.lang.String ifName, android.net.dhcp.DhcpServingParamsParcel params, android.net.dhcp.IDhcpServerCallbacks cb, android.net.INetworkStackConnector connector) {
        try {
            connector.makeDhcpServer(ifName, params, cb);
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void makeIpClient(final java.lang.String ifName, final android.net.ip.IIpClientCallbacks cb) {
        requestConnector(new android.net.NetworkStackClient.NetworkStackCallback() { // from class: android.net.NetworkStackClient$$ExternalSyntheticLambda0
            @Override // android.net.NetworkStackClient.NetworkStackCallback
            public final void onNetworkStackConnected(android.net.INetworkStackConnector iNetworkStackConnector) {
                android.net.NetworkStackClient.lambda$makeIpClient$1(ifName, cb, iNetworkStackConnector);
            }
        });
    }

    static /* synthetic */ void lambda$makeIpClient$1(java.lang.String ifName, android.net.ip.IIpClientCallbacks cb, android.net.INetworkStackConnector connector) {
        try {
            connector.makeIpClient(ifName, cb);
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void makeNetworkMonitor(final android.net.Network network, final java.lang.String name, final android.net.INetworkMonitorCallbacks cb) {
        requestConnector(new android.net.NetworkStackClient.NetworkStackCallback() { // from class: android.net.NetworkStackClient$$ExternalSyntheticLambda1
            @Override // android.net.NetworkStackClient.NetworkStackCallback
            public final void onNetworkStackConnected(android.net.INetworkStackConnector iNetworkStackConnector) {
                android.net.NetworkStackClient.lambda$makeNetworkMonitor$2(network, name, cb, iNetworkStackConnector);
            }
        });
    }

    static /* synthetic */ void lambda$makeNetworkMonitor$2(android.net.Network network, java.lang.String name, android.net.INetworkMonitorCallbacks cb, android.net.INetworkStackConnector connector) {
        try {
            connector.makeNetworkMonitor(network, name, cb);
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void fetchIpMemoryStore(final android.net.IIpMemoryStoreCallbacks cb) {
        requestConnector(new android.net.NetworkStackClient.NetworkStackCallback() { // from class: android.net.NetworkStackClient$$ExternalSyntheticLambda2
            @Override // android.net.NetworkStackClient.NetworkStackCallback
            public final void onNetworkStackConnected(android.net.INetworkStackConnector iNetworkStackConnector) {
                android.net.NetworkStackClient.lambda$fetchIpMemoryStore$3(cb, iNetworkStackConnector);
            }
        });
    }

    static /* synthetic */ void lambda$fetchIpMemoryStore$3(android.net.IIpMemoryStoreCallbacks cb, android.net.INetworkStackConnector connector) {
        try {
            connector.fetchIpMemoryStore(cb);
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    private class NetworkStackConnection implements android.net.ConnectivityModuleConnector.ModuleServiceCallback {
        private NetworkStackConnection() {
        }

        @Override // android.net.ConnectivityModuleConnector.ModuleServiceCallback
        public void onModuleServiceConnected(android.os.IBinder service) {
            android.net.NetworkStackClient.this.logi("Network stack service connected");
            android.net.NetworkStackClient.this.registerNetworkStackService(service);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerNetworkStackService(android.os.IBinder service) {
        java.util.ArrayList<android.net.NetworkStackClient.NetworkStackCallback> requests;
        android.net.INetworkStackConnector connector = android.net.INetworkStackConnector.Stub.asInterface(service);
        this.mDependencies.addToServiceManager(service);
        log("Network stack service registered");
        synchronized (this.mPendingNetStackRequests) {
            requests = new java.util.ArrayList<>(this.mPendingNetStackRequests);
            this.mPendingNetStackRequests.clear();
            this.mConnector = connector;
        }
        for (android.net.NetworkStackClient.NetworkStackCallback r : requests) {
            r.onNetworkStackConnected(connector);
        }
    }

    public void init() {
        log("Network stack init");
        this.mWasSystemServerInitialized = true;
    }

    public void start() {
        this.mDependencies.getConnectivityModuleConnector().startModuleService(android.net.INetworkStackConnector.class.getName(), "android.permission.MAINLINE_NETWORK_STACK", new android.net.NetworkStackClient.NetworkStackConnection());
        log("Network stack service start requested");
    }

    private void log(java.lang.String message) {
        android.util.Log.d(TAG, message);
    }

    private void logWtf(java.lang.String message, java.lang.Throwable e) {
        android.util.Slog.wtf(TAG, message);
        android.util.Log.e(TAG, message, e);
    }

    private void loge(java.lang.String message, java.lang.Throwable e) {
        android.util.Log.e(TAG, message, e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logi(java.lang.String message) {
        android.util.Log.i(TAG, message);
    }

    private android.net.INetworkStackConnector getRemoteConnector() {
        try {
            long before = java.lang.System.currentTimeMillis();
            do {
                android.os.IBinder connector = android.os.ServiceManager.getService("network_stack");
                if (connector == null) {
                    java.lang.Thread.sleep(20L);
                } else {
                    return android.net.INetworkStackConnector.Stub.asInterface(connector);
                }
            } while (java.lang.System.currentTimeMillis() - before <= 10000);
            loge("Timeout waiting for NetworkStack connector", null);
            return null;
        } catch (java.lang.InterruptedException e) {
            loge("Error waiting for NetworkStack connector", e);
            return null;
        }
    }

    private void requestConnector(android.net.NetworkStackClient.NetworkStackCallback request) {
        this.mDependencies.checkCallerUid();
        if (!this.mWasSystemServerInitialized) {
            android.net.INetworkStackConnector connector = getRemoteConnector();
            synchronized (this.mPendingNetStackRequests) {
                this.mConnector = connector;
            }
            request.onNetworkStackConnected(connector);
            return;
        }
        synchronized (this.mPendingNetStackRequests) {
            android.net.INetworkStackConnector connector2 = this.mConnector;
            if (connector2 == null) {
                this.mPendingNetStackRequests.add(request);
            } else {
                request.onNetworkStackConnected(connector2);
            }
        }
    }
}
