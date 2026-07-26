package com.android.server.companion;

/* JADX INFO: loaded from: classes.dex */
public class CompanionDeviceConfig {
    public static final java.lang.String ENABLE_CONTEXT_SYNC_TELECOM = "enable_context_sync_telecom";
    private static final java.lang.String NAMESPACE_COMPANION = "companion";

    public static boolean isEnabled(java.lang.String flag) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return android.provider.DeviceConfig.getBoolean(NAMESPACE_COMPANION, flag, false);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public static boolean isEnabled(java.lang.String flag, boolean defaultValue) {
        return android.provider.DeviceConfig.getBoolean(NAMESPACE_COMPANION, flag, defaultValue);
    }
}
