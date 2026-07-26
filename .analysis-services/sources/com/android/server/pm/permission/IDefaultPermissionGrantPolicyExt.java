package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public interface IDefaultPermissionGrantPolicyExt {
    default void hookGetDefaultPermissionFiles(java.util.ArrayList<java.io.File> ret, java.io.File dir) {
    }

    default java.util.Set<java.lang.String> hookGrantDefaultSystemHandlerPermissions(java.util.Set<java.lang.String> defaultPermissions) {
        return defaultPermissions;
    }
}
