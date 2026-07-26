package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public class NetdEventListenerService extends com.android.net.module.util.BaseNetdEventListener {
    private static final int CONNECT_LATENCY_BURST_LIMIT = 5000;
    private static final int CONNECT_LATENCY_FILL_RATE = 15000;
    private static final boolean DBG = false;
    private static final int METRICS_SNAPSHOT_BUFFER_SIZE = 48;
    private static final long METRICS_SNAPSHOT_SPAN_MS = 300000;
    public static final java.lang.String SERVICE_NAME = "netd_listener";
    static final int WAKEUP_EVENT_BUFFER_LENGTH = 1024;
    static final java.lang.String WAKEUP_EVENT_PREFIX_DELIM = ":";
    final com.android.server.connectivity.NetdEventListenerService.TransportForNetIdNetworkCallback mCallback;
    private final android.net.ConnectivityManager mCm;
    private final com.android.internal.util.TokenBucket mConnectTb;
    private android.content.Context mContext;
    private long mLastSnapshot;
    private android.net.INetdEventCallback[] mNetdEventCallbackList;
    private final android.util.SparseArray<android.net.metrics.NetworkMetrics> mNetworkMetrics;
    private final com.android.internal.util.RingBuffer<com.android.server.connectivity.NetdEventListenerService.NetworkMetricsSnapshot> mNetworkMetricsSnapshots;
    private final com.android.internal.util.RingBuffer<android.net.metrics.WakeupEvent> mWakeupEvents;
    private final android.util.ArrayMap<java.lang.String, android.net.metrics.WakeupStats> mWakeupStats;
    private static final java.lang.String TAG = com.android.server.connectivity.NetdEventListenerService.class.getSimpleName();
    private static final int[] ALLOWED_CALLBACK_TYPES = {0, 1, 2};

    public synchronized boolean addNetdEventCallback(int callerType, android.net.INetdEventCallback callback) {
        if (!isValidCallerType(callerType)) {
            android.util.Log.e(TAG, "Invalid caller type: " + callerType);
            return false;
        }
        this.mNetdEventCallbackList[callerType] = callback;
        return true;
    }

    public synchronized boolean removeNetdEventCallback(int callerType) {
        if (!isValidCallerType(callerType)) {
            android.util.Log.e(TAG, "Invalid caller type: " + callerType);
            return false;
        }
        this.mNetdEventCallbackList[callerType] = null;
        return true;
    }

    private static boolean isValidCallerType(int callerType) {
        for (int i = 0; i < ALLOWED_CALLBACK_TYPES.length; i++) {
            if (callerType == ALLOWED_CALLBACK_TYPES[i]) {
                return true;
            }
        }
        return false;
    }

    public NetdEventListenerService(android.content.Context context) {
        this((android.net.ConnectivityManager) context.getSystemService(android.net.ConnectivityManager.class));
        this.mContext = context;
    }

    public NetdEventListenerService(android.net.ConnectivityManager cm) {
        this.mContext = null;
        this.mNetworkMetrics = new android.util.SparseArray<>();
        this.mNetworkMetricsSnapshots = new com.android.internal.util.RingBuffer<>(com.android.server.connectivity.NetdEventListenerService.NetworkMetricsSnapshot.class, 48);
        this.mLastSnapshot = 0L;
        this.mWakeupStats = new android.util.ArrayMap<>();
        this.mWakeupEvents = new com.android.internal.util.RingBuffer<>(android.net.metrics.WakeupEvent.class, 1024);
        this.mConnectTb = new com.android.internal.util.TokenBucket(15000, 5000);
        this.mCallback = new com.android.server.connectivity.NetdEventListenerService.TransportForNetIdNetworkCallback();
        this.mNetdEventCallbackList = new android.net.INetdEventCallback[ALLOWED_CALLBACK_TYPES.length];
        this.mCm = cm;
        this.mCm.registerNetworkCallback(new android.net.NetworkRequest.Builder().clearCapabilities().build(), this.mCallback);
    }

    private static long projectSnapshotTime(long timeMs) {
        return (timeMs / 300000) * 300000;
    }

    private android.net.metrics.NetworkMetrics getMetricsForNetwork(long timeMs, int netId) {
        android.net.metrics.NetworkMetrics metrics = this.mNetworkMetrics.get(netId);
        android.net.NetworkCapabilities nc = this.mCallback.getNetworkCapabilities(netId);
        long transports = nc != null ? com.android.internal.util.BitUtils.packBits(nc.getTransportTypes()) : 0L;
        boolean forceCollect = (metrics == null || nc == null || metrics.transports == transports) ? false : true;
        collectPendingMetricsSnapshot(timeMs, forceCollect);
        if (metrics == null || forceCollect) {
            android.net.metrics.NetworkMetrics metrics2 = new android.net.metrics.NetworkMetrics(netId, transports, this.mConnectTb);
            this.mNetworkMetrics.put(netId, metrics2);
            return metrics2;
        }
        return metrics;
    }

    private com.android.server.connectivity.NetdEventListenerService.NetworkMetricsSnapshot[] getNetworkMetricsSnapshots() {
        collectPendingMetricsSnapshot(java.lang.System.currentTimeMillis(), false);
        return (com.android.server.connectivity.NetdEventListenerService.NetworkMetricsSnapshot[]) this.mNetworkMetricsSnapshots.toArray();
    }

    private void collectPendingMetricsSnapshot(long timeMs, boolean forceCollect) {
        if (!forceCollect && java.lang.Math.abs(timeMs - this.mLastSnapshot) <= 300000) {
            return;
        }
        this.mLastSnapshot = projectSnapshotTime(timeMs);
        com.android.server.connectivity.NetdEventListenerService.NetworkMetricsSnapshot snapshot = com.android.server.connectivity.NetdEventListenerService.NetworkMetricsSnapshot.collect(this.mLastSnapshot, this.mNetworkMetrics);
        if (snapshot.stats.isEmpty()) {
            return;
        }
        this.mNetworkMetricsSnapshots.append(snapshot);
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onDnsEvent(int netId, int eventType, int returnCode, int latencyMs, java.lang.String hostname, java.lang.String[] ipAddresses, int ipAddressesCount, int uid) {
        int i;
        int i2;
        long timestamp = java.lang.System.currentTimeMillis();
        getMetricsForNetwork(timestamp, netId).addDnsResult(eventType, returnCode, latencyMs);
        android.net.INetdEventCallback[] iNetdEventCallbackArr = this.mNetdEventCallbackList;
        int length = iNetdEventCallbackArr.length;
        int i3 = 0;
        while (i3 < length) {
            android.net.INetdEventCallback callback = iNetdEventCallbackArr[i3];
            if (callback == null) {
                i = i3;
                i2 = length;
            } else {
                i = i3;
                i2 = length;
                try {
                    callback.onDnsEvent(netId, eventType, returnCode, hostname, ipAddresses, ipAddressesCount, timestamp, uid);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            i3 = i + 1;
            length = i2;
        }
        if (this.mContext != null) {
            ((com.android.server.IOplusNecConnectMonitor) com.android.server.OplusServiceFactory.getInstance().getFeature(com.android.server.IOplusNecConnectMonitor.DEFAULT, new java.lang.Object[]{this.mContext})).addDnsRecord(netId, returnCode, latencyMs, 0L, 0L);
            ((com.android.server.IOplusNecConnectMonitor) com.android.server.OplusServiceFactory.getInstance().getFeature(com.android.server.IOplusNecConnectMonitor.DEFAULT, new java.lang.Object[]{this.mContext})).onDnsEvent(netId, eventType, returnCode, latencyMs, hostname, ipAddresses, ipAddressesCount, uid);
        }
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onNat64PrefixEvent(int netId, boolean added, java.lang.String prefixString, int prefixLength) {
        for (android.net.INetdEventCallback callback : this.mNetdEventCallbackList) {
            if (callback != null) {
                try {
                    callback.onNat64PrefixEvent(netId, added, prefixString, prefixLength);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onPrivateDnsValidationEvent(int netId, java.lang.String ipAddress, java.lang.String hostname, boolean validated) {
        for (android.net.INetdEventCallback callback : this.mNetdEventCallbackList) {
            if (callback != null) {
                try {
                    callback.onPrivateDnsValidationEvent(netId, ipAddress, hostname, validated);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onConnectEvent(int netId, int error, int latencyMs, java.lang.String ipAddr, int port, int uid) {
        long timestamp = java.lang.System.currentTimeMillis();
        getMetricsForNetwork(timestamp, netId).addConnectResult(error, latencyMs, ipAddr);
        for (android.net.INetdEventCallback callback : this.mNetdEventCallbackList) {
            if (callback != null) {
                try {
                    callback.onConnectEvent(ipAddr, port, timestamp, uid);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
        if (this.mContext != null) {
            ((com.android.server.IOplusNecConnectMonitor) com.android.server.OplusServiceFactory.getInstance().getFeature(com.android.server.IOplusNecConnectMonitor.DEFAULT, new java.lang.Object[]{this.mContext})).onConnectEvent(netId, error, latencyMs, ipAddr, port, uid);
        }
    }

    private boolean hasWifiTransport(android.net.Network network) {
        android.net.NetworkCapabilities nc = this.mCm.getNetworkCapabilities(network);
        return nc.hasTransport(1);
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onWakeupEvent(java.lang.String prefix, int uid, int ethertype, int ipNextHeader, byte[] dstHw, java.lang.String srcIp, java.lang.String dstIp, int srcPort, int dstPort, long timestampNs) {
        java.lang.String[] prefixParts = prefix.split(WAKEUP_EVENT_PREFIX_DELIM);
        if (prefixParts.length != 2) {
            throw new java.lang.IllegalArgumentException("Prefix " + prefix + " required in format <nethandle>:<interface>");
        }
        long netHandle = java.lang.Long.parseLong(prefixParts[0]);
        android.net.Network network = android.net.Network.fromNetworkHandle(netHandle);
        android.net.metrics.WakeupEvent event = new android.net.metrics.WakeupEvent();
        event.iface = prefixParts[1];
        event.uid = uid;
        event.ethertype = ethertype;
        if (com.android.internal.util.ArrayUtils.isEmpty(dstHw)) {
            if (hasWifiTransport(network)) {
                android.util.Log.e(TAG, "Empty mac address on WiFi transport, network: " + network);
            }
            event.dstHwAddr = null;
        } else {
            event.dstHwAddr = android.net.MacAddress.fromBytes(dstHw);
        }
        event.srcIp = srcIp;
        event.dstIp = dstIp;
        event.ipNextHeader = ipNextHeader;
        event.srcPort = srcPort;
        event.dstPort = dstPort;
        if (timestampNs > 0) {
            event.timestampMs = timestampNs / 1000000;
        } else {
            event.timestampMs = java.lang.System.currentTimeMillis();
        }
        addWakeupEvent(event);
        android.os.BatteryStatsInternal bsi = (android.os.BatteryStatsInternal) com.android.server.LocalServices.getService(android.os.BatteryStatsInternal.class);
        if (bsi != null) {
            long elapsedMs = (android.os.SystemClock.elapsedRealtime() + event.timestampMs) - java.lang.System.currentTimeMillis();
            bsi.noteCpuWakingNetworkPacket(network, elapsedMs, event.uid);
        }
        java.lang.String dstMac = java.lang.String.valueOf(event.dstHwAddr);
        com.android.internal.util.FrameworkStatsLog.write(44, uid, event.iface, ethertype, dstMac, srcIp, dstIp, ipNextHeader, srcPort, dstPort);
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public synchronized void onTcpSocketStatsEvent(int[] networkIds, int[] sentPackets, int[] lostPackets, int[] rttsUs, int[] sentAckDiffsMs) {
        int[] iArr = networkIds;
        synchronized (this) {
            if (iArr.length == sentPackets.length && iArr.length == lostPackets.length && iArr.length == rttsUs.length && iArr.length == sentAckDiffsMs.length) {
                long timestamp = java.lang.System.currentTimeMillis();
                int i = 0;
                while (i < iArr.length) {
                    int netId = iArr[i];
                    int sent = sentPackets[i];
                    int lost = lostPackets[i];
                    int rttUs = rttsUs[i];
                    int sentAckDiffMs = sentAckDiffsMs[i];
                    getMetricsForNetwork(timestamp, netId).addTcpStatsResult(sent, lost, rttUs, sentAckDiffMs);
                    if (this.mContext != null) {
                        ((com.android.server.IOplusNecConnectMonitor) com.android.server.OplusServiceFactory.getInstance().getFeature(com.android.server.IOplusNecConnectMonitor.DEFAULT, new java.lang.Object[]{this.mContext})).addTcpStateRecord(netId, sent, lost, rttUs / 1000);
                    }
                    i++;
                    iArr = networkIds;
                }
                return;
            }
            android.util.Log.e(TAG, "Mismatched lengths of TCP socket stats data arrays");
        }
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public int getInterfaceVersion() {
        return 1;
    }

    @Override // com.android.net.module.util.BaseNetdEventListener, android.net.metrics.INetdEventListener
    public java.lang.String getInterfaceHash() {
        return android.net.metrics.INetdEventListener.HASH;
    }

    private void addWakeupEvent(android.net.metrics.WakeupEvent event) {
        java.lang.String iface = event.iface;
        this.mWakeupEvents.append(event);
        android.net.metrics.WakeupStats stats = this.mWakeupStats.get(iface);
        if (stats == null) {
            stats = new android.net.metrics.WakeupStats(iface);
            this.mWakeupStats.put(iface, stats);
        }
        stats.countEvent(event);
    }

    public synchronized void flushStatistics(java.util.List<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> events) {
        for (int i = 0; i < this.mNetworkMetrics.size(); i++) {
            android.net.metrics.ConnectStats stats = this.mNetworkMetrics.valueAt(i).connectMetrics;
            if (stats.eventCount != 0) {
                events.add(com.android.server.connectivity.IpConnectivityEventBuilder.toProto(stats));
            }
        }
        for (int i2 = 0; i2 < this.mNetworkMetrics.size(); i2++) {
            android.net.metrics.DnsEvent ev = this.mNetworkMetrics.valueAt(i2).dnsMetrics;
            if (ev.eventCount != 0) {
                events.add(com.android.server.connectivity.IpConnectivityEventBuilder.toProto(ev));
            }
        }
        for (int i3 = 0; i3 < this.mWakeupStats.size(); i3++) {
            events.add(com.android.server.connectivity.IpConnectivityEventBuilder.toProto(this.mWakeupStats.valueAt(i3)));
        }
        this.mNetworkMetrics.clear();
        this.mWakeupStats.clear();
    }

    public synchronized void list(java.io.PrintWriter pw) {
        pw.println("dns/connect events:");
        for (int i = 0; i < this.mNetworkMetrics.size(); i++) {
            pw.println(this.mNetworkMetrics.valueAt(i).connectMetrics);
        }
        for (int i2 = 0; i2 < this.mNetworkMetrics.size(); i2++) {
            pw.println(this.mNetworkMetrics.valueAt(i2).dnsMetrics);
        }
        pw.println("");
        pw.println("network statistics:");
        for (com.android.server.connectivity.NetdEventListenerService.NetworkMetricsSnapshot s : getNetworkMetricsSnapshots()) {
            pw.println(s);
        }
        pw.println("");
        pw.println("packet wakeup events:");
        for (int i3 = 0; i3 < this.mWakeupStats.size(); i3++) {
            pw.println(this.mWakeupStats.valueAt(i3));
        }
        for (android.net.metrics.WakeupEvent wakeup : (android.net.metrics.WakeupEvent[]) this.mWakeupEvents.toArray()) {
            pw.println(wakeup);
        }
    }

    public synchronized java.util.List<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> listAsProtos() {
        java.util.List<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> list;
        list = new java.util.ArrayList<>();
        for (int i = 0; i < this.mNetworkMetrics.size(); i++) {
            list.add(com.android.server.connectivity.IpConnectivityEventBuilder.toProto(this.mNetworkMetrics.valueAt(i).connectMetrics));
        }
        for (int i2 = 0; i2 < this.mNetworkMetrics.size(); i2++) {
            list.add(com.android.server.connectivity.IpConnectivityEventBuilder.toProto(this.mNetworkMetrics.valueAt(i2).dnsMetrics));
        }
        for (int i3 = 0; i3 < this.mWakeupStats.size(); i3++) {
            list.add(com.android.server.connectivity.IpConnectivityEventBuilder.toProto(this.mWakeupStats.valueAt(i3)));
        }
        return list;
    }

    static class NetworkMetricsSnapshot {
        public java.util.List<android.net.metrics.NetworkMetrics.Summary> stats = new java.util.ArrayList();
        public long timeMs;

        NetworkMetricsSnapshot() {
        }

        static com.android.server.connectivity.NetdEventListenerService.NetworkMetricsSnapshot collect(long timeMs, android.util.SparseArray<android.net.metrics.NetworkMetrics> networkMetrics) {
            com.android.server.connectivity.NetdEventListenerService.NetworkMetricsSnapshot snapshot = new com.android.server.connectivity.NetdEventListenerService.NetworkMetricsSnapshot();
            snapshot.timeMs = timeMs;
            for (int i = 0; i < networkMetrics.size(); i++) {
                android.net.metrics.NetworkMetrics.Summary s = networkMetrics.valueAt(i).getPendingStats();
                if (s != null) {
                    snapshot.stats.add(s);
                }
            }
            return snapshot;
        }

        public java.lang.String toString() {
            java.util.StringJoiner j = new java.util.StringJoiner(", ");
            for (android.net.metrics.NetworkMetrics.Summary s : this.stats) {
                j.add(s.toString());
            }
            return java.lang.String.format("%tT.%tL: %s", java.lang.Long.valueOf(this.timeMs), java.lang.Long.valueOf(this.timeMs), j.toString());
        }
    }

    private class TransportForNetIdNetworkCallback extends android.net.ConnectivityManager.NetworkCallback {
        private final android.util.SparseArray<android.net.NetworkCapabilities> mCapabilities;

        private TransportForNetIdNetworkCallback() {
            this.mCapabilities = new android.util.SparseArray<>();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(android.net.Network network, android.net.NetworkCapabilities nc) {
            synchronized (this.mCapabilities) {
                this.mCapabilities.put(network.getNetId(), nc);
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(android.net.Network network) {
            synchronized (this.mCapabilities) {
                this.mCapabilities.remove(network.getNetId());
            }
        }

        public android.net.NetworkCapabilities getNetworkCapabilities(int netId) {
            android.net.NetworkCapabilities networkCapabilities;
            synchronized (this.mCapabilities) {
                networkCapabilities = this.mCapabilities.get(netId);
            }
            return networkCapabilities;
        }
    }
}
