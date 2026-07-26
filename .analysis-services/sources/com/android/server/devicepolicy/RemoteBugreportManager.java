package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public class RemoteBugreportManager {
    static final java.lang.String BUGREPORT_MIMETYPE = "application/vnd.android.bugreport";
    private static final java.lang.String CTL_STOP = "ctl.stop";
    private static final int NOTIFICATION_ID = 678432343;
    private static final java.lang.String REMOTE_BUGREPORT_SERVICE = "bugreportd";
    private static final long REMOTE_BUGREPORT_TIMEOUT_MILLIS = 600000;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final com.android.server.devicepolicy.DevicePolicyManagerService.Injector mInjector;
    private final com.android.server.devicepolicy.DevicePolicyManagerService mService;
    private final java.security.SecureRandom mRng = new java.security.SecureRandom();
    private final java.util.concurrent.atomic.AtomicLong mRemoteBugreportNonce = new java.util.concurrent.atomic.AtomicLong();
    private final java.util.concurrent.atomic.AtomicBoolean mRemoteBugreportServiceIsActive = new java.util.concurrent.atomic.AtomicBoolean();
    private final java.util.concurrent.atomic.AtomicBoolean mRemoteBugreportSharingAccepted = new java.util.concurrent.atomic.AtomicBoolean();
    private final java.lang.Runnable mRemoteBugreportTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.devicepolicy.RemoteBugreportManager$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };
    private final android.content.BroadcastReceiver mRemoteBugreportFinishedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.devicepolicy.RemoteBugreportManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if ("android.intent.action.REMOTE_BUGREPORT_DISPATCH".equals(intent.getAction()) && com.android.server.devicepolicy.RemoteBugreportManager.this.mRemoteBugreportServiceIsActive.get()) {
                com.android.server.devicepolicy.RemoteBugreportManager.this.onBugreportFinished(intent);
            }
        }
    };
    private final android.content.BroadcastReceiver mRemoteBugreportConsentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.devicepolicy.RemoteBugreportManager.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            com.android.server.devicepolicy.RemoteBugreportManager.this.cancelNotification();
            if ("com.android.server.action.REMOTE_BUGREPORT_SHARING_ACCEPTED".equals(action)) {
                com.android.server.devicepolicy.RemoteBugreportManager.this.onBugreportSharingAccepted();
            } else if ("com.android.server.action.REMOTE_BUGREPORT_SHARING_DECLINED".equals(action)) {
                com.android.server.devicepolicy.RemoteBugreportManager.this.onBugreportSharingDeclined();
            }
            com.android.server.devicepolicy.RemoteBugreportManager.this.mContext.unregisterReceiver(com.android.server.devicepolicy.RemoteBugreportManager.this.mRemoteBugreportConsentReceiver);
        }
    };

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface RemoteBugreportNotificationType {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.mRemoteBugreportServiceIsActive.get()) {
            onBugreportFailed();
        }
    }

    public RemoteBugreportManager(com.android.server.devicepolicy.DevicePolicyManagerService service, com.android.server.devicepolicy.DevicePolicyManagerService.Injector injector) {
        this.mService = service;
        this.mInjector = injector;
        this.mContext = service.mContext;
        this.mHandler = service.mHandler;
    }

    private android.app.Notification buildNotification(int type) {
        android.content.Intent dialogIntent = new android.content.Intent("android.settings.SHOW_REMOTE_BUGREPORT_DIALOG");
        dialogIntent.addFlags(268468224);
        dialogIntent.putExtra("android.app.extra.bugreport_notification_type", type);
        android.content.pm.ActivityInfo targetInfo = dialogIntent.resolveActivityInfo(this.mContext.getPackageManager(), 1048576);
        if (targetInfo != null) {
            dialogIntent.setComponent(targetInfo.getComponentName());
        } else {
            com.android.server.utils.Slogf.wtf("DevicePolicyManager", "Failed to resolve intent for remote bugreport dialog");
        }
        android.app.PendingIntent pendingDialogIntent = android.app.PendingIntent.getActivityAsUser(this.mContext, type, dialogIntent, 67108864, null, android.os.UserHandle.CURRENT);
        android.app.Notification.Builder builder = new android.app.Notification.Builder(this.mContext, com.android.internal.notification.SystemNotificationChannels.DEVICE_ADMIN).setSmallIcon(android.R.drawable.seekbar_thumb_pressed_to_unpressed_animation).setOngoing(true).setLocalOnly(true).setContentIntent(pendingDialogIntent).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color)).extend(new android.app.Notification.TvExtender());
        if (type == 2) {
            builder.setContentTitle(this.mContext.getString(android.R.string.scNullCipherIssueNonEncryptedTitle)).setProgress(0, 0, true);
        } else if (type == 1) {
            builder.setContentTitle(this.mContext.getString(android.R.string.status_bar_call_strength)).setProgress(0, 0, true);
        } else if (type == 3) {
            android.app.PendingIntent pendingIntentAccept = android.app.PendingIntent.getBroadcast(this.mContext, NOTIFICATION_ID, new android.content.Intent("com.android.server.action.REMOTE_BUGREPORT_SHARING_ACCEPTED"), android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF);
            android.app.PendingIntent pendingIntentDecline = android.app.PendingIntent.getBroadcast(this.mContext, NOTIFICATION_ID, new android.content.Intent("com.android.server.action.REMOTE_BUGREPORT_SHARING_DECLINED"), android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF);
            builder.addAction(new android.app.Notification.Action.Builder((android.graphics.drawable.Icon) null, this.mContext.getString(android.R.string.default_audio_route_name_headphones), pendingIntentDecline).build()).addAction(new android.app.Notification.Action.Builder((android.graphics.drawable.Icon) null, this.mContext.getString(android.R.string.scNullCipherIssueActionSettings), pendingIntentAccept).build()).setContentTitle(this.mContext.getString(android.R.string.scNullCipherIssueEncryptedTitle)).setContentText(this.mContext.getString(android.R.string.scNullCipherIssueEncryptedSummary)).setStyle(new android.app.Notification.BigTextStyle().bigText(this.mContext.getString(android.R.string.scNullCipherIssueEncryptedSummary)));
        }
        return builder.build();
    }

    public boolean requestBugreport() {
        long nonce;
        if (this.mRemoteBugreportServiceIsActive.get() || this.mService.getDeviceOwnerRemoteBugreportUriAndHash() != null) {
            com.android.server.utils.Slogf.d("DevicePolicyManager", "Remote bugreport wasn't started because there's already one running");
            return false;
        }
        long callingIdentity = this.mInjector.binderClearCallingIdentity();
        do {
            try {
                nonce = this.mRng.nextLong();
            } catch (android.os.RemoteException re) {
                com.android.server.utils.Slogf.e("DevicePolicyManager", "Failed to make remote calls to start bugreportremote service", re);
                return false;
            } finally {
                this.mInjector.binderRestoreCallingIdentity(callingIdentity);
            }
        } while (nonce == 0);
        this.mInjector.getIActivityManager().requestRemoteBugReport(nonce);
        this.mRemoteBugreportNonce.set(nonce);
        this.mRemoteBugreportServiceIsActive.set(true);
        this.mRemoteBugreportSharingAccepted.set(false);
        registerRemoteBugreportReceivers();
        notify(1);
        this.mHandler.postDelayed(this.mRemoteBugreportTimeoutRunnable, 600000L);
        return true;
    }

    private void registerRemoteBugreportReceivers() {
        try {
            android.content.IntentFilter filterFinished = new android.content.IntentFilter("android.intent.action.REMOTE_BUGREPORT_DISPATCH", BUGREPORT_MIMETYPE);
            this.mContext.registerReceiver(this.mRemoteBugreportFinishedReceiver, filterFinished, 2);
        } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
            com.android.server.utils.Slogf.w("DevicePolicyManager", e, "Failed to set type %s", BUGREPORT_MIMETYPE);
        }
        android.content.IntentFilter filterConsent = new android.content.IntentFilter();
        filterConsent.addAction("com.android.server.action.REMOTE_BUGREPORT_SHARING_DECLINED");
        filterConsent.addAction("com.android.server.action.REMOTE_BUGREPORT_SHARING_ACCEPTED");
        this.mContext.registerReceiver(this.mRemoteBugreportConsentReceiver, filterConsent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBugreportFinished(android.content.Intent intent) {
        long nonce = intent.getLongExtra("android.intent.extra.REMOTE_BUGREPORT_NONCE", 0L);
        if (nonce == 0 || this.mRemoteBugreportNonce.get() != nonce) {
            com.android.server.utils.Slogf.w("DevicePolicyManager", "Invalid nonce provided, ignoring " + nonce);
            return;
        }
        this.mHandler.removeCallbacks(this.mRemoteBugreportTimeoutRunnable);
        this.mRemoteBugreportServiceIsActive.set(false);
        android.net.Uri bugreportUri = intent.getData();
        java.lang.String bugreportUriString = null;
        if (bugreportUri != null) {
            bugreportUriString = bugreportUri.toString();
        }
        java.lang.String bugreportHash = intent.getStringExtra("android.intent.extra.REMOTE_BUGREPORT_HASH");
        if (this.mRemoteBugreportSharingAccepted.get()) {
            shareBugreportWithDeviceOwnerIfExists(bugreportUriString, bugreportHash);
            cancelNotification();
        } else {
            this.mService.setDeviceOwnerRemoteBugreportUriAndHash(bugreportUriString, bugreportHash);
            notify(3);
        }
        this.mContext.unregisterReceiver(this.mRemoteBugreportFinishedReceiver);
    }

    private void onBugreportFailed() {
        this.mRemoteBugreportServiceIsActive.set(false);
        this.mInjector.systemPropertiesSet(CTL_STOP, REMOTE_BUGREPORT_SERVICE);
        this.mRemoteBugreportSharingAccepted.set(false);
        this.mService.setDeviceOwnerRemoteBugreportUriAndHash(null, null);
        cancelNotification();
        android.os.Bundle extras = new android.os.Bundle();
        extras.putInt("android.app.extra.BUGREPORT_FAILURE_REASON", 0);
        this.mService.sendDeviceOwnerCommand("android.app.action.BUGREPORT_FAILED", extras);
        this.mContext.unregisterReceiver(this.mRemoteBugreportConsentReceiver);
        this.mContext.unregisterReceiver(this.mRemoteBugreportFinishedReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBugreportSharingAccepted() {
        this.mRemoteBugreportSharingAccepted.set(true);
        android.util.Pair<java.lang.String, java.lang.String> uriAndHash = this.mService.getDeviceOwnerRemoteBugreportUriAndHash();
        if (uriAndHash != null) {
            shareBugreportWithDeviceOwnerIfExists((java.lang.String) uriAndHash.first, (java.lang.String) uriAndHash.second);
        } else if (this.mRemoteBugreportServiceIsActive.get()) {
            notify(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBugreportSharingDeclined() {
        if (this.mRemoteBugreportServiceIsActive.get()) {
            this.mInjector.systemPropertiesSet(CTL_STOP, REMOTE_BUGREPORT_SERVICE);
            this.mRemoteBugreportServiceIsActive.set(false);
            this.mHandler.removeCallbacks(this.mRemoteBugreportTimeoutRunnable);
            this.mContext.unregisterReceiver(this.mRemoteBugreportFinishedReceiver);
        }
        this.mRemoteBugreportSharingAccepted.set(false);
        this.mService.setDeviceOwnerRemoteBugreportUriAndHash(null, null);
        this.mService.sendDeviceOwnerCommand("android.app.action.BUGREPORT_SHARING_DECLINED", null);
    }

    private void shareBugreportWithDeviceOwnerIfExists(java.lang.String bugreportUriString, java.lang.String bugreportHash) {
        try {
            try {
            } catch (java.io.FileNotFoundException e) {
                android.os.Bundle extras = new android.os.Bundle();
                extras.putInt("android.app.extra.BUGREPORT_FAILURE_REASON", 1);
                this.mService.sendDeviceOwnerCommand("android.app.action.BUGREPORT_FAILED", extras);
            }
            if (bugreportUriString == null) {
                throw new java.io.FileNotFoundException();
            }
            android.net.Uri bugreportUri = android.net.Uri.parse(bugreportUriString);
            this.mService.sendBugreportToDeviceOwner(bugreportUri, bugreportHash);
        } finally {
            this.mRemoteBugreportSharingAccepted.set(false);
            this.mService.setDeviceOwnerRemoteBugreportUriAndHash(null, null);
        }
    }

    public void checkForPendingBugreportAfterBoot() {
        if (this.mService.getDeviceOwnerRemoteBugreportUriAndHash() == null) {
            return;
        }
        android.content.IntentFilter filterConsent = new android.content.IntentFilter();
        filterConsent.addAction("com.android.server.action.REMOTE_BUGREPORT_SHARING_DECLINED");
        filterConsent.addAction("com.android.server.action.REMOTE_BUGREPORT_SHARING_ACCEPTED");
        this.mContext.registerReceiver(this.mRemoteBugreportConsentReceiver, filterConsent);
        notify(3);
    }

    private void notify(int type) {
        this.mInjector.getNotificationManager().notifyAsUser("DevicePolicyManager", NOTIFICATION_ID, buildNotification(type), android.os.UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelNotification() {
        this.mInjector.getNotificationManager().cancelAsUser("DevicePolicyManager", NOTIFICATION_ID, android.os.UserHandle.ALL);
    }
}
