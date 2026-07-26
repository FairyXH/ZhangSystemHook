package com.android.server.compat;

/* JADX INFO: loaded from: classes.dex */
public class OverrideValidatorImpl extends com.android.internal.compat.IOverrideValidator.Stub {
    private com.android.internal.compat.AndroidBuildClassifier mAndroidBuildClassifier;
    private com.android.server.compat.CompatConfig mCompatConfig;
    private android.content.Context mContext;
    private boolean mForceNonDebuggableFinalBuild = false;

    private class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver() {
            super(new android.os.Handler());
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            com.android.server.compat.OverrideValidatorImpl.this.mForceNonDebuggableFinalBuild = android.provider.Settings.Global.getInt(com.android.server.compat.OverrideValidatorImpl.this.mContext.getContentResolver(), "force_non_debuggable_final_build_for_compat", 0) == 1;
        }
    }

    OverrideValidatorImpl(com.android.internal.compat.AndroidBuildClassifier androidBuildClassifier, android.content.Context context, com.android.server.compat.CompatConfig config) {
        this.mAndroidBuildClassifier = androidBuildClassifier;
        this.mContext = context;
        this.mCompatConfig = config;
    }

    com.android.internal.compat.OverrideAllowedState getOverrideAllowedStateForRecheck(long changeId, java.lang.String packageName) {
        return getOverrideAllowedStateInternal(changeId, packageName, true);
    }

    public com.android.internal.compat.OverrideAllowedState getOverrideAllowedState(long changeId, java.lang.String packageName) {
        return getOverrideAllowedStateInternal(changeId, packageName, false);
    }

    private com.android.internal.compat.OverrideAllowedState getOverrideAllowedStateInternal(long changeId, java.lang.String packageName, boolean isRecheck) {
        if (this.mCompatConfig.isLoggingOnly(changeId)) {
            return new com.android.internal.compat.OverrideAllowedState(5, -1, -1);
        }
        boolean debuggableBuild = this.mAndroidBuildClassifier.isDebuggableBuild() && !this.mForceNonDebuggableFinalBuild;
        boolean finalBuild = this.mAndroidBuildClassifier.isFinalBuild() || this.mForceNonDebuggableFinalBuild;
        int maxTargetSdk = this.mCompatConfig.maxTargetSdkForChangeIdOptIn(changeId);
        boolean disabled = this.mCompatConfig.isDisabled(changeId);
        if (debuggableBuild) {
            return new com.android.internal.compat.OverrideAllowedState(0, -1, -1);
        }
        if (maxTargetSdk >= this.mAndroidBuildClassifier.platformTargetSdk()) {
            return new com.android.internal.compat.OverrideAllowedState(6, -1, maxTargetSdk);
        }
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        if (packageManager == null) {
            throw new java.lang.IllegalStateException("No PackageManager!");
        }
        try {
            android.content.pm.ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 4194304);
            if (this.mCompatConfig.isOverridable(changeId) && (isRecheck || this.mContext.checkCallingOrSelfPermission("android.permission.OVERRIDE_COMPAT_CHANGE_CONFIG_ON_RELEASE_BUILD") == 0)) {
                return new com.android.internal.compat.OverrideAllowedState(0, -1, -1);
            }
            int appTargetSdk = applicationInfo.targetSdkVersion;
            if ((applicationInfo.flags & 2) == 0) {
                return new com.android.internal.compat.OverrideAllowedState(1, -1, -1);
            }
            if (!finalBuild) {
                return new com.android.internal.compat.OverrideAllowedState(0, appTargetSdk, maxTargetSdk);
            }
            if (maxTargetSdk == -1 && !disabled) {
                return new com.android.internal.compat.OverrideAllowedState(2, appTargetSdk, maxTargetSdk);
            }
            if (disabled || appTargetSdk <= maxTargetSdk) {
                return new com.android.internal.compat.OverrideAllowedState(0, appTargetSdk, maxTargetSdk);
            }
            return new com.android.internal.compat.OverrideAllowedState(3, appTargetSdk, maxTargetSdk);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return new com.android.internal.compat.OverrideAllowedState(4, -1, -1);
        }
    }

    void registerContentObserver() {
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("force_non_debuggable_final_build_for_compat"), false, new com.android.server.compat.OverrideValidatorImpl.SettingsObserver());
    }

    void forceNonDebuggableFinalForTest(boolean value) {
        this.mForceNonDebuggableFinalBuild = value;
    }
}
