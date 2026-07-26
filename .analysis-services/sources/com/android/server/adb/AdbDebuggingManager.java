package com.android.server.adb;

/* JADX INFO: loaded from: classes.dex */
public class AdbDebuggingManager {
    private static final java.lang.String ADBD_SOCKET = "adbd";
    private static final java.lang.String ADB_DIRECTORY = "misc/adb";
    private static final java.lang.String ADB_KEYS_FILE = "adb_keys";
    private static final java.lang.String ADB_TEMP_KEYS_FILE = "adb_temp_keys.xml";
    private static final int BUFFER_SIZE = 65536;
    private static final boolean DEBUG = false;
    private static final boolean MDNS_DEBUG = false;
    private static final int PAIRING_CODE_LENGTH = 6;
    private static final java.lang.String WIFI_PERSISTENT_CONFIG_PROPERTY = "persist.adb.tls_server.enable";
    private static final java.lang.String WIFI_PERSISTENT_GUID = "persist.adb.wifi.guid";
    private com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo mAdbConnectionInfo;
    private boolean mAdbUsbEnabled;
    private boolean mAdbWifiEnabled;
    private final java.lang.String mConfirmComponent;
    private final java.util.Map<java.lang.String, java.lang.Integer> mConnectedKeys;
    private com.android.server.adb.AdbDebuggingManager.AdbConnectionPortPoller mConnectionPortPoller;
    private final android.content.ContentResolver mContentResolver;
    private final android.content.Context mContext;
    private java.lang.String mFingerprints;
    final com.android.server.adb.AdbDebuggingManager.AdbDebuggingHandler mHandler;
    private com.android.server.adb.AdbDebuggingManager.PairingThread mPairingThread;
    private final com.android.server.adb.AdbDebuggingManager.PortListenerImpl mPortListener;
    private final java.io.File mTempKeysFile;
    private com.android.server.adb.AdbDebuggingManager.AdbDebuggingThread mThread;
    private final com.android.server.adb.AdbDebuggingManager.Ticker mTicker;
    private final java.io.File mUserKeyFile;
    private final java.util.Set<java.lang.String> mWifiConnectedKeys;
    private static final java.lang.String TAG = com.android.server.adb.AdbDebuggingManager.class.getSimpleName();
    private static final com.android.server.adb.AdbDebuggingManager.Ticker SYSTEM_TICKER = new com.android.server.adb.AdbDebuggingManager.Ticker() { // from class: com.android.server.adb.AdbDebuggingManager$$ExternalSyntheticLambda0
        @Override // com.android.server.adb.AdbDebuggingManager.Ticker
        public final long currentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }
    };
    private static final long ADBD_STATE_CHANGE_TIMEOUT = android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;

    interface AdbConnectionPortListener {
        void onPortReceived(int i);
    }

    interface Ticker {
        long currentTimeMillis();
    }

    public AdbDebuggingManager(android.content.Context context) {
        this(context, null, getAdbFile(ADB_KEYS_FILE), getAdbFile(ADB_TEMP_KEYS_FILE), null, SYSTEM_TICKER);
    }

    AdbDebuggingManager(android.content.Context context, java.lang.String confirmComponent, java.io.File testUserKeyFile, java.io.File tempKeysFile, com.android.server.adb.AdbDebuggingManager.AdbDebuggingThread adbDebuggingThread, com.android.server.adb.AdbDebuggingManager.Ticker ticker) {
        this.mAdbUsbEnabled = false;
        this.mAdbWifiEnabled = false;
        this.mConnectedKeys = new java.util.HashMap();
        this.mPairingThread = null;
        this.mWifiConnectedKeys = new java.util.HashSet();
        this.mAdbConnectionInfo = new com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo();
        this.mPortListener = new com.android.server.adb.AdbDebuggingManager.PortListenerImpl();
        this.mContext = context;
        this.mContentResolver = this.mContext.getContentResolver();
        this.mConfirmComponent = confirmComponent;
        this.mUserKeyFile = testUserKeyFile;
        this.mTempKeysFile = tempKeysFile;
        this.mThread = adbDebuggingThread;
        this.mTicker = ticker;
        this.mHandler = new com.android.server.adb.AdbDebuggingManager.AdbDebuggingHandler(com.android.server.FgThread.get().getLooper(), this.mThread);
    }

    static void sendBroadcastWithDebugPermission(android.content.Context context, android.content.Intent intent, android.os.UserHandle userHandle) {
        context.sendBroadcastAsUser(intent, userHandle, "android.permission.MANAGE_DEBUGGING");
    }

    class PairingThread extends java.lang.Thread implements android.net.nsd.NsdManager.RegistrationListener {
        static final java.lang.String SERVICE_PROTOCOL = "adb-tls-pairing";
        private java.lang.String mGuid;
        private android.net.nsd.NsdManager mNsdManager;
        private java.lang.String mPairingCode;
        private int mPort;
        private java.lang.String mPublicKey;
        private java.lang.String mServiceName;
        private final java.lang.String mServiceType;

        private native void native_pairing_cancel();

        private native int native_pairing_start(java.lang.String str, java.lang.String str2);

        private native boolean native_pairing_wait();

        PairingThread(java.lang.String pairingCode, java.lang.String serviceName) {
            super(com.android.server.adb.AdbDebuggingManager.TAG);
            this.mServiceType = java.lang.String.format("_%s._tcp.", SERVICE_PROTOCOL);
            this.mPairingCode = pairingCode;
            this.mGuid = android.os.SystemProperties.get(com.android.server.adb.AdbDebuggingManager.WIFI_PERSISTENT_GUID);
            this.mServiceName = serviceName;
            if (serviceName == null || serviceName.isEmpty()) {
                this.mServiceName = this.mGuid;
            }
            this.mPort = -1;
            this.mNsdManager = (android.net.nsd.NsdManager) com.android.server.adb.AdbDebuggingManager.this.mContext.getSystemService("servicediscovery");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            android.net.nsd.NsdServiceInfo serviceInfo = new android.net.nsd.NsdServiceInfo();
            serviceInfo.setServiceName(this.mServiceName);
            serviceInfo.setServiceType(this.mServiceType);
            serviceInfo.setPort(this.mPort);
            this.mNsdManager.registerService(serviceInfo, 1, this);
            android.os.Message msg = com.android.server.adb.AdbDebuggingManager.this.mHandler.obtainMessage(21);
            msg.obj = java.lang.Integer.valueOf(this.mPort);
            com.android.server.adb.AdbDebuggingManager.this.mHandler.sendMessage(msg);
            boolean paired = native_pairing_wait();
            this.mNsdManager.unregisterService(this);
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putString("publicKey", paired ? this.mPublicKey : null);
            android.os.Message message = android.os.Message.obtain(com.android.server.adb.AdbDebuggingManager.this.mHandler, 20, bundle);
            com.android.server.adb.AdbDebuggingManager.this.mHandler.sendMessage(message);
        }

        @Override // java.lang.Thread
        public void start() {
            if (this.mGuid.isEmpty()) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "adbwifi guid was not set");
                return;
            }
            this.mPort = native_pairing_start(this.mGuid, this.mPairingCode);
            if (this.mPort <= 0) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Unable to start pairing server");
            } else {
                super.start();
            }
        }

        public void cancelPairing() {
            native_pairing_cancel();
        }

        @Override // android.net.nsd.NsdManager.RegistrationListener
        public void onServiceRegistered(android.net.nsd.NsdServiceInfo serviceInfo) {
        }

        @Override // android.net.nsd.NsdManager.RegistrationListener
        public void onRegistrationFailed(android.net.nsd.NsdServiceInfo serviceInfo, int errorCode) {
            android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Failed to register pairing service(err=" + errorCode + "): " + serviceInfo);
            cancelPairing();
        }

        @Override // android.net.nsd.NsdManager.RegistrationListener
        public void onServiceUnregistered(android.net.nsd.NsdServiceInfo serviceInfo) {
        }

        @Override // android.net.nsd.NsdManager.RegistrationListener
        public void onUnregistrationFailed(android.net.nsd.NsdServiceInfo serviceInfo, int errorCode) {
            android.util.Slog.w(com.android.server.adb.AdbDebuggingManager.TAG, "Failed to unregister pairing service(err=" + errorCode + "): " + serviceInfo);
        }
    }

    static class AdbConnectionPortPoller extends java.lang.Thread {
        private com.android.server.adb.AdbDebuggingManager.AdbConnectionPortListener mListener;
        private final java.lang.String mAdbPortProp = "service.adb.tls.port";
        private final int mDurationSecs = 10;
        private java.util.concurrent.atomic.AtomicBoolean mCanceled = new java.util.concurrent.atomic.AtomicBoolean(false);

        AdbConnectionPortPoller(com.android.server.adb.AdbDebuggingManager.AdbConnectionPortListener listener) {
            this.mListener = listener;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            for (int i = 0; i < 10; i++) {
                if (this.mCanceled.get()) {
                    return;
                }
                int port = android.os.SystemProperties.getInt("service.adb.tls.port", Integer.MAX_VALUE);
                if (port == -1 || (port > 0 && port <= 65535)) {
                    this.mListener.onPortReceived(port);
                    return;
                }
                android.os.SystemClock.sleep(1000L);
            }
            android.util.Slog.w(com.android.server.adb.AdbDebuggingManager.TAG, "Failed to receive adb connection port");
            this.mListener.onPortReceived(-1);
        }

        public void cancelAndWait() {
            this.mCanceled.set(true);
            if (isAlive()) {
                try {
                    join();
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
    }

    class PortListenerImpl implements com.android.server.adb.AdbDebuggingManager.AdbConnectionPortListener {
        PortListenerImpl() {
        }

        @Override // com.android.server.adb.AdbDebuggingManager.AdbConnectionPortListener
        public void onPortReceived(int port) {
            int i;
            com.android.server.adb.AdbDebuggingManager.AdbDebuggingHandler adbDebuggingHandler = com.android.server.adb.AdbDebuggingManager.this.mHandler;
            if (port > 0) {
                i = 24;
            } else {
                i = 25;
            }
            android.os.Message msg = adbDebuggingHandler.obtainMessage(i);
            msg.obj = java.lang.Integer.valueOf(port);
            com.android.server.adb.AdbDebuggingManager.this.mHandler.sendMessage(msg);
        }
    }

    static class AdbDebuggingThread extends java.lang.Thread {
        private android.os.Handler mHandler;
        private java.io.InputStream mInputStream;
        private java.io.OutputStream mOutputStream;
        private android.net.LocalSocket mSocket;
        private boolean mStopped;

        AdbDebuggingThread() {
            super(com.android.server.adb.AdbDebuggingManager.TAG);
        }

        void setHandler(android.os.Handler handler) {
            this.mHandler = handler;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                synchronized (this) {
                    if (this.mStopped) {
                        return;
                    }
                    try {
                        openSocketLocked();
                    } catch (java.lang.Exception e) {
                        android.os.SystemClock.sleep(1000L);
                    }
                }
                try {
                    listenToSocket();
                } catch (java.lang.Exception e2) {
                    android.os.SystemClock.sleep(1000L);
                }
            }
        }

        private void openSocketLocked() throws java.io.IOException {
            try {
                android.net.LocalSocketAddress address = new android.net.LocalSocketAddress(com.android.server.adb.AdbDebuggingManager.ADBD_SOCKET, android.net.LocalSocketAddress.Namespace.RESERVED);
                this.mInputStream = null;
                this.mSocket = new android.net.LocalSocket(3);
                this.mSocket.connect(address);
                this.mOutputStream = this.mSocket.getOutputStream();
                this.mInputStream = this.mSocket.getInputStream();
                this.mHandler.sendEmptyMessage(26);
            } catch (java.io.IOException ioe) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Caught an exception opening the socket: " + ioe);
                closeSocketLocked();
                throw ioe;
            }
        }

        private void listenToSocket() throws java.io.IOException {
            try {
                byte[] buffer = new byte[65536];
                while (true) {
                    int count = this.mInputStream.read(buffer);
                    if (count < 2) {
                        android.util.Slog.w(com.android.server.adb.AdbDebuggingManager.TAG, "Read failed with count " + count);
                        break;
                    }
                    if (buffer[0] == 80 && buffer[1] == 75) {
                        java.lang.String key = new java.lang.String(java.util.Arrays.copyOfRange(buffer, 2, count));
                        android.util.Slog.d(com.android.server.adb.AdbDebuggingManager.TAG, "Received public key: " + key);
                        android.os.Message msg = this.mHandler.obtainMessage(5);
                        msg.obj = key;
                        this.mHandler.sendMessage(msg);
                    } else if (buffer[0] == 68 && buffer[1] == 67) {
                        java.lang.String key2 = new java.lang.String(java.util.Arrays.copyOfRange(buffer, 2, count));
                        android.util.Slog.d(com.android.server.adb.AdbDebuggingManager.TAG, "Received disconnected message: " + key2);
                        android.os.Message msg2 = this.mHandler.obtainMessage(7);
                        msg2.obj = key2;
                        this.mHandler.sendMessage(msg2);
                    } else if (buffer[0] == 67 && buffer[1] == 75) {
                        java.lang.String key3 = new java.lang.String(java.util.Arrays.copyOfRange(buffer, 2, count));
                        android.util.Slog.d(com.android.server.adb.AdbDebuggingManager.TAG, "Received connected key message: " + key3);
                        android.os.Message msg3 = this.mHandler.obtainMessage(10);
                        msg3.obj = key3;
                        this.mHandler.sendMessage(msg3);
                    } else if (buffer[0] == 87 && buffer[1] == 69) {
                        byte transportType = buffer[2];
                        java.lang.String key4 = new java.lang.String(java.util.Arrays.copyOfRange(buffer, 3, count));
                        if (transportType == 0) {
                            android.util.Slog.d(com.android.server.adb.AdbDebuggingManager.TAG, "Received USB TLS connected key message: " + key4);
                            android.os.Message msg4 = this.mHandler.obtainMessage(10);
                            msg4.obj = key4;
                            this.mHandler.sendMessage(msg4);
                        } else if (transportType == 1) {
                            android.util.Slog.d(com.android.server.adb.AdbDebuggingManager.TAG, "Received WIFI TLS connected key message: " + key4);
                            android.os.Message msg5 = this.mHandler.obtainMessage(22);
                            msg5.obj = key4;
                            this.mHandler.sendMessage(msg5);
                        } else {
                            android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Got unknown transport type from adbd (" + ((int) transportType) + ")");
                        }
                    } else {
                        if (buffer[0] != 87 || buffer[1] != 70) {
                            break;
                        }
                        byte transportType2 = buffer[2];
                        java.lang.String key5 = new java.lang.String(java.util.Arrays.copyOfRange(buffer, 3, count));
                        if (transportType2 == 0) {
                            android.util.Slog.d(com.android.server.adb.AdbDebuggingManager.TAG, "Received USB TLS disconnect message: " + key5);
                            android.os.Message msg6 = this.mHandler.obtainMessage(7);
                            msg6.obj = key5;
                            this.mHandler.sendMessage(msg6);
                        } else if (transportType2 == 1) {
                            android.util.Slog.d(com.android.server.adb.AdbDebuggingManager.TAG, "Received WIFI TLS disconnect key message: " + key5);
                            android.os.Message msg7 = this.mHandler.obtainMessage(23);
                            msg7.obj = key5;
                            this.mHandler.sendMessage(msg7);
                        } else {
                            android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Got unknown transport type from adbd (" + ((int) transportType2) + ")");
                        }
                    }
                }
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Wrong message: " + new java.lang.String(java.util.Arrays.copyOfRange(buffer, 0, 2)));
                synchronized (this) {
                    closeSocketLocked();
                }
            } catch (java.lang.Throwable th) {
                synchronized (this) {
                    closeSocketLocked();
                    throw th;
                }
            }
        }

        private void closeSocketLocked() {
            try {
                if (this.mOutputStream != null) {
                    this.mOutputStream.close();
                    this.mOutputStream = null;
                }
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Failed closing output stream: " + e);
            }
            try {
                if (this.mSocket != null) {
                    this.mSocket.close();
                    this.mSocket = null;
                }
            } catch (java.io.IOException ex) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Failed closing socket: " + ex);
            }
            this.mHandler.sendEmptyMessage(27);
        }

        void stopListening() {
            synchronized (this) {
                this.mStopped = true;
                closeSocketLocked();
            }
        }

        void sendResponse(java.lang.String msg) {
            synchronized (this) {
                if (!this.mStopped && this.mOutputStream != null) {
                    try {
                        this.mOutputStream.write(msg.getBytes());
                    } catch (java.io.IOException ex) {
                        android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Failed to write response:", ex);
                    }
                }
            }
        }
    }

    private static class AdbConnectionInfo {
        private java.lang.String mBssid;
        private int mPort;
        private java.lang.String mSsid;

        AdbConnectionInfo() {
            this.mBssid = "";
            this.mSsid = "";
            this.mPort = -1;
        }

        AdbConnectionInfo(java.lang.String bssid, java.lang.String ssid) {
            this.mBssid = bssid;
            this.mSsid = ssid;
        }

        AdbConnectionInfo(com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo other) {
            this.mBssid = other.mBssid;
            this.mSsid = other.mSsid;
            this.mPort = other.mPort;
        }

        public java.lang.String getBSSID() {
            return this.mBssid;
        }

        public java.lang.String getSSID() {
            return this.mSsid;
        }

        public int getPort() {
            return this.mPort;
        }

        public void setPort(int port) {
            this.mPort = port;
        }

        public void clear() {
            this.mBssid = "";
            this.mSsid = "";
            this.mPort = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAdbConnectionInfo(com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo info) {
        synchronized (this.mAdbConnectionInfo) {
            if (info == null) {
                this.mAdbConnectionInfo.clear();
            } else {
                this.mAdbConnectionInfo = info;
            }
        }
    }

    private com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo getAdbConnectionInfo() {
        com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo adbConnectionInfo;
        synchronized (this.mAdbConnectionInfo) {
            adbConnectionInfo = new com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo(this.mAdbConnectionInfo);
        }
        return adbConnectionInfo;
    }

    class AdbDebuggingHandler extends android.os.Handler {
        private static final java.lang.String ADB_NOTIFICATION_CHANNEL_ID_TV = "usbdevicemanager.adb.tv";
        static final int MESSAGE_ADB_ALLOW = 3;
        static final int MESSAGE_ADB_CLEAR = 6;
        static final int MESSAGE_ADB_CONFIRM = 5;
        static final int MESSAGE_ADB_CONNECTED_KEY = 10;
        static final int MESSAGE_ADB_DENY = 4;
        static final int MESSAGE_ADB_DISABLED = 2;
        static final int MESSAGE_ADB_DISCONNECT = 7;
        static final int MESSAGE_ADB_ENABLED = 1;
        static final int MESSAGE_ADB_PERSIST_KEYSTORE = 8;
        static final int MESSAGE_ADB_UPDATE_KEYSTORE = 9;
        private static final int MESSAGE_KEY_FILES_UPDATED = 28;
        static final int MSG_ADBDWIFI_DISABLE = 12;
        static final int MSG_ADBDWIFI_ENABLE = 11;
        static final int MSG_ADBD_SOCKET_CONNECTED = 26;
        static final int MSG_ADBD_SOCKET_DISCONNECTED = 27;
        static final int MSG_ADBWIFI_ALLOW = 18;
        static final int MSG_ADBWIFI_DENY = 19;
        static final java.lang.String MSG_DISABLE_ADBDWIFI = "DA";
        static final java.lang.String MSG_DISCONNECT_DEVICE = "DD";
        static final int MSG_PAIRING_CANCEL = 14;
        static final int MSG_PAIR_PAIRING_CODE = 15;
        static final int MSG_PAIR_QR_CODE = 16;
        static final int MSG_REQ_UNPAIR = 17;
        static final int MSG_RESPONSE_PAIRING_PORT = 21;
        static final int MSG_RESPONSE_PAIRING_RESULT = 20;
        static final int MSG_SERVER_CONNECTED = 24;
        static final int MSG_SERVER_DISCONNECTED = 25;
        static final int MSG_WIFI_DEVICE_CONNECTED = 22;
        static final int MSG_WIFI_DEVICE_DISCONNECTED = 23;
        static final long UPDATE_KEYSTORE_JOB_INTERVAL = 86400000;
        static final long UPDATE_KEYSTORE_MIN_JOB_INTERVAL = 60000;
        private int mAdbEnabledRefCount;
        com.android.server.adb.AdbDebuggingManager.AdbKeyStore mAdbKeyStore;
        private boolean mAdbNotificationShown;
        private android.database.ContentObserver mAuthTimeObserver;
        private final android.content.BroadcastReceiver mBroadcastReceiver;
        private android.app.NotificationManager mNotificationManager;

        private boolean isTv() {
            return com.android.server.adb.AdbDebuggingManager.this.mContext.getPackageManager().hasSystemFeature("android.software.leanback");
        }

        private void setupNotifications() {
            if (this.mNotificationManager != null) {
                return;
            }
            this.mNotificationManager = (android.app.NotificationManager) com.android.server.adb.AdbDebuggingManager.this.mContext.getSystemService("notification");
            if (this.mNotificationManager == null) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Unable to setup notifications for wireless debugging");
            } else if (isTv()) {
                this.mNotificationManager.createNotificationChannel(new android.app.NotificationChannel(ADB_NOTIFICATION_CHANNEL_ID_TV, com.android.server.adb.AdbDebuggingManager.this.mContext.getString(android.R.string.accessibility_uncheck_legacy_item_warning), 4));
            }
        }

        AdbDebuggingHandler(android.os.Looper looper, com.android.server.adb.AdbDebuggingManager.AdbDebuggingThread thread) {
            super(looper);
            this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.adb.AdbDebuggingManager.AdbDebuggingHandler.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    java.lang.String action = intent.getAction();
                    if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                        int state = intent.getIntExtra("wifi_state", 1);
                        if (state == 1) {
                            android.util.Slog.i(com.android.server.adb.AdbDebuggingManager.TAG, "Wifi disabled. Disabling adbwifi.");
                            android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                            return;
                        }
                        return;
                    }
                    if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                        android.net.NetworkInfo networkInfo = (android.net.NetworkInfo) intent.getParcelableExtra("networkInfo", android.net.NetworkInfo.class);
                        if (networkInfo.getType() == 1) {
                            if (!networkInfo.isConnected()) {
                                android.util.Slog.i(com.android.server.adb.AdbDebuggingManager.TAG, "Network disconnected. Disabling adbwifi.");
                                android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                                return;
                            }
                            android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) com.android.server.adb.AdbDebuggingManager.this.mContext.getSystemService("wifi");
                            android.net.wifi.WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                            if (wifiInfo == null || wifiInfo.getNetworkId() == -1) {
                                android.util.Slog.i(com.android.server.adb.AdbDebuggingManager.TAG, "Not connected to any wireless network. Not enabling adbwifi.");
                                android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                                return;
                            }
                            synchronized (com.android.server.adb.AdbDebuggingManager.this.mAdbConnectionInfo) {
                                java.lang.String bssid = wifiInfo.getBSSID();
                                if (android.text.TextUtils.isEmpty(bssid)) {
                                    android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Unable to get the wifi ap's BSSID. Disabling adbwifi.");
                                    android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                                } else {
                                    if (!android.text.TextUtils.equals(bssid, com.android.server.adb.AdbDebuggingManager.this.mAdbConnectionInfo.getBSSID())) {
                                        android.util.Slog.i(com.android.server.adb.AdbDebuggingManager.TAG, "Detected wifi network change. Disabling adbwifi.");
                                        android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                                    }
                                }
                            }
                        }
                    }
                }
            };
            this.mAdbEnabledRefCount = 0;
            this.mAuthTimeObserver = new android.database.ContentObserver(this) { // from class: com.android.server.adb.AdbDebuggingManager.AdbDebuggingHandler.2
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange, android.net.Uri uri) {
                    android.util.Slog.d(com.android.server.adb.AdbDebuggingManager.TAG, "Received notification that uri " + uri + " was modified; rescheduling keystore job");
                    com.android.server.adb.AdbDebuggingManager.AdbDebuggingHandler.this.scheduleJobToUpdateAdbKeyStore();
                }
            };
            com.android.server.adb.AdbDebuggingManager.this.mThread = thread;
        }

        void initKeyStore() {
            if (this.mAdbKeyStore == null) {
                this.mAdbKeyStore = com.android.server.adb.AdbDebuggingManager.this.new AdbKeyStore();
            }
        }

        public void showAdbConnectedNotification(boolean show) {
            if (show == this.mAdbNotificationShown) {
                return;
            }
            setupNotifications();
            if (!this.mAdbNotificationShown) {
                android.app.Notification notification = android.debug.AdbNotifications.createNotification(com.android.server.adb.AdbDebuggingManager.this.mContext, (byte) 1);
                this.mAdbNotificationShown = true;
                this.mNotificationManager.notifyAsUser(null, 62, notification, android.os.UserHandle.ALL);
            } else {
                this.mAdbNotificationShown = false;
                this.mNotificationManager.cancelAsUser(null, 62, android.os.UserHandle.ALL);
            }
        }

        private void startAdbDebuggingThread() {
            this.mAdbEnabledRefCount++;
            if (this.mAdbEnabledRefCount > 1) {
                return;
            }
            registerForAuthTimeChanges();
            com.android.server.adb.AdbDebuggingManager.this.mThread = new com.android.server.adb.AdbDebuggingManager.AdbDebuggingThread();
            com.android.server.adb.AdbDebuggingManager.this.mThread.setHandler(com.android.server.adb.AdbDebuggingManager.this.mHandler);
            com.android.server.adb.AdbDebuggingManager.this.mThread.start();
            this.mAdbKeyStore.updateKeyStore();
            scheduleJobToUpdateAdbKeyStore();
        }

        private void stopAdbDebuggingThread() {
            this.mAdbEnabledRefCount--;
            if (this.mAdbEnabledRefCount > 0) {
                return;
            }
            if (com.android.server.adb.AdbDebuggingManager.this.mThread != null) {
                com.android.server.adb.AdbDebuggingManager.this.mThread.stopListening();
                com.android.server.adb.AdbDebuggingManager.this.mThread = null;
            }
            if (!com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.isEmpty()) {
                for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.entrySet()) {
                    this.mAdbKeyStore.setLastConnectionTime(entry.getKey(), com.android.server.adb.AdbDebuggingManager.this.mTicker.currentTimeMillis());
                }
                com.android.server.adb.AdbDebuggingManager.this.sendPersistKeyStoreMessage();
                com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.clear();
                com.android.server.adb.AdbDebuggingManager.this.mWifiConnectedKeys.clear();
            }
            scheduleJobToUpdateAdbKeyStore();
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            initKeyStore();
            switch (msg.what) {
                case 1:
                    if (!com.android.server.adb.AdbDebuggingManager.this.mAdbUsbEnabled) {
                        startAdbDebuggingThread();
                        com.android.server.adb.AdbDebuggingManager.this.mAdbUsbEnabled = true;
                        return;
                    }
                    return;
                case 2:
                    if (com.android.server.adb.AdbDebuggingManager.this.mAdbUsbEnabled) {
                        stopAdbDebuggingThread();
                        com.android.server.adb.AdbDebuggingManager.this.mAdbUsbEnabled = false;
                        return;
                    }
                    return;
                case 3:
                    java.lang.String key = (java.lang.String) msg.obj;
                    java.lang.String fingerprints = com.android.server.adb.AdbDebuggingManager.this.getFingerprints(key);
                    if (!fingerprints.equals(com.android.server.adb.AdbDebuggingManager.this.mFingerprints)) {
                        android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Fingerprints do not match. Got " + fingerprints + ", expected " + com.android.server.adb.AdbDebuggingManager.this.mFingerprints);
                        return;
                    }
                    boolean alwaysAllow = msg.arg1 == 1;
                    if (com.android.server.adb.AdbDebuggingManager.this.mThread != null) {
                        com.android.server.adb.AdbDebuggingManager.this.mThread.sendResponse("OK");
                        if (alwaysAllow) {
                            if (!com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.containsKey(key)) {
                                com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.put(key, 1);
                            }
                            this.mAdbKeyStore.setLastConnectionTime(key, com.android.server.adb.AdbDebuggingManager.this.mTicker.currentTimeMillis());
                            com.android.server.adb.AdbDebuggingManager.this.sendPersistKeyStoreMessage();
                            scheduleJobToUpdateAdbKeyStore();
                        }
                        logAdbConnectionChanged(key, 2, alwaysAllow);
                        return;
                    }
                    return;
                case 4:
                    if (com.android.server.adb.AdbDebuggingManager.this.mThread != null) {
                        android.util.Slog.w(com.android.server.adb.AdbDebuggingManager.TAG, "Denying adb confirmation");
                        com.android.server.adb.AdbDebuggingManager.this.mThread.sendResponse("NO");
                        logAdbConnectionChanged(null, 3, false);
                        return;
                    }
                    return;
                case 5:
                    java.lang.String key2 = (java.lang.String) msg.obj;
                    int mode = android.os.SystemProperties.getInt("persist.sys.adb.engineermode", 1);
                    if (mode == 0) {
                        if (com.android.server.adb.AdbDebuggingManager.this.mThread != null) {
                            com.android.server.adb.AdbDebuggingManager.this.mThread.sendResponse("OK");
                            logAdbConnectionChanged(key2, 2, false);
                            return;
                        }
                        return;
                    }
                    java.lang.String fingerprints2 = com.android.server.adb.AdbDebuggingManager.this.getFingerprints(key2);
                    if ("".equals(fingerprints2)) {
                        if (com.android.server.adb.AdbDebuggingManager.this.mThread != null) {
                            com.android.server.adb.AdbDebuggingManager.this.mThread.sendResponse("NO");
                            logAdbConnectionChanged(key2, 5, false);
                            return;
                        }
                        return;
                    }
                    logAdbConnectionChanged(key2, 1, false);
                    com.android.server.adb.AdbDebuggingManager.this.mFingerprints = fingerprints2;
                    com.android.server.adb.AdbDebuggingManager.this.startConfirmationForKey(key2, com.android.server.adb.AdbDebuggingManager.this.mFingerprints);
                    return;
                case 6:
                    android.util.Slog.d(com.android.server.adb.AdbDebuggingManager.TAG, "Received a request to clear the adb authorizations");
                    com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.clear();
                    initKeyStore();
                    com.android.server.adb.AdbDebuggingManager.this.mWifiConnectedKeys.clear();
                    this.mAdbKeyStore.deleteKeyStore();
                    cancelJobToUpdateAdbKeyStore();
                    if (android.provider.Settings.Global.getInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_disconnect_sessions_on_revoke", 1) == 1 && com.android.server.adb.AdbDebuggingManager.this.mAdbUsbEnabled) {
                        try {
                            android.os.SystemService.stop(com.android.server.adb.AdbDebuggingManager.ADBD_SOCKET);
                            android.os.SystemService.waitForState(com.android.server.adb.AdbDebuggingManager.ADBD_SOCKET, android.os.SystemService.State.STOPPED, com.android.server.adb.AdbDebuggingManager.ADBD_STATE_CHANGE_TIMEOUT);
                            android.os.SystemService.start(com.android.server.adb.AdbDebuggingManager.ADBD_SOCKET);
                            android.os.SystemService.waitForState(com.android.server.adb.AdbDebuggingManager.ADBD_SOCKET, android.os.SystemService.State.RUNNING, com.android.server.adb.AdbDebuggingManager.ADBD_STATE_CHANGE_TIMEOUT);
                            return;
                        } catch (java.util.concurrent.TimeoutException e) {
                            android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Timeout occurred waiting for adbd to cycle: ", e);
                            android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_enabled", 0);
                            return;
                        }
                    }
                    return;
                case 7:
                    java.lang.String key3 = (java.lang.String) msg.obj;
                    boolean alwaysAllow2 = false;
                    if (key3 != null && key3.length() > 0) {
                        if (com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.containsKey(key3)) {
                            alwaysAllow2 = true;
                            int refcount = ((java.lang.Integer) com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.get(key3)).intValue() - 1;
                            if (refcount == 0) {
                                this.mAdbKeyStore.setLastConnectionTime(key3, com.android.server.adb.AdbDebuggingManager.this.mTicker.currentTimeMillis());
                                com.android.server.adb.AdbDebuggingManager.this.sendPersistKeyStoreMessage();
                                scheduleJobToUpdateAdbKeyStore();
                                com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.remove(key3);
                            } else {
                                com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.put(key3, java.lang.Integer.valueOf(refcount));
                            }
                        }
                    } else {
                        android.util.Slog.w(com.android.server.adb.AdbDebuggingManager.TAG, "Received a disconnected key message with an empty key");
                    }
                    logAdbConnectionChanged(key3, 7, alwaysAllow2);
                    return;
                case 8:
                    if (this.mAdbKeyStore != null) {
                        this.mAdbKeyStore.persistKeyStore();
                        return;
                    }
                    return;
                case 9:
                    if (!com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.isEmpty()) {
                        for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.entrySet()) {
                            this.mAdbKeyStore.setLastConnectionTime(entry.getKey(), com.android.server.adb.AdbDebuggingManager.this.mTicker.currentTimeMillis());
                        }
                        com.android.server.adb.AdbDebuggingManager.this.sendPersistKeyStoreMessage();
                        scheduleJobToUpdateAdbKeyStore();
                        return;
                    }
                    if (!this.mAdbKeyStore.isEmpty()) {
                        this.mAdbKeyStore.updateKeyStore();
                        scheduleJobToUpdateAdbKeyStore();
                        return;
                    }
                    return;
                case 10:
                    java.lang.String key4 = (java.lang.String) msg.obj;
                    if (key4 == null || key4.length() == 0) {
                        android.util.Slog.w(com.android.server.adb.AdbDebuggingManager.TAG, "Received a connected key message with an empty key");
                        return;
                    }
                    if (!com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.containsKey(key4)) {
                        com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.put(key4, 1);
                    } else {
                        com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.put(key4, java.lang.Integer.valueOf(((java.lang.Integer) com.android.server.adb.AdbDebuggingManager.this.mConnectedKeys.get(key4)).intValue() + 1));
                    }
                    this.mAdbKeyStore.setLastConnectionTime(key4, com.android.server.adb.AdbDebuggingManager.this.mTicker.currentTimeMillis());
                    com.android.server.adb.AdbDebuggingManager.this.sendPersistKeyStoreMessage();
                    scheduleJobToUpdateAdbKeyStore();
                    logAdbConnectionChanged(key4, 4, true);
                    return;
                case 11:
                    if (!com.android.server.adb.AdbDebuggingManager.this.mAdbWifiEnabled) {
                        com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo currentInfo = getCurrentWifiApInfo();
                        if (currentInfo == null) {
                            android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                            return;
                        }
                        if (!verifyWifiNetwork(currentInfo.getBSSID(), currentInfo.getSSID())) {
                            android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                            return;
                        }
                        com.android.server.adb.AdbDebuggingManager.this.setAdbConnectionInfo(currentInfo);
                        android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
                        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
                        com.android.server.adb.AdbDebuggingManager.this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
                        android.os.SystemProperties.set(com.android.server.adb.AdbDebuggingManager.WIFI_PERSISTENT_CONFIG_PROPERTY, "1");
                        com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller = new com.android.server.adb.AdbDebuggingManager.AdbConnectionPortPoller(com.android.server.adb.AdbDebuggingManager.this.mPortListener);
                        com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller.start();
                        startAdbDebuggingThread();
                        com.android.server.adb.AdbDebuggingManager.this.mAdbWifiEnabled = true;
                        return;
                    }
                    return;
                case 12:
                    if (com.android.server.adb.AdbDebuggingManager.this.mAdbWifiEnabled) {
                        com.android.server.adb.AdbDebuggingManager.this.mAdbWifiEnabled = false;
                        com.android.server.adb.AdbDebuggingManager.this.setAdbConnectionInfo(null);
                        com.android.server.adb.AdbDebuggingManager.this.mContext.unregisterReceiver(this.mBroadcastReceiver);
                        if (com.android.server.adb.AdbDebuggingManager.this.mThread != null) {
                            com.android.server.adb.AdbDebuggingManager.this.mThread.sendResponse(MSG_DISABLE_ADBDWIFI);
                        }
                        onAdbdWifiServerDisconnected(-1);
                        stopAdbDebuggingThread();
                        return;
                    }
                    return;
                case 13:
                default:
                    return;
                case 14:
                    if (com.android.server.adb.AdbDebuggingManager.this.mPairingThread != null) {
                        com.android.server.adb.AdbDebuggingManager.this.mPairingThread.cancelPairing();
                        try {
                            com.android.server.adb.AdbDebuggingManager.this.mPairingThread.join();
                            break;
                        } catch (java.lang.InterruptedException e2) {
                            android.util.Slog.w(com.android.server.adb.AdbDebuggingManager.TAG, "Error while waiting for pairing thread to quit.");
                            e2.printStackTrace();
                        }
                        com.android.server.adb.AdbDebuggingManager.this.mPairingThread = null;
                        return;
                    }
                    return;
                case 15:
                    java.lang.String pairingCode = createPairingCode(6);
                    updateUIPairCode(pairingCode);
                    com.android.server.adb.AdbDebuggingManager.this.mPairingThread = com.android.server.adb.AdbDebuggingManager.this.new PairingThread(pairingCode, null);
                    com.android.server.adb.AdbDebuggingManager.this.mPairingThread.start();
                    return;
                case 16:
                    android.os.Bundle bundle = (android.os.Bundle) msg.obj;
                    java.lang.String serviceName = bundle.getString("serviceName");
                    java.lang.String password = bundle.getString(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PASSWORD);
                    com.android.server.adb.AdbDebuggingManager.this.mPairingThread = com.android.server.adb.AdbDebuggingManager.this.new PairingThread(password, serviceName);
                    com.android.server.adb.AdbDebuggingManager.this.mPairingThread.start();
                    return;
                case 17:
                    java.lang.String fingerprint = (java.lang.String) msg.obj;
                    java.lang.String publicKey = this.mAdbKeyStore.findKeyFromFingerprint(fingerprint);
                    if (publicKey == null || publicKey.isEmpty()) {
                        java.lang.String cmdStr = com.android.server.adb.AdbDebuggingManager.TAG;
                        android.util.Slog.e(cmdStr, "Not a known fingerprint [" + fingerprint + "]");
                        return;
                    }
                    java.lang.String cmdStr2 = MSG_DISCONNECT_DEVICE + publicKey;
                    if (com.android.server.adb.AdbDebuggingManager.this.mThread != null) {
                        com.android.server.adb.AdbDebuggingManager.this.mThread.sendResponse(cmdStr2);
                    }
                    this.mAdbKeyStore.removeKey(publicKey);
                    sendPairedDevicesToUI(this.mAdbKeyStore.getPairedDevices());
                    return;
                case 18:
                    if (!com.android.server.adb.AdbDebuggingManager.this.mAdbWifiEnabled) {
                        java.lang.String bssid = (java.lang.String) msg.obj;
                        if (msg.arg1 == 1) {
                            this.mAdbKeyStore.addTrustedNetwork(bssid);
                        }
                        com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo newInfo = getCurrentWifiApInfo();
                        if (newInfo != null && bssid.equals(newInfo.getBSSID())) {
                            com.android.server.adb.AdbDebuggingManager.this.setAdbConnectionInfo(newInfo);
                            android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 1);
                            android.content.IntentFilter intentFilter2 = new android.content.IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
                            intentFilter2.addAction("android.net.wifi.STATE_CHANGE");
                            com.android.server.adb.AdbDebuggingManager.this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter2);
                            android.os.SystemProperties.set(com.android.server.adb.AdbDebuggingManager.WIFI_PERSISTENT_CONFIG_PROPERTY, "1");
                            com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller = new com.android.server.adb.AdbDebuggingManager.AdbConnectionPortPoller(com.android.server.adb.AdbDebuggingManager.this.mPortListener);
                            com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller.start();
                            startAdbDebuggingThread();
                            com.android.server.adb.AdbDebuggingManager.this.mAdbWifiEnabled = true;
                            return;
                        }
                        return;
                    }
                    return;
                case 19:
                    android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                    sendServerConnectionState(false, -1);
                    return;
                case 20:
                    onPairingResult(((android.os.Bundle) msg.obj).getString("publicKey"));
                    sendPairedDevicesToUI(this.mAdbKeyStore.getPairedDevices());
                    return;
                case 21:
                    sendPairingPortToUI(((java.lang.Integer) msg.obj).intValue());
                    return;
                case 22:
                    if (com.android.server.adb.AdbDebuggingManager.this.mWifiConnectedKeys.add((java.lang.String) msg.obj)) {
                        sendPairedDevicesToUI(this.mAdbKeyStore.getPairedDevices());
                        showAdbConnectedNotification(true);
                        return;
                    }
                    return;
                case 23:
                    if (com.android.server.adb.AdbDebuggingManager.this.mWifiConnectedKeys.remove((java.lang.String) msg.obj)) {
                        sendPairedDevicesToUI(this.mAdbKeyStore.getPairedDevices());
                        if (com.android.server.adb.AdbDebuggingManager.this.mWifiConnectedKeys.isEmpty()) {
                            showAdbConnectedNotification(false);
                            return;
                        }
                        return;
                    }
                    return;
                case 24:
                    int port = ((java.lang.Integer) msg.obj).intValue();
                    onAdbdWifiServerConnected(port);
                    synchronized (com.android.server.adb.AdbDebuggingManager.this.mAdbConnectionInfo) {
                        com.android.server.adb.AdbDebuggingManager.this.mAdbConnectionInfo.setPort(port);
                        break;
                    }
                    android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 1);
                    return;
                case 25:
                    if (com.android.server.adb.AdbDebuggingManager.this.mAdbWifiEnabled) {
                        onAdbdWifiServerDisconnected(((java.lang.Integer) msg.obj).intValue());
                        android.provider.Settings.Global.putInt(com.android.server.adb.AdbDebuggingManager.this.mContentResolver, "adb_wifi_enabled", 0);
                        stopAdbDebuggingThread();
                        if (com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller != null) {
                            com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller.cancelAndWait();
                            com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller = null;
                            return;
                        }
                        return;
                    }
                    return;
                case 26:
                    if (com.android.server.adb.AdbDebuggingManager.this.mAdbWifiEnabled) {
                        com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller = new com.android.server.adb.AdbDebuggingManager.AdbConnectionPortPoller(com.android.server.adb.AdbDebuggingManager.this.mPortListener);
                        com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller.start();
                        return;
                    }
                    return;
                case 27:
                    if (com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller != null) {
                        com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller.cancelAndWait();
                        com.android.server.adb.AdbDebuggingManager.this.mConnectionPortPoller = null;
                    }
                    if (com.android.server.adb.AdbDebuggingManager.this.mAdbWifiEnabled) {
                        onAdbdWifiServerDisconnected(-1);
                        return;
                    }
                    return;
                case 28:
                    this.mAdbKeyStore.reloadKeyMap();
                    return;
            }
        }

        void registerForAuthTimeChanges() {
            android.net.Uri uri = android.provider.Settings.Global.getUriFor("adb_allowed_connection_time");
            com.android.server.adb.AdbDebuggingManager.this.mContext.getContentResolver().registerContentObserver(uri, false, this.mAuthTimeObserver);
        }

        private void logAdbConnectionChanged(java.lang.String key, int state, boolean alwaysAllow) {
            long lastConnectionTime = this.mAdbKeyStore.getLastConnectionTime(key);
            long authWindow = this.mAdbKeyStore.getAllowedConnectionTime();
            android.util.Slog.d(com.android.server.adb.AdbDebuggingManager.TAG, "Logging key " + key + ", state = " + state + ", alwaysAllow = " + alwaysAllow + ", lastConnectionTime = " + lastConnectionTime + ", authWindow = " + authWindow);
            com.android.internal.util.FrameworkStatsLog.write(144, lastConnectionTime, authWindow, state, alwaysAllow);
        }

        long scheduleJobToUpdateAdbKeyStore() {
            long delay;
            cancelJobToUpdateAdbKeyStore();
            long keyExpiration = this.mAdbKeyStore.getNextExpirationTime();
            if (keyExpiration == -1) {
                return -1L;
            }
            if (keyExpiration == 0) {
                delay = 0;
            } else {
                delay = java.lang.Math.max(java.lang.Math.min(86400000L, keyExpiration), 60000L);
            }
            android.os.Message message = obtainMessage(9);
            sendMessageDelayed(message, delay);
            return delay;
        }

        private void cancelJobToUpdateAdbKeyStore() {
            removeMessages(9);
        }

        private java.lang.String createPairingCode(int size) {
            java.lang.String res = "";
            java.security.SecureRandom rand = new java.security.SecureRandom();
            for (int i = 0; i < size; i++) {
                res = res + rand.nextInt(10);
            }
            return res;
        }

        private void sendServerConnectionState(boolean connected, int port) {
            int i;
            android.content.Intent intent = new android.content.Intent("com.android.server.adb.WIRELESS_DEBUG_STATUS");
            if (connected) {
                i = 4;
            } else {
                i = 5;
            }
            intent.putExtra("status", i);
            intent.putExtra("adb_port", port);
            com.android.server.adb.AdbDebuggingManager.sendBroadcastWithDebugPermission(com.android.server.adb.AdbDebuggingManager.this.mContext, intent, android.os.UserHandle.ALL);
        }

        private void onAdbdWifiServerConnected(int port) {
            sendPairedDevicesToUI(this.mAdbKeyStore.getPairedDevices());
            sendServerConnectionState(true, port);
        }

        private void onAdbdWifiServerDisconnected(int port) {
            com.android.server.adb.AdbDebuggingManager.this.mWifiConnectedKeys.clear();
            showAdbConnectedNotification(false);
            sendServerConnectionState(false, port);
        }

        private com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo getCurrentWifiApInfo() {
            java.lang.String ssid;
            android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) com.android.server.adb.AdbDebuggingManager.this.mContext.getSystemService("wifi");
            android.net.wifi.WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo == null || wifiInfo.getNetworkId() == -1) {
                java.lang.String ssid2 = com.android.server.adb.AdbDebuggingManager.TAG;
                android.util.Slog.i(ssid2, "Not connected to any wireless network. Not enabling adbwifi.");
                return null;
            }
            if (wifiInfo.isPasspointAp() || wifiInfo.isOsuAp()) {
                ssid = wifiInfo.getPasspointProviderFriendlyName();
            } else {
                ssid = wifiInfo.getSSID();
                if (ssid == null || "<unknown ssid>".equals(ssid)) {
                    java.util.List<android.net.wifi.WifiConfiguration> networks = wifiManager.getConfiguredNetworks();
                    int length = networks.size();
                    for (int i = 0; i < length; i++) {
                        if (networks.get(i).networkId == wifiInfo.getNetworkId()) {
                            ssid = networks.get(i).SSID;
                        }
                    }
                    if (ssid == null) {
                        android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Unable to get ssid of the wifi AP.");
                        return null;
                    }
                }
            }
            java.lang.String bssid = wifiInfo.getBSSID();
            if (android.text.TextUtils.isEmpty(bssid)) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Unable to get the wifi ap's BSSID.");
                return null;
            }
            return new com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo(bssid, ssid);
        }

        private boolean verifyWifiNetwork(java.lang.String bssid, java.lang.String ssid) {
            if (this.mAdbKeyStore.isTrustedNetwork(bssid)) {
                return true;
            }
            com.android.server.adb.AdbDebuggingManager.this.startConfirmationForNetwork(ssid, bssid);
            return false;
        }

        private void onPairingResult(java.lang.String publicKey) {
            if (publicKey == null) {
                android.content.Intent intent = new android.content.Intent("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT");
                intent.putExtra("status", 0);
                com.android.server.adb.AdbDebuggingManager.sendBroadcastWithDebugPermission(com.android.server.adb.AdbDebuggingManager.this.mContext, intent, android.os.UserHandle.ALL);
                return;
            }
            android.content.Intent intent2 = new android.content.Intent("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT");
            intent2.putExtra("status", 1);
            java.lang.String fingerprints = com.android.server.adb.AdbDebuggingManager.this.getFingerprints(publicKey);
            java.lang.String hostname = "nouser@nohostname";
            java.lang.String[] args = publicKey.split("\\s+");
            if (args.length > 1) {
                hostname = args[1];
            }
            android.debug.PairDevice device = new android.debug.PairDevice();
            device.name = fingerprints;
            device.guid = hostname;
            device.connected = false;
            intent2.putExtra("pair_device", (android.os.Parcelable) device);
            com.android.server.adb.AdbDebuggingManager.sendBroadcastWithDebugPermission(com.android.server.adb.AdbDebuggingManager.this.mContext, intent2, android.os.UserHandle.ALL);
            this.mAdbKeyStore.setLastConnectionTime(publicKey, com.android.server.adb.AdbDebuggingManager.this.mTicker.currentTimeMillis());
            com.android.server.adb.AdbDebuggingManager.this.sendPersistKeyStoreMessage();
            scheduleJobToUpdateAdbKeyStore();
        }

        private void sendPairingPortToUI(int port) {
            android.content.Intent intent = new android.content.Intent("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT");
            intent.putExtra("status", 4);
            intent.putExtra("adb_port", port);
            com.android.server.adb.AdbDebuggingManager.sendBroadcastWithDebugPermission(com.android.server.adb.AdbDebuggingManager.this.mContext, intent, android.os.UserHandle.ALL);
        }

        private void sendPairedDevicesToUI(java.util.Map<java.lang.String, android.debug.PairDevice> devices) {
            android.content.Intent intent = new android.content.Intent("com.android.server.adb.WIRELESS_DEBUG_PAIRED_DEVICES");
            intent.putExtra("devices_map", (java.util.HashMap) devices);
            com.android.server.adb.AdbDebuggingManager.sendBroadcastWithDebugPermission(com.android.server.adb.AdbDebuggingManager.this.mContext, intent, android.os.UserHandle.ALL);
        }

        private void updateUIPairCode(java.lang.String code) {
            android.content.Intent intent = new android.content.Intent("com.android.server.adb.WIRELESS_DEBUG_PAIRING_RESULT");
            intent.putExtra("pairing_code", code);
            intent.putExtra("status", 3);
            com.android.server.adb.AdbDebuggingManager.sendBroadcastWithDebugPermission(com.android.server.adb.AdbDebuggingManager.this.mContext, intent, android.os.UserHandle.ALL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getFingerprints(java.lang.String key) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (key == null) {
            return "";
        }
        try {
            java.security.MessageDigest digester = java.security.MessageDigest.getInstance("MD5");
            byte[] base64_data = key.split("\\s+")[0].getBytes();
            try {
                byte[] digest = digester.digest(android.util.Base64.decode(base64_data, 0));
                for (int i = 0; i < digest.length; i++) {
                    sb.append("0123456789ABCDEF".charAt((digest[i] >> 4) & 15));
                    sb.append("0123456789ABCDEF".charAt(digest[i] & 15));
                    if (i < digest.length - 1) {
                        sb.append(":");
                    }
                }
                return sb.toString();
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.e(TAG, "error doing base64 decoding", e);
                return "";
            }
        } catch (java.lang.Exception ex) {
            android.util.Slog.e(TAG, "Error getting digester", ex);
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startConfirmationForNetwork(java.lang.String ssid, java.lang.String bssid) {
        java.util.List<java.util.Map.Entry<java.lang.String, java.lang.String>> extras = new java.util.ArrayList<>();
        extras.add(new java.util.AbstractMap.SimpleEntry<>("ssid", ssid));
        extras.add(new java.util.AbstractMap.SimpleEntry<>("bssid", bssid));
        int currentUserId = android.app.ActivityManager.getCurrentUser();
        java.lang.String componentString = android.content.res.Resources.getSystem().getString(android.R.string.config_defaultAmbientContextConsentComponent);
        android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(componentString);
        android.content.pm.UserInfo userInfo = android.os.UserManager.get(this.mContext).getUserInfo(currentUserId);
        if (startConfirmationActivity(componentName, userInfo.getUserHandle(), extras) || startConfirmationService(componentName, userInfo.getUserHandle(), extras)) {
            return;
        }
        android.util.Slog.e(TAG, "Unable to start customAdbWifiNetworkConfirmation[SecondaryUser]Component " + componentString + " as an Activity or a Service");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startConfirmationForKey(java.lang.String key, java.lang.String fingerprints) {
        java.lang.String componentString;
        java.util.List<java.util.Map.Entry<java.lang.String, java.lang.String>> extras = new java.util.ArrayList<>();
        extras.add(new java.util.AbstractMap.SimpleEntry<>("key", key));
        extras.add(new java.util.AbstractMap.SimpleEntry<>("fingerprints", fingerprints));
        int currentUserId = android.app.ActivityManager.getCurrentUser();
        android.content.pm.UserInfo userInfo = android.os.UserManager.get(this.mContext).getUserInfo(currentUserId);
        if (userInfo.isAdmin() || currentUserId == 888) {
            java.lang.String componentString2 = this.mConfirmComponent;
            componentString = componentString2 != null ? this.mConfirmComponent : android.content.res.Resources.getSystem().getString(android.R.string.config_defaultAccessibilityNotificationSound);
        } else {
            componentString = android.content.res.Resources.getSystem().getString(android.R.string.config_defaultAccessibilityService);
        }
        android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(componentString);
        if (startConfirmationActivity(componentName, userInfo.getUserHandle(), extras) || startConfirmationService(componentName, userInfo.getUserHandle(), extras)) {
            return;
        }
        android.util.Slog.e(TAG, "unable to start customAdbPublicKeyConfirmation[SecondaryUser]Component " + componentString + " as an Activity or a Service");
    }

    private boolean startConfirmationActivity(android.content.ComponentName componentName, android.os.UserHandle userHandle, java.util.List<java.util.Map.Entry<java.lang.String, java.lang.String>> extras) {
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        android.content.Intent intent = createConfirmationIntent(componentName, extras);
        intent.addFlags(268435456);
        if (packageManager.resolveActivity(intent, 65536) != null) {
            try {
                this.mContext.startActivityAsUser(intent, userHandle);
                return true;
            } catch (android.content.ActivityNotFoundException e) {
                android.util.Slog.e(TAG, "unable to start adb whitelist activity: " + componentName, e);
                return false;
            }
        }
        return false;
    }

    private boolean startConfirmationService(android.content.ComponentName componentName, android.os.UserHandle userHandle, java.util.List<java.util.Map.Entry<java.lang.String, java.lang.String>> extras) {
        android.content.Intent intent = createConfirmationIntent(componentName, extras);
        try {
            if (this.mContext.startServiceAsUser(intent, userHandle) != null) {
                return true;
            }
            return false;
        } catch (java.lang.SecurityException e) {
            android.util.Slog.e(TAG, "unable to start adb whitelist service: " + componentName, e);
            return false;
        }
    }

    private android.content.Intent createConfirmationIntent(android.content.ComponentName componentName, java.util.List<java.util.Map.Entry<java.lang.String, java.lang.String>> extras) {
        android.content.Intent intent = new android.content.Intent();
        intent.setClassName(componentName.getPackageName(), componentName.getClassName());
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : extras) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        return intent;
    }

    private static java.io.File getAdbFile(java.lang.String fileName) {
        java.io.File dataDir = android.os.Environment.getDataDirectory();
        java.io.File adbDir = new java.io.File(dataDir, ADB_DIRECTORY);
        if (!adbDir.exists()) {
            android.util.Slog.e(TAG, "ADB data directory does not exist");
            return null;
        }
        return new java.io.File(adbDir, fileName);
    }

    java.io.File getAdbTempKeysFile() {
        return this.mTempKeysFile;
    }

    java.io.File getUserKeyFile() {
        return this.mUserKeyFile;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeKeys(java.lang.Iterable<java.lang.String> keys) {
        if (this.mUserKeyFile == null) {
            return;
        }
        android.util.AtomicFile atomicKeyFile = new android.util.AtomicFile(this.mUserKeyFile);
        java.io.FileOutputStream fo = null;
        try {
            fo = atomicKeyFile.startWrite();
            for (java.lang.String key : keys) {
                fo.write(key.getBytes());
                fo.write(10);
            }
            atomicKeyFile.finishWrite(fo);
            android.os.FileUtils.setPermissions(this.mUserKeyFile.toString(), com.android.internal.util.FrameworkStatsLog.DISPLAY_HBM_STATE_CHANGED, -1, -1);
        } catch (java.io.IOException ex) {
            android.util.Slog.e(TAG, "Error writing keys: " + ex);
            atomicKeyFile.failWrite(fo);
        }
    }

    public void setAdbEnabled(boolean enabled, byte transportType) {
        int i = 1;
        if (transportType == 0) {
            com.android.server.adb.AdbDebuggingManager.AdbDebuggingHandler adbDebuggingHandler = this.mHandler;
            if (!enabled) {
                i = 2;
            }
            adbDebuggingHandler.sendEmptyMessage(i);
            return;
        }
        if (transportType == 1) {
            this.mHandler.sendEmptyMessage(enabled ? 11 : 12);
            return;
        }
        throw new java.lang.IllegalArgumentException("setAdbEnabled called with unimplemented transport type=" + ((int) transportType));
    }

    public void allowDebugging(boolean z, java.lang.String str) {
        android.os.Message messageObtainMessage = this.mHandler.obtainMessage(3);
        messageObtainMessage.arg1 = z ? 1 : 0;
        messageObtainMessage.obj = str;
        this.mHandler.sendMessage(messageObtainMessage);
    }

    public void denyDebugging() {
        this.mHandler.sendEmptyMessage(4);
    }

    public void clearDebuggingKeys() {
        this.mHandler.sendEmptyMessage(6);
    }

    public void allowWirelessDebugging(boolean z, java.lang.String str) {
        android.os.Message messageObtainMessage = this.mHandler.obtainMessage(18);
        messageObtainMessage.arg1 = z ? 1 : 0;
        messageObtainMessage.obj = str;
        this.mHandler.sendMessage(messageObtainMessage);
    }

    public void denyWirelessDebugging() {
        this.mHandler.sendEmptyMessage(19);
    }

    public int getAdbWirelessPort() {
        com.android.server.adb.AdbDebuggingManager.AdbConnectionInfo info = getAdbConnectionInfo();
        if (info == null) {
            return 0;
        }
        return info.getPort();
    }

    public java.util.Map<java.lang.String, android.debug.PairDevice> getPairedDevices() {
        com.android.server.adb.AdbDebuggingManager.AdbKeyStore keystore = new com.android.server.adb.AdbDebuggingManager.AdbKeyStore();
        return keystore.getPairedDevices();
    }

    public void unpairDevice(java.lang.String fingerprint) {
        android.os.Message message = android.os.Message.obtain(this.mHandler, 17, fingerprint);
        this.mHandler.sendMessage(message);
    }

    public void enablePairingByPairingCode() {
        this.mHandler.sendEmptyMessage(15);
    }

    public void enablePairingByQrCode(java.lang.String serviceName, java.lang.String password) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putString("serviceName", serviceName);
        bundle.putString(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PASSWORD, password);
        android.os.Message message = android.os.Message.obtain(this.mHandler, 16, bundle);
        this.mHandler.sendMessage(message);
    }

    public void disablePairing() {
        this.mHandler.sendEmptyMessage(14);
    }

    public boolean isAdbWifiEnabled() {
        return this.mAdbWifiEnabled;
    }

    public void notifyKeyFilesUpdated() {
        this.mHandler.sendEmptyMessage(28);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendPersistKeyStoreMessage() {
        android.os.Message msg = this.mHandler.obtainMessage(8);
        this.mHandler.sendMessage(msg);
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        dump.write("connected_to_adb", 1133871366145L, this.mThread != null);
        com.android.internal.util.dump.DumpUtils.writeStringIfNotNull(dump, "last_key_received", 1138166333442L, this.mFingerprints);
        try {
            java.io.File userKeys = new java.io.File("/data/misc/adb/adb_keys");
            if (userKeys.exists()) {
                dump.write("user_keys", 1138166333443L, android.os.FileUtils.readTextFile(userKeys, 0, null));
            } else {
                android.util.Slog.i(TAG, "No user keys on this device");
            }
        } catch (java.io.IOException e) {
            android.util.Slog.i(TAG, "Cannot read user keys", e);
        }
        try {
            dump.write("system_keys", 1138166333444L, android.os.FileUtils.readTextFile(new java.io.File("/adb_keys"), 0, null));
        } catch (java.io.IOException e2) {
            android.util.Slog.i(TAG, "Cannot read system keys", e2);
        }
        try {
            dump.write("keystore", 1138166333445L, android.os.FileUtils.readTextFile(this.mTempKeysFile, 0, null));
        } catch (java.io.IOException e3) {
            android.util.Slog.i(TAG, "Cannot read keystore: ", e3);
        }
        dump.end(token);
    }

    class AdbKeyStore {
        private static final int KEYSTORE_VERSION = 1;
        private static final int MAX_SUPPORTED_KEYSTORE_VERSION = 1;
        public static final long NO_PREVIOUS_CONNECTION = 0;
        private static final java.lang.String SYSTEM_KEY_FILE = "/adb_keys";
        private static final java.lang.String XML_ATTRIBUTE_KEY = "key";
        private static final java.lang.String XML_ATTRIBUTE_LAST_CONNECTION = "lastConnection";
        private static final java.lang.String XML_ATTRIBUTE_VERSION = "version";
        private static final java.lang.String XML_ATTRIBUTE_WIFI_BSSID = "bssid";
        private static final java.lang.String XML_KEYSTORE_START_TAG = "keyStore";
        private static final java.lang.String XML_TAG_ADB_KEY = "adbKey";
        private static final java.lang.String XML_TAG_WIFI_ACCESS_POINT = "wifiAP";
        private android.util.AtomicFile mAtomicKeyFile;
        private final java.util.Set<java.lang.String> mSystemKeys;
        private final java.util.Map<java.lang.String, java.lang.Long> mKeyMap = new java.util.HashMap();
        private final java.util.List<java.lang.String> mTrustedNetworks = new java.util.ArrayList();

        AdbKeyStore() {
            initKeyFile();
            readTempKeysFile();
            this.mSystemKeys = getSystemKeysFromFile(SYSTEM_KEY_FILE);
            addExistingUserKeysToKeyStore();
        }

        public void reloadKeyMap() {
            readTempKeysFile();
        }

        public void addTrustedNetwork(java.lang.String bssid) {
            this.mTrustedNetworks.add(bssid);
            com.android.server.adb.AdbDebuggingManager.this.sendPersistKeyStoreMessage();
        }

        public java.util.Map<java.lang.String, android.debug.PairDevice> getPairedDevices() {
            java.util.Map<java.lang.String, android.debug.PairDevice> pairedDevices = new java.util.HashMap<>();
            for (java.util.Map.Entry<java.lang.String, java.lang.Long> keyEntry : this.mKeyMap.entrySet()) {
                java.lang.String fingerprints = com.android.server.adb.AdbDebuggingManager.this.getFingerprints(keyEntry.getKey());
                java.lang.String hostname = "nouser@nohostname";
                java.lang.String[] args = keyEntry.getKey().split("\\s+");
                if (args.length > 1) {
                    hostname = args[1];
                }
                android.debug.PairDevice pairDevice = new android.debug.PairDevice();
                pairDevice.name = hostname;
                pairDevice.guid = fingerprints;
                pairDevice.connected = com.android.server.adb.AdbDebuggingManager.this.mWifiConnectedKeys.contains(keyEntry.getKey());
                pairedDevices.put(keyEntry.getKey(), pairDevice);
            }
            return pairedDevices;
        }

        public java.lang.String findKeyFromFingerprint(java.lang.String fingerprint) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Long> entry : this.mKeyMap.entrySet()) {
                java.lang.String f = com.android.server.adb.AdbDebuggingManager.this.getFingerprints(entry.getKey());
                if (fingerprint.equals(f)) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public void removeKey(java.lang.String key) {
            if (this.mKeyMap.containsKey(key)) {
                this.mKeyMap.remove(key);
                com.android.server.adb.AdbDebuggingManager.this.sendPersistKeyStoreMessage();
            }
        }

        private void initKeyFile() {
            if (com.android.server.adb.AdbDebuggingManager.this.mTempKeysFile != null) {
                this.mAtomicKeyFile = new android.util.AtomicFile(com.android.server.adb.AdbDebuggingManager.this.mTempKeysFile);
            }
        }

        private java.util.Set<java.lang.String> getSystemKeysFromFile(java.lang.String fileName) {
            java.util.Set<java.lang.String> systemKeys = new java.util.HashSet<>();
            java.io.File systemKeyFile = new java.io.File(fileName);
            if (systemKeyFile.exists()) {
                try {
                    java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(systemKeyFile));
                    while (true) {
                        try {
                            java.lang.String key = in.readLine();
                            if (key == null) {
                                break;
                            }
                            java.lang.String key2 = key.trim();
                            if (key2.length() > 0) {
                                systemKeys.add(key2);
                            }
                        } finally {
                        }
                    }
                    in.close();
                } catch (java.io.IOException e) {
                    android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Caught an exception reading " + fileName + ": " + e);
                }
            }
            return systemKeys;
        }

        public boolean isEmpty() {
            return this.mKeyMap.isEmpty();
        }

        public void updateKeyStore() {
            if (filterOutOldKeys()) {
                com.android.server.adb.AdbDebuggingManager.this.sendPersistKeyStoreMessage();
            }
        }

        private void readTempKeysFile() {
            com.android.modules.utils.TypedXmlPullParser parser;
            this.mKeyMap.clear();
            this.mTrustedNetworks.clear();
            if (this.mAtomicKeyFile == null) {
                initKeyFile();
                if (this.mAtomicKeyFile == null) {
                    android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Unable to obtain the key file, " + com.android.server.adb.AdbDebuggingManager.this.mTempKeysFile + ", for reading");
                    return;
                }
            }
            if (!this.mAtomicKeyFile.exists()) {
                return;
            }
            try {
                java.io.FileInputStream keyStream = this.mAtomicKeyFile.openRead();
                try {
                    try {
                        parser = android.util.Xml.resolvePullParser(keyStream);
                        com.android.internal.util.XmlUtils.beginDocument(parser, XML_KEYSTORE_START_TAG);
                        int keystoreVersion = parser.getAttributeInt((java.lang.String) null, XML_ATTRIBUTE_VERSION);
                        if (keystoreVersion > 1) {
                            android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Keystore version=" + keystoreVersion + " not supported (max_supported=1)");
                            if (keyStream != null) {
                                keyStream.close();
                                return;
                            }
                            return;
                        }
                    } catch (java.lang.Throwable th) {
                        if (keyStream != null) {
                            try {
                                keyStream.close();
                            } catch (java.lang.Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (org.xmlpull.v1.XmlPullParserException e) {
                    parser = android.util.Xml.resolvePullParser(keyStream);
                }
                readKeyStoreContents(parser);
                if (keyStream != null) {
                    keyStream.close();
                }
            } catch (java.io.IOException e2) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Caught an IOException parsing the XML key file: ", e2);
            } catch (org.xmlpull.v1.XmlPullParserException e3) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Caught XmlPullParserException parsing the XML key file: ", e3);
            }
        }

        private void readKeyStoreContents(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            while (parser.next() != 1) {
                java.lang.String tagName = parser.getName();
                if (XML_TAG_ADB_KEY.equals(tagName)) {
                    addAdbKeyToKeyMap(parser);
                } else if (XML_TAG_WIFI_ACCESS_POINT.equals(tagName)) {
                    addTrustedNetworkToTrustedNetworks(parser);
                } else {
                    android.util.Slog.w(com.android.server.adb.AdbDebuggingManager.TAG, "Ignoring tag '" + tagName + "'. Not recognized.");
                }
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            }
        }

        private void addAdbKeyToKeyMap(com.android.modules.utils.TypedXmlPullParser parser) {
            java.lang.String key = parser.getAttributeValue((java.lang.String) null, XML_ATTRIBUTE_KEY);
            try {
                long connectionTime = parser.getAttributeLong((java.lang.String) null, XML_ATTRIBUTE_LAST_CONNECTION);
                this.mKeyMap.put(key, java.lang.Long.valueOf(connectionTime));
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Error reading adbKey attributes", e);
            }
        }

        private void addTrustedNetworkToTrustedNetworks(com.android.modules.utils.TypedXmlPullParser parser) {
            java.lang.String bssid = parser.getAttributeValue((java.lang.String) null, XML_ATTRIBUTE_WIFI_BSSID);
            this.mTrustedNetworks.add(bssid);
        }

        private void addExistingUserKeysToKeyStore() {
            if (com.android.server.adb.AdbDebuggingManager.this.mUserKeyFile == null || !com.android.server.adb.AdbDebuggingManager.this.mUserKeyFile.exists()) {
                return;
            }
            boolean mapUpdated = false;
            try {
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(com.android.server.adb.AdbDebuggingManager.this.mUserKeyFile));
                while (true) {
                    try {
                        java.lang.String key = in.readLine();
                        if (key == null) {
                            break;
                        } else if (!this.mKeyMap.containsKey(key)) {
                            this.mKeyMap.put(key, java.lang.Long.valueOf(com.android.server.adb.AdbDebuggingManager.this.mTicker.currentTimeMillis()));
                            mapUpdated = true;
                        }
                    } finally {
                    }
                }
                in.close();
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Caught an exception reading " + com.android.server.adb.AdbDebuggingManager.this.mUserKeyFile + ": " + e);
            }
            if (mapUpdated) {
                com.android.server.adb.AdbDebuggingManager.this.sendPersistKeyStoreMessage();
            }
        }

        public void persistKeyStore() {
            filterOutOldKeys();
            if (this.mKeyMap.isEmpty() && this.mTrustedNetworks.isEmpty()) {
                deleteKeyStore();
                return;
            }
            if (this.mAtomicKeyFile == null) {
                initKeyFile();
                if (this.mAtomicKeyFile == null) {
                    android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Unable to obtain the key file, " + com.android.server.adb.AdbDebuggingManager.this.mTempKeysFile + ", for writing");
                    return;
                }
            }
            java.io.FileOutputStream keyStream = null;
            try {
                keyStream = this.mAtomicKeyFile.startWrite();
                com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(keyStream);
                serializer.startDocument((java.lang.String) null, true);
                serializer.startTag((java.lang.String) null, XML_KEYSTORE_START_TAG);
                serializer.attributeInt((java.lang.String) null, XML_ATTRIBUTE_VERSION, 1);
                for (java.util.Map.Entry<java.lang.String, java.lang.Long> keyEntry : this.mKeyMap.entrySet()) {
                    serializer.startTag((java.lang.String) null, XML_TAG_ADB_KEY);
                    serializer.attribute((java.lang.String) null, XML_ATTRIBUTE_KEY, keyEntry.getKey());
                    serializer.attributeLong((java.lang.String) null, XML_ATTRIBUTE_LAST_CONNECTION, keyEntry.getValue().longValue());
                    serializer.endTag((java.lang.String) null, XML_TAG_ADB_KEY);
                }
                for (java.lang.String bssid : this.mTrustedNetworks) {
                    serializer.startTag((java.lang.String) null, XML_TAG_WIFI_ACCESS_POINT);
                    serializer.attribute((java.lang.String) null, XML_ATTRIBUTE_WIFI_BSSID, bssid);
                    serializer.endTag((java.lang.String) null, XML_TAG_WIFI_ACCESS_POINT);
                }
                serializer.endTag((java.lang.String) null, XML_KEYSTORE_START_TAG);
                serializer.endDocument();
                this.mAtomicKeyFile.finishWrite(keyStream);
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.adb.AdbDebuggingManager.TAG, "Caught an exception writing the key map: ", e);
                this.mAtomicKeyFile.failWrite(keyStream);
            }
            com.android.server.adb.AdbDebuggingManager.this.writeKeys(this.mKeyMap.keySet());
        }

        private boolean filterOutOldKeys() {
            long allowedTime = getAllowedConnectionTime();
            if (allowedTime == 0) {
                return false;
            }
            boolean keysDeleted = false;
            long systemTime = com.android.server.adb.AdbDebuggingManager.this.mTicker.currentTimeMillis();
            java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.Long>> keyMapIterator = this.mKeyMap.entrySet().iterator();
            while (keyMapIterator.hasNext()) {
                java.util.Map.Entry<java.lang.String, java.lang.Long> keyEntry = keyMapIterator.next();
                long connectionTime = keyEntry.getValue().longValue();
                if (systemTime > connectionTime + allowedTime) {
                    keyMapIterator.remove();
                    keysDeleted = true;
                }
            }
            if (keysDeleted) {
                com.android.server.adb.AdbDebuggingManager.this.writeKeys(this.mKeyMap.keySet());
            }
            return keysDeleted;
        }

        public long getNextExpirationTime() {
            long minExpiration = -1;
            long allowedTime = getAllowedConnectionTime();
            if (allowedTime == 0) {
                return -1L;
            }
            long systemTime = com.android.server.adb.AdbDebuggingManager.this.mTicker.currentTimeMillis();
            for (java.util.Map.Entry<java.lang.String, java.lang.Long> keyEntry : this.mKeyMap.entrySet()) {
                long connectionTime = keyEntry.getValue().longValue();
                long keyExpiration = java.lang.Math.max(0L, (connectionTime + allowedTime) - systemTime);
                if (minExpiration == -1 || keyExpiration < minExpiration) {
                    minExpiration = keyExpiration;
                }
            }
            return minExpiration;
        }

        public void deleteKeyStore() {
            this.mKeyMap.clear();
            this.mTrustedNetworks.clear();
            if (com.android.server.adb.AdbDebuggingManager.this.mUserKeyFile != null) {
                com.android.server.adb.AdbDebuggingManager.this.mUserKeyFile.delete();
            }
            if (this.mAtomicKeyFile == null) {
                return;
            }
            this.mAtomicKeyFile.delete();
        }

        public long getLastConnectionTime(java.lang.String key) {
            return this.mKeyMap.getOrDefault(key, 0L).longValue();
        }

        public void setLastConnectionTime(java.lang.String key, long connectionTime) {
            setLastConnectionTime(key, connectionTime, false);
        }

        void setLastConnectionTime(java.lang.String key, long connectionTime, boolean force) {
            if ((this.mKeyMap.containsKey(key) && this.mKeyMap.get(key).longValue() >= connectionTime && !force) || this.mSystemKeys.contains(key)) {
                return;
            }
            this.mKeyMap.put(key, java.lang.Long.valueOf(connectionTime));
        }

        public long getAllowedConnectionTime() {
            return android.provider.Settings.Global.getLong(com.android.server.adb.AdbDebuggingManager.this.mContext.getContentResolver(), "adb_allowed_connection_time", com.android.server.usage.UnixCalendar.WEEK_IN_MILLIS);
        }

        public boolean isKeyAuthorized(java.lang.String key) {
            if (this.mSystemKeys.contains(key)) {
                return true;
            }
            long lastConnectionTime = getLastConnectionTime(key);
            if (lastConnectionTime == 0) {
                return false;
            }
            long allowedConnectionTime = getAllowedConnectionTime();
            return allowedConnectionTime == 0 || com.android.server.adb.AdbDebuggingManager.this.mTicker.currentTimeMillis() < lastConnectionTime + allowedConnectionTime;
        }

        public boolean isTrustedNetwork(java.lang.String bssid) {
            return this.mTrustedNetworks.contains(bssid);
        }
    }
}
