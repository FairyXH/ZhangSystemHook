package com.android.server.webkit;

/* JADX INFO: loaded from: classes3.dex */
class WebViewUpdateServiceImpl implements com.android.server.webkit.WebViewUpdateServiceInterface {
    private static final int MULTIPROCESS_SETTING_OFF_VALUE = Integer.MIN_VALUE;
    private static final int MULTIPROCESS_SETTING_ON_VALUE = Integer.MAX_VALUE;
    private static final long NS_PER_MS = 1000000;
    private static final int NUMBER_OF_RELROS_UNKNOWN = Integer.MAX_VALUE;
    private static final java.lang.String TAG = com.android.server.webkit.WebViewUpdateServiceImpl.class.getSimpleName();
    private static final int VALIDITY_INCORRECT_SDK_VERSION = 1;
    private static final int VALIDITY_INCORRECT_SIGNATURE = 3;
    private static final int VALIDITY_INCORRECT_VERSION_CODE = 2;
    private static final int VALIDITY_NO_LIBRARY_FLAG = 4;
    private static final int VALIDITY_OK = 0;
    private static final int WAIT_TIMEOUT_MS = 1000;
    private final android.content.Context mContext;
    private final com.android.server.webkit.SystemInterface mSystemInterface;
    private long mMinimumVersionCode = -1;
    private int mNumRelroCreationsStarted = 0;
    private int mNumRelroCreationsFinished = 0;
    private boolean mWebViewPackageDirty = false;
    private boolean mAnyWebViewInstalled = false;
    private android.content.pm.PackageInfo mCurrentWebViewPackage = null;
    private final java.lang.Object mLock = new java.lang.Object();

    private static class WebViewPackageMissingException extends java.lang.Exception {
        WebViewPackageMissingException(java.lang.String message) {
            super(message);
        }

        WebViewPackageMissingException(java.lang.Exception e) {
            super(e);
        }
    }

    WebViewUpdateServiceImpl(android.content.Context context, com.android.server.webkit.SystemInterface systemInterface) {
        this.mContext = context;
        this.mSystemInterface = systemInterface;
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public void packageStateChanged(java.lang.String packageName, int changedState, int userId) {
        android.content.pm.PackageInfo newPackage;
        for (android.webkit.WebViewProviderInfo provider : this.mSystemInterface.getWebViewPackages()) {
            java.lang.String webviewPackage = provider.packageName;
            if (webviewPackage.equals(packageName)) {
                boolean updateWebView = false;
                boolean removedOrChangedOldPackage = false;
                java.lang.String oldProviderName = null;
                synchronized (this.mLock) {
                    try {
                        newPackage = findPreferredWebViewPackage();
                        if (this.mCurrentWebViewPackage != null) {
                            oldProviderName = this.mCurrentWebViewPackage.packageName;
                        }
                        updateWebView = provider.packageName.equals(newPackage.packageName) || provider.packageName.equals(oldProviderName) || this.mCurrentWebViewPackage == null;
                        removedOrChangedOldPackage = provider.packageName.equals(oldProviderName);
                    } catch (com.android.server.webkit.WebViewUpdateServiceImpl.WebViewPackageMissingException e) {
                        this.mCurrentWebViewPackage = null;
                        android.util.Slog.e(TAG, "Could not find valid WebView package to create relro with " + e);
                    }
                    if (updateWebView) {
                        onWebViewProviderChanged(newPackage);
                    }
                }
                if (updateWebView && !removedOrChangedOldPackage && oldProviderName != null) {
                    this.mSystemInterface.killPackageDependents(oldProviderName);
                    return;
                }
                return;
            }
        }
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public void prepareWebViewInSystemServer() {
        this.mSystemInterface.notifyZygote(isMultiProcessEnabled());
        try {
            synchronized (this.mLock) {
                this.mCurrentWebViewPackage = findPreferredWebViewPackage();
                java.lang.String userSetting = this.mSystemInterface.getUserChosenWebViewProvider(this.mContext);
                if (userSetting != null && !userSetting.equals(this.mCurrentWebViewPackage.packageName)) {
                    this.mSystemInterface.updateUserSetting(this.mContext, this.mCurrentWebViewPackage.packageName);
                }
                onWebViewProviderChanged(this.mCurrentWebViewPackage);
            }
        } catch (com.android.server.webkit.WebViewUpdateServiceImpl.WebViewPackageMissingException e) {
            android.util.Slog.e(TAG, "Could not find valid WebView package to create relro with", e);
        } catch (java.lang.Throwable t) {
            android.util.Slog.wtf(TAG, "error preparing webview provider from system server", t);
        }
        if (getCurrentWebViewPackage() == null) {
            android.webkit.WebViewProviderInfo[] webviewProviders = this.mSystemInterface.getWebViewPackages();
            android.webkit.WebViewProviderInfo fallbackProvider = getFallbackProvider(webviewProviders);
            if (fallbackProvider != null) {
                android.util.Slog.w(TAG, "No valid provider, trying to enable " + fallbackProvider.packageName);
                this.mSystemInterface.enablePackageForAllUsers(this.mContext, fallbackProvider.packageName, true);
            } else {
                android.util.Slog.e(TAG, "No valid provider and no fallback available.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startZygoteWhenReady() {
        waitForAndGetProvider();
        this.mSystemInterface.ensureZygoteStarted();
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public void handleNewUser(int userId) {
        if (userId == 0) {
            return;
        }
        handleUserChange();
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public void handleUserRemoved(int userId) {
        handleUserChange();
    }

    private void handleUserChange() {
        updateCurrentWebViewPackage(null);
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public void notifyRelroCreationCompleted() {
        synchronized (this.mLock) {
            this.mNumRelroCreationsFinished++;
            checkIfRelrosDoneLocked();
        }
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public android.webkit.WebViewProviderResponse waitForAndGetProvider() {
        boolean webViewReady;
        android.content.pm.PackageInfo webViewPackage;
        long timeoutTimeMs = (java.lang.System.nanoTime() / NS_PER_MS) + 1000;
        int webViewStatus = 0;
        synchronized (this.mLock) {
            webViewReady = webViewIsReadyLocked();
            while (!webViewReady) {
                long timeNowMs = java.lang.System.nanoTime() / NS_PER_MS;
                if (timeNowMs >= timeoutTimeMs) {
                    break;
                }
                try {
                    this.mLock.wait(timeoutTimeMs - timeNowMs);
                } catch (java.lang.InterruptedException e) {
                }
                webViewReady = webViewIsReadyLocked();
            }
            webViewPackage = this.mCurrentWebViewPackage;
            if (!webViewReady) {
                if (!this.mAnyWebViewInstalled) {
                    webViewStatus = 4;
                } else {
                    webViewStatus = 3;
                    java.lang.String timeoutError = "Timed out waiting for relro creation, relros started " + this.mNumRelroCreationsStarted + " relros finished " + this.mNumRelroCreationsFinished + " package dirty? " + this.mWebViewPackageDirty;
                    android.util.Slog.e(TAG, timeoutError);
                    android.os.Trace.instant(64L, timeoutError);
                }
            }
        }
        if (!webViewReady) {
            android.util.Slog.w(TAG, "creating relro file timed out");
        }
        return new android.webkit.WebViewProviderResponse(webViewPackage, webViewStatus);
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public java.lang.String changeProviderAndSetting(java.lang.String newProviderName) {
        android.content.pm.PackageInfo newPackage = updateCurrentWebViewPackage(newProviderName);
        return newPackage == null ? "" : newPackage.packageName;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.content.pm.PackageInfo updateCurrentWebViewPackage(java.lang.String r10) {
        /*
            r9 = this;
            r0 = 0
            r1 = 0
            r2 = 0
            java.lang.Object r3 = r9.mLock
            monitor-enter(r3)
            android.content.pm.PackageInfo r4 = r9.mCurrentWebViewPackage     // Catch: java.lang.Throwable -> L59
            r0 = r4
            if (r10 == 0) goto L12
            com.android.server.webkit.SystemInterface r4 = r9.mSystemInterface     // Catch: java.lang.Throwable -> L59
            android.content.Context r5 = r9.mContext     // Catch: java.lang.Throwable -> L59
            r4.updateUserSetting(r5, r10)     // Catch: java.lang.Throwable -> L59
        L12:
            android.content.pm.PackageInfo r4 = r9.findPreferredWebViewPackage()     // Catch: com.android.server.webkit.WebViewUpdateServiceImpl.WebViewPackageMissingException -> L3b java.lang.Throwable -> L59
            r1 = r4
            if (r0 == 0) goto L26
            java.lang.String r4 = r1.packageName     // Catch: com.android.server.webkit.WebViewUpdateServiceImpl.WebViewPackageMissingException -> L3b java.lang.Throwable -> L59
            java.lang.String r5 = r0.packageName     // Catch: com.android.server.webkit.WebViewUpdateServiceImpl.WebViewPackageMissingException -> L3b java.lang.Throwable -> L59
            boolean r4 = r4.equals(r5)     // Catch: com.android.server.webkit.WebViewUpdateServiceImpl.WebViewPackageMissingException -> L3b java.lang.Throwable -> L59
            if (r4 != 0) goto L24
            goto L26
        L24:
            r4 = 0
            goto L27
        L26:
            r4 = 1
        L27:
            r2 = r4
            if (r2 == 0) goto L2e
            r9.onWebViewProviderChanged(r1)     // Catch: java.lang.Throwable -> L59
        L2e:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L59
            if (r2 == 0) goto L3a
            if (r0 == 0) goto L3a
            com.android.server.webkit.SystemInterface r3 = r9.mSystemInterface
            java.lang.String r4 = r0.packageName
            r3.killPackageDependents(r4)
        L3a:
            return r1
        L3b:
            r4 = move-exception
            r5 = 0
            r9.mCurrentWebViewPackage = r5     // Catch: java.lang.Throwable -> L59
            java.lang.String r6 = com.android.server.webkit.WebViewUpdateServiceImpl.TAG     // Catch: java.lang.Throwable -> L59
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L59
            r7.<init>()     // Catch: java.lang.Throwable -> L59
            java.lang.String r8 = "Couldn't find WebView package to use "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch: java.lang.Throwable -> L59
            java.lang.StringBuilder r7 = r7.append(r4)     // Catch: java.lang.Throwable -> L59
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> L59
            android.util.Slog.e(r6, r7)     // Catch: java.lang.Throwable -> L59
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L59
            return r5
        L59:
            r4 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L59
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.webkit.WebViewUpdateServiceImpl.updateCurrentWebViewPackage(java.lang.String):android.content.pm.PackageInfo");
    }

    private void onWebViewProviderChanged(android.content.pm.PackageInfo newPackage) {
        synchronized (this.mLock) {
            this.mAnyWebViewInstalled = true;
            if (this.mNumRelroCreationsStarted == this.mNumRelroCreationsFinished) {
                this.mSystemInterface.pinWebviewIfRequired(newPackage.applicationInfo);
                this.mCurrentWebViewPackage = newPackage;
                this.mNumRelroCreationsStarted = Integer.MAX_VALUE;
                this.mNumRelroCreationsFinished = 0;
                this.mNumRelroCreationsStarted = this.mSystemInterface.onWebViewProviderChanged(newPackage);
                com.android.modules.expresslog.Counter.logIncrement("webview.value_on_webview_provider_changed_counter");
                if (newPackage.packageName.equals(getDefaultWebViewPackage().packageName)) {
                    com.android.modules.expresslog.Counter.logIncrement("webview.value_on_webview_provider_changed_with_default_package_counter");
                }
                checkIfRelrosDoneLocked();
            } else {
                this.mWebViewPackageDirty = true;
            }
        }
        if (isMultiProcessEnabled()) {
            android.os.AsyncTask.THREAD_POOL_EXECUTOR.execute(new java.lang.Runnable() { // from class: com.android.server.webkit.WebViewUpdateServiceImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.startZygoteWhenReady();
                }
            });
        }
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public android.webkit.WebViewProviderInfo[] getValidWebViewPackages() {
        com.android.server.webkit.WebViewUpdateServiceImpl.ProviderAndPackageInfo[] providersAndPackageInfos = getValidWebViewPackagesAndInfos();
        android.webkit.WebViewProviderInfo[] providers = new android.webkit.WebViewProviderInfo[providersAndPackageInfos.length];
        for (int n = 0; n < providersAndPackageInfos.length; n++) {
            providers[n] = providersAndPackageInfos[n].provider;
        }
        return providers;
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public android.webkit.WebViewProviderInfo getDefaultWebViewPackage() {
        for (android.webkit.WebViewProviderInfo provider : getWebViewPackages()) {
            if (provider.availableByDefault) {
                return provider;
            }
        }
        throw new android.util.AndroidRuntimeException("No available by default WebView Provider.");
    }

    private static class ProviderAndPackageInfo {
        public final android.content.pm.PackageInfo packageInfo;
        public final android.webkit.WebViewProviderInfo provider;

        ProviderAndPackageInfo(android.webkit.WebViewProviderInfo provider, android.content.pm.PackageInfo packageInfo) {
            this.provider = provider;
            this.packageInfo = packageInfo;
        }
    }

    private com.android.server.webkit.WebViewUpdateServiceImpl.ProviderAndPackageInfo[] getValidWebViewPackagesAndInfos() {
        android.webkit.WebViewProviderInfo[] allProviders = this.mSystemInterface.getWebViewPackages();
        java.util.List<com.android.server.webkit.WebViewUpdateServiceImpl.ProviderAndPackageInfo> providers = new java.util.ArrayList<>();
        for (int n = 0; n < allProviders.length; n++) {
            try {
                android.content.pm.PackageInfo packageInfo = this.mSystemInterface.getPackageInfoForProvider(allProviders[n]);
                if (validityResult(allProviders[n], packageInfo) == 0) {
                    providers.add(new com.android.server.webkit.WebViewUpdateServiceImpl.ProviderAndPackageInfo(allProviders[n], packageInfo));
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }
        int n2 = providers.size();
        return (com.android.server.webkit.WebViewUpdateServiceImpl.ProviderAndPackageInfo[]) providers.toArray(new com.android.server.webkit.WebViewUpdateServiceImpl.ProviderAndPackageInfo[n2]);
    }

    private android.content.pm.PackageInfo findPreferredWebViewPackage() throws com.android.server.webkit.WebViewUpdateServiceImpl.WebViewPackageMissingException {
        com.android.server.webkit.WebViewUpdateServiceImpl.ProviderAndPackageInfo[] providers = getValidWebViewPackagesAndInfos();
        java.lang.String userChosenProvider = this.mSystemInterface.getUserChosenWebViewProvider(this.mContext);
        for (com.android.server.webkit.WebViewUpdateServiceImpl.ProviderAndPackageInfo providerAndPackage : providers) {
            if (providerAndPackage.provider.packageName.equals(userChosenProvider)) {
                java.util.List<android.webkit.UserPackage> userPackages = this.mSystemInterface.getPackageInfoForProviderAllUsers(this.mContext, providerAndPackage.provider);
                if (isInstalledAndEnabledForAllUsers(userPackages)) {
                    return providerAndPackage.packageInfo;
                }
            }
        }
        for (com.android.server.webkit.WebViewUpdateServiceImpl.ProviderAndPackageInfo providerAndPackage2 : providers) {
            if (providerAndPackage2.provider.availableByDefault) {
                java.util.List<android.webkit.UserPackage> userPackages2 = this.mSystemInterface.getPackageInfoForProviderAllUsers(this.mContext, providerAndPackage2.provider);
                if (isInstalledAndEnabledForAllUsers(userPackages2)) {
                    return providerAndPackage2.packageInfo;
                }
            }
        }
        this.mAnyWebViewInstalled = false;
        throw new com.android.server.webkit.WebViewUpdateServiceImpl.WebViewPackageMissingException("Could not find a loadable WebView package");
    }

    private static boolean isInstalledAndEnabledForAllUsers(java.util.List<android.webkit.UserPackage> userPackages) {
        for (android.webkit.UserPackage userPackage : userPackages) {
            if (!userPackage.isInstalledPackage() || !userPackage.isEnabledPackage()) {
                return false;
            }
        }
        return true;
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public android.webkit.WebViewProviderInfo[] getWebViewPackages() {
        return this.mSystemInterface.getWebViewPackages();
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public android.content.pm.PackageInfo getCurrentWebViewPackage() {
        android.content.pm.PackageInfo packageInfo;
        synchronized (this.mLock) {
            packageInfo = this.mCurrentWebViewPackage;
        }
        return packageInfo;
    }

    private boolean webViewIsReadyLocked() {
        return !this.mWebViewPackageDirty && this.mNumRelroCreationsStarted == this.mNumRelroCreationsFinished && this.mAnyWebViewInstalled;
    }

    private void checkIfRelrosDoneLocked() {
        if (this.mNumRelroCreationsStarted == this.mNumRelroCreationsFinished) {
            if (this.mWebViewPackageDirty) {
                this.mWebViewPackageDirty = false;
                try {
                    android.content.pm.PackageInfo newPackage = findPreferredWebViewPackage();
                    onWebViewProviderChanged(newPackage);
                    return;
                } catch (com.android.server.webkit.WebViewUpdateServiceImpl.WebViewPackageMissingException e) {
                    this.mCurrentWebViewPackage = null;
                    return;
                }
            }
            this.mLock.notifyAll();
        }
    }

    private int validityResult(android.webkit.WebViewProviderInfo configInfo, android.content.pm.PackageInfo packageInfo) {
        if (!android.webkit.UserPackage.hasCorrectTargetSdkVersion(packageInfo)) {
            return 1;
        }
        if (!versionCodeGE(packageInfo.getLongVersionCode(), getMinimumVersionCode()) && !this.mSystemInterface.systemIsDebuggable()) {
            return 2;
        }
        if (!providerHasValidSignature(configInfo, packageInfo, this.mSystemInterface)) {
            return 3;
        }
        if (android.webkit.WebViewFactory.getWebViewLibrary(packageInfo.applicationInfo) == null) {
            return 4;
        }
        return 0;
    }

    private static boolean versionCodeGE(long versionCode1, long versionCode2) {
        long v1 = versionCode1 / 100000;
        long v2 = versionCode2 / 100000;
        return v1 >= v2;
    }

    private long getMinimumVersionCode() {
        if (this.mMinimumVersionCode > 0) {
            return this.mMinimumVersionCode;
        }
        long minimumVersionCode = -1;
        for (android.webkit.WebViewProviderInfo provider : this.mSystemInterface.getWebViewPackages()) {
            if (provider.availableByDefault) {
                try {
                    long versionCode = this.mSystemInterface.getFactoryPackageVersion(provider.packageName);
                    if (minimumVersionCode < 0 || versionCode < minimumVersionCode) {
                        minimumVersionCode = versionCode;
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                }
            }
        }
        this.mMinimumVersionCode = minimumVersionCode;
        return this.mMinimumVersionCode;
    }

    private static boolean providerHasValidSignature(android.webkit.WebViewProviderInfo provider, android.content.pm.PackageInfo packageInfo, com.android.server.webkit.SystemInterface systemInterface) {
        if (systemInterface.systemIsDebuggable() || packageInfo.applicationInfo.isSystemApp()) {
            return true;
        }
        if (packageInfo.signatures.length != 1) {
            return false;
        }
        for (android.content.pm.Signature signature : provider.signatures) {
            if (signature.equals(packageInfo.signatures[0])) {
                return true;
            }
        }
        return false;
    }

    private static android.webkit.WebViewProviderInfo getFallbackProvider(android.webkit.WebViewProviderInfo[] webviewPackages) {
        for (android.webkit.WebViewProviderInfo provider : webviewPackages) {
            if (provider.isFallback) {
                return provider;
            }
        }
        return null;
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public boolean isMultiProcessEnabled() {
        int settingValue = this.mSystemInterface.getMultiProcessSetting(this.mContext);
        return this.mSystemInterface.isMultiProcessDefaultEnabled() ? settingValue > Integer.MIN_VALUE : settingValue >= Integer.MAX_VALUE;
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public void enableMultiProcess(boolean enable) {
        android.content.pm.PackageInfo current = getCurrentWebViewPackage();
        this.mSystemInterface.setMultiProcessSetting(this.mContext, enable ? Integer.MAX_VALUE : Integer.MIN_VALUE);
        this.mSystemInterface.notifyZygote(enable);
        if (current != null) {
            this.mSystemInterface.killPackageDependents(current.packageName);
        }
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public void dumpState(java.io.PrintWriter pw) {
        pw.println("Current WebView Update Service state");
        pw.println(java.lang.String.format("  Multiprocess enabled: %b", java.lang.Boolean.valueOf(isMultiProcessEnabled())));
        synchronized (this.mLock) {
            if (this.mCurrentWebViewPackage == null) {
                pw.println("  Current WebView package is null");
            } else {
                pw.println(java.lang.String.format("  Current WebView package (name, version): (%s, %s)", this.mCurrentWebViewPackage.packageName, this.mCurrentWebViewPackage.versionName));
            }
            pw.println(java.lang.String.format("  Minimum targetSdkVersion: %d", 33));
            pw.println(java.lang.String.format("  Minimum WebView version code: %d", java.lang.Long.valueOf(this.mMinimumVersionCode)));
            pw.println(java.lang.String.format("  Number of relros started: %d", java.lang.Integer.valueOf(this.mNumRelroCreationsStarted)));
            pw.println(java.lang.String.format("  Number of relros finished: %d", java.lang.Integer.valueOf(this.mNumRelroCreationsFinished)));
            pw.println(java.lang.String.format("  WebView package dirty: %b", java.lang.Boolean.valueOf(this.mWebViewPackageDirty)));
            pw.println(java.lang.String.format("  Any WebView package installed: %b", java.lang.Boolean.valueOf(this.mAnyWebViewInstalled)));
            try {
                android.content.pm.PackageInfo preferredWebViewPackage = findPreferredWebViewPackage();
                pw.println(java.lang.String.format("  Preferred WebView package (name, version): (%s, %s)", preferredWebViewPackage.packageName, preferredWebViewPackage.versionName));
            } catch (com.android.server.webkit.WebViewUpdateServiceImpl.WebViewPackageMissingException e) {
                pw.println(java.lang.String.format("  Preferred WebView package: none", new java.lang.Object[0]));
            }
            dumpAllPackageInformationLocked(pw);
        }
    }

    private void dumpAllPackageInformationLocked(java.io.PrintWriter pw) {
        android.webkit.WebViewProviderInfo[] allProviders = this.mSystemInterface.getWebViewPackages();
        pw.println("  WebView packages:");
        for (android.webkit.WebViewProviderInfo provider : allProviders) {
            java.util.List<android.webkit.UserPackage> userPackages = this.mSystemInterface.getPackageInfoForProviderAllUsers(this.mContext, provider);
            android.content.pm.PackageInfo systemUserPackageInfo = userPackages.get(0).getPackageInfo();
            if (systemUserPackageInfo == null) {
                pw.println(java.lang.String.format("    %s is NOT installed.", provider.packageName));
            } else {
                int validity = validityResult(provider, systemUserPackageInfo);
                java.lang.String packageDetails = java.lang.String.format("versionName: %s, versionCode: %d, targetSdkVersion: %d", systemUserPackageInfo.versionName, java.lang.Long.valueOf(systemUserPackageInfo.getLongVersionCode()), java.lang.Integer.valueOf(systemUserPackageInfo.applicationInfo.targetSdkVersion));
                if (validity == 0) {
                    boolean installedForAllUsers = isInstalledAndEnabledForAllUsers(this.mSystemInterface.getPackageInfoForProviderAllUsers(this.mContext, provider));
                    pw.println(java.lang.String.format("    Valid package %s (%s) is %s installed/enabled for all users", systemUserPackageInfo.packageName, packageDetails, installedForAllUsers ? "" : "NOT"));
                } else {
                    pw.println(java.lang.String.format("    Invalid package %s (%s), reason: %s", systemUserPackageInfo.packageName, packageDetails, getInvalidityReason(validity)));
                }
            }
        }
    }

    private static java.lang.String getInvalidityReason(int invalidityReason) {
        switch (invalidityReason) {
            case 1:
                return "SDK version too low";
            case 2:
                return "Version code too low";
            case 3:
                return "Incorrect signature";
            case 4:
                return "No WebView-library manifest flag";
            default:
                return "Unexcepted validity-reason";
        }
    }
}
