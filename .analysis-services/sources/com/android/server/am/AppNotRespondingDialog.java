package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class AppNotRespondingDialog extends com.android.server.am.BaseErrorDialog implements android.view.View.OnClickListener {
    public static final int ALREADY_SHOWING = -2;
    public static final int CANT_SHOW = -1;
    static final int FORCE_CLOSE = 1;
    private static final java.lang.String TAG = "AppNotRespondingDialog";
    static final int WAIT = 2;
    static final int WAIT_AND_REPORT = 3;
    private final com.android.server.am.AppNotRespondingDialog.Data mData;
    private final android.os.Handler mHandler;
    private final com.android.server.am.ProcessRecord mProc;
    private final com.android.server.am.ActivityManagerService mService;

    /* JADX WARN: Removed duplicated region for block: B:13:0x0057 A[PHI: r2
  0x0057: PHI (r2v1 'name2' java.lang.CharSequence) = (r2v0 'name2' java.lang.CharSequence), (r2v4 'name2' java.lang.CharSequence) binds: [B:7:0x0037, B:9:0x0046] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AppNotRespondingDialog(com.android.server.am.ActivityManagerService r9, android.content.Context r10, com.android.server.am.AppNotRespondingDialog.Data r11) {
        /*
            Method dump skipped, instruction units count: 215
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.AppNotRespondingDialog.<init>(com.android.server.am.ActivityManagerService, android.content.Context, com.android.server.am.AppNotRespondingDialog$Data):void");
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.widget.FrameLayout frame = (android.widget.FrameLayout) findViewById(android.R.id.custom);
        android.content.Context context = getContext();
        android.view.LayoutInflater.from(context).inflate(android.R.layout.alert_dialog_title_material, (android.view.ViewGroup) frame, true);
        android.widget.TextView report = (android.widget.TextView) findViewById(android.R.id.action4);
        report.setOnClickListener(this);
        boolean hasReceiver = this.mProc.mErrorState.getErrorReportReceiver() != null;
        report.setVisibility(hasReceiver ? 0 : 8);
        android.widget.TextView close = (android.widget.TextView) findViewById(android.R.id.action2);
        close.setOnClickListener(this);
        android.widget.TextView wait = (android.widget.TextView) findViewById(android.R.id.actionGo);
        wait.setOnClickListener(this);
        findViewById(android.R.id.container).setVisibility(0);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case android.R.id.action2:
                this.mHandler.obtainMessage(1).sendToTarget();
                break;
            case android.R.id.action4:
                this.mHandler.obtainMessage(3).sendToTarget();
                break;
            case android.R.id.actionGo:
                this.mHandler.obtainMessage(2).sendToTarget();
                break;
        }
    }

    @Override // com.android.server.am.BaseErrorDialog
    protected void closeDialog() {
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    public static class Data {
        final android.content.pm.ApplicationInfo aInfo;
        final boolean aboveSystem;
        final boolean isContinuousAnr;
        final com.android.server.am.ProcessRecord proc;

        public Data(com.android.server.am.ProcessRecord proc, android.content.pm.ApplicationInfo aInfo, boolean aboveSystem, boolean isContinuousAnr) {
            this.proc = proc;
            this.aInfo = aInfo;
            this.aboveSystem = aboveSystem;
            this.isContinuousAnr = isContinuousAnr;
        }
    }
}
