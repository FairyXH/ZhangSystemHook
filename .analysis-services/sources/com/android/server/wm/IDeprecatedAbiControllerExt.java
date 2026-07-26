package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDeprecatedAbiControllerExt {
    default java.lang.String getDeprecatedAbiDialogMessage(android.content.Context context, java.lang.String packageName) {
        return context.getResources().getString(android.R.string.device_unlock_notification_name);
    }
}
