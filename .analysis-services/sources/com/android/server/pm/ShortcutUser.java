package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class ShortcutUser {
    private static final java.lang.String ATTR_KNOWN_LOCALES = "locales";
    private static final java.lang.String ATTR_LAST_APP_SCAN_OS_FINGERPRINT = "last-app-scan-fp";
    private static final java.lang.String ATTR_LAST_APP_SCAN_TIME = "last-app-scan-time2";
    private static final java.lang.String ATTR_RESTORE_SOURCE_FINGERPRINT = "restore-from-fp";
    private static final java.lang.String ATTR_VALUE = "value";
    static final java.lang.String DIRECTORY_LUANCHERS = "launchers";
    static final java.lang.String DIRECTORY_PACKAGES = "packages";
    private static final java.lang.String KEY_LAUNCHERS = "launchers";
    private static final java.lang.String KEY_PACKAGES = "packages";
    private static final java.lang.String KEY_USER_ID = "userId";
    private static final java.lang.String TAG = "ShortcutService";
    private static final java.lang.String TAG_LAUNCHER = "launcher";
    static final java.lang.String TAG_ROOT = "user";
    final android.app.appsearch.AppSearchManager mAppSearchManager;
    private java.lang.String mCachedLauncher;
    private java.lang.String mKnownLocales;
    private java.lang.String mLastAppScanOsFingerprint;
    private long mLastAppScanTime;
    private java.lang.String mRestoreFromOsFingerprint;
    final com.android.server.pm.ShortcutService mService;
    private final int mUserId;
    private final android.util.ArrayMap<java.lang.String, com.android.server.pm.ShortcutPackage> mPackages = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<android.content.pm.UserPackage, com.android.server.pm.ShortcutLauncher> mLaunchers = new android.util.ArrayMap<>();
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.ArrayList<com.android.internal.infra.AndroidFuture<android.app.appsearch.AppSearchSession>> mInFlightSessions = new java.util.ArrayList<>();
    final java.util.concurrent.Executor mExecutor = com.android.server.FgThread.getExecutor();

    public ShortcutUser(com.android.server.pm.ShortcutService service, int userId) {
        this.mService = service;
        this.mUserId = userId;
        this.mAppSearchManager = (android.app.appsearch.AppSearchManager) service.mContext.createContextAsUser(android.os.UserHandle.of(userId), 0).getSystemService(android.app.appsearch.AppSearchManager.class);
    }

    public int getUserId() {
        return this.mUserId;
    }

    public long getLastAppScanTime() {
        return this.mLastAppScanTime;
    }

    public void setLastAppScanTime(long lastAppScanTime) {
        this.mLastAppScanTime = lastAppScanTime;
    }

    public java.lang.String getLastAppScanOsFingerprint() {
        return this.mLastAppScanOsFingerprint;
    }

    public void setLastAppScanOsFingerprint(java.lang.String lastAppScanOsFingerprint) {
        this.mLastAppScanOsFingerprint = lastAppScanOsFingerprint;
    }

    android.util.ArrayMap<java.lang.String, com.android.server.pm.ShortcutPackage> getAllPackagesForTest() {
        return this.mPackages;
    }

    public boolean hasPackage(java.lang.String packageName) {
        return this.mPackages.containsKey(packageName);
    }

    private void addPackage(com.android.server.pm.ShortcutPackage p) {
        p.replaceUser(this);
        this.mPackages.put(p.getPackageName(), p);
    }

    public com.android.server.pm.ShortcutPackage removePackage(java.lang.String packageName) {
        com.android.server.pm.ShortcutPackage removed = this.mPackages.remove(packageName);
        if (removed != null) {
            removed.removeAllShortcutsAsync();
        }
        this.mService.cleanupBitmapsForPackage(this.mUserId, packageName);
        return removed;
    }

    android.util.ArrayMap<android.content.pm.UserPackage, com.android.server.pm.ShortcutLauncher> getAllLaunchersForTest() {
        return this.mLaunchers;
    }

    private void addLauncher(com.android.server.pm.ShortcutLauncher launcher) {
        launcher.replaceUser(this);
        this.mLaunchers.put(android.content.pm.UserPackage.of(launcher.getPackageUserId(), launcher.getPackageName()), launcher);
    }

    public com.android.server.pm.ShortcutLauncher removeLauncher(int packageUserId, java.lang.String packageName) {
        return this.mLaunchers.remove(android.content.pm.UserPackage.of(packageUserId, packageName));
    }

    public com.android.server.pm.ShortcutPackage getPackageShortcutsIfExists(java.lang.String packageName) {
        com.android.server.pm.ShortcutPackage ret = this.mPackages.get(packageName);
        if (ret != null) {
            ret.attemptToRestoreIfNeededAndSave();
        }
        return ret;
    }

    public com.android.server.pm.ShortcutPackage getPackageShortcuts(java.lang.String packageName) {
        com.android.server.pm.ShortcutPackage ret = getPackageShortcutsIfExists(packageName);
        if (ret == null) {
            com.android.server.pm.ShortcutPackage ret2 = new com.android.server.pm.ShortcutPackage(this, this.mUserId, packageName);
            this.mPackages.put(packageName, ret2);
            return ret2;
        }
        return ret;
    }

    public com.android.server.pm.ShortcutLauncher getLauncherShortcuts(java.lang.String packageName, int launcherUserId) {
        android.content.pm.UserPackage key = android.content.pm.UserPackage.of(launcherUserId, packageName);
        com.android.server.pm.ShortcutLauncher ret = this.mLaunchers.get(key);
        if (ret == null) {
            ret = new com.android.server.pm.ShortcutLauncher(this, this.mUserId, packageName, launcherUserId);
            this.mLaunchers.put(key, ret);
        }
        ret.attemptToRestoreIfNeededAndSave();
        return ret;
    }

    public void forAllPackages(java.util.function.Consumer<? super com.android.server.pm.ShortcutPackage> callback) {
        int size = this.mPackages.size();
        for (int i = 0; i < size; i++) {
            callback.accept(this.mPackages.valueAt(i));
        }
    }

    public void forAllLaunchers(java.util.function.Consumer<? super com.android.server.pm.ShortcutLauncher> callback) {
        int size = this.mLaunchers.size();
        for (int i = 0; i < size; i++) {
            callback.accept(this.mLaunchers.valueAt(i));
        }
    }

    public void forAllPackageItems(java.util.function.Consumer<? super com.android.server.pm.ShortcutPackageItem> callback) {
        forAllLaunchers(callback);
        forAllPackages(callback);
    }

    public void forPackageItem(final java.lang.String packageName, final int packageUserId, final java.util.function.Consumer<com.android.server.pm.ShortcutPackageItem> callback) {
        forAllPackageItems(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutUser$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutUser.lambda$forPackageItem$0(packageUserId, packageName, callback, (com.android.server.pm.ShortcutPackageItem) obj);
            }
        });
    }

    static /* synthetic */ void lambda$forPackageItem$0(int packageUserId, java.lang.String packageName, java.util.function.Consumer callback, com.android.server.pm.ShortcutPackageItem spi) {
        if (spi.getPackageUserId() == packageUserId && spi.getPackageName().equals(packageName)) {
            callback.accept(spi);
        }
    }

    public void onCalledByPublisher(java.lang.String packageName) {
        detectLocaleChange();
        rescanPackageIfNeeded(packageName, false);
    }

    private java.lang.String getKnownLocales() {
        if (android.text.TextUtils.isEmpty(this.mKnownLocales)) {
            this.mKnownLocales = this.mService.injectGetLocaleTagsForUser(this.mUserId);
            this.mService.scheduleSaveUser(this.mUserId);
        }
        return this.mKnownLocales;
    }

    public void detectLocaleChange() {
        java.lang.String currentLocales = this.mService.injectGetLocaleTagsForUser(this.mUserId);
        if (!android.text.TextUtils.isEmpty(this.mKnownLocales) && this.mKnownLocales.equals(currentLocales)) {
            return;
        }
        if (com.android.server.pm.ShortcutService.DEBUG) {
            android.util.Slog.d(TAG, "Locale changed from " + this.mKnownLocales + " to " + currentLocales + " for user " + this.mUserId);
        }
        this.mKnownLocales = currentLocales;
        forAllPackages(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutUser$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutUser.lambda$detectLocaleChange$1((com.android.server.pm.ShortcutPackage) obj);
            }
        });
        this.mService.scheduleSaveUser(this.mUserId);
    }

    static /* synthetic */ void lambda$detectLocaleChange$1(com.android.server.pm.ShortcutPackage pkg) {
        pkg.resetRateLimiting();
        pkg.resolveResourceStrings();
    }

    public void rescanPackageIfNeeded(java.lang.String packageName, boolean forceRescan) {
        boolean isNewApp = !this.mPackages.containsKey(packageName);
        android.util.Slog.d(TAG, "rescanPackageIfNeeded " + getUserId() + "@" + packageName + ", forceRescan=" + forceRescan + " , isNewApp=" + isNewApp);
        com.android.server.pm.ShortcutPackage shortcutPackage = getPackageShortcuts(packageName);
        if (!shortcutPackage.rescanPackageIfNeeded(isNewApp, forceRescan) && isNewApp) {
            this.mPackages.remove(packageName);
        }
    }

    public void attemptToRestoreIfNeededAndSave(com.android.server.pm.ShortcutService s, java.lang.String packageName, int packageUserId) {
        forPackageItem(packageName, packageUserId, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutUser$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.pm.ShortcutPackageItem) obj).attemptToRestoreIfNeededAndSave();
            }
        });
    }

    public void saveToXml(com.android.modules.utils.TypedXmlSerializer out, boolean forBackup) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        out.startTag((java.lang.String) null, TAG_ROOT);
        if (!forBackup) {
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_KNOWN_LOCALES, this.mKnownLocales);
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_LAST_APP_SCAN_TIME, this.mLastAppScanTime);
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_LAST_APP_SCAN_OS_FINGERPRINT, this.mLastAppScanOsFingerprint);
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_RESTORE_SOURCE_FINGERPRINT, this.mRestoreFromOsFingerprint);
        } else {
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_RESTORE_SOURCE_FINGERPRINT, this.mService.injectBuildFingerprint());
        }
        if (!forBackup) {
            this.mService.getWrapper().getExtImpl().backupShortcutData(this.mUserId);
            java.io.File root = this.mService.injectUserDataPath(this.mUserId);
            android.os.FileUtils.deleteContents(new java.io.File(root, "packages"));
            android.os.FileUtils.deleteContents(new java.io.File(root, "launchers"));
        }
        int size = this.mLaunchers.size();
        for (int i = 0; i < size; i++) {
            saveShortcutPackageItem(out, this.mLaunchers.valueAt(i), forBackup);
        }
        int size2 = this.mPackages.size();
        for (int i2 = 0; i2 < size2; i2++) {
            saveShortcutPackageItem(out, this.mPackages.valueAt(i2), forBackup);
        }
        out.endTag((java.lang.String) null, TAG_ROOT);
        this.mService.getWrapper().getExtImpl().clearBackupShortcutData(this.mUserId);
    }

    private void saveShortcutPackageItem(com.android.modules.utils.TypedXmlSerializer out, com.android.server.pm.ShortcutPackageItem spi, boolean forBackup) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (forBackup) {
            if (spi.getPackageUserId() != spi.getOwnerUserId()) {
                return;
            }
            spi.waitForBitmapSaves();
            spi.saveToXml(out, forBackup);
            return;
        }
        spi.scheduleSave();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:21:0x006d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.server.pm.ShortcutUser loadFromXml(final com.android.server.pm.ShortcutService r17, com.android.modules.utils.TypedXmlPullParser r18, final int r19, final boolean r20) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, com.android.server.pm.ShortcutService.InvalidFileFormatException {
        /*
            Method dump skipped, instruction units count: 260
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShortcutUser.loadFromXml(com.android.server.pm.ShortcutService, com.android.modules.utils.TypedXmlPullParser, int, boolean):com.android.server.pm.ShortcutUser");
    }

    static /* synthetic */ void lambda$loadFromXml$3(com.android.server.pm.ShortcutService s, com.android.server.pm.ShortcutUser ret, boolean fromBackup, java.io.File f) {
        com.android.server.pm.ShortcutPackage sp = com.android.server.pm.ShortcutPackage.loadFromFile(s, ret, f, fromBackup);
        if (sp != null) {
            ret.mPackages.put(sp.getPackageName(), sp);
        }
    }

    static /* synthetic */ void lambda$loadFromXml$4(com.android.server.pm.ShortcutUser ret, int userId, boolean fromBackup, java.io.File f) {
        com.android.server.pm.ShortcutLauncher sl = com.android.server.pm.ShortcutLauncher.loadFromFile(f, ret, userId, fromBackup);
        if (sl != null) {
            ret.addLauncher(sl);
        }
    }

    private static void forMainFilesIn(java.io.File path, java.util.function.Consumer<java.io.File> callback) {
        if (!path.exists()) {
            return;
        }
        java.io.File[] list = path.listFiles();
        for (java.io.File f : list) {
            if (!f.getName().endsWith(".reservecopy") && !f.getName().endsWith(".backup")) {
                callback.accept(f);
            }
        }
    }

    public void setCachedLauncher(java.lang.String launcher) {
        this.mCachedLauncher = launcher;
    }

    public java.lang.String getCachedLauncher() {
        return this.mCachedLauncher;
    }

    public void resetThrottling() {
        for (int i = this.mPackages.size() - 1; i >= 0; i--) {
            this.mPackages.valueAt(i).resetThrottling();
        }
    }

    public void mergeRestoredFile(com.android.server.pm.ShortcutUser restored) {
        final com.android.server.pm.ShortcutService s = this.mService;
        final int[] restoredLaunchers = new int[1];
        final int[] restoredPackages = new int[1];
        final int[] restoredShortcuts = new int[1];
        this.mLaunchers.clear();
        restored.forAllLaunchers(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutUser$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$mergeRestoredFile$5(s, restoredLaunchers, (com.android.server.pm.ShortcutLauncher) obj);
            }
        });
        restored.forAllPackages(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutUser$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$mergeRestoredFile$6(s, restoredPackages, restoredShortcuts, (com.android.server.pm.ShortcutPackage) obj);
            }
        });
        restored.mLaunchers.clear();
        restored.mPackages.clear();
        this.mRestoreFromOsFingerprint = restored.mRestoreFromOsFingerprint;
        android.util.Slog.i(TAG, "Restored: L=" + restoredLaunchers[0] + " P=" + restoredPackages[0] + " S=" + restoredShortcuts[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$mergeRestoredFile$5(com.android.server.pm.ShortcutService s, int[] restoredLaunchers, com.android.server.pm.ShortcutLauncher sl) {
        if (s.isPackageInstalled(sl.getPackageName(), getUserId()) && !s.shouldBackupApp(sl.getPackageName(), getUserId())) {
            return;
        }
        addLauncher(sl);
        restoredLaunchers[0] = restoredLaunchers[0] + 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$mergeRestoredFile$6(com.android.server.pm.ShortcutService s, int[] restoredPackages, int[] restoredShortcuts, com.android.server.pm.ShortcutPackage sp) {
        if (s.isPackageInstalled(sp.getPackageName(), getUserId()) && !s.shouldBackupApp(sp.getPackageName(), getUserId())) {
            return;
        }
        com.android.server.pm.ShortcutPackage previous = getPackageShortcutsIfExists(sp.getPackageName());
        if (previous != null && previous.hasNonManifestShortcuts()) {
            android.util.Log.w(TAG, "Shortcuts for package " + sp.getPackageName() + " are being restored. Existing non-manifeset shortcuts will be overwritten.");
        }
        sp.removeAllShortcutsAsync();
        addPackage(sp);
        restoredPackages[0] = restoredPackages[0] + 1;
        restoredShortcuts[0] = restoredShortcuts[0] + sp.getShortcutCount();
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.pm.ShortcutService.DumpFilter filter) {
        if (filter.shouldDumpDetails()) {
            pw.print(prefix);
            pw.print("User: ");
            pw.print(this.mUserId);
            pw.print("  Known locales: ");
            pw.print(this.mKnownLocales);
            pw.print("  Last app scan: [");
            pw.print(this.mLastAppScanTime);
            pw.print("] ");
            pw.println(com.android.server.pm.ShortcutService.formatTime(this.mLastAppScanTime));
            prefix = prefix + prefix + "  ";
            pw.print(prefix);
            pw.print("Last app scan FP: ");
            pw.println(this.mLastAppScanOsFingerprint);
            pw.print(prefix);
            pw.print("Restore from FP: ");
            pw.print(this.mRestoreFromOsFingerprint);
            pw.println();
            pw.print(prefix);
            pw.print("Cached launcher: ");
            pw.print(this.mCachedLauncher);
            pw.println();
        }
        for (int i = 0; i < this.mLaunchers.size(); i++) {
            com.android.server.pm.ShortcutLauncher launcher = this.mLaunchers.valueAt(i);
            if (filter.isPackageMatch(launcher.getPackageName())) {
                launcher.dump(pw, prefix, filter);
            }
        }
        for (int i2 = 0; i2 < this.mPackages.size(); i2++) {
            com.android.server.pm.ShortcutPackage pkg = this.mPackages.valueAt(i2);
            if (filter.isPackageMatch(pkg.getPackageName())) {
                pkg.dump(pw, prefix, filter);
            }
        }
        if (filter.shouldDumpDetails()) {
            pw.println();
            pw.print(prefix);
            pw.println("Bitmap directories: ");
            dumpDirectorySize(pw, prefix + "  ", this.mService.getUserBitmapFilePath(this.mUserId));
        }
    }

    private void dumpDirectorySize(java.io.PrintWriter pw, java.lang.String prefix, java.io.File path) {
        int numFiles = 0;
        long size = 0;
        java.io.File[] children = path.listFiles();
        if (children != null) {
            for (java.io.File child : path.listFiles()) {
                if (child.isFile()) {
                    numFiles++;
                    size += child.length();
                } else if (child.isDirectory()) {
                    dumpDirectorySize(pw, prefix + "  ", child);
                }
            }
        }
        pw.print(prefix);
        pw.print("Path: ");
        pw.print(path.getName());
        pw.print("/ has ");
        pw.print(numFiles);
        pw.print(" files, size=");
        pw.print(size);
        pw.print(" (");
        pw.print(android.text.format.Formatter.formatFileSize(this.mService.mContext, size));
        pw.println(")");
    }

    public org.json.JSONObject dumpCheckin(boolean clear) throws org.json.JSONException {
        org.json.JSONObject result = new org.json.JSONObject();
        result.put("userId", this.mUserId);
        org.json.JSONArray launchers = new org.json.JSONArray();
        for (int i = 0; i < this.mLaunchers.size(); i++) {
            launchers.put(this.mLaunchers.valueAt(i).dumpCheckin(clear));
        }
        result.put("launchers", launchers);
        org.json.JSONArray packages = new org.json.JSONArray();
        for (int i2 = 0; i2 < this.mPackages.size(); i2++) {
            packages.put(this.mPackages.valueAt(i2).dumpCheckin(clear));
        }
        result.put("packages", packages);
        return result;
    }

    void logSharingShortcutStats(com.android.internal.logging.MetricsLogger logger) {
        int packageWithShareTargetsCount = 0;
        int totalSharingShortcutCount = 0;
        for (int i = 0; i < this.mPackages.size(); i++) {
            if (this.mPackages.valueAt(i).hasShareTargets()) {
                packageWithShareTargetsCount++;
                totalSharingShortcutCount += this.mPackages.valueAt(i).getSharingShortcutCount();
            }
        }
        android.metrics.LogMaker logMaker = new android.metrics.LogMaker(1717);
        logger.write(logMaker.setType(1).setSubtype(this.mUserId));
        logger.write(logMaker.setType(2).setSubtype(packageWithShareTargetsCount));
        logger.write(logMaker.setType(3).setSubtype(totalSharingShortcutCount));
    }

    com.android.internal.infra.AndroidFuture<android.app.appsearch.AppSearchSession> getAppSearch(android.app.appsearch.AppSearchManager.SearchContext searchContext) {
        final com.android.internal.infra.AndroidFuture<android.app.appsearch.AppSearchSession> future = new com.android.internal.infra.AndroidFuture<>();
        synchronized (this.mLock) {
            this.mInFlightSessions.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutUser$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.internal.infra.AndroidFuture) obj).isDone();
                }
            });
            this.mInFlightSessions.add(future);
        }
        if (this.mAppSearchManager == null) {
            future.completeExceptionally(new java.lang.RuntimeException("app search manager is null"));
            return future;
        }
        if (!this.mService.mUserManagerInternal.isUserUnlockingOrUnlocked(getUserId())) {
            future.completeExceptionally(new java.lang.RuntimeException("User " + getUserId() + " is "));
            return future;
        }
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            this.mAppSearchManager.createSearchSession(searchContext, this.mExecutor, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutUser$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.pm.ShortcutUser.lambda$getAppSearch$7(future, (android.app.appsearch.AppSearchResult) obj);
                }
            });
            return future;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
        }
    }

    static /* synthetic */ void lambda$getAppSearch$7(com.android.internal.infra.AndroidFuture future, android.app.appsearch.AppSearchResult result) {
        if (!result.isSuccess()) {
            future.completeExceptionally(new java.lang.RuntimeException(result.getErrorMessage()));
        } else {
            future.complete((android.app.appsearch.AppSearchSession) result.getResultValue());
        }
    }

    void cancelAllInFlightTasks() {
        synchronized (this.mLock) {
            for (com.android.internal.infra.AndroidFuture<android.app.appsearch.AppSearchSession> session : this.mInFlightSessions) {
                session.cancel(true);
            }
            this.mInFlightSessions.clear();
        }
    }
}
