package com.android.server.backup.fullbackup;

/* JADX INFO: loaded from: classes.dex */
public interface IFullBackupEngineExt {
    default void setHasSameAgentTask(boolean hasNextPackage) {
    }

    default boolean hasSameAgentTask(com.android.server.backup.UserBackupManagerService userBms, android.content.pm.ApplicationInfo applicationInfo) {
        return false;
    }

    public interface IStaticExt {
        default void routeSocketDataToOutput(android.os.ParcelFileDescriptor inPipe, java.io.OutputStream out, java.lang.String packageName) throws java.io.IOException {
        }
    }
}
