package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class InstallSource {
    static final com.android.server.pm.InstallSource EMPTY = new com.android.server.pm.InstallSource(null, null, null, -1, null, null, false, false, null, 0);
    private static final com.android.server.pm.InstallSource EMPTY_ORPHANED = new com.android.server.pm.InstallSource(null, null, null, -1, null, null, true, false, null, 0);
    final java.lang.String mInitiatingPackageName;
    final com.android.server.pm.PackageSignatures mInitiatingPackageSignatures;
    final java.lang.String mInstallerAttributionTag;
    java.lang.String mInstallerPackageName;
    final int mInstallerPackageUid;
    final boolean mIsInitiatingPackageUninstalled;
    final boolean mIsOrphaned;
    final java.lang.String mOriginatingPackageName;
    final int mPackageSource;
    final java.lang.String mUpdateOwnerPackageName;

    static com.android.server.pm.InstallSource create(java.lang.String initiatingPackageName, java.lang.String originatingPackageName, java.lang.String installerPackageName, int installerPackageUid, java.lang.String updateOwnerPackageName, java.lang.String installerAttributionTag, boolean isOrphaned, boolean isInitiatingPackageUninstalled) {
        return create(initiatingPackageName, originatingPackageName, installerPackageName, installerPackageUid, updateOwnerPackageName, installerAttributionTag, 0, isOrphaned, isInitiatingPackageUninstalled);
    }

    static com.android.server.pm.InstallSource create(java.lang.String initiatingPackageName, java.lang.String originatingPackageName, java.lang.String installerPackageName, int installerPackageUid, java.lang.String updateOwnerPackageName, java.lang.String installerAttributionTag, int packageSource) {
        return create(initiatingPackageName, originatingPackageName, installerPackageName, installerPackageUid, updateOwnerPackageName, installerAttributionTag, packageSource, false, false);
    }

    static com.android.server.pm.InstallSource create(java.lang.String initiatingPackageName, java.lang.String originatingPackageName, java.lang.String installerPackageName, int installerPackageUid, java.lang.String updateOwnerPackageName, java.lang.String installerAttributionTag, int packageSource, boolean isOrphaned, boolean isInitiatingPackageUninstalled) {
        return createInternal(intern(initiatingPackageName), intern(originatingPackageName), intern(installerPackageName), installerPackageUid, intern(updateOwnerPackageName), installerAttributionTag, packageSource, isOrphaned, isInitiatingPackageUninstalled, null);
    }

    private static com.android.server.pm.InstallSource createInternal(java.lang.String initiatingPackageName, java.lang.String originatingPackageName, java.lang.String installerPackageName, int installerPackageUid, java.lang.String updateOwnerPackageName, java.lang.String installerAttributionTag, int packageSource, boolean isOrphaned, boolean isInitiatingPackageUninstalled, com.android.server.pm.PackageSignatures initiatingPackageSignatures) {
        if (initiatingPackageName == null && originatingPackageName == null && installerPackageName == null && updateOwnerPackageName == null && initiatingPackageSignatures == null && !isInitiatingPackageUninstalled && packageSource == 0) {
            return isOrphaned ? EMPTY_ORPHANED : EMPTY;
        }
        return new com.android.server.pm.InstallSource(initiatingPackageName, originatingPackageName, installerPackageName, installerPackageUid, updateOwnerPackageName, installerAttributionTag, isOrphaned, isInitiatingPackageUninstalled, initiatingPackageSignatures, packageSource);
    }

    private InstallSource(java.lang.String initiatingPackageName, java.lang.String originatingPackageName, java.lang.String installerPackageName, int installerPackageUid, java.lang.String updateOwnerPackageName, java.lang.String installerAttributionTag, boolean isOrphaned, boolean isInitiatingPackageUninstalled, com.android.server.pm.PackageSignatures initiatingPackageSignatures, int packageSource) {
        if (initiatingPackageName == null) {
            com.android.internal.util.Preconditions.checkArgument(initiatingPackageSignatures == null);
            com.android.internal.util.Preconditions.checkArgument(!isInitiatingPackageUninstalled);
        }
        this.mInitiatingPackageName = initiatingPackageName;
        this.mOriginatingPackageName = originatingPackageName;
        this.mInstallerPackageName = installerPackageName;
        this.mInstallerPackageUid = installerPackageUid;
        this.mUpdateOwnerPackageName = updateOwnerPackageName;
        this.mInstallerAttributionTag = installerAttributionTag;
        this.mIsOrphaned = isOrphaned;
        this.mIsInitiatingPackageUninstalled = isInitiatingPackageUninstalled;
        this.mInitiatingPackageSignatures = initiatingPackageSignatures;
        this.mPackageSource = packageSource;
    }

    com.android.server.pm.InstallSource setInstallerPackage(java.lang.String installerPackageName, int installerPackageUid) {
        if (java.util.Objects.equals(installerPackageName, this.mInstallerPackageName)) {
            return this;
        }
        return createInternal(this.mInitiatingPackageName, this.mOriginatingPackageName, intern(installerPackageName), installerPackageUid, this.mUpdateOwnerPackageName, this.mInstallerAttributionTag, this.mPackageSource, this.mIsOrphaned, this.mIsInitiatingPackageUninstalled, this.mInitiatingPackageSignatures);
    }

    com.android.server.pm.InstallSource setUpdateOwnerPackageName(java.lang.String updateOwnerPackageName) {
        if (java.util.Objects.equals(updateOwnerPackageName, this.mUpdateOwnerPackageName)) {
            return this;
        }
        return createInternal(this.mInitiatingPackageName, this.mOriginatingPackageName, this.mInstallerPackageName, this.mInstallerPackageUid, intern(updateOwnerPackageName), this.mInstallerAttributionTag, this.mPackageSource, this.mIsOrphaned, this.mIsInitiatingPackageUninstalled, this.mInitiatingPackageSignatures);
    }

    com.android.server.pm.InstallSource setIsOrphaned(boolean isOrphaned) {
        if (isOrphaned == this.mIsOrphaned) {
            return this;
        }
        return createInternal(this.mInitiatingPackageName, this.mOriginatingPackageName, this.mInstallerPackageName, this.mInstallerPackageUid, this.mUpdateOwnerPackageName, this.mInstallerAttributionTag, this.mPackageSource, isOrphaned, this.mIsInitiatingPackageUninstalled, this.mInitiatingPackageSignatures);
    }

    com.android.server.pm.InstallSource setInitiatingPackageSignatures(com.android.server.pm.PackageSignatures signatures) {
        if (signatures == this.mInitiatingPackageSignatures) {
            return this;
        }
        return createInternal(this.mInitiatingPackageName, this.mOriginatingPackageName, this.mInstallerPackageName, this.mInstallerPackageUid, this.mUpdateOwnerPackageName, this.mInstallerAttributionTag, this.mPackageSource, this.mIsOrphaned, this.mIsInitiatingPackageUninstalled, signatures);
    }

    com.android.server.pm.InstallSource removeInstallerPackage(java.lang.String packageName) {
        if (packageName == null) {
            return this;
        }
        boolean modified = false;
        boolean isInitiatingPackageUninstalled = this.mIsInitiatingPackageUninstalled;
        java.lang.String originatingPackageName = this.mOriginatingPackageName;
        java.lang.String installerPackageName = this.mInstallerPackageName;
        java.lang.String updateOwnerPackageName = this.mUpdateOwnerPackageName;
        int installerPackageUid = this.mInstallerPackageUid;
        boolean isOrphaned = this.mIsOrphaned;
        if (packageName.equals(this.mInitiatingPackageName) && !isInitiatingPackageUninstalled) {
            isInitiatingPackageUninstalled = true;
            modified = true;
        }
        if (packageName.equals(originatingPackageName)) {
            originatingPackageName = null;
            modified = true;
        }
        if (packageName.equals(installerPackageName)) {
            installerPackageName = null;
            installerPackageUid = -1;
            isOrphaned = true;
            modified = true;
        }
        if (packageName.equals(updateOwnerPackageName)) {
            updateOwnerPackageName = null;
            modified = true;
        }
        if (!modified) {
            return this;
        }
        return createInternal(this.mInitiatingPackageName, originatingPackageName, installerPackageName, installerPackageUid, updateOwnerPackageName, null, this.mPackageSource, isOrphaned, isInitiatingPackageUninstalled, this.mInitiatingPackageSignatures);
    }

    private static java.lang.String intern(java.lang.String packageName) {
        if (packageName == null) {
            return null;
        }
        return packageName.intern();
    }
}
