package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class TopPriority<V> extends com.android.server.devicepolicy.ResolutionMechanism<V> {
    private final java.util.List<java.lang.String> mHighestToLowestPriorityAuthorities;

    TopPriority(java.util.List<java.lang.String> highestToLowestPriorityAuthorities) {
        java.util.Objects.requireNonNull(highestToLowestPriorityAuthorities);
        this.mHighestToLowestPriorityAuthorities = highestToLowestPriorityAuthorities;
    }

    @Override // com.android.server.devicepolicy.ResolutionMechanism
    android.app.admin.PolicyValue<V> resolve(java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> adminPolicies) {
        if (adminPolicies.isEmpty()) {
            return null;
        }
        for (final java.lang.String authority : this.mHighestToLowestPriorityAuthorities) {
            java.util.Optional<com.android.server.devicepolicy.EnforcingAdmin> admin = adminPolicies.keySet().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.devicepolicy.TopPriority$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.devicepolicy.EnforcingAdmin) obj).hasAuthority(authority);
                }
            }).findFirst();
            if (admin.isPresent()) {
                return adminPolicies.get(admin.get());
            }
        }
        java.util.Map.Entry<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> policy = adminPolicies.entrySet().stream().findFirst().get();
        return policy.getValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.ResolutionMechanism
    /* JADX INFO: renamed from: getParcelableResolutionMechanism, reason: merged with bridge method [inline-methods] */
    public android.app.admin.TopPriority<V> mo3274getParcelableResolutionMechanism() {
        return new android.app.admin.TopPriority<>(getParcelableAuthorities());
    }

    private java.util.List<android.app.admin.Authority> getParcelableAuthorities() {
        java.util.List<android.app.admin.Authority> authorities = new java.util.ArrayList<>();
        for (java.lang.String authority : this.mHighestToLowestPriorityAuthorities) {
            authorities.add(com.android.server.devicepolicy.EnforcingAdmin.getParcelableAuthority(authority));
        }
        return authorities;
    }

    public java.lang.String toString() {
        return "TopPriority { mHighestToLowestPriorityAuthorities= " + this.mHighestToLowestPriorityAuthorities + " }";
    }
}
