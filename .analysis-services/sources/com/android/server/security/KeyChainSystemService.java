package com.android.server.security;

/* JADX INFO: loaded from: classes3.dex */
public class KeyChainSystemService extends com.android.server.SystemService {
    private static final int KEYCHAIN_IDLE_ALLOWLIST_DURATION_MS = 30000;
    private static final java.lang.String TAG = "KeyChainSystemService";
    private final android.content.BroadcastReceiver mPackageReceiver;

    public KeyChainSystemService(android.content.Context context) {
        super(context);
        this.mPackageReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.security.KeyChainSystemService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent broadcastIntent) {
                if (broadcastIntent.getPackage() != null) {
                    return;
                }
                try {
                    android.content.Intent intent = new android.content.Intent(android.security.IKeyChainService.class.getName());
                    android.content.ComponentName service = intent.resolveSystemService(com.android.server.security.KeyChainSystemService.this.getContext().getPackageManager(), 0);
                    if (service == null) {
                        return;
                    }
                    intent.setComponent(service);
                    intent.setAction(broadcastIntent.getAction());
                    com.android.server.security.KeyChainSystemService.this.startServiceInBackgroundAsUser(intent, android.os.UserHandle.of(getSendingUserId()));
                } catch (java.lang.RuntimeException e) {
                    android.util.Slog.e(com.android.server.security.KeyChainSystemService.TAG, "Unable to forward package removed broadcast to KeyChain", e);
                }
            }
        };
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        android.content.IntentFilter packageFilter = new android.content.IntentFilter("android.intent.action.PACKAGE_REMOVED");
        packageFilter.addDataScheme("package");
        try {
            getContext().registerReceiverAsUser(this.mPackageReceiver, android.os.UserHandle.ALL, packageFilter, null, null);
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.w(TAG, "Unable to register for package removed broadcast", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startServiceInBackgroundAsUser(android.content.Intent intent, android.os.UserHandle user) {
        if (intent.getComponent() == null) {
            return;
        }
        java.lang.String packageName = intent.getComponent().getPackageName();
        com.android.server.DeviceIdleInternal idleController = (com.android.server.DeviceIdleInternal) com.android.server.LocalServices.getService(com.android.server.DeviceIdleInternal.class);
        idleController.addPowerSaveTempWhitelistApp(android.os.Process.myUid(), packageName, 30000L, user.getIdentifier(), false, com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_KEY_CHAIN, "keychain");
        getContext().startServiceAsUser(intent, user);
    }
}
