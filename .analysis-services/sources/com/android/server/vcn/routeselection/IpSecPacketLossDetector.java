package com.android.server.vcn.routeselection;

/* JADX INFO: loaded from: classes3.dex */
public class IpSecPacketLossDetector extends com.android.server.vcn.routeselection.NetworkMetricMonitor {
    private static final int IPSEC_PACKET_LOSS_PERCENT_THRESHOLD_DEFAULT = 12;
    static final int IPSEC_PACKET_LOSS_PERCENT_THRESHOLD_DISABLE_DETECTOR = -1;
    private static final int MAX_SEQ_NUM_INCREASE_DEFAULT_DISABLED = -1;
    static final int MIN_VALID_EXPECTED_RX_PACKET_NUM = 10;
    private static final int PACKET_LOSS_PERCENT_UNAVAILABLE = -1;
    private static final int PACKET_LOSS_RATE_INVALID = 1;
    private static final int PACKET_LOSS_RATE_VALID = 0;
    private static final int PACKET_LOSS_UNUSUAL_SEQ_NUM_LEAP = 2;
    private static final int POLL_IPSEC_STATE_INTERVAL_SECONDS_DEFAULT = 20;
    private static final java.lang.String TAG = com.android.server.vcn.routeselection.IpSecPacketLossDetector.class.getSimpleName();
    private final java.lang.Object mCancellationToken;
    private final android.net.ConnectivityManager mConnectivityManager;
    private final android.os.Handler mHandler;
    private com.android.server.vcn.routeselection.NetworkMetricMonitor.IpSecTransformWrapper mInboundTransform;
    private android.net.IpSecTransformState mLastIpSecTransformState;
    private int mMaxSeqNumIncreasePerSecond;
    private final com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculator mPacketLossCalculator;
    private int mPacketLossRatePercentThreshold;
    private long mPollIpSecStateIntervalMs;
    private final android.os.PowerManager mPowerManager;

    @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE_USE})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface PacketLossResultType {
    }

    public IpSecPacketLossDetector(com.android.server.vcn.VcnContext vcnContext, android.net.Network network, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig, com.android.server.vcn.routeselection.NetworkMetricMonitor.NetworkMetricMonitorCallback callback, com.android.server.vcn.routeselection.IpSecPacketLossDetector.Dependencies deps) throws java.lang.IllegalAccessException {
        super(vcnContext, network, carrierConfig, callback);
        this.mCancellationToken = new java.lang.Object();
        java.util.Objects.requireNonNull(deps, "Missing deps");
        if (!vcnContext.isFlagIpSecTransformStateEnabled()) {
            logWtf("ipsecTransformState flag disabled");
            throw new java.lang.IllegalAccessException("ipsecTransformState flag disabled");
        }
        this.mHandler = new android.os.Handler(getVcnContext().getLooper());
        this.mPowerManager = (android.os.PowerManager) getVcnContext().getContext().getSystemService(android.os.PowerManager.class);
        this.mConnectivityManager = (android.net.ConnectivityManager) getVcnContext().getContext().getSystemService(android.net.ConnectivityManager.class);
        this.mPacketLossCalculator = deps.getPacketLossCalculator();
        this.mPollIpSecStateIntervalMs = getPollIpSecStateIntervalMs(carrierConfig);
        this.mPacketLossRatePercentThreshold = getPacketLossRatePercentThreshold(carrierConfig);
        this.mMaxSeqNumIncreasePerSecond = getMaxSeqNumIncreasePerSecond(carrierConfig);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        getVcnContext().getContext().registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.vcn.routeselection.IpSecPacketLossDetector.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if ("android.os.action.DEVICE_IDLE_MODE_CHANGED".equals(intent.getAction()) && com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.mPowerManager.isDeviceIdleMode()) {
                    com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.mLastIpSecTransformState = null;
                }
            }
        }, intentFilter, null, this.mHandler);
    }

    public IpSecPacketLossDetector(com.android.server.vcn.VcnContext vcnContext, android.net.Network network, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig, com.android.server.vcn.routeselection.NetworkMetricMonitor.NetworkMetricMonitorCallback callback) throws java.lang.IllegalAccessException {
        this(vcnContext, network, carrierConfig, callback, new com.android.server.vcn.routeselection.IpSecPacketLossDetector.Dependencies());
    }

    public static class Dependencies {
        public com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculator getPacketLossCalculator() {
            return new com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculator();
        }
    }

    private static long getPollIpSecStateIntervalMs(com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        int seconds;
        if (carrierConfig != null) {
            seconds = carrierConfig.getInt("vcn_network_selection_poll_ipsec_state_interval_seconds", 20);
        } else {
            seconds = 20;
        }
        return java.util.concurrent.TimeUnit.SECONDS.toMillis(seconds);
    }

    private static int getPacketLossRatePercentThreshold(com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        if (carrierConfig != null) {
            return carrierConfig.getInt("vcn_network_selection_ipsec_packet_loss_percent_threshold", 12);
        }
        return 12;
    }

    static int getMaxSeqNumIncreasePerSecond(com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        int maxSeqNumIncrease = -1;
        if (android.net.vcn.Flags.handleSeqNumLeap() && carrierConfig != null) {
            maxSeqNumIncrease = carrierConfig.getInt("vcn_network_selection_max_seq_num_increase_per_second", -1);
        }
        if (maxSeqNumIncrease < -1) {
            logE(TAG, "Invalid value of MAX_SEQ_NUM_INCREASE_PER_SECOND_KEY " + maxSeqNumIncrease);
            return -1;
        }
        return maxSeqNumIncrease;
    }

    @Override // com.android.server.vcn.routeselection.NetworkMetricMonitor
    protected void onSelectedUnderlyingNetworkChanged() {
        if (!isSelectedUnderlyingNetwork()) {
            this.mInboundTransform = null;
            stop();
        }
    }

    @Override // com.android.server.vcn.routeselection.NetworkMetricMonitor
    public void setInboundTransformInternal(com.android.server.vcn.routeselection.NetworkMetricMonitor.IpSecTransformWrapper inboundTransform) {
        java.util.Objects.requireNonNull(inboundTransform, "inboundTransform is null");
        if (java.util.Objects.equals(inboundTransform, this.mInboundTransform)) {
            return;
        }
        if (!isSelectedUnderlyingNetwork()) {
            logWtf("setInboundTransform called but network not selected");
            return;
        }
        this.mInboundTransform = inboundTransform;
        if (!android.net.vcn.Flags.allowDisableIpsecLossDetector() || canStart()) {
            start();
        }
    }

    @Override // com.android.server.vcn.routeselection.NetworkMetricMonitor
    public void setCarrierConfig(com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        this.mPollIpSecStateIntervalMs = getPollIpSecStateIntervalMs(carrierConfig);
        if (android.net.vcn.Flags.handleSeqNumLeap()) {
            this.mPacketLossRatePercentThreshold = getPacketLossRatePercentThreshold(carrierConfig);
            this.mMaxSeqNumIncreasePerSecond = getMaxSeqNumIncreasePerSecond(carrierConfig);
        }
        if (android.net.vcn.Flags.allowDisableIpsecLossDetector() && canStart() != isStarted()) {
            if (canStart()) {
                start();
            } else {
                stop();
            }
        }
    }

    @Override // com.android.server.vcn.routeselection.NetworkMetricMonitor
    public void onLinkPropertiesOrCapabilitiesChanged() {
        if (isStarted()) {
            reschedulePolling();
        }
    }

    private void reschedulePolling() {
        this.mHandler.removeCallbacksAndEqualMessages(this.mCancellationToken);
        this.mHandler.postDelayed(new com.android.server.vcn.routeselection.IpSecPacketLossDetector.PollIpSecStateRunnable(), this.mCancellationToken, 0L);
    }

    private boolean canStart() {
        return (this.mInboundTransform == null || this.mPacketLossRatePercentThreshold == -1) ? false : true;
    }

    @Override // com.android.server.vcn.routeselection.NetworkMetricMonitor
    protected void start() {
        super.start();
        clearTransformStateAndPollingEvents();
        this.mHandler.postDelayed(new com.android.server.vcn.routeselection.IpSecPacketLossDetector.PollIpSecStateRunnable(), this.mCancellationToken, 0L);
    }

    @Override // com.android.server.vcn.routeselection.NetworkMetricMonitor
    public void stop() {
        super.stop();
        clearTransformStateAndPollingEvents();
    }

    private void clearTransformStateAndPollingEvents() {
        this.mHandler.removeCallbacksAndEqualMessages(this.mCancellationToken);
        this.mLastIpSecTransformState = null;
    }

    @Override // com.android.server.vcn.routeselection.NetworkMetricMonitor, java.lang.AutoCloseable
    public void close() {
        super.close();
        if (this.mInboundTransform != null) {
            this.mInboundTransform.close();
        }
    }

    public android.net.IpSecTransformState getLastTransformState() {
        return this.mLastIpSecTransformState;
    }

    public com.android.server.vcn.routeselection.NetworkMetricMonitor.IpSecTransformWrapper getInboundTransformInternal() {
        return this.mInboundTransform;
    }

    private class PollIpSecStateRunnable implements java.lang.Runnable {
        private PollIpSecStateRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.isStarted()) {
                com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.logWtf("Monitor stopped but PollIpSecStateRunnable not removed from Handler");
            } else {
                com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.getInboundTransformInternal().requestIpSecTransformState(new android.os.HandlerExecutor(com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.mHandler), new com.android.server.vcn.routeselection.IpSecPacketLossDetector.IpSecTransformStateReceiver());
                com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.mHandler.postDelayed(com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.new PollIpSecStateRunnable(), com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.mCancellationToken, com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.mPollIpSecStateIntervalMs);
            }
        }
    }

    private class IpSecTransformStateReceiver implements android.os.OutcomeReceiver<android.net.IpSecTransformState, java.lang.RuntimeException> {
        private IpSecTransformStateReceiver() {
        }

        @Override // android.os.OutcomeReceiver
        public void onResult(android.net.IpSecTransformState state) {
            com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.getVcnContext().ensureRunningOnLooperThread();
            if (!com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.isStarted()) {
                return;
            }
            com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.onIpSecTransformStateReceived(state);
        }

        @Override // android.os.OutcomeReceiver
        public void onError(java.lang.RuntimeException error) {
            com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.getVcnContext().ensureRunningOnLooperThread();
            com.android.server.vcn.routeselection.IpSecPacketLossDetector.this.logW("TransformStateReceiver#onError " + error.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onIpSecTransformStateReceived(android.net.IpSecTransformState state) {
        if (this.mLastIpSecTransformState == null) {
            this.mLastIpSecTransformState = state;
            return;
        }
        com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult calculateResult = this.mPacketLossCalculator.getPacketLossRatePercentage(this.mLastIpSecTransformState, state, this.mMaxSeqNumIncreasePerSecond, getLogPrefix());
        if (calculateResult.getResultType() == 1) {
            return;
        }
        java.lang.String logMsg = "calculateResult: " + calculateResult + "% in the past " + (state.getTimestampMillis() - this.mLastIpSecTransformState.getTimestampMillis()) + "ms";
        this.mLastIpSecTransformState = state;
        if (calculateResult.getPacketLossRatePercent() < this.mPacketLossRatePercentThreshold) {
            logV(logMsg);
            onValidationResultReceivedInternal(false);
            return;
        }
        logInfo(logMsg);
        if (calculateResult.getResultType() == 0) {
            onValidationResultReceivedInternal(true);
        }
        if (android.net.vcn.Flags.validateNetworkOnIpsecLoss()) {
            this.mConnectivityManager.reportNetworkConnectivity(getNetwork(), false);
        }
    }

    public static class PacketLossCalculator {
        public com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult getPacketLossRatePercentage(android.net.IpSecTransformState oldState, android.net.IpSecTransformState newState, int maxSeqNumIncreasePerSecond, java.lang.String logPrefix) {
            com.android.server.vcn.routeselection.IpSecPacketLossDetector.logVIpSecTransform("oldState", oldState, logPrefix);
            com.android.server.vcn.routeselection.IpSecPacketLossDetector.logVIpSecTransform("newState", newState, logPrefix);
            int replayWindowSize = oldState.getReplayBitmap().length * 8;
            long oldSeqHi = oldState.getRxHighestSequenceNumber();
            long oldSeqLow = java.lang.Math.max(0L, (oldSeqHi - ((long) replayWindowSize)) + 1);
            long newSeqHi = newState.getRxHighestSequenceNumber();
            long newSeqLow = java.lang.Math.max(0L, (newSeqHi - ((long) replayWindowSize)) + 1);
            if (oldSeqHi == newSeqHi || newSeqHi < replayWindowSize) {
                return com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult.invalid();
            }
            boolean isUnusualSeqNumLeap = false;
            if (android.net.vcn.Flags.handleSeqNumLeap() && maxSeqNumIncreasePerSecond != -1) {
                long timeDiffMillis = newState.getTimestampMillis() - oldState.getTimestampMillis();
                long maxSeqNumIncrease = (((long) maxSeqNumIncreasePerSecond) * timeDiffMillis) / 1000;
                if (maxSeqNumIncrease >= 0 && newSeqHi - oldSeqHi >= maxSeqNumIncrease) {
                    isUnusualSeqNumLeap = true;
                }
            }
            long newExpectedPktCnt = com.android.server.vcn.routeselection.IpSecPacketLossDetector.getPacketCntInReplayWindow(newState) + newSeqLow;
            long oldExpectedPktCnt = oldSeqLow + com.android.server.vcn.routeselection.IpSecPacketLossDetector.getPacketCntInReplayWindow(oldState);
            long expectedPktCntDiff = newExpectedPktCnt - oldExpectedPktCnt;
            long newExpectedPktCnt2 = newState.getPacketCount() - oldState.getPacketCount();
            com.android.server.vcn.routeselection.NetworkMetricMonitor.logV(com.android.server.vcn.routeselection.IpSecPacketLossDetector.TAG, logPrefix + " expectedPktCntDiff: " + expectedPktCntDiff + " actualPktCntDiff: " + newExpectedPktCnt2);
            if (android.net.vcn.Flags.handleSeqNumLeap() && expectedPktCntDiff < 10) {
                return com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult.invalid();
            }
            if (expectedPktCntDiff < 0 || expectedPktCntDiff == 0 || newExpectedPktCnt2 < 0 || newExpectedPktCnt2 > expectedPktCntDiff) {
                com.android.server.vcn.routeselection.NetworkMetricMonitor.logWtf(com.android.server.vcn.routeselection.IpSecPacketLossDetector.TAG, "Impossible values for expectedPktCntDiff or actualPktCntDiff");
                return com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult.invalid();
            }
            int percent = 100 - ((int) ((100 * newExpectedPktCnt2) / expectedPktCntDiff));
            if (isUnusualSeqNumLeap) {
                return com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult.unusualSeqNumLeap(percent);
            }
            return com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult.valid(percent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logVIpSecTransform(java.lang.String transformTag, android.net.IpSecTransformState state, java.lang.String logPrefix) {
        java.lang.String stateString = " seqNo: " + state.getRxHighestSequenceNumber() + " | pktCnt: " + state.getPacketCount() + " | pktCntInWindow: " + getPacketCntInReplayWindow(state);
        logV(TAG, logPrefix + " " + transformTag + stateString);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long getPacketCntInReplayWindow(android.net.IpSecTransformState state) {
        return java.util.BitSet.valueOf(state.getReplayBitmap()).cardinality();
    }

    public static class PacketLossCalculationResult {
        private final int mPacketLossRatePercent;
        private final int mResultType;

        private PacketLossCalculationResult(int type, int percent) {
            this.mResultType = type;
            this.mPacketLossRatePercent = percent;
        }

        public static com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult valid(int percent) {
            return new com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult(0, percent);
        }

        public static com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult invalid() {
            return new com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult(1, -1);
        }

        public static com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult unusualSeqNumLeap(int percent) {
            return new com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult(2, percent);
        }

        public int getResultType() {
            return this.mResultType;
        }

        public int getPacketLossRatePercent() {
            return this.mPacketLossRatePercent;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.mResultType), java.lang.Integer.valueOf(this.mPacketLossRatePercent));
        }

        public boolean equals(java.lang.Object other) {
            if (!(other instanceof com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult)) {
                return false;
            }
            com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult rhs = (com.android.server.vcn.routeselection.IpSecPacketLossDetector.PacketLossCalculationResult) other;
            return this.mResultType == rhs.mResultType && this.mPacketLossRatePercent == rhs.mPacketLossRatePercent;
        }

        public java.lang.String toString() {
            return "mResultType: " + this.mResultType + " | mPacketLossRatePercent: " + this.mPacketLossRatePercent;
        }
    }
}
