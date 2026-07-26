package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusDisplayManagerShellCommandExt {
    default boolean customMatchedOnCommand(com.android.server.display.DisplayManagerService service, java.lang.String cmd) {
        return false;
    }

    default int customRunOnCommand(com.android.server.display.DisplayManagerShellCommand shellCommand, java.lang.String cmd) {
        return -1;
    }
}
