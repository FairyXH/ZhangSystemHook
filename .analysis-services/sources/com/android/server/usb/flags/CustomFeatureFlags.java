package com.android.server.usb.flags;

/* JADX INFO: loaded from: classes3.dex */
public class CustomFeatureFlags implements com.android.server.usb.flags.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.usb.flags.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.server.usb.flags.Flags.FLAG_ALLOW_RESTRICTION_OF_OVERLAY_ACTIVITIES, com.android.server.usb.flags.Flags.FLAG_ENABLE_BIND_TO_MTP_SERVICE, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.server.usb.flags.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.server.usb.flags.FeatureFlags
    public boolean allowRestrictionOfOverlayActivities() {
        return getValue(com.android.server.usb.flags.Flags.FLAG_ALLOW_RESTRICTION_OF_OVERLAY_ACTIVITIES, new java.util.function.Predicate() { // from class: com.android.server.usb.flags.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.usb.flags.FeatureFlags) obj).allowRestrictionOfOverlayActivities();
            }
        });
    }

    @Override // com.android.server.usb.flags.FeatureFlags
    public boolean enableBindToMtpService() {
        return getValue(com.android.server.usb.flags.Flags.FLAG_ENABLE_BIND_TO_MTP_SERVICE, new java.util.function.Predicate() { // from class: com.android.server.usb.flags.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.usb.flags.FeatureFlags) obj).enableBindToMtpService();
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

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.server.usb.flags.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.server.usb.flags.Flags.FLAG_ALLOW_RESTRICTION_OF_OVERLAY_ACTIVITIES, com.android.server.usb.flags.Flags.FLAG_ENABLE_BIND_TO_MTP_SERVICE);
    }
}
