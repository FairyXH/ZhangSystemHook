package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class VcnManagementService extends android.net.vcn.IVcnManagementService.Stub {
    private static final java.lang.String CONTEXT_ATTRIBUTION_TAG = "VCN";
    private static final int LOCAL_LOG_LINE_COUNT = 512;
    public static final boolean VDBG = false;
    private final com.android.server.vcn.util.PersistableBundleUtils.LockingReadWriteHelper mConfigDiskRwHelper;
    private final android.content.Context mContext;
    private final com.android.server.VcnManagementService.Dependencies mDeps;
    private final android.os.Handler mHandler;
    private final android.os.Looper mLooper;
    private final com.android.server.vcn.VcnNetworkProvider mNetworkProvider;
    private final com.android.server.vcn.TelephonySubscriptionTracker mTelephonySubscriptionTracker;
    private static final java.lang.String TAG = com.android.server.VcnManagementService.class.getSimpleName();
    private static final long DUMP_TIMEOUT_MILLIS = java.util.concurrent.TimeUnit.SECONDS.toMillis(5);
    private static final java.util.Set<java.lang.Integer> RESTRICTED_TRANSPORTS_DEFAULT = java.util.Collections.singleton(1);
    public static final android.util.LocalLog LOCAL_LOG = new android.util.LocalLog(512);
    static final java.lang.String VCN_CONFIG_FILE = new java.io.File(android.os.Environment.getDataSystemDirectory(), "vcn/configs.xml").getPath();
    static final long CARRIER_PRIVILEGES_LOST_TEARDOWN_DELAY_MS = java.util.concurrent.TimeUnit.SECONDS.toMillis(30);
    private final com.android.server.VcnManagementService.TrackingNetworkCallback mTrackingNetworkCallback = new com.android.server.VcnManagementService.TrackingNetworkCallback();
    private final java.util.Map<android.os.ParcelUuid, android.net.vcn.VcnConfig> mConfigs = new android.util.ArrayMap();
    private final java.util.Map<android.os.ParcelUuid, com.android.server.vcn.Vcn> mVcns = new android.util.ArrayMap();
    private com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot mLastSnapshot = com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot.EMPTY_SNAPSHOT;
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.Map<android.os.IBinder, com.android.server.VcnManagementService.PolicyListenerBinderDeath> mRegisteredPolicyListeners = new android.util.ArrayMap();
    private final java.util.Map<android.os.IBinder, com.android.server.VcnManagementService.VcnStatusCallbackInfo> mRegisteredStatusCallbacks = new android.util.ArrayMap();
    private final com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionTrackerCallback mTelephonySubscriptionTrackerCb = new com.android.server.VcnManagementService.VcnSubscriptionTrackerCallback();
    private final android.content.BroadcastReceiver mVcnBroadcastReceiver = new com.android.server.VcnManagementService.VcnBroadcastReceiver();

    public interface VcnCallback {
        void onGatewayConnectionError(java.lang.String str, int i, java.lang.String str2, java.lang.String str3);

        void onSafeModeStatusChanged(boolean z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    VcnManagementService(android.content.Context context, com.android.server.VcnManagementService.Dependencies dependencies) {
        this.mContext = ((android.content.Context) java.util.Objects.requireNonNull(context, "Missing context")).createAttributionContext(CONTEXT_ATTRIBUTION_TAG);
        this.mDeps = (com.android.server.VcnManagementService.Dependencies) java.util.Objects.requireNonNull(dependencies, "Missing dependencies");
        this.mLooper = this.mDeps.getLooper();
        this.mHandler = new android.os.Handler(this.mLooper);
        this.mNetworkProvider = new com.android.server.vcn.VcnNetworkProvider(this.mContext, this.mLooper);
        this.mTelephonySubscriptionTracker = this.mDeps.newTelephonySubscriptionTracker(this.mContext, this.mLooper, this.mTelephonySubscriptionTrackerCb);
        this.mConfigDiskRwHelper = this.mDeps.newPersistableBundleLockingReadWriteHelper(VCN_CONFIG_FILE);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        intentFilter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        intentFilter.addDataScheme("package");
        this.mContext.registerReceiver(this.mVcnBroadcastReceiver, intentFilter, null, this.mHandler);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() throws java.io.IOException {
                this.f$0.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() throws java.io.IOException {
        android.os.PersistableBundle configBundle;
        try {
            configBundle = this.mConfigDiskRwHelper.readFromDisk();
        } catch (java.io.IOException e1) {
            logErr("Failed to read configs from disk; retrying", e1);
            try {
                configBundle = this.mConfigDiskRwHelper.readFromDisk();
            } catch (java.io.IOException e2) {
                logWtf("Failed to read configs from disk", e2);
                return;
            }
        }
        if (configBundle != null) {
            java.util.Map<android.os.ParcelUuid, android.net.vcn.VcnConfig> configs = com.android.server.vcn.util.PersistableBundleUtils.toMap(configBundle, new com.android.server.vcn.util.PersistableBundleUtils.Deserializer() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda9
                @Override // com.android.server.vcn.util.PersistableBundleUtils.Deserializer
                public final java.lang.Object fromPersistableBundle(android.os.PersistableBundle persistableBundle) {
                    return com.android.server.vcn.util.PersistableBundleUtils.toParcelUuid(persistableBundle);
                }
            }, new com.android.server.vcn.util.PersistableBundleUtils.Deserializer() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda10
                @Override // com.android.server.vcn.util.PersistableBundleUtils.Deserializer
                public final java.lang.Object fromPersistableBundle(android.os.PersistableBundle persistableBundle) {
                    return new android.net.vcn.VcnConfig(persistableBundle);
                }
            });
            synchronized (this.mLock) {
                for (java.util.Map.Entry<android.os.ParcelUuid, android.net.vcn.VcnConfig> entry : configs.entrySet()) {
                    if (!this.mConfigs.containsKey(entry.getKey())) {
                        this.mConfigs.put(entry.getKey(), entry.getValue());
                    }
                }
                this.mTelephonySubscriptionTrackerCb.onNewSnapshot(this.mLastSnapshot);
            }
        }
    }

    static com.android.server.VcnManagementService create(android.content.Context context) {
        return new com.android.server.VcnManagementService(context, new com.android.server.VcnManagementService.Dependencies());
    }

    public static class Dependencies {
        private android.os.HandlerThread mHandlerThread;

        public android.os.Looper getLooper() {
            if (this.mHandlerThread == null) {
                synchronized (this) {
                    if (this.mHandlerThread == null) {
                        this.mHandlerThread = new android.os.HandlerThread(com.android.server.VcnManagementService.TAG);
                        this.mHandlerThread.start();
                    }
                }
            }
            return this.mHandlerThread.getLooper();
        }

        public com.android.server.vcn.TelephonySubscriptionTracker newTelephonySubscriptionTracker(android.content.Context context, android.os.Looper looper, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionTrackerCallback callback) {
            return new com.android.server.vcn.TelephonySubscriptionTracker(context, new android.os.Handler(looper), callback);
        }

        public int getBinderCallingUid() {
            return android.os.Binder.getCallingUid();
        }

        public com.android.server.vcn.util.PersistableBundleUtils.LockingReadWriteHelper newPersistableBundleLockingReadWriteHelper(java.lang.String path) {
            return new com.android.server.vcn.util.PersistableBundleUtils.LockingReadWriteHelper(path);
        }

        public com.android.server.vcn.VcnContext newVcnContext(android.content.Context context, android.os.Looper looper, com.android.server.vcn.VcnNetworkProvider vcnNetworkProvider, boolean isInTestMode) {
            return new com.android.server.vcn.VcnContext(context, looper, vcnNetworkProvider, isInTestMode);
        }

        public com.android.server.vcn.Vcn newVcn(com.android.server.vcn.VcnContext vcnContext, android.os.ParcelUuid subscriptionGroup, android.net.vcn.VcnConfig config, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, com.android.server.VcnManagementService.VcnCallback vcnCallback) {
            return new com.android.server.vcn.Vcn(vcnContext, subscriptionGroup, config, snapshot, vcnCallback);
        }

        public int getSubIdForWifiInfo(android.net.wifi.WifiInfo wifiInfo) {
            return wifiInfo.getSubscriptionId();
        }

        public com.android.net.module.util.LocationPermissionChecker newLocationPermissionChecker(android.content.Context context) {
            return new com.android.net.module.util.LocationPermissionChecker(context);
        }

        public java.util.Set<java.lang.Integer> getRestrictedTransportsFromCarrierConfig(android.os.ParcelUuid subGrp, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot lastSnapshot) {
            if (!android.os.Build.IS_ENG && !android.os.Build.IS_USERDEBUG) {
                return com.android.server.VcnManagementService.RESTRICTED_TRANSPORTS_DEFAULT;
            }
            com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig = lastSnapshot.getCarrierConfigForSubGrp(subGrp);
            if (carrierConfig == null) {
                return com.android.server.VcnManagementService.RESTRICTED_TRANSPORTS_DEFAULT;
            }
            int[] defaultValue = com.android.server.VcnManagementService.RESTRICTED_TRANSPORTS_DEFAULT.stream().mapToInt(new java.util.function.ToIntFunction() { // from class: com.android.server.VcnManagementService$Dependencies$$ExternalSyntheticLambda0
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(java.lang.Object obj) {
                    return ((java.lang.Integer) obj).intValue();
                }
            }).toArray();
            int[] restrictedTransportsArray = carrierConfig.getIntArray("vcn_restricted_transports", defaultValue);
            java.util.Set<java.lang.Integer> restrictedTransports = new android.util.ArraySet<>();
            for (int transport : restrictedTransportsArray) {
                restrictedTransports.add(java.lang.Integer.valueOf(transport));
            }
            return restrictedTransports;
        }

        public java.util.Set<java.lang.Integer> getRestrictedTransports(android.os.ParcelUuid subGrp, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot lastSnapshot, android.net.vcn.VcnConfig vcnConfig) {
            java.util.Set<java.lang.Integer> restrictedTransports = new android.util.ArraySet<>();
            restrictedTransports.addAll(vcnConfig.getRestrictedUnderlyingNetworkTransports());
            restrictedTransports.addAll(getRestrictedTransportsFromCarrierConfig(subGrp, lastSnapshot));
            return restrictedTransports;
        }
    }

    public void systemReady() {
        this.mNetworkProvider.register();
        ((android.net.ConnectivityManager) this.mContext.getSystemService(android.net.ConnectivityManager.class)).registerNetworkCallback(new android.net.NetworkRequest.Builder().clearCapabilities().build(), this.mTrackingNetworkCallback);
        this.mTelephonySubscriptionTracker.register();
    }

    private void enforcePrimaryUser() {
        int uid = this.mDeps.getBinderCallingUid();
        if (uid == 1000) {
            throw new java.lang.IllegalStateException("Calling identity was System Server. Was Binder calling identity cleared?");
        }
        final android.os.UserHandle userHandle = android.os.UserHandle.getUserHandleForUid(uid);
        if (android.net.vcn.Flags.enforceMainUser()) {
            final android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda14
                public final void runOrThrow() throws java.lang.Exception {
                    com.android.server.VcnManagementService.lambda$enforcePrimaryUser$1(userManager, userHandle);
                }
            });
        } else if (!userHandle.isSystem()) {
            throw new java.lang.SecurityException("VcnManagementService can only be used by callers running as the primary user");
        }
    }

    static /* synthetic */ void lambda$enforcePrimaryUser$1(android.os.UserManager userManager, android.os.UserHandle userHandle) throws java.lang.Exception {
        if (!java.util.Objects.equals(userManager.getMainUser(), userHandle)) {
            throw new java.lang.SecurityException("VcnManagementService can only be used by callers running as the main user");
        }
    }

    private void enforceCallingUserAndCarrierPrivilege(final android.os.ParcelUuid subscriptionGroup, java.lang.String pkgName) {
        enforcePrimaryUser();
        final android.telephony.SubscriptionManager subMgr = (android.telephony.SubscriptionManager) this.mContext.getSystemService(android.telephony.SubscriptionManager.class);
        final java.util.List<android.telephony.SubscriptionInfo> subscriptionInfos = new java.util.ArrayList<>();
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda8
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$enforceCallingUserAndCarrierPrivilege$2(subMgr, subscriptionGroup, subscriptionInfos);
            }
        });
        for (android.telephony.SubscriptionInfo info : subscriptionInfos) {
            android.telephony.TelephonyManager telMgr = ((android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class)).createForSubscriptionId(info.getSubscriptionId());
            if (android.telephony.SubscriptionManager.isValidSlotIndex(info.getSimSlotIndex()) && telMgr.checkCarrierPrivilegesForPackage(pkgName) == 1) {
                return;
            }
        }
        throw new java.lang.SecurityException("Carrier privilege required for subscription group to set VCN Config");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enforceCallingUserAndCarrierPrivilege$2(android.telephony.SubscriptionManager subMgr, android.os.ParcelUuid subscriptionGroup, java.util.List subscriptionInfos) throws java.lang.Exception {
        java.util.List<android.telephony.SubscriptionInfo> subsInGroup = subMgr.getSubscriptionsInGroup(subscriptionGroup);
        if (subsInGroup == null) {
            logWtf("Received null from getSubscriptionsInGroup");
            subsInGroup = java.util.Collections.emptyList();
        }
        subscriptionInfos.addAll(subsInGroup);
    }

    private void enforceManageTestNetworksForTestMode(android.net.vcn.VcnConfig vcnConfig) {
        if (vcnConfig.isTestModeProfile()) {
            this.mContext.enforceCallingPermission("android.permission.MANAGE_TEST_NETWORKS", "Test-mode require the MANAGE_TEST_NETWORKS permission");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isActiveSubGroup(android.os.ParcelUuid subGrp, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot) {
        if (subGrp == null || snapshot == null) {
            return false;
        }
        return java.util.Objects.equals(subGrp, snapshot.getActiveDataSubscriptionGroup());
    }

    private class VcnBroadcastReceiver extends android.content.BroadcastReceiver {
        private VcnBroadcastReceiver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:20:0x003e  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r8, android.content.Intent r9) {
            /*
                Method dump skipped, instruction units count: 286
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.VcnManagementService.VcnBroadcastReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class VcnSubscriptionTrackerCallback implements com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionTrackerCallback {
        private VcnSubscriptionTrackerCallback() {
        }

        @Override // com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionTrackerCallback
        public void onNewSnapshot(com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot) {
            long teardownDelayMs;
            com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot = snapshot;
            synchronized (com.android.server.VcnManagementService.this.mLock) {
                com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot oldSnapshot = com.android.server.VcnManagementService.this.mLastSnapshot;
                com.android.server.VcnManagementService.this.mLastSnapshot = telephonySubscriptionSnapshot;
                com.android.server.VcnManagementService.this.logInfo("new snapshot: " + com.android.server.VcnManagementService.this.mLastSnapshot);
                for (java.util.Map.Entry<android.os.ParcelUuid, android.net.vcn.VcnConfig> entry : com.android.server.VcnManagementService.this.mConfigs.entrySet()) {
                    android.os.ParcelUuid subGrp = entry.getKey();
                    if (telephonySubscriptionSnapshot.packageHasPermissionsForSubscriptionGroup(subGrp, entry.getValue().getProvisioningPackageName()) && com.android.server.VcnManagementService.this.isActiveSubGroup(subGrp, telephonySubscriptionSnapshot)) {
                        if (!com.android.server.VcnManagementService.this.mVcns.containsKey(subGrp)) {
                            com.android.server.VcnManagementService.this.startVcnLocked(subGrp, entry.getValue());
                        }
                        com.android.server.VcnManagementService.this.mHandler.removeCallbacksAndMessages(com.android.server.VcnManagementService.this.mVcns.get(subGrp));
                    }
                }
                boolean needNotifyAllPolicyListeners = false;
                for (java.util.Map.Entry<android.os.ParcelUuid, com.android.server.vcn.Vcn> entry2 : com.android.server.VcnManagementService.this.mVcns.entrySet()) {
                    final android.os.ParcelUuid subGrp2 = entry2.getKey();
                    android.net.vcn.VcnConfig config = (android.net.vcn.VcnConfig) com.android.server.VcnManagementService.this.mConfigs.get(subGrp2);
                    boolean isActiveSubGrp = com.android.server.VcnManagementService.this.isActiveSubGroup(subGrp2, telephonySubscriptionSnapshot);
                    boolean isValidActiveDataSubIdNotInVcnSubGrp = android.telephony.SubscriptionManager.isValidSubscriptionId(snapshot.getActiveDataSubscriptionId()) && !com.android.server.VcnManagementService.this.isActiveSubGroup(subGrp2, telephonySubscriptionSnapshot);
                    if (config != null && telephonySubscriptionSnapshot.packageHasPermissionsForSubscriptionGroup(subGrp2, config.getProvisioningPackageName()) && isActiveSubGrp) {
                        entry2.getValue().updateSubscriptionSnapshot(com.android.server.VcnManagementService.this.mLastSnapshot);
                        needNotifyAllPolicyListeners |= true ^ java.util.Objects.equals(oldSnapshot.getCarrierConfigForSubGrp(subGrp2), com.android.server.VcnManagementService.this.mLastSnapshot.getCarrierConfigForSubGrp(subGrp2));
                    } else {
                        final com.android.server.vcn.Vcn instanceToTeardown = entry2.getValue();
                        if (isValidActiveDataSubIdNotInVcnSubGrp) {
                            teardownDelayMs = 0;
                        } else {
                            teardownDelayMs = com.android.server.VcnManagementService.CARRIER_PRIVILEGES_LOST_TEARDOWN_DELAY_MS;
                        }
                        com.android.server.VcnManagementService.this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.VcnManagementService$VcnSubscriptionTrackerCallback$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f$0.lambda$onNewSnapshot$0(subGrp2, instanceToTeardown);
                            }
                        }, instanceToTeardown, teardownDelayMs);
                    }
                    telephonySubscriptionSnapshot = snapshot;
                }
                java.util.Map<android.os.ParcelUuid, java.util.Set<java.lang.Integer>> oldSubGrpMappings = com.android.server.VcnManagementService.this.getSubGroupToSubIdMappings(oldSnapshot);
                java.util.Map<android.os.ParcelUuid, java.util.Set<java.lang.Integer>> currSubGrpMappings = com.android.server.VcnManagementService.this.getSubGroupToSubIdMappings(com.android.server.VcnManagementService.this.mLastSnapshot);
                if (!currSubGrpMappings.equals(oldSubGrpMappings)) {
                    com.android.server.VcnManagementService.this.garbageCollectAndWriteVcnConfigsLocked();
                    needNotifyAllPolicyListeners = true;
                }
                if (needNotifyAllPolicyListeners) {
                    com.android.server.VcnManagementService.this.notifyAllPolicyListenersLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNewSnapshot$0(android.os.ParcelUuid uuidToTeardown, com.android.server.vcn.Vcn instanceToTeardown) {
            synchronized (com.android.server.VcnManagementService.this.mLock) {
                if (com.android.server.VcnManagementService.this.mVcns.get(uuidToTeardown) == instanceToTeardown) {
                    com.android.server.VcnManagementService.this.stopVcnLocked(uuidToTeardown);
                    com.android.server.VcnManagementService.this.notifyAllPermissionedStatusCallbacksLocked(uuidToTeardown, 1);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.Map<android.os.ParcelUuid, java.util.Set<java.lang.Integer>> getSubGroupToSubIdMappings(com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot) {
        java.util.Map<android.os.ParcelUuid, java.util.Set<java.lang.Integer>> subGrpMappings = new android.util.ArrayMap<>();
        for (android.os.ParcelUuid subGrp : this.mVcns.keySet()) {
            subGrpMappings.put(subGrp, snapshot.getAllSubIdsInGroup(subGrp));
        }
        return subGrpMappings;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopVcnLocked(android.os.ParcelUuid uuidToTeardown) {
        logInfo("Stopping VCN config for subGrp: " + uuidToTeardown);
        com.android.server.vcn.Vcn vcnToTeardown = this.mVcns.get(uuidToTeardown);
        if (vcnToTeardown == null) {
            return;
        }
        vcnToTeardown.teardownAsynchronously();
        this.mVcns.remove(uuidToTeardown);
        notifyAllPolicyListenersLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAllPolicyListenersLocked() {
        for (final com.android.server.VcnManagementService.PolicyListenerBinderDeath policyListener : this.mRegisteredPolicyListeners.values()) {
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda0
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$notifyAllPolicyListenersLocked$3(policyListener);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyAllPolicyListenersLocked$3(com.android.server.VcnManagementService.PolicyListenerBinderDeath policyListener) throws java.lang.Exception {
        try {
            policyListener.mListener.onPolicyChanged();
        } catch (android.os.RemoteException e) {
            logDbg("VcnStatusCallback threw on VCN status change", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAllPermissionedStatusCallbacksLocked(android.os.ParcelUuid subGroup, final int statusCode) {
        for (final com.android.server.VcnManagementService.VcnStatusCallbackInfo cbInfo : this.mRegisteredStatusCallbacks.values()) {
            if (isCallbackPermissioned(cbInfo, subGroup)) {
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda12
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$notifyAllPermissionedStatusCallbacksLocked$4(cbInfo, statusCode);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyAllPermissionedStatusCallbacksLocked$4(com.android.server.VcnManagementService.VcnStatusCallbackInfo cbInfo, int statusCode) throws java.lang.Exception {
        try {
            cbInfo.mCallback.onVcnStatusChanged(statusCode);
        } catch (android.os.RemoteException e) {
            logDbg("VcnStatusCallback threw on VCN status change", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startVcnLocked(android.os.ParcelUuid subscriptionGroup, android.net.vcn.VcnConfig config) {
        logInfo("Starting VCN config for subGrp: " + subscriptionGroup);
        if (!this.mVcns.isEmpty()) {
            for (android.os.ParcelUuid uuidToTeardown : this.mVcns.keySet()) {
                stopVcnLocked(uuidToTeardown);
            }
        }
        com.android.server.VcnManagementService.VcnCallbackImpl vcnCallback = new com.android.server.VcnManagementService.VcnCallbackImpl(subscriptionGroup);
        com.android.server.vcn.VcnContext vcnContext = this.mDeps.newVcnContext(this.mContext, this.mLooper, this.mNetworkProvider, config.isTestModeProfile());
        com.android.server.vcn.Vcn newInstance = this.mDeps.newVcn(vcnContext, subscriptionGroup, config, this.mLastSnapshot, vcnCallback);
        this.mVcns.put(subscriptionGroup, newInstance);
        notifyAllPolicyListenersLocked();
        notifyAllPermissionedStatusCallbacksLocked(subscriptionGroup, 2);
    }

    private void startOrUpdateVcnLocked(android.os.ParcelUuid subscriptionGroup, android.net.vcn.VcnConfig config) {
        logDbg("Starting or updating VCN config for subGrp: " + subscriptionGroup);
        if (this.mVcns.containsKey(subscriptionGroup)) {
            com.android.server.vcn.Vcn vcn = this.mVcns.get(subscriptionGroup);
            vcn.updateConfig(config);
            notifyAllPolicyListenersLocked();
        } else if (isActiveSubGroup(subscriptionGroup, this.mLastSnapshot)) {
            startVcnLocked(subscriptionGroup, config);
        }
    }

    public void setVcnConfig(final android.os.ParcelUuid subscriptionGroup, final android.net.vcn.VcnConfig config, java.lang.String opPkgName) {
        java.util.Objects.requireNonNull(subscriptionGroup, "subscriptionGroup was null");
        java.util.Objects.requireNonNull(config, "config was null");
        java.util.Objects.requireNonNull(opPkgName, "opPkgName was null");
        if (!config.getProvisioningPackageName().equals(opPkgName)) {
            throw new java.lang.IllegalArgumentException("Mismatched caller and VcnConfig creator");
        }
        logInfo("VCN config updated for subGrp: " + subscriptionGroup);
        ((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class)).checkPackage(this.mDeps.getBinderCallingUid(), config.getProvisioningPackageName());
        enforceManageTestNetworksForTestMode(config);
        enforceCallingUserAndCarrierPrivilege(subscriptionGroup, opPkgName);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda7
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$setVcnConfig$5(subscriptionGroup, config);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setVcnConfig$5(android.os.ParcelUuid subscriptionGroup, android.net.vcn.VcnConfig config) throws java.lang.Exception {
        synchronized (this.mLock) {
            this.mConfigs.put(subscriptionGroup, config);
            startOrUpdateVcnLocked(subscriptionGroup, config);
            writeConfigsToDiskLocked();
        }
    }

    private void enforceCarrierPrivilegeOrProvisioningPackage(android.os.ParcelUuid subscriptionGroup, java.lang.String pkg) {
        enforcePrimaryUser();
        if (isProvisioningPackageForConfig(subscriptionGroup, pkg)) {
            return;
        }
        enforceCallingUserAndCarrierPrivilege(subscriptionGroup, pkg);
    }

    private boolean isProvisioningPackageForConfig(android.os.ParcelUuid subscriptionGroup, java.lang.String pkg) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                android.net.vcn.VcnConfig config = this.mConfigs.get(subscriptionGroup);
                if (config == null || !pkg.equals(config.getProvisioningPackageName())) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    return false;
                }
                android.os.Binder.restoreCallingIdentity(identity);
                return true;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    public void clearVcnConfig(final android.os.ParcelUuid subscriptionGroup, java.lang.String opPkgName) {
        java.util.Objects.requireNonNull(subscriptionGroup, "subscriptionGroup was null");
        java.util.Objects.requireNonNull(opPkgName, "opPkgName was null");
        logInfo("VCN config cleared for subGrp: " + subscriptionGroup);
        ((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class)).checkPackage(this.mDeps.getBinderCallingUid(), opPkgName);
        enforceCarrierPrivilegeOrProvisioningPackage(subscriptionGroup, opPkgName);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda4
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$clearVcnConfig$6(subscriptionGroup);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearVcnConfig$6(android.os.ParcelUuid subscriptionGroup) throws java.lang.Exception {
        synchronized (this.mLock) {
            stopAndClearVcnConfigInternalLocked(subscriptionGroup);
            writeConfigsToDiskLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAndClearVcnConfigInternalLocked(android.os.ParcelUuid subscriptionGroup) {
        this.mConfigs.remove(subscriptionGroup);
        boolean vcnExists = this.mVcns.containsKey(subscriptionGroup);
        stopVcnLocked(subscriptionGroup);
        if (vcnExists) {
            notifyAllPermissionedStatusCallbacksLocked(subscriptionGroup, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void garbageCollectAndWriteVcnConfigsLocked() throws android.os.ServiceSpecificException {
        android.telephony.SubscriptionManager subMgr = (android.telephony.SubscriptionManager) this.mContext.getSystemService(android.telephony.SubscriptionManager.class);
        boolean shouldWrite = false;
        java.util.Iterator<android.os.ParcelUuid> configsIterator = this.mConfigs.keySet().iterator();
        while (configsIterator.hasNext()) {
            android.os.ParcelUuid subGrp = configsIterator.next();
            java.util.List<android.telephony.SubscriptionInfo> subscriptions = subMgr.getSubscriptionsInGroup(subGrp);
            if (subscriptions == null || subscriptions.isEmpty()) {
                configsIterator.remove();
                shouldWrite = true;
            }
        }
        if (shouldWrite) {
            writeConfigsToDiskLocked();
        }
    }

    public java.util.List<android.os.ParcelUuid> getConfiguredSubscriptionGroups(java.lang.String opPkgName) {
        java.util.Objects.requireNonNull(opPkgName, "opPkgName was null");
        ((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class)).checkPackage(this.mDeps.getBinderCallingUid(), opPkgName);
        enforcePrimaryUser();
        java.util.List<android.os.ParcelUuid> result = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            for (android.os.ParcelUuid subGrp : this.mConfigs.keySet()) {
                if (this.mLastSnapshot.packageHasPermissionsForSubscriptionGroup(subGrp, opPkgName) || isProvisioningPackageForConfig(subGrp, opPkgName)) {
                    result.add(subGrp);
                }
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void writeConfigsToDiskLocked() throws android.os.ServiceSpecificException {
        try {
            android.os.PersistableBundle bundle = com.android.server.vcn.util.PersistableBundleUtils.fromMap(this.mConfigs, new com.android.server.vcn.util.PersistableBundleUtils.Serializer() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda1
                @Override // com.android.server.vcn.util.PersistableBundleUtils.Serializer
                public final android.os.PersistableBundle toPersistableBundle(java.lang.Object obj) {
                    return com.android.server.vcn.util.PersistableBundleUtils.fromParcelUuid((android.os.ParcelUuid) obj);
                }
            }, new com.android.server.vcn.util.PersistableBundleUtils.Serializer() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda2
                @Override // com.android.server.vcn.util.PersistableBundleUtils.Serializer
                public final android.os.PersistableBundle toPersistableBundle(java.lang.Object obj) {
                    return ((android.net.vcn.VcnConfig) obj).toPersistableBundle();
                }
            });
            this.mConfigDiskRwHelper.writeToDisk(bundle);
        } catch (java.io.IOException e) {
            logErr("Failed to save configs to disk", e);
            throw new android.os.ServiceSpecificException(0, "Failed to save configs");
        }
    }

    java.util.Map<android.os.ParcelUuid, android.net.vcn.VcnConfig> getConfigs() {
        java.util.Map<android.os.ParcelUuid, android.net.vcn.VcnConfig> mapUnmodifiableMap;
        synchronized (this.mLock) {
            mapUnmodifiableMap = java.util.Collections.unmodifiableMap(this.mConfigs);
        }
        return mapUnmodifiableMap;
    }

    public java.util.Map<android.os.ParcelUuid, com.android.server.vcn.Vcn> getAllVcns() {
        java.util.Map<android.os.ParcelUuid, com.android.server.vcn.Vcn> mapUnmodifiableMap;
        synchronized (this.mLock) {
            mapUnmodifiableMap = java.util.Collections.unmodifiableMap(this.mVcns);
        }
        return mapUnmodifiableMap;
    }

    public java.util.Map<android.os.IBinder, com.android.server.VcnManagementService.VcnStatusCallbackInfo> getAllStatusCallbacks() {
        java.util.Map<android.os.IBinder, com.android.server.VcnManagementService.VcnStatusCallbackInfo> mapUnmodifiableMap;
        synchronized (this.mLock) {
            mapUnmodifiableMap = java.util.Collections.unmodifiableMap(this.mRegisteredStatusCallbacks);
        }
        return mapUnmodifiableMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class PolicyListenerBinderDeath implements android.os.IBinder.DeathRecipient {
        private final android.net.vcn.IVcnUnderlyingNetworkPolicyListener mListener;

        PolicyListenerBinderDeath(android.net.vcn.IVcnUnderlyingNetworkPolicyListener listener) {
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Log.e(com.android.server.VcnManagementService.TAG, "app died without removing VcnUnderlyingNetworkPolicyListener");
            com.android.server.VcnManagementService.this.removeVcnUnderlyingNetworkPolicyListener(this.mListener);
        }
    }

    public void addVcnUnderlyingNetworkPolicyListener(final android.net.vcn.IVcnUnderlyingNetworkPolicyListener listener) {
        java.util.Objects.requireNonNull(listener, "listener was null");
        com.android.net.module.util.PermissionUtils.enforceAnyPermissionOf(this.mContext, new java.lang.String[]{"android.permission.NETWORK_FACTORY", "android.permission.MANAGE_TEST_NETWORKS"});
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda11
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$addVcnUnderlyingNetworkPolicyListener$7(listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addVcnUnderlyingNetworkPolicyListener$7(android.net.vcn.IVcnUnderlyingNetworkPolicyListener listener) throws java.lang.Exception {
        com.android.server.VcnManagementService.PolicyListenerBinderDeath listenerBinderDeath = new com.android.server.VcnManagementService.PolicyListenerBinderDeath(listener);
        synchronized (this.mLock) {
            this.mRegisteredPolicyListeners.put(listener.asBinder(), listenerBinderDeath);
            try {
                listener.asBinder().linkToDeath(listenerBinderDeath, 0);
            } catch (android.os.RemoteException e) {
                listenerBinderDeath.binderDied();
            }
        }
    }

    public void removeVcnUnderlyingNetworkPolicyListener(final android.net.vcn.IVcnUnderlyingNetworkPolicyListener listener) {
        java.util.Objects.requireNonNull(listener, "listener was null");
        com.android.net.module.util.PermissionUtils.enforceAnyPermissionOf(this.mContext, new java.lang.String[]{"android.permission.NETWORK_FACTORY", "android.permission.MANAGE_TEST_NETWORKS"});
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda3
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$removeVcnUnderlyingNetworkPolicyListener$8(listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeVcnUnderlyingNetworkPolicyListener$8(android.net.vcn.IVcnUnderlyingNetworkPolicyListener listener) throws java.lang.Exception {
        synchronized (this.mLock) {
            com.android.server.VcnManagementService.PolicyListenerBinderDeath listenerBinderDeath = this.mRegisteredPolicyListeners.remove(listener.asBinder());
            if (listenerBinderDeath != null) {
                listener.asBinder().unlinkToDeath(listenerBinderDeath, 0);
            }
        }
    }

    private android.os.ParcelUuid getSubGroupForNetworkCapabilities(android.net.NetworkCapabilities networkCapabilities) {
        com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot;
        android.os.ParcelUuid subGrp = null;
        synchronized (this.mLock) {
            snapshot = this.mLastSnapshot;
        }
        java.util.Iterator it = networkCapabilities.getSubscriptionIds().iterator();
        while (it.hasNext()) {
            int subId = ((java.lang.Integer) it.next()).intValue();
            if (subGrp != null && !subGrp.equals(snapshot.getGroupForSubId(subId))) {
                logWtf("Got multiple subscription groups for a single network");
            }
            subGrp = snapshot.getGroupForSubId(subId);
        }
        return subGrp;
    }

    public android.net.vcn.VcnUnderlyingNetworkPolicy getUnderlyingNetworkPolicy(final android.net.NetworkCapabilities networkCapabilities, final android.net.LinkProperties linkProperties) {
        java.util.Objects.requireNonNull(networkCapabilities, "networkCapabilities was null");
        java.util.Objects.requireNonNull(linkProperties, "linkProperties was null");
        com.android.net.module.util.PermissionUtils.enforceAnyPermissionOf(this.mContext, new java.lang.String[]{"android.permission.NETWORK_FACTORY", "android.permission.MANAGE_TEST_NETWORKS"});
        boolean isUsingManageTestNetworks = this.mContext.checkCallingOrSelfPermission("android.permission.NETWORK_FACTORY") != 0;
        if (isUsingManageTestNetworks && !networkCapabilities.hasTransport(7)) {
            throw new java.lang.IllegalStateException("NetworkCapabilities must be for Test Network if using permission MANAGE_TEST_NETWORKS");
        }
        return (android.net.vcn.VcnUnderlyingNetworkPolicy) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda13
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$getUnderlyingNetworkPolicy$9(networkCapabilities, linkProperties);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.net.vcn.VcnUnderlyingNetworkPolicy lambda$getUnderlyingNetworkPolicy$9(android.net.NetworkCapabilities networkCapabilities, android.net.LinkProperties linkProperties) throws java.lang.Exception {
        android.net.NetworkCapabilities ncCopy = new android.net.NetworkCapabilities(networkCapabilities);
        android.os.ParcelUuid subGrp = getSubGroupForNetworkCapabilities(ncCopy);
        boolean isVcnManagedNetwork = false;
        boolean isRestricted = false;
        synchronized (this.mLock) {
            com.android.server.vcn.Vcn vcn = this.mVcns.get(subGrp);
            android.net.vcn.VcnConfig vcnConfig = this.mConfigs.get(subGrp);
            if (vcn != null) {
                if (vcnConfig == null) {
                    logWtf("Vcn instance exists but VcnConfig does not for " + subGrp);
                }
                if (vcn.getStatus() == 2) {
                    isVcnManagedNetwork = true;
                }
                java.util.Set<java.lang.Integer> restrictedTransports = this.mDeps.getRestrictedTransports(subGrp, this.mLastSnapshot, vcnConfig);
                java.util.Iterator<java.lang.Integer> it = restrictedTransports.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    int restrictedTransport = it.next().intValue();
                    if (ncCopy.hasTransport(restrictedTransport)) {
                        if (restrictedTransport != 0 && restrictedTransport != 7) {
                            isRestricted = true;
                            break;
                        }
                        isRestricted |= vcn.getStatus() == 2;
                    }
                }
            }
        }
        android.net.NetworkCapabilities.Builder ncBuilder = new android.net.NetworkCapabilities.Builder(ncCopy);
        if (isVcnManagedNetwork) {
            ncBuilder.removeCapability(28);
        } else {
            ncBuilder.addCapability(28);
        }
        if (isRestricted) {
            ncBuilder.removeCapability(13);
        }
        android.net.NetworkCapabilities result = ncBuilder.build();
        android.net.vcn.VcnUnderlyingNetworkPolicy policy = new android.net.vcn.VcnUnderlyingNetworkPolicy(this.mTrackingNetworkCallback.requiresRestartForImmutableCapabilityChanges(result, linkProperties), result);
        logVdbg("getUnderlyingNetworkPolicy() called for caps: " + networkCapabilities + "; and lp: " + linkProperties + "; result = " + policy);
        return policy;
    }

    class VcnStatusCallbackInfo implements android.os.IBinder.DeathRecipient {
        final android.net.vcn.IVcnStatusCallback mCallback;
        final java.lang.String mPkgName;
        final android.os.ParcelUuid mSubGroup;
        final int mUid;

        private VcnStatusCallbackInfo(android.os.ParcelUuid subGroup, android.net.vcn.IVcnStatusCallback callback, java.lang.String pkgName, int uid) {
            this.mSubGroup = subGroup;
            this.mCallback = callback;
            this.mPkgName = pkgName;
            this.mUid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Log.e(com.android.server.VcnManagementService.TAG, "app died without unregistering VcnStatusCallback");
            com.android.server.VcnManagementService.this.unregisterVcnStatusCallback(this.mCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCallbackPermissioned(com.android.server.VcnManagementService.VcnStatusCallbackInfo cbInfo, android.os.ParcelUuid subgroup) {
        return subgroup.equals(cbInfo.mSubGroup) && this.mLastSnapshot.packageHasPermissionsForSubscriptionGroup(subgroup, cbInfo.mPkgName);
    }

    public void registerVcnStatusCallback(android.os.ParcelUuid subGroup, android.net.vcn.IVcnStatusCallback callback, java.lang.String opPkgName) {
        int resultStatus;
        int callingUid = this.mDeps.getBinderCallingUid();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            java.util.Objects.requireNonNull(subGroup, "subGroup must not be null");
            java.util.Objects.requireNonNull(callback, "callback must not be null");
            java.util.Objects.requireNonNull(opPkgName, "opPkgName must not be null");
            ((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class)).checkPackage(callingUid, opPkgName);
            android.os.IBinder cbBinder = callback.asBinder();
            com.android.server.VcnManagementService.VcnStatusCallbackInfo cbInfo = new com.android.server.VcnManagementService.VcnStatusCallbackInfo(subGroup, callback, opPkgName, callingUid);
            int vcnStatus = 0;
            try {
                cbBinder.linkToDeath(cbInfo, 0);
                synchronized (this.mLock) {
                    if (this.mRegisteredStatusCallbacks.containsKey(cbBinder)) {
                        throw new java.lang.IllegalStateException("Attempting to register a callback that is already in use");
                    }
                    this.mRegisteredStatusCallbacks.put(cbBinder, cbInfo);
                    android.net.vcn.VcnConfig vcnConfig = this.mConfigs.get(subGroup);
                    com.android.server.vcn.Vcn vcn = this.mVcns.get(subGroup);
                    if (vcn != null) {
                        vcnStatus = vcn.getStatus();
                    }
                    if (vcnConfig == null || !isCallbackPermissioned(cbInfo, subGroup)) {
                        resultStatus = 0;
                    } else if (vcn == null) {
                        resultStatus = 1;
                    } else if (vcnStatus == 2 || vcnStatus == 3) {
                        resultStatus = vcnStatus;
                    } else {
                        logWtf("Unknown VCN status: " + vcnStatus);
                        resultStatus = 0;
                    }
                    try {
                        cbInfo.mCallback.onVcnStatusChanged(resultStatus);
                    } catch (android.os.RemoteException e) {
                        logDbg("VcnStatusCallback threw on VCN status change", e);
                    }
                }
            } catch (android.os.RemoteException e2) {
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void unregisterVcnStatusCallback(android.net.vcn.IVcnStatusCallback callback) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            java.util.Objects.requireNonNull(callback, "callback must not be null");
            android.os.IBinder cbBinder = callback.asBinder();
            synchronized (this.mLock) {
                com.android.server.VcnManagementService.VcnStatusCallbackInfo cbInfo = this.mRegisteredStatusCallbacks.remove(cbBinder);
                if (cbInfo != null) {
                    cbBinder.unlinkToDeath(cbInfo, 0);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    void setLastSnapshot(com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot) {
        this.mLastSnapshot = (com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot) java.util.Objects.requireNonNull(snapshot);
    }

    private void logVdbg(java.lang.String msg) {
    }

    private void logDbg(java.lang.String msg) {
        android.util.Slog.d(TAG, msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logDbg(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.d(TAG, msg, tr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logInfo(java.lang.String msg) {
        android.util.Slog.i(TAG, msg);
        LOCAL_LOG.log("[INFO] [" + TAG + "] " + msg);
    }

    private void logInfo(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.i(TAG, msg, tr);
        LOCAL_LOG.log("[INFO] [" + TAG + "] " + msg + tr);
    }

    private void logErr(java.lang.String msg) {
        android.util.Slog.e(TAG, msg);
        LOCAL_LOG.log("[ERR] [" + TAG + "] " + msg);
    }

    private void logErr(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.e(TAG, msg, tr);
        LOCAL_LOG.log("[ERR ] [" + TAG + "] " + msg + tr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logWtf(java.lang.String msg) {
        android.util.Slog.wtf(TAG, msg);
        LOCAL_LOG.log("[WTF] [" + TAG + "] " + msg);
    }

    private void logWtf(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.wtf(TAG, msg, tr);
        LOCAL_LOG.log("[WTF ] [" + TAG + "] " + msg + tr);
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.DUMP", TAG);
        final com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(writer, "| ");
        this.mHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.VcnManagementService$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dump$10(pw);
            }
        }, DUMP_TIMEOUT_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$10(com.android.internal.util.IndentingPrintWriter pw) {
        this.mNetworkProvider.dump(pw);
        pw.println();
        this.mTrackingNetworkCallback.dump(pw);
        pw.println();
        synchronized (this.mLock) {
            this.mLastSnapshot.dump(pw);
            pw.println();
            pw.println("mConfigs:");
            pw.increaseIndent();
            for (java.util.Map.Entry<android.os.ParcelUuid, android.net.vcn.VcnConfig> entry : this.mConfigs.entrySet()) {
                pw.println(entry.getKey() + ": " + entry.getValue().getProvisioningPackageName());
            }
            pw.decreaseIndent();
            pw.println();
            pw.println("mVcns:");
            pw.increaseIndent();
            for (com.android.server.vcn.Vcn vcn : this.mVcns.values()) {
                vcn.dump(pw);
            }
            pw.decreaseIndent();
            pw.println();
        }
        pw.println("Local log:");
        pw.increaseIndent();
        LOCAL_LOG.dump(pw);
        pw.decreaseIndent();
        pw.println();
    }

    private class TrackingNetworkCallback extends android.net.ConnectivityManager.NetworkCallback {
        private final java.util.Map<android.net.Network, android.net.NetworkCapabilities> mCaps;
        private final java.util.Map<android.net.Network, android.net.LinkProperties> mLinkProperties;
        private final java.lang.Object mLockObject;

        private TrackingNetworkCallback() {
            this.mLockObject = new java.lang.Object();
            this.mCaps = new android.util.ArrayMap();
            this.mLinkProperties = new android.util.ArrayMap();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(android.net.Network network, android.net.NetworkCapabilities caps) {
            synchronized (this.mLockObject) {
                this.mCaps.put(network, caps);
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(android.net.Network network, android.net.LinkProperties lp) {
            synchronized (this.mLockObject) {
                this.mLinkProperties.put(network, lp);
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(android.net.Network network) {
            synchronized (this.mLockObject) {
                this.mCaps.remove(network);
                this.mLinkProperties.remove(network);
            }
        }

        private java.util.Set<java.lang.Integer> getNonTestTransportTypes(android.net.NetworkCapabilities caps) {
            java.util.Set<java.lang.Integer> transportTypes = new android.util.ArraySet<>();
            for (int t : caps.getTransportTypes()) {
                transportTypes.add(java.lang.Integer.valueOf(t));
            }
            return transportTypes;
        }

        private boolean hasSameTransportsAndCapabilities(android.net.NetworkCapabilities caps, android.net.NetworkCapabilities capsOther) {
            if (!java.util.Objects.equals(getNonTestTransportTypes(caps), getNonTestTransportTypes(capsOther))) {
                return false;
            }
            java.util.Iterator it = android.net.vcn.VcnGatewayConnectionConfig.ALLOWED_CAPABILITIES.iterator();
            while (it.hasNext()) {
                int capability = ((java.lang.Integer) it.next()).intValue();
                if (caps.hasCapability(capability) != capsOther.hasCapability(capability)) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean requiresRestartForImmutableCapabilityChanges(android.net.NetworkCapabilities caps, android.net.LinkProperties lp) {
            if (caps.getSubscriptionIds() == null) {
                return false;
            }
            synchronized (this.mLockObject) {
                for (java.util.Map.Entry<android.net.Network, android.net.LinkProperties> lpEntry : this.mLinkProperties.entrySet()) {
                    if (lp.getInterfaceName() != null && !lp.getInterfaceName().isEmpty() && java.util.Objects.equals(lp.getInterfaceName(), lpEntry.getValue().getInterfaceName())) {
                        return this.mCaps.get(lpEntry.getKey()).hasCapability(13) != caps.hasCapability(13);
                    }
                }
                return false;
            }
        }

        public void dump(com.android.internal.util.IndentingPrintWriter pw) {
            pw.println("TrackingNetworkCallback:");
            pw.increaseIndent();
            pw.println("mCaps:");
            pw.increaseIndent();
            synchronized (this.mCaps) {
                for (java.util.Map.Entry<android.net.Network, android.net.NetworkCapabilities> entry : this.mCaps.entrySet()) {
                    pw.println(entry.getKey() + ": " + entry.getValue());
                }
            }
            pw.decreaseIndent();
            pw.println();
            pw.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class VcnCallbackImpl implements com.android.server.VcnManagementService.VcnCallback {
        private final android.os.ParcelUuid mSubGroup;

        private VcnCallbackImpl(android.os.ParcelUuid subGroup) {
            this.mSubGroup = (android.os.ParcelUuid) java.util.Objects.requireNonNull(subGroup, "Missing subGroup");
        }

        @Override // com.android.server.VcnManagementService.VcnCallback
        public void onSafeModeStatusChanged(boolean isInSafeMode) {
            synchronized (com.android.server.VcnManagementService.this.mLock) {
                if (com.android.server.VcnManagementService.this.mVcns.containsKey(this.mSubGroup)) {
                    int status = isInSafeMode ? 3 : 2;
                    com.android.server.VcnManagementService.this.notifyAllPolicyListenersLocked();
                    com.android.server.VcnManagementService.this.notifyAllPermissionedStatusCallbacksLocked(this.mSubGroup, status);
                }
            }
        }

        @Override // com.android.server.VcnManagementService.VcnCallback
        public void onGatewayConnectionError(final java.lang.String gatewayConnectionName, final int errorCode, final java.lang.String exceptionClass, final java.lang.String exceptionMessage) {
            synchronized (com.android.server.VcnManagementService.this.mLock) {
                if (com.android.server.VcnManagementService.this.mVcns.containsKey(this.mSubGroup)) {
                    for (final com.android.server.VcnManagementService.VcnStatusCallbackInfo cbInfo : com.android.server.VcnManagementService.this.mRegisteredStatusCallbacks.values()) {
                        if (com.android.server.VcnManagementService.this.isCallbackPermissioned(cbInfo, this.mSubGroup)) {
                            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.VcnManagementService$VcnCallbackImpl$$ExternalSyntheticLambda0
                                public final void runOrThrow() throws java.lang.Exception {
                                    this.f$0.lambda$onGatewayConnectionError$0(cbInfo, gatewayConnectionName, errorCode, exceptionClass, exceptionMessage);
                                }
                            });
                        }
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onGatewayConnectionError$0(com.android.server.VcnManagementService.VcnStatusCallbackInfo cbInfo, java.lang.String gatewayConnectionName, int errorCode, java.lang.String exceptionClass, java.lang.String exceptionMessage) throws java.lang.Exception {
            try {
                cbInfo.mCallback.onGatewayConnectionError(gatewayConnectionName, errorCode, exceptionClass, exceptionMessage);
            } catch (android.os.RemoteException e) {
                com.android.server.VcnManagementService.this.logDbg("VcnStatusCallback threw on VCN status change", e);
            }
        }
    }
}
