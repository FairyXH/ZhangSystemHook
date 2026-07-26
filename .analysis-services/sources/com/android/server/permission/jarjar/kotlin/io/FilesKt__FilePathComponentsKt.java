package com.android.server.permission.jarjar.kotlin.io;

/* JADX INFO: compiled from: FilePathComponents.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000$\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u000b\u001a\u00020\f*\u00020\bH\u0002¢\u0006\u0002\b\r\u001a\u001c\u0010\u000e\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\fH\u0000\u001a\f\u0010\u0011\u001a\u00020\u0012*\u00020\u0002H\u0000\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\"\u0018\u0010\u0004\u001a\u00020\u0002*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\"\u0018\u0010\u0007\u001a\u00020\b*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\u0013"}, d2 = {"isRooted", "", "Ljava/io/File;", "(Ljava/io/File;)Z", "root", "getRoot", "(Ljava/io/File;)Ljava/io/File;", "rootName", "", "getRootName", "(Ljava/io/File;)Ljava/lang/String;", "getRootLength", "", "getRootLength$FilesKt__FilePathComponentsKt", "subPath", "beginIndex", "endIndex", "toComponents", "Lkotlin/io/FilePathComponents;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/io/FilesKt")
class FilesKt__FilePathComponentsKt {
    private static final int getRootLength$FilesKt__FilePathComponentsKt(java.lang.String $this$getRootLength) {
        int first;
        int first2 = com.android.server.permission.jarjar.kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) $this$getRootLength, java.io.File.separatorChar, 0, false, 4, (java.lang.Object) null);
        if (first2 == 0) {
            if ($this$getRootLength.length() <= 1 || $this$getRootLength.charAt(1) != java.io.File.separatorChar || (first = com.android.server.permission.jarjar.kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) $this$getRootLength, java.io.File.separatorChar, 2, false, 4, (java.lang.Object) null)) < 0) {
                return 1;
            }
            int first3 = com.android.server.permission.jarjar.kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) $this$getRootLength, java.io.File.separatorChar, first + 1, false, 4, (java.lang.Object) null);
            if (first3 >= 0) {
                return first3 + 1;
            }
            return $this$getRootLength.length();
        }
        if (first2 > 0 && $this$getRootLength.charAt(first2 - 1) == ':') {
            return first2 + 1;
        }
        if (first2 == -1 && com.android.server.permission.jarjar.kotlin.text.StringsKt.endsWith$default((java.lang.CharSequence) $this$getRootLength, ':', false, 2, (java.lang.Object) null)) {
            return $this$getRootLength.length();
        }
        return 0;
    }

    public static final java.lang.String getRootName(java.io.File $this$rootName) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$rootName, "<this>");
        java.lang.String path = $this$rootName.getPath();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(path, "getPath(...)");
        java.lang.String path2 = $this$rootName.getPath();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(path2, "getPath(...)");
        java.lang.String strSubstring = path.substring(0, getRootLength$FilesKt__FilePathComponentsKt(path2));
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static final java.io.File getRoot(java.io.File $this$root) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$root, "<this>");
        return new java.io.File(com.android.server.permission.jarjar.kotlin.io.FilesKt.getRootName($this$root));
    }

    public static final boolean isRooted(java.io.File $this$isRooted) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$isRooted, "<this>");
        java.lang.String path = $this$isRooted.getPath();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(path, "getPath(...)");
        return getRootLength$FilesKt__FilePathComponentsKt(path) > 0;
    }

    public static final com.android.server.permission.jarjar.kotlin.io.FilePathComponents toComponents(java.io.File $this$toComponents) {
        java.util.List list;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toComponents, "<this>");
        java.lang.String path = $this$toComponents.getPath();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(path);
        int rootLength = getRootLength$FilesKt__FilePathComponentsKt(path);
        java.lang.String rootName = path.substring(0, rootLength);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(rootName, "substring(...)");
        java.lang.String subPath = path.substring(rootLength);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(subPath, "substring(...)");
        if (subPath.length() == 0) {
            list = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.emptyList();
        } else {
            java.lang.Iterable $this$map$iv = com.android.server.permission.jarjar.kotlin.text.StringsKt.split$default((java.lang.CharSequence) subPath, new char[]{java.io.File.separatorChar}, false, 0, 6, (java.lang.Object) null);
            java.util.Collection destination$iv$iv = new java.util.ArrayList(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            for (java.lang.Object item$iv$iv : $this$map$iv) {
                java.lang.String p0 = (java.lang.String) item$iv$iv;
                destination$iv$iv.add(new java.io.File(p0));
            }
            list = (java.util.List) destination$iv$iv;
        }
        return new com.android.server.permission.jarjar.kotlin.io.FilePathComponents(new java.io.File(rootName), list);
    }

    public static final java.io.File subPath(java.io.File $this$subPath, int beginIndex, int endIndex) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$subPath, "<this>");
        return com.android.server.permission.jarjar.kotlin.io.FilesKt.toComponents($this$subPath).subPath(beginIndex, endIndex);
    }
}
