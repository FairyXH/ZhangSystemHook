package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IProcessRecordExt {
    public static final int UX_STATE_BG = 1;
    public static final int UX_STATE_DEFAULT = 0;
    public static final int UX_STATE_TOP = 3;
    public static final int UX_STATE_VISIBLE = 2;

    default boolean getIsANR() {
        return false;
    }

    default void setIsANR(boolean ANR) {
    }

    default boolean isRPLaunch() {
        return false;
    }

    default void setRPLaunch(boolean rpLaunch) {
    }

    default void setExplicitDisableRestart(boolean disable) {
    }

    default boolean isExplicitDisableRestart() {
        return false;
    }

    default boolean isWaitingPermissionChoice() {
        return false;
    }

    default void setWaitingPermissionChoice(boolean isWaitingPermissionChoice) {
    }

    default int getOplusReceiverRecordListSize() {
        return 0;
    }

    default java.lang.String getAnrAnnotation() {
        return null;
    }

    default void setAnrAnnotation(java.lang.String annotation) {
    }

    default void detectForgroundExceptions(java.lang.String[] mPackageList, android.content.Context mContext, int mCurProcState, java.lang.String reason) {
    }

    default void callOrmsSetSceneActionForRemoteAnimation(boolean isFinish) {
    }

    default void saveAmKillRecordToList(long dateTime, int pid, java.lang.String processName, java.lang.String reason) {
    }

    default int getOldSchedGroup() {
        return 0;
    }

    default void setOldSchedGroup(int schedGroup) {
    }

    default void createProcessInfo(com.android.server.am.ProcessRecord app) {
    }

    default void updateProcessState(com.android.server.am.ProcessRecord app, int procState) {
    }

    default void updateExecutingComponent(com.android.server.am.ProcessRecord pr, java.lang.String cpnName, int eventType) {
    }

    default void dump(java.io.PrintWriter pw, java.lang.String prefix, long nowUptime) {
    }

    default java.lang.String getExecutingCpn() {
        return null;
    }

    default void setVirtualFreeze(boolean freeze) {
    }

    default boolean getVirtualFreeze() {
        return false;
    }

    default void resetUxState() {
    }

    default int getUxState() {
        return 0;
    }

    default void setUxState(int uxState) {
    }

    default void setAttachApplicationDone(boolean done) {
    }

    default boolean isAttachApplicationDone() {
        return false;
    }

    default void onStartActivity(com.android.server.am.ProcessRecord app, java.lang.String packageName) {
    }

    default void makeInactive(com.android.server.am.ProcessRecord app) {
    }
}
