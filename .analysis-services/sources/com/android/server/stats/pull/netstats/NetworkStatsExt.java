package com.android.server.stats.pull.netstats;

/* JADX INFO: loaded from: classes3.dex */
public class NetworkStatsExt {
    public final boolean isTypeProxy;
    public final int oemManaged;
    public final int ratType;
    public final boolean slicedByFgbg;
    public final boolean slicedByMetered;
    public final boolean slicedByTag;
    public final android.net.NetworkStats stats;
    public final com.android.server.stats.pull.netstats.SubInfo subInfo;
    public final int[] transports;

    public NetworkStatsExt(android.net.NetworkStats stats, int[] transports, boolean slicedByFgbg) {
        this(stats, transports, slicedByFgbg, false, false, 0, null, -1, false);
    }

    public NetworkStatsExt(android.net.NetworkStats stats, int[] transports, boolean slicedByFgbg, boolean slicedByTag, boolean slicedByMetered, int ratType, com.android.server.stats.pull.netstats.SubInfo subInfo, int oemManaged, boolean isTypeProxy) {
        this.stats = stats;
        this.transports = java.util.Arrays.copyOf(transports, transports.length);
        java.util.Arrays.sort(this.transports);
        this.slicedByFgbg = slicedByFgbg;
        this.slicedByTag = slicedByTag;
        this.slicedByMetered = slicedByMetered;
        this.ratType = ratType;
        this.subInfo = subInfo;
        this.oemManaged = oemManaged;
        this.isTypeProxy = isTypeProxy;
    }

    public boolean hasSameSlicing(com.android.server.stats.pull.netstats.NetworkStatsExt other) {
        return java.util.Arrays.equals(this.transports, other.transports) && this.slicedByFgbg == other.slicedByFgbg && this.slicedByTag == other.slicedByTag && this.slicedByMetered == other.slicedByMetered && this.ratType == other.ratType && java.util.Objects.equals(this.subInfo, other.subInfo) && this.oemManaged == other.oemManaged && this.isTypeProxy == other.isTypeProxy;
    }
}
