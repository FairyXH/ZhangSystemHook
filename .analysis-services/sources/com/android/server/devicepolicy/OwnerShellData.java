package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class OwnerShellData {
    public final android.content.ComponentName admin;
    public boolean isAffiliated;
    public final boolean isDeviceOwner;
    public final boolean isManagedProfileOwner;
    public final boolean isProfileOwner;
    public final int parentUserId;
    public final int userId;

    private OwnerShellData(int userId, int parentUserId, android.content.ComponentName admin, boolean isDeviceOwner, boolean isProfileOwner, boolean isManagedProfileOwner) {
        com.android.internal.util.Preconditions.checkArgument(userId != -10000, "userId cannot be USER_NULL");
        this.userId = userId;
        this.parentUserId = parentUserId;
        this.admin = (android.content.ComponentName) java.util.Objects.requireNonNull(admin, "admin must not be null");
        this.isDeviceOwner = isDeviceOwner;
        this.isProfileOwner = isProfileOwner;
        this.isManagedProfileOwner = isManagedProfileOwner;
        if (isManagedProfileOwner) {
            com.android.internal.util.Preconditions.checkArgument(parentUserId != -10000, "parentUserId cannot be USER_NULL for managed profile owner");
            com.android.internal.util.Preconditions.checkArgument(parentUserId != userId, "cannot be parent of itself (%d)", new java.lang.Object[]{java.lang.Integer.valueOf(userId)});
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(getClass().getSimpleName()).append("[userId=").append(this.userId).append(",admin=").append(this.admin.flattenToShortString());
        if (this.isDeviceOwner) {
            sb.append(",deviceOwner");
        }
        if (this.isProfileOwner) {
            sb.append(",isProfileOwner");
        }
        if (this.isManagedProfileOwner) {
            sb.append(",isManagedProfileOwner");
        }
        if (this.parentUserId != -10000) {
            sb.append(",parentUserId=").append(this.parentUserId);
        }
        if (this.isAffiliated) {
            sb.append(",isAffiliated");
        }
        return sb.append(']').toString();
    }

    static com.android.server.devicepolicy.OwnerShellData forDeviceOwner(int userId, android.content.ComponentName admin) {
        return new com.android.server.devicepolicy.OwnerShellData(userId, -10000, admin, true, false, false);
    }

    static com.android.server.devicepolicy.OwnerShellData forUserProfileOwner(int userId, android.content.ComponentName admin) {
        return new com.android.server.devicepolicy.OwnerShellData(userId, -10000, admin, false, true, false);
    }

    static com.android.server.devicepolicy.OwnerShellData forManagedProfileOwner(int userId, int parentUserId, android.content.ComponentName admin) {
        return new com.android.server.devicepolicy.OwnerShellData(userId, parentUserId, admin, false, false, true);
    }
}
