package com.android.server.webkit;

/* JADX INFO: loaded from: classes3.dex */
public interface SystemInterface {
    void enablePackageForAllUsers(android.content.Context context, java.lang.String str, boolean z);

    void ensureZygoteStarted();

    long getFactoryPackageVersion(java.lang.String str) throws android.content.pm.PackageManager.NameNotFoundException;

    int getMultiProcessSetting(android.content.Context context);

    android.content.pm.PackageInfo getPackageInfoForProvider(android.webkit.WebViewProviderInfo webViewProviderInfo) throws android.content.pm.PackageManager.NameNotFoundException;

    java.util.List<android.webkit.UserPackage> getPackageInfoForProviderAllUsers(android.content.Context context, android.webkit.WebViewProviderInfo webViewProviderInfo);

    java.lang.String getUserChosenWebViewProvider(android.content.Context context);

    android.webkit.WebViewProviderInfo[] getWebViewPackages();

    void installExistingPackageForAllUsers(android.content.Context context, java.lang.String str);

    boolean isMultiProcessDefaultEnabled();

    void killPackageDependents(java.lang.String str);

    void notifyZygote(boolean z);

    int onWebViewProviderChanged(android.content.pm.PackageInfo packageInfo);

    void pinWebviewIfRequired(android.content.pm.ApplicationInfo applicationInfo);

    void setMultiProcessSetting(android.content.Context context, int i);

    boolean systemIsDebuggable();

    void updateUserSetting(android.content.Context context, java.lang.String str);
}
