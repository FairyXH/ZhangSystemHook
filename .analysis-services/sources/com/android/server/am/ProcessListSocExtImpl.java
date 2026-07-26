package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class ProcessListSocExtImpl implements com.android.server.am.IProcessListSocExt {
    private static final java.lang.String TAG = "ProcessListSocExtImpl";
    public static android.util.BoostFramework mPerfServiceStartHint = new android.util.BoostFramework();
    com.android.server.am.ProcessList mProcessList;

    public ProcessListSocExtImpl(java.lang.Object processList) {
        this.mProcessList = (com.android.server.am.ProcessList) processList;
    }

    @Override // com.android.server.am.IProcessListSocExt
    public void startProcess(com.android.server.am.HostingRecord hostingRecord, android.os.Process.ProcessStartResult startResult, com.android.server.am.ProcessRecord app) {
        if (mPerfServiceStartHint != null && hostingRecord.getType() != null) {
            if ((hostingRecord.getType().equals(com.android.server.am.HostingRecord.HOSTING_TYPE_NEXT_ACTIVITY) || hostingRecord.getType().equals(com.android.server.am.HostingRecord.HOSTING_TYPE_NEXT_TOP_ACTIVITY)) && startResult != null) {
                mPerfServiceStartHint.perfHint(4225, app.processName, startResult.pid, 101);
            }
        }
    }

    @Override // com.android.server.am.IProcessListSocExt
    public void onStartProcess(com.android.server.am.ActivityManagerService service, java.lang.String hostingType, java.lang.String packageName) {
    }
}
