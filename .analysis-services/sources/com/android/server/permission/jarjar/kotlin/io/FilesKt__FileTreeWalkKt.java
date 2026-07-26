package com.android.server.permission.jarjar.kotlin.io;

/* JADX INFO: compiled from: FileTreeWalk.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0007"}, d2 = {"walk", "Lkotlin/io/FileTreeWalk;", "Ljava/io/File;", "direction", "Lkotlin/io/FileWalkDirection;", "walkBottomUp", "walkTopDown", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/io/FilesKt")
class FilesKt__FileTreeWalkKt extends com.android.server.permission.jarjar.kotlin.io.FilesKt__FileReadWriteKt {
    public static /* synthetic */ com.android.server.permission.jarjar.kotlin.io.FileTreeWalk walk$default(java.io.File file, com.android.server.permission.jarjar.kotlin.io.FileWalkDirection fileWalkDirection, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            fileWalkDirection = com.android.server.permission.jarjar.kotlin.io.FileWalkDirection.TOP_DOWN;
        }
        return com.android.server.permission.jarjar.kotlin.io.FilesKt.walk(file, fileWalkDirection);
    }

    public static final com.android.server.permission.jarjar.kotlin.io.FileTreeWalk walk(java.io.File $this$walk, com.android.server.permission.jarjar.kotlin.io.FileWalkDirection direction) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$walk, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(direction, "direction");
        return new com.android.server.permission.jarjar.kotlin.io.FileTreeWalk($this$walk, direction);
    }

    public static final com.android.server.permission.jarjar.kotlin.io.FileTreeWalk walkTopDown(java.io.File $this$walkTopDown) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$walkTopDown, "<this>");
        return com.android.server.permission.jarjar.kotlin.io.FilesKt.walk($this$walkTopDown, com.android.server.permission.jarjar.kotlin.io.FileWalkDirection.TOP_DOWN);
    }

    public static final com.android.server.permission.jarjar.kotlin.io.FileTreeWalk walkBottomUp(java.io.File $this$walkBottomUp) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$walkBottomUp, "<this>");
        return com.android.server.permission.jarjar.kotlin.io.FilesKt.walk($this$walkBottomUp, com.android.server.permission.jarjar.kotlin.io.FileWalkDirection.BOTTOM_UP);
    }
}
