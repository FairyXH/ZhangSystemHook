package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
public class ContextHubService extends android.hardware.location.IContextHubService.Stub {
    public static final int CONTEXT_HUB_EVENT_RESTARTED = 1;
    public static final int CONTEXT_HUB_EVENT_UNKNOWN = 0;
    private static final boolean DEBUG_LOG_ENABLED = false;
    public static final int MSG_DISABLE_NANO_APP = 2;
    public static final int MSG_ENABLE_NANO_APP = 1;
    public static final int MSG_HUB_RESET = 7;
    public static final int MSG_LOAD_NANO_APP = 3;
    public static final int MSG_QUERY_MEMORY = 6;
    public static final int MSG_QUERY_NANO_APPS = 5;
    public static final int MSG_UNLOAD_NANO_APP = 4;
    private static final int OS_APP_INSTANCE = -1;
    private static final int PERIOD_METRIC_QUERY_DAYS = 1;
    private static final java.lang.String TAG = "ContextHubService";
    private com.android.server.location.contexthub.ContextHubClientManager mClientManager;
    private final android.content.Context mContext;
    private java.util.Map<java.lang.Integer, android.hardware.location.ContextHubInfo> mContextHubIdToInfoMap;
    private java.util.List<android.hardware.location.ContextHubInfo> mContextHubInfoList;
    private final com.android.server.location.contexthub.IContextHubWrapper mContextHubWrapper;
    private java.util.Map<java.lang.Integer, android.hardware.location.IContextHubClient> mDefaultClientMap;
    private android.hardware.SensorPrivacyManagerInternal mSensorPrivacyManagerInternal;
    private java.util.List<java.lang.String> mSupportedContextHubPerms;
    private com.android.server.location.contexthub.ContextHubTransactionManager mTransactionManager;
    private final android.os.RemoteCallbackList<android.hardware.location.IContextHubCallback> mCallbacksList = new android.os.RemoteCallbackList<>();
    private final com.android.server.location.contexthub.NanoAppStateManager mNanoAppStateManager = new com.android.server.location.contexthub.NanoAppStateManager();
    private final java.util.concurrent.ScheduledThreadPoolExecutor mDailyMetricTimer = new java.util.concurrent.ScheduledThreadPoolExecutor(1);
    private final java.util.PriorityQueue<com.android.server.location.contexthub.ContextHubService.ReliableMessageRecord> mReliableMessageRecordQueue = new java.util.PriorityQueue<>(new java.util.Comparator() { // from class: com.android.server.location.contexthub.ContextHubService$$ExternalSyntheticLambda2
        @Override // java.util.Comparator
        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
            return java.lang.Long.compare(((com.android.server.location.contexthub.ContextHubService.ReliableMessageRecord) obj).getTimestamp(), ((com.android.server.location.contexthub.ContextHubService.ReliableMessageRecord) obj2).getTimestamp());
        }
    });
    private final com.android.server.location.contexthub.ContextHubService.TestModeManager mTestModeManager = new com.android.server.location.contexthub.ContextHubService.TestModeManager();
    private boolean mIsWifiAvailable = false;
    private boolean mIsWifiScanningEnabled = false;
    private boolean mIsWifiMainEnabled = false;
    private boolean mIsBtScanningEnabled = false;
    private boolean mIsBtMainEnabled = false;
    private java.util.concurrent.atomic.AtomicBoolean mIsTestModeEnabled = new java.util.concurrent.atomic.AtomicBoolean(false);
    private java.util.Set<java.lang.Integer> mMetricQueryPendingContextHubIds = java.util.Collections.newSetFromMap(new java.util.concurrent.ConcurrentHashMap());
    private final java.lang.Object mSendWifiSettingUpdateLock = new java.lang.Object();
    private android.os.UserManager mUserManager = null;
    private final java.util.Map<java.lang.Integer, java.util.concurrent.atomic.AtomicLong> mLastRestartTimestampMap = new java.util.HashMap();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private class ContextHubServiceCallback implements com.android.server.location.contexthub.IContextHubWrapper.ICallback {
        private final int mContextHubId;

        ContextHubServiceCallback(int contextHubId) {
            this.mContextHubId = contextHubId;
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper.ICallback
        public void handleTransactionResult(int transactionId, boolean success) {
            com.android.server.location.contexthub.ContextHubService.this.handleTransactionResultCallback(this.mContextHubId, transactionId, success);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper.ICallback
        public void handleContextHubEvent(int eventType) {
            com.android.server.location.contexthub.ContextHubService.this.handleHubEventCallback(this.mContextHubId, eventType);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper.ICallback
        public void handleNanoappAbort(long nanoappId, int abortCode) {
            com.android.server.location.contexthub.ContextHubService.this.handleAppAbortCallback(this.mContextHubId, nanoappId, abortCode);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper.ICallback
        public void handleNanoappInfo(java.util.List<android.hardware.location.NanoAppState> nanoappStateList) {
            com.android.server.location.contexthub.ContextHubService.this.handleQueryAppsCallback(this.mContextHubId, nanoappStateList);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper.ICallback
        public void handleNanoappMessage(short hostEndpointId, android.hardware.location.NanoAppMessage message, java.util.List<java.lang.String> nanoappPermissions, java.util.List<java.lang.String> messagePermissions) throws java.lang.Throwable {
            if (android.chre.flags.Flags.reliableMessageImplementation() && android.chre.flags.Flags.reliableMessageTestModeBehavior() && com.android.server.location.contexthub.ContextHubService.this.mIsTestModeEnabled.get() && com.android.server.location.contexthub.ContextHubService.this.mTestModeManager.handleNanoappMessage(this.mContextHubId, hostEndpointId, message, nanoappPermissions, messagePermissions)) {
                return;
            }
            com.android.server.location.contexthub.ContextHubService.this.handleClientMessageCallback(this.mContextHubId, hostEndpointId, message, nanoappPermissions, messagePermissions);
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper.ICallback
        public void handleServiceRestart() {
            android.util.Log.i(com.android.server.location.contexthub.ContextHubService.TAG, "Recovering from Context Hub HAL restart...");
            com.android.server.location.contexthub.ContextHubService.this.initExistingCallbacks();
            com.android.server.location.contexthub.ContextHubService.this.resetSettings();
            if (android.chre.flags.Flags.reconnectHostEndpointsAfterHalRestart()) {
                com.android.server.location.contexthub.ContextHubService.this.mClientManager.forEachClientOfHub(this.mContextHubId, new java.util.function.Consumer() { // from class: com.android.server.location.contexthub.ContextHubService$ContextHubServiceCallback$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.location.contexthub.ContextHubClientBroker) obj).sendHostEndpointConnectedEvent();
                    }
                });
            }
            android.util.Log.i(com.android.server.location.contexthub.ContextHubService.TAG, "Finished recovering from Context Hub HAL restart");
        }

        @Override // com.android.server.location.contexthub.IContextHubWrapper.ICallback
        public void handleMessageDeliveryStatus(android.hardware.contexthub.MessageDeliveryStatus messageDeliveryStatus) {
            com.android.server.location.contexthub.ContextHubService.this.handleMessageDeliveryStatusCallback(messageDeliveryStatus);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ReliableMessageRecord {
        public static final int TIMEOUT_NS = 1000000000;
        public int mContextHubId;
        byte mErrorCode;
        public int mMessageSequenceNumber;
        public long mTimestamp;

        ReliableMessageRecord(int contextHubId, long timestamp, int messageSequenceNumber, byte errorCode) {
            this.mContextHubId = contextHubId;
            this.mTimestamp = timestamp;
            this.mMessageSequenceNumber = messageSequenceNumber;
            this.mErrorCode = errorCode;
        }

        public int getContextHubId() {
            return this.mContextHubId;
        }

        public long getTimestamp() {
            return this.mTimestamp;
        }

        public int getMessageSequenceNumber() {
            return this.mMessageSequenceNumber;
        }

        public byte getErrorCode() {
            return this.mErrorCode;
        }

        public void setErrorCode(byte errorCode) {
            this.mErrorCode = errorCode;
        }

        public boolean isExpired() {
            return this.mTimestamp + 1000000000 < android.os.SystemClock.elapsedRealtimeNanos();
        }
    }

    private class TestModeManager {
        private static final int MAX_PROBABILITY_PERCENT = 100;
        private static final int MESSAGE_DUPLICATION_PROBABILITY_PERCENT = 50;
        private static final int NUM_MESSAGES_TO_DUPLICATE = 3;
        private java.util.Random mRandom;

        private TestModeManager() {
            this.mRandom = new java.util.Random();
        }

        public boolean handleNanoappMessage(int contextHubId, short hostEndpointId, android.hardware.location.NanoAppMessage message, java.util.List<java.lang.String> nanoappPermissions, java.util.List<java.lang.String> messagePermissions) throws java.lang.Throwable {
            if (!message.isReliable() || !android.chre.flags.Flags.reliableMessageDuplicateDetectionService() || !didEventHappen(50)) {
                return false;
            }
            android.util.Log.i(com.android.server.location.contexthub.ContextHubService.TAG, "[TEST MODE] Duplicating message (3 sends) with message sequence number: " + message.getMessageSequenceNumber());
            for (int i = 0; i < 3; i++) {
                com.android.server.location.contexthub.ContextHubService.this.handleClientMessageCallback(contextHubId, hostEndpointId, message, nanoappPermissions, messagePermissions);
            }
            return true;
        }

        private boolean didEventHappen(int probabilityPercent) {
            return this.mRandom.nextInt(100) < probabilityPercent;
        }
    }

    public ContextHubService(android.content.Context context, com.android.server.location.contexthub.IContextHubWrapper contextHubWrapper) {
        android.util.Log.i(TAG, "Starting Context Hub Service init");
        this.mContext = context;
        long startTimeNs = android.os.SystemClock.elapsedRealtimeNanos();
        this.mContextHubWrapper = contextHubWrapper;
        if (!initContextHubServiceState(startTimeNs)) {
            android.util.Log.e(TAG, "Failed to initialize the Context Hub Service");
            return;
        }
        initDefaultClientMap();
        initLocationSettingNotifications();
        initWifiSettingNotifications();
        initAirplaneModeSettingNotifications();
        initMicrophoneSettingNotifications();
        initBtSettingNotifications();
        scheduleDailyMetricSnapshot();
        android.util.Log.i(TAG, "Finished Context Hub Service init");
    }

    private android.hardware.location.IContextHubClientCallback createDefaultClientCallback(final int contextHubId) {
        return new android.hardware.location.IContextHubClientCallback.Stub() { // from class: com.android.server.location.contexthub.ContextHubService.1
            private void finishCallback() {
                try {
                    android.hardware.location.IContextHubClient client = (android.hardware.location.IContextHubClient) com.android.server.location.contexthub.ContextHubService.this.mDefaultClientMap.get(java.lang.Integer.valueOf(contextHubId));
                    client.callbackFinished();
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.location.contexthub.ContextHubService.TAG, "RemoteException while finishing callback for hub (ID = " + contextHubId + ")", e);
                }
            }

            public void onMessageFromNanoApp(android.hardware.location.NanoAppMessage message) {
                int nanoAppHandle = com.android.server.location.contexthub.ContextHubService.this.mNanoAppStateManager.getNanoAppHandle(contextHubId, message.getNanoAppId());
                com.android.server.location.contexthub.ContextHubService.this.onMessageReceiptOldApi(message.getMessageType(), contextHubId, nanoAppHandle, message.getMessageBody());
                finishCallback();
            }

            public void onHubReset() {
                byte[] data = {0};
                com.android.server.location.contexthub.ContextHubService.this.onMessageReceiptOldApi(7, contextHubId, -1, data);
                finishCallback();
            }

            public void onNanoAppAborted(long nanoAppId, int abortCode) {
                finishCallback();
            }

            public void onNanoAppLoaded(long nanoAppId) {
                finishCallback();
            }

            public void onNanoAppUnloaded(long nanoAppId) {
                finishCallback();
            }

            public void onNanoAppEnabled(long nanoAppId) {
                finishCallback();
            }

            public void onNanoAppDisabled(long nanoAppId) {
                finishCallback();
            }

            public void onClientAuthorizationChanged(long nanoAppId, int authorization) {
                finishCallback();
            }
        };
    }

    private boolean initContextHubServiceState(long startTimeNs) {
        android.util.Pair<java.util.List<android.hardware.location.ContextHubInfo>, java.util.List<java.lang.String>> hubInfo;
        if (this.mContextHubWrapper == null) {
            this.mTransactionManager = null;
            this.mClientManager = null;
            this.mSensorPrivacyManagerInternal = null;
            this.mDefaultClientMap = java.util.Collections.emptyMap();
            this.mContextHubIdToInfoMap = java.util.Collections.emptyMap();
            this.mSupportedContextHubPerms = java.util.Collections.emptyList();
            this.mContextHubInfoList = java.util.Collections.emptyList();
            return false;
        }
        try {
            hubInfo = this.mContextHubWrapper.getHubs();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "RemoteException while getting Context Hub info", e);
            hubInfo = new android.util.Pair<>(java.util.Collections.emptyList(), java.util.Collections.emptyList());
        }
        long bootTimeNs = android.os.SystemClock.elapsedRealtimeNanos() - startTimeNs;
        int numContextHubs = ((java.util.List) hubInfo.first).size();
        com.android.server.location.contexthub.ContextHubStatsLog.write(com.android.server.location.contexthub.ContextHubStatsLog.CONTEXT_HUB_BOOTED, bootTimeNs, numContextHubs);
        this.mContextHubIdToInfoMap = java.util.Collections.unmodifiableMap(com.android.server.location.contexthub.ContextHubServiceUtil.createContextHubInfoMap((java.util.List) hubInfo.first));
        this.mSupportedContextHubPerms = (java.util.List) hubInfo.second;
        this.mContextHubInfoList = new java.util.ArrayList(this.mContextHubIdToInfoMap.values());
        this.mClientManager = new com.android.server.location.contexthub.ContextHubClientManager(this.mContext, this.mContextHubWrapper);
        this.mTransactionManager = new com.android.server.location.contexthub.ContextHubTransactionManager(this.mContextHubWrapper, this.mClientManager, this.mNanoAppStateManager);
        this.mSensorPrivacyManagerInternal = (android.hardware.SensorPrivacyManagerInternal) com.android.server.LocalServices.getService(android.hardware.SensorPrivacyManagerInternal.class);
        return true;
    }

    private void initDefaultClientMap() {
        java.util.HashMap<java.lang.Integer, android.hardware.location.IContextHubClient> defaultClientMap = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.Integer, android.hardware.location.ContextHubInfo> entry : this.mContextHubIdToInfoMap.entrySet()) {
            int contextHubId = entry.getKey().intValue();
            android.hardware.location.ContextHubInfo contextHubInfo = entry.getValue();
            this.mLastRestartTimestampMap.put(java.lang.Integer.valueOf(contextHubId), new java.util.concurrent.atomic.AtomicLong(android.os.SystemClock.elapsedRealtimeNanos()));
            try {
                this.mContextHubWrapper.registerCallback(contextHubId, new com.android.server.location.contexthub.ContextHubService.ContextHubServiceCallback(contextHubId));
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "RemoteException while registering service callback for hub (ID = " + contextHubId + ")", e);
            }
            android.hardware.location.IContextHubClient client = this.mClientManager.registerClient(contextHubInfo, createDefaultClientCallback(contextHubId), (java.lang.String) null, this.mTransactionManager, this.mContext.getPackageName());
            defaultClientMap.put(java.lang.Integer.valueOf(contextHubId), client);
            queryNanoAppsInternal(contextHubId);
        }
        this.mDefaultClientMap = java.util.Collections.unmodifiableMap(defaultClientMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initExistingCallbacks() {
        java.util.Iterator<java.lang.Integer> it = this.mContextHubIdToInfoMap.keySet().iterator();
        while (it.hasNext()) {
            int contextHubId = it.next().intValue();
            try {
                this.mContextHubWrapper.registerExistingCallback(contextHubId);
                android.util.Log.i(TAG, "Re-registered callback to context hub " + contextHubId);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "RemoteException while registering existing service callback for hub (ID = " + contextHubId + ")", e);
            }
        }
    }

    private void initLocationSettingNotifications() {
        if (this.mContextHubWrapper == null || !this.mContextHubWrapper.supportsLocationSettingNotifications()) {
            return;
        }
        sendLocationSettingUpdate();
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("location_mode"), true, new android.database.ContentObserver(null) { // from class: com.android.server.location.contexthub.ContextHubService.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.location.contexthub.ContextHubService.this.sendLocationSettingUpdate();
            }
        }, -1);
    }

    private void initWifiSettingNotifications() {
        if (this.mContextHubWrapper == null || !this.mContextHubWrapper.supportsWifiSettingNotifications()) {
            return;
        }
        sendWifiSettingUpdate(true);
        android.content.BroadcastReceiver wifiReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.location.contexthub.ContextHubService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if ("android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction()) || "android.net.wifi.action.WIFI_SCAN_AVAILABILITY_CHANGED".equals(intent.getAction())) {
                    com.android.server.location.contexthub.ContextHubService.this.sendWifiSettingUpdate(false);
                }
            }
        };
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.action.WIFI_SCAN_AVAILABILITY_CHANGED");
        this.mContext.registerReceiver(wifiReceiver, filter);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("wifi_scan_always_enabled"), true, new android.database.ContentObserver(null) { // from class: com.android.server.location.contexthub.ContextHubService.4
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.location.contexthub.ContextHubService.this.sendWifiSettingUpdate(false);
            }
        }, -1);
    }

    private void initAirplaneModeSettingNotifications() {
        if (this.mContextHubWrapper == null || !this.mContextHubWrapper.supportsAirplaneModeSettingNotifications()) {
            return;
        }
        sendAirplaneModeSettingUpdate();
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("airplane_mode_on"), true, new android.database.ContentObserver(null) { // from class: com.android.server.location.contexthub.ContextHubService.5
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.location.contexthub.ContextHubService.this.sendAirplaneModeSettingUpdate();
            }
        }, -1);
    }

    private void initMicrophoneSettingNotifications() {
        if (this.mContextHubWrapper == null || !this.mContextHubWrapper.supportsMicrophoneSettingNotifications()) {
            return;
        }
        if (this.mUserManager == null) {
            this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
            if (this.mUserManager == null) {
                android.util.Log.e(TAG, "Unable to get the UserManager service");
                return;
            }
        }
        sendMicrophoneDisableSettingUpdateForCurrentUser();
        if (this.mSensorPrivacyManagerInternal == null) {
            android.util.Log.e(TAG, "Unable to add a sensor privacy listener for all users");
        } else {
            this.mSensorPrivacyManagerInternal.addSensorPrivacyListenerForAllUsers(1, new android.hardware.SensorPrivacyManagerInternal.OnUserSensorPrivacyChangedListener() { // from class: com.android.server.location.contexthub.ContextHubService$$ExternalSyntheticLambda6
                public final void onSensorPrivacyChanged(int i, boolean z) {
                    this.f$0.lambda$initMicrophoneSettingNotifications$1(i, z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initMicrophoneSettingNotifications$1(int userId, boolean enabled) {
        if (android.os.UserManager.isHeadlessSystemUserMode() || userId == getCurrentUserId()) {
            android.util.Log.d(TAG, "User: " + userId + " mic privacy: " + enabled);
            sendMicrophoneDisableSettingUpdate(enabled);
        }
    }

    private void initBtSettingNotifications() {
        if (this.mContextHubWrapper == null || !this.mContextHubWrapper.supportsBtSettingNotifications()) {
            return;
        }
        sendBtSettingUpdate(true);
        android.content.BroadcastReceiver btReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.location.contexthub.ContextHubService.6
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
                    com.android.server.location.contexthub.ContextHubService.this.sendBtSettingUpdate(false);
                }
            }
        };
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mContext.registerReceiver(btReceiver, filter);
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("ble_scan_always_enabled"), false, new android.database.ContentObserver(null) { // from class: com.android.server.location.contexthub.ContextHubService.7
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.location.contexthub.ContextHubService.this.sendBtSettingUpdate(false);
            }
        }, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetSettings() {
        this.mIsTestModeEnabled.set(false);
        sendLocationSettingUpdate();
        sendWifiSettingUpdate(true);
        sendAirplaneModeSettingUpdate();
        sendMicrophoneDisableSettingUpdateForCurrentUser();
        sendBtSettingUpdate(true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver result) {
        new com.android.server.location.contexthub.ContextHubShellCommand(this.mContext, this).exec(this, in, out, err, args, callback, result);
    }

    public int registerCallback(android.hardware.location.IContextHubCallback callback) throws android.os.RemoteException {
        super.registerCallback_enforcePermission();
        this.mCallbacksList.register(callback);
        android.util.Log.d(TAG, "Added callback, total callbacks " + this.mCallbacksList.getRegisteredCallbackCount());
        return 0;
    }

    public int[] getContextHubHandles() throws android.os.RemoteException {
        super.getContextHubHandles_enforcePermission();
        return com.android.server.location.contexthub.ContextHubServiceUtil.createPrimitiveIntArray(this.mContextHubIdToInfoMap.keySet());
    }

    public android.hardware.location.ContextHubInfo getContextHubInfo(int contextHubHandle) throws android.os.RemoteException {
        super.getContextHubInfo_enforcePermission();
        if (!this.mContextHubIdToInfoMap.containsKey(java.lang.Integer.valueOf(contextHubHandle))) {
            android.util.Log.e(TAG, "Invalid Context Hub handle " + contextHubHandle + " in getContextHubInfo");
            return null;
        }
        return this.mContextHubIdToInfoMap.get(java.lang.Integer.valueOf(contextHubHandle));
    }

    public java.util.List<android.hardware.location.ContextHubInfo> getContextHubs() throws android.os.RemoteException {
        super.getContextHubs_enforcePermission();
        return this.mContextHubInfoList;
    }

    private android.hardware.location.IContextHubTransactionCallback createLoadTransactionCallback(final int contextHubId, final android.hardware.location.NanoAppBinary nanoAppBinary) {
        return new android.hardware.location.IContextHubTransactionCallback.Stub() { // from class: com.android.server.location.contexthub.ContextHubService.8
            public void onTransactionComplete(int result) {
                com.android.server.location.contexthub.ContextHubService.this.handleLoadResponseOldApi(contextHubId, result, nanoAppBinary);
            }

            public void onQueryResponse(int result, java.util.List<android.hardware.location.NanoAppState> nanoAppStateList) {
            }
        };
    }

    private android.hardware.location.IContextHubTransactionCallback createUnloadTransactionCallback(final int contextHubId) {
        return new android.hardware.location.IContextHubTransactionCallback.Stub() { // from class: com.android.server.location.contexthub.ContextHubService.9
            public void onTransactionComplete(int result) {
                com.android.server.location.contexthub.ContextHubService.this.handleUnloadResponseOldApi(contextHubId, result);
            }

            public void onQueryResponse(int result, java.util.List<android.hardware.location.NanoAppState> nanoAppStateList) {
            }
        };
    }

    private android.hardware.location.IContextHubTransactionCallback createQueryTransactionCallback(final int contextHubId) {
        return new android.hardware.location.IContextHubTransactionCallback.Stub() { // from class: com.android.server.location.contexthub.ContextHubService.10
            public void onTransactionComplete(int result) {
            }

            public void onQueryResponse(int result, java.util.List<android.hardware.location.NanoAppState> nanoAppStateList) {
                byte[] data = {(byte) result};
                com.android.server.location.contexthub.ContextHubService.this.onMessageReceiptOldApi(5, contextHubId, -1, data);
            }
        };
    }

    public int loadNanoApp(int contextHubHandle, android.hardware.location.NanoApp nanoApp) throws android.os.RemoteException {
        super.loadNanoApp_enforcePermission();
        if (this.mContextHubWrapper == null) {
            return -1;
        }
        if (!isValidContextHubId(contextHubHandle)) {
            android.util.Log.e(TAG, "Invalid Context Hub handle " + contextHubHandle + " in loadNanoApp");
            return -1;
        }
        if (nanoApp == null) {
            android.util.Log.e(TAG, "NanoApp cannot be null in loadNanoApp");
            return -1;
        }
        android.hardware.location.NanoAppBinary nanoAppBinary = new android.hardware.location.NanoAppBinary(nanoApp.getAppBinary());
        android.hardware.location.IContextHubTransactionCallback onCompleteCallback = createLoadTransactionCallback(contextHubHandle, nanoAppBinary);
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionManager.createLoadTransaction(contextHubHandle, nanoAppBinary, onCompleteCallback, getCallingPackageName());
        this.mTransactionManager.addTransaction(transaction);
        return 0;
    }

    public int unloadNanoApp(int nanoAppHandle) throws android.os.RemoteException {
        super.unloadNanoApp_enforcePermission();
        if (this.mContextHubWrapper == null) {
            return -1;
        }
        android.hardware.location.NanoAppInstanceInfo info = this.mNanoAppStateManager.getNanoAppInstanceInfo(nanoAppHandle);
        if (info == null) {
            android.util.Log.e(TAG, "Invalid nanoapp handle " + nanoAppHandle + " in unloadNanoApp");
            return -1;
        }
        int contextHubId = info.getContexthubId();
        long nanoAppId = info.getAppId();
        android.hardware.location.IContextHubTransactionCallback onCompleteCallback = createUnloadTransactionCallback(contextHubId);
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionManager.createUnloadTransaction(contextHubId, nanoAppId, onCompleteCallback, getCallingPackageName());
        this.mTransactionManager.addTransaction(transaction);
        return 0;
    }

    public android.hardware.location.NanoAppInstanceInfo getNanoAppInstanceInfo(int nanoAppHandle) throws android.os.RemoteException {
        super.getNanoAppInstanceInfo_enforcePermission();
        return this.mNanoAppStateManager.getNanoAppInstanceInfo(nanoAppHandle);
    }

    public int[] findNanoAppOnHub(int contextHubHandle, final android.hardware.location.NanoAppFilter filter) throws android.os.RemoteException {
        super.findNanoAppOnHub_enforcePermission();
        final java.util.ArrayList<java.lang.Integer> foundInstances = new java.util.ArrayList<>();
        if (filter != null) {
            this.mNanoAppStateManager.foreachNanoAppInstanceInfo(new java.util.function.Consumer() { // from class: com.android.server.location.contexthub.ContextHubService$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.location.contexthub.ContextHubService.lambda$findNanoAppOnHub$2(filter, foundInstances, (android.hardware.location.NanoAppInstanceInfo) obj);
                }
            });
        }
        int[] retArray = new int[foundInstances.size()];
        for (int i = 0; i < foundInstances.size(); i++) {
            retArray[i] = foundInstances.get(i).intValue();
        }
        return retArray;
    }

    static /* synthetic */ void lambda$findNanoAppOnHub$2(android.hardware.location.NanoAppFilter filter, java.util.ArrayList foundInstances, android.hardware.location.NanoAppInstanceInfo info) {
        if (filter.testMatch(info)) {
            foundInstances.add(java.lang.Integer.valueOf(info.getHandle()));
        }
    }

    private boolean queryNanoAppsInternal(int contextHubId) {
        if (this.mContextHubWrapper == null) {
            return false;
        }
        android.hardware.location.IContextHubTransactionCallback onCompleteCallback = createQueryTransactionCallback(contextHubId);
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionManager.createQueryTransaction(contextHubId, onCompleteCallback, getCallingPackageName());
        this.mTransactionManager.addTransaction(transaction);
        return true;
    }

    public int sendMessage(int contextHubHandle, int nanoAppHandle, android.hardware.location.ContextHubMessage msg) throws android.os.RemoteException {
        super.sendMessage_enforcePermission();
        if (this.mContextHubWrapper == null) {
            return -1;
        }
        if (msg == null) {
            android.util.Log.e(TAG, "ContextHubMessage cannot be null in sendMessage");
            return -1;
        }
        if (msg.getData() == null) {
            android.util.Log.e(TAG, "ContextHubMessage message body cannot be null in sendMessage");
            return -1;
        }
        if (!isValidContextHubId(contextHubHandle)) {
            android.util.Log.e(TAG, "Invalid Context Hub handle " + contextHubHandle + " in sendMessage");
            return -1;
        }
        boolean success = false;
        if (nanoAppHandle == -1) {
            if (msg.getMsgType() != 5) {
                android.util.Log.e(TAG, "Invalid OS message params of type " + msg.getMsgType());
            } else {
                success = queryNanoAppsInternal(contextHubHandle);
            }
        } else {
            android.hardware.location.NanoAppInstanceInfo info = getNanoAppInstanceInfo(nanoAppHandle);
            if (info == null) {
                android.util.Log.e(TAG, "Failed to send nanoapp message - nanoapp with handle " + nanoAppHandle + " does not exist.");
            } else {
                android.hardware.location.NanoAppMessage message = android.hardware.location.NanoAppMessage.createMessageToNanoApp(info.getAppId(), msg.getMsgType(), msg.getData());
                android.hardware.location.IContextHubClient client = this.mDefaultClientMap.get(java.lang.Integer.valueOf(contextHubHandle));
                success = client.sendMessageToNanoApp(message) == 0;
            }
        }
        return success ? 0 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleClientMessageCallback(int contextHubId, short hostEndpointId, android.hardware.location.NanoAppMessage message, java.util.List<java.lang.String> nanoappPermissions, java.util.List<java.lang.String> messagePermissions) throws java.lang.Throwable {
        byte errorCode;
        if (!android.chre.flags.Flags.reliableMessageImplementation() || !android.chre.flags.Flags.reliableMessageDuplicateDetectionService()) {
            byte errorCode2 = this.mClientManager.onMessageFromNanoApp(contextHubId, hostEndpointId, message, nanoappPermissions, messagePermissions);
            if (message.isReliable() && errorCode2 != 0) {
                sendMessageDeliveryStatusToContextHub(contextHubId, message.getMessageSequenceNumber(), errorCode2);
                return;
            }
            return;
        }
        if (!message.isReliable()) {
            this.mClientManager.onMessageFromNanoApp(contextHubId, hostEndpointId, message, nanoappPermissions, messagePermissions);
            cleanupReliableMessageRecordQueue();
            return;
        }
        synchronized (this.mReliableMessageRecordQueue) {
            try {
                java.util.Optional<com.android.server.location.contexthub.ContextHubService.ReliableMessageRecord> record = findReliableMessageRecord(contextHubId, message.getMessageSequenceNumber());
                if (record.isPresent()) {
                    errorCode = record.get().getErrorCode();
                    if (errorCode == 1) {
                        android.util.Log.w(TAG, "Found duplicate reliable message with message sequence number: " + record.get().getMessageSequenceNumber() + ": retrying");
                        errorCode = this.mClientManager.onMessageFromNanoApp(contextHubId, hostEndpointId, message, nanoappPermissions, messagePermissions);
                        record.get().setErrorCode(errorCode);
                    } else {
                        android.util.Log.w(TAG, "Found duplicate reliable message with message sequence number: " + record.get().getMessageSequenceNumber());
                    }
                } else {
                    byte errorCode3 = this.mClientManager.onMessageFromNanoApp(contextHubId, hostEndpointId, message, nanoappPermissions, messagePermissions);
                    try {
                        this.mReliableMessageRecordQueue.add(new com.android.server.location.contexthub.ContextHubService.ReliableMessageRecord(contextHubId, android.os.SystemClock.elapsedRealtimeNanos(), message.getMessageSequenceNumber(), errorCode3));
                        errorCode = errorCode3;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                sendMessageDeliveryStatusToContextHub(contextHubId, message.getMessageSequenceNumber(), errorCode);
                cleanupReliableMessageRecordQueue();
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    private java.util.Optional<com.android.server.location.contexthub.ContextHubService.ReliableMessageRecord> findReliableMessageRecord(int contextHubId, int messageSequenceNumber) {
        for (com.android.server.location.contexthub.ContextHubService.ReliableMessageRecord record : this.mReliableMessageRecordQueue) {
            if (record.getContextHubId() == contextHubId && record.getMessageSequenceNumber() == messageSequenceNumber) {
                return java.util.Optional.of(record);
            }
        }
        return java.util.Optional.empty();
    }

    private void cleanupReliableMessageRecordQueue() {
        synchronized (this.mReliableMessageRecordQueue) {
            while (this.mReliableMessageRecordQueue.peek() != null && this.mReliableMessageRecordQueue.peek().isExpired()) {
                this.mReliableMessageRecordQueue.poll();
            }
        }
    }

    private void sendMessageDeliveryStatusToContextHub(int contextHubId, int messageSequenceNumber, byte errorCode) {
        if (!android.chre.flags.Flags.reliableMessageImplementation()) {
            return;
        }
        android.hardware.contexthub.MessageDeliveryStatus status = new android.hardware.contexthub.MessageDeliveryStatus();
        status.messageSequenceNumber = messageSequenceNumber;
        status.errorCode = errorCode;
        if (this.mContextHubWrapper.sendMessageDeliveryStatusToContextHub(contextHubId, status) != 0) {
            android.util.Log.e(TAG, "Failed to send the reliable message status for message sequence number: " + messageSequenceNumber + " with error code: " + ((int) errorCode));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLoadResponseOldApi(int contextHubId, int result, android.hardware.location.NanoAppBinary nanoAppBinary) {
        if (nanoAppBinary == null) {
            android.util.Log.e(TAG, "Nanoapp binary field was null for a load transaction");
            return;
        }
        byte[] data = new byte[5];
        data[0] = (byte) result;
        int nanoAppHandle = this.mNanoAppStateManager.getNanoAppHandle(contextHubId, nanoAppBinary.getNanoAppId());
        java.nio.ByteBuffer.wrap(data, 1, 4).order(java.nio.ByteOrder.nativeOrder()).putInt(nanoAppHandle);
        onMessageReceiptOldApi(3, contextHubId, -1, data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUnloadResponseOldApi(int contextHubId, int result) {
        byte[] data = {(byte) result};
        onMessageReceiptOldApi(4, contextHubId, -1, data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTransactionResultCallback(int contextHubId, int transactionId, boolean success) {
        this.mTransactionManager.onTransactionResponse(transactionId, success);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMessageDeliveryStatusCallback(android.hardware.contexthub.MessageDeliveryStatus messageDeliveryStatus) {
        this.mTransactionManager.onMessageDeliveryResponse(messageDeliveryStatus.messageSequenceNumber, messageDeliveryStatus.errorCode == 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHubEventCallback(int contextHubId, int eventType) {
        if (eventType == 1) {
            long now = android.os.SystemClock.elapsedRealtimeNanos();
            long lastRestartTimeNs = this.mLastRestartTimestampMap.get(java.lang.Integer.valueOf(contextHubId)).getAndSet(now);
            com.android.server.location.contexthub.ContextHubStatsLog.write(com.android.server.location.contexthub.ContextHubStatsLog.CONTEXT_HUB_RESTARTED, java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(now - lastRestartTimeNs), contextHubId);
            com.android.server.location.contexthub.ContextHubEventLogger.getInstance().logContextHubRestart(contextHubId);
            resetSettings();
            this.mTransactionManager.onHubReset();
            queryNanoAppsInternal(contextHubId);
            this.mClientManager.onHubReset(contextHubId);
            return;
        }
        android.util.Log.i(TAG, "Received unknown hub event (hub ID = " + contextHubId + ", type = " + eventType + ")");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAppAbortCallback(int contextHubId, long nanoAppId, int abortCode) {
        this.mClientManager.onNanoAppAborted(contextHubId, nanoAppId, abortCode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleQueryAppsCallback(int contextHubId, java.util.List<android.hardware.location.NanoAppState> nanoappStateList) {
        if (this.mMetricQueryPendingContextHubIds.contains(java.lang.Integer.valueOf(contextHubId))) {
            for (android.hardware.location.NanoAppState nanoappState : nanoappStateList) {
                com.android.server.location.contexthub.ContextHubStatsLog.write(400, contextHubId, nanoappState.getNanoAppId(), (int) nanoappState.getNanoAppVersion());
            }
            this.mMetricQueryPendingContextHubIds.remove(java.lang.Integer.valueOf(contextHubId));
            if (this.mMetricQueryPendingContextHubIds.isEmpty()) {
                scheduleDailyMetricSnapshot();
            }
        }
        this.mNanoAppStateManager.updateCache(contextHubId, nanoappStateList);
        this.mTransactionManager.onQueryResponse(nanoappStateList);
    }

    private boolean isValidContextHubId(int contextHubId) {
        return this.mContextHubIdToInfoMap.containsKey(java.lang.Integer.valueOf(contextHubId));
    }

    public android.hardware.location.IContextHubClient createClient(int contextHubId, android.hardware.location.IContextHubClientCallback clientCallback, java.lang.String attributionTag, java.lang.String packageName) throws android.os.RemoteException {
        super.createClient_enforcePermission();
        if (!isValidContextHubId(contextHubId)) {
            throw new java.lang.IllegalArgumentException("Invalid context hub ID " + contextHubId);
        }
        if (clientCallback == null) {
            throw new java.lang.NullPointerException("Cannot register client with null callback");
        }
        android.hardware.location.ContextHubInfo contextHubInfo = this.mContextHubIdToInfoMap.get(java.lang.Integer.valueOf(contextHubId));
        return this.mClientManager.registerClient(contextHubInfo, clientCallback, attributionTag, this.mTransactionManager, packageName);
    }

    public android.hardware.location.IContextHubClient createPendingIntentClient(int contextHubId, android.app.PendingIntent pendingIntent, long nanoAppId, java.lang.String attributionTag) throws android.os.RemoteException {
        super.createPendingIntentClient_enforcePermission();
        if (!isValidContextHubId(contextHubId)) {
            throw new java.lang.IllegalArgumentException("Invalid context hub ID " + contextHubId);
        }
        android.hardware.location.ContextHubInfo contextHubInfo = this.mContextHubIdToInfoMap.get(java.lang.Integer.valueOf(contextHubId));
        return this.mClientManager.registerClient(contextHubInfo, pendingIntent, nanoAppId, attributionTag, this.mTransactionManager);
    }

    public void loadNanoAppOnHub(int contextHubId, android.hardware.location.IContextHubTransactionCallback transactionCallback, android.hardware.location.NanoAppBinary nanoAppBinary) throws android.os.RemoteException {
        super.loadNanoAppOnHub_enforcePermission();
        if (!checkHalProxyAndContextHubId(contextHubId, transactionCallback, 0)) {
            return;
        }
        if (nanoAppBinary == null) {
            android.util.Log.e(TAG, "NanoAppBinary cannot be null in loadNanoAppOnHub");
            transactionCallback.onTransactionComplete(2);
        } else {
            com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionManager.createLoadTransaction(contextHubId, nanoAppBinary, transactionCallback, getCallingPackageName());
            this.mTransactionManager.addTransaction(transaction);
        }
    }

    public void unloadNanoAppFromHub(int contextHubId, android.hardware.location.IContextHubTransactionCallback transactionCallback, long nanoAppId) throws android.os.RemoteException {
        super.unloadNanoAppFromHub_enforcePermission();
        if (!checkHalProxyAndContextHubId(contextHubId, transactionCallback, 1)) {
            return;
        }
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionManager.createUnloadTransaction(contextHubId, nanoAppId, transactionCallback, getCallingPackageName());
        this.mTransactionManager.addTransaction(transaction);
    }

    public void enableNanoApp(int contextHubId, android.hardware.location.IContextHubTransactionCallback transactionCallback, long nanoAppId) throws android.os.RemoteException {
        super.enableNanoApp_enforcePermission();
        if (!checkHalProxyAndContextHubId(contextHubId, transactionCallback, 2)) {
            return;
        }
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionManager.createEnableTransaction(contextHubId, nanoAppId, transactionCallback, getCallingPackageName());
        this.mTransactionManager.addTransaction(transaction);
    }

    public void disableNanoApp(int contextHubId, android.hardware.location.IContextHubTransactionCallback transactionCallback, long nanoAppId) throws android.os.RemoteException {
        super.disableNanoApp_enforcePermission();
        if (!checkHalProxyAndContextHubId(contextHubId, transactionCallback, 3)) {
            return;
        }
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionManager.createDisableTransaction(contextHubId, nanoAppId, transactionCallback, getCallingPackageName());
        this.mTransactionManager.addTransaction(transaction);
    }

    public void queryNanoApps(int contextHubId, android.hardware.location.IContextHubTransactionCallback transactionCallback) throws android.os.RemoteException {
        super.queryNanoApps_enforcePermission();
        if (!checkHalProxyAndContextHubId(contextHubId, transactionCallback, 4)) {
            return;
        }
        com.android.server.location.contexthub.ContextHubServiceTransaction transaction = this.mTransactionManager.createQueryTransaction(contextHubId, transactionCallback, getCallingPackageName());
        this.mTransactionManager.addTransaction(transaction);
    }

    public long[] getPreloadedNanoAppIds(android.hardware.location.ContextHubInfo hubInfo) throws android.os.RemoteException {
        super.getPreloadedNanoAppIds_enforcePermission();
        java.util.Objects.requireNonNull(hubInfo, "hubInfo cannot be null");
        long[] nanoappIds = this.mContextHubWrapper.getPreloadedNanoappIds(hubInfo.getId());
        if (nanoappIds == null) {
            return new long[0];
        }
        return nanoappIds;
    }

    public boolean setTestMode(boolean enable) {
        super.setTestMode_enforcePermission();
        boolean status = this.mContextHubWrapper.setTestMode(enable);
        if (status) {
            this.mIsTestModeEnabled.set(enable);
        }
        java.util.Iterator<java.lang.Integer> it = this.mDefaultClientMap.keySet().iterator();
        while (it.hasNext()) {
            int contextHubId = it.next().intValue();
            queryNanoAppsInternal(contextHubId);
        }
        return status;
    }

    protected void dump(java.io.FileDescriptor fd, final java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            for (java.lang.String arg : args) {
                if ("--proto".equals(arg)) {
                    dump(new android.util.proto.ProtoOutputStream(fd));
                    return;
                }
            }
            pw.println("Dumping ContextHub Service");
            pw.println("");
            pw.println("=================== CONTEXT HUBS ====================");
            for (android.hardware.location.ContextHubInfo hubInfo : this.mContextHubIdToInfoMap.values()) {
                pw.println(hubInfo);
            }
            pw.println("Supported permissions: " + java.util.Arrays.toString(this.mSupportedContextHubPerms.toArray()));
            pw.println("");
            pw.println("=================== NANOAPPS ====================");
            com.android.server.location.contexthub.NanoAppStateManager nanoAppStateManager = this.mNanoAppStateManager;
            java.util.Objects.requireNonNull(pw);
            nanoAppStateManager.foreachNanoAppInstanceInfo(new java.util.function.Consumer() { // from class: com.android.server.location.contexthub.ContextHubService$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    pw.println((android.hardware.location.NanoAppInstanceInfo) obj);
                }
            });
            pw.println("");
            pw.println("=================== PRELOADED NANOAPPS ====================");
            dumpPreloadedNanoapps(pw);
            pw.println("");
            pw.println("=================== CLIENTS ====================");
            pw.println(this.mClientManager);
            pw.println("");
            pw.println("=================== TRANSACTIONS ====================");
            pw.println(this.mTransactionManager);
            pw.println("");
            pw.println("=================== EVENTS ====================");
            pw.println(com.android.server.location.contexthub.ContextHubEventLogger.getInstance());
        }
    }

    void denyClientAuthState(int contextHubId, final java.lang.String packageName, final long nanoAppId) {
        android.util.Log.i(TAG, "Denying " + packageName + " access to " + java.lang.Long.toHexString(nanoAppId) + " on context hub # " + contextHubId);
        this.mClientManager.forEachClientOfHub(contextHubId, new java.util.function.Consumer() { // from class: com.android.server.location.contexthub.ContextHubService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.location.contexthub.ContextHubService.lambda$denyClientAuthState$3(packageName, nanoAppId, (com.android.server.location.contexthub.ContextHubClientBroker) obj);
            }
        });
    }

    static /* synthetic */ void lambda$denyClientAuthState$3(java.lang.String packageName, long nanoAppId, com.android.server.location.contexthub.ContextHubClientBroker client) {
        if (client.getPackageName().equals(packageName)) {
            client.updateNanoAppAuthState(nanoAppId, java.util.Collections.emptyList(), false, true);
        }
    }

    private void dump(final android.util.proto.ProtoOutputStream proto) {
        this.mContextHubIdToInfoMap.values().forEach(new java.util.function.Consumer() { // from class: com.android.server.location.contexthub.ContextHubService$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.location.contexthub.ContextHubService.lambda$dump$4(proto, (android.hardware.location.ContextHubInfo) obj);
            }
        });
        long token = proto.start(1146756268034L);
        this.mClientManager.dump(proto);
        proto.end(token);
        proto.flush();
    }

    static /* synthetic */ void lambda$dump$4(android.util.proto.ProtoOutputStream proto, android.hardware.location.ContextHubInfo hubInfo) {
        long token = proto.start(2246267895809L);
        hubInfo.dump(proto);
        proto.end(token);
    }

    private void dumpPreloadedNanoapps(java.io.PrintWriter pw) {
        int contextHubId;
        long[] preloadedNanoappIds;
        if (this.mContextHubWrapper == null) {
            return;
        }
        java.util.Iterator<java.lang.Integer> it = this.mContextHubIdToInfoMap.keySet().iterator();
        while (it.hasNext() && (preloadedNanoappIds = this.mContextHubWrapper.getPreloadedNanoappIds((contextHubId = it.next().intValue()))) != null) {
            pw.print("Context Hub (id=");
            pw.print(contextHubId);
            pw.println("):");
            for (long preloadedNanoappId : preloadedNanoappIds) {
                pw.print("  ID: 0x");
                pw.println(java.lang.Long.toHexString(preloadedNanoappId));
            }
        }
    }

    private void checkPermissions() {
        com.android.server.location.contexthub.ContextHubServiceUtil.checkPermissions(this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int onMessageReceiptOldApi(int msgType, int contextHubHandle, int appInstance, byte[] data) {
        if (data == null) {
            return -1;
        }
        synchronized (this.mCallbacksList) {
            int callbacksCount = this.mCallbacksList.beginBroadcast();
            if (callbacksCount < 1) {
                return 0;
            }
            android.hardware.location.ContextHubMessage msg = new android.hardware.location.ContextHubMessage(msgType, 0, data);
            for (int i = 0; i < callbacksCount; i++) {
                android.hardware.location.IContextHubCallback callback = this.mCallbacksList.getBroadcastItem(i);
                try {
                    callback.onMessageReceipt(contextHubHandle, appInstance, msg);
                } catch (android.os.RemoteException e) {
                    android.util.Log.i(TAG, "Exception (" + e + ") calling remote callback (" + callback + ").");
                }
            }
            this.mCallbacksList.finishBroadcast();
            return 0;
        }
    }

    private boolean checkHalProxyAndContextHubId(int contextHubId, android.hardware.location.IContextHubTransactionCallback callback, int transactionType) {
        if (this.mContextHubWrapper == null) {
            try {
                callback.onTransactionComplete(8);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "RemoteException while calling onTransactionComplete", e);
            }
            return false;
        }
        if (!isValidContextHubId(contextHubId)) {
            android.util.Log.e(TAG, "Cannot start " + android.hardware.location.ContextHubTransaction.typeToString(transactionType, false) + " transaction for invalid hub ID " + contextHubId);
            try {
                callback.onTransactionComplete(2);
            } catch (android.os.RemoteException e2) {
                android.util.Log.e(TAG, "RemoteException while calling onTransactionComplete", e2);
            }
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendLocationSettingUpdate() {
        boolean enabled = ((android.location.LocationManager) this.mContext.getSystemService(android.location.LocationManager.class)).isLocationEnabledForUser(android.os.UserHandle.CURRENT);
        this.mContextHubWrapper.onLocationSettingChanged(enabled);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendWifiSettingUpdate(boolean forceUpdate) {
        synchronized (this.mSendWifiSettingUpdateLock) {
            android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) this.mContext.getSystemService(android.net.wifi.WifiManager.class);
            boolean wifiEnabled = wifiManager.isWifiEnabled();
            boolean wifiScanEnabled = wifiManager.isScanAlwaysAvailable();
            boolean wifiAvailable = wifiEnabled || wifiScanEnabled;
            if (forceUpdate || this.mIsWifiAvailable != wifiAvailable) {
                this.mIsWifiAvailable = wifiAvailable;
                this.mContextHubWrapper.onWifiSettingChanged(wifiAvailable);
            }
            if (forceUpdate || this.mIsWifiScanningEnabled != wifiScanEnabled) {
                this.mIsWifiScanningEnabled = wifiScanEnabled;
                this.mContextHubWrapper.onWifiScanningSettingChanged(wifiScanEnabled);
            }
            if (forceUpdate || this.mIsWifiMainEnabled != wifiEnabled) {
                this.mIsWifiMainEnabled = wifiEnabled;
                this.mContextHubWrapper.onWifiMainSettingChanged(wifiEnabled);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBtSettingUpdate(boolean forceUpdate) {
        android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            boolean btEnabled = adapter.isEnabled();
            boolean btScanEnabled = adapter.isBleScanAlwaysAvailable();
            if (forceUpdate || this.mIsBtScanningEnabled != btScanEnabled) {
                this.mIsBtScanningEnabled = btScanEnabled;
                this.mContextHubWrapper.onBtScanningSettingChanged(btScanEnabled);
            }
            if (forceUpdate || this.mIsBtMainEnabled != btEnabled) {
                this.mIsBtMainEnabled = btEnabled;
                this.mContextHubWrapper.onBtMainSettingChanged(btEnabled);
                return;
            }
            return;
        }
        android.util.Log.d(TAG, "BT adapter not available. Getting permissions from user settings");
        boolean btEnabled2 = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "bluetooth_on", 0) == 1;
        boolean btScanEnabled2 = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "ble_scan_always_enabled", 0) == 1;
        if (forceUpdate || this.mIsBtMainEnabled != btEnabled2) {
            this.mIsBtMainEnabled = btEnabled2;
            this.mContextHubWrapper.onBtMainSettingChanged(this.mIsBtMainEnabled);
        }
        if (forceUpdate || this.mIsBtScanningEnabled != btScanEnabled2) {
            this.mIsBtScanningEnabled = btScanEnabled2;
            this.mContextHubWrapper.onBtScanningSettingChanged(this.mIsBtScanningEnabled);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAirplaneModeSettingUpdate() {
        boolean enabled = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
        this.mContextHubWrapper.onAirplaneModeSettingChanged(enabled);
    }

    private void sendMicrophoneDisableSettingUpdate(boolean enabled) {
        android.util.Log.d(TAG, "Mic Disabled Setting: " + enabled);
        this.mContextHubWrapper.onMicrophoneSettingChanged(!enabled);
    }

    private void sendMicrophoneDisableSettingUpdateForCurrentUser() {
        boolean isEnabled = this.mSensorPrivacyManagerInternal == null ? false : this.mSensorPrivacyManagerInternal.isSensorPrivacyEnabled(getCurrentUserId(), 1);
        sendMicrophoneDisableSettingUpdate(isEnabled);
    }

    private void scheduleDailyMetricSnapshot() {
        java.lang.Runnable queryAllContextHub = new java.lang.Runnable() { // from class: com.android.server.location.contexthub.ContextHubService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleDailyMetricSnapshot$5();
            }
        };
        try {
            this.mDailyMetricTimer.schedule(queryAllContextHub, 1L, java.util.concurrent.TimeUnit.DAYS);
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Error when schedule a timer", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleDailyMetricSnapshot$5() {
        java.util.Iterator<java.lang.Integer> it = this.mContextHubIdToInfoMap.keySet().iterator();
        while (it.hasNext()) {
            int contextHubId = it.next().intValue();
            this.mMetricQueryPendingContextHubIds.add(java.lang.Integer.valueOf(contextHubId));
            queryNanoAppsInternal(contextHubId);
        }
    }

    private java.lang.String getCallingPackageName() {
        return this.mContext.getPackageManager().getNameForUid(android.os.Binder.getCallingUid());
    }

    private int getCurrentUserId() {
        long id = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.UserInfo currentUser = android.app.ActivityManager.getService().getCurrentUser();
            int i = currentUser.id;
            android.os.Binder.restoreCallingIdentity(id);
            return i;
        } catch (android.os.RemoteException e) {
            android.os.Binder.restoreCallingIdentity(id);
            return 0;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(id);
            throw th;
        }
    }

    public void onUserChanged() {
        android.util.Log.d(TAG, "User changed to id: " + getCurrentUserId());
        sendLocationSettingUpdate();
        sendMicrophoneDisableSettingUpdateForCurrentUser();
    }
}
