package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public interface AccessCheckDelegate extends com.android.server.pm.permission.PermissionManagerServiceInternal.CheckPermissionDelegate, android.app.AppOpsManagerInternal.CheckOpsDelegate {
    void addOverridePermissionState(int i, int i2, java.lang.String str, int i3);

    void clearAllOverridePermissionStates();

    void clearOverridePermissionStates(int i);

    java.util.List<java.lang.String> getDelegatedPermissionNames();

    boolean hasDelegateOrOverrides();

    boolean hasOverriddenPermissions();

    boolean hasShellPermissionDelegate();

    boolean isDelegateAndOwnerUid(int i);

    boolean isDelegatePackage(int i, java.lang.String str);

    void removeOverridePermissionState(int i, java.lang.String str);

    void removeShellPermissionDelegate();

    void setShellPermissionDelegate(int i, java.lang.String str, java.lang.String[] strArr);

    public static class AccessCheckDelegateImpl implements com.android.server.pm.permission.AccessCheckDelegate {
        public static final java.lang.String SHELL_PKG = "com.android.shell";
        boolean mDelegateAllPermissions;
        private int mDelegateAndOwnerUid = -1;
        private java.lang.String mDelegatePackage;
        private java.lang.String[] mDelegatePermissions;
        private android.util.SparseArray<android.util.ArrayMap<java.lang.String, java.lang.Integer>> mOverridePermissionStates;

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public void setShellPermissionDelegate(int uid, java.lang.String packageName, java.lang.String[] permissions) {
            this.mDelegateAndOwnerUid = uid;
            this.mDelegatePackage = packageName;
            this.mDelegatePermissions = permissions;
            this.mDelegateAllPermissions = permissions == null;
            android.content.pm.PackageManager.invalidatePackageInfoCache();
        }

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public void removeShellPermissionDelegate() {
            this.mDelegatePackage = null;
            this.mDelegatePermissions = null;
            this.mDelegateAllPermissions = false;
            android.content.pm.PackageManager.invalidatePackageInfoCache();
        }

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public void addOverridePermissionState(int ownerUid, int uid, java.lang.String permission, int state) {
            android.util.ArrayMap<java.lang.String, java.lang.Integer> perUidOverrides;
            if (this.mOverridePermissionStates == null) {
                this.mDelegateAndOwnerUid = ownerUid;
                this.mOverridePermissionStates = new android.util.SparseArray<>();
            }
            int uidIdx = this.mOverridePermissionStates.indexOfKey(uid);
            if (uidIdx < 0) {
                perUidOverrides = new android.util.ArrayMap<>();
                this.mOverridePermissionStates.put(uid, perUidOverrides);
            } else {
                perUidOverrides = this.mOverridePermissionStates.valueAt(uidIdx);
            }
            perUidOverrides.put(permission, java.lang.Integer.valueOf(state));
            android.content.pm.PackageManager.invalidatePackageInfoCache();
        }

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public void removeOverridePermissionState(int uid, java.lang.String permission) {
            android.util.ArrayMap<java.lang.String, java.lang.Integer> perUidOverrides;
            if (this.mOverridePermissionStates == null || (perUidOverrides = this.mOverridePermissionStates.get(uid)) == null) {
                return;
            }
            perUidOverrides.remove(permission);
            android.content.pm.PackageManager.invalidatePackageInfoCache();
            if (perUidOverrides.isEmpty()) {
                this.mOverridePermissionStates.remove(uid);
            }
            if (this.mOverridePermissionStates.size() == 0) {
                this.mOverridePermissionStates = null;
            }
        }

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public void clearOverridePermissionStates(int uid) {
            if (this.mOverridePermissionStates == null) {
                return;
            }
            this.mOverridePermissionStates.remove(uid);
            android.content.pm.PackageManager.invalidatePackageInfoCache();
            if (this.mOverridePermissionStates.size() == 0) {
                this.mOverridePermissionStates = null;
            }
        }

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public void clearAllOverridePermissionStates() {
            this.mOverridePermissionStates = null;
            android.content.pm.PackageManager.invalidatePackageInfoCache();
        }

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public java.util.List<java.lang.String> getDelegatedPermissionNames() {
            if (this.mDelegatePermissions == null) {
                return null;
            }
            return java.util.List.of((java.lang.Object[]) this.mDelegatePermissions);
        }

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public boolean hasShellPermissionDelegate() {
            return this.mDelegateAllPermissions || this.mDelegatePermissions != null;
        }

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public boolean isDelegatePackage(int uid, java.lang.String packageName) {
            return this.mDelegateAndOwnerUid == uid && android.text.TextUtils.equals(this.mDelegatePackage, packageName);
        }

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public boolean hasOverriddenPermissions() {
            return this.mOverridePermissionStates != null;
        }

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public boolean isDelegateAndOwnerUid(int uid) {
            return uid == this.mDelegateAndOwnerUid;
        }

        @Override // com.android.server.pm.permission.AccessCheckDelegate
        public boolean hasDelegateOrOverrides() {
            return hasShellPermissionDelegate() || hasOverriddenPermissions();
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal.CheckPermissionDelegate
        public int checkPermission(java.lang.String packageName, java.lang.String permissionName, java.lang.String persistentDeviceId, int userId, com.android.internal.util.function.QuadFunction<java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer> superImpl) {
            int uid;
            java.util.Map<java.lang.String, java.lang.Integer> permissionGrants;
            if (android.text.TextUtils.equals(this.mDelegatePackage, packageName) && !"com.android.shell".equals(packageName) && isDelegatePermission(permissionName)) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return checkPermission("com.android.shell", permissionName, persistentDeviceId, userId, superImpl);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            if (this.mOverridePermissionStates != null && (uid = ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).getPackageUid(packageName, 0L, userId)) >= 0 && (permissionGrants = this.mOverridePermissionStates.get(uid)) != null && permissionGrants.containsKey(permissionName)) {
                return permissionGrants.get(permissionName).intValue();
            }
            return ((java.lang.Integer) superImpl.apply(packageName, permissionName, persistentDeviceId, java.lang.Integer.valueOf(userId))).intValue();
        }

        @Override // com.android.server.pm.permission.PermissionManagerServiceInternal.CheckPermissionDelegate
        public int checkUidPermission(int uid, java.lang.String permissionName, java.lang.String persistentDeviceId, com.android.internal.util.function.TriFunction<java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer> superImpl) {
            java.util.Map<java.lang.String, java.lang.Integer> permissionGrants;
            if (uid == this.mDelegateAndOwnerUid && uid != 2000 && isDelegatePermission(permissionName)) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return checkUidPermission(2000, permissionName, persistentDeviceId, superImpl);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            if (this.mOverridePermissionStates != null && (permissionGrants = this.mOverridePermissionStates.get(uid)) != null && permissionGrants.containsKey(permissionName)) {
                return permissionGrants.get(permissionName).intValue();
            }
            return ((java.lang.Integer) superImpl.apply(java.lang.Integer.valueOf(uid), permissionName, persistentDeviceId)).intValue();
        }

        public int checkOperation(int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean raw, com.android.internal.util.function.HexFunction<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Boolean, java.lang.Integer> superImpl) {
            if (uid == this.mDelegateAndOwnerUid && isDelegateOp(code)) {
                int shellUid = android.os.UserHandle.getUid(android.os.UserHandle.getUserId(uid), 2000);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return ((java.lang.Integer) superImpl.apply(java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(shellUid), "com.android.shell", (java.lang.Object) null, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Boolean.valueOf(raw))).intValue();
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            return ((java.lang.Integer) superImpl.apply(java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(uid), packageName, attributionTag, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Boolean.valueOf(raw))).intValue();
        }

        public int checkAudioOperation(int code, int usage, int uid, java.lang.String packageName, com.android.internal.util.function.QuadFunction<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer> superImpl) {
            if (uid == this.mDelegateAndOwnerUid && isDelegateOp(code)) {
                int shellUid = android.os.UserHandle.getUid(android.os.UserHandle.getUserId(uid), 2000);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return ((java.lang.Integer) superImpl.apply(java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(usage), java.lang.Integer.valueOf(shellUid), "com.android.shell")).intValue();
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            return ((java.lang.Integer) superImpl.apply(java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(usage), java.lang.Integer.valueOf(uid), packageName)).intValue();
        }

        public android.app.SyncNotedAppOp noteOperation(int code, int uid, java.lang.String packageName, java.lang.String featureId, int virtualDeviceId, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, com.android.internal.util.function.OctFunction<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Boolean, java.lang.String, java.lang.Boolean, android.app.SyncNotedAppOp> superImpl) {
            if (uid == this.mDelegateAndOwnerUid && isDelegateOp(code)) {
                int shellUid = android.os.UserHandle.getUid(android.os.UserHandle.getUserId(uid), 2000);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return (android.app.SyncNotedAppOp) superImpl.apply(java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(shellUid), "com.android.shell", featureId, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage));
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            return (android.app.SyncNotedAppOp) superImpl.apply(java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(uid), packageName, featureId, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage));
        }

        public android.app.SyncNotedAppOp noteProxyOperation(int code, android.content.AttributionSource attributionSource, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skiProxyOperation, com.android.internal.util.function.HexFunction<java.lang.Integer, android.content.AttributionSource, java.lang.Boolean, java.lang.String, java.lang.Boolean, java.lang.Boolean, android.app.SyncNotedAppOp> superImpl) {
            if (attributionSource.getUid() == this.mDelegateAndOwnerUid && isDelegateOp(code)) {
                int shellUid = android.os.UserHandle.getUid(android.os.UserHandle.getUserId(attributionSource.getUid()), 2000);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return (android.app.SyncNotedAppOp) superImpl.apply(java.lang.Integer.valueOf(code), new android.content.AttributionSource(shellUid, -1, "com.android.shell", attributionSource.getAttributionTag(), attributionSource.getToken(), null, attributionSource.getDeviceId(), attributionSource.getNext()), java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage), java.lang.Boolean.valueOf(skiProxyOperation));
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            return (android.app.SyncNotedAppOp) superImpl.apply(java.lang.Integer.valueOf(code), attributionSource, java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage), java.lang.Boolean.valueOf(skiProxyOperation));
        }

        public android.app.SyncNotedAppOp startOperation(android.os.IBinder token, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, int attributionFlags, int attributionChainId, com.android.internal.util.function.DodecFunction<android.os.IBinder, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Boolean, java.lang.Boolean, java.lang.String, java.lang.Boolean, java.lang.Integer, java.lang.Integer, android.app.SyncNotedAppOp> superImpl) {
            if (uid == this.mDelegateAndOwnerUid && isDelegateOp(code)) {
                int shellUid = android.os.UserHandle.getUid(android.os.UserHandle.getUserId(uid), 2000);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return (android.app.SyncNotedAppOp) superImpl.apply(token, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(shellUid), "com.android.shell", attributionTag, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Boolean.valueOf(startIfModeDefault), java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage), java.lang.Integer.valueOf(attributionFlags), java.lang.Integer.valueOf(attributionChainId));
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            return (android.app.SyncNotedAppOp) superImpl.apply(token, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(uid), packageName, attributionTag, java.lang.Integer.valueOf(virtualDeviceId), java.lang.Boolean.valueOf(startIfModeDefault), java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage), java.lang.Integer.valueOf(attributionFlags), java.lang.Integer.valueOf(attributionChainId));
        }

        public android.app.SyncNotedAppOp startProxyOperation(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean startIfModeDefault, boolean shouldCollectAsyncNotedOp, java.lang.String message, boolean shouldCollectMessage, boolean skipProxyOperation, int proxyAttributionFlags, int proxiedAttributionFlags, int attributionChainId, com.android.internal.util.function.UndecFunction<android.os.IBinder, java.lang.Integer, android.content.AttributionSource, java.lang.Boolean, java.lang.Boolean, java.lang.String, java.lang.Boolean, java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.Integer, android.app.SyncNotedAppOp> superImpl) {
            if (attributionSource.getUid() == this.mDelegateAndOwnerUid && isDelegateOp(code)) {
                int shellUid = android.os.UserHandle.getUid(android.os.UserHandle.getUserId(attributionSource.getUid()), 2000);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    return (android.app.SyncNotedAppOp) superImpl.apply(clientId, java.lang.Integer.valueOf(code), new android.content.AttributionSource(shellUid, -1, "com.android.shell", attributionSource.getAttributionTag(), attributionSource.getToken(), null, attributionSource.getDeviceId(), attributionSource.getNext()), java.lang.Boolean.valueOf(startIfModeDefault), java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage), java.lang.Boolean.valueOf(skipProxyOperation), java.lang.Integer.valueOf(proxyAttributionFlags), java.lang.Integer.valueOf(proxiedAttributionFlags), java.lang.Integer.valueOf(attributionChainId));
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            return (android.app.SyncNotedAppOp) superImpl.apply(clientId, java.lang.Integer.valueOf(code), attributionSource, java.lang.Boolean.valueOf(startIfModeDefault), java.lang.Boolean.valueOf(shouldCollectAsyncNotedOp), message, java.lang.Boolean.valueOf(shouldCollectMessage), java.lang.Boolean.valueOf(skipProxyOperation), java.lang.Integer.valueOf(proxyAttributionFlags), java.lang.Integer.valueOf(proxiedAttributionFlags), java.lang.Integer.valueOf(attributionChainId));
        }

        public void finishProxyOperation(android.os.IBinder clientId, int code, android.content.AttributionSource attributionSource, boolean skipProxyOperation, com.android.internal.util.function.QuadFunction<android.os.IBinder, java.lang.Integer, android.content.AttributionSource, java.lang.Boolean, java.lang.Void> superImpl) {
            if (attributionSource.getUid() == this.mDelegateAndOwnerUid && isDelegateOp(code)) {
                int shellUid = android.os.UserHandle.getUid(android.os.UserHandle.getUserId(attributionSource.getUid()), 2000);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    superImpl.apply(clientId, java.lang.Integer.valueOf(code), new android.content.AttributionSource(shellUid, -1, "com.android.shell", attributionSource.getAttributionTag(), attributionSource.getToken(), null, attributionSource.getDeviceId(), attributionSource.getNext()), java.lang.Boolean.valueOf(skipProxyOperation));
                    return;
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            superImpl.apply(clientId, java.lang.Integer.valueOf(code), attributionSource, java.lang.Boolean.valueOf(skipProxyOperation));
        }

        public void finishOperation(android.os.IBinder clientId, int code, int uid, java.lang.String packageName, java.lang.String attributionTag, int virtualDeviceId, com.android.internal.util.function.HexConsumer<android.os.IBinder, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer> superImpl) {
            if (uid == this.mDelegateAndOwnerUid && isDelegateOp(code)) {
                int shellUid = android.os.UserHandle.getUid(android.os.UserHandle.getUserId(uid), 2000);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    superImpl.accept(clientId, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(shellUid), "com.android.shell", attributionTag, java.lang.Integer.valueOf(virtualDeviceId));
                    return;
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
            superImpl.accept(clientId, java.lang.Integer.valueOf(code), java.lang.Integer.valueOf(uid), packageName, attributionTag, java.lang.Integer.valueOf(virtualDeviceId));
        }

        private boolean isDelegatePermission(java.lang.String permission) {
            return this.mDelegateAndOwnerUid != -1 && (this.mDelegateAllPermissions || com.android.internal.util.ArrayUtils.contains(this.mDelegatePermissions, permission));
        }

        private boolean isDelegateOp(int code) {
            java.lang.String permission;
            if (this.mDelegateAllPermissions || (permission = android.app.AppOpsManager.opToPermission(code)) == null) {
                return true;
            }
            return isDelegatePermission(permission);
        }
    }
}
