package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
final class WifiDisplayAdapter extends com.android.server.display.DisplayAdapter {
    private static final java.lang.String ACTION_DISCONNECT = "android.server.display.wfd.DISCONNECT";
    private static final java.lang.String DISPLAY_NAME_PREFIX = "wifi:";
    private static final int MSG_SEND_STATUS_CHANGE_BROADCAST = 1;
    private long castTime;
    private android.hardware.display.WifiDisplay mActiveDisplay;
    private int mActiveDisplayState;
    private android.hardware.display.WifiDisplay[] mAvailableDisplays;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private android.hardware.display.WifiDisplayStatus mCurrentStatus;
    private vendor.oplus.hardware.cwb.V1_0.ICwbService mCwbService;
    private com.android.server.display.WifiDisplayController mDisplayController;
    private com.android.server.display.WifiDisplayAdapter.WifiDisplayDevice mDisplayDevice;
    private android.hardware.display.WifiDisplay[] mDisplays;
    private int mFeatureState;
    private final com.android.server.display.WifiDisplayAdapter.WifiDisplayHandler mHandler;
    private boolean mPendingStatusChangeBroadcast;
    private final com.android.server.display.PersistentDataStore mPersistentDataStore;
    private android.hardware.display.WifiDisplay[] mRememberedDisplays;
    private int mScanState;
    private android.hardware.display.WifiDisplaySessionInfo mSessionInfo;
    private final boolean mSupportsProtectedBuffers;
    private com.android.server.display.WifiDisplayAdapter.OplusWifiDisplayAdapterWrapper mWdaWrapper;
    private final com.android.server.display.WifiDisplayController.Listener mWifiDisplayListener;
    private static final java.lang.String TAG = "WifiDisplayAdapter";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);

    public WifiDisplayAdapter(com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.content.Context context, android.os.Handler handler, com.android.server.display.DisplayAdapter.Listener listener, com.android.server.display.PersistentDataStore persistentDataStore, com.android.server.display.feature.DisplayManagerFlags featureFlags) {
        super(syncRoot, context, handler, listener, TAG, featureFlags);
        this.mDisplays = android.hardware.display.WifiDisplay.EMPTY_ARRAY;
        this.mAvailableDisplays = android.hardware.display.WifiDisplay.EMPTY_ARRAY;
        this.mRememberedDisplays = android.hardware.display.WifiDisplay.EMPTY_ARRAY;
        this.mCwbService = null;
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.display.WifiDisplayAdapter.8
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (intent.getAction().equals(com.android.server.display.WifiDisplayAdapter.ACTION_DISCONNECT)) {
                    synchronized (com.android.server.display.WifiDisplayAdapter.this.getSyncRoot()) {
                        com.android.server.display.WifiDisplayAdapter.this.requestDisconnectLocked();
                    }
                }
            }
        };
        this.mWifiDisplayListener = new com.android.server.display.WifiDisplayController.Listener() { // from class: com.android.server.display.WifiDisplayAdapter.9
            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onFeatureStateChanged(int featureState) {
                synchronized (com.android.server.display.WifiDisplayAdapter.this.getSyncRoot()) {
                    if (com.android.server.display.WifiDisplayAdapter.this.mFeatureState != featureState) {
                        com.android.server.display.WifiDisplayAdapter.this.mFeatureState = featureState;
                        com.android.server.display.WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onScanStarted() {
                synchronized (com.android.server.display.WifiDisplayAdapter.this.getSyncRoot()) {
                    if (com.android.server.display.WifiDisplayAdapter.this.mScanState != 1) {
                        com.android.server.display.WifiDisplayAdapter.this.mScanState = 1;
                        com.android.server.display.WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                        com.android.server.display.WifiDisplayAdapter.this.mWdaWrapper.getExtImpl().reportWfdState("MM_FB_EventID#25001#search_count#1");
                        if (com.android.server.display.WifiDisplayAdapter.DEBUG) {
                            android.util.Slog.d(com.android.server.display.WifiDisplayAdapter.TAG, "onScanStarted, reportWfdState searchCount");
                        }
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onScanResults(android.hardware.display.WifiDisplay[] availableDisplays) {
                synchronized (com.android.server.display.WifiDisplayAdapter.this.getSyncRoot()) {
                    android.hardware.display.WifiDisplay[] availableDisplays2 = com.android.server.display.WifiDisplayAdapter.this.mPersistentDataStore.applyWifiDisplayAliases(availableDisplays);
                    boolean changed = !java.util.Arrays.equals(com.android.server.display.WifiDisplayAdapter.this.mAvailableDisplays, availableDisplays2);
                    for (int i = 0; !changed && i < availableDisplays2.length; i++) {
                        changed = availableDisplays2[i].canConnect() != com.android.server.display.WifiDisplayAdapter.this.mAvailableDisplays[i].canConnect();
                    }
                    if (changed) {
                        com.android.server.display.WifiDisplayAdapter.this.mAvailableDisplays = availableDisplays2;
                        com.android.server.display.WifiDisplayAdapter.this.fixRememberedDisplayNamesFromAvailableDisplaysLocked();
                        com.android.server.display.WifiDisplayAdapter.this.updateDisplaysLocked();
                        com.android.server.display.WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onScanFinished() {
                synchronized (com.android.server.display.WifiDisplayAdapter.this.getSyncRoot()) {
                    if (com.android.server.display.WifiDisplayAdapter.this.mScanState != 0) {
                        com.android.server.display.WifiDisplayAdapter.this.mScanState = 0;
                        com.android.server.display.WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplayConnecting(android.hardware.display.WifiDisplay display) {
                synchronized (com.android.server.display.WifiDisplayAdapter.this.getSyncRoot()) {
                    android.hardware.display.WifiDisplay display2 = com.android.server.display.WifiDisplayAdapter.this.mPersistentDataStore.applyWifiDisplayAlias(display);
                    if (com.android.server.display.WifiDisplayAdapter.this.mActiveDisplayState != 1 || com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay == null || !com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay.equals(display2)) {
                        com.android.server.display.WifiDisplayAdapter.this.mActiveDisplayState = 1;
                        com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay = display2;
                        com.android.server.display.WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplayConnectionFailed() {
                synchronized (com.android.server.display.WifiDisplayAdapter.this.getSyncRoot()) {
                    if (com.android.server.display.WifiDisplayAdapter.this.mActiveDisplayState != 0 || com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay != null) {
                        com.android.server.display.WifiDisplayAdapter.this.mActiveDisplayState = 0;
                        com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay = null;
                        com.android.server.display.WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplayConnected(android.hardware.display.WifiDisplay display, android.view.Surface surface, int width, int height, int flags) {
                synchronized (com.android.server.display.WifiDisplayAdapter.this.getSyncRoot()) {
                    com.android.server.display.WifiDisplayAdapter.this.setCwbEnabled(false);
                    android.hardware.display.WifiDisplay display2 = com.android.server.display.WifiDisplayAdapter.this.mPersistentDataStore.applyWifiDisplayAlias(display);
                    com.android.server.display.WifiDisplayAdapter.this.addDisplayDeviceLocked(display2, surface, width, height, flags);
                    if (com.android.server.display.WifiDisplayAdapter.this.mActiveDisplayState != 2 || com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay == null || !com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay.equals(display2)) {
                        com.android.server.display.WifiDisplayAdapter.this.mActiveDisplayState = 2;
                        com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay = display2;
                        com.android.server.display.WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                        com.android.server.display.WifiDisplayAdapter.this.mWdaWrapper.getExtImpl().reportWfdState("MM_FB_EventID#25004#success_count#1");
                        com.android.server.display.WifiDisplayAdapter.this.castTime = java.lang.System.currentTimeMillis();
                        if (com.android.server.display.WifiDisplayAdapter.DEBUG) {
                            android.util.Slog.d(com.android.server.display.WifiDisplayAdapter.TAG, "reportWfdState when display connect, device name: " + display2.getDeviceName() + " connect start Time:" + com.android.server.display.WifiDisplayAdapter.this.castTime + "ms");
                        }
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplaySessionInfo(android.hardware.display.WifiDisplaySessionInfo sessionInfo) {
                synchronized (com.android.server.display.WifiDisplayAdapter.this.getSyncRoot()) {
                    com.android.server.display.WifiDisplayAdapter.this.mSessionInfo = sessionInfo;
                    com.android.server.display.WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplayChanged(android.hardware.display.WifiDisplay display) {
                synchronized (com.android.server.display.WifiDisplayAdapter.this.getSyncRoot()) {
                    android.hardware.display.WifiDisplay display2 = com.android.server.display.WifiDisplayAdapter.this.mPersistentDataStore.applyWifiDisplayAlias(display);
                    if (com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay != null && com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay.hasSameAddress(display2) && !com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay.equals(display2)) {
                        com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay = display2;
                        com.android.server.display.WifiDisplayAdapter.this.renameDisplayDeviceLocked(display2.getFriendlyDisplayName());
                        com.android.server.display.WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplayDisconnected() {
                synchronized (com.android.server.display.WifiDisplayAdapter.this.getSyncRoot()) {
                    com.android.server.display.WifiDisplayAdapter.this.removeDisplayDeviceLocked();
                    com.android.server.display.WifiDisplayAdapter.this.setCwbEnabled(true);
                    if (com.android.server.display.WifiDisplayAdapter.this.mActiveDisplayState != 0 || com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay != null) {
                        com.android.server.display.WifiDisplayAdapter.this.mActiveDisplayState = 0;
                        com.android.server.display.WifiDisplayAdapter.this.mActiveDisplay = null;
                        com.android.server.display.WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                        if (com.android.server.display.WifiDisplayAdapter.this.castTime != 0) {
                            long endTime = java.lang.System.currentTimeMillis();
                            com.android.server.display.WifiDisplayAdapter.this.castTime = (endTime - com.android.server.display.WifiDisplayAdapter.this.castTime) / 1000;
                            java.lang.String reportData = "MM_FB_EventID#25003#disconnect_count#1#cast_time#" + com.android.server.display.WifiDisplayAdapter.this.castTime;
                            com.android.server.display.WifiDisplayAdapter.this.mWdaWrapper.getExtImpl().reportWfdState(reportData);
                            com.android.server.display.WifiDisplayAdapter.this.castTime = 0L;
                            if (com.android.server.display.WifiDisplayAdapter.DEBUG) {
                                android.util.Slog.d(com.android.server.display.WifiDisplayAdapter.TAG, "reportWfdState when display disConnect, end time:" + endTime + "ms, castTime: " + com.android.server.display.WifiDisplayAdapter.this.castTime + "s");
                            }
                        }
                    }
                }
            }
        };
        this.mWdaWrapper = new com.android.server.display.WifiDisplayAdapter.OplusWifiDisplayAdapterWrapper();
        if (!context.getPackageManager().hasSystemFeature("android.hardware.wifi.direct")) {
            throw new java.lang.RuntimeException("WiFi display was requested, but there is no WiFi Direct feature");
        }
        this.mHandler = new com.android.server.display.WifiDisplayAdapter.WifiDisplayHandler(handler.getLooper());
        this.mPersistentDataStore = persistentDataStore;
        this.mSupportsProtectedBuffers = context.getResources().getBoolean(android.R.bool.config_useDefaultFocusHighlight);
    }

    @Override // com.android.server.display.DisplayAdapter
    public void dumpLocked(java.io.PrintWriter pw) {
        super.dumpLocked(pw);
        pw.println("mCurrentStatus=" + getWifiDisplayStatusLocked());
        pw.println("mFeatureState=" + this.mFeatureState);
        pw.println("mScanState=" + this.mScanState);
        pw.println("mActiveDisplayState=" + this.mActiveDisplayState);
        pw.println("mActiveDisplay=" + this.mActiveDisplay);
        pw.println("mDisplays=" + java.util.Arrays.toString(this.mDisplays));
        pw.println("mAvailableDisplays=" + java.util.Arrays.toString(this.mAvailableDisplays));
        pw.println("mRememberedDisplays=" + java.util.Arrays.toString(this.mRememberedDisplays));
        pw.println("mPendingStatusChangeBroadcast=" + this.mPendingStatusChangeBroadcast);
        pw.println("mSupportsProtectedBuffers=" + this.mSupportsProtectedBuffers);
        if (this.mDisplayController == null) {
            pw.println("mDisplayController=null");
            return;
        }
        pw.println("mDisplayController:");
        com.android.internal.util.IndentingPrintWriter ipw = new com.android.internal.util.IndentingPrintWriter(pw, "  ");
        ipw.increaseIndent();
        com.android.internal.util.DumpUtils.dumpAsync(getHandler(), this.mDisplayController, ipw, "", 200L);
    }

    @Override // com.android.server.display.DisplayAdapter
    public void registerLocked() {
        super.registerLocked();
        updateRememberedDisplaysLocked();
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.1
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.display.WifiDisplayAdapter.this.mDisplayController = new com.android.server.display.WifiDisplayController(com.android.server.display.WifiDisplayAdapter.this.getContext(), com.android.server.display.WifiDisplayAdapter.this.getHandler(), com.android.server.display.WifiDisplayAdapter.this.mWifiDisplayListener);
                com.android.server.display.WifiDisplayAdapter.this.getContext().registerReceiverAsUser(com.android.server.display.WifiDisplayAdapter.this.mBroadcastReceiver, android.os.UserHandle.ALL, new android.content.IntentFilter(com.android.server.display.WifiDisplayAdapter.ACTION_DISCONNECT), null, com.android.server.display.WifiDisplayAdapter.this.mHandler, 4);
            }
        });
    }

    public void requestStartScanLocked() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestStartScanLocked");
        }
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.2
            @Override // java.lang.Runnable
            public void run() {
                if (com.android.server.display.WifiDisplayAdapter.this.mDisplayController != null) {
                    com.android.server.display.WifiDisplayAdapter.this.mDisplayController.requestStartScan();
                }
            }
        });
    }

    public void requestStopScanLocked() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestStopScanLocked");
        }
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.3
            @Override // java.lang.Runnable
            public void run() {
                if (com.android.server.display.WifiDisplayAdapter.this.mDisplayController != null) {
                    com.android.server.display.WifiDisplayAdapter.this.mDisplayController.requestStopScan();
                }
            }
        });
    }

    public void requestConnectLocked(final java.lang.String address) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestConnectLocked: address=" + address);
        }
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.4
            @Override // java.lang.Runnable
            public void run() {
                if (com.android.server.display.WifiDisplayAdapter.this.mDisplayController != null) {
                    java.lang.String reportData = "MM_FB_EventID#25002#connect_count#1#device_name#" + address;
                    com.android.server.display.WifiDisplayAdapter.this.mWdaWrapper.getExtImpl().reportWfdState(reportData);
                    if (com.android.server.display.WifiDisplayAdapter.DEBUG) {
                        android.util.Slog.d(com.android.server.display.WifiDisplayAdapter.TAG, "reportWfdState when request display connect, device name: " + address);
                    }
                    com.android.server.display.WifiDisplayAdapter.this.mDisplayController.requestConnect(address);
                }
            }
        });
    }

    public void requestPauseLocked() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestPauseLocked");
        }
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.5
            @Override // java.lang.Runnable
            public void run() {
                if (com.android.server.display.WifiDisplayAdapter.this.mDisplayController != null) {
                    com.android.server.display.WifiDisplayAdapter.this.mDisplayController.requestPause();
                }
            }
        });
    }

    public void requestResumeLocked() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestResumeLocked");
        }
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.6
            @Override // java.lang.Runnable
            public void run() {
                if (com.android.server.display.WifiDisplayAdapter.this.mDisplayController != null) {
                    com.android.server.display.WifiDisplayAdapter.this.mDisplayController.requestResume();
                }
            }
        });
    }

    public void requestDisconnectLocked() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestDisconnectedLocked");
        }
        getHandler().post(new java.lang.Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.7
            @Override // java.lang.Runnable
            public void run() {
                if (com.android.server.display.WifiDisplayAdapter.this.mDisplayController != null) {
                    com.android.server.display.WifiDisplayAdapter.this.mDisplayController.requestDisconnect();
                }
            }
        });
    }

    public void requestRenameLocked(java.lang.String address, java.lang.String alias) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestRenameLocked: address=" + address + ", alias=" + alias);
        }
        if (alias != null) {
            alias = alias.trim();
            if (alias.isEmpty() || alias.equals(address)) {
                alias = null;
            }
        }
        android.hardware.display.WifiDisplay display = this.mPersistentDataStore.getRememberedWifiDisplay(address);
        if (display != null && !java.util.Objects.equals(display.getDeviceAlias(), alias)) {
            if (this.mPersistentDataStore.rememberWifiDisplay(new android.hardware.display.WifiDisplay(address, display.getDeviceName(), alias, false, false, false))) {
                this.mPersistentDataStore.saveIfNeeded();
                updateRememberedDisplaysLocked();
                scheduleStatusChangedBroadcastLocked();
            }
        }
        if (this.mActiveDisplay != null && this.mActiveDisplay.getDeviceAddress().equals(address)) {
            renameDisplayDeviceLocked(this.mActiveDisplay.getFriendlyDisplayName());
        }
    }

    public void requestForgetLocked(java.lang.String address) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestForgetLocked: address=" + address);
        }
        if (this.mPersistentDataStore.forgetWifiDisplay(address)) {
            this.mPersistentDataStore.saveIfNeeded();
            updateRememberedDisplaysLocked();
            scheduleStatusChangedBroadcastLocked();
        }
        if (this.mActiveDisplay != null && this.mActiveDisplay.getDeviceAddress().equals(address)) {
            requestDisconnectLocked();
        }
    }

    public android.hardware.display.WifiDisplayStatus getWifiDisplayStatusLocked() {
        if (this.mCurrentStatus == null) {
            this.mCurrentStatus = new android.hardware.display.WifiDisplayStatus(this.mFeatureState, this.mScanState, this.mActiveDisplayState, this.mActiveDisplay, this.mDisplays, this.mSessionInfo);
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "getWifiDisplayStatusLocked: result=" + this.mCurrentStatus);
        }
        return this.mCurrentStatus;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisplaysLocked() {
        java.util.List<android.hardware.display.WifiDisplay> displays = new java.util.ArrayList<>(this.mAvailableDisplays.length + this.mRememberedDisplays.length);
        boolean[] remembered = new boolean[this.mAvailableDisplays.length];
        for (android.hardware.display.WifiDisplay d : this.mRememberedDisplays) {
            boolean available = false;
            int i = 0;
            while (true) {
                if (i >= this.mAvailableDisplays.length) {
                    break;
                }
                if (!d.equals(this.mAvailableDisplays[i])) {
                    i++;
                } else {
                    available = true;
                    remembered[i] = true;
                    break;
                }
            }
            if (!available) {
                displays.add(new android.hardware.display.WifiDisplay(d.getDeviceAddress(), d.getDeviceName(), d.getDeviceAlias(), false, false, true));
            }
        }
        for (int i2 = 0; i2 < this.mAvailableDisplays.length; i2++) {
            android.hardware.display.WifiDisplay d2 = this.mAvailableDisplays[i2];
            displays.add(new android.hardware.display.WifiDisplay(d2.getDeviceAddress(), d2.getDeviceName(), d2.getDeviceAlias(), true, d2.canConnect(), remembered[i2]));
        }
        this.mDisplays = (android.hardware.display.WifiDisplay[]) displays.toArray(android.hardware.display.WifiDisplay.EMPTY_ARRAY);
    }

    private void updateRememberedDisplaysLocked() {
        this.mRememberedDisplays = this.mPersistentDataStore.getRememberedWifiDisplays();
        this.mActiveDisplay = this.mPersistentDataStore.applyWifiDisplayAlias(this.mActiveDisplay);
        this.mAvailableDisplays = this.mPersistentDataStore.applyWifiDisplayAliases(this.mAvailableDisplays);
        updateDisplaysLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fixRememberedDisplayNamesFromAvailableDisplaysLocked() {
        boolean changed = false;
        for (int i = 0; i < this.mRememberedDisplays.length; i++) {
            android.hardware.display.WifiDisplay rememberedDisplay = this.mRememberedDisplays[i];
            android.hardware.display.WifiDisplay availableDisplay = findAvailableDisplayLocked(rememberedDisplay.getDeviceAddress());
            if (availableDisplay != null && !rememberedDisplay.equals(availableDisplay)) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "fixRememberedDisplayNamesFromAvailableDisplaysLocked: updating remembered display to " + availableDisplay);
                }
                this.mRememberedDisplays[i] = availableDisplay;
                changed |= this.mPersistentDataStore.rememberWifiDisplay(availableDisplay);
            }
        }
        if (changed) {
            this.mPersistentDataStore.saveIfNeeded();
        }
    }

    private android.hardware.display.WifiDisplay findAvailableDisplayLocked(java.lang.String address) {
        for (android.hardware.display.WifiDisplay display : this.mAvailableDisplays) {
            if (display.getDeviceAddress().equals(address)) {
                return display;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addDisplayDeviceLocked(android.hardware.display.WifiDisplay display, android.view.Surface surface, int width, int height, int flags) {
        int deviceFlags;
        android.os.IBinder displayToken;
        removeDisplayDeviceLocked();
        if (this.mPersistentDataStore.rememberWifiDisplay(display)) {
            this.mPersistentDataStore.saveIfNeeded();
            updateRememberedDisplaysLocked();
            scheduleStatusChangedBroadcastLocked();
        }
        boolean secure = (flags & 1) != 0;
        if (!secure) {
            deviceFlags = 64;
        } else {
            int deviceFlags2 = 64 | 4;
            if (!this.mSupportsProtectedBuffers) {
                deviceFlags = deviceFlags2;
            } else {
                deviceFlags = deviceFlags2 | 8;
            }
        }
        java.lang.String name = display.getFriendlyDisplayName();
        java.lang.String address = display.getDeviceAddress();
        if (android.os.Build.isMtkPlatform()) {
            displayToken = com.android.server.display.DisplayControl.createVirtualDisplay(name + "isWifiDpyForHWC", secure);
        } else {
            android.os.IBinder displayToken2 = com.android.server.display.DisplayControl.createVirtualDisplay(name, secure);
            displayToken = displayToken2;
        }
        this.mDisplayDevice = new com.android.server.display.WifiDisplayAdapter.WifiDisplayDevice(displayToken, name, width, height, 60.0f, deviceFlags, address, surface);
        sendDisplayDeviceEventLocked(this.mDisplayDevice, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeDisplayDeviceLocked() {
        if (this.mDisplayDevice != null) {
            this.mDisplayDevice.destroyLocked();
            sendDisplayDeviceEventLocked(this.mDisplayDevice, 3);
            this.mDisplayDevice = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renameDisplayDeviceLocked(java.lang.String name) {
        if (this.mDisplayDevice != null && !this.mDisplayDevice.getNameLocked().equals(name)) {
            this.mDisplayDevice.setNameLocked(name);
            sendDisplayDeviceEventLocked(this.mDisplayDevice, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleStatusChangedBroadcastLocked() {
        this.mCurrentStatus = null;
        if (!this.mPendingStatusChangeBroadcast) {
            this.mPendingStatusChangeBroadcast = true;
            this.mHandler.sendEmptyMessage(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSendStatusChangeBroadcast() {
        synchronized (getSyncRoot()) {
            if (this.mPendingStatusChangeBroadcast) {
                this.mPendingStatusChangeBroadcast = false;
                android.content.Intent intent = new android.content.Intent("android.hardware.display.action.WIFI_DISPLAY_STATUS_CHANGED");
                intent.addFlags(1073741824);
                intent.putExtra("android.hardware.display.extra.WIFI_DISPLAY_STATUS", (android.os.Parcelable) getWifiDisplayStatusLocked());
                android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
                options.setDeliveryGroupPolicy(1);
                getContext().sendBroadcastAsUser(intent, android.os.UserHandle.ALL, null, options.toBundle());
            }
        }
    }

    private final class WifiDisplayDevice extends com.android.server.display.DisplayDevice {
        private final android.view.DisplayAddress mAddress;
        private final int mFlags;
        private final int mHeight;
        private com.android.server.display.DisplayDeviceInfo mInfo;
        private final android.view.Display.Mode mMode;
        private java.lang.String mName;
        private final float mRefreshRate;
        private android.view.Surface mSurface;
        private final int mWidth;

        public WifiDisplayDevice(android.os.IBinder displayToken, java.lang.String name, int width, int height, float refreshRate, int flags, java.lang.String address, android.view.Surface surface) {
            super(com.android.server.display.WifiDisplayAdapter.this, displayToken, com.android.server.display.WifiDisplayAdapter.DISPLAY_NAME_PREFIX + address, com.android.server.display.WifiDisplayAdapter.this.getContext());
            this.mName = name;
            this.mWidth = width;
            this.mHeight = height;
            this.mRefreshRate = refreshRate;
            this.mFlags = flags;
            this.mAddress = android.view.DisplayAddress.fromMacAddress(address);
            this.mSurface = surface;
            this.mMode = com.android.server.display.DisplayAdapter.createMode(width, height, refreshRate);
        }

        @Override // com.android.server.display.DisplayDevice
        public boolean hasStableUniqueId() {
            return true;
        }

        public void destroyLocked() {
            if (this.mSurface != null) {
                this.mSurface.release();
                this.mSurface = null;
            }
            com.android.server.display.DisplayControl.destroyVirtualDisplay(getDisplayTokenLocked());
        }

        public void setNameLocked(java.lang.String name) {
            this.mName = name;
            this.mInfo = null;
        }

        @Override // com.android.server.display.DisplayDevice
        public void performTraversalLocked(android.view.SurfaceControl.Transaction t) {
            if (this.mSurface != null && !this.mDisplayDeviceExt.getMirageSetSurfaceNull()) {
                setSurfaceLocked(t, this.mSurface);
            }
        }

        @Override // com.android.server.display.DisplayDevice
        public com.android.server.display.DisplayDeviceInfo getDisplayDeviceInfoLocked() {
            if (this.mInfo == null) {
                this.mInfo = new com.android.server.display.DisplayDeviceInfo();
                this.mInfo.name = this.mName;
                this.mInfo.uniqueId = getUniqueId();
                this.mInfo.width = this.mWidth;
                this.mInfo.height = this.mHeight;
                this.mInfo.modeId = this.mMode.getModeId();
                this.mInfo.renderFrameRate = this.mMode.getRefreshRate();
                this.mInfo.defaultModeId = this.mMode.getModeId();
                this.mInfo.supportedModes = new android.view.Display.Mode[]{this.mMode};
                this.mInfo.presentationDeadlineNanos = 1000000000 / ((long) ((int) this.mRefreshRate));
                this.mInfo.flags = this.mFlags;
                this.mInfo.type = 3;
                this.mInfo.address = this.mAddress;
                this.mInfo.touch = 2;
                this.mInfo.setAssumedDensityForExternalDisplay(this.mWidth, this.mHeight);
                this.mInfo.flags |= 8192;
                this.mInfo.displayShape = android.view.DisplayShape.createDefaultDisplayShape(this.mInfo.width, this.mInfo.height, false);
            }
            return this.mInfo;
        }
    }

    private final class WifiDisplayHandler extends android.os.Handler {
        public WifiDisplayHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.display.WifiDisplayAdapter.this.handleSendStatusChangeBroadcast();
                    break;
            }
        }
    }

    void setCwbEnabled(boolean enable) {
        try {
            this.mCwbService = vendor.oplus.hardware.cwb.V1_0.ICwbService.getService();
            try {
                if (this.mCwbService != null) {
                    if (enable) {
                        this.mCwbService.enable();
                    } else {
                        this.mCwbService.disable();
                        try {
                            java.lang.Thread.sleep(200L);
                        } catch (java.lang.InterruptedException e) {
                            android.util.Slog.e(TAG, "Failed to wait CWB done" + e.toString());
                        }
                    }
                }
            } catch (java.lang.Exception e2) {
                android.util.Slog.e(TAG, "Failed to enable/disable Cwb function" + e2.toString());
            }
        } catch (java.lang.Exception e3) {
            android.util.Slog.i(TAG, "No Cwb service fetched.");
        }
    }

    public com.android.server.display.IOplusWifiDisplayAdapterWrapper getWrapper() {
        return this.mWdaWrapper;
    }

    private class OplusWifiDisplayAdapterWrapper implements com.android.server.display.IOplusWifiDisplayAdapterWrapper {
        private com.android.server.display.IOplusWifiDisplayAdapterExt mWfdAdapterExt;

        private OplusWifiDisplayAdapterWrapper() {
            this.mWfdAdapterExt = (com.android.server.display.IOplusWifiDisplayAdapterExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IOplusWifiDisplayAdapterExt.class).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.display.IOplusWifiDisplayAdapterExt getExtImpl() {
            return this.mWfdAdapterExt;
        }
    }
}
