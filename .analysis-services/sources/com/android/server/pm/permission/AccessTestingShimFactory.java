package com.android.server.pm.permission;

/* JADX INFO: loaded from: classes2.dex */
public class AccessTestingShimFactory {
    public static final java.lang.String DEVICE_CONFIG_SETTING = "selected_access_subsystem";
    private static final int RUN_BOTH_SUBSYSTEMS = 2;
    private static final int RUN_NEW_SUBSYSTEM = 1;
    private static final int RUN_OLD_SUBSYSTEM = 0;

    public static com.android.server.pm.permission.PermissionManagerServiceInterface getPms(android.content.Context context, java.util.function.Supplier<com.android.server.pm.permission.PermissionManagerServiceInterface> oldImpl, java.util.function.Supplier<com.android.server.pm.permission.PermissionManagerServiceInterface> newImpl) {
        int selectedSystem = android.provider.DeviceConfig.getInt("privacy", DEVICE_CONFIG_SETTING, 0);
        switch (selectedSystem) {
            case 1:
                return newImpl.get();
            case 2:
                return new com.android.server.pm.permission.PermissionManagerServiceTestingShim(oldImpl.get(), newImpl.get());
            default:
                return oldImpl.get();
        }
    }

    public static com.android.server.appop.AppOpsCheckingServiceInterface getAos(android.content.Context context, java.util.function.Supplier<com.android.server.appop.AppOpsCheckingServiceInterface> oldImpl, java.util.function.Supplier<com.android.server.appop.AppOpsCheckingServiceInterface> newImpl) {
        int selectedSystem = android.provider.DeviceConfig.getInt("privacy", DEVICE_CONFIG_SETTING, 0);
        switch (selectedSystem) {
            case 1:
                return newImpl.get();
            case 2:
                return new com.android.server.appop.AppOpsServiceTestingShim(oldImpl.get(), newImpl.get());
            default:
                return oldImpl.get();
        }
    }
}
