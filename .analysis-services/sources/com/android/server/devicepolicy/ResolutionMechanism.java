package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
abstract class ResolutionMechanism<V> {
    /* JADX INFO: renamed from: getParcelableResolutionMechanism */
    abstract android.app.admin.ResolutionMechanism<V> mo3274getParcelableResolutionMechanism();

    abstract android.app.admin.PolicyValue<V> resolve(java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> linkedHashMap);

    ResolutionMechanism() {
    }

    android.app.admin.PolicyValue<V> resolve(java.util.List<android.app.admin.PolicyValue<V>> adminPolicies) {
        throw new java.lang.UnsupportedOperationException();
    }
}
