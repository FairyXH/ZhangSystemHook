package com.android.server.display.notifications;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayNotificationManager implements com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector.Listener {
    private static final int DISPLAY_NOTIFICATION_ID = 1;
    private static final java.lang.String DISPLAY_NOTIFICATION_TAG = "DisplayNotificationManager";
    private static final java.lang.String NOTIFICATION_GROUP_NAME = "DisplayNotificationManager";
    private static final long NOTIFICATION_TIMEOUT_MILLISEC = 30000;
    private static final java.lang.String TAG = "DisplayNotificationManager";
    private final boolean mConnectedDisplayErrorHandlingEnabled;
    private com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector mConnectedDisplayUsbErrorsDetector;
    private final android.content.Context mContext;
    private final com.android.server.display.ExternalDisplayStatsService mExternalDisplayStatsService;
    private final com.android.server.display.notifications.DisplayNotificationManager.Injector mInjector;
    private android.app.NotificationManager mNotificationManager;

    public interface Injector {
        com.android.server.display.ExternalDisplayStatsService getExternalDisplayStatsService();

        android.app.NotificationManager getNotificationManager();

        com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector getUsbErrorsDetector();
    }

    public DisplayNotificationManager(final com.android.server.display.feature.DisplayManagerFlags flags, final android.content.Context context, final com.android.server.display.ExternalDisplayStatsService statsService) {
        this(flags, context, new com.android.server.display.notifications.DisplayNotificationManager.Injector() { // from class: com.android.server.display.notifications.DisplayNotificationManager.1
            @Override // com.android.server.display.notifications.DisplayNotificationManager.Injector
            public android.app.NotificationManager getNotificationManager() {
                return (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
            }

            @Override // com.android.server.display.notifications.DisplayNotificationManager.Injector
            public com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector getUsbErrorsDetector() {
                return new com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector(flags, context);
            }

            @Override // com.android.server.display.notifications.DisplayNotificationManager.Injector
            public com.android.server.display.ExternalDisplayStatsService getExternalDisplayStatsService() {
                return statsService;
            }
        });
    }

    DisplayNotificationManager(com.android.server.display.feature.DisplayManagerFlags flags, android.content.Context context, com.android.server.display.notifications.DisplayNotificationManager.Injector injector) {
        this.mConnectedDisplayErrorHandlingEnabled = flags.isConnectedDisplayErrorHandlingEnabled();
        this.mContext = context;
        this.mInjector = injector;
        this.mExternalDisplayStatsService = injector.getExternalDisplayStatsService();
    }

    public void onBootCompleted() {
        this.mNotificationManager = this.mInjector.getNotificationManager();
        if (this.mNotificationManager == null) {
            android.util.Slog.e("DisplayNotificationManager", "onBootCompleted: NotificationManager is null");
            return;
        }
        this.mConnectedDisplayUsbErrorsDetector = this.mInjector.getUsbErrorsDetector();
        if (this.mConnectedDisplayUsbErrorsDetector != null) {
            this.mConnectedDisplayUsbErrorsDetector.registerListener(this);
        }
    }

    @Override // com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector.Listener
    public void onDisplayPortLinkTrainingFailure() {
        if (!this.mConnectedDisplayErrorHandlingEnabled) {
            android.util.Slog.d("DisplayNotificationManager", "onDisplayPortLinkTrainingFailure: mConnectedDisplayErrorHandlingEnabled is false");
        } else {
            this.mExternalDisplayStatsService.onDisplayPortLinkTrainingFailure();
            sendErrorNotification(createErrorNotification(android.R.string.contentServiceTooManyDeletesNotificationDesc, android.R.string.contentServiceSyncNotificationTitle, android.R.drawable.tab_selected));
        }
    }

    @Override // com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector.Listener
    public void onCableNotCapableDisplayPort() {
        if (!this.mConnectedDisplayErrorHandlingEnabled) {
            android.util.Slog.d("DisplayNotificationManager", "onCableNotCapableDisplayPort: mConnectedDisplayErrorHandlingEnabled is false");
        } else {
            this.mExternalDisplayStatsService.onCableNotCapableDisplayPort();
            sendErrorNotification(createErrorNotification(android.R.string.contentServiceTooManyDeletesNotificationDesc, android.R.string.contentServiceSyncNotificationTitle, android.R.drawable.tab_selected));
        }
    }

    public void onHotplugConnectionError() {
        if (!this.mConnectedDisplayErrorHandlingEnabled) {
            android.util.Slog.d("DisplayNotificationManager", "onHotplugConnectionError: mConnectedDisplayErrorHandlingEnabled is false");
        } else {
            this.mExternalDisplayStatsService.onHotplugConnectionError();
            sendErrorNotification(createErrorNotification(android.R.string.contentServiceTooManyDeletesNotificationDesc, android.R.string.contentServiceSyncNotificationTitle, android.R.drawable.tab_selected));
        }
    }

    public void onHighTemperatureExternalDisplayNotAllowed() {
        if (!this.mConnectedDisplayErrorHandlingEnabled) {
            android.util.Slog.d("DisplayNotificationManager", "onHighTemperatureExternalDisplayNotAllowed: mConnectedDisplayErrorHandlingEnabled is false");
        } else {
            sendErrorNotification(createErrorNotification(android.R.string.contentServiceTooManyDeletesNotificationDesc, android.R.string.contentServiceSync, android.R.drawable.ic_private_profile_icon_badge));
        }
    }

    public void cancelNotifications() {
        if (this.mNotificationManager == null) {
            android.util.Slog.e("DisplayNotificationManager", "Can't cancelNotifications: NotificationManager is null");
        } else {
            this.mNotificationManager.cancel("DisplayNotificationManager", 1);
        }
    }

    private void sendErrorNotification(android.app.Notification notification) {
        if (this.mNotificationManager == null) {
            android.util.Slog.e("DisplayNotificationManager", "Can't sendErrorNotification: NotificationManager is null");
        } else {
            this.mNotificationManager.notify("DisplayNotificationManager", 1, notification);
        }
    }

    private android.app.Notification createErrorNotification(int titleId, int messageId, int icon) {
        android.content.res.Resources resources = this.mContext.getResources();
        java.lang.CharSequence title = resources.getText(titleId);
        java.lang.CharSequence message = resources.getText(messageId);
        int color = 0;
        try {
            android.content.res.TypedArray attrs = this.mContext.obtainStyledAttributes(new int[]{android.R.attr.colorError});
            try {
                color = attrs.getColor(0, 0);
                if (attrs != null) {
                    attrs.close();
                }
            } finally {
            }
        } catch (android.content.res.Resources.NotFoundException e) {
            android.util.Slog.e("DisplayNotificationManager", "colorError attribute is not found: " + e.getMessage());
        }
        return new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.ALERTS).setGroup("DisplayNotificationManager").setSmallIcon(icon).setWhen(0L).setTimeoutAfter(30000L).setOngoing(false).setTicker(title).setColor(color).setContentTitle(title).setContentText(message).setVisibility(1).setCategory("err").build();
    }
}
