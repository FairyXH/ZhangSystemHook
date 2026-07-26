package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusNecConnectMonitor extends android.common.IOplusCommonFeature {
    public static final com.android.server.IOplusNecConnectMonitor DEFAULT = new com.android.server.IOplusNecConnectMonitor() { // from class: com.android.server.IOplusNecConnectMonitor.1
    };
    public static final java.lang.String NAME = "IOplusNecConnectMonitor";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusNecConnectMonitor;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void addDnsRecord(int netId, int returnCode, int latencyMs, long mDnsIpv4Num, long mDnsIpv6Num) {
    }

    default void addTcpSynRecord(int netId, int returnCode, int latencyMs, int uid, long mTcpIpv4Num, long mTcpIpv6Num) {
    }

    default void addTcpStateRecord(int netId, int sentPackets, int lostPackets, int rttMs) {
    }

    default void addHttpRecord(java.lang.String prefix, int uid) {
    }

    default void onDnsEvent(int netId, int eventType, int returnCode, int latencyMs, java.lang.String hostname, java.lang.String[] ipAddresses, int ipAddressesCount, int uid) {
    }

    default void onConnectEvent(int netId, int error, int latencyMs, java.lang.String ipAddr, int port, int uid) {
    }

    default void addNetStackRecord(int uid, java.lang.String packageName, boolean netStackError) {
    }

    default java.util.concurrent.Executor getNecExecutor() {
        return null;
    }
}
