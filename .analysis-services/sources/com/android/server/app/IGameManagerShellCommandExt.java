package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
public interface IGameManagerShellCommandExt {
    default int onCommandExt(java.io.PrintWriter pw) {
        return -1;
    }

    default void onHelp(java.io.PrintWriter pw) {
    }
}
