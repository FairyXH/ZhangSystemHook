package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class CountdownConditionProvider extends com.android.server.notification.SystemConditionProviderService {
    private static final java.lang.String EXTRA_CONDITION_ID = "condition_id";
    private static final int REQUEST_CODE = 100;
    private static final java.lang.String TAG = "ConditionProviders.CCP";
    private boolean mConnected;
    private boolean mIsAlarm;
    private long mTime;
    private static final boolean DEBUG = android.util.Log.isLoggable("ConditionProviders", 3);
    public static final android.content.ComponentName COMPONENT = new android.content.ComponentName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, com.android.server.notification.CountdownConditionProvider.class.getName());
    private static final java.lang.String ACTION = com.android.server.notification.CountdownConditionProvider.class.getName();
    private final android.content.Context mContext = this;
    private final com.android.server.notification.CountdownConditionProvider.Receiver mReceiver = new com.android.server.notification.CountdownConditionProvider.Receiver();

    public CountdownConditionProvider() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "new CountdownConditionProvider()");
        }
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public android.content.ComponentName getComponent() {
        return COMPONENT;
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public boolean isValidConditionId(android.net.Uri id) {
        return android.service.notification.ZenModeConfig.isValidCountdownConditionId(id);
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public void attachBase(android.content.Context base) {
        attachBaseContext(base);
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public void onBootComplete() {
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public android.service.notification.IConditionProvider asInterface() {
        return onBind(null);
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public void dump(java.io.PrintWriter pw, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        pw.println("    CountdownConditionProvider:");
        pw.print("      mConnected=");
        pw.println(this.mConnected);
        pw.print("      mTime=");
        pw.println(this.mTime);
    }

    @Override // android.service.notification.ConditionProviderService
    public void onConnected() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onConnected");
        }
        this.mContext.registerReceiver(this.mReceiver, new android.content.IntentFilter(ACTION), 2);
        this.mConnected = true;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            android.util.Slog.d(TAG, "onDestroy");
        }
        if (this.mConnected) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
        this.mConnected = false;
    }

    @Override // android.service.notification.ConditionProviderService
    public void onSubscribe(android.net.Uri conditionId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onSubscribe " + conditionId);
        }
        this.mTime = android.service.notification.ZenModeConfig.tryParseCountdownConditionId(conditionId);
        this.mIsAlarm = android.service.notification.ZenModeConfig.isValidCountdownToAlarmConditionId(conditionId);
        android.app.AlarmManager alarms = (android.app.AlarmManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        android.app.PendingIntent pendingIntent = getPendingIntent(conditionId);
        alarms.cancel(pendingIntent);
        if (this.mTime > 0) {
            long now = java.lang.System.currentTimeMillis();
            java.lang.CharSequence span = android.text.format.DateUtils.getRelativeTimeSpanString(this.mTime, now, 60000L);
            if (this.mTime <= now) {
                notifyCondition(newCondition(this.mTime, this.mIsAlarm, 0));
            } else {
                alarms.setExact(0, this.mTime, pendingIntent);
            }
            if (DEBUG) {
                android.util.Slog.d(TAG, java.lang.String.format("%s %s for %s, %s in the future (%s), now=%s", this.mTime <= now ? "Not scheduling" : "Scheduling", ACTION, ts(this.mTime), java.lang.Long.valueOf(this.mTime - now), span, ts(now)));
            }
        }
    }

    android.app.PendingIntent getPendingIntent(android.net.Uri conditionId) {
        android.content.Intent intent = new android.content.Intent(ACTION).setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME).putExtra(EXTRA_CONDITION_ID, conditionId).setFlags(1073741824);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(this.mContext, 100, intent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD);
        return pendingIntent;
    }

    @Override // android.service.notification.ConditionProviderService
    public void onUnsubscribe(android.net.Uri conditionId) {
    }

    private final class Receiver extends android.content.BroadcastReceiver {
        private Receiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (com.android.server.notification.CountdownConditionProvider.ACTION.equals(intent.getAction())) {
                android.net.Uri conditionId = (android.net.Uri) intent.getParcelableExtra(com.android.server.notification.CountdownConditionProvider.EXTRA_CONDITION_ID, android.net.Uri.class);
                boolean alarm = android.service.notification.ZenModeConfig.isValidCountdownToAlarmConditionId(conditionId);
                long time = android.service.notification.ZenModeConfig.tryParseCountdownConditionId(conditionId);
                if (com.android.server.notification.CountdownConditionProvider.DEBUG) {
                    android.util.Slog.d(com.android.server.notification.CountdownConditionProvider.TAG, "Countdown condition fired: " + conditionId);
                }
                if (time > 0) {
                    com.android.server.notification.CountdownConditionProvider.this.notifyCondition(com.android.server.notification.CountdownConditionProvider.newCondition(time, alarm, 0));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final android.service.notification.Condition newCondition(long time, boolean alarm, int state) {
        return new android.service.notification.Condition(android.service.notification.ZenModeConfig.toCountdownConditionId(time, alarm), "", "", "", 0, state, 1);
    }

    public static java.lang.String tryParseDescription(android.net.Uri conditionUri) {
        long time = android.service.notification.ZenModeConfig.tryParseCountdownConditionId(conditionUri);
        if (time == 0) {
            return null;
        }
        long now = java.lang.System.currentTimeMillis();
        java.lang.CharSequence span = android.text.format.DateUtils.getRelativeTimeSpanString(time, now, 60000L);
        return java.lang.String.format("Scheduled for %s, %s in the future (%s), now=%s", ts(time), java.lang.Long.valueOf(time - now), span, ts(now));
    }
}
