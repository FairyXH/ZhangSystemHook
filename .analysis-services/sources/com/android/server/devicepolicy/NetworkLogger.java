package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class NetworkLogger {
    private static final java.lang.String TAG = com.android.server.devicepolicy.NetworkLogger.class.getSimpleName();
    private final com.android.server.devicepolicy.DevicePolicyManagerService mDpm;
    private com.android.server.ServiceThread mHandlerThread;
    private android.net.IIpConnectivityMetrics mIpConnectivityMetrics;
    private final java.util.concurrent.atomic.AtomicBoolean mIsLoggingEnabled = new java.util.concurrent.atomic.AtomicBoolean(false);
    private final android.net.INetdEventCallback mNetdEventCallback = new com.android.server.net.BaseNetdEventCallback() { // from class: com.android.server.devicepolicy.NetworkLogger.1
        public void onDnsEvent(int netId, int eventType, int returnCode, java.lang.String hostname, java.lang.String[] ipAddresses, int ipAddressesCount, long timestamp, int uid) {
            if (!com.android.server.devicepolicy.NetworkLogger.this.mIsLoggingEnabled.get() || !shouldLogNetworkEvent(uid)) {
                return;
            }
            android.app.admin.DnsEvent dnsEvent = new android.app.admin.DnsEvent(hostname, ipAddresses, ipAddressesCount, com.android.server.devicepolicy.NetworkLogger.this.mPm.getNameForUid(uid), timestamp);
            sendNetworkEvent(dnsEvent);
        }

        public void onConnectEvent(java.lang.String ipAddr, int port, long timestamp, int uid) {
            if (!com.android.server.devicepolicy.NetworkLogger.this.mIsLoggingEnabled.get() || !shouldLogNetworkEvent(uid)) {
                return;
            }
            android.app.admin.ConnectEvent connectEvent = new android.app.admin.ConnectEvent(ipAddr, port, com.android.server.devicepolicy.NetworkLogger.this.mPm.getNameForUid(uid), timestamp);
            sendNetworkEvent(connectEvent);
        }

        private void sendNetworkEvent(android.app.admin.NetworkEvent event) {
            android.os.Message msg = com.android.server.devicepolicy.NetworkLogger.this.mNetworkLoggingHandler.obtainMessage(1);
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putParcelable("network_event", event);
            msg.setData(bundle);
            com.android.server.devicepolicy.NetworkLogger.this.mNetworkLoggingHandler.sendMessage(msg);
        }

        private boolean shouldLogNetworkEvent(int uid) {
            return com.android.server.devicepolicy.NetworkLogger.this.mTargetUserId == -1 || com.android.server.devicepolicy.NetworkLogger.this.mTargetUserId == android.os.UserHandle.getUserId(uid);
        }
    };
    private com.android.server.devicepolicy.NetworkLoggingHandler mNetworkLoggingHandler;
    private final android.content.pm.PackageManagerInternal mPm;
    private final int mTargetUserId;

    NetworkLogger(com.android.server.devicepolicy.DevicePolicyManagerService dpm, android.content.pm.PackageManagerInternal pm, int targetUserId) {
        this.mDpm = dpm;
        this.mPm = pm;
        this.mTargetUserId = targetUserId;
    }

    private boolean checkIpConnectivityMetricsService() {
        if (this.mIpConnectivityMetrics != null) {
            return true;
        }
        android.net.IIpConnectivityMetrics service = this.mDpm.mInjector.getIIpConnectivityMetrics();
        if (service == null) {
            return false;
        }
        this.mIpConnectivityMetrics = service;
        return true;
    }

    boolean startNetworkLogging() {
        android.util.Log.d(TAG, "Starting network logging.");
        if (!checkIpConnectivityMetricsService()) {
            android.util.Slog.wtf(TAG, "Failed to register callback with IIpConnectivityMetrics.");
            return false;
        }
        try {
            if (!this.mIpConnectivityMetrics.addNetdEventCallback(1, this.mNetdEventCallback)) {
                return false;
            }
            this.mHandlerThread = new com.android.server.ServiceThread(TAG, 10, false);
            this.mHandlerThread.start();
            this.mNetworkLoggingHandler = new com.android.server.devicepolicy.NetworkLoggingHandler(this.mHandlerThread.getLooper(), this.mDpm, this.mTargetUserId);
            this.mNetworkLoggingHandler.scheduleBatchFinalization();
            this.mIsLoggingEnabled.set(true);
            return true;
        } catch (android.os.RemoteException re) {
            android.util.Slog.wtf(TAG, "Failed to make remote calls to register the callback", re);
            return false;
        }
    }

    boolean stopNetworkLogging() {
        android.util.Log.d(TAG, "Stopping network logging");
        this.mIsLoggingEnabled.set(false);
        discardLogs();
        try {
            try {
                if (checkIpConnectivityMetricsService()) {
                    boolean zRemoveNetdEventCallback = this.mIpConnectivityMetrics.removeNetdEventCallback(1);
                    if (this.mHandlerThread != null) {
                        this.mHandlerThread.quitSafely();
                    }
                    return zRemoveNetdEventCallback;
                }
                android.util.Slog.wtf(TAG, "Failed to unregister callback with IIpConnectivityMetrics.");
                if (this.mHandlerThread != null) {
                    this.mHandlerThread.quitSafely();
                }
                return true;
            } catch (android.os.RemoteException re) {
                android.util.Slog.wtf(TAG, "Failed to make remote calls to unregister the callback", re);
                if (this.mHandlerThread != null) {
                    this.mHandlerThread.quitSafely();
                }
                return true;
            }
        } catch (java.lang.Throwable th) {
            if (this.mHandlerThread != null) {
                this.mHandlerThread.quitSafely();
            }
            throw th;
        }
    }

    void pause() {
        if (this.mNetworkLoggingHandler != null) {
            this.mNetworkLoggingHandler.pause();
        }
    }

    void resume() {
        if (this.mNetworkLoggingHandler != null) {
            this.mNetworkLoggingHandler.resume();
        }
    }

    void discardLogs() {
        if (this.mNetworkLoggingHandler != null) {
            this.mNetworkLoggingHandler.discardLogs();
        }
    }

    java.util.List<android.app.admin.NetworkEvent> retrieveLogs(long batchToken) {
        return this.mNetworkLoggingHandler.retrieveFullLogBatch(batchToken);
    }

    long forceBatchFinalization() {
        return this.mNetworkLoggingHandler.forceBatchFinalization();
    }
}
