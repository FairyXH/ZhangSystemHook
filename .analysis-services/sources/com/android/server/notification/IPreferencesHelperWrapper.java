package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface IPreferencesHelperWrapper {
    default com.android.server.notification.IPreferencesHelperExt getPreferencesHelperExt() {
        return null;
    }

    default void updateConfig() {
    }

    default com.android.server.notification.IPackagePreferencesExt getOrCreatePackagePreferencesExt(java.lang.String pkg, int uid) {
        return null;
    }

    default int getImportanceOfPackage(java.lang.String pkg, int uid) {
        return 0;
    }
}
