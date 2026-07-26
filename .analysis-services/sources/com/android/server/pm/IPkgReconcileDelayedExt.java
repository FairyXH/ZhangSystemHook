package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IPkgReconcileDelayedExt {
    public static final com.android.server.pm.IPkgReconcileDelayedExt DEFAULT = new com.android.server.pm.IPkgReconcileDelayedExt() { // from class: com.android.server.pm.IPkgReconcileDelayedExt.1
    };

    default boolean canDelayPrepareAppData(com.android.server.pm.pkg.PackageStateInternal ps) {
        return false;
    }

    default void markDelayPrepareAppData(com.android.server.pm.pkg.PackageStateInternal ps) {
    }

    default void asyncDelayedPrepareWork(com.android.server.pm.PackageManagerService pms) {
    }
}
