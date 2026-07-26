package com.android.server.permission.jarjar.kotlin.io.path;

/* JADX INFO: compiled from: PathTreeWalk.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000$\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\u001a%\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0002¢\u0006\u0002\u0010\u0007\u001a\f\u0010\b\u001a\u00020\t*\u00020\nH\u0002¨\u0006\u000b"}, d2 = {"keyOf", "", "path", "Ljava/nio/file/Path;", "linkOptions", "", "Ljava/nio/file/LinkOption;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/lang/Object;", "createsCycle", "", "Lkotlin/io/path/PathNode;", "kotlin-stdlib-jdk7"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class PathTreeWalkKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final java.lang.Object keyOf(java.nio.file.Path path, java.nio.file.LinkOption[] linkOptions) {
        try {
            java.nio.file.LinkOption[] linkOptionArr = (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(linkOptions, linkOptions.length);
            java.nio.file.attribute.BasicFileAttributes attributes = java.nio.file.Files.readAttributes(path, (java.lang.Class<java.nio.file.attribute.BasicFileAttributes>) java.nio.file.attribute.BasicFileAttributes.class, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(linkOptionArr, linkOptionArr.length));
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(attributes, "readAttributes(...)");
            return attributes.fileKey();
        } catch (java.lang.Throwable th) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean createsCycle(com.android.server.permission.jarjar.kotlin.io.path.PathNode $this$createsCycle) {
        for (com.android.server.permission.jarjar.kotlin.io.path.PathNode ancestor = $this$createsCycle.getParent(); ancestor != null; ancestor = ancestor.getParent()) {
            if (ancestor.getKey() != null && $this$createsCycle.getKey() != null) {
                if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(ancestor.getKey(), $this$createsCycle.getKey())) {
                    return true;
                }
            } else {
                try {
                    if (java.nio.file.Files.isSameFile(ancestor.getPath(), $this$createsCycle.getPath())) {
                        return true;
                    }
                } catch (java.io.IOException e) {
                } catch (java.lang.SecurityException e2) {
                }
            }
        }
        return false;
    }
}
