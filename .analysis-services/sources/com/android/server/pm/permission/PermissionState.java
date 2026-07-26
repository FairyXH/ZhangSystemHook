package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public final class PermissionState {
    private int mFlags;
    private boolean mGranted;
    private final java.lang.Object mLock;
    private final com.android.server.pm.permission.Permission mPermission;

    public PermissionState(com.android.server.pm.permission.Permission permission) {
        this.mLock = new java.lang.Object();
        this.mPermission = permission;
    }

    public PermissionState(com.android.server.pm.permission.PermissionState other) {
        this(other.mPermission);
        this.mGranted = other.mGranted;
        this.mFlags = other.mFlags;
    }

    public com.android.server.pm.permission.Permission getPermission() {
        return this.mPermission;
    }

    public java.lang.String getName() {
        return this.mPermission.getName();
    }

    public int[] computeGids(int userId) {
        return this.mPermission.computeGids(userId);
    }

    public boolean isGranted() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mGranted;
        }
        return z;
    }

    public boolean grant() {
        synchronized (this.mLock) {
            if (this.mGranted) {
                return false;
            }
            this.mGranted = true;
            com.android.server.pm.permission.UidPermissionState.invalidateCache();
            return true;
        }
    }

    public boolean revoke() {
        synchronized (this.mLock) {
            if (!this.mGranted) {
                return false;
            }
            this.mGranted = false;
            com.android.server.pm.permission.UidPermissionState.invalidateCache();
            return true;
        }
    }

    public int getFlags() {
        int i;
        synchronized (this.mLock) {
            i = this.mFlags;
        }
        return i;
    }

    public boolean updateFlags(int flagMask, int flagValues) {
        boolean z;
        synchronized (this.mLock) {
            int newFlags = flagValues & flagMask;
            com.android.server.pm.permission.UidPermissionState.invalidateCache();
            int oldFlags = this.mFlags;
            this.mFlags = (this.mFlags & (~flagMask)) | newFlags;
            z = this.mFlags != oldFlags;
        }
        return z;
    }

    public boolean isDefault() {
        boolean z;
        synchronized (this.mLock) {
            z = !this.mGranted && this.mFlags == 0;
        }
        return z;
    }
}
