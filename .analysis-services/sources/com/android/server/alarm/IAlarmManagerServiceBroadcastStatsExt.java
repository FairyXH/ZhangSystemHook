package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public interface IAlarmManagerServiceBroadcastStatsExt {
    default void setNumWakeupScreenoff(int numWakeupScreenoff) {
    }

    default void addNumWakeupScreenoff(int addVal) {
    }

    default int getNumWakeupScreenoff() {
        return 0;
    }

    default void setNumWakeupWhenReset(int numWakeupWhenReset) {
    }

    default int getNumWakeupWhenReset() {
        return 0;
    }

    default void setLastTimeWakeup(long lastTimeWakeup) {
    }

    default long getLastTimeWakeup() {
        return 0L;
    }

    default void setTimestampWakupCountReset(long timestampWakupCountReset) {
    }

    default long getTimestampWakupCountReset() {
        return 0L;
    }

    default void setNumCanceledWakeup(int numCanceledWakeup) {
    }

    default void addNumCanceledWakeup(int addVal) {
    }

    default int getNumCanceledWakeup() {
        return 0;
    }

    default void setNumRealWakeupScreeoff(int numRealWakeupScreeoff) {
    }

    default void addNumRealWakeupScreeoff(int addVal) {
    }

    default int getNumRealWakeupScreeoff() {
        return 0;
    }

    default void setNumRealWakeupWhenReset(int numRealWakeupWhenReset) {
    }

    default int getNumRealWakeupWhenReset() {
        return 0;
    }
}
