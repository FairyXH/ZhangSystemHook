package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IActivityRecordWrapper {
    default java.lang.String getLaunchedFromPackage() {
        return null;
    }

    default java.lang.String getPackageName() {
        return null;
    }

    default int getLaunchedFromPid() {
        return -1;
    }

    default int getLaunchedFromUid() {
        return -1;
    }

    default android.content.pm.ApplicationInfo getAppliationInfo() {
        return null;
    }

    default android.content.Intent getIntent() {
        return null;
    }

    default com.android.server.wm.ActivityRecord getAppToken() {
        return null;
    }

    default java.lang.String getshortComponentName() {
        return null;
    }

    default boolean isActivityTypeHome() {
        return false;
    }

    default int getResultToUserId() {
        return 0;
    }

    default java.lang.String getResultToPackageName() {
        return null;
    }

    default java.lang.String getProcessName() {
        return null;
    }

    default com.android.server.wm.IActivityRecordExt getExtImpl() {
        return new com.android.server.wm.IActivityRecordExt() { // from class: com.android.server.wm.IActivityRecordWrapper.1
        };
    }

    default com.android.server.wm.IActivityRecordSocExt getSocExtImpl() {
        return new com.android.server.wm.IActivityRecordSocExt() { // from class: com.android.server.wm.IActivityRecordWrapper.2
        };
    }

    default android.util.MergedConfiguration getLastReportedConfiguration() {
        return null;
    }

    default boolean shouldRelaunchLocked(int changes, android.content.res.Configuration changesConfig) {
        return false;
    }

    default int getConfigurationChanges(android.content.res.Configuration changesConfig) {
        return 0;
    }

    default android.window.RemoteTransition getPendingRemoteTransition() {
        return null;
    }

    default int getPid() {
        return 0;
    }

    default int getUid() {
        return 0;
    }

    default long getLaunchTickTime() {
        return 0L;
    }

    default boolean isNowVisible() {
        return false;
    }

    default void setLaunchSourceType(int type) {
    }

    default int getStartingWindowType(boolean newTask, boolean taskSwitch, boolean processRunning, boolean allowTaskSnapshot, boolean activityCreated, boolean activityAllDrawn, android.window.TaskSnapshot snapshot) {
        return -1;
    }

    default boolean isOccludeParent() {
        return false;
    }
}
