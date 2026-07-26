package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public final class UserPermissionState {
    private final android.util.ArraySet<java.lang.String> mInstallPermissionsFixed = new android.util.ArraySet<>();
    private final android.util.SparseArray<com.android.server.pm.permission.UidPermissionState> mUidStates = new android.util.SparseArray<>();

    public boolean areInstallPermissionsFixed(java.lang.String packageName) {
        return this.mInstallPermissionsFixed.contains(packageName);
    }

    public void setInstallPermissionsFixed(java.lang.String packageName, boolean fixed) {
        if (fixed) {
            this.mInstallPermissionsFixed.add(packageName);
        } else {
            this.mInstallPermissionsFixed.remove(packageName);
        }
    }

    public com.android.server.pm.permission.UidPermissionState getUidState(int appId) {
        checkAppId(appId);
        return this.mUidStates.get(appId);
    }

    public com.android.server.pm.permission.UidPermissionState getOrCreateUidState(int appId) {
        checkAppId(appId);
        com.android.server.pm.permission.UidPermissionState uidState = this.mUidStates.get(appId);
        if (uidState == null) {
            com.android.server.pm.permission.UidPermissionState uidState2 = new com.android.server.pm.permission.UidPermissionState();
            this.mUidStates.put(appId, uidState2);
            return uidState2;
        }
        return uidState;
    }

    com.android.server.pm.permission.UidPermissionState createUidStateWithExisting(int appId, com.android.server.pm.permission.UidPermissionState other) {
        checkAppId(appId);
        com.android.server.pm.permission.UidPermissionState uidState = new com.android.server.pm.permission.UidPermissionState(other);
        this.mUidStates.put(appId, uidState);
        return uidState;
    }

    public void removeUidState(int appId) {
        checkAppId(appId);
        this.mUidStates.delete(appId);
    }

    private void checkAppId(int appId) {
        if (android.os.UserHandle.getUserId(appId) != 0) {
            throw new java.lang.IllegalArgumentException("Invalid app ID " + appId);
        }
    }
}
