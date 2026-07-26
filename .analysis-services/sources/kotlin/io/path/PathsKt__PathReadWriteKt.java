package kotlin.io.path;

/* JADX INFO: compiled from: PathReadWrite.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0082\u0001\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a%\u0010\u0005\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a%\u0010\u0005\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u000b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u001e\u0010\f\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a:\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010\u0015\u001a:\u0010\u0016\u001a\u00020\u0017*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010\u0018\u001a=\u0010\u0019\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2!\u0010\u001a\u001a\u001d\u0012\u0013\u0012\u00110\u001c¢\u0006\f\b\u001d\u0012\b\b\u001e\u0012\u0004\b\b(\u001f\u0012\u0004\u0012\u00020\u00010\u001bH\u0087\bø\u0001\u0000\u001a&\u0010 \u001a\u00020!*\u00020\u00022\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010\"\u001a&\u0010#\u001a\u00020$*\u00020\u00022\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010%\u001a\r\u0010&\u001a\u00020\u0004*\u00020\u0002H\u0087\b\u001a\u001d\u0010'\u001a\b\u0012\u0004\u0012\u00020\u001c0(*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0016\u0010)\u001a\u00020\u001c*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a0\u0010*\u001a\u00020+*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010,\u001a?\u0010-\u001a\u0002H.\"\u0004\b\u0000\u0010.*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0018\u0010/\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\u000b\u0012\u0004\u0012\u0002H.0\u001bH\u0087\bø\u0001\u0000¢\u0006\u0002\u00100\u001a.\u00101\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u00102\u001a>\u00103\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u00104\u001a>\u00103\u001a\u00020\u0002*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u000b2\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u00105\u001a7\u00106\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0007¢\u0006\u0002\u00107\u001a0\u00108\u001a\u000209*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\n2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0087\b¢\u0006\u0002\u0010:\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006;"}, d2 = {"appendBytes", "", "Ljava/nio/file/Path;", "array", "", "appendLines", "lines", "", "", "charset", "Ljava/nio/charset/Charset;", "Lkotlin/sequences/Sequence;", "appendText", "text", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "options", "", "Ljava/nio/file/OpenOption;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;I[Ljava/nio/file/OpenOption;)Ljava/io/BufferedReader;", "bufferedWriter", "Ljava/io/BufferedWriter;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;I[Ljava/nio/file/OpenOption;)Ljava/io/BufferedWriter;", "forEachLine", "action", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "line", "inputStream", "Ljava/io/InputStream;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Ljava/io/InputStream;", "outputStream", "Ljava/io/OutputStream;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Ljava/io/OutputStream;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/io/InputStreamReader;", "useLines", "T", "block", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "(Ljava/nio/file/Path;[B[Ljava/nio/file/OpenOption;)V", "writeLines", "(Ljava/nio/file/Path;Ljava/lang/Iterable;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/nio/file/Path;", "(Ljava/nio/file/Path;Lkotlin/sequences/Sequence;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/nio/file/Path;", "writeText", "(Ljava/nio/file/Path;Ljava/lang/CharSequence;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)V", "writer", "Ljava/io/OutputStreamWriter;", "(Ljava/nio/file/Path;Ljava/nio/charset/Charset;[Ljava/nio/file/OpenOption;)Ljava/io/OutputStreamWriter;", "kotlin-stdlib-jdk7"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/io/path/PathsKt")
class PathsKt__PathReadWriteKt {
    static /* synthetic */ java.io.InputStreamReader reader$default(java.nio.file.Path $this$reader_u24default, java.nio.charset.Charset charset, java.nio.file.OpenOption[] options, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reader_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        return new java.io.InputStreamReader(java.nio.file.Files.newInputStream($this$reader_u24default, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length)), charset);
    }

    private static final java.io.InputStreamReader reader(java.nio.file.Path $this$reader, java.nio.charset.Charset charset, java.nio.file.OpenOption... options) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reader, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        return new java.io.InputStreamReader(java.nio.file.Files.newInputStream($this$reader, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length)), charset);
    }

    static /* synthetic */ java.io.BufferedReader bufferedReader$default(java.nio.file.Path $this$bufferedReader_u24default, java.nio.charset.Charset charset, int bufferSize, java.nio.file.OpenOption[] options, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        if ((i & 2) != 0) {
            bufferSize = 8192;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedReader_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        return new java.io.BufferedReader(new java.io.InputStreamReader(java.nio.file.Files.newInputStream($this$bufferedReader_u24default, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length)), charset), bufferSize);
    }

    private static final java.io.BufferedReader bufferedReader(java.nio.file.Path $this$bufferedReader, java.nio.charset.Charset charset, int bufferSize, java.nio.file.OpenOption... options) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedReader, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        return new java.io.BufferedReader(new java.io.InputStreamReader(java.nio.file.Files.newInputStream($this$bufferedReader, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length)), charset), bufferSize);
    }

    static /* synthetic */ java.io.OutputStreamWriter writer$default(java.nio.file.Path $this$writer_u24default, java.nio.charset.Charset charset, java.nio.file.OpenOption[] options, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writer_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        return new java.io.OutputStreamWriter(java.nio.file.Files.newOutputStream($this$writer_u24default, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length)), charset);
    }

    private static final java.io.OutputStreamWriter writer(java.nio.file.Path $this$writer, java.nio.charset.Charset charset, java.nio.file.OpenOption... options) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writer, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        return new java.io.OutputStreamWriter(java.nio.file.Files.newOutputStream($this$writer, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length)), charset);
    }

    static /* synthetic */ java.io.BufferedWriter bufferedWriter$default(java.nio.file.Path $this$bufferedWriter_u24default, java.nio.charset.Charset charset, int bufferSize, java.nio.file.OpenOption[] options, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        if ((i & 2) != 0) {
            bufferSize = 8192;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedWriter_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        return new java.io.BufferedWriter(new java.io.OutputStreamWriter(java.nio.file.Files.newOutputStream($this$bufferedWriter_u24default, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length)), charset), bufferSize);
    }

    private static final java.io.BufferedWriter bufferedWriter(java.nio.file.Path $this$bufferedWriter, java.nio.charset.Charset charset, int bufferSize, java.nio.file.OpenOption... options) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedWriter, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        return new java.io.BufferedWriter(new java.io.OutputStreamWriter(java.nio.file.Files.newOutputStream($this$bufferedWriter, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length)), charset), bufferSize);
    }

    private static final byte[] readBytes(java.nio.file.Path $this$readBytes) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readBytes, "<this>");
        byte[] allBytes = java.nio.file.Files.readAllBytes($this$readBytes);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(allBytes, "readAllBytes(...)");
        return allBytes;
    }

    private static final void writeBytes(java.nio.file.Path $this$writeBytes, byte[] array, java.nio.file.OpenOption... options) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writeBytes, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        java.nio.file.Files.write($this$writeBytes, array, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length));
    }

    private static final void appendBytes(java.nio.file.Path $this$appendBytes, byte[] array) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendBytes, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        java.nio.file.Files.write($this$appendBytes, array, java.nio.file.StandardOpenOption.APPEND);
    }

    public static /* synthetic */ java.lang.String readText$default(java.nio.file.Path path, java.nio.charset.Charset charset, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        return kotlin.io.path.PathsKt.readText(path, charset);
    }

    public static final java.lang.String readText(java.nio.file.Path $this$readText, java.nio.charset.Charset charset) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readText, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.InputStreamReader inputStreamReader = new java.io.InputStreamReader(java.nio.file.Files.newInputStream($this$readText, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(new java.nio.file.OpenOption[0], 0)), charset);
        try {
            java.io.InputStreamReader it = inputStreamReader;
            java.lang.String text = kotlin.io.TextStreamsKt.readText(it);
            kotlin.io.CloseableKt.closeFinally(inputStreamReader, null);
            return text;
        } finally {
        }
    }

    public static /* synthetic */ void writeText$default(java.nio.file.Path path, java.lang.CharSequence charSequence, java.nio.charset.Charset charset, java.nio.file.OpenOption[] openOptionArr, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 2) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.io.path.PathsKt.writeText(path, charSequence, charset, openOptionArr);
    }

    public static final void writeText(java.nio.file.Path $this$writeText, java.lang.CharSequence text, java.nio.charset.Charset charset, java.nio.file.OpenOption... options) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writeText, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(text, "text");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        java.io.OutputStream outputStreamNewOutputStream = java.nio.file.Files.newOutputStream($this$writeText, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(outputStreamNewOutputStream, "newOutputStream(...)");
        java.io.OutputStreamWriter outputStreamWriter = new java.io.OutputStreamWriter(outputStreamNewOutputStream, charset);
        try {
            java.io.OutputStreamWriter it = outputStreamWriter;
            it.append(text);
            kotlin.io.CloseableKt.closeFinally(outputStreamWriter, null);
        } finally {
        }
    }

    public static /* synthetic */ void appendText$default(java.nio.file.Path path, java.lang.CharSequence charSequence, java.nio.charset.Charset charset, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 2) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.io.path.PathsKt.appendText(path, charSequence, charset);
    }

    public static final void appendText(java.nio.file.Path $this$appendText, java.lang.CharSequence text, java.nio.charset.Charset charset) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendText, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(text, "text");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.OutputStream outputStreamNewOutputStream = java.nio.file.Files.newOutputStream($this$appendText, java.nio.file.StandardOpenOption.APPEND);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(outputStreamNewOutputStream, "newOutputStream(...)");
        java.io.OutputStreamWriter outputStreamWriter = new java.io.OutputStreamWriter(outputStreamNewOutputStream, charset);
        try {
            java.io.OutputStreamWriter it = outputStreamWriter;
            it.append(text);
            kotlin.io.CloseableKt.closeFinally(outputStreamWriter, null);
        } finally {
        }
    }

    static /* synthetic */ void forEachLine$default(java.nio.file.Path $this$forEachLine_u24default, java.nio.charset.Charset charset, kotlin.jvm.functions.Function1 action, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$forEachLine_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        java.io.Reader readerNewBufferedReader = java.nio.file.Files.newBufferedReader($this$forEachLine_u24default, charset);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(readerNewBufferedReader, "newBufferedReader(...)");
        java.io.Reader $this$useLines$iv = readerNewBufferedReader;
        java.io.BufferedReader bufferedReader = (java.io.BufferedReader) $this$useLines$iv;
        try {
            java.io.BufferedReader it$iv = bufferedReader;
            java.util.Iterator<java.lang.String> it = kotlin.io.TextStreamsKt.lineSequence(it$iv).iterator();
            while (it.hasNext()) {
                action.invoke(it.next());
            }
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                kotlin.io.CloseableKt.closeFinally(bufferedReader, null);
            } else {
                bufferedReader.close();
            }
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        } catch (java.lang.Throwable th) {
            try {
                throw th;
            } catch (java.lang.Throwable th2) {
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
                if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    kotlin.io.CloseableKt.closeFinally(bufferedReader, th);
                } else {
                    try {
                        bufferedReader.close();
                    } catch (java.lang.Throwable th3) {
                    }
                }
                kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th2;
            }
        }
    }

    private static final void forEachLine(java.nio.file.Path $this$forEachLine, java.nio.charset.Charset charset, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> action) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$forEachLine, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        java.io.Reader readerNewBufferedReader = java.nio.file.Files.newBufferedReader($this$forEachLine, charset);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(readerNewBufferedReader, "newBufferedReader(...)");
        java.io.Reader $this$useLines$iv = readerNewBufferedReader;
        java.io.BufferedReader bufferedReader = (java.io.BufferedReader) $this$useLines$iv;
        try {
            java.io.BufferedReader it$iv = bufferedReader;
            for (java.lang.Object element$iv : kotlin.io.TextStreamsKt.lineSequence(it$iv)) {
                action.invoke(element$iv);
            }
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                kotlin.io.CloseableKt.closeFinally(bufferedReader, null);
            } else {
                bufferedReader.close();
            }
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        } catch (java.lang.Throwable th) {
            try {
                throw th;
            } catch (java.lang.Throwable th2) {
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
                if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    kotlin.io.CloseableKt.closeFinally(bufferedReader, th);
                } else {
                    try {
                        bufferedReader.close();
                    } catch (java.lang.Throwable th3) {
                    }
                }
                kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th2;
            }
        }
    }

    private static final java.io.InputStream inputStream(java.nio.file.Path $this$inputStream, java.nio.file.OpenOption... options) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$inputStream, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        java.io.InputStream inputStreamNewInputStream = java.nio.file.Files.newInputStream($this$inputStream, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(inputStreamNewInputStream, "newInputStream(...)");
        return inputStreamNewInputStream;
    }

    private static final java.io.OutputStream outputStream(java.nio.file.Path $this$outputStream, java.nio.file.OpenOption... options) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$outputStream, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        java.io.OutputStream outputStreamNewOutputStream = java.nio.file.Files.newOutputStream($this$outputStream, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(outputStreamNewOutputStream, "newOutputStream(...)");
        return outputStreamNewOutputStream;
    }

    static /* synthetic */ java.util.List readLines$default(java.nio.file.Path $this$readLines_u24default, java.nio.charset.Charset charset, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readLines_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.util.List<java.lang.String> allLines = java.nio.file.Files.readAllLines($this$readLines_u24default, charset);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(allLines, "readAllLines(...)");
        return allLines;
    }

    private static final java.util.List<java.lang.String> readLines(java.nio.file.Path $this$readLines, java.nio.charset.Charset charset) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readLines, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.util.List<java.lang.String> allLines = java.nio.file.Files.readAllLines($this$readLines, charset);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(allLines, "readAllLines(...)");
        return allLines;
    }

    static /* synthetic */ java.lang.Object useLines$default(java.nio.file.Path $this$useLines_u24default, java.nio.charset.Charset charset, kotlin.jvm.functions.Function1 block, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$useLines_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        java.io.BufferedReader bufferedReaderNewBufferedReader = java.nio.file.Files.newBufferedReader($this$useLines_u24default, charset);
        try {
            java.io.BufferedReader it = bufferedReaderNewBufferedReader;
            kotlin.jvm.internal.Intrinsics.checkNotNull(it);
            java.lang.Object objInvoke = block.invoke(kotlin.io.TextStreamsKt.lineSequence(it));
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                kotlin.io.CloseableKt.closeFinally(bufferedReaderNewBufferedReader, null);
            } else if (bufferedReaderNewBufferedReader != null) {
                bufferedReaderNewBufferedReader.close();
            }
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
            return objInvoke;
        } catch (java.lang.Throwable th) {
            try {
                throw th;
            } catch (java.lang.Throwable th2) {
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
                if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    kotlin.io.CloseableKt.closeFinally(bufferedReaderNewBufferedReader, th);
                } else if (bufferedReaderNewBufferedReader != null) {
                    try {
                        bufferedReaderNewBufferedReader.close();
                    } catch (java.lang.Throwable th3) {
                    }
                }
                kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th2;
            }
        }
    }

    private static final <T> T useLines(java.nio.file.Path $this$useLines, java.nio.charset.Charset charset, kotlin.jvm.functions.Function1<? super kotlin.sequences.Sequence<java.lang.String>, ? extends T> block) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$useLines, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        java.io.BufferedReader bufferedReaderNewBufferedReader = java.nio.file.Files.newBufferedReader($this$useLines, charset);
        try {
            java.io.BufferedReader it = bufferedReaderNewBufferedReader;
            kotlin.jvm.internal.Intrinsics.checkNotNull(it);
            T tInvoke = block.invoke(kotlin.io.TextStreamsKt.lineSequence(it));
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                kotlin.io.CloseableKt.closeFinally(bufferedReaderNewBufferedReader, null);
            } else if (bufferedReaderNewBufferedReader != null) {
                bufferedReaderNewBufferedReader.close();
            }
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
            return tInvoke;
        } catch (java.lang.Throwable th) {
            try {
                throw th;
            } catch (java.lang.Throwable th2) {
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
                if (kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    kotlin.io.CloseableKt.closeFinally(bufferedReaderNewBufferedReader, th);
                } else if (bufferedReaderNewBufferedReader != null) {
                    try {
                        bufferedReaderNewBufferedReader.close();
                    } catch (java.lang.Throwable th3) {
                    }
                }
                kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th2;
            }
        }
    }

    static /* synthetic */ java.nio.file.Path writeLines$default(java.nio.file.Path $this$writeLines_u24default, java.lang.Iterable lines, java.nio.charset.Charset charset, java.nio.file.OpenOption[] options, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 2) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writeLines_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(lines, "lines");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        java.nio.file.Path pathWrite = java.nio.file.Files.write($this$writeLines_u24default, lines, charset, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(pathWrite, "write(...)");
        return pathWrite;
    }

    private static final java.nio.file.Path writeLines(java.nio.file.Path $this$writeLines, java.lang.Iterable<? extends java.lang.CharSequence> lines, java.nio.charset.Charset charset, java.nio.file.OpenOption... options) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writeLines, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(lines, "lines");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        java.nio.file.Path pathWrite = java.nio.file.Files.write($this$writeLines, lines, charset, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(pathWrite, "write(...)");
        return pathWrite;
    }

    static /* synthetic */ java.nio.file.Path writeLines$default(java.nio.file.Path $this$writeLines_u24default, kotlin.sequences.Sequence lines, java.nio.charset.Charset charset, java.nio.file.OpenOption[] options, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 2) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writeLines_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(lines, "lines");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        java.nio.file.Path pathWrite = java.nio.file.Files.write($this$writeLines_u24default, kotlin.sequences.SequencesKt.asIterable(lines), charset, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(pathWrite, "write(...)");
        return pathWrite;
    }

    private static final java.nio.file.Path writeLines(java.nio.file.Path $this$writeLines, kotlin.sequences.Sequence<? extends java.lang.CharSequence> lines, java.nio.charset.Charset charset, java.nio.file.OpenOption... options) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writeLines, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(lines, "lines");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(options, "options");
        java.nio.file.Path pathWrite = java.nio.file.Files.write($this$writeLines, kotlin.sequences.SequencesKt.asIterable(lines), charset, (java.nio.file.OpenOption[]) java.util.Arrays.copyOf(options, options.length));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(pathWrite, "write(...)");
        return pathWrite;
    }

    static /* synthetic */ java.nio.file.Path appendLines$default(java.nio.file.Path $this$appendLines_u24default, java.lang.Iterable lines, java.nio.charset.Charset charset, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 2) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLines_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(lines, "lines");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.nio.file.Path pathWrite = java.nio.file.Files.write($this$appendLines_u24default, lines, charset, java.nio.file.StandardOpenOption.APPEND);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(pathWrite, "write(...)");
        return pathWrite;
    }

    private static final java.nio.file.Path appendLines(java.nio.file.Path $this$appendLines, java.lang.Iterable<? extends java.lang.CharSequence> lines, java.nio.charset.Charset charset) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLines, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(lines, "lines");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.nio.file.Path pathWrite = java.nio.file.Files.write($this$appendLines, lines, charset, java.nio.file.StandardOpenOption.APPEND);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(pathWrite, "write(...)");
        return pathWrite;
    }

    static /* synthetic */ java.nio.file.Path appendLines$default(java.nio.file.Path $this$appendLines_u24default, kotlin.sequences.Sequence lines, java.nio.charset.Charset charset, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 2) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLines_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(lines, "lines");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.nio.file.Path pathWrite = java.nio.file.Files.write($this$appendLines_u24default, kotlin.sequences.SequencesKt.asIterable(lines), charset, java.nio.file.StandardOpenOption.APPEND);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(pathWrite, "write(...)");
        return pathWrite;
    }

    private static final java.nio.file.Path appendLines(java.nio.file.Path $this$appendLines, kotlin.sequences.Sequence<? extends java.lang.CharSequence> lines, java.nio.charset.Charset charset) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLines, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(lines, "lines");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.nio.file.Path pathWrite = java.nio.file.Files.write($this$appendLines, kotlin.sequences.SequencesKt.asIterable(lines), charset, java.nio.file.StandardOpenOption.APPEND);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(pathWrite, "write(...)");
        return pathWrite;
    }
}
