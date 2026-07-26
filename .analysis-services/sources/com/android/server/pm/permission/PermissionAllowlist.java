package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public final class PermissionAllowlist {
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mOemAppAllowlist = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mPrivilegedAppAllowlist = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mVendorPrivilegedAppAllowlist = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mProductPrivilegedAppAllowlist = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mSystemExtPrivilegedAppAllowlist = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>>> mApexPrivilegedAppAllowlists = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mSignatureAppAllowlist = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mVendorSignatureAppAllowlist = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mProductSignatureAppAllowlist = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mSystemExtSignatureAppAllowlist = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> mApexSignatureAppAllowlist = new android.util.ArrayMap<>();

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> getOemAppAllowlist() {
        return this.mOemAppAllowlist;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> getPrivilegedAppAllowlist() {
        return this.mPrivilegedAppAllowlist;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> getVendorPrivilegedAppAllowlist() {
        return this.mVendorPrivilegedAppAllowlist;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> getProductPrivilegedAppAllowlist() {
        return this.mProductPrivilegedAppAllowlist;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> getSystemExtPrivilegedAppAllowlist() {
        return this.mSystemExtPrivilegedAppAllowlist;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>>> getApexPrivilegedAppAllowlists() {
        return this.mApexPrivilegedAppAllowlists;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> getSignatureAppAllowlist() {
        return this.mSignatureAppAllowlist;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> getVendorSignatureAppAllowlist() {
        return this.mVendorSignatureAppAllowlist;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> getProductSignatureAppAllowlist() {
        return this.mProductSignatureAppAllowlist;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> getSystemExtSignatureAppAllowlist() {
        return this.mSystemExtSignatureAppAllowlist;
    }

    public android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> getApexSignatureAppAllowlist() {
        return this.mApexSignatureAppAllowlist;
    }

    public java.lang.Boolean getOemAppAllowlistState(java.lang.String packageName, java.lang.String permissionName) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = this.mOemAppAllowlist.get(packageName);
        if (permissions == null) {
            return null;
        }
        return permissions.get(permissionName);
    }

    public java.lang.Boolean getPrivilegedAppAllowlistState(java.lang.String packageName, java.lang.String permissionName) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = this.mPrivilegedAppAllowlist.get(packageName);
        if (permissions == null) {
            return null;
        }
        return permissions.get(permissionName);
    }

    public java.lang.Boolean getVendorPrivilegedAppAllowlistState(java.lang.String packageName, java.lang.String permissionName) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = this.mVendorPrivilegedAppAllowlist.get(packageName);
        if (permissions == null) {
            return null;
        }
        return permissions.get(permissionName);
    }

    public java.lang.Boolean getProductPrivilegedAppAllowlistState(java.lang.String packageName, java.lang.String permissionName) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = this.mProductPrivilegedAppAllowlist.get(packageName);
        if (permissions == null) {
            return null;
        }
        return permissions.get(permissionName);
    }

    public java.lang.Boolean getSystemExtPrivilegedAppAllowlistState(java.lang.String packageName, java.lang.String permissionName) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = this.mSystemExtPrivilegedAppAllowlist.get(packageName);
        if (permissions == null) {
            return null;
        }
        return permissions.get(permissionName);
    }

    public java.lang.Boolean getApexPrivilegedAppAllowlistState(java.lang.String moduleName, java.lang.String packageName, java.lang.String permissionName) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions;
        android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Boolean>> allowlist = this.mApexPrivilegedAppAllowlists.get(moduleName);
        if (allowlist == null || (permissions = allowlist.get(packageName)) == null) {
            return null;
        }
        return permissions.get(permissionName);
    }

    public java.lang.Boolean getSignatureAppAllowlistState(java.lang.String packageName, java.lang.String permissionName) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = this.mSignatureAppAllowlist.get(packageName);
        if (permissions == null) {
            return null;
        }
        return permissions.get(permissionName);
    }

    public java.lang.Boolean getVendorSignatureAppAllowlistState(java.lang.String packageName, java.lang.String permissionName) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = this.mVendorSignatureAppAllowlist.get(packageName);
        if (permissions == null) {
            return null;
        }
        return permissions.get(permissionName);
    }

    public java.lang.Boolean getProductSignatureAppAllowlistState(java.lang.String packageName, java.lang.String permissionName) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = this.mProductSignatureAppAllowlist.get(packageName);
        if (permissions == null) {
            return null;
        }
        return permissions.get(permissionName);
    }

    public java.lang.Boolean getSystemExtSignatureAppAllowlistState(java.lang.String packageName, java.lang.String permissionName) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = this.mSystemExtSignatureAppAllowlist.get(packageName);
        if (permissions == null) {
            return null;
        }
        return permissions.get(permissionName);
    }

    public java.lang.Boolean getApexSignatureAppAllowlistState(java.lang.String packageName, java.lang.String permissionName) {
        android.util.ArrayMap<java.lang.String, java.lang.Boolean> permissions = this.mApexSignatureAppAllowlist.get(packageName);
        if (permissions == null) {
            return null;
        }
        return permissions.get(permissionName);
    }
}
