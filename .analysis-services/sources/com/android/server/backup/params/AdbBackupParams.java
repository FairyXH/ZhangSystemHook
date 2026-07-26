package com.android.server.backup.params;

/* JADX INFO: loaded from: classes.dex */
public class AdbBackupParams extends com.android.server.backup.params.AdbParams {
    public boolean allApps;
    public com.android.server.backup.utils.BackupEligibilityRules backupEligibilityRules;
    public boolean doCompress;
    public boolean doWidgets;
    public boolean includeApks;
    public boolean includeKeyValue;
    public boolean includeObbs;
    public boolean includeShared;
    public boolean includeSystem;
    public java.lang.String[] packages;

    public AdbBackupParams(android.os.ParcelFileDescriptor output, boolean saveApks, boolean saveObbs, boolean saveShared, boolean alsoWidgets, boolean doAllApps, boolean doSystem, boolean compress, boolean doKeyValue, java.lang.String[] pkgList, com.android.server.backup.utils.BackupEligibilityRules eligibilityRules) {
        this.fd = output;
        this.includeApks = saveApks;
        this.includeObbs = saveObbs;
        this.includeShared = saveShared;
        this.doWidgets = alsoWidgets;
        this.allApps = doAllApps;
        this.includeSystem = doSystem;
        this.doCompress = compress;
        this.includeKeyValue = doKeyValue;
        this.packages = pkgList;
        this.backupEligibilityRules = eligibilityRules;
    }
}
