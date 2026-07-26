package com.android.server.stats;

/* JADX INFO: loaded from: classes3.dex */
public final class Flags {
    private static com.android.server.stats.FeatureFlags FEATURE_FLAGS = new com.android.server.stats.FeatureFlagsImpl();
    public static final java.lang.String FLAG_ADD_MOBILE_BYTES_TRANSFER_BY_PROC_STATE_PULLER = "com.android.server.stats.add_mobile_bytes_transfer_by_proc_state_puller";

    public static boolean addMobileBytesTransferByProcStatePuller() {
        return FEATURE_FLAGS.addMobileBytesTransferByProcStatePuller();
    }
}
