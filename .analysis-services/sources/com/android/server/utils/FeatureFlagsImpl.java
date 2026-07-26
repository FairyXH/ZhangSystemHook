package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public final class FeatureFlagsImpl implements com.android.server.utils.FeatureFlags {
    @Override // com.android.server.utils.FeatureFlags
    public boolean anrTimerFreezer() {
        return false;
    }

    @Override // com.android.server.utils.FeatureFlags
    public boolean anrTimerService() {
        return false;
    }
}
