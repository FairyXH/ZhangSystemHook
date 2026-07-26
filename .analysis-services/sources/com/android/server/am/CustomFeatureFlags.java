package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class CustomFeatureFlags implements com.android.server.am.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.am.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.am.Flags.FLAG_AVOID_REPEATED_BCAST_RE_ENQUEUES, com.android.server.am.Flags.FLAG_AVOID_RESOLVING_TYPE, com.android.server.am.Flags.FLAG_BATCHING_OOM_ADJ, com.android.server.am.Flags.FLAG_BFGS_MANAGED_NETWORK_ACCESS, com.android.server.am.Flags.FLAG_DEFER_OUTGOING_BROADCASTS, com.android.server.am.Flags.FLAG_FGS_ABUSE_DETECTION, com.android.server.am.Flags.FLAG_FGS_BOOT_COMPLETED, com.android.server.am.Flags.FLAG_FGS_DISABLE_SAW, com.android.server.am.Flags.FLAG_FOLLOW_UP_OOMADJ_UPDATES, com.android.server.am.Flags.FLAG_LOG_EXCESSIVE_BINDER_PROXIES, com.android.server.am.Flags.FLAG_MIGRATE_FULL_OOMADJ_UPDATES, com.android.server.am.Flags.FLAG_NEW_FGS_RESTRICTION_LOGIC, com.android.server.am.Flags.FLAG_OOMADJUSTER_CORRECTNESS_REWRITE, com.android.server.am.Flags.FLAG_SERVICE_BINDING_OOM_ADJ_POLICY, com.android.server.am.Flags.FLAG_SIMPLIFY_PROCESS_TRAVERSAL, com.android.server.am.Flags.FLAG_SKIP_UNIMPORTANT_CONNECTIONS, com.android.server.am.Flags.FLAG_TRACE_RECEIVER_REGISTRATION, com.android.server.am.Flags.FLAG_USE_PERMISSION_MANAGER_FOR_BROADCAST_DELIVERY_CHECK, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.am.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean avoidRepeatedBcastReEnqueues() {
        return getValue(com.android.server.am.Flags.FLAG_AVOID_REPEATED_BCAST_RE_ENQUEUES, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).avoidRepeatedBcastReEnqueues();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean avoidResolvingType() {
        return getValue(com.android.server.am.Flags.FLAG_AVOID_RESOLVING_TYPE, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).avoidResolvingType();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean batchingOomAdj() {
        return getValue(com.android.server.am.Flags.FLAG_BATCHING_OOM_ADJ, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).batchingOomAdj();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean bfgsManagedNetworkAccess() {
        return getValue(com.android.server.am.Flags.FLAG_BFGS_MANAGED_NETWORK_ACCESS, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda16
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).bfgsManagedNetworkAccess();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean deferOutgoingBroadcasts() {
        return getValue(com.android.server.am.Flags.FLAG_DEFER_OUTGOING_BROADCASTS, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).deferOutgoingBroadcasts();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean fgsAbuseDetection() {
        return getValue(com.android.server.am.Flags.FLAG_FGS_ABUSE_DETECTION, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda17
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).fgsAbuseDetection();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean fgsBootCompleted() {
        return getValue(com.android.server.am.Flags.FLAG_FGS_BOOT_COMPLETED, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).fgsBootCompleted();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean fgsDisableSaw() {
        return getValue(com.android.server.am.Flags.FLAG_FGS_DISABLE_SAW, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).fgsDisableSaw();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean followUpOomadjUpdates() {
        return getValue(com.android.server.am.Flags.FLAG_FOLLOW_UP_OOMADJ_UPDATES, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).followUpOomadjUpdates();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean logExcessiveBinderProxies() {
        return getValue(com.android.server.am.Flags.FLAG_LOG_EXCESSIVE_BINDER_PROXIES, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).logExcessiveBinderProxies();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean migrateFullOomadjUpdates() {
        return getValue(com.android.server.am.Flags.FLAG_MIGRATE_FULL_OOMADJ_UPDATES, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).migrateFullOomadjUpdates();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean newFgsRestrictionLogic() {
        return getValue(com.android.server.am.Flags.FLAG_NEW_FGS_RESTRICTION_LOGIC, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).newFgsRestrictionLogic();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean oomadjusterCorrectnessRewrite() {
        return getValue(com.android.server.am.Flags.FLAG_OOMADJUSTER_CORRECTNESS_REWRITE, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).oomadjusterCorrectnessRewrite();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean serviceBindingOomAdjPolicy() {
        return getValue(com.android.server.am.Flags.FLAG_SERVICE_BINDING_OOM_ADJ_POLICY, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).serviceBindingOomAdjPolicy();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean simplifyProcessTraversal() {
        return getValue(com.android.server.am.Flags.FLAG_SIMPLIFY_PROCESS_TRAVERSAL, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).simplifyProcessTraversal();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean skipUnimportantConnections() {
        return getValue(com.android.server.am.Flags.FLAG_SKIP_UNIMPORTANT_CONNECTIONS, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).skipUnimportantConnections();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean traceReceiverRegistration() {
        return getValue(com.android.server.am.Flags.FLAG_TRACE_RECEIVER_REGISTRATION, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).traceReceiverRegistration();
            }
        });
    }

    @Override // com.android.server.am.FeatureFlags
    public boolean usePermissionManagerForBroadcastDeliveryCheck() {
        return getValue(com.android.server.am.Flags.FLAG_USE_PERMISSION_MANAGER_FOR_BROADCAST_DELIVERY_CHECK, new java.util.function.Predicate() { // from class: com.android.server.am.CustomFeatureFlags$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.FeatureFlags) obj).usePermissionManagerForBroadcastDeliveryCheck();
            }
        });
    }

    public boolean isFlagReadOnlyOptimized(java.lang.String flagName) {
        if (this.mReadOnlyFlagsSet.contains(flagName) && isOptimizationEnabled()) {
            return true;
        }
        return false;
    }

    private boolean isOptimizationEnabled() {
        return false;
    }

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.am.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.am.Flags.FLAG_AVOID_REPEATED_BCAST_RE_ENQUEUES, com.android.server.am.Flags.FLAG_AVOID_RESOLVING_TYPE, com.android.server.am.Flags.FLAG_BATCHING_OOM_ADJ, com.android.server.am.Flags.FLAG_BFGS_MANAGED_NETWORK_ACCESS, com.android.server.am.Flags.FLAG_DEFER_OUTGOING_BROADCASTS, com.android.server.am.Flags.FLAG_FGS_ABUSE_DETECTION, com.android.server.am.Flags.FLAG_FGS_BOOT_COMPLETED, com.android.server.am.Flags.FLAG_FGS_DISABLE_SAW, com.android.server.am.Flags.FLAG_FOLLOW_UP_OOMADJ_UPDATES, com.android.server.am.Flags.FLAG_LOG_EXCESSIVE_BINDER_PROXIES, com.android.server.am.Flags.FLAG_MIGRATE_FULL_OOMADJ_UPDATES, com.android.server.am.Flags.FLAG_NEW_FGS_RESTRICTION_LOGIC, com.android.server.am.Flags.FLAG_OOMADJUSTER_CORRECTNESS_REWRITE, com.android.server.am.Flags.FLAG_SERVICE_BINDING_OOM_ADJ_POLICY, com.android.server.am.Flags.FLAG_SIMPLIFY_PROCESS_TRAVERSAL, com.android.server.am.Flags.FLAG_SKIP_UNIMPORTANT_CONNECTIONS, com.android.server.am.Flags.FLAG_TRACE_RECEIVER_REGISTRATION, com.android.server.am.Flags.FLAG_USE_PERMISSION_MANAGER_FOR_BROADCAST_DELIVERY_CHECK);
    }
}
