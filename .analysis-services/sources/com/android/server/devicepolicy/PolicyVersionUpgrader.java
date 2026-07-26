package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public class PolicyVersionUpgrader {
    private static final java.lang.String LOG_TAG = "DevicePolicyManager";
    private static final boolean VERBOSE_LOG = false;
    private final com.android.server.devicepolicy.PolicyPathProvider mPathProvider;
    private final com.android.server.devicepolicy.PolicyUpgraderDataProvider mProvider;

    PolicyVersionUpgrader(com.android.server.devicepolicy.PolicyUpgraderDataProvider provider, com.android.server.devicepolicy.PolicyPathProvider pathProvider) {
        this.mProvider = provider;
        this.mPathProvider = pathProvider;
    }

    public void upgradePolicy(int dpmsVersion) {
        int oldVersion = readVersion();
        if (oldVersion >= dpmsVersion) {
            android.util.Slog.i(LOG_TAG, java.lang.String.format("Current version %d, latest version %d, not upgrading.", java.lang.Integer.valueOf(oldVersion), java.lang.Integer.valueOf(dpmsVersion)));
            return;
        }
        int[] allUsers = this.mProvider.getUsersForUpgrade();
        com.android.server.devicepolicy.OwnersData ownersData = loadOwners(allUsers);
        android.util.SparseArray<com.android.server.devicepolicy.DevicePolicyData> allUsersData = loadAllUsersData(allUsers, oldVersion, ownersData);
        int currentVersion = oldVersion;
        if (currentVersion == 0) {
            android.util.Slog.i(LOG_TAG, java.lang.String.format("Upgrading from version %d", java.lang.Integer.valueOf(currentVersion)));
            currentVersion = 1;
        }
        if (currentVersion == 1) {
            android.util.Slog.i(LOG_TAG, java.lang.String.format("Upgrading from version %d", java.lang.Integer.valueOf(currentVersion)));
            upgradeSensorPermissionsAccess(allUsers, ownersData, allUsersData);
            currentVersion = 2;
        }
        if (currentVersion == 2) {
            android.util.Slog.i(LOG_TAG, java.lang.String.format("Upgrading from version %d", java.lang.Integer.valueOf(currentVersion)));
            upgradeProtectedPackages(ownersData, allUsersData);
            currentVersion = 3;
        }
        if (currentVersion == 3) {
            android.util.Slog.i(LOG_TAG, java.lang.String.format("Upgrading from version %d", java.lang.Integer.valueOf(currentVersion)));
            upgradePackageSuspension(allUsers, ownersData, allUsersData);
            currentVersion = 4;
        }
        if (currentVersion == 4) {
            android.util.Slog.i(LOG_TAG, java.lang.String.format("Upgrading from version %d", java.lang.Integer.valueOf(currentVersion)));
            initializeEffectiveKeepProfilesRunning(allUsersData);
            currentVersion = 5;
        }
        if (currentVersion == 5) {
            android.util.Slog.i(LOG_TAG, java.lang.String.format("Upgrading from version %d", java.lang.Integer.valueOf(currentVersion)));
            currentVersion = 6;
        }
        writePoliciesAndVersion(allUsers, allUsersData, ownersData, currentVersion);
    }

    private void upgradeSensorPermissionsAccess(int[] allUsers, com.android.server.devicepolicy.OwnersData ownersData, android.util.SparseArray<com.android.server.devicepolicy.DevicePolicyData> allUsersData) {
        for (int userId : allUsers) {
            com.android.server.devicepolicy.DevicePolicyData userData = allUsersData.get(userId);
            if (userData != null) {
                for (com.android.server.devicepolicy.ActiveAdmin admin : userData.mAdminList) {
                    if (ownersData.mDeviceOwnerUserId == userId && ownersData.mDeviceOwner != null && ownersData.mDeviceOwner.admin.equals(admin.info.getComponent())) {
                        android.util.Slog.i(LOG_TAG, java.lang.String.format("Marking Device Owner in user %d for permission grant ", java.lang.Integer.valueOf(userId)));
                        admin.mAdminCanGrantSensorsPermissions = true;
                    }
                }
            }
        }
    }

    private void upgradeProtectedPackages(com.android.server.devicepolicy.OwnersData ownersData, android.util.SparseArray<com.android.server.devicepolicy.DevicePolicyData> allUsersData) {
        if (ownersData.mDeviceOwner == null) {
            return;
        }
        java.util.List<java.lang.String> protectedPackages = null;
        com.android.server.devicepolicy.DevicePolicyData doUserData = allUsersData.get(ownersData.mDeviceOwnerUserId);
        if (doUserData == null) {
            android.util.Slog.e(LOG_TAG, "No policy data for do user");
            return;
        }
        if (ownersData.mDeviceOwnerProtectedPackages != null) {
            java.util.List<java.lang.String> protectedPackages2 = ownersData.mDeviceOwnerProtectedPackages.get(ownersData.mDeviceOwner.packageName);
            protectedPackages = protectedPackages2;
            if (protectedPackages != null) {
                android.util.Slog.i(LOG_TAG, "Found protected packages in Owners");
            }
            ownersData.mDeviceOwnerProtectedPackages = null;
        } else if (doUserData.mUserControlDisabledPackages != null) {
            android.util.Slog.i(LOG_TAG, "Found protected packages in DevicePolicyData");
            protectedPackages = doUserData.mUserControlDisabledPackages;
            doUserData.mUserControlDisabledPackages = null;
        }
        com.android.server.devicepolicy.ActiveAdmin doAdmin = doUserData.mAdminMap.get(ownersData.mDeviceOwner.admin);
        if (doAdmin == null) {
            android.util.Slog.e(LOG_TAG, "DO admin not found in DO user");
        } else if (protectedPackages != null) {
            doAdmin.protectedPackages = new java.util.ArrayList(protectedPackages);
        }
    }

    private void upgradePackageSuspension(int[] allUsers, com.android.server.devicepolicy.OwnersData ownersData, android.util.SparseArray<com.android.server.devicepolicy.DevicePolicyData> allUsersData) {
        if (ownersData.mDeviceOwner != null) {
            saveSuspendedPackages(allUsersData, ownersData.mDeviceOwnerUserId, ownersData.mDeviceOwner.admin);
        }
        for (int i = 0; i < ownersData.mProfileOwners.size(); i++) {
            int ownerUserId = ownersData.mProfileOwners.keyAt(i).intValue();
            com.android.server.devicepolicy.OwnersData.OwnerInfo ownerInfo = ownersData.mProfileOwners.valueAt(i);
            saveSuspendedPackages(allUsersData, ownerUserId, ownerInfo.admin);
        }
    }

    private void saveSuspendedPackages(android.util.SparseArray<com.android.server.devicepolicy.DevicePolicyData> allUsersData, int ownerUserId, android.content.ComponentName ownerPackage) {
        com.android.server.devicepolicy.DevicePolicyData ownerUserData = allUsersData.get(ownerUserId);
        if (ownerUserData == null) {
            android.util.Slog.e(LOG_TAG, "No policy data for owner user, cannot migrate suspended packages");
            return;
        }
        com.android.server.devicepolicy.ActiveAdmin ownerAdmin = ownerUserData.mAdminMap.get(ownerPackage);
        if (ownerAdmin == null) {
            android.util.Slog.e(LOG_TAG, "No admin for owner, cannot migrate suspended packages");
        } else {
            ownerAdmin.suspendedPackages = this.mProvider.getPlatformSuspendedPackages(ownerUserId);
            android.util.Slog.i(LOG_TAG, java.lang.String.format("Saved %d packages suspended by %s in user %d", java.lang.Integer.valueOf(ownerAdmin.suspendedPackages.size()), ownerPackage, java.lang.Integer.valueOf(ownerUserId)));
        }
    }

    private void initializeEffectiveKeepProfilesRunning(android.util.SparseArray<com.android.server.devicepolicy.DevicePolicyData> allUsersData) {
        com.android.server.devicepolicy.DevicePolicyData systemUserData = allUsersData.get(0);
        if (systemUserData == null) {
            return;
        }
        systemUserData.mEffectiveKeepProfilesRunning = false;
        android.util.Slog.i(LOG_TAG, "Keep profile running effective state set to false");
    }

    private com.android.server.devicepolicy.OwnersData loadOwners(int[] allUsers) {
        com.android.server.devicepolicy.OwnersData ownersData = new com.android.server.devicepolicy.OwnersData(this.mPathProvider);
        ownersData.load(allUsers);
        return ownersData;
    }

    private void writePoliciesAndVersion(int[] allUsers, android.util.SparseArray<com.android.server.devicepolicy.DevicePolicyData> allUsersData, com.android.server.devicepolicy.OwnersData ownersData, int currentVersion) {
        boolean allWritesSuccessful = true;
        int length = allUsers.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= length) {
                break;
            }
            int user = allUsers[i];
            if (!allWritesSuccessful || !writeDataForUser(user, allUsersData.get(user))) {
                z = false;
            }
            allWritesSuccessful = z;
            i++;
        }
        boolean allWritesSuccessful2 = allWritesSuccessful && ownersData.writeDeviceOwner();
        for (int i2 : allUsers) {
            allWritesSuccessful2 = allWritesSuccessful2 && ownersData.writeProfileOwner(i2);
        }
        if (allWritesSuccessful2) {
            writeVersion(currentVersion);
        } else {
            android.util.Slog.e(LOG_TAG, java.lang.String.format("Error: Failed upgrading policies to version %d", java.lang.Integer.valueOf(currentVersion)));
        }
    }

    private android.util.SparseArray<com.android.server.devicepolicy.DevicePolicyData> loadAllUsersData(int[] allUsers, int loadVersion, com.android.server.devicepolicy.OwnersData ownersData) {
        android.util.SparseArray<com.android.server.devicepolicy.DevicePolicyData> allUsersData = new android.util.SparseArray<>();
        for (int user : allUsers) {
            android.content.ComponentName owner = getOwnerForUser(ownersData, user);
            allUsersData.append(user, loadDataForUser(user, loadVersion, owner));
        }
        return allUsersData;
    }

    private android.content.ComponentName getOwnerForUser(com.android.server.devicepolicy.OwnersData ownersData, int user) {
        if (ownersData.mDeviceOwnerUserId == user && ownersData.mDeviceOwner != null) {
            android.content.ComponentName owner = ownersData.mDeviceOwner.admin;
            return owner;
        }
        if (!ownersData.mProfileOwners.containsKey(java.lang.Integer.valueOf(user))) {
            return null;
        }
        android.content.ComponentName owner2 = ownersData.mProfileOwners.get(java.lang.Integer.valueOf(user)).admin;
        return owner2;
    }

    private com.android.server.devicepolicy.DevicePolicyData loadDataForUser(int userId, int loadVersion, android.content.ComponentName ownerComponent) {
        com.android.server.devicepolicy.DevicePolicyData policy = new com.android.server.devicepolicy.DevicePolicyData(userId);
        if (loadVersion == 5 && userId == 0) {
            policy.mEffectiveKeepProfilesRunning = true;
        }
        com.android.server.devicepolicy.DevicePolicyData.load(policy, this.mProvider.makeDevicePoliciesJournaledFile(userId), this.mProvider.getAdminInfoSupplier(userId), ownerComponent);
        return policy;
    }

    private boolean writeDataForUser(int userId, com.android.server.devicepolicy.DevicePolicyData policy) {
        return com.android.server.devicepolicy.DevicePolicyData.store(policy, this.mProvider.makeDevicePoliciesJournaledFile(userId));
    }

    private com.android.internal.util.JournaledFile getVersionFile() {
        return this.mProvider.makePoliciesVersionJournaledFile(0);
    }

    private int readVersion() {
        com.android.internal.util.JournaledFile versionFile = getVersionFile();
        java.io.File file = versionFile.chooseForRead();
        try {
            java.lang.String versionString = java.nio.file.Files.readAllLines(file.toPath(), java.nio.charset.Charset.defaultCharset()).get(0);
            return java.lang.Integer.parseInt(versionString);
        } catch (java.nio.file.NoSuchFileException e) {
            return 0;
        } catch (java.io.IOException | java.lang.IndexOutOfBoundsException | java.lang.NumberFormatException e2) {
            android.util.Slog.e(LOG_TAG, "Error reading version", e2);
            return 0;
        }
    }

    private void writeVersion(int version) {
        com.android.internal.util.JournaledFile versionFile = getVersionFile();
        java.io.File file = versionFile.chooseForWrite();
        try {
            byte[] versionBytes = java.lang.String.format("%d", java.lang.Integer.valueOf(version)).getBytes();
            java.nio.file.Files.write(file.toPath(), versionBytes, new java.nio.file.OpenOption[0]);
            versionFile.commit();
        } catch (java.io.IOException e) {
            android.util.Slog.e(LOG_TAG, java.lang.String.format("Writing version %d failed", java.lang.Integer.valueOf(version)), e);
            versionFile.rollback();
        }
    }
}
