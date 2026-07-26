package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageInstallerSessionExt {
    default void adjustAssertShellOrSystemCallingThrowException(com.android.server.pm.PackageManagerService pm, java.lang.String operation) {
    }

    default void beforeSessionCommit(com.android.server.pm.PackageInstallerSession session) {
    }

    default void afterDispatchSessionFinished(com.android.server.pm.PackageInstallerSession session, com.android.server.pm.PackageManagerService pms) {
    }

    default void handleInHandlerCallback(android.os.Message msg, com.android.server.pm.PackageInstallerSession session, com.android.server.pm.PackageManagerService pms) {
    }

    default android.os.ParcelFileDescriptor adjustResultInOpenWrite(android.os.ParcelFileDescriptor pfd, java.lang.String name, android.os.Handler handler, com.android.server.pm.PackageManagerService pms, int installerUid, java.io.File stageDir) throws java.io.IOException {
        return pfd;
    }

    default void afterWrite(java.lang.String name, android.os.Handler handler, com.android.server.pm.PackageManagerService pms, int installerUid, java.io.File stageDir) {
    }

    default void beforeOpenInDoWriteInternal(java.io.File target) {
    }

    default void beforeHandleStreamValidateAndCommit() {
    }

    default void beforeDispatchSessionFinished(com.android.server.pm.PackageInstallerSession session) {
    }

    default android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ApkLite> getPreParseRetInValidateApkInstall(java.io.File targetFile) {
        return null;
    }

    default void beforeCreateOatDirs() {
    }

    default boolean hasPreExtractNativeLibsFinished() {
        return false;
    }

    default boolean adjustUserActionPendingInComputeUserActionRequirement() {
        return true;
    }

    default void checkMainlineLimited(com.android.server.pm.PackageInstallerSession session) throws com.android.server.pm.PackageManagerException {
    }

    default void onStagedSessionVerificationComplete(com.android.server.pm.PackageInstallerSession session, com.android.server.pm.StagingManager.StagedSession stagedSession, com.android.server.pm.PackageManagerService pms) {
    }

    default boolean hasSilentInstallationPermissions(com.android.server.pm.Computer computer, java.lang.String installerPackageName, int originalInstallerUid) {
        return false;
    }

    default void beforeRequestUserPreapprovalAvailable(java.lang.String requiredInstaller) {
    }

    default void recordSotaAppResult(com.android.server.pm.StagingManager.StagedSession session, int result, java.lang.String errMsg) {
    }

    public interface IStaticExt {
        default boolean secureFrpWhiteList(java.lang.String packageName) {
            return false;
        }
    }
}
