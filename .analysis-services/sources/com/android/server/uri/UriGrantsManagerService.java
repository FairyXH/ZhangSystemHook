package com.android.server.uri;

/* JADX INFO: loaded from: classes3.dex */
public class UriGrantsManagerService extends android.app.IUriGrantsManager.Stub implements com.android.server.uri.UriMetricsHelper.PersistentUriGrantsProvider {
    private static final java.lang.String ATTR_CREATED_TIME = "createdTime";
    private static final java.lang.String ATTR_MODE_FLAGS = "modeFlags";
    private static final java.lang.String ATTR_PREFIX = "prefix";
    private static final java.lang.String ATTR_SOURCE_PKG = "sourcePkg";
    private static final java.lang.String ATTR_SOURCE_USER_ID = "sourceUserId";
    private static final java.lang.String ATTR_TARGET_PKG = "targetPkg";
    private static final java.lang.String ATTR_TARGET_USER_ID = "targetUserId";
    private static final java.lang.String ATTR_URI = "uri";
    private static final java.lang.String ATTR_USER_HANDLE = "userHandle";
    private static final boolean DEBUG = false;
    private static final boolean ENABLE_DYNAMIC_PERMISSIONS = true;
    private static final int MAX_PERSISTED_URI_GRANTS = 512;
    private static final java.lang.String TAG = "UriGrantsManagerService";
    private static final java.lang.String TAG_URI_GRANT = "uri-grant";
    private static final java.lang.String TAG_URI_GRANTS = "uri-grants";
    android.app.ActivityManagerInternal mAmInternal;
    private final android.util.AtomicFile mGrantFile;
    private final android.util.SparseArray<android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission>> mGrantedUriPermissions;
    private final com.android.server.uri.UriGrantsManagerService.H mH;
    private final java.lang.Object mLock;
    com.android.server.uri.UriMetricsHelper mMetricsHelper;
    android.content.pm.PackageManagerInternal mPmInternal;
    private com.android.server.uri.IUriGrantsManagerServiceExt mUriGrantsManagerServiceExt;

    private UriGrantsManagerService() {
        this(com.android.server.SystemServiceManager.ensureSystemDir(), TAG_URI_GRANTS);
    }

    private UriGrantsManagerService(java.io.File systemDir, java.lang.String commitTag) {
        this.mLock = new java.lang.Object();
        this.mUriGrantsManagerServiceExt = (com.android.server.uri.IUriGrantsManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.uri.IUriGrantsManagerServiceExt.class).base(this).create();
        this.mGrantedUriPermissions = new android.util.SparseArray<>();
        this.mH = new com.android.server.uri.UriGrantsManagerService.H(com.android.server.IoThread.get().getLooper());
        java.io.File file = new java.io.File(systemDir, "urigrants.xml");
        this.mGrantFile = commitTag != null ? new android.util.AtomicFile(file, commitTag) : new android.util.AtomicFile(file);
    }

    static com.android.server.uri.UriGrantsManagerService createForTest(java.io.File systemDir) {
        com.android.server.uri.UriGrantsManagerService service = new com.android.server.uri.UriGrantsManagerService(systemDir, null) { // from class: com.android.server.uri.UriGrantsManagerService.1
            @Override // com.android.server.uri.UriGrantsManagerService
            protected int checkUidPermission(java.lang.String permission, int uid) {
                return -1;
            }

            @Override // com.android.server.uri.UriGrantsManagerService
            protected int checkComponentPermission(java.lang.String permission, int uid, int owningUid, boolean exported) {
                return -1;
            }
        };
        service.mAmInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        service.mPmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        return service;
    }

    com.android.server.uri.UriGrantsManagerInternal getLocalService() {
        return new com.android.server.uri.UriGrantsManagerService.LocalService();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void start() {
        com.android.server.LocalServices.addService(com.android.server.uri.UriGrantsManagerInternal.class, new com.android.server.uri.UriGrantsManagerService.LocalService());
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private final android.content.Context mContext;
        private final com.android.server.uri.UriGrantsManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
            this.mContext = context;
            this.mService = new com.android.server.uri.UriGrantsManagerService();
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            publishBinderService("uri_grants", this.mService);
            this.mService.mMetricsHelper = new com.android.server.uri.UriMetricsHelper(this.mContext, this.mService);
            this.mService.start();
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 500) {
                this.mService.mAmInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
                this.mService.mPmInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
                this.mService.mMetricsHelper.registerPuller();
            }
        }
    }

    protected int checkUidPermission(java.lang.String permission, int uid) {
        try {
            return android.app.AppGlobals.getPackageManager().checkUidPermission(permission, uid);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    protected int checkComponentPermission(java.lang.String permission, int uid, int owningUid, boolean exported) {
        return android.app.ActivityManager.checkComponentPermission(permission, uid, owningUid, exported);
    }

    public void grantUriPermissionFromOwner(android.os.IBinder token, int fromUid, java.lang.String targetPkg, android.net.Uri uri, int modeFlags, int sourceUserId, int targetUserId) throws java.lang.Throwable {
        grantUriPermissionFromOwnerUnlocked(token, fromUid, targetPkg, uri, modeFlags, sourceUserId, targetUserId);
    }

    private void grantUriPermissionFromOwnerUnlocked(android.os.IBinder token, int fromUid, java.lang.String targetPkg, android.net.Uri uri, int modeFlags, int sourceUserId, int targetUserId) throws java.lang.Throwable {
        int targetUserId2 = this.mAmInternal.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), targetUserId, false, 2, "grantUriPermissionFromOwner", (java.lang.String) null);
        com.android.server.uri.UriPermissionOwner owner = com.android.server.uri.UriPermissionOwner.fromExternalToken(token);
        if (owner == null) {
            throw new java.lang.IllegalArgumentException("Unknown owner: " + token);
        }
        if (fromUid != android.os.Binder.getCallingUid() && android.os.Binder.getCallingUid() != android.os.Process.myUid()) {
            throw new java.lang.SecurityException("nice try");
        }
        if (targetPkg == null) {
            throw new java.lang.IllegalArgumentException("null target");
        }
        if (uri == null) {
            throw new java.lang.IllegalArgumentException("null uri");
        }
        grantUriPermissionUnlocked(fromUid, targetPkg, new com.android.server.uri.GrantUri(sourceUserId, uri, modeFlags), modeFlags, owner, targetUserId2);
    }

    public android.content.pm.ParceledListSlice<android.content.UriPermission> getUriPermissions(java.lang.String packageName, boolean incoming, boolean persistedOnly) {
        enforceNotIsolatedCaller("getUriPermissions");
        java.util.Objects.requireNonNull(packageName, com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        int packageUid = pm.getPackageUid(packageName, 786432L, callingUserId);
        if (packageUid != callingUid) {
            throw new java.lang.SecurityException("Package " + packageName + " does not belong to calling UID " + callingUid);
        }
        java.util.ArrayList<android.content.UriPermission> result = com.google.android.collect.Lists.newArrayList();
        synchronized (this.mLock) {
            if (incoming) {
                android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms = this.mGrantedUriPermissions.get(callingUid);
                if (perms == null) {
                    android.util.Slog.w(TAG, "No permission grants found for " + packageName);
                } else {
                    for (int j = 0; j < perms.size(); j++) {
                        com.android.server.uri.UriPermission perm = perms.valueAt(j);
                        if (packageName.equals(perm.targetPkg) && (!persistedOnly || perm.persistedModeFlags != 0)) {
                            result.add(perm.buildPersistedPublicApiObject());
                        }
                    }
                }
            } else {
                int size = this.mGrantedUriPermissions.size();
                for (int i = 0; i < size; i++) {
                    android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms2 = this.mGrantedUriPermissions.valueAt(i);
                    for (int j2 = 0; j2 < perms2.size(); j2++) {
                        com.android.server.uri.UriPermission perm2 = perms2.valueAt(j2);
                        if (packageName.equals(perm2.sourcePkg) && (!persistedOnly || perm2.persistedModeFlags != 0)) {
                            result.add(perm2.buildPersistedPublicApiObject());
                        }
                    }
                }
            }
        }
        return new android.content.pm.ParceledListSlice<>(result);
    }

    public android.content.pm.ParceledListSlice<android.app.GrantedUriPermission> getGrantedUriPermissions(java.lang.String packageName, int userId) {
        this.mAmInternal.enforceCallingPermission("android.permission.GET_APP_GRANTED_URI_PERMISSIONS", "getGrantedUriPermissions");
        java.util.List<android.app.GrantedUriPermission> result = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            int size = this.mGrantedUriPermissions.size();
            for (int i = 0; i < size; i++) {
                android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms = this.mGrantedUriPermissions.valueAt(i);
                for (int j = 0; j < perms.size(); j++) {
                    com.android.server.uri.UriPermission perm = perms.valueAt(j);
                    if ((packageName == null || packageName.equals(perm.targetPkg)) && perm.targetUserId == userId && perm.persistedModeFlags != 0) {
                        result.add(perm.buildGrantedUriPermission());
                    }
                }
            }
        }
        return new android.content.pm.ParceledListSlice<>(result);
    }

    public void takePersistableUriPermission(android.net.Uri uri, int modeFlags, java.lang.String toPackage, int userId) {
        int uid;
        if (toPackage != null) {
            this.mAmInternal.enforceCallingPermission("android.permission.FORCE_PERSISTABLE_URI_PERMISSIONS", "takePersistableUriPermission");
            uid = this.mPmInternal.getPackageUid(toPackage, 0L, userId);
        } else {
            enforceNotIsolatedCaller("takePersistableUriPermission");
            uid = android.os.Binder.getCallingUid();
        }
        com.android.internal.util.Preconditions.checkFlagsArgument(modeFlags, 3);
        synchronized (this.mLock) {
            boolean prefixValid = false;
            com.android.server.uri.UriPermission exactPerm = findUriPermissionLocked(uid, new com.android.server.uri.GrantUri(userId, uri, 0));
            com.android.server.uri.UriPermission prefixPerm = findUriPermissionLocked(uid, new com.android.server.uri.GrantUri(userId, uri, 128));
            boolean exactValid = exactPerm != null && (exactPerm.persistableModeFlags & modeFlags) == modeFlags;
            if (prefixPerm != null && (prefixPerm.persistableModeFlags & modeFlags) == modeFlags) {
                prefixValid = true;
            }
            if (!exactValid && !prefixValid) {
                throw new java.lang.SecurityException("No persistable permission grants found for UID " + uid + " and Uri " + uri.toSafeString());
            }
            boolean persistChanged = exactValid ? false | exactPerm.takePersistableModes(modeFlags) : false;
            if (prefixValid) {
                persistChanged |= prefixPerm.takePersistableModes(modeFlags);
            }
            if (persistChanged | maybePrunePersistedUriGrantsLocked(uid)) {
                schedulePersistUriGrants();
            }
        }
    }

    public void clearGrantedUriPermissions(java.lang.String packageName, int userId) {
        this.mAmInternal.enforceCallingPermission("android.permission.CLEAR_APP_GRANTED_URI_PERMISSIONS", "clearGrantedUriPermissions");
        synchronized (this.mLock) {
            removeUriPermissionsForPackageLocked(packageName, userId, true, true);
        }
    }

    public void releasePersistableUriPermission(android.net.Uri uri, int modeFlags, java.lang.String toPackage, int userId) {
        int uid;
        if (toPackage != null) {
            this.mAmInternal.enforceCallingPermission("android.permission.FORCE_PERSISTABLE_URI_PERMISSIONS", "releasePersistableUriPermission");
            uid = this.mPmInternal.getPackageUid(toPackage, 0L, userId);
        } else {
            enforceNotIsolatedCaller("releasePersistableUriPermission");
            uid = android.os.Binder.getCallingUid();
        }
        com.android.internal.util.Preconditions.checkFlagsArgument(modeFlags, 3);
        synchronized (this.mLock) {
            boolean persistChanged = false;
            com.android.server.uri.UriPermission exactPerm = findUriPermissionLocked(uid, new com.android.server.uri.GrantUri(userId, uri, 0));
            com.android.server.uri.UriPermission prefixPerm = findUriPermissionLocked(uid, new com.android.server.uri.GrantUri(userId, uri, 128));
            if (exactPerm == null && prefixPerm == null && toPackage == null) {
                throw new java.lang.SecurityException("No permission grants found for UID " + uid + " and Uri " + uri.toSafeString());
            }
            if (exactPerm != null) {
                persistChanged = false | exactPerm.releasePersistableModes(modeFlags);
                removeUriPermissionIfNeededLocked(exactPerm);
            }
            if (prefixPerm != null) {
                persistChanged |= prefixPerm.releasePersistableModes(modeFlags);
                removeUriPermissionIfNeededLocked(prefixPerm);
            }
            if (persistChanged) {
                schedulePersistUriGrants();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeUriPermissionsForPackageLocked(java.lang.String packageName, int userHandle, boolean persistable, boolean targetOnly) {
        if (userHandle == -1 && packageName == null) {
            throw new java.lang.IllegalArgumentException("Must narrow by either package or user");
        }
        boolean persistChanged = false;
        int N = this.mGrantedUriPermissions.size();
        int i = 0;
        while (i < N) {
            int targetUid = this.mGrantedUriPermissions.keyAt(i);
            android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms = this.mGrantedUriPermissions.valueAt(i);
            if (userHandle == -1 || userHandle == android.os.UserHandle.getUserId(targetUid)) {
                java.util.Iterator<com.android.server.uri.UriPermission> it = perms.values().iterator();
                while (it.hasNext()) {
                    com.android.server.uri.UriPermission perm = it.next();
                    if (packageName == null || ((!targetOnly && perm.sourcePkg.equals(packageName)) || perm.targetPkg.equals(packageName))) {
                        if (!"downloads".equals(perm.uri.uri.getAuthority()) || persistable) {
                            persistChanged |= perm.revokeModes(persistable ? -1 : -65, true);
                            if (perm.modeFlags == 0) {
                                it.remove();
                            }
                        }
                    }
                }
                if (perms.isEmpty()) {
                    this.mGrantedUriPermissions.remove(targetUid);
                    N--;
                    i--;
                }
            }
            i++;
        }
        if (persistChanged) {
            schedulePersistUriGrants();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkAuthorityGrantsLocked(int callingUid, android.content.pm.ProviderInfo cpi, int userId, boolean checkUser) {
        if (cpi != null && this.mUriGrantsManagerServiceExt.needChangeUid(this.mGrantedUriPermissions, cpi.authority, callingUid) && 999 == android.os.UserHandle.getUserId(callingUid)) {
            callingUid = android.os.UserHandle.getUid(0, android.os.UserHandle.getAppId(callingUid));
            userId = 0;
        }
        android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms = this.mGrantedUriPermissions.get(callingUid);
        if (perms != null) {
            for (int i = perms.size() - 1; i >= 0; i--) {
                com.android.server.uri.GrantUri grantUri = perms.keyAt(i);
                if ((grantUri.sourceUserId == userId || !checkUser) && matchesProvider(grantUri.uri, cpi)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesProvider(android.net.Uri uri, android.content.pm.ProviderInfo cpi) {
        java.lang.String uriAuth = uri.getAuthority();
        java.lang.String cpiAuth = cpi.authority;
        if (cpiAuth.indexOf(59) == -1) {
            return cpiAuth.equals(uriAuth);
        }
        java.lang.String[] cpiAuths = cpiAuth.split(";");
        for (java.lang.String str : cpiAuths) {
            if (str.equals(uriAuth)) {
                return true;
            }
        }
        return false;
    }

    private boolean maybePrunePersistedUriGrantsLocked(int uid) {
        android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms = this.mGrantedUriPermissions.get(uid);
        if (perms == null || perms.size() < 512) {
            return false;
        }
        java.util.ArrayList<com.android.server.uri.UriPermission> persisted = com.google.android.collect.Lists.newArrayList();
        for (com.android.server.uri.UriPermission perm : perms.values()) {
            if (perm.persistedModeFlags != 0) {
                persisted.add(perm);
            }
        }
        int trimCount = persisted.size() - 512;
        if (trimCount <= 0) {
            return false;
        }
        java.util.Collections.sort(persisted, new com.android.server.uri.UriPermission.PersistedTimeComparator());
        for (int i = 0; i < trimCount; i++) {
            com.android.server.uri.UriPermission perm2 = persisted.get(i);
            perm2.releasePersistableModes(-1);
            removeUriPermissionIfNeededLocked(perm2);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.uri.NeededUriGrants checkGrantUriPermissionFromIntentUnlocked(int callingUid, java.lang.String targetPkg, android.content.Intent intent, int mode, com.android.server.uri.NeededUriGrants needed, int targetUserId, java.lang.Integer requireContentUriPermissionFromCaller) {
        int contentUserHint;
        int targetUid;
        int contentUserHint2;
        com.android.server.uri.NeededUriGrants needed2;
        com.android.server.uri.NeededUriGrants needed3;
        com.android.server.uri.NeededUriGrants needed4 = needed;
        if (targetPkg == null) {
            throw new java.lang.NullPointerException(ATTR_TARGET_PKG);
        }
        if (intent == null) {
            return null;
        }
        int contentUserHint3 = intent.getContentUserHint();
        if (contentUserHint3 != -2) {
            contentUserHint = contentUserHint3;
        } else {
            contentUserHint = android.os.UserHandle.getUserId(callingUid);
        }
        if (android.security.Flags.contentUriPermissionApis()) {
            enforceRequireContentUriPermissionFromCallerOnIntentExtraStream(intent, contentUserHint, mode, callingUid, requireContentUriPermissionFromCaller);
        }
        android.net.Uri data = intent.getData();
        android.content.ClipData clip = intent.getClipData();
        if (data == null && clip == null) {
            return null;
        }
        if (needed4 != null) {
            targetUid = needed4.targetUid;
        } else {
            int targetUid2 = this.mPmInternal.getPackageUid(targetPkg, 268435456L, targetUserId);
            if (targetUid2 < 0) {
                return null;
            }
            targetUid = targetUid2;
        }
        if (data != null) {
            int contentUserHint4 = this.mUriGrantsManagerServiceExt.changeUserIdInUriGrantsManagerService(contentUserHint, data);
            com.android.server.uri.GrantUri grantUri = com.android.server.uri.GrantUri.resolve(contentUserHint4, data, mode);
            if (android.security.Flags.contentUriPermissionApis()) {
                enforceRequireContentUriPermissionFromCaller(requireContentUriPermissionFromCaller, grantUri, callingUid);
            }
            targetUid = checkGrantUriPermissionUnlocked(callingUid, targetPkg, grantUri, mode, targetUid);
            if (targetUid <= 0) {
                contentUserHint = contentUserHint4;
            } else {
                if (needed4 != null) {
                    needed3 = needed4;
                } else {
                    needed3 = new com.android.server.uri.NeededUriGrants(targetPkg, targetUid, mode);
                }
                needed3.uris.add(grantUri);
                needed4 = needed3;
                contentUserHint = contentUserHint4;
            }
        }
        if (clip != null) {
            com.android.server.uri.NeededUriGrants needed5 = needed4;
            int targetUid3 = targetUid;
            int contentUserHint5 = contentUserHint;
            for (int contentUserHint6 = 0; contentUserHint6 < clip.getItemCount(); contentUserHint6++) {
                android.net.Uri uri = clip.getItemAt(contentUserHint6).getUri();
                if (uri != null) {
                    contentUserHint5 = this.mUriGrantsManagerServiceExt.changeUserIdInUriGrantsManagerService(contentUserHint5, uri);
                    com.android.server.uri.GrantUri grantUri2 = com.android.server.uri.GrantUri.resolve(contentUserHint5, uri, mode);
                    if (android.security.Flags.contentUriPermissionApis()) {
                        enforceRequireContentUriPermissionFromCaller(requireContentUriPermissionFromCaller, grantUri2, callingUid);
                    }
                    int targetUid4 = checkGrantUriPermissionUnlocked(callingUid, targetPkg, grantUri2, mode, targetUid3);
                    if (targetUid4 > 0) {
                        if (needed5 != null) {
                            needed2 = needed5;
                        } else {
                            needed2 = new com.android.server.uri.NeededUriGrants(targetPkg, targetUid4, mode);
                        }
                        needed2.uris.add(grantUri2);
                        needed5 = needed2;
                    }
                    targetUid3 = targetUid4;
                } else {
                    android.content.Intent clipIntent = clip.getItemAt(contentUserHint6).getIntent();
                    if (clipIntent != null) {
                        contentUserHint2 = contentUserHint5;
                        com.android.server.uri.NeededUriGrants newNeeded = checkGrantUriPermissionFromIntentUnlocked(callingUid, targetPkg, clipIntent, mode, needed5, targetUserId, requireContentUriPermissionFromCaller);
                        if (newNeeded != null) {
                            needed5 = newNeeded;
                            contentUserHint5 = contentUserHint2;
                        }
                    } else {
                        contentUserHint2 = contentUserHint5;
                    }
                    contentUserHint5 = contentUserHint2;
                }
            }
            return needed5;
        }
        return needed4;
    }

    private void enforceRequireContentUriPermissionFromCaller(java.lang.Integer requireContentUriPermissionFromCaller, com.android.server.uri.GrantUri grantUri, int uid) {
        if (requireContentUriPermissionFromCaller == null || requireContentUriPermissionFromCaller.intValue() == 0 || !com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(grantUri.uri.getScheme())) {
            return;
        }
        boolean hasPermission = false;
        boolean readMet = !android.content.pm.ActivityInfo.isRequiredContentUriPermissionRead(requireContentUriPermissionFromCaller.intValue()) || checkContentUriPermissionFullUnlocked(grantUri, uid, 1);
        boolean writeMet = !android.content.pm.ActivityInfo.isRequiredContentUriPermissionWrite(requireContentUriPermissionFromCaller.intValue()) || checkContentUriPermissionFullUnlocked(grantUri, uid, 2);
        if (requireContentUriPermissionFromCaller.intValue() != 3 ? !(!readMet || !writeMet) : !(!readMet && !writeMet)) {
            hasPermission = true;
        }
        if (!hasPermission) {
            throw new java.lang.SecurityException("You can't launch this activity because you don't have the required " + android.content.pm.ActivityInfo.requiredContentUriPermissionToShortString(requireContentUriPermissionFromCaller.intValue()) + " access to " + grantUri.uri);
        }
    }

    private void enforceRequireContentUriPermissionFromCallerOnIntentExtraStream(android.content.Intent intent, int contentUserHint, int mode, int callingUid, java.lang.Integer requireContentUriPermissionFromCaller) {
        try {
            android.net.Uri uri = (android.net.Uri) intent.getParcelableExtra("android.intent.extra.STREAM", android.net.Uri.class);
            if (uri != null) {
                com.android.server.uri.GrantUri grantUri = com.android.server.uri.GrantUri.resolve(contentUserHint, uri, mode);
                enforceRequireContentUriPermissionFromCaller(requireContentUriPermissionFromCaller, grantUri, callingUid);
            }
        } catch (android.os.BadParcelableException e) {
            android.util.Slog.w(TAG, "Failed to unparcel an URI in EXTRA_STREAM, skipping requireContentUriPermissionFromCaller: " + e);
        }
        try {
            java.util.ArrayList<android.net.Uri> uris = intent.getParcelableArrayListExtra("android.intent.extra.STREAM", android.net.Uri.class);
            if (uris != null) {
                for (int i = uris.size() - 1; i >= 0; i--) {
                    com.android.server.uri.GrantUri grantUri2 = com.android.server.uri.GrantUri.resolve(contentUserHint, uris.get(i), mode);
                    enforceRequireContentUriPermissionFromCaller(requireContentUriPermissionFromCaller, grantUri2, callingUid);
                }
            }
        } catch (android.os.BadParcelableException e2) {
            android.util.Slog.w(TAG, "Failed to unparcel an ArrayList of URIs in EXTRA_STREAM, skipping requireContentUriPermissionFromCaller: " + e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readGrantedUriPermissionsLocked() throws java.lang.Throwable {
        com.android.modules.utils.TypedXmlPullParser in;
        long now;
        java.io.FileInputStream fis;
        int sourceUserId;
        int targetUserId;
        com.android.server.uri.UriGrantsManagerService uriGrantsManagerService = this;
        long now2 = java.lang.System.currentTimeMillis();
        java.io.FileInputStream fis2 = null;
        try {
            try {
                fis2 = uriGrantsManagerService.mGrantFile.openRead();
                try {
                    com.android.modules.utils.TypedXmlPullParser in2 = android.util.Xml.resolvePullParser(fis2);
                    while (true) {
                        int type = in2.next();
                        if (type == 1) {
                            libcore.io.IoUtils.closeQuietly(fis2);
                            return;
                        }
                        java.lang.String tag = in2.getName();
                        if (type != 2) {
                            in = in2;
                            now = now2;
                            fis = fis2;
                        } else if (TAG_URI_GRANT.equals(tag)) {
                            int userHandle = in2.getAttributeInt((java.lang.String) null, ATTR_USER_HANDLE, -10000);
                            if (userHandle != -10000) {
                                sourceUserId = userHandle;
                                targetUserId = userHandle;
                            } else {
                                sourceUserId = in2.getAttributeInt((java.lang.String) null, ATTR_SOURCE_USER_ID);
                                targetUserId = in2.getAttributeInt((java.lang.String) null, ATTR_TARGET_USER_ID);
                            }
                            java.lang.String sourcePkg = in2.getAttributeValue((java.lang.String) null, ATTR_SOURCE_PKG);
                            java.lang.String targetPkg = in2.getAttributeValue((java.lang.String) null, ATTR_TARGET_PKG);
                            android.net.Uri uri = android.net.Uri.parse(in2.getAttributeValue((java.lang.String) null, ATTR_URI));
                            boolean prefix = in2.getAttributeBoolean((java.lang.String) null, ATTR_PREFIX, false);
                            int modeFlags = in2.getAttributeInt((java.lang.String) null, ATTR_MODE_FLAGS);
                            long createdTime = in2.getAttributeLong((java.lang.String) null, ATTR_CREATED_TIME, now2);
                            in = in2;
                            now = now2;
                            try {
                                android.content.pm.ProviderInfo pi = uriGrantsManagerService.getProviderInfo(uri.getAuthority(), sourceUserId, com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED, 1000);
                                if (pi == null || !sourcePkg.equals(pi.packageName)) {
                                    fis = fis2;
                                    android.util.Slog.w(TAG, "Persisted grant for " + uri + " had source " + sourcePkg + " but instead found " + pi);
                                } else {
                                    fis = fis2;
                                    try {
                                        int targetUid = uriGrantsManagerService.mPmInternal.getPackageUid(targetPkg, 8192L, targetUserId);
                                        if (targetUid != -1) {
                                            com.android.server.uri.GrantUri grantUri = new com.android.server.uri.GrantUri(sourceUserId, uri, prefix ? 128 : 0);
                                            com.android.server.uri.UriPermission perm = uriGrantsManagerService.findOrCreateUriPermissionLocked(sourcePkg, targetPkg, targetUid, grantUri);
                                            perm.initPersistedModes(modeFlags, createdTime);
                                            uriGrantsManagerService.mPmInternal.grantImplicitAccess(targetUserId, null, android.os.UserHandle.getAppId(targetUid), pi.applicationInfo.uid, false, true);
                                        }
                                    } catch (java.io.FileNotFoundException e) {
                                        fis2 = fis;
                                        libcore.io.IoUtils.closeQuietly(fis2);
                                        return;
                                    } catch (java.io.IOException e2) {
                                        e = e2;
                                        fis2 = fis;
                                        android.util.Slog.wtf(TAG, "Failed reading Uri grants", e);
                                        libcore.io.IoUtils.closeQuietly(fis2);
                                        return;
                                    } catch (org.xmlpull.v1.XmlPullParserException e3) {
                                        e = e3;
                                        fis2 = fis;
                                        android.util.Slog.wtf(TAG, "Failed reading Uri grants", e);
                                        libcore.io.IoUtils.closeQuietly(fis2);
                                        return;
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                        fis2 = fis;
                                        libcore.io.IoUtils.closeQuietly(fis2);
                                        throw th;
                                    }
                                }
                            } catch (java.io.FileNotFoundException e4) {
                            } catch (java.io.IOException e5) {
                                e = e5;
                            } catch (org.xmlpull.v1.XmlPullParserException e6) {
                                e = e6;
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        } else {
                            in = in2;
                            now = now2;
                            fis = fis2;
                        }
                        uriGrantsManagerService = this;
                        in2 = in;
                        now2 = now;
                        fis2 = fis;
                    }
                } catch (java.io.FileNotFoundException e7) {
                } catch (java.io.IOException e8) {
                    e = e8;
                } catch (org.xmlpull.v1.XmlPullParserException e9) {
                    e = e9;
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        } catch (java.io.FileNotFoundException e10) {
        } catch (java.io.IOException e11) {
            e = e11;
        } catch (org.xmlpull.v1.XmlPullParserException e12) {
            e = e12;
        } catch (java.lang.Throwable th5) {
            th = th5;
        }
    }

    private com.android.server.uri.UriPermission findOrCreateUriPermissionLocked(java.lang.String sourcePkg, java.lang.String targetPkg, int targetUid, com.android.server.uri.GrantUri grantUri) {
        android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> targetUris = this.mGrantedUriPermissions.get(targetUid);
        int targetUid2 = this.mUriGrantsManagerServiceExt.changeTargetUid(targetUid, android.os.Binder.getCallingUid(), targetPkg, grantUri.uri.getAuthority());
        if (targetUris == null) {
            targetUris = com.google.android.collect.Maps.newArrayMap();
            this.mGrantedUriPermissions.put(targetUid2, targetUris);
        }
        com.android.server.uri.UriPermission perm = targetUris.get(grantUri);
        if (perm == null) {
            com.android.server.uri.UriPermission perm2 = new com.android.server.uri.UriPermission(sourcePkg, targetPkg, targetUid2, grantUri);
            targetUris.put(grantUri, perm2);
            return perm2;
        }
        return perm;
    }

    private void grantUriPermissionUnchecked(int targetUid, java.lang.String targetPkg, com.android.server.uri.GrantUri grantUri, int modeFlags, com.android.server.uri.UriPermissionOwner owner) throws java.lang.Throwable {
        if (!android.content.Intent.isAccessUriMode(modeFlags)) {
            return;
        }
        java.lang.String authority = grantUri.uri.getAuthority();
        android.content.pm.ProviderInfo pi = getProviderInfo(authority, grantUri.sourceUserId, 268435456, 1000);
        if (pi == null) {
            android.util.Slog.w(TAG, "No content provider found for grant: " + grantUri.toSafeString());
            return;
        }
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.uri.UriPermission perm = findOrCreateUriPermissionLocked(pi.packageName, targetPkg, targetUid, grantUri);
                    perm.grantModes(modeFlags, owner);
                    this.mPmInternal.grantImplicitAccess(android.os.UserHandle.getUserId(targetUid), null, android.os.UserHandle.getAppId(targetUid), pi.applicationInfo.uid, false, (modeFlags & 64) != 0);
                } catch (java.lang.Throwable th) {
                    th = th;
                    while (true) {
                        try {
                            throw th;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    }
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void grantUriPermissionUncheckedFromIntent(com.android.server.uri.NeededUriGrants needed, com.android.server.uri.UriPermissionOwner owner) throws java.lang.Throwable {
        if (needed == null) {
            return;
        }
        int N = needed.uris.size();
        for (int i = 0; i < N; i++) {
            grantUriPermissionUnchecked(needed.targetUid, needed.targetPkg, needed.uris.valueAt(i), needed.flags, owner);
        }
    }

    private void grantUriPermissionUnlocked(int callingUid, java.lang.String targetPkg, com.android.server.uri.GrantUri grantUri, int modeFlags, com.android.server.uri.UriPermissionOwner owner, int targetUserId) throws java.lang.Throwable {
        if (targetPkg == null) {
            throw new java.lang.NullPointerException(ATTR_TARGET_PKG);
        }
        int targetUid = checkGrantUriPermissionUnlocked(callingUid, targetPkg, grantUri, modeFlags, this.mPmInternal.getPackageUid(targetPkg, 268435456L, targetUserId));
        if (targetUid < 0) {
            return;
        }
        grantUriPermissionUnchecked(targetUid, targetPkg, grantUri, modeFlags, owner);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void revokeUriPermission(java.lang.String targetPackage, int callingUid, com.android.server.uri.GrantUri grantUri, int modeFlags) {
        java.lang.String authority = grantUri.uri.getAuthority();
        android.content.pm.ProviderInfo pi = getProviderInfo(authority, grantUri.sourceUserId, com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED, callingUid);
        if (pi == null) {
            android.util.Slog.w(TAG, "No content provider found for permission revoke: " + grantUri.toSafeString());
            return;
        }
        boolean callerHoldsPermissions = checkHoldingPermissionsUnlocked(pi, grantUri, callingUid, modeFlags);
        synchronized (this.mLock) {
            revokeUriPermissionLocked(targetPackage, callingUid, grantUri, modeFlags, callerHoldsPermissions);
        }
    }

    private void revokeUriPermissionLocked(java.lang.String targetPackage, int callingUid, com.android.server.uri.GrantUri grantUri, int modeFlags, boolean callerHoldsPermissions) {
        if (!callerHoldsPermissions) {
            android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms = this.mGrantedUriPermissions.get(callingUid);
            if (perms != null) {
                boolean persistChanged = false;
                for (int i = perms.size() - 1; i >= 0; i--) {
                    com.android.server.uri.UriPermission perm = perms.valueAt(i);
                    if ((targetPackage == null || targetPackage.equals(perm.targetPkg)) && perm.uri.sourceUserId == grantUri.sourceUserId && perm.uri.uri.isPathPrefixMatch(grantUri.uri)) {
                        persistChanged |= perm.revokeModes(modeFlags | 64, false);
                        if (perm.modeFlags == 0) {
                            perms.removeAt(i);
                        }
                    }
                }
                if (perms.isEmpty()) {
                    this.mGrantedUriPermissions.remove(callingUid);
                }
                if (persistChanged) {
                    schedulePersistUriGrants();
                    return;
                }
                return;
            }
            return;
        }
        boolean persistChanged2 = false;
        for (int i2 = this.mGrantedUriPermissions.size() - 1; i2 >= 0; i2--) {
            this.mGrantedUriPermissions.keyAt(i2);
            android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms2 = this.mGrantedUriPermissions.valueAt(i2);
            for (int j = perms2.size() - 1; j >= 0; j--) {
                com.android.server.uri.UriPermission perm2 = perms2.valueAt(j);
                if ((targetPackage == null || targetPackage.equals(perm2.targetPkg)) && perm2.uri.sourceUserId == grantUri.sourceUserId && perm2.uri.uri.isPathPrefixMatch(grantUri.uri)) {
                    persistChanged2 |= perm2.revokeModes(modeFlags | 64, targetPackage == null);
                    if (perm2.modeFlags == 0) {
                        perms2.removeAt(j);
                    }
                }
            }
            if (perms2.isEmpty()) {
                this.mGrantedUriPermissions.removeAt(i2);
            }
        }
        if (persistChanged2) {
            schedulePersistUriGrants();
        }
    }

    private boolean checkHoldingPermissionsUnlocked(android.content.pm.ProviderInfo pi, com.android.server.uri.GrantUri grantUri, int uid, int modeFlags) {
        if (android.os.UserHandle.getUserId(uid) != grantUri.sourceUserId && checkComponentPermission("android.permission.INTERACT_ACROSS_USERS", uid, -1, true) != 0) {
            return false;
        }
        return checkHoldingPermissionsInternalUnlocked(pi, grantUri, uid, modeFlags, true);
    }

    private boolean checkHoldingPermissionsInternalUnlocked(android.content.pm.ProviderInfo pi, com.android.server.uri.GrantUri grantUri, int uid, int modeFlags, boolean considerUidPermissions) {
        boolean forceMet;
        java.lang.String ppwperm;
        java.lang.String pprperm;
        if (java.lang.Thread.holdsLock(this.mLock)) {
            throw new java.lang.IllegalStateException("Must never hold local mLock");
        }
        if (pi.applicationInfo.uid == uid) {
            return true;
        }
        if (!pi.exported) {
            return false;
        }
        boolean readMet = (modeFlags & 1) == 0;
        boolean writeMet = (modeFlags & 2) == 0;
        if (!readMet && pi.readPermission != null && considerUidPermissions && checkUidPermission(pi.readPermission, uid) == 0) {
            readMet = true;
        }
        if (!writeMet && pi.writePermission != null && considerUidPermissions && checkUidPermission(pi.writePermission, uid) == 0) {
            writeMet = true;
        }
        boolean allowDefaultRead = pi.readPermission == null;
        boolean allowDefaultWrite = pi.writePermission == null;
        android.content.pm.PathPermission[] pps = pi.pathPermissions;
        if (pps != null) {
            java.lang.String path = grantUri.uri.getPath();
            int i = pps.length;
            while (i > 0 && (!readMet || !writeMet)) {
                i--;
                android.content.pm.PathPermission pp = pps[i];
                if (pp.match(path)) {
                    if (!readMet && (pprperm = pp.getReadPermission()) != null) {
                        if (considerUidPermissions && checkUidPermission(pprperm, uid) == 0) {
                            readMet = true;
                        } else {
                            allowDefaultRead = false;
                        }
                    }
                    if (!writeMet && (ppwperm = pp.getWritePermission()) != null) {
                        if (considerUidPermissions && checkUidPermission(ppwperm, uid) == 0) {
                            writeMet = true;
                        } else {
                            allowDefaultWrite = false;
                        }
                    }
                }
            }
        }
        if (allowDefaultRead) {
            readMet = true;
        }
        if (allowDefaultWrite) {
            writeMet = true;
        }
        boolean needSkipMultapp = this.mUriGrantsManagerServiceExt.skipMultiappHandleUri(android.os.UserHandle.getUserId(uid), grantUri.uri);
        if (pi.forceUriPermissions && !needSkipMultapp) {
            int providerUserId = android.os.UserHandle.getUserId(pi.applicationInfo.uid);
            int clientUserId = android.os.UserHandle.getUserId(uid);
            java.lang.String nfcUriAuthority = (grantUri == null || grantUri.uri == null) ? "" : grantUri.uri.getAuthority();
            if ((1027 == uid % 100000 && "media".equals(nfcUriAuthority)) || this.mUriGrantsManagerServiceExt.isGrantedSystemApp(this.mPmInternal.getNameForUid(uid))) {
                forceMet = true;
            } else {
                forceMet = providerUserId == clientUserId && this.mAmInternal.checkContentProviderUriPermission(grantUri.uri, providerUserId, uid, modeFlags) == 0;
            }
        } else {
            forceMet = true;
        }
        return readMet && writeMet && forceMet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeUriPermissionIfNeededLocked(com.android.server.uri.UriPermission perm) {
        android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms;
        if (perm.modeFlags != 0 || (perms = this.mGrantedUriPermissions.get(perm.targetUid)) == null) {
            return;
        }
        perms.remove(perm.uri);
        if (perms.isEmpty()) {
            this.mGrantedUriPermissions.remove(perm.targetUid);
        }
    }

    private com.android.server.uri.UriPermission findUriPermissionLocked(int targetUid, com.android.server.uri.GrantUri grantUri) {
        android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> targetUris = this.mGrantedUriPermissions.get(targetUid);
        if (targetUris != null) {
            return targetUris.get(grantUri);
        }
        return null;
    }

    private void schedulePersistUriGrants() {
        if (!this.mH.hasMessages(1)) {
            this.mH.sendMessageDelayed(this.mH.obtainMessage(1), 10000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceNotIsolatedCaller(java.lang.String caller) {
        if (android.os.UserHandle.isIsolated(android.os.Binder.getCallingUid())) {
            throw new java.lang.SecurityException("Isolated process not allowed to call " + caller);
        }
    }

    private android.content.pm.ProviderInfo getProviderInfo(java.lang.String authority, int userHandle, int pmFlags, int callingUid) {
        return this.mPmInternal.resolveContentProvider(authority, pmFlags | 2048, userHandle, callingUid);
    }

    private int checkGrantUriPermissionUnlocked(int callingUid, java.lang.String targetPkg, com.android.server.uri.GrantUri grantUri, int modeFlags, int lastTargetUid) {
        int targetUid;
        int i;
        boolean grantAllowed;
        boolean res;
        if (!isContentUriWithAccessModeFlags(grantUri, modeFlags, "grant URI permission")) {
            return -1;
        }
        int callingAppId = android.os.UserHandle.getAppId(callingUid);
        if ((callingAppId == 1000 || callingAppId == 0) && !"com.android.settings.files".equals(grantUri.uri.getAuthority()) && !"com.android.settings.module_licenses".equals(grantUri.uri.getAuthority())) {
            android.util.Slog.w(TAG, "For security reasons, the system cannot issue a Uri permission grant to " + grantUri + "; use startActivityAsCaller() instead");
            return -1;
        }
        java.lang.String authority = grantUri.uri.getAuthority();
        android.content.pm.ProviderInfo pi = getProviderInfo(authority, grantUri.sourceUserId, 268435456, callingUid);
        if (pi == null) {
            android.util.Slog.w(TAG, "No content provider found for permission check: " + grantUri.uri.toSafeString());
            return -1;
        }
        if (lastTargetUid < 0 && targetPkg != null) {
            int targetUid2 = this.mPmInternal.getPackageUid(targetPkg, 268435456L, android.os.UserHandle.getUserId(callingUid));
            if (targetUid2 < 0) {
                return -1;
            }
            targetUid = targetUid2;
        } else {
            targetUid = lastTargetUid;
        }
        int targetUid3 = 0;
        if (targetUid >= 0) {
            if (checkHoldingPermissionsUnlocked(pi, grantUri, targetUid, modeFlags)) {
                targetUid3 = 1;
            }
        } else {
            boolean allowed = pi.exported;
            if ((modeFlags & 1) != 0 && pi.readPermission != null) {
                allowed = false;
            }
            if ((modeFlags & 2) != 0 && pi.writePermission != null) {
                allowed = false;
            }
            if (pi.pathPermissions != null) {
                int N = pi.pathPermissions.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= N) {
                        break;
                    }
                    if (pi.pathPermissions[i2] == null || !pi.pathPermissions[i2].match(grantUri.uri.getPath())) {
                        i2++;
                    } else {
                        if ((modeFlags & 1) != 0 && pi.pathPermissions[i2].getReadPermission() != null) {
                            allowed = false;
                        }
                        if ((modeFlags & 2) != 0 && pi.pathPermissions[i2].getWritePermission() != null) {
                            allowed = false;
                        }
                    }
                }
            }
            if (allowed) {
                targetUid3 = 1;
            }
        }
        if (!pi.forceUriPermissions) {
            i = targetUid3;
        } else {
            i = 0;
        }
        boolean z = false;
        boolean basicGrant = (modeFlags & 192) == 0;
        if (basicGrant && i != 0) {
            this.mPmInternal.grantImplicitAccess(android.os.UserHandle.getUserId(targetUid), null, android.os.UserHandle.getAppId(targetUid), pi.applicationInfo.uid, false);
            return -1;
        }
        if (targetUid >= 0 && android.os.UserHandle.getUserId(targetUid) != grantUri.sourceUserId && checkHoldingPermissionsInternalUnlocked(pi, grantUri, callingUid, modeFlags, false)) {
            z = true;
        }
        boolean specialCrossUserGrant = z;
        boolean grantAllowed2 = pi.grantUriPermissions;
        if (com.android.internal.util.ArrayUtils.isEmpty(pi.uriPermissionPatterns)) {
            grantAllowed = grantAllowed2;
        } else {
            int N2 = pi.uriPermissionPatterns.length;
            int i3 = 0;
            while (true) {
                if (i3 >= N2) {
                    grantAllowed = false;
                    break;
                }
                if (pi.uriPermissionPatterns[i3] == null || !pi.uriPermissionPatterns[i3].match(grantUri.uri.getPath())) {
                    i3++;
                } else {
                    grantAllowed = true;
                    break;
                }
            }
        }
        if (!grantAllowed) {
            if (specialCrossUserGrant) {
                if (!basicGrant) {
                    throw new java.lang.SecurityException("Provider " + pi.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pi.name + " does not allow granting of advanced Uri permissions (uri " + grantUri + ")");
                }
            } else {
                throw new java.lang.SecurityException("Provider " + pi.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + pi.name + " does not allow granting of Uri permissions (uri " + grantUri + ")");
            }
        }
        if (!checkHoldingPermissionsUnlocked(pi, grantUri, callingUid, modeFlags)) {
            synchronized (this.mLock) {
                res = checkUriPermissionLocked(grantUri, callingUid, modeFlags);
            }
            if (!res) {
                if ("android.permission.MANAGE_DOCUMENTS".equals(pi.readPermission)) {
                    throw new java.lang.SecurityException("UID " + callingUid + " does not have permission to " + grantUri + "; you could obtain access using ACTION_OPEN_DOCUMENT or related APIs");
                }
                throw new java.lang.SecurityException("UID " + callingUid + " does not have permission to " + grantUri);
            }
        }
        return targetUid;
    }

    private boolean isContentUriWithAccessModeFlags(com.android.server.uri.GrantUri grantUri, int modeFlags, java.lang.String logAction) {
        return android.content.Intent.isAccessUriMode(modeFlags) && com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(grantUri.uri.getScheme());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkContentUriPermissionFullUnlocked(com.android.server.uri.GrantUri grantUri, int uid, int modeFlags) {
        boolean zCheckUriPermissionLocked;
        if (uid < 0) {
            throw new java.lang.IllegalArgumentException("Uid must be positive for the content URI permission check of " + grantUri.uri.toSafeString());
        }
        if (!isContentUriWithAccessModeFlags(grantUri, modeFlags, "check content URI permission")) {
            throw new java.lang.IllegalArgumentException("The URI must be a content URI and the mode flags must be at least read and/or write for the content URI permission check of " + grantUri.uri.toSafeString());
        }
        int appId = android.os.UserHandle.getAppId(uid);
        if (appId == 1000 || appId == 0) {
            return true;
        }
        java.lang.String authority = grantUri.uri.getAuthority();
        android.content.pm.ProviderInfo pi = getProviderInfo(authority, grantUri.sourceUserId, 268435456, uid);
        if (pi == null) {
            android.util.Slog.w(TAG, "No content provider found for content URI permission check: " + grantUri.uri.toSafeString());
            return false;
        }
        if (checkHoldingPermissionsUnlocked(pi, grantUri, uid, modeFlags)) {
            return true;
        }
        synchronized (this.mLock) {
            zCheckUriPermissionLocked = checkUriPermissionLocked(grantUri, uid, modeFlags);
        }
        return zCheckUriPermissionLocked;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkGrantUriPermissionUnlocked(int callingUid, java.lang.String targetPkg, android.net.Uri uri, int modeFlags, int userId) {
        return checkGrantUriPermissionUnlocked(callingUid, targetPkg, new com.android.server.uri.GrantUri(userId, uri, modeFlags), modeFlags, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkUriPermissionLocked(com.android.server.uri.GrantUri grantUri, int uid, int modeFlags) {
        boolean persistable = (modeFlags & 64) != 0;
        int minStrength = persistable ? 3 : 1;
        if (uid == 0) {
            return true;
        }
        if (grantUri != null && grantUri.uri != null && this.mUriGrantsManagerServiceExt.needChangeUid(this.mGrantedUriPermissions, grantUri.uri.getAuthority(), uid) && 999 == android.os.UserHandle.getUserId(uid)) {
            uid = android.os.UserHandle.getUid(0, android.os.UserHandle.getAppId(uid));
        }
        android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms = this.mGrantedUriPermissions.get(uid);
        if (perms == null) {
            return false;
        }
        com.android.server.uri.UriPermission exactPerm = perms.get(grantUri);
        if (exactPerm != null && exactPerm.getStrength(modeFlags) >= minStrength) {
            return true;
        }
        int N = perms.size();
        for (int i = 0; i < N; i++) {
            com.android.server.uri.UriPermission perm = perms.valueAt(i);
            if (perm.uri.prefix && grantUri.uri.isPathPrefixMatch(perm.uri.uri) && perm.getStrength(modeFlags) >= minStrength) {
                return true;
            }
        }
        return false;
    }

    public int checkGrantUriPermission_ignoreNonSystem(int callingUid, java.lang.String targetPkg, android.net.Uri uri, int modeFlags, int userId) {
        if (!isCallerIsSystemOrPrivileged()) {
            return -1;
        }
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            return checkGrantUriPermissionUnlocked(callingUid, targetPkg, uri, modeFlags, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    private boolean isCallerIsSystemOrPrivileged() {
        int uid = android.os.Binder.getCallingUid();
        return uid == 1000 || uid == 0 || checkComponentPermission("android.permission.INTERACT_ACROSS_USERS_FULL", uid, -1, true) == 0;
    }

    @Override // com.android.server.uri.UriMetricsHelper.PersistentUriGrantsProvider
    public java.util.ArrayList<com.android.server.uri.UriPermission> providePersistentUriGrants() {
        java.util.ArrayList<com.android.server.uri.UriPermission> result = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            int size = this.mGrantedUriPermissions.size();
            for (int i = 0; i < size; i++) {
                android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms = this.mGrantedUriPermissions.valueAt(i);
                int permissionsForPackageSize = perms.size();
                for (int j = 0; j < permissionsForPackageSize; j++) {
                    com.android.server.uri.UriPermission permission = perms.valueAt(j);
                    if (permission.persistedModeFlags != 0) {
                        result.add(permission);
                    }
                }
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeGrantedUriPermissions() {
        long startTime = android.os.SystemClock.uptimeMillis();
        int persistentUriPermissionsCount = 0;
        java.util.ArrayList<com.android.server.uri.UriPermission.Snapshot> persist = com.google.android.collect.Lists.newArrayList();
        synchronized (this.mLock) {
            int size = this.mGrantedUriPermissions.size();
            for (int i = 0; i < size; i++) {
                android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms = this.mGrantedUriPermissions.valueAt(i);
                int permissionsForPackageSize = perms.size();
                for (int j = 0; j < permissionsForPackageSize; j++) {
                    com.android.server.uri.UriPermission permission = perms.valueAt(j);
                    if (permission.persistedModeFlags != 0) {
                        persistentUriPermissionsCount++;
                        persist.add(permission.snapshot());
                    }
                }
            }
        }
        java.io.FileOutputStream fos = null;
        try {
            fos = this.mGrantFile.startWrite(startTime);
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fos);
            out.startDocument((java.lang.String) null, true);
            out.startTag((java.lang.String) null, TAG_URI_GRANTS);
            for (com.android.server.uri.UriPermission.Snapshot perm : persist) {
                out.startTag((java.lang.String) null, TAG_URI_GRANT);
                out.attributeInt((java.lang.String) null, ATTR_SOURCE_USER_ID, perm.uri.sourceUserId);
                out.attributeInt((java.lang.String) null, ATTR_TARGET_USER_ID, perm.targetUserId);
                out.attributeInterned((java.lang.String) null, ATTR_SOURCE_PKG, perm.sourcePkg);
                out.attributeInterned((java.lang.String) null, ATTR_TARGET_PKG, perm.targetPkg);
                out.attribute((java.lang.String) null, ATTR_URI, java.lang.String.valueOf(perm.uri.uri));
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_PREFIX, perm.uri.prefix);
                out.attributeInt((java.lang.String) null, ATTR_MODE_FLAGS, perm.persistedModeFlags);
                out.attributeLong((java.lang.String) null, ATTR_CREATED_TIME, perm.persistedCreateTime);
                out.endTag((java.lang.String) null, TAG_URI_GRANT);
            }
            out.endTag((java.lang.String) null, TAG_URI_GRANTS);
            out.endDocument();
            this.mGrantFile.finishWrite(fos);
        } catch (java.io.IOException e) {
            if (fos != null) {
                this.mGrantFile.failWrite(fos);
            }
        }
        this.mMetricsHelper.reportPersistentUriFlushed(persistentUriPermissionsCount);
    }

    final class H extends android.os.Handler {
        static final int PERSIST_URI_GRANTS_MSG = 1;

        public H(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.uri.UriGrantsManagerService.this.writeGrantedUriPermissions();
                    break;
            }
        }
    }

    private final class LocalService implements com.android.server.uri.UriGrantsManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void removeUriPermissionIfNeeded(com.android.server.uri.UriPermission perm) {
            synchronized (com.android.server.uri.UriGrantsManagerService.this.mLock) {
                com.android.server.uri.UriGrantsManagerService.this.removeUriPermissionIfNeededLocked(perm);
            }
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void revokeUriPermission(java.lang.String targetPackage, int callingUid, com.android.server.uri.GrantUri grantUri, int modeFlags) {
            com.android.server.uri.UriGrantsManagerService.this.revokeUriPermission(targetPackage, callingUid, grantUri, modeFlags);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public boolean checkUriPermission(com.android.server.uri.GrantUri grantUri, int uid, int modeFlags, boolean isFullAccessForContentUri) {
            boolean zCheckUriPermissionLocked;
            if (isFullAccessForContentUri) {
                return com.android.server.uri.UriGrantsManagerService.this.checkContentUriPermissionFullUnlocked(grantUri, uid, modeFlags);
            }
            synchronized (com.android.server.uri.UriGrantsManagerService.this.mLock) {
                zCheckUriPermissionLocked = com.android.server.uri.UriGrantsManagerService.this.checkUriPermissionLocked(grantUri, uid, modeFlags);
            }
            return zCheckUriPermissionLocked;
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public int checkGrantUriPermission(int callingUid, java.lang.String targetPkg, android.net.Uri uri, int modeFlags, int userId) {
            com.android.server.uri.UriGrantsManagerService.this.enforceNotIsolatedCaller("checkGrantUriPermission");
            return com.android.server.uri.UriGrantsManagerService.this.checkGrantUriPermissionUnlocked(callingUid, targetPkg, uri, modeFlags, userId);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public com.android.server.uri.NeededUriGrants checkGrantUriPermissionFromIntent(android.content.Intent intent, int callingUid, java.lang.String targetPkg, int targetUserId) {
            return internalCheckGrantUriPermissionFromIntent(intent, callingUid, targetPkg, targetUserId, null);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public com.android.server.uri.NeededUriGrants checkGrantUriPermissionFromIntent(android.content.Intent intent, int callingUid, java.lang.String targetPkg, int targetUserId, int requireContentUriPermissionFromCaller) {
            return internalCheckGrantUriPermissionFromIntent(intent, callingUid, targetPkg, targetUserId, java.lang.Integer.valueOf(requireContentUriPermissionFromCaller));
        }

        private com.android.server.uri.NeededUriGrants internalCheckGrantUriPermissionFromIntent(android.content.Intent intent, int callingUid, java.lang.String targetPkg, int targetUserId, java.lang.Integer requireContentUriPermissionFromCaller) {
            int mode = intent != null ? intent.getFlags() : 0;
            return com.android.server.uri.UriGrantsManagerService.this.checkGrantUriPermissionFromIntentUnlocked(callingUid, targetPkg, intent, mode, null, targetUserId, requireContentUriPermissionFromCaller);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void grantUriPermissionUncheckedFromIntent(com.android.server.uri.NeededUriGrants needed, com.android.server.uri.UriPermissionOwner owner) throws java.lang.Throwable {
            com.android.server.uri.UriGrantsManagerService.this.grantUriPermissionUncheckedFromIntent(needed, owner);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void onSystemReady() {
            synchronized (com.android.server.uri.UriGrantsManagerService.this.mLock) {
                com.android.server.uri.UriGrantsManagerService.this.readGrantedUriPermissionsLocked();
            }
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public android.os.IBinder newUriPermissionOwner(java.lang.String name) {
            com.android.server.uri.UriGrantsManagerService.this.enforceNotIsolatedCaller("newUriPermissionOwner");
            com.android.server.uri.UriPermissionOwner owner = new com.android.server.uri.UriPermissionOwner(this, name);
            return owner.getExternalToken();
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void removeUriPermissionsForPackage(java.lang.String packageName, int userHandle, boolean persistable, boolean targetOnly) {
            synchronized (com.android.server.uri.UriGrantsManagerService.this.mLock) {
                com.android.server.uri.UriGrantsManagerService.this.removeUriPermissionsForPackageLocked(packageName, userHandle, persistable, targetOnly);
            }
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void revokeUriPermissionFromOwner(android.os.IBinder token, android.net.Uri uri, int mode, int userId) {
            revokeUriPermissionFromOwner(token, uri, mode, userId, null, -1);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void revokeUriPermissionFromOwner(android.os.IBinder token, android.net.Uri uri, int mode, int userId, java.lang.String targetPkg, int targetUserId) {
            com.android.server.uri.GrantUri grantUri;
            com.android.server.uri.UriPermissionOwner owner = com.android.server.uri.UriPermissionOwner.fromExternalToken(token);
            if (owner == null) {
                throw new java.lang.IllegalArgumentException("Unknown owner: " + token);
            }
            if (uri == null) {
                grantUri = null;
            } else {
                try {
                    grantUri = new com.android.server.uri.GrantUri(userId, uri, mode);
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(com.android.server.uri.UriGrantsManagerService.TAG, "Failed remove uri permission", e);
                    return;
                }
            }
            owner.removeUriPermission(grantUri, mode, targetPkg, targetUserId);
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public boolean checkAuthorityGrants(int callingUid, android.content.pm.ProviderInfo cpi, int userId, boolean checkUser) {
            boolean zCheckAuthorityGrantsLocked;
            synchronized (com.android.server.uri.UriGrantsManagerService.this.mLock) {
                zCheckAuthorityGrantsLocked = com.android.server.uri.UriGrantsManagerService.this.checkAuthorityGrantsLocked(callingUid, cpi, userId, checkUser);
            }
            return zCheckAuthorityGrantsLocked;
        }

        @Override // com.android.server.uri.UriGrantsManagerInternal
        public void dump(java.io.PrintWriter pw, boolean dumpAll, java.lang.String dumpPackage) {
            synchronized (com.android.server.uri.UriGrantsManagerService.this.mLock) {
                boolean needSep = false;
                boolean printedAnything = false;
                if (com.android.server.uri.UriGrantsManagerService.this.mGrantedUriPermissions.size() > 0) {
                    boolean printed = false;
                    int dumpUid = -2;
                    if (dumpPackage != null) {
                        dumpUid = com.android.server.uri.UriGrantsManagerService.this.mPmInternal.getPackageUid(dumpPackage, 4194304L, 0);
                    }
                    for (int i = 0; i < com.android.server.uri.UriGrantsManagerService.this.mGrantedUriPermissions.size(); i++) {
                        int uid = com.android.server.uri.UriGrantsManagerService.this.mGrantedUriPermissions.keyAt(i);
                        if (dumpUid < -1 || android.os.UserHandle.getAppId(uid) == dumpUid) {
                            android.util.ArrayMap<com.android.server.uri.GrantUri, com.android.server.uri.UriPermission> perms = (android.util.ArrayMap) com.android.server.uri.UriGrantsManagerService.this.mGrantedUriPermissions.valueAt(i);
                            if (!printed) {
                                if (needSep) {
                                    pw.println();
                                }
                                needSep = true;
                                pw.println("  Granted Uri Permissions:");
                                printed = true;
                                printedAnything = true;
                            }
                            pw.print("  * UID ");
                            pw.print(uid);
                            pw.println(" holds:");
                            for (com.android.server.uri.UriPermission perm : perms.values()) {
                                pw.print("    ");
                                pw.println(perm);
                                if (dumpAll) {
                                    perm.dump(pw, "      ");
                                }
                            }
                        }
                    }
                }
                if (!printedAnything) {
                    pw.println("  (nothing)");
                }
            }
        }
    }
}
