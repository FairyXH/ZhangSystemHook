package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IDisplayManagerServiceExt {
    default void init(android.content.Context context) {
    }

    default void onBootComplete(int phase, com.android.server.display.DisplayPowerController dpc, com.android.server.display.DisplayManagerService.SyncRoot syncRoot) {
    }

    default void onSystemReady() {
    }

    default float oplusAdjustBrightness(float brightness) {
        return brightness;
    }

    default void enterDCMode(com.android.server.wm.WindowManagerInternal windowManagerInternal, float brightnessState) {
    }

    default boolean dynamicallyConfigDebug(java.io.PrintWriter pw, java.lang.String[] args) {
        return false;
    }

    default boolean oplusAutoBrightnessAdjustmentSkipCheck(android.content.Context context, int uid) {
        return false;
    }

    default android.view.DisplayInfo getZoomModeDisplayInfo(android.view.DisplayInfo info, int displayId, int callingUid) {
        return null;
    }

    default android.view.DisplayInfo getBacklightTypeDisplayInfo(android.view.DisplayInfo info, int displayId) {
        return null;
    }

    default void initPowerManagement(com.android.server.display.DisplayPowerController dpc) {
    }

    default boolean onDisplayStateChange(int state, int displayState, int displayid, com.android.server.display.LogicalDisplayMapper logicalDisplayMapper) {
        return false;
    }

    default void hookUpdateScreenRecorderState(int uid, java.lang.String packageName, boolean state) {
    }

    default void onStart(android.os.Binder binder) {
    }

    default void setSpecBrightness(int gear, java.lang.String reason, int rate) {
    }

    default boolean addProxyBinder(android.os.IBinder bpBinder, int uid, int pid) {
        return false;
    }

    default boolean removeProxyBinder(android.os.IBinder bpBinder, int uid) {
        return false;
    }

    default boolean requestPowerState(com.android.server.display.LogicalDisplayMapper logicalDisplayMapper, int groupId, android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request, boolean waitForNegativeProximity) {
        return false;
    }

    default void scheduleTraversalLocked(boolean inTraversal) {
    }

    default void handleLogicalDisplayAddedLocked(com.android.server.display.LogicalDisplay display) {
    }

    default void addDisplayPowerControllerLocked(com.android.server.display.LogicalDisplay display, com.android.server.display.DisplayPowerController dpc) {
    }

    default void handleLogicalDisplayChangedLocked(com.android.server.display.LogicalDisplay display) {
    }

    default void handleLogicalDisplayRemovedLocked(com.android.server.display.LogicalDisplay display) {
    }

    default void handleLogicalDisplaySwappedLocked(com.android.server.display.LogicalDisplay display) {
    }

    default void handleLogicalDisplayDeviceStateTransitionLocked(com.android.server.display.LogicalDisplay display) {
    }

    default void setUiHandler(android.os.Handler uiHandler) {
    }

    default void setLogicalDisplayMapper(com.android.server.display.LogicalDisplayMapper logicalDisplayMapper) {
    }

    default void setTemporaryAutoBrightnessAdjustment(float adjustment) {
    }

    default void setActivityPreloadDisplayAdapter(java.util.ArrayList<com.android.server.display.DisplayAdapter> das) {
    }

    default void adjustDisplayPowerRequest(android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request, int displayId) {
    }

    default boolean isBoostDisplayRefreshRateForAnim() {
        return false;
    }

    default void notifyDisplayModeSpecsChanged(int displayId, float refreshrate) {
    }

    default void setBrightnessInfoUid(int uid) {
    }

    default void setBrightnessUid(int uid) {
    }

    default boolean setBrightnessByAccessibility(int uid) {
        return false;
    }

    default boolean isValidBrightness(int displayId, float brightness) {
        return false;
    }

    default void setThreadSchedPolicy(int tid, java.lang.String tidName, int group) {
    }
}
