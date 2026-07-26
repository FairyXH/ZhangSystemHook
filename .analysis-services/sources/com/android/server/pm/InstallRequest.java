package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class InstallRequest {
    private android.apex.ApexInfo mApexInfo;
    private java.lang.String mApexModuleName;
    private int mAppId;
    private android.content.pm.ArchivedPackageParcel mArchivedPackage;
    private boolean mClearCodeCache;
    private int mDexoptStatus;
    private com.android.server.pm.PackageSetting mDisabledPs;
    private java.lang.String mExistingPackageName;
    private int[] mFirstTimeBroadcastInstantUserIds;
    private int[] mFirstTimeBroadcastUserIds;
    private com.android.server.pm.PackageFreezer mFreezer;
    private final boolean mHasAppMetadataFileFromInstaller;
    private com.android.server.pm.InstallArgs mInstallArgs;
    private int mInstallerUidForInstallExisting;
    private int mInternalErrorCode;
    private boolean mIsInstallForUsers;
    private boolean mIsInstallInherit;
    private java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> mLibraryConsumers;
    private java.lang.String mName;
    private int[] mNewUsers;
    private java.lang.String mOrigPackage;
    private java.lang.String mOrigPermission;
    private int[] mOrigUsers;
    private com.android.server.pm.PackageSetting mOriginalPs;
    private android.content.pm.parsing.PackageLite mPackageLite;
    private final com.android.server.pm.PackageMetrics mPackageMetrics;
    private int mParseFlags;
    private com.android.internal.pm.parsing.pkg.ParsedPackage mParsedPackage;
    private com.android.server.pm.pkg.AndroidPackage mPkg;
    private java.lang.Runnable mPostInstallRunnable;
    private android.content.pm.verify.domain.DomainSet mPreVerifiedDomains;
    private com.android.server.pm.PackageRemovedInfo mRemovedInfo;
    private boolean mReplace;
    private final int mRequireUserAction;
    private android.util.SparseArray<java.lang.String> mResponsibleInstallerTitles;
    private int mReturnCode;
    private java.lang.String mReturnMsg;
    private int mScanFlags;
    private com.android.server.pm.ScanResult mScanResult;
    private final int mSessionId;
    private boolean mSystem;
    private int[] mUpdateBroadcastInstantUserIds;
    private int[] mUpdateBroadcastUserIds;
    private final int mUserId;
    private final java.util.ArrayList<java.lang.String> mWarnings;
    private final com.android.server.pm.IInstallRequestWrapper mWrapper;

    InstallRequest(com.android.server.pm.InstallingSession params) {
        this.mAppId = -1;
        this.mFirstTimeBroadcastUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mFirstTimeBroadcastInstantUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mUpdateBroadcastUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mUpdateBroadcastInstantUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mWarnings = new java.util.ArrayList<>();
        this.mInstallerUidForInstallExisting = -1;
        this.mWrapper = new com.android.server.pm.InstallRequest.InstallRequestWrapper();
        this.mUserId = params.getUser().getIdentifier();
        this.mInstallArgs = new com.android.server.pm.InstallArgs(params.mOriginInfo, params.mMoveInfo, params.mObserver, params.mInstallFlags, params.mDevelopmentInstallFlags, params.mInstallSource, params.mVolumeUuid, params.getUser(), null, params.mPackageAbiOverride, params.mPermissionStates, params.mAllowlistedRestrictedPermissions, params.mAutoRevokePermissionsMode, params.mTraceMethod, params.mTraceCookie, params.mSigningDetails, params.mInstallReason, params.mInstallScenario, params.mForceQueryableOverride, params.mDataLoaderType, params.mPackageSource, params.mApplicationEnabledSettingPersistent, params.mDexoptCompilerFilter);
        this.mPackageLite = params.mPackageLite;
        this.mPackageMetrics = new com.android.server.pm.PackageMetrics(this);
        this.mIsInstallInherit = params.mIsInherit;
        this.mSessionId = params.mSessionId;
        this.mRequireUserAction = params.mRequireUserAction;
        this.mPreVerifiedDomains = params.mPreVerifiedDomains;
        this.mHasAppMetadataFileFromInstaller = params.mHasAppMetadataFile;
    }

    InstallRequest(int userId, int returnCode, com.android.server.pm.pkg.AndroidPackage pkg, int[] newUsers, java.lang.Runnable runnable, int appId, int installerUid, boolean isSystem) {
        this.mAppId = -1;
        this.mFirstTimeBroadcastUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mFirstTimeBroadcastInstantUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mUpdateBroadcastUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mUpdateBroadcastInstantUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mWarnings = new java.util.ArrayList<>();
        this.mInstallerUidForInstallExisting = -1;
        this.mWrapper = new com.android.server.pm.InstallRequest.InstallRequestWrapper();
        this.mUserId = userId;
        this.mInstallArgs = null;
        this.mReturnCode = returnCode;
        this.mPkg = pkg;
        this.mNewUsers = newUsers;
        this.mPostInstallRunnable = runnable;
        this.mPackageMetrics = new com.android.server.pm.PackageMetrics(this);
        this.mIsInstallForUsers = true;
        this.mSessionId = -1;
        this.mRequireUserAction = 0;
        this.mAppId = appId;
        this.mInstallerUidForInstallExisting = installerUid;
        this.mSystem = isSystem;
        this.mHasAppMetadataFileFromInstaller = false;
    }

    InstallRequest(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, int parseFlags, int scanFlags, android.os.UserHandle user, com.android.server.pm.ScanResult scanResult, com.android.server.pm.PackageSetting disabledPs) {
        this.mAppId = -1;
        this.mFirstTimeBroadcastUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mFirstTimeBroadcastInstantUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mUpdateBroadcastUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mUpdateBroadcastInstantUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mWarnings = new java.util.ArrayList<>();
        this.mInstallerUidForInstallExisting = -1;
        this.mWrapper = new com.android.server.pm.InstallRequest.InstallRequestWrapper();
        if (user != null) {
            this.mUserId = user.getIdentifier();
        } else {
            this.mUserId = 0;
        }
        this.mInstallArgs = null;
        this.mParsedPackage = parsedPackage;
        this.mArchivedPackage = null;
        this.mParseFlags = parseFlags;
        this.mScanFlags = scanFlags;
        this.mScanResult = scanResult;
        this.mPackageMetrics = null;
        this.mSessionId = -1;
        this.mRequireUserAction = 0;
        this.mDisabledPs = disabledPs;
        this.mHasAppMetadataFileFromInstaller = false;
    }

    public java.lang.String getName() {
        return this.mName;
    }

    public java.lang.String getReturnMsg() {
        return this.mReturnMsg;
    }

    public com.android.server.pm.OriginInfo getOriginInfo() {
        if (this.mInstallArgs == null) {
            return null;
        }
        return this.mInstallArgs.mOriginInfo;
    }

    public com.android.server.pm.PackageRemovedInfo getRemovedInfo() {
        return this.mRemovedInfo;
    }

    public java.lang.String getOrigPackage() {
        return this.mOrigPackage;
    }

    public java.lang.String getOrigPermission() {
        return this.mOrigPermission;
    }

    public java.io.File getCodeFile() {
        if (this.mInstallArgs == null) {
            return null;
        }
        return this.mInstallArgs.mCodeFile;
    }

    public java.lang.String getCodePath() {
        if (this.mInstallArgs == null || this.mInstallArgs.mCodeFile == null) {
            return null;
        }
        return this.mInstallArgs.mCodeFile.getAbsolutePath();
    }

    public java.lang.String getAbiOverride() {
        if (this.mInstallArgs == null) {
            return null;
        }
        return this.mInstallArgs.mAbiOverride;
    }

    public int getReturnCode() {
        return this.mReturnCode;
    }

    public int getInternalErrorCode() {
        return this.mInternalErrorCode;
    }

    public android.content.pm.IPackageInstallObserver2 getObserver() {
        if (this.mInstallArgs == null) {
            return null;
        }
        return this.mInstallArgs.mObserver;
    }

    public boolean isInstallMove() {
        return (this.mInstallArgs == null || this.mInstallArgs.mMoveInfo == null) ? false : true;
    }

    public java.lang.String getMoveToUuid() {
        if (this.mInstallArgs == null || this.mInstallArgs.mMoveInfo == null) {
            return null;
        }
        return this.mInstallArgs.mMoveInfo.mToUuid;
    }

    public java.lang.String getMovePackageName() {
        if (this.mInstallArgs == null || this.mInstallArgs.mMoveInfo == null) {
            return null;
        }
        return this.mInstallArgs.mMoveInfo.mPackageName;
    }

    public java.lang.String getMoveFromCodePath() {
        if (this.mInstallArgs == null || this.mInstallArgs.mMoveInfo == null) {
            return null;
        }
        return this.mInstallArgs.mMoveInfo.mFromCodePath;
    }

    public java.io.File getOldCodeFile() {
        if (this.mRemovedInfo == null || this.mRemovedInfo.mArgs == null) {
            return null;
        }
        return this.mRemovedInfo.mArgs.getCodeFile();
    }

    public java.lang.String[] getOldInstructionSet() {
        if (this.mRemovedInfo == null || this.mRemovedInfo.mArgs == null) {
            return null;
        }
        return this.mRemovedInfo.mArgs.getInstructionSets();
    }

    public android.os.UserHandle getUser() {
        return new android.os.UserHandle(this.mUserId);
    }

    public int getUserId() {
        return this.mUserId;
    }

    public int getInstallFlags() {
        if (this.mInstallArgs == null) {
            return 0;
        }
        return this.mInstallArgs.mInstallFlags;
    }

    public int getDevelopmentInstallFlags() {
        if (this.mInstallArgs == null) {
            return 0;
        }
        return this.mInstallArgs.mDevelopmentInstallFlags;
    }

    public int getInstallReason() {
        if (this.mInstallArgs == null) {
            return 0;
        }
        return this.mInstallArgs.mInstallReason;
    }

    public java.lang.String getVolumeUuid() {
        if (this.mInstallArgs == null) {
            return null;
        }
        return this.mInstallArgs.mVolumeUuid;
    }

    public com.android.server.pm.pkg.AndroidPackage getPkg() {
        return this.mPkg;
    }

    public android.content.pm.parsing.PackageLite getPackageLite() {
        return this.mPackageLite;
    }

    public java.lang.String getTraceMethod() {
        if (this.mInstallArgs == null) {
            return null;
        }
        return this.mInstallArgs.mTraceMethod;
    }

    public int getTraceCookie() {
        if (this.mInstallArgs == null) {
            return 0;
        }
        return this.mInstallArgs.mTraceCookie;
    }

    public boolean isUpdate() {
        return (this.mRemovedInfo == null || this.mRemovedInfo.mRemovedPackage == null) ? false : true;
    }

    public boolean isArchived() {
        return com.android.server.pm.PackageInstallerSession.isArchivedInstallation(getInstallFlags());
    }

    public boolean hasAppMetadataFile() {
        return this.mHasAppMetadataFileFromInstaller;
    }

    public java.lang.String getRemovedPackage() {
        if (this.mRemovedInfo != null) {
            return this.mRemovedInfo.mRemovedPackage;
        }
        return null;
    }

    public boolean isInstallExistingForUser() {
        return this.mInstallArgs == null;
    }

    public com.android.server.pm.InstallSource getInstallSource() {
        if (this.mInstallArgs == null) {
            return null;
        }
        return this.mInstallArgs.mInstallSource;
    }

    public java.lang.String getInstallerPackageName() {
        if (this.mInstallArgs == null || this.mInstallArgs.mInstallSource == null) {
            return null;
        }
        return this.mInstallArgs.mInstallSource.mInstallerPackageName;
    }

    public int getInstallerPackageUid() {
        if (this.mInstallArgs != null && this.mInstallArgs.mInstallSource != null) {
            return this.mInstallArgs.mInstallSource.mInstallerPackageUid;
        }
        return this.mInstallerUidForInstallExisting;
    }

    public int getDataLoaderType() {
        if (this.mInstallArgs == null) {
            return 0;
        }
        return this.mInstallArgs.mDataLoaderType;
    }

    public int getSignatureSchemeVersion() {
        if (this.mInstallArgs == null) {
            return 0;
        }
        return this.mInstallArgs.mSigningDetails.getSignatureSchemeVersion();
    }

    public android.content.pm.SigningDetails getSigningDetails() {
        return this.mInstallArgs == null ? android.content.pm.SigningDetails.UNKNOWN : this.mInstallArgs.mSigningDetails;
    }

    public android.net.Uri getOriginUri() {
        if (this.mInstallArgs == null) {
            return null;
        }
        return android.net.Uri.fromFile(this.mInstallArgs.mOriginInfo.mResolvedFile);
    }

    public android.apex.ApexInfo getApexInfo() {
        return this.mApexInfo;
    }

    public java.lang.String getApexModuleName() {
        return this.mApexModuleName;
    }

    public android.util.SparseArray<java.lang.String> getResponsibleInstallerTitles() {
        return this.mResponsibleInstallerTitles;
    }

    public boolean isRollback() {
        return this.mInstallArgs != null && this.mInstallArgs.mInstallReason == 5;
    }

    public int[] getNewUsers() {
        return this.mNewUsers;
    }

    public int[] getOriginUsers() {
        return this.mOrigUsers;
    }

    public int getAppId() {
        return this.mAppId;
    }

    public android.util.ArrayMap<java.lang.String, java.lang.Integer> getPermissionStates() {
        if (this.mInstallArgs == null) {
            return null;
        }
        return this.mInstallArgs.mPermissionStates;
    }

    public java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> getLibraryConsumers() {
        return this.mLibraryConsumers;
    }

    public java.util.List<java.lang.String> getAllowlistedRestrictedPermissions() {
        if (this.mInstallArgs == null) {
            return null;
        }
        return this.mInstallArgs.mAllowlistedRestrictedPermissions;
    }

    public int getAutoRevokePermissionsMode() {
        if (this.mInstallArgs == null) {
            return 3;
        }
        return this.mInstallArgs.mAutoRevokePermissionsMode;
    }

    public int getPackageSource() {
        if (this.mInstallArgs == null) {
            return 0;
        }
        return this.mInstallArgs.mPackageSource;
    }

    public int getInstallScenario() {
        if (this.mInstallArgs == null) {
            return 0;
        }
        return this.mInstallArgs.mInstallScenario;
    }

    public com.android.internal.pm.parsing.pkg.ParsedPackage getParsedPackage() {
        return this.mParsedPackage;
    }

    public android.content.pm.ArchivedPackageParcel getArchivedPackage() {
        return this.mArchivedPackage;
    }

    public int getParseFlags() {
        return this.mParseFlags;
    }

    public int getScanFlags() {
        return this.mScanFlags;
    }

    public java.lang.String getExistingPackageName() {
        return this.mExistingPackageName;
    }

    public com.android.server.pm.pkg.AndroidPackage getScanRequestOldPackage() {
        assertScanResultExists();
        return this.mScanResult.mRequest.mOldPkg;
    }

    public boolean isClearCodeCache() {
        return this.mClearCodeCache;
    }

    public boolean isInstallReplace() {
        return this.mReplace;
    }

    public boolean isInstallSystem() {
        return this.mSystem;
    }

    public boolean isInstallInherit() {
        return this.mIsInstallInherit;
    }

    public boolean isInstallForUsers() {
        return this.mIsInstallForUsers;
    }

    public boolean isInstallFromAdb() {
        return (this.mInstallArgs == null || (this.mInstallArgs.mInstallFlags & 32) == 0) ? false : true;
    }

    public com.android.server.pm.PackageSetting getOriginalPackageSetting() {
        return this.mOriginalPs;
    }

    public com.android.server.pm.PackageSetting getDisabledPackageSetting() {
        return this.mDisabledPs;
    }

    public com.android.server.pm.PackageSetting getScanRequestOldPackageSetting() {
        assertScanResultExists();
        return this.mScanResult.mRequest.mOldPkgSetting;
    }

    public com.android.server.pm.PackageSetting getScanRequestOriginalPackageSetting() {
        assertScanResultExists();
        return this.mScanResult.mRequest.mOriginalPkgSetting;
    }

    public com.android.server.pm.PackageSetting getScanRequestPackageSetting() {
        assertScanResultExists();
        return this.mScanResult.mRequest.mPkgSetting;
    }

    public com.android.server.pm.PackageSetting getScanRequestDisabledPackageSetting() {
        assertScanResultExists();
        return this.mScanResult.mRequest.mDisabledPkgSetting;
    }

    public java.lang.String getRealPackageName() {
        assertScanResultExists();
        return this.mScanResult.mRequest.mRealPkgName;
    }

    public java.util.List<java.lang.String> getChangedAbiCodePath() {
        assertScanResultExists();
        return this.mScanResult.mChangedAbiCodePath;
    }

    public boolean isApplicationEnabledSettingPersistent() {
        if (this.mInstallArgs == null) {
            return false;
        }
        return this.mInstallArgs.mApplicationEnabledSettingPersistent;
    }

    public boolean isForceQueryableOverride() {
        return this.mInstallArgs != null && this.mInstallArgs.mForceQueryableOverride;
    }

    public android.content.pm.SharedLibraryInfo getSdkSharedLibraryInfo() {
        assertScanResultExists();
        return this.mScanResult.mSdkSharedLibraryInfo;
    }

    public android.content.pm.SharedLibraryInfo getStaticSharedLibraryInfo() {
        assertScanResultExists();
        return this.mScanResult.mStaticSharedLibraryInfo;
    }

    public java.util.List<android.content.pm.SharedLibraryInfo> getDynamicSharedLibraryInfos() {
        assertScanResultExists();
        return this.mScanResult.mDynamicSharedLibraryInfos;
    }

    public com.android.server.pm.PackageSetting getScannedPackageSetting() {
        assertScanResultExists();
        return this.mScanResult.mPkgSetting;
    }

    public com.android.server.pm.PackageSetting getRealPackageSetting() {
        com.android.server.pm.PackageSetting realPkgSetting = isExistingSettingCopied() ? getScanRequestPackageSetting() : getScannedPackageSetting();
        if (realPkgSetting == null) {
            return getScannedPackageSetting();
        }
        return realPkgSetting;
    }

    public boolean isExistingSettingCopied() {
        assertScanResultExists();
        return this.mScanResult.mExistingSettingCopied;
    }

    public boolean needsNewAppId() {
        assertScanResultExists();
        return this.mScanResult.mPreviousAppId != -1;
    }

    public int getPreviousAppId() {
        assertScanResultExists();
        return this.mScanResult.mPreviousAppId;
    }

    public boolean isPlatformPackage() {
        assertScanResultExists();
        return this.mScanResult.mRequest.mIsPlatformPackage;
    }

    public boolean isInstantInstall() {
        return (this.mScanFlags & 8192) != 0;
    }

    public void assertScanResultExists() {
        if (this.mScanResult == null) {
            if (android.os.Build.IS_USERDEBUG || android.os.Build.IS_ENG) {
                throw new java.lang.IllegalStateException("ScanResult cannot be null.");
            }
            android.util.Slog.e("PackageManager", "ScanResult is null and it should not happen");
        }
    }

    public int getSessionId() {
        return this.mSessionId;
    }

    public int getRequireUserAction() {
        return this.mRequireUserAction;
    }

    public int getDexoptStatus() {
        return this.mDexoptStatus;
    }

    public boolean isAllNewUsers() {
        return this.mOrigUsers == null || this.mOrigUsers.length == 0;
    }

    public int[] getFirstTimeBroadcastUserIds() {
        return this.mFirstTimeBroadcastUserIds;
    }

    public int[] getFirstTimeBroadcastInstantUserIds() {
        return this.mFirstTimeBroadcastInstantUserIds;
    }

    public int[] getUpdateBroadcastUserIds() {
        return this.mUpdateBroadcastUserIds;
    }

    public int[] getUpdateBroadcastInstantUserIds() {
        return this.mUpdateBroadcastInstantUserIds;
    }

    public java.util.ArrayList<java.lang.String> getWarnings() {
        return this.mWarnings;
    }

    public java.lang.String getDexoptCompilerFilter() {
        if (this.mInstallArgs != null) {
            return this.mInstallArgs.mDexoptCompilerFilter;
        }
        return null;
    }

    public void setScanFlags(int scanFlags) {
        this.mScanFlags = scanFlags;
    }

    public void closeFreezer() {
        if (this.mFreezer != null) {
            this.mFreezer.close();
        }
    }

    public void setPostInstallRunnable(java.lang.Runnable runnable) {
        this.mPostInstallRunnable = runnable;
    }

    public boolean hasPostInstallRunnable() {
        return this.mPostInstallRunnable != null;
    }

    public void runPostInstallRunnable() {
        if (this.mPostInstallRunnable != null) {
            this.mPostInstallRunnable.run();
        }
    }

    public void setCodeFile(java.io.File codeFile) {
        if (this.mInstallArgs != null) {
            this.mInstallArgs.mCodeFile = codeFile;
        }
    }

    public void setError(int code, java.lang.String msg) {
        setReturnCode(code);
        setReturnMessage(msg);
        android.util.Slog.w("PackageManager", msg);
        if (this.mPackageMetrics != null) {
            this.mPackageMetrics.onInstallFailed();
        }
    }

    public void setError(com.android.server.pm.PackageManagerException e) {
        setError((java.lang.String) null, e);
    }

    public void setError(java.lang.String msg, com.android.server.pm.PackageManagerException e) {
        this.mInternalErrorCode = e.internalErrorCode;
        this.mReturnCode = e.error;
        setReturnMessage(android.util.ExceptionUtils.getCompleteMessage(msg, e));
        android.util.Slog.w("PackageManager", msg, e);
        if (this.mPackageMetrics != null) {
            this.mPackageMetrics.onInstallFailed();
        }
    }

    public void setReturnCode(int returnCode) {
        this.mReturnCode = returnCode;
    }

    public void setReturnMessage(java.lang.String returnMsg) {
        this.mReturnMsg = returnMsg;
    }

    public void setApexInfo(android.apex.ApexInfo apexInfo) {
        this.mApexInfo = apexInfo;
    }

    public void setApexModuleName(java.lang.String apexModuleName) {
        this.mApexModuleName = apexModuleName;
    }

    public void setResponsibleInstallerTitles(android.util.SparseArray<java.lang.String> responsibleInstallerTitles) {
        this.mResponsibleInstallerTitles = responsibleInstallerTitles;
    }

    public void setPkg(com.android.server.pm.pkg.AndroidPackage pkg) {
        this.mPkg = pkg;
    }

    public void setAppId(int appId) {
        this.mAppId = appId;
    }

    public void setNewUsers(int[] newUsers) {
        this.mNewUsers = newUsers;
        populateBroadcastUsers();
    }

    public void setOriginPackage(java.lang.String originPackage) {
        this.mOrigPackage = originPackage;
    }

    public void setOriginPermission(java.lang.String originPermission) {
        this.mOrigPermission = originPermission;
    }

    public void setName(java.lang.String packageName) {
        this.mName = packageName;
    }

    public void setOriginUsers(int[] userIds) {
        this.mOrigUsers = userIds;
    }

    public void setFreezer(com.android.server.pm.PackageFreezer freezer) {
        this.mFreezer = freezer;
    }

    public void setRemovedInfo(com.android.server.pm.PackageRemovedInfo removedInfo) {
        this.mRemovedInfo = removedInfo;
    }

    public void setLibraryConsumers(java.util.ArrayList<com.android.server.pm.pkg.AndroidPackage> libraryConsumers) {
        this.mLibraryConsumers = libraryConsumers;
    }

    public void setPrepareResult(boolean replace, int scanFlags, int parseFlags, com.android.server.pm.pkg.PackageState existingPackageState, com.android.internal.pm.parsing.pkg.ParsedPackage packageToScan, android.content.pm.ArchivedPackageParcel archivedPackage, boolean clearCodeCache, boolean system2, com.android.server.pm.PackageSetting originalPs, com.android.server.pm.PackageSetting disabledPs) {
        this.mReplace = replace;
        this.mScanFlags = scanFlags;
        this.mParseFlags = parseFlags;
        this.mExistingPackageName = existingPackageState != null ? existingPackageState.getPackageName() : null;
        this.mParsedPackage = packageToScan;
        this.mArchivedPackage = archivedPackage;
        this.mClearCodeCache = clearCodeCache;
        this.mSystem = system2;
        this.mOriginalPs = originalPs;
        this.mDisabledPs = disabledPs;
    }

    public void setScanResult(com.android.server.pm.ScanResult scanResult) {
        this.mScanResult = scanResult;
    }

    public void setScannedPackageSettingAppId(int appId) {
        assertScanResultExists();
        this.mScanResult.mPkgSetting.setAppId(appId);
    }

    public void setScannedPackageSettingFirstInstallTimeFromReplaced(com.android.server.pm.pkg.PackageStateInternal replacedPkgSetting, int[] userId) {
        assertScanResultExists();
        this.mScanResult.mPkgSetting.setFirstInstallTimeFromReplaced(replacedPkgSetting, userId);
    }

    public void setScannedPackageSettingLastUpdateTime(long lastUpdateTim) {
        assertScanResultExists();
        this.mScanResult.mPkgSetting.setLastUpdateTime(lastUpdateTim);
    }

    public void setRemovedAppId(int appId) {
        if (this.mRemovedInfo != null) {
            this.mRemovedInfo.mUid = appId;
            this.mRemovedInfo.mIsAppIdRemoved = true;
        }
    }

    private void populateBroadcastUsers() {
        assertScanResultExists();
        this.mFirstTimeBroadcastUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mFirstTimeBroadcastInstantUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mUpdateBroadcastUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        this.mUpdateBroadcastInstantUserIds = com.android.server.pm.PackageManagerService.EMPTY_INT_ARRAY;
        boolean allNewUsers = isAllNewUsers();
        int i = 0;
        if (allNewUsers) {
            int[] iArr = this.mNewUsers;
            int length = iArr.length;
            while (i < length) {
                int newUser = iArr[i];
                if (this.mScanResult.mPkgSetting.getUserStateOrDefault(newUser).isInstantApp()) {
                    this.mFirstTimeBroadcastInstantUserIds = com.android.internal.util.ArrayUtils.appendInt(this.mFirstTimeBroadcastInstantUserIds, newUser);
                } else {
                    this.mFirstTimeBroadcastUserIds = com.android.internal.util.ArrayUtils.appendInt(this.mFirstTimeBroadcastUserIds, newUser);
                }
                i++;
            }
            return;
        }
        int[] iArr2 = this.mNewUsers;
        int length2 = iArr2.length;
        while (i < length2) {
            int newUser2 = iArr2[i];
            boolean isFirstTimeUser = !com.android.internal.util.ArrayUtils.contains(this.mOrigUsers, newUser2);
            boolean isInstantApp = this.mScanResult.mPkgSetting.getUserStateOrDefault(newUser2).isInstantApp();
            if (isFirstTimeUser) {
                if (isInstantApp) {
                    this.mFirstTimeBroadcastInstantUserIds = com.android.internal.util.ArrayUtils.appendInt(this.mFirstTimeBroadcastInstantUserIds, newUser2);
                } else {
                    this.mFirstTimeBroadcastUserIds = com.android.internal.util.ArrayUtils.appendInt(this.mFirstTimeBroadcastUserIds, newUser2);
                }
            } else if (isInstantApp) {
                this.mUpdateBroadcastInstantUserIds = com.android.internal.util.ArrayUtils.appendInt(this.mUpdateBroadcastInstantUserIds, newUser2);
            } else {
                this.mUpdateBroadcastUserIds = com.android.internal.util.ArrayUtils.appendInt(this.mUpdateBroadcastUserIds, newUser2);
            }
            i++;
        }
    }

    public android.content.pm.verify.domain.DomainSet getPreVerifiedDomains() {
        return this.mPreVerifiedDomains;
    }

    public void addWarning(java.lang.String warning) {
        this.mWarnings.add(warning);
    }

    public void onPrepareStarted() {
        if (this.mPackageMetrics != null) {
            this.mPackageMetrics.onStepStarted(1);
        }
    }

    public void onPrepareFinished() {
        if (this.mPackageMetrics != null) {
            this.mPackageMetrics.onStepFinished(1);
        }
    }

    public void onScanStarted() {
        if (this.mPackageMetrics != null) {
            this.mPackageMetrics.onStepStarted(2);
        }
    }

    public void onScanFinished() {
        if (this.mPackageMetrics != null) {
            this.mPackageMetrics.onStepFinished(2);
        }
    }

    public void onReconcileStarted() {
        if (this.mPackageMetrics != null) {
            this.mPackageMetrics.onStepStarted(3);
        }
    }

    public void onReconcileFinished() {
        if (this.mPackageMetrics != null) {
            this.mPackageMetrics.onStepFinished(3);
        }
    }

    public void onCommitStarted() {
        if (this.mPackageMetrics != null) {
            this.mPackageMetrics.onStepStarted(4);
        }
    }

    public void onCommitFinished() {
        if (this.mPackageMetrics != null) {
            this.mPackageMetrics.onStepFinished(4);
        }
    }

    public void onDexoptFinished(com.android.server.art.model.DexoptResult dexoptResult) {
        if (dexoptResult == null) {
            return;
        }
        if (isInstallFromAdb()) {
            java.util.LinkedHashSet<java.lang.String> externalProfileErrors = new java.util.LinkedHashSet<>();
            for (com.android.server.art.model.DexoptResult.PackageDexoptResult packageResult : dexoptResult.getPackageDexoptResults()) {
                for (com.android.server.art.model.DexoptResult.DexContainerFileDexoptResult fileResult : packageResult.getDexContainerFileDexoptResults()) {
                    externalProfileErrors.addAll(fileResult.getExternalProfileErrors());
                }
            }
            if (!externalProfileErrors.isEmpty()) {
                addWarning("Error occurred during dexopt when processing external profiles:\n  " + java.lang.String.join("\n  ", externalProfileErrors));
            }
        }
        if (this.mPackageMetrics != null) {
            this.mDexoptStatus = dexoptResult.getFinalStatus();
            if (this.mDexoptStatus == 20) {
                long durationMillis = 0;
                for (com.android.server.art.model.DexoptResult.PackageDexoptResult packageResult2 : dexoptResult.getPackageDexoptResults()) {
                    for (com.android.server.art.model.DexoptResult.DexContainerFileDexoptResult fileResult2 : packageResult2.getDexContainerFileDexoptResults()) {
                        durationMillis += fileResult2.getDex2oatWallTimeMillis();
                    }
                }
                this.mPackageMetrics.onStepFinished(5, durationMillis);
            }
        }
    }

    public void onInstallCompleted() {
        if (getReturnCode() == 1 && this.mPackageMetrics != null) {
            this.mPackageMetrics.onInstallSucceed();
        }
    }

    public void onFreezeStarted() {
        if (this.mPackageMetrics != null && com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.improveInstallFreeze()) {
            this.mPackageMetrics.onStepStarted(6);
        }
    }

    public void onFreezeCompleted() {
        if (this.mPackageMetrics != null && com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.improveInstallFreeze()) {
            this.mPackageMetrics.onStepFinished(6);
        }
    }

    public com.android.server.pm.IInstallRequestWrapper getWrapper() {
        return this.mWrapper;
    }

    private class InstallRequestWrapper implements com.android.server.pm.IInstallRequestWrapper {
        private InstallRequestWrapper() {
        }

        @Override // com.android.server.pm.IInstallRequestWrapper
        public com.android.server.pm.InstallArgs getInstallArgs() {
            return com.android.server.pm.InstallRequest.this.mInstallArgs;
        }
    }
}
