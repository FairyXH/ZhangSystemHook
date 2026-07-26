package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class ScanRequest {
    public final java.lang.String mCpuAbiOverride;
    public final com.android.server.pm.PackageSetting mDisabledPkgSetting;
    public final boolean mIsPlatformPackage;
    public final com.android.server.pm.pkg.AndroidPackage mOldPkg;
    public final com.android.server.pm.PackageSetting mOldPkgSetting;
    public final com.android.server.pm.SharedUserSetting mOldSharedUserSetting;
    public final com.android.server.pm.PackageSetting mOriginalPkgSetting;
    public final int mParseFlags;
    public final com.android.internal.pm.parsing.pkg.ParsedPackage mParsedPackage;
    public final com.android.server.pm.PackageSetting mPkgSetting;
    public final java.lang.String mRealPkgName;
    public final int mScanFlags;
    public final com.android.server.pm.SharedUserSetting mSharedUserSetting;
    public final android.os.UserHandle mUser;

    ScanRequest(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, com.android.server.pm.SharedUserSetting oldSharedUserSetting, com.android.server.pm.pkg.AndroidPackage oldPkg, com.android.server.pm.PackageSetting pkgSetting, com.android.server.pm.SharedUserSetting sharedUserSetting, com.android.server.pm.PackageSetting disabledPkgSetting, com.android.server.pm.PackageSetting originalPkgSetting, java.lang.String realPkgName, int parseFlags, int scanFlags, boolean isPlatformPackage, android.os.UserHandle user, java.lang.String cpuAbiOverride) {
        this.mParsedPackage = parsedPackage;
        this.mOldPkg = oldPkg;
        this.mPkgSetting = pkgSetting;
        this.mOldSharedUserSetting = oldSharedUserSetting;
        this.mSharedUserSetting = sharedUserSetting;
        this.mOldPkgSetting = pkgSetting == null ? null : new com.android.server.pm.PackageSetting(pkgSetting);
        this.mDisabledPkgSetting = disabledPkgSetting;
        this.mOriginalPkgSetting = originalPkgSetting;
        this.mRealPkgName = realPkgName;
        this.mParseFlags = parseFlags;
        this.mScanFlags = scanFlags;
        this.mIsPlatformPackage = isPlatformPackage;
        this.mUser = user;
        this.mCpuAbiOverride = cpuAbiOverride;
    }
}
