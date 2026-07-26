package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class WifiDisplayControllerSocExtImpl implements com.android.server.display.IWifiDisplayControllerSocExt {
    private static boolean DEBUG = true;
    private static final int RECONNECT_RETRY_DELAY_MILLIS = 500;
    private static final int RECONNECT_TIMEOUT_MILLIS = 10000;
    private static final java.lang.String TAG = "QcomWifiDisplayControllerSocExtImpl";
    private android.content.Context mContext;
    private com.android.server.display.WifiDisplayController mController;
    private android.os.Handler mHandler;
    private android.net.wifi.p2p.WifiP2pDevice mReConnectDevice;
    private int mReConnection_Timeout_Remain_Millis;
    private boolean mRemoveGroupFlag = false;
    private final java.lang.Runnable mReConnect = new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayControllerSocExtImpl.1
        @Override // java.lang.Runnable
        public void run() {
            android.util.Slog.i(com.android.server.display.WifiDisplayControllerSocExtImpl.TAG, "mReConnect, run()");
            if (com.android.server.display.WifiDisplayControllerSocExtImpl.this.mReConnectDevice == null) {
                android.util.Slog.d(com.android.server.display.WifiDisplayControllerSocExtImpl.TAG, "WFD connect failed, stop scan.");
                return;
            }
            for (android.net.wifi.p2p.WifiP2pDevice device : com.android.server.display.WifiDisplayControllerSocExtImpl.this.mController.mAvailableWifiDisplayPeers) {
                if (com.android.server.display.WifiDisplayControllerSocExtImpl.DEBUG) {
                    android.util.Slog.d(com.android.server.display.WifiDisplayControllerSocExtImpl.TAG, "\t" + com.android.server.display.WifiDisplayControllerSocExtImpl.describeWifiP2pDevice(device));
                }
                if (device.deviceAddress.equals(com.android.server.display.WifiDisplayControllerSocExtImpl.this.mReConnectDevice.deviceAddress)) {
                    android.util.Slog.i(com.android.server.display.WifiDisplayControllerSocExtImpl.TAG, "connect() in mReConnect. Set mReConnecting as true");
                    com.android.server.display.WifiDisplayControllerSocExtImpl.this.mReConnectDevice = null;
                    com.android.server.display.WifiDisplayControllerSocExtImpl.this.mRemoveGroupFlag = false;
                    com.android.server.display.WifiDisplayControllerSocExtImpl.this.mController.requestConnect(device.deviceAddress);
                    return;
                }
            }
            com.android.server.display.WifiDisplayControllerSocExtImpl.this.mReConnection_Timeout_Remain_Millis -= 500;
            if (com.android.server.display.WifiDisplayControllerSocExtImpl.this.mReConnection_Timeout_Remain_Millis > 0) {
                android.util.Slog.i(com.android.server.display.WifiDisplayControllerSocExtImpl.TAG, "post delay mReconnect, ms:" + com.android.server.display.WifiDisplayControllerSocExtImpl.this.mReConnection_Timeout_Remain_Millis);
                com.android.server.display.WifiDisplayControllerSocExtImpl.this.mHandler.postDelayed(com.android.server.display.WifiDisplayControllerSocExtImpl.this.mReConnect, 500L);
                return;
            }
            android.util.Slog.e(com.android.server.display.WifiDisplayControllerSocExtImpl.TAG, "reconnect timeout!");
            com.android.server.display.WifiDisplayControllerSocExtImpl.this.mReConnectDevice = null;
            com.android.server.display.WifiDisplayControllerSocExtImpl.this.mRemoveGroupFlag = false;
            com.android.server.display.WifiDisplayControllerSocExtImpl.this.mReConnection_Timeout_Remain_Millis = 0;
            com.android.server.display.WifiDisplayControllerSocExtImpl.this.mHandler.removeCallbacks(com.android.server.display.WifiDisplayControllerSocExtImpl.this.mReConnect);
        }
    };

    public WifiDisplayControllerSocExtImpl(java.lang.Object wifiDisplayControllerSocExtImpl) {
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public android.net.wifi.p2p.WifiP2pConfig overWriteConfig(android.net.wifi.p2p.WifiP2pConfig oldConfig) {
        return oldConfig;
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public void initSocWifiDisplayController(android.content.Context context, android.os.Handler handler, com.android.server.display.WifiDisplayController controller) {
        this.mContext = context;
        this.mHandler = handler;
        this.mController = controller;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String describeWifiP2pDevice(android.net.wifi.p2p.WifiP2pDevice device) {
        return device != null ? device.toString().replace('\n', ',') : "null";
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public void checkReConnect() {
        if (this.mRemoveGroupFlag && this.mReConnectDevice != null) {
            this.mController.requestStopScan();
            android.util.Slog.i(TAG, "reconnect requestStartScan");
            this.mController.requestStartScan();
            this.mReConnection_Timeout_Remain_Millis = 10000;
            this.mHandler.postDelayed(this.mReConnect, 500L);
        }
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public void setReConnectDevice(android.net.wifi.p2p.WifiP2pDevice reConnectDevice) {
        if (this.mRemoveGroupFlag) {
            this.mReConnectDevice = reConnectDevice;
        }
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public void setRemoveGroupFlag(boolean enable) {
        android.util.Slog.i(TAG, "set remove group flag");
        this.mRemoveGroupFlag = enable;
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public boolean getRemoveGroupFlag() {
        return this.mRemoveGroupFlag;
    }

    @Override // com.android.server.display.IWifiDisplayControllerSocExt
    public void setWFD(boolean enable) {
    }
}
