package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public interface LegacyPermissionManagerInternal {

    public interface PackagesProvider {
        java.lang.String[] getPackages(int i);
    }

    public interface SyncAdapterPackagesProvider {
        java.lang.String[] getPackages(java.lang.String str, int i);
    }

    int checkSoundTriggerRecordAudioPermissionForDataDelivery(int i, java.lang.String str, java.lang.String str2, java.lang.String str3);

    void grantDefaultPermissions(int i);

    void grantDefaultPermissionsToDefaultSimCallManager(java.lang.String str, int i);

    void grantDefaultPermissionsToDefaultUseOpenWifiApp(java.lang.String str, int i);

    void resetRuntimePermissions();

    void scheduleReadDefaultPermissionExceptions();

    void setDialerAppPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider packagesProvider);

    void setLocationExtraPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider packagesProvider);

    void setLocationPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider packagesProvider);

    void setSimCallManagerPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider packagesProvider);

    void setSmsAppPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider packagesProvider);

    void setSyncAdapterPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.SyncAdapterPackagesProvider syncAdapterPackagesProvider);

    void setUseOpenWifiAppPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider packagesProvider);

    void setVoiceInteractionPackagesProvider(com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider packagesProvider);
}
