package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class ScanResult {
    public final java.util.List<java.lang.String> mChangedAbiCodePath;
    public final java.util.List<android.content.pm.SharedLibraryInfo> mDynamicSharedLibraryInfos;
    public final boolean mExistingSettingCopied;
    public final com.android.server.pm.PackageSetting mPkgSetting;
    public final int mPreviousAppId = -1;
    public final com.android.server.pm.ScanRequest mRequest;
    public final android.content.pm.SharedLibraryInfo mSdkSharedLibraryInfo;
    public final android.content.pm.SharedLibraryInfo mStaticSharedLibraryInfo;

    ScanResult(com.android.server.pm.ScanRequest request, com.android.server.pm.PackageSetting pkgSetting, java.util.List<java.lang.String> changedAbiCodePath, boolean existingSettingCopied, int previousAppId, android.content.pm.SharedLibraryInfo sdkSharedLibraryInfo, android.content.pm.SharedLibraryInfo staticSharedLibraryInfo, java.util.List<android.content.pm.SharedLibraryInfo> dynamicSharedLibraryInfos) {
        this.mRequest = request;
        this.mPkgSetting = pkgSetting;
        this.mChangedAbiCodePath = changedAbiCodePath;
        this.mExistingSettingCopied = existingSettingCopied;
        this.mSdkSharedLibraryInfo = sdkSharedLibraryInfo;
        this.mStaticSharedLibraryInfo = staticSharedLibraryInfo;
        this.mDynamicSharedLibraryInfos = dynamicSharedLibraryInfos;
    }

    public boolean needsNewAppId() {
        return this.mPreviousAppId != -1;
    }
}
