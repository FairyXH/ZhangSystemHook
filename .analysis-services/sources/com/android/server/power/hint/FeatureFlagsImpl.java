package com.android.server.power.hint;

/* JADX INFO: loaded from: classes3.dex */
public final class FeatureFlagsImpl implements com.android.server.power.hint.FeatureFlags {
    @Override // com.android.server.power.hint.FeatureFlags
    public boolean adpfSessionTag() {
        return false;
    }

    @Override // com.android.server.power.hint.FeatureFlags
    public boolean powerhintThreadCleanup() {
        return true;
    }
}
