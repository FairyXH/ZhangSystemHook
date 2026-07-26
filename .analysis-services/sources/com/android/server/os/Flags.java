package com.android.server.os;

/* JADX INFO: loaded from: classes2.dex */
public final class Flags {
    private static com.android.server.os.FeatureFlags FEATURE_FLAGS = new com.android.server.os.FeatureFlagsImpl();
    public static final java.lang.String FLAG_ASYNC_START_BUGREPORT = "com.android.server.os.async_start_bugreport";
    public static final java.lang.String FLAG_PROTO_TOMBSTONE = "com.android.server.os.proto_tombstone";

    public static boolean asyncStartBugreport() {
        return FEATURE_FLAGS.asyncStartBugreport();
    }

    public static boolean protoTombstone() {
        return FEATURE_FLAGS.protoTombstone();
    }
}
