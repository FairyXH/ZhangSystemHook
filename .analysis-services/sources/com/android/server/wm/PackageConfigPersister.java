package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class PackageConfigPersister {
    private static final java.lang.String ATTR_LOCALES = "locale_list";
    private static final java.lang.String ATTR_NIGHT_MODE = "night_mode";
    private static final java.lang.String ATTR_PACKAGE_NAME = "package_name";
    private static final boolean DEBUG = false;
    private static final java.lang.String PACKAGE_DIRNAME = "package_configs";
    private static final java.lang.String SUFFIX_FILE_NAME = "_config.xml";
    private static final java.lang.String TAG = com.android.server.wm.PackageConfigPersister.class.getSimpleName();
    private static final java.lang.String TAG_CONFIG = "config";
    private final com.android.server.wm.ActivityTaskManagerService mAtm;
    private final com.android.server.wm.PersisterQueue mPersisterQueue;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord>> mPendingWrite = new android.util.SparseArray<>();
    private final android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord>> mModified = new android.util.SparseArray<>();

    /* JADX INFO: Access modifiers changed from: private */
    public static java.io.File getUserConfigsDir(int userId) {
        return new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), PACKAGE_DIRNAME);
    }

    PackageConfigPersister(com.android.server.wm.PersisterQueue queue, com.android.server.wm.ActivityTaskManagerService atm) {
        this.mPersisterQueue = queue;
        this.mAtm = atm;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00ad  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void loadUserPackages(int r20) {
        /*
            Method dump skipped, instruction units count: 310
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.PackageConfigPersister.loadUserPackages(int):void");
    }

    void updateConfigIfNeeded(com.android.server.wm.ConfigurationContainer container, int userId, java.lang.String packageName) {
        synchronized (this.mLock) {
            com.android.server.wm.PackageConfigPersister.PackageConfigRecord modifiedRecord = findRecord(this.mModified, packageName, userId);
            if (modifiedRecord != null) {
                container.applyAppSpecificConfig(modifiedRecord.mNightMode, com.android.server.wm.LocaleOverlayHelper.combineLocalesIfOverlayExists(modifiedRecord.mLocales, this.mAtm.getGlobalConfiguration().getLocales()), modifiedRecord.mGrammaticalGender);
            } else {
                container.applyAppSpecificConfig(null, null, null);
            }
        }
    }

    boolean updateFromImpl(java.lang.String packageName, int userId, com.android.server.wm.PackageConfigurationUpdaterImpl impl) {
        com.android.server.wm.PackageConfigPersister.PackageConfigRecord writeRecord;
        synchronized (this.mLock) {
            boolean isRecordPresent = false;
            com.android.server.wm.PackageConfigPersister.PackageConfigRecord record = findRecord(this.mModified, packageName, userId);
            if (record != null) {
                isRecordPresent = true;
            } else {
                record = findRecordOrCreate(this.mModified, packageName, userId);
            }
            boolean isNightModeChanged = updateNightMode(impl.getNightMode(), record);
            boolean isLocalesChanged = updateLocales(impl.getLocales(), record);
            boolean isGenderChanged = updateGender(impl.getGrammaticalGender(), record);
            if ((record.mNightMode != null && !record.isResetNightMode()) || ((record.mLocales != null && !record.mLocales.isEmpty()) || (record.mGrammaticalGender != null && record.mGrammaticalGender.intValue() != 0))) {
                if (!isNightModeChanged && !isLocalesChanged && !isGenderChanged) {
                    return false;
                }
                com.android.server.wm.PackageConfigPersister.PackageConfigRecord pendingRecord = findRecord(this.mPendingWrite, record.mName, record.mUserId);
                if (pendingRecord == null) {
                    writeRecord = findRecordOrCreate(this.mPendingWrite, record.mName, record.mUserId);
                } else {
                    writeRecord = pendingRecord;
                }
                if (updateNightMode(record.mNightMode, writeRecord) || updateLocales(record.mLocales, writeRecord) || updateGender(record.mGrammaticalGender, writeRecord)) {
                    this.mPersisterQueue.addItem(new com.android.server.wm.PackageConfigPersister.WriteProcessItem(writeRecord), false);
                    return true;
                }
                return false;
            }
            removePackage(packageName, userId);
            return isRecordPresent;
        }
    }

    private boolean updateNightMode(java.lang.Integer requestedNightMode, com.android.server.wm.PackageConfigPersister.PackageConfigRecord record) {
        if (requestedNightMode == null || requestedNightMode.equals(record.mNightMode)) {
            return false;
        }
        record.mNightMode = requestedNightMode;
        return true;
    }

    private boolean updateLocales(android.os.LocaleList requestedLocaleList, com.android.server.wm.PackageConfigPersister.PackageConfigRecord record) {
        if (requestedLocaleList == null || requestedLocaleList.equals(record.mLocales)) {
            return false;
        }
        record.mLocales = requestedLocaleList;
        return true;
    }

    private boolean updateGender(java.lang.Integer requestedGender, com.android.server.wm.PackageConfigPersister.PackageConfigRecord record) {
        if (requestedGender == null || requestedGender.equals(record.mGrammaticalGender)) {
            return false;
        }
        record.mGrammaticalGender = requestedGender;
        return true;
    }

    void removeUser(int userId) {
        synchronized (this.mLock) {
            java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord> modifyRecords = this.mModified.get(userId);
            java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord> writeRecords = this.mPendingWrite.get(userId);
            if ((modifyRecords != null && modifyRecords.size() != 0) || (writeRecords != null && writeRecords.size() != 0)) {
                java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord> tempList = new java.util.HashMap<>(modifyRecords);
                tempList.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.wm.PackageConfigPersister$$ExternalSyntheticLambda0
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        this.f$0.lambda$removeUser$0((java.lang.String) obj, (com.android.server.wm.PackageConfigPersister.PackageConfigRecord) obj2);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeUser$0(java.lang.String name, com.android.server.wm.PackageConfigPersister.PackageConfigRecord record) {
        removePackage(record.mName, record.mUserId);
    }

    void onPackageUninstall(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            removePackage(packageName, userId);
        }
    }

    void onPackageDataCleared(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            removePackage(packageName, userId);
        }
    }

    private void removePackage(java.lang.String packageName, int userId) {
        final com.android.server.wm.PackageConfigPersister.PackageConfigRecord record = findRecord(this.mPendingWrite, packageName, userId);
        if (record != null) {
            removeRecord(this.mPendingWrite, record);
            this.mPersisterQueue.removeItems(new java.util.function.Predicate() { // from class: com.android.server.wm.PackageConfigPersister$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.PackageConfigPersister.lambda$removePackage$1(record, (com.android.server.wm.PackageConfigPersister.WriteProcessItem) obj);
                }
            }, com.android.server.wm.PackageConfigPersister.WriteProcessItem.class);
        }
        com.android.server.wm.PackageConfigPersister.PackageConfigRecord modifyRecord = findRecord(this.mModified, packageName, userId);
        if (modifyRecord != null) {
            removeRecord(this.mModified, modifyRecord);
            this.mPersisterQueue.addItem(new com.android.server.wm.PackageConfigPersister.DeletePackageItem(userId, packageName), false);
        }
    }

    static /* synthetic */ boolean lambda$removePackage$1(com.android.server.wm.PackageConfigPersister.PackageConfigRecord record, com.android.server.wm.PackageConfigPersister.WriteProcessItem item) {
        return item.mRecord.mName == record.mName && item.mRecord.mUserId == record.mUserId;
    }

    com.android.server.wm.ActivityTaskManagerInternal.PackageConfig findPackageConfiguration(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            com.android.server.wm.PackageConfigPersister.PackageConfigRecord packageConfigRecord = findRecord(this.mModified, packageName, userId);
            if (packageConfigRecord == null) {
                android.util.Slog.w(TAG, "App-specific configuration not found for packageName: " + packageName + " and userId: " + userId);
                return null;
            }
            return new com.android.server.wm.ActivityTaskManagerInternal.PackageConfig(packageConfigRecord.mNightMode, packageConfigRecord.mLocales, packageConfigRecord.mGrammaticalGender);
        }
    }

    void dump(java.io.PrintWriter pw, int userId) {
        pw.println("INSTALLED PACKAGES HAVING APP-SPECIFIC CONFIGURATIONS");
        pw.println("Current user ID : " + userId);
        synchronized (this.mLock) {
            java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord> persistedPackageConfigMap = this.mModified.get(userId);
            if (persistedPackageConfigMap != null) {
                for (com.android.server.wm.PackageConfigPersister.PackageConfigRecord packageConfig : persistedPackageConfigMap.values()) {
                    pw.println();
                    pw.println("    PackageName : " + packageConfig.mName);
                    pw.println("        NightMode : " + packageConfig.mNightMode);
                    pw.println("        Locales : " + packageConfig.mLocales);
                }
            }
        }
    }

    static class PackageConfigRecord {
        java.lang.Integer mGrammaticalGender;
        android.os.LocaleList mLocales;
        final java.lang.String mName;
        java.lang.Integer mNightMode;
        final int mUserId;

        PackageConfigRecord(java.lang.String name, int userId) {
            this.mName = name;
            this.mUserId = userId;
        }

        boolean isResetNightMode() {
            return this.mNightMode.intValue() == 0;
        }

        public java.lang.String toString() {
            return "PackageConfigRecord package name: " + this.mName + " userId " + this.mUserId + " nightMode " + this.mNightMode + " locales " + this.mLocales;
        }
    }

    private com.android.server.wm.PackageConfigPersister.PackageConfigRecord findRecordOrCreate(android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord>> list, java.lang.String name, int userId) {
        java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord> records = list.get(userId);
        if (records == null) {
            records = new java.util.HashMap<>();
            list.put(userId, records);
        }
        com.android.server.wm.PackageConfigPersister.PackageConfigRecord record = records.get(name);
        if (record != null) {
            return record;
        }
        com.android.server.wm.PackageConfigPersister.PackageConfigRecord record2 = new com.android.server.wm.PackageConfigPersister.PackageConfigRecord(name, userId);
        records.put(name, record2);
        return record2;
    }

    private com.android.server.wm.PackageConfigPersister.PackageConfigRecord findRecord(android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord>> list, java.lang.String name, int userId) {
        java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord> packages = list.get(userId);
        if (packages == null) {
            return null;
        }
        return packages.get(name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeRecord(android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord>> list, com.android.server.wm.PackageConfigPersister.PackageConfigRecord record) {
        java.util.HashMap<java.lang.String, com.android.server.wm.PackageConfigPersister.PackageConfigRecord> processes = list.get(record.mUserId);
        if (processes != null) {
            processes.remove(record.mName);
        }
    }

    private static class DeletePackageItem implements com.android.server.wm.PersisterQueue.WriteQueueItem {
        final java.lang.String mPackageName;
        final int mUserId;

        DeletePackageItem(int userId, java.lang.String packageName) {
            this.mUserId = userId;
            this.mPackageName = packageName;
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() {
            java.io.File userConfigsDir = com.android.server.wm.PackageConfigPersister.getUserConfigsDir(this.mUserId);
            if (!userConfigsDir.isDirectory()) {
                return;
            }
            android.util.AtomicFile atomicFile = new android.util.AtomicFile(new java.io.File(userConfigsDir, this.mPackageName + com.android.server.wm.PackageConfigPersister.SUFFIX_FILE_NAME));
            if (atomicFile.exists()) {
                atomicFile.delete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class WriteProcessItem implements com.android.server.wm.PersisterQueue.WriteQueueItem {
        final com.android.server.wm.PackageConfigPersister.PackageConfigRecord mRecord;

        WriteProcessItem(com.android.server.wm.PackageConfigPersister.PackageConfigRecord record) {
            this.mRecord = record;
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() throws java.io.IOException {
            byte[] data = null;
            synchronized (com.android.server.wm.PackageConfigPersister.this.mLock) {
                try {
                    data = saveToXml();
                } catch (java.lang.Exception e) {
                }
                com.android.server.wm.PackageConfigPersister.this.removeRecord(com.android.server.wm.PackageConfigPersister.this.mPendingWrite, this.mRecord);
            }
            if (data != null) {
                android.util.AtomicFile atomicFile = null;
                try {
                    java.io.File userConfigsDir = com.android.server.wm.PackageConfigPersister.getUserConfigsDir(this.mRecord.mUserId);
                    if (!userConfigsDir.isDirectory() && !userConfigsDir.mkdirs()) {
                        android.util.Slog.e(com.android.server.wm.PackageConfigPersister.TAG, "Failure creating tasks directory for user " + this.mRecord.mUserId + ": " + userConfigsDir);
                        return;
                    }
                    android.util.AtomicFile atomicFile2 = new android.util.AtomicFile(new java.io.File(userConfigsDir, this.mRecord.mName + com.android.server.wm.PackageConfigPersister.SUFFIX_FILE_NAME));
                    java.io.FileOutputStream file = atomicFile2.startWrite();
                    file.write(data);
                    atomicFile2.finishWrite(file);
                } catch (java.io.IOException e2) {
                    if (0 != 0) {
                        atomicFile.failWrite(null);
                    }
                    android.util.Slog.e(com.android.server.wm.PackageConfigPersister.TAG, "Unable to open " + ((java.lang.Object) null) + " for persisting. " + e2);
                }
            }
        }

        private byte[] saveToXml() throws java.io.IOException {
            java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
            com.android.modules.utils.TypedXmlSerializer xmlSerializer = android.util.Xml.resolveSerializer(os);
            xmlSerializer.startDocument((java.lang.String) null, true);
            xmlSerializer.startTag((java.lang.String) null, com.android.server.wm.PackageConfigPersister.TAG_CONFIG);
            xmlSerializer.attribute((java.lang.String) null, com.android.server.wm.PackageConfigPersister.ATTR_PACKAGE_NAME, this.mRecord.mName);
            if (this.mRecord.mNightMode != null) {
                xmlSerializer.attributeInt((java.lang.String) null, com.android.server.wm.PackageConfigPersister.ATTR_NIGHT_MODE, this.mRecord.mNightMode.intValue());
            }
            if (this.mRecord.mLocales != null) {
                xmlSerializer.attribute((java.lang.String) null, com.android.server.wm.PackageConfigPersister.ATTR_LOCALES, this.mRecord.mLocales.toLanguageTags());
            }
            xmlSerializer.endTag((java.lang.String) null, com.android.server.wm.PackageConfigPersister.TAG_CONFIG);
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            return os.toByteArray();
        }
    }
}
