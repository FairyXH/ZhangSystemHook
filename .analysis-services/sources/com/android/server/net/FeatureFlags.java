package com.android.server.net;

/* JADX INFO: loaded from: classes2.dex */
public interface FeatureFlags {
    boolean networkBlockedForTopSleepingAndAbove();

    boolean useDifferentDelaysForBackgroundChain();

    boolean useMeteredFirewallChains();
}
