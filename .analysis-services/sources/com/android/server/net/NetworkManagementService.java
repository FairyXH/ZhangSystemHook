package com.android.server.net;

/* JADX INFO: loaded from: classes2.dex */
public class NetworkManagementService extends android.os.INetworkManagementService.Stub {
    private static final int DATASAVE_FLAG = 64;
    private static final int DOZABLE_FLAG = 2;
    private static final int LOW_POWER_STANDBY_FLAG = 8;
    private static final int METERED_FLAG = 32;
    private static final int POWERSAVE_FLAG = 4;
    private static final int REJECT_BACKGROUND_FLAG = 128;
    private static final int RESTRICTED_FLAG = 16;
    private static final int STANDBY_FLAG = 1;
    private java.util.HashMap<java.lang.String, java.lang.Long> mActiveAlerts;
    private java.util.HashMap<java.lang.String, java.lang.Long> mActiveQuotas;
    private com.android.internal.app.IBatteryStats mBatteryStats;
    private final android.content.Context mContext;
    private final android.os.Handler mDaemonHandler;
    private volatile boolean mDataSaverMode;
    private final com.android.server.net.NetworkManagementService.Dependencies mDeps;
    final android.util.SparseBooleanArray mFirewallChainStates;
    private volatile boolean mFirewallEnabled;
    private android.net.INetd mNetdService;
    private final com.android.server.net.NetworkManagementService.NetdUnsolicitedEventListener mNetdUnsolicitedEventListener;
    private final android.os.RemoteCallbackList<android.net.INetworkManagementEventObserver> mObservers;
    private android.net.NetworkPolicyManager mPolicyManager;
    private final java.lang.Object mQuotaLock;
    private final java.lang.Object mRulesLock;
    private volatile boolean mStrictEnabled;
    private android.util.SparseBooleanArray mUidAllowOnMetered;
    private android.util.SparseIntArray mUidCleartextPolicy;
    private final android.util.SparseIntArray mUidFirewallBackgroundRules;
    private final android.util.SparseIntArray mUidFirewallDozableRules;
    private final android.util.SparseIntArray mUidFirewallLowPowerStandbyRules;
    private final android.util.SparseIntArray mUidFirewallPowerSaveRules;
    private final android.util.SparseIntArray mUidFirewallRestrictedRules;
    private final android.util.SparseIntArray mUidFirewallRules;
    private final android.util.SparseIntArray mUidFirewallStandbyRules;
    private final android.util.SparseIntArray mUidMeteredFirewallAllowRules;
    private final android.util.SparseIntArray mUidMeteredFirewallDenyAdminRules;
    private final android.util.SparseIntArray mUidMeteredFirewallDenyUserRules;
    private android.util.SparseBooleanArray mUidRejectOnMetered;
    private final boolean mUseMeteredFirewallChains;
    private static final java.lang.String TAG = "NetworkManagement";
    private static final boolean DBG = android.util.Log.isLoggable(TAG, 3);

    /* JADX INFO: Access modifiers changed from: private */
    @java.lang.FunctionalInterface
    interface NetworkManagementEventCallback {
        void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) throws android.os.RemoteException;
    }

    static class Dependencies {
        Dependencies() {
        }

        public android.os.IBinder getService(java.lang.String name) {
            return android.os.ServiceManager.getService(name);
        }

        public void registerLocalService(com.android.server.net.NetworkManagementInternal nmi) {
            com.android.server.LocalServices.addService(com.android.server.net.NetworkManagementInternal.class, nmi);
        }

        public android.net.INetd getNetd() {
            return android.net.util.NetdService.get();
        }

        public int getCallingUid() {
            return android.os.Binder.getCallingUid();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private NetworkManagementService(android.content.Context context, com.android.server.net.NetworkManagementService.Dependencies dependencies) {
        super(android.os.PermissionEnforcer.fromContext(context));
        java.lang.Object[] objArr = 0;
        this.mPolicyManager = null;
        this.mObservers = new android.os.RemoteCallbackList<>();
        this.mQuotaLock = new java.lang.Object();
        this.mRulesLock = new java.lang.Object();
        this.mActiveQuotas = com.google.android.collect.Maps.newHashMap();
        this.mActiveAlerts = com.google.android.collect.Maps.newHashMap();
        this.mUidRejectOnMetered = new android.util.SparseBooleanArray();
        this.mUidAllowOnMetered = new android.util.SparseBooleanArray();
        this.mUidCleartextPolicy = new android.util.SparseIntArray();
        this.mUidFirewallRules = new android.util.SparseIntArray();
        this.mUidFirewallStandbyRules = new android.util.SparseIntArray();
        this.mUidFirewallDozableRules = new android.util.SparseIntArray();
        this.mUidFirewallPowerSaveRules = new android.util.SparseIntArray();
        this.mUidFirewallRestrictedRules = new android.util.SparseIntArray();
        this.mUidFirewallLowPowerStandbyRules = new android.util.SparseIntArray();
        this.mUidFirewallBackgroundRules = new android.util.SparseIntArray();
        this.mUidMeteredFirewallAllowRules = new android.util.SparseIntArray();
        this.mUidMeteredFirewallDenyUserRules = new android.util.SparseIntArray();
        this.mUidMeteredFirewallDenyAdminRules = new android.util.SparseIntArray();
        this.mFirewallChainStates = new android.util.SparseBooleanArray();
        this.mContext = context;
        this.mDeps = dependencies;
        this.mUseMeteredFirewallChains = com.android.server.net.Flags.useMeteredFirewallChains();
        if (this.mUseMeteredFirewallChains) {
            this.mFirewallChainStates.put(11, true);
            this.mFirewallChainStates.put(12, true);
        }
        this.mDaemonHandler = new android.os.Handler(com.android.server.FgThread.get().getLooper());
        this.mNetdUnsolicitedEventListener = new com.android.server.net.NetworkManagementService.NetdUnsolicitedEventListener();
        this.mDeps.registerLocalService(new com.android.server.net.NetworkManagementService.LocalService());
    }

    static com.android.server.net.NetworkManagementService create(android.content.Context context, com.android.server.net.NetworkManagementService.Dependencies deps) throws java.lang.InterruptedException {
        com.android.server.net.NetworkManagementService service = new com.android.server.net.NetworkManagementService(context, deps);
        if (DBG) {
            android.util.Slog.d(TAG, "Creating NetworkManagementService");
        }
        if (DBG) {
            android.util.Slog.d(TAG, "Connecting native netd service");
        }
        service.connectNativeNetdService();
        if (DBG) {
            android.util.Slog.d(TAG, "Connected");
        }
        return service;
    }

    public static com.android.server.net.NetworkManagementService create(android.content.Context context) throws java.lang.InterruptedException {
        return create(context, new com.android.server.net.NetworkManagementService.Dependencies());
    }

    public void systemReady() {
        if (DBG) {
            long start = java.lang.System.currentTimeMillis();
            prepareNativeDaemon();
            long delta = java.lang.System.currentTimeMillis() - start;
            android.util.Slog.d(TAG, "Prepared in " + delta + "ms");
            return;
        }
        prepareNativeDaemon();
    }

    private com.android.internal.app.IBatteryStats getBatteryStats() {
        synchronized (this) {
            if (this.mBatteryStats != null) {
                return this.mBatteryStats;
            }
            this.mBatteryStats = com.android.internal.app.IBatteryStats.Stub.asInterface(this.mDeps.getService("batterystats"));
            return this.mBatteryStats;
        }
    }

    public void registerObserver(android.net.INetworkManagementEventObserver observer) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        this.mObservers.register(observer);
    }

    public void unregisterObserver(android.net.INetworkManagementEventObserver observer) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        this.mObservers.unregister(observer);
    }

    private synchronized void invokeForAllObservers(com.android.server.net.NetworkManagementService.NetworkManagementEventCallback eventCallback) {
        int length = this.mObservers.beginBroadcast();
        for (int i = 0; i < length; i++) {
            try {
                try {
                    try {
                        eventCallback.sendCallback((android.net.INetworkManagementEventObserver) this.mObservers.getBroadcastItem(i));
                    } catch (android.os.RemoteException | java.lang.RuntimeException e) {
                    }
                } catch (android.os.RemoteException | java.lang.RuntimeException e2) {
                }
            } finally {
                this.mObservers.finishBroadcast();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyInterfaceStatusChanged(final java.lang.String iface, final boolean up) {
        invokeForAllObservers(new com.android.server.net.NetworkManagementService.NetworkManagementEventCallback() { // from class: com.android.server.net.NetworkManagementService$$ExternalSyntheticLambda2
            @Override // com.android.server.net.NetworkManagementService.NetworkManagementEventCallback
            public final void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) {
                iNetworkManagementEventObserver.interfaceStatusChanged(iface, up);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyInterfaceLinkStateChanged(final java.lang.String iface, final boolean up) {
        invokeForAllObservers(new com.android.server.net.NetworkManagementService.NetworkManagementEventCallback() { // from class: com.android.server.net.NetworkManagementService$$ExternalSyntheticLambda4
            @Override // com.android.server.net.NetworkManagementService.NetworkManagementEventCallback
            public final void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) {
                iNetworkManagementEventObserver.interfaceLinkStateChanged(iface, up);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyInterfaceAdded(final java.lang.String iface) {
        invokeForAllObservers(new com.android.server.net.NetworkManagementService.NetworkManagementEventCallback() { // from class: com.android.server.net.NetworkManagementService$$ExternalSyntheticLambda0
            @Override // com.android.server.net.NetworkManagementService.NetworkManagementEventCallback
            public final void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) {
                iNetworkManagementEventObserver.interfaceAdded(iface);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyInterfaceRemoved(final java.lang.String iface) {
        this.mActiveAlerts.remove(iface);
        this.mActiveQuotas.remove(iface);
        invokeForAllObservers(new com.android.server.net.NetworkManagementService.NetworkManagementEventCallback() { // from class: com.android.server.net.NetworkManagementService$$ExternalSyntheticLambda5
            @Override // com.android.server.net.NetworkManagementService.NetworkManagementEventCallback
            public final void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) {
                iNetworkManagementEventObserver.interfaceRemoved(iface);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyLimitReached(final java.lang.String limitName, final java.lang.String iface) {
        invokeForAllObservers(new com.android.server.net.NetworkManagementService.NetworkManagementEventCallback() { // from class: com.android.server.net.NetworkManagementService$$ExternalSyntheticLambda10
            @Override // com.android.server.net.NetworkManagementService.NetworkManagementEventCallback
            public final void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) {
                iNetworkManagementEventObserver.limitReached(limitName, iface);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyInterfaceClassActivity(final int label, final boolean isActive, final long tsNanos, final int uid) {
        invokeForAllObservers(new com.android.server.net.NetworkManagementService.NetworkManagementEventCallback() { // from class: com.android.server.net.NetworkManagementService$$ExternalSyntheticLambda1
            @Override // com.android.server.net.NetworkManagementService.NetworkManagementEventCallback
            public final void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) {
                iNetworkManagementEventObserver.interfaceClassDataActivityChanged(label, isActive, tsNanos, uid);
            }
        });
    }

    private void syncFirewallChainLocked(int chain, java.lang.String name) {
        android.util.SparseIntArray rules;
        synchronized (this.mRulesLock) {
            android.util.SparseIntArray uidFirewallRules = getUidFirewallRulesLR(chain);
            rules = uidFirewallRules.clone();
            uidFirewallRules.clear();
        }
        if (rules.size() > 0) {
            if (DBG) {
                android.util.Slog.d(TAG, "Pushing " + rules.size() + " active firewall " + name + "UID rules");
            }
            for (int i = 0; i < rules.size(); i++) {
                setFirewallUidRuleLocked(chain, rules.keyAt(i), rules.valueAt(i));
            }
        }
    }

    private void connectNativeNetdService() {
        this.mNetdService = this.mDeps.getNetd();
        try {
            this.mNetdService.registerUnsolicitedEventListener(this.mNetdUnsolicitedEventListener);
            if (DBG) {
                android.util.Slog.d(TAG, "Register unsolicited event listener");
            }
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            android.util.Slog.e(TAG, "Failed to set Netd unsolicited event listener " + e);
        }
    }

    private void prepareNativeDaemon() {
        synchronized (this.mQuotaLock) {
            this.mStrictEnabled = true;
            setDataSaverModeEnabled(this.mDataSaverMode);
            int size = this.mActiveQuotas.size();
            if (size > 0) {
                if (DBG) {
                    android.util.Slog.d(TAG, "Pushing " + size + " active quota rules");
                }
                java.util.HashMap<java.lang.String, java.lang.Long> activeQuotas = this.mActiveQuotas;
                this.mActiveQuotas = com.google.android.collect.Maps.newHashMap();
                for (java.util.Map.Entry<java.lang.String, java.lang.Long> entry : activeQuotas.entrySet()) {
                    setInterfaceQuota(entry.getKey(), entry.getValue().longValue());
                }
            }
            int size2 = this.mActiveAlerts.size();
            if (size2 > 0) {
                if (DBG) {
                    android.util.Slog.d(TAG, "Pushing " + size2 + " active alert rules");
                }
                java.util.HashMap<java.lang.String, java.lang.Long> activeAlerts = this.mActiveAlerts;
                this.mActiveAlerts = com.google.android.collect.Maps.newHashMap();
                for (java.util.Map.Entry<java.lang.String, java.lang.Long> entry2 : activeAlerts.entrySet()) {
                    setInterfaceAlert(entry2.getKey(), entry2.getValue().longValue());
                }
            }
            if (!this.mUseMeteredFirewallChains) {
                android.util.SparseBooleanArray uidRejectOnQuota = null;
                android.util.SparseBooleanArray uidAcceptOnQuota = null;
                synchronized (this.mRulesLock) {
                    int size3 = this.mUidRejectOnMetered.size();
                    if (size3 > 0) {
                        if (DBG) {
                            android.util.Slog.d(TAG, "Pushing " + size3 + " UIDs to metered denylist rules");
                        }
                        uidRejectOnQuota = this.mUidRejectOnMetered;
                        this.mUidRejectOnMetered = new android.util.SparseBooleanArray();
                    }
                    int size4 = this.mUidAllowOnMetered.size();
                    if (size4 > 0) {
                        if (DBG) {
                            android.util.Slog.d(TAG, "Pushing " + size4 + " UIDs to metered allowlist rules");
                        }
                        uidAcceptOnQuota = this.mUidAllowOnMetered;
                        this.mUidAllowOnMetered = new android.util.SparseBooleanArray();
                    }
                }
                if (uidRejectOnQuota != null) {
                    for (int i = 0; i < uidRejectOnQuota.size(); i++) {
                        setUidOnMeteredNetworkDenylist(uidRejectOnQuota.keyAt(i), uidRejectOnQuota.valueAt(i));
                    }
                }
                if (uidAcceptOnQuota != null) {
                    for (int i2 = 0; i2 < uidAcceptOnQuota.size(); i2++) {
                        setUidOnMeteredNetworkAllowlist(uidAcceptOnQuota.keyAt(i2), uidAcceptOnQuota.valueAt(i2));
                    }
                }
            }
            int size5 = this.mUidCleartextPolicy.size();
            if (size5 > 0) {
                if (DBG) {
                    android.util.Slog.d(TAG, "Pushing " + size5 + " active UID cleartext policies");
                }
                android.util.SparseIntArray local = this.mUidCleartextPolicy;
                this.mUidCleartextPolicy = new android.util.SparseIntArray();
                for (int i3 = 0; i3 < local.size(); i3++) {
                    setUidCleartextNetworkPolicy(local.keyAt(i3), local.valueAt(i3));
                }
            }
            setFirewallEnabled(this.mFirewallEnabled);
            syncFirewallChainLocked(0, "");
            syncFirewallChainLocked(2, "standby ");
            syncFirewallChainLocked(1, "dozable ");
            syncFirewallChainLocked(3, "powersave ");
            syncFirewallChainLocked(4, "restricted ");
            syncFirewallChainLocked(5, "low power standby ");
            syncFirewallChainLocked(6, "background");
            if (this.mUseMeteredFirewallChains) {
                syncFirewallChainLocked(10, "metered_allow");
                syncFirewallChainLocked(11, "metered_deny_user");
                syncFirewallChainLocked(12, "metered_deny_admin");
            }
            int[] chainsToEnable = {2, 1, 3, 4, 5, 6};
            for (int chain : chainsToEnable) {
                if (getFirewallChainState(chain)) {
                    setFirewallChainEnabled(chain, true);
                }
            }
        }
        try {
            getBatteryStats().noteNetworkStatsEnabled();
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAddressUpdated(final java.lang.String iface, final android.net.LinkAddress address) {
        invokeForAllObservers(new com.android.server.net.NetworkManagementService.NetworkManagementEventCallback() { // from class: com.android.server.net.NetworkManagementService$$ExternalSyntheticLambda7
            @Override // com.android.server.net.NetworkManagementService.NetworkManagementEventCallback
            public final void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) {
                iNetworkManagementEventObserver.addressUpdated(iface, address);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAddressRemoved(final java.lang.String iface, final android.net.LinkAddress address) {
        invokeForAllObservers(new com.android.server.net.NetworkManagementService.NetworkManagementEventCallback() { // from class: com.android.server.net.NetworkManagementService$$ExternalSyntheticLambda3
            @Override // com.android.server.net.NetworkManagementService.NetworkManagementEventCallback
            public final void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) {
                iNetworkManagementEventObserver.addressRemoved(iface, address);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyInterfaceDnsServerInfo(final java.lang.String iface, final long lifetime, final java.lang.String[] addresses) {
        invokeForAllObservers(new com.android.server.net.NetworkManagementService.NetworkManagementEventCallback() { // from class: com.android.server.net.NetworkManagementService$$ExternalSyntheticLambda6
            @Override // com.android.server.net.NetworkManagementService.NetworkManagementEventCallback
            public final void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) {
                iNetworkManagementEventObserver.interfaceDnsServerInfo(iface, lifetime, addresses);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyRouteChange(boolean updated, final android.net.RouteInfo route) {
        if (updated) {
            invokeForAllObservers(new com.android.server.net.NetworkManagementService.NetworkManagementEventCallback() { // from class: com.android.server.net.NetworkManagementService$$ExternalSyntheticLambda8
                @Override // com.android.server.net.NetworkManagementService.NetworkManagementEventCallback
                public final void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) {
                    iNetworkManagementEventObserver.routeUpdated(route);
                }
            });
        } else {
            invokeForAllObservers(new com.android.server.net.NetworkManagementService.NetworkManagementEventCallback() { // from class: com.android.server.net.NetworkManagementService$$ExternalSyntheticLambda9
                @Override // com.android.server.net.NetworkManagementService.NetworkManagementEventCallback
                public final void sendCallback(android.net.INetworkManagementEventObserver iNetworkManagementEventObserver) {
                    iNetworkManagementEventObserver.routeRemoved(route);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class NetdUnsolicitedEventListener extends android.net.INetdUnsolicitedEventListener.Stub {
        private NetdUnsolicitedEventListener() {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceClassActivityChanged(final boolean isActive, final int label, long timestamp, final int uid) throws android.os.RemoteException {
            long timestampNanos;
            if (timestamp <= 0) {
                timestampNanos = android.os.SystemClock.elapsedRealtimeNanos();
            } else {
                timestampNanos = timestamp;
            }
            final long j = timestampNanos;
            com.android.server.net.NetworkManagementService.this.mDaemonHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkManagementService$NetdUnsolicitedEventListener$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onInterfaceClassActivityChanged$0(label, isActive, j, uid);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInterfaceClassActivityChanged$0(int label, boolean isActive, long timestampNanos, int uid) {
            com.android.server.net.NetworkManagementService.this.notifyInterfaceClassActivity(label, isActive, timestampNanos, uid);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onQuotaLimitReached$1(java.lang.String alertName, java.lang.String ifName) {
            com.android.server.net.NetworkManagementService.this.notifyLimitReached(alertName, ifName);
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onQuotaLimitReached(final java.lang.String alertName, final java.lang.String ifName) throws android.os.RemoteException {
            com.android.server.net.NetworkManagementService.this.mDaemonHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkManagementService$NetdUnsolicitedEventListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onQuotaLimitReached$1(alertName, ifName);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInterfaceDnsServerInfo$2(java.lang.String ifName, long lifetime, java.lang.String[] servers) {
            com.android.server.net.NetworkManagementService.this.notifyInterfaceDnsServerInfo(ifName, lifetime, servers);
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceDnsServerInfo(final java.lang.String ifName, final long lifetime, final java.lang.String[] servers) throws android.os.RemoteException {
            com.android.server.net.NetworkManagementService.this.mDaemonHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkManagementService$NetdUnsolicitedEventListener$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onInterfaceDnsServerInfo$2(ifName, lifetime, servers);
                }
            });
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceAddressUpdated(java.lang.String addr, final java.lang.String ifName, int flags, int scope) throws android.os.RemoteException {
            final android.net.LinkAddress address = new android.net.LinkAddress(addr, flags, scope);
            com.android.server.net.NetworkManagementService.this.mDaemonHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkManagementService$NetdUnsolicitedEventListener$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onInterfaceAddressUpdated$3(ifName, address);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInterfaceAddressUpdated$3(java.lang.String ifName, android.net.LinkAddress address) {
            com.android.server.net.NetworkManagementService.this.notifyAddressUpdated(ifName, address);
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceAddressRemoved(java.lang.String addr, final java.lang.String ifName, int flags, int scope) throws android.os.RemoteException {
            final android.net.LinkAddress address = new android.net.LinkAddress(addr, flags, scope);
            com.android.server.net.NetworkManagementService.this.mDaemonHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkManagementService$NetdUnsolicitedEventListener$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onInterfaceAddressRemoved$4(ifName, address);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInterfaceAddressRemoved$4(java.lang.String ifName, android.net.LinkAddress address) {
            com.android.server.net.NetworkManagementService.this.notifyAddressRemoved(ifName, address);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInterfaceAdded$5(java.lang.String ifName) {
            com.android.server.net.NetworkManagementService.this.notifyInterfaceAdded(ifName);
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceAdded(final java.lang.String ifName) throws android.os.RemoteException {
            com.android.server.net.NetworkManagementService.this.mDaemonHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkManagementService$NetdUnsolicitedEventListener$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onInterfaceAdded$5(ifName);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInterfaceRemoved$6(java.lang.String ifName) {
            com.android.server.net.NetworkManagementService.this.notifyInterfaceRemoved(ifName);
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceRemoved(final java.lang.String ifName) throws android.os.RemoteException {
            com.android.server.net.NetworkManagementService.this.mDaemonHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkManagementService$NetdUnsolicitedEventListener$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onInterfaceRemoved$6(ifName);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInterfaceChanged$7(java.lang.String ifName, boolean up) {
            com.android.server.net.NetworkManagementService.this.notifyInterfaceStatusChanged(ifName, up);
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceChanged(final java.lang.String ifName, final boolean up) throws android.os.RemoteException {
            com.android.server.net.NetworkManagementService.this.mDaemonHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkManagementService$NetdUnsolicitedEventListener$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onInterfaceChanged$7(ifName, up);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onInterfaceLinkStateChanged$8(java.lang.String ifName, boolean up) {
            com.android.server.net.NetworkManagementService.this.notifyInterfaceLinkStateChanged(ifName, up);
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceLinkStateChanged(final java.lang.String ifName, final boolean up) throws android.os.RemoteException {
            com.android.server.net.NetworkManagementService.this.mDaemonHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkManagementService$NetdUnsolicitedEventListener$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onInterfaceLinkStateChanged$8(ifName, up);
                }
            });
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onRouteChanged(final boolean updated, java.lang.String route, java.lang.String gateway, java.lang.String ifName) throws android.os.RemoteException {
            final android.net.RouteInfo processRoute = new android.net.RouteInfo(new android.net.IpPrefix(route), "".equals(gateway) ? null : android.net.InetAddresses.parseNumericAddress(gateway), ifName, 1);
            com.android.server.net.NetworkManagementService.this.mDaemonHandler.post(new java.lang.Runnable() { // from class: com.android.server.net.NetworkManagementService$NetdUnsolicitedEventListener$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onRouteChanged$9(updated, processRoute);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRouteChanged$9(boolean updated, android.net.RouteInfo processRoute) {
            com.android.server.net.NetworkManagementService.this.notifyRouteChange(updated, processRoute);
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onStrictCleartextDetected(int uid, java.lang.String hex) throws android.os.RemoteException {
            android.app.ActivityManager.getService().notifyCleartextNetwork(uid, com.android.internal.util.HexDump.hexStringToByteArray(hex));
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public int getInterfaceVersion() {
            return 15;
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public java.lang.String getInterfaceHash() {
            return "2be6ff6fb01645cdddb3bb60f6de5727e5733267";
        }
    }

    public java.lang.String[] listInterfaces() {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermissionOr(this.mContext, new java.lang.String[]{"android.permission.CONNECTIVITY_INTERNAL"});
        try {
            return this.mNetdService.interfaceGetList();
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    private static android.net.InterfaceConfigurationParcel toStableParcel(android.net.InterfaceConfiguration cfg, java.lang.String iface) {
        android.net.InterfaceConfigurationParcel cfgParcel = new android.net.InterfaceConfigurationParcel();
        cfgParcel.ifName = iface;
        java.lang.String hwAddr = cfg.getHardwareAddress();
        if (!android.text.TextUtils.isEmpty(hwAddr)) {
            cfgParcel.hwAddr = hwAddr;
        } else {
            cfgParcel.hwAddr = "";
        }
        cfgParcel.ipv4Addr = cfg.getLinkAddress().getAddress().getHostAddress();
        cfgParcel.prefixLength = cfg.getLinkAddress().getPrefixLength();
        java.util.ArrayList<java.lang.String> flags = new java.util.ArrayList<>();
        for (java.lang.String flag : cfg.getFlags()) {
            flags.add(flag);
        }
        cfgParcel.flags = (java.lang.String[]) flags.toArray(new java.lang.String[0]);
        return cfgParcel;
    }

    public static android.net.InterfaceConfiguration fromStableParcel(android.net.InterfaceConfigurationParcel p) {
        android.net.InterfaceConfiguration cfg = new android.net.InterfaceConfiguration();
        cfg.setHardwareAddress(p.hwAddr);
        java.net.InetAddress addr = android.net.InetAddresses.parseNumericAddress(p.ipv4Addr);
        cfg.setLinkAddress(new android.net.LinkAddress(addr, p.prefixLength));
        for (java.lang.String flag : p.flags) {
            cfg.setFlag(flag);
        }
        return cfg;
    }

    public android.net.InterfaceConfiguration getInterfaceConfig(java.lang.String iface) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermissionOr(this.mContext, new java.lang.String[]{"android.permission.CONNECTIVITY_INTERNAL"});
        try {
            android.net.InterfaceConfigurationParcel result = this.mNetdService.interfaceGetCfg(iface);
            try {
                android.net.InterfaceConfiguration cfg = fromStableParcel(result);
                return cfg;
            } catch (java.lang.IllegalArgumentException iae) {
                throw new java.lang.IllegalStateException("Invalid InterfaceConfigurationParcel", iae);
            }
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void setInterfaceConfig(java.lang.String iface, android.net.InterfaceConfiguration cfg) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermissionOr(this.mContext, new java.lang.String[]{"android.permission.CONNECTIVITY_INTERNAL"});
        android.net.LinkAddress linkAddr = cfg.getLinkAddress();
        if (linkAddr == null || linkAddr.getAddress() == null) {
            throw new java.lang.IllegalStateException("Null LinkAddress given");
        }
        android.net.InterfaceConfigurationParcel cfgParcel = toStableParcel(cfg, iface);
        try {
            this.mNetdService.interfaceSetCfg(cfgParcel);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void setInterfaceDown(java.lang.String iface) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        android.net.InterfaceConfiguration ifcg = getInterfaceConfig(iface);
        ifcg.setInterfaceDown();
        setInterfaceConfig(iface, ifcg);
    }

    public void setInterfaceUp(java.lang.String iface) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        android.net.InterfaceConfiguration ifcg = getInterfaceConfig(iface);
        ifcg.setInterfaceUp();
        setInterfaceConfig(iface, ifcg);
    }

    public void setInterfaceIpv6PrivacyExtensions(java.lang.String iface, boolean enable) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        try {
            this.mNetdService.interfaceSetIPv6PrivacyExtensions(iface, enable);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void clearInterfaceAddresses(java.lang.String iface) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        try {
            this.mNetdService.interfaceClearAddrs(iface);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void enableIpv6(java.lang.String iface) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        try {
            this.mNetdService.interfaceSetEnableIPv6(iface, true);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void setIPv6AddrGenMode(java.lang.String iface, int mode) throws android.os.ServiceSpecificException {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        try {
            this.mNetdService.setIPv6AddrGenMode(iface, mode);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void disableIpv6(java.lang.String iface) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        try {
            this.mNetdService.interfaceSetEnableIPv6(iface, false);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void shutdown() {
        super.shutdown_enforcePermission();
        android.util.Slog.i(TAG, "Shutting down");
    }

    public boolean getIpForwardingEnabled() throws java.lang.IllegalStateException {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        if (com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
            throw new java.lang.UnsupportedOperationException("NMS#getIpForwardingEnabled not supported in V+");
        }
        try {
            return this.mNetdService.ipfwdEnabled();
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void setIpForwardingEnabled(boolean enable) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        if (com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
            throw new java.lang.UnsupportedOperationException("NMS#setIpForwardingEnabled not supported in V+");
        }
        try {
            if (enable) {
                this.mNetdService.ipfwdEnableForwarding("tethering");
            } else {
                this.mNetdService.ipfwdDisableForwarding("tethering");
            }
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void startTethering(java.lang.String[] dhcpRange) throws android.os.ServiceSpecificException {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        if (com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
            throw new java.lang.UnsupportedOperationException("NMS#startTethering not supported in V+");
        }
        try {
            com.android.net.module.util.NetdUtils.tetherStart(this.mNetdService, true, dhcpRange);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void stopTethering() {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        if (com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
            throw new java.lang.UnsupportedOperationException("NMS#stopTethering not supported in V+");
        }
        try {
            this.mNetdService.tetherStop();
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public boolean isTetheringStarted() {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        if (com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
            throw new java.lang.UnsupportedOperationException("NMS#isTetheringStarted not supported in V+");
        }
        try {
            return this.mNetdService.tetherIsEnabled();
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void tetherInterface(java.lang.String iface) throws android.os.ServiceSpecificException {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        if (com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
            throw new java.lang.UnsupportedOperationException("NMS#tetherInterface not supported in V+");
        }
        try {
            android.net.LinkAddress addr = getInterfaceConfig(iface).getLinkAddress();
            android.net.IpPrefix dest = new android.net.IpPrefix(addr.getAddress(), addr.getPrefixLength());
            com.android.net.module.util.NetdUtils.tetherInterface(this.mNetdService, iface, dest);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void untetherInterface(java.lang.String iface) throws android.os.ServiceSpecificException {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        if (com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
            throw new java.lang.UnsupportedOperationException("NMS#untetherInterface not supported in V+");
        }
        try {
            com.android.net.module.util.NetdUtils.untetherInterface(this.mNetdService, iface);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public java.lang.String[] listTetheredInterfaces() {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        if (com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
            throw new java.lang.UnsupportedOperationException("NMS#listTetheredInterfaces not supported in V+");
        }
        try {
            return this.mNetdService.tetherInterfaceList();
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void enableNat(java.lang.String internalInterface, java.lang.String externalInterface) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        if (com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
            throw new java.lang.UnsupportedOperationException("NMS#enableNat not supported in V+");
        }
        try {
            this.mNetdService.tetherAddForward(internalInterface, externalInterface);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void disableNat(java.lang.String internalInterface, java.lang.String externalInterface) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        if (com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
            throw new java.lang.UnsupportedOperationException("NMS#disableNat not supported in V+");
        }
        try {
            this.mNetdService.tetherRemoveForward(internalInterface, externalInterface);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void setInterfaceQuota(java.lang.String iface, long quotaBytes) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        synchronized (this.mQuotaLock) {
            if (this.mActiveQuotas.containsKey(iface)) {
                throw new java.lang.IllegalStateException("iface " + iface + " already has quota");
            }
            try {
                this.mNetdService.bandwidthSetInterfaceQuota(iface, quotaBytes);
                this.mActiveQuotas.put(iface, java.lang.Long.valueOf(quotaBytes));
            } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                throw new java.lang.IllegalStateException(e);
            }
        }
    }

    public void removeInterfaceQuota(java.lang.String iface) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        synchronized (this.mQuotaLock) {
            if (this.mActiveQuotas.containsKey(iface)) {
                this.mActiveQuotas.remove(iface);
                this.mActiveAlerts.remove(iface);
                try {
                    this.mNetdService.bandwidthRemoveInterfaceQuota(iface);
                } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                    throw new java.lang.IllegalStateException(e);
                }
            }
        }
    }

    public void setInterfaceAlert(java.lang.String iface, long alertBytes) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        if (!this.mActiveQuotas.containsKey(iface)) {
            throw new java.lang.IllegalStateException("setting alert requires existing quota on iface");
        }
        synchronized (this.mQuotaLock) {
            if (this.mActiveAlerts.containsKey(iface)) {
                throw new java.lang.IllegalStateException("iface " + iface + " already has alert");
            }
            try {
                this.mNetdService.bandwidthSetInterfaceAlert(iface, alertBytes);
                this.mActiveAlerts.put(iface, java.lang.Long.valueOf(alertBytes));
            } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                throw new java.lang.IllegalStateException(e);
            }
        }
    }

    public void removeInterfaceAlert(java.lang.String iface) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        synchronized (this.mQuotaLock) {
            if (this.mActiveAlerts.containsKey(iface)) {
                try {
                    this.mNetdService.bandwidthRemoveInterfaceAlert(iface);
                    this.mActiveAlerts.remove(iface);
                } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
                    throw new java.lang.IllegalStateException(e);
                }
            }
        }
    }

    private void setUidOnMeteredNetworkList(int uid, boolean allowlist, boolean enable) {
        android.util.SparseBooleanArray quotaList;
        boolean oldEnable;
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        synchronized (this.mQuotaLock) {
            synchronized (this.mRulesLock) {
                quotaList = allowlist ? this.mUidAllowOnMetered : this.mUidRejectOnMetered;
                oldEnable = quotaList.get(uid, false);
            }
            if (oldEnable == enable) {
                return;
            }
            android.os.Trace.traceBegin(2097152L, "inetd bandwidth");
            android.net.ConnectivityManager cm = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
            try {
                try {
                    if (allowlist) {
                        if (enable) {
                            cm.addUidToMeteredNetworkAllowList(uid);
                        } else {
                            cm.removeUidFromMeteredNetworkAllowList(uid);
                        }
                    } else if (enable) {
                        cm.addUidToMeteredNetworkDenyList(uid);
                    } else {
                        cm.removeUidFromMeteredNetworkDenyList(uid);
                    }
                    synchronized (this.mRulesLock) {
                        if (enable) {
                            quotaList.put(uid, true);
                        } else {
                            quotaList.delete(uid);
                        }
                    }
                } catch (java.lang.RuntimeException e) {
                    throw new java.lang.IllegalStateException(e);
                }
            } finally {
                android.os.Trace.traceEnd(2097152L);
            }
        }
    }

    public void setUidOnMeteredNetworkDenylist(int uid, boolean enable) {
        setUidOnMeteredNetworkList(uid, false, enable);
    }

    public void setUidOnMeteredNetworkAllowlist(int uid, boolean enable) {
        setUidOnMeteredNetworkList(uid, true, enable);
    }

    public boolean setDataSaverModeEnabled(boolean enable) {
        super.setDataSaverModeEnabled_enforcePermission();
        if (DBG) {
            android.util.Log.d(TAG, "setDataSaverMode: " + enable);
        }
        synchronized (this.mQuotaLock) {
            if (this.mDataSaverMode == enable) {
                android.util.Log.w(TAG, "setDataSaverMode(): already " + this.mDataSaverMode);
                return true;
            }
            android.os.Trace.traceBegin(2097152L, "setDataSaverModeEnabled");
            try {
                try {
                    if (!com.android.modules.utils.build.SdkLevel.isAtLeastV()) {
                        boolean changed = this.mNetdService.bandwidthEnableDataSaver(enable);
                        if (changed) {
                            this.mDataSaverMode = enable;
                        } else {
                            android.util.Log.e(TAG, "setDataSaverMode(" + enable + "): failed to set iptables");
                        }
                        return changed;
                    }
                    ((android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class)).setDataSaverEnabled(enable);
                    this.mDataSaverMode = enable;
                    if (this.mUseMeteredFirewallChains) {
                        synchronized (this.mRulesLock) {
                            this.mFirewallChainStates.put(10, enable);
                        }
                    }
                    return true;
                } finally {
                    android.os.Trace.traceEnd(2097152L);
                }
            } catch (android.os.RemoteException | java.lang.IllegalStateException e) {
                android.util.Log.e(TAG, "setDataSaverMode(" + enable + "): failed with exception", e);
                return false;
            }
        }
    }

    private void applyUidCleartextNetworkPolicy(int uid, int policy) {
        int policyValue;
        switch (policy) {
            case 0:
                policyValue = 1;
                break;
            case 1:
                policyValue = 2;
                break;
            case 2:
                policyValue = 3;
                break;
            default:
                throw new java.lang.IllegalArgumentException("Unknown policy " + policy);
        }
        try {
            this.mNetdService.strictUidCleartextPenalty(uid, policyValue);
            this.mUidCleartextPolicy.put(uid, policy);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void setUidCleartextNetworkPolicy(int uid, int policy) {
        if (this.mDeps.getCallingUid() != uid) {
            com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        }
        synchronized (this.mQuotaLock) {
            int oldPolicy = this.mUidCleartextPolicy.get(uid, 0);
            if (oldPolicy == policy) {
                return;
            }
            if (!this.mStrictEnabled) {
                this.mUidCleartextPolicy.put(uid, policy);
                return;
            }
            if (oldPolicy != 0 && policy != 0) {
                applyUidCleartextNetworkPolicy(uid, 0);
            }
            applyUidCleartextNetworkPolicy(uid, policy);
        }
    }

    public boolean isBandwidthControlEnabled() {
        return true;
    }

    public void setFirewallEnabled(boolean enabled) {
        enforceSystemUid();
        try {
            this.mNetdService.firewallSetFirewallType(enabled ? 0 : 1);
            this.mFirewallEnabled = enabled;
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public boolean isFirewallEnabled() {
        enforceSystemUid();
        return this.mFirewallEnabled;
    }

    public void setFirewallChainEnabled(int chain, boolean enable) {
        enforceSystemUid();
        synchronized (this.mQuotaLock) {
            synchronized (this.mRulesLock) {
                if (getFirewallChainState(chain) == enable) {
                    return;
                }
                setFirewallChainState(chain, enable);
                if (!isValidFirewallChainForSetEnabled(chain)) {
                    throw new java.lang.IllegalArgumentException("Invalid chain for setFirewallChainEnabled: " + com.android.server.net.NetworkPolicyLogger.getFirewallChainName(chain));
                }
                android.net.ConnectivityManager cm = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
                try {
                    cm.setFirewallChainEnabled(chain, enable);
                } catch (java.lang.RuntimeException e) {
                    throw new java.lang.IllegalStateException(e);
                }
            }
        }
    }

    private boolean isValidFirewallChainForSetEnabled(int chain) {
        switch (chain) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    private int getFirewallType(int i) {
        switch (i) {
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 10:
                return 0;
            case 2:
            case 11:
            case 12:
                return 1;
            case 7:
            case 8:
            case 9:
            default:
                return 1 ^ (isFirewallEnabled() ? 1 : 0);
        }
    }

    public void setFirewallUidRules(int chain, int[] uids, int[] rules) {
        enforceSystemUid();
        synchronized (this.mQuotaLock) {
            synchronized (this.mRulesLock) {
                android.util.SparseIntArray uidFirewallRules = getUidFirewallRulesLR(chain);
                android.util.SparseIntArray newRules = new android.util.SparseIntArray();
                for (int index = uids.length - 1; index >= 0; index--) {
                    int uid = uids[index];
                    int rule = rules[index];
                    updateFirewallUidRuleLocked(chain, uid, rule);
                    newRules.put(uid, rule);
                }
                android.util.SparseIntArray rulesToRemove = new android.util.SparseIntArray();
                for (int index2 = uidFirewallRules.size() - 1; index2 >= 0; index2--) {
                    int uid2 = uidFirewallRules.keyAt(index2);
                    if (newRules.indexOfKey(uid2) < 0) {
                        rulesToRemove.put(uid2, 0);
                    }
                }
                int index3 = rulesToRemove.size();
                for (int index4 = index3 - 1; index4 >= 0; index4--) {
                    updateFirewallUidRuleLocked(chain, rulesToRemove.keyAt(index4), 0);
                }
            }
            android.net.ConnectivityManager cm = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
            try {
                cm.replaceFirewallChain(chain, uids);
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.w(TAG, "Error flushing firewall chain " + chain, e);
            }
        }
    }

    public void setFirewallUidRule(int chain, int uid, int rule) {
        enforceSystemUid();
        synchronized (this.mQuotaLock) {
            setFirewallUidRuleLocked(chain, uid, rule);
        }
    }

    private void setFirewallUidRuleLocked(int chain, int uid, int rule) {
        if (updateFirewallUidRuleLocked(chain, uid, rule)) {
            android.net.ConnectivityManager cm = (android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class);
            try {
                cm.setUidFirewallRule(chain, uid, rule);
            } catch (java.lang.RuntimeException e) {
                throw new java.lang.IllegalStateException(e);
            }
        }
    }

    private boolean updateFirewallUidRuleLocked(int chain, int uid, int rule) {
        synchronized (this.mRulesLock) {
            android.util.SparseIntArray uidFirewallRules = getUidFirewallRulesLR(chain);
            int oldUidFirewallRule = uidFirewallRules.get(uid, 0);
            if (DBG) {
                android.util.Slog.d(TAG, "oldRule = " + oldUidFirewallRule + ", newRule=" + rule + " for uid=" + uid + " on chain " + chain);
            }
            if (oldUidFirewallRule == rule) {
                if (DBG) {
                    android.util.Slog.d(TAG, "!!!!! Skipping change");
                }
                return false;
            }
            java.lang.String ruleName = getFirewallRuleName(chain, rule);
            java.lang.String oldRuleName = getFirewallRuleName(chain, oldUidFirewallRule);
            if (rule == 0) {
                uidFirewallRules.delete(uid);
            } else {
                uidFirewallRules.put(uid, rule);
            }
            return ruleName.equals(oldRuleName) ? false : true;
        }
    }

    private java.lang.String getFirewallRuleName(int chain, int rule) {
        if (getFirewallType(chain) == 0) {
            if (rule == 1) {
                return "allow";
            }
            return "deny";
        }
        if (rule == 2) {
            return "deny";
        }
        return "allow";
    }

    private android.util.SparseIntArray getUidFirewallRulesLR(int chain) {
        switch (chain) {
            case 0:
                return this.mUidFirewallRules;
            case 1:
                return this.mUidFirewallDozableRules;
            case 2:
                return this.mUidFirewallStandbyRules;
            case 3:
                return this.mUidFirewallPowerSaveRules;
            case 4:
                return this.mUidFirewallRestrictedRules;
            case 5:
                return this.mUidFirewallLowPowerStandbyRules;
            case 6:
                return this.mUidFirewallBackgroundRules;
            case 7:
            case 8:
            case 9:
            default:
                throw new java.lang.IllegalArgumentException("Unknown chain:" + chain);
            case 10:
                return this.mUidMeteredFirewallAllowRules;
            case 11:
                return this.mUidMeteredFirewallDenyUserRules;
            case 12:
                return this.mUidMeteredFirewallDenyAdminRules;
        }
    }

    public int getRestrictedFlag(int uid) {
        int flag = 0;
        synchronized (this.mRulesLock) {
            if (getFirewallChainState(2) && getUidFirewallRulesLR(2).get(uid) == 2) {
                flag = 0 | 1;
            }
            if (getFirewallChainState(1) && getUidFirewallRulesLR(1).get(uid) != 1) {
                flag |= 2;
            }
            if (getFirewallChainState(3) && getUidFirewallRulesLR(3).get(uid) != 1) {
                flag |= 4;
            }
            if (getFirewallChainState(5) && getUidFirewallRulesLR(3).get(uid) != 1) {
                flag |= 8;
            }
            if (getFirewallChainState(4) && getUidFirewallRulesLR(4).get(uid) != 1) {
                flag |= 16;
            }
            if (this.mUidRejectOnMetered.get(uid)) {
                flag |= 32;
            }
            if (this.mDataSaverMode) {
                flag |= 64;
            }
            this.mPolicyManager = android.net.NetworkPolicyManager.from(this.mContext);
            if (this.mPolicyManager.getRestrictBackgroundStatus(uid) == 3) {
                flag |= 128;
            }
        }
        return flag;
    }

    private void enforceSystemUid() {
        int uid = this.mDeps.getCallingUid();
        if (uid != 1000) {
            throw new java.lang.SecurityException("Only available to AID_SYSTEM");
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            pw.println("Flags:");
            pw.println("com.android.server.net.use_metered_firewall_chains: " + this.mUseMeteredFirewallChains);
            pw.println();
            synchronized (this.mQuotaLock) {
                pw.print("Active quota ifaces: ");
                pw.println(this.mActiveQuotas.toString());
                pw.print("Active alert ifaces: ");
                pw.println(this.mActiveAlerts.toString());
                pw.print("Data saver mode: ");
                pw.println(this.mDataSaverMode);
                synchronized (this.mRulesLock) {
                    dumpUidRuleOnQuotaLocked(pw, "denied UIDs", this.mUidRejectOnMetered);
                    dumpUidRuleOnQuotaLocked(pw, "allowed UIDs", this.mUidAllowOnMetered);
                }
            }
            synchronized (this.mRulesLock) {
                dumpUidFirewallRule(pw, "", this.mUidFirewallRules);
                pw.print("UID firewall standby chain enabled: ");
                pw.println(getFirewallChainState(2));
                dumpUidFirewallRule(pw, "standby", this.mUidFirewallStandbyRules);
                pw.print("UID firewall dozable chain enabled: ");
                pw.println(getFirewallChainState(1));
                dumpUidFirewallRule(pw, "dozable", this.mUidFirewallDozableRules);
                pw.print("UID firewall powersave chain enabled: ");
                pw.println(getFirewallChainState(3));
                dumpUidFirewallRule(pw, "powersave", this.mUidFirewallPowerSaveRules);
                pw.print("UID firewall restricted mode chain enabled: ");
                pw.println(getFirewallChainState(4));
                dumpUidFirewallRule(pw, "restricted", this.mUidFirewallRestrictedRules);
                pw.print("UID firewall low power standby chain enabled: ");
                pw.println(getFirewallChainState(5));
                dumpUidFirewallRule(pw, com.android.server.power.LowPowerStandbyController.DeviceConfigWrapper.NAMESPACE, this.mUidFirewallLowPowerStandbyRules);
                pw.print("UID firewall background chain enabled: ");
                pw.println(getFirewallChainState(6));
                dumpUidFirewallRule(pw, "background", this.mUidFirewallBackgroundRules);
                pw.print("UID firewall metered allow chain enabled (Data saver mode): ");
                pw.println(getFirewallChainState(10));
                dumpUidFirewallRule(pw, "metered_allow", this.mUidMeteredFirewallAllowRules);
                pw.print("UID firewall metered deny_user chain enabled (always-on): ");
                pw.println(getFirewallChainState(11));
                dumpUidFirewallRule(pw, "metered_deny_user", this.mUidMeteredFirewallDenyUserRules);
                pw.print("UID firewall metered deny_admin chain enabled (always-on): ");
                pw.println(getFirewallChainState(12));
                dumpUidFirewallRule(pw, "metered_deny_admin", this.mUidMeteredFirewallDenyAdminRules);
            }
            pw.print("Firewall enabled: ");
            pw.println(this.mFirewallEnabled);
            pw.print("Netd service status: ");
            if (this.mNetdService == null) {
                pw.println("disconnected");
                return;
            }
            try {
                boolean alive = this.mNetdService.isAlive();
                pw.println(alive ? "alive" : "dead");
            } catch (android.os.RemoteException e) {
                pw.println(android.net.INetd.NEXTHOP_UNREACHABLE);
            }
        }
    }

    private void dumpUidRuleOnQuotaLocked(java.io.PrintWriter pw, java.lang.String name, android.util.SparseBooleanArray list) {
        pw.print("UID bandwith control ");
        pw.print(name);
        pw.print(": [");
        int size = list.size();
        for (int i = 0; i < size; i++) {
            pw.print(list.keyAt(i));
            if (i < size - 1) {
                pw.print(",");
            }
        }
        pw.println("]");
    }

    private void dumpUidFirewallRule(java.io.PrintWriter pw, java.lang.String name, android.util.SparseIntArray rules) {
        pw.print("UID firewall ");
        pw.print(name);
        pw.print(" rule: [");
        int size = rules.size();
        for (int i = 0; i < size; i++) {
            pw.print(rules.keyAt(i));
            pw.print(":");
            pw.print(rules.valueAt(i));
            if (i < size - 1) {
                pw.print(",");
            }
        }
        pw.println("]");
    }

    public void allowProtect(int uid) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        try {
            this.mNetdService.networkSetProtectAllow(uid);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public void denyProtect(int uid) {
        com.android.net.module.util.PermissionUtils.enforceNetworkStackPermission(this.mContext);
        try {
            this.mNetdService.networkSetProtectDeny(uid);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public boolean isNetworkRestricted(int uid) {
        super.isNetworkRestricted_enforcePermission();
        return isNetworkRestrictedInternal(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isNetworkRestrictedInternal(int uid) {
        synchronized (this.mRulesLock) {
            if (getFirewallChainState(2) && this.mUidFirewallStandbyRules.get(uid) == 2) {
                if (DBG) {
                    android.util.Slog.d(TAG, "Uid " + uid + " restricted because of app standby mode");
                }
                return true;
            }
            if (getFirewallChainState(1) && this.mUidFirewallDozableRules.get(uid) != 1) {
                if (DBG) {
                    android.util.Slog.d(TAG, "Uid " + uid + " restricted because of device idle mode");
                }
                return true;
            }
            if (getFirewallChainState(3) && this.mUidFirewallPowerSaveRules.get(uid) != 1) {
                if (DBG) {
                    android.util.Slog.d(TAG, "Uid " + uid + " restricted because of power saver mode");
                }
                return true;
            }
            if (getFirewallChainState(4) && this.mUidFirewallRestrictedRules.get(uid) != 1) {
                if (DBG) {
                    android.util.Slog.d(TAG, "Uid " + uid + " restricted because of restricted mode");
                }
                return true;
            }
            if (getFirewallChainState(5) && this.mUidFirewallLowPowerStandbyRules.get(uid) != 1) {
                if (DBG) {
                    android.util.Slog.d(TAG, "Uid " + uid + " restricted because of low power standby");
                }
                return true;
            }
            if (getFirewallChainState(6) && this.mUidFirewallBackgroundRules.get(uid) != 1) {
                if (DBG) {
                    android.util.Slog.d(TAG, "Uid " + uid + " restricted because it is in background");
                }
                return true;
            }
            if (this.mUseMeteredFirewallChains) {
                if (getFirewallChainState(11) && this.mUidMeteredFirewallDenyUserRules.get(uid) == 2) {
                    if (DBG) {
                        android.util.Slog.d(TAG, "Uid " + uid + " restricted because of user-restricted metered data in the background");
                    }
                    return true;
                }
                if (getFirewallChainState(12) && this.mUidMeteredFirewallDenyAdminRules.get(uid) == 2) {
                    if (DBG) {
                        android.util.Slog.d(TAG, "Uid " + uid + " restricted because of admin-restricted metered data in the background");
                    }
                    return true;
                }
                if (getFirewallChainState(10) && this.mUidMeteredFirewallAllowRules.get(uid) != 1) {
                    if (DBG) {
                        android.util.Slog.d(TAG, "Uid " + uid + " restricted because of data saver mode");
                    }
                    return true;
                }
            } else {
                if (this.mUidRejectOnMetered.get(uid)) {
                    if (DBG) {
                        android.util.Slog.d(TAG, "Uid " + uid + " restricted because of no metered data in the background");
                    }
                    return true;
                }
                if (this.mDataSaverMode && !this.mUidAllowOnMetered.get(uid)) {
                    if (DBG) {
                        android.util.Slog.d(TAG, "Uid " + uid + " restricted because of data saver mode");
                    }
                    return true;
                }
            }
            return false;
        }
    }

    private void setFirewallChainState(int chain, boolean state) {
        synchronized (this.mRulesLock) {
            this.mFirewallChainStates.put(chain, state);
        }
    }

    private boolean getFirewallChainState(int chain) {
        boolean z;
        synchronized (this.mRulesLock) {
            z = this.mFirewallChainStates.get(chain);
        }
        return z;
    }

    private class LocalService extends com.android.server.net.NetworkManagementInternal {
        private LocalService() {
        }

        @Override // com.android.server.net.NetworkManagementInternal
        public boolean isNetworkRestrictedForUid(int uid) {
            return com.android.server.net.NetworkManagementService.this.isNetworkRestrictedInternal(uid);
        }
    }
}
