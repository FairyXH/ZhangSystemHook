package com.android.net.module.util;

/* JADX INFO: loaded from: classes.dex */
public class BaseNetdEventListener extends android.net.metrics.INetdEventListener.Stub {
    @Override // android.net.metrics.INetdEventListener
    public void onDnsEvent(int netId, int eventType, int returnCode, int latencyMs, java.lang.String hostname, java.lang.String[] ipAddresses, int ipAddressesCount, int uid) {
    }

    @Override // android.net.metrics.INetdEventListener
    public void onPrivateDnsValidationEvent(int netId, java.lang.String ipAddress, java.lang.String hostname, boolean validated) {
    }

    @Override // android.net.metrics.INetdEventListener
    public void onConnectEvent(int netId, int error, int latencyMs, java.lang.String ipAddr, int port, int uid) {
    }

    @Override // android.net.metrics.INetdEventListener
    public void onWakeupEvent(java.lang.String prefix, int uid, int ethertype, int ipNextHeader, byte[] dstHw, java.lang.String srcIp, java.lang.String dstIp, int srcPort, int dstPort, long timestampNs) {
    }

    @Override // android.net.metrics.INetdEventListener
    public void onTcpSocketStatsEvent(int[] networkIds, int[] sentPackets, int[] lostPackets, int[] rttUs, int[] sentAckDiffMs) {
    }

    @Override // android.net.metrics.INetdEventListener
    public void onNat64PrefixEvent(int netId, boolean added, java.lang.String prefixString, int prefixLength) {
    }

    @Override // android.net.metrics.INetdEventListener
    public int getInterfaceVersion() {
        return 1;
    }

    @Override // android.net.metrics.INetdEventListener
    public java.lang.String getInterfaceHash() {
        return android.net.metrics.INetdEventListener.HASH;
    }
}
