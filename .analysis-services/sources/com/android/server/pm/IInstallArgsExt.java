package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IInstallArgsExt {
    default void init(com.android.server.pm.IInstallParamsExt installParamsExt) {
    }

    default int getExtraInstallFlags() {
        return 0;
    }

    default java.lang.String getExtraSessionInfo() {
        return null;
    }

    default java.lang.String getPackageName() {
        return null;
    }

    default void setPackageName(java.lang.String pkgName) {
    }

    default int setExtraDextopFlags(int flags) {
        return 0;
    }

    default int getExtraDextopFlags() {
        return 0;
    }
}
