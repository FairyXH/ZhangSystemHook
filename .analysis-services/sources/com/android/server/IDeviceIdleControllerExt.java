package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IDeviceIdleControllerExt {
    public static final long ADVANCE_TIME = 10000;
    public static final int ANY_MOTION = 2;
    public static final boolean DEBUG_OPLUS = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final long DEFAULT_TOTAL_INTERVAL_TO_IDLE = 1800000;
    public static final long DURATION_TRAFFIC_CHECK = 300000;
    public static final long QUICK_ENTER_DEEPSLEEP_DRUATION = 180000;
    public static final int SIGNIFICANT_MOTION = 1;
    public static final boolean mDeepIdleAccordingToRus = true;
    public static final boolean mLightIdleAccordingToRus = true;

    default void init(android.content.Context context) {
    }

    default void onIdleOn(android.util.ArrayMap<java.lang.String, java.lang.Integer> powerSaveWhitelistUserApps, boolean msgIdleOn) {
    }

    default boolean isClosedSuperFirewall() {
        return false;
    }

    default void removePackage(android.content.Intent intent) {
    }

    default void onIdleExit() {
    }

    default void initArgs(com.android.server.DeviceIdleController.Constants mConstants, android.content.Context context, android.os.Handler handler, com.android.server.DeviceIdleController controller) {
    }

    default void dump(java.io.PrintWriter pw) {
    }

    default void hookonBootPhase(int phase, android.content.Context context, com.android.server.DeviceIdleController controller, com.android.server.DeviceIdleController.Constants constants) {
    }

    default void enterSmartDozeIfNeeded(java.lang.String reason) {
    }

    default boolean isInSmartDozeMode(int mState) {
        return false;
    }

    default void initCustomizeDozeModeState() {
    }

    default boolean isCustomizeDozeModeDisabled() {
        return false;
    }

    default void addInvalidDozeWhitelist(java.util.List<java.lang.String> packageNames) {
    }

    default void onShellCommand(android.os.ShellCommand shell) {
    }

    default void onBroadcastIdleState() {
    }

    default boolean shouldIgnoreTempWhitelistChange(int uid, boolean added, boolean isAppOnWhitelist) {
        return false;
    }
}
