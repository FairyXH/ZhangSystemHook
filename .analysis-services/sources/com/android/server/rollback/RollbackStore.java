package com.android.server.rollback;

/* JADX INFO: loaded from: classes3.dex */
class RollbackStore {
    private static final java.lang.String TAG = "RollbackManager";
    private final java.io.File mRollbackDataDir;
    private final java.io.File mRollbackHistoryDir;

    RollbackStore(java.io.File rollbackDataDir, java.io.File rollbackHistoryDir) {
        this.mRollbackDataDir = rollbackDataDir;
        this.mRollbackHistoryDir = rollbackHistoryDir;
    }

    private static java.util.List<com.android.server.rollback.Rollback> loadRollbacks(java.io.File rollbackDataDir) {
        java.util.List<com.android.server.rollback.Rollback> rollbacks = new java.util.ArrayList<>();
        rollbackDataDir.mkdirs();
        for (java.io.File rollbackDir : rollbackDataDir.listFiles()) {
            if (rollbackDir.isDirectory()) {
                try {
                    rollbacks.add(loadRollback(rollbackDir));
                } catch (java.io.IOException e) {
                    android.util.Slog.e(TAG, "Unable to read rollback at " + rollbackDir, e);
                    removeFile(rollbackDir);
                }
            }
        }
        return rollbacks;
    }

    java.util.List<com.android.server.rollback.Rollback> loadRollbacks() {
        return loadRollbacks(this.mRollbackDataDir);
    }

    java.util.List<com.android.server.rollback.Rollback> loadHistorialRollbacks() {
        return loadRollbacks(this.mRollbackHistoryDir);
    }

    private static java.util.List<java.lang.Integer> toIntList(org.json.JSONArray jsonArray) throws org.json.JSONException {
        java.util.List<java.lang.Integer> ret = new java.util.ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            ret.add(java.lang.Integer.valueOf(jsonArray.getInt(i)));
        }
        return ret;
    }

    private static org.json.JSONArray fromIntList(java.util.List<java.lang.Integer> list) {
        org.json.JSONArray jsonArray = new org.json.JSONArray();
        for (int i = 0; i < list.size(); i++) {
            jsonArray.put(list.get(i));
        }
        return jsonArray;
    }

    private static org.json.JSONArray convertToJsonArray(java.util.List<android.content.rollback.PackageRollbackInfo.RestoreInfo> list) throws org.json.JSONException {
        org.json.JSONArray jsonArray = new org.json.JSONArray();
        for (android.content.rollback.PackageRollbackInfo.RestoreInfo ri : list) {
            org.json.JSONObject jo = new org.json.JSONObject();
            jo.put("userId", ri.userId);
            jo.put("appId", ri.appId);
            jo.put("seInfo", ri.seInfo);
            jsonArray.put(jo);
        }
        return jsonArray;
    }

    private static java.util.ArrayList<android.content.rollback.PackageRollbackInfo.RestoreInfo> convertToRestoreInfoArray(org.json.JSONArray array) throws org.json.JSONException {
        java.util.ArrayList<android.content.rollback.PackageRollbackInfo.RestoreInfo> restoreInfos = new java.util.ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            org.json.JSONObject jo = array.getJSONObject(i);
            restoreInfos.add(new android.content.rollback.PackageRollbackInfo.RestoreInfo(jo.getInt("userId"), jo.getInt("appId"), jo.getString("seInfo")));
        }
        return restoreInfos;
    }

    private static org.json.JSONArray extensionVersionsToJson(android.util.SparseIntArray extensionVersions) throws org.json.JSONException {
        org.json.JSONArray array = new org.json.JSONArray();
        for (int i = 0; i < extensionVersions.size(); i++) {
            org.json.JSONObject entryJson = new org.json.JSONObject();
            entryJson.put("sdkVersion", extensionVersions.keyAt(i));
            entryJson.put("extensionVersion", extensionVersions.valueAt(i));
            array.put(entryJson);
        }
        return array;
    }

    private static android.util.SparseIntArray extensionVersionsFromJson(org.json.JSONArray json) throws org.json.JSONException {
        if (json == null) {
            return new android.util.SparseIntArray(0);
        }
        android.util.SparseIntArray extensionVersions = new android.util.SparseIntArray(json.length());
        for (int i = 0; i < json.length(); i++) {
            org.json.JSONObject entry = json.getJSONObject(i);
            extensionVersions.append(entry.getInt("sdkVersion"), entry.getInt("extensionVersion"));
        }
        return extensionVersions;
    }

    private static org.json.JSONObject rollbackInfoToJson(android.content.rollback.RollbackInfo rollback) throws org.json.JSONException {
        org.json.JSONObject json = new org.json.JSONObject();
        json.put("rollbackId", rollback.getRollbackId());
        json.put("packages", toJson((java.util.List<android.content.rollback.PackageRollbackInfo>) rollback.getPackages()));
        json.put("isStaged", rollback.isStaged());
        json.put("causePackages", versionedPackagesToJson(rollback.getCausePackages()));
        json.put("committedSessionId", rollback.getCommittedSessionId());
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.recoverabilityDetection()) {
            json.put("rollbackImpactLevel", rollback.getRollbackImpactLevel());
        }
        return json;
    }

    private static android.content.rollback.RollbackInfo rollbackInfoFromJson(org.json.JSONObject json) throws org.json.JSONException {
        android.content.rollback.RollbackInfo rollbackInfo = new android.content.rollback.RollbackInfo(json.getInt("rollbackId"), packageRollbackInfosFromJson(json.getJSONArray("packages")), json.getBoolean("isStaged"), versionedPackagesFromJson(json.getJSONArray("causePackages")), json.getInt("committedSessionId"));
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.recoverabilityDetection()) {
            rollbackInfo.setRollbackImpactLevel(json.optInt("rollbackImpactLevel", 0));
        }
        return rollbackInfo;
    }

    com.android.server.rollback.Rollback createNonStagedRollback(int rollbackId, int originalSessionId, int userId, java.lang.String installerPackageName, int[] packageSessionIds, android.util.SparseIntArray extensionVersions) {
        java.io.File backupDir = new java.io.File(this.mRollbackDataDir, java.lang.Integer.toString(rollbackId));
        return new com.android.server.rollback.Rollback(rollbackId, backupDir, originalSessionId, false, userId, installerPackageName, packageSessionIds, extensionVersions);
    }

    com.android.server.rollback.Rollback createStagedRollback(int rollbackId, int originalSessionId, int userId, java.lang.String installerPackageName, int[] packageSessionIds, android.util.SparseIntArray extensionVersions) {
        java.io.File backupDir = new java.io.File(this.mRollbackDataDir, java.lang.Integer.toString(rollbackId));
        return new com.android.server.rollback.Rollback(rollbackId, backupDir, originalSessionId, true, userId, installerPackageName, packageSessionIds, extensionVersions);
    }

    private static boolean isLinkPossible(java.io.File oldFile, java.io.File newFile) {
        try {
            return android.system.Os.stat(oldFile.getAbsolutePath()).st_dev == android.system.Os.stat(newFile.getAbsolutePath()).st_dev;
        } catch (android.system.ErrnoException e) {
            return false;
        }
    }

    static void backupPackageCodePath(com.android.server.rollback.Rollback rollback, java.lang.String packageName, java.lang.String codePath) throws java.io.IOException {
        java.io.File sourceFile = new java.io.File(codePath);
        java.io.File targetDir = new java.io.File(rollback.getBackupDir(), packageName);
        targetDir.mkdirs();
        java.io.File targetFile = new java.io.File(targetDir, sourceFile.getName());
        boolean fallbackToCopy = !isLinkPossible(sourceFile, targetDir);
        if (!fallbackToCopy) {
            try {
                android.system.Os.link(sourceFile.getAbsolutePath(), targetFile.getAbsolutePath());
            } catch (android.system.ErrnoException e) {
                boolean isRollbackTest = android.os.SystemProperties.getBoolean("persist.rollback.is_test", false);
                if (isRollbackTest) {
                    throw new java.io.IOException(e);
                }
                fallbackToCopy = true;
            }
        }
        if (fallbackToCopy) {
            java.nio.file.Files.copy(sourceFile.toPath(), targetFile.toPath(), new java.nio.file.CopyOption[0]);
        }
    }

    static java.io.File[] getPackageCodePaths(com.android.server.rollback.Rollback rollback, java.lang.String packageName) {
        java.io.File targetDir = new java.io.File(rollback.getBackupDir(), packageName);
        java.io.File[] files = targetDir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        return files;
    }

    static void deletePackageCodePaths(com.android.server.rollback.Rollback rollback) {
        for (android.content.rollback.PackageRollbackInfo info : rollback.info.getPackages()) {
            java.io.File targetDir = new java.io.File(rollback.getBackupDir(), info.getPackageName());
            removeFile(targetDir);
        }
    }

    private static void saveRollback(com.android.server.rollback.Rollback rollback, java.io.File backDir) {
        java.io.FileOutputStream fos = null;
        android.util.AtomicFile file = new android.util.AtomicFile(new java.io.File(backDir, "rollback.json"));
        try {
            backDir.mkdirs();
            org.json.JSONObject dataJson = new org.json.JSONObject();
            dataJson.put("info", rollbackInfoToJson(rollback.info));
            dataJson.put(com.android.server.net.watchlist.WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP, rollback.getTimestamp().toString());
            if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.rollbackLifetime()) {
                dataJson.put("rollbackLifetimeMillis", rollback.getRollbackLifetimeMillis());
            }
            dataJson.put("originalSessionId", rollback.getOriginalSessionId());
            dataJson.put("state", rollback.getStateAsString());
            dataJson.put("stateDescription", rollback.getStateDescription());
            dataJson.put("restoreUserDataInProgress", rollback.isRestoreUserDataInProgress());
            dataJson.put("userId", rollback.getUserId());
            dataJson.putOpt("installerPackageName", rollback.getInstallerPackageName());
            dataJson.putOpt("extensionVersions", extensionVersionsToJson(rollback.getExtensionVersions()));
            fos = file.startWrite();
            fos.write(dataJson.toString().getBytes());
            fos.flush();
            file.finishWrite(fos);
        } catch (java.io.IOException | org.json.JSONException e) {
            android.util.Slog.e(TAG, "Unable to save rollback for: " + rollback.info.getRollbackId(), e);
            if (fos != null) {
                file.failWrite(fos);
            }
        }
    }

    static void saveRollback(com.android.server.rollback.Rollback rollback) {
        saveRollback(rollback, rollback.getBackupDir());
    }

    void saveRollbackToHistory(com.android.server.rollback.Rollback rollback) {
        java.lang.String suffix = java.lang.Long.toHexString(rollback.getTimestamp().getEpochSecond());
        java.lang.String dirName = java.lang.Integer.toString(rollback.info.getRollbackId());
        java.io.File backupDir = new java.io.File(this.mRollbackHistoryDir, dirName + "-" + suffix);
        saveRollback(rollback, backupDir);
    }

    static void deleteRollback(com.android.server.rollback.Rollback rollback) {
        removeFile(rollback.getBackupDir());
    }

    private static com.android.server.rollback.Rollback loadRollback(java.io.File backupDir) throws java.io.IOException {
        try {
            java.io.File rollbackJsonFile = new java.io.File(backupDir, "rollback.json");
            org.json.JSONObject dataJson = new org.json.JSONObject(libcore.io.IoUtils.readFileAsString(rollbackJsonFile.getAbsolutePath()));
            return rollbackFromJson(dataJson, backupDir);
        } catch (java.text.ParseException | java.time.format.DateTimeParseException | org.json.JSONException e) {
            throw new java.io.IOException(e);
        }
    }

    static com.android.server.rollback.Rollback rollbackFromJson(org.json.JSONObject dataJson, java.io.File backupDir) throws org.json.JSONException, java.text.ParseException {
        com.android.server.rollback.Rollback rollback = new com.android.server.rollback.Rollback(rollbackInfoFromJson(dataJson.getJSONObject("info")), backupDir, java.time.Instant.parse(dataJson.getString(com.android.server.net.watchlist.WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP)), dataJson.optInt("originalSessionId", dataJson.optInt("stagedSessionId", -1)), com.android.server.rollback.Rollback.rollbackStateFromString(dataJson.getString("state")), dataJson.optString("stateDescription"), dataJson.getBoolean("restoreUserDataInProgress"), dataJson.optInt("userId", android.os.UserHandle.SYSTEM.getIdentifier()), dataJson.optString("installerPackageName", ""), extensionVersionsFromJson(dataJson.optJSONArray("extensionVersions")));
        if (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.rollbackLifetime()) {
            rollback.setRollbackLifetimeMillis(dataJson.optLong("rollbackLifetimeMillis"));
        }
        return rollback;
    }

    private static org.json.JSONObject toJson(android.content.pm.VersionedPackage pkg) throws org.json.JSONException {
        org.json.JSONObject json = new org.json.JSONObject();
        json.put(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, pkg.getPackageName());
        json.put("longVersionCode", pkg.getLongVersionCode());
        return json;
    }

    private static android.content.pm.VersionedPackage versionedPackageFromJson(org.json.JSONObject json) throws org.json.JSONException {
        java.lang.String packageName = json.getString(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        long longVersionCode = json.getLong("longVersionCode");
        return new android.content.pm.VersionedPackage(packageName, longVersionCode);
    }

    private static org.json.JSONObject toJson(android.content.rollback.PackageRollbackInfo info) throws org.json.JSONException {
        org.json.JSONObject json = new org.json.JSONObject();
        json.put("versionRolledBackFrom", toJson(info.getVersionRolledBackFrom()));
        json.put("versionRolledBackTo", toJson(info.getVersionRolledBackTo()));
        java.util.List<java.lang.Integer> pendingBackups = info.getPendingBackups();
        java.util.List<android.content.rollback.PackageRollbackInfo.RestoreInfo> pendingRestores = info.getPendingRestores();
        java.util.List<java.lang.Integer> snapshottedUsers = info.getSnapshottedUsers();
        json.put("pendingBackups", fromIntList(pendingBackups));
        json.put("pendingRestores", convertToJsonArray(pendingRestores));
        json.put("isApex", info.isApex());
        json.put("isApkInApex", info.isApkInApex());
        json.put("installedUsers", fromIntList(snapshottedUsers));
        json.put("rollbackDataPolicy", info.getRollbackDataPolicy());
        return json;
    }

    private static android.content.rollback.PackageRollbackInfo packageRollbackInfoFromJson(org.json.JSONObject json) throws org.json.JSONException {
        android.content.pm.VersionedPackage versionRolledBackFrom = versionedPackageFromJson(json.getJSONObject("versionRolledBackFrom"));
        android.content.pm.VersionedPackage versionRolledBackTo = versionedPackageFromJson(json.getJSONObject("versionRolledBackTo"));
        java.util.List<java.lang.Integer> pendingBackups = toIntList(json.getJSONArray("pendingBackups"));
        java.util.ArrayList<android.content.rollback.PackageRollbackInfo.RestoreInfo> pendingRestores = convertToRestoreInfoArray(json.getJSONArray("pendingRestores"));
        boolean isApex = json.getBoolean("isApex");
        boolean isApkInApex = json.getBoolean("isApkInApex");
        java.util.List<java.lang.Integer> snapshottedUsers = toIntList(json.getJSONArray("installedUsers"));
        int rollbackDataPolicy = json.optInt("rollbackDataPolicy", 0);
        return new android.content.rollback.PackageRollbackInfo(versionRolledBackFrom, versionRolledBackTo, pendingBackups, pendingRestores, isApex, isApkInApex, snapshottedUsers, rollbackDataPolicy);
    }

    private static org.json.JSONArray versionedPackagesToJson(java.util.List<android.content.pm.VersionedPackage> packages) throws org.json.JSONException {
        org.json.JSONArray json = new org.json.JSONArray();
        for (android.content.pm.VersionedPackage pkg : packages) {
            json.put(toJson(pkg));
        }
        return json;
    }

    private static java.util.List<android.content.pm.VersionedPackage> versionedPackagesFromJson(org.json.JSONArray json) throws org.json.JSONException {
        java.util.List<android.content.pm.VersionedPackage> packages = new java.util.ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            packages.add(versionedPackageFromJson(json.getJSONObject(i)));
        }
        return packages;
    }

    private static org.json.JSONArray toJson(java.util.List<android.content.rollback.PackageRollbackInfo> infos) throws org.json.JSONException {
        org.json.JSONArray json = new org.json.JSONArray();
        for (android.content.rollback.PackageRollbackInfo info : infos) {
            json.put(toJson(info));
        }
        return json;
    }

    private static java.util.List<android.content.rollback.PackageRollbackInfo> packageRollbackInfosFromJson(org.json.JSONArray json) throws org.json.JSONException {
        java.util.List<android.content.rollback.PackageRollbackInfo> infos = new java.util.ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            infos.add(packageRollbackInfoFromJson(json.getJSONObject(i)));
        }
        return infos;
    }

    private static void removeFile(java.io.File file) {
        if (file.isDirectory()) {
            for (java.io.File child : file.listFiles()) {
                removeFile(child);
            }
        }
        if (file.exists()) {
            file.delete();
        }
    }
}
