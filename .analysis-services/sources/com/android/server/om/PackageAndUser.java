package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
final class PackageAndUser {
    public final java.lang.String packageName;
    public final int userId;

    PackageAndUser(java.lang.String packageName, int userId) {
        this.packageName = packageName;
        this.userId = userId;
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.om.PackageAndUser)) {
            return false;
        }
        com.android.server.om.PackageAndUser other = (com.android.server.om.PackageAndUser) obj;
        return this.packageName.equals(other.packageName) && this.userId == other.userId;
    }

    public int hashCode() {
        int result = (1 * 31) + this.packageName.hashCode();
        return (result * 31) + this.userId;
    }

    public java.lang.String toString() {
        return java.lang.String.format("PackageAndUser{packageName=%s, userId=%d}", this.packageName, java.lang.Integer.valueOf(this.userId));
    }
}
