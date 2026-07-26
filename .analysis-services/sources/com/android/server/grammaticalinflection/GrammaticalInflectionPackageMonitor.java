package com.android.server.grammaticalinflection;

/* JADX INFO: loaded from: classes2.dex */
public class GrammaticalInflectionPackageMonitor extends com.android.internal.content.PackageMonitor {
    private com.android.server.grammaticalinflection.GrammaticalInflectionBackupHelper mBackupHelper;

    GrammaticalInflectionPackageMonitor(com.android.server.grammaticalinflection.GrammaticalInflectionBackupHelper backupHelper) {
        this.mBackupHelper = backupHelper;
    }

    public void onPackageAdded(java.lang.String packageName, int uid) {
        this.mBackupHelper.onPackageAdded(packageName, uid);
    }

    public void onPackageDataCleared(java.lang.String packageName, int uid) {
        this.mBackupHelper.onPackageDataCleared();
    }

    public void onPackageRemoved(java.lang.String packageName, int uid) {
        this.mBackupHelper.onPackageRemoved();
    }
}
