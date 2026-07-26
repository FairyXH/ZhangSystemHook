package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public final class WifiDisplayController implements com.android.internal.util.DumpUtils.Dump {
    private static final int CONNECTION_TIMEOUT_SECONDS = 30;
    private static final int CONNECT_MAX_RETRIES = 3;
    private static final int CONNECT_RETRY_DELAY_MILLIS = 500;
    private static final boolean DEBUG;
    private static final boolean DEBUGV;
    private static final int DEFAULT_CONTROL_PORT = 7236;
    private static final int DISCOVER_PEERS_INTERVAL_MILLIS = 5000;
    private static final int MAX_THROUGHPUT = 50;
    private static final int RTSP_TIMEOUT_SECONDS = 30;
    private static final int RTSP_TIMEOUT_SECONDS_CERT_MODE = 120;
    private static final java.lang.String TAG = "WifiDisplayController";
    private long connectedTime;
    private android.hardware.display.WifiDisplay mAdvertisedDisplay;
    private int mAdvertisedDisplayFlags;
    private int mAdvertisedDisplayHeight;
    private android.view.Surface mAdvertisedDisplaySurface;
    private int mAdvertisedDisplayWidth;
    private android.net.wifi.p2p.WifiP2pDevice mCancelingDevice;
    public android.net.wifi.p2p.WifiP2pDevice mConnectedDevice;
    private android.net.wifi.p2p.WifiP2pGroup mConnectedDeviceGroupInfo;
    public android.net.wifi.p2p.WifiP2pDevice mConnectingDevice;
    private int mConnectionRetriesLeft;
    private final android.content.Context mContext;
    private android.net.wifi.p2p.WifiP2pDevice mDesiredDevice;
    private android.net.wifi.p2p.WifiP2pDevice mDisconnectingDevice;
    private boolean mDiscoverPeersInProgress;
    private java.lang.Object mExtRemoteDisplay;
    private final android.os.Handler mHandler;
    private final com.android.server.display.WifiDisplayController.Listener mListener;
    private android.net.NetworkInfo mNetworkInfo;
    private java.lang.Object mOplusRemoteDisplay;
    private android.media.RemoteDisplay mRemoteDisplay;
    private boolean mRemoteDisplayConnected;
    private java.lang.String mRemoteDisplayInterface;
    private boolean mScanRequested;
    private android.net.wifi.p2p.WifiP2pDevice mThisDevice;
    private boolean mWfdEnabled;
    private boolean mWfdEnabling;
    private boolean mWifiDisplayCertMode;
    private boolean mWifiDisplayOnSetting;
    private android.net.wifi.p2p.WifiP2pManager.Channel mWifiP2pChannel;
    private boolean mWifiP2pEnabled;
    private android.net.wifi.p2p.WifiP2pManager mWifiP2pManager;
    public final java.util.ArrayList<android.net.wifi.p2p.WifiP2pDevice> mAvailableWifiDisplayPeers = new java.util.ArrayList<>();
    private int mWifiDisplayWpsConfig = 4;
    public com.android.server.display.IWifiDisplayControllerSocExt mWdcSocExt = (com.android.server.display.IWifiDisplayControllerSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IWifiDisplayControllerSocExt.class).base(this).create();
    private int mWFDState = 0;
    private final java.lang.Runnable mDiscoverPeers = new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayController.16
        @Override // java.lang.Runnable
        public void run() {
            com.android.server.display.WifiDisplayController.this.tryDiscoverPeers();
        }
    };
    private final java.lang.Runnable mConnectionTimeout = new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayController.17
        @Override // java.lang.Runnable
        public void run() {
            if (com.android.server.display.WifiDisplayController.this.mConnectingDevice != null && com.android.server.display.WifiDisplayController.this.mConnectingDevice == com.android.server.display.WifiDisplayController.this.mDesiredDevice) {
                android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Timed out waiting for Wifi display connection after 30 seconds: " + com.android.server.display.WifiDisplayController.this.mConnectingDevice.deviceName);
                com.android.server.display.WifiDisplayController.this.handleConnectionFailure(true);
                com.android.server.display.WifiDisplayController.this.mOwduhWrapper.getExtImpl().wfdConnectedFailed("P2P_Connection_Timeout", com.android.server.display.WifiDisplayController.this.mConnectingDevice, com.android.server.display.WifiDisplayController.this.mConnectedDeviceGroupInfo, com.android.server.display.WifiDisplayController.this.mContext);
            }
        }
    };
    private final java.lang.Runnable mRtspTimeout = new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayController.18
        @Override // java.lang.Runnable
        public void run() {
            if (com.android.server.display.WifiDisplayController.this.mConnectedDevice != null) {
                if ((com.android.server.display.WifiDisplayController.this.mRemoteDisplay != null || com.android.server.display.WifiDisplayController.this.mExtRemoteDisplay != null || com.android.server.display.WifiDisplayController.this.mOplusRemoteDisplay != null) && !com.android.server.display.WifiDisplayController.this.mRemoteDisplayConnected) {
                    android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Timed out waiting for Wifi display RTSP connection after 30 seconds: " + com.android.server.display.WifiDisplayController.this.mConnectedDevice.deviceName);
                    com.android.server.display.WifiDisplayController.this.handleConnectionFailure(true);
                    com.android.server.display.WifiDisplayController.this.mOwduhWrapper.getExtImpl().wfdConnectedFailed("RTSP_TimeOut", com.android.server.display.WifiDisplayController.this.mConnectedDevice, com.android.server.display.WifiDisplayController.this.mConnectedDeviceGroupInfo, com.android.server.display.WifiDisplayController.this.mContext);
                }
            }
        }
    };
    private final android.content.BroadcastReceiver mWifiP2pReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.display.WifiDisplayController.22
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if (action.equals("android.net.wifi.p2p.STATE_CHANGED")) {
                boolean enabled = intent.getIntExtra("wifi_p2p_state", 1) == 2;
                if (com.android.server.display.WifiDisplayController.DEBUG) {
                    android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Received WIFI_P2P_STATE_CHANGED_ACTION: enabled=" + enabled);
                }
                com.android.server.display.WifiDisplayController.this.handleStateChanged(enabled);
                return;
            }
            if (action.equals("android.net.wifi.p2p.PEERS_CHANGED")) {
                if (com.android.server.display.WifiDisplayController.DEBUG) {
                    android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Received WIFI_P2P_PEERS_CHANGED_ACTION.");
                }
                com.android.server.display.WifiDisplayController.this.handlePeersChanged();
            } else {
                if (action.equals("android.net.wifi.p2p.CONNECTION_STATE_CHANGE")) {
                    android.net.NetworkInfo networkInfo = (android.net.NetworkInfo) intent.getParcelableExtra("networkInfo", android.net.NetworkInfo.class);
                    if (com.android.server.display.WifiDisplayController.DEBUG) {
                        android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Received WIFI_P2P_CONNECTION_CHANGED_ACTION: networkInfo=" + networkInfo);
                    }
                    com.android.server.display.WifiDisplayController.this.handleConnectionChanged(networkInfo);
                    return;
                }
                if (action.equals("android.net.wifi.p2p.THIS_DEVICE_CHANGED")) {
                    com.android.server.display.WifiDisplayController.this.mThisDevice = (android.net.wifi.p2p.WifiP2pDevice) intent.getParcelableExtra("wifiP2pDevice", android.net.wifi.p2p.WifiP2pDevice.class);
                    if (com.android.server.display.WifiDisplayController.DEBUG) {
                        android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Received WIFI_P2P_THIS_DEVICE_CHANGED_ACTION: mThisDevice= " + com.android.server.display.WifiDisplayController.this.mThisDevice);
                    }
                }
            }
        }
    };
    private com.android.server.display.WifiDisplayController.WifiDisplayControllerWrapper mWdcWrapper = new com.android.server.display.WifiDisplayController.WifiDisplayControllerWrapper();
    private com.android.server.display.WifiDisplayController.OplusWifiDisplayUsageHelperWrapper mOwduhWrapper = new com.android.server.display.WifiDisplayController.OplusWifiDisplayUsageHelperWrapper();

    public interface Listener {
        void onDisplayChanged(android.hardware.display.WifiDisplay wifiDisplay);

        void onDisplayConnected(android.hardware.display.WifiDisplay wifiDisplay, android.view.Surface surface, int i, int i2, int i3);

        void onDisplayConnecting(android.hardware.display.WifiDisplay wifiDisplay);

        void onDisplayConnectionFailed();

        void onDisplayDisconnected();

        void onDisplaySessionInfo(android.hardware.display.WifiDisplaySessionInfo wifiDisplaySessionInfo);

        void onFeatureStateChanged(int i);

        void onScanFinished();

        void onScanResults(android.hardware.display.WifiDisplay[] wifiDisplayArr);

        void onScanStarted();
    }

    static {
        DEBUG = android.os.Build.isMtkPlatform() ? true : android.os.SystemProperties.getBoolean("persist.vendor.debug.wfdcdbg", false);
        DEBUGV = android.os.SystemProperties.getBoolean("persist.vendor.debug.wfdcdbgv", false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public WifiDisplayController(android.content.Context context, android.os.Handler handler, com.android.server.display.WifiDisplayController.Listener listener) {
        this.mContext = context;
        this.mHandler = handler;
        this.mListener = listener;
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
        intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
        context.registerReceiver(this.mWifiP2pReceiver, intentFilter, null, this.mHandler);
        android.database.ContentObserver contentObserver = new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.display.WifiDisplayController.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, android.net.Uri uri) {
                com.android.server.display.WifiDisplayController.this.updateSettings();
            }
        };
        android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
        contentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("wifi_display_on"), false, contentObserver);
        contentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("wifi_display_certification_on"), false, contentObserver);
        contentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("wifi_display_wps_config"), false, contentObserver);
        updateSettings();
        this.mWdcSocExt.initSocWifiDisplayController(this.mContext, this.mHandler, this);
        this.mWdcWrapper.getExtImpl().initWifiDisplayControllerExtImpl(this.mContext);
    }

    private void retrieveWifiP2pManagerAndChannel() {
        if (this.mWifiP2pManager == null) {
            this.mWifiP2pManager = (android.net.wifi.p2p.WifiP2pManager) this.mContext.getSystemService("wifip2p");
        }
        if (this.mWifiP2pChannel == null && this.mWifiP2pManager != null) {
            this.mWifiP2pChannel = this.mWifiP2pManager.initialize(this.mContext, this.mHandler.getLooper(), null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSettings() {
        android.content.ContentResolver resolver = this.mContext.getContentResolver();
        this.mWifiDisplayOnSetting = android.provider.Settings.Global.getInt(resolver, "wifi_display_on", 0) != 0;
        this.mWifiDisplayCertMode = android.provider.Settings.Global.getInt(resolver, "wifi_display_certification_on", 0) != 0;
        this.mWifiDisplayWpsConfig = 4;
        if (this.mWifiDisplayCertMode) {
            this.mWifiDisplayWpsConfig = android.provider.Settings.Global.getInt(resolver, "wifi_display_wps_config", 4);
        }
        updateWfdEnableState();
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println("mWifiDisplayOnSetting=" + this.mWifiDisplayOnSetting);
        pw.println("mWifiP2pEnabled=" + this.mWifiP2pEnabled);
        pw.println("mWfdEnabled=" + this.mWfdEnabled);
        pw.println("mWfdEnabling=" + this.mWfdEnabling);
        pw.println("mNetworkInfo=" + this.mNetworkInfo);
        pw.println("mScanRequested=" + this.mScanRequested);
        pw.println("mDiscoverPeersInProgress=" + this.mDiscoverPeersInProgress);
        pw.println("mDesiredDevice=" + describeWifiP2pDevice(this.mDesiredDevice));
        pw.println("mConnectingDisplay=" + describeWifiP2pDevice(this.mConnectingDevice));
        pw.println("mDisconnectingDisplay=" + describeWifiP2pDevice(this.mDisconnectingDevice));
        pw.println("mCancelingDisplay=" + describeWifiP2pDevice(this.mCancelingDevice));
        pw.println("mConnectedDevice=" + describeWifiP2pDevice(this.mConnectedDevice));
        pw.println("mConnectionRetriesLeft=" + this.mConnectionRetriesLeft);
        pw.println("mRemoteDisplay=" + this.mRemoteDisplay);
        pw.println("mRemoteDisplayInterface=" + this.mRemoteDisplayInterface);
        pw.println("mRemoteDisplayConnected=" + this.mRemoteDisplayConnected);
        pw.println("mAdvertisedDisplay=" + this.mAdvertisedDisplay);
        pw.println("mAdvertisedDisplaySurface=" + this.mAdvertisedDisplaySurface);
        pw.println("mAdvertisedDisplayWidth=" + this.mAdvertisedDisplayWidth);
        pw.println("mAdvertisedDisplayHeight=" + this.mAdvertisedDisplayHeight);
        pw.println("mAdvertisedDisplayFlags=" + this.mAdvertisedDisplayFlags);
        pw.println("mAvailableWifiDisplayPeers: size=" + this.mAvailableWifiDisplayPeers.size());
        for (android.net.wifi.p2p.WifiP2pDevice device : this.mAvailableWifiDisplayPeers) {
            pw.println("  " + describeWifiP2pDevice(device));
        }
    }

    private void dump() {
        android.util.Slog.d(TAG, "mWifiDisplayOnSetting=" + this.mWifiDisplayOnSetting);
        android.util.Slog.d(TAG, "mWifiP2pEnabled=" + this.mWifiP2pEnabled);
        android.util.Slog.d(TAG, "mWfdEnabled=" + this.mWfdEnabled);
        android.util.Slog.d(TAG, "mWfdEnabling=" + this.mWfdEnabling);
        android.util.Slog.d(TAG, "mNetworkInfo=" + this.mNetworkInfo);
        android.util.Slog.d(TAG, "mScanRequested=" + this.mScanRequested);
        android.util.Slog.d(TAG, "mDiscoverPeersInProgress=" + this.mDiscoverPeersInProgress);
        android.util.Slog.d(TAG, "mDesiredDevice=" + describeWifiP2pDevice(this.mDesiredDevice));
        android.util.Slog.d(TAG, "mConnectingDisplay=" + describeWifiP2pDevice(this.mConnectingDevice));
        android.util.Slog.d(TAG, "mDisconnectingDisplay=" + describeWifiP2pDevice(this.mDisconnectingDevice));
        android.util.Slog.d(TAG, "mCancelingDisplay=" + describeWifiP2pDevice(this.mCancelingDevice));
        android.util.Slog.d(TAG, "mConnectedDevice=" + describeWifiP2pDevice(this.mConnectedDevice));
        android.util.Slog.d(TAG, "mConnectionRetriesLeft=" + this.mConnectionRetriesLeft);
        android.util.Slog.d(TAG, "mRemoteDisplay=" + this.mRemoteDisplay);
        android.util.Slog.d(TAG, "mRemoteDisplayInterface=" + this.mRemoteDisplayInterface);
        android.util.Slog.d(TAG, "mRemoteDisplayConnected=" + this.mRemoteDisplayConnected);
        android.util.Slog.d(TAG, "mAdvertisedDisplay=" + this.mAdvertisedDisplay);
        android.util.Slog.d(TAG, "mAdvertisedDisplaySurface=" + this.mAdvertisedDisplaySurface);
        android.util.Slog.d(TAG, "mAdvertisedDisplayWidth=" + this.mAdvertisedDisplayWidth);
        android.util.Slog.d(TAG, "mAdvertisedDisplayHeight=" + this.mAdvertisedDisplayHeight);
        android.util.Slog.d(TAG, "mAdvertisedDisplayFlags=" + this.mAdvertisedDisplayFlags);
        android.util.Slog.d(TAG, "mAvailableWifiDisplayPeers: size=" + this.mAvailableWifiDisplayPeers.size());
        for (android.net.wifi.p2p.WifiP2pDevice device : this.mAvailableWifiDisplayPeers) {
            android.util.Slog.d(TAG, "  " + describeWifiP2pDevice(device));
        }
    }

    public void requestStartScan() {
        if (!this.mScanRequested) {
            this.mScanRequested = true;
            updateScanState();
        }
    }

    public void requestStopScan() {
        if (this.mScanRequested) {
            this.mScanRequested = false;
            updateScanState();
        }
    }

    public void requestConnect(java.lang.String address) {
        for (android.net.wifi.p2p.WifiP2pDevice device : this.mAvailableWifiDisplayPeers) {
            if (device.deviceAddress.equals(address)) {
                connect(device);
            }
        }
    }

    public void requestPause() {
        if (this.mRemoteDisplay != null) {
            this.mRemoteDisplay.pause();
        }
    }

    public void requestResume() {
        if (this.mRemoteDisplay != null) {
            this.mRemoteDisplay.resume();
        }
    }

    public void requestDisconnect() {
        disconnect();
    }

    private void updateWfdEnableState() {
        if (this.mWifiDisplayOnSetting && this.mWifiP2pEnabled) {
            if (!this.mWfdEnabled && !this.mWfdEnabling) {
                this.mWfdEnabling = true;
                android.net.wifi.p2p.WifiP2pWfdInfo wfdInfo = new android.net.wifi.p2p.WifiP2pWfdInfo();
                wfdInfo.setEnabled(true);
                wfdInfo.setDeviceType(0);
                wfdInfo.setSessionAvailable(true);
                wfdInfo.setControlPort(DEFAULT_CONTROL_PORT);
                wfdInfo.setMaxThroughput(50);
                this.mWifiP2pManager.setWfdInfo(this.mWifiP2pChannel, wfdInfo, new android.net.wifi.p2p.WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.2
                    @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                    public void onSuccess() {
                        if (com.android.server.display.WifiDisplayController.DEBUG) {
                            android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Successfully set WFD info.");
                        }
                        if (com.android.server.display.WifiDisplayController.this.mWfdEnabling) {
                            com.android.server.display.WifiDisplayController.this.mWfdEnabling = false;
                            com.android.server.display.WifiDisplayController.this.mWfdEnabled = true;
                            com.android.server.display.WifiDisplayController.this.reportFeatureState();
                            com.android.server.display.WifiDisplayController.this.updateScanState();
                        }
                    }

                    @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                    public void onFailure(int reason) {
                        if (com.android.server.display.WifiDisplayController.DEBUG) {
                            android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Failed to set WFD info with reason " + reason + ".");
                        }
                        com.android.server.display.WifiDisplayController.this.mWfdEnabling = false;
                    }
                });
                this.mOwduhWrapper.getExtImpl().reportWfdEnableState(wfdInfo, true, this.mContext);
                return;
            }
            return;
        }
        if (this.mWfdEnabled || this.mWfdEnabling) {
            android.net.wifi.p2p.WifiP2pWfdInfo wfdInfo2 = new android.net.wifi.p2p.WifiP2pWfdInfo();
            wfdInfo2.setEnabled(false);
            this.mOwduhWrapper.getExtImpl().reportWfdEnableState(wfdInfo2, false, this.mContext);
            this.mWifiP2pManager.setWfdInfo(this.mWifiP2pChannel, wfdInfo2, new android.net.wifi.p2p.WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.3
                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onSuccess() {
                    if (com.android.server.display.WifiDisplayController.DEBUG) {
                        android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Successfully set WFD info.");
                    }
                }

                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onFailure(int reason) {
                    if (com.android.server.display.WifiDisplayController.DEBUG) {
                        android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Failed to set WFD info with reason " + reason + ".");
                    }
                }
            });
        }
        this.mWfdEnabling = false;
        this.mWfdEnabled = false;
        reportFeatureState();
        updateScanState();
        disconnect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportFeatureState() {
        final int featureState = computeFeatureState();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayController.4
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.display.WifiDisplayController.this.mListener.onFeatureStateChanged(featureState);
            }
        });
    }

    private int computeFeatureState() {
        if (this.mWifiP2pEnabled) {
            return this.mWifiDisplayOnSetting ? 3 : 2;
        }
        if (this.mWifiDisplayOnSetting) {
            android.util.Slog.d(TAG, "Wifi p2p is disabled, update WIFI_DISPLAY_ON as false.");
            android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "wifi_display_on", 0);
            this.mWifiDisplayOnSetting = false;
            return 1;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScanState() {
        if ((this.mScanRequested && this.mWfdEnabled && this.mDesiredDevice == null && this.mConnectedDevice == null && this.mDisconnectingDevice == null) || (this.mScanRequested && this.mWfdEnabled && this.mWdcSocExt.getRemoveGroupFlag())) {
            if (!this.mDiscoverPeersInProgress) {
                android.util.Slog.i(TAG, "Starting Wifi display scan.");
                this.mDiscoverPeersInProgress = true;
                handleScanStarted();
                this.mOwduhWrapper.getExtImpl().getWfdScanState(true, this.mContext);
                tryDiscoverPeers();
                return;
            }
            return;
        }
        if (this.mDiscoverPeersInProgress) {
            this.mHandler.removeCallbacks(this.mDiscoverPeers);
            if (this.mDesiredDevice == null || this.mDesiredDevice == this.mConnectedDevice || this.mWdcSocExt.getRemoveGroupFlag()) {
                android.util.Slog.i(TAG, "Stopping Wifi display scan.");
                this.mDiscoverPeersInProgress = false;
                stopPeerDiscovery();
                handleScanFinished();
                this.mOwduhWrapper.getExtImpl().getWfdScanState(false, this.mContext);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryDiscoverPeers() {
        this.mWifiP2pManager.discoverPeers(this.mWifiP2pChannel, new android.net.wifi.p2p.WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.5
            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onSuccess() {
                if (com.android.server.display.WifiDisplayController.DEBUG) {
                    android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Discover peers succeeded.  Requesting peers now.");
                }
                if (com.android.server.display.WifiDisplayController.this.mDiscoverPeersInProgress) {
                    com.android.server.display.WifiDisplayController.this.requestPeers();
                }
                com.android.server.display.WifiDisplayController.this.mOwduhWrapper.getExtImpl().getDiscoverPeersState(true, com.android.server.display.WifiDisplayController.this.mContext);
            }

            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onFailure(int reason) {
                if (com.android.server.display.WifiDisplayController.DEBUG) {
                    android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Discover peers failed with reason " + reason + ".");
                }
            }
        });
        this.mHandler.postDelayed(this.mDiscoverPeers, 5000L);
    }

    private void stopPeerDiscovery() {
        this.mWifiP2pManager.stopPeerDiscovery(this.mWifiP2pChannel, new android.net.wifi.p2p.WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.6
            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onSuccess() {
                if (com.android.server.display.WifiDisplayController.DEBUG) {
                    android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Stop peer discovery succeeded.");
                }
            }

            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onFailure(int reason) {
                if (com.android.server.display.WifiDisplayController.DEBUG) {
                    android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Stop peer discovery failed with reason " + reason + ".");
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestPeers() {
        if (this.mWifiP2pManager == null) {
            android.util.Slog.d(TAG, "requestPeers failed, mWifiP2pManager is null");
        } else {
            this.mWifiP2pManager.requestPeers(this.mWifiP2pChannel, new android.net.wifi.p2p.WifiP2pManager.PeerListListener() { // from class: com.android.server.display.WifiDisplayController.7
                @Override // android.net.wifi.p2p.WifiP2pManager.PeerListListener
                public void onPeersAvailable(android.net.wifi.p2p.WifiP2pDeviceList peers) {
                    if (com.android.server.display.WifiDisplayController.DEBUG) {
                        android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Received list of peers.");
                    }
                    com.android.server.display.WifiDisplayController.this.mAvailableWifiDisplayPeers.clear();
                    for (android.net.wifi.p2p.WifiP2pDevice device : peers.getDeviceList()) {
                        if (com.android.server.display.WifiDisplayController.DEBUG) {
                            android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "  " + com.android.server.display.WifiDisplayController.describeWifiP2pDevice(device));
                        }
                        if (com.android.server.display.WifiDisplayController.isWifiDisplay(device)) {
                            com.android.server.display.WifiDisplayController.this.mAvailableWifiDisplayPeers.add(device);
                            com.android.server.display.WifiDisplayController.this.mOwduhWrapper.getExtImpl().getPeers(com.android.server.display.WifiDisplayController.this.mContext);
                        }
                    }
                    if (com.android.server.display.WifiDisplayController.this.mDiscoverPeersInProgress) {
                        com.android.server.display.WifiDisplayController.this.handleScanResults();
                    }
                }
            });
        }
    }

    private void handleScanStarted() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayController.8
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.display.WifiDisplayController.this.mListener.onScanStarted();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScanResults() {
        int count = this.mAvailableWifiDisplayPeers.size();
        final android.hardware.display.WifiDisplay[] displays = (android.hardware.display.WifiDisplay[]) android.hardware.display.WifiDisplay.CREATOR.newArray(count);
        for (int i = 0; i < count; i++) {
            android.net.wifi.p2p.WifiP2pDevice device = this.mAvailableWifiDisplayPeers.get(i);
            displays[i] = createWifiDisplay(device);
            updateDesiredDevice(device);
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayController.9
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.display.WifiDisplayController.this.mListener.onScanResults(displays);
            }
        });
    }

    private void handleScanFinished() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayController.10
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.display.WifiDisplayController.this.mListener.onScanFinished();
            }
        });
    }

    private void updateDesiredDevice(android.net.wifi.p2p.WifiP2pDevice device) {
        java.lang.String address = device.deviceAddress;
        if (this.mDesiredDevice != null && this.mDesiredDevice.deviceAddress.equals(address)) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "updateDesiredDevice: new information " + describeWifiP2pDevice(device));
            }
            this.mDesiredDevice.update(device);
            if (this.mAdvertisedDisplay != null && this.mAdvertisedDisplay.getDeviceAddress().equals(address)) {
                readvertiseDisplay(createWifiDisplay(this.mDesiredDevice));
            }
        }
    }

    private void connect(android.net.wifi.p2p.WifiP2pDevice device) {
        if (this.mDesiredDevice != null && !this.mDesiredDevice.deviceAddress.equals(device.deviceAddress)) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "connect: nothing to do, already connecting to " + describeWifiP2pDevice(device));
                return;
            }
            return;
        }
        if (this.mConnectedDevice != null && !this.mConnectedDevice.deviceAddress.equals(device.deviceAddress) && this.mDesiredDevice == null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "connect: nothing to do, already connected to " + describeWifiP2pDevice(device) + " and not part way through connecting to a different device.");
            }
        } else {
            if (!this.mWfdEnabled) {
                android.util.Slog.i(TAG, "Ignoring request to connect to Wifi display because the  feature is currently disabled: " + device.deviceName);
                return;
            }
            if (handlePreExistingConnection(device)) {
                android.util.Slog.i(TAG, "already handle the preexisting p2p connection status");
                return;
            }
            this.mDesiredDevice = device;
            this.mConnectionRetriesLeft = 3;
            this.mWFDState = 3;
            updateConnection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnect() {
        this.mDesiredDevice = null;
        updateConnection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retryConnection() {
        this.mDesiredDevice = new android.net.wifi.p2p.WifiP2pDevice(this.mDesiredDevice);
        updateConnection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConnection() {
        if (DEBUGV) {
            java.lang.StackTraceElement[] st = java.lang.Thread.currentThread().getStackTrace();
            for (int i = 2; i < st.length && i < 5; i++) {
                android.util.Slog.i(TAG, st[i].toString());
            }
            dump();
        }
        updateScanState();
        if (((this.mRemoteDisplay != null || this.mExtRemoteDisplay != null || this.mOplusRemoteDisplay != null) && this.mConnectedDevice != null && !this.mConnectedDevice.equals(this.mDesiredDevice)) || (this.mRemoteDisplayInterface != null && this.mConnectedDevice == null)) {
            android.util.Slog.i(TAG, "Stopped listening for RTSP connection on " + this.mRemoteDisplayInterface);
            this.mOwduhWrapper.getExtImpl().reportWfdConnectionTime(java.lang.Long.valueOf(java.lang.System.currentTimeMillis() - this.connectedTime), this.mConnectedDevice, this.mContext);
            this.mOwduhWrapper.getExtImpl().getWfdDisconnected(this.mContext);
            if (this.mRemoteDisplay != null) {
                this.mRemoteDisplay.dispose();
            } else if (this.mExtRemoteDisplay != null) {
                com.android.server.display.ExtendedRemoteDisplayHelper.dispose(this.mExtRemoteDisplay);
            }
            if (this.mOplusRemoteDisplay != null) {
                this.mWdcWrapper.getExtImpl().dispose(this.mOplusRemoteDisplay);
                this.mOplusRemoteDisplay = null;
            }
            this.mExtRemoteDisplay = null;
            this.mRemoteDisplay = null;
            this.mRemoteDisplayInterface = null;
            this.mHandler.removeCallbacks(this.mRtspTimeout);
            this.mWifiP2pManager.setMiracastMode(0);
            unadvertiseDisplay();
        }
        if (this.mRemoteDisplayConnected || this.mDisconnectingDevice != null) {
            return;
        }
        if (this.mConnectedDevice != null && !this.mConnectedDevice.equals(this.mDesiredDevice)) {
            android.util.Slog.i(TAG, "Disconnecting from Wifi display: " + this.mConnectedDevice.deviceName);
            this.mDisconnectingDevice = this.mConnectedDevice;
            this.mConnectedDevice = null;
            this.mConnectedDeviceGroupInfo = null;
            unadvertiseDisplay();
            final android.net.wifi.p2p.WifiP2pDevice oldDevice = this.mDisconnectingDevice;
            this.mWifiP2pManager.removeGroup(this.mWifiP2pChannel, new android.net.wifi.p2p.WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.11
                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onSuccess() {
                    android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Disconnected from Wifi display: " + oldDevice.deviceName);
                    next();
                }

                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onFailure(int reason) {
                    android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Failed to disconnect from Wifi display: " + oldDevice.deviceName + ", reason=" + reason);
                    next();
                }

                private void next() {
                    if (com.android.server.display.WifiDisplayController.this.mDisconnectingDevice == oldDevice) {
                        com.android.server.display.WifiDisplayController.this.mDisconnectingDevice = null;
                        com.android.server.display.WifiDisplayController.this.updateConnection();
                    }
                }
            });
            return;
        }
        android.net.wifi.p2p.WifiP2pDevice oldDevice2 = this.mCancelingDevice;
        if (oldDevice2 != null) {
            return;
        }
        if (this.mConnectingDevice != null && this.mConnectingDevice != this.mDesiredDevice) {
            android.util.Slog.i(TAG, "Canceling connection to Wifi display: " + this.mConnectingDevice.deviceName);
            this.mCancelingDevice = this.mConnectingDevice;
            this.mConnectingDevice = null;
            unadvertiseDisplay();
            this.mHandler.removeCallbacks(this.mConnectionTimeout);
            final android.net.wifi.p2p.WifiP2pDevice oldDevice3 = this.mCancelingDevice;
            this.mWifiP2pManager.cancelConnect(this.mWifiP2pChannel, new android.net.wifi.p2p.WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.12
                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onSuccess() {
                    android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Canceled connection to Wifi display: " + oldDevice3.deviceName);
                    next();
                }

                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onFailure(int reason) {
                    android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Failed to cancel connection to Wifi display: " + oldDevice3.deviceName + ", reason=" + reason);
                    next();
                }

                private void next() {
                    if (com.android.server.display.WifiDisplayController.this.mCancelingDevice == oldDevice3) {
                        com.android.server.display.WifiDisplayController.this.mCancelingDevice = null;
                        com.android.server.display.WifiDisplayController.this.updateConnection();
                    }
                }
            });
            return;
        }
        android.net.wifi.p2p.WifiP2pDevice oldDevice4 = this.mDesiredDevice;
        if (oldDevice4 == null) {
            if (this.mWifiDisplayCertMode) {
                this.mListener.onDisplaySessionInfo(getSessionInfo(this.mConnectedDeviceGroupInfo, 0));
            }
            unadvertiseDisplay();
            return;
        }
        final android.net.wifi.p2p.WifiP2pDevice oldDevice5 = this.mDesiredDevice;
        android.media.RemoteDisplay.Listener listener = new android.media.RemoteDisplay.Listener() { // from class: com.android.server.display.WifiDisplayController.13
            public void onDisplayConnected(android.view.Surface surface, int width, int height, int flags, int session) {
                if (com.android.server.display.WifiDisplayController.this.mConnectedDevice == oldDevice5 && !com.android.server.display.WifiDisplayController.this.mRemoteDisplayConnected) {
                    android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Opened RTSP connection with Wifi display: " + com.android.server.display.WifiDisplayController.this.mConnectedDevice.deviceName);
                    com.android.server.display.WifiDisplayController.this.mRemoteDisplayConnected = true;
                    com.android.server.display.WifiDisplayController.this.mHandler.removeCallbacks(com.android.server.display.WifiDisplayController.this.mRtspTimeout);
                    if (com.android.server.display.WifiDisplayController.this.mWifiDisplayCertMode) {
                        com.android.server.display.WifiDisplayController.this.mListener.onDisplaySessionInfo(com.android.server.display.WifiDisplayController.this.getSessionInfo(com.android.server.display.WifiDisplayController.this.mConnectedDeviceGroupInfo, session));
                    }
                    android.hardware.display.WifiDisplay display = com.android.server.display.WifiDisplayController.createWifiDisplay(com.android.server.display.WifiDisplayController.this.mConnectedDevice);
                    com.android.server.display.WifiDisplayController.this.advertiseDisplay(display, surface, width, height, flags);
                }
                com.android.server.display.WifiDisplayController.this.mOwduhWrapper.getExtImpl().wfdConnecteSuceess(com.android.server.display.WifiDisplayController.this.mConnectedDeviceGroupInfo, com.android.server.display.WifiDisplayController.this.mConnectedDevice, com.android.server.display.WifiDisplayController.this.mContext);
                com.android.server.display.WifiDisplayController.this.mWFDState = 4;
                com.android.server.display.WifiDisplayController.this.connectedTime = java.lang.System.currentTimeMillis();
                if (com.android.server.display.WifiDisplayController.this.mOplusRemoteDisplay != null) {
                    com.android.server.display.WifiDisplayController.this.mWdcWrapper.getExtImpl().setOplusWifiDisplayInfo(com.android.server.display.WifiDisplayController.this.mConnectedDevice);
                }
            }

            public void onDisplayDisconnected() {
                if (com.android.server.display.WifiDisplayController.this.mConnectedDevice == oldDevice5) {
                    android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Closed RTSP connection with Wifi display: " + com.android.server.display.WifiDisplayController.this.mConnectedDevice.deviceName);
                    com.android.server.display.WifiDisplayController.this.mHandler.removeCallbacks(com.android.server.display.WifiDisplayController.this.mRtspTimeout);
                    com.android.server.display.WifiDisplayController.this.mRemoteDisplayConnected = false;
                    com.android.server.display.WifiDisplayController.this.disconnect();
                }
            }

            public void onDisplayError(int error) {
                if (com.android.server.display.WifiDisplayController.this.mConnectedDevice == oldDevice5) {
                    android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Lost RTSP connection with Wifi display due to error " + error + ": " + com.android.server.display.WifiDisplayController.this.mConnectedDevice.deviceName);
                    com.android.server.display.WifiDisplayController.this.mHandler.removeCallbacks(com.android.server.display.WifiDisplayController.this.mRtspTimeout);
                    com.android.server.display.WifiDisplayController.this.handleConnectionFailure(false);
                    com.android.server.display.WifiDisplayController.this.mOwduhWrapper.getExtImpl().wfdConnectedFailed("Lost_RTSP_Connection", com.android.server.display.WifiDisplayController.this.mConnectedDevice, com.android.server.display.WifiDisplayController.this.mConnectedDeviceGroupInfo, com.android.server.display.WifiDisplayController.this.mContext);
                }
            }
        };
        if (this.mConnectedDevice == null && this.mConnectingDevice == null) {
            android.util.Slog.i(TAG, "Connecting to Wifi display: " + this.mDesiredDevice.deviceName);
            this.mConnectingDevice = this.mDesiredDevice;
            android.net.wifi.p2p.WifiP2pConfig config = new android.net.wifi.p2p.WifiP2pConfig();
            android.net.wifi.WpsInfo wps = new android.net.wifi.WpsInfo();
            if (this.mWifiDisplayWpsConfig != 4) {
                wps.setup = this.mWifiDisplayWpsConfig;
            } else if (this.mConnectingDevice.wpsPbcSupported()) {
                wps.setup = 0;
            } else if (this.mConnectingDevice.wpsDisplaySupported()) {
                wps.setup = 2;
            } else {
                wps.setup = 1;
            }
            config.wps = wps;
            config.deviceAddress = this.mConnectingDevice.deviceAddress;
            android.net.wifi.p2p.WifiP2pConfig config2 = this.mWdcWrapper.getExtImpl().generateConfigByGoIntent(config, this.mConnectingDevice);
            android.hardware.display.WifiDisplay display = createWifiDisplay(this.mConnectingDevice);
            advertiseDisplay(display, null, 0, 0, 0);
            if (!this.mWdcWrapper.getExtImpl().isOplusRemoteDisplayAvailable() && com.android.server.display.ExtendedRemoteDisplayHelper.isAvailable() && this.mExtRemoteDisplay == null) {
                int port = getPortNumber(this.mDesiredDevice);
                java.lang.String iface = "255.255.255.255:" + port;
                this.mRemoteDisplayInterface = iface;
                android.util.Slog.i(TAG, "Listening for RTSP connection on " + iface + " from Wifi display: " + this.mDesiredDevice.deviceName);
                this.mExtRemoteDisplay = com.android.server.display.ExtendedRemoteDisplayHelper.listen(iface, listener, this.mHandler, this.mContext);
            }
            final android.net.wifi.p2p.WifiP2pDevice newDevice = this.mDesiredDevice;
            this.mWifiP2pManager.connect(this.mWifiP2pChannel, config2, new android.net.wifi.p2p.WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.14
                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onSuccess() {
                    android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Initiated connection to Wifi display: " + newDevice.deviceName);
                    com.android.server.display.WifiDisplayController.this.mHandler.postDelayed(com.android.server.display.WifiDisplayController.this.mConnectionTimeout, 30000L);
                }

                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onFailure(int reason) {
                    if (com.android.server.display.WifiDisplayController.this.mConnectingDevice == newDevice) {
                        android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Failed to initiate connection to Wifi display: " + newDevice.deviceName + ", reason=" + reason);
                        com.android.server.display.WifiDisplayController.this.mConnectingDevice = null;
                        com.android.server.display.WifiDisplayController.this.handleConnectionFailure(false);
                        com.android.server.display.WifiDisplayController.this.mOwduhWrapper.getExtImpl().wfdConnectedFailed("P2P_Fail_Connect", com.android.server.display.WifiDisplayController.this.mConnectingDevice, com.android.server.display.WifiDisplayController.this.mConnectedDeviceGroupInfo, com.android.server.display.WifiDisplayController.this.mContext);
                    }
                }
            });
            return;
        }
        if (this.mConnectedDevice != null && this.mRemoteDisplay == null && this.mOplusRemoteDisplay == null) {
            java.net.Inet4Address addr = getInterfaceAddress(this.mConnectedDeviceGroupInfo);
            if (addr == null) {
                android.util.Slog.i(TAG, "Failed to get local interface address for communicating with Wifi display: " + this.mConnectedDevice.deviceName);
                handleConnectionFailure(false);
                this.mOwduhWrapper.getExtImpl().wfdConnectedFailed("P2P_Addr_NULL", this.mConnectedDevice, this.mConnectedDeviceGroupInfo, this.mContext);
                return;
            }
            this.mWifiP2pManager.setMiracastMode(1);
            int port2 = getPortNumber(this.mConnectedDevice);
            java.lang.String iface2 = addr.getHostAddress() + ":" + port2;
            this.mRemoteDisplayInterface = iface2;
            if (this.mWdcWrapper.getExtImpl().isOplusRemoteDisplayAvailable()) {
                org.json.JSONObject wifiDisplayInfo = new org.json.JSONObject();
                try {
                    wifiDisplayInfo.put("deviceName", this.mConnectedDevice.deviceName);
                    wifiDisplayInfo.put("primaryDeviceType", this.mConnectedDevice.primaryDeviceType);
                    wifiDisplayInfo.put("deviceAddress", this.mConnectedDevice.deviceAddress);
                    android.util.Slog.i(TAG, "Listening for RTSP connection on " + iface2 + " from Oplus Wifi Display: " + this.mConnectedDevice.deviceName);
                    this.mOplusRemoteDisplay = this.mWdcWrapper.getExtImpl().listen(iface2, listener, this.mHandler, wifiDisplayInfo.toString());
                } catch (org.json.JSONException e) {
                    android.util.Slog.e(TAG, "catch json exception in Oplus Wifi Display:" + e.getMessage());
                    return;
                }
            } else if (!com.android.server.display.ExtendedRemoteDisplayHelper.isAvailable()) {
                android.util.Slog.i(TAG, "Listening for RTSP connection on " + iface2 + " from Wifi display: " + this.mConnectedDevice.deviceName);
                this.mRemoteDisplay = android.media.RemoteDisplay.listen(iface2, listener, this.mHandler, this.mContext.getOpPackageName());
            }
            int rtspTimeout = this.mWifiDisplayCertMode ? 120 : 30;
            this.mHandler.postDelayed(this.mRtspTimeout, rtspTimeout * 1000);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.hardware.display.WifiDisplaySessionInfo getSessionInfo(android.net.wifi.p2p.WifiP2pGroup info, int session) {
        if (info == null || info.getOwner() == null) {
            return null;
        }
        java.net.Inet4Address addr = getInterfaceAddress(info);
        android.hardware.display.WifiDisplaySessionInfo sessionInfo = new android.hardware.display.WifiDisplaySessionInfo(!info.getOwner().deviceAddress.equals(this.mThisDevice.deviceAddress), session, info.getOwner().deviceAddress + " " + info.getNetworkName(), info.getPassphrase(), addr != null ? addr.getHostAddress() : "");
        if (DEBUG) {
            android.util.Slog.d(TAG, sessionInfo.toString());
        }
        this.mOwduhWrapper.getExtImpl().getSessionInfo(!info.getOwner().deviceAddress.equals(this.mThisDevice.deviceAddress), info.getOwner().deviceAddress + " " + info.getNetworkName(), addr != null ? addr.getHostAddress() : "", this.mContext);
        return sessionInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStateChanged(boolean enabled) {
        this.mWifiP2pEnabled = enabled;
        if (enabled) {
            retrieveWifiP2pManagerAndChannel();
        }
        updateWfdEnableState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePeersChanged() {
        requestPeers();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean contains(android.net.wifi.p2p.WifiP2pGroup group, android.net.wifi.p2p.WifiP2pDevice device) {
        return group.getOwner().equals(device) || group.getClientList().contains(device);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectionChanged(android.net.NetworkInfo networkInfo) {
        this.mNetworkInfo = networkInfo;
        if (this.mWfdEnabled && networkInfo.isConnected()) {
            if (this.mDesiredDevice != null || this.mWifiDisplayCertMode) {
                this.mWifiP2pManager.requestGroupInfo(this.mWifiP2pChannel, new android.net.wifi.p2p.WifiP2pManager.GroupInfoListener() { // from class: com.android.server.display.WifiDisplayController.15
                    @Override // android.net.wifi.p2p.WifiP2pManager.GroupInfoListener
                    public void onGroupInfoAvailable(android.net.wifi.p2p.WifiP2pGroup info) {
                        if (info == null) {
                            return;
                        }
                        if (com.android.server.display.WifiDisplayController.DEBUG) {
                            android.util.Slog.d(com.android.server.display.WifiDisplayController.TAG, "Received group info: " + com.android.server.display.WifiDisplayController.describeWifiP2pGroup(info));
                        }
                        if (com.android.server.display.WifiDisplayController.this.mConnectingDevice != null && !com.android.server.display.WifiDisplayController.contains(info, com.android.server.display.WifiDisplayController.this.mConnectingDevice)) {
                            android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Aborting connection to Wifi display because the current P2P group does not contain the device we expected to find: " + com.android.server.display.WifiDisplayController.this.mConnectingDevice.deviceName + ", group info was: " + com.android.server.display.WifiDisplayController.describeWifiP2pGroup(info));
                            com.android.server.display.WifiDisplayController.this.mOwduhWrapper.getExtImpl().wfdConnectedFailed("P2P_Group_Fail", com.android.server.display.WifiDisplayController.this.mConnectingDevice, com.android.server.display.WifiDisplayController.this.mConnectedDeviceGroupInfo, com.android.server.display.WifiDisplayController.this.mContext);
                            com.android.server.display.WifiDisplayController.this.handleConnectionFailure(false);
                            return;
                        }
                        if (com.android.server.display.WifiDisplayController.this.mDesiredDevice != null && !com.android.server.display.WifiDisplayController.contains(info, com.android.server.display.WifiDisplayController.this.mDesiredDevice)) {
                            com.android.server.display.WifiDisplayController.this.disconnect();
                            return;
                        }
                        if (com.android.server.display.WifiDisplayController.this.mWifiDisplayCertMode) {
                            boolean owner = info.getOwner() != null ? info.getOwner().deviceAddress.equals(com.android.server.display.WifiDisplayController.this.mThisDevice.deviceAddress) : false;
                            if (owner && info.getClientList().isEmpty()) {
                                com.android.server.display.WifiDisplayController wifiDisplayController = com.android.server.display.WifiDisplayController.this;
                                com.android.server.display.WifiDisplayController.this.mDesiredDevice = null;
                                wifiDisplayController.mConnectingDevice = null;
                                com.android.server.display.WifiDisplayController.this.mConnectedDeviceGroupInfo = info;
                                com.android.server.display.WifiDisplayController.this.updateConnection();
                            } else if (com.android.server.display.WifiDisplayController.this.mConnectingDevice == null && com.android.server.display.WifiDisplayController.this.mDesiredDevice == null) {
                                com.android.server.display.WifiDisplayController wifiDisplayController2 = com.android.server.display.WifiDisplayController.this;
                                com.android.server.display.WifiDisplayController wifiDisplayController3 = com.android.server.display.WifiDisplayController.this;
                                android.net.wifi.p2p.WifiP2pDevice next = owner ? info.getClientList().iterator().next() : info.getOwner();
                                wifiDisplayController3.mDesiredDevice = next;
                                wifiDisplayController2.mConnectingDevice = next;
                            }
                        }
                        if (com.android.server.display.WifiDisplayController.this.mConnectingDevice != null && com.android.server.display.WifiDisplayController.this.mConnectingDevice.equals(com.android.server.display.WifiDisplayController.this.mDesiredDevice)) {
                            android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Connected to Wifi display: " + com.android.server.display.WifiDisplayController.this.mConnectingDevice.deviceName);
                            com.android.server.display.WifiDisplayController.this.mHandler.removeCallbacks(com.android.server.display.WifiDisplayController.this.mConnectionTimeout);
                            com.android.server.display.WifiDisplayController.this.mConnectedDeviceGroupInfo = info;
                            com.android.server.display.WifiDisplayController.this.mConnectedDevice = com.android.server.display.WifiDisplayController.this.mConnectingDevice;
                            com.android.server.display.WifiDisplayController.this.mConnectingDevice = null;
                            com.android.server.display.WifiDisplayController.this.updateConnection();
                        }
                    }
                });
                return;
            }
            return;
        }
        if (!networkInfo.isConnectedOrConnecting()) {
            this.mConnectedDeviceGroupInfo = null;
            if (this.mConnectingDevice != null || this.mConnectedDevice != null) {
                disconnect();
            }
            if (this.mWFDState == 3) {
                android.util.Slog.d(TAG, "WFD connect Fail.");
                this.mOwduhWrapper.getExtImpl().wfdConnectedFailed("P2P_Fail_Connect", this.mConnectingDevice, this.mConnectedDeviceGroupInfo, this.mContext);
            }
            this.mWFDState = 0;
            if (this.mDesiredDevice != null && !this.mWdcSocExt.getRemoveGroupFlag()) {
                android.util.Slog.i(TAG, "reconnect new device: " + this.mDesiredDevice.deviceName);
                updateConnection();
            } else if (this.mWfdEnabled) {
                requestPeers();
                if (this.mWdcSocExt.getRemoveGroupFlag()) {
                    this.mWdcSocExt.setReConnectDevice(this.mDesiredDevice);
                }
                this.mWdcSocExt.checkReConnect();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectionFailure(boolean timeoutOccurred) {
        android.util.Slog.i(TAG, "Wifi display connection failed!");
        if (this.mDesiredDevice != null) {
            if (this.mConnectionRetriesLeft > 0) {
                final android.net.wifi.p2p.WifiP2pDevice oldDevice = this.mDesiredDevice;
                this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayController.19
                    @Override // java.lang.Runnable
                    public void run() {
                        if (com.android.server.display.WifiDisplayController.this.mDesiredDevice == oldDevice && com.android.server.display.WifiDisplayController.this.mConnectionRetriesLeft > 0) {
                            com.android.server.display.WifiDisplayController wifiDisplayController = com.android.server.display.WifiDisplayController.this;
                            wifiDisplayController.mConnectionRetriesLeft--;
                            android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Retrying Wifi display connection.  Retries left: " + com.android.server.display.WifiDisplayController.this.mConnectionRetriesLeft);
                            com.android.server.display.WifiDisplayController.this.retryConnection();
                        }
                    }
                }, timeoutOccurred ? 0L : 500L);
            } else {
                disconnect();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void advertiseDisplay(final android.hardware.display.WifiDisplay display, final android.view.Surface surface, final int width, final int height, final int flags) {
        if (!java.util.Objects.equals(this.mAdvertisedDisplay, display) || this.mAdvertisedDisplaySurface != surface || this.mAdvertisedDisplayWidth != width || this.mAdvertisedDisplayHeight != height || this.mAdvertisedDisplayFlags != flags) {
            final android.hardware.display.WifiDisplay oldDisplay = this.mAdvertisedDisplay;
            final android.view.Surface oldSurface = this.mAdvertisedDisplaySurface;
            this.mAdvertisedDisplay = display;
            this.mAdvertisedDisplaySurface = surface;
            this.mAdvertisedDisplayWidth = width;
            this.mAdvertisedDisplayHeight = height;
            this.mAdvertisedDisplayFlags = flags;
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayController.20
                @Override // java.lang.Runnable
                public void run() {
                    if (oldSurface != null && surface != oldSurface) {
                        com.android.server.display.WifiDisplayController.this.mListener.onDisplayDisconnected();
                        com.android.server.display.WifiDisplayController.this.mWdcSocExt.setWFD(false);
                    } else if (oldDisplay != null && !oldDisplay.hasSameAddress(display)) {
                        com.android.server.display.WifiDisplayController.this.mListener.onDisplayConnectionFailed();
                        com.android.server.display.WifiDisplayController.this.mWdcSocExt.setWFD(false);
                    }
                    if (display != null) {
                        if (!display.hasSameAddress(oldDisplay)) {
                            com.android.server.display.WifiDisplayController.this.mListener.onDisplayConnecting(display);
                        } else if (!display.equals(oldDisplay)) {
                            com.android.server.display.WifiDisplayController.this.mListener.onDisplayChanged(display);
                        }
                        if (surface != null && surface != oldSurface) {
                            com.android.server.display.WifiDisplayController.this.mListener.onDisplayConnected(display, surface, width, height, flags);
                            com.android.server.display.WifiDisplayController.this.mWdcSocExt.setWFD(true);
                        }
                    }
                }
            });
        }
    }

    private void unadvertiseDisplay() {
        advertiseDisplay(null, null, 0, 0, 0);
    }

    private void readvertiseDisplay(android.hardware.display.WifiDisplay display) {
        advertiseDisplay(display, this.mAdvertisedDisplaySurface, this.mAdvertisedDisplayWidth, this.mAdvertisedDisplayHeight, this.mAdvertisedDisplayFlags);
    }

    private boolean handlePreExistingConnection(final android.net.wifi.p2p.WifiP2pDevice device) {
        if (this.mNetworkInfo == null || !this.mNetworkInfo.isConnected() || this.mWifiDisplayCertMode) {
            return false;
        }
        android.util.Slog.i(TAG, "handle the preexisting p2p connection status");
        this.mWifiP2pManager.requestGroupInfo(this.mWifiP2pChannel, new android.net.wifi.p2p.WifiP2pManager.GroupInfoListener() { // from class: com.android.server.display.WifiDisplayController.21
            @Override // android.net.wifi.p2p.WifiP2pManager.GroupInfoListener
            public void onGroupInfoAvailable(android.net.wifi.p2p.WifiP2pGroup info) {
                if (info == null) {
                    return;
                }
                if (com.android.server.display.WifiDisplayController.contains(info, device)) {
                    android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "already connected to the desired device: " + device.deviceName);
                    com.android.server.display.WifiDisplayController.this.updateConnection();
                    com.android.server.display.WifiDisplayController.this.handleConnectionChanged(com.android.server.display.WifiDisplayController.this.mNetworkInfo);
                } else {
                    com.android.server.display.WifiDisplayController.this.mWdcSocExt.setRemoveGroupFlag(true);
                    com.android.server.display.WifiDisplayController.this.mWifiP2pManager.removeGroup(com.android.server.display.WifiDisplayController.this.mWifiP2pChannel, new android.net.wifi.p2p.WifiP2pManager.ActionListener() { // from class: com.android.server.display.WifiDisplayController.21.1
                        @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                        public void onSuccess() {
                            android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "disconnect the old device");
                        }

                        @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                        public void onFailure(int reason) {
                            android.util.Slog.i(com.android.server.display.WifiDisplayController.TAG, "Failed to disconnect the old device: reason=" + reason);
                        }
                    });
                }
            }
        });
        this.mDesiredDevice = device;
        this.mConnectionRetriesLeft = 3;
        return true;
    }

    private static java.net.Inet4Address getInterfaceAddress(android.net.wifi.p2p.WifiP2pGroup info) {
        try {
            java.net.NetworkInterface iface = java.net.NetworkInterface.getByName(info.getInterface());
            java.util.Enumeration<java.net.InetAddress> addrs = iface.getInetAddresses();
            while (addrs.hasMoreElements()) {
                java.net.InetAddress addr = addrs.nextElement();
                if (addr instanceof java.net.Inet4Address) {
                    return (java.net.Inet4Address) addr;
                }
            }
            android.util.Slog.w(TAG, "Could not obtain address of network interface " + info.getInterface() + " because it had no IPv4 addresses.");
            return null;
        } catch (java.net.SocketException ex) {
            android.util.Slog.w(TAG, "Could not obtain address of network interface " + info.getInterface(), ex);
            return null;
        }
    }

    private static int getPortNumber(android.net.wifi.p2p.WifiP2pDevice device) {
        if (device.deviceName.startsWith("DIRECT-") && device.deviceName.endsWith("Broadcom")) {
            return 8554;
        }
        return DEFAULT_CONTROL_PORT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isWifiDisplay(android.net.wifi.p2p.WifiP2pDevice device) {
        android.net.wifi.p2p.WifiP2pWfdInfo wfdInfo = device.getWfdInfo();
        return wfdInfo != null && wfdInfo.isEnabled() && isPrimarySinkDeviceType(wfdInfo.getDeviceType());
    }

    private static boolean isPrimarySinkDeviceType(int deviceType) {
        return deviceType == 1 || deviceType == 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String describeWifiP2pDevice(android.net.wifi.p2p.WifiP2pDevice device) {
        return device != null ? device.toString().replace('\n', ',') : "null";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String describeWifiP2pGroup(android.net.wifi.p2p.WifiP2pGroup group) {
        return group != null ? group.toString().replace('\n', ',') : "null";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.hardware.display.WifiDisplay createWifiDisplay(android.net.wifi.p2p.WifiP2pDevice device) {
        android.net.wifi.p2p.WifiP2pWfdInfo wfdInfo = device.getWfdInfo();
        boolean isSessionAvailable = wfdInfo != null && wfdInfo.isSessionAvailable();
        return new android.hardware.display.WifiDisplay(device.deviceAddress, device.deviceName, (java.lang.String) null, true, isSessionAvailable, false);
    }

    public com.android.server.display.IWifiDisplayControllerWrapper getWrapper() {
        return this.mWdcWrapper;
    }

    private class WifiDisplayControllerWrapper implements com.android.server.display.IWifiDisplayControllerWrapper {
        private com.android.server.display.IWifiDisplayControllerExt mWdcExt;

        private WifiDisplayControllerWrapper() {
            this.mWdcExt = (com.android.server.display.IWifiDisplayControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IWifiDisplayControllerExt.class).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.display.IWifiDisplayControllerExt getExtImpl() {
            return this.mWdcExt;
        }
    }

    public com.android.server.display.IOplusWifiDisplayUsageHelperWrapper getOplusWifiDisplayUsageHelperWrapper() {
        return this.mOwduhWrapper;
    }

    private class OplusWifiDisplayUsageHelperWrapper implements com.android.server.display.IOplusWifiDisplayUsageHelperWrapper {
        private com.android.server.display.IOplusWifiDisplayUsageHelperExt mOwduhExt;

        private OplusWifiDisplayUsageHelperWrapper() {
            this.mOwduhExt = (com.android.server.display.IOplusWifiDisplayUsageHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IOplusWifiDisplayUsageHelperExt.class).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.display.IOplusWifiDisplayUsageHelperExt getExtImpl() {
            return this.mOwduhExt;
        }
    }
}
