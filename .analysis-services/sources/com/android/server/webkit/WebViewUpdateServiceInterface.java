package com.android.server.webkit;

/* JADX INFO: loaded from: classes3.dex */
interface WebViewUpdateServiceInterface {
    java.lang.String changeProviderAndSetting(java.lang.String str);

    void dumpState(java.io.PrintWriter printWriter);

    void enableMultiProcess(boolean z);

    android.content.pm.PackageInfo getCurrentWebViewPackage();

    android.webkit.WebViewProviderInfo getDefaultWebViewPackage();

    android.webkit.WebViewProviderInfo[] getValidWebViewPackages();

    android.webkit.WebViewProviderInfo[] getWebViewPackages();

    void handleNewUser(int i);

    void handleUserRemoved(int i);

    boolean isMultiProcessEnabled();

    void notifyRelroCreationCompleted();

    void packageStateChanged(java.lang.String str, int i, int i2);

    void prepareWebViewInSystemServer();

    android.webkit.WebViewProviderResponse waitForAndGetProvider();
}
