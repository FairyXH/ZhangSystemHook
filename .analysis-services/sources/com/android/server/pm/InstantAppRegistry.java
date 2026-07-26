package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class InstantAppRegistry implements com.android.server.utils.Watchable, com.android.server.utils.Snappable {
    private static final java.lang.String ATTR_GRANTED = "granted";
    private static final java.lang.String ATTR_LABEL = "label";
    private static final java.lang.String ATTR_NAME = "name";
    private static final boolean DEBUG = false;
    private static final long DEFAULT_INSTALLED_INSTANT_APP_MAX_CACHE_PERIOD = 15552000000L;
    static final long DEFAULT_INSTALLED_INSTANT_APP_MIN_CACHE_PERIOD = 604800000;
    private static final long DEFAULT_UNINSTALLED_INSTANT_APP_MAX_CACHE_PERIOD = 15552000000L;
    static final long DEFAULT_UNINSTALLED_INSTANT_APP_MIN_CACHE_PERIOD = 604800000;
    private static final java.lang.String INSTANT_APPS_FOLDER = "instant";
    private static final java.lang.String INSTANT_APP_ANDROID_ID_FILE = "android_id";
    private static final java.lang.String INSTANT_APP_COOKIE_FILE_PREFIX = "cookie_";
    private static final java.lang.String INSTANT_APP_COOKIE_FILE_SIFFIX = ".dat";
    private static final java.lang.String INSTANT_APP_ICON_FILE = "icon.png";
    private static final java.lang.String INSTANT_APP_METADATA_FILE = "metadata.xml";
    private static final java.lang.String LOG_TAG = "InstantAppRegistry";
    private static final java.lang.String TAG_PACKAGE = "package";
    private static final java.lang.String TAG_PERMISSION = "permission";
    private static final java.lang.String TAG_PERMISSIONS = "permissions";
    private final android.content.Context mContext;
    private final com.android.server.pm.InstantAppRegistry.CookiePersistence mCookiePersistence;
    private final com.android.server.pm.DeletePackageHelper mDeletePackageHelper;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedSparseArray<com.android.server.utils.WatchedSparseBooleanArray> mInstalledInstantAppUids;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedSparseArray<com.android.server.utils.WatchedSparseArray<com.android.server.utils.WatchedSparseBooleanArray>> mInstantGrants;
    private final java.lang.Object mLock;
    private final com.android.server.utils.Watcher mObserver;
    private final com.android.server.pm.permission.PermissionManagerServiceInternal mPermissionManager;
    private final com.android.server.utils.SnapshotCache<com.android.server.pm.InstantAppRegistry> mSnapshot;

    @com.android.server.utils.Watched
    private final com.android.server.utils.WatchedSparseArray<java.util.List<com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState>> mUninstalledInstantApps;
    private final com.android.server.pm.UserManagerInternal mUserManager;
    private final com.android.server.utils.WatchableImpl mWatchable;

    @Override // com.android.server.utils.Watchable
    public void registerObserver(com.android.server.utils.Watcher observer) {
        this.mWatchable.registerObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public void unregisterObserver(com.android.server.utils.Watcher observer) {
        this.mWatchable.unregisterObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public boolean isRegisteredObserver(com.android.server.utils.Watcher observer) {
        return this.mWatchable.isRegisteredObserver(observer);
    }

    @Override // com.android.server.utils.Watchable
    public void dispatchChange(com.android.server.utils.Watchable what) {
        this.mWatchable.dispatchChange(what);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onChanged() {
        dispatchChange(this);
    }

    private com.android.server.utils.SnapshotCache<com.android.server.pm.InstantAppRegistry> makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.InstantAppRegistry>(this, this) { // from class: com.android.server.pm.InstantAppRegistry.2
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.InstantAppRegistry createSnapshot() {
                com.android.server.pm.InstantAppRegistry s = new com.android.server.pm.InstantAppRegistry();
                s.mWatchable.seal();
                return s;
            }
        };
    }

    public InstantAppRegistry(android.content.Context context, com.android.server.pm.permission.PermissionManagerServiceInternal permissionManager, com.android.server.pm.UserManagerInternal userManager, com.android.server.pm.DeletePackageHelper deletePackageHelper) {
        this.mLock = new java.lang.Object();
        this.mWatchable = new com.android.server.utils.WatchableImpl();
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.InstantAppRegistry.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.pm.InstantAppRegistry.this.onChanged();
            }
        };
        this.mContext = context;
        this.mPermissionManager = permissionManager;
        this.mUserManager = userManager;
        this.mDeletePackageHelper = deletePackageHelper;
        this.mCookiePersistence = new com.android.server.pm.InstantAppRegistry.CookiePersistence(com.android.internal.os.BackgroundThread.getHandler().getLooper());
        this.mUninstalledInstantApps = new com.android.server.utils.WatchedSparseArray<>();
        this.mInstantGrants = new com.android.server.utils.WatchedSparseArray<>();
        this.mInstalledInstantAppUids = new com.android.server.utils.WatchedSparseArray<>();
        this.mUninstalledInstantApps.registerObserver(this.mObserver);
        this.mInstantGrants.registerObserver(this.mObserver);
        this.mInstalledInstantAppUids.registerObserver(this.mObserver);
        com.android.server.utils.Watchable.verifyWatchedAttributes(this, this.mObserver);
        this.mSnapshot = makeCache();
    }

    private InstantAppRegistry(com.android.server.pm.InstantAppRegistry r) {
        this.mLock = new java.lang.Object();
        this.mWatchable = new com.android.server.utils.WatchableImpl();
        this.mObserver = new com.android.server.utils.Watcher() { // from class: com.android.server.pm.InstantAppRegistry.1
            @Override // com.android.server.utils.Watcher
            public void onChange(com.android.server.utils.Watchable what) {
                com.android.server.pm.InstantAppRegistry.this.onChanged();
            }
        };
        this.mContext = r.mContext;
        this.mPermissionManager = r.mPermissionManager;
        this.mUserManager = r.mUserManager;
        this.mDeletePackageHelper = r.mDeletePackageHelper;
        this.mCookiePersistence = null;
        this.mUninstalledInstantApps = new com.android.server.utils.WatchedSparseArray<>(r.mUninstalledInstantApps);
        this.mInstantGrants = new com.android.server.utils.WatchedSparseArray<>(r.mInstantGrants);
        this.mInstalledInstantAppUids = new com.android.server.utils.WatchedSparseArray<>(r.mInstalledInstantAppUids);
        this.mSnapshot = null;
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.InstantAppRegistry snapshot() {
        return this.mSnapshot.snapshot();
    }

    public byte[] getInstantAppCookie(com.android.server.pm.pkg.AndroidPackage pkg, int userId) {
        synchronized (this.mLock) {
            byte[] pendingCookie = this.mCookiePersistence.getPendingPersistCookieLPr(pkg, userId);
            if (pendingCookie != null) {
                return pendingCookie;
            }
            java.io.File cookieFile = peekInstantCookieFile(pkg.getPackageName(), userId);
            if (cookieFile != null && cookieFile.exists()) {
                try {
                    return libcore.io.IoUtils.readFileAsByteArray(cookieFile.toString());
                } catch (java.io.IOException e) {
                    android.util.Slog.w(LOG_TAG, "Error reading cookie file: " + cookieFile);
                }
            }
            return null;
        }
    }

    public boolean setInstantAppCookie(com.android.server.pm.pkg.AndroidPackage pkg, byte[] cookie, int instantAppCookieMaxBytes, int userId) {
        synchronized (this.mLock) {
            if (cookie != null) {
                if (cookie.length > 0 && cookie.length > instantAppCookieMaxBytes) {
                    android.util.Slog.e(LOG_TAG, "Instant app cookie for package " + pkg.getPackageName() + " size " + cookie.length + " bytes while max size is " + instantAppCookieMaxBytes);
                    return false;
                }
            }
            this.mCookiePersistence.schedulePersistLPw(userId, pkg, cookie);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void persistInstantApplicationCookie(byte[] cookie, java.lang.String packageName, java.io.File cookieFile, int userId) {
        synchronized (this.mLock) {
            java.io.File appDir = getInstantApplicationDir(packageName, userId);
            if (!appDir.exists() && !appDir.mkdirs()) {
                android.util.Slog.e(LOG_TAG, "Cannot create instant app cookie directory");
                return;
            }
            if (cookieFile.exists() && !cookieFile.delete()) {
                android.util.Slog.e(LOG_TAG, "Cannot delete instant app cookie file");
            }
            if (cookie != null && cookie.length > 0) {
                try {
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(cookieFile);
                    try {
                        fos.write(cookie, 0, cookie.length);
                        fos.close();
                    } finally {
                    }
                } catch (java.io.IOException e) {
                    android.util.Slog.e(LOG_TAG, "Error writing instant app cookie file: " + cookieFile, e);
                }
            }
        }
    }

    public android.graphics.Bitmap getInstantAppIcon(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            java.io.File iconFile = new java.io.File(getInstantApplicationDir(packageName, userId), INSTANT_APP_ICON_FILE);
            if (!iconFile.exists()) {
                return null;
            }
            return android.graphics.BitmapFactory.decodeFile(iconFile.toString());
        }
    }

    public java.lang.String getInstantAppAndroidId(java.lang.String packageName, int userId) {
        java.lang.String id;
        java.io.File appDir;
        java.io.File idFile;
        java.io.FileOutputStream fos;
        synchronized (this.mLock) {
            java.io.File idFile2 = new java.io.File(getInstantApplicationDir(packageName, userId), INSTANT_APP_ANDROID_ID_FILE);
            if (idFile2.exists()) {
                try {
                    return libcore.io.IoUtils.readFileAsString(idFile2.getAbsolutePath());
                } catch (java.io.IOException e) {
                    android.util.Slog.e(LOG_TAG, "Failed to read instant app android id file: " + idFile2, e);
                    byte[] randomBytes = new byte[8];
                    new java.security.SecureRandom().nextBytes(randomBytes);
                    id = libcore.util.HexEncoding.encodeToString(randomBytes, false);
                    appDir = getInstantApplicationDir(packageName, userId);
                    if (appDir.exists()) {
                    }
                    idFile = new java.io.File(getInstantApplicationDir(packageName, userId), INSTANT_APP_ANDROID_ID_FILE);
                    try {
                        fos = new java.io.FileOutputStream(idFile);
                    } catch (java.io.IOException e2) {
                        android.util.Slog.e(LOG_TAG, "Error writing instant app android id file: " + idFile, e2);
                    }
                    try {
                        fos.write(id.getBytes());
                        fos.close();
                        return id;
                    } catch (java.lang.Throwable th) {
                        try {
                            fos.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
            }
            byte[] randomBytes2 = new byte[8];
            new java.security.SecureRandom().nextBytes(randomBytes2);
            id = libcore.util.HexEncoding.encodeToString(randomBytes2, false);
            appDir = getInstantApplicationDir(packageName, userId);
            if (appDir.exists() && !appDir.mkdirs()) {
                android.util.Slog.e(LOG_TAG, "Cannot create instant app cookie directory");
                return id;
            }
            idFile = new java.io.File(getInstantApplicationDir(packageName, userId), INSTANT_APP_ANDROID_ID_FILE);
            fos = new java.io.FileOutputStream(idFile);
            fos.write(id.getBytes());
            fos.close();
            return id;
        }
    }

    public java.util.List<android.content.pm.InstantAppInfo> getInstantApps(com.android.server.pm.Computer computer, int userId) {
        java.util.List<android.content.pm.InstantAppInfo> installedApps = getInstalledInstantApplications(computer, userId);
        java.util.List<android.content.pm.InstantAppInfo> uninstalledApps = getUninstalledInstantApplications(computer, userId);
        if (installedApps != null) {
            if (uninstalledApps != null) {
                installedApps.addAll(uninstalledApps);
            }
            return installedApps;
        }
        return uninstalledApps;
    }

    public void onPackageInstalled(com.android.server.pm.Computer computer, java.lang.String packageName, int[] userIds) {
        com.android.server.pm.pkg.PackageStateInternal ps = computer.getPackageStateInternal(packageName);
        final com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = ps == null ? null : ps.getPkg();
        if (pkg == null) {
            return;
        }
        synchronized (this.mLock) {
            for (int userId : userIds) {
                if (ps.getUserStateOrDefault(userId).isInstalled()) {
                    propagateInstantAppPermissionsIfNeeded(pkg, userId);
                    if (ps.getUserStateOrDefault(userId).isInstantApp()) {
                        addInstantApp(userId, ps.getAppId());
                    }
                    removeUninstalledInstantAppStateLPw(new java.util.function.Predicate() { // from class: com.android.server.pm.InstantAppRegistry$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return ((com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState) obj).mInstantAppInfo.getPackageName().equals(pkg.getPackageName());
                        }
                    }, userId);
                    java.io.File instantAppDir = getInstantApplicationDir(pkg.getPackageName(), userId);
                    new java.io.File(instantAppDir, INSTANT_APP_METADATA_FILE).delete();
                    new java.io.File(instantAppDir, INSTANT_APP_ICON_FILE).delete();
                    java.io.File currentCookieFile = peekInstantCookieFile(pkg.getPackageName(), userId);
                    if (currentCookieFile != null) {
                        java.lang.String cookieName = currentCookieFile.getName();
                        java.lang.String currentCookieSha256 = cookieName.substring(INSTANT_APP_COOKIE_FILE_PREFIX.length(), cookieName.length() - INSTANT_APP_COOKIE_FILE_SIFFIX.length());
                        if (pkg.getSigningDetails().checkCapability(currentCookieSha256, 1)) {
                            return;
                        }
                        java.lang.String[] signaturesSha256Digests = android.util.PackageUtils.computeSignaturesSha256Digests(pkg.getSigningDetails().getSignatures());
                        for (java.lang.String s : signaturesSha256Digests) {
                            if (s.equals(currentCookieSha256)) {
                                return;
                            }
                        }
                        android.util.Slog.i(LOG_TAG, "Signature for package " + pkg.getPackageName() + " changed - dropping cookie");
                        this.mCookiePersistence.cancelPendingPersistLPw(pkg, userId);
                        currentCookieFile.delete();
                    }
                }
            }
        }
    }

    public void onPackageUninstalled(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.PackageSetting ps, int[] userIds, boolean packageInstalledForSomeUsers) {
        if (ps == null) {
            return;
        }
        synchronized (this.mLock) {
            for (int userId : userIds) {
                if (!packageInstalledForSomeUsers || !ps.getInstalled(userId)) {
                    if (ps.getInstantApp(userId)) {
                        addUninstalledInstantAppLPw(ps, userId);
                        removeInstantAppLPw(userId, ps.getAppId());
                    } else {
                        deleteDir(getInstantApplicationDir(pkg.getPackageName(), userId));
                        this.mCookiePersistence.cancelPendingPersistLPw(pkg, userId);
                        removeAppLPw(userId, ps.getAppId());
                    }
                }
            }
        }
    }

    public void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            this.mUninstalledInstantApps.remove(userId);
            this.mInstalledInstantAppUids.remove(userId);
            this.mInstantGrants.remove(userId);
            deleteDir(getInstantApplicationsDir(userId));
        }
    }

    public boolean isInstantAccessGranted(int userId, int targetAppId, int instantAppId) {
        synchronized (this.mLock) {
            com.android.server.utils.WatchedSparseArray<com.android.server.utils.WatchedSparseBooleanArray> targetAppList = this.mInstantGrants.get(userId);
            if (targetAppList == null) {
                return false;
            }
            com.android.server.utils.WatchedSparseBooleanArray instantGrantList = targetAppList.get(targetAppId);
            if (instantGrantList == null) {
                return false;
            }
            return instantGrantList.get(instantAppId);
        }
    }

    public boolean grantInstantAccess(int userId, android.content.Intent intent, int recipientUid, int instantAppId) {
        java.util.Set<java.lang.String> categories;
        synchronized (this.mLock) {
            if (this.mInstalledInstantAppUids == null) {
                return false;
            }
            com.android.server.utils.WatchedSparseBooleanArray instantAppList = this.mInstalledInstantAppUids.get(userId);
            if (instantAppList != null && instantAppList.get(instantAppId)) {
                if (instantAppList.get(recipientUid)) {
                    return false;
                }
                if (intent != null && "android.intent.action.VIEW".equals(intent.getAction()) && (categories = intent.getCategories()) != null && categories.contains("android.intent.category.BROWSABLE")) {
                    return false;
                }
                com.android.server.utils.WatchedSparseArray<com.android.server.utils.WatchedSparseBooleanArray> targetAppList = this.mInstantGrants.get(userId);
                if (targetAppList == null) {
                    targetAppList = new com.android.server.utils.WatchedSparseArray<>();
                    this.mInstantGrants.put(userId, targetAppList);
                }
                com.android.server.utils.WatchedSparseBooleanArray instantGrantList = targetAppList.get(recipientUid);
                if (instantGrantList == null) {
                    instantGrantList = new com.android.server.utils.WatchedSparseBooleanArray();
                    targetAppList.put(recipientUid, instantGrantList);
                }
                instantGrantList.put(instantAppId, true);
                return true;
            }
            return false;
        }
    }

    public void addInstantApp(int userId, int instantAppId) {
        synchronized (this.mLock) {
            com.android.server.utils.WatchedSparseBooleanArray instantAppList = this.mInstalledInstantAppUids.get(userId);
            if (instantAppList == null) {
                instantAppList = new com.android.server.utils.WatchedSparseBooleanArray();
                this.mInstalledInstantAppUids.put(userId, instantAppList);
            }
            instantAppList.put(instantAppId, true);
        }
        onChanged();
    }

    private void removeInstantAppLPw(int userId, int instantAppId) {
        com.android.server.utils.WatchedSparseBooleanArray instantAppList;
        if (this.mInstalledInstantAppUids == null || (instantAppList = this.mInstalledInstantAppUids.get(userId)) == null) {
            return;
        }
        try {
            instantAppList.delete(instantAppId);
            if (this.mInstantGrants == null) {
                return;
            }
            com.android.server.utils.WatchedSparseArray<com.android.server.utils.WatchedSparseBooleanArray> targetAppList = this.mInstantGrants.get(userId);
            if (targetAppList == null) {
                return;
            }
            for (int i = targetAppList.size() - 1; i >= 0; i--) {
                targetAppList.valueAt(i).delete(instantAppId);
            }
        } finally {
            onChanged();
        }
    }

    private void removeAppLPw(int userId, int targetAppId) {
        com.android.server.utils.WatchedSparseArray<com.android.server.utils.WatchedSparseBooleanArray> targetAppList;
        if (this.mInstantGrants == null || (targetAppList = this.mInstantGrants.get(userId)) == null) {
            return;
        }
        targetAppList.delete(targetAppId);
        onChanged();
    }

    private void addUninstalledInstantAppLPw(com.android.server.pm.pkg.PackageStateInternal packageState, int userId) {
        android.content.pm.InstantAppInfo uninstalledApp = createInstantAppInfoForPackage(packageState, userId, false);
        if (uninstalledApp == null) {
            return;
        }
        java.util.List<com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState> uninstalledAppStates = this.mUninstalledInstantApps.get(userId);
        if (uninstalledAppStates == null) {
            uninstalledAppStates = new java.util.ArrayList();
            this.mUninstalledInstantApps.put(userId, uninstalledAppStates);
        }
        com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState uninstalledAppState = new com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState(uninstalledApp, java.lang.System.currentTimeMillis());
        uninstalledAppStates.add(uninstalledAppState);
        writeUninstalledInstantAppMetadata(uninstalledApp, userId);
        writeInstantApplicationIconLPw(packageState.getPkg(), userId);
    }

    private void writeInstantApplicationIconLPw(com.android.server.pm.pkg.AndroidPackage pkg, int userId) {
        android.graphics.Bitmap bitmap;
        java.io.File appDir = getInstantApplicationDir(pkg.getPackageName(), userId);
        if (!appDir.exists()) {
            return;
        }
        android.graphics.drawable.Drawable icon = com.android.server.pm.parsing.pkg.AndroidPackageUtils.generateAppInfoWithoutState(pkg).loadIcon(this.mContext.getPackageManager());
        if (icon instanceof android.graphics.drawable.BitmapDrawable) {
            bitmap = ((android.graphics.drawable.BitmapDrawable) icon).getBitmap();
        } else {
            bitmap = android.graphics.Bitmap.createBitmap(icon.getIntrinsicWidth(), icon.getIntrinsicHeight(), android.graphics.Bitmap.Config.ARGB_8888);
            android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            icon.draw(canvas);
        }
        java.io.File iconFile = new java.io.File(getInstantApplicationDir(pkg.getPackageName(), userId), INSTANT_APP_ICON_FILE);
        try {
            java.io.FileOutputStream out = new java.io.FileOutputStream(iconFile);
            try {
                bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out);
                out.close();
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(LOG_TAG, "Error writing instant app icon", e);
        }
    }

    boolean hasInstantApplicationMetadata(java.lang.String packageName, int userId) {
        return hasUninstalledInstantAppState(packageName, userId) || hasInstantAppMetadata(packageName, userId);
    }

    public void deleteInstantApplicationMetadata(final java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            removeUninstalledInstantAppStateLPw(new java.util.function.Predicate() { // from class: com.android.server.pm.InstantAppRegistry$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState) obj).mInstantAppInfo.getPackageName().equals(packageName);
                }
            }, userId);
            java.io.File instantAppDir = getInstantApplicationDir(packageName, userId);
            new java.io.File(instantAppDir, INSTANT_APP_METADATA_FILE).delete();
            new java.io.File(instantAppDir, INSTANT_APP_ICON_FILE).delete();
            new java.io.File(instantAppDir, INSTANT_APP_ANDROID_ID_FILE).delete();
            java.io.File cookie = peekInstantCookieFile(packageName, userId);
            if (cookie != null) {
                cookie.delete();
            }
        }
    }

    private void removeUninstalledInstantAppStateLPw(java.util.function.Predicate<com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState> criteria, int userId) {
        java.util.List<com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState> uninstalledAppStates;
        if (this.mUninstalledInstantApps == null || (uninstalledAppStates = this.mUninstalledInstantApps.get(userId)) == null) {
            return;
        }
        int appCount = uninstalledAppStates.size();
        for (int i = appCount - 1; i >= 0; i--) {
            com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState uninstalledAppState = uninstalledAppStates.get(i);
            if (criteria.test(uninstalledAppState)) {
                uninstalledAppStates.remove(i);
                if (uninstalledAppStates.isEmpty()) {
                    this.mUninstalledInstantApps.remove(userId);
                    onChanged();
                    return;
                }
            }
        }
    }

    private boolean hasUninstalledInstantAppState(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            if (this.mUninstalledInstantApps == null) {
                return false;
            }
            java.util.List<com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState> uninstalledAppStates = this.mUninstalledInstantApps.get(userId);
            if (uninstalledAppStates == null) {
                return false;
            }
            int appCount = uninstalledAppStates.size();
            for (int i = 0; i < appCount; i++) {
                com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState uninstalledAppState = uninstalledAppStates.get(i);
                if (packageName.equals(uninstalledAppState.mInstantAppInfo.getPackageName())) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean hasInstantAppMetadata(java.lang.String packageName, int userId) {
        java.io.File instantAppDir = getInstantApplicationDir(packageName, userId);
        return new java.io.File(instantAppDir, INSTANT_APP_METADATA_FILE).exists() || new java.io.File(instantAppDir, INSTANT_APP_ICON_FILE).exists() || new java.io.File(instantAppDir, INSTANT_APP_ANDROID_ID_FILE).exists() || peekInstantCookieFile(packageName, userId) != null;
    }

    void pruneInstantApps(com.android.server.pm.Computer computer) throws java.lang.Throwable {
        long maxInstalledCacheDuration = android.provider.Settings.Global.getLong(this.mContext.getContentResolver(), "installed_instant_app_max_cache_period", 15552000000L);
        long maxUninstalledCacheDuration = android.provider.Settings.Global.getLong(this.mContext.getContentResolver(), "uninstalled_instant_app_max_cache_period", 15552000000L);
        try {
            pruneInstantApps(computer, Long.MAX_VALUE, maxInstalledCacheDuration, maxUninstalledCacheDuration);
        } catch (java.io.IOException e) {
            android.util.Slog.e(LOG_TAG, "Error pruning installed and uninstalled instant apps", e);
        }
    }

    boolean pruneInstalledInstantApps(com.android.server.pm.Computer computer, long neededSpace, long maxInstalledCacheDuration) {
        try {
            return pruneInstantApps(computer, neededSpace, maxInstalledCacheDuration, Long.MAX_VALUE);
        } catch (java.io.IOException e) {
            android.util.Slog.e(LOG_TAG, "Error pruning installed instant apps", e);
            return false;
        }
    }

    boolean pruneUninstalledInstantApps(com.android.server.pm.Computer computer, long neededSpace, long maxUninstalledCacheDuration) {
        try {
            return pruneInstantApps(computer, neededSpace, Long.MAX_VALUE, maxUninstalledCacheDuration);
        } catch (java.io.IOException e) {
            android.util.Slog.e(LOG_TAG, "Error pruning uninstalled instant apps", e);
            return false;
        }
    }

    private boolean pruneInstantApps(com.android.server.pm.Computer computer, long neededSpace, long maxInstalledCacheDuration, final long maxUninstalledCacheDuration) throws java.lang.Throwable {
        int[] iArr;
        int i;
        long now;
        java.io.File[] files;
        int i2;
        long now2;
        android.os.storage.StorageManager storage;
        com.android.server.pm.InstantAppRegistry instantAppRegistry = this;
        android.os.storage.StorageManager storage2 = (android.os.storage.StorageManager) instantAppRegistry.mContext.getSystemService(android.os.storage.StorageManager.class);
        java.io.File file = storage2.findPathForUuid(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL);
        if (file.getUsableSpace() >= neededSpace) {
            return true;
        }
        long now3 = java.lang.System.currentTimeMillis();
        int[] allUsers = instantAppRegistry.mUserManager.getUserIds();
        final android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = computer.getPackageStates();
        int packageStateCount = packageStates.size();
        java.util.List<java.lang.String> packagesToDelete = null;
        int i3 = 0;
        while (i3 < packageStateCount) {
            com.android.server.pm.pkg.PackageStateInternal ps = packageStates.valueAt(i3);
            com.android.server.pm.pkg.AndroidPackage pkg = ps == null ? null : ps.getPkg();
            if (pkg == null) {
                storage = storage2;
            } else if (now3 - ps.getTransientState().getLatestPackageUseTimeInMills() < maxInstalledCacheDuration) {
                storage = storage2;
            } else {
                boolean installedOnlyAsInstantApp = false;
                int length = allUsers.length;
                int i4 = 0;
                while (true) {
                    if (i4 >= length) {
                        storage = storage2;
                        break;
                    }
                    storage = storage2;
                    com.android.server.pm.pkg.PackageUserStateInternal userState = ps.getUserStateOrDefault(allUsers[i4]);
                    if (userState.isInstalled()) {
                        if (userState.isInstantApp()) {
                            installedOnlyAsInstantApp = true;
                        } else {
                            installedOnlyAsInstantApp = false;
                            break;
                        }
                    }
                    i4++;
                    storage2 = storage;
                }
                if (installedOnlyAsInstantApp) {
                    if (packagesToDelete == null) {
                        packagesToDelete = new java.util.ArrayList<>();
                    }
                    packagesToDelete.add(pkg.getPackageName());
                }
            }
            i3++;
            storage2 = storage;
        }
        if (packagesToDelete != null) {
            packagesToDelete.sort(new java.util.Comparator() { // from class: com.android.server.pm.InstantAppRegistry$$ExternalSyntheticLambda2
                @Override // java.util.Comparator
                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.pm.InstantAppRegistry.lambda$pruneInstantApps$2(packageStates, (java.lang.String) obj, (java.lang.String) obj2);
                }
            });
        }
        if (packagesToDelete != null) {
            int packageCount = packagesToDelete.size();
            for (int i5 = 0; i5 < packageCount; i5++) {
                java.lang.String packageToDelete = packagesToDelete.get(i5);
                if (instantAppRegistry.mDeletePackageHelper.deletePackageX(packageToDelete, -1L, 0, 2, true) == 1 && file.getUsableSpace() >= neededSpace) {
                    return true;
                }
            }
        }
        synchronized (instantAppRegistry.mLock) {
            try {
                int[] userIds = instantAppRegistry.mUserManager.getUserIds();
                int length2 = userIds.length;
                int i6 = 0;
                while (i6 < length2) {
                    int userId = userIds[i6];
                    instantAppRegistry.removeUninstalledInstantAppStateLPw(new java.util.function.Predicate() { // from class: com.android.server.pm.InstantAppRegistry$$ExternalSyntheticLambda3
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.pm.InstantAppRegistry.lambda$pruneInstantApps$3(maxUninstalledCacheDuration, (com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState) obj);
                        }
                    }, userId);
                    java.io.File instantAppsDir = getInstantApplicationsDir(userId);
                    if (!instantAppsDir.exists()) {
                        iArr = userIds;
                        i = length2;
                        now = now3;
                    } else {
                        iArr = userIds;
                        java.io.File[] files2 = instantAppsDir.listFiles();
                        if (files2 == null) {
                            i = length2;
                            now = now3;
                        } else {
                            int length3 = files2.length;
                            i = length2;
                            int i7 = 0;
                            while (i7 < length3) {
                                java.io.File instantDir = files2[i7];
                                if (!instantDir.isDirectory()) {
                                    files = files2;
                                    i2 = length3;
                                    now2 = now3;
                                } else {
                                    files = files2;
                                    i2 = length3;
                                    now2 = now3;
                                    java.io.File metadataFile = new java.io.File(instantDir, INSTANT_APP_METADATA_FILE);
                                    if (metadataFile.exists()) {
                                        long elapsedCachingMillis = java.lang.System.currentTimeMillis() - metadataFile.lastModified();
                                        if (elapsedCachingMillis > maxUninstalledCacheDuration) {
                                            deleteDir(instantDir);
                                            if (file.getUsableSpace() >= neededSpace) {
                                                return true;
                                            }
                                        }
                                    }
                                }
                                i7++;
                                files2 = files;
                                length3 = i2;
                                now3 = now2;
                            }
                            now = now3;
                        }
                    }
                    try {
                        i6++;
                        instantAppRegistry = this;
                        userIds = iArr;
                        length2 = i;
                        now3 = now;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                return false;
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    static /* synthetic */ int lambda$pruneInstantApps$2(android.util.ArrayMap packageStates, java.lang.String lhs, java.lang.String rhs) {
        com.android.server.pm.pkg.PackageStateInternal rhsPs;
        com.android.server.pm.pkg.PackageStateInternal lhsPkgState = (com.android.server.pm.pkg.PackageStateInternal) packageStates.get(lhs);
        com.android.server.pm.pkg.PackageStateInternal rhsPkgState = (com.android.server.pm.pkg.PackageStateInternal) packageStates.get(rhs);
        com.android.server.pm.pkg.AndroidPackage lhsPkg = lhsPkgState == null ? null : lhsPkgState.getPkg();
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = rhsPkgState != null ? rhsPkgState.getPkg() : null;
        if (lhsPkg == null && pkg == null) {
            return 0;
        }
        if (lhsPkg == null) {
            return -1;
        }
        if (pkg == null) {
            return 1;
        }
        com.android.server.pm.pkg.PackageStateInternal lhsPs = (com.android.server.pm.pkg.PackageStateInternal) packageStates.get(lhsPkg.getPackageName());
        if (lhsPs == null || (rhsPs = (com.android.server.pm.pkg.PackageStateInternal) packageStates.get(pkg.getPackageName())) == null) {
            return 0;
        }
        if (lhsPs.getTransientState().getLatestPackageUseTimeInMills() > rhsPs.getTransientState().getLatestPackageUseTimeInMills()) {
            return 1;
        }
        if (lhsPs.getTransientState().getLatestPackageUseTimeInMills() < rhsPs.getTransientState().getLatestPackageUseTimeInMills() || com.android.server.pm.pkg.PackageStateUtils.getEarliestFirstInstallTime(lhsPs.getUserStates()) <= com.android.server.pm.pkg.PackageStateUtils.getEarliestFirstInstallTime(rhsPs.getUserStates())) {
            return -1;
        }
        return 1;
    }

    static /* synthetic */ boolean lambda$pruneInstantApps$3(long maxUninstalledCacheDuration, com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState state) {
        long elapsedCachingMillis = java.lang.System.currentTimeMillis() - state.mTimestamp;
        return elapsedCachingMillis > maxUninstalledCacheDuration;
    }

    private java.util.List<android.content.pm.InstantAppInfo> getInstalledInstantApplications(com.android.server.pm.Computer computer, int userId) {
        android.content.pm.InstantAppInfo info;
        java.util.List<android.content.pm.InstantAppInfo> result = null;
        android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> packageStates = computer.getPackageStates();
        int packageCount = packageStates.size();
        for (int i = 0; i < packageCount; i++) {
            com.android.server.pm.pkg.PackageStateInternal ps = packageStates.valueAt(i);
            if (ps != null && ps.getUserStateOrDefault(userId).isInstantApp() && (info = createInstantAppInfoForPackage(ps, userId, true)) != null) {
                if (result == null) {
                    result = new java.util.ArrayList<>();
                }
                result.add(info);
            }
        }
        return result;
    }

    private android.content.pm.InstantAppInfo createInstantAppInfoForPackage(com.android.server.pm.pkg.PackageStateInternal ps, int userId, boolean addApplicationInfo) {
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = ps.getPkg();
        if (pkg == null || !ps.getUserStateOrDefault(userId).isInstalled()) {
            return null;
        }
        java.lang.String[] requestedPermissions = new java.lang.String[pkg.getRequestedPermissions().size()];
        pkg.getRequestedPermissions().toArray(requestedPermissions);
        java.util.Set<java.lang.String> permissions = this.mPermissionManager.getGrantedPermissions(pkg.getPackageName(), userId);
        java.lang.String[] grantedPermissions = new java.lang.String[permissions.size()];
        permissions.toArray(grantedPermissions);
        android.content.pm.ApplicationInfo appInfo = com.android.server.pm.parsing.PackageInfoUtils.generateApplicationInfo(ps.getPkg(), 0L, ps.getUserStateOrDefault(userId), userId, ps);
        if (addApplicationInfo) {
            return new android.content.pm.InstantAppInfo(appInfo, requestedPermissions, grantedPermissions);
        }
        return new android.content.pm.InstantAppInfo(appInfo.packageName, appInfo.loadLabel(this.mContext.getPackageManager()), requestedPermissions, grantedPermissions);
    }

    private java.util.List<android.content.pm.InstantAppInfo> getUninstalledInstantApplications(com.android.server.pm.Computer computer, int userId) {
        java.util.List<com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState> uninstalledAppStates = getUninstalledInstantAppStates(userId);
        if (uninstalledAppStates == null || uninstalledAppStates.isEmpty()) {
            return null;
        }
        java.util.List<android.content.pm.InstantAppInfo> uninstalledApps = null;
        int stateCount = uninstalledAppStates.size();
        for (int i = 0; i < stateCount; i++) {
            com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState uninstalledAppState = uninstalledAppStates.get(i);
            if (uninstalledApps == null) {
                uninstalledApps = new java.util.ArrayList<>();
            }
            uninstalledApps.add(uninstalledAppState.mInstantAppInfo);
        }
        return uninstalledApps;
    }

    private void propagateInstantAppPermissionsIfNeeded(com.android.server.pm.pkg.AndroidPackage pkg, int userId) {
        android.content.pm.InstantAppInfo appInfo = peekOrParseUninstalledInstantAppInfo(pkg.getPackageName(), userId);
        if (appInfo == null || com.android.internal.util.ArrayUtils.isEmpty(appInfo.getGrantedPermissions())) {
            return;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            for (java.lang.String grantedPermission : appInfo.getGrantedPermissions()) {
                boolean propagatePermission = canPropagatePermission(grantedPermission);
                if (propagatePermission && pkg.getRequestedPermissions().contains(grantedPermission)) {
                    ((android.permission.PermissionManager) this.mContext.getSystemService(android.permission.PermissionManager.class)).grantRuntimePermission(pkg.getPackageName(), grantedPermission, android.os.UserHandle.of(userId));
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private boolean canPropagatePermission(java.lang.String permissionName) {
        android.permission.PermissionManager permissionManager = (android.permission.PermissionManager) this.mContext.getSystemService(android.permission.PermissionManager.class);
        android.content.pm.PermissionInfo permissionInfo = permissionManager.getPermissionInfo(permissionName, 0);
        if (permissionInfo != null) {
            return (permissionInfo.getProtection() == 1 || (permissionInfo.getProtectionFlags() & 32) != 0) && (permissionInfo.getProtectionFlags() & 4096) != 0;
        }
        return false;
    }

    private android.content.pm.InstantAppInfo peekOrParseUninstalledInstantAppInfo(java.lang.String packageName, int userId) {
        java.util.List<com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState> uninstalledAppStates;
        synchronized (this.mLock) {
            if (this.mUninstalledInstantApps != null && (uninstalledAppStates = this.mUninstalledInstantApps.get(userId)) != null) {
                int appCount = uninstalledAppStates.size();
                for (int i = 0; i < appCount; i++) {
                    com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState uninstalledAppState = uninstalledAppStates.get(i);
                    if (uninstalledAppState.mInstantAppInfo.getPackageName().equals(packageName)) {
                        return uninstalledAppState.mInstantAppInfo;
                    }
                }
            }
            java.io.File metadataFile = new java.io.File(getInstantApplicationDir(packageName, userId), INSTANT_APP_METADATA_FILE);
            com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState uninstalledAppState2 = parseMetadataFile(metadataFile);
            if (uninstalledAppState2 == null) {
                return null;
            }
            return uninstalledAppState2.mInstantAppInfo;
        }
    }

    private java.util.List<com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState> getUninstalledInstantAppStates(int userId) {
        java.util.List<com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState> uninstalledAppStates;
        java.io.File[] files;
        java.util.List<com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState> uninstalledAppStates2 = null;
        synchronized (this.mLock) {
            if (this.mUninstalledInstantApps != null && (uninstalledAppStates2 = this.mUninstalledInstantApps.get(userId)) != null) {
                return uninstalledAppStates2;
            }
            java.io.File instantAppsDir = getInstantApplicationsDir(userId);
            if (instantAppsDir.exists() && (files = instantAppsDir.listFiles()) != null) {
                for (java.io.File instantDir : files) {
                    if (instantDir.isDirectory()) {
                        java.io.File metadataFile = new java.io.File(instantDir, INSTANT_APP_METADATA_FILE);
                        com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState uninstalledAppState = parseMetadataFile(metadataFile);
                        if (uninstalledAppState != null) {
                            if (uninstalledAppStates2 == null) {
                                uninstalledAppStates2 = new java.util.ArrayList();
                            }
                            uninstalledAppStates2.add(uninstalledAppState);
                        }
                    }
                }
                uninstalledAppStates = uninstalledAppStates2;
            } else {
                uninstalledAppStates = uninstalledAppStates2;
            }
            synchronized (this.mLock) {
                this.mUninstalledInstantApps.put(userId, uninstalledAppStates);
            }
            return uninstalledAppStates;
        }
    }

    private static com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState parseMetadataFile(java.io.File metadataFile) {
        if (!metadataFile.exists()) {
            return null;
        }
        try {
            java.io.FileInputStream in = new android.util.AtomicFile(metadataFile).openRead();
            java.io.File instantDir = metadataFile.getParentFile();
            long timestamp = metadataFile.lastModified();
            java.lang.String packageName = instantDir.getName();
            try {
                try {
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                    return new com.android.server.pm.InstantAppRegistry.UninstalledInstantAppState(parseMetadata(parser, packageName), timestamp);
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                    throw new java.lang.IllegalStateException("Failed parsing instant metadata file: " + metadataFile, e);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(in);
            }
        } catch (java.io.FileNotFoundException e2) {
            android.util.Slog.i(LOG_TAG, "No instant metadata file");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.io.File computeInstantCookieFile(java.lang.String packageName, java.lang.String sha256Digest, int userId) {
        java.io.File appDir = getInstantApplicationDir(packageName, userId);
        java.lang.String cookieFile = INSTANT_APP_COOKIE_FILE_PREFIX + sha256Digest + INSTANT_APP_COOKIE_FILE_SIFFIX;
        return new java.io.File(appDir, cookieFile);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.io.File peekInstantCookieFile(java.lang.String packageName, int userId) {
        java.io.File[] files;
        java.io.File appDir = getInstantApplicationDir(packageName, userId);
        if (!appDir.exists() || (files = appDir.listFiles()) == null) {
            return null;
        }
        for (java.io.File file : files) {
            if (!file.isDirectory() && file.getName().startsWith(INSTANT_APP_COOKIE_FILE_PREFIX) && file.getName().endsWith(INSTANT_APP_COOKIE_FILE_SIFFIX)) {
                return file;
            }
        }
        return null;
    }

    private static android.content.pm.InstantAppInfo parseMetadata(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String packageName) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if ("package".equals(parser.getName())) {
                return parsePackage(parser, packageName);
            }
        }
        return null;
    }

    private static android.content.pm.InstantAppInfo parsePackage(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String packageName) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String label = parser.getAttributeValue((java.lang.String) null, ATTR_LABEL);
        java.util.List<java.lang.String> outRequestedPermissions = new java.util.ArrayList<>();
        java.util.List<java.lang.String> outGrantedPermissions = new java.util.ArrayList<>();
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (TAG_PERMISSIONS.equals(parser.getName())) {
                parsePermissions(parser, outRequestedPermissions, outGrantedPermissions);
            }
        }
        java.lang.String[] requestedPermissions = new java.lang.String[outRequestedPermissions.size()];
        outRequestedPermissions.toArray(requestedPermissions);
        java.lang.String[] grantedPermissions = new java.lang.String[outGrantedPermissions.size()];
        outGrantedPermissions.toArray(grantedPermissions);
        return new android.content.pm.InstantAppInfo(packageName, label, requestedPermissions, grantedPermissions);
    }

    private static void parsePermissions(com.android.modules.utils.TypedXmlPullParser parser, java.util.List<java.lang.String> outRequestedPermissions, java.util.List<java.lang.String> outGrantedPermissions) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if ("permission".equals(parser.getName())) {
                java.lang.String permission = com.android.internal.util.XmlUtils.readStringAttribute(parser, "name");
                outRequestedPermissions.add(permission);
                if (parser.getAttributeBoolean((java.lang.String) null, ATTR_GRANTED, false)) {
                    outGrantedPermissions.add(permission);
                }
            }
        }
    }

    private void writeUninstalledInstantAppMetadata(android.content.pm.InstantAppInfo instantApp, int userId) {
        com.android.modules.utils.TypedXmlSerializer serializer;
        boolean z;
        java.io.File appDir = getInstantApplicationDir(instantApp.getPackageName(), userId);
        if (!appDir.exists() && !appDir.mkdirs()) {
            return;
        }
        java.io.File metadataFile = new java.io.File(appDir, INSTANT_APP_METADATA_FILE);
        android.util.AtomicFile destination = new android.util.AtomicFile(metadataFile);
        java.io.FileOutputStream out = null;
        try {
            out = destination.startWrite();
            serializer = android.util.Xml.resolveSerializer(out);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startDocument((java.lang.String) null, true);
            serializer.startTag((java.lang.String) null, "package");
            try {
            } catch (java.lang.Throwable th) {
                t = th;
                try {
                    android.util.Slog.wtf(LOG_TAG, "Failed to write instant state, restoring backup", t);
                    destination.failWrite(out);
                } finally {
                    libcore.io.IoUtils.closeQuietly(out);
                }
            }
        } catch (java.lang.Throwable th2) {
            t = th2;
        }
        try {
            serializer.attribute((java.lang.String) null, ATTR_LABEL, instantApp.loadLabel(this.mContext.getPackageManager()).toString());
            serializer.startTag((java.lang.String) null, TAG_PERMISSIONS);
            java.lang.String[] requestedPermissions = instantApp.getRequestedPermissions();
            int length = requestedPermissions.length;
            int i = 0;
            while (i < length) {
                java.lang.String permission = requestedPermissions[i];
                serializer.startTag((java.lang.String) null, "permission");
                java.io.File appDir2 = appDir;
                try {
                    serializer.attribute((java.lang.String) null, "name", permission);
                    if (com.android.internal.util.ArrayUtils.contains(instantApp.getGrantedPermissions(), permission)) {
                        z = true;
                        serializer.attributeBoolean((java.lang.String) null, ATTR_GRANTED, true);
                    } else {
                        z = true;
                    }
                    serializer.endTag((java.lang.String) null, "permission");
                    i++;
                    appDir = appDir2;
                } catch (java.lang.Throwable th3) {
                    t = th3;
                    android.util.Slog.wtf(LOG_TAG, "Failed to write instant state, restoring backup", t);
                    destination.failWrite(out);
                }
            }
            serializer.endTag((java.lang.String) null, TAG_PERMISSIONS);
            serializer.endTag((java.lang.String) null, "package");
            serializer.endDocument();
            destination.finishWrite(out);
        } catch (java.lang.Throwable th4) {
            t = th4;
            android.util.Slog.wtf(LOG_TAG, "Failed to write instant state, restoring backup", t);
            destination.failWrite(out);
        }
    }

    private static java.io.File getInstantApplicationsDir(int userId) {
        return new java.io.File(android.os.Environment.getUserSystemDirectory(userId), INSTANT_APPS_FOLDER);
    }

    private static java.io.File getInstantApplicationDir(java.lang.String packageName, int userId) {
        return new java.io.File(getInstantApplicationsDir(userId), packageName);
    }

    private static void deleteDir(java.io.File dir) {
        java.io.File[] files = dir.listFiles();
        if (files != null) {
            for (java.io.File file : files) {
                deleteDir(file);
            }
        }
        dir.delete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class UninstalledInstantAppState {
        final android.content.pm.InstantAppInfo mInstantAppInfo;
        final long mTimestamp;

        public UninstalledInstantAppState(android.content.pm.InstantAppInfo instantApp, long timestamp) {
            this.mInstantAppInfo = instantApp;
            this.mTimestamp = timestamp;
        }
    }

    private final class CookiePersistence extends android.os.Handler {
        private static final long PERSIST_COOKIE_DELAY_MILLIS = 1000;
        private final android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.internal.os.SomeArgs>> mPendingPersistCookies;

        public CookiePersistence(android.os.Looper looper) {
            super(looper);
            this.mPendingPersistCookies = new android.util.SparseArray<>();
        }

        public void schedulePersistLPw(int userId, com.android.server.pm.pkg.AndroidPackage pkg, byte[] cookie) {
            java.io.File newCookieFile = com.android.server.pm.InstantAppRegistry.computeInstantCookieFile(pkg.getPackageName(), android.util.PackageUtils.computeSignaturesSha256Digest(pkg.getSigningDetails().getSignatures()), userId);
            if (!pkg.getSigningDetails().hasSignatures()) {
                android.util.Slog.wtf(com.android.server.pm.InstantAppRegistry.LOG_TAG, "Parsed Instant App contains no valid signatures!");
            }
            java.io.File oldCookieFile = com.android.server.pm.InstantAppRegistry.peekInstantCookieFile(pkg.getPackageName(), userId);
            if (oldCookieFile != null && !newCookieFile.equals(oldCookieFile)) {
                oldCookieFile.delete();
            }
            cancelPendingPersistLPw(pkg, userId);
            addPendingPersistCookieLPw(userId, pkg, cookie, newCookieFile);
            sendMessageDelayed(obtainMessage(userId, pkg), 1000L);
        }

        public byte[] getPendingPersistCookieLPr(com.android.server.pm.pkg.AndroidPackage pkg, int userId) {
            com.android.internal.os.SomeArgs state;
            android.util.ArrayMap<java.lang.String, com.android.internal.os.SomeArgs> pendingWorkForUser = this.mPendingPersistCookies.get(userId);
            if (pendingWorkForUser != null && (state = pendingWorkForUser.get(pkg.getPackageName())) != null) {
                return (byte[]) state.arg1;
            }
            return null;
        }

        public void cancelPendingPersistLPw(com.android.server.pm.pkg.AndroidPackage pkg, int userId) {
            removeMessages(userId, pkg);
            com.android.internal.os.SomeArgs state = removePendingPersistCookieLPr(pkg, userId);
            if (state != null) {
                state.recycle();
            }
        }

        private void addPendingPersistCookieLPw(int userId, com.android.server.pm.pkg.AndroidPackage pkg, byte[] cookie, java.io.File cookieFile) {
            android.util.ArrayMap<java.lang.String, com.android.internal.os.SomeArgs> pendingWorkForUser = this.mPendingPersistCookies.get(userId);
            if (pendingWorkForUser == null) {
                pendingWorkForUser = new android.util.ArrayMap<>();
                this.mPendingPersistCookies.put(userId, pendingWorkForUser);
            }
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = cookie;
            args.arg2 = cookieFile;
            pendingWorkForUser.put(pkg.getPackageName(), args);
        }

        private com.android.internal.os.SomeArgs removePendingPersistCookieLPr(com.android.server.pm.pkg.AndroidPackage pkg, int userId) {
            android.util.ArrayMap<java.lang.String, com.android.internal.os.SomeArgs> pendingWorkForUser = this.mPendingPersistCookies.get(userId);
            com.android.internal.os.SomeArgs state = null;
            if (pendingWorkForUser != null) {
                com.android.internal.os.SomeArgs state2 = pendingWorkForUser.remove(pkg.getPackageName());
                state = state2;
                if (pendingWorkForUser.isEmpty()) {
                    this.mPendingPersistCookies.remove(userId);
                }
            }
            return state;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message message) {
            int userId = message.what;
            com.android.server.pm.pkg.AndroidPackage pkg = (com.android.server.pm.pkg.AndroidPackage) message.obj;
            com.android.internal.os.SomeArgs state = removePendingPersistCookieLPr(pkg, userId);
            if (state == null) {
                return;
            }
            byte[] cookie = (byte[]) state.arg1;
            java.io.File cookieFile = (java.io.File) state.arg2;
            state.recycle();
            com.android.server.pm.InstantAppRegistry.this.persistInstantApplicationCookie(cookie, pkg.getPackageName(), cookieFile, userId);
        }
    }
}
