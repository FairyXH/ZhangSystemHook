package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IAppStartInfoTrackerWrapper {
    default boolean hasAppStartupInfo(java.lang.String pkgName, int uid) {
        return true;
    }
}
