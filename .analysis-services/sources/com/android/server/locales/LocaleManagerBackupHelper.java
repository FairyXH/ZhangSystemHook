package com.android.server.locales;

/* JADX INFO: loaded from: classes2.dex */
class LocaleManagerBackupHelper {
    private static final java.lang.String ARCHIVED_PACKAGES_PREFS = "ArchivedPackagesPrefs.xml";
    private static final java.lang.String ATTR_DELEGATE_SELECTOR = "delegate_selector";
    private static final java.lang.String ATTR_LOCALES = "locales";
    private static final java.lang.String ATTR_PACKAGE_NAME = "name";
    private static final java.lang.String KEY_STAGED_DATA_TIME = "staged_data_time";
    private static final java.lang.String LOCALES_FROM_DELEGATE_PREFS = "LocalesFromDelegatePrefs.xml";
    private static final java.lang.String LOCALES_STAGED_DATA_PREFS = "LocalesStagedDataPrefs.xml";
    private static final java.lang.String LOCALES_XML_TAG = "locales";
    private static final java.lang.String PACKAGE_XML_TAG = "package";
    private static final java.time.Duration STAGE_DATA_RETENTION_PERIOD = java.time.Duration.ofDays(3);
    private static final java.lang.String STRING_SPLIT = " s:";
    private static final java.lang.String SYSTEM_BACKUP_PACKAGE_KEY = "android";
    private static final java.lang.String TAG = "LocaleManagerBkpHelper";
    private final java.io.File mArchivedPackagesFile;
    private final java.time.Clock mClock;
    private final android.content.Context mContext;
    private final android.content.SharedPreferences mDelegateAppLocalePackages;
    private final com.android.server.locales.LocaleManagerService mLocaleManagerService;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.util.SparseArray<java.io.File> mStagedDataFiles;
    private final java.lang.Object mStagedDataLock;
    private final android.content.BroadcastReceiver mUserMonitor;

    LocaleManagerBackupHelper(com.android.server.locales.LocaleManagerService localeManagerService, android.content.pm.PackageManager packageManager, android.os.HandlerThread broadcastHandlerThread) {
        this(localeManagerService.mContext, localeManagerService, packageManager, java.time.Clock.systemUTC(), broadcastHandlerThread, null, null, null);
    }

    LocaleManagerBackupHelper(android.content.Context context, com.android.server.locales.LocaleManagerService localeManagerService, android.content.pm.PackageManager packageManager, java.time.Clock clock, android.os.HandlerThread broadcastHandlerThread, android.util.SparseArray<java.io.File> stagedDataFiles, java.io.File archivedPackagesFile, android.content.SharedPreferences delegateAppLocalePackages) {
        this.mStagedDataLock = new java.lang.Object();
        this.mContext = context;
        this.mLocaleManagerService = localeManagerService;
        this.mPackageManager = packageManager;
        this.mClock = clock;
        this.mDelegateAppLocalePackages = delegateAppLocalePackages != null ? delegateAppLocalePackages : createPersistedInfo();
        this.mArchivedPackagesFile = archivedPackagesFile;
        this.mStagedDataFiles = stagedDataFiles;
        this.mUserMonitor = new com.android.server.locales.LocaleManagerBackupHelper.UserMonitor();
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.USER_REMOVED");
        context.registerReceiverAsUser(this.mUserMonitor, android.os.UserHandle.ALL, filter, null, broadcastHandlerThread.getThreadHandler());
    }

    android.content.BroadcastReceiver getUserMonitor() {
        return this.mUserMonitor;
    }

    public byte[] getBackupPayload(int userId) {
        synchronized (this.mStagedDataLock) {
            cleanStagedDataForOldEntriesLocked(userId);
        }
        java.util.HashMap<java.lang.String, com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo> pkgStates = new java.util.HashMap<>();
        for (android.content.pm.ApplicationInfo appInfo : this.mPackageManager.getInstalledApplicationsAsUser(android.content.pm.PackageManager.ApplicationInfoFlags.of(0L), userId)) {
            try {
                android.os.LocaleList appLocales = this.mLocaleManagerService.getApplicationLocales(appInfo.packageName, userId);
                if (!appLocales.isEmpty()) {
                    boolean localeSetFromDelegate = false;
                    if (this.mDelegateAppLocalePackages != null) {
                        localeSetFromDelegate = this.mDelegateAppLocalePackages.getStringSet(java.lang.Integer.toString(userId), java.util.Collections.emptySet()).contains(appInfo.packageName);
                    }
                    com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo localesInfo = new com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo(appLocales.toLanguageTags(), localeSetFromDelegate);
                    pkgStates.put(appInfo.packageName, localesInfo);
                }
            } catch (android.os.RemoteException | java.lang.IllegalArgumentException e) {
                android.util.Slog.e(TAG, "Exception when getting locales for package: " + appInfo.packageName, e);
            }
        }
        if (pkgStates.isEmpty()) {
            return null;
        }
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        try {
            writeToXml(out, pkgStates);
            return out.toByteArray();
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Could not write to xml for backup ", e2);
            return null;
        }
    }

    private void cleanStagedDataForOldEntriesLocked(int userId) {
        java.lang.Long created_time = java.lang.Long.valueOf(getStagedDataSp(userId).getLong(KEY_STAGED_DATA_TIME, -1L));
        if (created_time.longValue() != -1 && created_time.longValue() < this.mClock.millis() - STAGE_DATA_RETENTION_PERIOD.toMillis()) {
            deleteStagedDataLocked(userId);
        }
    }

    public void stageAndApplyRestoredPayload(byte[] payload, int userId) {
        if (payload == null) {
            android.util.Slog.e(TAG, "stageAndApplyRestoredPayload: no payload to restore for user " + userId);
            return;
        }
        java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(payload);
        try {
            com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.newFastPullParser();
            parser.setInput(inputStream, java.nio.charset.StandardCharsets.UTF_8.name());
            com.android.internal.util.XmlUtils.beginDocument(parser, "locales");
            java.util.HashMap<java.lang.String, com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo> pkgStates = readFromXml(parser);
            synchronized (this.mStagedDataLock) {
                for (java.lang.String pkgName : pkgStates.keySet()) {
                    com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo localesInfo = pkgStates.get(pkgName);
                    if (isPackageInstalledForUser(pkgName, userId)) {
                        removeFromArchivedPackagesInfo(userId, pkgName);
                        checkExistingLocalesAndApplyRestore(pkgName, localesInfo, userId);
                    } else {
                        storeStagedDataInfo(userId, pkgName, localesInfo);
                    }
                }
                if (!getStagedDataSp(userId).getAll().isEmpty()) {
                    storeStagedDataCreatedTime(userId);
                }
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Could not parse payload ", e);
        }
    }

    public void notifyBackupManager() {
        android.app.backup.BackupManager.dataChanged("android");
    }

    void onPackageAddedWithExtras(java.lang.String packageName, int uid, android.os.Bundle extras) {
        int userId = android.os.UserHandle.getUserId(uid);
        if (extras != null) {
            boolean archived = extras.getBoolean("android.intent.extra.ARCHIVAL", false);
            if (archived) {
                addInArchivedPackagesInfo(userId, packageName);
            }
        }
        checkStageDataAndApplyRestore(packageName, userId);
    }

    void onPackageUpdateFinished(java.lang.String packageName, int uid) {
        int userId = android.os.UserHandle.getUserId(uid);
        java.lang.String user = java.lang.Integer.toString(userId);
        java.io.File file = getArchivedPackagesFile();
        if (file.exists()) {
            android.content.SharedPreferences sp = getArchivedPackagesSp(file);
            java.util.Set<java.lang.String> packageNames = new android.util.ArraySet<>(sp.getStringSet(user, new android.util.ArraySet()));
            if (packageNames.remove(packageName)) {
                android.content.SharedPreferences.Editor editor = sp.edit();
                if (packageNames.isEmpty()) {
                    if (!editor.remove(user).commit()) {
                        android.util.Slog.e(TAG, "Failed to remove the user");
                    }
                    if (sp.getAll().isEmpty()) {
                        file.delete();
                    }
                } else if (!editor.putStringSet(user, packageNames).commit()) {
                    android.util.Slog.e(TAG, "failed to remove the package");
                }
                checkStageDataAndApplyRestore(packageName, userId);
            }
        }
        cleanApplicationLocalesIfNeeded(packageName, userId);
    }

    void onPackageDataCleared(java.lang.String packageName, int uid) {
        try {
            notifyBackupManager();
            int userId = android.os.UserHandle.getUserId(uid);
            removePackageFromPersistedInfo(packageName, userId);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Exception in onPackageDataCleared.", e);
        }
    }

    void onPackageRemoved(java.lang.String packageName, int uid) {
        try {
            notifyBackupManager();
            int userId = android.os.UserHandle.getUserId(uid);
            removePackageFromPersistedInfo(packageName, userId);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Exception in onPackageRemoved.", e);
        }
    }

    private void checkStageDataAndApplyRestore(java.lang.String packageName, int userId) {
        try {
            synchronized (this.mStagedDataLock) {
                cleanStagedDataForOldEntriesLocked(userId);
                if (!getStagedDataSp(userId).getString(packageName, "").isEmpty()) {
                    removeFromArchivedPackagesInfo(userId, packageName);
                    doLazyRestoreLocked(packageName, userId);
                }
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Exception in onPackageAdded.", e);
        }
    }

    private boolean isPackageInstalledForUser(java.lang.String packageName, int userId) {
        android.content.pm.PackageInfo pkgInfo = null;
        try {
            pkgInfo = this.mContext.getPackageManager().getPackageInfoAsUser(packageName, 0, userId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
        return pkgInfo != null;
    }

    private void checkExistingLocalesAndApplyRestore(java.lang.String pkgName, com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo localesInfo, int userId) {
        if (localesInfo == null) {
            android.util.Slog.w(TAG, "No locales info for " + pkgName);
            return;
        }
        try {
            android.os.LocaleList currLocales = this.mLocaleManagerService.getApplicationLocales(pkgName, userId);
            if (!currLocales.isEmpty()) {
                return;
            }
        } catch (android.os.RemoteException | java.lang.IllegalArgumentException e) {
            android.util.Slog.e(TAG, "Could not check for current locales before restoring", e);
        }
        try {
            this.mLocaleManagerService.setApplicationLocales(pkgName, userId, android.os.LocaleList.forLanguageTags(localesInfo.mLocales), localesInfo.mSetFromDelegate, 3);
        } catch (android.os.RemoteException | java.lang.IllegalArgumentException e2) {
            android.util.Slog.e(TAG, "Could not restore locales for " + pkgName, e2);
        }
    }

    void deleteStagedDataLocked(int userId) {
        java.io.File stagedFile = getStagedDataFile(userId);
        android.content.SharedPreferences sp = getStagedDataSp(stagedFile);
        if (!sp.edit().clear().commit()) {
            android.util.Slog.e(TAG, "Failed to commit data!");
        }
        if (stagedFile.exists()) {
            stagedFile.delete();
        }
    }

    private java.util.HashMap<java.lang.String, com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo> readFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.util.HashMap<java.lang.String, com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo> packageStates = new java.util.HashMap<>();
        int depth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
            if (parser.getName().equals("package")) {
                java.lang.String packageName = parser.getAttributeValue((java.lang.String) null, "name");
                java.lang.String languageTags = parser.getAttributeValue((java.lang.String) null, "locales");
                boolean delegateSelector = parser.getAttributeBoolean((java.lang.String) null, ATTR_DELEGATE_SELECTOR, false);
                if (!android.text.TextUtils.isEmpty(packageName) && !android.text.TextUtils.isEmpty(languageTags)) {
                    com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo localesInfo = new com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo(languageTags, delegateSelector);
                    packageStates.put(packageName, localesInfo);
                }
            }
        }
        return packageStates;
    }

    private static void writeToXml(java.io.OutputStream stream, java.util.HashMap<java.lang.String, com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo> pkgStates) throws java.io.IOException {
        if (pkgStates.isEmpty()) {
            return;
        }
        com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.newFastSerializer();
        out.setOutput(stream, java.nio.charset.StandardCharsets.UTF_8.name());
        out.startDocument((java.lang.String) null, true);
        out.startTag((java.lang.String) null, "locales");
        for (java.lang.String pkg : pkgStates.keySet()) {
            out.startTag((java.lang.String) null, "package");
            out.attribute((java.lang.String) null, "name", pkg);
            out.attribute((java.lang.String) null, "locales", pkgStates.get(pkg).mLocales);
            out.attributeBoolean((java.lang.String) null, ATTR_DELEGATE_SELECTOR, pkgStates.get(pkg).mSetFromDelegate);
            out.endTag((java.lang.String) null, "package");
        }
        out.endTag((java.lang.String) null, "locales");
        out.endDocument();
    }

    static class LocalesInfo {
        final java.lang.String mLocales;
        final boolean mSetFromDelegate;

        LocalesInfo(java.lang.String locales, boolean setFromDelegate) {
            this.mLocales = locales;
            this.mSetFromDelegate = setFromDelegate;
        }
    }

    private final class UserMonitor extends android.content.BroadcastReceiver {
        private UserMonitor() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            try {
                java.lang.String action = intent.getAction();
                if (action.equals("android.intent.action.USER_REMOVED")) {
                    int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
                    synchronized (com.android.server.locales.LocaleManagerBackupHelper.this.mStagedDataLock) {
                        com.android.server.locales.LocaleManagerBackupHelper.this.deleteStagedDataLocked(userId);
                        com.android.server.locales.LocaleManagerBackupHelper.this.removeProfileFromPersistedInfo(userId);
                        com.android.server.locales.LocaleManagerBackupHelper.this.removeArchivedPackagesForUser(userId);
                    }
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.locales.LocaleManagerBackupHelper.TAG, "Exception in user monitor.", e);
            }
        }
    }

    private void doLazyRestoreLocked(java.lang.String packageName, int userId) {
        if (!isPackageInstalledForUser(packageName, userId)) {
            android.util.Slog.e(TAG, packageName + " not installed for user " + userId + ". Could not restore locales from stage data");
            return;
        }
        android.content.SharedPreferences sp = getStagedDataSp(userId);
        java.lang.String value = sp.getString(packageName, "");
        if (!value.isEmpty()) {
            java.lang.String[] info = value.split(STRING_SPLIT);
            if (info == null || info.length != 2) {
                android.util.Slog.e(TAG, "Failed to restore data");
                return;
            }
            com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo localesInfo = new com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo(info[0], java.lang.Boolean.parseBoolean(info[1]));
            checkExistingLocalesAndApplyRestore(packageName, localesInfo, userId);
            if (!sp.edit().remove(packageName).commit()) {
                android.util.Slog.e(TAG, "Failed to commit data!");
            }
        }
        if (sp.getAll().size() == 1 && sp.getLong(KEY_STAGED_DATA_TIME, -1L) != -1) {
            deleteStagedDataLocked(userId);
        }
    }

    private java.io.File getStagedDataFile(int userId) {
        return this.mStagedDataFiles == null ? new java.io.File(android.os.Environment.getDataSystemDeDirectory(userId), LOCALES_STAGED_DATA_PREFS) : this.mStagedDataFiles.get(userId);
    }

    private android.content.SharedPreferences getStagedDataSp(java.io.File file) {
        if (this.mStagedDataFiles == null) {
            return this.mContext.createDeviceProtectedStorageContext().getSharedPreferences(file, 0);
        }
        return this.mContext.getSharedPreferences(file, 0);
    }

    private android.content.SharedPreferences getStagedDataSp(int userId) {
        if (this.mStagedDataFiles == null) {
            return this.mContext.createDeviceProtectedStorageContext().getSharedPreferences(getStagedDataFile(userId), 0);
        }
        return this.mContext.getSharedPreferences(this.mStagedDataFiles.get(userId), 0);
    }

    private void storeStagedDataInfo(int userId, java.lang.String packageName, com.android.server.locales.LocaleManagerBackupHelper.LocalesInfo localesInfo) {
        java.lang.String info = localesInfo.mLocales + STRING_SPLIT + java.lang.String.valueOf(localesInfo.mSetFromDelegate);
        android.content.SharedPreferences sp = getStagedDataSp(userId);
        if (!sp.edit().putString(packageName, info).commit()) {
            android.util.Slog.e(TAG, "Failed to commit data!");
        }
    }

    private void storeStagedDataCreatedTime(int userId) {
        android.content.SharedPreferences sp = getStagedDataSp(userId);
        if (!sp.edit().putLong(KEY_STAGED_DATA_TIME, this.mClock.millis()).commit()) {
            android.util.Slog.e(TAG, "Failed to commit data!");
        }
    }

    private java.io.File getArchivedPackagesFile() {
        if (this.mArchivedPackagesFile == null) {
            return new java.io.File(android.os.Environment.getDataSystemDeDirectory(0), ARCHIVED_PACKAGES_PREFS);
        }
        return this.mArchivedPackagesFile;
    }

    private android.content.SharedPreferences getArchivedPackagesSp(java.io.File file) {
        if (this.mArchivedPackagesFile == null) {
            return this.mContext.createDeviceProtectedStorageContext().getSharedPreferences(file, 0);
        }
        return this.mContext.getSharedPreferences(file, 0);
    }

    private void addInArchivedPackagesInfo(int userId, java.lang.String packageName) {
        java.lang.String user = java.lang.Integer.toString(userId);
        android.content.SharedPreferences sp = getArchivedPackagesSp(getArchivedPackagesFile());
        java.util.Set<java.lang.String> packageNames = new android.util.ArraySet<>(sp.getStringSet(user, new android.util.ArraySet()));
        if (packageNames.add(packageName) && !sp.edit().putStringSet(user, packageNames).commit()) {
            android.util.Slog.e(TAG, "failed to add the package");
        }
    }

    private void removeFromArchivedPackagesInfo(int userId, java.lang.String packageName) {
        java.io.File file = getArchivedPackagesFile();
        if (file.exists()) {
            java.lang.String user = java.lang.Integer.toString(userId);
            android.content.SharedPreferences sp = getArchivedPackagesSp(getArchivedPackagesFile());
            java.util.Set<java.lang.String> packageNames = new android.util.ArraySet<>(sp.getStringSet(user, new android.util.ArraySet()));
            if (packageNames.remove(packageName)) {
                android.content.SharedPreferences.Editor editor = sp.edit();
                if (packageNames.isEmpty()) {
                    if (!editor.remove(user).commit()) {
                        android.util.Slog.e(TAG, "Failed to remove user");
                    }
                    if (sp.getAll().isEmpty()) {
                        file.delete();
                        return;
                    }
                    return;
                }
                if (!editor.putStringSet(user, packageNames).commit()) {
                    android.util.Slog.e(TAG, "failed to remove the package");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeArchivedPackagesForUser(int userId) {
        java.lang.String user = java.lang.Integer.toString(userId);
        java.io.File file = getArchivedPackagesFile();
        android.content.SharedPreferences sp = getArchivedPackagesSp(file);
        if (sp == null || !sp.contains(user)) {
            android.util.Slog.w(TAG, "The profile is not existed in the archived package info");
            return;
        }
        if (!sp.edit().remove(user).commit()) {
            android.util.Slog.e(TAG, "Failed to remove user");
        }
        if (sp.getAll().isEmpty() && file.exists()) {
            file.delete();
        }
    }

    android.content.SharedPreferences createPersistedInfo() {
        java.io.File prefsFile = new java.io.File(android.os.Environment.getDataSystemDeDirectory(0), LOCALES_FROM_DELEGATE_PREFS);
        return this.mContext.createDeviceProtectedStorageContext().getSharedPreferences(prefsFile, 0);
    }

    public android.content.SharedPreferences getPersistedInfo() {
        return this.mDelegateAppLocalePackages;
    }

    private void removePackageFromPersistedInfo(java.lang.String packageName, int userId) {
        if (this.mDelegateAppLocalePackages == null) {
            android.util.Slog.w(TAG, "Failed to persist data into the shared preference!");
            return;
        }
        java.lang.String key = java.lang.Integer.toString(userId);
        java.util.Set<java.lang.String> packageNames = new android.util.ArraySet<>(this.mDelegateAppLocalePackages.getStringSet(key, new android.util.ArraySet()));
        if (packageNames.contains(packageName)) {
            packageNames.remove(packageName);
            android.content.SharedPreferences.Editor editor = this.mDelegateAppLocalePackages.edit();
            editor.putStringSet(key, packageNames);
            if (!editor.commit()) {
                android.util.Slog.e(TAG, "Failed to commit data!");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeProfileFromPersistedInfo(int userId) {
        java.lang.String key = java.lang.Integer.toString(userId);
        if (this.mDelegateAppLocalePackages == null || !this.mDelegateAppLocalePackages.contains(key)) {
            android.util.Slog.w(TAG, "The profile is not existed in the persisted info");
        } else if (!this.mDelegateAppLocalePackages.edit().remove(key).commit()) {
            android.util.Slog.e(TAG, "Failed to commit data!");
        }
    }

    void persistLocalesModificationInfo(int userId, java.lang.String packageName, boolean fromDelegate, boolean emptyLocales) {
        if (this.mDelegateAppLocalePackages == null) {
            android.util.Slog.w(TAG, "Failed to persist data into the shared preference!");
            return;
        }
        android.content.SharedPreferences.Editor editor = this.mDelegateAppLocalePackages.edit();
        java.lang.String user = java.lang.Integer.toString(userId);
        java.util.Set<java.lang.String> packageNames = new android.util.ArraySet<>(this.mDelegateAppLocalePackages.getStringSet(user, new android.util.ArraySet()));
        if (fromDelegate && !emptyLocales) {
            if (!packageNames.contains(packageName)) {
                packageNames.add(packageName);
                editor.putStringSet(user, packageNames);
            }
        } else if (packageNames.contains(packageName)) {
            packageNames.remove(packageName);
            editor.putStringSet(user, packageNames);
        }
        if (!editor.commit()) {
            android.util.Slog.e(TAG, "failed to commit locale setter info");
        }
    }

    boolean areLocalesSetFromDelegate(int userId, java.lang.String packageName) {
        if (this.mDelegateAppLocalePackages == null) {
            android.util.Slog.w(TAG, "Failed to persist data into the shared preference!");
            return false;
        }
        java.lang.String user = java.lang.Integer.toString(userId);
        java.util.Set<java.lang.String> packageNames = new android.util.ArraySet<>(this.mDelegateAppLocalePackages.getStringSet(user, new android.util.ArraySet()));
        return packageNames.contains(packageName);
    }

    private void cleanApplicationLocalesIfNeeded(java.lang.String packageName, int userId) {
        if (this.mDelegateAppLocalePackages == null) {
            android.util.Slog.w(TAG, "Failed to persist data into the shared preference!");
            return;
        }
        java.lang.String user = java.lang.Integer.toString(userId);
        java.util.Set<java.lang.String> packageNames = new android.util.ArraySet<>(this.mDelegateAppLocalePackages.getStringSet(user, new android.util.ArraySet()));
        try {
            android.os.LocaleList appLocales = this.mLocaleManagerService.getApplicationLocales(packageName, userId);
            if (appLocales.isEmpty()) {
                return;
            }
            if (!packageNames.contains(packageName)) {
                return;
            }
            try {
                android.app.LocaleConfig localeConfig = new android.app.LocaleConfig(this.mContext.createPackageContextAsUser(packageName, 0, android.os.UserHandle.of(userId)));
                this.mLocaleManagerService.removeUnsupportedAppLocales(packageName, userId, localeConfig, 4);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Slog.e(TAG, "Can not found the package name : " + packageName + " / " + e);
            }
        } catch (android.os.RemoteException | java.lang.IllegalArgumentException e2) {
            android.util.Slog.e(TAG, "Exception when getting locales for " + packageName, e2);
        }
    }
}
