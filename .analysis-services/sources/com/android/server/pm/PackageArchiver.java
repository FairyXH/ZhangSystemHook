package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageArchiver {
    private static final java.lang.String ACTION_UNARCHIVE_DIALOG = "com.android.intent.action.UNARCHIVE_DIALOG";
    private static final java.lang.String ACTION_UNARCHIVE_ERROR_DIALOG = "com.android.intent.action.UNARCHIVE_ERROR_DIALOG";
    private static final java.lang.String ARCHIVE_ICONS_DIR = "package_archiver";
    private static final boolean DEBUG = true;
    private static final int DEFAULT_UNARCHIVE_FOREGROUND_TIMEOUT_MS = 120000;
    private static final java.lang.String EXTRA_INSTALLER_PACKAGE_NAME = "com.android.content.pm.extra.UNARCHIVE_INSTALLER_PACKAGE_NAME";
    private static final java.lang.String EXTRA_INSTALLER_TITLE = "com.android.content.pm.extra.UNARCHIVE_INSTALLER_TITLE";
    private static final java.lang.String EXTRA_REQUIRED_BYTES = "com.android.content.pm.extra.UNARCHIVE_EXTRA_REQUIRED_BYTES";
    public static final java.lang.String EXTRA_UNARCHIVE_INTENT_SENDER = "android.content.pm.extra.UNARCHIVE_INTENT_SENDER";
    private static final android.graphics.PorterDuffColorFilter OPACITY_LAYER_FILTER = new android.graphics.PorterDuffColorFilter(android.graphics.Color.argb(0.5f, 0.0f, 0.0f, 0.0f), android.graphics.PorterDuff.Mode.SRC_ATOP);
    private static final java.lang.String TAG = "PackageArchiverService";
    private android.app.AppOpsManager mAppOpsManager;
    private final com.android.server.pm.AppStateHelper mAppStateHelper;
    private final android.content.Context mContext;
    private android.content.pm.LauncherApps mLauncherApps;
    private final java.util.Map<android.util.Pair<java.lang.Integer, java.lang.String>, android.content.IntentSender> mLauncherIntentSenders = new java.util.HashMap();
    private final com.android.server.pm.PackageManagerService mPm;
    private android.os.UserManager mUserManager;

    PackageArchiver(android.content.Context context, com.android.server.pm.PackageManagerService mPm) {
        this.mContext = context;
        this.mPm = mPm;
        this.mAppStateHelper = new com.android.server.pm.AppStateHelper(this.mContext);
    }

    public static boolean isArchived(com.android.server.pm.pkg.PackageUserState userState) {
        return (userState.getArchiveState() == null || userState.isInstalled()) ? false : true;
    }

    public static boolean isArchivingEnabled() {
        return com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.archiving();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    void requestArchive(java.lang.String packageName, java.lang.String callerPackageName, android.content.IntentSender intentSender, android.os.UserHandle userHandle) throws android.os.ParcelableException {
        requestArchive(packageName, callerPackageName, 0, intentSender, userHandle);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    void requestArchive(final java.lang.String packageName, final java.lang.String callerPackageName, int flags, final android.content.IntentSender intentSender, android.os.UserHandle userHandle) throws android.os.ParcelableException {
        int[] userIds;
        java.util.Objects.requireNonNull(packageName);
        java.util.Objects.requireNonNull(callerPackageName);
        java.util.Objects.requireNonNull(intentSender);
        java.util.Objects.requireNonNull(userHandle);
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("Requested archival of package %s for user %s.", new java.lang.Object[]{packageName, java.lang.Integer.valueOf(userHandle.getIdentifier())}));
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        final int binderUserId = userHandle.getIdentifier();
        final int binderUid = android.os.Binder.getCallingUid();
        final int binderPid = android.os.Binder.getCallingPid();
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(binderUid)) {
            verifyCaller(snapshot.getPackageUid(callerPackageName, 0L, binderUserId), binderUid);
        }
        boolean deleteAllUsers = (flags & 2) != 0;
        if (deleteAllUsers) {
            userIds = this.mPm.mInjector.getUserManagerInternal().getUserIds();
        } else {
            userIds = new int[]{binderUserId};
        }
        int[] users = userIds;
        int length = users.length;
        int i = 0;
        while (i < length) {
            int userId = users[i];
            snapshot.enforceCrossUserPermission(binderUid, userId, true, true, "archiveApp");
            i++;
            users = users;
            snapshot = snapshot;
        }
        int[] users2 = users;
        verifyUninstallPermissions();
        java.util.concurrent.CompletableFuture<java.lang.Void>[] archiveStateStored = new java.util.concurrent.CompletableFuture[users2.length];
        try {
            int size = users2.length;
            for (int i2 = 0; i2 < size; i2++) {
                try {
                    archiveStateStored[i2] = createAndStoreArchiveState(packageName, users2[i2]);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    e = e;
                    android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Failed to archive %s with message %s", new java.lang.Object[]{packageName, e.getMessage()}));
                    throw new android.os.ParcelableException(e);
                }
            }
            final int deleteFlags = (deleteAllUsers ? 2 : 0) | 17;
            java.util.concurrent.CompletableFuture.allOf(archiveStateStored).thenAccept(new java.util.function.Consumer() { // from class: com.android.server.pm.PackageArchiver$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$requestArchive$0(packageName, callerPackageName, deleteFlags, intentSender, binderUserId, binderUid, binderPid, (java.lang.Void) obj);
                }
            }).exceptionally(new java.util.function.Function() { // from class: com.android.server.pm.PackageArchiver$$ExternalSyntheticLambda7
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$requestArchive$1(packageName, intentSender, (java.lang.Throwable) obj);
                }
            });
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            e = e2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestArchive$0(java.lang.String packageName, java.lang.String callerPackageName, int deleteFlags, android.content.IntentSender intentSender, int binderUserId, int binderUid, int binderPid, java.lang.Void ignored) {
        this.mPm.mInstallerService.uninstall(new android.content.pm.VersionedPackage(packageName, -1), callerPackageName, deleteFlags, intentSender, binderUserId, binderUid, binderPid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Void lambda$requestArchive$1(java.lang.String packageName, android.content.IntentSender intentSender, java.lang.Throwable e) {
        android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Failed to archive %s with message %s", new java.lang.Object[]{packageName, e.getMessage()}));
        sendFailureStatus(intentSender, packageName, e.getMessage());
        return null;
    }

    public int requestUnarchiveOnActivityStart(android.content.Intent intent, java.lang.String callerPackageName, int userId, int callingUid) {
        boolean openAppDetailsIfOngoingUnarchival;
        final android.content.pm.PackageInstaller.SessionInfo activeUnarchivalSession;
        final java.lang.String packageName = getPackageNameFromIntent(intent);
        if (packageName == null) {
            android.util.Slog.e(TAG, "packageName cannot be null for unarchival!");
            return -92;
        }
        if (callerPackageName == null) {
            android.util.Slog.e(TAG, "callerPackageName cannot be null for unarchival!");
            return -92;
        }
        java.lang.String currentLauncherPackageName = getCurrentLauncherPackageName(getParentUserId(userId));
        if ((currentLauncherPackageName == null || !android.text.TextUtils.equals(callerPackageName, currentLauncherPackageName)) && callingUid != 2000) {
            android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("callerPackageName: %s does not qualify for unarchival of package: %s!", new java.lang.Object[]{callerPackageName, packageName}));
            return -94;
        }
        try {
            openAppDetailsIfOngoingUnarchival = getAppOpsManager().checkOp(146, callingUid, callerPackageName) == 0;
        } catch (java.lang.Throwable t) {
            android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Unexpected error occurred while unarchiving package %s: %s.", new java.lang.Object[]{packageName, t.getLocalizedMessage()}));
        }
        if (openAppDetailsIfOngoingUnarchival && (activeUnarchivalSession = getActiveUnarchivalSession(packageName, userId)) != null) {
            this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageArchiver$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$requestUnarchiveOnActivityStart$2(packageName, activeUnarchivalSession);
                }
            });
            return 102;
        }
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("Unarchival is starting for: %s", new java.lang.Object[]{packageName}));
        requestUnarchive(packageName, callerPackageName, getOrCreateLauncherListener(userId, packageName), android.os.UserHandle.of(userId), getAppOpsManager().checkOp(146, callingUid, callerPackageName) == 0);
        return 102;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestUnarchiveOnActivityStart$2(java.lang.String packageName, android.content.pm.PackageInstaller.SessionInfo activeUnarchivalSession) {
        android.util.Slog.i(TAG, "Opening app details page for ongoing unarchival of: " + packageName);
        getLauncherApps().startPackageInstallerSessionDetailsActivity(activeUnarchivalSession, null, null);
    }

    private int getParentUserId(int userId) {
        android.content.pm.UserInfo profileParent = getUserManager().getProfileParent(userId);
        return profileParent == null ? userId : profileParent.id;
    }

    public boolean isIntentResolvedToArchivedApp(android.content.Intent intent, int userId) {
        com.android.server.pm.pkg.PackageState packageState;
        java.lang.String packageName = getPackageNameFromIntent(intent);
        if (packageName == null || intent.getComponent() == null || (packageState = this.mPm.snapshotComputer().getPackageStateInternal(packageName)) == null) {
            return false;
        }
        com.android.server.pm.pkg.PackageUserState userState = packageState.getUserStateOrDefault(userId);
        if (!isArchived(userState)) {
            return false;
        }
        java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> archiveActivityInfoList = userState.getArchiveState().getActivityInfos();
        for (int i = 0; i < archiveActivityInfoList.size(); i++) {
            if (archiveActivityInfoList.get(i).getOriginalComponentName().equals(intent.getComponent())) {
                return true;
            }
        }
        android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Package: %s is archived but component to start main activity cannot be found!", new java.lang.Object[]{packageName}));
        return false;
    }

    void clearArchiveState(java.lang.String packageName, int userId) {
        com.android.server.pm.PackageSetting ps;
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                ps = this.mPm.mSettings.getPackageLPr(packageName);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        clearArchiveState(ps, userId);
    }

    void clearArchiveState(com.android.server.pm.PackageSetting ps, int userId) {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            if (ps != null) {
                try {
                    if (ps.getUserStateOrDefault(userId).getArchiveState() != null) {
                        android.util.Slog.e(TAG, "Clearing archive states for " + ps.getPackageName());
                        ps.setArchiveState(null, userId);
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        java.io.File iconsDir = getIconsDir(ps.getPackageName(), userId);
                        if (!iconsDir.exists()) {
                            android.util.Slog.e(TAG, "Icons are already deleted at " + iconsDir.getAbsolutePath());
                            return;
                        } else if (!android.os.FileUtils.deleteContentsAndDir(iconsDir)) {
                            android.util.Slog.e(TAG, "Failed to clean up archive files for " + ps.getPackageName());
                            return;
                        } else {
                            android.util.Slog.e(TAG, "Deleted icons at " + iconsDir.getAbsolutePath());
                            return;
                        }
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getCurrentLauncherPackageName(int userId) {
        android.content.ComponentName defaultLauncherComponent = this.mPm.snapshotComputer().getDefaultHomeActivity(userId);
        if (defaultLauncherComponent != null) {
            return defaultLauncherComponent.getPackageName();
        }
        return null;
    }

    private boolean isCallingPackageValid(java.lang.String callingPackage, int callingUid, int userId) {
        int packageUid = this.mPm.snapshotComputer().getPackageUid(callingPackage, 0L, userId);
        if (packageUid != callingUid) {
            android.util.Slog.w(TAG, android.text.TextUtils.formatSimple("Calling package: %s does not belong to uid: %d", new java.lang.Object[]{callingPackage, java.lang.Integer.valueOf(callingUid)}));
            return false;
        }
        return true;
    }

    private android.content.IntentSender getOrCreateLauncherListener(int userId, java.lang.String packageName) {
        android.util.Pair<java.lang.Integer, java.lang.String> key = android.util.Pair.create(java.lang.Integer.valueOf(userId), packageName);
        synchronized (this.mLauncherIntentSenders) {
            android.content.IntentSender intentSender = this.mLauncherIntentSenders.get(key);
            if (intentSender != null) {
                return intentSender;
            }
            android.content.IntentSender unarchiveIntentSender = new android.content.IntentSender(new com.android.server.pm.PackageArchiver.UnarchiveIntentSender());
            this.mLauncherIntentSenders.put(key, unarchiveIntentSender);
            return unarchiveIntentSender;
        }
    }

    private java.util.concurrent.CompletableFuture<java.lang.Void> createAndStoreArchiveState(final java.lang.String packageName, final int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        com.android.server.pm.pkg.PackageStateInternal ps = getPackageState(packageName, snapshot, android.os.Binder.getCallingUid(), userId);
        verifyNotSystemApp(ps.getFlags());
        verifyInstalled(ps, userId);
        final java.lang.String responsibleInstallerPackage = getResponsibleInstallerPackage(ps);
        final android.content.pm.ApplicationInfo installerInfo = verifyInstaller(snapshot, responsibleInstallerPackage, userId);
        verifyOptOutStatus(packageName, android.os.UserHandle.getUid(userId, android.os.UserHandle.getUid(userId, ps.getAppId())));
        final java.util.List<android.content.pm.LauncherActivityInfo> mainActivities = getLauncherActivityInfos(ps.getPackageName(), userId);
        final java.util.concurrent.CompletableFuture<java.lang.Void> archiveStateStored = new java.util.concurrent.CompletableFuture<>();
        this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageArchiver$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createAndStoreArchiveState$3(installerInfo, responsibleInstallerPackage, userId, packageName, mainActivities, archiveStateStored);
            }
        });
        return archiveStateStored;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createAndStoreArchiveState$3(android.content.pm.ApplicationInfo installerInfo, java.lang.String responsibleInstallerPackage, int userId, java.lang.String packageName, java.util.List mainActivities, java.util.concurrent.CompletableFuture archiveStateStored) {
        try {
            java.lang.String installerTitle = getResponsibleInstallerTitle(this.mContext, installerInfo, responsibleInstallerPackage, userId);
            com.android.server.pm.pkg.ArchiveState archiveState = createArchiveStateInternal(packageName, userId, mainActivities, installerTitle);
            storeArchiveState(packageName, archiveState, userId);
            archiveStateStored.complete(null);
        } catch (android.content.pm.PackageManager.NameNotFoundException | java.io.IOException e) {
            archiveStateStored.completeExceptionally(e);
        }
    }

    com.android.server.pm.pkg.ArchiveState createArchiveState(android.content.pm.ArchivedPackageParcel archivedPackage, int userId, java.lang.String installerPackage, java.lang.String responsibleInstallerTitle) {
        int i = userId;
        android.content.pm.ApplicationInfo installerInfo = this.mPm.snapshotComputer().getApplicationInfo(installerPackage, 0L, i);
        if (installerInfo == null) {
            android.util.Slog.e(TAG, "Couldn't find installer " + installerPackage);
            return null;
        }
        if (responsibleInstallerTitle == null) {
            android.util.Slog.e(TAG, "Couldn't get the title of the installer");
            return null;
        }
        int iconSize = ((android.app.ActivityManager) this.mContext.getSystemService(android.app.ActivityManager.class)).getLauncherLargeIconSize();
        android.content.pm.ArchivedPackageInfo info = new android.content.pm.ArchivedPackageInfo(archivedPackage);
        try {
            java.lang.String packageName = info.getPackageName();
            java.util.List<android.content.pm.ArchivedActivityInfo> mainActivities = info.getLauncherActivities();
            java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> archiveActivityInfos = new java.util.ArrayList<>(mainActivities.size());
            int i2 = 0;
            int size = mainActivities.size();
            while (i2 < size) {
                android.content.pm.ArchivedActivityInfo mainActivity = mainActivities.get(i2);
                java.nio.file.Path iconPath = storeAdaptiveDrawable(packageName, mainActivity.getIcon(), i, (i2 * 2) + 0, iconSize);
                java.nio.file.Path monochromePath = storeAdaptiveDrawable(packageName, mainActivity.getMonochromeIcon(), i, (i2 * 2) + 1, iconSize);
                java.lang.String packageName2 = packageName;
                java.lang.String packageName3 = mainActivity.getLabel().toString();
                com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo activityInfo = new com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo(packageName3, mainActivity.getComponentName(), iconPath, monochromePath);
                archiveActivityInfos.add(activityInfo);
                i2++;
                i = userId;
                packageName = packageName2;
            }
            return new com.android.server.pm.pkg.ArchiveState(archiveActivityInfos, responsibleInstallerTitle);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to create archive state", e);
            return null;
        }
    }

    com.android.server.pm.pkg.ArchiveState createArchiveStateInternal(java.lang.String packageName, int userId, java.util.List<android.content.pm.LauncherActivityInfo> mainActivities, java.lang.String installerTitle) throws java.io.IOException {
        int iconSize = ((android.app.ActivityManager) this.mContext.getSystemService(android.app.ActivityManager.class)).getLauncherLargeIconSize();
        java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> archiveActivityInfos = new java.util.ArrayList<>(mainActivities.size());
        int size = mainActivities.size();
        for (int i = 0; i < size; i++) {
            android.content.pm.LauncherActivityInfo mainActivity = mainActivities.get(i);
            java.nio.file.Path iconPath = storeIcon(packageName, mainActivity, userId, (i * 2) + 0, iconSize);
            com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo activityInfo = new com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo(mainActivity.getLabel().toString(), mainActivity.getComponentName(), iconPath, null);
            archiveActivityInfos.add(activityInfo);
        }
        return new com.android.server.pm.pkg.ArchiveState(archiveActivityInfos, installerTitle);
    }

    java.nio.file.Path storeIcon(java.lang.String packageName, android.content.pm.LauncherActivityInfo mainActivity, int userId, int index, int iconSize) throws java.io.IOException {
        int iconResourceId = mainActivity.getActivityInfo().getIconResource();
        if (iconResourceId == 0) {
            return null;
        }
        return storeDrawable(packageName, mainActivity.getIcon(0), userId, index, iconSize);
    }

    private static java.nio.file.Path storeDrawable(java.lang.String packageName, android.graphics.drawable.Drawable iconDrawable, int userId, int index, int iconSize) throws java.io.IOException {
        if (iconDrawable == null) {
            return null;
        }
        java.io.File iconsDir = createIconsDir(packageName, userId);
        java.io.File iconFile = new java.io.File(iconsDir, index + ".png");
        android.graphics.Bitmap icon = android.content.pm.ArchivedActivityInfo.drawableToBitmap(iconDrawable, iconSize);
        java.io.FileOutputStream out = new java.io.FileOutputStream(iconFile);
        try {
            if (!icon.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out)) {
                throw new java.io.IOException(android.text.TextUtils.formatSimple("Failure to store icon file %s", new java.lang.Object[]{iconFile.getAbsolutePath()}));
            }
            out.flush();
            out.close();
            if (iconFile.exists()) {
                android.util.Slog.i(TAG, "Stored icon at " + iconFile.getAbsolutePath());
            }
            return iconFile.toPath();
        } catch (java.lang.Throwable th) {
            try {
                out.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static class FixedSizeBitmapDrawable extends android.graphics.drawable.BitmapDrawable {
        FixedSizeBitmapDrawable(android.graphics.Bitmap bitmap) {
            super((android.content.res.Resources) null, bitmap);
        }

        @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return getBitmap().getWidth();
        }

        @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return getBitmap().getWidth();
        }
    }

    private static java.nio.file.Path storeAdaptiveDrawable(java.lang.String packageName, android.graphics.drawable.Drawable iconDrawable, int userId, int index, int iconSize) throws java.io.IOException {
        if (iconDrawable == null) {
            return null;
        }
        if (iconDrawable instanceof android.graphics.drawable.BitmapDrawable) {
            android.graphics.Bitmap icon = ((android.graphics.drawable.BitmapDrawable) iconDrawable).getBitmap();
            iconDrawable = new com.android.server.pm.PackageArchiver.FixedSizeBitmapDrawable(icon);
        }
        float inset = android.graphics.drawable.AdaptiveIconDrawable.getExtraInsetFraction();
        float inset2 = inset / ((2.0f * inset) + 1.0f);
        android.graphics.drawable.Drawable d = new android.graphics.drawable.AdaptiveIconDrawable(new android.graphics.drawable.ColorDrawable(android.hardware.audio.common.V2_0.AudioFormat.MAIN_MASK), new android.graphics.drawable.InsetDrawable(iconDrawable, inset2, inset2, inset2, inset2));
        return storeDrawable(packageName, d, userId, index, iconSize);
    }

    private android.content.pm.ApplicationInfo verifyInstaller(com.android.server.pm.Computer snapshot, java.lang.String installerPackageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        if (android.text.TextUtils.isEmpty(installerPackageName)) {
            throw new android.content.pm.PackageManager.NameNotFoundException("No installer found");
        }
        if (android.os.Binder.getCallingUid() != 2000 && !verifySupportsUnarchival(installerPackageName, userId)) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Installer does not support unarchival");
        }
        android.content.pm.ApplicationInfo appInfo = snapshot.getApplicationInfo(installerPackageName, 0L, userId);
        if (appInfo == null) {
            throw new android.content.pm.PackageManager.NameNotFoundException("Failed to obtain Installer info");
        }
        return appInfo;
    }

    public boolean verifySupportsUnarchival(java.lang.String installerPackage, final int userId) {
        if (android.text.TextUtils.isEmpty(installerPackage)) {
            return false;
        }
        final android.content.Intent intent = new android.content.Intent("android.intent.action.UNARCHIVE_PACKAGE").setPackage(installerPackage);
        android.content.pm.ParceledListSlice<android.content.pm.ResolveInfo> intentReceivers = (android.content.pm.ParceledListSlice) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.PackageArchiver$$ExternalSyntheticLambda1
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$verifySupportsUnarchival$4(intent, userId);
            }
        });
        return (intentReceivers == null || intentReceivers.getList().isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.pm.ParceledListSlice lambda$verifySupportsUnarchival$4(android.content.Intent intent, int userId) throws java.lang.Exception {
        return this.mPm.queryIntentReceivers(this.mPm.snapshotComputer(), intent, null, 0L, userId);
    }

    private void verifyNotSystemApp(int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        if ((flags & 1) != 0 || (flags & 128) != 0) {
            throw new android.content.pm.PackageManager.NameNotFoundException("System apps cannot be archived.");
        }
    }

    private void verifyInstalled(com.android.server.pm.pkg.PackageStateInternal ps, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        if (!ps.getUserStateOrDefault(userId).isInstalled()) {
            throw new android.content.pm.PackageManager.NameNotFoundException(android.text.TextUtils.formatSimple("%s is not installed.", new java.lang.Object[]{ps.getPackageName()}));
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public boolean isAppArchivable(java.lang.String packageName, android.os.UserHandle user) throws android.os.ParcelableException {
        java.util.Objects.requireNonNull(packageName);
        java.util.Objects.requireNonNull(user);
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        int userId = user.getIdentifier();
        int binderUid = android.os.Binder.getCallingUid();
        snapshot.enforceCrossUserPermission(binderUid, userId, true, true, "isAppArchivable");
        try {
            com.android.server.pm.pkg.PackageStateInternal ps = getPackageState(packageName, this.mPm.snapshotComputer(), android.os.Binder.getCallingUid(), userId);
            if ((ps.getFlags() & 1) != 0 || (ps.getFlags() & 128) != 0 || isAppOptedOutOfArchiving(packageName, android.os.UserHandle.getUid(userId, ps.getAppId()))) {
                return false;
            }
            try {
                verifyInstaller(snapshot, getResponsibleInstallerPackage(ps), userId);
                getLauncherActivityInfos(packageName, userId);
                return true;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                return false;
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            throw new android.os.ParcelableException(e2);
        }
    }

    private boolean isAppOptedOutOfArchiving(final java.lang.String packageName, final int uid) {
        return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.PackageArchiver$$ExternalSyntheticLambda0
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$isAppOptedOutOfArchiving$5(uid, packageName);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$isAppOptedOutOfArchiving$5(int uid, java.lang.String packageName) throws java.lang.Exception {
        return java.lang.Boolean.valueOf(getAppOpsManager().checkOpNoThrow(97, uid, packageName) == 1);
    }

    private void verifyOptOutStatus(java.lang.String packageName, int uid) throws android.content.pm.PackageManager.NameNotFoundException {
        if (isAppOptedOutOfArchiving(packageName, uid)) {
            throw new android.content.pm.PackageManager.NameNotFoundException(android.text.TextUtils.formatSimple("The app %s is opted out of archiving.", new java.lang.Object[]{packageName}));
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    void requestUnarchive(java.lang.String packageName, java.lang.String callerPackageName, android.content.IntentSender statusReceiver, android.os.UserHandle userHandle) throws android.os.ParcelableException {
        requestUnarchive(packageName, callerPackageName, statusReceiver, userHandle, false);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    private void requestUnarchive(final java.lang.String packageName, final java.lang.String callerPackageName, final android.content.IntentSender statusReceiver, final android.os.UserHandle userHandle, boolean showUnarchivalConfirmation) throws android.os.ParcelableException {
        java.util.Objects.requireNonNull(packageName);
        java.util.Objects.requireNonNull(callerPackageName);
        java.util.Objects.requireNonNull(statusReceiver);
        java.util.Objects.requireNonNull(userHandle);
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        final int userId = userHandle.getIdentifier();
        int binderUid = android.os.Binder.getCallingUid();
        if (!com.android.server.pm.PackageManagerServiceUtils.isSystemOrRootOrShell(binderUid)) {
            verifyCaller(snapshot.getPackageUid(callerPackageName, 0L, userId), binderUid);
        }
        snapshot.enforceCrossUserPermission(binderUid, userId, true, true, "unarchiveApp");
        try {
            com.android.server.pm.pkg.PackageStateInternal ps = getPackageState(packageName, snapshot, binderUid, userId);
            com.android.server.pm.pkg.PackageStateInternal callerPs = getPackageState(callerPackageName, snapshot, binderUid, userId);
            verifyArchived(ps, userId);
            final java.lang.String installerPackage = getResponsibleInstallerPackage(ps);
            if (installerPackage == null) {
                throw new android.os.ParcelableException(new android.content.pm.PackageManager.NameNotFoundException(android.text.TextUtils.formatSimple("No installer found to unarchive app %s.", new java.lang.Object[]{packageName})));
            }
            boolean hasInstallPackages = this.mContext.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") == 0;
            boolean hasRequestInstallPackages = callerPs.getAndroidPackage().getRequestedPermissions().contains("android.permission.REQUEST_INSTALL_PACKAGES");
            if (!hasInstallPackages && !hasRequestInstallPackages) {
                throw new java.lang.SecurityException("You need the com.android.permission.INSTALL_PACKAGES or com.android.permission.REQUEST_INSTALL_PACKAGES permission to request an unarchival.");
            }
            if (!hasInstallPackages || showUnarchivalConfirmation) {
                requestUnarchiveConfirmation(packageName, statusReceiver, userHandle);
                return;
            }
            try {
                final int draftSessionId = ((java.lang.Integer) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.PackageArchiver$$ExternalSyntheticLambda4
                    public final java.lang.Object getOrThrow() {
                        return this.f$0.lambda$requestUnarchive$6(packageName, installerPackage, callerPackageName, statusReceiver, userId);
                    }
                })).intValue();
                this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageArchiver$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$requestUnarchive$7(packageName, userHandle, installerPackage, draftSessionId);
                    }
                });
            } catch (java.lang.RuntimeException e) {
                if (e.getCause() instanceof java.io.IOException) {
                    throw android.util.ExceptionUtils.wrap((java.io.IOException) e.getCause());
                }
                throw e;
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            throw new android.os.ParcelableException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$requestUnarchive$6(java.lang.String packageName, java.lang.String installerPackage, java.lang.String callerPackageName, android.content.IntentSender statusReceiver, int userId) throws java.lang.Exception {
        return java.lang.Integer.valueOf(createDraftSession(packageName, installerPackage, callerPackageName, statusReceiver, userId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestUnarchive$7(java.lang.String packageName, android.os.UserHandle userHandle, java.lang.String installerPackage, int draftSessionId) {
        android.util.Slog.i(TAG, "Starting app unarchival for: " + packageName);
        unarchiveInternal(packageName, userHandle, installerPackage, draftSessionId);
    }

    private android.content.pm.PackageInstaller.SessionInfo getActiveUnarchivalSession(java.lang.String packageName, int userId) {
        java.util.List<android.content.pm.PackageInstaller.SessionInfo> activeSessions = this.mPm.mInstallerService.getAllSessions(userId).getList();
        for (int idx = 0; idx < activeSessions.size(); idx++) {
            android.content.pm.PackageInstaller.SessionInfo activeSession = activeSessions.get(idx);
            if (android.text.TextUtils.equals(activeSession.appPackageName, packageName) && activeSession.userId == userId && activeSession.active && activeSession.isUnarchival()) {
                return activeSession;
            }
        }
        return null;
    }

    private void requestUnarchiveConfirmation(java.lang.String packageName, android.content.IntentSender statusReceiver, android.os.UserHandle user) {
        android.content.Intent dialogIntent = new android.content.Intent(ACTION_UNARCHIVE_DIALOG);
        dialogIntent.putExtra(EXTRA_UNARCHIVE_INTENT_SENDER, statusReceiver);
        dialogIntent.putExtra("android.content.pm.extra.PACKAGE_NAME", packageName);
        android.content.Intent broadcastIntent = new android.content.Intent();
        broadcastIntent.putExtra("android.content.pm.extra.PACKAGE_NAME", packageName);
        broadcastIntent.putExtra("android.content.pm.extra.UNARCHIVE_STATUS", -1);
        broadcastIntent.putExtra("android.intent.extra.INTENT", dialogIntent);
        broadcastIntent.putExtra("android.intent.extra.USER", user);
        sendIntent(statusReceiver, packageName, "", broadcastIntent);
    }

    private void verifyUninstallPermissions() {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DELETE_PACKAGES") != 0 && this.mContext.checkCallingOrSelfPermission("android.permission.REQUEST_DELETE_PACKAGES") != 0) {
            throw new java.lang.SecurityException("You need the com.android.permission.DELETE_PACKAGES or com.android.permission.REQUEST_DELETE_PACKAGES permission to request an archival.");
        }
    }

    private int createDraftSession(java.lang.String packageName, java.lang.String installerPackage, java.lang.String callerPackageName, android.content.IntentSender statusReceiver, int userId) throws java.io.IOException {
        android.content.pm.PackageInstaller.SessionParams sessionParams = new android.content.pm.PackageInstaller.SessionParams(1);
        sessionParams.setAppPackageName(packageName);
        sessionParams.setAppLabel(this.mContext.getString(android.R.string.stk_cc_ss_to_dial_video));
        sessionParams.setAppIcon(getArchivedAppIcon(packageName, android.os.UserHandle.of(userId), callerPackageName));
        sessionParams.installFlags = 1610612736;
        int installerUid = this.mPm.snapshotComputer().getPackageUid(installerPackage, 0L, userId);
        int existingSessionId = this.mPm.mInstallerService.getExistingDraftSessionId(installerUid, sessionParams, userId);
        if (existingSessionId != -1) {
            attachListenerToSession(statusReceiver, existingSessionId, userId);
            return existingSessionId;
        }
        final int sessionId = this.mPm.mInstallerService.createSessionInternal(sessionParams, installerPackage, this.mContext.getAttributionTag(), installerUid, userId);
        attachListenerToSession(statusReceiver, sessionId, userId);
        this.mPm.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.pm.PackageArchiver$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createDraftSession$8(sessionId);
            }
        }, getUnarchiveForegroundTimeout());
        return sessionId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createDraftSession$8(int sessionId) {
        this.mPm.mInstallerService.cleanupDraftIfUnclaimed(sessionId);
    }

    private void attachListenerToSession(android.content.IntentSender statusReceiver, int existingSessionId, int userId) {
        com.android.server.pm.PackageInstallerSession session = this.mPm.mInstallerService.getSession(existingSessionId);
        int status = session.getUnarchivalStatus();
        if (status == 0) {
            notifyUnarchivalListener(0, session.getInstallerPackageName(), session.params.appPackageName, 0L, null, java.util.Set.of(statusReceiver), userId);
        } else {
            if (status != -1) {
                throw new java.lang.IllegalStateException(android.text.TextUtils.formatSimple("Session %s has unarchive status%s but is still active.", new java.lang.Object[]{java.lang.Integer.valueOf(session.sessionId), java.lang.Integer.valueOf(status)}));
            }
            session.registerUnarchivalListener(statusReceiver);
        }
    }

    public android.graphics.Bitmap getArchivedAppIcon(java.lang.String packageName, android.os.UserHandle user, java.lang.String callingPackageName) {
        java.util.Objects.requireNonNull(packageName);
        java.util.Objects.requireNonNull(user);
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        int callingUid = android.os.Binder.getCallingUid();
        int userId = user.getIdentifier();
        try {
            com.android.server.pm.pkg.PackageStateInternal ps = getPackageState(packageName, snapshot, callingUid, userId);
            com.android.server.pm.pkg.ArchiveState archiveState = getAnyArchiveState(ps, userId);
            if (archiveState == null || archiveState.getActivityInfos().size() == 0) {
                return null;
            }
            android.graphics.Bitmap icon = decodeIcon(archiveState.getActivityInfos().get(0));
            if (icon != null && getAppOpsManager().checkOp(145, callingUid, callingPackageName) == 0) {
                return includeCloudOverlay(icon);
            }
            return icon;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Package %s couldn't be found.", new java.lang.Object[]{packageName}), e);
            return null;
        }
    }

    private com.android.server.pm.pkg.ArchiveState getAnyArchiveState(com.android.server.pm.pkg.PackageStateInternal ps, int userId) {
        com.android.server.pm.pkg.PackageUserStateInternal userState = ps.getUserStateOrDefault(userId);
        if (isArchived(userState)) {
            return userState.getArchiveState();
        }
        for (int i = 0; i < ps.getUserStates().size(); i++) {
            com.android.server.pm.pkg.PackageUserStateInternal userState2 = ps.getUserStates().valueAt(i);
            if (isArchived(userState2)) {
                return userState2.getArchiveState();
            }
        }
        return null;
    }

    android.graphics.Bitmap decodeIcon(com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo activityInfo) {
        java.nio.file.Path iconBitmap = activityInfo.getIconBitmap();
        if (iconBitmap == null) {
            return null;
        }
        android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(iconBitmap.toString());
        if (bitmap == null) {
            android.util.Slog.e(TAG, "Archived icon cannot be decoded " + iconBitmap.toAbsolutePath());
            return null;
        }
        return bitmap;
    }

    android.graphics.Bitmap includeCloudOverlay(android.graphics.Bitmap bitmap) {
        android.graphics.drawable.Drawable cloudDrawable = this.mContext.getResources().getDrawable(android.R.drawable.ab_solid_light_holo, this.mContext.getTheme());
        if (cloudDrawable == null) {
            android.util.Slog.e(TAG, "Unable to locate cloud overlay for archived app!");
            return bitmap;
        }
        android.graphics.drawable.BitmapDrawable appIconDrawable = new android.graphics.drawable.BitmapDrawable(this.mContext.getResources(), bitmap);
        appIconDrawable.setColorFilter(OPACITY_LAYER_FILTER);
        appIconDrawable.setBounds(0, 0, cloudDrawable.getIntrinsicWidth(), cloudDrawable.getIntrinsicHeight());
        android.graphics.drawable.LayerDrawable layerDrawable = new android.graphics.drawable.LayerDrawable(new android.graphics.drawable.Drawable[]{appIconDrawable, cloudDrawable});
        int iconSize = ((android.app.ActivityManager) this.mContext.getSystemService(android.app.ActivityManager.class)).getLauncherLargeIconSize();
        android.graphics.Bitmap appIconWithCloudOverlay = android.content.pm.ArchivedActivityInfo.drawableToBitmap(layerDrawable, iconSize);
        if (bitmap != null) {
            bitmap.recycle();
        }
        return appIconWithCloudOverlay;
    }

    private void verifyArchived(com.android.server.pm.pkg.PackageStateInternal ps, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        com.android.server.pm.pkg.PackageUserStateInternal userState = ps.getUserStateOrDefault(userId);
        if (!isArchived(userState)) {
            throw new android.content.pm.PackageManager.NameNotFoundException(android.text.TextUtils.formatSimple("Package %s is not currently archived.", new java.lang.Object[]{ps.getPackageName()}));
        }
    }

    private void unarchiveInternal(java.lang.String packageName, android.os.UserHandle userHandle, java.lang.String installerPackage, int unarchiveId) {
        android.os.UserHandle userForUnarchival;
        int userId = userHandle.getIdentifier();
        android.content.Intent unarchiveIntent = new android.content.Intent("android.intent.action.UNARCHIVE_PACKAGE");
        unarchiveIntent.addFlags(268435456);
        unarchiveIntent.putExtra("android.content.pm.extra.UNARCHIVE_ID", unarchiveId);
        unarchiveIntent.putExtra("android.content.pm.extra.UNARCHIVE_PACKAGE_NAME", packageName);
        unarchiveIntent.putExtra("android.content.pm.extra.UNARCHIVE_ALL_USERS", userId == -1);
        unarchiveIntent.setPackage(installerPackage);
        if (userId == -1) {
            userForUnarchival = android.os.UserHandle.of(this.mPm.mUserManager.getCurrentUserId());
        } else {
            userForUnarchival = userHandle;
        }
        this.mContext.sendOrderedBroadcastAsUser(unarchiveIntent, userForUnarchival, null, -1, createUnarchiveOptions(), null, null, 0, null, null);
    }

    java.util.List<android.content.pm.LauncherActivityInfo> getLauncherActivityInfos(final java.lang.String packageName, final int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        java.util.List<android.content.pm.LauncherActivityInfo> mainActivities = (java.util.List) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.pm.PackageArchiver$$ExternalSyntheticLambda3
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$getLauncherActivityInfos$9(packageName, userId);
            }
        });
        if (mainActivities.isEmpty()) {
            throw new android.content.pm.PackageManager.NameNotFoundException(android.text.TextUtils.formatSimple("The app %s does not have a main activity.", new java.lang.Object[]{packageName}));
        }
        return mainActivities;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.List lambda$getLauncherActivityInfos$9(java.lang.String packageName, int userId) throws java.lang.Exception {
        return getLauncherApps().getActivityList(packageName, new android.os.UserHandle(userId));
    }

    private android.os.Bundle createUnarchiveOptions() {
        android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
        options.setTemporaryAppAllowlist(getUnarchiveForegroundTimeout(), 0, 328, "");
        return options.toBundle();
    }

    private static int getUnarchiveForegroundTimeout() {
        return 120000;
    }

    private static java.lang.String getResponsibleInstallerPackage(com.android.server.pm.InstallSource installSource) {
        if (android.text.TextUtils.isEmpty(installSource.mUpdateOwnerPackageName)) {
            return installSource.mInstallerPackageName;
        }
        return installSource.mUpdateOwnerPackageName;
    }

    private static java.lang.String getResponsibleInstallerTitle(android.content.Context context, android.content.pm.ApplicationInfo appInfo, java.lang.String responsibleInstallerPackage, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        android.content.Context userContext = context.createPackageContextAsUser(responsibleInstallerPackage, 0, new android.os.UserHandle(userId));
        return appInfo.loadLabel(userContext.getPackageManager()).toString();
    }

    static java.lang.String getResponsibleInstallerPackage(com.android.server.pm.pkg.PackageStateInternal ps) {
        return getResponsibleInstallerPackage(ps.getInstallSource());
    }

    static android.util.SparseArray<java.lang.String> getResponsibleInstallerTitles(android.content.Context context, com.android.server.pm.Computer snapshot, com.android.server.pm.InstallSource installSource, int requestUserId, int[] allUserIds) {
        java.lang.String responsibleInstallerPackage = getResponsibleInstallerPackage(installSource);
        android.util.SparseArray<java.lang.String> responsibleInstallerTitles = new android.util.SparseArray<>();
        try {
            if (requestUserId != -1) {
                android.content.pm.ApplicationInfo responsibleInstallerInfo = snapshot.getApplicationInfo(responsibleInstallerPackage, 0L, requestUserId);
                if (responsibleInstallerInfo == null) {
                    return null;
                }
                java.lang.String title = getResponsibleInstallerTitle(context, responsibleInstallerInfo, responsibleInstallerPackage, requestUserId);
                responsibleInstallerTitles.put(requestUserId, title);
            } else {
                for (int userId : allUserIds) {
                    android.content.pm.ApplicationInfo responsibleInstallerInfo2 = snapshot.getApplicationInfo(responsibleInstallerPackage, 0L, userId);
                    if (responsibleInstallerInfo2 != null) {
                        java.lang.String title2 = getResponsibleInstallerTitle(context, responsibleInstallerInfo2, responsibleInstallerPackage, userId);
                        responsibleInstallerTitles.put(userId, title2);
                    }
                }
            }
            return responsibleInstallerTitles;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    void notifyUnarchivalListener(int status, java.lang.String installerPackageName, java.lang.String appPackageName, long requiredStorageBytes, android.app.PendingIntent userActionIntent, java.util.Set<android.content.IntentSender> unarchiveIntentSenders, int userId) {
        android.content.Intent broadcastIntent = new android.content.Intent();
        broadcastIntent.putExtra("android.content.pm.extra.PACKAGE_NAME", appPackageName);
        broadcastIntent.putExtra("android.content.pm.extra.UNARCHIVE_STATUS", status);
        if (status != 0) {
            android.content.Intent dialogIntent = createErrorDialogIntent(status, installerPackageName, appPackageName, requiredStorageBytes, userActionIntent, userId);
            if (dialogIntent == null) {
                return;
            }
            broadcastIntent.putExtra("android.intent.extra.INTENT", dialogIntent);
            broadcastIntent.putExtra("android.intent.extra.USER", android.os.UserHandle.of(userId));
        }
        android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
        options.setPendingIntentBackgroundActivityStartMode(2);
        for (android.content.IntentSender intentSender : unarchiveIntentSenders) {
            try {
                try {
                    intentSender.sendIntent(this.mContext, 0, broadcastIntent, null, null, null, options.toBundle());
                    synchronized (this.mLauncherIntentSenders) {
                        this.mLauncherIntentSenders.remove(android.util.Pair.create(java.lang.Integer.valueOf(userId), appPackageName));
                    }
                } catch (android.content.IntentSender.SendIntentException e) {
                    android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Failed to send unarchive intent", new java.lang.Object[0]), e);
                    synchronized (this.mLauncherIntentSenders) {
                        this.mLauncherIntentSenders.remove(android.util.Pair.create(java.lang.Integer.valueOf(userId), appPackageName));
                    }
                }
            } catch (java.lang.Throwable th) {
                synchronized (this.mLauncherIntentSenders) {
                    this.mLauncherIntentSenders.remove(android.util.Pair.create(java.lang.Integer.valueOf(userId), appPackageName));
                    throw th;
                }
            }
        }
    }

    private android.content.Intent createErrorDialogIntent(int status, java.lang.String installerPackageName, java.lang.String appPackageName, long requiredStorageBytes, android.app.PendingIntent userActionIntent, int userId) {
        android.content.Intent dialogIntent = new android.content.Intent(ACTION_UNARCHIVE_ERROR_DIALOG);
        dialogIntent.putExtra("android.content.pm.extra.UNARCHIVE_STATUS", status);
        dialogIntent.putExtra("android.intent.extra.USER", android.os.UserHandle.of(userId));
        if (requiredStorageBytes > 0) {
            dialogIntent.putExtra(EXTRA_REQUIRED_BYTES, requiredStorageBytes);
        }
        if (userActionIntent != null) {
            dialogIntent.putExtra("android.intent.extra.INTENT", userActionIntent);
        }
        dialogIntent.putExtra(EXTRA_INSTALLER_PACKAGE_NAME, installerPackageName);
        java.lang.String installerTitle = getInstallerTitle(appPackageName, userId);
        if (installerTitle == null) {
            return null;
        }
        dialogIntent.putExtra(EXTRA_INSTALLER_TITLE, installerTitle);
        return dialogIntent;
    }

    private java.lang.String getInstallerTitle(java.lang.String appPackageName, int userId) {
        try {
            com.android.server.pm.pkg.PackageStateInternal packageState = getPackageState(appPackageName, this.mPm.snapshotComputer(), 1000, userId);
            com.android.server.pm.pkg.ArchiveState archiveState = packageState.getUserStateOrDefault(userId).getArchiveState();
            if (archiveState == null) {
                android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("notifyUnarchivalListener: App not archived %s.", new java.lang.Object[]{appPackageName}));
                return null;
            }
            return archiveState.getInstallerTitle();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("notifyUnarchivalListener: Couldn't fetch package state for %s.", new java.lang.Object[]{appPackageName}), e);
            return null;
        }
    }

    private static com.android.server.pm.pkg.PackageStateInternal getPackageState(java.lang.String packageName, com.android.server.pm.Computer snapshot, int callingUid, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateFiltered(packageName, callingUid, userId);
        if (ps == null) {
            throw new android.content.pm.PackageManager.NameNotFoundException(android.text.TextUtils.formatSimple("Package %s not found.", new java.lang.Object[]{packageName}));
        }
        return ps;
    }

    private android.content.pm.LauncherApps getLauncherApps() {
        if (this.mLauncherApps == null) {
            this.mLauncherApps = (android.content.pm.LauncherApps) this.mContext.getSystemService(android.content.pm.LauncherApps.class);
        }
        return this.mLauncherApps;
    }

    private android.app.AppOpsManager getAppOpsManager() {
        if (this.mAppOpsManager == null) {
            this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        }
        return this.mAppOpsManager;
    }

    private android.os.UserManager getUserManager() {
        if (this.mUserManager == null) {
            this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        }
        return this.mUserManager;
    }

    private void storeArchiveState(java.lang.String packageName, com.android.server.pm.pkg.ArchiveState archiveState, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                com.android.server.pm.PackageSetting packageSetting = getPackageSettingLocked(packageName, userId);
                packageSetting.modifyUserState(userId).setArchiveState(archiveState);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
    }

    private com.android.server.pm.PackageSetting getPackageSettingLocked(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        com.android.server.pm.PackageSetting ps = this.mPm.mSettings.getPackageLPr(packageName);
        if (ps == null || !ps.getUserStateOrDefault(userId).isInstalled()) {
            throw new android.content.pm.PackageManager.NameNotFoundException(android.text.TextUtils.formatSimple("Package %s not found.", new java.lang.Object[]{packageName}));
        }
        return ps;
    }

    private void sendFailureStatus(android.content.IntentSender statusReceiver, java.lang.String packageName, java.lang.String message) {
        android.util.Slog.d(TAG, android.text.TextUtils.formatSimple("Failed to archive %s with message %s", new java.lang.Object[]{packageName, message}));
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra("android.content.pm.extra.PACKAGE_NAME", packageName);
        intent.putExtra("android.content.pm.extra.STATUS", 1);
        intent.putExtra("android.content.pm.extra.STATUS_MESSAGE", message);
        sendIntent(statusReceiver, packageName, message, intent);
    }

    private void sendIntent(android.content.IntentSender statusReceiver, java.lang.String packageName, java.lang.String message, android.content.Intent intent) {
        try {
            android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
            options.setPendingIntentBackgroundActivityStartMode(2);
            statusReceiver.sendIntent(this.mContext, 0, intent, null, null, null, options.toBundle());
        } catch (android.content.IntentSender.SendIntentException e) {
            android.util.Slog.e(TAG, android.text.TextUtils.formatSimple("Failed to send status for %s with message %s", new java.lang.Object[]{packageName, message}), e);
        }
    }

    private static void verifyCaller(int providedUid, int binderUid) {
        if (providedUid != binderUid) {
            throw new java.lang.SecurityException(android.text.TextUtils.formatSimple("The UID %s of callerPackageName set by the caller doesn't match the caller's actual UID %s.", new java.lang.Object[]{java.lang.Integer.valueOf(providedUid), java.lang.Integer.valueOf(binderUid)}));
        }
    }

    private static java.io.File createIconsDir(java.lang.String packageName, int userId) throws java.io.IOException {
        java.io.File iconsDir = getIconsDir(packageName, userId);
        if (!iconsDir.isDirectory()) {
            iconsDir.delete();
            iconsDir.mkdirs();
            if (!iconsDir.isDirectory()) {
                throw new java.io.IOException("Unable to create directory " + iconsDir);
            }
            android.util.Slog.i(TAG, "Created icons directory at " + iconsDir.getAbsolutePath());
        }
        android.os.SELinux.restorecon(iconsDir);
        return iconsDir;
    }

    private static java.io.File getIconsDir(java.lang.String packageName, int userId) {
        return new java.io.File(new java.io.File(android.os.Environment.getDataSystemCeDirectory(userId), ARCHIVE_ICONS_DIR), packageName);
    }

    private static byte[] bytesFromBitmapFile(java.nio.file.Path path) throws java.io.IOException {
        if (path == null) {
            return null;
        }
        return android.content.pm.ArchivedActivityInfo.bytesFromBitmap(android.graphics.BitmapFactory.decodeFile(path.toString()));
    }

    private static java.lang.String getPackageNameFromIntent(android.content.Intent intent) {
        if (intent == null) {
            return null;
        }
        if (intent.getPackage() != null) {
            return intent.getPackage();
        }
        if (intent.getComponent() == null) {
            return null;
        }
        return intent.getComponent().getPackageName();
    }

    static android.content.pm.ArchivedActivityParcel[] createArchivedActivities(com.android.server.pm.pkg.ArchiveState archiveState) throws java.io.IOException {
        java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> infos = archiveState.getActivityInfos();
        if (infos == null || infos.isEmpty()) {
            throw new java.lang.IllegalArgumentException("No activities in archive state");
        }
        java.util.List<android.content.pm.ArchivedActivityParcel> activities = new java.util.ArrayList<>(infos.size());
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo info = infos.get(i);
            if (info != null) {
                android.content.pm.ArchivedActivityParcel archivedActivity = new android.content.pm.ArchivedActivityParcel();
                archivedActivity.title = info.getTitle();
                archivedActivity.originalComponentName = info.getOriginalComponentName();
                archivedActivity.iconBitmap = bytesFromBitmapFile(info.getIconBitmap());
                archivedActivity.monochromeIconBitmap = bytesFromBitmapFile(info.getMonochromeIconBitmap());
                activities.add(archivedActivity);
            }
        }
        if (activities.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Failed to extract title and icon of main activities");
        }
        return (android.content.pm.ArchivedActivityParcel[]) activities.toArray(new android.content.pm.ArchivedActivityParcel[activities.size()]);
    }

    static android.content.pm.ArchivedActivityParcel[] createArchivedActivities(java.util.List<android.content.pm.LauncherActivityInfo> infos, int iconSize) throws java.io.IOException {
        if (infos == null || infos.isEmpty()) {
            throw new java.lang.IllegalArgumentException("No launcher activities");
        }
        java.util.List<android.content.pm.ArchivedActivityParcel> activities = new java.util.ArrayList<>(infos.size());
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            android.content.pm.LauncherActivityInfo info = infos.get(i);
            if (info != null) {
                android.content.pm.ArchivedActivityParcel archivedActivity = new android.content.pm.ArchivedActivityParcel();
                archivedActivity.title = info.getLabel().toString();
                archivedActivity.originalComponentName = info.getComponentName();
                archivedActivity.iconBitmap = info.getActivityInfo().getIconResource() == 0 ? null : android.content.pm.ArchivedActivityInfo.bytesFromBitmap(android.content.pm.ArchivedActivityInfo.drawableToBitmap(info.getIcon(0), iconSize));
                archivedActivity.monochromeIconBitmap = null;
                activities.add(archivedActivity);
            }
        }
        if (activities.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Failed to extract title and icon of main activities");
        }
        return (android.content.pm.ArchivedActivityParcel[]) activities.toArray(new android.content.pm.ArchivedActivityParcel[activities.size()]);
    }

    private class UnarchiveIntentSender extends android.content.IIntentSender.Stub {
        private UnarchiveIntentSender() {
        }

        public void send(int code, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder whitelistToken, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) throws android.os.RemoteException {
            int status = intent.getExtras().getInt("android.content.pm.extra.UNARCHIVE_STATUS", -1);
            if (status == 0) {
                return;
            }
            android.content.Intent extraIntent = (android.content.Intent) intent.getParcelableExtra("android.intent.extra.INTENT", android.content.Intent.class);
            android.os.UserHandle user = (android.os.UserHandle) intent.getParcelableExtra("android.intent.extra.USER", android.os.UserHandle.class);
            if (extraIntent != null && user != null && com.android.server.pm.PackageArchiver.this.mAppStateHelper.isAppTopVisible(com.android.server.pm.PackageArchiver.this.getCurrentLauncherPackageName(user.getIdentifier()))) {
                extraIntent.setFlags(268435456);
                com.android.server.pm.PackageArchiver.this.mContext.startActivityAsUser(extraIntent, user);
            }
        }
    }
}
