package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class BackupAndRestoreFeatureFlags {
    private static final java.lang.String NAMESPACE = "backup_and_restore";

    private BackupAndRestoreFeatureFlags() {
    }

    public static long getBackupTransportFutureTimeoutMillis() {
        return android.provider.DeviceConfig.getLong(NAMESPACE, "backup_transport_future_timeout_millis", 600000L);
    }

    public static long getBackupTransportCallbackTimeoutMillis() {
        return android.provider.DeviceConfig.getLong(NAMESPACE, "backup_transport_callback_timeout_millis", 300000L);
    }

    public static int getFullBackupWriteToTransportBufferSizeBytes() {
        return android.provider.DeviceConfig.getInt(NAMESPACE, "full_backup_write_to_transport_buffer_size_bytes", 8192);
    }

    public static int getFullBackupUtilsRouteBufferSizeBytes() {
        return android.provider.DeviceConfig.getInt(NAMESPACE, "full_backup_utils_route_buffer_size_bytes", 32768);
    }

    public static boolean getUnifiedRestoreContinueAfterTransportFailureInKvRestore() {
        return android.provider.DeviceConfig.getBoolean(NAMESPACE, "unified_restore_continue_after_transport_failure_in_kv_restore", true);
    }
}
