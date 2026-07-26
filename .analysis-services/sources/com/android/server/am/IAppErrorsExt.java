package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IAppErrorsExt {
    default java.lang.String handleAnrAnnotation(com.android.server.am.ProcessRecord app) {
        return "";
    }

    default void doErrorsStatistics(android.content.Context context, com.android.server.am.ProcessRecord r, android.app.ApplicationErrorReport.CrashInfo crashInfo) {
    }

    default boolean isThreadGroupLeader(java.lang.String tag, int pid) {
        return false;
    }

    default boolean isShowDialog() {
        return false;
    }

    default void hookHandleAppCrashBegin(android.content.Context context, com.android.server.am.ProcessRecord app, java.lang.String stackTrace) {
    }

    default void hookHandleShowAppErrorUi(com.android.server.am.ProcessErrorStateRecord errState, com.android.server.am.AppErrorResult res, com.android.server.am.AppErrorDialog.Data data) {
    }

    default boolean isPersistProcessRestarting(com.android.server.am.ProcessRecord app, com.android.server.am.ActivityManagerService service) {
        return false;
    }

    default long getVmSize(int pid, java.lang.String pkgName) {
        return 0L;
    }

    default boolean isAppForeground(java.lang.String pkgName) {
        return false;
    }

    default void doUpload(android.content.Context context, java.lang.String pkgName, java.lang.String versionCode, java.lang.String extraValue) {
    }

    default boolean isShowOriAnrDialog(com.android.server.am.ProcessRecord app) {
        return false;
    }
}
