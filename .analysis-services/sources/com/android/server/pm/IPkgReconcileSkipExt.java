package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPkgReconcileSkipExt {
    public static final com.android.server.pm.IPkgReconcileSkipExt DEFAULT = new com.android.server.pm.IPkgReconcileSkipExt() { // from class: com.android.server.pm.IPkgReconcileSkipExt.1
    };

    default boolean skipPrepareAppData(com.android.server.pm.pkg.PackageStateInternal ps) {
        return false;
    }
}
