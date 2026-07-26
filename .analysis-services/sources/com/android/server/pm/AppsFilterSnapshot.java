package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface AppsFilterSnapshot {
    boolean canQueryPackage(com.android.server.pm.pkg.AndroidPackage androidPackage, java.lang.String str);

    void dumpQueries(java.io.PrintWriter printWriter, java.lang.Integer num, com.android.server.pm.DumpState dumpState, int[] iArr, com.android.internal.util.function.QuadFunction<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Boolean, java.lang.String[]> quadFunction);

    android.util.SparseArray<int[]> getVisibilityAllowList(com.android.server.pm.snapshot.PackageDataSnapshot packageDataSnapshot, com.android.server.pm.pkg.PackageStateInternal packageStateInternal, int[] iArr, android.util.ArrayMap<java.lang.String, ? extends com.android.server.pm.pkg.PackageStateInternal> arrayMap);

    boolean shouldFilterApplication(com.android.server.pm.snapshot.PackageDataSnapshot packageDataSnapshot, int i, java.lang.Object obj, com.android.server.pm.pkg.PackageStateInternal packageStateInternal, int i2);
}
