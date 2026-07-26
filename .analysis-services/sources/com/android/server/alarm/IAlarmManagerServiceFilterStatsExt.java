package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public interface IAlarmManagerServiceFilterStatsExt {
    default void setNumWakeupWhenScreenoff(int numWakeupWhenScreenoff) {
    }

    default int getNumWakeupWhenScreenoff() {
        return 0;
    }

    default void setNumWakeupWhenReset(int numWakeupWhenReset) {
    }

    default int getNumWakeupWhenReset() {
        return 0;
    }
}
