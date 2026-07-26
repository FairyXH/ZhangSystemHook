package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public interface IPackagePreferencesWrapper {
    default com.android.server.notification.IPackagePreferencesExt getPackagePreferencesExt() {
        return null;
    }

    default int getImportance() {
        return 0;
    }
}
