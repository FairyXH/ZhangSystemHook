package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class PolicyWarningUIController {
    private static final java.lang.String EXTRA_TIME_FOR_LOGGING = "start_time_to_log_a11y_tool";
    private static final int SEND_NOTIFICATION_DELAY_HOURS = 24;
    private final android.app.AlarmManager mAlarmManager;
    private final android.content.Context mContext;
    private final android.util.ArraySet<android.content.ComponentName> mEnabledA11yServices = new android.util.ArraySet<>();
    private final android.os.Handler mMainHandler;
    private final com.android.server.accessibility.PolicyWarningUIController.NotificationController mNotificationController;
    private static final java.lang.String TAG = com.android.server.accessibility.PolicyWarningUIController.class.getSimpleName();
    protected static final java.lang.String ACTION_SEND_NOTIFICATION = TAG + ".ACTION_SEND_NOTIFICATION";
    protected static final java.lang.String ACTION_A11Y_SETTINGS = TAG + ".ACTION_A11Y_SETTINGS";
    protected static final java.lang.String ACTION_DISMISS_NOTIFICATION = TAG + ".ACTION_DISMISS_NOTIFICATION";

    public PolicyWarningUIController(android.os.Handler handler, android.content.Context context, com.android.server.accessibility.PolicyWarningUIController.NotificationController notificationController) {
        this.mMainHandler = handler;
        this.mContext = context;
        this.mNotificationController = notificationController;
        this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(android.app.AlarmManager.class);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction(ACTION_SEND_NOTIFICATION);
        filter.addAction(ACTION_A11Y_SETTINGS);
        filter.addAction(ACTION_DISMISS_NOTIFICATION);
        this.mContext.registerReceiver(this.mNotificationController, filter, "android.permission.MANAGE_ACCESSIBILITY", this.mMainHandler, 2);
    }

    public void onSwitchUser(int userId, java.util.Set<android.content.ComponentName> enabledServices) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.onSwitchUserInternal(((java.lang.Integer) obj).intValue(), (java.util.Set) obj2);
            }
        }, java.lang.Integer.valueOf(userId), enabledServices));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSwitchUserInternal(int userId, java.util.Set<android.content.ComponentName> enabledServices) {
        this.mEnabledA11yServices.clear();
        this.mEnabledA11yServices.addAll(enabledServices);
        this.mNotificationController.onSwitchUser(userId);
    }

    public void onEnabledServicesChanged(int userId, java.util.Set<android.content.ComponentName> enabledServices) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.onEnabledServicesChangedInternal(((java.lang.Integer) obj).intValue(), (java.util.Set) obj2);
            }
        }, java.lang.Integer.valueOf(userId), enabledServices));
    }

    void onEnabledServicesChangedInternal(int userId, java.util.Set<android.content.ComponentName> enabledServices) {
        android.util.ArraySet<android.content.ComponentName> disabledServices = new android.util.ArraySet<>(this.mEnabledA11yServices);
        disabledServices.removeAll(enabledServices);
        this.mEnabledA11yServices.clear();
        this.mEnabledA11yServices.addAll(enabledServices);
        android.os.Handler handler = this.mMainHandler;
        final com.android.server.accessibility.PolicyWarningUIController.NotificationController notificationController = this.mNotificationController;
        java.util.Objects.requireNonNull(notificationController);
        handler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                notificationController.onServicesDisabled(((java.lang.Integer) obj).intValue(), (android.util.ArraySet) obj2);
            }
        }, java.lang.Integer.valueOf(userId), disabledServices));
    }

    public void onNonA11yCategoryServiceBound(int userId, android.content.ComponentName service) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.setAlarm(((java.lang.Integer) obj).intValue(), (android.content.ComponentName) obj2);
            }
        }, java.lang.Integer.valueOf(userId), service));
    }

    public void onNonA11yCategoryServiceUnbound(int userId, android.content.ComponentName service) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.cancelAlarm(((java.lang.Integer) obj).intValue(), (android.content.ComponentName) obj2);
            }
        }, java.lang.Integer.valueOf(userId), service));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAlarm(int userId, android.content.ComponentName service) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(10, 24);
        this.mAlarmManager.set(0, cal.getTimeInMillis(), createPendingIntent(this.mContext, userId, ACTION_SEND_NOTIFICATION, service));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAlarm(int userId, android.content.ComponentName service) {
        this.mAlarmManager.cancel(createPendingIntent(this.mContext, userId, ACTION_SEND_NOTIFICATION, service));
    }

    protected static android.app.PendingIntent createPendingIntent(android.content.Context context, int userId, java.lang.String action, android.content.ComponentName serviceComponentName) {
        return android.app.PendingIntent.getBroadcast(context, 0, createIntent(context, userId, action, serviceComponentName), 67108864);
    }

    protected static android.content.Intent createIntent(android.content.Context context, int userId, java.lang.String action, android.content.ComponentName serviceComponentName) {
        android.content.Intent intent = new android.content.Intent(action);
        intent.setPackage(context.getPackageName()).setIdentifier(serviceComponentName.flattenToShortString()).putExtra("android.intent.extra.COMPONENT_NAME", serviceComponentName).putExtra("android.intent.extra.USER_ID", userId).putExtra("android.intent.extra.TIME", android.os.SystemClock.elapsedRealtime());
        return intent;
    }

    public void enableSendingNonA11yToolNotification(boolean enable) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.enableSendingNonA11yToolNotificationInternal(((java.lang.Boolean) obj).booleanValue());
            }
        }, java.lang.Boolean.valueOf(enable)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableSendingNonA11yToolNotificationInternal(boolean enable) {
        this.mNotificationController.setSendingNotification(enable);
    }

    public static class NotificationController extends android.content.BroadcastReceiver {
        private static final char RECORD_SEPARATOR = ':';
        private final android.content.Context mContext;
        private int mCurrentUserId;
        private final android.app.NotificationManager mNotificationManager;
        private boolean mSendNotification;
        private final android.util.ArraySet<android.content.ComponentName> mNotifiedA11yServices = new android.util.ArraySet<>();
        private final java.util.List<android.content.ComponentName> mSentA11yServiceNotification = new java.util.ArrayList();

        public NotificationController(android.content.Context context) {
            this.mContext = context;
            this.mNotificationManager = (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            android.content.ComponentName componentName = (android.content.ComponentName) intent.getParcelableExtra("android.intent.extra.COMPONENT_NAME", android.content.ComponentName.class);
            if (android.text.TextUtils.isEmpty(action) || componentName == null) {
                return;
            }
            long startTimeMills = intent.getLongExtra("android.intent.extra.TIME", 0L);
            long durationMills = startTimeMills > 0 ? android.os.SystemClock.elapsedRealtime() - startTimeMills : 0L;
            int userId = intent.getIntExtra("android.intent.extra.USER_ID", 0);
            if (com.android.server.accessibility.PolicyWarningUIController.ACTION_SEND_NOTIFICATION.equals(action)) {
                if (trySendNotification(userId, componentName)) {
                    com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logNonA11yToolServiceWarningReported(componentName.getPackageName(), com.android.internal.accessibility.util.AccessibilityStatsLogUtils.ACCESSIBILITY_PRIVACY_WARNING_STATUS_SHOWN, durationMills);
                }
            } else {
                if (com.android.server.accessibility.PolicyWarningUIController.ACTION_A11Y_SETTINGS.equals(action)) {
                    if (tryLaunchSettings(userId, componentName)) {
                        com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logNonA11yToolServiceWarningReported(componentName.getPackageName(), com.android.internal.accessibility.util.AccessibilityStatsLogUtils.ACCESSIBILITY_PRIVACY_WARNING_STATUS_CLICKED, durationMills);
                    }
                    this.mNotificationManager.cancel(componentName.flattenToShortString(), 1005);
                    this.mSentA11yServiceNotification.remove(componentName);
                    onNotificationCanceled(userId, componentName);
                    return;
                }
                if (com.android.server.accessibility.PolicyWarningUIController.ACTION_DISMISS_NOTIFICATION.equals(action)) {
                    this.mSentA11yServiceNotification.remove(componentName);
                    onNotificationCanceled(userId, componentName);
                }
            }
        }

        protected void onSwitchUser(int userId) {
            cancelSentNotifications();
            this.mNotifiedA11yServices.clear();
            this.mCurrentUserId = userId;
            this.mNotifiedA11yServices.addAll((android.util.ArraySet<? extends android.content.ComponentName>) readNotifiedServiceList(userId));
        }

        protected void onServicesDisabled(int userId, android.util.ArraySet<android.content.ComponentName> disabledServices) {
            if (this.mNotifiedA11yServices.removeAll((android.util.ArraySet<? extends android.content.ComponentName>) disabledServices)) {
                writeNotifiedServiceList(userId, this.mNotifiedA11yServices);
            }
        }

        private boolean trySendNotification(int userId, android.content.ComponentName componentName) {
            if (userId != this.mCurrentUserId || !this.mSendNotification) {
                return false;
            }
            java.util.List<android.accessibilityservice.AccessibilityServiceInfo> enabledServiceInfos = getEnabledServiceInfos();
            int i = 0;
            while (true) {
                if (i >= enabledServiceInfos.size()) {
                    break;
                }
                android.accessibilityservice.AccessibilityServiceInfo a11yServiceInfo = enabledServiceInfos.get(i);
                if (!componentName.flattenToShortString().equals(a11yServiceInfo.getComponentName().flattenToShortString())) {
                    i++;
                } else if (!a11yServiceInfo.isAccessibilityTool() && !this.mNotifiedA11yServices.contains(componentName)) {
                    java.lang.CharSequence displayName = a11yServiceInfo.getResolveInfo().serviceInfo.loadLabel(this.mContext.getPackageManager());
                    android.graphics.drawable.Drawable drawable = a11yServiceInfo.getResolveInfo().loadIcon(this.mContext.getPackageManager());
                    int size = this.mContext.getResources().getDimensionPixelSize(android.R.dimen.app_icon_size);
                    sendNotification(userId, componentName, displayName, com.android.internal.util.ImageUtils.buildScaledBitmap(drawable, size, size));
                    return true;
                }
            }
            return false;
        }

        private boolean tryLaunchSettings(int userId, android.content.ComponentName componentName) {
            if (userId != this.mCurrentUserId) {
                return false;
            }
            android.content.Intent intent = new android.content.Intent("android.settings.ACCESSIBILITY_DETAILS_SETTINGS");
            intent.addFlags(268468224);
            intent.putExtra("android.intent.extra.COMPONENT_NAME", componentName.flattenToShortString());
            intent.putExtra(com.android.server.accessibility.PolicyWarningUIController.EXTRA_TIME_FOR_LOGGING, android.os.SystemClock.elapsedRealtime());
            android.os.Bundle bundle = android.app.ActivityOptions.makeBasic().setLaunchDisplayId(this.mContext.getDisplayId()).toBundle();
            this.mContext.startActivityAsUser(intent, bundle, android.os.UserHandle.of(userId));
            ((android.app.StatusBarManager) this.mContext.getSystemService(android.app.StatusBarManager.class)).collapsePanels();
            return true;
        }

        protected void onNotificationCanceled(int userId, android.content.ComponentName componentName) {
            if (userId == this.mCurrentUserId && this.mNotifiedA11yServices.add(componentName)) {
                writeNotifiedServiceList(userId, this.mNotifiedA11yServices);
            }
        }

        private void sendNotification(int userId, android.content.ComponentName serviceComponentName, java.lang.CharSequence name, android.graphics.Bitmap bitmap) {
            android.app.Notification.Builder notificationBuilder = new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.ACCESSIBILITY_SECURITY_POLICY);
            notificationBuilder.setSmallIcon(android.R.drawable.fastscroll_label_left_holo_light).setContentTitle(this.mContext.getString(android.R.string.time_placeholder)).setContentText(this.mContext.getString(android.R.string.time_picker_text_input_mode_description, name)).setStyle(new android.app.Notification.BigTextStyle().bigText(this.mContext.getString(android.R.string.time_picker_text_input_mode_description, name))).setTicker(this.mContext.getString(android.R.string.time_placeholder)).setOnlyAlertOnce(true).setDeleteIntent(com.android.server.accessibility.PolicyWarningUIController.createPendingIntent(this.mContext, userId, com.android.server.accessibility.PolicyWarningUIController.ACTION_DISMISS_NOTIFICATION, serviceComponentName)).setContentIntent(com.android.server.accessibility.PolicyWarningUIController.createPendingIntent(this.mContext, userId, com.android.server.accessibility.PolicyWarningUIController.ACTION_A11Y_SETTINGS, serviceComponentName));
            if (bitmap != null) {
                notificationBuilder.setLargeIcon(bitmap);
            }
            this.mNotificationManager.notify(serviceComponentName.flattenToShortString(), 1005, notificationBuilder.build());
            this.mSentA11yServiceNotification.add(serviceComponentName);
        }

        private android.util.ArraySet<android.content.ComponentName> readNotifiedServiceList(int userId) {
            java.lang.String notifiedServiceSetting = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "notified_non_accessibility_category_services", userId);
            if (android.text.TextUtils.isEmpty(notifiedServiceSetting)) {
                return new android.util.ArraySet<>();
            }
            android.text.TextUtils.StringSplitter componentNameSplitter = new android.text.TextUtils.SimpleStringSplitter(RECORD_SEPARATOR);
            componentNameSplitter.setString(notifiedServiceSetting);
            android.util.ArraySet<android.content.ComponentName> notifiedServices = new android.util.ArraySet<>();
            for (java.lang.String componentNameString : componentNameSplitter) {
                android.content.ComponentName notifiedService = android.content.ComponentName.unflattenFromString(componentNameString);
                if (notifiedService != null) {
                    notifiedServices.add(notifiedService);
                }
            }
            return notifiedServices;
        }

        private void writeNotifiedServiceList(int userId, android.util.ArraySet<android.content.ComponentName> services) {
            java.lang.StringBuilder notifiedServicesBuilder = new java.lang.StringBuilder();
            for (int i = 0; i < services.size(); i++) {
                if (i > 0) {
                    notifiedServicesBuilder.append(RECORD_SEPARATOR);
                }
                android.content.ComponentName notifiedService = services.valueAt(i);
                notifiedServicesBuilder.append(notifiedService.flattenToShortString());
            }
            android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), "notified_non_accessibility_category_services", notifiedServicesBuilder.toString(), userId);
        }

        protected java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getEnabledServiceInfos() {
            android.view.accessibility.AccessibilityManager accessibilityManager = android.view.accessibility.AccessibilityManager.getInstance(this.mContext);
            return accessibilityManager.getEnabledAccessibilityServiceList(-1);
        }

        private void cancelSentNotifications() {
            this.mSentA11yServiceNotification.forEach(new java.util.function.Consumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$NotificationController$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$cancelSentNotifications$0((android.content.ComponentName) obj);
                }
            });
            this.mSentA11yServiceNotification.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$cancelSentNotifications$0(android.content.ComponentName componentName) {
            this.mNotificationManager.cancel(componentName.flattenToShortString(), 1005);
        }

        void setSendingNotification(boolean enable) {
            this.mSendNotification = enable;
        }
    }
}
