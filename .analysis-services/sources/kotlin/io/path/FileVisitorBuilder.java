package kotlin.io.path;

/* JADX INFO: compiled from: FileVisitorBuilder.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\bw\u0018\u00002\u00020\u0001JB\u0010\u0002\u001a\u00020\u000328\u0010\u0004\u001a4\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0015\u0012\u0013\u0018\u00010\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\f0\u0005H&J@\u0010\r\u001a\u00020\u000326\u0010\u0004\u001a2\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\u000e¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\f0\u0005H&J@\u0010\u0010\u001a\u00020\u000326\u0010\u0004\u001a2\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0011\u0012\u0013\u0012\u00110\u000e¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\f0\u0005H&J@\u0010\u0012\u001a\u00020\u000326\u0010\u0004\u001a2\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0011\u0012\u0013\u0012\u00110\n¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\f0\u0005H&\u0082\u0001\u0001\u0013¨\u0006\u0014"}, d2 = {"Lkotlin/io/path/FileVisitorBuilder;", "", "onPostVisitDirectory", "", "function", "Lkotlin/Function2;", "Ljava/nio/file/Path;", "Lkotlin/ParameterName;", "name", "directory", "Ljava/io/IOException;", "exception", "Ljava/nio/file/FileVisitResult;", "onPreVisitDirectory", "Ljava/nio/file/attribute/BasicFileAttributes;", "attributes", "onVisitFile", "file", "onVisitFileFailed", "Lkotlin/io/path/FileVisitorBuilderImpl;", "kotlin-stdlib-jdk7"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface FileVisitorBuilder {
    void onPostVisitDirectory(kotlin.jvm.functions.Function2<? super java.nio.file.Path, ? super java.io.IOException, ? extends java.nio.file.FileVisitResult> function);

    void onPreVisitDirectory(kotlin.jvm.functions.Function2<? super java.nio.file.Path, ? super java.nio.file.attribute.BasicFileAttributes, ? extends java.nio.file.FileVisitResult> function);

    void onVisitFile(kotlin.jvm.functions.Function2<? super java.nio.file.Path, ? super java.nio.file.attribute.BasicFileAttributes, ? extends java.nio.file.FileVisitResult> function);

    void onVisitFileFailed(kotlin.jvm.functions.Function2<? super java.nio.file.Path, ? super java.io.IOException, ? extends java.nio.file.FileVisitResult> function);
}
