package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ActivitySecurityModelFeatureFlags {
    static final int ASM_VERSION = 11;
    private static final int DEFAULT_VALUE = 0;
    static final java.lang.String DOC_LINK = "go/android-asm";
    private static final java.lang.String KEY_ASM_PREFIX = "ActivitySecurity__";
    private static final java.lang.String KEY_ASM_RESTRICTIONS_ENABLED = "ActivitySecurity__asm_restrictions_enabled";
    private static final java.lang.String KEY_ASM_TOASTS_ENABLED = "ActivitySecurity__asm_toasts_enabled";
    private static final java.lang.String NAMESPACE = "window_manager";
    private static final int VALUE_DISABLE = 0;
    private static final int VALUE_ENABLE_FOR_ALL = 2;
    private static final int VALUE_ENABLE_FOR_V = 1;
    private static int sAsmRestrictionsEnabled;
    private static int sAsmToastsEnabled;

    ActivitySecurityModelFeatureFlags() {
    }

    static void initialize(java.util.concurrent.Executor executor) {
        updateFromDeviceConfig();
        android.provider.DeviceConfig.addOnPropertiesChangedListener(NAMESPACE, executor, new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wm.ActivitySecurityModelFeatureFlags$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                com.android.server.wm.ActivitySecurityModelFeatureFlags.updateFromDeviceConfig();
            }
        });
    }

    static boolean shouldShowToast(int uid) {
        if (sAsmToastsEnabled != 2) {
            return sAsmToastsEnabled == 1 && android.app.compat.CompatChanges.isChangeEnabled(230590090L, uid);
        }
        return true;
    }

    static boolean shouldRestrictActivitySwitch(int uid) {
        if (android.security.Flags.asmRestrictionsEnabled()) {
            return android.app.compat.CompatChanges.isChangeEnabled(230590090L, uid) || asmRestrictionsEnabledForAll();
        }
        return false;
    }

    static boolean asmRestrictionsEnabledForAll() {
        return sAsmRestrictionsEnabled == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateFromDeviceConfig() {
        sAsmToastsEnabled = android.provider.DeviceConfig.getInt(NAMESPACE, KEY_ASM_TOASTS_ENABLED, 0);
        sAsmRestrictionsEnabled = android.provider.DeviceConfig.getInt(NAMESPACE, KEY_ASM_RESTRICTIONS_ENABLED, 0);
    }
}
