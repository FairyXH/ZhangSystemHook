package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IInstallParamsExt {
    default void init(android.content.pm.ISessionParamsExt sessionParamsExt, int installerUid, android.content.pm.PackageInstaller.SessionParams params) {
    }

    default int getInstallerUid() {
        return -1;
    }

    default void setInstallerUid(int uid) {
    }

    default android.content.pm.PackageInstaller.SessionParams getSessionParams() {
        return null;
    }

    default void setSessionParams(android.content.pm.PackageInstaller.SessionParams params) {
    }

    default int getExtraInstallFlags() {
        return 0;
    }

    default java.lang.String getExtraSessionInfo() {
        return null;
    }

    default int setExtraDextopFlags(int flags) {
        return 0;
    }

    default int getExtraDextopFlags() {
        return 0;
    }
}
