package com.android.server.locales;

/* JADX INFO: loaded from: classes2.dex */
final class LocaleManagerServicePackageMonitor extends com.android.internal.content.PackageMonitor {
    private com.android.server.locales.LocaleManagerBackupHelper mBackupHelper;
    private com.android.server.locales.LocaleManagerService mLocaleManagerService;
    private com.android.server.locales.SystemAppUpdateTracker mSystemAppUpdateTracker;

    LocaleManagerServicePackageMonitor(com.android.server.locales.LocaleManagerBackupHelper localeManagerBackupHelper, com.android.server.locales.SystemAppUpdateTracker systemAppUpdateTracker, com.android.server.locales.LocaleManagerService localeManagerService) {
        this.mBackupHelper = localeManagerBackupHelper;
        this.mSystemAppUpdateTracker = systemAppUpdateTracker;
        this.mLocaleManagerService = localeManagerService;
    }

    public void onPackageAddedWithExtras(java.lang.String packageName, int uid, android.os.Bundle extras) {
        this.mBackupHelper.onPackageAddedWithExtras(packageName, uid, extras);
    }

    public void onPackageDataCleared(java.lang.String packageName, int uid) {
        this.mBackupHelper.onPackageDataCleared(packageName, uid);
    }

    public void onPackageRemoved(java.lang.String packageName, int uid) {
        this.mBackupHelper.onPackageRemoved(packageName, uid);
        this.mLocaleManagerService.deleteOverrideLocaleConfig(packageName, android.os.UserHandle.getUserId(uid));
    }

    public void onPackageUpdateFinished(java.lang.String packageName, int uid) {
        this.mBackupHelper.onPackageUpdateFinished(packageName, uid);
        this.mSystemAppUpdateTracker.onPackageUpdateFinished(packageName, uid);
    }
}
