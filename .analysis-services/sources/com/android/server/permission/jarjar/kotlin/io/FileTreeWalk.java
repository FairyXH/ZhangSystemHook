package com.android.server.permission.jarjar.kotlin.io;

/* JADX INFO: compiled from: FileTreeWalk.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\u001a\u001b\u001cB\u0019\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006B\u0089\u0001\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u0014\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\b\u0012\u0014\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b\u00128\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\r\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0014¢\u0006\u0002\u0010\u0015J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0017H\u0096\u0002J\u000e\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0014J\u001a\u0010\u0007\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t0\bJ \u0010\f\u001a\u00020\u00002\u0018\u0010\u0019\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u000b0\rJ\u001a\u0010\n\u001a\u00020\u00002\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b0\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u0082\u0004¢\u0006\u0002\n\u0000R@\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0013\u0012\u00110\u0011¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0012\u0012\u0004\u0012\u00020\u000b\u0018\u00010\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u000b\u0018\u00010\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lkotlin/io/FileTreeWalk;", "Lkotlin/sequences/Sequence;", "Ljava/io/File;", "start", "direction", "Lkotlin/io/FileWalkDirection;", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;)V", "onEnter", "Lkotlin/Function1;", "", "onLeave", "", "onFail", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "f", "Ljava/io/IOException;", "e", "maxDepth", "", "(Ljava/io/File;Lkotlin/io/FileWalkDirection;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;I)V", "iterator", "", "depth", "function", "DirectoryState", "FileTreeWalkIterator", "WalkState", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class FileTreeWalk implements com.android.server.permission.jarjar.kotlin.sequences.Sequence<java.io.File> {
    private final com.android.server.permission.jarjar.kotlin.io.FileWalkDirection direction;
    private final int maxDepth;
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<java.io.File, java.lang.Boolean> onEnter;
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<java.io.File, java.io.IOException, com.android.server.permission.jarjar.kotlin.Unit> onFail;
    private final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<java.io.File, com.android.server.permission.jarjar.kotlin.Unit> onLeave;
    private final java.io.File start;

    /* JADX WARN: Multi-variable type inference failed */
    private FileTreeWalk(java.io.File start, com.android.server.permission.jarjar.kotlin.io.FileWalkDirection direction, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.io.File, java.lang.Boolean> function1, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.io.File, com.android.server.permission.jarjar.kotlin.Unit> function12, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.io.File, ? super java.io.IOException, com.android.server.permission.jarjar.kotlin.Unit> function2, int maxDepth) {
        this.start = start;
        this.direction = direction;
        this.onEnter = function1;
        this.onLeave = function12;
        this.onFail = function2;
        this.maxDepth = maxDepth;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /* synthetic */ FileTreeWalk(java.io.File file, com.android.server.permission.jarjar.kotlin.io.FileWalkDirection fileWalkDirection, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 function1, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 function12, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2 function2, int i, int i2, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        com.android.server.permission.jarjar.kotlin.io.FileWalkDirection fileWalkDirection2;
        int i3;
        if ((i2 & 2) == 0) {
            fileWalkDirection2 = fileWalkDirection;
        } else {
            fileWalkDirection2 = com.android.server.permission.jarjar.kotlin.io.FileWalkDirection.TOP_DOWN;
        }
        if ((i2 & 32) == 0) {
            i3 = i;
        } else {
            i3 = Integer.MAX_VALUE;
        }
        this(file, fileWalkDirection2, function1, function12, function2, i3);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public FileTreeWalk(java.io.File start, com.android.server.permission.jarjar.kotlin.io.FileWalkDirection direction) {
        this(start, direction, null, null, null, 0, 32, null);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(start, "start");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(direction, "direction");
    }

    public /* synthetic */ FileTreeWalk(java.io.File file, com.android.server.permission.jarjar.kotlin.io.FileWalkDirection fileWalkDirection, int i, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(file, (i & 2) != 0 ? com.android.server.permission.jarjar.kotlin.io.FileWalkDirection.TOP_DOWN : fileWalkDirection);
    }

    @Override // com.android.server.permission.jarjar.kotlin.sequences.Sequence
    public java.util.Iterator<java.io.File> iterator() {
        return new com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator();
    }

    /* JADX INFO: compiled from: FileTreeWalk.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H&R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\b"}, d2 = {"Lkotlin/io/FileTreeWalk$WalkState;", "", "root", "Ljava/io/File;", "(Ljava/io/File;)V", "getRoot", "()Ljava/io/File;", "step", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static abstract class WalkState {
        private final java.io.File root;

        public abstract java.io.File step();

        public WalkState(java.io.File root) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(root, "root");
            this.root = root;
        }

        public final java.io.File getRoot() {
            return this.root;
        }
    }

    /* JADX INFO: compiled from: FileTreeWalk.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\"\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Lkotlin/io/FileTreeWalk$DirectoryState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootDir", "Ljava/io/File;", "(Ljava/io/File;)V", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static abstract class DirectoryState extends com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.WalkState {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DirectoryState(java.io.File rootDir) {
            super(rootDir);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(rootDir, "rootDir");
            if (!com.android.server.permission.jarjar.kotlin._Assertions.ENABLED) {
                return;
            }
            boolean zIsDirectory = rootDir.isDirectory();
            if (com.android.server.permission.jarjar.kotlin._Assertions.ENABLED && !zIsDirectory) {
                throw new java.lang.AssertionError("rootDir must be verified to be directory beforehand.");
            }
        }
    }

    /* JADX INFO: compiled from: FileTreeWalk.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0003\r\u000e\u000fB\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0007\u001a\u00020\bH\u0014J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002H\u0002J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0082\u0010R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;", "Lkotlin/collections/AbstractIterator;", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk;)V", "state", "Ljava/util/ArrayDeque;", "Lkotlin/io/FileTreeWalk$WalkState;", "computeNext", "", "directoryState", "Lkotlin/io/FileTreeWalk$DirectoryState;", "root", "gotoNext", "BottomUpDirectoryState", "SingleFileState", "TopDownDirectoryState", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class FileTreeWalkIterator extends com.android.server.permission.jarjar.kotlin.collections.AbstractIterator<java.io.File> {
        private final java.util.ArrayDeque<com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.WalkState> state = new java.util.ArrayDeque<>();

        /* JADX INFO: compiled from: FileTreeWalk.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[com.android.server.permission.jarjar.kotlin.io.FileWalkDirection.values().length];
                try {
                    iArr[com.android.server.permission.jarjar.kotlin.io.FileWalkDirection.TOP_DOWN.ordinal()] = 1;
                } catch (java.lang.NoSuchFieldError e) {
                }
                try {
                    iArr[com.android.server.permission.jarjar.kotlin.io.FileWalkDirection.BOTTOM_UP.ordinal()] = 2;
                } catch (java.lang.NoSuchFieldError e2) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        public FileTreeWalkIterator() {
            if (com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this.start.isDirectory()) {
                this.state.push(directoryState(com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this.start));
            } else if (com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this.start.isFile()) {
                this.state.push(new com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator.SingleFileState(this, com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this.start));
            } else {
                done();
            }
        }

        @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractIterator
        protected void computeNext() {
            java.io.File nextFile = gotoNext();
            if (nextFile != null) {
                setNext(nextFile);
            } else {
                done();
            }
        }

        private final com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.DirectoryState directoryState(java.io.File root) {
            switch (com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator.WhenMappings.$EnumSwitchMapping$0[com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this.direction.ordinal()]) {
                case 1:
                    return new com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator.TopDownDirectoryState(this, root);
                case 2:
                    return new com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator.BottomUpDirectoryState(this, root);
                default:
                    throw new com.android.server.permission.jarjar.kotlin.NoWhenBranchMatchedException();
            }
        }

        private final java.io.File gotoNext() {
            java.io.File file;
            while (true) {
                com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.WalkState topState = this.state.peek();
                if (topState == null) {
                    return null;
                }
                file = topState.step();
                if (file == null) {
                    this.state.pop();
                } else {
                    if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(file, topState.getRoot()) || !file.isDirectory() || this.state.size() >= com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this.maxDepth) {
                        break;
                    }
                    this.state.push(directoryState(file));
                }
            }
            return file;
        }

        /* JADX INFO: compiled from: FileTreeWalk.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\r\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\nX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$BottomUpDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "failed", "", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", "step", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
        private final class BottomUpDirectoryState extends com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.DirectoryState {
            private boolean failed;
            private int fileIndex;
            private java.io.File[] fileList;
            private boolean rootVisited;
            final /* synthetic */ com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public BottomUpDirectoryState(com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator this$0, java.io.File rootDir) {
                super(rootDir);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(rootDir, "rootDir");
                this.this$0 = this$0;
            }

            @Override // com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.WalkState
            public java.io.File step() {
                if (!this.failed && this.fileList == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 function1 = com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this.onEnter;
                    boolean z = false;
                    if (function1 != null && !((java.lang.Boolean) function1.invoke(getRoot())).booleanValue()) {
                        z = true;
                    }
                    if (z) {
                        return null;
                    }
                    this.fileList = getRoot().listFiles();
                    if (this.fileList == null) {
                        com.android.server.permission.jarjar.kotlin.jvm.functions.Function2 function2 = com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this.onFail;
                        if (function2 != null) {
                            function2.invoke(getRoot(), new com.android.server.permission.jarjar.kotlin.io.AccessDeniedException(getRoot(), null, "Cannot list files in a directory", 2, null));
                        }
                        this.failed = true;
                    }
                }
                if (this.fileList != null) {
                    int i = this.fileIndex;
                    java.io.File[] fileArr = this.fileList;
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(fileArr);
                    if (i < fileArr.length) {
                        java.io.File[] fileArr2 = this.fileList;
                        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(fileArr2);
                        int i2 = this.fileIndex;
                        this.fileIndex = i2 + 1;
                        return fileArr2[i2];
                    }
                }
                if (this.rootVisited) {
                    com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 function12 = com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this.onLeave;
                    if (function12 != null) {
                        function12.invoke(getRoot());
                    }
                    return null;
                }
                this.rootVisited = true;
                return getRoot();
            }
        }

        /* JADX INFO: compiled from: FileTreeWalk.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\f\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$TopDownDirectoryState;", "Lkotlin/io/FileTreeWalk$DirectoryState;", "rootDir", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "fileIndex", "", "fileList", "", "[Ljava/io/File;", "rootVisited", "", "step", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
        private final class TopDownDirectoryState extends com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.DirectoryState {
            private int fileIndex;
            private java.io.File[] fileList;
            private boolean rootVisited;
            final /* synthetic */ com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public TopDownDirectoryState(com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator this$0, java.io.File rootDir) {
                super(rootDir);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(rootDir, "rootDir");
                this.this$0 = this$0;
            }

            /* JADX WARN: Code restructure failed: missing block: B:32:0x0089, code lost:
            
                if (r0.length == 0) goto L33;
             */
            @Override // com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.WalkState
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public java.io.File step() {
                /*
                    r10 = this;
                    boolean r0 = r10.rootVisited
                    r1 = 0
                    if (r0 != 0) goto L2c
                    com.android.server.permission.jarjar.kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r10.this$0
                    com.android.server.permission.jarjar.kotlin.io.FileTreeWalk r0 = com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this
                    com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 r0 = com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.access$getOnEnter$p(r0)
                    r2 = 0
                    r3 = 1
                    if (r0 == 0) goto L22
                    java.io.File r4 = r10.getRoot()
                    java.lang.Object r0 = r0.invoke(r4)
                    java.lang.Boolean r0 = (java.lang.Boolean) r0
                    boolean r0 = r0.booleanValue()
                    if (r0 != 0) goto L22
                    r2 = r3
                L22:
                    if (r2 == 0) goto L25
                    return r1
                L25:
                    r10.rootVisited = r3
                    java.io.File r0 = r10.getRoot()
                    return r0
                L2c:
                    java.io.File[] r0 = r10.fileList
                    if (r0 == 0) goto L4d
                    int r0 = r10.fileIndex
                    java.io.File[] r2 = r10.fileList
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(r2)
                    int r2 = r2.length
                    if (r0 >= r2) goto L3b
                    goto L4d
                L3b:
                    com.android.server.permission.jarjar.kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r10.this$0
                    com.android.server.permission.jarjar.kotlin.io.FileTreeWalk r0 = com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this
                    com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 r0 = com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.access$getOnLeave$p(r0)
                    if (r0 == 0) goto L4c
                    java.io.File r2 = r10.getRoot()
                    r0.invoke(r2)
                L4c:
                    return r1
                L4d:
                    java.io.File[] r0 = r10.fileList
                    if (r0 != 0) goto L9d
                    java.io.File r0 = r10.getRoot()
                    java.io.File[] r0 = r0.listFiles()
                    r10.fileList = r0
                    java.io.File[] r0 = r10.fileList
                    if (r0 != 0) goto L7f
                    com.android.server.permission.jarjar.kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r10.this$0
                    com.android.server.permission.jarjar.kotlin.io.FileTreeWalk r0 = com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this
                    com.android.server.permission.jarjar.kotlin.jvm.functions.Function2 r0 = com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.access$getOnFail$p(r0)
                    if (r0 == 0) goto L7f
                    java.io.File r2 = r10.getRoot()
                    com.android.server.permission.jarjar.kotlin.io.AccessDeniedException r9 = new com.android.server.permission.jarjar.kotlin.io.AccessDeniedException
                    java.io.File r4 = r10.getRoot()
                    r7 = 2
                    r8 = 0
                    r5 = 0
                    java.lang.String r6 = "Cannot list files in a directory"
                    r3 = r9
                    r3.<init>(r4, r5, r6, r7, r8)
                    r0.invoke(r2, r9)
                L7f:
                    java.io.File[] r0 = r10.fileList
                    if (r0 == 0) goto L8b
                    java.io.File[] r0 = r10.fileList
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
                    int r0 = r0.length
                    if (r0 != 0) goto L9d
                L8b:
                    com.android.server.permission.jarjar.kotlin.io.FileTreeWalk$FileTreeWalkIterator r0 = r10.this$0
                    com.android.server.permission.jarjar.kotlin.io.FileTreeWalk r0 = com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.this
                    com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 r0 = com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.access$getOnLeave$p(r0)
                    if (r0 == 0) goto L9c
                    java.io.File r2 = r10.getRoot()
                    r0.invoke(r2)
                L9c:
                    return r1
                L9d:
                    java.io.File[] r0 = r10.fileList
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
                    int r1 = r10.fileIndex
                    int r2 = r1 + 1
                    r10.fileIndex = r2
                    r0 = r0[r1]
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator.TopDownDirectoryState.step():java.io.File");
            }
        }

        /* JADX INFO: compiled from: FileTreeWalk.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\n\u0010\u0007\u001a\u0004\u0018\u00010\u0003H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlin/io/FileTreeWalk$FileTreeWalkIterator$SingleFileState;", "Lkotlin/io/FileTreeWalk$WalkState;", "rootFile", "Ljava/io/File;", "(Lkotlin/io/FileTreeWalk$FileTreeWalkIterator;Ljava/io/File;)V", "visited", "", "step", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
        private final class SingleFileState extends com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.WalkState {
            final /* synthetic */ com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator this$0;
            private boolean visited;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public SingleFileState(com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.FileTreeWalkIterator this$0, java.io.File rootFile) {
                super(rootFile);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(rootFile, "rootFile");
                this.this$0 = this$0;
                if (!com.android.server.permission.jarjar.kotlin._Assertions.ENABLED) {
                    return;
                }
                boolean zIsFile = rootFile.isFile();
                if (com.android.server.permission.jarjar.kotlin._Assertions.ENABLED && !zIsFile) {
                    throw new java.lang.AssertionError("rootFile must be verified to be file beforehand.");
                }
            }

            @Override // com.android.server.permission.jarjar.kotlin.io.FileTreeWalk.WalkState
            public java.io.File step() {
                if (this.visited) {
                    return null;
                }
                this.visited = true;
                return getRoot();
            }
        }
    }

    public final com.android.server.permission.jarjar.kotlin.io.FileTreeWalk onEnter(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.io.File, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "function");
        return new com.android.server.permission.jarjar.kotlin.io.FileTreeWalk(this.start, this.direction, function1, this.onLeave, this.onFail, this.maxDepth);
    }

    public final com.android.server.permission.jarjar.kotlin.io.FileTreeWalk onLeave(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.io.File, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "function");
        return new com.android.server.permission.jarjar.kotlin.io.FileTreeWalk(this.start, this.direction, this.onEnter, function1, this.onFail, this.maxDepth);
    }

    public final com.android.server.permission.jarjar.kotlin.io.FileTreeWalk onFail(com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.io.File, ? super java.io.IOException, com.android.server.permission.jarjar.kotlin.Unit> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "function");
        return new com.android.server.permission.jarjar.kotlin.io.FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, function2, this.maxDepth);
    }

    public final com.android.server.permission.jarjar.kotlin.io.FileTreeWalk maxDepth(int depth) {
        if (depth <= 0) {
            throw new java.lang.IllegalArgumentException("depth must be positive, but was " + depth + '.');
        }
        return new com.android.server.permission.jarjar.kotlin.io.FileTreeWalk(this.start, this.direction, this.onEnter, this.onLeave, this.onFail, depth);
    }
}
