package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IBackgroundLaunchProcessControllerExt {
    default void monitorActivityStartInfo(java.lang.String reason, boolean isCheckingForFgsStart) {
    }

    default void monitorInterceptBgActivityStartInfo(java.lang.String callingPackageName, java.lang.String callingComponent, java.lang.String packageName, java.lang.String activity, int blockType) {
    }
}
