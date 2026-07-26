package com.android.systemui.shared;

/* JADX INFO: loaded from: classes3.dex */
public final class FeatureFlagsImpl implements com.android.systemui.shared.FeatureFlags {
    @Override // com.android.systemui.shared.FeatureFlags
    public boolean bouncerAreaExclusion() {
        return true;
    }

    @Override // com.android.systemui.shared.FeatureFlags
    public boolean enableHomeDelay() {
        return false;
    }

    @Override // com.android.systemui.shared.FeatureFlags
    public boolean exampleSharedFlag() {
        return false;
    }

    @Override // com.android.systemui.shared.FeatureFlags
    public boolean returnAnimationFrameworkLibrary() {
        return false;
    }

    @Override // com.android.systemui.shared.FeatureFlags
    public boolean shadeAllowBackGesture() {
        return false;
    }

    @Override // com.android.systemui.shared.FeatureFlags
    public boolean sidefpsControllerRefactor() {
        return true;
    }
}
