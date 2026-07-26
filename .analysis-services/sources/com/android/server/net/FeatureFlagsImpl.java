package com.android.server.net;

/* JADX INFO: loaded from: classes2.dex */
public final class FeatureFlagsImpl implements com.android.server.net.FeatureFlags {
    @Override // com.android.server.net.FeatureFlags
    public boolean networkBlockedForTopSleepingAndAbove() {
        return true;
    }

    @Override // com.android.server.net.FeatureFlags
    public boolean useDifferentDelaysForBackgroundChain() {
        return false;
    }

    @Override // com.android.server.net.FeatureFlags
    public boolean useMeteredFirewallChains() {
        return true;
    }
}
