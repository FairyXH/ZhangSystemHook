package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class FlagUnion extends com.android.server.devicepolicy.ResolutionMechanism<java.lang.Integer> {
    FlagUnion() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    public android.app.admin.IntegerPolicyValue resolve(java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<java.lang.Integer>> adminPolicies) {
        java.util.Objects.requireNonNull(adminPolicies);
        if (adminPolicies.isEmpty()) {
            return null;
        }
        java.lang.Integer unionOfPolicies = 0;
        for (android.app.admin.PolicyValue<java.lang.Integer> policy : adminPolicies.values()) {
            unionOfPolicies = java.lang.Integer.valueOf(unionOfPolicies.intValue() | ((java.lang.Integer) policy.getValue()).intValue());
        }
        return new android.app.admin.IntegerPolicyValue(unionOfPolicies.intValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    /* JADX INFO: renamed from: getParcelableResolutionMechanism, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
    public android.app.admin.FlagUnion mo3274getParcelableResolutionMechanism() {
        return android.app.admin.FlagUnion.FLAG_UNION;
    }

    public java.lang.String toString() {
        return "IntegerUnion {}";
    }
}
