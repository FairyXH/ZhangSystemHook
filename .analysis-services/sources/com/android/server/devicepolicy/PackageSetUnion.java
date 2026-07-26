package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class PackageSetUnion extends com.android.server.devicepolicy.ResolutionMechanism<java.util.Set<java.lang.String>> {
    PackageSetUnion() {
    }

    @Override // com.android.server.devicepolicy.ResolutionMechanism
    android.app.admin.PolicyValue<java.util.Set<java.lang.String>> resolve(java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<java.util.Set<java.lang.String>>> adminPolicies) {
        java.util.Objects.requireNonNull(adminPolicies);
        if (adminPolicies.isEmpty()) {
            return null;
        }
        java.util.Set<java.lang.String> unionOfPolicies = new java.util.HashSet<>();
        for (android.app.admin.PolicyValue<java.util.Set<java.lang.String>> policy : adminPolicies.values()) {
            unionOfPolicies.addAll((java.util.Collection) policy.getValue());
        }
        return new android.app.admin.PackageSetPolicyValue(unionOfPolicies);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    /* JADX INFO: renamed from: getParcelableResolutionMechanism, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
    public android.app.admin.StringSetUnion mo3274getParcelableResolutionMechanism() {
        return new android.app.admin.StringSetUnion();
    }

    public java.lang.String toString() {
        return "PackageSetUnion {}";
    }
}
