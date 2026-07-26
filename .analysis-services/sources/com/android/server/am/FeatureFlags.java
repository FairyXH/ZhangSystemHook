package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface FeatureFlags {
    boolean avoidRepeatedBcastReEnqueues();

    boolean avoidResolvingType();

    boolean batchingOomAdj();

    boolean bfgsManagedNetworkAccess();

    boolean deferOutgoingBroadcasts();

    boolean fgsAbuseDetection();

    boolean fgsBootCompleted();

    boolean fgsDisableSaw();

    boolean followUpOomadjUpdates();

    boolean logExcessiveBinderProxies();

    boolean migrateFullOomadjUpdates();

    boolean newFgsRestrictionLogic();

    boolean oomadjusterCorrectnessRewrite();

    boolean serviceBindingOomAdjPolicy();

    boolean simplifyProcessTraversal();

    boolean skipUnimportantConnections();

    boolean traceReceiverRegistration();

    boolean usePermissionManagerForBroadcastDeliveryCheck();
}
