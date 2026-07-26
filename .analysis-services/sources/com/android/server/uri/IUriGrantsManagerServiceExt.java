package com.android.server.uri;

/* JADX INFO: loaded from: classes3.dex */
public interface IUriGrantsManagerServiceExt {
    default int changeUserIdInUriGrantsManagerService(int originUserId, android.net.Uri uri) {
        return originUserId;
    }

    default boolean skipMultiappHandleUri(int userId, android.net.Uri uri) {
        return false;
    }

    default int changeTargetUid(int origintargetUid, int callingUid, java.lang.String targetPkg, java.lang.String authority) {
        return origintargetUid;
    }

    default boolean isMultiappFromUid(int uid) {
        return false;
    }

    default boolean checkLastChanceInCheckUriPermissionLocked(android.util.SparseArray<android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission>> mGrantedUriPermissions, com.android.server.uri.GrantUri grantUri, int uid, int modeFlags, int minStrength) {
        return false;
    }

    default boolean needChangeUid(android.util.SparseArray<android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission>> mGrantedUriPermissions, java.lang.String auth, int uid) {
        return false;
    }

    default boolean isGrantedSystemApp(java.lang.String name) {
        return false;
    }
}
