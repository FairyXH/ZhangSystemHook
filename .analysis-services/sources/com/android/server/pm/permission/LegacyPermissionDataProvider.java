package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public interface LegacyPermissionDataProvider {
    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getAllAppOpPermissionPackages();

    int[] getGidsForUid(int i);

    com.android.server.pm.permission.LegacyPermissionState getLegacyPermissionState(int i);

    java.util.List<com.android.server.pm.permission.LegacyPermission> getLegacyPermissions();

    void writeLegacyPermissionStateTEMP();
}
