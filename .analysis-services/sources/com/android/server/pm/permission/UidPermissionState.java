package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public final class UidPermissionState {
    private boolean mMissing;
    private android.util.ArrayMap<java.lang.String, com.android.server.pm.permission.PermissionState> mPermissions;

    public UidPermissionState() {
    }

    public UidPermissionState(com.android.server.pm.permission.UidPermissionState other) {
        this.mMissing = other.mMissing;
        if (other.mPermissions != null) {
            this.mPermissions = new android.util.ArrayMap<>();
            int permissionsSize = other.mPermissions.size();
            for (int i = 0; i < permissionsSize; i++) {
                java.lang.String name = other.mPermissions.keyAt(i);
                com.android.server.pm.permission.PermissionState permissionState = other.mPermissions.valueAt(i);
                this.mPermissions.put(name, new com.android.server.pm.permission.PermissionState(permissionState));
            }
        }
    }

    public void reset() {
        this.mMissing = false;
        this.mPermissions = null;
        invalidateCache();
    }

    public boolean isMissing() {
        return this.mMissing;
    }

    public void setMissing(boolean missing) {
        this.mMissing = missing;
    }

    @java.lang.Deprecated
    public boolean hasPermissionState(java.lang.String name) {
        return this.mPermissions != null && this.mPermissions.containsKey(name);
    }

    @java.lang.Deprecated
    public boolean hasPermissionState(android.util.ArraySet<java.lang.String> names) {
        if (this.mPermissions == null) {
            return false;
        }
        int namesSize = names.size();
        for (int i = 0; i < namesSize; i++) {
            java.lang.String name = names.valueAt(i);
            if (this.mPermissions.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    public com.android.server.pm.permission.PermissionState getPermissionState(java.lang.String name) {
        if (this.mPermissions == null) {
            return null;
        }
        return this.mPermissions.get(name);
    }

    private com.android.server.pm.permission.PermissionState getOrCreatePermissionState(com.android.server.pm.permission.Permission permission) {
        if (this.mPermissions == null) {
            this.mPermissions = new android.util.ArrayMap<>();
        }
        java.lang.String name = permission.getName();
        com.android.server.pm.permission.PermissionState permissionState = this.mPermissions.get(name);
        if (permissionState == null) {
            com.android.server.pm.permission.PermissionState permissionState2 = new com.android.server.pm.permission.PermissionState(permission);
            this.mPermissions.put(name, permissionState2);
            return permissionState2;
        }
        return permissionState;
    }

    public java.util.List<com.android.server.pm.permission.PermissionState> getPermissionStates() {
        if (this.mPermissions == null) {
            return java.util.Collections.emptyList();
        }
        return new java.util.ArrayList(this.mPermissions.values());
    }

    public void putPermissionState(com.android.server.pm.permission.Permission permission, boolean granted, int flags) {
        java.lang.String name = permission.getName();
        if (this.mPermissions == null) {
            this.mPermissions = new android.util.ArrayMap<>();
        } else {
            this.mPermissions.remove(name);
        }
        com.android.server.pm.permission.PermissionState permissionState = new com.android.server.pm.permission.PermissionState(permission);
        if (granted) {
            permissionState.grant();
        }
        permissionState.updateFlags(flags, flags);
        this.mPermissions.put(name, permissionState);
    }

    public boolean removePermissionState(java.lang.String name) {
        if (this.mPermissions == null) {
            return false;
        }
        boolean changed = this.mPermissions.remove(name) != null;
        if (changed && this.mPermissions.isEmpty()) {
            this.mPermissions = null;
        }
        return changed;
    }

    public boolean isPermissionGranted(java.lang.String name) {
        com.android.server.pm.permission.PermissionState permissionState = getPermissionState(name);
        return permissionState != null && permissionState.isGranted();
    }

    public java.util.Set<java.lang.String> getGrantedPermissions() {
        if (this.mPermissions == null) {
            return java.util.Collections.emptySet();
        }
        java.util.Set<java.lang.String> permissions = new android.util.ArraySet<>(this.mPermissions.size());
        int permissionsSize = this.mPermissions.size();
        for (int i = 0; i < permissionsSize; i++) {
            com.android.server.pm.permission.PermissionState permissionState = this.mPermissions.valueAt(i);
            if (permissionState.isGranted()) {
                permissions.add(permissionState.getName());
            }
        }
        return permissions;
    }

    public boolean grantPermission(com.android.server.pm.permission.Permission permission) {
        com.android.server.pm.permission.PermissionState permissionState = getOrCreatePermissionState(permission);
        return permissionState.grant();
    }

    public boolean revokePermission(com.android.server.pm.permission.Permission permission) {
        java.lang.String name = permission.getName();
        com.android.server.pm.permission.PermissionState permissionState = getPermissionState(name);
        if (permissionState == null) {
            return false;
        }
        boolean changed = permissionState.revoke();
        if (changed && permissionState.isDefault()) {
            removePermissionState(name);
        }
        return changed;
    }

    public int getPermissionFlags(java.lang.String name) {
        com.android.server.pm.permission.PermissionState permissionState = getPermissionState(name);
        if (permissionState == null) {
            return 0;
        }
        return permissionState.getFlags();
    }

    public boolean updatePermissionFlags(com.android.server.pm.permission.Permission permission, int flagMask, int flagValues) {
        if (flagMask == 0) {
            return false;
        }
        com.android.server.pm.permission.PermissionState permissionState = getOrCreatePermissionState(permission);
        boolean changed = permissionState.updateFlags(flagMask, flagValues);
        if (changed && permissionState.isDefault()) {
            removePermissionState(permission.getName());
        }
        return changed;
    }

    public boolean updatePermissionFlagsForAllPermissions(int flagMask, int flagValues) {
        if (flagMask == 0 || this.mPermissions == null) {
            return false;
        }
        boolean anyChanged = false;
        for (int i = this.mPermissions.size() - 1; i >= 0; i--) {
            com.android.server.pm.permission.PermissionState permissionState = this.mPermissions.valueAt(i);
            boolean changed = permissionState.updateFlags(flagMask, flagValues);
            if (changed && permissionState.isDefault()) {
                this.mPermissions.removeAt(i);
            }
            anyChanged |= changed;
        }
        return anyChanged;
    }

    public boolean isPermissionsReviewRequired() {
        if (this.mPermissions == null) {
            return false;
        }
        int permissionsSize = this.mPermissions.size();
        for (int i = 0; i < permissionsSize; i++) {
            com.android.server.pm.permission.PermissionState permission = this.mPermissions.valueAt(i);
            if ((permission.getFlags() & 64) != 0) {
                return true;
            }
        }
        return false;
    }

    public int[] computeGids(int[] globalGids, int userId) {
        android.util.IntArray gids = android.util.IntArray.wrap(globalGids);
        if (this.mPermissions == null) {
            return gids.toArray();
        }
        int permissionsSize = this.mPermissions.size();
        for (int i = 0; i < permissionsSize; i++) {
            com.android.server.pm.permission.PermissionState permissionState = this.mPermissions.valueAt(i);
            if (permissionState.isGranted()) {
                int[] permissionGids = permissionState.computeGids(userId);
                if (permissionGids.length != 0) {
                    gids.addAll(permissionGids);
                }
            }
        }
        return gids.toArray();
    }

    static void invalidateCache() {
        android.content.pm.PackageManager.invalidatePackageInfoCache();
    }
}
