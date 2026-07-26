package com.android.server.permission.jarjar.kotlin.io.path;

/* JADX INFO: compiled from: PathTreeWalk.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010(\n\u0002\b\u000b\b\u0002\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0001\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0000¢\u0006\u0002\u0010\u0006R\"\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0000\u0018\u00010\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0000¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0013"}, d2 = {"Lkotlin/io/path/PathNode;", "", "path", "Ljava/nio/file/Path;", "key", "parent", "(Ljava/nio/file/Path;Ljava/lang/Object;Lkotlin/io/path/PathNode;)V", "contentIterator", "", "getContentIterator", "()Ljava/util/Iterator;", "setContentIterator", "(Ljava/util/Iterator;)V", "getKey", "()Ljava/lang/Object;", "getParent", "()Lkotlin/io/path/PathNode;", "getPath", "()Ljava/nio/file/Path;", "kotlin-stdlib-jdk7"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class PathNode {
    private java.util.Iterator<com.android.server.permission.jarjar.kotlin.io.path.PathNode> contentIterator;
    private final java.lang.Object key;
    private final com.android.server.permission.jarjar.kotlin.io.path.PathNode parent;
    private final java.nio.file.Path path;

    public PathNode(java.nio.file.Path path, java.lang.Object key, com.android.server.permission.jarjar.kotlin.io.path.PathNode parent) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(path, "path");
        this.path = path;
        this.key = key;
        this.parent = parent;
    }

    public final java.lang.Object getKey() {
        return this.key;
    }

    public final com.android.server.permission.jarjar.kotlin.io.path.PathNode getParent() {
        return this.parent;
    }

    public final java.nio.file.Path getPath() {
        return this.path;
    }

    public final java.util.Iterator<com.android.server.permission.jarjar.kotlin.io.path.PathNode> getContentIterator() {
        return this.contentIterator;
    }

    public final void setContentIterator(java.util.Iterator<com.android.server.permission.jarjar.kotlin.io.path.PathNode> it) {
        this.contentIterator = it;
    }
}
