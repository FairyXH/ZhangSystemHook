package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IAppExitInfoTrackerExt {
    default void notifyOplusExitInfo(android.app.ApplicationExitInfo raw, com.android.server.am.ProcessRecord app) {
    }

    default void updateOplusExitInfo(android.app.ApplicationExitInfo info) {
    }

    default void notifyAppExitInfo(android.app.ApplicationExitInfo info) {
    }

    default void removeProcessInfo(com.android.server.am.ProcessRecord app) {
    }

    default java.lang.String updateExitInfoMsg(java.lang.String msg, com.android.server.am.ProcessRecord app) {
        return msg;
    }

    default void removeByUserId(int userId) {
    }

    default void removeByUid(int uid, java.lang.Integer realUid, boolean allUsers) {
    }

    default void notifyAthenaKill(int pid, int uid, int reason, int subReason, java.lang.String description) {
    }

    default android.util.Pair<java.lang.String, android.util.Pair<java.lang.Integer, java.lang.Integer>> removeAthenaKillRecord(int pid, int uid) {
        return null;
    }

    default void updateApplicationExitInfo(android.app.ApplicationExitInfo info, int reason, int subReason, java.lang.String description) {
    }

    default boolean updateKillReasonInfo(android.app.ApplicationExitInfo info, java.lang.Integer status) {
        return false;
    }

    default android.os.Bundle updateAppExitFeature(android.os.Bundle featureBundle) {
        return null;
    }

    default void setThreadSchedPolicy(int tid, java.lang.String tidName, int group) {
    }
}
