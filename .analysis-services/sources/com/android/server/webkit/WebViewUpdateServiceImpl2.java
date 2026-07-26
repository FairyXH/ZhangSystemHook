package com.android.server.webkit;

/* JADX INFO: loaded from: classes3.dex */
class WebViewUpdateServiceImpl2 implements com.android.server.webkit.WebViewUpdateServiceInterface {
    private static final long NS_PER_MS = 1000000;
    private static final int NUMBER_OF_RELROS_UNKNOWN = Integer.MAX_VALUE;
    private static final java.lang.String TAG = com.android.server.webkit.WebViewUpdateServiceImpl2.class.getSimpleName();
    private static final int VALIDITY_INCORRECT_SDK_VERSION = 1;
    private static final int VALIDITY_INCORRECT_SIGNATURE = 3;
    private static final int VALIDITY_INCORRECT_VERSION_CODE = 2;
    private static final int VALIDITY_NO_LIBRARY_FLAG = 4;
    private static final int VALIDITY_OK = 0;
    private static final int WAIT_TIMEOUT_MS = 1000;
    private final android.content.Context mContext;
    private final android.webkit.WebViewProviderInfo mDefaultProvider;
    private final com.android.server.webkit.SystemInterface mSystemInterface;
    private long mMinimumVersionCode = -1;
    private int mNumRelroCreationsStarted = 0;
    private int mNumRelroCreationsFinished = 0;
    private boolean mWebViewPackageDirty = false;
    private boolean mAnyWebViewInstalled = false;
    private boolean mAttemptedToRepairBefore = false;
    private android.content.pm.PackageInfo mCurrentWebViewPackage = null;
    private final java.lang.Object mLock = new java.lang.Object();

    private static class WebViewPackageMissingException extends java.lang.Exception {
        WebViewPackageMissingException(java.lang.String message) {
            super(message);
        }
    }

    WebViewUpdateServiceImpl2(android.content.Context context, com.android.server.webkit.SystemInterface systemInterface) {
        int i = 0;
        this.mContext = context;
        this.mSystemInterface = systemInterface;
        android.webkit.WebViewProviderInfo[] webviewProviders = getWebViewPackages();
        android.webkit.WebViewProviderInfo defaultProvider = null;
        int length = webviewProviders.length;
        while (true) {
            if (i >= length) {
                break;
            }
            android.webkit.WebViewProviderInfo provider = webviewProviders[i];
            if (!provider.availableByDefault) {
                i++;
            } else {
                defaultProvider = provider;
                break;
            }
        }
        if (defaultProvider == null) {
            throw new android.util.AndroidRuntimeException("No available by default WebView Provider.");
        }
        this.mDefaultProvider = defaultProvider;
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public void packageStateChanged(java.lang.String packageName, int changedState, int userId) {
        boolean repairNeeded;
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
                    } catch (com.android.server.webkit.WebViewUpdateServiceImpl2.WebViewPackageMissingException e) {
                        this.mCurrentWebViewPackage = null;
                        android.util.Slog.e(TAG, "Could not find valid WebView package to create relro with " + e);
                    }
                    if (updateWebView) {
                        onWebViewProviderChanged(newPackage);
                        repairNeeded = shouldTriggerRepairLocked();
                    } else {
                        repairNeeded = shouldTriggerRepairLocked();
                    }
                }
                if (updateWebView && !removedOrChangedOldPackage && oldProviderName != null) {
                    this.mSystemInterface.killPackageDependents(oldProviderName);
                }
                if (repairNeeded) {
                    attemptRepair();
                    return;
                }
                return;
            }
        }
    }

    private boolean shouldTriggerRepairLocked() {
        if (this.mAttemptedToRepairBefore) {
            return false;
        }
        if (this.mCurrentWebViewPackage == null) {
            return true;
        }
        if (!this.mCurrentWebViewPackage.packageName.equals(this.mDefaultProvider.packageName)) {
            return false;
        }
        java.util.List<android.webkit.UserPackage> userPackages = this.mSystemInterface.getPackageInfoForProviderAllUsers(this.mContext, this.mDefaultProvider);
        return !isInstalledAndEnabledForAllUsers(userPackages);
    }

    private void attemptRepair() {
        synchronized (this.mLock) {
            if (this.mAttemptedToRepairBefore) {
                return;
            }
            this.mAttemptedToRepairBefore = true;
            android.util.Slog.w(TAG, "No provider available for all users, trying to install and enable " + this.mDefaultProvider.packageName);
            this.mSystemInterface.installExistingPackageForAllUsers(this.mContext, this.mDefaultProvider.packageName);
            this.mSystemInterface.enablePackageForAllUsers(this.mContext, this.mDefaultProvider.packageName, true);
        }
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public void prepareWebViewInSystemServer() {
        boolean repairNeeded;
        try {
            synchronized (this.mLock) {
                this.mCurrentWebViewPackage = findPreferredWebViewPackage();
                repairNeeded = shouldTriggerRepairLocked();
                java.lang.String userSetting = this.mSystemInterface.getUserChosenWebViewProvider(this.mContext);
                if (userSetting != null && !userSetting.equals(this.mCurrentWebViewPackage.packageName)) {
                    this.mSystemInterface.updateUserSetting(this.mContext, this.mCurrentWebViewPackage.packageName);
                }
                onWebViewProviderChanged(this.mCurrentWebViewPackage);
            }
            if (repairNeeded) {
                attemptRepair();
            }
        } catch (com.android.server.webkit.WebViewUpdateServiceImpl2.WebViewPackageMissingException e) {
            android.util.Slog.e(TAG, "Could not find valid WebView package to create relro with", e);
        } catch (java.lang.Throwable t) {
            android.util.Slog.wtf(TAG, "error preparing webview provider from system server", t);
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

    /* JADX WARN: Removed duplicated region for block: B:13:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.content.pm.PackageInfo updateCurrentWebViewPackage(java.lang.String r11) {
        /*
            r10 = this;
            r0 = 0
            r1 = 0
            r2 = 0
            r3 = 0
            java.lang.Object r4 = r10.mLock
            monitor-enter(r4)
            android.content.pm.PackageInfo r5 = r10.mCurrentWebViewPackage     // Catch: java.lang.Throwable -> L66
            r0 = r5
            if (r11 == 0) goto L13
            com.android.server.webkit.SystemInterface r5 = r10.mSystemInterface     // Catch: java.lang.Throwable -> L66
            android.content.Context r6 = r10.mContext     // Catch: java.lang.Throwable -> L66
            r5.updateUserSetting(r6, r11)     // Catch: java.lang.Throwable -> L66
        L13:
            android.content.pm.PackageInfo r5 = r10.findPreferredWebViewPackage()     // Catch: com.android.server.webkit.WebViewUpdateServiceImpl2.WebViewPackageMissingException -> L48 java.lang.Throwable -> L66
            r1 = r5
            if (r0 == 0) goto L27
            java.lang.String r5 = r1.packageName     // Catch: com.android.server.webkit.WebViewUpdateServiceImpl2.WebViewPackageMissingException -> L48 java.lang.Throwable -> L66
            java.lang.String r6 = r0.packageName     // Catch: com.android.server.webkit.WebViewUpdateServiceImpl2.WebViewPackageMissingException -> L48 java.lang.Throwable -> L66
            boolean r5 = r5.equals(r6)     // Catch: com.android.server.webkit.WebViewUpdateServiceImpl2.WebViewPackageMissingException -> L48 java.lang.Throwable -> L66
            if (r5 != 0) goto L25
            goto L27
        L25:
            r5 = 0
            goto L28
        L27:
            r5 = 1
        L28:
            r2 = r5
            if (r2 == 0) goto L2f
            r10.onWebViewProviderChanged(r1)     // Catch: java.lang.Throwable -> L66
        L2f:
            if (r11 != 0) goto L36
            boolean r5 = r10.shouldTriggerRepairLocked()     // Catch: java.lang.Throwable -> L66
            r3 = r5
        L36:
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L66
            if (r2 == 0) goto L42
            if (r0 == 0) goto L42
            com.android.server.webkit.SystemInterface r4 = r10.mSystemInterface
            java.lang.String r5 = r0.packageName
            r4.killPackageDependents(r5)
        L42:
            if (r3 == 0) goto L47
            r10.attemptRepair()
        L47:
            return r1
        L48:
            r5 = move-exception
            r6 = 0
            r10.mCurrentWebViewPackage = r6     // Catch: java.lang.Throwable -> L66
            java.lang.String r7 = com.android.server.webkit.WebViewUpdateServiceImpl2.TAG     // Catch: java.lang.Throwable -> L66
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L66
            r8.<init>()     // Catch: java.lang.Throwable -> L66
            java.lang.String r9 = "Couldn't find WebView package to use "
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch: java.lang.Throwable -> L66
            java.lang.StringBuilder r8 = r8.append(r5)     // Catch: java.lang.Throwable -> L66
            java.lang.String r8 = r8.toString()     // Catch: java.lang.Throwable -> L66
            android.util.Slog.e(r7, r8)     // Catch: java.lang.Throwable -> L66
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L66
            return r6
        L66:
            r5 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L66
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.webkit.WebViewUpdateServiceImpl2.updateCurrentWebViewPackage(java.lang.String):android.content.pm.PackageInfo");
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
        android.os.AsyncTask.THREAD_POOL_EXECUTOR.execute(new java.lang.Runnable() { // from class: com.android.server.webkit.WebViewUpdateServiceImpl2$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.startZygoteWhenReady();
            }
        });
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public android.webkit.WebViewProviderInfo[] getValidWebViewPackages() {
        com.android.server.webkit.WebViewUpdateServiceImpl2.ProviderAndPackageInfo[] providersAndPackageInfos = getValidWebViewPackagesAndInfos();
        android.webkit.WebViewProviderInfo[] providers = new android.webkit.WebViewProviderInfo[providersAndPackageInfos.length];
        for (int n = 0; n < providersAndPackageInfos.length; n++) {
            providers[n] = providersAndPackageInfos[n].provider;
        }
        return providers;
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public android.webkit.WebViewProviderInfo getDefaultWebViewPackage() {
        return this.mDefaultProvider;
    }

    private static class ProviderAndPackageInfo {
        public final android.content.pm.PackageInfo packageInfo;
        public final android.webkit.WebViewProviderInfo provider;

        ProviderAndPackageInfo(android.webkit.WebViewProviderInfo provider, android.content.pm.PackageInfo packageInfo) {
            this.provider = provider;
            this.packageInfo = packageInfo;
        }
    }

    private com.android.server.webkit.WebViewUpdateServiceImpl2.ProviderAndPackageInfo[] getValidWebViewPackagesAndInfos() {
        android.webkit.WebViewProviderInfo[] allProviders = this.mSystemInterface.getWebViewPackages();
        java.util.List<com.android.server.webkit.WebViewUpdateServiceImpl2.ProviderAndPackageInfo> providers = new java.util.ArrayList<>();
        for (int n = 0; n < allProviders.length; n++) {
            try {
                android.content.pm.PackageInfo packageInfo = this.mSystemInterface.getPackageInfoForProvider(allProviders[n]);
                if (validityResult(allProviders[n], packageInfo) == 0) {
                    providers.add(new com.android.server.webkit.WebViewUpdateServiceImpl2.ProviderAndPackageInfo(allProviders[n], packageInfo));
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }
        int n2 = providers.size();
        return (com.android.server.webkit.WebViewUpdateServiceImpl2.ProviderAndPackageInfo[]) providers.toArray(new com.android.server.webkit.WebViewUpdateServiceImpl2.ProviderAndPackageInfo[n2]);
    }

    private android.content.pm.PackageInfo findPreferredWebViewPackage() throws com.android.server.webkit.WebViewUpdateServiceImpl2.WebViewPackageMissingException {
        com.android.modules.expresslog.Counter.logIncrement("webview.value_find_preferred_webview_package_counter");
        java.lang.String userChosenPackageName = this.mSystemInterface.getUserChosenWebViewProvider(this.mContext);
        android.webkit.WebViewProviderInfo userChosenProvider = getWebViewProviderForPackage(userChosenPackageName);
        if (userChosenProvider != null) {
            try {
                android.content.pm.PackageInfo packageInfo = this.mSystemInterface.getPackageInfoForProvider(userChosenProvider);
                if (validityResult(userChosenProvider, packageInfo) == 0) {
                    java.util.List<android.webkit.UserPackage> userPackages = this.mSystemInterface.getPackageInfoForProviderAllUsers(this.mContext, userChosenProvider);
                    if (isInstalledAndEnabledForAllUsers(userPackages)) {
                        return packageInfo;
                    }
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.w(TAG, "User chosen WebView package (" + userChosenPackageName + ") not found");
            }
        }
        try {
            android.content.pm.PackageInfo packageInfo2 = this.mSystemInterface.getPackageInfoForProvider(this.mDefaultProvider);
            if (validityResult(this.mDefaultProvider, packageInfo2) == 0) {
                return packageInfo2;
            }
            com.android.modules.expresslog.Counter.logIncrement("webview.value_default_webview_package_invalid_counter");
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            android.util.Slog.w(TAG, "Default WebView package (" + this.mDefaultProvider.packageName + ") not found");
        }
        com.android.modules.expresslog.Counter.logIncrement("webview.value_webview_not_usable_for_all_users_counter");
        this.mAnyWebViewInstalled = false;
        throw new com.android.server.webkit.WebViewUpdateServiceImpl2.WebViewPackageMissingException("Could not find a loadable WebView package");
    }

    private android.webkit.WebViewProviderInfo getWebViewProviderForPackage(java.lang.String packageName) {
        android.webkit.WebViewProviderInfo[] allProviders = getWebViewPackages();
        for (int n = 0; n < allProviders.length; n++) {
            if (allProviders[n].packageName.equals(packageName)) {
                return allProviders[n];
            }
        }
        return null;
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
                } catch (com.android.server.webkit.WebViewUpdateServiceImpl2.WebViewPackageMissingException e) {
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
        throw new java.lang.IllegalStateException("isMultiProcessEnabled shouldn't be called if update_service_v2 flag is set.");
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public void enableMultiProcess(boolean enable) {
        throw new java.lang.IllegalStateException("enableMultiProcess shouldn't be called if update_service_v2 flag is set.");
    }

    @Override // com.android.server.webkit.WebViewUpdateServiceInterface
    public void dumpState(java.io.PrintWriter pw) {
        pw.println("Current WebView Update Service state");
        synchronized (this.mLock) {
            if (this.mCurrentWebViewPackage == null) {
                pw.println("  Current WebView package is null");
            } else {
                pw.println(android.text.TextUtils.formatSimple("  Current WebView package (name, version): (%s, %s)", new java.lang.Object[]{this.mCurrentWebViewPackage.packageName, this.mCurrentWebViewPackage.versionName}));
            }
            pw.println(android.text.TextUtils.formatSimple("  Minimum targetSdkVersion: %d", new java.lang.Object[]{33}));
            pw.println(android.text.TextUtils.formatSimple("  Minimum WebView version code: %d", new java.lang.Object[]{java.lang.Long.valueOf(this.mMinimumVersionCode)}));
            pw.println(android.text.TextUtils.formatSimple("  Number of relros started: %d", new java.lang.Object[]{java.lang.Integer.valueOf(this.mNumRelroCreationsStarted)}));
            pw.println(android.text.TextUtils.formatSimple("  Number of relros finished: %d", new java.lang.Object[]{java.lang.Integer.valueOf(this.mNumRelroCreationsFinished)}));
            pw.println(android.text.TextUtils.formatSimple("  WebView package dirty: %b", new java.lang.Object[]{java.lang.Boolean.valueOf(this.mWebViewPackageDirty)}));
            pw.println(android.text.TextUtils.formatSimple("  Any WebView package installed: %b", new java.lang.Object[]{java.lang.Boolean.valueOf(this.mAnyWebViewInstalled)}));
            try {
                android.content.pm.PackageInfo preferredWebViewPackage = findPreferredWebViewPackage();
                pw.println(android.text.TextUtils.formatSimple("  Preferred WebView package (name, version): (%s, %s)", new java.lang.Object[]{preferredWebViewPackage.packageName, preferredWebViewPackage.versionName}));
            } catch (com.android.server.webkit.WebViewUpdateServiceImpl2.WebViewPackageMissingException e) {
                pw.println("  Preferred WebView package: none");
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
                pw.println(android.text.TextUtils.formatSimple("    %s is NOT installed.", new java.lang.Object[]{provider.packageName}));
            } else {
                int validity = validityResult(provider, systemUserPackageInfo);
                java.lang.String packageDetails = android.text.TextUtils.formatSimple("versionName: %s, versionCode: %d, targetSdkVersion: %d", new java.lang.Object[]{systemUserPackageInfo.versionName, java.lang.Long.valueOf(systemUserPackageInfo.getLongVersionCode()), java.lang.Integer.valueOf(systemUserPackageInfo.applicationInfo.targetSdkVersion)});
                if (validity == 0) {
                    boolean installedForAllUsers = isInstalledAndEnabledForAllUsers(this.mSystemInterface.getPackageInfoForProviderAllUsers(this.mContext, provider));
                    pw.println(android.text.TextUtils.formatSimple("    Valid package %s (%s) is %s installed/enabled for all users", new java.lang.Object[]{systemUserPackageInfo.packageName, packageDetails, installedForAllUsers ? "" : "NOT"}));
                } else {
                    pw.println(android.text.TextUtils.formatSimple("    Invalid package %s (%s), reason: %s", new java.lang.Object[]{systemUserPackageInfo.packageName, packageDetails, getInvalidityReason(validity)}));
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
