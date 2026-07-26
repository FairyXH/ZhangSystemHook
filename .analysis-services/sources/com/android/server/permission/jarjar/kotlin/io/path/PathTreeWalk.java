package com.android.server.permission.jarjar.kotlin.io.path;

/* JADX INFO: compiled from: PathTreeWalk.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0015H\u0002J\u000e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0015H\u0002J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00020\u0015H\u0096\u0002JB\u0010\u0018\u001a\u00020\u0019*\b\u0012\u0004\u0012\u00020\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0018\u0010\u001f\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0!\u0012\u0004\u0012\u00020\u00190 H\u0082H¢\u0006\u0002\u0010\"R\u0014\u0010\b\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u0014\u0010\u000e\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000bR\u001a\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u00058BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0018\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\u0003\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lkotlin/io/path/PathTreeWalk;", "Lkotlin/sequences/Sequence;", "Ljava/nio/file/Path;", "start", "options", "", "Lkotlin/io/path/PathWalkOption;", "(Ljava/nio/file/Path;[Lkotlin/io/path/PathWalkOption;)V", "followLinks", "", "getFollowLinks", "()Z", "includeDirectories", "getIncludeDirectories", "isBFS", "linkOptions", "Ljava/nio/file/LinkOption;", "getLinkOptions", "()[Ljava/nio/file/LinkOption;", "[Lkotlin/io/path/PathWalkOption;", "bfsIterator", "", "dfsIterator", "iterator", "yieldIfNeeded", "", "Lkotlin/sequences/SequenceScope;", "node", "Lkotlin/io/path/PathNode;", "entriesReader", "Lkotlin/io/path/DirectoryEntriesReader;", "entriesAction", "Lkotlin/Function1;", "", "(Lkotlin/sequences/SequenceScope;Lkotlin/io/path/PathNode;Lkotlin/io/path/DirectoryEntriesReader;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib-jdk7"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class PathTreeWalk implements com.android.server.permission.jarjar.kotlin.sequences.Sequence<java.nio.file.Path> {
    private final com.android.server.permission.jarjar.kotlin.io.path.PathWalkOption[] options;
    private final java.nio.file.Path start;

    public PathTreeWalk(java.nio.file.Path start, com.android.server.permission.jarjar.kotlin.io.path.PathWalkOption[] options) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(start, "start");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        this.start = start;
        this.options = options;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean getFollowLinks() {
        return com.android.server.permission.jarjar.kotlin.collections.ArraysKt.contains(this.options, com.android.server.permission.jarjar.kotlin.io.path.PathWalkOption.FOLLOW_LINKS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.nio.file.LinkOption[] getLinkOptions() {
        return com.android.server.permission.jarjar.kotlin.io.path.LinkFollowing.INSTANCE.toLinkOptions(getFollowLinks());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean getIncludeDirectories() {
        return com.android.server.permission.jarjar.kotlin.collections.ArraysKt.contains(this.options, com.android.server.permission.jarjar.kotlin.io.path.PathWalkOption.INCLUDE_DIRECTORIES);
    }

    private final boolean isBFS() {
        return com.android.server.permission.jarjar.kotlin.collections.ArraysKt.contains(this.options, com.android.server.permission.jarjar.kotlin.io.path.PathWalkOption.BREADTH_FIRST);
    }

    @Override // com.android.server.permission.jarjar.kotlin.sequences.Sequence
    public java.util.Iterator<java.nio.file.Path> iterator() {
        return isBFS() ? bfsIterator() : dfsIterator();
    }

    private final java.lang.Object yieldIfNeeded(com.android.server.permission.jarjar.kotlin.sequences.SequenceScope<? super java.nio.file.Path> sequenceScope, com.android.server.permission.jarjar.kotlin.io.path.PathNode node, com.android.server.permission.jarjar.kotlin.io.path.DirectoryEntriesReader entriesReader, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.List<com.android.server.permission.jarjar.kotlin.io.path.PathNode>, com.android.server.permission.jarjar.kotlin.Unit> function1, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> continuation) throws java.nio.file.FileSystemLoopException {
        java.nio.file.Path path = node.getPath();
        java.nio.file.LinkOption[] linkOptions = getLinkOptions();
        java.nio.file.LinkOption[] linkOptionArr = (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(linkOptions, linkOptions.length);
        if (java.nio.file.Files.isDirectory(path, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(linkOptionArr, linkOptionArr.length))) {
            if (!com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalkKt.createsCycle(node)) {
                if (getIncludeDirectories()) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.mark(0);
                    sequenceScope.yield(path, continuation);
                    com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.mark(1);
                }
                java.nio.file.LinkOption[] linkOptions2 = getLinkOptions();
                java.nio.file.LinkOption[] linkOptionArr2 = (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(linkOptions2, linkOptions2.length);
                if (java.nio.file.Files.isDirectory(path, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(linkOptionArr2, linkOptionArr2.length))) {
                    function1.invoke(entriesReader.readEntries(node));
                }
            } else {
                throw new java.nio.file.FileSystemLoopException(path.toString());
            }
        } else if (java.nio.file.Files.exists(path, (java.nio.file.LinkOption[]) java.util.Arrays.copyOf(new java.nio.file.LinkOption[]{java.nio.file.LinkOption.NOFOLLOW_LINKS}, 1))) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.mark(0);
            sequenceScope.yield(path, continuation);
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.mark(1);
            return com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
        return com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk$dfsIterator$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: PathTreeWalk.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Ljava/nio/file/Path;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk$dfsIterator$1", f = "PathTreeWalk.kt", i = {0, 0, 0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3}, l = {184, 190, 199, 205}, m = "invokeSuspend", n = {"$this$iterator", "stack", "entriesReader", "startNode", "this_$iv", "path$iv", "$this$iterator", "stack", "entriesReader", "$this$iterator", "stack", "entriesReader", "pathNode", "this_$iv", "path$iv", "$this$iterator", "stack", "entriesReader"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1", "L$2"})
    static final class C00361 extends com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.RestrictedSuspendLambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<com.android.server.permission.jarjar.kotlin.sequences.SequenceScope<? super java.nio.file.Path>, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit>, java.lang.Object> {
        private /* synthetic */ java.lang.Object L$0;
        java.lang.Object L$1;
        java.lang.Object L$2;
        java.lang.Object L$3;
        java.lang.Object L$4;
        java.lang.Object L$5;
        int label;

        C00361(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.C00361> continuation) {
            super(2, continuation);
        }

        @Override // com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final com.android.server.permission.jarjar.kotlin.coroutines.Continuation<com.android.server.permission.jarjar.kotlin.Unit> create(java.lang.Object obj, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<?> continuation) {
            com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.C00361 c00361 = com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.this.new C00361(continuation);
            c00361.L$0 = obj;
            return c00361;
        }

        @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(com.android.server.permission.jarjar.kotlin.sequences.SequenceScope<? super java.nio.file.Path> sequenceScope, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> continuation) {
            return ((com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.C00361) create(sequenceScope, continuation)).invokeSuspend(com.android.server.permission.jarjar.kotlin.Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x012f  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x017b  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x0207  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:49:0x0205 -> B:33:0x0171). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:50:0x0207 -> B:33:0x0171). Please report as a decompilation issue!!! */
        @Override // com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object invokeSuspend(java.lang.Object r18) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 616
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.C00361.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    private final java.util.Iterator<java.nio.file.Path> dfsIterator() {
        return com.android.server.permission.jarjar.kotlin.sequences.SequencesKt.iterator(new com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.C00361(null));
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk$bfsIterator$1, reason: invalid class name */
    /* JADX INFO: compiled from: PathTreeWalk.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Ljava/nio/file/Path;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.DebugMetadata(c = "com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk$bfsIterator$1", f = "PathTreeWalk.kt", i = {0, 0, 0, 0, 0, 0, 1, 1, 1}, l = {184, 190}, m = "invokeSuspend", n = {"$this$iterator", "queue", "entriesReader", "pathNode", "this_$iv", "path$iv", "$this$iterator", "queue", "entriesReader"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1", "L$2"})
    static final class AnonymousClass1 extends com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.RestrictedSuspendLambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<com.android.server.permission.jarjar.kotlin.sequences.SequenceScope<? super java.nio.file.Path>, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit>, java.lang.Object> {
        private /* synthetic */ java.lang.Object L$0;
        java.lang.Object L$1;
        java.lang.Object L$2;
        java.lang.Object L$3;
        java.lang.Object L$4;
        java.lang.Object L$5;
        int label;

        AnonymousClass1(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.AnonymousClass1> continuation) {
            super(2, continuation);
        }

        @Override // com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final com.android.server.permission.jarjar.kotlin.coroutines.Continuation<com.android.server.permission.jarjar.kotlin.Unit> create(java.lang.Object obj, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<?> continuation) {
            com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.AnonymousClass1 anonymousClass1 = com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.this.new AnonymousClass1(continuation);
            anonymousClass1.L$0 = obj;
            return anonymousClass1;
        }

        @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(com.android.server.permission.jarjar.kotlin.sequences.SequenceScope<? super java.nio.file.Path> sequenceScope, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit> continuation) {
            return ((com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.AnonymousClass1) create(sequenceScope, continuation)).invokeSuspend(com.android.server.permission.jarjar.kotlin.Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0094  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0106  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0104 -> B:9:0x0089). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x0106 -> B:9:0x0089). Please report as a decompilation issue!!! */
        @Override // com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object invokeSuspend(java.lang.Object r17) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 346
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.AnonymousClass1.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    private final java.util.Iterator<java.nio.file.Path> bfsIterator() {
        return com.android.server.permission.jarjar.kotlin.sequences.SequencesKt.iterator(new com.android.server.permission.jarjar.kotlin.io.path.PathTreeWalk.AnonymousClass1(null));
    }
}
