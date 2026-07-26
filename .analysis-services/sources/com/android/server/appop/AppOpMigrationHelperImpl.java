package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public class AppOpMigrationHelperImpl implements com.android.server.appop.AppOpMigrationHelper {
    private int mVersionAtBoot;
    private android.util.SparseArray<java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.Integer>>> mAppIdAppOpModes = null;
    private android.util.SparseArray<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>>> mPackageAppOpModes = null;
    private final java.lang.Object mLock = new java.lang.Object();

    @Override // com.android.server.appop.AppOpMigrationHelper
    public java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.Integer>> getLegacyAppIdAppOpModes(int userId) {
        synchronized (this.mLock) {
            if (this.mAppIdAppOpModes == null) {
                readLegacyAppOpState();
            }
        }
        return this.mAppIdAppOpModes.get(userId, java.util.Collections.emptyMap());
    }

    @Override // com.android.server.appop.AppOpMigrationHelper
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> getLegacyPackageAppOpModes(int userId) {
        synchronized (this.mLock) {
            if (this.mPackageAppOpModes == null) {
                readLegacyAppOpState();
            }
        }
        return this.mPackageAppOpModes.get(userId, java.util.Collections.emptyMap());
    }

    private void readLegacyAppOpState() {
        java.io.File systemDir = com.android.server.SystemServiceManager.ensureSystemDir();
        android.util.AtomicFile appOpFile = new android.util.AtomicFile(new java.io.File(systemDir, "appops.xml"));
        android.util.SparseArray<android.util.SparseIntArray> uidAppOpModes = new android.util.SparseArray<>();
        android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseIntArray>> packageAppOpModes = new android.util.SparseArray<>();
        com.android.server.appop.LegacyAppOpStateParser parser = new com.android.server.appop.LegacyAppOpStateParser();
        int version = parser.readState(appOpFile, uidAppOpModes, packageAppOpModes);
        switch (version) {
            case -2:
                this.mVersionAtBoot = -1;
                break;
            case -1:
                this.mVersionAtBoot = 0;
                break;
            default:
                this.mVersionAtBoot = version;
                break;
        }
        this.mAppIdAppOpModes = getAppIdAppOpModes(uidAppOpModes);
        this.mPackageAppOpModes = getPackageAppOpModes(packageAppOpModes);
    }

    private android.util.SparseArray<java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.Integer>>> getAppIdAppOpModes(android.util.SparseArray<android.util.SparseIntArray> uidAppOpModes) {
        android.util.SparseArray<java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.Integer>>> userAppIdAppOpModes = new android.util.SparseArray<>();
        int size = uidAppOpModes.size();
        for (int uidIndex = 0; uidIndex < size; uidIndex++) {
            int uid = uidAppOpModes.keyAt(uidIndex);
            int userId = android.os.UserHandle.getUserId(uid);
            java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.Integer>> appIdAppOpModes = userAppIdAppOpModes.get(userId);
            if (appIdAppOpModes == null) {
                appIdAppOpModes = new android.util.ArrayMap();
                userAppIdAppOpModes.put(userId, appIdAppOpModes);
            }
            android.util.SparseIntArray appOpModes = uidAppOpModes.valueAt(uidIndex);
            appIdAppOpModes.put(java.lang.Integer.valueOf(android.os.UserHandle.getAppId(uid)), getAppOpModesForOpName(appOpModes));
        }
        return userAppIdAppOpModes;
    }

    private android.util.SparseArray<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>>> getPackageAppOpModes(android.util.SparseArray<android.util.ArrayMap<java.lang.String, android.util.SparseIntArray>> legacyPackageAppOpModes) {
        android.util.SparseArray<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>>> userPackageAppOpModes = new android.util.SparseArray<>();
        int usersSize = legacyPackageAppOpModes.size();
        for (int userIndex = 0; userIndex < usersSize; userIndex++) {
            int userId = legacyPackageAppOpModes.keyAt(userIndex);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> packageAppOpModes = userPackageAppOpModes.get(userId);
            if (packageAppOpModes == null) {
                packageAppOpModes = new android.util.ArrayMap();
                userPackageAppOpModes.put(userId, packageAppOpModes);
            }
            android.util.ArrayMap<java.lang.String, android.util.SparseIntArray> legacyPackagesModes = legacyPackageAppOpModes.valueAt(userIndex);
            int packagesSize = legacyPackagesModes.size();
            for (int packageIndex = 0; packageIndex < packagesSize; packageIndex++) {
                java.lang.String packageName = legacyPackagesModes.keyAt(packageIndex);
                android.util.SparseIntArray modes = legacyPackagesModes.valueAt(packageIndex);
                packageAppOpModes.put(packageName, getAppOpModesForOpName(modes));
            }
        }
        return userPackageAppOpModes;
    }

    private java.util.Map<java.lang.String, java.lang.Integer> getAppOpModesForOpName(android.util.SparseIntArray appOpCodeModes) {
        int modesSize = appOpCodeModes.size();
        java.util.Map<java.lang.String, java.lang.Integer> appOpNameModes = new android.util.ArrayMap<>(modesSize);
        for (int modeIndex = 0; modeIndex < modesSize; modeIndex++) {
            int opCode = appOpCodeModes.keyAt(modeIndex);
            int opMode = appOpCodeModes.valueAt(modeIndex);
            appOpNameModes.put(android.app.AppOpsManager.opToPublicName(opCode), java.lang.Integer.valueOf(opMode));
        }
        return appOpNameModes;
    }

    @Override // com.android.server.appop.AppOpMigrationHelper
    public int getLegacyAppOpVersion() {
        synchronized (this.mLock) {
            if (this.mAppIdAppOpModes == null || this.mPackageAppOpModes == null) {
                readLegacyAppOpState();
            }
        }
        return this.mVersionAtBoot;
    }

    @Override // com.android.server.appop.AppOpMigrationHelper
    public boolean hasLegacyAppOpState() {
        return getLegacyAppOpVersion() > -1;
    }
}
