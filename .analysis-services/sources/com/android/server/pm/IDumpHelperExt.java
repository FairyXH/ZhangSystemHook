package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IDumpHelperExt {
    default boolean hasOplusPackageName(java.lang.String cmd) {
        return false;
    }

    default boolean customLogicInDump(java.lang.String cmd, java.io.PrintWriter pw, java.lang.String[] args, int opti) {
        return false;
    }
}
