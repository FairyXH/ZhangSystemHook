package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DeprecatedTargetSdkVersionDialog extends com.android.server.wm.AppWarnings.BaseDialog {
    DeprecatedTargetSdkVersionDialog(final com.android.server.wm.AppWarnings manager, final android.content.Context context, android.content.pm.ApplicationInfo appInfo, int userId) {
        super(manager, context, appInfo.packageName, userId);
        android.content.pm.PackageManager pm = context.getPackageManager();
        java.lang.CharSequence label = appInfo.loadSafeLabel(pm, 1000.0f, 5);
        java.lang.CharSequence message = context.getString(android.R.string.dismiss_action);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context).setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() { // from class: com.android.server.wm.DeprecatedTargetSdkVersionDialog$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(android.content.DialogInterface dialogInterface, int i) {
                this.f$0.lambda$new$0(manager, dialogInterface, i);
            }
        }).setMessage(message).setTitle(label);
        final android.content.Intent installerIntent = com.android.server.utils.AppInstallerUtil.createIntent(context, appInfo.packageName);
        if (installerIntent != null) {
            builder.setNeutralButton(android.R.string.disable_accessibility_shortcut, new android.content.DialogInterface.OnClickListener() { // from class: com.android.server.wm.DeprecatedTargetSdkVersionDialog$$ExternalSyntheticLambda1
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
        window.getAttributes().setTitle("DeprecatedTargetSdkVersionDialog");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(com.android.server.wm.AppWarnings manager, android.content.DialogInterface dialog, int which) {
        manager.setPackageFlag(this.mUserId, this.mPackageName, 4, true);
    }
}
