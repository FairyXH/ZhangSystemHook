package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class SplashScreenExceptionList {
    private static final boolean DEBUG = android.os.Build.isDebuggable();
    private static final java.lang.String KEY_SPLASH_SCREEN_EXCEPTION_LIST = "splash_screen_exception_list";
    private static final java.lang.String LOG_TAG = "SplashScreenExceptionList";
    private static final java.lang.String NAMESPACE = "window_manager";
    private static final java.lang.String OPT_OUT_METADATA_FLAG = "android.splashscreen.exception_opt_out";
    private final java.util.HashSet<java.lang.String> mDeviceConfigExcludedPackages = new java.util.HashSet<>();
    private final java.lang.Object mLock = new java.lang.Object();
    final android.provider.DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener;

    SplashScreenExceptionList(java.util.concurrent.Executor executor) {
        updateDeviceConfig(android.provider.DeviceConfig.getString(NAMESPACE, KEY_SPLASH_SCREEN_EXCEPTION_LIST, ""));
        this.mOnPropertiesChangedListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wm.SplashScreenExceptionList$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$new$0(properties);
            }
        };
        android.provider.DeviceConfig.addOnPropertiesChangedListener(NAMESPACE, executor, this.mOnPropertiesChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.provider.DeviceConfig.Properties properties) {
        updateDeviceConfig(properties.getString(KEY_SPLASH_SCREEN_EXCEPTION_LIST, ""));
    }

    void updateDeviceConfig(java.lang.String values) {
        parseDeviceConfigPackageList(values);
    }

    public boolean isException(java.lang.String packageName, int targetSdk, java.util.function.Supplier<android.content.pm.ApplicationInfo> infoSupplier) {
        if (targetSdk > 35) {
            return false;
        }
        synchronized (this.mLock) {
            if (DEBUG) {
                android.util.Slog.v(LOG_TAG, java.lang.String.format(java.util.Locale.US, "SplashScreen checking exception for package %s (target sdk:%d) -> %s", packageName, java.lang.Integer.valueOf(targetSdk), java.lang.Boolean.valueOf(this.mDeviceConfigExcludedPackages.contains(packageName))));
            }
            if (this.mDeviceConfigExcludedPackages.contains(packageName)) {
                return !isOptedOut(infoSupplier);
            }
            return false;
        }
    }

    private static boolean isOptedOut(java.util.function.Supplier<android.content.pm.ApplicationInfo> infoProvider) {
        android.content.pm.ApplicationInfo info;
        return (infoProvider == null || (info = infoProvider.get()) == null || info.metaData == null || !info.metaData.getBoolean(OPT_OUT_METADATA_FLAG, false)) ? false : true;
    }

    private void parseDeviceConfigPackageList(java.lang.String rawList) {
        synchronized (this.mLock) {
            this.mDeviceConfigExcludedPackages.clear();
            java.lang.String[] packages = rawList.split(",");
            for (java.lang.String packageName : packages) {
                java.lang.String packageNameTrimmed = packageName.trim();
                if (!packageNameTrimmed.isEmpty()) {
                    this.mDeviceConfigExcludedPackages.add(packageNameTrimmed);
                }
            }
        }
    }
}
