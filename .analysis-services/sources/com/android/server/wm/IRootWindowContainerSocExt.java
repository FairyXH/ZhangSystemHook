package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IRootWindowContainerSocExt {
    default void acquireAppLaunchPerfLock(com.android.server.wm.ActivityRecord r, com.android.server.wm.ActivityTaskManagerService service) {
    }

    default void acquireUxPerfLock(int opcode, java.lang.String packageName) {
    }
}
