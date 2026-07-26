package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IWifiDisplayControllerExt {
    default void initWifiDisplayControllerExtImpl(android.content.Context context) {
    }

    default android.net.wifi.p2p.WifiP2pConfig generateConfigByGoIntent(android.net.wifi.p2p.WifiP2pConfig config, android.net.wifi.p2p.WifiP2pDevice device) {
        return config;
    }

    default void updateWFDControl() {
    }

    default boolean isOplusRemoteDisplayAvailable() {
        return false;
    }

    default java.lang.Object listen(java.lang.String iface, android.media.RemoteDisplay.Listener listener, android.os.Handler handler, java.lang.String opPackageName) {
        return null;
    }

    default void dispose(java.lang.Object oplusRemoteDisplay) {
    }

    default void setOplusWifiDisplayInfo(android.net.wifi.p2p.WifiP2pDevice connectedDevice) {
    }
}
