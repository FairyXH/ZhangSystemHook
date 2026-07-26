package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class ProcessRecordSocExtImpl implements com.android.server.am.IProcessRecordSocExt {
    private static final java.lang.String TAG = "ProcessRecordSocExtImpl";
    com.android.server.am.ProcessRecord mProcessRecord;

    public ProcessRecordSocExtImpl(java.lang.Object processRecord) {
        this.mProcessRecord = (com.android.server.am.ProcessRecord) processRecord;
    }

    @Override // com.android.server.am.IProcessRecordSocExt
    public void killLocked(com.android.server.am.ActivityManagerService _service, com.android.server.am.ProcessErrorStateRecord errorState, com.android.server.am.ProcessRecord processRecord) {
        android.util.BoostFramework ux_perf = new android.util.BoostFramework();
        if (!com.android.server.am.ActivityManagerService.mForceStopKill && !errorState.isNotResponding() && !errorState.isCrashing()) {
            if (ux_perf.board_first_api_lvl < 33 && ux_perf.board_api_lvl < 33) {
                ux_perf.perfUXEngine_events(4, 0, processRecord.processName, 0);
            }
            ux_perf.perfEvent(4243, processRecord.processName, 2, new int[]{0, processRecord.getPid()});
            return;
        }
        com.android.server.am.ActivityManagerService.mForceStopKill = false;
    }
}
