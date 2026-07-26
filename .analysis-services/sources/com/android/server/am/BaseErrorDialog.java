package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class BaseErrorDialog extends android.app.AlertDialog {
    private static final int DISABLE_BUTTONS = 1;
    private static final int ENABLE_BUTTONS = 0;
    public static com.android.server.am.IBaseErrorDialogExt mBaseErrorDialogExt = (com.android.server.am.IBaseErrorDialogExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IBaseErrorDialogExt.class).create();
    private boolean mConsuming;
    private android.os.Handler mHandler;
    private android.content.BroadcastReceiver mReceiver;

    public BaseErrorDialog(android.content.Context context) {
        super(context, mBaseErrorDialogExt.getDialogThemeStyle(android.R.style.TextAppearance.Widget.ActionMode.Title));
        this.mConsuming = true;
        this.mHandler = new android.os.Handler() { // from class: com.android.server.am.BaseErrorDialog.2
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0) {
                    com.android.server.am.BaseErrorDialog.this.mConsuming = false;
                    com.android.server.am.BaseErrorDialog.this.setEnabled(true);
                } else if (msg.what == 1) {
                    com.android.server.am.BaseErrorDialog.this.setEnabled(false);
                }
            }
        };
        context.assertRuntimeOverlayThemable();
        getWindow().setType(2003);
        getWindow().setFlags(131072, 131072);
        android.view.WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.setTitle("Error Dialog");
        getWindow().setAttributes(attrs);
    }

    @Override // android.app.Dialog
    public void onStart() {
        super.onStart();
        this.mHandler.sendEmptyMessage(1);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0), 1000L);
        if (this.mReceiver == null) {
            this.mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.am.BaseErrorDialog.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                        com.android.server.am.BaseErrorDialog.this.closeDialog();
                    }
                }
            };
            getContext().registerReceiver(this.mReceiver, new android.content.IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"), 2);
        }
    }

    @Override // android.app.Dialog
    protected void onStop() {
        super.onStop();
        if (this.mReceiver != null) {
            try {
                getContext().unregisterReceiver(this.mReceiver);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.e("BaseErrorDialog", "unregisterReceiver threw exception: " + e.getMessage());
            }
            this.mReceiver = null;
        }
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        if (this.mConsuming) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEnabled(boolean enabled) {
        android.widget.Button b = (android.widget.Button) findViewById(android.R.id.button1);
        if (b != null) {
            b.setEnabled(enabled);
        }
        android.widget.Button b2 = (android.widget.Button) findViewById(android.R.id.button2);
        if (b2 != null) {
            b2.setEnabled(enabled);
        }
        android.widget.Button b3 = (android.widget.Button) findViewById(android.R.id.button3);
        if (b3 != null) {
            b3.setEnabled(enabled);
        }
    }

    protected void closeDialog() {
        if (this.mCancelable) {
            cancel();
        } else {
            dismiss();
        }
    }
}
