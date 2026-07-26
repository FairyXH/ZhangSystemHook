package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public final class FeatureFlagsImpl implements com.android.server.biometrics.FeatureFlags {
    @Override // com.android.server.biometrics.FeatureFlags
    public boolean faceVhalFeature() {
        return false;
    }

    @Override // com.android.server.biometrics.FeatureFlags
    public boolean useVhalForTesting() {
        return false;
    }
}
