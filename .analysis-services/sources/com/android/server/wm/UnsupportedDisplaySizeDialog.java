package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class UnsupportedDisplaySizeDialog extends com.android.server.wm.AppWarnings.BaseDialog {
    UnsupportedDisplaySizeDialog(final com.android.server.wm.AppWarnings manager, android.content.Context context, android.content.pm.ApplicationInfo appInfo, int userId) {
        super(manager, context, appInfo.packageName, userId);
        android.content.pm.PackageManager pm = context.getPackageManager();
        java.lang.CharSequence label = appInfo.loadSafeLabel(pm, 1000.0f, 5);
        java.lang.CharSequence message = context.getString(android.R.string.storage_sd_card, label);
        this.mDialog = new android.app.AlertDialog.Builder(context).setPositiveButton(android.R.string.ok, (android.content.DialogInterface.OnClickListener) null).setMessage(message).setView(android.R.layout.slice_small_template).create();
        this.mDialog.create();
        android.view.Window window = this.mDialog.getWindow();
        window.setType(com.android.server.camera.ICameraServiceProxyExt.MSG_FLOAT_WINDOW_SHOW);
        window.getAttributes().setTitle("UnsupportedDisplaySizeDialog");
        android.widget.CheckBox alwaysShow = (android.widget.CheckBox) this.mDialog.findViewById(android.R.id.aerr_close);
        alwaysShow.setChecked(true);
        alwaysShow.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() { // from class: com.android.server.wm.UnsupportedDisplaySizeDialog$$ExternalSyntheticLambda0
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean z) {
                this.f$0.lambda$new$0(manager, compoundButton, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(com.android.server.wm.AppWarnings manager, android.widget.CompoundButton buttonView, boolean isChecked) {
        manager.setPackageFlag(this.mUserId, this.mPackageName, 1, !isChecked);
    }
}
