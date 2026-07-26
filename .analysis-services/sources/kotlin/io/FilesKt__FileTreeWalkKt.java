package kotlin.io;

/* JADX INFO: compiled from: FileTreeWalk.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0007"}, d2 = {"walk", "Lkotlin/io/FileTreeWalk;", "Ljava/io/File;", "direction", "Lkotlin/io/FileWalkDirection;", "walkBottomUp", "walkTopDown", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/io/FilesKt")
class FilesKt__FileTreeWalkKt extends kotlin.io.FilesKt__FileReadWriteKt {
    public static /* synthetic */ kotlin.io.FileTreeWalk walk$default(java.io.File file, kotlin.io.FileWalkDirection fileWalkDirection, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            fileWalkDirection = kotlin.io.FileWalkDirection.TOP_DOWN;
        }
        return kotlin.io.FilesKt.walk(file, fileWalkDirection);
    }

    public static final kotlin.io.FileTreeWalk walk(java.io.File $this$walk, kotlin.io.FileWalkDirection direction) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$walk, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(direction, "direction");
        return new kotlin.io.FileTreeWalk($this$walk, direction);
    }

    public static final kotlin.io.FileTreeWalk walkTopDown(java.io.File $this$walkTopDown) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$walkTopDown, "<this>");
        return kotlin.io.FilesKt.walk($this$walkTopDown, kotlin.io.FileWalkDirection.TOP_DOWN);
    }

    public static final kotlin.io.FileTreeWalk walkBottomUp(java.io.File $this$walkBottomUp) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$walkBottomUp, "<this>");
        return kotlin.io.FilesKt.walk($this$walkBottomUp, kotlin.io.FileWalkDirection.BOTTOM_UP);
    }
}
