package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
class Owners {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "DevicePolicyManagerService";
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;
    private final com.android.server.devicepolicy.OwnersData mData;
    private final com.android.server.devicepolicy.DeviceStateCacheImpl mDeviceStateCache;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private boolean mSystemReady;
    private final android.os.UserManager mUserManager;
    private final com.android.server.pm.UserManagerInternal mUserManagerInternal;

    Owners(android.os.UserManager userManager, com.android.server.pm.UserManagerInternal userManagerInternal, android.content.pm.PackageManagerInternal packageManagerInternal, com.android.server.wm.ActivityTaskManagerInternal activityTaskManagerInternal, android.app.ActivityManagerInternal activityManagerInternal, com.android.server.devicepolicy.DeviceStateCacheImpl deviceStateCache, com.android.server.devicepolicy.PolicyPathProvider pathProvider) {
        this.mUserManager = userManager;
        this.mUserManagerInternal = userManagerInternal;
        this.mPackageManagerInternal = packageManagerInternal;
        this.mActivityTaskManagerInternal = activityTaskManagerInternal;
        this.mActivityManagerInternal = activityManagerInternal;
        this.mDeviceStateCache = deviceStateCache;
        this.mData = new com.android.server.devicepolicy.OwnersData(pathProvider);
    }

    void load() {
        synchronized (this.mData) {
            int[] usersIds = this.mUserManager.getAliveUsers().stream().mapToInt(new java.util.function.ToIntFunction() { // from class: com.android.server.devicepolicy.Owners$$ExternalSyntheticLambda0
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(java.lang.Object obj) {
                    return ((android.content.pm.UserInfo) obj).id;
                }
            }).toArray();
            this.mData.load(usersIds);
            int i = 0;
            if (android.provider.DeviceConfig.getBoolean("device_policy_manager", "deprecate_usermanagerinternal_devicepolicy", true)) {
                if (hasDeviceOwner()) {
                    int deviceOwnerType = this.mData.mDeviceOwnerTypes.getOrDefault(this.mData.mDeviceOwner.packageName, 0).intValue();
                    this.mDeviceStateCache.setDeviceOwnerType(deviceOwnerType);
                } else {
                    this.mDeviceStateCache.setDeviceOwnerType(-1);
                }
                int length = usersIds.length;
                while (i < length) {
                    int userId = usersIds[i];
                    this.mDeviceStateCache.setHasProfileOwner(userId, hasProfileOwner(userId));
                    i++;
                }
            } else {
                this.mUserManagerInternal.setDeviceManaged(hasDeviceOwner());
                int length2 = usersIds.length;
                while (i < length2) {
                    int userId2 = usersIds[i];
                    this.mUserManagerInternal.setUserManaged(userId2, hasProfileOwner(userId2));
                    i++;
                }
            }
            notifyChangeLocked();
            pushDeviceOwnerUidToActivityTaskManagerLocked();
            pushProfileOwnerUidsToActivityTaskManagerLocked();
        }
    }

    private void notifyChangeLocked() {
        pushToDevicePolicyManager();
        pushToPackageManagerLocked();
        pushToActivityManagerLocked();
        pushToAppOpsLocked();
    }

    private void pushToDevicePolicyManager() {
        com.android.server.devicepolicy.DevicePolicyManagerService.invalidateBinderCaches();
    }

    private void pushToPackageManagerLocked() {
        android.util.SparseArray<java.lang.String> po = new android.util.SparseArray<>();
        for (int i = this.mData.mProfileOwners.size() - 1; i >= 0; i--) {
            po.put(this.mData.mProfileOwners.keyAt(i).intValue(), this.mData.mProfileOwners.valueAt(i).packageName);
        }
        java.lang.String doPackage = this.mData.mDeviceOwner != null ? this.mData.mDeviceOwner.packageName : null;
        this.mPackageManagerInternal.setDeviceAndProfileOwnerPackages(this.mData.mDeviceOwnerUserId, doPackage, po);
    }

    private void pushDeviceOwnerUidToActivityTaskManagerLocked() {
        this.mActivityTaskManagerInternal.setDeviceOwnerUid(getDeviceOwnerUidLocked());
    }

    private void pushProfileOwnerUidsToActivityTaskManagerLocked() {
        this.mActivityTaskManagerInternal.setProfileOwnerUids(getProfileOwnerUidsLocked());
    }

    private void pushToActivityManagerLocked() {
        this.mActivityManagerInternal.setDeviceOwnerUid(getDeviceOwnerUidLocked());
        android.util.ArraySet<java.lang.Integer> profileOwners = new android.util.ArraySet<>();
        for (int poi = this.mData.mProfileOwners.size() - 1; poi >= 0; poi--) {
            int userId = this.mData.mProfileOwners.keyAt(poi).intValue();
            int profileOwnerUid = this.mPackageManagerInternal.getPackageUid(this.mData.mProfileOwners.valueAt(poi).packageName, 4333568L, userId);
            if (profileOwnerUid >= 0) {
                profileOwners.add(java.lang.Integer.valueOf(profileOwnerUid));
            }
        }
        this.mActivityManagerInternal.setProfileOwnerUid(profileOwners);
    }

    int getDeviceOwnerUidLocked() {
        if (this.mData.mDeviceOwner != null) {
            return this.mPackageManagerInternal.getPackageUid(this.mData.mDeviceOwner.packageName, 4333568L, this.mData.mDeviceOwnerUserId);
        }
        return -1;
    }

    java.util.Set<java.lang.Integer> getProfileOwnerUidsLocked() {
        java.util.Set<java.lang.Integer> uids = new android.util.ArraySet<>();
        for (int i = 0; i < this.mData.mProfileOwners.size(); i++) {
            int userId = this.mData.mProfileOwners.keyAt(i).intValue();
            com.android.server.devicepolicy.OwnersData.OwnerInfo info = this.mData.mProfileOwners.valueAt(i);
            uids.add(java.lang.Integer.valueOf(this.mPackageManagerInternal.getPackageUid(info.packageName, 4333568L, userId)));
        }
        return uids;
    }

    java.lang.String getDeviceOwnerPackageName() {
        java.lang.String str;
        synchronized (this.mData) {
            str = this.mData.mDeviceOwner != null ? this.mData.mDeviceOwner.packageName : null;
        }
        return str;
    }

    int getDeviceOwnerUserId() {
        int i;
        synchronized (this.mData) {
            i = this.mData.mDeviceOwnerUserId;
        }
        return i;
    }

    android.util.Pair<java.lang.Integer, android.content.ComponentName> getDeviceOwnerUserIdAndComponent() {
        synchronized (this.mData) {
            if (this.mData.mDeviceOwner == null) {
                return null;
            }
            return android.util.Pair.create(java.lang.Integer.valueOf(this.mData.mDeviceOwnerUserId), this.mData.mDeviceOwner.admin);
        }
    }

    android.content.ComponentName getDeviceOwnerComponent() {
        android.content.ComponentName componentName;
        synchronized (this.mData) {
            componentName = this.mData.mDeviceOwner != null ? this.mData.mDeviceOwner.admin : null;
        }
        return componentName;
    }

    java.lang.String getDeviceOwnerRemoteBugreportUri() {
        java.lang.String str;
        synchronized (this.mData) {
            str = this.mData.mDeviceOwner != null ? this.mData.mDeviceOwner.remoteBugreportUri : null;
        }
        return str;
    }

    java.lang.String getDeviceOwnerRemoteBugreportHash() {
        java.lang.String str;
        synchronized (this.mData) {
            str = this.mData.mDeviceOwner != null ? this.mData.mDeviceOwner.remoteBugreportHash : null;
        }
        return str;
    }

    void setDeviceOwner(android.content.ComponentName admin, int userId) {
        if (userId < 0) {
            android.util.Slog.e(TAG, "Invalid user id for device owner user: " + userId);
            return;
        }
        synchronized (this.mData) {
            this.mData.mDeviceOwner = new com.android.server.devicepolicy.OwnersData.OwnerInfo(admin, null, null, true);
            this.mData.mDeviceOwnerUserId = userId;
            if (android.provider.DeviceConfig.getBoolean("device_policy_manager", "deprecate_usermanagerinternal_devicepolicy", true)) {
                int deviceOwnerType = this.mData.mDeviceOwnerTypes.getOrDefault(this.mData.mDeviceOwner.packageName, 0).intValue();
                this.mDeviceStateCache.setDeviceOwnerType(deviceOwnerType);
            } else {
                this.mUserManagerInternal.setDeviceManaged(true);
            }
            notifyChangeLocked();
            pushDeviceOwnerUidToActivityTaskManagerLocked();
        }
    }

    void clearDeviceOwner() {
        synchronized (this.mData) {
            this.mData.mDeviceOwnerTypes.remove(this.mData.mDeviceOwner.packageName);
            this.mData.mDeviceOwner = null;
            this.mData.mDeviceOwnerUserId = -10000;
            if (android.provider.DeviceConfig.getBoolean("device_policy_manager", "deprecate_usermanagerinternal_devicepolicy", true)) {
                this.mDeviceStateCache.setDeviceOwnerType(-1);
            } else {
                this.mUserManagerInternal.setDeviceManaged(false);
            }
            notifyChangeLocked();
            pushDeviceOwnerUidToActivityTaskManagerLocked();
        }
    }

    void setProfileOwner(android.content.ComponentName admin, int userId) {
        synchronized (this.mData) {
            this.mData.mProfileOwners.put(java.lang.Integer.valueOf(userId), new com.android.server.devicepolicy.OwnersData.OwnerInfo(admin, null, null, false));
            if (android.provider.DeviceConfig.getBoolean("device_policy_manager", "deprecate_usermanagerinternal_devicepolicy", true)) {
                this.mDeviceStateCache.setHasProfileOwner(userId, true);
            } else {
                this.mUserManagerInternal.setUserManaged(userId, true);
            }
            notifyChangeLocked();
            pushProfileOwnerUidsToActivityTaskManagerLocked();
        }
    }

    void removeProfileOwner(int userId) {
        synchronized (this.mData) {
            this.mData.mProfileOwners.remove(java.lang.Integer.valueOf(userId));
            if (android.provider.DeviceConfig.getBoolean("device_policy_manager", "deprecate_usermanagerinternal_devicepolicy", true)) {
                this.mDeviceStateCache.setHasProfileOwner(userId, false);
            } else {
                this.mUserManagerInternal.setUserManaged(userId, false);
            }
            notifyChangeLocked();
            pushProfileOwnerUidsToActivityTaskManagerLocked();
        }
    }

    void transferProfileOwner(android.content.ComponentName target, int userId) {
        synchronized (this.mData) {
            com.android.server.devicepolicy.OwnersData.OwnerInfo ownerInfo = this.mData.mProfileOwners.get(java.lang.Integer.valueOf(userId));
            com.android.server.devicepolicy.OwnersData.OwnerInfo newOwnerInfo = new com.android.server.devicepolicy.OwnersData.OwnerInfo(target, ownerInfo.remoteBugreportUri, ownerInfo.remoteBugreportHash, ownerInfo.isOrganizationOwnedDevice);
            this.mData.mProfileOwners.put(java.lang.Integer.valueOf(userId), newOwnerInfo);
            notifyChangeLocked();
            pushProfileOwnerUidsToActivityTaskManagerLocked();
        }
    }

    void transferDeviceOwnership(android.content.ComponentName target) {
        synchronized (this.mData) {
            java.lang.Integer previousDeviceOwnerType = this.mData.mDeviceOwnerTypes.remove(this.mData.mDeviceOwner.packageName);
            this.mData.mDeviceOwner = new com.android.server.devicepolicy.OwnersData.OwnerInfo(target, this.mData.mDeviceOwner.remoteBugreportUri, this.mData.mDeviceOwner.remoteBugreportHash, this.mData.mDeviceOwner.isOrganizationOwnedDevice);
            if (previousDeviceOwnerType != null) {
                this.mData.mDeviceOwnerTypes.put(this.mData.mDeviceOwner.packageName, previousDeviceOwnerType);
            }
            notifyChangeLocked();
            pushDeviceOwnerUidToActivityTaskManagerLocked();
        }
    }

    android.content.ComponentName getProfileOwnerComponent(int userId) {
        android.content.ComponentName componentName;
        synchronized (this.mData) {
            com.android.server.devicepolicy.OwnersData.OwnerInfo profileOwner = this.mData.mProfileOwners.get(java.lang.Integer.valueOf(userId));
            componentName = profileOwner != null ? profileOwner.admin : null;
        }
        return componentName;
    }

    java.lang.String getProfileOwnerPackage(int userId) {
        java.lang.String str;
        synchronized (this.mData) {
            com.android.server.devicepolicy.OwnersData.OwnerInfo profileOwner = this.mData.mProfileOwners.get(java.lang.Integer.valueOf(userId));
            str = profileOwner != null ? profileOwner.packageName : null;
        }
        return str;
    }

    boolean isProfileOwnerOfOrganizationOwnedDevice(int userId) {
        boolean z;
        synchronized (this.mData) {
            com.android.server.devicepolicy.OwnersData.OwnerInfo profileOwner = this.mData.mProfileOwners.get(java.lang.Integer.valueOf(userId));
            z = profileOwner != null ? profileOwner.isOrganizationOwnedDevice : false;
        }
        return z;
    }

    java.util.Set<java.lang.Integer> getProfileOwnerKeys() {
        java.util.Set<java.lang.Integer> setKeySet;
        synchronized (this.mData) {
            setKeySet = this.mData.mProfileOwners.keySet();
        }
        return setKeySet;
    }

    java.util.List<com.android.server.devicepolicy.OwnerShellData> listAllOwners() {
        java.util.List<com.android.server.devicepolicy.OwnerShellData> owners = new java.util.ArrayList<>();
        synchronized (this.mData) {
            if (this.mData.mDeviceOwner != null) {
                owners.add(com.android.server.devicepolicy.OwnerShellData.forDeviceOwner(this.mData.mDeviceOwnerUserId, this.mData.mDeviceOwner.admin));
            }
            for (int i = 0; i < this.mData.mProfileOwners.size(); i++) {
                int userId = this.mData.mProfileOwners.keyAt(i).intValue();
                com.android.server.devicepolicy.OwnersData.OwnerInfo info = this.mData.mProfileOwners.valueAt(i);
                owners.add(com.android.server.devicepolicy.OwnerShellData.forUserProfileOwner(userId, info.admin));
            }
        }
        return owners;
    }

    android.app.admin.SystemUpdatePolicy getSystemUpdatePolicy() {
        android.app.admin.SystemUpdatePolicy systemUpdatePolicy;
        synchronized (this.mData) {
            systemUpdatePolicy = this.mData.mSystemUpdatePolicy;
        }
        return systemUpdatePolicy;
    }

    void setSystemUpdatePolicy(android.app.admin.SystemUpdatePolicy systemUpdatePolicy) {
        synchronized (this.mData) {
            this.mData.mSystemUpdatePolicy = systemUpdatePolicy;
        }
    }

    void clearSystemUpdatePolicy() {
        synchronized (this.mData) {
            this.mData.mSystemUpdatePolicy = null;
        }
    }

    android.util.Pair<java.time.LocalDate, java.time.LocalDate> getSystemUpdateFreezePeriodRecord() {
        android.util.Pair<java.time.LocalDate, java.time.LocalDate> pair;
        synchronized (this.mData) {
            pair = new android.util.Pair<>(this.mData.mSystemUpdateFreezeStart, this.mData.mSystemUpdateFreezeEnd);
        }
        return pair;
    }

    java.lang.String getSystemUpdateFreezePeriodRecordAsString() {
        java.lang.String systemUpdateFreezePeriodRecordAsString;
        synchronized (this.mData) {
            systemUpdateFreezePeriodRecordAsString = this.mData.getSystemUpdateFreezePeriodRecordAsString();
        }
        return systemUpdateFreezePeriodRecordAsString;
    }

    boolean setSystemUpdateFreezePeriodRecord(java.time.LocalDate start, java.time.LocalDate end) {
        boolean changed = false;
        synchronized (this.mData) {
            if (!java.util.Objects.equals(this.mData.mSystemUpdateFreezeStart, start)) {
                this.mData.mSystemUpdateFreezeStart = start;
                changed = true;
            }
            if (!java.util.Objects.equals(this.mData.mSystemUpdateFreezeEnd, end)) {
                this.mData.mSystemUpdateFreezeEnd = end;
                changed = true;
            }
        }
        return changed;
    }

    boolean hasDeviceOwner() {
        boolean z;
        synchronized (this.mData) {
            z = this.mData.mDeviceOwner != null;
        }
        return z;
    }

    boolean isDeviceOwnerUserId(int userId) {
        boolean z;
        synchronized (this.mData) {
            z = this.mData.mDeviceOwner != null && this.mData.mDeviceOwnerUserId == userId;
        }
        return z;
    }

    boolean isDefaultDeviceOwnerUserId(int userId) {
        boolean z;
        synchronized (this.mData) {
            z = this.mData.mDeviceOwner != null && this.mData.mDeviceOwnerUserId == userId && getDeviceOwnerType(getDeviceOwnerPackageName()) == 0;
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x001b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean isFinancedDeviceOwnerUserId(int r4) {
        /*
            r3 = this;
            com.android.server.devicepolicy.OwnersData r0 = r3.mData
            monitor-enter(r0)
            com.android.server.devicepolicy.OwnersData r1 = r3.mData     // Catch: java.lang.Throwable -> L1e
            com.android.server.devicepolicy.OwnersData$OwnerInfo r1 = r1.mDeviceOwner     // Catch: java.lang.Throwable -> L1e
            if (r1 == 0) goto L1b
            com.android.server.devicepolicy.OwnersData r1 = r3.mData     // Catch: java.lang.Throwable -> L1e
            int r1 = r1.mDeviceOwnerUserId     // Catch: java.lang.Throwable -> L1e
            if (r1 != r4) goto L1b
            java.lang.String r1 = r3.getDeviceOwnerPackageName()     // Catch: java.lang.Throwable -> L1e
            int r1 = r3.getDeviceOwnerType(r1)     // Catch: java.lang.Throwable -> L1e
            r2 = 1
            if (r1 != r2) goto L1b
            goto L1c
        L1b:
            r2 = 0
        L1c:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L1e
            return r2
        L1e:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L1e
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.Owners.isFinancedDeviceOwnerUserId(int):boolean");
    }

    boolean hasProfileOwner(int userId) {
        boolean z;
        synchronized (this.mData) {
            z = getProfileOwnerComponent(userId) != null;
        }
        return z;
    }

    void setDeviceOwnerRemoteBugreportUriAndHash(java.lang.String remoteBugreportUri, java.lang.String remoteBugreportHash) {
        synchronized (this.mData) {
            if (this.mData.mDeviceOwner != null) {
                this.mData.mDeviceOwner.remoteBugreportUri = remoteBugreportUri;
                this.mData.mDeviceOwner.remoteBugreportHash = remoteBugreportHash;
            }
            writeDeviceOwner();
        }
    }

    void setProfileOwnerOfOrganizationOwnedDevice(int userId, boolean isOrganizationOwnedDevice) {
        synchronized (this.mData) {
            com.android.server.devicepolicy.OwnersData.OwnerInfo profileOwner = this.mData.mProfileOwners.get(java.lang.Integer.valueOf(userId));
            if (profileOwner != null) {
                profileOwner.isOrganizationOwnedDevice = isOrganizationOwnedDevice;
            } else {
                android.util.Slog.e(TAG, java.lang.String.format("No profile owner for user %d to set org-owned flag.", java.lang.Integer.valueOf(userId)));
            }
            writeProfileOwner(userId);
        }
    }

    void setDeviceOwnerType(java.lang.String packageName, int deviceOwnerType, boolean isAdminTestOnly) {
        synchronized (this.mData) {
            if (!hasDeviceOwner()) {
                android.util.Slog.e(TAG, "Attempting to set a device owner type when there is no device owner");
            } else if (!isAdminTestOnly && isDeviceOwnerTypeSetForDeviceOwner(packageName)) {
                android.util.Slog.e(TAG, "Setting the device owner type more than once is only allowed for test only admins");
            } else {
                this.mData.mDeviceOwnerTypes.put(packageName, java.lang.Integer.valueOf(deviceOwnerType));
                writeDeviceOwner();
            }
        }
    }

    int getDeviceOwnerType(java.lang.String packageName) {
        synchronized (this.mData) {
            if (!isDeviceOwnerTypeSetForDeviceOwner(packageName)) {
                return 0;
            }
            return this.mData.mDeviceOwnerTypes.get(packageName).intValue();
        }
    }

    boolean isDeviceOwnerTypeSetForDeviceOwner(java.lang.String packageName) {
        boolean z;
        synchronized (this.mData) {
            z = !this.mData.mDeviceOwnerTypes.isEmpty() && this.mData.mDeviceOwnerTypes.containsKey(packageName);
        }
        return z;
    }

    void writeDeviceOwner() {
        synchronized (this.mData) {
            pushToDevicePolicyManager();
            this.mData.writeDeviceOwner();
        }
    }

    void writeProfileOwner(int userId) {
        synchronized (this.mData) {
            pushToDevicePolicyManager();
            this.mData.writeProfileOwner(userId);
        }
    }

    boolean saveSystemUpdateInfo(android.app.admin.SystemUpdateInfo newInfo) {
        synchronized (this.mData) {
            if (java.util.Objects.equals(newInfo, this.mData.mSystemUpdateInfo)) {
                return false;
            }
            this.mData.mSystemUpdateInfo = newInfo;
            this.mData.writeDeviceOwner();
            return true;
        }
    }

    public android.app.admin.SystemUpdateInfo getSystemUpdateInfo() {
        android.app.admin.SystemUpdateInfo systemUpdateInfo;
        synchronized (this.mData) {
            systemUpdateInfo = this.mData.mSystemUpdateInfo;
        }
        return systemUpdateInfo;
    }

    void markMigrationToPolicyEngine() {
        synchronized (this.mData) {
            this.mData.mMigratedToPolicyEngine = true;
            this.mData.writeDeviceOwner();
        }
    }

    boolean isMigratedToPolicyEngine() {
        boolean z;
        synchronized (this.mData) {
            z = this.mData.mMigratedToPolicyEngine;
        }
        return z;
    }

    void markSecurityLoggingMigrated() {
        synchronized (this.mData) {
            this.mData.mSecurityLoggingMigrated = true;
            this.mData.writeDeviceOwner();
        }
    }

    void markPostUpgradeMigration() {
        synchronized (this.mData) {
            this.mData.mPoliciesMigratedPostUpdate = true;
            this.mData.writeDeviceOwner();
        }
    }

    boolean isSecurityLoggingMigrated() {
        boolean z;
        synchronized (this.mData) {
            z = this.mData.mSecurityLoggingMigrated;
        }
        return z;
    }

    boolean isRequiredPasswordComplexityMigrated() {
        boolean z;
        synchronized (this.mData) {
            z = this.mData.mRequiredPasswordComplexityMigrated;
        }
        return z;
    }

    void markRequiredPasswordComplexityMigrated() {
        synchronized (this.mData) {
            this.mData.mRequiredPasswordComplexityMigrated = true;
            this.mData.writeDeviceOwner();
        }
    }

    boolean isSuspendedPackagesMigrated() {
        boolean z;
        synchronized (this.mData) {
            z = this.mData.mSuspendedPackagesMigrated;
        }
        return z;
    }

    void markSuspendedPackagesMigrated() {
        synchronized (this.mData) {
            this.mData.mSuspendedPackagesMigrated = true;
            this.mData.writeDeviceOwner();
        }
    }

    boolean isMigratedPostUpdate() {
        boolean z;
        synchronized (this.mData) {
            z = this.mData.mPoliciesMigratedPostUpdate;
        }
        return z;
    }

    void pushToAppOpsLocked() {
        int uid;
        if (!this.mSystemReady) {
            return;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            android.util.SparseIntArray owners = new android.util.SparseIntArray();
            if (this.mData.mDeviceOwner != null && (uid = getDeviceOwnerUidLocked()) >= 0) {
                owners.put(this.mData.mDeviceOwnerUserId, uid);
            }
            if (this.mData.mProfileOwners != null) {
                for (int poi = this.mData.mProfileOwners.size() - 1; poi >= 0; poi--) {
                    int uid2 = this.mPackageManagerInternal.getPackageUid(this.mData.mProfileOwners.valueAt(poi).packageName, 4333568L, this.mData.mProfileOwners.keyAt(poi).intValue());
                    if (uid2 >= 0) {
                        owners.put(this.mData.mProfileOwners.keyAt(poi).intValue(), uid2);
                    }
                }
            }
            android.app.AppOpsManagerInternal appops = (android.app.AppOpsManagerInternal) com.android.server.LocalServices.getService(android.app.AppOpsManagerInternal.class);
            if (appops != null) {
                appops.setDeviceAndProfileOwners(owners.size() > 0 ? owners : null);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void systemReady() {
        synchronized (this.mData) {
            this.mSystemReady = true;
            pushToActivityManagerLocked();
            pushToAppOpsLocked();
        }
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this.mData) {
            this.mData.dump(pw);
        }
    }

    java.io.File getDeviceOwnerFile() {
        return this.mData.getDeviceOwnerFile();
    }

    java.io.File getProfileOwnerFile(int userId) {
        return this.mData.getProfileOwnerFile(userId);
    }
}
