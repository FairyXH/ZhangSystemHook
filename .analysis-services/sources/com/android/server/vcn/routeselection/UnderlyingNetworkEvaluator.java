package com.android.server.vcn.routeselection;

/* JADX INFO: loaded from: classes3.dex */
public class UnderlyingNetworkEvaluator {
    private final java.lang.Object mCancellationToken;
    private final com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.Dependencies mDependencies;
    private final com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.NetworkEvaluatorCallback mEvaluatorCallback;
    private final android.os.Handler mHandler;
    private boolean mIsPenalized;
    private boolean mIsSelected;
    private final java.util.List<com.android.server.vcn.routeselection.NetworkMetricMonitor> mMetricMonitors;
    private final com.android.server.vcn.routeselection.UnderlyingNetworkRecord.Builder mNetworkRecordBuilder;
    private long mPenalizedTimeoutMs;
    private int mPriorityClass;
    private final com.android.server.vcn.VcnContext mVcnContext;
    private static final java.lang.String TAG = com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.class.getSimpleName();
    private static final int[] PENALTY_TIMEOUT_MINUTES_DEFAULT = {5};

    public interface NetworkEvaluatorCallback {
        void onEvaluationResultChanged();
    }

    public UnderlyingNetworkEvaluator(com.android.server.vcn.VcnContext vcnContext, android.net.Network network, java.util.List<android.net.vcn.VcnUnderlyingNetworkTemplate> underlyingNetworkTemplates, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot lastSnapshot, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig, com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.NetworkEvaluatorCallback evaluatorCallback, com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.Dependencies dependencies) {
        this.mCancellationToken = new java.lang.Object();
        this.mMetricMonitors = new java.util.ArrayList();
        this.mPriorityClass = -1;
        this.mVcnContext = (com.android.server.vcn.VcnContext) java.util.Objects.requireNonNull(vcnContext, "Missing vcnContext");
        this.mHandler = new android.os.Handler(this.mVcnContext.getLooper());
        this.mDependencies = (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.Dependencies) java.util.Objects.requireNonNull(dependencies, "Missing dependencies");
        this.mEvaluatorCallback = (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.NetworkEvaluatorCallback) java.util.Objects.requireNonNull(evaluatorCallback, "Missing deps");
        java.util.Objects.requireNonNull(underlyingNetworkTemplates, "Missing underlyingNetworkTemplates");
        java.util.Objects.requireNonNull(subscriptionGroup, "Missing subscriptionGroup");
        java.util.Objects.requireNonNull(lastSnapshot, "Missing lastSnapshot");
        this.mNetworkRecordBuilder = new com.android.server.vcn.routeselection.UnderlyingNetworkRecord.Builder((android.net.Network) java.util.Objects.requireNonNull(network, "Missing network"));
        this.mIsSelected = false;
        this.mIsPenalized = false;
        this.mPenalizedTimeoutMs = getPenaltyTimeoutMs(carrierConfig);
        updatePriorityClass(underlyingNetworkTemplates, subscriptionGroup, lastSnapshot, carrierConfig);
        if (isIpSecPacketLossDetectorEnabled()) {
            try {
                this.mMetricMonitors.add(this.mDependencies.newIpSecPacketLossDetector(this.mVcnContext, this.mNetworkRecordBuilder.getNetwork(), carrierConfig, new com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.MetricMonitorCallbackImpl()));
            } catch (java.lang.IllegalAccessException e) {
            }
        }
    }

    public UnderlyingNetworkEvaluator(com.android.server.vcn.VcnContext vcnContext, android.net.Network network, java.util.List<android.net.vcn.VcnUnderlyingNetworkTemplate> underlyingNetworkTemplates, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot lastSnapshot, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig, com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.NetworkEvaluatorCallback evaluatorCallback) {
        this(vcnContext, network, underlyingNetworkTemplates, subscriptionGroup, lastSnapshot, carrierConfig, evaluatorCallback, new com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.Dependencies());
    }

    public static class Dependencies {
        public com.android.server.vcn.routeselection.IpSecPacketLossDetector newIpSecPacketLossDetector(com.android.server.vcn.VcnContext vcnContext, android.net.Network network, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig, com.android.server.vcn.routeselection.NetworkMetricMonitor.NetworkMetricMonitorCallback callback) throws java.lang.IllegalAccessException {
            return new com.android.server.vcn.routeselection.IpSecPacketLossDetector(vcnContext, network, carrierConfig, callback);
        }
    }

    private class MetricMonitorCallbackImpl implements com.android.server.vcn.routeselection.NetworkMetricMonitor.NetworkMetricMonitorCallback {
        private MetricMonitorCallbackImpl() {
        }

        @Override // com.android.server.vcn.routeselection.NetworkMetricMonitor.NetworkMetricMonitorCallback
        public void onValidationResultReceived() {
            com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.this.mVcnContext.ensureRunningOnLooperThread();
            com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.this.handleValidationResult();
        }
    }

    private void updatePriorityClass(java.util.List<android.net.vcn.VcnUnderlyingNetworkTemplate> underlyingNetworkTemplates, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot lastSnapshot, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        if (this.mNetworkRecordBuilder.isValid()) {
            this.mPriorityClass = com.android.server.vcn.routeselection.NetworkPriorityClassifier.calculatePriorityClass(this.mVcnContext, this.mNetworkRecordBuilder.build(), underlyingNetworkTemplates, subscriptionGroup, lastSnapshot, this.mIsSelected, carrierConfig);
        } else {
            this.mPriorityClass = -1;
        }
    }

    private boolean isIpSecPacketLossDetectorEnabled() {
        return isIpSecPacketLossDetectorEnabled(this.mVcnContext);
    }

    private static boolean isIpSecPacketLossDetectorEnabled(com.android.server.vcn.VcnContext vcnContext) {
        return vcnContext.isFlagIpSecTransformStateEnabled() && vcnContext.isFlagNetworkMetricMonitorEnabled();
    }

    public static java.util.Comparator<com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator> getComparator(final com.android.server.vcn.VcnContext vcnContext) {
        return new java.util.Comparator() { // from class: com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.lambda$getComparator$0(vcnContext, (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator) obj, (com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$getComparator$0(com.android.server.vcn.VcnContext vcnContext, com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator left, com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator right) {
        if (isIpSecPacketLossDetectorEnabled(vcnContext) && left.mIsPenalized != right.mIsPenalized) {
            return left.mIsPenalized ? 1 : -1;
        }
        int leftIndex = left.mPriorityClass;
        int rightIndex = right.mPriorityClass;
        if (leftIndex == rightIndex) {
            if (left.mIsSelected) {
                return -1;
            }
            if (right.mIsSelected) {
                return 1;
            }
        }
        return java.lang.Integer.compare(leftIndex, rightIndex);
    }

    private static long getPenaltyTimeoutMs(com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        int[] timeoutMinuteList;
        if (carrierConfig != null) {
            timeoutMinuteList = carrierConfig.getIntArray("vcn_network_selection_penalty_timeout_minutes_list", PENALTY_TIMEOUT_MINUTES_DEFAULT);
        } else {
            timeoutMinuteList = PENALTY_TIMEOUT_MINUTES_DEFAULT;
        }
        return java.util.concurrent.TimeUnit.MINUTES.toMillis(timeoutMinuteList[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleValidationResult() {
        boolean wasPenalized = this.mIsPenalized;
        this.mIsPenalized = false;
        for (com.android.server.vcn.routeselection.NetworkMetricMonitor monitor : this.mMetricMonitors) {
            this.mIsPenalized |= monitor.isValidationFailed();
        }
        if (wasPenalized == this.mIsPenalized) {
            return;
        }
        logInfo("#handleValidationResult: wasPenalized " + wasPenalized + " mIsPenalized " + this.mIsPenalized);
        if (this.mIsPenalized) {
            this.mHandler.postDelayed(new com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.ExitPenaltyBoxRunnable(), this.mCancellationToken, this.mPenalizedTimeoutMs);
        } else {
            this.mHandler.removeCallbacksAndEqualMessages(this.mCancellationToken);
        }
        this.mEvaluatorCallback.onEvaluationResultChanged();
    }

    public class ExitPenaltyBoxRunnable implements java.lang.Runnable {
        public ExitPenaltyBoxRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.this.mIsPenalized) {
                com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.this.logWtf("Evaluator not being penalized but ExitPenaltyBoxRunnable was scheduled");
            } else {
                com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.this.mIsPenalized = false;
                com.android.server.vcn.routeselection.UnderlyingNetworkEvaluator.this.mEvaluatorCallback.onEvaluationResultChanged();
            }
        }
    }

    public void setNetworkCapabilities(android.net.NetworkCapabilities nc, java.util.List<android.net.vcn.VcnUnderlyingNetworkTemplate> underlyingNetworkTemplates, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot lastSnapshot, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        this.mNetworkRecordBuilder.setNetworkCapabilities(nc);
        updatePriorityClass(underlyingNetworkTemplates, subscriptionGroup, lastSnapshot, carrierConfig);
        if (android.net.vcn.Flags.evaluateIpsecLossOnLpNcChange()) {
            for (com.android.server.vcn.routeselection.NetworkMetricMonitor monitor : this.mMetricMonitors) {
                monitor.onLinkPropertiesOrCapabilitiesChanged();
            }
        }
    }

    public void setLinkProperties(android.net.LinkProperties lp, java.util.List<android.net.vcn.VcnUnderlyingNetworkTemplate> underlyingNetworkTemplates, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot lastSnapshot, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        this.mNetworkRecordBuilder.setLinkProperties(lp);
        updatePriorityClass(underlyingNetworkTemplates, subscriptionGroup, lastSnapshot, carrierConfig);
        if (android.net.vcn.Flags.evaluateIpsecLossOnLpNcChange()) {
            for (com.android.server.vcn.routeselection.NetworkMetricMonitor monitor : this.mMetricMonitors) {
                monitor.onLinkPropertiesOrCapabilitiesChanged();
            }
        }
    }

    public void setIsBlocked(boolean isBlocked, java.util.List<android.net.vcn.VcnUnderlyingNetworkTemplate> underlyingNetworkTemplates, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot lastSnapshot, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        this.mNetworkRecordBuilder.setIsBlocked(isBlocked);
        updatePriorityClass(underlyingNetworkTemplates, subscriptionGroup, lastSnapshot, carrierConfig);
    }

    public void setIsSelected(boolean isSelected, java.util.List<android.net.vcn.VcnUnderlyingNetworkTemplate> underlyingNetworkTemplates, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot lastSnapshot, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        this.mIsSelected = isSelected;
        updatePriorityClass(underlyingNetworkTemplates, subscriptionGroup, lastSnapshot, carrierConfig);
        for (com.android.server.vcn.routeselection.NetworkMetricMonitor monitor : this.mMetricMonitors) {
            monitor.setIsSelectedUnderlyingNetwork(isSelected);
        }
    }

    public void reevaluate(java.util.List<android.net.vcn.VcnUnderlyingNetworkTemplate> underlyingNetworkTemplates, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot lastSnapshot, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        updatePriorityClass(underlyingNetworkTemplates, subscriptionGroup, lastSnapshot, carrierConfig);
        this.mPenalizedTimeoutMs = getPenaltyTimeoutMs(carrierConfig);
        for (com.android.server.vcn.routeselection.NetworkMetricMonitor monitor : this.mMetricMonitors) {
            monitor.setCarrierConfig(carrierConfig);
        }
    }

    public void setInboundTransform(android.net.IpSecTransform transform) {
        if (!this.mIsSelected) {
            logWtf("setInboundTransform on an unselected evaluator");
            return;
        }
        for (com.android.server.vcn.routeselection.NetworkMetricMonitor monitor : this.mMetricMonitors) {
            monitor.setInboundTransform(transform);
        }
    }

    public void close() {
        this.mHandler.removeCallbacksAndEqualMessages(this.mCancellationToken);
        for (com.android.server.vcn.routeselection.NetworkMetricMonitor monitor : this.mMetricMonitors) {
            monitor.close();
        }
    }

    public boolean isValid() {
        return this.mNetworkRecordBuilder.isValid();
    }

    public android.net.Network getNetwork() {
        return this.mNetworkRecordBuilder.getNetwork();
    }

    public com.android.server.vcn.routeselection.UnderlyingNetworkRecord getNetworkRecord() {
        return this.mNetworkRecordBuilder.build();
    }

    public int getPriorityClass() {
        return this.mPriorityClass;
    }

    public boolean isPenalized() {
        return this.mIsPenalized;
    }

    public void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("UnderlyingNetworkEvaluator:");
        pw.increaseIndent();
        if (this.mNetworkRecordBuilder.isValid()) {
            getNetworkRecord().dump(pw);
        } else {
            pw.println("UnderlyingNetworkRecord incomplete: mNetwork: " + this.mNetworkRecordBuilder.getNetwork());
        }
        pw.println("mIsSelected: " + this.mIsSelected);
        pw.println("mPriorityClass: " + this.mPriorityClass);
        pw.println("mIsPenalized: " + this.mIsPenalized);
        pw.decreaseIndent();
    }

    private java.lang.String getLogPrefix() {
        return "[Network " + this.mNetworkRecordBuilder.getNetwork() + "] ";
    }

    private void logInfo(java.lang.String msg) {
        android.util.Slog.i(TAG, getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[INFO ] " + TAG + getLogPrefix() + msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logWtf(java.lang.String msg) {
        android.util.Slog.wtf(TAG, getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[WTF ] " + TAG + getLogPrefix() + msg);
    }
}
