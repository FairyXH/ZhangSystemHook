package com.android.server.net.watchlist;

/* JADX INFO: loaded from: classes2.dex */
public class NetworkWatchlistService extends com.android.internal.net.INetworkWatchlistManager.Stub {
    static final boolean DEBUG = false;
    private static final int MAX_NUM_OF_WATCHLIST_DIGESTS = 10000;
    private static final java.lang.String TAG = com.android.server.net.watchlist.NetworkWatchlistService.class.getSimpleName();
    private final com.android.server.net.watchlist.WatchlistConfig mConfig;
    private final android.content.Context mContext;
    private final com.android.server.ServiceThread mHandlerThread;
    android.net.IIpConnectivityMetrics mIpConnectivityMetrics;
    private volatile boolean mIsLoggingEnabled;
    private final java.lang.Object mLoggingSwitchLock;
    private final android.net.INetdEventCallback mNetdEventCallback;
    com.android.server.net.watchlist.WatchlistLoggingHandler mNetworkWatchlistHandler;

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.net.watchlist.NetworkWatchlistService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            if (android.provider.Settings.Global.getInt(getContext().getContentResolver(), "network_watchlist_enabled", 1) == 0) {
                android.util.Slog.i(com.android.server.net.watchlist.NetworkWatchlistService.TAG, "Network Watchlist service is disabled");
            } else {
                this.mService = new com.android.server.net.watchlist.NetworkWatchlistService(getContext());
                publishBinderService("network_watchlist", this.mService);
            }
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 550) {
                if (android.provider.Settings.Global.getInt(getContext().getContentResolver(), "network_watchlist_enabled", 1) == 0) {
                    android.util.Slog.i(com.android.server.net.watchlist.NetworkWatchlistService.TAG, "Network Watchlist service is disabled");
                    return;
                }
                try {
                    this.mService.init();
                    this.mService.initIpConnectivityMetrics();
                    this.mService.startWatchlistLogging();
                } catch (android.os.RemoteException e) {
                }
                com.android.server.net.watchlist.ReportWatchlistJobService.schedule(getContext());
            }
        }
    }

    public NetworkWatchlistService(android.content.Context context) {
        this.mIsLoggingEnabled = false;
        this.mLoggingSwitchLock = new java.lang.Object();
        this.mNetdEventCallback = new com.android.server.net.BaseNetdEventCallback() { // from class: com.android.server.net.watchlist.NetworkWatchlistService.1
            public void onDnsEvent(int netId, int eventType, int returnCode, java.lang.String hostname, java.lang.String[] ipAddresses, int ipAddressesCount, long timestamp, int uid) {
                if (!com.android.server.net.watchlist.NetworkWatchlistService.this.mIsLoggingEnabled) {
                    return;
                }
                com.android.server.net.watchlist.NetworkWatchlistService.this.mNetworkWatchlistHandler.asyncNetworkEvent(hostname, ipAddresses, uid);
            }

            public void onConnectEvent(java.lang.String ipAddr, int port, long timestamp, int uid) {
                if (!com.android.server.net.watchlist.NetworkWatchlistService.this.mIsLoggingEnabled) {
                    return;
                }
                com.android.server.net.watchlist.NetworkWatchlistService.this.mNetworkWatchlistHandler.asyncNetworkEvent(null, new java.lang.String[]{ipAddr}, uid);
            }
        };
        this.mContext = context;
        this.mConfig = com.android.server.net.watchlist.WatchlistConfig.getInstance();
        this.mHandlerThread = new com.android.server.ServiceThread(TAG, 10, false);
        this.mHandlerThread.start();
        this.mNetworkWatchlistHandler = new com.android.server.net.watchlist.WatchlistLoggingHandler(this.mContext, this.mHandlerThread.getLooper());
        this.mNetworkWatchlistHandler.reportWatchlistIfNecessary();
    }

    NetworkWatchlistService(android.content.Context context, com.android.server.ServiceThread handlerThread, com.android.server.net.watchlist.WatchlistLoggingHandler handler, android.net.IIpConnectivityMetrics ipConnectivityMetrics) {
        this.mIsLoggingEnabled = false;
        this.mLoggingSwitchLock = new java.lang.Object();
        this.mNetdEventCallback = new com.android.server.net.BaseNetdEventCallback() { // from class: com.android.server.net.watchlist.NetworkWatchlistService.1
            public void onDnsEvent(int netId, int eventType, int returnCode, java.lang.String hostname, java.lang.String[] ipAddresses, int ipAddressesCount, long timestamp, int uid) {
                if (!com.android.server.net.watchlist.NetworkWatchlistService.this.mIsLoggingEnabled) {
                    return;
                }
                com.android.server.net.watchlist.NetworkWatchlistService.this.mNetworkWatchlistHandler.asyncNetworkEvent(hostname, ipAddresses, uid);
            }

            public void onConnectEvent(java.lang.String ipAddr, int port, long timestamp, int uid) {
                if (!com.android.server.net.watchlist.NetworkWatchlistService.this.mIsLoggingEnabled) {
                    return;
                }
                com.android.server.net.watchlist.NetworkWatchlistService.this.mNetworkWatchlistHandler.asyncNetworkEvent(null, new java.lang.String[]{ipAddr}, uid);
            }
        };
        this.mContext = context;
        this.mConfig = com.android.server.net.watchlist.WatchlistConfig.getInstance();
        this.mHandlerThread = handlerThread;
        this.mNetworkWatchlistHandler = handler;
        this.mIpConnectivityMetrics = ipConnectivityMetrics;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        this.mConfig.removeTestModeConfig();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initIpConnectivityMetrics() {
        this.mIpConnectivityMetrics = android.net.IIpConnectivityMetrics.Stub.asInterface(android.os.ServiceManager.getService("connmetrics"));
    }

    private boolean isCallerShell() {
        int callingUid = android.os.Binder.getCallingUid();
        return callingUid == 2000 || callingUid == 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        if (!isCallerShell()) {
            android.util.Slog.w(TAG, "Only shell is allowed to call network watchlist shell commands");
        } else {
            new com.android.server.net.watchlist.NetworkWatchlistShellCommand(this, this.mContext).exec(this, in, out, err, args, callback, resultReceiver);
        }
    }

    protected boolean startWatchlistLoggingImpl() throws android.os.RemoteException {
        synchronized (this.mLoggingSwitchLock) {
            if (this.mIsLoggingEnabled) {
                android.util.Slog.w(TAG, "Watchlist logging is already running");
                return true;
            }
            try {
                if (!this.mIpConnectivityMetrics.addNetdEventCallback(2, this.mNetdEventCallback)) {
                    return false;
                }
                this.mIsLoggingEnabled = true;
                return true;
            } catch (android.os.RemoteException e) {
                return false;
            }
        }
    }

    public boolean startWatchlistLogging() throws android.os.RemoteException {
        enforceWatchlistLoggingPermission();
        return startWatchlistLoggingImpl();
    }

    protected boolean stopWatchlistLoggingImpl() {
        synchronized (this.mLoggingSwitchLock) {
            if (!this.mIsLoggingEnabled) {
                android.util.Slog.w(TAG, "Watchlist logging is not running");
                return true;
            }
            this.mIsLoggingEnabled = false;
            try {
                return this.mIpConnectivityMetrics.removeNetdEventCallback(2);
            } catch (android.os.RemoteException e) {
                return false;
            }
        }
    }

    public boolean stopWatchlistLogging() throws android.os.RemoteException {
        enforceWatchlistLoggingPermission();
        return stopWatchlistLoggingImpl();
    }

    public byte[] getWatchlistConfigHash() {
        return this.mConfig.getWatchlistConfigHash();
    }

    private void enforceWatchlistLoggingPermission() {
        int uid = android.os.Binder.getCallingUid();
        if (uid != 1000) {
            throw new java.lang.SecurityException(java.lang.String.format("Uid %d has no permission to change watchlist setting.", java.lang.Integer.valueOf(uid)));
        }
    }

    public void reloadWatchlist() throws android.os.RemoteException {
        enforceWatchlistLoggingPermission();
        android.util.Slog.i(TAG, "Reloading watchlist");
        this.mConfig.reloadConfig();
    }

    public void reportWatchlistIfNecessary() {
        this.mNetworkWatchlistHandler.reportWatchlistIfNecessary();
    }

    public boolean forceReportWatchlistForTest(long lastReportTime) {
        if (this.mConfig.isConfigSecure()) {
            return false;
        }
        this.mNetworkWatchlistHandler.forceReportWatchlistForTest(lastReportTime);
        return true;
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            this.mConfig.dump(fd, pw, args);
        }
    }
}
