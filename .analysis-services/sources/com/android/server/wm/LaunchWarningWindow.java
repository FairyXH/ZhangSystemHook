package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public final class LaunchWarningWindow extends android.app.Dialog {
    public LaunchWarningWindow(android.content.Context context, com.android.server.wm.ActivityRecord cur, com.android.server.wm.ActivityRecord next) {
        super(context, android.R.style.Theme.Material.Notification);
        requestWindowFeature(3);
        getWindow().setType(2003);
        getWindow().addFlags(24);
        setContentView(android.R.layout.keyboard_key_preview);
        setTitle(context.getText(android.R.string.kg_password_instructions));
        android.util.TypedValue out = new android.util.TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.alertDialogIcon, out, true);
        getWindow().setFeatureDrawableResource(3, out.resourceId);
        android.widget.ImageView icon = (android.widget.ImageView) findViewById(android.R.id.prefs_frame);
        icon.setImageDrawable(next.info.applicationInfo.loadIcon(context.getPackageManager()));
        android.widget.TextView text = (android.widget.TextView) findViewById(android.R.id.preinstalled);
        text.setText(context.getResources().getString(android.R.string.kg_login_username_hint, next.info.applicationInfo.loadLabel(context.getPackageManager()).toString()));
        android.widget.ImageView icon2 = (android.widget.ImageView) findViewById(android.R.id.nosensor);
        icon2.setImageDrawable(cur.info.applicationInfo.loadIcon(context.getPackageManager()));
        android.widget.TextView text2 = (android.widget.TextView) findViewById(android.R.id.notSensitive);
        text2.setText(context.getResources().getString(android.R.string.kg_login_too_many_attempts, cur.info.applicationInfo.loadLabel(context.getPackageManager()).toString()));
    }
}
