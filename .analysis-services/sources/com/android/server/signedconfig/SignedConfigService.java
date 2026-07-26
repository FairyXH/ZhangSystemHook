package com.android.server.signedconfig;

/* JADX INFO: loaded from: classes3.dex */
public class SignedConfigService {
    private static final boolean DBG = false;
    private static final java.lang.String KEY_GLOBAL_SETTINGS = "android.settings.global";
    private static final java.lang.String KEY_GLOBAL_SETTINGS_SIGNATURE = "android.settings.global.signature";
    private static final java.lang.String TAG = "SignedConfig";
    private final android.content.Context mContext;
    private final android.content.pm.PackageManagerInternal mPacMan = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);

    private static class UpdateReceiver extends android.content.BroadcastReceiver {
        private UpdateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            new com.android.server.signedconfig.SignedConfigService(context).handlePackageBroadcast(intent);
        }
    }

    public SignedConfigService(android.content.Context context) {
        this.mContext = context;
    }

    void handlePackageBroadcast(android.content.Intent intent) {
        android.net.Uri packageData = intent.getData();
        java.lang.String packageName = packageData == null ? null : packageData.getSchemeSpecificPart();
        if (packageName == null) {
            return;
        }
        int userId = this.mContext.getUser().getIdentifier();
        android.content.pm.PackageInfo pi = this.mPacMan.getPackageInfo(packageName, 128L, 1000, userId);
        if (pi == null) {
            android.util.Slog.w(TAG, "Got null PackageInfo for " + packageName + "; user " + userId);
            return;
        }
        android.os.Bundle metaData = pi.applicationInfo.metaData;
        if (metaData != null && metaData.containsKey(KEY_GLOBAL_SETTINGS) && metaData.containsKey(KEY_GLOBAL_SETTINGS_SIGNATURE)) {
            com.android.server.signedconfig.SignedConfigEvent event = new com.android.server.signedconfig.SignedConfigEvent();
            try {
                event.type = 1;
                event.fromPackage = packageName;
                java.lang.String config = metaData.getString(KEY_GLOBAL_SETTINGS);
                java.lang.String signature = metaData.getString(KEY_GLOBAL_SETTINGS_SIGNATURE);
                new com.android.server.signedconfig.GlobalSettingsConfigApplicator(this.mContext, packageName, event).applyConfig(new java.lang.String(java.util.Base64.getDecoder().decode(config), java.nio.charset.StandardCharsets.UTF_8), signature);
            } catch (java.lang.IllegalArgumentException e) {
                android.util.Slog.e(TAG, "Failed to base64 decode global settings config from " + packageName);
                event.status = 2;
            } finally {
                event.send();
            }
        }
    }

    public static void registerUpdateReceiver(android.content.Context context) {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addDataScheme("package");
        context.registerReceiver(new com.android.server.signedconfig.SignedConfigService.UpdateReceiver(), filter);
    }
}
