package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class ReconciledPackage {
    private final java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> mAllPackages;
    public final java.util.List<android.content.pm.SharedLibraryInfo> mAllowedSharedLibraryInfos;
    public java.util.ArrayList<android.content.pm.SharedLibraryInfo> mCollectedSharedLibraryInfos;
    public final com.android.server.pm.DeletePackageAction mDeletePackageAction;
    public final com.android.server.pm.InstallRequest mInstallRequest;
    private final java.util.List<com.android.server.pm.InstallRequest> mInstallRequests;
    public final boolean mRemoveAppKeySetData;
    public final boolean mSharedUserSignaturesChanged;
    public final android.content.pm.SigningDetails mSigningDetails;

    ReconciledPackage(java.util.List<com.android.server.pm.InstallRequest> installRequests, java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> allPackages, com.android.server.pm.InstallRequest installRequest, com.android.server.pm.DeletePackageAction deletePackageAction, java.util.List<android.content.pm.SharedLibraryInfo> allowedSharedLibraryInfos, android.content.pm.SigningDetails signingDetails, boolean sharedUserSignaturesChanged, boolean removeAppKeySetData) {
        this.mInstallRequests = installRequests;
        this.mAllPackages = allPackages;
        this.mInstallRequest = installRequest;
        this.mDeletePackageAction = deletePackageAction;
        this.mAllowedSharedLibraryInfos = allowedSharedLibraryInfos;
        this.mSigningDetails = signingDetails;
        this.mSharedUserSignaturesChanged = sharedUserSignaturesChanged;
        this.mRemoveAppKeySetData = removeAppKeySetData;
    }

    java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> getCombinedAvailablePackages() {
        android.util.ArrayMap<java.lang.String, com.android.server.pm.pkg.AndroidPackage> combined = new android.util.ArrayMap<>(this.mAllPackages.size() + this.mInstallRequests.size());
        combined.putAll(this.mAllPackages);
        for (com.android.server.pm.InstallRequest installRequest : this.mInstallRequests) {
            combined.put(installRequest.getScannedPackageSetting().getPackageName(), installRequest.getParsedPackage());
        }
        return combined;
    }
}
