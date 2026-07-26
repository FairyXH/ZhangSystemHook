package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public interface Injector {
    com.android.server.location.injector.AlarmHelper getAlarmHelper();

    com.android.server.location.injector.AppForegroundHelper getAppForegroundHelper();

    com.android.server.location.injector.AppOpsHelper getAppOpsHelper();

    com.android.server.location.injector.DeviceIdleHelper getDeviceIdleHelper();

    com.android.server.location.injector.DeviceStationaryHelper getDeviceStationaryHelper();

    com.android.server.location.injector.EmergencyHelper getEmergencyHelper();

    com.android.server.location.injector.LocationPermissionsHelper getLocationPermissionsHelper();

    com.android.server.location.injector.LocationPowerSaveModeHelper getLocationPowerSaveModeHelper();

    com.android.server.location.settings.LocationSettings getLocationSettings();

    com.android.server.location.injector.LocationUsageLogger getLocationUsageLogger();

    com.android.server.location.injector.PackageResetHelper getPackageResetHelper();

    com.android.server.location.injector.ScreenInteractiveHelper getScreenInteractiveHelper();

    com.android.server.location.injector.SettingsHelper getSettingsHelper();

    com.android.server.location.injector.UserInfoHelper getUserInfoHelper();
}
