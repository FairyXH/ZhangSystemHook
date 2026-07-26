package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public interface IDevicePolicyManagerServiceExt {
    default int checkPakcageState(android.content.Context context, android.content.ComponentName componentName) {
        return 0;
    }

    default boolean isCustomDevicePolicyEnabled(android.content.Context context) {
        return false;
    }

    default boolean isDisabledDeactivateMdmPackage(android.content.Context context, java.lang.String packageName) {
        return false;
    }

    default boolean shouldShiftToNullParamForGetCallerIdentity(android.content.Context context, android.content.ComponentName componentName) {
        return false;
    }

    default boolean shouldSkipDumpPerUserData() {
        return false;
    }

    default void updateSwitchState(boolean state) {
    }

    default void updateWhiteList(java.util.List<java.lang.String> whiteList) {
    }

    default java.lang.String getCustCallerPackage(android.content.ComponentName who, java.lang.String callerPackage) {
        return callerPackage;
    }

    default boolean isCustInvokeInterface(android.content.Context context) {
        return false;
    }

    default void registerForDateChanged(android.content.Context context) {
    }
}
