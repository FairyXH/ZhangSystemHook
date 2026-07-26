package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public final class FeatureFlagsImpl implements com.android.server.alarm.FeatureFlags {
    @Override // com.android.server.alarm.FeatureFlags
    public boolean startUserBeforeScheduledAlarms() {
        return false;
    }

    @Override // com.android.server.alarm.FeatureFlags
    public boolean useFrozenStateToDropListenerAlarms() {
        return true;
    }
}
