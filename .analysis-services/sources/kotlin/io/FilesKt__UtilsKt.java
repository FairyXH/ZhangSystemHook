package kotlin.io;

/* JADX INFO: compiled from: Utils.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000<\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u001a*\u0010\t\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0007\u001a*\u0010\r\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0007\u001a8\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\u001a\b\u0002\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u0013\u001a&\u0010\u0016\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u001a\n\u0010\u0019\u001a\u00020\u000f*\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\n\u0010\u001c\u001a\u00020\u0002*\u00020\u0002\u001a\u001d\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00020\u001d*\b\u0012\u0004\u0012\u00020\u00020\u001dH\u0002¢\u0006\u0002\b\u001e\u001a\u0011\u0010\u001c\u001a\u00020\u001f*\u00020\u001fH\u0002¢\u0006\u0002\b\u001e\u001a\u0012\u0010 \u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0014\u0010\"\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\u0012\u0010(\u001a\u00020\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u001b\u0010)\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002H\u0002¢\u0006\u0002\b*\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\"\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\b\u0010\u0004¨\u0006+"}, d2 = {"extension", "", "Ljava/io/File;", "getExtension", "(Ljava/io/File;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath", "nameWithoutExtension", "getNameWithoutExtension", "createTempDir", "prefix", "suffix", "directory", "createTempFile", "copyRecursively", "", "target", "overwrite", "onError", "Lkotlin/Function2;", "Ljava/io/IOException;", "Lkotlin/io/OnErrorAction;", "copyTo", "bufferSize", "", "deleteRecursively", "endsWith", "other", "normalize", "", "normalize$FilesKt__UtilsKt", "Lkotlin/io/FilePathComponents;", "relativeTo", "base", "relativeToOrNull", "relativeToOrSelf", "resolve", "relative", "resolveSibling", "startsWith", "toRelativeString", "toRelativeStringOrNull", "toRelativeStringOrNull$FilesKt__UtilsKt", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/io/FilesKt")
class FilesKt__UtilsKt extends kotlin.io.FilesKt__FileTreeWalkKt {
    public static /* synthetic */ java.io.File createTempDir$default(java.lang.String str, java.lang.String str2, java.io.File file, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str = "tmp";
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            file = null;
        }
        return kotlin.io.FilesKt.createTempDir(str, str2, file);
    }

    @kotlin.Deprecated(message = "Avoid creating temporary directories in the default temp location with this function due to too wide permissions on the newly created directory. Use kotlin.io.path.createTempDirectory instead.")
    public static final java.io.File createTempDir(java.lang.String prefix, java.lang.String suffix, java.io.File directory) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prefix, "prefix");
        java.io.File dir = java.io.File.createTempFile(prefix, suffix, directory);
        dir.delete();
        if (dir.mkdir()) {
            kotlin.jvm.internal.Intrinsics.checkNotNull(dir);
            return dir;
        }
        throw new java.io.IOException("Unable to create temporary directory " + dir + '.');
    }

    public static /* synthetic */ java.io.File createTempFile$default(java.lang.String str, java.lang.String str2, java.io.File file, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str = "tmp";
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            file = null;
        }
        return kotlin.io.FilesKt.createTempFile(str, str2, file);
    }

    @kotlin.Deprecated(message = "Avoid creating temporary files in the default temp location with this function due to too wide permissions on the newly created file. Use kotlin.io.path.createTempFile instead or resort to java.io.File.createTempFile.")
    public static final java.io.File createTempFile(java.lang.String prefix, java.lang.String suffix, java.io.File directory) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prefix, "prefix");
        java.io.File fileCreateTempFile = java.io.File.createTempFile(prefix, suffix, directory);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(fileCreateTempFile, "createTempFile(...)");
        return fileCreateTempFile;
    }

    public static final java.lang.String getExtension(java.io.File $this$extension) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$extension, "<this>");
        java.lang.String name = $this$extension.getName();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
        return kotlin.text.StringsKt.substringAfterLast(name, '.', "");
    }

    public static final java.lang.String getInvariantSeparatorsPath(java.io.File $this$invariantSeparatorsPath) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$invariantSeparatorsPath, "<this>");
        if (java.io.File.separatorChar != '/') {
            java.lang.String path = $this$invariantSeparatorsPath.getPath();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(path, "getPath(...)");
            return kotlin.text.StringsKt.replace$default(path, java.io.File.separatorChar, '/', false, 4, (java.lang.Object) null);
        }
        java.lang.String path2 = $this$invariantSeparatorsPath.getPath();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(path2, "getPath(...)");
        return path2;
    }

    public static final java.lang.String getNameWithoutExtension(java.io.File $this$nameWithoutExtension) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nameWithoutExtension, "<this>");
        java.lang.String name = $this$nameWithoutExtension.getName();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
        return kotlin.text.StringsKt.substringBeforeLast$default(name, ".", (java.lang.String) null, 2, (java.lang.Object) null);
    }

    public static final java.lang.String toRelativeString(java.io.File $this$toRelativeString, java.io.File base) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toRelativeString, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(base, "base");
        java.lang.String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt($this$toRelativeString, base);
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            return relativeStringOrNull$FilesKt__UtilsKt;
        }
        throw new java.lang.IllegalArgumentException("this and base files have different roots: " + $this$toRelativeString + " and " + base + '.');
    }

    public static final java.io.File relativeTo(java.io.File $this$relativeTo, java.io.File base) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$relativeTo, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(base, "base");
        return new java.io.File(kotlin.io.FilesKt.toRelativeString($this$relativeTo, base));
    }

    public static final java.io.File relativeToOrSelf(java.io.File $this$relativeToOrSelf, java.io.File base) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$relativeToOrSelf, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(base, "base");
        java.lang.String p0 = toRelativeStringOrNull$FilesKt__UtilsKt($this$relativeToOrSelf, base);
        return p0 != null ? new java.io.File(p0) : $this$relativeToOrSelf;
    }

    public static final java.io.File relativeToOrNull(java.io.File $this$relativeToOrNull, java.io.File base) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$relativeToOrNull, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(base, "base");
        java.lang.String p0 = toRelativeStringOrNull$FilesKt__UtilsKt($this$relativeToOrNull, base);
        if (p0 != null) {
            return new java.io.File(p0);
        }
        return null;
    }

    private static final java.lang.String toRelativeStringOrNull$FilesKt__UtilsKt(java.io.File $this$toRelativeStringOrNull, java.io.File base) {
        kotlin.io.FilePathComponents thisComponents = normalize$FilesKt__UtilsKt(kotlin.io.FilesKt.toComponents($this$toRelativeStringOrNull));
        kotlin.io.FilePathComponents baseComponents = normalize$FilesKt__UtilsKt(kotlin.io.FilesKt.toComponents(base));
        if (!kotlin.jvm.internal.Intrinsics.areEqual(thisComponents.getRoot(), baseComponents.getRoot())) {
            return null;
        }
        int baseCount = baseComponents.getSize();
        int thisCount = thisComponents.getSize();
        int i = 0;
        int maxSameCount = java.lang.Math.min(thisCount, baseCount);
        while (i < maxSameCount && kotlin.jvm.internal.Intrinsics.areEqual(thisComponents.getSegments().get(i), baseComponents.getSegments().get(i))) {
            i++;
        }
        int sameCount = i;
        java.lang.StringBuilder res = new java.lang.StringBuilder();
        int i2 = baseCount - 1;
        if (sameCount <= i2) {
            while (!kotlin.jvm.internal.Intrinsics.areEqual(baseComponents.getSegments().get(i2).getName(), "..")) {
                res.append("..");
                if (i2 != sameCount) {
                    res.append(java.io.File.separatorChar);
                }
                if (i2 != sameCount) {
                    i2--;
                }
            }
            return null;
        }
        if (sameCount < thisCount) {
            if (sameCount < baseCount) {
                res.append(java.io.File.separatorChar);
            }
            java.lang.String separator = java.io.File.separator;
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(separator, "separator");
            kotlin.collections.CollectionsKt.joinTo(kotlin.collections.CollectionsKt.drop(thisComponents.getSegments(), sameCount), res, (124 & 2) != 0 ? ", " : separator, (124 & 4) != 0 ? "" : null, (124 & 8) != 0 ? "" : null, (124 & 16) != 0 ? -1 : 0, (124 & 32) != 0 ? "..." : null, (124 & 64) != 0 ? null : null);
        }
        return res.toString();
    }

    public static /* synthetic */ java.io.File copyTo$default(java.io.File file, java.io.File file2, boolean z, int i, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 8192;
        }
        return kotlin.io.FilesKt.copyTo(file, file2, z, i);
    }

    public static final java.io.File copyTo(java.io.File $this$copyTo, java.io.File target, boolean overwrite, int bufferSize) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$copyTo, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(target, "target");
        if (!$this$copyTo.exists()) {
            throw new kotlin.io.NoSuchFileException($this$copyTo, null, "The source file doesn't exist.", 2, null);
        }
        if (target.exists()) {
            if (!overwrite) {
                throw new kotlin.io.FileAlreadyExistsException($this$copyTo, target, "The destination file already exists.");
            }
            if (!target.delete()) {
                throw new kotlin.io.FileAlreadyExistsException($this$copyTo, target, "Tried to overwrite the destination, but failed to delete it.");
            }
        }
        if ($this$copyTo.isDirectory()) {
            if (!target.mkdirs()) {
                throw new kotlin.io.FileSystemException($this$copyTo, target, "Failed to create target directory.");
            }
        } else {
            java.io.File parentFile = target.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            java.io.FileOutputStream fileInputStream = new java.io.FileInputStream($this$copyTo);
            try {
                java.io.FileInputStream input = fileInputStream;
                fileInputStream = new java.io.FileOutputStream(target);
                try {
                    java.io.FileOutputStream output = fileInputStream;
                    kotlin.io.ByteStreamsKt.copyTo(input, output, bufferSize);
                    kotlin.io.CloseableKt.closeFinally(fileInputStream, null);
                    kotlin.io.CloseableKt.closeFinally(fileInputStream, null);
                } finally {
                }
            } finally {
            }
        }
        return target;
    }

    public static /* synthetic */ boolean copyRecursively$default(java.io.File file, java.io.File file2, boolean z, kotlin.jvm.functions.Function2 function2, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        if ((i & 4) != 0) {
            function2 = new kotlin.jvm.functions.Function2() { // from class: kotlin.io.FilesKt__UtilsKt.copyRecursively.1
                @Override // kotlin.jvm.functions.Function2
                public final java.lang.Void invoke(java.io.File file3, java.io.IOException exception) throws java.io.IOException {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(file3, "<anonymous parameter 0>");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
                    throw exception;
                }
            };
        }
        return kotlin.io.FilesKt.copyRecursively(file, file2, z, function2);
    }

    public static final boolean copyRecursively(java.io.File $this$copyRecursively, java.io.File target, boolean overwrite, final kotlin.jvm.functions.Function2<? super java.io.File, ? super java.io.IOException, ? extends kotlin.io.OnErrorAction> onError) {
        boolean stillExists;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$copyRecursively, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(target, "target");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onError, "onError");
        if (!$this$copyRecursively.exists()) {
            return onError.invoke($this$copyRecursively, new kotlin.io.NoSuchFileException($this$copyRecursively, null, "The source file doesn't exist.", 2, null)) != kotlin.io.OnErrorAction.TERMINATE;
        }
        try {
            for (java.io.File src : kotlin.io.FilesKt.walkTopDown($this$copyRecursively).onFail(new kotlin.jvm.functions.Function2<java.io.File, java.io.IOException, kotlin.Unit>() { // from class: kotlin.io.FilesKt__UtilsKt.copyRecursively.2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                /* JADX WARN: Multi-variable type inference failed */
                {
                    super(2);
                }

                @Override // kotlin.jvm.functions.Function2
                public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.io.File file, java.io.IOException iOException) throws kotlin.io.TerminateException {
                    invoke2(file, iOException);
                    return kotlin.Unit.INSTANCE;
                }

                /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2(java.io.File f, java.io.IOException e) throws kotlin.io.TerminateException {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(f, "f");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(e, "e");
                    if (onError.invoke(f, e) == kotlin.io.OnErrorAction.TERMINATE) {
                        throw new kotlin.io.TerminateException(f);
                    }
                }
            })) {
                if (!src.exists()) {
                    if (onError.invoke(src, new kotlin.io.NoSuchFileException(src, null, "The source file doesn't exist.", 2, null)) == kotlin.io.OnErrorAction.TERMINATE) {
                        return false;
                    }
                } else {
                    java.lang.String relPath = kotlin.io.FilesKt.toRelativeString(src, $this$copyRecursively);
                    java.io.File dstFile = new java.io.File(target, relPath);
                    if (dstFile.exists() && (!src.isDirectory() || !dstFile.isDirectory())) {
                        if (!overwrite) {
                            stillExists = true;
                        } else if (dstFile.isDirectory()) {
                            stillExists = !kotlin.io.FilesKt.deleteRecursively(dstFile);
                        } else {
                            stillExists = !dstFile.delete();
                        }
                        if (stillExists) {
                            if (onError.invoke(dstFile, new kotlin.io.FileAlreadyExistsException(src, dstFile, "The destination file already exists.")) == kotlin.io.OnErrorAction.TERMINATE) {
                                return false;
                            }
                        }
                    }
                    boolean stillExists2 = src.isDirectory();
                    if (stillExists2) {
                        dstFile.mkdirs();
                    } else if (kotlin.io.FilesKt.copyTo$default(src, dstFile, overwrite, 0, 4, null).length() != src.length() && onError.invoke(src, new java.io.IOException("Source file wasn't copied completely, length of destination file differs.")) == kotlin.io.OnErrorAction.TERMINATE) {
                        return false;
                    }
                }
            }
            return true;
        } catch (kotlin.io.TerminateException e) {
            return false;
        }
    }

    public static final boolean deleteRecursively(java.io.File $this$deleteRecursively) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$deleteRecursively, "<this>");
        kotlin.sequences.Sequence $this$fold$iv = kotlin.io.FilesKt.walkBottomUp($this$deleteRecursively);
        boolean accumulator$iv = true;
        for (java.lang.Object element$iv : $this$fold$iv) {
            java.io.File it = (java.io.File) element$iv;
            boolean res = accumulator$iv;
            accumulator$iv = (it.delete() || !it.exists()) && res;
        }
        return accumulator$iv;
    }

    public static final boolean startsWith(java.io.File $this$startsWith, java.io.File other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        kotlin.io.FilePathComponents components = kotlin.io.FilesKt.toComponents($this$startsWith);
        kotlin.io.FilePathComponents otherComponents = kotlin.io.FilesKt.toComponents(other);
        if (kotlin.jvm.internal.Intrinsics.areEqual(components.getRoot(), otherComponents.getRoot()) && components.getSize() >= otherComponents.getSize()) {
            return components.getSegments().subList(0, otherComponents.getSize()).equals(otherComponents.getSegments());
        }
        return false;
    }

    public static final boolean startsWith(java.io.File $this$startsWith, java.lang.String other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        return kotlin.io.FilesKt.startsWith($this$startsWith, new java.io.File(other));
    }

    public static final boolean endsWith(java.io.File $this$endsWith, java.io.File other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        kotlin.io.FilePathComponents components = kotlin.io.FilesKt.toComponents($this$endsWith);
        kotlin.io.FilePathComponents otherComponents = kotlin.io.FilesKt.toComponents(other);
        if (otherComponents.isRooted()) {
            return kotlin.jvm.internal.Intrinsics.areEqual($this$endsWith, other);
        }
        int shift = components.getSize() - otherComponents.getSize();
        if (shift < 0) {
            return false;
        }
        return components.getSegments().subList(shift, components.getSize()).equals(otherComponents.getSegments());
    }

    public static final boolean endsWith(java.io.File $this$endsWith, java.lang.String other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        return kotlin.io.FilesKt.endsWith($this$endsWith, new java.io.File(other));
    }

    public static final java.io.File normalize(java.io.File $this$normalize) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$normalize, "<this>");
        kotlin.io.FilePathComponents $this$normalize_u24lambda_u245 = kotlin.io.FilesKt.toComponents($this$normalize);
        java.io.File root = $this$normalize_u24lambda_u245.getRoot();
        java.util.List<java.io.File> listNormalize$FilesKt__UtilsKt = normalize$FilesKt__UtilsKt($this$normalize_u24lambda_u245.getSegments());
        java.lang.String separator = java.io.File.separator;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(separator, "separator");
        return kotlin.io.FilesKt.resolve(root, kotlin.collections.CollectionsKt.joinToString$default(listNormalize$FilesKt__UtilsKt, separator, null, null, 0, null, null, 62, null));
    }

    private static final kotlin.io.FilePathComponents normalize$FilesKt__UtilsKt(kotlin.io.FilePathComponents $this$normalize) {
        return new kotlin.io.FilePathComponents($this$normalize.getRoot(), normalize$FilesKt__UtilsKt($this$normalize.getSegments()));
    }

    private static final java.util.List<java.io.File> normalize$FilesKt__UtilsKt(java.util.List<? extends java.io.File> list) {
        java.util.List list2 = new java.util.ArrayList(list.size());
        for (java.io.File file : list) {
            java.lang.String name = file.getName();
            if (!kotlin.jvm.internal.Intrinsics.areEqual(name, ".")) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(name, "..")) {
                    if (list2.isEmpty() || kotlin.jvm.internal.Intrinsics.areEqual(((java.io.File) kotlin.collections.CollectionsKt.last(list2)).getName(), "..")) {
                        list2.add(file);
                    } else {
                        list2.remove(list2.size() - 1);
                    }
                } else {
                    list2.add(file);
                }
            }
        }
        return list2;
    }

    public static final java.io.File resolve(java.io.File $this$resolve, java.io.File relative) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$resolve, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(relative, "relative");
        if (kotlin.io.FilesKt.isRooted(relative)) {
            return relative;
        }
        java.lang.String baseName = $this$resolve.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(baseName, "toString(...)");
        return ((baseName.length() == 0) || kotlin.text.StringsKt.endsWith$default((java.lang.CharSequence) baseName, java.io.File.separatorChar, false, 2, (java.lang.Object) null)) ? new java.io.File(baseName + relative) : new java.io.File(baseName + java.io.File.separatorChar + relative);
    }

    public static final java.io.File resolve(java.io.File $this$resolve, java.lang.String relative) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$resolve, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(relative, "relative");
        return kotlin.io.FilesKt.resolve($this$resolve, new java.io.File(relative));
    }

    public static final java.io.File resolveSibling(java.io.File $this$resolveSibling, java.io.File relative) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$resolveSibling, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(relative, "relative");
        kotlin.io.FilePathComponents components = kotlin.io.FilesKt.toComponents($this$resolveSibling);
        java.io.File parentSubPath = components.getSize() == 0 ? new java.io.File("..") : components.subPath(0, components.getSize() - 1);
        return kotlin.io.FilesKt.resolve(kotlin.io.FilesKt.resolve(components.getRoot(), parentSubPath), relative);
    }

    public static final java.io.File resolveSibling(java.io.File $this$resolveSibling, java.lang.String relative) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$resolveSibling, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(relative, "relative");
        return kotlin.io.FilesKt.resolveSibling($this$resolveSibling, new java.io.File(relative));
    }
}
