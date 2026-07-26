package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IIntentBindRecordExt {
    default long getBeginTime() {
        return -1L;
    }

    default void setBeginTime(long beginTime) {
    }

    default long getScheduleBindTime() {
        return -1L;
    }

    default void setScheduleBindTime(long scheduleBindTime) {
    }

    default int getProcState() {
        return -1;
    }

    default void setProcState(int procState) {
    }

    default boolean getReported() {
        return true;
    }

    default void setReported(boolean reported) {
    }
}
