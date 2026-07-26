package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class MostRestrictive<V> extends com.android.server.devicepolicy.ResolutionMechanism<V> {
    private java.util.List<android.app.admin.PolicyValue<V>> mMostToLeastRestrictive;

    MostRestrictive(java.util.List<android.app.admin.PolicyValue<V>> mostToLeastRestrictive) {
        this.mMostToLeastRestrictive = mostToLeastRestrictive;
    }

    @Override // com.android.server.devicepolicy.ResolutionMechanism
    android.app.admin.PolicyValue<V> resolve(java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> adminPolicies) {
        return resolve(new java.util.ArrayList(adminPolicies.values()));
    }

    @Override // com.android.server.devicepolicy.ResolutionMechanism
    android.app.admin.PolicyValue<V> resolve(java.util.List<android.app.admin.PolicyValue<V>> adminPolicies) {
        if (adminPolicies.isEmpty()) {
            return null;
        }
        for (android.app.admin.PolicyValue<V> value : this.mMostToLeastRestrictive) {
            if (adminPolicies.contains(value)) {
                return value;
            }
        }
        return adminPolicies.get(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    /* JADX INFO: renamed from: getParcelableResolutionMechanism, reason: merged with bridge method [inline-methods] */
    public android.app.admin.MostRestrictive<V> mo3274getParcelableResolutionMechanism() {
        return new android.app.admin.MostRestrictive<>(this.mMostToLeastRestrictive);
    }

    public java.lang.String toString() {
        return "MostRestrictive { mMostToLeastRestrictive= " + this.mMostToLeastRestrictive + " }";
    }
}
