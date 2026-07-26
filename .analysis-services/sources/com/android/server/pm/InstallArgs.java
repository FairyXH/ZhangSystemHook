package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class InstallArgs {
    final java.lang.String mAbiOverride;
    final java.util.List<java.lang.String> mAllowlistedRestrictedPermissions;
    final boolean mApplicationEnabledSettingPersistent;
    final int mAutoRevokePermissionsMode;
    java.io.File mCodeFile;
    final int mDataLoaderType;
    final int mDevelopmentInstallFlags;
    final java.lang.String mDexoptCompilerFilter;
    final boolean mForceQueryableOverride;
    com.android.server.pm.IInstallArgsExt mInstallArgsExt;
    final int mInstallFlags;
    final int mInstallReason;
    final int mInstallScenario;
    final com.android.server.pm.InstallSource mInstallSource;
    final java.lang.String[] mInstructionSets;
    final com.android.server.pm.MoveInfo mMoveInfo;
    final android.content.pm.IPackageInstallObserver2 mObserver;
    final com.android.server.pm.OriginInfo mOriginInfo;
    final int mPackageSource;
    final android.util.ArrayMap<java.lang.String, java.lang.Integer> mPermissionStates;
    final android.content.pm.SigningDetails mSigningDetails;
    final int mTraceCookie;
    final java.lang.String mTraceMethod;
    final android.os.UserHandle mUser;
    final java.lang.String mVolumeUuid;

    InstallArgs(com.android.server.pm.OriginInfo originInfo, com.android.server.pm.MoveInfo moveInfo, android.content.pm.IPackageInstallObserver2 observer, int installFlags, int developmentInstallFlags, com.android.server.pm.InstallSource installSource, java.lang.String volumeUuid, android.os.UserHandle user, java.lang.String[] instructionSets, java.lang.String abiOverride, android.util.ArrayMap<java.lang.String, java.lang.Integer> permissionStates, java.util.List<java.lang.String> allowlistedRestrictedPermissions, int autoRevokePermissionsMode, java.lang.String traceMethod, int traceCookie, android.content.pm.SigningDetails signingDetails, int installReason, int installScenario, boolean forceQueryableOverride, int dataLoaderType, int packageSource, boolean applicationEnabledSettingPersistent, java.lang.String dexoptCompilerFilter) {
        this.mOriginInfo = originInfo;
        this.mMoveInfo = moveInfo;
        this.mInstallFlags = installFlags;
        this.mDevelopmentInstallFlags = developmentInstallFlags;
        this.mObserver = observer;
        this.mInstallSource = (com.android.server.pm.InstallSource) com.android.internal.util.Preconditions.checkNotNull(installSource);
        this.mVolumeUuid = volumeUuid;
        this.mUser = user;
        this.mInstructionSets = instructionSets;
        this.mAbiOverride = abiOverride;
        this.mPermissionStates = permissionStates;
        this.mAllowlistedRestrictedPermissions = allowlistedRestrictedPermissions;
        this.mAutoRevokePermissionsMode = autoRevokePermissionsMode;
        this.mTraceMethod = traceMethod;
        this.mTraceCookie = traceCookie;
        this.mSigningDetails = signingDetails;
        this.mInstallReason = installReason;
        this.mInstallScenario = installScenario;
        this.mForceQueryableOverride = forceQueryableOverride;
        this.mDataLoaderType = dataLoaderType;
        this.mPackageSource = packageSource;
        this.mApplicationEnabledSettingPersistent = applicationEnabledSettingPersistent;
        this.mDexoptCompilerFilter = dexoptCompilerFilter;
    }
}
