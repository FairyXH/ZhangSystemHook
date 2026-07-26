package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public abstract class PreBootBroadcaster extends android.content.IIntentReceiver.Stub {
    private static final int MSG_HIDE = 2;
    private static final int MSG_SHOW = 1;
    private static final java.lang.String TAG = "PreBootBroadcaster";
    private final com.android.internal.util.ProgressReporter mProgress;
    private final boolean mQuiet;
    private final com.android.server.am.ActivityManagerService mService;
    private final java.util.List<android.content.pm.ResolveInfo> mTargets;
    private final int mUserId;
    private int mIndex = 0;
    private com.android.server.am.IPreBootBroadcasterExt mPreBootBroadcasterExt = (com.android.server.am.IPreBootBroadcasterExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IPreBootBroadcasterExt.class).base(this).create();
    private android.os.Handler mHandler = new android.os.Handler(com.android.server.UiThread.get().getLooper(), null, true) { // from class: com.android.server.am.PreBootBroadcaster.1
        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            android.app.PendingIntent contentIntent;
            android.content.Context context = com.android.server.am.PreBootBroadcaster.this.mService.mContext;
            android.app.NotificationManager notifManager = (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
            int max = msg.arg1;
            int index = msg.arg2;
            switch (msg.what) {
                case 1:
                    java.lang.CharSequence title = context.getText(android.R.string.alert_windows_notification_turn_off_action);
                    android.content.Intent intent = new android.content.Intent();
                    intent.setClassName("com.android.settings", "com.android.settings.HelpTrampoline");
                    intent.putExtra("android.intent.extra.TEXT", "help_url_upgrading");
                    if (context.getPackageManager().resolveActivity(intent, 0) != null) {
                        contentIntent = android.app.PendingIntent.getActivity(context, 0, intent, 67108864);
                    } else {
                        contentIntent = null;
                    }
                    android.app.Notification notif = new android.app.Notification.Builder(com.android.server.am.PreBootBroadcaster.this.mService.mContext, com.android.internal.notification.SystemNotificationChannels.UPDATES).setSmallIcon(android.R.drawable.seekbar_thumb_pressed_to_unpressed_animation).setWhen(0L).setOngoing(true).setTicker(title).setColor(context.getColor(android.R.color.system_notification_accent_color)).setContentTitle(title).setContentIntent(contentIntent).setVisibility(1).setProgress(max, index, false).build();
                    notifManager.notifyAsUser(com.android.server.am.PreBootBroadcaster.TAG, 13, notif, android.os.UserHandle.of(com.android.server.am.PreBootBroadcaster.this.mUserId));
                    break;
                case 2:
                    notifManager.cancelAsUser(com.android.server.am.PreBootBroadcaster.TAG, 13, android.os.UserHandle.of(com.android.server.am.PreBootBroadcaster.this.mUserId));
                    break;
            }
        }
    };
    private final android.content.Intent mIntent = new android.content.Intent("android.intent.action.PRE_BOOT_COMPLETED");

    public abstract void onFinished();

    public PreBootBroadcaster(com.android.server.am.ActivityManagerService service, int userId, com.android.internal.util.ProgressReporter progress, boolean quiet) {
        this.mService = service;
        this.mUserId = userId;
        this.mProgress = progress;
        this.mQuiet = quiet;
        this.mIntent.addFlags(33554688);
        this.mTargets = this.mService.mContext.getPackageManager().queryBroadcastReceiversAsUser(this.mIntent, 1048576, android.os.UserHandle.of(userId));
        this.mPreBootBroadcasterExt.getReorderedList((java.util.ArrayList) this.mTargets);
    }

    public void sendNext() throws java.lang.Throwable {
        long duration;
        if (this.mIndex < this.mTargets.size()) {
            if (!this.mService.isUserRunning(this.mUserId, 0)) {
                android.util.Slog.i(TAG, "User " + this.mUserId + " is no longer running; skipping remaining receivers");
                this.mHandler.obtainMessage(2).sendToTarget();
                onFinished();
                return;
            }
            if (!this.mQuiet) {
                this.mHandler.obtainMessage(1, this.mTargets.size(), this.mIndex).sendToTarget();
            }
            java.util.List<android.content.pm.ResolveInfo> list = this.mTargets;
            int i = this.mIndex;
            this.mIndex = i + 1;
            android.content.pm.ResolveInfo ri = list.get(i);
            android.content.ComponentName componentName = ri.activityInfo.getComponentName();
            if (this.mProgress != null) {
                java.lang.CharSequence label = ri.activityInfo.loadLabel(this.mService.mContext.getPackageManager());
                this.mProgress.setProgress(this.mIndex, this.mTargets.size(), this.mService.mContext.getString(android.R.string.aerr_restart, label));
            }
            android.util.Slog.i(TAG, "Pre-boot of " + componentName.toShortString() + " for user " + this.mUserId);
            com.android.server.am.EventLogTags.writeAmPreBoot(this.mUserId, componentName.getPackageName());
            this.mIntent.setComponent(componentName);
            android.app.ActivityManagerInternal amInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            if (amInternal == null) {
                duration = 10000;
            } else {
                long duration2 = amInternal.getBootTimeTempAllowListDuration();
                duration = duration2;
            }
            android.app.BroadcastOptions bOptions = android.app.BroadcastOptions.makeBasic();
            bOptions.setTemporaryAppAllowlist(duration, 0, 201, "");
            com.android.server.am.ActivityManagerService activityManagerService = this.mService;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    try {
                        this.mService.broadcastIntentLocked(null, null, null, this.mIntent, null, this, 0, null, null, null, null, null, -1, bOptions.toBundle(), true, false, com.android.server.am.ActivityManagerService.MY_PID, 1000, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), this.mUserId);
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        return;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }
        this.mHandler.obtainMessage(2).sendToTarget();
        onFinished();
    }

    public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) throws java.lang.Throwable {
        sendNext();
    }
}
