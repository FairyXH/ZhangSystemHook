package kotlin.io.path;

/* JADX INFO: compiled from: PathTreeWalk.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0006\u001a\u00020\u0007J\u0018\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lkotlin/io/path/DirectoryEntriesReader;", "Ljava/nio/file/SimpleFileVisitor;", "Ljava/nio/file/Path;", "followLinks", "", "(Z)V", "directoryNode", "Lkotlin/io/path/PathNode;", "entries", "Lkotlin/collections/ArrayDeque;", "getFollowLinks", "()Z", "preVisitDirectory", "Ljava/nio/file/FileVisitResult;", "dir", "attrs", "Ljava/nio/file/attribute/BasicFileAttributes;", "readEntries", "", "visitFile", "file", "kotlin-stdlib-jdk7"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class DirectoryEntriesReader extends java.nio.file.SimpleFileVisitor<java.nio.file.Path> {
    private kotlin.io.path.PathNode directoryNode;
    private kotlin.collections.ArrayDeque<kotlin.io.path.PathNode> entries = new kotlin.collections.ArrayDeque<>();
    private final boolean followLinks;

    public DirectoryEntriesReader(boolean followLinks) {
        this.followLinks = followLinks;
    }

    public final boolean getFollowLinks() {
        return this.followLinks;
    }

    public final java.util.List<kotlin.io.path.PathNode> readEntries(kotlin.io.path.PathNode directoryNode) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(directoryNode, "directoryNode");
        this.directoryNode = directoryNode;
        java.nio.file.Files.walkFileTree(directoryNode.getPath(), kotlin.io.path.LinkFollowing.INSTANCE.toVisitOptions(this.followLinks), 1, this);
        this.entries.removeFirst();
        kotlin.collections.ArrayDeque<kotlin.io.path.PathNode> arrayDeque = this.entries;
        this.entries = new kotlin.collections.ArrayDeque<>();
        return arrayDeque;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public java.nio.file.FileVisitResult preVisitDirectory(java.nio.file.Path dir, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dir, "dir");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(attrs, "attrs");
        kotlin.io.path.PathNode directoryEntry = new kotlin.io.path.PathNode(dir, attrs.fileKey(), this.directoryNode);
        this.entries.add(directoryEntry);
        java.nio.file.FileVisitResult fileVisitResultPreVisitDirectory = super.preVisitDirectory(dir, attrs);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(fileVisitResultPreVisitDirectory, "preVisitDirectory(...)");
        return fileVisitResultPreVisitDirectory;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public java.nio.file.FileVisitResult visitFile(java.nio.file.Path file, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(file, "file");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(attrs, "attrs");
        kotlin.io.path.PathNode fileEntry = new kotlin.io.path.PathNode(file, null, this.directoryNode);
        this.entries.add(fileEntry);
        java.nio.file.FileVisitResult fileVisitResultVisitFile = super.visitFile(file, attrs);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(fileVisitResultVisitFile, "visitFile(...)");
        return fileVisitResultVisitFile;
    }
}
