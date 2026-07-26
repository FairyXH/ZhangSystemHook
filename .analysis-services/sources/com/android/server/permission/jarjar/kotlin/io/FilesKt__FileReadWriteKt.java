package com.android.server.permission.jarjar.kotlin.io;

/* JADX INFO: compiled from: FileReadWrite.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000z\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010\u0005\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a!\u0010\n\u001a\u00020\u000b*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001a!\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u0087\b\u001aB\u0010\u0010\u001a\u00020\u0001*\u00020\u000226\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001aJ\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\r26\u0010\u0011\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0015\u0012\u0013\u0012\u00110\r¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u0016\u0012\u0004\u0012\u00020\u00010\u0012\u001a7\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0007¢\u0006\f\b\u0013\u0012\b\b\u0014\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u00010\u0019\u001a\r\u0010\u001b\u001a\u00020\u001c*\u00020\u0002H\u0087\b\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0017\u0010\u001f\u001a\u00020 *\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a\n\u0010!\u001a\u00020\u0004*\u00020\u0002\u001a\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00070#*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0014\u0010$\u001a\u00020\u0007*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010%\u001a\u00020&*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u001a?\u0010'\u001a\u0002H(\"\u0004\b\u0000\u0010(*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\t2\u0018\u0010)\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070*\u0012\u0004\u0012\u0002H(0\u0019H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010+\u001a\u0012\u0010,\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u001c\u0010-\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t\u001a\u0017\u0010.\u001a\u00020/*\u00020\u00022\b\b\u0002\u0010\b\u001a\u00020\tH\u0087\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u00060"}, d2 = {"appendBytes", "", "Ljava/io/File;", "array", "", "appendText", "text", "", "charset", "Ljava/nio/charset/Charset;", "bufferedReader", "Ljava/io/BufferedReader;", "bufferSize", "", "bufferedWriter", "Ljava/io/BufferedWriter;", "forEachBlock", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "buffer", "bytesRead", "blockSize", "forEachLine", "Lkotlin/Function1;", "line", "inputStream", "Ljava/io/FileInputStream;", "outputStream", "Ljava/io/FileOutputStream;", "printWriter", "Ljava/io/PrintWriter;", "readBytes", "readLines", "", "readText", "reader", "Ljava/io/InputStreamReader;", "useLines", "T", "block", "Lkotlin/sequences/Sequence;", "(Ljava/io/File;Ljava/nio/charset/Charset;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "writeBytes", "writeText", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/io/FilesKt")
class FilesKt__FileReadWriteKt extends com.android.server.permission.jarjar.kotlin.io.FilesKt__FilePathComponentsKt {
    static /* synthetic */ java.io.InputStreamReader reader$default(java.io.File $this$reader_u24default, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reader_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.io.InputStreamReader(new java.io.FileInputStream($this$reader_u24default), charset);
    }

    private static final java.io.InputStreamReader reader(java.io.File $this$reader, java.nio.charset.Charset charset) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reader, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.io.InputStreamReader(new java.io.FileInputStream($this$reader), charset);
    }

    static /* synthetic */ java.io.BufferedReader bufferedReader$default(java.io.File $this$bufferedReader_u24default, java.nio.charset.Charset charset, int bufferSize, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        if ((i & 2) != 0) {
            bufferSize = 8192;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedReader_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.Reader inputStreamReader = new java.io.InputStreamReader(new java.io.FileInputStream($this$bufferedReader_u24default), charset);
        return inputStreamReader instanceof java.io.BufferedReader ? (java.io.BufferedReader) inputStreamReader : new java.io.BufferedReader(inputStreamReader, bufferSize);
    }

    private static final java.io.BufferedReader bufferedReader(java.io.File $this$bufferedReader, java.nio.charset.Charset charset, int bufferSize) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedReader, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.Reader inputStreamReader = new java.io.InputStreamReader(new java.io.FileInputStream($this$bufferedReader), charset);
        return inputStreamReader instanceof java.io.BufferedReader ? (java.io.BufferedReader) inputStreamReader : new java.io.BufferedReader(inputStreamReader, bufferSize);
    }

    static /* synthetic */ java.io.OutputStreamWriter writer$default(java.io.File $this$writer_u24default, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writer_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.io.OutputStreamWriter(new java.io.FileOutputStream($this$writer_u24default), charset);
    }

    private static final java.io.OutputStreamWriter writer(java.io.File $this$writer, java.nio.charset.Charset charset) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writer, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.io.OutputStreamWriter(new java.io.FileOutputStream($this$writer), charset);
    }

    static /* synthetic */ java.io.BufferedWriter bufferedWriter$default(java.io.File $this$bufferedWriter_u24default, java.nio.charset.Charset charset, int bufferSize, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        if ((i & 2) != 0) {
            bufferSize = 8192;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedWriter_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.Writer outputStreamWriter = new java.io.OutputStreamWriter(new java.io.FileOutputStream($this$bufferedWriter_u24default), charset);
        return outputStreamWriter instanceof java.io.BufferedWriter ? (java.io.BufferedWriter) outputStreamWriter : new java.io.BufferedWriter(outputStreamWriter, bufferSize);
    }

    private static final java.io.BufferedWriter bufferedWriter(java.io.File $this$bufferedWriter, java.nio.charset.Charset charset, int bufferSize) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedWriter, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.Writer outputStreamWriter = new java.io.OutputStreamWriter(new java.io.FileOutputStream($this$bufferedWriter), charset);
        return outputStreamWriter instanceof java.io.BufferedWriter ? (java.io.BufferedWriter) outputStreamWriter : new java.io.BufferedWriter(outputStreamWriter, bufferSize);
    }

    static /* synthetic */ java.io.PrintWriter printWriter$default(java.io.File $this$printWriter_u24default, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$printWriter_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.Writer outputStreamWriter = new java.io.OutputStreamWriter(new java.io.FileOutputStream($this$printWriter_u24default), charset);
        return new java.io.PrintWriter(outputStreamWriter instanceof java.io.BufferedWriter ? (java.io.BufferedWriter) outputStreamWriter : new java.io.BufferedWriter(outputStreamWriter, 8192));
    }

    private static final java.io.PrintWriter printWriter(java.io.File $this$printWriter, java.nio.charset.Charset charset) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$printWriter, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.Writer outputStreamWriter = new java.io.OutputStreamWriter(new java.io.FileOutputStream($this$printWriter), charset);
        return new java.io.PrintWriter(outputStreamWriter instanceof java.io.BufferedWriter ? (java.io.BufferedWriter) outputStreamWriter : new java.io.BufferedWriter(outputStreamWriter, 8192));
    }

    public static final byte[] readBytes(java.io.File $this$readBytes) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readBytes, "<this>");
        java.io.FileInputStream fileInputStream = new java.io.FileInputStream($this$readBytes);
        try {
            java.io.FileInputStream input = fileInputStream;
            int offset = 0;
            long length = $this$readBytes.length();
            if (length > 2147483647L) {
                throw new java.lang.OutOfMemoryError("File " + $this$readBytes + " is too big (" + length + " bytes) to fit in memory.");
            }
            int remaining = (int) length;
            byte[] result = new byte[remaining];
            while (remaining > 0) {
                int read = input.read(result, offset, remaining);
                if (read < 0) {
                    break;
                }
                remaining -= read;
                offset += read;
            }
            if (remaining > 0) {
                byte[] bArrCopyOf = java.util.Arrays.copyOf(result, offset);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                result = bArrCopyOf;
            } else {
                int extraByte = input.read();
                if (extraByte != -1) {
                    com.android.server.permission.jarjar.kotlin.io.ExposingBufferByteArrayOutputStream extra = new com.android.server.permission.jarjar.kotlin.io.ExposingBufferByteArrayOutputStream(8193);
                    extra.write(extraByte);
                    com.android.server.permission.jarjar.kotlin.io.ByteStreamsKt.copyTo$default(input, extra, 0, 2, null);
                    int resultingSize = result.length + extra.size();
                    if (resultingSize < 0) {
                        throw new java.lang.OutOfMemoryError("File " + $this$readBytes + " is too big to fit in memory.");
                    }
                    byte[] buffer = extra.getBuffer();
                    byte[] bArrCopyOf2 = java.util.Arrays.copyOf(result, resultingSize);
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bArrCopyOf2, "copyOf(...)");
                    result = com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(buffer, bArrCopyOf2, result.length, 0, extra.size());
                }
            }
            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileInputStream, null);
            return result;
        } catch (java.lang.Throwable th) {
            try {
                throw th;
            } catch (java.lang.Throwable th2) {
                com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileInputStream, th);
                throw th2;
            }
        }
    }

    public static final void writeBytes(java.io.File $this$writeBytes, byte[] array) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writeBytes, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream($this$writeBytes);
        try {
            java.io.FileOutputStream it = fileOutputStream;
            it.write(array);
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStream, null);
        } finally {
        }
    }

    public static final void appendBytes(java.io.File $this$appendBytes, byte[] array) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendBytes, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream($this$appendBytes, true);
        try {
            java.io.FileOutputStream it = fileOutputStream;
            it.write(array);
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStream, null);
        } finally {
        }
    }

    public static final java.lang.String readText(java.io.File $this$readText, java.nio.charset.Charset charset) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readText, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.InputStreamReader inputStreamReader = new java.io.InputStreamReader(new java.io.FileInputStream($this$readText), charset);
        try {
            java.io.InputStreamReader it = inputStreamReader;
            java.lang.String text = com.android.server.permission.jarjar.kotlin.io.TextStreamsKt.readText(it);
            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamReader, null);
            return text;
        } finally {
        }
    }

    public static /* synthetic */ java.lang.String readText$default(java.io.File file, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        return com.android.server.permission.jarjar.kotlin.io.FilesKt.readText(file, charset);
    }

    public static final void writeText(java.io.File $this$writeText, java.lang.String text, java.nio.charset.Charset charset) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writeText, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(text, "text");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        byte[] bytes = text.getBytes(charset);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        com.android.server.permission.jarjar.kotlin.io.FilesKt.writeBytes($this$writeText, bytes);
    }

    public static /* synthetic */ void writeText$default(java.io.File file, java.lang.String str, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        com.android.server.permission.jarjar.kotlin.io.FilesKt.writeText(file, str, charset);
    }

    public static final void appendText(java.io.File $this$appendText, java.lang.String text, java.nio.charset.Charset charset) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendText, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(text, "text");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        byte[] bytes = text.getBytes(charset);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        com.android.server.permission.jarjar.kotlin.io.FilesKt.appendBytes($this$appendText, bytes);
    }

    public static /* synthetic */ void appendText$default(java.io.File file, java.lang.String str, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        com.android.server.permission.jarjar.kotlin.io.FilesKt.appendText(file, str, charset);
    }

    public static final void forEachBlock(java.io.File $this$forEachBlock, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super byte[], ? super java.lang.Integer, com.android.server.permission.jarjar.kotlin.Unit> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$forEachBlock, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "action");
        com.android.server.permission.jarjar.kotlin.io.FilesKt.forEachBlock($this$forEachBlock, 4096, function2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4, types: [byte[], java.lang.Object] */
    public static final void forEachBlock(java.io.File $this$forEachBlock, int blockSize, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super byte[], ? super java.lang.Integer, com.android.server.permission.jarjar.kotlin.Unit> function2) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$forEachBlock, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "action");
        ?? r0 = new byte[com.android.server.permission.jarjar.kotlin.ranges.RangesKt.coerceAtLeast(blockSize, 512)];
        java.io.FileInputStream fileInputStream = new java.io.FileInputStream($this$forEachBlock);
        try {
            java.io.FileInputStream fileInputStream2 = fileInputStream;
            while (true) {
                int size = fileInputStream2.read(r0);
                if (size <= 0) {
                    com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                    com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileInputStream, null);
                    return;
                }
                function2.invoke(r0, java.lang.Integer.valueOf(size));
            }
        } finally {
        }
    }

    public static /* synthetic */ void forEachLine$default(java.io.File file, java.nio.charset.Charset charset, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 function1, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        com.android.server.permission.jarjar.kotlin.io.FilesKt.forEachLine(file, charset, function1);
    }

    public static final void forEachLine(java.io.File $this$forEachLine, java.nio.charset.Charset charset, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.lang.String, com.android.server.permission.jarjar.kotlin.Unit> function1) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$forEachLine, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        com.android.server.permission.jarjar.kotlin.io.TextStreamsKt.forEachLine(new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream($this$forEachLine), charset)), function1);
    }

    private static final java.io.FileInputStream inputStream(java.io.File $this$inputStream) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$inputStream, "<this>");
        return new java.io.FileInputStream($this$inputStream);
    }

    private static final java.io.FileOutputStream outputStream(java.io.File $this$outputStream) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$outputStream, "<this>");
        return new java.io.FileOutputStream($this$outputStream);
    }

    public static /* synthetic */ java.util.List readLines$default(java.io.File file, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        return com.android.server.permission.jarjar.kotlin.io.FilesKt.readLines(file, charset);
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.io.FilesKt__FileReadWriteKt$readLines$1, reason: invalid class name */
    /* JADX INFO: compiled from: FileReadWrite.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
    static final class AnonymousClass1 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<java.lang.String, com.android.server.permission.jarjar.kotlin.Unit> {
        final /* synthetic */ java.util.ArrayList<java.lang.String> $result;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(java.util.ArrayList<java.lang.String> arrayList) {
            super(1);
            this.$result = arrayList;
        }

        @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.Unit invoke(java.lang.String str) {
            invoke2(str);
            return com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(java.lang.String it) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
            this.$result.add(it);
        }
    }

    public static final java.util.List<java.lang.String> readLines(java.io.File $this$readLines, java.nio.charset.Charset charset) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readLines, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.util.ArrayList result = new java.util.ArrayList();
        com.android.server.permission.jarjar.kotlin.io.FilesKt.forEachLine($this$readLines, charset, new com.android.server.permission.jarjar.kotlin.io.FilesKt__FileReadWriteKt.AnonymousClass1(result));
        return result;
    }

    public static /* synthetic */ java.lang.Object useLines$default(java.io.File $this$useLines_u24default, java.nio.charset.Charset charset, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 block, int i, java.lang.Object obj) throws java.io.IOException {
        if ((i & 1) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$useLines_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        java.io.Reader inputStreamReader = new java.io.InputStreamReader(new java.io.FileInputStream($this$useLines_u24default), charset);
        java.io.BufferedReader bufferedReader = inputStreamReader instanceof java.io.BufferedReader ? (java.io.BufferedReader) inputStreamReader : new java.io.BufferedReader(inputStreamReader, 8192);
        try {
            java.io.BufferedReader it = bufferedReader;
            java.lang.Object objInvoke = block.invoke(com.android.server.permission.jarjar.kotlin.io.TextStreamsKt.lineSequence(it));
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
            if (com.android.server.permission.jarjar.kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(bufferedReader, null);
            } else {
                bufferedReader.close();
            }
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
            return objInvoke;
        } finally {
        }
    }

    public static final <T> T useLines(java.io.File $this$useLines, java.nio.charset.Charset charset, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.sequences.Sequence<java.lang.String>, ? extends T> function1) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$useLines, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "block");
        java.io.Reader inputStreamReader = new java.io.InputStreamReader(new java.io.FileInputStream($this$useLines), charset);
        java.io.BufferedReader bufferedReader = inputStreamReader instanceof java.io.BufferedReader ? (java.io.BufferedReader) inputStreamReader : new java.io.BufferedReader(inputStreamReader, 8192);
        try {
            java.io.BufferedReader it = bufferedReader;
            T tInvoke = function1.invoke(com.android.server.permission.jarjar.kotlin.io.TextStreamsKt.lineSequence(it));
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
            if (com.android.server.permission.jarjar.kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(bufferedReader, null);
            } else {
                bufferedReader.close();
            }
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
            return tInvoke;
        } catch (java.lang.Throwable th) {
            try {
                throw th;
            } catch (java.lang.Throwable th2) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                if (com.android.server.permission.jarjar.kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(bufferedReader, th);
                } else {
                    try {
                        bufferedReader.close();
                    } catch (java.lang.Throwable th3) {
                    }
                }
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th2;
            }
        }
    }
}
