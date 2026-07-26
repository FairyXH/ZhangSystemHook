package com.android.server.vcn;

/* JADX INFO: loaded from: classes3.dex */
public class TelephonySubscriptionTracker extends android.content.BroadcastReceiver {
    private static final boolean LOG_DBG = false;
    private static final java.lang.String TAG = com.android.server.vcn.TelephonySubscriptionTracker.class.getSimpleName();
    private final com.android.server.vcn.TelephonySubscriptionTracker.ActiveDataSubscriptionIdListener mActiveDataSubIdListener;
    private final com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionTrackerCallback mCallback;
    private final android.telephony.CarrierConfigManager.CarrierConfigChangeListener mCarrierConfigChangeListener;
    private final android.telephony.CarrierConfigManager mCarrierConfigManager;
    private final java.util.List<android.telephony.TelephonyManager.CarrierPrivilegesCallback> mCarrierPrivilegesCallbacks;
    private final android.content.Context mContext;
    private com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot mCurrentSnapshot;
    private final com.android.server.vcn.TelephonySubscriptionTracker.Dependencies mDeps;
    private final android.os.Handler mHandler;
    private final java.util.Map<java.lang.Integer, java.lang.Integer> mReadySubIdsBySlotId;
    private final java.util.Map<java.lang.Integer, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper> mSubIdToCarrierConfigMap;
    private final android.telephony.SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionChangedListener;
    private final android.telephony.SubscriptionManager mSubscriptionManager;
    private final android.telephony.TelephonyManager mTelephonyManager;

    public interface TelephonySubscriptionTrackerCallback {
        void onNewSnapshot(com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int logicalSlotIndex, int subscriptionId, int carrierId, int specificCarrierId) {
        handleActionCarrierConfigChanged(logicalSlotIndex, subscriptionId);
    }

    public TelephonySubscriptionTracker(android.content.Context context, android.os.Handler handler, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionTrackerCallback callback) {
        this(context, handler, callback, new com.android.server.vcn.TelephonySubscriptionTracker.Dependencies());
    }

    TelephonySubscriptionTracker(android.content.Context context, android.os.Handler handler, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionTrackerCallback callback, com.android.server.vcn.TelephonySubscriptionTracker.Dependencies deps) {
        this.mReadySubIdsBySlotId = new java.util.HashMap();
        this.mSubIdToCarrierConfigMap = new java.util.HashMap();
        this.mCarrierPrivilegesCallbacks = new java.util.ArrayList();
        this.mCarrierConfigChangeListener = new android.telephony.CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.server.vcn.TelephonySubscriptionTracker$$ExternalSyntheticLambda1
            @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
            public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                this.f$0.lambda$new$0(i, i2, i3, i4);
            }
        };
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context, "Missing context");
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler, "Missing handler");
        this.mCallback = (com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionTrackerCallback) java.util.Objects.requireNonNull(callback, "Missing callback");
        this.mDeps = (com.android.server.vcn.TelephonySubscriptionTracker.Dependencies) java.util.Objects.requireNonNull(deps, "Missing deps");
        this.mTelephonyManager = (android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class);
        this.mSubscriptionManager = (android.telephony.SubscriptionManager) this.mContext.getSystemService(android.telephony.SubscriptionManager.class);
        this.mCarrierConfigManager = (android.telephony.CarrierConfigManager) this.mContext.getSystemService(android.telephony.CarrierConfigManager.class);
        this.mActiveDataSubIdListener = new com.android.server.vcn.TelephonySubscriptionTracker.ActiveDataSubscriptionIdListener();
        this.mSubscriptionChangedListener = new android.telephony.SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.server.vcn.TelephonySubscriptionTracker.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                com.android.server.vcn.TelephonySubscriptionTracker.this.handleSubscriptionsChanged();
            }
        };
    }

    public void register() {
        java.util.concurrent.Executor handlerExecutor = new android.os.HandlerExecutor(this.mHandler);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.telephony.action.MULTI_SIM_CONFIG_CHANGED");
        this.mContext.registerReceiver(this, filter, null, this.mHandler);
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(handlerExecutor, this.mSubscriptionChangedListener);
        this.mTelephonyManager.registerTelephonyCallback(handlerExecutor, this.mActiveDataSubIdListener);
        if (this.mCarrierConfigManager != null) {
            this.mCarrierConfigManager.registerCarrierConfigChangeListener(handlerExecutor, this.mCarrierConfigChangeListener);
        }
        registerCarrierPrivilegesCallbacks();
    }

    private void registerCarrierPrivilegesCallbacks() {
        java.util.concurrent.Executor handlerExecutor = new android.os.HandlerExecutor(this.mHandler);
        int modemCount = this.mTelephonyManager.getActiveModemCount();
        for (int i = 0; i < modemCount; i++) {
            try {
                android.telephony.TelephonyManager.CarrierPrivilegesCallback carrierPrivilegesCallback = new android.telephony.TelephonyManager.CarrierPrivilegesCallback() { // from class: com.android.server.vcn.TelephonySubscriptionTracker.2
                    public void onCarrierPrivilegesChanged(java.util.Set<java.lang.String> privilegedPackageNames, java.util.Set<java.lang.Integer> privilegedUids) {
                        com.android.server.vcn.TelephonySubscriptionTracker.this.handleSubscriptionsChanged();
                    }
                };
                this.mTelephonyManager.registerCarrierPrivilegesCallback(i, handlerExecutor, carrierPrivilegesCallback);
                this.mCarrierPrivilegesCallbacks.add(carrierPrivilegesCallback);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.wtf(TAG, "Encounted exception registering carrier privileges listeners", e);
                return;
            }
        }
    }

    public void unregister() {
        this.mContext.unregisterReceiver(this);
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mSubscriptionChangedListener);
        this.mTelephonyManager.unregisterTelephonyCallback(this.mActiveDataSubIdListener);
        if (this.mCarrierConfigManager != null) {
            this.mCarrierConfigManager.unregisterCarrierConfigChangeListener(this.mCarrierConfigChangeListener);
        }
        unregisterCarrierPrivilegesCallbacks();
    }

    private void unregisterCarrierPrivilegesCallbacks() {
        for (android.telephony.TelephonyManager.CarrierPrivilegesCallback carrierPrivilegesCallback : this.mCarrierPrivilegesCallbacks) {
            this.mTelephonyManager.unregisterCarrierPrivilegesCallback(carrierPrivilegesCallback);
        }
        this.mCarrierPrivilegesCallbacks.clear();
    }

    public void handleSubscriptionsChanged() {
        java.util.Map<android.os.ParcelUuid, java.util.Set<java.lang.String>> privilegedPackages = new java.util.HashMap<>();
        java.util.Map<java.lang.Integer, android.telephony.SubscriptionInfo> newSubIdToInfoMap = new java.util.HashMap<>();
        java.util.List<android.telephony.SubscriptionInfo> allSubs = this.mSubscriptionManager.getAllSubscriptionInfoList();
        if (allSubs == null) {
            return;
        }
        for (android.telephony.SubscriptionInfo subInfo : allSubs) {
            if (subInfo.getGroupUuid() != null) {
                newSubIdToInfoMap.put(java.lang.Integer.valueOf(subInfo.getSubscriptionId()), subInfo);
                if (subInfo.getSimSlotIndex() != -1 && this.mReadySubIdsBySlotId.values().contains(java.lang.Integer.valueOf(subInfo.getSubscriptionId()))) {
                    android.telephony.TelephonyManager subIdSpecificTelephonyManager = this.mTelephonyManager.createForSubscriptionId(subInfo.getSubscriptionId());
                    android.os.ParcelUuid subGroup = subInfo.getGroupUuid();
                    java.util.Set<java.lang.String> pkgs = privilegedPackages.getOrDefault(subGroup, new android.util.ArraySet<>());
                    pkgs.addAll(subIdSpecificTelephonyManager.getPackagesWithCarrierPrivileges());
                    privilegedPackages.put(subGroup, pkgs);
                }
            }
        }
        final com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot newSnapshot = new com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot(this.mDeps.getActiveDataSubscriptionId(), newSubIdToInfoMap, this.mSubIdToCarrierConfigMap, privilegedPackages);
        if (!newSnapshot.equals(this.mCurrentSnapshot)) {
            this.mCurrentSnapshot = newSnapshot;
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.vcn.TelephonySubscriptionTracker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$handleSubscriptionsChanged$1(newSnapshot);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSubscriptionsChanged$1(com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot newSnapshot) {
        this.mCallback.onNewSnapshot(newSnapshot);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        byte b;
        java.lang.String action = intent.getAction();
        switch (action.hashCode()) {
            case 1093296680:
                if (action.equals("android.telephony.action.MULTI_SIM_CONFIG_CHANGED")) {
                    b = 0;
                    break;
                }
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                handleActionMultiSimConfigChanged(context, intent);
                break;
            default:
                android.util.Slog.v(TAG, "Unknown intent received with action: " + intent.getAction());
                break;
        }
    }

    private void handleActionMultiSimConfigChanged(android.content.Context context, android.content.Intent intent) {
        unregisterCarrierPrivilegesCallbacks();
        int modemCount = this.mTelephonyManager.getActiveModemCount();
        java.util.Iterator<java.lang.Integer> slotIdIterator = this.mReadySubIdsBySlotId.keySet().iterator();
        while (slotIdIterator.hasNext()) {
            int slotId = slotIdIterator.next().intValue();
            if (slotId >= modemCount) {
                slotIdIterator.remove();
            }
        }
        registerCarrierPrivilegesCallbacks();
        handleSubscriptionsChanged();
    }

    private void handleActionCarrierConfigChanged(int slotId, int subId) {
        android.os.PersistableBundle carrierConfig;
        if (slotId == -1) {
            return;
        }
        if (android.telephony.SubscriptionManager.isValidSubscriptionId(subId)) {
            if (com.android.internal.telephony.flags.Flags.fixCrashOnGettingConfigWhenPhoneIsGone()) {
                carrierConfig = android.telephony.CarrierConfigManager.getCarrierConfigSubset(this.mContext, subId, android.net.vcn.VcnManager.VCN_RELATED_CARRIER_CONFIG_KEYS);
            } else {
                carrierConfig = this.mCarrierConfigManager.getConfigForSubId(subId, android.net.vcn.VcnManager.VCN_RELATED_CARRIER_CONFIG_KEYS);
            }
            if (this.mDeps.isConfigForIdentifiedCarrier(carrierConfig)) {
                this.mReadySubIdsBySlotId.put(java.lang.Integer.valueOf(slotId), java.lang.Integer.valueOf(subId));
                if (!carrierConfig.isEmpty()) {
                    this.mSubIdToCarrierConfigMap.put(java.lang.Integer.valueOf(subId), new com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper(carrierConfig));
                }
                handleSubscriptionsChanged();
                return;
            }
            return;
        }
        java.lang.Integer oldSubid = this.mReadySubIdsBySlotId.remove(java.lang.Integer.valueOf(slotId));
        if (oldSubid != null) {
            this.mSubIdToCarrierConfigMap.remove(oldSubid);
        }
        handleSubscriptionsChanged();
    }

    void setReadySubIdsBySlotId(java.util.Map<java.lang.Integer, java.lang.Integer> readySubIdsBySlotId) {
        this.mReadySubIdsBySlotId.clear();
        this.mReadySubIdsBySlotId.putAll(readySubIdsBySlotId);
    }

    void setSubIdToCarrierConfigMap(java.util.Map<java.lang.Integer, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper> subIdToCarrierConfigMap) {
        this.mSubIdToCarrierConfigMap.clear();
        this.mSubIdToCarrierConfigMap.putAll(subIdToCarrierConfigMap);
    }

    java.util.Map<java.lang.Integer, java.lang.Integer> getReadySubIdsBySlotId() {
        return java.util.Collections.unmodifiableMap(this.mReadySubIdsBySlotId);
    }

    java.util.Map<java.lang.Integer, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper> getSubIdToCarrierConfigMap() {
        return java.util.Collections.unmodifiableMap(this.mSubIdToCarrierConfigMap);
    }

    public static class TelephonySubscriptionSnapshot {
        public static final com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot EMPTY_SNAPSHOT = new com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot(-1, java.util.Collections.emptyMap(), java.util.Collections.emptyMap(), java.util.Collections.emptyMap());
        private final int mActiveDataSubId;
        private final java.util.Map<android.os.ParcelUuid, java.util.Set<java.lang.String>> mPrivilegedPackages;
        private final java.util.Map<java.lang.Integer, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper> mSubIdToCarrierConfigMap;
        private final java.util.Map<java.lang.Integer, android.telephony.SubscriptionInfo> mSubIdToInfoMap;

        TelephonySubscriptionSnapshot(int activeDataSubId, java.util.Map<java.lang.Integer, android.telephony.SubscriptionInfo> subIdToInfoMap, java.util.Map<java.lang.Integer, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper> subIdToCarrierConfigMap, java.util.Map<android.os.ParcelUuid, java.util.Set<java.lang.String>> privilegedPackages) {
            this.mActiveDataSubId = activeDataSubId;
            java.util.Objects.requireNonNull(subIdToInfoMap, "subIdToInfoMap was null");
            java.util.Objects.requireNonNull(privilegedPackages, "privilegedPackages was null");
            java.util.Objects.requireNonNull(subIdToCarrierConfigMap, "subIdToCarrierConfigMap was null");
            this.mSubIdToInfoMap = java.util.Collections.unmodifiableMap(new java.util.HashMap(subIdToInfoMap));
            this.mSubIdToCarrierConfigMap = java.util.Collections.unmodifiableMap(new java.util.HashMap(subIdToCarrierConfigMap));
            java.util.Map<android.os.ParcelUuid, java.util.Set<java.lang.String>> unmodifiableInnerSets = new android.util.ArrayMap<>();
            for (java.util.Map.Entry<android.os.ParcelUuid, java.util.Set<java.lang.String>> entry : privilegedPackages.entrySet()) {
                unmodifiableInnerSets.put(entry.getKey(), java.util.Collections.unmodifiableSet(entry.getValue()));
            }
            this.mPrivilegedPackages = java.util.Collections.unmodifiableMap(unmodifiableInnerSets);
        }

        public int getActiveDataSubscriptionId() {
            return this.mActiveDataSubId;
        }

        public android.os.ParcelUuid getActiveDataSubscriptionGroup() {
            android.telephony.SubscriptionInfo info = this.mSubIdToInfoMap.get(java.lang.Integer.valueOf(getActiveDataSubscriptionId()));
            if (info == null) {
                return null;
            }
            return info.getGroupUuid();
        }

        public java.util.Set<android.os.ParcelUuid> getActiveSubscriptionGroups() {
            return this.mPrivilegedPackages.keySet();
        }

        public boolean packageHasPermissionsForSubscriptionGroup(android.os.ParcelUuid subGrp, java.lang.String packageName) {
            java.util.Set<java.lang.String> privilegedPackages = this.mPrivilegedPackages.get(subGrp);
            return privilegedPackages != null && privilegedPackages.contains(packageName);
        }

        public android.os.ParcelUuid getGroupForSubId(int subId) {
            if (this.mSubIdToInfoMap.containsKey(java.lang.Integer.valueOf(subId))) {
                return this.mSubIdToInfoMap.get(java.lang.Integer.valueOf(subId)).getGroupUuid();
            }
            return null;
        }

        public java.util.Set<java.lang.Integer> getAllSubIdsInGroup(android.os.ParcelUuid subGrp) {
            java.util.Set<java.lang.Integer> subIds = new android.util.ArraySet<>();
            for (java.util.Map.Entry<java.lang.Integer, android.telephony.SubscriptionInfo> entry : this.mSubIdToInfoMap.entrySet()) {
                if (subGrp.equals(entry.getValue().getGroupUuid())) {
                    subIds.add(entry.getKey());
                }
            }
            return subIds;
        }

        public boolean isOpportunistic(int subId) {
            if (this.mSubIdToInfoMap.containsKey(java.lang.Integer.valueOf(subId))) {
                return this.mSubIdToInfoMap.get(java.lang.Integer.valueOf(subId)).isOpportunistic();
            }
            return false;
        }

        public com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper getCarrierConfigForSubGrp(android.os.ParcelUuid subGrp) {
            com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper result = null;
            java.util.Iterator<java.lang.Integer> it = getAllSubIdsInGroup(subGrp).iterator();
            while (it.hasNext()) {
                int subId = it.next().intValue();
                com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper config = this.mSubIdToCarrierConfigMap.get(java.lang.Integer.valueOf(subId));
                if (config != null) {
                    result = config;
                    if (!isOpportunistic(subId)) {
                        return config;
                    }
                }
            }
            return result;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.mActiveDataSubId), this.mSubIdToInfoMap, this.mSubIdToCarrierConfigMap, this.mPrivilegedPackages);
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot)) {
                return false;
            }
            com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot other = (com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot) obj;
            return this.mActiveDataSubId == other.mActiveDataSubId && this.mSubIdToInfoMap.equals(other.mSubIdToInfoMap) && this.mSubIdToCarrierConfigMap.equals(other.mSubIdToCarrierConfigMap) && this.mPrivilegedPackages.equals(other.mPrivilegedPackages);
        }

        public void dump(com.android.internal.util.IndentingPrintWriter pw) {
            pw.println("TelephonySubscriptionSnapshot:");
            pw.increaseIndent();
            pw.println("mActiveDataSubId: " + this.mActiveDataSubId);
            pw.println("mSubIdToInfoMap: " + this.mSubIdToInfoMap);
            pw.println("mSubIdToCarrierConfigMap: " + this.mSubIdToCarrierConfigMap);
            pw.println("mPrivilegedPackages: " + this.mPrivilegedPackages);
            pw.decreaseIndent();
        }

        public java.lang.String toString() {
            return "TelephonySubscriptionSnapshot{ mActiveDataSubId=" + this.mActiveDataSubId + ", mSubIdToInfoMap=" + this.mSubIdToInfoMap + ", mSubIdToCarrierConfigMap=" + this.mSubIdToCarrierConfigMap + ", mPrivilegedPackages=" + this.mPrivilegedPackages + " }";
        }
    }

    private class ActiveDataSubscriptionIdListener extends android.telephony.TelephonyCallback implements android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener {
        private ActiveDataSubscriptionIdListener() {
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int subId) {
            com.android.server.vcn.TelephonySubscriptionTracker.this.handleSubscriptionsChanged();
        }
    }

    public static class Dependencies {
        public boolean isConfigForIdentifiedCarrier(android.os.PersistableBundle bundle) {
            return android.telephony.CarrierConfigManager.isConfigForIdentifiedCarrier(bundle);
        }

        public int getActiveDataSubscriptionId() {
            return android.telephony.SubscriptionManager.getActiveDataSubscriptionId();
        }
    }
}
