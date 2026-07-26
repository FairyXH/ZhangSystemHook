package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class FeatureFlagsImpl implements com.android.server.am.FeatureFlags {
    @Override // com.android.server.am.FeatureFlags
    public boolean avoidRepeatedBcastReEnqueues() {
        return true;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean avoidResolvingType() {
        return true;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean batchingOomAdj() {
        return false;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean bfgsManagedNetworkAccess() {
        return false;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean deferOutgoingBroadcasts() {
        return false;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean fgsAbuseDetection() {
        return false;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean fgsBootCompleted() {
        return true;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean fgsDisableSaw() {
        return true;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean followUpOomadjUpdates() {
        return false;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean logExcessiveBinderProxies() {
        return false;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean migrateFullOomadjUpdates() {
        return true;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean newFgsRestrictionLogic() {
        return true;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean oomadjusterCorrectnessRewrite() {
        return true;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean serviceBindingOomAdjPolicy() {
        return true;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean simplifyProcessTraversal() {
        return true;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean skipUnimportantConnections() {
        return false;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean traceReceiverRegistration() {
        return true;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean usePermissionManagerForBroadcastDeliveryCheck() {
        return true;
    }
}
