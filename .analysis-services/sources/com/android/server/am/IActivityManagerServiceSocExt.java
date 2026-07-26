package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IActivityManagerServiceSocExt {
    default void addPidLocked(com.android.server.am.ProcessRecord app) {
    }

    default void removePidLocked(com.android.server.am.ProcessRecord app) {
    }

    default void appDiedLocked(com.android.server.am.ProcessRecord app, int pid) {
    }

    default void perfHint(com.android.server.am.ProcessRecord app, int pid) {
    }

    default void updateForceStopKillFlag() {
    }

    default void compactAllSystem() {
    }

    default boolean delayMessage(android.os.Handler mHandler, android.os.Message msg, int msgId, int time) {
        return false;
    }

    default void addAnrManagerService() {
    }

    default void startAnrManagerService(int pid) {
    }

    default void writeBootCompletedEvent() {
    }

    default boolean isAnrDeferrable() {
        return false;
    }

    default void onAddErrorToDropBox(java.lang.String dropboxTag, java.lang.String info, int pid) {
    }

    default java.lang.Object getAnrManager() {
        return null;
    }

    default java.lang.Object getAmsExt() {
        return null;
    }

    default void onNotifyAppCrash(int pid, int uid, java.lang.String packageName) {
    }
}
