package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IProcessListSocExt {
    default void startProcess(com.android.server.am.HostingRecord hostingRecord, android.os.Process.ProcessStartResult startResult, com.android.server.am.ProcessRecord app) {
    }

    default void onStartProcess(com.android.server.am.ActivityManagerService service, java.lang.String hostingType, java.lang.String packageName) {
    }
}
