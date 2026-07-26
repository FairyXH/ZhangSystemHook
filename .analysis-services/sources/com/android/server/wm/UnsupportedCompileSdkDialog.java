package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class UnsupportedCompileSdkDialog extends com.android.server.wm.AppWarnings.BaseDialog {
    UnsupportedCompileSdkDialog(final com.android.server.wm.AppWarnings manager, final android.content.Context context, android.content.pm.ApplicationInfo appInfo, int userId) {
        super(manager, context, appInfo.packageName, userId);
        android.content.pm.PackageManager pm = context.getPackageManager();
        java.lang.CharSequence label = appInfo.loadSafeLabel(pm, 1000.0f, 5);
        java.lang.CharSequence message = context.getString(android.R.string.stk_cc_ussd_to_ussd, label);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context).setPositiveButton(android.R.string.ok, (android.content.DialogInterface.OnClickListener) null).setMessage(message).setView(android.R.layout.slice_secondary_text);
        final android.content.Intent installerIntent = com.android.server.utils.AppInstallerUtil.createIntent(context, appInfo.packageName);
        if (installerIntent != null) {
            builder.setNeutralButton(android.R.string.stk_cc_ussd_to_ss, new android.content.DialogInterface.OnClickListener() { // from class: com.android.server.wm.UnsupportedCompileSdkDialog$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(android.content.DialogInterface dialogInterface, int i) {
                    context.startActivity(installerIntent);
                }
            });
        }
        this.mDialog = builder.create();
        this.mDialog.create();
        android.view.Window window = this.mDialog.getWindow();
        window.setType(com.android.server.camera.ICameraServiceProxyExt.MSG_FLOAT_WINDOW_SHOW);
        window.getAttributes().setTitle("UnsupportedCompileSdkDialog");
        android.widget.CheckBox alwaysShow = (android.widget.CheckBox) this.mDialog.findViewById(android.R.id.aerr_close);
        alwaysShow.setChecked(true);
        alwaysShow.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() { // from class: com.android.server.wm.UnsupportedCompileSdkDialog$$ExternalSyntheticLambda1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean z) {
                this.f$0.lambda$new$1(manager, compoundButton, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(com.android.server.wm.AppWarnings manager, android.widget.CompoundButton buttonView, boolean isChecked) {
        manager.setPackageFlag(this.mUserId, this.mPackageName, 2, !isChecked);
    }
}
