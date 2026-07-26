package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackageManagerShellCommandExt {
    default boolean customMatchedOnCommand(java.lang.String cmd) {
        return false;
    }

    default int customLogicOnCommand(com.android.server.pm.PackageManagerShellCommand shellCommand, java.lang.String cmd) {
        return -1;
    }

    default int customLogicOnRunList(com.android.server.pm.PackageManagerShellCommand shellCommand, java.lang.String type) {
        return -1;
    }

    default boolean interceptInDoRunInstall(com.android.server.pm.PackageManagerShellCommand shellCommand) {
        return false;
    }

    default boolean interceptInRunUninstall(com.android.server.pm.PackageManagerShellCommand shellCommand) {
        return false;
    }
}
