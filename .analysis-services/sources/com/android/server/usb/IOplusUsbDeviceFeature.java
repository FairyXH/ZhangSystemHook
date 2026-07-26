package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public interface IOplusUsbDeviceFeature extends android.common.IOplusCommonFeature {
    public static final com.android.server.usb.IOplusUsbDeviceFeature DEFAULT = new com.android.server.usb.IOplusUsbDeviceFeature() { // from class: com.android.server.usb.IOplusUsbDeviceFeature.1
    };
    public static final java.lang.String NAME = "IOplusUsbDeviceFeature";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusUsbDeviceFeature;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void init(android.content.Context context, android.os.Handler handler) {
        android.util.Log.d(NAME, "default init");
    }

    default boolean getMuiltiUserSwitch() {
        android.util.Log.d(NAME, "default getMuiltiUserSwitch");
        return false;
    }

    default void setMuiltiUserSwitch(boolean value) {
        android.util.Log.d(NAME, "default setMuiltiUserSwitch");
    }

    default void setUsbAccessoryStartFlag() {
        android.util.Log.d(NAME, "default setUsbAccessoryStartFlag");
    }

    default void initUsbAccessoryStartFlag() {
        android.util.Log.d(NAME, "default initUsbAccessoryStartFlag");
    }

    default boolean getUsbAccessoryStartFlag(long functions) {
        android.util.Log.d(NAME, "default getUsbAccessoryStartFlag");
        return false;
    }

    default void initUsbPlugFlag() {
        android.util.Log.d(NAME, "default initUsbPlugFlag");
    }

    default void setOplusUsbDeviceManagerCallback(com.android.server.usb.IOplusUsbDeviceManagerCallback callback) {
        android.util.Log.d(NAME, "default setOplusUsbDeviceManagerCallback");
    }

    default void setUsbPlugFlag(boolean connected) {
        android.util.Log.d(NAME, "default setUsbPlugFlag");
    }

    default boolean usbFunctionsShuoldUseDefault(java.lang.String functions) {
        android.util.Log.d(NAME, "default usbFunctionsShuoldUseDefault");
        return false;
    }

    default void printFinishBootInfo(com.android.server.usb.OplusUsbDeviceFinishBootInfo bootInfo) {
        android.util.Log.d(NAME, "default printFinishBootInfo");
    }

    default long getChargingFunctions() {
        android.util.Log.d(NAME, "default getChargingFunctions");
        return 0L;
    }

    default boolean isTelecomRequirement(java.lang.String functions) {
        android.util.Log.d(NAME, "default isTelecomRequirement");
        return false;
    }

    default void printBootModeForDebug(java.lang.String bootMode) {
        android.util.Log.d(NAME, "default printBootModeForDebug");
    }

    default boolean isNormalBoot() {
        android.util.Log.d(NAME, "default isNormalBoot");
        return false;
    }

    default void printFunctionsForDebug(com.android.server.usb.OplusUsbDeviceFunctionInfo functionInfo) {
        android.util.Log.d(NAME, "default printFunctionsForDebug");
    }

    default void usbSwitchModeCallerRecord(java.lang.String callerPkg, java.lang.String type) {
        android.util.Log.d(NAME, "default usbSwitchModeCallerRecord");
    }

    default void usbEnterAccessoryTimeoutRecord(java.lang.String reason, java.lang.String type) {
        android.util.Log.d(NAME, "default usbEnterAccessoryTimeoutRecord");
    }

    default void usbAoaHandshakeTimeoutRecord(java.lang.String reason, java.lang.String type) {
        android.util.Log.d(NAME, "default usbAoaHandshakeTimeoutRecord");
    }

    default void usbMtkSetFunctionTimeoutRecord(java.lang.String reason, java.lang.String type) {
        android.util.Log.d(NAME, "default usbMtkSetFunctionTimeoutRecord");
    }

    default void usbMtkSwitchFunctionTimeoutRecord(java.lang.String reason, java.lang.String type) {
        android.util.Log.d(NAME, "default usbMtkSwitchFunctionTimeoutRecord");
    }

    default void usbSetFunctionFailedRecord(java.lang.String state, java.lang.String propertyValue, java.lang.String type) {
        android.util.Log.d(NAME, "default usbSetFunctionFailedRecord");
    }

    default void usbGadgetServiceStatusRecord(java.lang.String reason, java.lang.String type) {
        android.util.Log.d(NAME, "default usbGadgetServiceStatusRecord");
    }

    default void usbAdbFeatureStatusRecord(android.content.Context context, java.lang.String status, java.lang.String type) {
        android.util.Log.d(NAME, "default usbAdbFeatureStatusRecord");
    }

    default void usbHostRecord(android.content.Context context, android.hardware.usb.UsbDevice newDevice) {
        android.util.Log.d(NAME, "default usbHostRecord");
    }

    default void processUserTestHarnessIfNeed(android.content.Context context) {
        android.util.Log.d(NAME, "default processUserTestHarnessIfNeed");
    }

    default long usbTetheringSwitchOffFunctions(long usbFunctions, java.lang.String currentFunctionsStr) {
        android.util.Log.d(NAME, "default usbTetheringSwitchOffFunctions");
        return 0L;
    }

    default boolean isUsbTetheringDisabled(android.content.Context context) {
        android.util.Log.d(NAME, "default isUsbTetheringDisabled");
        return false;
    }

    default void printCallerPkg(int currentPid) {
        android.util.Log.d(NAME, "default getCallerInfo");
    }

    default void sendUserSwitchBroadcast(boolean connected, boolean configured, int currentUser) {
        android.util.Log.d(NAME, "default getCallerInfo");
    }

    default void sendPortChangeMessage(android.hardware.usb.UsbPortStatus usbPortStatus, boolean isInitializing) {
        android.util.Log.d(NAME, "default sendPortChangeMessage");
    }

    default java.lang.String getUsbCurrentEyeDiagram(int model) {
        android.util.Log.d(NAME, "default getUsbCurrentEyeDiagram");
        return "";
    }

    default int setUsbEyeDiagram(int model, java.lang.String eyeDiagram, boolean isDefaultEyeDiagram) {
        android.util.Log.d(NAME, "default setUsbEyeDiagram");
        return 0;
    }
}
