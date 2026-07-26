package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public interface PermissionMigrationHelper {
    int getLegacyPermissionStateVersion(int i);

    java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermissionState>> getLegacyPermissionStates(int i);

    java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermission> getLegacyPermissionTrees();

    java.util.Map<java.lang.String, com.android.server.pm.permission.PermissionMigrationHelper.LegacyPermission> getLegacyPermissions();

    boolean hasLegacyPermission();

    boolean hasLegacyPermissionState(int i);

    public static final class LegacyPermission {
        private final android.content.pm.PermissionInfo mPermissionInfo;
        private final int mType;

        LegacyPermission(android.content.pm.PermissionInfo permissionInfo, int type) {
            this.mPermissionInfo = permissionInfo;
            this.mType = type;
        }

        public android.content.pm.PermissionInfo getPermissionInfo() {
            return this.mPermissionInfo;
        }

        public int getType() {
            return this.mType;
        }
    }

    public static final class LegacyPermissionState {
        private final int mFlags;
        private final boolean mGranted;

        LegacyPermissionState(boolean granted, int flags) {
            this.mGranted = granted;
            this.mFlags = flags;
        }

        public boolean isGranted() {
            return this.mGranted;
        }

        public int getFlags() {
            return this.mFlags;
        }
    }
}
