package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IVerifyingSessionWrapper {
    default int getRet() {
        return 1;
    }

    default android.content.pm.parsing.PackageLite getPackageLite() {
        return null;
    }

    default com.android.server.pm.InstallSource getInstallSource() {
        return null;
    }

    default android.os.UserHandle getUser() {
        return null;
    }
}
