package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusDeviceIdleHelper extends android.common.IOplusCommonFeature {
    public static final com.android.server.IOplusDeviceIdleHelper DEFAULT = new com.android.server.IOplusDeviceIdleHelper() { // from class: com.android.server.IOplusDeviceIdleHelper.1
    };
    public static final java.lang.String NAME = "IOplusDeviceIdleHelper";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusDeviceIdleHelper;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void initArgs(android.content.Context context, com.android.server.DeviceIdleController controller, com.android.server.DeviceIdleController.Constants constants) {
        android.util.Slog.d(NAME, "interface do initArgs");
    }

    default boolean isAutoPowerModesEnabled() {
        return true;
    }

    default long getTotalIntervalToIdle() {
        return 1800000L;
    }

    default void onMotionDetected(int state, int typeMotion) {
    }

    default void onDeepIdleOn(java.util.ArrayList<java.lang.String> listPowerSaveUser) {
    }

    default void onLightIdleOn(java.util.ArrayList<java.lang.String> listPowerSaveUser) {
    }

    default void onIdleExit() {
    }

    default boolean onScreenOff() {
        return false;
    }

    default void onScreenOn() {
    }

    default void dump(java.io.PrintWriter pw) {
    }

    default void motionDetected(int mState, int type) {
    }

    default void updateLastLightTrafficRecord() {
    }

    default void updateLastDeepTrafficRecord() {
    }

    default boolean isDeepInTraffic() {
        return false;
    }

    default boolean isLightInTraffic() {
        return false;
    }

    default void enterDeepSleepQuickly() {
    }

    default void removePackage(android.content.Intent intent) {
    }

    default boolean isInited() {
        return false;
    }

    default void addPowerSaveWhitelist(java.lang.String pkgname) {
    }

    default void addPowerSaveWhitelistExceptIdle(java.lang.String pkgname) {
    }

    default boolean getOpenDeviceIdleSwitch() {
        return false;
    }

    default boolean getGoogleRestrictSwitch() {
        return false;
    }

    default void addInvalidDozeWhitelist(java.util.List<java.lang.String> packageNames) {
    }

    default void removeInvalidDozeWhitelist(java.lang.String name) {
    }

    default java.util.List<java.lang.String> getInValidDozeWhitelist() {
        return new java.util.ArrayList();
    }

    default void setDebugSwitch(boolean b) {
    }

    default void addPowerSaveWhitelistAllFromSystemConfig() {
    }

    default boolean shouldIgnoreTempWhitelistChange(int uid, boolean added, boolean isAppOnWhitelist) {
        return false;
    }

    default boolean shouldIgnoreTempWhitelistChange(int uid, java.lang.String pkgName, boolean added, boolean isAppOnWhitelist) {
        return false;
    }
}
