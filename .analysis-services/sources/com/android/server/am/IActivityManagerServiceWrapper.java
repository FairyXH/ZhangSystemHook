package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IActivityManagerServiceWrapper {
    default void addServiceToMap(android.util.ArrayMap<java.lang.String, android.os.IBinder> map, java.lang.String name) {
    }

    default java.util.List<android.content.pm.ResolveInfo> collectReceiverComponents(android.content.Intent intent, java.lang.String resolvedType, int callingUid, int callingPid, int[] users, int[] broadcastAllowList) {
        return null;
    }

    default void cleanupDisabledPackageComponentsLocked(java.lang.String packageName, int userId, java.lang.String[] changedClasses) {
    }

    default void trimApplications(boolean forceFullOomAdj, int oomAdjReason) {
    }

    default boolean startUser(int userId, int userStartMode, android.os.IProgressListener unlockListener) {
        return false;
    }

    default int getCurrentUserIdLU() {
        return -1;
    }

    default void removeUriPermissionsForPackage(java.lang.String packageName, int userHandle, boolean persistable, boolean targetOnly) {
    }

    default void removeRecentTasksByPackageName(java.lang.String packageName, int userId) {
    }

    default void killPackageProcessesLocked(java.lang.String packageName, int appId, int userId, int minOomAdj, int reasonCode, int subReason, java.lang.String reason) {
    }

    default boolean forceStopPackageLocked(java.lang.String packageName, int appId, boolean callerWillRestart, boolean purgeCache, boolean doit, boolean evenPersistent, boolean uninstalling, boolean packageStateStopped, int userId, java.lang.String reason) {
        return false;
    }

    default void forceStopPackageLocked(java.lang.String packageName, int userId) {
    }

    default void removeDyingProviderLocked(com.android.server.am.ProcessRecord proc, com.android.server.am.ContentProviderRecord cpr, boolean always) {
    }

    default com.android.server.am.ProcessRecord getTopAppLockedForBroadcast() {
        return null;
    }

    default void dynamicalConfigLog(java.lang.String categoryTypeName, android.app.IApplicationThread thread, boolean on) {
    }

    default boolean isInRestartingServicesList(java.lang.String pkgName, int uid) {
        return false;
    }

    default java.lang.Object getAnrManager() {
        return null;
    }

    default void addBootEvent(java.lang.String bootEvent) {
    }

    default java.lang.Object getAmsExt() {
        return null;
    }

    default boolean isCameraActiveForUid(int uid) {
        return false;
    }

    default com.android.server.am.IActivityManagerServiceExt getExtImpl() {
        return new com.android.server.am.IActivityManagerServiceExt() { // from class: com.android.server.am.IActivityManagerServiceWrapper.1
        };
    }
}
