package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class AppWaitingForDebuggerDialog extends com.android.server.am.BaseErrorDialog {
    private java.lang.CharSequence mAppName;
    private final android.os.Handler mHandler;
    final com.android.server.am.ProcessRecord mProc;
    final com.android.server.am.ActivityManagerService mService;

    public AppWaitingForDebuggerDialog(com.android.server.am.ActivityManagerService service, android.content.Context context, com.android.server.am.ProcessRecord app) {
        super(context);
        this.mHandler = new android.os.Handler() { // from class: com.android.server.am.AppWaitingForDebuggerDialog.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        com.android.server.am.AppWaitingForDebuggerDialog.this.mService.killAppAtUsersRequest(com.android.server.am.AppWaitingForDebuggerDialog.this.mProc);
                        break;
                }
            }
        };
        this.mService = service;
        this.mProc = app;
        this.mAppName = context.getPackageManager().getApplicationLabel(app.info);
        setCancelable(false);
        java.lang.StringBuilder text = new java.lang.StringBuilder();
        if (this.mAppName != null && this.mAppName.length() > 0) {
            text.append("Application ");
            text.append(this.mAppName);
            text.append(" (process ");
            text.append(app.processName);
            text.append(")");
        } else {
            text.append("Process ");
            text.append(app.processName);
        }
        text.append(" is waiting for the debugger to attach.");
        setMessage(text.toString());
        setButton(-1, "Force Close", this.mHandler.obtainMessage(1, app));
        setTitle("Waiting For Debugger");
        android.view.WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.privateFlags |= 16;
        attrs.setTitle("Waiting For Debugger: " + app.info.processName);
        getWindow().setAttributes(attrs);
    }

    @Override // com.android.server.am.BaseErrorDialog
    protected void closeDialog() {
    }
}
