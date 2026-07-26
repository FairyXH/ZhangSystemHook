package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class AlertWindowNotification {
    private static final java.lang.String CHANNEL_PREFIX = "com.android.server.wm.AlertWindowNotification - ";
    private static final int NOTIFICATION_ID = 0;
    private static android.app.NotificationChannelGroup sChannelGroup;
    private static int sNextRequestCode = 0;
    private final android.app.NotificationManager mNotificationManager;
    private java.lang.String mNotificationTag;
    private final java.lang.String mPackageName;
    private boolean mPosted;
    private final int mRequestCode;
    private final com.android.server.wm.WindowManagerService mService;

    AlertWindowNotification(com.android.server.wm.WindowManagerService service, java.lang.String packageName) {
        this.mService = service;
        this.mPackageName = packageName;
        this.mNotificationManager = (android.app.NotificationManager) this.mService.mContext.getSystemService("notification");
        this.mNotificationTag = CHANNEL_PREFIX + this.mPackageName;
        int i = sNextRequestCode;
        sNextRequestCode = i + 1;
        this.mRequestCode = i;
    }

    void post() {
        this.mService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.AlertWindowNotification$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.onPostNotification();
            }
        });
    }

    void cancel(final boolean deleteChannel) {
        this.mService.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.AlertWindowNotification$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cancel$0(deleteChannel);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onCancelNotification, reason: merged with bridge method [inline-methods] */
    public void lambda$cancel$0(boolean deleteChannel) {
        if (!this.mPosted) {
            return;
        }
        this.mPosted = false;
        this.mNotificationManager.cancel(this.mNotificationTag, 0);
        if (deleteChannel) {
            this.mNotificationManager.deleteNotificationChannel(this.mNotificationTag);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPostNotification() {
        if (this.mPosted) {
            return;
        }
        this.mPosted = true;
        android.content.Context context = this.mService.mContext;
        android.content.pm.PackageManager pm = context.getPackageManager();
        android.content.pm.ApplicationInfo aInfo = getApplicationInfo(pm, this.mPackageName);
        java.lang.String appName = aInfo != null ? pm.getApplicationLabel(aInfo).toString() : this.mPackageName;
        if (this.mService.getWrapper().getExtImpl().isInNotificationbpList(this.mPackageName)) {
            return;
        }
        createNotificationChannel(context, appName);
        java.lang.String message = context.getString(android.R.string.add_account_label, appName);
        android.os.Bundle extras = new android.os.Bundle();
        extras.putStringArray("android.foregroundApps", new java.lang.String[]{this.mPackageName});
        android.app.Notification.Builder builder = new android.app.Notification.Builder(context, this.mNotificationTag).setOngoing(true).setContentTitle(context.getString(android.R.string.aerr_application, appName)).setContentText(message).setSmallIcon(android.R.drawable.ab_share_pack_material).setColor(context.getColor(android.R.color.system_notification_accent_color)).setStyle(new android.app.Notification.BigTextStyle().bigText(message)).setLocalOnly(true).addExtras(extras).setContentIntent(getContentIntent(context, this.mPackageName));
        if (aInfo != null) {
            android.graphics.drawable.Drawable drawable = pm.getApplicationIcon(aInfo);
            int size = context.getResources().getDimensionPixelSize(android.R.dimen.app_icon_size);
            android.graphics.Bitmap bitmap = com.android.internal.util.ImageUtils.buildScaledBitmap(drawable, size, size);
            if (bitmap != null) {
                builder.setLargeIcon(bitmap);
            }
        }
        this.mNotificationManager.notify(this.mNotificationTag, 0, builder.build());
    }

    private android.app.PendingIntent getContentIntent(android.content.Context context, java.lang.String packageName) {
        android.content.Intent intent = new android.content.Intent("android.settings.MANAGE_APP_OVERLAY_PERMISSION", android.net.Uri.fromParts("package", packageName, null));
        intent.setFlags(268468224);
        return android.app.PendingIntent.getActivity(context, this.mRequestCode, intent, android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF);
    }

    private void createNotificationChannel(android.content.Context context, java.lang.String appName) {
        if (sChannelGroup == null) {
            sChannelGroup = new android.app.NotificationChannelGroup(CHANNEL_PREFIX, this.mService.mContext.getString(android.R.string.addToDictionary));
            this.mNotificationManager.createNotificationChannelGroup(sChannelGroup);
        }
        java.lang.String nameChannel = context.getString(android.R.string.add_account_button_label, appName);
        if (this.mNotificationManager.getNotificationChannel(this.mNotificationTag) != null) {
            return;
        }
        android.app.NotificationChannel channel = new android.app.NotificationChannel(this.mNotificationTag, nameChannel, 1);
        channel.enableLights(false);
        channel.enableVibration(false);
        channel.setBlockable(true);
        channel.setGroup(sChannelGroup.getId());
        channel.setBypassDnd(true);
        if (this.mNotificationManager.getNotificationChannelGroup(sChannelGroup.getId()) == null) {
            this.mNotificationManager.createNotificationChannelGroup(sChannelGroup);
        }
        this.mNotificationManager.createNotificationChannel(channel);
    }

    private android.content.pm.ApplicationInfo getApplicationInfo(android.content.pm.PackageManager pm, java.lang.String packageName) {
        try {
            return pm.getApplicationInfo(packageName, 0);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
