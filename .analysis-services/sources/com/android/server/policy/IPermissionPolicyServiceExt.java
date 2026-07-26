package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public interface IPermissionPolicyServiceExt {
    default boolean skipSynchronizePackagePermissionsAndAppOpsAsyncForUser(java.lang.String packageName, int changedUserId) {
        return false;
    }

    default boolean skipSynchronizeUidPermissionsAndAppOpsAsync(int uid) {
        return false;
    }

    default boolean shouldSynchronizePermissionsAndAppOpsForUser(int userId) {
        return false;
    }

    default boolean shouldGrantOrUpgradeDefaultRuntimePermissions(int userId) {
        return false;
    }

    default boolean shouldCallRemotePermissionController(int userId) {
        return false;
    }

    default void beforeGrantOrUpgradeDefaultRuntimePermissions(int userId) {
    }

    default boolean skipRunOnInitialized(int userId) {
        return false;
    }

    default boolean shouldDelayAppOpsSyncJob(int uid) {
        return false;
    }

    default int getSwitchOp(java.lang.String permission) {
        return -1;
    }

    default boolean skipUpdateUserSensitiveForApp(int uid, android.content.Intent intent, android.os.Handler handler) {
        return false;
    }
}
