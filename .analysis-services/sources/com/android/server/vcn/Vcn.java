package com.android.server.vcn;

/* JADX INFO: loaded from: classes3.dex */
public class Vcn extends android.os.Handler {
    private static final int MSG_CMD_BASE = 100;
    private static final int MSG_CMD_TEARDOWN = 100;
    private static final int MSG_EVENT_BASE = 0;
    private static final int MSG_EVENT_CONFIG_UPDATED = 0;
    private static final int MSG_EVENT_GATEWAY_CONNECTION_QUIT = 3;
    private static final int MSG_EVENT_MOBILE_DATA_TOGGLED = 5;
    private static final int MSG_EVENT_NETWORK_REQUESTED = 1;
    private static final int MSG_EVENT_SAFE_MODE_STATE_CHANGED = 4;
    private static final int MSG_EVENT_SUBSCRIPTIONS_CHANGED = 2;
    private static final int VCN_LEGACY_SCORE_INT = 52;
    private android.net.vcn.VcnConfig mConfig;
    private final com.android.server.vcn.Vcn.VcnContentResolver mContentResolver;
    private volatile int mCurrentStatus;
    private final com.android.server.vcn.Vcn.Dependencies mDeps;
    private boolean mIsMobileDataEnabled;
    private com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot mLastSnapshot;
    private final android.database.ContentObserver mMobileDataSettingsObserver;
    private final java.util.Map<java.lang.Integer, com.android.server.vcn.Vcn.VcnUserMobileDataStateListener> mMobileDataStateListeners;
    private final com.android.server.vcn.Vcn.VcnNetworkRequestListener mRequestListener;
    private final android.os.ParcelUuid mSubscriptionGroup;
    private final com.android.server.VcnManagementService.VcnCallback mVcnCallback;
    private final com.android.server.vcn.VcnContext mVcnContext;
    private final java.util.Map<android.net.vcn.VcnGatewayConnectionConfig, com.android.server.vcn.VcnGatewayConnection> mVcnGatewayConnections;
    private static final java.lang.String TAG = com.android.server.vcn.Vcn.class.getSimpleName();
    private static final java.util.List<java.lang.Integer> CAPS_REQUIRING_MOBILE_DATA = java.util.Arrays.asList(12, 2);

    public interface VcnGatewayStatusCallback {
        void onGatewayConnectionError(java.lang.String str, int i, java.lang.String str2, java.lang.String str3);

        void onQuit();

        void onSafeModeStatusChanged();
    }

    public Vcn(com.android.server.vcn.VcnContext vcnContext, android.os.ParcelUuid subscriptionGroup, android.net.vcn.VcnConfig config, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, com.android.server.VcnManagementService.VcnCallback vcnCallback) {
        this(vcnContext, subscriptionGroup, config, snapshot, vcnCallback, new com.android.server.vcn.Vcn.Dependencies());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Vcn(com.android.server.vcn.VcnContext vcnContext, android.os.ParcelUuid parcelUuid, android.net.vcn.VcnConfig vcnConfig, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot, com.android.server.VcnManagementService.VcnCallback vcnCallback, com.android.server.vcn.Vcn.Dependencies dependencies) {
        super(((com.android.server.vcn.VcnContext) java.util.Objects.requireNonNull(vcnContext, "Missing vcnContext")).getLooper());
        this.mMobileDataStateListeners = new android.util.ArrayMap();
        this.mVcnGatewayConnections = new java.util.HashMap();
        this.mCurrentStatus = 2;
        this.mIsMobileDataEnabled = false;
        this.mVcnContext = vcnContext;
        this.mSubscriptionGroup = (android.os.ParcelUuid) java.util.Objects.requireNonNull(parcelUuid, "Missing subscriptionGroup");
        this.mVcnCallback = (com.android.server.VcnManagementService.VcnCallback) java.util.Objects.requireNonNull(vcnCallback, "Missing vcnCallback");
        this.mDeps = (com.android.server.vcn.Vcn.Dependencies) java.util.Objects.requireNonNull(dependencies, "Missing deps");
        this.mRequestListener = new com.android.server.vcn.Vcn.VcnNetworkRequestListener();
        this.mContentResolver = this.mDeps.newVcnContentResolver(this.mVcnContext);
        this.mMobileDataSettingsObserver = new com.android.server.vcn.Vcn.VcnMobileDataContentObserver(this);
        this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("mobile_data"), true, this.mMobileDataSettingsObserver);
        this.mConfig = (android.net.vcn.VcnConfig) java.util.Objects.requireNonNull(vcnConfig, "Missing config");
        this.mLastSnapshot = (com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot) java.util.Objects.requireNonNull(telephonySubscriptionSnapshot, "Missing snapshot");
        this.mIsMobileDataEnabled = getMobileDataStatus();
        updateMobileDataStateListeners();
        this.mVcnContext.getVcnNetworkProvider().registerListener(this.mRequestListener);
    }

    public void updateConfig(android.net.vcn.VcnConfig config) {
        java.util.Objects.requireNonNull(config, "Missing config");
        sendMessage(obtainMessage(0, config));
    }

    public void updateSubscriptionSnapshot(com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot) {
        java.util.Objects.requireNonNull(snapshot, "Missing snapshot");
        sendMessage(obtainMessage(2, snapshot));
    }

    public void teardownAsynchronously() {
        sendMessageAtFrontOfQueue(obtainMessage(100));
    }

    public int getStatus() {
        return this.mCurrentStatus;
    }

    public void setStatus(int status) {
        this.mCurrentStatus = status;
    }

    public java.util.Set<com.android.server.vcn.VcnGatewayConnection> getVcnGatewayConnections() {
        return java.util.Collections.unmodifiableSet(new java.util.HashSet(this.mVcnGatewayConnections.values()));
    }

    public java.util.Map<android.net.vcn.VcnGatewayConnectionConfig, com.android.server.vcn.VcnGatewayConnection> getVcnGatewayConnectionConfigMap() {
        return java.util.Collections.unmodifiableMap(new java.util.HashMap(this.mVcnGatewayConnections));
    }

    private class VcnNetworkRequestListener implements com.android.server.vcn.VcnNetworkProvider.NetworkRequestListener {
        private VcnNetworkRequestListener() {
        }

        @Override // com.android.server.vcn.VcnNetworkProvider.NetworkRequestListener
        public void onNetworkRequested(android.net.NetworkRequest request) {
            java.util.Objects.requireNonNull(request, "Missing request");
            com.android.server.vcn.Vcn.this.sendMessage(com.android.server.vcn.Vcn.this.obtainMessage(1, request));
        }
    }

    @Override // android.os.Handler
    public void handleMessage(android.os.Message msg) {
        if (this.mCurrentStatus != 2 && this.mCurrentStatus != 3) {
        }
        switch (msg.what) {
            case 0:
                handleConfigUpdated((android.net.vcn.VcnConfig) msg.obj);
                break;
            case 1:
                handleNetworkRequested((android.net.NetworkRequest) msg.obj);
                break;
            case 2:
                handleSubscriptionsChanged((com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot) msg.obj);
                break;
            case 3:
                handleGatewayConnectionQuit((android.net.vcn.VcnGatewayConnectionConfig) msg.obj);
                break;
            case 4:
                handleSafeModeStatusChanged();
                break;
            case 5:
                handleMobileDataToggled();
                break;
            case 100:
                handleTeardown();
                break;
            default:
                logWtf("Unknown msg.what: " + msg.what);
                break;
        }
    }

    private void handleConfigUpdated(android.net.vcn.VcnConfig config) {
        logDbg("Config updated: old = " + this.mConfig.hashCode() + "; new = " + config.hashCode());
        this.mConfig = config;
        for (java.util.Map.Entry<android.net.vcn.VcnGatewayConnectionConfig, com.android.server.vcn.VcnGatewayConnection> entry : this.mVcnGatewayConnections.entrySet()) {
            android.net.vcn.VcnGatewayConnectionConfig gatewayConnectionConfig = entry.getKey();
            com.android.server.vcn.VcnGatewayConnection gatewayConnection = entry.getValue();
            if (!this.mConfig.getGatewayConnectionConfigs().contains(gatewayConnectionConfig)) {
                if (gatewayConnection == null) {
                    logWtf("Found gatewayConnectionConfig without GatewayConnection");
                } else {
                    logInfo("Config updated, restarting gateway " + gatewayConnection.getLogPrefix());
                    gatewayConnection.teardownAsynchronously();
                }
            }
        }
        this.mVcnContext.getVcnNetworkProvider().resendAllRequests(this.mRequestListener);
    }

    private void handleTeardown() {
        logDbg("Tearing down");
        this.mVcnContext.getVcnNetworkProvider().unregisterListener(this.mRequestListener);
        for (com.android.server.vcn.VcnGatewayConnection gatewayConnection : this.mVcnGatewayConnections.values()) {
            gatewayConnection.teardownAsynchronously();
        }
        for (com.android.server.vcn.Vcn.VcnUserMobileDataStateListener listener : this.mMobileDataStateListeners.values()) {
            getTelephonyManager().unregisterTelephonyCallback(listener);
        }
        this.mMobileDataStateListeners.clear();
        this.mCurrentStatus = 1;
    }

    private void handleSafeModeStatusChanged() {
        logVdbg("VcnGatewayConnection safe mode status changed");
        boolean hasSafeModeGatewayConnection = false;
        java.util.Iterator<com.android.server.vcn.VcnGatewayConnection> it = this.mVcnGatewayConnections.values().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            com.android.server.vcn.VcnGatewayConnection gatewayConnection = it.next();
            if (gatewayConnection.isInSafeMode()) {
                hasSafeModeGatewayConnection = true;
                break;
            }
        }
        int oldStatus = this.mCurrentStatus;
        this.mCurrentStatus = hasSafeModeGatewayConnection ? 3 : 2;
        if (oldStatus != this.mCurrentStatus) {
            this.mVcnCallback.onSafeModeStatusChanged(hasSafeModeGatewayConnection);
            logInfo("Safe mode " + (this.mCurrentStatus == 3 ? "entered" : "exited"));
        }
    }

    private void handleNetworkRequested(android.net.NetworkRequest request) {
        logVdbg("Received request " + request);
        java.util.Iterator<android.net.vcn.VcnGatewayConnectionConfig> it = this.mVcnGatewayConnections.keySet().iterator();
        while (it.hasNext()) {
            if (isRequestSatisfiedByGatewayConnectionConfig(request, it.next())) {
                logVdbg("Request already satisfied by existing VcnGatewayConnection: " + request);
                return;
            }
        }
        for (android.net.vcn.VcnGatewayConnectionConfig gatewayConnectionConfig : this.mConfig.getGatewayConnectionConfigs()) {
            if (isRequestSatisfiedByGatewayConnectionConfig(request, gatewayConnectionConfig) && !getExposedCapabilitiesForMobileDataState(gatewayConnectionConfig).isEmpty()) {
                if (this.mVcnGatewayConnections.containsKey(gatewayConnectionConfig)) {
                    logWtf("Attempted to bring up VcnGatewayConnection for config with existing VcnGatewayConnection");
                    return;
                }
                logInfo("Bringing up new VcnGatewayConnection for request " + request);
                com.android.server.vcn.VcnGatewayConnection vcnGatewayConnection = this.mDeps.newVcnGatewayConnection(this.mVcnContext, this.mSubscriptionGroup, this.mLastSnapshot, gatewayConnectionConfig, new com.android.server.vcn.Vcn.VcnGatewayStatusCallbackImpl(gatewayConnectionConfig), this.mIsMobileDataEnabled);
                this.mVcnGatewayConnections.put(gatewayConnectionConfig, vcnGatewayConnection);
                return;
            }
        }
        logVdbg("Request could not be fulfilled by VCN: " + request);
    }

    private java.util.Set<java.lang.Integer> getExposedCapabilitiesForMobileDataState(android.net.vcn.VcnGatewayConnectionConfig gatewayConnectionConfig) {
        if (this.mIsMobileDataEnabled) {
            return gatewayConnectionConfig.getAllExposedCapabilities();
        }
        java.util.Set<java.lang.Integer> exposedCapsWithoutMobileData = new android.util.ArraySet<>(gatewayConnectionConfig.getAllExposedCapabilities());
        exposedCapsWithoutMobileData.removeAll(CAPS_REQUIRING_MOBILE_DATA);
        return exposedCapsWithoutMobileData;
    }

    private void handleGatewayConnectionQuit(android.net.vcn.VcnGatewayConnectionConfig config) {
        logInfo("VcnGatewayConnection quit: " + config);
        this.mVcnGatewayConnections.remove(config);
        this.mVcnContext.getVcnNetworkProvider().resendAllRequests(this.mRequestListener);
    }

    private void handleSubscriptionsChanged(com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot) {
        this.mLastSnapshot = snapshot;
        for (com.android.server.vcn.VcnGatewayConnection gatewayConnection : this.mVcnGatewayConnections.values()) {
            gatewayConnection.updateSubscriptionSnapshot(this.mLastSnapshot);
        }
        updateMobileDataStateListeners();
        handleMobileDataToggled();
    }

    private void updateMobileDataStateListeners() {
        java.util.Set<java.lang.Integer> subIdsInGroup = this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup);
        android.os.HandlerExecutor executor = new android.os.HandlerExecutor(this);
        java.util.Iterator<java.lang.Integer> it = subIdsInGroup.iterator();
        while (it.hasNext()) {
            int subId = it.next().intValue();
            if (!this.mMobileDataStateListeners.containsKey(java.lang.Integer.valueOf(subId))) {
                com.android.server.vcn.Vcn.VcnUserMobileDataStateListener listener = new com.android.server.vcn.Vcn.VcnUserMobileDataStateListener();
                getTelephonyManagerForSubid(subId).registerTelephonyCallback(executor, listener);
                this.mMobileDataStateListeners.put(java.lang.Integer.valueOf(subId), listener);
            }
        }
        java.util.Iterator<java.util.Map.Entry<java.lang.Integer, com.android.server.vcn.Vcn.VcnUserMobileDataStateListener>> iterator = this.mMobileDataStateListeners.entrySet().iterator();
        while (iterator.hasNext()) {
            java.util.Map.Entry<java.lang.Integer, com.android.server.vcn.Vcn.VcnUserMobileDataStateListener> entry = iterator.next();
            if (!subIdsInGroup.contains(entry.getKey())) {
                getTelephonyManager().unregisterTelephonyCallback(entry.getValue());
                iterator.remove();
            }
        }
    }

    private void handleMobileDataToggled() {
        boolean oldMobileDataEnabledStatus = this.mIsMobileDataEnabled;
        this.mIsMobileDataEnabled = getMobileDataStatus();
        if (oldMobileDataEnabledStatus != this.mIsMobileDataEnabled) {
            for (java.util.Map.Entry<android.net.vcn.VcnGatewayConnectionConfig, com.android.server.vcn.VcnGatewayConnection> entry : this.mVcnGatewayConnections.entrySet()) {
                android.net.vcn.VcnGatewayConnectionConfig gatewayConnectionConfig = entry.getKey();
                com.android.server.vcn.VcnGatewayConnection gatewayConnection = entry.getValue();
                java.util.Set<java.lang.Integer> exposedCaps = gatewayConnectionConfig.getAllExposedCapabilities();
                if (exposedCaps.contains(12) || exposedCaps.contains(2)) {
                    if (gatewayConnection == null) {
                        logWtf("Found gatewayConnectionConfig without GatewayConnection");
                    } else {
                        gatewayConnection.teardownAsynchronously();
                    }
                }
            }
            this.mVcnContext.getVcnNetworkProvider().resendAllRequests(this.mRequestListener);
            logInfo("Mobile data " + (this.mIsMobileDataEnabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED));
        }
    }

    private boolean getMobileDataStatus() {
        java.util.Iterator<java.lang.Integer> it = this.mLastSnapshot.getAllSubIdsInGroup(this.mSubscriptionGroup).iterator();
        while (it.hasNext()) {
            int subId = it.next().intValue();
            if (getTelephonyManagerForSubid(subId).isDataEnabled()) {
                return true;
            }
        }
        return false;
    }

    private boolean isRequestSatisfiedByGatewayConnectionConfig(android.net.NetworkRequest request, android.net.vcn.VcnGatewayConnectionConfig config) {
        android.net.NetworkCapabilities.Builder builder = new android.net.NetworkCapabilities.Builder();
        builder.addTransportType(0);
        builder.addCapability(28);
        java.util.Iterator<java.lang.Integer> it = getExposedCapabilitiesForMobileDataState(config).iterator();
        while (it.hasNext()) {
            int cap = it.next().intValue();
            builder.addCapability(cap);
        }
        return request.canBeSatisfiedBy(builder.build());
    }

    private android.telephony.TelephonyManager getTelephonyManager() {
        return (android.telephony.TelephonyManager) this.mVcnContext.getContext().getSystemService(android.telephony.TelephonyManager.class);
    }

    private android.telephony.TelephonyManager getTelephonyManagerForSubid(int subid) {
        return getTelephonyManager().createForSubscriptionId(subid);
    }

    private java.lang.String getLogPrefix() {
        return "(" + com.android.server.vcn.util.LogUtils.getHashedSubscriptionGroup(this.mSubscriptionGroup) + "-" + java.lang.System.identityHashCode(this) + ") ";
    }

    private void logVdbg(java.lang.String msg) {
    }

    private void logDbg(java.lang.String msg) {
        android.util.Slog.d(TAG, getLogPrefix() + msg);
    }

    private void logDbg(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.d(TAG, getLogPrefix() + msg, tr);
    }

    private void logInfo(java.lang.String msg) {
        android.util.Slog.i(TAG, getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "INFO: " + msg);
    }

    private void logInfo(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.i(TAG, getLogPrefix() + msg, tr);
        com.android.server.VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "INFO: " + msg + tr);
    }

    private void logErr(java.lang.String msg) {
        android.util.Slog.e(TAG, getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "ERR: " + msg);
    }

    private void logErr(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.e(TAG, getLogPrefix() + msg, tr);
        com.android.server.VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "ERR: " + msg + tr);
    }

    private void logWtf(java.lang.String msg) {
        android.util.Slog.wtf(TAG, getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "WTF: " + msg);
    }

    private void logWtf(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.wtf(TAG, getLogPrefix() + msg, tr);
        com.android.server.VcnManagementService.LOCAL_LOG.log(getLogPrefix() + "WTF: " + msg + tr);
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        this.mVcnContext.ensureRunningOnLooperThread();
        pw.println("Vcn (" + this.mSubscriptionGroup + "):");
        pw.increaseIndent();
        pw.println("mCurrentStatus: " + this.mCurrentStatus);
        pw.println("mIsMobileDataEnabled: " + this.mIsMobileDataEnabled);
        pw.println();
        pw.println("mVcnGatewayConnections:");
        pw.increaseIndent();
        for (com.android.server.vcn.VcnGatewayConnection gw : this.mVcnGatewayConnections.values()) {
            gw.dump(pw);
        }
        pw.decreaseIndent();
        pw.println();
        pw.decreaseIndent();
    }

    public boolean isMobileDataEnabled() {
        return this.mIsMobileDataEnabled;
    }

    public void setMobileDataEnabled(boolean isMobileDataEnabled) {
        this.mIsMobileDataEnabled = isMobileDataEnabled;
    }

    static android.net.NetworkScore getNetworkScore() {
        return new android.net.NetworkScore.Builder().setLegacyInt(52).setTransportPrimary(true).build();
    }

    private class VcnGatewayStatusCallbackImpl implements com.android.server.vcn.Vcn.VcnGatewayStatusCallback {
        public final android.net.vcn.VcnGatewayConnectionConfig mGatewayConnectionConfig;

        VcnGatewayStatusCallbackImpl(android.net.vcn.VcnGatewayConnectionConfig gatewayConnectionConfig) {
            this.mGatewayConnectionConfig = gatewayConnectionConfig;
        }

        @Override // com.android.server.vcn.Vcn.VcnGatewayStatusCallback
        public void onQuit() {
            com.android.server.vcn.Vcn.this.sendMessage(com.android.server.vcn.Vcn.this.obtainMessage(3, this.mGatewayConnectionConfig));
        }

        @Override // com.android.server.vcn.Vcn.VcnGatewayStatusCallback
        public void onSafeModeStatusChanged() {
            com.android.server.vcn.Vcn.this.sendMessage(com.android.server.vcn.Vcn.this.obtainMessage(4));
        }

        @Override // com.android.server.vcn.Vcn.VcnGatewayStatusCallback
        public void onGatewayConnectionError(java.lang.String gatewayConnectionName, int errorCode, java.lang.String exceptionClass, java.lang.String exceptionMessage) {
            com.android.server.vcn.Vcn.this.mVcnCallback.onGatewayConnectionError(gatewayConnectionName, errorCode, exceptionClass, exceptionMessage);
        }
    }

    private class VcnMobileDataContentObserver extends android.database.ContentObserver {
        private VcnMobileDataContentObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            com.android.server.vcn.Vcn.this.sendMessage(com.android.server.vcn.Vcn.this.obtainMessage(5));
        }
    }

    class VcnUserMobileDataStateListener extends android.telephony.TelephonyCallback implements android.telephony.TelephonyCallback.UserMobileDataStateListener {
        VcnUserMobileDataStateListener() {
        }

        @Override // android.telephony.TelephonyCallback.UserMobileDataStateListener
        public void onUserMobileDataStateChanged(boolean enabled) {
            com.android.server.vcn.Vcn.this.sendMessage(com.android.server.vcn.Vcn.this.obtainMessage(5));
        }
    }

    public static class Dependencies {
        public com.android.server.vcn.VcnGatewayConnection newVcnGatewayConnection(com.android.server.vcn.VcnContext vcnContext, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, android.net.vcn.VcnGatewayConnectionConfig connectionConfig, com.android.server.vcn.Vcn.VcnGatewayStatusCallback gatewayStatusCallback, boolean isMobileDataEnabled) {
            return new com.android.server.vcn.VcnGatewayConnection(vcnContext, subscriptionGroup, snapshot, connectionConfig, gatewayStatusCallback, isMobileDataEnabled);
        }

        public com.android.server.vcn.Vcn.VcnContentResolver newVcnContentResolver(com.android.server.vcn.VcnContext vcnContext) {
            return new com.android.server.vcn.Vcn.VcnContentResolver(vcnContext);
        }
    }

    public static class VcnContentResolver {
        private final android.content.ContentResolver mImpl;

        public VcnContentResolver(com.android.server.vcn.VcnContext vcnContext) {
            this.mImpl = vcnContext.getContext().getContentResolver();
        }

        public void registerContentObserver(android.net.Uri uri, boolean notifyForDescendants, android.database.ContentObserver observer) {
            this.mImpl.registerContentObserver(uri, notifyForDescendants, observer);
        }
    }
}
