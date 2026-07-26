package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public class PackageStateUnserialized {
    private boolean apkInUpdatedApex;
    private boolean hiddenUntilInstalled;
    private volatile long[] lastPackageUsageTimeInMills;
    private java.lang.String mApexModuleName;
    private final com.android.server.pm.PackageSetting mPackageSetting;
    private java.lang.String overrideSeInfo;
    private java.lang.String seInfo;
    private boolean updatedSystemApp;
    private java.util.List<com.android.server.pm.pkg.SharedLibraryWrapper> usesLibraryInfos = java.util.Collections.emptyList();
    private java.util.List<java.lang.String> usesLibraryFiles = java.util.Collections.emptyList();

    public PackageStateUnserialized(com.android.server.pm.PackageSetting packageSetting) {
        this.mPackageSetting = packageSetting;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized addUsesLibraryInfo(com.android.server.pm.pkg.SharedLibraryWrapper value) {
        this.usesLibraryInfos = com.android.internal.util.CollectionUtils.add(this.usesLibraryInfos, value);
        return this;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized addUsesLibraryFile(java.lang.String value) {
        this.usesLibraryFiles = com.android.internal.util.CollectionUtils.add(this.usesLibraryFiles, value);
        return this;
    }

    private long[] lazyInitLastPackageUsageTimeInMills() {
        return new long[8];
    }

    public com.android.server.pm.pkg.PackageStateUnserialized setLastPackageUsageTimeInMills(int reason, long time) {
        if (reason < 0 || reason >= 8) {
            return this;
        }
        getLastPackageUsageTimeInMills()[reason] = time;
        return this;
    }

    public long getLatestPackageUseTimeInMills() {
        long latestUse = 0;
        for (long use : getLastPackageUsageTimeInMills()) {
            latestUse = java.lang.Math.max(latestUse, use);
        }
        return latestUse;
    }

    public long getLatestForegroundPackageUseTimeInMills() {
        int[] foregroundReasons = {0, 2};
        long latestUse = 0;
        for (int reason : foregroundReasons) {
            latestUse = java.lang.Math.max(latestUse, getLastPackageUsageTimeInMills()[reason]);
        }
        return latestUse;
    }

    public void updateFrom(com.android.server.pm.pkg.PackageStateUnserialized other) {
        this.hiddenUntilInstalled = other.hiddenUntilInstalled;
        if (!other.usesLibraryInfos.isEmpty()) {
            this.usesLibraryInfos = new java.util.ArrayList(other.usesLibraryInfos);
        }
        if (!other.usesLibraryFiles.isEmpty()) {
            this.usesLibraryFiles = new java.util.ArrayList(other.usesLibraryFiles);
        }
        this.updatedSystemApp = other.updatedSystemApp;
        this.apkInUpdatedApex = other.apkInUpdatedApex;
        this.lastPackageUsageTimeInMills = other.lastPackageUsageTimeInMills;
        this.overrideSeInfo = other.overrideSeInfo;
        this.seInfo = other.seInfo;
        this.mApexModuleName = other.mApexModuleName;
        this.mPackageSetting.onChanged();
    }

    public java.util.List<android.content.pm.SharedLibraryInfo> getNonNativeUsesLibraryInfos() {
        java.util.ArrayList<android.content.pm.SharedLibraryInfo> list = new java.util.ArrayList<>();
        this.usesLibraryInfos = getUsesLibraryInfos();
        for (int index = 0; index < this.usesLibraryInfos.size(); index++) {
            com.android.server.pm.pkg.SharedLibraryWrapper library = this.usesLibraryInfos.get(index);
            if (!library.isNative()) {
                list.add(library.getInfo());
            }
        }
        return list;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized setHiddenUntilInstalled(boolean value) {
        this.hiddenUntilInstalled = value;
        this.mPackageSetting.onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized setUsesLibraryInfos(java.util.List<android.content.pm.SharedLibraryInfo> value) {
        java.util.ArrayList<com.android.server.pm.pkg.SharedLibraryWrapper> list = new java.util.ArrayList<>();
        for (int index = 0; index < value.size(); index++) {
            list.add(new com.android.server.pm.pkg.SharedLibraryWrapper(value.get(index)));
        }
        this.usesLibraryInfos = list;
        this.mPackageSetting.onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized setUsesLibraryFiles(java.util.List<java.lang.String> value) {
        this.usesLibraryFiles = value;
        this.mPackageSetting.onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized setUpdatedSystemApp(boolean value) {
        this.updatedSystemApp = value;
        this.mPackageSetting.onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized setApkInUpdatedApex(boolean value) {
        this.apkInUpdatedApex = value;
        this.mPackageSetting.onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized setLastPackageUsageTimeInMills(long... value) {
        this.lastPackageUsageTimeInMills = value;
        this.mPackageSetting.onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized setOverrideSeInfo(java.lang.String value) {
        this.overrideSeInfo = value;
        this.mPackageSetting.onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized setSeInfo(java.lang.String value) {
        this.seInfo = android.text.TextUtils.safeIntern(value);
        this.mPackageSetting.onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized setApexModuleName(java.lang.String value) {
        this.mApexModuleName = value;
        this.mPackageSetting.onChanged();
        return this;
    }

    public boolean isHiddenUntilInstalled() {
        return this.hiddenUntilInstalled;
    }

    public java.util.List<com.android.server.pm.pkg.SharedLibraryWrapper> getUsesLibraryInfos() {
        return this.usesLibraryInfos;
    }

    public java.util.List<java.lang.String> getUsesLibraryFiles() {
        return this.usesLibraryFiles;
    }

    public boolean isUpdatedSystemApp() {
        return this.updatedSystemApp;
    }

    public boolean isApkInUpdatedApex() {
        return this.apkInUpdatedApex;
    }

    public long[] getLastPackageUsageTimeInMills() {
        long[] _lastPackageUsageTimeInMills = this.lastPackageUsageTimeInMills;
        if (_lastPackageUsageTimeInMills == null) {
            synchronized (this) {
                _lastPackageUsageTimeInMills = this.lastPackageUsageTimeInMills;
                if (_lastPackageUsageTimeInMills == null) {
                    long[] jArrLazyInitLastPackageUsageTimeInMills = lazyInitLastPackageUsageTimeInMills();
                    this.lastPackageUsageTimeInMills = jArrLazyInitLastPackageUsageTimeInMills;
                    _lastPackageUsageTimeInMills = jArrLazyInitLastPackageUsageTimeInMills;
                }
            }
        }
        return _lastPackageUsageTimeInMills;
    }

    public java.lang.String getOverrideSeInfo() {
        return this.overrideSeInfo;
    }

    public java.lang.String getSeInfo() {
        return this.seInfo;
    }

    public com.android.server.pm.PackageSetting getPackageSetting() {
        return this.mPackageSetting;
    }

    public java.lang.String getApexModuleName() {
        return this.mApexModuleName;
    }

    @java.lang.Deprecated
    private void __metadata() {
    }
}
