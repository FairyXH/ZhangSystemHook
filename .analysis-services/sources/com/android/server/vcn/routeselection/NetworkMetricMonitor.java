package com.android.server.vcn.routeselection;

/* JADX INFO: loaded from: classes3.dex */
public abstract class NetworkMetricMonitor implements java.lang.AutoCloseable {
    private static final java.lang.String TAG = com.android.server.vcn.routeselection.NetworkMetricMonitor.class.getSimpleName();
    private static final boolean VDBG = false;
    private final com.android.server.vcn.routeselection.NetworkMetricMonitor.NetworkMetricMonitorCallback mCallback;
    private final android.util.CloseGuard mCloseGuard = new android.util.CloseGuard();
    private boolean mIsSelectedUnderlyingNetwork;
    private boolean mIsStarted;
    private boolean mIsValidationFailed;
    private final android.net.Network mNetwork;
    private final com.android.server.vcn.VcnContext mVcnContext;

    public interface NetworkMetricMonitorCallback {
        void onValidationResultReceived();
    }

    protected abstract void onSelectedUnderlyingNetworkChanged();

    protected NetworkMetricMonitor(com.android.server.vcn.VcnContext vcnContext, android.net.Network network, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig, com.android.server.vcn.routeselection.NetworkMetricMonitor.NetworkMetricMonitorCallback callback) throws java.lang.IllegalAccessException {
        if (!vcnContext.isFlagNetworkMetricMonitorEnabled()) {
            logWtf("networkMetricMonitor flag disabled");
            throw new java.lang.IllegalAccessException("networkMetricMonitor flag disabled");
        }
        this.mVcnContext = (com.android.server.vcn.VcnContext) java.util.Objects.requireNonNull(vcnContext, "Missing vcnContext");
        this.mNetwork = (android.net.Network) java.util.Objects.requireNonNull(network, "Missing network");
        this.mCallback = (com.android.server.vcn.routeselection.NetworkMetricMonitor.NetworkMetricMonitorCallback) java.util.Objects.requireNonNull(callback, "Missing callback");
        this.mIsSelectedUnderlyingNetwork = false;
        this.mIsStarted = false;
        this.mIsValidationFailed = false;
    }

    protected void start() {
        this.mIsStarted = true;
    }

    public void stop() {
        this.mIsValidationFailed = false;
        this.mIsStarted = false;
    }

    protected void onValidationResultReceivedInternal(boolean isFailed) {
        this.mIsValidationFailed = isFailed;
        this.mCallback.onValidationResultReceived();
    }

    public void setIsSelectedUnderlyingNetwork(boolean isSelectedUnderlyingNetwork) {
        if (this.mIsSelectedUnderlyingNetwork == isSelectedUnderlyingNetwork) {
            return;
        }
        this.mIsSelectedUnderlyingNetwork = isSelectedUnderlyingNetwork;
        onSelectedUnderlyingNetworkChanged();
    }

    public static class IpSecTransformWrapper {
        public final android.net.IpSecTransform ipSecTransform;

        public IpSecTransformWrapper(android.net.IpSecTransform ipSecTransform) {
            this.ipSecTransform = ipSecTransform;
        }

        public void requestIpSecTransformState(java.util.concurrent.Executor executor, android.os.OutcomeReceiver<android.net.IpSecTransformState, java.lang.RuntimeException> callback) {
            this.ipSecTransform.requestIpSecTransformState(executor, callback);
        }

        public void close() {
            this.ipSecTransform.close();
        }

        public int hashCode() {
            return java.util.Objects.hash(this.ipSecTransform);
        }

        public boolean equals(java.lang.Object o) {
            if (!(o instanceof com.android.server.vcn.routeselection.NetworkMetricMonitor.IpSecTransformWrapper)) {
                return false;
            }
            com.android.server.vcn.routeselection.NetworkMetricMonitor.IpSecTransformWrapper other = (com.android.server.vcn.routeselection.NetworkMetricMonitor.IpSecTransformWrapper) o;
            return java.util.Objects.equals(this.ipSecTransform, other.ipSecTransform);
        }
    }

    public void setInboundTransform(android.net.IpSecTransform inTransform) {
        setInboundTransformInternal(new com.android.server.vcn.routeselection.NetworkMetricMonitor.IpSecTransformWrapper(inTransform));
    }

    public void setInboundTransformInternal(com.android.server.vcn.routeselection.NetworkMetricMonitor.IpSecTransformWrapper inTransform) {
    }

    public void setCarrierConfig(com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
    }

    public void onLinkPropertiesOrCapabilitiesChanged() {
    }

    public boolean isValidationFailed() {
        return this.mIsValidationFailed;
    }

    public boolean isSelectedUnderlyingNetwork() {
        return this.mIsSelectedUnderlyingNetwork;
    }

    public boolean isStarted() {
        return this.mIsStarted;
    }

    public com.android.server.vcn.VcnContext getVcnContext() {
        return this.mVcnContext;
    }

    public android.net.Network getNetwork() {
        return this.mNetwork;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mCloseGuard.close();
        stop();
    }

    protected void finalize() throws java.lang.Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    private java.lang.String getClassName() {
        return getClass().getSimpleName();
    }

    protected java.lang.String getLogPrefix() {
        return " [Network " + this.mNetwork + "] ";
    }

    protected void logV(java.lang.String msg) {
    }

    protected void logInfo(java.lang.String msg) {
        android.util.Slog.i(getClassName(), getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[INFO ] " + getClassName() + getLogPrefix() + msg);
    }

    protected void logW(java.lang.String msg) {
        android.util.Slog.w(getClassName(), getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[WARN ] " + getClassName() + getLogPrefix() + msg);
    }

    protected void logWtf(java.lang.String msg) {
        android.util.Slog.wtf(getClassName(), getLogPrefix() + msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[WTF ] " + getClassName() + getLogPrefix() + msg);
    }

    protected static void logV(java.lang.String className, java.lang.String msgWithPrefix) {
    }

    protected static void logE(java.lang.String className, java.lang.String msgWithPrefix) {
        android.util.Slog.w(className, msgWithPrefix);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[ERROR ] " + className + msgWithPrefix);
    }

    protected static void logWtf(java.lang.String className, java.lang.String msgWithPrefix) {
        android.util.Slog.wtf(className, msgWithPrefix);
        com.android.server.VcnManagementService.LOCAL_LOG.log("[WTF ] " + className + msgWithPrefix);
    }
}
