package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class EnforcingAdmin {
    private static final java.lang.String ATTR_AUTHORITIES = "authorities";
    private static final java.lang.String ATTR_AUTHORITIES_SEPARATOR = ";";
    private static final java.lang.String ATTR_CLASS_NAME = "class-name";
    private static final java.lang.String ATTR_IS_ROLE = "is-role";
    private static final java.lang.String ATTR_PACKAGE_NAME = "package-name";
    private static final java.lang.String ATTR_USER_ID = "user-id";
    static final java.lang.String DEFAULT_AUTHORITY = "default";
    static final java.lang.String DEVICE_ADMIN_AUTHORITY = "device_admin";
    static final java.lang.String DPC_AUTHORITY = "enterprise";
    static final java.lang.String ROLE_AUTHORITY_PREFIX = "role:";
    static final java.lang.String TAG = "EnforcingAdmin";
    private final com.android.server.devicepolicy.ActiveAdmin mActiveAdmin;
    private java.util.Set<java.lang.String> mAuthorities;
    private final android.content.ComponentName mComponentName;
    private final boolean mIsRoleAuthority;
    private final java.lang.String mPackageName;
    private final int mUserId;

    static com.android.server.devicepolicy.EnforcingAdmin createEnforcingAdmin(java.lang.String packageName, int userId, com.android.server.devicepolicy.ActiveAdmin admin) {
        java.util.Objects.requireNonNull(packageName);
        return new com.android.server.devicepolicy.EnforcingAdmin(packageName, userId, admin);
    }

    static com.android.server.devicepolicy.EnforcingAdmin createEnterpriseEnforcingAdmin(android.content.ComponentName componentName, int userId) {
        java.util.Objects.requireNonNull(componentName);
        return new com.android.server.devicepolicy.EnforcingAdmin(componentName.getPackageName(), componentName, java.util.Set.of(DPC_AUTHORITY), userId, null);
    }

    static com.android.server.devicepolicy.EnforcingAdmin createEnterpriseEnforcingAdmin(android.content.ComponentName componentName, int userId, com.android.server.devicepolicy.ActiveAdmin activeAdmin) {
        java.util.Objects.requireNonNull(componentName);
        return new com.android.server.devicepolicy.EnforcingAdmin(componentName.getPackageName(), componentName, java.util.Set.of(DPC_AUTHORITY), userId, activeAdmin);
    }

    static com.android.server.devicepolicy.EnforcingAdmin createDeviceAdminEnforcingAdmin(android.content.ComponentName componentName, int userId, com.android.server.devicepolicy.ActiveAdmin activeAdmin) {
        java.util.Objects.requireNonNull(componentName);
        return new com.android.server.devicepolicy.EnforcingAdmin(componentName.getPackageName(), componentName, java.util.Set.of(DEVICE_ADMIN_AUTHORITY), userId, activeAdmin);
    }

    static com.android.server.devicepolicy.EnforcingAdmin createEnforcingAdmin(android.app.admin.EnforcingAdmin admin) {
        java.util.Objects.requireNonNull(admin);
        android.app.admin.RoleAuthority authority = admin.getAuthority();
        new java.util.HashSet();
        if (android.app.admin.DpcAuthority.DPC_AUTHORITY.equals(authority)) {
            return new com.android.server.devicepolicy.EnforcingAdmin(admin.getPackageName(), admin.getComponentName(), java.util.Set.of(DPC_AUTHORITY), admin.getUserHandle().getIdentifier(), null);
        }
        if (android.app.admin.DeviceAdminAuthority.DEVICE_ADMIN_AUTHORITY.equals(authority)) {
            return new com.android.server.devicepolicy.EnforcingAdmin(admin.getPackageName(), admin.getComponentName(), java.util.Set.of(DEVICE_ADMIN_AUTHORITY), admin.getUserHandle().getIdentifier(), null);
        }
        if (authority instanceof android.app.admin.RoleAuthority) {
            return new com.android.server.devicepolicy.EnforcingAdmin(admin.getPackageName(), admin.getComponentName(), java.util.Set.of(DEVICE_ADMIN_AUTHORITY), admin.getUserHandle().getIdentifier(), null, true);
        }
        return new com.android.server.devicepolicy.EnforcingAdmin(admin.getPackageName(), admin.getComponentName(), java.util.Set.of(), admin.getUserHandle().getIdentifier(), null);
    }

    static java.lang.String getRoleAuthorityOf(java.lang.String roleName) {
        return ROLE_AUTHORITY_PREFIX + roleName;
    }

    static android.app.admin.Authority getParcelableAuthority(java.lang.String authority) {
        if (authority == null || authority.isEmpty()) {
            return android.app.admin.UnknownAuthority.UNKNOWN_AUTHORITY;
        }
        if (DPC_AUTHORITY.equals(authority)) {
            return android.app.admin.DpcAuthority.DPC_AUTHORITY;
        }
        if (DEVICE_ADMIN_AUTHORITY.equals(authority)) {
            return android.app.admin.DeviceAdminAuthority.DEVICE_ADMIN_AUTHORITY;
        }
        if (authority.startsWith(ROLE_AUTHORITY_PREFIX)) {
            java.lang.String role = authority.substring(ROLE_AUTHORITY_PREFIX.length());
            return new android.app.admin.RoleAuthority(java.util.Set.of(role));
        }
        return android.app.admin.UnknownAuthority.UNKNOWN_AUTHORITY;
    }

    private EnforcingAdmin(java.lang.String packageName, android.content.ComponentName componentName, java.util.Set<java.lang.String> authorities, int userId, com.android.server.devicepolicy.ActiveAdmin activeAdmin) {
        java.util.Objects.requireNonNull(packageName);
        java.util.Objects.requireNonNull(authorities);
        this.mIsRoleAuthority = false;
        this.mPackageName = packageName;
        this.mComponentName = componentName;
        this.mAuthorities = new java.util.HashSet(authorities);
        this.mUserId = userId;
        this.mActiveAdmin = activeAdmin;
    }

    private EnforcingAdmin(java.lang.String packageName, int userId, com.android.server.devicepolicy.ActiveAdmin activeAdmin) {
        java.util.Objects.requireNonNull(packageName);
        this.mIsRoleAuthority = true;
        this.mPackageName = packageName;
        this.mUserId = userId;
        this.mComponentName = null;
        this.mAuthorities = null;
        this.mActiveAdmin = activeAdmin;
    }

    private EnforcingAdmin(java.lang.String packageName, android.content.ComponentName componentName, java.util.Set<java.lang.String> authorities, int userId, com.android.server.devicepolicy.ActiveAdmin activeAdmin, boolean isRoleAuthority) {
        java.util.Objects.requireNonNull(packageName);
        java.util.Objects.requireNonNull(authorities);
        this.mIsRoleAuthority = isRoleAuthority;
        this.mPackageName = packageName;
        this.mComponentName = componentName;
        this.mAuthorities = new java.util.HashSet(authorities);
        this.mUserId = userId;
        this.mActiveAdmin = activeAdmin;
    }

    private static java.util.Set<java.lang.String> getRoleAuthoritiesOrDefault(java.lang.String packageName, int userId) {
        java.util.Set<java.lang.String> roles = getRoles(packageName, userId);
        java.util.Set<java.lang.String> authorities = new java.util.HashSet<>();
        for (java.lang.String role : roles) {
            authorities.add(ROLE_AUTHORITY_PREFIX + role);
        }
        return authorities.isEmpty() ? java.util.Set.of("default") : authorities;
    }

    private static java.util.Set<java.lang.String> getRoles(java.lang.String packageName, int userId) {
        com.android.role.RoleManagerLocal roleManagerLocal = (com.android.role.RoleManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.role.RoleManagerLocal.class);
        java.util.Set<java.lang.String> roles = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> rolesAndHolders = roleManagerLocal.getRolesAndHolders(userId);
        for (java.lang.String role : rolesAndHolders.keySet()) {
            if (rolesAndHolders.get(role).contains(packageName)) {
                roles.add(role);
            }
        }
        return roles;
    }

    private java.util.Set<java.lang.String> getAuthorities() {
        if (this.mAuthorities == null && this.mIsRoleAuthority) {
            this.mAuthorities = getRoleAuthoritiesOrDefault(this.mPackageName, this.mUserId);
        }
        return this.mAuthorities;
    }

    void reloadRoleAuthorities() {
        if (this.mIsRoleAuthority) {
            this.mAuthorities = getRoleAuthoritiesOrDefault(this.mPackageName, this.mUserId);
        }
    }

    boolean hasAuthority(java.lang.String authority) {
        return getAuthorities().contains(authority);
    }

    java.lang.String getPackageName() {
        return this.mPackageName;
    }

    int getUserId() {
        return this.mUserId;
    }

    public com.android.server.devicepolicy.ActiveAdmin getActiveAdmin() {
        return this.mActiveAdmin;
    }

    android.app.admin.EnforcingAdmin getParcelableAdmin() {
        android.app.admin.UnknownAuthority roleAuthority;
        if (this.mIsRoleAuthority) {
            java.util.Set<java.lang.String> roles = getRoles(this.mPackageName, this.mUserId);
            if (roles.isEmpty()) {
                roleAuthority = android.app.admin.UnknownAuthority.UNKNOWN_AUTHORITY;
            } else {
                roleAuthority = new android.app.admin.RoleAuthority(roles);
            }
        } else if (this.mAuthorities.contains(DPC_AUTHORITY)) {
            roleAuthority = android.app.admin.DpcAuthority.DPC_AUTHORITY;
        } else if (this.mAuthorities.contains(DEVICE_ADMIN_AUTHORITY)) {
            roleAuthority = android.app.admin.DeviceAdminAuthority.DEVICE_ADMIN_AUTHORITY;
        } else {
            roleAuthority = android.app.admin.UnknownAuthority.UNKNOWN_AUTHORITY;
        }
        return new android.app.admin.EnforcingAdmin(this.mPackageName, roleAuthority, android.os.UserHandle.of(this.mUserId), this.mComponentName);
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.devicepolicy.EnforcingAdmin other = (com.android.server.devicepolicy.EnforcingAdmin) o;
        if (java.util.Objects.equals(this.mPackageName, other.mPackageName) && java.util.Objects.equals(this.mComponentName, other.mComponentName) && java.util.Objects.equals(java.lang.Boolean.valueOf(this.mIsRoleAuthority), java.lang.Boolean.valueOf(other.mIsRoleAuthority)) && hasMatchingAuthorities(this, other)) {
            return true;
        }
        return false;
    }

    private static boolean hasMatchingAuthorities(com.android.server.devicepolicy.EnforcingAdmin admin1, com.android.server.devicepolicy.EnforcingAdmin admin2) {
        if (admin1.mIsRoleAuthority && admin2.mIsRoleAuthority) {
            return true;
        }
        return admin1.getAuthorities().equals(admin2.getAuthorities());
    }

    public int hashCode() {
        if (this.mIsRoleAuthority) {
            return java.util.Objects.hash(this.mPackageName, java.lang.Integer.valueOf(this.mUserId));
        }
        return java.util.Objects.hash(this.mComponentName == null ? this.mPackageName : this.mComponentName, java.lang.Integer.valueOf(this.mUserId), getAuthorities());
    }

    void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        serializer.attribute((java.lang.String) null, ATTR_PACKAGE_NAME, this.mPackageName);
        serializer.attributeBoolean((java.lang.String) null, ATTR_IS_ROLE, this.mIsRoleAuthority);
        serializer.attributeInt((java.lang.String) null, ATTR_USER_ID, this.mUserId);
        if (!this.mIsRoleAuthority) {
            if (this.mComponentName != null) {
                serializer.attribute((java.lang.String) null, ATTR_CLASS_NAME, this.mComponentName.getClassName());
            }
            serializer.attribute((java.lang.String) null, ATTR_AUTHORITIES, java.lang.String.join(ATTR_AUTHORITIES_SEPARATOR, getAuthorities()));
        }
    }

    static com.android.server.devicepolicy.EnforcingAdmin readFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException {
        java.lang.String packageName = parser.getAttributeValue((java.lang.String) null, ATTR_PACKAGE_NAME);
        boolean isRoleAuthority = parser.getAttributeBoolean((java.lang.String) null, ATTR_IS_ROLE);
        java.lang.String authoritiesStr = parser.getAttributeValue((java.lang.String) null, ATTR_AUTHORITIES);
        int userId = parser.getAttributeInt((java.lang.String) null, ATTR_USER_ID);
        if (isRoleAuthority) {
            if (packageName == null) {
                com.android.server.utils.Slogf.wtf(TAG, "Error parsing EnforcingAdmin with RoleAuthority, packageName is null.");
                return null;
            }
            return new com.android.server.devicepolicy.EnforcingAdmin(packageName, userId, null);
        }
        if (packageName == null || authoritiesStr == null) {
            com.android.server.utils.Slogf.wtf(TAG, "Error parsing EnforcingAdmin, packageName is " + (packageName == null ? "null" : packageName) + ", and authorities is " + (authoritiesStr != null ? authoritiesStr : "null") + ".");
            return null;
        }
        java.lang.String className = parser.getAttributeValue((java.lang.String) null, ATTR_CLASS_NAME);
        android.content.ComponentName componentName = className != null ? new android.content.ComponentName(packageName, className) : null;
        java.util.Set<java.lang.String> authorities = java.util.Set.of((java.lang.Object[]) authoritiesStr.split(ATTR_AUTHORITIES_SEPARATOR));
        return new com.android.server.devicepolicy.EnforcingAdmin(packageName, componentName, authorities, userId, null);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("EnforcingAdmin { mPackageName= ");
        sb.append(this.mPackageName);
        if (this.mComponentName != null) {
            sb.append(", mComponentName= ");
            sb.append(this.mComponentName);
        }
        if (this.mAuthorities != null) {
            sb.append(", mAuthorities= ");
            sb.append(this.mAuthorities);
        }
        sb.append(", mUserId= ");
        sb.append(this.mUserId);
        sb.append(", mIsRoleAuthority= ");
        sb.append(this.mIsRoleAuthority);
        sb.append(" }");
        return sb.toString();
    }
}
