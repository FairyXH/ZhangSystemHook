package com.android.server.usb.flags;

/* JADX INFO: loaded from: classes3.dex */
public final class Flags {
    private static com.android.server.usb.flags.FeatureFlags FEATURE_FLAGS = new com.android.server.usb.flags.FeatureFlagsImpl();
    public static final java.lang.String FLAG_ALLOW_RESTRICTION_OF_OVERLAY_ACTIVITIES = "com.android.server.usb.flags.allow_restriction_of_overlay_activities";
    public static final java.lang.String FLAG_ENABLE_BIND_TO_MTP_SERVICE = "com.android.server.usb.flags.enable_bind_to_mtp_service";

    public static boolean allowRestrictionOfOverlayActivities() {
        return FEATURE_FLAGS.allowRestrictionOfOverlayActivities();
    }

    public static boolean enableBindToMtpService() {
        return FEATURE_FLAGS.enableBindToMtpService();
    }
}
