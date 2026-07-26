package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
interface PackageSender {
    void notifyPackageAdded(java.lang.String str, int i);

    void notifyPackageChanged(java.lang.String str, int i);

    void notifyPackageRemoved(java.lang.String str, int i);
}
