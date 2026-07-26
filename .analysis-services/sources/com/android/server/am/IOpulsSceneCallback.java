package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IOpulsSceneCallback {
    default void bootCompleted() {
    }

    default boolean checkActivityIfRestricted(int callingUid, java.lang.String callingPkg, int uid, java.lang.String packageName, android.content.ComponentName componentName) {
        return false;
    }

    default void resumeTopActivityIfNeedLocked(int uid, java.lang.String packageName) {
    }

    default boolean checkStartServiceIfRestricted(int callingPid, int callingUid, java.lang.String callingPkg, int uid, java.lang.String packageName, java.lang.String processName, android.content.ComponentName componentName, java.lang.String action, boolean isBound) {
        return false;
    }

    default boolean checkBumpServiceIfRestricted(int uid, java.lang.String pkgName, java.lang.String why) {
        return false;
    }

    default void checkReStartServiceIfRestricted(int uid, java.lang.String pkgName) {
    }

    default boolean checkProviderIfRestricted(int callingPid, int callingUid, java.lang.String callingPackage, int uid, java.lang.String pkgName, java.lang.String processName, java.lang.String cpnName) {
        return false;
    }

    default boolean checkReceiverIfRestricted(com.android.server.am.BroadcastRecord r, java.lang.Object o) {
        return false;
    }

    default boolean checkSyncIfRestricted(int uid, java.lang.String packageName) {
        return false;
    }

    default boolean checkJobIfRestricted(int uid, java.lang.String packageName, android.app.job.JobInfo jobInfo) {
        return false;
    }

    default boolean checkAlarmIfRestricted(int uid, java.lang.String packageName, java.lang.String action) {
        return false;
    }

    default void noteIsolatedApp(int isolatedUid, int appUid, java.lang.String packageName, boolean add) {
    }

    default void noteWatchdog() {
    }

    default void noteSysShutdown() {
    }

    default void noteSysStateChanged(int type, int state, java.lang.String spare) {
    }
}
