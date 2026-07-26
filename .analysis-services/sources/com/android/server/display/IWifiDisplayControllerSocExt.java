package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IWifiDisplayControllerSocExt {
    default void initSocWifiDisplayController(android.content.Context context, android.os.Handler handler, com.android.server.display.WifiDisplayController controller) {
    }

    default android.net.wifi.p2p.WifiP2pConfig overWriteConfig(android.net.wifi.p2p.WifiP2pConfig oldConfig) {
        return null;
    }

    default void checkReConnect() {
    }

    default void setReConnectDevice(android.net.wifi.p2p.WifiP2pDevice reConnectDevice) {
    }

    default void setRemoveGroupFlag(boolean enable) {
    }

    default boolean getRemoveGroupFlag() {
        return false;
    }

    default void setWFD(boolean enable) {
    }
}
