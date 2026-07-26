package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class Flags {
    private static com.android.server.am.FeatureFlags FEATURE_FLAGS = new com.android.server.am.FeatureFlagsImpl();
    public static final java.lang.String FLAG_AVOID_REPEATED_BCAST_RE_ENQUEUES = "com.android.server.am.avoid_repeated_bcast_re_enqueues";
    public static final java.lang.String FLAG_AVOID_RESOLVING_TYPE = "com.android.server.am.avoid_resolving_type";
    public static final java.lang.String FLAG_BATCHING_OOM_ADJ = "com.android.server.am.batching_oom_adj";
    public static final java.lang.String FLAG_BFGS_MANAGED_NETWORK_ACCESS = "com.android.server.am.bfgs_managed_network_access";
    public static final java.lang.String FLAG_DEFER_OUTGOING_BROADCASTS = "com.android.server.am.defer_outgoing_broadcasts";
    public static final java.lang.String FLAG_FGS_ABUSE_DETECTION = "com.android.server.am.fgs_abuse_detection";
    public static final java.lang.String FLAG_FGS_BOOT_COMPLETED = "com.android.server.am.fgs_boot_completed";
    public static final java.lang.String FLAG_FGS_DISABLE_SAW = "com.android.server.am.fgs_disable_saw";
    public static final java.lang.String FLAG_FOLLOW_UP_OOMADJ_UPDATES = "com.android.server.am.follow_up_oomadj_updates";
    public static final java.lang.String FLAG_LOG_EXCESSIVE_BINDER_PROXIES = "com.android.server.am.log_excessive_binder_proxies";
    public static final java.lang.String FLAG_MIGRATE_FULL_OOMADJ_UPDATES = "com.android.server.am.migrate_full_oomadj_updates";
    public static final java.lang.String FLAG_NEW_FGS_RESTRICTION_LOGIC = "com.android.server.am.new_fgs_restriction_logic";
    public static final java.lang.String FLAG_OOMADJUSTER_CORRECTNESS_REWRITE = "com.android.server.am.oomadjuster_correctness_rewrite";
    public static final java.lang.String FLAG_SERVICE_BINDING_OOM_ADJ_POLICY = "com.android.server.am.service_binding_oom_adj_policy";
    public static final java.lang.String FLAG_SIMPLIFY_PROCESS_TRAVERSAL = "com.android.server.am.simplify_process_traversal";
    public static final java.lang.String FLAG_SKIP_UNIMPORTANT_CONNECTIONS = "com.android.server.am.skip_unimportant_connections";
    public static final java.lang.String FLAG_TRACE_RECEIVER_REGISTRATION = "com.android.server.am.trace_receiver_registration";
    public static final java.lang.String FLAG_USE_PERMISSION_MANAGER_FOR_BROADCAST_DELIVERY_CHECK = "com.android.server.am.use_permission_manager_for_broadcast_delivery_check";

    public static boolean avoidRepeatedBcastReEnqueues() {
        return FEATURE_FLAGS.avoidRepeatedBcastReEnqueues();
    }

    public static boolean avoidResolvingType() {
        return FEATURE_FLAGS.avoidResolvingType();
    }

    public static boolean batchingOomAdj() {
        return FEATURE_FLAGS.batchingOomAdj();
    }

    public static boolean bfgsManagedNetworkAccess() {
        return FEATURE_FLAGS.bfgsManagedNetworkAccess();
    }

    public static boolean deferOutgoingBroadcasts() {
        return FEATURE_FLAGS.deferOutgoingBroadcasts();
    }

    public static boolean fgsAbuseDetection() {
        return FEATURE_FLAGS.fgsAbuseDetection();
    }

    public static boolean fgsBootCompleted() {
        return FEATURE_FLAGS.fgsBootCompleted();
    }

    public static boolean fgsDisableSaw() {
        return FEATURE_FLAGS.fgsDisableSaw();
    }

    public static boolean followUpOomadjUpdates() {
        return FEATURE_FLAGS.followUpOomadjUpdates();
    }

    public static boolean logExcessiveBinderProxies() {
        return FEATURE_FLAGS.logExcessiveBinderProxies();
    }

    public static boolean migrateFullOomadjUpdates() {
        return FEATURE_FLAGS.migrateFullOomadjUpdates();
    }

    public static boolean newFgsRestrictionLogic() {
        return FEATURE_FLAGS.newFgsRestrictionLogic();
    }

    public static boolean oomadjusterCorrectnessRewrite() {
        return FEATURE_FLAGS.oomadjusterCorrectnessRewrite();
    }

    public static boolean serviceBindingOomAdjPolicy() {
        return FEATURE_FLAGS.serviceBindingOomAdjPolicy();
    }

    public static boolean simplifyProcessTraversal() {
        return FEATURE_FLAGS.simplifyProcessTraversal();
    }

    public static boolean skipUnimportantConnections() {
        return FEATURE_FLAGS.skipUnimportantConnections();
    }

    public static boolean traceReceiverRegistration() {
        return FEATURE_FLAGS.traceReceiverRegistration();
    }

    public static boolean usePermissionManagerForBroadcastDeliveryCheck() {
        return FEATURE_FLAGS.usePermissionManagerForBroadcastDeliveryCheck();
    }
}
