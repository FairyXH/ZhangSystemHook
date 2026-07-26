package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public class CertificateMonitor {
    protected static final int MONITORING_CERT_NOTIFICATION_ID = 33;
    private final android.os.Handler mHandler;
    private final com.android.server.devicepolicy.DevicePolicyManagerService.Injector mInjector;
    private final android.content.BroadcastReceiver mRootCaReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.devicepolicy.CertificateMonitor.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            int userId = intent.getIntExtra("android.intent.extra.user_handle", getSendingUserId());
            com.android.server.devicepolicy.CertificateMonitor.this.updateInstalledCertificates(android.os.UserHandle.of(userId));
        }
    };
    private final com.android.server.devicepolicy.DevicePolicyManagerService mService;

    public CertificateMonitor(com.android.server.devicepolicy.DevicePolicyManagerService service, com.android.server.devicepolicy.DevicePolicyManagerService.Injector injector, android.os.Handler handler) {
        this.mService = service;
        this.mInjector = injector;
        this.mHandler = handler;
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.USER_STARTED");
        filter.addAction("android.intent.action.USER_UNLOCKED");
        filter.addAction("android.security.action.TRUST_STORE_CHANGED");
        filter.setPriority(1000);
        this.mInjector.mContext.registerReceiverAsUser(this.mRootCaReceiver, android.os.UserHandle.ALL, filter, null, this.mHandler);
    }

    public java.lang.String installCaCert(android.os.UserHandle userHandle, byte[] certBuffer) {
        try {
            java.security.cert.X509Certificate cert = parseCert(certBuffer);
            byte[] pemCert = android.security.Credentials.convertToPem(new java.security.cert.Certificate[]{cert});
            try {
                android.security.KeyChain.KeyChainConnection keyChainConnection = this.mInjector.keyChainBindAsUser(userHandle);
                try {
                    java.lang.String strInstallCaCertificate = keyChainConnection.getService().installCaCertificate(pemCert);
                    if (keyChainConnection != null) {
                        keyChainConnection.close();
                    }
                    return strInstallCaCertificate;
                } finally {
                }
            } catch (android.os.RemoteException e) {
                com.android.server.utils.Slogf.e("DevicePolicyManager", "installCaCertsToKeyChain(): ", e);
                return null;
            } catch (java.lang.InterruptedException e1) {
                com.android.server.utils.Slogf.w("DevicePolicyManager", "installCaCertsToKeyChain(): ", e1);
                java.lang.Thread.currentThread().interrupt();
                return null;
            }
        } catch (java.io.IOException | java.security.cert.CertificateException ce) {
            com.android.server.utils.Slogf.e("DevicePolicyManager", "Problem converting cert", ce);
            return null;
        }
    }

    public void uninstallCaCerts(android.os.UserHandle userHandle, java.lang.String[] aliases) {
        try {
            android.security.KeyChain.KeyChainConnection keyChainConnection = this.mInjector.keyChainBindAsUser(userHandle);
            for (java.lang.String str : aliases) {
                try {
                    keyChainConnection.getService().deleteCaCertificate(str);
                } catch (java.lang.Throwable th) {
                    if (keyChainConnection != null) {
                        try {
                            keyChainConnection.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            if (keyChainConnection != null) {
                keyChainConnection.close();
            }
        } catch (android.os.RemoteException e) {
            com.android.server.utils.Slogf.e("DevicePolicyManager", "from CaCertUninstaller: ", e);
        } catch (java.lang.InterruptedException ie) {
            com.android.server.utils.Slogf.w("DevicePolicyManager", "CaCertUninstaller: ", ie);
            java.lang.Thread.currentThread().interrupt();
        }
    }

    private java.util.List<java.lang.String> getInstalledCaCertificates(android.os.UserHandle userHandle) throws android.os.RemoteException, java.lang.RuntimeException {
        try {
            android.security.KeyChain.KeyChainConnection conn = this.mInjector.keyChainBindAsUser(userHandle);
            try {
                java.util.List<java.lang.String> list = conn.getService().getUserCaAliases().getList();
                if (conn != null) {
                    conn.close();
                }
                return list;
            } catch (java.lang.Throwable th) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (java.lang.AssertionError e) {
            throw new java.lang.RuntimeException(e);
        } catch (java.lang.InterruptedException e2) {
            java.lang.Thread.currentThread().interrupt();
            throw new java.lang.RuntimeException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCertificateApprovalsChanged$0(int userId) {
        updateInstalledCertificates(android.os.UserHandle.of(userId));
    }

    public void onCertificateApprovalsChanged(final int userId) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.devicepolicy.CertificateMonitor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onCertificateApprovalsChanged$0(userId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInstalledCertificates(android.os.UserHandle userHandle) {
        int userId = userHandle.getIdentifier();
        if (!this.mInjector.getUserManager().isUserUnlocked(userId)) {
            return;
        }
        try {
            java.util.List<java.lang.String> installedCerts = getInstalledCaCertificates(userHandle);
            this.mService.onInstalledCertificatesChanged(userHandle, installedCerts);
            int pendingCertificateCount = installedCerts.size() - this.mService.getAcceptedCaCertificates(userHandle).size();
            if (pendingCertificateCount == 0) {
                this.mInjector.getNotificationManager().cancelAsUser("DevicePolicyManager", 33, userHandle);
            } else {
                android.app.Notification noti = buildNotification(userHandle, pendingCertificateCount);
                this.mInjector.getNotificationManager().notifyAsUser("DevicePolicyManager", 33, noti, userHandle);
            }
        } catch (android.os.RemoteException | java.lang.RuntimeException e) {
            com.android.server.utils.Slogf.e("DevicePolicyManager", e, "Could not retrieve certificates from KeyChain service for user %d", java.lang.Integer.valueOf(userId));
        }
    }

    private android.app.Notification buildNotification(android.os.UserHandle userHandle, int pendingCertificateCount) {
        int parentUserId;
        java.lang.String contentText;
        int smallIconId;
        try {
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e = e;
        }
        try {
            android.content.Context userContext = this.mInjector.createContextAsUser(userHandle);
            android.content.res.Resources resources = this.mInjector.getResources();
            int parentUserId2 = userHandle.getIdentifier();
            if (this.mService.lambda$isProfileOwner$75(userHandle.getIdentifier()) != null) {
                java.lang.String contentText2 = resources.getString(android.R.string.serviceErased, this.mService.getProfileOwnerName(userHandle.getIdentifier()));
                parentUserId = this.mService.getProfileParentId(userHandle.getIdentifier());
                contentText = contentText2;
                smallIconId = 17303916;
            } else if (this.mService.getDeviceOwnerUserId() == userHandle.getIdentifier()) {
                java.lang.String contentText3 = resources.getString(android.R.string.serviceErased, this.mService.getDeviceOwnerName());
                parentUserId = parentUserId2;
                contentText = contentText3;
                smallIconId = 17303916;
            } else {
                java.lang.String contentText4 = resources.getString(android.R.string.serviceEnabledFor);
                parentUserId = parentUserId2;
                contentText = contentText4;
                smallIconId = 17301642;
            }
            android.content.Intent dialogIntent = new android.content.Intent("com.android.settings.MONITORING_CERT_INFO");
            dialogIntent.setFlags(268468224);
            dialogIntent.putExtra("android.settings.extra.number_of_certificates", pendingCertificateCount);
            dialogIntent.putExtra("android.intent.extra.USER_ID", userHandle.getIdentifier());
            android.content.pm.ActivityInfo targetInfo = dialogIntent.resolveActivityInfo(this.mInjector.getPackageManager(), 1048576);
            if (targetInfo != null) {
                dialogIntent.setComponent(targetInfo.getComponentName());
            }
            android.app.PendingIntent notifyIntent = this.mInjector.pendingIntentGetActivityAsUser(userContext, 0, dialogIntent, android.hardware.audio.common.V2_0.AudioFormat.DTS_HD, null, android.os.UserHandle.of(parentUserId));
            java.util.Map<java.lang.String, java.lang.Object> arguments = new java.util.HashMap<>();
            arguments.put(com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, java.lang.Integer.valueOf(pendingCertificateCount));
            return new android.app.Notification.Builder(userContext, com.android.internal.notification.SystemNotificationChannels.SECURITY).setSmallIcon(smallIconId).setContentTitle(android.util.PluralsMessageFormatter.format(resources, arguments, android.R.string.serviceNotProvisioned)).setContentText(contentText).setContentIntent(notifyIntent).setShowWhen(false).setColor(android.R.color.system_notification_accent_color).build();
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            e = e2;
            com.android.server.utils.Slogf.e("DevicePolicyManager", e, "Create context as %s failed", userHandle);
            return null;
        }
    }

    private static java.security.cert.X509Certificate parseCert(byte[] certBuffer) throws java.security.cert.CertificateException {
        java.security.cert.CertificateFactory certFactory = java.security.cert.CertificateFactory.getInstance("X.509");
        return (java.security.cert.X509Certificate) certFactory.generateCertificate(new java.io.ByteArrayInputStream(certBuffer));
    }
}
