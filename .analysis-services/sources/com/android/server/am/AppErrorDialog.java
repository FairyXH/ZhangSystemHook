package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class AppErrorDialog extends com.android.server.am.BaseErrorDialog implements android.view.View.OnClickListener {
    static final int APP_INFO = 8;
    static final int CANCEL = 7;
    static final long DISMISS_TIMEOUT = 300000;
    static final int FORCE_QUIT = 1;
    static final int FORCE_QUIT_AND_REPORT = 2;
    static final int MUTE = 5;
    static final int RESTART = 3;
    static final int TIMEOUT = 6;
    private final android.os.Handler mHandler;
    private final boolean mIsRestartable;
    private final com.android.server.am.ProcessRecord mProc;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private final com.android.server.am.AppErrorResult mResult;
    private final com.android.server.am.ActivityManagerService mService;
    static int CANT_SHOW = -1;
    static int BACKGROUND_USER = -2;
    static int ALREADY_SHOWING = -3;

    public AppErrorDialog(android.content.Context context, com.android.server.am.ActivityManagerService service, com.android.server.am.AppErrorDialog.Data data) {
        java.lang.CharSequence name;
        super(context);
        this.mHandler = new android.os.Handler() { // from class: com.android.server.am.AppErrorDialog.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                com.android.server.am.AppErrorDialog.this.setResult(msg.what);
                com.android.server.am.AppErrorDialog.this.dismiss();
            }
        };
        android.content.res.Resources res = context.getResources();
        this.mService = service;
        this.mProcLock = service.mProcLock;
        this.mProc = data.proc;
        this.mResult = data.result;
        boolean z = false;
        if ((data.taskId != -1 || data.isRestartableForService) && android.provider.Settings.Global.getInt(context.getContentResolver(), "show_restart_in_crash_dialog", 0) != 0) {
            z = true;
        }
        this.mIsRestartable = z;
        android.text.BidiFormatter bidi = android.text.BidiFormatter.getInstance();
        if (this.mProc.getPkgList().size() == 1 && (name = context.getPackageManager().getApplicationLabel(this.mProc.info)) != null) {
            setTitle(res.getString(data.repeating ? android.R.string.activity_resolver_use_once : android.R.string.activity_resolver_use_always, bidi.unicodeWrap(name.toString()), bidi.unicodeWrap(this.mProc.info.processName)));
        } else {
            java.lang.CharSequence name2 = this.mProc.processName;
            setTitle(res.getString(data.repeating ? android.R.string.adb_active_notification_title : android.R.string.adb_active_notification_message, bidi.unicodeWrap(name2.toString())));
        }
        setCancelable(true);
        setCancelMessage(this.mHandler.obtainMessage(7));
        android.view.WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.setTitle("Application Error: " + this.mProc.info.processName);
        attrs.privateFlags |= 272;
        getWindow().setAttributes(attrs);
        if (this.mProc.isPersistent()) {
            getWindow().setType(2010);
        }
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(6), 300000L);
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.widget.FrameLayout frame = (android.widget.FrameLayout) findViewById(android.R.id.custom);
        android.content.Context context = getContext();
        android.view.LayoutInflater.from(context).inflate(android.R.layout.alert_dialog_title_watch, (android.view.ViewGroup) frame, true);
        boolean hasReceiver = this.mProc.mErrorState.getErrorReportReceiver() != null;
        android.widget.TextView restart = (android.widget.TextView) findViewById(android.R.id.actionDone);
        restart.setOnClickListener(this);
        restart.setVisibility(this.mIsRestartable ? 0 : 8);
        android.widget.TextView report = (android.widget.TextView) findViewById(android.R.id.action4);
        report.setOnClickListener(this);
        report.setVisibility(hasReceiver ? 0 : 8);
        android.widget.TextView close = (android.widget.TextView) findViewById(android.R.id.action2);
        close.setOnClickListener(this);
        android.widget.TextView appInfo = (android.widget.TextView) findViewById(android.R.id.action1);
        appInfo.setOnClickListener(this);
        boolean showMute = (android.os.Build.IS_USER || android.provider.Settings.Global.getInt(context.getContentResolver(), "development_settings_enabled", 0) == 0 || android.provider.Settings.Global.getInt(context.getContentResolver(), "show_mute_in_crash_dialog", 0) == 0) ? false : true;
        android.widget.TextView mute = (android.widget.TextView) findViewById(android.R.id.action3);
        mute.setOnClickListener(this);
        mute.setVisibility(showMute ? 0 : 8);
        findViewById(android.R.id.container).setVisibility(0);
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (!this.mResult.mHasResult) {
            setResult(1);
        }
        super.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setResult(int result) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                if (this.mProc != null) {
                    this.mProc.mErrorState.getDialogController().clearCrashDialogs(false);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        this.mResult.set(result);
        this.mHandler.removeMessages(6);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case android.R.id.action1:
                this.mHandler.obtainMessage(8).sendToTarget();
                break;
            case android.R.id.action2:
                this.mHandler.obtainMessage(1).sendToTarget();
                break;
            case android.R.id.action3:
                this.mHandler.obtainMessage(5).sendToTarget();
                break;
            case android.R.id.action4:
                this.mHandler.obtainMessage(2).sendToTarget();
                break;
            case android.R.id.actionDone:
                this.mHandler.obtainMessage(3).sendToTarget();
                break;
        }
    }

    static class Data {
        android.app.ApplicationErrorReport.CrashInfo crashInfo;
        boolean isForeground;
        boolean isRestartableForService;
        com.android.server.am.ProcessRecord proc;
        boolean repeating;
        com.android.server.am.AppErrorResult result;
        int taskId;
        long vmSize;

        Data() {
        }
    }
}
