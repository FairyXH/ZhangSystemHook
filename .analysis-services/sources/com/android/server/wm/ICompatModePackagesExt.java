package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ICompatModePackagesExt {
    default void overrideCompatInfoIfNeed(android.content.pm.ApplicationInfo ai) {
    }

    default float getCompatScale(java.lang.String packageName, int uid) {
        return 1.0f;
    }
}
