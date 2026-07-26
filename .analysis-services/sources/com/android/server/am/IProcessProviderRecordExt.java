package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IProcessProviderRecordExt {
    default boolean checkIfAlwaysCleanupAppInLaunchingProviders(boolean allowRestart) {
        return false;
    }

    default boolean checkIfAlwaysCleanupAppInLaunchingProviders(android.content.Context context, com.android.server.am.ProcessRecord app, boolean allowRestart) {
        return false;
    }
}
