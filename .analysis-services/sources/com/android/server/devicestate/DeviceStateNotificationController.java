package com.android.server.devicestate;

/* JADX INFO: loaded from: classes.dex */
class DeviceStateNotificationController extends android.content.BroadcastReceiver {
    static final java.lang.String CHANNEL_ID = "DeviceStateManager";
    static final java.lang.String INTENT_ACTION_CANCEL_STATE = "com.android.server.devicestate.INTENT_ACTION_CANCEL_STATE";
    static final int NOTIFICATION_ID = 1;
    static final java.lang.String NOTIFICATION_TAG = "DeviceStateManager";
    private static final java.lang.String TAG = "DeviceStateNotificationController";
    private final java.lang.Runnable mCancelStateRunnable;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final com.android.server.devicestate.DeviceStateNotificationController.NotificationInfoProvider mNotificationInfoProvider;
    private final android.app.NotificationManager mNotificationManager;
    private final android.content.pm.PackageManager mPackageManager;

    DeviceStateNotificationController(android.content.Context context, android.os.Handler handler, java.lang.Runnable cancelStateRunnable) {
        this(context, handler, cancelStateRunnable, new com.android.server.devicestate.DeviceStateNotificationController.NotificationInfoProvider(context), context.getPackageManager(), (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class));
    }

    DeviceStateNotificationController(android.content.Context context, android.os.Handler handler, java.lang.Runnable cancelStateRunnable, com.android.server.devicestate.DeviceStateNotificationController.NotificationInfoProvider notificationInfoProvider, android.content.pm.PackageManager packageManager, android.app.NotificationManager notificationManager) {
        this.mContext = context;
        this.mHandler = handler;
        this.mCancelStateRunnable = cancelStateRunnable;
        this.mNotificationInfoProvider = notificationInfoProvider;
        this.mPackageManager = packageManager;
        this.mNotificationManager = notificationManager;
        this.mContext.registerReceiver(this, new android.content.IntentFilter(INTENT_ACTION_CANCEL_STATE), "android.permission.CONTROL_DEVICE_STATE", this.mHandler, 4);
    }

    void showStateActiveNotificationIfNeeded(int state, int requestingAppUid) {
        com.android.server.devicestate.DeviceStateNotificationController.NotificationInfo info = getNotificationInfos().get(state);
        if (info == null || !info.hasActiveNotification()) {
            return;
        }
        java.lang.String requesterApplicationLabel = getApplicationLabel(requestingAppUid);
        if (requesterApplicationLabel != null) {
            android.content.Intent intent = new android.content.Intent(INTENT_ACTION_CANCEL_STATE).setPackage(this.mContext.getPackageName());
            android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(this.mContext, 0, intent, 67108864);
            showNotification(info.name, info.activeNotificationTitle, java.lang.String.format(info.activeNotificationContent, requesterApplicationLabel), true, android.R.drawable.ic_close, pendingIntent, this.mContext.getString(android.R.string.display_manager_overlay_display_title));
            return;
        }
        android.util.Slog.e(TAG, "Cannot determine the requesting app name when showing state active notification. uid=" + requestingAppUid + ", state=" + state);
    }

    void showThermalCriticalNotificationIfNeeded(int state) {
        com.android.server.devicestate.DeviceStateNotificationController.NotificationInfo info = getNotificationInfos().get(state);
        if (info == null || !info.hasThermalCriticalNotification()) {
            return;
        }
        showNotification(info.name, info.thermalCriticalNotificationTitle, info.thermalCriticalNotificationContent, false, android.R.drawable.ic_private_profile_badge, null, null);
    }

    void showPowerSaveNotificationIfNeeded(int state) {
        com.android.server.devicestate.DeviceStateNotificationController.NotificationInfo info = getNotificationInfos().get(state);
        if (info == null || !info.hasPowerSaveModeNotification()) {
            return;
        }
        android.content.Intent intent = new android.content.Intent("android.settings.BATTERY_SAVER_SETTINGS");
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivity(this.mContext, 0, intent, 67108864);
        showNotification(info.name, info.powerSaveModeNotificationTitle, info.powerSaveModeNotificationContent, false, android.R.drawable.ic_private_profile_badge, pendingIntent, this.mContext.getString(android.R.string.display_manager_overlay_display_secure_suffix));
    }

    void cancelNotification(int state) {
        if (getNotificationInfos().get(state) == null) {
            return;
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.devicestate.DeviceStateNotificationController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$cancelNotification$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelNotification$0() {
        this.mNotificationManager.cancel("DeviceStateManager", 1);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        if (intent != null && INTENT_ACTION_CANCEL_STATE.equals(intent.getAction())) {
            this.mCancelStateRunnable.run();
        }
    }

    private void showNotification(java.lang.String name, java.lang.String title, java.lang.String content, boolean ongoing, int iconRes, android.app.PendingIntent pendingIntent, java.lang.String actionText) {
        final android.app.NotificationChannel channel = new android.app.NotificationChannel("DeviceStateManager", name, 4);
        final android.app.Notification.Builder builder = new android.app.Notification.Builder(this.mContext, "DeviceStateManager").setSmallIcon(iconRes).setContentTitle(title).setContentText(content).setSubText(name).setLocalOnly(true).setOngoing(ongoing).setCategory("sys");
        if (pendingIntent != null && actionText != null) {
            android.app.Notification.Action action = new android.app.Notification.Action.Builder((android.graphics.drawable.Icon) null, actionText, pendingIntent).build();
            builder.addAction(action);
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.devicestate.DeviceStateNotificationController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$showNotification$1(channel, builder);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showNotification$1(android.app.NotificationChannel channel, android.app.Notification.Builder builder) {
        this.mNotificationManager.createNotificationChannel(channel);
        this.mNotificationManager.notify("DeviceStateManager", 1, builder.build());
    }

    private android.util.SparseArray<com.android.server.devicestate.DeviceStateNotificationController.NotificationInfo> getNotificationInfos() {
        java.util.Locale locale = this.mContext.getResources().getConfiguration().getLocales().get(0);
        return this.mNotificationInfoProvider.getNotificationInfos(locale);
    }

    public static class NotificationInfoProvider {
        java.util.Locale mCachedLocale;
        private android.util.SparseArray<com.android.server.devicestate.DeviceStateNotificationController.NotificationInfo> mCachedNotificationInfos;
        private final android.content.Context mContext;
        private final java.lang.Object mLock = new java.lang.Object();

        NotificationInfoProvider(android.content.Context context) {
            this.mContext = context;
        }

        public android.util.SparseArray<com.android.server.devicestate.DeviceStateNotificationController.NotificationInfo> getNotificationInfos(java.util.Locale locale) {
            android.util.SparseArray<com.android.server.devicestate.DeviceStateNotificationController.NotificationInfo> sparseArray;
            synchronized (this.mLock) {
                if (!locale.equals(this.mCachedLocale)) {
                    refreshNotificationInfos(locale);
                }
                sparseArray = this.mCachedNotificationInfos;
            }
            return sparseArray;
        }

        java.util.Locale getCachedLocale() {
            java.util.Locale locale;
            synchronized (this.mLock) {
                locale = this.mCachedLocale;
            }
            return locale;
        }

        public void refreshNotificationInfos(java.util.Locale locale) {
            synchronized (this.mLock) {
                this.mCachedLocale = locale;
                this.mCachedNotificationInfos = loadNotificationInfos();
            }
        }

        public android.util.SparseArray<com.android.server.devicestate.DeviceStateNotificationController.NotificationInfo> loadNotificationInfos() {
            android.util.SparseArray<com.android.server.devicestate.DeviceStateNotificationController.NotificationInfo> notificationInfos = new android.util.SparseArray<>();
            int[] stateIdentifiers = this.mContext.getResources().getIntArray(android.R.array.config_wearActivityModeRadios);
            java.lang.String[] names = this.mContext.getResources().getStringArray(android.R.array.config_virtualKeyVibePattern);
            java.lang.String[] activeNotificationTitles = this.mContext.getResources().getStringArray(android.R.array.config_verizon_satellite_enabled_tagids);
            java.lang.String[] activeNotificationContents = this.mContext.getResources().getStringArray(android.R.array.config_usbHostDenylist);
            java.lang.String[] thermalCriticalNotificationTitles = this.mContext.getResources().getStringArray(android.R.array.cross_profile_apps);
            java.lang.String[] thermalCriticalNotificationContents = this.mContext.getResources().getStringArray(android.R.array.crossSimSpnFormats);
            java.lang.String[] powerSaveModeNotificationTitles = this.mContext.getResources().getStringArray(android.R.array.config_waterfallCutoutArray);
            java.lang.String[] powerSaveModeNotificationContents = this.mContext.getResources().getStringArray(android.R.array.config_vvmSmsFilterRegexes);
            if (stateIdentifiers.length != names.length || stateIdentifiers.length != activeNotificationTitles.length || stateIdentifiers.length != activeNotificationContents.length || stateIdentifiers.length != thermalCriticalNotificationTitles.length || stateIdentifiers.length != thermalCriticalNotificationContents.length || stateIdentifiers.length != powerSaveModeNotificationTitles.length || stateIdentifiers.length != powerSaveModeNotificationContents.length) {
                throw new java.lang.IllegalStateException("The length of state identifiers and notification texts must match!");
            }
            for (int i = 0; i < stateIdentifiers.length; i++) {
                int identifier = stateIdentifiers[i];
                if (identifier != -1) {
                    notificationInfos.put(identifier, new com.android.server.devicestate.DeviceStateNotificationController.NotificationInfo(names[i], activeNotificationTitles[i], activeNotificationContents[i], thermalCriticalNotificationTitles[i], thermalCriticalNotificationContents[i], powerSaveModeNotificationTitles[i], powerSaveModeNotificationContents[i]));
                }
            }
            return notificationInfos;
        }
    }

    private java.lang.String getApplicationLabel(int uid) {
        java.lang.String packageName = this.mPackageManager.getNameForUid(uid);
        try {
            android.content.pm.ApplicationInfo appInfo = this.mPackageManager.getApplicationInfo(packageName, android.content.pm.PackageManager.ApplicationInfoFlags.of(0L));
            return appInfo.loadLabel(this.mPackageManager).toString();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    static class NotificationInfo {
        public final java.lang.String activeNotificationContent;
        public final java.lang.String activeNotificationTitle;
        public final java.lang.String name;
        public final java.lang.String powerSaveModeNotificationContent;
        public final java.lang.String powerSaveModeNotificationTitle;
        public final java.lang.String thermalCriticalNotificationContent;
        public final java.lang.String thermalCriticalNotificationTitle;

        NotificationInfo(java.lang.String name, java.lang.String activeNotificationTitle, java.lang.String activeNotificationContent, java.lang.String thermalCriticalNotificationTitle, java.lang.String thermalCriticalNotificationContent, java.lang.String powerSaveModeNotificationTitle, java.lang.String powerSaveModeNotificationContent) {
            this.name = name;
            this.activeNotificationTitle = activeNotificationTitle;
            this.activeNotificationContent = activeNotificationContent;
            this.thermalCriticalNotificationTitle = thermalCriticalNotificationTitle;
            this.thermalCriticalNotificationContent = thermalCriticalNotificationContent;
            this.powerSaveModeNotificationTitle = powerSaveModeNotificationTitle;
            this.powerSaveModeNotificationContent = powerSaveModeNotificationContent;
        }

        boolean hasActiveNotification() {
            return this.activeNotificationTitle != null && this.activeNotificationTitle.length() > 0;
        }

        boolean hasThermalCriticalNotification() {
            return this.thermalCriticalNotificationTitle != null && this.thermalCriticalNotificationTitle.length() > 0;
        }

        boolean hasPowerSaveModeNotification() {
            return this.powerSaveModeNotificationTitle != null && this.powerSaveModeNotificationTitle.length() > 0;
        }
    }
}
