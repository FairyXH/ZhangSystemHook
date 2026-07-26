package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public interface AudioPolicyFacade {
    com.android.media.permission.INativePermissionController getPermissionController();

    boolean isHotwordStreamSupported(boolean z);

    void registerOnStartTask(java.lang.Runnable runnable);
}
