package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageSetting extends com.android.server.pm.SettingBase implements com.android.server.pm.pkg.PackageStateInternal {
    private int categoryOverride;
    private com.android.server.pm.InstallSource installSource;
    private com.android.server.pm.PackageKeySetData keySetData;
    private long lastUpdateTime;

    @java.lang.Deprecated
    private java.lang.String legacyNativeLibraryPath;
    private int mAppId;
    private java.lang.String mAppMetadataFilePath;
    private int mAppMetadataSource;
    private int mBooleans;
    private java.lang.String mCpuAbiOverride;
    private java.util.UUID mDomainSetId;
    public com.android.server.pm.IPackageSettingExt mExtImpl;
    private long mLastModifiedTime;
    private long mLoadingCompletedTime;
    private float mLoadingProgress;
    private java.lang.String mName;
    private java.util.LinkedHashSet<java.io.File> mOldPaths;
    private java.io.File mPath;
    private java.lang.String mPathString;
    private java.lang.String mPrimaryCpuAbi;
    private java.lang.String mRealName;
    private byte[] mRestrictUpdateHash;
    private java.lang.String mSecondaryCpuAbi;
    private int mSharedUserAppId;
    private final com.android.server.utils.SnapshotCache<com.android.server.pm.PackageSetting> mSnapshot;
    private int mTargetSdkVersion;
    private final android.util.SparseArray<com.android.server.pm.pkg.PackageUserStateImpl> mUserStates;
    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> mimeGroups;
    private com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg;
    private final com.android.server.pm.pkg.PackageStateUnserialized pkgState;
    private com.android.server.pm.PackageSignatures signatures;
    private java.lang.String[] usesSdkLibraries;
    private boolean[] usesSdkLibrariesOptional;
    private long[] usesSdkLibrariesVersionsMajor;
    private java.lang.String[] usesStaticLibraries;
    private long[] usesStaticLibrariesVersions;
    private long versionCode;
    private java.lang.String volumeUuid;

    private static class Booleans {
        private static final int FORCE_QUERYABLE_OVERRIDE = 4;
        private static final int INSTALL_PERMISSION_FIXED = 1;
        private static final int PENDING_RESTORE = 16;
        private static final int SCANNED_AS_STOPPED_SYSTEM_APP = 8;
        private static final int UPDATE_AVAILABLE = 2;

        public @interface Flags {
        }

        private Booleans() {
        }
    }

    private void setBoolean(int flag, boolean value) {
        if (value) {
            this.mBooleans |= flag;
        } else {
            this.mBooleans &= ~flag;
        }
    }

    private boolean getBoolean(int flag) {
        return (this.mBooleans & flag) != 0;
    }

    private com.android.server.utils.SnapshotCache<com.android.server.pm.PackageSetting> makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.PackageSetting>(this, this) { // from class: com.android.server.pm.PackageSetting.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.PackageSetting createSnapshot() {
                return new com.android.server.pm.PackageSetting((com.android.server.pm.PackageSetting) this.mSource, true);
            }
        };
    }

    public PackageSetting(java.lang.String name, java.lang.String realName, java.io.File path, int pkgFlags, int pkgPrivateFlags, java.util.UUID domainSetId) {
        super(pkgFlags, pkgPrivateFlags);
        this.keySetData = new com.android.server.pm.PackageKeySetData();
        this.mUserStates = new android.util.SparseArray<>();
        this.categoryOverride = -1;
        this.pkgState = new com.android.server.pm.pkg.PackageStateUnserialized(this);
        this.mAppMetadataSource = 0;
        this.mExtImpl = (com.android.server.pm.IPackageSettingExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageSettingExt.class).base(this).create();
        this.mName = name;
        this.mRealName = realName;
        this.mPath = path;
        this.mPathString = path.toString();
        this.signatures = new com.android.server.pm.PackageSignatures();
        this.installSource = com.android.server.pm.InstallSource.EMPTY;
        this.mDomainSetId = domainSetId;
        this.mSnapshot = makeCache();
    }

    PackageSetting(com.android.server.pm.PackageSetting orig) {
        this(orig, false);
    }

    PackageSetting(com.android.server.pm.PackageSetting base, java.lang.String realPkgName) {
        this(base, false);
        this.mRealName = realPkgName;
    }

    public PackageSetting(com.android.server.pm.PackageSetting original, boolean sealedSnapshot) {
        super(original);
        this.keySetData = new com.android.server.pm.PackageKeySetData();
        this.mUserStates = new android.util.SparseArray<>();
        this.categoryOverride = -1;
        this.pkgState = new com.android.server.pm.pkg.PackageStateUnserialized(this);
        this.mAppMetadataSource = 0;
        this.mExtImpl = (com.android.server.pm.IPackageSettingExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageSettingExt.class).base(this).create();
        copyPackageSetting(original, sealedSnapshot);
        if (sealedSnapshot) {
            this.mSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        } else {
            this.mSnapshot = makeCache();
        }
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.PackageSetting snapshot() {
        return this.mSnapshot.snapshot();
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, java.util.List<android.content.pm.UserInfo> users, com.android.server.pm.permission.LegacyPermissionDataProvider dataProvider) {
        long packageToken = proto.start(fieldId);
        proto.write(1138166333441L, this.mRealName != null ? this.mRealName : this.mName);
        proto.write(1120986464258L, this.mAppId);
        proto.write(1120986464259L, this.versionCode);
        proto.write(1112396529670L, this.lastUpdateTime);
        proto.write(1138166333447L, this.installSource.mInstallerPackageName);
        if (this.pkg != null) {
            proto.write(1138166333444L, this.pkg.getVersionName());
            long splitToken = proto.start(2246267895816L);
            proto.write(1138166333441L, "base");
            proto.write(1120986464258L, this.pkg.getBaseRevisionCode());
            proto.end(splitToken);
            for (int i = 0; i < this.pkg.getSplitNames().length; i++) {
                long splitToken2 = proto.start(2246267895816L);
                proto.write(1138166333441L, this.pkg.getSplitNames()[i]);
                proto.write(1120986464258L, this.pkg.getSplitRevisionCodes()[i]);
                proto.end(splitToken2);
            }
            long sourceToken = proto.start(1146756268042L);
            proto.write(1138166333441L, this.installSource.mInitiatingPackageName);
            proto.write(1138166333442L, this.installSource.mOriginatingPackageName);
            proto.write(1138166333443L, this.installSource.mUpdateOwnerPackageName);
            proto.end(sourceToken);
        }
        proto.write(1133871366146L, isLoading());
        writeUsersInfoToProto(proto, 2246267895817L);
        writePackageUserPermissionsProto(proto, 2246267895820L, users, dataProvider);
        proto.end(packageToken);
    }

    public com.android.server.pm.PackageSetting setAppId(int appId) {
        this.mAppId = appId;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setCpuAbiOverride(java.lang.String cpuAbiOverrideString) {
        this.mCpuAbiOverride = cpuAbiOverrideString;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setFirstInstallTimeFromReplaced(com.android.server.pm.pkg.PackageStateInternal replacedPkgSetting, int[] userIds) {
        for (int userId = 0; userId < userIds.length; userId++) {
            long previousFirstInstallTime = replacedPkgSetting.getUserStateOrDefault(userId).getFirstInstallTimeMillis();
            if (previousFirstInstallTime != 0) {
                modifyUserState(userId).setFirstInstallTimeMillis(previousFirstInstallTime);
            }
        }
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setFirstInstallTime(long firstInstallTime, int userId) {
        if (userId == -1) {
            int userStateCount = this.mUserStates.size();
            for (int i = 0; i < userStateCount; i++) {
                this.mUserStates.valueAt(i).setFirstInstallTimeMillis(firstInstallTime);
            }
        } else {
            modifyUserState(userId).setFirstInstallTimeMillis(firstInstallTime);
        }
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setForceQueryableOverride(boolean forceQueryableOverride) {
        setBoolean(4, forceQueryableOverride);
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setInstallerPackage(java.lang.String installerPackageName, int installerPackageUid) {
        this.installSource = this.installSource.setInstallerPackage(installerPackageName, installerPackageUid);
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setUpdateOwnerPackage(java.lang.String updateOwnerPackageName) {
        this.installSource = this.installSource.setUpdateOwnerPackageName(updateOwnerPackageName);
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setInstallSource(com.android.server.pm.InstallSource installSource) {
        this.installSource = (com.android.server.pm.InstallSource) java.util.Objects.requireNonNull(installSource);
        onChanged();
        return this;
    }

    com.android.server.pm.PackageSetting removeInstallerPackage(java.lang.String packageName) {
        this.installSource = this.installSource.removeInstallerPackage(packageName);
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setIsOrphaned(boolean isOrphaned) {
        this.installSource = this.installSource.setIsOrphaned(isOrphaned);
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setKeySetData(com.android.server.pm.PackageKeySetData keySetData) {
        this.keySetData = keySetData;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setLastModifiedTime(long timeStamp) {
        this.mLastModifiedTime = timeStamp;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setLongVersionCode(long versionCode) {
        this.versionCode = versionCode;
        onChanged();
        return this;
    }

    public boolean setMimeGroup(java.lang.String mimeGroup, android.util.ArraySet<java.lang.String> newMimeTypes) {
        java.util.Set<java.lang.String> oldMimeTypes = this.mimeGroups == null ? null : this.mimeGroups.get(mimeGroup);
        if (oldMimeTypes == null) {
            throw new java.lang.IllegalArgumentException("Unknown MIME group " + mimeGroup + " for package " + this.mName);
        }
        boolean hasChanges = !newMimeTypes.equals(oldMimeTypes);
        this.mimeGroups.put(mimeGroup, newMimeTypes);
        if (hasChanges) {
            onChanged();
        }
        return hasChanges;
    }

    public com.android.server.pm.PackageSetting setPkg(com.android.server.pm.pkg.AndroidPackage pkg) {
        this.pkg = (com.android.internal.pm.parsing.pkg.AndroidPackageInternal) pkg;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setPkgStateLibraryFiles(java.util.Collection<java.lang.String> usesLibraryFiles) {
        java.util.Collection<java.lang.String> oldUsesLibraryFiles = getUsesLibraryFiles();
        if (oldUsesLibraryFiles.size() != usesLibraryFiles.size() || !oldUsesLibraryFiles.containsAll(usesLibraryFiles)) {
            this.pkgState.setUsesLibraryFiles(new java.util.ArrayList(usesLibraryFiles));
            onChanged();
        }
        return this;
    }

    public com.android.server.pm.PackageSetting setPrimaryCpuAbi(java.lang.String primaryCpuAbiString) {
        this.mPrimaryCpuAbi = primaryCpuAbiString;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setSecondaryCpuAbi(java.lang.String secondaryCpuAbiString) {
        this.mSecondaryCpuAbi = secondaryCpuAbiString;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setSignatures(com.android.server.pm.PackageSignatures signatures) {
        this.signatures = signatures;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setVolumeUuid(java.lang.String volumeUuid) {
        this.volumeUuid = volumeUuid;
        onChanged();
        return this;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isExternalStorage() {
        return (getFlags() & 262144) != 0;
    }

    public com.android.server.pm.PackageSetting setUpdateAvailable(boolean updateAvailable) {
        setBoolean(2, updateAvailable);
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setSharedUserAppId(int sharedUserAppId) {
        this.mSharedUserAppId = sharedUserAppId;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setTargetSdkVersion(int targetSdkVersion) {
        this.mTargetSdkVersion = targetSdkVersion;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setRestrictUpdateHash(byte[] restrictUpdateHash) {
        this.mRestrictUpdateHash = restrictUpdateHash;
        onChanged();
        return this;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public int getSharedUserAppId() {
        return this.mSharedUserAppId;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean hasSharedUser() {
        return this.mSharedUserAppId > 0;
    }

    public com.android.server.pm.PackageSetting setPendingRestore(boolean value) {
        setBoolean(16, value);
        onChanged();
        return this;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isPendingRestore() {
        return getBoolean(16);
    }

    public java.lang.String toString() {
        return "PackageSetting{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + this.mName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mAppId + "}";
    }

    private void copyMimeGroups(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> newMimeGroups) {
        if (newMimeGroups == null) {
            this.mimeGroups = null;
            return;
        }
        this.mimeGroups = new android.util.ArrayMap(newMimeGroups.size());
        for (java.lang.String mimeGroup : newMimeGroups.keySet()) {
            java.util.Set<java.lang.String> mimeTypes = newMimeGroups.get(mimeGroup);
            if (mimeTypes != null) {
                this.mimeGroups.put(mimeGroup, new android.util.ArraySet(mimeTypes));
            } else {
                this.mimeGroups.put(mimeGroup, new android.util.ArraySet());
            }
        }
    }

    public void updateFrom(com.android.server.pm.PackageSetting other) {
        copyPackageSetting(other, false);
        java.util.Set<java.lang.String> mimeGroupNames = other.mimeGroups != null ? other.mimeGroups.keySet() : null;
        updateMimeGroups(mimeGroupNames);
        onChanged();
    }

    com.android.server.pm.PackageSetting updateMimeGroups(java.util.Set<java.lang.String> newMimeGroupNames) {
        if (newMimeGroupNames == null) {
            this.mimeGroups = null;
            return this;
        }
        if (this.mimeGroups == null) {
            this.mimeGroups = java.util.Collections.emptyMap();
        }
        android.util.ArrayMap<java.lang.String, java.util.Set<java.lang.String>> updatedMimeGroups = new android.util.ArrayMap<>(newMimeGroupNames.size());
        for (java.lang.String mimeGroup : newMimeGroupNames) {
            if (this.mimeGroups.containsKey(mimeGroup)) {
                updatedMimeGroups.put(mimeGroup, this.mimeGroups.get(mimeGroup));
            } else {
                updatedMimeGroups.put(mimeGroup, new android.util.ArraySet<>());
            }
        }
        onChanged();
        this.mimeGroups = updatedMimeGroups;
        return this;
    }

    @Override // com.android.server.pm.SettingBase, com.android.server.pm.pkg.PackageStateInternal
    @java.lang.Deprecated
    public com.android.server.pm.permission.LegacyPermissionState getLegacyPermissionState() {
        return super.getLegacyPermissionState();
    }

    public com.android.server.pm.PackageSetting setInstallPermissionsFixed(boolean installPermissionsFixed) {
        setBoolean(1, installPermissionsFixed);
        return this;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isPrivileged() {
        return (getPrivateFlags() & 8) != 0;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isOem() {
        return (getPrivateFlags() & 131072) != 0;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isVendor() {
        return (getPrivateFlags() & 262144) != 0;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isProduct() {
        return (getPrivateFlags() & 524288) != 0;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isRequiredForSystemUser() {
        return (getPrivateFlags() & 512) != 0;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isSystemExt() {
        return (getPrivateFlags() & 2097152) != 0;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isOdm() {
        return (getPrivateFlags() & 1073741824) != 0;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isSystem() {
        return (getFlags() & 1) != 0;
    }

    public boolean isRequestLegacyExternalStorage() {
        return (getPrivateFlags() & 536870912) != 0;
    }

    public boolean isUserDataFragile() {
        return (getPrivateFlags() & 16777216) != 0;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public android.content.pm.SigningDetails getSigningDetails() {
        return this.signatures.mSigningDetails;
    }

    public com.android.server.pm.PackageSetting setSigningDetails(android.content.pm.SigningDetails signingDetails) {
        this.signatures.mSigningDetails = signingDetails;
        onChanged();
        return this;
    }

    public void copyPackageSetting(com.android.server.pm.PackageSetting other, boolean sealedSnapshot) {
        java.lang.String[] strArr;
        long[] jArrCopyOf;
        boolean[] zArrCopyOf;
        java.lang.String[] strArr2;
        super.copySettingBase(other);
        this.mBooleans = other.mBooleans;
        this.mSharedUserAppId = other.mSharedUserAppId;
        this.mLoadingProgress = other.mLoadingProgress;
        this.mLoadingCompletedTime = other.mLoadingCompletedTime;
        this.legacyNativeLibraryPath = other.legacyNativeLibraryPath;
        this.mName = other.mName;
        this.mRealName = other.mRealName;
        this.mAppId = other.mAppId;
        this.pkg = other.pkg;
        this.mPath = other.mPath;
        this.mPathString = other.mPathString;
        this.mOldPaths = other.mOldPaths == null ? null : new java.util.LinkedHashSet<>(other.mOldPaths);
        this.mPrimaryCpuAbi = other.mPrimaryCpuAbi;
        this.mSecondaryCpuAbi = other.mSecondaryCpuAbi;
        this.mCpuAbiOverride = other.mCpuAbiOverride;
        this.mLastModifiedTime = other.mLastModifiedTime;
        this.lastUpdateTime = other.lastUpdateTime;
        this.versionCode = other.versionCode;
        this.signatures = other.signatures;
        this.keySetData = new com.android.server.pm.PackageKeySetData(other.keySetData);
        this.installSource = other.installSource;
        this.volumeUuid = other.volumeUuid;
        this.categoryOverride = other.categoryOverride;
        this.mDomainSetId = other.mDomainSetId;
        this.mAppMetadataFilePath = other.mAppMetadataFilePath;
        this.mAppMetadataSource = other.mAppMetadataSource;
        this.mTargetSdkVersion = other.mTargetSdkVersion;
        this.mRestrictUpdateHash = other.mRestrictUpdateHash == null ? null : (byte[]) other.mRestrictUpdateHash.clone();
        if (other.usesSdkLibraries != null) {
            strArr = (java.lang.String[]) java.util.Arrays.copyOf(other.usesSdkLibraries, other.usesSdkLibraries.length);
        } else {
            strArr = null;
        }
        this.usesSdkLibraries = strArr;
        if (other.usesSdkLibrariesVersionsMajor != null) {
            jArrCopyOf = java.util.Arrays.copyOf(other.usesSdkLibrariesVersionsMajor, other.usesSdkLibrariesVersionsMajor.length);
        } else {
            jArrCopyOf = null;
        }
        this.usesSdkLibrariesVersionsMajor = jArrCopyOf;
        if (other.usesSdkLibrariesOptional != null) {
            zArrCopyOf = java.util.Arrays.copyOf(other.usesSdkLibrariesOptional, other.usesSdkLibrariesOptional.length);
        } else {
            zArrCopyOf = null;
        }
        this.usesSdkLibrariesOptional = zArrCopyOf;
        if (other.usesStaticLibraries != null) {
            strArr2 = (java.lang.String[]) java.util.Arrays.copyOf(other.usesStaticLibraries, other.usesStaticLibraries.length);
        } else {
            strArr2 = null;
        }
        this.usesStaticLibraries = strArr2;
        this.usesStaticLibrariesVersions = other.usesStaticLibrariesVersions != null ? java.util.Arrays.copyOf(other.usesStaticLibrariesVersions, other.usesStaticLibrariesVersions.length) : null;
        this.mUserStates.clear();
        for (int i = 0; i < other.mUserStates.size(); i++) {
            if (sealedSnapshot) {
                this.mUserStates.put(other.mUserStates.keyAt(i), other.mUserStates.valueAt(i).snapshot());
            } else {
                com.android.server.pm.pkg.PackageUserStateImpl userState = other.mUserStates.valueAt(i);
                userState.setWatchable(this);
                this.mUserStates.put(other.mUserStates.keyAt(i), userState);
            }
        }
        copyMimeGroups(other.mimeGroups);
        this.pkgState.updateFrom(other.pkgState);
        onChanged();
    }

    com.android.server.pm.pkg.PackageUserStateImpl modifyUserState(int userId) {
        com.android.server.pm.pkg.PackageUserStateImpl state = this.mUserStates.get(userId);
        if (state == null) {
            com.android.server.pm.pkg.PackageUserStateImpl state2 = new com.android.server.pm.pkg.PackageUserStateImpl(this);
            this.mUserStates.put(userId, state2);
            onChanged();
            return state2;
        }
        return state;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl getOrCreateUserState(int userId) {
        com.android.server.pm.pkg.PackageUserStateImpl state = this.mUserStates.get(userId);
        if (state == null) {
            com.android.server.pm.pkg.PackageUserStateImpl state2 = new com.android.server.pm.pkg.PackageUserStateImpl(this);
            this.mUserStates.put(userId, state2);
            return state2;
        }
        return state;
    }

    public com.android.server.pm.pkg.PackageUserStateInternal readUserState(int userId) {
        com.android.server.pm.pkg.PackageUserStateInternal state = this.mUserStates.get(userId);
        if (state == null) {
            return com.android.server.pm.pkg.PackageUserStateInternal.DEFAULT;
        }
        return state;
    }

    public void setEnabled(int state, int userId, java.lang.String callingPackage) {
        modifyUserState(userId).setEnabledState(state).setLastDisableAppCaller(callingPackage);
        onChanged();
    }

    int getEnabled(int userId) {
        return readUserState(userId).getEnabledState();
    }

    public void setInstalled(boolean inst, int userId) {
        modifyUserState(userId).setInstalled(inst);
        onChanged();
    }

    void setArchiveState(com.android.server.pm.pkg.ArchiveState archiveState, int userId) {
        modifyUserState(userId).setArchiveState(archiveState);
        onChanged();
    }

    boolean getInstalled(int userId) {
        return readUserState(userId).isInstalled();
    }

    boolean isArchived(int userId) {
        return com.android.server.pm.PackageArchiver.isArchived(readUserState(userId));
    }

    int getInstallReason(int userId) {
        return readUserState(userId).getInstallReason();
    }

    void setInstallReason(int installReason, int userId) {
        modifyUserState(userId).setInstallReason(installReason);
        onChanged();
    }

    int getUninstallReason(int userId) {
        return readUserState(userId).getUninstallReason();
    }

    void setUninstallReason(int uninstallReason, int userId) {
        modifyUserState(userId).setUninstallReason(uninstallReason);
        onChanged();
    }

    android.content.pm.overlay.OverlayPaths getOverlayPaths(int userId) {
        return readUserState(userId).getOverlayPaths();
    }

    boolean setOverlayPathsForLibrary(java.lang.String libName, android.content.pm.overlay.OverlayPaths overlayPaths, int userId) {
        boolean changed = modifyUserState(userId).setSharedLibraryOverlayPaths(libName, overlayPaths);
        onChanged();
        return changed;
    }

    boolean isInstalledOnAnyOtherUser(int[] allUsers, int currentUser) {
        for (int user : allUsers) {
            if (user != currentUser) {
                com.android.server.pm.pkg.PackageUserStateInternal userState = readUserState(user);
                if (userState.isInstalled()) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean hasDataOnAnyOtherUser(int[] allUsers, int currentUser) {
        for (int user : allUsers) {
            if (user != currentUser) {
                com.android.server.pm.pkg.PackageUserStateInternal userState = readUserState(user);
                if (userState.dataExists()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[] queryInstalledUsers(int[] users, boolean installed) {
        int num = 0;
        for (int i : users) {
            if (getInstalled(i) == installed) {
                num++;
            }
        }
        int[] res = new int[num];
        int num2 = 0;
        for (int user : users) {
            if (getInstalled(user) == installed) {
                res[num2] = user;
                num2++;
            }
        }
        return res;
    }

    int[] queryUsersInstalledOrHasData(int[] users) {
        int num = 0;
        for (int user : users) {
            if (getInstalled(user) || readUserState(user).dataExists()) {
                num++;
            }
        }
        int[] res = new int[num];
        int num2 = 0;
        for (int user2 : users) {
            if (getInstalled(user2) || readUserState(user2).dataExists()) {
                res[num2] = user2;
                num2++;
            }
        }
        return res;
    }

    long getCeDataInode(int userId) {
        return readUserState(userId).getCeDataInode();
    }

    long getDeDataInode(int userId) {
        return readUserState(userId).getDeDataInode();
    }

    void setCeDataInode(long ceDataInode, int userId) {
        modifyUserState(userId).setCeDataInode(ceDataInode);
        onChanged();
    }

    void setDeDataInode(long deDataInode, int userId) {
        modifyUserState(userId).setDeDataInode(deDataInode);
        onChanged();
    }

    boolean getStopped(int userId) {
        return readUserState(userId).isStopped();
    }

    void setStopped(boolean stop, int userId) {
        modifyUserState(userId).setStopped(stop);
        onChanged();
    }

    public com.android.server.pm.PackageSetting setScannedAsStoppedSystemApp(boolean stop) {
        setBoolean(8, stop);
        onChanged();
        return this;
    }

    boolean getNotLaunched(int userId) {
        return readUserState(userId).isNotLaunched();
    }

    void setNotLaunched(boolean stop, int userId) {
        modifyUserState(userId).setNotLaunched(stop);
        onChanged();
    }

    boolean getHidden(int userId) {
        return readUserState(userId).isHidden();
    }

    void setHidden(boolean hidden, int userId) {
        modifyUserState(userId).setHidden(hidden);
        onChanged();
    }

    int getDistractionFlags(int userId) {
        return readUserState(userId).getDistractionFlags();
    }

    void setDistractionFlags(int distractionFlags, int userId) {
        modifyUserState(userId).setDistractionFlags(distractionFlags);
        onChanged();
    }

    public boolean getInstantApp(int userId) {
        return readUserState(userId).isInstantApp();
    }

    void setInstantApp(boolean instantApp, int userId) {
        modifyUserState(userId).setInstantApp(instantApp);
        onChanged();
    }

    boolean getVirtualPreload(int userId) {
        return readUserState(userId).isVirtualPreload();
    }

    void setVirtualPreload(boolean virtualPreload, int userId) {
        modifyUserState(userId).setVirtualPreload(virtualPreload);
        onChanged();
    }

    void setUserState(int userId, long ceDataInode, long deDataInode, int enabled, boolean installed, boolean stopped, boolean notLaunched, boolean hidden, int distractionFlags, android.util.ArrayMap<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> suspendParams, boolean instantApp, boolean virtualPreload, java.lang.String lastDisableAppCaller, android.util.ArraySet<java.lang.String> enabledComponents, android.util.ArraySet<java.lang.String> disabledComponents, int installReason, int uninstallReason, java.lang.String harmfulAppWarning, java.lang.String splashScreenTheme, long firstInstallTime, int aspectRatio, com.android.server.pm.pkg.ArchiveState archiveState) {
        modifyUserState(userId).setSuspendParams(suspendParams).setCeDataInode(ceDataInode).setDeDataInode(deDataInode).setEnabledState(enabled).setInstalled(installed).setStopped(stopped).setNotLaunched(notLaunched).setHidden(hidden).setDistractionFlags(distractionFlags).setLastDisableAppCaller(lastDisableAppCaller).setEnabledComponents(enabledComponents).setDisabledComponents(disabledComponents).setInstallReason(installReason).setUninstallReason(uninstallReason).setInstantApp(instantApp).setVirtualPreload(virtualPreload).setHarmfulAppWarning(harmfulAppWarning).setSplashScreenTheme(splashScreenTheme).setFirstInstallTimeMillis(firstInstallTime).setMinAspectRatio(aspectRatio).setArchiveState(archiveState);
        onChanged();
    }

    void setUserState(int userId, com.android.server.pm.pkg.PackageUserStateInternal otherState) {
        setUserState(userId, otherState.getCeDataInode(), otherState.getDeDataInode(), otherState.getEnabledState(), otherState.isInstalled(), otherState.isStopped(), otherState.isNotLaunched(), otherState.isHidden(), otherState.getDistractionFlags(), otherState.getSuspendParams() == null ? null : otherState.getSuspendParams().untrackedStorage(), otherState.isInstantApp(), otherState.isVirtualPreload(), otherState.getLastDisableAppCaller(), otherState.getEnabledComponentsNoCopy() == null ? null : otherState.getEnabledComponentsNoCopy().untrackedStorage(), otherState.getDisabledComponentsNoCopy() == null ? null : otherState.getDisabledComponentsNoCopy().untrackedStorage(), otherState.getInstallReason(), otherState.getUninstallReason(), otherState.getHarmfulAppWarning(), otherState.getSplashScreenTheme(), otherState.getFirstInstallTimeMillis(), otherState.getMinAspectRatio(), otherState.getArchiveState());
    }

    com.android.server.utils.WatchedArraySet<java.lang.String> getEnabledComponents(int userId) {
        return readUserState(userId).getEnabledComponentsNoCopy();
    }

    com.android.server.utils.WatchedArraySet<java.lang.String> getDisabledComponents(int userId) {
        return readUserState(userId).getDisabledComponentsNoCopy();
    }

    void setEnabledComponents(com.android.server.utils.WatchedArraySet<java.lang.String> components, int userId) {
        modifyUserState(userId).setEnabledComponents(components);
        onChanged();
    }

    void setDisabledComponents(com.android.server.utils.WatchedArraySet<java.lang.String> components, int userId) {
        modifyUserState(userId).setDisabledComponents(components);
        onChanged();
    }

    void setEnabledComponentsCopy(com.android.server.utils.WatchedArraySet<java.lang.String> components, int userId) {
        modifyUserState(userId).setEnabledComponents(components != null ? components.untrackedStorage() : null);
        onChanged();
    }

    void setDisabledComponentsCopy(com.android.server.utils.WatchedArraySet<java.lang.String> components, int userId) {
        modifyUserState(userId).setDisabledComponents(components != null ? components.untrackedStorage() : null);
        onChanged();
    }

    com.android.server.pm.pkg.PackageUserStateImpl modifyUserStateComponents(int userId, boolean disabled, boolean enabled) {
        com.android.server.pm.pkg.PackageUserStateImpl state = modifyUserState(userId);
        boolean changed = false;
        if (disabled && state.getDisabledComponentsNoCopy() == null) {
            state.setDisabledComponents(new android.util.ArraySet<>(1));
            changed = true;
        }
        if (enabled && state.getEnabledComponentsNoCopy() == null) {
            state.setEnabledComponents(new android.util.ArraySet<>(1));
            changed = true;
        }
        if (changed) {
            onChanged();
        }
        return state;
    }

    void addDisabledComponent(java.lang.String componentClassName, int userId) {
        modifyUserStateComponents(userId, true, false).getDisabledComponentsNoCopy().add(componentClassName);
        onChanged();
    }

    void addEnabledComponent(java.lang.String componentClassName, int userId) {
        modifyUserStateComponents(userId, false, true).getEnabledComponentsNoCopy().add(componentClassName);
        onChanged();
    }

    boolean enableComponentLPw(java.lang.String componentClassName, int userId) {
        com.android.server.pm.pkg.PackageUserStateImpl state = modifyUserStateComponents(userId, false, true);
        boolean changed = (state.getDisabledComponentsNoCopy() != null ? state.getDisabledComponentsNoCopy().remove(componentClassName) : false) | state.getEnabledComponentsNoCopy().add(componentClassName);
        if (changed) {
            onChanged();
        }
        return changed;
    }

    boolean disableComponentLPw(java.lang.String componentClassName, int userId) {
        com.android.server.pm.pkg.PackageUserStateImpl state = modifyUserStateComponents(userId, true, false);
        boolean changed = (state.getEnabledComponentsNoCopy() != null ? state.getEnabledComponentsNoCopy().remove(componentClassName) : false) | state.getDisabledComponentsNoCopy().add(componentClassName);
        if (changed) {
            onChanged();
        }
        return changed;
    }

    boolean restoreComponentLPw(java.lang.String componentClassName, int userId) {
        com.android.server.pm.pkg.PackageUserStateImpl state = modifyUserStateComponents(userId, true, true);
        boolean changed = (state.getDisabledComponentsNoCopy() != null ? state.getDisabledComponentsNoCopy().remove(componentClassName) : false) | (state.getEnabledComponentsNoCopy() != null ? state.getEnabledComponentsNoCopy().remove(componentClassName) : false);
        if (changed) {
            onChanged();
        }
        return changed;
    }

    void restoreComponentSettings(int userId) {
        com.android.server.pm.pkg.PackageUserStateImpl state = modifyUserStateComponents(userId, true, true);
        com.android.server.utils.WatchedArraySet<java.lang.String> enabledComponents = state.getEnabledComponentsNoCopy();
        com.android.server.utils.WatchedArraySet<java.lang.String> disabledComponents = state.getDisabledComponentsNoCopy();
        boolean changed = false;
        for (int i = enabledComponents.size() - 1; i >= 0; i--) {
            if (!com.android.server.pm.parsing.pkg.AndroidPackageUtils.hasComponentClassName(this.pkg, enabledComponents.valueAt(i))) {
                enabledComponents.removeAt(i);
                changed = true;
            }
        }
        int i2 = disabledComponents.size();
        for (int i3 = i2 - 1; i3 >= 0; i3--) {
            if (!com.android.server.pm.parsing.pkg.AndroidPackageUtils.hasComponentClassName(this.pkg, disabledComponents.valueAt(i3))) {
                disabledComponents.removeAt(i3);
                changed = true;
            }
        }
        if (changed) {
            onChanged();
        }
    }

    int getCurrentEnabledStateLPr(java.lang.String componentName, int userId) {
        com.android.server.pm.pkg.PackageUserStateInternal state = readUserState(userId);
        if (state.getEnabledComponentsNoCopy() != null && state.getEnabledComponentsNoCopy().contains(componentName)) {
            return 1;
        }
        if (state.getDisabledComponentsNoCopy() != null && state.getDisabledComponentsNoCopy().contains(componentName)) {
            return 2;
        }
        return 0;
    }

    void removeUser(int userId) {
        this.mUserStates.delete(userId);
        onChanged();
    }

    public int[] getNotInstalledUserIds() {
        int count = 0;
        int userStateCount = this.mUserStates.size();
        for (int i = 0; i < userStateCount; i++) {
            if (!this.mUserStates.valueAt(i).isInstalled()) {
                count++;
            }
        }
        if (count == 0) {
            return libcore.util.EmptyArray.INT;
        }
        int[] excludedUserIds = new int[count];
        int idx = 0;
        for (int i2 = 0; i2 < userStateCount; i2++) {
            if (!this.mUserStates.valueAt(i2).isInstalled()) {
                excludedUserIds[idx] = this.mUserStates.keyAt(i2);
                idx++;
            }
        }
        return excludedUserIds;
    }

    void writePackageUserPermissionsProto(android.util.proto.ProtoOutputStream proto, long fieldId, java.util.List<android.content.pm.UserInfo> users, com.android.server.pm.permission.LegacyPermissionDataProvider dataProvider) {
        for (android.content.pm.UserInfo user : users) {
            long permissionsToken = proto.start(2246267895820L);
            proto.write(1120986464257L, user.id);
            java.util.Collection<com.android.server.pm.permission.LegacyPermissionState.PermissionState> runtimePermissionStates = dataProvider.getLegacyPermissionState(this.mAppId).getPermissionStates(user.id);
            for (com.android.server.pm.permission.LegacyPermissionState.PermissionState permission : runtimePermissionStates) {
                if (permission.isGranted()) {
                    proto.write(2237677961218L, permission.getName());
                }
            }
            proto.end(permissionsToken);
        }
    }

    protected void writeUsersInfoToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        int installType;
        int count = this.mUserStates.size();
        for (int i = 0; i < count; i++) {
            long userToken = proto.start(fieldId);
            int userId = this.mUserStates.keyAt(i);
            com.android.server.pm.pkg.PackageUserStateInternal state = this.mUserStates.valueAt(i);
            proto.write(1120986464257L, userId);
            if (state.isInstantApp()) {
                installType = 2;
            } else if (state.isInstalled()) {
                installType = 1;
            } else {
                installType = 0;
            }
            proto.write(1159641169922L, installType);
            proto.write(1133871366147L, state.isHidden());
            proto.write(1120986464266L, state.getDistractionFlags());
            proto.write(1133871366148L, state.isSuspended());
            if (state.isSuspended()) {
                for (int j = 0; j < state.getSuspendParams().size(); j++) {
                    proto.write(2237677961225L, state.getSuspendParams().keyAt(j).packageName);
                    if (android.app.admin.flags.Flags.crossUserSuspensionEnabledRo()) {
                        proto.write(2220498092045L, state.getSuspendParams().keyAt(j).userId);
                    }
                }
            }
            proto.write(1133871366149L, state.isStopped());
            proto.write(1133871366150L, !state.isNotLaunched());
            proto.write(1159641169927L, state.getEnabledState());
            proto.write(1138166333448L, state.getLastDisableAppCaller());
            proto.write(1120986464267L, state.getFirstInstallTimeMillis());
            writeArchiveState(proto, state.getArchiveState());
            proto.end(userToken);
        }
    }

    private static void writeArchiveState(android.util.proto.ProtoOutputStream proto, com.android.server.pm.pkg.ArchiveState archiveState) {
        if (archiveState == null) {
            return;
        }
        long archiveStateToken = proto.start(1146756268044L);
        for (com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo activityInfo : archiveState.getActivityInfos()) {
            long activityInfoToken = proto.start(2246267895809L);
            proto.write(1138166333441L, activityInfo.getTitle());
            proto.write(1138166333444L, activityInfo.getOriginalComponentName().flattenToString());
            if (activityInfo.getIconBitmap() != null) {
                proto.write(1138166333442L, activityInfo.getIconBitmap().toAbsolutePath().toString());
            }
            if (activityInfo.getMonochromeIconBitmap() != null) {
                proto.write(1138166333443L, activityInfo.getMonochromeIconBitmap().toAbsolutePath().toString());
            }
            proto.end(activityInfoToken);
        }
        proto.write(1138166333442L, archiveState.getInstallerTitle());
        proto.end(archiveStateToken);
    }

    public com.android.server.pm.PackageSetting setPath(java.io.File path) {
        this.mPath = path;
        this.mPathString = path.toString();
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting addOldPath(java.io.File path) {
        if (this.mOldPaths == null) {
            this.mOldPaths = new java.util.LinkedHashSet<>();
        }
        this.mOldPaths.add(path);
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting removeOldPath(java.io.File path) {
        if (path != null && this.mOldPaths != null && this.mOldPaths.remove(path)) {
            onChanged();
        }
        return this;
    }

    public boolean overrideNonLocalizedLabelAndIcon(android.content.ComponentName component, java.lang.String label, java.lang.Integer icon, int userId) {
        boolean changed = modifyUserState(userId).overrideLabelAndIcon(component, label, icon);
        onChanged();
        return changed;
    }

    public void resetOverrideComponentLabelIcon(int userId) {
        modifyUserState(userId).resetOverrideComponentLabelIcon();
        onChanged();
    }

    public java.lang.String getSplashScreenTheme(int userId) {
        return readUserState(userId).getSplashScreenTheme();
    }

    public boolean isIncremental() {
        return android.os.incremental.IncrementalManager.isIncrementalPath(this.mPathString);
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public boolean isLoading() {
        return java.lang.Math.abs(1.0f - this.mLoadingProgress) >= 1.0E-8f;
    }

    public com.android.server.pm.PackageSetting setLoadingProgress(float progress) {
        if (this.mLoadingProgress < progress) {
            this.mLoadingProgress = progress;
            onChanged();
        }
        return this;
    }

    public com.android.server.pm.PackageSetting setLoadingCompletedTime(long loadingCompletedTime) {
        this.mLoadingCompletedTime = loadingCompletedTime;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setAppMetadataFilePath(java.lang.String path) {
        this.mAppMetadataFilePath = path;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setAppMetadataSource(int source) {
        this.mAppMetadataSource = source;
        onChanged();
        return this;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long getVersionCode() {
        return this.versionCode;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getMimeGroups() {
        return com.android.internal.util.CollectionUtils.isEmpty(this.mimeGroups) ? java.util.Collections.emptyMap() : java.util.Collections.unmodifiableMap(this.mimeGroups);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.lang.String getPackageName() {
        return this.mName;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public com.android.server.pm.pkg.AndroidPackage getAndroidPackage() {
        return getPkg();
    }

    @Override // com.android.server.pm.pkg.PackageState
    public android.content.pm.SigningInfo getSigningInfo() {
        return new android.content.pm.SigningInfo(this.signatures.mSigningDetails);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.lang.String[] getUsesSdkLibraries() {
        return this.usesSdkLibraries == null ? libcore.util.EmptyArray.STRING : this.usesSdkLibraries;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long[] getUsesSdkLibrariesVersionsMajor() {
        return this.usesSdkLibrariesVersionsMajor == null ? libcore.util.EmptyArray.LONG : this.usesSdkLibrariesVersionsMajor;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean[] getUsesSdkLibrariesOptional() {
        return this.usesSdkLibrariesOptional == null ? libcore.util.EmptyArray.BOOLEAN : this.usesSdkLibrariesOptional;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.lang.String[] getUsesStaticLibraries() {
        return this.usesStaticLibraries == null ? libcore.util.EmptyArray.STRING : this.usesStaticLibraries;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long[] getUsesStaticLibrariesVersions() {
        return this.usesStaticLibrariesVersions == null ? libcore.util.EmptyArray.LONG : this.usesStaticLibrariesVersions;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.util.List<com.android.server.pm.pkg.SharedLibrary> getSharedLibraryDependencies() {
        return java.util.Collections.unmodifiableList(this.pkgState.getUsesLibraryInfos());
    }

    public com.android.server.pm.PackageSetting addUsesLibraryInfo(android.content.pm.SharedLibraryInfo value) {
        this.pkgState.addUsesLibraryInfo(new com.android.server.pm.pkg.SharedLibraryWrapper(value));
        return this;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.util.List<java.lang.String> getUsesLibraryFiles() {
        return java.util.Collections.unmodifiableList(this.pkgState.getUsesLibraryFiles());
    }

    public com.android.server.pm.PackageSetting addUsesLibraryFile(java.lang.String value) {
        this.pkgState.addUsesLibraryFile(value);
        return this;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isHiddenUntilInstalled() {
        return this.pkgState.isHiddenUntilInstalled();
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long[] getLastPackageUsageTime() {
        return this.pkgState.getLastPackageUsageTimeInMills();
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isUpdatedSystemApp() {
        return this.pkgState.isUpdatedSystemApp();
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isApkInUpdatedApex() {
        return this.pkgState.isApkInUpdatedApex();
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.lang.String getApexModuleName() {
        return this.pkgState.getApexModuleName();
    }

    public com.android.server.pm.PackageSetting setDomainSetId(java.util.UUID domainSetId) {
        this.mDomainSetId = domainSetId;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setCategoryOverride(int categoryHint) {
        this.categoryOverride = categoryHint;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setLegacyNativeLibraryPath(java.lang.String legacyNativeLibraryPathString) {
        this.legacyNativeLibraryPath = legacyNativeLibraryPathString;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setMimeGroups(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> mimeGroups) {
        if (mimeGroups != null) {
            copyMimeGroups(mimeGroups);
            onChanged();
        }
        return this;
    }

    public com.android.server.pm.PackageSetting setUsesSdkLibraries(java.lang.String[] usesSdkLibraries) {
        this.usesSdkLibraries = usesSdkLibraries;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setUsesSdkLibrariesVersionsMajor(long[] usesSdkLibrariesVersions) {
        this.usesSdkLibrariesVersionsMajor = usesSdkLibrariesVersions;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setUsesSdkLibrariesOptional(boolean[] usesSdkLibrariesOptional) {
        this.usesSdkLibrariesOptional = usesSdkLibrariesOptional;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setUsesStaticLibraries(java.lang.String[] usesStaticLibraries) {
        this.usesStaticLibraries = usesStaticLibraries;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setUsesStaticLibrariesVersions(long[] usesStaticLibrariesVersions) {
        this.usesStaticLibrariesVersions = usesStaticLibrariesVersions;
        onChanged();
        return this;
    }

    public com.android.server.pm.PackageSetting setApexModuleName(java.lang.String apexModuleName) {
        this.pkgState.setApexModuleName(apexModuleName);
        return this;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public com.android.server.pm.pkg.PackageStateUnserialized getTransientState() {
        return this.pkgState;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal, com.android.server.pm.pkg.PackageState
    public android.util.SparseArray<? extends com.android.server.pm.pkg.PackageUserStateInternal> getUserStates() {
        return this.mUserStates;
    }

    public com.android.server.pm.PackageSetting addMimeTypes(java.lang.String mimeGroup, java.util.Set<java.lang.String> mimeTypes) {
        if (this.mimeGroups == null) {
            this.mimeGroups = new android.util.ArrayMap();
        }
        java.util.Set<java.lang.String> existingMimeTypes = this.mimeGroups.get(mimeGroup);
        if (existingMimeTypes == null) {
            existingMimeTypes = new android.util.ArraySet();
            this.mimeGroups.put(mimeGroup, existingMimeTypes);
        }
        existingMimeTypes.addAll(mimeTypes);
        return this;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public com.android.server.pm.pkg.PackageUserState getStateForUser(android.os.UserHandle user) {
        com.android.server.pm.pkg.PackageUserState userState = getUserStates().get(user.getIdentifier());
        return userState == null ? com.android.server.pm.pkg.PackageUserState.DEFAULT : userState;
    }

    void setOplusFreezeState(int freezeState, int userId) {
        com.android.server.pm.pkg.PackageUserStateImpl st = modifyUserState(userId);
        st.mPackageUserStateExt.setFreezeState(freezeState);
    }

    int getOplusFreezeState(int userId) {
        com.android.server.pm.pkg.PackageUserStateInternal st = readUserState(userId);
        if (st instanceof com.android.server.pm.pkg.PackageUserStateImpl) {
            return ((com.android.server.pm.pkg.PackageUserStateImpl) st).mPackageUserStateExt.getFreezeState();
        }
        return 0;
    }

    void setOplusFreezeFlag(int freezeFlag, int userId) {
        com.android.server.pm.pkg.PackageUserStateImpl st = modifyUserState(userId);
        st.mPackageUserStateExt.setFreezeFlag(freezeFlag);
    }

    int getOplusFreezeFlag(int userId) {
        com.android.server.pm.pkg.PackageUserStateInternal st = readUserState(userId);
        if (st instanceof com.android.server.pm.pkg.PackageUserStateImpl) {
            return ((com.android.server.pm.pkg.PackageUserStateImpl) st).mPackageUserStateExt.getFreezeFlag();
        }
        return 0;
    }

    void setPendingMig(boolean state, int userId) {
        com.android.server.pm.pkg.PackageUserStateImpl st = modifyUserState(userId);
        st.mPackageUserStateExt.setPendingDataMig(state);
    }

    boolean getPendingMig(int userId) {
        com.android.server.pm.pkg.PackageUserStateInternal st = readUserState(userId);
        if (st instanceof com.android.server.pm.pkg.PackageUserStateImpl) {
            return ((com.android.server.pm.pkg.PackageUserStateImpl) st).mPackageUserStateExt.isPendingDataMig();
        }
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.lang.String getPrimaryCpuAbi() {
        if (android.text.TextUtils.isEmpty(this.mPrimaryCpuAbi) && this.pkg != null) {
            return com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawPrimaryCpuAbi(this.pkg);
        }
        return this.mPrimaryCpuAbi;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.lang.String getSecondaryCpuAbi() {
        if (android.text.TextUtils.isEmpty(this.mSecondaryCpuAbi) && this.pkg != null) {
            return com.android.server.pm.parsing.pkg.AndroidPackageUtils.getRawSecondaryCpuAbi(this.pkg);
        }
        return this.mSecondaryCpuAbi;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.lang.String getSeInfo() {
        java.lang.String overrideSeInfo = getTransientState().getOverrideSeInfo();
        if (!android.text.TextUtils.isEmpty(overrideSeInfo)) {
            return overrideSeInfo;
        }
        return getTransientState().getSeInfo();
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public java.lang.String getPrimaryCpuAbiLegacy() {
        return this.mPrimaryCpuAbi;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public java.lang.String getSecondaryCpuAbiLegacy() {
        return this.mSecondaryCpuAbi;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public int getHiddenApiEnforcementPolicy() {
        return com.android.server.pm.parsing.pkg.AndroidPackageUtils.getHiddenApiEnforcementPolicy(getAndroidPackage(), this);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isApex() {
        return getAndroidPackage() != null && getAndroidPackage().isApex();
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isForceQueryableOverride() {
        return getBoolean(4);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isUpdateAvailable() {
        return getBoolean(2);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isInstallPermissionsFixed() {
        return getBoolean(1);
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isDefaultToDeviceProtectedStorage() {
        return (getPrivateFlags() & 32) != 0;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isPersistent() {
        return (getFlags() & 8) != 0;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public boolean isScannedAsStoppedSystemApp() {
        return getBoolean(8);
    }

    @java.lang.Deprecated
    public java.lang.String getLegacyNativeLibraryPath() {
        return this.legacyNativeLibraryPath;
    }

    public java.lang.String getName() {
        return this.mName;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public java.lang.String getRealName() {
        return this.mRealName;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public int getAppId() {
        return this.mAppId;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public com.android.internal.pm.parsing.pkg.AndroidPackageInternal getPkg() {
        return this.pkg;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.io.File getPath() {
        return this.mPath;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public java.lang.String getPathString() {
        return this.mPathString;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public java.util.LinkedHashSet<java.io.File> getOldPaths() {
        return this.mOldPaths;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public float getLoadingProgress() {
        return this.mLoadingProgress;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public long getLoadingCompletedTime() {
        return this.mLoadingCompletedTime;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.lang.String getCpuAbiOverride() {
        return this.mCpuAbiOverride;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long getLastModifiedTime() {
        return this.mLastModifiedTime;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public com.android.server.pm.PackageSignatures getSignatures() {
        return this.signatures;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public com.android.server.pm.PackageKeySetData getKeySetData() {
        return this.keySetData;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public com.android.server.pm.InstallSource getInstallSource() {
        return this.installSource;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public java.lang.String getVolumeUuid() {
        return this.volumeUuid;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public int getCategoryOverride() {
        return this.categoryOverride;
    }

    public com.android.server.pm.pkg.PackageStateUnserialized getPkgState() {
        return this.pkgState;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public java.util.UUID getDomainSetId() {
        return this.mDomainSetId;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public java.lang.String getAppMetadataFilePath() {
        return this.mAppMetadataFilePath;
    }

    @Override // com.android.server.pm.pkg.PackageStateInternal
    public int getAppMetadataSource() {
        return this.mAppMetadataSource;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public int getTargetSdkVersion() {
        return this.mTargetSdkVersion;
    }

    @Override // com.android.server.pm.pkg.PackageState
    public byte[] getRestrictUpdateHash() {
        return this.mRestrictUpdateHash;
    }

    @java.lang.Deprecated
    private void __metadata() {
    }
}
