package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageInstallerServiceExt {
    public static final java.lang.String NONE = "none";

    default boolean skipRemoveInstallAllUsersFlag(int flags) {
        return false;
    }

    default void afterRestoreSession(java.util.List<com.android.server.pm.StagingManager.StagedSession> sessions, android.util.SparseArray<com.android.server.pm.PackageInstallerSession> mSessions, com.android.server.pm.PackageManagerService pms) {
    }

    default void triggerPostBootApexSessionEvent() {
    }

    default int changeUserIdIfNeed(int userId, int callingUid, com.android.server.pm.Computer computer) {
        return userId;
    }

    default boolean canForceAbandonMainlineSession(com.android.server.pm.PackageInstallerSession session) {
        return false;
    }

    default java.lang.String getSotaAppState() {
        return "none";
    }

    default boolean isSotaAppSession(com.android.server.pm.StagingManager.StagedSession session) {
        return false;
    }
}
