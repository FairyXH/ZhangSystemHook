package com.android.server.vcn.routeselection;

/* JADX INFO: loaded from: classes3.dex */
public class UnderlyingNetworkController {
    private static final java.lang.String TAG = com.android.server.vcn.routeselection.UnderlyingNetworkController.class.getSimpleName();
    private final android.telephony.TelephonyCallback mActiveDataSubIdListener;
    private com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper mCarrierConfig;
    private final com.android.server.vcn.routeselection.UnderlyingNetworkController.UnderlyingNetworkControllerCallback mCb;
    private final java.util.List<android.net.ConnectivityManager.NetworkCallback> mCellBringupCallbacks;
    private final android.net.vcn.VcnGatewayConnectionConfig mConnectionConfig;
    private final android.net.ConnectivityManager mConnectivityManager;
    private com.android.server.vcn.routeselection.UnderlyingNetworkRecord mCurrentRecord;
    private final com.android.server.vcn.routeselection.UnderlyingNetworkController.Dependencies mDeps;
    private final android.os.Handler mHandler;
    private boolean mIsQuitting;
    private com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot mLastSnapshot;
    private com.android.server.vcn.routeselection.UnderlyingNetworkRecord.Builder mRecordInProgress;
    private com.android.server.vcn.routeselection.UnderlyingNetworkController.UnderlyingNetworkListener mRouteSelectionCallback;
    private final android.os.ParcelUuid mSubscriptionGroup;
    private final java.util.Map<android.net.Network, com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator> mUnderlyingNetworkRecords;
    private final com.android.server.vcn.VcnContext mVcnContext;
    private android.net.ConnectivityManager.NetworkCallback mWifiBringupCallback;
    private android.net.ConnectivityManager.NetworkCallback mWifiEntryRssiThresholdCallback;
    private android.net.ConnectivityManager.NetworkCallback mWifiExitRssiThresholdCallback;

    public interface UnderlyingNetworkControllerCallback {
        void onSelectedUnderlyingNetworkChanged(com.android.server.vcn.routeselection.UnderlyingNetworkRecord underlyingNetworkRecord);
    }

    public UnderlyingNetworkController(com.android.server.vcn.VcnContext vcnContext, android.net.vcn.VcnGatewayConnectionConfig connectionConfig, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, com.android.server.vcn.routeselection.UnderlyingNetworkController.UnderlyingNetworkControllerCallback cb) {
        this(vcnContext, connectionConfig, subscriptionGroup, snapshot, cb, new com.android.server.vcn.routeselection.UnderlyingNetworkController.Dependencies());
    }

    UnderlyingNetworkController(com.android.server.vcn.VcnContext vcnContext, android.net.vcn.VcnGatewayConnectionConfig connectionConfig, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, com.android.server.vcn.routeselection.UnderlyingNetworkController.UnderlyingNetworkControllerCallback cb, com.android.server.vcn.routeselection.UnderlyingNetworkController.Dependencies deps) {
        this.mActiveDataSubIdListener = new com.android.server.vcn.routeselection.UnderlyingNetworkController.VcnActiveDataSubscriptionIdListener();
        this.mUnderlyingNetworkRecords = new android.util.ArrayMap();
        this.mCellBringupCallbacks = new java.util.ArrayList();
        this.mIsQuitting = false;
        this.mVcnContext = (com.android.server.vcn.VcnContext) java.util.Objects.requireNonNull(vcnContext, "Missing vcnContext");
        this.mConnectionConfig = (android.net.vcn.VcnGatewayConnectionConfig) java.util.Objects.requireNonNull(connectionConfig, "Missing connectionConfig");
        this.mSubscriptionGroup = (android.os.ParcelUuid) java.util.Objects.requireNonNull(subscriptionGroup, "Missing subscriptionGroup");
        this.mLastSnapshot = (com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot) java.util.Objects.requireNonNull(snapshot, "Missing snapshot");
        this.mCb = (com.android.server.vcn.routeselection.UnderlyingNetworkController.UnderlyingNetworkControllerCallback) java.util.Objects.requireNonNull(cb, "Missing cb");
        this.mDeps = (com.android.server.vcn.routeselection.UnderlyingNetworkController.Dependencies) java.util.Objects.requireNonNull(deps, "Missing deps");
        this.mHandler = new android.os.Handler(this.mVcnContext.getLooper());
        this.mConnectivityManager = (android.net.ConnectivityManager) this.mVcnContext.getContext().getSystemService(android.net.ConnectivityManager.class);
        ((android.telephony.TelephonyManager) this.mVcnContext.getContext().getSystemService(android.telephony.TelephonyManager.class)).registerTelephonyCallback(new android.os.HandlerExecutor(this.mHandler), this.mActiveDataSubIdListener);
        this.mCarrierConfig = this.mLastSnapshot.getCarrierConfigForSubGrp(this.mSubscriptionGroup);
        registerOrUpdateNetworkRequests();
    }

    private static class CapabilityMatchCriteria {
        public final int capability;
        public final int matchCriteria;

        CapabilityMatchCriteria(int capability, int matchCriteria) {
            this.capability = capability;
            this.matchCriteria = matchCriteria;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.capability), java.lang.Integer.valueOf(this.matchCriteria));
        }

        public boolean equals(java.lang.Object other) {
            if (!(other instanceof com.android.server.vcn.routeselection.UnderlyingNetworkController.CapabilityMatchCriteria)) {
                return false;
            }
            com.android.server.vcn.routeselection.UnderlyingNetworkController.CapabilityMatchCriteria rhs = (com.android.server.vcn.routeselection.UnderlyingNetworkController.CapabilityMatchCriteria) other;
            return this.capability == rhs.capability && this.matchCriteria == rhs.matchCriteria;
        }
    }

    private static java.util.Set<java.util.Set<com.android.server.vcn.routeselection.UnderlyingNetworkController.CapabilityMatchCriteria>> dedupAndGetCapRequirementsForCell(android.net.vcn.VcnGatewayConnectionConfig connectionConfig) {
        java.util.Set<java.util.Set<com.android.server.vcn.routeselection.UnderlyingNetworkController.CapabilityMatchCriteria>> dedupedCapsMatchSets = new android.util.ArraySet<>();
        for (android.net.vcn.VcnUnderlyingNetworkTemplate template : connectionConfig.getVcnUnderlyingNetworkPriorities()) {
            if (template instanceof android.net.vcn.VcnCellUnderlyingNetworkTemplate) {
                java.util.Set<com.android.server.vcn.routeselection.UnderlyingNetworkController.CapabilityMatchCriteria> capsMatchSet = new android.util.ArraySet<>();
                for (java.util.Map.Entry<java.lang.Integer, java.lang.Integer> entry : ((android.net.vcn.VcnCellUnderlyingNetworkTemplate) template).getCapabilitiesMatchCriteria().entrySet()) {
                    int capability = entry.getKey().intValue();
                    int matchCriteria = entry.getValue().intValue();
                    if (matchCriteria != 0) {
                        capsMatchSet.add(new com.android.server.vcn.routeselection.UnderlyingNetworkController.CapabilityMatchCriteria(capability, matchCriteria));
                    }
                }
                dedupedCapsMatchSets.add(capsMatchSet);
            }
        }
        dedupedCapsMatchSets.add(java.util.Collections.singleton(new com.android.server.vcn.routeselection.UnderlyingNetworkController.CapabilityMatchCriteria(12, 1)));
        return dedupedCapsMatchSets;
    }

    private void registerOrUpdateNetworkRequests() {
        android.net.ConnectivityManager.NetworkCallback oldRouteSelectionCallback = this.mRouteSelectionCallback;
        android.net.ConnectivityManager.NetworkCallback oldWifiCallback = this.mWifiBringupCallback;
        android.net.ConnectivityManager.NetworkCallback oldWifiEntryRssiThresholdCallback = this.mWifiEntryRssiThresholdCallback;
        android.net.ConnectivityManager.NetworkCallback oldWifiExitRssiThresholdCallback = this.mWifiExitRssiThresholdCallback;
        java.util.List<android.net.ConnectivityManager.NetworkCallback> oldCellCallbacks = new java.util.ArrayList<>(this.mCellBringupCallbacks);
        this.mCellBringupCallbacks.clear();
        if (this.mVcnContext.isFlagNetworkMetricMonitorEnabled() && this.mVcnContext.isFlagIpSecTransformStateEnabled()) {
            for (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator evaluator : this.mUnderlyingNetworkRecords.values()) {
                evaluator.close();
            }
        }
        this.mUnderlyingNetworkRecords.clear();
        if (!this.mIsQuitting) {
            this.mRouteSelectionCallback = new com.android.server.vcn.routeselection.UnderlyingNetworkController.UnderlyingNetworkListener();
            this.mConnectivityManager.registerNetworkCallback(getRouteSelectionRequest(), this.mRouteSelectionCallback, this.mHandler);
            this.mWifiEntryRssiThresholdCallback = new com.android.server.vcn.routeselection.UnderlyingNetworkController.NetworkBringupCallback();
            this.mConnectivityManager.registerNetworkCallback(getWifiEntryRssiThresholdNetworkRequest(), this.mWifiEntryRssiThresholdCallback, this.mHandler);
            this.mWifiExitRssiThresholdCallback = new com.android.server.vcn.routeselection.UnderlyingNetworkController.NetworkBringupCallback();
            this.mConnectivityManager.registerNetworkCallback(getWifiExitRssiThresholdNetworkRequest(), this.mWifiExitRssiThresholdCallback, this.mHandler);
            this.mWifiBringupCallback = new com.android.server.vcn.routeselection.UnderlyingNetworkController.NetworkBringupCallback();
            this.mConnectivityManager.requestBackgroundNetwork(getWifiNetworkRequest(), this.mWifiBringupCallback, this.mHandler);
            java.util.Iterator<java.lang.Integer> it = this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup).iterator();
            while (it.hasNext()) {
                int subId = it.next().intValue();
                for (java.util.Set<com.android.server.vcn.routeselection.UnderlyingNetworkController.CapabilityMatchCriteria> capsMatchCriteria : dedupAndGetCapRequirementsForCell(this.mConnectionConfig)) {
                    com.android.server.vcn.routeselection.UnderlyingNetworkController.NetworkBringupCallback cb = new com.android.server.vcn.routeselection.UnderlyingNetworkController.NetworkBringupCallback();
                    this.mCellBringupCallbacks.add(cb);
                    this.mConnectivityManager.requestBackgroundNetwork(getCellNetworkRequestForSubId(subId, capsMatchCriteria), cb, this.mHandler);
                }
            }
        } else {
            this.mRouteSelectionCallback = null;
            this.mWifiBringupCallback = null;
            this.mWifiEntryRssiThresholdCallback = null;
            this.mWifiExitRssiThresholdCallback = null;
        }
        if (oldRouteSelectionCallback != null) {
            this.mConnectivityManager.unregisterNetworkCallback(oldRouteSelectionCallback);
        }
        if (oldWifiCallback != null) {
            this.mConnectivityManager.unregisterNetworkCallback(oldWifiCallback);
        }
        if (oldWifiEntryRssiThresholdCallback != null) {
            this.mConnectivityManager.unregisterNetworkCallback(oldWifiEntryRssiThresholdCallback);
        }
        if (oldWifiExitRssiThresholdCallback != null) {
            this.mConnectivityManager.unregisterNetworkCallback(oldWifiExitRssiThresholdCallback);
        }
        for (android.net.ConnectivityManager.NetworkCallback cellBringupCallback : oldCellCallbacks) {
            this.mConnectivityManager.unregisterNetworkCallback(cellBringupCallback);
        }
    }

    private android.net.NetworkRequest getRouteSelectionRequest() {
        if (this.mVcnContext.isInTestMode()) {
            return getTestNetworkRequest(this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup));
        }
        return getBaseNetworkRequestBuilder().addCapability(16).addCapability(21).setSubscriptionIds(this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup)).build();
    }

    private android.net.NetworkRequest.Builder getBaseWifiNetworkRequestBuilder() {
        return getBaseNetworkRequestBuilder().addTransportType(1).addCapability(12).setSubscriptionIds(this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup));
    }

    private android.net.NetworkRequest getWifiNetworkRequest() {
        return getBaseWifiNetworkRequestBuilder().build();
    }

    private android.net.NetworkRequest getWifiEntryRssiThresholdNetworkRequest() {
        return getBaseWifiNetworkRequestBuilder().setSignalStrength(com.android.server.vcn.routeselection.NetworkPriorityClassifier.getWifiEntryRssiThreshold(this.mCarrierConfig)).build();
    }

    private android.net.NetworkRequest getWifiExitRssiThresholdNetworkRequest() {
        return getBaseWifiNetworkRequestBuilder().setSignalStrength(com.android.server.vcn.routeselection.NetworkPriorityClassifier.getWifiExitRssiThreshold(this.mCarrierConfig)).build();
    }

    private android.net.NetworkRequest getCellNetworkRequestForSubId(int subId, java.util.Set<com.android.server.vcn.routeselection.UnderlyingNetworkController.CapabilityMatchCriteria> capsMatchCriteria) {
        android.net.NetworkRequest.Builder nrBuilder = getBaseNetworkRequestBuilder().addTransportType(0).setNetworkSpecifier(new android.net.TelephonyNetworkSpecifier(subId));
        for (com.android.server.vcn.routeselection.UnderlyingNetworkController.CapabilityMatchCriteria capMatchCriteria : capsMatchCriteria) {
            int cap = capMatchCriteria.capability;
            int matchCriteria = capMatchCriteria.matchCriteria;
            if (matchCriteria == 1) {
                nrBuilder.addCapability(cap);
            } else if (matchCriteria == 2) {
                nrBuilder.addForbiddenCapability(cap);
            }
        }
        return nrBuilder.build();
    }

    private android.net.NetworkRequest.Builder getBaseNetworkRequestBuilder() {
        return new android.net.NetworkRequest.Builder().removeCapability(14).removeCapability(13).removeCapability(28);
    }

    private android.net.NetworkRequest getTestNetworkRequest(java.util.Set<java.lang.Integer> subIds) {
        return new android.net.NetworkRequest.Builder().clearCapabilities().addTransportType(7).setSubscriptionIds(subIds).build();
    }

    public void updateSubscriptionSnapshot(com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot newSnapshot) {
        java.util.Objects.requireNonNull(newSnapshot, "Missing newSnapshot");
        com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot oldSnapshot = this.mLastSnapshot;
        this.mLastSnapshot = newSnapshot;
        this.mCarrierConfig = this.mLastSnapshot.getCarrierConfigForSubGrp(this.mSubscriptionGroup);
        for (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator evaluator : this.mUnderlyingNetworkRecords.values()) {
            evaluator.reevaluate(this.mConnectionConfig.getVcnUnderlyingNetworkPriorities(), this.mSubscriptionGroup, this.mLastSnapshot, this.mCarrierConfig);
        }
        if (oldSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup).equals(newSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup))) {
            if (this.mVcnContext.isFlagNetworkMetricMonitorEnabled() && this.mVcnContext.isFlagIpSecTransformStateEnabled()) {
                reevaluateNetworks();
                return;
            }
            return;
        }
        registerOrUpdateNetworkRequests();
    }

    public void updateInboundTransform(com.android.server.vcn.routeselection.UnderlyingNetworkRecord currentNetwork, android.net.IpSecTransform transform) {
        if (!this.mVcnContext.isFlagNetworkMetricMonitorEnabled() || !this.mVcnContext.isFlagIpSecTransformStateEnabled()) {
            logWtf("#updateInboundTransform: unexpected call; flags missing");
            return;
        }
        java.util.Objects.requireNonNull(currentNetwork, "currentNetwork is null");
        java.util.Objects.requireNonNull(transform, "transform is null");
        if (this.mCurrentRecord == null || this.mRouteSelectionCallback == null || !java.util.Objects.equals(currentNetwork.network, this.mCurrentRecord.network)) {
            return;
        }
        this.mUnderlyingNetworkRecords.get(this.mCurrentRecord.network).setInboundTransform(transform);
    }

    public void teardown() {
        this.mVcnContext.ensureRunningOnLooperThread();
        this.mIsQuitting = true;
        registerOrUpdateNetworkRequests();
        ((android.telephony.TelephonyManager) this.mVcnContext.getContext().getSystemService(android.telephony.TelephonyManager.class)).unregisterTelephonyCallback(this.mActiveDataSubIdListener);
    }

    private java.util.TreeSet<com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator> getSortedUnderlyingNetworks() {
        java.util.TreeSet<com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator> sorted = new java.util.TreeSet<>(com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.getComparator(this.mVcnContext));
        for (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator evaluator : this.mUnderlyingNetworkRecords.values()) {
            if (evaluator.getPriorityClass() != -1) {
                sorted.add(evaluator);
            }
        }
        return sorted;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reevaluateNetworks() {
        if (this.mIsQuitting || this.mRouteSelectionCallback == null) {
            return;
        }
        java.util.TreeSet<com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator> sorted = getSortedUnderlyingNetworks();
        com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator candidateEvaluator = sorted.isEmpty() ? null : sorted.first();
        com.android.server.vcn.routeselection.UnderlyingNetworkRecord candidate = candidateEvaluator == null ? null : candidateEvaluator.getNetworkRecord();
        if (java.util.Objects.equals(this.mCurrentRecord, candidate)) {
            return;
        }
        java.lang.String allNetworkPriorities = "";
        for (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator recordEvaluator : sorted) {
            if (!allNetworkPriorities.isEmpty()) {
                allNetworkPriorities = allNetworkPriorities + ", ";
            }
            allNetworkPriorities = allNetworkPriorities + recordEvaluator.getNetwork() + ": " + recordEvaluator.getPriorityClass();
        }
        if (!com.android.server.vcn.routeselection.UnderlyingNetworkRecord.isSameNetwork(this.mCurrentRecord, candidate)) {
            logInfo("Selected network changed to " + (candidate != null ? candidate.network : null) + ", selected from list: " + allNetworkPriorities);
        }
        this.mCurrentRecord = candidate;
        this.mCb.onSelectedUnderlyingNetworkChanged(this.mCurrentRecord);
        java.util.Iterator<com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator> it = this.mUnderlyingNetworkRecords.values().iterator();
        while (it.hasNext()) {
            com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator evaluator = it.next();
            evaluator.setIsSelected(candidateEvaluator == evaluator, this.mConnectionConfig.getVcnUnderlyingNetworkPriorities(), this.mSubscriptionGroup, this.mLastSnapshot, this.mCarrierConfig);
        }
    }

    class NetworkBringupCallback extends android.net.ConnectivityManager.NetworkCallback {
        NetworkBringupCallback() {
        }
    }

    class UnderlyingNetworkListener extends android.net.ConnectivityManager.NetworkCallback {
        UnderlyingNetworkListener() {
            super(1);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(android.net.Network network) {
            com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mUnderlyingNetworkRecords.put(network, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mDeps.newUnderlyingNetworkEvaluator(com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mVcnContext, network, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mConnectionConfig.getVcnUnderlyingNetworkPriorities(), com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mSubscriptionGroup, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mLastSnapshot, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mCarrierConfig, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.new NetworkEvaluatorCallbackImpl()));
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(android.net.Network network) {
            if (com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mVcnContext.isFlagNetworkMetricMonitorEnabled() && com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mVcnContext.isFlagIpSecTransformStateEnabled()) {
                ((com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator) com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mUnderlyingNetworkRecords.get(network)).close();
            }
            com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mUnderlyingNetworkRecords.remove(network);
            com.android.server.vcn.routeselection.UnderlyingNetworkController.this.reevaluateNetworks();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(android.net.Network network, android.net.NetworkCapabilities networkCapabilities) {
            com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator evaluator = (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator) com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mUnderlyingNetworkRecords.get(network);
            if (evaluator == null) {
                com.android.server.vcn.routeselection.UnderlyingNetworkController.this.logWtf("Got capabilities change for unknown key: " + network);
                return;
            }
            evaluator.setNetworkCapabilities(networkCapabilities, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mConnectionConfig.getVcnUnderlyingNetworkPriorities(), com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mSubscriptionGroup, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mLastSnapshot, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mCarrierConfig);
            if (evaluator.isValid()) {
                com.android.server.vcn.routeselection.UnderlyingNetworkController.this.reevaluateNetworks();
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(android.net.Network network, android.net.LinkProperties linkProperties) {
            com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator evaluator = (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator) com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mUnderlyingNetworkRecords.get(network);
            if (evaluator == null) {
                com.android.server.vcn.routeselection.UnderlyingNetworkController.this.logWtf("Got link properties change for unknown key: " + network);
                return;
            }
            evaluator.setLinkProperties(linkProperties, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mConnectionConfig.getVcnUnderlyingNetworkPriorities(), com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mSubscriptionGroup, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mLastSnapshot, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mCarrierConfig);
            if (evaluator.isValid()) {
                com.android.server.vcn.routeselection.UnderlyingNetworkController.this.reevaluateNetworks();
            }
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onBlockedStatusChanged(android.net.Network network, boolean isBlocked) {
            com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator evaluator = (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator) com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mUnderlyingNetworkRecords.get(network);
            if (evaluator == null) {
                com.android.server.vcn.routeselection.UnderlyingNetworkController.this.logWtf("Got blocked status change for unknown key: " + network);
                return;
            }
            evaluator.setIsBlocked(isBlocked, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mConnectionConfig.getVcnUnderlyingNetworkPriorities(), com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mSubscriptionGroup, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mLastSnapshot, com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mCarrierConfig);
            if (evaluator.isValid()) {
                com.android.server.vcn.routeselection.UnderlyingNetworkController.this.reevaluateNetworks();
            }
        }
    }

    class NetworkEvaluatorCallbackImpl implements com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.NetworkEvaluatorCallback {
        NetworkEvaluatorCallbackImpl() {
        }

        @Override // com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.NetworkEvaluatorCallback
        public void onEvaluationResultChanged() {
            if (!com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mVcnContext.isFlagNetworkMetricMonitorEnabled() || !com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mVcnContext.isFlagIpSecTransformStateEnabled()) {
                com.android.server.vcn.routeselection.UnderlyingNetworkController.this.logWtf("#onEvaluationResultChanged: unexpected call; flags missing");
            } else {
                com.android.server.vcn.routeselection.UnderlyingNetworkController.this.mVcnContext.ensureRunningOnLooperThread();
                com.android.server.vcn.routeselection.UnderlyingNetworkController.this.reevaluateNetworks();
            }
        }
    }

    private java.lang.String getLogPrefix() {
        return "(" + com.android.server.vcn.util.LogUtils.getHashedSubscriptionGroup(this.mSubscriptionGroup) + "-" + this.mConnectionConfig.getGatewayConnectionName() + "-" + java.lang.System.identityHashCode(this) + ") ";
    }

    private java.lang.String getTagLogPrefix() {
        return "[ " + TAG + " " + getLogPrefix() + "]";
    }

    private void logInfo(java.lang.String msg) {
        android.util.Slog.i(TAG, getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[INFO] " + getTagLogPrefix() + msg);
    }

    private void logInfo(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.i(TAG, getLogPrefix() + msg, tr);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[INFO] " + getTagLogPrefix() + msg + tr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logWtf(java.lang.String msg) {
        android.util.Slog.wtf(TAG, msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log(TAG + "[WTF ] " + getTagLogPrefix() + msg);
    }

    private void logWtf(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.wtf(TAG, msg, tr);
        com.android.server.VcnManagementService.LOCAL_LOG.log(TAG + "[WTF ] " + getTagLogPrefix() + msg + tr);
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("UnderlyingNetworkController:");
        pw.increaseIndent();
        pw.println("Carrier WiFi Entry Threshold: " + com.android.server.vcn.routeselection.NetworkPriorityClassifier.getWifiEntryRssiThreshold(this.mCarrierConfig));
        pw.println("Carrier WiFi Exit Threshold: " + com.android.server.vcn.routeselection.NetworkPriorityClassifier.getWifiExitRssiThreshold(this.mCarrierConfig));
        pw.println("Currently selected: " + (this.mCurrentRecord == null ? null : this.mCurrentRecord.network));
        pw.println("VcnUnderlyingNetworkTemplate list:");
        pw.increaseIndent();
        int index = 0;
        for (android.net.vcn.VcnUnderlyingNetworkTemplate priority : this.mConnectionConfig.getVcnUnderlyingNetworkPriorities()) {
            pw.println("Priority index: " + index);
            priority.dump(pw);
            index++;
        }
        pw.decreaseIndent();
        pw.println();
        pw.println("Underlying networks:");
        pw.increaseIndent();
        if (this.mRouteSelectionCallback != null) {
            for (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator recordEvaluator : getSortedUnderlyingNetworks()) {
                recordEvaluator.dump(pw);
            }
        }
        pw.decreaseIndent();
        pw.println();
        pw.decreaseIndent();
    }

    private class VcnActiveDataSubscriptionIdListener extends android.telephony.TelephonyCallback implements android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener {
        private VcnActiveDataSubscriptionIdListener() {
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int subId) {
            com.android.server.vcn.routeselection.UnderlyingNetworkController.this.reevaluateNetworks();
        }
    }

    public static class Dependencies {
        public com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator newUnderlyingNetworkEvaluator(com.android.server.vcn.VcnContext vcnContext, android.net.Network network, java.util.List<android.net.vcn.VcnUnderlyingNetworkTemplate> underlyingNetworkTemplates, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot lastSnapshot, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig, com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.NetworkEvaluatorCallback evaluatorCallback) {
            return new com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator(vcnContext, network, underlyingNetworkTemplates, subscriptionGroup, lastSnapshot, carrierConfig, evaluatorCallback);
        }
    }
}
