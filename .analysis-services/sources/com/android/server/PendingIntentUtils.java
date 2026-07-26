package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class PendingIntentUtils {
    public static android.os.Bundle createDontSendToRestrictedAppsBundle(android.os.Bundle bundle) {
        android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
        options.setDontSendToRestrictedApps(true);
        options.setPendingIntentBackgroundActivityLaunchAllowed(false);
        if (bundle == null) {
            return options.toBundle();
        }
        bundle.putAll(options.toBundle());
        return bundle;
    }

    private PendingIntentUtils() {
    }
}
