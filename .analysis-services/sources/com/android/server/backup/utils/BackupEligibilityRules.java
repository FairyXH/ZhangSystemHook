package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public class BackupEligibilityRules {
    private static final boolean DEBUG = false;
    static final long IGNORE_ALLOW_BACKUP_IN_D2D = 183147249;
    static final long RESTRICT_ADB_BACKUP = 171032338;
    private final int mBackupDestination;
    private boolean mIsProfileUser;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final boolean mSkipRestoreForLaunchedApps;
    private final int mUserId;
    private static final java.util.Set<java.lang.String> systemPackagesAllowedForProfileUser = com.google.android.collect.Sets.newArraySet(new java.lang.String[]{com.android.server.backup.UserBackupManagerService.PACKAGE_MANAGER_SENTINEL, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME});
    private static final java.util.Set<java.lang.String> systemPackagesAllowedForNonSystemUsers = com.android.server.backup.SetUtils.union(systemPackagesAllowedForProfileUser, com.google.android.collect.Sets.newArraySet(new java.lang.String[]{com.android.server.backup.UserBackupManagerService.WALLPAPER_PACKAGE, com.android.server.backup.UserBackupManagerService.SETTINGS_PACKAGE}));

    public static com.android.server.backup.utils.BackupEligibilityRules forBackup(android.content.pm.PackageManager packageManager, android.content.pm.PackageManagerInternal packageManagerInternal, int userId, android.content.Context context) {
        return new com.android.server.backup.utils.BackupEligibilityRules(packageManager, packageManagerInternal, userId, context, 0);
    }

    public BackupEligibilityRules(android.content.pm.PackageManager packageManager, android.content.pm.PackageManagerInternal packageManagerInternal, int userId, android.content.Context context, int backupDestination) {
        this(packageManager, packageManagerInternal, userId, context, backupDestination, false);
    }

    public BackupEligibilityRules(android.content.pm.PackageManager packageManager, android.content.pm.PackageManagerInternal packageManagerInternal, int userId, android.content.Context context, int backupDestination, boolean skipRestoreForLaunchedApps) {
        this.mIsProfileUser = false;
        this.mPackageManager = packageManager;
        this.mPackageManagerInternal = packageManagerInternal;
        this.mUserId = userId;
        this.mBackupDestination = backupDestination;
        android.os.UserManager userManager = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        this.mIsProfileUser = userManager.isProfile();
        this.mSkipRestoreForLaunchedApps = skipRestoreForLaunchedApps;
    }

    public boolean appIsEligibleForBackup(android.content.pm.ApplicationInfo app) {
        if (!isAppBackupAllowed(app)) {
            return false;
        }
        if (android.os.UserHandle.isCore(app.uid)) {
            if (this.mUserId != 0) {
                if (this.mIsProfileUser && !systemPackagesAllowedForProfileUser.contains(app.packageName)) {
                    return false;
                }
                if (!this.mIsProfileUser && !systemPackagesAllowedForNonSystemUsers.contains(app.packageName)) {
                    return false;
                }
            }
            if (app.backupAgentName == null) {
                return false;
            }
        }
        if (app.packageName.equals(com.android.server.backup.UserBackupManagerService.SHARED_BACKUP_AGENT_PACKAGE) || app.isInstantApp()) {
            return false;
        }
        return !appIsDisabled(app);
    }

    public boolean isAppBackupAllowed(android.content.pm.ApplicationInfo app) {
        boolean allowBackup = (app.flags & 32768) != 0;
        switch (this.mBackupDestination) {
            case 0:
                return allowBackup;
            case 1:
                boolean isSystemApp = (app.flags & 1) != 0;
                boolean ignoreAllowBackup = !isSystemApp && android.app.compat.CompatChanges.isChangeEnabled(IGNORE_ALLOW_BACKUP_IN_D2D, app.packageName, android.os.UserHandle.of(this.mUserId));
                return ignoreAllowBackup || allowBackup;
            case 2:
                java.lang.String packageName = app.packageName;
                if (packageName == null) {
                    android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Invalid ApplicationInfo object");
                    return false;
                }
                if (!android.app.compat.CompatChanges.isChangeEnabled(RESTRICT_ADB_BACKUP, packageName, android.os.UserHandle.of(this.mUserId))) {
                    return allowBackup;
                }
                if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName)) {
                    return true;
                }
                boolean isPrivileged = (app.flags & 8) != 0;
                boolean isDebuggable = (app.flags & 2) != 0;
                if (android.os.UserHandle.isCore(app.uid) || isPrivileged) {
                    try {
                        return this.mPackageManager.getPropertyAsUser("android.backup.ALLOW_ADB_BACKUP", packageName, null, this.mUserId).getBoolean();
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Failed to read allowAdbBackup property for + " + packageName);
                        return allowBackup;
                    }
                }
                return isDebuggable;
            case 3:
                return true;
            default:
                android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "Unknown operation type:" + this.mBackupDestination);
                return false;
        }
    }

    public boolean appIsRunningAndEligibleForBackupWithTransport(com.android.server.backup.transport.TransportConnection transportConnection, java.lang.String packageName) {
        try {
            android.content.pm.PackageInfo packageInfo = this.mPackageManager.getPackageInfoAsUser(packageName, 134217728, this.mUserId);
            android.content.pm.ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if (!appIsEligibleForBackup(applicationInfo) || appIsStopped(applicationInfo) || appIsDisabled(applicationInfo)) {
                return false;
            }
            if (transportConnection != null) {
                try {
                    com.android.server.backup.transport.BackupTransportClient transport = transportConnection.connectOrThrow("AppBackupUtils.appIsRunningAndEligibleForBackupWithTransport");
                    return transport.isAppEligibleForBackup(packageInfo, appGetsFullBackup(packageInfo));
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(com.android.server.backup.BackupManagerService.TAG, "Unable to ask about eligibility: " + e.getMessage());
                    return true;
                }
            }
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            return false;
        }
    }

    public boolean isAppEligibleForRestore(android.content.pm.ApplicationInfo app) {
        if (this.mSkipRestoreForLaunchedApps && app.backupAgentName == null) {
            return !this.mPackageManagerInternal.wasPackageEverLaunched(app.packageName, this.mUserId);
        }
        return true;
    }

    boolean appIsDisabled(android.content.pm.ApplicationInfo app) {
        int enabledSetting = this.mPackageManagerInternal.getApplicationEnabledState(app.packageName, this.mUserId);
        switch (enabledSetting) {
            case 0:
                return true ^ app.enabled;
            case 1:
            default:
                return false;
            case 2:
            case 3:
            case 4:
                return true;
        }
    }

    public boolean appIsStopped(android.content.pm.ApplicationInfo app) {
        return (app.flags & 2097152) != 0;
    }

    public boolean appGetsFullBackup(android.content.pm.PackageInfo pkg) {
        return pkg.applicationInfo.backupAgentName == null || (pkg.applicationInfo.flags & 67108864) != 0;
    }

    public boolean appIsKeyValueOnly(android.content.pm.PackageInfo pkg) {
        return !appGetsFullBackup(pkg);
    }

    public boolean signaturesMatch(android.content.pm.Signature[] storedSigs, android.content.pm.PackageInfo target) {
        if (target == null || target.packageName == null) {
            return false;
        }
        if ((target.applicationInfo.flags & 1) != 0) {
            return true;
        }
        if (com.android.internal.util.ArrayUtils.isEmpty(storedSigs)) {
            return false;
        }
        android.content.pm.SigningInfo signingInfo = target.signingInfo;
        if (signingInfo == null) {
            android.util.Slog.w(com.android.server.backup.BackupManagerService.TAG, "signingInfo is empty, app was either unsigned or the flag PackageManager#GET_SIGNING_CERTIFICATES was not specified");
            return false;
        }
        int nStored = storedSigs.length;
        if (nStored == 1) {
            return this.mPackageManagerInternal.isDataRestoreSafe(storedSigs[0], target.packageName);
        }
        android.content.pm.Signature[] deviceSigs = signingInfo.getApkContentsSigners();
        int nDevice = deviceSigs.length;
        for (android.content.pm.Signature signature : storedSigs) {
            boolean match = false;
            int j = 0;
            while (true) {
                if (j >= nDevice) {
                    break;
                }
                if (!signature.equals(deviceSigs[j])) {
                    j++;
                } else {
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }
        return true;
    }

    public int getBackupDestination() {
        return this.mBackupDestination;
    }
}
