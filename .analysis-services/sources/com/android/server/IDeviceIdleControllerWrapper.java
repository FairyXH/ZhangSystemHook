package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IDeviceIdleControllerWrapper {
    default int addPowerSaveWhitelistAppsInternal(java.util.List<java.lang.String> pkgNames) {
        return 0;
    }

    default int getState() {
        return 0;
    }

    default void setState(int state) {
    }

    default android.util.ArrayMap<java.lang.String, java.lang.Integer> getPowerSaveWhitelistUserApps() {
        return new android.util.ArrayMap<>();
    }

    default boolean getDeepEnabled() {
        return false;
    }

    default void setDeepEnabled(boolean enabled) {
    }

    default boolean getLightEnabled() {
        return false;
    }

    default void setLightEnabled(boolean enabled) {
    }

    default void setActiveReason(int reason) {
    }

    default void addPowerSaveWhitelistApps(android.util.ArrayMap<java.lang.String, java.lang.Integer> powerSaveList) {
    }
}
