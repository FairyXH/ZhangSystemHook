package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IDisplayWindowSettingsExt {
    default boolean skipSetWindowingMode(com.android.server.wm.DisplayContent dc, boolean includeRotationSettings, int windowingMode) {
        return false;
    }
}
