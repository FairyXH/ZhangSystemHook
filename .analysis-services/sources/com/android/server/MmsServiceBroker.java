package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class MmsServiceBroker extends com.android.server.SystemService {
    private static final int MSG_TRY_CONNECTING = 1;
    private static final long RETRY_DELAY_ON_DISCONNECTION_MS = 3000;
    private static final long SERVICE_CONNECTION_WAIT_TIME_MS = 4000;
    private static final java.lang.String TAG = "MmsServiceBroker";
    private volatile android.app.AppOpsManager mAppOpsManager;
    private android.content.ServiceConnection mConnection;
    private final android.os.Handler mConnectionHandler;
    private android.content.Context mContext;
    private volatile android.content.pm.PackageManager mPackageManager;
    private volatile com.android.internal.telephony.IMms mService;
    private final com.android.internal.telephony.IMms mServiceStubForFailure;
    private volatile android.telephony.TelephonyManager mTelephonyManager;
    private static final android.content.ComponentName MMS_SERVICE_COMPONENT = new android.content.ComponentName("com.android.mms.service", "com.android.mms.service.MmsService");
    private static final android.net.Uri FAKE_SMS_SENT_URI = android.net.Uri.parse("content://sms/sent/0");
    private static final android.net.Uri FAKE_MMS_SENT_URI = android.net.Uri.parse("content://mms/sent/0");
    private static final android.net.Uri FAKE_SMS_DRAFT_URI = android.net.Uri.parse("content://sms/draft/0");
    private static final android.net.Uri FAKE_MMS_DRAFT_URI = android.net.Uri.parse("content://mms/draft/0");

    public MmsServiceBroker(android.content.Context context) {
        super(context);
        this.mAppOpsManager = null;
        this.mPackageManager = null;
        this.mTelephonyManager = null;
        this.mConnectionHandler = new android.os.Handler() { // from class: com.android.server.MmsServiceBroker.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        com.android.server.MmsServiceBroker.this.tryConnecting();
                        break;
                    default:
                        android.util.Slog.e(com.android.server.MmsServiceBroker.TAG, "Unknown message");
                        break;
                }
            }
        };
        this.mConnection = new android.content.ServiceConnection() { // from class: com.android.server.MmsServiceBroker.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
                android.util.Slog.i(com.android.server.MmsServiceBroker.TAG, "MmsService connected");
                synchronized (com.android.server.MmsServiceBroker.this) {
                    com.android.server.MmsServiceBroker.this.mService = com.android.internal.telephony.IMms.Stub.asInterface(android.os.Binder.allowBlocking(service));
                    com.android.server.MmsServiceBroker.this.notifyAll();
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(android.content.ComponentName name) {
                android.util.Slog.i(com.android.server.MmsServiceBroker.TAG, "MmsService unexpectedly disconnected");
                synchronized (com.android.server.MmsServiceBroker.this) {
                    com.android.server.MmsServiceBroker.this.mService = null;
                    com.android.server.MmsServiceBroker.this.notifyAll();
                }
                com.android.server.MmsServiceBroker.this.mConnectionHandler.sendMessageDelayed(com.android.server.MmsServiceBroker.this.mConnectionHandler.obtainMessage(1), 3000L);
            }
        };
        this.mServiceStubForFailure = new com.android.internal.telephony.IMms() { // from class: com.android.server.MmsServiceBroker.3
            public android.os.IBinder asBinder() {
                return null;
            }

            public void sendMessage(int subId, java.lang.String callingPkg, android.net.Uri contentUri, java.lang.String locationUrl, android.os.Bundle configOverrides, android.app.PendingIntent sentIntent, long messageId, java.lang.String attributionTag) throws android.os.RemoteException {
                returnPendingIntentWithError(sentIntent);
            }

            public void downloadMessage(int subId, java.lang.String callingPkg, java.lang.String locationUrl, android.net.Uri contentUri, android.os.Bundle configOverrides, android.app.PendingIntent downloadedIntent, long messageId, java.lang.String attributionTag) throws android.os.RemoteException {
                returnPendingIntentWithError(downloadedIntent);
            }

            public android.net.Uri importTextMessage(java.lang.String callingPkg, java.lang.String address, int type, java.lang.String text, long timestampMillis, boolean seen, boolean read) throws android.os.RemoteException {
                return null;
            }

            public android.net.Uri importMultimediaMessage(java.lang.String callingPkg, android.net.Uri contentUri, java.lang.String messageId, long timestampSecs, boolean seen, boolean read) throws android.os.RemoteException {
                return null;
            }

            public boolean deleteStoredMessage(java.lang.String callingPkg, android.net.Uri messageUri) throws android.os.RemoteException {
                return false;
            }

            public boolean deleteStoredConversation(java.lang.String callingPkg, long conversationId) throws android.os.RemoteException {
                return false;
            }

            public boolean updateStoredMessageStatus(java.lang.String callingPkg, android.net.Uri messageUri, android.content.ContentValues statusValues) throws android.os.RemoteException {
                return false;
            }

            public boolean archiveStoredConversation(java.lang.String callingPkg, long conversationId, boolean archived) throws android.os.RemoteException {
                return false;
            }

            public android.net.Uri addTextMessageDraft(java.lang.String callingPkg, java.lang.String address, java.lang.String text) throws android.os.RemoteException {
                return null;
            }

            public android.net.Uri addMultimediaMessageDraft(java.lang.String callingPkg, android.net.Uri contentUri) throws android.os.RemoteException {
                return null;
            }

            public void sendStoredMessage(int subId, java.lang.String callingPkg, android.net.Uri messageUri, android.os.Bundle configOverrides, android.app.PendingIntent sentIntent) throws android.os.RemoteException {
                returnPendingIntentWithError(sentIntent);
            }

            public void setAutoPersisting(java.lang.String callingPkg, boolean enabled) throws android.os.RemoteException {
            }

            public boolean getAutoPersisting() throws android.os.RemoteException {
                return false;
            }

            private void returnPendingIntentWithError(android.app.PendingIntent pendingIntent) {
                try {
                    pendingIntent.send(com.android.server.MmsServiceBroker.this.mContext, 1, (android.content.Intent) null);
                } catch (android.app.PendingIntent.CanceledException e) {
                    android.util.Slog.e(com.android.server.MmsServiceBroker.TAG, "Failed to return pending intent result", e);
                }
            }
        };
        this.mContext = context;
        this.mService = null;
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("imms", new com.android.server.MmsServiceBroker.BinderService());
    }

    public void systemRunning() {
        android.util.Slog.i(TAG, "Delay connecting to MmsService until an API is called");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryConnecting() {
        android.util.Slog.i(TAG, "Connecting to MmsService");
        synchronized (this) {
            if (this.mService != null) {
                android.util.Slog.d(TAG, "Already connected");
                return;
            }
            android.content.Intent intent = new android.content.Intent();
            intent.setComponent(MMS_SERVICE_COMPONENT);
            try {
                if (!this.mContext.bindService(intent, this.mConnection, 1)) {
                    android.util.Slog.e(TAG, "Failed to bind to MmsService");
                }
            } catch (java.lang.SecurityException e) {
                android.util.Slog.e(TAG, "Forbidden to bind to MmsService", e);
            }
        }
    }

    private com.android.internal.telephony.IMms getOrConnectService() {
        synchronized (this) {
            if (this.mService != null) {
                return this.mService;
            }
            android.util.Slog.w(TAG, "MmsService not connected. Try connecting...");
            this.mConnectionHandler.sendMessage(this.mConnectionHandler.obtainMessage(1));
            long shouldEnd = android.os.SystemClock.elapsedRealtime() + SERVICE_CONNECTION_WAIT_TIME_MS;
            for (long waitTime = SERVICE_CONNECTION_WAIT_TIME_MS; waitTime > 0; waitTime = shouldEnd - android.os.SystemClock.elapsedRealtime()) {
                try {
                    wait(waitTime);
                } catch (java.lang.InterruptedException e) {
                    android.util.Slog.w(TAG, "Connection wait interrupted", e);
                }
                if (this.mService != null) {
                    return this.mService;
                }
            }
            android.util.Slog.e(TAG, "Can not connect to MmsService (timed out)");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.internal.telephony.IMms getServiceGuarded() {
        com.android.internal.telephony.IMms service = getOrConnectService();
        if (service != null) {
            return service;
        }
        return this.mServiceStubForFailure;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.AppOpsManager getAppOpsManager() {
        if (this.mAppOpsManager == null) {
            this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService("appops");
        }
        return this.mAppOpsManager;
    }

    private android.content.pm.PackageManager getPackageManager() {
        if (this.mPackageManager == null) {
            this.mPackageManager = this.mContext.getPackageManager();
        }
        return this.mPackageManager;
    }

    private android.telephony.TelephonyManager getTelephonyManager() {
        if (this.mTelephonyManager == null) {
            this.mTelephonyManager = (android.telephony.TelephonyManager) this.mContext.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
        }
        return this.mTelephonyManager;
    }

    private java.lang.String getCallingPackageName() {
        java.lang.String[] packages = getPackageManager().getPackagesForUid(android.os.Binder.getCallingUid());
        if (packages != null && packages.length > 0) {
            return packages[0];
        }
        return "unknown";
    }

    private final class BinderService extends com.android.internal.telephony.IMms.Stub {
        private static final java.lang.String PHONE_PACKAGE_NAME = "com.android.phone";

        private BinderService() {
        }

        public void sendMessage(int subId, java.lang.String callingPkg, android.net.Uri contentUri, java.lang.String locationUrl, android.os.Bundle configOverrides, android.app.PendingIntent sentIntent, long messageId, java.lang.String attributionTag) throws java.lang.Throwable {
            android.util.Slog.d(com.android.server.MmsServiceBroker.TAG, "sendMessage() by " + callingPkg);
            com.android.server.MmsServiceBroker.this.mContext.enforceCallingPermission("android.permission.SEND_SMS", "Send MMS message");
            if (!com.android.internal.telephony.TelephonyPermissions.checkSubscriptionAssociatedWithUser(com.android.server.MmsServiceBroker.this.mContext, subId, android.os.Binder.getCallingUserHandle()) && com.android.server.MmsServiceBroker.this.isActiveSubId(subId)) {
                com.android.internal.telephony.util.TelephonyUtils.showSwitchToManagedProfileDialogIfAppropriate(com.android.server.MmsServiceBroker.this.mContext, subId, android.os.Binder.getCallingUid(), callingPkg);
            } else if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(20, android.os.Binder.getCallingUid(), callingPkg, attributionTag, (java.lang.String) null) != 0) {
                android.util.Slog.e(com.android.server.MmsServiceBroker.TAG, callingPkg + " is not allowed to call sendMessage()");
            } else {
                com.android.server.MmsServiceBroker.this.getServiceGuarded().sendMessage(subId, callingPkg, adjustUriForUserAndGrantPermission(contentUri, "android.service.carrier.CarrierMessagingService", 1, subId), locationUrl, configOverrides, sentIntent, messageId, attributionTag);
            }
        }

        public void downloadMessage(int subId, java.lang.String callingPkg, java.lang.String locationUrl, android.net.Uri contentUri, android.os.Bundle configOverrides, android.app.PendingIntent downloadedIntent, long messageId, java.lang.String attributionTag) throws java.lang.Throwable {
            android.util.Slog.d(com.android.server.MmsServiceBroker.TAG, "downloadMessage() by " + callingPkg);
            com.android.server.MmsServiceBroker.this.mContext.enforceCallingPermission("android.permission.RECEIVE_MMS", "Download MMS message");
            if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(18, android.os.Binder.getCallingUid(), callingPkg, attributionTag, (java.lang.String) null) != 0) {
                android.util.Slog.e(com.android.server.MmsServiceBroker.TAG, callingPkg + " is not allowed to call downloadMessage()");
            } else {
                com.android.server.MmsServiceBroker.this.getServiceGuarded().downloadMessage(subId, callingPkg, locationUrl, adjustUriForUserAndGrantPermission(contentUri, "android.service.carrier.CarrierMessagingService", 3, subId), configOverrides, downloadedIntent, messageId, attributionTag);
            }
        }

        public android.net.Uri importTextMessage(java.lang.String callingPkg, java.lang.String address, int type, java.lang.String text, long timestampMillis, boolean seen, boolean read) throws android.os.RemoteException {
            if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(15, android.os.Binder.getCallingUid(), callingPkg, (java.lang.String) null, (java.lang.String) null) != 0) {
                return com.android.server.MmsServiceBroker.FAKE_SMS_SENT_URI;
            }
            return com.android.server.MmsServiceBroker.this.getServiceGuarded().importTextMessage(callingPkg, address, type, text, timestampMillis, seen, read);
        }

        public android.net.Uri importMultimediaMessage(java.lang.String callingPkg, android.net.Uri contentUri, java.lang.String messageId, long timestampSecs, boolean seen, boolean read) throws android.os.RemoteException {
            if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(15, android.os.Binder.getCallingUid(), callingPkg, (java.lang.String) null, (java.lang.String) null) != 0) {
                return com.android.server.MmsServiceBroker.FAKE_MMS_SENT_URI;
            }
            return com.android.server.MmsServiceBroker.this.getServiceGuarded().importMultimediaMessage(callingPkg, contentUri, messageId, timestampSecs, seen, read);
        }

        public boolean deleteStoredMessage(java.lang.String callingPkg, android.net.Uri messageUri) throws android.os.RemoteException {
            if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(15, android.os.Binder.getCallingUid(), callingPkg, (java.lang.String) null, (java.lang.String) null) != 0) {
                return false;
            }
            return com.android.server.MmsServiceBroker.this.getServiceGuarded().deleteStoredMessage(callingPkg, messageUri);
        }

        public boolean deleteStoredConversation(java.lang.String callingPkg, long conversationId) throws android.os.RemoteException {
            if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(15, android.os.Binder.getCallingUid(), callingPkg, (java.lang.String) null, (java.lang.String) null) != 0) {
                return false;
            }
            return com.android.server.MmsServiceBroker.this.getServiceGuarded().deleteStoredConversation(callingPkg, conversationId);
        }

        public boolean updateStoredMessageStatus(java.lang.String callingPkg, android.net.Uri messageUri, android.content.ContentValues statusValues) throws android.os.RemoteException {
            if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(15, android.os.Binder.getCallingUid(), callingPkg, (java.lang.String) null, (java.lang.String) null) != 0) {
                return false;
            }
            return com.android.server.MmsServiceBroker.this.getServiceGuarded().updateStoredMessageStatus(callingPkg, messageUri, statusValues);
        }

        public boolean archiveStoredConversation(java.lang.String callingPkg, long conversationId, boolean archived) throws android.os.RemoteException {
            if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(15, android.os.Binder.getCallingUid(), callingPkg, (java.lang.String) null, (java.lang.String) null) != 0) {
                return false;
            }
            return com.android.server.MmsServiceBroker.this.getServiceGuarded().archiveStoredConversation(callingPkg, conversationId, archived);
        }

        public android.net.Uri addTextMessageDraft(java.lang.String callingPkg, java.lang.String address, java.lang.String text) throws android.os.RemoteException {
            if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(15, android.os.Binder.getCallingUid(), callingPkg, (java.lang.String) null, (java.lang.String) null) != 0) {
                return com.android.server.MmsServiceBroker.FAKE_SMS_DRAFT_URI;
            }
            return com.android.server.MmsServiceBroker.this.getServiceGuarded().addTextMessageDraft(callingPkg, address, text);
        }

        public android.net.Uri addMultimediaMessageDraft(java.lang.String callingPkg, android.net.Uri contentUri) throws android.os.RemoteException {
            if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(15, android.os.Binder.getCallingUid(), callingPkg, (java.lang.String) null, (java.lang.String) null) != 0) {
                return com.android.server.MmsServiceBroker.FAKE_MMS_DRAFT_URI;
            }
            return com.android.server.MmsServiceBroker.this.getServiceGuarded().addMultimediaMessageDraft(callingPkg, contentUri);
        }

        public void sendStoredMessage(int subId, java.lang.String callingPkg, android.net.Uri messageUri, android.os.Bundle configOverrides, android.app.PendingIntent sentIntent) throws android.os.RemoteException {
            if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(20, android.os.Binder.getCallingUid(), callingPkg, (java.lang.String) null, (java.lang.String) null) != 0) {
                return;
            }
            com.android.server.MmsServiceBroker.this.getServiceGuarded().sendStoredMessage(subId, callingPkg, messageUri, configOverrides, sentIntent);
        }

        public void setAutoPersisting(java.lang.String callingPkg, boolean enabled) throws android.os.RemoteException {
            if (com.android.server.MmsServiceBroker.this.getAppOpsManager().noteOp(15, android.os.Binder.getCallingUid(), callingPkg, (java.lang.String) null, (java.lang.String) null) != 0) {
                return;
            }
            com.android.server.MmsServiceBroker.this.getServiceGuarded().setAutoPersisting(callingPkg, enabled);
        }

        public boolean getAutoPersisting() throws android.os.RemoteException {
            return com.android.server.MmsServiceBroker.this.getServiceGuarded().getAutoPersisting();
        }

        private android.net.Uri adjustUriForUserAndGrantPermission(android.net.Uri contentUri, java.lang.String action, int permission, int subId) throws java.lang.Throwable {
            android.net.Uri contentUri2;
            android.content.Intent grantIntent = new android.content.Intent();
            grantIntent.setData(contentUri);
            grantIntent.setFlags(permission);
            int callingUid = android.os.Binder.getCallingUid();
            int callingUserId = android.os.UserHandle.getCallingUserId();
            if (callingUserId == 0) {
                contentUri2 = contentUri;
            } else {
                contentUri2 = android.content.ContentProvider.maybeAddUserId(contentUri, callingUserId);
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.uri.UriGrantsManagerInternal ugm = (com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class);
                com.android.server.uri.NeededUriGrants needed = ugm.checkGrantUriPermissionFromIntent(grantIntent, callingUid, PHONE_PACKAGE_NAME, 0);
                ugm.grantUriPermissionUncheckedFromIntent(needed, null);
                try {
                    android.content.Intent intent = new android.content.Intent(action);
                    android.telephony.TelephonyManager telephonyManager = (android.telephony.TelephonyManager) com.android.server.MmsServiceBroker.this.mContext.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
                    try {
                        java.util.List<java.lang.String> carrierPackages = telephonyManager.getCarrierPackageNamesForIntentAndPhone(intent, com.android.server.MmsServiceBroker.this.getPhoneIdFromSubId(subId));
                        if (carrierPackages != null && carrierPackages.size() == 1) {
                            com.android.server.uri.NeededUriGrants carrierNeeded = ugm.checkGrantUriPermissionFromIntent(grantIntent, callingUid, carrierPackages.get(0), 0);
                            ugm.grantUriPermissionUncheckedFromIntent(carrierNeeded, null);
                        }
                        android.os.Binder.restoreCallingIdentity(token);
                        return contentUri2;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(token);
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    android.os.Binder.restoreCallingIdentity(token);
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0018  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean isActiveSubId(int r5) {
        /*
            r4 = this;
            long r0 = android.os.Binder.clearCallingIdentity()
            android.content.Context r2 = r4.mContext     // Catch: java.lang.Throwable -> L1d
            java.lang.Class<android.telephony.SubscriptionManager> r3 = android.telephony.SubscriptionManager.class
            java.lang.Object r2 = r2.getSystemService(r3)     // Catch: java.lang.Throwable -> L1d
            android.telephony.SubscriptionManager r2 = (android.telephony.SubscriptionManager) r2     // Catch: java.lang.Throwable -> L1d
            if (r2 == 0) goto L18
            boolean r3 = r2.isActiveSubscriptionId(r5)     // Catch: java.lang.Throwable -> L1d
            if (r3 == 0) goto L18
            r3 = 1
            goto L19
        L18:
            r3 = 0
        L19:
            android.os.Binder.restoreCallingIdentity(r0)
            return r3
        L1d:
            r2 = move-exception
            android.os.Binder.restoreCallingIdentity(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.MmsServiceBroker.isActiveSubId(int):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPhoneIdFromSubId(int subId) {
        android.telephony.SubscriptionInfo info;
        android.telephony.SubscriptionManager subManager = (android.telephony.SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        if (subManager == null || (info = subManager.getActiveSubscriptionInfo(subId)) == null) {
            return -1;
        }
        return info.getSimSlotIndex();
    }
}
