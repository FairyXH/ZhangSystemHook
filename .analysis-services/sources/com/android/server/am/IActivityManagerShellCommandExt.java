package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IActivityManagerShellCommandExt {
    default boolean isAllowedForcestop(java.lang.String pkgName) {
        return true;
    }
}
