package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class MostRecent<V> extends com.android.server.devicepolicy.ResolutionMechanism<V> {
    MostRecent() {
    }

    @Override // com.android.server.devicepolicy.ResolutionMechanism
    android.app.admin.PolicyValue<V> resolve(java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> adminPolicies) {
        java.util.List<java.util.Map.Entry<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>>> policiesList = new java.util.ArrayList<>(adminPolicies.entrySet());
        if (policiesList.isEmpty()) {
            return null;
        }
        return policiesList.get(policiesList.size() - 1).getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    /* JADX INFO: renamed from: getParcelableResolutionMechanism, reason: merged with bridge method [inline-methods] */
    public android.app.admin.MostRecent<V> mo3274getParcelableResolutionMechanism() {
        return new android.app.admin.MostRecent<>();
    }

    public java.lang.String toString() {
        return "MostRecent {}";
    }
}
