package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public final class RemoteDisplayProviderWatcher {
    private final com.android.server.media.RemoteDisplayProviderWatcher.Callback mCallback;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final android.content.pm.PackageManager mPackageManager;
    private boolean mRunning;
    private final int mUserId;
    private static final java.lang.String TAG = "RemoteDisplayProvider";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private final java.util.ArrayList<com.android.server.media.RemoteDisplayProviderProxy> mProviders = new java.util.ArrayList<>();
    private final android.content.BroadcastReceiver mScanPackagesReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.media.RemoteDisplayProviderWatcher.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (com.android.server.media.RemoteDisplayProviderWatcher.DEBUG) {
                android.util.Slog.d(com.android.server.media.RemoteDisplayProviderWatcher.TAG, "Received package manager broadcast: " + intent);
            }
            com.android.server.media.RemoteDisplayProviderWatcher.this.scanPackages();
        }
    };
    private final java.lang.Runnable mScanPackagesRunnable = new java.lang.Runnable() { // from class: com.android.server.media.RemoteDisplayProviderWatcher.2
        @Override // java.lang.Runnable
        public void run() {
            com.android.server.media.RemoteDisplayProviderWatcher.this.scanPackages();
        }
    };

    public interface Callback {
        void addProvider(com.android.server.media.RemoteDisplayProviderProxy remoteDisplayProviderProxy);

        void removeProvider(com.android.server.media.RemoteDisplayProviderProxy remoteDisplayProviderProxy);
    }

    public RemoteDisplayProviderWatcher(android.content.Context context, com.android.server.media.RemoteDisplayProviderWatcher.Callback callback, android.os.Handler handler, int userId) {
        this.mContext = context;
        this.mCallback = callback;
        this.mHandler = handler;
        this.mUserId = userId;
        this.mPackageManager = context.getPackageManager();
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "Watcher");
        pw.println(prefix + "  mUserId=" + this.mUserId);
        pw.println(prefix + "  mRunning=" + this.mRunning);
        pw.println(prefix + "  mProviders.size()=" + this.mProviders.size());
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
            for (int i = this.mProviders.size() - 1; i >= 0; i--) {
                this.mProviders.get(i).stop();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scanPackages() {
        if (!this.mRunning) {
            return;
        }
        int targetIndex = 0;
        android.content.Intent intent = new android.content.Intent("com.android.media.remotedisplay.RemoteDisplayProvider");
        for (android.content.pm.ResolveInfo resolveInfo : this.mPackageManager.queryIntentServicesAsUser(intent, 0, this.mUserId)) {
            android.content.pm.ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (serviceInfo != null && verifyServiceTrusted(serviceInfo)) {
                int sourceIndex = findProvider(serviceInfo.packageName, serviceInfo.name);
                if (sourceIndex < 0) {
                    com.android.server.media.RemoteDisplayProviderProxy provider = new com.android.server.media.RemoteDisplayProviderProxy(this.mContext, new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name), this.mUserId);
                    provider.start();
                    this.mProviders.add(targetIndex, provider);
                    this.mCallback.addProvider(provider);
                    targetIndex++;
                } else if (sourceIndex >= targetIndex) {
                    com.android.server.media.RemoteDisplayProviderProxy provider2 = this.mProviders.get(sourceIndex);
                    provider2.start();
                    provider2.rebindIfDisconnected();
                    java.util.Collections.swap(this.mProviders, sourceIndex, targetIndex);
                    targetIndex++;
                }
            }
        }
        if (targetIndex < this.mProviders.size()) {
            for (int i = this.mProviders.size() - 1; i >= targetIndex; i--) {
                com.android.server.media.RemoteDisplayProviderProxy provider3 = this.mProviders.get(i);
                this.mCallback.removeProvider(provider3);
                this.mProviders.remove(provider3);
                provider3.stop();
            }
        }
    }

    private boolean verifyServiceTrusted(android.content.pm.ServiceInfo serviceInfo) {
        if (serviceInfo.permission == null || !serviceInfo.permission.equals("android.permission.BIND_REMOTE_DISPLAY")) {
            android.util.Slog.w(TAG, "Ignoring remote display provider service because it did not require the BIND_REMOTE_DISPLAY permission in its manifest: " + serviceInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + serviceInfo.name);
            return false;
        }
        if (this.mPackageManager.checkPermission("android.permission.REMOTE_DISPLAY_PROVIDER", serviceInfo.packageName) != 0) {
            android.util.Slog.w(TAG, "Ignoring remote display provider service because it does not have the REMOTE_DISPLAY_PROVIDER permission: " + serviceInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + serviceInfo.name);
            return false;
        }
        return true;
    }

    private int findProvider(java.lang.String packageName, java.lang.String className) {
        int count = this.mProviders.size();
        for (int i = 0; i < count; i++) {
            com.android.server.media.RemoteDisplayProviderProxy provider = this.mProviders.get(i);
            if (provider.hasComponentName(packageName, className)) {
                return i;
            }
        }
        return -1;
    }
}
