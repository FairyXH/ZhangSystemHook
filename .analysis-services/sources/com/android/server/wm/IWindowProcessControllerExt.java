package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowProcessControllerExt {
    default boolean hookappEarlyNotRespondingForAging() {
        return false;
    }

    default void hookappEarlyNotRespondingPrecess(com.android.server.wm.ActivityTaskManagerService mAtm) {
    }

    default boolean hookappNotRespondingForAgine() {
        return false;
    }

    default void hookappNotRespondingProcess(com.android.server.wm.ActivityTaskManagerService mAtm) {
    }

    default boolean hookUpdateRapidActivityLaunchSkipApp(java.lang.String pkgName) {
        return false;
    }

    default boolean isConfigChange(android.content.res.Configuration config, android.content.res.Configuration lastConfig, android.content.pm.ApplicationInfo info) {
        return false;
    }

    default boolean shouldMakeActivityFinishing(java.lang.String pkgName, int userId) {
        return true;
    }

    default boolean getFirstTransferState() {
        return true;
    }

    default void setFirstTransferState() {
    }

    default void resolveOverrideConfiguration(android.content.res.Configuration resolvedConfig, com.android.server.wm.ActivityRecord configActivityRecord) {
    }

    default void updateWaitActivityToAttach(boolean waiting) {
    }

    default boolean waitActivityToAttach() {
        return false;
    }

    default void handleAddActivity(com.android.server.wm.WindowProcessController wpc, boolean first) {
    }

    default void handleRemoveActivity(com.android.server.wm.WindowProcessController wpc, boolean hasActivityBeforeRemove) {
    }

    default com.android.server.wm.ActivityRecord getTopActivity() {
        return null;
    }

    default boolean canSetPreQTopResumedActivity(com.android.server.wm.TaskFragment taskFrag, int targetSdkVersion) {
        return false;
    }

    default boolean shouldUpdateProcessConfig(com.android.server.wm.WindowProcessController proc, com.android.server.wm.ActivityTaskManagerService atm) {
        return false;
    }

    default boolean hasActivityInVisibleTask() {
        return false;
    }

    default boolean hasResumedActivity() {
        return false;
    }
}
