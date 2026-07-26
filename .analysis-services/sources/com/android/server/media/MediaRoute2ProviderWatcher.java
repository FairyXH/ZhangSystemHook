package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
final class MediaRoute2ProviderWatcher {
    private final com.android.server.media.MediaRoute2ProviderWatcher.Callback mCallback;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final android.content.pm.PackageManager mPackageManager;
    private boolean mRunning;
    private final int mUserId;
    private static final java.lang.String TAG = "MR2ProviderWatcher";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final android.content.pm.PackageManager.ResolveInfoFlags RESOLVE_INFO_FLAGS = android.content.pm.PackageManager.ResolveInfoFlags.of(64);
    private final java.util.ArrayList<com.android.server.media.MediaRoute2ProviderServiceProxy> mProxies = new java.util.ArrayList<>();
    private final android.content.BroadcastReceiver mScanPackagesReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.media.MediaRoute2ProviderWatcher.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (com.android.server.media.MediaRoute2ProviderWatcher.DEBUG) {
                android.util.Slog.d(com.android.server.media.MediaRoute2ProviderWatcher.TAG, "Received package manager broadcast: " + intent);
            }
            com.android.server.media.MediaRoute2ProviderWatcher.this.postScanPackagesIfNeeded();
        }
    };

    public interface Callback {
        void onAddProviderService(com.android.server.media.MediaRoute2ProviderServiceProxy mediaRoute2ProviderServiceProxy);

        void onRemoveProviderService(com.android.server.media.MediaRoute2ProviderServiceProxy mediaRoute2ProviderServiceProxy);
    }

    MediaRoute2ProviderWatcher(android.content.Context context, com.android.server.media.MediaRoute2ProviderWatcher.Callback callback, android.os.Handler handler, int userId) {
        this.mContext = context;
        this.mCallback = callback;
        this.mHandler = handler;
        this.mUserId = userId;
        this.mPackageManager = context.getPackageManager();
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "MediaRoute2ProviderWatcher");
        java.lang.String prefix2 = prefix + "  ";
        if (this.mProxies.isEmpty()) {
            pw.println(prefix2 + "<no provider service proxies>");
            return;
        }
        for (com.android.server.media.MediaRoute2ProviderServiceProxy proxy : this.mProxies) {
            proxy.dump(pw, prefix2);
        }
    }

    public void start() {
        if (!this.mRunning) {
            this.mRunning = true;
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.PACKAGE_ADDED");
            filter.addAction("android.intent.action.PACKAGE_REMOVED");
            filter.addAction("android.intent.action.PACKAGE_CHANGED");
            filter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=IGNORE_WM_COMP");
            filter.addAction("android.intent.action.PACKAGE_REPLACED");
            if (!com.android.media.flags.Flags.enablePreventionOfKeepAliveRouteProviders()) {
                filter.addAction("android.intent.action.PACKAGE_RESTARTED");
            }
            filter.addDataScheme("package");
            this.mContext.registerReceiverAsUser(this.mScanPackagesReceiver, new android.os.UserHandle(this.mUserId), filter, null, this.mHandler);
            postScanPackagesIfNeeded();
        }
    }

    public void stop() {
        if (this.mRunning) {
            this.mRunning = false;
            this.mContext.unregisterReceiver(this.mScanPackagesReceiver);
            this.mHandler.removeCallbacks(new com.android.server.media.MediaRoute2ProviderWatcher$$ExternalSyntheticLambda0(this));
            for (int i = this.mProxies.size() - 1; i >= 0; i--) {
                this.mProxies.get(i).stop();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scanPackages() {
        if (!this.mRunning) {
            return;
        }
        int targetIndex = 0;
        android.content.Intent intent = new android.content.Intent("android.media.MediaRoute2ProviderService");
        for (android.content.pm.ResolveInfo resolveInfo : this.mPackageManager.queryIntentServicesAsUser(intent, RESOLVE_INFO_FLAGS, this.mUserId)) {
            android.content.pm.ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (serviceInfo != null) {
                boolean isSelfScanOnlyProvider = false;
                java.util.Iterator<java.lang.String> categoriesIterator = resolveInfo.filter.categoriesIterator();
                if (categoriesIterator != null) {
                    while (categoriesIterator.hasNext()) {
                        isSelfScanOnlyProvider |= "android.media.MediaRoute2ProviderService.SELF_SCAN_ONLY".equals(categoriesIterator.next());
                    }
                }
                int sourceIndex = findProvider(serviceInfo.packageName, serviceInfo.name);
                if (sourceIndex < 0) {
                    com.android.server.media.MediaRoute2ProviderServiceProxy proxy = new com.android.server.media.MediaRoute2ProviderServiceProxy(this.mContext, this.mHandler.getLooper(), new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name), isSelfScanOnlyProvider, this.mUserId);
                    android.util.Slog.i(TAG, "Enabling proxy for MediaRoute2ProviderService: " + proxy.mComponentName);
                    proxy.start(false);
                    this.mProxies.add(targetIndex, proxy);
                    this.mCallback.onAddProviderService(proxy);
                    targetIndex++;
                } else if (sourceIndex >= targetIndex) {
                    this.mProxies.get(sourceIndex).start(!com.android.media.flags.Flags.enablePreventionOfKeepAliveRouteProviders());
                    java.util.Collections.swap(this.mProxies, sourceIndex, targetIndex);
                    targetIndex++;
                }
            }
        }
        if (targetIndex < this.mProxies.size()) {
            for (int i = this.mProxies.size() - 1; i >= targetIndex; i--) {
                com.android.server.media.MediaRoute2ProviderServiceProxy proxy2 = this.mProxies.get(i);
                android.util.Slog.i(TAG, "Disabling proxy for MediaRoute2ProviderService: " + proxy2.mComponentName);
                this.mCallback.onRemoveProviderService(proxy2);
                this.mProxies.remove(proxy2);
                proxy2.stop();
            }
        }
    }

    private int findProvider(java.lang.String packageName, java.lang.String className) {
        int count = this.mProxies.size();
        for (int i = 0; i < count; i++) {
            com.android.server.media.MediaRoute2ProviderServiceProxy proxy = this.mProxies.get(i);
            if (proxy.hasComponentName(packageName, className)) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postScanPackagesIfNeeded() {
        if (!this.mHandler.hasCallbacks(new com.android.server.media.MediaRoute2ProviderWatcher$$ExternalSyntheticLambda0(this))) {
            this.mHandler.post(new com.android.server.media.MediaRoute2ProviderWatcher$$ExternalSyntheticLambda0(this));
        }
    }
}
