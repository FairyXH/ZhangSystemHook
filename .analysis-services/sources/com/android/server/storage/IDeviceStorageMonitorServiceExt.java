package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public interface IDeviceStorageMonitorServiceExt {
    default void setForceLevel(int level) {
    }

    default void shellCmdForceFull(android.os.ShellCommand shell, android.content.Context context, java.lang.String permission, android.os.Handler handler) {
    }

    default void dataCheck(android.os.Handler handler) {
    }

    default void onStart(android.os.Handler handler, android.content.Context context, com.android.server.storage.DeviceStorageMonitorService deviceStorageMonitorService) {
    }

    default long getMemoryLowThresholdInternal() {
        return 0L;
    }

    default boolean simulationTest(java.lang.String[] args, java.io.PrintWriter pw) {
        return false;
    }

    default void dumpImpl(java.io.PrintWriter pw) {
    }

    default void setCmdForceLevel(java.lang.String cmd) {
    }

    default void onBootPhase() {
    }
}
