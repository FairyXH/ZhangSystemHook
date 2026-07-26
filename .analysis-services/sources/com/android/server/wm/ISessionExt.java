package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ISessionExt {
    default void hookrelayout(android.util.MergedConfiguration mergedConfiguration, java.lang.String packageName) {
    }

    default void setOplusSafeWindowPermission(com.android.server.wm.WindowManagerService service) {
    }

    default boolean hasOplusSafeWindowPermission() {
        return false;
    }

    default void setOplusWallpaperUpdatePermission(com.android.server.wm.WindowManagerService service) {
    }

    default boolean hasOplusWallpaperUpdatePermission() {
        return false;
    }
}
