package com.android.server.usb.flags;

/* JADX INFO: loaded from: classes3.dex */
public final class FeatureFlagsImpl implements com.android.server.usb.flags.FeatureFlags {
    @Override // com.android.server.usb.flags.FeatureFlags
    public boolean allowRestrictionOfOverlayActivities() {
        return true;
    }

    @Override // com.android.server.usb.flags.FeatureFlags
    public boolean enableBindToMtpService() {
        return true;
    }
}
