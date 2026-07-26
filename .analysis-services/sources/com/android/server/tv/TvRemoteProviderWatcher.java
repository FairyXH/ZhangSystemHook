package com.android.server.tv;

/* JADX INFO: loaded from: classes3.dex */
final class TvRemoteProviderWatcher {
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final java.lang.Object mLock;
    private final android.content.pm.PackageManager mPackageManager;
    private final java.util.ArrayList<com.android.server.tv.TvRemoteProviderProxy> mProviderProxies;
    private boolean mRunning;
    private final android.content.BroadcastReceiver mScanPackagesReceiver;
    private final java.lang.Runnable mScanPackagesRunnable;
    private final java.util.Set<java.lang.String> mUnbundledServicePackages;
    private final int mUserId;
    private static final java.lang.String TAG = "TvRemoteProviderWatcher";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 2);

    TvRemoteProviderWatcher(android.content.Context context, java.lang.Object lock, android.os.Handler handler) {
        this.mProviderProxies = new java.util.ArrayList<>();
        this.mUnbundledServicePackages = new java.util.HashSet();
        this.mScanPackagesReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.tv.TvRemoteProviderWatcher.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if (com.android.server.tv.TvRemoteProviderWatcher.DEBUG) {
                    android.util.Slog.d(com.android.server.tv.TvRemoteProviderWatcher.TAG, "Received package manager broadcast: " + intent);
                }
                com.android.server.tv.TvRemoteProviderWatcher.this.mHandler.post(com.android.server.tv.TvRemoteProviderWatcher.this.mScanPackagesRunnable);
            }
        };
        this.mScanPackagesRunnable = new java.lang.Runnable() { // from class: com.android.server.tv.TvRemoteProviderWatcher.2
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.tv.TvRemoteProviderWatcher.this.scanPackages();
            }
        };
        this.mContext = context;
        this.mHandler = handler;
        this.mUserId = android.os.UserHandle.myUserId();
        this.mPackageManager = context.getPackageManager();
        this.mLock = lock;
        android.text.TextUtils.SimpleStringSplitter splitter = new android.text.TextUtils.SimpleStringSplitter(',');
        splitter.setString(context.getString(android.R.string.config_usbPermissionActivity));
        splitter.forEach(new java.util.function.Consumer() { // from class: com.android.server.tv.TvRemoteProviderWatcher$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$0((java.lang.String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(java.lang.String packageName) {
        java.lang.String packageName2 = packageName.trim();
        if (!packageName2.isEmpty()) {
            this.mUnbundledServicePackages.add(packageName2);
        }
    }

    TvRemoteProviderWatcher(android.content.Context context, java.lang.Object lock) {
        this(context, lock, new android.os.Handler(true));
    }

    public void start() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "start()");
        }
        if (!this.mRunning) {
            this.mRunning = true;
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.PACKAGE_ADDED");
            filter.addAction("android.intent.action.PACKAGE_REMOVED");
            filter.addAction("android.intent.action.PACKAGE_CHANGED");
            filter.addAction("android.intent.action.PACKAGE_REPLACED");
            filter.addAction("android.intent.action.PACKAGE_RESTARTED");
            filter.addDataScheme("package");
            this.mContext.registerReceiverAsUser(this.mScanPackagesReceiver, new android.os.UserHandle(this.mUserId), filter, null, this.mHandler);
            this.mHandler.post(this.mScanPackagesRunnable);
        }
    }

    public void stop() {
        if (this.mRunning) {
            this.mRunning = false;
            this.mContext.unregisterReceiver(this.mScanPackagesReceiver);
            this.mHandler.removeCallbacks(this.mScanPackagesRunnable);
            for (int i = this.mProviderProxies.size() - 1; i >= 0; i--) {
                this.mProviderProxies.get(i).stop();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scanPackages() {
        if (!this.mRunning) {
            return;
        }
        if (DEBUG) {
            android.util.Log.d(TAG, "scanPackages()");
        }
        int targetIndex = 0;
        android.content.Intent intent = new android.content.Intent("com.android.media.tv.remoteprovider.TvRemoteProvider");
        for (android.content.pm.ResolveInfo resolveInfo : this.mPackageManager.queryIntentServicesAsUser(intent, 0, this.mUserId)) {
            android.content.pm.ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (serviceInfo != null && verifyServiceTrusted(serviceInfo)) {
                int sourceIndex = findProvider(serviceInfo.packageName, serviceInfo.name);
                if (sourceIndex < 0) {
                    com.android.server.tv.TvRemoteProviderProxy providerProxy = new com.android.server.tv.TvRemoteProviderProxy(this.mContext, this.mLock, new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name), this.mUserId, serviceInfo.applicationInfo.uid);
                    providerProxy.start();
                    this.mProviderProxies.add(targetIndex, providerProxy);
                    targetIndex++;
                } else if (sourceIndex >= targetIndex) {
                    com.android.server.tv.TvRemoteProviderProxy provider = this.mProviderProxies.get(sourceIndex);
                    provider.start();
                    provider.rebindIfDisconnected();
                    java.util.Collections.swap(this.mProviderProxies, sourceIndex, targetIndex);
                    targetIndex++;
                }
            }
        }
        if (DEBUG) {
            android.util.Log.d(TAG, "scanPackages() targetIndex " + targetIndex);
        }
        if (targetIndex < this.mProviderProxies.size()) {
            for (int i = this.mProviderProxies.size() - 1; i >= targetIndex; i--) {
                com.android.server.tv.TvRemoteProviderProxy providerProxy2 = this.mProviderProxies.get(i);
                this.mProviderProxies.remove(providerProxy2);
                providerProxy2.stop();
            }
        }
    }

    boolean verifyServiceTrusted(android.content.pm.ServiceInfo serviceInfo) {
        if (serviceInfo.permission == null || !serviceInfo.permission.equals("android.permission.BIND_TV_REMOTE_SERVICE")) {
            android.util.Slog.w(TAG, "Ignoring atv remote provider service because it did not require the BIND_TV_REMOTE_SERVICE permission in its manifest: " + serviceInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + serviceInfo.name);
            return false;
        }
        if (!this.mUnbundledServicePackages.contains(serviceInfo.packageName)) {
            android.util.Slog.w(TAG, "Ignoring atv remote provider service because the package has not been set and/or whitelisted: " + serviceInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + serviceInfo.name);
            return false;
        }
        if (!hasNecessaryPermissions(serviceInfo.packageName)) {
            android.util.Slog.w(TAG, "Ignoring atv remote provider service because its package does not have TV_VIRTUAL_REMOTE_CONTROLLER permission: " + serviceInfo.packageName);
            return false;
        }
        return true;
    }

    private boolean hasNecessaryPermissions(java.lang.String packageName) {
        if (this.mPackageManager.checkPermission("android.permission.TV_VIRTUAL_REMOTE_CONTROLLER", packageName) == 0) {
            return true;
        }
        return false;
    }

    private int findProvider(java.lang.String packageName, java.lang.String className) {
        int count = this.mProviderProxies.size();
        for (int i = 0; i < count; i++) {
            com.android.server.tv.TvRemoteProviderProxy provider = this.mProviderProxies.get(i);
            if (provider.hasComponentName(packageName, className)) {
                return i;
            }
        }
        return -1;
    }
}
