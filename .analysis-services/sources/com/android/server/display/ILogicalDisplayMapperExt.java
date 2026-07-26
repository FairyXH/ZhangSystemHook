package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface ILogicalDisplayMapperExt {
    default void setDisplayLayout(android.util.SparseArray<com.android.server.display.layout.Layout> map) {
    }

    default void notifyDisplaySwaped() {
    }

    default void setPowerHandler(android.os.Handler handler) {
    }

    default void transitionToPendingStateLocked() {
    }

    default boolean updateLogicalDisplaysLocked(com.android.server.display.LogicalDisplay display) {
        return false;
    }

    default void resetPowerModeChanged(com.android.server.display.LogicalDisplay display) {
    }

    default boolean hasFoldRemapDisplayDisableFeature() {
        return false;
    }

    default int interceptBaseDeviceState(int pendingState, int newState) {
        return newState;
    }

    default boolean avoidRemoveInternalDisplay(int displayId) {
        return false;
    }

    default void setDisableDisplayOff(boolean bootCompleted, android.view.DisplayInfo newDisplayInfo, com.android.server.display.DisplayDevice displayDevice, android.os.Handler handler) {
    }

    default boolean filterSecondaryDisplay(android.content.Context context, int displayId, int phase, int callingUid) {
        return false;
    }

    default boolean getOverrideState(boolean displayLayoutStatus, com.android.server.display.LogicalDisplay newDisplay) {
        return displayLayoutStatus;
    }

    default boolean isRemapDisabledSecondaryDisplayId(int displayId) {
        return false;
    }

    default void fastFreezeOnWakeup(int deviceState, int pendingDeviceState) {
    }

    default void setUxOnWakeup(int deviceState, int pendingDeviceState) {
    }

    default void screenOnCpuBoost(int deviceState, int pendingDeviceState) {
    }

    default void initDvMultiDisplay() {
    }

    default void updateDvsParam(int foldState) {
    }

    default void setMainDisplayUniqueId(java.lang.String uniqueueId) {
    }

    default boolean getOplusSleepDevice(boolean sleepDevice, android.content.Context context, int pendingState, int curState) {
        return false;
    }
}
