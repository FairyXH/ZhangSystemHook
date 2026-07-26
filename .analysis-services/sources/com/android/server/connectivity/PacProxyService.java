package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public class PacProxyService extends android.net.IPacProxyManager.Stub {
    private static final java.lang.String ACTION_PAC_REFRESH = "android.net.proxy.PAC_REFRESH";
    private static final java.lang.String DEFAULT_DELAYS = "8 32 120 14400 43200";
    private static final int DELAY_1 = 0;
    private static final int DELAY_4 = 3;
    private static final int DELAY_LONG = 4;
    private static final long MAX_PAC_SIZE = 20000000;
    private static final java.lang.String PAC_PACKAGE = "com.android.pacprocessor";
    private static final java.lang.String PAC_SERVICE = "com.android.pacprocessor.PacService";
    private static final java.lang.String PAC_SERVICE_NAME = "com.android.net.IProxyService";
    private static final java.lang.String PROXY_PACKAGE = "com.android.proxyhandler";
    private static final java.lang.String PROXY_SERVICE = "com.android.proxyhandler.ProxyService";
    private static final java.lang.String TAG = "PacProxyService";
    private android.app.AlarmManager mAlarmManager;
    private android.content.ServiceConnection mConnection;
    private android.content.Context mContext;
    private int mCurrentDelay;
    private java.lang.String mCurrentPac;
    private volatile boolean mHasDownloaded;
    private volatile boolean mHasSentBroadcast;
    private final android.os.Handler mNetThreadHandler;
    private android.app.PendingIntent mPacRefreshIntent;
    private android.content.ServiceConnection mProxyConnection;
    private com.android.net.IProxyService mProxyService;
    private volatile android.net.Uri mPacUrl = android.net.Uri.EMPTY;
    private final android.os.RemoteCallbackList<android.net.IPacProxyInstalledListener> mCallbacks = new android.os.RemoteCallbackList<>();
    private final java.lang.Object mProxyLock = new java.lang.Object();
    private final java.lang.Object mBroadcastStateLock = new java.lang.Object();
    private java.lang.Runnable mPacDownloader = new java.lang.Runnable() { // from class: com.android.server.connectivity.PacProxyService.1
        @Override // java.lang.Runnable
        public void run() {
            java.lang.String file;
            android.net.Uri pacUrl = com.android.server.connectivity.PacProxyService.this.mPacUrl;
            if (android.net.Uri.EMPTY.equals(pacUrl)) {
                return;
            }
            int oldTag = android.net.TrafficStats.getAndSetThreadStatsTag(-187);
            try {
                try {
                    file = com.android.server.connectivity.PacProxyService.get(pacUrl);
                } catch (java.io.IOException ioe) {
                    android.util.Log.w(com.android.server.connectivity.PacProxyService.TAG, "Failed to load PAC file: " + ioe);
                    android.net.TrafficStats.setThreadStatsTag(oldTag);
                    file = null;
                }
                if (file == null) {
                    com.android.server.connectivity.PacProxyService.this.reschedule();
                    return;
                }
                synchronized (com.android.server.connectivity.PacProxyService.this.mProxyLock) {
                    if (!file.equals(com.android.server.connectivity.PacProxyService.this.mCurrentPac)) {
                        com.android.server.connectivity.PacProxyService.this.setCurrentProxyScript(file);
                    }
                }
                com.android.server.connectivity.PacProxyService.this.mHasDownloaded = true;
                com.android.server.connectivity.PacProxyService.this.sendProxyIfNeeded();
                com.android.server.connectivity.PacProxyService.this.longSchedule();
            } finally {
                android.net.TrafficStats.setThreadStatsTag(oldTag);
            }
        }
    };
    private int mLastPort = -1;

    class PacRefreshIntentReceiver extends android.content.BroadcastReceiver {
        PacRefreshIntentReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.connectivity.PacProxyService.this.mNetThreadHandler.post(com.android.server.connectivity.PacProxyService.this.mPacDownloader);
        }
    }

    public PacProxyService(android.content.Context context) {
        this.mContext = context;
        android.os.HandlerThread netThread = new android.os.HandlerThread("android.pacproxyservice", 0);
        netThread.start();
        this.mNetThreadHandler = new android.os.Handler(netThread.getLooper());
        this.mPacRefreshIntent = android.app.PendingIntent.getBroadcast(context, 0, new android.content.Intent(ACTION_PAC_REFRESH), 67108864);
        context.registerReceiver(new com.android.server.connectivity.PacProxyService.PacRefreshIntentReceiver(), new android.content.IntentFilter(ACTION_PAC_REFRESH));
    }

    private android.app.AlarmManager getAlarmManager() {
        if (this.mAlarmManager == null) {
            this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        }
        return this.mAlarmManager;
    }

    public void addListener(android.net.IPacProxyInstalledListener listener) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermissionOr(this.mContext, new java.lang.String[]{"android.permission.NETWORK_SETTINGS"});
        this.mCallbacks.register(listener);
    }

    public void removeListener(android.net.IPacProxyInstalledListener listener) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermissionOr(this.mContext, new java.lang.String[]{"android.permission.NETWORK_SETTINGS"});
        this.mCallbacks.unregister(listener);
    }

    public void setCurrentProxyScriptUrl(android.net.ProxyInfo proxy) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermissionOr(this.mContext, new java.lang.String[]{"android.permission.NETWORK_SETTINGS"});
        synchronized (this.mBroadcastStateLock) {
            if (proxy != null) {
                if (!android.net.Uri.EMPTY.equals(proxy.getPacFileUrl())) {
                    if (proxy.getPacFileUrl().equals(this.mPacUrl) && proxy.getPort() > 0) {
                        return;
                    }
                    this.mPacUrl = proxy.getPacFileUrl();
                    this.mCurrentDelay = 0;
                    this.mHasSentBroadcast = false;
                    this.mHasDownloaded = false;
                    getAlarmManager().cancel(this.mPacRefreshIntent);
                    bind();
                }
            }
            getAlarmManager().cancel(this.mPacRefreshIntent);
            synchronized (this.mProxyLock) {
                this.mPacUrl = android.net.Uri.EMPTY;
                this.mCurrentPac = null;
                if (this.mProxyService != null) {
                    unbind();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String get(android.net.Uri pacUri) throws java.io.IOException {
        if (!android.webkit.URLUtil.isValidUrl(pacUri.toString())) {
            throw new java.io.IOException("Malformed URL:" + pacUri);
        }
        java.net.URL url = new java.net.URL(pacUri.toString());
        try {
            java.net.URLConnection urlConnection = url.openConnection(java.net.Proxy.NO_PROXY);
            long contentLength = -1;
            try {
                contentLength = java.lang.Long.parseLong(urlConnection.getHeaderField("Content-Length"));
            } catch (java.lang.NumberFormatException e) {
            }
            if (contentLength > MAX_PAC_SIZE) {
                throw new java.io.IOException("PAC too big: " + contentLength + " bytes");
            }
            java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            do {
                int count = urlConnection.getInputStream().read(buffer);
                if (count != -1) {
                    bytes.write(buffer, 0, count);
                } else {
                    return bytes.toString();
                }
            } while (bytes.size() <= MAX_PAC_SIZE);
            throw new java.io.IOException("PAC too big");
        } catch (java.lang.IllegalArgumentException e2) {
            throw new java.io.IOException("Incorrect proxy type for " + pacUri);
        } catch (java.lang.UnsupportedOperationException e3) {
            throw new java.io.IOException("Unsupported URL connection type for " + pacUri);
        }
    }

    private int getNextDelay(int currentDelay) {
        int currentDelay2 = currentDelay + 1;
        if (currentDelay2 > 3) {
            return 3;
        }
        return currentDelay2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void longSchedule() {
        this.mCurrentDelay = 0;
        setDownloadIn(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reschedule() {
        this.mCurrentDelay = getNextDelay(this.mCurrentDelay);
        setDownloadIn(this.mCurrentDelay);
    }

    private java.lang.String getPacChangeDelay() {
        android.content.ContentResolver cr = this.mContext.getContentResolver();
        java.lang.String defaultDelay = android.os.SystemProperties.get("conn.pac_change_delay", DEFAULT_DELAYS);
        java.lang.String val = android.provider.Settings.Global.getString(cr, "pac_change_delay");
        return val == null ? defaultDelay : val;
    }

    private long getDownloadDelay(int delayIndex) {
        java.lang.String[] list = getPacChangeDelay().split(" ");
        if (delayIndex < list.length) {
            return java.lang.Long.parseLong(list[delayIndex]);
        }
        return 0L;
    }

    private void setDownloadIn(int delayIndex) {
        long delay = getDownloadDelay(delayIndex);
        long timeTillTrigger = (1000 * delay) + android.os.SystemClock.elapsedRealtime();
        getAlarmManager().set(3, timeTillTrigger, this.mPacRefreshIntent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentProxyScript(java.lang.String script) {
        if (this.mProxyService == null) {
            android.util.Log.e(TAG, "setCurrentProxyScript: no proxy service");
            return;
        }
        try {
            this.mProxyService.setPacFile(script);
            this.mCurrentPac = script;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Unable to set PAC file", e);
        }
    }

    private void bind() {
        if (this.mContext == null) {
            android.util.Log.e(TAG, "No context for binding");
            return;
        }
        android.content.Intent intent = new android.content.Intent();
        intent.setClassName(PAC_PACKAGE, PAC_SERVICE);
        if (this.mProxyConnection != null && this.mConnection != null) {
            this.mNetThreadHandler.post(this.mPacDownloader);
            return;
        }
        this.mConnection = new android.content.ServiceConnection() { // from class: com.android.server.connectivity.PacProxyService.2
            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(android.content.ComponentName component) {
                synchronized (com.android.server.connectivity.PacProxyService.this.mProxyLock) {
                    com.android.server.connectivity.PacProxyService.this.mProxyService = null;
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(android.content.ComponentName component, android.os.IBinder binder) {
                synchronized (com.android.server.connectivity.PacProxyService.this.mProxyLock) {
                    try {
                        android.util.Log.d(com.android.server.connectivity.PacProxyService.TAG, "Adding service com.android.net.IProxyService " + binder.getInterfaceDescriptor());
                    } catch (android.os.RemoteException e1) {
                        android.util.Log.e(com.android.server.connectivity.PacProxyService.TAG, "Remote Exception", e1);
                    }
                    android.os.ServiceManager.addService(com.android.server.connectivity.PacProxyService.PAC_SERVICE_NAME, binder);
                    com.android.server.connectivity.PacProxyService.this.mProxyService = com.android.net.IProxyService.Stub.asInterface(binder);
                    if (com.android.server.connectivity.PacProxyService.this.mProxyService == null) {
                        android.util.Log.e(com.android.server.connectivity.PacProxyService.TAG, "No proxy service");
                    } else if (com.android.server.connectivity.PacProxyService.this.mCurrentPac != null) {
                        com.android.server.connectivity.PacProxyService.this.setCurrentProxyScript(com.android.server.connectivity.PacProxyService.this.mCurrentPac);
                    } else {
                        com.android.server.connectivity.PacProxyService.this.mNetThreadHandler.post(com.android.server.connectivity.PacProxyService.this.mPacDownloader);
                    }
                }
            }
        };
        this.mContext.bindServiceAsUser(intent, this.mConnection, 1073741829, android.os.UserHandle.SYSTEM);
        android.content.Intent intent2 = new android.content.Intent();
        intent2.setClassName(PROXY_PACKAGE, PROXY_SERVICE);
        this.mProxyConnection = new android.content.ServiceConnection() { // from class: com.android.server.connectivity.PacProxyService.3
            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(android.content.ComponentName component) {
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(android.content.ComponentName component, android.os.IBinder binder) {
                com.android.net.IProxyCallback callbackService = com.android.net.IProxyCallback.Stub.asInterface(binder);
                if (callbackService != null) {
                    try {
                        callbackService.getProxyPort(new com.android.net.IProxyPortListener.Stub() { // from class: com.android.server.connectivity.PacProxyService.3.1
                            public void setProxyPort(int port) {
                                if (com.android.server.connectivity.PacProxyService.this.mLastPort != -1) {
                                    com.android.server.connectivity.PacProxyService.this.mHasSentBroadcast = false;
                                }
                                com.android.server.connectivity.PacProxyService.this.mLastPort = port;
                                if (port != -1) {
                                    android.util.Log.d(com.android.server.connectivity.PacProxyService.TAG, "Local proxy is bound on " + port);
                                    com.android.server.connectivity.PacProxyService.this.sendProxyIfNeeded();
                                } else {
                                    android.util.Log.e(com.android.server.connectivity.PacProxyService.TAG, "Received invalid port from Local Proxy, PAC will not be operational");
                                }
                            }
                        });
                    } catch (android.os.RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.mContext.bindServiceAsUser(intent2, this.mProxyConnection, 1073741829, this.mNetThreadHandler, android.os.UserHandle.SYSTEM);
    }

    private void unbind() {
        if (this.mConnection != null) {
            this.mContext.unbindService(this.mConnection);
            this.mConnection = null;
        }
        if (this.mProxyConnection != null) {
            this.mContext.unbindService(this.mProxyConnection);
            this.mProxyConnection = null;
        }
        this.mProxyService = null;
        this.mLastPort = -1;
    }

    private void sendPacBroadcast(android.net.ProxyInfo proxy) {
        int length = this.mCallbacks.beginBroadcast();
        for (int i = 0; i < length; i++) {
            android.net.IPacProxyInstalledListener listener = this.mCallbacks.getBroadcastItem(i);
            if (listener != null) {
                try {
                    listener.onPacProxyInstalled((android.net.Network) null, proxy);
                } catch (android.os.RemoteException e) {
                }
            }
        }
        this.mCallbacks.finishBroadcast();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendProxyIfNeeded() {
        synchronized (this.mBroadcastStateLock) {
            if (this.mHasDownloaded && this.mLastPort != -1) {
                if (!this.mHasSentBroadcast) {
                    sendPacBroadcast(android.net.ProxyInfo.buildPacProxy(this.mPacUrl, this.mLastPort));
                    this.mHasSentBroadcast = true;
                }
            }
        }
    }
}
