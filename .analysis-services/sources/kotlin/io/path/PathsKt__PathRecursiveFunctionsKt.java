package kotlin.io.path;

/* JADX INFO: compiled from: PathRecursiveFunctions.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000v\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0082\b¢\u0006\u0002\b\u0006\u001a\u001d\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0002\b\n\u001a\u001d\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0002\b\r\u001a&\u0010\u000e\u001a\u0004\u0018\u0001H\u000f\"\u0004\b\u0000\u0010\u000f2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0005H\u0082\b¢\u0006\u0004\b\u0010\u0010\u0011\u001aw\u0010\u0012\u001a\u00020\t*\u00020\t2\u0006\u0010\u0013\u001a\u00020\t2Q\b\u0002\u0010\u0014\u001aK\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0013\u0012\u0017\u0012\u00150\u0019j\u0002`\u001a¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u001c0\u00152\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eH\u0007\u001a´\u0001\u0010\u0012\u001a\u00020\t*\u00020\t2\u0006\u0010\u0013\u001a\u00020\t2Q\b\u0002\u0010\u0014\u001aK\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0013\u0012\u0017\u0012\u00150\u0019j\u0002`\u001a¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u001c0\u00152\u0006\u0010\u001d\u001a\u00020\u001e2C\b\u0002\u0010 \u001a=\u0012\u0004\u0012\u00020!\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\"0\u0015¢\u0006\u0002\b#H\u0007\u001a\f\u0010$\u001a\u00020\u0001*\u00020\tH\u0007\u001a\u001b\u0010%\u001a\f\u0012\b\u0012\u00060\u0019j\u0002`\u001a0&*\u00020\tH\u0002¢\u0006\u0002\b'\u001a'\u0010(\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\t0)2\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0002\b*\u001a'\u0010+\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\t0)2\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0002\b,\u001a5\u0010-\u001a\u00020\u001e*\b\u0012\u0004\u0012\u00020\t0)2\u0006\u0010.\u001a\u00020\t2\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020100\"\u000201H\u0002¢\u0006\u0004\b2\u00103\u001a\u0011\u00104\u001a\u000205*\u00020\"H\u0003¢\u0006\u0002\b6\u001a\u0011\u00104\u001a\u000205*\u00020\u001cH\u0003¢\u0006\u0002\b6¨\u00067"}, d2 = {"collectIfThrows", "", "collector", "Lkotlin/io/path/ExceptionsCollector;", "function", "Lkotlin/Function0;", "collectIfThrows$PathsKt__PathRecursiveFunctionsKt", "insecureEnterDirectory", "path", "Ljava/nio/file/Path;", "insecureEnterDirectory$PathsKt__PathRecursiveFunctionsKt", "insecureHandleEntry", "entry", "insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt", "tryIgnoreNoSuchFileException", "R", "tryIgnoreNoSuchFileException$PathsKt__PathRecursiveFunctionsKt", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "copyToRecursively", "target", "onError", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", "source", "Ljava/lang/Exception;", "Lkotlin/Exception;", "exception", "Lkotlin/io/path/OnErrorResult;", "followLinks", "", "overwrite", "copyAction", "Lkotlin/io/path/CopyActionContext;", "Lkotlin/io/path/CopyActionResult;", "Lkotlin/ExtensionFunctionType;", "deleteRecursively", "deleteRecursivelyImpl", "", "deleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt", "enterDirectory", "Ljava/nio/file/SecureDirectoryStream;", "enterDirectory$PathsKt__PathRecursiveFunctionsKt", "handleEntry", "handleEntry$PathsKt__PathRecursiveFunctionsKt", "isDirectory", "entryName", "options", "", "Ljava/nio/file/LinkOption;", "isDirectory$PathsKt__PathRecursiveFunctionsKt", "(Ljava/nio/file/SecureDirectoryStream;Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Z", "toFileVisitResult", "Ljava/nio/file/FileVisitResult;", "toFileVisitResult$PathsKt__PathRecursiveFunctionsKt", "kotlin-stdlib-jdk7"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/io/path/PathsKt")
class PathsKt__PathRecursiveFunctionsKt extends kotlin.io.path.PathsKt__PathReadWriteKt {

    /* JADX INFO: compiled from: PathRecursiveFunctions.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1;

        static {
            int[] iArr = new int[kotlin.io.path.CopyActionResult.values().length];
            try {
                iArr[kotlin.io.path.CopyActionResult.CONTINUE.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                iArr[kotlin.io.path.CopyActionResult.TERMINATE.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                iArr[kotlin.io.path.CopyActionResult.SKIP_SUBTREE.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
            int[] iArr2 = new int[kotlin.io.path.OnErrorResult.values().length];
            try {
                iArr2[kotlin.io.path.OnErrorResult.TERMINATE.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e4) {
            }
            try {
                iArr2[kotlin.io.path.OnErrorResult.SKIP_SUBTREE.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e5) {
            }
            $EnumSwitchMapping$1 = iArr2;
        }
    }

    public static /* synthetic */ java.nio.file.Path copyToRecursively$default(java.nio.file.Path path, java.nio.file.Path path2, kotlin.jvm.functions.Function3 function3, boolean z, boolean z2, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            function3 = new kotlin.jvm.functions.Function3() { // from class: kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.copyToRecursively.1
                @Override // kotlin.jvm.functions.Function3
                public final java.lang.Void invoke(java.nio.file.Path path3, java.nio.file.Path path4, java.lang.Exception exception) throws java.lang.Exception {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(path3, "<anonymous parameter 0>");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(path4, "<anonymous parameter 1>");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
                    throw exception;
                }
            };
        }
        return kotlin.io.path.PathsKt.copyToRecursively(path, path2, (kotlin.jvm.functions.Function3<? super java.nio.file.Path, ? super java.nio.file.Path, ? super java.lang.Exception, ? extends kotlin.io.path.OnErrorResult>) function3, z, z2);
    }

    public static final java.nio.file.Path copyToRecursively(java.nio.file.Path $this$copyToRecursively, java.nio.file.Path target, kotlin.jvm.functions.Function3<? super java.nio.file.Path, ? super java.nio.file.Path, ? super java.lang.Exception, ? extends kotlin.io.path.OnErrorResult> onError, final boolean followLinks, boolean overwrite) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$copyToRecursively, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(target, "target");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onError, "onError");
        if (overwrite) {
            return kotlin.io.path.PathsKt.copyToRecursively($this$copyToRecursively, target, onError, followLinks, new kotlin.jvm.functions.Function3<kotlin.io.path.CopyActionContext, java.nio.file.Path, java.nio.file.Path, kotlin.io.path.CopyActionResult>() { // from class: kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.copyToRecursively.2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(3);
                }

                @Override // kotlin.jvm.functions.Function3
                public final kotlin.io.path.CopyActionResult invoke(kotlin.io.path.CopyActionContext copyToRecursively, java.nio.file.Path src, java.nio.file.Path dst) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(copyToRecursively, "$this$copyToRecursively");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(src, "src");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dst, "dst");
                    java.nio.file.LinkOption[] options = kotlin.io.path.LinkFollowing.INSTANCE.toLinkOptions(followLinks);
                    boolean dstIsDirectory = java.nio.file.Files.isDirectory(dst, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(new java.nio.file.LinkOption[]{java.nio.file.LinkOption.NOFOLLOW_LINKS}, 1));
                    java.nio.file.LinkOption[] linkOptionArr = (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(options, options.length);
                    boolean srcIsDirectory = java.nio.file.Files.isDirectory(src, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(linkOptionArr, linkOptionArr.length));
                    if (!srcIsDirectory || !dstIsDirectory) {
                        if (dstIsDirectory) {
                            kotlin.io.path.PathsKt.deleteRecursively(dst);
                        }
                        kotlin.jvm.internal.SpreadBuilder spreadBuilder = new kotlin.jvm.internal.SpreadBuilder(2);
                        spreadBuilder.addSpread(options);
                        spreadBuilder.add(java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        java.nio.file.CopyOption[] copyOptionArr = (java.nio.file.CopyOption[]) spreadBuilder.toArray(new java.nio.file.CopyOption[spreadBuilder.size()]);
                        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(java.nio.file.Files.copy(src, dst, (java.nio.file.CopyOption[]) java.util.Arrays.copyOf(copyOptionArr, copyOptionArr.length)), "copy(...)");
                    }
                    return kotlin.io.path.CopyActionResult.CONTINUE;
                }
            });
        }
        return kotlin.io.path.PathsKt.copyToRecursively$default($this$copyToRecursively, target, onError, followLinks, (kotlin.jvm.functions.Function3) null, 8, (java.lang.Object) null);
    }

    public static /* synthetic */ java.nio.file.Path copyToRecursively$default(java.nio.file.Path path, java.nio.file.Path path2, kotlin.jvm.functions.Function3 function3, final boolean z, kotlin.jvm.functions.Function3 function32, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            function3 = new kotlin.jvm.functions.Function3() { // from class: kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.copyToRecursively.3
                @Override // kotlin.jvm.functions.Function3
                public final java.lang.Void invoke(java.nio.file.Path path3, java.nio.file.Path path4, java.lang.Exception exception) throws java.lang.Exception {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(path3, "<anonymous parameter 0>");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(path4, "<anonymous parameter 1>");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
                    throw exception;
                }
            };
        }
        if ((i & 8) != 0) {
            function32 = new kotlin.jvm.functions.Function3<kotlin.io.path.CopyActionContext, java.nio.file.Path, java.nio.file.Path, kotlin.io.path.CopyActionResult>() { // from class: kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.copyToRecursively.4
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(3);
                }

                @Override // kotlin.jvm.functions.Function3
                public final kotlin.io.path.CopyActionResult invoke(kotlin.io.path.CopyActionContext $this$null, java.nio.file.Path src, java.nio.file.Path dst) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$null, "$this$null");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(src, "src");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(dst, "dst");
                    return $this$null.copyToIgnoringExistingDirectory(src, dst, z);
                }
            };
        }
        return kotlin.io.path.PathsKt.copyToRecursively(path, path2, (kotlin.jvm.functions.Function3<? super java.nio.file.Path, ? super java.nio.file.Path, ? super java.lang.Exception, ? extends kotlin.io.path.OnErrorResult>) function3, z, (kotlin.jvm.functions.Function3<? super kotlin.io.path.CopyActionContext, ? super java.nio.file.Path, ? super java.nio.file.Path, ? extends kotlin.io.path.CopyActionResult>) function32);
    }

    public static final java.nio.file.Path copyToRecursively(final java.nio.file.Path $this$copyToRecursively, final java.nio.file.Path target, final kotlin.jvm.functions.Function3<? super java.nio.file.Path, ? super java.nio.file.Path, ? super java.lang.Exception, ? extends kotlin.io.path.OnErrorResult> onError, boolean followLinks, final kotlin.jvm.functions.Function3<? super kotlin.io.path.CopyActionContext, ? super java.nio.file.Path, ? super java.nio.file.Path, ? extends kotlin.io.path.CopyActionResult> copyAction) throws java.nio.file.FileSystemException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$copyToRecursively, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(target, "target");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onError, "onError");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(copyAction, "copyAction");
        java.nio.file.LinkOption[] linkOptions = kotlin.io.path.LinkFollowing.INSTANCE.toLinkOptions(followLinks);
        java.nio.file.LinkOption[] linkOptionArr = (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(linkOptions, linkOptions.length);
        if (!java.nio.file.Files.exists($this$copyToRecursively, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(linkOptionArr, linkOptionArr.length))) {
            throw new java.nio.file.NoSuchFileException($this$copyToRecursively.toString(), target.toString(), "The source file doesn't exist.");
        }
        boolean isSubdirectory = false;
        if (java.nio.file.Files.exists($this$copyToRecursively, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(new java.nio.file.LinkOption[0], 0)) && (followLinks || !java.nio.file.Files.isSymbolicLink($this$copyToRecursively))) {
            boolean targetExistsAndNotSymlink = java.nio.file.Files.exists(target, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(new java.nio.file.LinkOption[0], 0)) && !java.nio.file.Files.isSymbolicLink(target);
            if (!targetExistsAndNotSymlink || !java.nio.file.Files.isSameFile($this$copyToRecursively, target)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual($this$copyToRecursively.getFileSystem(), target.getFileSystem())) {
                    if (targetExistsAndNotSymlink) {
                        isSubdirectory = target.toRealPath(new java.nio.file.LinkOption[0]).startsWith($this$copyToRecursively.toRealPath(new java.nio.file.LinkOption[0]));
                    } else {
                        java.nio.file.Path it = target.getParent();
                        if (it != null && java.nio.file.Files.exists(it, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(new java.nio.file.LinkOption[0], 0)) && it.toRealPath(new java.nio.file.LinkOption[0]).startsWith($this$copyToRecursively.toRealPath(new java.nio.file.LinkOption[0]))) {
                            isSubdirectory = true;
                        }
                    }
                }
                if (isSubdirectory) {
                    throw new java.nio.file.FileSystemException($this$copyToRecursively.toString(), target.toString(), "Recursively copying a directory into its subdirectory is prohibited.");
                }
            }
        }
        kotlin.io.path.PathsKt.visitFileTree$default($this$copyToRecursively, 0, followLinks, new kotlin.jvm.functions.Function1<kotlin.io.path.FileVisitorBuilder, kotlin.Unit>() { // from class: kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.copyToRecursively.5
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(1);
            }

            /* JADX INFO: renamed from: kotlin.io.path.PathsKt__PathRecursiveFunctionsKt$copyToRecursively$5$1, reason: invalid class name */
            /* JADX INFO: compiled from: PathRecursiveFunctions.kt */
            @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass1 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function2<java.nio.file.Path, java.nio.file.attribute.BasicFileAttributes, java.nio.file.FileVisitResult> {
                final /* synthetic */ kotlin.jvm.functions.Function3<kotlin.io.path.CopyActionContext, java.nio.file.Path, java.nio.file.Path, kotlin.io.path.CopyActionResult> $copyAction;
                final /* synthetic */ kotlin.jvm.functions.Function3<java.nio.file.Path, java.nio.file.Path, java.lang.Exception, kotlin.io.path.OnErrorResult> $onError;
                final /* synthetic */ java.nio.file.Path $target;
                final /* synthetic */ java.nio.file.Path $this_copyToRecursively;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                /* JADX WARN: Multi-variable type inference failed */
                AnonymousClass1(kotlin.jvm.functions.Function3<? super kotlin.io.path.CopyActionContext, ? super java.nio.file.Path, ? super java.nio.file.Path, ? extends kotlin.io.path.CopyActionResult> function3, java.nio.file.Path path, java.nio.file.Path path2, kotlin.jvm.functions.Function3<? super java.nio.file.Path, ? super java.nio.file.Path, ? super java.lang.Exception, ? extends kotlin.io.path.OnErrorResult> function32) {
                    super(2, kotlin.jvm.internal.Intrinsics.Kotlin.class, "copy", "copyToRecursively$copy$PathsKt__PathRecursiveFunctionsKt(Lkotlin/jvm/functions/Function3;Ljava/nio/file/Path;Ljava/nio/file/Path;Lkotlin/jvm/functions/Function3;Ljava/nio/file/Path;Ljava/nio/file/attribute/BasicFileAttributes;)Ljava/nio/file/FileVisitResult;", 0);
                    this.$copyAction = function3;
                    this.$this_copyToRecursively = path;
                    this.$target = path2;
                    this.$onError = function32;
                }

                @Override // kotlin.jvm.functions.Function2
                public final java.nio.file.FileVisitResult invoke(java.nio.file.Path p0, java.nio.file.attribute.BasicFileAttributes p1) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p1, "p1");
                    return kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.copyToRecursively$copy$PathsKt__PathRecursiveFunctionsKt(this.$copyAction, this.$this_copyToRecursively, this.$target, this.$onError, p0, p1);
                }
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ kotlin.Unit invoke(kotlin.io.path.FileVisitorBuilder fileVisitorBuilder) {
                invoke2(fileVisitorBuilder);
                return kotlin.Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: kotlin.io.path.PathsKt__PathRecursiveFunctionsKt$copyToRecursively$5$2, reason: invalid class name */
            /* JADX INFO: compiled from: PathRecursiveFunctions.kt */
            @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass2 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function2<java.nio.file.Path, java.nio.file.attribute.BasicFileAttributes, java.nio.file.FileVisitResult> {
                final /* synthetic */ kotlin.jvm.functions.Function3<kotlin.io.path.CopyActionContext, java.nio.file.Path, java.nio.file.Path, kotlin.io.path.CopyActionResult> $copyAction;
                final /* synthetic */ kotlin.jvm.functions.Function3<java.nio.file.Path, java.nio.file.Path, java.lang.Exception, kotlin.io.path.OnErrorResult> $onError;
                final /* synthetic */ java.nio.file.Path $target;
                final /* synthetic */ java.nio.file.Path $this_copyToRecursively;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                /* JADX WARN: Multi-variable type inference failed */
                AnonymousClass2(kotlin.jvm.functions.Function3<? super kotlin.io.path.CopyActionContext, ? super java.nio.file.Path, ? super java.nio.file.Path, ? extends kotlin.io.path.CopyActionResult> function3, java.nio.file.Path path, java.nio.file.Path path2, kotlin.jvm.functions.Function3<? super java.nio.file.Path, ? super java.nio.file.Path, ? super java.lang.Exception, ? extends kotlin.io.path.OnErrorResult> function32) {
                    super(2, kotlin.jvm.internal.Intrinsics.Kotlin.class, "copy", "copyToRecursively$copy$PathsKt__PathRecursiveFunctionsKt(Lkotlin/jvm/functions/Function3;Ljava/nio/file/Path;Ljava/nio/file/Path;Lkotlin/jvm/functions/Function3;Ljava/nio/file/Path;Ljava/nio/file/attribute/BasicFileAttributes;)Ljava/nio/file/FileVisitResult;", 0);
                    this.$copyAction = function3;
                    this.$this_copyToRecursively = path;
                    this.$target = path2;
                    this.$onError = function32;
                }

                @Override // kotlin.jvm.functions.Function2
                public final java.nio.file.FileVisitResult invoke(java.nio.file.Path p0, java.nio.file.attribute.BasicFileAttributes p1) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p1, "p1");
                    return kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.copyToRecursively$copy$PathsKt__PathRecursiveFunctionsKt(this.$copyAction, this.$this_copyToRecursively, this.$target, this.$onError, p0, p1);
                }
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(kotlin.io.path.FileVisitorBuilder visitFileTree) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(visitFileTree, "$this$visitFileTree");
                visitFileTree.onPreVisitDirectory(new kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.AnonymousClass5.AnonymousClass1(copyAction, $this$copyToRecursively, target, onError));
                visitFileTree.onVisitFile(new kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.AnonymousClass5.AnonymousClass2(copyAction, $this$copyToRecursively, target, onError));
                visitFileTree.onVisitFileFailed(new kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.AnonymousClass5.AnonymousClass3(onError, $this$copyToRecursively, target));
                final kotlin.jvm.functions.Function3<java.nio.file.Path, java.nio.file.Path, java.lang.Exception, kotlin.io.path.OnErrorResult> function3 = onError;
                final java.nio.file.Path path = $this$copyToRecursively;
                final java.nio.file.Path path2 = target;
                visitFileTree.onPostVisitDirectory(new kotlin.jvm.functions.Function2<java.nio.file.Path, java.io.IOException, java.nio.file.FileVisitResult>() { // from class: kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.copyToRecursively.5.4
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    /* JADX WARN: Multi-variable type inference failed */
                    {
                        super(2);
                    }

                    @Override // kotlin.jvm.functions.Function2
                    public final java.nio.file.FileVisitResult invoke(java.nio.file.Path directory, java.io.IOException exception) {
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(directory, "directory");
                        if (exception != null) {
                            return kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(function3, path, path2, directory, exception);
                        }
                        return java.nio.file.FileVisitResult.CONTINUE;
                    }
                });
            }

            /* JADX INFO: renamed from: kotlin.io.path.PathsKt__PathRecursiveFunctionsKt$copyToRecursively$5$3, reason: invalid class name */
            /* JADX INFO: compiled from: PathRecursiveFunctions.kt */
            @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass3 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function2<java.nio.file.Path, java.lang.Exception, java.nio.file.FileVisitResult> {
                final /* synthetic */ kotlin.jvm.functions.Function3<java.nio.file.Path, java.nio.file.Path, java.lang.Exception, kotlin.io.path.OnErrorResult> $onError;
                final /* synthetic */ java.nio.file.Path $target;
                final /* synthetic */ java.nio.file.Path $this_copyToRecursively;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                /* JADX WARN: Multi-variable type inference failed */
                AnonymousClass3(kotlin.jvm.functions.Function3<? super java.nio.file.Path, ? super java.nio.file.Path, ? super java.lang.Exception, ? extends kotlin.io.path.OnErrorResult> function3, java.nio.file.Path path, java.nio.file.Path path2) {
                    super(2, kotlin.jvm.internal.Intrinsics.Kotlin.class, "error", "copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(Lkotlin/jvm/functions/Function3;Ljava/nio/file/Path;Ljava/nio/file/Path;Ljava/nio/file/Path;Ljava/lang/Exception;)Ljava/nio/file/FileVisitResult;", 0);
                    this.$onError = function3;
                    this.$this_copyToRecursively = path;
                    this.$target = path2;
                }

                @Override // kotlin.jvm.functions.Function2
                public final java.nio.file.FileVisitResult invoke(java.nio.file.Path p0, java.lang.Exception p1) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p1, "p1");
                    return kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(this.$onError, this.$this_copyToRecursively, this.$target, p0, p1);
                }
            }
        }, 1, (java.lang.Object) null);
        return target;
    }

    private static final java.nio.file.Path copyToRecursively$destination$PathsKt__PathRecursiveFunctionsKt(java.nio.file.Path $this_copyToRecursively, java.nio.file.Path $target, java.nio.file.Path source) {
        java.nio.file.Path relativePath = kotlin.io.path.PathsKt.relativeTo(source, $this_copyToRecursively);
        java.nio.file.Path pathResolve = $target.resolve(relativePath.toString());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(pathResolve, "resolve(...)");
        return pathResolve;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final java.nio.file.FileVisitResult copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(kotlin.jvm.functions.Function3<? super java.nio.file.Path, ? super java.nio.file.Path, ? super java.lang.Exception, ? extends kotlin.io.path.OnErrorResult> function3, java.nio.file.Path $this_copyToRecursively, java.nio.file.Path $target, java.nio.file.Path source, java.lang.Exception exception) {
        return toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(function3.invoke(source, copyToRecursively$destination$PathsKt__PathRecursiveFunctionsKt($this_copyToRecursively, $target, source), exception));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final java.nio.file.FileVisitResult copyToRecursively$copy$PathsKt__PathRecursiveFunctionsKt(kotlin.jvm.functions.Function3<? super kotlin.io.path.CopyActionContext, ? super java.nio.file.Path, ? super java.nio.file.Path, ? extends kotlin.io.path.CopyActionResult> function3, java.nio.file.Path $this_copyToRecursively, java.nio.file.Path $target, kotlin.jvm.functions.Function3<? super java.nio.file.Path, ? super java.nio.file.Path, ? super java.lang.Exception, ? extends kotlin.io.path.OnErrorResult> function32, java.nio.file.Path source, java.nio.file.attribute.BasicFileAttributes attributes) {
        try {
            return toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(function3.invoke(kotlin.io.path.DefaultCopyActionContext.INSTANCE, source, copyToRecursively$destination$PathsKt__PathRecursiveFunctionsKt($this_copyToRecursively, $target, source)));
        } catch (java.lang.Exception exception) {
            return copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(function32, $this_copyToRecursively, $target, source, exception);
        }
    }

    private static final java.nio.file.FileVisitResult toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(kotlin.io.path.CopyActionResult $this$toFileVisitResult) {
        switch (kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.WhenMappings.$EnumSwitchMapping$0[$this$toFileVisitResult.ordinal()]) {
            case 1:
                return java.nio.file.FileVisitResult.CONTINUE;
            case 2:
                return java.nio.file.FileVisitResult.TERMINATE;
            case 3:
                return java.nio.file.FileVisitResult.SKIP_SUBTREE;
            default:
                throw new kotlin.NoWhenBranchMatchedException();
        }
    }

    private static final java.nio.file.FileVisitResult toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(kotlin.io.path.OnErrorResult $this$toFileVisitResult) {
        switch (kotlin.io.path.PathsKt__PathRecursiveFunctionsKt.WhenMappings.$EnumSwitchMapping$1[$this$toFileVisitResult.ordinal()]) {
            case 1:
                return java.nio.file.FileVisitResult.TERMINATE;
            case 2:
                return java.nio.file.FileVisitResult.SKIP_SUBTREE;
            default:
                throw new kotlin.NoWhenBranchMatchedException();
        }
    }

    public static final void deleteRecursively(java.nio.file.Path $this$deleteRecursively) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$deleteRecursively, "<this>");
        java.lang.Iterable iterableDeleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt = deleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt($this$deleteRecursively);
        if (!((java.util.Collection) iterableDeleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt).isEmpty()) {
            java.nio.file.FileSystemException $this$deleteRecursively_u24lambda_u242 = new java.nio.file.FileSystemException("Failed to delete one or more files. See suppressed exceptions for details.");
            java.lang.Iterable $this$forEach$iv = iterableDeleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt;
            for (java.lang.Object element$iv : $this$forEach$iv) {
                java.lang.Exception it = (java.lang.Exception) element$iv;
                kotlin.ExceptionsKt.addSuppressed($this$deleteRecursively_u24lambda_u242, it);
            }
            throw $this$deleteRecursively_u24lambda_u242;
        }
    }

    private static final java.util.List<java.lang.Exception> deleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt(java.nio.file.Path $this$deleteRecursivelyImpl) throws java.io.IOException {
        java.nio.file.DirectoryStream<java.nio.file.Path> directoryStreamNewDirectoryStream;
        kotlin.io.path.ExceptionsCollector collector = new kotlin.io.path.ExceptionsCollector(0, 1, null);
        boolean useInsecure = true;
        java.nio.file.Path parent = $this$deleteRecursivelyImpl.getParent();
        if (parent != null) {
            try {
                directoryStreamNewDirectoryStream = java.nio.file.Files.newDirectoryStream(parent);
            } catch (java.lang.Throwable th) {
                directoryStreamNewDirectoryStream = null;
            }
            if (directoryStreamNewDirectoryStream != null) {
                java.nio.file.DirectoryStream<java.nio.file.Path> directoryStream = directoryStreamNewDirectoryStream;
                try {
                    java.nio.file.DirectoryStream<java.nio.file.Path> directoryStream2 = directoryStream;
                    if (directoryStream2 instanceof java.nio.file.SecureDirectoryStream) {
                        useInsecure = false;
                        collector.setPath(parent);
                        java.nio.file.Path fileName = $this$deleteRecursivelyImpl.getFileName();
                        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(fileName, "getFileName(...)");
                        handleEntry$PathsKt__PathRecursiveFunctionsKt((java.nio.file.SecureDirectoryStream) directoryStream2, fileName, collector);
                    }
                    kotlin.Unit unit = kotlin.Unit.INSTANCE;
                    kotlin.io.CloseableKt.closeFinally(directoryStream, null);
                } finally {
                }
            }
        }
        if (useInsecure) {
            insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt($this$deleteRecursivelyImpl, collector);
        }
        return collector.getCollectedExceptions();
    }

    private static final void collectIfThrows$PathsKt__PathRecursiveFunctionsKt(kotlin.io.path.ExceptionsCollector collector, kotlin.jvm.functions.Function0<kotlin.Unit> function0) {
        try {
            function0.invoke();
        } catch (java.lang.Exception exception) {
            collector.collect(exception);
        }
    }

    private static final <R> R tryIgnoreNoSuchFileException$PathsKt__PathRecursiveFunctionsKt(kotlin.jvm.functions.Function0<? extends R> function0) {
        try {
            return function0.invoke();
        } catch (java.nio.file.NoSuchFileException e) {
            return null;
        }
    }

    private static final void handleEntry$PathsKt__PathRecursiveFunctionsKt(java.nio.file.SecureDirectoryStream<java.nio.file.Path> secureDirectoryStream, java.nio.file.Path name, kotlin.io.path.ExceptionsCollector collector) {
        collector.enterEntry(name);
        try {
            if (isDirectory$PathsKt__PathRecursiveFunctionsKt(secureDirectoryStream, name, java.nio.file.LinkOption.NOFOLLOW_LINKS)) {
                int preEnterTotalExceptions = collector.getTotalExceptions();
                enterDirectory$PathsKt__PathRecursiveFunctionsKt(secureDirectoryStream, name, collector);
                if (preEnterTotalExceptions == collector.getTotalExceptions()) {
                    try {
                        secureDirectoryStream.deleteDirectory(name);
                        kotlin.Unit unit = kotlin.Unit.INSTANCE;
                    } catch (java.nio.file.NoSuchFileException e) {
                    }
                }
            } else {
                try {
                    secureDirectoryStream.deleteFile(name);
                    kotlin.Unit unit2 = kotlin.Unit.INSTANCE;
                } catch (java.nio.file.NoSuchFileException e2) {
                }
            }
        } catch (java.lang.Exception exception$iv) {
            collector.collect(exception$iv);
        }
        collector.exitEntry(name);
    }

    private static final void enterDirectory$PathsKt__PathRecursiveFunctionsKt(java.nio.file.SecureDirectoryStream<java.nio.file.Path> secureDirectoryStream, java.nio.file.Path name, kotlin.io.path.ExceptionsCollector collector) {
        java.nio.file.SecureDirectoryStream<java.nio.file.Path> secureDirectoryStreamNewDirectoryStream;
        try {
            try {
                secureDirectoryStreamNewDirectoryStream = secureDirectoryStream.newDirectoryStream(name, java.nio.file.LinkOption.NOFOLLOW_LINKS);
            } catch (java.lang.Exception exception$iv) {
                collector.collect(exception$iv);
                return;
            }
        } catch (java.nio.file.NoSuchFileException e) {
            secureDirectoryStreamNewDirectoryStream = null;
        }
        if (secureDirectoryStreamNewDirectoryStream == null) {
            return;
        }
        java.nio.file.SecureDirectoryStream<java.nio.file.Path> secureDirectoryStream2 = secureDirectoryStreamNewDirectoryStream;
        try {
            java.nio.file.SecureDirectoryStream<java.nio.file.Path> secureDirectoryStream3 = secureDirectoryStream2;
            for (java.nio.file.Path entry : secureDirectoryStream3) {
                java.nio.file.Path fileName = entry.getFileName();
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(fileName, "getFileName(...)");
                handleEntry$PathsKt__PathRecursiveFunctionsKt(secureDirectoryStream3, fileName, collector);
            }
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
            kotlin.io.CloseableKt.closeFinally(secureDirectoryStream2, null);
        } finally {
        }
    }

    private static final boolean isDirectory$PathsKt__PathRecursiveFunctionsKt(java.nio.file.SecureDirectoryStream<java.nio.file.Path> secureDirectoryStream, java.nio.file.Path entryName, java.nio.file.LinkOption... options) {
        java.lang.Boolean boolValueOf;
        try {
            boolValueOf = java.lang.Boolean.valueOf(((java.nio.file.attribute.BasicFileAttributeView) secureDirectoryStream.getFileAttributeView(entryName, java.nio.file.attribute.BasicFileAttributeView.class, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(options, options.length))).readAttributes().isDirectory());
        } catch (java.nio.file.NoSuchFileException e) {
            boolValueOf = null;
        }
        if (boolValueOf != null) {
            return boolValueOf.booleanValue();
        }
        return false;
    }

    private static final void insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt(java.nio.file.Path entry, kotlin.io.path.ExceptionsCollector collector) {
        try {
            if (java.nio.file.Files.isDirectory(entry, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(new java.nio.file.LinkOption[]{java.nio.file.LinkOption.NOFOLLOW_LINKS}, 1))) {
                int preEnterTotalExceptions = collector.getTotalExceptions();
                insecureEnterDirectory$PathsKt__PathRecursiveFunctionsKt(entry, collector);
                if (preEnterTotalExceptions == collector.getTotalExceptions()) {
                    java.nio.file.Files.deleteIfExists(entry);
                }
            } else {
                java.nio.file.Files.deleteIfExists(entry);
            }
        } catch (java.lang.Exception exception$iv) {
            collector.collect(exception$iv);
        }
    }

    private static final void insecureEnterDirectory$PathsKt__PathRecursiveFunctionsKt(java.nio.file.Path path, kotlin.io.path.ExceptionsCollector collector) {
        java.nio.file.DirectoryStream<java.nio.file.Path> directoryStreamNewDirectoryStream;
        try {
            try {
                directoryStreamNewDirectoryStream = java.nio.file.Files.newDirectoryStream(path);
            } catch (java.lang.Exception exception$iv) {
                collector.collect(exception$iv);
                return;
            }
        } catch (java.nio.file.NoSuchFileException e) {
            directoryStreamNewDirectoryStream = null;
        }
        if (directoryStreamNewDirectoryStream == null) {
            return;
        }
        java.nio.file.DirectoryStream<java.nio.file.Path> directoryStream = directoryStreamNewDirectoryStream;
        try {
            for (java.nio.file.Path entry : directoryStream) {
                kotlin.jvm.internal.Intrinsics.checkNotNull(entry);
                insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt(entry, collector);
            }
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
            kotlin.io.CloseableKt.closeFinally(directoryStream, null);
        } finally {
        }
    }
}
