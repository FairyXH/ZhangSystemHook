package kotlin.io;

/* JADX INFO: compiled from: Utils.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Lkotlin/io/TerminateException;", "Lkotlin/io/FileSystemException;", "file", "Ljava/io/File;", "(Ljava/io/File;)V", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class TerminateException extends kotlin.io.FileSystemException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TerminateException(java.io.File file) {
        super(file, null, null, 6, null);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(file, "file");
    }
}
