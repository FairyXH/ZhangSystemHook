package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class SecurityStateManagerService extends android.os.ISecurityStateManager.Stub {
    static final java.util.regex.Pattern KERNEL_RELEASE_PATTERN = java.util.regex.Pattern.compile("(\\d+\\.\\d+\\.\\d+)(.*)");
    private static final java.lang.String TAG = "SecurityStateManagerService";
    static final java.lang.String VENDOR_SECURITY_PATCH_PROPERTY_KEY = "ro.vendor.build.security_patch";
    private final android.content.Context mContext;
    private final android.content.pm.PackageManager mPackageManager;

    public SecurityStateManagerService(android.content.Context context) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
    }

    public android.os.Bundle getGlobalSecurityState() {
        android.os.Bundle globalSecurityState = new android.os.Bundle();
        globalSecurityState.putString("system_spl", android.os.Build.VERSION.SECURITY_PATCH);
        globalSecurityState.putString("vendor_spl", android.os.SystemProperties.get(VENDOR_SECURITY_PATCH_PROPERTY_KEY, ""));
        java.lang.String moduleMetadataProviderPackageName = this.mContext.getString(android.R.string.config_defaultQrCodeComponent);
        if (!moduleMetadataProviderPackageName.isEmpty()) {
            globalSecurityState.putString(moduleMetadataProviderPackageName, getSpl(moduleMetadataProviderPackageName));
        }
        globalSecurityState.putString("kernel_version", getKernelVersion());
        addWebViewPackages(globalSecurityState);
        addSecurityStatePackages(globalSecurityState);
        return globalSecurityState;
    }

    private java.lang.String getSpl(java.lang.String packageName) {
        if (!android.text.TextUtils.isEmpty(packageName)) {
            try {
                return this.mPackageManager.getPackageInfo(packageName, 0).versionName;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Failed to get SPL for package %s.", new java.lang.Object[]{packageName}), e);
                return "";
            }
        }
        return "";
    }

    private java.lang.String getKernelVersion() {
        java.util.regex.Matcher matcher = KERNEL_RELEASE_PATTERN.matcher(android.os.VintfRuntimeInfo.getKernelVersion());
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return "";
    }

    private void addWebViewPackages(android.os.Bundle bundle) {
        for (android.webkit.WebViewProviderInfo info : android.webkit.WebViewUpdateService.getAllWebViewPackages()) {
            java.lang.String packageName = info.packageName;
            bundle.putString(packageName, getSpl(packageName));
        }
    }

    private void addSecurityStatePackages(android.os.Bundle bundle) {
        java.lang.String[] packageNames = this.mContext.getResources().getStringArray(android.R.array.config_screenBrighteningThresholds);
        for (java.lang.String packageName : packageNames) {
            bundle.putString(packageName, getSpl(packageName));
        }
    }
}
