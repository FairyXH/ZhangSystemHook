package com.android.server.permission.jarjar.kotlin.io.path;

/* JADX INFO: compiled from: PathUtils.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lkotlin/io/path/PathRelativizer;", "", "()V", "emptyPath", "Ljava/nio/file/Path;", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "parentPath", "tryRelativeTo", "path", "base", "kotlin-stdlib-jdk7"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class PathRelativizer {
    public static final com.android.server.permission.jarjar.kotlin.io.path.PathRelativizer INSTANCE = new com.android.server.permission.jarjar.kotlin.io.path.PathRelativizer();
    private static final java.nio.file.Path emptyPath = java.nio.file.Paths.get("", new java.lang.String[0]);
    private static final java.nio.file.Path parentPath = java.nio.file.Paths.get("..", new java.lang.String[0]);

    private PathRelativizer() {
    }

    public final java.nio.file.Path tryRelativeTo(java.nio.file.Path path, java.nio.file.Path base) {
        java.nio.file.Path path2;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(path, "path");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(base, "base");
        java.nio.file.Path bn = base.normalize();
        java.nio.file.Path pn = path.normalize();
        java.nio.file.Path rn = bn.relativize(pn);
        int iMin = java.lang.Math.min(bn.getNameCount(), pn.getNameCount());
        for (int i = 0; i < iMin && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(bn.getName(i), parentPath); i++) {
            if (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(pn.getName(i), parentPath)) {
                throw new java.lang.IllegalArgumentException("Unable to compute relative path");
            }
        }
        if (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(pn, bn) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(bn, emptyPath)) {
            path2 = pn;
        } else {
            java.lang.String rnString = rn.toString();
            java.lang.String separator = rn.getFileSystem().getSeparator();
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(separator, "getSeparator(...)");
            if (com.android.server.permission.jarjar.kotlin.text.StringsKt.endsWith$default(rnString, separator, false, 2, (java.lang.Object) null)) {
                path2 = rn.getFileSystem().getPath(com.android.server.permission.jarjar.kotlin.text.StringsKt.dropLast(rnString, rn.getFileSystem().getSeparator().length()), new java.lang.String[0]);
            } else {
                path2 = rn;
            }
        }
        java.nio.file.Path r = path2;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(r);
        return r;
    }
}
