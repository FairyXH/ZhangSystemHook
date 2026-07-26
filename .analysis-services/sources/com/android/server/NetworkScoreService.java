package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class NetworkScoreService extends android.net.INetworkScoreService.Stub {
    private static final boolean DBG;
    private static final java.lang.String TAG = "NetworkScoreService";
    private static final boolean VERBOSE;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private android.content.BroadcastReceiver mLocationModeReceiver;
    private final com.android.server.NetworkScorerAppManager mNetworkScorerAppManager;
    private com.android.server.NetworkScoreService.NetworkScorerPackageMonitor mPackageMonitor;
    private final java.lang.Object mPackageMonitorLock;
    private final com.android.server.NetworkScoreService.DispatchingContentObserver mRecommendationSettingsObserver;
    private final java.util.Map<java.lang.Integer, android.os.RemoteCallbackList<android.net.INetworkScoreCache>> mScoreCaches;
    private final java.util.function.Function<android.net.NetworkScorerAppData, com.android.server.NetworkScoreService.ScoringServiceConnection> mServiceConnProducer;
    private com.android.server.NetworkScoreService.ScoringServiceConnection mServiceConnection;
    private final java.lang.Object mServiceConnectionLock;
    private final android.database.ContentObserver mUseOpenWifiPackageObserver;
    private android.content.BroadcastReceiver mUserIntentReceiver;

    static {
        DBG = android.os.Build.IS_DEBUGGABLE && android.util.Log.isLoggable(TAG, 3);
        VERBOSE = android.os.Build.IS_DEBUGGABLE && android.util.Log.isLoggable(TAG, 2);
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private final com.android.server.NetworkScoreService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
            this.mService = new com.android.server.NetworkScoreService(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            android.util.Log.i(com.android.server.NetworkScoreService.TAG, "Registering network_score");
            publishBinderService("network_score", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 500) {
                this.mService.systemReady();
            } else if (phase == 1000) {
                this.mService.systemRunning();
            }
        }
    }

    private class NetworkScorerPackageMonitor extends com.android.internal.content.PackageMonitor {
        final java.lang.String mPackageToWatch;

        private NetworkScorerPackageMonitor(java.lang.String packageToWatch) {
            this.mPackageToWatch = packageToWatch;
        }

        public void onPackageAdded(java.lang.String packageName, int uid) {
            evaluateBinding(packageName, true);
        }

        public void onPackageRemoved(java.lang.String packageName, int uid) {
            evaluateBinding(packageName, true);
        }

        public void onPackageModified(java.lang.String packageName) {
            evaluateBinding(packageName, false);
        }

        public boolean onHandleForceStop(android.content.Intent intent, java.lang.String[] packages, int uid, boolean doit) {
            if (doit) {
                for (java.lang.String packageName : packages) {
                    evaluateBinding(packageName, true);
                }
            }
            return super.onHandleForceStop(intent, packages, uid, doit);
        }

        public void onPackageUpdateFinished(java.lang.String packageName, int uid) {
            evaluateBinding(packageName, true);
        }

        private void evaluateBinding(java.lang.String changedPackageName, boolean forceUnbind) {
            if (!this.mPackageToWatch.equals(changedPackageName)) {
                return;
            }
            if (com.android.server.NetworkScoreService.DBG) {
                android.util.Log.d(com.android.server.NetworkScoreService.TAG, "Evaluating binding for: " + changedPackageName + ", forceUnbind=" + forceUnbind);
            }
            android.net.NetworkScorerAppData activeScorer = com.android.server.NetworkScoreService.this.mNetworkScorerAppManager.getActiveScorer();
            if (activeScorer == null) {
                if (com.android.server.NetworkScoreService.DBG) {
                    android.util.Log.d(com.android.server.NetworkScoreService.TAG, "No active scorers available.");
                }
                com.android.server.NetworkScoreService.this.refreshBinding();
            } else {
                if (forceUnbind) {
                    com.android.server.NetworkScoreService.this.unbindFromScoringServiceIfNeeded();
                }
                if (com.android.server.NetworkScoreService.DBG) {
                    android.util.Log.d(com.android.server.NetworkScoreService.TAG, "Binding to " + activeScorer.getRecommendationServiceComponent() + " if needed.");
                }
                com.android.server.NetworkScoreService.this.bindToScoringServiceIfNeeded(activeScorer);
            }
        }
    }

    public static class DispatchingContentObserver extends android.database.ContentObserver {
        private final android.content.Context mContext;
        private final android.os.Handler mHandler;
        private final java.util.Map<android.net.Uri, java.lang.Integer> mUriEventMap;

        public DispatchingContentObserver(android.content.Context context, android.os.Handler handler) {
            super(handler);
            this.mContext = context;
            this.mHandler = handler;
            this.mUriEventMap = new android.util.ArrayMap();
        }

        void observe(android.net.Uri uri, int what) {
            this.mUriEventMap.put(uri, java.lang.Integer.valueOf(what));
            android.content.ContentResolver resolver = this.mContext.getContentResolver();
            resolver.registerContentObserver(uri, false, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (com.android.server.NetworkScoreService.DBG) {
                android.util.Log.d(com.android.server.NetworkScoreService.TAG, java.lang.String.format("onChange(%s, %s)", java.lang.Boolean.valueOf(selfChange), uri));
            }
            java.lang.Integer what = this.mUriEventMap.get(uri);
            if (what != null) {
                this.mHandler.obtainMessage(what.intValue()).sendToTarget();
            } else {
                android.util.Log.w(com.android.server.NetworkScoreService.TAG, "No matching event to send for URI = " + uri);
            }
        }
    }

    public NetworkScoreService(android.content.Context context) {
        this(context, new com.android.server.NetworkScorerAppManager(context), new java.util.function.Function() { // from class: com.android.server.NetworkScoreService$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return new com.android.server.NetworkScoreService.ScoringServiceConnection((android.net.NetworkScorerAppData) obj);
            }
        }, android.os.Looper.myLooper());
    }

    NetworkScoreService(android.content.Context context, com.android.server.NetworkScorerAppManager networkScoreAppManager, java.util.function.Function<android.net.NetworkScorerAppData, com.android.server.NetworkScoreService.ScoringServiceConnection> serviceConnProducer, android.os.Looper looper) {
        this.mPackageMonitorLock = new java.lang.Object();
        this.mServiceConnectionLock = new java.lang.Object();
        this.mUserIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.NetworkScoreService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                if (com.android.server.NetworkScoreService.DBG) {
                    android.util.Log.d(com.android.server.NetworkScoreService.TAG, "Received " + action + " for userId " + userId);
                }
                if (userId != -10000 && "android.intent.action.USER_UNLOCKED".equals(action)) {
                    com.android.server.NetworkScoreService.this.onUserUnlocked(userId);
                }
            }
        };
        this.mLocationModeReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.NetworkScoreService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                if ("android.location.MODE_CHANGED".equals(action)) {
                    com.android.server.NetworkScoreService.this.refreshBinding();
                }
            }
        };
        this.mContext = context;
        this.mNetworkScorerAppManager = networkScoreAppManager;
        this.mScoreCaches = new android.util.ArrayMap();
        android.content.IntentFilter filter = new android.content.IntentFilter("android.intent.action.USER_UNLOCKED");
        this.mContext.registerReceiverAsUser(this.mUserIntentReceiver, android.os.UserHandle.SYSTEM, filter, null, null);
        this.mHandler = new com.android.server.NetworkScoreService.ServiceHandler(looper);
        android.content.IntentFilter locationModeFilter = new android.content.IntentFilter("android.location.MODE_CHANGED");
        this.mContext.registerReceiverAsUser(this.mLocationModeReceiver, android.os.UserHandle.SYSTEM, locationModeFilter, null, this.mHandler);
        this.mRecommendationSettingsObserver = new com.android.server.NetworkScoreService.DispatchingContentObserver(context, this.mHandler);
        this.mServiceConnProducer = serviceConnProducer;
        this.mUseOpenWifiPackageObserver = new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.NetworkScoreService.3
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
                android.net.Uri useOpenWifiPkgUri = android.provider.Settings.Global.getUriFor("use_open_wifi_package");
                if (useOpenWifiPkgUri.equals(uri)) {
                    java.lang.String useOpenWifiPackage = android.provider.Settings.Global.getString(com.android.server.NetworkScoreService.this.mContext.getContentResolver(), "use_open_wifi_package");
                    if (!android.text.TextUtils.isEmpty(useOpenWifiPackage)) {
                        ((com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class)).grantDefaultPermissionsToDefaultUseOpenWifiApp(useOpenWifiPackage, userId);
                    }
                }
            }
        };
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("use_open_wifi_package"), false, this.mUseOpenWifiPackageObserver);
        ((com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class)).setUseOpenWifiAppPackagesProvider(new com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider() { // from class: com.android.server.NetworkScoreService$$ExternalSyntheticLambda0
            @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider
            public final java.lang.String[] getPackages(int i) {
                return this.f$0.lambda$new$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String[] lambda$new$0(int userId) {
        java.lang.String useOpenWifiPackage = android.provider.Settings.Global.getString(this.mContext.getContentResolver(), "use_open_wifi_package");
        if (!android.text.TextUtils.isEmpty(useOpenWifiPackage)) {
            return new java.lang.String[]{useOpenWifiPackage};
        }
        return null;
    }

    void systemReady() {
        if (DBG) {
            android.util.Log.d(TAG, "systemReady");
        }
        registerRecommendationSettingsObserver();
    }

    void systemRunning() {
        if (DBG) {
            android.util.Log.d(TAG, "systemRunning");
        }
    }

    void onUserUnlocked(int userId) {
        if (DBG) {
            android.util.Log.d(TAG, "onUserUnlocked(" + userId + ")");
        }
        refreshBinding();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshBinding() {
        if (DBG) {
            android.util.Log.d(TAG, "refreshBinding()");
        }
        this.mNetworkScorerAppManager.updateState();
        this.mNetworkScorerAppManager.migrateNetworkScorerAppSettingIfNeeded();
        registerPackageMonitorIfNeeded();
        bindToScoringServiceIfNeeded();
    }

    private void registerRecommendationSettingsObserver() {
        android.net.Uri packageNameUri = android.provider.Settings.Global.getUriFor("network_recommendations_package");
        this.mRecommendationSettingsObserver.observe(packageNameUri, 1);
        android.net.Uri settingUri = android.provider.Settings.Global.getUriFor("network_recommendations_enabled");
        this.mRecommendationSettingsObserver.observe(settingUri, 2);
    }

    private void registerPackageMonitorIfNeeded() {
        if (DBG) {
            android.util.Log.d(TAG, "registerPackageMonitorIfNeeded()");
        }
        android.net.NetworkScorerAppData appData = this.mNetworkScorerAppManager.getActiveScorer();
        synchronized (this.mPackageMonitorLock) {
            if (this.mPackageMonitor != null && (appData == null || !appData.getRecommendationServicePackageName().equals(this.mPackageMonitor.mPackageToWatch))) {
                if (DBG) {
                    android.util.Log.d(TAG, "Unregistering package monitor for " + this.mPackageMonitor.mPackageToWatch);
                }
                this.mPackageMonitor.unregister();
                this.mPackageMonitor = null;
            }
            if (appData != null && this.mPackageMonitor == null) {
                this.mPackageMonitor = new com.android.server.NetworkScoreService.NetworkScorerPackageMonitor(appData.getRecommendationServicePackageName());
                this.mPackageMonitor.register(this.mContext, null, android.os.UserHandle.SYSTEM, false);
                if (DBG) {
                    android.util.Log.d(TAG, "Registered package monitor for " + this.mPackageMonitor.mPackageToWatch);
                }
            }
        }
    }

    private void bindToScoringServiceIfNeeded() {
        if (DBG) {
            android.util.Log.d(TAG, "bindToScoringServiceIfNeeded");
        }
        android.net.NetworkScorerAppData scorerData = this.mNetworkScorerAppManager.getActiveScorer();
        bindToScoringServiceIfNeeded(scorerData);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindToScoringServiceIfNeeded(android.net.NetworkScorerAppData appData) {
        if (DBG) {
            android.util.Log.d(TAG, "bindToScoringServiceIfNeeded(" + appData + ")");
        }
        if (appData != null) {
            synchronized (this.mServiceConnectionLock) {
                if (this.mServiceConnection != null && !this.mServiceConnection.getAppData().equals(appData)) {
                    unbindFromScoringServiceIfNeeded();
                }
                if (this.mServiceConnection == null) {
                    this.mServiceConnection = this.mServiceConnProducer.apply(appData);
                }
                this.mServiceConnection.bind(this.mContext);
            }
            return;
        }
        unbindFromScoringServiceIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindFromScoringServiceIfNeeded() {
        if (DBG) {
            android.util.Log.d(TAG, "unbindFromScoringServiceIfNeeded");
        }
        synchronized (this.mServiceConnectionLock) {
            if (this.mServiceConnection != null) {
                this.mServiceConnection.unbind(this.mContext);
                if (DBG) {
                    android.util.Log.d(TAG, "Disconnected from: " + this.mServiceConnection.getAppData().getRecommendationServiceComponent());
                }
            }
            this.mServiceConnection = null;
        }
        clearInternal();
    }

    public boolean updateScores(android.net.ScoredNetwork[] networks) {
        android.os.RemoteCallbackList<android.net.INetworkScoreCache> callbackList;
        if (!isCallerActiveScorer(getCallingUid())) {
            throw new java.lang.SecurityException("Caller with UID " + getCallingUid() + " is not the active scorer.");
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            java.util.Map<java.lang.Integer, java.util.List<android.net.ScoredNetwork>> networksByType = new android.util.ArrayMap<>();
            for (android.net.ScoredNetwork network : networks) {
                java.util.List<android.net.ScoredNetwork> networkList = networksByType.get(java.lang.Integer.valueOf(network.networkKey.type));
                if (networkList == null) {
                    networkList = new java.util.ArrayList<>();
                    networksByType.put(java.lang.Integer.valueOf(network.networkKey.type), networkList);
                }
                networkList.add(network);
            }
            java.util.Iterator<java.util.Map.Entry<java.lang.Integer, java.util.List<android.net.ScoredNetwork>>> it = networksByType.entrySet().iterator();
            while (true) {
                boolean isEmpty = true;
                if (!it.hasNext()) {
                    return true;
                }
                java.util.Map.Entry<java.lang.Integer, java.util.List<android.net.ScoredNetwork>> entry = it.next();
                synchronized (this.mScoreCaches) {
                    callbackList = this.mScoreCaches.get(entry.getKey());
                    if (callbackList != null && callbackList.getRegisteredCallbackCount() != 0) {
                        isEmpty = false;
                    }
                }
                if (isEmpty) {
                    if (android.util.Log.isLoggable(TAG, 2)) {
                        android.util.Log.v(TAG, "No scorer registered for type " + entry.getKey() + ", discarding");
                    }
                } else {
                    java.util.function.BiConsumer<android.net.INetworkScoreCache, java.lang.Object> consumer = com.android.server.NetworkScoreService.FilteringCacheUpdatingConsumer.create(this.mContext, entry.getValue(), entry.getKey().intValue());
                    sendCacheUpdateCallback(consumer, java.util.Collections.singleton(callbackList));
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    static class FilteringCacheUpdatingConsumer implements java.util.function.BiConsumer<android.net.INetworkScoreCache, java.lang.Object> {
        private final android.content.Context mContext;
        private java.util.function.UnaryOperator<java.util.List<android.net.ScoredNetwork>> mCurrentNetworkFilter;
        private final int mNetworkType;
        private java.util.function.UnaryOperator<java.util.List<android.net.ScoredNetwork>> mScanResultsFilter;
        private final java.util.List<android.net.ScoredNetwork> mScoredNetworkList;

        static com.android.server.NetworkScoreService.FilteringCacheUpdatingConsumer create(android.content.Context context, java.util.List<android.net.ScoredNetwork> scoredNetworkList, int networkType) {
            return new com.android.server.NetworkScoreService.FilteringCacheUpdatingConsumer(context, scoredNetworkList, networkType, null, null);
        }

        FilteringCacheUpdatingConsumer(android.content.Context context, java.util.List<android.net.ScoredNetwork> scoredNetworkList, int networkType, java.util.function.UnaryOperator<java.util.List<android.net.ScoredNetwork>> currentNetworkFilter, java.util.function.UnaryOperator<java.util.List<android.net.ScoredNetwork>> scanResultsFilter) {
            this.mContext = context;
            this.mScoredNetworkList = scoredNetworkList;
            this.mNetworkType = networkType;
            this.mCurrentNetworkFilter = currentNetworkFilter;
            this.mScanResultsFilter = scanResultsFilter;
        }

        @Override // java.util.function.BiConsumer
        public void accept(android.net.INetworkScoreCache networkScoreCache, java.lang.Object cookie) {
            int filterType = 0;
            if (cookie instanceof java.lang.Integer) {
                filterType = ((java.lang.Integer) cookie).intValue();
            }
            try {
                java.util.List<android.net.ScoredNetwork> filteredNetworkList = filterScores(this.mScoredNetworkList, filterType);
                if (!filteredNetworkList.isEmpty()) {
                    networkScoreCache.updateScores(filteredNetworkList);
                }
            } catch (android.os.RemoteException e) {
                if (com.android.server.NetworkScoreService.VERBOSE) {
                    android.util.Log.v(com.android.server.NetworkScoreService.TAG, "Unable to update scores of type " + this.mNetworkType, e);
                }
            }
        }

        private java.util.List<android.net.ScoredNetwork> filterScores(java.util.List<android.net.ScoredNetwork> scoredNetworkList, int filterType) {
            switch (filterType) {
                case 0:
                    return scoredNetworkList;
                case 1:
                    if (this.mCurrentNetworkFilter == null) {
                        this.mCurrentNetworkFilter = new com.android.server.NetworkScoreService.CurrentNetworkScoreCacheFilter(new com.android.server.NetworkScoreService.WifiInfoSupplier(this.mContext));
                    }
                    return (java.util.List) this.mCurrentNetworkFilter.apply(scoredNetworkList);
                case 2:
                    if (this.mScanResultsFilter == null) {
                        this.mScanResultsFilter = new com.android.server.NetworkScoreService.ScanResultsScoreCacheFilter(new com.android.server.NetworkScoreService.ScanResultsSupplier(this.mContext));
                    }
                    return (java.util.List) this.mScanResultsFilter.apply(scoredNetworkList);
                default:
                    android.util.Log.w(com.android.server.NetworkScoreService.TAG, "Unknown filter type: " + filterType);
                    return scoredNetworkList;
            }
        }
    }

    private static class WifiInfoSupplier implements java.util.function.Supplier<android.net.wifi.WifiInfo> {
        private final android.content.Context mContext;

        WifiInfoSupplier(android.content.Context context) {
            this.mContext = context;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.function.Supplier
        public android.net.wifi.WifiInfo get() {
            android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) this.mContext.getSystemService(android.net.wifi.WifiManager.class);
            if (wifiManager != null) {
                return wifiManager.getConnectionInfo();
            }
            android.util.Log.w(com.android.server.NetworkScoreService.TAG, "WifiManager is null, failed to return the WifiInfo.");
            return null;
        }
    }

    private static class ScanResultsSupplier implements java.util.function.Supplier<java.util.List<android.net.wifi.ScanResult>> {
        private final android.content.Context mContext;

        ScanResultsSupplier(android.content.Context context) {
            this.mContext = context;
        }

        @Override // java.util.function.Supplier
        public java.util.List<android.net.wifi.ScanResult> get() {
            android.net.wifi.WifiScanner wifiScanner = (android.net.wifi.WifiScanner) this.mContext.getSystemService(android.net.wifi.WifiScanner.class);
            if (wifiScanner != null) {
                return wifiScanner.getSingleScanResults();
            }
            android.util.Log.w(com.android.server.NetworkScoreService.TAG, "WifiScanner is null, failed to return scan results.");
            return java.util.Collections.emptyList();
        }
    }

    static class CurrentNetworkScoreCacheFilter implements java.util.function.UnaryOperator<java.util.List<android.net.ScoredNetwork>> {
        private final android.net.NetworkKey mCurrentNetwork;

        CurrentNetworkScoreCacheFilter(java.util.function.Supplier<android.net.wifi.WifiInfo> wifiInfoSupplier) {
            this.mCurrentNetwork = android.net.NetworkKey.createFromWifiInfo(wifiInfoSupplier.get());
        }

        @Override // java.util.function.Function
        public java.util.List<android.net.ScoredNetwork> apply(java.util.List<android.net.ScoredNetwork> scoredNetworks) {
            if (this.mCurrentNetwork == null || scoredNetworks.isEmpty()) {
                return java.util.Collections.emptyList();
            }
            for (int i = 0; i < scoredNetworks.size(); i++) {
                android.net.ScoredNetwork scoredNetwork = scoredNetworks.get(i);
                if (scoredNetwork.networkKey.equals(this.mCurrentNetwork)) {
                    return java.util.Collections.singletonList(scoredNetwork);
                }
            }
            return java.util.Collections.emptyList();
        }
    }

    static class ScanResultsScoreCacheFilter implements java.util.function.UnaryOperator<java.util.List<android.net.ScoredNetwork>> {
        private final java.util.Set<android.net.NetworkKey> mScanResultKeys;

        ScanResultsScoreCacheFilter(java.util.function.Supplier<java.util.List<android.net.wifi.ScanResult>> resultsSupplier) {
            java.util.List<android.net.wifi.ScanResult> scanResults = resultsSupplier.get();
            int size = scanResults.size();
            this.mScanResultKeys = new android.util.ArraySet(size);
            for (int i = 0; i < size; i++) {
                android.net.wifi.ScanResult scanResult = scanResults.get(i);
                android.net.NetworkKey key = android.net.NetworkKey.createFromScanResult(scanResult);
                if (key != null) {
                    this.mScanResultKeys.add(key);
                }
            }
        }

        @Override // java.util.function.Function
        public java.util.List<android.net.ScoredNetwork> apply(java.util.List<android.net.ScoredNetwork> scoredNetworks) {
            if (this.mScanResultKeys.isEmpty() || scoredNetworks.isEmpty()) {
                return java.util.Collections.emptyList();
            }
            java.util.List<android.net.ScoredNetwork> filteredScores = new java.util.ArrayList<>();
            for (int i = 0; i < scoredNetworks.size(); i++) {
                android.net.ScoredNetwork scoredNetwork = scoredNetworks.get(i);
                if (this.mScanResultKeys.contains(scoredNetwork.networkKey)) {
                    filteredScores.add(scoredNetwork);
                }
            }
            return filteredScores;
        }
    }

    public boolean clearScores() {
        enforceSystemOrIsActiveScorer(getCallingUid());
        long token = android.os.Binder.clearCallingIdentity();
        try {
            clearInternal();
            android.os.Binder.restoreCallingIdentity(token);
            return true;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean setActiveScorer(java.lang.String packageName) {
        enforceSystemOrHasScoreNetworks();
        return this.mNetworkScorerAppManager.setActiveScorer(packageName);
    }

    public boolean isCallerActiveScorer(int callingUid) {
        boolean z;
        synchronized (this.mServiceConnectionLock) {
            z = this.mServiceConnection != null && this.mServiceConnection.getAppData().packageUid == callingUid;
        }
        return z;
    }

    private void enforceSystemOnly() throws java.lang.SecurityException {
        this.mContext.enforceCallingOrSelfPermission("android.permission.REQUEST_NETWORK_SCORES", "Caller must be granted REQUEST_NETWORK_SCORES.");
    }

    private void enforceSystemOrHasScoreNetworks() throws java.lang.SecurityException {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.REQUEST_NETWORK_SCORES") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.SCORE_NETWORKS") != 0) {
            throw new java.lang.SecurityException("Caller is neither the system process or a network scorer.");
        }
    }

    private void enforceSystemOrIsActiveScorer(int callingUid) throws java.lang.SecurityException {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.REQUEST_NETWORK_SCORES") != 0 && !isCallerActiveScorer(callingUid)) {
            throw new java.lang.SecurityException("Caller is neither the system process or the active network scorer.");
        }
    }

    public java.lang.String getActiveScorerPackage() {
        enforceSystemOrHasScoreNetworks();
        android.net.NetworkScorerAppData appData = this.mNetworkScorerAppManager.getActiveScorer();
        if (appData == null) {
            return null;
        }
        return appData.getRecommendationServicePackageName();
    }

    public android.net.NetworkScorerAppData getActiveScorer() {
        enforceSystemOnly();
        return this.mNetworkScorerAppManager.getActiveScorer();
    }

    public java.util.List<android.net.NetworkScorerAppData> getAllValidScorers() {
        enforceSystemOnly();
        return this.mNetworkScorerAppManager.getAllValidScorers();
    }

    public void disableScoring() {
        enforceSystemOrIsActiveScorer(getCallingUid());
    }

    private void clearInternal() {
        sendCacheUpdateCallback(new java.util.function.BiConsumer<android.net.INetworkScoreCache, java.lang.Object>() { // from class: com.android.server.NetworkScoreService.4
            @Override // java.util.function.BiConsumer
            public void accept(android.net.INetworkScoreCache networkScoreCache, java.lang.Object cookie) {
                try {
                    networkScoreCache.clearScores();
                } catch (android.os.RemoteException e) {
                    if (android.util.Log.isLoggable(com.android.server.NetworkScoreService.TAG, 2)) {
                        android.util.Log.v(com.android.server.NetworkScoreService.TAG, "Unable to clear scores", e);
                    }
                }
            }
        }, getScoreCacheLists());
    }

    public void registerNetworkScoreCache(int networkType, android.net.INetworkScoreCache scoreCache, int filterType) {
        enforceSystemOnly();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mScoreCaches) {
                android.os.RemoteCallbackList<android.net.INetworkScoreCache> callbackList = this.mScoreCaches.get(java.lang.Integer.valueOf(networkType));
                if (callbackList == null) {
                    callbackList = new android.os.RemoteCallbackList<>();
                    this.mScoreCaches.put(java.lang.Integer.valueOf(networkType), callbackList);
                }
                if (!callbackList.register(scoreCache, java.lang.Integer.valueOf(filterType))) {
                    if (callbackList.getRegisteredCallbackCount() == 0) {
                        this.mScoreCaches.remove(java.lang.Integer.valueOf(networkType));
                    }
                    if (android.util.Log.isLoggable(TAG, 2)) {
                        android.util.Log.v(TAG, "Unable to register NetworkScoreCache for type " + networkType);
                    }
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void unregisterNetworkScoreCache(int networkType, android.net.INetworkScoreCache scoreCache) {
        enforceSystemOnly();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mScoreCaches) {
                android.os.RemoteCallbackList<android.net.INetworkScoreCache> callbackList = this.mScoreCaches.get(java.lang.Integer.valueOf(networkType));
                if (callbackList == null || !callbackList.unregister(scoreCache)) {
                    if (android.util.Log.isLoggable(TAG, 2)) {
                        android.util.Log.v(TAG, "Unable to unregister NetworkScoreCache for type " + networkType);
                    }
                } else if (callbackList.getRegisteredCallbackCount() == 0) {
                    this.mScoreCaches.remove(java.lang.Integer.valueOf(networkType));
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public boolean requestScores(android.net.NetworkKey[] networks) {
        enforceSystemOnly();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.net.INetworkRecommendationProvider provider = getRecommendationProvider();
            if (provider != null) {
                try {
                    provider.requestScores(networks);
                    android.os.Binder.restoreCallingIdentity(token);
                    return true;
                } catch (android.os.RemoteException e) {
                    android.util.Log.w(TAG, "Failed to request scores.", e);
                }
            }
            android.os.Binder.restoreCallingIdentity(token);
            return false;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, writer)) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                android.net.NetworkScorerAppData currentScorer = this.mNetworkScorerAppManager.getActiveScorer();
                if (currentScorer == null) {
                    writer.println("Scoring is disabled.");
                    return;
                }
                writer.println("Current scorer: " + currentScorer);
                synchronized (this.mServiceConnectionLock) {
                    if (this.mServiceConnection != null) {
                        this.mServiceConnection.dump(fd, writer, args);
                    } else {
                        writer.println("ScoringServiceConnection: null");
                    }
                }
                writer.flush();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
    }

    private java.util.Collection<android.os.RemoteCallbackList<android.net.INetworkScoreCache>> getScoreCacheLists() {
        java.util.ArrayList arrayList;
        synchronized (this.mScoreCaches) {
            arrayList = new java.util.ArrayList(this.mScoreCaches.values());
        }
        return arrayList;
    }

    private void sendCacheUpdateCallback(java.util.function.BiConsumer<android.net.INetworkScoreCache, java.lang.Object> consumer, java.util.Collection<android.os.RemoteCallbackList<android.net.INetworkScoreCache>> remoteCallbackLists) {
        java.util.Iterator<android.os.RemoteCallbackList<android.net.INetworkScoreCache>> it = remoteCallbackLists.iterator();
        while (it.hasNext()) {
            android.os.RemoteCallbackList<android.net.INetworkScoreCache> callbackList = it.next();
            synchronized (callbackList) {
                int count = callbackList.beginBroadcast();
                for (int i = 0; i < count; i++) {
                    try {
                        consumer.accept((android.net.INetworkScoreCache) callbackList.getBroadcastItem(i), callbackList.getBroadcastCookie(i));
                    } finally {
                    }
                }
            }
        }
    }

    private android.net.INetworkRecommendationProvider getRecommendationProvider() {
        synchronized (this.mServiceConnectionLock) {
            if (this.mServiceConnection != null) {
                return this.mServiceConnection.getRecommendationProvider();
            }
            return null;
        }
    }

    public static class ScoringServiceConnection implements android.content.ServiceConnection {
        private final android.net.NetworkScorerAppData mAppData;
        private volatile boolean mBound = false;
        private volatile boolean mConnected = false;
        private volatile android.net.INetworkRecommendationProvider mRecommendationProvider;

        ScoringServiceConnection(android.net.NetworkScorerAppData appData) {
            this.mAppData = appData;
        }

        public void bind(android.content.Context context) {
            if (!this.mBound) {
                android.content.Intent service = new android.content.Intent("android.net.action.RECOMMEND_NETWORKS");
                service.setComponent(this.mAppData.getRecommendationServiceComponent());
                this.mBound = context.bindServiceAsUser(service, this, android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN, android.os.UserHandle.SYSTEM);
                if (!this.mBound) {
                    android.util.Log.w(com.android.server.NetworkScoreService.TAG, "Bind call failed for " + service);
                    context.unbindService(this);
                } else if (com.android.server.NetworkScoreService.DBG) {
                    android.util.Log.d(com.android.server.NetworkScoreService.TAG, "ScoringServiceConnection bound.");
                }
            }
        }

        public void unbind(android.content.Context context) {
            try {
                if (this.mBound) {
                    this.mBound = false;
                    context.unbindService(this);
                    if (com.android.server.NetworkScoreService.DBG) {
                        android.util.Log.d(com.android.server.NetworkScoreService.TAG, "ScoringServiceConnection unbound.");
                    }
                }
            } catch (java.lang.RuntimeException e) {
                android.util.Log.e(com.android.server.NetworkScoreService.TAG, "Unbind failed.", e);
            }
            this.mConnected = false;
            this.mRecommendationProvider = null;
        }

        public android.net.NetworkScorerAppData getAppData() {
            return this.mAppData;
        }

        public android.net.INetworkRecommendationProvider getRecommendationProvider() {
            return this.mRecommendationProvider;
        }

        public java.lang.String getPackageName() {
            return this.mAppData.getRecommendationServiceComponent().getPackageName();
        }

        public boolean isAlive() {
            return this.mBound && this.mConnected;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            if (com.android.server.NetworkScoreService.DBG) {
                android.util.Log.d(com.android.server.NetworkScoreService.TAG, "ScoringServiceConnection: " + name.flattenToString());
            }
            this.mConnected = true;
            this.mRecommendationProvider = android.net.INetworkRecommendationProvider.Stub.asInterface(service);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            if (com.android.server.NetworkScoreService.DBG) {
                android.util.Log.d(com.android.server.NetworkScoreService.TAG, "ScoringServiceConnection, disconnected: " + name.flattenToString());
            }
            this.mConnected = false;
            this.mRecommendationProvider = null;
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            writer.println("ScoringServiceConnection: " + this.mAppData.getRecommendationServiceComponent() + ", bound: " + this.mBound + ", connected: " + this.mConnected);
        }
    }

    public final class ServiceHandler extends android.os.Handler {
        public static final int MSG_RECOMMENDATIONS_PACKAGE_CHANGED = 1;
        public static final int MSG_RECOMMENDATION_ENABLED_SETTING_CHANGED = 2;

        public ServiceHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                case 2:
                    com.android.server.NetworkScoreService.this.refreshBinding();
                    break;
                default:
                    android.util.Log.w(com.android.server.NetworkScoreService.TAG, "Unknown message: " + what);
                    break;
            }
        }
    }
}
