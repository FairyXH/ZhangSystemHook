package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IPinnerServiceExt {
    default void updateExt(android.util.ArraySet<java.lang.String> updatedPackages, boolean force) {
    }

    default java.lang.String[] replaceDefaultFiles(java.lang.String[] defaultFilesToPin) {
        return defaultFilesToPin;
    }

    default int customizePinLauncherBytes(int defaultPinBytes) {
        return defaultPinBytes;
    }
}
