package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class FactoryErrorDialog extends com.android.server.am.BaseErrorDialog {
    private final android.os.Handler mHandler;

    public FactoryErrorDialog(android.content.Context context, java.lang.CharSequence msg) {
        super(context);
        this.mHandler = new android.os.Handler() { // from class: com.android.server.wm.FactoryErrorDialog.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg2) {
                throw new java.lang.RuntimeException("Rebooting from failed factory test");
            }
        };
        setCancelable(false);
        setTitle(context.getText(android.R.string.factorytest_not_system));
        setMessage(msg);
        setButton(-1, context.getText(android.R.string.fallback_wallpaper_component), this.mHandler.obtainMessage(0));
        android.view.WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.setTitle("Factory Error");
        getWindow().setAttributes(attrs);
    }

    @Override // com.android.server.am.BaseErrorDialog
    protected void closeDialog() {
    }
}
