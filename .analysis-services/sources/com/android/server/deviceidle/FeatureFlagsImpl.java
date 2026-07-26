package com.android.server.deviceidle;

/* JADX INFO: loaded from: classes.dex */
public final class FeatureFlagsImpl implements com.android.server.deviceidle.FeatureFlags {
    @Override // com.android.server.deviceidle.FeatureFlags
    public boolean disableWakelocksInLightIdle() {
        return false;
    }

    @Override // com.android.server.deviceidle.FeatureFlags
    public boolean removeIdleLocation() {
        return false;
    }
}
