package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusWifiDisplayUsageHelperExt {
    default void wfdConnecteSuceess(android.net.wifi.p2p.WifiP2pGroup group, android.net.wifi.p2p.WifiP2pDevice p2pdevice, android.content.Context context) {
    }

    default void wfdConnectedFailed(java.lang.String reason, android.net.wifi.p2p.WifiP2pDevice p2pdevice, android.net.wifi.p2p.WifiP2pGroup connectedDeviceGroupInfo, android.content.Context context) {
    }

    default void reportWfdConnectionTime(java.lang.Long connectionTime, android.net.wifi.p2p.WifiP2pDevice connectedDevice, android.content.Context context) {
    }

    default void reportWfdEnableState(android.net.wifi.p2p.WifiP2pWfdInfo wfdInfo, boolean wfdEnabled, android.content.Context context) {
    }

    default void getWfdScanState(boolean scanStart, android.content.Context context) {
    }

    default void getDiscoverPeersState(boolean success, android.content.Context context) {
    }

    default void getPeers(android.content.Context context) {
    }

    default void getP2pRole(java.lang.String role, int intent, boolean force, android.content.Context context) {
    }

    default void getSessionInfo(boolean client, java.lang.String group, java.lang.String ip, android.content.Context context) {
    }

    default void getWfdDisconnected(android.content.Context context) {
    }
}
