package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IBatteryStatsServiceExt {
    default void init(android.content.Context context) {
    }

    default void systemServicesReady() {
    }

    default boolean dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        return false;
    }

    default void setDumpParam(int flags, long historyStart) {
    }

    default void setThermalState(java.lang.Object thermalState) {
    }

    default void noteStartSensor(int uid, int sensor) {
    }

    default void noteStopSensor(int uid, int sensor) {
    }

    default void noteStartCamera(int uid) {
    }

    default void noteStopCamera(int uid) {
    }

    default void noteResetCamera() {
    }

    default void noteStartVideo(int uid) {
    }

    default void noteStopVideo(int uid) {
    }

    default void noteGpsChanged(android.os.WorkSource oldWs, android.os.WorkSource newWs) {
    }

    default void noteStartAudio(int uid) {
    }

    default void noteStopAudio(int uid) {
    }

    default void noteResetAudio() {
    }

    default void noteResetVideo() {
    }

    default void noteBleScanStarted(android.os.WorkSource ws, boolean isUnoptimized) {
    }

    default void noteBleScanStopped(android.os.WorkSource ws, boolean isUnoptimized) {
    }

    default void noteBleScanReset() {
    }

    default void noteWifiScanStarted(int uid) {
    }

    default void noteWifiScanStopped(int uid) {
    }

    default void noteWifiScanStartedFromSource(android.os.WorkSource ws) {
    }

    default void noteWifiScanStoppedFromSource(android.os.WorkSource ws) {
    }

    default void noteWifiMulticastEnabled(int uid) {
    }

    default void noteWifiMulticastDisabled(int uid) {
    }

    default boolean isDumpBatteryStatsDetail() {
        return false;
    }
}
