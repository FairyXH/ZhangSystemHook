package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class WindowMagnificationPromptController {
    static final java.lang.String ACTION_DISMISS = "com.android.server.accessibility.magnification.action.DISMISS";
    static final java.lang.String ACTION_TURN_ON_IN_SETTINGS = "com.android.server.accessibility.magnification.action.TURN_ON_IN_SETTINGS";
    private static final android.net.Uri MAGNIFICATION_WINDOW_MODE_PROMPT_URI = android.provider.Settings.Secure.getUriFor("accessibility_show_window_magnification_prompt");
    private final android.database.ContentObserver mContentObserver = new android.database.ContentObserver(null) { // from class: com.android.server.accessibility.magnification.WindowMagnificationPromptController.1
        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            com.android.server.accessibility.magnification.WindowMagnificationPromptController.this.onPromptSettingsValueChanged();
        }
    };
    private final android.content.Context mContext;
    private boolean mNeedToShowNotification;
    android.content.BroadcastReceiver mNotificationActionReceiver;
    private final android.app.NotificationManager mNotificationManager;
    private final int mUserId;

    public WindowMagnificationPromptController(android.content.Context context, int userId) {
        this.mContext = context;
        this.mNotificationManager = (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
        this.mUserId = userId;
        context.getContentResolver().registerContentObserver(MAGNIFICATION_WINDOW_MODE_PROMPT_URI, false, this.mContentObserver, this.mUserId);
        this.mNeedToShowNotification = isWindowMagnificationPromptEnabled();
    }

    protected void onPromptSettingsValueChanged() {
        boolean needToShowNotification = isWindowMagnificationPromptEnabled();
        if (this.mNeedToShowNotification == needToShowNotification) {
            return;
        }
        this.mNeedToShowNotification = needToShowNotification;
        if (!this.mNeedToShowNotification) {
            unregisterReceiverIfNeeded();
            this.mNotificationManager.cancel(1004);
        }
    }

    void showNotificationIfNeeded() {
        if (this.mNeedToShowNotification) {
            android.app.Notification.Builder notificationBuilder = new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.ACCESSIBILITY_MAGNIFICATION);
            java.lang.String message = this.mContext.getString(android.R.string.whichOpenHostLinksWith);
            notificationBuilder.setSmallIcon(android.R.drawable.fastscroll_label_left_holo_light).setContentTitle(this.mContext.getString(android.R.string.whichOpenHostLinksWithApp)).setContentText(message).setLargeIcon(android.graphics.drawable.Icon.createWithResource(this.mContext, android.R.drawable.fastscroll_thumb_pressed_holo)).setTicker(this.mContext.getString(android.R.string.whichOpenHostLinksWithApp)).setOnlyAlertOnce(true).setStyle(new android.app.Notification.BigTextStyle().bigText(message)).setDeleteIntent(createPendingIntent(ACTION_DISMISS)).setContentIntent(createPendingIntent(ACTION_TURN_ON_IN_SETTINGS)).setActions(buildTurnOnAction());
            this.mNotificationManager.notify(1004, notificationBuilder.build());
            registerReceiverIfNeeded();
        }
    }

    public void onDestroy() {
        dismissNotification();
        this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    private boolean isWindowMagnificationPromptEnabled() {
        return android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_show_window_magnification_prompt", 0, this.mUserId) == 1;
    }

    private android.app.Notification.Action buildTurnOnAction() {
        return new android.app.Notification.Action.Builder((android.graphics.drawable.Icon) null, this.mContext.getString(android.R.string.status_bar_tty), createPendingIntent(ACTION_TURN_ON_IN_SETTINGS)).build();
    }

    private android.app.PendingIntent createPendingIntent(java.lang.String action) {
        android.content.Intent intent = new android.content.Intent(action);
        intent.setPackage(this.mContext.getPackageName());
        return android.app.PendingIntent.getBroadcast(this.mContext, 0, intent, 67108864);
    }

    private void registerReceiverIfNeeded() {
        if (this.mNotificationActionReceiver != null) {
            return;
        }
        this.mNotificationActionReceiver = new com.android.server.accessibility.magnification.WindowMagnificationPromptController.NotificationActionReceiver();
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction(ACTION_DISMISS);
        intentFilter.addAction(ACTION_TURN_ON_IN_SETTINGS);
        this.mContext.registerReceiver(this.mNotificationActionReceiver, intentFilter, "android.permission.MANAGE_ACCESSIBILITY", null, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchMagnificationSettings() {
        android.content.Intent intent = new android.content.Intent("android.settings.ACCESSIBILITY_DETAILS_SETTINGS");
        intent.addFlags(268468224);
        intent.putExtra("android.intent.extra.COMPONENT_NAME", com.android.internal.accessibility.AccessibilityShortcutController.MAGNIFICATION_COMPONENT_NAME.flattenToShortString());
        intent.addFlags(268435456);
        android.os.Bundle bundle = android.app.ActivityOptions.makeBasic().setLaunchDisplayId(this.mContext.getDisplayId()).toBundle();
        this.mContext.startActivityAsUser(intent, bundle, android.os.UserHandle.of(this.mUserId));
        ((android.app.StatusBarManager) this.mContext.getSystemService(android.app.StatusBarManager.class)).collapsePanels();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissNotification() {
        unregisterReceiverIfNeeded();
        this.mNotificationManager.cancel(1004);
    }

    private void unregisterReceiverIfNeeded() {
        if (this.mNotificationActionReceiver == null) {
            return;
        }
        this.mContext.unregisterReceiver(this.mNotificationActionReceiver);
        this.mNotificationActionReceiver = null;
    }

    private class NotificationActionReceiver extends android.content.BroadcastReceiver {
        private NotificationActionReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if (android.text.TextUtils.isEmpty(action)) {
                return;
            }
            com.android.server.accessibility.magnification.WindowMagnificationPromptController.this.mNeedToShowNotification = false;
            android.provider.Settings.Secure.putIntForUser(com.android.server.accessibility.magnification.WindowMagnificationPromptController.this.mContext.getContentResolver(), "accessibility_show_window_magnification_prompt", 0, com.android.server.accessibility.magnification.WindowMagnificationPromptController.this.mUserId);
            if (com.android.server.accessibility.magnification.WindowMagnificationPromptController.ACTION_TURN_ON_IN_SETTINGS.equals(action)) {
                com.android.server.accessibility.magnification.WindowMagnificationPromptController.this.launchMagnificationSettings();
                com.android.server.accessibility.magnification.WindowMagnificationPromptController.this.dismissNotification();
            } else if (com.android.server.accessibility.magnification.WindowMagnificationPromptController.ACTION_DISMISS.equals(action)) {
                com.android.server.accessibility.magnification.WindowMagnificationPromptController.this.dismissNotification();
            }
        }
    }
}
