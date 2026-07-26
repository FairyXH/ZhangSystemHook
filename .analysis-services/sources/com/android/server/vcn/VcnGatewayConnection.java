package com.android.server.vcn;

/* JADX INFO: loaded from: classes3.dex */
public class VcnGatewayConnection extends com.android.internal.util.StateMachine {
    private static final int ARG_NOT_PRESENT = Integer.MIN_VALUE;
    private static final java.lang.String DISCONNECT_REASON_INTERNAL_ERROR = "Uncaught exception: ";
    private static final java.lang.String DISCONNECT_REASON_NETWORK_AGENT_UNWANTED = "NetworkAgent was unwanted";
    private static final java.lang.String DISCONNECT_REASON_TEARDOWN = "teardown() called on VcnTunnel";
    private static final java.lang.String DISCONNECT_REASON_UNDERLYING_NETWORK_LOST = "Underlying Network lost";
    private static final int EVENT_DATA_STALL_SUSPECTED = 13;
    private static final int EVENT_DISCONNECT_REQUESTED = 7;
    private static final int EVENT_IKE_CONNECTION_INFO_CHANGED = 12;
    private static final int EVENT_MIGRATION_COMPLETED = 11;
    private static final int EVENT_RETRY_TIMEOUT_EXPIRED = 2;
    private static final int EVENT_SAFE_MODE_TIMEOUT_EXCEEDED = 10;
    private static final int EVENT_SESSION_CLOSED = 4;
    private static final int EVENT_SESSION_LOST = 3;
    private static final int EVENT_SETUP_COMPLETED = 6;
    private static final int EVENT_SUBSCRIPTIONS_CHANGED = 9;
    private static final int EVENT_TEARDOWN_TIMEOUT_EXPIRED = 8;
    private static final int EVENT_TRANSFORM_CREATED = 5;
    private static final int EVENT_UNDERLYING_NETWORK_CHANGED = 1;
    static final java.lang.String NETWORK_INFO_EXTRA_INFO = "VCN";
    static final java.lang.String NETWORK_INFO_NETWORK_TYPE_STRING = "MOBILE";
    static final int NETWORK_LOSS_DISCONNECT_TIMEOUT_SECONDS = 30;
    static final int SAFEMODE_TIMEOUT_SECONDS = 30;
    private static final int SAFEMODE_TIMEOUT_SECONDS_TEST_MODE = 10;
    static final int TEARDOWN_TIMEOUT_SECONDS = 5;
    private static final int TOKEN_ALL = Integer.MIN_VALUE;
    static final int TUNNEL_AGGREGATION_SA_COUNT_MAX_DEFAULT = 1;
    private com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration mChildConfig;
    final com.android.server.vcn.VcnGatewayConnection.ConnectedState mConnectedState;
    final com.android.server.vcn.VcnGatewayConnection.ConnectingState mConnectingState;
    private final android.net.vcn.VcnGatewayConnectionConfig mConnectionConfig;
    private final com.android.server.vcn.VcnGatewayConnection.VcnConnectivityDiagnosticsCallback mConnectivityDiagnosticsCallback;
    private final android.net.ConnectivityDiagnosticsManager mConnectivityDiagnosticsManager;
    private final android.net.ConnectivityManager mConnectivityManager;
    private int mCurrentToken;
    private final com.android.server.vcn.VcnGatewayConnection.Dependencies mDeps;
    private com.android.internal.util.WakeupMessage mDisconnectRequestAlarm;
    final com.android.server.vcn.VcnGatewayConnection.DisconnectedState mDisconnectedState;
    final com.android.server.vcn.VcnGatewayConnection.DisconnectingState mDisconnectingState;
    private int mFailedAttempts;
    private final com.android.server.vcn.Vcn.VcnGatewayStatusCallback mGatewayStatusCallback;
    private android.net.ipsec.ike.IkeSessionConnectionInfo mIkeConnectionInfo;
    private com.android.server.vcn.VcnGatewayConnection.VcnIkeSession mIkeSession;
    private final android.net.IpSecManager mIpSecManager;
    private boolean mIsInSafeMode;
    private final boolean mIsMobileDataEnabled;
    private com.android.server.vcn.util.OneWayBoolean mIsQuitting;
    private com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot mLastSnapshot;
    private com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent mNetworkAgent;
    private com.android.internal.util.WakeupMessage mRetryTimeoutAlarm;
    final com.android.server.vcn.VcnGatewayConnection.RetryTimeoutState mRetryTimeoutState;
    private com.android.internal.util.WakeupMessage mSafeModeTimeoutAlarm;
    private final android.os.ParcelUuid mSubscriptionGroup;
    private com.android.internal.util.WakeupMessage mTeardownTimeoutAlarm;
    private android.net.IpSecManager.IpSecTunnelInterface mTunnelIface;
    private com.android.server.vcn.routeselection.UnderlyingNetworkRecord mUnderlying;
    private final com.android.server.vcn.routeselection.UnderlyingNetworkController mUnderlyingNetworkController;
    private final com.android.server.vcn.VcnGatewayConnection.VcnUnderlyingNetworkControllerCallback mUnderlyingNetworkControllerCallback;
    private final com.android.server.vcn.VcnContext mVcnContext;
    private final com.android.server.vcn.VcnGatewayConnection.VcnWakeLock mWakeLock;
    private static final java.lang.String TAG = com.android.server.vcn.VcnGatewayConnection.class.getSimpleName();
    static final java.net.InetAddress DUMMY_ADDR = android.net.InetAddresses.parseNumericAddress("192.0.2.0");
    static final java.lang.String TEARDOWN_TIMEOUT_ALARM = TAG + "_TEARDOWN_TIMEOUT_ALARM";
    static final java.lang.String DISCONNECT_REQUEST_ALARM = TAG + "_DISCONNECT_REQUEST_ALARM";
    static final java.lang.String RETRY_TIMEOUT_ALARM = TAG + "_RETRY_TIMEOUT_ALARM";
    static final java.lang.String SAFEMODE_TIMEOUT_ALARM = TAG + "_SAFEMODE_TIMEOUT_ALARM";
    private static final int[] MERGED_CAPABILITIES = {11, 18};

    private interface EventInfo {
    }

    private static class EventUnderlyingNetworkChangedInfo implements com.android.server.vcn.VcnGatewayConnection.EventInfo {
        public final com.android.server.vcn.routeselection.UnderlyingNetworkRecord newUnderlying;

        EventUnderlyingNetworkChangedInfo(com.android.server.vcn.routeselection.UnderlyingNetworkRecord newUnderlying) {
            this.newUnderlying = newUnderlying;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.newUnderlying);
        }

        public boolean equals(java.lang.Object other) {
            if (!(other instanceof com.android.server.vcn.VcnGatewayConnection.EventUnderlyingNetworkChangedInfo)) {
                return false;
            }
            com.android.server.vcn.VcnGatewayConnection.EventUnderlyingNetworkChangedInfo rhs = (com.android.server.vcn.VcnGatewayConnection.EventUnderlyingNetworkChangedInfo) other;
            return java.util.Objects.equals(this.newUnderlying, rhs.newUnderlying);
        }
    }

    private static class EventSessionLostInfo implements com.android.server.vcn.VcnGatewayConnection.EventInfo {
        public final java.lang.Exception exception;

        EventSessionLostInfo(java.lang.Exception exception) {
            this.exception = exception;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.exception);
        }

        public boolean equals(java.lang.Object other) {
            if (!(other instanceof com.android.server.vcn.VcnGatewayConnection.EventSessionLostInfo)) {
                return false;
            }
            com.android.server.vcn.VcnGatewayConnection.EventSessionLostInfo rhs = (com.android.server.vcn.VcnGatewayConnection.EventSessionLostInfo) other;
            return java.util.Objects.equals(this.exception, rhs.exception);
        }
    }

    private static class EventTransformCreatedInfo implements com.android.server.vcn.VcnGatewayConnection.EventInfo {
        public final int direction;
        public final android.net.IpSecTransform transform;

        EventTransformCreatedInfo(int direction, android.net.IpSecTransform transform) {
            this.direction = direction;
            this.transform = (android.net.IpSecTransform) java.util.Objects.requireNonNull(transform);
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.direction), this.transform);
        }

        public boolean equals(java.lang.Object other) {
            if (!(other instanceof com.android.server.vcn.VcnGatewayConnection.EventTransformCreatedInfo)) {
                return false;
            }
            com.android.server.vcn.VcnGatewayConnection.EventTransformCreatedInfo rhs = (com.android.server.vcn.VcnGatewayConnection.EventTransformCreatedInfo) other;
            return this.direction == rhs.direction && java.util.Objects.equals(this.transform, rhs.transform);
        }
    }

    private static class EventSetupCompletedInfo implements com.android.server.vcn.VcnGatewayConnection.EventInfo {
        public final com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration childSessionConfig;

        EventSetupCompletedInfo(com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration childSessionConfig) {
            this.childSessionConfig = (com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration) java.util.Objects.requireNonNull(childSessionConfig);
        }

        public int hashCode() {
            return java.util.Objects.hash(this.childSessionConfig);
        }

        public boolean equals(java.lang.Object other) {
            if (!(other instanceof com.android.server.vcn.VcnGatewayConnection.EventSetupCompletedInfo)) {
                return false;
            }
            com.android.server.vcn.VcnGatewayConnection.EventSetupCompletedInfo rhs = (com.android.server.vcn.VcnGatewayConnection.EventSetupCompletedInfo) other;
            return java.util.Objects.equals(this.childSessionConfig, rhs.childSessionConfig);
        }
    }

    private static class EventDisconnectRequestedInfo implements com.android.server.vcn.VcnGatewayConnection.EventInfo {
        public final java.lang.String reason;
        public final boolean shouldQuit;

        EventDisconnectRequestedInfo(java.lang.String reason, boolean shouldQuit) {
            this.reason = (java.lang.String) java.util.Objects.requireNonNull(reason);
            this.shouldQuit = shouldQuit;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.reason, java.lang.Boolean.valueOf(this.shouldQuit));
        }

        public boolean equals(java.lang.Object other) {
            if (!(other instanceof com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo)) {
                return false;
            }
            com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo rhs = (com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo) other;
            return this.reason.equals(rhs.reason) && this.shouldQuit == rhs.shouldQuit;
        }
    }

    private static class EventMigrationCompletedInfo implements com.android.server.vcn.VcnGatewayConnection.EventInfo {
        public final android.net.IpSecTransform inTransform;
        public final android.net.IpSecTransform outTransform;

        EventMigrationCompletedInfo(android.net.IpSecTransform inTransform, android.net.IpSecTransform outTransform) {
            this.inTransform = (android.net.IpSecTransform) java.util.Objects.requireNonNull(inTransform);
            this.outTransform = (android.net.IpSecTransform) java.util.Objects.requireNonNull(outTransform);
        }

        public int hashCode() {
            return java.util.Objects.hash(this.inTransform, this.outTransform);
        }

        public boolean equals(java.lang.Object other) {
            if (!(other instanceof com.android.server.vcn.VcnGatewayConnection.EventMigrationCompletedInfo)) {
                return false;
            }
            com.android.server.vcn.VcnGatewayConnection.EventMigrationCompletedInfo rhs = (com.android.server.vcn.VcnGatewayConnection.EventMigrationCompletedInfo) other;
            return java.util.Objects.equals(this.inTransform, rhs.inTransform) && java.util.Objects.equals(this.outTransform, rhs.outTransform);
        }
    }

    private static class EventIkeConnectionInfoChangedInfo implements com.android.server.vcn.VcnGatewayConnection.EventInfo {
        public final android.net.ipsec.ike.IkeSessionConnectionInfo ikeConnectionInfo;

        EventIkeConnectionInfoChangedInfo(android.net.ipsec.ike.IkeSessionConnectionInfo ikeConnectionInfo) {
            this.ikeConnectionInfo = ikeConnectionInfo;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.ikeConnectionInfo);
        }

        public boolean equals(java.lang.Object other) {
            if (!(other instanceof com.android.server.vcn.VcnGatewayConnection.EventIkeConnectionInfoChangedInfo)) {
                return false;
            }
            com.android.server.vcn.VcnGatewayConnection.EventIkeConnectionInfoChangedInfo rhs = (com.android.server.vcn.VcnGatewayConnection.EventIkeConnectionInfoChangedInfo) other;
            return java.util.Objects.equals(this.ikeConnectionInfo, rhs.ikeConnectionInfo);
        }
    }

    private static class EventDataStallSuspectedInfo implements com.android.server.vcn.VcnGatewayConnection.EventInfo {
        public final android.net.Network network;

        EventDataStallSuspectedInfo(android.net.Network network) {
            this.network = network;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.network);
        }

        public boolean equals(java.lang.Object other) {
            if (!(other instanceof com.android.server.vcn.VcnGatewayConnection.EventDataStallSuspectedInfo)) {
                return false;
            }
            com.android.server.vcn.VcnGatewayConnection.EventDataStallSuspectedInfo rhs = (com.android.server.vcn.VcnGatewayConnection.EventDataStallSuspectedInfo) other;
            return java.util.Objects.equals(this.network, rhs.network);
        }
    }

    public VcnGatewayConnection(com.android.server.vcn.VcnContext vcnContext, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, android.net.vcn.VcnGatewayConnectionConfig connectionConfig, com.android.server.vcn.Vcn.VcnGatewayStatusCallback gatewayStatusCallback, boolean isMobileDataEnabled) {
        this(vcnContext, subscriptionGroup, snapshot, connectionConfig, gatewayStatusCallback, isMobileDataEnabled, new com.android.server.vcn.VcnGatewayConnection.Dependencies());
    }

    /* JADX WARN: Multi-variable type inference failed */
    VcnGatewayConnection(com.android.server.vcn.VcnContext vcnContext, android.os.ParcelUuid parcelUuid, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot telephonySubscriptionSnapshot, android.net.vcn.VcnGatewayConnectionConfig vcnGatewayConnectionConfig, com.android.server.vcn.Vcn.VcnGatewayStatusCallback vcnGatewayStatusCallback, boolean z, com.android.server.vcn.VcnGatewayConnection.Dependencies dependencies) {
        super(TAG, ((com.android.server.vcn.VcnContext) java.util.Objects.requireNonNull(vcnContext, "Missing vcnContext")).getLooper());
        this.mDisconnectedState = new com.android.server.vcn.VcnGatewayConnection.DisconnectedState();
        this.mDisconnectingState = new com.android.server.vcn.VcnGatewayConnection.DisconnectingState();
        this.mConnectingState = new com.android.server.vcn.VcnGatewayConnection.ConnectingState();
        this.mConnectedState = new com.android.server.vcn.VcnGatewayConnection.ConnectedState();
        this.mRetryTimeoutState = new com.android.server.vcn.VcnGatewayConnection.RetryTimeoutState();
        this.mTunnelIface = null;
        this.mIsQuitting = new com.android.server.vcn.util.OneWayBoolean();
        this.mIsInSafeMode = false;
        this.mCurrentToken = -1;
        this.mFailedAttempts = 0;
        this.mVcnContext = vcnContext;
        this.mSubscriptionGroup = (android.os.ParcelUuid) java.util.Objects.requireNonNull(parcelUuid, "Missing subscriptionGroup");
        this.mConnectionConfig = (android.net.vcn.VcnGatewayConnectionConfig) java.util.Objects.requireNonNull(vcnGatewayConnectionConfig, "Missing connectionConfig");
        this.mGatewayStatusCallback = (com.android.server.vcn.Vcn.VcnGatewayStatusCallback) java.util.Objects.requireNonNull(vcnGatewayStatusCallback, "Missing gatewayStatusCallback");
        this.mIsMobileDataEnabled = z;
        this.mDeps = (com.android.server.vcn.VcnGatewayConnection.Dependencies) java.util.Objects.requireNonNull(dependencies, "Missing deps");
        this.mLastSnapshot = (com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot) java.util.Objects.requireNonNull(telephonySubscriptionSnapshot, "Missing snapshot");
        this.mUnderlyingNetworkControllerCallback = new com.android.server.vcn.VcnGatewayConnection.VcnUnderlyingNetworkControllerCallback();
        this.mWakeLock = this.mDeps.newWakeLock(this.mVcnContext.getContext(), 1, TAG);
        this.mUnderlyingNetworkController = this.mDeps.newUnderlyingNetworkController(this.mVcnContext, this.mConnectionConfig, parcelUuid, this.mLastSnapshot, this.mUnderlyingNetworkControllerCallback);
        this.mIpSecManager = (android.net.IpSecManager) this.mVcnContext.getContext().getSystemService(android.net.IpSecManager.class);
        this.mConnectivityManager = (android.net.ConnectivityManager) this.mVcnContext.getContext().getSystemService(android.net.ConnectivityManager.class);
        this.mConnectivityDiagnosticsManager = (android.net.ConnectivityDiagnosticsManager) this.mVcnContext.getContext().getSystemService(android.net.ConnectivityDiagnosticsManager.class);
        this.mConnectivityDiagnosticsCallback = new com.android.server.vcn.VcnGatewayConnection.VcnConnectivityDiagnosticsCallback();
        if (this.mConnectionConfig.hasGatewayOption(0)) {
            this.mConnectivityDiagnosticsManager.registerConnectivityDiagnosticsCallback(new android.net.NetworkRequest.Builder().addTransportType(0).build(), new android.os.HandlerExecutor(new android.os.Handler(vcnContext.getLooper())), this.mConnectivityDiagnosticsCallback);
        }
        addState(this.mDisconnectedState);
        addState(this.mDisconnectingState);
        addState(this.mConnectingState);
        addState(this.mConnectedState);
        addState(this.mRetryTimeoutState);
        setInitialState(this.mDisconnectedState);
        setDbg(false);
        start();
    }

    public boolean isInSafeMode() {
        this.mVcnContext.ensureRunningOnLooperThread();
        return this.mIsInSafeMode;
    }

    public void teardownAsynchronously() {
        logDbg("Triggering async teardown");
        sendDisconnectRequestedAndAcquireWakelock(DISCONNECT_REASON_TEARDOWN, true);
    }

    protected void onQuitting() {
        logInfo("Quitting VcnGatewayConnection");
        if (this.mNetworkAgent != null) {
            logWtf("NetworkAgent was non-null in onQuitting");
            this.mNetworkAgent.unregister();
            this.mNetworkAgent = null;
        }
        if (this.mIkeSession != null) {
            logWtf("IkeSession was non-null in onQuitting");
            this.mIkeSession.kill();
            this.mIkeSession = null;
        }
        if (this.mTunnelIface != null) {
            this.mTunnelIface.close();
        }
        releaseWakeLock();
        cancelTeardownTimeoutAlarm();
        cancelDisconnectRequestAlarm();
        cancelRetryTimeoutAlarm();
        cancelSafeModeAlarm();
        this.mUnderlyingNetworkController.teardown();
        this.mGatewayStatusCallback.onQuit();
        this.mConnectivityDiagnosticsManager.unregisterConnectivityDiagnosticsCallback(this.mConnectivityDiagnosticsCallback);
    }

    public void updateSubscriptionSnapshot(com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot) {
        java.util.Objects.requireNonNull(snapshot, "Missing snapshot");
        this.mVcnContext.ensureRunningOnLooperThread();
        this.mLastSnapshot = snapshot;
        this.mUnderlyingNetworkController.updateSubscriptionSnapshot(this.mLastSnapshot);
        sendMessageAndAcquireWakeLock(9, Integer.MIN_VALUE);
    }

    private class VcnConnectivityDiagnosticsCallback extends android.net.ConnectivityDiagnosticsManager.ConnectivityDiagnosticsCallback {
        private VcnConnectivityDiagnosticsCallback() {
        }

        @Override // android.net.ConnectivityDiagnosticsManager.ConnectivityDiagnosticsCallback
        public void onDataStallSuspected(android.net.ConnectivityDiagnosticsManager.DataStallReport report) {
            com.android.server.vcn.VcnGatewayConnection.this.mVcnContext.ensureRunningOnLooperThread();
            android.net.Network network = report.getNetwork();
            com.android.server.vcn.VcnGatewayConnection.this.logInfo("Data stall suspected on " + network);
            com.android.server.vcn.VcnGatewayConnection.this.sendMessageAndAcquireWakeLock(13, Integer.MIN_VALUE, new com.android.server.vcn.VcnGatewayConnection.EventDataStallSuspectedInfo(network));
        }
    }

    private class VcnUnderlyingNetworkControllerCallback implements com.android.server.vcn.routeselection.UnderlyingNetworkController.UnderlyingNetworkControllerCallback {
        private VcnUnderlyingNetworkControllerCallback() {
        }

        @Override // com.android.server.vcn.routeselection.UnderlyingNetworkController.UnderlyingNetworkControllerCallback
        public void onSelectedUnderlyingNetworkChanged(com.android.server.vcn.routeselection.UnderlyingNetworkRecord underlying) {
            com.android.server.vcn.VcnGatewayConnection.this.mVcnContext.ensureRunningOnLooperThread();
            if (!com.android.server.vcn.routeselection.UnderlyingNetworkRecord.isSameNetwork(com.android.server.vcn.VcnGatewayConnection.this.mUnderlying, underlying)) {
                com.android.server.vcn.VcnGatewayConnection.this.logInfo("Selected underlying network changed: " + (underlying == null ? null : underlying.network));
            }
            if (underlying == null) {
                if (com.android.server.vcn.VcnGatewayConnection.this.mDeps.isAirplaneModeOn(com.android.server.vcn.VcnGatewayConnection.this.mVcnContext)) {
                    com.android.server.vcn.VcnGatewayConnection.this.sendMessageAndAcquireWakeLock(1, Integer.MIN_VALUE, new com.android.server.vcn.VcnGatewayConnection.EventUnderlyingNetworkChangedInfo(null));
                    com.android.server.vcn.VcnGatewayConnection.this.sendDisconnectRequestedAndAcquireWakelock(com.android.server.vcn.VcnGatewayConnection.DISCONNECT_REASON_UNDERLYING_NETWORK_LOST, false);
                    return;
                }
                com.android.server.vcn.VcnGatewayConnection.this.setDisconnectRequestAlarm();
            } else {
                com.android.server.vcn.VcnGatewayConnection.this.cancelDisconnectRequestAlarm();
            }
            com.android.server.vcn.VcnGatewayConnection.this.sendMessageAndAcquireWakeLock(1, Integer.MIN_VALUE, new com.android.server.vcn.VcnGatewayConnection.EventUnderlyingNetworkChangedInfo(underlying));
        }
    }

    private void acquireWakeLock() {
        this.mVcnContext.ensureRunningOnLooperThread();
        if (!this.mIsQuitting.getValue()) {
            this.mWakeLock.acquire();
            logVdbg("Wakelock acquired: " + this.mWakeLock);
        }
    }

    private void releaseWakeLock() {
        this.mVcnContext.ensureRunningOnLooperThread();
        this.mWakeLock.release();
        logVdbg("Wakelock released: " + this.mWakeLock);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeReleaseWakeLock() {
        android.os.Handler handler = getHandler();
        if (handler == null || !handler.hasMessagesOrCallbacks()) {
            releaseWakeLock();
        }
    }

    public void sendMessage(int what) {
        logWtf("sendMessage should not be used in VcnGatewayConnection. See sendMessageAndAcquireWakeLock()");
        super.sendMessage(what);
    }

    public void sendMessage(int what, java.lang.Object obj) {
        logWtf("sendMessage should not be used in VcnGatewayConnection. See sendMessageAndAcquireWakeLock()");
        super.sendMessage(what, obj);
    }

    public void sendMessage(int what, int arg1) {
        logWtf("sendMessage should not be used in VcnGatewayConnection. See sendMessageAndAcquireWakeLock()");
        super.sendMessage(what, arg1);
    }

    public void sendMessage(int what, int arg1, int arg2) {
        logWtf("sendMessage should not be used in VcnGatewayConnection. See sendMessageAndAcquireWakeLock()");
        super.sendMessage(what, arg1, arg2);
    }

    public void sendMessage(int what, int arg1, int arg2, java.lang.Object obj) {
        logWtf("sendMessage should not be used in VcnGatewayConnection. See sendMessageAndAcquireWakeLock()");
        super.sendMessage(what, arg1, arg2, obj);
    }

    public void sendMessage(android.os.Message msg) {
        logWtf("sendMessage should not be used in VcnGatewayConnection. See sendMessageAndAcquireWakeLock()");
        super.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessageAndAcquireWakeLock(int what, int token) {
        acquireWakeLock();
        super.sendMessage(what, token);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessageAndAcquireWakeLock(int what, int token, com.android.server.vcn.VcnGatewayConnection.EventInfo data) {
        acquireWakeLock();
        super.sendMessage(what, token, Integer.MIN_VALUE, data);
    }

    private void sendMessageAndAcquireWakeLock(int what, int token, int arg2, com.android.server.vcn.VcnGatewayConnection.EventInfo data) {
        acquireWakeLock();
        super.sendMessage(what, token, arg2, data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: sendMessageAndAcquireWakeLock, reason: merged with bridge method [inline-methods] */
    public void lambda$createScheduledAlarm$0(android.os.Message msg) {
        acquireWakeLock();
        super.sendMessage(msg);
    }

    private void removeEqualMessages(int what) {
        removeEqualMessages(what, null);
    }

    private void removeEqualMessages(int what, java.lang.Object obj) {
        android.os.Handler handler = getHandler();
        if (handler != null) {
            handler.removeEqualMessages(what, obj);
        }
        maybeReleaseWakeLock();
    }

    private com.android.internal.util.WakeupMessage createScheduledAlarm(java.lang.String cmdName, final android.os.Message delayedMessage, long delay) {
        android.os.Handler handler = getHandler();
        if (handler == null) {
            logWarn("Attempted to schedule alarm after StateMachine has quit", new java.lang.IllegalStateException());
            return null;
        }
        com.android.internal.util.WakeupMessage alarm = this.mDeps.newWakeupMessage(this.mVcnContext, handler, cmdName, new java.lang.Runnable() { // from class: com.android.server.vcn.VcnGatewayConnection$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$createScheduledAlarm$0(delayedMessage);
            }
        });
        alarm.schedule(this.mDeps.getElapsedRealTime() + delay);
        return alarm;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTeardownTimeoutAlarm() {
        logVdbg("Setting teardown timeout alarm; mCurrentToken: " + this.mCurrentToken);
        if (this.mTeardownTimeoutAlarm != null) {
            logWtf("mTeardownTimeoutAlarm should be null before being set; mCurrentToken: " + this.mCurrentToken);
        }
        android.os.Message delayedMessage = obtainMessage(8, this.mCurrentToken);
        this.mTeardownTimeoutAlarm = createScheduledAlarm(TEARDOWN_TIMEOUT_ALARM, delayedMessage, java.util.concurrent.TimeUnit.SECONDS.toMillis(5L));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelTeardownTimeoutAlarm() {
        logVdbg("Cancelling teardown timeout alarm; mCurrentToken: " + this.mCurrentToken);
        if (this.mTeardownTimeoutAlarm != null) {
            this.mTeardownTimeoutAlarm.cancel();
            this.mTeardownTimeoutAlarm = null;
        }
        removeEqualMessages(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisconnectRequestAlarm() {
        logVdbg("Setting alarm to disconnect due to underlying network loss; mCurrentToken: " + this.mCurrentToken);
        if (this.mDisconnectRequestAlarm != null) {
            return;
        }
        android.os.Message delayedMessage = obtainMessage(7, Integer.MIN_VALUE, 0, new com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo(DISCONNECT_REASON_UNDERLYING_NETWORK_LOST, false));
        this.mDisconnectRequestAlarm = createScheduledAlarm(DISCONNECT_REQUEST_ALARM, delayedMessage, java.util.concurrent.TimeUnit.SECONDS.toMillis(30L));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelDisconnectRequestAlarm() {
        logVdbg("Cancelling alarm to disconnect due to underlying network loss; mCurrentToken: " + this.mCurrentToken);
        if (this.mDisconnectRequestAlarm != null) {
            this.mDisconnectRequestAlarm.cancel();
            this.mDisconnectRequestAlarm = null;
        }
        removeEqualMessages(7, new com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo(DISCONNECT_REASON_UNDERLYING_NETWORK_LOST, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRetryTimeoutAlarm(long delay) {
        logVdbg("Setting retry alarm; mCurrentToken: " + this.mCurrentToken);
        if (this.mRetryTimeoutAlarm != null) {
            logWtf("mRetryTimeoutAlarm should be null before being set; mCurrentToken: " + this.mCurrentToken);
        }
        android.os.Message delayedMessage = obtainMessage(2, this.mCurrentToken);
        this.mRetryTimeoutAlarm = createScheduledAlarm(RETRY_TIMEOUT_ALARM, delayedMessage, delay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelRetryTimeoutAlarm() {
        logVdbg("Cancel retry alarm; mCurrentToken: " + this.mCurrentToken);
        if (this.mRetryTimeoutAlarm != null) {
            this.mRetryTimeoutAlarm.cancel();
            this.mRetryTimeoutAlarm = null;
        }
        removeEqualMessages(2);
    }

    void setSafeModeAlarm() {
        boolean isFlagSafeModeConfigEnabled = this.mVcnContext.getFeatureFlags().safeModeConfig();
        logVdbg("isFlagSafeModeConfigEnabled " + isFlagSafeModeConfigEnabled);
        if (isFlagSafeModeConfigEnabled && !this.mConnectionConfig.isSafeModeEnabled()) {
            logVdbg("setSafeModeAlarm: safe mode disabled");
            return;
        }
        logVdbg("Setting safe mode alarm; mCurrentToken: " + this.mCurrentToken);
        if (this.mSafeModeTimeoutAlarm != null) {
            return;
        }
        android.os.Message delayedMessage = obtainMessage(10, Integer.MIN_VALUE);
        this.mSafeModeTimeoutAlarm = createScheduledAlarm(SAFEMODE_TIMEOUT_ALARM, delayedMessage, getSafeModeTimeoutMs(this.mVcnContext, this.mLastSnapshot, this.mSubscriptionGroup));
    }

    public static long getSafeModeTimeoutMs(com.android.server.vcn.VcnContext vcnContext, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, android.os.ParcelUuid subGrp) {
        int defaultSeconds;
        if (vcnContext.isInTestMode()) {
            defaultSeconds = 10;
        } else {
            defaultSeconds = 30;
        }
        com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig = snapshot.getCarrierConfigForSubGrp(subGrp);
        int resultSeconds = defaultSeconds;
        if (vcnContext.isFlagSafeModeTimeoutConfigEnabled() && carrierConfig != null) {
            resultSeconds = carrierConfig.getInt("vcn_safe_mode_timeout_seconds_key", defaultSeconds);
        }
        return java.util.concurrent.TimeUnit.SECONDS.toMillis(resultSeconds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelSafeModeAlarm() {
        logVdbg("Cancel safe mode alarm; mCurrentToken: " + this.mCurrentToken);
        if (this.mSafeModeTimeoutAlarm != null) {
            this.mSafeModeTimeoutAlarm.cancel();
            this.mSafeModeTimeoutAlarm = null;
        }
        removeEqualMessages(10);
    }

    private void sessionLostWithoutCallback(int token, java.lang.Exception exception) {
        sendMessageAndAcquireWakeLock(3, token, new com.android.server.vcn.VcnGatewayConnection.EventSessionLostInfo(exception));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sessionLost(int token, java.lang.Exception exception) {
        if (exception != null) {
            this.mGatewayStatusCallback.onGatewayConnectionError(this.mConnectionConfig.getGatewayConnectionName(), 0, java.lang.RuntimeException.class.getName(), "Received " + exception.getClass().getSimpleName() + " with message: " + exception.getMessage());
        }
        sessionLostWithoutCallback(token, exception);
    }

    private static boolean isIkeAuthFailure(java.lang.Exception exception) {
        return (exception instanceof android.net.ipsec.ike.exceptions.IkeProtocolException) && ((android.net.ipsec.ike.exceptions.IkeProtocolException) exception).getErrorType() == 24;
    }

    private void notifyStatusCallbackForSessionClosed(java.lang.Exception exception) {
        int errorCode;
        java.lang.String exceptionClass;
        java.lang.String exceptionMessage;
        if (isIkeAuthFailure(exception)) {
            errorCode = 1;
            exceptionClass = exception.getClass().getName();
            exceptionMessage = exception.getMessage();
        } else if ((exception instanceof android.net.ipsec.ike.exceptions.IkeInternalException) && (exception.getCause() instanceof java.io.IOException)) {
            errorCode = 2;
            exceptionClass = java.io.IOException.class.getName();
            exceptionMessage = exception.getCause().getMessage();
        } else {
            errorCode = 0;
            exceptionClass = java.lang.RuntimeException.class.getName();
            exceptionMessage = "Received " + exception.getClass().getSimpleName() + " with message: " + exception.getMessage();
        }
        logDbg("Encountered error; code=" + errorCode + ", exceptionClass=" + exceptionClass + ", exceptionMessage=" + exceptionMessage);
        this.mGatewayStatusCallback.onGatewayConnectionError(this.mConnectionConfig.getGatewayConnectionName(), errorCode, exceptionClass, exceptionMessage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ikeConnectionInfoChanged(int token, android.net.ipsec.ike.IkeSessionConnectionInfo ikeConnectionInfo) {
        sendMessageAndAcquireWakeLock(12, token, new com.android.server.vcn.VcnGatewayConnection.EventIkeConnectionInfoChangedInfo(ikeConnectionInfo));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sessionClosed(int token, java.lang.Exception exception) {
        if (exception != null) {
            notifyStatusCallbackForSessionClosed(exception);
        }
        sessionLostWithoutCallback(token, exception);
        sendMessageAndAcquireWakeLock(4, token);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void migrationCompleted(int token, android.net.IpSecTransform inTransform, android.net.IpSecTransform outTransform) {
        sendMessageAndAcquireWakeLock(11, token, new com.android.server.vcn.VcnGatewayConnection.EventMigrationCompletedInfo(inTransform, outTransform));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void childTransformCreated(int token, android.net.IpSecTransform transform, int direction) {
        sendMessageAndAcquireWakeLock(5, token, new com.android.server.vcn.VcnGatewayConnection.EventTransformCreatedInfo(direction, transform));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void childOpened(int token, com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration childConfig) {
        sendMessageAndAcquireWakeLock(6, token, new com.android.server.vcn.VcnGatewayConnection.EventSetupCompletedInfo(childConfig));
    }

    private abstract class BaseState extends com.android.internal.util.State {
        protected abstract void processStateMsg(android.os.Message message) throws java.lang.Exception;

        private BaseState() {
        }

        public void enter() {
            try {
                enterState();
            } catch (java.lang.Exception e) {
                com.android.server.vcn.VcnGatewayConnection.this.logWtf("Uncaught exception", e);
                com.android.server.vcn.VcnGatewayConnection.this.sendDisconnectRequestedAndAcquireWakelock(com.android.server.vcn.VcnGatewayConnection.DISCONNECT_REASON_INTERNAL_ERROR + e.toString(), true);
            }
        }

        protected void enterState() throws java.lang.Exception {
        }

        protected boolean isValidToken(int token) {
            return true;
        }

        public final boolean processMessage(android.os.Message msg) {
            int token = msg.arg1;
            if (!isValidToken(token)) {
                com.android.server.vcn.VcnGatewayConnection.this.logDbg("Message called with obsolete token: " + token + "; what: " + msg.what);
                return true;
            }
            try {
                processStateMsg(msg);
            } catch (java.lang.Exception e) {
                com.android.server.vcn.VcnGatewayConnection.this.logWtf("Uncaught exception", e);
                com.android.server.vcn.VcnGatewayConnection.this.sendDisconnectRequestedAndAcquireWakelock(com.android.server.vcn.VcnGatewayConnection.DISCONNECT_REASON_INTERNAL_ERROR + e.toString(), true);
            }
            com.android.server.vcn.VcnGatewayConnection.this.maybeReleaseWakeLock();
            return true;
        }

        public void exit() {
            try {
                exitState();
            } catch (java.lang.Exception e) {
                com.android.server.vcn.VcnGatewayConnection.this.logWtf("Uncaught exception", e);
                com.android.server.vcn.VcnGatewayConnection.this.sendDisconnectRequestedAndAcquireWakelock(com.android.server.vcn.VcnGatewayConnection.DISCONNECT_REASON_INTERNAL_ERROR + e.toString(), true);
            }
        }

        protected void exitState() throws java.lang.Exception {
        }

        protected void logUnhandledMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                    logUnexpectedEvent(msg.what);
                    break;
                default:
                    logWtfUnknownEvent(msg.what);
                    break;
            }
        }

        protected void teardownNetwork() {
            if (com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent != null) {
                com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent.unregister();
                com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent = null;
            }
        }

        protected void handleDisconnectRequested(com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo info) {
            com.android.server.vcn.VcnGatewayConnection.this.logInfo("Tearing down. Cause: " + info.reason + "; quitting = " + info.shouldQuit);
            if (info.shouldQuit) {
                com.android.server.vcn.VcnGatewayConnection.this.mIsQuitting.setTrue();
            }
            teardownNetwork();
            if (com.android.server.vcn.VcnGatewayConnection.this.mIkeSession == null) {
                com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mDisconnectedState);
            } else {
                com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mDisconnectingState);
            }
        }

        protected void handleSafeModeTimeoutExceeded() {
            com.android.server.vcn.VcnGatewayConnection.this.mSafeModeTimeoutAlarm = null;
            com.android.server.vcn.VcnGatewayConnection.this.logInfo("Entering safe mode after timeout exceeded");
            teardownNetwork();
            com.android.server.vcn.VcnGatewayConnection.this.mIsInSafeMode = true;
            com.android.server.vcn.VcnGatewayConnection.this.mGatewayStatusCallback.onSafeModeStatusChanged();
        }

        protected void logUnexpectedEvent(int what) {
            com.android.server.vcn.VcnGatewayConnection.this.logVdbg("Unexpected event code " + what + " in state " + getClass().getSimpleName());
        }

        protected void logWtfUnknownEvent(int what) {
            com.android.server.vcn.VcnGatewayConnection.this.logWtf("Unknown event code " + what + " in state " + getClass().getSimpleName());
        }
    }

    private class DisconnectedState extends com.android.server.vcn.VcnGatewayConnection.BaseState {
        private DisconnectedState() {
            super();
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void enterState() {
            if (com.android.server.vcn.VcnGatewayConnection.this.mIsQuitting.getValue()) {
                com.android.server.vcn.VcnGatewayConnection.this.quitNow();
            }
            if (com.android.server.vcn.VcnGatewayConnection.this.mIkeSession != null || com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent != null) {
                com.android.server.vcn.VcnGatewayConnection.this.logWtf("Active IKE Session or NetworkAgent in DisconnectedState");
            }
            com.android.server.vcn.VcnGatewayConnection.this.cancelSafeModeAlarm();
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void processStateMsg(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.vcn.VcnGatewayConnection.this.mUnderlying = ((com.android.server.vcn.VcnGatewayConnection.EventUnderlyingNetworkChangedInfo) msg.obj).newUnderlying;
                    if (com.android.server.vcn.VcnGatewayConnection.this.mUnderlying != null) {
                        com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mConnectingState);
                    }
                    break;
                case 7:
                    if (((com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo) msg.obj).shouldQuit) {
                        com.android.server.vcn.VcnGatewayConnection.this.mIsQuitting.setTrue();
                        com.android.server.vcn.VcnGatewayConnection.this.quitNow();
                    }
                    break;
                default:
                    logUnhandledMessage(msg);
                    break;
            }
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void exitState() {
            com.android.server.vcn.VcnGatewayConnection.this.setSafeModeAlarm();
        }
    }

    private abstract class ActiveBaseState extends com.android.server.vcn.VcnGatewayConnection.BaseState {
        private ActiveBaseState() {
            super();
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected boolean isValidToken(int token) {
            return token == Integer.MIN_VALUE || token == com.android.server.vcn.VcnGatewayConnection.this.mCurrentToken;
        }
    }

    private class DisconnectingState extends com.android.server.vcn.VcnGatewayConnection.ActiveBaseState {
        private boolean mSkipRetryTimeout;

        private DisconnectingState() {
            super();
            this.mSkipRetryTimeout = false;
        }

        public void setSkipRetryTimeout(boolean shouldSkip) {
            this.mSkipRetryTimeout = shouldSkip;
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void enterState() throws java.lang.Exception {
            if (com.android.server.vcn.VcnGatewayConnection.this.mIkeSession == null) {
                com.android.server.vcn.VcnGatewayConnection.this.logWtf("IKE session was already closed when entering Disconnecting state.");
                com.android.server.vcn.VcnGatewayConnection.this.sendMessageAndAcquireWakeLock(4, com.android.server.vcn.VcnGatewayConnection.this.mCurrentToken);
            } else if (com.android.server.vcn.VcnGatewayConnection.this.mUnderlying == null) {
                com.android.server.vcn.VcnGatewayConnection.this.mIkeSession.kill();
            } else {
                com.android.server.vcn.VcnGatewayConnection.this.mIkeSession.close();
                com.android.server.vcn.VcnGatewayConnection.this.setTeardownTimeoutAlarm();
            }
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void processStateMsg(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.vcn.VcnGatewayConnection.this.mUnderlying = ((com.android.server.vcn.VcnGatewayConnection.EventUnderlyingNetworkChangedInfo) msg.obj).newUnderlying;
                    if (com.android.server.vcn.VcnGatewayConnection.this.mUnderlying != null) {
                        return;
                    }
                    break;
                case 4:
                    com.android.server.vcn.VcnGatewayConnection.this.mIkeSession = null;
                    if (!com.android.server.vcn.VcnGatewayConnection.this.mIsQuitting.getValue() && com.android.server.vcn.VcnGatewayConnection.this.mUnderlying != null) {
                        com.android.server.vcn.VcnGatewayConnection.this.transitionTo(this.mSkipRetryTimeout ? com.android.server.vcn.VcnGatewayConnection.this.mConnectingState : com.android.server.vcn.VcnGatewayConnection.this.mRetryTimeoutState);
                        return;
                    } else {
                        teardownNetwork();
                        com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mDisconnectedState);
                        return;
                    }
                case 7:
                    com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo info = (com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo) msg.obj;
                    if (info.shouldQuit) {
                        com.android.server.vcn.VcnGatewayConnection.this.mIsQuitting.setTrue();
                    }
                    teardownNetwork();
                    if (info.reason.equals(com.android.server.vcn.VcnGatewayConnection.DISCONNECT_REASON_UNDERLYING_NETWORK_LOST)) {
                        com.android.server.vcn.VcnGatewayConnection.this.mIkeSession.kill();
                        return;
                    }
                    return;
                case 8:
                    break;
                case 10:
                    handleSafeModeTimeoutExceeded();
                    return;
                default:
                    logUnhandledMessage(msg);
                    return;
            }
            com.android.server.vcn.VcnGatewayConnection.this.mIkeSession.kill();
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void exitState() throws java.lang.Exception {
            this.mSkipRetryTimeout = false;
            com.android.server.vcn.VcnGatewayConnection.this.cancelTeardownTimeoutAlarm();
        }
    }

    private class ConnectingState extends com.android.server.vcn.VcnGatewayConnection.ActiveBaseState {
        private ConnectingState() {
            super();
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void enterState() {
            if (com.android.server.vcn.VcnGatewayConnection.this.mIkeSession != null) {
                com.android.server.vcn.VcnGatewayConnection.this.logWtf("ConnectingState entered with active session");
                com.android.server.vcn.VcnGatewayConnection.this.mIkeSession.kill();
                com.android.server.vcn.VcnGatewayConnection.this.mIkeSession = null;
            }
            com.android.server.vcn.VcnGatewayConnection.this.mIkeSession = com.android.server.vcn.VcnGatewayConnection.this.buildIkeSession(com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network);
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void processStateMsg(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.vcn.routeselection.UnderlyingNetworkRecord oldUnderlying = com.android.server.vcn.VcnGatewayConnection.this.mUnderlying;
                    com.android.server.vcn.VcnGatewayConnection.this.mUnderlying = ((com.android.server.vcn.VcnGatewayConnection.EventUnderlyingNetworkChangedInfo) msg.obj).newUnderlying;
                    if (oldUnderlying == null) {
                        com.android.server.vcn.VcnGatewayConnection.this.logWtf("Old underlying network was null in connected state. Bug?");
                    }
                    if (com.android.server.vcn.VcnGatewayConnection.this.mUnderlying == null) {
                        com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mDisconnectingState);
                        return;
                    } else if (oldUnderlying == null || !com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network.equals(oldUnderlying.network)) {
                        com.android.server.vcn.VcnGatewayConnection.this.mDisconnectingState.setSkipRetryTimeout(true);
                    } else {
                        return;
                    }
                    break;
                case 2:
                case 8:
                case 9:
                case 11:
                default:
                    logUnhandledMessage(msg);
                    return;
                case 3:
                    break;
                case 4:
                    com.android.server.vcn.VcnGatewayConnection.this.deferMessage(msg);
                    com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mDisconnectingState);
                    return;
                case 5:
                case 6:
                case 12:
                    com.android.server.vcn.VcnGatewayConnection.this.deferMessage(msg);
                    com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mConnectedState);
                    return;
                case 7:
                    handleDisconnectRequested((com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo) msg.obj);
                    return;
                case 10:
                    handleSafeModeTimeoutExceeded();
                    return;
            }
            com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mDisconnectingState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    abstract class ConnectedStateBase extends com.android.server.vcn.VcnGatewayConnection.ActiveBaseState {
        private ConnectedStateBase() {
            super();
        }

        protected void updateNetworkAgent(android.net.IpSecManager.IpSecTunnelInterface tunnelIface, com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent agent, com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration childConfig, android.net.ipsec.ike.IkeSessionConnectionInfo ikeConnectionInfo) {
            android.net.NetworkCapabilities caps = com.android.server.vcn.VcnGatewayConnection.buildNetworkCapabilities(com.android.server.vcn.VcnGatewayConnection.this.mConnectionConfig, com.android.server.vcn.VcnGatewayConnection.this.mUnderlying, com.android.server.vcn.VcnGatewayConnection.this.mIsMobileDataEnabled);
            android.net.LinkProperties lp = com.android.server.vcn.VcnGatewayConnection.this.buildConnectedLinkProperties(com.android.server.vcn.VcnGatewayConnection.this.mConnectionConfig, tunnelIface, childConfig, com.android.server.vcn.VcnGatewayConnection.this.mUnderlying, ikeConnectionInfo);
            agent.sendNetworkCapabilities(caps);
            agent.sendLinkProperties(lp);
            agent.setUnderlyingNetworks(com.android.server.vcn.VcnGatewayConnection.this.mUnderlying == null ? null : java.util.Collections.singletonList(com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network));
        }

        protected com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent buildNetworkAgent(android.net.IpSecManager.IpSecTunnelInterface tunnelIface, com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration childConfig, android.net.ipsec.ike.IkeSessionConnectionInfo ikeConnectionInfo) {
            android.net.NetworkCapabilities caps = com.android.server.vcn.VcnGatewayConnection.buildNetworkCapabilities(com.android.server.vcn.VcnGatewayConnection.this.mConnectionConfig, com.android.server.vcn.VcnGatewayConnection.this.mUnderlying, com.android.server.vcn.VcnGatewayConnection.this.mIsMobileDataEnabled);
            android.net.LinkProperties lp = com.android.server.vcn.VcnGatewayConnection.this.buildConnectedLinkProperties(com.android.server.vcn.VcnGatewayConnection.this.mConnectionConfig, tunnelIface, childConfig, com.android.server.vcn.VcnGatewayConnection.this.mUnderlying, ikeConnectionInfo);
            android.net.NetworkAgentConfig nac = new android.net.NetworkAgentConfig.Builder().setLegacyType(0).setLegacyTypeName(com.android.server.vcn.VcnGatewayConnection.NETWORK_INFO_NETWORK_TYPE_STRING).setLegacySubType(0).setLegacySubTypeName(android.telephony.TelephonyManager.getNetworkTypeName(0)).setLegacyExtraInfo(com.android.server.vcn.VcnGatewayConnection.NETWORK_INFO_EXTRA_INFO).build();
            com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent agent = com.android.server.vcn.VcnGatewayConnection.this.mDeps.newNetworkAgent(com.android.server.vcn.VcnGatewayConnection.this.mVcnContext, com.android.server.vcn.VcnGatewayConnection.TAG, caps, lp, com.android.server.vcn.Vcn.getNetworkScore(), nac, com.android.server.vcn.VcnGatewayConnection.this.mVcnContext.getVcnNetworkProvider(), new java.util.function.Consumer() { // from class: com.android.server.vcn.VcnGatewayConnection$ConnectedStateBase$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$buildNetworkAgent$0((com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent) obj);
                }
            }, new java.util.function.Consumer() { // from class: com.android.server.vcn.VcnGatewayConnection$ConnectedStateBase$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$buildNetworkAgent$1((java.lang.Integer) obj);
                }
            });
            agent.register();
            agent.markConnected();
            return agent;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$buildNetworkAgent$0(com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent agentRef) {
            if (com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent != agentRef) {
                com.android.server.vcn.VcnGatewayConnection.this.logDbg("unwanted() called on stale NetworkAgent");
            } else {
                com.android.server.vcn.VcnGatewayConnection.this.logInfo(com.android.server.vcn.VcnGatewayConnection.DISCONNECT_REASON_NETWORK_AGENT_UNWANTED);
                com.android.server.vcn.VcnGatewayConnection.this.teardownAsynchronously();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$buildNetworkAgent$1(java.lang.Integer status) {
            if (com.android.server.vcn.VcnGatewayConnection.this.mIsQuitting.getValue()) {
            }
            switch (status.intValue()) {
                case 1:
                    clearFailedAttemptCounterAndSafeModeAlarm();
                    break;
                case 2:
                    if (com.android.server.vcn.VcnGatewayConnection.this.mUnderlying != null) {
                        com.android.server.vcn.VcnGatewayConnection.this.mConnectivityManager.reportNetworkConnectivity(com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network, false);
                    }
                    com.android.server.vcn.VcnGatewayConnection.this.setSafeModeAlarm();
                    break;
                default:
                    com.android.server.vcn.VcnGatewayConnection.this.logWtf("Unknown validation status " + status + "; ignoring");
                    break;
            }
        }

        protected void clearFailedAttemptCounterAndSafeModeAlarm() {
            com.android.server.vcn.VcnGatewayConnection.this.mVcnContext.ensureRunningOnLooperThread();
            com.android.server.vcn.VcnGatewayConnection.this.mFailedAttempts = 0;
            com.android.server.vcn.VcnGatewayConnection.this.cancelSafeModeAlarm();
            com.android.server.vcn.VcnGatewayConnection.this.mIsInSafeMode = false;
            com.android.server.vcn.VcnGatewayConnection.this.mGatewayStatusCallback.onSafeModeStatusChanged();
        }

        protected void applyTransform(int token, android.net.IpSecManager.IpSecTunnelInterface tunnelIface, android.net.Network underlyingNetwork, android.net.IpSecTransform transform, int direction) {
            if (direction != 0 && direction != 1) {
                com.android.server.vcn.VcnGatewayConnection.this.logWtf("Applying transform for unexpected direction: " + direction);
            }
            try {
                tunnelIface.setUnderlyingNetwork(underlyingNetwork);
                com.android.server.vcn.VcnGatewayConnection.this.mIpSecManager.applyTunnelModeTransform(tunnelIface, direction, transform);
                if (direction == 0 && com.android.server.vcn.VcnGatewayConnection.this.mVcnContext.isFlagNetworkMetricMonitorEnabled() && com.android.server.vcn.VcnGatewayConnection.this.mVcnContext.isFlagIpSecTransformStateEnabled()) {
                    com.android.server.vcn.VcnGatewayConnection.this.mUnderlyingNetworkController.updateInboundTransform(com.android.server.vcn.VcnGatewayConnection.this.mUnderlying, transform);
                }
                java.util.Set<java.lang.Integer> exposedCaps = com.android.server.vcn.VcnGatewayConnection.this.mConnectionConfig.getAllExposedCapabilities();
                if (direction == 0 && exposedCaps.contains(2)) {
                    com.android.server.vcn.VcnGatewayConnection.this.mIpSecManager.applyTunnelModeTransform(tunnelIface, 2, transform);
                }
            } catch (java.io.IOException | java.lang.IllegalArgumentException e) {
                com.android.server.vcn.VcnGatewayConnection.this.logInfo("Transform application failed for network " + token, e);
                com.android.server.vcn.VcnGatewayConnection.this.sessionLost(token, e);
            }
        }

        protected void setupInterface(int token, android.net.IpSecManager.IpSecTunnelInterface tunnelIface, com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration childConfig, com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration oldChildConfig) {
            try {
                java.util.Collection<? extends android.net.LinkAddress> arraySet = new android.util.ArraySet<>(childConfig.getInternalAddresses());
                android.util.ArraySet arraySet2 = new android.util.ArraySet();
                if (oldChildConfig != null) {
                    arraySet2.addAll(oldChildConfig.getInternalAddresses());
                }
                java.util.Set<android.net.LinkAddress> toAdd = new android.util.ArraySet<>();
                toAdd.addAll(arraySet);
                toAdd.removeAll(arraySet2);
                java.util.Set<android.net.LinkAddress> toRemove = new android.util.ArraySet<>();
                toRemove.addAll(arraySet2);
                toRemove.removeAll(arraySet);
                for (android.net.LinkAddress address : toAdd) {
                    tunnelIface.addAddress(address.getAddress(), address.getPrefixLength());
                }
                for (android.net.LinkAddress address2 : toRemove) {
                    tunnelIface.removeAddress(address2.getAddress(), address2.getPrefixLength());
                }
            } catch (java.io.IOException e) {
                com.android.server.vcn.VcnGatewayConnection.this.logInfo("Adding address to tunnel failed for token " + token, e);
                com.android.server.vcn.VcnGatewayConnection.this.sessionLost(token, e);
            }
        }
    }

    class ConnectedState extends com.android.server.vcn.VcnGatewayConnection.ConnectedStateBase {
        ConnectedState() {
            super();
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void enterState() throws java.lang.Exception {
            if (com.android.server.vcn.VcnGatewayConnection.this.mTunnelIface == null) {
                try {
                    com.android.server.vcn.VcnGatewayConnection.this.mTunnelIface = com.android.server.vcn.VcnGatewayConnection.this.mIpSecManager.createIpSecTunnelInterface(com.android.server.vcn.VcnGatewayConnection.DUMMY_ADDR, com.android.server.vcn.VcnGatewayConnection.DUMMY_ADDR, com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network);
                } catch (android.net.IpSecManager.ResourceUnavailableException | java.io.IOException e) {
                    com.android.server.vcn.VcnGatewayConnection.this.teardownAsynchronously();
                }
            }
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void processStateMsg(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    handleUnderlyingNetworkChanged(msg);
                    break;
                case 2:
                case 8:
                case 9:
                default:
                    logUnhandledMessage(msg);
                    break;
                case 3:
                    com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mDisconnectingState);
                    break;
                case 4:
                    com.android.server.vcn.VcnGatewayConnection.this.deferMessage(msg);
                    com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mDisconnectingState);
                    break;
                case 5:
                    com.android.server.vcn.VcnGatewayConnection.EventTransformCreatedInfo transformCreatedInfo = (com.android.server.vcn.VcnGatewayConnection.EventTransformCreatedInfo) msg.obj;
                    applyTransform(com.android.server.vcn.VcnGatewayConnection.this.mCurrentToken, com.android.server.vcn.VcnGatewayConnection.this.mTunnelIface, com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network, transformCreatedInfo.transform, transformCreatedInfo.direction);
                    break;
                case 6:
                    com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration oldChildConfig = com.android.server.vcn.VcnGatewayConnection.this.mChildConfig;
                    com.android.server.vcn.VcnGatewayConnection.this.mChildConfig = ((com.android.server.vcn.VcnGatewayConnection.EventSetupCompletedInfo) msg.obj).childSessionConfig;
                    setupInterfaceAndNetworkAgent(com.android.server.vcn.VcnGatewayConnection.this.mCurrentToken, com.android.server.vcn.VcnGatewayConnection.this.mTunnelIface, com.android.server.vcn.VcnGatewayConnection.this.mChildConfig, oldChildConfig, com.android.server.vcn.VcnGatewayConnection.this.mIkeConnectionInfo);
                    int parallelTunnelCount = com.android.server.vcn.VcnGatewayConnection.this.mDeps.getParallelTunnelCount(com.android.server.vcn.VcnGatewayConnection.this.mLastSnapshot, com.android.server.vcn.VcnGatewayConnection.this.mSubscriptionGroup);
                    com.android.server.vcn.VcnGatewayConnection.this.logInfo("Parallel tunnel count: " + parallelTunnelCount);
                    for (int i = 0; i < parallelTunnelCount - 1; i++) {
                        com.android.server.vcn.VcnGatewayConnection.this.mIkeSession.openChildSession(com.android.server.vcn.VcnGatewayConnection.this.buildOpportunisticChildParams(), com.android.server.vcn.VcnGatewayConnection.this.new VcnChildSessionCallback(com.android.server.vcn.VcnGatewayConnection.this.mCurrentToken, true));
                    }
                    break;
                case 7:
                    handleDisconnectRequested((com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo) msg.obj);
                    break;
                case 10:
                    handleSafeModeTimeoutExceeded();
                    break;
                case 11:
                    com.android.server.vcn.VcnGatewayConnection.EventMigrationCompletedInfo migrationCompletedInfo = (com.android.server.vcn.VcnGatewayConnection.EventMigrationCompletedInfo) msg.obj;
                    handleMigrationCompleted(migrationCompletedInfo);
                    break;
                case 12:
                    com.android.server.vcn.VcnGatewayConnection.this.mIkeConnectionInfo = ((com.android.server.vcn.VcnGatewayConnection.EventIkeConnectionInfoChangedInfo) msg.obj).ikeConnectionInfo;
                    break;
                case 13:
                    android.net.Network networkWithDataStall = ((com.android.server.vcn.VcnGatewayConnection.EventDataStallSuspectedInfo) msg.obj).network;
                    handleDataStallSuspected(networkWithDataStall);
                    break;
            }
        }

        private void handleMigrationCompleted(com.android.server.vcn.VcnGatewayConnection.EventMigrationCompletedInfo migrationCompletedInfo) {
            com.android.server.vcn.VcnGatewayConnection.this.logInfo("Migration completed: " + com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network);
            applyTransform(com.android.server.vcn.VcnGatewayConnection.this.mCurrentToken, com.android.server.vcn.VcnGatewayConnection.this.mTunnelIface, com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network, migrationCompletedInfo.inTransform, 0);
            applyTransform(com.android.server.vcn.VcnGatewayConnection.this.mCurrentToken, com.android.server.vcn.VcnGatewayConnection.this.mTunnelIface, com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network, migrationCompletedInfo.outTransform, 1);
            updateNetworkAgent(com.android.server.vcn.VcnGatewayConnection.this.mTunnelIface, com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent, com.android.server.vcn.VcnGatewayConnection.this.mChildConfig, com.android.server.vcn.VcnGatewayConnection.this.mIkeConnectionInfo);
            com.android.server.vcn.VcnGatewayConnection.this.mConnectivityManager.reportNetworkConnectivity(com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent.getNetwork(), false);
        }

        private void handleUnderlyingNetworkChanged(android.os.Message msg) {
            com.android.server.vcn.routeselection.UnderlyingNetworkRecord oldUnderlying = com.android.server.vcn.VcnGatewayConnection.this.mUnderlying;
            com.android.server.vcn.VcnGatewayConnection.this.mUnderlying = ((com.android.server.vcn.VcnGatewayConnection.EventUnderlyingNetworkChangedInfo) msg.obj).newUnderlying;
            if (com.android.server.vcn.VcnGatewayConnection.this.mUnderlying == null) {
                com.android.server.vcn.VcnGatewayConnection.this.logInfo("Underlying network lost");
                return;
            }
            if (oldUnderlying == null || !oldUnderlying.network.equals(com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network)) {
                com.android.server.vcn.VcnGatewayConnection.this.logInfo("Migrating to new network: " + com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network);
                com.android.server.vcn.VcnGatewayConnection.this.mIkeSession.setNetwork(com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network);
            } else if (com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent != null && com.android.server.vcn.VcnGatewayConnection.this.mChildConfig != null) {
                updateNetworkAgent(com.android.server.vcn.VcnGatewayConnection.this.mTunnelIface, com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent, com.android.server.vcn.VcnGatewayConnection.this.mChildConfig, com.android.server.vcn.VcnGatewayConnection.this.mIkeConnectionInfo);
            }
        }

        private void handleDataStallSuspected(android.net.Network networkWithDataStall) {
            if (com.android.server.vcn.VcnGatewayConnection.this.mUnderlying != null && com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent != null && com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent.getNetwork().equals(networkWithDataStall)) {
                com.android.server.vcn.VcnGatewayConnection.this.logInfo("Perform Mobility update to recover from suspected data stall");
                com.android.server.vcn.VcnGatewayConnection.this.mIkeSession.setNetwork(com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network);
            }
        }

        protected void setupInterfaceAndNetworkAgent(int token, android.net.IpSecManager.IpSecTunnelInterface tunnelIface, com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration childConfig, com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration oldChildConfig, android.net.ipsec.ike.IkeSessionConnectionInfo ikeConnectionInfo) {
            setupInterface(token, tunnelIface, childConfig, oldChildConfig);
            if (com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent == null) {
                com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent = buildNetworkAgent(tunnelIface, childConfig, ikeConnectionInfo);
            } else {
                updateNetworkAgent(tunnelIface, com.android.server.vcn.VcnGatewayConnection.this.mNetworkAgent, childConfig, ikeConnectionInfo);
                clearFailedAttemptCounterAndSafeModeAlarm();
            }
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void exitState() {
            com.android.server.vcn.VcnGatewayConnection.this.setSafeModeAlarm();
        }
    }

    class RetryTimeoutState extends com.android.server.vcn.VcnGatewayConnection.ActiveBaseState {
        RetryTimeoutState() {
            super();
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void enterState() throws java.lang.Exception {
            com.android.server.vcn.VcnGatewayConnection.this.mFailedAttempts++;
            if (com.android.server.vcn.VcnGatewayConnection.this.mUnderlying == null) {
                com.android.server.vcn.VcnGatewayConnection.this.logWtf("Underlying network was null in retry state");
                teardownNetwork();
                com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mDisconnectedState);
                return;
            }
            com.android.server.vcn.VcnGatewayConnection.this.setRetryTimeoutAlarm(getNextRetryIntervalsMs());
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        protected void processStateMsg(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.vcn.routeselection.UnderlyingNetworkRecord oldUnderlying = com.android.server.vcn.VcnGatewayConnection.this.mUnderlying;
                    com.android.server.vcn.VcnGatewayConnection.this.mUnderlying = ((com.android.server.vcn.VcnGatewayConnection.EventUnderlyingNetworkChangedInfo) msg.obj).newUnderlying;
                    if (com.android.server.vcn.VcnGatewayConnection.this.mUnderlying == null) {
                        teardownNetwork();
                        com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mDisconnectedState);
                        return;
                    } else if (oldUnderlying != null && com.android.server.vcn.VcnGatewayConnection.this.mUnderlying.network.equals(oldUnderlying.network)) {
                        return;
                    }
                case 2:
                    break;
                case 7:
                    handleDisconnectRequested((com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo) msg.obj);
                    return;
                case 10:
                    handleSafeModeTimeoutExceeded();
                    return;
                default:
                    logUnhandledMessage(msg);
                    return;
            }
            com.android.server.vcn.VcnGatewayConnection.this.transitionTo(com.android.server.vcn.VcnGatewayConnection.this.mConnectingState);
        }

        @Override // com.android.server.vcn.VcnGatewayConnection.BaseState
        public void exitState() {
            com.android.server.vcn.VcnGatewayConnection.this.cancelRetryTimeoutAlarm();
        }

        private long getNextRetryIntervalsMs() {
            int retryDelayIndex = com.android.server.vcn.VcnGatewayConnection.this.mFailedAttempts - 1;
            long[] retryIntervalsMs = com.android.server.vcn.VcnGatewayConnection.this.mConnectionConfig.getRetryIntervalsMillis();
            if (retryDelayIndex >= retryIntervalsMs.length) {
                return retryIntervalsMs[retryIntervalsMs.length - 1];
            }
            return retryIntervalsMs[retryDelayIndex];
        }
    }

    static android.net.NetworkCapabilities buildNetworkCapabilities(android.net.vcn.VcnGatewayConnectionConfig gatewayConnectionConfig, com.android.server.vcn.routeselection.UnderlyingNetworkRecord underlying, boolean isMobileDataEnabled) {
        int[] adminUids;
        android.net.NetworkCapabilities.Builder builder = new android.net.NetworkCapabilities.Builder();
        builder.addTransportType(0);
        builder.addCapability(28);
        builder.addCapability(20);
        builder.addCapability(21);
        java.util.Iterator it = gatewayConnectionConfig.getAllExposedCapabilities().iterator();
        while (it.hasNext()) {
            int cap = ((java.lang.Integer) it.next()).intValue();
            if (isMobileDataEnabled || (cap != 12 && cap != 2)) {
                builder.addCapability(cap);
            }
        }
        if (underlying != null) {
            android.net.NetworkCapabilities underlyingCaps = underlying.networkCapabilities;
            for (int cap2 : MERGED_CAPABILITIES) {
                if (underlyingCaps.hasCapability(cap2)) {
                    builder.addCapability(cap2);
                }
            }
            int[] underlyingAdminUids = underlyingCaps.getAdministratorUids();
            java.util.Arrays.sort(underlyingAdminUids);
            if (underlyingCaps.getOwnerUid() > 0 && java.util.Arrays.binarySearch(underlyingAdminUids, underlyingCaps.getOwnerUid()) < 0) {
                adminUids = java.util.Arrays.copyOf(underlyingAdminUids, underlyingAdminUids.length + 1);
                adminUids[adminUids.length - 1] = underlyingCaps.getOwnerUid();
                java.util.Arrays.sort(adminUids);
            } else {
                adminUids = underlyingAdminUids;
            }
            builder.setOwnerUid(android.os.Process.myUid());
            int[] adminUids2 = java.util.Arrays.copyOf(adminUids, adminUids.length + 1);
            adminUids2[adminUids2.length - 1] = android.os.Process.myUid();
            builder.setAdministratorUids(adminUids2);
            builder.setLinkUpstreamBandwidthKbps(underlyingCaps.getLinkUpstreamBandwidthKbps());
            builder.setLinkDownstreamBandwidthKbps(underlyingCaps.getLinkDownstreamBandwidthKbps());
            if (!underlyingCaps.hasTransport(1) || !(underlyingCaps.getTransportInfo() instanceof android.net.wifi.WifiInfo)) {
                if (underlyingCaps.hasTransport(0) && (underlyingCaps.getNetworkSpecifier() instanceof android.net.TelephonyNetworkSpecifier)) {
                    android.net.TelephonyNetworkSpecifier telNetSpecifier = (android.net.TelephonyNetworkSpecifier) underlyingCaps.getNetworkSpecifier();
                    builder.setTransportInfo(new android.net.vcn.VcnTransportInfo(telNetSpecifier.getSubscriptionId(), gatewayConnectionConfig.getMinUdpPort4500NatTimeoutSeconds()));
                } else {
                    android.util.Slog.wtf(TAG, "Unknown transport type or missing TransportInfo/NetworkSpecifier for non-null underlying network");
                }
            } else {
                android.net.wifi.WifiInfo wifiInfo = (android.net.wifi.WifiInfo) underlyingCaps.getTransportInfo();
                builder.setTransportInfo(new android.net.vcn.VcnTransportInfo(wifiInfo, gatewayConnectionConfig.getMinUdpPort4500NatTimeoutSeconds()));
            }
            builder.setUnderlyingNetworks(java.util.List.of(underlying.network));
        } else {
            android.util.Slog.wtf(TAG, "No underlying network while building network capabilities", new java.lang.IllegalStateException());
        }
        return builder.build();
    }

    android.net.LinkProperties buildConnectedLinkProperties(android.net.vcn.VcnGatewayConnectionConfig gatewayConnectionConfig, android.net.IpSecManager.IpSecTunnelInterface tunnelIface, com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration childConfig, com.android.server.vcn.routeselection.UnderlyingNetworkRecord underlying, android.net.ipsec.ike.IkeSessionConnectionInfo ikeConnectionInfo) {
        android.net.ipsec.ike.IkeTunnelConnectionParams ikeTunnelParams = gatewayConnectionConfig.getTunnelConnectionParams();
        android.net.LinkProperties lp = new android.net.LinkProperties();
        lp.setInterfaceName(tunnelIface.getInterfaceName());
        for (android.net.LinkAddress addr : childConfig.getInternalAddresses()) {
            lp.addLinkAddress(addr);
        }
        for (java.net.InetAddress addr2 : childConfig.getInternalDnsServers()) {
            lp.addDnsServer(addr2);
        }
        lp.addRoute(new android.net.RouteInfo(new android.net.IpPrefix(java.net.Inet4Address.ANY, 0), null, null, 1));
        lp.addRoute(new android.net.RouteInfo(new android.net.IpPrefix(java.net.Inet6Address.ANY, 0), null, null, 1));
        int underlyingMtu = 0;
        if (underlying != null) {
            android.net.LinkProperties underlyingLp = underlying.linkProperties;
            lp.setTcpBufferSizes(underlyingLp.getTcpBufferSizes());
            underlyingMtu = underlyingLp.getMtu();
            if (underlyingMtu == 0 && underlyingLp.getInterfaceName() != null) {
                underlyingMtu = this.mDeps.getUnderlyingIfaceMtu(underlyingLp.getInterfaceName());
            }
        } else {
            android.util.Slog.wtf(TAG, "No underlying network while building link properties", new java.lang.IllegalStateException());
        }
        lp.setMtu(com.android.server.vcn.util.MtuUtils.getMtu(ikeTunnelParams.getTunnelModeChildSessionParams().getSaProposals(), gatewayConnectionConfig.getMaxMtu(), underlyingMtu, ikeConnectionInfo.getLocalAddress() instanceof java.net.Inet4Address));
        return lp;
    }

    private class IkeSessionCallbackImpl implements android.net.ipsec.ike.IkeSessionCallback {
        private final int mToken;

        IkeSessionCallbackImpl(int token) {
            this.mToken = token;
        }

        @Override // android.net.ipsec.ike.IkeSessionCallback
        public void onOpened(android.net.ipsec.ike.IkeSessionConfiguration ikeSessionConfig) {
            com.android.server.vcn.VcnGatewayConnection.this.logDbg("IkeOpened for token " + this.mToken);
            com.android.server.vcn.VcnGatewayConnection.this.ikeConnectionInfoChanged(this.mToken, ikeSessionConfig.getIkeSessionConnectionInfo());
        }

        @Override // android.net.ipsec.ike.IkeSessionCallback
        public void onClosed() {
            com.android.server.vcn.VcnGatewayConnection.this.logDbg("IkeClosed for token " + this.mToken);
            com.android.server.vcn.VcnGatewayConnection.this.sessionClosed(this.mToken, null);
        }

        public void onClosedExceptionally(android.net.ipsec.ike.exceptions.IkeException exception) {
            com.android.server.vcn.VcnGatewayConnection.this.logInfo("IkeClosedExceptionally for token " + this.mToken, exception);
            com.android.server.vcn.VcnGatewayConnection.this.sessionClosed(this.mToken, exception);
        }

        public void onError(android.net.ipsec.ike.exceptions.IkeProtocolException exception) {
            com.android.server.vcn.VcnGatewayConnection.this.logInfo("IkeError for token " + this.mToken, exception);
        }

        public void onIkeSessionConnectionInfoChanged(android.net.ipsec.ike.IkeSessionConnectionInfo connectionInfo) {
            com.android.server.vcn.VcnGatewayConnection.this.logDbg("onIkeSessionConnectionInfoChanged for token " + this.mToken);
            com.android.server.vcn.VcnGatewayConnection.this.ikeConnectionInfoChanged(this.mToken, connectionInfo);
        }
    }

    public class VcnChildSessionCallback implements android.net.ipsec.ike.ChildSessionCallback {
        private boolean mIsChildOpened;
        private final boolean mIsOpportunistic;
        private final int mToken;

        VcnChildSessionCallback(com.android.server.vcn.VcnGatewayConnection this$0, int token) {
            this(token, false);
        }

        VcnChildSessionCallback(int token, boolean isOpportunistic) {
            this.mIsChildOpened = false;
            this.mToken = token;
            this.mIsOpportunistic = isOpportunistic;
        }

        void onOpened(com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration childConfig) {
            com.android.server.vcn.VcnGatewayConnection.this.logDbg("ChildOpened for token " + this.mToken);
            if (this.mIsOpportunistic) {
                com.android.server.vcn.VcnGatewayConnection.this.logDbg("ChildOpened for opportunistic child; suppressing event message");
                this.mIsChildOpened = true;
            } else {
                com.android.server.vcn.VcnGatewayConnection.this.childOpened(this.mToken, childConfig);
            }
        }

        @Override // android.net.ipsec.ike.ChildSessionCallback
        public void onOpened(android.net.ipsec.ike.ChildSessionConfiguration childConfig) {
            onOpened(new com.android.server.vcn.VcnGatewayConnection.VcnChildSessionConfiguration(childConfig));
        }

        @Override // android.net.ipsec.ike.ChildSessionCallback
        public void onClosed() {
            com.android.server.vcn.VcnGatewayConnection.this.logDbg("ChildClosed for token " + this.mToken);
            if (this.mIsOpportunistic && !this.mIsChildOpened) {
                com.android.server.vcn.VcnGatewayConnection.this.logDbg("ChildClosed for unopened opportunistic child; ignoring");
            } else {
                com.android.server.vcn.VcnGatewayConnection.this.sessionLost(this.mToken, null);
            }
        }

        public void onClosedExceptionally(android.net.ipsec.ike.exceptions.IkeException exception) {
            com.android.server.vcn.VcnGatewayConnection.this.logInfo("ChildClosedExceptionally for token " + this.mToken, exception);
            if (this.mIsOpportunistic && !this.mIsChildOpened) {
                com.android.server.vcn.VcnGatewayConnection.this.logInfo("ChildClosedExceptionally for unopened opportunistic child; ignoring");
            } else {
                com.android.server.vcn.VcnGatewayConnection.this.sessionLost(this.mToken, exception);
            }
        }

        @Override // android.net.ipsec.ike.ChildSessionCallback
        public void onIpSecTransformCreated(android.net.IpSecTransform transform, int direction) {
            com.android.server.vcn.VcnGatewayConnection.this.logDbg("ChildTransformCreated; Direction: " + direction + "; token " + this.mToken);
            com.android.server.vcn.VcnGatewayConnection.this.childTransformCreated(this.mToken, transform, direction);
        }

        public void onIpSecTransformsMigrated(android.net.IpSecTransform inIpSecTransform, android.net.IpSecTransform outIpSecTransform) {
            com.android.server.vcn.VcnGatewayConnection.this.logDbg("ChildTransformsMigrated; token " + this.mToken);
            com.android.server.vcn.VcnGatewayConnection.this.migrationCompleted(this.mToken, inIpSecTransform, outIpSecTransform);
        }

        @Override // android.net.ipsec.ike.ChildSessionCallback
        public void onIpSecTransformDeleted(android.net.IpSecTransform transform, int direction) {
            com.android.server.vcn.VcnGatewayConnection.this.logDbg("ChildTransformDeleted; Direction: " + direction + "; for token " + this.mToken);
        }
    }

    public java.lang.String getLogPrefix() {
        return "(" + com.android.server.vcn.util.LogUtils.getHashedSubscriptionGroup(this.mSubscriptionGroup) + "-" + this.mConnectionConfig.getGatewayConnectionName() + "-" + java.lang.System.identityHashCode(this) + ") ";
    }

    private java.lang.String getTagLogPrefix() {
        return "[ " + TAG + " " + getLogPrefix() + "]";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logVdbg(java.lang.String msg) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logDbg(java.lang.String msg) {
        android.util.Slog.d(TAG, getLogPrefix() + msg);
    }

    private void logDbg(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.d(TAG, getLogPrefix() + msg, tr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logInfo(java.lang.String msg) {
        android.util.Slog.i(TAG, getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[INFO] " + getTagLogPrefix() + msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logInfo(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.i(TAG, getLogPrefix() + msg, tr);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[INFO] " + getTagLogPrefix() + msg + tr);
    }

    private void logWarn(java.lang.String msg) {
        android.util.Slog.w(TAG, getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[WARN] " + getTagLogPrefix() + msg);
    }

    private void logWarn(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.w(TAG, getLogPrefix() + msg, tr);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[WARN] " + getTagLogPrefix() + msg + tr);
    }

    private void logErr(java.lang.String msg) {
        android.util.Slog.e(TAG, getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[ERR ] " + getTagLogPrefix() + msg);
    }

    private void logErr(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.e(TAG, getLogPrefix() + msg, tr);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[ERR ] " + getTagLogPrefix() + msg + tr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logWtf(java.lang.String msg) {
        android.util.Slog.wtf(TAG, getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[WTF ] " + msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logWtf(java.lang.String msg, java.lang.Throwable tr) {
        android.util.Slog.wtf(TAG, getLogPrefix() + msg, tr);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[WTF ] " + msg + tr);
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        java.lang.String simpleName;
        this.mVcnContext.ensureRunningOnLooperThread();
        pw.println("VcnGatewayConnection (" + this.mConnectionConfig.getGatewayConnectionName() + "):");
        pw.increaseIndent();
        java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("Current state: ");
        if (getCurrentState() == null) {
            simpleName = null;
        } else {
            simpleName = getCurrentState().getClass().getSimpleName();
        }
        pw.println(sbAppend.append(simpleName).toString());
        pw.println("mIsQuitting: " + this.mIsQuitting.getValue());
        pw.println("mIsInSafeMode: " + this.mIsInSafeMode);
        pw.println("mCurrentToken: " + this.mCurrentToken);
        pw.println("mFailedAttempts: " + this.mFailedAttempts);
        pw.println("mNetworkAgent.getNetwork(): " + (this.mNetworkAgent != null ? this.mNetworkAgent.getNetwork() : null));
        pw.println();
        this.mUnderlyingNetworkController.dump(pw);
        pw.println();
        if (this.mIkeSession == null) {
            pw.println("mIkeSession: null");
        } else {
            pw.println("mIkeSession:");
            try {
                this.mIkeSession.dump(pw);
            } catch (java.lang.Exception e) {
                android.util.Slog.wtf(TAG, "Failed to dump IkeSession: " + e);
            }
        }
        pw.decreaseIndent();
    }

    void setTunnelInterface(android.net.IpSecManager.IpSecTunnelInterface tunnelIface) {
        this.mTunnelIface = tunnelIface;
    }

    com.android.server.vcn.routeselection.UnderlyingNetworkController.UnderlyingNetworkControllerCallback getUnderlyingNetworkControllerCallback() {
        return this.mUnderlyingNetworkControllerCallback;
    }

    android.net.ConnectivityDiagnosticsManager.ConnectivityDiagnosticsCallback getConnectivityDiagnosticsCallback() {
        return this.mConnectivityDiagnosticsCallback;
    }

    com.android.server.vcn.routeselection.UnderlyingNetworkRecord getUnderlyingNetwork() {
        return this.mUnderlying;
    }

    void setUnderlyingNetwork(com.android.server.vcn.routeselection.UnderlyingNetworkRecord record) {
        this.mUnderlying = record;
    }

    android.net.ipsec.ike.IkeSessionConnectionInfo getIkeConnectionInfo() {
        return this.mIkeConnectionInfo;
    }

    boolean isQuitting() {
        return this.mIsQuitting.getValue();
    }

    void setQuitting() {
        this.mIsQuitting.setTrue();
    }

    com.android.server.vcn.VcnGatewayConnection.VcnIkeSession getIkeSession() {
        return this.mIkeSession;
    }

    void setIkeSession(com.android.server.vcn.VcnGatewayConnection.VcnIkeSession session) {
        this.mIkeSession = session;
    }

    com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent getNetworkAgent() {
        return this.mNetworkAgent;
    }

    void setNetworkAgent(com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent networkAgent) {
        this.mNetworkAgent = networkAgent;
    }

    void sendDisconnectRequestedAndAcquireWakelock(java.lang.String reason, boolean shouldQuit) {
        sendMessageAndAcquireWakeLock(7, Integer.MIN_VALUE, new com.android.server.vcn.VcnGatewayConnection.EventDisconnectRequestedInfo(reason, shouldQuit));
    }

    private android.net.ipsec.ike.IkeSessionParams buildIkeParams(android.net.Network network) {
        android.net.ipsec.ike.IkeTunnelConnectionParams ikeTunnelConnectionParams = this.mConnectionConfig.getTunnelConnectionParams();
        android.net.ipsec.ike.IkeSessionParams.Builder builder = new android.net.ipsec.ike.IkeSessionParams.Builder(ikeTunnelConnectionParams.getIkeSessionParams());
        builder.setNetwork(network);
        return builder.build();
    }

    private android.net.ipsec.ike.ChildSessionParams buildChildParams() {
        return this.mConnectionConfig.getTunnelConnectionParams().getTunnelModeChildSessionParams();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.net.ipsec.ike.ChildSessionParams buildOpportunisticChildParams() {
        android.net.ipsec.ike.ChildSessionParams baseParams = this.mConnectionConfig.getTunnelConnectionParams().getTunnelModeChildSessionParams();
        android.net.ipsec.ike.TunnelModeChildSessionParams.Builder builder = new android.net.ipsec.ike.TunnelModeChildSessionParams.Builder();
        for (android.net.ipsec.ike.ChildSaProposal proposal : baseParams.getChildSaProposals()) {
            builder.addChildSaProposal(proposal);
        }
        for (android.net.ipsec.ike.IkeTrafficSelector inboundSelector : baseParams.getInboundTrafficSelectors()) {
            builder.addInboundTrafficSelectors(inboundSelector);
        }
        for (android.net.ipsec.ike.IkeTrafficSelector outboundSelector : baseParams.getOutboundTrafficSelectors()) {
            builder.addOutboundTrafficSelectors(outboundSelector);
        }
        builder.setLifetimeSeconds(baseParams.getHardLifetimeSeconds(), baseParams.getSoftLifetimeSeconds());
        return builder.build();
    }

    com.android.server.vcn.VcnGatewayConnection.VcnIkeSession buildIkeSession(android.net.Network network) {
        int token = this.mCurrentToken + 1;
        this.mCurrentToken = token;
        return this.mDeps.newIkeSession(this.mVcnContext, buildIkeParams(network), buildChildParams(), new com.android.server.vcn.VcnGatewayConnection.IkeSessionCallbackImpl(token), new com.android.server.vcn.VcnGatewayConnection.VcnChildSessionCallback(this, token));
    }

    public static class Dependencies {
        public com.android.server.vcn.routeselection.UnderlyingNetworkController newUnderlyingNetworkController(com.android.server.vcn.VcnContext vcnContext, android.net.vcn.VcnGatewayConnectionConfig connectionConfig, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, com.android.server.vcn.routeselection.UnderlyingNetworkController.UnderlyingNetworkControllerCallback callback) {
            return new com.android.server.vcn.routeselection.UnderlyingNetworkController(vcnContext, connectionConfig, subscriptionGroup, snapshot, callback);
        }

        public com.android.server.vcn.VcnGatewayConnection.VcnIkeSession newIkeSession(com.android.server.vcn.VcnContext vcnContext, android.net.ipsec.ike.IkeSessionParams ikeSessionParams, android.net.ipsec.ike.ChildSessionParams childSessionParams, android.net.ipsec.ike.IkeSessionCallback ikeSessionCallback, android.net.ipsec.ike.ChildSessionCallback childSessionCallback) {
            return new com.android.server.vcn.VcnGatewayConnection.VcnIkeSession(vcnContext, ikeSessionParams, childSessionParams, ikeSessionCallback, childSessionCallback);
        }

        public com.android.server.vcn.VcnGatewayConnection.VcnWakeLock newWakeLock(android.content.Context context, int wakeLockFlag, java.lang.String wakeLockTag) {
            return new com.android.server.vcn.VcnGatewayConnection.VcnWakeLock(context, wakeLockFlag, wakeLockTag);
        }

        public com.android.internal.util.WakeupMessage newWakeupMessage(com.android.server.vcn.VcnContext vcnContext, android.os.Handler handler, java.lang.String tag, java.lang.Runnable runnable) {
            return new com.android.internal.util.WakeupMessage(vcnContext.getContext(), handler, tag, runnable);
        }

        public com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent newNetworkAgent(com.android.server.vcn.VcnContext vcnContext, java.lang.String tag, android.net.NetworkCapabilities caps, android.net.LinkProperties lp, android.net.NetworkScore score, android.net.NetworkAgentConfig nac, android.net.NetworkProvider provider, java.util.function.Consumer<com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent> networkUnwantedCallback, java.util.function.Consumer<java.lang.Integer> validationStatusCallback) {
            return new com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent(vcnContext, tag, caps, lp, score, nac, provider, networkUnwantedCallback, validationStatusCallback);
        }

        public boolean isAirplaneModeOn(com.android.server.vcn.VcnContext vcnContext) {
            return android.provider.Settings.Global.getInt(vcnContext.getContext().getContentResolver(), "airplane_mode_on", 0) != 0;
        }

        public long getElapsedRealTime() {
            return android.os.SystemClock.elapsedRealtime();
        }

        public int getUnderlyingIfaceMtu(java.lang.String ifaceName) {
            try {
                java.net.NetworkInterface underlyingIface = java.net.NetworkInterface.getByName(ifaceName);
                if (underlyingIface == null) {
                    return 0;
                }
                return underlyingIface.getMTU();
            } catch (java.io.IOException e) {
                android.util.Slog.d(com.android.server.vcn.VcnGatewayConnection.TAG, "Could not get MTU of underlying network", e);
                return 0;
            }
        }

        public int getParallelTunnelCount(com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, android.os.ParcelUuid subGrp) {
            com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig = snapshot.getCarrierConfigForSubGrp(subGrp);
            int result = 1;
            if (carrierConfig != null) {
                result = carrierConfig.getInt("vcn_tunnel_aggregation_sa_count_max", 1);
            }
            return java.lang.Math.max(1, result);
        }
    }

    public static class VcnChildSessionConfiguration {
        private final android.net.ipsec.ike.ChildSessionConfiguration mChildConfig;

        public VcnChildSessionConfiguration(android.net.ipsec.ike.ChildSessionConfiguration childConfig) {
            this.mChildConfig = childConfig;
        }

        public java.util.List<android.net.LinkAddress> getInternalAddresses() {
            return this.mChildConfig.getInternalAddresses();
        }

        public java.util.List<java.net.InetAddress> getInternalDnsServers() {
            return this.mChildConfig.getInternalDnsServers();
        }
    }

    public static class VcnIkeSession {
        private final android.net.ipsec.ike.IkeSession mImpl;

        public VcnIkeSession(com.android.server.vcn.VcnContext vcnContext, android.net.ipsec.ike.IkeSessionParams ikeSessionParams, android.net.ipsec.ike.ChildSessionParams childSessionParams, android.net.ipsec.ike.IkeSessionCallback ikeSessionCallback, android.net.ipsec.ike.ChildSessionCallback childSessionCallback) {
            this.mImpl = new android.net.ipsec.ike.IkeSession(vcnContext.getContext(), ikeSessionParams, childSessionParams, new android.os.HandlerExecutor(new android.os.Handler(vcnContext.getLooper())), ikeSessionCallback, childSessionCallback);
        }

        public void openChildSession(android.net.ipsec.ike.ChildSessionParams childSessionParams, android.net.ipsec.ike.ChildSessionCallback childSessionCallback) {
            this.mImpl.openChildSession(childSessionParams, childSessionCallback);
        }

        public void closeChildSession(android.net.ipsec.ike.ChildSessionCallback childSessionCallback) {
            this.mImpl.closeChildSession(childSessionCallback);
        }

        public void close() {
            this.mImpl.close();
        }

        public void kill() {
            this.mImpl.kill();
        }

        public void setNetwork(android.net.Network network) {
            this.mImpl.setNetwork(network);
        }

        public void dump(com.android.internal.util.IndentingPrintWriter pw) {
            this.mImpl.dump(pw);
        }
    }

    public static class VcnWakeLock {
        private final android.os.PowerManager.WakeLock mImpl;

        public VcnWakeLock(android.content.Context context, int flags, java.lang.String tag) {
            android.os.PowerManager powerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
            this.mImpl = powerManager.newWakeLock(flags, tag);
            this.mImpl.setReferenceCounted(false);
        }

        public synchronized void acquire() {
            this.mImpl.acquire();
        }

        public synchronized void release() {
            this.mImpl.release();
        }
    }

    public static class VcnNetworkAgent {
        private final android.net.NetworkAgent mImpl;

        public VcnNetworkAgent(com.android.server.vcn.VcnContext vcnContext, java.lang.String tag, android.net.NetworkCapabilities caps, android.net.LinkProperties lp, android.net.NetworkScore score, android.net.NetworkAgentConfig nac, android.net.NetworkProvider provider, final java.util.function.Consumer<com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent> networkUnwantedCallback, final java.util.function.Consumer<java.lang.Integer> validationStatusCallback) {
            this.mImpl = new android.net.NetworkAgent(vcnContext.getContext(), vcnContext.getLooper(), tag, caps, lp, score, nac, provider) { // from class: com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent.1
                public void onNetworkUnwanted() {
                    networkUnwantedCallback.accept(com.android.server.vcn.VcnGatewayConnection.VcnNetworkAgent.this);
                }

                public void onValidationStatus(int status, android.net.Uri redirectUri) {
                    validationStatusCallback.accept(java.lang.Integer.valueOf(status));
                }
            };
        }

        public void register() {
            this.mImpl.register();
        }

        public void markConnected() {
            this.mImpl.markConnected();
        }

        public void unregister() {
            this.mImpl.unregister();
        }

        public void sendNetworkCapabilities(android.net.NetworkCapabilities caps) {
            this.mImpl.sendNetworkCapabilities(caps);
        }

        public void sendLinkProperties(android.net.LinkProperties lp) {
            this.mImpl.sendLinkProperties(lp);
        }

        public void setUnderlyingNetworks(java.util.List<android.net.Network> underlyingNetworks) {
            this.mImpl.setUnderlyingNetworks(underlyingNetworks);
        }

        public android.net.Network getNetwork() {
            return this.mImpl.getNetwork();
        }
    }
}
