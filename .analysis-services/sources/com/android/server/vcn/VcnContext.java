package com.android.server.vcn;

/* JADX INFO: loaded from: classes3.dex */
public class VcnContext {
    private final android.content.Context mContext;
    private final android.net.vcn.FeatureFlags mFeatureFlags = new android.net.vcn.FeatureFlagsImpl();
    private final boolean mIsInTestMode;
    private final android.os.Looper mLooper;
    private final com.android.server.vcn.VcnNetworkProvider mVcnNetworkProvider;

    public VcnContext(android.content.Context context, android.os.Looper looper, com.android.server.vcn.VcnNetworkProvider vcnNetworkProvider, boolean isInTestMode) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context, "Missing context");
        this.mLooper = (android.os.Looper) java.util.Objects.requireNonNull(looper, "Missing looper");
        this.mVcnNetworkProvider = (com.android.server.vcn.VcnNetworkProvider) java.util.Objects.requireNonNull(vcnNetworkProvider, "Missing networkProvider");
        this.mIsInTestMode = isInTestMode;
    }

    public android.content.Context getContext() {
        return this.mContext;
    }

    public android.os.Looper getLooper() {
        return this.mLooper;
    }

    public com.android.server.vcn.VcnNetworkProvider getVcnNetworkProvider() {
        return this.mVcnNetworkProvider;
    }

    public boolean isInTestMode() {
        return this.mIsInTestMode;
    }

    public boolean isFlagNetworkMetricMonitorEnabled() {
        return this.mFeatureFlags.networkMetricMonitor();
    }

    public boolean isFlagIpSecTransformStateEnabled() {
        try {
            new android.net.IpSecTransformState.Builder();
            return true;
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    public android.net.vcn.FeatureFlags getFeatureFlags() {
        return this.mFeatureFlags;
    }

    public boolean isFlagSafeModeTimeoutConfigEnabled() {
        return this.mFeatureFlags.safeModeTimeoutConfig();
    }

    public void ensureRunningOnLooperThread() {
        if (getLooper().getThread() != java.lang.Thread.currentThread()) {
            throw new java.lang.IllegalStateException("Not running on VcnMgmtSvc thread");
        }
    }
}
