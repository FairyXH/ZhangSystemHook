package com.android.aconfig_new_storage;

/* JADX INFO: loaded from: classes.dex */
public final class Flags {
    private static com.android.aconfig_new_storage.FeatureFlags FEATURE_FLAGS = new com.android.aconfig_new_storage.FeatureFlagsImpl();
    public static final java.lang.String FLAG_ENABLE_ACONFIG_STORAGE_DAEMON = "com.android.aconfig_new_storage.enable_aconfig_storage_daemon";

    public static boolean enableAconfigStorageDaemon() {
        return FEATURE_FLAGS.enableAconfigStorageDaemon();
    }
}
