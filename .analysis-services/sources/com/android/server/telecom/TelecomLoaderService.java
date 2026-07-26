package com.android.server.telecom;

/* JADX INFO: loaded from: classes3.dex */
public class TelecomLoaderService extends com.android.server.SystemService {
    private static final java.lang.String SERVICE_ACTION = "com.android.ITelecomService";
    private static final android.content.ComponentName SERVICE_COMPONENT = new android.content.ComponentName("com.android.server.telecom", "com.android.server.telecom.components.TelecomService");
    private static final java.lang.String TAG = "TelecomLoaderService";
    private final android.content.Context mContext;
    private android.util.IntArray mDefaultSimCallManagerRequests;
    private com.android.server.telecom.ITelecomLoaderServiceExt mExt;
    private final java.lang.Object mLock;
    private com.android.server.telecom.TelecomLoaderService.TelecomServiceConnection mServiceConnection;
    private com.android.server.telecom.InternalServiceRepository mServiceRepo;

    private class TelecomServiceConnection implements android.content.ServiceConnection {
        private TelecomServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) throws java.lang.Exception {
            try {
                com.android.internal.telecom.ITelecomLoader telecomLoader = com.android.internal.telecom.ITelecomLoader.Stub.asInterface(service);
                com.android.internal.telecom.ITelecomService telecomService = telecomLoader.createTelecomService(com.android.server.telecom.TelecomLoaderService.this.mServiceRepo);
                com.android.internal.telephony.SmsApplication.getDefaultMmsApplication(com.android.server.telecom.TelecomLoaderService.this.mContext, false);
                android.os.ServiceManager.addService("telecom", telecomService.asBinder());
                synchronized (com.android.server.telecom.TelecomLoaderService.this.mLock) {
                    com.android.server.pm.permission.LegacyPermissionManagerInternal permissionManager = (com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class);
                    if (com.android.server.telecom.TelecomLoaderService.this.mDefaultSimCallManagerRequests != null) {
                        android.telecom.TelecomManager telecomManager = (android.telecom.TelecomManager) com.android.server.telecom.TelecomLoaderService.this.mContext.getSystemService("telecom");
                        android.telecom.PhoneAccountHandle phoneAccount = telecomManager.getSimCallManager();
                        if (phoneAccount != null) {
                            int requestCount = com.android.server.telecom.TelecomLoaderService.this.mDefaultSimCallManagerRequests.size();
                            java.lang.String packageName = phoneAccount.getComponentName().getPackageName();
                            for (int i = requestCount - 1; i >= 0; i--) {
                                int userId = com.android.server.telecom.TelecomLoaderService.this.mDefaultSimCallManagerRequests.get(i);
                                com.android.server.telecom.TelecomLoaderService.this.mDefaultSimCallManagerRequests.remove(i);
                                permissionManager.grantDefaultPermissionsToDefaultSimCallManager(packageName, userId);
                            }
                        }
                    }
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.telecom.TelecomLoaderService.TAG, "Failed linking to death.");
            } catch (java.lang.Exception e2) {
                boolean isVSOC = android.os.SystemProperties.get("ro.soc.model").equals("vsoc_arm64");
                if (isVSOC) {
                    e2.printStackTrace();
                    return;
                }
                throw e2;
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            com.android.server.telecom.TelecomLoaderService.this.connectToTelecom();
        }
    }

    public TelecomLoaderService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mContext = context;
        this.mExt = (com.android.server.telecom.ITelecomLoaderServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.telecom.ITelecomLoaderServiceExt.class).base(this).create();
        registerDefaultAppProviders();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 550) {
            registerDefaultAppNotifier();
            registerCarrierConfigChangedReceiver();
            setupServiceRepository();
            connectToTelecom();
            this.mExt.connectToTelecomExt(this.mContext);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectToTelecom() {
        synchronized (this.mLock) {
            if (this.mServiceConnection != null) {
                this.mContext.unbindService(this.mServiceConnection);
                this.mServiceConnection = null;
            }
            com.android.server.telecom.TelecomLoaderService.TelecomServiceConnection serviceConnection = new com.android.server.telecom.TelecomLoaderService.TelecomServiceConnection();
            android.content.Intent intent = new android.content.Intent(SERVICE_ACTION);
            intent.setComponent(SERVICE_COMPONENT);
            if (this.mContext.bindServiceAsUser(intent, serviceConnection, 67108929, android.os.UserHandle.SYSTEM)) {
                this.mServiceConnection = serviceConnection;
            }
        }
    }

    private void setupServiceRepository() {
        com.android.server.DeviceIdleInternal deviceIdleInternal = (com.android.server.DeviceIdleInternal) getLocalService(com.android.server.DeviceIdleInternal.class);
        this.mServiceRepo = new com.android.server.telecom.InternalServiceRepository(deviceIdleInternal);
    }

    private void registerDefaultAppProviders() {
        com.android.server.pm.permission.LegacyPermissionManagerInternal permissionManager = (com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class);
        permissionManager.setSmsAppPackagesProvider(new com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider() { // from class: com.android.server.telecom.TelecomLoaderService$$ExternalSyntheticLambda0
            @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider
            public final java.lang.String[] getPackages(int i) {
                return this.f$0.lambda$registerDefaultAppProviders$0(i);
            }
        });
        permissionManager.setDialerAppPackagesProvider(new com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider() { // from class: com.android.server.telecom.TelecomLoaderService$$ExternalSyntheticLambda1
            @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider
            public final java.lang.String[] getPackages(int i) {
                return this.f$0.lambda$registerDefaultAppProviders$1(i);
            }
        });
        permissionManager.setSimCallManagerPackagesProvider(new com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider() { // from class: com.android.server.telecom.TelecomLoaderService$$ExternalSyntheticLambda2
            @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider
            public final java.lang.String[] getPackages(int i) {
                return this.f$0.lambda$registerDefaultAppProviders$2(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String[] lambda$registerDefaultAppProviders$0(int userId) {
        synchronized (this.mLock) {
            if (this.mServiceConnection == null) {
                return null;
            }
            android.content.ComponentName smsComponent = com.android.internal.telephony.SmsApplication.getDefaultSmsApplication(this.mContext, true);
            if (smsComponent != null) {
                return new java.lang.String[]{smsComponent.getPackageName()};
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String[] lambda$registerDefaultAppProviders$1(int userId) {
        synchronized (this.mLock) {
            if (this.mServiceConnection == null) {
                return null;
            }
            java.lang.String packageName = android.telecom.DefaultDialerManager.getDefaultDialerApplication(this.mContext);
            if (packageName != null) {
                return new java.lang.String[]{packageName};
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String[] lambda$registerDefaultAppProviders$2(int userId) {
        synchronized (this.mLock) {
            if (this.mServiceConnection == null) {
                if (this.mDefaultSimCallManagerRequests == null) {
                    this.mDefaultSimCallManagerRequests = new android.util.IntArray();
                }
                this.mDefaultSimCallManagerRequests.add(userId);
                return null;
            }
            android.telephony.SubscriptionManager subscriptionManager = (android.telephony.SubscriptionManager) this.mContext.getSystemService(android.telephony.SubscriptionManager.class);
            if (subscriptionManager == null) {
                return null;
            }
            android.telecom.TelecomManager telecomManager = (android.telecom.TelecomManager) this.mContext.getSystemService("telecom");
            java.util.List<java.lang.String> packages = new java.util.ArrayList<>();
            int[] subIds = subscriptionManager.getActiveSubscriptionIdList();
            for (int subId : subIds) {
                android.telecom.PhoneAccountHandle phoneAccount = telecomManager.getSimCallManagerForSubscription(subId);
                if (phoneAccount != null) {
                    packages.add(phoneAccount.getComponentName().getPackageName());
                }
            }
            return (java.lang.String[]) packages.toArray(new java.lang.String[0]);
        }
    }

    private void registerDefaultAppNotifier() {
        android.app.role.RoleManager roleManager = (android.app.role.RoleManager) this.mContext.getSystemService(android.app.role.RoleManager.class);
        roleManager.addOnRoleHoldersChangedListenerAsUser(this.mContext.getMainExecutor(), new android.app.role.OnRoleHoldersChangedListener() { // from class: com.android.server.telecom.TelecomLoaderService$$ExternalSyntheticLambda3
            public final void onRoleHoldersChanged(java.lang.String str, android.os.UserHandle userHandle) {
                this.f$0.lambda$registerDefaultAppNotifier$3(str, userHandle);
            }
        }, android.os.UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerDefaultAppNotifier$3(java.lang.String roleName, android.os.UserHandle user) {
        updateSimCallManagerPermissions(user.getIdentifier());
    }

    private void registerCarrierConfigChangedReceiver() {
        android.content.BroadcastReceiver receiver = new android.content.BroadcastReceiver() { // from class: com.android.server.telecom.TelecomLoaderService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if (intent.getAction().equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
                    for (int userId : com.android.server.pm.UserManagerService.getInstance().getUserIds()) {
                        com.android.server.telecom.TelecomLoaderService.this.updateSimCallManagerPermissions(userId);
                    }
                }
            }
        };
        this.mContext.registerReceiverAsUser(receiver, android.os.UserHandle.ALL, new android.content.IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"), null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSimCallManagerPermissions(int userId) {
        com.android.server.pm.permission.LegacyPermissionManagerInternal permissionManager = (com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class);
        android.telecom.TelecomManager telecomManager = (android.telecom.TelecomManager) this.mContext.getSystemService("telecom");
        android.telecom.PhoneAccountHandle phoneAccount = telecomManager.getSimCallManager(userId);
        if (phoneAccount != null) {
            android.util.Slog.i(TAG, "updating sim call manager permissions for userId:" + userId);
            java.lang.String packageName = phoneAccount.getComponentName().getPackageName();
            permissionManager.grantDefaultPermissionsToDefaultSimCallManager(packageName, userId);
        }
    }
}
