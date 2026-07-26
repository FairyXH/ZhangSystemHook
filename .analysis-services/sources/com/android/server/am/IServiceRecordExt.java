package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IServiceRecordExt {
    public static final int callingPid = -1;
    public static final int callingUid = 0;
    public static final java.lang.String type = "";

    default int getCallingPid() {
        return -1;
    }

    default java.lang.String getType() {
        return "";
    }

    default void setCallingPid(int callingPid2) {
    }

    default void setType(java.lang.String type2) {
    }

    default int getCallingUid() {
        return 0;
    }

    default void setCallingUid(int callingUid2) {
    }

    default void setCallerAppPackage(java.lang.String callingPackage) {
    }

    default java.lang.String getCallerAppPackage() {
        return null;
    }

    default void setExceptionWhenBringUp(boolean set) {
    }

    default boolean getExceptionWhenBringUp() {
        return false;
    }

    default void incRestartDelayPromoteCount() {
    }

    default void resetRestartDelayPromoteCount() {
    }

    default int getRestartDelayPromoteCount() {
        return 0;
    }

    default void resetRestartSchedulingCount() {
    }

    default void incRestartSchedulingCount() {
    }

    default int getRestartSchedulingCount() {
        return -1;
    }

    default void hookEndOfDump(java.io.PrintWriter pw, java.lang.String prefix) {
    }

    default boolean updateRestartMask(boolean add, int mask) {
        return false;
    }

    default int getRestartMask() {
        return 0;
    }

    default void setCallingPackageName(java.lang.String callingPackageName) {
    }

    default java.lang.String getCallingPackageName() {
        return null;
    }
}
