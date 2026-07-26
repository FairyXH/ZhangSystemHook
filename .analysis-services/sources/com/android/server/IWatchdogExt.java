package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IWatchdogExt {
    default void onProcessBinderCnt() {
    }

    default void getBinderBlockTimeMS() {
    }

    default void processStarted(java.lang.String processName, int pid) {
    }

    default void eventDailyPush() {
    }

    default void checkSystemHeapMem() {
    }

    default void killMultimediaProcess() {
    }

    default boolean shouldGotoDump() {
        return false;
    }

    default void unfreezeForWatchdog() {
    }

    default void dumpStackAndAddDropbox(java.lang.String subject) {
    }

    default void triggerDetect() {
    }

    default void writeEvent(java.lang.String subject) {
    }

    default void setWatchdogHappenValue(boolean value) {
    }

    default boolean isSkipAnrDump() {
        return false;
    }

    default void init(android.content.Context context, com.android.server.am.ActivityManagerService activity) {
    }

    default void addStabilityDebugInAll(boolean halfWatchdog, java.io.File stackFile, java.lang.String subject) {
    }

    default void catchPsAndBinderinfos() {
    }

    default void addBinderPid(java.util.ArrayList<java.lang.Integer> javaList, java.util.ArrayList<java.lang.Integer> nativeList, int pid) {
    }

    default boolean checkIfNeedCloseWdt() {
        return false;
    }

    default void sendTheiaMsg(java.lang.String subject) {
    }

    default void removeTheiaMsg() {
    }

    default void addWatchdogExtNativePids(java.util.HashSet<java.lang.Integer> pids) {
    }
}
