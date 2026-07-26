package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface PackageSessionProvider {
    com.android.server.pm.GentleUpdateHelper getGentleUpdateHelper();

    com.android.server.pm.PackageInstallerSession getSession(int i);

    com.android.server.pm.PackageSessionVerifier getSessionVerifier();
}
