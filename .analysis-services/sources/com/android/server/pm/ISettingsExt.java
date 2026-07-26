package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface ISettingsExt {
    default void initPMS(com.android.server.pm.PackageManagerService pms) {
    }

    default boolean adjustInstalledWhenNoFileInRPRLPr(boolean installed, int userId) {
        return installed;
    }

    default void readAndSetCustomPackageAttrInRPRLPr(com.android.modules.utils.TypedXmlPullParser parser, com.android.server.pm.PackageSetting ps, int userId) {
    }

    default void adjustPsAfterReadPackageInRPRLPr(com.android.server.pm.PackageSetting ps, java.lang.String name, int userId, java.lang.String enabledCaller) {
    }

    default boolean isCustomTagInRPRLPr(java.lang.String tagName) {
        return false;
    }

    default void readAndSetCustomTagInRPRLPr(java.lang.String tagName, com.android.modules.utils.TypedXmlPullParser parser, int userId) {
    }

    default void writeCustomPackageAttrInWPRLPr(com.android.server.pm.pkg.PackageUserStateInternal ustate, com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
    }

    default void writeCustomTagInWPRLPr(com.android.modules.utils.TypedXmlSerializer serializer, int userId) throws java.io.IOException {
    }

    default boolean adjustShouldReallyInstallInCreateNewUserLI(boolean shouldReallyInstall, int userHandle, java.lang.String pkgName, boolean allowInstallIgnoreUserType) {
        return shouldReallyInstall;
    }

    default boolean skipCreateAppDataInCreateNewUserLI(boolean shouldReallyInstall) {
        return false;
    }

    default void onRemoveUserLPw(int userId) {
    }

    default void onPrintPackageAttrInDumpPackageLPr(java.io.PrintWriter pw, com.android.server.pm.PackageSetting ps, android.content.pm.UserInfo user) {
    }

    default void setUserPendingMig(int userId, boolean state) {
    }

    default boolean getUserPendingMig(int userId) {
        return false;
    }

    default void adjustInstalledWhenCreateNewUserLI(com.android.server.pm.PackageSetting ps, int userHandle) {
    }

    default boolean fixDisableStateWhenCreateUser(com.android.server.pm.PackageSetting ps, int userId, boolean installed) {
        return false;
    }

    default boolean adjustStrategyBeforeFileSync() {
        return true;
    }

    default void adjustBackupFileDelete(int userid, java.io.File backupFile) {
    }

    default boolean tryFixWhenUserPackagesStateFileNotFound(int userid, java.io.File userPackagesStateFile) {
        return false;
    }

    default void afterCreateFutureInCreateNewUserLI(java.util.concurrent.CompletableFuture<android.os.CreateAppDataResult> future, com.android.server.pm.PackageSetting ps, int userId) {
    }
}
