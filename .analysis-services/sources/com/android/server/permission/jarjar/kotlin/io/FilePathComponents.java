package com.android.server.permission.jarjar.kotlin.io;

/* JADX INFO: compiled from: FilePathComponents.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\r\b\u0080\b\u0018\u00002\u00020\u0001B\u001d\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005HÆ\u0003J#\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005HÆ\u0001J\u0013\u0010\u0019\u001a\u00020\b2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u0013HÖ\u0001J\u0016\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001e\u001a\u00020\u0013J\t\u0010\u001f\u001a\u00020\rHÖ\u0001R\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015¨\u0006 "}, d2 = {"Lkotlin/io/FilePathComponents;", "", "root", "Ljava/io/File;", "segments", "", "(Ljava/io/File;Ljava/util/List;)V", "isRooted", "", "()Z", "getRoot", "()Ljava/io/File;", "rootName", "", "getRootName", "()Ljava/lang/String;", "getSegments", "()Ljava/util/List;", "size", "", "getSize", "()I", "component1", "component2", "copy", "equals", "other", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "subPath", "beginIndex", "endIndex", "toString", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class FilePathComponents {
    private final java.io.File root;
    private final java.util.List<java.io.File> segments;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ com.android.server.permission.jarjar.kotlin.io.FilePathComponents copy$default(com.android.server.permission.jarjar.kotlin.io.FilePathComponents filePathComponents, java.io.File file, java.util.List list, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            file = filePathComponents.root;
        }
        if ((i & 2) != 0) {
            list = filePathComponents.segments;
        }
        return filePathComponents.copy(file, list);
    }

    public final java.io.File component1() {
        return this.root;
    }

    public final java.util.List<java.io.File> component2() {
        return this.segments;
    }

    public final com.android.server.permission.jarjar.kotlin.io.FilePathComponents copy(java.io.File file, java.util.List<? extends java.io.File> list) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(file, "root");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "segments");
        return new com.android.server.permission.jarjar.kotlin.io.FilePathComponents(file, list);
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.permission.jarjar.kotlin.io.FilePathComponents)) {
            return false;
        }
        com.android.server.permission.jarjar.kotlin.io.FilePathComponents filePathComponents = (com.android.server.permission.jarjar.kotlin.io.FilePathComponents) obj;
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.root, filePathComponents.root) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.segments, filePathComponents.segments);
    }

    public int hashCode() {
        return (this.root.hashCode() * 31) + this.segments.hashCode();
    }

    public java.lang.String toString() {
        return "FilePathComponents(root=" + this.root + ", segments=" + this.segments + ')';
    }

    /* JADX WARN: Multi-variable type inference failed */
    public FilePathComponents(java.io.File root, java.util.List<? extends java.io.File> list) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(root, "root");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "segments");
        this.root = root;
        this.segments = list;
    }

    public final java.io.File getRoot() {
        return this.root;
    }

    public final java.util.List<java.io.File> getSegments() {
        return this.segments;
    }

    public final java.lang.String getRootName() {
        java.lang.String path = this.root.getPath();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(path, "getPath(...)");
        return path;
    }

    public final boolean isRooted() {
        java.lang.String path = this.root.getPath();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(path, "getPath(...)");
        return path.length() > 0;
    }

    public final int getSize() {
        return this.segments.size();
    }

    public final java.io.File subPath(int beginIndex, int endIndex) {
        if (beginIndex < 0 || beginIndex > endIndex || endIndex > getSize()) {
            throw new java.lang.IllegalArgumentException();
        }
        java.util.List<java.io.File> listSubList = this.segments.subList(beginIndex, endIndex);
        java.lang.String str = java.io.File.separator;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "separator");
        return new java.io.File(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.joinToString$default(listSubList, str, null, null, 0, null, null, 62, null));
    }
}
