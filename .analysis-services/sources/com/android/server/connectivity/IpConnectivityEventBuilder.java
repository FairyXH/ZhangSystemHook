package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public final class IpConnectivityEventBuilder {
    private static final int[] IFNAME_LINKLAYERS;
    private static final java.lang.String[] IFNAME_PREFIXES;
    private static final int KNOWN_PREFIX = 7;
    private static final android.util.SparseIntArray TRANSPORT_LINKLAYER_MAP = new android.util.SparseIntArray();

    private IpConnectivityEventBuilder() {
    }

    public static byte[] serialize(int dropped, java.util.List<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> events) throws java.io.IOException {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityLog log = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityLog();
        log.events = (com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent[]) events.toArray(new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent[events.size()]);
        log.droppedEvents = dropped;
        if (log.events.length > 0 || dropped > 0) {
            log.version = 2;
        }
        return com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityLog.toByteArray(log);
    }

    public static java.util.List<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> toProto(java.util.List<android.net.ConnectivityMetricsEvent> eventsIn) {
        java.util.ArrayList<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> eventsOut = new java.util.ArrayList<>(eventsIn.size());
        for (android.net.ConnectivityMetricsEvent in : eventsIn) {
            com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out = toProto(in);
            if (out != null) {
                eventsOut.add(out);
            }
        }
        return eventsOut;
    }

    public static com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent toProto(android.net.ConnectivityMetricsEvent ev) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out = buildEvent(ev.netId, ev.transports, ev.ifname);
        out.timeMs = ev.timestamp;
        if (!setEvent(out, ev.data)) {
            return null;
        }
        return out;
    }

    public static com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent toProto(android.net.metrics.ConnectStats in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.ConnectStatistics stats = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.ConnectStatistics();
        stats.connectCount = in.connectCount;
        stats.connectBlockingCount = in.connectBlockingCount;
        stats.ipv6AddrCount = in.ipv6ConnectCount;
        stats.latenciesMs = in.latencies.toArray();
        stats.errnosCounters = toPairArray(in.errnos);
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out = buildEvent(in.netId, in.transports, null);
        out.setConnectStatistics(stats);
        return out;
    }

    public static com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent toProto(android.net.metrics.DnsEvent in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.DNSLookupBatch dnsLookupBatch = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.DNSLookupBatch();
        in.resize(in.eventCount);
        dnsLookupBatch.eventTypes = bytesToInts(in.eventTypes);
        dnsLookupBatch.returnCodes = bytesToInts(in.returnCodes);
        dnsLookupBatch.latenciesMs = in.latenciesMs;
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out = buildEvent(in.netId, in.transports, null);
        out.setDnsLookupBatch(dnsLookupBatch);
        return out;
    }

    public static com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent toProto(android.net.metrics.WakeupStats in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.WakeupStats wakeupStats = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.WakeupStats();
        in.updateDuration();
        wakeupStats.durationSec = in.durationSec;
        wakeupStats.totalWakeups = in.totalWakeups;
        wakeupStats.rootWakeups = in.rootWakeups;
        wakeupStats.systemWakeups = in.systemWakeups;
        wakeupStats.nonApplicationWakeups = in.nonApplicationWakeups;
        wakeupStats.applicationWakeups = in.applicationWakeups;
        wakeupStats.noUidWakeups = in.noUidWakeups;
        wakeupStats.l2UnicastCount = in.l2UnicastCount;
        wakeupStats.l2MulticastCount = in.l2MulticastCount;
        wakeupStats.l2BroadcastCount = in.l2BroadcastCount;
        wakeupStats.ethertypeCounts = toPairArray(in.ethertypes);
        wakeupStats.ipNextHeaderCounts = toPairArray(in.ipNextHeaders);
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out = buildEvent(0, 0L, in.iface);
        out.setWakeupStats(wakeupStats);
        return out;
    }

    public static com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent toProto(android.net.metrics.DefaultNetworkEvent in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.DefaultNetworkEvent ev = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.DefaultNetworkEvent();
        ev.finalScore = in.finalScore;
        ev.initialScore = in.initialScore;
        ev.ipSupport = ipSupportOf(in);
        ev.defaultNetworkDurationMs = in.durationMs;
        ev.validationDurationMs = in.validatedMs;
        ev.previousDefaultNetworkLinkLayer = transportsToLinkLayer(in.previousTransports);
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out = buildEvent(in.netId, in.transports, null);
        if (in.transports == 0) {
            out.linkLayer = 5;
        }
        out.setDefaultNetworkEvent(ev);
        return out;
    }

    private static com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent buildEvent(int netId, long transports, java.lang.String ifname) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent ev = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent();
        ev.networkId = netId;
        ev.transports = transports;
        if (ifname != null) {
            ev.ifName = ifname;
        }
        inferLinkLayer(ev);
        return ev;
    }

    private static boolean setEvent(com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out, android.os.Parcelable in) {
        if (in instanceof android.net.metrics.DhcpErrorEvent) {
            setDhcpErrorEvent(out, (android.net.metrics.DhcpErrorEvent) in);
            return true;
        }
        if (in instanceof android.net.metrics.DhcpClientEvent) {
            setDhcpClientEvent(out, (android.net.metrics.DhcpClientEvent) in);
            return true;
        }
        if (in instanceof android.net.metrics.IpManagerEvent) {
            setIpManagerEvent(out, (android.net.metrics.IpManagerEvent) in);
            return true;
        }
        if (in instanceof android.net.metrics.IpReachabilityEvent) {
            setIpReachabilityEvent(out, (android.net.metrics.IpReachabilityEvent) in);
            return true;
        }
        if (in instanceof android.net.metrics.NetworkEvent) {
            setNetworkEvent(out, (android.net.metrics.NetworkEvent) in);
            return true;
        }
        if (in instanceof android.net.metrics.ValidationProbeEvent) {
            setValidationProbeEvent(out, (android.net.metrics.ValidationProbeEvent) in);
            return true;
        }
        if (in instanceof android.net.metrics.ApfProgramEvent) {
            setApfProgramEvent(out, (android.net.metrics.ApfProgramEvent) in);
            return true;
        }
        if (in instanceof android.net.metrics.ApfStats) {
            setApfStats(out, (android.net.metrics.ApfStats) in);
            return true;
        }
        if (in instanceof android.net.metrics.RaEvent) {
            setRaEvent(out, (android.net.metrics.RaEvent) in);
            return true;
        }
        return false;
    }

    private static void setDhcpErrorEvent(com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out, android.net.metrics.DhcpErrorEvent in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.DHCPEvent dhcpEvent = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.DHCPEvent();
        dhcpEvent.setErrorCode(in.errorCode);
        out.setDhcpEvent(dhcpEvent);
    }

    private static void setDhcpClientEvent(com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out, android.net.metrics.DhcpClientEvent in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.DHCPEvent dhcpEvent = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.DHCPEvent();
        dhcpEvent.setStateTransition(in.msg);
        dhcpEvent.durationMs = in.durationMs;
        out.setDhcpEvent(dhcpEvent);
    }

    private static void setIpManagerEvent(com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out, android.net.metrics.IpManagerEvent in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpProvisioningEvent ipProvisioningEvent = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpProvisioningEvent();
        ipProvisioningEvent.eventType = in.eventType;
        ipProvisioningEvent.latencyMs = (int) in.durationMs;
        out.setIpProvisioningEvent(ipProvisioningEvent);
    }

    private static void setIpReachabilityEvent(com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out, android.net.metrics.IpReachabilityEvent in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpReachabilityEvent ipReachabilityEvent = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpReachabilityEvent();
        ipReachabilityEvent.eventType = in.eventType;
        out.setIpReachabilityEvent(ipReachabilityEvent);
    }

    private static void setNetworkEvent(com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out, android.net.metrics.NetworkEvent in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.NetworkEvent networkEvent = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.NetworkEvent();
        networkEvent.eventType = in.eventType;
        networkEvent.latencyMs = (int) in.durationMs;
        out.setNetworkEvent(networkEvent);
    }

    private static void setValidationProbeEvent(com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out, android.net.metrics.ValidationProbeEvent in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.ValidationProbeEvent validationProbeEvent = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.ValidationProbeEvent();
        validationProbeEvent.latencyMs = (int) in.durationMs;
        validationProbeEvent.probeType = in.probeType;
        validationProbeEvent.probeResult = in.returnCode;
        out.setValidationProbeEvent(validationProbeEvent);
    }

    private static void setApfProgramEvent(com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out, android.net.metrics.ApfProgramEvent in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.ApfProgramEvent apfProgramEvent = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.ApfProgramEvent();
        apfProgramEvent.lifetime = in.lifetime;
        apfProgramEvent.effectiveLifetime = in.actualLifetime;
        apfProgramEvent.filteredRas = in.filteredRas;
        apfProgramEvent.currentRas = in.currentRas;
        apfProgramEvent.programLength = in.programLength;
        if (isBitSet(in.flags, 0)) {
            apfProgramEvent.dropMulticast = true;
        }
        if (isBitSet(in.flags, 1)) {
            apfProgramEvent.hasIpv4Addr = true;
        }
        out.setApfProgramEvent(apfProgramEvent);
    }

    private static void setApfStats(com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out, android.net.metrics.ApfStats in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.ApfStatistics apfStatistics = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.ApfStatistics();
        apfStatistics.durationMs = in.durationMs;
        apfStatistics.receivedRas = in.receivedRas;
        apfStatistics.matchingRas = in.matchingRas;
        apfStatistics.droppedRas = in.droppedRas;
        apfStatistics.zeroLifetimeRas = in.zeroLifetimeRas;
        apfStatistics.parseErrors = in.parseErrors;
        apfStatistics.programUpdates = in.programUpdates;
        apfStatistics.programUpdatesAll = in.programUpdatesAll;
        apfStatistics.programUpdatesAllowingMulticast = in.programUpdatesAllowingMulticast;
        apfStatistics.maxProgramSize = in.maxProgramSize;
        out.setApfStatistics(apfStatistics);
    }

    private static void setRaEvent(com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent out, android.net.metrics.RaEvent in) {
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.RaEvent raEvent = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.RaEvent();
        raEvent.routerLifetime = in.routerLifetime;
        raEvent.prefixValidLifetime = in.prefixValidLifetime;
        raEvent.prefixPreferredLifetime = in.prefixPreferredLifetime;
        raEvent.routeInfoLifetime = in.routeInfoLifetime;
        raEvent.rdnssLifetime = in.rdnssLifetime;
        raEvent.dnsslLifetime = in.dnsslLifetime;
        out.setRaEvent(raEvent);
    }

    private static int[] bytesToInts(byte[] in) {
        int[] out = new int[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = in[i] & 255;
        }
        return out;
    }

    private static com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.Pair[] toPairArray(android.util.SparseIntArray counts) {
        int s = counts.size();
        com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.Pair[] pairs = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.Pair[s];
        for (int i = 0; i < s; i++) {
            com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.Pair p = new com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.Pair();
            p.key = counts.keyAt(i);
            p.value = counts.valueAt(i);
            pairs[i] = p;
        }
        return pairs;
    }

    private static int ipSupportOf(android.net.metrics.DefaultNetworkEvent in) {
        if (in.ipv4 && in.ipv6) {
            return 3;
        }
        if (in.ipv6) {
            return 2;
        }
        if (in.ipv4) {
            return 1;
        }
        return 0;
    }

    private static boolean isBitSet(int flags, int bit) {
        return ((1 << bit) & flags) != 0;
    }

    private static void inferLinkLayer(com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent ev) {
        int linkLayer = 0;
        if (ev.transports != 0) {
            linkLayer = transportsToLinkLayer(ev.transports);
        } else if (ev.ifName != null) {
            linkLayer = ifnameToLinkLayer(ev.ifName);
        }
        if (linkLayer == 0) {
            return;
        }
        ev.linkLayer = linkLayer;
        ev.ifName = "";
    }

    private static int transportsToLinkLayer(long transports) {
        switch (java.lang.Long.bitCount(transports)) {
            case 0:
                return 0;
            case 1:
                int t = java.lang.Long.numberOfTrailingZeros(transports);
                return TRANSPORT_LINKLAYER_MAP.get(t, 0);
            default:
                return 6;
        }
    }

    static {
        TRANSPORT_LINKLAYER_MAP.append(0, 2);
        TRANSPORT_LINKLAYER_MAP.append(1, 4);
        TRANSPORT_LINKLAYER_MAP.append(2, 1);
        TRANSPORT_LINKLAYER_MAP.append(3, 3);
        TRANSPORT_LINKLAYER_MAP.append(4, 0);
        TRANSPORT_LINKLAYER_MAP.append(5, 8);
        TRANSPORT_LINKLAYER_MAP.append(6, 9);
        IFNAME_PREFIXES = new java.lang.String[7];
        IFNAME_LINKLAYERS = new int[7];
        IFNAME_PREFIXES[0] = "rmnet";
        IFNAME_LINKLAYERS[0] = 2;
        IFNAME_PREFIXES[1] = "wlan";
        IFNAME_LINKLAYERS[1] = 4;
        IFNAME_PREFIXES[2] = "bt-pan";
        IFNAME_LINKLAYERS[2] = 1;
        IFNAME_PREFIXES[3] = "p2p";
        IFNAME_LINKLAYERS[3] = 7;
        IFNAME_PREFIXES[4] = "aware";
        IFNAME_LINKLAYERS[4] = 8;
        IFNAME_PREFIXES[5] = "eth";
        IFNAME_LINKLAYERS[5] = 3;
        IFNAME_PREFIXES[6] = "wpan";
        IFNAME_LINKLAYERS[6] = 9;
    }

    private static int ifnameToLinkLayer(java.lang.String ifname) {
        for (int i = 0; i < 7; i++) {
            java.lang.String pattern = IFNAME_PREFIXES[i];
            if (ifname.startsWith(pattern)) {
                return IFNAME_LINKLAYERS[i];
            }
        }
        return 0;
    }
}
