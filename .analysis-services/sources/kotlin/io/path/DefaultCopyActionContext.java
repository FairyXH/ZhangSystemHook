package kotlin.io.path;

/* JADX INFO: compiled from: PathRecursiveFunctions.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\bÃ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, d2 = {"Lkotlin/io/path/DefaultCopyActionContext;", "Lkotlin/io/path/CopyActionContext;", "()V", "copyToIgnoringExistingDirectory", "Lkotlin/io/path/CopyActionResult;", "Ljava/nio/file/Path;", "target", "followLinks", "", "kotlin-stdlib-jdk7"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class DefaultCopyActionContext implements kotlin.io.path.CopyActionContext {
    public static final kotlin.io.path.DefaultCopyActionContext INSTANCE = new kotlin.io.path.DefaultCopyActionContext();

    private DefaultCopyActionContext() {
    }

    @Override // kotlin.io.path.CopyActionContext
    public kotlin.io.path.CopyActionResult copyToIgnoringExistingDirectory(java.nio.file.Path $this$copyToIgnoringExistingDirectory, java.nio.file.Path target, boolean followLinks) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$copyToIgnoringExistingDirectory, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(target, "target");
        java.nio.file.LinkOption[] options = kotlin.io.path.LinkFollowing.INSTANCE.toLinkOptions(followLinks);
        java.nio.file.LinkOption[] linkOptionArr = (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(options, options.length);
        if (!java.nio.file.Files.isDirectory($this$copyToIgnoringExistingDirectory, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(linkOptionArr, linkOptionArr.length)) || !java.nio.file.Files.isDirectory(target, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(new java.nio.file.LinkOption[]{java.nio.file.LinkOption.NOFOLLOW_LINKS}, 1))) {
            java.nio.file.CopyOption[] copyOptionArr = (java.nio.file.CopyOption[]) java.util.Arrays.copyOf(options, options.length);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(java.nio.file.Files.copy($this$copyToIgnoringExistingDirectory, target, (java.nio.file.CopyOption[]) java.util.Arrays.copyOf(copyOptionArr, copyOptionArr.length)), "copy(...)");
        }
        return kotlin.io.path.CopyActionResult.CONTINUE;
    }
}
