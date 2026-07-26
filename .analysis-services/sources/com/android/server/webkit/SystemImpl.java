package com.android.server.webkit;

/* JADX INFO: loaded from: classes3.dex */
public class SystemImpl implements com.android.server.webkit.SystemInterface {
    private static final int PACKAGE_FLAGS = 272630976;
    private static final java.lang.String PIN_GROUP = "webview";
    private static final java.lang.String TAG = com.android.server.webkit.SystemImpl.class.getSimpleName();
    private static final java.lang.String TAG_AVAILABILITY = "availableByDefault";
    private static final java.lang.String TAG_DESCRIPTION = "description";
    private static final java.lang.String TAG_FALLBACK = "isFallback";
    private static final java.lang.String TAG_PACKAGE_NAME = "packageName";
    private static final java.lang.String TAG_SIGNATURE = "signature";
    private static final java.lang.String TAG_START = "webviewproviders";
    private static final java.lang.String TAG_WEBVIEW_PROVIDER = "webviewprovider";
    private final android.webkit.WebViewProviderInfo[] mWebViewProviderPackages;

    private static class LazyHolder {
        private static final com.android.server.webkit.SystemImpl INSTANCE = new com.android.server.webkit.SystemImpl();

        private LazyHolder() {
        }
    }

    public static com.android.server.webkit.SystemImpl getInstance() {
        return com.android.server.webkit.SystemImpl.LazyHolder.INSTANCE;
    }

    private SystemImpl() {
        int numFallbackPackages = 0;
        int numAvailableByDefaultPackages = 0;
        android.content.res.XmlResourceParser parser = null;
        java.util.List<android.webkit.WebViewProviderInfo> webViewProviders = new java.util.ArrayList<>();
        try {
            try {
                parser = android.app.AppGlobals.getInitialApplication().getResources().getXml(android.R.xml.config_webview_packages);
                com.android.internal.util.XmlUtils.beginDocument(parser, TAG_START);
                while (true) {
                    com.android.internal.util.XmlUtils.nextElement(parser);
                    java.lang.String element = parser.getName();
                    if (element != null) {
                        if (element.equals(TAG_WEBVIEW_PROVIDER)) {
                            java.lang.String packageName = parser.getAttributeValue(null, "packageName");
                            if (packageName == null) {
                                throw new android.util.AndroidRuntimeException("WebView provider in framework resources missing package name");
                            }
                            java.lang.String description = parser.getAttributeValue(null, TAG_DESCRIPTION);
                            if (description == null) {
                                throw new android.util.AndroidRuntimeException("WebView provider in framework resources missing description");
                            }
                            boolean availableByDefault = "true".equals(parser.getAttributeValue(null, TAG_AVAILABILITY));
                            boolean isFallback = "true".equals(parser.getAttributeValue(null, TAG_FALLBACK));
                            android.webkit.WebViewProviderInfo currentProvider = new android.webkit.WebViewProviderInfo(packageName, description, availableByDefault, isFallback, readSignatures(parser));
                            if (currentProvider.isFallback) {
                                numFallbackPackages++;
                                if (!currentProvider.availableByDefault) {
                                    throw new android.util.AndroidRuntimeException("Each WebView fallback package must be available by default.");
                                }
                                if (numFallbackPackages > 1) {
                                    throw new android.util.AndroidRuntimeException("There can be at most one WebView fallback package.");
                                }
                            }
                            numAvailableByDefaultPackages = currentProvider.availableByDefault ? numAvailableByDefaultPackages + 1 : numAvailableByDefaultPackages;
                            webViewProviders.add(currentProvider);
                        } else {
                            android.util.Log.e(TAG, "Found an element that is not a WebView provider");
                        }
                    } else {
                        if (numAvailableByDefaultPackages == 0) {
                            throw new android.util.AndroidRuntimeException("There must be at least one WebView package that is available by default");
                        }
                        this.mWebViewProviderPackages = (android.webkit.WebViewProviderInfo[]) webViewProviders.toArray(new android.webkit.WebViewProviderInfo[webViewProviders.size()]);
                        return;
                    }
                }
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                throw new android.util.AndroidRuntimeException("Error when parsing WebView config " + e);
            }
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    @Override // com.android.server.webkit.SystemInterface
    public android.webkit.WebViewProviderInfo[] getWebViewPackages() {
        return this.mWebViewProviderPackages;
    }

    @Override // com.android.server.webkit.SystemInterface
    public long getFactoryPackageVersion(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        android.content.pm.PackageManager pm = android.app.AppGlobals.getInitialApplication().getPackageManager();
        return pm.getPackageInfo(packageName, 2097152).getLongVersionCode();
    }

    private static java.lang.String[] readSignatures(android.content.res.XmlResourceParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.util.List<java.lang.String> signatures = new java.util.ArrayList<>();
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals(TAG_SIGNATURE)) {
                java.lang.String signature = parser.nextText();
                signatures.add(signature);
            } else {
                android.util.Log.e(TAG, "Found an element in a webview provider that is not a signature");
            }
        }
        return (java.lang.String[]) signatures.toArray(new java.lang.String[signatures.size()]);
    }

    @Override // com.android.server.webkit.SystemInterface
    public int onWebViewProviderChanged(android.content.pm.PackageInfo packageInfo) {
        return android.webkit.WebViewFactory.onWebViewProviderChanged(packageInfo);
    }

    @Override // com.android.server.webkit.SystemInterface
    public java.lang.String getUserChosenWebViewProvider(android.content.Context context) {
        return android.provider.Settings.Global.getString(context.getContentResolver(), "webview_provider");
    }

    @Override // com.android.server.webkit.SystemInterface
    public void updateUserSetting(android.content.Context context, java.lang.String newProviderName) {
        android.provider.Settings.Global.putString(context.getContentResolver(), "webview_provider", newProviderName == null ? "" : newProviderName);
    }

    @Override // com.android.server.webkit.SystemInterface
    public void killPackageDependents(java.lang.String packageName) {
        try {
            android.app.ActivityManager.getService().killPackageDependents(packageName, -1);
        } catch (android.os.RemoteException e) {
            android.util.Slog.wtf(TAG, "failed to call killPackageDependents for " + packageName, e);
        }
    }

    @Override // com.android.server.webkit.SystemInterface
    public void enablePackageForAllUsers(android.content.Context context, java.lang.String packageName, boolean enable) {
        android.os.UserManager userManager = (android.os.UserManager) context.getSystemService("user");
        for (android.content.pm.UserInfo userInfo : userManager.getUsers()) {
            enablePackageForUser(packageName, enable, userInfo.id);
        }
    }

    private void enablePackageForUser(java.lang.String packageName, boolean enable, int userId) {
        try {
            android.app.AppGlobals.getPackageManager().setApplicationEnabledSetting(packageName, enable ? 0 : 3, 0, userId, (java.lang.String) null);
        } catch (android.os.RemoteException | java.lang.IllegalArgumentException e) {
            android.util.Log.w(TAG, "Tried to " + (enable ? "enable " : "disable ") + packageName + " for user " + userId + ": " + e);
        }
    }

    @Override // com.android.server.webkit.SystemInterface
    public void installExistingPackageForAllUsers(android.content.Context context, java.lang.String packageName) {
        android.os.UserManager userManager = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        for (android.content.pm.UserInfo userInfo : userManager.getUsers()) {
            installPackageForUser(packageName, userInfo.id);
        }
    }

    private void installPackageForUser(java.lang.String packageName, int userId) {
        android.content.Context context = android.app.AppGlobals.getInitialApplication();
        android.content.Context contextAsUser = context.createContextAsUser(android.os.UserHandle.of(userId), 0);
        android.content.pm.PackageInstaller installer = contextAsUser.getPackageManager().getPackageInstaller();
        installer.installExistingPackage(packageName, 0, null);
    }

    @Override // com.android.server.webkit.SystemInterface
    public boolean systemIsDebuggable() {
        return android.os.Build.IS_DEBUGGABLE;
    }

    @Override // com.android.server.webkit.SystemInterface
    public android.content.pm.PackageInfo getPackageInfoForProvider(android.webkit.WebViewProviderInfo configInfo) throws android.content.pm.PackageManager.NameNotFoundException {
        android.content.pm.PackageManager pm = android.app.AppGlobals.getInitialApplication().getPackageManager();
        return pm.getPackageInfo(configInfo.packageName, PACKAGE_FLAGS);
    }

    @Override // com.android.server.webkit.SystemInterface
    public java.util.List<android.webkit.UserPackage> getPackageInfoForProviderAllUsers(android.content.Context context, android.webkit.WebViewProviderInfo configInfo) {
        return android.webkit.UserPackage.getPackageInfosAllUsers(context, configInfo.packageName, PACKAGE_FLAGS);
    }

    @Override // com.android.server.webkit.SystemInterface
    public int getMultiProcessSetting(android.content.Context context) {
        if (android.webkit.Flags.updateServiceV2()) {
            throw new java.lang.IllegalStateException("getMultiProcessSetting shouldn't be called if update_service_v2 flag is set.");
        }
        return android.provider.Settings.Global.getInt(context.getContentResolver(), "webview_multiprocess", 0);
    }

    @Override // com.android.server.webkit.SystemInterface
    public void setMultiProcessSetting(android.content.Context context, int value) {
        if (android.webkit.Flags.updateServiceV2()) {
            throw new java.lang.IllegalStateException("setMultiProcessSetting shouldn't be called if update_service_v2 flag is set.");
        }
        android.provider.Settings.Global.putInt(context.getContentResolver(), "webview_multiprocess", value);
    }

    @Override // com.android.server.webkit.SystemInterface
    public void notifyZygote(boolean enableMultiProcess) {
        if (android.webkit.Flags.updateServiceV2()) {
            throw new java.lang.IllegalStateException("notifyZygote shouldn't be called if update_service_v2 flag is set.");
        }
        android.webkit.WebViewZygote.setMultiprocessEnabled(enableMultiProcess);
    }

    @Override // com.android.server.webkit.SystemInterface
    public void ensureZygoteStarted() {
        android.webkit.WebViewZygote.getProcess();
    }

    @Override // com.android.server.webkit.SystemInterface
    public boolean isMultiProcessDefaultEnabled() {
        return true;
    }

    @Override // com.android.server.webkit.SystemInterface
    public void pinWebviewIfRequired(android.content.pm.ApplicationInfo appInfo) {
        com.android.server.PinnerService pinnerService = (com.android.server.PinnerService) com.android.server.LocalServices.getService(com.android.server.PinnerService.class);
        int webviewPinQuota = pinnerService.getWebviewPinQuota();
        if (webviewPinQuota <= 0) {
            return;
        }
        pinnerService.unpinGroup(PIN_GROUP);
        java.util.ArrayList<java.lang.String> apksToPin = new java.util.ArrayList<>();
        boolean pinSharedFirst = appInfo.metaData.getBoolean("PIN_SHARED_LIBS_FIRST", true);
        if (appInfo.sharedLibraryFiles != null) {
            for (java.lang.String sharedLib : appInfo.sharedLibraryFiles) {
                apksToPin.add(sharedLib);
            }
        }
        apksToPin.add(appInfo.sourceDir);
        if (!pinSharedFirst) {
            java.util.Collections.reverse(apksToPin);
        }
        for (java.lang.String apk : apksToPin) {
            if (webviewPinQuota > 0) {
                int bytesPinned = pinnerService.pinFile(apk, webviewPinQuota, appInfo, PIN_GROUP);
                webviewPinQuota -= bytesPinned;
            } else {
                return;
            }
        }
    }
}
