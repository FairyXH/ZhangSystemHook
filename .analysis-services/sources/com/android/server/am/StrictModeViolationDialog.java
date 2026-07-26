package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class StrictModeViolationDialog extends com.android.server.am.BaseErrorDialog {
    static final int ACTION_OK = 0;
    static final int ACTION_OK_AND_REPORT = 1;
    static final long DISMISS_TIMEOUT = 60000;
    private static final java.lang.String TAG = "StrictModeViolationDialog";
    private final android.os.Handler mHandler;
    private final com.android.server.am.ProcessRecord mProc;
    private final com.android.server.am.AppErrorResult mResult;
    private final com.android.server.am.ActivityManagerService mService;

    public StrictModeViolationDialog(android.content.Context context, com.android.server.am.ActivityManagerService service, com.android.server.am.AppErrorResult result, com.android.server.am.ProcessRecord app) {
        java.lang.CharSequence name;
        super(context);
        this.mHandler = new android.os.Handler() { // from class: com.android.server.am.StrictModeViolationDialog.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = com.android.server.am.StrictModeViolationDialog.this.mService.mProcLock;
                com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        if (com.android.server.am.StrictModeViolationDialog.this.mProc != null) {
                            com.android.server.am.StrictModeViolationDialog.this.mProc.mErrorState.getDialogController().clearViolationDialogs();
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                com.android.server.am.StrictModeViolationDialog.this.mResult.set(msg.what);
                com.android.server.am.StrictModeViolationDialog.this.dismiss();
            }
        };
        android.content.res.Resources res = context.getResources();
        this.mService = service;
        this.mProc = app;
        this.mResult = result;
        if (app.getPkgList().size() == 1 && (name = context.getPackageManager().getApplicationLabel(app.info)) != null) {
            setMessage(res.getString(android.R.string.serviceClassPacket, name.toString(), app.info.processName));
        } else {
            setMessage(res.getString(android.R.string.serviceClassSMS, app.processName.toString()));
        }
        setCancelable(false);
        setButton(-1, res.getText(android.R.string.duration_days_relative), this.mHandler.obtainMessage(0));
        if (app.mErrorState.getErrorReportReceiver() != null) {
            setButton(-2, res.getText(android.R.string.rating_label), this.mHandler.obtainMessage(1));
        }
        getWindow().addPrivateFlags(256);
        getWindow().setTitle("Strict Mode Violation: " + app.info.processName);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), 60000L);
    }

    @Override // com.android.server.am.BaseErrorDialog
    protected void closeDialog() {
        this.mHandler.obtainMessage(0).sendToTarget();
    }
}
