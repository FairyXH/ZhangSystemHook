package com.android.server.permission.jarjar.kotlin.io.path;

/* JADX INFO: compiled from: FileVisitorBuilder.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001Bw\u0012\u001a\u0010\u0003\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004\u0012\u001a\u0010\u0007\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004\u0012\u001a\u0010\b\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004\u0012\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004¢\u0006\u0002\u0010\u000bJ\u001a\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\tH\u0016J\u0018\u0010\u000f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u0005H\u0016J\u0018\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u0005H\u0016J\u0018\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\tH\u0016R$\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\"\u0010\u0003\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\"\u0010\u0007\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\"\u0010\b\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lkotlin/io/path/FileVisitorImpl;", "Ljava/nio/file/SimpleFileVisitor;", "Ljava/nio/file/Path;", "onPreVisitDirectory", "Lkotlin/Function2;", "Ljava/nio/file/attribute/BasicFileAttributes;", "Ljava/nio/file/FileVisitResult;", "onVisitFile", "onVisitFileFailed", "Ljava/io/IOException;", "onPostVisitDirectory", "(Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;)V", "postVisitDirectory", "dir", "exc", "preVisitDirectory", "attrs", "visitFile", "file", "visitFileFailed", "kotlin-stdlib-jdk7"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class FileVisitorImpl extends java.nio.file.SimpleFileVisitor<java.nio.file.Path> {
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<java.nio.file.Path, java.io.IOException, java.nio.file.FileVisitResult> onPostVisitDirectory;
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<java.nio.file.Path, java.nio.file.attribute.BasicFileAttributes, java.nio.file.FileVisitResult> onPreVisitDirectory;
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<java.nio.file.Path, java.nio.file.attribute.BasicFileAttributes, java.nio.file.FileVisitResult> onVisitFile;
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<java.nio.file.Path, java.io.IOException, java.nio.file.FileVisitResult> onVisitFileFailed;

    /* JADX WARN: Multi-variable type inference failed */
    public FileVisitorImpl(com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.nio.file.Path, ? super java.nio.file.attribute.BasicFileAttributes, ? extends java.nio.file.FileVisitResult> function2, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.nio.file.Path, ? super java.nio.file.attribute.BasicFileAttributes, ? extends java.nio.file.FileVisitResult> function22, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.nio.file.Path, ? super java.io.IOException, ? extends java.nio.file.FileVisitResult> function23, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.nio.file.Path, ? super java.io.IOException, ? extends java.nio.file.FileVisitResult> function24) {
        this.onPreVisitDirectory = function2;
        this.onVisitFile = function22;
        this.onVisitFileFailed = function23;
        this.onPostVisitDirectory = function24;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public java.nio.file.FileVisitResult preVisitDirectory(java.nio.file.Path dir, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
        java.nio.file.FileVisitResult fileVisitResultInvoke;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dir, "dir");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(attrs, "attrs");
        com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<java.nio.file.Path, java.nio.file.attribute.BasicFileAttributes, java.nio.file.FileVisitResult> function2 = this.onPreVisitDirectory;
        if (function2 != null && (fileVisitResultInvoke = function2.invoke(dir, attrs)) != null) {
            return fileVisitResultInvoke;
        }
        java.nio.file.FileVisitResult fileVisitResultPreVisitDirectory = super.preVisitDirectory(dir, attrs);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(fileVisitResultPreVisitDirectory, "preVisitDirectory(...)");
        return fileVisitResultPreVisitDirectory;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public java.nio.file.FileVisitResult visitFile(java.nio.file.Path file, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
        java.nio.file.FileVisitResult fileVisitResultInvoke;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(file, "file");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(attrs, "attrs");
        com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<java.nio.file.Path, java.nio.file.attribute.BasicFileAttributes, java.nio.file.FileVisitResult> function2 = this.onVisitFile;
        if (function2 != null && (fileVisitResultInvoke = function2.invoke(file, attrs)) != null) {
            return fileVisitResultInvoke;
        }
        java.nio.file.FileVisitResult fileVisitResultVisitFile = super.visitFile(file, attrs);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(fileVisitResultVisitFile, "visitFile(...)");
        return fileVisitResultVisitFile;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public java.nio.file.FileVisitResult visitFileFailed(java.nio.file.Path file, java.io.IOException exc) throws java.io.IOException {
        java.nio.file.FileVisitResult fileVisitResultInvoke;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(file, "file");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exc, "exc");
        com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<java.nio.file.Path, java.io.IOException, java.nio.file.FileVisitResult> function2 = this.onVisitFileFailed;
        if (function2 != null && (fileVisitResultInvoke = function2.invoke(file, exc)) != null) {
            return fileVisitResultInvoke;
        }
        java.nio.file.FileVisitResult fileVisitResultVisitFileFailed = super.visitFileFailed(file, exc);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(fileVisitResultVisitFileFailed, "visitFileFailed(...)");
        return fileVisitResultVisitFileFailed;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public java.nio.file.FileVisitResult postVisitDirectory(java.nio.file.Path dir, java.io.IOException exc) throws java.io.IOException {
        java.nio.file.FileVisitResult fileVisitResultInvoke;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dir, "dir");
        com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<java.nio.file.Path, java.io.IOException, java.nio.file.FileVisitResult> function2 = this.onPostVisitDirectory;
        if (function2 != null && (fileVisitResultInvoke = function2.invoke(dir, exc)) != null) {
            return fileVisitResultInvoke;
        }
        java.nio.file.FileVisitResult fileVisitResultPostVisitDirectory = super.postVisitDirectory(dir, exc);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(fileVisitResultPostVisitDirectory, "postVisitDirectory(...)");
        return fileVisitResultPostVisitDirectory;
    }
}
